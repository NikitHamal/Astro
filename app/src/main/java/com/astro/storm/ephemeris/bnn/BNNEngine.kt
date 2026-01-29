package com.astro.storm.ephemeris.bnn

import com.astro.storm.core.common.Language
import com.astro.storm.core.model.Planet
import com.astro.storm.core.model.Quality
import com.astro.storm.core.model.VedicChart
import com.astro.storm.core.model.ZodiacSign

/**
 * Bhrigu Nandi Nadi (BNN) Engine
 *
 * Main orchestration engine for complete Bhrigu Nandi Nadi analysis.
 * This engine coordinates all BNN sub-systems to produce comprehensive
 * Nadi-style chart analysis.
 *
 * The BNN system differs from standard Parashari astrology:
 * 1. NO house system - purely sign and planet based
 * 2. ALL planets aspect 1, 2, 5, 7, 9, 12 positions
 * 3. Focus on planetary LINKS and HANDSHAKES
 * 4. Career/event prediction through planetary combinations
 *
 * This engine provides:
 * - Complete BNN aspect analysis
 * - Handshake yoga identification
 * - Planetary link graph analysis
 * - Career indication mapping
 * - Character trait analysis
 * - Relationship pattern analysis
 * - Health indicator analysis
 * - Key interpretation generation
 *
 * Classical References:
 * - Bhrigu Nandi Nadi (palm leaf manuscripts)
 * - Dhruva Nadi
 * - Satya Jatakam
 * - R.G. Rao's Nadi interpretations
 *
 * @author AstroStorm - Ultra-Precision Vedic Astrology
 */
object BNNEngine {

    /**
     * Perform complete BNN analysis on a chart
     */
    fun analyze(
        chart: VedicChart,
        language: Language = Language.ENGLISH
    ): BNNAnalysisResult {
        // Step 1: Get planetary positions (sign-based, no houses)
        val planetaryPositions = extractPlanetaryPositions(chart)

        // Step 2: Calculate all BNN aspects
        val allAspects = BNNAspectCalculator.calculateAllAspects(chart)

        // Step 3: Find handshake yogas (mutual aspects)
        val handshakeYogas = BNNAspectCalculator.findHandshakeYogas(allAspects)

        // Step 4: Build planetary graph and find links
        val graph = BNNGraphAnalyzer.buildPlanetaryGraph(chart)
        val planetaryLinks = BNNGraphAnalyzer.findAllPlanetaryLinks(graph)

        // Step 5: Analyze career indications from links
        val careerIndications = BNNGraphAnalyzer.analyzeCareerFromLinks(planetaryLinks)

        // Step 6: Analyze character traits
        val characterTraits = BNNGraphAnalyzer.analyzeCharacterTraits(planetaryLinks)

        // Step 7: Analyze relationship patterns
        val relationshipPatterns = BNNGraphAnalyzer.analyzeRelationshipPatterns(planetaryLinks)

        // Step 8: Analyze health indicators
        val healthIndicators = BNNGraphAnalyzer.analyzeHealthIndicators(planetaryLinks)

        // Step 9: Generate key interpretations
        val keyInterpretations = generateKeyInterpretations(
            planetaryPositions = planetaryPositions,
            handshakeYogas = handshakeYogas,
            planetaryLinks = planetaryLinks,
            careerIndications = careerIndications
        )

        return BNNAnalysisResult(
            planetaryPositions = planetaryPositions,
            allAspects = allAspects,
            handshakeYogas = handshakeYogas,
            planetaryLinks = planetaryLinks,
            careerIndications = careerIndications,
            characterTraits = characterTraits,
            relationshipPatterns = relationshipPatterns,
            healthIndicators = healthIndicators,
            keyInterpretations = keyInterpretations,
            language = language
        )
    }

    /**
     * Extract planetary positions (sign-based only)
     */
    private fun extractPlanetaryPositions(chart: VedicChart): Map<Planet, ZodiacSign> {
        return chart.planetPositions
            .filter { it.planet in Planet.MAIN_PLANETS }
            .associate { it.planet to it.sign }
    }

