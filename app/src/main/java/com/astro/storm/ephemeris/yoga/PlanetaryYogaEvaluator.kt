package com.astro.storm.ephemeris.yoga

import com.astro.storm.core.model.Planet
import com.astro.storm.core.model.PlanetPosition
import com.astro.storm.core.model.VedicChart
import com.astro.storm.core.model.ZodiacSign

/**
 * Planetary Yoga Evaluator - Individual Planet Position and Dignity Yogas
 *
 * This evaluator handles yogas formed by individual planets in specific signs,
 * houses, and states of dignity. These yogas are based on the intrinsic nature
 * of planets and their positions.
 *
 * Categories covered:
 * 1. Suryadi Yogas - Sun-based planetary yogas
 * 2. Chandradi Yogas - Moon-based planetary yogas
 * 3. Mangaladi Yogas - Mars-based planetary yogas
 * 4. Budhadi Yogas - Mercury-based planetary yogas
 * 5. Gurvadi Yogas - Jupiter-based planetary yogas
 * 6. Shukradi Yogas - Venus-based planetary yogas
 * 7. Shaniadi Yogas - Saturn-based planetary yogas
 * 8. Node-based Yogas - Rahu/Ketu position yogas
 * 9. Dignity Yogas - Exaltation, debilitation patterns
 * 10. House Position Yogas - Planets in specific houses
 *
 * Based on classical texts:
 * - Brihat Parasara Hora Shastra (BPHS), Chapters 25-27
 * - Phaladeepika, Chapters 2-4
 * - Saravali, Chapters 30-37
 * - Hora Ratna
 *
 * @author AstroStorm
 */
class PlanetaryYogaEvaluator : YogaEvaluator {

    override val category: YogaCategory = YogaCategory.SPECIAL_YOGA

    override fun evaluate(chart: VedicChart): List<Yoga> {
        val yogas = mutableListOf<Yoga>()
        val ascendantSign = ZodiacSign.fromLongitude(chart.ascendant)
        val houseLords = YogaHelpers.getHouseLords(ascendantSign)

        // 1. Sun-based Yogas
        yogas.addAll(evaluateSunYogas(chart, houseLords))

        // 2. Moon-based Yogas
        yogas.addAll(evaluateMoonYogas(chart, houseLords))

        // 3. Mars-based Yogas
        yogas.addAll(evaluateMarsYogas(chart, houseLords))

        // 4. Mercury-based Yogas
        yogas.addAll(evaluateMercuryYogas(chart, houseLords))

        // 5. Jupiter-based Yogas
        yogas.addAll(evaluateJupiterYogas(chart, houseLords))

        // 6. Venus-based Yogas
        yogas.addAll(evaluateVenusYogas(chart, houseLords))

        // 7. Saturn-based Yogas
        yogas.addAll(evaluateSaturnYogas(chart, houseLords))

        // 8. Rahu-Ketu Yogas
        yogas.addAll(evaluateRahuKetuYogas(chart, houseLords))

        // 9. Dignity Pattern Yogas
        yogas.addAll(evaluateDignityYogas(chart))

        // 10. Special House Position Yogas
        yogas.addAll(evaluateHousePositionYogas(chart, houseLords))

        return yogas
    }

    // ==================== SUN-BASED YOGAS ====================

    private fun evaluateSunYogas(chart: VedicChart, houseLords: Map<Int, Planet>): List<Yoga> {
        val yogas = mutableListOf<Yoga>()
        val sunPos = chart.planetPositions.find { it.planet == Planet.SUN } ?: return yogas

        // 1. Uttamadi Yoga - Sun in exaltation (Aries)
        if (YogaHelpers.isExalted(sunPos)) {
            val strength = YogaHelpers.calculateYogaStrength(chart, listOf(sunPos)) * 1.2
            yogas.add(Yoga(
                name = "Uttamadi Yoga",
                sanskritName = "उत्तमादि योग",
                category = YogaCategory.SPECIAL_YOGA,
                planets = listOf(Planet.SUN),
                houses = listOf(sunPos.house),
                description = "Sun exalted in Aries at ${String.format("%.1f", sunPos.longitude % 30)}°",
                effects = "Leadership qualities, royal status, government favor, authority over others, strong vitality, father prosperous",
                strength = YogaHelpers.strengthFromPercentage(strength.coerceIn(10.0, 100.0)),
                strengthPercentage = strength.coerceIn(10.0, 100.0),
                isAuspicious = true,
                activationPeriod = "Sun Dasha and Antardasha periods",
                cancellationFactors = if (sunPos.house in listOf(6, 8, 12))
                    listOf("Sun in dusthana house reduces some benefic effects") else emptyList()
            ))
        }

        // 2. Neechabhilashi Yoga - Sun debilitated but aspected by its dispositor (Venus)
        if (YogaHelpers.isDebilitated(sunPos)) {
            val venusPos = chart.planetPositions.find { it.planet == Planet.VENUS }
            if (venusPos != null && YogaHelpers.isAspecting(venusPos, sunPos)) {
                val strength = 65.0
                yogas.add(Yoga(
                    name = "Neechabhilashi Yoga (Sun)",
                    sanskritName = "नीचाभिलाषी योग",
                    category = YogaCategory.SPECIAL_YOGA,
                    planets = listOf(Planet.SUN, Planet.VENUS),
                    houses = listOf(sunPos.house),
                    description = "Debilitated Sun aspected by Venus (its dispositor)",
                    effects = "Initial struggles followed by rise, gains through creative and artistic pursuits, refined leadership",
                    strength = YogaHelpers.strengthFromPercentage(strength),
                    strengthPercentage = strength,
                    isAuspicious = true,
                    activationPeriod = "Sun-Venus or Venus-Sun periods",
                    cancellationFactors = emptyList()
                ))
            }
        }

        // 3. Pravrajya Yoga (Sun variant) - Sun in 10th with Saturn aspect
        val saturnPos = chart.planetPositions.find { it.planet == Planet.SATURN }
        if (sunPos.house == 10 && saturnPos != null && YogaHelpers.isAspecting(saturnPos, sunPos)) {
            val strength = 55.0
            yogas.add(Yoga(
                name = "Tapasvi Yoga",
                sanskritName = "तपस्वी योग",
                category = YogaCategory.SPECIAL_YOGA,
                planets = listOf(Planet.SUN, Planet.SATURN),
                houses = listOf(10),
                description = "Sun in 10th house aspected by Saturn",
                effects = "Disciplined leader, achieves through hard work and perseverance, respected for integrity, may face delays but ultimate success",
                strength = YogaHelpers.strengthFromPercentage(strength),
                strengthPercentage = strength,
                isAuspicious = true,
                activationPeriod = "Sun or Saturn periods",
                cancellationFactors = listOf("Sun-Saturn aspect creates tension between ego and discipline")
            ))
        }

        // 4. Surya Yoga - Sun in its own sign (Leo) in Kendra
        if (sunPos.sign == ZodiacSign.LEO && sunPos.house in listOf(1, 4, 7, 10)) {
            val strength = YogaHelpers.calculateYogaStrength(chart, listOf(sunPos))
            yogas.add(Yoga(
                name = "Surya Yoga",
                sanskritName = "सूर्य योग",
                category = YogaCategory.SPECIAL_YOGA,
                planets = listOf(Planet.SUN),
                houses = listOf(sunPos.house),
                description = "Sun in own sign Leo in Kendra (house ${sunPos.house})",
                effects = "Natural leader, strong personality, government connections, success in politics and administration, fame and recognition",
                strength = YogaHelpers.strengthFromPercentage(strength),
                strengthPercentage = strength,
                isAuspicious = true,
                activationPeriod = "Sun Dasha periods",
                cancellationFactors = emptyList()
            ))
        }

        // 5. Prabhakar Yoga - Sun in 1st or 10th, exalted or own sign, with Jupiter aspect
        val jupiterPos = chart.planetPositions.find { it.planet == Planet.JUPITER }
        if ((sunPos.house == 1 || sunPos.house == 10) &&
            (YogaHelpers.isExalted(sunPos) || YogaHelpers.isInOwnSign(sunPos)) &&
            jupiterPos != null && YogaHelpers.isAspecting(jupiterPos, sunPos)) {
            val strength = 85.0
            yogas.add(Yoga(
                name = "Prabhakar Yoga",
                sanskritName = "प्रभाकर योग",
                category = YogaCategory.SPECIAL_YOGA,
                planets = listOf(Planet.SUN, Planet.JUPITER),
                houses = listOf(sunPos.house),
                description = "Strong Sun in Lagna or 10th with Jupiter aspect",
                effects = "Brilliant personality, spiritual leadership, philanthropic nature, highly respected, long-lasting fame and reputation",
                strength = YogaHelpers.strengthFromPercentage(strength),
                strengthPercentage = strength,
                isAuspicious = true,
                activationPeriod = "Sun-Jupiter or Jupiter-Sun periods",
                cancellationFactors = emptyList()
            ))
        }

        // 6. Khala Yoga - Sun combust with Mars
        val marsPos = chart.planetPositions.find { it.planet == Planet.MARS }
        if (marsPos != null && YogaHelpers.areConjunct(sunPos, marsPos, 5.0)) {
            val combustionFactor = YogaHelpers.getCombustionFactor(marsPos, chart)
            if (combustionFactor < 0.7) {
                val strength = 45.0
                yogas.add(Yoga(
                    name = "Khala Yoga (Sun-Mars)",
                    sanskritName = "खल योग",
                    category = YogaCategory.SPECIAL_YOGA,
                    planets = listOf(Planet.SUN, Planet.MARS),
                    houses = listOf(sunPos.house),
                    description = "Mars combust with Sun within 5°",
                    effects = "Impulsive nature, conflicts with authority figures, excess heat in body, needs to control anger, courageous but rash decisions",
                    strength = YogaHelpers.strengthFromPercentage(strength),
                    strengthPercentage = strength,
                    isAuspicious = false,
                    activationPeriod = "Sun-Mars or Mars-Sun periods",
                    cancellationFactors = listOf("Jupiter aspect can moderate the effects", "If in fire signs, Mars retains some strength")
                ))
            }
        }

        return yogas
    }

