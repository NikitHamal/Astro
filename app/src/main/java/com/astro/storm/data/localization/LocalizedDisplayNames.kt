package com.astro.storm.core.common

import com.astro.storm.core.model.Gender
import com.astro.storm.core.model.HouseSystem
import com.astro.storm.core.model.Nakshatra
import com.astro.storm.core.model.Planet
import com.astro.storm.core.model.ZodiacSign
import com.astro.storm.data.preferences.ThemeMode
import com.astro.storm.ephemeris.remedy.*
import com.astro.storm.ephemeris.remedy.RemediesCalculator
import com.astro.storm.core.model.Yoni
import com.astro.storm.ephemeris.panchanga.Tithi
import com.astro.storm.ephemeris.panchanga.TithiGroup
import com.astro.storm.ephemeris.panchanga.Yoga as NityaYoga
import com.astro.storm.ephemeris.panchanga.YogaNature
import com.astro.storm.ephemeris.panchanga.Karana
import com.astro.storm.ephemeris.panchanga.KaranaType
import com.astro.storm.ephemeris.panchanga.Vara
import com.astro.storm.ephemeris.panchanga.Paksha
import com.astro.storm.ephemeris.StrengthRating
import com.astro.storm.ephemeris.KakshaTransitCalculator
import com.astro.storm.ephemeris.UpachayaLevel
import com.astro.storm.ephemeris.TransitQuality as UpachayaTransitQuality
import com.astro.storm.ephemeris.TransitReference
import com.astro.storm.ephemeris.HouseStrength
import com.astro.storm.core.common.StringKeyAdvanced
import com.astro.storm.ephemeris.RetrogradeCombustionCalculator
import com.astro.storm.ephemeris.yoga.YogaCategory
import com.astro.storm.ephemeris.yoga.YogaStrength
import com.astro.storm.ephemeris.remedy.RemedyCategory

/**
 * Extension functions for localized display names of various enums
 *
 * This provides a centralized way to get localized names for all enums
 * used throughout the app without modifying the original enum classes.
 */

/**
 * Get localized display name for Planet
 */
fun Planet.getLocalizedName(language: Language): String {
    return when (this) {
        Planet.SUN -> StringResources.get(StringKeyGeneralPart8.PLANET_SUN, language)
        Planet.MOON -> StringResources.get(StringKeyGeneralPart8.PLANET_MOON, language)
        Planet.MERCURY -> StringResources.get(StringKeyGeneralPart8.PLANET_MERCURY, language)
        Planet.VENUS -> StringResources.get(StringKeyGeneralPart8.PLANET_VENUS, language)
        Planet.MARS -> StringResources.get(StringKeyGeneralPart8.PLANET_MARS, language)
        Planet.JUPITER -> StringResources.get(StringKeyGeneralPart8.PLANET_JUPITER, language)
        Planet.SATURN -> StringResources.get(StringKeyGeneralPart8.PLANET_SATURN, language)
        Planet.RAHU -> StringResources.get(StringKeyGeneralPart8.PLANET_RAHU, language)
        Planet.KETU -> StringResources.get(StringKeyGeneralPart8.PLANET_KETU, language)
        Planet.TRUE_NODE -> StringResources.get(StringKeyGeneralPart8.PLANET_RAHU, language) // Reuse Rahu key for now or add TRa
        Planet.URANUS -> StringResources.get(StringKeyGeneralPart8.PLANET_URANUS, language)
        Planet.NEPTUNE -> StringResources.get(StringKeyGeneralPart8.PLANET_NEPTUNE, language)
        Planet.PLUTO -> StringResources.get(StringKeyGeneralPart8.PLANET_PLUTO, language)
    }
}

/**
 * Get localized display name for ZodiacSign
 */
fun ZodiacSign.getLocalizedName(language: Language): String {
    return when (this) {
        ZodiacSign.ARIES -> StringResources.get(StringKeyGeneralPart10.SIGN_ARIES, language)
        ZodiacSign.TAURUS -> StringResources.get(StringKeyGeneralPart10.SIGN_TAURUS, language)
        ZodiacSign.GEMINI -> StringResources.get(StringKeyGeneralPart10.SIGN_GEMINI, language)
        ZodiacSign.CANCER -> StringResources.get(StringKeyGeneralPart10.SIGN_CANCER, language)
        ZodiacSign.LEO -> StringResources.get(StringKeyGeneralPart10.SIGN_LEO, language)
        ZodiacSign.VIRGO -> StringResources.get(StringKeyGeneralPart10.SIGN_VIRGO, language)
        ZodiacSign.LIBRA -> StringResources.get(StringKeyGeneralPart10.SIGN_LIBRA, language)
        ZodiacSign.SCORPIO -> StringResources.get(StringKeyGeneralPart10.SIGN_SCORPIO, language)
        ZodiacSign.SAGITTARIUS -> StringResources.get(StringKeyGeneralPart10.SIGN_SAGITTARIUS, language)
        ZodiacSign.CAPRICORN -> StringResources.get(StringKeyGeneralPart10.SIGN_CAPRICORN, language)
        ZodiacSign.AQUARIUS -> StringResources.get(StringKeyGeneralPart10.SIGN_AQUARIUS, language)
        ZodiacSign.PISCES -> StringResources.get(StringKeyGeneralPart10.SIGN_PISCES, language)
    }
}

/**
 * Get localized display name for Nakshatra
 */
