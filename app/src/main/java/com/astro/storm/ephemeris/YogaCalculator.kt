package com.astro.storm.ephemeris

import com.astro.storm.core.model.VedicChart
import com.astro.storm.ephemeris.yoga.AdvancedYogaEvaluator
import com.astro.storm.ephemeris.yoga.BhavaYogaEvaluator
import com.astro.storm.ephemeris.yoga.ChandraYogaEvaluator
import com.astro.storm.ephemeris.yoga.ConjunctionYogaEvaluator
import com.astro.storm.ephemeris.yoga.DhanaYogaEvaluator
import com.astro.storm.ephemeris.yoga.MahapurushaYogaEvaluator
import com.astro.storm.ephemeris.yoga.NabhasaYogaEvaluator
import com.astro.storm.ephemeris.yoga.NegativeYogaEvaluator
import com.astro.storm.ephemeris.yoga.RajaYogaEvaluator
import com.astro.storm.ephemeris.yoga.SolarYogaEvaluator
import com.astro.storm.ephemeris.yoga.SpecialYogaEvaluator
import com.astro.storm.ephemeris.yoga.Yoga
import com.astro.storm.ephemeris.yoga.YogaAnalysis
import com.astro.storm.ephemeris.yoga.YogaCategory
import com.astro.storm.ephemeris.yoga.YogaEvaluator
import com.astro.storm.ephemeris.yoga.YogaStrength
import com.astro.storm.ephemeris.yoga.evaluators.ArishtaYogaEvaluator
import com.astro.storm.ephemeris.yoga.evaluators.KarmaYogaEvaluator
import com.astro.storm.ephemeris.yoga.evaluators.NakshatraYogaEvaluator
import com.astro.storm.ephemeris.yoga.evaluators.ParivartanaYogaEvaluator
import com.astro.storm.ephemeris.yoga.evaluators.RahuKetuYogaEvaluator
import com.astro.storm.ephemeris.yoga.evaluators.SannyasaYogaEvaluator
import com.astro.storm.ephemeris.yoga.evaluators.VivahaYogaEvaluator

/**
 * Yoga Calculator - Orchestrator for Planetary Yoga Analysis
 *
 * This calculator uses the Strategy Pattern to evaluate different categories
 * of Vedic yogas. Each yoga category is handled by a specialized evaluator.
 *
 * Evaluators:
 * - RajaYogaEvaluator: Power and authority combinations
 * - DhanaYogaEvaluator: Wealth and prosperity combinations
 * - MahapurushaYogaEvaluator: Five great person yogas (Pancha Mahapurusha)
 * - NabhasaYogaEvaluator: Pattern-based planetary combinations
 * - ChandraYogaEvaluator: Moon-based combinations
 * - SolarYogaEvaluator: Sun-based combinations
 * - NegativeYogaEvaluator: Challenging combinations (Arishta)
 * - SpecialYogaEvaluator: Other significant combinations
 *
 * Based on classical texts:
 * - Brihat Parasara Hora Shastra (BPHS)
 * - Phaladeepika
 * - Saravali
 * - Jataka Parijata
 *
 * @author AstroStorm
 */
object YogaCalculator {

