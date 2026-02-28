package com.astro.vajra.ephemeris.yoga

import com.astro.vajra.core.model.Planet
import com.astro.vajra.core.model.PlanetPosition
import com.astro.vajra.core.model.VedicChart
import com.astro.vajra.core.model.ZodiacSign

/**
 * Sannyasa & Moksha Yoga Evaluator - Renunciation and Spiritual Liberation Combinations
 *
 * This evaluator handles yogas related to spiritual advancement, renunciation,
 * detachment, and ultimate liberation (moksha). These yogas indicate souls
 * inclined toward the spiritual path and higher consciousness.
 *
 * Categories covered:
 * 1. Classical Sannyasa Yogas - Formal renunciation combinations (BPHS Chapter 79)
 * 2. Moksha Yogas - Liberation-oriented combinations
 * 3. Pravrajya Yogas - Ascetic/monastic combinations
 * 4. Spiritual Ketu Yogas - Ketu-based detachment patterns
 * 5. 12th House Yogas - Transcendence and liberation
 * 6. Saturn-Moon Yogas - Detachment and renunciation
 * 7. Multiple Planet Conjunctions for Sannyasa
 * 8. Nakshatra-based Spiritual Yogas
 *
 * Based on classical texts:
 * - Brihat Parasara Hora Shastra (BPHS), Chapters 79-80
 * - Phaladeepika, Chapter 27
 * - Jataka Parijata, Chapter 15
 * - Saravali, Chapter 51
 * - Brihat Jataka, Chapter 15
 *
 * Note: These yogas indicate spiritual inclination. They must be evaluated
 * alongside the overall chart for accurate interpretation.
 *
 * @author AstroVajra
 */
class SannyasaMokshaYogaEvaluator : YogaEvaluator {

    override val category: YogaCategory = YogaCategory.SPECIAL_YOGA

    override fun evaluate(chart: VedicChart): List<Yoga> {
        val yogas = mutableListOf<Yoga>()
        val ascendantSign = ZodiacSign.fromLongitude(chart.ascendant)
        val houseLords = YogaHelpers.getHouseLords(ascendantSign)

        // 1. Classical Sannyasa Yogas
        yogas.addAll(evaluateClassicalSannyasaYogas(chart, houseLords))

        // 2. Moksha Yogas - Liberation combinations
        yogas.addAll(evaluateMokshaYogas(chart, houseLords))

        // 3. Pravrajya Yogas - Ascetic patterns
        yogas.addAll(evaluatePravrajyaYogas(chart, houseLords))

        // 4. Ketu-based Spiritual Yogas
        yogas.addAll(evaluateKetuSpiritualYogas(chart, houseLords))

        // 5. 12th House Spiritual Yogas
        yogas.addAll(evaluate12thHouseYogas(chart, houseLords))

        // 6. Saturn-Moon Detachment Yogas
        yogas.addAll(evaluateSaturnMoonYogas(chart, houseLords))

        // 7. Jupiter Spiritual Yogas
        yogas.addAll(evaluateJupiterSpiritualYogas(chart, houseLords))

        // 8. Neptune-like Transcendence Patterns
        yogas.addAll(evaluateTranscendenceYogas(chart, houseLords))

        return yogas
    }

    // ==================== CLASSICAL SANNYASA YOGAS ====================

    private fun evaluateClassicalSannyasaYogas(
        chart: VedicChart,
        houseLords: Map<Int, Planet>
    ): List<Yoga> {
        val yogas = mutableListOf<Yoga>()

        // Classical Sannyasa Yoga - 4+ planets in one house including 10th lord
        val planetsByHouse = chart.planetPositions.groupBy { it.house }
        val lord10 = houseLords[10]

        for ((house, planets) in planetsByHouse) {
            if (planets.size >= 4) {
                val includes10thLord = planets.any { it.planet == lord10 }
                val planetNames = planets.map { it.planet }

                // Determine the type of sannyasa based on strongest planet
                val sannyasaType = determineSannyasaType(planets)

                if (includes10thLord) {
                    yogas.add(Yoga(
                        name = "Sannyasa Yoga (Classical)",
                        sanskritName = "संन्यास योग",
                        category = YogaCategory.SPECIAL_YOGA,
                        planets = planetNames,
                        houses = listOf(house),
                        description = "${planets.size} planets including 10th lord in house $house",
                        effects = "Strong inclination toward renunciation, may take formal sannyasa, detachment from worldly pursuits, spiritual seeking dominates life, monastic tendencies, philosophical nature",
                        strength = YogaStrength.VERY_STRONG,
                        strengthPercentage = 85.0,
                        isAuspicious = true,
                        activationPeriod = "Especially during Rahu or Saturn dasha",
                        cancellationFactors = listOf("Strong 2nd/7th lords may delay", "Benefic aspects may keep in world while spiritual")
                    ))
                }

                // General stellium with spiritual implications
                if (planets.size >= 4 && !includes10thLord) {
                    yogas.add(Yoga(
                        name = "$sannyasaType Pravrajya Yoga",
                        sanskritName = "${sannyasaType}प्रव्रज्या योग",
                        category = YogaCategory.SPECIAL_YOGA,
                        planets = planetNames,
                        houses = listOf(house),
                        description = "${planets.size} planets conjunct in house $house forming stellium",
                        effects = "Focused life purpose, potential for renunciation, intense concentration of energy in ${getHouseSignifications(house)}, may pursue spiritual path through this house's themes",
                        strength = YogaStrength.STRONG,
                        strengthPercentage = 75.0,
                        isAuspicious = true,
                        activationPeriod = "Dashas of conjunct planets",
                        cancellationFactors = listOf("Material planets may redirect toward worldly achievements", "Benefic influences modify expression")
                    ))
                }
            }
        }

        // Specific Sannyasa Yoga - Moon alone in Navamsha conjunct Saturn, aspected by Mars
        val moonPos = chart.planetPositions.find { it.planet == Planet.MOON }
        val saturnPos = chart.planetPositions.find { it.planet == Planet.SATURN }
        val marsPos = chart.planetPositions.find { it.planet == Planet.MARS }

        if (moonPos != null && saturnPos != null && marsPos != null) {
            val moonSaturnConjunct = YogaHelpers.areConjunct(moonPos, saturnPos)
            val marsAspectsMoon = YogaHelpers.isAspecting(marsPos, moonPos)

            if (moonSaturnConjunct && marsAspectsMoon) {
                yogas.add(Yoga(
                    name = "Parivraja Yoga",
                    sanskritName = "परिव्रज योग",
                    category = YogaCategory.SPECIAL_YOGA,
                    planets = listOf(Planet.MOON, Planet.SATURN, Planet.MARS),
                    houses = listOf(moonPos.house, marsPos.house),
                    description = "Moon conjunct Saturn with Mars aspect - classical wandering ascetic combination",
                    effects = "Wandering nature, detachment from home, ascetic tendencies, may leave family for spiritual pursuits, simple living, pilgrim nature, solitary path",
                    strength = YogaStrength.STRONG,
                    strengthPercentage = 75.0,
                    isAuspicious = true,
                    activationPeriod = "Saturn and Moon dashas trigger renunciation",
                    cancellationFactors = listOf("Jupiter aspect may give teaching role instead", "Venus keeps some worldly attachments")
                ))
            }
        }

        return yogas
    }