fun Nakshatra.getLocalizedName(language: Language): String {
    return when (this) {
        Nakshatra.ASHWINI -> StringResources.get(StringKeyPanchanga.NAKSHATRA_ASHWINI, language)
        Nakshatra.BHARANI -> StringResources.get(StringKeyPanchanga.NAKSHATRA_BHARANI, language)
        Nakshatra.KRITTIKA -> StringResources.get(StringKeyPanchanga.NAKSHATRA_KRITTIKA, language)
        Nakshatra.ROHINI -> StringResources.get(StringKeyPanchanga.NAKSHATRA_ROHINI, language)
        Nakshatra.MRIGASHIRA -> StringResources.get(StringKeyPanchanga.NAKSHATRA_MRIGASHIRA, language)
        Nakshatra.ARDRA -> StringResources.get(StringKeyPanchanga.NAKSHATRA_ARDRA, language)
        Nakshatra.PUNARVASU -> StringResources.get(StringKeyPanchanga.NAKSHATRA_PUNARVASU, language)
        Nakshatra.PUSHYA -> StringResources.get(StringKeyPanchanga.NAKSHATRA_PUSHYA, language)
        Nakshatra.ASHLESHA -> StringResources.get(StringKeyPanchanga.NAKSHATRA_ASHLESHA, language)
        Nakshatra.MAGHA -> StringResources.get(StringKeyPanchanga.NAKSHATRA_MAGHA, language)
        Nakshatra.PURVA_PHALGUNI -> StringResources.get(StringKeyPanchanga.NAKSHATRA_PURVA_PHALGUNI, language)
        Nakshatra.UTTARA_PHALGUNI -> StringResources.get(StringKeyPanchanga.NAKSHATRA_UTTARA_PHALGUNI, language)
        Nakshatra.HASTA -> StringResources.get(StringKeyPanchanga.NAKSHATRA_HASTA, language)
        Nakshatra.CHITRA -> StringResources.get(StringKeyPanchanga.NAKSHATRA_CHITRA, language)
        Nakshatra.SWATI -> StringResources.get(StringKeyPanchanga.NAKSHATRA_SWATI, language)
        Nakshatra.VISHAKHA -> StringResources.get(StringKeyPanchanga.NAKSHATRA_VISHAKHA, language)
        Nakshatra.ANURADHA -> StringResources.get(StringKeyPanchanga.NAKSHATRA_ANURADHA, language)
        Nakshatra.JYESHTHA -> StringResources.get(StringKeyPanchanga.NAKSHATRA_JYESHTHA, language)
        Nakshatra.MULA -> StringResources.get(StringKeyPanchanga.NAKSHATRA_MULA, language)
        Nakshatra.PURVA_ASHADHA -> StringResources.get(StringKeyPanchanga.NAKSHATRA_PURVA_ASHADHA, language)
        Nakshatra.UTTARA_ASHADHA -> StringResources.get(StringKeyPanchanga.NAKSHATRA_UTTARA_ASHADHA, language)
        Nakshatra.SHRAVANA -> StringResources.get(StringKeyPanchanga.NAKSHATRA_SHRAVANA, language)
        Nakshatra.DHANISHTHA -> StringResources.get(StringKeyPanchanga.NAKSHATRA_DHANISHTHA, language)
        Nakshatra.SHATABHISHA -> StringResources.get(StringKeyPanchanga.NAKSHATRA_SHATABHISHA, language)
        Nakshatra.PURVA_BHADRAPADA -> StringResources.get(StringKeyPanchanga.NAKSHATRA_PURVA_BHADRAPADA, language)
        Nakshatra.UTTARA_BHADRAPADA -> StringResources.get(StringKeyPanchanga.NAKSHATRA_UTTARA_BHADRAPADA, language)
        Nakshatra.REVATI -> StringResources.get(StringKeyPanchanga.NAKSHATRA_REVATI, language)
    }
}

// ============================================
// PANCHANGA EXTENSIONS
// ============================================

/**
 * Get localized display name for Tithi
 */
fun Tithi.getLocalizedName(language: Language): String {
    return when (language) {
        Language.ENGLISH -> this.displayName
        Language.NEPALI -> this.sanskrit
    }
}

/**
 * Get localized display name for TithiGroup
 */
fun TithiGroup.getLocalizedName(language: Language): String {
    return when (language) {
        Language.ENGLISH -> this.displayName
        Language.NEPALI -> when (this) {
            TithiGroup.NANDA -> "नन्दा"
            TithiGroup.BHADRA -> "भद्रा"
            TithiGroup.JAYA -> "जया"
            TithiGroup.RIKTA -> "रिक्ता"
            TithiGroup.PURNA -> "पूर्णा"
        }
    }
}

/**
 * Get localized nature description for TithiGroup
 */
fun TithiGroup.getLocalizedNature(language: Language): String {
    return when (language) {
        Language.ENGLISH -> this.nature
        Language.NEPALI -> when (this) {
            TithiGroup.NANDA -> "आनन्दमय"
            TithiGroup.BHADRA -> "शुभ"
            TithiGroup.JAYA -> "विजयी"
            TithiGroup.RIKTA -> "रिक्त"
            TithiGroup.PURNA -> "पूर्ण"
        }
    }
}

/**
 * Get localized display name for YogaNature
 */
fun YogaNature.getLocalizedName(language: Language): String {
    return when (language) {
        Language.ENGLISH -> this.displayName
        Language.NEPALI -> when (this) {
            YogaNature.AUSPICIOUS -> "शुभ"
            YogaNature.INAUSPICIOUS -> "अशुभ"
            YogaNature.MIXED -> "मिश्रित"
        }
    }
}

