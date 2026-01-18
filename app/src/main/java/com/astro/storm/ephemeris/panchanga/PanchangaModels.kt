package com.astro.storm.ephemeris.panchanga

import com.astro.storm.core.common.Language
import com.astro.storm.core.common.StringKeyAnalysis
import com.astro.storm.data.localization.stringResources
import com.astro.storm.core.model.Nakshatra
import com.astro.storm.core.model.Planet
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.util.Locale

enum class Tithi(val number: Int, val displayName: String, val sanskrit: String, val group: TithiGroup) {
    PRATIPADA(1, "Pratipada", "प्रतिपदा", TithiGroup.NANDA),
    DWITIYA(2, "Dwitiya", "द्वितीया", TithiGroup.BHADRA),
    TRITIYA(3, "Tritiya", "तृतीया", TithiGroup.JAYA),
    CHATURTHI(4, "Chaturthi", "चतुर्थी", TithiGroup.RIKTA),
    PANCHAMI(5, "Panchami", "पञ्चमी", TithiGroup.PURNA),
    SHASHTHI(6, "Shashthi", "षष्ठी", TithiGroup.NANDA),
    SAPTAMI(7, "Saptami", "सप्तमी", TithiGroup.BHADRA),
    ASHTAMI(8, "Ashtami", "अष्टमी", TithiGroup.JAYA),
    NAVAMI(9, "Navami", "नवमी", TithiGroup.RIKTA),
    DASHAMI(10, "Dashami", "दशमी", TithiGroup.PURNA),
    EKADASHI(11, "Ekadashi", "एकादशी", TithiGroup.NANDA),
    DWADASHI(12, "Dwadashi", "द्वादशी", TithiGroup.BHADRA),
    TRAYODASHI(13, "Trayodashi", "त्रयोदशी", TithiGroup.JAYA),
    CHATURDASHI(14, "Chaturdashi", "चतुर्दशी", TithiGroup.RIKTA),
    PURNIMA(15, "Purnima", "पूर्णिमा", TithiGroup.PURNA),
    PRATIPADA_K(16, "Pratipada", "प्रतिपदा", TithiGroup.NANDA),
    DWITIYA_K(17, "Dwitiya", "द्वितीया", TithiGroup.BHADRA),
    TRITIYA_K(18, "Tritiya", "तृतीया", TithiGroup.JAYA),
    CHATURTHI_K(19, "Chaturthi", "चतुर्थी", TithiGroup.RIKTA),
    PANCHAMI_K(20, "Panchami", "पञ्चमी", TithiGroup.PURNA),
    SHASHTHI_K(21, "Shashthi", "षष्ठी", TithiGroup.NANDA),
    SAPTAMI_K(22, "Saptami", "सप्तमी", TithiGroup.BHADRA),
    ASHTAMI_K(23, "Ashtami", "अष्टमी", TithiGroup.JAYA),
    NAVAMI_K(24, "Navami", "नवमी", TithiGroup.RIKTA),
    DASHAMI_K(25, "Dashami", "दशमी", TithiGroup.PURNA),
    EKADASHI_K(26, "Ekadashi", "एकादशी", TithiGroup.NANDA),
    DWADASHI_K(27, "Dwadashi", "द्वादशी", TithiGroup.BHADRA),
    TRAYODASHI_K(28, "Trayodashi", "त्रयोदशी", TithiGroup.JAYA),
    CHATURDASHI_K(29, "Chaturdashi", "चतुर्दशी", TithiGroup.RIKTA),
    AMAVASYA(30, "Amavasya", "अमावस्या", TithiGroup.PURNA)
}

enum class TithiGroup(val displayName: String, val nature: String) {
    NANDA("Nanda", "Joyful"),
    BHADRA("Bhadra", "Auspicious"),
    JAYA("Jaya", "Victorious"),
    RIKTA("Rikta", "Empty"),
    PURNA("Purna", "Complete");