    // ==================== MOON-BASED YOGAS ====================

    private fun evaluateMoonYogas(chart: VedicChart, houseLords: Map<Int, Planet>): List<Yoga> {
        val yogas = mutableListOf<Yoga>()
        val moonPos = chart.planetPositions.find { it.planet == Planet.MOON } ?: return yogas
        val moonStrength = YogaHelpers.getMoonPhaseStrength(moonPos, chart)

        // 1. Pushkara Navamsha Yoga - Moon in Pushkara Navamsha degrees
        val pushkaraDegrees = listOf(
            // Pushkara Navamsha positions by sign (approximate)
            ZodiacSign.ARIES to listOf(21.0..23.33),
            ZodiacSign.TAURUS to listOf(7.0..10.0, 21.0..23.33),
            ZodiacSign.GEMINI to listOf(3.33..6.66, 17.0..20.0),
            ZodiacSign.CANCER to listOf(14.0..16.66, 27.0..30.0),
            ZodiacSign.LEO to listOf(10.0..13.33, 24.0..26.66),
            ZodiacSign.VIRGO to listOf(7.0..10.0, 20.0..23.33),
            ZodiacSign.LIBRA to listOf(3.33..6.66, 17.0..20.0),
            ZodiacSign.SCORPIO to listOf(14.0..16.66, 27.0..30.0),
            ZodiacSign.SAGITTARIUS to listOf(10.0..13.33, 24.0..26.66),
            ZodiacSign.CAPRICORN to listOf(7.0..10.0, 21.0..23.33),
            ZodiacSign.AQUARIUS to listOf(3.33..6.66, 17.0..20.0),
            ZodiacSign.PISCES to listOf(14.0..16.66, 27.0..30.0)
        ).toMap()

        val moonDegreeInSign = moonPos.longitude % 30
        val pushkaraRanges = pushkaraDegrees[moonPos.sign] ?: emptyList()
        val inPushkara = pushkaraRanges.any { moonDegreeInSign in it }

        if (inPushkara) {
            val strength = 70.0 * (if (moonStrength > 0.5) 1.2 else 0.9)
            yogas.add(Yoga(
                name = "Pushkara Navamsha Yoga",
                sanskritName = "पुष्कर नवांश योग",
                category = YogaCategory.SPECIAL_YOGA,
                planets = listOf(Planet.MOON),
                houses = listOf(moonPos.house),
                description = "Moon at ${String.format("%.1f", moonDegreeInSign)}° ${moonPos.sign.displayName} in Pushkara Navamsha",
                effects = "Blessed and auspicious Moon, prosperity in material and spiritual matters, good fortune, supported by divine grace",
                strength = YogaHelpers.strengthFromPercentage(strength.coerceIn(10.0, 100.0)),
                strengthPercentage = strength.coerceIn(10.0, 100.0),
                isAuspicious = true,
                activationPeriod = "Moon Dasha periods",
                cancellationFactors = if (moonStrength < 0.3) listOf("Waning Moon reduces effect") else emptyList()
            ))
        }

        // 2. Chandra Mangala Yoga (covered in Dhana) - Skip here to avoid duplicate

        // 3. Sharada Yoga - Moon in own or exalted sign with Mercury aspect
        val mercuryPos = chart.planetPositions.find { it.planet == Planet.MERCURY }
        if ((moonPos.sign == ZodiacSign.CANCER || moonPos.sign == ZodiacSign.TAURUS) &&
            mercuryPos != null && YogaHelpers.isAspecting(mercuryPos, moonPos)) {
            val strength = 75.0
            yogas.add(Yoga(
                name = "Sharada Yoga",
                sanskritName = "शारदा योग",
                category = YogaCategory.SPECIAL_YOGA,
                planets = listOf(Planet.MOON, Planet.MERCURY),
                houses = listOf(moonPos.house),
                description = "Moon in Cancer/Taurus with Mercury aspect",
                effects = "Poetic talents, literary abilities, pleasant speech, good memory, emotional intelligence combined with intellectual prowess",
                strength = YogaHelpers.strengthFromPercentage(strength),
                strengthPercentage = strength,
                isAuspicious = true,
                activationPeriod = "Moon-Mercury or Mercury-Moon periods",
                cancellationFactors = emptyList()
            ))
        }

        // 4. Lakshmi Yoga (Moon variant) - Moon in own sign in Kendra with Venus aspect
        val venusPos = chart.planetPositions.find { it.planet == Planet.VENUS }
        if (moonPos.sign == ZodiacSign.CANCER && moonPos.house in listOf(1, 4, 7, 10) &&
            venusPos != null && YogaHelpers.isAspecting(venusPos, moonPos)) {
            val strength = 80.0
            yogas.add(Yoga(
                name = "Chandra Lakshmi Yoga",
                sanskritName = "चंद्र लक्ष्मी योग",
                category = YogaCategory.SPECIAL_YOGA,
                planets = listOf(Planet.MOON, Planet.VENUS),
                houses = listOf(moonPos.house),
                description = "Moon in own sign in Kendra with Venus aspect",
                effects = "Wealth through property and agriculture, beautiful home, loving mother, comforts and luxuries, emotional satisfaction",
                strength = YogaHelpers.strengthFromPercentage(strength),
                strengthPercentage = strength,
                isAuspicious = true,
                activationPeriod = "Moon-Venus or Venus-Moon periods",
                cancellationFactors = emptyList()
            ))
        }

        // 5. Purnima Yoga - Full Moon (within 12° of opposition to Sun)
        if (moonStrength > 0.9) {
            val strength = 75.0
            yogas.add(Yoga(
                name = "Purnima Yoga",
                sanskritName = "पूर्णिमा योग",
                category = YogaCategory.CHANDRA_YOGA,
                planets = listOf(Planet.MOON),
                houses = listOf(moonPos.house),
                description = "Moon at or near full phase (${String.format("%.0f", moonStrength * 100)}% illuminated)",
                effects = "Strong mind, public popularity, good maternal influences, emotional stability, prosperity especially in Moon-ruled matters",
                strength = YogaHelpers.strengthFromPercentage(strength),
                strengthPercentage = strength,
                isAuspicious = true,
                activationPeriod = "Moon Dasha periods",
                cancellationFactors = emptyList()
            ))
        }

        // 6. Amavasya Yoga - New Moon (within 12° of conjunction with Sun)
        if (moonStrength < 0.1) {
            val strength = 40.0
            yogas.add(Yoga(
                name = "Amavasya Yoga",
                sanskritName = "अमावस्या योग",
                category = YogaCategory.CHANDRA_YOGA,
                planets = listOf(Planet.MOON),
                houses = listOf(moonPos.house),
                description = "Moon at or near new phase (dark Moon)",
                effects = "Introspective nature, spiritual inclinations, may face mental challenges, mother may have health issues, needs to work on emotional stability",
                strength = YogaHelpers.strengthFromPercentage(strength),
                strengthPercentage = strength,
                isAuspicious = false,
                activationPeriod = "Moon Dasha periods require extra care",
                cancellationFactors = listOf("Jupiter aspect significantly improves results", "Moon in Kendra reduces negative effects")
            ))
        }

        // 7. Chandradhi Yoga - Benefics in 6th, 7th, 8th from Moon
        val benefics = listOf(Planet.JUPITER, Planet.VENUS, Planet.MERCURY)
        val house6FromMoon = chart.planetPositions.filter {
            it.planet in benefics && YogaHelpers.getHouseFrom(it.sign, moonPos.sign) == 6
        }
        val house7FromMoon = chart.planetPositions.filter {
            it.planet in benefics && YogaHelpers.getHouseFrom(it.sign, moonPos.sign) == 7
        }
        val house8FromMoon = chart.planetPositions.filter {
            it.planet in benefics && YogaHelpers.getHouseFrom(it.sign, moonPos.sign) == 8
        }

        if (house6FromMoon.isNotEmpty() && house7FromMoon.isNotEmpty() && house8FromMoon.isNotEmpty()) {
            val allBenefics = house6FromMoon + house7FromMoon + house8FromMoon
            val strength = YogaHelpers.calculateYogaStrength(chart, allBenefics)
            yogas.add(Yoga(
                name = "Chandradhi Yoga",
                sanskritName = "चंद्राधि योग",
                category = YogaCategory.CHANDRA_YOGA,
                planets = allBenefics.map { it.planet },
                houses = allBenefics.map { it.house }.distinct(),
                description = "Benefics in 6th, 7th, and 8th houses from Moon",
                effects = "Polite, trustworthy, destroys enemies, many comforts, long life, good health, respected in society",
                strength = YogaHelpers.strengthFromPercentage(strength),
                strengthPercentage = strength,
                isAuspicious = true,
                activationPeriod = "Periods of the benefics involved",
                cancellationFactors = emptyList()
            ))
        }

        return yogas
    }