/**
 * Get localized display name for Karana
 */
fun Karana.getLocalizedName(language: Language): String {
    return when (language) {
        Language.ENGLISH -> this.displayName
        Language.NEPALI -> this.sanskrit
    }
}

/**
 * Get localized display name for KaranaType
 */
fun KaranaType.getLocalizedName(language: Language): String {
    return when (language) {
        Language.ENGLISH -> this.displayName
        Language.NEPALI -> when (this) {
            KaranaType.FIXED -> "स्थिर"
            KaranaType.MOVABLE -> "चर"
        }
    }
}

/**
 * Get localized display name for Vara
 */
fun Vara.getLocalizedName(language: Language): String {
    return when (language) {
        Language.ENGLISH -> this.displayName
        Language.NEPALI -> this.sanskrit
    }
}

/**
 * Get localized display name for Paksha
 */
fun Paksha.getLocalizedName(language: Language): String {
    return when (language) {
        Language.ENGLISH -> this.displayName
        Language.NEPALI -> this.sanskrit
    }
}

/**
 * Get localized display name for Gender
 */
fun Gender.getLocalizedName(language: Language): String {
    return when (this) {
        Gender.MALE -> StringResources.get(StringKeyUIPart1.GENDER_MALE, language)
        Gender.FEMALE -> StringResources.get(StringKeyUIPart1.GENDER_FEMALE, language)
        Gender.OTHER -> StringResources.get(StringKeyUIPart1.GENDER_OTHER, language)
    }
}

/**
 * Get localized display name for HouseSystem
 */
fun HouseSystem.getLocalizedName(language: Language): String {
    return when (this) {
        HouseSystem.PLACIDUS -> StringResources.get(StringKeyGeneralPart5.HOUSE_PLACIDUS, language)
        HouseSystem.KOCH -> StringResources.get(StringKeyGeneralPart5.HOUSE_KOCH, language)
        HouseSystem.PORPHYRIUS -> StringResources.get(StringKeyGeneralPart5.HOUSE_PORPHYRIUS, language)
        HouseSystem.REGIOMONTANUS -> StringResources.get(StringKeyGeneralPart5.HOUSE_REGIOMONTANUS, language)
        HouseSystem.CAMPANUS -> StringResources.get(StringKeyGeneralPart5.HOUSE_CAMPANUS, language)
        HouseSystem.EQUAL -> StringResources.get(StringKeyGeneralPart5.HOUSE_EQUAL, language)
        HouseSystem.WHOLE_SIGN -> StringResources.get(StringKeyGeneralPart5.HOUSE_WHOLE_SIGN, language)
        HouseSystem.VEHLOW -> StringResources.get(StringKeyGeneralPart5.HOUSE_VEHLOW, language)
        HouseSystem.MERIDIAN -> StringResources.get(StringKeyGeneralPart5.HOUSE_MERIDIAN, language)
        HouseSystem.MORINUS -> StringResources.get(StringKeyGeneralPart5.HOUSE_MORINUS, language)
        HouseSystem.ALCABITUS -> StringResources.get(StringKeyGeneralPart5.HOUSE_ALCABITUS, language)
    }
}

/**
 * Get localized display name for ThemeMode
 */
fun ThemeMode.getLocalizedName(language: Language): String {
    return when (this) {
        ThemeMode.LIGHT -> StringResources.get(StringKeyPrediction.THEME_LIGHT, language)
        ThemeMode.DARK -> StringResources.get(StringKeyPrediction.THEME_DARK, language)
        ThemeMode.SYSTEM -> StringResources.get(StringKeyPrediction.THEME_SYSTEM, language)
    }
}

/**
 * Get localized description for ThemeMode
 */
fun ThemeMode.getLocalizedDescription(language: Language): String {
    return when (this) {
        ThemeMode.LIGHT -> StringResources.get(StringKeyPrediction.THEME_LIGHT_DESC, language)
        ThemeMode.DARK -> StringResources.get(StringKeyPrediction.THEME_DARK_DESC, language)
        ThemeMode.SYSTEM -> StringResources.get(StringKeyPrediction.THEME_SYSTEM_DESC, language)
    }
}

/**
 * Get localized day name
 */
fun getDayName(dayOfWeek: Int, language: Language): String {
    return when (dayOfWeek) {
        1 -> StringResources.get(StringKeyGeneralPart3.DAY_MONDAY, language)
        2 -> StringResources.get(StringKeyGeneralPart3.DAY_TUESDAY, language)
        3 -> StringResources.get(StringKeyGeneralPart3.DAY_WEDNESDAY, language)
        4 -> StringResources.get(StringKeyGeneralPart3.DAY_THURSDAY, language)
        5 -> StringResources.get(StringKeyGeneralPart3.DAY_FRIDAY, language)
        6 -> StringResources.get(StringKeyGeneralPart3.DAY_SATURDAY, language)
        7 -> StringResources.get(StringKeyGeneralPart3.DAY_SUNDAY, language)
        else -> StringResources.get(StringKeyGeneralPart7.MISC_UNKNOWN, language)
    }
}

/**
 * Get energy description based on level
 */
