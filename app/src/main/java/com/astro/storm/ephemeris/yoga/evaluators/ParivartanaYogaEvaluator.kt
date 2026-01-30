package com.astro.storm.ephemeris.yoga.evaluators

import com.astro.storm.core.model.Planet
import com.astro.storm.core.model.PlanetPosition
import com.astro.storm.core.model.VedicChart
import com.astro.storm.core.model.ZodiacSign
import com.astro.storm.ephemeris.yoga.Yoga
import com.astro.storm.ephemeris.yoga.YogaCategory
import com.astro.storm.ephemeris.yoga.YogaEvaluator
import com.astro.storm.ephemeris.yoga.YogaHelpers
import com.astro.storm.ephemeris.yoga.YogaStrength

/**
 * Parivartana (Mutual Exchange) Yoga Evaluator
 *
 * Evaluates all types of Parivartana Yogas based on classical Vedic texts.
 * Parivartana occurs when two planets are placed in each other's signs.
 *
 * Types of Parivartana:
 * 1. Maha Parivartana Yoga - Exchange between lords of 1,2,4,5,7,9,10,11 houses
 * 2. Kahala Parivartana - Exchange involving 3rd house lord
 * 3. Dainya Parivartana - Exchange involving 6th, 8th, or 12th house lord
 * 4. Khala Parivartana - Exchange creating inauspicious results
 *
 * Based on:
 * - Brihat Parashara Hora Shastra (BPHS), Chapter 41
 * - Phaladeepika
 * - Uttara Kalamrita
 *
 * @author AstroStorm
 */
class ParivartanaYogaEvaluator : YogaEvaluator {

    override val category: YogaCategory = YogaCategory.RAJA_YOGA

    companion object {
        // Benefic houses for Maha Parivartana
        private val BENEFIC_HOUSES = setOf(1, 2, 4, 5, 7, 9, 10, 11)
        // Dusthana houses
        private val DUSTHANA_HOUSES = setOf(6, 8, 12)
        // Trikona houses
        private val TRIKONA_HOUSES = setOf(1, 5, 9)
        // Kendra houses
        private val KENDRA_HOUSES = setOf(1, 4, 7, 10)
    }

    override fun evaluate(chart: VedicChart): List<Yoga> {
        val yogas = mutableListOf<Yoga>()
        val ascendantSign = ZodiacSign.fromLongitude(chart.ascendant)
        val houseLords = YogaHelpers.getHouseLords(ascendantSign)

        // Get all exchange pairs
        val exchanges = findAllExchanges(chart, houseLords)

        exchanges.forEach { exchange ->
            val yoga = classifyAndCreateYoga(chart, exchange, houseLords)
            if (yoga != null) {
                yogas.add(yoga)
            }
        }

        return yogas
    }

    /**
     * Find all planetary exchanges (Parivartana) in the chart
     */
    private fun findAllExchanges(
        chart: VedicChart,
        houseLords: Map<Int, Planet>
    ): List<ExchangePair> {
        val exchanges = mutableListOf<ExchangePair>()
        val processedPairs = mutableSetOf<Pair<Int, Int>>()

        for (house1 in 1..12) {
            for (house2 in (house1 + 1)..12) {
                val lord1 = houseLords[house1] ?: continue
                val lord2 = houseLords[house2] ?: continue

                // Skip if same planet rules both houses
                if (lord1 == lord2) continue

                val pos1 = chart.planetPositions.find { it.planet == lord1 } ?: continue
                val pos2 = chart.planetPositions.find { it.planet == lord2 } ?: continue

                // Check if they are in each other's signs
                if (YogaHelpers.areInExchange(pos1, pos2)) {
                    val pairKey = if (house1 < house2) Pair(house1, house2) else Pair(house2, house1)
                    if (pairKey !in processedPairs) {
                        processedPairs.add(pairKey)
                        exchanges.add(ExchangePair(
                            house1 = house1,
                            house2 = house2,
                            planet1 = lord1,
                            planet2 = lord2,
                            pos1 = pos1,
                            pos2 = pos2
                        ))
                    }
                }
            }
        }

        return exchanges
    }

