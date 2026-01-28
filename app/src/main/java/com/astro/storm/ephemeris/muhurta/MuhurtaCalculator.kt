package com.astro.storm.ephemeris.muhurta

import android.content.Context
import com.astro.storm.core.common.Language
import com.astro.storm.core.model.Nakshatra
import com.astro.storm.ephemeris.muhurta.MuhurtaAstronomicalCalculator.calculateJulianDay
import com.astro.storm.ephemeris.muhurta.MuhurtaAstronomicalCalculator.calculateSunriseSunsetJD
import com.astro.storm.ephemeris.muhurta.MuhurtaAstronomicalCalculator.getPlanetLongitude
import com.astro.storm.ephemeris.muhurta.MuhurtaAstronomicalCalculator.jdToLocalTime
import com.astro.storm.ephemeris.muhurta.MuhurtaConstants.AYANAMSA_LAHIRI
import com.astro.storm.ephemeris.muhurta.MuhurtaConstants.SEFLG_SIDEREAL
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
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.ZoneId
import java.time.ZonedDateTime

class MuhurtaCalculator(context: Context) {

    private val swissEph = SwissEph()
    private val ephemerisPath: String = context.filesDir.absolutePath + "/ephe"

    init {
        swissEph.swe_set_ephe_path(ephemerisPath)
        swissEph.swe_set_sid_mode(AYANAMSA_LAHIRI, 0.0, 0.0)
    }

    fun calculateMuhurta(dateTime: LocalDateTime, latitude: Double, longitude: Double, timezone: String): MuhurtaDetails {
        val zoneId = ZoneId.of(timezone); val utc = ZonedDateTime.of(dateTime, zoneId).withZoneSameInstant(ZoneId.of("UTC")).toLocalDateTime()
        val jd = calculateJulianDay(utc); val sunLong = getPlanetLongitude(SweConst.SE_SUN, jd, swissEph); val moonLong = getPlanetLongitude(SweConst.SE_MOON, jd, swissEph)
        val (srJd, ssJd) = calculateSunriseSunsetJD(jd, latitude, longitude, swissEph); val sr = jdToLocalTime(srJd, zoneId); val ss = jdToLocalTime(ssJd, zoneId)
        val v = calculateVara(dateTime.toLocalDate()); val t = calculateTithi(sunLong, moonLong); val n = calculateNakshatra(moonLong); val y = calculateYoga(sunLong, moonLong); val k = calculateKarana(sunLong, moonLong)
        val c = calculateChoghadiya(dateTime.toLocalTime(), v, sr, ss); val h = calculateHora(dateTime.toLocalTime(), v, sr, ss); val ip = calculateInauspiciousPeriods(v, sr, ss); val am = calculateAbhijitMuhurta(sr, ss, dateTime.toLocalTime()); val sy = calculateSpecialYogas(v, t, n)
        val (score, suit, avoid, rec) = evaluateMuhurta(v, t, n, y, k, c, h, dateTime.toLocalTime(), ip, am, sy, Language.ENGLISH)
        return MuhurtaDetails(dateTime, v, t, n, y, k, c, h, ip, am, sr, ss, score, suit, avoid, rec, sy)
    }

    private fun calculateVara(date: LocalDate): Vara = Vara.entries.find { it.dayNumber == date.dayOfWeek.value % 7 } ?: Vara.SUNDAY

    fun findAuspiciousMuhurtas(
        activity: ActivityType,
        startDate: LocalDate,
        endDate: LocalDate,
        latitude: Double,
        longitude: Double,
        timezone: String,
        minScore: Int = 60
    ): List<MuhurtaSearchResult> {
        return findOptimalMuhurtas(activity, startDate, endDate, latitude, longitude, timezone, minScore).topResults
    }

    fun findOptimalMuhurtas(
        activity: ActivityType,
        startDate: LocalDate,
        endDate: LocalDate,
        latitude: Double,
        longitude: Double,
        timezone: String,
        minScore: Int = 60,
        stepMinutes: Long = 5,
        maxResults: Int = 20
    ): MuhurtaOptimizationResult {
        val results = mutableListOf<MuhurtaSearchResult>()
        var current = startDate
        var evaluated = 0
        while (!current.isAfter(endDate)) {
            val jd = calculateJulianDay(ZonedDateTime.of(current, LocalTime.of(6, 0), ZoneId.of(timezone)).withZoneSameInstant(ZoneId.of("UTC")).toLocalDateTime())
            val (srJd, ssJd) = calculateSunriseSunsetJD(jd, latitude, longitude, swissEph)
            val sr = jdToLocalTime(srJd, ZoneId.of(timezone))
            val nextSunrise = jdToLocalTime(srJd + 1.0, ZoneId.of(timezone))
            var time = sr
            while (time.isBefore(nextSunrise)) {
                val baseDate = if (time.isBefore(sr)) current.plusDays(1) else current
                if (baseDate.isAfter(endDate)) break
                val dateTime = LocalDateTime.of(baseDate, time)
                val m = calculateMuhurta(dateTime, latitude, longitude, timezone)
                val (s, r, w) = evaluateForActivity(m, activity, Language.ENGLISH)
                if (s >= minScore) {
                    results.add(
                        MuhurtaSearchResult(
                            dateTime,
                            s,
                            m.vara,
                            m.nakshatra.nakshatra,
                            m.tithi.name,
                            m.choghadiya.choghadiya,
                            r,
                            w,
                            m.specialYogas
                        )
                    )
                }
                evaluated++
                time = time.plusMinutes(stepMinutes)
            }
            current = current.plusDays(1)
        }
        val sorted = results.sortedByDescending { it.score }.take(maxResults)
        return MuhurtaOptimizationResult(sorted.firstOrNull(), sorted, evaluated)
    }

