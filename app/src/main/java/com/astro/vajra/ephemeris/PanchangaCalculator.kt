package com.astro.vajra.ephemeris

import android.content.Context
import com.astro.vajra.core.common.Language
import com.astro.vajra.core.common.StringKeyAnalysis
import com.astro.vajra.core.common.StringResources
import com.astro.vajra.core.model.Nakshatra
import com.astro.vajra.core.model.Planet
import com.astro.vajra.ephemeris.panchanga.*
import swisseph.DblObj
import swisseph.SweConst
import swisseph.SweDate
import swisseph.SwissEph
import java.io.Closeable
import java.time.DateTimeException
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.ZoneId
import java.time.ZoneOffset
import java.time.ZonedDateTime
import java.util.Locale
import kotlin.math.cos
import kotlin.math.floor
import kotlin.math.roundToInt

class PanchangaCalculator(context: Context) : Closeable {

    private val swissEph: SwissEph = SwissEph()
    private val ephemerisPath: String = context.filesDir.absolutePath + "/ephe"
    @Volatile
    private var isClosed: Boolean = false

    companion object {
        private const val AYANAMSA_LAHIRI = SweConst.SE_SIDM_LAHIRI
        private const val TITHI_SPAN = 12.0
        private const val NAKSHATRA_SPAN = 360.0 / 27.0
        private const val YOGA_SPAN = 360.0 / 27.0
        private const val KARANA_SPAN = 6.0
        private const val PADA_SPAN = NAKSHATRA_SPAN / 4.0
        private const val TOTAL_TITHIS = 30
        private const val TOTAL_NAKSHATRAS = 27
        private const val TOTAL_YOGAS = 27
        private const val TOTAL_KARANAS = 60
        private const val MOVABLE_KARANAS = 7
    }

    init {
        swissEph.swe_set_ephe_path(ephemerisPath)
        swissEph.swe_set_sid_mode(AYANAMSA_LAHIRI, 0.0, 0.0)
    }

    fun calculatePanchanga(dateTime: LocalDateTime, latitude: Double, longitude: Double, timezone: String): PanchangaData {
        check(!isClosed) { "PanchangaCalculator has been closed" }
        val zoneId = resolveZoneId(timezone)
        val utc = ZonedDateTime.of(dateTime, zoneId).withZoneSameInstant(ZoneId.of("UTC")).toLocalDateTime()
        val jd = calculateJulianDay(utc.year, utc.monthValue, utc.dayOfMonth, utc.hour, utc.minute, utc.second)
        val sunSid = getPlanetLongitude(SweConst.SE_SUN, jd, true)
        val moonSid = getPlanetLongitude(SweConst.SE_MOON, jd, true)
        val sunTrop = getPlanetLongitude(SweConst.SE_SUN, jd, false)
        val moonTrop = getPlanetLongitude(SweConst.SE_MOON, jd, false)
        val tithi = calculateTithi(sunSid, moonSid)
        val localDate = dateTime.toLocalDate()
        val localNoonUtc = ZonedDateTime.of(localDate, LocalTime.NOON, zoneId).withZoneSameInstant(ZoneId.of("UTC")).toLocalDateTime()
        val jdNoon = calculateJulianDay(localNoonUtc.year, localNoonUtc.monthValue, localNoonUtc.dayOfMonth, localNoonUtc.hour, localNoonUtc.minute, localNoonUtc.second)
        val (srJd, ssJd) = calculateSunriseSunsetJD(jdNoon, latitude, longitude)
        val moonriseJd = calculateRiseSetJD(jdNoon, latitude, longitude, SweConst.SE_MOON, isRise = true)
        val moonsetJd = calculateRiseSetJD(jdNoon, latitude, longitude, SweConst.SE_MOON, isRise = false)
        val srTime = jdToLocalTime(srJd, zoneId)
        val ssTime = jdToLocalTime(ssJd, zoneId)
        val moonriseTime = moonriseJd?.let { jdToLocalTime(it, zoneId) }
        val moonsetTime = moonsetJd?.let { jdToLocalTime(it, zoneId) }
        require(ssTime.isAfter(srTime)) {
            "Invalid daylight interval for $localDate at lat=$latitude lon=$longitude (sunrise=$srTime sunset=$ssTime)"
        }
        return PanchangaData(
            tithi, calculateNakshatra(moonSid), calculateYoga(sunSid, moonSid), calculateKarana(sunSid, moonSid),
            calculateVara(localDate), if (tithi.number <= 15) Paksha.SHUKLA else Paksha.KRISHNA,
            formatLocalTime(srTime), formatLocalTime(ssTime),
            moonriseTime?.let { formatLocalTime(it) }, moonsetTime?.let { formatLocalTime(it) },
            srTime, ssTime, moonriseTime, moonsetTime, srJd, ssJd, moonriseJd, moonsetJd,
            calculateMoonPhase(sunTrop, moonTrop), sunSid, moonSid, swissEph.swe_get_ayanamsa_ut(jd)
        )
    }

