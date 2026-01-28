package com.astro.storm.ephemeris.triplepillar

import com.astro.storm.core.common.Language
import com.astro.storm.core.model.Planet
import com.astro.storm.core.model.ZodiacSign
import com.astro.storm.ephemeris.DashaCalculator.DashaLevel
import java.time.LocalDateTime

/**
 * Data models for Triple-Pillar Predictive Engine
 *
 * These models represent the synthesis of three fundamental Vedic astrology systems:
 * 1. Vimshottari Dasha - Planetary period system based on Moon's nakshatra
 * 2. Gochara (Transits) - Current planetary positions relative to natal chart
 * 3. Ashtakavarga - Point-based strength system for predicting transit effects
 *
 * The Triple-Pillar approach provides a comprehensive predictive framework by
 * cross-referencing these three systems to identify periods of maximum
 * auspiciousness or challenge.
 */

/**
 * Represents a single point on the success probability timeline
 */
data class ProbabilityPoint(
    val dateTime: LocalDateTime,
    val probability: Double,           // 0.0 to 1.0
    val dashaScore: Double,            // Contribution from Dasha (0-100)
    val transitScore: Double,          // Contribution from Gochara (0-100)
    val ashtakavargaScore: Double,     // Contribution from Ashtakavarga (0-100)
    val activeDashaLords: List<DashaLordInfo>,
    val transitHighlights: List<TransitHighlight>,
    val peakReason: String?            // Explanation if this is a peak point
) {
    val isPeak: Boolean get() = probability >= 0.75
    val isTrough: Boolean get() = probability <= 0.25

    val qualitativeRating: ProbabilityRating
        get() = when {
            probability >= 0.85 -> ProbabilityRating.EXCELLENT
            probability >= 0.70 -> ProbabilityRating.GOOD
            probability >= 0.50 -> ProbabilityRating.MODERATE
            probability >= 0.35 -> ProbabilityRating.CHALLENGING
            else -> ProbabilityRating.DIFFICULT
        }
}

/**
 * Qualitative rating for probability points
 */
enum class ProbabilityRating(val displayName: String, val colorHex: String) {
    EXCELLENT("Excellent", "#4CAF50"),   // Green
    GOOD("Good", "#8BC34A"),             // Light Green
    MODERATE("Moderate", "#FFC107"),     // Amber
    CHALLENGING("Challenging", "#FF9800"), // Orange
    DIFFICULT("Difficult", "#F44336")    // Red
}

/**
 * Information about an active Dasha lord at a given time
 */
data class DashaLordInfo(
    val planet: Planet,
    val level: DashaLevel,
    val startDate: LocalDateTime,
    val endDate: LocalDateTime,
    val strength: DashaStrength,
    val natalHouse: Int,               // House position in natal chart
    val ruledHouses: List<Int>,        // Houses ruled by this planet
    val dignity: PlanetaryDignity,     // Exalted, Own, Friendly, etc.
    val isYogaKaraka: Boolean          // Whether planet is Yoga Karaka for the chart
)

/**
 * Strength assessment for a Dasha lord
 */
enum class DashaStrength(val multiplier: Double, val description: String) {
    VERY_STRONG(1.5, "Very strong - exalted/own sign, angular house, forming Raja Yoga"),
    STRONG(1.25, "Strong - friendly sign, good house, beneficial aspects"),
    MODERATE(1.0, "Moderate - neutral placement"),
    WEAK(0.75, "Weak - enemy sign, dusthana, afflicted"),
    VERY_WEAK(0.5, "Very weak - debilitated, combust, heavily afflicted")
}

/**
 * Planetary dignity classification
 */
enum class PlanetaryDignity(val score: Int) {
    EXALTED(5),
    MOOLATRIKONA(4),
    OWN_SIGN(4),
    FRIENDLY(3),
    NEUTRAL(2),
    ENEMY(1),
    DEBILITATED(0)
}

/**
 * Highlight of a significant transit
 */