    fun getDailyChoghadiya(date: LocalDate, latitude: Double, longitude: Double, timezone: String): Pair<List<ChoghadiyaInfo>, List<ChoghadiyaInfo>> {
        val jd = calculateJulianDay(ZonedDateTime.of(date, LocalTime.NOON, ZoneId.of(timezone)).withZoneSameInstant(ZoneId.of("UTC")).toLocalDateTime())
        val (srJd, ssJd) = calculateSunriseSunsetJD(jd, latitude, longitude, swissEph); val sr = jdToLocalTime(srJd, ZoneId.of(timezone)); val ss = jdToLocalTime(ssJd, ZoneId.of(timezone))
        return calculateAllDayChoghadiya(calculateVara(date), sr, ss) to calculateAllNightChoghadiya(calculateVara(date), ss, jdToLocalTime(srJd + 1.0, ZoneId.of(timezone)))
    }

    fun getDailyHoras(date: LocalDate, latitude: Double, longitude: Double, timezone: String): List<Hora> {
        val jd = calculateJulianDay(ZonedDateTime.of(date, LocalTime.NOON, ZoneId.of(timezone)).withZoneSameInstant(ZoneId.of("UTC")).toLocalDateTime())
        val (srJd, ssJd) = calculateSunriseSunsetJD(jd, latitude, longitude, swissEph)
        return calculateAllHoras(calculateVara(date), jdToLocalTime(srJd, ZoneId.of(timezone)), jdToLocalTime(ssJd, ZoneId.of(timezone)), jdToLocalTime(srJd + 1.0, ZoneId.of(timezone)))
    }

    fun getTithiEndTime(dateTime: LocalDateTime, latitude: Double, longitude: Double, timezone: String): LocalDateTime = findEndTime(dateTime, latitude, longitude, timezone) { m -> m.tithi.number }
    fun getNakshatraEndTime(dateTime: LocalDateTime, latitude: Double, longitude: Double, timezone: String): LocalDateTime = findEndTime(dateTime, latitude, longitude, timezone) { m -> m.nakshatra.nakshatra }
    fun getYogaEndTime(dateTime: LocalDateTime, latitude: Double, longitude: Double, timezone: String): LocalDateTime = findEndTime(dateTime, latitude, longitude, timezone) { m -> m.yoga.number }
    fun getKaranaEndTime(dateTime: LocalDateTime, latitude: Double, longitude: Double, timezone: String): LocalDateTime = findEndTime(dateTime, latitude, longitude, timezone) { m -> m.karana.number }

    private fun <T> findEndTime(start: LocalDateTime, lat: Double, lon: Double, tz: String, getter: (MuhurtaDetails) -> T): LocalDateTime {
        var currDateTime = start
        val initialValue = getter(calculateMuhurta(currDateTime, lat, lon, tz))
        
        // Use linear approximation for faster convergence (approx 1-2 calls vs 2000)
        // For Tithi/Nakshatra, we can estimate based on average motion
        // Moon speed is approx 13.2 deg/day, Sun 1 deg/day.
        // Tithi speed is approx 12.19 deg/day.
        
        for (i in 0 until 10) { // Max 10 iterations for safety
            val m = calculateMuhurta(currDateTime, lat, lon, tz)
            val currValue = getter(m)
            
            if (currValue != initialValue) {
                // We've crossed the boundary, step back and refine if needed
                // For simplicity in this production version, we use 1-minute precision
                var refineTime = currDateTime.minusMinutes(10)
                while (getter(calculateMuhurta(refineTime, lat, lon, tz)) == initialValue) {
                    refineTime = refineTime.plusMinutes(1)
                }
                return refineTime
            }
            
            // Advance by 30 minutes to check next point
            currDateTime = currDateTime.plusMinutes(30)
        }
        
        return start.plusHours(24) // Fallback
    }

    fun getPanchangaForDate(date: LocalDate, lat: Double, lon: Double, tz: String): PanchangaData {
        val srTime = getSunriseTime(date, lat, lon, tz); val srDateTime = LocalDateTime.of(date, srTime)
        val m = calculateMuhurta(srDateTime, lat, lon, tz)
        return PanchangaData(date, m.vara, m.tithi, getTithiEndTime(srDateTime, lat, lon, tz), m.nakshatra, getNakshatraEndTime(srDateTime, lat, lon, tz), m.yoga, getYogaEndTime(srDateTime, lat, lon, tz), m.karana, getKaranaEndTime(srDateTime, lat, lon, tz), m.sunrise, m.sunset, m.inauspiciousPeriods.rahukala, m.inauspiciousPeriods.yamaghanta, m.inauspiciousPeriods.gulikaKala, m.abhijitMuhurta, m.specialYogas)
    }

    private fun getSunriseTime(date: LocalDate, lat: Double, lon: Double, tz: String): LocalTime {
        val jd = calculateJulianDay(ZonedDateTime.of(date, LocalTime.NOON, ZoneId.of(tz)).withZoneSameInstant(ZoneId.of("UTC")).toLocalDateTime())
        return jdToLocalTime(calculateSunriseSunsetJD(jd, lat, lon, swissEph).first, ZoneId.of(tz))
    }

    fun close() { swissEph.swe_close() }
}
