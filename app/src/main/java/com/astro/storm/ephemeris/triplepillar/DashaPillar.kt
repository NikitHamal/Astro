package com.astro.storm.ephemeris.triplepillar

import com.astro.storm.core.model.Planet
import com.astro.storm.core.model.PlanetPosition
import com.astro.storm.core.model.VedicChart
import com.astro.storm.core.model.ZodiacSign
import com.astro.storm.ephemeris.DashaCalculator
import com.astro.storm.ephemeris.DashaCalculator.Antardasha
import com.astro.storm.ephemeris.DashaCalculator.DashaLevel
import com.astro.storm.ephemeris.DashaCalculator.DashaTimeline
import com.astro.storm.ephemeris.DashaCalculator.Mahadasha
import com.astro.storm.ephemeris.DashaCalculator.Pratyantardasha
import com.astro.storm.ephemeris.VedicAstrologyUtils
import java.time.LocalDateTime

/**
 * Dasha Pillar - Analyzes Vimshottari Dasha periods for the Triple-Pillar Engine
 *
 * This pillar evaluates the strength and favorability of active Dasha periods
 * based on multiple factors:
 *
 * 1. **Natal Position Analysis**: Evaluates the Dasha lord's placement in the birth chart
 *    - House position (angular, trine, dusthana)
 *    - Sign dignity (exalted, own sign, debilitated)
 *    - Aspects received from benefics and malefics
 *
 * 2. **House Lordship**: Considers which houses the Dasha lord rules
 *    - Benefic lords (1, 5, 9, 4, 7, 10) bring positive results
 *    - Malefic lords (6, 8, 12) bring challenges
 *    - Context of the ascendant sign matters
 *
 * 3. **Yoga Karaka Status**: Checks if the planet is Yoga Karaka for the ascendant
 *    - Mars for Cancer/Leo ascendant
 *    - Venus for Capricorn/Aquarius
 *    - Saturn for Taurus/Libra
 *
 * 4. **Period Combinations**: Evaluates Mahadasha-Antardasha-Pratyantardasha combinations
 *    - Natural friendships between period lords
 *    - Temporal relationships based on house positions
 *
 * Based on Brihat Parasara Hora Shastra (BPHS) Chapter 46-65
 */
object DashaPillar {

    /**
     * Houses classified by their nature
     */
    private val KENDRA_HOUSES = setOf(1, 4, 7, 10)      // Angular houses
    private val TRIKONA_HOUSES = setOf(1, 5, 9)         // Trine houses
    private val DUSTHANA_HOUSES = setOf(6, 8, 12)       // Malefic houses
    private val UPACHAYA_HOUSES = setOf(3, 6, 10, 11)   // Growth houses
    private val MARAKA_HOUSES = setOf(2, 7)             // Death-inflicting houses

    /**
     * Natural benefic planets
     */
    private val NATURAL_BENEFICS = setOf(Planet.JUPITER, Planet.VENUS, Planet.MOON, Planet.MERCURY)

    /**
     * Natural malefic planets
     */
    private val NATURAL_MALEFICS = setOf(Planet.SUN, Planet.MARS, Planet.SATURN, Planet.RAHU, Planet.KETU)

    /**
     * Yoga Karaka planets for each ascendant
     * Yoga Karaka rules both a Kendra and a Trikona house
     */
    private val YOGA_KARAKAS: Map<ZodiacSign, Planet?> = mapOf(
        ZodiacSign.ARIES to Planet.SATURN,       // Rules 10th and 11th (not strictly YK, but functional benefic)
        ZodiacSign.TAURUS to Planet.SATURN,      // Rules 9th and 10th
        ZodiacSign.GEMINI to null,               // No single Yoga Karaka
        ZodiacSign.CANCER to Planet.MARS,        // Rules 5th and 10th
        ZodiacSign.LEO to Planet.MARS,           // Rules 4th and 9th
        ZodiacSign.VIRGO to null,                // No single Yoga Karaka
        ZodiacSign.LIBRA to Planet.SATURN,       // Rules 4th and 5th
        ZodiacSign.SCORPIO to Planet.MOON,       // Rules 9th (functional benefic for Scorpio)
        ZodiacSign.SAGITTARIUS to null,          // No single Yoga Karaka
        ZodiacSign.CAPRICORN to Planet.VENUS,    // Rules 5th and 10th
        ZodiacSign.AQUARIUS to Planet.VENUS,     // Rules 4th and 9th
        ZodiacSign.PISCES to null                // No single Yoga Karaka
    )

