package com.astro.vajra.ephemeris.yoga

import com.astro.vajra.core.model.Planet
import com.astro.vajra.core.model.PlanetPosition
import com.astro.vajra.core.model.VedicChart
import com.astro.vajra.core.model.ZodiacSign
import com.astro.vajra.ephemeris.VipareetaRajaYogaCalculator

/**
 * Raja Yoga Evaluator - Power and Authority Combinations
 *
 * Raja Yogas are formed by the association of Kendra lords (1,4,7,10) and Trikona lords (1,5,9).
 * These yogas indicate power, authority, and leadership potential.
 *
 * This evaluator integrates with [VipareetaRajaYogaCalculator] for precise reverse raja yoga analysis.
 *
 * Types evaluated:
 * 1. Kendra-Trikona Raja Yoga - Conjunction/aspect/exchange of Kendra and Trikona lords
 * 2. Parivartana Raja Yoga - Exchange between benefic house lords
 * 3. Viparita Raja Yoga - Lords of 6, 8, 12 in each other's houses (via specialized calculator)
 * 4. Neecha Bhanga Raja Yoga - Cancellation of debilitation creating power
 * 5. Maha Raja Yoga - Jupiter and Venus in Kendra from Moon
 *
 * Based on:
 * - Brihat Parasara Hora Shastra (BPHS), Chapter 41
 * - Phaladeepika, Chapter 6
 *
 * @author AstroVajra
 */
class RajaYogaEvaluator : YogaEvaluator {

    override val category: YogaCategory = YogaCategory.RAJA_YOGA

    override fun evaluate(chart: VedicChart): List<Yoga> {
        val yogas = mutableListOf<Yoga>()
        val ascendantSign = ZodiacSign.fromLongitude(chart.ascendant)

        // Get house lords
        val houseLords = YogaHelpers.getHouseLords(ascendantSign)
        if (houseLords.isEmpty()) {
            return yogas
        }

        val kendraLords = listOf(houseLords[1], houseLords[4], houseLords[7], houseLords[10]).filterNotNull()
        val trikonaLords = listOf(houseLords[1], houseLords[5], houseLords[9]).filterNotNull()

        // 1. Kendra-Trikona Raja Yoga
        yogas.addAll(evaluateKendraTrikonaYogas(chart, kendraLords, trikonaLords))

        // 2. Viparita Raja Yoga (via specialized calculator)
        evaluateDusthanaYogas(chart, yogas)

        // 3. Neecha Bhanga Raja Yoga
        yogas.addAll(evaluateNeechaBhangaYogas(chart))

        // 4. Maha Raja Yoga
        yogas.addAll(evaluateMahaRajaYoga(chart))

        // 5. Maha Bhagya Yoga
        evaluateMahaBhagyaYoga(chart)?.let { yogas.add(it) }

        // 6. Pushkala Yoga
        evaluatePushkalaYoga(chart, houseLords)?.let { yogas.add(it) }

        // 7. Akhanda Samrajya Yoga
        evaluateAkhandaSamrajyaYoga(chart, houseLords)?.let { yogas.add(it) }

        return yogas
    }

    private fun evaluateDusthanaYogas(chart: VedicChart, yogas: MutableList<Yoga>) {
        val analysis = VipareetaRajaYogaCalculator.analyzeVipareetaRajaYogas(chart) ?: return

        listOf(analysis.harshaYoga, analysis.saralaYoga, analysis.vimalaYoga).forEach { detailedYoga ->
            if (detailedYoga.isFormed) {
                val basePct = when (detailedYoga.strength) {
                    VipareetaRajaYogaCalculator.YogaStrength.EXCEPTIONAL -> 90.0
                    VipareetaRajaYogaCalculator.YogaStrength.STRONG -> 75.0
                    VipareetaRajaYogaCalculator.YogaStrength.MODERATE -> 60.0
                    VipareetaRajaYogaCalculator.YogaStrength.MILD -> 45.0
                    VipareetaRajaYogaCalculator.YogaStrength.WEAK -> 30.0
                    else -> 0.0
                }

                yogas.add(Yoga(
                    name = detailedYoga.yogaType.displayName,
                    sanskritName = detailedYoga.yogaType.sanskritName,
                    category = YogaCategory.RAJA_YOGA,
                    planets = listOf(detailedYoga.dusthanaLord),
                    houses = listOf(detailedYoga.placedInHouse),
                    description = detailedYoga.yogaType.description,
                    effects = detailedYoga.interpretation,
                    strength = YogaHelpers.strengthFromPercentage(basePct),
                    strengthPercentage = basePct,
                    isAuspicious = true,
                    activationPeriod = detailedYoga.dusthanaLord.displayName + " periods",
                    cancellationFactors = detailedYoga.weaknessFactors,
                    detailedResult = detailedYoga,
                    
                    // Add high-precision keys
                    nameKey = detailedYoga.yogaType.nameKey,
                    descriptionKey = detailedYoga.yogaType.descKey
                ))
            }
        }
    }