    /**
     * Classify the type of Parivartana and create appropriate Yoga
     */
    private fun classifyAndCreateYoga(
        chart: VedicChart,
        exchange: ExchangePair,
        houseLords: Map<Int, Planet>
    ): Yoga? {
        val house1InBenefic = exchange.house1 in BENEFIC_HOUSES
        val house2InBenefic = exchange.house2 in BENEFIC_HOUSES
        val house1InDusthana = exchange.house1 in DUSTHANA_HOUSES
        val house2InDusthana = exchange.house2 in DUSTHANA_HOUSES

        return when {
            // Both houses are benefic (Maha Parivartana)
            house1InBenefic && house2InBenefic -> {
                createMahaParivartanaYoga(chart, exchange)
            }
            // One house is 3rd (Kahala Parivartana)
            exchange.house1 == 3 || exchange.house2 == 3 -> {
                createKahalaParivartanaYoga(chart, exchange)
            }
            // Both houses are dusthana (can create Viparita effect)
            house1InDusthana && house2InDusthana -> {
                createDainyaParivartanaYoga(chart, exchange, isViparita = true)
            }
            // One house is dusthana (Dainya Parivartana)
            house1InDusthana || house2InDusthana -> {
                createDainyaParivartanaYoga(chart, exchange, isViparita = false)
            }
            else -> null
        }
    }

    /**
     * Create Maha Parivartana Yoga - Most auspicious exchange
     */
    private fun createMahaParivartanaYoga(
        chart: VedicChart,
        exchange: ExchangePair
    ): Yoga {
        val positions = listOf(exchange.pos1, exchange.pos2)
        val (strengthPct, cancellationReasons) = YogaHelpers.calculateYogaStrengthWithReasons(chart, positions)

        // Boost strength based on specific house combinations
        var adjustedStrength = strengthPct

        // Kendra-Trikona exchange is strongest
        val isKendraTrikona = (exchange.house1 in KENDRA_HOUSES && exchange.house2 in TRIKONA_HOUSES) ||
                (exchange.house2 in KENDRA_HOUSES && exchange.house1 in TRIKONA_HOUSES)
        if (isKendraTrikona) {
            adjustedStrength *= 1.3
        }

        // 1st and 9th exchange is very powerful
        if ((exchange.house1 == 1 && exchange.house2 == 9) || (exchange.house1 == 9 && exchange.house2 == 1)) {
            adjustedStrength *= 1.25
        }

        // 4th and 10th exchange brings property and career success
        if ((exchange.house1 == 4 && exchange.house2 == 10) || (exchange.house1 == 10 && exchange.house2 == 4)) {
            adjustedStrength *= 1.2
        }

        // 5th and 9th (Trikona-Trikona) is excellent
        if ((exchange.house1 == 5 && exchange.house2 == 9) || (exchange.house1 == 9 && exchange.house2 == 5)) {
            adjustedStrength *= 1.25
        }

        val description = buildMahaParivartanaDescription(exchange)
        val effects = buildMahaParivartanaEffects(exchange)

        return Yoga(
            name = getMahaParivartanaName(exchange),
            sanskritName = "महा परिवर्तन योग",
            category = YogaCategory.RAJA_YOGA,
            planets = listOf(exchange.planet1, exchange.planet2),
            houses = listOf(exchange.house1, exchange.house2),
            description = description,
            effects = effects,
            strength = YogaHelpers.strengthFromPercentage(adjustedStrength.coerceIn(10.0, 100.0)),
            strengthPercentage = adjustedStrength.coerceIn(10.0, 100.0),
            isAuspicious = true,
            activationPeriod = "${exchange.planet1.displayName}-${exchange.planet2.displayName} Dasha/Antardasha",
            cancellationFactors = cancellationReasons.ifEmpty { listOf("None - yoga is unafflicted") }
        )
    }

