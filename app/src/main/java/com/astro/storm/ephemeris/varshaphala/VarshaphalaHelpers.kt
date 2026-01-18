package com.astro.storm.ephemeris.varshaphala

import com.astro.storm.core.common.Language
import com.astro.storm.core.common.StringKeyAnalysis
import com.astro.storm.data.localization.stringResources
import com.astro.storm.core.model.Planet
import com.astro.storm.core.model.ZodiacSign
import com.astro.storm.ephemeris.VedicAstrologyUtils
import com.astro.storm.ephemeris.varshaphala.VarshaphalaConstants.DEBILITATION_SIGNS
import com.astro.storm.ephemeris.varshaphala.VarshaphalaConstants.EXALTATION_DEGREES
import com.astro.storm.ephemeris.varshaphala.VarshaphalaConstants.FRIENDSHIPS
import com.astro.storm.ephemeris.varshaphala.VarshaphalaConstants.NEUTRALS
import swisseph.SweDate
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZonedDateTime
import kotlin.math.abs

object VarshaphalaHelpers {

    fun calculateJulianDay(dateTime: LocalDateTime): Double {
        val decimalHours = dateTime.hour + (dateTime.minute / 60.0) + (dateTime.second / 3600.0) + (dateTime.nano / 3600000000000.0)
        val sweDate = SweDate(dateTime.year, dateTime.monthValue, dateTime.dayOfMonth, decimalHours, SweDate.SE_GREG_CAL)
        return sweDate.julDay
    }

    fun jdToLocalDateTime(jd: Double, timezone: String): LocalDateTime {
        val sweDate = SweDate(jd)
        val year = sweDate.year
        val month = sweDate.month
        val day = sweDate.day
        val hourDouble = sweDate.hour
        val hour = hourDouble.toInt()
        val minuteDouble = (hourDouble - hour) * 60.0
        val minute = minuteDouble.toInt()
        val secondDouble = (minuteDouble - minute) * 60.0
        val second = secondDouble.toInt()
        val nano = ((secondDouble - second) * 1000000000).toInt()

        val utcDateTime = LocalDateTime.of(year, month, day, hour, minute, second, nano)
        val utcZoned = ZonedDateTime.of(utcDateTime, ZoneId.of("UTC"))
        return utcZoned.withZoneSameInstant(ZoneId.of(timezone)).toLocalDateTime()
    }

    fun normalizeAngle(angle: Double): Double = VedicAstrologyUtils.normalizeDegree(angle)

    fun getZodiacSignFromLongitude(longitude: Double): ZodiacSign = ZodiacSign.fromLongitude(longitude)

    fun getStandardZodiacIndex(sign: ZodiacSign): Int = sign.number - 1

    fun calculateWholeSignHouse(planetLongitude: Double, ascendantLongitude: Double): Int {
        val ascSignIndex = (ascendantLongitude / 30.0).toInt()
        val planetSignIndex = (planetLongitude / 30.0).toInt()
        return ((planetSignIndex - ascSignIndex + 12) % 12) + 1
    }

    fun isDayChart(sunLongitude: Double, ascendantLongitude: Double): Boolean {
        val sunHouse = calculateWholeSignHouse(sunLongitude, ascendantLongitude)
        return sunHouse in 7..12
    }

    fun areFriends(p1: Planet, p2: Planet): Boolean = FRIENDSHIPS[p1]?.contains(p2) == true
    fun areNeutral(p1: Planet, p2: Planet): Boolean = NEUTRALS[p1]?.contains(p2) == true

    fun isExalted(planet: Planet, sign: ZodiacSign): Boolean {
        val exDegree = EXALTATION_DEGREES[planet] ?: return false
        return (exDegree / 30.0).toInt() == getStandardZodiacIndex(sign)
    }

    fun isDebilitated(planet: Planet, sign: ZodiacSign): Boolean = DEBILITATION_SIGNS[planet] == sign

    fun evaluatePlanetStrengthDescription(planet: Planet, chart: SolarReturnChart, language: Language): String {
        val pos = chart.planetPositions[planet] ?: return StringResources.get(StringKeyAnalysis.VARSHA_STRENGTH_UNKNOWN, language)
        return when {
            isExalted(planet, pos.sign) -> StringResources.get(StringKeyAnalysis.VARSHA_STRENGTH_EXALTED, language)
            pos.sign.ruler == planet -> StringResources.get(StringKeyAnalysis.VARSHA_STRENGTH_STRONG, language)
            pos.house in listOf(1, 4, 7, 10) -> StringResources.get(StringKeyAnalysis.VARSHA_STRENGTH_ANGULAR, language)
            isDebilitated(planet, pos.sign) -> StringResources.get(StringKeyAnalysis.VARSHA_STRENGTH_DEBILITATED, language)
            else -> StringResources.get(StringKeyAnalysis.VARSHA_STRENGTH_MODERATE, language)
        }
    }

    fun getHouseSignificance(house: Int, language: Language): String {
        val key = when (house) {
            1 -> StringKeyAnalysis.HOUSE_1_SIGNIFICANCE
            2 -> StringKeyAnalysis.HOUSE_2_SIGNIFICANCE
            3 -> StringKeyAnalysis.HOUSE_3_SIGNIFICANCE
            4 -> StringKeyAnalysis.HOUSE_4_SIGNIFICANCE
            5 -> StringKeyAnalysis.HOUSE_5_SIGNIFICANCE
            6 -> StringKeyAnalysis.HOUSE_6_SIGNIFICANCE
            7 -> StringKeyAnalysis.HOUSE_7_SIGNIFICANCE
            8 -> StringKeyAnalysis.HOUSE_8_SIGNIFICANCE
            9 -> StringKeyAnalysis.HOUSE_9_SIGNIFICANCE
            10 -> StringKeyAnalysis.HOUSE_10_SIGNIFICANCE
            11 -> StringKeyAnalysis.HOUSE_11_SIGNIFICANCE
            12 -> StringKeyAnalysis.HOUSE_12_SIGNIFICANCE
            else -> StringKeyAnalysis.LABEL_UNKNOWN
        }
        return StringResources.get(key, language)
    }
}

