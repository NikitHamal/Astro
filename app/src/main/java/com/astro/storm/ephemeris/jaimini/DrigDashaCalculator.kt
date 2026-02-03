package com.astro.storm.ephemeris.jaimini

import com.astro.storm.core.model.Planet
import com.astro.storm.core.model.PlanetPosition
import com.astro.storm.core.model.VedicChart
import com.astro.storm.core.model.ZodiacSign
import com.astro.storm.ephemeris.VedicAstrologyUtils
import java.time.LocalDateTime
import java.time.temporal.ChronoUnit
import kotlin.math.abs

/**
 * DrigDashaCalculator - Jaimini Drig (Sthira) Dasha System for Longevity
 *
 * Drig Dasha is one of the important conditional Nakshatra-based dashas in Jaimini astrology,
 * primarily used for determining longevity (Ayurdaya) and timing of significant life events.
 * It is also known as Sthira Dasha because it uses the fixed (sthira) significators.
 *
 * Key Principles:
 * 1. Starting Point: Depends on Lagna (odd/even sign)
 * 2. Order: Signs progress based on aspectual relationships (Drig = aspect/vision)
 * 3. Duration: Based on planetary influences on signs
 * 4. Focus: Longevity prediction, timing of health crises
 *
 * Three Longevity Spans (Ayur):
 * - Alpayu (Short life): 0-32 years
 * - Madhyayu (Medium life): 32-64 years
 * - Purnayu (Full life): 64-96+ years
 *
 * Calculation Methodology:
 * 1. Determine Drig Dasha starting sign from Lagna/9th house
 * 2. Calculate dasha years for each sign based on aspects
 * 3. Apply Brahma, Rudra, Maheshwara analysis
 * 4. Determine critical periods (Maraka periods)
 *
 * Vedic References:
 * - Jaimini Sutras (Chapter 2, Pada 4)
 * - Jaimini Upadesa Sutras
 * - Phala Ratnamala by Krishnamishra
 *
 * @author AstroStorm
 * @since 2026.02
 */
object DrigDashaCalculator {

    /**
     * Total dasha cycle duration in years (based on traditional Jaimini)
     */
    private const val TOTAL_CYCLE_YEARS = 120.0

    /**
     * Base years per sign (for standard calculation)
     */
    private const val BASE_YEARS_PER_SIGN = 9.0

    /**
     * Longevity classification
     */
    enum class AyurSpan(
        val displayName: String,
        val sanskritName: String,
        val minYears: Int,
        val maxYears: Int,
        val description: String
    ) {
        BALA_ARISHTA("Infant Mortality", "Bala Arishta", 0, 8, "Critical period in early childhood"),
        ALPAYU("Short Life", "Alpayu", 8, 32, "Life span up to 32 years"),
        MADHYAYU("Medium Life", "Madhyayu", 32, 64, "Life span between 32-64 years"),
        PURNAYU("Full Life", "Purnayu", 64, 120, "Life span beyond 64 years")
    }

    /**
     * Sthira Karaka (Fixed Significator) for each house
     */
    enum class SthiraKaraka(
        val house: Int,
        val planet: Planet,
        val signification: String
    ) {
        FIRST(1, Planet.SUN, "Self, Vitality, Soul"),
        SECOND(2, Planet.JUPITER, "Wealth, Family, Speech"),
        THIRD(3, Planet.MARS, "Siblings, Courage, Arms"),
        FOURTH(4, Planet.MOON, "Mother, Mind, Home"),
        FIFTH(5, Planet.JUPITER, "Children, Intelligence, Merit"),
        SIXTH(6, Planet.SATURN, "Enemies, Disease, Service"),
        SEVENTH(7, Planet.VENUS, "Spouse, Partnership, Desire"),
        EIGHTH(8, Planet.SATURN, "Longevity, Hidden, Occult"),
        NINTH(9, Planet.JUPITER, "Father, Dharma, Fortune"),
        TENTH(10, Planet.MERCURY, "Karma, Profession, Status"),
        ELEVENTH(11, Planet.JUPITER, "Gains, Elder Siblings, Desires"),
        TWELFTH(12, Planet.SATURN, "Loss, Liberation, Foreign")
    }

