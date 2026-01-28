package com.astro.storm.ephemeris.bnn

import com.astro.storm.core.model.Planet
import com.astro.storm.core.model.VedicChart
import com.astro.storm.core.model.ZodiacSign

/**
 * Bhrigu Nandi Nadi (BNN) Aspect Engine
 *
 * Uses Nadi aspect rules (1, 5, 9, 2, 12, 7) and consecutive-sign handshakes
 * to build planetary link chains for Nadi-style interpretation.
 */
object BnnAspectEngine {

    private val NADI_ASPECTS = mapOf(
        1 to BnnAspectType.CONJUNCTION,
        2 to BnnAspectType.DWITIYA,
        5 to BnnAspectType.PANCHAMA,
        7 to BnnAspectType.SAPTAMA,
        9 to BnnAspectType.NAVAMA,
        12 to BnnAspectType.DWADASA
    )

    private val PLANET_KEYWORDS = mapOf(
        Planet.SUN to listOf("authority", "leadership", "vitality"),
        Planet.MOON to listOf("public", "care", "nurture"),
        Planet.MARS to listOf("engineering", "mechanics", "action"),
        Planet.MERCURY to listOf("analysis", "communication", "tech"),
        Planet.JUPITER to listOf("knowledge", "guidance", "expansion"),
        Planet.VENUS to listOf("design", "arts", "harmony"),
        Planet.SATURN to listOf("structure", "systems", "industry"),
        Planet.RAHU to listOf("innovation", "research", "unconventional"),
        Planet.KETU to listOf("insight", "precision", "specialization")
    )

    private val SIGNATURE_PATTERNS = listOf(
        BnnSignaturePattern(
            name = "Mechanical Engineering",
            required = setOf(Planet.JUPITER, Planet.MARS, Planet.SATURN),
            description = "Jupiter-Mars-Saturn links indicate applied knowledge, mechanics, and structural discipline."
        ),
        BnnSignaturePattern(
            name = "Technology & Analytics",
            required = setOf(Planet.MERCURY, Planet.RAHU, Planet.SATURN),
            description = "Mercury-Rahu-Saturn chains highlight analytical tech, systems, and modern tools."
        ),
        BnnSignaturePattern(
            name = "Creative Design",
            required = setOf(Planet.VENUS, Planet.MERCURY, Planet.MOON),
            description = "Venus-Mercury-Moon links support aesthetics, communication, and public resonance."
        )
    )

    data class BnnPlanetaryLink(
        val from: Planet,
        val to: Planet,
        val distance: Int,
        val aspectType: BnnAspectType,
        val relationType: BnnRelationType
    )

    data class BnnLinkChain(
        val planets: List<Planet>,
        val relationTypes: List<BnnRelationType>,
        val description: String
    )

    data class BnnSignature(
        val title: String,
        val description: String,
        val contributingChains: List<BnnLinkChain>
    )

    data class BnnAnalysisResult(
        val chart: VedicChart,
        val links: List<BnnPlanetaryLink>,
        val chains: List<BnnLinkChain>,
        val signatures: List<BnnSignature>
    )

    enum class BnnAspectType(val displayName: String) {
        CONJUNCTION("Conjunction"),
        DWITIYA("2nd"),
        PANCHAMA("5th"),
        SAPTAMA("7th"),
        NAVAMA("9th"),
        DWADASA("12th")
    }

    enum class BnnRelationType(val displayName: String) {
        NADI_ASPECT("Nadi Aspect"),
        CONSECUTIVE_HANDSHAKE("Consecutive Handshake")
    }

    data class BnnSignaturePattern(
        val name: String,
        val required: Set<Planet>,
        val description: String
    )

    fun analyze(chart: VedicChart): BnnAnalysisResult {
        val positions = chart.planetPositions.filter { it.planet in Planet.MAIN_PLANETS }
        val links = mutableListOf<BnnPlanetaryLink>()

        for (from in positions) {
            for (to in positions) {
                if (from.planet == to.planet) continue
                val distance = signDistance(from.sign, to.sign)
                val aspectType = NADI_ASPECTS[distance]
                if (aspectType != null) {
                    links.add(
                        BnnPlanetaryLink(
                            from = from.planet,
                            to = to.planet,
                            distance = distance,
                            aspectType = aspectType,
                            relationType = BnnRelationType.NADI_ASPECT
                        )
                    )
                }
                if (distance == 2 || distance == 12) {
                    links.add(
                        BnnPlanetaryLink(
                            from = from.planet,
                            to = to.planet,
                            distance = distance,
                            aspectType = aspectType ?: BnnAspectType.DWITIYA,
                            relationType = BnnRelationType.CONSECUTIVE_HANDSHAKE
                        )
                    )
                }
            }
        }

        val chains = buildChains(links)
        val signatures = deriveSignatures(chains)

        return BnnAnalysisResult(
            chart = chart,
            links = links.distinctBy { "${it.from}-${it.to}-${it.relationType}-${it.aspectType}" },
            chains = chains,
            signatures = signatures
        )
    }

    private fun signDistance(from: ZodiacSign, to: ZodiacSign): Int {
        val diff = (to.number - from.number + 12) % 12
        return if (diff == 0) 1 else diff + 1
    }

    private fun buildChains(links: List<BnnPlanetaryLink>): List<BnnLinkChain> {
        val adjacency = links.groupBy { it.from }
        val chains = mutableListOf<BnnLinkChain>()
        val maxDepth = 4

        fun dfs(path: MutableList<Planet>, relations: MutableList<BnnRelationType>) {
            val current = path.last()
            val nextLinks = adjacency[current].orEmpty()
            for (link in nextLinks) {
                if (path.contains(link.to)) continue
                path.add(link.to)
                relations.add(link.relationType)

                if (path.size >= 3) {
                    val description = buildChainDescription(path)
                    chains.add(BnnLinkChain(path.toList(), relations.toList(), description))
                }

                if (path.size < maxDepth) {
                    dfs(path, relations)
                }

                path.removeAt(path.size - 1)
                relations.removeAt(relations.size - 1)
            }
        }

        val planets = links.map { it.from }.toSet()
        for (planet in planets) {
            dfs(mutableListOf(planet), mutableListOf())
        }

        return chains.distinctBy { it.planets.joinToString("-") }
    }

    private fun buildChainDescription(planets: List<Planet>): String {
        val keywords = planets.flatMap { PLANET_KEYWORDS[it].orEmpty() }.distinct()
        val theme = keywords.take(3).joinToString(", ")
        return "Chain expresses $theme"
    }

    private fun deriveSignatures(chains: List<BnnLinkChain>): List<BnnSignature> {
        val signatures = mutableListOf<BnnSignature>()
        for (pattern in SIGNATURE_PATTERNS) {
            val matching = chains.filter { chain -> pattern.required.all { it in chain.planets } }
            if (matching.isNotEmpty()) {
                signatures.add(
                    BnnSignature(
                        title = pattern.name,
                        description = pattern.description,
                        contributingChains = matching
                    )
                )
            }
        }
        return signatures
    }
}