    /**
     * Get specific name for Maha Parivartana based on houses involved
     */
    private fun getMahaParivartanaName(exchange: ExchangePair): String {
        // Special named exchanges
        return when {
            (exchange.house1 == 1 && exchange.house2 == 9) || (exchange.house1 == 9 && exchange.house2 == 1) ->
                "Lakshmi-Narayana Parivartana Yoga"
            (exchange.house1 == 4 && exchange.house2 == 10) || (exchange.house1 == 10 && exchange.house2 == 4) ->
                "Sukha-Karma Parivartana Yoga"
            (exchange.house1 == 5 && exchange.house2 == 9) || (exchange.house1 == 9 && exchange.house2 == 5) ->
                "Purva-Punya Parivartana Yoga"
            (exchange.house1 == 1 && exchange.house2 == 5) || (exchange.house1 == 5 && exchange.house2 == 1) ->
                "Atma-Putra Parivartana Yoga"
            (exchange.house1 == 2 && exchange.house2 == 11) || (exchange.house1 == 11 && exchange.house2 == 2) ->
                "Dhana-Labha Parivartana Yoga"
            (exchange.house1 == 9 && exchange.house2 == 10) || (exchange.house1 == 10 && exchange.house2 == 9) ->
                "Dharma-Karma Parivartana Yoga"
            (exchange.house1 == 7 && exchange.house2 == 10) || (exchange.house1 == 10 && exchange.house2 == 7) ->
                "Kalatra-Karma Parivartana Yoga"
            else -> "Maha Parivartana Yoga (${exchange.house1}-${exchange.house2})"
        }
    }

    /**
     * Build description for Maha Parivartana
     */
    private fun buildMahaParivartanaDescription(exchange: ExchangePair): String {
        return "${exchange.planet1.displayName} (lord of ${ordinal(exchange.house1)} house) in " +
                "${ordinal(exchange.house2)} house exchanges with ${exchange.planet2.displayName} " +
                "(lord of ${ordinal(exchange.house2)} house) in ${ordinal(exchange.house1)} house"
    }

    /**
     * Build effects for Maha Parivartana based on specific houses
     */
    private fun buildMahaParivartanaEffects(exchange: ExchangePair): String {
        val effects = mutableListOf<String>()

        // Add general effect
        effects.add("Powerful mutual exchange creating strong Raja Yoga")

        // Add specific effects based on houses
        when {
            1 in listOf(exchange.house1, exchange.house2) -> effects.add("enhanced personality and self-expression")
            2 in listOf(exchange.house1, exchange.house2) -> effects.add("wealth accumulation and family prosperity")
            4 in listOf(exchange.house1, exchange.house2) -> effects.add("property, vehicles, and domestic happiness")
            5 in listOf(exchange.house1, exchange.house2) -> effects.add("intelligence, children, and creative success")
            7 in listOf(exchange.house1, exchange.house2) -> effects.add("partnership success and marital harmony")
            9 in listOf(exchange.house1, exchange.house2) -> effects.add("fortune, higher learning, and father's blessings")
            10 in listOf(exchange.house1, exchange.house2) -> effects.add("career advancement and professional recognition")
            11 in listOf(exchange.house1, exchange.house2) -> effects.add("gains, income, and fulfillment of desires")
        }

        return effects.joinToString(", ")
    }

    /**
     * Create Kahala Parivartana Yoga - Exchange involving 3rd house
     */
    private fun createKahalaParivartanaYoga(
        chart: VedicChart,
        exchange: ExchangePair
    ): Yoga {
        val positions = listOf(exchange.pos1, exchange.pos2)
        val strengthPct = YogaHelpers.calculateYogaStrength(chart, positions)

        val otherHouse = if (exchange.house1 == 3) exchange.house2 else exchange.house1

        val effects = when (otherHouse) {
            1 -> "Courageous personality, self-effort brings success through bold initiatives"
            2 -> "Wealth through courage and initiative, good communication skills bring financial gains"
            4 -> "Property through bold ventures, courage in domestic matters"
            5 -> "Creative courage, bold in speculation and investments"
            7 -> "Bold in partnerships, courage benefits relationships"
            9 -> "Fortune through courageous actions, bold spiritual pursuits"
            10 -> "Career through initiative, bold professional moves bring success"
            11 -> "Gains through courageous actions, bold ventures fulfill desires"
            else -> "Courage enhanced in multiple life areas"
        }

        return Yoga(
            name = "Kahala Parivartana Yoga",
            sanskritName = "कहल परिवर्तन योग",
            category = YogaCategory.RAJA_YOGA,
            planets = listOf(exchange.planet1, exchange.planet2),
            houses = listOf(exchange.house1, exchange.house2),
            description = "Exchange between 3rd house lord and ${ordinal(otherHouse)} house lord",
            effects = effects,
            strength = YogaHelpers.strengthFromPercentage(strengthPct * 0.9),
            strengthPercentage = (strengthPct * 0.9).coerceIn(10.0, 100.0),
            isAuspicious = true,
            activationPeriod = "${exchange.planet1.displayName}-${exchange.planet2.displayName} periods",
            cancellationFactors = emptyList()
        )
    }

