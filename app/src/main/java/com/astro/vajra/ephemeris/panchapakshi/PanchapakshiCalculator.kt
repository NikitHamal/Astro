package com.astro.vajra.ephemeris.panchapakshi

import com.astro.vajra.core.model.Nakshatra
import com.astro.vajra.core.model.Planet
import com.astro.vajra.core.model.PlanetPosition
import com.astro.vajra.core.model.VedicChart
import com.astro.vajra.ephemeris.VedicAstrologyUtils
import java.time.DayOfWeek
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.Duration
import kotlin.math.PI
import kotlin.math.abs
import kotlin.math.acos
import kotlin.math.cos
import kotlin.math.roundToInt
import kotlin.math.sin
import kotlin.math.tan

// =====================================================================================
// PANCHAPAKSHI DATA MODELS
// =====================================================================================

/**
 * Represents one of the five sacred birds in the Panchapakshi system.
 *
 * Each bird is associated with a planetary ruler and embodies specific energetic qualities
 * from the Tamil Siddha tradition. The five birds represent the cyclical nature of
 * bio-rhythmic energy throughout the day and night.
 *
 * @property tamil The Tamil name of the bird
 * @property english The English name of the bird
 * @property ruler The planetary ruler governing this bird's energy
 */
enum class PakshiBird(val tamil: String, val english: String, val ruler: Planet) {
    VULTURE("Kazhugu", "Vulture", Planet.SATURN),
    OWL("Andai", "Owl", Planet.MARS),
    CROW("Kaakam", "Crow", Planet.SUN),
    COCK("Kozhi", "Cock", Planet.JUPITER),
    PEACOCK("Mayil", "Peacock", Planet.VENUS);

    companion object {
        /** Returns the bird at the given ordinal offset (wrapping cyclically). */
        fun atCyclicIndex(index: Int): PakshiBird {
            val normalized = ((index % 5) + 5) % 5
            return entries[normalized]
        }
    }
}

/**
 * Represents one of the five activities each bird cycles through.
 *
 * Activities are ordered from most powerful (Ruling) to least powerful (Dying).
 * The strength value indicates the bio-rhythmic potency during that activity phase.
 *
 * @property tamil Tamil name of the activity
 * @property english English name of the activity
 * @property strength Vibrational strength score (0-100)
 */
enum class PakshiActivity(val tamil: String, val english: String, val strength: Int) {
    RULING("Aatchi", "Ruling", 100),
    EATING("Unavu", "Eating", 80),
    WALKING("Nadai", "Walking", 60),
    SLEEPING("Urakkam", "Sleeping", 30),
    DYING("Maranam", "Dying", 0);

    companion object {
        /** Returns the activity at the given ordinal offset (wrapping cyclically). */
        fun atCyclicIndex(index: Int): PakshiActivity {
            val normalized = ((index % 5) + 5) % 5
            return entries[normalized]
        }
    }
}

/**
 * Paksha (lunar fortnight) used in Panchapakshi birth bird calculation.
 */
enum class Paksha(val displayName: String) {
    SHUKLA("Shukla Paksha (Waxing Moon)"),
    KRISHNA("Krishna Paksha (Waning Moon)")
}

/**
 * The complete activity state of a single bird at a given moment.
 *
 * @property bird The bird whose state is described
 * @property activity The primary activity the bird is engaged in
 * @property subActivity The sub-activity within the primary period
 * @property strength Composite vibrational strength (0-100)
 * @property isRuling Whether this bird is currently in its most powerful state
 */
data class BirdActivityState(
    val bird: PakshiBird,
    val activity: PakshiActivity,
    val subActivity: PakshiActivity,
    val strength: Int,
    val isRuling: Boolean
)

/**
 * A time segment within the Panchapakshi daily cycle.
 *
 * @property segmentNumber Segment index within the day or night half (1-5)
 * @property startTime Start time of this segment
 * @property endTime End time of this segment
 * @property bird The bird active in this segment
 * @property activity The primary activity during this segment
 * @property subSegments The five sub-segments within this main segment
 * @property isDaytime Whether this segment falls during daytime
 */
data class PakshiTimeSegment(
    val segmentNumber: Int,
    val startTime: LocalTime,
    val endTime: LocalTime,
    val bird: PakshiBird,
    val activity: PakshiActivity,
    val subSegments: List<PakshiSubSegment>,
    val isDaytime: Boolean
)

/**
 * A sub-segment (1/25 of day or night half) within a main Panchapakshi segment.
 *
 * @property subSegmentNumber Sub-segment index within the parent segment (1-5)
 * @property startTime Start time
 * @property endTime End time
 * @property subActivity The activity active during this sub-period
 */
data class PakshiSubSegment(
    val subSegmentNumber: Int,
    val startTime: LocalTime,
    val endTime: LocalTime,
    val subActivity: PakshiActivity
)

/**
 * A timeline entry for display, combining time, bird, activities, and strength.
 *
 * @property startTime Start time of the entry
 * @property endTime End time of the entry
 * @property bird The bird for this period
 * @property activity Primary activity
 * @property subActivity Sub-activity
 * @property strength Composite vibrational strength
 * @property isDaytime Whether this falls in the day half
 * @property segmentNumber Main segment number (1-5)
 * @property subSegmentNumber Sub-segment number (1-5)
 */
