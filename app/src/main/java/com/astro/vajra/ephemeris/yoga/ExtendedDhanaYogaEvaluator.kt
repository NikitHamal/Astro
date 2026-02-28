package com.astro.vajra.ephemeris.yoga

import com.astro.vajra.core.model.Planet
import com.astro.vajra.core.model.PlanetPosition
import com.astro.vajra.core.model.VedicChart
import com.astro.vajra.core.model.ZodiacSign

/**
 * Extended Dhana (Wealth) Yoga Evaluator - Comprehensive Wealth Combinations
 *
 * This evaluator handles the extended set of wealth-producing yogas beyond
 * the basic dhana combinations. These yogas indicate financial prosperity,
 * material abundance, and resource accumulation.
 *
 * Categories covered:
 * 1. Classical Dhana Yogas from BPHS
 * 2. Lakshmi Yogas - Goddess of wealth combinations
 * 3. Kubera Yogas - God of wealth combinations
 * 4. 2nd and 11th House Special Combinations
 * 5. Planet-Specific Wealth Yogas
 * 6. Hora Lagna Wealth Yogas
 * 7. Multiple Source Wealth Yogas
 * 8. Hidden Wealth/Inheritance Yogas
 * 9. Business/Commerce Yogas
 * 10. Property and Asset Yogas
 *
 * Based on classical texts:
 * - Brihat Parasara Hora Shastra (BPHS), Chapters 37-39
 * - Phaladeepika, Chapters 8-9
 * - Jataka Parijata, Chapter 9
 * - Saravali, Chapters 37-40
 * - Uttara Kalamrita, Section 4
 *
 * @author AstroVajra
 */
class ExtendedDhanaYogaEvaluator : YogaEvaluator {

    override val category: YogaCategory = YogaCategory.DHANA_YOGA

    override fun evaluate(chart: VedicChart): List<Yoga> {
        val yogas = mutableListOf<Yoga>()
        val ascendantSign = ZodiacSign.fromLongitude(chart.ascendant)
        val houseLords = YogaHelpers.getHouseLords(ascendantSign)

        // 1. Classical Dhana Yogas
        yogas.addAll(evaluateClassicalDhanaYogas(chart, houseLords))

        // 2. Lakshmi Yogas
        yogas.addAll(evaluateLakshmiYogas(chart, houseLords))

        // 3. Special 2nd House Yogas
        yogas.addAll(evaluate2ndHouseYogas(chart, houseLords))

        // 4. Special 11th House Yogas
        yogas.addAll(evaluate11thHouseYogas(chart, houseLords))

        // 5. Planet-Specific Wealth Yogas
        yogas.addAll(evaluatePlanetWealthYogas(chart, houseLords))

        // 6. Hidden Wealth and Inheritance Yogas
        yogas.addAll(evaluateHiddenWealthYogas(chart, houseLords))

        // 7. Business and Commerce Yogas
        yogas.addAll(evaluateBusinessYogas(chart, houseLords))

        // 8. Property and Real Estate Yogas
        yogas.addAll(evaluatePropertyYogas(chart, houseLords))

        // 9. Multiple Income Source Yogas
        yogas.addAll(evaluateMultipleIncomeYogas(chart, houseLords))

        // 10. Rare and Exceptional Wealth Yogas
        yogas.addAll(evaluateRareWealthYogas(chart, houseLords, ascendantSign))

        return yogas
    }

    // ==================== CLASSICAL DHANA YOGAS ====================