    /**
     * Registry of yoga evaluators
     * 
     * Core Evaluators (Original):
     * - RajaYogaEvaluator: Power and authority combinations
     * - DhanaYogaEvaluator: Wealth and prosperity combinations
     * - MahapurushaYogaEvaluator: Five great person yogas
     * - NabhasaYogaEvaluator: Pattern-based planetary combinations
     * - ChandraYogaEvaluator: Moon-based combinations
     * - SolarYogaEvaluator: Sun-based combinations
     * - NegativeYogaEvaluator: Challenging combinations
     * - BhavaYogaEvaluator: House lord placement yogas
     * - ConjunctionYogaEvaluator: Planetary conjunction yogas
     * - AdvancedYogaEvaluator: Complex multi-planet yogas
     * - SpecialYogaEvaluator: Other significant combinations
     * 
     * Extended Evaluators (New):
     * - ParivartanaYogaEvaluator: Mutual exchange yogas
     * - ArishtaYogaEvaluator: Comprehensive negative/challenging yogas
     * - NakshatraYogaEvaluator: Nakshatra-based yogas
     * - KarmaYogaEvaluator: Career and profession yogas
     * - VivahaYogaEvaluator: Marriage and relationship yogas
     * - RahuKetuYogaEvaluator: Nodal yogas including 12 Kala Sarpa types
     * - SannyasaYogaEvaluator: Renunciation and spiritual yogas
     */
    private val evaluators: List<YogaEvaluator> = listOf(
        // Core evaluators
        RajaYogaEvaluator(),
        DhanaYogaEvaluator(),
        MahapurushaYogaEvaluator(),
        NabhasaYogaEvaluator(),
        ChandraYogaEvaluator(),
        SolarYogaEvaluator(),
        NegativeYogaEvaluator(),
        BhavaYogaEvaluator(),
        ConjunctionYogaEvaluator(),
        AdvancedYogaEvaluator(),
        SpecialYogaEvaluator(),
        // Extended evaluators
        ParivartanaYogaEvaluator(),
        ArishtaYogaEvaluator(),
        NakshatraYogaEvaluator(),
        KarmaYogaEvaluator(),
        VivahaYogaEvaluator(),
        RahuKetuYogaEvaluator(),
        SannyasaYogaEvaluator()
    )

    /**
     * Analyze a Vedic chart for all yogas
     *
     * @param chart The Vedic chart to analyze
     * @return Complete yoga analysis result
     */
    fun calculateYogas(chart: VedicChart): YogaAnalysis {
        // Collect yogas from all evaluators
        val allYogas = mutableListOf<Yoga>()
        val yogasByCategory = mutableMapOf<YogaCategory, MutableList<Yoga>>()

        // Initialize category lists
        YogaCategory.entries.forEach { category ->
            yogasByCategory[category] = mutableListOf()
        }

        // Execute each evaluator
        evaluators.forEach { evaluator ->
            try {
                val yogas = evaluator.evaluate(chart)
                allYogas.addAll(yogas)
                yogasByCategory[evaluator.category]?.addAll(yogas)
            } catch (e: Exception) {
                // Log error but continue with other evaluators
                android.util.Log.e("YogaCalculator", "Error in ${evaluator.category}: ${e.message}")
            }
        }

        // Calculate overall strength
        val overallStrength = calculateOverallStrength(allYogas)

        // Determine dominant category
        val dominantCategory = determineDominantCategory(yogasByCategory)

        return YogaAnalysis(
            chart = chart,
            allYogas = allYogas.sortedByDescending { it.strengthPercentage },
            rajaYogas = yogasByCategory[YogaCategory.RAJA_YOGA] ?: emptyList(),
            dhanaYogas = yogasByCategory[YogaCategory.DHANA_YOGA] ?: emptyList(),
            mahapurushaYogas = yogasByCategory[YogaCategory.MAHAPURUSHA_YOGA] ?: emptyList(),
            nabhasaYogas = yogasByCategory[YogaCategory.NABHASA_YOGA] ?: emptyList(),
            chandraYogas = yogasByCategory[YogaCategory.CHANDRA_YOGA] ?: emptyList(),
            solarYogas = yogasByCategory[YogaCategory.SOLAR_YOGA] ?: emptyList(),
            negativeYogas = yogasByCategory[YogaCategory.NEGATIVE_YOGA] ?: emptyList(),
            specialYogas = yogasByCategory[YogaCategory.SPECIAL_YOGA] ?: emptyList(),
            bhavaYogas = yogasByCategory[YogaCategory.BHAVA_YOGA] ?: emptyList(),
            conjunctionYogas = yogasByCategory[YogaCategory.CONJUNCTION_YOGA] ?: emptyList(),
            dominantYogaCategory = dominantCategory,
            overallYogaStrength = overallStrength
        )
    }