    fun getLocalizedName(language: Language): String {
        val key = when (this) {
            NANDA -> StringKeyAnalysis.TITHI_GROUP_NANDA
            BHADRA -> StringKeyAnalysis.TITHI_GROUP_BHADRA
            JAYA -> StringKeyAnalysis.TITHI_GROUP_JAYA
            RIKTA -> StringKeyAnalysis.TITHI_GROUP_RIKTA
            PURNA -> StringKeyAnalysis.TITHI_GROUP_PURNA
        }
        return StringResources.get(key, language)
    }

    fun getLocalizedNature(language: Language): String {
        val key = when (this) {
            NANDA -> StringKeyAnalysis.TITHI_GROUP_NANDA_NATURE
            BHADRA -> StringKeyAnalysis.TITHI_GROUP_BHADRA_NATURE
            JAYA -> StringKeyAnalysis.TITHI_GROUP_JAYA_NATURE
            RIKTA -> StringKeyAnalysis.TITHI_GROUP_RIKTA_NATURE
            PURNA -> StringKeyAnalysis.TITHI_GROUP_PURNA_NATURE
        }
        return StringResources.get(key, language)
    }
}

enum class Yoga(val number: Int, val displayName: String, val sanskrit: String, val nature: YogaNature) {
    VISHKUMBHA(1, "Vishkumbha", "विष्कुम्भ", YogaNature.INAUSPICIOUS),
    PRITI(2, "Priti", "प्रीति", YogaNature.AUSPICIOUS),
    AYUSHMAN(3, "Ayushman", "आयुष्मान्", YogaNature.AUSPICIOUS),
    SAUBHAGYA(4, "Saubhagya", "सौभाग्य", YogaNature.AUSPICIOUS),
    SHOBHANA(5, "Shobhana", "शोभन", YogaNature.AUSPICIOUS),
    ATIGANDA(6, "Atiganda", "अतिगण्ड", YogaNature.INAUSPICIOUS),
    SUKARMA(7, "Sukarma", "सुकर्म", YogaNature.AUSPICIOUS),
    DHRITI(8, "Dhriti", "धृति", YogaNature.AUSPICIOUS),
    SHULA(9, "Shula", "शूल", YogaNature.INAUSPICIOUS),
    GANDA(10, "Ganda", "गण्ड", YogaNature.INAUSPICIOUS),
    VRIDDHI(11, "Vriddhi", "वृद्धि", YogaNature.AUSPICIOUS),
    DHRUVA(12, "Dhruva", "ध्रुव", YogaNature.AUSPICIOUS),
    VYAGHATA(13, "Vyaghata", "व्याघात", YogaNature.INAUSPICIOUS),
    HARSHANA(14, "Harshana", "हर्षण", YogaNature.AUSPICIOUS),
    VAJRA(15, "Vajra", "वज्र", YogaNature.MIXED),
    SIDDHI(16, "Siddhi", "सिद्धि", YogaNature.AUSPICIOUS),
    VYATIPATA(17, "Vyatipata", "व्यतीपात", YogaNature.INAUSPICIOUS),
    VARIYAN(18, "Variyan", "वरीयान्", YogaNature.AUSPICIOUS),
    PARIGHA(19, "Parigha", "परिघ", YogaNature.INAUSPICIOUS),
    SHIVA(20, "Shiva", "शिव", YogaNature.AUSPICIOUS),
    SIDDHA(21, "Siddha", "सिद्ध", YogaNature.AUSPICIOUS),
    SADHYA(22, "Sadhya", "साध्य", YogaNature.AUSPICIOUS),
    SHUBHA(23, "Shubha", "शुभ", YogaNature.AUSPICIOUS),
    SHUKLA(24, "Shukla", "शुक्ल", YogaNature.AUSPICIOUS),
    BRAHMA(25, "Brahma", "ब्रह्म", YogaNature.AUSPICIOUS),
    INDRA(26, "Indra", "इन्द्र", YogaNature.AUSPICIOUS),
    VAIDHRITI(27, "Vaidhriti", "वैधृति", YogaNature.INAUSPICIOUS);

