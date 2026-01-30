package com.astro.storm.ephemeris.yoga.evaluators

import com.astro.storm.core.model.Planet
import com.astro.storm.core.model.PlanetPosition
import com.astro.storm.core.model.VedicChart
import com.astro.storm.core.model.ZodiacSign
import com.astro.storm.ephemeris.yoga.Yoga
import com.astro.storm.ephemeris.yoga.YogaCategory
import com.astro.storm.ephemeris.yoga.YogaEvaluator
import com.astro.storm.ephemeris.yoga.YogaHelpers
import com.astro.storm.ephemeris.yoga.YogaStrength
import kotlin.math.abs

/**
 * Arishta (Inauspicious) Yoga Evaluator
 *
 * Comprehensive evaluator for challenging planetary combinations.
 * Understanding these yogas helps in identifying potential challenges
 * and recommending appropriate remedial measures.
 *
 * Categories evaluated:
 * 1. Balarishta - Childhood afflictions
 * 2. Alpaayu - Short life indicators
 * 3. Madhyaayu - Medium life indicators
 * 4. Marana Karaka - Death-inflicting combinations
 * 5. Dosha Yogas - Various afflictions (Pitru, Matru, etc.)
 * 6. Gandanta - Junction point birth
 * 7. Combustion-related yogas
 * 8. Affliction patterns
 *
 * Based on:
 * - Brihat Parashara Hora Shastra (BPHS), Chapter 44
 * - Phaladeepika, Chapter 14
 * - Jataka Parijata
 *
 * @author AstroStorm
 */
class ArishtaYogaEvaluator : YogaEvaluator {

    override val category: YogaCategory = YogaCategory.NEGATIVE_YOGA

    companion object {
        // Natural malefics
        private val MALEFICS = listOf(Planet.SATURN, Planet.MARS, Planet.RAHU, Planet.KETU, Planet.SUN)
        // Natural benefics
        private val BENEFICS = listOf(Planet.JUPITER, Planet.VENUS, Planet.MERCURY, Planet.MOON)
        // Maraka houses
        private val MARAKA_HOUSES = listOf(2, 7)
        // Dusthana houses
        private val DUSTHANA_HOUSES = listOf(6, 8, 12)
        // Gandanta degrees
        private val GANDANTA_RANGES = listOf(
            Pair(0.0, 3.333), // Aries-Pisces junction
            Pair(116.667, 120.0), // Cancer-Leo junction at Leo start
            Pair(236.667, 240.0), // Scorpio-Sagittarius junction
            Pair(356.667, 360.0) // Pisces-Aries junction at end
        )
    }

    override fun evaluate(chart: VedicChart): List<Yoga> {
        val yogas = mutableListOf<Yoga>()
        val ascendantSign = ZodiacSign.fromLongitude(chart.ascendant)
        val houseLords = YogaHelpers.getHouseLords(ascendantSign)

        // 1. Balarishta Yogas (Childhood afflictions)
        yogas.addAll(evaluateBalarishta(chart, houseLords))

        // 2. Alpaayu Yogas (Short life)
        yogas.addAll(evaluateAlpaayu(chart, houseLords))

        // 3. Maraka Yogas (Death-inflicting)
        yogas.addAll(evaluateMaraka(chart, houseLords))

        // 4. Pitru Dosha (Ancestral affliction)
        evaluatePitruDosha(chart, houseLords)?.let { yogas.add(it) }

        // 5. Matru Dosha (Mother affliction)
        evaluateMatruDosha(chart, houseLords)?.let { yogas.add(it) }

        // 6. Gandanta Yoga (Junction point birth)
        evaluateGandantaYoga(chart)?.let { yogas.add(it) }

        // 7. Combustion Yogas
        yogas.addAll(evaluateCombustionYogas(chart))

        // 8. Duryoga (General misfortune)
        evaluateDuryoga(chart, houseLords)?.let { yogas.add(it) }

        // 9. Asubhakartari (Malefic scissors)
        yogas.addAll(evaluateAsubhakartari(chart))

        // 10. Lagna Dosha (Ascendant affliction)
        evaluateLagnaDosha(chart, houseLords)?.let { yogas.add(it) }

        // 11. Rogadhipati Yoga (Disease lord)
        evaluateRogadhipati(chart, houseLords)?.let { yogas.add(it) }

        // 12. Bandhana Yoga (Imprisonment)
        evaluateBandhanaYoga(chart, houseLords)?.let { yogas.add(it) }

        return yogas
    }

