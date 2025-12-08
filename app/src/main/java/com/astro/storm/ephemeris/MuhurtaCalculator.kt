package com.astro.storm.ephemeris

import android.content.Context
import androidx.annotation.StringRes
import com.astro.storm.R
import com.astro.storm.data.model.Planet
import com.astro.storm.data.model.Nakshatra
import com.astro.storm.data.model.VedicChart
import com.astro.storm.data.model.ZodiacSign
import swisseph.DblObj
import swisseph.SweConst
import swisseph.SweDate
import swisseph.SwissEph
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.temporal.ChronoUnit
import kotlin.math.abs
import kotlin.math.floor

class MuhurtaCalculator(private val context: Context) {

    private val swissEph = SwissEph()
    private val ephemerisPath: String

    companion object {
        private const val AYANAMSA_LAHIRI = SweConst.SE_SIDM_LAHIRI
        private const val SEFLG_SIDEREAL = SweConst.SEFLG_SIDEREAL
        private const val SEFLG_SPEED = SweConst.SEFLG_SPEED

        private const val DEGREES_PER_TITHI = 12.0
        private const val DEGREES_PER_NAKSHATRA = 360.0 / 27.0
        private const val DEGREES_PER_YOGA = 360.0 / 27.0
        private const val DEGREES_PER_KARANA = 6.0

        private const val TOTAL_TITHIS = 30
        private const val TOTAL_YOGAS = 27
        private const val TOTAL_KARANAS = 60

        private const val DAY_MUHURTAS = 15
        private const val DAY_CHOGHADIYAS = 8
        private const val NIGHT_CHOGHADIYAS = 8
        private const val DAY_HORAS = 12
        private const val NIGHT_HORAS = 12

        private val CHALDEAN_ORDER = listOf(
            Planet.SATURN, Planet.JUPITER, Planet.MARS, Planet.SUN,
            Planet.VENUS, Planet.MERCURY, Planet.MOON
        )

        private val INAUSPICIOUS_YOGAS = setOf(1, 6, 9, 10, 13, 15, 17, 19, 27)

        private val RIKTA_TITHIS = setOf(4, 9, 14, 19, 24, 29)
        private val NANDA_TITHIS = setOf(1, 6, 11, 16, 21, 26)
        private val BHADRA_TITHIS = setOf(2, 7, 12, 17, 22, 27)
        private val JAYA_TITHIS = setOf(3, 8, 13, 18, 23, 28)
        private val PURNA_TITHIS = setOf(5, 10, 15, 20, 25, 30)
    }

    init {
        ephemerisPath = context.filesDir.absolutePath + "/ephe"
        swissEph.swe_set_ephe_path(ephemerisPath)
        swissEph.swe_set_sid_mode(AYANAMSA_LAHIRI, 0.0, 0.0)
    }

