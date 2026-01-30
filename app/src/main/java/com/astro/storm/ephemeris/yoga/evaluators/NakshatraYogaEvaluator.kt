package com.astro.storm.ephemeris.yoga.evaluators

import com.astro.storm.core.model.Nakshatra
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
 * Nakshatra-Based Yoga Evaluator
 *
 * Evaluates yogas formed based on nakshatra (lunar mansion) positions.
 * This includes:
 * 1. Moon Nakshatra qualities
 * 2. Nakshatra-planet combinations
 * 3. Pushkara Navamsa/Bhaga
 * 4. Mrityu Bhaga (death degrees)
 * 5. Nakshatra Sandhi (junction)
 * 6. Special nakshatra formations
 * 7. Vargottama nakshatra positions
 *
 * Based on:
 * - Brihat Parashara Hora Shastra (BPHS)
 * - Jataka Parijata
 * - Muhurta texts
 *
 * @author AstroStorm
 */
class NakshatraYogaEvaluator : YogaEvaluator {

    override val category: YogaCategory = YogaCategory.SPECIAL_YOGA

    companion object {
        // Nakshatra categories
        private val DHRUVA_NAKSHATRAS = listOf(
            Nakshatra.UTTARA_PHALGUNI, Nakshatra.UTTARA_ASHADHA, 
            Nakshatra.UTTARA_BHADRAPADA, Nakshatra.ROHINI
        )
        private val CHARA_NAKSHATRAS = listOf(
            Nakshatra.PUNARVASU, Nakshatra.SWATI, 
            Nakshatra.SHRAVANA, Nakshatra.SHATABHISHA
        )
        private val TIKSHNA_NAKSHATRAS = listOf(
            Nakshatra.ARDRA, Nakshatra.ASHLESHA, 
            Nakshatra.MOOLA, Nakshatra.JYESHTHA
        )
        private val MRIDU_NAKSHATRAS = listOf(
            Nakshatra.MRIGASHIRA, Nakshatra.CHITRA, 
            Nakshatra.ANURADHA, Nakshatra.REVATI
        )
        private val UGRA_NAKSHATRAS = listOf(
            Nakshatra.BHARANI, Nakshatra.MAGHA, 
            Nakshatra.PURVA_PHALGUNI, Nakshatra.PURVA_ASHADHA, 
            Nakshatra.PURVA_BHADRAPADA
        )
        private val MISHRA_NAKSHATRAS = listOf(
            Nakshatra.KRITTIKA, Nakshatra.VISHAKHA
        )

        // Pushkara Navamsa positions (specific degrees)
        private val PUSHKARA_NAVAMSA_DEGREES = listOf(
            // Format: Pair(startDegree, endDegree) for Pushkara navamsas
            Pair(20.0, 23.333),   // Taurus navamsa in Aries
            Pair(86.667, 90.0),  // Cancer navamsa in Cancer
            Pair(200.0, 203.333), // Libra navamsa in Libra
            Pair(266.667, 270.0), // Capricorn navamsa in Capricorn
            // Additional Pushkara positions per classical texts
            Pair(53.333, 56.667),
            Pair(143.333, 146.667),
            Pair(233.333, 236.667),
            Pair(323.333, 326.667)
        )

        // Mrityu Bhaga (death degrees) for each sign - classical values
        private val MRITYU_BHAGA = mapOf(
            ZodiacSign.ARIES to 26.0,
            ZodiacSign.TAURUS to 12.0,
            ZodiacSign.GEMINI to 13.0,
            ZodiacSign.CANCER to 25.0,
            ZodiacSign.LEO to 24.0,
            ZodiacSign.VIRGO to 22.0,
            ZodiacSign.LIBRA to 7.0,
            ZodiacSign.SCORPIO to 14.0,
            ZodiacSign.SAGITTARIUS to 13.0,
            ZodiacSign.CAPRICORN to 25.0,
            ZodiacSign.AQUARIUS to 5.0,
            ZodiacSign.PISCES to 12.0
        )
    }

