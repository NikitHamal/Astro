package com.astro.storm.ephemeris.triplepillar

import com.astro.storm.core.model.Planet
import com.astro.storm.core.model.PlanetPosition
import com.astro.storm.core.model.VedicChart
import com.astro.storm.core.model.ZodiacSign
import com.astro.storm.ephemeris.VedicAstrologyUtils
import java.time.LocalDateTime

/**
 * Transit Pillar - Analyzes Gochara (transits) for the Triple-Pillar Engine
 *
 * This pillar evaluates current planetary transits against the natal chart using:
 *
 * 1. **Gochara Rules**: Classical transit-from-Moon analysis
 *    - Favorable houses: Vary by planet
 *    - Unfavorable houses: Opposite positions
 *    - Based on Phaladeepika and Brihat Samhita
 *
 * 2. **Vedha (Obstruction)**: Cancellation of favorable transits
 *    - When a planet transits a favorable position, another planet
 *      in its Vedha point can nullify the benefits
 *
 * 3. **Transit Speed and Retrogression**:
 *    - Slow planets (Saturn, Jupiter) have longer-lasting effects
 *    - Retrograde transits intensify effects
 *
 * 4. **House-Based Transit Analysis**:
 *    - From Ascendant (for physical/material matters)
 *    - From Moon (for mental/emotional matters)
 *    - From specific houses for specific life areas
 *
 * Based on classical texts: Phaladeepika, Brihat Samhita, and BPHS
 */
object TransitPillar {

    /**
     * Favorable transit houses from Moon for each planet
     * Based on classical Gochara rules
     */
    private val FAVORABLE_TRANSITS_FROM_MOON = mapOf(
        Planet.SUN to listOf(3, 6, 10, 11),
        Planet.MOON to listOf(1, 3, 6, 7, 10, 11),
        Planet.MARS to listOf(3, 6, 11),
        Planet.MERCURY to listOf(2, 4, 6, 8, 10, 11),
        Planet.JUPITER to listOf(2, 5, 7, 9, 11),
        Planet.VENUS to listOf(1, 2, 3, 4, 5, 8, 9, 11, 12),
        Planet.SATURN to listOf(3, 6, 11),
        Planet.RAHU to listOf(3, 6, 10, 11),
        Planet.KETU to listOf(3, 6, 10, 11)
    )

    /**
     * Neutral transit houses from Moon
     */
    private val NEUTRAL_TRANSITS_FROM_MOON = mapOf(
        Planet.SUN to listOf(1, 2, 5),
        Planet.MOON to listOf(2, 5),
        Planet.MARS to listOf(1, 10),
        Planet.MERCURY to listOf(1, 3, 5),
        Planet.JUPITER to listOf(1, 4, 6, 8, 10),
        Planet.VENUS to listOf(6, 7, 10),
        Planet.SATURN to listOf(1, 2, 10),
        Planet.RAHU to listOf(1, 2, 5),
        Planet.KETU to listOf(1, 2, 5)
    )

    /**
     * Vedha points: Map<Planet, Map<FavorableHouse, VedhaHouse>>
     * When a planet is in a favorable house but another planet is in the Vedha house,
     * the favorable effects are blocked
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
     * Weight of planets based on their transit speed and importance
     * Slower planets have higher weight due to longer-lasting effects
     */
    private val PLANET_WEIGHTS = mapOf(
        Planet.SATURN to 1.5,
        Planet.JUPITER to 1.4,
        Planet.RAHU to 1.3,
        Planet.KETU to 1.3,
        Planet.MARS to 1.0,
        Planet.SUN to 0.8,
        Planet.VENUS to 0.8,
        Planet.MERCURY to 0.7,
        Planet.MOON to 0.5
    )

