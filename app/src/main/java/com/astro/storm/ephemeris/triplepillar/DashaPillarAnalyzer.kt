package com.astro.storm.ephemeris.triplepillar

import com.astro.storm.core.model.Planet
import com.astro.storm.core.model.VedicChart
import com.astro.storm.core.model.ZodiacSign
import com.astro.storm.ephemeris.DashaCalculator
import com.astro.storm.ephemeris.ShadbalaCalculator
import com.astro.storm.ephemeris.VedicAstrologyUtils
import java.time.LocalDateTime

/**
 * Dasha Pillar Analyzer
 *
 * Analyzes the Vimshottari Dasha system as the first pillar of the Triple-Pillar
 * Predictive Engine. This evaluates:
 *
 * 1. Mahadasha Lord's functional nature (benefic/malefic for the chart)
 * 2. Antardasha Lord's relationship with Mahadasha Lord
 * 3. Dasha Lord's placement, dignity, and strength
 * 4. Yoga formations involving Dasha Lords
 * 5. Dasha Sandhi (transition) periods
 *
 * Classical References:
 * - Brihat Parashara Hora Shastra (Dasha Phala Adhyaya)
 * - Phaladeepika (Ch. 20)
 * - Uttara Kalamrita (Ch. 6)
 *
 * @author AstroStorm - Ultra-Precision Vedic Astrology
 */
object DashaPillarAnalyzer {

    /**
     * Houses that make a planet functional benefic (Kendras and Trikonas)
     * 1, 4, 7, 10 = Kendras (Angular houses)
     * 1, 5, 9 = Trikonas (Trines)
     * Combined unique: 1, 4, 5, 7, 9, 10
     */
    private val BENEFIC_HOUSES = setOf(1, 4, 5, 7, 9, 10)

    /**
     * Dusthana houses (houses of difficulty)
     * 6, 8, 12 = Trik houses
     */
    private val DUSTHANA_HOUSES = setOf(6, 8, 12)

    /**
     * Maraka houses (death-inflicting)
     * 2, 7 = Maraka Sthanas
     */
    private val MARAKA_HOUSES = setOf(2, 7)

    /**
     * Upachaya houses (houses of growth)
     * 3, 6, 10, 11 = Upachayas
     */
    private val UPACHAYA_HOUSES = setOf(3, 6, 10, 11)

    /**
     * Natural benefic planets
     */
    private val NATURAL_BENEFICS = setOf(Planet.JUPITER, Planet.VENUS, Planet.MERCURY, Planet.MOON)

    /**
     * Natural malefic planets
     */
    private val NATURAL_MALEFICS = setOf(Planet.SATURN, Planet.MARS, Planet.SUN, Planet.RAHU, Planet.KETU)

    /**
     * Yogakaraka configuration by Ascendant
     * Maps Ascendant Sign -> Yogakaraka Planet
     */
    private val YOGAKARAKA_BY_ASCENDANT = mapOf(
        ZodiacSign.ARIES to Planet.SUN,           // Sun rules 5th
        ZodiacSign.TAURUS to Planet.SATURN,       // Saturn rules 9th and 10th
        ZodiacSign.GEMINI to Planet.VENUS,        // Venus rules 5th (but debatable)
        ZodiacSign.CANCER to Planet.MARS,         // Mars rules 5th and 10th
        ZodiacSign.LEO to Planet.MARS,            // Mars rules 4th and 9th
        ZodiacSign.VIRGO to Planet.VENUS,         // Venus rules 2nd and 9th
        ZodiacSign.LIBRA to Planet.SATURN,        // Saturn rules 4th and 5th
        ZodiacSign.SCORPIO to Planet.MOON,        // Moon rules 9th
        ZodiacSign.SAGITTARIUS to Planet.SUN,     // Sun rules 9th
        ZodiacSign.CAPRICORN to Planet.VENUS,     // Venus rules 5th and 10th
        ZodiacSign.AQUARIUS to Planet.VENUS,      // Venus rules 4th and 9th
        ZodiacSign.PISCES to Planet.MARS          // Mars rules 2nd and 9th
    )

