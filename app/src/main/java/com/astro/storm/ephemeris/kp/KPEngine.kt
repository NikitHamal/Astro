package com.astro.storm.ephemeris.kp

import com.astro.storm.core.common.Language
import com.astro.storm.core.model.Planet
import com.astro.storm.core.model.VedicChart
import com.astro.storm.core.model.ZodiacSign
import java.time.DayOfWeek
import java.time.LocalDateTime

/**
 * Extension function to get cusp longitude by house number (1-12)
 */
fun VedicChart.getCuspLongitude(houseNumber: Int): Double {
    val index = (houseNumber - 1).coerceIn(0, 11)
    return if (houseCusps.size > index) houseCusps[index] else ascendant
}

/**
 * KP (Krishnamurti Padhdhati) Engine
 *
 * Main orchestration engine for complete KP system analysis.
 * This engine coordinates all KP sub-systems to provide:
 *
 * 1. Complete KP Chart Analysis
 *    - Planet positions with Star-Sub-Sub-Sub lords
 *    - Cusp positions with sub-lord analysis
 *    - Complete significator table
 *
 * 2. 4-Step Theory Verification
 *    - Event feasibility analysis
 *    - Multiple house group support
 *
 * 3. Ruling Planets
 *    - Current moment ruling planets
 *    - Verification of timing
 *
 * 4. KP Horary (Number-based)
 *    - 249 number system support
 *
 * Classical Reference:
 * - KP Reader series by Prof. K.S. Krishnamurti
 * - KP & Astrology Magazine publications
 *
 * @author AstroStorm - Ultra-Precision Vedic Astrology
 */
object KPEngine {

    /**
     * Perform complete KP analysis on a chart
     */
    fun analyze(
        chart: VedicChart,
        language: Language = Language.ENGLISH
    ): KPAnalysisResult {
        // Calculate all significators
        val significators = KPSignificatorCalculator.calculateAllSignificators(chart)

        // Build significator table
        val significatorTable = KPSignificatorCalculator.buildSignificatorTable(significators)

        // Calculate cusp analysis
        val cusps = KPSignificatorCalculator.calculateCuspAnalysis(chart, significators)

        // Build owned houses map
        val ownedHousesMap = buildOwnedHousesMap(chart)

        // Calculate planet positions with KP details
        val planets = KPSignificatorCalculator.calculatePlanetPositions(chart, significators, ownedHousesMap)

        // Calculate ruling planets for chart moment
        val rulingPlanets = calculateRulingPlanets(chart)

        // Create sub navigator
        val planetLongitudes = chart.planetPositions.associate { it.planet to it.longitude }
        val cuspLongitudes = (1..12).associate { it to chart.getCuspLongitude(it) }
        val subNavigator = KPSubCalculator.createSubNavigator(planetLongitudes, cuspLongitudes)

        return KPAnalysisResult(
            cusps = cusps,
            planets = planets,
            rulingPlanets = rulingPlanets,
            significatorTable = significatorTable,
            fourStepResults = emptyList(), // Populated on demand
            subLordNavigator = subNavigator,
            language = language
        )
    }

    /**
     * Build owned houses map for the chart
     */
    private fun buildOwnedHousesMap(chart: VedicChart): Map<Planet, List<Int>> {
        val ownedHouses = mutableMapOf<Planet, MutableList<Int>>()
        for (planet in Planet.MAIN_PLANETS) {
            ownedHouses[planet] = mutableListOf()
        }

        for (houseNum in 1..12) {
            val cuspLongitude = chart.getCuspLongitude(houseNum)
            val sign = ZodiacSign.fromLongitude(cuspLongitude)
            ownedHouses[sign.ruler]?.add(houseNum)
        }

        return ownedHouses
    }

    /**
     * Perform 4-Step analysis for a specific event
     */
    fun analyzeEvent(
        chart: VedicChart,
        houseGroup: HouseGroup,
        dashaLord: Planet,
        bhuktiLord: Planet,
        antarLord: Planet? = null,
        transitPositions: Map<Planet, Double>? = null
    ): FourStepTheoryResult {
        return KPFourStepEngine.analyze(
            chart = chart,
            houseGroup = houseGroup,
            currentDashaLord = dashaLord,
            currentBhuktiLord = bhuktiLord,
            currentAntarLord = antarLord,
            transitPositions = transitPositions
        )
    }