    private fun getPlanetLongitude(id: Int, jd: Double, sid: Boolean): Double {
        val xx = DoubleArray(6)
        val flags = if (sid) SweConst.SEFLG_SIDEREAL or SweConst.SEFLG_SPEED or SweConst.SEFLG_SWIEPH else SweConst.SEFLG_SPEED or SweConst.SEFLG_SWIEPH
        val serr = StringBuffer()
        if (swissEph.swe_calc_ut(jd, id, flags, xx, serr) < 0) {
            throw IllegalStateException("Swiss Ephemeris swe_calc_ut failed for planetId=$id jd=$jd: $serr")
        }
        return VedicAstrologyUtils.normalizeDegree(xx[0])
    }

    private fun calculateTithi(sun: Double, moon: Double): TithiData {
        var elon = moon - sun; if (elon < 0.0) elon += 360.0
        val idx = (elon / TITHI_SPAN).toInt(); val num = idx + 1; val prog = (elon % TITHI_SPAN / TITHI_SPAN) * 100.0
        return TithiData(Tithi.entries[idx.coerceIn(0, Tithi.entries.size - 1)], num.coerceIn(1, 30), prog, getTithiLord(num), elon, TITHI_SPAN - (elon % TITHI_SPAN))
    }

    private fun getTithiLord(n: Int): Planet {
        val p = ((n - 1) % 15) + 1
        return when (p) { 1, 9 -> Planet.SUN; 2, 10 -> Planet.MOON; 3, 11 -> Planet.MARS; 4, 12 -> Planet.MERCURY; 5, 13 -> Planet.JUPITER; 6, 14 -> Planet.VENUS; 7, 15 -> Planet.SATURN; 8 -> Planet.RAHU; else -> Planet.SUN }
    }

    private fun calculateNakshatra(moon: Double): NakshatraData {
        val idx = (moon / NAKSHATRA_SPAN).toInt(); val num = idx + 1; val pos = moon % NAKSHATRA_SPAN
        val nak = Nakshatra.entries[idx.coerceIn(0, Nakshatra.entries.size - 1)]
        return NakshatraData(nak, num.coerceIn(1, 27), (pos / PADA_SPAN).toInt() % 4 + 1, (pos / NAKSHATRA_SPAN) * 100.0, nak.ruler, pos, NAKSHATRA_SPAN - pos)
    }

    private fun calculateYoga(sun: Double, moon: Double): YogaData {
        var s = (sun + moon) % 360.0; val idx = (s / YOGA_SPAN).toInt(); val num = idx + 1
        return YogaData(Yoga.entries[idx.coerceIn(0, Yoga.entries.size - 1)], num.coerceIn(1, 27), (s % YOGA_SPAN / YOGA_SPAN) * 100.0, s, YOGA_SPAN - (s % YOGA_SPAN))
    }