    override fun evaluate(chart: VedicChart): List<Yoga> {
        val yogas = mutableListOf<Yoga>()

        // 1. Moon Nakshatra Quality Yoga
        evaluateMoonNakshatraYoga(chart)?.let { yogas.add(it) }

        // 2. Pushkara Navamsa Yogas
        yogas.addAll(evaluatePushkaraNavamsaYogas(chart))

        // 3. Mrityu Bhaga Yogas
        yogas.addAll(evaluateMrityuBhagaYogas(chart))

        // 4. Nakshatra Sandhi Yogas
        yogas.addAll(evaluateNakshatraSandhiYogas(chart))

        // 5. Nakshatra Lord Yogas
        yogas.addAll(evaluateNakshatraLordYogas(chart))

        // 6. Special Nakshatra Combinations
        yogas.addAll(evaluateSpecialNakshatraCombinations(chart))

        // 7. Abhijit Muhurta Yoga (if applicable)
        evaluateAbhijitYoga(chart)?.let { yogas.add(it) }

        return yogas
    }

    /**
     * Evaluate Moon's nakshatra quality and effects
     */
    private fun evaluateMoonNakshatraYoga(chart: VedicChart): Yoga? {
        val moonPos = chart.planetPositions.find { it.planet == Planet.MOON } ?: return null
        val nakshatra = moonPos.nakshatra

        // Determine nakshatra category and effects
        val (category, effects, isAuspicious) = when {
            nakshatra in DHRUVA_NAKSHATRAS -> Triple(
                "Dhruva (Fixed)",
                "Stable personality, consistent efforts bring lasting results, reliable nature, good for permanent undertakings",
                true
            )
            nakshatra in CHARA_NAKSHATRAS -> Triple(
                "Chara (Movable)",
                "Adaptable nature, success through travel and change, flexibility in approach, good for temporal activities",
                true
            )
            nakshatra in TIKSHNA_NAKSHATRAS -> Triple(
                "Tikshna (Sharp)",
                "Penetrating intellect, success through analysis, potential for healing or destruction, transformative abilities",
                false // Can be challenging
            )
            nakshatra in MRIDU_NAKSHATRAS -> Triple(
                "Mridu (Soft)",
                "Gentle nature, artistic abilities, harmonious relationships, success through diplomacy",
                true
            )
            nakshatra in UGRA_NAKSHATRAS -> Triple(
                "Ugra (Fierce)",
                "Powerful personality, leadership through strength, success in competitive fields, can be overwhelming",
                true
            )
            nakshatra in MISHRA_NAKSHATRAS -> Triple(
                "Mishra (Mixed)",
                "Versatile abilities, success in varied fields, need for balance between extremes",
                true
            )
            else -> Triple(
                "General",
                "Balanced nakshatra qualities present",
                true
            )
        }

        val nakshatraLord = nakshatra.ruler
        val lordPos = chart.planetPositions.find { it.planet == nakshatraLord }
        val lordStrong = lordPos != null && (YogaHelpers.isExalted(lordPos) || YogaHelpers.isInOwnSign(lordPos))

        val strength = if (lordStrong) 75.0 else 60.0

        return Yoga(
            name = "Janma Nakshatra Yoga ($category)",
            sanskritName = "जन्म नक्षत्र योग",
            category = YogaCategory.SPECIAL_YOGA,
            planets = listOf(Planet.MOON, nakshatraLord),
            houses = listOf(moonPos.house),
            description = "Moon in ${nakshatra.displayName} - $category nakshatra, ruled by ${nakshatraLord.displayName}",
            effects = effects,
            strength = YogaHelpers.strengthFromPercentage(strength),
            strengthPercentage = strength,
            isAuspicious = isAuspicious,
            activationPeriod = "Moon and ${nakshatraLord.displayName} periods",
            cancellationFactors = if (!isAuspicious) listOf("Benefic aspects can soften challenging qualities") else emptyList()
        )
    }