fun getEnergyDescription(energy: Int, language: Language): String {
    return when {
        energy >= 9 -> StringResources.get(StringKeyGeneralPart4.ENERGY_EXCEPTIONAL, language)
        energy >= 8 -> StringResources.get(StringKeyGeneralPart4.ENERGY_EXCELLENT, language)
        energy >= 7 -> StringResources.get(StringKeyGeneralPart4.ENERGY_STRONG, language)
        energy >= 6 -> StringResources.get(StringKeyGeneralPart4.ENERGY_FAVORABLE, language)
        energy >= 5 -> StringResources.get(StringKeyGeneralPart4.ENERGY_BALANCED, language)
        energy >= 4 -> StringResources.get(StringKeyGeneralPart4.ENERGY_MODERATE, language)
        energy >= 3 -> StringResources.get(StringKeyGeneralPart4.ENERGY_LOWER, language)
        energy >= 2 -> StringResources.get(StringKeyGeneralPart4.ENERGY_CHALLENGING, language)
        else -> StringResources.get(StringKeyGeneralPart4.ENERGY_REST, language)
    }
}

/**
 * Get Ayanamsa localized name
 */
fun getAyanamsaLocalizedName(ayanamsa: String, language: Language): String {
    return when (ayanamsa.lowercase()) {
        "lahiri" -> StringResources.get(StringKeyGeneralPart2.AYANAMSA_LAHIRI, language)
        "raman" -> StringResources.get(StringKeyGeneralPart2.AYANAMSA_RAMAN, language)
        "krishnamurti" -> StringResources.get(StringKeyGeneralPart2.AYANAMSA_KRISHNAMURTI, language)
        "true chitrapaksha" -> StringResources.get(StringKeyGeneralPart2.AYANAMSA_TRUE_CHITRAPAKSHA, language)
        else -> ayanamsa
    }
}

/**
 * Format duration with localization
 */
fun formatLocalizedDuration(days: Long, language: Language): String {
    if (days <= 0) return "0d"

    return when {
        days < 7 -> StringResources.get(StringKeyGeneralPart11.TIME_DAYS, language, days)
        days < 30 -> {
            val weeks = days / 7
            StringResources.get(StringKeyGeneralPart11.TIME_WEEKS, language, weeks)
        }
        days < 365 -> {
            val months = days / 30
            StringResources.get(StringKeyGeneralPart11.TIME_MONTHS, language, months)
        }
        else -> {
            val years = days / 365
            StringResources.get(StringKeyGeneralPart11.TIME_YEARS, language, years)
        }
    }
}

// ============================================
// YOGA CALCULATOR EXTENSIONS
// ============================================

// YogaCategory and YogaStrength localized names are now handled as member functions 
// in YogaModels.kt. Do not define them here as extensions to avoid ambiguity.

// ============================================
// REMEDIES CALCULATOR EXTENSIONS
// ============================================

/**
 * Get localized display name for PlanetaryStrength
 */
fun PlanetaryStrength.getLocalizedName(language: Language): String {
    return when (this) {
        PlanetaryStrength.VERY_STRONG -> StringResources.get(StringKeyGeneralPart8.PLANETARY_STRENGTH_VERY_STRONG, language)
        PlanetaryStrength.STRONG -> StringResources.get(StringKeyGeneralPart8.PLANETARY_STRENGTH_STRONG, language)
        PlanetaryStrength.MODERATE -> StringResources.get(StringKeyGeneralPart8.PLANETARY_STRENGTH_MODERATE, language)
        PlanetaryStrength.WEAK -> StringResources.get(StringKeyGeneralPart8.PLANETARY_STRENGTH_WEAK, language)
        PlanetaryStrength.VERY_WEAK -> StringResources.get(StringKeyGeneralPart8.PLANETARY_STRENGTH_VERY_WEAK, language)
        PlanetaryStrength.AFFLICTED -> StringResources.get(StringKeyGeneralPart8.PLANETARY_STRENGTH_AFFLICTED, language)
    }
}

/**
 * Get localized display name for Shadbala StrengthRating
 */
fun StrengthRating.getLocalizedName(language: Language): String {
    return when (this) {
        StrengthRating.EXTREMELY_WEAK -> StringResources.get(StringKeyAnalysis.SHADBALA_EXTREMELY_WEAK, language)
        StrengthRating.WEAK -> StringResources.get(StringKeyAnalysis.SHADBALA_WEAK, language)
        StrengthRating.BELOW_AVERAGE -> StringResources.get(StringKeyAnalysis.SHADBALA_BELOW_AVERAGE, language)
        StrengthRating.AVERAGE -> StringResources.get(StringKeyAnalysis.SHADBALA_AVERAGE, language)
        StrengthRating.ABOVE_AVERAGE -> StringResources.get(StringKeyAnalysis.SHADBALA_ABOVE_AVERAGE, language)
        StrengthRating.STRONG -> StringResources.get(StringKeyAnalysis.SHADBALA_STRONG, language)
        StrengthRating.VERY_STRONG -> StringResources.get(StringKeyAnalysis.SHADBALA_VERY_STRONG, language)
        StrengthRating.EXTREMELY_STRONG -> StringResources.get(StringKeyAnalysis.SHADBALA_EXTREMELY_STRONG, language)
    }
}

/**
 * Get localized display name for CombustionStatus
 */
fun RetrogradeCombustionCalculator.CombustionStatus.getLocalizedName(language: Language): String {
    return when (this) {
        RetrogradeCombustionCalculator.CombustionStatus.NOT_COMBUST -> StringResources.get(StringKeyGeneralPart3.COMBUSTION_NOT_COMBUST, language)
        RetrogradeCombustionCalculator.CombustionStatus.APPROACHING -> StringResources.get(StringKeyGeneralPart3.COMBUSTION_APPROACHING, language)
        RetrogradeCombustionCalculator.CombustionStatus.COMBUST -> StringResources.get(StringKeyGeneralPart3.COMBUSTION_COMBUST, language)
        RetrogradeCombustionCalculator.CombustionStatus.DEEP_COMBUST -> StringResources.get(StringKeyGeneralPart3.COMBUSTION_DEEP_COMBUST, language)
        RetrogradeCombustionCalculator.CombustionStatus.CAZIMI -> StringResources.get(StringKeyGeneralPart3.COMBUSTION_CAZIMI, language)
        RetrogradeCombustionCalculator.CombustionStatus.SEPARATING -> StringResources.get(StringKeyGeneralPart3.COMBUSTION_SEPARATING, language)
    }
}

