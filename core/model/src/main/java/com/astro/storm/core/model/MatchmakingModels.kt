package com.astro.storm.core.model

import androidx.compose.ui.graphics.vector.ImageVector
import com.astro.storm.core.common.Language
import com.astro.storm.core.common.StringKey
import com.astro.storm.core.common.StringKeyInterface
import com.astro.storm.core.common.StringKeyMatch
import com.astro.storm.core.common.StringKeyDosha
import com.astro.storm.core.common.StringKeyAnalysis
import com.astro.storm.core.common.StringResources

/**
 * Matchmaking (Kundli Milan) Data Models
 *
 * Contains all enums and data classes used in Vedic matchmaking analysis
 * based on Ashtakoota (8 Guna) system from classical texts:
 * - Brihat Parasara Hora Shastra (BPHS)
 * - Muhurta Chintamani
 * - Jataka Parijata
 */

// ============================================
// Varna (Spiritual/Ego Classification)
// ============================================

/**
 * Varna represents spiritual compatibility and ego harmony.
 * Based on the element of the Moon sign.
 *
 * @property value Hierarchical value (4=highest, 1=lowest)
 * @property stringKey Localization key
 */
enum class Varna(val value: Int, val stringKey: StringKeyInterface) {
    BRAHMIN(4, StringKeyMatch.VARNA_BRAHMIN),
    KSHATRIYA(3, StringKeyMatch.VARNA_KSHATRIYA),
    VAISHYA(2, StringKeyMatch.VARNA_VAISHYA),
    SHUDRA(1, StringKeyMatch.VARNA_SHUDRA);

    val displayName: String get() = stringKey.en

    fun getLocalizedName(language: Language): String {
        return StringResources.get(stringKey, language)
    }

    companion object {
        /**
         * Get Varna from zodiac sign based on classical mapping
         * Water signs: Brahmin, Fire signs: Kshatriya, Earth signs: Vaishya, Air signs: Shudra
         */
        fun fromZodiacSign(sign: ZodiacSign): Varna = when (sign) {
            ZodiacSign.CANCER, ZodiacSign.SCORPIO, ZodiacSign.PISCES -> BRAHMIN
            ZodiacSign.ARIES, ZodiacSign.LEO, ZodiacSign.SAGITTARIUS -> KSHATRIYA
            ZodiacSign.TAURUS, ZodiacSign.VIRGO, ZodiacSign.CAPRICORN -> VAISHYA
            ZodiacSign.GEMINI, ZodiacSign.LIBRA, ZodiacSign.AQUARIUS -> SHUDRA
        }
    }
}

// ============================================
// Vashya (Mutual Attraction/Control)
// ============================================

/**
 * Vashya indicates the degree of magnetic control or attraction.
 * Determines influence dynamics in the relationship.
 */
enum class Vashya(val stringKey: StringKeyInterface) {
    CHATUSHPADA(StringKeyMatch.VASHYA_CHATUSHPADA),
    MANAVA(StringKeyMatch.VASHYA_MANAVA),
    JALACHARA(StringKeyMatch.VASHYA_JALACHARA),
    VANACHARA(StringKeyMatch.VASHYA_VANACHARA),
    KEETA(StringKeyMatch.VASHYA_KEETA);

    val displayName: String get() = stringKey.en

    fun getLocalizedName(language: Language): String {
        return StringResources.get(stringKey, language)
    }

    companion object {
        fun fromZodiacSign(sign: ZodiacSign): Vashya = when (sign) {
            ZodiacSign.ARIES -> CHATUSHPADA
            ZodiacSign.TAURUS -> CHATUSHPADA
            ZodiacSign.GEMINI -> MANAVA
            ZodiacSign.CANCER -> JALACHARA
            ZodiacSign.LEO -> VANACHARA
            ZodiacSign.VIRGO -> MANAVA
            ZodiacSign.LIBRA -> MANAVA
            ZodiacSign.SCORPIO -> KEETA
            ZodiacSign.SAGITTARIUS -> MANAVA
            ZodiacSign.CAPRICORN -> JALACHARA
            ZodiacSign.AQUARIUS -> MANAVA
            ZodiacSign.PISCES -> JALACHARA
        }

        /** Vashya control relationships */
        val controlPairs: Map<Vashya, Set<Vashya>> = mapOf(
            MANAVA to setOf(CHATUSHPADA, JALACHARA),
            VANACHARA to setOf(CHATUSHPADA),
            CHATUSHPADA to setOf(JALACHARA)
        )

        /** Enemy Vashya pairs */
        val enemyPairs: Set<Set<Vashya>> = setOf(
            setOf(MANAVA, VANACHARA),
            setOf(CHATUSHPADA, VANACHARA)
        )
    }
}

