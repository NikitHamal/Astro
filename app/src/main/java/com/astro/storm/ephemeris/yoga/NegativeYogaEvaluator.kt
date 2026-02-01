package com.astro.storm.ephemeris.yoga

import com.astro.storm.core.model.Planet
import com.astro.storm.core.model.PlanetPosition
import com.astro.storm.core.model.VedicChart
import com.astro.storm.core.model.ZodiacSign

/**
 * Negative Yoga Evaluator - Challenging Planetary Combinations
 *
 * Negative (Arishta) Yogas indicate challenges, obstacles, or difficulties.
 * Understanding these helps prepare remedial measures.
 *
 * Types evaluated:
 * 1. Daridra Yoga - 11th lord in 6th/8th/12th
 * 2. Guru-Chandal Yoga - Jupiter conjunct Rahu
 * 3. Grahan Yogas - Sun/Moon with Rahu/Ketu (eclipse combinations)
 * 4. Angarak Yoga - Mars conjunct Rahu
 * 5. Shrapit Yoga - Saturn conjunct Rahu
 * 6. Kala Sarpa Yoga - All planets hemmed between Rahu-Ketu axis
 * 7. Papakartari Yoga - Ascendant/Moon hemmed between malefics
 *
 * Based on:
 * - Brihat Parasara Hora Shastra (BPHS), Chapter 44
 * - Phaladeepika, Chapter 14
 *
 * @author AstroStorm
 */
class NegativeYogaEvaluator : YogaEvaluator {

    override val category: YogaCategory = YogaCategory.NEGATIVE_YOGA

    override fun evaluate(chart: VedicChart): List<Yoga> {
        val yogas = mutableListOf<Yoga>()
        val ascendantSign = ZodiacSign.fromLongitude(chart.ascendant)
        val houseLords = YogaHelpers.getHouseLords(ascendantSign)

        // 1. Daridra Yoga
        evaluateDaridraYoga(chart, houseLords)?.let { yogas.add(it) }

        // 2. Guru-Chandal Yoga
        evaluateGuruChandalYoga(chart)?.let { yogas.add(it) }

        // 3. Grahan Yogas (Eclipse combinations)
        yogas.addAll(evaluateGrahanYogas(chart))

        // 4. Angarak Yoga
        evaluateAngarakYoga(chart)?.let { yogas.add(it) }

        // 5. Shrapit Yoga
        evaluateShrapitYoga(chart)?.let { yogas.add(it) }

        // 6. Kala Sarpa Yoga
        evaluateKalaSarpaYoga(chart)?.let { yogas.add(it) }

        // 7. Papakartari Yoga
        yogas.addAll(evaluatePapakartariYogas(chart))

        // 8. Sakata Yoga
        evaluateSakataYoga(chart)?.let { yogas.add(it) }

        return yogas
    }

