package com.astro.vajra.ephemeris.muhurta

import android.content.Context
import com.astro.vajra.core.common.Language
import com.astro.vajra.core.model.Nakshatra
import com.astro.vajra.core.model.Planet
import com.astro.vajra.core.model.PlanetPosition
import com.astro.vajra.core.model.VedicChart
import com.astro.vajra.core.model.ZodiacSign
import com.astro.vajra.ephemeris.VedicAstrologyUtils
import com.astro.vajra.ephemeris.muhurta.MuhurtaAstronomicalCalculator.calculateJulianDay
import com.astro.vajra.ephemeris.muhurta.MuhurtaAstronomicalCalculator.calculateSunriseSunsetJD
import com.astro.vajra.ephemeris.muhurta.MuhurtaAstronomicalCalculator.getPlanetLongitude
import com.astro.vajra.ephemeris.muhurta.MuhurtaAstronomicalCalculator.getPlanetSpeed
import com.astro.vajra.ephemeris.muhurta.MuhurtaAstronomicalCalculator.jdToLocalTime
import com.astro.vajra.ephemeris.muhurta.MuhurtaConstants.AYANAMSA_LAHIRI
import swisseph.SweConst
import swisseph.SwissEph
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.ZoneId
import java.time.ZonedDateTime
import java.util.concurrent.ConcurrentLinkedQueue
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit
import kotlin.math.abs
import kotlin.math.roundToInt

/**
 * MuhurtaSearchEngine - Advanced Electional Astrology Search Engine
 *
 * Finds the optimal muhurta windows within a date range for a given activity.
 * Uses a multi-layer scoring model:
 *   1. Base Muhurta Score (40%) - Panchanga factors via MuhurtaEvaluator
 *   2. Lagna Strength (20%) - Ascendant lord dignity, combustion, house placement
 *   3. House Analysis (20%) - Activity-specific house benefic/malefic presence & aspects
 *   4. Moon Aspects (20%) - Applying aspects from Moon, Chandrabala, Tarabala
 *
 * Advanced features:
 *   - Tarabala compatibility (birth nakshatra vs muhurta nakshatra)
 *   - Chandrabala (Moon transit strength from natal Moon sign)
 *   - Pre-filtering of Rahukala, Yamaghanta, Durmuhurta
 *   - Parallel day scanning for performance on wide date ranges
 *   - Peak window grouping: consecutive high-scoring slots merge into windows
 *
 * Vedic references:
 *   - Muhurta Chintamani
 *   - Muhurta Martanda
 *   - Kalaprakashika
 *   - BPHS (electional chapters)
 */
class MuhurtaSearchEngine(context: Context) {

    private val swissEph = SwissEph()
    private val ephemerisPath: String = context.filesDir.absolutePath + "/ephe"
    private val muhurtaCalculator = MuhurtaCalculator(context)

    init {
        swissEph.swe_set_ephe_path(ephemerisPath)
        swissEph.swe_set_sid_mode(AYANAMSA_LAHIRI, 0.0, 0.0)
    }

    // ============================================================================
    // PUBLIC API
    // ============================================================================

    /**
     * Primary search entry point. Scans the date range at the configured time step,
     * scores each point, groups consecutive high-scoring points into windows,
     * and returns the top N windows.
     */
    fun searchOptimalMuhurta(request: MuhurtaSearchRequest): MuhurtaOptimalSearchResult {
        require(!request.endDate.isBefore(request.startDate)) {
            "endDate must be on or after startDate"
        }
        require(request.timeStepMinutes in 1..60) {
            "timeStepMinutes must be between 1 and 60"
        }

        val startMillis = System.currentTimeMillis()
        val zoneId = resolveZoneId(request.timezone)

        val allDays = generateSequence(request.startDate) { it.plusDays(1) }
            .takeWhile { !it.isAfter(request.endDate) }
            .toList()

        // Parallel day scanning for performance
        val allScoredPoints = ConcurrentLinkedQueue<ScoredTimePoint>()
        val totalPointsScanned = java.util.concurrent.atomic.AtomicInteger(0)

        val threadCount = (Runtime.getRuntime().availableProcessors() - 1).coerceIn(1, 4)
        val executor = Executors.newFixedThreadPool(threadCount)

        try {
            val futures = allDays.map { date ->
                executor.submit {
                    val dayPoints = scanDay(
                        date = date,
                        request = request,
                        zoneId = zoneId
                    )
                    totalPointsScanned.addAndGet(dayPoints.size)
                    allScoredPoints.addAll(dayPoints.filter { it.compositeScore >= request.minimumScore })
                }
            }
            futures.forEach { it.get(120, TimeUnit.SECONDS) }
        } finally {
            executor.shutdown()
        }

        val sortedPoints = allScoredPoints.sortedByDescending { it.compositeScore }
        val windows = groupIntoWindows(sortedPoints, request.timeStepMinutes)
            .sortedByDescending { it.peakScore }
            .take(request.topN)

        val searchDuration = System.currentTimeMillis() - startMillis

        return MuhurtaOptimalSearchResult(
            windows = windows,
            searchDuration = searchDuration,
            totalPointsScanned = totalPointsScanned.get(),
            bestWindow = windows.firstOrNull(),
            dateRange = request.startDate to request.endDate
        )
    }

