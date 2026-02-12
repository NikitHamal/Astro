package com.astro.storm.ephemeris.prashna

import android.content.Context
import com.astro.storm.core.common.Language
import com.astro.storm.core.common.StringKeyAnalysis
import com.astro.storm.core.common.StringResources
import com.astro.storm.core.model.BirthData
import com.astro.storm.core.model.HouseSystem
import com.astro.storm.core.model.Planet
import com.astro.storm.core.model.PlanetPosition
import com.astro.storm.core.model.VedicChart
import com.astro.storm.core.model.ZodiacSign
import com.astro.storm.ephemeris.prashna.PrashnaConstants.AYANAMSA_LAHIRI
import com.astro.storm.ephemeris.prashna.PrashnaConstants.DEGREES_PER_SIGN
import com.astro.storm.ephemeris.prashna.PrashnaConstants.PRASHNA_HOUSE_SIGNIFICATIONS
import com.astro.storm.ephemeris.prashna.PrashnaConstants.SEFLG_SIDEREAL
import com.astro.storm.ephemeris.prashna.PrashnaConstants.SEFLG_SPEED
import com.astro.storm.ephemeris.prashna.PrashnaHelpers.calculateJulianDay
import com.astro.storm.ephemeris.prashna.PrashnaHelpers.determineHouse
import com.astro.storm.ephemeris.prashna.PrashnaHelpers.normalizeDegrees
import swisseph.SwissEph
import java.time.DateTimeException
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZoneOffset
import java.time.ZonedDateTime
import kotlin.math.roundToInt

/**
 * PrashnaCalculator - Refactored Production-grade Vedic Horary Astrology Engine
 */
class PrashnaCalculator(context: Context) {

    private val swissEph = SwissEph()
    private val ephemerisPath: String = context.filesDir.absolutePath + "/ephe"

    init {
        swissEph.swe_set_ephe_path(ephemerisPath)
        swissEph.swe_set_sid_mode(AYANAMSA_LAHIRI, 0.0, 0.0)
    }

    /**
     * Generate instant Prashna chart for current moment
     */
    fun generatePrashnaChart(
        question: String,
        category: PrashnaCategory,
        latitude: Double,
        longitude: Double,
        timezone: String,
        language: Language = Language.ENGLISH,
        houseSystem: HouseSystem = HouseSystem.PLACIDUS
    ): PrashnaResult {
        val questionTime = LocalDateTime.now(resolveZoneId(timezone))
        return analyzePrashna(question, category, questionTime, latitude, longitude, timezone, language, houseSystem)
    }

    /**
     * Analyze Prashna for a specific time
     */
    fun analyzePrashna(
        question: String,
        category: PrashnaCategory,
        questionTime: LocalDateTime,
        latitude: Double,
        longitude: Double,
        timezone: String,
        language: Language = Language.ENGLISH,
        houseSystem: HouseSystem = HouseSystem.PLACIDUS
    ): PrashnaResult {
        val prashnaData = BirthData(
            name = StringResources.get(StringKeyAnalysis.PRASHNA_CHART_LABEL, language),
            dateTime = questionTime,
            latitude = latitude,
            longitude = longitude,
            timezone = timezone,
            location = StringResources.get(StringKeyAnalysis.PRASHNA_QUESTION_LOCATION, language)
        )

        val chart = calculatePrashnaChart(prashnaData, houseSystem)

        val moonAnalysis = PrashnaMoonAnalyzer.analyzeMoon(chart, questionTime, language)
        val lagnaAnalysis = PrashnaLagnaAnalyzer.analyzeLagna(chart, language)
        val houseAnalysis = PrashnaHouseAnalyzer.analyzeHouses(chart, category, language)
        val specialYogas = PrashnaYogaEvaluator.detectPrashnaYogas(chart, moonAnalysis, lagnaAnalysis, language)
        val omens = PrashnaOmenEvaluator.detectOmens(chart, questionTime, moonAnalysis, language)

        val judgment = PrashnaJudgmentEvaluator.calculateJudgment(
            chart, category, moonAnalysis, lagnaAnalysis, houseAnalysis, specialYogas, omens, language
        )

        val timingPrediction = PrashnaTimingCalculator.calculateTiming(
            chart, category, moonAnalysis, lagnaAnalysis, houseAnalysis, language
        )

        val recommendations = PrashnaInterpretationGenerator.generateRecommendations(
            judgment, moonAnalysis, lagnaAnalysis, houseAnalysis, specialYogas, language
        )

        val interpretation = PrashnaInterpretationGenerator.generateDetailedInterpretation(
            question, category, judgment, moonAnalysis, lagnaAnalysis,
            houseAnalysis, timingPrediction, specialYogas, language
        )

        val confidence = PrashnaJudgmentEvaluator.calculateConfidence(
            judgment, moonAnalysis, lagnaAnalysis, specialYogas
        )

        return PrashnaResult(
            questionTime = questionTime,
            question = question,
            category = category,
            chart = chart,
            judgment = judgment,
            moonAnalysis = moonAnalysis,
            lagnaAnalysis = lagnaAnalysis,
            houseAnalysis = houseAnalysis,
            timingPrediction = timingPrediction,
            specialYogas = specialYogas,
            omens = omens,
            recommendations = recommendations,
            detailedInterpretation = interpretation,
            confidence = confidence
        )
    }

