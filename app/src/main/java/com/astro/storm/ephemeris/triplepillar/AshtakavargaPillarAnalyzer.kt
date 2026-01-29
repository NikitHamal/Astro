package com.astro.storm.ephemeris.triplepillar

import com.astro.storm.core.model.Planet
import com.astro.storm.core.model.VedicChart
import com.astro.storm.core.model.ZodiacSign
import com.astro.storm.ephemeris.AshtakavargaCalculator
import com.astro.storm.ephemeris.VedicAstrologyUtils
import java.time.LocalDateTime

/**
 * Ashtakavarga Pillar Analyzer
 *
 * Analyzes the Ashtakavarga system as the third pillar of the Triple-Pillar
 * Predictive Engine. This evaluates:
 *
 * 1. Bhinnashtakavarga (BAV) - Individual planet bindus in each sign
 * 2. Sarvashtakavarga (SAV) - Combined bindu totals per sign
 * 3. Transit effectiveness based on bindu scores
 * 4. Kaksha-level analysis for precise timing
 * 5. Shodhana (reduction) for refined predictions
 *
 * Classical References:
 * - Brihat Parashara Hora Shastra (Ch. 66-72)
 * - Jataka Parijata (Ch. 7)
 * - Hora Makarandam
 *
 * Key Principles:
 * - Signs with SAV ≥ 28 are favorable for transits
 * - BAV ≥ 4 indicates strong planet in that sign
 * - Kaksha divisions (3°45' each) pinpoint exact timing
 *
 * @author AstroStorm - Ultra-Precision Vedic Astrology
 */
object AshtakavargaPillarAnalyzer {

    /**
     * Threshold for favorable SAV (traditionally 28 out of 56 max theoretical)
     */
    private const val SAV_FAVORABLE_THRESHOLD = 28

    /**
     * Threshold for weak SAV
     */
    private const val SAV_WEAK_THRESHOLD = 22

    /**
     * Threshold for favorable BAV (4 out of 8)
     */
    private const val BAV_FAVORABLE_THRESHOLD = 4

    /**
     * Threshold for weak BAV
     */
    private const val BAV_WEAK_THRESHOLD = 2

    /**
     * Analyze the Ashtakavarga Pillar
     */
    fun analyzeAshtakavargaPillar(
        natalChart: VedicChart,
        transitPositions: Map<Planet, TransitPillarAnalysis.TransitPosition>,
        dashaLords: List<Planet>
    ): AshtakavargaPillarAnalysis {
        // Calculate Ashtakavarga for the natal chart
        val ashtakavargaAnalysis = AshtakavargaCalculator.calculateAshtakavarga(natalChart)

        // Get SAV distribution
        val sarvashtakavargaDistribution = ZodiacSign.entries.associateWith { sign ->
            ashtakavargaAnalysis.sarvashtakavarga.getBindusForSign(sign)
        }

        // Calculate transit scores for each planet
        val currentTransitScores = calculateTransitScores(
            ashtakavargaAnalysis = ashtakavargaAnalysis,
            transitPositions = transitPositions
        )

        // Find strong and weak houses
        val ascendantSign = ZodiacSign.fromLongitude(natalChart.ascendant)
        val strongHouses = findStrongHouses(sarvashtakavargaDistribution, ascendantSign)
        val weakHouses = findWeakHouses(sarvashtakavargaDistribution, ascendantSign)

        // Calculate Dasha Lord BAV in their transit signs
        val dashaLordBav = calculateDashaLordBav(
            ashtakavargaAnalysis = ashtakavargaAnalysis,
            dashaLords = dashaLords,
            transitPositions = transitPositions
        )

        // Calculate overall Ashtakavarga score
        val overallScore = calculateOverallAshtakavargaScore(
            currentTransitScores = currentTransitScores,
            sarvashtakavargaDistribution = sarvashtakavargaDistribution,
            transitPositions = transitPositions,
            dashaLordBav = dashaLordBav
        )

        return AshtakavargaPillarAnalysis(
            currentTransitScores = currentTransitScores,
            sarvashtakavargaDistribution = sarvashtakavargaDistribution,
            strongHouses = strongHouses,
            weakHouses = weakHouses,
            dashaLordBav = dashaLordBav,
            overallAshtakavargaScore = overallScore
        )
    }

