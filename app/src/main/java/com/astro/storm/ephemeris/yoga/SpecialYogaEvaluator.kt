package com.astro.storm.ephemeris.yoga

import com.astro.storm.core.model.Planet
import com.astro.storm.core.model.PlanetPosition
import com.astro.storm.core.model.VedicChart
import com.astro.storm.core.model.ZodiacSign

/**
 * Special Yoga Evaluator - Miscellaneous Significant Combinations
 *
 * Special Yogas are unique combinations that don't fit neatly into other categories
 * but have significant effects on the native's life.
 *
 * Types evaluated:
 * 1. Saraswati Yoga - Jupiter, Venus, Mercury in Kendra/Trikona/2nd
 * 2. Amala Yoga - Benefic in 10th from Lagna/Moon
 * 3. Parvata Yoga - Benefics in Kendras, no malefics in Kendras
 * 4. Kahala Yoga - 4th and 9th lords in mutual Kendra
 * 5. Chamara Yoga - Lagna lord exalted with Jupiter aspect
 * 6. Sanyasa Yoga - 4+ planets in one house including Saturn
 * 7. Shubhakartari Yoga - Planet/house flanked by benefics
 * 8. Dharma-Karmadhipati Yoga - 9th and 10th lords connected
 *
 * Based on:
 * - Brihat Parasara Hora Shastra (BPHS)
 * - Phaladeepika
 * - Jataka Parijata
 *
 * @author AstroStorm
 */
class SpecialYogaEvaluator : YogaEvaluator {

    override val category: YogaCategory = YogaCategory.SPECIAL_YOGA

    override fun evaluate(chart: VedicChart): List<Yoga> {
        val yogas = mutableListOf<Yoga>()
        val ascendantSign = ZodiacSign.fromLongitude(chart.ascendant)
        val houseLords = YogaHelpers.getHouseLords(ascendantSign)

        // 1. Saraswati Yoga
        evaluateSaraswatiYoga(chart)?.let { yogas.add(it) }

        // 2. Amala Yoga
        yogas.addAll(evaluateAmalaYoga(chart))

        // 3. Parvata Yoga
        evaluateParvataYoga(chart)?.let { yogas.add(it) }

        // 4. Kahala Yoga
        evaluateKahalaYoga(chart, houseLords)?.let { yogas.add(it) }

        // 5. Chamara Yoga
        evaluateChamaraYoga(chart, houseLords)?.let { yogas.add(it) }

        // 6. Sanyasa Yoga
        evaluateSanyasaYoga(chart)?.let { yogas.add(it) }

        // 7. Shubhakartari Yoga
        yogas.addAll(evaluateShubhakartariYogas(chart))

        // 8. Dharma-Karmadhipati Yoga
        evaluateDharmaKarmadhipatiYoga(chart, houseLords)?.let { yogas.add(it) }

        return yogas
    }

    /**
     * Saraswati Yoga - Jupiter, Venus, Mercury in Kendra/Trikona/2nd
     */
    private fun evaluateSaraswatiYoga(chart: VedicChart): Yoga? {
        val jupiterPos = chart.planetPositions.find { it.planet == Planet.JUPITER } ?: return null
        val venusPos = chart.planetPositions.find { it.planet == Planet.VENUS } ?: return null
        val mercuryPos = chart.planetPositions.find { it.planet == Planet.MERCURY } ?: return null

        val goodHouses = listOf(1, 2, 4, 5, 7, 9, 10)

        val jupiterGood = jupiterPos.house in goodHouses
        val venusGood = venusPos.house in goodHouses
        val mercuryGood = mercuryPos.house in goodHouses

        if (jupiterGood && venusGood && mercuryGood) {
            val positions = listOf(jupiterPos, venusPos, mercuryPos)
            val (strength, cancellationReasons) = YogaHelpers.calculateYogaStrengthWithReasons(chart, positions)

            // Boost for Jupiter in own/exalted sign
            var adjustedStrength = strength
            if (YogaHelpers.isExalted(jupiterPos) || YogaHelpers.isInOwnSign(jupiterPos)) {
                adjustedStrength *= 1.2
            }

            return Yoga(
                name = "Saraswati Yoga",
                sanskritName = "Saraswati Yoga",
                category = YogaCategory.SPECIAL_YOGA,
                planets = listOf(Planet.JUPITER, Planet.VENUS, Planet.MERCURY),
                houses = listOf(jupiterPos.house, venusPos.house, mercuryPos.house),
                description = "Jupiter, Venus, Mercury in Kendra/Trikona/2nd houses",
                effects = "Exceptional learning, artistic talents, eloquence, expertise in multiple fields, fame through knowledge",
                strength = YogaHelpers.strengthFromPercentage(adjustedStrength.coerceIn(10.0, 100.0)),
                strengthPercentage = adjustedStrength.coerceIn(10.0, 100.0),
                isAuspicious = true,
                activationPeriod = "Jupiter, Venus, or Mercury periods",
                cancellationFactors = cancellationReasons.ifEmpty { listOf("None - yoga is unafflicted") }
            )
        }

        return null
    }

