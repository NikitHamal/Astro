package com.astro.storm.ephemeris.kp

import com.astro.storm.core.common.Language
import com.astro.storm.core.model.Nakshatra
import com.astro.storm.core.model.Planet
import com.astro.storm.core.model.ZodiacSign

/**
 * KP (Krishnamurti Padhdhati) System Models
 *
 * The KP system, developed by Prof. K.S. Krishnamurti, is a precise timing system
 * that uses sub-divisions of Nakshatras to pinpoint events. Key concepts:
 *
 * 1. CUSPAL SUB-LORDS: Each house cusp has a Sign Lord, Star Lord, and Sub Lord
 * 2. SIGNIFICATORS: Planets signify houses through ownership, occupation, and star lordship
 * 3. 4-STEP THEORY: Event verification through sub-lord analysis
 * 4. RULING PLANETS: Current moment planets used for verification
 *
 * The Sub division is based on Vimshottari Dasha proportions:
 * - Each Nakshatra (13°20') is divided into 9 subs
 * - Sub size is proportional to Dasha years
 * - Sub-Sub division provides even finer timing
 *
 * @author AstroStorm - Ultra-Precision Vedic Astrology
 */

/**
 * KP Sub division of a Nakshatra
 * Each Nakshatra is divided into 9 subs based on Vimshottari proportions
 */
data class KPSub(
    val nakshatra: Nakshatra,
    val subLord: Planet,
    val subNumber: Int,
    val startDegree: Double,
    val endDegree: Double,
    val spanDegrees: Double
) {
    val midPoint: Double get() = (startDegree + endDegree) / 2
}

/**
 * Complete KP position for a planet or cusp
 */
data class KPPosition(
    val longitude: Double,
    val sign: ZodiacSign,
    val signLord: Planet,
    val nakshatra: Nakshatra,
    val nakshatraPada: Int,
    val starLord: Planet,
    val subLord: Planet,
    val subSubLord: Planet? = null
) {
    /**
     * Get the complete significator chain
     */
    fun getSignificatorChain(): List<Planet> {
        return listOfNotNull(signLord, starLord, subLord, subSubLord).distinct()
    }

    /**
     * Get formatted position string
     */
    fun toFormattedString(): String {
        val deg = longitude.toInt() % 30
        val min = ((longitude % 1) * 60).toInt()
        return "${sign.displayName} ${deg}°${min}' (${starLord.displayName}-${subLord.displayName})"
    }
}

/**
 * House cusp with KP analysis
 */
data class KPCusp(
    val houseNumber: Int,
    val position: KPPosition,
    val significations: List<Int>,  // Houses signified by sub-lord
    val interpretation: String
) {
    val cuspDegree: Double get() = position.longitude
    val signLord: Planet get() = position.signLord
    val starLord: Planet get() = position.starLord
    val subLord: Planet get() = position.subLord
}

/**
 * Planet with KP significators
 */
data class KPPlanetPosition(
    val planet: Planet,
    val position: KPPosition,
    val occupiedHouse: Int,
    val ownedHouses: List<Int>,
    val starLordOf: List<Planet>,  // Planets in this planet's stars
    val significators: KPSignificators
)

/**
 * KP Significator analysis for a planet
 * A planet signifies houses in this order of strength:
 * 1. Houses occupied by planets in its stars
 * 2. Houses owned by planets in its stars
 * 3. House it occupies
 * 4. Houses it owns
 */
data class KPSignificators(
    val planet: Planet,
    val primarySignifications: List<Int>,     // Houses occupied by planets in its stars
    val secondarySignifications: List<Int>,   // Houses owned by planets in its stars
    val tertiarySignifications: List<Int>,    // House occupied
    val quaternarySignifications: List<Int>,  // Houses owned
    val allSignifications: Set<Int>
) {
    fun getStrongestSignifications(): List<Int> {
        return (primarySignifications + secondarySignifications).distinct()
    }

    fun signifiesHouse(house: Int): Boolean = house in allSignifications

    fun getSignificationStrength(house: Int): SignificationStrength {
        return when {
            house in primarySignifications -> SignificationStrength.STRONGEST
            house in secondarySignifications -> SignificationStrength.STRONG
            house in tertiarySignifications -> SignificationStrength.MODERATE
            house in quaternarySignifications -> SignificationStrength.WEAK
            else -> SignificationStrength.NONE
        }
    }
}

/**
 * Signification strength levels
 */
enum class SignificationStrength(val weight: Double) {
    STRONGEST(1.0),   // Planets in star - their occupied houses
    STRONG(0.75),     // Planets in star - their owned houses
    MODERATE(0.5),    // Self occupation
    WEAK(0.25),       // Self ownership
    NONE(0.0)
}

/**
 * KP 4-Step Theory verification result
 */
