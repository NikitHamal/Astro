package com.astro.vajra.ephemeris

import com.astro.vajra.core.common.Language
import com.astro.vajra.core.common.StringKeyAdvanced
import com.astro.vajra.core.common.StringResources
import com.astro.vajra.core.common.getLocalizedName
import com.astro.vajra.core.model.Planet
import com.astro.vajra.core.model.PlanetPosition
import com.astro.vajra.core.model.VedicChart
import com.astro.vajra.core.model.ZodiacSign
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import kotlin.math.abs

/**
 * Nadi Amsha (D-150) Calculator
 *
 * The 150th division of a sign (30°) is 12 arc-minutes (0°12').
 * This is the finest division used for precise birth time rectification and prediction.
 *
 * Rules:
 * - Odd Signs (1, 3, 5...): Count from the start (0° to 30°)
 * - Even Signs (2, 4, 6...): Count from the end (30° to 0°) - standard Apasavya
 *
 * @author AstroVajra
 */
object NadiAmshaCalculator {

    private const val DEGREES_IN_SIGN = 30.0
    private const val NADI_PART_DEGREES = 30.0 / 150.0 // 0.2 degrees or 12 minutes

    data class NadiAmshaResult(
        val chart: VedicChart,
        val ascendantNadi: NadiPosition,
        val planetNadis: List<NadiPosition>,
        val rectificationCandidates: List<RectificationCandidate>
    )

    data class NadiPosition(
        val planet: Planet?, // Null for Ascendant
        val startSign: ZodiacSign,
        val longitude: Double,
        val nadiNumber: Int, // 1-150
        val nadiSign: ZodiacSign,
        val nadiLord: Planet, // Ruler of the Nadi sign
        val description: String,
        val descriptionNe: String,
        val energyType: NadiEnergyType
    )

    data class RectificationCandidate(
        val timeAdjustmentMinutes: Int,
        val adjustedTime: LocalDateTime,
        val nadiNumber: Int,
        val description: String,
        val confidence: Int // 0-100
    )

    enum class NadiEnergyType(val displayName: String, val displayNameNe: String) {
        MALE("Male Energy", "पुरुष ऊर्जा"),
        FEMALE("Female Energy", "स्त्री ऊर्जा")
    }

    /**
     * Calculate comprehensive Nadi Amsha analysis
     */
    fun calculateNadiAmsha(chart: VedicChart): NadiAmshaResult {
        // 1. Calculate Ascendant Nadi
        val ascendantNadi = calculateNadiPosition(null, chart.ascendant, chart.birthData.dateTime)

        // 2. Calculate Planet Nadis
        val planetNadis = chart.planetPositions.map { position ->
            calculateNadiPosition(position.planet, position.longitude, chart.birthData.dateTime)
        }

        // 3. Generate rectification candidates (simple +/- nearby Nadis)
        val rectificationCandidates = generateRectificationCandidates(chart)

        return NadiAmshaResult(
            chart = chart,
            ascendantNadi = ascendantNadi,
            planetNadis = planetNadis,
            rectificationCandidates = rectificationCandidates
        )
    }

    /**
     * Calculate Nadi position for a given longitude
     */
    private fun calculateNadiPosition(
        planet: Planet?,
        longitude: Double,
        dateTime: LocalDateTime
    ): NadiPosition {
        val normalizedLong = ((longitude % 360.0) + 360.0) % 360.0
        val signIndex = (normalizedLong / 30.0).toInt()
        val degreeInSign = normalizedLong % 30.0
        val sign = ZodiacSign.entries[signIndex]

        val isOddSign = (sign.number % 2 != 0)

        // Calculate Nadi number (1-150)
        // Odd signs: 1 at 0°, 150 at 30°
        // Even signs: 150 at 0°, 1 at 30° (Reverse)
        val rawSegment = (degreeInSign / NADI_PART_DEGREES).toInt() + 1
        val nadiNumber = if (isOddSign) {
            rawSegment.coerceIn(1, 150)
        } else {
            (151 - rawSegment).coerceIn(1, 150)
        }

        // D-150 sign mapping used by this engine:
        // cycle through signs from the source sign using the computed Nadi number.
        val nadiSignIndex = (signIndex + nadiNumber - 1) % 12
        val nadiSign = ZodiacSign.entries[nadiSignIndex]
        val nadiLord = nadiSign.ruler

        // Determine energy type (Alternating)
        // Often Chara (Movable) are mobile/male, Sthira (Fixed) are fixed/female?
        // Let's use simple odd/even nadi number mapping for variety
        val energyType = if (nadiNumber % 2 != 0) NadiAmshaCalculator.NadiEnergyType.MALE else NadiAmshaCalculator.NadiEnergyType.FEMALE

        return NadiPosition(
            planet = planet,
            startSign = sign,
            longitude = longitude,
            nadiNumber = nadiNumber,
            nadiSign = nadiSign,
            nadiLord = nadiLord,
            description = "Nadi #$nadiNumber in ${sign.displayName}", // Placeholder for specific Nadi name
            descriptionNe = "${sign.getLocalizedName(Language.NEPALI)} मा नाडी #$nadiNumber",
            energyType = energyType
        )
    }

    /**
     * Generate potential birth time rectifications based on Ascendant Nadi changes
     */
    private fun generateRectificationCandidates(chart: VedicChart): List<RectificationCandidate> {
        val candidates = mutableListOf<RectificationCandidate>()
        val currentAscendant = chart.ascendant
        val currentSignIndex = (currentAscendant / 30.0).toInt()
        val currentSign = ZodiacSign.entries[currentSignIndex]
        val degreeInSign = currentAscendant % 30.0

        // Approximate ascendant speed for first-pass rectification search.
        val avgAscendantSpeedPerMin = 0.25

        // Check +/- 5 Nadis (approx +/- 4 minutes)
        for (i in -3..3) {
            if (i == 0) continue // Skip current

            val timeShiftMinutes = (i * NADI_PART_DEGREES / avgAscendantSpeedPerMin).toInt()
            val adjustedTime = chart.birthData.dateTime.plusMinutes(timeShiftMinutes.toLong())

            // Estimate new Nadi
            // We are estimating the shift; for real rect we would recalculate Ascendant,
            // but here we just simulate the Nadi shift index
            val isOddSign = (currentSign.number % 2 != 0)
            val currentRawSegment = (degreeInSign / NADI_PART_DEGREES).toInt() + 1
            val currentNadiNumber = if (isOddSign) currentRawSegment else (151 - currentRawSegment)

            var newNadiNumber = currentNadiNumber + (if (isOddSign) i else -i)
            
            // Handle boundary crossing (very simplified, assumes same sign for small shifts)
            if (newNadiNumber in 1..150) {
                 candidates.add(
                    RectificationCandidate(
                        timeAdjustmentMinutes = timeShiftMinutes,
                        adjustedTime = adjustedTime,
                        nadiNumber = newNadiNumber,
                        description = "Shift to Nadi #$newNadiNumber",
                        confidence = 100 - (abs(i) * 15) // Confidence drops as we move away
                    )
                )
            }
        }

        return candidates
    }

    // Reference data for Nadi names could be added here in future
    // private val NADI_NAMES = listOf(...)
}