    /**
     * Amala Yoga - Benefic in 10th from Lagna or Moon
     */
    private fun evaluateAmalaYoga(chart: VedicChart): List<Yoga> {
        val yogas = mutableListOf<Yoga>()
        val benefics = listOf(Planet.JUPITER, Planet.VENUS, Planet.MERCURY, Planet.MOON)

        // From Lagna
        val planetsIn10th = chart.planetPositions.filter { it.house == 10 && it.planet in benefics }
        if (planetsIn10th.isNotEmpty()) {
            val strength = YogaHelpers.calculateYogaStrength(chart, planetsIn10th)
            yogas.add(Yoga(
                name = "Amala Yoga (from Lagna)",
                sanskritName = "Amala Yoga",
                category = YogaCategory.SPECIAL_YOGA,
                planets = planetsIn10th.map { it.planet },
                houses = listOf(10),
                description = "${planetsIn10th.joinToString { it.planet.displayName }} in 10th house",
                effects = "Pure reputation, success in career, respected profession, lasting fame",
                strength = YogaHelpers.strengthFromPercentage(strength),
                strengthPercentage = strength,
                isAuspicious = true,
                activationPeriod = "${planetsIn10th.first().planet.displayName} periods",
                cancellationFactors = emptyList()
            ))
        }

        // From Moon
        val moonPos = chart.planetPositions.find { it.planet == Planet.MOON }
        if (moonPos != null) {
            val planetsIn10thFromMoon = chart.planetPositions.filter {
                it.planet in benefics &&
                        it.planet != Planet.MOON &&
                        YogaHelpers.getHouseFrom(it.sign, moonPos.sign) == 10
            }
            if (planetsIn10thFromMoon.isNotEmpty()) {
                val strength = YogaHelpers.calculateYogaStrength(chart, planetsIn10thFromMoon)
                yogas.add(Yoga(
                    name = "Amala Yoga (from Moon)",
                    sanskritName = "Amala Yoga",
                    category = YogaCategory.SPECIAL_YOGA,
                    planets = planetsIn10thFromMoon.map { it.planet },
                    houses = planetsIn10thFromMoon.map { it.house },
                    description = "${planetsIn10thFromMoon.joinToString { it.planet.displayName }} in 10th from Moon",
                    effects = "Mental satisfaction through career, public recognition, respected position",
                    strength = YogaHelpers.strengthFromPercentage(strength),
                    strengthPercentage = strength,
                    isAuspicious = true,
                    activationPeriod = "${planetsIn10thFromMoon.first().planet.displayName} periods",
                    cancellationFactors = emptyList()
                ))
            }
        }

        return yogas
    }

    /**
     * Parvata Yoga - Benefics in Kendras, no malefics in Kendras
     */
    private fun evaluateParvataYoga(chart: VedicChart): Yoga? {
        val kendras = listOf(1, 4, 7, 10)
        val benefics = listOf(Planet.JUPITER, Planet.VENUS, Planet.MERCURY)
        val malefics = listOf(Planet.SATURN, Planet.MARS, Planet.RAHU, Planet.KETU)

        val beneficsInKendra = chart.planetPositions.filter {
            it.planet in benefics && it.house in kendras
        }
        val maleficsInKendra = chart.planetPositions.filter {
            it.planet in malefics && it.house in kendras
        }

        if (beneficsInKendra.size >= 2 && maleficsInKendra.isEmpty()) {
            val strength = YogaHelpers.calculateYogaStrength(chart, beneficsInKendra)
            return Yoga(
                name = "Parvata Yoga",
                sanskritName = "Parvata Yoga",
                category = YogaCategory.SPECIAL_YOGA,
                planets = beneficsInKendra.map { it.planet },
                houses = beneficsInKendra.map { it.house },
                description = "Benefics in Kendras without malefic interference",
                effects = "Mountain-like stability, wealth, fame, charitable nature, leadership",
                strength = YogaHelpers.strengthFromPercentage(strength),
                strengthPercentage = strength,
                isAuspicious = true,
                activationPeriod = "Throughout life, especially benefic periods",
                cancellationFactors = emptyList()
            )
        }

        return null
    }