/**
 * Get localized display name for RemedyCategory
 */
fun RemedyCategory.getLocalizedName(language: Language): String {
    return when (this) {
        RemedyCategory.GEMSTONE -> StringResources.get(StringKeyRemedy.REMEDY_CAT_GEMSTONE, language)
        RemedyCategory.MANTRA -> StringResources.get(StringKeyRemedy.REMEDY_CAT_MANTRA, language)
        RemedyCategory.YANTRA -> StringResources.get(StringKeyRemedy.REMEDY_CAT_YANTRA, language)
        RemedyCategory.CHARITY -> StringResources.get(StringKeyRemedy.REMEDY_CAT_CHARITY, language)
        RemedyCategory.FASTING -> StringResources.get(StringKeyRemedy.REMEDY_CAT_FASTING, language)
        RemedyCategory.COLOR -> StringResources.get(StringKeyRemedy.REMEDY_CAT_COLOR, language)
        RemedyCategory.METAL -> StringResources.get(StringKeyRemedy.REMEDY_CAT_METAL, language)
        RemedyCategory.RUDRAKSHA -> StringResources.get(StringKeyRemedy.REMEDY_CAT_RUDRAKSHA, language)
        RemedyCategory.DEITY -> StringResources.get(StringKeyRemedy.REMEDY_CAT_DEITY, language)
        RemedyCategory.LIFESTYLE -> StringResources.get(StringKeyRemedy.REMEDY_CAT_LIFESTYLE, language)
    }
}

/**
 * Get localized display name for RemedyPriority
 */
fun RemedyPriority.getLocalizedName(language: Language): String {
    return when (this) {
        RemedyPriority.ESSENTIAL -> StringResources.get(StringKeyRemedy.REMEDY_PRIORITY_ESSENTIAL, language)
        RemedyPriority.HIGHLY_RECOMMENDED -> StringResources.get(StringKeyRemedy.REMEDY_PRIORITY_HIGHLY_RECOMMENDED, language)
        RemedyPriority.RECOMMENDED -> StringResources.get(StringKeyRemedy.REMEDY_PRIORITY_RECOMMENDED, language)
        RemedyPriority.OPTIONAL -> StringResources.get(StringKeyRemedy.REMEDY_PRIORITY_OPTIONAL, language)
    }
}

/**
 * Get localized display name for PanchMahapurusha recommendation category
 */
fun com.astro.storm.ephemeris.PanchMahapurushaYogaCalculator.RecommendationType.getLocalizedName(language: Language): String {
    return when (this) {
        com.astro.storm.ephemeris.PanchMahapurushaYogaCalculator.RecommendationType.STRENGTHENING -> if (language == Language.NEPALI) "सुदृढीकरण अभ्यास" else "Strengthening Practice"
        com.astro.storm.ephemeris.PanchMahapurushaYogaCalculator.RecommendationType.CAREER -> if (language == Language.NEPALI) "क्यारियर संरेखण" else "Career Alignment"
        com.astro.storm.ephemeris.PanchMahapurushaYogaCalculator.RecommendationType.GEMSTONE -> if (language == Language.NEPALI) "रत्न सिफारिस" else "Gemstone Recommendation"
        com.astro.storm.ephemeris.PanchMahapurushaYogaCalculator.RecommendationType.MANTRA -> if (language == Language.NEPALI) "मन्त्र जप" else "Mantra Recitation"
        com.astro.storm.ephemeris.PanchMahapurushaYogaCalculator.RecommendationType.CHARITY -> if (language == Language.NEPALI) "परोपकारी कार्य" else "Charitable Practice"
    }
}

// ============================================
// MATCHMAKING CALCULATOR EXTENSIONS
// ============================================

// NOTE: Varna, Vashya, Gana, Yoni, Nadi, Rajju, ManglikDosha, and CompatibilityRating
// already have getLocalizedName() and getLocalizedDescription() methods defined
// as member functions in MatchmakingModels.kt. Do not duplicate them here to avoid
// overload resolution ambiguity errors.

/**
 * Get localized display name for Yoni animal
 * This is a utility function since Yoni doesn't have a member function for localized animal names.
 */
fun getYoniLocalizedAnimalName(yoni: Yoni, language: Language): String {
    return yoni.getLocalizedAnimal(language)
}

/**
 * Get localized house signification
 */
