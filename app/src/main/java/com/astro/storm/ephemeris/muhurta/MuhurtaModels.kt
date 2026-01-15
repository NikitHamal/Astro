package com.astro.storm.ephemeris.muhurta

import com.astro.storm.core.common.Language
import com.astro.storm.core.common.StringKeyAnalysis
import com.astro.storm.core.common.StringKeyMatch
import com.astro.storm.core.common.StringResources
import com.astro.storm.core.model.Nakshatra
import com.astro.storm.core.model.Planet
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime

enum class ActivityType(
    val displayNameKey: StringKeyMatch,
    val descriptionKey: StringKeyMatch,
    val icon: String,
    val favorableNakshatras: List<Nakshatra>,
    val favorableTithis: List<Int>,
    val favorableVaras: List<Vara>,
    val avoidNakshatras: List<Nakshatra>
) {
    MARRIAGE(
        StringKeyMatch.ACTIVITY_MARRIAGE_NAME, StringKeyMatch.ACTIVITY_MARRIAGE_DESC, "ring",
        listOf(Nakshatra.ROHINI, Nakshatra.MRIGASHIRA, Nakshatra.MAGHA, Nakshatra.UTTARA_PHALGUNI, Nakshatra.HASTA, Nakshatra.SWATI, Nakshatra.ANURADHA, Nakshatra.MULA, Nakshatra.UTTARA_ASHADHA, Nakshatra.UTTARA_BHADRAPADA, Nakshatra.REVATI),
        listOf(2, 3, 5, 7, 10, 11, 12, 13), listOf(Vara.MONDAY, Vara.WEDNESDAY, Vara.THURSDAY, Vara.FRIDAY),
        listOf(Nakshatra.BHARANI, Nakshatra.KRITTIKA, Nakshatra.ARDRA, Nakshatra.ASHLESHA, Nakshatra.PURVA_PHALGUNI, Nakshatra.VISHAKHA, Nakshatra.JYESHTHA, Nakshatra.PURVA_ASHADHA, Nakshatra.PURVA_BHADRAPADA)
    ),
    TRAVEL(
        StringKeyMatch.ACTIVITY_TRAVEL_NAME, StringKeyMatch.ACTIVITY_TRAVEL_DESC, "flight",
        listOf(Nakshatra.ASHWINI, Nakshatra.MRIGASHIRA, Nakshatra.PUNARVASU, Nakshatra.PUSHYA, Nakshatra.HASTA, Nakshatra.ANURADHA, Nakshatra.SHRAVANA, Nakshatra.DHANISHTHA, Nakshatra.REVATI),
        listOf(2, 3, 5, 7, 10, 11, 12, 13), listOf(Vara.MONDAY, Vara.WEDNESDAY, Vara.THURSDAY, Vara.FRIDAY),
        listOf(Nakshatra.ARDRA, Nakshatra.ASHLESHA, Nakshatra.JYESHTHA, Nakshatra.MULA, Nakshatra.BHARANI)
    ),
    BUSINESS(
        StringKeyMatch.ACTIVITY_BUSINESS_NAME, StringKeyMatch.ACTIVITY_BUSINESS_DESC, "business",
        listOf(Nakshatra.ROHINI, Nakshatra.PUSHYA, Nakshatra.HASTA, Nakshatra.CHITRA, Nakshatra.SWATI, Nakshatra.ANURADHA, Nakshatra.SHRAVANA, Nakshatra.DHANISHTHA, Nakshatra.REVATI, Nakshatra.UTTARA_PHALGUNI, Nakshatra.UTTARA_ASHADHA, Nakshatra.UTTARA_BHADRAPADA),
        listOf(1, 2, 3, 5, 7, 10, 11, 13), listOf(Vara.WEDNESDAY, Vara.THURSDAY, Vara.FRIDAY),
        listOf(Nakshatra.BHARANI, Nakshatra.ASHLESHA, Nakshatra.MULA, Nakshatra.JYESHTHA, Nakshatra.ARDRA)
    ),
    PROPERTY(
        StringKeyMatch.ACTIVITY_PROPERTY_NAME, StringKeyMatch.ACTIVITY_PROPERTY_DESC, "home",
        listOf(Nakshatra.ROHINI, Nakshatra.MRIGASHIRA, Nakshatra.UTTARA_PHALGUNI, Nakshatra.HASTA, Nakshatra.CHITRA, Nakshatra.SWATI, Nakshatra.ANURADHA, Nakshatra.UTTARA_ASHADHA, Nakshatra.SHRAVANA, Nakshatra.DHANISHTHA, Nakshatra.SHATABHISHA, Nakshatra.UTTARA_BHADRAPADA, Nakshatra.REVATI),
        listOf(2, 3, 5, 7, 10, 11, 12, 13), listOf(Vara.MONDAY, Vara.WEDNESDAY, Vara.THURSDAY, Vara.FRIDAY),
        listOf(Nakshatra.ARDRA, Nakshatra.ASHLESHA, Nakshatra.JYESHTHA, Nakshatra.MULA, Nakshatra.BHARANI, Nakshatra.KRITTIKA)
    ),
    EDUCATION(
        StringKeyMatch.ACTIVITY_EDUCATION_NAME, StringKeyMatch.ACTIVITY_EDUCATION_DESC, "school",
        listOf(Nakshatra.ASHWINI, Nakshatra.ROHINI, Nakshatra.MRIGASHIRA, Nakshatra.PUNARVASU, Nakshatra.PUSHYA, Nakshatra.HASTA, Nakshatra.CHITRA, Nakshatra.SWATI, Nakshatra.SHRAVANA, Nakshatra.DHANISHTHA, Nakshatra.SHATABHISHA, Nakshatra.REVATI),
        listOf(2, 3, 5, 7, 10, 11, 12), listOf(Vara.WEDNESDAY, Vara.THURSDAY, Vara.FRIDAY, Vara.MONDAY),
        listOf(Nakshatra.KRITTIKA, Nakshatra.ARDRA, Nakshatra.ASHLESHA, Nakshatra.BHARANI, Nakshatra.MULA)
    ),
    MEDICAL(
        StringKeyMatch.ACTIVITY_MEDICAL_NAME, StringKeyMatch.ACTIVITY_MEDICAL_DESC, "medical",
        listOf(Nakshatra.ASHWINI, Nakshatra.ROHINI, Nakshatra.MRIGASHIRA, Nakshatra.PUNARVASU, Nakshatra.PUSHYA, Nakshatra.UTTARA_PHALGUNI, Nakshatra.HASTA, Nakshatra.CHITRA, Nakshatra.SHRAVANA, Nakshatra.DHANISHTHA, Nakshatra.REVATI),
        listOf(2, 3, 5, 6, 7, 10, 11, 12), listOf(Vara.MONDAY, Vara.WEDNESDAY, Vara.THURSDAY, Vara.FRIDAY),
        listOf(Nakshatra.BHARANI, Nakshatra.KRITTIKA, Nakshatra.ARDRA, Nakshatra.ASHLESHA, Nakshatra.JYESHTHA, Nakshatra.MULA)
    ),
    VEHICLE(
        StringKeyMatch.ACTIVITY_VEHICLE_NAME, StringKeyMatch.ACTIVITY_VEHICLE_DESC, "car",
        listOf(Nakshatra.ASHWINI, Nakshatra.ROHINI, Nakshatra.PUSHYA, Nakshatra.HASTA, Nakshatra.SWATI, Nakshatra.ANURADHA, Nakshatra.SHRAVANA, Nakshatra.DHANISHTHA, Nakshatra.REVATI),
        listOf(2, 3, 5, 7, 10, 11, 12, 13), listOf(Vara.WEDNESDAY, Vara.THURSDAY, Vara.FRIDAY),
        listOf(Nakshatra.BHARANI, Nakshatra.MULA, Nakshatra.VISHAKHA, Nakshatra.ARDRA, Nakshatra.ASHLESHA)
    ),
    SPIRITUAL(
        StringKeyMatch.ACTIVITY_SPIRITUAL_NAME, StringKeyMatch.ACTIVITY_SPIRITUAL_DESC, "temple",
        listOf(Nakshatra.ASHWINI, Nakshatra.PUNARVASU, Nakshatra.PUSHYA, Nakshatra.HASTA, Nakshatra.SWATI, Nakshatra.ANURADHA, Nakshatra.SHRAVANA, Nakshatra.UTTARA_BHADRAPADA, Nakshatra.REVATI, Nakshatra.MRIGASHIRA, Nakshatra.CHITRA),
        listOf(2, 3, 5, 7, 10, 11, 12, 13, 15), listOf(Vara.MONDAY, Vara.THURSDAY, Vara.FRIDAY, Vara.SUNDAY),
        listOf(Nakshatra.KRITTIKA, Nakshatra.ARDRA, Nakshatra.ASHLESHA, Nakshatra.BHARANI, Nakshatra.MULA, Nakshatra.JYESHTHA)
    ),
    GRIHA_PRAVESHA(
        StringKeyMatch.ACTIVITY_GRIHA_PRAVESHA_NAME, StringKeyMatch.ACTIVITY_GRIHA_PRAVESHA_DESC, "home_work",
        listOf(Nakshatra.ROHINI, Nakshatra.MRIGASHIRA, Nakshatra.UTTARA_PHALGUNI, Nakshatra.HASTA, Nakshatra.CHITRA, Nakshatra.SWATI, Nakshatra.ANURADHA, Nakshatra.UTTARA_ASHADHA, Nakshatra.SHRAVANA, Nakshatra.DHANISHTHA, Nakshatra.UTTARA_BHADRAPADA, Nakshatra.REVATI),
        listOf(2, 3, 5, 7, 10, 11, 12, 13), listOf(Vara.MONDAY, Vara.WEDNESDAY, Vara.THURSDAY, Vara.FRIDAY),
        listOf(Nakshatra.ARDRA, Nakshatra.ASHLESHA, Nakshatra.JYESHTHA, Nakshatra.MULA, Nakshatra.BHARANI, Nakshatra.KRITTIKA, Nakshatra.PURVA_PHALGUNI, Nakshatra.PURVA_ASHADHA, Nakshatra.PURVA_BHADRAPADA)
    ),
    NAMING_CEREMONY(
        StringKeyMatch.ACTIVITY_NAMING_NAME, StringKeyMatch.ACTIVITY_NAMING_DESC, "child_care",
        listOf(Nakshatra.ASHWINI, Nakshatra.ROHINI, Nakshatra.MRIGASHIRA, Nakshatra.PUNARVASU, Nakshatra.PUSHYA, Nakshatra.UTTARA_PHALGUNI, Nakshatra.HASTA, Nakshatra.CHITRA, Nakshatra.SWATI, Nakshatra.ANURADHA, Nakshatra.SHRAVANA, Nakshatra.DHANISHTHA, Nakshatra.SHATABHISHA, Nakshatra.UTTARA_BHADRAPADA, Nakshatra.REVATI),
        listOf(2, 3, 5, 7, 10, 11, 12, 13), listOf(Vara.MONDAY, Vara.WEDNESDAY, Vara.THURSDAY, Vara.FRIDAY),
        listOf(Nakshatra.BHARANI, Nakshatra.KRITTIKA, Nakshatra.ARDRA, Nakshatra.ASHLESHA, Nakshatra.MULA, Nakshatra.JYESHTHA)
    ),
    GENERAL(
        StringKeyMatch.ACTIVITY_GENERAL_NAME, StringKeyMatch.ACTIVITY_GENERAL_DESC, "star",
        listOf(Nakshatra.ASHWINI, Nakshatra.ROHINI, Nakshatra.MRIGASHIRA, Nakshatra.PUNARVASU, Nakshatra.PUSHYA, Nakshatra.UTTARA_PHALGUNI, Nakshatra.HASTA, Nakshatra.CHITRA, Nakshatra.SWATI, Nakshatra.ANURADHA, Nakshatra.SHRAVANA, Nakshatra.DHANISHTHA, Nakshatra.UTTARA_BHADRAPADA, Nakshatra.REVATI),
        listOf(2, 3, 5, 7, 10, 11, 12, 13), listOf(Vara.MONDAY, Vara.WEDNESDAY, Vara.THURSDAY, Vara.FRIDAY),
        listOf(Nakshatra.BHARANI, Nakshatra.KRITTIKA, Nakshatra.ARDRA, Nakshatra.ASHLESHA, Nakshatra.MULA, Nakshatra.JYESHTHA)
    );

    fun getLocalizedName(language: Language): String = StringResources.get(displayNameKey, language)
    fun getLocalizedDescription(language: Language): String = StringResources.get(descriptionKey, language)
}

