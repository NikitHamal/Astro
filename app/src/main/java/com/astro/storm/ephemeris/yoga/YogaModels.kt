package com.astro.storm.ephemeris.yoga

import com.astro.storm.core.common.Language
import com.astro.storm.core.common.StringKeyInterface
import com.astro.storm.core.common.StringKeyMatch
import com.astro.storm.core.common.StringKeyYogaExpanded
import com.astro.storm.core.common.StringResources
import com.astro.storm.core.model.Planet
import com.astro.storm.core.model.VedicChart

/**
 * Yoga Models - Data classes for Yoga calculations
 *
 * Contains all data structures used across yoga evaluators:
 * - YogaCategory: Classification of yoga types
 * - YogaStrength: Strength levels for yogas
 * - Yoga: Complete yoga information
 * - YogaAnalysis: Full analysis result
 *
 * Based on classical texts:
 * - Brihat Parasara Hora Shastra (BPHS)
 * - Phaladeepika
 * - Saravali
 * - Jataka Parijata
 *
 * @author AstroStorm
 */

/**
 * Yoga category enumeration
 */
enum class YogaCategory(val displayName: String, val description: String) {
    RAJA_YOGA("Raja Yoga", "Power, authority, and leadership combinations"),
    DHANA_YOGA("Dhana Yoga", "Wealth and prosperity combinations"),
    MAHAPURUSHA_YOGA("Pancha Mahapurusha Yoga", "Five great person combinations"),
    NABHASA_YOGA("Nabhasa Yoga", "Pattern-based planetary combinations"),
    CHANDRA_YOGA("Chandra Yoga", "Moon-based combinations"),
    SOLAR_YOGA("Solar Yoga", "Sun-based combinations"),
    NEGATIVE_YOGA("Negative Yoga", "Challenging combinations"),
    SPECIAL_YOGA("Special Yoga", "Other significant combinations"),
    BHAVA_YOGA("Bhava Yoga", "House Lord Placements"),
    CONJUNCTION_YOGA("Conjunction Yoga", "Planetary Conjunctions");

    /**
     * Get localized display name
     */
    fun getLocalizedName(language: Language): String {
        val key: StringKeyInterface = when (this) {
            RAJA_YOGA -> StringKeyMatch.YOGA_CAT_RAJA
            DHANA_YOGA -> StringKeyMatch.YOGA_CAT_DHANA
            MAHAPURUSHA_YOGA -> StringKeyMatch.YOGA_CAT_PANCHA_MAHAPURUSHA
            NABHASA_YOGA -> StringKeyMatch.YOGA_CAT_NABHASA
            CHANDRA_YOGA -> StringKeyMatch.YOGA_CAT_CHANDRA
            SOLAR_YOGA -> StringKeyMatch.YOGA_CAT_SOLAR
            NEGATIVE_YOGA -> StringKeyMatch.YOGA_CAT_NEGATIVE
            SPECIAL_YOGA -> StringKeyMatch.YOGA_CAT_SPECIAL
            BHAVA_YOGA -> StringKeyYogaExpanded.YOGA_CAT_BHAVA
            CONJUNCTION_YOGA -> StringKeyYogaExpanded.YOGA_CAT_CONJUNCTION
        }
        return StringResources.get(key, language)
    }

    /**
     * Get localized description
     */
    fun getLocalizedDescription(language: Language): String {
        val key: StringKeyInterface = when (this) {
            RAJA_YOGA -> StringKeyMatch.YOGA_CAT_RAJA_DESC
            DHANA_YOGA -> StringKeyMatch.YOGA_CAT_DHANA_DESC
            MAHAPURUSHA_YOGA -> StringKeyMatch.YOGA_CAT_PANCHA_MAHAPURUSHA_DESC
            NABHASA_YOGA -> StringKeyMatch.YOGA_CAT_NABHASA_DESC
            CHANDRA_YOGA -> StringKeyMatch.YOGA_CAT_CHANDRA_DESC
            SOLAR_YOGA -> StringKeyMatch.YOGA_CAT_SOLAR_DESC
            NEGATIVE_YOGA -> StringKeyMatch.YOGA_CAT_NEGATIVE_DESC
            SPECIAL_YOGA -> StringKeyMatch.YOGA_CAT_SPECIAL_DESC
            BHAVA_YOGA -> StringKeyYogaExpanded.YOGA_CAT_BHAVA_DESC
            CONJUNCTION_YOGA -> StringKeyYogaExpanded.YOGA_CAT_CONJUNCTION_DESC
        }
        return StringResources.get(key, language)
    }
}