    /**
     * Sthira Karaka Info for Analysis
     */
    data class SthiraKarakaInfo(
        val sthiraKaraka: SthiraKaraka,
        val position: PlanetPosition,
        val dignity: VedicAstrologyUtils.PlanetaryDignity,
        val strength: String
    )

    /**
     * Drig Dasha period for a sign
     */
    data class DrigDashaPeriod(
        val sign: ZodiacSign,
        val startDate: LocalDateTime,
        val endDate: LocalDateTime,
        val durationYears: Double,
        val signLord: Planet,
        val planetsInSign: List<Planet>,
        val aspectingPlanets: List<Planet>,
        val isMarakaPeriod: Boolean,
        val marakaReason: String?,
        val interpretation: String,
        val healthIndicators: List<String>,
        val criticalSubPeriods: List<DrigAntardasha>
    ) {
        val durationMonths: Double get() = durationYears * 12
        val durationDays: Long get() = (durationYears * 365.25).toLong()

        fun contains(date: LocalDateTime): Boolean {
            return !date.isBefore(startDate) && date.isBefore(endDate)
        }
    }

    /**
     * Drig Antardasha (sub-period)
     */
    data class DrigAntardasha(
        val sign: ZodiacSign,
        val startDate: LocalDateTime,
        val endDate: LocalDateTime,
        val durationMonths: Double,
        val isCritical: Boolean,
        val criticalReason: String?
    )

    /**
     * Complete Drig Dasha analysis
     */
    data class DrigDashaAnalysis(
        val chartId: String,
        val birthDateTime: LocalDateTime,
        val drigDashaSequence: List<DrigDashaPeriod>,
        val currentDasha: DrigDashaPeriod?,
        val upcomingDashas: List<DrigDashaPeriod>,
        val longevitySpan: AyurSpan,
        val estimatedLongevity: Double,
        val brahmaSign: ZodiacSign,
        val rudraSign: ZodiacSign,
        val maheshwaraSign: ZodiacSign,
        val criticalPeriods: List<CriticalPeriod>,
        val longevityAnalysis: LongevityAnalysis,
        val sthiraKarakas: List<SthiraKarakaInfo> = emptyList(),
        val timestamp: Long = System.currentTimeMillis()
    )

    /**
     * Critical period identification
     */
    data class CriticalPeriod(
        val startDate: LocalDateTime,
        val endDate: LocalDateTime,
        val sign: ZodiacSign,
        val reason: String,
        val severity: Int, // 1-5
        val remedies: List<String>
    )

    /**
     * Detailed longevity analysis
     */
    data class LongevityAnalysis(
        val lagnaPairSpan: AyurSpan,
        val moonPairSpan: AyurSpan,
        val saturnPairSpan: AyurSpan,
        val finalSpan: AyurSpan,
        val kakshyaContribution: Double,
        val factors: List<LongevityFactor>
    )

    /**
     * Individual longevity factor
     */
    data class LongevityFactor(
        val name: String,
        val contribution: Double, // Positive or negative years
        val description: String
    )

    /**
     * Calculate complete Drig Dasha analysis
     *
     * @param chart The birth chart
     * @param currentDate Current date for determining active dasha
     * @return Complete DrigDashaAnalysis
     */
    fun calculateDrigDasha(
        chart: VedicChart,
        currentDate: LocalDateTime = LocalDateTime.now()
    ): DrigDashaAnalysis {
        val birthDateTime = chart.birthData.dateTime
        val lagnaSign = VedicAstrologyUtils.getAscendantSign(chart)

        // Calculate Brahma, Rudra, Maheshwara signs
        val brahma = calculateBrahmaSign(chart)
        val rudra = calculateRudraSign(chart, brahma)
        val maheshwara = calculateMaheshwaraSign(chart)

        // Calculate longevity span
        val longevityAnalysis = calculateLongevitySpan(chart)
        val longevitySpan = longevityAnalysis.finalSpan

        // Calculate dasha sequence
        val dashSequence = calculateDashaSequence(chart, lagnaSign, birthDateTime)

        // Find current and upcoming dashas
        val currentDasha = dashSequence.find { it.contains(currentDate) }
        val upcomingDashas = dashSequence.filter { it.startDate.isAfter(currentDate) }.take(3)

        // Identify critical periods
        val criticalPeriods = identifyCriticalPeriods(dashSequence, chart, rudra, maheshwara)

        // Calculate estimated longevity
        val estimatedLongevity = calculateEstimatedLongevity(longevityAnalysis, chart)

        return DrigDashaAnalysis(
            chartId = generateChartId(chart),
            birthDateTime = birthDateTime,
            drigDashaSequence = dashSequence,
            currentDasha = currentDasha,
            upcomingDashas = upcomingDashas,
            longevitySpan = longevitySpan,
            estimatedLongevity = estimatedLongevity,
            brahmaSign = brahma,
            rudraSign = rudra,
            maheshwaraSign = maheshwara,
            criticalPeriods = criticalPeriods,
            longevityAnalysis = longevityAnalysis
        )
    }

