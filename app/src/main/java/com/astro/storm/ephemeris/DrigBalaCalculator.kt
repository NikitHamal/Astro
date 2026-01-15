package com.astro.storm.ephemeris

import com.astro.storm.core.model.Planet
import com.astro.storm.core.model.PlanetPosition
import com.astro.storm.core.model.VedicChart
import kotlin.math.abs

/**
 * DrigBalaCalculator - Comprehensive Aspectual Strength Analysis
 *
 * Drig Bala (Aspectual Strength) is one of the six components of Shadbala.
 * It measures the strength a planet gains or loses from aspects received
 * from other planets.
 *
 * Key Concepts:
 * - Benefic aspects add strength (positive virupas)
 * - Malefic aspects reduce strength (negative virupas)
 * - Net Drig Bala = Benefic aspects - Malefic aspects
 * - Special aspects (Mars 4th/8th, Jupiter 5th/9th, Saturn 3rd/10th) have full strength
 * - Regular 7th house aspects have full strength for all planets
 *
 * Vedic References:
 * - BPHS (Brihat Parashara Hora Shastra) Shadbala chapter
 * - Graha Sutras
 */
object DrigBalaCalculator {

    private const val VIRUPAS_PER_RUPA = 60.0

    /**
     * Planets used in Drig Bala calculation (7 classical planets)
     */
    private val DRIG_BALA_PLANETS = setOf(
        Planet.SUN, Planet.MOON, Planet.MARS,
        Planet.MERCURY, Planet.JUPITER, Planet.VENUS, Planet.SATURN
    )

    /**
     * Aspect type enumeration
     */
    enum class AspectType(val displayName: String, val strength: Double) {
        FULL("Full Aspect (100%)", 1.0),
        THREE_QUARTER("3/4 Aspect (75%)", 0.75),
        HALF("Half Aspect (50%)", 0.5),
        QUARTER("Quarter Aspect (25%)", 0.25),
        NONE("No Aspect", 0.0)
    }

    /**
     * Aspect effect type
     */
    enum class AspectEffect(val displayName: String) {
        BENEFIC("Benefic"),
        MALEFIC("Malefic"),
        NEUTRAL("Neutral")
    }

    /**
     * Individual aspect data
     */
    data class AspectInfo(
        val aspectingPlanet: Planet,
        val aspectedPlanet: Planet,
        val houseDistance: Int,
        val aspectType: AspectType,
        val isSpecialAspect: Boolean,
        val aspectEffect: AspectEffect,
        val virupas: Double,
        val interpretation: String
    )

    /**
     * Planet's Drig Bala analysis
     */
    data class PlanetDrigBala(
        val planet: Planet,
        val aspectsReceived: List<AspectInfo>,
        val aspectsCast: List<AspectInfo>,
        val beneficVirupas: Double,
        val maleficVirupas: Double,
        val netDrigBala: Double,
        val isPositive: Boolean,
        val strengthRating: String,
        val interpretation: String
    )

    /**
     * House aspect analysis
     */
    data class HouseAspects(
        val houseNumber: Int,
        val aspectingPlanets: List<AspectInfo>,
        val beneficCount: Int,
        val maleficCount: Int,
        val netEffect: AspectEffect,
        val interpretation: String
    )

    /**
     * Complete Drig Bala analysis result
     */
    data class DrigBalaAnalysis(
        val planetaryDrigBala: Map<Planet, PlanetDrigBala>,
        val houseAspects: List<HouseAspects>,
        val aspectMatrix: List<AspectInfo>,
        val strongestPlanet: Planet,
        val weakestPlanet: Planet,
        val overallScore: Double,
        val keyInsights: List<String>,
        val recommendations: List<String>
    )

    /**
     * Benefic values for aspects (virupas contributed per full aspect)
     */
    private val beneficAspectValues = mapOf(
        Planet.JUPITER to 15.0,
        Planet.VENUS to 15.0,
        Planet.MOON to 10.0,
        Planet.MERCURY to 8.0
    )

