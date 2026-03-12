package com.astro.vajra.ephemeris.kp

import com.astro.vajra.core.model.Nakshatra
import com.astro.vajra.core.model.Planet
import com.astro.vajra.core.model.ZodiacSign
import com.astro.vajra.ephemeris.AstrologicalConstants

/**
 * KP Sub-Lord Table Generator and Lookup Engine
 *
 * Generates the complete 249-entry KP sub-lord table algorithmically.
 *
 * The zodiac (0°–360° sidereal) is divided into:
 *   27 nakshatras × 9 sub-divisions = 243 base entries
 *   + entries split at sign boundaries (every 30°) = 249 total entries
 *
 * Each nakshatra (13°20') is divided proportionally by the Vimshottari dasha
 * periods of the 9 planets. The sub-lord sequence within each nakshatra starts
 * from the nakshatra's own lord.
 *
 * Reference: K.S. Krishnamurti - "Reader" series, Volumes 1-6
 */
object KPSubLordTable {

    /** Vimshottari dasha sequence */
    private val VIMSHOTTARI_SEQUENCE = AstrologicalConstants.VIMSOTTARI_SEQUENCE

    /** Total Vimshottari cycle years */
    private const val TOTAL_YEARS = 120.0

    /** Degrees per nakshatra (13°20') */
    private const val NAKSHATRA_SPAN = AstrologicalConstants.DEGREES_PER_NAKSHATRA