    /**
     * Calculate Brahma - the creator significator
     *
     * Brahma is determined from the stronger of:
     * - 6th from Lagna
     * - 6th from 7th (i.e., 12th from Lagna)
     *
     * The stronger sign between these becomes Brahma.
     */
    private fun calculateBrahmaSign(chart: VedicChart): ZodiacSign {
        val lagnaSign = VedicAstrologyUtils.getAscendantSign(chart)

        // 6th from Lagna
        val sixthSign = getSignFromLagna(lagnaSign, 6)
        // 12th from Lagna (6th from 7th)
        val twelfthSign = getSignFromLagna(lagnaSign, 12)

        val sixthStrength = calculateSignStrength(chart, sixthSign)
        val twelfthStrength = calculateSignStrength(chart, twelfthSign)

        return if (sixthStrength >= twelfthStrength) sixthSign else twelfthSign
    }

    /**
     * Calculate Rudra - the destroyer significator
     *
     * Rudra is the 8th from Brahma or the stronger maraka sign.
     */
    private fun calculateRudraSign(chart: VedicChart, brahma: ZodiacSign): ZodiacSign {
        // 8th from Brahma
        val eighthFromBrahma = getSignFromLagna(brahma, 8)

        // Also check 2nd and 7th from Lagna (maraka houses)
        val lagnaSign = VedicAstrologyUtils.getAscendantSign(chart)
        val secondSign = getSignFromLagna(lagnaSign, 2)
        val seventhSign = getSignFromLagna(lagnaSign, 7)

        // Choose the strongest among these
        val candidates = listOf(eighthFromBrahma, secondSign, seventhSign)
        return candidates.maxByOrNull { calculateSignStrength(chart, it) } ?: eighthFromBrahma
    }

    /**
     * Calculate Maheshwara - the supreme controller
     *
     * Maheshwara is determined by the 8th lord's position and strength.
     */
    private fun calculateMaheshwaraSign(chart: VedicChart): ZodiacSign {
        val lagnaSign = VedicAstrologyUtils.getAscendantSign(chart)
        val eighthSign = getSignFromLagna(lagnaSign, 8)
        val eighthLord = eighthSign.ruler

        // Find where 8th lord is placed
        val eighthLordPos = chart.planetPositions.find { it.planet == eighthLord }
        return eighthLordPos?.sign ?: eighthSign
    }

    /**
     * Calculate the complete dasha sequence
     */
    private fun calculateDashaSequence(
        chart: VedicChart,
        lagnaSign: ZodiacSign,
        birthDate: LocalDateTime
    ): List<DrigDashaPeriod> {
        val sequence = mutableListOf<DrigDashaPeriod>()
        var currentDate = birthDate

        // Determine starting sign based on odd/even Lagna
        val isOddLagna = lagnaSign.number % 2 == 1
        val startSign = if (isOddLagna) lagnaSign else getSignFromLagna(lagnaSign, 7)

        // Generate dasha for all 12 signs
        val signOrder = getDrigDashaSignOrder(startSign, isOddLagna)

        for (sign in signOrder) {
            val duration = calculateSignDashaDuration(chart, sign)
            val endDate = currentDate.plusDays((duration * 365.25).toLong())

            val signLord = sign.ruler
            val planetsInSign = chart.planetPositions
                .filter { it.sign == sign }
                .map { it.planet }
            val aspectingPlanets = getAspectingPlanets(chart, sign)

            val isMaraka = isMarakaPeriod(chart, sign)
            val marakaReason = if (isMaraka) getMarakaReason(chart, sign) else null

            val interpretation = buildDashaPeriodInterpretation(sign, signLord, planetsInSign, isMaraka)
            val healthIndicators = getHealthIndicators(chart, sign)
            val antardashas = calculateAntardashas(sign, currentDate, endDate, chart)

            sequence.add(
                DrigDashaPeriod(
                    sign = sign,
                    startDate = currentDate,
                    endDate = endDate,
                    durationYears = duration,
                    signLord = signLord,
                    planetsInSign = planetsInSign,
                    aspectingPlanets = aspectingPlanets,
                    isMarakaPeriod = isMaraka,
                    marakaReason = marakaReason,
                    interpretation = interpretation,
                    healthIndicators = healthIndicators,
                    criticalSubPeriods = antardashas
                )
            )

            currentDate = endDate
        }

        return sequence
    }

