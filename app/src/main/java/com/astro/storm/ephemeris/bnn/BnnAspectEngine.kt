package com.astro.storm.ephemeris.bnn

import com.astro.storm.core.model.Planet
import com.astro.storm.core.model.VedicChart
import com.astro.storm.core.model.ZodiacSign

/**
 * Bhrigu Nandi Nadi (BNN) Aspect Engine
 *
 * BNN uses sign-based relationships and planetary handshakes instead of houses or lagna.
 * This engine focuses on graha-to-graha links using the classical BNN aspect distances
 * (1, 2, 5, 7, 9, 12 signs apart) and discovers chained links via recursive traversal.
 */
object BnnAspectEngine {

    private val BNN_ASPECT_DISTANCES = setOf(1, 2, 5, 7, 9, 12)
    private val CONSECUTIVE_DISTANCES = setOf(2, 12)

    data class BnnPlanetNode(
        val planet: Planet,
        val sign: ZodiacSign,
        val longitude: Double
    )

    data class BnnAspectLink(
        val from: Planet,
        val to: Planet,
        val distanceSigns: Int,
        val isHandshake: Boolean,
        val isConsecutive: Boolean
    )

    data class BnnPlanetaryLink(
        val path: List<Planet>,
        val distances: List<Int>
    )

    data class BnnAnalysis(
        val chartId: Long,
        val planetNodes: List<BnnPlanetNode>,
        val aspectLinks: List<BnnAspectLink>,
        val planetaryLinks: List<BnnPlanetaryLink>,
        val consecutiveChains: List<BnnPlanetaryLink>,
        val timestamp: Long = System.currentTimeMillis()
    )

    fun analyze(chart: VedicChart, maxDepth: Int = 4): BnnAnalysis {
        val planetNodes = chart.planetPositions
            .filter { it.planet in Planet.MAIN_PLANETS }
            .map { position ->
                BnnPlanetNode(
                    planet = position.planet,
                    sign = position.sign,
                    longitude = position.longitude
                )
            }

        val aspectLinks = buildAspectLinks(planetNodes)
        val adjacency = buildAdjacency(aspectLinks)
        val planetaryLinks = buildPlanetaryLinks(adjacency, maxDepth)
        val consecutiveChains = planetaryLinks.filter { link ->
            link.distances.all { it in CONSECUTIVE_DISTANCES }
        }

        return BnnAnalysis(
            chartId = chart.id,
            planetNodes = planetNodes,
            aspectLinks = aspectLinks,
            planetaryLinks = planetaryLinks,
            consecutiveChains = consecutiveChains
        )
    }

    private fun buildAspectLinks(nodes: List<BnnPlanetNode>): List<BnnAspectLink> {
        val links = mutableListOf<BnnAspectLink>()
        val nodeMap = nodes.associateBy { it.planet }
        nodes.forEach { fromNode ->
            nodes.forEach { toNode ->
                if (fromNode.planet == toNode.planet) return@forEach
                val distance = signDistance(fromNode.sign, toNode.sign)
                if (distance in BNN_ASPECT_DISTANCES) {
                    val isHandshake = isMutualAspect(nodeMap, fromNode, toNode)
                    links.add(
                        BnnAspectLink(
                            from = fromNode.planet,
                            to = toNode.planet,
                            distanceSigns = distance,
                            isHandshake = isHandshake,
                            isConsecutive = distance in CONSECUTIVE_DISTANCES
                        )
                    )
                }
            }
        }
        return links.distinctBy { listOf(it.from, it.to, it.distanceSigns) }
    }

    private fun isMutualAspect(
        nodeMap: Map<Planet, BnnPlanetNode>,
        fromNode: BnnPlanetNode,
        toNode: BnnPlanetNode
    ): Boolean {
        val backNode = nodeMap[fromNode.planet] ?: return false
        val reverseDistance = signDistance(toNode.sign, backNode.sign)
        return reverseDistance in BNN_ASPECT_DISTANCES
    }

    private fun buildAdjacency(links: List<BnnAspectLink>): Map<Planet, List<Pair<Planet, Int>>> {
        val map = mutableMapOf<Planet, MutableList<Pair<Planet, Int>>>()
        links.forEach { link ->
            map.getOrPut(link.from) { mutableListOf() }.add(link.to to link.distanceSigns)
        }
        return map
    }

    private fun buildPlanetaryLinks(
        adjacency: Map<Planet, List<Pair<Planet, Int>>>,
        maxDepth: Int
    ): List<BnnPlanetaryLink> {
        val unique = mutableSetOf<String>()
        val results = mutableListOf<BnnPlanetaryLink>()

        fun dfs(current: Planet, path: MutableList<Planet>, distances: MutableList<Int>) {
            if (path.size >= maxDepth) return
            val neighbors = adjacency[current].orEmpty()
            neighbors.forEach { (next, distance) ->
                if (next in path) return@forEach
                path.add(next)
                distances.add(distance)

                if (path.size >= 2) {
                    val signature = path.joinToString("-") { it.name }
                    if (unique.add(signature)) {
                        results.add(BnnPlanetaryLink(path.toList(), distances.toList()))
                    }
                }

                dfs(next, path, distances)
                path.removeAt(path.lastIndex)
                distances.removeAt(distances.lastIndex)
            }
        }

        adjacency.keys.forEach { planet ->
            dfs(planet, mutableListOf(planet), mutableListOf())
        }

        return results
    }

    private fun signDistance(from: ZodiacSign, to: ZodiacSign): Int {
        val diff = (to.number - from.number + 12) % 12
        return diff + 1
    }
}
