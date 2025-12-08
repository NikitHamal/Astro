package com.astro.storm.ephemeris

import android.content.Context
import androidx.annotation.StringRes
import com.astro.storm.R
import com.astro.storm.data.model.Nakshatra
import com.astro.storm.data.model.Planet
import swisseph.DblObj
import swisseph.SweConst
import swisseph.SweDate
import swisseph.SwissEph
import java.io.Closeable
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZonedDateTime
import java.util.Locale
import kotlin.math.cos
import kotlin.math.floor

class PanchangaCalculator(context: Context) : Closeable {

    private val swissEph: SwissEph = SwissEph()
    private val ephemerisPath: String
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
        private const val MOVABLE_KARANA_CYCLES = 8
    }

    init {
        ephemerisPath = context.filesDir.absolutePath + "/ephe"
        swissEph.swe_set_ephe_path(ephemerisPath)
        swissEph.swe_set_sid_mode(AYANAMSA_LAHIRI, 0.0, 0.0)
    }

    fun calculatePanchanga(
        dateTime: LocalDateTime,
        latitude: Double,
        longitude: Double,
        timezone: String
    ): PanchangaData {
        check(!isClosed) { "PanchangaCalculator has been closed" }

        require(latitude in -90.0..90.0) {
            "Latitude must be between -90 and 90 degrees, got $latitude"
        }
        require(longitude in -180.0..180.0) {
            "Longitude must be between -180 and 180 degrees, got $longitude"
        }

        val zoneId = try {
            ZoneId.of(timezone)
        } catch (e: Exception) {
            throw IllegalArgumentException("Invalid timezone: $timezone", e)
        }

        val zonedDateTime = ZonedDateTime.of(dateTime, zoneId)
        val utcDateTime = zonedDateTime.withZoneSameInstant(ZoneId.of("UTC"))

        val julianDay = calculateJulianDay(
            utcDateTime.year,
            utcDateTime.monthValue,
            utcDateTime.dayOfMonth,
            utcDateTime.hour,
            utcDateTime.minute,
            utcDateTime.second
        )

        val sunLongitudeSidereal = getPlanetLongitude(SweConst.SE_SUN, julianDay, sidereal = true)
        val moonLongitudeSidereal = getPlanetLongitude(SweConst.SE_MOON, julianDay, sidereal = true)

        val sunLongitudeTropical = getPlanetLongitude(SweConst.SE_SUN, julianDay, sidereal = false)
        val moonLongitudeTropical = getPlanetLongitude(SweConst.SE_MOON, julianDay, sidereal = false)

        val tithi = calculateTithi(sunLongitudeSidereal, moonLongitudeSidereal)
        val nakshatra = calculateNakshatra(moonLongitudeSidereal)
        val yoga = calculateYoga(sunLongitudeSidereal, moonLongitudeSidereal)
        val karana = calculateKarana(sunLongitudeSidereal, moonLongitudeSidereal)
        val vara = calculateVara(julianDay)
        val paksha = determinePaksha(tithi.number)

        val (sunrise, sunset) = calculateSunriseSunset(julianDay, latitude, longitude, zoneId)
        val moonPhase = calculateMoonPhase(sunLongitudeTropical, moonLongitudeTropical)

        val ayanamsa = swissEph.swe_get_ayanamsa_ut(julianDay)

        return PanchangaData(
            tithi = tithi,
            nakshatra = nakshatra,
            yoga = yoga,
            karana = karana,
            vara = vara,
            paksha = paksha,
            sunrise = sunrise,
            sunset = sunset,
            moonPhase = moonPhase,
            sunLongitude = sunLongitudeSidereal,
            moonLongitude = moonLongitudeSidereal,
            ayanamsa = ayanamsa
        )
    }

    private fun getPlanetLongitude(planetId: Int, julianDay: Double, sidereal: Boolean): Double {
        val positions = DoubleArray(6)
        val errorBuffer = StringBuffer()

        val flags = if (sidereal) {
            SweConst.SEFLG_SIDEREAL or SweConst.SEFLG_SPEED or SweConst.SEFLG_SWIEPH
        } else {
            SweConst.SEFLG_SPEED or SweConst.SEFLG_SWIEPH
        }

        val result = swissEph.swe_calc_ut(julianDay, planetId, flags, positions, errorBuffer)

        if (result < 0) {
            throw PanchangaCalculationException(
                "Failed to calculate planet position for planet ID $planetId: $errorBuffer"
            )
        }

        return normalizeDegrees(positions[0])
    }

    private fun normalizeDegrees(degrees: Double): Double {
        var normalized = degrees % 360.0
        if (normalized < 0.0) normalized += 360.0
        if (normalized >= 360.0) normalized = 0.0
        return normalized
    }

    private fun calculateLunarElongation(sunLongitude: Double, moonLongitude: Double): Double {
        var elongation = moonLongitude - sunLongitude
        if (elongation < 0.0) elongation += 360.0
        if (elongation >= 360.0) elongation = 0.0
        return elongation
    }

    private fun calculateTithi(sunLongitude: Double, moonLongitude: Double): TithiData {
        val elongation = calculateLunarElongation(sunLongitude, moonLongitude)

        val tithiIndex = (elongation / TITHI_SPAN).toInt()
        val tithiNumber = tithiIndex + 1
        val progressInTithi = elongation % TITHI_SPAN
        val tithiProgress = (progressInTithi / TITHI_SPAN) * 100.0

        val clampedIndex = tithiIndex.coerceIn(0, Tithi.entries.size - 1)
        val tithi = Tithi.entries[clampedIndex]
        val lord = getTithiLord(tithiNumber)

        val remainingDegrees = TITHI_SPAN - progressInTithi

        return TithiData(
            tithi = tithi,
            number = tithiNumber.coerceIn(1, TOTAL_TITHIS),
            progress = tithiProgress,
            lord = lord,
            elongation = elongation,
            remainingDegrees = remainingDegrees
        )
    }

    private fun getTithiLord(tithiNumber: Int): Planet {
        val tithiInPaksha = ((tithiNumber - 1) % 15) + 1
        return when (tithiInPaksha) {
            1 -> Planet.SUN
            2 -> Planet.MOON
            3 -> Planet.MARS
            4 -> Planet.MERCURY
            5 -> Planet.JUPITER
            6 -> Planet.VENUS
            7 -> Planet.SATURN
            8 -> Planet.RAHU
            9 -> Planet.SUN
            10 -> Planet.MOON
            11 -> Planet.MARS
            12 -> Planet.MERCURY
            13 -> Planet.JUPITER
            14 -> Planet.VENUS
            15 -> Planet.SATURN
            else -> Planet.SUN
        }
    }

    private fun calculateNakshatra(moonLongitude: Double): NakshatraData {
        val nakshatraIndex = (moonLongitude / NAKSHATRA_SPAN).toInt()
        val nakshatraNumber = nakshatraIndex + 1

        val positionInNakshatra = moonLongitude % NAKSHATRA_SPAN
        val nakshatraProgress = (positionInNakshatra / NAKSHATRA_SPAN) * 100.0

        val padaIndex = (positionInNakshatra / PADA_SPAN).toInt()
        val pada = (padaIndex % 4) + 1

        val clampedIndex = nakshatraIndex.coerceIn(0, Nakshatra.entries.size - 1)
        val nakshatra = Nakshatra.entries[clampedIndex]

        val remainingDegrees = NAKSHATRA_SPAN - positionInNakshatra

        return NakshatraData(
            nakshatra = nakshatra,
            number = nakshatraNumber.coerceIn(1, TOTAL_NAKSHATRAS),
            pada = pada,
            progress = nakshatraProgress,
            lord = nakshatra.ruler,
            degreeInNakshatra = positionInNakshatra,
            remainingDegrees = remainingDegrees
        )
    }

    private fun calculateYoga(sunLongitude: Double, moonLongitude: Double): YogaData {
        var sum = sunLongitude + moonLongitude

        while (sum >= 360.0) {
            sum -= 360.0
        }

        val yogaIndex = (sum / YOGA_SPAN).toInt()
        val yogaNumber = yogaIndex + 1
        val progressInYoga = sum % YOGA_SPAN
        val yogaProgress = (progressInYoga / YOGA_SPAN) * 100.0

        val clampedIndex = yogaIndex.coerceIn(0, Yoga.entries.size - 1)
        val yoga = Yoga.entries[clampedIndex]

        val remainingDegrees = YOGA_SPAN - progressInYoga

        return YogaData(
            yoga = yoga,
            number = yogaNumber.coerceIn(1, TOTAL_YOGAS),
            progress = yogaProgress,
            combinedLongitude = sum,
            remainingDegrees = remainingDegrees
        )
    }

    private fun calculateKarana(sunLongitude: Double, moonLongitude: Double): KaranaData {
        val elongation = calculateLunarElongation(sunLongitude, moonLongitude)

        val karanaIndex = (elongation / KARANA_SPAN).toInt()
        val karanaNumber = karanaIndex + 1
        val progressInKarana = elongation % KARANA_SPAN
        val karanaProgress = (progressInKarana / KARANA_SPAN) * 100.0

        val karana = getKaranaFromNumber(karanaNumber)

        val remainingDegrees = KARANA_SPAN - progressInKarana

        return KaranaData(
            karana = karana,
            number = karanaNumber.coerceIn(1, TOTAL_KARANAS),
            progress = karanaProgress,
            remainingDegrees = remainingDegrees
        )
    }

    private fun getKaranaFromNumber(karanaNumber: Int): Karana {
        return when (karanaNumber) {
            1 -> Karana.KIMSTUGHNA
            58 -> Karana.SHAKUNI
            59 -> Karana.CHATUSHPADA
            60 -> Karana.NAGA
            else -> {
                val adjustedNumber = karanaNumber - 2
                val movableIndex = adjustedNumber % MOVABLE_KARANAS
                when (movableIndex) {
                    0 -> Karana.BAVA
                    1 -> Karana.BALAVA
                    2 -> Karana.KAULAVA
                    3 -> Karana.TAITILA
                    4 -> Karana.GARA
                    5 -> Karana.VANIJA
                    6 -> Karana.VISHTI
                    else -> Karana.BAVA
                }
            }
        }
    }

    private fun calculateVara(julianDay: Double): Vara {
        val dayNumber = (floor(julianDay + 1.5).toLong() % 7).toInt()
        return Vara.entries[dayNumber.coerceIn(0, Vara.entries.size - 1)]
    }

    private fun determinePaksha(tithiNumber: Int): Paksha {
        return if (tithiNumber <= 15) Paksha.SHUKLA else Paksha.KRISHNA
    }

    private fun calculateSunriseSunset(
        julianDay: Double,
        latitude: Double,
        longitude: Double,
        zoneId: ZoneId
    ): Pair<String, String> {
        val geopos = doubleArrayOf(longitude, latitude, 0.0)
        val timeResult = DblObj()
        val errorBuffer = StringBuffer()

        val jdMidnight = floor(julianDay - 0.5) + 0.5

        val sunriseFlags = SweConst.SE_CALC_RISE or SweConst.SE_BIT_DISC_CENTER
        val riseResult = swissEph.swe_rise_trans(
            jdMidnight,
            SweConst.SE_SUN,
            null,
            SweConst.SEFLG_SWIEPH,
            sunriseFlags,
            geopos,
            1013.25,
            15.0,
            timeResult,
            errorBuffer
        )

        val sunriseJD: Double? = if (riseResult >= 0 && timeResult.`val` > jdMidnight) {
            timeResult.`val`
        } else {
            null
        }

        val sunsetFlags = SweConst.SE_CALC_SET or SweConst.SE_BIT_DISC_CENTER
        val setResult = swissEph.swe_rise_trans(
            jdMidnight,
            SweConst.SE_SUN,
            null,
            SweConst.SEFLG_SWIEPH,
            sunsetFlags,
            geopos,
            1013.25,
            15.0,
            timeResult,
            errorBuffer
        )

        val sunsetJD: Double? = if (setResult >= 0 && timeResult.`val` > jdMidnight) {
            timeResult.`val`
        } else {
            null
        }

        val sunrise = sunriseJD?.let { formatJulianDayToLocalTime(it, zoneId) } ?: "N/A"
        val sunset = sunsetJD?.let { formatJulianDayToLocalTime(it, zoneId) } ?: "N/A"

        return Pair(sunrise, sunset)
    }

    private fun formatJulianDayToLocalTime(julianDay: Double, zoneId: ZoneId): String {
        val sweDate = SweDate(julianDay)

        val utcHour = sweDate.hour
        val hourInt = utcHour.toInt()
        val minuteFraction = (utcHour - hourInt) * 60.0
        val minuteInt = minuteFraction.toInt()
        val secondFraction = (minuteFraction - minuteInt) * 60.0
        val secondInt = secondFraction.toInt()

        val utcZonedDateTime = ZonedDateTime.of(
            sweDate.year,
            sweDate.month,
            sweDate.day,
            hourInt,
            minuteInt,
            secondInt,
            0,
            ZoneId.of("UTC")
        )

        val localDateTime = utcZonedDateTime.withZoneSameInstant(zoneId)

        val hour = localDateTime.hour
        val minute = localDateTime.minute
        val second = localDateTime.second

        val amPm = if (hour < 12) "AM" else "PM"
        val displayHour = when {
            hour == 0 -> 12
            hour > 12 -> hour - 12
            else -> hour
        }

        return String.format(Locale.US, "%d:%02d:%02d %s", displayHour, minute, second, amPm)
    }

    private fun calculateMoonPhase(sunLongitudeTropical: Double, moonLongitudeTropical: Double): Double {
        var elongation = moonLongitudeTropical - sunLongitudeTropical
        if (elongation < 0.0) elongation += 360.0
        if (elongation >= 360.0) elongation = 0.0

        val elongationRadians = Math.toRadians(elongation)
        val illumination = (1.0 - cos(elongationRadians)) / 2.0 * 100.0

        return illumination.coerceIn(0.0, 100.0)
    }

    private fun calculateJulianDay(
        year: Int,
        month: Int,
        day: Int,
        hour: Int,
        minute: Int,
        second: Int
    ): Double {
        val decimalHours = hour + (minute / 60.0) + (second / 3600.0)
        val sweDate = SweDate(year, month, day, decimalHours, SweDate.SE_GREG_CAL)
        return sweDate.julDay
    }

    override fun close() {
        if (!isClosed) {
            synchronized(this) {
                if (!isClosed) {
                    swissEph.swe_close()
                    isClosed = true
                }
            }
        }
    }
}

