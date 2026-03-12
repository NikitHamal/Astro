package com.astro.vajra.ephemeris.kp

import com.astro.vajra.core.model.Nakshatra
import com.astro.vajra.core.model.Planet
import com.astro.vajra.core.model.ZodiacSign

/**
 * KP (Krishnamurti Paddhati) System Data Models
 *
 * These models represent the complete KP astrological analysis structures
 * including sub-lord divisions, cusp analysis, significators, ruling planets,
 * and horary chart data.
 */

// ============================================================================
// SUB-LORD TABLE MODELS
// ============================================================================

/**
 * Represents a single entry in the KP 249-division sub-lord table.
 *
 * Each entry maps a zodiac degree range to its sign lord, star lord, and sub lord.
 * The 249 entries arise from 27 nakshatras × 9 sub-divisions, with additional
 * entries where a sub-division straddles a sign boundary.
 */
data class KPSubLordEntry(
    val number: Int,
    val startDegree: Double,
    val endDegree: Double,
    val signLord: Planet,
    val starLord: Planet,
    val subLord: Planet,
    val sign: ZodiacSign
) {
    val spanDegrees: Double get() = endDegree - startDegree
}

/**
 * Full KP positional breakdown for any zodiac degree.
 * Includes sign lord, star lord, sub lord, and sub-sub lord.
 */
data class KPPosition(
    val longitude: Double,
    val sign: ZodiacSign,
    val signLord: Planet,
    val nakshatra: Nakshatra,
    val starLord: Planet,
    val subLord: Planet,
    val subSubLord: Planet,
    val degreeInSign: Double,
    val formattedDegree: String
)

// ============================================================================
// CUSP ANALYSIS MODELS
// ============================================================================

/**
 * KP analysis of a single house cusp.
 */
data class KPCuspResult(
    val cuspNumber: Int,
    val longitude: Double,
    val sign: ZodiacSign,
    val signLord: Planet,
    val starLord: Planet,
    val subLord: Planet,
    val subSubLord: Planet,
    val formattedDegree: String
)

// ============================================================================
// PLANET ANALYSIS MODELS
// ============================================================================

/**
 * KP analysis of a planet's position.
 */
data class KPPlanetResult(
    val planet: Planet,
    val longitude: Double,
    val sign: ZodiacSign,
    val signLord: Planet,
    val nakshatra: Nakshatra,
    val starLord: Planet,
    val subLord: Planet,
    val subSubLord: Planet,
    val isRetrograde: Boolean,
    val house: Int,
    val formattedDegree: String
)

// ============================================================================
// SIGNIFICATOR MODELS
// ============================================================================

/**
 * Complete KP significator analysis for a single house.
 * Follows the 4-step KP method (CSOC - Cuspal Sub-Lord, Star-Lord, Owner, Conjunction).
 */
data class KPHouseSignificators(
    val houseNumber: Int,
    /** Step 1: Planets posited in the stars of occupants of this house */
    val starlordOfOccupants: List<Planet>,
    /** Step 2: Planets occupying this house */
    val occupants: List<Planet>,
    /** Step 3: Planets posited in the stars of the owner of this house */
    val starlordOfOwner: List<Planet>,
    /** Step 4: Owner of this house (sign lord of cusp) */
    val owner: Planet,
    /** Planets conjoined with or aspecting significators (modifiers) */
    val conjoinedAspected: List<Planet>
) {
    /**
     * Returns all significators in priority order (Step 1 strongest to Step 4 weakest).
     */
    val allSignificators: List<Planet>
        get() = (starlordOfOccupants + occupants + starlordOfOwner + listOf(owner))
            .distinct()

    /**
     * Returns the strongest significators (Steps 1 & 2 combined, deduplicated).
     */
    val strongestSignificators: List<Planet>
        get() = (starlordOfOccupants + occupants).distinct()
}

/**
 * Complete significator table for all 12 houses.
 */
data class KPSignificatorTable(
    val houses: Map<Int, KPHouseSignificators>,
    /** Reverse mapping: for each planet, which houses it signifies */
    val planetSignifications: Map<Planet, Set<Int>>
)

// ============================================================================
// RULING PLANETS MODELS
// ============================================================================

/**
 * Ruling planets at a specific moment, used for event verification/confirmation.
 */
data class KPRulingPlanets(
    val dayLord: Planet,
    val moonSignLord: Planet,
    val moonStarLord: Planet,
    val moonSubLord: Planet,
    val ascSignLord: Planet,
    val ascStarLord: Planet,
    val ascSubLord: Planet,
    val calculationTime: String
) {
    /**
     * All unique ruling planets in order of significance.
     */
    val allRulingPlanets: List<Planet>
        get() = listOf(
            dayLord, moonSignLord, moonStarLord, moonSubLord,
            ascSignLord, ascStarLord, ascSubLord
        ).distinct()

    /**
     * Planets appearing more than once as rulers (strongest confirmation).
     */
    val repeatedRulers: List<Planet>
        get() {
            val all = listOf(
                dayLord, moonSignLord, moonStarLord, moonSubLord,
                ascSignLord, ascStarLord, ascSubLord
            )
            return all.groupBy { it }
                .filter { it.value.size > 1 }
                .keys.toList()
        }
}

// ============================================================================
// HORARY MODELS
// ============================================================================

/**
 * KP Horary chart data derived from a number (1-249).
 */
data class KPHoraryData(
    val horaryNumber: Int,
    val ascendantDegree: Double,
    val subLordEntry: KPSubLordEntry,
    val ascendantPosition: KPPosition
)

// ============================================================================
// COMPLETE ANALYSIS RESULT
// ============================================================================

/**
 * Complete KP analysis result containing all computed data.
 */
data class KPAnalysisResult(
    val cusps: List<KPCuspResult>,
    val planets: List<KPPlanetResult>,
    val significatorTable: KPSignificatorTable,
    val rulingPlanets: KPRulingPlanets,
    val ayanamsaValue: Double,
    val ayanamsaName: String
)
