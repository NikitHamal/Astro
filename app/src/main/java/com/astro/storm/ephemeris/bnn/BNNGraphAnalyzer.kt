package com.astro.storm.ephemeris.bnn

import com.astro.storm.core.model.Planet
import com.astro.storm.core.model.VedicChart
import com.astro.storm.core.model.ZodiacSign

/**
 * BNN Graph Analyzer
 *
 * Performs graph-based analysis of planetary connections in Bhrigu Nandi Nadi.
 * Uses recursive traversal to discover complex planetary links that reveal
 * career, relationship, health, and life event patterns.
 *
 * Key Concepts:
 * 1. Each planet is a node in the graph
 * 2. BNN aspects (1, 2, 5, 7, 9, 12) form edges between nodes
 * 3. Recursive traversal finds chains like Jupiter → Mars → Saturn
 * 4. Dispositor chains add another layer of connectivity
 * 5. Strength of links is computed from individual aspect strengths
 *
 * Classical Basis:
 * - Bhrigu Nandi Nadi manuscripts
 * - Dhruva Nadi principles
 * - R.G. Rao's Nadi interpretations
 *
 * @author AstroStorm - Ultra-Precision Vedic Astrology
 */
object BNNGraphAnalyzer {

    /**
     * Maximum depth for recursive graph traversal
     */
    private const val MAX_TRAVERSAL_DEPTH = 5

    /**
     * Minimum strength threshold for valid links
     */
    private const val MIN_LINK_STRENGTH = 0.3

    /**
     * Build the planetary graph from chart data
     */
    fun buildPlanetaryGraph(chart: VedicChart): Map<Planet, BNNGraphNode> {
        val positions = chart.planetPositions.associateBy { it.planet }
        val nodes = mutableMapOf<Planet, BNNGraphNode>()

        // Create nodes for all planets
        for (planet in Planet.MAIN_PLANETS) {
            val pos = positions[planet] ?: continue
            nodes[planet] = BNNGraphNode(
                planet = planet,
                sign = pos.sign,
                connections = mutableListOf()
            )
        }

        // Add edges based on BNN aspects
        for ((planet1, node1) in nodes) {
            for ((planet2, node2) in nodes) {
                if (planet1 == planet2) continue

                val signDistance = calculateSignDistance(node1.sign, node2.sign)
                if (signDistance in BNN_ASPECT_DISTANCES) {
                    val aspectType = getAspectType(signDistance)
                    node1.connections.add(BNNGraphEdge(
                        targetPlanet = planet2,
                        aspectType = aspectType,
                        signDistance = signDistance,
                        weight = aspectType.strength
                    ))
                }
            }
        }

        // Add dispositor connections (sign lord relationships)
        addDispositorConnections(nodes)

        return nodes
    }

    /**
     * BNN aspect distances
     */
    private val BNN_ASPECT_DISTANCES = setOf(1, 2, 5, 7, 9, 12)

    /**
     * Calculate sign distance (1-12)
     */
    private fun calculateSignDistance(from: ZodiacSign, to: ZodiacSign): Int {
        val distance = ((to.number - from.number + 12) % 12)
        return if (distance == 0) 1 else distance + 1
    }

    /**
     * Get BNN aspect type from sign distance
     */
    private fun getAspectType(signDistance: Int): BNNAspectType {
        return when (signDistance) {
            1 -> BNNAspectType.CONJUNCTION
            2, 12 -> BNNAspectType.SECOND_TWELFTH
            5, 9 -> BNNAspectType.FIFTH_NINTH
            7 -> BNNAspectType.SEVENTH
            else -> BNNAspectType.CONJUNCTION
        }
    }

