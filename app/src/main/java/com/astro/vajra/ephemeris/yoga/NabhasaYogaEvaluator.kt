package com.astro.vajra.ephemeris.yoga

import com.astro.vajra.core.model.Planet
import com.astro.vajra.core.model.VedicChart
import kotlin.math.abs

/**
 * Nabhasa Yoga Evaluator - Pattern-Based Planetary Combinations
 *
 * Nabhasa (celestial/sky) Yogas are based on the overall pattern of planets
 * in the chart rather than specific house lords. They are divided into:
 * 1. Akriti (shape) yogas based on planetary distribution
 * 2. Sankhya (number) yogas based on planet counts in houses
 * 3. Ashraya (base) yogas based on sign/element distribution
 *
 * Types evaluated:
 * - Yava (Barley) - Planets spread with gap in middle
 * - Shringataka (Triangle) - Planets in 1, 5, 9 or 4, 8, 12
 * - Gada (Mace) - Planets in two adjacent Kendras
 * - Shakata (Cart) - Planets only in 1 and 7
 * - Rajju (Rope) - Planets only in movable signs
 * - Musala (Pestle) - Planets only in fixed signs
 * - Nala (Reed) - Planets only in dual signs
 * - Kedara (Field) - Planets in 4 houses
 * - Shoola (Trident) - Planets in 3 consecutive houses
 * - Yuga (Age) - Planets in 2 houses
 * - Gola (Sphere) - Planets in 1 house
 * - Veena (Lute) - Planets in 7 consecutive houses
 *
 * Based on:
 * - Brihat Parasara Hora Shastra (BPHS), Chapter 35
 * - Saravali
 *
 * @author AstroVajra
 */
class NabhasaYogaEvaluator : YogaEvaluator {

    override val category: YogaCategory = YogaCategory.NABHASA_YOGA

    override fun evaluate(chart: VedicChart): List<Yoga> {
        val yogas = mutableListOf<Yoga>()

        // Get occupied houses (excluding Rahu/Ketu for some calculations)
        val mainPlanets = listOf(Planet.SUN, Planet.MOON, Planet.MARS, Planet.MERCURY,
            Planet.JUPITER, Planet.VENUS, Planet.SATURN)
        val occupiedHouses = chart.planetPositions
            .filter { it.planet in mainPlanets }
            .map { it.house }
            .distinct()
            .sorted()

        // Check Sankhya Yogas (based on number of occupied houses)
        evaluateSankhyaYoga(chart, occupiedHouses)?.let { yogas.add(it) }

        // Check Akriti (shape) Yogas
        evaluateYavaYoga(chart, occupiedHouses)?.let { yogas.add(it) }
        evaluateShringatakaYoga(chart, occupiedHouses)?.let { yogas.add(it) }
        evaluateGadaYoga(chart, occupiedHouses)?.let { yogas.add(it) }
        evaluateShakatYoga(chart, occupiedHouses)?.let { yogas.add(it) }
        evaluateShoolaYoga(chart, occupiedHouses)?.let { yogas.add(it) }
        evaluateVeenaYoga(chart, occupiedHouses)?.let { yogas.add(it) }

        // Check Ashraya (sign quality) Yogas
        yogas.addAll(evaluateAshrayaYogas(chart))

        return yogas
    }

