package com.astro.storm.ephemeris.yoga

import com.astro.storm.core.model.Planet
import com.astro.storm.core.model.PlanetPosition
import com.astro.storm.core.model.VedicChart
import com.astro.storm.core.model.ZodiacSign

/**
 * Raja Yoga Evaluator - Power and Authority Combinations
 *
 * Raja Yogas are formed by the association of Kendra lords (1,4,7,10) and Trikona lords (1,5,9).
 * These yogas indicate power, authority, and leadership potential.
 *
 * Types evaluated:
 * 1. Kendra-Trikona Raja Yoga - Conjunction/aspect/exchange of Kendra and Trikona lords
 * 2. Parivartana Raja Yoga - Exchange between benefic house lords
 * 3. Viparita Raja Yoga - Lords of 6, 8, 12 in each other's houses
 * 4. Neecha Bhanga Raja Yoga - Cancellation of debilitation creating power
 * 5. Maha Raja Yoga - Jupiter and Venus in Kendra from Moon
 *
 * Based on:
 * - Brihat Parasara Hora Shastra (BPHS), Chapter 41
 * - Phaladeepika, Chapter 6
 *
 * @author AstroStorm
 */
class RajaYogaEvaluator : YogaEvaluator {

    override val category: YogaCategory = YogaCategory.RAJA_YOGA

    override fun evaluate(chart: VedicChart): List<Yoga> {
        val yogas = mutableListOf<Yoga>()
        val ascendantSign = ZodiacSign.fromLongitude(chart.ascendant)

        // Get house lords
        val houseLords = YogaHelpers.getHouseLords(ascendantSign)
        if (houseLords.isEmpty()) {
            return yogas
        }

        val kendraLords = listOf(houseLords[1], houseLords[4], houseLords[7], houseLords[10]).filterNotNull()
        val trikonaLords = listOf(houseLords[1], houseLords[5], houseLords[9]).filterNotNull()

        // 1. Kendra-Trikona Raja Yoga
        yogas.addAll(evaluateKendraTrikonaYogas(chart, kendraLords, trikonaLords))

        // 2. Viparita Raja Yoga
        yogas.addAll(evaluateViparitaRajaYogas(chart, houseLords))

        // 3. Neecha Bhanga Raja Yoga
        yogas.addAll(evaluateNeechaBhangaYogas(chart))

        // 4. Maha Raja Yoga
        yogas.addAll(evaluateMahaRajaYoga(chart))

        return yogas
    }

    /**
     * Evaluate Kendra-Trikona Raja Yogas
     */
    private fun evaluateKendraTrikonaYogas(
        chart: VedicChart,
        kendraLords: List<Planet>,
        trikonaLords: List<Planet>
    ): List<Yoga> {
        val yogas = mutableListOf<Yoga>()

        kendraLords.forEach { kendraLord ->
            trikonaLords.forEach { trikonaLord ->
                if (kendraLord != trikonaLord) {
                    val kendraPos = chart.planetPositions.find { it.planet == kendraLord }
                    val trikonaPos = chart.planetPositions.find { it.planet == trikonaLord }

                    if (kendraPos != null && trikonaPos != null) {
                        // Check for conjunction
                        if (YogaHelpers.areConjunct(kendraPos, trikonaPos)) {
                            val strength = YogaHelpers.calculateYogaStrength(chart, listOf(kendraPos, trikonaPos))
                            yogas.add(createKendraTrikonaRajaYoga(
                                kendraLord, trikonaLord, "conjunction", strength, chart
                            ))
                        }

                        // Check for mutual aspect
                        if (YogaHelpers.areMutuallyAspecting(kendraPos, trikonaPos)) {
                            val strength = YogaHelpers.calculateYogaStrength(chart, listOf(kendraPos, trikonaPos)) * 0.8
                            yogas.add(createKendraTrikonaRajaYoga(
                                kendraLord, trikonaLord, "aspect", strength, chart
                            ))
                        }

                        // Check for exchange (Parivartana)
                        if (YogaHelpers.areInExchange(kendraPos, trikonaPos)) {
                            val strength = YogaHelpers.calculateYogaStrength(chart, listOf(kendraPos, trikonaPos)) * 1.2
                            yogas.add(createParivartanaRajaYoga(kendraLord, trikonaLord, strength, chart))
                        }
                    }
                }
            }
        }

        return yogas
    }

    /**
     * Evaluate Viparita Raja Yogas (lords of 6, 8, 12 in each other's houses)
     */
    private fun evaluateViparitaRajaYogas(
        chart: VedicChart,
        houseLords: Map<Int, Planet>
    ): List<Yoga> {
        val yogas = mutableListOf<Yoga>()

        val dusthanaLords = listOf(houseLords[6], houseLords[8], houseLords[12]).filterNotNull()
        dusthanaLords.forEachIndexed { i, lord1 ->
            dusthanaLords.drop(i + 1).forEach { lord2 ->
                val pos1 = chart.planetPositions.find { it.planet == lord1 }
                val pos2 = chart.planetPositions.find { it.planet == lord2 }

                if (pos1 != null && pos2 != null) {
                    if (YogaHelpers.areInExchange(pos1, pos2) || YogaHelpers.areConjunct(pos1, pos2)) {
                        val strength = YogaHelpers.calculateYogaStrength(chart, listOf(pos1, pos2)) * 0.7
                        yogas.add(createViparitaRajaYoga(lord1, lord2, strength, chart))
                    }
                }
            }
        }

        return yogas
    }

