package com.astro.storm.ephemeris.remedy

import com.astro.storm.core.common.*
import com.astro.storm.core.model.*
import java.util.UUID

enum class PlanetaryStrength(val displayName: String, val severity: Int) {
    VERY_STRONG("Very Strong", 0),
    STRONG("Strong", 1),
    MODERATE("Moderate", 2),
    WEAK("Weak", 3),
    VERY_WEAK("Very Weak", 4),
    AFFLICTED("Afflicted", 5);

    fun getLocalizedName(language: Language): String {
        val key = when (this) {
            VERY_STRONG -> StringKeyUIPart1.STRENGTH_VERY_STRONG
            STRONG -> StringKeyUIPart1.STRENGTH_STRONG
            MODERATE -> StringKeyUIPart1.STRENGTH_MODERATE
            WEAK -> StringKeyUIPart1.STRENGTH_WEAK
            VERY_WEAK -> StringKeyUIPart1.STRENGTH_VERY_WEAK
            AFFLICTED -> StringKeyUIPart1.STRENGTH_AFFLICTED
        }
        return StringResources.get(key, language)
    }
}

enum class RemedyCategory(val displayName: String) {
    GEMSTONE("Gemstone"),
    MANTRA("Mantra"),
    YANTRA("Yantra"),
    CHARITY("Charity"),
    FASTING("Fasting"),
    COLOR("Color Therapy"),
    METAL("Metal"),
    RUDRAKSHA("Rudraksha"),
    DEITY("Deity Worship"),
    LIFESTYLE("Lifestyle");

    fun getLocalizedName(language: Language): String {
        val key = when (this) {
            GEMSTONE -> StringKeyGeneralPart2.CAT_GEMSTONE
            MANTRA -> StringKeyGeneralPart2.CAT_MANTRA
            YANTRA -> StringKeyGeneralPart2.CAT_YANTRA
            CHARITY -> StringKeyGeneralPart2.CAT_CHARITY
            FASTING -> StringKeyGeneralPart2.CAT_FASTING
            COLOR -> StringKeyGeneralPart2.CAT_COLOR
            METAL -> StringKeyGeneralPart2.CAT_METAL
            RUDRAKSHA -> StringKeyGeneralPart2.CAT_RUDRAKSHA
            DEITY -> StringKeyGeneralPart2.CAT_DEITY
            LIFESTYLE -> StringKeyGeneralPart2.CAT_LIFESTYLE
        }
        return StringResources.get(key, language)
    }
}

enum class RemedyPriority(val displayName: String, val level: Int) {
    ESSENTIAL("Essential", 1),
    HIGHLY_RECOMMENDED("Highly Recommended", 2),
    RECOMMENDED("Recommended", 3),
    OPTIONAL("Optional", 4);

    fun getLocalizedName(language: Language): String {
        val key = when (this) {
            ESSENTIAL -> StringKeyGeneralPart9.PRIORITY_ESSENTIAL
            HIGHLY_RECOMMENDED -> StringKeyGeneralPart9.PRIORITY_HIGHLY_RECOMMENDED
            RECOMMENDED -> StringKeyGeneralPart9.PRIORITY_RECOMMENDED
            OPTIONAL -> StringKeyGeneralPart9.PRIORITY_OPTIONAL
        }
        return StringResources.get(key, language)
    }
}

data class Remedy(
    val id: String = UUID.randomUUID().toString(),
    val category: RemedyCategory,
    val title: String,
    val description: String,
    val method: String,
    val timing: String,
    val duration: String,
    val planet: Planet?,
    val priority: RemedyPriority,
    val benefits: List<String>,
    val cautions: List<String>,
    val mantraText: String? = null,
    val mantraSanskrit: String? = null,
    val mantraCount: Int? = null,
    val alternativeGemstone: String? = null,
    val nakshatraSpecific: Boolean = false
)

data class PlanetaryAnalysis(
    val planet: Planet,
    val strength: PlanetaryStrength,
    val strengthScore: Int,
    val issues: List<String>,
    val positives: List<String>,
    val needsRemedy: Boolean,
    val housePosition: Int,
    val sign: ZodiacSign,
    val nakshatra: Nakshatra,
    val nakshatraPada: Int,
    val longitude: Double,
    val isRetrograde: Boolean,
    val isCombust: Boolean,
    val isDebilitated: Boolean,
    val isExalted: Boolean,
    val isOwnSign: Boolean,
    val isMooltrikona: Boolean,
    val isFriendlySign: Boolean,
    val isEnemySign: Boolean,
    val isNeutralSign: Boolean,
    val hasNeechaBhangaRajaYoga: Boolean,
    val isInGandanta: Boolean,
    val isInMrityuBhaga: Boolean,
    val isInPushkarNavamsha: Boolean,
    val isFunctionalBenefic: Boolean,
    val isFunctionalMalefic: Boolean,
    val isYogakaraka: Boolean,
    val aspectingPlanets: List<Planet>,
    val aspectedByBenefics: Boolean,
    val aspectedByMalefics: Boolean,
    val shadbalaStrength: Double,
    val dignityDescription: String
)