    /**
     * Analyze the Dasha Pillar for a given chart
     */
    fun analyzeDashaPillar(
        chart: VedicChart,
        dashaTimeline: DashaCalculator.DashaTimeline,
        targetDateTime: LocalDateTime = LocalDateTime.now()
    ): DashaPillarAnalysis {
        val currentMahadasha = dashaTimeline.currentMahadasha
            ?: throw IllegalStateException("No active Mahadasha found")

        val currentAntardasha = dashaTimeline.currentAntardasha
        val currentPratyantardasha = dashaTimeline.currentPratyantardasha

        val mahadashaPlanet = currentMahadasha.planet
        val antardashaPlanet = currentAntardasha?.planet
        val pratyantardashaPlanet = currentPratyantardasha?.planet

        // Get planet positions
        val mahadashaPosition = chart.planetPositions.find { it.planet == mahadashaPlanet }
            ?: throw IllegalStateException("Mahadasha planet not found in chart")

        val ascendantSign = ZodiacSign.fromLongitude(chart.ascendant)

        // Determine functional nature
        val mahadashaLordNature = determineFunctionalNature(
            planet = mahadashaPlanet,
            chart = chart,
            ascendantSign = ascendantSign
        )

        val antardashaNature = antardashaPlanet?.let {
            determineFunctionalNature(it, chart, ascendantSign)
        }

        // Calculate Dasha Lord Strength
        val dashaLordStrength = calculateDashaLordStrength(chart, mahadashaPlanet)

        // Check for Dasha Sandhi
        val (isDashaSandhi, sandhiSeverity) = checkDashaSandhi(dashaTimeline, targetDateTime)

        // Calculate relative positions from Dasha Lord
        val relativePositions = calculateRelativePositions(chart, mahadashaPlanet)

        // Find yoga formations
        val yogaFormations = findYogaFormations(chart, mahadashaPlanet, antardashaPlanet)

        // Generate interpretation
        val interpretation = generateDashaInterpretation(
            mahadashaPlanet = mahadashaPlanet,
            antardashaPlanet = antardashaPlanet,
            mahadashaLordNature = mahadashaLordNature,
            antardashaNature = antardashaNature,
            mahadashaPosition = mahadashaPosition,
            isDashaSandhi = isDashaSandhi,
            yogaFormations = yogaFormations
        )

        return DashaPillarAnalysis(
            mahadashaPlanet = mahadashaPlanet,
            antardashaPlanet = antardashaPlanet,
            pratyantardashaPlanet = pratyantardashaPlanet,
            mahadashaLordNature = mahadashaLordNature,
            antardashaNature = antardashaNature,
            mahadashaPlanetHouse = mahadashaPosition.house,
            mahadashaPlanetSign = mahadashaPosition.sign,
            dashaLordStrength = dashaLordStrength,
            isDashaSandhi = isDashaSandhi,
            sandhiSeverity = sandhiSeverity,
            relativePositions = relativePositions,
            yogaFormations = yogaFormations,
            interpretation = interpretation
        )
    }

