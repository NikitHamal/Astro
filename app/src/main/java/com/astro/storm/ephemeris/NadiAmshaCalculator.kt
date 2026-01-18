package com.astro.storm.ephemeris

import com.astro.storm.core.common.Language
import com.astro.storm.core.common.StringKeyAdvanced
import com.astro.storm.core.common.StringResources
import com.astro.storm.core.common.getLocalizedName
import com.astro.storm.core.model.Planet
import com.astro.storm.core.model.PlanetPosition
import com.astro.storm.core.model.VedicChart
import com.astro.storm.core.model.ZodiacSign
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import kotlin.math.abs
import kotlin.math.floor

/**
 * Nadi Amsha (D-150) Calculator
 *
 * D-150 (Nadi Amsha or Nadi Navamsa/Chaturaseethi-sama-amsa) is the finest
 * divisional chart used in Vedic astrology for:
 * - Precise birth time rectification (to within seconds)
 * - Very subtle predictions and timing
 * - Understanding deep karmic patterns
 *
 * Technical Details:
 * - Each Nadi spans exactly 12 arc-minutes (0°12' or 0.2°)
 * - 150 Nadis per sign × 12 signs = 1800 Nadis in the zodiac
 * - At the equator, Ascendant moves through ~1 Nadi every 48 seconds
 *
 * Calculation Rules (per classical texts):
 * - Odd Signs (Aries, Gemini, Leo, Libra, Sagittarius, Aquarius):
 *   Count 1→150 from 0° to 30° (Savya/direct order)
 * - Even Signs (Taurus, Cancer, Virgo, Scorpio, Capricorn, Pisces):
 *   Count 1→150 from 30° to 0° (Apasavya/reverse order)
 *
 * D-150 Sign Determination:
 * Following the standard Varga Vimshopaka rule for D-150:
 * The Nadi number determines which sign the planet occupies in D-150.
 * Each Nadi maps to one of the 12 signs cyclically based on the
 * starting sign and the Nadi number.
 *
 * Precision Requirements:
 * - Birth time must be known to within 4 minutes for meaningful D-150 analysis
 * - For rectification, even 1-2 minute shifts cause significant Nadi changes
 *
 * @author AstroStorm - Ultra-Precision Vedic Astrology
 */
object NadiAmshaCalculator {

    /** Total degrees in one zodiac sign */
    private const val DEGREES_IN_SIGN = 30.0

    /** Number of Nadi divisions per sign */
    private const val NADIS_PER_SIGN = 150

    /**
     * Size of each Nadi in degrees.
     * 30° / 150 = 0.2° = 12 arc-minutes exactly.
     */
    private const val NADI_PART_DEGREES = DEGREES_IN_SIGN / NADIS_PER_SIGN // 0.2 degrees

    /**
     * Size of each Nadi in arc-minutes for display purposes.
     */
    private const val NADI_PART_ARCMINUTES = 12.0 // 0.2° × 60 = 12'

    /**
     * Average Ascendant speed in degrees per minute.
     * 360° / 1440 minutes (24 hours) = 0.25°/min average.
     * Actual speed varies by latitude and local sidereal time.
     */
    private const val AVG_ASCENDANT_SPEED_DEG_PER_MIN = 0.25

    /**
     * Average time for Ascendant to cross one Nadi (in seconds).
     * 0.2° / 0.25°/min = 0.8 min = 48 seconds average.
     */
    private const val AVG_NADI_CROSSING_SECONDS = 48.0

    /**
     * Complete Nadi Amsha analysis result
     */
    data class NadiAmshaResult(
        val chart: VedicChart,
        val ascendantNadi: NadiPosition,
        val planetNadis: List<NadiPosition>,
        val rectificationCandidates: List<RectificationCandidate>,
        val precisionWarnings: List<String> = emptyList()
    )

    /**
     * Nadi position for a planet or Ascendant.
     *
     * Contains precise information about which of the 150 Nadis
     * the planet occupies, along with the resulting D-150 sign.
     */
    data class NadiPosition(
        val planet: Planet?, // Null for Ascendant
        val startSign: ZodiacSign,
        val longitude: Double,
        val degreeInSign: Double,
        val nadiNumber: Int, // 1-150
        val nadiSign: ZodiacSign,
        val nadiLord: Planet, // Ruler of the Nadi sign
        val description: String,
        val descriptionNe: String,
        val energyType: NadiEnergyType,
        val progressInNadi: Double, // 0.0 to 1.0 - how far through current Nadi
        val secondsToNextNadi: Double // Estimated seconds until next Nadi boundary (for Asc)
    )