    /**
     * Calculate transit scores using Ashtakavarga
     */
    private fun calculateTransitScores(
        ashtakavargaAnalysis: AshtakavargaCalculator.AshtakavargaAnalysis,
        transitPositions: Map<Planet, TransitPillarAnalysis.TransitPosition>
    ): Map<Planet, AshtakavargaPillarAnalysis.AshtakavargaTransitScore> {
        val scores = mutableMapOf<Planet, AshtakavargaPillarAnalysis.AshtakavargaTransitScore>()

        // Ashtakavarga is calculated for 7 planets (not Rahu/Ketu)
        val ashtakavargaPlanets = listOf(
            Planet.SUN, Planet.MOON, Planet.MARS, Planet.MERCURY,
            Planet.JUPITER, Planet.VENUS, Planet.SATURN
        )

        for (planet in ashtakavargaPlanets) {
            val transitPos = transitPositions[planet] ?: continue
            val transitSign = transitPos.sign

            // Get BAV score for this planet in transit sign
            val bav = ashtakavargaAnalysis.bhinnashtakavarga[planet]
            val bavScore = bav?.getBindusForSign(transitSign) ?: 0

            // Get SAV score for transit sign
            val savScore = ashtakavargaAnalysis.sarvashtakavarga.getBindusForSign(transitSign)

            // Determine quality
            val quality = determineTransitQuality(bavScore, savScore)

            // Generate interpretation
            val interpretation = generateTransitInterpretation(planet, transitSign, bavScore, savScore, quality)

            scores[planet] = AshtakavargaPillarAnalysis.AshtakavargaTransitScore(
                planet = planet,
                transitSign = transitSign,
                bavScore = bavScore,
                savScore = savScore,
                quality = quality,
                interpretation = interpretation
            )
        }

        return scores
    }

    /**
     * Determine transit quality based on BAV and SAV scores
     */
    private fun determineTransitQuality(bavScore: Int, savScore: Int): TransitQuality {
        return when {
            bavScore >= 5 && savScore >= 30 -> TransitQuality.EXCELLENT
            bavScore >= BAV_FAVORABLE_THRESHOLD && savScore >= SAV_FAVORABLE_THRESHOLD -> TransitQuality.GOOD
            bavScore >= 3 && savScore >= 25 -> TransitQuality.AVERAGE
            bavScore >= BAV_WEAK_THRESHOLD && savScore >= SAV_WEAK_THRESHOLD -> TransitQuality.CHALLENGING
            else -> TransitQuality.DIFFICULT
        }
    }

    /**
     * Generate transit interpretation
     */
    private fun generateTransitInterpretation(
        planet: Planet,
        sign: ZodiacSign,
        bavScore: Int,
        savScore: Int,
        quality: TransitQuality
    ): String {
        val planetName = planet.displayName
        val signName = sign.displayName

        val bavDescription = when {
            bavScore >= 5 -> "strong ($bavScore/8 bindus)"
            bavScore >= 4 -> "favorable ($bavScore/8 bindus)"
            bavScore >= 3 -> "moderate ($bavScore/8 bindus)"
            bavScore >= 2 -> "weak ($bavScore/8 bindus)"
            else -> "very weak ($bavScore/8 bindus)"
        }

        val savDescription = when {
            savScore >= 30 -> "highly supportive ($savScore SAV)"
            savScore >= 28 -> "supportive ($savScore SAV)"
            savScore >= 25 -> "moderate ($savScore SAV)"
            savScore >= 22 -> "limited ($savScore SAV)"
            else -> "challenging ($savScore SAV)"
        }

        val effectDescription = getEffectDescription(planet, quality)

        return "$planetName transiting $signName has $bavDescription personal strength and $savDescription overall sign strength. $effectDescription"
    }

