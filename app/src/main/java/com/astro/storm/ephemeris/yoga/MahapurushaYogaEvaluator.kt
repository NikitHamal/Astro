package com.astro.storm.ephemeris.yoga

import com.astro.storm.core.model.Planet
import com.astro.storm.core.model.VedicChart
import com.astro.storm.core.model.ZodiacSign

/**
 * Mahapurusha Yoga Evaluator - Five Great Person Combinations
 *
 * The Pancha (Five) Mahapurusha Yogas are formed when a planet is in its own
 * or exalted sign AND in a Kendra house (1, 4, 7, 10).
 *
 * Types evaluated:
 * 1. Ruchaka Yoga - Mars in own/exalted sign in Kendra
 * 2. Bhadra Yoga - Mercury in own/exalted sign in Kendra
 * 3. Hamsa Yoga - Jupiter in own/exalted sign in Kendra
 * 4. Malavya Yoga - Venus in own/exalted sign in Kendra
 * 5. Sasa Yoga - Saturn in own/exalted sign in Kendra
 *
 * Only Mars, Mercury, Jupiter, Venus, and Saturn can form these yogas.
 * Sun and Moon are luminaries and Rahu/Ketu are shadow planets.
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
        val yogas = mutableListOf<Yoga>()
        val kendraHouses = listOf(1, 4, 7, 10)

        // Mars - Ruchaka Yoga
        chart.planetPositions.find { it.planet == Planet.MARS }?.let { marsPos ->
            if (marsPos.house in kendraHouses && (YogaHelpers.isInOwnSign(marsPos) || YogaHelpers.isExalted(marsPos))) {
                yogas.add(createRuchakaYoga(marsPos, chart))
            }
        }

        // Mercury - Bhadra Yoga
        chart.planetPositions.find { it.planet == Planet.MERCURY }?.let { mercuryPos ->
            if (mercuryPos.house in kendraHouses && (YogaHelpers.isInOwnSign(mercuryPos) || YogaHelpers.isExalted(mercuryPos))) {
                yogas.add(createBhadraYoga(mercuryPos, chart))
            }
        }

        // Jupiter - Hamsa Yoga
        chart.planetPositions.find { it.planet == Planet.JUPITER }?.let { jupiterPos ->
            if (jupiterPos.house in kendraHouses && (YogaHelpers.isInOwnSign(jupiterPos) || YogaHelpers.isExalted(jupiterPos))) {
                yogas.add(createHamsaYoga(jupiterPos, chart))
            }
        }

        // Venus - Malavya Yoga
        chart.planetPositions.find { it.planet == Planet.VENUS }?.let { venusPos ->
            if (venusPos.house in kendraHouses && (YogaHelpers.isInOwnSign(venusPos) || YogaHelpers.isExalted(venusPos))) {
                yogas.add(createMalavyaYoga(venusPos, chart))
            }
        }

        // Saturn - Sasa Yoga
        chart.planetPositions.find { it.planet == Planet.SATURN }?.let { saturnPos ->
            if (saturnPos.house in kendraHouses && (YogaHelpers.isInOwnSign(saturnPos) || YogaHelpers.isExalted(saturnPos))) {
                yogas.add(createSasaYoga(saturnPos, chart))
            }
        }

        return yogas
    }

    private fun createRuchakaYoga(marsPos: com.astro.storm.core.model.PlanetPosition, chart: VedicChart): Yoga {
        val (strengthPct, cancellationReasons) = YogaHelpers.calculateYogaStrengthWithReasons(chart, listOf(marsPos))
        val dignityType = if (YogaHelpers.isExalted(marsPos)) "exalted" else "own sign"

        return Yoga(
            name = "Ruchaka Mahapurusha Yoga",
            sanskritName = "Ruchaka Yoga",
            category = YogaCategory.MAHAPURUSHA_YOGA,
            planets = listOf(Planet.MARS),
            houses = listOf(marsPos.house),
            description = "Mars $dignityType in ${marsPos.sign.displayName} in Kendra (house ${marsPos.house})",
            effects = "Courageous warrior qualities, leadership in military or sports, physical prowess, success in competitive fields",
            strength = YogaHelpers.strengthFromPercentage(strengthPct),
            strengthPercentage = strengthPct,
            isAuspicious = true,
            activationPeriod = "Mars Mahadasha and Antardashas",
            cancellationFactors = cancellationReasons.ifEmpty { listOf("None - yoga is unafflicted") }
        )
    }

    private fun createBhadraYoga(mercuryPos: com.astro.storm.core.model.PlanetPosition, chart: VedicChart): Yoga {
        val (strengthPct, cancellationReasons) = YogaHelpers.calculateYogaStrengthWithReasons(chart, listOf(mercuryPos))
        val dignityType = if (YogaHelpers.isExalted(mercuryPos)) "exalted" else "own sign"

        return Yoga(
            name = "Bhadra Mahapurusha Yoga",
            sanskritName = "Bhadra Yoga",
            category = YogaCategory.MAHAPURUSHA_YOGA,
            planets = listOf(Planet.MERCURY),
            houses = listOf(mercuryPos.house),
            description = "Mercury $dignityType in ${mercuryPos.sign.displayName} in Kendra (house ${mercuryPos.house})",
            effects = "Exceptional intelligence, eloquent speech, success in business, education, and communication fields",
            strength = YogaHelpers.strengthFromPercentage(strengthPct),
            strengthPercentage = strengthPct,
            isAuspicious = true,
            activationPeriod = "Mercury Mahadasha and Antardashas",
            cancellationFactors = cancellationReasons.ifEmpty { listOf("None - yoga is unafflicted") }
        )
    }

    private fun createHamsaYoga(jupiterPos: com.astro.storm.core.model.PlanetPosition, chart: VedicChart): Yoga {
        val (strengthPct, cancellationReasons) = YogaHelpers.calculateYogaStrengthWithReasons(chart, listOf(jupiterPos))
        val dignityType = if (YogaHelpers.isExalted(jupiterPos)) "exalted" else "own sign"

        return Yoga(
            name = "Hamsa Mahapurusha Yoga",
            sanskritName = "Hamsa Yoga",
            category = YogaCategory.MAHAPURUSHA_YOGA,
            planets = listOf(Planet.JUPITER),
            houses = listOf(jupiterPos.house),
            description = "Jupiter $dignityType in ${jupiterPos.sign.displayName} in Kendra (house ${jupiterPos.house})",
            effects = "Wisdom and righteousness, spiritual inclination, success in teaching, law, and advisory roles",
            strength = YogaHelpers.strengthFromPercentage(strengthPct),
            strengthPercentage = strengthPct,
            isAuspicious = true,
            activationPeriod = "Jupiter Mahadasha and Antardashas",
            cancellationFactors = cancellationReasons.ifEmpty { listOf("None - yoga is unafflicted") }
        )
    }

    private fun createMalavyaYoga(venusPos: com.astro.storm.core.model.PlanetPosition, chart: VedicChart): Yoga {
        val (strengthPct, cancellationReasons) = YogaHelpers.calculateYogaStrengthWithReasons(chart, listOf(venusPos))
        val dignityType = if (YogaHelpers.isExalted(venusPos)) "exalted" else "own sign"

        return Yoga(
            name = "Malavya Mahapurusha Yoga",
            sanskritName = "Malavya Yoga",
            category = YogaCategory.MAHAPURUSHA_YOGA,
            planets = listOf(Planet.VENUS),
            houses = listOf(venusPos.house),
            description = "Venus $dignityType in ${venusPos.sign.displayName} in Kendra (house ${venusPos.house})",
            effects = "Luxury and comfort, artistic talent, beautiful appearance, success in creative and entertainment fields",
            strength = YogaHelpers.strengthFromPercentage(strengthPct),
            strengthPercentage = strengthPct,
            isAuspicious = true,
            activationPeriod = "Venus Mahadasha and Antardashas",
            cancellationFactors = cancellationReasons.ifEmpty { listOf("None - yoga is unafflicted") }
        )
    }

    private fun createSasaYoga(saturnPos: com.astro.storm.core.model.PlanetPosition, chart: VedicChart): Yoga {
        val (strengthPct, cancellationReasons) = YogaHelpers.calculateYogaStrengthWithReasons(chart, listOf(saturnPos))
        val dignityType = if (YogaHelpers.isExalted(saturnPos)) "exalted" else "own sign"

        return Yoga(
            name = "Sasa Mahapurusha Yoga",
            sanskritName = "Sasa Yoga",
            category = YogaCategory.MAHAPURUSHA_YOGA,
            planets = listOf(Planet.SATURN),
            houses = listOf(saturnPos.house),
            description = "Saturn $dignityType in ${saturnPos.sign.displayName} in Kendra (house ${saturnPos.house})",
            effects = "Authority over masses, success in politics, administrative power, longevity, steady rise to prominence",
            strength = YogaHelpers.strengthFromPercentage(strengthPct),
            strengthPercentage = strengthPct,
            isAuspicious = true,
            activationPeriod = "Saturn Mahadasha and Antardashas",
            cancellationFactors = cancellationReasons.ifEmpty { listOf("None - yoga is unafflicted") }
        )
    }
}