    /**
     * Add dispositor (sign lord) connections to the graph
     */
    private fun addDispositorConnections(nodes: Map<Planet, BNNGraphNode>) {
        for ((planet, node) in nodes) {
            val signLord = node.sign.ruler
            if (signLord != planet && nodes.containsKey(signLord)) {
                // Check if connection already exists
                val existingConnection = node.connections.find { it.targetPlanet == signLord }
                if (existingConnection == null) {
                    node.connections.add(BNNGraphEdge(
                        targetPlanet = signLord,
                        aspectType = BNNAspectType.CONJUNCTION, // Dispositor treated as conjunction-strength
                        signDistance = 0, // Special marker for dispositor
                        weight = 0.7 // Dispositor connection strength
                    ))
                } else {
                    // Strengthen existing connection
                    val index = node.connections.indexOf(existingConnection)
                    node.connections[index] = existingConnection.copy(
                        weight = minOf(existingConnection.weight + 0.2, 1.0)
                    )
                }
            }
        }
    }

    /**
     * Find all planetary links using recursive graph traversal
     */
    fun findAllPlanetaryLinks(
        graph: Map<Planet, BNNGraphNode>,
        minLength: Int = 2,
        maxLength: Int = MAX_TRAVERSAL_DEPTH
    ): List<PlanetaryLink> {
        val allLinks = mutableListOf<PlanetaryLink>()

        // Start traversal from each planet
        for (startPlanet in Planet.MAIN_PLANETS) {
            val startNode = graph[startPlanet] ?: continue
            val visited = mutableSetOf(startPlanet)
            val currentPath = mutableListOf(startPlanet)
            val signPath = mutableListOf(startNode.sign)

            traverseGraph(
                graph = graph,
                currentPlanet = startPlanet,
                visited = visited,
                currentPath = currentPath,
                signPath = signPath,
                depth = 1,
                maxLength = maxLength,
                minLength = minLength,
                accumulatedStrength = 1.0,
                allLinks = allLinks
            )
        }

        // Remove duplicate links (same planets, different order)
        return allLinks
            .distinctBy { it.chain.toSet() }
            .filter { it.strength >= MIN_LINK_STRENGTH }
            .sortedByDescending { it.strength }
    }

    /**
     * Recursive graph traversal
     */
    private fun traverseGraph(
        graph: Map<Planet, BNNGraphNode>,
        currentPlanet: Planet,
        visited: MutableSet<Planet>,
        currentPath: MutableList<Planet>,
        signPath: MutableList<ZodiacSign>,
        depth: Int,
        maxLength: Int,
        minLength: Int,
        accumulatedStrength: Double,
        allLinks: MutableList<PlanetaryLink>
    ) {
        // If we have a valid length path, create a link
        if (currentPath.size >= minLength) {
            val link = createPlanetaryLink(
                chain = currentPath.toList(),
                signPath = signPath.toList(),
                strength = accumulatedStrength / (currentPath.size - 1) // Average strength
            )
            if (link != null && link.strength >= MIN_LINK_STRENGTH) {
                allLinks.add(link)
            }
        }

        // Stop if max depth reached
        if (depth >= maxLength) return

        // Get current node
        val currentNode = graph[currentPlanet] ?: return

        // Traverse all connections
        for (edge in currentNode.connections) {
            if (edge.targetPlanet in visited) continue

            val targetNode = graph[edge.targetPlanet] ?: continue

            // Add to path
            visited.add(edge.targetPlanet)
            currentPath.add(edge.targetPlanet)
            signPath.add(targetNode.sign)

            // Recurse
            traverseGraph(
                graph = graph,
                currentPlanet = edge.targetPlanet,
                visited = visited,
                currentPath = currentPath,
                signPath = signPath,
                depth = depth + 1,
                maxLength = maxLength,
                minLength = minLength,
                accumulatedStrength = accumulatedStrength + edge.weight,
                allLinks = allLinks
            )

            // Backtrack
            visited.remove(edge.targetPlanet)
            currentPath.removeLast()
            signPath.removeLast()
        }
    }