data class FourStepTheoryResult(
    val query: String,
    val houseGroup: HouseGroup,
    val relevantHouses: List<Int>,
    val step1Result: Step1Result,
    val step2Result: Step2Result,
    val step3Result: Step3Result,
    val step4Result: Step4Result,
    val overallVerdict: KPVerdict,
    val confidence: Double,
    val explanation: String
)

/**
 * Step 1: Cusp Sub-Lord analysis
 */
data class Step1Result(
    val primaryCusp: Int,
    val subLord: Planet,
    val subLordSignifications: KPSignificators,
    val supportsEvent: Boolean,
    val explanation: String
)

/**
 * Step 2: Sub-Lord's Star-Lord analysis
 */
data class Step2Result(
    val starLord: Planet,
    val starLordSignifications: KPSignificators,
    val supportsEvent: Boolean,
    val explanation: String
)

/**
 * Step 3: Dasha analysis
 */
data class Step3Result(
    val currentDashaLord: Planet,
    val currentBhuktiLord: Planet,
    val currentAntarLord: Planet?,
    val dashaSignifications: Map<Planet, KPSignificators>,
    val supportsEvent: Boolean,
    val explanation: String
)

/**
 * Step 4: Transit analysis
 */
data class Step4Result(
    val significantTransits: List<KPTransit>,
    val supportsEvent: Boolean,
    val explanation: String
)

/**
 * KP Transit information
 */
data class KPTransit(
    val planet: Planet,
    val position: KPPosition,
    val significations: KPSignificators,
    val isRelevant: Boolean
)

/**
 * KP Verdict for event
 */
enum class KPVerdict(val displayName: String) {
    STRONGLY_POSITIVE("Strongly Favorable"),
    POSITIVE("Favorable"),
    NEUTRAL("Neutral/Mixed"),
    NEGATIVE("Unfavorable"),
    STRONGLY_NEGATIVE("Strongly Unfavorable")
}

/**
 * House groups for different life events
 */
enum class HouseGroup(
    val displayName: String,
    val favorableHouses: List<Int>,
    val unfavorableHouses: List<Int>,
    val neutralHouses: List<Int>
) {
    MARRIAGE(
        "Marriage/Partnership",
        listOf(2, 7, 11),
        listOf(1, 6, 10, 12),
        listOf(3, 4, 5, 8, 9)
    ),
    CAREER_JOB(
        "Career/Job",
        listOf(2, 6, 10, 11),
        listOf(1, 5, 9, 12),
        listOf(3, 4, 7, 8)
    ),
    BUSINESS(
        "Business/Self-Employment",
        listOf(2, 7, 10, 11),
        listOf(6, 8, 12),
        listOf(1, 3, 4, 5, 9)
    ),
    EDUCATION(
        "Education/Learning",
        listOf(4, 9, 11),
        listOf(6, 8, 12),
        listOf(1, 2, 3, 5, 7, 10)
    ),
    HEALTH(
        "Health/Recovery",
        listOf(1, 5, 11),
        listOf(6, 8, 12),
        listOf(2, 3, 4, 7, 9, 10)
    ),
    FOREIGN_TRAVEL(
        "Foreign Travel/Settlement",
        listOf(3, 9, 12),
        listOf(4, 8),
        listOf(1, 2, 5, 6, 7, 10, 11)
    ),
    PROPERTY(
        "Property/Land/Vehicle",
        listOf(4, 11, 12),
        listOf(3, 6, 8),
        listOf(1, 2, 5, 7, 9, 10)
    ),
    CHILDREN(
        "Children/Progeny",
        listOf(2, 5, 11),
        listOf(1, 4, 10, 12),
        listOf(3, 6, 7, 8, 9)
    ),
    WEALTH(
        "Wealth/Finance",
        listOf(2, 6, 10, 11),
        listOf(5, 8, 12),
        listOf(1, 3, 4, 7, 9)
    ),
    LITIGATION(
        "Litigation/Legal Matters",
        listOf(6, 11),
        listOf(7, 12),
        listOf(1, 2, 3, 4, 5, 8, 9, 10)
    ),
    SPIRITUAL(
        "Spirituality/Moksha",
        listOf(5, 9, 12),
        listOf(2, 6, 10, 11),
        listOf(1, 3, 4, 7, 8)
    )
}

/**
 * Ruling Planets at a specific moment
 */
data class RulingPlanets(
    val ascendantSignLord: Planet,
    val ascendantStarLord: Planet,
    val moonSignLord: Planet,
    val moonStarLord: Planet,
    val dayLord: Planet,
    val allRulingPlanets: Set<Planet>
) {
    /**
     * Check if a planet is a ruling planet
     */
    fun isRulingPlanet(planet: Planet): Boolean = planet in allRulingPlanets

    /**
     * Get strength of ruling planet (how many times it appears)
     */
    fun getRulingStrength(planet: Planet): Int {
        var count = 0
        if (planet == ascendantSignLord) count++
        if (planet == ascendantStarLord) count++
        if (planet == moonSignLord) count++
        if (planet == moonStarLord) count++
        if (planet == dayLord) count++
        return count
    }
}