data class RemediesResult(
    val chart: VedicChart,
    val planetaryAnalyses: List<PlanetaryAnalysis>,
    val weakestPlanets: List<Planet>,
    val remedies: List<Remedy>,
    val generalRecommendations: List<String>,
    val dashaRemedies: List<Remedy>,
    val lifeAreaFocus: Map<LifeArea, List<Remedy>>,
    val prioritizedRemedies: List<Remedy>,
    val summary: String,
    val ascendantSign: ZodiacSign,
    val moonSign: ZodiacSign,
    val timestamp: Long = System.currentTimeMillis()
) {
    val totalRemediesCount: Int get() = remedies.size
    val essentialRemediesCount: Int get() = remedies.count { it.priority == RemedyPriority.ESSENTIAL }

    fun toPlainText(language: Language = Language.ENGLISH): String = buildString {
        val reportTitle = StringResources.get(StringKeyReport.REPORT_REMEDIES, language)
        val strengthAnalysisTitle = StringResources.get(StringKeyReport.REPORT_PLANETARY_STRENGTH_ANALYSIS, language)
        val planetsAttentionTitle = StringResources.get(StringKeyReport.REPORT_PLANETS_REQUIRING_ATTENTION, language)
        val recommendedRemediesTitle = StringResources.get(StringKeyReport.REPORT_RECOMMENDED_REMEDIES, language)
        val generalRecommendationsTitle = StringResources.get(StringKeyReport.REPORT_GENERAL_RECOMMENDATIONS, language)
        val summaryTitle = StringResources.get(StringKeyReport.REPORT_SUMMARY, language)
        val generatedBy = StringResources.get(StringKeyReport.REPORT_GENERATED_BY, language)
        val nameLabel = StringResources.get(StringKeyReport.REPORT_NAME_LABEL, language)
        val ascendantLabel = StringResources.get(StringKeyReport.REPORT_ASCENDANT_LABEL, language)
        val moonSignLabel = StringResources.get(StringKeyReport.REPORT_MOON_SIGN_LABEL, language)
        val categoryLabel = StringResources.get(StringKeyReport.REPORT_CATEGORY, language)
        val planetLabel = StringResources.get(StringKeyReport.REPORT_PLANET, language)
        val methodLabel = StringResources.get(StringKeyReport.REPORT_METHOD, language)
        val timingLabel = StringResources.get(StringKeyReport.REPORT_TIMING, language)
        val mantraLabel = StringResources.get(StringKeyReport.REPORT_MANTRA_LABEL, language)

        appendLine("═══════════════════════════════════════════════════════════")
        appendLine("              $reportTitle")
        appendLine("═══════════════════════════════════════════════════════════")
        appendLine()
        appendLine("$nameLabel ${chart.birthData.name}")
        appendLine("$ascendantLabel ${ascendantSign.getLocalizedName(language)}")
        appendLine("$moonSignLabel ${moonSign.getLocalizedName(language)}")
        appendLine()
        appendLine("─────────────────────────────────────────────────────────")
        appendLine("                $strengthAnalysisTitle")
        appendLine("─────────────────────────────────────────────────────────")
        appendLine()
        planetaryAnalyses.forEach { analysis ->
            appendLine("${analysis.planet.getLocalizedName(language)}: ${analysis.strength.getLocalizedName(language)} (${analysis.strengthScore}%)")
            appendLine("  ${analysis.dignityDescription}")
            if (analysis.issues.isNotEmpty()) {
                analysis.issues.forEach { appendLine("  ⚠ $it") }
            }
            if (analysis.positives.isNotEmpty()) {
                analysis.positives.forEach { appendLine("  ✓ $it") }
            }
            appendLine()
        }
        if (weakestPlanets.isNotEmpty()) {
            appendLine("$planetsAttentionTitle")
            weakestPlanets.forEach { appendLine("  • ${it.getLocalizedName(language)}") }
        }
        appendLine()
        appendLine("─────────────────────────────────────────────────────────")
        appendLine("                    $recommendedRemediesTitle")
        appendLine("─────────────────────────────────────────────────────────")
        prioritizedRemedies.take(15).forEachIndexed { index, remedy ->
            appendLine()
            appendLine("${index + 1}. ${remedy.title} [${remedy.priority.getLocalizedName(language)}]")
            appendLine("   $categoryLabel: ${remedy.category.getLocalizedName(language)}")
            remedy.planet?.let { appendLine("   $planetLabel: ${it.getLocalizedName(language)}") }
            appendLine("   ${remedy.description}")
            appendLine("   $methodLabel: ${remedy.method}")
            appendLine("   $timingLabel: ${remedy.timing}")
            remedy.mantraText?.let { appendLine("   $mantraLabel $it") }
        }
        appendLine()
        appendLine("─────────────────────────────────────────────────────────")
        appendLine("                  $generalRecommendationsTitle")
        appendLine("─────────────────────────────────────────────────────────")
        generalRecommendations.forEach { appendLine("• $it") }
        appendLine()
        appendLine("─────────────────────────────────────────────────────────")
        appendLine("                        $summaryTitle")
        appendLine("─────────────────────────────────────────────────────────")
        appendLine(summary)
        appendLine()
        appendLine("═══════════════════════════════════════════════════════════")
        appendLine(generatedBy)
        appendLine("═══════════════════════════════════════════════════════════")
    }
}
