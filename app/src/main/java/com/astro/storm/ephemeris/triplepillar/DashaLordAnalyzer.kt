package com.astro.storm.ephemeris.triplepillar

import com.astro.storm.core.common.Language
import com.astro.storm.core.model.Planet
import com.astro.storm.core.model.VedicChart
import com.astro.storm.core.model.ZodiacSign
import com.astro.storm.ephemeris.DashaCalculator

/**
 * Dasha Lord Analyzer
 *
 * Analyzes the relationship between Dasha Lords (Mahadasha, Antardasha, Pratyantardasha)
 * to determine the quality and nature of combined planetary periods.
 *
 * Based on classical principles from:
 * - Brihat Parashara Hora Shastra (Chapter 46-49 on Dasha Interpretation)
 * - Phaladeepika (Dasha Phala sections)
 * - Uttara Kalamrita (Dasha-Bhukti effects)
 *
 * Key factors analyzed:
 * 1. Natural relationship between planets
 * 2. Temporary relationship based on chart positions
 * 3. Yoga-karaka status and functional beneficence
 * 4. Mutual aspects and conjunctions
 * 5. House lordship combinations
 *
 * @author AstroStorm - Ultra-Precision Vedic Astrology
 */
object DashaLordAnalyzer {

    /**
     * Natural planetary relationships (Naisargika Maitri)
     * Based on BPHS principles
     */
    private val NATURAL_FRIENDS = mapOf(
        Planet.SUN to setOf(Planet.MOON, Planet.MARS, Planet.JUPITER),
        Planet.MOON to setOf(Planet.SUN, Planet.MERCURY),
        Planet.MARS to setOf(Planet.SUN, Planet.MOON, Planet.JUPITER),
        Planet.MERCURY to setOf(Planet.SUN, Planet.VENUS),
        Planet.JUPITER to setOf(Planet.SUN, Planet.MOON, Planet.MARS),
        Planet.VENUS to setOf(Planet.MERCURY, Planet.SATURN),
        Planet.SATURN to setOf(Planet.MERCURY, Planet.VENUS),
        Planet.RAHU to setOf(Planet.MERCURY, Planet.VENUS, Planet.SATURN),
        Planet.KETU to setOf(Planet.MARS, Planet.VENUS, Planet.SATURN)
    )

    private val NATURAL_ENEMIES = mapOf(
        Planet.SUN to setOf(Planet.SATURN, Planet.VENUS),
        Planet.MOON to setOf<Planet>(),
        Planet.MARS to setOf(Planet.MERCURY),
        Planet.MERCURY to setOf(Planet.MOON),
        Planet.JUPITER to setOf(Planet.MERCURY, Planet.VENUS),
        Planet.VENUS to setOf(Planet.SUN, Planet.MOON),
        Planet.SATURN to setOf(Planet.SUN, Planet.MOON, Planet.MARS),
        Planet.RAHU to setOf(Planet.SUN, Planet.MOON, Planet.MARS),
        Planet.KETU to setOf(Planet.SUN, Planet.MOON)
    )

    /**
     * Own signs for each planet
     */
    private val OWN_SIGNS = mapOf(
        Planet.SUN to listOf(ZodiacSign.LEO),
        Planet.MOON to listOf(ZodiacSign.CANCER),
        Planet.MARS to listOf(ZodiacSign.ARIES, ZodiacSign.SCORPIO),
        Planet.MERCURY to listOf(ZodiacSign.GEMINI, ZodiacSign.VIRGO),
        Planet.JUPITER to listOf(ZodiacSign.SAGITTARIUS, ZodiacSign.PISCES),
        Planet.VENUS to listOf(ZodiacSign.TAURUS, ZodiacSign.LIBRA),
        Planet.SATURN to listOf(ZodiacSign.CAPRICORN, ZodiacSign.AQUARIUS)
    )