/**
 * Yoga strength level
 */
enum class YogaStrength(val displayName: String, val value: Int) {
    EXTREMELY_STRONG("Extremely Strong", 6),
    VERY_STRONG("Very Strong", 5),
    STRONG("Strong", 4),
    MODERATE("Moderate", 3),
    WEAK("Weak", 2),
    VERY_WEAK("Very Weak", 1);

    /**
     * Get localized display name
     */
    fun getLocalizedName(language: Language): String {
        val key = when (this) {
            EXTREMELY_STRONG -> StringKeyMatch.YOGA_STRENGTH_EXTREMELY_STRONG
            VERY_STRONG -> StringKeyMatch.YOGA_STRENGTH_VERY_STRONG
            STRONG -> StringKeyMatch.YOGA_STRENGTH_STRONG
            MODERATE -> StringKeyMatch.YOGA_STRENGTH_MODERATE
            WEAK -> StringKeyMatch.YOGA_STRENGTH_WEAK
            VERY_WEAK -> StringKeyMatch.YOGA_STRENGTH_VERY_WEAK
        }
        return StringResources.get(key, language)
    }

    companion object {
        /**
         * Get strength enum from percentage value
         */
        fun fromPercentage(percentage: Double): YogaStrength {
            return when {
                percentage >= 85 -> EXTREMELY_STRONG
                percentage >= 75 -> VERY_STRONG
                percentage >= 60 -> STRONG
                percentage >= 50 -> MODERATE
                percentage >= 30 -> WEAK
                else -> VERY_WEAK
            }
        }
    }
}

/**
 * Complete Yoga data class
 */
data class Yoga(
    val name: String,
    val sanskritName: String,
    val category: YogaCategory,
    val planets: List<Planet>,
    val houses: List<Int>,
    val description: String,
    val effects: String,
    val strength: YogaStrength,
    val strengthPercentage: Double,
    val isAuspicious: Boolean,
    val activationPeriod: String,
    val cancellationFactors: List<String>
)

/**
 * Complete Yoga analysis result
 */
