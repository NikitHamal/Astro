package com.astro.storm.ephemeris.yoga

import com.astro.storm.core.model.Nakshatra
import com.astro.storm.core.model.Planet
import com.astro.storm.core.model.PlanetPosition
import com.astro.storm.core.model.VedicChart
import com.astro.storm.core.model.ZodiacSign

/**
 * Nakshatra Yoga Evaluator - Lunar Mansion Based Combinations
 *
 * This evaluator handles yogas formed based on Nakshatra (lunar mansion) positions.
 * Nakshatras are fundamental to Vedic astrology and provide deep insights into
 * personality, karma, and life patterns.
 *
 * Categories covered:
 * 1. Janma Nakshatra Yogas - Birth star combinations
 * 2. Moon Nakshatra Yogas - Lunar mansion patterns
 * 3. Nakshatra Lord Yogas - Based on Nakshatra ruling planets
 * 4. Nakshatra Pada Yogas - Quarter-based combinations
 * 5. Pushya Nakshatra Yogas - Auspicious star combinations
 * 6. Mula Nakshatra Yogas - Root star patterns
 * 7. Gandanta Yogas - Junction point combinations
 * 8. Abhijit Nakshatra - 28th nakshatra special combinations
 * 9. Nakshatra Compatibility Patterns
 * 10. Deity-based Nakshatra Yogas
 *
 * Based on classical texts:
 * - Brihat Parasara Hora Shastra (BPHS), Chapter 84-86
 * - Muhurta Chintamani
 * - Jataka Parijata, Chapter 2
 * - Hora Sara
 *
 * @author AstroStorm
 */
class NakshatraYogaEvaluator : YogaEvaluator {

    override val category: YogaCategory = YogaCategory.SPECIAL_YOGA

    // Nakshatra deity and ruling planet mapping
    private val nakshatraDeities = mapOf(
        Nakshatra.ASHWINI to "Ashwini Kumaras",
        Nakshatra.BHARANI to "Yama",
        Nakshatra.KRITTIKA to "Agni",
        Nakshatra.ROHINI to "Brahma",
        Nakshatra.MRIGASHIRA to "Soma",
        Nakshatra.ARDRA to "Rudra",
        Nakshatra.PUNARVASU to "Aditi",
        Nakshatra.PUSHYA to "Brihaspati",
        Nakshatra.ASHLESHA to "Nagas",
        Nakshatra.MAGHA to "Pitris",
        Nakshatra.PURVA_PHALGUNI to "Bhaga",
        Nakshatra.UTTARA_PHALGUNI to "Aryaman",
        Nakshatra.HASTA to "Savitar",
        Nakshatra.CHITRA to "Tvashtar",
        Nakshatra.SWATI to "Vayu",
        Nakshatra.VISHAKHA to "Indra-Agni",
        Nakshatra.ANURADHA to "Mitra",
        Nakshatra.JYESHTHA to "Indra",
        Nakshatra.MULA to "Nirriti",
        Nakshatra.PURVA_ASHADHA to "Apas",
        Nakshatra.UTTARA_ASHADHA to "Vishvadevas",
        Nakshatra.SHRAVANA to "Vishnu",
        Nakshatra.DHANISHTA to "Vasus",
        Nakshatra.SHATABHISHA to "Varuna",
        Nakshatra.PURVA_BHADRAPADA to "Ajaikapada",
        Nakshatra.UTTARA_BHADRAPADA to "Ahirbudhnya",
        Nakshatra.REVATI to "Pushan"
    )

    // Nakshatra Gana (nature/temperament)
    private val nakshatraGana = mapOf(
        // Deva (Divine) Nakshatras
        Nakshatra.ASHWINI to "Deva",
        Nakshatra.MRIGASHIRA to "Deva",
        Nakshatra.PUNARVASU to "Deva",
        Nakshatra.PUSHYA to "Deva",
        Nakshatra.HASTA to "Deva",
        Nakshatra.SWATI to "Deva",
        Nakshatra.ANURADHA to "Deva",
        Nakshatra.SHRAVANA to "Deva",
        Nakshatra.REVATI to "Deva",
        // Manushya (Human) Nakshatras
        Nakshatra.BHARANI to "Manushya",
        Nakshatra.ROHINI to "Manushya",
        Nakshatra.ARDRA to "Manushya",
        Nakshatra.PURVA_PHALGUNI to "Manushya",
        Nakshatra.UTTARA_PHALGUNI to "Manushya",
        Nakshatra.PURVA_ASHADHA to "Manushya",
        Nakshatra.UTTARA_ASHADHA to "Manushya",
        Nakshatra.PURVA_BHADRAPADA to "Manushya",
        Nakshatra.UTTARA_BHADRAPADA to "Manushya",
        // Rakshasa (Demonic) Nakshatras
        Nakshatra.KRITTIKA to "Rakshasa",
        Nakshatra.ASHLESHA to "Rakshasa",
        Nakshatra.MAGHA to "Rakshasa",
        Nakshatra.CHITRA to "Rakshasa",
        Nakshatra.VISHAKHA to "Rakshasa",
        Nakshatra.JYESHTHA to "Rakshasa",
        Nakshatra.MULA to "Rakshasa",
        Nakshatra.DHANISHTA to "Rakshasa",
        Nakshatra.SHATABHISHA to "Rakshasa"
    )

