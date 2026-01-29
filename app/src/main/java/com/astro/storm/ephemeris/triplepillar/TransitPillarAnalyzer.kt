package com.astro.storm.ephemeris.triplepillar

import com.astro.storm.core.model.BirthData
import com.astro.storm.core.model.HouseSystem
import com.astro.storm.core.model.Planet
import com.astro.storm.core.model.PlanetPosition
import com.astro.storm.core.model.VedicChart
import com.astro.storm.core.model.ZodiacSign
import com.astro.storm.ephemeris.SwissEphemerisEngine
import com.astro.storm.ephemeris.VedicAstrologyUtils
import java.time.LocalDateTime

/**
 * Transit Pillar Analyzer
 *
 * Analyzes the Gochara (Transit) system as the second pillar of the Triple-Pillar
 * Predictive Engine. This evaluates:
 *
 * 1. Current planetary positions in the sky
 * 2. Transit positions from natal Moon (classical Gochara)
 * 3. Transit-to-natal aspects and conjunctions
 * 4. Vedha (obstruction) points that nullify favorable transits
 * 5. Special transit events (Saturn return, Jupiter transit, etc.)
 *
 * Gochara Rules (from Brihat Parashara Hora Shastra):
 * - Favorable houses from Moon differ for each planet
 * - Vedha points can obstruct favorable transits
 * - Slow-moving planets (Saturn, Jupiter, Rahu/Ketu) have more impact
 *
 * @author AstroStorm - Ultra-Precision Vedic Astrology
 */
object TransitPillarAnalyzer {

    /**
     * Favorable transit houses from Moon for each planet
     * Based on classical Gochara rules from BPHS and Phaladeepika
     */
    private val FAVORABLE_HOUSES_FROM_MOON = mapOf(
        Planet.SUN to setOf(3, 6, 10, 11),
        Planet.MOON to setOf(1, 3, 6, 7, 10, 11),
        Planet.MARS to setOf(3, 6, 11),
        Planet.MERCURY to setOf(2, 4, 6, 8, 10, 11),
        Planet.JUPITER to setOf(2, 5, 7, 9, 11),
        Planet.VENUS to setOf(1, 2, 3, 4, 5, 8, 9, 11, 12),
        Planet.SATURN to setOf(3, 6, 11),
        Planet.RAHU to setOf(3, 6, 10, 11),
        Planet.KETU to setOf(3, 6, 10, 11)
    )

    /**
     * Neutral transit houses from Moon
     */
    private val NEUTRAL_HOUSES_FROM_MOON = mapOf(
        Planet.SUN to setOf(1, 2, 5),
        Planet.MOON to setOf(2, 5),
        Planet.MARS to setOf(1, 10),
        Planet.MERCURY to setOf(1, 3, 5),
        Planet.JUPITER to setOf(1, 4, 6, 8, 10),
        Planet.VENUS to setOf(6, 7, 10),
        Planet.SATURN to setOf(1, 2, 10),
        Planet.RAHU to setOf(1, 2, 5),
        Planet.KETU to setOf(1, 2, 5)
    )

    /**
     * Vedha (Obstruction) points
     * Format: Map<FavorableHouse, VedhaHouse>
     * When a planet is in a favorable house but another planet is in its Vedha point,
     * the favorable results are obstructed
     */
    private val VEDHA_POINTS = mapOf(
        Planet.SUN to mapOf(3 to 9, 6 to 12, 10 to 4, 11 to 5),
        Planet.MOON to mapOf(1 to 5, 3 to 9, 6 to 12, 7 to 2, 10 to 4, 11 to 8),
        Planet.MARS to mapOf(3 to 12, 6 to 9, 11 to 5),
        Planet.MERCURY to mapOf(2 to 5, 4 to 3, 6 to 9, 8 to 1, 10 to 8, 11 to 12),
        Planet.JUPITER to mapOf(2 to 12, 5 to 4, 7 to 3, 9 to 10, 11 to 8),
        Planet.VENUS to mapOf(1 to 8, 2 to 7, 3 to 1, 4 to 10, 5 to 9, 8 to 5, 9 to 11, 11 to 6, 12 to 3),
        Planet.SATURN to mapOf(3 to 12, 6 to 9, 11 to 5)
    )