    fun close() {
        muhurtaCalculator.close()
        swissEph.swe_close()
    }

    // ============================================================================
    // DAY SCANNING
    // ============================================================================

    /**
     * Scans a single day from sunrise to sunset at the configured time step.
     * Pre-filters inauspicious periods (Rahukala, Yamaghanta, Durmuhurta).
     */
    private fun scanDay(
        date: LocalDate,
        request: MuhurtaSearchRequest,
        zoneId: ZoneId
    ): List<ScoredTimePoint> {
        val points = mutableListOf<ScoredTimePoint>()

        val noonUtc = ZonedDateTime.of(date, LocalTime.NOON, zoneId)
            .withZoneSameInstant(ZoneId.of("UTC")).toLocalDateTime()
        val jdNoon = calculateJulianDay(noonUtc)
        val (srJd, ssJd) = calculateSunriseSunsetJD(jdNoon, request.latitude, request.longitude, swissEph)
        val sunrise = jdToLocalTime(srJd, zoneId)
        val sunset = jdToLocalTime(ssJd, zoneId)

        if (!sunset.isAfter(sunrise)) return points

        val vara = calculateVara(date)
        val inauspicious = MuhurtaTimeSegmentCalculator.calculateInauspiciousPeriods(vara, sunrise, sunset)

        var time = sunrise
        while (time.isBefore(sunset)) {
            val dateTime = LocalDateTime.of(date, time)

            // Pre-filter: skip Rahukala, Yamaghanta, Durmuhurta entirely
            if (isInInauspiciousPeriod(time, inauspicious)) {
                time = time.plusMinutes(request.timeStepMinutes.toLong())
                continue
            }

            val scoredPoint = evaluateTimePoint(
                dateTime = dateTime,
                request = request,
                zoneId = zoneId,
                vara = vara,
                sunrise = sunrise,
                sunset = sunset,
                inauspicious = inauspicious
            )

            points.add(scoredPoint)
            time = time.plusMinutes(request.timeStepMinutes.toLong())
        }

        return points
    }

    /**
     * Check if time falls in Rahukala, Yamaghanta, or any Durmuhurta.
     */
    private fun isInInauspiciousPeriod(time: LocalTime, ip: InauspiciousPeriods): Boolean {
        if (ip.rahukala.contains(time)) return true
        if (ip.yamaghanta.contains(time)) return true
        for (d in ip.durmuhurtas) {
            if (d.contains(time)) return true
        }
        return false
    }

    // ============================================================================
    // TIME POINT EVALUATION - COMPOSITE SCORING
    // ============================================================================