    override fun evaluate(chart: VedicChart): List<Yoga> {
        val yogas = mutableListOf<Yoga>()

        // 1. Janma Nakshatra Yogas
        yogas.addAll(evaluateJanmaNakshatraYogas(chart))

        // 2. Moon Nakshatra Special Yogas
        yogas.addAll(evaluateMoonNakshatraYogas(chart))

        // 3. Nakshatra Lord Position Yogas
        yogas.addAll(evaluateNakshatraLordYogas(chart))

        // 4. Gandanta Yogas
        yogas.addAll(evaluateGandantaYogas(chart))

        // 5. Pushkara Nakshatra Yogas
        yogas.addAll(evaluatePushkaraNakshatraYogas(chart))

        // 6. Planetary Nakshatra Combinations
        yogas.addAll(evaluatePlanetaryNakshatraYogas(chart))

        // 7. Nakshatra Gana Yogas
        yogas.addAll(evaluateNakshatraGanaYogas(chart))

        // 8. Deity-based Nakshatra Yogas
        yogas.addAll(evaluateDeityNakshatraYogas(chart))

        // 9. Nakshatra Tara Balance
        yogas.addAll(evaluateTaraBalanceYogas(chart))

        return yogas
    }

    // ==================== JANMA NAKSHATRA YOGAS ====================

    private fun evaluateJanmaNakshatraYogas(chart: VedicChart): List<Yoga> {
        val yogas = mutableListOf<Yoga>()
        val moonPos = chart.planetPositions.find { it.planet == Planet.MOON } ?: return yogas
        val janmaNakshatra = moonPos.nakshatra

        // 1. Ashwini Nakshatra - Healing abilities
        if (janmaNakshatra == Nakshatra.ASHWINI) {
            val strength = YogaHelpers.calculateYogaStrength(chart, listOf(moonPos))
            yogas.add(Yoga(
                name = "Ashwini Janma Yoga",
                sanskritName = "अश्विनी जन्म योग",
                category = YogaCategory.SPECIAL_YOGA,
                planets = listOf(Planet.MOON),
                houses = listOf(moonPos.house),
                description = "Moon in Ashwini Nakshatra (ruled by Ketu, deity: Ashwini Kumaras)",
                effects = "Natural healing abilities, quick recovery from illness, interest in medicine and alternative healing, youthful appearance, swift action, good for starting new ventures",
                strength = YogaHelpers.strengthFromPercentage(strength),
                strengthPercentage = strength,
                isAuspicious = true,
                activationPeriod = "Moon and Ketu periods",
                cancellationFactors = emptyList()
            ))
        }

        // 2. Rohini Nakshatra - Creativity and prosperity
        if (janmaNakshatra == Nakshatra.ROHINI) {
            val strength = YogaHelpers.calculateYogaStrength(chart, listOf(moonPos)) * 1.1
            yogas.add(Yoga(
                name = "Rohini Janma Yoga",
                sanskritName = "रोहिणी जन्म योग",
                category = YogaCategory.SPECIAL_YOGA,
                planets = listOf(Planet.MOON),
                houses = listOf(moonPos.house),
                description = "Moon in Rohini Nakshatra (ruled by Moon, deity: Brahma)",
                effects = "Beautiful appearance, creative talents, material prosperity, agricultural success, artistic abilities, fertile imagination, romantic nature",
                strength = YogaHelpers.strengthFromPercentage(strength.coerceIn(10.0, 100.0)),
                strengthPercentage = strength.coerceIn(10.0, 100.0),
                isAuspicious = true,
                activationPeriod = "Moon periods bring greatest benefits",
                cancellationFactors = emptyList()
            ))
        }

        // 3. Pushya Nakshatra - Most auspicious
        if (janmaNakshatra == Nakshatra.PUSHYA) {
            val strength = 85.0
            yogas.add(Yoga(
                name = "Pushya Janma Yoga",
                sanskritName = "पुष्य जन्म योग",
                category = YogaCategory.SPECIAL_YOGA,
                planets = listOf(Planet.MOON),
                houses = listOf(moonPos.house),
                description = "Moon in Pushya Nakshatra (ruled by Saturn, deity: Brihaspati)",
                effects = "Most auspicious birth star, nourishing nature, spiritual inclinations, wise and generous, respected in society, good for spiritual pursuits, blessed by Jupiter",
                strength = YogaHelpers.strengthFromPercentage(strength),
                strengthPercentage = strength,
                isAuspicious = true,
                activationPeriod = "Saturn periods bring spiritual growth",
                cancellationFactors = emptyList()
            ))
        }

        // 4. Magha Nakshatra - Royal and ancestral
        if (janmaNakshatra == Nakshatra.MAGHA) {
            val strength = 75.0
            yogas.add(Yoga(
                name = "Magha Janma Yoga",
                sanskritName = "मघा जन्म योग",
                category = YogaCategory.SPECIAL_YOGA,
                planets = listOf(Planet.MOON),
                houses = listOf(moonPos.house),
                description = "Moon in Magha Nakshatra (ruled by Ketu, deity: Pitris)",
                effects = "Royal bearing, strong ancestral connections, leadership qualities, interest in genealogy and traditions, may receive inheritance, authoritative personality",
                strength = YogaHelpers.strengthFromPercentage(strength),
                strengthPercentage = strength,
                isAuspicious = true,
                activationPeriod = "Ketu periods activate ancestral karma",
                cancellationFactors = emptyList()
            ))
        }

        // 5. Mula Nakshatra - Transformative
        if (janmaNakshatra == Nakshatra.MULA) {
            val strength = 60.0
            yogas.add(Yoga(
                name = "Mula Janma Yoga",
                sanskritName = "मूल जन्म योग",
                category = YogaCategory.SPECIAL_YOGA,
                planets = listOf(Planet.MOON),
                houses = listOf(moonPos.house),
                description = "Moon in Mula Nakshatra (ruled by Ketu, deity: Nirriti)",
                effects = "Deep investigative nature, seeks root causes, transformative life experiences, may face early challenges, research abilities, interest in occult and hidden knowledge",
                strength = YogaHelpers.strengthFromPercentage(strength),
                strengthPercentage = strength,
                isAuspicious = true,
                activationPeriod = "Ketu periods bring transformation",
                cancellationFactors = listOf("First pada may indicate challenges to father", "Remedies recommended for first birth in family")
            ))
        }

        // 6. Shravana Nakshatra - Learning
        if (janmaNakshatra == Nakshatra.SHRAVANA) {
            val strength = 80.0
            yogas.add(Yoga(
                name = "Shravana Janma Yoga",
                sanskritName = "श्रवण जन्म योग",
                category = YogaCategory.SPECIAL_YOGA,
                planets = listOf(Planet.MOON),
                houses = listOf(moonPos.house),
                description = "Moon in Shravana Nakshatra (ruled by Moon, deity: Vishnu)",
                effects = "Excellent listening and learning abilities, scholarly pursuits, devotion to Vishnu, good hearing, success through knowledge, teaching abilities, preservation of traditions",
                strength = YogaHelpers.strengthFromPercentage(strength),
                strengthPercentage = strength,
                isAuspicious = true,
                activationPeriod = "Moon periods enhance learning",
                cancellationFactors = emptyList()
            ))
        }

        // 7. Revati Nakshatra - Completion and spirituality
        if (janmaNakshatra == Nakshatra.REVATI) {
            val strength = 78.0
            yogas.add(Yoga(
                name = "Revati Janma Yoga",
                sanskritName = "रेवती जन्म योग",
                category = YogaCategory.SPECIAL_YOGA,
                planets = listOf(Planet.MOON),
                houses = listOf(moonPos.house),
                description = "Moon in Revati Nakshatra (ruled by Mercury, deity: Pushan)",
                effects = "Protective and nurturing nature, interest in animals, completion of cycles, spiritual culmination, travel to foreign lands, prosperity through journeys, gentle disposition",
                strength = YogaHelpers.strengthFromPercentage(strength),
                strengthPercentage = strength,
                isAuspicious = true,
                activationPeriod = "Mercury periods bring journeys and knowledge",
                cancellationFactors = emptyList()
            ))
        }

        return yogas
    }