    // ==================== MOKSHA YOGAS ====================

    private fun evaluateMokshaYogas(
        chart: VedicChart,
        houseLords: Map<Int, Planet>
    ): List<Yoga> {
        val yogas = mutableListOf<Yoga>()

        val lord1 = houseLords[1]
        val lord4 = houseLords[4]
        val lord5 = houseLords[5]
        val lord9 = houseLords[9]
        val lord10 = houseLords[10]
        val lord12 = houseLords[12]

        // Moksha Trikona Lords connection (4, 8, 12)
        val lord8 = houseLords[8]
        val lord4Pos = if (lord4 != null) chart.planetPositions.find { it.planet == lord4 } else null
        val lord8Pos = if (lord8 != null) chart.planetPositions.find { it.planet == lord8 } else null
        val lord12Pos = if (lord12 != null) chart.planetPositions.find { it.planet == lord12 } else null

        // Check for Moksha Trikona lord connections
        if (lord4Pos != null && lord8Pos != null && lord12Pos != null) {
            val connected = (YogaHelpers.areConjunct(lord4Pos, lord8Pos) ||
                    YogaHelpers.areConjunct(lord4Pos, lord12Pos) ||
                    YogaHelpers.areConjunct(lord8Pos, lord12Pos))

            if (connected) {
                yogas.add(Yoga(
                    name = "Moksha Trikona Yoga",
                    sanskritName = "मोक्ष त्रिकोण योग",
                    category = YogaCategory.SPECIAL_YOGA,
                    planets = listOfNotNull(lord4, lord8, lord12).distinct(),
                    houses = listOf(4, 8, 12),
                    description = "Moksha trikona lords (4th, 8th, 12th) in connection",
                    effects = "Strong spiritual orientation, liberation-seeking soul, deep meditation capacity, understanding of life-death-liberation cycle, potential for enlightenment, yogic practices",
                    strength = YogaStrength.VERY_STRONG,
                    strengthPercentage = 85.0,
                    isAuspicious = true,
                    activationPeriod = "12th lord dasha for spiritual culmination",
                    cancellationFactors = listOf("Must pursue spiritual practice", "Without sadhana, gives anxiety instead")
                ))
            }
        }

        // Ketu in 12th - Natural Moksha indicator
        val ketuPos = chart.planetPositions.find { it.planet == Planet.KETU }
        if (ketuPos?.house == 12) {
            val jupiterPos = chart.planetPositions.find { it.planet == Planet.JUPITER }
            val jupiterAspect = jupiterPos?.let { YogaHelpers.isAspecting(it, ketuPos) } == true

            val strength = if (jupiterAspect) 90.0 else 80.0

            yogas.add(Yoga(
                name = "Ketu Moksha Yoga",
                sanskritName = "केतु मोक्ष योग",
                category = YogaCategory.SPECIAL_YOGA,
                planets = listOfNotNull(Planet.KETU, if (jupiterAspect) Planet.JUPITER else null),
                houses = listOf(12),
                description = "Ketu in 12th house of liberation${if (jupiterAspect) " with Jupiter's aspect" else ""}",
                effects = "Natural inclination toward moksha, past-life spiritual evolution, intuitive wisdom, detachment from material world, meditation comes naturally, may have visions, spiritual inheritance",
                strength = YogaHelpers.strengthFromPercentage(strength),
                strengthPercentage = strength,
                isAuspicious = true,
                activationPeriod = "Ketu dasha intensifies spiritual seeking",
                cancellationFactors = listOf("One of the most spiritual placements", "Material life may feel meaningless without spiritual purpose")
            ))
        }

        // Moksha Yoga - Jupiter in 9th, 10th lord in 5th or trikona
        val jupiterPos = chart.planetPositions.find { it.planet == Planet.JUPITER }
        val lord10Pos = if (lord10 != null) chart.planetPositions.find { it.planet == lord10 } else null

        if (jupiterPos?.house == 9 && lord10Pos != null && lord10Pos.house in listOf(1, 5, 9)) {
            yogas.add(Yoga(
                name = "Dharma Moksha Yoga",
                sanskritName = "धर्म मोक्ष योग",
                category = YogaCategory.SPECIAL_YOGA,
                planets = listOf(Planet.JUPITER, lord10!!),
                houses = listOf(9, lord10Pos.house),
                description = "Jupiter in 9th with 10th lord in trikona",
                effects = "Liberation through righteous action (karma yoga), dharmic career leads to moksha, guru's guidance crucial, religious profession, teaching spiritual truths, pilgrimage brings realization",
                strength = YogaStrength.STRONG,
                strengthPercentage = 80.0,
                isAuspicious = true,
                activationPeriod = "Jupiter dasha for spiritual elevation",
                cancellationFactors = listOf("Must follow dharmic path", "Adharmic actions block moksha")
            ))
        }

        // 9th and 12th lord exchange or conjunction
        val lord9Pos = if (lord9 != null) chart.planetPositions.find { it.planet == lord9 } else null

        if (lord9Pos != null && lord12Pos != null) {
            val exchange = lord9Pos.house == 12 && lord12Pos.house == 9
            val conjunct = YogaHelpers.areConjunct(lord9Pos, lord12Pos)

            if (exchange || conjunct) {
                yogas.add(Yoga(
                    name = "Bhagya Moksha Yoga",
                    sanskritName = "भाग्य मोक्ष योग",
                    category = YogaCategory.SPECIAL_YOGA,
                    planets = listOfNotNull(lord9, lord12).distinct(),
                    houses = listOf(lord9Pos.house, lord12Pos.house).distinct(),
                    description = "9th lord and 12th lord in ${if (exchange) "exchange" else "conjunction"}",
                    effects = "Fortune through spiritual pursuits, guru leads to liberation, overseas spiritual journey, ashram life favorable, fortunate for meditation, past merit supporting moksha",
                    strength = YogaStrength.STRONG,
                    strengthPercentage = 80.0,
                    isAuspicious = true,
                    activationPeriod = "9th and 12th lord dashas for spiritual progress",
                    cancellationFactors = listOf("Very auspicious for spiritual life", "Must engage in sadhana")
                ))
            }
        }

        return yogas
    }