    /**
     * Transit impact weights based on planetary speed
     * Slower planets have more lasting impact
     */
    private val TRANSIT_IMPACT_WEIGHTS = mapOf(
        Planet.SATURN to 1.0,      // ~2.5 years per sign
        Planet.JUPITER to 0.85,    // ~1 year per sign
        Planet.RAHU to 0.80,       // ~1.5 years per sign
        Planet.KETU to 0.80,       // ~1.5 years per sign
        Planet.MARS to 0.50,       // ~1.5 months per sign
        Planet.SUN to 0.40,        // ~1 month per sign
        Planet.VENUS to 0.35,      // ~1 month per sign
        Planet.MERCURY to 0.30,    // ~3 weeks per sign (varies due to retrograde)
        Planet.MOON to 0.15        // ~2.5 days per sign
    )

    /**
     * Analyze the Transit Pillar for a given chart
     */
    fun analyzeTransitPillar(
        natalChart: VedicChart,
        transitDateTime: LocalDateTime,
        ephemerisEngine: SwissEphemerisEngine
    ): TransitPillarAnalysis {
        // Get current transit positions
        val transitPositions = getTransitPositions(natalChart, transitDateTime, ephemerisEngine)

        // Get natal Moon position for Gochara
        val natalMoon = natalChart.planetPositions.find { it.planet == Planet.MOON }
            ?: throw IllegalStateException("Natal Moon not found")

        // Calculate Gochara effects
        val gocharaEffects = calculateGocharaEffects(
            natalMoonSign = natalMoon.sign,
            transitPositions = transitPositions
        )

        // Find significant transits
        val significantTransits = findSignificantTransits(
            natalChart = natalChart,
            transitPositions = transitPositions,
            gocharaEffects = gocharaEffects
        )

        // Get vedha affected planets
        val vedhaAffectedPlanets = gocharaEffects
            .filter { it.value.isVedhaAffected }
            .map { it.key }

        // Calculate overall Gochara score
        val overallGocharaScore = calculateOverallGocharaScore(gocharaEffects, transitPositions)

        return TransitPillarAnalysis(
            transitDateTime = transitDateTime,
            transitPositions = transitPositions,
            gocharaFromMoon = gocharaEffects,
            significantTransits = significantTransits,
            vedhaAffectedPlanets = vedhaAffectedPlanets,
            overallGocharaScore = overallGocharaScore
        )
    }

    /**
     * Get current transit positions
     */
    private fun getTransitPositions(
        natalChart: VedicChart,
        transitDateTime: LocalDateTime,
        ephemerisEngine: SwissEphemerisEngine
    ): Map<Planet, TransitPillarAnalysis.TransitPosition> {
        val natalMoon = natalChart.planetPositions.find { it.planet == Planet.MOON }!!
        val ascendantSign = ZodiacSign.fromLongitude(natalChart.ascendant)

        val transitBirthData = BirthData(
            name = "Transit",
            dateTime = transitDateTime,
            latitude = natalChart.birthData.latitude,
            longitude = natalChart.birthData.longitude,
            timezone = natalChart.birthData.timezone,
            location = "Transit Chart"
        )

        val transitChart = ephemerisEngine.calculateVedicChart(transitBirthData, HouseSystem.DEFAULT)

        return transitChart.planetPositions.associate { pos ->
            val houseFromLagna = VedicAstrologyUtils.getHouseFromSigns(pos.sign, ascendantSign)
            val houseFromMoon = VedicAstrologyUtils.getHouseFromSigns(pos.sign, natalMoon.sign)

            pos.planet to TransitPillarAnalysis.TransitPosition(
                planet = pos.planet,
                sign = pos.sign,
                houseFromLagna = houseFromLagna,
                houseFromMoon = houseFromMoon,
                isRetrograde = pos.isRetrograde,
                speed = pos.speed
            )
        }
    }