enum class Vara(val dayNumber: Int, val displayNameKey: StringKeyMatch, val lord: Planet) {
    SUNDAY(0, StringKeyMatch.VARA_SUNDAY, Planet.SUN),
    MONDAY(1, StringKeyMatch.VARA_MONDAY, Planet.MOON),
    TUESDAY(2, StringKeyMatch.VARA_TUESDAY, Planet.MARS),
    WEDNESDAY(3, StringKeyMatch.VARA_WEDNESDAY, Planet.MERCURY),
    THURSDAY(4, StringKeyMatch.VARA_THURSDAY, Planet.JUPITER),
    FRIDAY(5, StringKeyMatch.VARA_FRIDAY, Planet.VENUS),
    SATURDAY(6, StringKeyMatch.VARA_SATURDAY, Planet.SATURN);

    fun getLocalizedName(language: Language): String = StringResources.get(displayNameKey, language)
}

enum class Choghadiya(val displayNameKey: StringKeyMatch, val nature: ChoghadiyaNature, val lord: Planet) {
    UDVEG(StringKeyMatch.CHOGHADIYA_UDVEG, ChoghadiyaNature.INAUSPICIOUS, Planet.SUN),
    CHAR(StringKeyMatch.CHOGHADIYA_CHAR, ChoghadiyaNature.GOOD, Planet.VENUS),
    LABH(StringKeyMatch.CHOGHADIYA_LABH, ChoghadiyaNature.VERY_GOOD, Planet.MERCURY),
    AMRIT(StringKeyMatch.CHOGHADIYA_AMRIT, ChoghadiyaNature.EXCELLENT, Planet.MOON),
    KAAL(StringKeyMatch.CHOGHADIYA_KAAL, ChoghadiyaNature.INAUSPICIOUS, Planet.SATURN),
    SHUBH(StringKeyMatch.CHOGHADIYA_SHUBH, ChoghadiyaNature.VERY_GOOD, Planet.JUPITER),
    ROG(StringKeyMatch.CHOGHADIYA_ROG, ChoghadiyaNature.INAUSPICIOUS, Planet.MARS);

