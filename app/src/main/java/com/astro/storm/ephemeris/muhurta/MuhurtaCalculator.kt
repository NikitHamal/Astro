package com.astro.storm.ephemeris.muhurta

import android.content.Context
import com.astro.storm.core.common.Language
import com.astro.storm.ephemeris.muhurta.MuhurtaAstronomicalCalculator.calculateJulianDay
import com.astro.storm.ephemeris.muhurta.MuhurtaAstronomicalCalculator.calculateSunriseSunsetJD
import com.astro.storm.ephemeris.muhurta.MuhurtaAstronomicalCalculator.getPlanetLongitude
import com.astro.storm.ephemeris.muhurta.MuhurtaAstronomicalCalculator.jdToLocalTime
import com.astro.storm.ephemeris.muhurta.MuhurtaConstants.AYANAMSA_LAHIRI
import com.astro.storm.ephemeris.muhurta.MuhurtaEvaluator.calculateSpecialYogas
import com.astro.storm.ephemeris.muhurta.MuhurtaEvaluator.evaluateForActivity
import com.astro.storm.ephemeris.muhurta.MuhurtaEvaluator.evaluateMuhurta
import com.astro.storm.ephemeris.muhurta.MuhurtaPanchangaEvaluator.calculateKarana
import com.astro.storm.ephemeris.muhurta.MuhurtaPanchangaEvaluator.calculateNakshatra
import com.astro.storm.ephemeris.muhurta.MuhurtaPanchangaEvaluator.calculateTithi
import com.astro.storm.ephemeris.muhurta.MuhurtaPanchangaEvaluator.calculateYoga
import com.astro.storm.ephemeris.muhurta.MuhurtaTimeSegmentCalculator.calculateAbhijitMuhurta
import com.astro.storm.ephemeris.muhurta.MuhurtaTimeSegmentCalculator.calculateAllDayChoghadiya
import com.astro.storm.ephemeris.muhurta.MuhurtaTimeSegmentCalculator.calculateAllHoras
import com.astro.storm.ephemeris.muhurta.MuhurtaTimeSegmentCalculator.calculateAllNightChoghadiya
import com.astro.storm.ephemeris.muhurta.MuhurtaTimeSegmentCalculator.calculateChoghadiya
import com.astro.storm.ephemeris.muhurta.MuhurtaTimeSegmentCalculator.calculateHora
import com.astro.storm.ephemeris.muhurta.MuhurtaTimeSegmentCalculator.calculateInauspiciousPeriods
import swisseph.SweConst
import swisseph.SwissEph
import java.time.DateTimeException
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.ZoneId
import java.time.ZoneOffset
import java.time.ZonedDateTime
import kotlin.math.abs
import kotlin.math.roundToInt

class MuhurtaCalculator(context: Context) {

    private val swissEph = SwissEph()
    private val ephemerisPath: String = context.filesDir.absolutePath + "/ephe"

    init {
        swissEph.swe_set_ephe_path(ephemerisPath)
        swissEph.swe_set_sid_mode(AYANAMSA_LAHIRI, 0.0, 0.0)
    }

    fun calculateMuhurta(dateTime: LocalDateTime, latitude: Double, longitude: Double, timezone: String): MuhurtaDetails {
        val zoneId = resolveZoneId(timezone)
        val utc = ZonedDateTime.of(dateTime, zoneId).withZoneSameInstant(ZoneId.of("UTC")).toLocalDateTime()
        val jd = calculateJulianDay(utc); val sunLong = getPlanetLongitude(SweConst.SE_SUN, jd, swissEph); val moonLong = getPlanetLongitude(SweConst.SE_MOON, jd, swissEph)
        val (srJd, ssJd) = calculateSunriseSunsetJD(jd, latitude, longitude, swissEph); val sr = jdToLocalTime(srJd, zoneId); val ss = jdToLocalTime(ssJd, zoneId)
        val v = calculateVara(dateTime.toLocalDate()); val t = calculateTithi(sunLong, moonLong); val n = calculateNakshatra(moonLong); val y = calculateYoga(sunLong, moonLong); val k = calculateKarana(sunLong, moonLong)
        val c = calculateChoghadiya(dateTime.toLocalTime(), v, sr, ss); val h = calculateHora(dateTime.toLocalTime(), v, sr, ss); val ip = calculateInauspiciousPeriods(v, sr, ss); val am = calculateAbhijitMuhurta(sr, ss, dateTime.toLocalTime()); val sy = calculateSpecialYogas(v, t, n)
        val (score, suit, avoid, rec) = evaluateMuhurta(v, t, n, y, k, c, h, dateTime.toLocalTime(), ip, am, sy, Language.ENGLISH)
        return MuhurtaDetails(dateTime, v, t, n, y, k, c, h, ip, am, sr, ss, score, suit, avoid, rec, sy)
    }