    private fun evaluateClassicalDhanaYogas(
        chart: VedicChart,
        houseLords: Map<Int, Planet>
    ): List<Yoga> {
        val yogas = mutableListOf<Yoga>()

        val lord1 = houseLords[1]
        val lord2 = houseLords[2]
        val lord5 = houseLords[5]
        val lord9 = houseLords[9]
        val lord11 = houseLords[11]

        // Get positions
        val lord1Pos = if (lord1 != null) chart.planetPositions.find { it.planet == lord1 } else null
        val lord2Pos = if (lord2 != null) chart.planetPositions.find { it.planet == lord2 } else null
        val lord5Pos = if (lord5 != null) chart.planetPositions.find { it.planet == lord5 } else null
        val lord9Pos = if (lord9 != null) chart.planetPositions.find { it.planet == lord9 } else null
        val lord11Pos = if (lord11 != null) chart.planetPositions.find { it.planet == lord11 } else null

        // Dhana Yoga Type 1: 2nd lord with 11th lord
        if (lord2Pos != null && lord11Pos != null) {
            val connected = YogaHelpers.areConjunct(lord2Pos, lord11Pos) ||
                    YogaHelpers.isAspecting(lord2Pos, lord11Pos) ||
                    YogaHelpers.isAspecting(lord11Pos, lord2Pos)

            if (connected) {
                val isConjunct = YogaHelpers.areConjunct(lord2Pos, lord11Pos)
                val strength = if (isConjunct) 85.0 else 75.0

                yogas.add(Yoga(
                    name = "Dhana-Labha Yoga",
                    sanskritName = "धन-लाभ योग",
                    category = YogaCategory.DHANA_YOGA,
                    planets = listOfNotNull(lord2, lord11).distinct(),
                    houses = listOf(lord2Pos.house, lord11Pos.house).distinct(),
                    description = "2nd lord ${lord2!!.displayName} ${if (isConjunct) "conjunct" else "connected with"} 11th lord ${lord11!!.displayName}",
                    effects = "Wealth through gains, steady income flow, speech brings profit, family and network support finances, accumulated wealth, multiple income sources",
                    strength = YogaHelpers.strengthFromPercentage(strength),
                    strengthPercentage = strength,
                    isAuspicious = true,
                    activationPeriod = "2nd and 11th lord dashas for wealth accumulation",
                    cancellationFactors = listOf("Direct wealth houses connected", "Very reliable wealth yoga")
                ))
            }
        }

        // Dhana Yoga Type 2: 5th lord with 9th lord (Trikona wealth)
        if (lord5Pos != null && lord9Pos != null) {
            val connected = YogaHelpers.areConjunct(lord5Pos, lord9Pos) ||
                    YogaHelpers.isAspecting(lord5Pos, lord9Pos) ||
                    YogaHelpers.isAspecting(lord9Pos, lord5Pos)

            if (connected) {
                val isConjunct = YogaHelpers.areConjunct(lord5Pos, lord9Pos)
                val strength = if (isConjunct) 90.0 else 80.0

                yogas.add(Yoga(
                    name = "Trikona Dhana Yoga",
                    sanskritName = "त्रिकोण धन योग",
                    category = YogaCategory.DHANA_YOGA,
                    planets = listOfNotNull(lord5, lord9).distinct(),
                    houses = listOf(lord5Pos.house, lord9Pos.house).distinct(),
                    description = "5th lord ${lord5!!.displayName} ${if (isConjunct) "conjunct" else "connected with"} 9th lord ${lord9!!.displayName}",
                    effects = "Purva punya wealth, fortunate investments, children bring wealth, speculative gains, creative income, father's wealth, inherited fortune",
                    strength = YogaHelpers.strengthFromPercentage(strength),
                    strengthPercentage = strength,
                    isAuspicious = true,
                    activationPeriod = "5th and 9th lord dashas for fortunate wealth",
                    cancellationFactors = listOf("Trikona lords always beneficial", "Wealth through dharma")
                ))
            }
        }

        // Dhana Yoga Type 3: 1st lord with 2nd lord
        if (lord1Pos != null && lord2Pos != null) {
            val connected = YogaHelpers.areConjunct(lord1Pos, lord2Pos) ||
                    lord1Pos.house == 2 || lord2Pos.house == 1

            if (connected) {
                val strength = 80.0

                yogas.add(Yoga(
                    name = "Lagna-Dhana Yoga",
                    sanskritName = "लग्न-धन योग",
                    category = YogaCategory.DHANA_YOGA,
                    planets = listOfNotNull(lord1, lord2).distinct(),
                    houses = listOf(lord1Pos.house, lord2Pos.house).distinct(),
                    description = "1st lord ${lord1!!.displayName} connected with 2nd lord ${lord2!!.displayName}",
                    effects = "Self-earned wealth, personal efforts bring money, identity tied to wealth, good speech for earning, family supports self",
                    strength = YogaHelpers.strengthFromPercentage(strength),
                    strengthPercentage = strength,
                    isAuspicious = true,
                    activationPeriod = "Lagna lord dasha for self-made wealth",
                    cancellationFactors = listOf("Self and wealth houses connected", "Personal efforts matter")
                ))
            }
        }

        // Count multiple dhana yoga combinations
        val dhanaLordConnections = mutableListOf<Pair<Planet, Planet>>()
        val dhanaLords = listOfNotNull(lord1, lord2, lord5, lord9, lord11).distinct()

        for (i in dhanaLords.indices) {
            for (j in i + 1 until dhanaLords.size) {
                val pos1 = chart.planetPositions.find { it.planet == dhanaLords[i] }
                val pos2 = chart.planetPositions.find { it.planet == dhanaLords[j] }
                if (pos1 != null && pos2 != null &&
                    (YogaHelpers.areConjunct(pos1, pos2) ||
                            YogaHelpers.isAspecting(pos1, pos2) ||
                            YogaHelpers.isAspecting(pos2, pos1))) {
                    dhanaLordConnections.add(Pair(dhanaLords[i], dhanaLords[j]))
                }
            }
        }

        if (dhanaLordConnections.size >= 3) {
            yogas.add(Yoga(
                name = "Bahudha Dhana Yoga",
                sanskritName = "बहुधा धन योग",
                category = YogaCategory.DHANA_YOGA,
                planets = dhanaLordConnections.flatMap { listOf(it.first, it.second) }.distinct(),
                houses = emptyList(),
                description = "${dhanaLordConnections.size} wealth-producing lord connections in chart",
                effects = "Multiple wealth yogas create exceptional prosperity, diversified income, wealth from multiple sources, financially blessed life, growing fortune",
                strength = YogaStrength.VERY_STRONG,
                strengthPercentage = 90.0,
                isAuspicious = true,
                activationPeriod = "Multiple wealth-lord dashas all bring prosperity",
                cancellationFactors = listOf("Multiple dhana yogas multiply results", "Very fortunate financially")
            ))
        }

        return yogas
    }

    // ==================== LAKSHMI YOGAS ====================