    /**
     * Generate key interpretations for the chart
     */
    private fun generateKeyInterpretations(
        planetaryPositions: Map<Planet, ZodiacSign>,
        handshakeYogas: List<HandshakeYoga>,
        planetaryLinks: List<PlanetaryLink>,
        careerIndications: List<CareerIndication>
    ): List<String> {
        val interpretations = mutableListOf<String>()

        // 1. Strongest handshake yoga interpretation
        handshakeYogas.maxByOrNull { it.strength }?.let { strongest ->
            interpretations.add(
                "Strongest Yoga: ${strongest.yogaName} - The mutual connection between " +
                        "${strongest.planet1.displayName} and ${strongest.planet2.displayName} " +
                        "is highly significant. Life areas affected: ${strongest.lifeAreas.take(3).joinToString(", ")}."
            )
        }

        // 2. Most significant planetary link
        planetaryLinks.maxByOrNull { it.strength }?.let { strongest ->
            val chainStr = strongest.chain.joinToString(" → ") { it.displayName }
            interpretations.add(
                "Key Planetary Link: $chainStr - This ${strongest.linkType.description.lowercase()} " +
                        "indicates ${strongest.primaryIndication.lowercase()}."
            )
        }

        // 3. Top career indication
        careerIndications.maxByOrNull { it.confidence }?.let { top ->
            interpretations.add(
                "Primary Career Direction: ${top.careerField} - Strong potential for " +
                        "${top.specificRoles.take(2).joinToString(" or ")} based on " +
                        "${top.primaryPlanets.joinToString("-") { it.displayName }} combination."
            )
        }

        // 4. Dispositor analysis
        val ultimateDispositor = findUltimateDispositor(planetaryPositions)
        if (ultimateDispositor != null) {
            interpretations.add(
                "Ultimate Dispositor: ${ultimateDispositor.displayName} - This planet holds " +
                        "final authority in the chart. Its significations are especially prominent."
            )
        }

        // 5. Planetary groupings
        val groupings = analyzeSignGroupings(planetaryPositions)
        if (groupings.isNotEmpty()) {
            interpretations.addAll(groupings)
        }

        // 6. Nodes analysis (Rahu-Ketu axis)
        val rahuSign = planetaryPositions[Planet.RAHU]
        val ketuSign = planetaryPositions[Planet.KETU]
        if (rahuSign != null && ketuSign != null) {
            interpretations.add(
                "Karmic Axis: Rahu in ${rahuSign.displayName} (material focus) and " +
                        "Ketu in ${ketuSign.displayName} (spiritual release). " +
                        "Growth through ${rahuSign.element} experiences, " +
                        "releasing ${ketuSign.element} attachments."
            )
        }

        // 7. Benefic vs Malefic balance
        val beneficMaleficAnalysis = analyzeBeneficMaleficBalance(planetaryPositions, handshakeYogas)
        if (beneficMaleficAnalysis != null) {
            interpretations.add(beneficMaleficAnalysis)
        }

        // 8. Special Nadi Yogas
        val specialYogas = findSpecialNadiYogas(planetaryPositions)
        interpretations.addAll(specialYogas)

        return interpretations.take(10) // Limit to top 10 interpretations
    }

    /**
     * Find the ultimate dispositor of the chart
     */
    private fun findUltimateDispositor(positions: Map<Planet, ZodiacSign>): Planet? {
        // A planet is an ultimate dispositor if it's in its own sign or
        // if all dispositor chains eventually lead to it

        // Check for planets in own sign
        val planetsInOwnSign = positions.filter { (planet, sign) ->
            sign.ruler == planet
        }.keys

        if (planetsInOwnSign.size == 1) {
            return planetsInOwnSign.first()
        }

        // Check for mutual reception leading to one planet
        if (planetsInOwnSign.isEmpty()) {
            // Follow dispositor chain from each planet
            val finalDispositors = mutableMapOf<Planet, Planet>()

            for (planet in positions.keys) {
                var current = planet
                val visited = mutableSetOf<Planet>()

                while (current !in visited) {
                    visited.add(current)
                    val sign = positions[current] ?: break
                    val lord = sign.ruler

                    if (lord == current || lord !in positions.keys) {
                        finalDispositors[planet] = current
                        break
                    }
                    current = lord
                }

                // If we hit a loop, the last planet before loop is considered
                if (current in visited) {
                    finalDispositors[planet] = current
                }
            }

            // Find most common final dispositor
            val counts = finalDispositors.values.groupingBy { it }.eachCount()
            val mostCommon = counts.maxByOrNull { it.value }

            if (mostCommon != null && mostCommon.value >= positions.size / 2) {
                return mostCommon.key
            }
        }

        return planetsInOwnSign.firstOrNull()
    }