    /**
     * Evaluate Balarishta (Childhood affliction) Yogas
     */
    private fun evaluateBalarishta(
        chart: VedicChart,
        houseLords: Map<Int, Planet>
    ): List<Yoga> {
        val yogas = mutableListOf<Yoga>()
        val moonPos = chart.planetPositions.find { it.planet == Planet.MOON } ?: return yogas
        val lagnaLord = houseLords[1]
        val lagnaLordPos = chart.planetPositions.find { it.planet == lagnaLord }

        // Moon afflictions
        val moonAfflicted = MALEFICS.any { malefic ->
            val maleficPos = chart.planetPositions.find { it.planet == malefic }
            maleficPos != null && (
                    YogaHelpers.areConjunct(moonPos, maleficPos, 8.0) ||
                            YogaHelpers.isAspecting(maleficPos, moonPos)
                    )
        }

        // Weak Moon in dusthana with malefic aspect
        val moonWeak = YogaHelpers.getMoonPhaseStrength(moonPos, chart) < 0.3
        val moonInDusthana = moonPos.house in DUSTHANA_HOUSES

        if (moonWeak && moonInDusthana && moonAfflicted) {
            yogas.add(Yoga(
                name = "Balarishta Yoga (Moon Affliction)",
                sanskritName = "बालारिष्ट योग",
                category = YogaCategory.NEGATIVE_YOGA,
                planets = listOf(Planet.MOON),
                houses = listOf(moonPos.house),
                description = "Weak Moon afflicted by malefics in dusthana house",
                effects = "Childhood health challenges requiring careful nurturing, improves after 8 years with proper remedies",
                strength = YogaStrength.MODERATE,
                strengthPercentage = 55.0,
                isAuspicious = false,
                activationPeriod = "Moon dasha and childhood years",
                cancellationFactors = listOf(
                    "Jupiter's aspect on Moon cancels",
                    "Strong benefic influence mitigates",
                    "Effects reduce after age 8"
                )
            ))
        }

        // Lagna lord weak and afflicted
        if (lagnaLordPos != null) {
            val lagnaLordInDusthana = lagnaLordPos.house in DUSTHANA_HOUSES
            val lagnaLordDebilitated = YogaHelpers.isDebilitated(lagnaLordPos)
            val lagnaLordCombust = YogaHelpers.getCombustionFactor(lagnaLordPos, chart) < 0.5

            if (lagnaLordInDusthana && (lagnaLordDebilitated || lagnaLordCombust)) {
                yogas.add(Yoga(
                    name = "Balarishta Yoga (Lagna Affliction)",
                    sanskritName = "बालारिष्ट योग",
                    category = YogaCategory.NEGATIVE_YOGA,
                    planets = listOfNotNull(lagnaLord),
                    houses = listOf(lagnaLordPos.house),
                    description = "Lagna lord weak in dusthana house",
                    effects = "Early life vitality challenges, requires health vigilance and proper care",
                    strength = YogaStrength.MODERATE,
                    strengthPercentage = 50.0,
                    isAuspicious = false,
                    activationPeriod = "Lagna lord dasha",
                    cancellationFactors = listOf(
                        "Jupiter aspect provides protection",
                        "Strong Moon mitigates",
                        "Neecha Bhanga if applicable"
                    )
                ))
            }
        }

        return yogas
    }