    /**
     * Akhanda Samrajya Yoga - Powerful leadership and wealth
     */
    private fun evaluateAkhandaSamrajyaYoga(chart: VedicChart, houseLords: Map<Int, Planet>): Yoga? {
        val ascSign = ZodiacSign.fromLongitude(chart.ascendant)
        
        // 1. Must be a fixed sign Lagna (Taurus, Leo, Scorpio, Aquarius)
        if (ascSign !in listOf(ZodiacSign.TAURUS, ZodiacSign.LEO, ZodiacSign.SCORPIO, ZodiacSign.AQUARIUS)) return null
        
        // 2. Jupiter must rule 2nd, 5th, or 11th
        val jupiterRulesWealth = when (ascSign) {
            ZodiacSign.SCORPIO -> true // Rules 2nd (Sagittarius) and 5th (Pisces)
            ZodiacSign.AQUARIUS -> true // Rules 2nd (Pisces) and 11th (Sagittarius)
            ZodiacSign.LEO -> true // Rules 5th (Sagittarius)
            ZodiacSign.TAURUS -> true // Rules 11th (Pisces)
            else -> false
        }
        
        if (!jupiterRulesWealth) return null
        
        // 3. One of the lords of 2, 9, 11 in Kendra from Moon
        val moonPos = chart.planetPositions.find { it.planet == Planet.MOON } ?: return null
        val lord2 = houseLords[2]
        val lord9 = houseLords[9]
        val lord11 = houseLords[11]
        
        val wealthLords = listOfNotNull(lord2, lord9, lord11)
        val wealthPositions = chart.planetPositions.filter { it.planet in wealthLords }
        
        val formed = wealthPositions.any { pos ->
            YogaHelpers.isInKendraFrom(pos, moonPos)
        }
        
        if (formed) {
            val strength = 80.0
            return Yoga(
                name = "Akhanda Samrajya Yoga",
                sanskritName = "Akhanda Samrajya Yoga",
                category = YogaCategory.RAJA_YOGA,
                planets = listOf(Planet.JUPITER, Planet.MOON),
                houses = wealthPositions.map { it.house },
                description = "Fixed sign Lagna with wealth house lords in Kendra from Moon",
                effects = "Undisputed leadership, wide-ranging influence, vast wealth and authority",
                strength = YogaStrength.EXTREMELY_STRONG,
                strengthPercentage = strength,
                isAuspicious = true,
                activationPeriod = "Jupiter or Moon periods",
                cancellationFactors = emptyList()
            )
        }
        
        return null
    }

    /**
     * Evaluate Maha Bhagya Yoga
     * Male: Day birth, Sun/Moon/Asc in odd signs
     * Female: Night birth, Sun/Moon/Asc in even signs
     */
    private fun evaluateMahaBhagyaYoga(chart: VedicChart): Yoga? {
        val isDayBirth = YogaHelpers.isDayBirth(chart)
        
        val sunPos = chart.planetPositions.find { it.planet == Planet.SUN } ?: return null
        val moonPos = chart.planetPositions.find { it.planet == Planet.MOON } ?: return null
        val ascSign = ZodiacSign.fromLongitude(chart.ascendant)
        
        val sunOdd = YogaHelpers.isOddSign(sunPos.sign.number)
        val moonOdd = YogaHelpers.isOddSign(moonPos.sign.number)
        val ascOdd = YogaHelpers.isOddSign(ascSign.number)
        
        val isMale = chart.birthData.gender == com.astro.vajra.core.model.Gender.MALE
        val isFemale = chart.birthData.gender == com.astro.vajra.core.model.Gender.FEMALE
        
        val formed = when {
            isMale && isDayBirth && sunOdd && moonOdd && ascOdd -> true
            isFemale && !isDayBirth && !sunOdd && !moonOdd && !ascOdd -> true
            else -> false
        }
        
        if (formed) {
            val strength = 85.0 // Maha Bhagya is very powerful
            return Yoga(
                name = "Maha Bhagya Yoga",
                sanskritName = "Maha Bhagya Yoga",
                category = YogaCategory.RAJA_YOGA,
                planets = listOf(Planet.SUN, Planet.MOON),
                houses = listOf(sunPos.house, moonPos.house, 1),
                description = if (isMale) "Male born during day with Sun, Moon, and Ascendant in odd signs" 
                             else "Female born during night with Sun, Moon, and Ascendant in even signs",
                effects = "Exceptional fortune, leadership, magnetic personality, success from birth",
                strength = YogaStrength.EXTREMELY_STRONG,
                strengthPercentage = strength,
                isAuspicious = true,
                activationPeriod = "Sun or Moon periods",
                cancellationFactors = emptyList()
            )
        }
        
        return null
    }

