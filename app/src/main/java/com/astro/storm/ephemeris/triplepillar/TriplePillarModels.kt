package com.astro.storm.ephemeris.triplepillar

import com.astro.storm.core.common.Language
import com.astro.storm.core.common.StringResources
import com.astro.storm.core.model.Planet
import com.astro.storm.core.model.ZodiacSign
import com.astro.storm.ephemeris.DashaCalculator
import com.astro.storm.ephemeris.TransitAnalyzer
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

/**
 * Triple-Pillar Predictive Engine Models
 *
 * This module contains all data models for the Triple-Pillar synthesis engine
 * that combines Vimshottari Dasha, Gochara (Transits), and Ashtakavarga scores
 * to generate comprehensive "Success Probability" timelines.
 *
 * Based on the "Three Pillars of Timing" from classical texts:
 * 1. Dasha (Planetary Periods) - Shows karmic potential
 * 2. Gochara (Transits) - Shows current planetary influences
 * 3. Ashtakavarga (Bindu Strength) - Shows quantitative strength
 *
 * A prediction is most reliable when all three pillars align favorably.
 *
 * @author AstroStorm - Ultra-Precision Vedic Astrology
 */

/**
 * Overall probability rating for a time period
 */
enum class ProbabilityRating(
    val displayName: String,
    val minScore: Double,
    val maxScore: Double,
    val colorHex: String
) {
    PEAK("Peak Success", 80.0, 100.0, "#4CAF50"),
    HIGH("High Probability", 65.0, 80.0, "#8BC34A"),
    MODERATE("Moderate", 50.0, 65.0, "#FFC107"),
    LOW("Low Probability", 35.0, 50.0, "#FF9800"),
    CHALLENGING("Challenging", 20.0, 35.0, "#FF5722"),
    CRITICAL("Critical Period", 0.0, 20.0, "#F44336");

    companion object {
        fun fromScore(score: Double): ProbabilityRating {
            return entries.find { score >= it.minScore && score < it.maxScore }
                ?: if (score >= 100.0) PEAK else CRITICAL
        }
    }
}

/**
 * Dasha favorability assessment for a specific planet
 */
data class DashaFavorability(
    val planet: Planet,
    val dashaLevel: DashaCalculator.DashaLevel,
    val naturalBenefic: Boolean,
    val functionalBenefic: Boolean,
    val lordOfHouses: List<Int>,
    val dignity: PlanetaryDignity,
    val aspectedByBenefics: Boolean,
    val aspectedByMalefics: Boolean,
    val retrograde: Boolean,
    val combust: Boolean,
    val favorabilityScore: Double,
    val interpretation: String
) {
    val isFavorable: Boolean
        get() = favorabilityScore >= 60.0

    val isNeutral: Boolean
        get() = favorabilityScore in 40.0..60.0

    val isUnfavorable: Boolean
        get() = favorabilityScore < 40.0
}

/**
 * Planetary dignity categories
 */
enum class PlanetaryDignity(val displayName: String, val scoreMultiplier: Double) {
    EXALTED("Exalted", 1.5),
    OWN_SIGN("Own Sign", 1.3),
    MOOLATRIKONA("Moolatrikona", 1.4),
    FRIEND_SIGN("Friend's Sign", 1.15),
    NEUTRAL_SIGN("Neutral Sign", 1.0),
    ENEMY_SIGN("Enemy's Sign", 0.75),
    DEBILITATED("Debilitated", 0.5)
}

/**
 * Gochara (Transit) favorability assessment
 */
data class GocharaFavorability(
    val planet: Planet,
    val transitSign: ZodiacSign,
    val houseFromMoon: Int,
    val houseFromLagna: Int,
    val effect: TransitAnalyzer.TransitEffect,
    val isVedhaObstructed: Boolean,
    val vedhaSource: Planet?,
    val transitSpeed: TransitSpeed,
    val isRetrograde: Boolean,
    val favorabilityScore: Double,
    val interpretation: String
)

/**
 * Transit speed categories
 */
enum class TransitSpeed(val displayName: String) {
    FAST("Fast-moving"),
    MEDIUM("Medium-speed"),
    SLOW("Slow-moving"),
    STATIONARY("Stationary (powerful)")
}

/**
 * Ashtakavarga strength assessment for transit
 */
data class AshtakavargaStrength(
    val planet: Planet,
    val sign: ZodiacSign,
    val bavBindus: Int,
    val savBindus: Int,
    val kakshaLord: String,
    val kakshaFavorable: Boolean,
    val strengthLevel: BinduStrengthLevel,
    val favorabilityScore: Double,
    val interpretation: String
)