    /**
     * Evaluate Alpaayu (Short life) indicators
     */
    private fun evaluateAlpaayu(
        chart: VedicChart,
        houseLords: Map<Int, Planet>
    ): List<Yoga> {
        val yogas = mutableListOf<Yoga>()
        val lord8 = houseLords[8]
        val lord8Pos = chart.planetPositions.find { it.planet == lord8 }
        val saturnPos = chart.planetPositions.find { it.planet == Planet.SATURN }

        // Multiple malefics in 8th house
        val maleficsIn8th = chart.planetPositions.filter {
            it.house == 8 && it.planet in MALEFICS
        }

        if (maleficsIn8th.size >= 2) {
            // Check for cancellation
            val jupiterAspects8th = chart.planetPositions.find { it.planet == Planet.JUPITER }
                ?.let { jup -> jup.house == 8 || YogaHelpers.getHouseFrom(ZodiacSign.entries[7], jup.sign) in listOf(1, 5, 7, 9) }
                ?: false

            if (!jupiterAspects8th) {
                yogas.add(Yoga(
                    name = "Alpaayu Yoga",
                    sanskritName = "अल्पायु योग",
                    category = YogaCategory.NEGATIVE_YOGA,
                    planets = maleficsIn8th.map { it.planet },
                    houses = listOf(8),
                    description = "Multiple malefics afflicting 8th house of longevity",
                    effects = "Requires health vigilance, sudden health changes possible, remedial measures strongly recommended",
                    strength = YogaStrength.STRONG,
                    strengthPercentage = 65.0,
                    isAuspicious = false,
                    activationPeriod = "Dashas of 8th house occupants",
                    cancellationFactors = listOf(
                        "Jupiter's aspect on 8th house cancels",
                        "Strong Saturn in good dignity protects",
                        "Multiple longevity factors can override"
                    )
                ))
            }
        }

        return yogas
    }

    /**
     * Evaluate Maraka (Death-inflicting) Yogas
     */
    private fun evaluateMaraka(
        chart: VedicChart,
        houseLords: Map<Int, Planet>
    ): List<Yoga> {
        val yogas = mutableListOf<Yoga>()
        val lord2 = houseLords[2]
        val lord7 = houseLords[7]

        // 2nd lord analysis
        val lord2Pos = chart.planetPositions.find { it.planet == lord2 }
        if (lord2Pos != null) {
            val isMalefic = lord2 in MALEFICS
            val isInMarakaHouse = lord2Pos.house in MARAKA_HOUSES
            val isAfflicted = MALEFICS.any { malefic ->
                val maleficPos = chart.planetPositions.find { it.planet == malefic }
                maleficPos != null && malefic != lord2 && YogaHelpers.areConjunct(lord2Pos, maleficPos)
            }

            if (isMalefic && isInMarakaHouse && isAfflicted) {
                yogas.add(Yoga(
                    name = "Maraka Yoga (2nd Lord)",
                    sanskritName = "मारक योग",
                    category = YogaCategory.NEGATIVE_YOGA,
                    planets = listOfNotNull(lord2),
                    houses = listOf(2, lord2Pos.house),
                    description = "Malefic 2nd lord afflicted in Maraka house",
                    effects = "Potential health vulnerabilities during 2nd lord's dasha, requires careful monitoring",
                    strength = YogaStrength.MODERATE,
                    strengthPercentage = 55.0,
                    isAuspicious = false,
                    activationPeriod = "${lord2?.displayName} Mahadasha",
                    cancellationFactors = listOf(
                        "Strong 8th house protects",
                        "Benefic aspects mitigate",
                        "Saturn's positive placement adds longevity"
                    )
                ))
            }
        }

        // 7th lord analysis
        val lord7Pos = chart.planetPositions.find { it.planet == lord7 }
        if (lord7Pos != null) {
            val isMalefic = lord7 in MALEFICS
            val isWeakened = YogaHelpers.isDebilitated(lord7Pos) || YogaHelpers.getCombustionFactor(lord7Pos, chart) < 0.5

            if (isMalefic && isWeakened) {
                yogas.add(Yoga(
                    name = "Maraka Yoga (7th Lord)",
                    sanskritName = "मारक योग",
                    category = YogaCategory.NEGATIVE_YOGA,
                    planets = listOfNotNull(lord7),
                    houses = listOf(7, lord7Pos.house),
                    description = "Weakened malefic 7th lord",
                    effects = "Health attention needed during 7th lord periods, partnership stress may affect health",
                    strength = YogaStrength.MODERATE,
                    strengthPercentage = 50.0,
                    isAuspicious = false,
                    activationPeriod = "${lord7?.displayName} Mahadasha",
                    cancellationFactors = listOf(
                        "Strong Jupiter protects",
                        "Benefic association helps",
                        "Good 8th house indicators override"
                    )
                ))
            }
        }

        return yogas
    }