    /**
     * Create a planetary link from a path
     */
    private fun createPlanetaryLink(
        chain: List<Planet>,
        signPath: List<ZodiacSign>,
        strength: Double
    ): PlanetaryLink? {
        if (chain.size < 2) return null

        val linkType = determineLinkType(chain, signPath)
        val primaryIndication = getPrimaryIndication(chain)
        val secondaryIndications = getSecondaryIndications(chain)
        val interpretation = generateLinkInterpretation(chain, linkType)

        return PlanetaryLink(
            chain = chain,
            signPath = signPath,
            linkType = linkType,
            strength = strength,
            primaryIndication = primaryIndication,
            secondaryIndications = secondaryIndications,
            interpretation = interpretation
        )
    }

    /**
     * Determine the type of link based on chain composition
     */
    private fun determineLinkType(chain: List<Planet>, signPath: List<ZodiacSign>): LinkType {
        if (chain.size < 2) return LinkType.DIRECT_CONJUNCTION

        // Check for same sign (conjunction)
        if (signPath.distinct().size == 1) {
            return LinkType.DIRECT_CONJUNCTION
        }

        // Check for consecutive signs
        var allConsecutive = true
        for (i in 0 until signPath.size - 1) {
            val dist = calculateSignDistance(signPath[i], signPath[i + 1])
            if (dist !in listOf(2, 12)) {
                allConsecutive = false
                break
            }
        }
        if (allConsecutive) return LinkType.CONSECUTIVE_SIGN

        // Check for trine-based link
        var allTrine = true
        for (i in 0 until signPath.size - 1) {
            val dist = calculateSignDistance(signPath[i], signPath[i + 1])
            if (dist !in listOf(5, 9)) {
                allTrine = false
                break
            }
        }
        if (allTrine) return LinkType.TRINE_LINK

        // Check for opposition-based link
        var allOpposition = true
        for (i in 0 until signPath.size - 1) {
            val dist = calculateSignDistance(signPath[i], signPath[i + 1])
            if (dist != 7) {
                allOpposition = false
                break
            }
        }
        if (allOpposition) return LinkType.OPPOSITION_LINK

        // Check for dispositor chain
        var isDispositorChain = true
        for (i in 0 until chain.size - 1) {
            if (signPath[i].ruler != chain[i + 1]) {
                isDispositorChain = false
                break
            }
        }
        if (isDispositorChain) return LinkType.DISPOSITOR_CHAIN

        return LinkType.MIXED_LINK
    }

    /**
     * Get primary indication for a planetary chain
     */
    private fun getPrimaryIndication(chain: List<Planet>): String {
        val chainSet = chain.toSet()

        // Match against known career patterns
        for (pattern in CareerLinkPattern.entries) {
            if (chainSet.containsAll(pattern.planets)) {
                return "${pattern.careerField}: ${pattern.specificRoles.first()}"
            }
        }

        // Generate indication based on planet combination
        return when {
            Planet.SUN in chainSet && Planet.JUPITER in chainSet -> "Authority, Leadership, Teaching"
            Planet.MOON in chainSet && Planet.VENUS in chainSet -> "Arts, Creativity, Public Relations"
            Planet.MARS in chainSet && Planet.SATURN in chainSet -> "Engineering, Construction, Discipline"
            Planet.MERCURY in chainSet && Planet.JUPITER in chainSet -> "Writing, Education, Communication"
            Planet.VENUS in chainSet && Planet.MERCURY in chainSet -> "Arts, Commerce, Aesthetics"
            Planet.SATURN in chainSet && Planet.RAHU in chainSet -> "Technology, Foreign Connections, Innovation"
            Planet.JUPITER in chainSet && Planet.KETU in chainSet -> "Spirituality, Philosophy, Moksha"
            else -> "Combined influence of ${chain.joinToString(" + ") { it.displayName }}"
        }
    }