    /**
     * Analyze the combination of active Dasha Lords.
     *
     * @param dashaPeriodInfo Current dasha period information
     * @param chart The natal chart for house lordship analysis
     * @param language Language for interpretations
     * @return DashaLordCombination with full analysis
     */
    fun analyzeDashaLordCombination(
        dashaPeriodInfo: DashaCalculator.DashaPeriodInfo,
        chart: VedicChart,
        language: Language = Language.ENGLISH
    ): DashaLordCombination? {
        val mahadasha = dashaPeriodInfo.mahadasha?.planet ?: return null
        val antardasha = dashaPeriodInfo.antardasha?.planet ?: return null
        val pratyantardasha = dashaPeriodInfo.pratyantardasha?.planet

        val naturalRelationship = getNaturalRelationship(mahadasha, antardasha)
        val temporaryRelationship = getTemporaryRelationship(mahadasha, antardasha, chart)
        val compoundRelationship = getCompoundRelationship(naturalRelationship, temporaryRelationship)

        val combinationQuality = assessCombinationQuality(
            mahadasha, antardasha, pratyantardasha, chart, compoundRelationship
        )

        val interpretation = generateCombinationInterpretation(
            mahadasha, antardasha, pratyantardasha,
            compoundRelationship, combinationQuality, chart, language
        )

        return DashaLordCombination(
            mahadashaLord = mahadasha,
            antardashaLord = antardasha,
            pratyantardashaLord = pratyantardasha,
            relationship = compoundRelationship,
            combinationQuality = combinationQuality,
            interpretation = interpretation
        )
    }

    /**
     * Get natural (Naisargika) relationship between two planets.
     */
    fun getNaturalRelationship(planet1: Planet, planet2: Planet): PlanetaryRelationship {
        if (planet1 == planet2) return PlanetaryRelationship.BEST_FRIEND

        val isFriend1To2 = NATURAL_FRIENDS[planet1]?.contains(planet2) == true
        val isFriend2To1 = NATURAL_FRIENDS[planet2]?.contains(planet1) == true
        val isEnemy1To2 = NATURAL_ENEMIES[planet1]?.contains(planet2) == true
        val isEnemy2To1 = NATURAL_ENEMIES[planet2]?.contains(planet1) == true

        return when {
            isFriend1To2 && isFriend2To1 -> PlanetaryRelationship.BEST_FRIEND
            isFriend1To2 || isFriend2To1 -> PlanetaryRelationship.FRIEND
            isEnemy1To2 && isEnemy2To1 -> PlanetaryRelationship.BITTER_ENEMY
            isEnemy1To2 || isEnemy2To1 -> PlanetaryRelationship.ENEMY
            else -> PlanetaryRelationship.NEUTRAL
        }
    }

    /**
     * Get temporary (Tatkalika) relationship based on chart positions.
     *
     * Rule: Planets in houses 2, 3, 4, 10, 11, 12 from each other are temporary friends.
     * Planets in houses 1, 5, 6, 7, 8, 9 from each other are temporary enemies.
     */
    fun getTemporaryRelationship(planet1: Planet, planet2: Planet, chart: VedicChart): PlanetaryRelationship {
        if (planet1 == planet2) return PlanetaryRelationship.BEST_FRIEND

        val pos1 = chart.planetPositions.find { it.planet == planet1 }
        val pos2 = chart.planetPositions.find { it.planet == planet2 }

        if (pos1 == null || pos2 == null) return PlanetaryRelationship.NEUTRAL

        val house = getHouseBetween(pos1.sign, pos2.sign)

        val friendlyHouses = listOf(2, 3, 4, 10, 11, 12)
        return if (house in friendlyHouses) {
            PlanetaryRelationship.FRIEND
        } else {
            PlanetaryRelationship.ENEMY
        }
    }