    /**
     * Pushkala Yoga - Lagna lord with Moon, Moon in Kendra, Moon's dispositor in Kendra strong
     */
    private fun evaluatePushkalaYoga(chart: VedicChart, houseLords: Map<Int, Planet>): Yoga? {
        val lagnaLord = houseLords[1] ?: return null
        val lagnaLordPos = chart.planetPositions.find { it.planet == lagnaLord } ?: return null
        val moonPos = chart.planetPositions.find { it.planet == Planet.MOON } ?: return null
        
        // Moon in Kendra from Lagna
        if (moonPos.house !in listOf(1, 4, 7, 10)) return null
        
        // Lagna lord with Moon
        if (!YogaHelpers.areConjunct(lagnaLordPos, moonPos, 12.0)) return null
        
        // Moon's dispositor
        val moonDispositor = moonPos.sign.ruler
        val dispositorPos = chart.planetPositions.find { it.planet == moonDispositor } ?: return null
        
        // Dispositor in Kendra from Lagna
        if (dispositorPos.house !in listOf(1, 4, 7, 10)) return null
        
        // Dispositor strong
        if (YogaHelpers.isExalted(dispositorPos) || YogaHelpers.isInOwnSign(dispositorPos)) {
            val strength = YogaHelpers.calculateYogaStrength(chart, listOf(moonPos, lagnaLordPos, dispositorPos))
            return Yoga(
                name = "Pushkala Yoga",
                sanskritName = "Pushkala Yoga",
                category = YogaCategory.RAJA_YOGA,
                planets = listOf(Planet.MOON, lagnaLord, moonDispositor),
                houses = listOf(moonPos.house, dispositorPos.house),
                description = "Lagna lord conjunct Moon in Kendra, Moon's dispositor in Kendra and strong",
                effects = "Wealthy, honored by rulers, famous, eloquent, good conduct",
                strength = YogaHelpers.strengthFromPercentage(strength),
                strengthPercentage = strength,
                isAuspicious = true,
                activationPeriod = "Moon or Lagna lord periods",
                cancellationFactors = emptyList()
            )
        }
        
        return null
    }

    /**
     * Evaluate Kendra-Trikona Raja Yogas
     */
    private fun evaluateKendraTrikonaYogas(
        chart: VedicChart,
        kendraLords: List<Planet>,
        trikonaLords: List<Planet>
    ): List<Yoga> {
        val yogas = mutableListOf<Yoga>()

        kendraLords.forEach { kendraLord ->
            trikonaLords.forEach { trikonaLord ->
                if (kendraLord != trikonaLord) {
                    val kendraPos = chart.planetPositions.find { it.planet == kendraLord }
                    val trikonaPos = chart.planetPositions.find { it.planet == trikonaLord }

                    if (kendraPos != null && trikonaPos != null) {
                        // Check for conjunction
                        if (YogaHelpers.areConjunct(kendraPos, trikonaPos)) {
                            val strength = YogaHelpers.calculateDynamicYogaStrength(chart, listOf(kendraPos, trikonaPos))
                            yogas.add(createKendraTrikonaRajaYoga(
                                kendraLord, trikonaLord, "conjunction", strength, chart
                            ))
                        }

                        // Check for mutual aspect
                        if (YogaHelpers.areMutuallyAspecting(kendraPos, trikonaPos)) {
                            val strength = YogaHelpers.calculateDynamicYogaStrength(chart, listOf(kendraPos, trikonaPos)) * 0.8
                            yogas.add(createKendraTrikonaRajaYoga(
                                kendraLord, trikonaLord, "aspect", strength, chart
                            ))
                        }

                        // Check for exchange (Parivartana)
                        if (YogaHelpers.areInExchange(kendraPos, trikonaPos)) {
                            val strength = YogaHelpers.calculateDynamicYogaStrength(chart, listOf(kendraPos, trikonaPos)) * 1.2
                            yogas.add(createParivartanaRajaYoga(kendraLord, trikonaLord, strength, chart))
                        }
                    }
                }
            }
        }

        return yogas
    }