/**
 * Bindu strength levels based on Ashtakavarga scores
 */
enum class BinduStrengthLevel(
    val displayName: String,
    val bavMin: Int,
    val savMin: Int,
    val scoreBonus: Double
) {
    EXCELLENT("Excellent", 5, 30, 25.0),
    GOOD("Good", 4, 28, 15.0),
    AVERAGE("Average", 3, 25, 5.0),
    BELOW_AVERAGE("Below Average", 2, 22, -5.0),
    WEAK("Weak", 1, 18, -15.0),
    VERY_WEAK("Very Weak", 0, 0, -25.0);

    companion object {
        fun fromBindus(bav: Int, sav: Int): BinduStrengthLevel {
            return when {
                bav >= 5 && sav >= 30 -> EXCELLENT
                bav >= 4 && sav >= 28 -> GOOD
                bav >= 3 && sav >= 25 -> AVERAGE
                bav >= 2 && sav >= 22 -> BELOW_AVERAGE
                bav >= 1 && sav >= 18 -> WEAK
                else -> VERY_WEAK
            }
        }
    }
}

/**
 * Synthesized pillar analysis for a single planet at a point in time
 */
data class PlanetaryPillarSynthesis(
    val planet: Planet,
    val dateTime: LocalDateTime,
    val dashaFavorability: DashaFavorability?,
    val gocharaFavorability: GocharaFavorability,
    val ashtakavargaStrength: AshtakavargaStrength,
    val synthesizedScore: Double,
    val alignment: PillarAlignment,
    val interpretation: String
) {
    /**
     * Check if all three pillars are favorable
     */
    val isTripleFavorable: Boolean
        get() = (dashaFavorability?.isFavorable != false) &&
                gocharaFavorability.favorabilityScore >= 60.0 &&
                ashtakavargaStrength.favorabilityScore >= 60.0

    /**
     * Check if all three pillars are unfavorable
     */
    val isTripleUnfavorable: Boolean
        get() = (dashaFavorability?.isUnfavorable == true) &&
                gocharaFavorability.favorabilityScore < 40.0 &&
                ashtakavargaStrength.favorabilityScore < 40.0
}

/**
 * Pillar alignment status
 */
enum class PillarAlignment(val displayName: String, val scoreBonus: Double) {
    ALL_FAVORABLE("All Pillars Favorable", 15.0),
    TWO_FAVORABLE("Two Pillars Favorable", 5.0),
    MIXED("Mixed Signals", 0.0),
    TWO_UNFAVORABLE("Two Pillars Unfavorable", -5.0),
    ALL_UNFAVORABLE("All Pillars Unfavorable", -15.0)
}

/**
 * Success probability point on a timeline
 */