    /**
     * Sakata Yoga - Moon in 6, 8, 12 from Jupiter
     * Sakata Bhanga occurs if Moon is in a Kendra from Lagna.
     */
    private fun evaluateSakataYoga(chart: VedicChart): Yoga? {
        val moonPos = chart.planetPositions.find { it.planet == Planet.MOON } ?: return null
        val jupiterPos = chart.planetPositions.find { it.planet == Planet.JUPITER } ?: return null
        
        val houseFromJupiter = YogaHelpers.getHouseFrom(moonPos.sign, jupiterPos.sign)
        
        if (houseFromJupiter in listOf(6, 8, 12)) {
            // Check for Sakata Bhanga (Cancellation)
            // If Moon is in Kendra from Lagna, Sakata is cancelled
            val isCancelled = moonPos.house in listOf(1, 4, 7, 10)
            
            if (isCancelled) {
                val strength = 55.0
                return Yoga(
                    name = "Kemadruma Bhanga (Sakata Cancellation)",
                    sanskritName = "Sakata Bhanga",
                    category = YogaCategory.SPECIAL_YOGA,
                    planets = listOf(Planet.MOON, Planet.JUPITER),
                    houses = listOf(moonPos.house, jupiterPos.house),
                    description = "Moon in Kendra from Lagna cancels Sakata Yoga",
                    effects = "Overcoming initial struggles, internal strength to face life's ups and downs",
                    strength = YogaHelpers.strengthFromPercentage(strength),
                    strengthPercentage = strength,
                    isAuspicious = true,
                    activationPeriod = "Moon and Jupiter periods",
                    cancellationFactors = emptyList(),
                    nameKey = com.astro.storm.core.common.StringKeyPanchanga.YOGA_KEMADRUMA_BHANGA,
                    effectsKey = com.astro.storm.core.common.StringKeyPanchanga.YOGA_EFFECT_KEMADRUMA_BHANGA
                )
            }
            
            val severity = 60.0
            return Yoga(
                name = "Sakata Yoga",
                sanskritName = "Sakata Yoga",
                category = YogaCategory.NEGATIVE_YOGA,
                planets = listOf(Planet.MOON, Planet.JUPITER),
                houses = listOf(moonPos.house, jupiterPos.house),
                description = "Moon in $houseFromJupiter house from Jupiter",
                effects = "Fluctuating fortunes, periods of struggle followed by recovery, lessons in resilience",
                strength = YogaHelpers.strengthFromPercentage(severity),
                strengthPercentage = severity,
                isAuspicious = false,
                activationPeriod = "Moon and Jupiter periods",
                cancellationFactors = listOf("Cancelled if Moon is in Kendra from Lagna"),
                nameKey = com.astro.storm.core.common.StringKeyPanchanga.YOGA_SAKATA,
                effectsKey = com.astro.storm.core.common.StringKeyPanchanga.YOGA_EFFECT_SAKATA
            )
        }
        
        return null
    }

    /**
     * Daridra Yoga - 11th lord in Dusthana (6, 8, 12)
     */
    private fun evaluateDaridraYoga(chart: VedicChart, houseLords: Map<Int, Planet>): Yoga? {
        val lord11 = houseLords[11] ?: return null
        val lord11Pos = chart.planetPositions.find { it.planet == lord11 } ?: return null

        if (lord11Pos.house in listOf(6, 8, 12)) {
            val severity = when (lord11Pos.house) {
                8 -> 70.0
                12 -> 60.0
                6 -> 50.0
                else -> 55.0
            }

            // Check for mitigation
            val cancellationReasons = mutableListOf<String>()
            if (YogaHelpers.isExalted(lord11Pos) || YogaHelpers.isInOwnSign(lord11Pos)) {
                cancellationReasons.add("${lord11.displayName} in dignity - effects reduced")
            }

            val jupiterPos = chart.planetPositions.find { it.planet == Planet.JUPITER }
            if (jupiterPos != null && YogaHelpers.isAspecting(jupiterPos, lord11Pos)) {
                cancellationReasons.add("Jupiter aspect provides protection")
            }

            return Yoga(
                name = "Daridra Yoga",
                sanskritName = "Daridra Yoga",
                category = YogaCategory.NEGATIVE_YOGA,
                planets = listOf(lord11),
                houses = listOf(lord11Pos.house),
                description = "11th lord ${lord11.displayName} in ${lord11Pos.house}th house",
                effects = "Difficulties in achieving gains, financial struggles, unfulfilled desires",
                strength = YogaHelpers.strengthFromPercentage(severity),
                strengthPercentage = severity,
                isAuspicious = false,
                activationPeriod = "${lord11.displayName} Mahadasha",
                cancellationFactors = cancellationReasons.ifEmpty { listOf("Remedial measures recommended") },
                nameKey = com.astro.storm.core.common.StringKeyPanchanga.YOGA_DARIDRA,
                effectsKey = com.astro.storm.core.common.StringKeyPanchanga.YOGA_EFFECT_DARIDRA
            )
        }

        return null
    }