data class PakshiTimelineEntry(
    val startTime: LocalTime,
    val endTime: LocalTime,
    val bird: PakshiBird,
    val activity: PakshiActivity,
    val subActivity: PakshiActivity,
    val strength: Int,
    val isDaytime: Boolean,
    val segmentNumber: Int,
    val subSegmentNumber: Int
)

/**
 * Interpretive guidance based on the current Panchapakshi state.
 *
 * @property currentActivityMeaning Description of the current primary activity's significance
 * @property currentSubActivityMeaning Description of the current sub-activity's significance
 * @property overallGuidance General guidance for the current moment
 * @property bestTimesForAction List of upcoming periods favorable for action
 * @property warningPeriods List of upcoming periods to avoid important actions
 * @property birdPersonality Character traits associated with the birth bird
 * @property planetaryInfluence How the ruling planet colors the bird's expression
 */
data class PakshiInterpretation(
    val currentActivityMeaning: String,
    val currentSubActivityMeaning: String,
    val overallGuidance: String,
    val bestTimesForAction: List<String>,
    val warningPeriods: List<String>,
    val birdPersonality: String,
    val planetaryInfluence: String
)

/**
 * Complete Panchapakshi analysis result combining birth bird, current state,
 * daily timeline, and interpretive guidance.
 *
 * @property birthBird The native's birth bird determined from birth Nakshatra and Paksha
 * @property birthPaksha The lunar Paksha at the time of birth
 * @property currentActivity Current primary activity of the birth bird
 * @property currentSubActivity Current sub-activity of the birth bird
 * @property vibrationalStrength Current composite vibrational strength (0-100)
 * @property allBirdStates States of all five birds at the query moment
 * @property daySegments The five daytime segments
 * @property nightSegments The five nighttime segments
 * @property dailyTimeline Complete daily timeline with sub-segments
 * @property interpretation Interpretive guidance for the native
 */
data class PanchapakshiAnalysis(
    val birthBird: PakshiBird,
    val birthPaksha: Paksha,
    val currentActivity: PakshiActivity,
    val currentSubActivity: PakshiActivity,
    val vibrationalStrength: Int,
    val allBirdStates: Map<PakshiBird, BirdActivityState>,
    val daySegments: List<PakshiTimeSegment>,
    val nightSegments: List<PakshiTimeSegment>,
    val dailyTimeline: List<PakshiTimelineEntry>,
    val interpretation: PakshiInterpretation
)

// =====================================================================================
// PANCHAPAKSHI CALCULATOR
// =====================================================================================

/**
 * Panchapakshi (Five Birds) Timing System Calculator.
 *
 * Panchapakshi is an ancient Tamil Siddha timing system that maps bio-rhythmic
 * energy cycles to five archetypal birds. Each bird sequentially cycles through
 * five activities (Ruling, Eating, Walking, Sleeping, Dying) throughout the day
 * and night, creating a precise 25-segment timing grid for each half.
 *
 * **Core Principles:**
 * 1. The birth bird is determined from the Moon's Nakshatra at birth and the
 *    lunar Paksha (Shukla/Krishna).
 * 2. The day (sunrise to sunset) and night (sunset to next sunrise) are each
 *    divided into 5 equal segments.
 * 3. The activity rotation for the birth bird depends on the weekday (Vara).
 * 4. Each main segment is further divided into 5 sub-segments following the
 *    same rotation pattern.
 * 5. The opposing birds follow a complementary activity cycle: when one bird
 *    rules, the bird 3 positions away is dying.
 *
 * **Classical Reference:** Pancha Pakshi Shastra, attributed to Tamil Siddha
 * tradition (Agastya lineage).
 *
 * @author AstroVajra
 */
object PanchapakshiCalculator {

    // ============================================================================
    // BIRTH BIRD DETERMINATION
    // ============================================================================

    /**
     * Shukla Paksha birth bird mapping by Nakshatra number (1-27).
     *
     * Groups of 5 Nakshatras map to each bird in order:
     * 1-5: Vulture, 6-10: Owl, 11-15: Crow, 16-20: Cock, 21-25: Peacock,
     * 26-27: Vulture (cycle restart).
     */
    private val SHUKLA_BIRD_MAP: Map<IntRange, PakshiBird> = mapOf(
        (1..5) to PakshiBird.VULTURE,
        (6..10) to PakshiBird.OWL,
        (11..15) to PakshiBird.CROW,
        (16..20) to PakshiBird.COCK,
        (21..25) to PakshiBird.PEACOCK,
        (26..27) to PakshiBird.VULTURE
    )

    /**
     * Krishna Paksha reverses the bird order.
     * 1-5: Peacock, 6-10: Cock, 11-15: Crow, 16-20: Owl, 21-25: Vulture,
     * 26-27: Peacock (cycle restart).
     */
    private val KRISHNA_BIRD_MAP: Map<IntRange, PakshiBird> = mapOf(
        (1..5) to PakshiBird.PEACOCK,
        (6..10) to PakshiBird.COCK,
        (11..15) to PakshiBird.CROW,
        (16..20) to PakshiBird.OWL,
        (21..25) to PakshiBird.VULTURE,
        (26..27) to PakshiBird.PEACOCK
    )