    /**
     * Analyze multiple events at once
     */
    fun analyzeMultipleEvents(
        chart: VedicChart,
        houseGroups: List<HouseGroup>,
        dashaLord: Planet,
        bhuktiLord: Planet,
        antarLord: Planet? = null
    ): Map<HouseGroup, FourStepTheoryResult> {
        return houseGroups.associateWith { houseGroup ->
            analyzeEvent(
                chart = chart,
                houseGroup = houseGroup,
                dashaLord = dashaLord,
                bhuktiLord = bhuktiLord,
                antarLord = antarLord
            )
        }
    }

    /**
     * Calculate ruling planets for a specific moment
     */
    fun calculateRulingPlanets(chart: VedicChart): RulingPlanets {
        // Ascendant details
        val ascLongitude = chart.getCuspLongitude(1)
        val ascSign = ZodiacSign.fromLongitude(ascLongitude)
        val ascPosition = KPSubCalculator.getKPPosition(ascLongitude)

        // Moon details
        val moonPosition = chart.planetPositions.find { it.planet == Planet.MOON }
        val moonLongitude = moonPosition?.longitude ?: 0.0
        val moonSign = ZodiacSign.fromLongitude(moonLongitude)
        val moonKPPosition = KPSubCalculator.getKPPosition(moonLongitude)

        // Day lord (based on chart date/time - simplified)
        val dayLord = getDayLord(chart)

        val allRuling = setOf(
            ascSign.ruler,
            ascPosition.starLord,
            moonSign.ruler,
            moonKPPosition.starLord,
            dayLord
        )

        return RulingPlanets(
            ascendantSignLord = ascSign.ruler,
            ascendantStarLord = ascPosition.starLord,
            moonSignLord = moonSign.ruler,
            moonStarLord = moonKPPosition.starLord,
            dayLord = dayLord,
            allRulingPlanets = allRuling
        )
    }

    /**
     * Calculate ruling planets for current moment
     */
    fun calculateCurrentRulingPlanets(
        currentAscLongitude: Double,
        currentMoonLongitude: Double,
        dateTime: LocalDateTime
    ): RulingPlanets {
        val ascSign = ZodiacSign.fromLongitude(currentAscLongitude)
        val ascPosition = KPSubCalculator.getKPPosition(currentAscLongitude)

        val moonSign = ZodiacSign.fromLongitude(currentMoonLongitude)
        val moonKPPosition = KPSubCalculator.getKPPosition(currentMoonLongitude)

        val dayLord = getDayLordFromDateTime(dateTime)

        val allRuling = setOf(
            ascSign.ruler,
            ascPosition.starLord,
            moonSign.ruler,
            moonKPPosition.starLord,
            dayLord
        )

        return RulingPlanets(
            ascendantSignLord = ascSign.ruler,
            ascendantStarLord = ascPosition.starLord,
            moonSignLord = moonSign.ruler,
            moonStarLord = moonKPPosition.starLord,
            dayLord = dayLord,
            allRulingPlanets = allRuling
        )
    }

    /**
     * Get day lord from chart (simplified - would need actual date)
     */
    private fun getDayLord(chart: VedicChart): Planet {
        // Default to Sun as placeholder - actual implementation would use chart date
        return Planet.SUN
    }

    /**
     * Get day lord from day of week
     */
    private fun getDayLordFromDateTime(dateTime: LocalDateTime): Planet {
        return when (dateTime.dayOfWeek) {
            DayOfWeek.SUNDAY -> Planet.SUN
            DayOfWeek.MONDAY -> Planet.MOON
            DayOfWeek.TUESDAY -> Planet.MARS
            DayOfWeek.WEDNESDAY -> Planet.MERCURY
            DayOfWeek.THURSDAY -> Planet.JUPITER
            DayOfWeek.FRIDAY -> Planet.VENUS
            DayOfWeek.SATURDAY -> Planet.SATURN
        }
    }

