package com.astro.storm.ephemeris.yoga

import com.astro.storm.core.model.Planet
import com.astro.storm.core.model.PlanetPosition
import com.astro.storm.core.model.VedicChart

/**
 * Solar Yoga Evaluator - Sun-Based Combinations
 *
 * Solar Yogas are formed based on the Sun's position and its relationship
 * with other planets. The Sun represents soul, authority, and self.
 *
 * Types evaluated:
 * 1. Vesi Yoga - Planet (not Moon) in 2nd from Sun
 * 2. Vosi Yoga - Planet (not Moon) in 12th from Sun
 * 3. Ubhayachari Yoga - Planets in both 2nd and 12th from Sun
 * 4. Budha-Aditya Yoga - Mercury conjunct Sun
 *
 * Note: Moon is excluded from Vesi/Vosi as it creates different effects.
 *
 * Based on:
 * - Brihat Parasara Hora Shastra (BPHS), Chapter 36
 * - Phaladeepika, Chapter 4
 *
 * @author AstroStorm
 */
class SolarYogaEvaluator : YogaEvaluator {

    override val category: YogaCategory = YogaCategory.SOLAR_YOGA

    override fun evaluate(chart: VedicChart): List<Yoga> {
        val yogas = mutableListOf<Yoga>()

        val sunPos = chart.planetPositions.find { it.planet == Planet.SUN } ?: return yogas

        // Calculate houses from Sun
        val houseFromSun = { pos: PlanetPosition ->
            YogaHelpers.getHouseFrom(pos.sign, sunPos.sign)
        }

        // Eligible planets (excluding Moon, Rahu, Ketu)
        val eligiblePlanets = listOf(Planet.MARS, Planet.MERCURY, Planet.JUPITER, Planet.VENUS, Planet.SATURN)
        val positions = chart.planetPositions.filter { it.planet in eligiblePlanets }

        val planetsIn2nd = positions.filter { houseFromSun(it) == 2 }
        val planetsIn12th = positions.filter { houseFromSun(it) == 12 }

        // 1. Vesi Yoga
        if (planetsIn2nd.isNotEmpty() && planetsIn12th.isEmpty()) {
            yogas.add(createVesiYoga(planetsIn2nd, sunPos, chart))
        }

        // 2. Vosi Yoga
        if (planetsIn12th.isNotEmpty() && planetsIn2nd.isEmpty()) {
            yogas.add(createVosiYoga(planetsIn12th, sunPos, chart))
        }

        // 3. Ubhayachari Yoga
        if (planetsIn2nd.isNotEmpty() && planetsIn12th.isNotEmpty()) {
            yogas.add(createUbhayachariYoga(planetsIn2nd, planetsIn12th, sunPos, chart))
        }

        // 4. Budha-Aditya Yoga
        evaluateBudhaAdityaYoga(chart, sunPos)?.let { yogas.add(it) }

        return yogas
    }

    private fun createVesiYoga(
        planetsIn2nd: List<PlanetPosition>,
        sunPos: PlanetPosition,
        chart: VedicChart
    ): Yoga {
        val planets = planetsIn2nd.map { it.planet }
        val strength = YogaHelpers.calculateYogaStrength(chart, planetsIn2nd)

        val effectDetail = when {
            Planet.JUPITER in planets -> "wisdom and knowledge"
            Planet.VENUS in planets -> "arts and luxury"
            Planet.MERCURY in planets -> "communication and business"
            Planet.MARS in planets -> "courage and leadership"
            Planet.SATURN in planets -> "discipline and perseverance"
            else -> "general success"
        }

        return Yoga(
            name = "Vesi Yoga",
            sanskritName = "Vesi Yoga",
            category = YogaCategory.SOLAR_YOGA,
            planets = planets + Planet.SUN,
            houses = planetsIn2nd.map { it.house },
            description = "${planets.joinToString { it.displayName }} in 2nd from Sun",
            effects = "Truthful speech, authority through $effectDetail, respected by rulers",
            strength = YogaHelpers.strengthFromPercentage(strength),
            strengthPercentage = strength,
            isAuspicious = true,
            activationPeriod = "Sun and ${planets.first().displayName} periods",
            cancellationFactors = listOf("Planets should not be combust"),
            nameKey = com.astro.storm.core.common.StringKeyYogaExpanded.YOGA_VESI,
            effectsKey = com.astro.storm.core.common.StringKeyYogaExpanded.YOGA_EFFECT_VESI
        )
    }

