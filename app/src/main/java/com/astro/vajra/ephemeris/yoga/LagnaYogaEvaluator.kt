package com.astro.vajra.ephemeris.yoga

import com.astro.vajra.core.model.Planet
import com.astro.vajra.core.model.PlanetPosition
import com.astro.vajra.core.model.VedicChart
import com.astro.vajra.core.model.ZodiacSign

/**
 * Lagna (Ascendant) Yoga Evaluator - Ascendant-Based Combinations
 *
 * This evaluator handles yogas that are specifically formed based on the ascendant sign
 * and the condition of the ascendant lord. These yogas modify the overall life direction
 * and personality expression.
 *
 * Categories covered:
 * 1. Lagnesh (Ascendant Lord) Position Yogas - strength by house placement
 * 2. Ascendant Sign Specific Yogas - unique formations per sign
 * 3. Lagna Adhi Yogas - benefics around ascendant
 * 4. Subhakartari/Papakartari Lagna - enclosure yogas
 * 5. Hora Lagna Yogas - wealth/fortune combinations
 * 6. Ghatika Lagna Yogas - power/authority combinations
 * 7. Bhava Lagna Yogas - house lord relationships
 * 8. Rising Sign Planetary Yogas - specific planet combinations per ascendant
 *
 * Based on classical texts:
 * - Brihat Parasara Hora Shastra (BPHS), Chapters 24-28
 * - Phaladeepika, Chapters 6-8
 * - Jataka Parijata, Chapters 4-5
 * - Saravali, Chapters 30-35
 * - Laghu Parasari (Jataka Chandrika)
 *
 * @author AstroVajra
 */
class LagnaYogaEvaluator : YogaEvaluator {

    override val category: YogaCategory = YogaCategory.SPECIAL_YOGA

    override fun evaluate(chart: VedicChart): List<Yoga> {
        val yogas = mutableListOf<Yoga>()
        val ascendantSign = ZodiacSign.fromLongitude(chart.ascendant)
        val houseLords = YogaHelpers.getHouseLords(ascendantSign)

        // 1. Lagnesh Position Yogas
        yogas.addAll(evaluateLagneshPositionYogas(chart, houseLords, ascendantSign))

        // 2. Lagna Adhi Yogas - benefics supporting ascendant
        yogas.addAll(evaluateLagnaAdhiYogas(chart, houseLords))

        // 3. Lagna Kartari Yogas - hemming effects
        yogas.addAll(evaluateLagnaKartariYogas(chart))

        // 4. Ascendant Sign Specific Yogas
        yogas.addAll(evaluateAscendantSpecificYogas(chart, houseLords, ascendantSign))

        // 5. Lagna Strength Yogas
        yogas.addAll(evaluateLagnaStrengthYogas(chart, houseLords, ascendantSign))

        // 6. Bhava Lagna Relationship Yogas
        yogas.addAll(evaluateBhavaLagnaYogas(chart, houseLords))

        // 7. Special Lagna Combinations
        yogas.addAll(evaluateSpecialLagnaCombinations(chart, houseLords, ascendantSign))

        // 8. Navamsha Lagna Yogas
        yogas.addAll(evaluateNavamshaLagnaYogas(chart, houseLords, ascendantSign))

        return yogas
    }

    // ==================== LAGNESH POSITION YOGAS ====================