    /**
     * Evaluate the Dasha pillar score for a given date
     *
     * @param chart The natal chart
     * @param timeline The pre-calculated Dasha timeline
     * @param targetDate The date to evaluate
     * @param config Configuration options
     * @return DashaPillarResult containing score and detailed analysis
     */
    fun evaluateDashaPillar(
        chart: VedicChart,
        timeline: DashaTimeline,
        targetDate: LocalDateTime,
        config: TriplePillarConfig = TriplePillarConfig.DEFAULT
    ): DashaPillarResult {
        val dashaPeriodInfo = timeline.getDashaAtDate(targetDate)

        val mahadasha = dashaPeriodInfo.mahadasha
        val antardasha = dashaPeriodInfo.antardasha
        val pratyantardasha = dashaPeriodInfo.pratyantardasha

        if (mahadasha == null) {
            return DashaPillarResult(
                score = 50.0,
                activeLords = emptyList(),
                mahadashaAnalysis = null,
                antardashaAnalysis = null,
                pratyantardashaAnalysis = null,
                combinationAnalysis = null,
                summary = "No active Dasha period found for the given date."
            )
        }

        val ascendantSign = ZodiacSign.fromLongitude(chart.ascendant)

        // Analyze each Dasha level
        val mahadashaAnalysis = analyzeDashaLord(
            chart, mahadasha.planet, DashaLevel.MAHADASHA, ascendantSign
        )

        val antardashaAnalysis = antardasha?.let {
            analyzeDashaLord(chart, it.planet, DashaLevel.ANTARDASHA, ascendantSign)
        }

        val pratyantardashaAnalysis = if (config.includeSubDashas && pratyantardasha != null) {
            analyzeDashaLord(chart, pratyantardasha.planet, DashaLevel.PRATYANTARDASHA, ascendantSign)
        } else null

        // Analyze the combination of lords
        val combinationAnalysis = analyzeDashaCombination(
            mahadashaAnalysis,
            antardashaAnalysis,
            pratyantardashaAnalysis,
            chart
        )

        // Calculate composite score
        val mahadashaWeight = 0.50
        val antardashaWeight = 0.35
        val pratyantardashaWeight = 0.15

        var score = mahadashaAnalysis.score * mahadashaWeight

        if (antardashaAnalysis != null) {
            score += antardashaAnalysis.score * antardashaWeight
        } else {
            score += 50.0 * antardashaWeight  // Neutral if not available
        }

        if (pratyantardashaAnalysis != null) {
            score += pratyantardashaAnalysis.score * pratyantardashaWeight
        } else {
            score += 50.0 * pratyantardashaWeight
        }

        // Apply combination modifiers
        score *= combinationAnalysis.modifier
        score = score.coerceIn(0.0, 100.0)

        // Build active lords list
        val activeLords = buildActiveLordsList(
            mahadashaAnalysis, antardashaAnalysis, pratyantardashaAnalysis
        )

        val summary = buildDashaSummary(
            mahadashaAnalysis, antardashaAnalysis, pratyantardashaAnalysis, combinationAnalysis, score
        )

        return DashaPillarResult(
            score = score,
            activeLords = activeLords,
            mahadashaAnalysis = mahadashaAnalysis,
            antardashaAnalysis = antardashaAnalysis,
            pratyantardashaAnalysis = pratyantardashaAnalysis,
            combinationAnalysis = combinationAnalysis,
            summary = summary
        )
    }