data class TransitHighlight(
    val transitingPlanet: Planet,
    val transitSign: ZodiacSign,
    val houseFromMoon: Int,
    val houseFromAscendant: Int,
    val isRetrograde: Boolean,
    val isFavorable: Boolean,
    val isVedhaAffected: Boolean,
    val bavBindus: Int,                // Bhinnashtakavarga score (0-8)
    val savBindus: Int,                // Sarvashtakavarga score (0-56)
    val significance: TransitSignificance
)

/**
 * Significance level of a transit
 */
enum class TransitSignificance(val weight: Double, val description: String) {
    MAJOR(1.0, "Major transit - slow planet (Saturn, Jupiter, Rahu/Ketu) changing sign"),
    SIGNIFICANT(0.75, "Significant - outer planet aspecting natal planet exactly"),
    MODERATE(0.5, "Moderate - fast planet in key position"),
    MINOR(0.25, "Minor - fast planet transit, limited duration")
}

/**
 * Complete success probability timeline analysis
 */
data class TriplePillarTimeline(
    val startDate: LocalDateTime,
    val endDate: LocalDateTime,
    val points: List<ProbabilityPoint>,
    val peaks: List<PeakPeriod>,
    val troughs: List<TroughPeriod>,
    val overallAssessment: TimelineAssessment,
    val calculationTimestamp: Long = System.currentTimeMillis()
) {
    /**
     * Find the best period within the timeline
     */
    fun findBestPeriod(): PeakPeriod? = peaks.maxByOrNull { it.averageProbability }

    /**
     * Find periods suitable for specific activities
     */
    fun findSuitablePeriods(activity: ActivityCategory): List<PeakPeriod> {
        return peaks.filter { peak ->
            peak.suitableActivities.contains(activity)
        }.sortedByDescending { it.averageProbability }
    }

    /**
     * Get probability at a specific date
     */
    fun getProbabilityAt(dateTime: LocalDateTime): ProbabilityPoint? {
        return points.minByOrNull {
            kotlin.math.abs(java.time.Duration.between(it.dateTime, dateTime).toMinutes())
        }
    }
}

/**
 * A peak period where probability is consistently high
 */
data class PeakPeriod(
    val startDate: LocalDateTime,
    val endDate: LocalDateTime,
    val averageProbability: Double,
    val peakProbability: Double,
    val peakDateTime: LocalDateTime,
    val primaryReason: String,
    val supportingFactors: List<String>,
    val suitableActivities: List<ActivityCategory>,
    val dashaLordsActive: List<Planet>,
    val transitHighlights: List<TransitHighlight>
)

/**
 * A trough period where probability is consistently low
 */
data class TroughPeriod(
    val startDate: LocalDateTime,
    val endDate: LocalDateTime,
    val averageProbability: Double,
    val lowestProbability: Double,
    val lowestDateTime: LocalDateTime,
    val primaryChallenge: String,
    val mitigatingFactors: List<String>,
    val activitiesToAvoid: List<ActivityCategory>,
    val remedialSuggestions: List<String>
)

/**
 * Categories of activities for auspiciousness matching
 */
enum class ActivityCategory(val displayName: String, val keyPlanets: List<Planet>) {
    CAREER(
        "Career & Profession",
        listOf(Planet.SUN, Planet.SATURN, Planet.MARS, Planet.JUPITER)
    ),
    FINANCE(
        "Finance & Wealth",
        listOf(Planet.JUPITER, Planet.VENUS, Planet.MERCURY)
    ),
    RELATIONSHIPS(
        "Relationships & Marriage",
        listOf(Planet.VENUS, Planet.MOON, Planet.JUPITER)
    ),
    HEALTH(
        "Health & Wellness",
        listOf(Planet.SUN, Planet.MOON, Planet.MARS)
    ),
    EDUCATION(
        "Education & Learning",
        listOf(Planet.JUPITER, Planet.MERCURY, Planet.MOON)
    ),
    TRAVEL(
        "Travel & Movement",
        listOf(Planet.MOON, Planet.MERCURY, Planet.RAHU)
    ),
    SPIRITUALITY(
        "Spirituality & Self-Development",
        listOf(Planet.JUPITER, Planet.KETU, Planet.MOON)
    ),
    PROPERTY(
        "Property & Real Estate",
        listOf(Planet.MARS, Planet.SATURN, Planet.VENUS, Planet.MOON)
    ),
    LEGAL(
        "Legal Matters",
        listOf(Planet.JUPITER, Planet.SUN, Planet.SATURN)
    ),
    GENERAL(
        "General Auspiciousness",
        Planet.MAIN_PLANETS
    );