    /**
     * Get secondary indications for a planetary chain
     */
    private fun getSecondaryIndications(chain: List<Planet>): List<String> {
        val indications = mutableListOf<String>()
        val chainSet = chain.toSet()

        // Add indications based on planet presence
        if (Planet.SUN in chainSet) indications.add("Government, Father, Authority")
        if (Planet.MOON in chainSet) indications.add("Mind, Mother, Public, Emotions")
        if (Planet.MARS in chainSet) indications.add("Energy, Property, Siblings, Courage")
        if (Planet.MERCURY in chainSet) indications.add("Intelligence, Communication, Business")
        if (Planet.JUPITER in chainSet) indications.add("Wisdom, Children, Fortune, Dharma")
        if (Planet.VENUS in chainSet) indications.add("Relationships, Luxury, Arts, Beauty")
        if (Planet.SATURN in chainSet) indications.add("Career, Longevity, Service, Karma")
        if (Planet.RAHU in chainSet) indications.add("Ambition, Foreign, Unusual, Material")
        if (Planet.KETU in chainSet) indications.add("Spirituality, Past Life, Liberation, Intuition")

        return indications.distinct()
    }

    /**
     * Generate interpretation for a planetary link
     */
    private fun generateLinkInterpretation(chain: List<Planet>, linkType: LinkType): String {
        val chainStr = chain.joinToString(" → ") { it.displayName }

        return buildString {
            append("The planetary link $chainStr ")

            when (linkType) {
                LinkType.DIRECT_CONJUNCTION -> {
                    append("shows planets united in the same sign, creating a powerful fusion of energies. ")
                    append("The native strongly expresses the combined qualities of these planets. ")
                }
                LinkType.TRINE_LINK -> {
                    append("forms a harmonious chain through trine connections. ")
                    append("These planets support each other, bringing fortune in their combined significations. ")
                }
                LinkType.OPPOSITION_LINK -> {
                    append("creates a chain of oppositions requiring balance and awareness. ")
                    append("The native must integrate these opposing forces consciously. ")
                }
                LinkType.CONSECUTIVE_SIGN -> {
                    append("shows planets in adjacent signs, creating a flow of energy. ")
                    append("Each planet feeds into the next, creating a progressive chain of influence. ")
                }
                LinkType.DISPOSITOR_CHAIN -> {
                    append("reveals a dispositor chain where each planet is ruled by the next. ")
                    append("The final planet in the chain becomes the ultimate dispositor, greatly strengthening its influence. ")
                }
                LinkType.MIXED_LINK -> {
                    append("combines multiple types of connections. ")
                    append("This complex link requires careful analysis of each step in the chain. ")
                }
            }

            // Add specific interpretations for common chains
            val chainSet = chain.toSet()
            when {
                chainSet == setOf(Planet.JUPITER, Planet.MARS, Planet.SATURN) -> {
                    append("This classic combination indicates mechanical/engineering abilities, ")
                    append("physical strength with wisdom, and success in technical fields. ")
                }
                chainSet == setOf(Planet.MERCURY, Planet.SATURN, Planet.RAHU) -> {
                    append("This modern combination strongly indicates IT/software career, ")
                    append("analytical abilities, and success with technology. ")
                }
                chainSet == setOf(Planet.SUN, Planet.MARS, Planet.JUPITER) -> {
                    append("This royal combination indicates leadership, authority, ")
                    append("and potential for medicine (surgery) or government service. ")
                }
                chainSet == setOf(Planet.VENUS, Planet.MOON, Planet.MERCURY) -> {
                    append("This creative combination indicates artistic talents, ")
                    append("emotional intelligence, and success in creative fields. ")
                }
                chainSet == setOf(Planet.JUPITER, Planet.KETU, Planet.MOON) -> {
                    append("This spiritual combination indicates strong spiritual inclinations, ")
                    append("psychic abilities, and potential for teaching spiritual wisdom. ")
                }
            }
        }
    }