    /**
     * Special transit configurations and their effects
     */
    private val SPECIAL_SATURN_TRANSITS = mapOf(
        // Sade Sati phases
        "SADE_SATI_RISING" to Pair(12, "Saturn in 12th from Moon - Sade Sati beginning"),
        "SADE_SATI_PEAK" to Pair(1, "Saturn over Moon - Sade Sati peak"),
        "SADE_SATI_SETTING" to Pair(2, "Saturn in 2nd from Moon - Sade Sati ending"),
        // Other significant positions
        "ASHTAMA_SHANI" to Pair(8, "Saturn in 8th from Moon - Ashtama Shani"),
        "KANTAK_SHANI_4" to Pair(4, "Saturn in 4th from Moon - Kantak Shani"),
        "KANTAK_SHANI_7" to Pair(7, "Saturn in 7th from Moon - Kantak Shani")
    )

    /**
     * Evaluate the Transit pillar score for a given date
     *
     * @param natalChart The natal chart
     * @param transitPositions Current planetary positions
     * @param config Configuration options
     * @return TransitPillarResult containing score and detailed analysis
     */
    fun evaluateTransitPillar(
        natalChart: VedicChart,
        transitPositions: List<PlanetPosition>,
        config: TriplePillarConfig = TriplePillarConfig.DEFAULT
    ): TransitPillarResult {
        val natalMoon = natalChart.planetPositions.find { it.planet == Planet.MOON }
            ?: return createDefaultResult("Natal Moon position not found")

        val ascendantSign = ZodiacSign.fromLongitude(natalChart.ascendant)

        val transitAnalyses = mutableListOf<SingleTransitAnalysis>()
        val transitHighlights = mutableListOf<TransitHighlight>()

        var totalWeightedScore = 0.0
        var totalWeight = 0.0

        for (transitPos in transitPositions) {
            val planet = transitPos.planet
            if (planet !in Planet.MAIN_PLANETS) continue
            if (!config.includeOuterPlanets && planet in Planet.OUTER_PLANETS) continue

            val analysis = analyzeTransit(
                planet = planet,
                transitPosition = transitPos,
                natalMoon = natalMoon,
                natalChart = natalChart,
                allTransitPositions = transitPositions,
                ascendantSign = ascendantSign,
                config = config
            )

            transitAnalyses.add(analysis)

            val weight = PLANET_WEIGHTS[planet] ?: 1.0
            totalWeightedScore += analysis.score * weight
            totalWeight += weight

            // Create highlight for significant transits
            if (analysis.significance != TransitSignificance.MINOR) {
                transitHighlights.add(createTransitHighlight(analysis, transitPos))
            }
        }

        val compositeScore = if (totalWeight > 0) {
            (totalWeightedScore / totalWeight).coerceIn(0.0, 100.0)
        } else 50.0

        // Check for special configurations
        val specialConfigurations = checkSpecialConfigurations(
            transitAnalyses, natalMoon.sign
        )

        // Apply special configuration modifiers
        var finalScore = compositeScore
        for (specialConfig in specialConfigurations) {
            finalScore *= specialConfig.modifier
        }
        finalScore = finalScore.coerceIn(0.0, 100.0)

        val summary = buildTransitSummary(transitAnalyses, specialConfigurations, finalScore)

        return TransitPillarResult(
            score = finalScore,
            transitAnalyses = transitAnalyses,
            transitHighlights = transitHighlights,
            specialConfigurations = specialConfigurations,
            summary = summary
        )
    }

