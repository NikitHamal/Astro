package com.astro.storm.ephemeris.yoga

import com.astro.storm.data.model.Planet
import com.astro.storm.data.model.VedicChart
import com.astro.storm.data.model.ZodiacSign

/**
 * Dhana Yoga Calculator
 *
 * Dhana Yogas are wealth-giving combinations formed by the association
 * of wealth-producing house lords (2, 5, 9, 11).
 *
 * Types calculated:
 * 1. Basic Dhana Yoga - Lords of 2, 5, 9, 11 in conjunction
 * 2. Lakshmi Yoga - Venus in own/exalted sign in Kendra/Trikona
 * 3. Kubera Yoga - Jupiter in 2nd with Mercury
 * 4. Chandra-Mangala Yoga - Moon-Mars conjunction for business wealth
 * 5. Labha Yoga - 11th lord well-placed
 *
 * Reference: BPHS Chapter 42, Phaladeepika
 *
 * @author AstroStorm - Ultra-Precision Vedic Astrology
 */
object DhanaYogaCalculator {

    /**
     * Calculate all Dhana (Wealth) Yogas in the chart
     */
    fun calculate(chart: VedicChart): List<Yoga> {
        val yogas = mutableListOf<Yoga>()
        val ascendantSign = ZodiacSign.fromLongitude(chart.ascendant)
        val houseLords = YogaUtils.getHouseLords(ascendantSign)

        // Dhana houses: 2 (wealth), 5 (past merit), 9 (fortune), 11 (gains)
        val dhanaHouses = listOf(2, 5, 9, 11)
        val dhanaLords = dhanaHouses.mapNotNull { houseLords[it] }

        // 1. Basic Dhana Yoga - Lords of wealth houses in conjunction
        yogas.addAll(calculateBasicDhanaYogas(chart, dhanaLords))

        // 2. Lakshmi Yoga
        calculateLakshmiYoga(chart)?.let { yogas.add(it) }

        // 3. Kubera Yoga
        calculateKuberaYoga(chart)?.let { yogas.add(it) }

        // 4. Chandra-Mangala Yoga
        calculateChandraMangalaYoga(chart)?.let { yogas.add(it) }

        // 5. Labha Yoga
        calculateLabhaYoga(chart, houseLords)?.let { yogas.add(it) }

        return yogas
    }

