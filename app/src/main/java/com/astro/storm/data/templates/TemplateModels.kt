package com.astro.storm.data.templates

import com.astro.storm.core.common.Language
import com.astro.storm.core.model.Planet
import com.astro.storm.core.model.ZodiacSign
import com.astro.storm.core.model.Nakshatra

/**
 * Core template models for AstroStorm's comprehensive prediction system.
 * These models support 5,000+ localized templates for offline Vedic astrological predictions.
 */

/**
 * Represents a localized prediction template with English and Nepali translations.
 */
data class LocalizedTemplate(
    val en: String,
    val ne: String
) {
    fun get(language: Language): String = when (language) {
        Language.ENGLISH -> en
        Language.NEPALI -> ne
    }
}

/**
 * Degree range for precision template matching.
 * Supports sign-based (0-30), decanate-based (0-10, 10-20, 20-30),
 * and Nakshatra-pada-based precision.
 */
data class DegreeRange(
    val minDegree: Double,
    val maxDegree: Double
) {
    fun contains(degree: Double): Boolean = degree >= minDegree && degree < maxDegree

    companion object {
        val FULL_SIGN = DegreeRange(0.0, 30.0)
        val FIRST_DECANATE = DegreeRange(0.0, 10.0)
        val SECOND_DECANATE = DegreeRange(10.0, 20.0)
        val THIRD_DECANATE = DegreeRange(20.0, 30.0)

        // Nakshatra padas (3.333° each)
        fun padaRange(pada: Int): DegreeRange {
            val padaSpan = 30.0 / 9.0  // ~3.333 degrees
            val start = (pada - 1) * padaSpan
            return DegreeRange(start, start + padaSpan)
        }
    }
}

/**
 * Planetary dignity levels for template selection.
 */
enum class Dignity {
    EXALTED,           // Uchcha
    OWN_SIGN,          // Swakshetra
    MOOL_TRIKONA,      // Mooltrikona
    FRIENDLY,          // Mitra Kshetra
    NEUTRAL,           // Sama Kshetra
    ENEMY,             // Shatru Kshetra
    DEBILITATED,       // Neecha
    COMBUST,           // Asta
    RETROGRADE         // Vakri
}

/**
 * House positions (Bhavas) 1-12.
 */
enum class House(val number: Int, val nameEn: String, val nameNe: String) {
    FIRST(1, "First House (Lagna)", "पहिलो भाव (लग्न)"),
    SECOND(2, "Second House", "दोस्रो भाव"),
    THIRD(3, "Third House", "तेस्रो भाव"),
    FOURTH(4, "Fourth House", "चौथो भाव"),
    FIFTH(5, "Fifth House", "पाँचौ भाव"),
    SIXTH(6, "Sixth House", "छैटौं भाव"),
    SEVENTH(7, "Seventh House", "सातौं भाव"),
    EIGHTH(8, "Eighth House", "आठौं भाव"),
    NINTH(9, "Ninth House", "नवौं भाव"),
    TENTH(10, "Tenth House", "दशौं भाव"),
    ELEVENTH(11, "Eleventh House", "एघारौं भाव"),
    TWELFTH(12, "Twelfth House", "बाह्रौं भाव");

    companion object {
        fun fromNumber(num: Int): House = entries.first { it.number == num }
    }
}

/**
 * Strength levels for template categorization.
 */
enum class StrengthLevel {
    EXCELLENT,      // > 6 Rupas
    VERY_STRONG,    // 5-6 Rupas
    STRONG,         // 4-5 Rupas
    MODERATE,       // 3-4 Rupas
    WEAK,           // 2-3 Rupas
    VERY_WEAK,      // 1-2 Rupas
    AFFLICTED       // < 1 Rupa
}

/**
 * Template condition for rule-based selection.
 */
data class TemplateCondition(
    val planet: Planet? = null,
    val sign: ZodiacSign? = null,
    val nakshatra: Nakshatra? = null,
    val house: House? = null,
    val degreeRange: DegreeRange? = null,
    val dignity: Dignity? = null,
    val strengthLevel: StrengthLevel? = null,
    val aspects: List<Planet>? = null,
    val conjunctions: List<Planet>? = null,
    val isRetrograde: Boolean? = null,
    val isCombust: Boolean? = null
)