    /**
     * Analyze a single Dasha lord's strength and favorability
     */
    private fun analyzeDashaLord(
        chart: VedicChart,
        planet: Planet,
        level: DashaLevel,
        ascendantSign: ZodiacSign
    ): DashaLordAnalysis {
        val planetPosition = chart.planetPositions.find { it.planet == planet }
            ?: return createNeutralAnalysis(planet, level)

        val housePosition = planetPosition.house
        val signPosition = planetPosition.sign

        // Calculate dignity
        val dignity = calculateDignity(planet, signPosition)

        // Calculate house-based score
        val houseScore = calculateHouseScore(housePosition)

        // Check if Yoga Karaka
        val isYogaKaraka = YOGA_KARAKAS[ascendantSign] == planet

        // Get ruled houses
        val ruledHouses = getRuledHouses(planet, ascendantSign)

        // Calculate lordship score
        val lordshipScore = calculateLordshipScore(ruledHouses)

        // Check for aspects
        val aspectScore = calculateAspectScore(planet, chart)

        // Check for special conditions
        val specialConditions = checkSpecialConditions(planet, planetPosition, chart)

        // Combine scores
        var totalScore = (
            dignity.score * 15 +           // Max 75
            houseScore +                    // Max 20
            lordshipScore +                 // Max 30
            aspectScore +                   // -20 to +20
            specialConditions.modifier
        ).coerceIn(0.0, 100.0)

        // Yoga Karaka bonus
        if (isYogaKaraka) {
            totalScore = (totalScore * 1.15).coerceAtMost(100.0)
        }

        val strength = determineStrength(totalScore, dignity, housePosition, specialConditions)

        return DashaLordAnalysis(
            planet = planet,
            level = level,
            score = totalScore,
            dignity = dignity,
            housePosition = housePosition,
            signPosition = signPosition,
            ruledHouses = ruledHouses,
            isYogaKaraka = isYogaKaraka,
            strength = strength,
            aspectingPlanets = getAspectingPlanets(planet, chart),
            specialConditions = specialConditions.conditions
        )
    }

    /**
     * Calculate planetary dignity based on sign placement
     */
    private fun calculateDignity(planet: Planet, sign: ZodiacSign): PlanetaryDignity {
        return when {
            VedicAstrologyUtils.isExalted(planet, sign) -> PlanetaryDignity.EXALTED
            VedicAstrologyUtils.isMoolatrikona(planet, sign) -> PlanetaryDignity.MOOLATRIKONA
            VedicAstrologyUtils.isOwnSign(planet, sign) -> PlanetaryDignity.OWN_SIGN
            VedicAstrologyUtils.isDebilitated(planet, sign) -> PlanetaryDignity.DEBILITATED
            else -> {
                val friendship = VedicAstrologyUtils.getPlanetaryFriendship(planet, sign.ruler)
                when (friendship) {
                    VedicAstrologyUtils.Friendship.GREAT_FRIEND,
                    VedicAstrologyUtils.Friendship.FRIEND -> PlanetaryDignity.FRIENDLY
                    VedicAstrologyUtils.Friendship.NEUTRAL -> PlanetaryDignity.NEUTRAL
                    else -> PlanetaryDignity.ENEMY
                }
            }
        }
    }

    /**
     * Calculate score based on house position
     */
    private fun calculateHouseScore(house: Int): Double {
        return when (house) {
            in KENDRA_HOUSES -> 18.0
            in TRIKONA_HOUSES -> 20.0
            11 -> 15.0  // House of gains
            3 -> 12.0   // Upachaya
            in DUSTHANA_HOUSES -> when (house) {
                6 -> 8.0   // Enemies, but also upachaya
                8 -> 4.0   // Transformation, hidden
                12 -> 5.0  // Losses, but also liberation
                else -> 5.0
            }
            2 -> 14.0  // Wealth
            else -> 10.0
        }
    }