    /**
     * Get effect description based on planet and quality
     */
    private fun getEffectDescription(planet: Planet, quality: TransitQuality): String {
        val planetEffects = when (planet) {
            Planet.SUN -> "authority, health, father, government matters"
            Planet.MOON -> "mind, emotions, mother, public perception"
            Planet.MARS -> "energy, courage, property, siblings"
            Planet.MERCURY -> "communication, business, education, intellect"
            Planet.JUPITER -> "wisdom, fortune, children, expansion"
            Planet.VENUS -> "relationships, luxury, arts, vehicles"
            Planet.SATURN -> "discipline, career, longevity, delays"
            else -> "general matters"
        }

        return when (quality) {
            TransitQuality.EXCELLENT -> "Excellent results expected in $planetEffects."
            TransitQuality.GOOD -> "Favorable results in $planetEffects."
            TransitQuality.AVERAGE -> "Mixed results in $planetEffects."
            TransitQuality.CHALLENGING -> "Challenges in $planetEffects. Exercise patience."
            TransitQuality.DIFFICULT -> "Difficult period for $planetEffects. Remedies recommended."
        }
    }

    /**
     * Find houses with strong SAV (28+)
     */
    private fun findStrongHouses(
        savDistribution: Map<ZodiacSign, Int>,
        ascendantSign: ZodiacSign
    ): List<Int> {
        return savDistribution
            .filter { it.value >= SAV_FAVORABLE_THRESHOLD }
            .map { VedicAstrologyUtils.getHouseFromSigns(it.key, ascendantSign) }
            .sorted()
    }

    /**
     * Find houses with weak SAV (<22)
     */
    private fun findWeakHouses(
        savDistribution: Map<ZodiacSign, Int>,
        ascendantSign: ZodiacSign
    ): List<Int> {
        return savDistribution
            .filter { it.value < SAV_WEAK_THRESHOLD }
            .map { VedicAstrologyUtils.getHouseFromSigns(it.key, ascendantSign) }
            .sorted()
    }

    /**
     * Calculate Dasha Lord's BAV in their current transit sign
     */
    private fun calculateDashaLordBav(
        ashtakavargaAnalysis: AshtakavargaCalculator.AshtakavargaAnalysis,
        dashaLords: List<Planet>,
        transitPositions: Map<Planet, TransitPillarAnalysis.TransitPosition>
    ): Map<Planet, Int> {
        val result = mutableMapOf<Planet, Int>()

        for (dashaLord in dashaLords) {
            // Skip Rahu/Ketu as they don't have traditional BAV
            if (dashaLord == Planet.RAHU || dashaLord == Planet.KETU) continue

            val transitPos = transitPositions[dashaLord] ?: continue
            val bav = ashtakavargaAnalysis.bhinnashtakavarga[dashaLord]
            val bavScore = bav?.getBindusForSign(transitPos.sign) ?: 0
            result[dashaLord] = bavScore
        }

        return result
    }

    /**
     * Calculate overall Ashtakavarga score for the transit period
     */
    private fun calculateOverallAshtakavargaScore(
        currentTransitScores: Map<Planet, AshtakavargaPillarAnalysis.AshtakavargaTransitScore>,
        sarvashtakavargaDistribution: Map<ZodiacSign, Int>,
        transitPositions: Map<Planet, TransitPillarAnalysis.TransitPosition>,
        dashaLordBav: Map<Planet, Int>
    ): Double {
        var totalScore = 0.0
        var totalWeight = 0.0

        // Weight slow-moving planets more heavily
        val planetWeights = mapOf(
            Planet.SATURN to 1.0,
            Planet.JUPITER to 0.9,
            Planet.MARS to 0.5,
            Planet.SUN to 0.4,
            Planet.VENUS to 0.35,
            Planet.MERCURY to 0.3,
            Planet.MOON to 0.2
        )

        // Score from BAV of transiting planets
        for ((planet, score) in currentTransitScores) {
            val weight = planetWeights[planet] ?: 0.3
            val normalizedBav = score.bavScore / 8.0
            val normalizedSav = score.savScore / 56.0

            // Combined BAV and SAV score (70% BAV, 30% SAV for planet-specific prediction)
            val combinedScore = (normalizedBav * 0.7) + (normalizedSav * 0.3)

            totalScore += combinedScore * weight
            totalWeight += weight
        }

        // Additional score from Dasha Lord BAV
        var dashaLordScore = 0.0
        var dashaLordCount = 0
        for ((planet, bavScore) in dashaLordBav) {
            dashaLordScore += bavScore / 8.0
            dashaLordCount++
        }
        if (dashaLordCount > 0) {
            val avgDashaLordScore = dashaLordScore / dashaLordCount
            // Dasha lord's transit strength gets 20% weight
            totalScore = (totalScore / totalWeight) * 0.8 + avgDashaLordScore * 0.2
            return totalScore.coerceIn(0.0, 1.0)
        }

        return if (totalWeight > 0) (totalScore / totalWeight).coerceIn(0.0, 1.0) else 0.5
    }