    // ==================== PRAVRAJYA YOGAS ====================

    private fun evaluatePravrajyaYogas(
        chart: VedicChart,
        houseLords: Map<Int, Planet>
    ): List<Yoga> {
        val yogas = mutableListOf<Yoga>()

        // Pravrajya Yoga Type 1 - Moon in last drekkana of sign, Saturn aspecting
        val moonPos = chart.planetPositions.find { it.planet == Planet.MOON }
        val saturnPos = chart.planetPositions.find { it.planet == Planet.SATURN }

        if (moonPos != null && saturnPos != null) {
            val moonDegreeInSign = moonPos.longitude % 30
            val isLastDrekkana = moonDegreeInSign >= 20.0
            val saturnAspects = YogaHelpers.isAspecting(saturnPos, moonPos)

            if (isLastDrekkana && saturnAspects) {
                yogas.add(Yoga(
                    name = "Chandra Pravrajya Yoga",
                    sanskritName = "चन्द्र प्रव्रज्या योग",
                    category = YogaCategory.SPECIAL_YOGA,
                    planets = listOf(Planet.MOON, Planet.SATURN),
                    houses = listOf(moonPos.house, saturnPos.house),
                    description = "Moon in last drekkana of sign with Saturn's aspect",
                    effects = "Emotional detachment develops, renunciation after disappointments, spiritual seeking through emotional suffering, may leave family late in life, monastic tendencies",
                    strength = YogaStrength.MODERATE,
                    strengthPercentage = 65.0,
                    isAuspicious = true,
                    activationPeriod = "Saturn or Moon dasha may trigger renunciation",
                    cancellationFactors = listOf("Jupiter aspect may give teaching instead of full renunciation", "Strong Venus keeps worldly ties")
                ))
            }
        }

        // Pravrajya Yoga Type 2 - Lord of sign Moon is in, conjunct Saturn and aspected by Mars
        if (moonPos != null && saturnPos != null) {
            val moonSignLord = YogaHelpers.getSignLord(ZodiacSign.fromLongitude(moonPos.longitude))
            val moonSignLordPos = chart.planetPositions.find { it.planet == moonSignLord }
            val marsPos = chart.planetPositions.find { it.planet == Planet.MARS }

            if (moonSignLordPos != null && marsPos != null) {
                val signLordSaturnConjunct = YogaHelpers.areConjunct(moonSignLordPos, saturnPos)
                val marsAspects = YogaHelpers.isAspecting(marsPos, moonSignLordPos)

                if (signLordSaturnConjunct && marsAspects) {
                    yogas.add(Yoga(
                        name = "Rashi Pravrajya Yoga",
                        sanskritName = "राशि प्रव्रज्या योग",
                        category = YogaCategory.SPECIAL_YOGA,
                        planets = listOf(moonSignLord, Planet.SATURN, Planet.MARS),
                        houses = listOf(moonSignLordPos.house),
                        description = "Moon's sign lord conjunct Saturn with Mars aspect",
                        effects = "Mind's lord under malefic influence, emotional detachment forced, suffering leads to wisdom, may renounce after crisis, austere nature develops",
                        strength = YogaStrength.MODERATE,
                        strengthPercentage = 60.0,
                        isAuspicious = true,
                        activationPeriod = "Dasha of Moon's sign lord",
                        cancellationFactors = listOf("Can be difficult before becoming liberating", "Jupiter's influence helps processing")
                    ))
                }
            }
        }

        // Ascetic Yoga - Sun, Moon, Saturn, and Mars all in one sign
        val sunPos = chart.planetPositions.find { it.planet == Planet.SUN }
        val marsPos = chart.planetPositions.find { it.planet == Planet.MARS }

        if (sunPos != null && moonPos != null && saturnPos != null && marsPos != null) {
            val sameHouse = sunPos.house == moonPos.house &&
                    moonPos.house == saturnPos.house &&
                    saturnPos.house == marsPos.house

            if (sameHouse) {
                yogas.add(Yoga(
                    name = "Tapasvi Yoga",
                    sanskritName = "तपस्वी योग",
                    category = YogaCategory.SPECIAL_YOGA,
                    planets = listOf(Planet.SUN, Planet.MOON, Planet.SATURN, Planet.MARS),
                    houses = listOf(sunPos.house),
                    description = "Sun, Moon, Saturn, and Mars conjunct in house ${sunPos.house}",
                    effects = "Ascetic nature, capacity for intense tapas (austerities), burning karma through discipline, powerful meditation ability, may live simply, spiritual warrior nature",
                    strength = YogaStrength.VERY_STRONG,
                    strengthPercentage = 85.0,
                    isAuspicious = true,
                    activationPeriod = "Saturn or Mars dasha for intense spiritual practice",
                    cancellationFactors = listOf("Requires channeling through spiritual practice", "Without discipline can be destructive energy")
                ))
            }
        }

        return yogas
    }