    /**
     * Day activity sequence matrix: the starting activity offset for the birth bird
     * by day of week (0 = Sunday through 6 = Saturday).
     *
     * Sunday/Friday: Ruling, Eating, Walking, Sleeping, Dying (offset 0)
     * Monday/Saturday: Eating, Walking, Sleeping, Dying, Ruling (offset 1)
     * Tuesday: Walking, Sleeping, Dying, Ruling, Eating (offset 2)
     * Wednesday: Sleeping, Dying, Ruling, Eating, Walking (offset 3)
     * Thursday: Dying, Ruling, Eating, Walking, Sleeping (offset 4)
     */
    private val DAY_ACTIVITY_OFFSET = mapOf(
        DayOfWeek.SUNDAY to 0,
        DayOfWeek.MONDAY to 1,
        DayOfWeek.TUESDAY to 2,
        DayOfWeek.WEDNESDAY to 3,
        DayOfWeek.THURSDAY to 4,
        DayOfWeek.FRIDAY to 0,
        DayOfWeek.SATURDAY to 1
    )

    // ============================================================================
    // PUBLIC API
    // ============================================================================

    /**
     * Performs a complete Panchapakshi analysis for the given birth chart at the
     * specified query time.
     *
     * This is the primary entry point for the Panchapakshi system. It determines
     * the native's birth bird, computes the full daily timeline of bird activities
     * for the query date, identifies the current activity state of all five birds,
     * and provides interpretive guidance.
     *
     * @param chart The native's Vedic birth chart (used to determine birth Nakshatra and Paksha)
     * @param queryDateTime The date/time for which to calculate current activity states
     * @param latitude Geographic latitude for astronomical sunrise/sunset calculation
     * @param longitude Geographic longitude for astronomical sunrise/sunset calculation
     * @return Complete [PanchapakshiAnalysis] with birth bird, timeline, and interpretation
     */
    fun analyze(
        chart: VedicChart,
        queryDateTime: LocalDateTime,
        latitude: Double,
        longitude: Double
    ): PanchapakshiAnalysis {
        // 1. Determine birth bird from birth Nakshatra and Paksha
        val moonPosition = chart.planetPositions.find { it.planet == Planet.MOON }
            ?: throw IllegalArgumentException("Moon position required for Panchapakshi analysis")
        val sunPosition = chart.planetPositions.find { it.planet == Planet.SUN }
            ?: throw IllegalArgumentException("Sun position required for Panchapakshi analysis")

        val birthPaksha = determinePaksha(sunPosition.longitude, moonPosition.longitude)
        val birthBird = determineBirthBird(moonPosition.nakshatra, birthPaksha)

        // 2. Calculate sunrise and sunset for the query date
        val queryDate = queryDateTime.toLocalDate()
        val sunrise = calculateSunriseTime(queryDate.atStartOfDay(), latitude, longitude)
        val sunset = calculateSunsetTime(queryDate.atStartOfDay(), latitude, longitude)

        // Next day sunrise for night segment calculation
        val nextSunrise = calculateSunriseTime(queryDate.plusDays(1).atStartOfDay(), latitude, longitude)

        // 3. Calculate day and night segment durations
        val dayDuration = Duration.between(sunrise, sunset)
        val nightDuration = Duration.between(sunset, nextSunrise)
        val daySegmentDuration = dayDuration.dividedBy(5)
        val nightSegmentDuration = nightDuration.dividedBy(5)

        // 4. Determine weekday and activity offset
        val dayOfWeek = queryDateTime.toLocalDate().dayOfWeek
        val dayOffset = DAY_ACTIVITY_OFFSET[dayOfWeek] ?: 0

        // 5. Build day segments (5 main segments with 5 sub-segments each)
        val daySegments = buildSegments(
            startTime = sunrise,
            segmentDuration = daySegmentDuration,
            activityOffset = dayOffset,
            isDaytime = true,
            birthBird = birthBird
        )

        // 6. Build night segments (reversed activity order)
        val nightOffset = (dayOffset + 4) % 5 // Night reversal: 5th segment of day becomes 1st of night
        val nightSegments = buildSegments(
            startTime = sunset,
            segmentDuration = nightSegmentDuration,
            activityOffset = nightOffset,
            isDaytime = false,
            birthBird = birthBird
        )

        // 7. Build the complete daily timeline
        val dailyTimeline = buildDailyTimeline(daySegments, nightSegments)

        // 8. Determine current activity state
        val queryTime = queryDateTime.toLocalTime()
        val (currentActivity, currentSubActivity) = findCurrentActivities(
            queryTime, daySegments, nightSegments, sunrise, sunset
        )

        // 9. Calculate vibrational strength
        val vibrationalStrength = calculateVibrationalStrength(currentActivity, currentSubActivity)

        // 10. Calculate states for all five birds at the current moment
        val allBirdStates = calculateAllBirdStates(
            birthBird, currentActivity, currentSubActivity
        )

        // 11. Generate interpretation
        val interpretation = generateInterpretation(
            birthBird, currentActivity, currentSubActivity,
            vibrationalStrength, daySegments, nightSegments, queryTime
        )

        return PanchapakshiAnalysis(
            birthBird = birthBird,
            birthPaksha = birthPaksha,
            currentActivity = currentActivity,
            currentSubActivity = currentSubActivity,
            vibrationalStrength = vibrationalStrength,
            allBirdStates = allBirdStates,
            daySegments = daySegments,
            nightSegments = nightSegments,
            dailyTimeline = dailyTimeline,
            interpretation = interpretation
        )
    }