    // ==================== MARS-BASED YOGAS ====================

    private fun evaluateMarsYogas(chart: VedicChart, houseLords: Map<Int, Planet>): List<Yoga> {
        val yogas = mutableListOf<Yoga>()
        val marsPos = chart.planetPositions.find { it.planet == Planet.MARS } ?: return yogas

        // 1. Ruchaka Yoga is handled in MahapurushaYogaEvaluator - skip

        // 2. Kuja Dosha / Mangal Dosha (for reference - handled specially)
        val mangalDoshaHouses = listOf(1, 2, 4, 7, 8, 12)
        if (marsPos.house in mangalDoshaHouses) {
            // Check for cancellation conditions
            val cancellations = mutableListOf<String>()

            // Cancellation 1: Mars in own sign (Aries, Scorpio)
            if (YogaHelpers.isInOwnSign(marsPos)) {
                cancellations.add("Mars in own sign cancels Mangal Dosha")
            }

            // Cancellation 2: Mars in exaltation (Capricorn)
            if (YogaHelpers.isExalted(marsPos)) {
                cancellations.add("Mars exalted cancels Mangal Dosha")
            }

            // Cancellation 3: Jupiter aspect on Mars
            val jupiterPos = chart.planetPositions.find { it.planet == Planet.JUPITER }
            if (jupiterPos != null && YogaHelpers.isAspecting(jupiterPos, marsPos)) {
                cancellations.add("Jupiter aspect on Mars reduces Mangal Dosha")
            }

            // Cancellation 4: Mars in movable sign
            val movableSigns = listOf(ZodiacSign.ARIES, ZodiacSign.CANCER, ZodiacSign.LIBRA, ZodiacSign.CAPRICORN)
            if (marsPos.sign in movableSigns) {
                cancellations.add("Mars in movable sign reduces intensity")
            }

            val isCancelled = cancellations.size >= 2
            val strength = if (isCancelled) 30.0 else 55.0

            yogas.add(Yoga(
                name = "Kuja Dosha",
                sanskritName = "कुज दोष",
                category = YogaCategory.NEGATIVE_YOGA,
                planets = listOf(Planet.MARS),
                houses = listOf(marsPos.house),
                description = "Mars in house ${marsPos.house} (Mangal Dosha house)",
                effects = if (isCancelled)
                    "Mangal Dosha present but significantly cancelled - minor delays in marriage, occasional conflicts, but generally manageable"
                else
                    "Delays or difficulties in marriage, need for Mangal Dosha matching, possible conflicts in partnerships, should marry after age 28 or to someone with similar placement",
                strength = YogaHelpers.strengthFromPercentage(strength),
                strengthPercentage = strength,
                isAuspicious = false,
                activationPeriod = "Mars Dasha and during Mars transits to 7th house",
                cancellationFactors = cancellations.ifEmpty { listOf("No significant cancellations present") }
            ))
        }

        // 3. Vijaya Yoga - Mars in 10th house exalted or own sign
        if (marsPos.house == 10 && (YogaHelpers.isExalted(marsPos) || YogaHelpers.isInOwnSign(marsPos))) {
            val strength = 80.0
            yogas.add(Yoga(
                name = "Vijaya Yoga (Mars)",
                sanskritName = "विजय योग",
                category = YogaCategory.SPECIAL_YOGA,
                planets = listOf(Planet.MARS),
                houses = listOf(10),
                description = "Mars strong in 10th house",
                effects = "Victory in endeavors, success in competitive fields, leadership in military/police/sports, strong career, commands respect",
                strength = YogaHelpers.strengthFromPercentage(strength),
                strengthPercentage = strength,
                isAuspicious = true,
                activationPeriod = "Mars Dasha periods",
                cancellationFactors = emptyList()
            ))
        }

        // 4. Parakrama Yoga - Mars in 3rd house strong
        if (marsPos.house == 3 && (YogaHelpers.isExalted(marsPos) || YogaHelpers.isInOwnSign(marsPos) || YogaHelpers.isInFriendSign(marsPos))) {
            val strength = YogaHelpers.calculateYogaStrength(chart, listOf(marsPos))
            yogas.add(Yoga(
                name = "Parakrama Yoga",
                sanskritName = "पराक्रम योग",
                category = YogaCategory.SPECIAL_YOGA,
                planets = listOf(Planet.MARS),
                houses = listOf(3),
                description = "Strong Mars in 3rd house of valor",
                effects = "Exceptional courage, adventurous spirit, good relations with siblings, success through own efforts, skilled in communications and short travels",
                strength = YogaHelpers.strengthFromPercentage(strength),
                strengthPercentage = strength,
                isAuspicious = true,
                activationPeriod = "Mars Dasha periods",
                cancellationFactors = emptyList()
            ))
        }

        // 5. Bhratri Karaka Yoga - Mars as significator of siblings in good condition
        if (marsPos.house in listOf(3, 11) && !YogaHelpers.isDebilitated(marsPos)) {
            val lord3 = houseLords[3]
            val lord3Pos = chart.planetPositions.find { it.planet == lord3 }
            if (lord3Pos != null && (YogaHelpers.isExalted(lord3Pos) || YogaHelpers.isInOwnSign(lord3Pos))) {
                val strength = 70.0
                yogas.add(Yoga(
                    name = "Bhratri Karaka Yoga",
                    sanskritName = "भ्रातृ कारक योग",
                    category = YogaCategory.SPECIAL_YOGA,
                    planets = listOf(Planet.MARS, lord3!!),
                    houses = listOf(marsPos.house, lord3Pos.house),
                    description = "Mars well-placed and 3rd lord strong",
                    effects = "Good relations with siblings, helpful and supportive brothers, courage and initiative, success in team endeavors",
                    strength = YogaHelpers.strengthFromPercentage(strength),
                    strengthPercentage = strength,
                    isAuspicious = true,
                    activationPeriod = "Mars periods and 3rd lord periods",
                    cancellationFactors = emptyList()
                ))
            }
        }

        return yogas
    }