    private fun evaluateLakshmiYogas(
        chart: VedicChart,
        houseLords: Map<Int, Planet>
    ): List<Yoga> {
        val yogas = mutableListOf<Yoga>()

        val lord1 = houseLords[1]
        val lord9 = houseLords[9]
        val venusPos = chart.planetPositions.find { it.planet == Planet.VENUS }
        val jupiterPos = chart.planetPositions.find { it.planet == Planet.JUPITER }

        val lord1Pos = if (lord1 != null) chart.planetPositions.find { it.planet == lord1 } else null
        val lord9Pos = if (lord9 != null) chart.planetPositions.find { it.planet == lord9 } else null

        // Lakshmi Yoga Type 1: 9th lord strong in kendra/trikona, lagna lord also strong
        if (lord9Pos != null && lord1Pos != null) {
            val lord9Strong = lord9Pos.house in listOf(1, 4, 5, 7, 9, 10) &&
                    (YogaHelpers.isExalted(lord9Pos) || YogaHelpers.isInOwnSign(lord9Pos))
            val lord1Strong = lord1Pos.house in listOf(1, 4, 5, 7, 9, 10)

            if (lord9Strong && lord1Strong) {
                yogas.add(Yoga(
                    name = "Maha Lakshmi Yoga",
                    sanskritName = "महा लक्ष्मी योग",
                    category = YogaCategory.DHANA_YOGA,
                    planets = listOfNotNull(lord9, lord1).distinct(),
                    houses = listOf(lord9Pos.house, lord1Pos.house).distinct(),
                    description = "9th lord ${lord9!!.displayName} dignified in kendra/trikona with strong lagna lord",
                    effects = "Goddess Lakshmi's full blessings, exceptional wealth, high status, luxury lifestyle, generous nature, wealth for dharmic purposes, lasting prosperity",
                    strength = YogaStrength.EXTREMELY_STRONG,
                    strengthPercentage = 95.0,
                    isAuspicious = true,
                    activationPeriod = "9th lord dasha brings abundant wealth",
                    cancellationFactors = listOf("Classical Lakshmi Yoga", "One of the most auspicious wealth yogas")
                ))
            }
        }

        // Lakshmi Yoga Type 2: Venus in own/exalted sign in kendra/trikona
        if (venusPos != null) {
            val venusStrong = (YogaHelpers.isExalted(venusPos) || YogaHelpers.isInOwnSign(venusPos)) &&
                    venusPos.house in listOf(1, 4, 5, 7, 9, 10)

            if (venusStrong) {
                yogas.add(Yoga(
                    name = "Shukra Lakshmi Yoga",
                    sanskritName = "शुक्र लक्ष्मी योग",
                    category = YogaCategory.DHANA_YOGA,
                    planets = listOf(Planet.VENUS),
                    houses = listOf(venusPos.house),
                    description = "Venus dignified in kendra/trikona (house ${venusPos.house})",
                    effects = "Venus as Lakshmi karaka brings luxury, wealth through arts/beauty/relationships, comfortable lifestyle, beautiful possessions, spouse brings wealth",
                    strength = YogaStrength.VERY_STRONG,
                    strengthPercentage = 85.0,
                    isAuspicious = true,
                    activationPeriod = "Venus dasha for luxury and wealth",
                    cancellationFactors = listOf("Venus is natural significator of Lakshmi", "Attracts wealth naturally")
                ))
            }
        }

        // Chandra-Mangala Yoga - Moon and Mars conjunction (wealth yoga)
        val moonPos = chart.planetPositions.find { it.planet == Planet.MOON }
        val marsPos = chart.planetPositions.find { it.planet == Planet.MARS }

        if (moonPos != null && marsPos != null && YogaHelpers.areConjunct(moonPos, marsPos)) {
            val inGoodHouse = moonPos.house in listOf(1, 2, 4, 5, 9, 10, 11)
            val strength = if (inGoodHouse) 80.0 else 65.0

            yogas.add(Yoga(
                name = "Chandra-Mangala Yoga",
                sanskritName = "चन्द्र-मंगल योग",
                category = YogaCategory.DHANA_YOGA,
                planets = listOf(Planet.MOON, Planet.MARS),
                houses = listOf(moonPos.house),
                description = "Moon and Mars conjunct in house ${moonPos.house}",
                effects = "Wealth through action and emotion combined, business acumen, property dealings, inherited wealth, mother's support in finances, entrepreneurial mind",
                strength = YogaHelpers.strengthFromPercentage(strength),
                strengthPercentage = strength,
                isAuspicious = inGoodHouse,
                activationPeriod = "Moon-Mars or Mars-Moon periods bring financial opportunities",
                cancellationFactors = listOf("Classical wealth yoga", "Better in wealth houses")
            ))
        }

        return yogas
    }

    // ==================== 2ND HOUSE YOGAS ====================

    private fun evaluate2ndHouseYogas(
        chart: VedicChart,
        houseLords: Map<Int, Planet>
    ): List<Yoga> {
        val yogas = mutableListOf<Yoga>()

        val planetsIn2 = chart.planetPositions.filter { it.house == 2 }
        val lord2 = houseLords[2]
        val lord2Pos = if (lord2 != null) chart.planetPositions.find { it.planet == lord2 } else null

        // Benefics in 2nd house
        val beneficsIn2 = planetsIn2.filter { it.planet in listOf(Planet.JUPITER, Planet.VENUS, Planet.MERCURY, Planet.MOON) }
        if (beneficsIn2.isNotEmpty()) {
            val jupiterPresent = beneficsIn2.any { it.planet == Planet.JUPITER }
            val strength = if (jupiterPresent) 85.0 else 75.0

            yogas.add(Yoga(
                name = "Shubha Dhana Bhava Yoga",
                sanskritName = "शुभ धन भाव योग",
                category = YogaCategory.DHANA_YOGA,
                planets = beneficsIn2.map { it.planet },
                houses = listOf(2),
                description = "${beneficsIn2.size} benefic(s) in 2nd house: ${beneficsIn2.joinToString { it.planet.displayName }}",
                effects = "Accumulated wealth, sweet speech, good family relations, food abundance, savings grow, collections/valuables, beautiful voice${if (jupiterPresent) ", Jupiter's wisdom in finances" else ""}",
                strength = YogaHelpers.strengthFromPercentage(strength),
                strengthPercentage = strength,
                isAuspicious = true,
                activationPeriod = "Benefic planet dashas for wealth growth",
                cancellationFactors = listOf("Benefics in wealth house are excellent", "Jupiter gives wisdom in spending")
            ))
        }

        // 2nd lord exalted or in own sign
        if (lord2Pos != null && (YogaHelpers.isExalted(lord2Pos) || YogaHelpers.isInOwnSign(lord2Pos))) {
            yogas.add(Yoga(
                name = "Uccha/Swakshetra Dhanesh Yoga",
                sanskritName = "उच्च/स्वक्षेत्र धनेश योग",
                category = YogaCategory.DHANA_YOGA,
                planets = listOf(lord2!!),
                houses = listOf(lord2Pos.house),
                description = "2nd lord ${lord2.displayName} in ${if (YogaHelpers.isExalted(lord2Pos)) "exaltation" else "own sign"}",
                effects = "Strong wealth accumulation, self-made fortune, excellent speech for earning, family prosperity, valuable assets, financial wisdom",
                strength = YogaStrength.STRONG,
                strengthPercentage = 80.0,
                isAuspicious = true,
                activationPeriod = "2nd lord dasha for wealth building",
                cancellationFactors = listOf("Dignified wealth lord is powerful", "Speech and family also benefit")
            ))
        }

        // 2nd lord in 11th or 11th lord in 2nd
        val lord11 = houseLords[11]
        val lord11Pos = if (lord11 != null) chart.planetPositions.find { it.planet == lord11 } else null

        if (lord2Pos?.house == 11 || lord11Pos?.house == 2) {
            yogas.add(Yoga(
                name = "Dhana-Labha Exchange Yoga",
                sanskritName = "धन-लाभ परस्पर योग",
                category = YogaCategory.DHANA_YOGA,
                planets = listOfNotNull(lord2, lord11).distinct(),
                houses = listOf(2, 11),
                description = "${if (lord2Pos?.house == 11) "2nd lord in 11th" else "11th lord in 2nd"}",
                effects = "Savings grow into gains, income supports family, network helps wealth, steady financial growth, multiple income channels",
                strength = YogaStrength.STRONG,
                strengthPercentage = 80.0,
                isAuspicious = true,
                activationPeriod = "2nd and 11th lord periods for financial growth",
                cancellationFactors = listOf("Wealth houses supporting each other", "Financial stability yoga")
            ))
        }

        return yogas
    }

