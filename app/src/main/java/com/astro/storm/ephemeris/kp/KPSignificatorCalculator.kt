package com.astro.storm.ephemeris.kp

import com.astro.storm.core.model.Planet
import com.astro.storm.core.model.VedicChart
import com.astro.storm.core.model.ZodiacSign
import com.astro.storm.ephemeris.kp.getCuspLongitude

/**
 * KP Significator Calculator
 *
 * Calculates planet significators according to KP rules.
 * In KP, planets signify houses in a specific order of strength:
 *
 * LEVEL 1 (Strongest): Houses occupied by planets posited in the star of this planet
 * LEVEL 2: Houses owned by planets posited in the star of this planet
 * LEVEL 3: House occupied by the planet itself
 * LEVEL 4 (Weakest): Houses owned by the planet itself
 *
 * The star-lord (Nakshatra lord) is crucial - a planet primarily delivers
 * the results of its star-lord's significations.
 *
 * @author AstroStorm - Ultra-Precision Vedic Astrology
 */
object KPSignificatorCalculator {

    /**
     * Calculate complete significators for all planets
     */
    fun calculateAllSignificators(chart: VedicChart): Map<Planet, KPSignificators> {
        val positions = chart.planetPositions.associateBy { it.planet }
        val significatorMap = mutableMapOf<Planet, KPSignificators>()

        // First, build basic data structures
        val planetHouses = mutableMapOf<Planet, Int>()
        val planetStarLords = mutableMapOf<Planet, Planet>()
        val ownedHousesMap = buildOwnedHousesMap(chart)

        for ((planet, pos) in positions) {
            planetHouses[planet] = pos.house
            planetStarLords[planet] = pos.nakshatra.ruler
        }

        // Find which planets are in each planet's stars
        val planetsInStars = mutableMapOf<Planet, MutableList<Planet>>()
        for (starLord in Planet.MAIN_PLANETS) {
            planetsInStars[starLord] = mutableListOf()
        }
        for ((planet, starLord) in planetStarLords) {
            planetsInStars[starLord]?.add(planet)
        }

        // Calculate significators for each planet
        for (planet in Planet.MAIN_PLANETS) {
            if (planet !in positions) continue

            val occupiedHouse = planetHouses[planet] ?: 1
            val ownedHouses = ownedHousesMap[planet] ?: emptyList()
            val planetsInMyStar = planetsInStars[planet] ?: emptyList()

            // Level 1: Houses OCCUPIED by planets in my star
            val primarySignifications = planetsInMyStar
                .mapNotNull { planetHouses[it] }
                .distinct()

            // Level 2: Houses OWNED by planets in my star
            val secondarySignifications = planetsInMyStar
                .flatMap { ownedHousesMap[it] ?: emptyList() }
                .distinct()
                .filter { it !in primarySignifications }

            // Level 3: House I occupy
            val tertiarySignifications = listOf(occupiedHouse)
                .filter { it !in primarySignifications && it !in secondarySignifications }

            // Level 4: Houses I own
            val quaternarySignifications = ownedHouses
                .filter { 
                    it !in primarySignifications && 
                    it !in secondarySignifications && 
                    it !in tertiarySignifications 
                }

            val allSignifications = (primarySignifications + secondarySignifications +
                    tertiarySignifications + quaternarySignifications).toSet()

            significatorMap[planet] = KPSignificators(
                planet = planet,
                primarySignifications = primarySignifications,
                secondarySignifications = secondarySignifications,
                tertiarySignifications = tertiarySignifications,
                quaternarySignifications = quaternarySignifications,
                allSignifications = allSignifications
            )
        }

        return significatorMap
    }

    /**
     * Build map of houses owned by each planet
     */
    private fun buildOwnedHousesMap(chart: VedicChart): Map<Planet, List<Int>> {
        val ownedHouses = mutableMapOf<Planet, MutableList<Int>>()

        for (planet in Planet.MAIN_PLANETS) {
            ownedHouses[planet] = mutableListOf()
        }

        // Get house cusps and find lord of each
        for (houseNum in 1..12) {
            val cuspLongitude = chart.getCuspLongitude(houseNum)
            val sign = ZodiacSign.fromLongitude(cuspLongitude)
            val lord = sign.ruler
            ownedHouses[lord]?.add(houseNum)
        }

        return ownedHouses
    }

    /**
     * Build the complete significator table (House -> List of Significator Planets)
     */
    fun buildSignificatorTable(
        significators: Map<Planet, KPSignificators>
    ): Map<Int, List<Planet>> {
        val table = mutableMapOf<Int, MutableList<Planet>>()

        for (houseNum in 1..12) {
            table[houseNum] = mutableListOf()
        }

        for ((planet, sigs) in significators) {
            for (house in sigs.primarySignifications) {
                table[house]?.add(planet)
            }
            for (house in sigs.secondarySignifications) {
                if (planet !in (table[house] ?: emptyList())) {
                    table[house]?.add(planet)
                }
            }
            for (house in sigs.tertiarySignifications) {
                if (planet !in (table[house] ?: emptyList())) {
                    table[house]?.add(planet)
                }
            }
            for (house in sigs.quaternarySignifications) {
                if (planet !in (table[house] ?: emptyList())) {
                    table[house]?.add(planet)
                }
            }
        }

        // Sort by significance strength
        return table.mapValues { (house, planets) ->
            planets.sortedByDescending { planet ->
                val sigs = significators[planet]
                when {
                    sigs?.primarySignifications?.contains(house) == true -> 4
                    sigs?.secondarySignifications?.contains(house) == true -> 3
                    sigs?.tertiarySignifications?.contains(house) == true -> 2
                    else -> 1
                }
            }
        }
    }