    /**
     * Calculate Gochara effects for all planets
     */
    private fun calculateGocharaEffects(
        natalMoonSign: ZodiacSign,
        transitPositions: Map<Planet, TransitPillarAnalysis.TransitPosition>
    ): Map<Planet, TransitPillarAnalysis.GocharaEffect> {
        val effects = mutableMapOf<Planet, TransitPillarAnalysis.GocharaEffect>()

        for ((planet, position) in transitPositions) {
            if (planet !in Planet.MAIN_PLANETS) continue

            val houseFromMoon = position.houseFromMoon
            val favorableHouses = FAVORABLE_HOUSES_FROM_MOON[planet] ?: emptySet()
            val neutralHouses = NEUTRAL_HOUSES_FROM_MOON[planet] ?: emptySet()

            // Determine base transit quality
            val baseQuality = when (houseFromMoon) {
                in favorableHouses -> TransitQuality.GOOD
                in neutralHouses -> TransitQuality.AVERAGE
                else -> TransitQuality.CHALLENGING
            }

            // Check for Vedha
            val (isVedhaAffected, vedhaSource) = checkVedha(
                planet = planet,
                houseFromMoon = houseFromMoon,
                transitPositions = transitPositions
            )

            // Adjust quality if Vedha is present
            val finalQuality = if (isVedhaAffected && baseQuality == TransitQuality.GOOD) {
                TransitQuality.AVERAGE
            } else {
                baseQuality
            }

            effects[planet] = TransitPillarAnalysis.GocharaEffect(
                planet = planet,
                houseFromMoon = houseFromMoon,
                effect = finalQuality,
                isVedhaAffected = isVedhaAffected,
                vedhaSource = vedhaSource
            )
        }

        return effects
    }

    /**
     * Check for Vedha obstruction
     */
    private fun checkVedha(
        planet: Planet,
        houseFromMoon: Int,
        transitPositions: Map<Planet, TransitPillarAnalysis.TransitPosition>
    ): Pair<Boolean, Planet?> {
        val vedhaPoints = VEDHA_POINTS[planet] ?: return Pair(false, null)
        val vedhaHouse = vedhaPoints[houseFromMoon] ?: return Pair(false, null)

        // Check if any transiting planet is in the Vedha house
        for ((otherPlanet, otherPosition) in transitPositions) {
            if (otherPlanet != planet && otherPlanet in Planet.MAIN_PLANETS) {
                if (otherPosition.houseFromMoon == vedhaHouse) {
                    // Sun-Saturn exception: They don't cause Vedha to each other
                    if ((planet == Planet.SUN && otherPlanet == Planet.SATURN) ||
                        (planet == Planet.SATURN && otherPlanet == Planet.SUN)) {
                        continue
                    }
                    return Pair(true, otherPlanet)
                }
            }
        }

        return Pair(false, null)
    }

