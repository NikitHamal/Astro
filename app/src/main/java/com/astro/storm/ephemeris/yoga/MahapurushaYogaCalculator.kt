package com.astro.storm.ephemeris.yoga

import com.astro.storm.data.model.Planet
import com.astro.storm.data.model.VedicChart
import com.astro.storm.data.model.ZodiacSign

/**
 * Pancha Mahapurusha Yoga Calculator
 *
 * The five great person yogas formed when Mars, Mercury, Jupiter, Venus,
 * or Saturn is in its own sign or exaltation AND in a Kendra house (1,4,7,10).
 *
 * Yogas calculated:
 * 1. Ruchaka Yoga - Mars in own/exalted sign in Kendra
 * 2. Bhadra Yoga - Mercury in own/exalted sign in Kendra
 * 3. Hamsa Yoga - Jupiter in own/exalted sign in Kendra
 * 4. Malavya Yoga - Venus in own/exalted sign in Kendra
 * 5. Sasa Yoga - Saturn in own/exalted sign in Kendra
 *
 * Reference: BPHS Chapter 75, Phaladeepika Chapter 6
 *
 * @author AstroStorm - Ultra-Precision Vedic Astrology
 */
object MahapurushaYogaCalculator {

    private val KENDRA_HOUSES = listOf(1, 4, 7, 10)

    /**
     * Calculate all Pancha Mahapurusha Yogas
     */
    fun calculate(chart: VedicChart): List<Yoga> {
        val yogas = mutableListOf<Yoga>()

        // Ruchaka Yoga - Mars
        calculateRuchakaYoga(chart)?.let { yogas.add(it) }

        // Bhadra Yoga - Mercury
        calculateBhadraYoga(chart)?.let { yogas.add(it) }

        // Hamsa Yoga - Jupiter
        calculateHamsaYoga(chart)?.let { yogas.add(it) }

        // Malavya Yoga - Venus
        calculateMalavyaYoga(chart)?.let { yogas.add(it) }

        // Sasa Yoga - Saturn
        calculateSasaYoga(chart)?.let { yogas.add(it) }

        return yogas
    }

    /**
     * Ruchaka Yoga - Mars in Aries, Scorpio, or Capricorn in Kendra
     */
    private fun calculateRuchakaYoga(chart: VedicChart): Yoga? {
        val marsPos = chart.planetPositions.find { it.planet == Planet.MARS } ?: return null

        if (marsPos.house in KENDRA_HOUSES) {
            if (marsPos.sign in listOf(ZodiacSign.ARIES, ZodiacSign.SCORPIO, ZodiacSign.CAPRICORN)) {
                val (strength, cancellations) = YogaUtils.calculateMahapurushaStrengthWithReasons(marsPos, chart)
                return Yoga(
                    name = "Ruchaka Yoga",
                    sanskritName = "Ruchaka Mahapurusha Yoga",
                    category = YogaCategory.MAHAPURUSHA_YOGA,
                    planets = listOf(Planet.MARS),
                    houses = listOf(marsPos.house),
                    description = "Mars in own/exalted sign in Kendra",
                    effects = "Commander, army chief, valorous, muscular body, red complexion, successful in conflicts, " +
                            "skilled in warfare, leader of thieves or soldiers, wealth through martial arts or defense",
                    strength = YogaUtils.strengthFromPercentage(strength),
                    strengthPercentage = strength,
                    isAuspicious = true,
                    activationPeriod = "Mars Mahadasha and related Antardashas",
                    cancellationFactors = cancellations.ifEmpty { listOf("None - yoga is unafflicted") }
                )
            }
        }

        return null
    }

    /**
     * Bhadra Yoga - Mercury in Gemini or Virgo in Kendra
     */
    private fun calculateBhadraYoga(chart: VedicChart): Yoga? {
        val mercuryPos = chart.planetPositions.find { it.planet == Planet.MERCURY } ?: return null

        if (mercuryPos.house in KENDRA_HOUSES) {
            if (mercuryPos.sign in listOf(ZodiacSign.GEMINI, ZodiacSign.VIRGO)) {
                val (strength, cancellations) = YogaUtils.calculateMahapurushaStrengthWithReasons(mercuryPos, chart)
                return Yoga(
                    name = "Bhadra Yoga",
                    sanskritName = "Bhadra Mahapurusha Yoga",
                    category = YogaCategory.MAHAPURUSHA_YOGA,
                    planets = listOf(Planet.MERCURY),
                    houses = listOf(mercuryPos.house),
                    description = "Mercury in own/exalted sign in Kendra",
                    effects = "Intelligent, eloquent speaker, skilled in arts and sciences, long-lived, " +
                            "wealthy through intellect, respected in assemblies, lion-like face, broad chest",
                    strength = YogaUtils.strengthFromPercentage(strength),
                    strengthPercentage = strength,
                    isAuspicious = true,
                    activationPeriod = "Mercury Mahadasha and related Antardashas",
                    cancellationFactors = cancellations.ifEmpty { listOf("None - yoga is unafflicted") }
                )
            }
        }

        return null
    }