// ============================================
// Gana (Temperament)
// ============================================

/**
 * Gana represents temperament and fundamental nature.
 * Based on Nakshatra classification.
 */
enum class Gana(val stringKey: StringKeyInterface, val descKey: StringKeyInterface) {
    DEVA(StringKeyMatch.GANA_DEVA, StringKeyMatch.GANA_DEVA_DESC),
    MANUSHYA(StringKeyMatch.GANA_MANUSHYA, StringKeyMatch.GANA_MANUSHYA_DESC),
    RAKSHASA(StringKeyMatch.GANA_RAKSHASA, StringKeyMatch.GANA_RAKSHASA_DESC);

    val displayName: String get() = stringKey.en
    val description: String get() = descKey.en

    fun getLocalizedName(language: Language): String {
        return StringResources.get(stringKey, language)
    }

    fun getLocalizedDescription(language: Language): String {
        return StringResources.get(descKey, language)
    }

    companion object {
        fun fromNakshatra(nakshatra: Nakshatra): Gana = when (nakshatra) {
            Nakshatra.ASHWINI, Nakshatra.MRIGASHIRA, Nakshatra.PUNARVASU,
            Nakshatra.PUSHYA, Nakshatra.HASTA, Nakshatra.SWATI,
            Nakshatra.ANURADHA, Nakshatra.SHRAVANA, Nakshatra.REVATI -> DEVA

            Nakshatra.BHARANI, Nakshatra.ROHINI, Nakshatra.ARDRA,
            Nakshatra.PURVA_PHALGUNI, Nakshatra.UTTARA_PHALGUNI,
            Nakshatra.PURVA_ASHADHA, Nakshatra.UTTARA_ASHADHA,
            Nakshatra.PURVA_BHADRAPADA, Nakshatra.UTTARA_BHADRAPADA -> MANUSHYA

            Nakshatra.KRITTIKA, Nakshatra.ASHLESHA, Nakshatra.MAGHA,
            Nakshatra.CHITRA, Nakshatra.VISHAKHA, Nakshatra.JYESHTHA,
            Nakshatra.MULA, Nakshatra.DHANISHTHA, Nakshatra.SHATABHISHA -> RAKSHASA
        }
    }
}

// ============================================
// Yoni (Physical/Sexual Compatibility)
// ============================================

enum class YoniGender { MALE, FEMALE }

/**
 * Yoni represents sexual and physical compatibility.
 * Each Nakshatra is assigned an animal nature with male/female classification.
 */
enum class Yoni(val animalKey: StringKeyMatch, val gender: YoniGender, val groupId: Int) {
    ASHWA_MALE(StringKeyMatch.YONI_ANIMAL_HORSE, YoniGender.MALE, 1),
    ASHWA_FEMALE(StringKeyMatch.YONI_ANIMAL_HORSE, YoniGender.FEMALE, 1),
    GAJA_MALE(StringKeyMatch.YONI_ANIMAL_ELEPHANT, YoniGender.MALE, 2),
    GAJA_FEMALE(StringKeyMatch.YONI_ANIMAL_ELEPHANT, YoniGender.FEMALE, 2),
    MESHA_MALE(StringKeyMatch.YONI_ANIMAL_SHEEP, YoniGender.MALE, 3),
    MESHA_FEMALE(StringKeyMatch.YONI_ANIMAL_SHEEP, YoniGender.FEMALE, 3),
    SARPA_MALE(StringKeyMatch.YONI_ANIMAL_SERPENT, YoniGender.MALE, 4),
    SARPA_FEMALE(StringKeyMatch.YONI_ANIMAL_SERPENT, YoniGender.FEMALE, 4),
    SHWAN_MALE(StringKeyMatch.YONI_ANIMAL_DOG, YoniGender.MALE, 5),
    SHWAN_FEMALE(StringKeyMatch.YONI_ANIMAL_DOG, YoniGender.FEMALE, 5),
    MARJAR_MALE(StringKeyMatch.YONI_ANIMAL_CAT, YoniGender.MALE, 6),
    MARJAR_FEMALE(StringKeyMatch.YONI_ANIMAL_CAT, YoniGender.FEMALE, 6),
    MUSHAK_MALE(StringKeyMatch.YONI_ANIMAL_RAT, YoniGender.MALE, 7),
    MUSHAK_FEMALE(StringKeyMatch.YONI_ANIMAL_RAT, YoniGender.FEMALE, 7),
    GAU_MALE(StringKeyMatch.YONI_ANIMAL_COW, YoniGender.MALE, 8),
    GAU_FEMALE(StringKeyMatch.YONI_ANIMAL_COW, YoniGender.FEMALE, 8),
    MAHISH_MALE(StringKeyMatch.YONI_ANIMAL_BUFFALO, YoniGender.MALE, 9),
    MAHISH_FEMALE(StringKeyMatch.YONI_ANIMAL_BUFFALO, YoniGender.FEMALE, 9),
    VYAGHRA_MALE(StringKeyMatch.YONI_ANIMAL_TIGER, YoniGender.MALE, 10),
    VYAGHRA_FEMALE(StringKeyMatch.YONI_ANIMAL_TIGER, YoniGender.FEMALE, 10),
    MRIGA_MALE(StringKeyMatch.YONI_ANIMAL_DEER, YoniGender.MALE, 11),
    MRIGA_FEMALE(StringKeyMatch.YONI_ANIMAL_DEER, YoniGender.FEMALE, 11),
    VANAR_MALE(StringKeyMatch.YONI_ANIMAL_MONKEY, YoniGender.MALE, 12),
    VANAR_FEMALE(StringKeyMatch.YONI_ANIMAL_MONKEY, YoniGender.FEMALE, 12),
    NAKUL_MALE(StringKeyMatch.YONI_ANIMAL_MONGOOSE, YoniGender.MALE, 13),
    NAKUL_FEMALE(StringKeyMatch.YONI_ANIMAL_MONGOOSE, YoniGender.FEMALE, 13),
    SIMHA_MALE(StringKeyMatch.YONI_ANIMAL_LION, YoniGender.MALE, 14),
    SIMHA_FEMALE(StringKeyMatch.YONI_ANIMAL_LION, YoniGender.FEMALE, 14);

