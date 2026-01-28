package com.astro.storm.ephemeris.triplepillar

import android.content.Context
import com.astro.storm.core.model.Planet
import com.astro.storm.core.model.PlanetPosition
import com.astro.storm.core.model.VedicChart
import com.astro.storm.core.model.ZodiacSign
import com.astro.storm.core.model.BirthData
import com.astro.storm.core.model.HouseSystem
import com.astro.storm.ephemeris.AshtakavargaCalculator
import com.astro.storm.ephemeris.DashaCalculator
import com.astro.storm.ephemeris.SwissEphemerisEngine
import dagger.hilt.android.qualifiers.ApplicationContext
import java.time.Duration
import java.time.LocalDateTime
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.math.sqrt

/**
 * Triple-Pillar Predictive Engine
 *
 * This engine synthesizes three fundamental Vedic astrology systems to provide
 * a comprehensive "Success Probability" timeline:
 *
 * 1. **Dasha Pillar**: Evaluates the quality and favorability of active Vimshottari
 *    Dasha periods (Mahadasha, Antardasha, Pratyantardasha)
 *
 * 2. **Transit Pillar**: Analyzes current planetary transits using Gochara rules,
 *    Vedha points, and transit-to-natal aspects
 *
 * 3. **Ashtakavarga Pillar**: Quantifies transit strength using the bindu system
 *    (BAV, SAV, and Kaksha analysis)
 *
 * The synthesis produces a probability score (0-1) that peaks when:
 * - Favorable Dasha lords are active
 * - The Dasha lord transits high-bindu signs in Ashtakavarga
 * - Supportive Gochara positions align with the Dasha period
 *
 * ## Usage
 * ```kotlin
 * val engine = TriplePillarEngine(context, ephemerisEngine)
 * val timeline = engine.generateTimeline(
 *     natalChart = chart,
 *     startDate = LocalDateTime.now(),
 *     endDate = LocalDateTime.now().plusMonths(3)
 * )
 * val bestPeriod = timeline.findBestPeriod()
 * ```
 *
 * @author AstroStorm - Ultra-Precision Vedic Astrology Engine
 */