fun getHouseSignification(house: Int, language: Language): String {
    return when (house) {
        1 -> StringResources.get(StringKeyGeneralPart5.HOUSE_1_SIGNIFICATION, language)
        2 -> StringResources.get(StringKeyGeneralPart5.HOUSE_2_SIGNIFICATION, language)
        3 -> StringResources.get(StringKeyGeneralPart5.HOUSE_3_SIGNIFICATION, language)
        4 -> StringResources.get(StringKeyGeneralPart5.HOUSE_4_SIGNIFICATION, language)
        5 -> StringResources.get(StringKeyGeneralPart5.HOUSE_5_SIGNIFICATION, language)
        6 -> StringResources.get(StringKeyGeneralPart5.HOUSE_6_SIGNIFICATION, language)
        7 -> StringResources.get(StringKeyGeneralPart5.HOUSE_7_SIGNIFICATION, language)
        8 -> StringResources.get(StringKeyGeneralPart5.HOUSE_8_SIGNIFICATION, language)
        9 -> StringResources.get(StringKeyGeneralPart5.HOUSE_9_SIGNIFICATION, language)
        10 -> StringResources.get(StringKeyGeneralPart5.HOUSE_10_SIGNIFICATION, language)
        11 -> StringResources.get(StringKeyGeneralPart5.HOUSE_11_SIGNIFICATION, language)
        12 -> StringResources.get(StringKeyGeneralPart5.HOUSE_12_SIGNIFICATION, language)
        else -> ""
    }
}

/**
 * Get localized Choghadiya name
 */
fun getChoghadiyaName(choghadiya: String, language: Language): String {
    return when (choghadiya.lowercase()) {
        "amrit" -> StringResources.get(StringKeyGeneralPart3.CHOGHADIYA_AMRIT, language)
        "shubh" -> StringResources.get(StringKeyGeneralPart3.CHOGHADIYA_SHUBH, language)
        "labh" -> StringResources.get(StringKeyGeneralPart3.CHOGHADIYA_LABH, language)
        "char" -> StringResources.get(StringKeyGeneralPart3.CHOGHADIYA_CHAR, language)
        "rog" -> StringResources.get(StringKeyGeneralPart3.CHOGHADIYA_ROG, language)
        "kaal" -> StringResources.get(StringKeyGeneralPart3.CHOGHADIYA_KAAL, language)
        "udveg" -> StringResources.get(StringKeyGeneralPart3.CHOGHADIYA_UDVEG, language)
        else -> choghadiya
    }
}

// ============================================
// HOROSCOPE CALCULATOR EXTENSIONS
// ============================================

/**
 * Get localized display name for HoroscopeCalculator.LifeArea
 */
fun com.astro.storm.ephemeris.HoroscopeCalculator.LifeArea.getLocalizedName(language: Language): String {
    return when (this) {
        com.astro.storm.ephemeris.HoroscopeCalculator.LifeArea.CAREER -> StringResources.get(StringKeyGeneralPart6.LIFE_AREA_CAREER, language)
        com.astro.storm.ephemeris.HoroscopeCalculator.LifeArea.LOVE -> StringResources.get(StringKeyGeneralPart6.LIFE_AREA_LOVE, language)
        com.astro.storm.ephemeris.HoroscopeCalculator.LifeArea.HEALTH -> StringResources.get(StringKeyGeneralPart6.LIFE_AREA_HEALTH, language)
        com.astro.storm.ephemeris.HoroscopeCalculator.LifeArea.FINANCE -> StringResources.get(StringKeyGeneralPart6.LIFE_AREA_FINANCE, language)
        com.astro.storm.ephemeris.HoroscopeCalculator.LifeArea.FAMILY -> StringResources.get(StringKeyGeneralPart6.LIFE_AREA_FAMILY, language)
        com.astro.storm.ephemeris.HoroscopeCalculator.LifeArea.SPIRITUALITY -> StringResources.get(StringKeyGeneralPart6.LIFE_AREA_SPIRITUALITY, language)
    }
}

/**
 * Get localized full display name (with description) for HoroscopeCalculator.LifeArea
 */
fun com.astro.storm.ephemeris.HoroscopeCalculator.LifeArea.getLocalizedFullName(language: Language): String {
    return when (this) {
        com.astro.storm.ephemeris.HoroscopeCalculator.LifeArea.CAREER -> StringResources.get(StringKeyGeneralPart6.LIFE_AREA_CAREER_FULL, language)
        com.astro.storm.ephemeris.HoroscopeCalculator.LifeArea.LOVE -> StringResources.get(StringKeyGeneralPart6.LIFE_AREA_LOVE_FULL, language)
        com.astro.storm.ephemeris.HoroscopeCalculator.LifeArea.HEALTH -> StringResources.get(StringKeyGeneralPart6.LIFE_AREA_HEALTH_FULL, language)
        com.astro.storm.ephemeris.HoroscopeCalculator.LifeArea.FINANCE -> StringResources.get(StringKeyGeneralPart6.LIFE_AREA_FINANCE_FULL, language)
        com.astro.storm.ephemeris.HoroscopeCalculator.LifeArea.FAMILY -> StringResources.get(StringKeyGeneralPart6.LIFE_AREA_FAMILY_FULL, language)
        com.astro.storm.ephemeris.HoroscopeCalculator.LifeArea.SPIRITUALITY -> StringResources.get(StringKeyGeneralPart6.LIFE_AREA_SPIRITUALITY_FULL, language)
    }
}

/**
 * Get localized gemstone name for a planet
 */
fun Planet.getLocalizedGemstoneName(language: Language): String {
    return when (this) {
        Planet.SUN -> StringResources.get(StringKeyUIPart1.GEMSTONE_RUBY, language)
        Planet.MOON -> StringResources.get(StringKeyUIPart1.GEMSTONE_PEARL, language)
        Planet.MARS -> StringResources.get(StringKeyUIPart1.GEMSTONE_RED_CORAL, language)
        Planet.MERCURY -> StringResources.get(StringKeyUIPart1.GEMSTONE_EMERALD, language)
        Planet.JUPITER -> StringResources.get(StringKeyUIPart1.GEMSTONE_YELLOW_SAPPHIRE, language)
        Planet.VENUS -> StringResources.get(StringKeyUIPart1.GEMSTONE_DIAMOND, language)
        Planet.SATURN -> StringResources.get(StringKeyUIPart1.GEMSTONE_BLUE_SAPPHIRE, language)
        Planet.RAHU -> StringResources.get(StringKeyUIPart1.GEMSTONE_HESSONITE, language)
        Planet.KETU -> StringResources.get(StringKeyUIPart1.GEMSTONE_CATS_EYE, language)
        else -> this.displayName
    }
}