    /**
     * Malefic values for aspects (virupas reduced per full aspect)
     */
    private val maleficAspectValues = mapOf(
        Planet.SATURN to 10.0,
        Planet.MARS to 10.0,
        Planet.SUN to 5.0,
        Planet.RAHU to 8.0,
        Planet.KETU to 8.0
    )

    /**
     * Special aspects for outer planets
     */
    private val specialAspects = mapOf(
        Planet.MARS to listOf(4 to AspectType.THREE_QUARTER, 8 to AspectType.FULL),
        Planet.JUPITER to listOf(5 to AspectType.HALF, 9 to AspectType.FULL),
        Planet.SATURN to listOf(3 to AspectType.THREE_QUARTER, 10 to AspectType.FULL)
    )

    /**
     * Perform complete Drig Bala analysis
     */
    fun analyzeDrigBala(chart: VedicChart): DrigBalaAnalysis {
        val planetMap = chart.planetPositions.associateBy { it.planet }
        val aspectMatrix = buildAspectMatrix(chart)
        val planetaryDrigBala = mutableMapOf<Planet, PlanetDrigBala>()

        // Calculate Drig Bala for each planet
        for (position in chart.planetPositions) {
            if (position.planet in DRIG_BALA_PLANETS) {
                planetaryDrigBala[position.planet] = calculatePlanetDrigBala(
                    position.planet,
                    aspectMatrix,
                    planetMap
                )
            }
        }

        // Calculate house aspects
        val houseAspects = (1..12).map { house ->
            calculateHouseAspects(house, chart, aspectMatrix)
        }

        // Find strongest and weakest
        val sortedByStrength = planetaryDrigBala.values.sortedByDescending { it.netDrigBala }
        val strongestPlanet = sortedByStrength.firstOrNull()?.planet ?: Planet.JUPITER
        val weakestPlanet = sortedByStrength.lastOrNull()?.planet ?: Planet.SATURN

        // Calculate overall score
        val overallScore = calculateOverallScore(planetaryDrigBala)

        // Generate insights
        val insights = generateInsights(planetaryDrigBala, houseAspects)
        val recommendations = generateRecommendations(planetaryDrigBala, aspectMatrix)

        return DrigBalaAnalysis(
            planetaryDrigBala = planetaryDrigBala.toMap(),
            houseAspects = houseAspects,
            aspectMatrix = aspectMatrix,
            strongestPlanet = strongestPlanet,
            weakestPlanet = weakestPlanet,
            overallScore = overallScore,
            keyInsights = insights,
            recommendations = recommendations
        )
    }

    /**
     * Build complete aspect matrix for the chart
     */
    private fun buildAspectMatrix(chart: VedicChart): List<AspectInfo> {
        val aspects = mutableListOf<AspectInfo>()
        val positions = chart.planetPositions

        for (aspectingPos in positions) {
            if (!canCastAspect(aspectingPos.planet)) continue

            for (aspectedPos in positions) {
                if (aspectingPos.planet == aspectedPos.planet) continue

                val houseDistance = calculateHouseDistance(aspectingPos.house, aspectedPos.house)
                val aspectType = getAspectType(aspectingPos.planet, houseDistance)

                if (aspectType != AspectType.NONE) {
                    val isSpecial = isSpecialAspect(aspectingPos.planet, houseDistance)
                    val effect = getAspectEffect(aspectingPos.planet)
                    val virupas = calculateAspectVirupas(aspectingPos.planet, aspectType)

                    aspects.add(
                        AspectInfo(
                            aspectingPlanet = aspectingPos.planet,
                            aspectedPlanet = aspectedPos.planet,
                            houseDistance = houseDistance,
                            aspectType = aspectType,
                            isSpecialAspect = isSpecial,
                            aspectEffect = effect,
                            virupas = virupas,
                            interpretation = buildAspectInterpretation(
                                aspectingPos.planet,
                                aspectedPos.planet,
                                aspectType,
                                effect
                            )
                        )
                    )
                }
            }
        }

        return aspects
    }