    /**
     * Evaluate Neecha Bhanga Raja Yogas
     */
    private fun evaluateNeechaBhangaYogas(chart: VedicChart): List<Yoga> {
        val yogas = mutableListOf<Yoga>()

        chart.planetPositions.forEach { pos ->
            if (YogaHelpers.isDebilitated(pos)) {
                if (YogaHelpers.hasNeechaBhanga(pos, chart)) {
                    val strength = YogaHelpers.calculateYogaStrength(chart, listOf(pos))
                    yogas.add(createNeechaBhangaRajaYoga(pos.planet, strength, chart))
                }
            }
        }

        return yogas
    }

    /**
     * Evaluate Maha Raja Yoga (Jupiter and Venus in Kendra from Moon)
     */
    private fun evaluateMahaRajaYoga(chart: VedicChart): List<Yoga> {
        val yogas = mutableListOf<Yoga>()

        val moonPos = chart.planetPositions.find { it.planet == Planet.MOON }
        val jupiterPos = chart.planetPositions.find { it.planet == Planet.JUPITER }
        val venusPos = chart.planetPositions.find { it.planet == Planet.VENUS }

        if (moonPos != null && jupiterPos != null && venusPos != null) {
            if (YogaHelpers.isInKendraFrom(jupiterPos, moonPos) &&
                YogaHelpers.isInKendraFrom(venusPos, moonPos)) {
                val strength = YogaHelpers.calculateYogaStrength(chart, listOf(jupiterPos, venusPos, moonPos))
                yogas.add(
                    Yoga(
                        name = "Maha Raja Yoga",
                        sanskritName = "Maha Raja Yoga",
                        category = YogaCategory.RAJA_YOGA,
                        planets = listOf(Planet.JUPITER, Planet.VENUS, Planet.MOON),
                        houses = listOf(jupiterPos.house, venusPos.house),
                        description = "Jupiter and Venus in Kendra from Moon",
                        effects = "Exceptional fortune, royal status, widespread fame, great wealth and power",
                        strength = YogaHelpers.strengthFromPercentage(strength),
                        strengthPercentage = strength,
                        isAuspicious = true,
                        activationPeriod = "Jupiter and Venus Dashas",
                        cancellationFactors = emptyList()
                    )
                )
            }
        }

        return yogas
    }

    // ==================== YOGA CREATION HELPERS ====================

    private fun createKendraTrikonaRajaYoga(
        kendraLord: Planet,
        trikonaLord: Planet,
        type: String,
        baseStrength: Double,
        chart: VedicChart
    ): Yoga {
        val kendraPos = chart.planetPositions.find { it.planet == kendraLord }
        val trikonaPos = chart.planetPositions.find { it.planet == trikonaLord }
        val positions = listOfNotNull(kendraPos, trikonaPos)

        val (cancellationFactor, cancellationReasons) = YogaHelpers.calculateCancellationFactor(positions, chart)
        val adjustedStrength = (baseStrength * cancellationFactor).coerceIn(10.0, 100.0)

        return Yoga(
            name = "Kendra-Trikona Raja Yoga",
            sanskritName = "Kendra-Trikona Raja Yoga",
            category = YogaCategory.RAJA_YOGA,
            planets = listOf(kendraLord, trikonaLord),
            houses = listOfNotNull(kendraPos?.house, trikonaPos?.house),
            description = "${kendraLord.displayName} (Kendra lord) and ${trikonaLord.displayName} (Trikona lord) in $type",
            effects = "Rise to power and authority, leadership position, recognition from government",
            strength = YogaHelpers.strengthFromPercentage(adjustedStrength),
            strengthPercentage = adjustedStrength,
            isAuspicious = true,
            activationPeriod = "${kendraLord.displayName}-${trikonaLord.displayName} Dasha/Antardasha",
            cancellationFactors = cancellationReasons.ifEmpty { listOf("None - yoga is unafflicted") }
        )
    }

    private fun createParivartanaRajaYoga(
        planet1: Planet,
        planet2: Planet,
        baseStrength: Double,
        chart: VedicChart
    ): Yoga {
        val pos1 = chart.planetPositions.find { it.planet == planet1 }
        val pos2 = chart.planetPositions.find { it.planet == planet2 }
        val positions = listOfNotNull(pos1, pos2)

        val (cancellationFactor, cancellationReasons) = YogaHelpers.calculateCancellationFactor(positions, chart)
        val adjustedStrength = (baseStrength * cancellationFactor).coerceIn(10.0, 100.0)

        return Yoga(
            name = "Parivartana Raja Yoga",
            sanskritName = "Parivartana Raja Yoga",
            category = YogaCategory.RAJA_YOGA,
            planets = listOf(planet1, planet2),
            houses = listOfNotNull(pos1?.house, pos2?.house),
            description = "Exchange between ${planet1.displayName} and ${planet2.displayName}",
            effects = "Strong Raja Yoga through mutual exchange, stable rise to power, lasting authority",
            strength = YogaHelpers.strengthFromPercentage(adjustedStrength),
            strengthPercentage = adjustedStrength,
            isAuspicious = true,
            activationPeriod = "${planet1.displayName} and ${planet2.displayName} Dashas",
            cancellationFactors = cancellationReasons.ifEmpty { listOf("None - yoga is unafflicted") }
        )
    }