    // ==================== MOON NAKSHATRA SPECIAL YOGAS ====================

    private fun evaluateMoonNakshatraYogas(chart: VedicChart): List<Yoga> {
        val yogas = mutableListOf<Yoga>()
        val moonPos = chart.planetPositions.find { it.planet == Planet.MOON } ?: return yogas
        val janmaNakshatra = moonPos.nakshatra

        // 1. Abhukta Mula Dosha - Moon in first 4 ghatis of Mula
        if (janmaNakshatra == Nakshatra.MULA) {
            val degreeInNakshatra = (moonPos.longitude % (360.0 / 27.0))
            if (degreeInNakshatra < 3.333) { // First pada
                val strength = 55.0
                yogas.add(Yoga(
                    name = "Abhukta Mula Dosha",
                    sanskritName = "अभुक्त मूल दोष",
                    category = YogaCategory.NEGATIVE_YOGA,
                    planets = listOf(Planet.MOON),
                    houses = listOf(moonPos.house),
                    description = "Moon in first pada of Mula Nakshatra",
                    effects = "Traditional texts warn of challenges to father or in-laws, requires Mula Shanti puja, transformation through difficulties, ultimate spiritual growth",
                    strength = YogaHelpers.strengthFromPercentage(strength),
                    strengthPercentage = strength,
                    isAuspicious = false,
                    activationPeriod = "Early life and Ketu periods",
                    cancellationFactors = listOf("Jupiter's aspect mitigates effects", "Strong 9th lord helps father", "Mula Shanti puja recommended")
                ))
            }
        }

        // 2. Ganda Nakshatra - Junction stars
        val gandaNakshatras = listOf(Nakshatra.ASHLESHA, Nakshatra.JYESHTHA)
        if (janmaNakshatra in gandaNakshatras) {
            val strength = 50.0
            yogas.add(Yoga(
                name = "Ganda Nakshatra Yoga",
                sanskritName = "गंड नक्षत्र योग",
                category = YogaCategory.NEGATIVE_YOGA,
                planets = listOf(Planet.MOON),
                houses = listOf(moonPos.house),
                description = "Moon in ${janmaNakshatra.displayName} (Ganda Nakshatra)",
                effects = "Complex personality, potential for manipulation or being manipulated, sharp intellect but need to channel positively, intense emotions, secretive nature",
                strength = YogaHelpers.strengthFromPercentage(strength),
                strengthPercentage = strength,
                isAuspicious = false,
                activationPeriod = "Mercury periods for both nakshatras",
                cancellationFactors = listOf("Jupiter aspect brings wisdom", "Spiritual practices help channel energy positively")
            ))
        }

        // 3. Shubha Nakshatra Yoga - Moon in very auspicious nakshatras
        val shubhaNakshatras = listOf(
            Nakshatra.ROHINI, Nakshatra.MRIGASHIRA, Nakshatra.PUSHYA,
            Nakshatra.UTTARA_PHALGUNI, Nakshatra.HASTA, Nakshatra.SWATI,
            Nakshatra.ANURADHA, Nakshatra.UTTARA_ASHADHA, Nakshatra.SHRAVANA,
            Nakshatra.DHANISHTA, Nakshatra.UTTARA_BHADRAPADA, Nakshatra.REVATI
        )
        if (janmaNakshatra in shubhaNakshatras && YogaHelpers.getMoonPhaseStrength(moonPos, chart) > 0.5) {
            val strength = 75.0
            yogas.add(Yoga(
                name = "Shubha Nakshatra Yoga",
                sanskritName = "शुभ नक्षत्र योग",
                category = YogaCategory.SPECIAL_YOGA,
                planets = listOf(Planet.MOON),
                houses = listOf(moonPos.house),
                description = "Waxing Moon in auspicious nakshatra ${janmaNakshatra.displayName}",
                effects = "Generally fortunate life, good character, positive relationships, beneficial karma, auspicious undertakings succeed, peaceful mind",
                strength = YogaHelpers.strengthFromPercentage(strength),
                strengthPercentage = strength,
                isAuspicious = true,
                activationPeriod = "Moon periods and nakshatra lord periods",
                cancellationFactors = emptyList()
            ))
        }

        return yogas
    }

