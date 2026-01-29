package com.astro.storm.ephemeris.muhurta

import com.astro.storm.core.common.Language
import com.astro.storm.core.model.Nakshatra
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.temporal.ChronoUnit

/**
 * Muhurta Optimization Engine
 *
 * Advanced engine that searches through a date range to find optimal
 * muhurta windows for specific activities. Uses multi-criteria optimization
 * considering:
 *
 * 1. Panchanga Elements: Tithi, Nakshatra, Yoga, Karana, Vara
 * 2. Time Segments: Hora, Choghadiya, Abhijit Muhurta
 * 3. Inauspicious Periods: Rahu Kala, Yamaghanta, Gulika Kala
 * 4. Special Yogas: Amrit Siddhi, Sarvarthasiddhi, Dwipushkar, etc.
 * 5. Activity-Specific Requirements
 *
 * Search Strategy:
 * - Iterates through each day in the date range
 * - For each day, scans in configurable intervals (default 5 minutes)
 * - Scores each time slot and collects qualifying windows
 * - Returns ranked list of optimal muhurta windows
 *
 * @author AstroStorm - Ultra-Precision Vedic Astrology
 */
object MuhurtaOptimizationEngine {

    /**
     * Default search interval in minutes
     */
    private const val DEFAULT_INTERVAL_MINUTES = 5L

    /**
     * Minimum score threshold for a window to be considered
     */
    private const val MIN_SCORE_THRESHOLD = 60

    /**
     * Excellent score threshold
     */
    private const val EXCELLENT_SCORE_THRESHOLD = 80

    /**
     * Search parameters for muhurta optimization
     */
    data class SearchParams(
        val startDate: LocalDate,
        val endDate: LocalDate,
        val activityType: ActivityType,
        val preferredTimeRange: TimeRange? = null,
        val minScore: Int = MIN_SCORE_THRESHOLD,
        val intervalMinutes: Long = DEFAULT_INTERVAL_MINUTES,
        val maxResults: Int = 20,
        val requireDaylight: Boolean = true,
        val avoidInauspiciousPeriods: Boolean = true,
        val preferAbhijitMuhurta: Boolean = true,
        val latitude: Double,
        val longitude: Double,
        val timezone: String
    )

    /**
     * Time range preference
     */
    data class TimeRange(
        val startTime: LocalTime,
        val endTime: LocalTime
    ) {
        fun contains(time: LocalTime): Boolean {
            return if (startTime <= endTime) {
                time >= startTime && time <= endTime
            } else {
                time >= startTime || time <= endTime
            }
        }
    }

    /**
     * Optimized muhurta window result
     */
    data class OptimizedMuhurtaWindow(
        val startDateTime: LocalDateTime,
        val endDateTime: LocalDateTime,
        val score: Int,
        val rank: Int,
        val vara: Vara,
        val tithi: TithiInfo,
        val nakshatra: NakshatraInfo,
        val yoga: YogaInfo,
        val karana: KaranaInfo,
        val choghadiya: Choghadiya,
        val hora: Hora,
        val isAbhijitMuhurta: Boolean,
        val specialYogas: List<SpecialYoga>,
        val positiveFactors: List<String>,
        val negativeFactors: List<String>,
        val recommendation: String
    ) {
        val durationMinutes: Long get() = ChronoUnit.MINUTES.between(startDateTime, endDateTime)
        val isExcellent: Boolean get() = score >= EXCELLENT_SCORE_THRESHOLD
    }

