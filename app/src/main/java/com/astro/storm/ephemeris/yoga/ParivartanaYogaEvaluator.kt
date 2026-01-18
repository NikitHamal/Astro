package com.astro.storm.ephemeris.yoga

import com.astro.storm.core.model.Planet
import com.astro.storm.core.model.PlanetPosition
import com.astro.storm.core.model.VedicChart
import com.astro.storm.core.model.ZodiacSign

/**
 * Parivartana Yoga Evaluator - Mutual Exchange of Signs
 *
 * Parivartana Yoga occurs when two planets are placed in each other's signs,
 * creating a powerful connection between the houses they rule.
 *
 * Three Categories per Brihat Parasara Hora Shastra (BPHS):
 *
 * 1. **Maha Parivartana Yoga** (Great Exchange)
 *    - Exchange between lords of auspicious houses: 1, 2, 4, 5, 7, 9, 10, 11
 *    - Results: Wealth, power, success, good fortune
 *
 * 2. **Khala Parivartana Yoga** (Mischievous Exchange)
 *    - Exchange between an auspicious house lord and 3rd or 6th house lord
 *    - Results: Mixed - courage/enemies combined with other significations
 *
 * 3. **Dainya Parivartana Yoga** (Misery Exchange)
 *    - Exchange involving lords of dusthana houses: 6, 8, 12
 *    - Results: Viparita Raja Yoga potential if well-placed, otherwise challenges
 *
 * Special Exchanges:
 * - **Kendra-Trikona Exchange**: Creates Raja Yoga (handled by RajaYogaEvaluator)
 * - **Dusthana Exchange**: Can create Viparita Raja Yoga
 *
 * Classical References:
 * - Brihat Parasara Hora Shastra (BPHS), Chapter 32
 * - Phaladeepika, Chapter 6
 * - Uttara Kalamrita
 *
 * @author AstroStorm
 */
class ParivartanaYogaEvaluator : YogaEvaluator {

    override val category: YogaCategory = YogaCategory.SPECIAL_YOGA

    companion object {
        /** Auspicious houses (Kendra, Trikona, 2nd, 11th) */
        private val AUSPICIOUS_HOUSES = setOf(1, 2, 4, 5, 7, 9, 10, 11)

        /** Upachaya houses (3, 6, 10, 11) - houses of growth */
        private val UPACHAYA_HOUSES = setOf(3, 6, 10, 11)

        /** Dusthana houses (6, 8, 12) - challenging houses */
        private val DUSTHANA_HOUSES = setOf(6, 8, 12)

        /** Kendra houses (1, 4, 7, 10) - angular houses */
        private val KENDRA_HOUSES = setOf(1, 4, 7, 10)

        /** Trikona houses (1, 5, 9) - trinal houses */
        private val TRIKONA_HOUSES = setOf(1, 5, 9)

        /** Maraka houses (2, 7) - death-inflicting houses */
        private val MARAKA_HOUSES = setOf(2, 7)
    }

    override fun evaluate(chart: VedicChart): List<Yoga> {
        val yogas = mutableListOf<Yoga>()
        val ascendantSign = ZodiacSign.fromLongitude(chart.ascendant)
        val houseLords = YogaHelpers.getHouseLords(ascendantSign)

        if (houseLords.isEmpty()) return yogas

        // Find all exchanges in the chart
        val exchanges = findAllExchanges(chart, houseLords)

        // Classify and create yogas for each exchange
        exchanges.forEach { exchange ->
            val yoga = classifyAndCreateYoga(chart, exchange, houseLords, ascendantSign)
            if (yoga != null) {
                yogas.add(yoga)
            }
        }

        return yogas
    }

    /**
     * Data class to hold exchange information
     */
    private data class Exchange(
        val planet1: Planet,
        val planet2: Planet,
        val position1: PlanetPosition,
        val position2: PlanetPosition,
        val house1: Int,
        val house2: Int
    )