    /**
     * Calculate Drig Bala for a single planet
     */
    private fun calculatePlanetDrigBala(
        planet: Planet,
        aspectMatrix: List<AspectInfo>,
        planetMap: Map<Planet, PlanetPosition>
    ): PlanetDrigBala {
        val aspectsReceived = aspectMatrix.filter { it.aspectedPlanet == planet }
        val aspectsCast = aspectMatrix.filter { it.aspectingPlanet == planet }

        val beneficVirupas = aspectsReceived
            .filter { it.aspectEffect == AspectEffect.BENEFIC }
            .sumOf { it.virupas }

        val maleficVirupas = aspectsReceived
            .filter { it.aspectEffect == AspectEffect.MALEFIC }
            .sumOf { it.virupas }

        val netDrigBala = beneficVirupas - maleficVirupas
        val isPositive = netDrigBala >= 0

        val strengthRating = when {
            netDrigBala >= 20 -> "Excellent"
            netDrigBala >= 10 -> "Strong"
            netDrigBala >= 0 -> "Neutral"
            netDrigBala >= -10 -> "Weak"
            else -> "Very Weak"
        }

        return PlanetDrigBala(
            planet = planet,
            aspectsReceived = aspectsReceived,
            aspectsCast = aspectsCast,
            beneficVirupas = beneficVirupas,
            maleficVirupas = maleficVirupas,
            netDrigBala = netDrigBala,
            isPositive = isPositive,
            strengthRating = strengthRating,
            interpretation = buildPlanetInterpretation(planet, netDrigBala, aspectsReceived)
        )
    }

    /**
     * Calculate aspects on a house
     */
    private fun calculateHouseAspects(
        houseNumber: Int,
        chart: VedicChart,
        aspectMatrix: List<AspectInfo>
    ): HouseAspects {
        val aspectingPlanets = mutableListOf<AspectInfo>()

        for (position in chart.planetPositions) {
            if (!canCastAspect(position.planet)) continue

            val distance = calculateHouseDistance(position.house, houseNumber)
            val aspectType = getAspectType(position.planet, distance)

            if (aspectType != AspectType.NONE) {
                val effect = getAspectEffect(position.planet)
                aspectingPlanets.add(
                    AspectInfo(
                        aspectingPlanet = position.planet,
                        aspectedPlanet = Planet.SUN, // Placeholder for house
                        houseDistance = distance,
                        aspectType = aspectType,
                        isSpecialAspect = isSpecialAspect(position.planet, distance),
                        aspectEffect = effect,
                        virupas = calculateAspectVirupas(position.planet, aspectType),
                        interpretation = "${position.planet.displayName} aspects house $houseNumber"
                    )
                )
            }
        }

        val beneficCount = aspectingPlanets.count { it.aspectEffect == AspectEffect.BENEFIC }
        val maleficCount = aspectingPlanets.count { it.aspectEffect == AspectEffect.MALEFIC }

        val netEffect = when {
            beneficCount > maleficCount -> AspectEffect.BENEFIC
            maleficCount > beneficCount -> AspectEffect.MALEFIC
            else -> AspectEffect.NEUTRAL
        }

        return HouseAspects(
            houseNumber = houseNumber,
            aspectingPlanets = aspectingPlanets,
            beneficCount = beneficCount,
            maleficCount = maleficCount,
            netEffect = netEffect,
            interpretation = buildHouseInterpretation(houseNumber, beneficCount, maleficCount)
        )
    }

    /**
     * Check if planet can cast aspects
     */
    private fun canCastAspect(planet: Planet): Boolean {
        return planet !in listOf(Planet.URANUS, Planet.NEPTUNE, Planet.PLUTO)
    }