    /**
     * Calculate score based on which houses the planet rules
     */
    private fun calculateLordshipScore(ruledHouses: List<Int>): Double {
        var score = 0.0
        for (house in ruledHouses) {
            score += when (house) {
                in TRIKONA_HOUSES -> 10.0
                in KENDRA_HOUSES -> 8.0
                11 -> 6.0
                2 -> 5.0
                3 -> 3.0
                6 -> -2.0
                8 -> -5.0
                12 -> -3.0
                else -> 0.0
            }
        }
        return score.coerceIn(-10.0, 30.0)
    }

    /**
     * Calculate score based on aspects received
     */
    private fun calculateAspectScore(planet: Planet, chart: VedicChart): Double {
        var score = 0.0
        val aspectingPlanets = getAspectingPlanets(planet, chart)

        for (aspectingPlanet in aspectingPlanets) {
            score += when (aspectingPlanet) {
                Planet.JUPITER -> 8.0   // Jupiter's aspect is highly beneficial
                Planet.VENUS -> 5.0
                Planet.MERCURY -> 3.0
                Planet.MOON -> 2.0
                Planet.SUN -> 0.0       // Neutral
                Planet.MARS -> -4.0
                Planet.SATURN -> -5.0
                Planet.RAHU -> -6.0
                Planet.KETU -> -4.0
                else -> 0.0
            }
        }

        return score.coerceIn(-20.0, 20.0)
    }

    /**
     * Get planets aspecting a given planet
     */
    private fun getAspectingPlanets(planet: Planet, chart: VedicChart): List<Planet> {
        val aspectingPlanets = mutableListOf<Planet>()
        val planetPosition = chart.planetPositions.find { it.planet == planet } ?: return emptyList()

        for (otherPosition in chart.planetPositions) {
            if (otherPosition.planet == planet) continue

            val aspects = VedicAstrologyUtils.getVedicAspects(otherPosition.planet)
            val houseDistance = VedicAstrologyUtils.getHouseFromSigns(planetPosition.sign, otherPosition.sign)

            if (houseDistance in aspects) {
                aspectingPlanets.add(otherPosition.planet)
            }
        }

        return aspectingPlanets
    }

    /**
     * Check for special conditions affecting the planet
     */
    private fun checkSpecialConditions(
        planet: Planet,
        position: PlanetPosition,
        chart: VedicChart
    ): SpecialConditionsResult {
        val conditions = mutableListOf<String>()
        var modifier = 0.0

        // Check combustion (within certain degrees of Sun)
        val sunPosition = chart.planetPositions.find { it.planet == Planet.SUN }
        if (sunPosition != null && planet != Planet.SUN) {
            val distance = VedicAstrologyUtils.angularDistance(position.longitude, sunPosition.longitude)
            val combustionOrb = getCombustionOrb(planet)
            if (distance < combustionOrb) {
                conditions.add("Combust (within ${distance.toInt()}° of Sun)")
                modifier -= 10.0
            }
        }

        // Check retrograde
        if (position.isRetrograde) {
            conditions.add("Retrograde")
            // Retrograde planets are considered strong in Vedic astrology
            modifier += 5.0
        }

        // Check planetary war (Graha Yuddha)
        val nearbyPlanets = chart.planetPositions.filter { other ->
            other.planet != planet &&
            other.planet != Planet.SUN &&
            other.planet != Planet.MOON &&
            planet != Planet.SUN &&
            planet != Planet.MOON &&
            other.planet != Planet.RAHU &&
            other.planet != Planet.KETU &&
            planet != Planet.RAHU &&
            planet != Planet.KETU &&
            VedicAstrologyUtils.angularDistance(position.longitude, other.longitude) < 1.0
        }

        if (nearbyPlanets.isNotEmpty()) {
            val warPlanet = nearbyPlanets.first()
            val isWinner = isWinnerInPlanetaryWar(planet, warPlanet.planet, position, warPlanet)
            if (isWinner) {
                conditions.add("Wins planetary war with ${warPlanet.planet.displayName}")
                modifier += 8.0
            } else {
                conditions.add("Loses planetary war to ${warPlanet.planet.displayName}")
                modifier -= 10.0
            }
        }

        // Check vargottama (same sign in D1 and D9)
        if (isVargottama(position)) {
            conditions.add("Vargottama")
            modifier += 12.0
        }

        return SpecialConditionsResult(conditions, modifier)
    }