    fun isKeyPlanet(planet: Planet): Boolean = planet in keyPlanets
}

/**
 * Overall assessment of the timeline
 */
data class TimelineAssessment(
    val overallQuality: TimelineQuality,
    val averageProbability: Double,
    val volatility: Double,            // Standard deviation of probability
    val trendDirection: TrendDirection,
    val dominantDashaInfluence: Planet?,
    val dominantTransitInfluence: Planet?,
    val keyRecommendations: List<String>,
    val summary: String
)

/**
 * Quality rating for the overall timeline
 */
enum class TimelineQuality(val displayName: String) {
    VERY_FAVORABLE("Very Favorable Period"),
    FAVORABLE("Favorable Period"),
    MIXED("Mixed Period"),
    CHALLENGING("Challenging Period"),
    VERY_CHALLENGING("Very Challenging Period")
}

/**
 * Direction of probability trend
 */
enum class TrendDirection(val displayName: String) {
    IMPROVING("Improving - probability trending upward"),
    STABLE("Stable - probability relatively constant"),
    DECLINING("Declining - probability trending downward"),
    VOLATILE("Volatile - significant fluctuations")
}

/**
 * Configuration for Triple-Pillar calculation
 */
data class TriplePillarConfig(
    val dashaWeight: Double = 0.35,      // Weight given to Dasha analysis
    val transitWeight: Double = 0.35,    // Weight given to transit analysis
    val ashtakavargaWeight: Double = 0.30, // Weight given to Ashtakavarga
    val includeOuterPlanets: Boolean = false,
    val samplingIntervalMinutes: Int = 60,  // How often to sample
    val peakThreshold: Double = 0.70,    // Threshold for identifying peaks
    val troughThreshold: Double = 0.30,  // Threshold for identifying troughs
    val minPeakDurationHours: Int = 4,   // Minimum duration for a peak period
    val considerRetrogrades: Boolean = true,
    val applyVedhaCorrection: Boolean = true,
    val includeSubDashas: Boolean = true,  // Include up to Pratyantardasha
    val language: Language = Language.ENGLISH
) {
    init {
        require(dashaWeight + transitWeight + ashtakavargaWeight == 1.0) {
            "Weights must sum to 1.0"
        }
        require(samplingIntervalMinutes in 5..1440) {
            "Sampling interval must be between 5 minutes and 24 hours"
        }
    }

    companion object {
        val DEFAULT = TriplePillarConfig()

        val HIGH_PRECISION = TriplePillarConfig(
            samplingIntervalMinutes = 15,
            includeSubDashas = true
        )

        val QUICK_OVERVIEW = TriplePillarConfig(
            samplingIntervalMinutes = 360,  // 6 hours
            includeSubDashas = false
        )
    }
}

/**
 * Event-based probability request
 */
data class EventProbabilityRequest(
    val eventCategory: ActivityCategory,
    val targetDate: LocalDateTime,
    val searchWindowDays: Int = 30,
    val minimumProbability: Double = 0.6,
    val prioritizeDashaLords: List<Planet>? = null
)

/**
 * Result of event probability analysis
 */
data class EventProbabilityResult(
    val eventCategory: ActivityCategory,
    val targetDate: LocalDateTime,
    val probabilityAtTarget: Double,
    val bestAlternatives: List<AlternativeWindow>,
    val recommendation: String,
    val detailedAnalysis: String
)

/**
 * Alternative time window for an event
 */
data class AlternativeWindow(
    val startDate: LocalDateTime,
    val endDate: LocalDateTime,
    val probability: Double,
    val reason: String,
    val rank: Int
)