    /**
     * Get dasha sign order based on starting sign and Lagna type
     */
    private fun getDrigDashaSignOrder(startSign: ZodiacSign, isOddLagna: Boolean): List<ZodiacSign> {
        val allSigns = ZodiacSign.entries
        val startIndex = allSigns.indexOf(startSign)

        return if (isOddLagna) {
            // Forward direction for odd signs
            (0 until 12).map { allSigns[(startIndex + it) % 12] }
        } else {
            // Backward direction for even signs
            (0 until 12).map { allSigns[(startIndex - it + 12) % 12] }
        }
    }

    /**
     * Calculate dasha duration for a sign
     *
     * Duration = Base years + modifications based on:
     * - Planets in the sign
     * - Aspects on the sign
     * - Sign lord's condition
     */
    private fun calculateSignDashaDuration(chart: VedicChart, sign: ZodiacSign): Double {
        var duration = BASE_YEARS_PER_SIGN

        // Count planets in sign (each planet adds/modifies duration)
        val planetsInSign = chart.planetPositions.filter { it.sign == sign }
        duration += planetsInSign.size * 0.5

        // Benefic planets add slightly more
        val beneficCount = planetsInSign.count {
            it.planet in listOf(Planet.JUPITER, Planet.VENUS, Planet.MERCURY, Planet.MOON)
        }
        duration += beneficCount * 0.25

        // Sign lord condition
        val signLord = sign.ruler
        val lordPos = chart.planetPositions.find { it.planet == signLord }
        if (lordPos != null) {
            val dignity = VedicAstrologyUtils.getDignity(lordPos)
            duration += when (dignity) {
                VedicAstrologyUtils.PlanetaryDignity.EXALTED -> 1.0
                VedicAstrologyUtils.PlanetaryDignity.OWN_SIGN -> 0.5
                VedicAstrologyUtils.PlanetaryDignity.DEBILITATED -> -0.5
                else -> 0.0
            }
        }

        return duration.coerceIn(3.0, 15.0)
    }

    /**
     * Calculate Antardashas (sub-periods) within a Dasha
     */
    private fun calculateAntardashas(
        mainSign: ZodiacSign,
        startDate: LocalDateTime,
        endDate: LocalDateTime,
        chart: VedicChart
    ): List<DrigAntardasha> {
        val antardashas = mutableListOf<DrigAntardasha>()
        val totalMonths = ChronoUnit.DAYS.between(startDate, endDate) / 30.44

        // Sub-periods for each sign starting from main sign
        val subSigns = (0 until 12).map {
            ZodiacSign.entries[(mainSign.ordinal + it) % 12]
        }

        var currentStart = startDate
        val monthsPerSub = totalMonths / 12

        for (subSign in subSigns) {
            val subEnd = currentStart.plusDays((monthsPerSub * 30.44).toLong())
            val isCritical = isMarakaPeriod(chart, subSign)

            antardashas.add(
                DrigAntardasha(
                    sign = subSign,
                    startDate = currentStart,
                    endDate = subEnd,
                    durationMonths = monthsPerSub,
                    isCritical = isCritical,
                    criticalReason = if (isCritical) "Maraka influence on ${subSign.displayName}" else null
                )
            )

            currentStart = subEnd
        }

        return antardashas
    }