    // ==================== KETU SPIRITUAL YOGAS ====================

    private fun evaluateKetuSpiritualYogas(
        chart: VedicChart,
        houseLords: Map<Int, Planet>
    ): List<Yoga> {
        val yogas = mutableListOf<Yoga>()

        val ketuPos = chart.planetPositions.find { it.planet == Planet.KETU } ?: return yogas
        val jupiterPos = chart.planetPositions.find { it.planet == Planet.JUPITER }
        val moonPos = chart.planetPositions.find { it.planet == Planet.MOON }

        // Ketu-Jupiter conjunction - Guru-Chandala/Ganesha Yoga depending on sign
        if (jupiterPos != null && YogaHelpers.areConjunct(ketuPos, jupiterPos)) {
            val isJupiterStrong = YogaHelpers.isExalted(jupiterPos) || YogaHelpers.isInOwnSign(jupiterPos)

            if (isJupiterStrong) {
                yogas.add(Yoga(
                    name = "Ganesha Yoga",
                    sanskritName = "गणेश योग",
                    category = YogaCategory.SPECIAL_YOGA,
                    planets = listOf(Planet.KETU, Planet.JUPITER),
                    houses = listOf(ketuPos.house),
                    description = "Ketu conjunct dignified Jupiter",
                    effects = "Deep spiritual wisdom, obstacle-removing grace, intuitive knowledge of scriptures, may be a spiritual teacher, removes blockages on spiritual path, Ganesha's blessings",
                    strength = YogaStrength.VERY_STRONG,
                    strengthPercentage = 85.0,
                    isAuspicious = true,
                    activationPeriod = "Jupiter and Ketu dashas for spiritual breakthroughs",
                    cancellationFactors = listOf("Very powerful spiritual combination", "Must use wisdom for dharma")
                ))
            } else {
                yogas.add(Yoga(
                    name = "Guru Chandala Yoga (Spiritual)",
                    sanskritName = "गुरु चण्डाल योग (आध्यात्मिक)",
                    category = YogaCategory.SPECIAL_YOGA,
                    planets = listOf(Planet.KETU, Planet.JUPITER),
                    houses = listOf(ketuPos.house),
                    description = "Ketu conjunct Jupiter - unconventional wisdom",
                    effects = "Unconventional spiritual path, questions traditional teachings, may follow non-mainstream guru, spiritual rebels, reform orthodox traditions, unique philosophical views",
                    strength = YogaStrength.MODERATE,
                    strengthPercentage = 65.0,
                    isAuspicious = true,
                    activationPeriod = "Jupiter-Ketu or Ketu-Jupiter periods",
                    cancellationFactors = listOf("Results depend on house and sign", "Can be spiritually liberating through questioning")
                ))
            }
        }

        // Ketu in 9th house - Spiritual Ketu
        if (ketuPos.house == 9) {
            yogas.add(Yoga(
                name = "Dharma Ketu Yoga",
                sanskritName = "धर्म केतु योग",
                category = YogaCategory.SPECIAL_YOGA,
                planets = listOf(Planet.KETU),
                houses = listOf(9),
                description = "Ketu in 9th house of dharma and higher knowledge",
                effects = "Past-life spiritual seeker, intuitive understanding of dharma, may reject family religion for personal truth, pilgrimage brings realizations, guru appears unexpectedly",
                strength = YogaStrength.STRONG,
                strengthPercentage = 75.0,
                isAuspicious = true,
                activationPeriod = "Ketu dasha deepens spiritual seeking",
                cancellationFactors = listOf("May have unconventional religious views", "Father relationship may be distant or spiritual")
            ))
        }

        // Ketu in 4th - Detachment from home/heart
        if (ketuPos.house == 4) {
            yogas.add(Yoga(
                name = "Griha Vairagya Yoga",
                sanskritName = "गृह वैराग्य योग",
                category = YogaCategory.SPECIAL_YOGA,
                planets = listOf(Planet.KETU),
                houses = listOf(4),
                description = "Ketu in 4th house of home and heart",
                effects = "Detachment from domestic life, peace through renunciation of comforts, may live away from homeland, inner peace independent of external circumstances, meditation practice essential",
                strength = YogaStrength.MODERATE,
                strengthPercentage = 65.0,
                isAuspicious = true,
                activationPeriod = "Ketu dasha may bring physical or emotional distance from home",
                cancellationFactors = listOf("Without spiritual practice can feel rootless", "Jupiter aspect gives inner contentment")
            ))
        }

        // Ketu-Moon conjunction - Psychic/Spiritual sensitivity
        if (moonPos != null && YogaHelpers.areConjunct(ketuPos, moonPos)) {
            yogas.add(Yoga(
                name = "Chandra Ketu Moksha Yoga",
                sanskritName = "चन्द्र केतु मोक्ष योग",
                category = YogaCategory.SPECIAL_YOGA,
                planets = listOf(Planet.KETU, Planet.MOON),
                houses = listOf(ketuPos.house),
                description = "Ketu conjunct Moon - dissolution of emotional patterns",
                effects = "Deep intuition, psychic sensitivity, past-life memories possible, emotional detachment develops, meditation essential for mental peace, can access subtle realms",
                strength = YogaStrength.STRONG,
                strengthPercentage = 75.0,
                isAuspicious = true,
                activationPeriod = "Ketu or Moon dasha for spiritual sensitivity",
                cancellationFactors = listOf("Must ground through spiritual practice", "Jupiter aspect stabilizes")
            ))
        }

        return yogas
    }