    enum class ActivityType(
        @StringRes val displayName: Int,
        @StringRes val description: Int,
        val icon: String,
        val favorableNakshatras: List<Nakshatra>,
        val favorableTithis: List<Int>,
        val favorableVaras: List<Vara>,
        val avoidNakshatras: List<Nakshatra>
    ) {
        MARRIAGE(
            R.string.activity_marriage, R.string.activity_marriage_desc, "ring",
            listOf(
                Nakshatra.ROHINI, Nakshatra.MRIGASHIRA, Nakshatra.MAGHA,
                Nakshatra.UTTARA_PHALGUNI, Nakshatra.HASTA, Nakshatra.SWATI,
                Nakshatra.ANURADHA, Nakshatra.MULA, Nakshatra.UTTARA_ASHADHA,
                Nakshatra.UTTARA_BHADRAPADA, Nakshatra.REVATI
            ),
            listOf(2, 3, 5, 7, 10, 11, 12, 13),
            listOf(Vara.MONDAY, Vara.WEDNESDAY, Vara.THURSDAY, Vara.FRIDAY),
            listOf(Nakshatra.BHARANI, Nakshatra.KRITTIKA, Nakshatra.ARDRA,
                   Nakshatra.ASHLESHA, Nakshatra.PURVA_PHALGUNI, Nakshatra.VISHAKHA,
                   Nakshatra.JYESHTHA, Nakshatra.PURVA_ASHADHA, Nakshatra.PURVA_BHADRAPADA)
        ),
        TRAVEL(
            R.string.activity_travel, R.string.activity_travel_desc, "flight",
            listOf(
                Nakshatra.ASHWINI, Nakshatra.MRIGASHIRA, Nakshatra.PUNARVASU,
                Nakshatra.PUSHYA, Nakshatra.HASTA, Nakshatra.ANURADHA,
                Nakshatra.SHRAVANA, Nakshatra.DHANISHTHA, Nakshatra.REVATI
            ),
            listOf(2, 3, 5, 7, 10, 11, 12, 13),
            listOf(Vara.MONDAY, Vara.WEDNESDAY, Vara.THURSDAY, Vara.FRIDAY),
            listOf(Nakshatra.ARDRA, Nakshatra.ASHLESHA, Nakshatra.JYESHTHA,
                   Nakshatra.MULA, Nakshatra.BHARANI)
        ),
        BUSINESS(
            R.string.activity_business, R.string.activity_business_desc, "business",
            listOf(
                Nakshatra.ROHINI, Nakshatra.PUSHYA, Nakshatra.HASTA,
                Nakshatra.CHITRA, Nakshatra.SWATI, Nakshatra.ANURADHA,
                Nakshatra.SHRAVANA, Nakshatra.DHANISHTHA, Nakshatra.REVATI,
                Nakshatra.UTTARA_PHALGUNI, Nakshatra.UTTARA_ASHADHA, Nakshatra.UTTARA_BHADRAPADA
            ),
            listOf(1, 2, 3, 5, 7, 10, 11, 13),
            listOf(Vara.WEDNESDAY, Vara.THURSDAY, Vara.FRIDAY),
            listOf(Nakshatra.BHARANI, Nakshatra.ASHLESHA, Nakshatra.MULA,
                   Nakshatra.JYESHTHA, Nakshatra.ARDRA)
        ),
        PROPERTY(
            R.string.activity_property, R.string.activity_property_desc, "home",
            listOf(
                Nakshatra.ROHINI, Nakshatra.MRIGASHIRA, Nakshatra.UTTARA_PHALGUNI,
                Nakshatra.HASTA, Nakshatra.CHITRA, Nakshatra.SWATI,
                Nakshatra.ANURADHA, Nakshatra.UTTARA_ASHADHA, Nakshatra.SHRAVANA,
                Nakshatra.DHANISHTHA, Nakshatra.SHATABHISHA,
                Nakshatra.UTTARA_BHADRAPADA, Nakshatra.REVATI
            ),
            listOf(2, 3, 5, 7, 10, 11, 12, 13),
            listOf(Vara.MONDAY, Vara.WEDNESDAY, Vara.THURSDAY, Vara.FRIDAY),
            listOf(Nakshatra.ARDRA, Nakshatra.ASHLESHA, Nakshatra.JYESHTHA,
                   Nakshatra.MULA, Nakshatra.BHARANI, Nakshatra.KRITTIKA)
        ),
        EDUCATION(
            R.string.activity_education, R.string.activity_education_desc, "school",
            listOf(
                Nakshatra.ASHWINI, Nakshatra.ROHINI, Nakshatra.MRIGASHIRA,
                Nakshatra.PUNARVASU, Nakshatra.PUSHYA, Nakshatra.HASTA,
                Nakshatra.CHITRA, Nakshatra.SWATI, Nakshatra.SHRAVANA,
                Nakshatra.DHANISHTHA, Nakshatra.SHATABHISHA, Nakshatra.REVATI
            ),
            listOf(2, 3, 5, 7, 10, 11, 12),
            listOf(Vara.WEDNESDAY, Vara.THURSDAY, Vara.FRIDAY, Vara.MONDAY),
            listOf(Nakshatra.KRITTIKA, Nakshatra.ARDRA, Nakshatra.ASHLESHA,
                   Nakshatra.BHARANI, Nakshatra.MULA)
        ),
        MEDICAL(
            R.string.activity_medical, R.string.activity_medical_desc, "medical",
            listOf(
                Nakshatra.ASHWINI, Nakshatra.ROHINI, Nakshatra.MRIGASHIRA,
                Nakshatra.PUNARVASU, Nakshatra.PUSHYA, Nakshatra.UTTARA_PHALGUNI,
                Nakshatra.HASTA, Nakshatra.CHITRA, Nakshatra.SHRAVANA,
                Nakshatra.DHANISHTHA, Nakshatra.REVATI
            ),
            listOf(2, 3, 5, 6, 7, 10, 11, 12),
            listOf(Vara.MONDAY, Vara.WEDNESDAY, Vara.THURSDAY, Vara.FRIDAY),
            listOf(Nakshatra.BHARANI, Nakshatra.KRITTIKA, Nakshatra.ARDRA,
                   Nakshatra.ASHLESHA, Nakshatra.JYESHTHA, Nakshatra.MULA)
        ),
        VEHICLE(
            R.string.activity_vehicle, R.string.activity_vehicle_desc, "car",
            listOf(
                Nakshatra.ASHWINI, Nakshatra.ROHINI, Nakshatra.PUSHYA,
                Nakshatra.HASTA, Nakshatra.SWATI, Nakshatra.ANURADHA,
                Nakshatra.SHRAVANA, Nakshatra.DHANISHTHA, Nakshatra.REVATI
            ),
            listOf(2, 3, 5, 7, 10, 11, 12, 13),
            listOf(Vara.WEDNESDAY, Vara.THURSDAY, Vara.FRIDAY),
            listOf(Nakshatra.BHARANI, Nakshatra.MULA, Nakshatra.VISHAKHA,
                   Nakshatra.ARDRA, Nakshatra.ASHLESHA)
        ),
        SPIRITUAL(
            R.string.activity_spiritual, R.string.activity_spiritual_desc, "temple",
            listOf(
                Nakshatra.ASHWINI, Nakshatra.PUNARVASU, Nakshatra.PUSHYA,
                Nakshatra.HASTA, Nakshatra.SWATI, Nakshatra.ANURADHA,
                Nakshatra.SHRAVANA, Nakshatra.UTTARA_BHADRAPADA, Nakshatra.REVATI,
                Nakshatra.MRIGASHIRA, Nakshatra.CHITRA
            ),
            listOf(2, 3, 5, 7, 10, 11, 12, 13, 15),
            listOf(Vara.MONDAY, Vara.THURSDAY, Vara.FRIDAY, Vara.SUNDAY),
            listOf(Nakshatra.KRITTIKA, Nakshatra.ARDRA, Nakshatra.ASHLESHA,
                   Nakshatra.BHARANI, Nakshatra.MULA, Nakshatra.JYESHTHA)
        ),
        GRIHA_PRAVESHA(
            R.string.activity_griha_pravesha, R.string.activity_griha_pravesha_desc, "home_work",
            listOf(
                Nakshatra.ROHINI, Nakshatra.MRIGASHIRA, Nakshatra.UTTARA_PHALGUNI,
                Nakshatra.HASTA, Nakshatra.CHITRA, Nakshatra.SWATI,
                Nakshatra.ANURADHA, Nakshatra.UTTARA_ASHADHA, Nakshatra.SHRAVANA,
                Nakshatra.DHANISHTHA, Nakshatra.UTTARA_BHADRAPADA, Nakshatra.REVATI
            ),
            listOf(2, 3, 5, 7, 10, 11, 12, 13),
            listOf(Vara.MONDAY, Vara.WEDNESDAY, Vara.THURSDAY, Vara.FRIDAY),
            listOf(Nakshatra.ARDRA, Nakshatra.ASHLESHA, Nakshatra.JYESHTHA,
                   Nakshatra.MULA, Nakshatra.BHARANI, Nakshatra.KRITTIKA,
                   Nakshatra.PURVA_PHALGUNI, Nakshatra.PURVA_ASHADHA,
                   Nakshatra.PURVA_BHADRAPADA)
        ),
        NAMING_CEREMONY(
            R.string.activity_naming_ceremony, R.string.activity_naming_ceremony_desc, "child_care",
            listOf(
                Nakshatra.ASHWINI, Nakshatra.ROHINI, Nakshatra.MRIGASHIRA,
                Nakshatra.PUNARVASU, Nakshatra.PUSHYA, Nakshatra.UTTARA_PHALGUNI,
                Nakshatra.HASTA, Nakshatra.CHITRA, Nakshatra.SWATI,
                Nakshatra.ANURADHA, Nakshatra.SHRAVANA, Nakshatra.DHANISHTHA,
                Nakshatra.SHATABHISHA, Nakshatra.UTTARA_BHADRAPADA, Nakshatra.REVATI
            ),
            listOf(2, 3, 5, 7, 10, 11, 12, 13),
            listOf(Vara.MONDAY, Vara.WEDNESDAY, Vara.THURSDAY, Vara.FRIDAY),
            listOf(Nakshatra.BHARANI, Nakshatra.KRITTIKA, Nakshatra.ARDRA,
                   Nakshatra.ASHLESHA, Nakshatra.MULA, Nakshatra.JYESHTHA)
        ),
        GENERAL(
            R.string.activity_general, R.string.activity_general_desc, "star",
            listOf(
                Nakshatra.ASHWINI, Nakshatra.ROHINI, Nakshatra.MRIGASHIRA,
                Nakshatra.PUNARVASU, Nakshatra.PUSHYA, Nakshatra.UTTARA_PHALGUNI,
                Nakshatra.HASTA, Nakshatra.CHITRA, Nakshatra.SWATI,
                Nakshatra.ANURADHA, Nakshatra.SHRAVANA, Nakshatra.DHANISHTHA,
                Nakshatra.UTTARA_BHADRAPADA, Nakshatra.REVATI
            ),
            listOf(2, 3, 5, 7, 10, 11, 12, 13),
            listOf(Vara.MONDAY, Vara.WEDNESDAY, Vara.THURSDAY, Vara.FRIDAY),
            listOf(Nakshatra.BHARANI, Nakshatra.KRITTIKA, Nakshatra.ARDRA,
                   Nakshatra.ASHLESHA, Nakshatra.MULA, Nakshatra.JYESHTHA)
        )
    }

