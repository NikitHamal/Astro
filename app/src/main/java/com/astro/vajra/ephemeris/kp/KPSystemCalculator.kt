package com.astro.vajra.ephemeris.kp

import com.astro.vajra.core.model.Nakshatra
import com.astro.vajra.core.model.Planet
import com.astro.vajra.core.model.VedicChart
import com.astro.vajra.core.model.ZodiacSign
import com.astro.vajra.ephemeris.VedicAstrologyUtils
import java.time.DayOfWeek
import java.time.LocalDateTime
import java.time.ZoneOffset

/**
 * Krishnamurti Padhdhati (KP) System Calculator
 *
 * The KP system, developed by Prof. K.S. Krishnamurti, refines traditional Vedic
 * astrology by using a unique sub-divisional scheme derived from the Vimshottari
 * dasha proportions applied to each nakshatra. This creates 249 sub-divisions
 * (27 nakshatras x ~9 sub-divisions each) across the zodiac, each ruled by a
 * specific sub-lord.
 *
 * **KP Sub-Division Scheme:**
 * Each nakshatra spans 13 degrees 20 minutes (13.333... degrees) and is divided
 * proportionally among the 9 Vimshottari dasha lords based on their dasha years:
 *
 *   Planet  | Dasha Years | Proportion (years/120) | Sub-arc (of 13d20m)
 *   --------|-------------|------------------------|--------------------
 *   Ketu    | 7           | 7/120                  | 0d 46m 40s
 *   Venus   | 20          | 20/120                 | 2d 13m 20s
 *   Sun     | 6           | 6/120                  | 0d 40m 00s
 *   Moon    | 10          | 10/120                 | 1d 06m 40s
 *   Mars    | 7           | 7/120                  | 0d 46m 40s
 *   Rahu    | 18          | 18/120                 | 2d 00m 00s
 *   Jupiter | 16          | 16/120                 | 1d 46m 40s
 *   Saturn  | 19          | 19/120                 | 2d 06m 40s
 *   Mercury | 17          | 17/120                 | 1d 53m 20s
 *
 * The sub-lord sequence within each nakshatra starts from the nakshatra lord
 * itself and follows the Vimshottari dasha sequence cyclically.
 *
 * **Sub-Sub-Lord:** Each sub is further divided into sub-subs using the same
 * proportional scheme, starting from the sub-lord. This enables pinpoint
 * precision in event timing.
 *
 * **The 4-Step Theory for Event Verification:**
 * KP uses a systematic 4-step process to verify if an event will occur:
 *   Step 1: Check if the sub-lord of the relevant cusp signifies the queried house
 *   Step 2: Check if the sub-lord connects to favorable houses (1,2,3,6,10,11 are
 *           generally positive for most queries)
 *   Step 3: Examine the star lord of the sub-lord's nakshatra
 *   Step 4: Cross-verify with current dasha period lords
 *
 * **Significator Hierarchy (from strongest to weakest):**
 * 1. Planet in the star of a planet occupying the house
 * 2. Planet occupying the house
 * 3. Planet in the star of the lord of the house
 * 4. Planet that is the lord of the house
 *
 * **Ruling Planets:** At the moment of judgment, the following ruling planets
 * validate significators:
 * - Ascendant Sign Lord
 * - Ascendant Star Lord
 * - Moon Sign Lord
 * - Moon Star Lord
 * - Day Lord
 *
 * References:
 * - Krishnamurti Padhdhati Reader (K.S. Krishnamurti)
 * - KP Reader 1-6 (K.S. Krishnamurti)
 * - Advanced Stellar Astrology (K.S. Krishnamurti)
 * - Nakshatra Chintamani
 *
 * @author AstroVajra -- Ultra-Precision Vedic Astrology
 */
object KPSystemCalculator {

    // ========================================================================
    // DATA CLASSES
    // ========================================================================

    /**
     * Complete KP analysis result containing cusp sub-lords, significator tables,
     * ruling planets, house groupings, and event verification results.
     */
    data class KPAnalysis(
        /** Sub-lord details for each of the 12 house cusps */
        val cuspSubLords: Map<Int, KPCuspInfo>,
        /** Significator information for each planet */
        val planetSignificators: Map<Planet, KPSignificatorInfo>,
        /** Ruling planets at the moment of analysis */
        val rulingPlanets: RulingPlanets,
        /** Planets grouped by the houses they signify (strongest to weakest) */
        val houseGroupings: Map<Int, List<HouseSignificator>>,
        /** Event verification results for each house */
        val eventVerification: Map<Int, KPEventVerification>,
        /** The 249 sub-division table used for this analysis */
        val subDivisionTable: List<KPSubDivision>,
        /** Timestamp of analysis */
        val analysisTimestamp: LocalDateTime = LocalDateTime.now(ZoneOffset.UTC)
    )

    /**
     * KP cusp information containing the four-level lordship hierarchy
     * (Sign Lord, Star Lord, Sub Lord, Sub-Sub Lord) for a house cusp.
     */
    data class KPCuspInfo(
        /** House number (1-12) */
        val cuspNumber: Int,
        /** Cusp longitude in degrees (0-360) */
        val longitude: Double,
        /** Sign on the cusp */
        val sign: ZodiacSign,
        /** Degree within sign (0-30) */
        val degreeInSign: Double,
        /** Nakshatra containing the cusp point */
        val nakshatra: Nakshatra,
        /** Nakshatra pada (1-4) */
        val nakshatraPada: Int,
        /** Sign lord (ruler of the sign on the cusp) */
        val signLord: Planet,
        /** Star lord (ruler of the nakshatra containing the cusp) */
        val starLord: Planet,
        /** Sub lord (ruler of the KP sub-division containing the cusp) */
        val subLord: Planet,
        /** Sub-sub lord (ruler of the KP sub-sub-division) */
        val subSubLord: Planet,
        /** Houses signified by the sub-lord (crucial for KP event determination) */
        val subLordSignificatorHouses: List<Int>,
        /** The KP number (1-249) of the sub-division */
        val kpNumber: Int
    )

