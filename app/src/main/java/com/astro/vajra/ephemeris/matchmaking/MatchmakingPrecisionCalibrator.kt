package com.astro.vajra.ephemeris.matchmaking

import com.astro.vajra.core.model.AdditionalFactors
import com.astro.vajra.core.model.GunaAnalysis
import com.astro.vajra.core.model.GunaType
import com.astro.vajra.core.model.ManglikAnalysis
import com.astro.vajra.core.model.Planet
import com.astro.vajra.core.model.PlanetPosition
import com.astro.vajra.core.model.VedicChart
import com.astro.vajra.core.model.ZodiacSign
import com.astro.vajra.ephemeris.ArudhaPadaCalculator
import com.astro.vajra.ephemeris.DivisionalChartCalculator
import com.astro.vajra.ephemeris.VedicAstrologyUtils
import com.astro.vajra.ephemeris.VedicAstrologyUtils.PlanetaryRelationship
import com.astro.vajra.ephemeris.jaimini.JaiminiKarakaCalculator
import kotlin.math.abs

data class MatchmakingCalibration(
    val calibratedCompatibilityScore: Double,
    val practicalCompatibilityScore: Double,
    val relationshipReadinessScore: Double
)

object MatchmakingPrecisionCalibrator {
    private data class MarriageSignature(
        val promiseScore: Double,
        val stabilityScore: Double,
        val practicalBaseScore: Double,
        val seventhHouseScore: Double,
        val seventhLordScore: Double,
        val navamsaSeventhLordScore: Double,
        val upapadaScore: Double,
        val darakarakaScore: Double,
        val venusScore: Double,
        val jupiterScore: Double,
        val moonScore: Double,
        val mercuryScore: Double,
        val ulSign: ZodiacSign?,
        val darakarakaPlanet: Planet?,
        val darakarakaSign: ZodiacSign?,
        val moonSign: ZodiacSign?,
        val mercurySign: ZodiacSign?,
        val jupiterSign: ZodiacSign?,
        val venusSign: ZodiacSign?,
        val marsSign: ZodiacSign?,
        val saturnSign: ZodiacSign?,
        val seventhLord: Planet?,
        val navamsaSeventhLord: Planet?
    )

    fun calibrate(
        brideChart: VedicChart,
        groomChart: VedicChart,
        gunaAnalyses: List<GunaAnalysis>,
        brideManglik: ManglikAnalysis,
        groomManglik: ManglikAnalysis,
        additionalFactors: AdditionalFactors
    ): MatchmakingCalibration {
        val gunaFoundationScore = calculateGunaFoundationScore(gunaAnalyses)
        val nadiScore = gunaAnalyses.find { it.gunaType == GunaType.NADI }?.obtainedPoints ?: 0.0
        val bhakootScore = gunaAnalyses.find { it.gunaType == GunaType.BHAKOOT }?.obtainedPoints ?: 0.0

        val brideSignature = buildMarriageSignature(brideChart)
        val groomSignature = buildMarriageSignature(groomChart)

        val marriagePromiseScore = averageOf(brideSignature.promiseScore, groomSignature.promiseScore)
        val chartResonanceScore = calculateCrossChartMarriageResonance(brideSignature, groomSignature)
        val manglikScore = calculateManglikBalanceScore(brideManglik, groomManglik)
        val additionalFactorScore = calculateAdditionalFactorScore(additionalFactors)
        val structuralStability = averageOf(brideSignature.stabilityScore, groomSignature.stabilityScore)
        val practicalScore = calculatePracticalScore(brideSignature, groomSignature)

        var calibrated = (
            gunaFoundationScore * 0.34 +
                marriagePromiseScore * 0.24 +
                chartResonanceScore * 0.22 +
                manglikScore * 0.10 +
                additionalFactorScore * 0.10
            ).coerceIn(0.0, 100.0)

        calibrated = (
            calibrated * 0.82 +
                structuralStability * 0.18
            ).coerceIn(0.0, 100.0)

        val severeBlockers = countSevereBlockers(
            gunaAnalyses = gunaAnalyses,
            additionalFactors = additionalFactors,
            manglikScore = manglikScore,
            brideSignature = brideSignature,
            groomSignature = groomSignature
        )
        val protectiveFactors = countProtectiveFactors(
            gunaAnalyses = gunaAnalyses,
            additionalFactors = additionalFactors,
            brideSignature = brideSignature,
            groomSignature = groomSignature,
            chartResonanceScore = chartResonanceScore
        )

        calibrated = applyDecisionCaps(
            score = calibrated,
            severeBlockers = severeBlockers,
            protectiveFactors = protectiveFactors
        )

        if (nadiScore == 0.0 && bhakootScore == 0.0) {
            calibrated = minOf(calibrated, 38.0)
        }

        val readiness = (calibrated * 0.84 + practicalScore * 0.16)
            .coerceIn(0.0, 100.0)

        return MatchmakingCalibration(
            calibratedCompatibilityScore = calibrated.coerceIn(0.0, 100.0),
            practicalCompatibilityScore = practicalScore,
            relationshipReadinessScore = readiness
        )
    }