    /**
     * Sankhya Yogas based on number of occupied houses (1 to 7)
     */
    private fun evaluateSankhyaYoga(chart: VedicChart, occupiedHouses: List<Int>): Yoga? {
        val count = occupiedHouses.size
        if (count < 1 || count > 7) return null
        
        val baseStrength = 50.0 + (12 - count) * 3.0

        return when (count) {
            1 -> Yoga(
                name = "Gola Sankhya Yoga",
                sanskritName = "Gola Yoga",
                category = YogaCategory.NABHASA_YOGA,
                planets = emptyList(),
                houses = occupiedHouses,
                description = "All 7 planets in one house",
                effects = "Struggles in early life, need for persistence, unconventional path",
                strength = YogaHelpers.strengthFromPercentage(baseStrength),
                strengthPercentage = baseStrength,
                isAuspicious = occupiedHouses.first() in listOf(1, 2, 5, 9, 10, 11),
                activationPeriod = "Throughout life",
                cancellationFactors = emptyList(),
                nameKey = com.astro.vajra.core.common.StringKeyMatch.YOGA_SANKHYA_GOLA,
                descriptionKey = com.astro.vajra.core.common.StringKeyYogaExpanded.YOGA_CAT_NABHASA, // Placeholder or specific
                effectsKey = com.astro.vajra.core.common.StringKeyMatch.YOGA_EFFECT_SANKHYA_GOLA
            )
            2 -> Yoga(
                name = "Yuga Sankhya Yoga",
                sanskritName = "Yuga Yoga",
                category = YogaCategory.NABHASA_YOGA,
                planets = emptyList(),
                houses = occupiedHouses,
                description = "All planets in two houses",
                effects = "Unconventional lifestyle, may face social challenges, religious or philosophical",
                strength = YogaHelpers.strengthFromPercentage(baseStrength),
                strengthPercentage = baseStrength,
                isAuspicious = true,
                activationPeriod = "Throughout life",
                cancellationFactors = emptyList(),
                nameKey = com.astro.vajra.core.common.StringKeyMatch.YOGA_SANKHYA_YUGA,
                effectsKey = com.astro.vajra.core.common.StringKeyMatch.YOGA_EFFECT_SANKHYA_YUGA
            )
            3 -> Yoga(
                name = "Shoola Sankhya Yoga",
                sanskritName = "Shoola Yoga",
                category = YogaCategory.NABHASA_YOGA,
                planets = emptyList(),
                houses = occupiedHouses,
                description = "All planets in three houses",
                effects = "Brave but potentially aggressive, success through competition",
                strength = YogaHelpers.strengthFromPercentage(baseStrength),
                strengthPercentage = baseStrength,
                isAuspicious = true,
                activationPeriod = "Throughout life",
                cancellationFactors = emptyList(),
                nameKey = com.astro.vajra.core.common.StringKeyMatch.YOGA_SANKHYA_SHOOLA,
                effectsKey = com.astro.vajra.core.common.StringKeyMatch.YOGA_EFFECT_SANKHYA_SHOOLA
            )
            4 -> Yoga(
                name = "Kedara Sankhya Yoga",
                sanskritName = "Kedara Yoga",
                category = YogaCategory.NABHASA_YOGA,
                planets = emptyList(),
                houses = occupiedHouses,
                description = "All planets in four houses",
                effects = "Agricultural wealth, helpful, truthful, steady progress",
                strength = YogaHelpers.strengthFromPercentage(baseStrength),
                strengthPercentage = baseStrength,
                isAuspicious = true,
                activationPeriod = "Throughout life",
                cancellationFactors = emptyList(),
                nameKey = com.astro.vajra.core.common.StringKeyMatch.YOGA_SANKHYA_KEDARA,
                effectsKey = com.astro.vajra.core.common.StringKeyMatch.YOGA_EFFECT_SANKHYA_KEDARA
            )
            5 -> Yoga(
                name = "Pasa Sankhya Yoga",
                sanskritName = "Pasa Yoga",
                category = YogaCategory.NABHASA_YOGA,
                planets = emptyList(),
                houses = occupiedHouses,
                description = "All planets in five houses",
                effects = "Surrounded by friends and relatives, earns through proper means",
                strength = YogaHelpers.strengthFromPercentage(baseStrength),
                strengthPercentage = baseStrength,
                isAuspicious = true,
                activationPeriod = "Throughout life",
                cancellationFactors = emptyList(),
                nameKey = com.astro.vajra.core.common.StringKeyMatch.YOGA_SANKHYA_PASA,
                effectsKey = com.astro.vajra.core.common.StringKeyMatch.YOGA_EFFECT_SANKHYA_PASA
            )
            6 -> Yoga(
                name = "Damini Sankhya Yoga",
                sanskritName = "Damini Yoga",
                category = YogaCategory.NABHASA_YOGA,
                planets = emptyList(),
                houses = occupiedHouses,
                description = "All planets in six houses",
                effects = "Charitable, famous, helpful to others, intelligent",
                strength = YogaHelpers.strengthFromPercentage(baseStrength),
                strengthPercentage = baseStrength,
                isAuspicious = true,
                activationPeriod = "Throughout life",
                cancellationFactors = emptyList(),
                nameKey = com.astro.vajra.core.common.StringKeyMatch.YOGA_SANKHYA_DAMINI,
                effectsKey = com.astro.vajra.core.common.StringKeyMatch.YOGA_EFFECT_SANKHYA_DAMINI
            )
            7 -> Yoga(
                name = "Vallaki Sankhya Yoga",
                sanskritName = "Vallaki Yoga",
                category = YogaCategory.NABHASA_YOGA,
                planets = emptyList(),
                houses = occupiedHouses,
                description = "All planets in seven houses",
                effects = "Artistic talents, fond of music and dance, happy disposition",
                strength = YogaHelpers.strengthFromPercentage(baseStrength),
                strengthPercentage = baseStrength,
                isAuspicious = true,
                activationPeriod = "Throughout life",
                cancellationFactors = emptyList(),
                nameKey = com.astro.vajra.core.common.StringKeyMatch.YOGA_SANKHYA_VALLAKI,
                effectsKey = com.astro.vajra.core.common.StringKeyMatch.YOGA_EFFECT_SANKHYA_VALLAKI
            )
            else -> null
        }
    }