    /**
     * Evaluate Pushkara Navamsa positions
     */
    private fun evaluatePushkaraNavamsaYogas(chart: VedicChart): List<Yoga> {
        val yogas = mutableListOf<Yoga>()
        val importantPlanets = listOf(Planet.MOON, Planet.SUN, Planet.JUPITER, Planet.VENUS, Planet.MERCURY)

        importantPlanets.forEach { planet ->
            val pos = chart.planetPositions.find { it.planet == planet } ?: return@forEach

            val isInPushkara = PUSHKARA_NAVAMSA_DEGREES.any { (start, end) ->
                pos.longitude in start..end
            }

            if (isInPushkara) {
                val effects = when (planet) {
                    Planet.MOON -> "Highly auspicious mind, emotional contentment, mother's blessings, mental peace"
                    Planet.SUN -> "Honored by authorities, blessed soul, father's protection, radiant personality"
                    Planet.JUPITER -> "Divine blessings, wisdom, guru's grace, religious merit"
                    Planet.VENUS -> "Luxury and comfort, artistic success, beautiful relationships"
                    Planet.MERCURY -> "Exceptional intellect, communication skills, business acumen"
                    else -> "Enhanced planetary significations"
                }

                yogas.add(Yoga(
                    name = "${planet.displayName} Pushkara Navamsa Yoga",
                    sanskritName = "पुष्कर नवांश योग",
                    category = YogaCategory.SPECIAL_YOGA,
                    planets = listOf(planet),
                    houses = listOf(pos.house),
                    description = "${planet.displayName} in Pushkara Navamsa at ${String.format("%.2f", pos.longitude)}°",
                    effects = effects,
                    strength = YogaStrength.VERY_STRONG,
                    strengthPercentage = 80.0,
                    isAuspicious = true,
                    activationPeriod = "${planet.displayName} Dasha and Antardasha",
                    cancellationFactors = emptyList()
                ))
            }
        }

        return yogas
    }

    /**
     * Evaluate Mrityu Bhaga positions
     */
    private fun evaluateMrityuBhagaYogas(chart: VedicChart): List<Yoga> {
        val yogas = mutableListOf<Yoga>()
        val checkPlanets = listOf(Planet.MOON, Planet.SUN)

        checkPlanets.forEach { planet ->
            val pos = chart.planetPositions.find { it.planet == planet } ?: return@forEach
            val mrityuDegree = MRITYU_BHAGA[pos.sign] ?: return@forEach

            // Check if planet is within 1 degree of Mrityu Bhaga
            val positionInSign = pos.longitude % 30.0
            val isInMrityuBhaga = kotlin.math.abs(positionInSign - mrityuDegree) <= 1.0

            if (isInMrityuBhaga) {
                val cancellations = mutableListOf<String>()

                // Check for cancellation
                val jupiterPos = chart.planetPositions.find { it.planet == Planet.JUPITER }
                if (jupiterPos != null && YogaHelpers.isAspecting(jupiterPos, pos)) {
                    cancellations.add("Jupiter's aspect provides protection")
                }
                if (YogaHelpers.isExalted(pos) || YogaHelpers.isInOwnSign(pos)) {
                    cancellations.add("Planet in dignity reduces negative effects")
                }

                val effects = when (planet) {
                    Planet.MOON -> "Mind may face challenging periods, emotional sensitivity heightened, mother's health requires attention"
                    Planet.SUN -> "Vitality may fluctuate, father-related concerns, authority challenges"
                    else -> "Potential health sensitivities requiring attention"
                }

                yogas.add(Yoga(
                    name = "${planet.displayName} Mrityu Bhaga Yoga",
                    sanskritName = "मृत्यु भाग योग",
                    category = YogaCategory.NEGATIVE_YOGA,
                    planets = listOf(planet),
                    houses = listOf(pos.house),
                    description = "${planet.displayName} in Mrityu Bhaga at ${String.format("%.2f", positionInSign)}° in ${pos.sign.displayName}",
                    effects = effects,
                    strength = YogaStrength.MODERATE,
                    strengthPercentage = 55.0,
                    isAuspicious = false,
                    activationPeriod = "${planet.displayName} Dasha",
                    cancellationFactors = cancellations.ifEmpty { listOf("Remedial measures recommended") }
                ))
            }
        }

        return yogas
    }