    /**
     * Rectification candidate for birth time adjustment.
     *
     * Provides detailed information about a potential birth time
     * correction based on Nadi boundary analysis.
     */
    data class RectificationCandidate(
        val timeAdjustmentSeconds: Int, // Changed from minutes to seconds for precision
        val adjustedTime: LocalDateTime,
        val nadiNumber: Int,
        val nadiSign: ZodiacSign,
        val description: String,
        val descriptionNe: String,
        val confidence: Int, // 0-100
        val isNadiBoundary: Boolean // True if close to Nadi boundary
    )

    /**
     * Energy type classification for Nadis.
     *
     * Nadis alternate between male (odd-numbered) and female (even-numbered)
     * energies, used in compatibility and prediction analysis.
     */
    enum class NadiEnergyType(val displayName: String, val displayNameNe: String) {
        MALE("Male Energy (Purusha)", "पुरुष ऊर्जा"),
        FEMALE("Female Energy (Prakriti)", "प्रकृति ऊर्जा")
    }

    /**
     * Calculate comprehensive Nadi Amsha analysis.
     *
     * Provides full D-150 analysis including:
     * - Ascendant and all planet Nadi positions
     * - Rectification candidates for birth time adjustment
     * - Precision warnings if birth time uncertainty is high
     *
     * @param chart The VedicChart to analyze
     * @param birthTimeUncertaintyMinutes Estimated uncertainty in birth time (default 4 minutes)
     * @return Complete NadiAmshaResult with all analysis
     */
    fun calculateNadiAmsha(
        chart: VedicChart,
        birthTimeUncertaintyMinutes: Double = 4.0
    ): NadiAmshaResult {
        val precisionWarnings = mutableListOf<String>()

        // Warn if birth time uncertainty is too high for meaningful D-150
        val nadisOfUncertainty = (birthTimeUncertaintyMinutes * AVG_ASCENDANT_SPEED_DEG_PER_MIN) / NADI_PART_DEGREES
        if (nadisOfUncertainty > 5) {
            precisionWarnings.add(
                "Birth time uncertainty of ${birthTimeUncertaintyMinutes.toInt()} minutes spans approximately ${nadisOfUncertainty.toInt()} Nadis. " +
                "D-150 analysis may be unreliable. Recommend rectification."
            )
        }

        // 1. Calculate Ascendant Nadi with high precision
        val ascendantNadi = calculateNadiPositionPrecise(
            planet = null,
            longitude = chart.ascendant,
            isAscendant = true
        )

        // 2. Calculate Planet Nadis
        val planetNadis = chart.planetPositions.map { position ->
            calculateNadiPositionPrecise(
                planet = position.planet,
                longitude = position.longitude,
                isAscendant = false
            )
        }

        // 3. Generate rectification candidates with second-level precision
        val rectificationCandidates = generateRectificationCandidatesPrecise(chart, ascendantNadi)

        // 4. Add warning if Ascendant is very close to Nadi boundary
        if (ascendantNadi.progressInNadi < 0.1 || ascendantNadi.progressInNadi > 0.9) {
            val boundaryType = if (ascendantNadi.progressInNadi < 0.1) "entering" else "leaving"
            precisionWarnings.add(
                "Ascendant is $boundaryType current Nadi boundary. Even small birth time adjustments " +
                "(~${ascendantNadi.secondsToNextNadi.toInt()} seconds) would shift the Nadi."
            )
        }

        return NadiAmshaResult(
            chart = chart,
            ascendantNadi = ascendantNadi,
            planetNadis = planetNadis,
            rectificationCandidates = rectificationCandidates,
            precisionWarnings = precisionWarnings
        )
    }