    // ==================== NAKSHATRA LORD YOGAS ====================

    private fun evaluateNakshatraLordYogas(chart: VedicChart): List<Yoga> {
        val yogas = mutableListOf<Yoga>()
        val moonPos = chart.planetPositions.find { it.planet == Planet.MOON } ?: return yogas
        val janmaNakshatra = moonPos.nakshatra

        // Get nakshatra lord
        val nakshatraLord = janmaNakshatra.lord
        val lordPos = chart.planetPositions.find { it.planet == nakshatraLord } ?: return yogas

        // 1. Nakshatra Lord in Kendra
        if (lordPos.house in listOf(1, 4, 7, 10)) {
            val strength = YogaHelpers.calculateYogaStrength(chart, listOf(moonPos, lordPos))
            yogas.add(Yoga(
                name = "Nakshatra Pati Kendra Yoga",
                sanskritName = "नक्षत्र पति केंद्र योग",
                category = YogaCategory.SPECIAL_YOGA,
                planets = listOf(Planet.MOON, nakshatraLord),
                houses = listOf(moonPos.house, lordPos.house),
                description = "Janma Nakshatra lord ${nakshatraLord.displayName} in Kendra house ${lordPos.house}",
                effects = "Strong support for mental and emotional well-being, successful endeavors, protection of fortune, stable life foundation",
                strength = YogaHelpers.strengthFromPercentage(strength),
                strengthPercentage = strength,
                isAuspicious = true,
                activationPeriod = "${nakshatraLord.displayName} Dasha periods",
                cancellationFactors = emptyList()
            ))
        }

        // 2. Nakshatra Lord in Trikona
        if (lordPos.house in listOf(5, 9)) {
            val strength = YogaHelpers.calculateYogaStrength(chart, listOf(moonPos, lordPos))
            yogas.add(Yoga(
                name = "Nakshatra Pati Trikona Yoga",
                sanskritName = "नक्षत्र पति त्रिकोण योग",
                category = YogaCategory.SPECIAL_YOGA,
                planets = listOf(Planet.MOON, nakshatraLord),
                houses = listOf(moonPos.house, lordPos.house),
                description = "Janma Nakshatra lord ${nakshatraLord.displayName} in Trikona house ${lordPos.house}",
                effects = "Good fortune through nakshatra significations, dharmic support, children/education favored (5th) or fortune/guru blessings (9th)",
                strength = YogaHelpers.strengthFromPercentage(strength),
                strengthPercentage = strength,
                isAuspicious = true,
                activationPeriod = "${nakshatraLord.displayName} Dasha periods",
                cancellationFactors = emptyList()
            ))
        }

        // 3. Nakshatra Lord Debilitated
        if (YogaHelpers.isDebilitated(lordPos) && !YogaHelpers.hasNeechaBhanga(lordPos, chart)) {
            val strength = 45.0
            yogas.add(Yoga(
                name = "Nakshatra Pati Neecha Yoga",
                sanskritName = "नक्षत्र पति नीच योग",
                category = YogaCategory.NEGATIVE_YOGA,
                planets = listOf(Planet.MOON, nakshatraLord),
                houses = listOf(lordPos.house),
                description = "Janma Nakshatra lord ${nakshatraLord.displayName} debilitated in ${lordPos.sign.displayName}",
                effects = "Emotional challenges, need to work harder for mental peace, nakshatra significations require extra effort to manifest",
                strength = YogaHelpers.strengthFromPercentage(strength),
                strengthPercentage = strength,
                isAuspicious = false,
                activationPeriod = "${nakshatraLord.displayName} Dasha periods may be challenging",
                cancellationFactors = listOf("Neecha Bhanga if present helps", "Jupiter aspect mitigates")
            ))
        }

        // 4. Nakshatra Lord Exalted
        if (YogaHelpers.isExalted(lordPos)) {
            val strength = 85.0
            yogas.add(Yoga(
                name = "Nakshatra Pati Uccha Yoga",
                sanskritName = "नक्षत्र पति उच्च योग",
                category = YogaCategory.SPECIAL_YOGA,
                planets = listOf(Planet.MOON, nakshatraLord),
                houses = listOf(lordPos.house),
                description = "Janma Nakshatra lord ${nakshatraLord.displayName} exalted in ${lordPos.sign.displayName}",
                effects = "Excellent mental fortitude, nakshatra significations highly activated, emotional wisdom, prosperity through nakshatra-related activities",
                strength = YogaHelpers.strengthFromPercentage(strength),
                strengthPercentage = strength,
                isAuspicious = true,
                activationPeriod = "${nakshatraLord.displayName} Dasha periods bring exceptional results",
                cancellationFactors = emptyList()
            ))
        }

        // 5. Nakshatra Lord conjunct Moon
        if (YogaHelpers.areConjunct(moonPos, lordPos)) {
            val strength = 75.0
            yogas.add(Yoga(
                name = "Nakshatra Pati Chandra Yoga",
                sanskritName = "नक्षत्र पति चंद्र योग",
                category = YogaCategory.SPECIAL_YOGA,
                planets = listOf(Planet.MOON, nakshatraLord),
                houses = listOf(moonPos.house),
                description = "Moon conjunct its Nakshatra lord ${nakshatraLord.displayName}",
                effects = "Strong emotional alignment with nakshatra qualities, enhanced intuition, nakshatra deity blessings, cohesive personality",
                strength = YogaHelpers.strengthFromPercentage(strength),
                strengthPercentage = strength,
                isAuspicious = true,
                activationPeriod = "Moon-${nakshatraLord.displayName} periods",
                cancellationFactors = emptyList()
            ))
        }

        return yogas
    }