    /**
     * Evaluate Neecha Bhanga Raja Yogas
     */
    private fun evaluateNeechaBhangaYogas(chart: VedicChart): List<Yoga> {
        val yogas = mutableListOf<Yoga>()

        chart.planetPositions.forEach { pos ->
            if (YogaHelpers.isDebilitated(pos)) {
                if (YogaHelpers.hasNeechaBhanga(pos, chart)) {
                    val strength = YogaHelpers.calculateYogaStrength(chart, listOf(pos))
                    yogas.add(createNeechaBhangaRajaYoga(pos.planet, strength, chart))
                }
            }
        }

        return yogas
    }

    /**
     * Evaluate Maha Raja Yoga (Jupiter and Venus in Kendra from Moon)
     */
    private fun evaluateMahaRajaYoga(chart: VedicChart): List<Yoga> {
        val yogas = mutableListOf<Yoga>()

        val moonPos = chart.planetPositions.find { it.planet == Planet.MOON }
        val jupiterPos = chart.planetPositions.find { it.planet == Planet.JUPITER }
        val venusPos = chart.planetPositions.find { it.planet == Planet.VENUS }

        if (moonPos != null && jupiterPos != null && venusPos != null) {
            if (YogaHelpers.isInKendraFrom(jupiterPos, moonPos) &&
                YogaHelpers.isInKendraFrom(venusPos, moonPos)) {
                val strength = YogaHelpers.calculateYogaStrength(chart, listOf(jupiterPos, venusPos, moonPos))
                yogas.add(
                    Yoga(
                        name = "Maha Raja Yoga",
                        sanskritName = "Maha Raja Yoga",
                        category = YogaCategory.RAJA_YOGA,
                        planets = listOf(Planet.JUPITER, Planet.VENUS, Planet.MOON),
                        houses = listOf(jupiterPos.house, venusPos.house),
                        description = "Jupiter and Venus in Kendra from Moon",
                        effects = "Exceptional fortune, royal status, widespread fame, great wealth and power",
                        strength = YogaHelpers.strengthFromPercentage(strength),
                        strengthPercentage = strength,
                        isAuspicious = true,
                        activationPeriod = "Jupiter and Venus Dashas",
                        cancellationFactors = emptyList()
                    )
                )
            }
        }

        return yogas
    }

    // ==================== YOGA CREATION HELPERS ====================

    private fun createKendraTrikonaRajaYoga(
        kendraLord: Planet,
        trikonaLord: Planet,
        type: String,
        baseStrength: Double,
        chart: VedicChart
    ): Yoga {
        val kendraPos = chart.planetPositions.find { it.planet == kendraLord }
        val trikonaPos = chart.planetPositions.find { it.planet == trikonaLord }
        val positions = listOfNotNull(kendraPos, trikonaPos)

        val (cancellationFactor, cancellationReasons) = YogaHelpers.calculateCancellationFactor(positions, chart)
        val adjustedStrength = (baseStrength * cancellationFactor).coerceIn(10.0, 100.0)

        return Yoga(
            name = "Kendra-Trikona Raja Yoga",
            sanskritName = "Kendra-Trikona Raja Yoga",
            category = YogaCategory.RAJA_YOGA,
            planets = listOf(kendraLord, trikonaLord),
            houses = listOfNotNull(kendraPos?.house, trikonaPos?.house),
            description = "${kendraLord.displayName} (Kendra lord) and ${trikonaLord.displayName} (Trikona lord) in $type",
            effects = "Rise to power and authority, leadership position, recognition from government",
            strength = YogaHelpers.strengthFromPercentage(adjustedStrength),
            strengthPercentage = adjustedStrength,
            isAuspicious = true,
            activationPeriod = "${kendraLord.displayName}-${trikonaLord.displayName} Dasha/Antardasha",
            cancellationFactors = cancellationReasons.ifEmpty { listOf("None - yoga is unafflicted") }
        )
    }