    /**
     * Calculate longevity span using multiple methods
     */
    private fun calculateLongevitySpan(chart: VedicChart): LongevityAnalysis {
        val factors = mutableListOf<LongevityFactor>()

        // Method 1: Lagna-8th pair
        val lagnaPairSpan = calculateLagnaPairLongevity(chart)
        factors.add(LongevityFactor("Lagna-8th Pair", lagnaPairSpan.maxYears.toDouble(), "Based on Lagna and 8th house"))

        // Method 2: Moon-Saturn pair
        val moonPairSpan = calculateMoonSaturnPairLongevity(chart)
        factors.add(LongevityFactor("Moon-Saturn Pair", moonPairSpan.maxYears.toDouble(), "Based on Moon and Saturn positions"))

        // Method 3: Saturn-Hora pair
        val saturnPairSpan = calculateSaturnHoraLongevity(chart)
        factors.add(LongevityFactor("Saturn-Hora", saturnPairSpan.maxYears.toDouble(), "Based on Saturn's hora position"))

        // Kakshya (division) contribution
        val kakshya = calculateKakshyaContribution(chart)
        factors.add(LongevityFactor("Kakshya", kakshya, "Fine-tuning based on planetary divisions"))

        // Final determination (majority method)
        val spans = listOf(lagnaPairSpan, moonPairSpan, saturnPairSpan)
        val finalSpan = spans.groupBy { it }.maxByOrNull { it.value.size }?.key ?: AyurSpan.MADHYAYU

        return LongevityAnalysis(
            lagnaPairSpan = lagnaPairSpan,
            moonPairSpan = moonPairSpan,
            saturnPairSpan = saturnPairSpan,
            finalSpan = finalSpan,
            kakshyaContribution = kakshya,
            factors = factors
        )
    }

    /**
     * Calculate longevity from Lagna-8th pair
     */
    private fun calculateLagnaPairLongevity(chart: VedicChart): AyurSpan {
        val lagnaSign = VedicAstrologyUtils.getAscendantSign(chart)
        val eighthSign = getSignFromLagna(lagnaSign, 8)
        val eighthLord = eighthSign.ruler

        val eighthLordPos = chart.planetPositions.find { it.planet == eighthLord }

        // Check if 8th lord is in Kendra, Panapara, or Apoklima from Lagna
        val houseFromLagna = eighthLordPos?.house ?: 8

        return when {
            houseFromLagna in listOf(1, 4, 7, 10) -> AyurSpan.PURNAYU
            houseFromLagna in listOf(2, 5, 8, 11) -> AyurSpan.MADHYAYU
            else -> AyurSpan.ALPAYU
        }
    }

    /**
     * Calculate longevity from Moon-Saturn pair
     */
    private fun calculateMoonSaturnPairLongevity(chart: VedicChart): AyurSpan {
        val moonPos = chart.planetPositions.find { it.planet == Planet.MOON }
        val saturnPos = chart.planetPositions.find { it.planet == Planet.SATURN }

        if (moonPos == null || saturnPos == null) return AyurSpan.MADHYAYU

        // Check if Moon and Saturn are in mutual Kendra, Trikona, etc.
        val distance = abs(moonPos.house - saturnPos.house)
        val normalizedDist = if (distance > 6) 12 - distance else distance

        return when (normalizedDist) {
            0, 3, 6 -> AyurSpan.ALPAYU  // Conjunction or 4th/10th from each other
            1, 2, 5 -> AyurSpan.MADHYAYU // 2nd/3rd or 6th
            else -> AyurSpan.PURNAYU // Trikona (4, 8 positions = 5th, 9th)
        }
    }

    /**
     * Calculate longevity from Saturn-Hora
     */
    private fun calculateSaturnHoraLongevity(chart: VedicChart): AyurSpan {
        val saturnPos = chart.planetPositions.find { it.planet == Planet.SATURN }
            ?: return AyurSpan.MADHYAYU

        // Check Saturn's dignity
        val dignity = VedicAstrologyUtils.getDignity(saturnPos)

        return when (dignity) {
            VedicAstrologyUtils.PlanetaryDignity.EXALTED,
            VedicAstrologyUtils.PlanetaryDignity.MOOLATRIKONA,
            VedicAstrologyUtils.PlanetaryDignity.OWN_SIGN -> AyurSpan.PURNAYU
            VedicAstrologyUtils.PlanetaryDignity.FRIEND_SIGN,
            VedicAstrologyUtils.PlanetaryDignity.NEUTRAL_SIGN -> AyurSpan.MADHYAYU
            else -> AyurSpan.ALPAYU
        }
    }