    /**
     * Guru-Chandal Yoga - Jupiter conjunct Rahu
     */
    private fun evaluateGuruChandalYoga(chart: VedicChart): Yoga? {
        val jupiterPos = chart.planetPositions.find { it.planet == Planet.JUPITER } ?: return null
        val rahuPos = chart.planetPositions.find { it.planet == Planet.RAHU } ?: return null

        if (YogaHelpers.areConjunct(jupiterPos, rahuPos, 10.0)) {
            var severity = 65.0
            val cancellationReasons = mutableListOf<String>()

            // Jupiter in dignity reduces negative effects
            if (YogaHelpers.isExalted(jupiterPos) || YogaHelpers.isInOwnSign(jupiterPos)) {
                severity -= 20.0
                cancellationReasons.add("Jupiter in dignity - negative effects greatly reduced")
            }

            // In good houses, transformation is possible
            if (jupiterPos.house in listOf(1, 5, 9, 11)) {
                severity -= 10.0
                cancellationReasons.add("Good house placement allows positive transformation")
            }

            return Yoga(
                name = "Guru-Chandal Yoga",
                sanskritName = "Guru-Chandal Yoga",
                category = YogaCategory.NEGATIVE_YOGA,
                planets = listOf(Planet.JUPITER, Planet.RAHU),
                houses = listOf(jupiterPos.house),
                description = "Jupiter conjunct Rahu",
                effects = "Challenges with gurus/teachers, unconventional beliefs, potential for spiritual manipulation",
                strength = YogaHelpers.strengthFromPercentage(severity),
                strengthPercentage = severity,
                isAuspicious = false,
                activationPeriod = "Jupiter-Rahu or Rahu-Jupiter periods",
                cancellationFactors = cancellationReasons.ifEmpty { listOf("Proper spiritual guidance recommended") },
                nameKey = com.astro.storm.core.common.StringKeyPanchanga.YOGA_GURU_CHANDAL,
                effectsKey = com.astro.storm.core.common.StringKeyPanchanga.YOGA_EFFECT_GURU_CHANDAL
            )
        }

        return null
    }

