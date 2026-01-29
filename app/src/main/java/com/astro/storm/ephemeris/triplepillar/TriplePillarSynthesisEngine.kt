package com.astro.storm.ephemeris.triplepillar

import android.content.Context
import com.astro.storm.core.common.Language
import com.astro.storm.core.model.Planet
import com.astro.storm.core.model.VedicChart
import com.astro.storm.core.model.ZodiacSign
import com.astro.storm.ephemeris.DashaCalculator
import com.astro.storm.ephemeris.SwissEphemerisEngine
import com.astro.storm.ephemeris.VedicAstrologyUtils
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.temporal.ChronoUnit
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Triple-Pillar Synthesis Engine
 *
 * The crown jewel of AstroStorm's predictive capabilities. This engine synthesizes
 * the three fundamental pillars of Vedic timing to generate accurate predictions:
 *
 * 1. VIMSHOTTARI DASHA (40% weight) - The soul's karmic timeline
 * 2. GOCHARA (35% weight) - Current planetary weather
 * 3. ASHTAKAVARGA (25% weight) - Quantitative strength assessment
 *
 * Key Innovation:
 * Rather than reading Dashas or Transits in isolation (which leads to inaccurate predictions),
 * this engine creates a "Success Probability" timeline that peaks only when:
 * - The Dasha Lord is functional benefic
 * - The transits are favorable (good houses from Moon)
 * - The Ashtakavarga supports the transiting planets (high BAV/SAV)
 *
 * Classical Foundation:
 * - Brihat Parashara Hora Shastra emphasizes all three factors
 * - Phaladeepika correlates Dasha with Gochara for timing
 * - Sarvartha Chintamani uses Ashtakavarga for transit refinement
 *
 * @author AstroStorm - Ultra-Precision Vedic Astrology
 */