    /**
     * Determine the functional nature of a planet for a specific chart
     */
    private fun determineFunctionalNature(
        planet: Planet,
        chart: VedicChart,
        ascendantSign: ZodiacSign
    ): DashaLordNature {
        var score = 0.0
        val housesOwned = getHousesOwnedByPlanet(planet, ascendantSign)
        val planetPosition = chart.planetPositions.find { it.planet == planet }

        // 1. Check if Yogakaraka
        val yogakaraka = YOGAKARAKA_BY_ASCENDANT[ascendantSign]
        if (planet == yogakaraka) {
            score += 0.35
        }

        // 2. Check houses owned
        housesOwned.forEach { house ->
            when (house) {
                in listOf(1, 5, 9) -> score += 0.20   // Trikona lords are benefic
                in listOf(4, 7, 10) -> score += 0.10  // Kendra lords - Kendradhipati dosha for benefics
                in DUSTHANA_HOUSES -> score -= 0.25   // Dusthana lords are malefic
                in MARAKA_HOUSES -> score -= 0.15     // Maraka lords need caution
                11 -> score += 0.05                    // 11th lord is mildly benefic
                3 -> score -= 0.05                     // 3rd lord is mildly malefic
            }
        }

        // 3. Natural benefic/malefic adjustment
        if (planet in NATURAL_BENEFICS) {
            // Benefics owning kendras lose beneficence (Kendradhipati Dosha)
            val ownsKendra = housesOwned.any { it in listOf(4, 7, 10) }
            if (ownsKendra && planet != Planet.MOON) {
                score -= 0.10
            } else {
                score += 0.10
            }
        } else if (planet in NATURAL_MALEFICS && planet != Planet.RAHU && planet != Planet.KETU) {
            // Malefics owning kendras gain beneficence
            val ownsKendra = housesOwned.any { it in listOf(4, 7, 10) }
            if (ownsKendra) {
                score += 0.10
            }
        }

        // 4. Check placement
        planetPosition?.let { pos ->
            // Exaltation/Debilitation
            if (isExalted(planet, pos.sign)) {
                score += 0.15
            } else if (isDebilitated(planet, pos.sign)) {
                score -= 0.20
            }

            // Own sign
            if (isOwnSign(planet, pos.sign)) {
                score += 0.10
            }

            // Placement in benefic houses
            when (pos.house) {
                in listOf(1, 5, 9) -> score += 0.10
                in listOf(4, 7, 10) -> score += 0.05
                in DUSTHANA_HOUSES -> score -= 0.10
            }
        }

        // 5. Check for combustion
        val sunPosition = chart.planetPositions.find { it.planet == Planet.SUN }
        planetPosition?.let { pos ->
            sunPosition?.let { sunPos ->
                val distance = VedicAstrologyUtils.angularDistance(pos.longitude, sunPos.longitude)
                if (planet != Planet.SUN && distance < 8.0) {
                    score -= 0.15 // Combustion weakens the planet
                }
            }
        }

        // 6. Rahu/Ketu special rules
        if (planet == Planet.RAHU || planet == Planet.KETU) {
            score = evaluateNodeNature(planet, chart, ascendantSign)
        }

        // Convert score to DashaLordNature
        return when {
            score >= 0.40 -> DashaLordNature.HIGHLY_BENEFIC
            score >= 0.15 -> DashaLordNature.BENEFIC
            score >= -0.10 -> DashaLordNature.NEUTRAL
            score >= -0.30 -> DashaLordNature.MALEFIC
            else -> DashaLordNature.HIGHLY_MALEFIC
        }
    }

    /**
     * Evaluate Rahu/Ketu's functional nature based on dispositor and conjunction
     */
    private fun evaluateNodeNature(
        node: Planet,
        chart: VedicChart,
        ascendantSign: ZodiacSign
    ): Double {
        var score = 0.0
        val nodePosition = chart.planetPositions.find { it.planet == node } ?: return 0.0

        // Nodes act as their sign dispositor
        val dispositor = nodePosition.sign.ruler
        val dispositorNature = determineFunctionalNature(dispositor, chart, ascendantSign)

        score = when (dispositorNature) {
            DashaLordNature.HIGHLY_BENEFIC -> 0.30
            DashaLordNature.BENEFIC -> 0.15
            DashaLordNature.NEUTRAL -> 0.0
            DashaLordNature.MALEFIC -> -0.15
            DashaLordNature.HIGHLY_MALEFIC -> -0.30
        }

        // Check for conjunctions
        chart.planetPositions.filter { it.planet != node && it.sign == nodePosition.sign }.forEach { conjunct ->
            val conjunctNature = determineFunctionalNature(conjunct.planet, chart, ascendantSign)
            score += when (conjunctNature) {
                DashaLordNature.HIGHLY_BENEFIC -> 0.10
                DashaLordNature.BENEFIC -> 0.05
                DashaLordNature.NEUTRAL -> 0.0
                DashaLordNature.MALEFIC -> -0.05
                DashaLordNature.HIGHLY_MALEFIC -> -0.10
            }
        }

        // Rahu in Upachaya houses is good
        if (node == Planet.RAHU && nodePosition.house in UPACHAYA_HOUSES) {
            score += 0.15
        }

        // Ketu in 12th is good for spirituality
        if (node == Planet.KETU && nodePosition.house == 12) {
            score += 0.10
        }

        return score
    }