    /**
     * Evaluate Nakshatra Sandhi (junction) positions
     */
    private fun evaluateNakshatraSandhiYogas(chart: VedicChart): List<Yoga> {
        val yogas = mutableListOf<Yoga>()

        // Check Moon especially
        val moonPos = chart.planetPositions.find { it.planet == Planet.MOON } ?: return yogas

        // Each nakshatra is 13°20' = 13.333°
        val nakshatraDegrees = 13.333
        val positionInNakshatra = (moonPos.longitude % nakshatraDegrees)

        // Sandhi is first or last 1 degree
        val isInSandhi = positionInNakshatra <= 1.0 || positionInNakshatra >= (nakshatraDegrees - 1.0)

        if (isInSandhi) {
            val sandhiType = if (positionInNakshatra <= 1.0) "beginning" else "end"

            yogas.add(Yoga(
                name = "Nakshatra Sandhi Yoga",
                sanskritName = "नक्षत्र संधि योग",
                category = YogaCategory.NEGATIVE_YOGA,
                planets = listOf(Planet.MOON),
                houses = listOf(moonPos.house),
                description = "Moon at $sandhiType of ${moonPos.nakshatra.displayName} nakshatra",
                effects = "Junction point birth indicating transitional karmic patterns, may face identity or emotional challenges initially, improves with age",
                strength = YogaStrength.WEAK,
                strengthPercentage = 45.0,
                isAuspicious = false,
                activationPeriod = "Moon Dasha and major life transitions",
                cancellationFactors = listOf(
                    "Strong Jupiter mitigates",
                    "Effects usually mild and improve with time",
                    "Nakshatra sandhi shanti can help"
                )
            ))
        }

        return yogas
    }

    /**
     * Evaluate Nakshatra Lord Yogas
     */
    private fun evaluateNakshatraLordYogas(chart: VedicChart): List<Yoga> {
        val yogas = mutableListOf<Yoga>()
        val moonPos = chart.planetPositions.find { it.planet == Planet.MOON } ?: return yogas

        val nakshatraLord = moonPos.nakshatra.ruler
        val lordPos = chart.planetPositions.find { it.planet == nakshatraLord } ?: return yogas

        // Nakshatra lord in Kendra or Trikona
        val isInKendraTrikona = lordPos.house in listOf(1, 4, 5, 7, 9, 10)
        val isStrong = YogaHelpers.isExalted(lordPos) || YogaHelpers.isInOwnSign(lordPos)

        if (isInKendraTrikona && isStrong) {
            yogas.add(Yoga(
                name = "Nakshatra Pati Yoga",
                sanskritName = "नक्षत्र पति योग",
                category = YogaCategory.SPECIAL_YOGA,
                planets = listOf(nakshatraLord, Planet.MOON),
                houses = listOf(moonPos.house, lordPos.house),
                description = "Nakshatra lord ${nakshatraLord.displayName} strong in ${ordinal(lordPos.house)} house",
                effects = "Birth nakshatra lord strengthening overall fortune, mental clarity, success through nakshatra qualities",
                strength = YogaStrength.STRONG,
                strengthPercentage = 70.0,
                isAuspicious = true,
                activationPeriod = "Vimshottari Dasha of ${nakshatraLord.displayName}",
                cancellationFactors = emptyList()
            ))
        }

        // Nakshatra lord in Dusthana
        val isInDusthana = lordPos.house in listOf(6, 8, 12)
        val isWeak = YogaHelpers.isDebilitated(lordPos)

        if (isInDusthana && isWeak) {
            yogas.add(Yoga(
                name = "Nakshatra Pati Dosha",
                sanskritName = "नक्षत्र पति दोष",
                category = YogaCategory.NEGATIVE_YOGA,
                planets = listOf(nakshatraLord, Planet.MOON),
                houses = listOf(moonPos.house, lordPos.house),
                description = "Nakshatra lord ${nakshatraLord.displayName} weak in ${ordinal(lordPos.house)} house",
                effects = "Challenges during nakshatra lord's dasha, mental stress possible, fortune fluctuations",
                strength = YogaStrength.MODERATE,
                strengthPercentage = 50.0,
                isAuspicious = false,
                activationPeriod = "Vimshottari Dasha of ${nakshatraLord.displayName}",
                cancellationFactors = listOf(
                    "Jupiter's aspect mitigates",
                    "Neecha Bhanga can transform"
                )
            ))
        }

        return yogas
    }