    // ==================== 11TH HOUSE YOGAS ====================

    private fun evaluate11thHouseYogas(
        chart: VedicChart,
        houseLords: Map<Int, Planet>
    ): List<Yoga> {
        val yogas = mutableListOf<Yoga>()

        val planetsIn11 = chart.planetPositions.filter { it.house == 11 }
        val lord11 = houseLords[11]
        val lord11Pos = if (lord11 != null) chart.planetPositions.find { it.planet == lord11 } else null

        // Multiple planets in 11th - Labha Yoga
        if (planetsIn11.size >= 3) {
            val beneficCount = planetsIn11.count { it.planet in listOf(Planet.JUPITER, Planet.VENUS, Planet.MERCURY, Planet.MOON) }
            val strength = 70.0 + beneficCount * 5 + planetsIn11.size * 2

            yogas.add(Yoga(
                name = "Bahudha Labha Yoga",
                sanskritName = "बहुधा लाभ योग",
                category = YogaCategory.DHANA_YOGA,
                planets = planetsIn11.map { it.planet },
                houses = listOf(11),
                description = "${planetsIn11.size} planets in 11th house of gains",
                effects = "Multiple sources of income, fulfilled desires, supportive network, elder siblings help, gains from many directions, aspirations achieved",
                strength = YogaHelpers.strengthFromPercentage(strength.coerceAtMost(95.0)),
                strengthPercentage = strength.coerceAtMost(95.0),
                isAuspicious = beneficCount >= planetsIn11.size / 2,
                activationPeriod = "11th house planets' dashas all bring gains",
                cancellationFactors = listOf("11th is upachaya - results improve over time", "More planets = more income sources")
            ))
        }

        // Jupiter in 11th - Guru Labha Yoga
        val jupiterIn11 = planetsIn11.find { it.planet == Planet.JUPITER }
        if (jupiterIn11 != null) {
            yogas.add(Yoga(
                name = "Guru Labha Yoga",
                sanskritName = "गुरु लाभ योग",
                category = YogaCategory.DHANA_YOGA,
                planets = listOf(Planet.JUPITER),
                houses = listOf(11),
                description = "Jupiter in 11th house of gains",
                effects = "Gains through wisdom and teaching, wealthy elder siblings, learned friends, righteous income, spiritual gains, children's friends helpful, optimistic about gains",
                strength = YogaStrength.STRONG,
                strengthPercentage = 80.0,
                isAuspicious = true,
                activationPeriod = "Jupiter dasha for ethical wealth gains",
                cancellationFactors = listOf("Jupiter expands gains", "Income through knowledge/teaching")
            ))
        }

        // 11th lord strong
        if (lord11Pos != null && lord11Pos.house in listOf(1, 2, 5, 9, 10, 11)) {
            val isDignified = YogaHelpers.isExalted(lord11Pos) || YogaHelpers.isInOwnSign(lord11Pos)
            val strength = if (isDignified) 85.0 else 75.0

            yogas.add(Yoga(
                name = "Labhesha Bala Yoga",
                sanskritName = "लाभेश बल योग",
                category = YogaCategory.DHANA_YOGA,
                planets = listOf(lord11!!),
                houses = listOf(lord11Pos.house),
                description = "11th lord ${lord11.displayName} well-placed${if (isDignified) " and dignified" else ""}",
                effects = "Strong fulfillment of desires, reliable income flow, supportive network, elder siblings prosper, gains increase over time, aspirations achieved",
                strength = YogaHelpers.strengthFromPercentage(strength),
                strengthPercentage = strength,
                isAuspicious = true,
                activationPeriod = "11th lord dasha for maximum gains",
                cancellationFactors = listOf("Strong 11th lord supports all gains", "Network and aspirations flourish")
            ))
        }

        return yogas
    }

    // ==================== PLANET WEALTH YOGAS ====================

