package com.astro.storm.ephemeris.yoga

import com.astro.storm.core.model.VedicChart

/**
 * Yoga Evaluator Interface - Strategy Pattern for Yoga Calculations
 *
 * This interface defines the contract for all yoga evaluators.
 * Each evaluator is responsible for detecting specific categories of yogas
 * based on classical Vedic texts:
 * - Brihat Parasara Hora Shastra (BPHS)
 * - Phaladeepika
 * - Saravali
 * - Jataka Parijata
 *
 * Implementations:
 * - RajaYogaEvaluator: Power and authority combinations
 * - DhanaYogaEvaluator: Wealth and prosperity combinations
 * - MahapurushaYogaEvaluator: Five great person yogas
 * - NabhasaYogaEvaluator: Pattern-based planetary combinations
 * - ChandraYogaEvaluator: Moon-based combinations
 * - SolarYogaEvaluator: Sun-based combinations
 * - NegativeYogaEvaluator: Challenging combinations
 * - SpecialYogaEvaluator: Other significant combinations
 *
 * @author AstroStorm
 */
interface YogaEvaluator {

    /**
     * The category of yogas this evaluator handles
     */
    val category: YogaCategory

    /**
     * Evaluate the chart for yogas in this category
     *
     * @param chart The Vedic chart to analyze
     * @return List of detected yogas in this category
     */
    fun evaluate(chart: VedicChart): List<Yoga>
}
