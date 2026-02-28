package com.astro.vajra.ephemeris.prashna

import com.astro.vajra.core.common.BikramSambatConverter
import com.astro.vajra.core.common.Language
import com.astro.vajra.core.common.StringKeyAnalysis
import com.astro.vajra.core.common.StringResources
import com.astro.vajra.core.model.Nakshatra
import com.astro.vajra.core.model.Planet
import com.astro.vajra.core.model.PlanetPosition
import com.astro.vajra.core.model.VedicChart
import com.astro.vajra.core.model.ZodiacSign
import com.astro.vajra.ephemeris.VedicAstrologyUtils
import com.astro.vajra.ephemeris.prashna.PrashnaConstants.CONJUNCTION_ORB
import com.astro.vajra.ephemeris.prashna.PrashnaConstants.DEGREES_PER_SIGN
import com.astro.vajra.ephemeris.prashna.PrashnaConstants.NATURAL_BENEFICS
import com.astro.vajra.ephemeris.prashna.PrashnaConstants.NATURAL_MALEFICS
import com.astro.vajra.ephemeris.prashna.PrashnaConstants.OPPOSITION_ORB
import com.astro.vajra.ephemeris.prashna.PrashnaConstants.SEXTILE_ORB
import com.astro.vajra.ephemeris.prashna.PrashnaConstants.SQUARE_ORB
import com.astro.vajra.ephemeris.prashna.PrashnaConstants.TRINE_ORB
import swisseph.SweDate
import java.time.LocalDateTime
import kotlin.math.abs

object PrashnaHelpers {

    fun calculateJulianDay(dateTime: LocalDateTime): Double {
        val decimalHours = dateTime.hour +
                (dateTime.minute / 60.0) +
                (dateTime.second / 3600.0) +
                (dateTime.nano / 3600000000000.0)
        val sweDate = SweDate(
            dateTime.year,
            dateTime.monthValue,
            dateTime.dayOfMonth,
            decimalHours,
            SweDate.SE_GREG_CAL
        )
        return sweDate.julDay
    }

    fun normalizeDegrees(degrees: Double): Double = VedicAstrologyUtils.normalizeDegree(degrees)

    fun angularDistance(deg1: Double, deg2: Double): Double =
        VedicAstrologyUtils.angularDistance(deg1, deg2)

    fun determineHouse(longitude: Double, houseCusps: List<Double>): Int {
        for (houseNum in 1..12) {
            val cuspStart = houseCusps[houseNum - 1]
            val cuspEnd = if (houseNum == 12) houseCusps[0] else houseCusps[houseNum]

            val normalizedLongitude = normalizeDegrees(longitude - cuspStart)
            val houseWidth = normalizeDegrees(cuspEnd - cuspStart)

            val effectiveWidth = if (houseWidth < 0.001) DEGREES_PER_SIGN else houseWidth

            if (normalizedLongitude < effectiveWidth) {
                return houseNum
            }
        }
        return 1
    }

    fun getTithiName(tithiNumber: Int, language: Language): String {
        val tithiKey = when (tithiNumber) {
            1, 16 -> StringKeyAnalysis.PRASHNA_TITHI_PRATIPADA
            2, 17 -> StringKeyAnalysis.PRASHNA_TITHI_DWITIYA
            3, 18 -> StringKeyAnalysis.PRASHNA_TITHI_TRITIYA
            4, 19 -> StringKeyAnalysis.PRASHNA_TITHI_CHATURTHI
            5, 20 -> StringKeyAnalysis.PRASHNA_TITHI_PANCHAMI
            6, 21 -> StringKeyAnalysis.PRASHNA_TITHI_SHASHTHI
            7, 22 -> StringKeyAnalysis.PRASHNA_TITHI_SAPTAMI
            8, 23 -> StringKeyAnalysis.PRASHNA_TITHI_ASHTAMI
            9, 24 -> StringKeyAnalysis.PRASHNA_TITHI_NAVAMI
            10, 25 -> StringKeyAnalysis.PRASHNA_TITHI_DASHAMI
            11, 26 -> StringKeyAnalysis.PRASHNA_TITHI_EKADASHI
            12, 27 -> StringKeyAnalysis.PRASHNA_TITHI_DWADASHI
            13, 28 -> StringKeyAnalysis.PRASHNA_TITHI_TRAYODASHI
            14, 29 -> StringKeyAnalysis.PRASHNA_TITHI_CHATURDASHI
            15 -> StringKeyAnalysis.PRASHNA_TITHI_PURNIMA
            30 -> StringKeyAnalysis.PRASHNA_TITHI_AMAVASYA
            else -> StringKeyAnalysis.PRASHNA_TITHI_PRATIPADA
        }

        val name = StringResources.get(tithiKey, language)
        
        return when {
            tithiNumber == 15 || tithiNumber == 30 -> name
            tithiNumber < 15 -> StringResources.get(StringKeyAnalysis.PRASHNA_TITHI_SHUKLA, language).format(name)
            else -> StringResources.get(StringKeyAnalysis.PRASHNA_TITHI_KRISHNA, language).format(name)
        }
    }

