package com.astro.vajra.ephemeris.prashna

import com.astro.vajra.core.common.Language
import com.astro.vajra.core.common.StringKeyAnalysis
import com.astro.vajra.core.common.StringResources
import com.astro.vajra.ephemeris.prashna.PrashnaConstants.PRASHNA_HOUSE_SIGNIFICATIONS
import com.astro.vajra.ephemeris.prashna.PrashnaHelpers.localized

object PrashnaInterpretationGenerator {

    fun generateRecommendations(
        judgment: PrashnaJudgment,
        moonAnalysis: MoonAnalysis,
        lagnaAnalysis: LagnaAnalysis,
        houseAnalysis: HouseAnalysis,
        specialYogas: List<PrashnaYoga>,
        language: Language = Language.ENGLISH
    ): List<String> {
        val recommendations = mutableListOf<String>()

        when (judgment.verdict) {
            PrashnaVerdict.STRONGLY_YES, PrashnaVerdict.YES -> {
                recommendations.add(StringResources.get(StringKeyAnalysis.PRASHNA_REC_STRONGLY_SUPPORT, language))
            }
            PrashnaVerdict.LIKELY_YES -> {
                recommendations.add(StringResources.get(StringKeyAnalysis.PRASHNA_REC_AWARENESS, language))
            }
            PrashnaVerdict.UNCERTAIN -> {
                recommendations.add(StringResources.get(StringKeyAnalysis.PRASHNA_REC_PATIENCE, language))
                recommendations.add(StringResources.get(StringKeyAnalysis.PRASHNA_REC_GUIDANCE, language))
            }
            PrashnaVerdict.TIMING_DEPENDENT -> {
                recommendations.add(StringResources.get(StringKeyAnalysis.PRASHNA_REC_VOID_MOON, language))
                recommendations.add(StringResources.get(StringKeyAnalysis.PRASHNA_REC_VOID_MOON_REASK, language))
            }
            PrashnaVerdict.LIKELY_NO, PrashnaVerdict.NO -> {
                recommendations.add(StringResources.get(StringKeyAnalysis.PRASHNA_REC_RECONSIDER, language))
                recommendations.add(StringResources.get(StringKeyAnalysis.PRASHNA_REC_EXPLORE, language))
            }
            PrashnaVerdict.STRONGLY_NO -> {
                recommendations.add(StringResources.get(StringKeyAnalysis.PRASHNA_REC_ABANDON, language))
                recommendations.add(StringResources.get(StringKeyAnalysis.PRASHNA_REC_FOCUS_FAVORABLE, language))
            }
        }

        if (!moonAnalysis.isWaxing) {
            recommendations.add(StringResources.get(StringKeyAnalysis.PRASHNA_REC_WANING_MOON, language))
        }
        if (moonAnalysis.moonStrength in listOf(MoonStrength.WEAK, MoonStrength.VERY_WEAK, MoonStrength.AFFLICTED)) {
            recommendations.add(StringResources.get(StringKeyAnalysis.PRASHNA_REC_STRENGTHEN_MOON, language))
        }

        if (lagnaAnalysis.lagnaCondition == LagnaCondition.COMBUST) {
            recommendations.add(StringResources.get(StringKeyAnalysis.PRASHNA_REC_AVOID_CONFRONTATION, language))
        }
        if (lagnaAnalysis.lagnaCondition == LagnaCondition.RETROGRADE_LORD) {
            recommendations.add(StringResources.get(StringKeyAnalysis.PRASHNA_REC_REVIEW_PAST, language))
        }

        val positiveYogas = specialYogas.filter { it.isPositive }
        if (positiveYogas.any { it.name == StringResources.get(StringKeyAnalysis.PRASHNA_YOGA_ITHASALA_NAME, language) }) {
            recommendations.add(StringResources.get(StringKeyAnalysis.PRASHNA_REC_ACT_PROMPTLY, language))
        }
        if (specialYogas.any { it.name == StringResources.get(StringKeyAnalysis.PRASHNA_YOGA_NAKTA_NAME, language) }) {
            recommendations.add(StringResources.get(StringKeyAnalysis.PRASHNA_REC_SEEK_ASSISTANCE, language))
        }

        val weakHouses = houseAnalysis.houseConditions.filter {
            it.value.condition in listOf(HouseStrength.POOR, HouseStrength.AFFLICTED)
        }
        for ((house, _) in weakHouses) {
            val karaka = PRASHNA_HOUSE_SIGNIFICATIONS[house]?.karaka
            if (karaka != null) {
                recommendations.add(StringResources.get(StringKeyAnalysis.PRASHNA_REC_PROPITIATE, language, karaka.getLocalizedName(language), house.localized(language)))
            }
        }

        return recommendations.take(7)
    }