    enum class Vara(val dayNumber: Int, @StringRes val displayName: Int, val lord: Planet) {
        SUNDAY(0, R.string.vara_sunday, Planet.SUN),
        MONDAY(1, R.string.vara_monday, Planet.MOON),
        TUESDAY(2, R.string.vara_tuesday, Planet.MARS),
        WEDNESDAY(3, R.string.vara_wednesday, Planet.MERCURY),
        THURSDAY(4, R.string.vara_thursday, Planet.JUPITER),
        FRIDAY(5, R.string.vara_friday, Planet.VENUS),
        SATURDAY(6, R.string.vara_saturday, Planet.SATURN)
    }

    enum class Choghadiya(
        @StringRes val displayName: Int,
        val nature: ChoghadiyaNature,
        val lord: Planet
    ) {
        UDVEG(R.string.choghadiya_udveg, ChoghadiyaNature.INAUSPICIOUS, Planet.SUN),
        CHAR(R.string.choghadiya_char, ChoghadiyaNature.GOOD, Planet.VENUS),
        LABH(R.string.choghadiya_labh, ChoghadiyaNature.VERY_GOOD, Planet.MERCURY),
        AMRIT(R.string.choghadiya_amrit, ChoghadiyaNature.EXCELLENT, Planet.MOON),
        KAAL(R.string.choghadiya_kaal, ChoghadiyaNature.INAUSPICIOUS, Planet.SATURN),
        SHUBH(R.string.choghadiya_shubh, ChoghadiyaNature.VERY_GOOD, Planet.JUPITER),
        ROG(R.string.choghadiya_rog, ChoghadiyaNature.INAUSPICIOUS, Planet.MARS)
    }