    /**
     * Analyze sign groupings (stelliums, empty signs, etc.)
     */
    private fun analyzeSignGroupings(positions: Map<Planet, ZodiacSign>): List<String> {
        val interpretations = mutableListOf<String>()

        // Count planets per sign
        val signCounts = positions.values.groupingBy { it }.eachCount()

        // Find stelliums (3+ planets in one sign)
        signCounts.filter { it.value >= 3 }.forEach { (sign, count) ->
            val planetsInSign = positions.filter { it.value == sign }.keys
            interpretations.add(
                "Stellium in ${sign.displayName}: $count planets " +
                        "(${planetsInSign.joinToString(", ") { it.displayName }}) concentrated here. " +
                        "Strong emphasis on ${sign.element} qualities and " +
                        "${sign.displayName} significations."
            )
        }

        // Analyze element distribution
        val elementCounts = positions.values.groupingBy { it.element }.eachCount()
        val dominantElement = elementCounts.maxByOrNull { it.value }
        if (dominantElement != null && dominantElement.value >= 4) {
            interpretations.add(
                "Element Dominance: ${dominantElement.key} element is dominant with " +
                        "${dominantElement.value} planets. " +
                        getElementInterpretation(dominantElement.key)
            )
        }

        // Analyze modality distribution (using Quality enum from ZodiacSign)
        val modalityCounts = positions.values.groupingBy { it.quality }.eachCount()
        val dominantModality = modalityCounts.maxByOrNull { it.value }
        if (dominantModality != null && dominantModality.value >= 4) {
            interpretations.add(
                "Modality Dominance: ${dominantModality.key.name} modality is dominant. " +
                        getModalityInterpretation(dominantModality.key)
            )
        }

        return interpretations
    }

    /**
     * Get interpretation for dominant element (String-based)
     */
    private fun getElementInterpretation(element: String): String {
        return when (element.lowercase()) {
            "fire" -> "The native is action-oriented, enthusiastic, and leadership-inclined."
            "earth" -> "The native is practical, grounded, and focused on material security."
            "air" -> "The native is intellectual, communicative, and socially oriented."
            "water" -> "The native is emotional, intuitive, and deeply sensitive."
            else -> "Mixed elemental qualities."
        }
    }

    /**
     * Get interpretation for dominant modality (Quality enum)
     */
    private fun getModalityInterpretation(quality: Quality): String {
        return when (quality) {
            Quality.CARDINAL -> "The native is initiative-taking, leadership-oriented, and starts new things."
            Quality.FIXED -> "The native is stable, persistent, and sees things through to completion."
            Quality.MUTABLE -> "The native is adaptable, flexible, and good at handling change."
        }
    }

    /**
     * Analyze benefic vs malefic balance in handshakes
     */
    private fun analyzeBeneficMaleficBalance(
        positions: Map<Planet, ZodiacSign>,
        handshakes: List<HandshakeYoga>
    ): String? {
        val benefics = setOf(Planet.JUPITER, Planet.VENUS, Planet.MERCURY, Planet.MOON)
        val malefics = setOf(Planet.SATURN, Planet.MARS, Planet.RAHU, Planet.KETU, Planet.SUN)

        var beneficHandshakes = 0
        var maleficHandshakes = 0
        var mixedHandshakes = 0

        for (yoga in handshakes) {
            val p1Benefic = yoga.planet1 in benefics
            val p2Benefic = yoga.planet2 in benefics

            when {
                p1Benefic && p2Benefic -> beneficHandshakes++
                !p1Benefic && !p2Benefic -> maleficHandshakes++
                else -> mixedHandshakes++
            }
        }

        return when {
            beneficHandshakes > maleficHandshakes + mixedHandshakes ->
                "Benefic Dominance: Chart shows predominantly benefic planetary connections. " +
                        "Fortune, wisdom, and positive outcomes are indicated in connected life areas."
            maleficHandshakes > beneficHandshakes + mixedHandshakes ->
                "Malefic Dominance: Chart shows strong malefic planetary connections. " +
                        "Challenges build character; hard work leads to achievements through discipline."
            mixedHandshakes > beneficHandshakes && mixedHandshakes > maleficHandshakes ->
                "Mixed Influences: Chart shows balanced benefic-malefic connections. " +
                        "Life experiences include both challenges and rewards in equal measure."
            else -> null
        }
    }