    fun getLocalizedName(language: Language): String = StringResources.get(displayNameKey, language)
}

enum class ChoghadiyaNature(val displayName: String, val score: Int) {
    EXCELLENT("Excellent", 4), VERY_GOOD("Very Good", 3), GOOD("Good", 2), NEUTRAL("Neutral", 1), INAUSPICIOUS("Inauspicious", 0);
    fun getLocalizedName(language: Language): String {
        val key = when (this) { EXCELLENT -> StringKeyAnalysis.AUSPICIOUSNESS_HIGHLY_AUSPICIOUS; VERY_GOOD -> StringKeyAnalysis.AUSPICIOUSNESS_AUSPICIOUS; GOOD, NEUTRAL -> StringKeyAnalysis.AUSPICIOUSNESS_NEUTRAL; INAUSPICIOUS -> StringKeyAnalysis.CHOGHADIYA_NATURE_INAUSPICIOUS }
        return StringResources.get(key, language)
    }
}

enum class NakshatraNature(val displayName: String) { DHRUVA("Fixed/Dhruva"), CHARA("Movable/Chara"), TIKSHNA("Sharp/Tikshna"), UGRA("Fierce/Ugra"), MRIDU("Soft/Mridu"), KSHIPRA("Swift/Kshipra"), MISHRA("Mixed/Mishra") }