    /**
     * Create Dainya Parivartana Yoga - Exchange involving dusthana
     */
    private fun createDainyaParivartanaYoga(
        chart: VedicChart,
        exchange: ExchangePair,
        isViparita: Boolean
    ): Yoga {
        val positions = listOf(exchange.pos1, exchange.pos2)
        val strengthPct = YogaHelpers.calculateYogaStrength(chart, positions)

        val dusthanaHouse = when {
            exchange.house1 in DUSTHANA_HOUSES -> exchange.house1
            else -> exchange.house2
        }

        val beneficHouse = when {
            exchange.house1 !in DUSTHANA_HOUSES -> exchange.house1
            exchange.house2 !in DUSTHANA_HOUSES -> exchange.house2
            else -> null
        }

        // If both dusthana, it's a Viparita-like yoga
        if (isViparita) {
            return Yoga(
                name = "Viparita Parivartana Yoga",
                sanskritName = "विपरीत परिवर्तन योग",
                category = YogaCategory.RAJA_YOGA,
                planets = listOf(exchange.planet1, exchange.planet2),
                houses = listOf(exchange.house1, exchange.house2),
                description = "Exchange between dusthana lords (${ordinal(exchange.house1)} and ${ordinal(exchange.house2)})",
                effects = "Rise through adversity, gains from unexpected sources, transformation of obstacles into opportunities",
                strength = YogaHelpers.strengthFromPercentage(strengthPct * 0.75),
                strengthPercentage = (strengthPct * 0.75).coerceIn(10.0, 100.0),
                isAuspicious = true,
                activationPeriod = "${exchange.planet1.displayName}-${exchange.planet2.displayName} periods",
                cancellationFactors = listOf("Results may come through difficult circumstances")
            )
        }

        // One dusthana, one benefic house
        val effects = buildDainyaEffects(dusthanaHouse, beneficHouse)

        return Yoga(
            name = "Dainya Parivartana Yoga",
            sanskritName = "दैन्य परिवर्तन योग",
            category = YogaCategory.NEGATIVE_YOGA,
            planets = listOf(exchange.planet1, exchange.planet2),
            houses = listOf(exchange.house1, exchange.house2),
            description = "Exchange between ${ordinal(dusthanaHouse)} house lord and ${ordinal(beneficHouse ?: 0)} house lord",
            effects = effects,
            strength = YogaHelpers.strengthFromPercentage(strengthPct * 0.6),
            strengthPercentage = (strengthPct * 0.6).coerceIn(10.0, 100.0),
            isAuspicious = false,
            activationPeriod = "${exchange.planet1.displayName}-${exchange.planet2.displayName} periods",
            cancellationFactors = listOf(
                "Strong benefic aspects can mitigate",
                "Jupiter's association provides protection"
            )
        )
    }

    /**
     * Build effects for Dainya Parivartana
     */
    private fun buildDainyaEffects(dusthanaHouse: Int, beneficHouse: Int?): String {
        val dusthanaEffect = when (dusthanaHouse) {
            6 -> "health challenges or enemies"
            8 -> "sudden changes or obstacles"
            12 -> "losses or foreign connections"
            else -> "difficulties"
        }

        val beneficEffect = when (beneficHouse) {
            1 -> "personality and self-expression"
            2 -> "wealth and family"
            4 -> "property and happiness"
            5 -> "children and creativity"
            7 -> "partnerships"
            9 -> "fortune and dharma"
            10 -> "career"
            11 -> "gains"
            else -> "life areas"
        }

        return "Challenges from $dusthanaEffect affect $beneficEffect, requires effort to overcome obstacles"
    }

    /**
     * Convert number to ordinal string
     */
    private fun ordinal(n: Int): String = when (n) {
        1 -> "1st"
        2 -> "2nd"
        3 -> "3rd"
        else -> "${n}th"
    }

    /**
     * Data class for exchange pair
     */
    private data class ExchangePair(
        val house1: Int,
        val house2: Int,
        val planet1: Planet,
        val planet2: Planet,
        val pos1: PlanetPosition,
        val pos2: PlanetPosition
    )
}