    /**
     * Calculate KP cusp analysis
     */
    fun calculateCuspAnalysis(
        chart: VedicChart,
        significators: Map<Planet, KPSignificators>
    ): List<KPCusp> {
        val cusps = mutableListOf<KPCusp>()

        for (houseNum in 1..12) {
            val cuspLongitude = chart.getCuspLongitude(houseNum)
            val position = KPSubCalculator.getKPPosition(cuspLongitude)

            val subLordSignifications = significators[position.subLord]?.allSignifications ?: emptySet()

            val interpretation = generateCuspInterpretation(
                houseNum = houseNum,
                position = position,
                subLordSignifications = subLordSignifications.toList()
            )

            cusps.add(KPCusp(
                houseNumber = houseNum,
                position = position,
                significations = subLordSignifications.toList(),
                interpretation = interpretation
            ))
        }

        return cusps
    }

    /**
     * Generate interpretation for a cusp
     */
    private fun generateCuspInterpretation(
        houseNum: Int,
        position: KPPosition,
        subLordSignifications: List<Int>
    ): String {
        val houseName = getHouseName(houseNum)
        val favorableHouses = getFavorableHousesForCusp(houseNum)
        val unfavorableHouses = getUnfavorableHousesForCusp(houseNum)

        val favorable = subLordSignifications.filter { it in favorableHouses }
        val unfavorable = subLordSignifications.filter { it in unfavorableHouses }

        return buildString {
            append("House $houseNum ($houseName) Sub-Lord: ${position.subLord.displayName}. ")
            append("Star-Lord: ${position.starLord.displayName}. ")

            if (favorable.isNotEmpty()) {
                append("Favorable connections to houses ${favorable.joinToString(", ")}. ")
            }
            if (unfavorable.isNotEmpty()) {
                append("Challenges from houses ${unfavorable.joinToString(", ")}. ")
            }

            when {
                favorable.size > unfavorable.size ->
                    append("Overall favorable for $houseName matters.")
                unfavorable.size > favorable.size ->
                    append("Challenges indicated for $houseName matters.")
                else ->
                    append("Mixed results for $houseName matters.")
            }
        }
    }

    /**
     * Get house name/signification
     */
    private fun getHouseName(houseNum: Int): String {
        return when (houseNum) {
            1 -> "Self/Body"
            2 -> "Wealth/Family"
            3 -> "Siblings/Courage"
            4 -> "Mother/Property"
            5 -> "Children/Intelligence"
            6 -> "Enemies/Disease"
            7 -> "Spouse/Partnership"
            8 -> "Longevity/Obstacles"
            9 -> "Fortune/Father"
            10 -> "Career/Status"
            11 -> "Gains/Income"
            12 -> "Losses/Moksha"
            else -> "House $houseNum"
        }
    }

    /**
     * Get favorable houses for cusp analysis
     */
    private fun getFavorableHousesForCusp(houseNum: Int): List<Int> {
        return when (houseNum) {
            1 -> listOf(1, 5, 9, 10, 11) // Self: Trines, 10th, 11th
            2 -> listOf(2, 6, 10, 11) // Wealth: 2, 6, 10, 11
            3 -> listOf(3, 6, 10, 11) // Siblings: 3, 6, 10, 11
            4 -> listOf(4, 9, 11, 12) // Property: 4, 9, 11, 12
            5 -> listOf(2, 5, 11) // Children: 2, 5, 11
            6 -> listOf(6, 10, 11) // Victory over enemies: 6, 10, 11
            7 -> listOf(2, 7, 11) // Marriage: 2, 7, 11
            8 -> listOf(3, 8, 12) // Longevity: 3, 8 (in moderation)
            9 -> listOf(5, 9, 10, 11) // Fortune: 5, 9, 10, 11
            10 -> listOf(2, 6, 10, 11) // Career: 2, 6, 10, 11
            11 -> listOf(2, 6, 10, 11) // Gains: 2, 6, 10, 11
            12 -> listOf(5, 9, 12) // Moksha: 5, 9, 12
            else -> listOf(1, 5, 9, 11)
        }
    }