    // ==================== MERCURY-BASED YOGAS ====================

    private fun evaluateMercuryYogas(chart: VedicChart, houseLords: Map<Int, Planet>): List<Yoga> {
        val yogas = mutableListOf<Yoga>()
        val mercuryPos = chart.planetPositions.find { it.planet == Planet.MERCURY } ?: return yogas

        // 1. Bhadra Yoga is handled in MahapurushaYogaEvaluator - skip

        // 2. Vidya Yoga - Mercury in 4th or 5th house with Jupiter aspect
        val jupiterPos = chart.planetPositions.find { it.planet == Planet.JUPITER }
        if (mercuryPos.house in listOf(4, 5) && jupiterPos != null &&
            (YogaHelpers.isAspecting(jupiterPos, mercuryPos) || YogaHelpers.areConjunct(jupiterPos, mercuryPos))) {
            val strength = YogaHelpers.calculateYogaStrength(chart, listOf(mercuryPos, jupiterPos))
            yogas.add(Yoga(
                name = "Vidya Yoga",
                sanskritName = "विद्या योग",
                category = YogaCategory.SPECIAL_YOGA,
                planets = listOf(Planet.MERCURY, Planet.JUPITER),
                houses = listOf(mercuryPos.house),
                description = "Mercury in 4th/5th house with Jupiter influence",
                effects = "Excellent education, scholarly achievements, proficiency in multiple subjects, good at teaching, sharp intellect, success in examinations",
                strength = YogaHelpers.strengthFromPercentage(strength),
                strengthPercentage = strength,
                isAuspicious = true,
                activationPeriod = "Mercury-Jupiter or Jupiter-Mercury periods",
                cancellationFactors = emptyList()
            ))
        }

        // 3. Nipuna Yoga - Mercury exalted or own sign in Kendra
        if ((YogaHelpers.isExalted(mercuryPos) || YogaHelpers.isInOwnSign(mercuryPos)) &&
            mercuryPos.house in listOf(1, 4, 7, 10)) {
            val strength = 80.0
            yogas.add(Yoga(
                name = "Nipuna Yoga",
                sanskritName = "निपुण योग",
                category = YogaCategory.SPECIAL_YOGA,
                planets = listOf(Planet.MERCURY),
                houses = listOf(mercuryPos.house),
                description = "Mercury strong in Kendra house",
                effects = "Skilled in many arts and sciences, quick learner, excellent communication, success in commerce, writing, and intellectual pursuits",
                strength = YogaHelpers.strengthFromPercentage(strength),
                strengthPercentage = strength,
                isAuspicious = true,
                activationPeriod = "Mercury Dasha periods",
                cancellationFactors = if (YogaHelpers.getCombustionFactor(mercuryPos, chart) < 0.7)
                    listOf("Combustion reduces Mercury's expression") else emptyList()
            ))
        }

        // 4. Vanijya Yoga - Mercury in 2nd, 10th, or 11th house strong
        if (mercuryPos.house in listOf(2, 10, 11) && !YogaHelpers.isDebilitated(mercuryPos)) {
            val combustionFactor = YogaHelpers.getCombustionFactor(mercuryPos, chart)
            if (combustionFactor > 0.6) {
                val strength = YogaHelpers.calculateYogaStrength(chart, listOf(mercuryPos))
                yogas.add(Yoga(
                    name = "Vanijya Yoga",
                    sanskritName = "वणिज्य योग",
                    category = YogaCategory.DHANA_YOGA,
                    planets = listOf(Planet.MERCURY),
                    houses = listOf(mercuryPos.house),
                    description = "Mercury in wealth/career houses (${mercuryPos.house})",
                    effects = "Success in business and commerce, skilled trader, profits through intellectual work, good financial acumen, success in accounting, brokerage, or communications",
                    strength = YogaHelpers.strengthFromPercentage(strength),
                    strengthPercentage = strength,
                    isAuspicious = true,
                    activationPeriod = "Mercury Dasha periods",
                    cancellationFactors = emptyList()
                ))
            }
        }

        // 5. Matimanta Yoga - Mercury with Moon in good houses
        val moonPos = chart.planetPositions.find { it.planet == Planet.MOON }
        if (moonPos != null && YogaHelpers.areConjunct(mercuryPos, moonPos)) {
            val strength = YogaHelpers.calculateYogaStrength(chart, listOf(mercuryPos, moonPos))
            yogas.add(Yoga(
                name = "Matimanta Yoga",
                sanskritName = "मतिमंत योग",
                category = YogaCategory.SPECIAL_YOGA,
                planets = listOf(Planet.MERCURY, Planet.MOON),
                houses = listOf(mercuryPos.house),
                description = "Mercury conjunct Moon",
                effects = "Intelligent and emotionally balanced, good memory, imaginative thinking, success in creative writing, counseling, or psychology",
                strength = YogaHelpers.strengthFromPercentage(strength),
                strengthPercentage = strength,
                isAuspicious = true,
                activationPeriod = "Mercury-Moon or Moon-Mercury periods",
                cancellationFactors = emptyList()
            ))
        }

        return yogas
    }

    // ==================== JUPITER-BASED YOGAS ====================