    /**
     * KP Horary analysis using number 1-249
     */
    fun analyzeHorary(
        number: Int,
        query: String,
        houseGroup: HouseGroup,
        currentDateTime: LocalDateTime
    ): KPHoraryResult {
        if (number < 1 || number > 249) {
            return KPHoraryResult(
                number = number,
                query = query,
                isValid = false,
                kpNumber = null,
                analysis = "Invalid number. Please provide a number between 1 and 249.",
                verdict = KPVerdict.NEUTRAL,
                rulingPlanets = null
            )
        }

        val kpNumber = KPSubCalculator.getKPNumber(number)
            ?: return KPHoraryResult(
                number = number,
                query = query,
                isValid = false,
                kpNumber = null,
                analysis = "Unable to calculate KP number details.",
                verdict = KPVerdict.NEUTRAL,
                rulingPlanets = null
            )

        // The selected number gives us the Ascendant position
        val ascLongitude = (number - 1) * (360.0 / 249) + (360.0 / 249 / 2)
        val ascPosition = KPSubCalculator.getKPPosition(ascLongitude)

        // Check if sub-lord signifies favorable houses
        val favorableMatch = houseGroup.favorableHouses.any { house ->
            // Simplified check - in full implementation would build significators
            checkBasicSignification(ascPosition.subLord, house)
        }
        val unfavorableMatch = houseGroup.unfavorableHouses.any { house ->
            checkBasicSignification(ascPosition.subLord, house)
        }

        val verdict = when {
            favorableMatch && !unfavorableMatch -> KPVerdict.POSITIVE
            !favorableMatch && unfavorableMatch -> KPVerdict.NEGATIVE
            favorableMatch && unfavorableMatch -> KPVerdict.NEUTRAL
            else -> KPVerdict.NEUTRAL
        }

        val analysis = buildString {
            appendLine("KP HORARY ANALYSIS")
            appendLine("═".repeat(40))
            appendLine("Number: $number")
            appendLine("Query: $query")
            appendLine()
            appendLine("ASCENDANT DETAILS:")
            appendLine("Sign: ${kpNumber.sign.displayName}")
            appendLine("Nakshatra: ${kpNumber.nakshatra.displayName}-${kpNumber.pada}")
            appendLine("Sub-Lord: ${kpNumber.subLord.displayName}")
            appendLine()
            appendLine("ANALYSIS FOR ${houseGroup.displayName.uppercase()}:")
            appendLine("Favorable Houses: ${houseGroup.favorableHouses.joinToString(", ")}")
            appendLine("Sub-Lord ${kpNumber.subLord.displayName} analysis:")

            when (verdict) {
                KPVerdict.POSITIVE, KPVerdict.STRONGLY_POSITIVE -> {
                    appendLine("The sub-lord signifies favorable houses for this query.")
                    appendLine("RESULT: POSITIVE - The matter will likely succeed.")
                }
                KPVerdict.NEGATIVE, KPVerdict.STRONGLY_NEGATIVE -> {
                    appendLine("The sub-lord signifies unfavorable houses for this query.")
                    appendLine("RESULT: NEGATIVE - The matter faces obstacles.")
                }
                KPVerdict.NEUTRAL -> {
                    appendLine("Mixed indications from the sub-lord.")
                    appendLine("RESULT: UNCERTAIN - Outcome depends on additional factors.")
                }
            }
        }

        return KPHoraryResult(
            number = number,
            query = query,
            isValid = true,
            kpNumber = kpNumber,
            analysis = analysis,
            verdict = verdict,
            rulingPlanets = null // Would calculate from current moment
        )
    }

    /**
     * Basic signification check (simplified)
     */
    private fun checkBasicSignification(planet: Planet, house: Int): Boolean {
        // Simplified - would need full chart for accurate signification
        val naturalSignifications = mapOf(
            Planet.SUN to listOf(1, 5, 9, 10),
            Planet.MOON to listOf(4, 2, 11),
            Planet.MARS to listOf(3, 6, 10),
            Planet.MERCURY to listOf(3, 6, 10, 11),
            Planet.JUPITER to listOf(2, 5, 9, 11),
            Planet.VENUS to listOf(2, 4, 7, 12),
            Planet.SATURN to listOf(6, 8, 10, 12),
            Planet.RAHU to listOf(6, 8, 11, 12),
            Planet.KETU to listOf(5, 9, 12)
        )
        return house in (naturalSignifications[planet] ?: emptyList())
    }