    /**
     * Get compound (Panchadha Maitri) relationship by combining natural and temporary.
     *
     * Natural + Temporary = Compound
     * Friend + Friend = Best Friend
     * Friend + Enemy = Neutral
     * Enemy + Friend = Neutral
     * Enemy + Enemy = Bitter Enemy
     * Neutral + Friend = Friend
     * Neutral + Enemy = Enemy
     */
    fun getCompoundRelationship(
        naturalRelationship: PlanetaryRelationship,
        temporaryRelationship: PlanetaryRelationship
    ): PlanetaryRelationship {
        return when {
            naturalRelationship == PlanetaryRelationship.BEST_FRIEND ->
                if (temporaryRelationship.ordinal <= PlanetaryRelationship.FRIEND.ordinal)
                    PlanetaryRelationship.BEST_FRIEND
                else
                    PlanetaryRelationship.FRIEND

            naturalRelationship == PlanetaryRelationship.FRIEND -> when (temporaryRelationship) {
                PlanetaryRelationship.BEST_FRIEND, PlanetaryRelationship.FRIEND ->
                    PlanetaryRelationship.BEST_FRIEND
                PlanetaryRelationship.NEUTRAL ->
                    PlanetaryRelationship.FRIEND
                else ->
                    PlanetaryRelationship.NEUTRAL
            }

            naturalRelationship == PlanetaryRelationship.NEUTRAL -> when (temporaryRelationship) {
                PlanetaryRelationship.BEST_FRIEND, PlanetaryRelationship.FRIEND ->
                    PlanetaryRelationship.FRIEND
                PlanetaryRelationship.NEUTRAL ->
                    PlanetaryRelationship.NEUTRAL
                else ->
                    PlanetaryRelationship.ENEMY
            }

            naturalRelationship == PlanetaryRelationship.ENEMY -> when (temporaryRelationship) {
                PlanetaryRelationship.BEST_FRIEND, PlanetaryRelationship.FRIEND ->
                    PlanetaryRelationship.NEUTRAL
                else ->
                    PlanetaryRelationship.ENEMY
            }

            naturalRelationship == PlanetaryRelationship.BITTER_ENEMY ->
                if (temporaryRelationship.ordinal >= PlanetaryRelationship.ENEMY.ordinal)
                    PlanetaryRelationship.BITTER_ENEMY
                else
                    PlanetaryRelationship.ENEMY

            else -> PlanetaryRelationship.NEUTRAL
        }
    }

    /**
     * Assess the overall quality of the Dasha Lord combination.
     */
    private fun assessCombinationQuality(
        mahadasha: Planet,
        antardasha: Planet,
        pratyantardasha: Planet?,
        chart: VedicChart,
        relationship: PlanetaryRelationship
    ): CombinationQuality {
        var score = 0

        score += when (relationship) {
            PlanetaryRelationship.BEST_FRIEND -> 4
            PlanetaryRelationship.FRIEND -> 2
            PlanetaryRelationship.NEUTRAL -> 0
            PlanetaryRelationship.ENEMY -> -2
            PlanetaryRelationship.BITTER_ENEMY -> -4
        }

        val ascendant = ZodiacSign.fromLongitude(chart.ascendant)
        val mdYogaKaraka = isYogaKaraka(mahadasha, ascendant)
        val adYogaKaraka = isYogaKaraka(antardasha, ascendant)

        if (mdYogaKaraka) score += 2
        if (adYogaKaraka) score += 2

        val mdFunctionalBenefic = isFunctionalBeneficForAscendant(mahadasha, ascendant)
        val adFunctionalBenefic = isFunctionalBeneficForAscendant(antardasha, ascendant)

        if (mdFunctionalBenefic) score += 1
        if (adFunctionalBenefic) score += 1

        val mdPosition = chart.planetPositions.find { it.planet == mahadasha }
        val adPosition = chart.planetPositions.find { it.planet == antardasha }

        if (mdPosition != null && adPosition != null) {
            if (mdPosition.sign == adPosition.sign) {
                score += if (relationship.ordinal <= PlanetaryRelationship.FRIEND.ordinal) 2 else -1
            }

            if (areMutuallyAspecting(mahadasha, mdPosition.sign, antardasha, adPosition.sign)) {
                score += 1
            }
        }

        if (pratyantardasha != null) {
            val pdRelationshipWithMd = getNaturalRelationship(mahadasha, pratyantardasha)
            val pdRelationshipWithAd = getNaturalRelationship(antardasha, pratyantardasha)

            score += when {
                pdRelationshipWithMd.ordinal <= PlanetaryRelationship.FRIEND.ordinal &&
                pdRelationshipWithAd.ordinal <= PlanetaryRelationship.FRIEND.ordinal -> 2
                pdRelationshipWithMd.ordinal >= PlanetaryRelationship.ENEMY.ordinal &&
                pdRelationshipWithAd.ordinal >= PlanetaryRelationship.ENEMY.ordinal -> -2
                else -> 0
            }
        }

        return when {
            score >= 6 -> CombinationQuality.EXCELLENT
            score >= 3 -> CombinationQuality.GOOD
            score >= 0 -> CombinationQuality.AVERAGE
            score >= -3 -> CombinationQuality.CHALLENGING
            else -> CombinationQuality.DIFFICULT
        }
    }