    private fun evaluateJupiterYogas(chart: VedicChart, houseLords: Map<Int, Planet>): List<Yoga> {
        val yogas = mutableListOf<Yoga>()
        val jupiterPos = chart.planetPositions.find { it.planet == Planet.JUPITER } ?: return yogas

        // 1. Hamsa Yoga is handled in MahapurushaYogaEvaluator - skip

        // 2. Guru Chandala Yoga is handled in NegativeYogaEvaluator - skip

        // 3. Guru Mangala Yoga - Jupiter conjunct Mars
        val marsPos = chart.planetPositions.find { it.planet == Planet.MARS }
        if (marsPos != null && YogaHelpers.areConjunct(jupiterPos, marsPos)) {
            val strength = YogaHelpers.calculateYogaStrength(chart, listOf(jupiterPos, marsPos))
            yogas.add(Yoga(
                name = "Guru Mangala Yoga",
                sanskritName = "गुरु मंगल योग",
                category = YogaCategory.SPECIAL_YOGA,
                planets = listOf(Planet.JUPITER, Planet.MARS),
                houses = listOf(jupiterPos.house),
                description = "Jupiter conjunct Mars",
                effects = "Righteous warrior, success in law enforcement, military with ethics, courage guided by wisdom, successful entrepreneur, good for real estate",
                strength = YogaHelpers.strengthFromPercentage(strength),
                strengthPercentage = strength,
                isAuspicious = true,
                activationPeriod = "Jupiter-Mars or Mars-Jupiter periods",
                cancellationFactors = emptyList()
            ))
        }

        // 4. Dharma Yoga - Jupiter in 1st, 5th, or 9th house
        if (jupiterPos.house in listOf(1, 5, 9)) {
            val strength = YogaHelpers.calculateYogaStrength(chart, listOf(jupiterPos)) * 1.1
            yogas.add(Yoga(
                name = "Dharma Yoga",
                sanskritName = "धर्म योग",
                category = YogaCategory.SPECIAL_YOGA,
                planets = listOf(Planet.JUPITER),
                houses = listOf(jupiterPos.house),
                description = "Jupiter in Trikona house ${jupiterPos.house}",
                effects = "Religious and spiritual inclinations, righteous conduct, good fortune, blessings from teachers and elders, prosperity through ethical means",
                strength = YogaHelpers.strengthFromPercentage(strength.coerceIn(10.0, 100.0)),
                strengthPercentage = strength.coerceIn(10.0, 100.0),
                isAuspicious = true,
                activationPeriod = "Jupiter Dasha periods",
                cancellationFactors = if (YogaHelpers.isDebilitated(jupiterPos))
                    listOf("Jupiter debilitated reduces spiritual inclinations") else emptyList()
            ))
        }

        // 5. Brahma Yoga - Jupiter in Kendra from Atmakaraka
        // Simplified: Jupiter in Kendra with strong dignity
        if (jupiterPos.house in listOf(1, 4, 7, 10) &&
            (YogaHelpers.isExalted(jupiterPos) || YogaHelpers.isInOwnSign(jupiterPos))) {
            val strength = 85.0
            yogas.add(Yoga(
                name = "Brahma Yoga",
                sanskritName = "ब्रह्म योग",
                category = YogaCategory.SPECIAL_YOGA,
                planets = listOf(Planet.JUPITER),
                houses = listOf(jupiterPos.house),
                description = "Strong Jupiter in Kendra house",
                effects = "Vedic knowledge, respect from learned people, teaching abilities, priestly qualities, long life, moral authority, spiritual wisdom",
                strength = YogaHelpers.strengthFromPercentage(strength),
                strengthPercentage = strength,
                isAuspicious = true,
                activationPeriod = "Jupiter Dasha periods",
                cancellationFactors = emptyList()
            ))
        }

        // 6. Ganapati Yoga - Jupiter ruling 5th in good dignity
        val lord5 = houseLords[5]
        if (lord5 == Planet.JUPITER) {
            val strength = YogaHelpers.calculateYogaStrength(chart, listOf(jupiterPos))
            if (jupiterPos.house in listOf(1, 4, 5, 7, 9, 10, 11)) {
                yogas.add(Yoga(
                    name = "Ganapati Yoga",
                    sanskritName = "गणपति योग",
                    category = YogaCategory.SPECIAL_YOGA,
                    planets = listOf(Planet.JUPITER),
                    houses = listOf(jupiterPos.house),
                    description = "Jupiter as 5th lord well-placed",
                    effects = "Blessed with intelligence, good children, success in creative ventures, divine grace, removal of obstacles, wisdom in investments",
                    strength = YogaHelpers.strengthFromPercentage(strength),
                    strengthPercentage = strength,
                    isAuspicious = true,
                    activationPeriod = "Jupiter Dasha periods",
                    cancellationFactors = emptyList()
                ))
            }
        }

        return yogas
    }

    // ==================== VENUS-BASED YOGAS ====================

    private fun evaluateVenusYogas(chart: VedicChart, houseLords: Map<Int, Planet>): List<Yoga> {
        val yogas = mutableListOf<Yoga>()
        val venusPos = chart.planetPositions.find { it.planet == Planet.VENUS } ?: return yogas

        // 1. Malavya Yoga is handled in MahapurushaYogaEvaluator - skip

        // 2. Bharathi Yoga - Venus exalted with Mercury aspect
        val mercuryPos = chart.planetPositions.find { it.planet == Planet.MERCURY }
        if (YogaHelpers.isExalted(venusPos) && mercuryPos != null &&
            YogaHelpers.isAspecting(mercuryPos, venusPos)) {
            val strength = 80.0
            yogas.add(Yoga(
                name = "Bharathi Yoga",
                sanskritName = "भारती योग",
                category = YogaCategory.SPECIAL_YOGA,
                planets = listOf(Planet.VENUS, Planet.MERCURY),
                houses = listOf(venusPos.house),
                description = "Exalted Venus with Mercury aspect",
                effects = "Exceptional artistic abilities, fame in creative fields, eloquent speaker, knowledge of fine arts, success in entertainment industry",
                strength = YogaHelpers.strengthFromPercentage(strength),
                strengthPercentage = strength,
                isAuspicious = true,
                activationPeriod = "Venus-Mercury or Mercury-Venus periods",
                cancellationFactors = emptyList()
            ))
        }

        // 3. Kamini Yoga - Venus in own sign with Moon aspect
        val moonPos = chart.planetPositions.find { it.planet == Planet.MOON }
        if ((venusPos.sign == ZodiacSign.TAURUS || venusPos.sign == ZodiacSign.LIBRA) &&
            moonPos != null && (YogaHelpers.isAspecting(moonPos, venusPos) || YogaHelpers.areConjunct(moonPos, venusPos))) {
            val strength = 70.0
            yogas.add(Yoga(
                name = "Kamini Yoga",
                sanskritName = "कामिनी योग",
                category = YogaCategory.SPECIAL_YOGA,
                planets = listOf(Planet.VENUS, Planet.MOON),
                houses = listOf(venusPos.house),
                description = "Venus in own sign with Moon influence",
                effects = "Attractive personality, romantic nature, appreciation for beauty, happy married life, luxury and comforts, success in beauty/fashion industries",
                strength = YogaHelpers.strengthFromPercentage(strength),
                strengthPercentage = strength,
                isAuspicious = true,
                activationPeriod = "Venus-Moon or Moon-Venus periods",
                cancellationFactors = emptyList()
            ))
        }

        // 4. Kalatra Yoga - Venus as 7th lord well-placed
        val lord7 = houseLords[7]
        if (lord7 == Planet.VENUS && venusPos.house in listOf(1, 4, 5, 7, 9, 10)) {
            val strength = YogaHelpers.calculateYogaStrength(chart, listOf(venusPos))
            yogas.add(Yoga(
                name = "Kalatra Yoga",
                sanskritName = "कलत्र योग",
                category = YogaCategory.SPECIAL_YOGA,
                planets = listOf(Planet.VENUS),
                houses = listOf(venusPos.house),
                description = "Venus as 7th lord in auspicious house",
                effects = "Happy marriage, attractive and supportive spouse, success through partnerships, good social connections, harmonious relationships",
                strength = YogaHelpers.strengthFromPercentage(strength),
                strengthPercentage = strength,
                isAuspicious = true,
                activationPeriod = "Venus Dasha periods",
                cancellationFactors = emptyList()
            ))
        }

        // 5. Mridanga Yoga - Venus in 7th with Jupiter aspect
        val jupiterPos = chart.planetPositions.find { it.planet == Planet.JUPITER }
        if (venusPos.house == 7 && jupiterPos != null && YogaHelpers.isAspecting(jupiterPos, venusPos)) {
            val strength = 75.0
            yogas.add(Yoga(
                name = "Mridanga Yoga",
                sanskritName = "मृदंग योग",
                category = YogaCategory.SPECIAL_YOGA,
                planets = listOf(Planet.VENUS, Planet.JUPITER),
                houses = listOf(7),
                description = "Venus in 7th with Jupiter aspect",
                effects = "Fame and recognition, musical abilities, success in entertainment, happy marriage with spiritual partner, social prominence",
                strength = YogaHelpers.strengthFromPercentage(strength),
                strengthPercentage = strength,
                isAuspicious = true,
                activationPeriod = "Venus-Jupiter or Jupiter-Venus periods",
                cancellationFactors = emptyList()
            ))
        }

        // 6. Shukra Yoga - Venus in 2nd or 11th strong
        if (venusPos.house in listOf(2, 11) && !YogaHelpers.isDebilitated(venusPos)) {
            val strength = YogaHelpers.calculateYogaStrength(chart, listOf(venusPos))
            yogas.add(Yoga(
                name = "Shukra Yoga",
                sanskritName = "शुक्र योग",
                category = YogaCategory.DHANA_YOGA,
                planets = listOf(Planet.VENUS),
                houses = listOf(venusPos.house),
                description = "Venus in wealth house ${venusPos.house}",
                effects = "Wealth through arts, beauty, or entertainment, luxurious lifestyle, good taste, success in luxury goods business, pleasant speech",
                strength = YogaHelpers.strengthFromPercentage(strength),
                strengthPercentage = strength,
                isAuspicious = true,
                activationPeriod = "Venus Dasha periods",
                cancellationFactors = if (YogaHelpers.getCombustionFactor(venusPos, chart) < 0.7)
                    listOf("Venus combust reduces material gains") else emptyList()
            ))
        }

        return yogas
    }