    /** Vimshottari period for each planet in years */
    private val PLANET_YEARS = mapOf(
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

    /**
     * The complete 249-entry sub-lord table.
     * Lazily generated on first access and cached permanently.
     */
    val entries: List<KPSubLordEntry> by lazy { generateTable() }

    /**
     * Lookup the KP position (sign lord, star lord, sub lord, sub-sub lord)
     * for any sidereal longitude.
     */
    fun getKPPosition(longitude: Double): KPPosition {
        val normalized = AstrologicalConstants.normalizeDegree(longitude)
        val sign = ZodiacSign.fromLongitude(normalized)
        val nakshatra = Nakshatra.fromLongitude(normalized).first
        val starLord = nakshatra.ruler
        val subLord = findSubLord(normalized)
        val subSubLord = findSubSubLord(normalized)
        val degreeInSign = AstrologicalConstants.getDegreeInSign(normalized)

        return KPPosition(
            longitude = normalized,
            sign = sign,
            signLord = sign.ruler,
            nakshatra = nakshatra,
            starLord = starLord,
            subLord = subLord,
            subSubLord = subSubLord,
            degreeInSign = degreeInSign,
            formattedDegree = formatDegree(normalized)
        )
    }

    /**
     * Get the sub-lord entry for a given horary number (1-249).
     */
    fun getEntryByNumber(number: Int): KPSubLordEntry {
        require(number in 1..249) { "KP horary number must be between 1 and 249" }
        return entries[number - 1]
    }

    /**
     * Find the sub-lord for a given sidereal longitude.
     *
     * The sub-lord is determined by the Vimshottari proportional division
     * within the current nakshatra.
     */
    fun findSubLord(longitude: Double): Planet {
        val normalized = AstrologicalConstants.normalizeDegree(longitude)

        // Determine which nakshatra and position within it
        val nakshatraIndex = (normalized / NAKSHATRA_SPAN).toInt().coerceIn(0, 26)
        val nakshatraStart = nakshatraIndex * NAKSHATRA_SPAN
        val positionInNakshatra = normalized - nakshatraStart

        // Get the star lord and find its index in Vimshottari sequence
        val starLord = Nakshatra.entries[nakshatraIndex].ruler
        val startIndex = VIMSHOTTARI_SEQUENCE.indexOf(starLord)

        // Walk through the sub-divisions to find which sub contains this position
        var accumulated = 0.0
        for (i in 0 until 9) {
            val subPlanet = VIMSHOTTARI_SEQUENCE[(startIndex + i) % 9]
            val subSpan = (PLANET_YEARS[subPlanet]!! / TOTAL_YEARS) * NAKSHATRA_SPAN
            accumulated += subSpan
            if (positionInNakshatra < accumulated + 1e-10) {
                return subPlanet
            }
        }

        // Should never reach here; return last planet as safety
        return VIMSHOTTARI_SEQUENCE[(startIndex + 8) % 9]
    }

    /**
     * Find the sub-sub-lord for a given sidereal longitude.
     *
     * The sub-sub-lord is computed by further dividing the sub portion
     * using the same Vimshottari proportional method, starting from the sub-lord.
     */
    fun findSubSubLord(longitude: Double): Planet {
        val normalized = AstrologicalConstants.normalizeDegree(longitude)

        // Determine nakshatra and position within it
        val nakshatraIndex = (normalized / NAKSHATRA_SPAN).toInt().coerceIn(0, 26)
        val nakshatraStart = nakshatraIndex * NAKSHATRA_SPAN
        val positionInNakshatra = normalized - nakshatraStart

        // Get the star lord
        val starLord = Nakshatra.entries[nakshatraIndex].ruler
        val starStartIndex = VIMSHOTTARI_SEQUENCE.indexOf(starLord)

        // Find the sub-lord and position within the sub
        var subAccumulated = 0.0
        for (i in 0 until 9) {
            val subPlanet = VIMSHOTTARI_SEQUENCE[(starStartIndex + i) % 9]
            val subSpan = (PLANET_YEARS[subPlanet]!! / TOTAL_YEARS) * NAKSHATRA_SPAN
            val subStart = subAccumulated
            subAccumulated += subSpan

            if (positionInNakshatra < subAccumulated + 1e-10) {
                // Found the sub. Now find the sub-sub within this sub.
                val positionInSub = positionInNakshatra - subStart
                val subSubStartIndex = VIMSHOTTARI_SEQUENCE.indexOf(subPlanet)

                var subSubAccumulated = 0.0
                for (j in 0 until 9) {
                    val subSubPlanet = VIMSHOTTARI_SEQUENCE[(subSubStartIndex + j) % 9]
                    val subSubSpan = (PLANET_YEARS[subSubPlanet]!! / TOTAL_YEARS) * subSpan
                    subSubAccumulated += subSubSpan

                    if (positionInSub < subSubAccumulated + 1e-10) {
                        return subSubPlanet
                    }
                }
                // Safety fallback
                return VIMSHOTTARI_SEQUENCE[(subSubStartIndex + 8) % 9]
            }
        }

        // Safety fallback
        return VIMSHOTTARI_SEQUENCE[(starStartIndex + 8) % 9]
    }

    // ========================================================================
    // TABLE GENERATION
    // ========================================================================

    /**
     * Generates the complete 249-entry KP sub-lord table.
     *
     * Algorithm:
     * 1. For each of 27 nakshatras, compute 9 sub-divisions (proportional to Vimshottari)
     * 2. Each sub-division has a start/end degree, star lord, and sub lord
     * 3. If a sub-division crosses a sign boundary (multiple of 30°), split it into
     *    two entries with different sign lords
     * 4. Number entries sequentially 1-249
     */
    private fun generateTable(): List<KPSubLordEntry> {
        val result = mutableListOf<KPSubLordEntry>()
        var entryNumber = 1

        for (nakshatraIndex in 0 until 27) {
            val nakshatraStart = nakshatraIndex * NAKSHATRA_SPAN
            val starLord = Nakshatra.entries[nakshatraIndex].ruler
            val starStartIndex = VIMSHOTTARI_SEQUENCE.indexOf(starLord)

            var subStart = nakshatraStart

            for (subIndex in 0 until 9) {
                val subPlanet = VIMSHOTTARI_SEQUENCE[(starStartIndex + subIndex) % 9]
                val subSpan = (PLANET_YEARS[subPlanet]!! / TOTAL_YEARS) * NAKSHATRA_SPAN
                val subEnd = subStart + subSpan

                // Check if this sub crosses a sign boundary
                val signBoundaries = findSignBoundariesInRange(subStart, subEnd)

                if (signBoundaries.isEmpty()) {
                    // No sign boundary crossing - single entry
                    val sign = ZodiacSign.fromLongitude(subStart)
                    result.add(
                        KPSubLordEntry(
                            number = entryNumber++,
                            startDegree = subStart,
                            endDegree = subEnd,
                            signLord = sign.ruler,
                            starLord = starLord,
                            subLord = subPlanet,
                            sign = sign
                        )
                    )
                } else {
                    // Split at each sign boundary
                    var splitStart = subStart
                    for (boundary in signBoundaries) {
                        // Part before boundary
                        val signBefore = ZodiacSign.fromLongitude(splitStart)
                        result.add(
                            KPSubLordEntry(
                                number = entryNumber++,
                                startDegree = splitStart,
                                endDegree = boundary,
                                signLord = signBefore.ruler,
                                starLord = starLord,
                                subLord = subPlanet,
                                sign = signBefore
                            )
                        )
                        splitStart = boundary
                    }
                    // Final part after last boundary
                    val signAfter = ZodiacSign.fromLongitude(splitStart)
                    result.add(
                        KPSubLordEntry(
                            number = entryNumber++,
                            startDegree = splitStart,
                            endDegree = subEnd,
                            signLord = signAfter.ruler,
                            starLord = starLord,
                            subLord = subPlanet,
                            sign = signAfter
                        )
                    )
                }

                subStart = subEnd
            }
        }

        return result
    }

    /**
     * Find all sign boundaries (multiples of 30°) that fall strictly within the range (start, end).
     */
    private fun findSignBoundariesInRange(start: Double, end: Double): List<Double> {
        val boundaries = mutableListOf<Double>()
        // Sign boundaries are at 30, 60, 90, ..., 330, 360
        for (i in 1..12) {
            val boundary = i * 30.0
            // Strictly inside the range (not at endpoints)
            if (boundary > start + 1e-10 && boundary < end - 1e-10) {
                boundaries.add(boundary)
            }
        }
        return boundaries.sorted()
    }

    /**
     * Format a degree value as DD°MM'SS" SignAbbr
     */
    fun formatDegree(longitude: Double): String {
        val normalized = AstrologicalConstants.normalizeDegree(longitude)
        val sign = ZodiacSign.fromLongitude(normalized)
        val degInSign = normalized - sign.startDegree
        val degrees = degInSign.toInt()
        val minutesTotal = (degInSign - degrees) * 60.0
        val minutes = minutesTotal.toInt()
        val seconds = ((minutesTotal - minutes) * 60.0).toInt()
        return "%02d°%02d'%02d\" %s".format(degrees, minutes, seconds, sign.abbreviation)
    }
}