    /**
     * Search result containing all optimized windows
     */
    data class OptimizationResult(
        val activityType: ActivityType,
        val searchParams: SearchParams,
        val totalWindowsFound: Int,
        val excellentWindows: Int,
        val windows: List<OptimizedMuhurtaWindow>,
        val bestWindow: OptimizedMuhurtaWindow?,
        val searchDurationMs: Long,
        val language: Language = Language.ENGLISH
    ) {
        fun toSummaryText(): String = buildString {
            appendLine("MUHURTA OPTIMIZATION RESULTS")
            appendLine("═".repeat(50))
            appendLine("Activity: ${activityType.name}")
            appendLine("Search Period: ${searchParams.startDate} to ${searchParams.endDate}")
            appendLine("Total Windows Found: $totalWindowsFound")
            appendLine("Excellent Windows (80+): $excellentWindows")
            appendLine()

            bestWindow?.let { best ->
                appendLine("BEST MUHURTA")
                appendLine("─".repeat(50))
                appendLine("Date/Time: ${best.startDateTime}")
                appendLine("Score: ${best.score}/100")
                appendLine("Day: ${best.vara.name}")
                appendLine("Nakshatra: ${best.nakshatra.nakshatra.displayName}")
                appendLine("Tithi: ${best.tithi.name}")
                appendLine()
                appendLine("Positive Factors:")
                best.positiveFactors.forEach { appendLine("  + $it") }
                if (best.negativeFactors.isNotEmpty()) {
                    appendLine("Considerations:")
                    best.negativeFactors.forEach { appendLine("  - $it") }
                }
            }
        }
    }

    /**
     * Find optimal muhurta windows for an activity
     */
    fun findOptimalWindows(
        params: SearchParams,
        muhurtaCalculator: MuhurtaCalculatorInterface
    ): OptimizationResult {
        val startTime = System.currentTimeMillis()
        val allWindows = mutableListOf<OptimizedMuhurtaWindow>()

        var currentDate = params.startDate
        while (!currentDate.isAfter(params.endDate)) {
            // Get panchanga for this date
            val dayWindows = scanDayForWindows(
                date = currentDate,
                params = params,
                muhurtaCalculator = muhurtaCalculator
            )
            allWindows.addAll(dayWindows)
            currentDate = currentDate.plusDays(1)
        }

        // Sort by score (descending) and assign ranks
        val sortedWindows = allWindows
            .sortedByDescending { it.score }
            .take(params.maxResults)
            .mapIndexed { index, window ->
                window.copy(rank = index + 1)
            }

        val excellentCount = sortedWindows.count { it.isExcellent }
        val searchDuration = System.currentTimeMillis() - startTime

        return OptimizationResult(
            activityType = params.activityType,
            searchParams = params,
            totalWindowsFound = sortedWindows.size,
            excellentWindows = excellentCount,
            windows = sortedWindows,
            bestWindow = sortedWindows.firstOrNull(),
            searchDurationMs = searchDuration
        )
    }

