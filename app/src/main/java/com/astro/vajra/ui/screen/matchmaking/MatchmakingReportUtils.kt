package com.astro.vajra.ui.screen.matchmaking

import com.astro.vajra.core.common.Language
import com.astro.vajra.core.common.StringKeyMatch
import com.astro.vajra.core.common.StringKeyUICommon
import com.astro.vajra.core.common.StringKeyUIExtra
import com.astro.vajra.core.common.StringResources
import com.astro.vajra.core.model.CompatibilityRating
import com.astro.vajra.core.model.ManglikDosha
import com.astro.vajra.core.model.MatchmakingResult
import com.astro.vajra.core.model.Planet
import com.astro.vajra.core.model.VedicChart

/**
 * Utility functions for generating matchmaking reports.
 *
 * Handles text report generation for:
 * - Full detailed reports
 * - Summary reports
 * - Score-only reports
 *
 * These functions are used by the share/copy functionality in MatchmakingScreen.
 */
object MatchmakingReportUtils {

    /**
     * Generate a full detailed text report of the matchmaking analysis.
     *
     * Includes:
     * - Profile information (names, moon signs, nakshatras)
     * - Compatibility score and rating
     * - All 8 Guna analyses with details
     * - Manglik Dosha analysis
     * - Special considerations
     * - Suggested remedies
     */
    fun generateFullReport(
        result: MatchmakingResult,
        brideChart: VedicChart?,
        groomChart: VedicChart?,
        language: Language
    ): String {
        val naText = StringResources.get(StringKeyMatch.REPORT_NA, language)
        val brideLabel = StringResources.get(StringKeyMatch.REPORT_BRIDE_LABEL, language)
        val groomLabel = StringResources.get(StringKeyMatch.REPORT_GROOM_LABEL, language)
        val moonSignLabel = StringResources.get(StringKeyMatch.REPORT_MOON_SIGN_LABEL, language)
        val nakshatraLabel = StringResources.get(StringKeyMatch.REPORT_NAKSHATRA_LABEL, language)
        val cancellationText = StringResources.get(StringKeyMatch.REPORT_CANCELLATION, language)

        return buildString {
            appendLine("═══════════════════════════════════════")
            appendLine("       ${StringResources.get(StringKeyMatch.REPORT_MATCHMAKING_TITLE, language)}")
            appendLine("         ${StringResources.get(StringKeyMatch.REPORT_ASTROSTORM_ANALYSIS, language)}")
            appendLine("═══════════════════════════════════════")
            appendLine()

            appendLine(StringResources.get(StringKeyMatch.REPORT_PROFILES, language))
            appendLine("─────────────────────────────────────")
            brideChart?.let {
                appendLine("$brideLabel ${it.birthData.name ?: naText}")
                appendLine("  $moonSignLabel ${getRashiName(it, language)}")
                appendLine("  $nakshatraLabel ${getNakshatraName(it, language)} (${getPada(it, language)})")
            }
            groomChart?.let {
                appendLine("$groomLabel ${it.birthData.name ?: naText}")
                appendLine("  $moonSignLabel ${getRashiName(it, language)}")
                appendLine("  $nakshatraLabel ${getNakshatraName(it, language)} (${getPada(it, language)})")
            }
            appendLine()

            appendLine(StringResources.get(StringKeyMatch.REPORT_COMPATIBILITY_SCORE, language))
            appendLine("─────────────────────────────────────")
            appendLine("${StringResources.get(StringKeyMatch.REPORT_TOTAL_POINTS, language)} ${String.format("%.1f", result.totalPoints)} / ${result.maxPoints.toInt()}")
            appendLine("${StringResources.get(StringKeyMatch.REPORT_PERCENTAGE, language)} ${String.format("%.1f", result.percentage)}%")
            appendLine("${StringResources.get(StringKeyMatch.REPORT_RATING_LABEL, language)} ${result.rating.getLocalizedName(language)}")
            appendLine()

            appendLine(StringResources.get(StringKeyMatch.REPORT_ASHTAKOOTA_8_GUNA, language))
            appendLine("─────────────────────────────────────")
            result.gunaAnalyses.forEach { guna ->
                val status = if (guna.isPositive) StringResources.get(StringKeyUIExtra.CHECK_MARK, language) else StringResources.get(StringKeyUIExtra.X_MARK, language)
                val gunaTypeName = guna.gunaType.getLocalizedName(language)
                val description = com.astro.vajra.ephemeris.MatchmakingCalculator.getGunaDescription(guna.gunaType.displayName, language)
                
                appendLine("$status $gunaTypeName ($description)")
                appendLine("  ${StringResources.get(StringKeyMatch.REPORT_SCORE_LABEL, language)} ${guna.obtainedPoints.toInt()}/${guna.maxPoints.toInt()}")
                appendLine("  ${StringResources.get(StringKeyMatch.BRIDE, language)}: ${guna.brideValue} | ${StringResources.get(StringKeyMatch.GROOM, language)}: ${guna.groomValue}")
                appendLine("  ${guna.analysis}")
                appendLine()
            }

            appendLine(StringResources.get(StringKeyMatch.REPORT_MANGLIK_DOSHA_ANALYSIS, language))
            appendLine("─────────────────────────────────────")
            appendLine("${StringResources.get(StringKeyMatch.REPORT_COMPATIBILITY_LABEL, language)} ${result.manglikCompatibilityRecommendation}")
            appendLine()
            appendLine("${StringResources.get(StringKeyMatch.BRIDE, language)}: ${result.brideManglik.effectiveDosha.getLocalizedName(language)}")
            if (result.brideManglik.marsHouse > 0) {
                appendLine("  ${String.format(StringResources.get(StringKeyMatch.REPORT_MARS_IN_HOUSE, language), result.brideManglik.marsHouse)}")
            }
            result.brideManglik.factors.forEach { appendLine("  ${StringResources.get(StringKeyUICommon.BULLET, language)} $it") }
            result.brideManglik.cancellations.forEach { appendLine("  ${StringResources.get(StringKeyUIExtra.CHECK_MARK, language)} $it $cancellationText") }
            appendLine()
            appendLine("${StringResources.get(StringKeyMatch.GROOM, language)}: ${result.groomManglik.effectiveDosha.getLocalizedName(language)}")
            if (result.groomManglik.marsHouse > 0) {
                appendLine("  ${String.format(StringResources.get(StringKeyMatch.REPORT_MARS_IN_HOUSE, language), result.groomManglik.marsHouse)}")
            }
            result.groomManglik.factors.forEach { appendLine("  ${StringResources.get(StringKeyUICommon.BULLET, language)} $it") }
            result.groomManglik.cancellations.forEach { appendLine("  ${StringResources.get(StringKeyUIExtra.CHECK_MARK, language)} $it $cancellationText") }
            appendLine()

            if (result.specialConsiderations.isNotEmpty()) {
                appendLine(StringResources.get(StringKeyMatch.REPORT_SPECIAL_CONSIDERATIONS, language))
                appendLine("─────────────────────────────────────")
                result.specialConsiderations.forEach { appendLine("${StringResources.get(StringKeyUICommon.BULLET, language)} $it") }
                appendLine()
            }

            appendLine(StringResources.get(StringKeyMatch.REPORT_SUGGESTED_REMEDIES, language))
            appendLine("─────────────────────────────────────")
            result.remedies.forEachIndexed { index, remedy ->
                val pos = if (language == Language.NEPALI) com.astro.vajra.core.common.BikramSambatConverter.toNepaliNumerals(index + 1) else (index + 1).toString()
                appendLine("$pos. $remedy")
            }
            appendLine()

            appendLine("═══════════════════════════════════════")
            appendLine(StringResources.get(StringKeyMatch.REPORT_GENERATED_BY, language))
            appendLine(StringResources.get(StringKeyMatch.REPORT_ASTROSTORM_VEDIC, language))
            appendLine("═══════════════════════════════════════")
        }
    }