    /**
     * Find specific link patterns between given planets
     */
    fun findLinksBetweenPlanets(
        graph: Map<Planet, BNNGraphNode>,
        planet1: Planet,
        planet2: Planet,
        maxDepth: Int = 4
    ): List<PlanetaryLink> {
        val allLinks = mutableListOf<PlanetaryLink>()
        val startNode = graph[planet1] ?: return emptyList()
        val visited = mutableSetOf(planet1)
        val currentPath = mutableListOf(planet1)
        val signPath = mutableListOf(startNode.sign)

        findPathsToTarget(
            graph = graph,
            currentPlanet = planet1,
            targetPlanet = planet2,
            visited = visited,
            currentPath = currentPath,
            signPath = signPath,
            depth = 1,
            maxDepth = maxDepth,
            accumulatedStrength = 1.0,
            allLinks = allLinks
        )

        return allLinks.sortedByDescending { it.strength }
    }

    /**
     * Find paths to a specific target planet
     */
    private fun findPathsToTarget(
        graph: Map<Planet, BNNGraphNode>,
        currentPlanet: Planet,
        targetPlanet: Planet,
        visited: MutableSet<Planet>,
        currentPath: MutableList<Planet>,
        signPath: MutableList<ZodiacSign>,
        depth: Int,
        maxDepth: Int,
        accumulatedStrength: Double,
        allLinks: MutableList<PlanetaryLink>
    ) {
        // Check if we've reached target
        if (currentPlanet == targetPlanet && currentPath.size > 1) {
            val link = createPlanetaryLink(
                chain = currentPath.toList(),
                signPath = signPath.toList(),
                strength = accumulatedStrength / (currentPath.size - 1)
            )
            if (link != null) {
                allLinks.add(link)
            }
            return
        }

        if (depth >= maxDepth) return

        val currentNode = graph[currentPlanet] ?: return

        for (edge in currentNode.connections) {
            if (edge.targetPlanet in visited && edge.targetPlanet != targetPlanet) continue

            val targetNode = graph[edge.targetPlanet] ?: continue

            visited.add(edge.targetPlanet)
            currentPath.add(edge.targetPlanet)
            signPath.add(targetNode.sign)

            findPathsToTarget(
                graph = graph,
                currentPlanet = edge.targetPlanet,
                targetPlanet = targetPlanet,
                visited = visited,
                currentPath = currentPath,
                signPath = signPath,
                depth = depth + 1,
                maxDepth = maxDepth,
                accumulatedStrength = accumulatedStrength + edge.weight,
                allLinks = allLinks
            )

            if (edge.targetPlanet != targetPlanet) {
                visited.remove(edge.targetPlanet)
            }
            currentPath.removeLast()
            signPath.removeLast()
        }
    }

    /**
     * Analyze career potential from planetary links
     */
    fun analyzeCareerFromLinks(links: List<PlanetaryLink>): List<CareerIndication> {
        val careerIndications = mutableListOf<CareerIndication>()
        val matchedPatterns = mutableSetOf<CareerLinkPattern>()

        for (link in links) {
            val chainSet = link.chain.toSet()

            for (pattern in CareerLinkPattern.entries) {
                if (pattern in matchedPatterns) continue

                // Check if link contains the career pattern planets
                val matchCount = chainSet.intersect(pattern.planets).size
                val matchRatio = matchCount.toDouble() / pattern.planets.size

                if (matchRatio >= 0.67) { // At least 2/3 planets must match
                    matchedPatterns.add(pattern)

                    val confidence = calculateCareerConfidence(link, pattern, matchRatio)

                    careerIndications.add(CareerIndication(
                        primaryPlanets = link.chain.filter { it in pattern.planets },
                        supportingPlanets = link.chain.filter { it !in pattern.planets },
                        careerField = pattern.careerField,
                        specificRoles = pattern.specificRoles,
                        confidence = confidence,
                        explanation = generateCareerExplanation(link, pattern)
                    ))
                }
            }
        }

        return careerIndications.sortedByDescending { it.confidence }
    }