    fun getLocalizedAnimal(language: Language): String = StringResources.get(animalKey, language)
    
    fun getLocalizedGender(language: Language): String = when (gender) {
        YoniGender.MALE -> StringResources.get(StringKeyMatch.GENDER_MALE, language)
        YoniGender.FEMALE -> StringResources.get(StringKeyMatch.GENDER_FEMALE, language)
    }

    companion object {
        fun fromNakshatra(nakshatra: Nakshatra): Yoni = when (nakshatra) {
            Nakshatra.ASHWINI -> ASHWA_MALE
            Nakshatra.SHATABHISHA -> ASHWA_FEMALE
            Nakshatra.BHARANI -> GAJA_MALE
            Nakshatra.REVATI -> GAJA_FEMALE
            Nakshatra.PUSHYA -> MESHA_MALE
            Nakshatra.KRITTIKA -> MESHA_FEMALE
            Nakshatra.ROHINI -> SARPA_MALE
            Nakshatra.MRIGASHIRA -> SARPA_FEMALE
            Nakshatra.MULA -> SHWAN_MALE
            Nakshatra.ARDRA -> SHWAN_FEMALE
            Nakshatra.ASHLESHA -> MARJAR_MALE
            Nakshatra.PUNARVASU -> MARJAR_FEMALE
            Nakshatra.MAGHA -> MUSHAK_MALE
            Nakshatra.PURVA_PHALGUNI -> MUSHAK_FEMALE
            Nakshatra.UTTARA_PHALGUNI -> GAU_MALE
            Nakshatra.UTTARA_BHADRAPADA -> GAU_FEMALE
            Nakshatra.SWATI -> MAHISH_MALE
            Nakshatra.HASTA -> MAHISH_FEMALE
            Nakshatra.VISHAKHA -> VYAGHRA_MALE
            Nakshatra.CHITRA -> VYAGHRA_FEMALE
            Nakshatra.JYESHTHA -> MRIGA_MALE
            Nakshatra.ANURADHA -> MRIGA_FEMALE
            Nakshatra.PURVA_ASHADHA -> VANAR_MALE
            Nakshatra.SHRAVANA -> VANAR_FEMALE
            Nakshatra.UTTARA_ASHADHA -> NAKUL_MALE
            Nakshatra.PURVA_BHADRAPADA -> SIMHA_MALE
            Nakshatra.DHANISHTHA -> SIMHA_FEMALE
        }

        /** Enemy Yoni pairs (group IDs) */
        val enemyPairs: Set<Set<Int>> = setOf(
            setOf(1, 9),   // Horse - Buffalo
            setOf(2, 14),  // Elephant - Lion
            setOf(3, 12),  // Sheep - Monkey
            setOf(4, 13),  // Serpent - Mongoose
            setOf(5, 11),  // Dog - Deer
            setOf(6, 7),   // Cat - Rat
            setOf(8, 10)   // Cow - Tiger
        )

        /** Friendly Yoni groups (group IDs) */
        val friendlyGroups: List<Set<Int>> = listOf(
            setOf(1, 2, 3),     // Horse, Elephant, Sheep
            setOf(4, 5, 6),     // Serpent, Dog, Cat
            setOf(8, 9, 11),    // Cow, Buffalo, Deer
            setOf(12, 14)       // Monkey, Lion
        )
    }
}

