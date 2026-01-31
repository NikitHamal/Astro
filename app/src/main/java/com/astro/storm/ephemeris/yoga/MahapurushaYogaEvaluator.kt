package com.astro.storm.ephemeris.yoga

import com.astro.storm.core.model.Planet
import com.astro.storm.core.model.VedicChart
import com.astro.storm.ephemeris.PanchMahapurushaYogaCalculator

/**
 * Mahapurusha Yoga Evaluator - Five Great Person Combinations
 *
 * The Pancha (Five) Mahapurusha Yogas are formed when a planet is in its own
 * or exalted sign AND in a Kendra house (1, 4, 7, 10).
 *
 * This evaluator leverages the detailed [PanchMahapurushaYogaCalculator]
 * to provide highly precise analysis and integration with the main yoga system.
 *
 * Types evaluated:
 * 1. Ruchaka Yoga - Mars
 * 2. Bhadra Yoga - Mercury
 * 3. Hamsa Yoga - Jupiter
 * 4. Malavya Yoga - Venus
 * 5. Sasa Yoga - Saturn
 *
 * Based on:
 * - Brihat Parasara Hora Shastra (BPHS), Chapter 75
 * - Phaladeepika, Chapter 6
 *
 * @author AstroStorm
 */
class MahapurushaYogaEvaluator : YogaEvaluator {

    override val category: YogaCategory = YogaCategory.MAHAPURUSHA_YOGA

    override fun evaluate(chart: VedicChart): List<Yoga> {
        val analysis = PanchMahapurushaYogaCalculator.analyzePanchMahapurushaYogas(chart)

        return analysis.yogas.map { detailedYoga ->
            Yoga(
                name = detailedYoga.type.displayName,
                sanskritName = detailedYoga.type.displayName, // Already contains sanskritized names like Ruchaka
                category = YogaCategory.MAHAPURUSHA_YOGA,
                planets = listOf(detailedYoga.planet),
                houses = listOf(detailedYoga.house),
                description = detailedYoga.type.description,
                effects = detailedYoga.effects.careerIndications + ". " + detailedYoga.effects.manifestationStrength,
                strength = YogaHelpers.strengthFromPercentage(detailedYoga.strength.toDouble()),
                strengthPercentage = detailedYoga.strength.toDouble(),
                isAuspicious = true,
                activationPeriod = detailedYoga.planet.displayName + " periods",
                cancellationFactors = emptyList(), // Detailed calculator doesn't currently provide explicit cancellations, but handles strength
                detailedResult = detailedYoga,
                
                // Add high-precision keys
                nameKey = detailedYoga.type.nameKey,
                descriptionKey = detailedYoga.type.descKey
            )
        }
    }
}