    private fun calculateVara(date: LocalDate): Vara = Vara.entries.find { it.dayNumber == date.dayOfWeek.value % 7 } ?: Vara.SUNDAY

    fun findAuspiciousMuhurtas(activity: ActivityType, startDate: LocalDate, endDate: LocalDate, latitude: Double, longitude: Double, timezone: String, minScore: Int = 60): List<MuhurtaSearchResult> {
        require(!endDate.isBefore(startDate)) { "endDate ($endDate) must be on or after startDate ($startDate)" }
        val zoneId = resolveZoneId(timezone)
        val results = mutableListOf<MuhurtaSearchResult>(); var current = startDate
        while (!current.isAfter(endDate)) {
            val jd = calculateJulianDay(ZonedDateTime.of(current, LocalTime.of(6, 0), zoneId).withZoneSameInstant(ZoneId.of("UTC")).toLocalDateTime())
            val (srJd, ssJd) = calculateSunriseSunsetJD(jd, latitude, longitude, swissEph)
            val sr = jdToLocalTime(srJd, zoneId); val ss = jdToLocalTime(ssJd, zoneId)
            if (!ss.isAfter(sr)) {
                throw IllegalStateException("Invalid daylight interval for $current at lat=$latitude lon=$longitude (sunrise=$sr sunset=$ss)")
            }
            var time = sr
            while (time.isBefore(ss)) {
                val m = calculateMuhurta(LocalDateTime.of(current, time), latitude, longitude, timezone)
                val (s, r, w) = evaluateForActivity(m, activity, Language.ENGLISH)
                if (s >= minScore) results.add(MuhurtaSearchResult(LocalDateTime.of(current, time), s, m.vara, m.nakshatra.nakshatra, m.tithi.name, m.choghadiya.choghadiya, r, w, m.specialYogas))
                time = time.plusMinutes(30)
            }
            current = current.plusDays(1)
        }
        return results.sortedByDescending { it.score }.take(20)
    }

    fun getDailyChoghadiya(date: LocalDate, latitude: Double, longitude: Double, timezone: String): Pair<List<ChoghadiyaInfo>, List<ChoghadiyaInfo>> {
        val zoneId = resolveZoneId(timezone)
        val jd = calculateJulianDay(ZonedDateTime.of(date, LocalTime.NOON, zoneId).withZoneSameInstant(ZoneId.of("UTC")).toLocalDateTime())
        val (srJd, ssJd) = calculateSunriseSunsetJD(jd, latitude, longitude, swissEph)
        val sr = jdToLocalTime(srJd, zoneId)
        val ss = jdToLocalTime(ssJd, zoneId)

        val nextDayJd = calculateJulianDay(ZonedDateTime.of(date.plusDays(1), LocalTime.NOON, zoneId).withZoneSameInstant(ZoneId.of("UTC")).toLocalDateTime())
        val (nextSrJd, _) = calculateSunriseSunsetJD(nextDayJd, latitude, longitude, swissEph)
        val nextSr = jdToLocalTime(nextSrJd, zoneId)

        return calculateAllDayChoghadiya(calculateVara(date), sr, ss) to calculateAllNightChoghadiya(calculateVara(date), ss, nextSr)
    }

    fun getDailyHoras(date: LocalDate, latitude: Double, longitude: Double, timezone: String): List<Hora> {
        val zoneId = resolveZoneId(timezone)
        val jd = calculateJulianDay(ZonedDateTime.of(date, LocalTime.NOON, zoneId).withZoneSameInstant(ZoneId.of("UTC")).toLocalDateTime())
        val (srJd, ssJd) = calculateSunriseSunsetJD(jd, latitude, longitude, swissEph)
        val nextDayJd = calculateJulianDay(ZonedDateTime.of(date.plusDays(1), LocalTime.NOON, zoneId).withZoneSameInstant(ZoneId.of("UTC")).toLocalDateTime())
        val (nextSrJd, _) = calculateSunriseSunsetJD(nextDayJd, latitude, longitude, swissEph)
        return calculateAllHoras(
            calculateVara(date),
            jdToLocalTime(srJd, zoneId),
            jdToLocalTime(ssJd, zoneId),
            jdToLocalTime(nextSrJd, zoneId)
        )
    }