data class YogaAnalysis(
    val chart: VedicChart,
    val allYogas: List<Yoga>,
    val rajaYogas: List<Yoga>,
    val dhanaYogas: List<Yoga>,
    val mahapurushaYogas: List<Yoga>,
    val nabhasaYogas: List<Yoga>,
    val chandraYogas: List<Yoga>,
    val solarYogas: List<Yoga>,
    val negativeYogas: List<Yoga>,
    val specialYogas: List<Yoga>,
    val bhavaYogas: List<Yoga> = emptyList(), // Default for compatibility
    val conjunctionYogas: List<Yoga> = emptyList(), // Default for compatibility
    val dominantYogaCategory: YogaCategory,
    val overallYogaStrength: Double,
    val timestamp: Long = System.currentTimeMillis()
) {
    /**
     * Generate plain text report of yoga analysis
     */
    fun toPlainText(language: Language = Language.ENGLISH): String = buildString {
        val reportTitle = StringResources.get(StringKeyMatch.REPORT_YOGA_ANALYSIS, language)
        val totalYogasLabel = StringResources.get(StringKeyMatch.REPORT_TOTAL_YOGAS, language)
        val overallStrengthLabel = StringResources.get(StringKeyMatch.REPORT_OVERALL_STRENGTH, language)
        val dominantCategoryLabel = StringResources.get(StringKeyMatch.REPORT_DOMINANT_CATEGORY, language)
        val planetsLabel = StringResources.get(StringKeyMatch.REPORT_PLANETS, language)
        val housesLabel = StringResources.get(StringKeyMatch.REPORT_HOUSES, language)
        val strengthLabel = StringResources.get(StringKeyMatch.TAB_STRENGTH, language)
        val effectsLabel = StringResources.get(StringKeyMatch.REPORT_EFFECTS, language)
        val activationLabel = StringResources.get(StringKeyMatch.REPORT_ACTIVATION, language)
        val patternLabel = StringResources.get(StringKeyMatch.REPORT_PATTERN, language)
        val cancellationLabel = StringResources.get(StringKeyMatch.REPORT_CANCELLATION_FACTORS, language)
        val auspiciousText = StringResources.get(StringKeyMatch.REPORT_AUSPICIOUS, language)
        val inauspiciousText = StringResources.get(StringKeyMatch.REPORT_INAUSPICIOUS, language)

        appendLine("═══════════════════════════════════════════════════════════")
        appendLine("                    $reportTitle")
        appendLine("═══════════════════════════════════════════════════════════")
        appendLine()
        appendLine("$totalYogasLabel: ${allYogas.size}")
        appendLine("$overallStrengthLabel: ${String.format("%.1f", overallYogaStrength)}%")
        appendLine("$dominantCategoryLabel: ${dominantYogaCategory.getLocalizedName(language)}")
        appendLine()

        if (mahapurushaYogas.isNotEmpty()) {
            appendLine(YogaCategory.MAHAPURUSHA_YOGA.getLocalizedName(language).uppercase())
            appendLine("─────────────────────────────────────────────────────────")
            mahapurushaYogas.forEach { yoga ->
                val localizedName = YogaLocalization.getLocalizedYogaName(yoga.name, language)
                val localizedSanskrit = YogaLocalization.getLocalizedYogaSanskritName(yoga.name, language)
                val localizedEffects = YogaLocalization.getLocalizedYogaEffects(yoga.name, language).ifEmpty { yoga.effects }
                appendLine("★ $localizedName ($localizedSanskrit)")
                appendLine("  $planetsLabel: ${yoga.planets.joinToString { it.getLocalizedName(language) }}")
                appendLine("  $strengthLabel: ${yoga.strength.getLocalizedName(language)} (${String.format("%.0f", yoga.strengthPercentage)}%)")
                appendLine("  $effectsLabel: $localizedEffects")
                appendLine()
            }
        }

        if (rajaYogas.isNotEmpty()) {
            appendLine(YogaCategory.RAJA_YOGA.getLocalizedName(language).uppercase())
            appendLine("─────────────────────────────────────────────────────────")
            rajaYogas.forEach { yoga ->
                val localizedName = YogaLocalization.getLocalizedYogaName(yoga.name, language)
                val localizedSanskrit = YogaLocalization.getLocalizedYogaSanskritName(yoga.name, language)
                val localizedEffects = YogaLocalization.getLocalizedYogaEffects(yoga.name, language).ifEmpty { yoga.effects }
                appendLine("★ $localizedName ($localizedSanskrit)")
                appendLine("  $planetsLabel: ${yoga.planets.joinToString { it.getLocalizedName(language) }}")
                appendLine("  $housesLabel: ${yoga.houses.joinToString()}")
                appendLine("  $strengthLabel: ${yoga.strength.getLocalizedName(language)} (${String.format("%.0f", yoga.strengthPercentage)}%)")
                appendLine("  $effectsLabel: $localizedEffects")
                appendLine("  $activationLabel: ${yoga.activationPeriod}")
                appendLine()
            }
        }

        if (dhanaYogas.isNotEmpty()) {
            appendLine(YogaCategory.DHANA_YOGA.getLocalizedName(language).uppercase())
            appendLine("─────────────────────────────────────────────────────────")
            dhanaYogas.forEach { yoga ->
                val localizedName = YogaLocalization.getLocalizedYogaName(yoga.name, language)
                val localizedSanskrit = YogaLocalization.getLocalizedYogaSanskritName(yoga.name, language)
                val localizedEffects = YogaLocalization.getLocalizedYogaEffects(yoga.name, language).ifEmpty { yoga.effects }
                appendLine("★ $localizedName ($localizedSanskrit)")
                appendLine("  $planetsLabel: ${yoga.planets.joinToString { it.getLocalizedName(language) }}")
                appendLine("  $strengthLabel: ${yoga.strength.getLocalizedName(language)}")
                appendLine("  $effectsLabel: $localizedEffects")
                appendLine()
            }
        }

        if (chandraYogas.isNotEmpty()) {
            appendLine(YogaCategory.CHANDRA_YOGA.getLocalizedName(language).uppercase())
            appendLine("─────────────────────────────────────────────────────────")
            chandraYogas.forEach { yoga ->
                val localizedName = YogaLocalization.getLocalizedYogaName(yoga.name, language)
                val localizedEffects = YogaLocalization.getLocalizedYogaEffects(yoga.name, language).ifEmpty { yoga.effects }
                val auspicious = if (yoga.isAuspicious) auspiciousText else inauspiciousText
                appendLine("★ $localizedName - $auspicious")
                appendLine("  $effectsLabel: $localizedEffects")
                appendLine()
            }
        }

        if (solarYogas.isNotEmpty()) {
            appendLine(YogaCategory.SOLAR_YOGA.getLocalizedName(language).uppercase())
            appendLine("─────────────────────────────────────────────────────────")
            solarYogas.forEach { yoga ->
                val localizedName = YogaLocalization.getLocalizedYogaName(yoga.name, language)
                val localizedSanskrit = YogaLocalization.getLocalizedYogaSanskritName(yoga.name, language)
                val localizedEffects = YogaLocalization.getLocalizedYogaEffects(yoga.name, language).ifEmpty { yoga.effects }
                appendLine("★ $localizedName ($localizedSanskrit)")
                appendLine("  $effectsLabel: $localizedEffects")
                appendLine()
            }
        }

        if (nabhasaYogas.isNotEmpty()) {
            appendLine(YogaCategory.NABHASA_YOGA.getLocalizedName(language).uppercase())
            appendLine("─────────────────────────────────────────────────────────")
            nabhasaYogas.forEach { yoga ->
                val localizedName = YogaLocalization.getLocalizedYogaName(yoga.name, language)
                val localizedSanskrit = YogaLocalization.getLocalizedYogaSanskritName(yoga.name, language)
                val localizedEffects = YogaLocalization.getLocalizedYogaEffects(yoga.name, language).ifEmpty { yoga.effects }
                appendLine("★ $localizedName ($localizedSanskrit)")
                appendLine("  $patternLabel: ${yoga.description}")
                appendLine("  $effectsLabel: $localizedEffects")
                appendLine()
            }
        }

        if (negativeYogas.isNotEmpty()) {
            appendLine(YogaCategory.NEGATIVE_YOGA.getLocalizedName(language).uppercase())
            appendLine("─────────────────────────────────────────────────────────")
            negativeYogas.forEach { yoga ->
                val localizedName = YogaLocalization.getLocalizedYogaName(yoga.name, language)
                val localizedSanskrit = YogaLocalization.getLocalizedYogaSanskritName(yoga.name, language)
                val localizedEffects = YogaLocalization.getLocalizedYogaEffects(yoga.name, language).ifEmpty { yoga.effects }
                appendLine("⚠ $localizedName ($localizedSanskrit)")
                appendLine("  $effectsLabel: $localizedEffects")
                if (yoga.cancellationFactors.isNotEmpty()) {
                    appendLine("  $cancellationLabel: ${yoga.cancellationFactors.joinToString("; ")}")
                }
                appendLine()
            }
        }
        
        // New Conjunction Yogas section
        if (conjunctionYogas.isNotEmpty()) {
            appendLine(YogaCategory.CONJUNCTION_YOGA.getLocalizedName(language).uppercase())
            appendLine("─────────────────────────────────────────────────────────")
            conjunctionYogas.forEach { yoga ->
                // Basic localization or fallback
                val localizedName = yoga.name // Already somewhat localized or generic
                appendLine("★ $localizedName")
                appendLine("  $planetsLabel: ${yoga.planets.joinToString { it.getLocalizedName(language) }}")
                appendLine("  $effectsLabel: ${yoga.effects}")
                appendLine()
            }
        }
        
        // New Bhava Yogas section
        if (bhavaYogas.isNotEmpty()) {
            appendLine(YogaCategory.BHAVA_YOGA.getLocalizedName(language).uppercase())
            appendLine("─────────────────────────────────────────────────────────")
            bhavaYogas.forEach { yoga ->
                // Bhava yogas are numerous, keep concise
                appendLine("★ ${yoga.name}")
                appendLine("  $effectsLabel: ${yoga.effects}")
                appendLine()
            }
        }

        if (specialYogas.isNotEmpty()) {
            appendLine(YogaCategory.SPECIAL_YOGA.getLocalizedName(language).uppercase())
            appendLine("─────────────────────────────────────────────────────────")
            specialYogas.forEach { yoga ->
                val localizedName = YogaLocalization.getLocalizedYogaName(yoga.name, language)
                val localizedEffects = YogaLocalization.getLocalizedYogaEffects(yoga.name, language).ifEmpty { yoga.effects }
                appendLine("★ $localizedName")
                appendLine("  $effectsLabel: $localizedEffects")
                appendLine()
            }
        }
    }
}