data class SuccessProbabilityPoint(
    val dateTime: LocalDateTime,
    val overallScore: Double,
    val rating: ProbabilityRating,
    val activeDashaLords: List<Pair<DashaCalculator.DashaLevel, Planet>>,
    val planetarySyntheses: List<PlanetaryPillarSynthesis>,
    val peakPlanets: List<Planet>,
    val challengingPlanets: List<Planet>,
    val recommendations: List<String>
) {
    /**
     * Check if this is a peak success window
     */
    val isPeakWindow: Boolean
        get() = rating == ProbabilityRating.PEAK || rating == ProbabilityRating.HIGH

    /**
     * Check if this is a critical period requiring caution
     */
    val isCriticalPeriod: Boolean
        get() = rating == ProbabilityRating.CRITICAL || rating == ProbabilityRating.CHALLENGING

    /**
     * Get the dominant dasha lord for this point
     */
    val dominantDashaLord: Planet?
        get() = activeDashaLords.firstOrNull()?.second

    fun toFormattedString(): String = buildString {
        append(dateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")))
        append(" | Score: ${String.format("%.1f", overallScore)}%")
        append(" | ${rating.displayName}")
        if (peakPlanets.isNotEmpty()) {
            append(" | Peak: ${peakPlanets.joinToString(", ") { it.displayName }}")
        }
    }
}

/**
 * Time range for timeline analysis
 */
data class TimelineRange(
    val startDate: LocalDateTime,
    val endDate: LocalDateTime,
    val intervalHours: Int = 24
) {
    val durationDays: Long
        get() = java.time.Duration.between(startDate, endDate).toDays()
}

/**
 * Complete Success Probability Timeline
 */
data class SuccessProbabilityTimeline(
    val range: TimelineRange,
    val points: List<SuccessProbabilityPoint>,
    val peakWindows: List<PeakWindow>,
    val criticalPeriods: List<CriticalPeriod>,
    val overallTrend: TimelineTrend,
    val monthlyAverages: Map<String, Double>,
    val bestDayOfWeek: java.time.DayOfWeek?,
    val summary: TimelineSummary,
    val generatedAt: LocalDateTime = LocalDateTime.now()
) {
    /**
     * Get the best date in the timeline
     */
    val bestDate: LocalDateTime?
        get() = points.maxByOrNull { it.overallScore }?.dateTime

    /**
     * Get the most challenging date in the timeline
     */
    val worstDate: LocalDateTime?
        get() = points.minByOrNull { it.overallScore }?.dateTime

    /**
     * Average success probability across the timeline
     */
    val averageScore: Double
        get() = if (points.isNotEmpty()) points.map { it.overallScore }.average() else 0.0

    /**
     * Get score for a specific date
     */
    fun getScoreForDate(date: LocalDate): SuccessProbabilityPoint? {
        return points.find { it.dateTime.toLocalDate() == date }
    }

    /**
     * Get all points within a score range
     */
    fun getPointsInRange(minScore: Double, maxScore: Double): List<SuccessProbabilityPoint> {
        return points.filter { it.overallScore in minScore..maxScore }
    }
}

/**
 * Peak success window
 */
data class PeakWindow(
    val startDateTime: LocalDateTime,
    val endDateTime: LocalDateTime,
    val peakScore: Double,
    val peakDateTime: LocalDateTime,
    val dominantPlanets: List<Planet>,
    val supportingFactors: List<String>,
    val recommendations: List<String>
) {
    val durationHours: Long
        get() = java.time.Duration.between(startDateTime, endDateTime).toHours()

    val durationDays: Long
        get() = java.time.Duration.between(startDateTime, endDateTime).toDays()
}

/**
 * Critical period requiring caution
 */
data class CriticalPeriod(
    val startDateTime: LocalDateTime,
    val endDateTime: LocalDateTime,
    val lowestScore: Double,
    val lowestDateTime: LocalDateTime,
    val challengingPlanets: List<Planet>,
    val challenges: List<String>,
    val mitigationStrategies: List<String>
) {
    val durationHours: Long
        get() = java.time.Duration.between(startDateTime, endDateTime).toHours()

    val durationDays: Long
        get() = java.time.Duration.between(startDateTime, endDateTime).toDays()
}

/**
 * Overall timeline trend
 */
enum class TimelineTrend(val displayName: String) {
    STRONGLY_IMPROVING("Strongly Improving"),
    IMPROVING("Improving"),
    STABLE_POSITIVE("Stable Positive"),
    STABLE_NEUTRAL("Stable Neutral"),
    STABLE_NEGATIVE("Stable Negative"),
    DECLINING("Declining"),
    STRONGLY_DECLINING("Strongly Declining"),
    VOLATILE("Volatile/Mixed")
}

/**
 * Timeline summary with key insights
 */
data class TimelineSummary(
    val overallOutlook: String,
    val bestPeriodDescription: String,
    val challengingPeriodDescription: String,
    val keyRecommendations: List<String>,
    val focusAreas: List<String>,
    val auspiciousDates: List<LocalDate>,
    val cautionDates: List<LocalDate>
)

/**
 * Life area specific analysis using Triple-Pillar
 */
enum class LifeAreaFocus(
    val displayName: String,
    val relevantHouses: List<Int>,
    val primaryPlanets: List<Planet>,
    val secondaryPlanets: List<Planet>
) {
    CAREER(
        "Career & Profession",
        listOf(10, 6, 2, 11),
        listOf(Planet.SUN, Planet.SATURN, Planet.MARS),
        listOf(Planet.JUPITER, Planet.MERCURY)
    ),
    FINANCE(
        "Finance & Wealth",
        listOf(2, 11, 5, 9),
        listOf(Planet.JUPITER, Planet.VENUS, Planet.MERCURY),
        listOf(Planet.MOON, Planet.SUN)
    ),
    RELATIONSHIPS(
        "Relationships & Marriage",
        listOf(7, 5, 2, 11),
        listOf(Planet.VENUS, Planet.JUPITER, Planet.MOON),
        listOf(Planet.MARS, Planet.SUN)
    ),
    HEALTH(
        "Health & Vitality",
        listOf(1, 6, 8, 12),
        listOf(Planet.SUN, Planet.MOON, Planet.MARS),
        listOf(Planet.SATURN, Planet.JUPITER)
    ),
    EDUCATION(
        "Education & Learning",
        listOf(4, 5, 9, 2),
        listOf(Planet.JUPITER, Planet.MERCURY, Planet.MOON),
        listOf(Planet.SUN, Planet.VENUS)
    ),
    SPIRITUALITY(
        "Spirituality & Growth",
        listOf(9, 12, 5, 8),
        listOf(Planet.JUPITER, Planet.KETU, Planet.SUN),
        listOf(Planet.MOON, Planet.SATURN)
    ),
    PROPERTY(
        "Property & Assets",
        listOf(4, 2, 11, 7),
        listOf(Planet.MARS, Planet.VENUS, Planet.SATURN),
        listOf(Planet.JUPITER, Planet.MOON)
    ),
    TRAVEL(
        "Travel & Movement",
        listOf(3, 9, 12, 7),
        listOf(Planet.MERCURY, Planet.MOON, Planet.RAHU),
        listOf(Planet.JUPITER, Planet.VENUS)
    ),
    CHILDREN(
        "Children & Progeny",
        listOf(5, 2, 11, 9),
        listOf(Planet.JUPITER, Planet.MOON, Planet.VENUS),
        listOf(Planet.SUN, Planet.MERCURY)
    ),
    LEGAL(
        "Legal & Disputes",
        listOf(6, 7, 8, 12),
        listOf(Planet.SATURN, Planet.MARS, Planet.SUN),
        listOf(Planet.JUPITER, Planet.RAHU)
    )
}

/**
 * Life area specific success probability
 */
data class LifeAreaProbability(
    val lifeArea: LifeAreaFocus,
    val dateTime: LocalDateTime,
    val overallScore: Double,
    val rating: ProbabilityRating,
    val houseStrengths: Map<Int, Double>,
    val planetaryContributions: Map<Planet, Double>,
    val interpretation: String,
    val recommendations: List<String>
)

/**
 * Configuration for Triple-Pillar analysis
 */
data class TriplePillarConfig(
    val dashaWeight: Double = 0.35,
    val gocharaWeight: Double = 0.35,
    val ashtakavargaWeight: Double = 0.30,
    val includeOuterPlanets: Boolean = false,
    val vedhaConsideration: Boolean = true,
    val kakshaAnalysis: Boolean = true,
    val retrogradeAdjustment: Boolean = true,
    val combustAdjustment: Boolean = true,
    val nakshataConsideration: Boolean = true,
    val yogaBonus: Boolean = true,
    val minimumDashaLevel: DashaCalculator.DashaLevel = DashaCalculator.DashaLevel.ANTARDASHA
) {
    init {
        require(dashaWeight + gocharaWeight + ashtakavargaWeight == 1.0) {
            "Weights must sum to 1.0"
        }
    }

    companion object {
        val DEFAULT = TriplePillarConfig()

        val DASHA_FOCUSED = TriplePillarConfig(
            dashaWeight = 0.50,
            gocharaWeight = 0.30,
            ashtakavargaWeight = 0.20
        )

        val TRANSIT_FOCUSED = TriplePillarConfig(
            dashaWeight = 0.25,
            gocharaWeight = 0.45,
            ashtakavargaWeight = 0.30
        )

        val BINDU_FOCUSED = TriplePillarConfig(
            dashaWeight = 0.30,
            gocharaWeight = 0.30,
            ashtakavargaWeight = 0.40
        )
    }
}

/**
 * Planetary relationship type for yoga assessment
 */
enum class PlanetaryRelationship(val displayName: String, val scoreModifier: Double) {
    BEST_FRIEND("Best Friend", 1.25),
    FRIEND("Friend", 1.15),
    NEUTRAL("Neutral", 1.0),
    ENEMY("Enemy", 0.85),
    BITTER_ENEMY("Bitter Enemy", 0.75)
}

/**
 * Dasha lord combination assessment
 */
data class DashaLordCombination(
    val mahadashaLord: Planet,
    val antardashaLord: Planet,
    val pratyantardashaLord: Planet?,
    val relationship: PlanetaryRelationship,
    val combinationQuality: CombinationQuality,
    val interpretation: String
)

/**
 * Quality of dasha lord combinations
 */
enum class CombinationQuality(val displayName: String, val scoreModifier: Double) {
    EXCELLENT("Excellent Combination", 1.25),
    GOOD("Good Combination", 1.10),
    AVERAGE("Average Combination", 1.0),
    CHALLENGING("Challenging Combination", 0.90),
    DIFFICULT("Difficult Combination", 0.75)
}