    /**
     * Calculate Kakshya (division) contribution to longevity
     */
    private fun calculateKakshyaContribution(chart: VedicChart): Double {
        var contribution = 0.0

        // Jupiter's position adds years
        val jupiterPos = chart.planetPositions.find { it.planet == Planet.JUPITER }
        if (jupiterPos != null) {
            if (jupiterPos.house in listOf(1, 4, 7, 10)) contribution += 8.0
            if (jupiterPos.house in listOf(5, 9)) contribution += 6.0
        }

        // Saturn's position
        val saturnPos = chart.planetPositions.find { it.planet == Planet.SATURN }
        if (saturnPos != null) {
            if (VedicAstrologyUtils.isExalted(saturnPos)) contribution += 4.0
            if (VedicAstrologyUtils.isDebilitated(saturnPos)) contribution -= 4.0
        }

        // 8th lord condition
        val lagnaSign = VedicAstrologyUtils.getAscendantSign(chart)
        val eighthLord = getSignFromLagna(lagnaSign, 8).ruler
        val eighthLordPos = chart.planetPositions.find { it.planet == eighthLord }
        if (eighthLordPos != null) {
            if (eighthLordPos.house in listOf(6, 8, 12)) contribution -= 2.0
            if (eighthLordPos.house in listOf(1, 5, 9)) contribution += 2.0
        }

        return contribution
    }

    /**
     * Calculate estimated longevity in years
     */
    private fun calculateEstimatedLongevity(analysis: LongevityAnalysis, chart: VedicChart): Double {
        val baseYears = when (analysis.finalSpan) {
            AyurSpan.ALPAYU -> 24.0
            AyurSpan.MADHYAYU -> 48.0
            AyurSpan.PURNAYU -> 72.0
        }

        return baseYears + analysis.kakshyaContribution
    }

    /**
     * Identify critical periods in the dasha sequence
     */
    private fun identifyCriticalPeriods(
        dashas: List<DrigDashaPeriod>,
        chart: VedicChart,
        rudra: ZodiacSign,
        maheshwara: ZodiacSign
    ): List<CriticalPeriod> {
        val criticalPeriods = mutableListOf<CriticalPeriod>()

        for (dasha in dashas) {
            // Rudra dasha is critical
            if (dasha.sign == rudra) {
                criticalPeriods.add(
                    CriticalPeriod(
                        startDate = dasha.startDate,
                        endDate = dasha.endDate,
                        sign = dasha.sign,
                        reason = "Rudra Dasha - Period of potential health challenges",
                        severity = 4,
                        remedies = listOf(
                            "Maha Mrityunjaya Japa daily",
                            "Rudra Abhishekam on Mondays",
                            "Donate mustard oil on Saturdays"
                        )
                    )
                )
            }

            // Maheshwara dasha
            if (dasha.sign == maheshwara) {
                criticalPeriods.add(
                    CriticalPeriod(
                        startDate = dasha.startDate,
                        endDate = dasha.endDate,
                        sign = dasha.sign,
                        reason = "Maheshwara Dasha - Transformative period requiring caution",
                        severity = 3,
                        remedies = listOf(
                            "Shiva puja on Pradosha",
                            "Chant Om Namah Shivaya",
                            "Spiritual practices recommended"
                        )
                    )
                )
            }

            // Maraka periods
            if (dasha.isMarakaPeriod) {
                criticalPeriods.add(
                    CriticalPeriod(
                        startDate = dasha.startDate,
                        endDate = dasha.endDate,
                        sign = dasha.sign,
                        reason = dasha.marakaReason ?: "Maraka influence",
                        severity = 4,
                        remedies = listOf(
                            "Health check-ups recommended",
                            "Avoid risky activities",
                            "Perform longevity-enhancing rituals"
                        )
                    )
                )
            }
        }

        return criticalPeriods.sortedBy { it.startDate }
    }

    // ============================================
    // HELPER METHODS
    // ============================================