    private fun calculateGunaFoundationScore(gunaAnalyses: List<GunaAnalysis>): Double {
        if (gunaAnalyses.isEmpty()) return 0.0

        val weights = mapOf(
            GunaType.VARNA to 0.04,
            GunaType.VASHYA to 0.07,
            GunaType.TARA to 0.10,
            GunaType.YONI to 0.13,
            GunaType.GRAHA_MAITRI to 0.17,
            GunaType.GANA to 0.13,
            GunaType.BHAKOOT to 0.18,
            GunaType.NADI to 0.18
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

        if (nadi == 0.0) score -= 16.0
        if (bhakoot == 0.0) score -= 13.0
        if (gana <= 1.0) score -= 7.0
        if (yoni == 0.0) score -= 6.0
        if (maitri >= 4.0) score += 5.0
        if (tara >= 1.5) score += 3.0

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

    private fun buildMarriageSignature(chart: VedicChart): MarriageSignature {
        val moon = VedicAstrologyUtils.getMoonPosition(chart)
        val mercury = VedicAstrologyUtils.getPlanetPosition(chart, Planet.MERCURY)
        val jupiter = VedicAstrologyUtils.getPlanetPosition(chart, Planet.JUPITER)
        val venus = VedicAstrologyUtils.getPlanetPosition(chart, Planet.VENUS)
        val mars = VedicAstrologyUtils.getPlanetPosition(chart, Planet.MARS)
        val saturn = VedicAstrologyUtils.getPlanetPosition(chart, Planet.SATURN)

        val seventhHouseScore = calculateSeventhHouseScore(chart)
        val seventhLordScore = calculateSeventhLordScore(chart)
        val navamsaSeventhLordScore = calculateNavamsaSeventhLordScore(chart)
        val upapadaScore = calculateUpapadaScore(chart)
        val darakarakaInfo = calculateDarakarakaInfo(chart)
        val venusScore = scoreRelationshipPlanet(venus)
        val jupiterScore = scoreRelationshipPlanet(jupiter)
        val moonScore = scoreRelationshipPlanet(moon)
        val mercuryScore = scoreMercuryForRelationship(mercury)

        val promiseScore = (
            seventhHouseScore * 0.23 +
                seventhLordScore * 0.19 +
                navamsaSeventhLordScore * 0.19 +
                upapadaScore * 0.16 +
                darakarakaInfo.first * 0.11 +
                venusScore * 0.07 +
                jupiterScore * 0.05
            ).coerceIn(0.0, 100.0)

        val stabilityScore = (
            moonScore * 0.28 +
                mercuryScore * 0.18 +
                venusScore * 0.18 +
                jupiterScore * 0.16 +
                calculateSaturnMaturityScore(saturn, moon, venus) * 0.20
            ).coerceIn(0.0, 100.0)

        val practicalBaseScore = (
            mercuryScore * 0.32 +
                moonScore * 0.24 +
                jupiterScore * 0.20 +
                venusScore * 0.14 +
                calculateMarsRegulationScore(mars, mercury, saturn) * 0.10
            ).coerceIn(0.0, 100.0)

        return MarriageSignature(
            promiseScore = promiseScore,
            stabilityScore = stabilityScore,
            practicalBaseScore = practicalBaseScore,
            seventhHouseScore = seventhHouseScore,
            seventhLordScore = seventhLordScore,
            navamsaSeventhLordScore = navamsaSeventhLordScore,
            upapadaScore = upapadaScore,
            darakarakaScore = darakarakaInfo.first,
            venusScore = venusScore,
            jupiterScore = jupiterScore,
            moonScore = moonScore,
            mercuryScore = mercuryScore,
            ulSign = calculateUpapadaSign(chart),
            darakarakaPlanet = darakarakaInfo.second,
            darakarakaSign = darakarakaInfo.third,
            moonSign = moon?.sign,
            mercurySign = mercury?.sign,
            jupiterSign = jupiter?.sign,
            venusSign = venus?.sign,
            marsSign = mars?.sign,
            saturnSign = saturn?.sign,
            seventhLord = calculateSeventhLord(chart),
            navamsaSeventhLord = calculateNavamsaSeventhLord(chart)
        )
    }

    private fun calculateCrossChartMarriageResonance(
        bride: MarriageSignature,
        groom: MarriageSignature
    ): Double {
        val moonHarmony = signPairHarmony(bride.moonSign, groom.moonSign)
        val seventhLordHarmony = pairPlanetRelationshipScore(bride.seventhLord, groom.seventhLord)
        val navamsaSeventhHarmony = pairPlanetRelationshipScore(bride.navamsaSeventhLord, groom.navamsaSeventhLord)
        val venusMarsHarmony = averageOf(
            signPairHarmony(bride.venusSign, groom.marsSign),
            signPairHarmony(groom.venusSign, bride.marsSign)
        )
        val upapadaHarmony = signPairHarmony(bride.ulSign, groom.ulSign)
        val darakarakaHarmony = averageOf(
            pairPlanetRelationshipScore(bride.darakarakaPlanet, groom.darakarakaPlanet),
            signPairHarmony(bride.darakarakaSign, groom.ulSign),
            signPairHarmony(groom.darakarakaSign, bride.ulSign)
        )

        return (
            moonHarmony * 0.22 +
                seventhLordHarmony * 0.20 +
                navamsaSeventhHarmony * 0.16 +
                venusMarsHarmony * 0.18 +
                upapadaHarmony * 0.14 +
                darakarakaHarmony * 0.10
            ).coerceIn(0.0, 100.0)
    }

    private fun calculatePracticalScore(
        bride: MarriageSignature,
        groom: MarriageSignature
    ): Double {
        val communication = averageOf(
            bride.practicalBaseScore,
            groom.practicalBaseScore,
            signPairHarmony(bride.mercurySign, groom.mercurySign),
            signPairHarmony(bride.moonSign, groom.mercurySign),
            signPairHarmony(groom.moonSign, bride.mercurySign)
        )
        val valuesAndFamily = averageOf(
            signPairHarmony(bride.jupiterSign, groom.jupiterSign),
            signPairHarmony(bride.moonSign, groom.moonSign),
            signPairHarmony(bride.ulSign, groom.ulSign),
            bride.upapadaScore,
            groom.upapadaScore
        )
        val conflictStyle = averageOf(
            signPairHarmony(bride.marsSign, groom.marsSign),
            signPairHarmony(bride.saturnSign, groom.saturnSign),
            calculateConflictRecoveryScore(bride, groom)
        )

        return (communication * 0.38 + valuesAndFamily * 0.36 + conflictStyle * 0.26)
            .coerceIn(0.0, 100.0)
    }

    private fun calculateSeventhHouseScore(chart: VedicChart): Double {
        var score = 58.0

        chart.planetPositions.filter { it.house == 7 }.forEach { position ->
            score += if (VedicAstrologyUtils.isNaturalBenefic(position.planet)) 8.0 else -9.0
            score += dignityAdjustment(VedicAstrologyUtils.getDignity(position)) * 0.35
        }

        chart.planetPositions
            .filter { it.house != 7 && VedicAstrologyUtils.aspectsHouse(it.planet, it.house, 7) }
            .forEach { position ->
                score += if (VedicAstrologyUtils.isNaturalBenefic(position.planet)) 4.0 else -5.0
            }

        return score.coerceIn(0.0, 100.0)
    }

    private fun calculateSeventhLord(chart: VedicChart): Planet {
        val asc = ZodiacSign.fromLongitude(chart.ascendant)
        return ZodiacSign.entries[(asc.ordinal + 6) % 12].ruler
    }

    private fun calculateNavamsaSeventhLord(chart: VedicChart): Planet {
        val d9 = DivisionalChartCalculator.calculateNavamsa(chart)
        return ZodiacSign.entries[(d9.ascendantSign.ordinal + 6) % 12].ruler
    }

    private fun calculateSeventhLordScore(chart: VedicChart): Double {
        val position = VedicAstrologyUtils.getPlanetPosition(chart, calculateSeventhLord(chart)) ?: return 54.0
        return scoreMarriageAnchor(position)
    }

    private fun calculateNavamsaSeventhLordScore(chart: VedicChart): Double {
        val d9 = DivisionalChartCalculator.calculateNavamsa(chart)
        val seventhLord = ZodiacSign.entries[(d9.ascendantSign.ordinal + 6) % 12].ruler
        val position = d9.planetPositions.find { it.planet == seventhLord } ?: return 55.0
        return scoreMarriageAnchor(position)
    }

    private fun calculateUpapadaScore(chart: VedicChart): Double {
        val analysis = runCatching { ArudhaPadaCalculator.analyzeArudhaPadas(chart) }.getOrNull()
            ?: return 58.0
        val upapada = analysis.specialArudhas.upapada
        val lordStrength = arudhaDignityScore(upapada.dignityOfLord)
        val beneficSupport = upapada.beneficsInArudha.size * 7.0
        val maleficPressure = upapada.maleficsInArudha.size * 8.0

        return (52.0 + lordStrength * 0.40 + beneficSupport - maleficPressure)
            .coerceIn(0.0, 100.0)
    }

    private fun calculateUpapadaSign(chart: VedicChart): ZodiacSign? {
        return runCatching { ArudhaPadaCalculator.analyzeArudhaPadas(chart).specialArudhas.upapada.arudha.sign }
            .getOrNull()
    }

    private fun calculateDarakarakaInfo(chart: VedicChart): Triple<Double, Planet?, ZodiacSign?> {
        val analysis = runCatching { JaiminiKarakaCalculator.calculateKarakas(chart) }.getOrNull()
            ?: return Triple(58.0, null, null)
        val darakaraka = analysis.getDarakaraka() ?: return Triple(58.0, null, null)
        val score = (
            48.0 +
                dignityAdjustment(darakaraka.dignity) * 0.55 +
                housePlacementAdjustment(darakaraka.house, favorMarriageHouses = true) * 0.45
            ).coerceIn(0.0, 100.0)
        return Triple(score, darakaraka.planet, darakaraka.sign)
    }

    private fun scoreRelationshipPlanet(position: PlanetPosition?): Double {
        if (position == null) return 58.0
        return (
            46.0 +
                dignityAdjustment(VedicAstrologyUtils.getDignity(position)) * 0.50 +
                housePlacementAdjustment(position.house, favorMarriageHouses = true) * 0.50
            ).coerceIn(0.0, 100.0)
    }

    private fun scoreMercuryForRelationship(position: PlanetPosition?): Double {
        if (position == null) return 58.0
        return (
            48.0 +
                dignityAdjustment(VedicAstrologyUtils.getDignity(position)) * 0.46 +
                housePlacementAdjustment(position.house, favorMarriageHouses = false) * 0.54
            ).coerceIn(0.0, 100.0)
    }

    private fun scoreMarriageAnchor(position: PlanetPosition): Double {
        val retrogradeAdjustment = if (position.isRetrograde) -3.0 else 0.0
        return (
            44.0 +
                dignityAdjustment(VedicAstrologyUtils.getDignity(position)) * 0.52 +
                housePlacementAdjustment(position.house, favorMarriageHouses = true) * 0.48 +
                retrogradeAdjustment
            ).coerceIn(0.0, 100.0)
    }

    private fun calculateSaturnMaturityScore(
        saturn: PlanetPosition?,
        moon: PlanetPosition?,
        venus: PlanetPosition?
    ): Double {
        if (saturn == null) return 62.0

        var pressure = 0
        if (moon != null && isSaturnAspectingSign(saturn.sign, moon.sign)) pressure++
        if (venus != null && isSaturnAspectingSign(saturn.sign, venus.sign)) pressure++

        val pressureScore = when (pressure) {
            0 -> 76.0
            1 -> 63.0
            else -> 49.0
        }

        return (pressureScore * 0.64 + dignityAdjustment(VedicAstrologyUtils.getDignity(saturn)) * 0.36)
            .coerceIn(0.0, 100.0)
    }

    private fun calculateMarsRegulationScore(
        mars: PlanetPosition?,
        mercury: PlanetPosition?,
        saturn: PlanetPosition?
    ): Double {
        if (mars == null) return 60.0

        val marsStrength = scoreRelationshipPlanet(mars)
        val mercuryModeration = if (mercury != null) signPairHarmony(mars.sign, mercury.sign) else 58.0
        val saturnDiscipline = if (saturn != null) signPairHarmony(mars.sign, saturn.sign) else 56.0

        return (marsStrength * 0.42 + mercuryModeration * 0.34 + saturnDiscipline * 0.24)
            .coerceIn(0.0, 100.0)
    }

    private fun calculateConflictRecoveryScore(
        bride: MarriageSignature,
        groom: MarriageSignature
    ): Double {
        val mercuryResilience = averageOf(
            signPairHarmony(bride.mercurySign, groom.marsSign),
            signPairHarmony(groom.mercurySign, bride.marsSign)
        )
        val moonResilience = signPairHarmony(bride.moonSign, groom.moonSign)
        val saturnResilience = signPairHarmony(bride.saturnSign, groom.saturnSign)

        return (mercuryResilience * 0.44 + moonResilience * 0.34 + saturnResilience * 0.22)
            .coerceIn(0.0, 100.0)
    }

    private fun countSevereBlockers(
        gunaAnalyses: List<GunaAnalysis>,
        additionalFactors: AdditionalFactors,
        manglikScore: Double,
        brideSignature: MarriageSignature,
        groomSignature: MarriageSignature
    ): Int {
        var blockers = 0

        if ((gunaAnalyses.find { it.gunaType == GunaType.NADI }?.obtainedPoints ?: 0.0) == 0.0) blockers++
        if ((gunaAnalyses.find { it.gunaType == GunaType.BHAKOOT }?.obtainedPoints ?: 0.0) == 0.0) blockers++
        if (!additionalFactors.rajjuCompatible) blockers++
        if (manglikScore < 45.0) blockers++
        if (minOf(brideSignature.promiseScore, groomSignature.promiseScore) < 44.0) blockers++

        return blockers
    }

    private fun countProtectiveFactors(
        gunaAnalyses: List<GunaAnalysis>,
        additionalFactors: AdditionalFactors,
        brideSignature: MarriageSignature,
        groomSignature: MarriageSignature,
        chartResonanceScore: Double
    ): Int {
        var protective = 0

        if ((gunaAnalyses.find { it.gunaType == GunaType.GRAHA_MAITRI }?.obtainedPoints ?: 0.0) >= 4.0) protective++
        if ((gunaAnalyses.find { it.gunaType == GunaType.TARA }?.obtainedPoints ?: 0.0) >= 1.5) protective++
        if (additionalFactors.streeDeerghaSatisfied) protective++
        if (additionalFactors.mahendraSatisfied) protective++
        if (minOf(brideSignature.promiseScore, groomSignature.promiseScore) >= 68.0) protective++
        if (chartResonanceScore >= 70.0) protective++

        return protective
    }

    private fun applyDecisionCaps(
        score: Double,
        severeBlockers: Int,
        protectiveFactors: Int
    ): Double {
        return when {
            severeBlockers >= 3 -> minOf(score, 38.0)
            severeBlockers == 2 && protectiveFactors < 3 -> minOf(score, 49.0)
            severeBlockers == 2 -> minOf(score, 57.0)
            severeBlockers == 1 && protectiveFactors < 2 -> minOf(score, 67.0)
            severeBlockers == 0 && protectiveFactors >= 4 -> maxOf(score, 74.0)
            else -> score
        }.coerceIn(0.0, 100.0)
    }

    private fun signPairHarmony(sign1: ZodiacSign?, sign2: ZodiacSign?): Double {
        if (sign1 == null || sign2 == null) return 58.0
        val distance = VedicAstrologyUtils.getHouseFromSigns(sign2, sign1)
        return houseHarmonyScore(distance)
    }

    private fun houseHarmonyScore(distance: Int): Double = when (distance) {
        1, 5, 7, 9 -> 86.0
        3, 4, 10, 11 -> 72.0
        2, 12 -> 54.0
        6, 8 -> 38.0
        else -> 58.0
    }

    private fun housePlacementAdjustment(
        house: Int,
        favorMarriageHouses: Boolean
    ): Double = when (house) {
        1, 4, 5, 7, 9, 10, 11 -> if (favorMarriageHouses) 42.0 else 35.0
        2 -> 30.0
        3, 6 -> 18.0
        8, 12 -> -18.0
        else -> 12.0
    }

    private fun dignityAdjustment(dignity: VedicAstrologyUtils.PlanetaryDignity): Double = when (dignity) {
        VedicAstrologyUtils.PlanetaryDignity.EXALTED -> 48.0
        VedicAstrologyUtils.PlanetaryDignity.MOOLATRIKONA -> 42.0
        VedicAstrologyUtils.PlanetaryDignity.OWN_SIGN -> 38.0
        VedicAstrologyUtils.PlanetaryDignity.FRIEND_SIGN -> 26.0
        VedicAstrologyUtils.PlanetaryDignity.NEUTRAL_SIGN -> 12.0
        VedicAstrologyUtils.PlanetaryDignity.ENEMY_SIGN -> -10.0
        VedicAstrologyUtils.PlanetaryDignity.DEBILITATED -> -24.0
    }

    private fun arudhaDignityScore(dignity: ArudhaPadaCalculator.PlanetaryDignity): Double = when (dignity) {
        ArudhaPadaCalculator.PlanetaryDignity.EXALTED -> 48.0
        ArudhaPadaCalculator.PlanetaryDignity.MOOLATRIKONA -> 42.0
        ArudhaPadaCalculator.PlanetaryDignity.OWN_SIGN -> 38.0
        ArudhaPadaCalculator.PlanetaryDignity.FRIEND_SIGN -> 26.0
        ArudhaPadaCalculator.PlanetaryDignity.NEUTRAL_SIGN -> 12.0
        ArudhaPadaCalculator.PlanetaryDignity.ENEMY_SIGN -> -10.0
        ArudhaPadaCalculator.PlanetaryDignity.DEBILITATED -> -24.0
    }

    private fun pairPlanetRelationshipScore(planet1: Planet?, planet2: Planet?): Double {
        if (planet1 == null || planet2 == null) return 58.0
        return relationshipScore(planet1, planet2)
    }

    private fun relationshipScore(planet1: Planet, planet2: Planet): Double {
        val relationship1 = VedicAstrologyUtils.getNaturalRelationship(planet1, planet2)
        val relationship2 = VedicAstrologyUtils.getNaturalRelationship(planet2, planet1)

        return when {
            relationship1 == PlanetaryRelationship.FRIEND && relationship2 == PlanetaryRelationship.FRIEND -> 90.0
            (relationship1 == PlanetaryRelationship.FRIEND && relationship2 == PlanetaryRelationship.NEUTRAL) ||
                (relationship1 == PlanetaryRelationship.NEUTRAL && relationship2 == PlanetaryRelationship.FRIEND) -> 76.0
            relationship1 == PlanetaryRelationship.NEUTRAL && relationship2 == PlanetaryRelationship.NEUTRAL -> 66.0
            (relationship1 == PlanetaryRelationship.ENEMY && relationship2 == PlanetaryRelationship.NEUTRAL) ||
                (relationship1 == PlanetaryRelationship.NEUTRAL && relationship2 == PlanetaryRelationship.ENEMY) -> 46.0
            else -> 28.0
        }
    }

    private fun isSaturnAspectingSign(fromSign: ZodiacSign, toSign: ZodiacSign): Boolean {
        val distance = VedicAstrologyUtils.getHouseFromSigns(toSign, fromSign)
        return distance in setOf(3, 7, 10)
    }

    private fun averageOf(vararg values: Double): Double {
        val validValues = values.filter { it.isFinite() }
        return if (validValues.isEmpty()) 58.0 else validValues.average()
    }
}