    /**
     * Find all planetary exchanges in the chart
     */
    private fun findAllExchanges(
        chart: VedicChart,
        houseLords: Map<Int, Planet>
    ): List<Exchange> {
        val exchanges = mutableListOf<Exchange>()
        val positions = chart.planetPositions
        val processedPairs = mutableSetOf<Pair<Planet, Planet>>()

        // Iterate through all planet pairs
        positions.forEach { pos1 ->
            positions.forEach { pos2 ->
                if (pos1.planet != pos2.planet) {
                    // Create normalized pair to avoid duplicates
                    val pair = if (pos1.planet.ordinal < pos2.planet.ordinal) {
                        pos1.planet to pos2.planet
                    } else {
                        pos2.planet to pos1.planet
                    }

                    if (pair !in processedPairs) {
                        // Check if planets are in exchange
                        if (YogaHelpers.areInExchange(pos1, pos2)) {
                            // Find which houses these planets rule
                            val house1 = houseLords.entries.find { it.value == pos1.planet }?.key
                            val house2 = houseLords.entries.find { it.value == pos2.planet }?.key

                            if (house1 != null && house2 != null) {
                                exchanges.add(Exchange(
                                    planet1 = pos1.planet,
                                    planet2 = pos2.planet,
                                    position1 = pos1,
                                    position2 = pos2,
                                    house1 = house1,
                                    house2 = house2
                                ))
                            }
                            processedPairs.add(pair)
                        }
                    }
                }
            }
        }

        return exchanges
    }

    /**
     * Classify the exchange and create appropriate yoga
     */
    private fun classifyAndCreateYoga(
        chart: VedicChart,
        exchange: Exchange,
        houseLords: Map<Int, Planet>,
        ascendantSign: ZodiacSign
    ): Yoga? {
        val house1 = exchange.house1
        val house2 = exchange.house2
        val positions = listOf(exchange.position1, exchange.position2)

        // Classify the exchange type
        return when {
            // Maha Parivartana: Both houses are auspicious
            house1 in AUSPICIOUS_HOUSES && house2 in AUSPICIOUS_HOUSES -> {
                createMahaParivartanaYoga(chart, exchange, positions)
            }

            // Dainya Parivartana: At least one house is dusthana
            house1 in DUSTHANA_HOUSES || house2 in DUSTHANA_HOUSES -> {
                // Check if it's a dusthana-dusthana exchange (potential Viparita Raja)
                if (house1 in DUSTHANA_HOUSES && house2 in DUSTHANA_HOUSES) {
                    createDusthanaExchangeYoga(chart, exchange, positions)
                } else {
                    createDainyaParivartanaYoga(chart, exchange, positions)
                }
            }

            // Khala Parivartana: Exchange with 3rd house (not dusthana)
            house1 == 3 || house2 == 3 -> {
                createKhalaParivartanaYoga(chart, exchange, positions)
            }

            // Default to generic Parivartana
            else -> {
                createGenericParivartanaYoga(chart, exchange, positions)
            }
        }
    }

    /**
     * Create Maha Parivartana Yoga (auspicious exchange)
     */
    private fun createMahaParivartanaYoga(
        chart: VedicChart,
        exchange: Exchange,
        positions: List<PlanetPosition>
    ): Yoga {
        val (strengthValue, cancellationReasons) = YogaHelpers.calculateYogaStrengthWithReasons(chart, positions)

        // Determine specific subtype for more detailed effects
        val subtype = getMahaParivartanaSubtype(exchange.house1, exchange.house2)
        val effects = getMahaParivartanaEffects(exchange.house1, exchange.house2, subtype)

        return Yoga(
            name = "Maha Parivartana Yoga",
            sanskritName = "महा परिवर्तन योग",
            category = YogaCategory.SPECIAL_YOGA,
            planets = listOf(exchange.planet1, exchange.planet2),
            houses = listOf(exchange.house1, exchange.house2),
            description = buildString {
                append("Exchange between ${exchange.planet1.displayName} (${getOrdinal(exchange.house1)} lord) ")
                append("and ${exchange.planet2.displayName} (${getOrdinal(exchange.house2)} lord)")
                if (subtype.isNotEmpty()) {
                    append(" - $subtype")
                }
            },
            effects = effects,
            strength = YogaHelpers.strengthFromPercentage(strengthValue),
            strengthPercentage = strengthValue,
            isAuspicious = true,
            activationPeriod = "${exchange.planet1.displayName}/${exchange.planet2.displayName} Dasha or Antardasha periods",
            cancellationFactors = cancellationReasons.ifEmpty { listOf("None - yoga operates without obstruction") }
        )
    }

