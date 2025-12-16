package com.astro.storm.ephemeris.yoga

import com.astro.storm.data.model.Planet
import com.astro.storm.data.model.VedicChart
import com.astro.storm.data.model.ZodiacSign

/**
 * Raja Yoga Calculator
 *
 * Raja Yogas are formed by the association of Kendra lords (1,4,7,10)
 * and Trikona lords (1,5,9). These yogas bestow power, authority, and
 * leadership positions.
 *
 * Types calculated:
 * 1. Kendra-Trikona Raja Yoga - Basic Raja Yoga through conjunction/aspect/exchange
 * 2. Parivartana Raja Yoga - Through mutual exchange
 * 3. Viparita Raja Yoga - Lords of 6,8,12 in each other's houses
 * 4. Neecha Bhanga Raja Yoga - Debilitation cancellation leading to rise
 * 5. Maha Raja Yoga - Jupiter and Venus in Kendra from Moon
 *
 * Reference: Brihat Parasara Hora Shastra (BPHS), Chapter 41
 *
 * @author AstroStorm - Ultra-Precision Vedic Astrology
 */
object RajaYogaCalculator {

    /**
     * Calculate all Raja Yogas in the chart
     */
    fun calculate(chart: VedicChart): List<Yoga> {
        val yogas = mutableListOf<Yoga>()
        val ascendantSign = ZodiacSign.fromLongitude(chart.ascendant)

        val houseLords = YogaUtils.getHouseLords(ascendantSign)
        if (houseLords.isEmpty()) {
            return yogas
        }

        val kendraLords = listOf(houseLords[1], houseLords[4], houseLords[7], houseLords[10]).filterNotNull()
        val trikonaLords = listOf(houseLords[1], houseLords[5], houseLords[9]).filterNotNull()

        // 1. Kendra-Trikona Raja Yoga
        yogas.addAll(calculateKendraTrikonaYogas(chart, kendraLords, trikonaLords))

        // 2. Viparita Raja Yoga
        yogas.addAll(calculateViparitaRajaYogas(chart, houseLords))

        // 3. Neecha Bhanga Raja Yoga
        yogas.addAll(calculateNeechaBhangaRajaYogas(chart))

        // 4. Maha Raja Yoga
        calculateMahaRajaYoga(chart)?.let { yogas.add(it) }

        return yogas
    }