    /**
     * Find special Nadi yogas
     */
    private fun findSpecialNadiYogas(positions: Map<Planet, ZodiacSign>): List<String> {
        val yogas = mutableListOf<String>()

        // Saraswati Yoga: Jupiter, Venus, Mercury in kendra/trikona from each other
        if (arePlanetsInTrineOrKendra(positions, Planet.JUPITER, Planet.VENUS) &&
            arePlanetsInTrineOrKendra(positions, Planet.VENUS, Planet.MERCURY)
        ) {
            yogas.add(
                "Saraswati Yoga: Jupiter, Venus, and Mercury are connected through trine/kendra. " +
                        "Indicates high intelligence, learning, artistic talents, and eloquence."
            )
        }

        // Lakshmi Yoga: Venus strong with Jupiter aspect
        if (arePlanetsInTrineOrKendra(positions, Planet.VENUS, Planet.JUPITER)) {
            val venusSign = positions[Planet.VENUS]
            if (venusSign != null && (venusSign == ZodiacSign.TAURUS ||
                        venusSign == ZodiacSign.LIBRA || venusSign == ZodiacSign.PISCES)
            ) {
                yogas.add(
                    "Lakshmi Yoga: Venus is strong and connected to Jupiter. " +
                            "Indicates wealth, luxury, beauty, and material comforts."
                )
            }
        }

        // Gajakesari Yoga: Moon-Jupiter connection
        if (arePlanetsInTrineOrKendra(positions, Planet.MOON, Planet.JUPITER)) {
            yogas.add(
                "Gajakesari Yoga: Moon and Jupiter are in mutual kendra/trine. " +
                        "Indicates fame, wisdom, prosperity, and good reputation."
            )
        }

        // Budhaditya Yoga: Sun-Mercury conjunction
        val sunSign = positions[Planet.SUN]
        val mercurySign = positions[Planet.MERCURY]
        if (sunSign != null && sunSign == mercurySign) {
            yogas.add(
                "Budhaditya Yoga: Sun and Mercury are conjunct. " +
                        "Indicates intelligence, analytical abilities, and success in intellectual pursuits."
            )
        }

        // Neechabhanga: Debilitated planet with cancellation
        for ((planet, sign) in positions) {
            if (isDebilitated(planet, sign)) {
                if (hasNeechabhanga(planet, sign, positions)) {
                    yogas.add(
                        "Neechabhanga Raja Yoga: ${planet.displayName} is debilitated in " +
                                "${sign.displayName} but has cancellation. " +
                                "Initial struggles transform into significant achievements."
                    )
                }
            }
        }

        // Viparita Raja Yoga: Lords of 6, 8, 12 in each other's signs
        // (Simplified check - would need house positions for full implementation)

        // Kemadruma Yoga check (Moon alone without planet support)
        val moonSign = positions[Planet.MOON]
        if (moonSign != null) {
            val planetsNearMoon = positions.filter { (planet, sign) ->
                planet != Planet.MOON &&
                        (sign == moonSign ||
                                getSignDistance(moonSign, sign) == 2 ||
                                getSignDistance(moonSign, sign) == 12)
            }
            if (planetsNearMoon.isEmpty()) {
                yogas.add(
                    "Kemadruma Yoga indicated: Moon is isolated without planetary support. " +
                            "May indicate emotional challenges. Check for cancellations."
                )
            }
        }

        return yogas
    }

    /**
     * Check if two planets are in trine or kendra relationship
     */
    private fun arePlanetsInTrineOrKendra(
        positions: Map<Planet, ZodiacSign>,
        planet1: Planet,
        planet2: Planet
    ): Boolean {
        val sign1 = positions[planet1] ?: return false
        val sign2 = positions[planet2] ?: return false

        val distance = getSignDistance(sign1, sign2)

        // Kendra: 1, 4, 7, 10 (distances: 1, 4, 7, 10)
        // Trikona: 1, 5, 9 (distances: 1, 5, 9)
        return distance in listOf(1, 4, 5, 7, 9, 10)
    }

    /**
     * Get sign distance (1-12)
     */
    private fun getSignDistance(from: ZodiacSign, to: ZodiacSign): Int {
        val distance = ((to.number - from.number + 12) % 12)
        return if (distance == 0) 1 else distance + 1
    }

    /**
     * Check if planet is debilitated
     */
    private fun isDebilitated(planet: Planet, sign: ZodiacSign): Boolean {
        return when (planet) {
            Planet.SUN -> sign == ZodiacSign.LIBRA
            Planet.MOON -> sign == ZodiacSign.SCORPIO
            Planet.MARS -> sign == ZodiacSign.CANCER
            Planet.MERCURY -> sign == ZodiacSign.PISCES
            Planet.JUPITER -> sign == ZodiacSign.CAPRICORN
            Planet.VENUS -> sign == ZodiacSign.VIRGO
            Planet.SATURN -> sign == ZodiacSign.ARIES
            else -> false
        }
    }