    /**
     * Evaluate Pitru Dosha (Ancestral/Father affliction)
     */
    private fun evaluatePitruDosha(
        chart: VedicChart,
        houseLords: Map<Int, Planet>
    ): Yoga? {
        val sunPos = chart.planetPositions.find { it.planet == Planet.SUN } ?: return null
        val lord9 = houseLords[9]
        val lord9Pos = chart.planetPositions.find { it.planet == lord9 }
        val rahuPos = chart.planetPositions.find { it.planet == Planet.RAHU }
        val ketuPos = chart.planetPositions.find { it.planet == Planet.KETU }

        // Sun with Rahu/Ketu
        val sunWithNode = (rahuPos != null && YogaHelpers.areConjunct(sunPos, rahuPos, 10.0)) ||
                (ketuPos != null && YogaHelpers.areConjunct(sunPos, ketuPos, 10.0))

        // Sun in 9th house with malefics
        val sunIn9thAfflicted = sunPos.house == 9 && MALEFICS.any { malefic ->
            val maleficPos = chart.planetPositions.find { it.planet == malefic && it.planet != Planet.SUN }
            maleficPos?.house == 9
        }

        // 9th lord afflicted
        val lord9Afflicted = lord9Pos != null && (
                YogaHelpers.isDebilitated(lord9Pos) ||
                        (rahuPos != null && YogaHelpers.areConjunct(lord9Pos, rahuPos)) ||
                        (ketuPos != null && YogaHelpers.areConjunct(lord9Pos, ketuPos))
                )

        if (sunWithNode || sunIn9thAfflicted || lord9Afflicted) {
            val severity = when {
                sunWithNode && lord9Afflicted -> 70.0
                sunWithNode -> 60.0
                lord9Afflicted -> 55.0
                else -> 50.0
            }

            return Yoga(
                name = "Pitru Dosha",
                sanskritName = "पितृ दोष",
                category = YogaCategory.NEGATIVE_YOGA,
                planets = listOfNotNull(Planet.SUN, lord9, Planet.RAHU),
                houses = listOf(9, sunPos.house),
                description = "Affliction to Sun and/or 9th house indicators",
                effects = "Ancestral karmic patterns affecting fortune, father relationship challenges, requires shraddha rituals",
                strength = YogaHelpers.strengthFromPercentage(severity),
                strengthPercentage = severity,
                isAuspicious = false,
                activationPeriod = "Sun, Rahu, or 9th lord periods",
                cancellationFactors = listOf(
                    "Jupiter's aspect on 9th house mitigates",
                    "Strong Sun in good dignity reduces",
                    "Regular ancestral rituals provide relief"
                )
            )
        }

        return null
    }

    /**
     * Evaluate Matru Dosha (Mother affliction)
     */
    private fun evaluateMatruDosha(
        chart: VedicChart,
        houseLords: Map<Int, Planet>
    ): Yoga? {
        val moonPos = chart.planetPositions.find { it.planet == Planet.MOON } ?: return null
        val lord4 = houseLords[4]
        val lord4Pos = chart.planetPositions.find { it.planet == lord4 }
        val rahuPos = chart.planetPositions.find { it.planet == Planet.RAHU }
        val ketuPos = chart.planetPositions.find { it.planet == Planet.KETU }
        val saturnPos = chart.planetPositions.find { it.planet == Planet.SATURN }

        // Moon with Rahu/Ketu/Saturn
        val moonAfflicted = listOfNotNull(rahuPos, ketuPos, saturnPos).any { malefic ->
            YogaHelpers.areConjunct(moonPos, malefic, 10.0)
        }

        // Moon in 4th house afflicted
        val moonIn4thAfflicted = moonPos.house == 4 && MALEFICS.any { malefic ->
            val maleficPos = chart.planetPositions.find { it.planet == malefic }
            maleficPos?.house == 4
        }

        // 4th lord weak
        val lord4Weak = lord4Pos != null && (
                YogaHelpers.isDebilitated(lord4Pos) ||
                        lord4Pos.house in DUSTHANA_HOUSES
                )

        if (moonAfflicted || moonIn4thAfflicted || lord4Weak) {
            val severity = when {
                moonAfflicted && lord4Weak -> 65.0
                moonAfflicted -> 55.0
                lord4Weak -> 50.0
                else -> 45.0
            }

            return Yoga(
                name = "Matru Dosha",
                sanskritName = "मातृ दोष",
                category = YogaCategory.NEGATIVE_YOGA,
                planets = listOfNotNull(Planet.MOON, lord4),
                houses = listOf(4, moonPos.house),
                description = "Affliction to Moon and/or 4th house indicators",
                effects = "Mother relationship challenges, domestic unrest, emotional patterns from childhood",
                strength = YogaHelpers.strengthFromPercentage(severity),
                strengthPercentage = severity,
                isAuspicious = false,
                activationPeriod = "Moon or 4th lord periods",
                cancellationFactors = listOf(
                    "Jupiter's aspect on Moon mitigates",
                    "Strong 4th house protects",
                    "Venus association helps emotional balance"
                )
            )
        }

        return null
    }