    /**
     * Hamsa Yoga - Jupiter in Sagittarius, Pisces, or Cancer in Kendra
     */
    private fun calculateHamsaYoga(chart: VedicChart): Yoga? {
        val jupiterPos = chart.planetPositions.find { it.planet == Planet.JUPITER } ?: return null

        if (jupiterPos.house in KENDRA_HOUSES) {
            if (jupiterPos.sign in listOf(ZodiacSign.SAGITTARIUS, ZodiacSign.PISCES, ZodiacSign.CANCER)) {
                val (strength, cancellations) = YogaUtils.calculateMahapurushaStrengthWithReasons(jupiterPos, chart)
                return Yoga(
                    name = "Hamsa Yoga",
                    sanskritName = "Hamsa Mahapurusha Yoga",
                    category = YogaCategory.MAHAPURUSHA_YOGA,
                    planets = listOf(Planet.JUPITER),
                    houses = listOf(jupiterPos.house),
                    description = "Jupiter in own/exalted sign in Kendra",
                    effects = "Righteous king, fair complexion, elevated nose, beautiful face, devoted to gods and brahmins, " +
                            "fond of water sports, walks like a swan, respected by rulers, spiritual inclinations",
                    strength = YogaUtils.strengthFromPercentage(strength),
                    strengthPercentage = strength,
                    isAuspicious = true,
                    activationPeriod = "Jupiter Mahadasha and related Antardashas",
                    cancellationFactors = cancellations.ifEmpty { listOf("None - yoga is unafflicted") }
                )
            }
        }

        return null
    }

    /**
     * Malavya Yoga - Venus in Taurus, Libra, or Pisces in Kendra
     */
    private fun calculateMalavyaYoga(chart: VedicChart): Yoga? {
        val venusPos = chart.planetPositions.find { it.planet == Planet.VENUS } ?: return null

        if (venusPos.house in KENDRA_HOUSES) {
            if (venusPos.sign in listOf(ZodiacSign.TAURUS, ZodiacSign.LIBRA, ZodiacSign.PISCES)) {
                val (strength, cancellations) = YogaUtils.calculateMahapurushaStrengthWithReasons(venusPos, chart)
                return Yoga(
                    name = "Malavya Yoga",
                    sanskritName = "Malavya Mahapurusha Yoga",
                    category = YogaCategory.MAHAPURUSHA_YOGA,
                    planets = listOf(Planet.VENUS),
                    houses = listOf(venusPos.house),
                    description = "Venus in own/exalted sign in Kendra",
                    effects = "Wealthy, enjoys all comforts, beautiful spouse, strong limbs, attractive face, " +
                            "blessed with vehicles and servants, learned in scriptures, lives up to 77 years",
                    strength = YogaUtils.strengthFromPercentage(strength),
                    strengthPercentage = strength,
                    isAuspicious = true,
                    activationPeriod = "Venus Mahadasha and related Antardashas",
                    cancellationFactors = cancellations.ifEmpty { listOf("None - yoga is unafflicted") }
                )
            }
        }

        return null
    }

    /**
     * Sasa Yoga - Saturn in Capricorn, Aquarius, or Libra in Kendra
     */
    private fun calculateSasaYoga(chart: VedicChart): Yoga? {
        val saturnPos = chart.planetPositions.find { it.planet == Planet.SATURN } ?: return null

        if (saturnPos.house in KENDRA_HOUSES) {
            if (saturnPos.sign in listOf(ZodiacSign.CAPRICORN, ZodiacSign.AQUARIUS, ZodiacSign.LIBRA)) {
                val (strength, cancellations) = YogaUtils.calculateMahapurushaStrengthWithReasons(saturnPos, chart)
                return Yoga(
                    name = "Sasa Yoga",
                    sanskritName = "Sasa Mahapurusha Yoga",
                    category = YogaCategory.MAHAPURUSHA_YOGA,
                    planets = listOf(Planet.SATURN),
                    houses = listOf(saturnPos.house),
                    description = "Saturn in own/exalted sign in Kendra",
                    effects = "Head of village/town/city, wicked disposition but good servants, intriguing nature, " +
                            "knows others' weaknesses, commands over masses, wealth through iron or labor",
                    strength = YogaUtils.strengthFromPercentage(strength),
                    strengthPercentage = strength,
                    isAuspicious = true,
                    activationPeriod = "Saturn Mahadasha and related Antardashas",
                    cancellationFactors = cancellations.ifEmpty { listOf("None - yoga is unafflicted") }
                )
            }
        }

        return null
    }
}