    /**
     * Determines the birth bird from the Moon's Nakshatra and the Paksha at birth.
     *
     * @param nakshatra The Moon's Nakshatra at birth
     * @param paksha The lunar Paksha (Shukla or Krishna) at birth
     * @return The native's Panchapakshi birth bird
     */
    fun determineBirthBird(nakshatra: Nakshatra, paksha: Paksha): PakshiBird {
        val nakshatraNumber = nakshatra.number
        val birdMap = when (paksha) {
            Paksha.SHUKLA -> SHUKLA_BIRD_MAP
            Paksha.KRISHNA -> KRISHNA_BIRD_MAP
        }
        return birdMap.entries.first { nakshatraNumber in it.key }.value
    }

    /**
     * Determines the Paksha (lunar fortnight) from Sun and Moon longitudes.
     *
     * Shukla Paksha: Moon is 0-180 degrees ahead of Sun (waxing)
     * Krishna Paksha: Moon is 180-360 degrees ahead of Sun (waning)
     *
     * @param sunLongitude Sun's sidereal longitude
     * @param moonLongitude Moon's sidereal longitude
     * @return The Paksha at the given moment
     */
    fun determinePaksha(sunLongitude: Double, moonLongitude: Double): Paksha {
        val elongation = VedicAstrologyUtils.normalizeLongitude(moonLongitude - sunLongitude)
        return if (elongation < 180.0) Paksha.SHUKLA else Paksha.KRISHNA
    }

    // ============================================================================
    // SEGMENT BUILDING
    // ============================================================================

    /**
     * Builds the five main segments for a day or night half.
     *
     * Each segment gets an activity based on the rotation offset, and is further
     * divided into five sub-segments following the same rotation pattern.
     *
     * @param startTime The start time of this half (sunrise or sunset)
     * @param segmentDuration Duration of each main segment (1/5 of day or night)
     * @param activityOffset The starting activity rotation offset for this weekday
     * @param isDaytime Whether these are daytime segments
     * @param birthBird The native's birth bird (for reference)
     * @return List of five [PakshiTimeSegment] entries
     */
    private fun buildSegments(
        startTime: LocalTime,
        segmentDuration: Duration,
        activityOffset: Int,
        isDaytime: Boolean,
        birthBird: PakshiBird
    ): List<PakshiTimeSegment> {
        val segments = mutableListOf<PakshiTimeSegment>()
        val subSegmentDuration = segmentDuration.dividedBy(5)

        for (i in 0 until 5) {
            val segStart = startTime.plus(segmentDuration.multipliedBy(i.toLong()))
            val segEnd = startTime.plus(segmentDuration.multipliedBy((i + 1).toLong()))
            val mainActivity = PakshiActivity.atCyclicIndex(activityOffset + i)

            // Determine the bird for this segment: The birth bird's activity determines
            // which bird occupies which segment. The 5 birds cycle through simultaneously
            // with the birth bird's activity determining the segment assignment.
            val segmentBird = PakshiBird.atCyclicIndex(birthBird.ordinal + i)

            // Build sub-segments: each sub-segment follows the same rotation pattern
            val subSegments = mutableListOf<PakshiSubSegment>()
            for (j in 0 until 5) {
                val subStart = segStart.plus(subSegmentDuration.multipliedBy(j.toLong()))
                val subEnd = segStart.plus(subSegmentDuration.multipliedBy((j + 1).toLong()))
                val subActivity = PakshiActivity.atCyclicIndex(activityOffset + i + j)

                subSegments.add(
                    PakshiSubSegment(
                        subSegmentNumber = j + 1,
                        startTime = subStart,
                        endTime = subEnd,
                        subActivity = subActivity
                    )
                )
            }

            segments.add(
                PakshiTimeSegment(
                    segmentNumber = i + 1,
                    startTime = segStart,
                    endTime = segEnd,
                    bird = segmentBird,
                    activity = mainActivity,
                    subSegments = subSegments,
                    isDaytime = isDaytime
                )
            )
        }

        return segments
    }

    /**
     * Builds the complete daily timeline from day and night segments,
     * expanding sub-segments into individual timeline entries.
     */
    private fun buildDailyTimeline(
        daySegments: List<PakshiTimeSegment>,
        nightSegments: List<PakshiTimeSegment>
    ): List<PakshiTimelineEntry> {
        val timeline = mutableListOf<PakshiTimelineEntry>()

        for (segment in daySegments + nightSegments) {
            for (sub in segment.subSegments) {
                val strength = calculateVibrationalStrength(segment.activity, sub.subActivity)
                timeline.add(
                    PakshiTimelineEntry(
                        startTime = sub.startTime,
                        endTime = sub.endTime,
                        bird = segment.bird,
                        activity = segment.activity,
                        subActivity = sub.subActivity,
                        strength = strength,
                        isDaytime = segment.isDaytime,
                        segmentNumber = segment.segmentNumber,
                        subSegmentNumber = sub.subSegmentNumber
                    )
                )
            }
        }

        return timeline
    }

    // ============================================================================
    // CURRENT STATE DETERMINATION
    // ============================================================================