    /**
     * Get combustion orb for a planet
     */
    private fun getCombustionOrb(planet: Planet): Double {
        return when (planet) {
            Planet.MOON -> 12.0
            Planet.MARS -> 17.0
            Planet.MERCURY -> 14.0  // 12° when retrograde
            Planet.JUPITER -> 11.0
            Planet.VENUS -> 10.0    // 8° when retrograde
            Planet.SATURN -> 15.0
            else -> 0.0
        }
    }

    /**
     * Determine winner in planetary war (planet with higher latitude wins)
     */
    private fun isWinnerInPlanetaryWar(
        planet1: Planet,
        planet2: Planet,
        pos1: PlanetPosition,
        pos2: PlanetPosition
    ): Boolean {
        // In planetary war, the planet with greater brightness (generally northern latitude) wins
        // Simplified: use longitudinal position as proxy
        return pos1.longitude > pos2.longitude
    }

    /**
     * Check if planet is Vargottama (same sign in Rashi and Navamsa)
     */
    private fun isVargottama(position: PlanetPosition): Boolean {
        val rashiSign = position.sign
        val navamsaSign = calculateNavamsaSign(position.longitude)
        return rashiSign == navamsaSign
    }

    /**
     * Calculate Navamsa sign for a given longitude
     */
    private fun calculateNavamsaSign(longitude: Double): ZodiacSign {
        val normalizedLong = VedicAstrologyUtils.normalizeLongitude(longitude)
        val signIndex = (normalizedLong / 30.0).toInt()
        val degreeInSign = normalizedLong % 30.0
        val navamsaPada = (degreeInSign / 3.333333).toInt()

        // Navamsa starts from different signs based on element of Rashi sign
        val rashiSign = ZodiacSign.fromLongitude(longitude)
        val startingNavamsaIndex = when (rashiSign.element) {
            "Fire" -> 0     // Aries
            "Earth" -> 9    // Capricorn
            "Air" -> 6      // Libra
            "Water" -> 3    // Cancer
            else -> 0
        }

        val navamsaIndex = (startingNavamsaIndex + navamsaPada) % 12
        return ZodiacSign.entries[navamsaIndex]
    }

    /**
     * Get houses ruled by a planet for a given ascendant
     */
    private fun getRuledHouses(planet: Planet, ascendant: ZodiacSign): List<Int> {
        val ruledHouses = mutableListOf<Int>()

        ZodiacSign.entries.forEachIndexed { index, sign ->
            if (sign.ruler == planet) {
                val houseNumber = (index - ascendant.ordinal + 12) % 12 + 1
                ruledHouses.add(houseNumber)
            }
        }

        return ruledHouses.sorted()
    }

    /**
     * Determine overall strength category
     */
    private fun determineStrength(
        score: Double,
        dignity: PlanetaryDignity,
        house: Int,
        specialConditions: SpecialConditionsResult
    ): DashaStrength {
        return when {
            score >= 80 && dignity in listOf(PlanetaryDignity.EXALTED, PlanetaryDignity.OWN_SIGN) &&
                house in (KENDRA_HOUSES + TRIKONA_HOUSES) -> DashaStrength.VERY_STRONG
            score >= 65 -> DashaStrength.STRONG
            score >= 45 -> DashaStrength.MODERATE
            score >= 30 -> DashaStrength.WEAK
            else -> DashaStrength.VERY_WEAK
        }
    }