    /**
     * Analyze a single planet's transit
     */
    private fun analyzeTransit(
        planet: Planet,
        transitPosition: PlanetPosition,
        natalMoon: PlanetPosition,
        natalChart: VedicChart,
        allTransitPositions: List<PlanetPosition>,
        ascendantSign: ZodiacSign,
        config: TriplePillarConfig
    ): SingleTransitAnalysis {
        val transitSign = transitPosition.sign

        // Calculate house from Moon
        val houseFromMoon = VedicAstrologyUtils.getHouseFromSigns(transitSign, natalMoon.sign)

        // Calculate house from Ascendant
        val houseFromAscendant = VedicAstrologyUtils.getHouseFromSigns(transitSign, ascendantSign)

        // Determine base favorability
        val isFavorable = houseFromMoon in (FAVORABLE_TRANSITS_FROM_MOON[planet] ?: emptyList())
        val isNeutral = houseFromMoon in (NEUTRAL_TRANSITS_FROM_MOON[planet] ?: emptyList())

        // Check for Vedha
        var isVedhaAffected = false
        var vedhaPlanet: Planet? = null

        if (config.applyVedhaCorrection && isFavorable) {
            val vedhaResult = checkVedha(planet, houseFromMoon, allTransitPositions, natalMoon.sign)
            isVedhaAffected = vedhaResult.first
            vedhaPlanet = vedhaResult.second
        }

        // Calculate base score
        var score = when {
            isFavorable && !isVedhaAffected -> 70.0 + (houseFromMoon * 2) // Bonus for specific houses
            isFavorable && isVedhaAffected -> 45.0  // Vedha reduces favorable to neutral
            isNeutral -> 50.0
            else -> 30.0 - (if (houseFromMoon in listOf(6, 8, 12)) 10.0 else 0.0)
        }

        // Retrograde adjustment
        if (config.considerRetrogrades && transitPosition.isRetrograde) {
            // Retrograde intensifies effects - good becomes better, bad becomes worse
            score = if (isFavorable && !isVedhaAffected) {
                (score * 1.1).coerceAtMost(100.0)
            } else if (!isFavorable && !isNeutral) {
                score * 0.9
            } else score
        }

        // Transit to natal planet aspects
        val natalAspects = calculateTransitToNatalAspects(transitPosition, natalChart)
        for (aspect in natalAspects) {
            score += aspect.scoreModification
        }

        score = score.coerceIn(0.0, 100.0)

        // Determine significance
        val significance = determineSignificance(planet, transitPosition.isRetrograde, natalAspects)

        return SingleTransitAnalysis(
            planet = planet,
            transitSign = transitSign,
            houseFromMoon = houseFromMoon,
            houseFromAscendant = houseFromAscendant,
            isFavorable = isFavorable && !isVedhaAffected,
            isVedhaAffected = isVedhaAffected,
            vedhaPlanet = vedhaPlanet,
            isRetrograde = transitPosition.isRetrograde,
            score = score,
            natalAspects = natalAspects,
            significance = significance
        )
    }

    /**
     * Check if a transit has Vedha (obstruction)
     */
    private fun checkVedha(
        planet: Planet,
        houseFromMoon: Int,
        allTransitPositions: List<PlanetPosition>,
        natalMoonSign: ZodiacSign
    ): Pair<Boolean, Planet?> {
        val vedhaMap = VEDHA_POINTS[planet] ?: return Pair(false, null)
        val vedhaHouse = vedhaMap[houseFromMoon] ?: return Pair(false, null)

        for (otherTransit in allTransitPositions) {
            if (otherTransit.planet == planet) continue
            if (otherTransit.planet !in Planet.MAIN_PLANETS) continue

            val otherHouseFromMoon = VedicAstrologyUtils.getHouseFromSigns(
                otherTransit.sign, natalMoonSign
            )

            if (otherHouseFromMoon == vedhaHouse) {
                return Pair(true, otherTransit.planet)
            }
        }

        return Pair(false, null)
    }

