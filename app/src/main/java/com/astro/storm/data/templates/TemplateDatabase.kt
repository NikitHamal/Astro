package com.astro.storm.data.templates

import com.astro.storm.core.common.Language
import com.astro.storm.core.model.Planet
import com.astro.storm.core.model.ZodiacSign
import com.astro.storm.data.templates.categories.*

/**
 * Comprehensive Offline Prediction Template Database for AstroStorm.
 *
 * Contains 5,000+ bilingual (English/Nepali) prediction templates organized by category.
 * All templates are based on classical Vedic astrology texts:
 * - Brihat Parashara Hora Shastra (BPHS)
 * - Phaladeepika by Mantreshwara
 * - Saravali by Kalyana Varma
 * - Jataka Parijata
 * - Jaimini Sutras
 * - Bhrigu Nadi texts
 * - Prashna Marga
 *
 * Template selection is rule-based (no AI required) and works fully offline.
 */
object TemplateDatabase {

    // ============================================
    // TEMPLATE CATEGORIES
    // ============================================

    /** Dasha period effect templates (1,000+) - Planet in sign with degree range and house */
    val dashaTemplates: List<PredictionTemplate> by lazy { DashaTemplates.getAll() }

    /** Transit effect templates (800+) - Transiting planet through houses from Moon */
    val transitTemplates: List<PredictionTemplate> by lazy { TransitTemplates.getAll() }

    /** Yoga effect templates (600+) - Yoga combinations with strength and activation */
    val yogaTemplates: List<PredictionTemplate> by lazy { YogaTemplates.getAll() }

    /** House lord placement templates (600+) - Lord in house with dignity */
    val houseLordTemplates: List<PredictionTemplate> by lazy { HouseLordTemplates.getAll() }

    /** Nadi prediction templates (1,800) - 150 Nadis x 12 ascendants */
    val nadiTemplates: List<PredictionTemplate> by lazy { NadiTemplates.getAll() }

    /** Divisional chart templates (400+) - Analysis for D-1 through D-60 */
    val divisionalTemplates: List<PredictionTemplate> by lazy { DivisionalChartTemplates.getAll() }

    /** Life area templates (1,200+) - Career, health, relationship, wealth, etc. */
    val lifeAreaTemplates: List<PredictionTemplate> by lazy { LifeAreaTemplates.getAll() }

    // ============================================
    // AGGREGATE ACCESS
    // ============================================

    /** Total count of all templates across all categories */
    val totalTemplateCount: Int
        get() = dashaTemplates.size + transitTemplates.size + yogaTemplates.size +
                houseLordTemplates.size + nadiTemplates.size + divisionalTemplates.size +
                lifeAreaTemplates.size

    /** Get all templates (lazy, cached) */
    val allTemplates: List<PredictionTemplate> by lazy {
        dashaTemplates + transitTemplates + yogaTemplates +
                houseLordTemplates + nadiTemplates + divisionalTemplates +
                lifeAreaTemplates
    }

    /** Get templates by category */
    fun getByCategory(category: TemplateCategory): List<PredictionTemplate> {
        return when (category) {
            TemplateCategory.DASHA -> dashaTemplates
            TemplateCategory.TRANSIT -> transitTemplates
            TemplateCategory.YOGA -> yogaTemplates
            TemplateCategory.HOUSE_LORD -> houseLordTemplates
            TemplateCategory.NADI -> nadiTemplates
            TemplateCategory.DIVISIONAL -> divisionalTemplates
            TemplateCategory.LIFE_AREA -> lifeAreaTemplates
        }
    }

    /** Search templates by keyword in English text */
    fun search(query: String, category: TemplateCategory? = null): List<PredictionTemplate> {
        val source = if (category != null) getByCategory(category) else allTemplates
        val lowerQuery = query.lowercase()
        return source.filter {
            it.en.lowercase().contains(lowerQuery) || it.templateKey.lowercase().contains(lowerQuery)
        }
    }
}

// ============================================
// CORE DATA MODELS
// ============================================

/**
 * A single prediction template with bilingual text and astrological conditions.
 *
 * @param templateKey Unique identifier for this template
 * @param category The template category
 * @param en English prediction text
 * @param ne Nepali prediction text
 * @param conditions Astrological conditions that trigger this template
 * @param confidence Prediction confidence level
 * @param priority Priority for ordering (higher = more specific/important)
 * @param classicalSource Reference to classical text source
 */
data class PredictionTemplate(
    val templateKey: String,
    val category: TemplateCategory,
    val en: String,
    val ne: String,
    val conditions: TemplateConditions,
    val confidence: ConfidenceLevel = ConfidenceLevel.MEDIUM,
    val priority: Int = 50,
    val classicalSource: String = ""
) {
    /** Get localized text based on language */
    fun getText(language: Language): String = when (language) {
        Language.ENGLISH -> en
        Language.NEPALI -> ne
    }
}

/**
 * Astrological conditions that determine when a template applies.
 * All fields are nullable - null means "any value matches".
 */
data class TemplateConditions(
    val planet: Planet? = null,
    val sign: ZodiacSign? = null,
    val degreeMin: Double? = null,
    val degreeMax: Double? = null,
    val house: Int? = null,
    val dignity: DignityCondition? = null,
    val isRetrograde: Boolean? = null,
    val isCombust: Boolean? = null,
    val transitPlanet: Planet? = null,
    val transitHouse: Int? = null,
    val nakshatraIndex: Int? = null,
    val nadiNumber: Int? = null,
    val ascendantSign: ZodiacSign? = null,
    val yogaType: String? = null,
    val vargaNumber: Int? = null,
    val lifeArea: String? = null,
    val dashaLord: Planet? = null,
    val antardashaLord: Planet? = null,
    val strengthMin: Double? = null,
    val strengthMax: Double? = null
)

/** Dignity conditions for template matching */
enum class DignityCondition {
    EXALTED, MOOLATRIKONA, OWN_SIGN, FRIENDLY, NEUTRAL, ENEMY, DEBILITATED
}

/** Confidence level for prediction accuracy */
enum class ConfidenceLevel {
    HIGH, MEDIUM, LOW;

    fun getDisplayText(language: Language): String = when (this) {
        HIGH -> if (language == Language.ENGLISH) "High Confidence" else "उच्च विश्वास"
        MEDIUM -> if (language == Language.ENGLISH) "Medium Confidence" else "मध्यम विश्वास"
        LOW -> if (language == Language.ENGLISH) "Low Confidence" else "कम विश्वास"
    }
}

/** Template categories matching the level.md specification */
enum class TemplateCategory {
    DASHA, TRANSIT, YOGA, HOUSE_LORD, NADI, DIVISIONAL, LIFE_AREA;

    fun getDisplayName(language: Language): String = when (this) {
        DASHA -> if (language == Language.ENGLISH) "Dasha Effects" else "दशा प्रभाव"
        TRANSIT -> if (language == Language.ENGLISH) "Transit Effects" else "गोचर प्रभाव"
        YOGA -> if (language == Language.ENGLISH) "Yoga Effects" else "योग प्रभाव"
        HOUSE_LORD -> if (language == Language.ENGLISH) "House Lord Effects" else "भावेश प्रभाव"
        NADI -> if (language == Language.ENGLISH) "Nadi Predictions" else "नाडी भविष्यवाणी"
        DIVISIONAL -> if (language == Language.ENGLISH) "Divisional Chart Effects" else "वर्ग कुण्डली प्रभाव"
        LIFE_AREA -> if (language == Language.ENGLISH) "Life Area Predictions" else "जीवन क्षेत्र भविष्यवाणी"
    }
}