    /**
     * Get unfavorable houses for cusp analysis
     */
    private fun getUnfavorableHousesForCusp(houseNum: Int): List<Int> {
        return when (houseNum) {
            1 -> listOf(6, 8, 12) // Health obstacles
            2 -> listOf(5, 8, 12) // Wealth loss
            3 -> listOf(8, 12) // Sibling problems
            4 -> listOf(3, 6, 8) // Property loss
            5 -> listOf(1, 4, 10) // Children denial
            6 -> listOf(1, 5, 11) // Enemy success
            7 -> listOf(1, 6, 10, 12) // Marriage denial/divorce
            8 -> listOf(1, 6) // Life threat
            9 -> listOf(6, 8, 12) // Fortune obstacles
            10 -> listOf(5, 8, 12) // Career obstacles
            11 -> listOf(5, 8, 12) // Gain obstacles
            12 -> listOf(2, 6, 10, 11) // Material attachment
            else -> listOf(6, 8, 12)
        }
    }

    /**
     * Calculate planet positions with KP details
     */
    fun calculatePlanetPositions(
        chart: VedicChart,
        significators: Map<Planet, KPSignificators>,
        ownedHousesMap: Map<Planet, List<Int>>
    ): List<KPPlanetPosition> {
        val positions = mutableListOf<KPPlanetPosition>()

        for (pos in chart.planetPositions) {
            if (pos.planet !in Planet.MAIN_PLANETS) continue

            val kpPosition = KPSubCalculator.getKPPosition(pos.longitude)

            // Find planets in this planet's stars
            val starLordOf = chart.planetPositions
                .filter { it.nakshatra.ruler == pos.planet }
                .map { it.planet }

            positions.add(KPPlanetPosition(
                planet = pos.planet,
                position = kpPosition,
                occupiedHouse = pos.house,
                ownedHouses = ownedHousesMap[pos.planet] ?: emptyList(),
                starLordOf = starLordOf,
                significators = significators[pos.planet] ?: KPSignificators(
                    planet = pos.planet,
                    primarySignifications = emptyList(),
                    secondarySignifications = emptyList(),
                    tertiarySignifications = listOf(pos.house),
                    quaternarySignifications = ownedHousesMap[pos.planet] ?: emptyList(),
                    allSignifications = setOf(pos.house) + (ownedHousesMap[pos.planet]?.toSet() ?: emptySet())
                )
            ))
        }

        return positions
    }

    /**
     * Get significators for a specific house group (for event prediction)
     */
    fun getSignificatorsForHouseGroup(
        houseGroup: HouseGroup,
        significators: Map<Planet, KPSignificators>
    ): SignificatorAnalysis {
        val favorableSignificators = mutableMapOf<Planet, Double>()
        val unfavorableSignificators = mutableMapOf<Planet, Double>()

        for ((planet, sigs) in significators) {
            var favorableScore = 0.0
            var unfavorableScore = 0.0

            for (house in houseGroup.favorableHouses) {
                when (sigs.getSignificationStrength(house)) {
                    SignificationStrength.STRONGEST -> favorableScore += 1.0
                    SignificationStrength.STRONG -> favorableScore += 0.75
                    SignificationStrength.MODERATE -> favorableScore += 0.5
                    SignificationStrength.WEAK -> favorableScore += 0.25
                    SignificationStrength.NONE -> {}
                }
            }

            for (house in houseGroup.unfavorableHouses) {
                when (sigs.getSignificationStrength(house)) {
                    SignificationStrength.STRONGEST -> unfavorableScore += 1.0
                    SignificationStrength.STRONG -> unfavorableScore += 0.75
                    SignificationStrength.MODERATE -> unfavorableScore += 0.5
                    SignificationStrength.WEAK -> unfavorableScore += 0.25
                    SignificationStrength.NONE -> {}
                }
            }

            if (favorableScore > 0) favorableSignificators[planet] = favorableScore
            if (unfavorableScore > 0) unfavorableSignificators[planet] = unfavorableScore
        }

        return SignificatorAnalysis(
            houseGroup = houseGroup,
            favorableSignificators = favorableSignificators,
            unfavorableSignificators = unfavorableSignificators,
            bestSignificators = favorableSignificators.keys.filter { 
                it !in unfavorableSignificators.keys 
            },
            worstSignificators = unfavorableSignificators.keys.filter { 
                it !in favorableSignificators.keys 
            }
        )
    }
}

/**
 * Analysis of significators for a house group
 */
data class SignificatorAnalysis(
    val houseGroup: HouseGroup,
    val favorableSignificators: Map<Planet, Double>,
    val unfavorableSignificators: Map<Planet, Double>,
    val bestSignificators: List<Planet>,
    val worstSignificators: List<Planet>
) {
    /**
     * Get net score for a planet (positive = favorable)
     */
    fun getNetScore(planet: Planet): Double {
        val favorable = favorableSignificators[planet] ?: 0.0
        val unfavorable = unfavorableSignificators[planet] ?: 0.0
        return favorable - unfavorable
    }

    /**
     * Get planets sorted by their favorability for this event
     */
    fun getPlanetsByFavorability(): List<Pair<Planet, Double>> {
        val allPlanets = (favorableSignificators.keys + unfavorableSignificators.keys).distinct()
        return allPlanets
            .map { it to getNetScore(it) }
            .sortedByDescending { it.second }
    }
}