class PanchangaCalculationException(message: String, cause: Throwable? = null) : RuntimeException(message, cause)

enum class Tithi(val number: Int, @StringRes val stringRes: Int, @StringRes val sanskrit: Int, val group: TithiGroup) {
    PRATIPADA(1, R.string.tithi_pratipada, R.string.tithi_pratipada_sanskrit, TithiGroup.NANDA),
    DWITIYA(2, R.string.tithi_dwitiya, R.string.tithi_dwitiya_sanskrit, TithiGroup.BHADRA),
    TRITIYA(3, R.string.tithi_tritiya, R.string.tithi_tritiya_sanskrit, TithiGroup.JAYA),
    CHATURTHI(4, R.string.tithi_chaturthi, R.string.tithi_chaturthi_sanskrit, TithiGroup.RIKTA),
    PANCHAMI(5, R.string.tithi_panchami, R.string.tithi_panchami_sanskrit, TithiGroup.PURNA),
    SHASHTHI(6, R.string.tithi_shashthi, R.string.tithi_shashthi_sanskrit, TithiGroup.NANDA),
    SAPTAMI(7, R.string.tithi_saptami, R.string.tithi_saptami_sanskrit, TithiGroup.BHADRA),
    ASHTAMI(8, R.string.tithi_ashtami, R.string.tithi_ashtami_sanskrit, TithiGroup.JAYA),
    NAVAMI(9, R.string.tithi_navami, R.string.tithi_navami_sanskrit, TithiGroup.RIKTA),
    DASHAMI(10, R.string.tithi_dashami, R.string.tithi_dashami_sanskrit, TithiGroup.PURNA),
    EKADASHI(11, R.string.tithi_ekadashi, R.string.tithi_ekadashi_sanskrit, TithiGroup.NANDA),
    DWADASHI(12, R.string.tithi_dwadashi, R.string.tithi_dwadashi_sanskrit, TithiGroup.BHADRA),
    TRAYODASHI(13, R.string.tithi_trayodashi, R.string.tithi_trayodashi_sanskrit, TithiGroup.JAYA),
    CHATURDASHI(14, R.string.tithi_chaturdashi, R.string.tithi_chaturdashi_sanskrit, TithiGroup.RIKTA),
    PURNIMA(15, R.string.tithi_purnima, R.string.tithi_purnima_sanskrit, TithiGroup.PURNA),
    PRATIPADA_K(16, R.string.tithi_pratipada, R.string.tithi_pratipada_sanskrit, TithiGroup.NANDA),
    DWITIYA_K(17, R.string.tithi_dwitiya, R.string.tithi_dwitiya_sanskrit, TithiGroup.BHADRA),
    TRITIYA_K(18, R.string.tithi_tritiya, R.string.tithi_tritiya_sanskrit, TithiGroup.JAYA),
    CHATURTHI_K(19, R.string.tithi_chaturthi, R.string.tithi_chaturthi_sanskrit, TithiGroup.RIKTA),
    PANCHAMI_K(20, R.string.tithi_panchami, R.string.tithi_panchami_sanskrit, TithiGroup.PURNA),
    SHASHTHI_K(21, R.string.tithi_shashthi, R.string.tithi_shashthi_sanskrit, TithiGroup.NANDA),
    SAPTAMI_K(22, R.string.tithi_saptami, R.string.tithi_saptami_sanskrit, TithiGroup.BHADRA),
    ASHTAMI_K(23, R.string.tithi_ashtami, R.string.tithi_ashtami_sanskrit, TithiGroup.JAYA),
    NAVAMI_K(24, R.string.tithi_navami, R.string.tithi_navami_sanskrit, TithiGroup.RIKTA),
    DASHAMI_K(25, R.string.tithi_dashami, R.string.tithi_dashami_sanskrit, TithiGroup.PURNA),
    EKADASHI_K(26, R.string.tithi_ekadashi, R.string.tithi_ekadashi_sanskrit, TithiGroup.NANDA),
    DWADASHI_K(27, R.string.tithi_dwadashi, R.string.tithi_dwadashi_sanskrit, TithiGroup.BHADRA),
    TRAYODASHI_K(28, R.string.tithi_trayodashi, R.string.tithi_trayodashi_sanskrit, TithiGroup.JAYA),
    CHATURDASHI_K(29, R.string.tithi_chaturdashi, R.string.tithi_chaturdashi_sanskrit, TithiGroup.RIKTA),
    AMAVASYA(30, R.string.tithi_amavasya, R.string.tithi_amavasya_sanskrit, TithiGroup.PURNA)
}