    /**
     * Calculate career confidence score
     */
    private fun calculateCareerConfidence(
        link: PlanetaryLink,
        pattern: CareerLinkPattern,
        matchRatio: Double
    ): Double {
        var confidence = matchRatio * 0.5 // Base from match ratio

        // Add link strength
        confidence += link.strength * 0.3

        // Bonus for exact match
        if (link.chain.toSet().containsAll(pattern.planets)) {
            confidence += 0.15
        }

        // Bonus for trine links (harmonious)
        if (link.linkType == LinkType.TRINE_LINK) {
            confidence += 0.05
        }

        return minOf(confidence, 1.0)
    }

    /**
     * Generate career explanation
     */
    private fun generateCareerExplanation(link: PlanetaryLink, pattern: CareerLinkPattern): String {
        val chainStr = link.chain.joinToString(" → ") { it.displayName }
        return buildString {
            append("The planetary link $chainStr strongly indicates potential in ${pattern.careerField}. ")
            append("Key planets ${pattern.planets.joinToString(", ") { it.displayName }} ")
            append("are connected through ${link.linkType.description.lowercase()}. ")
            append("Specific roles indicated: ${pattern.specificRoles.joinToString(", ")}.")
        }
    }

    /**
     * Analyze character traits from planetary links
     */
    fun analyzeCharacterTraits(links: List<PlanetaryLink>): List<CharacterTrait> {
        val traits = mutableListOf<CharacterTrait>()
        val processedPairs = mutableSetOf<Set<Planet>>()

        for (link in links) {
            // Analyze consecutive pairs in the chain
            for (i in 0 until link.chain.size - 1) {
                val pair = setOf(link.chain[i], link.chain[i + 1])
                if (pair in processedPairs) continue
                processedPairs.add(pair)

                val trait = getTraitForPlanetPair(pair.toList(), link.strength)
                if (trait != null) {
                    traits.add(trait)
                }
            }
        }

        return traits.distinctBy { it.trait }.sortedByDescending { it.strength }
    }

    /**
     * Get character trait for a planet pair
     */
    private fun getTraitForPlanetPair(planets: List<Planet>, baseStrength: Double): CharacterTrait? {
        val pair = planets.toSet()

        val (trait, description) = when {
            pair == setOf(Planet.SUN, Planet.MARS) -> "Leadership & Courage" to
                    "Natural leader with courage and initiative. Assertive personality."
            pair == setOf(Planet.SUN, Planet.JUPITER) -> "Wisdom & Authority" to
                    "Wise leader with strong moral values. Respected by others."
            pair == setOf(Planet.MOON, Planet.VENUS) -> "Emotional Sensitivity" to
                    "Highly sensitive and artistic. Strong aesthetic sense."
            pair == setOf(Planet.MARS, Planet.SATURN) -> "Disciplined Energy" to
                    "Persistent and hardworking. Ability to overcome obstacles."
            pair == setOf(Planet.MERCURY, Planet.JUPITER) -> "Intellectual Wisdom" to
                    "Sharp intellect with broad knowledge. Good communicator."
            pair == setOf(Planet.VENUS, Planet.MERCURY) -> "Artistic Intelligence" to
                    "Creative mind with business acumen. Charming personality."
            pair == setOf(Planet.JUPITER, Planet.KETU) -> "Spiritual Wisdom" to
                    "Deep spiritual understanding. Detached perspective on life."
            pair == setOf(Planet.SATURN, Planet.RAHU) -> "Ambitious Worker" to
                    "Hardworking with unconventional methods. Persistent ambition."
            pair == setOf(Planet.MOON, Planet.JUPITER) -> "Emotional Wisdom" to
                    "Emotionally balanced with good judgment. Nurturing nature."
            pair == setOf(Planet.SUN, Planet.SATURN) -> "Responsible Authority" to
                    "Takes responsibilities seriously. Patient rise to power."
            else -> return null
        }

        return CharacterTrait(
            trait = trait,
            sourcePlanets = planets,
            strength = baseStrength,
            description = description
        )
    }