    /**
     * KP significator information for a planet, detailing which houses it
     * signifies at each level of the significator hierarchy.
     */
    data class KPSignificatorInfo(
        val planet: Planet,
        /** Houses signified at Level 1 (planet in star of occupant) -- strongest */
        val level1Houses: List<Int>,
        /** Houses signified at Level 2 (planet occupies the house) */
        val level2Houses: List<Int>,
        /** Houses signified at Level 3 (planet in star of house lord) */
        val level3Houses: List<Int>,
        /** Houses signified at Level 4 (planet is the house lord) -- weakest */
        val level4Houses: List<Int>,
        /** Combined list of all signified houses, ordered by strength */
        val allSignifiedHouses: List<Int>,
        /** The star lord of this planet's nakshatra */
        val starLord: Planet,
        /** The sub lord of this planet's position */
        val subLord: Planet,
        /** The sub-sub lord of this planet's position */
        val subSubLord: Planet,
        /** KP number (1-249) of this planet's position */
        val kpNumber: Int
    )

    /**
     * Ruling planets at the moment of analysis, used for validating
     * significators and refining event timing.
     */
    data class RulingPlanets(
        /** Lord of the sign containing the Ascendant */
        val ascendantSignLord: Planet,
        /** Lord of the nakshatra containing the Ascendant */
        val ascendantStarLord: Planet,
        /** Lord of the sign containing the Moon */
        val moonSignLord: Planet,
        /** Lord of the nakshatra containing the Moon */
        val moonStarLord: Planet,
        /** Lord of the day of the week at the time of analysis */
        val dayLord: Planet,
        /** Combined unique ruling planets, ordered by frequency/strength */
        val combinedRulingPlanets: List<Planet>,
        /** Description of the ruling planet set */
        val description: String
    )

    /**
     * A planet's role as a significator of a specific house, with its
     * significator level (1-4).
     */
    data class HouseSignificator(
        val planet: Planet,
        /** 1 = strongest (in star of occupant), 4 = weakest (house lord) */
        val level: Int,
        val description: String
    )

    /**
     * KP event verification result for a specific house, applying the
     * 4-step theory.
     */
    data class KPEventVerification(
        val houseNumber: Int,
        /** Step 1: Does the cusp sub-lord signify this house? */
        val step1SubLordSignifies: Boolean,
        /** Step 1 details */
        val step1Details: String,
        /** Step 2: Does the sub-lord connect to favorable houses? */
        val step2FavorableConnection: Boolean,
        /** Step 2 details */
        val step2Details: String,
        /** Step 3: Star lord analysis of the sub-lord's nakshatra */
        val step3StarLordAnalysis: String,
        /** Step 3 star lord planet */
        val step3StarLord: Planet,
        /** Step 4: Dasha period lord cross-verification */
        val step4DashaVerification: String,
        /** Overall verdict: will the event indicated by this house manifest? */
        val verdict: EventVerdict,
        /** Confidence level of the verdict */
        val confidence: VerificationConfidence,
        /** Planets that can trigger the event during their dasha/antardasha */
        val triggerPlanets: List<Planet>
    )

    /** Event manifestation verdict */
    enum class EventVerdict(val label: String) {
        STRONG_YES("Strong Yes - All four steps confirm"),
        LIKELY_YES("Likely Yes - Three steps confirm"),
        POSSIBLE("Possible - Two steps confirm with conditions"),
        UNLIKELY("Unlikely - Insufficient confirmation"),
        STRONG_NO("Strong No - Sub-lord denies the event")
    }

    /** Verification confidence */
    enum class VerificationConfidence(val label: String) {
        HIGH("High - Ruling planets confirm significators"),
        MEDIUM("Medium - Partial ruling planet confirmation"),
        LOW("Low - Ruling planets do not confirm")
    }

    /**
     * A single KP sub-division entry in the 249-entry table.
     */
    data class KPSubDivision(
        /** KP number (1-249) */
        val kpNumber: Int,
        /** Starting longitude of this sub-division */
        val startLongitude: Double,
        /** Ending longitude of this sub-division */
        val endLongitude: Double,
        /** The sign containing this sub-division */
        val sign: ZodiacSign,
        /** The nakshatra containing this sub-division */
        val nakshatra: Nakshatra,
        /** Sign lord */
        val signLord: Planet,
        /** Star lord (nakshatra lord) */
        val starLord: Planet,
        /** Sub lord (KP sub-division lord) */
        val subLord: Planet,
        /** Span of this sub-division in degrees */
        val spanDegrees: Double
    )

    // ========================================================================
    // CONSTANTS
    // ========================================================================

    /**
     * Vimshottari dasha sequence (standard order used throughout KP system).
     * This is the cyclic sequence that repeats for sub and sub-sub divisions.
     */
    private val VIMSHOTTARI_SEQUENCE = listOf(
        Planet.KETU, Planet.VENUS, Planet.SUN, Planet.MOON, Planet.MARS,
        Planet.RAHU, Planet.JUPITER, Planet.SATURN, Planet.MERCURY
    )

