package com.astro.vajra.ephemeris.varshaphala

import com.astro.vajra.core.model.Nakshatra
import com.astro.vajra.core.model.Planet
import com.astro.vajra.ephemeris.varshaphala.VarshaphalaConstants.AYANAMSA_LAHIRI
import com.astro.vajra.ephemeris.varshaphala.VarshaphalaConstants.SEFLG_SIDEREAL
import com.astro.vajra.ephemeris.varshaphala.VarshaphalaConstants.SEFLG_SPEED
import com.astro.vajra.ephemeris.varshaphala.VarshaphalaConstants.SIDEREAL_YEAR_DAYS
import com.astro.vajra.ephemeris.varshaphala.VarshaphalaHelpers.calculateJulianDay
import com.astro.vajra.ephemeris.varshaphala.VarshaphalaHelpers.calculateWholeSignHouse
import com.astro.vajra.ephemeris.varshaphala.VarshaphalaHelpers.getZodiacSignFromLongitude
import com.astro.vajra.ephemeris.varshaphala.VarshaphalaHelpers.isDayChart
import com.astro.vajra.ephemeris.varshaphala.VarshaphalaHelpers.jdToLocalDateTime
import com.astro.vajra.ephemeris.varshaphala.VarshaphalaHelpers.normalizeAngle
import com.astro.vajra.ephemeris.varshaphala.VarshaphalaHelpers.resolveZoneId
import swisseph.SweConst
import swisseph.SwissEph
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZonedDateTime
import kotlin.math.abs

object SolarReturnCalculator {

    fun calculateSolarReturnTime(
        birthDateTime: LocalDateTime,
        targetYear: Int,
        latitude: Double,
        longitude: Double,
        timezone: String,
        swissEph: SwissEph
    ): LocalDateTime {
        val zoneId = resolveZoneId(timezone)
        val birthZoned = ZonedDateTime.of(birthDateTime, zoneId)
        val birthUtc = birthZoned.withZoneSameInstant(ZoneId.of("UTC"))
        val birthJd = calculateJulianDay(birthUtc.toLocalDateTime())
        val natalSunLong = getPlanetLongitude(SweConst.SE_SUN, birthJd, swissEph)
        val yearsElapsed = targetYear - birthDateTime.year
        val approximateJd = birthJd + (yearsElapsed * SIDEREAL_YEAR_DAYS)
        var currentJd = approximateJd

        repeat(50) {
            val currentSunLong = getPlanetLongitude(SweConst.SE_SUN, currentJd, swissEph)
            var diff = natalSunLong - currentSunLong
            while (diff > 180.0) diff -= 360.0
            while (diff < -180.0) diff += 360.0
            if (abs(diff) < 0.0000001) return jdToLocalDateTime(currentJd, timezone)
            val sunSpeed = getSunSpeed(currentJd, swissEph)
            val correction = diff / sunSpeed
            currentJd += correction
            if (abs(correction) < 0.00001) return jdToLocalDateTime(currentJd, timezone)
        }
        return jdToLocalDateTime(currentJd, timezone)
    }

    private fun getPlanetLongitude(planetId: Int, julianDay: Double, swissEph: SwissEph): Double {
        val xx = DoubleArray(6)
        val serr = StringBuffer()
        val rc = swissEph.swe_calc_ut(julianDay, planetId, SEFLG_SIDEREAL or SEFLG_SPEED, xx, serr)
        if (rc < 0) {
            throw IllegalStateException("Swiss Ephemeris swe_calc_ut failed for planetId=$planetId, jd=$julianDay: $serr")
        }
        return normalizeAngle(xx[0])
    }

    private fun getSunSpeed(julianDay: Double, swissEph: SwissEph): Double {
        val xx = DoubleArray(6)
        val serr = StringBuffer()
        val rc = swissEph.swe_calc_ut(julianDay, SweConst.SE_SUN, SEFLG_SIDEREAL or SEFLG_SPEED, xx, serr)
        if (rc < 0) {
            throw IllegalStateException("Swiss Ephemeris swe_calc_ut failed for Sun speed, jd=$julianDay: $serr")
        }
        return if (xx[3] != 0.0) xx[3] else 0.9856
    }