    fun isAspecting(fromPlanet: PlanetPosition, toPlanet: PlanetPosition): Boolean {
        val distance = angularDistance(fromPlanet.longitude, toPlanet.longitude)

        // Standard aspects
        if (distance < CONJUNCTION_ORB) return true
        if (abs(distance - 180) < OPPOSITION_ORB) return true
        if (abs(distance - 120) < TRINE_ORB) return true
        if (abs(distance - 90) < SQUARE_ORB) return true
        if (abs(distance - 60) < SEXTILE_ORB) return true

        // Special Vedic aspects
        when (fromPlanet.planet) {
            Planet.MARS -> {
                val houseDiff = ((toPlanet.house - fromPlanet.house + 12) % 12)
                if (houseDiff == 3 || houseDiff == 7) return true
            }
            Planet.JUPITER -> {
                val houseDiff = ((toPlanet.house - fromPlanet.house + 12) % 12)
                if (houseDiff == 4 || houseDiff == 8) return true
            }
            Planet.SATURN -> {
                val houseDiff = ((toPlanet.house - fromPlanet.house + 12) % 12)
                if (houseDiff == 2 || houseDiff == 9) return true
            }
            else -> {}
        }
        return false
    }

    fun isAspectingHouse(planet: PlanetPosition, targetHouse: Int): Boolean {
        val houseDiff = ((targetHouse - planet.house + 12) % 12)

        if (houseDiff == 6) return true

        when (planet.planet) {
            Planet.MARS -> if (houseDiff == 3 || houseDiff == 7) return true
            Planet.JUPITER -> if (houseDiff == 4 || houseDiff == 8) return true
            Planet.SATURN -> if (houseDiff == 2 || houseDiff == 9) return true
            else -> {}
        }

        if (planet.house == targetHouse) return true

        return false
    }

    fun getAspectType(angle: Double): AspectType {
        return when {
            abs(angle) < 5 -> AspectType.CONJUNCTION
            abs(angle - 60) < 5 -> AspectType.SEXTILE
            abs(angle - 90) < 5 -> AspectType.SQUARE
            abs(angle - 120) < 5 -> AspectType.TRINE
            abs(angle - 180) < 5 -> AspectType.OPPOSITION
            else -> AspectType.CONJUNCTION
        }
    }

    fun calculateNavamshaSign(longitude: Double): ZodiacSign {
        val normalizedLong = normalizeDegrees(longitude)
        val navamshaIndex = ((normalizedLong / (30.0 / 9.0)).toInt()) % 12
        return ZodiacSign.entries[navamshaIndex]
    }

    fun isPushkaraNavamsha(longitude: Double): Boolean {
        val navamshaInSign = ((longitude % 30) / (30.0 / 9.0)).toInt() + 1
        val sign = ZodiacSign.fromLongitude(longitude)

        val pushkaraNavamshas = when (sign) {
            ZodiacSign.ARIES, ZodiacSign.LEO, ZodiacSign.SAGITTARIUS -> listOf(7, 9)
            ZodiacSign.TAURUS, ZodiacSign.VIRGO, ZodiacSign.CAPRICORN -> listOf(3, 5)
            ZodiacSign.GEMINI, ZodiacSign.LIBRA, ZodiacSign.AQUARIUS -> listOf(6, 8)
            ZodiacSign.CANCER, ZodiacSign.SCORPIO, ZodiacSign.PISCES -> listOf(1, 3)
        }

        return navamshaInSign in pushkaraNavamshas
    }