/**
 * Complete prediction template with conditions and localized content.
 */
data class PredictionTemplate(
    val id: String,
    val category: TemplateCategory,
    val condition: TemplateCondition,
    val content: LocalizedTemplate,
    val priority: Int = 0,
    val confidence: ConfidenceLevel = ConfidenceLevel.MEDIUM
)

/**
 * Template categories for organization.
 */
enum class TemplateCategory {
    // Dasha Templates
    MAHADASHA_GENERAL,
    MAHADASHA_BY_SIGN,
    MAHADASHA_BY_HOUSE,
    MAHADASHA_BY_DIGNITY,
    ANTARDASHA_GENERAL,
    ANTARDASHA_BY_SIGN,
    PRATYANTARDASHA,

    // Transit Templates
    TRANSIT_GENERAL,
    TRANSIT_BY_HOUSE,
    TRANSIT_ASPECT,
    TRANSIT_CONJUNCTION,
    TRANSIT_RETROGRADE,
    GOCHARA_VEDHA,
    SADE_SATI,

    // Yoga Templates
    RAJA_YOGA,
    DHANA_YOGA,
    MAHAPURUSHA_YOGA,
    NABHASA_YOGA,
    ARISHTA_YOGA,
    VIPARITA_RAJA_YOGA,
    KEMADRUMA_YOGA,
    GAJAKESARI_YOGA,

    // House Lord Templates
    HOUSE_LORD_PLACEMENT,
    HOUSE_LORD_ASPECT,
    HOUSE_LORD_CONJUNCTION,
    KARAKA_PLACEMENT,

    // Divisional Chart Templates
    NAVAMSA_D9,
    DASHAMSA_D10,
    SAPTAMSA_D7,
    DWADASAMSA_D12,
    HORA_D2,
    DREKKANA_D3,
    SHODASAMSA_D16,
    VIMSHAMSA_D20,
    CHATURVIMSHAMSA_D24,
    SAPTAVIMSHAMSA_D27,
    TRIMSAMSA_D30,
    KHAVEDAMSA_D40,
    AKSHAVEDAMSA_D45,
    SHASHTIAMSA_D60,

    // Nadi Templates
    NADI_GENERAL,
    NADI_BY_ASCENDANT,
    NADI_RECTIFICATION,

    // Life Area Templates
    CAREER_GENERAL,
    CAREER_BY_HOUSE,
    RELATIONSHIP_GENERAL,
    RELATIONSHIP_BY_HOUSE,
    HEALTH_GENERAL,
    HEALTH_BY_HOUSE,
    WEALTH_GENERAL,
    WEALTH_BY_HOUSE,
    EDUCATION_GENERAL,
    EDUCATION_BY_HOUSE,
    SPIRITUAL_GENERAL,
    SPIRITUAL_BY_HOUSE,

    // Planetary Templates
    PLANET_IN_SIGN,
    PLANET_IN_HOUSE,
    PLANET_IN_NAKSHATRA,
    PLANET_ASPECT,
    PLANET_CONJUNCTION,
    PLANET_DIGNITY,

    // Remedy Templates
    REMEDY_MANTRA,
    REMEDY_GEMSTONE,
    REMEDY_DONATION,
    REMEDY_FASTING,
    REMEDY_RITUAL
}

/**
 * Confidence level for predictions.
 */
enum class ConfidenceLevel {
    HIGH,       // Multiple confirming factors
    MEDIUM,     // Single strong factor
    LOW         // Weak or contradicting factors
}

/**
 * Nadi Amsha (D-150) information for traditional predictions.
 */
data class NadiInfo(
    val nadiNumber: Int,          // 1-150
    val nadiNameSanskrit: String,
    val nadiNameEnglish: String,
    val nadiNameNepali: String,
    val rulingPlanet: Planet,
    val generalPrediction: LocalizedTemplate
)

/**
 * Life area enumeration for comprehensive predictions.
 */
enum class LifeAreaType {
    CAREER,
    RELATIONSHIP,
    HEALTH,
    WEALTH,
    EDUCATION,
    SPIRITUAL,
    FAMILY,
    CHILDREN,
    PROPERTY,
    TRAVEL,
    LEGAL,
    GOVERNMENT
}