    /**
     * Finds the current activity and sub-activity for the query time
     * by scanning through day and night segments.
     *
     * @return Pair of (mainActivity, subActivity). Defaults to SLEEPING if time
     *         falls outside calculated segments (edge case).
     */
    private fun findCurrentActivities(
        queryTime: LocalTime,
        daySegments: List<PakshiTimeSegment>,
        nightSegments: List<PakshiTimeSegment>,
        sunrise: LocalTime,
        sunset: LocalTime
    ): Pair<PakshiActivity, PakshiActivity> {
        // Check day segments
        if (!queryTime.isBefore(sunrise) && queryTime.isBefore(sunset)) {
            for (segment in daySegments) {
                if (isTimeInRange(queryTime, segment.startTime, segment.endTime)) {
                    for (sub in segment.subSegments) {
                        if (isTimeInRange(queryTime, sub.startTime, sub.endTime)) {
                            return Pair(segment.activity, sub.subActivity)
                        }
                    }
                    // Fallback to last sub-segment of this main segment
                    return Pair(segment.activity, segment.subSegments.last().subActivity)
                }
            }
        }

        // Check night segments
        for (segment in nightSegments) {
            if (isTimeInNightRange(queryTime, segment.startTime, segment.endTime, sunset)) {
                for (sub in segment.subSegments) {
                    if (isTimeInNightRange(queryTime, sub.startTime, sub.endTime, sunset)) {
                        return Pair(segment.activity, sub.subActivity)
                    }
                }
                return Pair(segment.activity, segment.subSegments.last().subActivity)
            }
        }

        // Before sunrise: check if any night segment wraps past midnight
        for (segment in nightSegments) {
            // Night segments that start before midnight and end after
            if (segment.endTime.isBefore(segment.startTime)) {
                if (queryTime.isBefore(segment.endTime)) {
                    for (sub in segment.subSegments) {
                        if (sub.endTime.isBefore(sub.startTime) && queryTime.isBefore(sub.endTime)) {
                            return Pair(segment.activity, sub.subActivity)
                        }
                        if (!sub.endTime.isBefore(sub.startTime) && isTimeInRange(queryTime, sub.startTime, sub.endTime)) {
                            return Pair(segment.activity, sub.subActivity)
                        }
                    }
                    return Pair(segment.activity, segment.subSegments.last().subActivity)
                }
            }
        }

        // Default fallback
        return Pair(PakshiActivity.SLEEPING, PakshiActivity.SLEEPING)
    }

    /**
     * Checks whether a time falls within a range, handling the simple non-wrapping case.
     */
    private fun isTimeInRange(time: LocalTime, start: LocalTime, end: LocalTime): Boolean {
        return !time.isBefore(start) && time.isBefore(end)
    }

    /**
     * Checks whether a time falls within a night range that may wrap past midnight.
     */
    private fun isTimeInNightRange(
        time: LocalTime,
        start: LocalTime,
        end: LocalTime,
        sunset: LocalTime
    ): Boolean {
        // If the range wraps past midnight
        if (end.isBefore(start) || end == start) {
            return !time.isBefore(start) || time.isBefore(end)
        }
        return !time.isBefore(start) && time.isBefore(end)
    }

    // ============================================================================
    // VIBRATIONAL STRENGTH CALCULATION
    // ============================================================================

    /**
     * Calculates the composite vibrational strength from the main activity
     * and sub-activity.
     *
     * The composite score is a weighted combination:
     * - Main activity contributes 70% of its base strength
     * - Sub-activity contributes 30% of its base strength
     *
     * This reflects the Siddha principle that the primary period dominates
     * but the sub-period modulates the energy.
     *
     * @param mainActivity The primary activity of the current segment
     * @param subActivity The sub-activity of the current sub-segment
     * @return Composite vibrational strength (0-100)
     */
    fun calculateVibrationalStrength(
        mainActivity: PakshiActivity,
        subActivity: PakshiActivity
    ): Int {
        val mainContribution = mainActivity.strength * 0.70
        val subContribution = subActivity.strength * 0.30
        return (mainContribution + subContribution).roundToInt().coerceIn(0, 100)
    }

    // ============================================================================
    // ALL BIRD STATES
    // ============================================================================

    /**
     * Calculates the activity state for all five birds at the current moment.
     *
     * The Panchapakshi system assigns simultaneous activities to all five birds:
     * when the birth bird is at activity index N, the bird at offset +1 is at
     * activity index N+1, etc. This creates a complementary energy pattern
     * where exactly one bird is always Ruling and one is always Dying.
     *
     * @param birthBird The native's birth bird
     * @param currentMainActivity The birth bird's current main activity
     * @param currentSubActivity The birth bird's current sub-activity
     * @return Map of all five birds to their current activity states
     */
    private fun calculateAllBirdStates(
        birthBird: PakshiBird,
        currentMainActivity: PakshiActivity,
        currentSubActivity: PakshiActivity
    ): Map<PakshiBird, BirdActivityState> {
        val states = mutableMapOf<PakshiBird, BirdActivityState>()
        val birthActivityIndex = currentMainActivity.ordinal
        val birthSubActivityIndex = currentSubActivity.ordinal

        for (bird in PakshiBird.entries) {
            val offset = ((bird.ordinal - birthBird.ordinal) + 5) % 5
            val activity = PakshiActivity.atCyclicIndex(birthActivityIndex + offset)
            val subActivity = PakshiActivity.atCyclicIndex(birthSubActivityIndex + offset)
            val strength = calculateVibrationalStrength(activity, subActivity)

            states[bird] = BirdActivityState(
                bird = bird,
                activity = activity,
                subActivity = subActivity,
                strength = strength,
                isRuling = activity == PakshiActivity.RULING
            )
        }

        return states
    }