    /**
     * Evaluates a single time point with the 4-layer composite scoring model.
     */
    private fun evaluateTimePoint(
        dateTime: LocalDateTime,
        request: MuhurtaSearchRequest,
        zoneId: ZoneId,
        vara: Vara,
        sunrise: LocalTime,
        sunset: LocalTime,
        inauspicious: InauspiciousPeriods
    ): ScoredTimePoint {
        val utc = ZonedDateTime.of(dateTime, zoneId)
            .withZoneSameInstant(ZoneId.of("UTC")).toLocalDateTime()
        val jd = calculateJulianDay(utc)

        // Calculate planet positions at this moment
        val sunLong = getPlanetLongitude(SweConst.SE_SUN, jd, swissEph)
        val moonLong = getPlanetLongitude(SweConst.SE_MOON, jd, swissEph)

        // Panchanga elements
        val tithi = MuhurtaPanchangaEvaluator.calculateTithi(sunLong, moonLong)
        val nakshatraInfo = MuhurtaPanchangaEvaluator.calculateNakshatra(moonLong)
        val yoga = MuhurtaPanchangaEvaluator.calculateYoga(sunLong, moonLong)
        val karana = MuhurtaPanchangaEvaluator.calculateKarana(sunLong, moonLong)

        val time = dateTime.toLocalTime()
        val choghadiya = MuhurtaTimeSegmentCalculator.calculateChoghadiya(time, vara, sunrise, sunset)
        val hora = MuhurtaTimeSegmentCalculator.calculateHora(time, vara, sunrise, sunset)
        val abhijit = MuhurtaTimeSegmentCalculator.calculateAbhijitMuhurta(sunrise, sunset, time)
        val specialYogas = MuhurtaEvaluator.calculateSpecialYogas(vara, tithi, nakshatraInfo)

        // Layer 1: Base Muhurta Score from existing evaluator
        val (baseScore, _, _, reasons) = MuhurtaEvaluator.evaluateMuhurta(
            vara, tithi, nakshatraInfo, yoga, karana, choghadiya, hora,
            time, inauspicious, abhijit, specialYogas, Language.ENGLISH
        )

        // Activity-specific evaluation
        val (activityScore, actReasons, actWarnings) = MuhurtaEvaluator.evaluateForActivity(
            MuhurtaDetails(
                dateTime, vara, tithi, nakshatraInfo, yoga, karana, choghadiya, hora,
                inauspicious, abhijit, sunrise, sunset, baseScore,
                emptyList(), emptyList(), reasons, specialYogas
            ),
            request.activity,
            Language.ENGLISH
        )

        // Blend base and activity score for Layer 1
        val blendedBaseScore = ((baseScore * 0.4) + (activityScore * 0.6)).roundToInt()
            .coerceIn(0, 100)

        // Layer 2: Lagna Strength
        val lagnaLong = calculateLagna(jd, request.latitude, request.longitude)
        val lagnaSign = ZodiacSign.fromLongitude(lagnaLong)
        val lagnaLord = lagnaSign.ruler
        val lagnaScore = evaluateLagnaStrength(lagnaLord, jd, sunLong, lagnaSign, request.activity)

        // Layer 3: House Analysis
        val houseScore = evaluateHouseAnalysis(jd, lagnaLong, request.activity)

        // Layer 4: Moon Aspects + Tarabala + Chandrabala
        val moonScore = evaluateMoonAnalysis(
            moonLong = moonLong,
            jd = jd,
            natalChart = request.natalChart,
            muhurtaNakshatra = nakshatraInfo.nakshatra
        )

        // Tarabala & Chandrabala
        val tarabalaScore = request.natalChart?.let {
            calculateTarabala(it, nakshatraInfo.nakshatra)
        }
        val chandrabalaScore = request.natalChart?.let {
            calculateChandrabala(it, moonLong)
        }

        // Composite: Base 40% + Lagna 20% + House 20% + Moon 20%
        val compositeScore = (
            blendedBaseScore * 0.40 +
            lagnaScore * 0.20 +
            houseScore * 0.20 +
            moonScore * 0.20
        ).roundToInt().coerceIn(0, 100)

        // Boost/penalize for tarabala & chandrabala if available
        val adjustedScore = if (tarabalaScore != null && chandrabalaScore != null) {
            val bonus = ((tarabalaScore - 50) * 0.05 + (chandrabalaScore - 50) * 0.05).roundToInt()
            (compositeScore + bonus).coerceIn(0, 100)
        } else {
            compositeScore
        }

        val highlights = mutableListOf<String>()
        val warnings = mutableListOf<String>()

        highlights.addAll(actReasons)
        warnings.addAll(actWarnings)

        if (abhijit.isActive) highlights.add("Abhijit Muhurta active")
        if (lagnaScore >= 75) highlights.add("Strong Lagna: ${lagnaSign.displayName} with well-placed lord")
        if (moonScore >= 75) highlights.add("Moon well-aspected and strong")
        if (tarabalaScore != null && tarabalaScore >= 70) highlights.add("Favorable Tarabala ($tarabalaScore/100)")
        if (chandrabalaScore != null && chandrabalaScore >= 70) highlights.add("Favorable Chandrabala ($chandrabalaScore/100)")

        if (lagnaScore < 30) warnings.add("Weak Lagna lord")
        if (houseScore < 30) warnings.add("Activity-relevant houses afflicted")
        if (tarabalaScore != null && tarabalaScore < 30) warnings.add("Unfavorable Tarabala ($tarabalaScore/100)")
        if (chandrabalaScore != null && chandrabalaScore < 30) warnings.add("Unfavorable Chandrabala ($chandrabalaScore/100)")

        specialYogas.forEach { sy ->
            if (sy.isAuspicious) highlights.add(sy.name) else warnings.add(sy.name)
        }

        return ScoredTimePoint(
            dateTime = dateTime,
            compositeScore = adjustedScore,
            baseScore = blendedBaseScore,
            lagnaScore = lagnaScore,
            houseScore = houseScore,
            moonScore = moonScore,
            lagnaSign = lagnaSign,
            moonNakshatra = nakshatraInfo.nakshatra,
            highlights = highlights.distinct(),
            warnings = warnings.distinct(),
            tarabalaScore = tarabalaScore,
            chandrabalaScore = chandrabalaScore,
            specialYogas = specialYogas.map { it.name }
        )
    }

    // ============================================================================
    // LAYER 2: LAGNA STRENGTH EVALUATION
    // ============================================================================