    private fun evaluateLagneshPositionYogas(
        chart: VedicChart,
        houseLords: Map<Int, Planet>,
        ascendantSign: ZodiacSign
    ): List<Yoga> {
        val yogas = mutableListOf<Yoga>()
        val lagnesh = houseLords[1] ?: return yogas
        val lagneshPos = chart.planetPositions.find { it.planet == lagnesh } ?: return yogas

        // Lagnesh in different houses - classical results
        when (lagneshPos.house) {
            1 -> {
                // Lagnesh in 1st - Strong personality
                val isExalted = YogaHelpers.isExalted(lagneshPos)
                val isOwnSign = YogaHelpers.isInOwnSign(lagneshPos)
                val strength = when {
                    isExalted -> 90.0
                    isOwnSign -> 80.0
                    else -> 70.0
                }

                yogas.add(Yoga(
                    name = "Lagnesh Lagna Sthiti Yoga",
                    sanskritName = "लग्नेश लग्न स्थिति योग",
                    category = YogaCategory.SPECIAL_YOGA,
                    planets = listOf(lagnesh),
                    houses = listOf(1),
                    description = "Ascendant lord ${lagnesh.displayName} placed in 1st house${if (isExalted) " exalted" else if (isOwnSign) " in own sign" else ""}",
                    effects = "Strong constitution, self-confidence, leadership abilities, recognized personality, good health, natural authority, ability to overcome obstacles through personal effort",
                    strength = YogaHelpers.strengthFromPercentage(strength),
                    strengthPercentage = strength,
                    isAuspicious = true,
                    activationPeriod = "${lagnesh.displayName} dasha/antardasha",
                    cancellationFactors = listOf("Best results when unafflicted", "Dignity enhances effects")
                ))
            }
            4 -> {
                // Lagnesh in 4th - Happiness and property
                val strength = if (YogaHelpers.isExalted(lagneshPos) || YogaHelpers.isInOwnSign(lagneshPos)) 80.0 else 70.0

                yogas.add(Yoga(
                    name = "Lagnesh Sukha Sthana Yoga",
                    sanskritName = "लग्नेश सुख स्थान योग",
                    category = YogaCategory.SPECIAL_YOGA,
                    planets = listOf(lagnesh),
                    houses = listOf(4),
                    description = "Ascendant lord ${lagnesh.displayName} placed in 4th house of happiness",
                    effects = "Domestic happiness, good vehicles, property gains, peaceful mind, good education, mother's blessings, comfortable life, emotional security",
                    strength = YogaHelpers.strengthFromPercentage(strength),
                    strengthPercentage = strength,
                    isAuspicious = true,
                    activationPeriod = "${lagnesh.displayName} dasha and 4th lord periods",
                    cancellationFactors = listOf("Results enhanced if 4th lord well-placed", "Malefic aspect reduces comfort")
                ))
            }
            5 -> {
                // Lagnesh in 5th - Intelligence and children
                val strength = if (YogaHelpers.isExalted(lagneshPos) || YogaHelpers.isInOwnSign(lagneshPos)) 85.0 else 75.0

                yogas.add(Yoga(
                    name = "Lagnesh Putra Bhava Yoga",
                    sanskritName = "लग्नेश पुत्र भाव योग",
                    category = YogaCategory.SPECIAL_YOGA,
                    planets = listOf(lagnesh),
                    houses = listOf(5),
                    description = "Ascendant lord ${lagnesh.displayName} in 5th house of progeny",
                    effects = "High intelligence, creative talents, good children, speculative gains, ministerial position, fame through creativity, romantic nature, spiritual inclination",
                    strength = YogaHelpers.strengthFromPercentage(strength),
                    strengthPercentage = strength,
                    isAuspicious = true,
                    activationPeriod = "${lagnesh.displayName} and 5th lord dashas",
                    cancellationFactors = listOf("Creates Dharma-Karma connection", "Jupiter's involvement enhances")
                ))
            }
            7 -> {
                // Lagnesh in 7th - Partnership focused
                val strength = 70.0

                yogas.add(Yoga(
                    name = "Lagnesh Kalatra Yoga",
                    sanskritName = "लग्नेश कलत्र योग",
                    category = YogaCategory.SPECIAL_YOGA,
                    planets = listOf(lagnesh),
                    houses = listOf(7),
                    description = "Ascendant lord ${lagnesh.displayName} in 7th house of partnerships",
                    effects = "Life defined by partnerships, spouse has strong influence, business success through partnerships, may travel abroad, public dealings, beautiful spouse, social person",
                    strength = YogaHelpers.strengthFromPercentage(strength),
                    strengthPercentage = strength,
                    isAuspicious = true,
                    activationPeriod = "${lagnesh.displayName} dasha for marriage and partnerships",
                    cancellationFactors = listOf("7th also maraka position - careful in dashas", "Benefic aspects improve partnership quality")
                ))
            }
            9 -> {
                // Lagnesh in 9th - Dharma Karmadhipati
                val strength = if (YogaHelpers.isExalted(lagneshPos) || YogaHelpers.isInOwnSign(lagneshPos)) 90.0 else 80.0

                yogas.add(Yoga(
                    name = "Lagnesh Bhagya Sthana Yoga",
                    sanskritName = "लग्नेश भाग्य स्थान योग",
                    category = YogaCategory.RAJA_YOGA,
                    planets = listOf(lagnesh),
                    houses = listOf(9),
                    description = "Ascendant lord ${lagnesh.displayName} in 9th house of fortune",
                    effects = "Highly fortunate, father's blessings, religious and philosophical nature, long-distance travel, guru's grace, righteous conduct brings success, pilgrimage, higher education",
                    strength = YogaHelpers.strengthFromPercentage(strength),
                    strengthPercentage = strength,
                    isAuspicious = true,
                    activationPeriod = "${lagnesh.displayName} dasha brings fortune and dharmic life",
                    cancellationFactors = listOf("Creates powerful Lakshmi Yoga potential", "9th lord's position enhances further")
                ))
            }
            10 -> {
                // Lagnesh in 10th - Career success
                val strength = if (YogaHelpers.isExalted(lagneshPos) || YogaHelpers.isInOwnSign(lagneshPos)) 90.0 else 80.0

                yogas.add(Yoga(
                    name = "Lagnesh Karma Sthana Yoga",
                    sanskritName = "लग्नेश कर्म स्थान योग",
                    category = YogaCategory.RAJA_YOGA,
                    planets = listOf(lagnesh),
                    houses = listOf(10),
                    description = "Ascendant lord ${lagnesh.displayName} in 10th house of career",
                    effects = "Strong career, recognized profession, fame and honor, government favor, leadership positions, father-like authority, achievement in chosen field, administrative abilities",
                    strength = YogaHelpers.strengthFromPercentage(strength),
                    strengthPercentage = strength,
                    isAuspicious = true,
                    activationPeriod = "${lagnesh.displayName} dasha peak career period",
                    cancellationFactors = listOf("One of the best placements for worldly success", "10th lord's placement enhances")
                ))
            }
            11 -> {
                // Lagnesh in 11th - Gains and fulfillment
                val strength = 80.0

                yogas.add(Yoga(
                    name = "Lagnesh Labha Sthana Yoga",
                    sanskritName = "लग्नेश लाभ स्थान योग",
                    category = YogaCategory.DHANA_YOGA,
                    planets = listOf(lagnesh),
                    houses = listOf(11),
                    description = "Ascendant lord ${lagnesh.displayName} in 11th house of gains",
                    effects = "Fulfillment of desires, steady income, supportive elder siblings, influential friends and networks, multiple income sources, achieving ambitions, social recognition",
                    strength = YogaHelpers.strengthFromPercentage(strength),
                    strengthPercentage = strength,
                    isAuspicious = true,
                    activationPeriod = "${lagnesh.displayName} dasha for maximum gains",
                    cancellationFactors = listOf("Upachaya placement gives growing results", "11th lord connection creates more wealth")
                ))
            }
            6 -> {
                // Lagnesh in 6th - Conflict and service
                val cancellations = mutableListOf<String>()
                if (YogaHelpers.isExalted(lagneshPos) || YogaHelpers.isInOwnSign(lagneshPos)) {
                    cancellations.add("${lagnesh.displayName} dignified overcomes enemies")
                }

                val jupiterAspect = chart.planetPositions.find { it.planet == Planet.JUPITER }
                    ?.let { YogaHelpers.isAspecting(it, lagneshPos) } == true
                if (jupiterAspect) cancellations.add("Jupiter aspect protects health")

                val strength = if (cancellations.isNotEmpty()) 50.0 else 40.0

                yogas.add(Yoga(
                    name = "Lagnesh Ripu Bhava Yoga",
                    sanskritName = "लग्नेश रिपु भाव योग",
                    category = YogaCategory.SPECIAL_YOGA,
                    planets = listOf(lagnesh),
                    houses = listOf(6),
                    description = "Ascendant lord ${lagnesh.displayName} in 6th house of enemies and service",
                    effects = "Victory over enemies when strong, service-oriented life, health concerns need attention, competitive nature, legal matters, debts require management, healing abilities if dignified",
                    strength = YogaHelpers.strengthFromPercentage(strength),
                    strengthPercentage = strength,
                    isAuspicious = false,
                    activationPeriod = "${lagnesh.displayName} dasha may bring health or legal matters",
                    cancellationFactors = cancellations.ifEmpty { listOf("Dignified lagnesh defeats enemies", "Mars/Saturn aspects give fighting spirit") }
                ))
            }
            8 -> {
                // Lagnesh in 8th - Transformation
                val cancellations = mutableListOf<String>()
                if (YogaHelpers.isExalted(lagneshPos) || YogaHelpers.isInOwnSign(lagneshPos)) {
                    cancellations.add("${lagnesh.displayName} dignified gives research abilities and longevity")
                }
                if (lagnesh == Planet.SATURN || lagnesh == Planet.MARS) {
                    cancellations.add("Natural 8th house significator handles energy better")
                }

                val strength = if (cancellations.isNotEmpty()) 55.0 else 35.0

                yogas.add(Yoga(
                    name = "Lagnesh Randhra Yoga",
                    sanskritName = "लग्नेश रन्ध्र योग",
                    category = YogaCategory.SPECIAL_YOGA,
                    planets = listOf(lagnesh),
                    houses = listOf(8),
                    description = "Ascendant lord ${lagnesh.displayName} in 8th house of transformation",
                    effects = "Life of transformations, interest in occult/research, inheritance possible, hidden talents, psychological depth, chronic health requires attention, sudden events change life direction",
                    strength = YogaHelpers.strengthFromPercentage(strength),
                    strengthPercentage = strength,
                    isAuspicious = false,
                    activationPeriod = "${lagnesh.displayName} dasha brings significant transformations",
                    cancellationFactors = cancellations.ifEmpty { listOf("8th lord's dignity affects outcome", "Jupiter aspect gives protection", "Can excel in research/healing") }
                ))
            }
            12 -> {
                // Lagnesh in 12th - Spirituality and losses
                val cancellations = mutableListOf<String>()
                if (YogaHelpers.isExalted(lagneshPos) || YogaHelpers.isInOwnSign(lagneshPos)) {
                    cancellations.add("${lagnesh.displayName} dignified gives spiritual advancement")
                }

                val jupiterPos = chart.planetPositions.find { it.planet == Planet.JUPITER }
                val venusPos = chart.planetPositions.find { it.planet == Planet.VENUS }
                if (jupiterPos?.house == 12 || venusPos?.house == 12) {
                    cancellations.add("Benefic in 12th transforms to spiritual gains")
                }

                val strength = if (cancellations.isNotEmpty()) 55.0 else 40.0

                yogas.add(Yoga(
                    name = "Lagnesh Vyaya Yoga",
                    sanskritName = "लग्नेश व्यय योग",
                    category = YogaCategory.SPECIAL_YOGA,
                    planets = listOf(lagnesh),
                    houses = listOf(12),
                    description = "Ascendant lord ${lagnesh.displayName} in 12th house of losses and liberation",
                    effects = "Spiritual inclinations strong, foreign residence possible, expenditure on good causes, bed pleasures, need for solitude, charitable nature, moksha potential if well-placed",
                    strength = YogaHelpers.strengthFromPercentage(strength),
                    strengthPercentage = strength,
                    isAuspicious = cancellations.isNotEmpty(),
                    activationPeriod = "${lagnesh.displayName} dasha for foreign travel or spiritual pursuits",
                    cancellationFactors = cancellations.ifEmpty { listOf("Good for spiritual life", "Foreign settlement possible", "Hospital/ashram/institution connection") }
                ))
            }
            2 -> {
                // Lagnesh in 2nd - Wealth focus
                val strength = if (YogaHelpers.isExalted(lagneshPos) || YogaHelpers.isInOwnSign(lagneshPos)) 80.0 else 70.0

                yogas.add(Yoga(
                    name = "Lagnesh Dhana Bhava Yoga",
                    sanskritName = "लग्नेश धन भाव योग",
                    category = YogaCategory.DHANA_YOGA,
                    planets = listOf(lagnesh),
                    houses = listOf(2),
                    description = "Ascendant lord ${lagnesh.displayName} in 2nd house of wealth",
                    effects = "Natural wealth accumulator, good speech, family-oriented, food connoisseur, valuable collections, self-earned wealth, eye for beauty, musical/poetic talents possible",
                    strength = YogaHelpers.strengthFromPercentage(strength),
                    strengthPercentage = strength,
                    isAuspicious = true,
                    activationPeriod = "${lagnesh.displayName} dasha for wealth accumulation",
                    cancellationFactors = listOf("2nd also maraka - moderate speech", "Benefic aspects enhance eloquence")
                ))
            }
            3 -> {
                // Lagnesh in 3rd - Courage and siblings
                val strength = 65.0

                yogas.add(Yoga(
                    name = "Lagnesh Sahaja Bhava Yoga",
                    sanskritName = "लग्नेश सहज भाव योग",
                    category = YogaCategory.SPECIAL_YOGA,
                    planets = listOf(lagnesh),
                    houses = listOf(3),
                    description = "Ascendant lord ${lagnesh.displayName} in 3rd house of courage",
                    effects = "Brave and courageous, communication skills, artistic talents, relationship with siblings defines life, short travels, writing/media abilities, self-made through effort",
                    strength = YogaHelpers.strengthFromPercentage(strength),
                    strengthPercentage = strength,
                    isAuspicious = true,
                    activationPeriod = "${lagnesh.displayName} dasha for ventures requiring courage",
                    cancellationFactors = listOf("Upachaya house gives improving results", "Good for creative pursuits")
                ))
            }
        }

        // Check for Lagnesh dignity yogas
        if (YogaHelpers.isExalted(lagneshPos)) {
            yogas.add(Yoga(
                name = "Uccha Lagnesh Yoga",
                sanskritName = "उच्च लग्नेश योग",
                category = YogaCategory.SPECIAL_YOGA,
                planets = listOf(lagnesh),
                houses = listOf(lagneshPos.house),
                description = "Ascendant lord ${lagnesh.displayName} is exalted in house ${lagneshPos.house}",
                effects = "Highly auspicious, strong personality, natural leadership, health and vitality, recognition and honor, ability to achieve goals, respected in society, noble character",
                strength = YogaStrength.VERY_STRONG,
                strengthPercentage = 90.0,
                isAuspicious = true,
                activationPeriod = "${lagnesh.displayName} dasha brings excellent results",
                cancellationFactors = listOf("One of the best combinations", "Enhances all house significations")
            ))
        }

        if (YogaHelpers.isDebilitated(lagneshPos) && !YogaHelpers.hasNeechaBhanga(lagneshPos, chart)) {
            yogas.add(Yoga(
                name = "Neecha Lagnesh Yoga",
                sanskritName = "नीच लग्नेश योग",
                category = YogaCategory.NEGATIVE_YOGA,
                planets = listOf(lagnesh),
                houses = listOf(lagneshPos.house),
                description = "Ascendant lord ${lagnesh.displayName} is debilitated without cancellation",
                effects = "Health challenges possible, lack of confidence needs addressing, recognition comes late, must work harder for results, character building through difficulties",
                strength = YogaStrength.WEAK,
                strengthPercentage = 35.0,
                isAuspicious = false,
                activationPeriod = "${lagnesh.displayName} dasha requires careful navigation",
                cancellationFactors = listOf("Check for Neecha Bhanga", "Aspects can mitigate", "Transit Jupiter helps")
            ))
        }

        // Vargottama Lagnesh
        if (YogaHelpers.isVargottama(lagneshPos, chart)) {
            yogas.add(Yoga(
                name = "Vargottama Lagnesh Yoga",
                sanskritName = "वर्गोत्तम लग्नेश योग",
                category = YogaCategory.SPECIAL_YOGA,
                planets = listOf(lagnesh),
                houses = listOf(lagneshPos.house),
                description = "Ascendant lord ${lagnesh.displayName} is Vargottama (same sign in Rashi and Navamsha)",
                effects = "Strengthened self-expression, promises of rashi chart fulfilled in life, confident approach, true to one's nature, inner and outer alignment, authentic personality",
                strength = YogaStrength.STRONG,
                strengthPercentage = 80.0,
                isAuspicious = true,
                activationPeriod = "${lagnesh.displayName} dasha delivers promised results",
                cancellationFactors = listOf("Very supportive for overall chart", "Enhances all lagnesh significations")
            ))
        }

        return yogas
    }