    /**
     * Find significant transits
     */
    private fun findSignificantTransits(
        natalChart: VedicChart,
        transitPositions: Map<Planet, TransitPillarAnalysis.TransitPosition>,
        gocharaEffects: Map<Planet, TransitPillarAnalysis.GocharaEffect>
    ): List<TransitPillarAnalysis.SignificantTransit> {
        val significant = mutableListOf<TransitPillarAnalysis.SignificantTransit>()
        val natalMoon = natalChart.planetPositions.find { it.planet == Planet.MOON }!!
        val ascendantSign = ZodiacSign.fromLongitude(natalChart.ascendant)

        // Check Saturn transit (most significant)
        transitPositions[Planet.SATURN]?.let { saturnTransit ->
            val houseFromMoon = saturnTransit.houseFromMoon

            // Sade Sati check (12th, 1st, or 2nd from Moon)
            when (houseFromMoon) {
                12 -> significant.add(TransitPillarAnalysis.SignificantTransit(
                    planet = Planet.SATURN,
                    description = "Sade Sati Phase 1 (Rising): Saturn in 12th from Moon",
                    impact = -0.6,
                    duration = "~2.5 years"
                ))
                1 -> significant.add(TransitPillarAnalysis.SignificantTransit(
                    planet = Planet.SATURN,
                    description = "Sade Sati Phase 2 (Peak): Saturn on Moon sign",
                    impact = -0.8,
                    duration = "~2.5 years"
                ))
                2 -> significant.add(TransitPillarAnalysis.SignificantTransit(
                    planet = Planet.SATURN,
                    description = "Sade Sati Phase 3 (Setting): Saturn in 2nd from Moon",
                    impact = -0.5,
                    duration = "~2.5 years"
                ))
                8 -> significant.add(TransitPillarAnalysis.SignificantTransit(
                    planet = Planet.SATURN,
                    description = "Ashtama Shani: Saturn in 8th from Moon",
                    impact = -0.7,
                    duration = "~2.5 years"
                ))
                4 -> significant.add(TransitPillarAnalysis.SignificantTransit(
                    planet = Planet.SATURN,
                    description = "Kantak Shani: Saturn in 4th from Moon",
                    impact = -0.5,
                    duration = "~2.5 years"
                ))
                3, 6, 11 -> significant.add(TransitPillarAnalysis.SignificantTransit(
                    planet = Planet.SATURN,
                    description = "Favorable Saturn transit: ${houseFromMoon}th from Moon",
                    impact = 0.4,
                    duration = "~2.5 years"
                ))
                else -> { /* Other houses - generally neutral or mixed */ }
            }
        }

        // Check Jupiter transit
        transitPositions[Planet.JUPITER]?.let { jupiterTransit ->
            val houseFromMoon = jupiterTransit.houseFromMoon
            val houseFromLagna = jupiterTransit.houseFromLagna

            when {
                houseFromMoon in listOf(2, 5, 7, 9, 11) -> {
                    significant.add(TransitPillarAnalysis.SignificantTransit(
                        planet = Planet.JUPITER,
                        description = "Favorable Jupiter: ${houseFromMoon}th from Moon - expansion, growth",
                        impact = 0.7,
                        duration = "~1 year"
                    ))
                }
                houseFromLagna in listOf(1, 5, 9) -> {
                    significant.add(TransitPillarAnalysis.SignificantTransit(
                        planet = Planet.JUPITER,
                        description = "Jupiter in Trine from Lagna: Blessings and opportunities",
                        impact = 0.6,
                        duration = "~1 year"
                    ))
                }
                        planet = Planet.JUPITER,
                        description = "Challenging Jupiter transit: ${houseFromMoon}th from Moon",
                        impact = -0.3,
                        duration = "~1 year"
                    ))
                }
                else -> { /* Other cases */ }
            }
        }

        // Check Rahu/Ketu axis
        transitPositions[Planet.RAHU]?.let { rahuTransit ->
            val houseFromMoon = rahuTransit.houseFromMoon
            val houseFromLagna = rahuTransit.houseFromLagna

            when (houseFromLagna) {
                1 -> significant.add(TransitPillarAnalysis.SignificantTransit(
                    planet = Planet.RAHU,
                    description = "Rahu on Lagna: Major identity transformation",
                    impact = -0.4,
                    duration = "~1.5 years"
                ))
                7 -> significant.add(TransitPillarAnalysis.SignificantTransit(
                    planet = Planet.RAHU,
                    description = "Rahu in 7th: Relationship and partnership changes",
                    impact = -0.4,
                    duration = "~1.5 years"
                ))
                    planet = Planet.RAHU,
                    description = "Favorable Rahu transit: Material gains possible",
                    impact = 0.3,
                    duration = "~1.5 years"
                ))
                else -> { /* Other houses */ }
            }
        }

        // Check Mars transit (temporary but intense)
        transitPositions[Planet.MARS]?.let { marsTransit ->
            val houseFromMoon = marsTransit.houseFromMoon

            if (houseFromMoon in listOf(1, 4, 7, 8, 12)) {
                significant.add(TransitPillarAnalysis.SignificantTransit(
                    planet = Planet.MARS,
                    description = "Mars in challenging position: Exercise caution",
                    impact = -0.3,
                    duration = "~1.5 months"
                ))
            } else if (houseFromMoon in listOf(3, 6, 11)) {
                    planet = Planet.MARS,
                    description = "Favorable Mars transit: Energy and drive",
                    impact = 0.3,
                    duration = "~1.5 months"
                ))
            } else {
                // Neutral
            }
        }

        // Check for transit conjunctions with natal planets
        natalChart.planetPositions.forEach { natalPos ->
            transitPositions.forEach { (transitPlanet, transitPos) ->
                if (transitPlanet != natalPos.planet && 
                    transitPlanet in listOf(Planet.SATURN, Planet.JUPITER, Planet.RAHU, Planet.KETU)) {
                    if (transitPos.sign == natalPos.sign) {
                        val isNaturalBenefic = transitPlanet == Planet.JUPITER
                        val impact = if (isNaturalBenefic) 0.4 else -0.3

                        significant.add(TransitPillarAnalysis.SignificantTransit(
                            planet = transitPlanet,
                            description = "${transitPlanet.displayName} conjunct natal ${natalPos.planet.displayName}",
                            impact = impact,
                            duration = getTransitDuration(transitPlanet)
                        ))
                    }
                }
            }
        }

        return significant.sortedByDescending { kotlin.math.abs(it.impact) }
    }

    /**
     * Calculate overall Gochara score
     */
    private fun calculateOverallGocharaScore(
        gocharaEffects: Map<Planet, TransitPillarAnalysis.GocharaEffect>,
        transitPositions: Map<Planet, TransitPillarAnalysis.TransitPosition>
    ): Double {
        var weightedScore = 0.0
        var totalWeight = 0.0

        for ((planet, effect) in gocharaEffects) {
            val weight = TRANSIT_IMPACT_WEIGHTS[planet] ?: 0.3

            val baseScore = effect.effect.score

            // Reduce score if Vedha affected
            val adjustedScore = if (effect.isVedhaAffected) {
                baseScore * 0.7
            } else {
                baseScore
            }

            // Consider retrograde (intensifies effects for outer planets)
            val retroMultiplier = if (transitPositions[planet]?.isRetrograde == true &&
                planet in listOf(Planet.SATURN, Planet.JUPITER, Planet.MARS)) {
                1.15 // Retrograde planets are more intense
            } else {
                1.0
            }

            weightedScore += adjustedScore * weight * retroMultiplier
            totalWeight += weight
        }

        return if (totalWeight > 0) (weightedScore / totalWeight).coerceIn(0.0, 1.0) else 0.5
    }

    /**
     * Get typical transit duration for a planet
     */
    private fun getTransitDuration(planet: Planet): String {
        return when (planet) {
            Planet.SATURN -> "~2.5 years"
            Planet.JUPITER -> "~1 year"
            Planet.RAHU, Planet.KETU -> "~1.5 years"
            Planet.MARS -> "~1.5 months"
            Planet.SUN, Planet.VENUS -> "~1 month"
            Planet.MERCURY -> "~3 weeks"
            Planet.MOON -> "~2.5 days"
            else -> "Variable"
        }
    }

    /**
     * Check if a transit is applying or separating from exact
     */
    fun isTransitApplying(
        transitLongitude: Double,
        natalLongitude: Double,
        transitSpeed: Double
    ): Boolean {
        val distance = VedicAstrologyUtils.angularDistance(transitLongitude, natalLongitude)

        // If close and moving towards, it's applying
        if (distance < 15.0) {
            val direction = if (transitSpeed > 0) 1 else -1
            val futureTransit = transitLongitude + (direction * 0.5)
            val futureDistance = VedicAstrologyUtils.angularDistance(futureTransit, natalLongitude)
            return futureDistance < distance
        }

        return false
    }
}