    /**
     * Grahan (Eclipse) Yogas - Sun/Moon with Rahu/Ketu
     */
    private fun evaluateGrahanYogas(chart: VedicChart): List<Yoga> {
        val yogas = mutableListOf<Yoga>()
        val sunPos = chart.planetPositions.find { it.planet == Planet.SUN }
        val moonPos = chart.planetPositions.find { it.planet == Planet.MOON }
        val rahuPos = chart.planetPositions.find { it.planet == Planet.RAHU }
        val ketuPos = chart.planetPositions.find { it.planet == Planet.KETU }

        // Surya Grahan Yoga - Sun with Rahu
        if (sunPos != null && rahuPos != null && YogaHelpers.areConjunct(sunPos, rahuPos, 12.0)) {
            val severity = calculateGrahanSeverity(sunPos, rahuPos)
            yogas.add(Yoga(
                name = "Surya Grahan Yoga",
                sanskritName = "Surya Grahan Yoga",
                category = YogaCategory.NEGATIVE_YOGA,
                planets = listOf(Planet.SUN, Planet.RAHU),
                houses = listOf(sunPos.house),
                description = "Sun conjunct Rahu (solar eclipse pattern)",
                effects = "Challenges with authority/father, ego struggles, career obstacles requiring transformation",
                strength = YogaHelpers.strengthFromPercentage(severity),
                strengthPercentage = severity,
                isAuspicious = false,
                activationPeriod = "Sun-Rahu periods",
                cancellationFactors = listOf("Sun in Leo reduces effects", "Jupiter aspect protects"),
                nameKey = com.astro.storm.core.common.StringKeyPanchanga.YOGA_SURYA_GRAHAN,
                effectsKey = com.astro.storm.core.common.StringKeyPanchanga.YOGA_EFFECT_SURYA_GRAHAN
            ))
        }

        // Surya-Ketu Grahan Yoga
        if (sunPos != null && ketuPos != null && YogaHelpers.areConjunct(sunPos, ketuPos, 12.0)) {
            val severity = calculateGrahanSeverity(sunPos, ketuPos)
            yogas.add(Yoga(
                name = "Surya-Ketu Grahan Yoga",
                sanskritName = "Surya-Ketu Grahan Yoga",
                category = YogaCategory.NEGATIVE_YOGA,
                planets = listOf(Planet.SUN, Planet.KETU),
                houses = listOf(sunPos.house),
                description = "Sun conjunct Ketu",
                effects = "Spiritual challenges, detachment from worldly success, identity confusion",
                strength = YogaHelpers.strengthFromPercentage(severity),
                strengthPercentage = severity,
                isAuspicious = false,
                activationPeriod = "Sun-Ketu periods",
                cancellationFactors = listOf("Can lead to spiritual enlightenment if well-aspected"),
                nameKey = com.astro.storm.core.common.StringKeyPanchanga.YOGA_SURYA_KETU_GRAHAN,
                effectsKey = com.astro.storm.core.common.StringKeyPanchanga.YOGA_EFFECT_SURYA_KETU_GRAHAN
            ))
        }

        // Chandra Grahan Yoga - Moon with Rahu
        if (moonPos != null && rahuPos != null && YogaHelpers.areConjunct(moonPos, rahuPos, 12.0)) {
            val severity = calculateGrahanSeverity(moonPos, rahuPos)
            yogas.add(Yoga(
                name = "Chandra Grahan Yoga",
                sanskritName = "Chandra Grahan Yoga",
                category = YogaCategory.NEGATIVE_YOGA,
                planets = listOf(Planet.MOON, Planet.RAHU),
                houses = listOf(moonPos.house),
                description = "Moon conjunct Rahu (lunar eclipse pattern)",
                effects = "Mental restlessness, anxiety, mother-related challenges, emotional turbulence",
                strength = YogaHelpers.strengthFromPercentage(severity),
                strengthPercentage = severity,
                isAuspicious = false,
                activationPeriod = "Moon-Rahu periods",
                cancellationFactors = listOf("Jupiter aspect calms the mind", "Strong Moon in Cancer reduces effects"),
                nameKey = com.astro.storm.core.common.StringKeyPanchanga.YOGA_CHANDRA_GRAHAN,
                effectsKey = com.astro.storm.core.common.StringKeyPanchanga.YOGA_EFFECT_CHANDRA_GRAHAN
            ))
        }

        // Chandra-Ketu Yoga
        if (moonPos != null && ketuPos != null && YogaHelpers.areConjunct(moonPos, ketuPos, 12.0)) {
            val severity = calculateGrahanSeverity(moonPos, ketuPos)
            yogas.add(Yoga(
                name = "Chandra-Ketu Yoga",
                sanskritName = "Chandra-Ketu Yoga",
                category = YogaCategory.NEGATIVE_YOGA,
                planets = listOf(Planet.MOON, Planet.KETU),
                houses = listOf(moonPos.house),
                description = "Moon conjunct Ketu",
                effects = "Spiritual inclination, psychic sensitivity, potential emotional detachment",
                strength = YogaHelpers.strengthFromPercentage(severity),
                strengthPercentage = severity,
                isAuspicious = false,
                activationPeriod = "Moon-Ketu periods",
                cancellationFactors = listOf("Good for spiritual pursuits", "Jupiter aspect stabilizes"),
                nameKey = com.astro.storm.core.common.StringKeyPanchanga.YOGA_CHANDRA_KETU,
                effectsKey = com.astro.storm.core.common.StringKeyPanchanga.YOGA_EFFECT_CHANDRA_KETU
            ))
        }

        return yogas
    }

    private fun calculateGrahanSeverity(luminaryPos: PlanetPosition, nodePos: PlanetPosition): Double {
        val distance = kotlin.math.abs(luminaryPos.longitude - nodePos.longitude)
        val normalizedDistance = if (distance > 180) 360 - distance else distance

        // Closer conjunction = more severe
        return when {
            normalizedDistance <= 3 -> 75.0
            normalizedDistance <= 6 -> 65.0
            normalizedDistance <= 9 -> 55.0
            else -> 45.0
        }
    }