    private fun calculatePrashnaChart(birthData: BirthData, houseSystem: HouseSystem): VedicChart {
        val zoneId = resolveZoneId(birthData.timezone)
        val zonedDateTime = ZonedDateTime.of(birthData.dateTime, zoneId)
        val utcDateTime = zonedDateTime.withZoneSameInstant(ZoneId.of("UTC")).toLocalDateTime()
        val julianDay = calculateJulianDay(utcDateTime)
        val ayanamsa = swissEph.swe_get_ayanamsa_ut(julianDay)

        val houseCuspsArr = DoubleArray(13)
        val ascMcArr = DoubleArray(10)

        swissEph.swe_houses(
            julianDay,
            SEFLG_SIDEREAL,
            birthData.latitude,
            birthData.longitude,
            houseSystem.code.code,
            houseCuspsArr,
            ascMcArr
        )

        val houseCuspsList = (1..12).map { houseCuspsArr[it] }

        val planetPositions = Planet.ALL_PLANETS.map { planet ->
            calculatePlanetPosition(planet, julianDay, houseCuspsList)
        }

        return VedicChart(
            birthData = birthData,
            julianDay = julianDay,
            ayanamsa = ayanamsa,
            ayanamsaName = "Lahiri",
            ascendant = ascMcArr[0],
            midheaven = ascMcArr[1],
            planetPositions = planetPositions,
            houseCusps = houseCuspsList,
            houseSystem = houseSystem
        )
    }

    private fun calculatePlanetPosition(
        planet: Planet,
        julianDay: Double,
        houseCusps: List<Double>
    ): PlanetPosition {
        val xx = DoubleArray(6)
        val serr = StringBuffer()
        val sweId = if (planet == Planet.KETU) Planet.RAHU.swissEphId else planet.swissEphId

        val rc = swissEph.swe_calc_ut(julianDay, sweId, SEFLG_SIDEREAL or SEFLG_SPEED, xx, serr)
        if (rc < 0) {
            throw IllegalStateException("Swiss Ephemeris swe_calc_ut failed for ${planet.displayName} at jd=$julianDay: $serr")
        }

        var longitude = xx[0]
        var speed = xx[3]

        if (planet == Planet.KETU) {
            longitude = normalizeDegrees(longitude + 180.0)
            speed = -speed
        }

        longitude = normalizeDegrees(longitude)
        val sign = ZodiacSign.fromLongitude(longitude)
        val degreeInSign = longitude % DEGREES_PER_SIGN
        val wholeDegrees = degreeInSign.toInt()
        val fractionalDegrees = degreeInSign - wholeDegrees
        val totalMinutes = fractionalDegrees * 60
        val wholeMinutes = totalMinutes.toInt()
        val seconds = (totalMinutes - wholeMinutes) * 60

        val (nakshatra, pada) = com.astro.storm.core.model.Nakshatra.fromLongitude(longitude)
        val house = determineHouse(longitude, houseCusps)

        return PlanetPosition(
            planet = planet,
            longitude = longitude,
            latitude = xx[1],
            distance = xx[2],
            speed = speed,
            sign = sign,
            degree = wholeDegrees.toDouble(),
            minutes = wholeMinutes.toDouble(),
            seconds = seconds,
            isRetrograde = speed < 0,
            nakshatra = nakshatra,
            nakshatraPada = pada,
            house = house
        )
    }

    fun getHouseSignification(house: Int): PrashnaHouseSignification? {
        return PRASHNA_HOUSE_SIGNIFICATIONS[house]
    }

    fun getQuestionCategories(): List<PrashnaCategory> {
        return PrashnaCategory.entries
    }

    fun close() {
        swissEph.swe_close()
    }

    private fun resolveZoneId(timezone: String): ZoneId {
        return try {
            ZoneId.of(timezone)
        } catch (_: DateTimeException) {
            val trimmed = timezone.trim()
            val numericHours = trimmed.toDoubleOrNull()
            if (numericHours != null) {
                val totalSeconds = (numericHours * 3600.0).roundToInt()
                ZoneOffset.ofTotalSeconds(totalSeconds.coerceIn(-18 * 3600, 18 * 3600))
            } else {
                throw IllegalArgumentException("Invalid timezone: $timezone")
            }
        }
    }
}