data class Hora(val lord: Planet, val horaNumber: Int, val startTime: LocalTime, val endTime: LocalTime, val isDay: Boolean, val nature: HoraNature)

enum class HoraNature(val displayName: String) {
    BENEFIC("Benefic"), MALEFIC("Malefic"), NEUTRAL("Neutral");
    fun getLocalizedName(language: Language): String {
        val key = when (this) { BENEFIC -> StringKeyAnalysis.HORA_NATURE_BENEFIC; MALEFIC -> StringKeyAnalysis.HORA_NATURE_MALEFIC; NEUTRAL -> StringKeyAnalysis.HORA_NATURE_NEUTRAL }
        return StringResources.get(key, language)
    }
}

data class TimePeriod(val startTime: LocalTime, val endTime: LocalTime, val name: String = "") {
    fun contains(time: LocalTime): Boolean = if (startTime <= endTime) time >= startTime && time < endTime else time >= startTime || time < endTime
}

data class InauspiciousPeriods(val rahukala: TimePeriod, val yamaghanta: TimePeriod, val gulikaKala: TimePeriod, val durmuhurtas: List<TimePeriod>)

data class AbhijitMuhurta(val startTime: LocalTime, val endTime: LocalTime, val isActive: Boolean)

data class TithiInfo(val number: Int, val displayNumber: Int, val name: String, val paksha: String, val lord: Planet, val nature: TithiNature, val isAuspicious: Boolean)

