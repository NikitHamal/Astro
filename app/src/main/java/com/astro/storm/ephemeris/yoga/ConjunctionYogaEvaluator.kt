package com.astro.storm.ephemeris.yoga

import com.astro.storm.core.common.StringKeyYogaExpanded
import com.astro.storm.core.model.Planet
import com.astro.storm.core.model.VedicChart

/**
 * Conjunction Yoga Evaluator - Planetary Unions (Yuti)
 *
 * Evaluates conjunctions of 2 or more planets.
 * - Identifies specific named conjunctions (e.g., Budhaditya).
 * - identifying generic 2, 3, 4+ planet combinations.
 *
 * @author AstroStorm
 */
class ConjunctionYogaEvaluator : YogaEvaluator {

    override val category: YogaCategory = YogaCategory.CONJUNCTION_YOGA

    override fun evaluate(chart: VedicChart): List<Yoga> {
        val yogas = mutableListOf<Yoga>()
        
        // Group planets by house (Sign conjunction)
        // We consider Rahu/Ketu for conjunctions as well
        val planetsByHouse = chart.planetPositions.groupBy { it.house }
        
        planetsByHouse.forEach { (house, positions) ->
            if (positions.size >= 2) {
                // 1. Evaluate pairs (2-planet combinations)
                for (i in positions.indices) {
                    for (j in i + 1 until positions.size) {
                        val p1 = positions[i]
                        val p2 = positions[j]
                        
                        // Check if they are conjunct by degree (closer orb) for stronger effect?
                        // Classically sign conjunction is enough for Yuti, but degree is stronger.
                        // We will use sign conjunction as base.
                        
                        val pairYoga = createPairYoga(chart, p1, p2, house)
                        if (pairYoga != null) {
                            yogas.add(pairYoga)
                        }
                    }
                }
                
                // 2. Evaluate larger groups (3+ planets)
                if (positions.size >= 3) {
                    yogas.add(createGroupYoga(chart, positions, house))
                }
            }
        }
        
        return yogas
    }

    private fun createPairYoga(
        chart: VedicChart,
        p1: com.astro.storm.core.model.PlanetPosition,
        p2: com.astro.storm.core.model.PlanetPosition,
        house: Int
    ): Yoga? {
        val planets = listOf(p1.planet, p2.planet).sortedBy { it.ordinal }
        val planet1 = planets[0]
        val planet2 = planets[1]
        
        // Determine specific effect key
        val effectKey = getPairEffectKey(planet1, planet2)
        
        val name = "Conjunction of ${planet1.displayName} and ${planet2.displayName}"
        val sanskrit = if (effectKey?.en?.contains(":") == true) {
            effectKey.en.substringBefore(":") // Extract "Budhaditya Yoga" from "Budhaditya Yoga: ..."
        } else {
            "${planet1.displayName}-${planet2.displayName} Yoga"
        }
        
        val effects = effectKey?.en ?: "Conjunction of ${planet1.displayName} and ${planet2.displayName}"
        
        val strength = YogaHelpers.calculateYogaStrength(chart, listOf(p1, p2))
        
        return Yoga(
            name = name,
            sanskritName = sanskrit,
            category = YogaCategory.CONJUNCTION_YOGA,
            planets = listOf(planet1, planet2),
            houses = listOf(house),
            description = "Conjunction of two planets in the same sign",
            effects = effects,
            strength = YogaHelpers.strengthFromPercentage(strength),
            strengthPercentage = strength,
            isAuspicious = true, // Simplified
            activationPeriod = "${planet1.displayName}-${planet2.displayName} period",
            cancellationFactors = emptyList()
        )
    }

    private fun createGroupYoga(
        chart: VedicChart,
        positions: List<com.astro.storm.core.model.PlanetPosition>,
        house: Int
    ): Yoga {
        val planets = positions.map { it.planet }.sortedBy { it.ordinal }
        val planetNames = planets.joinToString(", ") { it.displayName }
        
        val count = planets.size
        val name = "$count-Planet Conjunction in House $house"
        
        val strength = YogaHelpers.calculateYogaStrength(chart, positions)
        
        return Yoga(
            name = name,
            sanskritName = "Graha Malika Yoga (Partial)", // Generic name
            category = YogaCategory.CONJUNCTION_YOGA,
            planets = planets,
            houses = listOf(house),
            description = "Conjunction of $count planets: $planetNames",
            effects = "Complex mixing of energies. Results depend on the strongest planet in the group.",
            strength = YogaHelpers.strengthFromPercentage(strength),
            strengthPercentage = strength,
            isAuspicious = true,
            activationPeriod = "Dasha of strongest planet",
            cancellationFactors = emptyList()
        )
    }

    private fun getPairEffectKey(p1: Planet, p2: Planet): com.astro.storm.core.common.StringKeyInterface? {
        // Map sorted pairs to keys
        return when (p1) {
            Planet.SUN -> when (p2) {
                Planet.MOON -> StringKeyYogaExpanded.EFFECT_SUN_MOON
                Planet.MARS -> StringKeyYogaExpanded.EFFECT_SUN_MARS
                Planet.MERCURY -> StringKeyYogaExpanded.EFFECT_SUN_MERCURY
                Planet.JUPITER -> StringKeyYogaExpanded.EFFECT_SUN_JUPITER
                Planet.VENUS -> StringKeyYogaExpanded.EFFECT_SUN_VENUS
                Planet.SATURN -> StringKeyYogaExpanded.EFFECT_SUN_SATURN
                else -> null
            }
            Planet.MOON -> when (p2) {
                Planet.MARS -> StringKeyYogaExpanded.EFFECT_MOON_MARS
                Planet.MERCURY -> StringKeyYogaExpanded.EFFECT_MOON_MERCURY
                Planet.JUPITER -> StringKeyYogaExpanded.EFFECT_MOON_JUPITER
                Planet.VENUS -> StringKeyYogaExpanded.EFFECT_MOON_VENUS
                Planet.SATURN -> StringKeyYogaExpanded.EFFECT_MOON_SATURN
                else -> null
            }
            Planet.MARS -> when (p2) {
                Planet.MERCURY -> StringKeyYogaExpanded.EFFECT_MARS_MERCURY
                Planet.JUPITER -> StringKeyYogaExpanded.EFFECT_MARS_JUPITER
                Planet.VENUS -> StringKeyYogaExpanded.EFFECT_MARS_VENUS
                Planet.SATURN -> StringKeyYogaExpanded.EFFECT_MARS_SATURN
                else -> null
            }
            Planet.MERCURY -> when (p2) {
                Planet.JUPITER -> StringKeyYogaExpanded.EFFECT_MERCURY_JUPITER
                Planet.VENUS -> StringKeyYogaExpanded.EFFECT_MERCURY_VENUS
                Planet.SATURN -> StringKeyYogaExpanded.EFFECT_MERCURY_SATURN
                else -> null
            }
            Planet.JUPITER -> when (p2) {
                Planet.VENUS -> StringKeyYogaExpanded.EFFECT_JUPITER_VENUS
                Planet.SATURN -> StringKeyYogaExpanded.EFFECT_JUPITER_SATURN
                else -> null
            }
            Planet.VENUS -> when (p2) {
                Planet.SATURN -> StringKeyYogaExpanded.EFFECT_VENUS_SATURN
                else -> null
            }
            else -> null
        }
    }
}