    /**
     * Evaluate Gandanta Yoga (Junction point birth)
     */
    private fun evaluateGandantaYoga(chart: VedicChart): Yoga? {
        val moonPos = chart.planetPositions.find { it.planet == Planet.MOON } ?: return null

        // Check if Moon is in Gandanta
        val isInGandanta = GANDANTA_RANGES.any { (start, end) ->
            moonPos.longitude in start..end ||
                    (start > end && (moonPos.longitude >= start || moonPos.longitude <= end))
        }

        // Check Lagna in Gandanta
        val lagnaInGandanta = GANDANTA_RANGES.any { (start, end) ->
            chart.ascendant in start..end ||
                    (start > end && (chart.ascendant >= start || chart.ascendant <= end))
        }

        if (isInGandanta || lagnaInGandanta) {
            val gandantaType = when {
                isInGandanta && lagnaInGandanta -> "Moon and Lagna in Gandanta"
                isInGandanta -> "Moon in Gandanta"
                else -> "Lagna in Gandanta"
            }

            val severity = when {
                isInGandanta && lagnaInGandanta -> 70.0
                isInGandanta -> 60.0
                else -> 55.0
            }

            return Yoga(
                name = "Gandanta Yoga",
                sanskritName = "गण्डान्त योग",
                category = YogaCategory.NEGATIVE_YOGA,
                planets = if (isInGandanta) listOf(Planet.MOON) else emptyList(),
                houses = listOf(1, moonPos.house),
                description = gandantaType,
                effects = "Birth at water-fire sign junction, transformative life challenges, karmic knots requiring resolution",
                strength = YogaHelpers.strengthFromPercentage(severity),
                strengthPercentage = severity,
                isAuspicious = false,
                activationPeriod = "Throughout life, especially during major transitions",
                cancellationFactors = listOf(
                    "Gandanta shanti rituals recommended",
                    "Strong Jupiter provides protection",
                    "Effects can become transformative positively with spiritual practice"
                )
            )
        }

        return null
    }

    /**
     * Evaluate Combustion-related Yogas
     */
    private fun evaluateCombustionYogas(chart: VedicChart): List<Yoga> {
        val yogas = mutableListOf<Yoga>()
        val sunPos = chart.planetPositions.find { it.planet == Planet.SUN } ?: return yogas

        val combustiblePlanets = listOf(Planet.MOON, Planet.MARS, Planet.MERCURY, Planet.JUPITER, Planet.VENUS, Planet.SATURN)

        combustiblePlanets.forEach { planet ->
            val planetPos = chart.planetPositions.find { it.planet == planet } ?: return@forEach
            val combustionFactor = YogaHelpers.getCombustionFactor(planetPos, chart)

            // Severely combust (factor < 0.4)
            if (combustionFactor < 0.4) {
                val effects = when (planet) {
                    Planet.MOON -> "Mental stress, mother's health issues, emotional instability"
                    Planet.MARS -> "Diminished energy, sibling challenges, courage deficit"
                    Planet.MERCURY -> "Communication issues, nervous problems, business setbacks"
                    Planet.JUPITER -> "Wisdom obscured, guru problems, children challenges"
                    Planet.VENUS -> "Relationship difficulties, comfort reduction, artistic blocks"
                    Planet.SATURN -> "Career obstacles, chronic health issues, authority problems"
                    else -> "Planet's significations weakened"
                }

                yogas.add(Yoga(
                    name = "${planet.displayName} Asta Yoga (Deep Combustion)",
                    sanskritName = "${getPlanetSanskritName(planet)} अस्त योग",
                    category = YogaCategory.NEGATIVE_YOGA,
                    planets = listOf(planet, Planet.SUN),
                    houses = listOf(planetPos.house),
                    description = "${planet.displayName} deeply combust within ${getProximityDegrees(combustionFactor)}° of Sun",
                    effects = effects,
                    strength = YogaStrength.STRONG,
                    strengthPercentage = 65.0 - (combustionFactor * 20),
                    isAuspicious = false,
                    activationPeriod = "${planet.displayName} Dasha periods",
                    cancellationFactors = listOf(
                        "Retrograde ${planet.displayName} reduces combustion effect",
                        "${planet.displayName} in own sign mitigates",
                        "Effects reduce when planet moves away from Sun"
                    )
                ))
            }
        }

        return yogas
    }