    /**
     * Angarak Yoga - Mars conjunct Rahu
     */
    private fun evaluateAngarakYoga(chart: VedicChart): Yoga? {
        val marsPos = chart.planetPositions.find { it.planet == Planet.MARS } ?: return null
        val rahuPos = chart.planetPositions.find { it.planet == Planet.RAHU } ?: return null

        if (YogaHelpers.areConjunct(marsPos, rahuPos, 10.0)) {
            var severity = 70.0
            val cancellationReasons = mutableListOf<String>()

            if (YogaHelpers.isExalted(marsPos) || YogaHelpers.isInOwnSign(marsPos)) {
                severity -= 15.0
                cancellationReasons.add("Mars in dignity - energy channeled constructively")
            }

            // In 3rd, 6th, 10th, 11th can be beneficial for competition
            if (marsPos.house in listOf(3, 6, 10, 11)) {
                severity -= 10.0
                cancellationReasons.add("Upachaya house placement - competitive success possible")
            }

            return Yoga(
                name = "Angarak Yoga",
                sanskritName = "Angarak Yoga",
                category = YogaCategory.NEGATIVE_YOGA,
                planets = listOf(Planet.MARS, Planet.RAHU),
                houses = listOf(marsPos.house),
                description = "Mars conjunct Rahu",
                effects = "Explosive energy, accident-prone, conflicts, aggressive tendencies",
                strength = YogaHelpers.strengthFromPercentage(severity),
                strengthPercentage = severity,
                isAuspicious = false,
                activationPeriod = "Mars-Rahu or Rahu-Mars periods",
                cancellationFactors = cancellationReasons.ifEmpty { listOf("Channel energy through physical activity") },
                nameKey = com.astro.storm.core.common.StringKeyPanchanga.YOGA_ANGARAK,
                effectsKey = com.astro.storm.core.common.StringKeyPanchanga.YOGA_EFFECT_ANGARAK
            )
        }

        return null
    }

    /**
     * Shrapit Yoga - Saturn conjunct Rahu
     */
    private fun evaluateShrapitYoga(chart: VedicChart): Yoga? {
        val saturnPos = chart.planetPositions.find { it.planet == Planet.SATURN } ?: return null
        val rahuPos = chart.planetPositions.find { it.planet == Planet.RAHU } ?: return null

        if (YogaHelpers.areConjunct(saturnPos, rahuPos, 10.0)) {
            var severity = 65.0
            val cancellationReasons = mutableListOf<String>()

            if (YogaHelpers.isExalted(saturnPos) || YogaHelpers.isInOwnSign(saturnPos)) {
                severity -= 20.0
                cancellationReasons.add("Saturn in dignity - karmic debts can be cleared")
            }

            val jupiterPos = chart.planetPositions.find { it.planet == Planet.JUPITER }
            if (jupiterPos != null && YogaHelpers.isAspecting(jupiterPos, saturnPos)) {
                severity -= 15.0
                cancellationReasons.add("Jupiter aspect - divine grace available")
            }

            return Yoga(
                name = "Shrapit Yoga",
                sanskritName = "Shrapit Yoga",
                category = YogaCategory.NEGATIVE_YOGA,
                planets = listOf(Planet.SATURN, Planet.RAHU),
                houses = listOf(saturnPos.house),
                description = "Saturn conjunct Rahu (cursed combination)",
                effects = "Karmic debts, delays, obstacles, past-life issues surfacing",
                strength = YogaHelpers.strengthFromPercentage(severity),
                strengthPercentage = severity,
                isAuspicious = false,
                activationPeriod = "Saturn-Rahu or Rahu-Saturn periods",
                cancellationFactors = cancellationReasons.ifEmpty { listOf("Remedial measures and patience required") },
                nameKey = com.astro.storm.core.common.StringKeyPanchanga.YOGA_SHRAPIT,
                effectsKey = com.astro.storm.core.common.StringKeyPanchanga.YOGA_EFFECT_SHRAPIT
            )
        }

        return null
    }