    /**
     * Generate a brief summary report suitable for quick sharing.
     *
     * Includes:
     * - Names of bride and groom
     * - Overall score and percentage
     * - Rating with description
     * - Manglik compatibility status
     */
    fun generateSummaryReport(
        result: MatchmakingResult,
        brideChart: VedicChart?,
        groomChart: VedicChart?,
        language: Language
    ): String {
        val naText = StringResources.get(StringKeyMatch.REPORT_NA, language)
        val brideLabel = StringResources.get(StringKeyMatch.BRIDE, language)
        val groomLabel = StringResources.get(StringKeyMatch.GROOM, language)

        return buildString {
            appendLine("${StringResources.get(StringKeyUIExtra.EMOJI_REPORT, language)} ${StringResources.get(StringKeyMatch.REPORT_KUNDLI_MILAN_SUMMARY, language)}")
            appendLine()
            brideChart?.let { appendLine("${StringResources.get(StringKeyUIExtra.EMOJI_BRIDE, language)} $brideLabel: ${it.birthData.name ?: naText}") }
            groomChart?.let { appendLine("${StringResources.get(StringKeyUIExtra.EMOJI_GROOM, language)} $groomLabel: ${it.birthData.name ?: naText}") }
            appendLine()
            appendLine("${StringResources.get(StringKeyUIExtra.EMOJI_STAR, language)} ${StringResources.get(StringKeyMatch.REPORT_SCORE_LABEL, language)} ${String.format("%.1f", result.totalPoints)}/${result.maxPoints.toInt()} (${String.format("%.1f", result.percentage)}%)")
            appendLine("${StringResources.get(StringKeyUIExtra.EMOJI_CHART, language)} ${StringResources.get(StringKeyMatch.REPORT_RATING_LABEL, language)} ${result.rating.getLocalizedName(language)}")
            appendLine()
            appendLine("${result.rating.getLocalizedDescription(language)}")
            appendLine()
            appendLine("${StringResources.get(StringKeyUIExtra.EMOJI_ALERT, language)} ${StringResources.get(StringKeyMatch.REPORT_MANGLIK_LABEL, language)} ${result.manglikCompatibilityRecommendation}")
            appendLine()
            appendLine("— ${StringResources.get(StringKeyMatch.REPORT_GENERATED_BY, language)}")
        }
    }