// ============================================
// Nadi (Health/Genetic Compatibility)
// ============================================

/**
 * Nadi represents health and genetic compatibility.
 * Most important factor in Ashtakoota carrying 8 points.
 * Same Nadi can cause health issues and affect progeny.
 */
enum class Nadi(val stringKey: StringKeyInterface, val descKey: StringKeyInterface) {
    ADI(StringKeyMatch.NADI_ADI, StringKeyMatch.NADI_ADI_DESC),
    MADHYA(StringKeyMatch.NADI_MADHYA, StringKeyMatch.NADI_MADHYA_DESC),
    ANTYA(StringKeyMatch.NADI_ANTYA, StringKeyMatch.NADI_ANTYA_DESC);

    val displayName: String get() = stringKey.en
    val description: String get() = descKey.en

    fun getLocalizedName(language: Language): String {
        return StringResources.get(stringKey, language)
    }

    fun getLocalizedDescription(language: Language): String {
        return StringResources.get(descKey, language)
    }

    companion object {
        fun fromNakshatra(nakshatra: Nakshatra): Nadi = when (nakshatra) {
            Nakshatra.ASHWINI, Nakshatra.ARDRA, Nakshatra.PUNARVASU,
            Nakshatra.UTTARA_PHALGUNI, Nakshatra.HASTA, Nakshatra.JYESHTHA,
            Nakshatra.MULA, Nakshatra.SHATABHISHA, Nakshatra.PURVA_BHADRAPADA -> ADI

            Nakshatra.BHARANI, Nakshatra.MRIGASHIRA, Nakshatra.PUSHYA,
            Nakshatra.PURVA_PHALGUNI, Nakshatra.CHITRA, Nakshatra.ANURADHA,
            Nakshatra.PURVA_ASHADHA, Nakshatra.DHANISHTHA, Nakshatra.UTTARA_BHADRAPADA -> MADHYA

            Nakshatra.KRITTIKA, Nakshatra.ROHINI, Nakshatra.ASHLESHA,
            Nakshatra.MAGHA, Nakshatra.SWATI, Nakshatra.VISHAKHA,
            Nakshatra.UTTARA_ASHADHA, Nakshatra.SHRAVANA, Nakshatra.REVATI -> ANTYA
        }

        /**
         * Nakshatra pairs that cancel Nadi dosha per classical texts
         */
        val cancellingPairs: List<Set<Nakshatra>> = listOf(
            // Adi Nadi cancelling pairs
            setOf(Nakshatra.ASHWINI, Nakshatra.SHATABHISHA),
            setOf(Nakshatra.ARDRA, Nakshatra.PUNARVASU),
            setOf(Nakshatra.UTTARA_PHALGUNI, Nakshatra.HASTA),
            setOf(Nakshatra.JYESHTHA, Nakshatra.MULA),
            setOf(Nakshatra.PURVA_BHADRAPADA, Nakshatra.UTTARA_BHADRAPADA),
            // Madhya Nadi cancelling pairs
            setOf(Nakshatra.BHARANI, Nakshatra.REVATI),
            setOf(Nakshatra.MRIGASHIRA, Nakshatra.CHITRA),
            setOf(Nakshatra.PUSHYA, Nakshatra.UTTARA_ASHADHA),
            setOf(Nakshatra.PURVA_PHALGUNI, Nakshatra.ANURADHA),
            setOf(Nakshatra.PURVA_ASHADHA, Nakshatra.DHANISHTHA),
            // Antya Nadi cancelling pairs
            setOf(Nakshatra.KRITTIKA, Nakshatra.VISHAKHA),
            setOf(Nakshatra.ROHINI, Nakshatra.SWATI),
            setOf(Nakshatra.ASHLESHA, Nakshatra.MAGHA),
            setOf(Nakshatra.UTTARA_ASHADHA, Nakshatra.SHRAVANA),
            setOf(Nakshatra.REVATI, Nakshatra.ASHWINI)
        )
    }
}

// ============================================
// Rajju (Cosmic Bond/Body Part)
// ============================================

/**
 * Rajju represents the cosmic bond, associated with body parts.
 * Same Rajju can cause problems related to that body part.
 */
enum class Rajju(val stringKey: StringKeyInterface, val bodyPartKey: StringKeyInterface) {
    PADA(StringKeyMatch.RAJJU_PADA, StringKeyMatch.RAJJU_PADA_BODY),
    KATI(StringKeyMatch.RAJJU_KATI, StringKeyMatch.RAJJU_KATI_BODY),
    NABHI(StringKeyMatch.RAJJU_NABHI, StringKeyMatch.RAJJU_NABHI_BODY),
    KANTHA(StringKeyMatch.RAJJU_KANTHA, StringKeyMatch.RAJJU_KANTHA_BODY),
    SIRO(StringKeyMatch.RAJJU_SIRO, StringKeyMatch.RAJJU_SIRO_BODY);