    private fun createViparitaRajaYoga(
        planet1: Planet,
        planet2: Planet,
        baseStrength: Double,
        chart: VedicChart
    ): Yoga {
        val pos1 = chart.planetPositions.find { it.planet == planet1 }
        val pos2 = chart.planetPositions.find { it.planet == planet2 }
        val positions = listOfNotNull(pos1, pos2)

        val (cancellationFactor, cancellationReasons) = YogaHelpers.calculateCancellationFactor(positions, chart)
        val adjustedStrength = (baseStrength * cancellationFactor).coerceIn(10.0, 100.0)

        // Add Viparita-specific consideration
        val specificReasons = cancellationReasons.toMutableList()
        positions.forEach { pos ->
            if (YogaHelpers.isExalted(pos) || YogaHelpers.isInOwnSign(pos)) {
                specificReasons.add("${pos.planet.displayName} is strong - Viparita results may be modified")
            }
        }

        return Yoga(
            name = "Viparita Raja Yoga",
            sanskritName = "Viparita Raja Yoga",
            category = YogaCategory.RAJA_YOGA,
            planets = listOf(planet1, planet2),
            houses = positions.map { it.house },
            description = "Lords of Dusthanas (6,8,12) connected",
            effects = "Rise through fall of enemies, sudden fortune from unexpected sources, gains through others' losses",
            strength = YogaHelpers.strengthFromPercentage(adjustedStrength),
            strengthPercentage = adjustedStrength,
            isAuspicious = true,
            activationPeriod = "${planet1.displayName}-${planet2.displayName} periods",
            cancellationFactors = specificReasons.ifEmpty { listOf("None - yoga is unafflicted") }
        )
    }

    private fun createNeechaBhangaRajaYoga(
        planet: Planet,
        baseStrength: Double,
        chart: VedicChart
    ): Yoga {
        val pos = chart.planetPositions.find { it.planet == planet }
        val cancellationReasons = mutableListOf<String>()
        var adjustedStrength = baseStrength

        if (pos != null) {
            // Check combustion
            val combustionFactor = YogaHelpers.getCombustionFactor(pos, chart)
            if (combustionFactor < 0.9) {
                adjustedStrength *= combustionFactor
                if (combustionFactor < 0.6) {
                    cancellationReasons.add("${planet.displayName} is combust - Neecha Bhanga weakened")
                }
            }

            // Check malefic aspects
            val afflictionFactor = YogaHelpers.getMaleficAfflictionFactor(pos, chart)
            if (afflictionFactor < 0.85) {
                adjustedStrength *= afflictionFactor
                cancellationReasons.add("Malefic aspects reduce yoga effectiveness")
            }

            // Check Papakartari
            if (YogaHelpers.isPapakartari(pos, chart)) {
                adjustedStrength *= 0.8
                cancellationReasons.add("Planet hemmed between malefics")
            }

            // Benefic aspects boost
            val beneficBoost = YogaHelpers.getBeneficAspectBoost(pos, chart)
            if (beneficBoost > 1.0) {
                adjustedStrength *= beneficBoost
            }

            // Identify cancellation type
            if (pos.house in listOf(1, 4, 7, 10)) {
                cancellationReasons.add(0, "Neecha Bhanga via Kendra placement")
            } else {
                val debilitatedSignLord = pos.sign.ruler
                val lordPos = chart.planetPositions.find { it.planet == debilitatedSignLord }
                if (lordPos != null && lordPos.house in listOf(1, 4, 7, 10)) {
                    cancellationReasons.add(0, "Neecha Bhanga via sign lord in Kendra")
                }
            }
        }

        return Yoga(
            name = "Neecha Bhanga Raja Yoga",
            sanskritName = "Neecha Bhanga Raja Yoga",
            category = YogaCategory.RAJA_YOGA,
            planets = listOf(planet),
            houses = listOfNotNull(pos?.house),
            description = "${planet.displayName} debilitated but with cancellation",
            effects = "Rise from humble beginnings, success after initial struggles, respected leader",
            strength = YogaHelpers.strengthFromPercentage(adjustedStrength.coerceIn(10.0, 100.0)),
            strengthPercentage = adjustedStrength.coerceIn(10.0, 100.0),
            isAuspicious = true,
            activationPeriod = "${planet.displayName} Dasha",
            cancellationFactors = cancellationReasons.ifEmpty { listOf("Clean Neecha Bhanga - yoga operates fully") }
        )
    }
}