    /**
     * Calculate house distance (1-indexed)
     */
    private fun calculateHouseDistance(fromHouse: Int, toHouse: Int): Int {
        var diff = toHouse - fromHouse
        if (diff <= 0) diff += 12
        return diff
    }

    /**
     * Get aspect type based on planet and house distance
     */
    private fun getAspectType(planet: Planet, houseDistance: Int): AspectType {
        // All planets have full 7th house aspect
        if (houseDistance == 7) return AspectType.FULL

        // Check special aspects
        specialAspects[planet]?.forEach { (house, type) ->
            if (houseDistance == house) return type
        }

        // Conjunction (same house)
        if (houseDistance == 12 || houseDistance == 0) return AspectType.FULL

        return AspectType.NONE
    }

    /**
     * Check if this is a special aspect
     */
    private fun isSpecialAspect(planet: Planet, houseDistance: Int): Boolean {
        if (houseDistance == 7) return false // 7th is universal, not special
        return specialAspects[planet]?.any { it.first == houseDistance } ?: false
    }

    /**
     * Get aspect effect (benefic/malefic/neutral)
     */
    private fun getAspectEffect(planet: Planet): AspectEffect {
        return when (planet) {
            Planet.JUPITER, Planet.VENUS -> AspectEffect.BENEFIC
            Planet.MOON, Planet.MERCURY -> AspectEffect.BENEFIC // Natural benefics
            Planet.SATURN, Planet.MARS, Planet.RAHU, Planet.KETU -> AspectEffect.MALEFIC
            Planet.SUN -> AspectEffect.MALEFIC // Mild malefic in aspects
            else -> AspectEffect.NEUTRAL
        }
    }

    /**
     * Calculate aspect virupas
     */
    private fun calculateAspectVirupas(planet: Planet, aspectType: AspectType): Double {
        val baseValue = beneficAspectValues[planet]
            ?: maleficAspectValues[planet]
            ?: 5.0
        return baseValue * aspectType.strength
    }

    /**
     * Build aspect interpretation
     */
    private fun buildAspectInterpretation(
        aspectingPlanet: Planet,
        aspectedPlanet: Planet,
        aspectType: AspectType,
        effect: AspectEffect
    ): String {
        val effectWord = when (effect) {
            AspectEffect.BENEFIC -> "supports"
            AspectEffect.MALEFIC -> "challenges"
            AspectEffect.NEUTRAL -> "influences"
        }
        return "${aspectingPlanet.displayName}'s ${aspectType.displayName} $effectWord ${aspectedPlanet.displayName}'s significations."
    }

    /**
     * Build planet interpretation
     */
    private fun buildPlanetInterpretation(
        planet: Planet,
        netDrigBala: Double,
        aspects: List<AspectInfo>
    ): String {
        val quality = when {
            netDrigBala >= 15 -> "strongly supported by benefic aspects"
            netDrigBala >= 5 -> "moderately supported by aspects"
            netDrigBala >= -5 -> "has balanced aspectual influence"
            netDrigBala >= -15 -> "moderately challenged by malefic aspects"
            else -> "significantly afflicted by malefic aspects"
        }

        val strongestAspect = aspects.maxByOrNull { abs(it.virupas) }
        val influencer = strongestAspect?.let {
            " ${it.aspectingPlanet.displayName} has the strongest influence."
        } ?: ""

        return "${planet.displayName} is $quality.$influencer"
    }

    /**
     * Build house interpretation
     */
    private fun buildHouseInterpretation(house: Int, benefic: Int, malefic: Int): String {
        return when {
            benefic > malefic -> "House $house is supported by ${benefic} benefic aspect(s), promoting its significations."
            malefic > benefic -> "House $house faces challenges from ${malefic} malefic aspect(s)."
            benefic > 0 -> "House $house has balanced aspectual influences."
            else -> "House $house receives no significant planetary aspects."
        }
    }