    /**
     * Get best significators for an event
     */
    fun getBestSignificatorsForEvent(
        chart: VedicChart,
        houseGroup: HouseGroup
    ): List<Planet> {
        val significators = KPSignificatorCalculator.calculateAllSignificators(chart)
        val analysis = KPSignificatorCalculator.getSignificatorsForHouseGroup(houseGroup, significators)
        return analysis.bestSignificators
    }

    /**
     * Check if event is promised (quick Step 1 check)
     */
    fun isEventPromised(chart: VedicChart, houseGroup: HouseGroup): Boolean {
        return KPFourStepEngine.isEventPromised(chart, houseGroup)
    }

    /**
     * Get detailed cusp analysis
     */
    fun getCuspAnalysis(chart: VedicChart, cuspNumber: Int): KPCusp? {
        val analysis = analyze(chart)
        return analysis.cusps.find { it.houseNumber == cuspNumber }
    }

    /**
     * Get sub-lord of a specific cusp
     */
    fun getCuspSubLord(chart: VedicChart, cuspNumber: Int): Planet {
        val cuspLongitude = chart.getCuspLongitude(cuspNumber)
        return KPSubCalculator.getSubLord(cuspLongitude)
    }

    /**
     * Get complete position details for a planet
     */
    fun getPlanetKPDetails(chart: VedicChart, planet: Planet): KPPlanetPosition? {
        val analysis = analyze(chart)
        return analysis.planets.find { it.planet == planet }
    }

    /**
     * Generate KP summary report
     */
    fun generateSummaryReport(chart: VedicChart, language: Language = Language.ENGLISH): String {
        val analysis = analyze(chart, language)

        return buildString {
            appendLine("KP SYSTEM ANALYSIS REPORT")
            appendLine("═".repeat(60))
            appendLine()

            // Cuspal Sub-Lords
            appendLine("CUSPAL SUB-LORDS")
            appendLine("─".repeat(60))
            for (cusp in analysis.cusps) {
                appendLine("House ${cusp.houseNumber.toString().padStart(2)}: " +
                        "${cusp.position.sign.abbreviation} " +
                        "Star: ${cusp.starLord.displayName.padEnd(8)} " +
                        "Sub: ${cusp.subLord.displayName}")
            }
            appendLine()

            // Planet Positions
            appendLine("PLANET POSITIONS WITH KP DETAILS")
            appendLine("─".repeat(60))
            for (planet in analysis.planets) {
                appendLine("${planet.planet.displayName.padEnd(10)}: " +
                        "${planet.position.toFormattedString()}")
            }
            appendLine()

            // Significator Table
            appendLine("SIGNIFICATOR TABLE")
            appendLine("─".repeat(60))
            for (house in 1..12) {
                val significators = analysis.getHouseSignificators(house)
                appendLine("House ${house.toString().padStart(2)}: ${significators.joinToString(", ") { it.displayName }}")
            }
            appendLine()

            // Ruling Planets
            appendLine("RULING PLANETS")
            appendLine("─".repeat(60))
            appendLine("Ascendant Sign Lord: ${analysis.rulingPlanets.ascendantSignLord.displayName}")
            appendLine("Ascendant Star Lord: ${analysis.rulingPlanets.ascendantStarLord.displayName}")
            appendLine("Moon Sign Lord: ${analysis.rulingPlanets.moonSignLord.displayName}")
            appendLine("Moon Star Lord: ${analysis.rulingPlanets.moonStarLord.displayName}")
            appendLine("Day Lord: ${analysis.rulingPlanets.dayLord.displayName}")
            appendLine("All Ruling: ${analysis.rulingPlanets.allRulingPlanets.joinToString(", ") { it.displayName }}")
        }
    }
}

/**
 * KP Horary result
 */
data class KPHoraryResult(
    val number: Int,
    val query: String,
    val isValid: Boolean,
    val kpNumber: KPNumber?,
    val analysis: String,
    val verdict: KPVerdict,
    val rulingPlanets: RulingPlanets?
)