    /**
     * Create Khala Parivartana Yoga (mixed exchange with 3rd house)
     */
    private fun createKhalaParivartanaYoga(
        chart: VedicChart,
        exchange: Exchange,
        positions: List<PlanetPosition>
    ): Yoga {
        val (strengthValue, cancellationReasons) = YogaHelpers.calculateYogaStrengthWithReasons(chart, positions)
        val adjustedStrength = strengthValue * 0.85 // Slightly reduced for mixed nature

        val otherHouse = if (exchange.house1 == 3) exchange.house2 else exchange.house1
        val otherPlanet = if (exchange.house1 == 3) exchange.planet2 else exchange.planet1

        return Yoga(
            name = "Khala Parivartana Yoga",
            sanskritName = "खल परिवर्तन योग",
            category = YogaCategory.SPECIAL_YOGA,
            planets = listOf(exchange.planet1, exchange.planet2),
            houses = listOf(exchange.house1, exchange.house2),
            description = buildString {
                append("Exchange between 3rd lord and ${getOrdinal(otherHouse)} lord")
                append(" - Courage and initiative combined with ${YogaHelpers.getHouseSignifications(otherHouse)}")
            },
            effects = buildString {
                append("Courage and initiative are directed toward ${YogaHelpers.getHouseSignifications(otherHouse)}. ")
                append("Siblings may be involved in matters of the ${getOrdinal(otherHouse)} house. ")
                append("Success through personal effort and risk-taking. ")
                append("May require overcoming obstacles to achieve ${getOrdinal(otherHouse)} house goals.")
            },
            strength = YogaHelpers.strengthFromPercentage(adjustedStrength),
            strengthPercentage = adjustedStrength,
            isAuspicious = true, // Khala can be beneficial if well-aspected
            activationPeriod = "${exchange.planet1.displayName}/${exchange.planet2.displayName} periods",
            cancellationFactors = cancellationReasons.ifEmpty { listOf("Results depend on overall chart support") }
        )
    }

    /**
     * Create Dainya Parivartana Yoga (exchange involving dusthana)
     */
    private fun createDainyaParivartanaYoga(
        chart: VedicChart,
        exchange: Exchange,
        positions: List<PlanetPosition>
    ): Yoga {
        val (strengthValue, cancellationReasons) = YogaHelpers.calculateYogaStrengthWithReasons(chart, positions)

        val dusthanaHouse = if (exchange.house1 in DUSTHANA_HOUSES) exchange.house1 else exchange.house2
        val otherHouse = if (exchange.house1 in DUSTHANA_HOUSES) exchange.house2 else exchange.house1
        val dusthanaPlanet = if (exchange.house1 in DUSTHANA_HOUSES) exchange.planet1 else exchange.planet2
        val otherPlanet = if (exchange.house1 in DUSTHANA_HOUSES) exchange.planet2 else exchange.planet1

        val dusthanaSignification = when (dusthanaHouse) {
            6 -> "enemies, debts, and illness"
            8 -> "obstacles, transformations, and hidden matters"
            12 -> "losses, isolation, and foreign lands"
            else -> "challenges"
        }

        // Dainya can have silver lining if other factors are favorable
        val hasSilverLining = positions.any { YogaHelpers.isExalted(it) || YogaHelpers.isInOwnSign(it) }

        return Yoga(
            name = "Dainya Parivartana Yoga",
            sanskritName = "दैन्य परिवर्तन योग",
            category = YogaCategory.NEGATIVE_YOGA, // Generally challenging
            planets = listOf(exchange.planet1, exchange.planet2),
            houses = listOf(exchange.house1, exchange.house2),
            description = buildString {
                append("Exchange between ${getOrdinal(dusthanaHouse)} lord (${dusthanaPlanet.displayName}) ")
                append("and ${getOrdinal(otherHouse)} lord (${otherPlanet.displayName})")
            },
            effects = buildString {
                append("Matters of the ${getOrdinal(otherHouse)} house become entangled with $dusthanaSignification. ")
                append("${YogaHelpers.getHouseSignifications(otherHouse).replaceFirstChar { it.uppercase() }} may face obstacles. ")
                if (hasSilverLining) {
                    append("However, planetary strength suggests eventual positive transformation through facing challenges. ")
                    append("Growth through adversity is indicated.")
                } else {
                    append("Requires patience and remedial measures to navigate challenges.")
                }
            },
            strength = YogaHelpers.strengthFromPercentage(strengthValue * 0.7),
            strengthPercentage = strengthValue * 0.7,
            isAuspicious = hasSilverLining, // Can be auspicious if planets are strong
            activationPeriod = "${dusthanaPlanet.displayName} Dasha may bring challenges; remedies recommended",
            cancellationFactors = buildList {
                addAll(cancellationReasons)
                add("Benefic aspects on involved planets can mitigate negative effects")
                if (hasSilverLining) {
                    add("Planetary strength provides resilience and eventual positive outcomes")
                }
            }
        )
    }

