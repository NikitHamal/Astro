package com.astro.vajra.ephemeris.kp

import com.astro.vajra.core.model.Planet
import com.astro.vajra.core.model.ZodiacSign
import com.astro.vajra.ephemeris.AstrologicalConstants

/**
 * KP Significator Calculator
 *
 * Implements the 4-step KP method for determining house significators.
 * In KP system, the strength of signification follows this hierarchy:
 *
 * Step 1 (Strongest): Planets in the nakshatra (star) of occupants of the house
 * Step 2: Occupants of the house
 * Step 3: Planets in the nakshatra (star) of the owner of the house
 * Step 4 (Weakest): Owner of the house (sign lord of cusp)
 *
 * Additionally, planets conjoined with or aspecting the significators act as modifiers.
 *
 * Reference: K.S. Krishnamurti, KP Reader Vol. 2, Chapter on Significators
 */
object KPSignificatorCalculator {

    /** Conjunction orb in KP system (8° standard) */
    private const val CONJUNCTION_ORB = 8.0

    /**
     * Calculate the complete significator table for all 12 houses.
     *
     * @param cuspResults KP analysis of all 12 cusps
     * @param planetResults KP analysis of all planets
     * @param cusps Raw cusp degree values for house boundary determination
     * @return Complete significator table with planet-to-house mapping
     */
    fun calculateSignificators(
        cuspResults: List<KPCuspResult>,
        planetResults: List<KPPlanetResult>,
        cusps: List<Double>
    ): KPSignificatorTable {
        // Pre-compute: which planets occupy which houses
        val occupantsOfHouse = mutableMapOf<Int, MutableList<Planet>>()
        for (i in 1..12) occupantsOfHouse[i] = mutableListOf()
        for (pp in planetResults) {
            occupantsOfHouse[pp.house]?.add(pp.planet)
        }

        // Pre-compute: star lord mapping for each planet
        val planetStarLords = planetResults.associate { it.planet to it.starLord }

        // Pre-compute: which planets are in whose star
        // Key: star lord planet, Value: list of planets in that star lord's nakshatras
        val planetsInStarOf = mutableMapOf<Planet, MutableList<Planet>>()
        for (pp in planetResults) {
            planetsInStarOf.getOrPut(pp.starLord) { mutableListOf() }.add(pp.planet)
        }

        // Pre-compute: planet longitudes for conjunction/aspect checks
        val planetLongitudes = planetResults.associate { it.planet to it.longitude }

        // Compute significators for each house
        val houses = mutableMapOf<Int, KPHouseSignificators>()

        for (houseNum in 1..12) {
            val cuspResult = cuspResults[houseNum - 1]

            // Owner of the house = sign lord of the cusp
            val owner = cuspResult.signLord

            // Step 1: Planets in the star of occupants of this house
            val occupants = occupantsOfHouse[houseNum] ?: emptyList()
            val starlordOfOccupants = mutableListOf<Planet>()
            for (occupant in occupants) {
                val planetsInStar = planetsInStarOf[occupant] ?: emptyList()
                starlordOfOccupants.addAll(planetsInStar)
            }

            // Step 2: Occupants (already computed)

            // Step 3: Planets in the star of the owner
            val starlordOfOwner = planetsInStarOf[owner] ?: emptyList()

            // Step 4: Owner (already computed)

            // Conjunctions and aspects on significators
            val allSignificators = (starlordOfOccupants + occupants + starlordOfOwner + listOf(owner)).distinct()
            val conjoinedAspected = findConjoinedAspected(
                significators = allSignificators,
                allPlanets = planetResults,
                planetLongitudes = planetLongitudes
            )

            houses[houseNum] = KPHouseSignificators(
                houseNumber = houseNum,
                starlordOfOccupants = starlordOfOccupants.distinct(),
                occupants = occupants,
                starlordOfOwner = starlordOfOwner.toList(),
                owner = owner,
                conjoinedAspected = conjoinedAspected
            )
        }

        // Build reverse mapping: planet -> set of houses it signifies
        val planetSignifications = buildPlanetSignifications(houses)

        return KPSignificatorTable(
            houses = houses,
            planetSignifications = planetSignifications
        )
    }

    /**
     * Find planets conjoined with or aspecting the given significators.
     * These are modifier planets that influence the signification.
     */
    private fun findConjoinedAspected(
        significators: List<Planet>,
        allPlanets: List<KPPlanetResult>,
        planetLongitudes: Map<Planet, Double>
    ): List<Planet> {
        val result = mutableSetOf<Planet>()

        for (sigPlanet in significators) {
            val sigLongitude = planetLongitudes[sigPlanet] ?: continue

            for (pp in allPlanets) {
                if (pp.planet == sigPlanet) continue
                if (pp.planet in significators) continue

                // Check conjunction (within orb)
                val distance = AstrologicalConstants.angularDistance(sigLongitude, pp.longitude)
                if (distance <= CONJUNCTION_ORB) {
                    result.add(pp.planet)
                    continue
                }

                // Check Vedic aspects (7th, and special aspects for Mars/Jupiter/Saturn)
                if (hasVedicAspect(pp.planet, pp.longitude, sigLongitude)) {
                    result.add(pp.planet)
                }
            }
        }

        return result.toList()
    }

    /**
     * Check if a planet casts a Vedic (Parashari) aspect on a target degree.
     *
     * All planets aspect 7th from them (180°).
     * Mars additionally aspects 4th (90°) and 8th (210°).
     * Jupiter additionally aspects 5th (120°) and 9th (240°).
     * Saturn additionally aspects 3rd (60°) and 10th (270°).
     */
    private fun hasVedicAspect(
        aspectingPlanet: Planet,
        aspectingLongitude: Double,
        targetLongitude: Double
    ): Boolean {
        val distance = AstrologicalConstants.normalizeDegree(targetLongitude - aspectingLongitude)
        val aspectOrb = 10.0

        // 7th aspect (all planets)
        if (kotlin.math.abs(distance - 180.0) <= aspectOrb) return true

        // Special aspects
        when (aspectingPlanet) {
            Planet.MARS -> {
                if (kotlin.math.abs(distance - 90.0) <= aspectOrb) return true  // 4th
                if (kotlin.math.abs(distance - 210.0) <= aspectOrb) return true // 8th
            }
            Planet.JUPITER -> {
                if (kotlin.math.abs(distance - 120.0) <= aspectOrb) return true // 5th
                if (kotlin.math.abs(distance - 240.0) <= aspectOrb) return true // 9th
            }
            Planet.SATURN -> {
                if (kotlin.math.abs(distance - 60.0) <= aspectOrb) return true  // 3rd
                if (kotlin.math.abs(distance - 270.0) <= aspectOrb) return true // 10th
            }
            else -> { /* No special aspects */ }
        }

        return false
    }

    /**
     * Build reverse mapping: for each planet, determine which houses it signifies.
     * A planet signifies house N if it appears in any of the 4 steps for house N.
     */
    private fun buildPlanetSignifications(
        houses: Map<Int, KPHouseSignificators>
    ): Map<Planet, Set<Int>> {
        val result = mutableMapOf<Planet, MutableSet<Int>>()

        for ((houseNum, sig) in houses) {
            for (planet in sig.allSignificators) {
                result.getOrPut(planet) { mutableSetOf() }.add(houseNum)
            }
        }

        return result
    }
}
