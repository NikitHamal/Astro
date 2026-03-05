package com.astro.vajra.ephemeris.matchmaking

import com.astro.vajra.core.model.AdditionalFactors
import com.astro.vajra.core.model.GunaAnalysis
import com.astro.vajra.core.model.GunaType
import com.astro.vajra.core.model.ManglikAnalysis
import com.astro.vajra.core.model.Planet
import com.astro.vajra.core.model.VedicChart
import com.astro.vajra.core.model.ZodiacSign
import com.astro.vajra.ephemeris.DivisionalChartCalculator
import com.astro.vajra.ephemeris.VedicAstrologyUtils
import com.astro.vajra.ephemeris.VedicAstrologyUtils.PlanetaryRelationship
import kotlin.math.abs

data class MatchmakingCalibration(
    val calibratedCompatibilityScore: Double,
    val practicalCompatibilityScore: Double,
    val relationshipReadinessScore: Double
)

object MatchmakingPrecisionCalibrator {

    fun calibrate(
        brideChart: VedicChart,
        groomChart: VedicChart,
        gunaAnalyses: List<GunaAnalysis>,
        brideManglik: ManglikAnalysis,
        groomManglik: ManglikAnalysis,
        additionalFactors: AdditionalFactors
    ): MatchmakingCalibration {
        val gunaQualityScore = calculateGunaQualityScore(gunaAnalyses)
        val nadiScore = gunaAnalyses.find { it.gunaType == GunaType.NADI }?.obtainedPoints ?: 0.0
        val bhakootScore = gunaAnalyses.find { it.gunaType == GunaType.BHAKOOT }?.obtainedPoints ?: 0.0

        val manglikScore = calculateManglikBalanceScore(brideManglik, groomManglik)
        val additionalFactorScore = calculateAdditionalFactorScore(additionalFactors)
        val navamsaScore = calculateNavamsaResonance(brideChart, groomChart)
        val practicalScore = calculatePracticalScore(brideChart, groomChart)

        var calibrated = (
            gunaQualityScore * 0.56 +
                manglikScore * 0.14 +
                navamsaScore * 0.18 +
                additionalFactorScore * 0.12
            )

        if (nadiScore == 0.0 && bhakootScore == 0.0) {
            calibrated *= 0.74
        }

        val readiness = (calibrated * 0.75 + practicalScore * 0.25)
            .coerceIn(0.0, 100.0)

        return MatchmakingCalibration(
            calibratedCompatibilityScore = calibrated.coerceIn(0.0, 100.0),
            practicalCompatibilityScore = practicalScore,
            relationshipReadinessScore = readiness
        )
    }

    private fun calculateGunaQualityScore(gunaAnalyses: List<GunaAnalysis>): Double {
        if (gunaAnalyses.isEmpty()) return 0.0

        val weights = mapOf(
            GunaType.VARNA to 0.06,
            GunaType.VASHYA to 0.07,
            GunaType.TARA to 0.09,
            GunaType.YONI to 0.12,
            GunaType.GRAHA_MAITRI to 0.15,
            GunaType.GANA to 0.13,
            GunaType.BHAKOOT to 0.18,
            GunaType.NADI to 0.20
        )

        var weighted = 0.0
        gunaAnalyses.forEach { guna ->
            val ratio = if (guna.maxPoints > 0.0) guna.obtainedPoints / guna.maxPoints else 0.0
            weighted += ratio * (weights[guna.gunaType] ?: 0.0)
        }

        var score = weighted * 100.0

        val nadi = gunaAnalyses.find { it.gunaType == GunaType.NADI }?.obtainedPoints ?: 0.0
        val bhakoot = gunaAnalyses.find { it.gunaType == GunaType.BHAKOOT }?.obtainedPoints ?: 0.0
        val gana = gunaAnalyses.find { it.gunaType == GunaType.GANA }?.obtainedPoints ?: 0.0
        val yoni = gunaAnalyses.find { it.gunaType == GunaType.YONI }?.obtainedPoints ?: 0.0
        val maitri = gunaAnalyses.find { it.gunaType == GunaType.GRAHA_MAITRI }?.obtainedPoints ?: 0.0
        val tara = gunaAnalyses.find { it.gunaType == GunaType.TARA }?.obtainedPoints ?: 0.0

        if (nadi == 0.0) score -= 18.0
        if (bhakoot == 0.0) score -= 14.0
        if (gana <= 1.0) score -= 8.0
        if (yoni == 0.0) score -= 7.0
        if (maitri >= 4.0) score += 4.0
        if (tara >= 1.5) score += 2.0

        return score.coerceIn(0.0, 100.0)
    }

    private fun calculateManglikBalanceScore(bride: ManglikAnalysis, groom: ManglikAnalysis): Double {
        val s1 = bride.effectiveDosha.severity
        val s2 = groom.effectiveDosha.severity

        if (s1 == 0 && s2 == 0) return 86.0

        if (s1 > 0 && s2 > 0) {
            val diff = abs(s1 - s2)
            return (82.0 - (diff * 11.0)).coerceIn(42.0, 90.0)
        }

        val maxSeverity = maxOf(s1, s2)
        return (56.0 - (maxSeverity * 9.0)).coerceIn(20.0, 56.0)
    }

    private fun calculateAdditionalFactorScore(factors: AdditionalFactors): Double {
        var score = 50.0

        score += if (!factors.vedhaPresent) 10.0 else -14.0
        score += if (factors.rajjuCompatible) 16.0 else -20.0
        score += if (factors.streeDeerghaSatisfied) 10.0 else -12.0
        score += if (factors.mahendraSatisfied) 8.0 else -3.0

        return score.coerceIn(0.0, 100.0)
    }