    /**
     * Calculate Kendra-Trikona Raja Yogas
     */
    private fun calculateKendraTrikonaYogas(
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
                        // Conjunction
                        if (YogaUtils.areConjunct(kendraPos, trikonaPos)) {
                            val strength = YogaUtils.calculateYogaStrength(chart, listOf(kendraPos, trikonaPos))
                            yogas.add(createKendraTrikonaRajaYoga(kendraLord, trikonaLord, "conjunction", strength, chart))
                        }

                        // Mutual aspect
                        if (YogaUtils.areMutuallyAspecting(kendraPos, trikonaPos)) {
                            val strength = YogaUtils.calculateYogaStrength(chart, listOf(kendraPos, trikonaPos)) * 0.8
                            yogas.add(createKendraTrikonaRajaYoga(kendraLord, trikonaLord, "aspect", strength, chart))
                        }

                        // Exchange (Parivartana)
                        if (YogaUtils.areInExchange(kendraPos, trikonaPos)) {
                            val strength = YogaUtils.calculateYogaStrength(chart, listOf(kendraPos, trikonaPos)) * 1.2
                            yogas.add(createParivartanaRajaYoga(kendraLord, trikonaLord, strength, chart))
                        }
                    }
                }
            }
        }

        return yogas
    }

    /**
     * Calculate Viparita Raja Yogas
     * Formed when lords of 6, 8, 12 are connected
     */
    private fun calculateViparitaRajaYogas(
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
                    if (YogaUtils.areInExchange(pos1, pos2) || YogaUtils.areConjunct(pos1, pos2)) {
                        val strength = YogaUtils.calculateYogaStrength(chart, listOf(pos1, pos2)) * 0.7
                        yogas.add(createViparitaRajaYoga(lord1, lord2, strength, chart))
                    }
                }
            }
        }

        return yogas
    }

    /**
     * Calculate Neecha Bhanga Raja Yogas
     * Formed when debilitated planets have their debilitation cancelled
     */
    private fun calculateNeechaBhangaRajaYogas(chart: VedicChart): List<Yoga> {
        val yogas = mutableListOf<Yoga>()

        chart.planetPositions.forEach { pos ->
            if (YogaUtils.isDebilitated(pos)) {
                if (YogaUtils.hasNeechaBhanga(pos, chart)) {
                    val strength = YogaUtils.calculateYogaStrength(chart, listOf(pos))
                    yogas.add(createNeechaBhangaRajaYoga(pos.planet, strength, chart))
                }
            }
        }

        return yogas
    }

    /**
     * Calculate Maha Raja Yoga
     * Jupiter and Venus in Kendra from Moon
     */
    private fun calculateMahaRajaYoga(chart: VedicChart): Yoga? {
        val moonPos = chart.planetPositions.find { it.planet == Planet.MOON } ?: return null
        val jupiterPos = chart.planetPositions.find { it.planet == Planet.JUPITER } ?: return null
        val venusPos = chart.planetPositions.find { it.planet == Planet.VENUS } ?: return null

        if (YogaUtils.isInKendraFrom(jupiterPos, moonPos) && YogaUtils.isInKendraFrom(venusPos, moonPos)) {
            val strength = YogaUtils.calculateYogaStrength(chart, listOf(jupiterPos, venusPos, moonPos))
            return Yoga(
                name = "Maha Raja Yoga",
                sanskritName = "Maha Raja Yoga",
                category = YogaCategory.RAJA_YOGA,
                planets = listOf(Planet.JUPITER, Planet.VENUS, Planet.MOON),
                houses = listOf(jupiterPos.house, venusPos.house),
                description = "Jupiter and Venus in Kendra from Moon",
                effects = "Exceptional fortune, royal status, widespread fame, great wealth and power",
                strength = YogaUtils.strengthFromPercentage(strength),
                strengthPercentage = strength,
                isAuspicious = true,
                activationPeriod = "Jupiter and Venus Dashas",
                cancellationFactors = emptyList()
            )
        }

        return null
    }

    // ==================== YOGA FACTORY FUNCTIONS ====================

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

        val (cancellationFactor, cancellationReasons) = YogaUtils.calculateCancellationFactor(positions, chart)
        val adjustedStrength = (baseStrength * cancellationFactor).coerceIn(10.0, 100.0)

        return Yoga(
            name = "Kendra-Trikona Raja Yoga",
            sanskritName = "Kendra-Trikona Raja Yoga",
            category = YogaCategory.RAJA_YOGA,
            planets = listOf(kendraLord, trikonaLord),
            houses = listOfNotNull(kendraPos?.house, trikonaPos?.house),
            description = "${kendraLord.displayName} (Kendra lord) and ${trikonaLord.displayName} (Trikona lord) in $type",
            effects = "Rise to power and authority, leadership position, recognition from government",
            strength = YogaUtils.strengthFromPercentage(adjustedStrength),
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

        val (cancellationFactor, cancellationReasons) = YogaUtils.calculateCancellationFactor(positions, chart)
        val adjustedStrength = (baseStrength * cancellationFactor).coerceIn(10.0, 100.0)

        return Yoga(
            name = "Parivartana Raja Yoga",
            sanskritName = "Parivartana Raja Yoga",
            category = YogaCategory.RAJA_YOGA,
            planets = listOf(planet1, planet2),
            houses = listOfNotNull(pos1?.house, pos2?.house),
            description = "Exchange between ${planet1.displayName} and ${planet2.displayName}",
            effects = "Strong Raja Yoga through mutual exchange, stable rise to power, lasting authority",
            strength = YogaUtils.strengthFromPercentage(adjustedStrength),
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

        val (cancellationFactor, cancellationReasons) = YogaUtils.calculateCancellationFactor(positions, chart)
        val adjustedStrength = (baseStrength * cancellationFactor).coerceIn(10.0, 100.0)

        val specificReasons = cancellationReasons.toMutableList()
        positions.forEach { pos ->
            if (YogaUtils.isExalted(pos) || YogaUtils.isInOwnSign(pos)) {
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
            strength = YogaUtils.strengthFromPercentage(adjustedStrength),
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
            val combustionFactor = YogaUtils.getCombustionFactor(pos, chart)
            if (combustionFactor < 0.9) {
                adjustedStrength *= combustionFactor
                if (combustionFactor < 0.6) {
                    cancellationReasons.add("${planet.displayName} is combust - Neecha Bhanga weakened")
                }
            }

            val afflictionFactor = YogaUtils.getMaleficAfflictionFactor(pos, chart)
            if (afflictionFactor < 0.85) {
                adjustedStrength *= afflictionFactor
                cancellationReasons.add("Malefic aspects reduce yoga effectiveness")
            }

            if (YogaUtils.isPapakartari(pos, chart)) {
                adjustedStrength *= 0.8
                cancellationReasons.add("Planet hemmed between malefics")
            }

            val beneficBoost = YogaUtils.getBeneficAspectBoost(pos, chart)
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
            strength = YogaUtils.strengthFromPercentage(adjustedStrength.coerceIn(10.0, 100.0)),
            strengthPercentage = adjustedStrength.coerceIn(10.0, 100.0),
            isAuspicious = true,
            activationPeriod = "${planet.displayName} Dasha",
            cancellationFactors = cancellationReasons.ifEmpty { listOf("Clean Neecha Bhanga - yoga operates fully") }
        )
    }
}