    /**
     * Scan a single day for muhurta windows
     */
    private fun scanDayForWindows(
        date: LocalDate,
        params: SearchParams,
        muhurtaCalculator: MuhurtaCalculatorInterface
    ): List<OptimizedMuhurtaWindow> {
        val windows = mutableListOf<OptimizedMuhurtaWindow>()

        // Get sunrise/sunset for daylight constraint
        val sunTimes = muhurtaCalculator.getSunTimes(date, params.latitude, params.longitude, params.timezone)
        val dayStart = if (params.requireDaylight) sunTimes.sunrise else LocalTime.of(0, 0)
        val dayEnd = if (params.requireDaylight) sunTimes.sunset else LocalTime.of(23, 59)

        // Apply preferred time range if specified
        val searchStart = params.preferredTimeRange?.let {
            maxOf(dayStart, it.startTime)
        } ?: dayStart
        val searchEnd = params.preferredTimeRange?.let {
            minOf(dayEnd, it.endTime)
        } ?: dayEnd

        // Get inauspicious periods for this day
        val inauspiciousPeriods = muhurtaCalculator.getInauspiciousPeriods(
            date, params.latitude, params.longitude, params.timezone
        )

        // Get panchanga data
        val panchangaData = muhurtaCalculator.getPanchanga(date, params.latitude, params.longitude, params.timezone)

        // Track window building
        var windowStart: LocalDateTime? = null
        var windowScore = 0
        var windowDetails: MuhurtaSlotDetails? = null

        var currentTime = searchStart
        while (currentTime.isBefore(searchEnd) || currentTime == searchEnd) {
            val dateTime = LocalDateTime.of(date, currentTime)

            // Skip if in inauspicious period
            val inInauspicious = params.avoidInauspiciousPeriods && isInInauspiciousPeriod(
                currentTime, inauspiciousPeriods
            )

            if (inInauspicious) {
                // Close any open window
                if (windowStart != null && windowDetails != null) {
                    windows.add(createWindow(windowStart, dateTime.minusMinutes(params.intervalMinutes),
                        windowScore, windowDetails, params.activityType))
                    windowStart = null
                    windowDetails = null
                }
                currentTime = currentTime.plusMinutes(params.intervalMinutes)
                continue
            }

            // Calculate score for this time slot
            val slotDetails = calculateSlotDetails(
                dateTime = dateTime,
                panchangaData = panchangaData,
                sunTimes = sunTimes,
                activityType = params.activityType,
                muhurtaCalculator = muhurtaCalculator,
                latitude = params.latitude,
                longitude = params.longitude,
                timezone = params.timezone
            )

            if (slotDetails.score >= params.minScore) {
                if (windowStart == null) {
                    // Start new window
                    windowStart = dateTime
                    windowScore = slotDetails.score
                    windowDetails = slotDetails
                } else {
                    // Continue window, update if better score
                    if (slotDetails.score > windowScore) {
                        windowScore = slotDetails.score
                        windowDetails = slotDetails
                    }
                }
            } else {
                // Score dropped below threshold, close window if open
                if (windowStart != null && windowDetails != null) {
                    windows.add(createWindow(windowStart, dateTime.minusMinutes(params.intervalMinutes),
                        windowScore, windowDetails, params.activityType))
                    windowStart = null
                    windowDetails = null
                }
            }

            currentTime = currentTime.plusMinutes(params.intervalMinutes)
        }

        // Close any remaining open window
        if (windowStart != null && windowDetails != null) {
            windows.add(createWindow(windowStart, LocalDateTime.of(date, searchEnd),
                windowScore, windowDetails, params.activityType))
        }

        return windows
    }