/**
 * Get localized direction for a planet
 */
fun Planet.getLocalizedDirection(language: Language): String {
    return when (this) {
        Planet.SUN, Planet.MARS -> StringResources.get(StringKeyGeneralPart6.LUCKY_DIRECTION_EAST, language)
        Planet.MOON, Planet.KETU -> StringResources.get(StringKeyGeneralPart6.LUCKY_DIRECTION_NORTHWEST, language)
        Planet.VENUS -> StringResources.get(StringKeyGeneralPart6.LUCKY_DIRECTION_SOUTHEAST, language)
        Planet.MERCURY -> StringResources.get(StringKeyGeneralPart6.LUCKY_DIRECTION_NORTH, language)
        Planet.JUPITER -> StringResources.get(StringKeyGeneralPart6.LUCKY_DIRECTION_NORTHEAST, language)
        Planet.SATURN -> StringResources.get(StringKeyGeneralPart6.LUCKY_DIRECTION_WEST, language)
        Planet.RAHU -> StringResources.get(StringKeyGeneralPart6.LUCKY_DIRECTION_SOUTHWEST, language)
        else -> StringResources.get(StringKeyGeneralPart6.LUCKY_DIRECTION_EAST, language)
    }
}

/**
 * Get localized dasha recommendation for a planet
 */
fun Planet.getLocalizedDashaRecommendation(language: Language): String {
    return when (this) {
        Planet.SUN -> StringResources.get(StringKeyDashaPart1.DASHA_REC_SUN, language)
        Planet.MOON -> StringResources.get(StringKeyDashaPart1.DASHA_REC_MOON, language)
        Planet.MARS -> StringResources.get(StringKeyDashaPart1.DASHA_REC_MARS, language)
        Planet.MERCURY -> StringResources.get(StringKeyDashaPart1.DASHA_REC_MERCURY, language)
        Planet.JUPITER -> StringResources.get(StringKeyDashaPart1.DASHA_REC_JUPITER, language)
        Planet.VENUS -> StringResources.get(StringKeyDashaPart1.DASHA_REC_VENUS, language)
        Planet.SATURN -> StringResources.get(StringKeyDashaPart1.DASHA_REC_SATURN, language)
        Planet.RAHU -> StringResources.get(StringKeyDashaPart1.DASHA_REC_RAHU, language)
        Planet.KETU -> StringResources.get(StringKeyDashaPart1.DASHA_REC_KETU, language)
        else -> ""
    }
}

/**
 * Get localized dasha affirmation for a planet
 */
fun Planet.getLocalizedDashaAffirmation(language: Language): String {
    return when (this) {
        Planet.SUN -> StringResources.get(StringKeyDashaPart1.DASHA_AFF_SUN, language)
        Planet.MOON -> StringResources.get(StringKeyDashaPart1.DASHA_AFF_MOON, language)
        Planet.MARS -> StringResources.get(StringKeyDashaPart1.DASHA_AFF_MARS, language)
        Planet.MERCURY -> StringResources.get(StringKeyDashaPart1.DASHA_AFF_MERCURY, language)
        Planet.JUPITER -> StringResources.get(StringKeyDashaPart1.DASHA_AFF_JUPITER, language)
        Planet.VENUS -> StringResources.get(StringKeyDashaPart1.DASHA_AFF_VENUS, language)
        Planet.SATURN -> StringResources.get(StringKeyDashaPart1.DASHA_AFF_SATURN, language)
        Planet.RAHU -> StringResources.get(StringKeyDashaPart1.DASHA_AFF_RAHU, language)
        Planet.KETU -> StringResources.get(StringKeyDashaPart1.DASHA_AFF_KETU, language)
        else -> ""
    }
}

/**
 * Get localized caution for a planet (if applicable)
 */
fun Planet.getLocalizedCaution(language: Language): String? {
    return when (this) {
        Planet.SATURN -> StringResources.get(StringKeyGeneralPart2.CAUTION_SATURN, language)
        Planet.MARS -> StringResources.get(StringKeyGeneralPart2.CAUTION_MARS, language)
        Planet.RAHU -> StringResources.get(StringKeyGeneralPart2.CAUTION_RAHU, language)
        Planet.KETU -> StringResources.get(StringKeyGeneralPart2.CAUTION_KETU, language)
        else -> null
    }
}

/**
 * Get localized lucky color by element
 */
fun getLocalizedElementColor(element: String, language: Language): String {
    return when (element.lowercase()) {
        "fire" -> StringResources.get(StringKeyPrediction.LUCKY_COLOR_FIRE, language)
        "earth" -> StringResources.get(StringKeyPrediction.LUCKY_COLOR_EARTH, language)
        "air" -> StringResources.get(StringKeyPrediction.LUCKY_COLOR_AIR, language)
        "water" -> StringResources.get(StringKeyPrediction.LUCKY_COLOR_WATER, language)
        else -> StringResources.get(StringKeyPrediction.LUCKY_COLOR_EARTH, language)
    }
}