    /**
     * Yava Yoga - Barley shape, planets concentrated at ends
     */
    private fun evaluateYavaYoga(chart: VedicChart, occupiedHouses: List<Int>): Yoga? {
        if (occupiedHouses.size < 5) return null

        // Check for barley shape: more planets at the edges, fewer in middle
        val firstQuarter = occupiedHouses.count { it in 1..3 }
        val lastQuarter = occupiedHouses.count { it in 10..12 }
        val middleTwo = occupiedHouses.count { it in 4..9 }

        if (firstQuarter >= 2 && lastQuarter >= 2 && middleTwo < (firstQuarter + lastQuarter)) {
            val strength = 55.0
            return Yoga(
                name = "Yava Yoga",
                sanskritName = "Yava Yoga",
                category = YogaCategory.NABHASA_YOGA,
                planets = emptyList(),
                houses = occupiedHouses,
                description = "Planets distributed in barley grain pattern",
                effects = "Generous nature, charitable inclinations, modest prosperity",
                strength = YogaHelpers.strengthFromPercentage(strength),
                strengthPercentage = strength,
                isAuspicious = true,
                activationPeriod = "Throughout life",
                cancellationFactors = emptyList(),
                nameKey = com.astro.vajra.core.common.StringKeyMatch.YOGA_YAVA,
                effectsKey = com.astro.vajra.core.common.StringKeyMatch.YOGA_EFFECT_YAVA
            )
        }

        return null
    }

    /**
     * Shringataka Yoga - Triangle pattern (1,5,9 or 4,8,12)
     */
    private fun evaluateShringatakaYoga(chart: VedicChart, occupiedHouses: List<Int>): Yoga? {
        val trikona = setOf(1, 5, 9)
        val dusthana = setOf(4, 8, 12)

        val trikonaCount = occupiedHouses.count { it in trikona }
        val dusthanaCount = occupiedHouses.count { it in dusthana }

        if (trikonaCount >= 3 && trikonaCount == occupiedHouses.size) {
            return Yoga(
                name = "Shringataka Yoga (Trikona)",
                sanskritName = "Shringataka Yoga",
                category = YogaCategory.NABHASA_YOGA,
                planets = emptyList(),
                houses = occupiedHouses,
                description = "Planets in triangular Trikona pattern (1,5,9)",
                effects = "Highly fortunate life, good past karma manifesting, spiritual success",
                strength = YogaStrength.STRONG,
                strengthPercentage = 75.0,
                isAuspicious = true,
                activationPeriod = "Throughout life",
                cancellationFactors = emptyList(),
                nameKey = com.astro.vajra.core.common.StringKeyMatch.YOGA_SHRINGATAKA,
                effectsKey = com.astro.vajra.core.common.StringKeyMatch.YOGA_EFFECT_SHRINGATAKA
            )
        }

        if (dusthanaCount >= 3) {
            return Yoga(
                name = "Shringataka Yoga (Dusthana)",
                sanskritName = "Shringataka Yoga",
                category = YogaCategory.NABHASA_YOGA,
                planets = emptyList(),
                houses = occupiedHouses,
                description = "Planets in triangular Dusthana pattern (4,8,12)",
                effects = "Challenges in early life, spiritual transformation, eventual liberation",
                strength = YogaStrength.MODERATE,
                strengthPercentage = 45.0,
                isAuspicious = false,
                activationPeriod = "Throughout life",
                cancellationFactors = listOf("Strong benefics can mitigate"),
                nameKey = com.astro.vajra.core.common.StringKeyMatch.YOGA_SHRINGATAKA,
                effectsKey = com.astro.vajra.core.common.StringKeyMatch.YOGA_EFFECT_SHRINGATAKA
            )
        }

        return null
    }