    private fun getSignFromLagna(lagna: ZodiacSign, house: Int): ZodiacSign {
        val index = (lagna.ordinal + house - 1) % 12
        return ZodiacSign.entries[index]
    }

    private fun calculateSignStrength(chart: VedicChart, sign: ZodiacSign): Double {
        var strength = 50.0

        // Planets in sign
        val planetsInSign = chart.planetPositions.filter { it.sign == sign }
        strength += planetsInSign.size * 10

        // Sign lord's condition
        val signLord = sign.ruler
        val lordPos = chart.planetPositions.find { it.planet == signLord }
        if (lordPos != null) {
            val dignity = VedicAstrologyUtils.getDignity(lordPos)
            strength += when (dignity) {
                VedicAstrologyUtils.PlanetaryDignity.EXALTED -> 20.0
                VedicAstrologyUtils.PlanetaryDignity.OWN_SIGN -> 15.0
                VedicAstrologyUtils.PlanetaryDignity.MOOLATRIKONA -> 15.0
                VedicAstrologyUtils.PlanetaryDignity.FRIEND_SIGN -> 10.0
                VedicAstrologyUtils.PlanetaryDignity.DEBILITATED -> -10.0
                VedicAstrologyUtils.PlanetaryDignity.ENEMY_SIGN -> -5.0
                else -> 0.0
            }
        }

        return strength
    }

    private fun getAspectingPlanets(chart: VedicChart, sign: ZodiacSign): List<Planet> {
        val aspectingPlanets = mutableListOf<Planet>()
        val targetHouse = sign.number

        for (pos in chart.planetPositions) {
            if (pos.sign != sign) {
                val aspectedHouses = VedicAstrologyUtils.getAspectedHouses(pos.planet, pos.house)
                val targetHouseFromLagna = ((targetHouse - VedicAstrologyUtils.getAscendantSign(chart).number + 12) % 12) + 1
                if (targetHouseFromLagna in aspectedHouses) {
                    aspectingPlanets.add(pos.planet)
                }
            }
        }

        return aspectingPlanets
    }

    private fun isMarakaPeriod(chart: VedicChart, sign: ZodiacSign): Boolean {
        val lagnaSign = VedicAstrologyUtils.getAscendantSign(chart)
        val secondSign = getSignFromLagna(lagnaSign, 2)
        val seventhSign = getSignFromLagna(lagnaSign, 7)

        // Check if sign is 2nd or 7th from Lagna (Maraka houses)
        if (sign == secondSign || sign == seventhSign) return true

        // Check if sign lord is Maraka lord
        val secondLord = secondSign.ruler
        val seventhLord = seventhSign.ruler

        if (sign.ruler == secondLord || sign.ruler == seventhLord) return true

        // Check if Maraka planets occupy this sign
        val marakaPlanets = listOf(secondLord, seventhLord)
        val planetsInSign = chart.planetPositions.filter { it.sign == sign }.map { it.planet }

        return planetsInSign.any { it in marakaPlanets }
    }

    private fun getMarakaReason(chart: VedicChart, sign: ZodiacSign): String {
        val lagnaSign = VedicAstrologyUtils.getAscendantSign(chart)
        val secondSign = getSignFromLagna(lagnaSign, 2)
        val seventhSign = getSignFromLagna(lagnaSign, 7)

        return when (sign) {
            secondSign -> "2nd house (Maraka sthana) - affects vitality and family"
            seventhSign -> "7th house (Maraka sthana) - affects partnerships and longevity"
            else -> "Contains Maraka lord influence"
        }
    }