    private fun evaluatePlanetWealthYogas(
        chart: VedicChart,
        houseLords: Map<Int, Planet>
    ): List<Yoga> {
        val yogas = mutableListOf<Yoga>()

        // Mercury wealth yogas - Business acumen
        val mercuryPos = chart.planetPositions.find { it.planet == Planet.MERCURY }
        if (mercuryPos != null) {
            val isDignified = YogaHelpers.isExalted(mercuryPos) || YogaHelpers.isInOwnSign(mercuryPos)
            val inWealthHouse = mercuryPos.house in listOf(2, 10, 11)

            if (isDignified && inWealthHouse) {
                yogas.add(Yoga(
                    name = "Budha Dhana Yoga",
                    sanskritName = "बुध धन योग",
                    category = YogaCategory.DHANA_YOGA,
                    planets = listOf(Planet.MERCURY),
                    houses = listOf(mercuryPos.house),
                    description = "Mercury dignified in wealth house ${mercuryPos.house}",
                    effects = "Wealth through intellect, business success, trading profits, writing/communication income, accounting skills, multiple businesses, clever investments",
                    strength = YogaStrength.STRONG,
                    strengthPercentage = 80.0,
                    isAuspicious = true,
                    activationPeriod = "Mercury dasha for business success",
                    cancellationFactors = listOf("Mercury signifies trade and intellect", "Business acumen enhanced")
                ))
            }
        }

        // Sun wealth yogas - Government/Authority income
        val sunPos = chart.planetPositions.find { it.planet == Planet.SUN }
        if (sunPos != null) {
            val lord10 = houseLords[10]
            val isSunLord10 = lord10 == Planet.SUN
            val sunIn10Or11 = sunPos.house in listOf(10, 11)

            if (isSunLord10 || (YogaHelpers.isExalted(sunPos) && sunIn10Or11)) {
                yogas.add(Yoga(
                    name = "Surya Dhana Yoga",
                    sanskritName = "सूर्य धन योग",
                    category = YogaCategory.DHANA_YOGA,
                    planets = listOf(Planet.SUN),
                    houses = listOf(sunPos.house),
                    description = "Sun${if (isSunLord10) " as 10th lord" else ""} in position for authority-based income",
                    effects = "Wealth through government/authority, father's wealth, leadership income, gold/precious items, self-employment success, medicine/healing income",
                    strength = YogaStrength.STRONG,
                    strengthPercentage = 75.0,
                    isAuspicious = true,
                    activationPeriod = "Sun dasha for government/authority income",
                    cancellationFactors = listOf("Sun signifies authority and gold", "Father may be wealthy")
                ))
            }
        }

        // Saturn wealth yogas - Slow steady income
        val saturnPos = chart.planetPositions.find { it.planet == Planet.SATURN }
        if (saturnPos != null) {
            val saturnIn11 = saturnPos.house == 11
            val isDignified = YogaHelpers.isExalted(saturnPos) || YogaHelpers.isInOwnSign(saturnPos)

            if (saturnIn11 || (isDignified && saturnPos.house in listOf(2, 10))) {
                yogas.add(Yoga(
                    name = "Shani Dhana Yoga",
                    sanskritName = "शनि धन योग",
                    category = YogaCategory.DHANA_YOGA,
                    planets = listOf(Planet.SATURN),
                    houses = listOf(saturnPos.house),
                    description = "Saturn ${if (saturnIn11) "in 11th house" else "dignified in house ${saturnPos.house}"}",
                    effects = "Slow but steady wealth accumulation, real estate gains, oil/mining industry, service industry income, elderly clients, lasting wealth, old items valuable",
                    strength = YogaStrength.STRONG,
                    strengthPercentage = 75.0,
                    isAuspicious = true,
                    activationPeriod = "Saturn dasha (especially after 36) for accumulated wealth",
                    cancellationFactors = listOf("Saturn gives lasting results", "Patience brings wealth")
                ))
            }
        }

        return yogas
    }

    // ==================== HIDDEN WEALTH YOGAS ====================

    private fun evaluateHiddenWealthYogas(
        chart: VedicChart,
        houseLords: Map<Int, Planet>
    ): List<Yoga> {
        val yogas = mutableListOf<Yoga>()

        val lord8 = houseLords[8]
        val lord8Pos = if (lord8 != null) chart.planetPositions.find { it.planet == lord8 } else null

        // 8th lord well-placed - Inheritance/Hidden wealth
        if (lord8Pos != null && lord8Pos.house in listOf(1, 2, 5, 9, 11)) {
            val isDignified = YogaHelpers.isExalted(lord8Pos) || YogaHelpers.isInOwnSign(lord8Pos)
            val strength = if (isDignified) 80.0 else 70.0

            yogas.add(Yoga(
                name = "Ashtamesh Dhana Yoga",
                sanskritName = "अष्टमेश धन योग",
                category = YogaCategory.DHANA_YOGA,
                planets = listOf(lord8!!),
                houses = listOf(lord8Pos.house),
                description = "8th lord ${lord8.displayName} well-placed in house ${lord8Pos.house}",
                effects = "Inheritance likely, hidden wealth surfaces, insurance benefits, spouse's family wealth, sudden gains, research/occult income, transformative financial events",
                strength = YogaHelpers.strengthFromPercentage(strength),
                strengthPercentage = strength,
                isAuspicious = true,
                activationPeriod = "8th lord dasha for unexpected wealth",
                cancellationFactors = listOf("8th house rules inheritance", "Sudden wealth possible")
            ))
        }

        // Planets in 8th house aspected by benefics
        val planetsIn8 = chart.planetPositions.filter { it.house == 8 }
        val jupiterPos = chart.planetPositions.find { it.planet == Planet.JUPITER }

        for (planetIn8 in planetsIn8) {
            val jupiterAspects = jupiterPos?.let { YogaHelpers.isAspecting(it, planetIn8) } == true

            if (jupiterAspects) {
                yogas.add(Yoga(
                    name = "Guptha Dhana Yoga",
                    sanskritName = "गुप्त धन योग",
                    category = YogaCategory.DHANA_YOGA,
                    planets = listOf(planetIn8.planet, Planet.JUPITER),
                    houses = listOf(8, jupiterPos?.house ?: 0).filter { it > 0 },
                    description = "${planetIn8.planet.displayName} in 8th aspected by Jupiter",
                    effects = "Hidden wealth protected, inheritance likely, research brings income, occult knowledge profitable, insurance/legacy benefits, transformation of finances",
                    strength = YogaStrength.MODERATE,
                    strengthPercentage = 70.0,
                    isAuspicious = true,
                    activationPeriod = "${planetIn8.planet.displayName}-Jupiter or Jupiter-${planetIn8.planet.displayName} periods",
                    cancellationFactors = listOf("Jupiter protects 8th house matters", "Hidden assets reveal positively")
                ))
            }
        }

        // Rahu in 2nd or 11th - Unconventional wealth
        val rahuPos = chart.planetPositions.find { it.planet == Planet.RAHU }
        if (rahuPos != null && rahuPos.house in listOf(2, 11)) {
            val jupiterAspect = jupiterPos?.let { YogaHelpers.isAspecting(it, rahuPos) } == true
            val strength = if (jupiterAspect) 75.0 else 60.0

            yogas.add(Yoga(
                name = "Rahu Dhana Yoga",
                sanskritName = "राहु धन योग",
                category = YogaCategory.DHANA_YOGA,
                planets = listOfNotNull(Planet.RAHU, if (jupiterAspect) Planet.JUPITER else null),
                houses = listOf(rahuPos.house),
                description = "Rahu in ${if (rahuPos.house == 2) "2nd" else "11th"} house${if (jupiterAspect) " with Jupiter's aspect" else ""}",
                effects = "Wealth through unconventional means, foreign income, technology profits, sudden gains, speculation success, gambling luck (if aspected well), networking wealth",
                strength = YogaHelpers.strengthFromPercentage(strength),
                strengthPercentage = strength,
                isAuspicious = jupiterAspect,
                activationPeriod = "Rahu dasha for unusual wealth channels",
                cancellationFactors = listOf(
                    if (jupiterAspect) "Jupiter sanctifies Rahu" else "Jupiter aspect would stabilize",
                    "Foreign/technology income likely"
                )
            ))
        }

        return yogas
    }

