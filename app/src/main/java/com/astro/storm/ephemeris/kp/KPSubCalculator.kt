package com.astro.storm.ephemeris.kp

import com.astro.storm.core.model.Nakshatra
import com.astro.storm.core.model.Planet
import com.astro.storm.core.model.ZodiacSign

/**
 * KP Sub Calculator
 *
 * Calculates the 249 KP sub-divisions of the zodiac.
 * Each of the 27 Nakshatras is divided into 9 subs based on
 * Vimshottari Dasha proportions (27 × 9 = 243 main subs).
 *
 * Sub calculation:
 * - Nakshatra span: 13°20' (800 minutes)
 * - Each sub's span is proportional to its lord's dasha years
 * - Sub lord sequence follows Vimshottari order starting from star lord
 *
 * The 249 numbers (used in KP Horary) cover 360° with slight adjustments.
 *
 * @author AstroStorm - Ultra-Precision Vedic Astrology
 */
object KPSubCalculator {

    /**
     * Nakshatra span in degrees
     */
    private const val NAKSHATRA_SPAN = 13.333333333333334 // 13°20'

    /**
     * Total zodiac degrees
     */
    private const val TOTAL_DEGREES = 360.0

    /**
     * Pre-calculated sub table for all 27 nakshatras × 9 subs = 243 entries
     */
    private val subTable: List<KPSub> by lazy { generateSubTable() }

    /**
     * KP Number table (1-249)
     */
    private val kpNumberTable: List<KPNumber> by lazy { generateKPNumberTable() }

    /**
     * Generate the complete sub table
     */
    private fun generateSubTable(): List<KPSub> {
        val subs = mutableListOf<KPSub>()
        var currentDegree = 0.0

        for (nakshatra in Nakshatra.entries) {
            val starLord = nakshatra.ruler
            val subSequence = VimshottariYears.getSequenceFrom(starLord)

            for ((subIndex, subLord) in subSequence.withIndex()) {
                val subSpan = VimshottariYears.calculateSubSpan(subLord, NAKSHATRA_SPAN)
                val startDegree = currentDegree
                val endDegree = currentDegree + subSpan

                subs.add(KPSub(
                    nakshatra = nakshatra,
                    subLord = subLord,
                    subNumber = subIndex + 1,
                    startDegree = startDegree,
                    endDegree = endDegree,
                    spanDegrees = subSpan
                ))

                currentDegree = endDegree
            }
        }

        return subs
    }

    /**
     * Generate KP Number table (1-249)
     * Used primarily in KP Horary
     */
    private fun generateKPNumberTable(): List<KPNumber> {
        val numbers = mutableListOf<KPNumber>()
        val totalNumbers = 249
        val degreePerNumber = TOTAL_DEGREES / totalNumbers

        for (num in 1..totalNumbers) {
            val startDegree = (num - 1) * degreePerNumber
            val midDegree = startDegree + degreePerNumber / 2

            val sign = ZodiacSign.fromLongitude(midDegree)
            val (nakshatra, pada) = Nakshatra.fromLongitude(midDegree)
            val sub = getSubAtDegree(midDegree)

            numbers.add(KPNumber(
                number = num,
                sign = sign,
                nakshatra = nakshatra,
                pada = pada,
                subLord = sub?.subLord ?: nakshatra.ruler,
                degreesInSign = midDegree % 30
            ))
        }

        return numbers
    }

    /**
     * Get the sub at a specific zodiacal degree
     */
    fun getSubAtDegree(degree: Double): KPSub? {
        val normalizedDegree = (degree % TOTAL_DEGREES + TOTAL_DEGREES) % TOTAL_DEGREES
        return subTable.find { normalizedDegree >= it.startDegree && normalizedDegree < it.endDegree }
    }

    /**
     * Get complete KP position for a degree
     */
    fun getKPPosition(longitude: Double): KPPosition {
        val normalizedLongitude = (longitude % TOTAL_DEGREES + TOTAL_DEGREES) % TOTAL_DEGREES

        val sign = ZodiacSign.fromLongitude(normalizedLongitude)
        val (nakshatra, pada) = Nakshatra.fromLongitude(normalizedLongitude)
        val starLord = nakshatra.ruler

        // Get sub
        val sub = getSubAtDegree(normalizedLongitude)
        val subLord = sub?.subLord ?: starLord

        // Calculate sub-sub
        val subSubLord = calculateSubSub(normalizedLongitude, sub)

        return KPPosition(
            longitude = normalizedLongitude,
            sign = sign,
            signLord = sign.ruler,
            nakshatra = nakshatra,
            nakshatraPada = pada,
            starLord = starLord,
            subLord = subLord,
            subSubLord = subSubLord
        )
    }