    /**
     * Kahala Yoga - 4th and 9th lords in mutual Kendra
     */
    private fun evaluateKahalaYoga(chart: VedicChart, houseLords: Map<Int, Planet>): Yoga? {
        val lord4 = houseLords[4] ?: return null
        val lord9 = houseLords[9] ?: return null

        val lord4Pos = chart.planetPositions.find { it.planet == lord4 } ?: return null
        val lord9Pos = chart.planetPositions.find { it.planet == lord9 } ?: return null

        if (YogaHelpers.isInKendraFrom(lord4Pos, lord9Pos)) {
            val strength = YogaHelpers.calculateYogaStrength(chart, listOf(lord4Pos, lord9Pos))
            return Yoga(
                name = "Kahala Yoga",
                sanskritName = "Kahala Yoga",
                category = YogaCategory.SPECIAL_YOGA,
                planets = listOf(lord4, lord9),
                houses = listOf(lord4Pos.house, lord9Pos.house),
                description = "4th lord and 9th lord in mutual Kendra",
                effects = "Bold and courageous, leadership abilities, success in competition, respected authority",
                strength = YogaHelpers.strengthFromPercentage(strength),
                strengthPercentage = strength,
                isAuspicious = true,
                activationPeriod = "$lord4-$lord9 periods",
                cancellationFactors = emptyList()
            )
        }

        return null
    }

    /**
     * Chamara Yoga - Lagna lord exalted with Jupiter aspect
     */
    private fun evaluateChamaraYoga(chart: VedicChart, houseLords: Map<Int, Planet>): Yoga? {
        val lagnaLord = houseLords[1] ?: return null
        val lagnaLordPos = chart.planetPositions.find { it.planet == lagnaLord } ?: return null
        val jupiterPos = chart.planetPositions.find { it.planet == Planet.JUPITER } ?: return null

        if (YogaHelpers.isExalted(lagnaLordPos) && YogaHelpers.isAspecting(jupiterPos, lagnaLordPos)) {
            val strength = YogaHelpers.calculateYogaStrength(chart, listOf(lagnaLordPos, jupiterPos)) * 1.2
            return Yoga(
                name = "Chamara Yoga",
                sanskritName = "Chamara Yoga",
                category = YogaCategory.SPECIAL_YOGA,
                planets = listOf(lagnaLord, Planet.JUPITER),
                houses = listOf(lagnaLordPos.house),
                description = "Lagna lord ${lagnaLord.displayName} exalted with Jupiter aspect",
                effects = "Royal status, command over others, eloquent speaker, long-lasting fame, wisdom",
                strength = YogaHelpers.strengthFromPercentage(strength.coerceIn(10.0, 100.0)),
                strengthPercentage = strength.coerceIn(10.0, 100.0),
                isAuspicious = true,
                activationPeriod = "${lagnaLord.displayName} and Jupiter periods",
                cancellationFactors = emptyList()
            )
        }

        return null
    }

    /**
     * Sanyasa Yoga - 4+ planets in one house including Saturn
     */
    private fun evaluateSanyasaYoga(chart: VedicChart): Yoga? {
        val mainPlanets = chart.planetPositions.filter {
            it.planet in listOf(Planet.SUN, Planet.MOON, Planet.MARS, Planet.MERCURY,
                Planet.JUPITER, Planet.VENUS, Planet.SATURN)
        }

        // Group by house
        val houseGroups = mainPlanets.groupBy { it.house }

        houseGroups.forEach { (house, positions) ->
            if (positions.size >= 4) {
                val hasSaturn = positions.any { it.planet == Planet.SATURN }
                if (hasSaturn) {
                    val strength = 60.0
                    return Yoga(
                        name = "Sanyasa Yoga",
                        sanskritName = "Sanyasa Yoga",
                        category = YogaCategory.SPECIAL_YOGA,
                        planets = positions.map { it.planet },
                        houses = listOf(house),
                        description = "${positions.size} planets including Saturn in house $house",
                        effects = "Strong inclination towards renunciation, spiritual pursuits, detachment from worldly matters",
                        strength = YogaHelpers.strengthFromPercentage(strength),
                        strengthPercentage = strength,
                        isAuspicious = true,
                        activationPeriod = "Saturn periods especially",
                        cancellationFactors = listOf(
                            "May manifest as philosophical mindset rather than literal renunciation",
                            "Jupiter involvement adds spiritual wisdom"
                        )
                    )
                }
            }
        }

        return null
    }