    // ==================== GANDANTA YOGAS ====================

    private fun evaluateGandantaYogas(chart: VedicChart): List<Yoga> {
        val yogas = mutableListOf<Yoga>()

        // Gandanta points: Junction between water and fire signs
        // Pisces-Aries (29°20' Pisces - 0°40' Aries) - Revati-Ashwini junction
        // Cancer-Leo (29°20' Cancer - 0°40' Leo) - Ashlesha-Magha junction
        // Scorpio-Sagittarius (29°20' Scorpio - 0°40' Sagittarius) - Jyeshtha-Mula junction

        val gandantaZones = listOf(
            Pair(ZodiacSign.PISCES to 29.333, ZodiacSign.ARIES to 0.667),    // Revati-Ashwini
            Pair(ZodiacSign.CANCER to 29.333, ZodiacSign.LEO to 0.667),      // Ashlesha-Magha
            Pair(ZodiacSign.SCORPIO to 29.333, ZodiacSign.SAGITTARIUS to 0.667) // Jyeshtha-Mula
        )

        chart.planetPositions.forEach { pos ->
            val degreeInSign = pos.longitude % 30
            val sign = pos.sign

            gandantaZones.forEachIndexed { index, zone ->
                val (waterEnd, fireStart) = zone
                val isInWaterEnd = sign == waterEnd.first && degreeInSign >= waterEnd.second
                val isInFireStart = sign == fireStart.first && degreeInSign <= fireStart.second

                if (isInWaterEnd || isInFireStart) {
                    val junctionName = when (index) {
                        0 -> "Revati-Ashwini"
                        1 -> "Ashlesha-Magha"
                        2 -> "Jyeshtha-Mula"
                        else -> ""
                    }

                    val severity = when (pos.planet) {
                        Planet.MOON -> "High" // Moon in Gandanta is most significant
                        Planet.SUN -> "Medium"
                        else -> "Lower"
                    }

                    val baseStrength = when (pos.planet) {
                        Planet.MOON -> 60.0
                        Planet.SUN -> 50.0
                        else -> 40.0
                    }

                    yogas.add(Yoga(
                        name = "Gandanta Yoga (${pos.planet.displayName})",
                        sanskritName = "गंडांत योग",
                        category = YogaCategory.NEGATIVE_YOGA,
                        planets = listOf(pos.planet),
                        houses = listOf(pos.house),
                        description = "${pos.planet.displayName} at ${String.format("%.2f", degreeInSign)}° ${sign.displayName} in $junctionName Gandanta zone",
                        effects = "Karmic knot requiring resolution, transformative experiences through ${pos.planet.displayName} significations, potential difficulties followed by growth, past life karma manifesting",
                        strength = YogaHelpers.strengthFromPercentage(baseStrength),
                        strengthPercentage = baseStrength,
                        isAuspicious = false,
                        activationPeriod = "${pos.planet.displayName} Dasha periods require awareness",
                        cancellationFactors = listOf(
                            "Severity: $severity",
                            "Jupiter aspect reduces negative effects",
                            "Gandanta Shanti puja recommended for Moon",
                            "Spiritual practices help resolve karmic knots"
                        )
                    ))
                }
            }
        }

        return yogas
    }

    // ==================== PUSHKARA NAKSHATRA YOGAS ====================