@Singleton
class TriplePillarEngine @Inject constructor(
    @ApplicationContext private val context: Context,
    private val ephemerisEngine: SwissEphemerisEngine
) {

    /**
     * Generate a complete Triple-Pillar timeline for a date range
     *
     * @param natalChart The birth chart to analyze
     * @param startDate Start of the analysis period
     * @param endDate End of the analysis period
     * @param config Configuration options
     * @return Complete timeline with probability points, peaks, and troughs
     */
    fun generateTimeline(
        natalChart: VedicChart,
        startDate: LocalDateTime,
        endDate: LocalDateTime,
        config: TriplePillarConfig = TriplePillarConfig.DEFAULT
    ): TriplePillarTimeline {
        require(!endDate.isBefore(startDate)) { "End date must be after start date" }

        // Pre-calculate static analyses
        val dashaTimeline = DashaCalculator.calculateDashaTimeline(natalChart)
        val ashtakavargaAnalysis = AshtakavargaCalculator.calculateAshtakavarga(natalChart)

        // Generate probability points
        val points = mutableListOf<ProbabilityPoint>()
        var currentDateTime = startDate
        val interval = Duration.ofMinutes(config.samplingIntervalMinutes.toLong())

        while (!currentDateTime.isAfter(endDate)) {
            val point = calculateProbabilityPoint(
                natalChart = natalChart,
                targetDateTime = currentDateTime,
                dashaTimeline = dashaTimeline,
                ashtakavargaAnalysis = ashtakavargaAnalysis,
                config = config
            )
            points.add(point)
            currentDateTime = currentDateTime.plus(interval)
        }

        // Identify peaks and troughs
        val peaks = identifyPeaks(points, config)
        val troughs = identifyTroughs(points, config)

        // Calculate overall assessment
        val assessment = calculateTimelineAssessment(points, peaks, troughs, config)

        return TriplePillarTimeline(
            startDate = startDate,
            endDate = endDate,
            points = points,
            peaks = peaks,
            troughs = troughs,
            overallAssessment = assessment
        )
    }

    /**
     * Calculate probability for a single point in time
     *
     * @param natalChart The birth chart
     * @param targetDateTime The specific date/time to analyze
     * @param dashaTimeline Pre-calculated Dasha timeline
     * @param ashtakavargaAnalysis Pre-calculated Ashtakavarga analysis
     * @param config Configuration options
     * @return ProbabilityPoint with detailed breakdown
     */
    fun calculateProbabilityPoint(
        natalChart: VedicChart,
        targetDateTime: LocalDateTime,
        dashaTimeline: DashaCalculator.DashaTimeline? = null,
        ashtakavargaAnalysis: AshtakavargaCalculator.AshtakavargaAnalysis? = null,
        config: TriplePillarConfig = TriplePillarConfig.DEFAULT
    ): ProbabilityPoint {
        // Calculate or use pre-calculated analyses
        val actualDashaTimeline = dashaTimeline
            ?: DashaCalculator.calculateDashaTimeline(natalChart)
        val actualAshtakavargaAnalysis = ashtakavargaAnalysis
            ?: AshtakavargaCalculator.calculateAshtakavarga(natalChart)

        // Get transit positions for the target date
        val transitPositions = getTransitPositions(targetDateTime, natalChart.birthData.timezone)

        // Evaluate each pillar
        val dashaPillarResult = DashaPillar.evaluateDashaPillar(
            natalChart, actualDashaTimeline, targetDateTime, config
        )

        val transitPillarResult = TransitPillar.evaluateTransitPillar(
            natalChart, transitPositions, config
        )

        val ashtakavargaPillarResult = AshtakavargaPillar.evaluateAshtakavargaPillar(
            natalChart, transitPositions, actualAshtakavargaAnalysis, config
        )

        // Check for Dasha-Transit alignment (bonus when Dasha lord transits high-bindu sign)
        val alignmentBonus = calculateDashaTransitAlignment(
            dashaPillarResult, transitPositions, actualAshtakavargaAnalysis
        )

        // Calculate weighted composite probability
        val rawScore = (
            dashaPillarResult.score * config.dashaWeight +
            transitPillarResult.score * config.transitWeight +
            ashtakavargaPillarResult.score * config.ashtakavargaWeight
        )

        // Apply alignment bonus (up to 10% boost)
        val adjustedScore = (rawScore + alignmentBonus).coerceIn(0.0, 100.0)

        // Convert to probability (0-1 scale)
        val probability = adjustedScore / 100.0

        // Determine if this is a peak and why
        val peakReason = if (probability >= config.peakThreshold) {
            determinePeakReason(dashaPillarResult, transitPillarResult, ashtakavargaPillarResult, alignmentBonus)
        } else null

        return ProbabilityPoint(
            dateTime = targetDateTime,
            probability = probability,
            dashaScore = dashaPillarResult.score,
            transitScore = transitPillarResult.score,
            ashtakavargaScore = ashtakavargaPillarResult.score,
            activeDashaLords = dashaPillarResult.activeLords,
            transitHighlights = transitPillarResult.transitHighlights,
            peakReason = peakReason
        )
    }

    /**
     * Calculate bonus when Dasha lord transits a favorable sign per Ashtakavarga
     */
    private fun calculateDashaTransitAlignment(
        dashaPillarResult: DashaPillarResult,
        transitPositions: List<PlanetPosition>,
        ashtakavargaAnalysis: AshtakavargaCalculator.AshtakavargaAnalysis
    ): Double {
        var totalBonus = 0.0
        var lordCount = 0

        for (lord in dashaPillarResult.activeLords) {
            val planet = lord.planet

            // Skip Rahu/Ketu as they're not in traditional Ashtakavarga
            if (planet in listOf(Planet.RAHU, Planet.KETU)) continue

            val transitPosition = transitPositions.find { it.planet == planet } ?: continue
            val transitSign = transitPosition.sign

            // Get Ashtakavarga scores
            val bavScore = ashtakavargaAnalysis.bhinnashtakavarga[planet]
                ?.getBindusForSign(transitSign) ?: 0
            val savScore = ashtakavargaAnalysis.sarvashtakavarga.getBindusForSign(transitSign)

            // Calculate alignment bonus based on bindu strength
            val binduBonus = when {
                bavScore >= 5 && savScore >= 30 -> 10.0  // Excellent alignment
                bavScore >= 4 && savScore >= 28 -> 7.0   // Good alignment
                bavScore >= 3 && savScore >= 25 -> 4.0   // Moderate alignment
                bavScore <= 2 && savScore <= 22 -> -5.0  // Poor alignment (penalty)
                else -> 0.0
            }

            // Weight by Dasha level importance
            val levelWeight = when (lord.level) {
                DashaCalculator.DashaLevel.MAHADASHA -> 1.0
                DashaCalculator.DashaLevel.ANTARDASHA -> 0.6
                DashaCalculator.DashaLevel.PRATYANTARDASHA -> 0.3
                else -> 0.2
            }

            totalBonus += binduBonus * levelWeight
            lordCount++
        }

        return if (lordCount > 0) totalBonus / lordCount else 0.0
    }

    /**
     * Determine the reason for a probability peak
     */
    private fun determinePeakReason(
        dashaPillarResult: DashaPillarResult,
        transitPillarResult: TransitPillarResult,
        ashtakavargaPillarResult: AshtakavargaPillarResult,
        alignmentBonus: Double
    ): String {
        val reasons = mutableListOf<String>()

        if (dashaPillarResult.score >= 70) {
            val mainLord = dashaPillarResult.activeLords.firstOrNull()
            reasons.add("Strong ${mainLord?.planet?.displayName ?: "Dasha"} period")
        }

        if (transitPillarResult.score >= 70) {
            val favorableTransits = transitPillarResult.transitAnalyses
                .filter { it.isFavorable }
                .take(2)
                .map { it.planet.displayName }
            if (favorableTransits.isNotEmpty()) {
                reasons.add("Favorable ${favorableTransits.joinToString(" & ")} transits")
            }
        }

        if (ashtakavargaPillarResult.score >= 70) {
            reasons.add("High Ashtakavarga support")
        }

        if (alignmentBonus >= 7) {
            reasons.add("Dasha lord in high-bindu sign")
        }

        return if (reasons.isNotEmpty()) {
            reasons.joinToString("; ")
        } else {
            "Multiple favorable factors aligning"
        }
    }

    /**
     * Identify peak periods in the timeline
     */
    private fun identifyPeaks(
        points: List<ProbabilityPoint>,
        config: TriplePillarConfig
    ): List<PeakPeriod> {
        val peaks = mutableListOf<PeakPeriod>()
        var peakStart: ProbabilityPoint? = null
        var peakPoints = mutableListOf<ProbabilityPoint>()

        for (point in points) {
            if (point.probability >= config.peakThreshold) {
                if (peakStart == null) {
                    peakStart = point
                }
                peakPoints.add(point)
            } else {
                if (peakStart != null && peakPoints.isNotEmpty()) {
                    val duration = Duration.between(peakStart.dateTime, peakPoints.last().dateTime)
                    if (duration.toHours() >= config.minPeakDurationHours) {
                        peaks.add(createPeakPeriod(peakPoints))
                    }
                }
                peakStart = null
                peakPoints = mutableListOf()
            }
        }

        // Handle peak at the end
        if (peakStart != null && peakPoints.isNotEmpty()) {
            val duration = Duration.between(peakStart.dateTime, peakPoints.last().dateTime)
            if (duration.toHours() >= config.minPeakDurationHours) {
                peaks.add(createPeakPeriod(peakPoints))
            }
        }

        return peaks.sortedByDescending { it.averageProbability }
    }

    /**
     * Create a PeakPeriod from a list of points
     */
    private fun createPeakPeriod(points: List<ProbabilityPoint>): PeakPeriod {
        val peakPoint = points.maxByOrNull { it.probability }!!
        val avgProbability = points.map { it.probability }.average()

        // Collect active Dasha lords
        val allDashaLords = points.flatMap { it.activeDashaLords }
            .distinctBy { it.planet }
            .map { it.planet }

        // Collect significant transit highlights
        val allHighlights = points.flatMap { it.transitHighlights }
            .filter { it.significance != TransitSignificance.MINOR }
            .distinctBy { it.transitingPlanet }

        // Determine suitable activities based on active planets
        val suitableActivities = determineSuitableActivities(allDashaLords, allHighlights)

        // Build supporting factors
        val supportingFactors = mutableListOf<String>()
        if (allDashaLords.any { it == Planet.JUPITER }) supportingFactors.add("Jupiter influence")
        if (allDashaLords.any { it == Planet.VENUS }) supportingFactors.add("Venus influence")
        if (allHighlights.any { it.isFavorable && it.transitingPlanet == Planet.JUPITER }) {
            supportingFactors.add("Favorable Jupiter transit")
        }

        return PeakPeriod(
            startDate = points.first().dateTime,
            endDate = points.last().dateTime,
            averageProbability = avgProbability,
            peakProbability = peakPoint.probability,
            peakDateTime = peakPoint.dateTime,
            primaryReason = peakPoint.peakReason ?: "Multiple favorable factors",
            supportingFactors = supportingFactors,
            suitableActivities = suitableActivities,
            dashaLordsActive = allDashaLords,
            transitHighlights = allHighlights
        )
    }

    /**
     * Identify trough periods in the timeline
     */
    private fun identifyTroughs(
        points: List<ProbabilityPoint>,
        config: TriplePillarConfig
    ): List<TroughPeriod> {
        val troughs = mutableListOf<TroughPeriod>()
        var troughStart: ProbabilityPoint? = null
        var troughPoints = mutableListOf<ProbabilityPoint>()

        for (point in points) {
            if (point.probability <= config.troughThreshold) {
                if (troughStart == null) {
                    troughStart = point
                }
                troughPoints.add(point)
            } else {
                if (troughStart != null && troughPoints.isNotEmpty()) {
                    val duration = Duration.between(troughStart.dateTime, troughPoints.last().dateTime)
                    if (duration.toHours() >= config.minPeakDurationHours) {
                        troughs.add(createTroughPeriod(troughPoints))
                    }
                }
                troughStart = null
                troughPoints = mutableListOf()
            }
        }

        // Handle trough at the end
        if (troughStart != null && troughPoints.isNotEmpty()) {
            val duration = Duration.between(troughStart.dateTime, troughPoints.last().dateTime)
            if (duration.toHours() >= config.minPeakDurationHours) {
                troughs.add(createTroughPeriod(troughPoints))
            }
        }

        return troughs.sortedBy { it.averageProbability }
    }

    /**
     * Create a TroughPeriod from a list of points
     */
    private fun createTroughPeriod(points: List<ProbabilityPoint>): TroughPeriod {
        val lowestPoint = points.minByOrNull { it.probability }!!
        val avgProbability = points.map { it.probability }.average()

        // Identify challenges
        val challenges = mutableListOf<String>()
        if (lowestPoint.dashaScore < 40) challenges.add("Challenging Dasha period")
        if (lowestPoint.transitScore < 40) challenges.add("Difficult transits")
        if (lowestPoint.ashtakavargaScore < 40) challenges.add("Weak Ashtakavarga support")

        // Determine activities to avoid
        val activitiesToAvoid = listOf(
            ActivityCategory.CAREER,
            ActivityCategory.FINANCE,
            ActivityCategory.LEGAL
        ).filter { activity ->
            // Avoid activities where key planets are weak
            val keyPlanets = activity.keyPlanets
            lowestPoint.activeDashaLords.none { lord ->
                lord.planet in keyPlanets && lord.strength.multiplier >= 1.0
            }
        }

        // Generate remedial suggestions
        val remedialSuggestions = generateRemedialSuggestions(lowestPoint)

        return TroughPeriod(
            startDate = points.first().dateTime,
            endDate = points.last().dateTime,
            averageProbability = avgProbability,
            lowestProbability = lowestPoint.probability,
            lowestDateTime = lowestPoint.dateTime,
            primaryChallenge = challenges.firstOrNull() ?: "Multiple challenging factors",
            mitigatingFactors = findMitigatingFactors(lowestPoint),
            activitiesToAvoid = activitiesToAvoid,
            remedialSuggestions = remedialSuggestions
        )
    }

    /**
     * Calculate overall timeline assessment
     */
    private fun calculateTimelineAssessment(
        points: List<ProbabilityPoint>,
        peaks: List<PeakPeriod>,
        troughs: List<TroughPeriod>,
        config: TriplePillarConfig
    ): TimelineAssessment {
        val avgProbability = points.map { it.probability }.average()

        // Calculate volatility (standard deviation)
        val variance = points.map { (it.probability - avgProbability).let { d -> d * d } }.average()
        val volatility = sqrt(variance)

        // Determine trend
        val firstQuarterAvg = points.take(points.size / 4).map { it.probability }.average()
        val lastQuarterAvg = points.takeLast(points.size / 4).map { it.probability }.average()
        val trend = when {
            lastQuarterAvg - firstQuarterAvg > 0.1 -> TrendDirection.IMPROVING
            firstQuarterAvg - lastQuarterAvg > 0.1 -> TrendDirection.DECLINING
            volatility > 0.15 -> TrendDirection.VOLATILE
            else -> TrendDirection.STABLE
        }

        // Determine overall quality
        val quality = when {
            avgProbability >= 0.70 && peaks.size >= 2 -> TimelineQuality.VERY_FAVORABLE
            avgProbability >= 0.55 -> TimelineQuality.FAVORABLE
            avgProbability >= 0.45 -> TimelineQuality.MIXED
            avgProbability >= 0.35 -> TimelineQuality.CHALLENGING
            else -> TimelineQuality.VERY_CHALLENGING
        }

        // Find dominant influences
        val dominantDasha = points.flatMap { it.activeDashaLords }
            .groupBy { it.planet }
            .maxByOrNull { it.value.size }
            ?.key

        val dominantTransit = points.flatMap { it.transitHighlights }
            .filter { it.significance == TransitSignificance.MAJOR }
            .groupBy { it.transitingPlanet }
            .maxByOrNull { it.value.size }
            ?.key

        // Generate recommendations
        val recommendations = generateRecommendations(quality, trend, peaks, troughs)

        val summary = buildTimelineSummary(quality, trend, avgProbability, peaks.size, troughs.size)

        return TimelineAssessment(
            overallQuality = quality,
            averageProbability = avgProbability,
            volatility = volatility,
            trendDirection = trend,
            dominantDashaInfluence = dominantDasha,
            dominantTransitInfluence = dominantTransit,
            keyRecommendations = recommendations,
            summary = summary
        )
    }

    /**
     * Get transit positions for a specific datetime
     */
    private fun getTransitPositions(dateTime: LocalDateTime, timezone: String): List<PlanetPosition> {
        val transitBirthData = BirthData(
            name = "Transit",
            dateTime = dateTime,
            latitude = 0.0,
            longitude = 0.0,
            timezone = timezone,
            location = "Transit Chart"
        )

        val transitChart = ephemerisEngine.calculateVedicChart(transitBirthData, HouseSystem.DEFAULT)
        return transitChart.planetPositions
    }

    /**
     * Determine suitable activities based on active planets
     */
    private fun determineSuitableActivities(
        dashaLords: List<Planet>,
        highlights: List<TransitHighlight>
    ): List<ActivityCategory> {
        val suitableActivities = mutableSetOf<ActivityCategory>()

        for (activity in ActivityCategory.entries) {
            val hasKeyDashaLord = dashaLords.any { activity.isKeyPlanet(it) }
            val hasFavorableKeyTransit = highlights.any {
                it.isFavorable && activity.isKeyPlanet(it.transitingPlanet)
            }

            if (hasKeyDashaLord && hasFavorableKeyTransit) {
                suitableActivities.add(activity)
            } else if (hasKeyDashaLord || hasFavorableKeyTransit) {
                // Add if planet strength is good
                if (dashaLords.any { it in activity.keyPlanets }) {
                    suitableActivities.add(activity)
                }
            }
        }

        // Always include GENERAL if other activities are suitable
        if (suitableActivities.isNotEmpty()) {
            suitableActivities.add(ActivityCategory.GENERAL)
        }

        return suitableActivities.toList()
    }

    /**
     * Generate remedial suggestions for challenging periods
     */
    private fun generateRemedialSuggestions(point: ProbabilityPoint): List<String> {
        val suggestions = mutableListOf<String>()

        // Based on weak Dasha lords
        for (lord in point.activeDashaLords) {
            if (lord.strength == DashaStrength.WEAK || lord.strength == DashaStrength.VERY_WEAK) {
                suggestions.add("Strengthen ${lord.planet.displayName} through its gemstone or mantra")
            }
        }

        // Based on challenging transits
        for (highlight in point.transitHighlights) {
            if (!highlight.isFavorable && highlight.significance == TransitSignificance.MAJOR) {
                when (highlight.transitingPlanet) {
                    Planet.SATURN -> suggestions.add("Offer sesame oil to Shani on Saturdays")
                    Planet.RAHU -> suggestions.add("Recite Rahu stotra; avoid risky decisions")
                    Planet.MARS -> suggestions.add("Observe fast on Tuesdays; avoid conflicts")
                    else -> {}
                }
            }
        }

        // General suggestions
        suggestions.add("Practice patience and mindfulness during this period")
        suggestions.add("Avoid major new initiatives; focus on routine matters")

        return suggestions.take(5)
    }

    /**
     * Find mitigating factors in challenging periods
     */
    private fun findMitigatingFactors(point: ProbabilityPoint): List<String> {
        val factors = mutableListOf<String>()

        // Look for any positive elements
        for (lord in point.activeDashaLords) {
            if (lord.strength.multiplier >= 1.0) {
                factors.add("${lord.planet.displayName} maintains reasonable strength")
            }
            if (lord.isYogaKaraka) {
                factors.add("${lord.planet.displayName} is Yoga Karaka - provides some protection")
            }
        }

        for (highlight in point.transitHighlights) {
            if (highlight.isFavorable) {
                factors.add("${highlight.transitingPlanet.displayName} transit is supportive")
            }
        }

        if (point.ashtakavargaScore >= 45) {
            factors.add("Ashtakavarga support provides baseline stability")
        }

        return factors
    }

    /**
     * Generate recommendations based on timeline assessment
     */
    private fun generateRecommendations(
        quality: TimelineQuality,
        trend: TrendDirection,
        peaks: List<PeakPeriod>,
        troughs: List<TroughPeriod>
    ): List<String> {
        val recommendations = mutableListOf<String>()

        when (quality) {
            TimelineQuality.VERY_FAVORABLE, TimelineQuality.FAVORABLE -> {
                recommendations.add("Excellent time for new initiatives and important decisions")
                if (peaks.isNotEmpty()) {
                    recommendations.add("Best windows: around ${peaks.first().peakDateTime.toLocalDate()}")
                }
            }
            TimelineQuality.MIXED -> {
                recommendations.add("Timing is important - plan carefully around favorable windows")
                if (peaks.isNotEmpty()) {
                    recommendations.add("Target activities during peak periods for best results")
                }
            }
            TimelineQuality.CHALLENGING, TimelineQuality.VERY_CHALLENGING -> {
                recommendations.add("Focus on consolidation rather than expansion")
                recommendations.add("Use this period for planning and preparation")
            }
        }

        when (trend) {
            TrendDirection.IMPROVING -> {
                recommendations.add("Conditions improving - patience will be rewarded")
            }
            TrendDirection.DECLINING -> {
                recommendations.add("Complete important matters sooner rather than later")
            }
            TrendDirection.VOLATILE -> {
                recommendations.add("Period shows fluctuations - remain adaptable")
            }
            else -> {}
        }

        return recommendations.take(5)
    }

    /**
     * Build timeline summary text
     */
    private fun buildTimelineSummary(
        quality: TimelineQuality,
        trend: TrendDirection,
        avgProbability: Double,
        peakCount: Int,
        troughCount: Int
    ): String {
        return buildString {
            append("${quality.displayName} with ${trend.displayName.lowercase()}. ")
            append("Average success probability: ${String.format("%.0f", avgProbability * 100)}%. ")
            append("Found $peakCount favorable windows")
            if (troughCount > 0) {
                append(" and $troughCount challenging periods")
            }
            append(".")
        }
    }

    /**
     * Find the best time window for a specific activity
     *
     * @param natalChart The birth chart
     * @param activity The activity category to optimize for
     * @param startDate Start of search range
     * @param endDate End of search range
     * @param config Configuration options
     * @return EventProbabilityResult with best windows
     */
    fun findBestTimeForActivity(
        natalChart: VedicChart,
        activity: ActivityCategory,
        startDate: LocalDateTime,
        endDate: LocalDateTime,
        config: TriplePillarConfig = TriplePillarConfig.DEFAULT
    ): EventProbabilityResult {
        val timeline = generateTimeline(natalChart, startDate, endDate, config)

        val suitablePeaks = timeline.findSuitablePeriods(activity)

        val alternatives = suitablePeaks.take(5).mapIndexed { index, peak ->
            AlternativeWindow(
                startDate = peak.startDate,
                endDate = peak.endDate,
                probability = peak.averageProbability,
                reason = peak.primaryReason,
                rank = index + 1
            )
        }

        val targetProbability = timeline.getProbabilityAt(startDate)?.probability ?: 0.5

        val recommendation = if (alternatives.isNotEmpty()) {
            "Best window for ${activity.displayName}: ${alternatives.first().startDate.toLocalDate()}"
        } else {
            "No optimal windows found; consider expanding search range"
        }

        return EventProbabilityResult(
            eventCategory = activity,
            targetDate = startDate,
            probabilityAtTarget = targetProbability,
            bestAlternatives = alternatives,
            recommendation = recommendation,
            detailedAnalysis = timeline.overallAssessment.summary
        )
    }

    /**
     * Get a quick snapshot probability for the current moment
     */
    fun getCurrentProbability(natalChart: VedicChart): ProbabilityPoint {
        return calculateProbabilityPoint(
            natalChart = natalChart,
            targetDateTime = LocalDateTime.now()
        )
    }
}