    /**
     * Get houses owned by a planet based on ascendant
     */
    private fun getHousesOwnedByPlanet(planet: Planet, ascendantSign: ZodiacSign): List<Int> {
        val signsOwned = getSignsOwnedByPlanet(planet)
        return signsOwned.map { sign ->
            VedicAstrologyUtils.getHouseFromSigns(sign, ascendantSign)
        }
    }

    /**
     * Get signs owned by a planet
     */
    private fun getSignsOwnedByPlanet(planet: Planet): List<ZodiacSign> {
        return when (planet) {
            Planet.SUN -> listOf(ZodiacSign.LEO)
            Planet.MOON -> listOf(ZodiacSign.CANCER)
            Planet.MARS -> listOf(ZodiacSign.ARIES, ZodiacSign.SCORPIO)
            Planet.MERCURY -> listOf(ZodiacSign.GEMINI, ZodiacSign.VIRGO)
            Planet.JUPITER -> listOf(ZodiacSign.SAGITTARIUS, ZodiacSign.PISCES)
            Planet.VENUS -> listOf(ZodiacSign.TAURUS, ZodiacSign.LIBRA)
            Planet.SATURN -> listOf(ZodiacSign.CAPRICORN, ZodiacSign.AQUARIUS)
            else -> emptyList() // Rahu/Ketu don't own signs traditionally
        }
    }

    /**
     * Check if planet is exalted
     */
    private fun isExalted(planet: Planet, sign: ZodiacSign): Boolean {
        return when (planet) {
            Planet.SUN -> sign == ZodiacSign.ARIES
            Planet.MOON -> sign == ZodiacSign.TAURUS
            Planet.MARS -> sign == ZodiacSign.CAPRICORN
            Planet.MERCURY -> sign == ZodiacSign.VIRGO
            Planet.JUPITER -> sign == ZodiacSign.CANCER
            Planet.VENUS -> sign == ZodiacSign.PISCES
            Planet.SATURN -> sign == ZodiacSign.LIBRA
            Planet.RAHU -> sign == ZodiacSign.TAURUS || sign == ZodiacSign.GEMINI
            Planet.KETU -> sign == ZodiacSign.SCORPIO || sign == ZodiacSign.SAGITTARIUS
            else -> false
        }
    }

    /**
     * Check if planet is debilitated
     */
    private fun isDebilitated(planet: Planet, sign: ZodiacSign): Boolean {
        return when (planet) {
            Planet.SUN -> sign == ZodiacSign.LIBRA
            Planet.MOON -> sign == ZodiacSign.SCORPIO
            Planet.MARS -> sign == ZodiacSign.CANCER
            Planet.MERCURY -> sign == ZodiacSign.PISCES
            Planet.JUPITER -> sign == ZodiacSign.CAPRICORN
            Planet.VENUS -> sign == ZodiacSign.VIRGO
            Planet.SATURN -> sign == ZodiacSign.ARIES
            Planet.RAHU -> sign == ZodiacSign.SCORPIO || sign == ZodiacSign.SAGITTARIUS
            Planet.KETU -> sign == ZodiacSign.TAURUS || sign == ZodiacSign.GEMINI
            else -> false
        }
    }

    /**
     * Check if planet is in its own sign
     */
    private fun isOwnSign(planet: Planet, sign: ZodiacSign): Boolean {
        return sign.ruler == planet
    }

    /**
     * Calculate Dasha Lord's strength (simplified Shadbala-based)
     */
    private fun calculateDashaLordStrength(chart: VedicChart, planet: Planet): Double {
        // This would ideally use ShadbalaCalculator
        // For now, use a simplified strength calculation
        val position = chart.planetPositions.find { it.planet == planet } ?: return 50.0

        var strength = 50.0

        // Dignity-based strength
        if (isExalted(planet, position.sign)) {
            strength += 30.0
        } else if (isDebilitated(planet, position.sign)) {
            strength -= 20.0
        } else if (isOwnSign(planet, position.sign)) {
            strength += 20.0
        }

        // House-based strength
        when (position.house) {
            1, 4, 7, 10 -> strength += 15.0  // Kendras
            5, 9 -> strength += 20.0          // Trikonas
            3, 6, 11 -> strength += 5.0       // Upachayas
            8, 12 -> strength -= 10.0         // Dusthanas
        }

        // Retrograde strength (traditionally gives strength)
        if (position.isRetrograde && planet != Planet.SUN && planet != Planet.MOON) {
            strength += 10.0
        }

        return strength.coerceIn(0.0, 100.0)
    }