    fun getLocalizedName(language: Language): String {
        return when (language) {
            Language.ENGLISH -> this.displayName
            Language.NEPALI -> this.sanskrit
        }
    }
}

enum class YogaNature(val displayName: String) {
    AUSPICIOUS("Auspicious"),
    INAUSPICIOUS("Inauspicious"),
    MIXED("Mixed");

    fun getLocalizedName(language: Language): String {
        val key = when (this) {
            AUSPICIOUS -> StringKeyAnalysis.YOGA_NATURE_AUSPICIOUS
            INAUSPICIOUS -> StringKeyAnalysis.YOGA_NATURE_INAUSPICIOUS
            MIXED -> StringKeyAnalysis.YOGA_NATURE_MIXED
        }
        return StringResources.get(key, language)
    }
}

enum class Karana(val displayName: String, val sanskrit: String, val type: KaranaType) {
    KIMSTUGHNA("Kimstughna", "किंस्तुघ्न", KaranaType.FIXED),
    BAVA("Bava", "बव", KaranaType.MOVABLE),
    BALAVA("Balava", "बालव", KaranaType.MOVABLE),
    KAULAVA("Kaulava", "कौलव", KaranaType.MOVABLE),
    TAITILA("Taitila", "तैतिल", KaranaType.MOVABLE),
    GARA("Gara", "गर", KaranaType.MOVABLE),
    VANIJA("Vanija", "वणिज", KaranaType.MOVABLE),
    VISHTI("Vishti", "विष्टि", KaranaType.MOVABLE),
    SHAKUNI("Shakuni", "शकुनि", KaranaType.FIXED),
    CHATUSHPADA("Chatushpada", "चतुष्पाद", KaranaType.FIXED),
    NAGA("Naga", "नाग", KaranaType.FIXED);

    val nature: String
        get() = type.displayName
}

enum class KaranaType(val displayName: String) {
    FIXED("Fixed"),
    MOVABLE("Movable")
}

enum class Vara(val number: Int, val displayName: String, val sanskrit: String, val lord: Planet) {
    SUNDAY(0, "Sunday", "रविवार", Planet.SUN),
    MONDAY(1, "Monday", "सोमवार", Planet.MOON),
    TUESDAY(2, "Tuesday", "मंगलवार", Planet.MARS),
    WEDNESDAY(3, "Wednesday", "बुधवार", Planet.MERCURY),
    THURSDAY(4, "Thursday", "गुरुवार", Planet.JUPITER),
    FRIDAY(5, "Friday", "शुक्रवार", Planet.VENUS),
    SATURDAY(6, "Saturday", "शनिवार", Planet.SATURN)
}

enum class Paksha(val displayName: String, val sanskrit: String) {
    SHUKLA("Shukla Paksha", "शुक्ल पक्ष"),
    KRISHNA("Krishna Paksha", "कृष्ण पक्ष");

    fun getLocalizedName(language: Language): String {
        val key = when (this) {
            SHUKLA -> StringKeyAnalysis.PAKSHA_SHUKLA
            KRISHNA -> StringKeyAnalysis.PAKSHA_KRISHNA
        }
        return StringResources.get(key, language)
    }
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
    val sunriseTime: LocalTime = LocalTime.of(6, 0),
    val sunsetTime: LocalTime = LocalTime.of(18, 0),
    val sunriseJD: Double = 0.0,
    val sunsetJD: Double = 0.0,
    val moonPhase: Double,
    val sunLongitude: Double,
    val moonLongitude: Double,
    val ayanamsa: Double = 0.0
) {
    val isShuklaPaksha: Boolean
        get() = paksha == Paksha.SHUKLA

    val tithiInPaksha: Int
        get() = if (tithi.number <= 15) tithi.number else tithi.number - 15
}