enum class TithiGroup(@StringRes val stringRes: Int, @StringRes val nature: Int) {
    NANDA(R.string.tithi_group_nanda, R.string.tithi_group_nanda_nature),
    BHADRA(R.string.tithi_group_bhadra, R.string.tithi_group_bhadra_nature),
    JAYA(R.string.tithi_group_jaya, R.string.tithi_group_jaya_nature),
    RIKTA(R.string.tithi_group_rikta, R.string.tithi_group_rikta_nature),
    PURNA(R.string.tithi_group_purna, R.string.tithi_group_purna_nature)
}

enum class Yoga(val number: Int, @StringRes val stringRes: Int, @StringRes val sanskrit: Int, val nature: YogaNature) {
    VISHKUMBHA(1, R.string.yoga_vishkumbha, R.string.yoga_vishkumbha_sanskrit, YogaNature.INAUSPICIOUS),
    PRITI(2, R.string.yoga_priti, R.string.yoga_priti_sanskrit, YogaNature.AUSPICIOUS),
    AYUSHMAN(3, R.string.yoga_ayushman, R.string.yoga_ayushman_sanskrit, YogaNature.AUSPICIOUS),
    SAUBHAGYA(4, R.string.yoga_saubhagya, R.string.yoga_saubhagya_sanskrit, YogaNature.AUSPICIOUS),
    SHOBHANA(5, R.string.yoga_shobhana, R.string.yoga_shobhana_sanskrit, YogaNature.AUSPICIOUS),
    ATIGANDA(6, R.string.yoga_atiganda, R.string.yoga_atiganda_sanskrit, YogaNature.INAUSPICIOUS),
    SUKARMA(7, R.string.yoga_sukarma, R.string.yoga_sukarma_sanskrit, YogaNature.AUSPICIOUS),
    DHRITI(8, R.string.yoga_dhriti, R.string.yoga_dhriti_sanskrit, YogaNature.AUSPICIOUS),
    SHULA(9, R.string.yoga_shula, R.string.yoga_shula_sanskrit, YogaNature.INAUSPICIOUS),
    GANDA(10, R.string.yoga_ganda, R.string.yoga_ganda_sanskrit, YogaNature.INAUSPICIOUS),
    VRIDDHI(11, R.string.yoga_vriddhi, R.string.yoga_vriddhi_sanskrit, YogaNature.AUSPICIOUS),
    DHRUVA(12, R.string.yoga_dhruva, R.string.yoga_dhruva_sanskrit, YogaNature.AUSPICIOUS),
    VYAGHATA(13, R.string.yoga_vyaghata, R.string.yoga_vyaghata_sanskrit, YogaNature.INAUSPICIOUS),
    HARSHANA(14, R.string.yoga_harshana, R.string.yoga_harshana_sanskrit, YogaNature.AUSPICIOUS),
    VAJRA(15, R.string.yoga_vajra, R.string.yoga_vajra_sanskrit, YogaNature.MIXED),
    SIDDHI(16, R.string.yoga_siddhi, R.string.yoga_siddhi_sanskrit, YogaNature.AUSPICIOUS),
    VYATIPATA(17, R.string.yoga_vyatipata, R.string.yoga_vyatipata_sanskrit, YogaNature.INAUSPICIOUS),
    VARIYAN(18, R.string.yoga_variyan, R.string.yoga_variyan_sanskrit, YogaNature.AUSPICIOUS),
    PARIGHA(19, R.string.yoga_parigha, R.string.yoga_parigha_sanskrit, YogaNature.INAUSPICIOUS),
    SHIVA(20, R.string.yoga_shiva, R.string.yoga_shiva_sanskrit, YogaNature.AUSPICIOUS),
    SIDDHA(21, R.string.yoga_siddha, R.string.yoga_siddha_sanskrit, YogaNature.AUSPICIOUS),
    SADHYA(22, R.string.yoga_sadhya, R.string.yoga_sadhya_sanskrit, YogaNature.AUSPICIOUS),
    SHUBHA(23, R.string.yoga_shubha, R.string.yoga_shubha_sanskrit, YogaNature.AUSPICIOUS),
    SHUKLA(24, R.string.yoga_shukla, R.string.yoga_shukla_sanskrit, YogaNature.AUSPICIOUS),
    BRAHMA(25, R.string.yoga_brahma, R.string.yoga_brahma_sanskrit, YogaNature.AUSPICIOUS),
    INDRA(26, R.string.yoga_indra, R.string.yoga_indra_sanskrit, YogaNature.AUSPICIOUS),
    VAIDHRITI(27, R.string.yoga_vaidhriti, R.string.yoga_vaidhriti_sanskrit, YogaNature.INAUSPICIOUS)
}