    // ==================== LAGNA ADHI YOGAS ====================

    private fun evaluateLagnaAdhiYogas(chart: VedicChart, houseLords: Map<Int, Planet>): List<Yoga> {
        val yogas = mutableListOf<Yoga>()

        // Check benefics in kendras from Lagna
        val naturalBenefics = listOf(Planet.JUPITER, Planet.VENUS, Planet.MERCURY, Planet.MOON)
        val kendraHouses = listOf(1, 4, 7, 10)

        val beneficsInKendras = chart.planetPositions.filter { pos ->
            pos.planet in naturalBenefics && pos.house in kendraHouses
        }

        if (beneficsInKendras.size >= 3) {
            val strength = 75.0 + (beneficsInKendras.size - 3) * 5

            yogas.add(Yoga(
                name = "Lagna Adhi Yoga",
                sanskritName = "लग्न आधि योग",
                category = YogaCategory.SPECIAL_YOGA,
                planets = beneficsInKendras.map { it.planet },
                houses = beneficsInKendras.map { it.house }.distinct(),
                description = "${beneficsInKendras.size} natural benefics in kendras: ${beneficsInKendras.joinToString { "${it.planet.displayName} in ${it.house}" }}",
                effects = "Protected life, benefic influences on personality, good fortune, helpful people surround, moral character, respected in society, wisdom and prosperity",
                strength = YogaHelpers.strengthFromPercentage(strength),
                strengthPercentage = strength,
                isAuspicious = true,
                activationPeriod = "Throughout life, enhanced in benefic dashas",
                cancellationFactors = listOf("Strong foundation for success", "Malefic aspects reduce but don't cancel")
            ))
        }

        // Subha Yoga - Benefics in 6th, 7th, 8th from Moon
        val moonPos = chart.planetPositions.find { it.planet == Planet.MOON }
        if (moonPos != null) {
            val relativeHouses = listOf(6, 7, 8)
            val beneficsFromMoon = chart.planetPositions.filter { pos ->
                pos.planet in naturalBenefics && pos.planet != Planet.MOON &&
                YogaHelpers.getHouseFromMoon(pos.house, moonPos.house) in relativeHouses
            }

            if (beneficsFromMoon.size >= 2) {
                val strength = 70.0 + (beneficsFromMoon.size - 2) * 5

                yogas.add(Yoga(
                    name = "Chandra Adhi Yoga",
                    sanskritName = "चन्द्र आधि योग",
                    category = YogaCategory.SPECIAL_YOGA,
                    planets = beneficsFromMoon.map { it.planet } + Planet.MOON,
                    houses = beneficsFromMoon.map { it.house }.distinct(),
                    description = "${beneficsFromMoon.size} benefics in 6th/7th/8th from Moon forming Adhi Yoga",
                    effects = "Leadership qualities, commander or minister position, defeat of enemies through wisdom, wealth and conveyances, long life, fame and recognition",
                    strength = YogaHelpers.strengthFromPercentage(strength),
                    strengthPercentage = strength,
                    isAuspicious = true,
                    activationPeriod = "Moon dasha and benefic planet periods",
                    cancellationFactors = listOf("Classical yoga for authority", "More benefics strengthen results")
                ))
            }
        }

        return yogas
    }

    // ==================== LAGNA KARTARI YOGAS ====================

    private fun evaluateLagnaKartariYogas(chart: VedicChart): List<Yoga> {
        val yogas = mutableListOf<Yoga>()

        val naturalMalefics = listOf(Planet.SUN, Planet.MARS, Planet.SATURN, Planet.RAHU, Planet.KETU)
        val naturalBenefics = listOf(Planet.JUPITER, Planet.VENUS, Planet.MERCURY, Planet.MOON)

        val planetsIn12 = chart.planetPositions.filter { it.house == 12 }.map { it.planet }
        val planetsIn2 = chart.planetPositions.filter { it.house == 2 }.map { it.planet }

        // Subhakartari Yoga - benefics hemming the lagna
        val beneficsIn12 = planetsIn12.filter { it in naturalBenefics }
        val beneficsIn2 = planetsIn2.filter { it in naturalBenefics }

        if (beneficsIn12.isNotEmpty() && beneficsIn2.isNotEmpty()) {
            val strength = 75.0

            yogas.add(Yoga(
                name = "Subhakartari Lagna Yoga",
                sanskritName = "शुभकर्तरी लग्न योग",
                category = YogaCategory.SPECIAL_YOGA,
                planets = beneficsIn12 + beneficsIn2,
                houses = listOf(12, 2),
                description = "Lagna hemmed by benefics - ${beneficsIn12.joinToString { it.displayName }} in 12th, ${beneficsIn2.joinToString { it.displayName }} in 2nd",
                effects = "Protected personality, beneficial circumstances, good reputation, helped by others, positive environment, fortunate beginnings, auspicious events",
                strength = YogaHelpers.strengthFromPercentage(strength),
                strengthPercentage = strength,
                isAuspicious = true,
                activationPeriod = "Throughout life, especially in hemming planets' dashas",
                cancellationFactors = listOf("Very protective yoga", "Malefics in same houses reduce effect")
            ))
        }

        // Papakartari Yoga - malefics hemming the lagna
        val maleficsIn12 = planetsIn12.filter { it in naturalMalefics }
        val maleficsIn2 = planetsIn2.filter { it in naturalMalefics }

        if (maleficsIn12.isNotEmpty() && maleficsIn2.isNotEmpty() && beneficsIn12.isEmpty() && beneficsIn2.isEmpty()) {
            val cancellations = mutableListOf<String>()
            val lagnaPlanets = chart.planetPositions.filter { it.house == 1 }
            if (lagnaPlanets.any { it.planet == Planet.JUPITER }) {
                cancellations.add("Jupiter in lagna provides protection")
            }

            val strength = if (cancellations.isNotEmpty()) 45.0 else 35.0

            yogas.add(Yoga(
                name = "Papakartari Lagna Yoga",
                sanskritName = "पापकर्तरी लग्न योग",
                category = YogaCategory.NEGATIVE_YOGA,
                planets = maleficsIn12 + maleficsIn2,
                houses = listOf(12, 2),
                description = "Lagna hemmed by malefics - ${maleficsIn12.joinToString { it.displayName }} in 12th, ${maleficsIn2.joinToString { it.displayName }} in 2nd",
                effects = "Obstacles in self-expression, difficult start in life, need to overcome environmental challenges, building resilience, restricted opportunities initially",
                strength = YogaHelpers.strengthFromPercentage(strength),
                strengthPercentage = strength,
                isAuspicious = false,
                activationPeriod = "Early life and malefic planet dashas",
                cancellationFactors = cancellations.ifEmpty { listOf("Jupiter aspects provide relief", "Strong lagna lord overcomes", "Spiritual practice helps") }
            ))
        }

        return yogas
    }