    /**
     * Evaluate Duryoga (General misfortune)
     */
    private fun evaluateDuryoga(
        chart: VedicChart,
        houseLords: Map<Int, Planet>
    ): Yoga? {
        var duryogaIndicators = 0
        val involvedPlanets = mutableListOf<Planet>()

        // Check multiple negative indicators
        // 1. Lagna lord in dusthana
        val lagnaLord = houseLords[1]
        val lagnaLordPos = chart.planetPositions.find { it.planet == lagnaLord }
        if (lagnaLordPos?.house in DUSTHANA_HOUSES) {
            duryogaIndicators++
            lagnaLord?.let { involvedPlanets.add(it) }
        }

        // 2. Moon weak and afflicted
        val moonPos = chart.planetPositions.find { it.planet == Planet.MOON }
        if (moonPos != null) {
            val moonWeak = YogaHelpers.getMoonPhaseStrength(moonPos, chart) < 0.25
            val moonAfflicted = YogaHelpers.getMaleficAfflictionFactor(moonPos, chart) < 0.5
            if (moonWeak && moonAfflicted) {
                duryogaIndicators++
                involvedPlanets.add(Planet.MOON)
            }
        }

        // 3. Jupiter weak or afflicted
        val jupiterPos = chart.planetPositions.find { it.planet == Planet.JUPITER }
        if (jupiterPos != null) {
            if (YogaHelpers.isDebilitated(jupiterPos) || jupiterPos.house in DUSTHANA_HOUSES) {
                duryogaIndicators++
                involvedPlanets.add(Planet.JUPITER)
            }
        }

        // 4. Multiple malefics in Kendras without benefic influence
        val maleficsInKendras = chart.planetPositions.filter {
            it.planet in MALEFICS && it.house in listOf(1, 4, 7, 10)
        }
        val beneficsInKendras = chart.planetPositions.filter {
            it.planet in BENEFICS && it.house in listOf(1, 4, 7, 10)
        }
        if (maleficsInKendras.size >= 3 && beneficsInKendras.isEmpty()) {
            duryogaIndicators++
        }

        if (duryogaIndicators >= 3) {
            return Yoga(
                name = "Duryoga",
                sanskritName = "दुर्योग",
                category = YogaCategory.NEGATIVE_YOGA,
                planets = involvedPlanets.distinct(),
                houses = listOf(1, 6, 8, 12).filter { houseLords[it] in involvedPlanets },
                description = "Multiple affliction indicators present",
                effects = "General life challenges requiring persistent effort, success comes through overcoming obstacles",
                strength = YogaHelpers.strengthFromPercentage(55.0 + (duryogaIndicators * 5)),
                strengthPercentage = (55.0 + (duryogaIndicators * 5)).coerceAtMost(80.0),
                isAuspicious = false,
                activationPeriod = "Throughout life",
                cancellationFactors = listOf(
                    "Strong Jupiter can significantly mitigate",
                    "Raja Yoga presence overrides",
                    "Determined effort transforms challenges"
                )
            )
        }

        return null
    }