    /**
     * Generate a scores-only report showing just the Guna points.
     *
     * Useful for quick reference of individual scores.
     */
    fun generateScoresReport(
        result: MatchmakingResult,
        language: Language
    ): String {
        return buildString {
            appendLine(StringResources.get(StringKeyMatch.REPORT_ASHTAKOOTA_GUNA_SCORES, language))
            appendLine("━━━━━━━━━━━━━━━━━━━━━━")
            result.gunaAnalyses.forEach { guna ->
                val emoji = if (guna.isPositive) StringResources.get(StringKeyUIExtra.SYMBOL_SUCCESS, language) else StringResources.get(StringKeyUIExtra.SYMBOL_WARNING, language)
                appendLine("$emoji ${guna.gunaType.getLocalizedName(language)}: ${guna.obtainedPoints.toInt()}/${guna.maxPoints.toInt()}")
            }
            appendLine("━━━━━━━━━━━━━━━━━━━━━━")
            appendLine("${StringResources.get(StringKeyMatch.REPORT_TOTAL, language)}: ${String.format("%.1f", result.totalPoints)}/${result.maxPoints.toInt()}")
            appendLine()
            appendLine("— AstroVajra")
        }
    }

    // ============================================
    // CHART DATA EXTRACTION HELPERS
    // ============================================

    private fun getMoonPosition(chart: VedicChart) = chart.planetPositions.find {
        it.planet == Planet.MOON
    }

    /**
     * Get the nakshatra name from a chart's Moon position.
     */
    fun getNakshatraName(chart: VedicChart, language: Language = Language.ENGLISH): String {
        return getMoonPosition(chart)?.nakshatra?.getLocalizedName(language) ?: StringResources.get(StringKeyMatch.REPORT_NA, language)
    }

    /**
     * Get the rashi (moon sign) name from a chart.
     */
    fun getRashiName(chart: VedicChart, language: Language = Language.ENGLISH): String {
        val moonPosition = getMoonPosition(chart) ?: return StringResources.get(StringKeyMatch.REPORT_NA, language)
        return moonPosition.sign.getLocalizedName(language)
    }

    /**
     * Get the pada (quarter) from a chart's Moon position.
     */
    fun getPada(chart: VedicChart, language: Language = Language.ENGLISH): String {
        val moonPosition = getMoonPosition(chart) ?: return StringResources.get(StringKeyMatch.REPORT_NA, language)
        return StringResources.get(StringKeyMatch.MATCH_PADA_NUMBER, language, moonPosition.nakshatraPada)
    }

    /**
     * Get the nakshatra lord from a chart's Moon position.
     */
    fun getNakshatraLord(chart: VedicChart, language: Language = Language.ENGLISH): String {
        val moonPosition = getMoonPosition(chart) ?: return StringResources.get(StringKeyMatch.REPORT_NA, language)
        return moonPosition.nakshatra.ruler.getLocalizedName(language)
    }
}

/**
 * Extension functions for quick Manglik status determination.
 */
fun MatchmakingResult.getManglikQuickStatus(language: Language = Language.ENGLISH): String {
    val brideStatus = brideManglik.effectiveDosha
    val groomStatus = groomManglik.effectiveDosha

    return when {
        brideStatus == ManglikDosha.NONE && groomStatus == ManglikDosha.NONE -> StringResources.get(StringKeyMatch.MANGLIK_QUICK_NONE, language)
        brideStatus != ManglikDosha.NONE && groomStatus != ManglikDosha.NONE -> StringResources.get(StringKeyMatch.MANGLIK_QUICK_BOTH, language)
        brideStatus != ManglikDosha.NONE -> StringResources.get(StringKeyMatch.MANGLIK_QUICK_BRIDE, language)
        groomStatus != ManglikDosha.NONE -> StringResources.get(StringKeyMatch.MANGLIK_QUICK_GROOM, language)
        else -> StringResources.get(StringKeyMatch.DETAILS, language)
    }
}