    private fun calculateKarana(sun: Double, moon: Double): KaranaData {
        var el = moon - sun; if (el < 0.0) el += 360.0
        val idx = (el / KARANA_SPAN).toInt(); val num = idx + 1
        val k = when (num) { 1 -> Karana.KIMSTUGHNA; 58 -> Karana.SHAKUNI; 59 -> Karana.CHATUSHPADA; 60 -> Karana.NAGA; else -> { val m = (num - 2) % 7; when (m) { 0 -> Karana.BAVA; 1 -> Karana.BALAVA; 2 -> Karana.KAULAVA; 3 -> Karana.TAITILA; 4 -> Karana.GARA; 5 -> Karana.VANIJA; else -> Karana.VISHTI } } }
        return KaranaData(k, num.coerceIn(1, 60), (el % KARANA_SPAN / KARANA_SPAN) * 100.0, KARANA_SPAN - (el % KARANA_SPAN))
    }

    private fun calculateVara(date: LocalDate): Vara = when (date.dayOfWeek.value % 7) {
        0 -> Vara.SUNDAY
        1 -> Vara.MONDAY
        2 -> Vara.TUESDAY
        3 -> Vara.WEDNESDAY
        4 -> Vara.THURSDAY
        5 -> Vara.FRIDAY
        else -> Vara.SATURDAY
    }

    private fun calculateSunriseSunsetJD(jd: Double, lat: Double, lon: Double): Pair<Double, Double> {
        val sr = calculateRiseSetJD(jd, lat, lon, SweConst.SE_SUN, isRise = true)
            ?: throw IllegalStateException("Swiss Ephemeris sunrise calculation failed at lat=$lat lon=$lon jd=$jd")
        val ss = calculateRiseSetJD(jd, lat, lon, SweConst.SE_SUN, isRise = false)
            ?: throw IllegalStateException("Swiss Ephemeris sunset calculation failed at lat=$lat lon=$lon jd=$jd")
        return sr to ss
    }

    private fun calculateRiseSetJD(
        jd: Double,
        lat: Double,
        lon: Double,
        bodyId: Int,
        isRise: Boolean
    ): Double? {
        val geo = doubleArrayOf(lon, lat, 0.0)
        val event = DblObj()
        val jdm = floor(jd - 0.5) + 0.5
        val eventFlag = if (isRise) SweConst.SE_CALC_RISE else SweConst.SE_CALC_SET
        val serr = StringBuffer()

        // Use visible limb + standard atmosphere for practical real-world rise/set timings.
        val result = swissEph.swe_rise_trans(
            jdm,
            bodyId,
            null,
            SweConst.SEFLG_SWIEPH,
            eventFlag,
            geo,
            1013.25,
            15.0,
            event,
            serr
        )

        return if (result >= 0) event.`val` else null
    }

    private fun jdToLocalTime(jd: Double, zoneId: ZoneId): LocalTime {
        val sd = SweDate(jd); val utc = ZonedDateTime.of(sd.year, sd.month, sd.day, sd.hour.toInt(), ((sd.hour - sd.hour.toInt()) * 60).toInt(), (((sd.hour - sd.hour.toInt()) * 60 - ((sd.hour - sd.hour.toInt()) * 60).toInt()) * 60).toInt().coerceIn(0, 59), 0, ZoneId.of("UTC"))
        return utc.withZoneSameInstant(zoneId).toLocalTime()
    }

    private fun formatLocalTime(t: LocalTime): String = String.format(Locale.US, "%d:%02d:%02d %s", if (t.hour == 0) 12 else if (t.hour > 12) t.hour - 12 else t.hour, t.minute, t.second, if (t.hour < 12) "AM" else "PM")

    private fun calculateMoonPhase(sun: Double, moon: Double): Double {
        var el = moon - sun; if (el < 0.0) el += 360.0
        return (1.0 - cos(Math.toRadians(el))) / 2.0 * 100.0
    }

    private fun calculateJulianDay(y: Int, m: Int, d: Int, h: Int, min: Int, s: Int): Double = SweDate(y, m, d, h + min / 60.0 + s / 3600.0, SweDate.SE_GREG_CAL).julDay

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

    override fun close() { if (!isClosed) { swissEph.swe_close(); isClosed = true } }
}