enum class YogaNature(@StringRes val stringRes: Int) {
    AUSPICIOUS(R.string.yoga_nature_auspicious),
    INAUSPICIOUS(R.string.yoga_nature_inauspicious),
    MIXED(R.string.yoga_nature_mixed)
}

enum class Karana(@StringRes val stringRes: Int, @StringRes val sanskrit: Int, val type: KaranaType) {
    KIMSTUGHNA(R.string.karana_kimstughna, R.string.karana_kimstughna_sanskrit, KaranaType.FIXED),
    BAVA(R.string.karana_bava, R.string.karana_bava_sanskrit, KaranaType.MOVABLE),
    BALAVA(R.string.karana_balava, R.string.karana_balava_sanskrit, KaranaType.MOVABLE),
    KAULAVA(R.string.karana_kaulava, R.string.karana_kaulava_sanskrit, KaranaType.MOVABLE),
    TAITILA(R.string.karana_taitila, R.string.karana_taitila_sanskrit, KaranaType.MOVABLE),
    GARA(R.string.karana_gara, R.string.karana_gara_sanskrit, KaranaType.MOVABLE),
    VANIJA(R.string.karana_vanija, R.string.karana_vanija_sanskrit, KaranaType.MOVABLE),
    VISHTI(R.string.karana_vishti, R.string.karana_vishti_sanskrit, KaranaType.MOVABLE),
    SHAKUNI(R.string.karana_shakuni, R.string.karana_shakuni_sanskrit, KaranaType.FIXED),
    CHATUSHPADA(R.string.karana_chatushpada, R.string.karana_chatushpada_sanskrit, KaranaType.FIXED),
    NAGA(R.string.karana_naga, R.string.karana_naga_sanskrit, KaranaType.FIXED);

