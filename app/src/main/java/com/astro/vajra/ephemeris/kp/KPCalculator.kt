package com.astro.vajra.ephemeris.kp

import com.astro.vajra.core.model.HouseSystem
import com.astro.vajra.core.model.Nakshatra
import com.astro.vajra.core.model.Planet
import com.astro.vajra.core.model.VedicChart
import com.astro.vajra.core.model.ZodiacSign
import com.astro.vajra.ephemeris.AstrologicalConstants
import com.astro.vajra.ephemeris.SwissEphemerisEngine

/**
 * KP Calculator - Main calculation engine for Krishnamurti Paddhati System
 *
 * KP System fundamentals:
 * - Uses Placidus house system (mandatory in KP)
 * - Uses KP Ayanamsa (Krishnamurti ayanamsa = SE_SIDM_KRISHNAMURTI)
 * - Each cusp and planet position is analyzed for sign lord, star lord, sub lord, sub-sub lord
 * - Sub-lord is the primary determinant in KP (not the planet or sign lord)
 *
 * This calculator can work with:
 * 1. An existing VedicChart (recalculating cusps with Placidus if needed)
 * 2. Birth data directly via SwissEphemerisEngine
 *
 * Reference: K.S. Krishnamurti - KP Reader Volumes 1-6
 */
object KPCalculator {

    /**
     * Perform complete KP analysis on a VedicChart.
     *
     * If the chart was not calculated with Placidus house system, the cusps
     * from the chart are still used but a warning is implicit. For best accuracy,
     * the chart should be recalculated with Placidus and KP Ayanamsa.
     *
     * @param chart The Vedic chart to analyze
     * @param ephemerisEngine Optional engine for recalculating with Placidus cusps
     * @return Complete KP analysis result
     */
    fun analyze(
        chart: VedicChart,
        ephemerisEngine: SwissEphemerisEngine? = null
    ): KPAnalysisResult {
        // Determine cusps - prefer Placidus recalculation if engine available
        val (cusps, ayanamsaValue) = if (ephemerisEngine != null && chart.houseSystem != HouseSystem.PLACIDUS) {
            val kpChart = ephemerisEngine.calculateVedicChart(chart.birthData, HouseSystem.PLACIDUS)
            Pair(kpChart.houseCusps, kpChart.ayanamsa)
        } else {
            Pair(chart.houseCusps, chart.ayanamsa)
        }

        // Analyze all 12 cusps
        val cuspResults = cusps.mapIndexed { index, cuspDegree ->
            analyzeCusp(index + 1, cuspDegree)
        }

        // Analyze all planet positions
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
                    house = determineHouse(pp.longitude, cusps),
                    formattedDegree = kpPos.formattedDegree
                )
            }

        // Calculate significators
        val significatorTable = KPSignificatorCalculator.calculateSignificators(
            cuspResults = cuspResults,
            planetResults = planetResults,
            cusps = cusps
        )

        // Calculate ruling planets at chart time
        val rulingPlanets = KPRulingPlanetsCalculator.calculateFromChart(chart, cusps)

        return KPAnalysisResult(
            cusps = cuspResults,
            planets = planetResults,
            significatorTable = significatorTable,
            rulingPlanets = rulingPlanets,
            ayanamsaValue = ayanamsaValue,
            ayanamsaName = chart.ayanamsaName
        )
    }

    /**
     * Analyze a single house cusp.
     */
    fun analyzeCusp(cuspNumber: Int, longitude: Double): KPCuspResult {
        val kpPos = KPSubLordTable.getKPPosition(longitude)
        return KPCuspResult(
            cuspNumber = cuspNumber,
            longitude = longitude,
            sign = kpPos.sign,
            signLord = kpPos.signLord,
            starLord = kpPos.starLord,
            subLord = kpPos.subLord,
            subSubLord = kpPos.subSubLord,
            formattedDegree = kpPos.formattedDegree
        )
    }

    /**
     * Determine which house a planet occupies based on Placidus cusp boundaries.
     *
     * In Placidus, house boundaries are defined by cusp positions.
     * A planet is in house N if it falls between cusp N and cusp N+1.
     */
    fun determineHouse(planetLongitude: Double, cusps: List<Double>): Int {
        val normalized = AstrologicalConstants.normalizeDegree(planetLongitude)

        for (i in 0 until 12) {
            val cuspStart = AstrologicalConstants.normalizeDegree(cusps[i])
            val cuspEnd = AstrologicalConstants.normalizeDegree(cusps[(i + 1) % 12])

            if (cuspStart < cuspEnd) {
                // Normal case: cusp start < cusp end
                if (normalized >= cuspStart - 1e-6 && normalized < cuspEnd - 1e-6) {
                    return i + 1
                }
            } else {
                // Wrap-around case (e.g., cusp at 350° to next at 20°)
                if (normalized >= cuspStart - 1e-6 || normalized < cuspEnd - 1e-6) {
                    return i + 1
                }
            }
        }

        // Fallback: use sign-based house determination
        return ((normalized / 30.0).toInt() % 12) + 1
    }

    /**
     * Format a degree value in KP notation: DD° MM' SS" Sign
     */
    fun formatDegree(longitude: Double): String {
        return KPSubLordTable.formatDegree(longitude)
    }
}