    /**
     * Evaluates the strength of the muhurta lagna (ascendant):
     *   - Lord in own sign/exalted = high score
     *   - Lord combust = penalize
     *   - Lord in dusthana = penalize
     *   - No malefics in lagna or 7th = bonus
     *   - 8th house from lagna not afflicted = bonus
     *   - Hora compatibility with activity
     */
    private fun evaluateLagnaStrength(
        lagnaLord: Planet,
        jd: Double,
        sunLong: Double,
        lagnaSign: ZodiacSign,
        activity: ActivityType
    ): Int {
        var score = 50

        // Get lagna lord's longitude and sign
        val lordLong = getLagnaLordLongitude(lagnaLord, jd)
        val lordSign = ZodiacSign.fromLongitude(lordLong)
        val lordDegreeInSign = lordLong % 30.0

        // Dignity of lagna lord
        when {
            VedicAstrologyUtils.isExalted(lagnaLord, lordSign) -> {
                score += 25
            }
            VedicAstrologyUtils.isInMoolatrikona(lagnaLord, lordSign, lordDegreeInSign) -> {
                score += 20
            }
            VedicAstrologyUtils.isInOwnSign(lagnaLord, lordSign) -> {
                score += 18
            }
            VedicAstrologyUtils.isInFriendSign(lagnaLord, lordSign) -> {
                score += 10
            }
            VedicAstrologyUtils.isDebilitated(lagnaLord, lordSign) -> {
                score -= 20
            }
            VedicAstrologyUtils.isInEnemySign(lagnaLord, lordSign) -> {
                score -= 10
            }
        }

        // Combustion check
        if (VedicAstrologyUtils.isCombust(lagnaLord, lordLong, sunLong)) {
            score -= 15
        }

        // Lagna lord retrograde (mixed - stronger but delayed)
        val lordSpeed = try {
            getPlanetSpeed(getPlanetSwissId(lagnaLord), jd, swissEph)
        } catch (_: Exception) { 1.0 }
        if (lordSpeed < 0) {
            score -= 5 // Slight penalty for retrograde lagna lord in muhurta
        }

        // House placement of lagna lord from lagna
        val lordHouse = getHouseFromLagna(lordLong, lagnaSign)
        when {
            VedicAstrologyUtils.isKendra(lordHouse) -> score += 8
            VedicAstrologyUtils.isTrikona(lordHouse) -> score += 8
            VedicAstrologyUtils.isDusthana(lordHouse) -> score -= 12
            lordHouse in setOf(2, 11) -> score += 5 // Dhana/Labha
        }

        // Activity-specific lagna preference
        score += getActivityLagnaBonus(activity, lagnaSign)

        return score.coerceIn(0, 100)
    }

    /**
     * Returns bonus/penalty for specific lagnas suitable for the activity.
     * Per classical Muhurta Chintamani guidelines.
     */
    private fun getActivityLagnaBonus(activity: ActivityType, lagnaSign: ZodiacSign): Int {
        return when (activity) {
            ActivityType.MARRIAGE -> when (lagnaSign) {
                ZodiacSign.TAURUS, ZodiacSign.GEMINI, ZodiacSign.CANCER,
                ZodiacSign.VIRGO, ZodiacSign.LIBRA, ZodiacSign.SAGITTARIUS -> 8
                ZodiacSign.ARIES, ZodiacSign.SCORPIO -> -5
                else -> 0
            }
            ActivityType.TRAVEL -> when (lagnaSign) {
                ZodiacSign.GEMINI, ZodiacSign.CANCER, ZodiacSign.VIRGO,
                ZodiacSign.LIBRA, ZodiacSign.SAGITTARIUS, ZodiacSign.AQUARIUS -> 8
                ZodiacSign.SCORPIO, ZodiacSign.CAPRICORN -> -5
                else -> 0
            }
            ActivityType.BUSINESS -> when (lagnaSign) {
                ZodiacSign.TAURUS, ZodiacSign.GEMINI, ZodiacSign.VIRGO,
                ZodiacSign.LIBRA, ZodiacSign.AQUARIUS -> 8
                ZodiacSign.SCORPIO -> -3
                else -> 0
            }
            ActivityType.PROPERTY, ActivityType.GRIHA_PRAVESHA -> when (lagnaSign) {
                ZodiacSign.TAURUS, ZodiacSign.CANCER, ZodiacSign.LEO,
                ZodiacSign.VIRGO, ZodiacSign.LIBRA, ZodiacSign.AQUARIUS -> 8
                ZodiacSign.SCORPIO, ZodiacSign.ARIES -> -5
                else -> 0
            }
            ActivityType.EDUCATION -> when (lagnaSign) {
                ZodiacSign.GEMINI, ZodiacSign.VIRGO, ZodiacSign.SAGITTARIUS,
                ZodiacSign.AQUARIUS -> 10
                else -> 0
            }
            ActivityType.MEDICAL -> when (lagnaSign) {
                ZodiacSign.ARIES, ZodiacSign.LEO, ZodiacSign.VIRGO,
                ZodiacSign.SCORPIO -> 8
                else -> 0
            }
            ActivityType.SPIRITUAL -> when (lagnaSign) {
                ZodiacSign.CANCER, ZodiacSign.SAGITTARIUS, ZodiacSign.PISCES -> 10
                ZodiacSign.LEO -> 5
                else -> 0
            }
            else -> 0
        }
    }