@Singleton
class TriplePillarSynthesisEngine @Inject constructor(
    @dagger.hilt.android.qualifiers.ApplicationContext private val context: Context,
    private val ephemerisEngine: SwissEphemerisEngine
) {

    companion object {
        // Pillar weights (must sum to 1.0)
        private const val DASHA_WEIGHT = 0.40
        private const val TRANSIT_WEIGHT = 0.35
        private const val ASHTAKAVARGA_WEIGHT = 0.25

        // Thresholds
        private const val PEAK_PERIOD_THRESHOLD = 0.70
        private const val CAUTION_PERIOD_THRESHOLD = 0.40

        // Life area house mappings for detailed predictions
        private val LIFE_AREA_HOUSE_SIGNIFICANCE = mapOf(
            LifeAreaCategory.CAREER to mapOf(1 to 0.2, 2 to 0.15, 6 to 0.2, 10 to 0.35, 11 to 0.1),
            LifeAreaCategory.WEALTH to mapOf(2 to 0.3, 5 to 0.15, 9 to 0.2, 11 to 0.35),
            LifeAreaCategory.RELATIONSHIP to mapOf(5 to 0.2, 7 to 0.5, 11 to 0.3),
            LifeAreaCategory.HEALTH to mapOf(1 to 0.4, 6 to 0.35, 8 to 0.25),
            LifeAreaCategory.EDUCATION to mapOf(2 to 0.15, 4 to 0.2, 5 to 0.35, 9 to 0.3),
            LifeAreaCategory.SPIRITUALITY to mapOf(5 to 0.2, 9 to 0.4, 12 to 0.4),
            LifeAreaCategory.PROPERTY to mapOf(4 to 0.6, 11 to 0.4),
            LifeAreaCategory.TRAVEL to mapOf(3 to 0.3, 9 to 0.4, 12 to 0.3),
            LifeAreaCategory.CHILDREN to mapOf(5 to 0.5, 9 to 0.3, 11 to 0.2),
            LifeAreaCategory.LITIGATION to mapOf(6 to 0.4, 7 to 0.3, 12 to 0.3)
        )
    }

    /**
     * Generate complete Triple-Pillar synthesis for a chart
     */
    fun generateSynthesis(
        chart: VedicChart,
        query: PredictionQuery = PredictionQuery(lifeArea = null, specificQuestion = null),
        language: Language = Language.ENGLISH
    ): TriplePillarSynthesis {
        val analysisDateTime = LocalDateTime.now()

        // Calculate Dasha timeline
        val dashaTimeline = DashaCalculator.calculateDashaTimeline(chart)

        // Analyze Dasha Pillar
        val dashaPillar = DashaPillarAnalyzer.analyzeDashaPillar(
            chart = chart,
            dashaTimeline = dashaTimeline,
            targetDateTime = analysisDateTime
        )

        // Analyze Transit Pillar
        val transitPillar = TransitPillarAnalyzer.analyzeTransitPillar(
            natalChart = chart,
            transitDateTime = analysisDateTime,
            ephemerisEngine = ephemerisEngine
        )

        // Get current Dasha Lords for Ashtakavarga analysis
        val dashaLords = listOfNotNull(
            dashaPillar.mahadashaPlanet,
            dashaPillar.antardashaPlanet,
            dashaPillar.pratyantardashaPlanet
        )

        // Analyze Ashtakavarga Pillar
        val ashtakavargaPillar = AshtakavargaPillarAnalyzer.analyzeAshtakavargaPillar(
            natalChart = chart,
            transitPositions = transitPillar.transitPositions,
            dashaLords = dashaLords
        )

        // Calculate overall success probability
        val overallSuccessProbability = calculateOverallProbability(
            dashaPillar = dashaPillar,
            transitPillar = transitPillar,
            ashtakavargaPillar = ashtakavargaPillar
        )

        // Generate life area predictions
        val lifeAreaPredictions = generateLifeAreaPredictions(
            chart = chart,
            dashaPillar = dashaPillar,
            transitPillar = transitPillar,
            ashtakavargaPillar = ashtakavargaPillar,
            query = query
        )

        // Generate timeline if requested
        val timeline = if (query.includeTimeline) {
            generateTimeline(
                chart = chart,
                dashaTimeline = dashaTimeline,
                startDate = analysisDateTime,
                months = query.timeframeMonths,
                granularity = query.granularity
            )
        } else {
            emptyList()
        }

        // Identify peak and caution periods
        val peakPeriods = identifyPeakPeriods(timeline)
        val cautionPeriods = identifyCautionPeriods(timeline)

        // Generate recommendations
        val recommendations = generateRecommendations(
            dashaPillar = dashaPillar,
            transitPillar = transitPillar,
            ashtakavargaPillar = ashtakavargaPillar,
            lifeAreaPredictions = lifeAreaPredictions
        )

        return TriplePillarSynthesis(
            analysisDateTime = analysisDateTime,
            natalChartId = chart.birthData.name,
            dashaPillar = dashaPillar,
            transitPillar = transitPillar,
            ashtakavargaPillar = ashtakavargaPillar,
            overallSuccessProbability = overallSuccessProbability,
            lifeAreaPredictions = lifeAreaPredictions,
            timeline = timeline,
            peakPeriods = peakPeriods,
            cautionPeriods = cautionPeriods,
            recommendations = recommendations,
            language = language
        )
    }

    /**
     * Calculate overall success probability from the three pillars
     */
    private fun calculateOverallProbability(
        dashaPillar: DashaPillarAnalysis,
        transitPillar: TransitPillarAnalysis,
        ashtakavargaPillar: AshtakavargaPillarAnalysis
    ): Double {
        val dashaScore = dashaPillar.toPillarContribution().score
        val transitScore = transitPillar.overallGocharaScore
        val ashtakavargaScore = ashtakavargaPillar.overallAshtakavargaScore

        // Weighted combination
        val weightedScore = (dashaScore * DASHA_WEIGHT) +
                (transitScore * TRANSIT_WEIGHT) +
                (ashtakavargaScore * ASHTAKAVARGA_WEIGHT)

        // Apply synergy bonus when all pillars align
        val synergyBonus = if (dashaScore >= 0.6 && transitScore >= 0.6 && ashtakavargaScore >= 0.6) {
            0.05 // 5% bonus for alignment
        } else if (dashaScore < 0.4 && transitScore < 0.4 && ashtakavargaScore < 0.4) {
            -0.05 // 5% penalty for all negative
        } else {
            0.0
        }

        return (weightedScore + synergyBonus).coerceIn(0.0, 1.0)
    }

    /**
     * Generate life area predictions
     */
    private fun generateLifeAreaPredictions(
        chart: VedicChart,
        dashaPillar: DashaPillarAnalysis,
        transitPillar: TransitPillarAnalysis,
        ashtakavargaPillar: AshtakavargaPillarAnalysis,
        query: PredictionQuery
    ): Map<LifeAreaCategory, LifeAreaPrediction> {
        val predictions = mutableMapOf<LifeAreaCategory, LifeAreaPrediction>()
        val areasToAnalyze = if (query.lifeArea != null) {
            listOf(query.lifeArea)
        } else {
            LifeAreaCategory.entries
        }

        val ascendantSign = ZodiacSign.fromLongitude(chart.ascendant)

        for (area in areasToAnalyze) {
            val houseWeights = LIFE_AREA_HOUSE_SIGNIFICANCE[area] ?: continue

            // Calculate Dasha support for this life area
            val dashaSupport = calculateDashaSupportForArea(
                area = area,
                dashaPillar = dashaPillar,
                chart = chart
            )

            // Calculate Transit support for this life area
            val transitSupport = calculateTransitSupportForArea(
                area = area,
                transitPillar = transitPillar,
                ascendantSign = ascendantSign
            )

            // Calculate Ashtakavarga support for this life area
            val ashtakavargaSupport = calculateAshtakavargaSupportForArea(
                area = area,
                ashtakavargaPillar = ashtakavargaPillar,
                ascendantSign = ascendantSign
            )

            // Calculate overall success probability for this area
            val successProbability = (dashaSupport * DASHA_WEIGHT) +
                    (transitSupport * TRANSIT_WEIGHT) +
                    (ashtakavargaSupport * ASHTAKAVARGA_WEIGHT)

            // Get relevant Dasha Lords
            val relevantDashaLords = getRelevantDashaLordsForArea(area, chart, dashaPillar)

            // Get key transits
            val keyTransits = getKeyTransitsForArea(area, transitPillar, ascendantSign)

            // Generate interpretations
            val (challenges, opportunities) = generateChallengesAndOpportunities(
                area = area,
                dashaSupport = dashaSupport,
                transitSupport = transitSupport,
                ashtakavargaSupport = ashtakavargaSupport,
                transitPillar = transitPillar
            )

            val briefInterpretation = generateBriefAreaInterpretation(area, successProbability)
            val detailedInterpretation = generateDetailedAreaInterpretation(
                area = area,
                dashaPillar = dashaPillar,
                transitPillar = transitPillar,
                ashtakavargaPillar = ashtakavargaPillar,
                successProbability = successProbability
            )

            predictions[area] = LifeAreaPrediction(
                area = area,
                successProbability = successProbability,
                dashaSupport = dashaSupport,
                transitSupport = transitSupport,
                ashtakavargaSupport = ashtakavargaSupport,
                relevantDashaLords = relevantDashaLords,
                keyTransits = keyTransits,
                favorableTimings = emptyList(), // Would need deeper calculation
                challenges = challenges,
                opportunities = opportunities,
                briefInterpretation = briefInterpretation,
                detailedInterpretation = detailedInterpretation
            )
        }

        return predictions
    }

    /**
     * Calculate Dasha support for a specific life area
     */
    private fun calculateDashaSupportForArea(
        area: LifeAreaCategory,
        dashaPillar: DashaPillarAnalysis,
        chart: VedicChart
    ): Double {
        var support = dashaPillar.mahadashaLordNature.score

        // Check if Dasha lord owns or aspects houses relevant to this area
        val areaHouses = area.houses
        val mahadashaHouse = dashaPillar.mahadashaPlanetHouse

        // Bonus if Dasha lord is placed in relevant house
        if (mahadashaHouse in areaHouses) {
            support += 0.2
        }

        // Check relative positions from Dasha lord
        val favorableHousesFromDashaLord = listOf(1, 5, 9, 2, 11) // Trines, 2nd, 11th are supportive
        val significators = area.significators

        // Check if area significators are well-placed from Dasha lord
        significators.forEach { significator ->
            val houseFromDashaLord = dashaPillar.relativePositions[significator] ?: 0
            if (houseFromDashaLord in favorableHousesFromDashaLord) {
                support += 0.05
            }
        }

        // Antardasha adjustment
        dashaPillar.antardashaNature?.let { antarNature ->
            val antarAdjustment = (antarNature.score - 0.5) * 0.2
            support += antarAdjustment
        }

        // Sandhi adjustment
        if (dashaPillar.isDashaSandhi) {
            support *= 0.85
        }

        return support.coerceIn(0.0, 1.0)
    }

    /**
     * Calculate Transit support for a specific life area
     */
    private fun calculateTransitSupportForArea(
        area: LifeAreaCategory,
        transitPillar: TransitPillarAnalysis,
        ascendantSign: ZodiacSign
    ): Double {
        var support = 0.0
        val areaHouses = area.houses
        val significators = area.significators

        // Check transits of significators
        significators.forEach { significator ->
            val gocharaEffect = transitPillar.gocharaFromMoon[significator]
            if (gocharaEffect != null) {
                val effectScore = gocharaEffect.effect.score
                val vedhaAdjustment = if (gocharaEffect.isVedhaAffected) 0.7 else 1.0
                support += (effectScore * vedhaAdjustment) / significators.size
            }
        }

        // Check if benefics (Jupiter, Venus) are transiting relevant houses
        val jupiterTransit = transitPillar.transitPositions[Planet.JUPITER]
        val venusTransit = transitPillar.transitPositions[Planet.VENUS]

        if (jupiterTransit != null && jupiterTransit.houseFromLagna in areaHouses) {
            support += 0.15
        }
        if (venusTransit != null && venusTransit.houseFromLagna in areaHouses) {
            support += 0.1
        }

        // Check if Saturn is transiting relevant houses (can be good or challenging)
        val saturnTransit = transitPillar.transitPositions[Planet.SATURN]
        if (saturnTransit != null && saturnTransit.houseFromLagna in areaHouses) {
            val saturnGocharaEffect = transitPillar.gocharaFromMoon[Planet.SATURN]
            support += if (saturnGocharaEffect?.effect?.score ?: 0.0 >= 0.5) 0.1 else -0.1
        }

        return support.coerceIn(0.0, 1.0)
    }

    /**
     * Calculate Ashtakavarga support for a specific life area
     */
    private fun calculateAshtakavargaSupportForArea(
        area: LifeAreaCategory,
        ashtakavargaPillar: AshtakavargaPillarAnalysis,
        ascendantSign: ZodiacSign
    ): Double {
        val areaHouses = area.houses
        var totalSupport = 0.0
        var count = 0

        // Check SAV of houses relevant to this area
        ashtakavargaPillar.sarvashtakavargaDistribution.forEach { (sign, sav) ->
            val house = VedicAstrologyUtils.getHouseFromSigns(sign, ascendantSign)
            if (house in areaHouses) {
                val normalizedSav = sav / 56.0
                totalSupport += normalizedSav
                count++
            }
        }

        // Check transit scores of area significators
        val significators = area.significators.filter { it != Planet.RAHU && it != Planet.KETU }
        significators.forEach { significator ->
            val transitScore = ashtakavargaPillar.currentTransitScores[significator]
            if (transitScore != null) {
                totalSupport += transitScore.quality.score * 0.5
                count++
            }
        }

        return if (count > 0) (totalSupport / count).coerceIn(0.0, 1.0) else 0.5
    }

    /**
     * Get relevant Dasha Lords for a life area
     */
    private fun getRelevantDashaLordsForArea(
        area: LifeAreaCategory,
        chart: VedicChart,
        dashaPillar: DashaPillarAnalysis
    ): List<Planet> {
        val relevantLords = mutableListOf<Planet>()

        // Always include current Dasha Lords
        relevantLords.add(dashaPillar.mahadashaPlanet)
        dashaPillar.antardashaPlanet?.let { relevantLords.add(it) }

        // Add area significators if they are currently running as Dasha Lords
        val significators = area.significators
        significators.forEach { sig ->
            if (sig == dashaPillar.mahadashaPlanet || sig == dashaPillar.antardashaPlanet) {
                // Already added
            } else if (sig == dashaPillar.pratyantardashaPlanet) {
                relevantLords.add(sig)
            }
        }

        return relevantLords.distinct()
    }

    /**
     * Get key transits for a life area
     */
    private fun getKeyTransitsForArea(
        area: LifeAreaCategory,
        transitPillar: TransitPillarAnalysis,
        ascendantSign: ZodiacSign
    ): List<String> {
        val keyTransits = mutableListOf<String>()
        val areaHouses = area.houses

        transitPillar.significantTransits
            .filter { transit ->
                val houseFromLagna = transitPillar.transitPositions[transit.planet]?.houseFromLagna
                houseFromLagna in areaHouses || transit.planet in area.significators
            }
            .take(3)
            .forEach { transit ->
                keyTransits.add(transit.description)
            }

        return keyTransits
    }

    /**
     * Generate challenges and opportunities for a life area
     */
    private fun generateChallengesAndOpportunities(
        area: LifeAreaCategory,
        dashaSupport: Double,
        transitSupport: Double,
        ashtakavargaSupport: Double,
        transitPillar: TransitPillarAnalysis
    ): Pair<List<String>, List<String>> {
        val challenges = mutableListOf<String>()
        val opportunities = mutableListOf<String>()

        // Dasha-based
        if (dashaSupport >= 0.6) {
            opportunities.add("Dasha period supports ${area.displayName.lowercase()} matters")
        } else if (dashaSupport < 0.4) {
            challenges.add("Current Dasha may not fully support ${area.displayName.lowercase()} initiatives")
        }

        // Transit-based
        if (transitSupport >= 0.6) {
            opportunities.add("Favorable transits for ${area.displayName.lowercase()}")
        } else if (transitSupport < 0.4) {
            challenges.add("Transit conditions require patience in ${area.displayName.lowercase()}")
        }

        // Ashtakavarga-based
        if (ashtakavargaSupport >= 0.6) {
            opportunities.add("Strong Ashtakavarga support indicates good timing")
        } else if (ashtakavargaSupport < 0.4) {
            challenges.add("Limited Ashtakavarga strength - selective timing recommended")
        }

        // Check for specific significant transits
        transitPillar.significantTransits.forEach { transit ->
            if (transit.planet in area.significators) {
                if (transit.impact > 0) {
                    opportunities.add(transit.description)
                } else {
                    challenges.add(transit.description)
                }
            }
        }

        return Pair(challenges.take(5), opportunities.take(5))
    }

    /**
     * Generate brief interpretation for a life area
     */
    private fun generateBriefAreaInterpretation(area: LifeAreaCategory, probability: Double): String {
        return when {
            probability >= 0.75 -> "Excellent period for ${area.displayName.lowercase()}. Favorable conditions across all pillars."
            probability >= 0.60 -> "Good period for ${area.displayName.lowercase()}. Generally supportive conditions."
            probability >= 0.45 -> "Average period. ${area.displayName} matters need careful timing."
            probability >= 0.30 -> "Challenging period. Patience recommended for ${area.displayName.lowercase()}."
            else -> "Difficult period. Consider remedies and wait for better timing."
        }
    }

    /**
     * Generate detailed interpretation for a life area
     */
    private fun generateDetailedAreaInterpretation(
        area: LifeAreaCategory,
        dashaPillar: DashaPillarAnalysis,
        transitPillar: TransitPillarAnalysis,
        ashtakavargaPillar: AshtakavargaPillarAnalysis,
        successProbability: Double
    ): String = buildString {
        append("For ${area.displayName}, the Triple-Pillar analysis reveals: ")

        // Dasha insight
        append("The ${dashaPillar.mahadashaPlanet.displayName} Mahadasha ")
        when (dashaPillar.mahadashaLordNature) {
            DashaLordNature.HIGHLY_BENEFIC, DashaLordNature.BENEFIC ->
                append("provides strong karmic support. ")
            DashaLordNature.NEUTRAL ->
                append("offers balanced energy. ")
            else ->
                append("requires careful navigation. ")
        }

        // Transit insight
        val favorableTransits = transitPillar.gocharaFromMoon.count { it.value.effect.score >= 0.5 }
        append("Currently, $favorableTransits out of ${transitPillar.gocharaFromMoon.size} planets are favorably transiting. ")

        // Ashtakavarga insight
        append("The Ashtakavarga analysis shows ")
        append(when {
            ashtakavargaPillar.overallAshtakavargaScore >= 0.6 -> "strong quantitative support. "
            ashtakavargaPillar.overallAshtakavargaScore >= 0.4 -> "moderate support. "
            else -> "limited support requiring selective timing. "
        })

        // Overall recommendation
        append("Overall success probability for ${area.displayName.lowercase()} is ")
        append("${String.format("%.0f", successProbability * 100)}%.")
    }

    /**
     * Generate success probability timeline
     */
    private fun generateTimeline(
        chart: VedicChart,
        dashaTimeline: DashaCalculator.DashaTimeline,
        startDate: LocalDateTime,
        months: Int,
        granularity: TimelineGranularity
    ): List<SuccessProbabilityPoint> {
        val timeline = mutableListOf<SuccessProbabilityPoint>()
        var currentDate = startDate.toLocalDate()
        val endDate = currentDate.plusMonths(months.toLong())

        while (!currentDate.isAfter(endDate)) {
            val dateTime = currentDate.atStartOfDay()

            // Get Dasha at this date
            val dashaAtDate = dashaTimeline.getDashaAtDate(dateTime)

            // Quick Dasha score calculation
            val dashaScore = calculateQuickDashaScore(dashaAtDate, chart)

            // Quick Transit score (simplified for timeline)
            val transitScore = calculateQuickTransitScore(chart, dateTime)

            // Quick Ashtakavarga score (use cached natal values)
            val ashtakavargaScore = 0.5 // Simplified - Ashtakavarga is natal-based

            // Calculate probability
            val probability = (dashaScore * DASHA_WEIGHT) +
                    (transitScore * TRANSIT_WEIGHT) +
                    (ashtakavargaScore * ASHTAKAVARGA_WEIGHT)

            // Determine dominant factor
            val dominantFactor = when {
                dashaScore >= transitScore && dashaScore >= ashtakavargaScore -> "Dasha"
                transitScore >= dashaScore && transitScore >= ashtakavargaScore -> "Transit"
                else -> "Ashtakavarga"
            }

            // Generate recommendation
            val recommendation = when {
                probability >= 0.7 -> "Excellent time for new initiatives"
                probability >= 0.5 -> "Good time for steady progress"
                probability >= 0.3 -> "Focus on maintenance and preparation"
                else -> "Rest and reflection recommended"
            }

            timeline.add(SuccessProbabilityPoint(
                date = currentDate,
                probability = probability.coerceIn(0.0, 1.0),
                dashaContribution = dashaScore * DASHA_WEIGHT,
                transitContribution = transitScore * TRANSIT_WEIGHT,
                ashtakavargaContribution = ashtakavargaScore * ASHTAKAVARGA_WEIGHT,
                dominantFactor = dominantFactor,
                recommendation = recommendation
            ))

            currentDate = currentDate.plusDays(granularity.days.toLong())
        }

        return timeline
    }

    /**
     * Quick Dasha score calculation for timeline
     */
    private fun calculateQuickDashaScore(
        dashaAtDate: DashaCalculator.DashaPeriodInfo,
        chart: VedicChart
    ): Double {
        val mahadashaPlanet = dashaAtDate.mahadasha?.planet ?: return 0.5
        val ascendantSign = ZodiacSign.fromLongitude(chart.ascendant)

        // Simplified scoring based on natural benefic/malefic and house ownership
        val isNaturalBenefic = mahadashaPlanet in listOf(Planet.JUPITER, Planet.VENUS, Planet.MERCURY, Planet.MOON)

        val mahadashaPosition = chart.planetPositions.find { it.planet == mahadashaPlanet }
        val housePosition = mahadashaPosition?.house ?: 1

        var score = if (isNaturalBenefic) 0.6 else 0.4

        // Adjust for house position
        when (housePosition) {
            1, 5, 9 -> score += 0.15
            4, 7, 10 -> score += 0.1
            3, 6, 11 -> score += 0.05
            8, 12 -> score -= 0.1
        }

        // Adjust for Antardasha
        dashaAtDate.antardasha?.planet?.let { antarPlanet ->
            val isAntarBenefic = antarPlanet in listOf(Planet.JUPITER, Planet.VENUS, Planet.MERCURY, Planet.MOON)
            score += if (isAntarBenefic) 0.1 else -0.05
        }

        return score.coerceIn(0.0, 1.0)
    }

    /**
     * Quick Transit score calculation for timeline
     */
    private fun calculateQuickTransitScore(chart: VedicChart, dateTime: LocalDateTime): Double {
        // This is a simplified calculation for timeline performance
        // Full transit analysis would be too expensive for every timeline point

        val natalMoon = chart.planetPositions.find { it.planet == Planet.MOON }!!.sign

        // Get approximate transit positions using Swiss Eph
        try {
            val transitChart = ephemerisEngine.calculateVedicChart(
                chart.birthData.copy(dateTime = dateTime),
                chart.houseSystem
            )

            var score = 0.5
            val favorableHouses = setOf(2, 5, 7, 9, 11) // Generally favorable houses

            // Check Jupiter and Saturn as primary slow movers
            transitChart.planetPositions.find { it.planet == Planet.JUPITER }?.let { jupiter ->
                val houseFromMoon = VedicAstrologyUtils.getHouseFromSigns(jupiter.sign, natalMoon)
                if (houseFromMoon in favorableHouses) score += 0.15
            }

            transitChart.planetPositions.find { it.planet == Planet.SATURN }?.let { saturn ->
                val houseFromMoon = VedicAstrologyUtils.getHouseFromSigns(saturn.sign, natalMoon)
                if (houseFromMoon in setOf(3, 6, 11)) {
                    score += 0.1
                } else if (houseFromMoon in setOf(1, 8, 12)) {
                    score -= 0.15
                }
            }

            return score.coerceIn(0.0, 1.0)
        } catch (e: Exception) {
            return 0.5
        }
    }

    /**
     * Identify peak periods from timeline
     */
    private fun identifyPeakPeriods(timeline: List<SuccessProbabilityPoint>): List<PredictionTimeWindow> {
        val peakPeriods = mutableListOf<PredictionTimeWindow>()
        var periodStart: LocalDate? = null
        var peakDate: LocalDate? = null
        var maxProb = 0.0
        val dominantInfluences = mutableListOf<String>()

        for (point in timeline) {
            if (point.probability >= PEAK_PERIOD_THRESHOLD) {
                if (periodStart == null) {
                    periodStart = point.date
                }
                if (point.probability > maxProb) {
                    maxProb = point.probability
                    peakDate = point.date
                }
                if (point.dominantFactor !in dominantInfluences) {
                    dominantInfluences.add("${point.dominantFactor}: ${point.recommendation}")
                }
            } else if (periodStart != null) {
                // End of peak period
                peakPeriods.add(PredictionTimeWindow(
                    startDate = periodStart.atStartOfDay(),
                    endDate = point.date.atStartOfDay(),
                    successProbability = maxProb,
                    peakDate = peakDate?.atStartOfDay(),
                    lifeAreaScores = emptyMap(),
                    dominantInfluences = dominantInfluences.toList()
                ))
                periodStart = null
                peakDate = null
                maxProb = 0.0
                dominantInfluences.clear()
            }
        }

        // Handle case where timeline ends during a peak period
        if (periodStart != null && timeline.isNotEmpty()) {
            peakPeriods.add(PredictionTimeWindow(
                startDate = periodStart.atStartOfDay(),
                endDate = timeline.last().date.atStartOfDay(),
                successProbability = maxProb,
                peakDate = peakDate?.atStartOfDay(),
                lifeAreaScores = emptyMap(),
                dominantInfluences = dominantInfluences.toList()
            ))
        }

        return peakPeriods
    }

    /**
     * Identify caution periods from timeline
     */
    private fun identifyCautionPeriods(timeline: List<SuccessProbabilityPoint>): List<PredictionTimeWindow> {
        val cautionPeriods = mutableListOf<PredictionTimeWindow>()
        var periodStart: LocalDate? = null
        var minProb = 1.0
        var lowestDate: LocalDate? = null
        val dominantInfluences = mutableListOf<String>()

        for (point in timeline) {
            if (point.probability < CAUTION_PERIOD_THRESHOLD) {
                if (periodStart == null) {
                    periodStart = point.date
                }
                if (point.probability < minProb) {
                    minProb = point.probability
                    lowestDate = point.date
                }
                if (point.dominantFactor !in dominantInfluences) {
                    dominantInfluences.add("${point.dominantFactor}: ${point.recommendation}")
                }
            } else if (periodStart != null) {
                cautionPeriods.add(PredictionTimeWindow(
                    startDate = periodStart.atStartOfDay(),
                    endDate = point.date.atStartOfDay(),
                    successProbability = minProb,
                    peakDate = lowestDate?.atStartOfDay(),
                    lifeAreaScores = emptyMap(),
                    dominantInfluences = dominantInfluences.toList()
                ))
                periodStart = null
                minProb = 1.0
                lowestDate = null
                dominantInfluences.clear()
            }
        }

        if (periodStart != null && timeline.isNotEmpty()) {
            cautionPeriods.add(PredictionTimeWindow(
                startDate = periodStart.atStartOfDay(),
                endDate = timeline.last().date.atStartOfDay(),
                successProbability = minProb,
                peakDate = lowestDate?.atStartOfDay(),
                lifeAreaScores = emptyMap(),
                dominantInfluences = dominantInfluences.toList()
            ))
        }

        return cautionPeriods
    }

    /**
     * Generate recommendations based on synthesis
     */
    private fun generateRecommendations(
        dashaPillar: DashaPillarAnalysis,
        transitPillar: TransitPillarAnalysis,
        ashtakavargaPillar: AshtakavargaPillarAnalysis,
        lifeAreaPredictions: Map<LifeAreaCategory, LifeAreaPrediction>
    ): List<String> {
        val recommendations = mutableListOf<String>()

        // Dasha-based recommendations
        when (dashaPillar.mahadashaLordNature) {
            DashaLordNature.HIGHLY_BENEFIC, DashaLordNature.BENEFIC -> {
                recommendations.add("Leverage the ${dashaPillar.mahadashaPlanet.displayName} Mahadasha period for major initiatives and growth.")
            }
            DashaLordNature.NEUTRAL -> {
                recommendations.add("The current Dasha is neutral - results depend heavily on transits and timing.")
            }
            else -> {
                recommendations.add("Consider remedies for ${dashaPillar.mahadashaPlanet.displayName} to mitigate challenging Dasha effects.")
            }
        }

        // Transit-based recommendations
        if (transitPillar.vedhaAffectedPlanets.isNotEmpty()) {
            recommendations.add("Some favorable transits are obstructed by Vedha - time initiatives carefully.")
        }

        transitPillar.significantTransits.take(2).forEach { transit ->
            if (transit.impact < -0.3) {
                recommendations.add("${transit.planet.displayName} transit requires patience: ${transit.description}")
            } else if (transit.impact > 0.3) {
                recommendations.add("Capitalize on ${transit.planet.displayName} transit: ${transit.description}")
            }
        }

        // Ashtakavarga-based recommendations
        if (ashtakavargaPillar.strongHouses.isNotEmpty()) {
            recommendations.add("Focus activities in houses ${ashtakavargaPillar.strongHouses.joinToString(", ")} (strong SAV support).")
        }

        // Life area specific recommendations
        val topArea = lifeAreaPredictions.maxByOrNull { it.value.successProbability }
        topArea?.let {
            if (it.value.successProbability >= 0.6) {
                recommendations.add("${it.key.displayName} shows excellent potential - prioritize efforts here.")
            }
        }

        val weakArea = lifeAreaPredictions.minByOrNull { it.value.successProbability }
        weakArea?.let {
            if (it.value.successProbability < 0.4) {
                recommendations.add("${it.key.displayName} needs patience and careful timing. Avoid forcing results.")
            }
        }

        return recommendations.distinct().take(7)
    }
}