    fun isGandanta(longitude: Double): Boolean {
        val normalizedLong = normalizeDegrees(longitude)
        val waterSignEnds = listOf(120.0, 240.0, 360.0)
        val fireSignStarts = listOf(0.0, 120.0, 240.0)
        val gandantaOrb = 3.333

        for (waterEnd in waterSignEnds) {
            if (abs(normalizedLong - waterEnd) < gandantaOrb ||
                abs(normalizedLong - (waterEnd - 360)) < gandantaOrb) {
                return true
            }
        }

        for (fireStart in fireSignStarts) {
            if (normalizedLong >= fireStart && normalizedLong < fireStart + gandantaOrb) {
                return true
            }
        }

        return false
    }

    fun calculateArudhaLagna(
        lagnaLord: Planet,
        lordPosition: PlanetPosition,
        chart: VedicChart
    ): Int {
        val lagnaHouse = 1
        val lordHouse = lordPosition.house
        val distance = lordHouse - lagnaHouse
        var arudha = lordHouse + distance
        arudha = ((arudha - 1) % 12) + 1
        if (arudha <= 0) arudha += 12
        if (arudha == 1) arudha = 10
        if (arudha == 7) arudha = 4
        return arudha
    }

    fun calculateHoraLord(dateTime: LocalDateTime): Planet {
        val dayOfWeek = dateTime.dayOfWeek.value % 7
        val hour = dateTime.hour
        val dayLords = listOf(
            Planet.SUN, Planet.MOON, Planet.MARS, Planet.MERCURY,
            Planet.JUPITER, Planet.VENUS, Planet.SATURN
        )
        val chaldeanOrder = listOf(
            Planet.SATURN, Planet.JUPITER, Planet.MARS, Planet.SUN,
            Planet.VENUS, Planet.MERCURY, Planet.MOON
        )
        val dayLordIndex = dayOfWeek
        val startingLordIndex = chaldeanOrder.indexOf(dayLords[dayLordIndex])
        val horaLordIndex = (startingLordIndex + hour) % 7
        return chaldeanOrder[horaLordIndex]
    }

    fun getDayLord(dateTime: LocalDateTime): Planet {
        val dayOfWeek = dateTime.dayOfWeek.value % 7
        return listOf(
            Planet.MOON, Planet.MARS, Planet.MERCURY, Planet.JUPITER,
            Planet.VENUS, Planet.SATURN, Planet.SUN
        )[dayOfWeek]
    }

    fun detectPlanetaryWars(chart: VedicChart): List<Pair<Planet, Planet>> {
        val wars = mutableListOf<Pair<Planet, Planet>>()
        val warOrb = 1.0
        val warringPlanets = listOf(
            Planet.MARS, Planet.MERCURY, Planet.JUPITER, Planet.VENUS, Planet.SATURN
        )
        for (i in warringPlanets.indices) {
            for (j in i + 1 until warringPlanets.size) {
                val planet1 = chart.planetPositions.first { it.planet == warringPlanets[i] }
                val planet2 = chart.planetPositions.first { it.planet == warringPlanets[j] }
                if (angularDistance(planet1.longitude, planet2.longitude) < warOrb) {
                    wars.add(Pair(warringPlanets[i], warringPlanets[j]))
                }
            }
        }
        return wars
    }

    fun getCombustionOrb(planet: Planet): Double {
        return when (planet) {
            Planet.MOON -> 12.0
            Planet.MARS -> 17.0
            Planet.MERCURY -> 14.0
            Planet.JUPITER -> 11.0
            Planet.VENUS -> 10.0
            Planet.SATURN -> 15.0
            else -> 0.0
        }
    }

    fun isAuspiciousNakshatra(nakshatra: Nakshatra): Boolean {
        return nakshatra in listOf(
            Nakshatra.ASHWINI, Nakshatra.ROHINI, Nakshatra.MRIGASHIRA,
            Nakshatra.PUNARVASU, Nakshatra.PUSHYA, Nakshatra.UTTARA_PHALGUNI,
            Nakshatra.HASTA, Nakshatra.CHITRA, Nakshatra.SWATI,
            Nakshatra.ANURADHA, Nakshatra.SHRAVANA, Nakshatra.DHANISHTHA,
            Nakshatra.UTTARA_BHADRAPADA, Nakshatra.REVATI
        )
    }

    fun Int.localized(language: Language): String {
        return if (language == Language.NEPALI) BikramSambatConverter.toNepaliNumerals(this) else this.toString()
    }

    fun Double.localized(language: Language, precision: Int = 1): String {
        val formatted = "%.${precision}f".format(this)
        return if (language == Language.NEPALI) BikramSambatConverter.toNepaliNumerals(formatted) else formatted
    }
}