    // ==================== ASCENDANT SIGN SPECIFIC YOGAS ====================

    private fun evaluateAscendantSpecificYogas(
        chart: VedicChart,
        houseLords: Map<Int, Planet>,
        ascendantSign: ZodiacSign
    ): List<Yoga> {
        val yogas = mutableListOf<Yoga>()

        // Sign-specific yogakarakas and special combinations
        when (ascendantSign) {
            ZodiacSign.ARIES -> {
                // Mars-Jupiter combination for Aries
                val marsPos = chart.planetPositions.find { it.planet == Planet.MARS }
                val jupiterPos = chart.planetPositions.find { it.planet == Planet.JUPITER }

                if (marsPos != null && jupiterPos != null && YogaHelpers.areConjunct(marsPos, jupiterPos)) {
                    yogas.add(Yoga(
                        name = "Mesha Dharma Yoga",
                        sanskritName = "मेष धर्म योग",
                        category = YogaCategory.RAJA_YOGA,
                        planets = listOf(Planet.MARS, Planet.JUPITER),
                        houses = listOf(marsPos.house),
                        description = "For Aries ascendant: Mars (1st, 8th lord) conjunct Jupiter (9th, 12th lord)",
                        effects = "Dharmic warrior nature, righteous action brings success, leadership in spiritual/educational matters, physical strength combined with wisdom, foreign travel for dharma",
                        strength = YogaStrength.STRONG,
                        strengthPercentage = 80.0,
                        isAuspicious = true,
                        activationPeriod = "Mars-Jupiter and Jupiter-Mars dashas",
                        cancellationFactors = listOf("Most effective in kendras/trikonas", "Both planets benefit from conjunction")
                    ))
                }

                // Sun exalted for Aries - 5th lord exalted
                val sunPos = chart.planetPositions.find { it.planet == Planet.SUN }
                if (sunPos != null && YogaHelpers.isExalted(sunPos)) {
                    yogas.add(Yoga(
                        name = "Mesha Putra Yoga",
                        sanskritName = "मेष पुत्र योग",
                        category = YogaCategory.SPECIAL_YOGA,
                        planets = listOf(Planet.SUN),
                        houses = listOf(sunPos.house),
                        description = "For Aries ascendant: Sun (5th lord) exalted in Aries",
                        effects = "Brilliant children, creative intelligence, government favor, fame through progeny or creative works, authoritative personality, leadership in education",
                        strength = YogaStrength.VERY_STRONG,
                        strengthPercentage = 85.0,
                        isAuspicious = true,
                        activationPeriod = "Sun dasha for creative and governmental success",
                        cancellationFactors = listOf("Exalted trikona lord", "Excellent for speculation and politics")
                    ))
                }
            }

            ZodiacSign.TAURUS -> {
                // Saturn-Venus combination for Taurus - yogakaraka
                val saturnPos = chart.planetPositions.find { it.planet == Planet.SATURN }
                val venusPos = chart.planetPositions.find { it.planet == Planet.VENUS }

                if (saturnPos != null && saturnPos.house in listOf(1, 4, 5, 7, 9, 10)) {
                    yogas.add(Yoga(
                        name = "Vrishabha Yogakaraka Yoga",
                        sanskritName = "वृषभ योगकारक योग",
                        category = YogaCategory.RAJA_YOGA,
                        planets = listOf(Planet.SATURN),
                        houses = listOf(saturnPos.house),
                        description = "For Taurus ascendant: Saturn (9th, 10th lord - Yogakaraka) in kendra/trikona",
                        effects = "Excellent for career and fortune, patient rise to success, stable wealth, real estate gains, government positions, authority in later life, disciplined nature brings rewards",
                        strength = YogaStrength.VERY_STRONG,
                        strengthPercentage = 85.0,
                        isAuspicious = true,
                        activationPeriod = "Saturn dasha brings significant success",
                        cancellationFactors = listOf("Saturn is the supreme benefic for Taurus", "Best results after 36 years")
                    ))
                }

                // Moon in 2nd - 3rd lord in 2nd
                val moonPos = chart.planetPositions.find { it.planet == Planet.MOON }
                if (moonPos != null && moonPos.house == 2 && (YogaHelpers.isExalted(moonPos) || YogaHelpers.isInOwnSign(moonPos))) {
                    yogas.add(Yoga(
                        name = "Vrishabha Dhana Yoga",
                        sanskritName = "वृषभ धन योग",
                        category = YogaCategory.DHANA_YOGA,
                        planets = listOf(Planet.MOON),
                        houses = listOf(2),
                        description = "For Taurus ascendant: Moon (3rd lord) exalted/own sign in 2nd house",
                        effects = "Wealth through communication, beautiful speech, family prosperity, artistic income, food-related gains, emotional connection to wealth",
                        strength = YogaStrength.STRONG,
                        strengthPercentage = 75.0,
                        isAuspicious = true,
                        activationPeriod = "Moon dasha for financial growth",
                        cancellationFactors = listOf("3rd lord in 2nd can be mixed", "Dignity makes it positive")
                    ))
                }
            }

            ZodiacSign.GEMINI -> {
                // Venus-Saturn combination for Gemini
                val venusPos = chart.planetPositions.find { it.planet == Planet.VENUS }
                val saturnPos = chart.planetPositions.find { it.planet == Planet.SATURN }

                if (venusPos != null && saturnPos != null && YogaHelpers.areConjunct(venusPos, saturnPos)) {
                    yogas.add(Yoga(
                        name = "Mithuna Raja Yoga",
                        sanskritName = "मिथुन राज योग",
                        category = YogaCategory.RAJA_YOGA,
                        planets = listOf(Planet.VENUS, Planet.SATURN),
                        houses = listOf(venusPos.house),
                        description = "For Gemini ascendant: Venus (5th, 12th lord) conjunct Saturn (8th, 9th lord)",
                        effects = "Artistic discipline, creative success through persistence, foreign connections for career, research abilities, long-term investments pay off, occult knowledge",
                        strength = YogaStrength.STRONG,
                        strengthPercentage = 75.0,
                        isAuspicious = true,
                        activationPeriod = "Venus-Saturn and Saturn-Venus periods",
                        cancellationFactors = listOf("5th-9th lord combination", "Better in benefic houses")
                    ))
                }

                // Mercury strong for Gemini - lagnesh dignity
                val mercuryPos = chart.planetPositions.find { it.planet == Planet.MERCURY }
                if (mercuryPos != null && (YogaHelpers.isExalted(mercuryPos) || YogaHelpers.isInOwnSign(mercuryPos))) {
                    yogas.add(Yoga(
                        name = "Mithuna Budha Yoga",
                        sanskritName = "मिथुन बुध योग",
                        category = YogaCategory.SPECIAL_YOGA,
                        planets = listOf(Planet.MERCURY),
                        houses = listOf(mercuryPos.house),
                        description = "For Gemini ascendant: Mercury (1st, 4th lord) in dignity",
                        effects = "Exceptional intellect, writing/communication mastery, property gains, educational achievements, business acumen, adaptable personality, multiple talents",
                        strength = YogaStrength.VERY_STRONG,
                        strengthPercentage = 85.0,
                        isAuspicious = true,
                        activationPeriod = "Mercury dasha for intellectual and material success",
                        cancellationFactors = listOf("Strong lagnesh-4th lord combination", "Excellent for writers/teachers")
                    ))
                }
            }

            ZodiacSign.CANCER -> {
                // Mars-Jupiter for Cancer - Yogakaraka Mars
                val marsPos = chart.planetPositions.find { it.planet == Planet.MARS }
                if (marsPos != null && marsPos.house in listOf(1, 4, 5, 7, 9, 10)) {
                    yogas.add(Yoga(
                        name = "Karka Yogakaraka Yoga",
                        sanskritName = "कर्क योगकारक योग",
                        category = YogaCategory.RAJA_YOGA,
                        planets = listOf(Planet.MARS),
                        houses = listOf(marsPos.house),
                        description = "For Cancer ascendant: Mars (5th, 10th lord - Yogakaraka) in kendra/trikona",
                        effects = "Action-oriented success, property gains, children bring fortune, technical/engineering success, courage in career, government positions, land ownership",
                        strength = YogaStrength.VERY_STRONG,
                        strengthPercentage = 85.0,
                        isAuspicious = true,
                        activationPeriod = "Mars dasha is highly beneficial",
                        cancellationFactors = listOf("Mars is supreme benefic for Cancer", "Real estate and career success")
                    ))
                }

                // Jupiter in 9th for Cancer - 6th, 9th lord
                val jupiterPos = chart.planetPositions.find { it.planet == Planet.JUPITER }
                if (jupiterPos != null && jupiterPos.house == 9) {
                    yogas.add(Yoga(
                        name = "Karka Bhagya Yoga",
                        sanskritName = "कर्क भाग्य योग",
                        category = YogaCategory.SPECIAL_YOGA,
                        planets = listOf(Planet.JUPITER),
                        houses = listOf(9),
                        description = "For Cancer ascendant: Jupiter (6th, 9th lord) in own 9th house Pisces",
                        effects = "Dharmic fortune, victory over enemies through righteousness, father's guidance, overseas fortune, teaching abilities, philosophical wisdom, protective grace",
                        strength = YogaStrength.STRONG,
                        strengthPercentage = 80.0,
                        isAuspicious = true,
                        activationPeriod = "Jupiter dasha for spiritual and material growth",
                        cancellationFactors = listOf("9th lord in 9th overcomes 6th lordship", "Excellent for guidance roles")
                    ))
                }
            }

            ZodiacSign.LEO -> {
                // Mars-Jupiter combination for Leo
                val marsPos = chart.planetPositions.find { it.planet == Planet.MARS }
                val jupiterPos = chart.planetPositions.find { it.planet == Planet.JUPITER }

                if (marsPos != null && jupiterPos != null && YogaHelpers.areConjunct(marsPos, jupiterPos)) {
                    yogas.add(Yoga(
                        name = "Simha Dharma Karma Yoga",
                        sanskritName = "सिंह धर्म कर्म योग",
                        category = YogaCategory.RAJA_YOGA,
                        planets = listOf(Planet.MARS, Planet.JUPITER),
                        houses = listOf(marsPos.house),
                        description = "For Leo ascendant: Mars (4th, 9th lord) conjunct Jupiter (5th, 8th lord)",
                        effects = "Royal authority through righteous action, property and fortune combined, intelligent children, transformative wisdom, ancestral blessings, leadership in dharma",
                        strength = YogaStrength.VERY_STRONG,
                        strengthPercentage = 85.0,
                        isAuspicious = true,
                        activationPeriod = "Mars-Jupiter or Jupiter-Mars periods for peak success",
                        cancellationFactors = listOf("Powerful trikona lords combination", "Jupiter reduces malefic 8th lordship")
                    ))
                }

                // Sun strong for Leo
                val sunPos = chart.planetPositions.find { it.planet == Planet.SUN }
                if (sunPos != null && (YogaHelpers.isExalted(sunPos) || YogaHelpers.isInOwnSign(sunPos))) {
                    yogas.add(Yoga(
                        name = "Simha Surya Yoga",
                        sanskritName = "सिंह सूर्य योग",
                        category = YogaCategory.SPECIAL_YOGA,
                        planets = listOf(Planet.SUN),
                        houses = listOf(sunPos.house),
                        description = "For Leo ascendant: Sun (lagnesh) in dignity - exalted or in Leo",
                        effects = "Natural authority, government favor, leadership positions, father's blessings, strong constitution, recognized personality, royal treatment, administrative success",
                        strength = YogaStrength.VERY_STRONG,
                        strengthPercentage = 90.0,
                        isAuspicious = true,
                        activationPeriod = "Sun dasha brings recognition and authority",
                        cancellationFactors = listOf("Strongest lagnesh placement", "Commands natural respect")
                    ))
                }
            }

            ZodiacSign.VIRGO -> {
                // Venus for Virgo - 2nd, 9th lord
                val venusPos = chart.planetPositions.find { it.planet == Planet.VENUS }
                if (venusPos != null && venusPos.house in listOf(1, 4, 5, 7, 9, 10)) {
                    yogas.add(Yoga(
                        name = "Kanya Lakshmi Yoga",
                        sanskritName = "कन्या लक्ष्मी योग",
                        category = YogaCategory.DHANA_YOGA,
                        planets = listOf(Planet.VENUS),
                        houses = listOf(venusPos.house),
                        description = "For Virgo ascendant: Venus (2nd, 9th lord) in kendra/trikona",
                        effects = "Wealth through dharma, beautiful speech brings fortune, artistic income, luxurious life, fortunate partnerships, spouse brings luck, refined tastes rewarded",
                        strength = YogaStrength.VERY_STRONG,
                        strengthPercentage = 85.0,
                        isAuspicious = true,
                        activationPeriod = "Venus dasha brings wealth and fortune",
                        cancellationFactors = listOf("Venus rules both dhana and bhagya houses", "Excellent for Virgo natives")
                    ))
                }

                // Mercury dignity for Virgo - lagnesh exalted
                val mercuryPos = chart.planetPositions.find { it.planet == Planet.MERCURY }
                if (mercuryPos != null && YogaHelpers.isExalted(mercuryPos)) {
                    yogas.add(Yoga(
                        name = "Kanya Uccha Lagnesh Yoga",
                        sanskritName = "कन्या उच्च लग्नेश योग",
                        category = YogaCategory.SPECIAL_YOGA,
                        planets = listOf(Planet.MERCURY),
                        houses = listOf(mercuryPos.house),
                        description = "For Virgo ascendant: Mercury (1st, 10th lord) exalted in Virgo (1st house)",
                        effects = "Exceptional analytical abilities, perfectionist nature succeeds, career through intellect, health consciousness, detail-oriented success, business acumen, advisory roles",
                        strength = YogaStrength.EXTREMELY_STRONG,
                        strengthPercentage = 95.0,
                        isAuspicious = true,
                        activationPeriod = "Mercury dasha brings life-defining success",
                        cancellationFactors = listOf("Lagnesh exalted in lagna is extremely powerful", "Both self and career signified")
                    ))
                }
            }

            ZodiacSign.LIBRA -> {
                // Saturn for Libra - Yogakaraka 4th, 5th lord
                val saturnPos = chart.planetPositions.find { it.planet == Planet.SATURN }
                if (saturnPos != null && saturnPos.house in listOf(1, 4, 5, 7, 9, 10)) {
                    yogas.add(Yoga(
                        name = "Tula Yogakaraka Yoga",
                        sanskritName = "तुला योगकारक योग",
                        category = YogaCategory.RAJA_YOGA,
                        planets = listOf(Planet.SATURN),
                        houses = listOf(saturnPos.house),
                        description = "For Libra ascendant: Saturn (4th, 5th lord - Yogakaraka) in kendra/trikona",
                        effects = "Property gains, intelligent approach to life, happiness through discipline, children after efforts, technical education, lasting achievements, patient success",
                        strength = YogaStrength.VERY_STRONG,
                        strengthPercentage = 85.0,
                        isAuspicious = true,
                        activationPeriod = "Saturn dasha is highly beneficial",
                        cancellationFactors = listOf("Saturn is supreme benefic for Libra", "Excellent for property and education")
                    ))
                }

                // Moon for Libra - 10th lord
                val moonPos = chart.planetPositions.find { it.planet == Planet.MOON }
                if (moonPos != null && moonPos.house == 10 && !YogaHelpers.isDebilitated(moonPos)) {
                    yogas.add(Yoga(
                        name = "Tula Karma Yoga",
                        sanskritName = "तुला कर्म योग",
                        category = YogaCategory.RAJA_YOGA,
                        planets = listOf(Planet.MOON),
                        houses = listOf(10),
                        description = "For Libra ascendant: Moon (10th lord) in 10th house Cancer",
                        effects = "Public popularity, career involving masses, nurturing profession, emotional connection to work, fluctuating but ultimately successful career, hospitality/care-giving success",
                        strength = YogaStrength.STRONG,
                        strengthPercentage = 80.0,
                        isAuspicious = true,
                        activationPeriod = "Moon dasha brings career prominence",
                        cancellationFactors = listOf("Moon own sign in 10th is excellent", "Public-facing roles favored")
                    ))
                }
            }

            ZodiacSign.SCORPIO -> {
                // Jupiter-Moon for Scorpio
                val jupiterPos = chart.planetPositions.find { it.planet == Planet.JUPITER }
                val moonPos = chart.planetPositions.find { it.planet == Planet.MOON }

                if (jupiterPos != null && jupiterPos.house in listOf(1, 4, 5, 7, 9, 10)) {
                    yogas.add(Yoga(
                        name = "Vrishchika Dhana Yoga",
                        sanskritName = "वृश्चिक धन योग",
                        category = YogaCategory.DHANA_YOGA,
                        planets = listOf(Planet.JUPITER),
                        houses = listOf(jupiterPos.house),
                        description = "For Scorpio ascendant: Jupiter (2nd, 5th lord) in kendra/trikona",
                        effects = "Wealth through intelligence, speculative gains, children bring fortune, wisdom in speech, teaching abilities, optimistic approach to finances, multiple income streams",
                        strength = YogaStrength.VERY_STRONG,
                        strengthPercentage = 85.0,
                        isAuspicious = true,
                        activationPeriod = "Jupiter dasha brings wealth and children",
                        cancellationFactors = listOf("Jupiter rules dhana and putra houses", "Excellent for financial growth")
                    ))
                }

                // Moon for Scorpio - 9th lord
                if (moonPos != null && moonPos.house == 9 && !YogaHelpers.isDebilitated(moonPos)) {
                    yogas.add(Yoga(
                        name = "Vrishchika Bhagya Yoga",
                        sanskritName = "वृश्चिक भाग्य योग",
                        category = YogaCategory.SPECIAL_YOGA,
                        planets = listOf(Planet.MOON),
                        houses = listOf(9),
                        description = "For Scorpio ascendant: Moon (9th lord) in 9th house Cancer (own sign)",
                        effects = "Emotionally connected to dharma, mother as guru, intuitive wisdom, fortunate for overseas travel, pilgrimage brings peace, nurturing spiritual nature",
                        strength = YogaStrength.STRONG,
                        strengthPercentage = 80.0,
                        isAuspicious = true,
                        activationPeriod = "Moon dasha for spiritual and fortunate experiences",
                        cancellationFactors = listOf("9th lord in 9th in own sign", "Excellent for spiritual growth")
                    ))
                }
            }

            ZodiacSign.SAGITTARIUS -> {
                // Sun-Mercury for Sagittarius
                val sunPos = chart.planetPositions.find { it.planet == Planet.SUN }
                val mercuryPos = chart.planetPositions.find { it.planet == Planet.MERCURY }

                if (sunPos != null && sunPos.house in listOf(1, 4, 5, 7, 9, 10)) {
                    yogas.add(Yoga(
                        name = "Dhanu Bhagya Yoga",
                        sanskritName = "धनु भाग्य योग",
                        category = YogaCategory.RAJA_YOGA,
                        planets = listOf(Planet.SUN),
                        houses = listOf(sunPos.house),
                        description = "For Sagittarius ascendant: Sun (9th lord) in kendra/trikona",
                        effects = "Father's blessings, government connections, dharmic authority, leadership in religious/educational institutions, foreign fortune, righteous reputation",
                        strength = YogaStrength.STRONG,
                        strengthPercentage = 80.0,
                        isAuspicious = true,
                        activationPeriod = "Sun dasha brings fortune and recognition",
                        cancellationFactors = listOf("Sun rules the bhagya house", "Excellent for authority in dharmic fields")
                    ))
                }

                // Mercury for Sagittarius - 7th, 10th lord
                if (mercuryPos != null && mercuryPos.house == 10) {
                    yogas.add(Yoga(
                        name = "Dhanu Karma Yoga",
                        sanskritName = "धनु कर्म योग",
                        category = YogaCategory.RAJA_YOGA,
                        planets = listOf(Planet.MERCURY),
                        houses = listOf(10),
                        description = "For Sagittarius ascendant: Mercury (7th, 10th lord) in 10th house Virgo (exalted)",
                        effects = "Business success through partnerships, communication-based career, writing/teaching profession, intellectual work, commercial partnerships, analytical career success",
                        strength = YogaStrength.VERY_STRONG,
                        strengthPercentage = 85.0,
                        isAuspicious = true,
                        activationPeriod = "Mercury dasha for career and partnership success",
                        cancellationFactors = listOf("7th and 10th lord exalted in 10th", "Powerful career combination")
                    ))
                }
            }

            ZodiacSign.CAPRICORN -> {
                // Venus for Capricorn - 5th, 10th lord
                val venusPos = chart.planetPositions.find { it.planet == Planet.VENUS }
                if (venusPos != null && venusPos.house in listOf(1, 4, 5, 7, 9, 10)) {
                    yogas.add(Yoga(
                        name = "Makara Raja Yoga",
                        sanskritName = "मकर राज योग",
                        category = YogaCategory.RAJA_YOGA,
                        planets = listOf(Planet.VENUS),
                        houses = listOf(venusPos.house),
                        description = "For Capricorn ascendant: Venus (5th, 10th lord) in kendra/trikona",
                        effects = "Career through arts/entertainment, creative profession, children support career, artistic authority, luxury industry success, beautiful work environment, diplomatic career",
                        strength = YogaStrength.VERY_STRONG,
                        strengthPercentage = 85.0,
                        isAuspicious = true,
                        activationPeriod = "Venus dasha brings career and creative success",
                        cancellationFactors = listOf("Venus rules trikona and kendra", "Best for arts and diplomacy")
                    ))
                }

                // Mars for Capricorn - 4th, 11th lord
                val marsPos = chart.planetPositions.find { it.planet == Planet.MARS }
                if (marsPos != null && marsPos.house == 1 && YogaHelpers.isExalted(marsPos)) {
                    yogas.add(Yoga(
                        name = "Makara Ruchaka-Lagna Yoga",
                        sanskritName = "मकर रुचक लग्न योग",
                        category = YogaCategory.MAHAPURUSHA_YOGA,
                        planets = listOf(Planet.MARS),
                        houses = listOf(1),
                        description = "For Capricorn ascendant: Mars (4th, 11th lord) exalted in lagna",
                        effects = "Warrior nature, property gains, network brings wealth, physical strength, real estate success, military/police success, fearless personality, gains through action",
                        strength = YogaStrength.EXTREMELY_STRONG,
                        strengthPercentage = 95.0,
                        isAuspicious = true,
                        activationPeriod = "Mars dasha brings property and gains",
                        cancellationFactors = listOf("Exalted Mars in lagna forms Ruchaka", "Combined with 4th-11th lordship very powerful")
                    ))
                }
            }

            ZodiacSign.AQUARIUS -> {
                // Venus for Aquarius - 4th, 9th lord
                val venusPos = chart.planetPositions.find { it.planet == Planet.VENUS }
                if (venusPos != null && venusPos.house in listOf(1, 4, 5, 7, 9, 10)) {
                    yogas.add(Yoga(
                        name = "Kumbha Lakshmi Yoga",
                        sanskritName = "कुम्भ लक्ष्मी योग",
                        category = YogaCategory.RAJA_YOGA,
                        planets = listOf(Planet.VENUS),
                        houses = listOf(venusPos.house),
                        description = "For Aquarius ascendant: Venus (4th, 9th lord) in kendra/trikona",
                        effects = "Fortunate home life, property through luck, artistic education, mother brings fortune, vehicles and comforts, dharmic happiness, refined lifestyle",
                        strength = YogaStrength.VERY_STRONG,
                        strengthPercentage = 85.0,
                        isAuspicious = true,
                        activationPeriod = "Venus dasha brings property and fortune",
                        cancellationFactors = listOf("Venus rules kendra and trikona", "Excellent for happiness and dharma")
                    ))
                }

                // Mars for Aquarius - 3rd, 10th lord
                val marsPos = chart.planetPositions.find { it.planet == Planet.MARS }
                if (marsPos != null && marsPos.house == 10) {
                    yogas.add(Yoga(
                        name = "Kumbha Karma Yoga",
                        sanskritName = "कुम्भ कर्म योग",
                        category = YogaCategory.RAJA_YOGA,
                        planets = listOf(Planet.MARS),
                        houses = listOf(10),
                        description = "For Aquarius ascendant: Mars (3rd, 10th lord) in 10th house Scorpio (own sign)",
                        effects = "Courageous career, technical profession, research abilities, brothers help career, action-oriented success, military/engineering success, transformative work",
                        strength = YogaStrength.STRONG,
                        strengthPercentage = 80.0,
                        isAuspicious = true,
                        activationPeriod = "Mars dasha for career advancement",
                        cancellationFactors = listOf("Mars in own sign in 10th", "Good for technical fields")
                    ))
                }
            }

            ZodiacSign.PISCES -> {
                // Mars for Pisces - 2nd, 9th lord
                val marsPos = chart.planetPositions.find { it.planet == Planet.MARS }
                if (marsPos != null && marsPos.house in listOf(1, 4, 5, 7, 9, 10)) {
                    yogas.add(Yoga(
                        name = "Meena Dhana Bhagya Yoga",
                        sanskritName = "मीन धन भाग्य योग",
                        category = YogaCategory.RAJA_YOGA,
                        planets = listOf(Planet.MARS),
                        houses = listOf(marsPos.house),
                        description = "For Pisces ascendant: Mars (2nd, 9th lord) in kendra/trikona",
                        effects = "Wealth through dharma, courageous speech brings fortune, action-oriented success, father's warrior spirit, property and finances combined with luck, assertive fortune",
                        strength = YogaStrength.VERY_STRONG,
                        strengthPercentage = 85.0,
                        isAuspicious = true,
                        activationPeriod = "Mars dasha brings wealth and fortune",
                        cancellationFactors = listOf("Mars rules dhana and bhagya houses", "Excellent for Pisces natives")
                    ))
                }

                // Moon for Pisces - 5th lord
                val moonPos = chart.planetPositions.find { it.planet == Planet.MOON }
                if (moonPos != null && moonPos.house == 5 && !YogaHelpers.isDebilitated(moonPos)) {
                    yogas.add(Yoga(
                        name = "Meena Putra Yoga",
                        sanskritName = "मीन पुत्र योग",
                        category = YogaCategory.SPECIAL_YOGA,
                        planets = listOf(Planet.MOON),
                        houses = listOf(5),
                        description = "For Pisces ascendant: Moon (5th lord) in 5th house Cancer (own sign)",
                        effects = "Intelligent and intuitive children, emotional creativity, motherly love for progeny, nurturing teaching ability, speculative success through intuition, romantic nature",
                        strength = YogaStrength.STRONG,
                        strengthPercentage = 80.0,
                        isAuspicious = true,
                        activationPeriod = "Moon dasha for children and creativity",
                        cancellationFactors = listOf("5th lord in 5th in own sign", "Excellent for progeny")
                    ))
                }
            }
        }

        return yogas
    }