    /**
     * Check if a planet is Yoga-Karaka for the given ascendant.
     * Yoga-Karaka is a planet that lords both a Kendra (1,4,7,10) and a Trikona (1,5,9).
     */
    private fun isYogaKaraka(planet: Planet, ascendant: ZodiacSign): Boolean {
        return when (ascendant) {
            ZodiacSign.ARIES -> planet == Planet.SATURN
            ZodiacSign.TAURUS -> planet == Planet.SATURN
            ZodiacSign.CANCER -> planet == Planet.MARS
            ZodiacSign.LEO -> planet == Planet.MARS
            ZodiacSign.LIBRA -> planet == Planet.SATURN
            ZodiacSign.SCORPIO -> planet in setOf(Planet.MOON, Planet.SUN)
            ZodiacSign.CAPRICORN -> planet == Planet.VENUS
            ZodiacSign.AQUARIUS -> planet == Planet.VENUS
            else -> false
        }
    }

    /**
     * Check if a planet is a functional benefic for the ascendant.
     */
    private fun isFunctionalBeneficForAscendant(planet: Planet, ascendant: ZodiacSign): Boolean {
        val beneficPlanets = when (ascendant) {
            ZodiacSign.ARIES -> setOf(Planet.SUN, Planet.JUPITER, Planet.MARS)
            ZodiacSign.TAURUS -> setOf(Planet.SATURN, Planet.MERCURY, Planet.VENUS)
            ZodiacSign.GEMINI -> setOf(Planet.VENUS, Planet.SATURN, Planet.MERCURY)
            ZodiacSign.CANCER -> setOf(Planet.MARS, Planet.JUPITER, Planet.MOON)
            ZodiacSign.LEO -> setOf(Planet.MARS, Planet.JUPITER, Planet.SUN)
            ZodiacSign.VIRGO -> setOf(Planet.VENUS, Planet.MERCURY)
            ZodiacSign.LIBRA -> setOf(Planet.SATURN, Planet.MERCURY, Planet.VENUS)
            ZodiacSign.SCORPIO -> setOf(Planet.JUPITER, Planet.MOON, Planet.SUN)
            ZodiacSign.SAGITTARIUS -> setOf(Planet.SUN, Planet.MARS, Planet.JUPITER)
            ZodiacSign.CAPRICORN -> setOf(Planet.VENUS, Planet.MERCURY, Planet.SATURN)
            ZodiacSign.AQUARIUS -> setOf(Planet.VENUS, Planet.SATURN)
            ZodiacSign.PISCES -> setOf(Planet.MOON, Planet.MARS, Planet.JUPITER)
        }
        return planet in beneficPlanets
    }

    /**
     * Check if two planets are mutually aspecting each other.
     */
    private fun areMutuallyAspecting(
        planet1: Planet,
        sign1: ZodiacSign,
        planet2: Planet,
        sign2: ZodiacSign
    ): Boolean {
        return hasAspect(planet1, sign1, sign2) && hasAspect(planet2, sign2, sign1)
    }

    /**
     * Check if a planet aspects a target sign.
     */
    private fun hasAspect(planet: Planet, fromSign: ZodiacSign, toSign: ZodiacSign): Boolean {
        val distance = ((toSign.ordinal - fromSign.ordinal + 12) % 12) + 1

        val aspectHouses = when (planet) {
            Planet.MARS -> listOf(4, 7, 8)
            Planet.JUPITER -> listOf(5, 7, 9)
            Planet.SATURN -> listOf(3, 7, 10)
            Planet.RAHU -> listOf(5, 7, 9)
            Planet.KETU -> listOf(5, 7, 9)
            else -> listOf(7)
        }

        return distance in aspectHouses
    }

    /**
     * Get house number between two signs.
     */
    private fun getHouseBetween(fromSign: ZodiacSign, toSign: ZodiacSign): Int {
        return ((toSign.ordinal - fromSign.ordinal + 12) % 12) + 1
    }