enum class TithiNature(val displayName: String) {
    NANDA("Nanda - Joy"), BHADRA("Bhadra - Auspicious"), JAYA("Jaya - Victory"), RIKTA("Rikta - Empty"), PURNA("Purna - Full");
    fun getLocalizedName(language: Language): String {
        val key = when (this) { NANDA -> StringKeyAnalysis.TITHI_NATURE_NANDA; BHADRA -> StringKeyAnalysis.TITHI_NATURE_BHADRA; JAYA -> StringKeyAnalysis.TITHI_NATURE_JAYA; RIKTA -> StringKeyAnalysis.TITHI_NATURE_RIKTA; PURNA -> StringKeyAnalysis.TITHI_NATURE_PURNA }
        return StringResources.get(key, language)
    }
}

data class NakshatraInfo(val nakshatra: Nakshatra, val pada: Int, val lord: Planet, val nature: NakshatraNature, val gana: NakshatraGana, val element: NakshatraElement)

enum class NakshatraGana(val displayName: String) {
    DEVA("Divine"), MANUSHYA("Human"), RAKSHASA("Demonic");
    fun getLocalizedName(language: Language): String {
        val key = when (this) { DEVA -> StringKeyAnalysis.NAKSHATRA_GANA_DEVA; MANUSHYA -> StringKeyAnalysis.NAKSHATRA_GANA_MANUSHYA; RAKSHASA -> StringKeyAnalysis.NAKSHATRA_GANA_RAKSHASA }
        return StringResources.get(key, language)
    }
}

enum class NakshatraElement(val displayName: String) {
    VAYU("Air"), AGNI("Fire"), PRITHVI("Earth"), JALA("Water"), AKASHA("Ether");
    fun getLocalizedName(language: Language): String {
        val key = when (this) { VAYU -> StringKeyAnalysis.NAKSHATRA_ELEMENT_AIR; AGNI -> StringKeyAnalysis.NAKSHATRA_ELEMENT_FIRE; PRITHVI -> StringKeyAnalysis.NAKSHATRA_ELEMENT_EARTH; JALA -> StringKeyAnalysis.NAKSHATRA_ELEMENT_WATER; AKASHA -> StringKeyAnalysis.NAKSHATRA_ELEMENT_ETHER }
        return StringResources.get(key, language)
    }
}

data class YogaInfo(val number: Int, val name: String, val nature: String, val isAuspicious: Boolean)

data class KaranaInfo(val number: Int, val name: String, val type: KaranaType, val isAuspicious: Boolean)

enum class KaranaType(val displayName: String) {
    STHIRA("Fixed"), CHARA("Movable");
    fun getLocalizedName(language: Language): String {
        val key = when (this) { STHIRA -> StringKeyAnalysis.KARANA_FIXED; CHARA -> StringKeyAnalysis.KARANA_MOVABLE }
        return StringResources.get(key, language)
    }
}

data class ChoghadiyaInfo(val choghadiya: Choghadiya, val startTime: LocalTime, val endTime: LocalTime, val isDay: Boolean)

data class SpecialYoga(val name: String, val description: String, val isAuspicious: Boolean)

data class MuhurtaDetails(
    val dateTime: LocalDateTime, val vara: Vara, val tithi: TithiInfo, val nakshatra: NakshatraInfo,
    val yoga: YogaInfo, val karana: KaranaInfo, val choghadiya: ChoghadiyaInfo, val hora: Hora,
    val inauspiciousPeriods: InauspiciousPeriods, val abhijitMuhurta: AbhijitMuhurta, val sunrise: LocalTime,
    val sunset: LocalTime, val overallScore: Int, val suitableActivities: List<ActivityType>,
    val avoidActivities: List<ActivityType>, val recommendations: List<String>, val specialYogas: List<SpecialYoga>
) {
    val isAuspicious: Boolean get() = overallScore >= 60
    val isExcellent: Boolean get() = overallScore >= 80
}

data class MuhurtaSearchResult(
    val dateTime: LocalDateTime, val score: Int, val vara: Vara, val nakshatra: Nakshatra,
    val tithi: String, val choghadiya: Choghadiya, val reasons: List<String>, val warnings: List<String>,
    val specialYogas: List<SpecialYoga>
)

data class PanchangaData(
    val date: LocalDate, val vara: Vara, val tithi: TithiInfo, val tithiEndTime: LocalDateTime,
    val nakshatra: NakshatraInfo, val nakshatraEndTime: LocalDateTime, val yoga: YogaInfo,
    val yogaEndTime: LocalDateTime, val karana: KaranaInfo, val karanaEndTime: LocalDateTime,
    val sunrise: LocalTime, val sunset: LocalTime, val rahukala: TimePeriod, val yamaghanta: TimePeriod,
    val gulikaKala: TimePeriod, val abhijitMuhurta: AbhijitMuhurta, val specialYogas: List<SpecialYoga>
)