    private fun calculateNavamsaResonance(brideChart: VedicChart, groomChart: VedicChart): Double {
        val brideD9 = DivisionalChartCalculator.calculateNavamsa(brideChart)
        val groomD9 = DivisionalChartCalculator.calculateNavamsa(groomChart)

        val brideAsc = brideD9.ascendantSign
        val groomAsc = groomD9.ascendantSign

        val bride7thLord = ZodiacSign.entries[(brideAsc.ordinal + 6) % 12].ruler
        val groom7thLord = ZodiacSign.entries[(groomAsc.ordinal + 6) % 12].ruler

        val relationship7th = relationshipScore(bride7thLord, groom7thLord)

        val brideVenus = brideD9.planetPositions.find { it.planet == Planet.VENUS }
        val groomVenus = groomD9.planetPositions.find { it.planet == Planet.VENUS }
        val venusAlignment = if (brideVenus != null && groomVenus != null) {
            val signDistance = VedicAstrologyUtils.getHouseFromSigns(groomVenus.sign, brideVenus.sign)
            when (signDistance) {
                1, 5, 7, 9 -> 84.0
                3, 4, 10, 11 -> 72.0
                else -> 52.0
            }
        } else {
            60.0
        }

        val moonPairScore = calculateMoonPairScore(brideChart, groomChart)

        return (relationship7th * 0.45 + venusAlignment * 0.35 + moonPairScore * 0.20)
            .coerceIn(0.0, 100.0)
    }

    private fun calculatePracticalScore(brideChart: VedicChart, groomChart: VedicChart): Double {
        val brideMercury = VedicAstrologyUtils.getPlanetPosition(brideChart, Planet.MERCURY)
        val groomMercury = VedicAstrologyUtils.getPlanetPosition(groomChart, Planet.MERCURY)
        val brideJupiter = VedicAstrologyUtils.getPlanetPosition(brideChart, Planet.JUPITER)
        val groomJupiter = VedicAstrologyUtils.getPlanetPosition(groomChart, Planet.JUPITER)
        val brideMars = VedicAstrologyUtils.getPlanetPosition(brideChart, Planet.MARS)
        val groomMars = VedicAstrologyUtils.getPlanetPosition(groomChart, Planet.MARS)

        val communication = if (brideMercury != null && groomMercury != null) {
            val rel = relationshipScore(brideMercury.sign.ruler, groomMercury.sign.ruler)
            val distance = VedicAstrologyUtils.getHouseFromSigns(groomMercury.sign, brideMercury.sign)
            (rel * 0.7 + houseDistancePracticalWeight(distance) * 0.3).coerceIn(0.0, 100.0)
        } else {
            58.0
        }

        val valuesAndFamily = if (brideJupiter != null && groomJupiter != null) {
            val rel = relationshipScore(brideJupiter.sign.ruler, groomJupiter.sign.ruler)
            val distance = VedicAstrologyUtils.getHouseFromSigns(groomJupiter.sign, brideJupiter.sign)
            (rel * 0.65 + houseDistancePracticalWeight(distance) * 0.35).coerceIn(0.0, 100.0)
        } else {
            60.0
        }

        val conflictStyle = if (brideMars != null && groomMars != null) {
            val distance = VedicAstrologyUtils.getHouseFromSigns(groomMars.sign, brideMars.sign)
            when (distance) {
                1, 5, 9 -> 72.0
                3, 11 -> 65.0
                6, 8, 12 -> 38.0
                else -> 55.0
            }
        } else {
            55.0
        }

        return (communication * 0.40 + valuesAndFamily * 0.35 + conflictStyle * 0.25)
            .coerceIn(0.0, 100.0)
    }

    private fun calculateMoonPairScore(brideChart: VedicChart, groomChart: VedicChart): Double {
        val brideMoon = VedicAstrologyUtils.getMoonPosition(brideChart)
        val groomMoon = VedicAstrologyUtils.getMoonPosition(groomChart)

        if (brideMoon == null || groomMoon == null) return 60.0

        val moonDistance = VedicAstrologyUtils.getHouseFromSigns(groomMoon.sign, brideMoon.sign)
        return when (moonDistance) {
            1, 5, 7, 9 -> 82.0
            3, 4, 10, 11 -> 70.0
            2, 12 -> 52.0
            6, 8 -> 40.0
            else -> 58.0
        }
    }

    private fun houseDistancePracticalWeight(distance: Int): Double = when (distance) {
        1, 5, 7, 9 -> 85.0
        3, 4, 10, 11 -> 72.0
        2, 12 -> 56.0
        6, 8 -> 38.0
        else -> 58.0
    }

    private fun relationshipScore(lord1: Planet, lord2: Planet): Double {
        val rel1 = VedicAstrologyUtils.getNaturalRelationship(lord1, lord2)
        val rel2 = VedicAstrologyUtils.getNaturalRelationship(lord2, lord1)

        return when {
            rel1 == PlanetaryRelationship.FRIEND && rel2 == PlanetaryRelationship.FRIEND -> 90.0
            (rel1 == PlanetaryRelationship.FRIEND && rel2 == PlanetaryRelationship.NEUTRAL) ||
                (rel1 == PlanetaryRelationship.NEUTRAL && rel2 == PlanetaryRelationship.FRIEND) -> 76.0
            rel1 == PlanetaryRelationship.NEUTRAL && rel2 == PlanetaryRelationship.NEUTRAL -> 66.0
            (rel1 == PlanetaryRelationship.ENEMY && rel2 == PlanetaryRelationship.NEUTRAL) ||
                (rel1 == PlanetaryRelationship.NEUTRAL && rel2 == PlanetaryRelationship.ENEMY) -> 46.0
            else -> 28.0
        }
    }
}