    // ==================== LAGNA STRENGTH YOGAS ====================

    private fun evaluateLagnaStrengthYogas(
        chart: VedicChart,
        houseLords: Map<Int, Planet>,
        ascendantSign: ZodiacSign
    ): List<Yoga> {
        val yogas = mutableListOf<Yoga>()

        // Count planets in lagna
        val planetsInLagna = chart.planetPositions.filter { it.house == 1 }
        if (planetsInLagna.size >= 3) {
            val beneficCount = planetsInLagna.count { it.planet in listOf(Planet.JUPITER, Planet.VENUS, Planet.MERCURY, Planet.MOON) }
            val isPositive = beneficCount >= 2

            val strength = if (isPositive) 70.0 + beneficCount * 5 else 50.0

            yogas.add(Yoga(
                name = if (isPositive) "Graha Sammelan Lagna Yoga" else "Graha Sanghata Lagna Yoga",
                sanskritName = if (isPositive) "ग्रह सम्मेलन लग्न योग" else "ग्रह संघात लग्न योग",
                category = if (isPositive) YogaCategory.SPECIAL_YOGA else YogaCategory.CONJUNCTION_YOGA,
                planets = planetsInLagna.map { it.planet },
                houses = listOf(1),
                description = "${planetsInLagna.size} planets in lagna: ${planetsInLagna.joinToString { it.planet.displayName }}",
                effects = if (isPositive)
                    "Strong personality, multiple talents, recognized individual, self-focused life, diverse abilities, complex character, significant presence"
                else
                    "Complex self-expression, competing inner drives, need to integrate diverse energies, powerful but needs direction, potential for great achievement if focused",
                strength = YogaHelpers.strengthFromPercentage(strength),
                strengthPercentage = strength,
                isAuspicious = isPositive,
                activationPeriod = "Dashas of lagna planets",
                cancellationFactors = listOf("Benefic majority is key", "Lord of lagna's condition matters")
            ))
        }

        // Digbala in Lagna - Jupiter or Mercury
        val jupiterPos = chart.planetPositions.find { it.planet == Planet.JUPITER }
        val mercuryPos = chart.planetPositions.find { it.planet == Planet.MERCURY }

        if (jupiterPos?.house == 1 || mercuryPos?.house == 1) {
            val digbalaplanet = if (jupiterPos?.house == 1) Planet.JUPITER else Planet.MERCURY
            val pos = if (jupiterPos?.house == 1) jupiterPos else mercuryPos!!

            yogas.add(Yoga(
                name = "Lagna Digbala Yoga",
                sanskritName = "लग्न दिग्बल योग",
                category = YogaCategory.SPECIAL_YOGA,
                planets = listOf(digbalaplanet),
                houses = listOf(1),
                description = "${digbalaplanet.displayName} has directional strength (Digbala) in 1st house",
                effects = if (digbalaplanet == Planet.JUPITER)
                    "Wisdom-infused personality, guru-like presence, respected advisor, optimistic nature, ethical character, teaching abilities, blessed by elders"
                else
                    "Intelligent communication, versatile personality, business acumen, youthful appearance, quick-witted, adaptable, commercial success",
                strength = YogaStrength.STRONG,
                strengthPercentage = 80.0,
                isAuspicious = true,
                activationPeriod = "${digbalaplanet.displayName} dasha brings full expression",
                cancellationFactors = listOf("Digbala is powerful positional strength", "Enhances planet's significations greatly")
            ))
        }

        return yogas
    }

