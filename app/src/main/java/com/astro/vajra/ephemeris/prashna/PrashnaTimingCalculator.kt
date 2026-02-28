package com.astro.vajra.ephemeris.prashna

import com.astro.vajra.core.common.BikramSambatConverter
import com.astro.vajra.core.common.Language
import com.astro.vajra.core.common.StringKeyAnalysis
import com.astro.vajra.core.common.StringResources
import com.astro.vajra.core.model.Quality
import com.astro.vajra.core.model.VedicChart
import com.astro.vajra.core.model.ZodiacSign
import com.astro.vajra.ephemeris.prashna.PrashnaConstants.QUESTION_CATEGORIES
import com.astro.vajra.ephemeris.prashna.PrashnaConstants.TIMING_UNITS
import com.astro.vajra.ephemeris.prashna.PrashnaHelpers.localized
import kotlin.math.round

object PrashnaTimingCalculator {

    fun calculateTiming(
        chart: VedicChart,
        category: PrashnaCategory,
        moonAnalysis: MoonAnalysis,
        lagnaAnalysis: LagnaAnalysis,
        houseAnalysis: HouseAnalysis,
        language: Language = Language.ENGLISH
    ): TimingPrediction {
        val relevantHouse = houseAnalysis.relevantHouses.firstOrNull() ?: 7
        val moonToRelevantHouse = calculateMoonTransitTime(moonAnalysis, relevantHouse)
        val lagnaDegreesTiming = calculateLagnaDegreesTiming(lagnaAnalysis)
        val significatorTiming = calculateSignificatorTiming(chart, category)

        val primaryMethod: TimingMethod
        val timingValue: Double
        val timingUnit: TimingUnit

        when {
            moonAnalysis.moonStrength.score >= 3 -> {
                primaryMethod = TimingMethod.MOON_TRANSIT
                timingValue = moonToRelevantHouse.first
                timingUnit = moonToRelevantHouse.second
            }
            lagnaAnalysis.lagnaCondition == LagnaCondition.STRONG -> {
                primaryMethod = TimingMethod.LAGNA_DEGREES
                timingValue = lagnaDegreesTiming.first
                timingUnit = lagnaDegreesTiming.second
            }
            else -> {
                primaryMethod = TimingMethod.MIXED
                timingValue = significatorTiming.first
                timingUnit = significatorTiming.second
            }
        }

        val willOccur = moonAnalysis.moonStrength.score >= 2 &&
                       lagnaAnalysis.lagnaCondition != LagnaCondition.WEAK

        val estimatedTime = formatTimingEstimate(timingValue, timingUnit, language)
        val confidence = calculateTimingConfidence(moonAnalysis, lagnaAnalysis)
        val explanation = buildTimingExplanation(primaryMethod, timingValue, timingUnit, moonAnalysis, language)

        return TimingPrediction(
            willEventOccur = willOccur,
            estimatedTime = estimatedTime,
            timingMethod = primaryMethod,
            unit = timingUnit,
            value = timingValue,
            confidence = confidence,
            explanation = explanation
        )
    }

    private fun calculateMoonTransitTime(
        moonAnalysis: MoonAnalysis,
        targetHouse: Int
    ): Pair<Double, TimingUnit> {
        val currentHouse = moonAnalysis.moonHouse
        val housesToTravel = if (targetHouse >= currentHouse) {
            targetHouse - currentHouse
        } else {
            12 - currentHouse + targetHouse
        }
        val days = housesToTravel * 2.5
        return if (days <= 14) {
            Pair(days, TimingUnit.DAYS)
        } else if (days <= 60) {
            Pair(days / 7, TimingUnit.WEEKS)
        } else {
            Pair(days / 30, TimingUnit.MONTHS)
        }
    }

    private fun calculateLagnaDegreesTiming(lagnaAnalysis: LagnaAnalysis): Pair<Double, TimingUnit> {
        val degreesInSign = lagnaAnalysis.lagnaDegree % 30
        val remainingDegrees = 30 - degreesInSign
        return when (lagnaAnalysis.lagnaSign.quality) {
            Quality.CARDINAL -> Pair(remainingDegrees, TimingUnit.DAYS)
            Quality.FIXED -> Pair(remainingDegrees, TimingUnit.MONTHS)
            Quality.MUTABLE -> Pair(remainingDegrees, TimingUnit.WEEKS)
        }
    }

    private fun calculateSignificatorTiming(
        chart: VedicChart,
        category: PrashnaCategory
    ): Pair<Double, TimingUnit> {
        val relevantHouse = QUESTION_CATEGORIES[category]?.firstOrNull() ?: 7
        val houseSign = ZodiacSign.fromLongitude(chart.houseCusps[relevantHouse - 1])
        val houseLord = houseSign.ruler
        val lordPosition = chart.planetPositions.first { it.planet == houseLord }
        val degreesInSign = lordPosition.longitude % 30
        val remainingDegrees = 30 - degreesInSign
        val unit = TIMING_UNITS[houseLord] ?: TimingUnit.WEEKS
        return Pair(remainingDegrees, unit)
    }

    private fun formatTimingEstimate(value: Double, unit: TimingUnit, language: Language): String {
        val roundedValue = round(value * 10) / 10
        val unitName = unit.getLocalizedName(language)
        return when {
            roundedValue < 1 -> StringResources.get(StringKeyAnalysis.PRASHNA_TIMING_WITHIN, language).format("1 $unitName".let { if (language == Language.NEPALI) BikramSambatConverter.toNepaliNumerals(it) else it })
            roundedValue == 1.0 -> StringResources.get(StringKeyAnalysis.PRASHNA_TIMING_ABOUT, language).format("1 $unitName".let { if (language == Language.NEPALI) BikramSambatConverter.toNepaliNumerals(it) else it })
            else -> StringResources.get(StringKeyAnalysis.PRASHNA_TIMING_ABOUT, language).format("${roundedValue.toInt()} $unitName".let { if (language == Language.NEPALI) BikramSambatConverter.toNepaliNumerals(it) else it })
        }
    }

    private fun calculateTimingConfidence(
        moonAnalysis: MoonAnalysis,
        lagnaAnalysis: LagnaAnalysis
    ): Int {
        var confidence = 50
        if (moonAnalysis.moonStrength.score >= 3) confidence += 15
        if (!moonAnalysis.isVoidOfCourse) confidence += 10
        if (lagnaAnalysis.lagnaCondition == LagnaCondition.STRONG) confidence += 10
        if (moonAnalysis.isWaxing) confidence += 5
        return confidence.coerceIn(20, 80)
    }

    private fun buildTimingExplanation(
        method: TimingMethod,
        value: Double,
        unit: TimingUnit,
        moonAnalysis: MoonAnalysis,
        language: Language
    ): String {
        val methodLabel = method.getLocalizedName(language)
        val timeframe = formatTimingEstimate(value, unit, language)
        val moonSpeed = moonAnalysis.moonSpeed.localized(language, 2)
        return StringResources.get(StringKeyAnalysis.PRASHNA_TIMING_EXPLANATION, language).format(methodLabel, timeframe, moonSpeed)
    }
}