    private fun createParivartanaRajaYoga(
        planet1: Planet,
        planet2: Planet,
        baseStrength: Double,
        chart: VedicChart
    ): Yoga {
        val pos1 = chart.planetPositions.find { it.planet == planet1 }
        val pos2 = chart.planetPositions.find { it.planet == planet2 }
        val positions = listOfNotNull(pos1, pos2)

        val (cancellationFactor, cancellationReasons) = YogaHelpers.calculateCancellationFactor(positions, chart)
        val adjustedStrength = (baseStrength * cancellationFactor).coerceIn(10.0, 100.0)

        return Yoga(
            name = "Parivartana Raja Yoga",
            sanskritName = "Parivartana Raja Yoga",
            category = YogaCategory.RAJA_YOGA,
            planets = listOf(planet1, planet2),
            houses = listOfNotNull(pos1?.house, pos2?.house),
            description = "Exchange between ${planet1.displayName} and ${planet2.displayName}",
            effects = "Strong Raja Yoga through mutual exchange, stable rise to power, lasting authority",
            strength = YogaHelpers.strengthFromPercentage(adjustedStrength),
            strengthPercentage = adjustedStrength,
            isAuspicious = true,
            activationPeriod = "${planet1.displayName} and ${planet2.displayName} Dashas",
            cancellationFactors = cancellationReasons.ifEmpty { listOf("None - yoga is unafflicted") }
        )
    }

    private fun createNeechaBhangaRajaYoga(
        planet: Planet,
        baseStrength: Double,
        chart: VedicChart
    ): Yoga {
        val pos = chart.planetPositions.find { it.planet == planet }
        val cancellationReasons = mutableListOf<String>()
        var adjustedStrength = baseStrength

        if (pos != null) {
            // Check combustion
            val combustionFactor = YogaHelpers.getCombustionFactor(pos, chart)
            if (combustionFactor < 0.9) {
                adjustedStrength *= combustionFactor
                if (combustionFactor < 0.6) {
                    cancellationReasons.add("${planet.displayName} is combust - Neecha Bhanga weakened")
                }
            }

            // Check malefic aspects
            val afflictionFactor = YogaHelpers.getMaleficAfflictionFactor(pos, chart)
            if (afflictionFactor < 0.85) {
                adjustedStrength *= afflictionFactor
                cancellationReasons.add("Malefic aspects reduce yoga effectiveness")
            }

            // Check Papakartari
            if (YogaHelpers.isPapakartari(pos, chart)) {
                adjustedStrength *= 0.8
                cancellationReasons.add("Planet hemmed between malefics")
            }

            // Benefic aspects boost
            val beneficBoost = YogaHelpers.getBeneficAspectBoost(pos, chart)
            if (beneficBoost > 1.0) {
                adjustedStrength *= beneficBoost
            }

            // Identify cancellation type
            if (pos.house in listOf(1, 4, 7, 10)) {
                cancellationReasons.add(0, "Neecha Bhanga via Kendra placement")
            } else {
                val debilitatedSignLord = pos.sign.ruler
                val lordPos = chart.planetPositions.find { it.planet == debilitatedSignLord }
                if (lordPos != null && lordPos.house in listOf(1, 4, 7, 10)) {
                    cancellationReasons.add(0, "Neecha Bhanga via sign lord in Kendra")
                }
            }
        }

        return Yoga(
            name = "Neecha Bhanga Raja Yoga",
            sanskritName = "Neecha Bhanga Raja Yoga",
            category = YogaCategory.RAJA_YOGA,
            planets = listOf(planet),
            houses = listOfNotNull(pos?.house),
            description = "${planet.displayName} debilitated but with cancellation",
            effects = "Rise from humble beginnings, success after initial struggles, respected leader",
            strength = YogaHelpers.strengthFromPercentage(adjustedStrength.coerceIn(10.0, 100.0)),
            strengthPercentage = adjustedStrength.coerceIn(10.0, 100.0),
            isAuspicious = true,
            activationPeriod = "${planet.displayName} Dasha",
            cancellationFactors = cancellationReasons.ifEmpty { listOf("Clean Neecha Bhanga - yoga operates fully") }
        )
    }
}