    // ==================== SATURN-BASED YOGAS ====================

    private fun evaluateSaturnYogas(chart: VedicChart, houseLords: Map<Int, Planet>): List<Yoga> {
        val yogas = mutableListOf<Yoga>()
        val saturnPos = chart.planetPositions.find { it.planet == Planet.SATURN } ?: return yogas

        // 1. Sasa Yoga is handled in MahapurushaYogaEvaluator - skip

        // 2. Shrapit Yoga is handled in NegativeYogaEvaluator - skip

        // 3. Pravrajya Yoga (Saturn variant) - Saturn strong in 12th with spiritual combinations
        if (saturnPos.house == 12 && (YogaHelpers.isExalted(saturnPos) || YogaHelpers.isInOwnSign(saturnPos))) {
            val jupiterPos = chart.planetPositions.find { it.planet == Planet.JUPITER }
            if (jupiterPos != null && (jupiterPos.house in listOf(1, 5, 9) ||
                YogaHelpers.isAspecting(jupiterPos, saturnPos))) {
                val strength = 70.0
                yogas.add(Yoga(
                    name = "Pravrajya Yoga (Saturn)",
                    sanskritName = "प्रव्रज्या योग",
                    category = YogaCategory.SPECIAL_YOGA,
                    planets = listOf(Planet.SATURN, Planet.JUPITER),
                    houses = listOf(12),
                    description = "Strong Saturn in 12th with Jupiter influence",
                    effects = "Deep spiritual inclinations, possible renunciation, meditation and contemplation, success in foreign lands, enlightenment through discipline",
                    strength = YogaHelpers.strengthFromPercentage(strength),
                    strengthPercentage = strength,
                    isAuspicious = true,
                    activationPeriod = "Saturn-Jupiter or Jupiter-Saturn periods",
                    cancellationFactors = listOf("May manifest as spiritual interest rather than full renunciation")
                ))
            }
        }

        // 4. Shani Yoga - Saturn in 3rd, 6th, or 11th (Upachaya) strong
        if (saturnPos.house in listOf(3, 6, 11) && !YogaHelpers.isDebilitated(saturnPos)) {
            val strength = YogaHelpers.calculateYogaStrength(chart, listOf(saturnPos))
            yogas.add(Yoga(
                name = "Shani Yoga",
                sanskritName = "शनि योग",
                category = YogaCategory.SPECIAL_YOGA,
                planets = listOf(Planet.SATURN),
                houses = listOf(saturnPos.house),
                description = "Saturn in Upachaya house ${saturnPos.house}",
                effects = "Success through perseverance, long-term gains, victory over enemies, good for service sector, discipline brings rewards over time",
                strength = YogaHelpers.strengthFromPercentage(strength),
                strengthPercentage = strength,
                isAuspicious = true,
                activationPeriod = "Saturn Dasha periods (results mature after age 36)",
                cancellationFactors = emptyList()
            ))
        }

        // 5. Dur Yoga - Saturn in 8th house afflicted
        if (saturnPos.house == 8 && YogaHelpers.isDebilitated(saturnPos)) {
            val strength = 50.0
            yogas.add(Yoga(
                name = "Dur Yoga",
                sanskritName = "दुर् योग",
                category = YogaCategory.NEGATIVE_YOGA,
                planets = listOf(Planet.SATURN),
                houses = listOf(8),
                description = "Debilitated Saturn in 8th house",
                effects = "Chronic health issues possible, difficulties with inheritance, need for transformation through challenges, longevity concerns require attention",
                strength = YogaHelpers.strengthFromPercentage(strength),
                strengthPercentage = strength,
                isAuspicious = false,
                activationPeriod = "Saturn Dasha periods",
                cancellationFactors = listOf("Jupiter aspect significantly reduces negative effects", "Spiritual practices provide relief")
            ))
        }

        // 6. Ayush Yoga - Saturn in 1st, 5th, 9th, or 10th strong
        if (saturnPos.house in listOf(1, 5, 9, 10) &&
            (YogaHelpers.isExalted(saturnPos) || YogaHelpers.isInOwnSign(saturnPos))) {
            val strength = 75.0
            yogas.add(Yoga(
                name = "Ayush Yoga (Saturn)",
                sanskritName = "आयुष योग",
                category = YogaCategory.SPECIAL_YOGA,
                planets = listOf(Planet.SATURN),
                houses = listOf(saturnPos.house),
                description = "Strong Saturn in auspicious house",
                effects = "Long life, slow but steady success, respected elder, wisdom through experience, authority in later life, disciplined nature",
                strength = YogaHelpers.strengthFromPercentage(strength),
                strengthPercentage = strength,
                isAuspicious = true,
                activationPeriod = "Saturn Dasha periods, especially after age 36",
                cancellationFactors = emptyList()
            ))
        }

        return yogas
    }

    // ==================== RAHU-KETU YOGAS ====================

