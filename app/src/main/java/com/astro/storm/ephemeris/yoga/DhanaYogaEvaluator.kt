package com.astro.storm.ephemeris.yoga

import com.astro.storm.data.model.Planet
import com.astro.storm.data.model.VedicChart
import com.astro.storm.data.model.ZodiacSign

/**
 * Dhana Yoga Evaluator - Wealth and Prosperity Combinations
 *
 * Dhana Yogas are formed through the association of wealth-indicating houses:
 * - 2nd house (accumulated wealth, family resources)
 * - 5th house (past merit, speculative gains)
 * - 9th house (fortune, luck)
 * - 11th house (gains, income)
 *
 * Types evaluated:
 * 1. Basic Dhana Yoga - Lords of 2, 5, 9, 11 in conjunction or exchange
 * 2. Lakshmi Yoga - Venus strong in Kendra/Trikona
 * 3. Kubera Yoga - Jupiter and Mercury in 2nd house
 * 4. Chandra-Mangala Yoga - Moon-Mars conjunction for business wealth
 * 5. Labha Yoga - 11th lord well-placed
 *
 * Based on:
 * - Brihat Parasara Hora Shastra (BPHS), Chapter 40
 * - Phaladeepika, Chapter 6
 *
 * @author AstroStorm
 */
class DhanaYogaEvaluator : YogaEvaluator {

    override val category: YogaCategory = YogaCategory.DHANA_YOGA

    override fun evaluate(chart: VedicChart): List<Yoga> {
        val yogas = mutableListOf<Yoga>()
        val ascendantSign = ZodiacSign.fromLongitude(chart.ascendant)
        val houseLords = YogaHelpers.getHouseLords(ascendantSign)

        // Dhana houses: 2 (wealth), 5 (past merit), 9 (fortune), 11 (gains)
        val dhanaHouses = listOf(2, 5, 9, 11)
        val dhanaLords = dhanaHouses.mapNotNull { houseLords[it] }

        // 1. Basic Dhana Yoga: Lords of 2, 5, 9, 11 in conjunction
        yogas.addAll(evaluateBasicDhanaYogas(chart, dhanaLords))

        // 2. Lakshmi Yoga
        evaluateLakshmiYoga(chart)?.let { yogas.add(it) }

        // 3. Kubera Yoga
        evaluateKuberaYoga(chart)?.let { yogas.add(it) }

        // 4. Chandra-Mangala Yoga
        evaluateChandraMangalaYoga(chart)?.let { yogas.add(it) }

        // 5. Labha Yoga
        evaluateLabhaYoga(chart, houseLords)?.let { yogas.add(it) }

        return yogas
    }

    /**
     * Evaluate basic Dhana Yogas from wealth house lord conjunctions
     */
    private fun evaluateBasicDhanaYogas(
        chart: VedicChart,
        dhanaLords: List<Planet>
    ): List<Yoga> {
        val yogas = mutableListOf<Yoga>()

        dhanaLords.forEachIndexed { i, lord1 ->
            dhanaLords.drop(i + 1).forEach { lord2 ->
                val pos1 = chart.planetPositions.find { it.planet == lord1 }
                val pos2 = chart.planetPositions.find { it.planet == lord2 }

                if (pos1 != null && pos2 != null && YogaHelpers.areConjunct(pos1, pos2)) {
                    val strength = YogaHelpers.calculateYogaStrength(chart, listOf(pos1, pos2))
                    yogas.add(
                        Yoga(
                            name = "${lord1.displayName}-${lord2.displayName} Dhana Yoga",
                            sanskritName = "Dhana Yoga",
                            category = YogaCategory.DHANA_YOGA,
                            planets = listOf(lord1, lord2),
                            houses = listOf(pos1.house, pos2.house),
                            description = "Lords of wealth houses in conjunction",
                            effects = "Wealth accumulation through ${YogaHelpers.getHouseSignifications(pos1.house)}",
                            strength = YogaHelpers.strengthFromPercentage(strength),
                            strengthPercentage = strength,
                            isAuspicious = true,
                            activationPeriod = "${lord1.displayName} or ${lord2.displayName} Dasha",
                            cancellationFactors = listOf("Combustion", "Debilitation without cancellation")
                        )
                    )
                }
            }
        }

        return yogas
    }