    // ==================== 12TH HOUSE SPIRITUAL YOGAS ====================

    private fun evaluate12thHouseYogas(
        chart: VedicChart,
        houseLords: Map<Int, Planet>
    ): List<Yoga> {
        val yogas = mutableListOf<Yoga>()

        val planetsIn12 = chart.planetPositions.filter { it.house == 12 }
        val lord12 = houseLords[12]
        val lord12Pos = if (lord12 != null) chart.planetPositions.find { it.planet == lord12 } else null

        // Jupiter in 12th - Classical spiritual placement
        val jupiterIn12 = planetsIn12.find { it.planet == Planet.JUPITER }
        if (jupiterIn12 != null) {
            val isStrong = YogaHelpers.isExalted(jupiterIn12) || YogaHelpers.isInOwnSign(jupiterIn12)
            val strength = if (isStrong) 90.0 else 80.0

            yogas.add(Yoga(
                name = "Guru Vyaya Yoga",
                sanskritName = "गुरु व्यय योग",
                category = YogaCategory.SPECIAL_YOGA,
                planets = listOf(Planet.JUPITER),
                houses = listOf(12),
                description = "Jupiter in 12th house of liberation${if (isStrong) " in dignity" else ""}",
                effects = "Divine grace for liberation, expenditure on spiritual pursuits, foreign spiritual connections, charitable nature, guru's blessings, meditation comes naturally, protected in solitude",
                strength = YogaHelpers.strengthFromPercentage(strength),
                strengthPercentage = strength,
                isAuspicious = true,
                activationPeriod = "Jupiter dasha for spiritual culmination",
                cancellationFactors = listOf("One of the best 12th house placements", "Protects from negative 12th house effects")
            ))
        }

        // Venus in 12th - Bed pleasures and spiritual love
        val venusIn12 = planetsIn12.find { it.planet == Planet.VENUS }
        if (venusIn12 != null) {
            yogas.add(Yoga(
                name = "Shukra Vyaya Yoga",
                sanskritName = "शुक्र व्यय योग",
                category = YogaCategory.SPECIAL_YOGA,
                planets = listOf(Planet.VENUS),
                houses = listOf(12),
                description = "Venus in 12th house",
                effects = "Devotional (bhakti) nature strong, pleasure through retreat, artistic expression in solitude, may spend on comforts, spiritual love transcending physical, foreign romantic connections",
                strength = YogaStrength.STRONG,
                strengthPercentage = 75.0,
                isAuspicious = true,
                activationPeriod = "Venus dasha for devotional and romantic experiences",
                cancellationFactors = listOf("Good for bhakti yoga", "May exhaust through pleasures if not channeled spiritually")
            ))
        }

        // Multiple planets in 12th
        if (planetsIn12.size >= 3) {
            yogas.add(Yoga(
                name = "Vyaya Sthana Yoga",
                sanskritName = "व्यय स्थान योग",
                category = YogaCategory.SPECIAL_YOGA,
                planets = planetsIn12.map { it.planet },
                houses = listOf(12),
                description = "${planetsIn12.size} planets in 12th house: ${planetsIn12.joinToString { it.planet.displayName }}",
                effects = "Strong 12th house focus - expenditure, foreign lands, spiritual pursuits, hospitals/ashrams, isolation/meditation, past-life karmas prominent, liberation-oriented life",
                strength = YogaStrength.STRONG,
                strengthPercentage = 75.0,
                isAuspicious = true,
                activationPeriod = "Dashas of 12th house planets bring major spiritual events",
                cancellationFactors = listOf("Channel energy toward moksha", "Without direction, can be losses and confusion")
            ))
        }

        // 12th lord in 9th - Bhagya through spirituality
        if (lord12Pos != null && lord12Pos.house == 9) {
            yogas.add(Yoga(
                name = "Vyayesh Bhagya Yoga",
                sanskritName = "व्ययेश भाग्य योग",
                category = YogaCategory.SPECIAL_YOGA,
                planets = listOf(lord12!!),
                houses = listOf(9),
                description = "12th lord ${lord12.displayName} in 9th house",
                effects = "Fortune through spiritual pursuits, ashram life brings luck, foreign pilgrimage, guru guides to liberation, righteous spending, charitable deeds bring merit",
                strength = YogaStrength.STRONG,
                strengthPercentage = 75.0,
                isAuspicious = true,
                activationPeriod = "12th lord dasha for spiritual fortune",
                cancellationFactors = listOf("Excellent for spiritual life", "May spend on dharmic causes")
            ))
        }

        return yogas
    }