    // ============================================================================
    // LAYER 3: HOUSE ANALYSIS
    // ============================================================================

    /**
     * Evaluates activity-specific houses for benefic/malefic presence & aspects.
     * Checks that the 8th from muhurta lagna is not afflicted.
     * Checks no malefics in lagna or 7th.
     */
    private fun evaluateHouseAnalysis(
        jd: Double,
        lagnaLong: Double,
        activity: ActivityType
    ): Int {
        var score = 50
        val lagnaSign = ZodiacSign.fromLongitude(lagnaLong)

        // Get all planet longitudes for this moment
        val planetData = Planet.MAIN_PLANETS.mapNotNull { planet ->
            try {
                val swissId = getPlanetSwissId(planet)
                val long = if (planet == Planet.KETU) {
                    VedicAstrologyUtils.normalizeLongitude(
                        getPlanetLongitude(SweConst.SE_MEAN_NODE, jd, swissEph) + 180.0
                    )
                } else {
                    getPlanetLongitude(swissId, jd, swissEph)
                }
                val house = getHouseFromLagna(long, lagnaSign)
                val speed = try {
                    if (planet == Planet.KETU) -1.0
                    else getPlanetSpeed(swissId, jd, swissEph)
                } catch (_: Exception) { 0.0 }
                PlanetHouseData(planet, long, house, speed < 0)
            } catch (_: Exception) { null }
        }

        // Check: no malefics in lagna (house 1)
        val maleficsInLagna = planetData.filter {
            it.house == 1 && it.planet in VedicAstrologyUtils.NATURAL_MALEFICS
        }
        if (maleficsInLagna.isEmpty()) {
            score += 8
        } else {
            score -= maleficsInLagna.size * 6
        }

        // Check: no malefics in 7th
        val maleficsIn7th = planetData.filter {
            it.house == 7 && it.planet in VedicAstrologyUtils.NATURAL_MALEFICS
        }
        if (maleficsIn7th.isEmpty()) {
            score += 5
        } else {
            score -= maleficsIn7th.size * 5
        }

        // Check: 8th house from lagna not afflicted
        val maleficsIn8th = planetData.filter {
            it.house == 8 && it.planet in VedicAstrologyUtils.NATURAL_MALEFICS
        }
        if (maleficsIn8th.isEmpty()) {
            score += 5
        } else {
            score -= maleficsIn8th.size * 4
        }

        // Benefics in lagna = bonus
        val beneficsInLagna = planetData.filter {
            it.house == 1 && it.planet in VedicAstrologyUtils.NATURAL_BENEFICS
        }
        score += beneficsInLagna.size * 4

        // Activity-specific house checks
        val relevantHouses = getActivityRelevantHouses(activity)
        for (house in relevantHouses) {
            val benefics = planetData.count {
                it.house == house && it.planet in VedicAstrologyUtils.NATURAL_BENEFICS
            }
            val malefics = planetData.count {
                it.house == house && it.planet in VedicAstrologyUtils.NATURAL_MALEFICS
            }
            score += benefics * 3
            score -= malefics * 3

            // Check benefic aspects on relevant houses
            for (pd in planetData) {
                if (pd.planet in VedicAstrologyUtils.NATURAL_BENEFICS) {
                    if (VedicAstrologyUtils.aspectsHouse(pd.planet, pd.house, house)) {
                        score += 2
                    }
                }
                if (pd.planet in VedicAstrologyUtils.NATURAL_MALEFICS) {
                    if (VedicAstrologyUtils.aspectsHouse(pd.planet, pd.house, house)) {
                        score -= 2
                    }
                }
            }
        }

        return score.coerceIn(0, 100)
    }

    /**
     * Returns the houses most relevant for each activity type.
     */
    private fun getActivityRelevantHouses(activity: ActivityType): List<Int> {
        return when (activity) {
            ActivityType.MARRIAGE -> listOf(1, 2, 7, 8, 12)
            ActivityType.TRAVEL -> listOf(1, 3, 9, 10)
            ActivityType.BUSINESS -> listOf(1, 2, 7, 10, 11)
            ActivityType.PROPERTY, ActivityType.GRIHA_PRAVESHA -> listOf(1, 4, 7, 10)
            ActivityType.EDUCATION -> listOf(1, 4, 5, 9)
            ActivityType.MEDICAL -> listOf(1, 6, 8, 10)
            ActivityType.VEHICLE -> listOf(1, 4, 7)
            ActivityType.SPIRITUAL -> listOf(1, 5, 9, 12)
            ActivityType.NAMING_CEREMONY -> listOf(1, 2, 5)
            ActivityType.GENERAL -> listOf(1, 7, 10)
        }
    }