    /**
     * Calculate basic Dhana Yogas from dhana lord conjunctions
     */
    private fun calculateBasicDhanaYogas(chart: VedicChart, dhanaLords: List<Planet>): List<Yoga> {
        val yogas = mutableListOf<Yoga>()

        dhanaLords.forEachIndexed { i, lord1 ->
            dhanaLords.drop(i + 1).forEach { lord2 ->
                val pos1 = chart.planetPositions.find { it.planet == lord1 }
                val pos2 = chart.planetPositions.find { it.planet == lord2 }

                if (pos1 != null && pos2 != null && YogaUtils.areConjunct(pos1, pos2)) {
                    val strength = YogaUtils.calculateYogaStrength(chart, listOf(pos1, pos2))
                    yogas.add(
                        Yoga(
                            name = "${lord1.displayName}-${lord2.displayName} Dhana Yoga",
                            sanskritName = "Dhana Yoga",
                            category = YogaCategory.DHANA_YOGA,
                            planets = listOf(lord1, lord2),
                            houses = listOf(pos1.house, pos2.house),
                            description = "Lords of wealth houses in conjunction",
                            effects = "Wealth accumulation through ${YogaLocalization.getHouseSignifications(pos1.house)}",
                            strength = YogaUtils.strengthFromPercentage(strength),
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
     * Calculate Lakshmi Yoga
     * Venus in own/exalted sign in Kendra/Trikona
     */
    private fun calculateLakshmiYoga(chart: VedicChart): Yoga? {
        val venusPos = chart.planetPositions.find { it.planet == Planet.VENUS } ?: return null

        val isStrong = YogaUtils.isInOwnSign(venusPos) || YogaUtils.isExalted(venusPos)
        val isGoodHouse = venusPos.house in listOf(1, 4, 5, 7, 9, 10)

        if (isStrong && isGoodHouse) {
            val strength = YogaUtils.calculateYogaStrength(chart, listOf(venusPos)) * 1.2
            return Yoga(
                name = "Lakshmi Yoga",
                sanskritName = "Lakshmi Yoga",
                category = YogaCategory.DHANA_YOGA,
                planets = listOf(Planet.VENUS),
                houses = listOf(venusPos.house),
                description = "Venus in own/exalted sign in Kendra/Trikona",
                effects = "Blessed by Goddess Lakshmi, abundant wealth, luxury, beauty, artistic success",
                strength = YogaUtils.strengthFromPercentage(strength),
                strengthPercentage = strength,
                isAuspicious = true,
                activationPeriod = "Venus Mahadasha and Antardashas",
                cancellationFactors = listOf("Affliction by malefics")
            )
        }

        return null
    }

    /**
     * Calculate Kubera Yoga
     * Jupiter in 2nd house with Mercury
     */
    private fun calculateKuberaYoga(chart: VedicChart): Yoga? {
        val jupiterPos = chart.planetPositions.find { it.planet == Planet.JUPITER } ?: return null
        val mercuryPos = chart.planetPositions.find { it.planet == Planet.MERCURY } ?: return null

        if (jupiterPos.house == 2 && YogaUtils.areConjunct(jupiterPos, mercuryPos)) {
            val strength = YogaUtils.calculateYogaStrength(chart, listOf(jupiterPos, mercuryPos))
            return Yoga(
                name = "Kubera Yoga",
                sanskritName = "Kubera Yoga",
                category = YogaCategory.DHANA_YOGA,
                planets = listOf(Planet.JUPITER, Planet.MERCURY),
                houses = listOf(2),
                description = "Jupiter and Mercury in 2nd house",
                effects = "Treasury of wealth like Kubera, excellent financial acumen, banking success",
                strength = YogaUtils.strengthFromPercentage(strength),
                strengthPercentage = strength,
                isAuspicious = true,
                activationPeriod = "Jupiter-Mercury periods",
                cancellationFactors = emptyList()
            )
        }

        return null
    }

    /**
     * Calculate Chandra-Mangala Yoga
     * Moon-Mars conjunction for wealth through enterprise
     */
    private fun calculateChandraMangalaYoga(chart: VedicChart): Yoga? {
        val moonPos = chart.planetPositions.find { it.planet == Planet.MOON } ?: return null
        val marsPos = chart.planetPositions.find { it.planet == Planet.MARS } ?: return null

        if (YogaUtils.areConjunct(moonPos, marsPos)) {
            val strength = YogaUtils.calculateYogaStrength(chart, listOf(moonPos, marsPos))
            return Yoga(
                name = "Chandra-Mangala Yoga",
                sanskritName = "Chandra-Mangala Yoga",
                category = YogaCategory.DHANA_YOGA,
                planets = listOf(Planet.MOON, Planet.MARS),
                houses = listOf(moonPos.house),
                description = "Moon and Mars in conjunction",
                effects = "Wealth through business, enterprise, real estate, aggressive financial pursuits",
                strength = YogaUtils.strengthFromPercentage(strength),
                strengthPercentage = strength,
                isAuspicious = true,
                activationPeriod = "Moon-Mars periods",
                cancellationFactors = listOf("In 6th, 8th, or 12th house reduces results")
            )
        }

        return null
    }

    /**
     * Calculate Labha Yoga
     * 11th lord well-placed in auspicious houses
     */
    private fun calculateLabhaYoga(chart: VedicChart, houseLords: Map<Int, Planet>): Yoga? {
        val lord11 = houseLords[11] ?: return null
        val lord11Pos = chart.planetPositions.find { it.planet == lord11 } ?: return null

        if (lord11Pos.house in listOf(1, 2, 5, 9, 10, 11)) {
            val strength = YogaUtils.calculateYogaStrength(chart, listOf(lord11Pos))
            return Yoga(
                name = "Labha Yoga",
                sanskritName = "Labha Yoga",
                category = YogaCategory.DHANA_YOGA,
                planets = listOf(lord11),
                houses = listOf(lord11Pos.house),
                description = "11th lord well-placed",
                effects = "Continuous gains, fulfillment of desires, income through ${YogaLocalization.getHouseSignifications(lord11Pos.house)}",
                strength = YogaUtils.strengthFromPercentage(strength),
                strengthPercentage = strength,
                isAuspicious = true,
                activationPeriod = "${lord11.displayName} Dasha",
                cancellationFactors = emptyList()
            )
        }

        return null
    }
}