    val nature: Int
        get() = type.stringRes
}

enum class KaranaType(@StringRes val stringRes: Int) {
    FIXED(R.string.karana_type_fixed),
    MOVABLE(R.string.karana_type_movable)
}

enum class Vara(val number: Int, @StringRes val stringRes: Int, @StringRes val sanskrit: Int, val lord: Planet) {
    SUNDAY(0, R.string.vara_sunday, R.string.vara_sunday_sanskrit, Planet.SUN),
    MONDAY(1, R.string.vara_monday, R.string.vara_monday_sanskrit, Planet.MOON),
    TUESDAY(2, R.string.vara_tuesday, R.string.vara_tuesday_sanskrit, Planet.MARS),
    WEDNESDAY(3, R.string.vara_wednesday, R.string.vara_wednesday_sanskrit, Planet.MERCURY),
    THURSDAY(4, R.string.vara_thursday, R.string.vara_thursday_sanskrit, Planet.JUPITER),
    FRIDAY(5, R.string.vara_friday, R.string.vara_friday_sanskrit, Planet.VENUS),
    SATURDAY(6, R.string.vara_saturday, R.string.vara_saturday_sanskrit, Planet.SATURN)
}

enum class Paksha(@StringRes val stringRes: Int, @StringRes val sanskrit: Int) {
    SHUKLA(R.string.paksha_shukla, R.string.paksha_shukla_sanskrit),
    KRISHNA(R.string.paksha_krishna, R.string.paksha_krishna_sanskrit)
}