    // ============================================================================
    // LAYER 4: MOON ANALYSIS, TARABALA, CHANDRABALA
    // ============================================================================

    /**
     * Evaluates Moon's condition at the muhurta moment:
     *   - Applying aspects from benefics = good
     *   - Applying aspects from malefics = bad
     *   - Moon waxing = bonus
     *   - Moon in good nakshatra for activity = bonus
     */
    private fun evaluateMoonAnalysis(
        moonLong: Double,
        jd: Double,
        natalChart: VedicChart?,
        muhurtaNakshatra: Nakshatra
    ): Int {
        var score = 50

        // Moon's speed (positive = direct, moving forward)
        val moonSpeed = try {
            getPlanetSpeed(SweConst.SE_MOON, jd, swissEph)
        } catch (_: Exception) { 13.0 }

        // Fast Moon is good for muhurta
        if (moonSpeed > 13.5) score += 5
        else if (moonSpeed < 11.5) score -= 5

        // Check Moon's phase (waxing = benefic, waning = mixed)
        val sunLong = try {
            getPlanetLongitude(SweConst.SE_SUN, jd, swissEph)
        } catch (_: Exception) { 0.0 }
        val moonSunDiff = VedicAstrologyUtils.normalizeLongitude(moonLong - sunLong)
        val isWaxing = moonSunDiff in 0.0..180.0
        if (isWaxing) score += 8 else score -= 3

        // Shukla Paksha Panchami to Purnima is considered very strong for Moon
        if (isWaxing && moonSunDiff >= 48.0 && moonSunDiff <= 180.0) {
            score += 5
        }

        // Check applying aspects from planets
        for (planet in listOf(Planet.JUPITER, Planet.VENUS, Planet.MERCURY, Planet.MARS, Planet.SATURN)) {
            val planetLong = try {
                val swissId = getPlanetSwissId(planet)
                getPlanetLongitude(swissId, jd, swissEph)
            } catch (_: Exception) { continue }

            val aspectDiff = VedicAstrologyUtils.normalizeLongitude(planetLong - moonLong)

            // Check standard Vedic aspect distances: ~60, ~90, ~120, ~180 degrees
            // Applying = planet is ahead of Moon by aspect distance and Moon is moving toward it
            val isApplying = isApplyingAspect(moonLong, moonSpeed, planetLong, aspectDiff)

            if (isApplying) {
                if (planet in VedicAstrologyUtils.NATURAL_BENEFICS) {
                    score += 6
                } else if (planet in VedicAstrologyUtils.NATURAL_MALEFICS) {
                    score -= 6
                }
            }
        }

        // Moon sign dignity
        val moonSign = ZodiacSign.fromLongitude(moonLong)
        when {
            VedicAstrologyUtils.isExalted(Planet.MOON, moonSign) -> score += 10
            VedicAstrologyUtils.isInOwnSign(Planet.MOON, moonSign) -> score += 8
            VedicAstrologyUtils.isDebilitated(Planet.MOON, moonSign) -> score -= 12
            VedicAstrologyUtils.isInFriendSign(Planet.MOON, moonSign) -> score += 5
            VedicAstrologyUtils.isInEnemySign(Planet.MOON, moonSign) -> score -= 5
        }

        return score.coerceIn(0, 100)
    }

    /**
     * Determines if there is an applying aspect between Moon and a planet.
     * Applying means the aspect is forming (Moon moving toward exact aspect).
     */
    private fun isApplyingAspect(
        moonLong: Double,
        moonSpeed: Double,
        planetLong: Double,
        rawDiff: Double
    ): Boolean {
        val aspectAngles = listOf(60.0, 90.0, 120.0, 180.0)
        val orb = 8.0 // degrees

        for (angle in aspectAngles) {
            val diff = abs(rawDiff - angle)
            if (diff <= orb) {
                // Applying if Moon is approaching the exact aspect
                // (Moon is slightly before the exact aspect angle)
                if (rawDiff < angle && moonSpeed > 0) return true
                if (rawDiff > (360.0 - angle) && moonSpeed > 0) return true
            }
        }
        return false
    }

    /**
     * Tarabala calculation: compatibility between native's birth nakshatra
     * and the muhurta nakshatra.
     *
     * Tarabala groups the 27 nakshatras into 9 taras (stars) from the birth nakshatra.
     * Tara 1 (Janma), 3 (Vipat), 5 (Pratyari), 7 (Vadha) are inauspicious.
     * Tara 2 (Sampat), 4 (Kshema), 6 (Sadhaka), 8 (Mitra), 9 (Parama Mitra) are auspicious.
     *
     * Returns a score 0-100.
     */
    private fun calculateTarabala(natalChart: VedicChart, muhurtaNakshatra: Nakshatra): Int {
        val moonPos = natalChart.planetPositions.find { it.planet == Planet.MOON } ?: return 50
        val (birthNakshatra, _) = Nakshatra.fromLongitude(moonPos.longitude)

        val diff = ((muhurtaNakshatra.number - birthNakshatra.number + 27) % 27)
        val tara = (diff % 9) + 1 // 1-9

        return when (tara) {
            1 -> 20  // Janma - birth star, challenging
            2 -> 85  // Sampat - wealth, very favorable
            3 -> 15  // Vipat - danger, avoid
            4 -> 80  // Kshema - well-being, favorable
            5 -> 20  // Pratyari - obstacles
            6 -> 75  // Sadhaka - achievement, favorable
            7 -> 10  // Vadha - death-like, most inauspicious
            8 -> 90  // Mitra - friendship, very favorable
            9 -> 95  // Parama Mitra - best friend, most auspicious
            else -> 50
        }
    }