    /**
     * Calculate details for a specific time slot
     */
    private fun calculateSlotDetails(
        dateTime: LocalDateTime,
        panchangaData: PanchangaData,
        sunTimes: SunTimes,
        activityType: ActivityType,
        muhurtaCalculator: MuhurtaCalculatorInterface,
        latitude: Double,
        longitude: Double,
        timezone: String
    ): MuhurtaSlotDetails {
        var score = 50 // Base score
        val positiveFactors = mutableListOf<String>()
        val negativeFactors = mutableListOf<String>()

        // 1. Nakshatra Analysis (25 points max)
        val nakshatra = panchangaData.nakshatra
        if (nakshatra.nakshatra in activityType.favorableNakshatras) {
            score += 20
            positiveFactors.add("Favorable Nakshatra: ${nakshatra.nakshatra.displayName}")
        } else if (nakshatra.nakshatra in activityType.avoidNakshatras) {
            score -= 15
            negativeFactors.add("Unfavorable Nakshatra: ${nakshatra.nakshatra.displayName}")
        }

        // 2. Tithi Analysis (15 points max)
        val tithi = panchangaData.tithi
        if (tithi.displayNumber in activityType.favorableTithis) {
            score += 15
            positiveFactors.add("Favorable Tithi: ${tithi.name}")
        }
        if (!tithi.isAuspicious) {
            score -= 10
            negativeFactors.add("Challenging Tithi: ${tithi.name}")
        }

        // 3. Vara Analysis (10 points max)
        val vara = panchangaData.vara
        if (vara in activityType.favorableVaras) {
            score += 10
            positiveFactors.add("Favorable Day: ${vara.name}")
        }

        // 4. Yoga Analysis (10 points max)
        val yoga = panchangaData.yoga
        if (yoga.isAuspicious) {
            score += 10
            positiveFactors.add("Auspicious Yoga: ${yoga.name}")
        } else {
            score -= 5
            negativeFactors.add("Challenging Yoga: ${yoga.name}")
        }

        // 5. Karana Analysis (5 points max)
        val karana = panchangaData.karana
        if (karana.isAuspicious) {
            score += 5
            positiveFactors.add("Favorable Karana: ${karana.name}")
        }

        // 6. Choghadiya Analysis (10 points max)
        val choghadiya = muhurtaCalculator.getChoghadiya(dateTime, latitude, longitude, timezone)
        when (choghadiya.choghadiya.nature) {
            ChoghadiyaNature.EXCELLENT -> {
                score += 10
                positiveFactors.add("Excellent Choghadiya: ${choghadiya.choghadiya.name}")
            }
            ChoghadiyaNature.VERY_GOOD -> {
                score += 7
                positiveFactors.add("Very Good Choghadiya: ${choghadiya.choghadiya.name}")
            }
            ChoghadiyaNature.GOOD -> {
                score += 4
            }
            ChoghadiyaNature.INAUSPICIOUS -> {
                score -= 10
                negativeFactors.add("Inauspicious Choghadiya: ${choghadiya.choghadiya.name}")
            }
            else -> {}
        }

        // 7. Hora Analysis (5 points max)
        val hora = muhurtaCalculator.getHora(dateTime, latitude, longitude, timezone)
        when (hora.nature) {
            HoraNature.BENEFIC -> {
                score += 5
                positiveFactors.add("Benefic Hora: ${hora.lord.displayName}")
            }
            HoraNature.MALEFIC -> {
                score -= 3
            }
            else -> {}
        }

        // 8. Abhijit Muhurta Bonus (10 points)
        val isAbhijit = isAbhijitMuhurta(dateTime.toLocalTime(), sunTimes)
        if (isAbhijit) {
            score += 10
            positiveFactors.add("Abhijit Muhurta - Most Auspicious!")
        }

        // 9. Special Yogas
        val specialYogas = muhurtaCalculator.getSpecialYogas(dateTime, latitude, longitude, timezone)
        for (yoga in specialYogas) {
            if (yoga.isAuspicious) {
                score += 5
                positiveFactors.add("Special Yoga: ${yoga.name}")
            } else {
                score -= 5
                negativeFactors.add("Negative Yoga: ${yoga.name}")
            }
        }

        // Clamp score to 0-100
        score = score.coerceIn(0, 100)

        return MuhurtaSlotDetails(
            dateTime = dateTime,
            score = score,
            vara = vara,
            tithi = tithi,
            nakshatra = nakshatra,
            yoga = panchangaData.yoga,
            karana = karana,
            choghadiya = choghadiya.choghadiya,
            hora = hora,
            isAbhijitMuhurta = isAbhijit,
            specialYogas = specialYogas,
            positiveFactors = positiveFactors,
            negativeFactors = negativeFactors
        )
    }

    /**
     * Check if time is in Abhijit Muhurta
     */
    private fun isAbhijitMuhurta(time: LocalTime, sunTimes: SunTimes): Boolean {
        val dayDuration = ChronoUnit.MINUTES.between(sunTimes.sunrise, sunTimes.sunset)
        val muhurtaDuration = dayDuration / 15.0 // 15 muhurtas in a day

        // Abhijit is the 8th muhurta (middle of the day)
        val abhijitStart = sunTimes.sunrise.plusMinutes((7 * muhurtaDuration).toLong())
        val abhijitEnd = sunTimes.sunrise.plusMinutes((8 * muhurtaDuration).toLong())

        return time >= abhijitStart && time < abhijitEnd
    }

    /**
     * Check if time is in any inauspicious period
     */
    private fun isInInauspiciousPeriod(time: LocalTime, periods: InauspiciousPeriods): Boolean {
        return periods.rahukala.contains(time) ||
                periods.yamaghanta.contains(time) ||
                periods.gulikaKala.contains(time)
    }