    /**
     * Analyze the combination of Dasha lords
     */
    private fun analyzeDashaCombination(
        mahadashaAnalysis: DashaLordAnalysis,
        antardashaAnalysis: DashaLordAnalysis?,
        pratyantardashaAnalysis: DashaLordAnalysis?,
        chart: VedicChart
    ): DashaCombinationAnalysis {
        var modifier = 1.0
        val factors = mutableListOf<String>()

        if (antardashaAnalysis == null) {
            return DashaCombinationAnalysis(modifier, factors, "Single Dasha lord active")
        }

        // Check natural friendship between Mahadasha and Antardasha lords
        val mdAdFriendship = VedicAstrologyUtils.getPlanetaryFriendship(
            mahadashaAnalysis.planet, antardashaAnalysis.planet
        )

        when (mdAdFriendship) {
            VedicAstrologyUtils.Friendship.GREAT_FRIEND -> {
                modifier *= 1.15
                factors.add("${mahadashaAnalysis.planet.displayName}-${antardashaAnalysis.planet.displayName}: Great friends")
            }
            VedicAstrologyUtils.Friendship.FRIEND -> {
                modifier *= 1.08
                factors.add("${mahadashaAnalysis.planet.displayName}-${antardashaAnalysis.planet.displayName}: Friends")
            }
            VedicAstrologyUtils.Friendship.ENEMY, VedicAstrologyUtils.Friendship.GREAT_ENEMY -> {
                modifier *= 0.85
                factors.add("${mahadashaAnalysis.planet.displayName}-${antardashaAnalysis.planet.displayName}: Enemies")
            }
            else -> { /* Neutral - no modification */ }
        }

        // Check if lords are in mutual angles (Kendra relation)
        val houseDiff = kotlin.math.abs(mahadashaAnalysis.housePosition - antardashaAnalysis.housePosition)
        if (houseDiff in listOf(0, 3, 6, 9)) {
            modifier *= 1.05
            factors.add("Dasha lords in angular relationship")
        }

        // Check if both lords aspect each other
        val mdAspects = VedicAstrologyUtils.getVedicAspects(mahadashaAnalysis.planet)
        val adAspects = VedicAstrologyUtils.getVedicAspects(antardashaAnalysis.planet)
        val mdToAdHouse = (antardashaAnalysis.housePosition - mahadashaAnalysis.housePosition + 12) % 12 + 1
        val adToMdHouse = (mahadashaAnalysis.housePosition - antardashaAnalysis.housePosition + 12) % 12 + 1

        if (mdToAdHouse in mdAspects && adToMdHouse in adAspects) {
            modifier *= 1.10
            factors.add("Mutual aspect between Dasha lords")
        }

        // Check Pratyantardasha if available
        if (pratyantardashaAnalysis != null) {
            val adPdFriendship = VedicAstrologyUtils.getPlanetaryFriendship(
                antardashaAnalysis.planet, pratyantardashaAnalysis.planet
            )
            if (adPdFriendship in listOf(VedicAstrologyUtils.Friendship.GREAT_FRIEND, VedicAstrologyUtils.Friendship.FRIEND)) {
                modifier *= 1.05
                factors.add("Antardasha-Pratyantardasha lords are friendly")
            }
        }

        val summary = buildCombinationSummary(factors, modifier)

        return DashaCombinationAnalysis(modifier, factors, summary)
    }

    /**
     * Build summary for combination analysis
     */
    private fun buildCombinationSummary(factors: List<String>, modifier: Double): String {
        return when {
            modifier >= 1.20 -> "Highly supportive Dasha combination"
            modifier >= 1.10 -> "Favorable Dasha combination"
            modifier >= 0.95 -> "Neutral Dasha combination"
            modifier >= 0.85 -> "Challenging Dasha combination"
            else -> "Difficult Dasha combination"
        }
    }