    /**
     * Create Dusthana Exchange Yoga (potential Viparita Raja)
     */
    private fun createDusthanaExchangeYoga(
        chart: VedicChart,
        exchange: Exchange,
        positions: List<PlanetPosition>
    ): Yoga {
        val (strengthValue, cancellationReasons) = YogaHelpers.calculateYogaStrengthWithReasons(chart, positions)

        // Check if it qualifies as Viparita Raja Yoga
        val isViparitaRaja = positions.all { pos ->
            // Viparita Raja is stronger when dusthana lords are weak or afflicted
            YogaHelpers.isDebilitated(pos) ||
            YogaHelpers.isInEnemySign(pos) ||
            pos.house in DUSTHANA_HOUSES
        }

        val yogaName = if (isViparitaRaja) "Viparita Raja Yoga (via Exchange)" else "Dusthana Parivartana Yoga"
        val sanskritName = if (isViparitaRaja) "विपरीत राज योग" else "दुस्थान परिवर्तन योग"

        return Yoga(
            name = yogaName,
            sanskritName = sanskritName,
            category = if (isViparitaRaja) YogaCategory.RAJA_YOGA else YogaCategory.SPECIAL_YOGA,
            planets = listOf(exchange.planet1, exchange.planet2),
            houses = listOf(exchange.house1, exchange.house2),
            description = buildString {
                append("Exchange between ${getOrdinal(exchange.house1)} and ${getOrdinal(exchange.house2)} lords ")
                append("(both Dusthana houses)")
                if (isViparitaRaja) {
                    append(" - Forms Viparita Raja Yoga")
                }
            },
            effects = buildString {
                if (isViparitaRaja) {
                    append("Rise through unconventional means. Success through overcoming obstacles. ")
                    append("Gains from others' downfall or through inheritance/insurance. ")
                    append("Hidden strengths emerge during difficult periods. ")
                    append("May excel in crisis management, investigation, or healing professions.")
                } else {
                    append("Complex karma involving multiple challenges. ")
                    append("Health, debts, and losses may be interconnected. ")
                    append("Requires careful navigation of competing difficulties.")
                }
            },
            strength = YogaHelpers.strengthFromPercentage(strengthValue),
            strengthPercentage = strengthValue,
            isAuspicious = isViparitaRaja,
            activationPeriod = "${exchange.planet1.displayName}/${exchange.planet2.displayName} periods",
            cancellationFactors = cancellationReasons.ifEmpty {
                if (isViparitaRaja) {
                    listOf("Viparita Raja operates through adversity - challenges activate the yoga")
                } else {
                    listOf("Remedial measures for both dusthana lords recommended")
                }
            }
        )
    }