    /**
     * Gada Yoga - Mace shape, planets in two adjacent Kendras
     */
    private fun evaluateGadaYoga(chart: VedicChart, occupiedHouses: List<Int>): Yoga? {
        val kendraPairs = listOf(
            setOf(1, 4), setOf(4, 7), setOf(7, 10), setOf(10, 1)
        )

        kendraPairs.forEach { pair ->
            val inPair = occupiedHouses.filter { it in pair }
            if (inPair.size >= 4) { // At least 4 planets in adjacent kendras
                return Yoga(
                    name = "Gada Yoga",
                    sanskritName = "Gada Yoga",
                    category = YogaCategory.NABHASA_YOGA,
                    planets = emptyList(),
                    houses = inPair,
                    description = "Planets in adjacent Kendra houses like a mace",
                    effects = "Wealthy through efforts, determined nature, success in competitions",
                    strength = YogaStrength.STRONG,
                    strengthPercentage = 70.0,
                    isAuspicious = true,
                    activationPeriod = "Throughout life",
                    cancellationFactors = emptyList(),
                    nameKey = com.astro.vajra.core.common.StringKeyMatch.YOGA_GADA,
                    effectsKey = com.astro.vajra.core.common.StringKeyMatch.YOGA_EFFECT_GADA
                )
            }
        }

        return null
    }

    /**
     * Shakata Yoga - Cart shape, planets only in 1 and 7 (opposites)
     */
    private fun evaluateShakatYoga(chart: VedicChart, occupiedHouses: List<Int>): Yoga? {
        if (occupiedHouses.containsAll(listOf(1, 7)) && occupiedHouses.all { it == 1 || it == 7 }) {
            return Yoga(
                name = "Shakata Yoga",
                sanskritName = "Shakata Yoga",
                category = YogaCategory.NABHASA_YOGA,
                planets = emptyList(),
                houses = occupiedHouses,
                description = "Planets only in 1st and 7th houses like cart wheels",
                effects = "Fluctuating fortune, ups and downs in life, dependent on relationships",
                strength = YogaStrength.MODERATE,
                strengthPercentage = 50.0,
                isAuspicious = false,
                activationPeriod = "Throughout life",
                cancellationFactors = listOf("Jupiter aspect can stabilize"),
                nameKey = com.astro.vajra.core.common.StringKeyMatch.YOGA_SHAKATA,
                effectsKey = com.astro.vajra.core.common.StringKeyMatch.YOGA_EFFECT_SHAKATA_NABHASA
            )
        }

        return null
    }

    /**
     * Shoola Yoga - Trident, planets in 3 consecutive houses
     */
    private fun evaluateShoolaYoga(chart: VedicChart, occupiedHouses: List<Int>): Yoga? {
        if (occupiedHouses.size != 3) return null

        // Check if all three are consecutive
        val sorted = occupiedHouses.sorted()
        val isConsecutive = (sorted[1] == sorted[0] + 1 && sorted[2] == sorted[1] + 1) ||
                (sorted == listOf(1, 11, 12)) || (sorted == listOf(1, 2, 12))

        if (isConsecutive) {
            return Yoga(
                name = "Shoola Yoga",
                sanskritName = "Shoola Yoga",
                category = YogaCategory.NABHASA_YOGA,
                planets = emptyList(),
                houses = occupiedHouses,
                description = "Planets in 3 consecutive houses like a trident",
                effects = "Sharp intellect, focused nature, success in specific domain",
                strength = YogaStrength.MODERATE,
                strengthPercentage = 55.0,
                isAuspicious = true,
                activationPeriod = "Throughout life",
                cancellationFactors = emptyList(),
                nameKey = com.astro.vajra.core.common.StringKeyMatch.YOGA_SHOOLA,
                effectsKey = com.astro.vajra.core.common.StringKeyMatch.YOGA_EFFECT_SHOOLA
            )
        }

        return null
    }

    /**
     * Veena Yoga - Lute, planets in 7 consecutive houses
     */
    private fun evaluateVeenaYoga(chart: VedicChart, occupiedHouses: List<Int>): Yoga? {
        if (occupiedHouses.size != 7) return null

        val sorted = occupiedHouses.sorted()
        var consecutive = true
        for (i in 0 until sorted.size - 1) {
            if (sorted[i + 1] != sorted[i] + 1) {
                consecutive = false
                break
            }
        }

        if (consecutive) {
            return Yoga(
                name = "Veena Yoga",
                sanskritName = "Veena Yoga",
                category = YogaCategory.NABHASA_YOGA,
                planets = emptyList(),
                houses = occupiedHouses,
                description = "Planets span 7 consecutive houses like a lute",
                effects = "Musical talent, artistic abilities, loved by all, happy disposition",
                strength = YogaStrength.STRONG,
                strengthPercentage = 70.0,
                isAuspicious = true,
                activationPeriod = "Throughout life",
                cancellationFactors = emptyList(),
                nameKey = com.astro.vajra.core.common.StringKeyMatch.YOGA_VEENA,
                effectsKey = com.astro.vajra.core.common.StringKeyMatch.YOGA_EFFECT_VEENA
            )
        }

        return null
    }