/**
 * Get localized element recommendation
 */
fun getLocalizedElementRecommendation(element: String, language: Language): String {
    return when (element.lowercase()) {
        "fire" -> StringResources.get(StringKeyGeneralPart4.ELEMENT_REC_FIRE, language)
        "earth" -> StringResources.get(StringKeyGeneralPart4.ELEMENT_REC_EARTH, language)
        "air" -> StringResources.get(StringKeyGeneralPart4.ELEMENT_REC_AIR, language)
        "water" -> StringResources.get(StringKeyGeneralPart4.ELEMENT_REC_WATER, language)
        else -> StringResources.get(StringKeyGeneralPart4.ELEMENT_REC_EARTH, language)
    }
}

/**
 * Get localized life area recommendation
 */
fun com.astro.storm.ephemeris.HoroscopeCalculator.LifeArea.getLocalizedRecommendation(language: Language): String {
    return when (this) {
        com.astro.storm.ephemeris.HoroscopeCalculator.LifeArea.CAREER -> StringResources.get(StringKeyAnalysis.AREA_REC_CAREER, language)
        com.astro.storm.ephemeris.HoroscopeCalculator.LifeArea.LOVE -> StringResources.get(StringKeyAnalysis.AREA_REC_LOVE, language)
        com.astro.storm.ephemeris.HoroscopeCalculator.LifeArea.HEALTH -> StringResources.get(StringKeyAnalysis.AREA_REC_HEALTH, language)
        com.astro.storm.ephemeris.HoroscopeCalculator.LifeArea.FINANCE -> StringResources.get(StringKeyAnalysis.AREA_REC_FINANCE, language)
        com.astro.storm.ephemeris.HoroscopeCalculator.LifeArea.FAMILY -> StringResources.get(StringKeyAnalysis.AREA_REC_FAMILY, language)
        com.astro.storm.ephemeris.HoroscopeCalculator.LifeArea.SPIRITUALITY -> StringResources.get(StringKeyAnalysis.AREA_REC_SPIRITUALITY, language)
    }
}

/**
 * Get localized name for Kaksha Transit quality
 */
fun KakshaTransitCalculator.KakshaQuality.getLocalizedName(language: Language): String {
    return when (this) {
        KakshaTransitCalculator.KakshaQuality.EXCELLENT -> StringResources.get(StringKeyAdvanced.KAKSHYA_QUALITY_EXCELLENT, language)
        KakshaTransitCalculator.KakshaQuality.GOOD -> StringResources.get(StringKeyAdvanced.KAKSHYA_QUALITY_GOOD, language)
        KakshaTransitCalculator.KakshaQuality.MODERATE -> StringResources.get(StringKeyAdvanced.KAKSHYA_QUALITY_MODERATE, language)
        KakshaTransitCalculator.KakshaQuality.POOR -> StringResources.get(StringKeyAdvanced.KAKSHYA_QUALITY_POOR, language)
    }
}

/**
 * Get localized name for Upachaya Transit level
 */
fun UpachayaLevel.getLocalizedName(language: Language): String {
    return when (this) {
        UpachayaLevel.EXCEPTIONAL -> StringResources.get(StringKeyUIPart1.STRENGTH_EXCELLENT, language)
        UpachayaLevel.HIGH -> StringResources.get(StringKeyUIPart1.STRENGTH_STRONG, language)
        UpachayaLevel.MODERATE -> StringResources.get(StringKeyUIPart1.STRENGTH_AVERAGE, language)
        UpachayaLevel.LOW -> StringResources.get(StringKeyUIPart1.STRENGTH_WEAK, language)
    }
}

/**
 * Get localized name for Upachaya Transit quality
 */
fun UpachayaTransitQuality.getLocalizedName(language: Language): String {
    return when (this) {
        UpachayaTransitQuality.EXCELLENT -> StringResources.get(StringKeyGeneralPart9.QUALITY_EXCELLENT, language)
        UpachayaTransitQuality.GOOD -> StringResources.get(StringKeyGeneralPart9.QUALITY_GOOD, language)
        UpachayaTransitQuality.FAVORABLE -> StringResources.get(StringKeyUIPart1.UI_FAVORABLE_PERIOD, language)
        UpachayaTransitQuality.NEUTRAL -> StringResources.get(StringKeyGeneralPart9.QUALITY_NEUTRAL, language)
    }
}

/**
 * Get localized name for Transit Reference
 */
fun TransitReference.getLocalizedName(language: Language): String {
    return when (this) {
        TransitReference.MOON -> StringResources.get(StringKeyUIPart1.UI_FROM_MOON, language)
        TransitReference.LAGNA -> StringResources.get(StringKeyUIPart1.UI_FROM_LAGNA, language)
    }
}

/**
 * Get localized name for House Strength
 */
fun HouseStrength.getLocalizedName(language: Language): String {
    return when (this) {
        HouseStrength.VERY_STRONG -> StringResources.get(StringKeyUIPart1.STRENGTH_VERY_STRONG, language)
        HouseStrength.STRONG -> StringResources.get(StringKeyUIPart1.STRENGTH_STRONG, language)
        HouseStrength.MODERATE -> StringResources.get(StringKeyUIPart1.STRENGTH_AVERAGE, language)
        HouseStrength.MILD -> StringResources.get(StringKeyUIPart1.STRENGTH_BELOW_AVERAGE, language)
        HouseStrength.INACTIVE -> StringResources.get(StringKeyUIPart2.UI_NONE, language)
    }
}