    /**
     * Calculate overall score
     */
    private fun calculateOverallScore(drigBalaMap: Map<Planet, PlanetDrigBala>): Double {
        if (drigBalaMap.isEmpty()) return 50.0

        val totalNet = drigBalaMap.values.sumOf { it.netDrigBala }
        val avgNet = totalNet / drigBalaMap.size

        // Normalize to 0-100 scale
        return ((avgNet + 30) / 60 * 100).coerceIn(0.0, 100.0)
    }

    /**
     * Generate insights
     */
    private fun generateInsights(
        drigBalaMap: Map<Planet, PlanetDrigBala>,
        houseAspects: List<HouseAspects>
    ): List<String> {
        val insights = mutableListOf<String>()

        // Planet-based insights
        val stronglySupported = drigBalaMap.values.filter { it.netDrigBala >= 15 }
        if (stronglySupported.isNotEmpty()) {
            val planets = stronglySupported.joinToString { it.planet.displayName }
            insights.add("$planets receive(s) strong benefic support through aspects")
        }

        val stronglyAfflicted = drigBalaMap.values.filter { it.netDrigBala <= -15 }
        if (stronglyAfflicted.isNotEmpty()) {
            val planets = stronglyAfflicted.joinToString { it.planet.displayName }
            insights.add("$planets face(s) significant aspectual challenges")
        }

        // House-based insights
        val beneficHouses = houseAspects.filter { it.netEffect == AspectEffect.BENEFIC }
        if (beneficHouses.isNotEmpty()) {
            val houses = beneficHouses.take(3).joinToString { "House ${it.houseNumber}" }
            insights.add("$houses receive benefic aspectual support")
        }

        // Jupiter's aspects
        val jupiterAspects = drigBalaMap.values
            .flatMap { it.aspectsReceived }
            .filter { it.aspectingPlanet == Planet.JUPITER }
        if (jupiterAspects.isNotEmpty()) {
            val aspected = jupiterAspects.map { it.aspectedPlanet.displayName }.distinct().take(3)
            insights.add("Jupiter's protective aspects benefit ${aspected.joinToString()}")
        }

        return insights.take(5)
    }

    /**
     * Generate recommendations
     */
    private fun generateRecommendations(
        drigBalaMap: Map<Planet, PlanetDrigBala>,
        aspectMatrix: List<AspectInfo>
    ): List<String> {
        val recommendations = mutableListOf<String>()

        // Recommend remedies for afflicted planets
        val afflicted = drigBalaMap.values.filter { it.netDrigBala < -10 }
        afflicted.forEach { planetBala ->
            recommendations.add("Consider remedies for ${planetBala.planet.displayName} which faces aspectual challenges")
        }

        // Positive aspects to utilize
        val beneficPlanets = drigBalaMap.values.filter { it.netDrigBala > 10 }
        beneficPlanets.forEach { planetBala ->
            recommendations.add("${planetBala.planet.displayName}'s strong aspectual support can be leveraged for success")
        }

        // Saturn aspects
        val saturnAspects = aspectMatrix.filter {
            it.aspectingPlanet == Planet.SATURN && it.aspectType == AspectType.FULL
        }
        if (saturnAspects.isNotEmpty()) {
            recommendations.add("Saturn's aspects require patience and disciplined effort")
        }

        return recommendations.take(5)
    }

    /**
     * Get aspect summary for a planet (used in UI)
     */
    fun getAspectSummary(planet: Planet, analysis: DrigBalaAnalysis): String {
        val drigBala = analysis.planetaryDrigBala[planet] ?: return "No data available"
        return buildString {
            appendLine("Benefic: +${String.format("%.1f", drigBala.beneficVirupas)} virupas")
            appendLine("Malefic: -${String.format("%.1f", drigBala.maleficVirupas)} virupas")
            appendLine("Net: ${String.format("%.1f", drigBala.netDrigBala)} virupas")
            appendLine("Rating: ${drigBala.strengthRating}")
        }
    }
}