    /**
     * Evaluate Asubhakartari (Malefic scissors) Yoga for various houses
     */
    private fun evaluateAsubhakartari(chart: VedicChart): List<Yoga> {
        val yogas = mutableListOf<Yoga>()

        // Check important houses: 1 (self), 2 (wealth), 4 (happiness), 5 (children), 7 (spouse), 9 (fortune), 10 (career)
        val importantHouses = listOf(1, 2, 4, 5, 7, 9, 10)

        importantHouses.forEach { house ->
            val prevHouse = if (house == 1) 12 else house - 1
            val nextHouse = if (house == 12) 1 else house + 1

            val hasMaleficBefore = chart.planetPositions.any { it.planet in MALEFICS && it.house == prevHouse }
            val hasMaleficAfter = chart.planetPositions.any { it.planet in MALEFICS && it.house == nextHouse }

            // Only if no benefic in the house itself
            val hasBeneficInHouse = chart.planetPositions.any { it.planet in BENEFICS && it.house == house }

            if (hasMaleficBefore && hasMaleficAfter && !hasBeneficInHouse) {
                val houseSignification = getHouseSignification(house)

                yogas.add(Yoga(
                    name = "Papakartari Yoga (${ordinal(house)} House)",
                    sanskritName = "पापकर्तरी योग",
                    category = YogaCategory.NEGATIVE_YOGA,
                    planets = emptyList(),
                    houses = listOf(prevHouse, house, nextHouse),
                    description = "${ordinal(house)} house hemmed between malefics",
                    effects = "Challenges in $houseSignification, pressure from both directions, requires extra effort",
                    strength = YogaStrength.MODERATE,
                    strengthPercentage = 55.0,
                    isAuspicious = false,
                    activationPeriod = "During periods of ${ordinal(house)} house lord",
                    cancellationFactors = listOf("Benefic planet in the house cancels", "Jupiter aspect mitigates")
                ))
            }
        }

        return yogas
    }

    /**
     * Evaluate Lagna Dosha (Ascendant affliction)
     */
    private fun evaluateLagnaDosha(
        chart: VedicChart,
        houseLords: Map<Int, Planet>
    ): Yoga? {
        val lagnaLord = houseLords[1] ?: return null
        val lagnaLordPos = chart.planetPositions.find { it.planet == lagnaLord } ?: return null

        // Multiple affliction criteria
        val isDebilitated = YogaHelpers.isDebilitated(lagnaLordPos)
        val isInDusthana = lagnaLordPos.house in DUSTHANA_HOUSES
        val isCombust = YogaHelpers.getCombustionFactor(lagnaLordPos, chart) < 0.5
        val isAfflictedByMalefics = MALEFICS.count { malefic ->
            val maleficPos = chart.planetPositions.find { it.planet == malefic }
            maleficPos != null && YogaHelpers.areConjunct(lagnaLordPos, maleficPos)
        } >= 2

        val afflictionCount = listOf(isDebilitated, isInDusthana, isCombust, isAfflictedByMalefics).count { it }

        if (afflictionCount >= 2) {
            return Yoga(
                name = "Lagna Dosha",
                sanskritName = "लग्न दोष",
                category = YogaCategory.NEGATIVE_YOGA,
                planets = listOf(lagnaLord),
                houses = listOf(1, lagnaLordPos.house),
                description = "Lagna lord multiply afflicted: ${buildAfflictionDescription(isDebilitated, isInDusthana, isCombust, isAfflictedByMalefics)}",
                effects = "Personality and health challenges, self-expression difficulties, requires conscious self-development",
                strength = YogaHelpers.strengthFromPercentage(45.0 + (afflictionCount * 10)),
                strengthPercentage = (45.0 + (afflictionCount * 10)).coerceAtMost(75.0),
                isAuspicious = false,
                activationPeriod = "Lagna lord dasha periods",
                cancellationFactors = listOf(
                    "Neecha Bhanga if applicable",
                    "Jupiter's aspect on Lagna or Lagna lord",
                    "Strong benefic influences can override"
                )
            )
        }

        return null
    }

    /**
     * Evaluate Rogadhipati (Disease lord) Yoga
     */
    private fun evaluateRogadhipati(
        chart: VedicChart,
        houseLords: Map<Int, Planet>
    ): Yoga? {
        val lord6 = houseLords[6] ?: return null
        val lord6Pos = chart.planetPositions.find { it.planet == lord6 } ?: return null

        // 6th lord in lagna or with lagna lord
        val inLagna = lord6Pos.house == 1
        val lagnaLord = houseLords[1]
        val lagnaLordPos = chart.planetPositions.find { it.planet == lagnaLord }
        val withLagnaLord = lagnaLordPos != null && YogaHelpers.areConjunct(lord6Pos, lagnaLordPos)

        if (inLagna || withLagnaLord) {
            return Yoga(
                name = "Rogadhipati Yoga",
                sanskritName = "रोगाधिपति योग",
                category = YogaCategory.NEGATIVE_YOGA,
                planets = listOfNotNull(lord6, if (withLagnaLord) lagnaLord else null),
                houses = listOf(1, 6),
                description = if (inLagna) "6th lord in Lagna" else "6th lord conjunct Lagna lord",
                effects = "Tendency towards health issues, immune system needs attention, disease management awareness required",
                strength = YogaStrength.MODERATE,
                strengthPercentage = 55.0,
                isAuspicious = false,
                activationPeriod = "6th lord dasha",
                cancellationFactors = listOf(
                    "Strong 6th lord in good dignity can actually defeat enemies/diseases",
                    "Jupiter's influence provides protection"
                )
            )
        }

        return null
    }