    /**
     * Calculate Nadi position with high precision.
     *
     * Uses floor() instead of toInt() for proper segment calculation,
     * and handles the exact boundary cases correctly.
     *
     * @param planet The planet (null for Ascendant)
     * @param longitude The longitude in degrees
     * @param isAscendant Whether this is the Ascendant (affects time calculations)
     * @return Precise NadiPosition
     */
    private fun calculateNadiPositionPrecise(
        planet: Planet?,
        longitude: Double,
        isAscendant: Boolean
    ): NadiPosition {
        // Normalize longitude to 0-360 range with high precision
        val normalizedLong = VedicAstrologyUtils.normalizeLongitude(longitude)

        // Determine sign (0-indexed)
        val signIndex = floor(normalizedLong / DEGREES_IN_SIGN).toInt().coerceIn(0, 11)
        val sign = ZodiacSign.entries[signIndex]

        // Calculate precise degree within sign
        val degreeInSign = normalizedLong - (signIndex * DEGREES_IN_SIGN)

        // Determine if odd sign (Savya) or even sign (Apasavya)
        val isOddSign = (sign.number % 2 != 0)

        // Calculate precise Nadi segment (0-indexed internally, then convert to 1-150)
        // Using floor() for proper mathematical behavior at boundaries
        val rawSegment = floor(degreeInSign / NADI_PART_DEGREES).toInt()

        // Calculate Nadi number (1-150)
        // Odd signs: Segment 0 → Nadi 1, Segment 149 → Nadi 150
        // Even signs: Segment 0 → Nadi 150, Segment 149 → Nadi 1 (reverse)
        val nadiNumber = if (isOddSign) {
            (rawSegment + 1).coerceIn(1, NADIS_PER_SIGN)
        } else {
            (NADIS_PER_SIGN - rawSegment).coerceIn(1, NADIS_PER_SIGN)
        }

        // Calculate progress within current Nadi (0.0 to 1.0)
        val nadiStartDegree = rawSegment * NADI_PART_DEGREES
        val progressInNadi = (degreeInSign - nadiStartDegree) / NADI_PART_DEGREES

        // Calculate seconds to next Nadi boundary (for Ascendant)
        val degreesToNextBoundary = NADI_PART_DEGREES - (degreeInSign - nadiStartDegree)
        val secondsToNextNadi = if (isAscendant) {
            (degreesToNextBoundary / AVG_ASCENDANT_SPEED_DEG_PER_MIN) * 60.0
        } else {
            0.0 // Not meaningful for planets (different speeds)
        }

        // Calculate D-150 Sign using standard cyclic rule
        // The sign in D-150 is determined by cycling through signs based on Nadi number
        // Starting from the rashi sign, count (nadiNumber - 1) signs forward
        val nadiSignIndex = (signIndex + nadiNumber - 1) % 12
        val nadiSign = ZodiacSign.entries[nadiSignIndex]
        val nadiLord = nadiSign.ruler

        // Determine energy type: odd Nadis = Male (Purusha), even Nadis = Female (Prakriti)
        val energyType = if (nadiNumber % 2 != 0) NadiEnergyType.MALE else NadiEnergyType.FEMALE

        // Format degree for display
        val degreeDisplay = formatDegreeMinuteSecond(degreeInSign)
        val entityName = planet?.displayName ?: "Ascendant"

        return NadiPosition(
            planet = planet,
            startSign = sign,
            longitude = longitude,
            degreeInSign = degreeInSign,
            nadiNumber = nadiNumber,
            nadiSign = nadiSign,
            nadiLord = nadiLord,
            description = "$entityName: Nadi #$nadiNumber in ${sign.displayName} ($degreeDisplay)",
            descriptionNe = "$entityName: ${sign.getLocalizedName(Language.NEPALI)} मा नाडी #$nadiNumber ($degreeDisplay)",
            energyType = energyType,
            progressInNadi = progressInNadi.coerceIn(0.0, 1.0),
            secondsToNextNadi = secondsToNextNadi
        )
    }

    /**
     * Format degrees as degrees°minutes'seconds"
     */
    private fun formatDegreeMinuteSecond(degrees: Double): String {
        val wholeDegrees = floor(degrees).toInt()
        val remainingMinutes = (degrees - wholeDegrees) * 60.0
        val wholeMinutes = floor(remainingMinutes).toInt()
        val remainingSeconds = (remainingMinutes - wholeMinutes) * 60.0
        val wholeSeconds = floor(remainingSeconds).toInt()
        return "${wholeDegrees}°${wholeMinutes}'${wholeSeconds}\""
    }