    val displayName: String get() = stringKey.en
    val bodyPart: String get() = bodyPartKey.en

    fun getLocalizedName(language: Language): String {
        return StringResources.get(stringKey, language)
    }

    fun getLocalizedBodyPart(language: Language): String {
        return StringResources.get(bodyPartKey, language)
    }

    /**
     * Get warning message for same Rajju dosha
     */
    fun getWarning(language: Language): String = when (this) {
        SIRO -> StringResources.get(StringKeyMatch.RAJJU_SIRO_WARNING, language)
        KANTHA -> StringResources.get(StringKeyMatch.RAJJU_KANTHA_WARNING, language)
        NABHI -> StringResources.get(StringKeyMatch.RAJJU_NABHI_WARNING, language)
        KATI -> StringResources.get(StringKeyMatch.RAJJU_KATI_WARNING, language)
        PADA -> StringResources.get(StringKeyMatch.RAJJU_PADA_WARNING, language)
    }

    /**
     * Get warning message (English fallback)
     */
    fun getWarningEnglish(): String = when (this) {
        SIRO -> "Most serious - affects longevity of spouse"
        KANTHA -> "May cause health issues to both"
        NABHI -> "May affect children"
        KATI -> "May cause financial difficulties"
        PADA -> "May cause wandering tendencies"
    }

    companion object {
        fun fromNakshatra(nakshatra: Nakshatra): Rajju = when (nakshatra) {
            Nakshatra.ASHWINI, Nakshatra.ASHLESHA, Nakshatra.MAGHA,
            Nakshatra.JYESHTHA, Nakshatra.MULA, Nakshatra.REVATI -> PADA

            Nakshatra.BHARANI, Nakshatra.PUSHYA, Nakshatra.PURVA_PHALGUNI,
            Nakshatra.ANURADHA, Nakshatra.PURVA_ASHADHA, Nakshatra.UTTARA_BHADRAPADA -> KATI

            Nakshatra.KRITTIKA, Nakshatra.PUNARVASU, Nakshatra.UTTARA_PHALGUNI,
            Nakshatra.VISHAKHA, Nakshatra.UTTARA_ASHADHA, Nakshatra.PURVA_BHADRAPADA -> NABHI

            Nakshatra.ROHINI, Nakshatra.ARDRA, Nakshatra.HASTA,
            Nakshatra.SWATI, Nakshatra.SHATABHISHA, Nakshatra.SHRAVANA -> KANTHA

            Nakshatra.MRIGASHIRA, Nakshatra.CHITRA, Nakshatra.DHANISHTHA -> SIRO
        }
    }
}

/**
 * Rajju Arudha (direction) - Ascending or Descending
 * Same Rajju + Same Arudha is most problematic
 */
enum class RajjuArudha(val stringKey: StringKeyInterface) {
    ASCENDING(StringKeyMatch.RAJJU_ASCENDING),
    DESCENDING(StringKeyMatch.RAJJU_DESCENDING);

    val displayName: String get() = stringKey.en

    fun getLocalizedName(language: Language): String {
        return StringResources.get(stringKey, language)
    }

    companion object {
        fun fromNakshatra(nakshatra: Nakshatra): RajjuArudha = when (nakshatra) {
            // Pada Rajju
            Nakshatra.ASHWINI, Nakshatra.MAGHA -> ASCENDING
            Nakshatra.ASHLESHA, Nakshatra.JYESHTHA, Nakshatra.MULA, Nakshatra.REVATI -> DESCENDING
            // Kati Rajju
            Nakshatra.BHARANI, Nakshatra.PURVA_PHALGUNI, Nakshatra.PURVA_ASHADHA -> ASCENDING
            Nakshatra.PUSHYA, Nakshatra.ANURADHA, Nakshatra.UTTARA_BHADRAPADA -> DESCENDING
            // Nabhi Rajju
            Nakshatra.KRITTIKA, Nakshatra.UTTARA_PHALGUNI, Nakshatra.UTTARA_ASHADHA -> ASCENDING
            Nakshatra.PUNARVASU, Nakshatra.VISHAKHA, Nakshatra.PURVA_BHADRAPADA -> DESCENDING
            // Kantha Rajju
            Nakshatra.ROHINI, Nakshatra.HASTA, Nakshatra.SHRAVANA -> ASCENDING
            Nakshatra.ARDRA, Nakshatra.SWATI, Nakshatra.SHATABHISHA -> DESCENDING
            // Siro Rajju
            Nakshatra.MRIGASHIRA -> ASCENDING
            Nakshatra.CHITRA, Nakshatra.DHANISHTHA -> DESCENDING
        }
    }
}