    /**
     * Evaluate Bandhana (Imprisonment) Yoga
     */
    private fun evaluateBandhanaYoga(
        chart: VedicChart,
        houseLords: Map<Int, Planet>
    ): Yoga? {
        val lord12 = houseLords[12]
        val lord12Pos = chart.planetPositions.find { it.planet == lord12 }
        val saturnPos = chart.planetPositions.find { it.planet == Planet.SATURN }
        val rahuPos = chart.planetPositions.find { it.planet == Planet.RAHU }

        // Saturn and Rahu afflicting lagna or lagna lord
        val lagnaAfflicted = (saturnPos?.house == 1 || rahuPos?.house == 1) &&
                (saturnPos != null && rahuPos != null && saturnPos.house == rahuPos.house)

        // 12th lord with Saturn or Rahu in 1, 6, or 8
        val lord12Afflicted = lord12Pos != null &&
                lord12Pos.house in listOf(1, 6, 8) &&
                (saturnPos?.let { YogaHelpers.areConjunct(lord12Pos, it) } == true ||
                        rahuPos?.let { YogaHelpers.areConjunct(lord12Pos, it) } == true)

        if (lagnaAfflicted || lord12Afflicted) {
            return Yoga(
                name = "Bandhana Yoga",
                sanskritName = "बन्धन योग",
                category = YogaCategory.NEGATIVE_YOGA,
                planets = listOfNotNull(Planet.SATURN, Planet.RAHU, lord12),
                houses = listOf(1, 12),
                description = "Saturn-Rahu affliction pattern related to confinement",
                effects = "Potential for restrictions, confinement situations, legal troubles - usually manifests as mental confinement or binding circumstances rather than literal imprisonment",
                strength = YogaStrength.WEAK,
                strengthPercentage = 45.0,
                isAuspicious = false,
                activationPeriod = "Saturn-Rahu or Rahu-Saturn periods",
                cancellationFactors = listOf(
                    "Jupiter's strong influence cancels",
                    "Strong 9th house provides protection",
                    "Usually manifests only in extreme cases"
                )
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

    private fun getHouseSignification(house: Int): String = when (house) {
        1 -> "self, health, and personality"
        2 -> "wealth, family, and speech"
        4 -> "happiness, property, and mother"
        5 -> "children, creativity, and intelligence"
        7 -> "marriage, partnerships, and business"
        9 -> "fortune, father, and higher learning"
        10 -> "career, reputation, and authority"
        else -> "related life areas"
    }

    private fun getPlanetSanskritName(planet: Planet): String = when (planet) {
        Planet.SUN -> "सूर्य"
        Planet.MOON -> "चन्द्र"
        Planet.MARS -> "मंगल"
        Planet.MERCURY -> "बुध"
        Planet.JUPITER -> "गुरु"
        Planet.VENUS -> "शुक्र"
        Planet.SATURN -> "शनि"
        else -> planet.displayName
    }

    private fun getProximityDegrees(combustionFactor: Double): Int = when {
        combustionFactor < 0.2 -> 3
        combustionFactor < 0.4 -> 6
        combustionFactor < 0.6 -> 10
        else -> 15
    }

    private fun buildAfflictionDescription(
        isDebilitated: Boolean,
        isInDusthana: Boolean,
        isCombust: Boolean,
        isAfflictedByMalefics: Boolean
    ): String {
        val reasons = mutableListOf<String>()
        if (isDebilitated) reasons.add("debilitated")
        if (isInDusthana) reasons.add("in dusthana")
        if (isCombust) reasons.add("combust")
        if (isAfflictedByMalefics) reasons.add("afflicted by malefics")
        return reasons.joinToString(", ")
    }
}