    /**
     * Check for Dasha Sandhi (transition period)
     */
    private fun checkDashaSandhi(
        timeline: DashaCalculator.DashaTimeline,
        targetDateTime: LocalDateTime
    ): Pair<Boolean, Double?> {
        val upcomingSandhis = timeline.getUpcomingSandhisWithin(30) // Within 30 days

        for (sandhi in upcomingSandhis) {
            if (sandhi.isWithinSandhi(targetDateTime)) {
                // Calculate severity based on how close to exact transition
                val totalSandhiSeconds = java.time.temporal.ChronoUnit.SECONDS.between(
                    sandhi.sandhiStartDate, sandhi.sandhiEndDate
                )
                val secondsFromStart = java.time.temporal.ChronoUnit.SECONDS.between(
                    sandhi.sandhiStartDate, targetDateTime
                )
                val midPoint = totalSandhiSeconds / 2.0

                // Severity is highest at the midpoint (transition moment)
                val distanceFromMid = kotlin.math.abs(secondsFromStart - midPoint)
                val severity = 1.0 - (distanceFromMid / midPoint).coerceIn(0.0, 1.0)

                // Mahadasha sandhi is more severe than Antardasha
                val levelMultiplier = when (sandhi.level) {
                    DashaCalculator.DashaLevel.MAHADASHA -> 1.0
                    DashaCalculator.DashaLevel.ANTARDASHA -> 0.6
                    DashaCalculator.DashaLevel.PRATYANTARDASHA -> 0.3
                    else -> 0.2
                }

                return Pair(true, severity * levelMultiplier)
            }
        }

        return Pair(false, null)
    }

    /**
     * Calculate relative positions from Dasha Lord
     */
    private fun calculateRelativePositions(
        chart: VedicChart,
        dashaLord: Planet
    ): Map<Planet, Int> {
        val dashaLordPosition = chart.planetPositions.find { it.planet == dashaLord }
            ?: return emptyMap()

        return chart.planetPositions.associate { position ->
            val houseFrom = VedicAstrologyUtils.getHouseFromSigns(
                position.sign, dashaLordPosition.sign
            )
            position.planet to houseFrom
        }
    }

    /**
     * Find yoga formations involving Dasha Lords
     */
    private fun findYogaFormations(
        chart: VedicChart,
        mahadashaPlanet: Planet,
        antardashaPlanet: Planet?
    ): List<String> {
        val yogas = mutableListOf<String>()
        val mahadashaPos = chart.planetPositions.find { it.planet == mahadashaPlanet }
        val antardashaPos = antardashaPlanet?.let { p -> chart.planetPositions.find { it.planet == p } }
        val ascendantSign = ZodiacSign.fromLongitude(chart.ascendant)

        mahadashaPos?.let { mdPos ->
            // Check for Raja Yoga (Kendra-Trikona connection)
            val mdOwnsKendra = getHousesOwnedByPlanet(mahadashaPlanet, ascendantSign).any { it in listOf(1, 4, 7, 10) }
            val mdOwnsTrikona = getHousesOwnedByPlanet(mahadashaPlanet, ascendantSign).any { it in listOf(1, 5, 9) }

            if (mdOwnsKendra && mdOwnsTrikona) {
                yogas.add("Raja Yoga: ${mahadashaPlanet.displayName} owns both Kendra and Trikona")
            }

            // Check if Mahadasha lord is with or aspecting Antardasha lord
            antardashaPos?.let { adPos ->
                if (mdPos.sign == adPos.sign) {
                    yogas.add("Dasha Lords Conjunction: ${mahadashaPlanet.displayName} conjunct ${antardashaPlanet?.displayName}")
                }

                val houseDiff = VedicAstrologyUtils.getHouseFromSigns(adPos.sign, mdPos.sign)
                if (houseDiff in listOf(5, 9)) {
                    yogas.add("Dasha Lords Trine: ${mahadashaPlanet.displayName} trines ${antardashaPlanet?.displayName}")
                }
            }

            // Check for Dhana Yoga (wealth)
            val mdOwns2Or11 = getHousesOwnedByPlanet(mahadashaPlanet, ascendantSign).any { it in listOf(2, 11) }
            val mdOwns5Or9 = getHousesOwnedByPlanet(mahadashaPlanet, ascendantSign).any { it in listOf(5, 9) }
            if (mdOwns2Or11 && mdOwns5Or9) {
                yogas.add("Dhana Yoga: ${mahadashaPlanet.displayName} connects wealth and fortune houses")
            }

            // Check for Viparita Raja Yoga
            val mdOwns6Or8Or12 = getHousesOwnedByPlanet(mahadashaPlanet, ascendantSign).any { it in listOf(6, 8, 12) }
            if (mdOwns6Or8Or12 && mdPos.house in listOf(6, 8, 12)) {
                yogas.add("Viparita Raja Yoga: Dusthana lord in Dusthana")
            }
        }

        return yogas
    }