    /**
     * Generate interpretation for the Dasha Lord combination.
     */
    private fun generateCombinationInterpretation(
        mahadasha: Planet,
        antardasha: Planet,
        pratyantardasha: Planet?,
        relationship: PlanetaryRelationship,
        quality: CombinationQuality,
        chart: VedicChart,
        language: Language
    ): String {
        val mdName = mahadasha.getLocalizedName(language)
        val adName = antardasha.getLocalizedName(language)
        val pdName = pratyantardasha?.getLocalizedName(language)

        return buildString {
            append("$mdName Mahadasha - $adName Antardasha")
            if (pdName != null) append(" - $pdName Pratyantardasha")
            appendLine()
            appendLine()

            append("Relationship: ${relationship.displayName}")
            appendLine()

            when (quality) {
                CombinationQuality.EXCELLENT -> {
                    append("This is an excellent dasha combination. ")
                    append("Both lords are harmoniously disposed, indicating a period of ")
                    append("significant growth and favorable outcomes. ")
                    append("The significations of both planets will manifest positively.")
                }
                CombinationQuality.GOOD -> {
                    append("This is a good dasha combination. ")
                    append("The lords work well together, suggesting a period of ")
                    append("steady progress and general well-being. ")
                    append("Minor challenges may arise but are easily overcome.")
                }
                CombinationQuality.AVERAGE -> {
                    append("This is an average dasha combination. ")
                    append("The lords are neither strongly supportive nor opposing. ")
                    append("Results will depend largely on transit influences and ")
                    append("individual effort during this period.")
                }
                CombinationQuality.CHALLENGING -> {
                    append("This is a challenging dasha combination. ")
                    append("The lords have some tension between them, indicating ")
                    append("a period requiring extra effort and patience. ")
                    append("Focus on areas where both planets can cooperate.")
                }
                CombinationQuality.DIFFICULT -> {
                    append("This is a difficult dasha combination. ")
                    append("The lords are inimical, suggesting a period of ")
                    append("obstacles and conflicting energies. ")
                    append("Remedial measures and careful timing are recommended.")
                }
            }
            appendLine()
            appendLine()

            append("Key themes: ")
            append(getPlanetThemes(mahadasha))
            append(" combined with ")
            append(getPlanetThemes(antardasha))
            append(".")
        }
    }

    /**
     * Get key themes for a planet.
     */
    private fun getPlanetThemes(planet: Planet): String {
        return when (planet) {
            Planet.SUN -> "authority, father, career, health, government"
            Planet.MOON -> "mind, mother, emotions, public, comfort"
            Planet.MARS -> "energy, siblings, property, courage, competition"
            Planet.MERCURY -> "communication, intellect, business, education"
            Planet.JUPITER -> "wisdom, children, fortune, spirituality, expansion"
            Planet.VENUS -> "relationships, luxury, arts, vehicles, pleasures"
            Planet.SATURN -> "discipline, longevity, karma, delays, hard work"
            Planet.RAHU -> "foreign, unconventional, desires, technology, ambition"
            Planet.KETU -> "spirituality, detachment, past karma, occult, liberation"
            else -> "various life matters"
        }
    }

    /**
     * Get detailed house lordship analysis for Dasha Lords.
     */
    fun getHouseLordshipAnalysis(
        planet: Planet,
        chart: VedicChart,
        language: Language = Language.ENGLISH
    ): HouseLordshipAnalysis {
        val ascendant = ZodiacSign.fromLongitude(chart.ascendant)
        val ownSigns = OWN_SIGNS[planet] ?: emptyList()

        val ruledHouses = ownSigns.map { sign ->
            ((sign.ordinal - ascendant.ordinal + 12) % 12) + 1
        }

        val houseTypes = mutableListOf<HouseType>()
        ruledHouses.forEach { house ->
            houseTypes.add(
                when (house) {
                    1 -> HouseType.KENDRA_TRIKONA
                    4, 7, 10 -> HouseType.KENDRA
                    5, 9 -> HouseType.TRIKONA
                    2, 11 -> HouseType.WEALTH
                    3 -> HouseType.UPACHAYA
                    6, 8, 12 -> HouseType.DUSTHANA
                    else -> HouseType.NEUTRAL
                }
            )
        }

        val isYogaKaraka = isYogaKaraka(planet, ascendant)
        val isFunctionalBenefic = isFunctionalBeneficForAscendant(planet, ascendant)
        val isFunctionalMalefic = isFunctionalMaleficForAscendant(planet, ascendant)

        val interpretation = generateHouseLordshipInterpretation(
            planet, ruledHouses, houseTypes, isYogaKaraka,
            isFunctionalBenefic, isFunctionalMalefic, language
        )

        return HouseLordshipAnalysis(
            planet = planet,
            ruledHouses = ruledHouses,
            houseTypes = houseTypes,
            isYogaKaraka = isYogaKaraka,
            isFunctionalBenefic = isFunctionalBenefic,
            isFunctionalMalefic = isFunctionalMalefic,
            interpretation = interpretation
        )
    }