    /**
     * Create generic Parivartana Yoga for edge cases
     */
    private fun createGenericParivartanaYoga(
        chart: VedicChart,
        exchange: Exchange,
        positions: List<PlanetPosition>
    ): Yoga {
        val (strengthValue, cancellationReasons) = YogaHelpers.calculateYogaStrengthWithReasons(chart, positions)

        return Yoga(
            name = "Parivartana Yoga",
            sanskritName = "परिवर्तन योग",
            category = YogaCategory.SPECIAL_YOGA,
            planets = listOf(exchange.planet1, exchange.planet2),
            houses = listOf(exchange.house1, exchange.house2),
            description = buildString {
                append("Exchange between ${exchange.planet1.displayName} (${getOrdinal(exchange.house1)} lord) ")
                append("and ${exchange.planet2.displayName} (${getOrdinal(exchange.house2)} lord)")
            },
            effects = buildString {
                append("Strong connection between ${YogaHelpers.getHouseSignifications(exchange.house1)} ")
                append("and ${YogaHelpers.getHouseSignifications(exchange.house2)}. ")
                append("Matters of both houses support each other. ")
                append("Mutual benefit indicated during planetary periods.")
            },
            strength = YogaHelpers.strengthFromPercentage(strengthValue),
            strengthPercentage = strengthValue,
            isAuspicious = exchange.house1 !in DUSTHANA_HOUSES && exchange.house2 !in DUSTHANA_HOUSES,
            activationPeriod = "${exchange.planet1.displayName}/${exchange.planet2.displayName} Dasha or Antardasha",
            cancellationFactors = cancellationReasons.ifEmpty { listOf("None - yoga operates normally") }
        )
    }

    /**
     * Get subtype name for Maha Parivartana based on houses involved
     */
    private fun getMahaParivartanaSubtype(house1: Int, house2: Int): String {
        val houses = setOf(house1, house2)

        return when {
            // Kendra-Trikona exchange (Raja Yoga class)
            (house1 in KENDRA_HOUSES && house2 in TRIKONA_HOUSES) ||
            (house2 in KENDRA_HOUSES && house1 in TRIKONA_HOUSES) -> "Kendra-Trikona Exchange"

            // Dhana Yoga class (involving wealth houses)
            2 in houses && 11 in houses -> "Dhana Yoga Exchange"
            (2 in houses || 11 in houses) && houses.any { it in KENDRA_HOUSES } -> "Dhana Yoga Exchange"

            // Lakshmi Yoga class (9th with Kendra)
            9 in houses && houses.any { it in KENDRA_HOUSES } -> "Lakshmi Yoga Exchange"

            // Career-related
            10 in houses -> "Karma Yoga Exchange"

            // Spouse-related
            7 in houses -> "Kalatra Yoga Exchange"

            // Property-related
            4 in houses -> "Sukha Yoga Exchange"

            else -> ""
        }
    }

    /**
     * Get specific effects for Maha Parivartana based on houses
     */
    private fun getMahaParivartanaEffects(house1: Int, house2: Int, subtype: String): String {
        val baseEffect = when (subtype) {
            "Kendra-Trikona Exchange" -> {
                "Powerful Raja Yoga through exchange. Rise to position of authority and power. " +
                "Leadership abilities manifest naturally. Success in politics, administration, or corporate leadership."
            }
            "Dhana Yoga Exchange" -> {
                "Strong wealth yoga through exchange. Financial prosperity flows naturally. " +
                "Multiple income sources. Family wealth and network gains combine favorably."
            }
            "Lakshmi Yoga Exchange" -> {
                "Fortune and luck are strongly indicated. Blessings from elders and teachers. " +
                "Success in higher education, law, or spiritual pursuits. International connections benefit."
            }
            "Karma Yoga Exchange" -> {
                "Career advancement through ${YogaHelpers.getHouseSignifications(if (house1 == 10) house2 else house1)}. " +
                "Professional recognition. Public status enhanced."
            }
            "Kalatra Yoga Exchange" -> {
                "Partnership benefits. Spouse may be connected to ${YogaHelpers.getHouseSignifications(if (house1 == 7) house2 else house1)}. " +
                "Business partnerships prosper."
            }
            "Sukha Yoga Exchange" -> {
                "Domestic happiness combined with ${YogaHelpers.getHouseSignifications(if (house1 == 4) house2 else house1)}. " +
                "Property gains. Education and comfort enhanced."
            }
            else -> {
                "Strong mutual support between ${YogaHelpers.getHouseSignifications(house1)} " +
                "and ${YogaHelpers.getHouseSignifications(house2)}. Both areas of life flourish."
            }
        }

        return baseEffect + " Exchange creates lasting foundation for success in both domains."
    }

    /**
     * Helper to get ordinal string for house number
     */
    private fun getOrdinal(number: Int): String {
        return when (number) {
            1 -> "1st"
            2 -> "2nd"
            3 -> "3rd"
            else -> "${number}th"
        }
    }
}
