package com.astro.storm.ephemeris.muhurta

import com.astro.storm.core.model.Planet
import com.astro.storm.ephemeris.VedicAstrologyUtils
import com.astro.storm.ephemeris.muhurta.MuhurtaConstants.SEFLG_SIDEREAL
import com.astro.storm.ephemeris.muhurta.MuhurtaConstants.SEFLG_SPEED
import swisseph.*
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.ZoneId
import java.time.ZonedDateTime
import kotlin.math.floor

object MuhurtaAstronomicalCalculator {

    fun getPlanetLongitude(planetId: Int, julianDay: Double, swissEph: SwissEph): Double {
        val xx = DoubleArray(6)
        val serr = StringBuffer()
        swissEph.swe_calc_ut(julianDay, planetId, SEFLG_SIDEREAL or SEFLG_SPEED, xx, serr)
        return normalizeDegrees(xx[0])
    }

    fun calculateSunriseSunsetJD(julianDay: Double, latitude: Double, longitude: Double, swissEph: SwissEph): Pair<Double, Double> {
        val geopos = doubleArrayOf(longitude, latitude, 0.0)
        val tret = DblObj()
        val serr = StringBuffer()
        val jdMidnight = floor(julianDay - 0.5) + 0.5
        swissEph.swe_rise_trans(jdMidnight, SweConst.SE_SUN, null, SweConst.SEFLG_SWIEPH, SweConst.SE_CALC_RISE or SweConst.SE_BIT_DISC_CENTER, geopos, 0.0, 0.0, tret, serr)
        val sunriseJD = tret.`val`
        swissEph.swe_rise_trans(jdMidnight, SweConst.SE_SUN, null, SweConst.SEFLG_SWIEPH, SweConst.SE_CALC_SET or SweConst.SE_BIT_DISC_CENTER, geopos, 0.0, 0.0, tret, serr)
        val sunsetJD = tret.`val`
        return Pair(sunriseJD, sunsetJD)
    }

    fun jdToLocalTime(jd: Double, zoneId: ZoneId): LocalTime {
        val sweDate = SweDate(jd)
        val utcHour = sweDate.hour
        val h = utcHour.toInt()
        val m = ((utcHour - h) * 60).toInt()
        val s = (((utcHour - h) * 60 - m) * 60).toInt().coerceIn(0, 59)
        val utcDateTime = LocalDateTime.of(sweDate.year, sweDate.month, sweDate.day, h.coerceIn(0, 23), m.coerceIn(0, 59), s)
        return ZonedDateTime.of(utcDateTime, ZoneId.of("UTC")).withZoneSameInstant(zoneId).toLocalTime()
    }

    fun calculateJulianDay(dateTime: LocalDateTime): Double {
        val decimalHours = dateTime.hour + (dateTime.minute / 60.0) + (dateTime.second / 3600.0) + (dateTime.nano / 3600000000000.0)
        return SweDate(dateTime.year, dateTime.monthValue, dateTime.dayOfMonth, decimalHours, SweDate.SE_GREG_CAL).julDay
    }

    fun normalizeDegrees(degrees: Double): Double = VedicAstrologyUtils.normalizeDegree(degrees)
}