    /**
     * Kala Sarpa Yoga - All planets between Rahu-Ketu axis.
     * Refined logic to distinguish between full and partial, and identify specific types.
     */
    private fun evaluateKalaSarpaYoga(chart: VedicChart): Yoga? {
        val rahuPos = chart.planetPositions.find { it.planet == Planet.RAHU } ?: return null
        val ketuPos = chart.planetPositions.find { it.planet == Planet.KETU } ?: return null

        val mainPlanets = chart.planetPositions.filter {
            it.planet in listOf(Planet.SUN, Planet.MOON, Planet.MARS, Planet.MERCURY,
                Planet.JUPITER, Planet.VENUS, Planet.SATURN)
        }

        val rahuLong = rahuPos.longitude
        val ketuLong = ketuPos.longitude

        // Check if all planets are on one side of the Rahu-Ketu axis
        // We check both directions because the axis divides the 360 into two 180 arcs
        val side1Count = mainPlanets.count { isInArc(it.longitude, rahuLong, ketuLong) }
        val side2Count = mainPlanets.count { isInArc(it.longitude, ketuLong, rahuLong) }

        val isFullKalaSarpa = side1Count == mainPlanets.size || side2Count == mainPlanets.size
        val isPartialKalaSarpa = (side1Count == mainPlanets.size - 1 || side2Count == mainPlanets.size - 1) && mainPlanets.size > 1

        if (isFullKalaSarpa || isPartialKalaSarpa) {
            // Determine type based on Rahu position
            val yogaNamePrefix = when (rahuPos.house) {
                1 -> "Ananta"
                2 -> "Kulik"
                3 -> "Vasuki"
                4 -> "Shankhpal"
                5 -> "Padma"
                6 -> "Maha Padma"
                7 -> "Takshak"
                8 -> "Karkotak"
                9 -> "Shankhachur"
                10 -> "Ghatak"
                11 -> "Vishdhar"
                12 -> "Sheshnag"
                else -> ""
            }

            val prefix = if (isPartialKalaSarpa) "Partial " else ""
            val yogaType = "$prefix$yogaNamePrefix Kala Sarpa"

            val severity = if (isFullKalaSarpa) 75.0 else 45.0

            val cancellationReasons = mutableListOf<String>()
            cancellationReasons.add("Effects typically reduce after age 33-36")

            // Jupiter aspect provides major relief
            val jupiterPos = chart.planetPositions.find { it.planet == Planet.JUPITER }
            if (jupiterPos != null && (YogaHelpers.isAspecting(jupiterPos, rahuPos) || YogaHelpers.isAspecting(jupiterPos, ketuPos))) {
                cancellationReasons.add("Jupiter aspect on Rahu/Ketu axis provides divine protection")
            }

            // Check if any planet is outside the axis (for partial)
            if (isPartialKalaSarpa) {
                val outsidePlanet = mainPlanets.find {
                    if (side1Count == mainPlanets.size - 1) !isInArc(it.longitude, rahuLong, ketuLong)
                    else !isInArc(it.longitude, ketuLong, rahuLong)
                }
                outsidePlanet?.let {
                    cancellationReasons.add("Broken by ${it.planet.displayName} being outside the Rahu-Ketu axis")
                }
            }

            return Yoga(
                name = "$yogaType Yoga",
                sanskritName = "Kala Sarpa Yoga",
                category = YogaCategory.NEGATIVE_YOGA,
                planets = listOf(Planet.RAHU, Planet.KETU),
                houses = listOf(rahuPos.house, ketuPos.house),
                description = if (isFullKalaSarpa) "All 7 main planets are hemmed between the Rahu-Ketu axis."
                             else "Most planets are hemmed between the Rahu-Ketu axis, with one planet breaking the formation.",
                effects = "Intense karmic experiences, sudden life transformations, initial struggle followed by significant rise, destiny-oriented path.",
                strength = YogaHelpers.strengthFromPercentage(severity),
                strengthPercentage = severity,
                isAuspicious = false,
                activationPeriod = "Throughout life, especially Rahu or Ketu periods",
                cancellationFactors = cancellationReasons,
                nameKey = com.astro.storm.core.common.StringKeyPanchanga.YOGA_KALA_SARPA,
                effectsKey = com.astro.storm.core.common.StringKeyPanchanga.YOGA_EFFECT_KALA_SARPA
            )
        }

        return null
    }