data class TithiData(
    val tithi: Tithi,
    val number: Int,
    val progress: Double,
    val lord: Planet,
    val elongation: Double,
    val remainingDegrees: Double
) {
    val group: TithiGroup
        get() = tithi.group

    val isKrishnaPaksha: Boolean
        get() = number > 15
}

data class NakshatraData(
    val nakshatra: Nakshatra,
    val number: Int,
    val pada: Int,
    val progress: Double,
    val lord: Planet,
    val degreeInNakshatra: Double,
    val remainingDegrees: Double
)

data class YogaData(
    val yoga: Yoga,
    val number: Int,
    val progress: Double,
    val combinedLongitude: Double,
    val remainingDegrees: Double
) {
    val isAuspicious: Boolean
        get() = yoga.nature == YogaNature.AUSPICIOUS
}

data class KaranaData(
    val karana: Karana,
    val number: Int,
    val progress: Double,
    val remainingDegrees: Double
) {
    val isVishti: Boolean
        get() = karana == Karana.VISHTI
}

data class PanchangaData(
    val tithi: TithiData,
    val nakshatra: NakshatraData,
    val yoga: YogaData,
    val karana: KaranaData,
    val vara: Vara,
    val paksha: Paksha,
    val sunrise: String,
    val sunset: String,
    val moonPhase: Double,
    val sunLongitude: Double,
    val moonLongitude: Double,
    val ayanamsa: Double = 0.0
) {
    val isShuklaPaksha: Boolean
        get() = paksha == Paksha.SHUKLA

    val tithiInPaksha: Int
        get() = if (tithi.number <= 15) tithi.number else tithi.number - 15

    fun toFormattedString(context: Context): String {
        return buildString {
            appendLine("════════════════════════════════════════════════════")
            appendLine("                    ${context.getString(R.string.panchanga_title)}")
            appendLine("════════════════════════════════════════════════════")
            appendLine()
            appendLine(context.getString(R.string.tithi_title))
            appendLine("  ${context.getString(tithi.tithi.stringRes)} (${context.getString(tithi.tithi.sanskrit)})")
            appendLine("  ${context.getString(paksha.stringRes)} - ${tithiInPaksha}/15")
            appendLine("  ${context.getString(R.string.group_title)}: ${context.getString(tithi.group.stringRes)} (${context.getString(tithi.group.nature)})")
            appendLine("  ${context.getString(R.string.progress_title)}: ${formatProgress(tithi.progress)}")
            appendLine("  ${context.getString(R.string.lord_title)}: ${context.getString(tithi.lord.stringRes)}")
            appendLine()
            appendLine(context.getString(R.string.nakshatra_title))
            appendLine("  ${context.getString(nakshatra.nakshatra.stringRes)} - Pada ${nakshatra.pada}")
            appendLine("  ${context.getString(R.string.number_title)}: ${nakshatra.number}/27")
            appendLine("  ${context.getString(R.string.progress_title)}: ${formatProgress(nakshatra.progress)}")
            appendLine("  ${context.getString(R.string.lord_title)}: ${context.getString(nakshatra.lord.stringRes)}")
            appendLine()
            appendLine(context.getString(R.string.yoga_title))
            appendLine("  ${context.getString(yoga.yoga.stringRes)} (${context.getString(yoga.yoga.sanskrit)})")
            appendLine("  ${context.getString(R.string.nature_title)}: ${context.getString(yoga.yoga.nature.stringRes)}")
            appendLine("  ${context.getString(R.string.number_title)}: ${yoga.number}/27")
            appendLine("  ${context.getString(R.string.progress_title)}: ${formatProgress(yoga.progress)}")
            appendLine()
            appendLine(context.getString(R.string.karana_title))
            appendLine("  ${context.getString(karana.karana.stringRes)} (${context.getString(karana.karana.sanskrit)})")
            appendLine("  ${context.getString(R.string.type_title)}: ${context.getString(karana.karana.nature)}")
            appendLine("  ${context.getString(R.string.number_title)}: ${karana.number}/60")
            appendLine("  ${context.getString(R.string.progress_title)}: ${formatProgress(karana.progress)}")
            if (karana.isVishti) {
                appendLine("  ${context.getString(R.string.vishti_warning)}")
            }
            appendLine()
            appendLine("${context.getString(R.string.vara_title)}: ${context.getString(vara.stringRes)} (${context.getString(vara.sanskrit)})")
            appendLine("  ${context.getString(R.string.lord_title)}: ${context.getString(vara.lord.stringRes)}")
            appendLine()
            appendLine("────────────────────────────────────────────────────")
            appendLine("${context.getString(R.string.sunrise_title)}: $sunrise")
            appendLine("${context.getString(R.string.sunset_title)}: $sunset")
            appendLine("${context.getString(R.string.moon_illumination_title)}: ${formatProgress(moonPhase)}")
            appendLine()
            appendLine("────────────────────────────────────────────────────")
            appendLine(context.getString(R.string.sidereal_positions_title))
            appendLine("  ${context.getString(R.string.sun_title)}: ${formatDegrees(sunLongitude)}")
            appendLine("  ${context.getString(R.string.moon_title)}: ${formatDegrees(moonLongitude)}")
            appendLine("  ${context.getString(R.string.ayanamsa_lahiri_title)}: ${formatDegrees(ayanamsa)}")
            appendLine()
        }
    }

    private fun formatProgress(value: Double): String {
        return String.format(Locale.US, "%.2f%%", value)
    }

    private fun formatDegrees(value: Double): String {
        val degrees = value.toInt()
        val minutesTotal = (value - degrees) * 60.0
        val minutes = minutesTotal.toInt()
        val seconds = ((minutesTotal - minutes) * 60.0).toInt()
        return String.format(Locale.US, "%d° %d' %d\"", degrees, minutes, seconds)
    }
}

private val Yoga.yogaNature: YogaNature
    get() = when (this) {
        Yoga.VISHKUMBHA, Yoga.ATIGANDA, Yoga.SHULA, Yoga.GANDA,
        Yoga.VYAGHATA, Yoga.VYATIPATA, Yoga.PARIGHA, Yoga.VAIDHRITI -> YogaNature.INAUSPICIOUS
        Yoga.VAJRA -> YogaNature.MIXED
        else -> YogaNature.AUSPICIOUS
    }