    // ==================== BHAVA LAGNA RELATIONSHIP YOGAS ====================

    private fun evaluateBhavaLagnaYogas(chart: VedicChart, houseLords: Map<Int, Planet>): List<Yoga> {
        val yogas = mutableListOf<Yoga>()

        // Check 1-5-9 trikona lord connections (dharma triangle)
        val lord1 = houseLords[1]
        val lord5 = houseLords[5]
        val lord9 = houseLords[9]

        val lord1Pos = if (lord1 != null) chart.planetPositions.find { it.planet == lord1 } else null
        val lord5Pos = if (lord5 != null) chart.planetPositions.find { it.planet == lord5 } else null
        val lord9Pos = if (lord9 != null) chart.planetPositions.find { it.planet == lord9 } else null

        // 1st and 5th lord connection
        if (lord1Pos != null && lord5Pos != null) {
            val connected = YogaHelpers.areConjunct(lord1Pos, lord5Pos) ||
                    YogaHelpers.isAspecting(lord1Pos, lord5Pos) ||
                    YogaHelpers.isAspecting(lord5Pos, lord1Pos)

            if (connected) {
                val isConjunct = YogaHelpers.areConjunct(lord1Pos, lord5Pos)
                val strength = if (isConjunct) 80.0 else 70.0

                yogas.add(Yoga(
                    name = "Lagnesh-Panchamesh Yoga",
                    sanskritName = "लग्नेश पंचमेश योग",
                    category = YogaCategory.RAJA_YOGA,
                    planets = listOfNotNull(lord1, lord5).distinct(),
                    houses = listOf(lord1Pos.house, lord5Pos.house).distinct(),
                    description = "1st lord ${lord1!!.displayName} ${if (isConjunct) "conjunct" else "aspecting"} 5th lord ${lord5!!.displayName}",
                    effects = "Self-expression through creativity, intelligent personality, children reflect native's character, success through intellect, purva punya activation, speculative abilities",
                    strength = YogaHelpers.strengthFromPercentage(strength),
                    strengthPercentage = strength,
                    isAuspicious = true,
                    activationPeriod = "${lord1.displayName} and ${lord5.displayName} dashas",
                    cancellationFactors = listOf("Trikona lords create Raja Yoga", "Enhances both intelligence and self-expression")
                ))
            }
        }

        // 1st and 9th lord connection
        if (lord1Pos != null && lord9Pos != null) {
            val connected = YogaHelpers.areConjunct(lord1Pos, lord9Pos) ||
                    YogaHelpers.isAspecting(lord1Pos, lord9Pos) ||
                    YogaHelpers.isAspecting(lord9Pos, lord1Pos)

            if (connected) {
                val isConjunct = YogaHelpers.areConjunct(lord1Pos, lord9Pos)
                val strength = if (isConjunct) 85.0 else 75.0

                yogas.add(Yoga(
                    name = "Lagnesh-Bhagyesh Yoga",
                    sanskritName = "लग्नेश भाग्येश योग",
                    category = YogaCategory.RAJA_YOGA,
                    planets = listOfNotNull(lord1, lord9).distinct(),
                    houses = listOf(lord1Pos.house, lord9Pos.house).distinct(),
                    description = "1st lord ${lord1!!.displayName} ${if (isConjunct) "conjunct" else "aspecting"} 9th lord ${lord9!!.displayName}",
                    effects = "Fortune follows the native, dharmic personality, father's support, guru's blessings, righteous character brings luck, long-distance travel beneficial, philosophical nature",
                    strength = YogaHelpers.strengthFromPercentage(strength),
                    strengthPercentage = strength,
                    isAuspicious = true,
                    activationPeriod = "${lord1.displayName} and ${lord9.displayName} dashas",
                    cancellationFactors = listOf("Powerful Lakshmi Yoga potential", "1st-9th axis is most fortunate")
                ))
            }
        }

        // Check 1-10 lord connection (karma yoga)
        val lord10 = houseLords[10]
        val lord10Pos = if (lord10 != null) chart.planetPositions.find { it.planet == lord10 } else null

        if (lord1Pos != null && lord10Pos != null) {
            val connected = YogaHelpers.areConjunct(lord1Pos, lord10Pos) ||
                    YogaHelpers.isAspecting(lord1Pos, lord10Pos) ||
                    YogaHelpers.isAspecting(lord10Pos, lord1Pos) ||
                    (lord1Pos.house == 10 || lord10Pos.house == 1)

            if (connected) {
                val strength = 80.0

                yogas.add(Yoga(
                    name = "Lagnesh-Karmesh Yoga",
                    sanskritName = "लग्नेश कर्मेश योग",
                    category = YogaCategory.RAJA_YOGA,
                    planets = listOfNotNull(lord1, lord10).distinct(),
                    houses = listOf(lord1Pos.house, lord10Pos.house).distinct(),
                    description = "1st lord ${lord1!!.displayName} connected with 10th lord ${lord10!!.displayName}",
                    effects = "Career reflects personality, recognized in profession, self-made success, action-oriented, known for one's work, professional identity strong, leadership positions",
                    strength = YogaHelpers.strengthFromPercentage(strength),
                    strengthPercentage = strength,
                    isAuspicious = true,
                    activationPeriod = "${lord1.displayName} and ${lord10.displayName} dashas for career peak",
                    cancellationFactors = listOf("Kendra-Lagna connection", "Very supportive for career success")
                ))
            }
        }

        return yogas
    }