    /**
     * Chandrabala calculation: Moon's strength from natal Moon sign.
     *
     * The transit Moon's sign counted from the natal Moon sign determines Chandrabala:
     * Houses 1, 3, 6, 7, 10, 11 from natal Moon = favorable
     * Houses 2, 5, 9 = neutral
     * Houses 4, 8, 12 = unfavorable
     *
     * Returns a score 0-100.
     */
    private fun calculateChandrabala(natalChart: VedicChart, transitMoonLong: Double): Int {
        val moonPos = natalChart.planetPositions.find { it.planet == Planet.MOON } ?: return 50
        val natalMoonSign = ZodiacSign.fromLongitude(moonPos.longitude)
        val transitMoonSign = ZodiacSign.fromLongitude(transitMoonLong)

        val house = VedicAstrologyUtils.getHouseFromSigns(transitMoonSign, natalMoonSign)

        return when (house) {
            1 -> 75  // Good
            2 -> 50  // Neutral
            3 -> 80  // Very good (upachaya)
            4 -> 25  // Bad
            5 -> 50  // Neutral
            6 -> 75  // Good (upachaya)
            7 -> 70  // Good
            8 -> 15  // Very bad
            9 -> 55  // Slightly positive
            10 -> 85 // Very good (upachaya)
            11 -> 90 // Excellent (labha)
            12 -> 20 // Bad (vyaya)
            else -> 50
        }
    }

    // ============================================================================
    // WINDOW GROUPING
    // ============================================================================

    /**
     * Groups consecutive high-scoring time points into muhurta windows.
     * Points within 2x the time step of each other are considered part of the same window.
     */
    private fun groupIntoWindows(
        sortedPoints: List<ScoredTimePoint>,
        timeStepMinutes: Int
    ): List<MuhurtaWindow> {
        if (sortedPoints.isEmpty()) return emptyList()

        // Sort chronologically for grouping
        val chronological = sortedPoints.sortedBy { it.dateTime }
        val maxGapMinutes = timeStepMinutes * 2L

        val windows = mutableListOf<MuhurtaWindow>()
        var currentGroup = mutableListOf(chronological.first())

        for (i in 1 until chronological.size) {
            val prev = chronological[i - 1]
            val curr = chronological[i]
            val gapMinutes = java.time.Duration.between(prev.dateTime, curr.dateTime).toMinutes()

            if (gapMinutes <= maxGapMinutes && prev.dateTime.toLocalDate() == curr.dateTime.toLocalDate()) {
                currentGroup.add(curr)
            } else {
                windows.add(createWindow(currentGroup))
                currentGroup = mutableListOf(curr)
            }
        }
        if (currentGroup.isNotEmpty()) {
            windows.add(createWindow(currentGroup))
        }

        return windows
    }

    /**
     * Creates a MuhurtaWindow from a group of consecutive ScoredTimePoints.
     */
    private fun createWindow(points: List<ScoredTimePoint>): MuhurtaWindow {
        val peak = points.maxByOrNull { it.compositeScore } ?: points.first()
        val avgScore = points.map { it.compositeScore }.average()

        val allHighlights = points.flatMap { it.highlights }.distinct()
        val allWarnings = points.flatMap { it.warnings }.distinct()
        val allYogas = points.flatMap { it.specialYogas }.distinct()

        return MuhurtaWindow(
            startTime = points.first().dateTime,
            endTime = points.last().dateTime,
            peakTime = peak.dateTime,
            peakScore = peak.compositeScore,
            averageScore = avgScore,
            lagnaSign = peak.lagnaSign,
            moonNakshatra = peak.moonNakshatra,
            highlights = allHighlights,
            warnings = allWarnings,
            tarabalaScore = peak.tarabalaScore,
            chandrabalaScore = peak.chandrabalaScore,
            specialYogas = allYogas
        )
    }

    // ============================================================================
    // ASTRONOMICAL HELPERS
    // ============================================================================