    // ============================================================================
    // ASTRONOMICAL CALCULATIONS (Sunrise/Sunset)
    // ============================================================================

    /**
     * Calculates approximate sunrise time for a given date and geographic location.
     *
     * Uses the standard astronomical sunrise formula based on solar declination
     * and the observer's latitude. This provides a reasonable approximation
     * without requiring Swiss Ephemeris access.
     *
     * The calculation accounts for:
     * - Solar declination variation throughout the year
     * - Observer latitude effect on day length
     * - Standard atmospheric refraction correction (0.833 degrees)
     *
     * @param dateTime Date for which to calculate sunrise
     * @param latitude Geographic latitude in degrees (positive = North)
     * @param longitude Geographic longitude in degrees (positive = East)
     * @return Approximate local sunrise time
     */
    private fun calculateSunriseTime(
        dateTime: LocalDateTime,
        latitude: Double,
        longitude: Double
    ): LocalTime {
        val dayOfYear = dateTime.dayOfYear
        val latRad = Math.toRadians(latitude)

        // Solar declination approximation
        val declination = 23.44 * sin(Math.toRadians(360.0 / 365.0 * (dayOfYear - 81)))
        val decRad = Math.toRadians(declination)

        // Hour angle for sunrise (accounting for refraction of 0.833 degrees)
        val cosHourAngle = (cos(Math.toRadians(90.833)) - sin(latRad) * sin(decRad)) /
                (cos(latRad) * cos(decRad))

        // Clamp for polar regions
        val hourAngle = if (cosHourAngle > 1.0) 0.0
        else if (cosHourAngle < -1.0) 180.0
        else Math.toDegrees(acos(cosHourAngle))

        // Solar noon approximation (in hours from midnight UTC)
        val equationOfTime = calculateEquationOfTime(dayOfYear)
        val solarNoonMinutes = 720.0 - 4.0 * longitude - equationOfTime

        // Sunrise in minutes from midnight (local solar time)
        val sunriseMinutes = solarNoonMinutes - hourAngle * 4.0

        // Convert to LocalTime
        val totalMinutes = sunriseMinutes.toLong().coerceIn(0, 1439)
        return LocalTime.of(
            (totalMinutes / 60).toInt().coerceIn(0, 23),
            (totalMinutes % 60).toInt().coerceIn(0, 59)
        )
    }

    /**
     * Calculates approximate sunset time for a given date and geographic location.
     *
     * @param dateTime Date for which to calculate sunset
     * @param latitude Geographic latitude in degrees
     * @param longitude Geographic longitude in degrees
     * @return Approximate local sunset time
     */
    private fun calculateSunsetTime(
        dateTime: LocalDateTime,
        latitude: Double,
        longitude: Double
    ): LocalTime {
        val dayOfYear = dateTime.dayOfYear
        val latRad = Math.toRadians(latitude)

        val declination = 23.44 * sin(Math.toRadians(360.0 / 365.0 * (dayOfYear - 81)))
        val decRad = Math.toRadians(declination)

        val cosHourAngle = (cos(Math.toRadians(90.833)) - sin(latRad) * sin(decRad)) /
                (cos(latRad) * cos(decRad))

        val hourAngle = if (cosHourAngle > 1.0) 0.0
        else if (cosHourAngle < -1.0) 180.0
        else Math.toDegrees(acos(cosHourAngle))

        val equationOfTime = calculateEquationOfTime(dayOfYear)
        val solarNoonMinutes = 720.0 - 4.0 * longitude - equationOfTime

        val sunsetMinutes = solarNoonMinutes + hourAngle * 4.0

        val totalMinutes = sunsetMinutes.toLong().coerceIn(0, 1439)
        return LocalTime.of(
            (totalMinutes / 60).toInt().coerceIn(0, 23),
            (totalMinutes % 60).toInt().coerceIn(0, 59)
        )
    }

    /**
     * Calculates the Equation of Time correction for solar time.
     *
     * The Equation of Time accounts for the eccentricity of Earth's orbit
     * and the tilt of its axis, producing a time correction of up to +/- 16 minutes.
     *
     * @param dayOfYear Day number within the year (1-366)
     * @return Equation of Time in minutes
     */
    private fun calculateEquationOfTime(dayOfYear: Int): Double {
        val b = 2.0 * PI * (dayOfYear - 81) / 364.0
        return 9.87 * sin(2.0 * b) - 7.53 * cos(b) - 1.5 * sin(b)
    }

    // ============================================================================
    // INTERPRETATION GENERATION
    // ============================================================================