// ============================================
// Manglik Dosha
// ============================================

/**
 * Manglik Dosha severity levels
 */
enum class ManglikDosha(val stringKey: StringKeyInterface, val severity: Int) {
    NONE(StringKeyMatch.MANGLIK_NONE, 0),
    PARTIAL(StringKeyMatch.MANGLIK_PARTIAL, 1),
    FULL(StringKeyMatch.MANGLIK_FULL, 2),
    DOUBLE(StringKeyMatch.MANGLIK_DOUBLE, 3);

    val displayName: String get() = stringKey.en

    fun getLocalizedName(language: Language): String {
        return StringResources.get(stringKey, language)
    }
}

// ============================================
// Compatibility Rating
// ============================================

/**
 * Overall compatibility rating based on total Guna score
 */
enum class CompatibilityRating(
    val stringKey: StringKeyInterface,
    val descKey: StringKeyInterface
) {
    EXCELLENT(StringKeyMatch.COMPAT_EXCELLENT, StringKeyMatch.COMPAT_EXCELLENT_DESC),
    GOOD(StringKeyMatch.COMPAT_GOOD, StringKeyMatch.COMPAT_GOOD_DESC),
    AVERAGE(StringKeyMatch.COMPAT_AVERAGE, StringKeyMatch.COMPAT_AVERAGE_DESC),
    BELOW_AVERAGE(StringKeyMatch.COMPAT_BELOW_AVERAGE, StringKeyMatch.COMPAT_BELOW_AVERAGE_DESC),
    POOR(StringKeyMatch.COMPAT_POOR, StringKeyMatch.COMPAT_POOR_DESC);

    val displayName: String get() = stringKey.en
    val description: String get() = descKey.en

    fun getLocalizedName(language: Language): String {
        return StringResources.get(stringKey, language)
    }

    fun getLocalizedDescription(language: Language): String {
        return StringResources.get(descKey, language)
    }

    companion object {
        const val EXCELLENT_THRESHOLD = MatchmakingConstants.EXCELLENT_THRESHOLD
        const val GOOD_THRESHOLD = MatchmakingConstants.GOOD_THRESHOLD
        const val AVERAGE_THRESHOLD = MatchmakingConstants.AVERAGE_THRESHOLD
        const val POOR_THRESHOLD = MatchmakingConstants.POOR_THRESHOLD

        fun fromScore(score: Double, nadiScore: Double = 8.0, bhakootScore: Double = 7.0): CompatibilityRating {
            // Both Nadi and Bhakoot zero is very serious
            if (nadiScore == 0.0 && bhakootScore == 0.0 && score < GOOD_THRESHOLD) {
                return POOR
            }
            return when {
                score >= EXCELLENT_THRESHOLD -> EXCELLENT
                score >= GOOD_THRESHOLD -> GOOD
                score >= AVERAGE_THRESHOLD -> AVERAGE
                score >= POOR_THRESHOLD -> BELOW_AVERAGE
                else -> POOR
            }
        }
    }
}

// ============================================
// Data Classes for Analysis Results
// ============================================

/**
 * Enumeration of the 8 Gunas (Ashtakoota) for type-safe matching
 * This replaces string-based matching for better code safety
 */
enum class GunaType(val stringKey: StringKeyInterface, val maxPoints: Double) {
    VARNA(StringKeyMatch.GUNA_VARNA, 1.0),
    VASHYA(StringKeyMatch.GUNA_VASHYA, 2.0),
    TARA(StringKeyMatch.GUNA_TARA, 3.0),
    YONI(StringKeyMatch.GUNA_YONI, 4.0),
    GRAHA_MAITRI(StringKeyMatch.GUNA_GRAHA_MAITRI, 5.0),
    GANA(StringKeyMatch.GUNA_GANA, 6.0),
    BHAKOOT(StringKeyMatch.GUNA_BHAKOOT, 7.0),
    NADI(StringKeyMatch.GUNA_NADI, 8.0);

    val displayName: String get() = stringKey.en

    fun getLocalizedName(language: Language): String {
        return StringResources.get(stringKey, language)
    }

    companion object {
        /**
         * Find GunaType from name string (for backward compatibility)
         */
        fun fromName(name: String): GunaType? {
            return entries.find {
                it.displayName.equals(name, ignoreCase = true) ||
                it.name.equals(name, ignoreCase = true)
            }
        }
    }
}

/**
 * Manglik compatibility level for a pair
 */
enum class ManglikCompatibilityLevel {
    EXCELLENT, GOOD, AVERAGE, BELOW_AVERAGE, POOR;