    /**
     * Check if a planet is a functional malefic for the ascendant.
     */
    private fun isFunctionalMaleficForAscendant(planet: Planet, ascendant: ZodiacSign): Boolean {
        val maleficPlanets = when (ascendant) {
            ZodiacSign.ARIES -> setOf(Planet.MERCURY, Planet.SATURN, Planet.VENUS)
            ZodiacSign.TAURUS -> setOf(Planet.JUPITER, Planet.MOON, Planet.MARS)
            ZodiacSign.GEMINI -> setOf(Planet.MARS, Planet.JUPITER)
            ZodiacSign.CANCER -> setOf(Planet.SATURN, Planet.VENUS, Planet.MERCURY)
            ZodiacSign.LEO -> setOf(Planet.SATURN, Planet.VENUS, Planet.MERCURY, Planet.MOON)
            ZodiacSign.VIRGO -> setOf(Planet.MARS, Planet.MOON, Planet.SUN)
            ZodiacSign.LIBRA -> setOf(Planet.JUPITER, Planet.SUN, Planet.MARS)
            ZodiacSign.SCORPIO -> setOf(Planet.MERCURY, Planet.VENUS)
            ZodiacSign.SAGITTARIUS -> setOf(Planet.VENUS, Planet.SATURN, Planet.MERCURY)
            ZodiacSign.CAPRICORN -> setOf(Planet.MOON, Planet.MARS, Planet.JUPITER)
            ZodiacSign.AQUARIUS -> setOf(Planet.MOON, Planet.MARS, Planet.JUPITER)
            ZodiacSign.PISCES -> setOf(Planet.SUN, Planet.VENUS, Planet.SATURN, Planet.MERCURY)
        }
        return planet in maleficPlanets
    }

    /**
     * Generate house lordship interpretation.
     */
    private fun generateHouseLordshipInterpretation(
        planet: Planet,
        ruledHouses: List<Int>,
        houseTypes: List<HouseType>,
        isYogaKaraka: Boolean,
        isFunctionalBenefic: Boolean,
        isFunctionalMalefic: Boolean,
        language: Language
    ): String {
        return buildString {
            append("${planet.getLocalizedName(language)} rules house(s) ${ruledHouses.joinToString(", ")}. ")

            if (isYogaKaraka) {
                append("This planet is a YOGA-KARAKA, holding exceptional power to bestow success. ")
            }

            if (isFunctionalBenefic && !isYogaKaraka) {
                append("Functionally benefic for this ascendant. ")
            }

            if (isFunctionalMalefic) {
                append("Functionally challenging for this ascendant. ")
            }

            val hasKendra = houseTypes.any { it == HouseType.KENDRA || it == HouseType.KENDRA_TRIKONA }
            val hasTrikona = houseTypes.any { it == HouseType.TRIKONA || it == HouseType.KENDRA_TRIKONA }
            val hasDusthana = houseTypes.any { it == HouseType.DUSTHANA }

            when {
                hasKendra && hasTrikona -> append("Lords both Kendra and Trikona - auspicious lord. ")
                hasKendra -> append("Kendra lord providing stability and foundation. ")
                hasTrikona -> append("Trikona lord bringing fortune and dharma. ")
                hasDusthana -> append("Dusthana lord - may indicate challenges in its dasha. ")
            }
        }
    }

    /**
     * House type classification
     */
    enum class HouseType(val displayName: String) {
        KENDRA("Kendra (Angular)"),
        TRIKONA("Trikona (Trinal)"),
        KENDRA_TRIKONA("Kendra & Trikona"),
        UPACHAYA("Upachaya (Growth)"),
        WEALTH("Wealth House"),
        DUSTHANA("Dusthana (Challenging)"),
        NEUTRAL("Neutral")
    }

    /**
     * House lordship analysis result
     */
    data class HouseLordshipAnalysis(
        val planet: Planet,
        val ruledHouses: List<Int>,
        val houseTypes: List<HouseType>,
        val isYogaKaraka: Boolean,
        val isFunctionalBenefic: Boolean,
        val isFunctionalMalefic: Boolean,
        val interpretation: String
    )
}