    private fun createVosiYoga(
        planetsIn12th: List<PlanetPosition>,
        sunPos: PlanetPosition,
        chart: VedicChart
    ): Yoga {
        val planets = planetsIn12th.map { it.planet }
        val strength = YogaHelpers.calculateYogaStrength(chart, planetsIn12th)

        val effectDetail = when {
            Planet.JUPITER in planets -> "spiritual wisdom"
            Planet.VENUS in planets -> "creative talents"
            Planet.MERCURY in planets -> "intellectual prowess"
            Planet.MARS in planets -> "physical strength"
            Planet.SATURN in planets -> "organizational skills"
            else -> "general success"
        }

        return Yoga(
            name = "Vosi Yoga",
            sanskritName = "Vosi Yoga",
            category = YogaCategory.SOLAR_YOGA,
            planets = planets + Planet.SUN,
            houses = planetsIn12th.map { it.house },
            description = "${planets.joinToString { it.displayName }} in 12th from Sun",
            effects = "Skillful and learned through $effectDetail, good memory, charitable nature",
            strength = YogaHelpers.strengthFromPercentage(strength),
            strengthPercentage = strength,
            isAuspicious = true,
            activationPeriod = "Sun and ${planets.first().displayName} periods",
            cancellationFactors = emptyList(),
            nameKey = com.astro.storm.core.common.StringKeyYogaExpanded.YOGA_VOSI,
            effectsKey = com.astro.storm.core.common.StringKeyYogaExpanded.YOGA_EFFECT_VOSI
        )
    }

    private fun createUbhayachariYoga(
        planetsIn2nd: List<PlanetPosition>,
        planetsIn12th: List<PlanetPosition>,
        sunPos: PlanetPosition,
        chart: VedicChart
    ): Yoga {
        val allPlanets = (planetsIn2nd + planetsIn12th).map { it.planet }
        val strength = YogaHelpers.calculateYogaStrength(chart, planetsIn2nd + planetsIn12th) * 1.2

        return Yoga(
            name = "Ubhayachari Yoga",
            sanskritName = "Ubhayachari Yoga",
            category = YogaCategory.SOLAR_YOGA,
            planets = allPlanets + Planet.SUN,
            houses = (planetsIn2nd + planetsIn12th).map { it.house }.distinct(),
            description = "Sun flanked by planets in 2nd and 12th houses",
            effects = "King-like status, eloquent speaker, wealthy, physically strong, famous and respected",
            strength = YogaHelpers.strengthFromPercentage(strength.coerceAtMost(100.0)),
            strengthPercentage = strength.coerceAtMost(100.0),
            isAuspicious = true,
            activationPeriod = "Sun periods especially",
            cancellationFactors = listOf("Flanking planets should not be combust"),
            nameKey = com.astro.storm.core.common.StringKeyYogaExpanded.YOGA_UBHAYACHARI,
            effectsKey = com.astro.storm.core.common.StringKeyYogaExpanded.YOGA_EFFECT_UBHAYACHARI
        )
    }

    private fun evaluateBudhaAdityaYoga(chart: VedicChart, sunPos: PlanetPosition): Yoga? {
        val mercuryPos = chart.planetPositions.find { it.planet == Planet.MERCURY } ?: return null

        if (YogaHelpers.areConjunct(sunPos, mercuryPos)) {
            val positions = listOf(sunPos, mercuryPos)
            var strength = YogaHelpers.calculateYogaStrength(chart, positions)

            // Check combustion - Mercury is naturally close to Sun
            val combustionFactor = YogaHelpers.getCombustionFactor(mercuryPos, chart)

            // Budha-Aditya is special - slight combustion is tolerated
            val adjustedCombustion = if (combustionFactor < 0.5) combustionFactor * 0.7 else combustionFactor * 0.9
            strength *= adjustedCombustion

            // Boost for good house placement
            if (sunPos.house in listOf(1, 4, 5, 9, 10, 11)) {
                strength *= 1.15
            }

            // Strong in Mercury's signs (Gemini, Virgo) or Sun's sign (Leo)
            if (sunPos.sign in listOf(
                    com.astro.storm.core.model.ZodiacSign.GEMINI,
                    com.astro.storm.core.model.ZodiacSign.VIRGO,
                    com.astro.storm.core.model.ZodiacSign.LEO
                )) {
                strength *= 1.1
            }

            val cancellationFactors = mutableListOf<String>()
            if (combustionFactor < 0.6) {
                cancellationFactors.add("Mercury too close to Sun - yoga weakened")
            }
            if (YogaHelpers.isDebilitated(mercuryPos)) {
                cancellationFactors.add("Mercury debilitated")
                strength *= 0.7
            }

            return Yoga(
                name = "Budha-Aditya Yoga",
                sanskritName = "Budha-Aditya Yoga",
                category = YogaCategory.SOLAR_YOGA,
                planets = listOf(Planet.SUN, Planet.MERCURY),
                houses = listOf(sunPos.house),
                description = "Sun and Mercury in conjunction",
                effects = "Sharp intellect, good education, eloquent speech, success in intellectual pursuits, administrative ability",
                strength = YogaHelpers.strengthFromPercentage(strength.coerceIn(10.0, 100.0)),
                strengthPercentage = strength.coerceIn(10.0, 100.0),
                isAuspicious = true,
                activationPeriod = "Sun-Mercury periods",
                cancellationFactors = cancellationFactors.ifEmpty { listOf("None - yoga operates well") },
                nameKey = com.astro.storm.core.common.StringKeyYogaExpanded.YOGA_BUDHA_ADITYA,
                effectsKey = com.astro.storm.core.common.StringKeyYogaExpanded.YOGA_EFFECT_BUDHA_ADITYA
            )
        }

        return null
    }
}