    /**
     * Generates comprehensive interpretive guidance based on the current
     * Panchapakshi state.
     *
     * @param birthBird The native's birth bird
     * @param currentActivity Current main activity
     * @param currentSubActivity Current sub-activity
     * @param strength Current vibrational strength
     * @param daySegments Day segments for finding best/worst upcoming times
     * @param nightSegments Night segments for finding best/worst upcoming times
     * @param queryTime The current query time
     * @return Complete [PakshiInterpretation] with meanings and guidance
     */
    private fun generateInterpretation(
        birthBird: PakshiBird,
        currentActivity: PakshiActivity,
        currentSubActivity: PakshiActivity,
        strength: Int,
        daySegments: List<PakshiTimeSegment>,
        nightSegments: List<PakshiTimeSegment>,
        queryTime: LocalTime
    ): PakshiInterpretation {
        val activityMeaning = getActivityMeaning(currentActivity)
        val subActivityMeaning = getSubActivityMeaning(currentSubActivity, currentActivity)

        val overallGuidance = when {
            strength >= 80 -> "Excellent period for important actions. Your birth bird (${birthBird.english}) " +
                    "is in a highly active state. Initiate significant ventures, meetings, and decisions now."
            strength >= 60 -> "Good period for moderate activity. Your energy level supports constructive " +
                    "work and forward movement. Favorable for routine tasks and progressive actions."
            strength >= 40 -> "Moderate period. Exercise normal caution with important decisions. " +
                    "Suitable for planning and preparation rather than major launches."
            strength >= 20 -> "Low energy period. Best used for rest, contemplation, and internal work. " +
                    "Avoid initiating critical negotiations or signing important documents."
            else -> "Unfavorable period. Your birth bird is in a depleted state. Postpone important " +
                    "actions if possible. Focus on devotion, mantra practice, and recuperation."
        }

        val bestTimes = findBestUpcomingTimes(daySegments, nightSegments, queryTime)
        val warningPeriods = findWarningPeriods(daySegments, nightSegments, queryTime)
        val birdPersonality = getBirdPersonality(birthBird)
        val planetaryInfluence = getPlanetaryInfluence(birthBird)

        return PakshiInterpretation(
            currentActivityMeaning = activityMeaning,
            currentSubActivityMeaning = subActivityMeaning,
            overallGuidance = overallGuidance,
            bestTimesForAction = bestTimes,
            warningPeriods = warningPeriods,
            birdPersonality = birdPersonality,
            planetaryInfluence = planetaryInfluence
        )
    }

    /**
     * Returns the classical meaning of a primary activity.
     */
    private fun getActivityMeaning(activity: PakshiActivity): String = when (activity) {
        PakshiActivity.RULING -> "Aatchi (Ruling): Your birth bird is at peak power. This is the most " +
                "auspicious phase for commanding authority, initiating ventures, taking bold decisions, " +
                "and exercising leadership. Actions started now carry maximum force and likelihood of success."
        PakshiActivity.EATING -> "Unavu (Eating): Your birth bird is nourishing itself. This is favorable " +
                "for accumulation - gathering resources, learning, absorbing information, making purchases, " +
                "and building relationships. A naturally productive and growth-oriented phase."
        PakshiActivity.WALKING -> "Nadai (Walking): Your birth bird is in motion. This is suitable for " +
                "travel, communication, networking, errands, and moderate activity. Movement and transition " +
                "are favored but major commitments should be weighed carefully."
        PakshiActivity.SLEEPING -> "Urakkam (Sleeping): Your birth bird is in a passive, dormant state. " +
                "Not favorable for initiating new activities. Best for rest, meditation, contemplation, " +
                "inner work, and allowing existing processes to unfold naturally."
        PakshiActivity.DYING -> "Maranam (Dying): Your birth bird is at its lowest ebb. Avoid all " +
                "important actions, negotiations, and new ventures. This is a period of dissolution and " +
                "release. Engage in spiritual practices, charity, and surrender."
    }