    private fun isInArc(longitude: Double, start: Double, end: Double): Boolean {
        return if (start <= end) {
            longitude in start..end
        } else {
            longitude >= start || longitude <= end
        }
    }

    /**
     * Papakartari Yoga - Hemmed between malefics
     */
    private fun evaluatePapakartariYogas(chart: VedicChart): List<Yoga> {
        val yogas = mutableListOf<Yoga>()
        val ascendantSign = ZodiacSign.fromLongitude(chart.ascendant)
        val ascendantHouse = 1

        // Check Papakartari on Ascendant
        val ascendantPos = chart.planetPositions.firstOrNull()
        if (ascendantPos != null) {
            // Create a pseudo-position for ascendant check
            val malefics = listOf(Planet.SATURN, Planet.MARS, Planet.RAHU, Planet.KETU, Planet.SUN)
            val prevHouse = 12
            val nextHouse = 2

            val hasMaleficBefore = chart.planetPositions.any {
                it.planet in malefics && it.house == prevHouse
            }
            val hasMaleficAfter = chart.planetPositions.any {
                it.planet in malefics && it.house == nextHouse
            }

            if (hasMaleficBefore && hasMaleficAfter) {
                yogas.add(Yoga(
                    name = "Papakartari Yoga (Lagna)",
                    sanskritName = "Papakartari Yoga",
                    category = YogaCategory.NEGATIVE_YOGA,
                    planets = emptyList(),
                    houses = listOf(1, 2, 12),
                    description = "Ascendant hemmed between malefics",
                    effects = "Physical struggles, obstacles in self-expression, challenges in early life",
                    strength = YogaStrength.MODERATE,
                    strengthPercentage = 55.0,
                    isAuspicious = false,
                    activationPeriod = "Throughout life",
                    cancellationFactors = listOf("Jupiter aspect on lagna provides relief"),
                    nameKey = com.astro.storm.core.common.StringKeyPanchanga.YOGA_PAPAKARTARI,
                    effectsKey = com.astro.storm.core.common.StringKeyPanchanga.YOGA_EFFECT_PAPAKARTARI
                ))
            }
        }

        // Check Papakartari on Moon
        val moonPos = chart.planetPositions.find { it.planet == Planet.MOON }
        if (moonPos != null && YogaHelpers.isPapakartari(moonPos, chart)) {
            yogas.add(Yoga(
                name = "Papakartari Yoga (Chandra)",
                sanskritName = "Papakartari Yoga",
                category = YogaCategory.NEGATIVE_YOGA,
                planets = listOf(Planet.MOON),
                houses = listOf(moonPos.house),
                description = "Moon hemmed between malefics",
                effects = "Mental stress, emotional challenges, anxiety, mother-related difficulties",
                strength = YogaStrength.MODERATE,
                strengthPercentage = 60.0,
                isAuspicious = false,
                activationPeriod = "Moon periods especially",
                cancellationFactors = listOf("Jupiter aspect calms the mind"),
                nameKey = com.astro.storm.core.common.StringKeyPanchanga.YOGA_PAPAKARTARI,
                effectsKey = com.astro.storm.core.common.StringKeyPanchanga.YOGA_EFFECT_PAPAKARTARI
            ))
        }

        return yogas
    }
}
