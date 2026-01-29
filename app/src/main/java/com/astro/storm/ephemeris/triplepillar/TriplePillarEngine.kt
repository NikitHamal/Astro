package com.astro.storm.ephemeris.triplepillar

import com.astro.storm.core.common.Language
import com.astro.storm.core.common.StringResources
import com.astro.storm.core.model.Planet
import com.astro.storm.core.model.PlanetPosition
import com.astro.storm.core.model.VedicChart
import com.astro.storm.core.model.ZodiacSign
import com.astro.storm.ephemeris.AshtakavargaCalculator
import com.astro.storm.ephemeris.DashaCalculator
import com.astro.storm.ephemeris.TransitAnalyzer
import com.astro.storm.ephemeris.VedicAstrologyUtils
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.math.abs

/**
 * Triple-Pillar Predictive Engine
 *
 * This engine synthesizes the "Three Pillars of Timing" from classical Vedic astrology:
 * 1. **Vimshottari Dasha** - Karmic potential based on Moon's nakshatra at birth
 * 2. **Gochara (Transits)** - Current planetary influences from Moon sign
 * 3. **Ashtakavarga** - Quantitative bindu strength for each transit position
 *
 * The engine creates "Success Probability" timelines that peak only when:
 * - A favorable Dasha Lord is active
 * - That Dasha Lord (or supporting planets) is transiting favorable houses
 * - The transit sign has high BAV/SAV bindus in Ashtakavarga
 *
 * This three-way validation ensures predictions are only made when all classical
 * timing factors converge, following the principle that reliable predictions
 * require agreement across multiple independent techniques.
 *
 * Based on principles from:
 * - Brihat Parashara Hora Shastra (BPHS)
 * - Phaladeepika
 * - Uttara Kalamrita
 * - Jataka Parijata
 *
 * @author AstroStorm - Ultra-Precision Vedic Astrology
 */