    // ==================== SATURN-MOON DETACHMENT YOGAS ====================

    private fun evaluateSaturnMoonYogas(
        chart: VedicChart,
        houseLords: Map<Int, Planet>
    ): List<Yoga> {
        val yogas = mutableListOf<Yoga>()

        val moonPos = chart.planetPositions.find { it.planet == Planet.MOON } ?: return yogas
        val saturnPos = chart.planetPositions.find { it.planet == Planet.SATURN } ?: return yogas

        // Saturn-Moon conjunction - Vairagya Yoga
        if (YogaHelpers.areConjunct(moonPos, saturnPos)) {
            val jupiterAspect = chart.planetPositions.find { it.planet == Planet.JUPITER }
                ?.let { YogaHelpers.isAspecting(it, moonPos) } == true

            yogas.add(Yoga(
                name = "Vairagya Yoga",
                sanskritName = "वैराग्य योग",
                category = YogaCategory.SPECIAL_YOGA,
                planets = listOf(Planet.MOON, Planet.SATURN),
                houses = listOf(moonPos.house),
                description = "Moon conjunct Saturn - classical detachment combination",
                effects = "Emotional detachment, serious mind, depression becomes wisdom, suffering leads to renunciation, may lose mother early or have distant relationship, philosophical outlook on emotions",
                strength = YogaStrength.STRONG,
                strengthPercentage = 75.0,
                isAuspicious = true,
                activationPeriod = "Saturn and Moon dashas bring detachment experiences",
                cancellationFactors = listOf(
                    if (jupiterAspect) "Jupiter aspect provides wisdom and protection" else "Jupiter aspect would help processing",
                    "Spiritual practice transforms suffering"
                )
            ))
        }

        // Saturn aspects Moon - lesser but still significant
        if (!YogaHelpers.areConjunct(moonPos, saturnPos) && YogaHelpers.isAspecting(saturnPos, moonPos)) {
            yogas.add(Yoga(
                name = "Shani Drishti Chandra Yoga",
                sanskritName = "शनि दृष्टि चन्द्र योग",
                category = YogaCategory.SPECIAL_YOGA,
                planets = listOf(Planet.SATURN, Planet.MOON),
                houses = listOf(moonPos.house, saturnPos.house),
                description = "Saturn aspects Moon from house ${saturnPos.house}",
                effects = "Emotional discipline, seriousness about feelings, delayed emotional fulfillment, maturity in relationships, responsibility weighs on mind, structured emotional life",
                strength = YogaStrength.MODERATE,
                strengthPercentage = 60.0,
                isAuspicious = true,
                activationPeriod = "Saturn dasha affecting emotional patterns",
                cancellationFactors = listOf("Less intense than conjunction", "Gives emotional discipline useful for meditation")
            ))
        }

        // Moon in Saturn's signs (Capricorn/Aquarius) - Moksha potential
        val moonSign = ZodiacSign.fromLongitude(moonPos.longitude)
        if (moonSign == ZodiacSign.CAPRICORN || moonSign == ZodiacSign.AQUARIUS) {
            val isDebilitated = moonSign == ZodiacSign.CAPRICORN && moonPos.longitude % 30 <= 3 // Near exact debilitation

            yogas.add(Yoga(
                name = "Shani Rashi Chandra Yoga",
                sanskritName = "शनि राशि चन्द्र योग",
                category = YogaCategory.SPECIAL_YOGA,
                planets = listOf(Planet.MOON),
                houses = listOf(moonPos.house),
                description = "Moon in Saturn's sign ${moonSign.displayName}${if (isDebilitated) " (debilitated)" else ""}",
                effects = "Practical emotional nature, detachment develops naturally, serious mind, responsibility for others' emotions, public service inclinations, structured inner life",
                strength = if (isDebilitated) YogaStrength.WEAK else YogaStrength.MODERATE,
                strengthPercentage = if (isDebilitated) 40.0 else 60.0,
                isAuspicious = !isDebilitated,
                activationPeriod = "Moon dasha brings Saturnian emotional experiences",
                cancellationFactors = listOf("Check for Neecha Bhanga if debilitated", "Saturn's position modifies results")
            ))
        }

        return yogas
    }