    /**
     * Create an optimized muhurta window from slot details
     */
    private fun createWindow(
        startDateTime: LocalDateTime,
        endDateTime: LocalDateTime,
        score: Int,
        details: MuhurtaSlotDetails,
        activityType: ActivityType
    ): OptimizedMuhurtaWindow {
        val recommendation = generateRecommendation(score, details, activityType)

        return OptimizedMuhurtaWindow(
            startDateTime = startDateTime,
            endDateTime = endDateTime,
            score = score,
            rank = 0, // Will be assigned later
            vara = details.vara,
            tithi = details.tithi,
            nakshatra = details.nakshatra,
            yoga = details.yoga,
            karana = details.karana,
            choghadiya = details.choghadiya,
            hora = details.hora,
            isAbhijitMuhurta = details.isAbhijitMuhurta,
            specialYogas = details.specialYogas,
            positiveFactors = details.positiveFactors,
            negativeFactors = details.negativeFactors,
            recommendation = recommendation
        )
    }

    /**
     * Generate recommendation text
     */
    private fun generateRecommendation(
        score: Int,
        details: MuhurtaSlotDetails,
        activityType: ActivityType
    ): String {
        return when {
            score >= 90 -> "HIGHLY RECOMMENDED: Excellent muhurta for ${activityType.name.lowercase()}. " +
                    "All major factors are favorable. Proceed with confidence."
            score >= 80 -> "RECOMMENDED: Very good muhurta for ${activityType.name.lowercase()}. " +
                    "Most factors support this activity."
            score >= 70 -> "SUITABLE: Good muhurta for ${activityType.name.lowercase()}. " +
                    "Consider the minor negative factors before proceeding."
            score >= 60 -> "ACCEPTABLE: Average muhurta. Some challenges exist but " +
                    "activity can proceed with awareness."
            else -> "NOT IDEAL: Below threshold. Consider finding a better time."
        }
    }

    /**
     * Quick search for the single best muhurta in a date range
     */
    fun findBestMuhurta(
        params: SearchParams,
        muhurtaCalculator: MuhurtaCalculatorInterface
    ): OptimizedMuhurtaWindow? {
        val result = findOptimalWindows(params.copy(maxResults = 1), muhurtaCalculator)
        return result.bestWindow
    }

    /**
     * Find muhurtas on a specific date only
     */
    fun findMuhurtasOnDate(
        date: LocalDate,
        activityType: ActivityType,
        latitude: Double,
        longitude: Double,
        timezone: String,
        muhurtaCalculator: MuhurtaCalculatorInterface,
        minScore: Int = MIN_SCORE_THRESHOLD
    ): List<OptimizedMuhurtaWindow> {
        val params = SearchParams(
            startDate = date,
            endDate = date,
            activityType = activityType,
            minScore = minScore,
            maxResults = 50,
            latitude = latitude,
            longitude = longitude,
            timezone = timezone
        )
        return findOptimalWindows(params, muhurtaCalculator).windows
    }
}

/**
 * Internal class for slot analysis
 */
internal data class MuhurtaSlotDetails(
    val dateTime: LocalDateTime,
    val score: Int,
    val vara: Vara,
    val tithi: TithiInfo,
    val nakshatra: NakshatraInfo,
    val yoga: YogaInfo,
    val karana: KaranaInfo,
    val choghadiya: Choghadiya,
    val hora: Hora,
    val isAbhijitMuhurta: Boolean,
    val specialYogas: List<SpecialYoga>,
    val positiveFactors: List<String>,
    val negativeFactors: List<String>
)

/**
 * Sun times data
 */
data class SunTimes(
    val sunrise: LocalTime,
    val sunset: LocalTime
)

/**
 * Interface for muhurta calculator dependency injection
 */
interface MuhurtaCalculatorInterface {
    fun getSunTimes(date: LocalDate, latitude: Double, longitude: Double, timezone: String): SunTimes
    fun getInauspiciousPeriods(date: LocalDate, latitude: Double, longitude: Double, timezone: String): InauspiciousPeriods
    fun getPanchanga(date: LocalDate, latitude: Double, longitude: Double, timezone: String): PanchangaData
    fun getChoghadiya(dateTime: LocalDateTime, latitude: Double, longitude: Double, timezone: String): ChoghadiyaInfo
    fun getHora(dateTime: LocalDateTime, latitude: Double, longitude: Double, timezone: String): Hora
    fun getSpecialYogas(dateTime: LocalDateTime, latitude: Double, longitude: Double, timezone: String): List<SpecialYoga>
}