    /**
     * Calculate Kaksha-level transit timing
     *
     * Each sign is divided into 8 Kakshas of 3°45' each, ruled by:
     * 1. Saturn (0° - 3°45')
     * 2. Jupiter (3°45' - 7°30')
     * 3. Mars (7°30' - 11°15')
     * 4. Sun (11°15' - 15°)
     * 5. Venus (15° - 18°45')
     * 6. Mercury (18°45' - 22°30')
     * 7. Moon (22°30' - 26°15')
     * 8. Ascendant (26°15' - 30°)
     *
     * Transit through a Kaksha ruled by a planet that gives bindu in that sign
     * is favorable; otherwise unfavorable.
     */
    fun calculateKakshaTransit(
        transitingPlanet: Planet,
        transitLongitude: Double,
        ashtakavargaAnalysis: AshtakavargaCalculator.AshtakavargaAnalysis
    ): KakshaTransitResult {
        val normalizedLong = VedicAstrologyUtils.normalizeLongitude(transitLongitude)
        val sign = ZodiacSign.fromLongitude(normalizedLong)
        val degreeInSign = normalizedLong % 30.0

        // Calculate Kaksha number (1-8)
        val kakshaSize = 3.75 // 30° / 8
        val kakshaNumber = ((degreeInSign / kakshaSize).toInt() + 1).coerceIn(1, 8)

        // Get Kaksha lord
        val kakshaLords = listOf(
            Planet.SATURN, Planet.JUPITER, Planet.MARS, Planet.SUN,
            Planet.VENUS, Planet.MERCURY, Planet.MOON
        )
        val kakshaLord = if (kakshaNumber <= 7) kakshaLords[kakshaNumber - 1] else null

        // Check if Kaksha lord gives bindu to transiting planet in this sign
        val isBeneficial = if (kakshaLord != null && transitingPlanet != Planet.RAHU && transitingPlanet != Planet.KETU) {
            val prastara = ashtakavargaAnalysis.prastarashtakavarga[transitingPlanet]
            prastara?.doesContributorGiveBindu(sign, kakshaLord.displayName) ?: false
        } else {
            // For 8th Kaksha (Ascendant) or nodes, check SAV
            ashtakavargaAnalysis.sarvashtakavarga.getBindusForSign(sign) >= SAV_FAVORABLE_THRESHOLD
        }

        // Calculate time remaining in this Kaksha (approximate)
        val degreeInKaksha = degreeInSign % kakshaSize
        val degreesRemaining = kakshaSize - degreeInKaksha

        return KakshaTransitResult(
            planet = transitingPlanet,
            sign = sign,
            kakshaNumber = kakshaNumber,
            kakshaLord = kakshaLord,
            isBeneficial = isBeneficial,
            degreesRemaining = degreesRemaining,
            interpretation = generateKakshaInterpretation(transitingPlanet, kakshaNumber, kakshaLord, isBeneficial)
        )
    }

    /**
     * Generate Kaksha interpretation
     */
    private fun generateKakshaInterpretation(
        planet: Planet,
        kakshaNumber: Int,
        kakshaLord: Planet?,
        isBeneficial: Boolean
    ): String {
        val kakshaLordName = kakshaLord?.displayName ?: "Ascendant"

        return if (isBeneficial) {
            "${planet.displayName} is transiting the ${kakshaNumber}th Kaksha ruled by $kakshaLordName, " +
                    "which gives bindu support. This portion of the transit is favorable for ${planet.displayName}'s significations."
        } else {
            "${planet.displayName} is transiting the ${kakshaNumber}th Kaksha ruled by $kakshaLordName, " +
                    "which does not give bindu support. This portion of the transit may bring challenges in ${planet.displayName}'s significations."
        }
    }

    /**
     * Kaksha transit result
     */
    data class KakshaTransitResult(
        val planet: Planet,
        val sign: ZodiacSign,
        val kakshaNumber: Int,
        val kakshaLord: Planet?,
        val isBeneficial: Boolean,
        val degreesRemaining: Double,
        val interpretation: String
    )
}