    /**
     * Evaluate Lakshmi Yoga - Venus in own/exalted sign in Kendra/Trikona
     */
    private fun evaluateLakshmiYoga(chart: VedicChart): Yoga? {
        val venusPos = chart.planetPositions.find { it.planet == Planet.VENUS } ?: return null

        val isStrong = YogaHelpers.isInOwnSign(venusPos) || YogaHelpers.isExalted(venusPos)
        val isGoodHouse = venusPos.house in listOf(1, 4, 5, 7, 9, 10)

        if (isStrong && isGoodHouse) {
            val strength = YogaHelpers.calculateYogaStrength(chart, listOf(venusPos)) * 1.2
            return Yoga(
                name = "Lakshmi Yoga",
                sanskritName = "Lakshmi Yoga",
                category = YogaCategory.DHANA_YOGA,
                planets = listOf(Planet.VENUS),
                houses = listOf(venusPos.house),
                description = "Venus in own/exalted sign in Kendra/Trikona",
                effects = "Blessed by Goddess Lakshmi, abundant wealth, luxury, beauty, artistic success",
                strength = YogaHelpers.strengthFromPercentage(strength),
                strengthPercentage = strength,
                isAuspicious = true,
                activationPeriod = "Venus Mahadasha and Antardashas",
                cancellationFactors = listOf("Affliction by malefics")
            )
        }

        return null
    }

    /**
     * Evaluate Kubera Yoga - Jupiter in 2nd with Mercury
     */
    private fun evaluateKuberaYoga(chart: VedicChart): Yoga? {
        val jupiterPos = chart.planetPositions.find { it.planet == Planet.JUPITER } ?: return null
        val mercuryPos = chart.planetPositions.find { it.planet == Planet.MERCURY } ?: return null

        if (jupiterPos.house == 2 && YogaHelpers.areConjunct(jupiterPos, mercuryPos)) {
            val strength = YogaHelpers.calculateYogaStrength(chart, listOf(jupiterPos, mercuryPos))
            return Yoga(
                name = "Kubera Yoga",
                sanskritName = "Kubera Yoga",
                category = YogaCategory.DHANA_YOGA,
                planets = listOf(Planet.JUPITER, Planet.MERCURY),
                houses = listOf(2),
                description = "Jupiter and Mercury in 2nd house",
                effects = "Treasury of wealth like Kubera, excellent financial acumen, banking success",
                strength = YogaHelpers.strengthFromPercentage(strength),
                strengthPercentage = strength,
                isAuspicious = true,
                activationPeriod = "Jupiter-Mercury periods",
                cancellationFactors = emptyList()
            )
        }

        return null
    }

    /**
     * Evaluate Chandra-Mangala Yoga - Moon-Mars conjunction for wealth
     */
    private fun evaluateChandraMangalaYoga(chart: VedicChart): Yoga? {
        val moonPos = chart.planetPositions.find { it.planet == Planet.MOON } ?: return null
        val marsPos = chart.planetPositions.find { it.planet == Planet.MARS } ?: return null

        if (YogaHelpers.areConjunct(moonPos, marsPos)) {
            val strength = YogaHelpers.calculateYogaStrength(chart, listOf(moonPos, marsPos))
            return Yoga(
                name = "Chandra-Mangala Yoga",
                sanskritName = "Chandra-Mangala Yoga",
                category = YogaCategory.DHANA_YOGA,
                planets = listOf(Planet.MOON, Planet.MARS),
                houses = listOf(moonPos.house),
                description = "Moon and Mars in conjunction",
                effects = "Wealth through business, enterprise, real estate, aggressive financial pursuits",
                strength = YogaHelpers.strengthFromPercentage(strength),
                strengthPercentage = strength,
                isAuspicious = true,
                activationPeriod = "Moon-Mars periods",
                cancellationFactors = listOf("In 6th, 8th, or 12th house reduces results")
            )
        }

        return null
    }

    /**
     * Evaluate Labha Yoga - 11th lord well-placed
     */
    private fun evaluateLabhaYoga(chart: VedicChart, houseLords: Map<Int, Planet>): Yoga? {
        val lord11 = houseLords[11] ?: return null
        val lord11Pos = chart.planetPositions.find { it.planet == lord11 } ?: return null

        if (lord11Pos.house in listOf(1, 2, 5, 9, 10, 11)) {
            val strength = YogaHelpers.calculateYogaStrength(chart, listOf(lord11Pos))
            return Yoga(
                name = "Labha Yoga",
                sanskritName = "Labha Yoga",
                category = YogaCategory.DHANA_YOGA,
                planets = listOf(lord11),
                houses = listOf(lord11Pos.house),
                description = "11th lord well-placed",
                effects = "Continuous gains, fulfillment of desires, income through ${YogaHelpers.getHouseSignifications(lord11Pos.house)}",
                strength = YogaHelpers.strengthFromPercentage(strength),
                strengthPercentage = strength,
                isAuspicious = true,
                activationPeriod = "${lord11.displayName} Dasha",
                cancellationFactors = emptyList()
            )
        }

        return null
    }
}