    // ==================== SPECIAL LAGNA COMBINATIONS ====================

    private fun evaluateSpecialLagnaCombinations(
        chart: VedicChart,
        houseLords: Map<Int, Planet>,
        ascendantSign: ZodiacSign
    ): List<Yoga> {
        val yogas = mutableListOf<Yoga>()

        // Shubha Lagna - Benefic in Lagna
        val beneficsInLagna = chart.planetPositions.filter {
            it.house == 1 && it.planet in listOf(Planet.JUPITER, Planet.VENUS, Planet.MERCURY, Planet.MOON)
        }

        if (beneficsInLagna.isNotEmpty()) {
            val jupiterPresent = beneficsInLagna.any { it.planet == Planet.JUPITER }
            val strength = if (jupiterPresent) 85.0 else 70.0

            yogas.add(Yoga(
                name = "Shubha Lagna Yoga",
                sanskritName = "शुभ लग्न योग",
                category = YogaCategory.SPECIAL_YOGA,
                planets = beneficsInLagna.map { it.planet },
                houses = listOf(1),
                description = "Benefic(s) in lagna: ${beneficsInLagna.joinToString { it.planet.displayName }}",
                effects = "Auspicious personality, pleasant appearance, good nature attracts opportunities, helpful disposition, refined character, ease in self-expression, blessed beginnings",
                strength = YogaHelpers.strengthFromPercentage(strength),
                strengthPercentage = strength,
                isAuspicious = true,
                activationPeriod = "Benefic planets' dashas enhance life quality",
                cancellationFactors = listOf("Jupiter in lagna is most beneficial", "Multiple benefics multiply effects")
            ))
        }

        // Malefics in 3, 6, 11 - Upachaya positions favorable for malefics
        val maleficsInUpachaya = chart.planetPositions.filter {
            it.planet in listOf(Planet.SUN, Planet.MARS, Planet.SATURN, Planet.RAHU, Planet.KETU) &&
                    it.house in listOf(3, 6, 11)
        }

        if (maleficsInUpachaya.size >= 2) {
            val strength = 70.0 + (maleficsInUpachaya.size - 2) * 5

            yogas.add(Yoga(
                name = "Papa Upachaya Yoga",
                sanskritName = "पाप उपचय योग",
                category = YogaCategory.SPECIAL_YOGA,
                planets = maleficsInUpachaya.map { it.planet },
                houses = maleficsInUpachaya.map { it.house }.distinct(),
                description = "${maleficsInUpachaya.size} malefics in upachaya houses (3, 6, 11)",
                effects = "Enemies defeated, gains through struggle, courage and competition, overcoming obstacles, improving circumstances through effort, competitive success, strategic mind",
                strength = YogaHelpers.strengthFromPercentage(strength),
                strengthPercentage = strength,
                isAuspicious = true,
                activationPeriod = "Malefic planet dashas bring victory through struggle",
                cancellationFactors = listOf("Malefics do well in upachaya", "Give fighting spirit and material gains")
            ))
        }

        // Kendradhipati Dosha check for benefics owning kendras
        val kendraLords = listOf(1, 4, 7, 10).mapNotNull { houseLords[it] }.distinct()
        val beneficKendraLords = kendraLords.filter { it in listOf(Planet.JUPITER, Planet.VENUS) }

        if (beneficKendraLords.isNotEmpty()) {
            // Check if any benefic kendra lord is afflicted or in dusthana
            for (planet in beneficKendraLords) {
                val pos = chart.planetPositions.find { it.planet == planet }
                if (pos != null && pos.house in listOf(6, 8, 12)) {
                    yogas.add(Yoga(
                        name = "Kendradhipati Dosha",
                        sanskritName = "केन्द्राधिपति दोष",
                        category = YogaCategory.SPECIAL_YOGA,
                        planets = listOf(planet),
                        houses = listOf(pos.house),
                        description = "Benefic ${planet.displayName} as kendra lord placed in dusthana (${pos.house})",
                        effects = "Benefic nature reduced by kendra lordship, mixed results in its significations, good intentions may not fructify fully, needs supportive aspects",
                        strength = YogaStrength.MODERATE,
                        strengthPercentage = 50.0,
                        isAuspicious = false,
                        activationPeriod = "${planet.displayName} dasha gives reduced benefic results",
                        cancellationFactors = listOf("Conjunction with trikona lord cancels dosha", "Own sign or exaltation helps", "Jupiter's aspect mitigates")
                    ))
                }
            }
        }

        return yogas
    }