    /**
     * Calculate aspects from transiting planet to natal planets
     */
    private fun calculateTransitToNatalAspects(
        transitPosition: PlanetPosition,
        natalChart: VedicChart
    ): List<TransitToNatalAspect> {
        val aspects = mutableListOf<TransitToNatalAspect>()
        val transitPlanet = transitPosition.planet
        val transitLongitude = transitPosition.longitude

        for (natalPos in natalChart.planetPositions) {
            val natalLongitude = natalPos.longitude
            val angularDistance = VedicAstrologyUtils.angularDistance(transitLongitude, natalLongitude)

            // Check for conjunction (0°)
            if (angularDistance < 8.0) {
                val isBenefic = transitPlanet in listOf(Planet.JUPITER, Planet.VENUS)
                aspects.add(
                    TransitToNatalAspect(
                        transitPlanet = transitPlanet,
                        natalPlanet = natalPos.planet,
                        aspectType = "Conjunction",
                        orb = angularDistance,
                        isBenefic = isBenefic,
                        scoreModification = if (isBenefic) 8.0 else -5.0
                    )
                )
            }

            // Check for opposition (180°)
            val oppositionDiff = kotlin.math.abs(angularDistance - 180.0)
            if (oppositionDiff < 8.0) {
                aspects.add(
                    TransitToNatalAspect(
                        transitPlanet = transitPlanet,
                        natalPlanet = natalPos.planet,
                        aspectType = "Opposition",
                        orb = oppositionDiff,
                        isBenefic = false,
                        scoreModification = -6.0
                    )
                )
            }

            // Check for trine (120°)
            val trineDiff = kotlin.math.abs(angularDistance - 120.0)
            if (trineDiff < 8.0) {
                aspects.add(
                    TransitToNatalAspect(
                        transitPlanet = transitPlanet,
                        natalPlanet = natalPos.planet,
                        aspectType = "Trine",
                        orb = trineDiff,
                        isBenefic = true,
                        scoreModification = 6.0
                    )
                )
            }

            // Check for square (90°)
            val squareDiff = kotlin.math.abs(angularDistance - 90.0)
            if (squareDiff < 8.0) {
                val isMalefic = transitPlanet in listOf(Planet.SATURN, Planet.MARS, Planet.RAHU, Planet.KETU)
                aspects.add(
                    TransitToNatalAspect(
                        transitPlanet = transitPlanet,
                        natalPlanet = natalPos.planet,
                        aspectType = "Square",
                        orb = squareDiff,
                        isBenefic = false,
                        scoreModification = if (isMalefic) -8.0 else -4.0
                    )
                )
            }
        }

        return aspects
    }