    private fun evaluateRahuKetuYogas(chart: VedicChart, houseLords: Map<Int, Planet>): List<Yoga> {
        val yogas = mutableListOf<Yoga>()
        val rahuPos = chart.planetPositions.find { it.planet == Planet.RAHU }
        val ketuPos = chart.planetPositions.find { it.planet == Planet.KETU }

        // 1. Kala Sarpa Yoga is handled in NegativeYogaEvaluator - skip

        // 2. Rahu in good houses
        if (rahuPos != null) {
            // Rahu in 3rd, 6th, 10th, 11th - generally favorable
            if (rahuPos.house in listOf(3, 6, 10, 11)) {
                val strength = 65.0
                val effects = when (rahuPos.house) {
                    3 -> "Courage through unconventional means, success in media/communications, adventurous nature"
                    6 -> "Victory over enemies, success in competition, good for healthcare/service"
                    10 -> "Sudden rise in career, fame through unconventional paths, success in technology/foreign companies"
                    11 -> "Gains through large networks, success with masses, wealth from foreign sources"
                    else -> ""
                }
                yogas.add(Yoga(
                    name = "Rahu Upachaya Yoga",
                    sanskritName = "राहु उपचय योग",
                    category = YogaCategory.SPECIAL_YOGA,
                    planets = listOf(Planet.RAHU),
                    houses = listOf(rahuPos.house),
                    description = "Rahu in Upachaya house ${rahuPos.house}",
                    effects = effects,
                    strength = YogaHelpers.strengthFromPercentage(strength),
                    strengthPercentage = strength,
                    isAuspicious = true,
                    activationPeriod = "Rahu Dasha periods",
                    cancellationFactors = emptyList()
                ))
            }

            // 3. Rahu with Jupiter - Modified Guru Chandala (already in NegativeYoga)
            // But if both are strong, can give unconventional spiritual path
            val jupiterPos = chart.planetPositions.find { it.planet == Planet.JUPITER }
            if (jupiterPos != null && YogaHelpers.areConjunct(rahuPos, jupiterPos) &&
                (YogaHelpers.isExalted(jupiterPos) || YogaHelpers.isInOwnSign(jupiterPos))) {
                val strength = 60.0
                yogas.add(Yoga(
                    name = "Vijnaana Yoga",
                    sanskritName = "विज्ञान योग",
                    category = YogaCategory.SPECIAL_YOGA,
                    planets = listOf(Planet.RAHU, Planet.JUPITER),
                    houses = listOf(rahuPos.house),
                    description = "Rahu with strong Jupiter",
                    effects = "Scientific mindset, research abilities, unconventional wisdom, success in technology and innovation, interest in foreign philosophies",
                    strength = YogaHelpers.strengthFromPercentage(strength),
                    strengthPercentage = strength,
                    isAuspicious = true,
                    activationPeriod = "Rahu-Jupiter or Jupiter-Rahu periods",
                    cancellationFactors = listOf("Jupiter's benefic nature modified by Rahu - unconventional expression")
                ))
            }
        }

        // 4. Ketu Yogas
        if (ketuPos != null) {
            // Ketu in 12th - Excellent for moksha
            if (ketuPos.house == 12) {
                val jupiterPos = chart.planetPositions.find { it.planet == Planet.JUPITER }
                val jupiterInfluence = jupiterPos != null &&
                    (YogaHelpers.isAspecting(jupiterPos, ketuPos) || jupiterPos.house in listOf(1, 5, 9))
                val strength = if (jupiterInfluence) 80.0 else 65.0
                yogas.add(Yoga(
                    name = "Moksha Yoga (Ketu)",
                    sanskritName = "मोक्ष योग",
                    category = YogaCategory.SPECIAL_YOGA,
                    planets = listOf(Planet.KETU),
                    houses = listOf(12),
                    description = "Ketu in 12th house of liberation",
                    effects = "Strong spiritual inclinations, detachment from material world, meditation abilities, potential for enlightenment, interest in past lives",
                    strength = YogaHelpers.strengthFromPercentage(strength),
                    strengthPercentage = strength,
                    isAuspicious = true,
                    activationPeriod = "Ketu Dasha periods",
                    cancellationFactors = if (!jupiterInfluence)
                        listOf("Jupiter influence enhances spiritual benefits") else emptyList()
                ))
            }

            // Ketu in 9th - Tapasvi nature
            if (ketuPos.house == 9) {
                val strength = 70.0
                yogas.add(Yoga(
                    name = "Tapasvi Yoga (Ketu)",
                    sanskritName = "तपस्वी योग",
                    category = YogaCategory.SPECIAL_YOGA,
                    planets = listOf(Planet.KETU),
                    houses = listOf(9),
                    description = "Ketu in 9th house of dharma",
                    effects = "Ascetic tendencies, disinterest in organized religion, direct spiritual experiences, past life spiritual merits, pilgrimage inclinations",
                    strength = YogaHelpers.strengthFromPercentage(strength),
                    strengthPercentage = strength,
                    isAuspicious = true,
                    activationPeriod = "Ketu Dasha periods",
                    cancellationFactors = emptyList()
                ))
            }
        }

        return yogas
    }

    // ==================== DIGNITY PATTERN YOGAS ====================

    private fun evaluateDignityYogas(chart: VedicChart): List<Yoga> {
        val yogas = mutableListOf<Yoga>()

        // 1. Multiple Exaltations - 3+ planets exalted
        val exaltedPlanets = chart.planetPositions.filter { YogaHelpers.isExalted(it) }
        if (exaltedPlanets.size >= 3) {
            val strength = 85.0 + (exaltedPlanets.size - 3) * 5.0
            yogas.add(Yoga(
                name = "Uttama Grahas Yoga",
                sanskritName = "उत्तम ग्रह योग",
                category = YogaCategory.SPECIAL_YOGA,
                planets = exaltedPlanets.map { it.planet },
                houses = exaltedPlanets.map { it.house },
                description = "${exaltedPlanets.size} planets exalted: ${exaltedPlanets.joinToString { it.planet.displayName }}",
                effects = "Exceptional fortune, multiple areas of excellence, natural authority, blessed life with many talents and achievements",
                strength = YogaHelpers.strengthFromPercentage(strength.coerceIn(10.0, 100.0)),
                strengthPercentage = strength.coerceIn(10.0, 100.0),
                isAuspicious = true,
                activationPeriod = "Periods of any exalted planet",
                cancellationFactors = emptyList()
            ))
        }

        // 2. Multiple Own Signs - 3+ planets in own sign
        val ownSignPlanets = chart.planetPositions.filter { YogaHelpers.isInOwnSign(it) }
        if (ownSignPlanets.size >= 3) {
            val strength = 80.0 + (ownSignPlanets.size - 3) * 4.0
            yogas.add(Yoga(
                name = "Swagraha Yoga",
                sanskritName = "स्वग्रह योग",
                category = YogaCategory.SPECIAL_YOGA,
                planets = ownSignPlanets.map { it.planet },
                houses = ownSignPlanets.map { it.house },
                description = "${ownSignPlanets.size} planets in own signs: ${ownSignPlanets.joinToString { it.planet.displayName }}",
                effects = "Self-made success, strong in multiple areas, comfortable expression of talents, stable and reliable achievements",
                strength = YogaHelpers.strengthFromPercentage(strength.coerceIn(10.0, 100.0)),
                strengthPercentage = strength.coerceIn(10.0, 100.0),
                isAuspicious = true,
                activationPeriod = "Periods of planets in own signs",
                cancellationFactors = emptyList()
            ))
        }

        // 3. Multiple Debilitations - 3+ planets debilitated
        val debilitatedPlanets = chart.planetPositions.filter {
            YogaHelpers.isDebilitated(it) && !YogaHelpers.hasNeechaBhanga(it, chart)
        }
        if (debilitatedPlanets.size >= 3) {
            val strength = 55.0 + (debilitatedPlanets.size - 3) * 5.0
            yogas.add(Yoga(
                name = "Neecha Grahas Yoga",
                sanskritName = "नीच ग्रह योग",
                category = YogaCategory.NEGATIVE_YOGA,
                planets = debilitatedPlanets.map { it.planet },
                houses = debilitatedPlanets.map { it.house },
                description = "${debilitatedPlanets.size} planets debilitated without cancellation",
                effects = "Challenges in multiple life areas, need to work harder for results, humility lessons, growth through difficulties",
                strength = YogaHelpers.strengthFromPercentage(strength.coerceIn(10.0, 100.0)),
                strengthPercentage = strength.coerceIn(10.0, 100.0),
                isAuspicious = false,
                activationPeriod = "Periods of debilitated planets require extra care",
                cancellationFactors = listOf("Strong Jupiter aspects can help", "Good Dasha can mitigate effects")
            ))
        }

        // 4. Vargottama - Planet in same sign in D1 and D9
        // This requires navamsha calculation - simplified check based on longitude
        chart.planetPositions.forEach { pos ->
            val d1Sign = pos.sign
            // Calculate D9 sign (simplified - actual implementation should use DivisionalChartCalculator)
            val navamshaNumber = ((pos.longitude % 30) / 3.333333).toInt()
            val navamshaStartSign = when (d1Sign.element) {
                ZodiacSign.Element.FIRE -> ZodiacSign.ARIES
                ZodiacSign.Element.EARTH -> ZodiacSign.CAPRICORN
                ZodiacSign.Element.AIR -> ZodiacSign.LIBRA
                ZodiacSign.Element.WATER -> ZodiacSign.CANCER
            }
            val d9SignIndex = (navamshaStartSign.ordinal + navamshaNumber) % 12
            val d9Sign = ZodiacSign.entries[d9SignIndex]

            if (d1Sign == d9Sign) {
                val baseStrength = YogaHelpers.calculateYogaStrength(chart, listOf(pos))
                val strength = baseStrength * 1.15
                yogas.add(Yoga(
                    name = "Vargottama ${pos.planet.displayName}",
                    sanskritName = "वर्गोत्तम ${pos.planet.displayName}",
                    category = YogaCategory.SPECIAL_YOGA,
                    planets = listOf(pos.planet),
                    houses = listOf(pos.house),
                    description = "${pos.planet.displayName} in same sign (${d1Sign.displayName}) in D1 and D9",
                    effects = "Planet greatly strengthened, gives excellent results in its significations, stable and enduring effects, like a planet in exaltation",
                    strength = YogaHelpers.strengthFromPercentage(strength.coerceIn(10.0, 100.0)),
                    strengthPercentage = strength.coerceIn(10.0, 100.0),
                    isAuspicious = true,
                    activationPeriod = "${pos.planet.displayName} Dasha periods",
                    cancellationFactors = emptyList()
                ))
            }
        }

        return yogas
    }