    /**
     * Calculate the sidereal ascendant (lagna) for a given JD and location.
     */
    private fun calculateLagna(jd: Double, latitude: Double, longitude: Double): Double {
        val cusps = DoubleArray(13)
        val ascmc = DoubleArray(10)
        swissEph.swe_houses(
            jd, SweConst.SEFLG_SIDEREAL,
            latitude, longitude,
            'W'.code, // Whole Sign
            cusps, ascmc
        )
        return VedicAstrologyUtils.normalizeLongitude(ascmc[0])
    }

    /**
     * Get longitude of the lagna lord at a given JD.
     */
    private fun getLagnaLordLongitude(planet: Planet, jd: Double): Double {
        return try {
            if (planet == Planet.KETU) {
                VedicAstrologyUtils.normalizeLongitude(
                    getPlanetLongitude(SweConst.SE_MEAN_NODE, jd, swissEph) + 180.0
                )
            } else {
                getPlanetLongitude(getPlanetSwissId(planet), jd, swissEph)
            }
        } catch (_: Exception) {
            0.0
        }
    }

    /**
     * Determine house number (1-12) from lagna for a given longitude.
     */
    private fun getHouseFromLagna(longitude: Double, lagnaSign: ZodiacSign): Int {
        val planetSign = ZodiacSign.fromLongitude(longitude)
        return VedicAstrologyUtils.getHouseFromSigns(planetSign, lagnaSign)
    }

    /**
     * Map Planet enum to Swiss Ephemeris planet ID.
     */
    private fun getPlanetSwissId(planet: Planet): Int {
        return when (planet) {
            Planet.SUN -> SweConst.SE_SUN
            Planet.MOON -> SweConst.SE_MOON
            Planet.MARS -> SweConst.SE_MARS
            Planet.MERCURY -> SweConst.SE_MERCURY
            Planet.JUPITER -> SweConst.SE_JUPITER
            Planet.VENUS -> SweConst.SE_VENUS
            Planet.SATURN -> SweConst.SE_SATURN
            Planet.RAHU -> SweConst.SE_MEAN_NODE
            Planet.KETU -> SweConst.SE_MEAN_NODE // Ketu = Rahu + 180
            else -> planet.swissEphId
        }
    }

    private fun calculateVara(date: LocalDate): Vara {
        return Vara.entries.find { it.dayNumber == date.dayOfWeek.value % 7 } ?: Vara.SUNDAY
    }

    private fun resolveZoneId(timezone: String): ZoneId {
        return com.astro.vajra.util.TimezoneSanitizer.resolveZoneIdOrNull(timezone)
            ?: throw IllegalArgumentException("Invalid timezone: $timezone")
    }

    // ============================================================================
    // INTERNAL DATA MODELS
    // ============================================================================

    /**
     * Internal scored time point used during scanning.
     */
    private data class ScoredTimePoint(
        val dateTime: LocalDateTime,
        val compositeScore: Int,
        val baseScore: Int,
        val lagnaScore: Int,
        val houseScore: Int,
        val moonScore: Int,
        val lagnaSign: ZodiacSign,
        val moonNakshatra: Nakshatra,
        val highlights: List<String>,
        val warnings: List<String>,
        val tarabalaScore: Int?,
        val chandrabalaScore: Int?,
        val specialYogas: List<String>
    )

    /**
     * Internal planet position data for house analysis.
     */
    private data class PlanetHouseData(
        val planet: Planet,
        val longitude: Double,
        val house: Int,
        val isRetrograde: Boolean
    )
}

// ============================================================================
// PUBLIC DATA CLASSES
// ============================================================================

/**
 * Search request parameters for the Muhurta Search Engine.
 */
data class MuhurtaSearchRequest(
    val activity: ActivityType,
    val startDate: LocalDate,
    val endDate: LocalDate,
    val latitude: Double,
    val longitude: Double,
    val timezone: String,
    val natalChart: VedicChart? = null,
    val timeStepMinutes: Int = 5,
    val topN: Int = 10,
    val minimumScore: Int = 60
)

/**
 * Complete search result containing ranked muhurta windows.
 * Named MuhurtaOptimalSearchResult to distinguish from the simpler
 * MuhurtaSearchResult in MuhurtaModels.kt used by the basic search.
 */
data class MuhurtaOptimalSearchResult(
    val windows: List<MuhurtaWindow>,
    val searchDuration: Long,
    val totalPointsScanned: Int,
    val bestWindow: MuhurtaWindow?,
    val dateRange: Pair<LocalDate, LocalDate>
)

/**
 * A contiguous window of auspicious time with peak and average scoring.
 */
data class MuhurtaWindow(
    val startTime: LocalDateTime,
    val endTime: LocalDateTime,
    val peakTime: LocalDateTime,
    val peakScore: Int,
    val averageScore: Double,
    val lagnaSign: ZodiacSign,
    val moonNakshatra: Nakshatra,
    val highlights: List<String>,
    val warnings: List<String>,
    val tarabalaScore: Int?,
    val chandrabalaScore: Int?,
    val specialYogas: List<String>
)
