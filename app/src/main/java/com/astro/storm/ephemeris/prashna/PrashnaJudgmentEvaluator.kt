package com.astro.storm.ephemeris.prashna

import com.astro.storm.core.common.Language
import com.astro.storm.core.common.StringKeyAnalysis
import com.astro.storm.core.common.StringResources
import com.astro.storm.core.model.VedicChart
import kotlin.math.abs

object PrashnaJudgmentEvaluator {

    fun calculateJudgment(
        chart: VedicChart,
        category: PrashnaCategory,
        moonAnalysis: MoonAnalysis,
        lagnaAnalysis: LagnaAnalysis,
        houseAnalysis: HouseAnalysis,
        specialYogas: List<PrashnaYoga>,
        omens: List<PrashnaOmen>,
        language: Language = Language.ENGLISH
    ): PrashnaJudgment {
        var score = 0
        val supportingFactors = mutableListOf<String>()
        val opposingFactors = mutableListOf<String>()

        when (moonAnalysis.moonStrength) {
            MoonStrength.EXCELLENT -> {
                score += 25
                supportingFactors.add(StringResources.get(StringKeyAnalysis.PRASHNA_FACTOR_MOON_EXCELLENT, language))
            }
            MoonStrength.GOOD -> {
                score += 18
                supportingFactors.add(StringResources.get(StringKeyAnalysis.PRASHNA_FACTOR_MOON_GOOD, language))
            }
            MoonStrength.AVERAGE -> {
                score += 8
                supportingFactors.add(StringResources.get(StringKeyAnalysis.PRASHNA_FACTOR_MOON_AVERAGE, language))
            }
            MoonStrength.WEAK -> {
                score -= 10
                opposingFactors.add(StringResources.get(StringKeyAnalysis.PRASHNA_FACTOR_MOON_WEAK, language))
            }
            MoonStrength.VERY_WEAK -> {
                score -= 18
                opposingFactors.add(StringResources.get(StringKeyAnalysis.PRASHNA_FACTOR_MOON_VERY_WEAK, language))
            }
            MoonStrength.AFFLICTED -> {
                score -= 25
                opposingFactors.add(StringResources.get(StringKeyAnalysis.PRASHNA_FACTOR_MOON_AFFLICTED, language))
            }
        }

        if (moonAnalysis.isWaxing) {
            score += 10
            supportingFactors.add(StringResources.get(StringKeyAnalysis.PRASHNA_FACTOR_MOON_WAXING, language))
        } else {
            score -= 5
            opposingFactors.add(StringResources.get(StringKeyAnalysis.PRASHNA_FACTOR_MOON_WANING, language))
        }

        if (moonAnalysis.isVoidOfCourse) {
            score -= 15
            opposingFactors.add(StringResources.get(StringKeyAnalysis.PRASHNA_FACTOR_MOON_VOID, language))
        }

        when (lagnaAnalysis.lagnaCondition) {
            LagnaCondition.STRONG -> {
                score += 20
                supportingFactors.add(StringResources.get(StringKeyAnalysis.PRASHNA_FACTOR_LAGNA_STRONG, language))
            }
            LagnaCondition.MODERATE -> {
                score += 10
                supportingFactors.add(StringResources.get(StringKeyAnalysis.PRASHNA_FACTOR_LAGNA_MODERATE, language))
            }
            LagnaCondition.WEAK -> {
                score -= 10
                opposingFactors.add(StringResources.get(StringKeyAnalysis.PRASHNA_FACTOR_LAGNA_WEAK, language))
            }
            LagnaCondition.COMBUST -> {
                score -= 15
                opposingFactors.add(StringResources.get(StringKeyAnalysis.PRASHNA_FACTOR_LAGNA_COMBUST, language))
            }
            LagnaCondition.RETROGRADE_LORD -> {
                score -= 5
                opposingFactors.add(StringResources.get(StringKeyAnalysis.PRASHNA_FACTOR_LAGNA_RETRO, language))
            }
        }

        val relevantHouseScore = houseAnalysis.relevantHouses.sumOf { house ->
            when (houseAnalysis.houseConditions[house]?.condition) {
                HouseStrength.EXCELLENT -> 5
                HouseStrength.GOOD -> 3
                HouseStrength.MODERATE -> 1
                HouseStrength.POOR -> -2
                HouseStrength.AFFLICTED -> -4
                null -> 0
            } as Int
        }
        score += (relevantHouseScore * 15) / (houseAnalysis.relevantHouses.size * 5)
        if (relevantHouseScore > 0) {
            supportingFactors.add(StringResources.get(StringKeyAnalysis.PRASHNA_FACTOR_HOUSES_FAVORABLE, language))
        } else if (relevantHouseScore < 0) {
            opposingFactors.add(StringResources.get(StringKeyAnalysis.PRASHNA_FACTOR_HOUSES_AFFLICTED, language))
        }

        for (yoga in specialYogas) {
            val symbolKey = if (yoga.isPositive) StringKeyAnalysis.PRASHNA_YOGA_SYMBOL_POSITIVE else StringKeyAnalysis.PRASHNA_YOGA_SYMBOL_NEGATIVE
            if (yoga.isPositive) {
                score += yoga.strength * 4
                supportingFactors.add(StringResources.get(symbolKey, language).format(yoga.name, yoga.interpretation))
            } else {
                score -= yoga.strength * 4
                opposingFactors.add(StringResources.get(symbolKey, language).format(yoga.name, yoga.interpretation))
            }
        }

        val positiveOmens = omens.count { it.isPositive }
        val negativeOmens = omens.count { !it.isPositive }
        score += (positiveOmens - negativeOmens) * 3
        score = score.coerceIn(-100, 100)

        val verdict = when {
            score >= 70 -> PrashnaVerdict.STRONGLY_YES
            score >= 45 -> PrashnaVerdict.YES
            score >= 20 -> PrashnaVerdict.LIKELY_YES
            score >= -20 -> PrashnaVerdict.UNCERTAIN
            score >= -45 -> PrashnaVerdict.LIKELY_NO
            score >= -70 -> PrashnaVerdict.NO
            else -> PrashnaVerdict.STRONGLY_NO
        }

        val finalVerdict = if (moonAnalysis.isVoidOfCourse && score > -20) {
            PrashnaVerdict.TIMING_DEPENDENT
        } else {
            verdict
        }

        val primaryReason = when {
            score >= 50 -> StringResources.get(StringKeyAnalysis.PRASHNA_REASON_STRONGLY_YES, language)
            score >= 20 -> StringResources.get(StringKeyAnalysis.PRASHNA_REASON_YES, language)
            score >= -20 -> StringResources.get(StringKeyAnalysis.PRASHNA_REASON_UNCERTAIN, language)
            score >= -50 -> StringResources.get(StringKeyAnalysis.PRASHNA_REASON_CHALLENGES, language)
            else -> StringResources.get(StringKeyAnalysis.PRASHNA_REASON_STRONGLY_NO, language)
        }

        val certaintyValue = abs(score)
        val certaintyLevel = when {
            certaintyValue >= 70 -> CertaintyLevel.VERY_HIGH
            certaintyValue >= 50 -> CertaintyLevel.HIGH
            certaintyValue >= 30 -> CertaintyLevel.MODERATE
            certaintyValue >= 15 -> CertaintyLevel.LOW
            else -> CertaintyLevel.VERY_LOW
        }

        return PrashnaJudgment(
            verdict = finalVerdict,
            primaryReason = primaryReason,
            supportingFactors = supportingFactors,
            opposingFactors = opposingFactors,
            overallScore = score,
            certaintyLevel = certaintyLevel
        )
    }

    fun calculateConfidence(
        judgment: PrashnaJudgment,
        moonAnalysis: MoonAnalysis,
        lagnaAnalysis: LagnaAnalysis,
        specialYogas: List<PrashnaYoga>
    ): Int {
        var confidence = 50
        confidence += abs(judgment.overallScore) / 3
        confidence += moonAnalysis.moonStrength.score * 4
        if (moonAnalysis.isVoidOfCourse) confidence -= 15
        if (lagnaAnalysis.lagnaCondition == LagnaCondition.STRONG) confidence += 10
        val strongYogas = specialYogas.count { it.strength >= 4 }
        confidence += strongYogas * 5
        val positiveYogas = specialYogas.count { it.isPositive }
        val negativeYogas = specialYogas.count { !it.isPositive }
        if (positiveYogas > 0 && negativeYogas > 0) {
            confidence -= 10
        }
        return confidence.coerceIn(10, 95)
    }
}