    private fun evaluatePushkaraNakshatraYogas(chart: VedicChart): List<Yoga> {
        val yogas = mutableListOf<Yoga>()

        // Pushkara Nakshatras - especially auspicious for specific activities
        val pushkaraNakshatras = mapOf(
            Nakshatra.PUNARVASU to "Restoration and renewal",
            Nakshatra.VISHAKHA to "Determination and achievement",
            Nakshatra.PURVA_BHADRAPADA to "Spiritual transformation"
        )

        val moonPos = chart.planetPositions.find { it.planet == Planet.MOON }
        if (moonPos != null && moonPos.nakshatra in pushkaraNakshatras.keys) {
            val quality = pushkaraNakshatras[moonPos.nakshatra]
            val strength = 75.0
            yogas.add(Yoga(
                name = "Pushkara Nakshatra Yoga",
                sanskritName = "पुष्कर नक्षत्र योग",
                category = YogaCategory.SPECIAL_YOGA,
                planets = listOf(Planet.MOON),
                houses = listOf(moonPos.house),
                description = "Moon in Pushkara Nakshatra ${moonPos.nakshatra.displayName}",
                effects = "Blessed birth, $quality, nourishing and supportive environment, activities initiated during Moon periods tend to flourish",
                strength = YogaHelpers.strengthFromPercentage(strength),
                strengthPercentage = strength,
                isAuspicious = true,
                activationPeriod = "Moon periods and nakshatra lord periods",
                cancellationFactors = emptyList()
            ))
        }

        return yogas
    }

    // ==================== PLANETARY NAKSHATRA YOGAS ====================

    private fun evaluatePlanetaryNakshatraYogas(chart: VedicChart): List<Yoga> {
        val yogas = mutableListOf<Yoga>()

        // 1. Planet in own nakshatra (Swanakshatra)
        chart.planetPositions.forEach { pos ->
            val nakshatra = pos.nakshatra
            if (nakshatra.lord == pos.planet) {
                val strength = YogaHelpers.calculateYogaStrength(chart, listOf(pos)) * 1.15
                yogas.add(Yoga(
                    name = "Swanakshatra Yoga (${pos.planet.displayName})",
                    sanskritName = "स्वनक्षत्र योग",
                    category = YogaCategory.SPECIAL_YOGA,
                    planets = listOf(pos.planet),
                    houses = listOf(pos.house),
                    description = "${pos.planet.displayName} in its own nakshatra ${nakshatra.displayName}",
                    effects = "${pos.planet.displayName} highly empowered, gives excellent results in its significations, comfortable and natural expression, enhanced planetary strength",
                    strength = YogaHelpers.strengthFromPercentage(strength.coerceIn(10.0, 100.0)),
                    strengthPercentage = strength.coerceIn(10.0, 100.0),
                    isAuspicious = true,
                    activationPeriod = "${pos.planet.displayName} Dasha periods",
                    cancellationFactors = emptyList()
                ))
            }
        }

        // 2. Benefic in Deva nakshatra + Malefic in Deva nakshatra patterns
        val benefics = listOf(Planet.JUPITER, Planet.VENUS, Planet.MERCURY, Planet.MOON)
        val devaNakshatras = nakshatraGana.filter { it.value == "Deva" }.keys

        val beneficsInDeva = chart.planetPositions.filter {
            it.planet in benefics && it.nakshatra in devaNakshatras
        }

        if (beneficsInDeva.size >= 2) {
            val strength = 70.0 + (beneficsInDeva.size - 2) * 5.0
            yogas.add(Yoga(
                name = "Deva Nakshatra Yoga",
                sanskritName = "देव नक्षत्र योग",
                category = YogaCategory.SPECIAL_YOGA,
                planets = beneficsInDeva.map { it.planet },
                houses = beneficsInDeva.map { it.house }.distinct(),
                description = "${beneficsInDeva.size} benefics in Deva (divine) nakshatras",
                effects = "Divine blessings, righteous nature, success through ethical means, protected from major troubles, pleasant personality",
                strength = YogaHelpers.strengthFromPercentage(strength.coerceIn(10.0, 100.0)),
                strengthPercentage = strength.coerceIn(10.0, 100.0),
                isAuspicious = true,
                activationPeriod = "Periods of benefics in Deva nakshatras",
                cancellationFactors = emptyList()
            ))
        }

        // 3. Sun in Krittika - Fire deity alignment
        val sunPos = chart.planetPositions.find { it.planet == Planet.SUN }
        if (sunPos != null && sunPos.nakshatra == Nakshatra.KRITTIKA) {
            val strength = 75.0
            yogas.add(Yoga(
                name = "Agni Nakshatra Yoga",
                sanskritName = "अग्नि नक्षत्र योग",
                category = YogaCategory.SPECIAL_YOGA,
                planets = listOf(Planet.SUN),
                houses = listOf(sunPos.house),
                description = "Sun in Krittika Nakshatra (ruled by Sun, deity: Agni)",
                effects = "Sharp intellect, purifying nature, ability to cut through obstacles, cooking/culinary skills, transformative leadership, fame and recognition",
                strength = YogaHelpers.strengthFromPercentage(strength),
                strengthPercentage = strength,
                isAuspicious = true,
                activationPeriod = "Sun Dasha periods",
                cancellationFactors = emptyList()
            ))
        }

        // 4. Jupiter in Punarvasu - Return to grace
        val jupiterPos = chart.planetPositions.find { it.planet == Planet.JUPITER }
        if (jupiterPos != null && jupiterPos.nakshatra == Nakshatra.PUNARVASU) {
            val strength = 80.0
            yogas.add(Yoga(
                name = "Aditi Nakshatra Yoga",
                sanskritName = "अदिति नक्षत्र योग",
                category = YogaCategory.SPECIAL_YOGA,
                planets = listOf(Planet.JUPITER),
                houses = listOf(jupiterPos.house),
                description = "Jupiter in Punarvasu Nakshatra (ruled by Jupiter, deity: Aditi)",
                effects = "Restoration of fortune, infinite possibilities, maternal blessings, recovery from setbacks, expansive opportunities, teaching abilities",
                strength = YogaHelpers.strengthFromPercentage(strength),
                strengthPercentage = strength,
                isAuspicious = true,
                activationPeriod = "Jupiter Dasha periods",
                cancellationFactors = emptyList()
            ))
        }

        // 5. Venus in Purva Phalguni - Creative pleasures
        val venusPos = chart.planetPositions.find { it.planet == Planet.VENUS }
        if (venusPos != null && venusPos.nakshatra == Nakshatra.PURVA_PHALGUNI) {
            val strength = 78.0
            yogas.add(Yoga(
                name = "Bhaga Nakshatra Yoga",
                sanskritName = "भग नक्षत्र योग",
                category = YogaCategory.SPECIAL_YOGA,
                planets = listOf(Planet.VENUS),
                houses = listOf(venusPos.house),
                description = "Venus in Purva Phalguni Nakshatra (ruled by Venus, deity: Bhaga)",
                effects = "Enjoyment of life's pleasures, creative talents, romantic success, artistic abilities, marriage blessings, wealth through entertainment",
                strength = YogaHelpers.strengthFromPercentage(strength),
                strengthPercentage = strength,
                isAuspicious = true,
                activationPeriod = "Venus Dasha periods",
                cancellationFactors = emptyList()
            ))
        }

        return yogas
    }