    // ==================== JUPITER SPIRITUAL YOGAS ====================

    private fun evaluateJupiterSpiritualYogas(
        chart: VedicChart,
        houseLords: Map<Int, Planet>
    ): List<Yoga> {
        val yogas = mutableListOf<Yoga>()

        val jupiterPos = chart.planetPositions.find { it.planet == Planet.JUPITER } ?: return yogas

        // Jupiter in 5th - Purva Punya (past merit)
        if (jupiterPos.house == 5) {
            val isStrong = YogaHelpers.isExalted(jupiterPos) || YogaHelpers.isInOwnSign(jupiterPos)
            val strength = if (isStrong) 90.0 else 80.0

            yogas.add(Yoga(
                name = "Purva Punya Yoga",
                sanskritName = "पूर्व पुण्य योग",
                category = YogaCategory.SPECIAL_YOGA,
                planets = listOf(Planet.JUPITER),
                houses = listOf(5),
                description = "Jupiter in 5th house of past merit${if (isStrong) " in dignity" else ""}",
                effects = "Strong past-life spiritual merit, natural wisdom, devotional children, mantra siddhi, intuitive knowledge, blessed intellect, divine grace through creativity",
                strength = YogaHelpers.strengthFromPercentage(strength),
                strengthPercentage = strength,
                isAuspicious = true,
                activationPeriod = "Jupiter dasha activates past-life merit",
                cancellationFactors = listOf("Excellent spiritual placement", "Children may be spiritually inclined")
            ))
        }

        // Jupiter in 9th - Guru Bhagya Yoga
        if (jupiterPos.house == 9) {
            val isStrong = YogaHelpers.isExalted(jupiterPos) || YogaHelpers.isInOwnSign(jupiterPos)
            val strength = if (isStrong) 95.0 else 85.0

            yogas.add(Yoga(
                name = "Guru Bhagya Yoga",
                sanskritName = "गुरु भाग्य योग",
                category = YogaCategory.SPECIAL_YOGA,
                planets = listOf(Planet.JUPITER),
                houses = listOf(9),
                description = "Jupiter in 9th house of dharma${if (isStrong) " in dignity" else ""}",
                effects = "Blessed by guru, dharmic fortune, religious wisdom, pilgrimage transforms, teaching abilities, righteous nature brings luck, philosophical depth, higher education success",
                strength = YogaHelpers.strengthFromPercentage(strength),
                strengthPercentage = strength,
                isAuspicious = true,
                activationPeriod = "Jupiter dasha brings spiritual teachers and fortune",
                cancellationFactors = listOf("Jupiter in own house of dharma", "Most auspicious 9th house placement")
            ))
        }

        // Jupiter aspecting 5th, 9th, or 12th houses
        val spiritualHouses = listOf(5, 9, 12)
        val jupiterAspects = spiritualHouses.filter { house ->
            val targetPos = chart.planetPositions.find { it.house == house }
            targetPos != null && YogaHelpers.isAspecting(jupiterPos, targetPos)
        }

        if (jupiterAspects.size >= 2) {
            yogas.add(Yoga(
                name = "Guru Drishti Adhyatma Yoga",
                sanskritName = "गुरु दृष्टि अध्यात्म योग",
                category = YogaCategory.SPECIAL_YOGA,
                planets = listOf(Planet.JUPITER),
                houses = listOf(jupiterPos.house) + jupiterAspects,
                description = "Jupiter aspects multiple spiritual houses (${jupiterAspects.joinToString()})",
                effects = "Divine protection on spiritual path, wisdom reaches multiple life areas, grace in meditation, blessed purva punya and dharma, liberation supported",
                strength = YogaStrength.STRONG,
                strengthPercentage = 75.0,
                isAuspicious = true,
                activationPeriod = "Jupiter dasha enhances all spiritual significations",
                cancellationFactors = listOf("Jupiter's aspects are always beneficial", "Protects spiritual houses")
            ))
        }

        return yogas
    }

    // ==================== TRANSCENDENCE YOGAS ====================