    // ==================== BUSINESS YOGAS ====================

    private fun evaluateBusinessYogas(
        chart: VedicChart,
        houseLords: Map<Int, Planet>
    ): List<Yoga> {
        val yogas = mutableListOf<Yoga>()

        val lord3 = houseLords[3]  // Enterprise, courage
        val lord7 = houseLords[7]  // Partnerships, business
        val lord10 = houseLords[10] // Career

        val lord3Pos = if (lord3 != null) chart.planetPositions.find { it.planet == lord3 } else null
        val lord7Pos = if (lord7 != null) chart.planetPositions.find { it.planet == lord7 } else null
        val lord10Pos = if (lord10 != null) chart.planetPositions.find { it.planet == lord10 } else null

        // 3rd and 10th lord connection - Enterprise success
        if (lord3Pos != null && lord10Pos != null) {
            val connected = YogaHelpers.areConjunct(lord3Pos, lord10Pos) ||
                    YogaHelpers.isAspecting(lord3Pos, lord10Pos)

            if (connected) {
                yogas.add(Yoga(
                    name = "Parakrama Karma Yoga",
                    sanskritName = "पराक्रम कर्म योग",
                    category = YogaCategory.DHANA_YOGA,
                    planets = listOfNotNull(lord3, lord10).distinct(),
                    houses = listOf(lord3Pos.house, lord10Pos.house).distinct(),
                    description = "3rd lord ${lord3!!.displayName} connected with 10th lord ${lord10!!.displayName}",
                    effects = "Entrepreneurial success, courage in business, self-made career, communication-based income, media/writing profession, siblings in business together",
                    strength = YogaStrength.STRONG,
                    strengthPercentage = 75.0,
                    isAuspicious = true,
                    activationPeriod = "3rd and 10th lord periods for business success",
                    cancellationFactors = listOf("Courage supports career", "Self-employment favored")
                ))
            }
        }

        // 7th lord well-placed - Partnership business
        if (lord7Pos != null && lord7Pos.house in listOf(1, 2, 5, 9, 10, 11)) {
            yogas.add(Yoga(
                name = "Vyapara Yoga",
                sanskritName = "व्यापार योग",
                category = YogaCategory.DHANA_YOGA,
                planets = listOf(lord7!!),
                houses = listOf(lord7Pos.house),
                description = "7th lord ${lord7.displayName} in auspicious house ${lord7Pos.house}",
                effects = "Business partnerships profitable, spouse supports business, public dealings succeed, trade and commerce, legal contracts favorable, customer relations strong",
                strength = YogaStrength.MODERATE,
                strengthPercentage = 70.0,
                isAuspicious = true,
                activationPeriod = "7th lord dasha for partnership success",
                cancellationFactors = listOf("7th rules partnerships and trade", "Public dealings favored")
            ))
        }

        // Mercury + Saturn combination - Business discipline
        val mercuryPos = chart.planetPositions.find { it.planet == Planet.MERCURY }
        val saturnPos = chart.planetPositions.find { it.planet == Planet.SATURN }

        if (mercuryPos != null && saturnPos != null && YogaHelpers.areConjunct(mercuryPos, saturnPos)) {
            val inGoodHouse = mercuryPos.house in listOf(2, 3, 6, 10, 11)
            val strength = if (inGoodHouse) 75.0 else 60.0

            yogas.add(Yoga(
                name = "Budha-Shani Vyapara Yoga",
                sanskritName = "बुध-शनि व्यापार योग",
                category = YogaCategory.DHANA_YOGA,
                planets = listOf(Planet.MERCURY, Planet.SATURN),
                houses = listOf(mercuryPos.house),
                description = "Mercury conjunct Saturn in house ${mercuryPos.house}",
                effects = "Business discipline, accounting skills, systematic approach to money, long-term planning, service business, detailed contracts, patience in commerce",
                strength = YogaHelpers.strengthFromPercentage(strength),
                strengthPercentage = strength,
                isAuspicious = inGoodHouse,
                activationPeriod = "Mercury-Saturn or Saturn-Mercury periods for business establishment",
                cancellationFactors = listOf("Intellect + discipline = business success", "May be slow but thorough")
            ))
        }

        return yogas
    }

    // ==================== PROPERTY YOGAS ====================