    fun generateDetailedInterpretation(
        question: String,
        category: PrashnaCategory,
        judgment: PrashnaJudgment,
        moonAnalysis: MoonAnalysis,
        lagnaAnalysis: LagnaAnalysis,
        houseAnalysis: HouseAnalysis,
        timingPrediction: TimingPrediction,
        specialYogas: List<PrashnaYoga>,
        language: Language = Language.ENGLISH
    ): String {
        return buildString {
            appendLine(StringResources.get(StringKeyAnalysis.PRASHNA_REPORT_TITLE, language))
            appendLine("=" .repeat(50))
            appendLine()

            appendLine("${StringResources.get(StringKeyAnalysis.PRASHNA_REPORT_QUESTION, language)}: $question")
            appendLine("${StringResources.get(StringKeyAnalysis.PRASHNA_REPORT_CATEGORY, language)}: ${category.getLocalizedName(language)}")
            appendLine()

            appendLine("${StringResources.get(StringKeyAnalysis.PRASHNA_REPORT_VERDICT, language)}: ${judgment.verdict.getLocalizedName(language)}")
            appendLine("${StringResources.get(StringKeyAnalysis.PRASHNA_REPORT_CERTAINTY, language)}: ${judgment.certaintyLevel.getLocalizedName(language)}")
            appendLine()

            appendLine("${StringResources.get(StringKeyAnalysis.PRASHNA_REPORT_PRIMARY_INDICATION, language)}:")
            appendLine(judgment.primaryReason)
            appendLine()

            appendLine("${StringResources.get(StringKeyAnalysis.PRASHNA_REPORT_MOON_ANALYSIS, language)}:")
            appendLine("- ${StringResources.get(StringKeyAnalysis.PRASHNA_REPORT_POSITION, language)}: ${moonAnalysis.moonSign.getLocalizedName(language)} in ${StringResources.get(StringKeyAnalysis.PRASHNA_REPORT_HOUSE, language)} ${moonAnalysis.moonHouse.localized(language)}")
            appendLine("- ${StringResources.get(StringKeyAnalysis.PRASHNA_REPORT_NAKSHATRA, language)}: ${moonAnalysis.nakshatra.getLocalizedName(language)} (${StringResources.get(StringKeyAnalysis.PRASHNA_REPORT_PADA, language)} ${moonAnalysis.nakshatraPada.localized(language)})")
            appendLine("- ${StringResources.get(StringKeyAnalysis.PRASHNA_REPORT_PHASE, language)}: ${if (moonAnalysis.isWaxing) StringResources.get(StringKeyAnalysis.PRASHNA_REPORT_WAXING, language) else StringResources.get(StringKeyAnalysis.PRASHNA_REPORT_WANING, language)} - ${moonAnalysis.tithiName}")
            appendLine("- ${StringResources.get(StringKeyAnalysis.PRASHNA_REPORT_STRENGTH, language)}: ${moonAnalysis.moonStrength.getLocalizedName(language)}")
            if (moonAnalysis.isVoidOfCourse) {
                appendLine("- ${StringResources.get(StringKeyAnalysis.PRASHNA_REPORT_WARNING_VOID, language)}")
            }
            appendLine()

            appendLine("${StringResources.get(StringKeyAnalysis.PRASHNA_REPORT_LAGNA_ANALYSIS, language)}:")
            appendLine("- ${StringResources.get(StringKeyAnalysis.PRASHNA_REPORT_RISING_SIGN, language)}: ${lagnaAnalysis.lagnaSign.getLocalizedName(language)}")
            appendLine("- ${StringResources.get(StringKeyAnalysis.PRASHNA_REPORT_LAGNA_LORD, language)}: ${lagnaAnalysis.lagnaLord.getLocalizedName(language)} in ${StringResources.get(StringKeyAnalysis.PRASHNA_REPORT_HOUSE, language)} ${lagnaAnalysis.lagnaLordPosition.house.localized(language)}")
            appendLine("- ${StringResources.get(StringKeyAnalysis.PRASHNA_REPORT_CONDITION, language)}: ${lagnaAnalysis.lagnaCondition.getLocalizedName(language)}")
            appendLine()

            appendLine("${StringResources.get(StringKeyAnalysis.PRASHNA_REPORT_RELEVANT_HOUSES, language)} (${houseAnalysis.relevantHouses.joinToString { it.localized(language) }}):")
            for (house in houseAnalysis.relevantHouses) {
                val condition = houseAnalysis.houseConditions[house]
                appendLine("- ${StringResources.get(StringKeyAnalysis.PRASHNA_REPORT_HOUSE, language)} ${house.localized(language)}: ${condition?.condition?.getLocalizedName(language)} (${StringResources.get(StringKeyAnalysis.PRASHNA_REPORT_LAGNA_LORD, language)} in ${StringResources.get(StringKeyAnalysis.PRASHNA_REPORT_HOUSE, language)} ${condition?.lordPosition?.localized(language)})")
            }
            appendLine()

            if (specialYogas.isNotEmpty()) {
                appendLine(StringResources.get(StringKeyAnalysis.PRASHNA_YOGAS_TITLE, language) + ":")
                for (yoga in specialYogas) {
                    val symbolKey = if (yoga.isPositive) StringKeyAnalysis.PRASHNA_YOGA_SYMBOL_POSITIVE else StringKeyAnalysis.PRASHNA_YOGA_SYMBOL_NEGATIVE
                    appendLine(StringResources.get(symbolKey, language).format(yoga.name, yoga.interpretation))
                }
                appendLine()
            }

            if (timingPrediction.willEventOccur) {
                appendLine(StringResources.get(StringKeyAnalysis.PRASHNA_REPORT_TIMING, language) + ":")
                appendLine("${StringResources.get(StringKeyAnalysis.PRASHNA_REPORT_ESTIMATED, language)}: ${timingPrediction.estimatedTime}")
                appendLine("${StringResources.get(StringKeyAnalysis.PRASHNA_REPORT_METHOD, language)}: ${timingPrediction.timingMethod.getLocalizedName(language)}")
                appendLine("${StringResources.get(StringKeyAnalysis.PRASHNA_REPORT_CONFIDENCE, language)}: ${timingPrediction.confidence}%")
                appendLine()
            }

            appendLine(StringResources.get(StringKeyAnalysis.PRASHNA_SUPPORTING_FACTORS, language) + ":")
            for (factor in judgment.supportingFactors.take(5)) {
                appendLine("+ $factor")
            }
            appendLine()

            if (judgment.opposingFactors.isNotEmpty()) {
                appendLine(StringResources.get(StringKeyAnalysis.PRASHNA_CHALLENGES, language) + ":")
                for (factor in judgment.opposingFactors.take(5)) {
                    appendLine("- $factor")
                }
            }
        }
    }
}