    /**
     * Check for Neechabhanga (debilitation cancellation)
     */
    private fun hasNeechabhanga(
        planet: Planet,
        sign: ZodiacSign,
        positions: Map<Planet, ZodiacSign>
    ): Boolean {
        // Rule 1: Lord of debilitation sign is in kendra from Moon
        val debilitationLord = sign.ruler
        val lordSign = positions[debilitationLord]
        val moonSign = positions[Planet.MOON]

        if (lordSign != null && moonSign != null) {
            val distFromMoon = getSignDistance(moonSign, lordSign)
            if (distFromMoon in listOf(1, 4, 7, 10)) return true
        }

        // Rule 2: Exaltation lord aspects or conjoins debilitated planet
        val exaltationSign = getExaltationSign(planet)
        if (exaltationSign != null) {
            val exaltLord = exaltationSign.ruler
            val exaltLordSign = positions[exaltLord]

            if (exaltLordSign != null) {
                val dist = getSignDistance(sign, exaltLordSign)
                // Conjunction or BNN aspect
                if (dist in listOf(1, 5, 7, 9)) return true
            }
        }

        // Rule 3: Debilitated planet in kendra from Lagna or Moon
        // (Would need Lagna for full check)

        return false
    }

    /**
     * Get exaltation sign for a planet
     */
    private fun getExaltationSign(planet: Planet): ZodiacSign? {
        return when (planet) {
            Planet.SUN -> ZodiacSign.ARIES
            Planet.MOON -> ZodiacSign.TAURUS
            Planet.MARS -> ZodiacSign.CAPRICORN
            Planet.MERCURY -> ZodiacSign.VIRGO
            Planet.JUPITER -> ZodiacSign.CANCER
            Planet.VENUS -> ZodiacSign.PISCES
            Planet.SATURN -> ZodiacSign.LIBRA
            Planet.RAHU -> ZodiacSign.TAURUS // As per some schools
            Planet.KETU -> ZodiacSign.SCORPIO
            else -> null
        }
    }

    /**
     * Find specific planetary combination for career analysis
     */
    fun analyzeCareer(chart: VedicChart): List<CareerIndication> {
        val graph = BNNGraphAnalyzer.buildPlanetaryGraph(chart)
        val links = BNNGraphAnalyzer.findAllPlanetaryLinks(graph)
        return BNNGraphAnalyzer.analyzeCareerFromLinks(links)
    }

    /**
     * Find handshake between specific planets
     */
    fun findHandshakeBetween(
        chart: VedicChart,
        planet1: Planet,
        planet2: Planet
    ): HandshakeYoga? {
        val aspects = BNNAspectCalculator.calculateAllAspects(chart)
        val handshakes = BNNAspectCalculator.findHandshakeYogas(aspects)

        return handshakes.find {
            (it.planet1 == planet1 && it.planet2 == planet2) ||
                    (it.planet1 == planet2 && it.planet2 == planet1)
        }
    }

    /**
     * Find all links involving a specific planet
     */
    fun findLinksInvolvingPlanet(chart: VedicChart, planet: Planet): List<PlanetaryLink> {
        val graph = BNNGraphAnalyzer.buildPlanetaryGraph(chart)
        val allLinks = BNNGraphAnalyzer.findAllPlanetaryLinks(graph)

        return allLinks.filter { it.containsPlanet(planet) }
    }

    /**
     * Get relationship analysis
     */
    fun analyzeRelationships(chart: VedicChart): List<RelationshipPattern> {
        val graph = BNNGraphAnalyzer.buildPlanetaryGraph(chart)
        val links = BNNGraphAnalyzer.findAllPlanetaryLinks(graph)
        return BNNGraphAnalyzer.analyzeRelationshipPatterns(links)
    }

    /**
     * Get health analysis
     */
    fun analyzeHealth(chart: VedicChart): List<HealthIndicator> {
        val graph = BNNGraphAnalyzer.buildPlanetaryGraph(chart)
        val links = BNNGraphAnalyzer.findAllPlanetaryLinks(graph)
        return BNNGraphAnalyzer.analyzeHealthIndicators(links)
    }

    /**
     * Get quick career summary
     */
    fun getCareerSummary(chart: VedicChart): String {
        val indications = analyzeCareer(chart)

        if (indications.isEmpty()) {
            return "Career analysis requires further examination of divisional charts and dashas."
        }

        val top3 = indications.take(3)
        return buildString {
            appendLine("Top Career Indications (BNN Analysis):")
            top3.forEachIndexed { index, career ->
                appendLine("${index + 1}. ${career.careerField}")
                appendLine("   Confidence: ${String.format("%.0f", career.confidence * 100)}%")
                appendLine("   Roles: ${career.specificRoles.take(3).joinToString(", ")}")
            }
        }
    }
}