    /**
     * Generate precise rectification candidates based on Nadi boundary analysis.
     *
     * Uses second-level precision for birth time adjustments, generating
     * candidates for nearby Nadi boundaries that could apply if birth time
     * were slightly different.
     *
     * @param chart The VedicChart to analyze
     * @param currentAscendantNadi The current Ascendant Nadi position
     * @return List of rectification candidates ordered by confidence
     */
    private fun generateRectificationCandidatesPrecise(
        chart: VedicChart,
        currentAscendantNadi: NadiPosition
    ): List<RectificationCandidate> {
        val candidates = mutableListOf<RectificationCandidate>()
        val currentSign = currentAscendantNadi.startSign
        val signIndex = currentSign.ordinal
        val degreeInSign = currentAscendantNadi.degreeInSign
        val isOddSign = (currentSign.number % 2 != 0)

        // Calculate current raw segment (0-indexed)
        val currentRawSegment = floor(degreeInSign / NADI_PART_DEGREES).toInt()

        // Calculate precise position within current Nadi for boundary detection
        val nadiStartDegree = currentRawSegment * NADI_PART_DEGREES
        val positionInNadi = degreeInSign - nadiStartDegree

        // Check +/- 5 Nadis (approx +/- 4 minutes of birth time)
        for (nadiOffset in -5..5) {
            if (nadiOffset == 0) continue // Skip current Nadi

            // Calculate target segment
            val targetSegment = currentRawSegment + nadiOffset

            // Skip if target segment is outside this sign (would require recalculating with different sign)
            if (targetSegment < 0 || targetSegment >= NADIS_PER_SIGN) continue

            // Calculate time adjustment in seconds (more precise than minutes)
            val degreeChange = nadiOffset * NADI_PART_DEGREES
            val timeChangeSeconds = (degreeChange / AVG_ASCENDANT_SPEED_DEG_PER_MIN) * 60.0

            // For negative offsets, we need to go back in time to earlier Nadi
            val adjustedTime = chart.birthData.dateTime.plusSeconds(timeChangeSeconds.toLong())

            // Calculate the new Nadi number for the target segment
            val newNadiNumber = if (isOddSign) {
                (targetSegment + 1).coerceIn(1, NADIS_PER_SIGN)
            } else {
                (NADIS_PER_SIGN - targetSegment).coerceIn(1, NADIS_PER_SIGN)
            }

            // Calculate D-150 sign for the new Nadi
            val newNadiSignIndex = (signIndex + newNadiNumber - 1) % 12
            val newNadiSign = ZodiacSign.entries[newNadiSignIndex]

            // Determine if this is near a Nadi boundary
            // Close to boundary if within first or last 10% of current Nadi
            val isNearBoundary = positionInNadi < (NADI_PART_DEGREES * 0.1) ||
                                 positionInNadi > (NADI_PART_DEGREES * 0.9)

            // Confidence decreases with distance from current position
            // Higher confidence for nearby Nadis and when near boundaries
            val distanceFactor = 100 - (abs(nadiOffset) * 12)
            val boundaryBonus = if (isNearBoundary && abs(nadiOffset) == 1) 10 else 0
            val confidence = (distanceFactor + boundaryBonus).coerceIn(10, 100)

            candidates.add(
                RectificationCandidate(
                    timeAdjustmentSeconds = timeChangeSeconds.toInt(),
                    adjustedTime = adjustedTime,
                    nadiNumber = newNadiNumber,
                    nadiSign = newNadiSign,
                    description = "Adjust ${if (timeChangeSeconds > 0) "+" else ""}${timeChangeSeconds.toInt()}s → Nadi #$newNadiNumber (${newNadiSign.displayName})",
                    descriptionNe = "${if (timeChangeSeconds > 0) "+" else ""}${timeChangeSeconds.toInt()} सेकेन्ड → नाडी #$newNadiNumber (${newNadiSign.getLocalizedName(Language.NEPALI)})",
                    confidence = confidence,
                    isNadiBoundary = isNearBoundary && abs(nadiOffset) == 1
                )
            )
        }

        // Sort by confidence (highest first)
        return candidates.sortedByDescending { it.confidence }
    }

    /**
     * Get the Nadi for a specific degree (utility method for external use).
     *
     * @param longitude The longitude in degrees
     * @return The Nadi number (1-150)
     */
    fun getNadiNumberForLongitude(longitude: Double): Int {
        val normalizedLong = VedicAstrologyUtils.normalizeLongitude(longitude)
        val signIndex = floor(normalizedLong / DEGREES_IN_SIGN).toInt().coerceIn(0, 11)
        val sign = ZodiacSign.entries[signIndex]
        val degreeInSign = normalizedLong - (signIndex * DEGREES_IN_SIGN)
        val isOddSign = (sign.number % 2 != 0)
        val rawSegment = floor(degreeInSign / NADI_PART_DEGREES).toInt()

        return if (isOddSign) {
            (rawSegment + 1).coerceIn(1, NADIS_PER_SIGN)
        } else {
            (NADIS_PER_SIGN - rawSegment).coerceIn(1, NADIS_PER_SIGN)
        }
    }

    /**
     * Get estimated time (in seconds) for Ascendant to cross one Nadi.
     *
     * This is an average value; actual crossing time varies with:
     * - Latitude of birth place
     * - Local sidereal time
     * - Whether Ascendant is in a sign of short or long ascension
     *
     * @return Average seconds per Nadi crossing
     */
    fun getAverageNadiCrossingSeconds(): Double = AVG_NADI_CROSSING_SECONDS

    /**
     * Check if two longitudes are in the same Nadi.
     *
     * Useful for comparing positions before and after time adjustments.
     *
     * @param longitude1 First longitude
     * @param longitude2 Second longitude
     * @return True if both are in the same Nadi
     */
    fun areSameNadi(longitude1: Double, longitude2: Double): Boolean {
        return getNadiNumberForLongitude(longitude1) == getNadiNumberForLongitude(longitude2) &&
               ZodiacSign.fromLongitude(longitude1) == ZodiacSign.fromLongitude(longitude2)
    }
}