    /**
     * Ashraya Yogas - Based on sign qualities (Movable/Fixed/Dual)
     */
    private fun evaluateAshrayaYogas(chart: VedicChart): List<Yoga> {
        val yogas = mutableListOf<Yoga>()
        val mainPlanets = listOf(Planet.SUN, Planet.MOON, Planet.MARS, Planet.MERCURY,
            Planet.JUPITER, Planet.VENUS, Planet.SATURN)

        val positions = chart.planetPositions.filter { it.planet in mainPlanets }

        // Movable signs: Aries, Cancer, Libra, Capricorn
        val movable = listOf(
            com.astro.vajra.core.model.ZodiacSign.ARIES,
            com.astro.vajra.core.model.ZodiacSign.CANCER,
            com.astro.vajra.core.model.ZodiacSign.LIBRA,
            com.astro.vajra.core.model.ZodiacSign.CAPRICORN
        )

        // Fixed signs: Taurus, Leo, Scorpio, Aquarius
        val fixed = listOf(
            com.astro.vajra.core.model.ZodiacSign.TAURUS,
            com.astro.vajra.core.model.ZodiacSign.LEO,
            com.astro.vajra.core.model.ZodiacSign.SCORPIO,
            com.astro.vajra.core.model.ZodiacSign.AQUARIUS
        )

        // Dual signs: Gemini, Virgo, Sagittarius, Pisces
        val dual = listOf(
            com.astro.vajra.core.model.ZodiacSign.GEMINI,
            com.astro.vajra.core.model.ZodiacSign.VIRGO,
            com.astro.vajra.core.model.ZodiacSign.SAGITTARIUS,
            com.astro.vajra.core.model.ZodiacSign.PISCES
        )

        val movableCount = positions.count { it.sign in movable }
        val fixedCount = positions.count { it.sign in fixed }
        val dualCount = positions.count { it.sign in dual }

        // Rajju Yoga - All planets in movable signs
        if (movableCount == positions.size && positions.isNotEmpty()) {
            yogas.add(Yoga(
                name = "Rajju Yoga",
                sanskritName = "Rajju Yoga",
                category = YogaCategory.NABHASA_YOGA,
                planets = emptyList(),
                houses = positions.map { it.house }.distinct(),
                description = "All planets in movable (cardinal) signs",
                effects = "Active life, frequent travels, adaptable nature, success in changeable fields",
                strength = YogaStrength.MODERATE,
                strengthPercentage = 55.0,
                isAuspicious = true,
                activationPeriod = "Throughout life",
                cancellationFactors = emptyList(),
                nameKey = com.astro.vajra.core.common.StringKeyMatch.YOGA_RAJJU,
                effectsKey = com.astro.vajra.core.common.StringKeyMatch.YOGA_EFFECT_RAJJU
            ))
        }

        // Musala Yoga - All planets in fixed signs
        if (fixedCount == positions.size && positions.isNotEmpty()) {
            yogas.add(Yoga(
                name = "Musala Yoga",
                sanskritName = "Musala Yoga",
                category = YogaCategory.NABHASA_YOGA,
                planets = emptyList(),
                houses = positions.map { it.house }.distinct(),
                description = "All planets in fixed signs",
                effects = "Stable life, wealth accumulation, stubborn nature, long-lasting achievements",
                strength = YogaStrength.MODERATE,
                strengthPercentage = 55.0,
                isAuspicious = true,
                activationPeriod = "Throughout life",
                cancellationFactors = emptyList(),
                nameKey = com.astro.vajra.core.common.StringKeyMatch.YOGA_MUSALA,
                effectsKey = com.astro.vajra.core.common.StringKeyMatch.YOGA_EFFECT_MUSALA
            ))
        }

        // Nala Yoga - All planets in dual signs
        if (dualCount == positions.size && positions.isNotEmpty()) {
            yogas.add(Yoga(
                name = "Nala Yoga",
                sanskritName = "Nala Yoga",
                category = YogaCategory.NABHASA_YOGA,
                planets = emptyList(),
                houses = positions.map { it.house }.distinct(),
                description = "All planets in dual (mutable) signs",
                effects = "Versatile abilities, success in multiple fields, diplomatic nature, intellectual pursuits",
                strength = YogaStrength.MODERATE,
                strengthPercentage = 55.0,
                isAuspicious = true,
                activationPeriod = "Throughout life",
                cancellationFactors = emptyList(),
                nameKey = com.astro.vajra.core.common.StringKeyMatch.YOGA_NALA,
                effectsKey = com.astro.vajra.core.common.StringKeyMatch.YOGA_EFFECT_NALA
            ))
        }

        return yogas
    }
}