    fun getLocalizedName(language: Language): String = when (this) {
        EXCELLENT -> StringResources.get(StringKeyDosha.MANGLIK_COMPAT_EXCELLENT, language)
        GOOD -> StringResources.get(StringKeyDosha.MANGLIK_COMPAT_GOOD, language)
        AVERAGE -> StringResources.get(StringKeyDosha.MANGLIK_COMPAT_AVERAGE, language)
        BELOW_AVERAGE -> StringResources.get(StringKeyDosha.MANGLIK_COMPAT_BELOW_AVERAGE, language)
        POOR -> StringResources.get(StringKeyDosha.MANGLIK_COMPAT_POOR, language)
    }
}

/**
 * Result of individual Guna analysis
 */
data class GunaAnalysis(
    val gunaType: GunaType,
    val name: String,
    val maxPoints: Double,
    val obtainedPoints: Double,
    val description: String,
    val brideValue: String,
    val groomValue: String,
    val analysis: String,
    val isPositive: Boolean
) {
    val percentage: Double get() = (obtainedPoints / maxPoints) * 100.0

    /**
     * Check if this is a specific Guna type (type-safe)
     */
    fun isType(type: GunaType): Boolean = gunaType == type

    /**
     * Check if this Guna has dosha (0 points)
     */
    val hasDosha: Boolean get() = obtainedPoints == 0.0
}

/**
 * Comprehensive Manglik Dosha analysis result
 */
data class ManglikAnalysis(
    val person: String,
    val dosha: ManglikDosha,
    val marsHouse: Int,
    val marsHouseFromMoon: Int = 0,
    val marsHouseFromVenus: Int = 0,
    val marsDegreeInHouse: Double = 0.0,
    val isRetrograde: Boolean = false,
    val factors: List<String>,
    val cancellations: List<String>,
    val effectiveDosha: ManglikDosha,
    val intensity: Int = 100,
    val fromLagna: Boolean = false,
    val fromMoon: Boolean = false,
    val fromVenus: Boolean = false
) {
    fun getDetailedDescription(language: Language): String {
        if (effectiveDosha == ManglikDosha.NONE) {
            return StringResources.get(StringKeyMatch.MANGLIK_NO_DOSHA_DESC, language)
        }

        return buildString {
            append("${effectiveDosha.getLocalizedName(language)} ")
            append(StringResources.get(StringKeyMatch.MANGLIK_DETECTED, language))
            if (intensity < 100) {
                append(" (${intensity}% ${StringResources.get(StringKeyMatch.MANGLIK_INTENSITY, language)})")
            }
            append(". ${StringResources.get(StringKeyMatch.MANGLIK_MARS_IN, language)} ")
            val sources = mutableListOf<String>()
            if (fromLagna) sources.add("${StringResources.get(StringKeyAnalysis.HOUSE, language)} $marsHouse ${StringResources.get(StringKeyMatch.FROM_LAGNA, language)}")
            if (fromMoon) sources.add("${StringResources.get(StringKeyAnalysis.HOUSE, language)} $marsHouseFromMoon ${StringResources.get(StringKeyMatch.FROM_MOON, language)}")
            if (fromVenus) sources.add("${StringResources.get(StringKeyAnalysis.HOUSE, language)} $marsHouseFromVenus ${StringResources.get(StringKeyMatch.FROM_VENUS, language)}")
            append(sources.joinToString(", "))
            append(".")
        }
    }
}

/**
 * Additional compatibility factors beyond Ashtakoota
 */
data class AdditionalFactors(
    val vedhaPresent: Boolean,
    val vedhaDetails: String,
    val rajjuCompatible: Boolean,
    val rajjuDetails: String,
    val brideRajju: Rajju,
    val groomRajju: Rajju,
    val brideArudha: RajjuArudha,
    val groomArudha: RajjuArudha,
    val streeDeerghaSatisfied: Boolean,
    val streeDeerghaDiff: Int,
    val mahendraSatisfied: Boolean,
    val mahendraDetails: String
)

/**
 * Complete matchmaking result
 */