    // ==================== NAVAMSHA LAGNA YOGAS ====================

    private fun evaluateNavamshaLagnaYogas(
        chart: VedicChart,
        houseLords: Map<Int, Planet>,
        ascendantSign: ZodiacSign
    ): List<Yoga> {
        val yogas = mutableListOf<Yoga>()

        // Check for Pushkara Navamsha rising
        val ascDegree = chart.ascendant % 30
        val pushkaraNavamshas = mapOf(
            ZodiacSign.ARIES to listOf(7, 9),      // 21-23.20, 26.40-30
            ZodiacSign.TAURUS to listOf(3, 5),     // 6.40-10, 13.20-16.40
            ZodiacSign.GEMINI to listOf(1, 6),     // 0-3.20, 16.40-20
            ZodiacSign.CANCER to listOf(1, 3),     // 0-3.20, 6.40-10
            ZodiacSign.LEO to listOf(5, 7),        // 13.20-16.40, 20-23.20
            ZodiacSign.VIRGO to listOf(6, 8),      // 16.40-20, 23.20-26.40
            ZodiacSign.LIBRA to listOf(7, 9),      // 20-23.20, 26.40-30
            ZodiacSign.SCORPIO to listOf(3, 5),    // 6.40-10, 13.20-16.40
            ZodiacSign.SAGITTARIUS to listOf(1, 6), // 0-3.20, 16.40-20
            ZodiacSign.CAPRICORN to listOf(1, 3),  // 0-3.20, 6.40-10
            ZodiacSign.AQUARIUS to listOf(5, 7),   // 13.20-16.40, 20-23.20
            ZodiacSign.PISCES to listOf(6, 8)      // 16.40-20, 23.20-26.40
        )

        val navamshaNumber = ((ascDegree / 3.333333).toInt() % 9) + 1
        val isPushkaraNavamsha = pushkaraNavamshas[ascendantSign]?.contains(navamshaNumber) == true

        if (isPushkaraNavamsha) {
            yogas.add(Yoga(
                name = "Pushkara Navamsha Lagna",
                sanskritName = "पुष्कर नवांश लग्न",
                category = YogaCategory.SPECIAL_YOGA,
                planets = emptyList(),
                houses = listOf(1),
                description = "Ascendant in Pushkara Navamsha - an auspicious division",
                effects = "Nourishing and blessed personality, auspicious beginnings, protected life path, inherent good fortune, spiritual inclinations, ability to nurture others",
                strength = YogaStrength.STRONG,
                strengthPercentage = 75.0,
                isAuspicious = true,
                activationPeriod = "Throughout life, enhances all lagna significations",
                cancellationFactors = listOf("Very auspicious navamsha position", "Enhances chart positivity")
            ))
        }

        // Check if Rashi and Navamsha lagna are same (Vargottama Lagna)
        val navamshaSign = YogaHelpers.getNavamshaSign(chart.ascendant)
        if (ascendantSign == navamshaSign) {
            yogas.add(Yoga(
                name = "Vargottama Lagna",
                sanskritName = "वर्गोत्तम लग्न",
                category = YogaCategory.SPECIAL_YOGA,
                planets = emptyList(),
                houses = listOf(1),
                description = "Ascendant is Vargottama - same sign in Rashi and Navamsha ($ascendantSign)",
                effects = "Strong sense of self, authentic personality, promises of chart fulfilled, inner and outer alignment, consistent character, truthful self-expression, integrity",
                strength = YogaStrength.VERY_STRONG,
                strengthPercentage = 85.0,
                isAuspicious = true,
                activationPeriod = "Throughout life - fundamental strength",
                cancellationFactors = listOf("One of the most strengthening factors", "Confirms chart's promises")
            ))
        }

        return yogas
    }
}