    /**
     * Evaluate special nakshatra combinations
     */
    private fun evaluateSpecialNakshatraCombinations(chart: VedicChart): List<Yoga> {
        val yogas = mutableListOf<Yoga>()

        // 1. Trikanshaka Yoga - Multiple planets in Tikshna nakshatras
        val planetsInTikshna = chart.planetPositions.filter {
            it.planet !in listOf(Planet.RAHU, Planet.KETU) && it.nakshatra in TIKSHNA_NAKSHATRAS
        }
        if (planetsInTikshna.size >= 3) {
            yogas.add(Yoga(
                name = "Tikshna Nakshatra Yoga",
                sanskritName = "तीक्ष्ण नक्षत्र योग",
                category = YogaCategory.SPECIAL_YOGA,
                planets = planetsInTikshna.map { it.planet },
                houses = planetsInTikshna.map { it.house }.distinct(),
                description = "${planetsInTikshna.size} planets in sharp nakshatras",
                effects = "Penetrating intellect, research abilities, healing powers, transformative nature - must channel energy constructively",
                strength = YogaStrength.MODERATE,
                strengthPercentage = 60.0,
                isAuspicious = true,
                activationPeriod = "Throughout life",
                cancellationFactors = emptyList()
            ))
        }

        // 2. Shubha Nakshatra Yoga - Benefics in Mridu nakshatras
        val beneficsInMridu = chart.planetPositions.filter {
            it.planet in listOf(Planet.JUPITER, Planet.VENUS, Planet.MERCURY) && it.nakshatra in MRIDU_NAKSHATRAS
        }
        if (beneficsInMridu.size >= 2) {
            yogas.add(Yoga(
                name = "Shubha Nakshatra Yoga",
                sanskritName = "शुभ नक्षत्र योग",
                category = YogaCategory.SPECIAL_YOGA,
                planets = beneficsInMridu.map { it.planet },
                houses = beneficsInMridu.map { it.house }.distinct(),
                description = "Benefic planets in soft, gentle nakshatras",
                effects = "Natural grace and harmony, artistic talents, diplomatic success, pleasant disposition",
                strength = YogaStrength.STRONG,
                strengthPercentage = 70.0,
                isAuspicious = true,
                activationPeriod = "Periods of involved benefics",
                cancellationFactors = emptyList()
            ))
        }

        // 3. Dhruva Nakshatra Yoga - Key planets in fixed nakshatras
        val planetsInDhruva = chart.planetPositions.filter {
            it.planet in listOf(Planet.MOON, Planet.SUN, Planet.JUPITER) && it.nakshatra in DHRUVA_NAKSHATRAS
        }
        if (planetsInDhruva.size >= 2) {
            yogas.add(Yoga(
                name = "Dhruva Nakshatra Yoga",
                sanskritName = "ध्रुव नक्षत्र योग",
                category = YogaCategory.SPECIAL_YOGA,
                planets = planetsInDhruva.map { it.planet },
                houses = planetsInDhruva.map { it.house }.distinct(),
                description = "Key planets in fixed, stable nakshatras",
                effects = "Rock-solid determination, permanent achievements, reliable nature, lasting success",
                strength = YogaStrength.STRONG,
                strengthPercentage = 72.0,
                isAuspicious = true,
                activationPeriod = "Throughout life, especially major planet periods",
                cancellationFactors = emptyList()
            ))
        }

        // 4. Nakshatra Vargottama - Moon in same nakshatra in D1 and D9
        val moonPos = chart.planetPositions.find { it.planet == Planet.MOON }
        if (moonPos != null) {
            // Check if Moon is vargottama (same sign in D1 and D9)
            val moonNavamsaSign = calculateNavamsaSign(moonPos.longitude)
            if (moonPos.sign == moonNavamsaSign) {
                yogas.add(Yoga(
                    name = "Chandra Vargottama Nakshatra Yoga",
                    sanskritName = "चन्द्र वर्गोत्तम नक्षत्र योग",
                    category = YogaCategory.SPECIAL_YOGA,
                    planets = listOf(Planet.MOON),
                    houses = listOf(moonPos.house),
                    description = "Moon vargottama - same sign in birth chart and navamsa",
                    effects = "Strong mind, emotional stability, consistent nature, natural wisdom in Moon's significations",
                    strength = YogaStrength.VERY_STRONG,
                    strengthPercentage = 78.0,
                    isAuspicious = true,
                    activationPeriod = "Moon Dasha and throughout life",
                    cancellationFactors = emptyList()
                ))
            }
        }

        return yogas
    }