    // ==================== NAKSHATRA GANA YOGAS ====================

    private fun evaluateNakshatraGanaYogas(chart: VedicChart): List<Yoga> {
        val yogas = mutableListOf<Yoga>()

        // Count planets in each Gana
        val ganaCount = mutableMapOf("Deva" to 0, "Manushya" to 0, "Rakshasa" to 0)

        chart.planetPositions.filter {
            it.planet in listOf(Planet.SUN, Planet.MOON, Planet.MARS, Planet.MERCURY,
                                Planet.JUPITER, Planet.VENUS, Planet.SATURN)
        }.forEach { pos ->
            val gana = nakshatraGana[pos.nakshatra]
            if (gana != null) {
                ganaCount[gana] = (ganaCount[gana] ?: 0) + 1
            }
        }

        // 1. Deva Gana predominance
        if ((ganaCount["Deva"] ?: 0) >= 4) {
            val strength = 75.0
            yogas.add(Yoga(
                name = "Deva Gana Predominance",
                sanskritName = "देव गण प्रधान योग",
                category = YogaCategory.SPECIAL_YOGA,
                planets = emptyList(),
                houses = emptyList(),
                description = "${ganaCount["Deva"]} planets in Deva (divine) Gana nakshatras",
                effects = "Divine temperament, refined nature, interest in higher knowledge, spiritual inclinations, helpful and charitable, attracted to sattvic lifestyle",
                strength = YogaHelpers.strengthFromPercentage(strength),
                strengthPercentage = strength,
                isAuspicious = true,
                activationPeriod = "Throughout life, especially in spiritual pursuits",
                cancellationFactors = emptyList()
            ))
        }

        // 2. Rakshasa Gana predominance
        if ((ganaCount["Rakshasa"] ?: 0) >= 4) {
            val strength = 55.0
            yogas.add(Yoga(
                name = "Rakshasa Gana Predominance",
                sanskritName = "राक्षस गण प्रधान योग",
                category = YogaCategory.SPECIAL_YOGA,
                planets = emptyList(),
                houses = emptyList(),
                description = "${ganaCount["Rakshasa"]} planets in Rakshasa Gana nakshatras",
                effects = "Intense and powerful nature, independent thinking, unconventional approach, strong survival instincts, can be challenging for others to understand, transforms obstacles into opportunities",
                strength = YogaHelpers.strengthFromPercentage(strength),
                strengthPercentage = strength,
                isAuspicious = true,
                activationPeriod = "Throughout life, especially in competitive situations",
                cancellationFactors = listOf("Channel intensity into constructive pursuits", "Spiritual practices help balance energy")
            ))
        }

        return yogas
    }

    // ==================== DEITY NAKSHATRA YOGAS ====================

