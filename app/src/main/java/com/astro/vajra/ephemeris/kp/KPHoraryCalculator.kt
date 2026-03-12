package com.astro.vajra.ephemeris.kp

import com.astro.vajra.core.model.BirthData
import com.astro.vajra.core.model.HouseSystem
import com.astro.vajra.core.model.Planet
import com.astro.vajra.core.model.VedicChart
import com.astro.vajra.ephemeris.AstrologicalConstants
import com.astro.vajra.ephemeris.SwissEphemerisEngine
import java.time.LocalDateTime

/**
 * KP Horary Calculator
 *
 * In KP Horary astrology, the querent thinks of a number between 1 and 249.
 * This number maps directly to one of the 249 sub-lord divisions of the zodiac.
 *
 * Process:
 * 1. The chosen number maps to a specific zodiac sub-division (from KP Sub-Lord Table)
 * 2. The midpoint of that sub-division becomes the ascendant (Lagna) of the horary chart
 * 3. House cusps are calculated using Placidus system from this ascendant
 * 4. Planet positions are taken for the moment of query
 * 5. Standard KP analysis (sub-lords, significators) is applied
 *
 * This method provides a precise ascendant without depending on exact birth time,
 * making it ideal for answering specific questions.
 *
 * Reference: K.S. Krishnamurti, KP Reader Vol. 3, "Horary Astrology"
 */
object KPHoraryCalculator {

    /**
     * Get the ascendant degree for a given horary number (1-249).
     *
     * The ascendant is the midpoint of the corresponding sub-lord division.
     *
     * @param number Horary number (1-249)
     * @return Ascendant degree in the sidereal zodiac
     */
    fun getAscendantForNumber(number: Int): Double {
        require(number in 1..249) { "KP Horary number must be between 1 and 249" }
        val entry = KPSubLordTable.getEntryByNumber(number)
        return (entry.startDegree + entry.endDegree) / 2.0
    }

    /**
     * Get the horary data for a given number.
     *
     * @param number Horary number (1-249)
     * @return Horary data including ascendant and sub-lord analysis
     */
    fun getHoraryData(number: Int): KPHoraryData {
        require(number in 1..249) { "KP Horary number must be between 1 and 249" }
        val entry = KPSubLordTable.getEntryByNumber(number)
        val ascDegree = (entry.startDegree + entry.endDegree) / 2.0
        val position = KPSubLordTable.getKPPosition(ascDegree)

        return KPHoraryData(
            horaryNumber = number,
            ascendantDegree = ascDegree,
            subLordEntry = entry,
            ascendantPosition = position
        )
    }

    /**
     * Perform a complete KP horary analysis.
     *
     * Creates a horary chart using the given number for the ascendant and
     * current planetary positions for the moment of query.
     *
     * @param number Horary number (1-249)
     * @param chart VedicChart with current/query-time planetary positions
     * @param ephemerisEngine Optional engine for precise cusp recalculation
     * @return Complete KP analysis for the horary chart
     */
    fun analyzeHorary(
        number: Int,
        chart: VedicChart,
        ephemerisEngine: SwissEphemerisEngine? = null
    ): KPAnalysisResult {
        val horaryData = getHoraryData(number)

        // Use query-time chart's cusps but with the horary ascendant
        // If engine is available, recalculate Placidus cusps for the query moment
        val cusps = if (ephemerisEngine != null) {
            val queryChart = ephemerisEngine.calculateVedicChart(chart.birthData, HouseSystem.PLACIDUS)
            adjustCuspsForHoraryAscendant(queryChart.houseCusps, horaryData.ascendantDegree)
        } else {
            adjustCuspsForHoraryAscendant(chart.houseCusps, horaryData.ascendantDegree)
        }

        // Analyze cusps
        val cuspResults = cusps.mapIndexed { index, cuspDegree ->
            KPCalculator.analyzeCusp(index + 1, cuspDegree)
        }

        // Analyze planets (use the query-time positions)
        val planetResults = chart.planetPositions
            .filter { it.planet in Planet.MAIN_PLANETS }
            .map { pp ->
                val kpPos = KPSubLordTable.getKPPosition(pp.longitude)
                KPPlanetResult(
                    planet = pp.planet,
                    longitude = pp.longitude,
                    sign = kpPos.sign,
                    signLord = kpPos.signLord,
                    nakshatra = kpPos.nakshatra,
                    starLord = kpPos.starLord,
                    subLord = kpPos.subLord,
                    subSubLord = kpPos.subSubLord,
                    isRetrograde = pp.isRetrograde,
                    house = KPCalculator.determineHouse(pp.longitude, cusps),
                    formattedDegree = kpPos.formattedDegree
                )
            }

        // Significators
        val significatorTable = KPSignificatorCalculator.calculateSignificators(
            cuspResults = cuspResults,
            planetResults = planetResults,
            cusps = cusps
        )

        // Ruling planets for query time
        val rulingPlanets = KPRulingPlanetsCalculator.calculateFromChart(chart, cusps)

        return KPAnalysisResult(
            cusps = cuspResults,
            planets = planetResults,
            significatorTable = significatorTable,
            rulingPlanets = rulingPlanets,
            ayanamsaValue = chart.ayanamsa,
            ayanamsaName = chart.ayanamsaName
        )
    }

    /**
     * Adjust Placidus cusps so that cusp 1 matches the horary ascendant.
     *
     * The angular offset between the computed ascendant and the horary ascendant
     * is applied uniformly to all cusps. This preserves the Placidus house proportions
     * while shifting the entire chart to match the horary number.
     */
    private fun adjustCuspsForHoraryAscendant(
        originalCusps: List<Double>,
        horaryAscendant: Double
    ): List<Double> {
        if (originalCusps.isEmpty()) return originalCusps

        val offset = horaryAscendant - originalCusps[0]
        return originalCusps.map { cusp ->
            AstrologicalConstants.normalizeDegree(cusp + offset)
        }
    }

    /**
     * Get the range of numbers for a given sign.
     * Useful for displaying which horary numbers fall in each sign.
     */
    fun getNumbersForSign(sign: com.astro.vajra.core.model.ZodiacSign): List<Int> {
        return KPSubLordTable.entries
            .filter { it.sign == sign }
            .map { it.number }
    }
}