    /**
     * Check for special transit configurations
     */
    private fun checkSpecialConfigurations(
        transitAnalyses: List<SingleTransitAnalysis>,
        natalMoonSign: ZodiacSign
    ): List<SpecialTransitConfiguration> {
        val configurations = mutableListOf<SpecialTransitConfiguration>()

        val saturnAnalysis = transitAnalyses.find { it.planet == Planet.SATURN }
        if (saturnAnalysis != null) {
            val saturnHouse = saturnAnalysis.houseFromMoon

            when (saturnHouse) {
                12 -> configurations.add(
                    SpecialTransitConfiguration(
                        name = "Sade Sati - Rising Phase",
                        description = "Saturn in 12th from Moon. Beginning of the 7.5-year Sade Sati period. " +
                            "Gradual increase in challenges and life lessons.",
                        modifier = 0.85,
                        involvedPlanets = listOf(Planet.SATURN),
                        severity = ConfigurationSeverity.MODERATE
                    )
                )
                1 -> configurations.add(
                    SpecialTransitConfiguration(
                        name = "Sade Sati - Peak Phase",
                        description = "Saturn transiting over natal Moon. The most intense phase of Sade Sati. " +
                            "Significant life transformations, emotional challenges, and karmic lessons.",
                        modifier = 0.70,
                        involvedPlanets = listOf(Planet.SATURN),
                        severity = ConfigurationSeverity.SEVERE
                    )
                )
                2 -> configurations.add(
                    SpecialTransitConfiguration(
                        name = "Sade Sati - Setting Phase",
                        description = "Saturn in 2nd from Moon. Final phase of Sade Sati. " +
                            "Gradual relief, consolidation of lessons learned.",
                        modifier = 0.88,
                        involvedPlanets = listOf(Planet.SATURN),
                        severity = ConfigurationSeverity.MODERATE
                    )
                )
                8 -> configurations.add(
                    SpecialTransitConfiguration(
                        name = "Ashtama Shani",
                        description = "Saturn in 8th from Moon. Can bring sudden transformations, " +
                            "health concerns, and need for deep introspection.",
                        modifier = 0.75,
                        involvedPlanets = listOf(Planet.SATURN),
                        severity = ConfigurationSeverity.SEVERE
                    )
                )
                4, 7 -> configurations.add(
                    SpecialTransitConfiguration(
                        name = "Kantak Shani",
                        description = "Saturn in ${saturnHouse}th from Moon. Creates obstacles, delays, " +
                            "and requires patience in domestic (4th) or relationship (7th) matters.",
                        modifier = 0.82,
                        involvedPlanets = listOf(Planet.SATURN),
                        severity = ConfigurationSeverity.MODERATE
                    )
                )
            }
        }

        // Jupiter's favorable transits
        val jupiterAnalysis = transitAnalyses.find { it.planet == Planet.JUPITER }
        if (jupiterAnalysis != null && jupiterAnalysis.isFavorable) {
            val jupiterHouse = jupiterAnalysis.houseFromMoon

            when (jupiterHouse) {
                2 -> configurations.add(
                    SpecialTransitConfiguration(
                        name = "Jupiter in 2nd - Wealth Phase",
                        description = "Jupiter in 2nd from Moon. Favorable for financial growth, " +
                            "family harmony, and speech/communication.",
                        modifier = 1.15,
                        involvedPlanets = listOf(Planet.JUPITER),
                        severity = ConfigurationSeverity.BENEFICIAL
                    )
                )
                5 -> configurations.add(
                    SpecialTransitConfiguration(
                        name = "Jupiter in 5th - Intelligence/Children",
                        description = "Jupiter in 5th from Moon. Excellent for education, creativity, " +
                            "children matters, and speculative gains.",
                        modifier = 1.18,
                        involvedPlanets = listOf(Planet.JUPITER),
                        severity = ConfigurationSeverity.BENEFICIAL
                    )
                )
                9 -> configurations.add(
                    SpecialTransitConfiguration(
                        name = "Jupiter in 9th - Fortune Phase",
                        description = "Jupiter in 9th from Moon. Highly auspicious for luck, " +
                            "higher education, spirituality, and long-distance travel.",
                        modifier = 1.20,
                        involvedPlanets = listOf(Planet.JUPITER),
                        severity = ConfigurationSeverity.HIGHLY_BENEFICIAL
                    )
                )
                11 -> configurations.add(
                    SpecialTransitConfiguration(
                        name = "Jupiter in 11th - Gains Phase",
                        description = "Jupiter in 11th from Moon. Excellent for achieving goals, " +
                            "financial gains, and social connections.",
                        modifier = 1.15,
                        involvedPlanets = listOf(Planet.JUPITER),
                        severity = ConfigurationSeverity.BENEFICIAL
                    )
                )
            }
        }

        // Rahu-Ketu axis transits
        val rahuAnalysis = transitAnalyses.find { it.planet == Planet.RAHU }
        val ketuAnalysis = transitAnalyses.find { it.planet == Planet.KETU }

        if (rahuAnalysis != null) {
            when (rahuAnalysis.houseFromMoon) {
                1 -> configurations.add(
                    SpecialTransitConfiguration(
                        name = "Rahu Transit over Moon",
                        description = "Rahu transiting natal Moon. Can bring confusion, " +
                            "illusions, and need for clarity in emotional matters.",
                        modifier = 0.80,
                        involvedPlanets = listOf(Planet.RAHU),
                        severity = ConfigurationSeverity.MODERATE
                    )
                )
                7 -> configurations.add(
                    SpecialTransitConfiguration(
                        name = "Ketu Transit over Moon",
                        description = "Ketu transiting over natal Moon (Rahu in 7th). " +
                            "Spiritual insights but emotional detachment challenges.",
                        modifier = 0.82,
                        involvedPlanets = listOf(Planet.RAHU, Planet.KETU),
                        severity = ConfigurationSeverity.MODERATE
                    )
                )
            }
        }

        return configurations
    }

    /**
     * Determine the significance level of a transit
     */
    private fun determineSignificance(
        planet: Planet,
        isRetrograde: Boolean,
        natalAspects: List<TransitToNatalAspect>
    ): TransitSignificance {
        // Slow planets are always significant
        if (planet in listOf(Planet.SATURN, Planet.JUPITER, Planet.RAHU, Planet.KETU)) {
            return TransitSignificance.MAJOR
        }

        // Tight aspects to natal planets are significant
        val hasTightAspect = natalAspects.any { it.orb < 2.0 }
        if (hasTightAspect) {
            return TransitSignificance.SIGNIFICANT
        }

        // Retrograde Mars or Venus
        if (isRetrograde && planet in listOf(Planet.MARS, Planet.VENUS)) {
            return TransitSignificance.SIGNIFICANT
        }

        // Any aspects at all
        if (natalAspects.isNotEmpty()) {
            return TransitSignificance.MODERATE
        }

        return TransitSignificance.MINOR
    }