/**
 * Complete KP Analysis result
 */
data class KPAnalysisResult(
    val cusps: List<KPCusp>,
    val planets: List<KPPlanetPosition>,
    val rulingPlanets: RulingPlanets,
    val significatorTable: Map<Int, List<Planet>>,  // House -> Significators
    val fourStepResults: List<FourStepTheoryResult>,
    val subLordNavigator: SubLordNavigator,
    val language: Language = Language.ENGLISH
) {
    /**
     * Get all significators for a house
     */
    fun getHouseSignificators(house: Int): List<Planet> {
        return significatorTable[house] ?: emptyList()
    }

    /**
     * Find best significators for an event
     */
    fun findBestSignificators(houseGroup: HouseGroup): List<Planet> {
        val relevant = houseGroup.favorableHouses.flatMap { getHouseSignificators(it) }
        val negative = houseGroup.unfavorableHouses.flatMap { getHouseSignificators(it) }

        // Planets that signify favorable houses but not unfavorable
        return relevant.filter { it !in negative }.distinct()
    }
}

/**
 * Sub-Lord Navigator for timing
 */
data class SubLordNavigator(
    val allSubs: List<KPSub>,
    val planetSubPositions: Map<Planet, KPSub>,
    val cuspSubPositions: Map<Int, KPSub>
) {
    /**
     * Find when a planet/cusp will change sub-lord
     */
    fun getNextSubChange(currentDegree: Double): KPSub? {
        return allSubs.find { it.startDegree > currentDegree }
    }

    /**
     * Find sub at a specific degree
     */
    fun getSubAtDegree(degree: Double): KPSub? {
        val normalizedDegree = (degree % 360 + 360) % 360
        return allSubs.find { normalizedDegree >= it.startDegree && normalizedDegree < it.endDegree }
    }
}

/**
 * KP Timing prediction
 */
data class KPTimingPrediction(
    val event: String,
    val houseGroup: HouseGroup,
    val favorablePeriods: List<KPTimingWindow>,
    val bestPeriod: KPTimingWindow?,
    val explanation: String
)

/**
 * Timing window for KP prediction
 */
data class KPTimingWindow(
    val startDate: java.time.LocalDate,
    val endDate: java.time.LocalDate,
    val dashaLord: Planet,
    val bhuktiLord: Planet,
    val antarLord: Planet?,
    val strength: Double,
    val reason: String
)

/**
 * KP Number (for Horary/Prashna)
 * Numbers 1-249 map to specific Nakshatra-Pada-Sub combinations
 */
data class KPNumber(
    val number: Int,  // 1-249
    val sign: ZodiacSign,
    val nakshatra: Nakshatra,
    val pada: Int,
    val subLord: Planet,
    val degreesInSign: Double
) {
    companion object {
        const val MAX_NUMBER = 249
    }
}

/**
 * Vimshottari Dasha years for sub calculation
 */
object VimshottariYears {
    val DASHA_YEARS = mapOf(
        Planet.KETU to 7.0,
        Planet.VENUS to 20.0,
        Planet.SUN to 6.0,
        Planet.MOON to 10.0,
        Planet.MARS to 7.0,
        Planet.RAHU to 18.0,
        Planet.JUPITER to 16.0,
        Planet.SATURN to 19.0,
        Planet.MERCURY to 17.0
    )

    val TOTAL_YEARS = 120.0

    val NAKSHATRA_SEQUENCE = listOf(
        Planet.KETU, Planet.VENUS, Planet.SUN, Planet.MOON, Planet.MARS,
        Planet.RAHU, Planet.JUPITER, Planet.SATURN, Planet.MERCURY
    )

    /**
     * Get the sequence starting from a nakshatra lord
     */
    fun getSequenceFrom(startLord: Planet): List<Planet> {
        val startIndex = NAKSHATRA_SEQUENCE.indexOf(startLord)
        if (startIndex == -1) return NAKSHATRA_SEQUENCE

        return NAKSHATRA_SEQUENCE.drop(startIndex) + NAKSHATRA_SEQUENCE.take(startIndex)
    }

    /**
     * Calculate sub span in degrees within a nakshatra
     */
    fun calculateSubSpan(planet: Planet, nakshatraSpan: Double): Double {
        val years = DASHA_YEARS[planet] ?: 0.0
        return (years / TOTAL_YEARS) * nakshatraSpan
    }
}