    /**
     * Build list of active Dasha lords with their info
     */
    private fun buildActiveLordsList(
        mahadashaAnalysis: DashaLordAnalysis,
        antardashaAnalysis: DashaLordAnalysis?,
        pratyantardashaAnalysis: DashaLordAnalysis?
    ): List<DashaLordInfo> {
        val lords = mutableListOf<DashaLordInfo>()

        lords.add(mahadashaAnalysis.toDashaLordInfo())

        antardashaAnalysis?.let { lords.add(it.toDashaLordInfo()) }
        pratyantardashaAnalysis?.let { lords.add(it.toDashaLordInfo()) }

        return lords
    }

    /**
     * Build overall Dasha summary
     */
    private fun buildDashaSummary(
        mahadashaAnalysis: DashaLordAnalysis,
        antardashaAnalysis: DashaLordAnalysis?,
        pratyantardashaAnalysis: DashaLordAnalysis?,
        combinationAnalysis: DashaCombinationAnalysis,
        score: Double
    ): String {
        return buildString {
            val quality = when {
                score >= 75 -> "Excellent"
                score >= 60 -> "Good"
                score >= 45 -> "Moderate"
                score >= 30 -> "Challenging"
                else -> "Difficult"
            }

            append("$quality Dasha period. ")
            append("${mahadashaAnalysis.planet.displayName} Mahadasha (${mahadashaAnalysis.strength.description})")

            antardashaAnalysis?.let {
                append(" with ${it.planet.displayName} Antardasha (${it.strength.description})")
            }

            if (mahadashaAnalysis.isYogaKaraka) {
                append(". ${mahadashaAnalysis.planet.displayName} is Yoga Karaka")
            }

            append(". ${combinationAnalysis.summary}.")
        }
    }

    /**
     * Create neutral analysis when planet position is not found
     */
    private fun createNeutralAnalysis(planet: Planet, level: DashaLevel): DashaLordAnalysis {
        return DashaLordAnalysis(
            planet = planet,
            level = level,
            score = 50.0,
            dignity = PlanetaryDignity.NEUTRAL,
            housePosition = 0,
            signPosition = ZodiacSign.ARIES,
            ruledHouses = emptyList(),
            isYogaKaraka = false,
            strength = DashaStrength.MODERATE,
            aspectingPlanets = emptyList(),
            specialConditions = emptyList()
        )
    }
}

/**
 * Result of Dasha pillar analysis
 */
data class DashaPillarResult(
    val score: Double,                    // 0-100
    val activeLords: List<DashaLordInfo>,
    val mahadashaAnalysis: DashaLordAnalysis?,
    val antardashaAnalysis: DashaLordAnalysis?,
    val pratyantardashaAnalysis: DashaLordAnalysis?,
    val combinationAnalysis: DashaCombinationAnalysis?,
    val summary: String
)

/**
 * Detailed analysis of a single Dasha lord
 */
data class DashaLordAnalysis(
    val planet: Planet,
    val level: DashaLevel,
    val score: Double,
    val dignity: PlanetaryDignity,
    val housePosition: Int,
    val signPosition: ZodiacSign,
    val ruledHouses: List<Int>,
    val isYogaKaraka: Boolean,
    val strength: DashaStrength,
    val aspectingPlanets: List<Planet>,
    val specialConditions: List<String>
) {
    fun toDashaLordInfo(): DashaLordInfo {
        return DashaLordInfo(
            planet = planet,
            level = level,
            startDate = LocalDateTime.now(), // Placeholder - actual dates come from timeline
            endDate = LocalDateTime.now().plusYears(1),
            strength = strength,
            natalHouse = housePosition,
            ruledHouses = ruledHouses,
            dignity = dignity,
            isYogaKaraka = isYogaKaraka
        )
    }
}

/**
 * Analysis of Dasha combination (multiple lords)
 */
data class DashaCombinationAnalysis(
    val modifier: Double,                 // Multiplier for combined score
    val factors: List<String>,            // Contributing factors
    val summary: String
)

/**
 * Special conditions affecting a planet
 */
data class SpecialConditionsResult(
    val conditions: List<String>,
    val modifier: Double
)