    /**
     * Analyze relationship patterns from planetary links
     */
    fun analyzeRelationshipPatterns(links: List<PlanetaryLink>): List<RelationshipPattern> {
        val patterns = mutableListOf<RelationshipPattern>()

        // Key relationship planets: Venus, Moon, 7th lord, Jupiter
        val relationshipPlanets = setOf(Planet.VENUS, Planet.MOON, Planet.JUPITER, Planet.MARS)

        for (link in links) {
            val relationshipPlanetCount = link.chain.count { it in relationshipPlanets }
            if (relationshipPlanetCount >= 2) {
                val pattern = analyzeRelationshipLink(link)
                if (pattern != null) {
                    patterns.add(pattern)
                }
            }
        }

        return patterns.distinctBy { it.patternName }
    }

    /**
     * Analyze a link for relationship patterns
     */
    private fun analyzeRelationshipLink(link: PlanetaryLink): RelationshipPattern? {
        val chainSet = link.chain.toSet()

        return when {
            Planet.VENUS in chainSet && Planet.MOON in chainSet -> RelationshipPattern(
                patternName = "Romantic Sensitivity",
                planets = link.chain,
                relationshipType = "Romantic Partnership",
                description = "Deep emotional connection in relationships. Values beauty and harmony.",
                challenges = listOf("Over-sensitivity", "Dependency on partner's approval"),
                strengths = listOf("Romantic nature", "Emotional understanding", "Artistic bonding")
            )
            Planet.VENUS in chainSet && Planet.MARS in chainSet -> RelationshipPattern(
                patternName = "Passionate Partnership",
                planets = link.chain,
                relationshipType = "Passionate Relationship",
                description = "Intense and passionate approach to relationships.",
                challenges = listOf("Jealousy", "Conflicts from strong desires"),
                strengths = listOf("Passion", "Physical chemistry", "Direct expression of love")
            )
            Planet.VENUS in chainSet && Planet.SATURN in chainSet -> RelationshipPattern(
                patternName = "Committed Partnership",
                planets = link.chain,
                relationshipType = "Long-term Commitment",
                description = "Serious approach to relationships. Values commitment and stability.",
                challenges = listOf("Delayed marriage", "Emotional reserve"),
                strengths = listOf("Loyalty", "Long-lasting bonds", "Mature relationships")
            )
            Planet.VENUS in chainSet && Planet.JUPITER in chainSet -> RelationshipPattern(
                patternName = "Fortunate Partnership",
                planets = link.chain,
                relationshipType = "Blessed Relationship",
                description = "Fortune in relationships. Attracts beneficial partnerships.",
                challenges = listOf("Over-optimism", "Taking partner for granted"),
                strengths = listOf("Good fortune in love", "Wise choice of partner", "Growth through relationship")
            )
            Planet.VENUS in chainSet && Planet.RAHU in chainSet -> RelationshipPattern(
                patternName = "Unconventional Partnership",
                planets = link.chain,
                relationshipType = "Unusual Relationship",
                description = "Attracted to unconventional or foreign partners.",
                challenges = listOf("Unstable attractions", "Illusions in love"),
                strengths = listOf("Open-minded in love", "Cross-cultural relationships", "Breaking barriers")
            )
            Planet.MOON in chainSet && Planet.SATURN in chainSet -> RelationshipPattern(
                patternName = "Emotional Reserve",
                planets = link.chain,
                relationshipType = "Cautious Emotions",
                description = "Careful and reserved in emotional expression.",
                challenges = listOf("Emotional suppression", "Fear of rejection"),
                strengths = listOf("Emotional stability", "Practical approach", "Deep commitment")
            )
            else -> null
        }
    }