data class MatchmakingResult(
    val brideChart: VedicChart,
    val groomChart: VedicChart,
    val gunaAnalyses: List<GunaAnalysis>,
    val totalPoints: Double,
    val maxPoints: Double,
    val percentage: Double,
    val rating: CompatibilityRating,
    val brideManglik: ManglikAnalysis,
    val groomManglik: ManglikAnalysis,
    val manglikCompatibilityLevel: ManglikCompatibilityLevel,
    val manglikCompatibilityRecommendation: String,
    val additionalFactors: AdditionalFactors,
    val specialConsiderations: List<String>,
    val remedies: List<String>,
    val summary: String,
    val detailedAnalysis: String,
    val timestamp: Long = System.currentTimeMillis()
) {
    val varnaScore: Double get() = gunaAnalyses.find { it.gunaType == GunaType.VARNA }?.obtainedPoints ?: 0.0
    val vashyaScore: Double get() = gunaAnalyses.find { it.gunaType == GunaType.VASHYA }?.obtainedPoints ?: 0.0
    val taraScore: Double get() = gunaAnalyses.find { it.gunaType == GunaType.TARA }?.obtainedPoints ?: 0.0
    val yoniScore: Double get() = gunaAnalyses.find { it.gunaType == GunaType.YONI }?.obtainedPoints ?: 0.0
    val grahaMaitriScore: Double get() = gunaAnalyses.find { it.gunaType == GunaType.GRAHA_MAITRI }?.obtainedPoints ?: 0.0
    val ganaScore: Double get() = gunaAnalyses.find { it.gunaType == GunaType.GANA }?.obtainedPoints ?: 0.0
    val bhakootScore: Double get() = gunaAnalyses.find { it.gunaType == GunaType.BHAKOOT }?.obtainedPoints ?: 0.0
    val nadiScore: Double get() = gunaAnalyses.find { it.gunaType == GunaType.NADI }?.obtainedPoints ?: 0.0
}

// ============================================
// Vedha Pairs (Nakshatra Obstruction)
// ============================================

/**
 * Vedha Nakshatra pairs that cause obstruction
 */
object VedhaPairs {
    val pairs: List<Pair<Nakshatra, Nakshatra>> = listOf(
        Pair(Nakshatra.ASHWINI, Nakshatra.JYESHTHA),
        Pair(Nakshatra.BHARANI, Nakshatra.ANURADHA),
        Pair(Nakshatra.KRITTIKA, Nakshatra.VISHAKHA),
        Pair(Nakshatra.ROHINI, Nakshatra.SWATI),
        Pair(Nakshatra.ARDRA, Nakshatra.SHRAVANA),
        Pair(Nakshatra.PUNARVASU, Nakshatra.UTTARA_ASHADHA),
        Pair(Nakshatra.PUSHYA, Nakshatra.PURVA_ASHADHA),
        Pair(Nakshatra.ASHLESHA, Nakshatra.MULA),
        Pair(Nakshatra.MAGHA, Nakshatra.REVATI),
        Pair(Nakshatra.PURVA_PHALGUNI, Nakshatra.UTTARA_BHADRAPADA),
        Pair(Nakshatra.UTTARA_PHALGUNI, Nakshatra.PURVA_BHADRAPADA),
        Pair(Nakshatra.HASTA, Nakshatra.SHATABHISHA),
        Pair(Nakshatra.CHITRA, Nakshatra.DHANISHTHA),
        Pair(Nakshatra.MRIGASHIRA, Nakshatra.DHANISHTHA)
    )

    fun hasVedha(nakshatra1: Nakshatra, nakshatra2: Nakshatra): Boolean {
        return pairs.any { (star1, star2) ->
            (nakshatra1 == star1 && nakshatra2 == star2) ||
            (nakshatra1 == star2 && nakshatra2 == star1)
        }
    }
}

// ============================================
// Scoring Constants
// ============================================

object MatchmakingConstants {
    const val MAX_VARNA = 1.0
    const val MAX_VASHYA = 2.0
    const val MAX_TARA = 3.0
    const val MAX_YONI = 4.0
    const val MAX_GRAHA_MAITRI = 5.0
    const val MAX_GANA = 6.0
    const val MAX_BHAKOOT = 7.0
    const val MAX_NADI = 8.0
    const val MAX_TOTAL = 36.0

    // Compatibility rating thresholds (based on guna points out of 36)
    const val EXCELLENT_THRESHOLD = 31.0  // 31+ points
    const val GOOD_THRESHOLD = 26.0       // 26-30 points
    const val AVERAGE_THRESHOLD = 21.0    // 21-25 points
    const val POOR_THRESHOLD = 16.0       // 16-20 points
    // Below 16 is BELOW_AVERAGE

    /** Manglik houses from Lagna: 1, 2, 4, 7, 8, 12 */
    val MANGLIK_HOUSES = listOf(1, 2, 4, 7, 8, 12)

    /** Most severe Manglik houses */
    val SEVERE_MANGLIK_HOUSES = listOf(7, 8)

    /** Moderate Manglik houses */
    val MODERATE_MANGLIK_HOUSES = listOf(1, 4, 12)

    /** Mild Manglik houses */
    val MILD_MANGLIK_HOUSES = listOf(2)

    /** Mahendra favorable positions */
    val MAHENDRA_POSITIONS = listOf(4, 7, 10, 13, 16, 19, 22, 25)
}