    private fun evaluatePropertyYogas(
        chart: VedicChart,
        houseLords: Map<Int, Planet>
    ): List<Yoga> {
        val yogas = mutableListOf<Yoga>()

        val lord4 = houseLords[4]
        val lord4Pos = if (lord4 != null) chart.planetPositions.find { it.planet == lord4 } else null
        val marsPos = chart.planetPositions.find { it.planet == Planet.MARS }

        // 4th lord well-placed - Property yoga
        if (lord4Pos != null && lord4Pos.house in listOf(1, 2, 4, 5, 9, 10, 11)) {
            val isDignified = YogaHelpers.isExalted(lord4Pos) || YogaHelpers.isInOwnSign(lord4Pos)
            val strength = if (isDignified) 85.0 else 75.0

            yogas.add(Yoga(
                name = "Bhumi Yoga",
                sanskritName = "भूमि योग",
                category = YogaCategory.DHANA_YOGA,
                planets = listOf(lord4!!),
                houses = listOf(lord4Pos.house),
                description = "4th lord ${lord4.displayName} well-placed${if (isDignified) " and dignified" else ""}",
                effects = "Property ownership, land gains, real estate success, comfortable home, vehicles, mother's wealth, agricultural income, mining interests",
                strength = YogaHelpers.strengthFromPercentage(strength),
                strengthPercentage = strength,
                isAuspicious = true,
                activationPeriod = "4th lord dasha for property acquisition",
                cancellationFactors = listOf("4th rules property and land", "Real estate success indicated")
            ))
        }

        // Mars in 4th - Property through effort
        if (marsPos?.house == 4) {
            val isDignified = YogaHelpers.isExalted(marsPos) || YogaHelpers.isInOwnSign(marsPos)
            val strength = if (isDignified) 80.0 else 65.0

            yogas.add(Yoga(
                name = "Kuja Bhumi Yoga",
                sanskritName = "कुज भूमि योग",
                category = YogaCategory.DHANA_YOGA,
                planets = listOf(Planet.MARS),
                houses = listOf(4),
                description = "Mars in 4th house${if (isDignified) " in dignity" else ""}",
                effects = "Property through courage and action, land through competition, real estate battles won, construction industry, agricultural machinery, military housing",
                strength = YogaHelpers.strengthFromPercentage(strength),
                strengthPercentage = strength,
                isAuspicious = isDignified,
                activationPeriod = "Mars dasha for property dealings",
                cancellationFactors = listOf("Mars gives property through effort", "May have disputes but wins them")
            ))
        }

        // 4th and 10th connection - Property and career linked
        val lord10 = houseLords[10]
        val lord10Pos = if (lord10 != null) chart.planetPositions.find { it.planet == lord10 } else null

        if (lord4Pos != null && lord10Pos != null) {
            val exchange = lord4Pos.house == 10 && lord10Pos.house == 4
            val conjunct = YogaHelpers.areConjunct(lord4Pos, lord10Pos)

            if (exchange || conjunct) {
                yogas.add(Yoga(
                    name = "Griha-Karma Yoga",
                    sanskritName = "गृह-कर्म योग",
                    category = YogaCategory.DHANA_YOGA,
                    planets = listOfNotNull(lord4, lord10).distinct(),
                    houses = listOf(4, 10),
                    description = "4th and 10th lords ${if (exchange) "in exchange" else "conjunct"}",
                    effects = "Career in real estate, work from home, property through profession, office ownership, vehicles for work, family business, commercial property",
                    strength = YogaStrength.STRONG,
                    strengthPercentage = 80.0,
                    isAuspicious = true,
                    activationPeriod = "4th and 10th lord periods for property through career",
                    cancellationFactors = listOf("Home and career linked", "Real estate profession possible")
                ))
            }
        }

        return yogas
    }

    // ==================== MULTIPLE INCOME YOGAS ====================

    private fun evaluateMultipleIncomeYogas(
        chart: VedicChart,
        houseLords: Map<Int, Planet>
    ): List<Yoga> {
        val yogas = mutableListOf<Yoga>()

        // Count planets in income houses (2, 6, 10, 11)
        val incomeHouses = listOf(2, 6, 10, 11)
        val planetsInIncomeHouses = chart.planetPositions.filter { it.house in incomeHouses }

        if (planetsInIncomeHouses.size >= 5) {
            val beneficCount = planetsInIncomeHouses.count {
                it.planet in listOf(Planet.JUPITER, Planet.VENUS, Planet.MERCURY, Planet.MOON)
            }
            val strength = 70.0 + beneficCount * 3 + planetsInIncomeHouses.size * 2

            yogas.add(Yoga(
                name = "Bahu Artha Yoga",
                sanskritName = "बहु अर्थ योग",
                category = YogaCategory.DHANA_YOGA,
                planets = planetsInIncomeHouses.map { it.planet },
                houses = incomeHouses,
                description = "${planetsInIncomeHouses.size} planets in income houses (2, 6, 10, 11)",
                effects = "Multiple income streams, diversified wealth, service and business income, consistent earnings, financial stability through variety, protected from single-source dependency",
                strength = YogaHelpers.strengthFromPercentage(strength.coerceAtMost(95.0)),
                strengthPercentage = strength.coerceAtMost(95.0),
                isAuspicious = true,
                activationPeriod = "Throughout life with varying intensity",
                cancellationFactors = listOf("Multiple income houses activated", "Financial security through diversity")
            ))
        }

        // 2nd, 9th, 11th lords all strong
        val lord2 = houseLords[2]
        val lord9 = houseLords[9]
        val lord11 = houseLords[11]

        val lord2Pos = if (lord2 != null) chart.planetPositions.find { it.planet == lord2 } else null
        val lord9Pos = if (lord9 != null) chart.planetPositions.find { it.planet == lord9 } else null
        val lord11Pos = if (lord11 != null) chart.planetPositions.find { it.planet == lord11 } else null

        val allThreeStrong = lord2Pos != null && lord9Pos != null && lord11Pos != null &&
                lord2Pos.house in listOf(1, 2, 4, 5, 9, 10, 11) &&
                lord9Pos.house in listOf(1, 4, 5, 7, 9, 10) &&
                lord11Pos.house in listOf(1, 2, 5, 9, 10, 11)

        if (allThreeStrong) {
            yogas.add(Yoga(
                name = "Tridha Dhana Yoga",
                sanskritName = "त्रिधा धन योग",
                category = YogaCategory.DHANA_YOGA,
                planets = listOfNotNull(lord2, lord9, lord11).distinct(),
                houses = listOf(lord2Pos!!.house, lord9Pos!!.house, lord11Pos!!.house).distinct(),
                description = "2nd, 9th, and 11th lords all well-placed",
                effects = "Three-fold wealth: accumulated (2nd), fortunate (9th), and gains (11th) all strong, financially blessed life, wealth from past merit, desires fulfilled",
                strength = YogaStrength.VERY_STRONG,
                strengthPercentage = 85.0,
                isAuspicious = true,
                activationPeriod = "All three lords' dashas bring different types of wealth",
                cancellationFactors = listOf("Three wealth indicators strong", "Comprehensive financial blessing")
            ))
        }

        return yogas
    }