    enum class ChoghadiyaNature(@StringRes val displayName: Int, val score: Int) {
        EXCELLENT(R.string.choghadiya_nature_excellent, 4),
        VERY_GOOD(R.string.choghadiya_nature_very_good, 3),
        GOOD(R.string.choghadiya_nature_good, 2),
        NEUTRAL(R.string.choghadiya_nature_neutral, 1),
        INAUSPICIOUS(R.string.choghadiya_nature_inauspicious, 0)
    }

    enum class NakshatraNature(@StringRes val displayName: Int) {
        DHRUVA(R.string.nakshatra_nature_dhruva),
        CHARA(R.string.nakshatra_nature_chara),
        TIKSHNA(R.string.nakshatra_nature_tikshna),
        UGRA(R.string.nakshatra_nature_ugra),
        MRIDU(R.string.nakshatra_nature_mridu),
        KSHIPRA(R.string.nakshatra_nature_kshipra),
        MISHRA(R.string.nakshatra_nature_mishra)
    }

    data class Hora(
        val lord: Planet,
        val horaNumber: Int,
        val startTime: LocalTime,
        val endTime: LocalTime,
        val isDay: Boolean,
        val nature: HoraNature
    )

    enum class HoraNature(@StringRes val displayName: Int) {
        BENEFIC(R.string.hora_nature_benefic),
        MALEFIC(R.string.hora_nature_malefic),
        NEUTRAL(R.string.hora_nature_neutral)
    }