    /**
     * Analyze health indicators from planetary links
     */
    fun analyzeHealthIndicators(links: List<PlanetaryLink>): List<HealthIndicator> {
        val indicators = mutableListOf<HealthIndicator>()
        val processedBodyParts = mutableSetOf<String>()

        for (link in links) {
            // Check for health-sensitive combinations
            val chainSet = link.chain.toSet()

            // Mars-Saturn: Bones, muscles, injuries
            if (Planet.MARS in chainSet && Planet.SATURN in chainSet) {
                if ("Musculoskeletal" !in processedBodyParts) {
                    processedBodyParts.add("Musculoskeletal")
                    indicators.add(HealthIndicator(
                        bodyPart = "Musculoskeletal System",
                        planets = listOf(Planet.MARS, Planet.SATURN),
                        vulnerability = 0.6,
                        recommendations = listOf(
                            "Regular exercise with proper warm-up",
                            "Avoid overexertion",
                            "Calcium and vitamin D supplementation",
                            "Proper rest between physical activities"
                        )
                    ))
                }
            }

            // Sun-Saturn: Heart, vitality
            if (Planet.SUN in chainSet && Planet.SATURN in chainSet) {
                if ("Cardiovascular" !in processedBodyParts) {
                    processedBodyParts.add("Cardiovascular")
                    indicators.add(HealthIndicator(
                        bodyPart = "Cardiovascular System",
                        planets = listOf(Planet.SUN, Planet.SATURN),
                        vulnerability = 0.5,
                        recommendations = listOf(
                            "Regular cardiac checkups",
                            "Manage stress levels",
                            "Heart-healthy diet",
                            "Moderate exercise routine"
                        )
                    ))
                }
            }

            // Moon-Saturn: Digestion, mental health
            if (Planet.MOON in chainSet && Planet.SATURN in chainSet) {
                if ("Digestive/Mental" !in processedBodyParts) {
                    processedBodyParts.add("Digestive/Mental")
                    indicators.add(HealthIndicator(
                        bodyPart = "Digestive & Mental Health",
                        planets = listOf(Planet.MOON, Planet.SATURN),
                        vulnerability = 0.55,
                        recommendations = listOf(
                            "Manage anxiety and depression",
                            "Regular meal times",
                            "Avoid cold and heavy foods",
                            "Meditation and relaxation practices"
                        )
                    ))
                }
            }

            // Mercury-Rahu: Nervous system
            if (Planet.MERCURY in chainSet && Planet.RAHU in chainSet) {
                if ("Nervous" !in processedBodyParts) {
                    processedBodyParts.add("Nervous")
                    indicators.add(HealthIndicator(
                        bodyPart = "Nervous System",
                        planets = listOf(Planet.MERCURY, Planet.RAHU),
                        vulnerability = 0.5,
                        recommendations = listOf(
                            "Limit screen time and mental strain",
                            "Practice grounding exercises",
                            "Adequate sleep",
                            "Avoid excessive stimulants"
                        )
                    ))
                }
            }

            // Venus-Rahu: Reproductive, kidney
            if (Planet.VENUS in chainSet && Planet.RAHU in chainSet) {
                if ("Reproductive/Urinary" !in processedBodyParts) {
                    processedBodyParts.add("Reproductive/Urinary")
                    indicators.add(HealthIndicator(
                        bodyPart = "Reproductive & Urinary System",
                        planets = listOf(Planet.VENUS, Planet.RAHU),
                        vulnerability = 0.45,
                        recommendations = listOf(
                            "Stay well hydrated",
                            "Regular health screenings",
                            "Balanced diet low in sugar",
                            "Avoid excessive indulgence"
                        )
                    ))
                }
            }
        }

        return indicators.sortedByDescending { it.vulnerability }
    }
}