    fun calculateSolarReturnChart(
        solarReturnTime: LocalDateTime,
        latitude: Double,
        longitude: Double,
        timezone: String,
        year: Int,
        swissEph: SwissEph
    ): SolarReturnChart {
        val zonedDateTime = ZonedDateTime.of(solarReturnTime, resolveZoneId(timezone))
        val utcDateTime = zonedDateTime.withZoneSameInstant(ZoneId.of("UTC"))
        val julianDay = calculateJulianDay(utcDateTime.toLocalDateTime())
        val ayanamsa = swissEph.swe_get_ayanamsa_ut(julianDay)
        val cusps = DoubleArray(14)
        val ascmc = DoubleArray(10)
        swissEph.swe_houses(julianDay, SEFLG_SIDEREAL, latitude, longitude, 'W'.code, cusps, ascmc)
        val houseCusps = (1..12).map { normalizeAngle(cusps[it]) }
        val ascendantDegree = normalizeAngle(cusps[1])
        val ascendant = getZodiacSignFromLongitude(ascendantDegree)
        val midheaven = normalizeAngle(ascmc[1])
        val planetPositions = mutableMapOf<Planet, SolarReturnPlanetPosition>()
        for (planet in Planet.MAIN_PLANETS) {
            planetPositions[planet] = calculateSolarReturnPlanetPosition(planet, julianDay, ascendantDegree, swissEph)
        }
        val sunPos = planetPositions[Planet.SUN]?.longitude ?: 0.0
        val isDayBirth = isDayChart(sunPos, ascendantDegree)
        val moonLong = planetPositions[Planet.MOON]?.longitude ?: 0.0
        val moonSign = getZodiacSignFromLongitude(moonLong)
        val (moonNakshatra, _) = Nakshatra.fromLongitude(moonLong)
        return SolarReturnChart(
            year = year, solarReturnTime = solarReturnTime, solarReturnTimeUtc = utcDateTime.toLocalDateTime(),
            julianDay = julianDay, planetPositions = planetPositions, ascendant = ascendant,
            ascendantDegree = ascendantDegree % 30.0, midheaven = midheaven, houseCusps = houseCusps,
            ayanamsa = ayanamsa, isDayBirth = isDayBirth, moonSign = moonSign, moonNakshatra = moonNakshatra.displayName
        )
    }

    private fun calculateSolarReturnPlanetPosition(
        planet: Planet,
        julianDay: Double,
        ascendantLongitude: Double,
        swissEph: SwissEph
    ): SolarReturnPlanetPosition {
        val xx = DoubleArray(6)
        val serr = StringBuffer()
        val planetId = if (planet == Planet.KETU) -1 else planet.swissEphId
        if (planetId >= 0) {
            val rc = swissEph.swe_calc_ut(julianDay, planetId, SEFLG_SIDEREAL or SEFLG_SPEED, xx, serr)
            if (rc < 0) {
                throw IllegalStateException("Swiss Ephemeris swe_calc_ut failed for ${planet.name}, jd=$julianDay: $serr")
            }
        } else {
            val rc = swissEph.swe_calc_ut(julianDay, SweConst.SE_MEAN_NODE, SEFLG_SIDEREAL or SEFLG_SPEED, xx, serr)
            if (rc < 0) {
                throw IllegalStateException("Swiss Ephemeris swe_calc_ut failed for KETU base node, jd=$julianDay: $serr")
            }
            xx[0] = normalizeAngle(xx[0] + 180.0)
            xx[3] = -xx[3]
        }
        val longitude = normalizeAngle(xx[0])
        val sign = getZodiacSignFromLongitude(longitude)
        val house = calculateWholeSignHouse(longitude, ascendantLongitude)
        val (nakshatra, pada) = Nakshatra.fromLongitude(longitude)
        return SolarReturnPlanetPosition(
            longitude = longitude, sign = sign, house = house, degree = longitude % 30.0,
            nakshatra = nakshatra.displayName, nakshatraPada = pada, isRetrograde = xx[3] < 0, speed = xx[3]
        )
    }
}