    /**
     * Shubhakartari Yoga - Planet/house flanked by benefics
     */
    private fun evaluateShubhakartariYogas(chart: VedicChart): List<Yoga> {
        val yogas = mutableListOf<Yoga>()
        val benefics = listOf(Planet.JUPITER, Planet.VENUS, Planet.MERCURY, Planet.MOON)

        // Check Shubhakartari on Ascendant (houses 2 and 12)
        val house2Benefics = chart.planetPositions.filter { it.house == 2 && it.planet in benefics }
        val house12Benefics = chart.planetPositions.filter { it.house == 12 && it.planet in benefics }

        if (house2Benefics.isNotEmpty() && house12Benefics.isNotEmpty()) {
            val allBenefics = house2Benefics + house12Benefics
            val strength = YogaHelpers.calculateYogaStrength(chart, allBenefics)
            yogas.add(Yoga(
                name = "Shubhakartari Yoga (Lagna)",
                sanskritName = "Shubhakartari Yoga",
                category = YogaCategory.SPECIAL_YOGA,
                planets = allBenefics.map { it.planet },
                houses = listOf(1, 2, 12),
                description = "Ascendant flanked by benefics in 2nd and 12th houses",
                effects = "Protected personality, good health, fortunate life, surrounded by helpful people",
                strength = YogaHelpers.strengthFromPercentage(strength),
                strengthPercentage = strength,
                isAuspicious = true,
                activationPeriod = "Throughout life",
                cancellationFactors = emptyList()
            ))
        }

        // Check on Moon
        val moonPos = chart.planetPositions.find { it.planet == Planet.MOON }
        if (moonPos != null) {
            val moonHouse = moonPos.house
            val prevHouse = if (moonHouse == 1) 12 else moonHouse - 1
            val nextHouse = if (moonHouse == 12) 1 else moonHouse + 1

            val prevBenefics = chart.planetPositions.filter {
                it.house == prevHouse && it.planet in benefics && it.planet != Planet.MOON
            }
            val nextBenefics = chart.planetPositions.filter {
                it.house == nextHouse && it.planet in benefics && it.planet != Planet.MOON
            }

            if (prevBenefics.isNotEmpty() && nextBenefics.isNotEmpty()) {
                val allBenefics = prevBenefics + nextBenefics + moonPos
                val strength = YogaHelpers.calculateYogaStrength(chart, allBenefics)
                yogas.add(Yoga(
                    name = "Shubhakartari Yoga (Chandra)",
                    sanskritName = "Shubhakartari Yoga",
                    category = YogaCategory.SPECIAL_YOGA,
                    planets = (prevBenefics + nextBenefics).map { it.planet },
                    houses = listOf(prevHouse, moonHouse, nextHouse),
                    description = "Moon flanked by benefics",
                    effects = "Mental peace, emotional stability, protected mind, supportive environment",
                    strength = YogaHelpers.strengthFromPercentage(strength),
                    strengthPercentage = strength,
                    isAuspicious = true,
                    activationPeriod = "Moon periods especially",
                    cancellationFactors = emptyList()
                ))
            }
        }

        return yogas
    }

    /**
     * Dharma-Karmadhipati Yoga - 9th and 10th lords connected
     */
    private fun evaluateDharmaKarmadhipatiYoga(chart: VedicChart, houseLords: Map<Int, Planet>): Yoga? {
        val lord9 = houseLords[9] ?: return null
        val lord10 = houseLords[10] ?: return null

        if (lord9 == lord10) return null // Same planet can't form this yoga

        val lord9Pos = chart.planetPositions.find { it.planet == lord9 } ?: return null
        val lord10Pos = chart.planetPositions.find { it.planet == lord10 } ?: return null

        val areConnected = YogaHelpers.areConjunct(lord9Pos, lord10Pos) ||
                YogaHelpers.areMutuallyAspecting(lord9Pos, lord10Pos) ||
                YogaHelpers.areInExchange(lord9Pos, lord10Pos)

        if (areConnected) {
            val positions = listOf(lord9Pos, lord10Pos)
            val (strength, cancellationReasons) = YogaHelpers.calculateYogaStrengthWithReasons(chart, positions)

            val connectionType = when {
                YogaHelpers.areInExchange(lord9Pos, lord10Pos) -> "mutual exchange"
                YogaHelpers.areConjunct(lord9Pos, lord10Pos) -> "conjunction"
                else -> "mutual aspect"
            }

            return Yoga(
                name = "Dharma-Karmadhipati Yoga",
                sanskritName = "Dharma-Karmadhipati Yoga",
                category = YogaCategory.SPECIAL_YOGA,
                planets = listOf(lord9, lord10),
                houses = listOf(lord9Pos.house, lord10Pos.house),
                description = "9th lord ${lord9.displayName} and 10th lord ${lord10.displayName} in $connectionType",
                effects = "Success through righteous actions, career aligned with dharma, rise to prominence through merit",
                strength = YogaHelpers.strengthFromPercentage(strength),
                strengthPercentage = strength,
                isAuspicious = true,
                activationPeriod = "${lord9.displayName}-${lord10.displayName} periods",
                cancellationFactors = cancellationReasons.ifEmpty { listOf("None - yoga is unafflicted") }
            )
        }

        return null
    }
}