    private fun getHealthIndicators(chart: VedicChart, sign: ZodiacSign): List<String> {
        val indicators = mutableListOf<String>()

        // Based on sign element
        when (sign) {
            ZodiacSign.ARIES, ZodiacSign.LEO, ZodiacSign.SAGITTARIUS ->
                indicators.add("Watch for Pitta-related issues (inflammation, fever)")
            ZodiacSign.TAURUS, ZodiacSign.VIRGO, ZodiacSign.CAPRICORN ->
                indicators.add("Watch for Vata-Kapha issues (joints, digestion)")
            ZodiacSign.GEMINI, ZodiacSign.LIBRA, ZodiacSign.AQUARIUS ->
                indicators.add("Watch for Vata issues (nervous system, anxiety)")
            ZodiacSign.CANCER, ZodiacSign.SCORPIO, ZodiacSign.PISCES ->
                indicators.add("Watch for Kapha issues (water retention, emotions)")
        }

        // Based on planets in sign
        val planetsInSign = chart.planetPositions.filter { it.sign == sign }.map { it.planet }

        if (Planet.SATURN in planetsInSign) indicators.add("Chronic conditions may surface")
        if (Planet.MARS in planetsInSign) indicators.add("Accidents, surgeries possible")
        if (Planet.RAHU in planetsInSign) indicators.add("Unusual or hard-to-diagnose conditions")

        return indicators
    }

    private fun buildDashaPeriodInterpretation(
        sign: ZodiacSign,
        signLord: Planet,
        planetsInSign: List<Planet>,
        isMaraka: Boolean
    ): String {
        return buildString {
            append("${sign.displayName} Dasha ruled by ${signLord.displayName}. ")

            if (planetsInSign.isNotEmpty()) {
                append("Planets ${planetsInSign.joinToString { it.displayName }} influence this period. ")
            }

            if (isMaraka) {
                append("⚠️ This is a Maraka period - exercise caution regarding health. ")
            }

            // Sign-based general prediction
            append(
                when (sign) {
                    ZodiacSign.ARIES -> "Period of initiative and new beginnings."
                    ZodiacSign.TAURUS -> "Focus on stability, wealth, and comforts."
                    ZodiacSign.GEMINI -> "Communication, learning, and short travels highlighted."
                    ZodiacSign.CANCER -> "Domestic matters, mother, and emotional growth."
                    ZodiacSign.LEO -> "Authority, recognition, and self-expression."
                    ZodiacSign.VIRGO -> "Health, service, and analytical pursuits."
                    ZodiacSign.LIBRA -> "Relationships, partnerships, and balance."
                    ZodiacSign.SCORPIO -> "Transformation, research, and hidden matters."
                    ZodiacSign.SAGITTARIUS -> "Higher learning, dharma, and long journeys."
                    ZodiacSign.CAPRICORN -> "Career advancement and responsibilities."
                    ZodiacSign.AQUARIUS -> "Gains, social causes, and aspirations."
                    ZodiacSign.PISCES -> "Spirituality, foreign connections, and closure."
                }
            )
        }
    }

    private fun generateChartId(chart: VedicChart): String {
        val birthData = chart.birthData
        return "DRIG-${birthData.name}-${birthData.dateTime}".replace(Regex("[^a-zA-Z0-9-]"), "_")
    }

    /**
     * Get summary text for display
     */
    fun getSummary(analysis: DrigDashaAnalysis): String {
        return buildString {
            appendLine("═══════════════════════════════════════")
            appendLine("JAIMINI DRIG DASHA ANALYSIS")
            appendLine("═══════════════════════════════════════")
            appendLine()
            appendLine("Longevity Span: ${analysis.longevitySpan.displayName} (${analysis.longevitySpan.sanskritName})")
            appendLine("Estimated Years: ${String.format("%.1f", analysis.estimatedLongevity)}")
            appendLine()
            appendLine("Key Signs:")
            appendLine("  Brahma: ${analysis.brahmaSign.displayName}")
            appendLine("  Rudra: ${analysis.rudraSign.displayName}")
            appendLine("  Maheshwara: ${analysis.maheshwaraSign.displayName}")
            appendLine()

            analysis.currentDasha?.let { dasha ->
                appendLine("Current Dasha: ${dasha.sign.displayName}")
                appendLine("  Period: ${dasha.startDate.toLocalDate()} to ${dasha.endDate.toLocalDate()}")
                if (dasha.isMarakaPeriod) {
                    appendLine("  ⚠️ Maraka Period - Exercise caution")
                }
            }

            if (analysis.criticalPeriods.isNotEmpty()) {
                appendLine()
                appendLine("Critical Periods:")
                analysis.criticalPeriods.take(3).forEach { period ->
                    appendLine("  ${period.sign.displayName}: ${period.startDate.toLocalDate()}")
                    appendLine("    Reason: ${period.reason}")
                }
            }
        }
    }
}
