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
            VERY_STRONG -> StringKeyRemedy.STRENGTH_VERY_STRONG
            STRONG -> StringKeyRemedy.STRENGTH_STRONG
            MODERATE -> StringKeyRemedy.STRENGTH_MODERATE
            WEAK -> StringKeyRemedy.STRENGTH_WEAK
            VERY_WEAK -> StringKeyRemedy.STRENGTH_VERY_WEAK
            AFFLICTED -> StringKeyRemedy.STRENGTH_AFFLICTED
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
            GEMSTONE -> StringKeyRemedy.CAT_GEMSTONE
            MANTRA -> StringKeyRemedy.CAT_MANTRA
            YANTRA -> StringKeyRemedy.CAT_YANTRA
            CHARITY -> StringKeyRemedy.CAT_CHARITY
            FASTING -> StringKeyRemedy.CAT_FASTING
            COLOR -> StringKeyRemedy.CAT_COLOR
            METAL -> StringKeyRemedy.CAT_METAL
            RUDRAKSHA -> StringKeyRemedy.CAT_RUDRAKSHA
            DEITY -> StringKeyRemedy.CAT_DEITY
            LIFESTYLE -> StringKeyRemedy.CAT_LIFESTYLE
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
            ESSENTIAL -> StringKeyRemedy.PRIORITY_ESSENTIAL
            HIGHLY_RECOMMENDED -> StringKeyRemedy.PRIORITY_HIGHLY_RECOMMENDED
            RECOMMENDED -> StringKeyRemedy.PRIORITY_RECOMMENDED
            OPTIONAL -> StringKeyRemedy.PRIORITY_OPTIONAL
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
    val ishtaDevataProfile: IshtaDevataCalculator.IshtaDevataProfile?,
    val beejaMantraProfile: BeejaMantraGenerator.BeejaMantraProfile?,
    val ascendantSign: ZodiacSign,
    val moonSign: ZodiacSign,
    val timestamp: Long = System.currentTimeMillis()
) {
    val totalRemediesCount: Int get() = remedies.size
    val essentialRemediesCount: Int get() = remedies.count { it.priority == RemedyPriority.ESSENTIAL }

    fun toPlainText(language: Language = Language.ENGLISH): String = buildString {
        val reportTitle = StringResources.get(StringKeyMatch.REPORT_REMEDIES, language)
        val strengthAnalysisTitle = StringResources.get(StringKeyMatch.REPORT_PLANETARY_STRENGTH_ANALYSIS, language)
        val planetsAttentionTitle = StringResources.get(StringKeyMatch.REPORT_PLANETS_REQUIRING_ATTENTION, language)
        val recommendedRemediesTitle = StringResources.get(StringKeyMatch.REPORT_RECOMMENDED_REMEDIES, language)
        val generalRecommendationsTitle = StringResources.get(StringKeyMatch.REPORT_GENERAL_RECOMMENDATIONS, language)
        val summaryTitle = StringResources.get(StringKeyMatch.REPORT_SUMMARY, language)
        val generatedBy = StringResources.get(StringKeyMatch.REPORT_GENERATED_BY, language)
        val nameLabel = StringResources.get(StringKeyDosha.REPORT_NAME_LABEL, language)
        val ascendantLabel = StringResources.get(StringKeyDosha.REPORT_ASCENDANT_LABEL, language)
        val moonSignLabel = StringResources.get(StringKeyMatch.REPORT_MOON_SIGN_LABEL, language)
        val categoryLabel = StringResources.get(StringKeyMatch.REPORT_CATEGORY, language)
        val planetLabel = StringResources.get(StringKeyMatch.REPORT_PLANET, language)
        val methodLabel = StringResources.get(StringKeyMatch.REPORT_METHOD, language)
        val timingLabel = StringResources.get(StringKeyMatch.REPORT_TIMING, language)
        val mantraLabel = StringResources.get(StringKeyDosha.REPORT_MANTRA_LABEL, language)

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
        ishtaDevataProfile?.let { profile ->
            appendLine("─────────────────────────────────────────────────────────")
            appendLine("                Ishta Devata & Beeja")
            appendLine("─────────────────────────────────────────────────────────")
            appendLine(profile.description)
            beejaMantraProfile?.let { beeja ->
                appendLine()
                appendLine("Beeja Akshara: ${beeja.akshara}")
                appendLine("Mantra: ${beeja.mantra}")
            }
            appendLine()
        }
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