    /**
     * Calculate overall yoga strength for the chart
     */
    private fun calculateOverallStrength(yogas: List<Yoga>): Double {
        if (yogas.isEmpty()) return 0.0

        // Weight auspicious yogas positively, negative yogas reduce score
        var totalPositive = 0.0
        var totalNegative = 0.0
        var positiveCount = 0
        var negativeCount = 0

        yogas.forEach { yoga ->
            if (yoga.isAuspicious) {
                totalPositive += yoga.strengthPercentage
                positiveCount++
            } else {
                totalNegative += yoga.strengthPercentage
                negativeCount++
            }
        }

        val positiveAvg = if (positiveCount > 0) totalPositive / positiveCount else 0.0
        val negativeAvg = if (negativeCount > 0) totalNegative / negativeCount else 0.0

        // Overall formula: positive average with negative reduction
        val overall = positiveAvg - (negativeAvg * 0.3)
        return overall.coerceIn(0.0, 100.0)
    }

    /**
     * Determine the dominant yoga category based on count and strength
     */
    private fun determineDominantCategory(
        yogasByCategory: Map<YogaCategory, List<Yoga>>
    ): YogaCategory {
        // Score each category based on count and strength
        val categoryScores = yogasByCategory.mapValues { (_, yogas) ->
            if (yogas.isEmpty()) 0.0
            else yogas.size * (yogas.sumOf { it.strengthPercentage } / yogas.size)
        }

        // Return highest scoring category (prefer non-negative)
        val nonNegativeScores = categoryScores.filter { it.key != YogaCategory.NEGATIVE_YOGA }
        return if (nonNegativeScores.isEmpty() || nonNegativeScores.values.all { it == 0.0 }) {
            YogaCategory.SPECIAL_YOGA
        } else {
            nonNegativeScores.maxByOrNull { it.value }?.key ?: YogaCategory.SPECIAL_YOGA
        }
    }

    // ==================== QUICK CHECK METHODS ====================

    /**
     * Quick check for Raja Yoga presence
     */
    fun hasRajaYoga(chart: VedicChart): Boolean {
        return RajaYogaEvaluator().evaluate(chart).isNotEmpty()
    }

    /**
     * Quick check for Mahapurusha Yoga presence
     */
    fun hasMahapurushaYoga(chart: VedicChart): Boolean {
        return MahapurushaYogaEvaluator().evaluate(chart).isNotEmpty()
    }

    /**
     * Quick check for Gaja-Kesari Yoga presence
     */
    fun hasGajaKesariYoga(chart: VedicChart): Boolean {
        return ChandraYogaEvaluator().evaluate(chart).any {
            it.name.contains("Gaja-Kesari")
        }
    }

    /**
     * Quick check for Kala Sarpa Yoga presence
     */
    fun hasKalaSarpaYoga(chart: VedicChart): Boolean {
        return NegativeYogaEvaluator().evaluate(chart).any {
            it.name.contains("Kala Sarpa")
        }
    }

    /**
     * Quick check for Dhana Yoga presence
     */
    fun hasDhanaYoga(chart: VedicChart): Boolean {
        return DhanaYogaEvaluator().evaluate(chart).isNotEmpty()
    }

    /**
     * Get count of auspicious vs negative yogas (for summary display)
     */
    fun getYogaSummary(chart: VedicChart): Pair<Int, Int> {
        val analysis = calculateYogas(chart)
        val auspicious = analysis.allYogas.count { it.isAuspicious }
        val negative = analysis.allYogas.count { !it.isAuspicious }
        return Pair(auspicious, negative)
    }

    /**
     * Get the strongest yoga in the chart
     */
    fun getStrongestYoga(chart: VedicChart): Yoga? {
        val analysis = calculateYogas(chart)
        return analysis.allYogas.filter { it.isAuspicious }.maxByOrNull { it.strengthPercentage }
    }

    /**
     * Get the most challenging yoga in the chart
     */
    fun getMostChallengingYoga(chart: VedicChart): Yoga? {
        val analysis = calculateYogas(chart)
        return analysis.allYogas.filter { !it.isAuspicious }.maxByOrNull { it.strengthPercentage }
    }
}