    /**
     * Returns the meaning of a sub-activity within its primary context.
     */
    private fun getSubActivityMeaning(
        subActivity: PakshiActivity,
        mainActivity: PakshiActivity
    ): String {
        val subContext = when (subActivity) {
            PakshiActivity.RULING -> "sub-period of peak micro-energy amplifying the main phase"
            PakshiActivity.EATING -> "sub-period of absorption and intake within the main phase"
            PakshiActivity.WALKING -> "sub-period of movement and transition within the main phase"
            PakshiActivity.SLEEPING -> "sub-period of passivity dampening the main phase"
            PakshiActivity.DYING -> "sub-period of depletion weakening the main phase"
        }
        return "Within the ${mainActivity.english} main period, this is a $subContext. " +
                "The combined effect ${if (subActivity.strength >= mainActivity.strength)
                    "reinforces" else "moderates"} the primary energy."
    }

    /**
     * Finds the best upcoming time segments (Ruling periods) after the query time.
     */
    private fun findBestUpcomingTimes(
        daySegments: List<PakshiTimeSegment>,
        nightSegments: List<PakshiTimeSegment>,
        queryTime: LocalTime
    ): List<String> {
        val bestTimes = mutableListOf<String>()

        for (segment in daySegments + nightSegments) {
            if (segment.activity == PakshiActivity.RULING) {
                val label = if (segment.isDaytime) "Day" else "Night"
                bestTimes.add(
                    "$label Segment ${segment.segmentNumber}: ${formatTime(segment.startTime)} - " +
                            "${formatTime(segment.endTime)} (${segment.bird.english} Ruling)"
                )
                // Also find Ruling sub-segments within non-Ruling main segments
            }
            for (sub in segment.subSegments) {
                if (sub.subActivity == PakshiActivity.RULING && segment.activity != PakshiActivity.RULING) {
                    val label = if (segment.isDaytime) "Day" else "Night"
                    bestTimes.add(
                        "$label Seg ${segment.segmentNumber}.${sub.subSegmentNumber}: " +
                                "${formatTime(sub.startTime)} - ${formatTime(sub.endTime)} " +
                                "(Ruling sub-period within ${segment.activity.english})"
                    )
                }
            }
        }

        return bestTimes.take(6)
    }

    /**
     * Finds upcoming warning periods (Dying phases) after the query time.
     */
    private fun findWarningPeriods(
        daySegments: List<PakshiTimeSegment>,
        nightSegments: List<PakshiTimeSegment>,
        queryTime: LocalTime
    ): List<String> {
        val warnings = mutableListOf<String>()

        for (segment in daySegments + nightSegments) {
            if (segment.activity == PakshiActivity.DYING) {
                val label = if (segment.isDaytime) "Day" else "Night"
                warnings.add(
                    "$label Segment ${segment.segmentNumber}: ${formatTime(segment.startTime)} - " +
                            "${formatTime(segment.endTime)} (${segment.bird.english} Dying - Avoid important actions)"
                )
            }
        }

        return warnings.take(4)
    }

    /**
     * Returns the personality traits associated with each birth bird.
     */
    private fun getBirdPersonality(bird: PakshiBird): String = when (bird) {
        PakshiBird.VULTURE -> "The Vulture (Kazhugu) native possesses sharp perception, patience, and the ability " +
                "to see through illusions. Ruled by Saturn, Vulture people are strategic, enduring, and " +
                "excel in situations requiring sustained effort and detachment. They rise above challenges " +
                "with remarkable resilience and have an instinct for timing their actions perfectly. " +
                "Natural affinity for research, investigation, and deep analysis."
        PakshiBird.OWL -> "The Owl (Andai) native possesses keen intuition, courage, and mastery of the unseen. " +
                "Ruled by Mars, Owl people are decisive, action-oriented, and unafraid of the unknown. " +
                "They operate effectively in challenging conditions where others falter. Their warrior " +
                "spirit combined with nocturnal wisdom gives them unique strategic advantages. " +
                "Natural affinity for defense, medicine, occult sciences, and crisis management."
        PakshiBird.CROW -> "The Crow (Kaakam) native possesses intelligence, adaptability, and resourcefulness. " +
                "Ruled by the Sun, Crow people have natural authority, social intelligence, and the ability " +
                "to thrive in any environment. They are excellent communicators with strong community bonds. " +
                "Their solar rulership gives them leadership potential and the capacity to illuminate " +
                "hidden truths. Natural affinity for politics, administration, and public life."
        PakshiBird.COCK -> "The Cock (Kozhi) native possesses discipline, punctuality, and announcing power. " +
                "Ruled by Jupiter, Cock people are dharmic, morally upright, and serve as awakeners for " +
                "those around them. They have excellent teaching abilities and a natural sense of justice. " +
                "Their Jupiterian wisdom combined with alertness makes them guardians of truth and tradition. " +
                "Natural affinity for education, law, spirituality, and counseling."
        PakshiBird.PEACOCK -> "The Peacock (Mayil) native possesses beauty, creativity, and transformative power. " +
                "Ruled by Venus, Peacock people are artistic, charming, and possess the rare ability to " +
                "transmute negativity into beauty. Associated with Lord Murugan in Tamil tradition, the " +
                "Peacock represents victory over ego (symbolized by eating serpents). Their Venusian grace " +
                "combined with warrior spirit creates magnetic personalities. Natural affinity for arts, " +
                "healing, aesthetics, and spiritual transformation."
    }

    /**
     * Returns the planetary influence description for the birth bird's ruler.
     */
    private fun getPlanetaryInfluence(bird: PakshiBird): String {
        val planet = bird.ruler
        return when (planet) {
            Planet.SATURN -> "Saturn's influence on the Vulture grants patience, discipline, karmic awareness, " +
                    "and mastery over time. During Saturn-ruled periods (Saturday, Saturn hora), the Vulture " +
                    "native experiences heightened power. Favorable directions: West. Favorable colors: " +
                    "Black, dark blue. Gems: Blue Sapphire, Amethyst."
            Planet.MARS -> "Mars's influence on the Owl grants courage, decisive action, and warrior spirit. " +
                    "During Mars-ruled periods (Tuesday, Mars hora), the Owl native experiences heightened " +
                    "power. Favorable directions: South. Favorable colors: Red, crimson. " +
                    "Gems: Red Coral, Garnet."
            Planet.SUN -> "Sun's influence on the Crow grants authority, leadership, and illuminating wisdom. " +
                    "During Sun-ruled periods (Sunday, Sun hora), the Crow native experiences heightened " +
                    "power. Favorable directions: East. Favorable colors: Gold, orange. " +
                    "Gems: Ruby, Sunstone."
            Planet.JUPITER -> "Jupiter's influence on the Cock grants wisdom, dharmic sense, and teaching ability. " +
                    "During Jupiter-ruled periods (Thursday, Jupiter hora), the Cock native experiences " +
                    "heightened power. Favorable directions: North-East. Favorable colors: Yellow, gold. " +
                    "Gems: Yellow Sapphire, Topaz."
            Planet.VENUS -> "Venus's influence on the Peacock grants creativity, aesthetic sense, and magnetic charm. " +
                    "During Venus-ruled periods (Friday, Venus hora), the Peacock native experiences " +
                    "heightened power. Favorable directions: South-East. Favorable colors: White, " +
                    "multi-colored. Gems: Diamond, White Sapphire."
            else -> "The ${planet.displayName} influence shapes the ${bird.english}'s characteristic expression."
        }
    }

    /**
     * Formats a [LocalTime] to a human-readable HH:MM string.
     */
    private fun formatTime(time: LocalTime): String {
        return String.format("%02d:%02d", time.hour, time.minute)
    }
}