    private fun evaluateTranscendenceYogas(
        chart: VedicChart,
        houseLords: Map<Int, Planet>
    ): List<Yoga> {
        val yogas = mutableListOf<Yoga>()

        // Rahu in 12th - Transcendence through the unconventional
        val rahuPos = chart.planetPositions.find { it.planet == Planet.RAHU }
        if (rahuPos?.house == 12) {
            val jupiterAspect = chart.planetPositions.find { it.planet == Planet.JUPITER }
                ?.let { YogaHelpers.isAspecting(it, rahuPos) } == true

            yogas.add(Yoga(
                name = "Rahu Vyaya Yoga",
                sanskritName = "राहु व्यय योग",
                category = YogaCategory.SPECIAL_YOGA,
                planets = listOf(Planet.RAHU),
                houses = listOf(12),
                description = "Rahu in 12th house of liberation",
                effects = "Unconventional spiritual path, foreign spiritual practices, tantra/occult interests, breaking free from conditioned mind, liberation through intense experiences, may live abroad for spiritual reasons",
                strength = if (jupiterAspect) YogaStrength.STRONG else YogaStrength.MODERATE,
                strengthPercentage = if (jupiterAspect) 75.0 else 60.0,
                isAuspicious = jupiterAspect,
                activationPeriod = "Rahu dasha brings intense spiritual seeking",
                cancellationFactors = listOf(
                    if (jupiterAspect) "Jupiter aspect sanctifies and protects" else "Jupiter aspect needed for positive results",
                    "Can go astray without proper guidance"
                )
            ))
        }

        // All moksha trikona (4, 8, 12) lords in kendras/trikonas
        val lord4 = houseLords[4]
        val lord8 = houseLords[8]
        val lord12 = houseLords[12]

        val lord4Pos = if (lord4 != null) chart.planetPositions.find { it.planet == lord4 } else null
        val lord8Pos = if (lord8 != null) chart.planetPositions.find { it.planet == lord8 } else null
        val lord12Pos = if (lord12 != null) chart.planetPositions.find { it.planet == lord12 } else null

        val goodHouses = listOf(1, 4, 5, 7, 9, 10)
        val allMokshaLordsWellPlaced = lord4Pos?.house in goodHouses &&
                lord8Pos?.house in goodHouses &&
                lord12Pos?.house in goodHouses

        if (allMokshaLordsWellPlaced && lord4Pos != null && lord8Pos != null && lord12Pos != null) {
            yogas.add(Yoga(
                name = "Parama Moksha Yoga",
                sanskritName = "परम मोक्ष योग",
                category = YogaCategory.SPECIAL_YOGA,
                planets = listOfNotNull(lord4, lord8, lord12).distinct(),
                houses = listOf(lord4Pos.house, lord8Pos.house, lord12Pos.house).distinct(),
                description = "All moksha trikona lords (4th, 8th, 12th) well-placed in kendras/trikonas",
                effects = "Excellent spiritual configuration, peace of mind, transformative wisdom leads to liberation, supported journey to moksha, meditation bears fruit, death peaceful",
                strength = YogaStrength.VERY_STRONG,
                strengthPercentage = 85.0,
                isAuspicious = true,
                activationPeriod = "12th lord dasha for final liberation aspects",
                cancellationFactors = listOf("Rare and powerful yoga", "Must engage in spiritual practice")
            ))
        }

        // Atmakaraka in 12th - Soul seeking liberation
        val atmakaraka = YogaHelpers.findAtmakaraka(chart)
        if (atmakaraka != null) {
            val atmakarakaPos = chart.planetPositions.find { it.planet == atmakaraka }
            if (atmakarakaPos?.house == 12) {
                yogas.add(Yoga(
                    name = "Atmakaraka Moksha Yoga",
                    sanskritName = "आत्मकारक मोक्ष योग",
                    category = YogaCategory.SPECIAL_YOGA,
                    planets = listOf(atmakaraka),
                    houses = listOf(12),
                    description = "Atmakaraka ${atmakaraka.displayName} in 12th house",
                    effects = "Soul's deepest desire is liberation, spiritual seeking is primary life purpose, renunciation natural, may live in foreign land or ashram, karma directed toward moksha",
                    strength = YogaStrength.VERY_STRONG,
                    strengthPercentage = 85.0,
                    isAuspicious = true,
                    activationPeriod = "${atmakaraka.displayName} dasha intensifies moksha seeking",
                    cancellationFactors = listOf("Atmakaraka shows soul's evolution direction", "Liberation is soul's destination")
                ))
            }
        }

        return yogas
    }

    // ==================== HELPER FUNCTIONS ====================

    private fun determineSannyasaType(planets: List<PlanetPosition>): String {
        val planetCounts = planets.groupBy { it.planet }

        // Find the strongest planet (by dignity or by being the karaka)
        return when {
            planets.any { it.planet == Planet.SUN && (YogaHelpers.isExalted(it) || YogaHelpers.isInOwnSign(it)) } -> "Surya"
            planets.any { it.planet == Planet.MOON && (YogaHelpers.isExalted(it) || YogaHelpers.isInOwnSign(it)) } -> "Chandra"
            planets.any { it.planet == Planet.MARS && (YogaHelpers.isExalted(it) || YogaHelpers.isInOwnSign(it)) } -> "Kuja"
            planets.any { it.planet == Planet.MERCURY && (YogaHelpers.isExalted(it) || YogaHelpers.isInOwnSign(it)) } -> "Budha"
            planets.any { it.planet == Planet.JUPITER && (YogaHelpers.isExalted(it) || YogaHelpers.isInOwnSign(it)) } -> "Guru"
            planets.any { it.planet == Planet.VENUS && (YogaHelpers.isExalted(it) || YogaHelpers.isInOwnSign(it)) } -> "Shukra"
            planets.any { it.planet == Planet.SATURN && (YogaHelpers.isExalted(it) || YogaHelpers.isInOwnSign(it)) } -> "Shani"
            planets.any { it.planet == Planet.JUPITER } -> "Guru"  // Default to Jupiter if present
            planets.any { it.planet == Planet.SATURN } -> "Shani"  // Or Saturn
            else -> "Graha"
        }
    }

    private fun getHouseSignifications(house: Int): String {
        return when (house) {
            1 -> "self and personality"
            2 -> "wealth and speech"
            3 -> "courage and siblings"
            4 -> "home and mother"
            5 -> "children and creativity"
            6 -> "enemies and service"
            7 -> "partnerships and spouse"
            8 -> "transformation and occult"
            9 -> "dharma and fortune"
            10 -> "career and status"
            11 -> "gains and aspirations"
            12 -> "liberation and losses"
            else -> "various life areas"
        }
    }
}