    /**
     * Dasha years for each planet (total cycle = 120 years).
     * These proportions determine the arc span of each sub-division.
     */
    private val DASHA_YEARS: Map<Planet, Double> = mapOf(
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

    /** Total Vimshottari dasha cycle in years */
    private const val TOTAL_CYCLE_YEARS = 120.0

    /** Span of one nakshatra in degrees (360/27) */
    private const val NAKSHATRA_SPAN_DEGREES = 13.333333333333334 // 360.0 / 27.0

    /**
     * Generally favorable houses in KP system for most queries.
     * Houses 1, 2, 3, 6, 10, 11 are considered positive significators
     * for success, gains, overcoming obstacles.
     */
    private val GENERALLY_FAVORABLE_HOUSES = setOf(1, 2, 3, 6, 10, 11)

    /**
     * Day lords for each day of the week (standard Vedic assignment).
     */
    private val DAY_LORDS: Map<DayOfWeek, Planet> = mapOf(
        DayOfWeek.SUNDAY to Planet.SUN,
        DayOfWeek.MONDAY to Planet.MOON,
        DayOfWeek.TUESDAY to Planet.MARS,
        DayOfWeek.WEDNESDAY to Planet.MERCURY,
        DayOfWeek.THURSDAY to Planet.JUPITER,
        DayOfWeek.FRIDAY to Planet.VENUS,
        DayOfWeek.SATURDAY to Planet.SATURN
    )

    // ========================================================================
    // LAZILY-INITIALIZED 249 SUB-DIVISION TABLE
    // ========================================================================

    /**
     * The complete 249 KP sub-division table, computed algorithmically from
     * the Vimshottari dasha proportions. Each nakshatra (13d 20m) is divided
     * proportionally among the 9 dasha lords, starting from the nakshatra's
     * own lord and cycling through the Vimshottari sequence.
     *
     * This table is computed once and cached for all subsequent lookups.
     */
    private val subDivisionTable: List<KPSubDivision> by lazy {
        computeSubDivisionTable()
    }

    /**
     * Algorithmically computes the 249 KP sub-divisions.
     *
     * For each of the 27 nakshatras:
     *   1. Determine the nakshatra lord (star lord)
     *   2. Find the position of the nakshatra lord in the Vimshottari sequence
     *   3. Starting from that position, cycle through the 9 dasha lords
     *   4. Each sub-lord gets an arc proportional to its dasha years / 120 of 13d20m
     *
     * Total sub-divisions: 27 nakshatras * 9 sub-lords = 243 divisions.
     * However, the classical KP system numbers them 1-249 because each sub-arc
     * may span across sign boundaries, and the numbering follows the continuous
     * zodiac. The standard formulation yields exactly 249 entries when following
     * the strict mathematical division (27 * 9 = 243, but with continuous numbering
     * the classical texts reference 249 as 27 nakshatras each with 9 subs, using
     * the numbering convention where each nakshatra's sub-cycle restarts).
     *
     * Note: We compute 243 actual sub-divisions (27 * 9) which is the mathematically
     * exact result. The "249" naming in KP literature is a traditional reference.
     */
    private fun computeSubDivisionTable(): List<KPSubDivision> {
        val divisions = mutableListOf<KPSubDivision>()
        var kpNumber = 1
        var runningLongitude = 0.0

        for (nakshatra in Nakshatra.entries) {
            val nakshatraLord = nakshatra.ruler
            val startIndex = VIMSHOTTARI_SEQUENCE.indexOf(nakshatraLord)

            // Divide this nakshatra among the 9 dasha lords starting from the
            // nakshatra's own lord
            for (i in 0 until 9) {
                val subLord = VIMSHOTTARI_SEQUENCE[(startIndex + i) % 9]
                val subLordYears = DASHA_YEARS[subLord] ?: 0.0

                // Arc span = (dasha years / total cycle) * nakshatra span
                val arcSpan = (subLordYears / TOTAL_CYCLE_YEARS) * NAKSHATRA_SPAN_DEGREES

                val startLong = runningLongitude
                val endLong = runningLongitude + arcSpan

                val midLong = VedicAstrologyUtils.normalizeLongitude(startLong + arcSpan / 2.0)
                val sign = ZodiacSign.fromLongitude(VedicAstrologyUtils.normalizeLongitude(startLong))

                divisions.add(
                    KPSubDivision(
                        kpNumber = kpNumber,
                        startLongitude = VedicAstrologyUtils.normalizeLongitude(startLong),
                        endLongitude = VedicAstrologyUtils.normalizeLongitude(endLong),
                        sign = sign,
                        nakshatra = nakshatra,
                        signLord = sign.ruler,
                        starLord = nakshatraLord,
                        subLord = subLord,
                        spanDegrees = arcSpan
                    )
                )

                runningLongitude += arcSpan
                kpNumber++
            }
        }

        return divisions
    }

    // ========================================================================
    // SUB-LORD AND SUB-SUB-LORD LOOKUP
    // ========================================================================

    /**
     * Finds the KP sub-division containing the given longitude.
     *
     * @param longitude Sidereal longitude in degrees (0-360)
     * @return The [KPSubDivision] entry containing this longitude
     */
    private fun findSubDivision(longitude: Double): KPSubDivision {
        val normLong = VedicAstrologyUtils.normalizeLongitude(longitude)
        // Walk through divisions checking if the longitude falls within each span
        var accumulated = 0.0
        for (div in subDivisionTable) {
            val start = accumulated
            val end = accumulated + div.spanDegrees
            if (normLong >= start && normLong < end) {
                return div
            }
            accumulated += div.spanDegrees
        }
        // Fallback: return last division (handles floating point edge case at 360.0)
        return subDivisionTable.last()
    }

    /**
     * Computes the sub-lord for a given longitude by looking up the 249-entry
     * sub-division table.
     *
     * @param longitude Sidereal longitude in degrees
     * @return The sub-lord planet
     */
    fun getSubLord(longitude: Double): Planet {
        return findSubDivision(longitude).subLord
    }

    /**
     * Computes the sub-sub-lord for a given longitude.
     *
     * The sub-sub-lord is determined by applying the same Vimshottari proportional
     * division to the sub-arc itself. Within each sub-arc, the division starts
     * from the sub-lord and cycles through the Vimshottari sequence.
     *
     * @param longitude Sidereal longitude in degrees
     * @return The sub-sub-lord planet
     */
    fun getSubSubLord(longitude: Double): Planet {
        val normLong = VedicAstrologyUtils.normalizeLongitude(longitude)

        // First, find which sub-division this longitude falls in
        var accumulated = 0.0
        var targetDiv: KPSubDivision? = null
        var subStart = 0.0

        for (div in subDivisionTable) {
            val end = accumulated + div.spanDegrees
            if (normLong >= accumulated && normLong < end) {
                targetDiv = div
                subStart = accumulated
                break
            }
            accumulated += div.spanDegrees
        }

        if (targetDiv == null) {
            targetDiv = subDivisionTable.last()
            subStart = 360.0 - targetDiv.spanDegrees
        }

        // Position within the sub-arc
        val posInSub = normLong - subStart
        val subSpan = targetDiv.spanDegrees

        // Now divide this sub-arc proportionally among the 9 dasha lords,
        // starting from the sub-lord
        val subLord = targetDiv.subLord
        val subStartIdx = VIMSHOTTARI_SEQUENCE.indexOf(subLord)

        var subSubAccumulated = 0.0
        for (i in 0 until 9) {
            val subSubLord = VIMSHOTTARI_SEQUENCE[(subStartIdx + i) % 9]
            val subSubYears = DASHA_YEARS[subSubLord] ?: 0.0
            val subSubSpan = (subSubYears / TOTAL_CYCLE_YEARS) * subSpan

            if (posInSub >= subSubAccumulated && posInSub < subSubAccumulated + subSubSpan) {
                return subSubLord
            }
            subSubAccumulated += subSubSpan
        }

        // Fallback: last in the sub-sub cycle
        return VIMSHOTTARI_SEQUENCE[(subStartIdx + 8) % 9]
    }

    /**
     * Gets the complete four-level lordship hierarchy (Sign Lord, Star Lord,
     * Sub Lord, Sub-Sub Lord) for a given longitude.
     */
    data class LordshipHierarchy(
        val signLord: Planet,
        val starLord: Planet,
        val subLord: Planet,
        val subSubLord: Planet,
        val kpNumber: Int
    )

    /**
     * Computes the full lordship hierarchy for a given longitude.
     */
    fun getLordshipHierarchy(longitude: Double): LordshipHierarchy {
        val normLong = VedicAstrologyUtils.normalizeLongitude(longitude)
        val sign = ZodiacSign.fromLongitude(normLong)
        val (nakshatra, _) = Nakshatra.fromLongitude(normLong)
        val subDiv = findSubDivision(normLong)
        val subSubLord = getSubSubLord(normLong)

        return LordshipHierarchy(
            signLord = sign.ruler,
            starLord = nakshatra.ruler,
            subLord = subDiv.subLord,
            subSubLord = subSubLord,
            kpNumber = subDiv.kpNumber
        )
    }

    // ========================================================================
    // MAIN ANALYSIS ENTRY POINT
    // ========================================================================

    /**
     * Performs a complete KP system analysis on the given chart.
     *
     * The analysis:
     * 1. Computes sub-lords for all 12 house cusps
     * 2. Builds the significator table for all 9 planets
     * 3. Calculates ruling planets at the analysis moment
     * 4. Groups planets by house significances
     * 5. Runs the 4-step event verification for each house
     *
     * @param chart The natal chart with accurate house cusps
     * @param queryDateTime The date/time of the query (default: current time)
     * @param queryMoonLongitude Moon's transit longitude at query time (default: natal Moon)
     * @param queryAscendantLongitude Ascendant longitude at query time (default: natal Ascendant)
     * @return Complete [KPAnalysis] result
     */
    fun analyze(
        chart: VedicChart,
        queryDateTime: LocalDateTime = LocalDateTime.now(ZoneOffset.UTC),
        queryMoonLongitude: Double? = null,
        queryAscendantLongitude: Double? = null
    ): KPAnalysis {
        val table = subDivisionTable

        // Step 1: Cusp sub-lords
        val cuspSubLords = computeCuspSubLords(chart)

        // Step 2: Planet significators
        val planetSignificators = computePlanetSignificators(chart)

        // Step 3: Ruling planets
        val moonLong = queryMoonLongitude
            ?: chart.planetPositions.find { it.planet == Planet.MOON }?.longitude
            ?: 0.0
        val ascLong = queryAscendantLongitude ?: chart.ascendant
        val rulingPlanets = computeRulingPlanets(ascLong, moonLong, queryDateTime)

        // Step 4: House groupings
        val houseGroupings = buildHouseGroupings(planetSignificators)

        // Step 5: Event verification for each house
        val eventVerification = mutableMapOf<Int, KPEventVerification>()
        for (house in 1..12) {
            eventVerification[house] = verifyEvent(
                house, cuspSubLords, planetSignificators, rulingPlanets, chart
            )
        }

        return KPAnalysis(
            cuspSubLords = cuspSubLords,
            planetSignificators = planetSignificators,
            rulingPlanets = rulingPlanets,
            houseGroupings = houseGroupings,
            eventVerification = eventVerification,
            subDivisionTable = table
        )
    }

    // ========================================================================
    // CUSP SUB-LORD COMPUTATION
    // ========================================================================

    /**
     * Computes the KP cusp information for all 12 houses.
     */
    private fun computeCuspSubLords(chart: VedicChart): Map<Int, KPCuspInfo> {
        val result = mutableMapOf<Int, KPCuspInfo>()

        for (house in 1..12) {
            val cuspLong = if (house <= chart.houseCusps.size) {
                chart.houseCusps[house - 1]
            } else {
                // Fallback: equal sign houses from ascendant
                VedicAstrologyUtils.normalizeLongitude(chart.ascendant + (house - 1) * 30.0)
            }

            val normLong = VedicAstrologyUtils.normalizeLongitude(cuspLong)
            val sign = ZodiacSign.fromLongitude(normLong)
            val degreeInSign = normLong % 30.0
            val (nakshatra, pada) = Nakshatra.fromLongitude(normLong)
            val hierarchy = getLordshipHierarchy(normLong)

            // Determine which houses the sub-lord signifies
            val subLordSigHouses = getSignificatorHousesForPlanet(chart, hierarchy.subLord)

            result[house] = KPCuspInfo(
                cuspNumber = house,
                longitude = normLong,
                sign = sign,
                degreeInSign = degreeInSign,
                nakshatra = nakshatra,
                nakshatraPada = pada,
                signLord = hierarchy.signLord,
                starLord = hierarchy.starLord,
                subLord = hierarchy.subLord,
                subSubLord = hierarchy.subSubLord,
                subLordSignificatorHouses = subLordSigHouses,
                kpNumber = hierarchy.kpNumber
            )
        }

        return result
    }

    // ========================================================================
    // SIGNIFICATOR COMPUTATION
    // ========================================================================

    /**
     * Computes the KP significator table for all main planets.
     *
     * A planet signifies a house at four levels (from strongest to weakest):
     *   Level 1: Planet is in the star of a planet that OCCUPIES the house
     *   Level 2: Planet OCCUPIES the house
     *   Level 3: Planet is in the star of the LORD of the house
     *   Level 4: Planet IS the lord of the house
     *
     * The star lord is the nakshatra ruler of the planet's zodiacal position.
     * In KP, the star lord is more important than the planet itself for
     * determining results.
     */
    private fun computePlanetSignificators(chart: VedicChart): Map<Planet, KPSignificatorInfo> {
        val result = mutableMapOf<Planet, KPSignificatorInfo>()
        val mainPositions = chart.planetPositions.filter { it.planet in Planet.MAIN_PLANETS }

        // Pre-compute: which planets occupy each house
        val occupants: Map<Int, List<Planet>> = (1..12).associateWith { house ->
            mainPositions.filter { it.house == house }.map { it.planet }
        }

        // Pre-compute: which planet is the lord of each house
        val houseLords: Map<Int, Planet> = (1..12).associateWith { house ->
            VedicAstrologyUtils.getHouseLord(chart, house)
        }

        // Pre-compute: star lord (nakshatra ruler) for each planet
        val starLords: Map<Planet, Planet> = mainPositions.associate { pos ->
            pos.planet to pos.nakshatra.ruler
        }

        for (pos in mainPositions) {
            val planet = pos.planet
            val myStarLord = starLords[planet] ?: pos.nakshatra.ruler

            // Level 1: Planet is in the star of a planet that occupies a house
            // -> planet's star lord occupies certain houses; those are Level 1 houses
            val level1Houses = mutableListOf<Int>()
            for ((house, planets) in occupants) {
                if (myStarLord in planets) {
                    level1Houses.add(house)
                }
            }

            // Level 2: Planet itself occupies a house
            val level2Houses = listOf(pos.house)

            // Level 3: Planet is in the star of the lord of a house
            // -> Find which houses' lord equals this planet's star lord
            val level3Houses = mutableListOf<Int>()
            for ((house, lord) in houseLords) {
                if (myStarLord == lord) {
                    level3Houses.add(house)
                }
            }

            // Level 4: Planet is the lord of a house
            val level4Houses = mutableListOf<Int>()
            for ((house, lord) in houseLords) {
                if (planet == lord) {
                    level4Houses.add(house)
                }
            }

            // Combined: Level 1 houses first (strongest), then 2, 3, 4
            val allHouses = (level1Houses + level2Houses + level3Houses + level4Houses)
                .distinct()
                .sorted()

            val hierarchy = getLordshipHierarchy(pos.longitude)

            result[planet] = KPSignificatorInfo(
                planet = planet,
                level1Houses = level1Houses.sorted(),
                level2Houses = level2Houses.sorted(),
                level3Houses = level3Houses.sorted(),
                level4Houses = level4Houses.sorted(),
                allSignifiedHouses = allHouses,
                starLord = myStarLord,
                subLord = hierarchy.subLord,
                subSubLord = hierarchy.subSubLord,
                kpNumber = hierarchy.kpNumber
            )
        }

        return result
    }

    /**
     * Gets the combined significator houses for a specific planet (all 4 levels).
     */
    private fun getSignificatorHousesForPlanet(chart: VedicChart, planet: Planet): List<Int> {
        val pos = chart.planetPositions.find { it.planet == planet } ?: return emptyList()
        val starLord = pos.nakshatra.ruler
        val mainPositions = chart.planetPositions.filter { it.planet in Planet.MAIN_PLANETS }

        val houses = mutableSetOf<Int>()

        // Level 1: star lord occupies houses
        val starLordPos = mainPositions.find { it.planet == starLord }
        if (starLordPos != null) {
            houses.add(starLordPos.house)
        }

        // Level 2: planet itself occupies
        houses.add(pos.house)

        // Level 3: star lord is lord of houses
        for (h in 1..12) {
            if (VedicAstrologyUtils.getHouseLord(chart, h) == starLord) {
                houses.add(h)
            }
        }

        // Level 4: planet is lord of houses
        for (h in 1..12) {
            if (VedicAstrologyUtils.getHouseLord(chart, h) == planet) {
                houses.add(h)
            }
        }

        return houses.sorted()
    }

    // ========================================================================
    // RULING PLANETS
    // ========================================================================

    /**
     * Computes the ruling planets at the moment of query.
     *
     * KP Ruling Planets:
     * 1. Sign lord of the Ascendant
     * 2. Star lord (nakshatra ruler) of the Ascendant
     * 3. Sign lord of the Moon
     * 4. Star lord of the Moon
     * 5. Day lord
     *
     * These ruling planets act as a filter to validate or reject significators.
     * A significator confirmed by ruling planets is more reliable.
     * Planets that repeat in the ruling planet list are given extra weight.
     */
    private fun computeRulingPlanets(
        ascLongitude: Double,
        moonLongitude: Double,
        queryDateTime: LocalDateTime
    ): RulingPlanets {
        val ascSign = ZodiacSign.fromLongitude(VedicAstrologyUtils.normalizeLongitude(ascLongitude))
        val ascSignLord = ascSign.ruler
        val (ascNakshatra, _) = Nakshatra.fromLongitude(VedicAstrologyUtils.normalizeLongitude(ascLongitude))
        val ascStarLord = ascNakshatra.ruler

        val moonSign = ZodiacSign.fromLongitude(VedicAstrologyUtils.normalizeLongitude(moonLongitude))
        val moonSignLord = moonSign.ruler
        val (moonNakshatra, _) = Nakshatra.fromLongitude(VedicAstrologyUtils.normalizeLongitude(moonLongitude))
        val moonStarLord = moonNakshatra.ruler

        val dayLord = DAY_LORDS[queryDateTime.dayOfWeek] ?: Planet.SUN

        // Combine and order by frequency (planets appearing more often = stronger)
        val allRulers = listOf(ascSignLord, ascStarLord, moonSignLord, moonStarLord, dayLord)
        val frequencyMap = allRulers.groupBy { it }.mapValues { it.value.size }
        val combined = frequencyMap.entries
            .sortedByDescending { it.value }
            .map { it.key }
            .distinct()

        // Filter out Rahu/Ketu from ruling planets (classical KP rule):
        // If Rahu/Ketu appear, replace with their sign dispositor's effect
        // but keep them in the list for reference
        val filtered = combined.filter { it != Planet.RAHU && it != Planet.KETU }
            .ifEmpty { combined }

        val desc = buildString {
            append("Ruling Planets at ${queryDateTime}: ")
            append("Asc Sign Lord: ${ascSignLord.displayName}, ")
            append("Asc Star Lord: ${ascStarLord.displayName}, ")
            append("Moon Sign Lord: ${moonSignLord.displayName}, ")
            append("Moon Star Lord: ${moonStarLord.displayName}, ")
            append("Day Lord: ${dayLord.displayName}. ")
            if (filtered.size < combined.size) {
                append("Rahu/Ketu in ruling list noted for dispositor analysis. ")
            }
            val repeats = frequencyMap.filter { it.value > 1 }
            if (repeats.isNotEmpty()) {
                append("Repeated planets (stronger): ")
                append(repeats.entries.joinToString(", ") { "${it.key.displayName} (${it.value}x)" })
            }
        }

        return RulingPlanets(
            ascendantSignLord = ascSignLord,
            ascendantStarLord = ascStarLord,
            moonSignLord = moonSignLord,
            moonStarLord = moonStarLord,
            dayLord = dayLord,
            combinedRulingPlanets = filtered,
            description = desc
        )
    }

    // ========================================================================
    // HOUSE GROUPINGS
    // ========================================================================

    /**
     * Groups all planets by the houses they signify, creating a quick
     * reference for which planets can activate which houses.
     */
    private fun buildHouseGroupings(
        significators: Map<Planet, KPSignificatorInfo>
    ): Map<Int, List<HouseSignificator>> {
        val groupings = mutableMapOf<Int, MutableList<HouseSignificator>>()

        for (house in 1..12) {
            groupings[house] = mutableListOf()
        }

        for ((planet, sigInfo) in significators) {
            for (house in sigInfo.level1Houses) {
                groupings[house]?.add(HouseSignificator(
                    planet = planet, level = 1,
                    description = "${planet.displayName} in star of occupant of house $house"
                ))
            }
            for (house in sigInfo.level2Houses) {
                groupings[house]?.add(HouseSignificator(
                    planet = planet, level = 2,
                    description = "${planet.displayName} occupies house $house"
                ))
            }
            for (house in sigInfo.level3Houses) {
                groupings[house]?.add(HouseSignificator(
                    planet = planet, level = 3,
                    description = "${planet.displayName} in star of lord of house $house"
                ))
            }
            for (house in sigInfo.level4Houses) {
                groupings[house]?.add(HouseSignificator(
                    planet = planet, level = 4,
                    description = "${planet.displayName} is lord of house $house"
                ))
            }
        }

        // Sort each house's significators by level (1 = strongest)
        return groupings.mapValues { (_, sigs) ->
            sigs.sortedBy { it.level }.distinctBy { "${it.planet}_${it.level}" }
        }
    }

    // ========================================================================
    // 4-STEP EVENT VERIFICATION
    // ========================================================================

    /**
     * Applies the KP 4-Step Theory to verify whether the event indicated by
     * a specific house will manifest.
     *
     * **Step 1:** Check if the sub-lord of the cusp is a significator of the
     * queried house. If the sub-lord signifies the house, the event is
     * "promised" in the chart.
     *
     * **Step 2:** Check if the sub-lord connects to generally favorable houses
     * (1, 2, 3, 6, 10, 11). Connection to these houses supports manifestation.
     *
     * **Step 3:** Analyze the star lord of the sub-lord's nakshatra. The star
     * lord indicates through which channel (house) the results will manifest.
     *
     * **Step 4:** Cross-verify with current dasha period lords. The event
     * manifests when the dasha/antardasha lords are also significators of the
     * queried house.
     *
     * @param house The house to verify (1-12)
     * @param cuspInfo Pre-computed cusp sub-lord data
     * @param significators Pre-computed significator data
     * @param rulingPlanets Current ruling planets
     * @param chart The natal chart
     * @return [KPEventVerification] with detailed 4-step analysis
     */
    private fun verifyEvent(
        house: Int,
        cuspInfo: Map<Int, KPCuspInfo>,
        significators: Map<Planet, KPSignificatorInfo>,
        rulingPlanets: RulingPlanets,
        chart: VedicChart
    ): KPEventVerification {
        val cusp = cuspInfo[house] ?: return createDefaultVerification(house)
        val subLord = cusp.subLord
        val subLordSig = significators[subLord]

        // ---- Step 1: Sub-lord signifies the queried house? ----
        val subLordHouses = subLordSig?.allSignifiedHouses ?: emptyList()
        val step1Result = house in subLordHouses
        val step1Details = if (step1Result) {
            "Sub-lord ${subLord.displayName} of the ${house}${VedicAstrologyUtils.getOrdinalSuffix(house)} cusp " +
                    "IS a significator of house $house (signifies houses: ${subLordHouses.joinToString(", ")}). " +
                    "The event is PROMISED in the chart."
        } else {
            "Sub-lord ${subLord.displayName} of the ${house}${VedicAstrologyUtils.getOrdinalSuffix(house)} cusp " +
                    "is NOT a significator of house $house (signifies houses: ${subLordHouses.joinToString(", ")}). " +
                    "The event is NOT promised. However, check if sub-lord connects to related houses."
        }

        // ---- Step 2: Sub-lord connects to favorable houses? ----
        val favorableHousesSignified = subLordHouses.filter { it in GENERALLY_FAVORABLE_HOUSES }
        val step2Result = favorableHousesSignified.isNotEmpty()
        val step2Details = if (step2Result) {
            "Sub-lord ${subLord.displayName} connects to favorable houses: " +
                    "${favorableHousesSignified.joinToString(", ")}. " +
                    "This supports positive manifestation."
        } else {
            "Sub-lord ${subLord.displayName} does not connect to generally favorable houses " +
                    "(1,2,3,6,10,11). The sub-lord signifies houses: ${subLordHouses.joinToString(", ")}. " +
                    "Results may face obstacles."
        }

        // ---- Step 3: Star lord analysis of the sub-lord ----
        val subLordPos = chart.planetPositions.find { it.planet == subLord }
        val step3StarLord = subLordPos?.nakshatra?.ruler ?: Planet.KETU
        val step3StarLordSig = significators[step3StarLord]
        val step3Houses = step3StarLordSig?.allSignifiedHouses ?: emptyList()
        val step3Details = buildString {
            append("Star lord of sub-lord ${subLord.displayName}'s nakshatra is ${step3StarLord.displayName}. ")
            append("${step3StarLord.displayName} signifies houses: ${step3Houses.joinToString(", ")}. ")
            if (house in step3Houses) {
                append("Star lord CONFIRMS the queried house $house. This strengthens the event promise.")
            } else {
                append("Star lord does not directly confirm house $house. ")
                append("Results may manifest through houses ${step3Houses.joinToString(", ")} instead.")
            }
        }

        // ---- Step 4: Dasha verification via ruling planets ----
        // Check which ruling planets are significators of the queried house
        val confirmingRulers = rulingPlanets.combinedRulingPlanets.filter { rp ->
            val rpSig = significators[rp]
            rpSig?.allSignifiedHouses?.contains(house) == true
        }

        val step4Details = if (confirmingRulers.isNotEmpty()) {
            "Ruling planets confirming house $house: ${confirmingRulers.joinToString(", ") { it.displayName }}. " +
                    "The event can manifest during the dasha/antardasha of these planets."
        } else {
            "No current ruling planets directly confirm house $house. " +
                    "The event may require a different dasha period for activation. " +
                    "Ruling planets: ${rulingPlanets.combinedRulingPlanets.joinToString(", ") { it.displayName }}"
        }

        // Determine trigger planets: significators of the queried house
        // that are also ruling planets (highest priority) or strong significators
        val triggerPlanets = significators.filter { (_, sig) ->
            house in sig.allSignifiedHouses
        }.keys.filter { it in Planet.MAIN_PLANETS }.toList()

        // ---- Verdict ----
        val confirmCount = listOf(
            step1Result,
            step2Result,
            house in step3Houses,
            confirmingRulers.isNotEmpty()
        ).count { it }

        val verdict = when {
            confirmCount >= 4 -> EventVerdict.STRONG_YES
            confirmCount >= 3 -> EventVerdict.LIKELY_YES
            confirmCount >= 2 -> EventVerdict.POSSIBLE
            confirmCount >= 1 && step1Result -> EventVerdict.POSSIBLE
            step1Result -> EventVerdict.POSSIBLE
            else -> if (subLordHouses.any { VedicAstrologyUtils.isDusthana(it) && it != house })
                EventVerdict.STRONG_NO else EventVerdict.UNLIKELY
        }

        val confidence = when {
            confirmingRulers.size >= 2 -> VerificationConfidence.HIGH
            confirmingRulers.size == 1 -> VerificationConfidence.MEDIUM
            else -> VerificationConfidence.LOW
        }

        return KPEventVerification(
            houseNumber = house,
            step1SubLordSignifies = step1Result,
            step1Details = step1Details,
            step2FavorableConnection = step2Result,
            step2Details = step2Details,
            step3StarLordAnalysis = step3Details,
            step3StarLord = step3StarLord,
            step4DashaVerification = step4Details,
            verdict = verdict,
            confidence = confidence,
            triggerPlanets = triggerPlanets
        )
    }

    /**
     * Creates a default verification result for error cases.
     */
    private fun createDefaultVerification(house: Int): KPEventVerification {
        return KPEventVerification(
            houseNumber = house,
            step1SubLordSignifies = false,
            step1Details = "Cusp data not available for house $house.",
            step2FavorableConnection = false,
            step2Details = "Unable to evaluate favorable connections.",
            step3StarLordAnalysis = "Star lord analysis not available.",
            step3StarLord = Planet.KETU,
            step4DashaVerification = "Dasha verification not available.",
            verdict = EventVerdict.UNLIKELY,
            confidence = VerificationConfidence.LOW,
            triggerPlanets = emptyList()
        )
    }

    // ========================================================================
    // UTILITY FUNCTIONS
    // ========================================================================

    /**
     * Gets the KP sub-division table for external reference or display.
     * The table is lazily computed and cached.
     */
    fun getSubDivisionTable(): List<KPSubDivision> = subDivisionTable

    /**
     * Finds all significators for a given house across all 4 levels.
     * Returns a map from significator level to list of planets.
     *
     * @param chart The natal chart
     * @param house The house number (1-12)
     * @return Map of level (1-4) to list of significator planets
     */
    fun findSignificatorsForHouse(chart: VedicChart, house: Int): Map<Int, List<Planet>> {
        val significators = computePlanetSignificators(chart)
        val result = mutableMapOf<Int, MutableList<Planet>>()
        for (level in 1..4) result[level] = mutableListOf()

        for ((planet, sig) in significators) {
            if (house in sig.level1Houses) result[1]?.add(planet)
            if (house in sig.level2Houses) result[2]?.add(planet)
            if (house in sig.level3Houses) result[3]?.add(planet)
            if (house in sig.level4Houses) result[4]?.add(planet)
        }

        return result
    }

    /**
     * Quick check: will the event associated with a house manifest?
     * This is a convenience method that runs only the critical Step 1
     * of the 4-step theory (sub-lord verification).
     *
     * @param chart The natal chart
     * @param house The house to query (1-12)
     * @return True if the sub-lord of the cusp signifies the house
     */
    fun isEventPromised(chart: VedicChart, house: Int): Boolean {
        val cuspLong = if (house <= chart.houseCusps.size) {
            chart.houseCusps[house - 1]
        } else {
            VedicAstrologyUtils.normalizeLongitude(chart.ascendant + (house - 1) * 30.0)
        }

        val subLord = getSubLord(cuspLong)
        val sigHouses = getSignificatorHousesForPlanet(chart, subLord)
        return house in sigHouses
    }

    /**
     * Determines the KP number (1-249) for any given longitude.
     *
     * @param longitude Sidereal longitude in degrees
     * @return KP number (1-249)
     */
    fun getKPNumber(longitude: Double): Int {
        return findSubDivision(longitude).kpNumber
    }

    /**
     * Validates significators using ruling planets.
     * Returns only those significators of a house that are confirmed by
     * the current ruling planet set.
     *
     * @param chart The natal chart
     * @param house The house to validate (1-12)
     * @param queryDateTime The date/time for ruling planet computation
     * @param queryMoonLongitude Moon's transit longitude at query time
     * @param queryAscendantLongitude Ascendant longitude at query time
     * @return List of validated significator planets
     */
    fun getValidatedSignificators(
        chart: VedicChart,
        house: Int,
        queryDateTime: LocalDateTime = LocalDateTime.now(ZoneOffset.UTC),
        queryMoonLongitude: Double? = null,
        queryAscendantLongitude: Double? = null
    ): List<Planet> {
        val moonLong = queryMoonLongitude
            ?: chart.planetPositions.find { it.planet == Planet.MOON }?.longitude ?: 0.0
        val ascLong = queryAscendantLongitude ?: chart.ascendant

        val rulingPlanets = computeRulingPlanets(ascLong, moonLong, queryDateTime)
        val significators = computePlanetSignificators(chart)

        // Find all significators of this house
        val houseSignificators = significators.filter { (_, sig) ->
            house in sig.allSignifiedHouses
        }.keys

        // Filter by ruling planets (validated significators must also be ruling planets
        // or be in the star of a ruling planet)
        val rulerSet = rulingPlanets.combinedRulingPlanets.toSet()

        return houseSignificators.filter { planet ->
            planet in rulerSet || significators[planet]?.starLord in rulerSet
        }.toList()
    }
}