    /**
     * Evaluate Abhijit Yoga (special timing)
     */
    private fun evaluateAbhijitYoga(chart: VedicChart): Yoga? {
        // Abhijit is the junction nakshatra between Uttara Ashadha and Shravana
        // It's traditionally considered very auspicious for birth
        val moonPos = chart.planetPositions.find { it.planet == Planet.MOON } ?: return null

        // Abhijit spans approximately from 276°40' to 280°53' (end of Uttara Ashadha)
        val abhijitStart = 276.667
        val abhijitEnd = 280.889

        if (moonPos.longitude in abhijitStart..abhijitEnd) {
            return Yoga(
                name = "Abhijit Nakshatra Yoga",
                sanskritName = "अभिजित नक्षत्र योग",
                category = YogaCategory.SPECIAL_YOGA,
                planets = listOf(Planet.MOON),
                houses = listOf(moonPos.house),
                description = "Moon in Abhijit, the victorious nakshatra",
                effects = "Victory in all endeavors, natural luck, success without much struggle, divine protection, auspicious birth",
                strength = YogaStrength.EXTREMELY_STRONG,
                strengthPercentage = 85.0,
                isAuspicious = true,
                activationPeriod = "Throughout life",
                cancellationFactors = emptyList()
            )
        }

        return null
    }

    // ==================== HELPER FUNCTIONS ====================

    private fun ordinal(n: Int): String = when (n) {
        1 -> "1st"
        2 -> "2nd"
        3 -> "3rd"
        else -> "${n}th"
    }

    /**
     * Calculate Navamsa sign from longitude
     */
    private fun calculateNavamsaSign(longitude: Double): ZodiacSign {
        val navamsaIndex = ((longitude / 3.333) % 12).toInt()
        val signOrdinal = (longitude / 30.0).toInt()
        val startSign = when (signOrdinal % 3) {
            0 -> signOrdinal * 9 % 12      // Fire sign - starts from same sign
            1 -> (signOrdinal * 9 + 8) % 12 // Earth sign  
            2 -> (signOrdinal * 9 + 4) % 12 // Air sign
            else -> navamsaIndex
        }
        return ZodiacSign.entries[navamsaIndex]
    }
}