    data class InauspiciousPeriods(
        val rahukala: TimePeriod,
        val yamaghanta: TimePeriod,
        val gulikaKala: TimePeriod,
        val durmuhurtas: List<TimePeriod>
    )

    data class TimePeriod(
        val startTime: LocalTime,
        val endTime: LocalTime,
        val name: String = ""
    ) {
        fun contains(time: LocalTime): Boolean {
            return if (startTime <= endTime) {
                time >= startTime && time < endTime
            } else {
                time >= startTime || time < endTime
            }
        }
    }

    data class AbhijitMuhurta(
        val startTime: LocalTime,
        val endTime: LocalTime,
        val isActive: Boolean
    )

    data class MuhurtaDetails(
        val dateTime: LocalDateTime,
        val vara: Vara,
        val tithi: TithiInfo,
        val nakshatra: NakshatraInfo,
        val yoga: YogaInfo,
        val karana: KaranaInfo,
        val choghadiya: ChoghadiyaInfo,
        val hora: Hora,
        val inauspiciousPeriods: InauspiciousPeriods,
        val abhijitMuhurta: AbhijitMuhurta,
        val sunrise: LocalTime,
        val sunset: LocalTime,
        val overallScore: Int,
        val suitableActivities: List<ActivityType>,
        val avoidActivities: List<ActivityType>,
        val recommendations: List<String>,
        val specialYogas: List<SpecialYoga>
    ) {
        val isAuspicious: Boolean get() = overallScore >= 60
        val isExcellent: Boolean get() = overallScore >= 80
    }

    data class TithiInfo(
        val number: Int,
        val displayNumber: Int,
        val name: String,
        val paksha: String,
        val lord: Planet,
        val nature: TithiNature,
        val isAuspicious: Boolean
    )

    enum class TithiNature(@StringRes val displayName: Int) {
        NANDA(R.string.tithi_nature_nanda),
        BHADRA(R.string.tithi_nature_bhadra),
        JAYA(R.string.tithi_nature_jaya),
        RIKTA(R.string.tithi_nature_rikta),
        PURNA(R.string.tithi_nature_purna)
    }

    data class NakshatraInfo(
        val nakshatra: Nakshatra,
        val pada: Int,
        val lord: Planet,
        val nature: NakshatraNature,
        val gana: NakshatraGana,
        val element: NakshatraElement
    )

    enum class NakshatraGana(@StringRes val displayName: Int) {
        DEVA(R.string.nakshatra_gana_deva),
        MANUSHYA(R.string.nakshatra_gana_manushya),
        RAKSHASA(R.string.nakshatra_gana_rakshasa)
    }

    enum class NakshatraElement(@StringRes val displayName: Int) {
        VAYU(R.string.nakshatra_element_vayu),
        AGNI(R.string.nakshatra_element_agni),
        PRITHVI(R.string.nakshatra_element_prithvi),
        JALA(R.string.nakshatra_element_jala),
        AKASHA(R.string.nakshatra_element_akasha)
    }

    data class YogaInfo(
        val number: Int,
        val name: String,
        val nature: String,
        val isAuspicious: Boolean
    )

    data class KaranaInfo(
        val number: Int,
        val name: String,
        val type: KaranaType,
        val isAuspicious: Boolean
    )

    enum class KaranaType(@StringRes val displayName: Int) {
        STHIRA(R.string.karana_type_sthira),
        CHARA(R.string.karana_type_chara)
    }

    data class ChoghadiyaInfo(
        val choghadiya: Choghadiya,
        val startTime: LocalTime,
        val endTime: LocalTime,
        val isDay: Boolean
    )

    data class SpecialYoga(
        val name: String,
        val description: String,
        val isAuspicious: Boolean
    )

    data class MuhurtaSearchResult(
        val dateTime: LocalDateTime,
        val score: Int,
        val vara: Vara,
        val nakshatra: Nakshatra,
        val tithi: String,
        val choghadiya: Choghadiya,
        val reasons: List<String>,
        val warnings: List<String>,
        val specialYogas: List<SpecialYoga>
    )

    private val TITHI_NAMES by lazy { context.resources.getStringArray(R.array.tithi_names_array) }
    private val YOGA_NAMES by lazy { context.resources.getStringArray(R.array.yoga_names_array) }
    private val MOVABLE_KARANAS by lazy { context.resources.getStringArray(R.array.movable_karanas_array) }

}