    /**
     * Create a transit highlight from analysis
     */
    private fun createTransitHighlight(
        analysis: SingleTransitAnalysis,
        position: PlanetPosition
    ): TransitHighlight {
        return TransitHighlight(
            transitingPlanet = analysis.planet,
            transitSign = analysis.transitSign,
            houseFromMoon = analysis.houseFromMoon,
            houseFromAscendant = analysis.houseFromAscendant,
            isRetrograde = analysis.isRetrograde,
            isFavorable = analysis.isFavorable,
            isVedhaAffected = analysis.isVedhaAffected,
            bavBindus = 0,  // Will be filled by Ashtakavarga pillar
            savBindus = 0,
            significance = analysis.significance
        )
    }

    /**
     * Build transit summary text
     */
    private fun buildTransitSummary(
        transitAnalyses: List<SingleTransitAnalysis>,
        specialConfigurations: List<SpecialTransitConfiguration>,
        finalScore: Double
    ): String {
        return buildString {
            val quality = when {
                finalScore >= 70 -> "Favorable"
                finalScore >= 55 -> "Moderately favorable"
                finalScore >= 45 -> "Mixed"
                finalScore >= 30 -> "Challenging"
                else -> "Difficult"
            }

            append("$quality transit period. ")

            val favorablePlanets = transitAnalyses.filter { it.isFavorable }.map { it.planet.displayName }
            val challengingPlanets = transitAnalyses.filter { !it.isFavorable && it.score < 40 }
                .map { it.planet.displayName }

            if (favorablePlanets.isNotEmpty()) {
                append("Favorable transits from ${favorablePlanets.joinToString(", ")}. ")
            }

            if (challengingPlanets.isNotEmpty()) {
                append("Challenging transits from ${challengingPlanets.joinToString(", ")}. ")
            }

            for (config in specialConfigurations) {
                when (config.severity) {
                    ConfigurationSeverity.SEVERE -> append("Alert: ${config.name}. ")
                    ConfigurationSeverity.HIGHLY_BENEFICIAL -> append("Opportunity: ${config.name}. ")
                    else -> {}
                }
            }
        }.trim()
    }

    /**
     * Create default result when analysis cannot be performed
     */
    private fun createDefaultResult(message: String): TransitPillarResult {
        return TransitPillarResult(
            score = 50.0,
            transitAnalyses = emptyList(),
            transitHighlights = emptyList(),
            specialConfigurations = emptyList(),
            summary = message
        )
    }
}

/**
 * Result of Transit pillar analysis
 */
data class TransitPillarResult(
    val score: Double,
    val transitAnalyses: List<SingleTransitAnalysis>,
    val transitHighlights: List<TransitHighlight>,
    val specialConfigurations: List<SpecialTransitConfiguration>,
    val summary: String
)

/**
 * Analysis of a single planet's transit
 */
data class SingleTransitAnalysis(
    val planet: Planet,
    val transitSign: ZodiacSign,
    val houseFromMoon: Int,
    val houseFromAscendant: Int,
    val isFavorable: Boolean,
    val isVedhaAffected: Boolean,
    val vedhaPlanet: Planet?,
    val isRetrograde: Boolean,
    val score: Double,
    val natalAspects: List<TransitToNatalAspect>,
    val significance: TransitSignificance
)

/**
 * Aspect from transiting planet to natal planet
 */
data class TransitToNatalAspect(
    val transitPlanet: Planet,
    val natalPlanet: Planet,
    val aspectType: String,
    val orb: Double,
    val isBenefic: Boolean,
    val scoreModification: Double
)

/**
 * Special transit configuration (like Sade Sati)
 */
data class SpecialTransitConfiguration(
    val name: String,
    val description: String,
    val modifier: Double,
    val involvedPlanets: List<Planet>,
    val severity: ConfigurationSeverity
)

/**
 * Severity/benefit level of a configuration
 */
enum class ConfigurationSeverity {
    HIGHLY_BENEFICIAL,
    BENEFICIAL,
    NEUTRAL,
    MODERATE,
    SEVERE
}