    // ==================== HOUSE POSITION YOGAS ====================

    private fun evaluateHousePositionYogas(chart: VedicChart, houseLords: Map<Int, Planet>): List<Yoga> {
        val yogas = mutableListOf<Yoga>()

        // 1. Subhapati Yoga - All Kendras occupied by benefics only
        val benefics = listOf(Planet.JUPITER, Planet.VENUS, Planet.MERCURY, Planet.MOON)
        val kendras = listOf(1, 4, 7, 10)

        val planetsInKendras = chart.planetPositions.filter { it.house in kendras }
        val allKendrasBenefic = planetsInKendras.all { it.planet in benefics }

        if (planetsInKendras.size >= 3 && allKendrasBenefic) {
            val strength = YogaHelpers.calculateYogaStrength(chart, planetsInKendras)
            yogas.add(Yoga(
                name = "Subhapati Yoga",
                sanskritName = "शुभपति योग",
                category = YogaCategory.SPECIAL_YOGA,
                planets = planetsInKendras.map { it.planet },
                houses = planetsInKendras.map { it.house },
                description = "Kendras occupied by benefics only",
                effects = "Overall auspicious life, protected from major troubles, good health, peaceful mind, supportive environment",
                strength = YogaHelpers.strengthFromPercentage(strength),
                strengthPercentage = strength,
                isAuspicious = true,
                activationPeriod = "Throughout life, especially during benefic periods",
                cancellationFactors = emptyList()
            ))
        }

        // 2. Papa Kartari on specific houses
        val malefics = listOf(Planet.SATURN, Planet.MARS, Planet.RAHU, Planet.KETU, Planet.SUN)

        // Check Papa Kartari on 2nd house (wealth and speech)
        val planetsInH1 = chart.planetPositions.filter { it.house == 1 }
        val planetsInH3 = chart.planetPositions.filter { it.house == 3 }
        val maleficsIn1 = planetsInH1.any { it.planet in malefics }
        val maleficsIn3 = planetsInH3.any { it.planet in malefics }

        if (maleficsIn1 && maleficsIn3) {
            val strength = 55.0
            yogas.add(Yoga(
                name = "Dhana Papa Kartari",
                sanskritName = "धन पाप कर्तरी",
                category = YogaCategory.NEGATIVE_YOGA,
                planets = (planetsInH1.filter { it.planet in malefics } +
                          planetsInH3.filter { it.planet in malefics }).map { it.planet },
                houses = listOf(1, 2, 3),
                description = "2nd house hemmed by malefics in 1st and 3rd",
                effects = "Challenges with family wealth, need to work hard for financial stability, speech may be harsh, family disharmony possible",
                strength = YogaHelpers.strengthFromPercentage(strength),
                strengthPercentage = strength,
                isAuspicious = false,
                activationPeriod = "Periods of malefics involved",
                cancellationFactors = listOf("Jupiter aspect on 2nd house mitigates effects", "Strong 2nd lord helps")
            ))
        }

        // 3. Trigraha Yoga - Three planets in one house
        val houseCounts = chart.planetPositions
            .filter { it.planet in listOf(Planet.SUN, Planet.MOON, Planet.MARS, Planet.MERCURY,
                                          Planet.JUPITER, Planet.VENUS, Planet.SATURN) }
            .groupBy { it.house }

        houseCounts.filter { it.value.size >= 3 }.forEach { (house, positions) ->
            val planets = positions.map { it.planet }
            val strength = YogaHelpers.calculateYogaStrength(chart, positions)
            val isAuspicious = house in listOf(1, 4, 5, 7, 9, 10, 11)

            yogas.add(Yoga(
                name = "Trigraha Yoga",
                sanskritName = "त्रिग्रह योग",
                category = if (isAuspicious) YogaCategory.SPECIAL_YOGA else YogaCategory.NEGATIVE_YOGA,
                planets = planets,
                houses = listOf(house),
                description = "${positions.size} planets conjunct in house $house: ${planets.joinToString { it.displayName }}",
                effects = if (isAuspicious)
                    "Concentrated energy in house $house matters, potential for significant achievements in related areas, focus and intensity"
                else
                    "Challenges related to house $house matters, scattered energies, need to balance multiple demands",
                strength = YogaHelpers.strengthFromPercentage(strength),
                strengthPercentage = strength,
                isAuspicious = isAuspicious,
                activationPeriod = "Periods of any planet in the conjunction",
                cancellationFactors = if (!isAuspicious)
                    listOf("Benefic aspects can moderate effects") else emptyList()
            ))
        }

        // 4. Grahayuddha - Planetary War (planets within 1° of each other)
        val mainPlanets = chart.planetPositions.filter {
            it.planet in listOf(Planet.MARS, Planet.MERCURY, Planet.JUPITER, Planet.VENUS, Planet.SATURN)
        }

        for (i in mainPlanets.indices) {
            for (j in i + 1 until mainPlanets.size) {
                val distance = kotlin.math.abs(mainPlanets[i].longitude - mainPlanets[j].longitude)
                val normalizedDistance = if (distance > 180) 360 - distance else distance

                if (normalizedDistance <= 1.0) {
                    // Determine winner based on brightness (simplified - Venus > Jupiter > Mars > Saturn > Mercury)
                    val brightnessRank = mapOf(
                        Planet.VENUS to 5,
                        Planet.JUPITER to 4,
                        Planet.MARS to 3,
                        Planet.SATURN to 2,
                        Planet.MERCURY to 1
                    )
                    val winner = if ((brightnessRank[mainPlanets[i].planet] ?: 0) >
                                    (brightnessRank[mainPlanets[j].planet] ?: 0))
                                    mainPlanets[i] else mainPlanets[j]
                    val loser = if (winner == mainPlanets[i]) mainPlanets[j] else mainPlanets[i]

                    val strength = 45.0
                    yogas.add(Yoga(
                        name = "Grahayuddha",
                        sanskritName = "ग्रहयुद्ध",
                        category = YogaCategory.NEGATIVE_YOGA,
                        planets = listOf(mainPlanets[i].planet, mainPlanets[j].planet),
                        houses = listOf(mainPlanets[i].house),
                        description = "${mainPlanets[i].planet.displayName} and ${mainPlanets[j].planet.displayName} in planetary war (${String.format("%.2f", normalizedDistance)}° apart)",
                        effects = "${loser.planet.displayName} loses the war and gives diminished results. ${winner.planet.displayName} gains strength. Conflict in matters ruled by both planets.",
                        strength = YogaHelpers.strengthFromPercentage(strength),
                        strengthPercentage = strength,
                        isAuspicious = false,
                        activationPeriod = "Periods of either planet involved",
                        cancellationFactors = listOf("Winner planet gives better results", "Loser planet's results are weakened")
                    ))
                }
            }
        }

        return yogas
    }
}