    /**
     * Generate interpretation text
     */
    private fun generateDashaInterpretation(
        mahadashaPlanet: Planet,
        antardashaPlanet: Planet?,
        mahadashaLordNature: DashaLordNature,
        antardashaNature: DashaLordNature?,
        mahadashaPosition: com.astro.storm.core.model.PlanetPosition,
        isDashaSandhi: Boolean,
        yogaFormations: List<String>
    ): String = buildString {
        append("You are currently running the ${mahadashaPlanet.displayName} Mahadasha, ")

        when (mahadashaLordNature) {
            DashaLordNature.HIGHLY_BENEFIC -> {
                append("which is exceptionally favorable for your chart. ")
                append("${mahadashaPlanet.displayName} acts as a Yogakaraka or strong benefic, ")
                append("promising excellent results in its significations. ")
            }
            DashaLordNature.BENEFIC -> {
                append("which is generally favorable. ")
                append("${mahadashaPlanet.displayName} functions as a benefic for your ascendant, ")
                append("bringing positive developments. ")
            }
            DashaLordNature.NEUTRAL -> {
                append("which brings mixed results. ")
                append("${mahadashaPlanet.displayName} is neutral for your chart, ")
                append("and results will depend on sub-periods and transits. ")
            }
            DashaLordNature.MALEFIC -> {
                append("which requires careful navigation. ")
                append("${mahadashaPlanet.displayName} is functionally challenging for your ascendant. ")
                append("Focus on remedies and timing. ")
            }
            DashaLordNature.HIGHLY_MALEFIC -> {
                append("which is a challenging period. ")
                append("${mahadashaPlanet.displayName} rules difficult houses for your chart. ")
                append("Patience, remedies, and careful timing are essential. ")
            }
        }

        append("The Dasha lord is placed in ${mahadashaPosition.sign.displayName} ")
        append("in the ${mahadashaPosition.house}${getOrdinalSuffix(mahadashaPosition.house)} house. ")

        antardashaPlanet?.let { ad ->
            append("The current Antardasha of ${ad.displayName} ")
            when (antardashaNature) {
                DashaLordNature.HIGHLY_BENEFIC, DashaLordNature.BENEFIC -> {
                    append("is supportive and enhances the period's potential. ")
                }
                DashaLordNature.NEUTRAL -> {
                    append("brings balanced energy to this period. ")
                }
                DashaLordNature.MALEFIC, DashaLordNature.HIGHLY_MALEFIC -> {
                    append("adds challenges that need to be navigated carefully. ")
                }
                null -> {}
            }
        }

        if (isDashaSandhi) {
            append("Important: You are in a Dasha transition (Sandhi) period. ")
            append("This is a time of uncertainty and adjustment. ")
            append("New initiatives may face obstacles. ")
        }

        if (yogaFormations.isNotEmpty()) {
            append("Notable combinations: ${yogaFormations.take(2).joinToString("; ")}. ")
        }
    }

    private fun getOrdinalSuffix(n: Int): String {
        return when {
            n % 100 in 11..13 -> "th"
            n % 10 == 1 -> "st"
            n % 10 == 2 -> "nd"
            n % 10 == 3 -> "rd"
            else -> "th"
        }
    }
}