    private fun evaluateDeityNakshatraYogas(chart: VedicChart): List<Yoga> {
        val yogas = mutableListOf<Yoga>()
        val moonPos = chart.planetPositions.find { it.planet == Planet.MOON } ?: return yogas

        // Special deity alignments based on Moon nakshatra
        val deity = nakshatraDeities[moonPos.nakshatra]

        // Vishnu nakshatras - preservation and protection
        val vishnuNakshatras = listOf(Nakshatra.SHRAVANA, Nakshatra.UTTARA_ASHADHA)
        if (moonPos.nakshatra in vishnuNakshatras) {
            val strength = 80.0
            yogas.add(Yoga(
                name = "Vishnu Nakshatra Yoga",
                sanskritName = "विष्णु नक्षत्र योग",
                category = YogaCategory.SPECIAL_YOGA,
                planets = listOf(Planet.MOON),
                houses = listOf(moonPos.house),
                description = "Moon in $deity-ruled nakshatra ${moonPos.nakshatra.displayName}",
                effects = "Protected by Vishnu energy, preserving and sustaining nature, success through patience, long-term achievements, organizational abilities",
                strength = YogaHelpers.strengthFromPercentage(strength),
                strengthPercentage = strength,
                isAuspicious = true,
                activationPeriod = "Moon periods bring Vishnu blessings",
                cancellationFactors = emptyList()
            ))
        }

        // Shiva nakshatras - transformation
        val shivaNakshatras = listOf(Nakshatra.ARDRA, Nakshatra.MULA, Nakshatra.UTTARA_BHADRAPADA)
        if (moonPos.nakshatra in shivaNakshatras) {
            val strength = 70.0
            yogas.add(Yoga(
                name = "Rudra Nakshatra Yoga",
                sanskritName = "रुद्र नक्षत्र योग",
                category = YogaCategory.SPECIAL_YOGA,
                planets = listOf(Planet.MOON),
                houses = listOf(moonPos.house),
                description = "Moon in Shiva/Rudra-influenced nakshatra ${moonPos.nakshatra.displayName}",
                effects = "Transformative experiences, destruction of obstacles, intensity and depth, research abilities, interest in occult, healing through crisis",
                strength = YogaHelpers.strengthFromPercentage(strength),
                strengthPercentage = strength,
                isAuspicious = true,
                activationPeriod = "Moon periods bring transformative experiences",
                cancellationFactors = listOf("Initial challenges lead to growth")
            ))
        }

        return yogas
    }

    // ==================== TARA BALANCE YOGAS ====================

    private fun evaluateTaraBalanceYogas(chart: VedicChart): List<Yoga> {
        val yogas = mutableListOf<Yoga>()
        val moonPos = chart.planetPositions.find { it.planet == Planet.MOON } ?: return yogas
        val janmaNakshatra = moonPos.nakshatra

        // Tara Chakra - 9 Taras from Janma Nakshatra
        // 1-Janma, 2-Sampat, 3-Vipat, 4-Kshema, 5-Pratyak, 6-Sadhana, 7-Naidhana, 8-Mitra, 9-Parama Mitra

        val taraNames = listOf("Janma", "Sampat", "Vipat", "Kshema", "Pratyak", "Sadhana", "Naidhana", "Mitra", "Parama Mitra")
        val auspiciousTaras = listOf(1, 2, 4, 6, 8, 9) // 1-indexed: Janma is special, Sampat, Kshema, Sadhana, Mitra, Parama Mitra
        val inauspiciousTaras = listOf(3, 5, 7) // Vipat, Pratyak, Naidhana

        // Count planets in each tara category
        var auspiciousCount = 0
        var inauspiciousCount = 0

        chart.planetPositions.filter {
            it.planet in listOf(Planet.SUN, Planet.MARS, Planet.MERCURY, Planet.JUPITER, Planet.VENUS, Planet.SATURN)
        }.forEach { pos ->
            val nakshatraDiff = (pos.nakshatra.ordinal - janmaNakshatra.ordinal + 27) % 27
            val taraNumber = (nakshatraDiff % 9) + 1

            if (taraNumber in auspiciousTaras) auspiciousCount++
            if (taraNumber in inauspiciousTaras) inauspiciousCount++
        }

        if (auspiciousCount >= 4 && inauspiciousCount <= 1) {
            val strength = 75.0 + (auspiciousCount - 4) * 3.0
            yogas.add(Yoga(
                name = "Shubha Tara Yoga",
                sanskritName = "शुभ तारा योग",
                category = YogaCategory.SPECIAL_YOGA,
                planets = listOf(Planet.MOON),
                houses = listOf(moonPos.house),
                description = "Most planets in auspicious Taras from Janma Nakshatra",
                effects = "Generally supportive planetary configuration, smoother life journey, challenges more easily overcome, good timing for initiatives",
                strength = YogaHelpers.strengthFromPercentage(strength.coerceIn(10.0, 100.0)),
                strengthPercentage = strength.coerceIn(10.0, 100.0),
                isAuspicious = true,
                activationPeriod = "Throughout life",
                cancellationFactors = emptyList()
            ))
        }

        if (inauspiciousCount >= 3) {
            val strength = 50.0 + (inauspiciousCount - 3) * 5.0
            yogas.add(Yoga(
                name = "Ashubha Tara Yoga",
                sanskritName = "अशुभ तारा योग",
                category = YogaCategory.NEGATIVE_YOGA,
                planets = listOf(Planet.MOON),
                houses = listOf(moonPos.house),
                description = "Multiple planets in challenging Taras from Janma Nakshatra",
                effects = "More obstacles to overcome, requires extra effort for success, timing awareness important, remedies through nakshatra worship beneficial",
                strength = YogaHelpers.strengthFromPercentage(strength.coerceIn(10.0, 100.0)),
                strengthPercentage = strength.coerceIn(10.0, 100.0),
                isAuspicious = false,
                activationPeriod = "Periods of planets in Vipat/Pratyak/Naidhana taras",
                cancellationFactors = listOf("Choose auspicious timings for important activities", "Nakshatra remedies help")
            ))
        }

        return yogas
    }
}