@Singleton
class TriplePillarEngine @Inject constructor(
    private val transitAnalyzer: TransitAnalyzer
) {
    companion object {
        private val NATURAL_BENEFICS = setOf(Planet.JUPITER, Planet.VENUS, Planet.MERCURY, Planet.MOON)
        private val NATURAL_MALEFICS = setOf(Planet.SATURN, Planet.MARS, Planet.SUN, Planet.RAHU, Planet.KETU)

        private val EXALTATION_SIGNS = mapOf(
            Planet.SUN to ZodiacSign.ARIES,
            Planet.MOON to ZodiacSign.TAURUS,
            Planet.MARS to ZodiacSign.CAPRICORN,
            Planet.MERCURY to ZodiacSign.VIRGO,
            Planet.JUPITER to ZodiacSign.CANCER,
            Planet.VENUS to ZodiacSign.PISCES,
            Planet.SATURN to ZodiacSign.LIBRA,
            Planet.RAHU to ZodiacSign.TAURUS,
            Planet.KETU to ZodiacSign.SCORPIO
        )

        private val DEBILITATION_SIGNS = mapOf(
            Planet.SUN to ZodiacSign.LIBRA,
            Planet.MOON to ZodiacSign.SCORPIO,
            Planet.MARS to ZodiacSign.CANCER,
            Planet.MERCURY to ZodiacSign.PISCES,
            Planet.JUPITER to ZodiacSign.CAPRICORN,
            Planet.VENUS to ZodiacSign.VIRGO,
            Planet.SATURN to ZodiacSign.ARIES,
            Planet.RAHU to ZodiacSign.SCORPIO,
            Planet.KETU to ZodiacSign.TAURUS
        )

        private val OWN_SIGNS = mapOf(
            Planet.SUN to listOf(ZodiacSign.LEO),
            Planet.MOON to listOf(ZodiacSign.CANCER),
            Planet.MARS to listOf(ZodiacSign.ARIES, ZodiacSign.SCORPIO),
            Planet.MERCURY to listOf(ZodiacSign.GEMINI, ZodiacSign.VIRGO),
            Planet.JUPITER to listOf(ZodiacSign.SAGITTARIUS, ZodiacSign.PISCES),
            Planet.VENUS to listOf(ZodiacSign.TAURUS, ZodiacSign.LIBRA),
            Planet.SATURN to listOf(ZodiacSign.CAPRICORN, ZodiacSign.AQUARIUS)
        )

        private val MOOLATRIKONA_SIGNS = mapOf(
            Planet.SUN to ZodiacSign.LEO,
            Planet.MOON to ZodiacSign.TAURUS,
            Planet.MARS to ZodiacSign.ARIES,
            Planet.MERCURY to ZodiacSign.VIRGO,
            Planet.JUPITER to ZodiacSign.SAGITTARIUS,
            Planet.VENUS to ZodiacSign.LIBRA,
            Planet.SATURN to ZodiacSign.AQUARIUS
        )

        private val FRIENDLY_PLANETS = mapOf(
            Planet.SUN to setOf(Planet.MOON, Planet.MARS, Planet.JUPITER),
            Planet.MOON to setOf(Planet.SUN, Planet.MERCURY),
            Planet.MARS to setOf(Planet.SUN, Planet.MOON, Planet.JUPITER),
            Planet.MERCURY to setOf(Planet.SUN, Planet.VENUS),
            Planet.JUPITER to setOf(Planet.SUN, Planet.MOON, Planet.MARS),
            Planet.VENUS to setOf(Planet.MERCURY, Planet.SATURN),
            Planet.SATURN to setOf(Planet.MERCURY, Planet.VENUS)
        )

        private val ENEMY_PLANETS = mapOf(
            Planet.SUN to setOf(Planet.SATURN, Planet.VENUS),
            Planet.MOON to setOf<Planet>(),
            Planet.MARS to setOf(Planet.MERCURY),
            Planet.MERCURY to setOf(Planet.MOON),
            Planet.JUPITER to setOf(Planet.MERCURY, Planet.VENUS),
            Planet.VENUS to setOf(Planet.SUN, Planet.MOON),
            Planet.SATURN to setOf(Planet.SUN, Planet.MOON, Planet.MARS)
        )

        private const val COMBUSTION_ORB_SUN = 8.5
        private const val COMBUSTION_ORB_MOON = 12.0
        private const val COMBUSTION_ORB_MARS = 17.0
        private const val COMBUSTION_ORB_MERCURY = 14.0
        private const val COMBUSTION_ORB_JUPITER = 11.0
        private const val COMBUSTION_ORB_VENUS = 10.0
        private const val COMBUSTION_ORB_SATURN = 15.0
    }

    /**
     * Generate a complete Success Probability Timeline for a given chart and date range.
     *
     * @param chart The natal VedicChart to analyze
     * @param startDate Starting date for the timeline
     * @param endDate Ending date for the timeline
     * @param config Configuration options for the analysis
     * @param language Language for interpretations
     * @return Complete SuccessProbabilityTimeline with all analysis points
     */
    fun generateSuccessProbabilityTimeline(
        chart: VedicChart,
        startDate: LocalDateTime,
        endDate: LocalDateTime,
        config: TriplePillarConfig = TriplePillarConfig.DEFAULT,
        language: Language = Language.ENGLISH
    ): SuccessProbabilityTimeline {
        val dashaTimeline = DashaCalculator.calculateDashaTimeline(chart)
        val ashtakavargaAnalysis = AshtakavargaCalculator.calculateAshtakavarga(chart)
        val natalMoon = chart.planetPositions.find { it.planet == Planet.MOON }
            ?: throw IllegalStateException("Moon position not found in chart")

        val intervalHours = determineOptimalInterval(startDate, endDate)
        val timelineRange = TimelineRange(startDate, endDate, intervalHours)

        val points = mutableListOf<SuccessProbabilityPoint>()
        var currentDateTime = startDate

        while (!currentDateTime.isAfter(endDate)) {
            val point = calculateSuccessProbabilityPoint(
                chart = chart,
                dateTime = currentDateTime,
                dashaTimeline = dashaTimeline,
                ashtakavargaAnalysis = ashtakavargaAnalysis,
                natalMoon = natalMoon,
                config = config,
                language = language
            )
            points.add(point)
            currentDateTime = currentDateTime.plusHours(intervalHours.toLong())
        }

        val peakWindows = identifyPeakWindows(points)
        val criticalPeriods = identifyCriticalPeriods(points)
        val overallTrend = calculateOverallTrend(points)
        val monthlyAverages = calculateMonthlyAverages(points)
        val bestDayOfWeek = findBestDayOfWeek(points)
        val summary = generateTimelineSummary(points, peakWindows, criticalPeriods, language)

        return SuccessProbabilityTimeline(
            range = timelineRange,
            points = points,
            peakWindows = peakWindows,
            criticalPeriods = criticalPeriods,
            overallTrend = overallTrend,
            monthlyAverages = monthlyAverages,
            bestDayOfWeek = bestDayOfWeek,
            summary = summary
        )
    }

    /**
     * Calculate Success Probability for a specific life area.
     *
     * @param chart The natal VedicChart
     * @param dateTime The date/time to analyze
     * @param lifeArea The specific life area to focus on
     * @param config Configuration options
     * @param language Language for interpretations
     * @return LifeAreaProbability with focused analysis
     */
    fun calculateLifeAreaProbability(
        chart: VedicChart,
        dateTime: LocalDateTime,
        lifeArea: LifeAreaFocus,
        config: TriplePillarConfig = TriplePillarConfig.DEFAULT,
        language: Language = Language.ENGLISH
    ): LifeAreaProbability {
        val dashaTimeline = DashaCalculator.calculateDashaTimeline(chart)
        val ashtakavargaAnalysis = AshtakavargaCalculator.calculateAshtakavarga(chart)
        val natalMoon = chart.planetPositions.find { it.planet == Planet.MOON }
            ?: throw IllegalStateException("Moon position not found in chart")

        val transitPositions = transitAnalyzer.getTransitPositionsForDateTime(
            dateTime, chart.birthData.timezone
        )

        val houseStrengths = calculateHouseStrengths(
            chart, transitPositions, ashtakavargaAnalysis, lifeArea.relevantHouses
        )

        val planetaryContributions = calculatePlanetaryContributions(
            chart = chart,
            dateTime = dateTime,
            dashaTimeline = dashaTimeline,
            ashtakavargaAnalysis = ashtakavargaAnalysis,
            transitPositions = transitPositions,
            natalMoon = natalMoon,
            primaryPlanets = lifeArea.primaryPlanets,
            secondaryPlanets = lifeArea.secondaryPlanets,
            config = config
        )

        val overallScore = calculateLifeAreaOverallScore(
            houseStrengths, planetaryContributions, lifeArea
        )

        val rating = ProbabilityRating.fromScore(overallScore)
        val interpretation = generateLifeAreaInterpretation(
            lifeArea, rating, houseStrengths, planetaryContributions, language
        )
        val recommendations = generateLifeAreaRecommendations(
            lifeArea, rating, planetaryContributions, language
        )

        return LifeAreaProbability(
            lifeArea = lifeArea,
            dateTime = dateTime,
            overallScore = overallScore,
            rating = rating,
            houseStrengths = houseStrengths,
            planetaryContributions = planetaryContributions,
            interpretation = interpretation,
            recommendations = recommendations
        )
    }

    /**
     * Calculate a single Success Probability Point.
     */
    private fun calculateSuccessProbabilityPoint(
        chart: VedicChart,
        dateTime: LocalDateTime,
        dashaTimeline: DashaCalculator.DashaTimeline,
        ashtakavargaAnalysis: AshtakavargaCalculator.AshtakavargaAnalysis,
        natalMoon: PlanetPosition,
        config: TriplePillarConfig,
        language: Language
    ): SuccessProbabilityPoint {
        val dashaPeriodInfo = dashaTimeline.getDashaAtDate(dateTime)
        val activeDashaLords = buildActiveDashaLords(dashaPeriodInfo, config)

        val transitPositions = transitAnalyzer.getTransitPositionsForDateTime(
            dateTime, chart.birthData.timezone
        )

        val planetarySyntheses = mutableListOf<PlanetaryPillarSynthesis>()

        Planet.MAIN_PLANETS.forEach { planet ->
            if (!config.includeOuterPlanets && planet in Planet.OUTER_PLANETS) return@forEach

            val dashaFavorability = calculateDashaFavorability(
                planet, dashaPeriodInfo, chart, config, language
            )

            val gocharaFavorability = calculateGocharaFavorability(
                planet, transitPositions, natalMoon, chart, config, language
            )

            val ashtakavargaStrength = calculateAshtakavargaStrength(
                planet, transitPositions, ashtakavargaAnalysis, chart, config, language
            )

            val synthesis = synthesizePillar(
                planet, dateTime, dashaFavorability, gocharaFavorability,
                ashtakavargaStrength, config, language
            )

            planetarySyntheses.add(synthesis)
        }

        val overallScore = calculateOverallScore(planetarySyntheses, activeDashaLords, config)
        val rating = ProbabilityRating.fromScore(overallScore)

        val peakPlanets = planetarySyntheses
            .filter { it.synthesizedScore >= 70.0 }
            .map { it.planet }

        val challengingPlanets = planetarySyntheses
            .filter { it.synthesizedScore < 40.0 }
            .map { it.planet }

        val recommendations = generateRecommendations(
            rating, activeDashaLords, peakPlanets, challengingPlanets, language
        )

        return SuccessProbabilityPoint(
            dateTime = dateTime,
            overallScore = overallScore,
            rating = rating,
            activeDashaLords = activeDashaLords,
            planetarySyntheses = planetarySyntheses,
            peakPlanets = peakPlanets,
            challengingPlanets = challengingPlanets,
            recommendations = recommendations
        )
    }

    /**
     * Build active dasha lords list based on configuration.
     */
    private fun buildActiveDashaLords(
        dashaPeriodInfo: DashaCalculator.DashaPeriodInfo,
        config: TriplePillarConfig
    ): List<Pair<DashaCalculator.DashaLevel, Planet>> {
        val lords = mutableListOf<Pair<DashaCalculator.DashaLevel, Planet>>()

        dashaPeriodInfo.mahadasha?.let {
            lords.add(DashaCalculator.DashaLevel.MAHADASHA to it.planet)
        }

        if (config.minimumDashaLevel.levelNumber >= DashaCalculator.DashaLevel.ANTARDASHA.levelNumber) {
            dashaPeriodInfo.antardasha?.let {
                lords.add(DashaCalculator.DashaLevel.ANTARDASHA to it.planet)
            }
        }

        if (config.minimumDashaLevel.levelNumber >= DashaCalculator.DashaLevel.PRATYANTARDASHA.levelNumber) {
            dashaPeriodInfo.pratyantardasha?.let {
                lords.add(DashaCalculator.DashaLevel.PRATYANTARDASHA to it.planet)
            }
        }

        if (config.minimumDashaLevel.levelNumber >= DashaCalculator.DashaLevel.SOOKSHMADASHA.levelNumber) {
            dashaPeriodInfo.sookshmadasha?.let {
                lords.add(DashaCalculator.DashaLevel.SOOKSHMADASHA to it.planet)
            }
        }

        return lords
    }

    /**
     * Calculate Dasha Favorability for a specific planet.
     */
    private fun calculateDashaFavorability(
        planet: Planet,
        dashaPeriodInfo: DashaCalculator.DashaPeriodInfo,
        chart: VedicChart,
        config: TriplePillarConfig,
        language: Language
    ): DashaFavorability? {
        val allDashaLords = dashaPeriodInfo.getAllLords()
        if (planet !in allDashaLords) return null

        val dashaLevel = when (planet) {
            dashaPeriodInfo.mahadasha?.planet -> DashaCalculator.DashaLevel.MAHADASHA
            dashaPeriodInfo.antardasha?.planet -> DashaCalculator.DashaLevel.ANTARDASHA
            dashaPeriodInfo.pratyantardasha?.planet -> DashaCalculator.DashaLevel.PRATYANTARDASHA
            dashaPeriodInfo.sookshmadasha?.planet -> DashaCalculator.DashaLevel.SOOKSHMADASHA
            dashaPeriodInfo.pranadasha?.planet -> DashaCalculator.DashaLevel.PRANADASHA
            dashaPeriodInfo.dehadasha?.planet -> DashaCalculator.DashaLevel.DEHADASHA
            else -> DashaCalculator.DashaLevel.MAHADASHA
        }

        val planetPosition = chart.planetPositions.find { it.planet == planet }
        val naturalBenefic = planet in NATURAL_BENEFICS
        val functionalBenefic = isFunctionalBenefic(planet, chart)
        val lordOfHouses = getLordshipHouses(planet, chart)
        val dignity = calculatePlanetaryDignity(planet, planetPosition?.sign)
        val aspectedByBenefics = isAspectedByBenefics(planet, chart)
        val aspectedByMalefics = isAspectedByMalefics(planet, chart)
        val retrograde = planetPosition?.isRetrograde ?: false
        val combust = isCombust(planet, chart)

        var score = 50.0

        if (naturalBenefic) score += 10.0
        if (functionalBenefic) score += 15.0

        score *= dignity.scoreMultiplier

        if (aspectedByBenefics) score += 8.0
        if (aspectedByMalefics) score -= 8.0

        if (config.retrogradeAdjustment && retrograde) {
            score -= 5.0
        }

        if (config.combustAdjustment && combust) {
            score -= 12.0
        }

        val beneficHouses = listOf(1, 2, 4, 5, 7, 9, 10, 11)
        val maleficHouses = listOf(6, 8, 12)
        lordOfHouses.forEach { house ->
            when (house) {
                in beneficHouses -> score += 3.0
                in maleficHouses -> score -= 3.0
            }
        }

        score = score.coerceIn(0.0, 100.0)

        val interpretation = generateDashaInterpretation(
            planet, dashaLevel, dignity, functionalBenefic,
            retrograde, combust, score, language
        )

        return DashaFavorability(
            planet = planet,
            dashaLevel = dashaLevel,
            naturalBenefic = naturalBenefic,
            functionalBenefic = functionalBenefic,
            lordOfHouses = lordOfHouses,
            dignity = dignity,
            aspectedByBenefics = aspectedByBenefics,
            aspectedByMalefics = aspectedByMalefics,
            retrograde = retrograde,
            combust = combust,
            favorabilityScore = score,
            interpretation = interpretation
        )
    }

    /**
     * Calculate Gochara (Transit) Favorability.
     */
    private fun calculateGocharaFavorability(
        planet: Planet,
        transitPositions: List<PlanetPosition>,
        natalMoon: PlanetPosition,
        chart: VedicChart,
        config: TriplePillarConfig,
        language: Language
    ): GocharaFavorability {
        val transitPosition = transitPositions.find { it.planet == planet }
        val transitSign = transitPosition?.sign ?: ZodiacSign.ARIES

        val houseFromMoon = VedicAstrologyUtils.getHouseFromSigns(transitSign, natalMoon.sign)
        val houseFromLagna = VedicAstrologyUtils.getHouseFromSigns(
            transitSign, ZodiacSign.fromLongitude(chart.ascendant)
        )

        val baseEffect = getGocharaBaseEffect(planet, houseFromMoon)

        val (isVedhaObstructed, vedhaSource) = if (config.vedhaConsideration) {
            checkVedha(planet, houseFromMoon, transitPositions, natalMoon.sign)
        } else {
            Pair(false, null)
        }

        val transitSpeed = determineTransitSpeed(transitPosition)
        val isRetrograde = transitPosition?.isRetrograde ?: false

        var score = when (baseEffect) {
            TransitAnalyzer.TransitEffect.EXCELLENT -> 85.0
            TransitAnalyzer.TransitEffect.GOOD -> 70.0
            TransitAnalyzer.TransitEffect.NEUTRAL -> 50.0
            TransitAnalyzer.TransitEffect.CHALLENGING -> 35.0
            TransitAnalyzer.TransitEffect.DIFFICULT -> 20.0
        }

        if (isVedhaObstructed && baseEffect in listOf(
                TransitAnalyzer.TransitEffect.EXCELLENT,
                TransitAnalyzer.TransitEffect.GOOD
            )) {
            score -= 20.0
        }

        when (transitSpeed) {
            TransitSpeed.STATIONARY -> score += 5.0
            TransitSpeed.SLOW -> score += 2.0
            TransitSpeed.FAST -> score -= 2.0
            else -> {}
        }

        if (config.retrogradeAdjustment && isRetrograde) {
            score -= 5.0
        }

        score = score.coerceIn(0.0, 100.0)

        val interpretation = generateGocharaInterpretation(
            planet, transitSign, houseFromMoon, baseEffect,
            isVedhaObstructed, vedhaSource, score, language
        )

        return GocharaFavorability(
            planet = planet,
            transitSign = transitSign,
            houseFromMoon = houseFromMoon,
            houseFromLagna = houseFromLagna,
            effect = baseEffect,
            isVedhaObstructed = isVedhaObstructed,
            vedhaSource = vedhaSource,
            transitSpeed = transitSpeed,
            isRetrograde = isRetrograde,
            favorabilityScore = score,
            interpretation = interpretation
        )
    }

    /**
     * Calculate Ashtakavarga Strength for a planet in transit.
     */
    private fun calculateAshtakavargaStrength(
        planet: Planet,
        transitPositions: List<PlanetPosition>,
        ashtakavargaAnalysis: AshtakavargaCalculator.AshtakavargaAnalysis,
        chart: VedicChart,
        config: TriplePillarConfig,
        language: Language
    ): AshtakavargaStrength {
        val transitPosition = transitPositions.find { it.planet == planet }
        val transitSign = transitPosition?.sign ?: ZodiacSign.ARIES

        val bavBindus = if (planet in listOf(Planet.RAHU, Planet.KETU)) {
            0
        } else {
            ashtakavargaAnalysis.bhinnashtakavarga[planet]
                ?.getBindusForSign(transitSign) ?: 0
        }

        val savBindus = ashtakavargaAnalysis.sarvashtakavarga.getBindusForSign(transitSign)

        val (kakshaLord, kakshaFavorable) = if (config.kakshaAnalysis && transitPosition != null) {
            val kakshaPosition = AshtakavargaCalculator.calculateKakshaPosition(
                transitPosition.longitude, chart, ashtakavargaAnalysis
            )
            Pair(kakshaPosition.kakshaLord, kakshaPosition.isBeneficial)
        } else {
            Pair("Unknown", false)
        }

        val strengthLevel = BinduStrengthLevel.fromBindus(bavBindus, savBindus)

        var score = 50.0 + strengthLevel.scoreBonus

        if (bavBindus >= 5) score += 10.0
        else if (bavBindus >= 4) score += 5.0
        else if (bavBindus <= 1) score -= 10.0
        else if (bavBindus <= 2) score -= 5.0

        if (savBindus >= 30) score += 8.0
        else if (savBindus >= 28) score += 4.0
        else if (savBindus < 22) score -= 8.0
        else if (savBindus < 25) score -= 4.0

        if (kakshaFavorable) score += 5.0

        score = score.coerceIn(0.0, 100.0)

        val interpretation = generateAshtakavargaInterpretation(
            planet, transitSign, bavBindus, savBindus,
            strengthLevel, kakshaLord, kakshaFavorable, score, language
        )

        return AshtakavargaStrength(
            planet = planet,
            sign = transitSign,
            bavBindus = bavBindus,
            savBindus = savBindus,
            kakshaLord = kakshaLord,
            kakshaFavorable = kakshaFavorable,
            strengthLevel = strengthLevel,
            favorabilityScore = score,
            interpretation = interpretation
        )
    }

    /**
     * Synthesize all three pillars into a unified analysis.
     */
    private fun synthesizePillar(
        planet: Planet,
        dateTime: LocalDateTime,
        dashaFavorability: DashaFavorability?,
        gocharaFavorability: GocharaFavorability,
        ashtakavargaStrength: AshtakavargaStrength,
        config: TriplePillarConfig,
        language: Language
    ): PlanetaryPillarSynthesis {
        val dashaScore = dashaFavorability?.favorabilityScore ?: 50.0
        val gocharaScore = gocharaFavorability.favorabilityScore
        val ashtakavargaScore = ashtakavargaStrength.favorabilityScore

        val weightedScore = if (dashaFavorability != null) {
            (dashaScore * config.dashaWeight) +
            (gocharaScore * config.gocharaWeight) +
            (ashtakavargaScore * config.ashtakavargaWeight)
        } else {
            val adjustedGocharaWeight = config.gocharaWeight + (config.dashaWeight / 2)
            val adjustedAshtakavargaWeight = config.ashtakavargaWeight + (config.dashaWeight / 2)
            (gocharaScore * adjustedGocharaWeight) + (ashtakavargaScore * adjustedAshtakavargaWeight)
        }

        val alignment = calculatePillarAlignment(
            dashaFavorability?.favorabilityScore,
            gocharaFavorability.favorabilityScore,
            ashtakavargaStrength.favorabilityScore
        )

        val synthesizedScore = (weightedScore + alignment.scoreBonus).coerceIn(0.0, 100.0)

        val interpretation = generateSynthesisInterpretation(
            planet, dashaFavorability, gocharaFavorability,
            ashtakavargaStrength, alignment, synthesizedScore, language
        )

        return PlanetaryPillarSynthesis(
            planet = planet,
            dateTime = dateTime,
            dashaFavorability = dashaFavorability,
            gocharaFavorability = gocharaFavorability,
            ashtakavargaStrength = ashtakavargaStrength,
            synthesizedScore = synthesizedScore,
            alignment = alignment,
            interpretation = interpretation
        )
    }

    /**
     * Calculate pillar alignment status.
     */
    private fun calculatePillarAlignment(
        dashaScore: Double?,
        gocharaScore: Double,
        ashtakavargaScore: Double
    ): PillarAlignment {
        val threshold = 55.0
        val dashaFavorable = dashaScore?.let { it >= threshold } ?: true
        val gocharaFavorable = gocharaScore >= threshold
        val ashtakavargaFavorable = ashtakavargaScore >= threshold

        val favorableCount = listOf(dashaFavorable, gocharaFavorable, ashtakavargaFavorable)
            .count { it }

        return when (favorableCount) {
            3 -> PillarAlignment.ALL_FAVORABLE
            2 -> PillarAlignment.TWO_FAVORABLE
            1 -> PillarAlignment.TWO_UNFAVORABLE
            0 -> PillarAlignment.ALL_UNFAVORABLE
            else -> PillarAlignment.MIXED
        }
    }

    /**
     * Calculate overall success probability score.
     */
    private fun calculateOverallScore(
        syntheses: List<PlanetaryPillarSynthesis>,
        activeDashaLords: List<Pair<DashaCalculator.DashaLevel, Planet>>,
        config: TriplePillarConfig
    ): Double {
        if (syntheses.isEmpty()) return 50.0

        val dashaLordPlanets = activeDashaLords.map { it.second }.toSet()

        var totalWeightedScore = 0.0
        var totalWeight = 0.0

        syntheses.forEach { synthesis ->
            val weight = when {
                synthesis.planet in dashaLordPlanets -> 2.5
                synthesis.planet in NATURAL_BENEFICS -> 1.5
                synthesis.planet in NATURAL_MALEFICS -> 1.0
                else -> 1.2
            }

            totalWeightedScore += synthesis.synthesizedScore * weight
            totalWeight += weight
        }

        val baseScore = if (totalWeight > 0) totalWeightedScore / totalWeight else 50.0

        var bonusScore = 0.0
        val tripleAlignedCount = syntheses.count { it.alignment == PillarAlignment.ALL_FAVORABLE }
        bonusScore += tripleAlignedCount * 2.0

        val allUnfavorableCount = syntheses.count { it.alignment == PillarAlignment.ALL_UNFAVORABLE }
        bonusScore -= allUnfavorableCount * 2.0

        return (baseScore + bonusScore).coerceIn(0.0, 100.0)
    }

    /**
     * Determine optimal analysis interval based on date range.
     */
    private fun determineOptimalInterval(startDate: LocalDateTime, endDate: LocalDateTime): Int {
        val daysBetween = ChronoUnit.DAYS.between(startDate, endDate)
        return when {
            daysBetween <= 7 -> 6
            daysBetween <= 30 -> 12
            daysBetween <= 90 -> 24
            daysBetween <= 365 -> 48
            else -> 72
        }
    }

    /**
     * Identify peak success windows in the timeline.
     */
    private fun identifyPeakWindows(points: List<SuccessProbabilityPoint>): List<PeakWindow> {
        val peakWindows = mutableListOf<PeakWindow>()
        var windowStart: SuccessProbabilityPoint? = null
        var windowPeak: SuccessProbabilityPoint? = null
        val windowPoints = mutableListOf<SuccessProbabilityPoint>()

        points.forEach { point ->
            if (point.isPeakWindow) {
                if (windowStart == null) {
                    windowStart = point
                    windowPeak = point
                    windowPoints.clear()
                }
                windowPoints.add(point)

                if (windowPeak == null || point.overallScore > windowPeak!!.overallScore) {
                    windowPeak = point
                }
            } else {
                if (windowStart != null && windowPoints.isNotEmpty()) {
                    val dominantPlanets = windowPoints
                        .flatMap { it.peakPlanets }
                        .groupBy { it }
                        .entries
                        .sortedByDescending { it.value.size }
                        .take(3)
                        .map { it.key }

                    peakWindows.add(
                        PeakWindow(
                            startDateTime = windowStart!!.dateTime,
                            endDateTime = windowPoints.last().dateTime,
                            peakScore = windowPeak!!.overallScore,
                            peakDateTime = windowPeak!!.dateTime,
                            dominantPlanets = dominantPlanets,
                            supportingFactors = extractSupportingFactors(windowPoints),
                            recommendations = windowPeak!!.recommendations
                        )
                    )
                }
                windowStart = null
                windowPeak = null
                windowPoints.clear()
            }
        }

        if (windowStart != null && windowPoints.isNotEmpty()) {
            val dominantPlanets = windowPoints
                .flatMap { it.peakPlanets }
                .groupBy { it }
                .entries
                .sortedByDescending { it.value.size }
                .take(3)
                .map { it.key }

            peakWindows.add(
                PeakWindow(
                    startDateTime = windowStart!!.dateTime,
                    endDateTime = windowPoints.last().dateTime,
                    peakScore = windowPeak!!.overallScore,
                    peakDateTime = windowPeak!!.dateTime,
                    dominantPlanets = dominantPlanets,
                    supportingFactors = extractSupportingFactors(windowPoints),
                    recommendations = windowPeak!!.recommendations
                )
            )
        }

        return peakWindows
    }

    /**
     * Identify critical periods requiring caution.
     */
    private fun identifyCriticalPeriods(points: List<SuccessProbabilityPoint>): List<CriticalPeriod> {
        val criticalPeriods = mutableListOf<CriticalPeriod>()
        var periodStart: SuccessProbabilityPoint? = null
        var periodLowest: SuccessProbabilityPoint? = null
        val periodPoints = mutableListOf<SuccessProbabilityPoint>()

        points.forEach { point ->
            if (point.isCriticalPeriod) {
                if (periodStart == null) {
                    periodStart = point
                    periodLowest = point
                    periodPoints.clear()
                }
                periodPoints.add(point)

                if (periodLowest == null || point.overallScore < periodLowest!!.overallScore) {
                    periodLowest = point
                }
            } else {
                if (periodStart != null && periodPoints.isNotEmpty()) {
                    val challengingPlanets = periodPoints
                        .flatMap { it.challengingPlanets }
                        .groupBy { it }
                        .entries
                        .sortedByDescending { it.value.size }
                        .take(3)
                        .map { it.key }

                    criticalPeriods.add(
                        CriticalPeriod(
                            startDateTime = periodStart!!.dateTime,
                            endDateTime = periodPoints.last().dateTime,
                            lowestScore = periodLowest!!.overallScore,
                            lowestDateTime = periodLowest!!.dateTime,
                            challengingPlanets = challengingPlanets,
                            challenges = extractChallenges(periodPoints),
                            mitigationStrategies = generateMitigationStrategies(challengingPlanets)
                        )
                    )
                }
                periodStart = null
                periodLowest = null
                periodPoints.clear()
            }
        }

        if (periodStart != null && periodPoints.isNotEmpty()) {
            val challengingPlanets = periodPoints
                .flatMap { it.challengingPlanets }
                .groupBy { it }
                .entries
                .sortedByDescending { it.value.size }
                .take(3)
                .map { it.key }

            criticalPeriods.add(
                CriticalPeriod(
                    startDateTime = periodStart!!.dateTime,
                    endDateTime = periodPoints.last().dateTime,
                    lowestScore = periodLowest!!.overallScore,
                    lowestDateTime = periodLowest!!.dateTime,
                    challengingPlanets = challengingPlanets,
                    challenges = extractChallenges(periodPoints),
                    mitigationStrategies = generateMitigationStrategies(challengingPlanets)
                )
            )
        }

        return criticalPeriods
    }

    /**
     * Calculate overall timeline trend.
     */
    private fun calculateOverallTrend(points: List<SuccessProbabilityPoint>): TimelineTrend {
        if (points.size < 2) return TimelineTrend.STABLE_NEUTRAL

        val firstQuarter = points.take(points.size / 4).map { it.overallScore }.average()
        val lastQuarter = points.takeLast(points.size / 4).map { it.overallScore }.average()
        val overallAvg = points.map { it.overallScore }.average()
        val variance = points.map { (it.overallScore - overallAvg).let { d -> d * d } }.average()

        val scoreDiff = lastQuarter - firstQuarter

        return when {
            variance > 400 -> TimelineTrend.VOLATILE
            scoreDiff > 15 -> TimelineTrend.STRONGLY_IMPROVING
            scoreDiff > 7 -> TimelineTrend.IMPROVING
            scoreDiff < -15 -> TimelineTrend.STRONGLY_DECLINING
            scoreDiff < -7 -> TimelineTrend.DECLINING
            overallAvg >= 60 -> TimelineTrend.STABLE_POSITIVE
            overallAvg < 40 -> TimelineTrend.STABLE_NEGATIVE
            else -> TimelineTrend.STABLE_NEUTRAL
        }
    }

    /**
     * Calculate monthly averages for the timeline.
     */
    private fun calculateMonthlyAverages(points: List<SuccessProbabilityPoint>): Map<String, Double> {
        return points
            .groupBy { it.dateTime.format(DateTimeFormatter.ofPattern("yyyy-MM")) }
            .mapValues { (_, monthPoints) -> monthPoints.map { it.overallScore }.average() }
    }

    /**
     * Find the best day of week based on historical data.
     */
    private fun findBestDayOfWeek(points: List<SuccessProbabilityPoint>): DayOfWeek? {
        if (points.isEmpty()) return null

        return points
            .groupBy { it.dateTime.dayOfWeek }
            .mapValues { (_, dayPoints) -> dayPoints.map { it.overallScore }.average() }
            .maxByOrNull { it.value }
            ?.key
    }

    /**
     * Generate timeline summary.
     */
    private fun generateTimelineSummary(
        points: List<SuccessProbabilityPoint>,
        peakWindows: List<PeakWindow>,
        criticalPeriods: List<CriticalPeriod>,
        language: Language
    ): TimelineSummary {
        val avgScore = points.map { it.overallScore }.average()
        val rating = ProbabilityRating.fromScore(avgScore)

        val overallOutlook = when (rating) {
            ProbabilityRating.PEAK, ProbabilityRating.HIGH ->
                "This period shows excellent potential with strong planetary support across all three pillars."
            ProbabilityRating.MODERATE ->
                "This period presents balanced opportunities requiring strategic timing and focused effort."
            ProbabilityRating.LOW, ProbabilityRating.CHALLENGING ->
                "This period requires patience and careful planning. Focus on consolidation rather than expansion."
            ProbabilityRating.CRITICAL ->
                "This period calls for caution and preparation. Use this time for inner work and strategic planning."
        }

        val bestPeriodDescription = if (peakWindows.isNotEmpty()) {
            val best = peakWindows.maxByOrNull { it.peakScore }!!
            "Best window: ${best.startDateTime.format(DateTimeFormatter.ofPattern("MMM d"))} - " +
            "${best.endDateTime.format(DateTimeFormatter.ofPattern("MMM d"))} with ${String.format("%.0f", best.peakScore)}% success probability. " +
            "Dominant planets: ${best.dominantPlanets.joinToString(", ") { it.displayName }}"
        } else {
            "No peak success windows identified in this period."
        }

        val challengingPeriodDescription = if (criticalPeriods.isNotEmpty()) {
            val worst = criticalPeriods.minByOrNull { it.lowestScore }!!
            "Critical period: ${worst.startDateTime.format(DateTimeFormatter.ofPattern("MMM d"))} - " +
            "${worst.endDateTime.format(DateTimeFormatter.ofPattern("MMM d"))}. " +
            "Challenging planets: ${worst.challengingPlanets.joinToString(", ") { it.displayName }}"
        } else {
            "No critical periods requiring special caution."
        }

        val keyRecommendations = mutableListOf<String>()
        if (peakWindows.isNotEmpty()) {
            keyRecommendations.add("Initiate important ventures during identified peak windows")
        }
        if (criticalPeriods.isNotEmpty()) {
            keyRecommendations.add("Avoid major commitments during critical periods")
        }
        keyRecommendations.add("Align activities with favorable dasha lord significations")
        keyRecommendations.add("Consider Ashtakavarga bindu strength for timing decisions")

        val focusAreas = mutableListOf<String>()
        val dominantDashaLords = points
            .flatMap { it.activeDashaLords }
            .map { it.second }
            .groupBy { it }
            .entries
            .sortedByDescending { it.value.size }
            .take(2)
            .map { it.key }

        dominantDashaLords.forEach { planet ->
            focusAreas.add("${planet.displayName} dasha themes: ${getDashaThemes(planet)}")
        }

        val auspiciousDates = peakWindows
            .map { it.peakDateTime.toLocalDate() }
            .distinct()
            .take(5)

        val cautionDates = criticalPeriods
            .map { it.lowestDateTime.toLocalDate() }
            .distinct()
            .take(5)

        return TimelineSummary(
            overallOutlook = overallOutlook,
            bestPeriodDescription = bestPeriodDescription,
            challengingPeriodDescription = challengingPeriodDescription,
            keyRecommendations = keyRecommendations,
            focusAreas = focusAreas,
            auspiciousDates = auspiciousDates,
            cautionDates = cautionDates
        )
    }

    private fun getDashaThemes(planet: Planet): String {
        return when (planet) {
            Planet.SUN -> "authority, father, government, career recognition"
            Planet.MOON -> "mind, mother, emotions, public dealings"
            Planet.MARS -> "energy, siblings, property, courage, competitions"
            Planet.MERCURY -> "communication, business, education, intellect"
            Planet.JUPITER -> "wisdom, children, fortune, spirituality, expansion"
            Planet.VENUS -> "relationships, luxury, arts, comforts, vehicles"
            Planet.SATURN -> "discipline, longevity, karma, hard work, delays"
            Planet.RAHU -> "foreign connections, unconventional paths, material desires"
            Planet.KETU -> "spirituality, detachment, past-life karma, occult"
            else -> "general life matters"
        }
    }

    private fun extractSupportingFactors(points: List<SuccessProbabilityPoint>): List<String> {
        val factors = mutableListOf<String>()

        val avgDashaScore = points
            .flatMap { it.planetarySyntheses }
            .mapNotNull { it.dashaFavorability?.favorabilityScore }
            .average()

        if (avgDashaScore >= 60) {
            factors.add("Favorable Dasha period active")
        }

        val tripleAlignedCount = points
            .flatMap { it.planetarySyntheses }
            .count { it.alignment == PillarAlignment.ALL_FAVORABLE }

        if (tripleAlignedCount > points.size) {
            factors.add("Multiple planets with triple-pillar alignment")
        }

        val highBinduTransits = points
            .flatMap { it.planetarySyntheses }
            .count { it.ashtakavargaStrength.bavBindus >= 4 }

        if (highBinduTransits > points.size / 2) {
            factors.add("Strong Ashtakavarga support for transits")
        }

        return factors.ifEmpty { listOf("Favorable planetary combinations") }
    }

    private fun extractChallenges(points: List<SuccessProbabilityPoint>): List<String> {
        val challenges = mutableListOf<String>()

        val lowGocharaCount = points
            .flatMap { it.planetarySyntheses }
            .count { it.gocharaFavorability.favorabilityScore < 40 }

        if (lowGocharaCount > points.size) {
            challenges.add("Challenging transit positions from Moon")
        }

        val vedhaCount = points
            .flatMap { it.planetarySyntheses }
            .count { it.gocharaFavorability.isVedhaObstructed }

        if (vedhaCount > points.size / 2) {
            challenges.add("Vedha obstructions affecting favorable transits")
        }

        val lowBinduCount = points
            .flatMap { it.planetarySyntheses }
            .count { it.ashtakavargaStrength.bavBindus <= 2 }

        if (lowBinduCount > points.size) {
            challenges.add("Low Ashtakavarga bindus reducing transit strength")
        }

        return challenges.ifEmpty { listOf("General planetary challenges") }
    }

    private fun generateMitigationStrategies(challengingPlanets: List<Planet>): List<String> {
        val strategies = mutableListOf<String>()

        challengingPlanets.forEach { planet ->
            when (planet) {
                Planet.SATURN -> strategies.add("Practice patience, serve elders, focus on discipline and structure")
                Planet.MARS -> strategies.add("Channel energy constructively, avoid conflicts, exercise regularly")
                Planet.RAHU -> strategies.add("Stay grounded, avoid speculation, maintain ethical standards")
                Planet.KETU -> strategies.add("Focus on spiritual practices, accept detachment gracefully")
                Planet.SUN -> strategies.add("Practice humility, respect authority, maintain health")
                Planet.MOON -> strategies.add("Emotional self-care, connect with mother, maintain routine")
                Planet.MERCURY -> strategies.add("Double-check communications, avoid signing contracts hastily")
                Planet.JUPITER -> strategies.add("Seek wise counsel, maintain optimism, avoid overconfidence")
                Planet.VENUS -> strategies.add("Practice moderation in pleasures, nurture relationships")
                else -> {}
            }
        }

        strategies.add("Perform remedial measures appropriate for challenging planets")
        strategies.add("Focus on internal growth rather than external achievements")

        return strategies.distinct().take(5)
    }

    private fun generateRecommendations(
        rating: ProbabilityRating,
        activeDashaLords: List<Pair<DashaCalculator.DashaLevel, Planet>>,
        peakPlanets: List<Planet>,
        challengingPlanets: List<Planet>,
        language: Language
    ): List<String> {
        val recommendations = mutableListOf<String>()

        when (rating) {
            ProbabilityRating.PEAK, ProbabilityRating.HIGH -> {
                recommendations.add("Excellent time for initiating important ventures")
                recommendations.add("Leverage favorable dasha and transit alignment")
            }
            ProbabilityRating.MODERATE -> {
                recommendations.add("Proceed with calculated optimism")
                recommendations.add("Focus on areas supported by peak planets")
            }
            ProbabilityRating.LOW, ProbabilityRating.CHALLENGING -> {
                recommendations.add("Exercise patience and avoid major commitments")
                recommendations.add("Focus on preparation and planning")
            }
            ProbabilityRating.CRITICAL -> {
                recommendations.add("Prioritize caution and protective measures")
                recommendations.add("Avoid initiating new ventures")
            }
        }

        if (peakPlanets.isNotEmpty()) {
            recommendations.add("Favorable activities: ${peakPlanets.take(2).joinToString(", ") {
                getPlanetFavorableActivities(it)
            }}")
        }

        return recommendations.take(4)
    }

    private fun getPlanetFavorableActivities(planet: Planet): String {
        return when (planet) {
            Planet.SUN -> "leadership, government matters"
            Planet.MOON -> "public relations, travel"
            Planet.MARS -> "property, competition, athletics"
            Planet.MERCURY -> "business, communication, learning"
            Planet.JUPITER -> "education, spiritual pursuits, expansion"
            Planet.VENUS -> "relationships, arts, comfort purchases"
            Planet.SATURN -> "long-term planning, structure"
            Planet.RAHU -> "foreign dealings, technology"
            Planet.KETU -> "spiritual practices, research"
            else -> "general activities"
        }
    }

    private fun isFunctionalBenefic(planet: Planet, chart: VedicChart): Boolean {
        val ascendantSign = ZodiacSign.fromLongitude(chart.ascendant)
        val beneficHouseLords = getBeneficHouseLordsForAscendant(ascendantSign)
        return planet in beneficHouseLords
    }

    private fun getBeneficHouseLordsForAscendant(ascendant: ZodiacSign): Set<Planet> {
        return when (ascendant) {
            ZodiacSign.ARIES -> setOf(Planet.SUN, Planet.JUPITER, Planet.MARS)
            ZodiacSign.TAURUS -> setOf(Planet.SATURN, Planet.MERCURY, Planet.VENUS)
            ZodiacSign.GEMINI -> setOf(Planet.VENUS, Planet.SATURN, Planet.MERCURY)
            ZodiacSign.CANCER -> setOf(Planet.MARS, Planet.JUPITER, Planet.MOON)
            ZodiacSign.LEO -> setOf(Planet.MARS, Planet.JUPITER, Planet.SUN)
            ZodiacSign.VIRGO -> setOf(Planet.VENUS, Planet.MERCURY)
            ZodiacSign.LIBRA -> setOf(Planet.SATURN, Planet.MERCURY, Planet.VENUS)
            ZodiacSign.SCORPIO -> setOf(Planet.JUPITER, Planet.MOON, Planet.SUN)
            ZodiacSign.SAGITTARIUS -> setOf(Planet.SUN, Planet.MARS, Planet.JUPITER)
            ZodiacSign.CAPRICORN -> setOf(Planet.VENUS, Planet.MERCURY, Planet.SATURN)
            ZodiacSign.AQUARIUS -> setOf(Planet.VENUS, Planet.SATURN)
            ZodiacSign.PISCES -> setOf(Planet.MOON, Planet.MARS, Planet.JUPITER)
        }
    }

    private fun getLordshipHouses(planet: Planet, chart: VedicChart): List<Int> {
        val houses = mutableListOf<Int>()
        val ascendantSign = ZodiacSign.fromLongitude(chart.ascendant)

        OWN_SIGNS[planet]?.forEach { ownSign ->
            val house = ((ownSign.ordinal - ascendantSign.ordinal + 12) % 12) + 1
            houses.add(house)
        }

        return houses
    }

    private fun calculatePlanetaryDignity(planet: Planet, sign: ZodiacSign?): PlanetaryDignity {
        if (sign == null) return PlanetaryDignity.NEUTRAL_SIGN

        return when {
            EXALTATION_SIGNS[planet] == sign -> PlanetaryDignity.EXALTED
            DEBILITATION_SIGNS[planet] == sign -> PlanetaryDignity.DEBILITATED
            MOOLATRIKONA_SIGNS[planet] == sign -> PlanetaryDignity.MOOLATRIKONA
            OWN_SIGNS[planet]?.contains(sign) == true -> PlanetaryDignity.OWN_SIGN
            isFriendlySign(planet, sign) -> PlanetaryDignity.FRIEND_SIGN
            isEnemySign(planet, sign) -> PlanetaryDignity.ENEMY_SIGN
            else -> PlanetaryDignity.NEUTRAL_SIGN
        }
    }

    private fun isFriendlySign(planet: Planet, sign: ZodiacSign): Boolean {
        val signLord = sign.ruler
        return FRIENDLY_PLANETS[planet]?.contains(signLord) == true
    }

    private fun isEnemySign(planet: Planet, sign: ZodiacSign): Boolean {
        val signLord = sign.ruler
        return ENEMY_PLANETS[planet]?.contains(signLord) == true
    }

    private fun isAspectedByBenefics(planet: Planet, chart: VedicChart): Boolean {
        val planetPosition = chart.planetPositions.find { it.planet == planet } ?: return false
        val planetSign = planetPosition.sign

        return NATURAL_BENEFICS.any { benefic ->
            if (benefic == planet) return@any false
            val beneficPosition = chart.planetPositions.find { it.planet == benefic } ?: return@any false
            hasAspect(benefic, beneficPosition.sign, planetSign)
        }
    }

    private fun isAspectedByMalefics(planet: Planet, chart: VedicChart): Boolean {
        val planetPosition = chart.planetPositions.find { it.planet == planet } ?: return false
        val planetSign = planetPosition.sign

        return NATURAL_MALEFICS.any { malefic ->
            if (malefic == planet) return@any false
            val maleficPosition = chart.planetPositions.find { it.planet == malefic } ?: return@any false
            hasAspect(malefic, maleficPosition.sign, planetSign)
        }
    }

    private fun hasAspect(aspectingPlanet: Planet, fromSign: ZodiacSign, toSign: ZodiacSign): Boolean {
        val distance = ((toSign.ordinal - fromSign.ordinal + 12) % 12) + 1

        val aspectHouses = when (aspectingPlanet) {
            Planet.MARS -> listOf(4, 7, 8)
            Planet.JUPITER -> listOf(5, 7, 9)
            Planet.SATURN -> listOf(3, 7, 10)
            else -> listOf(7)
        }

        return distance in aspectHouses
    }

    private fun isCombust(planet: Planet, chart: VedicChart): Boolean {
        if (planet == Planet.SUN) return false

        val sunPosition = chart.planetPositions.find { it.planet == Planet.SUN } ?: return false
        val planetPosition = chart.planetPositions.find { it.planet == planet } ?: return false

        val distance = abs(VedicAstrologyUtils.angularDistance(
            sunPosition.longitude, planetPosition.longitude
        ))

        val combustionOrb = when (planet) {
            Planet.MOON -> COMBUSTION_ORB_MOON
            Planet.MARS -> COMBUSTION_ORB_MARS
            Planet.MERCURY -> COMBUSTION_ORB_MERCURY
            Planet.JUPITER -> COMBUSTION_ORB_JUPITER
            Planet.VENUS -> COMBUSTION_ORB_VENUS
            Planet.SATURN -> COMBUSTION_ORB_SATURN
            else -> COMBUSTION_ORB_SUN
        }

        return distance <= combustionOrb
    }

    private fun getGocharaBaseEffect(planet: Planet, houseFromMoon: Int): TransitAnalyzer.TransitEffect {
        val favorableHouses = when (planet) {
            Planet.SUN -> listOf(3, 6, 10, 11)
            Planet.MOON -> listOf(1, 3, 6, 7, 10, 11)
            Planet.MARS -> listOf(3, 6, 11)
            Planet.MERCURY -> listOf(2, 4, 6, 8, 10, 11)
            Planet.JUPITER -> listOf(2, 5, 7, 9, 11)
            Planet.VENUS -> listOf(1, 2, 3, 4, 5, 8, 9, 11, 12)
            Planet.SATURN -> listOf(3, 6, 11)
            Planet.RAHU, Planet.KETU -> listOf(3, 6, 10, 11)
            else -> emptyList()
        }

        return when {
            houseFromMoon in favorableHouses -> {
                if (planet in NATURAL_BENEFICS) TransitAnalyzer.TransitEffect.EXCELLENT
                else TransitAnalyzer.TransitEffect.GOOD
            }
            houseFromMoon in listOf(1, 2, 5) -> TransitAnalyzer.TransitEffect.NEUTRAL
            else -> {
                if (planet in NATURAL_MALEFICS) TransitAnalyzer.TransitEffect.DIFFICULT
                else TransitAnalyzer.TransitEffect.CHALLENGING
            }
        }
    }

    private fun checkVedha(
        planet: Planet,
        houseFromMoon: Int,
        transitPositions: List<PlanetPosition>,
        natalMoonSign: ZodiacSign
    ): Pair<Boolean, Planet?> {
        val vedhaMap = when (planet) {
            Planet.SUN -> mapOf(3 to 9, 6 to 12, 10 to 4, 11 to 5)
            Planet.MOON -> mapOf(1 to 5, 3 to 9, 6 to 12, 7 to 2, 10 to 4, 11 to 8)
            Planet.MARS -> mapOf(3 to 12, 6 to 9, 11 to 5)
            Planet.MERCURY -> mapOf(2 to 5, 4 to 3, 6 to 9, 8 to 1, 10 to 8, 11 to 12)
            Planet.JUPITER -> mapOf(2 to 12, 5 to 4, 7 to 3, 9 to 10, 11 to 8)
            Planet.VENUS -> mapOf(1 to 8, 2 to 7, 3 to 1, 4 to 10, 5 to 9, 8 to 5, 9 to 11, 11 to 6, 12 to 3)
            Planet.SATURN -> mapOf(3 to 12, 6 to 9, 11 to 5)
            else -> emptyMap()
        }

        val vedhaHouse = vedhaMap[houseFromMoon] ?: return Pair(false, null)

        transitPositions.forEach { transitPos ->
            if (transitPos.planet != planet && transitPos.planet in Planet.MAIN_PLANETS) {
                val otherHouse = VedicAstrologyUtils.getHouseFromSigns(transitPos.sign, natalMoonSign)
                if (otherHouse == vedhaHouse) {
                    return Pair(true, transitPos.planet)
                }
            }
        }

        return Pair(false, null)
    }

    private fun determineTransitSpeed(position: PlanetPosition?): TransitSpeed {
        if (position == null) return TransitSpeed.MEDIUM

        val normalSpeed = when (position.planet) {
            Planet.SUN -> 1.0
            Planet.MOON -> 13.0
            Planet.MERCURY -> 1.5
            Planet.VENUS -> 1.2
            Planet.MARS -> 0.5
            Planet.JUPITER -> 0.08
            Planet.SATURN -> 0.03
            Planet.RAHU, Planet.KETU -> 0.05
            else -> 0.5
        }

        val actualSpeed = abs(position.speed)
        val speedRatio = actualSpeed / normalSpeed

        return when {
            speedRatio < 0.1 -> TransitSpeed.STATIONARY
            speedRatio < 0.5 -> TransitSpeed.SLOW
            speedRatio > 1.5 -> TransitSpeed.FAST
            else -> TransitSpeed.MEDIUM
        }
    }

    private fun calculateHouseStrengths(
        chart: VedicChart,
        transitPositions: List<PlanetPosition>,
        ashtakavargaAnalysis: AshtakavargaCalculator.AshtakavargaAnalysis,
        relevantHouses: List<Int>
    ): Map<Int, Double> {
        val ascendantSign = ZodiacSign.fromLongitude(chart.ascendant)
        val houseStrengths = mutableMapOf<Int, Double>()

        relevantHouses.forEach { house ->
            val houseSign = ZodiacSign.entries[(ascendantSign.ordinal + house - 1) % 12]
            val savBindus = ashtakavargaAnalysis.sarvashtakavarga.getBindusForSign(houseSign)

            var strength = (savBindus.toDouble() / 56.0) * 100

            val planetsInHouse = transitPositions.filter {
                val transitHouse = ((it.sign.ordinal - ascendantSign.ordinal + 12) % 12) + 1
                transitHouse == house
            }

            planetsInHouse.forEach { planetPos ->
                if (planetPos.planet in NATURAL_BENEFICS) strength += 5.0
                if (planetPos.planet in NATURAL_MALEFICS) strength -= 5.0
            }

            houseStrengths[house] = strength.coerceIn(0.0, 100.0)
        }

        return houseStrengths
    }

    private fun calculatePlanetaryContributions(
        chart: VedicChart,
        dateTime: LocalDateTime,
        dashaTimeline: DashaCalculator.DashaTimeline,
        ashtakavargaAnalysis: AshtakavargaCalculator.AshtakavargaAnalysis,
        transitPositions: List<PlanetPosition>,
        natalMoon: PlanetPosition,
        primaryPlanets: List<Planet>,
        secondaryPlanets: List<Planet>,
        config: TriplePillarConfig
    ): Map<Planet, Double> {
        val contributions = mutableMapOf<Planet, Double>()
        val dashaPeriodInfo = dashaTimeline.getDashaAtDate(dateTime)

        (primaryPlanets + secondaryPlanets).distinct().forEach { planet ->
            val transitPosition = transitPositions.find { it.planet == planet }
            val transitSign = transitPosition?.sign ?: ZodiacSign.ARIES

            var contribution = 50.0

            val dashaLords = dashaPeriodInfo.getAllLords()
            if (planet in dashaLords) {
                contribution += 15.0
            }

            val houseFromMoon = VedicAstrologyUtils.getHouseFromSigns(transitSign, natalMoon.sign)
            val gocharaEffect = getGocharaBaseEffect(planet, houseFromMoon)
            contribution += when (gocharaEffect) {
                TransitAnalyzer.TransitEffect.EXCELLENT -> 20.0
                TransitAnalyzer.TransitEffect.GOOD -> 10.0
                TransitAnalyzer.TransitEffect.NEUTRAL -> 0.0
                TransitAnalyzer.TransitEffect.CHALLENGING -> -10.0
                TransitAnalyzer.TransitEffect.DIFFICULT -> -20.0
            }

            val bavBindus = ashtakavargaAnalysis.bhinnashtakavarga[planet]
                ?.getBindusForSign(transitSign) ?: 0
            contribution += (bavBindus - 4) * 5

            if (planet in primaryPlanets) contribution *= 1.2

            contributions[planet] = contribution.coerceIn(0.0, 100.0)
        }

        return contributions
    }

    private fun calculateLifeAreaOverallScore(
        houseStrengths: Map<Int, Double>,
        planetaryContributions: Map<Planet, Double>,
        lifeArea: LifeAreaFocus
    ): Double {
        val avgHouseStrength = if (houseStrengths.isNotEmpty()) {
            houseStrengths.values.average()
        } else 50.0

        val primaryContrib = planetaryContributions
            .filterKeys { it in lifeArea.primaryPlanets }
            .values
            .average()
            .takeIf { !it.isNaN() } ?: 50.0

        val secondaryContrib = planetaryContributions
            .filterKeys { it in lifeArea.secondaryPlanets }
            .values
            .average()
            .takeIf { !it.isNaN() } ?: 50.0

        return (avgHouseStrength * 0.30 + primaryContrib * 0.50 + secondaryContrib * 0.20)
            .coerceIn(0.0, 100.0)
    }

    private fun generateDashaInterpretation(
        planet: Planet,
        level: DashaCalculator.DashaLevel,
        dignity: PlanetaryDignity,
        functionalBenefic: Boolean,
        retrograde: Boolean,
        combust: Boolean,
        score: Double,
        language: Language
    ): String {
        return buildString {
            append("${planet.displayName} as ${level.displayName} lord ")
            append("is ${dignity.displayName.lowercase()}. ")

            if (functionalBenefic) append("Functionally benefic for this ascendant. ")
            if (retrograde) append("Retrograde motion indicates internalized energy. ")
            if (combust) append("Combust by Sun reduces natural significations. ")

            append("Overall favorability: ${String.format("%.0f", score)}%.")
        }
    }

    private fun generateGocharaInterpretation(
        planet: Planet,
        sign: ZodiacSign,
        houseFromMoon: Int,
        effect: TransitAnalyzer.TransitEffect,
        vedhaObstructed: Boolean,
        vedhaSource: Planet?,
        score: Double,
        language: Language
    ): String {
        return buildString {
            append("${planet.displayName} transiting ${sign.displayName} ")
            append("(house $houseFromMoon from Moon) - ${effect.displayName}. ")

            if (vedhaObstructed && vedhaSource != null) {
                append("Vedha from ${vedhaSource.displayName} reduces positive effects. ")
            }

            append("Transit strength: ${String.format("%.0f", score)}%.")
        }
    }

    private fun generateAshtakavargaInterpretation(
        planet: Planet,
        sign: ZodiacSign,
        bavBindus: Int,
        savBindus: Int,
        strengthLevel: BinduStrengthLevel,
        kakshaLord: String,
        kakshaFavorable: Boolean,
        score: Double,
        language: Language
    ): String {
        return buildString {
            append("${planet.displayName} in ${sign.displayName}: ")
            append("BAV $bavBindus/8, SAV $savBindus/56. ")
            append("${strengthLevel.displayName} strength. ")

            if (kakshaFavorable) {
                append("Favorable kaksha ($kakshaLord). ")
            }

            append("Ashtakavarga support: ${String.format("%.0f", score)}%.")
        }
    }

    private fun generateSynthesisInterpretation(
        planet: Planet,
        dashaFavorability: DashaFavorability?,
        gocharaFavorability: GocharaFavorability,
        ashtakavargaStrength: AshtakavargaStrength,
        alignment: PillarAlignment,
        score: Double,
        language: Language
    ): String {
        return buildString {
            append("${planet.displayName}: ${alignment.displayName}. ")

            if (dashaFavorability != null) {
                append("Dasha ${if (dashaFavorability.isFavorable) "favorable" else "challenging"}. ")
            }

            append("Transit ${if (gocharaFavorability.favorabilityScore >= 55) "supportive" else "mixed"}. ")
            append("Ashtakavarga ${ashtakavargaStrength.strengthLevel.displayName.lowercase()}. ")
            append("Combined probability: ${String.format("%.0f", score)}%.")
        }
    }

    private fun generateLifeAreaInterpretation(
        lifeArea: LifeAreaFocus,
        rating: ProbabilityRating,
        houseStrengths: Map<Int, Double>,
        planetaryContributions: Map<Planet, Double>,
        language: Language
    ): String {
        val strongestHouse = houseStrengths.maxByOrNull { it.value }
        val strongestPlanet = planetaryContributions.maxByOrNull { it.value }

        return buildString {
            append("${lifeArea.displayName}: ${rating.displayName}. ")

            strongestHouse?.let {
                append("House ${it.key} shows strongest support (${String.format("%.0f", it.value)}%). ")
            }

            strongestPlanet?.let {
                append("${it.key.displayName} contributes most favorably. ")
            }

            when (rating) {
                ProbabilityRating.PEAK, ProbabilityRating.HIGH ->
                    append("Excellent time for ${lifeArea.displayName.lowercase()} initiatives.")
                ProbabilityRating.MODERATE ->
                    append("Moderate opportunities available with focused effort.")
                else ->
                    append("Patience recommended for ${lifeArea.displayName.lowercase()} matters.")
            }
        }
    }

    private fun generateLifeAreaRecommendations(
        lifeArea: LifeAreaFocus,
        rating: ProbabilityRating,
        planetaryContributions: Map<Planet, Double>,
        language: Language
    ): List<String> {
        val recommendations = mutableListOf<String>()

        when (lifeArea) {
            LifeAreaFocus.CAREER -> {
                if (rating in listOf(ProbabilityRating.PEAK, ProbabilityRating.HIGH)) {
                    recommendations.add("Excellent time for career advancement initiatives")
                    recommendations.add("Seek recognition and leadership opportunities")
                } else {
                    recommendations.add("Focus on skill development and preparation")
                    recommendations.add("Build relationships with mentors and allies")
                }
            }
            LifeAreaFocus.FINANCE -> {
                if (rating in listOf(ProbabilityRating.PEAK, ProbabilityRating.HIGH)) {
                    recommendations.add("Favorable for investments and wealth building")
                    recommendations.add("Expand income sources strategically")
                } else {
                    recommendations.add("Maintain conservative financial approach")
                    recommendations.add("Focus on savings and reducing expenses")
                }
            }
            LifeAreaFocus.RELATIONSHIPS -> {
                if (rating in listOf(ProbabilityRating.PEAK, ProbabilityRating.HIGH)) {
                    recommendations.add("Auspicious for relationship commitments")
                    recommendations.add("Nurture partnerships actively")
                } else {
                    recommendations.add("Prioritize communication and understanding")
                    recommendations.add("Address existing relationship challenges")
                }
            }
            else -> {
                recommendations.add("Align activities with favorable planetary influences")
                recommendations.add("Use peak periods strategically for ${lifeArea.displayName.lowercase()}")
            }
        }

        return recommendations.take(3)
    }
}