    // ==================== RARE WEALTH YOGAS ====================

    private fun evaluateRareWealthYogas(
        chart: VedicChart,
        houseLords: Map<Int, Planet>,
        ascendantSign: ZodiacSign
    ): List<Yoga> {
        val yogas = mutableListOf<Yoga>()

        // Mahalakshmi Yoga - Venus exalted/own sign aspected by Jupiter
        val venusPos = chart.planetPositions.find { it.planet == Planet.VENUS }
        val jupiterPos = chart.planetPositions.find { it.planet == Planet.JUPITER }

        if (venusPos != null && jupiterPos != null) {
            val venusStrong = YogaHelpers.isExalted(venusPos) || YogaHelpers.isInOwnSign(venusPos)
            val jupiterAspects = YogaHelpers.isAspecting(jupiterPos, venusPos)

            if (venusStrong && jupiterAspects) {
                yogas.add(Yoga(
                    name = "Mahalakshmi Yoga",
                    sanskritName = "महालक्ष्मी योग",
                    category = YogaCategory.DHANA_YOGA,
                    planets = listOf(Planet.VENUS, Planet.JUPITER),
                    houses = listOf(venusPos.house, jupiterPos.house),
                    description = "Venus dignified with Jupiter's aspect",
                    effects = "Goddess Mahalakshmi's blessings, immense wealth, luxury lifestyle, beautiful possessions, spouse brings fortune, artistic income, lasting prosperity",
                    strength = YogaStrength.EXTREMELY_STRONG,
                    strengthPercentage = 95.0,
                    isAuspicious = true,
                    activationPeriod = "Venus dasha with Jupiter antardasha for peak wealth",
                    cancellationFactors = listOf("Venus as Lakshmi + Jupiter's blessings", "Rare and powerful wealth yoga")
                ))
            }
        }

        // Chandika Yoga - Moon exalted in 10th
        val moonPos = chart.planetPositions.find { it.planet == Planet.MOON }
        if (moonPos != null && moonPos.house == 10 && YogaHelpers.isExalted(moonPos)) {
            yogas.add(Yoga(
                name = "Chandika Yoga",
                sanskritName = "चण्डिका योग",
                category = YogaCategory.DHANA_YOGA,
                planets = listOf(Planet.MOON),
                houses = listOf(10),
                description = "Moon exalted in 10th house",
                effects = "Goddess Chandika's blessings, public wealth, emotional connection to career, nurturing profession brings wealth, mass appeal, food/hospitality income",
                strength = YogaStrength.VERY_STRONG,
                strengthPercentage = 90.0,
                isAuspicious = true,
                activationPeriod = "Moon dasha for public recognition and wealth",
                cancellationFactors = listOf("Exalted Moon in career house", "Public loves the native")
            ))
        }

        // Kubera Yoga - Mercury, Jupiter, Venus in kendras
        val mercuryPos = chart.planetPositions.find { it.planet == Planet.MERCURY }
        val kendras = listOf(1, 4, 7, 10)

        val mercuryInKendra = mercuryPos?.house in kendras
        val jupiterInKendra = jupiterPos?.house in kendras
        val venusInKendra = venusPos?.house in kendras

        if (mercuryInKendra && jupiterInKendra && venusInKendra) {
            yogas.add(Yoga(
                name = "Kubera Yoga",
                sanskritName = "कुबेर योग",
                category = YogaCategory.DHANA_YOGA,
                planets = listOf(Planet.MERCURY, Planet.JUPITER, Planet.VENUS),
                houses = listOfNotNull(mercuryPos?.house, jupiterPos?.house, venusPos?.house).distinct(),
                description = "Mercury, Jupiter, and Venus all in kendras",
                effects = "Kubera (God of Wealth) blessings, treasurer-like wealth, business and wisdom combined, luxury through intelligence, artistic commerce, massive wealth potential",
                strength = YogaStrength.EXTREMELY_STRONG,
                strengthPercentage = 95.0,
                isAuspicious = true,
                activationPeriod = "All three planets' dashas bring wealth",
                cancellationFactors = listOf("Three benefics in angular houses", "Very rare and powerful")
            ))
        }

        // Indu Lagna based wealth - If 9th from Moon lord is strong
        if (moonPos != null) {
            val ninthFromMoon = ((moonPos.house - 1 + 8) % 12) + 1
            val ninthFromMoonSign = ZodiacSign.entries[(ZodiacSign.fromLongitude(moonPos.longitude).ordinal + 8) % 12]
            val lordOfNinthFromMoon = YogaHelpers.getSignLord(ninthFromMoonSign)
            val lordPos = chart.planetPositions.find { it.planet == lordOfNinthFromMoon }

            if (lordPos != null && (YogaHelpers.isExalted(lordPos) || YogaHelpers.isInOwnSign(lordPos))) {
                yogas.add(Yoga(
                    name = "Indu Lagna Dhana Yoga",
                    sanskritName = "इन्दु लग्न धन योग",
                    category = YogaCategory.DHANA_YOGA,
                    planets = listOf(Planet.MOON, lordOfNinthFromMoon),
                    houses = listOf(moonPos.house, lordPos.house),
                    description = "9th lord from Moon (${lordOfNinthFromMoon.displayName}) is dignified",
                    effects = "Indu Lagna (wealth ascendant) strong, inherited/family wealth, emotional connection to prosperity, mother's family wealthy, intuitive wealth decisions",
                    strength = YogaStrength.STRONG,
                    strengthPercentage = 80.0,
                    isAuspicious = true,
                    activationPeriod = "Moon dasha and 9th lord from Moon periods",
                    cancellationFactors = listOf("Indu Lagna is special wealth indicator", "Family wealth likely")
                ))
            }
        }

        return yogas
    }
}