    /**
     * Calculate sub-sub lord for even finer precision
     */
    private fun calculateSubSub(longitude: Double, sub: KPSub?): Planet? {
        if (sub == null) return null

        val positionInSub = longitude - sub.startDegree
        val subSequence = VimshottariYears.getSequenceFrom(sub.subLord)

        var currentPos = 0.0
        for (subSubLord in subSequence) {
            val subSubSpan = VimshottariYears.calculateSubSpan(subSubLord, sub.spanDegrees)
            if (positionInSub < currentPos + subSubSpan) {
                return subSubLord
            }
            currentPos += subSubSpan
        }

        return sub.subLord
    }

    /**
     * Get all subs for a specific nakshatra
     */
    fun getSubsForNakshatra(nakshatra: Nakshatra): List<KPSub> {
        return subTable.filter { it.nakshatra == nakshatra }
    }

    /**
     * Get all subs for a specific sign
     */
    fun getSubsForSign(sign: ZodiacSign): List<KPSub> {
        val signStart = sign.startDegree
        val signEnd = sign.endDegree
        return subTable.filter {
            (it.startDegree >= signStart && it.startDegree < signEnd) ||
                    (it.endDegree > signStart && it.endDegree <= signEnd) ||
                    (it.startDegree < signStart && it.endDegree > signEnd)
        }
    }

    /**
     * Get KP Number details
     */
    fun getKPNumber(number: Int): KPNumber? {
        if (number < 1 || number > KPNumber.MAX_NUMBER) return null
        return kpNumberTable.getOrNull(number - 1)
    }

    /**
     * Find KP Number from longitude
     */
    fun getKPNumberFromLongitude(longitude: Double): KPNumber? {
        val normalizedLongitude = (longitude % TOTAL_DEGREES + TOTAL_DEGREES) % TOTAL_DEGREES
        val degreePerNumber = TOTAL_DEGREES / 249
        val number = (normalizedLongitude / degreePerNumber).toInt() + 1
        return getKPNumber(number.coerceIn(1, 249))
    }

    /**
     * Get all subs in the table
     */
    fun getAllSubs(): List<KPSub> = subTable.toList()

    /**
     * Get sub-lord for a specific degree without full position
     */
    fun getSubLord(longitude: Double): Planet {
        return getSubAtDegree(longitude)?.subLord ?: Nakshatra.fromLongitude(longitude).first.ruler
    }

    /**
     * Get star-lord (nakshatra lord) for a degree
     */
    fun getStarLord(longitude: Double): Planet {
        return Nakshatra.fromLongitude(longitude).first.ruler
    }

    /**
     * Create a Sub Navigator for timing analysis
     */
    fun createSubNavigator(
        planetPositions: Map<Planet, Double>,
        cuspPositions: Map<Int, Double>
    ): SubLordNavigator {
        val planetSubPositions = planetPositions.mapValues { (_, lng) ->
            getSubAtDegree(lng) ?: subTable.first()
        }

        val cuspSubPositions = cuspPositions.mapValues { (_, lng) ->
            getSubAtDegree(lng) ?: subTable.first()
        }

        return SubLordNavigator(
            allSubs = subTable,
            planetSubPositions = planetSubPositions,
            cuspSubPositions = cuspSubPositions
        )
    }

    /**
     * Find when a transiting planet will reach a specific sub-lord
     */
    fun findNextSubLordOccurrence(
        currentLongitude: Double,
        targetSubLord: Planet,
        searchWithin: Double = 360.0
    ): List<Double> {
        val normalizedCurrent = (currentLongitude % TOTAL_DEGREES + TOTAL_DEGREES) % TOTAL_DEGREES
        val endSearch = normalizedCurrent + searchWithin

        return subTable
            .filter { it.subLord == targetSubLord }
            .map { it.startDegree }
            .filter { degree ->
                val adjustedDegree = if (degree < normalizedCurrent) degree + 360 else degree
                adjustedDegree in normalizedCurrent..endSearch
            }
            .map { (it % TOTAL_DEGREES) }
    }

    /**
     * Get significator chain for a degree (Sign → Star → Sub → Sub-Sub)
     */
    fun getSignificatorChain(longitude: Double): List<Planet> {
        val position = getKPPosition(longitude)
        return position.getSignificatorChain()
    }

    /**
     * Format sub position as readable string
     */
    fun formatSubPosition(longitude: Double): String {
        val position = getKPPosition(longitude)
        val degInSign = longitude % 30
        val deg = degInSign.toInt()
        val min = ((degInSign - deg) * 60).toInt()
        val sec = (((degInSign - deg) * 60 - min) * 60).toInt()

        return buildString {
            append(position.sign.abbreviation)
            append(" ${deg}°${min}'${sec}\"")
            append(" | ${position.nakshatra.displayName}-${position.nakshatraPada}")
            append(" | Star: ${position.starLord.displayName}")
            append(" | Sub: ${position.subLord.displayName}")
            position.subSubLord?.let { append(" | SS: ${it.displayName}") }
        }
    }
}