    fun getTithiEndTime(dateTime: LocalDateTime, latitude: Double, longitude: Double, timezone: String): LocalDateTime = findEndTime(dateTime, latitude, longitude, timezone) { m -> m.tithi.number }
    fun getNakshatraEndTime(dateTime: LocalDateTime, latitude: Double, longitude: Double, timezone: String): LocalDateTime = findEndTime(dateTime, latitude, longitude, timezone) { m -> m.nakshatra.nakshatra }
    fun getYogaEndTime(dateTime: LocalDateTime, latitude: Double, longitude: Double, timezone: String): LocalDateTime = findEndTime(dateTime, latitude, longitude, timezone) { m -> m.yoga.number }
    fun getKaranaEndTime(dateTime: LocalDateTime, latitude: Double, longitude: Double, timezone: String): LocalDateTime = findEndTime(dateTime, latitude, longitude, timezone) { m -> m.karana.number }

    private fun <T> findEndTime(start: LocalDateTime, lat: Double, lon: Double, tz: String, getter: (MuhurtaDetails) -> T): LocalDateTime {
        val initial = getter(calculateMuhurta(start, lat, lon, tz))
        val stepMinutes = 5L
        val maxSearchHours = 72L
        val maxSteps = (maxSearchHours * 60 / stepMinutes).toInt()

        var prev = start
        for (step in 1..maxSteps) {
            val curr = start.plusMinutes(step * stepMinutes)
            val value = getter(calculateMuhurta(curr, lat, lon, tz))
            if (value != initial) {
                var lo = prev
                var hi = curr
                while (java.time.Duration.between(lo, hi).toMinutes() > 1) {
                    val midOffset = java.time.Duration.between(lo, hi).toMinutes() / 2
                    val mid = lo.plusMinutes(midOffset)
                    val midValue = getter(calculateMuhurta(mid, lat, lon, tz))
                    if (midValue == initial) lo = mid else hi = mid
                }
                return hi
            }
            prev = curr
        }
        throw IllegalStateException("Unable to find end time within $maxSearchHours hours from $start for tz=$tz")
    }

    fun getPanchangaForDate(date: LocalDate, lat: Double, lon: Double, tz: String): PanchangaData {
        val srTime = getSunriseTime(date, lat, lon, tz); val srDateTime = LocalDateTime.of(date, srTime)
        val m = calculateMuhurta(srDateTime, lat, lon, tz)
        return PanchangaData(date, m.vara, m.tithi, getTithiEndTime(srDateTime, lat, lon, tz), m.nakshatra, getNakshatraEndTime(srDateTime, lat, lon, tz), m.yoga, getYogaEndTime(srDateTime, lat, lon, tz), m.karana, getKaranaEndTime(srDateTime, lat, lon, tz), m.sunrise, m.sunset, m.inauspiciousPeriods.rahukala, m.inauspiciousPeriods.yamaghanta, m.inauspiciousPeriods.gulikaKala, m.abhijitMuhurta, m.specialYogas)
    }

    private fun getSunriseTime(date: LocalDate, lat: Double, lon: Double, tz: String): LocalTime {
        val zoneId = resolveZoneId(tz)
        val jd = calculateJulianDay(ZonedDateTime.of(date, LocalTime.NOON, zoneId).withZoneSameInstant(ZoneId.of("UTC")).toLocalDateTime())
        return jdToLocalTime(calculateSunriseSunsetJD(jd, lat, lon, swissEph).first, zoneId)
    }

    fun close() { swissEph.swe_close() }

    private fun resolveZoneId(timezone: String): ZoneId {
        try {
            return ZoneId.of(timezone)
        } catch (_: DateTimeException) {
            val trimmed = timezone.trim()
            val numericHours = trimmed.toDoubleOrNull()
            if (numericHours != null) {
                val totalSeconds = (numericHours * 3600.0).roundToInt()
                return ZoneOffset.ofTotalSeconds(totalSeconds.coerceIn(-18 * 3600, 18 * 3600))
            }
            val normalized = when {
                trimmed.startsWith("UTC", ignoreCase = true) && trimmed.length > 3 -> trimmed.removePrefix("UTC").removePrefix("utc")
                else -> trimmed
            }
            val normalizedOffset = if (normalized.matches(Regex("^[+-]\\d{1,2}(?::\\d{2})?$"))) {
                val parts = normalized.split(":")
                if (parts.size == 1) {
                    val hours = parts[0].toIntOrNull()
                    if (hours != null && abs(hours) <= 18) String.format("%+03d:00", hours) else null
                } else {
                    normalized
                }
            } else null
            if (normalizedOffset != null) {
                return ZoneOffset.of(normalizedOffset)
            }
            throw IllegalArgumentException("Invalid timezone: $timezone")
        }
    }
}
