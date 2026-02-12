package com.astro.storm.ephemeris

import android.content.Context
import com.astro.storm.core.common.Language
import com.astro.storm.core.common.StringKeyAnalysis
import com.astro.storm.core.common.StringResources
import com.astro.storm.core.common.StringKeyTarabala
import com.astro.storm.core.common.StringKeyNative
import com.astro.storm.core.common.getLocalizedName
import com.astro.storm.core.model.Nakshatra
import com.astro.storm.core.model.Planet
import com.astro.storm.core.model.PlanetPosition
import com.astro.storm.core.model.VedicChart
import com.astro.storm.core.model.ZodiacSign
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneId
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Comprehensive Tarabala and Chandrabala Calculator
 *
 * This calculator implements two essential daily strength systems from Vedic astrology:
 *
 * 1. **TARABALA (Star Strength)**
 *    - Based on the 9-fold nakshatra cycle from birth nakshatra
 *    - Determines favorable and unfavorable days for activities
 *    - Traditional 9 Taras: Janma, Sampat, Vipat, Kshema, Pratyari, Sadhaka, Vadha, Mitra, Parama Mitra
 *
 * 2. **CHANDRABALA (Moon Strength)**
 *    - Based on transit Moon's position from natal Moon sign
 *    - Houses 3, 6, 10, 11 are favorable (Upachaya)
 *    - Houses 1, 2, 5, 7, 9 are neutral to good
 *    - Houses 4, 8, 12 are challenging
 *
 * Combined Analysis:
 * - Both Tarabala and Chandrabala should be favorable for important activities
 * - Used extensively in Muhurta (electional astrology) and daily predictions
 *
 * Classical References:
 * - Muhurta Chintamani
 * - Muhurta Martanda
 * - Phaladeepika (Transit chapters)
 *
 * @author AstroStorm - Ultra-Precision Vedic Astrology
 */
@Singleton
class TarabalaCalculator @Inject constructor(
    private val ephemerisEngine: SwissEphemerisEngine
) {

    // ============================================================================
    // TARA (NAKSHATRA) TYPES - 9-fold cycle
    // ============================================================================

    /**
     * The nine Taras in the nakshatra cycle.
     * Each represents a specific strength/weakness pattern.
     */
    enum class Tara(
        val number: Int,
        val sanskritName: String,
        val englishMeaning: String,
        val isFavorable: Boolean,
        val strength: Int // 1-5 scale, 5 being most favorable
    ) {
        JANMA(1, "Janma", "Birth Star", false, 2),
        SAMPAT(2, "Sampat", "Wealth", true, 4),
        VIPAT(3, "Vipat", "Danger", false, 1),
        KSHEMA(4, "Kshema", "Well-being", true, 4),
        PRATYARI(5, "Pratyari", "Obstacle", false, 2),
        SADHAKA(6, "Sadhaka", "Achievement", true, 3),
        VADHA(7, "Vadha", "Death", false, 1),
        MITRA(8, "Mitra", "Friend", true, 4),
        PARAMA_MITRA(9, "Parama Mitra", "Great Friend", true, 5);

        /**
         * Get localized name
         */
        fun getLocalizedName(language: Language): String {
            val key = when (this) {
                JANMA -> StringKeyAnalysis.TARA_JANMA
                SAMPAT -> StringKeyAnalysis.TARA_SAMPAT
                VIPAT -> StringKeyAnalysis.TARA_VIPAT
                KSHEMA -> StringKeyAnalysis.TARA_KSHEMA
                PRATYARI -> StringKeyAnalysis.TARA_PRATYARI
                SADHAKA -> StringKeyAnalysis.TARA_SADHAKA
                VADHA -> StringKeyAnalysis.TARA_VADHA
                MITRA -> StringKeyAnalysis.TARA_MITRA
                PARAMA_MITRA -> StringKeyAnalysis.TARA_PARAMA_MITRA
            }
            return StringResources.get(key, language)
        }

        /**
         * Get localized description
         */
        fun getLocalizedDescription(language: Language): String {
            val key = when (this) {
                JANMA -> StringKeyAnalysis.TARA_JANMA_DESC
                SAMPAT -> StringKeyAnalysis.TARA_SAMPAT_DESC
                VIPAT -> StringKeyAnalysis.TARA_VIPAT_DESC
                KSHEMA -> StringKeyAnalysis.TARA_KSHEMA_DESC
                PRATYARI -> StringKeyAnalysis.TARA_PRATYARI_DESC
                SADHAKA -> StringKeyAnalysis.TARA_SADHAKA_DESC
                VADHA -> StringKeyAnalysis.TARA_VADHA_DESC
                MITRA -> StringKeyAnalysis.TARA_MITRA_DESC
                PARAMA_MITRA -> StringKeyAnalysis.TARA_PARAMA_MITRA_DESC
            }
            return StringResources.get(key, language)
        }

        companion object {
            fun fromNumber(num: Int): Tara = entries.find { it.number == num } ?: JANMA
        }
    }

    // ============================================================================
    // CHANDRABALA - Moon position strength
    // ============================================================================

    /**
     * Chandrabala strength levels based on Moon's transit house from natal Moon.
     */
    enum class ChandrabalaStrength(
        val displayName: String,
        val strength: Int, // 1-5 scale
        val isFavorable: Boolean
    ) {
        EXCELLENT("Excellent", 5, true),
        GOOD("Good", 4, true),
        NEUTRAL("Neutral", 3, true),
        WEAK("Weak", 2, false),
        UNFAVORABLE("Unfavorable", 1, false);

        fun getLocalizedName(language: Language): String {
            val key = when (this) {
                EXCELLENT -> StringKeyAnalysis.CHANDRABALA_EXCELLENT
                GOOD -> StringKeyAnalysis.CHANDRABALA_GOOD
                NEUTRAL -> StringKeyAnalysis.CHANDRABALA_NEUTRAL
                WEAK -> StringKeyAnalysis.CHANDRABALA_WEAK
                UNFAVORABLE -> StringKeyAnalysis.CHANDRABALA_UNFAVORABLE
            }
            return StringResources.get(key, language)
        }
    }

    /**
     * Chandrabala house significations - each house from Moon has specific effects
     */
    private val CHANDRABALA_HOUSE_STRENGTH = mapOf(
        1 to ChandrabalaStrength.NEUTRAL,      // Self - neutral
        2 to ChandrabalaStrength.GOOD,         // Wealth - generally good
        3 to ChandrabalaStrength.EXCELLENT,    // Courage (Upachaya) - excellent
        4 to ChandrabalaStrength.WEAK,         // Home - can be restrictive
        5 to ChandrabalaStrength.GOOD,         // Intelligence - good
        6 to ChandrabalaStrength.EXCELLENT,    // Victory (Upachaya) - excellent
        7 to ChandrabalaStrength.GOOD,         // Partnership - good
        8 to ChandrabalaStrength.UNFAVORABLE,  // Obstacles - unfavorable
        9 to ChandrabalaStrength.GOOD,         // Fortune - good
        10 to ChandrabalaStrength.EXCELLENT,   // Career (Upachaya) - excellent
        11 to ChandrabalaStrength.EXCELLENT,   // Gains (Upachaya) - excellent
        12 to ChandrabalaStrength.UNFAVORABLE  // Losses - unfavorable
    )

    // ============================================================================
    // DATA CLASSES - Results
    // ============================================================================

    /**
     * Single Tarabala result for a specific nakshatra
     */
    data class TarabalaResult(
        val targetNakshatra: Nakshatra,
        val tara: Tara,
        val cycle: Int, // Which 9-nakshatra cycle (1-3)
        val overallStrength: Int,
        val recommendations: List<String>
    ) {
        val isFavorable: Boolean get() = tara.isFavorable
    }

    /**
     * Single Chandrabala result
     */
    data class ChandrabalaResult(
        val transitMoonSign: ZodiacSign,
        val natalMoonSign: ZodiacSign,
        val houseFromMoon: Int,
        val strength: ChandrabalaStrength,
        val significations: String,
        val recommendations: List<String>
    ) {
        val isFavorable: Boolean get() = strength.isFavorable
    }

    /**
     * Combined daily strength result
     */
    data class DailyStrengthResult(
        val date: LocalDate,
        val tarabala: TarabalaResult,
        val chandrabala: ChandrabalaResult,
        val combinedStrength: CombinedStrength,
        val overallScore: Int, // 0-100
        val favorableActivities: List<ActivityRecommendation>,
        val avoidActivities: List<ActivityRecommendation>,
        val generalAdvice: String
    )

    /**
     * Combined strength assessment
     */
    enum class CombinedStrength(
        val displayName: String,
        val score: Int
    ) {
        HIGHLY_FAVORABLE("Highly Favorable", 5),
        FAVORABLE("Favorable", 4),
        MIXED("Mixed Results", 3),
        CHALLENGING("Challenging", 2),
        UNFAVORABLE("Unfavorable", 1);

        fun getLocalizedName(language: Language): String {
            val key = when (this) {
                HIGHLY_FAVORABLE -> StringKeyAnalysis.COMBINED_HIGHLY_FAVORABLE
                FAVORABLE -> StringKeyAnalysis.COMBINED_FAVORABLE
                MIXED -> StringKeyAnalysis.COMBINED_MIXED
                CHALLENGING -> StringKeyAnalysis.COMBINED_CHALLENGING
                UNFAVORABLE -> StringKeyAnalysis.COMBINED_UNFAVORABLE
            }
            return StringResources.get(key, language)
        }
    }

    /**
     * Activity recommendation with suitability rating
     */
    data class ActivityRecommendation(
        val activity: String,
        val suitability: ActivitySuitability,
        val reason: String
    )

    enum class ActivitySuitability {
        HIGHLY_RECOMMENDED,
        RECOMMENDED,
        ACCEPTABLE,
        CAUTION,
        AVOID
    }

    /**
     * Weekly forecast with daily strengths
     */
    data class WeeklyForecast(
        val startDate: LocalDate,
        val dailyStrengths: List<DailyStrengthResult>,
        val bestDay: DailyStrengthResult,
        val worstDay: DailyStrengthResult,
        val weeklyAdvice: String
    )

    /**
     * Complete Tarabala/Chandrabala analysis
     */
    data class TarabalaChandrabalaAnalysis(
        val birthNakshatra: Nakshatra,
        val natalMoonSign: ZodiacSign,
        val currentDate: LocalDate,
        val currentTarabala: TarabalaResult,
        val currentChandrabala: ChandrabalaResult,
        val todayStrength: DailyStrengthResult,
        val weeklyForecast: WeeklyForecast,
        val allTaras: List<TarabalaResult>, // All 27 nakshatras analyzed
        val timestamp: Long = System.currentTimeMillis()
    )

    // ============================================================================
    // CALCULATION METHODS
    // ============================================================================

    /**
     * Calculate comprehensive analysis for current moment or specific time
     */
    fun calculateAnalysis(
        chart: VedicChart,
        dateTime: LocalDateTime? = null,
        language: Language = Language.ENGLISH
    ): TarabalaChandrabalaAnalysis? {
        val effectiveDateTime = dateTime ?: LocalDateTime.now(resolveZoneId(chart.birthData.timezone))
        val moonPosition = chart.planetPositions.find { it.planet == Planet.MOON } ?: return null
        val birthNakshatra = moonPosition.nakshatra
        val natalMoonSign = moonPosition.sign

        // Calculate transit positions
        val transitMoonSign = calculateTransitMoonSign(effectiveDateTime, chart) ?: natalMoonSign
        val transitNakshatra = calculateTransitNakshatra(effectiveDateTime, chart) ?: birthNakshatra

        val currentTarabala = calculateTarabala(birthNakshatra, transitNakshatra, language)
        val currentChandrabala = calculateChandrabala(natalMoonSign, transitMoonSign, language)
        val todayStrength = calculateDailyStrength(effectiveDateTime.toLocalDate(), currentTarabala, currentChandrabala, language)

        val weeklyForecast = calculateWeeklyForecast(chart, effectiveDateTime.toLocalDate(), language)

        // Calculate all 27 nakshatras for the "All Nakshatras" tab
        val allTaras = Nakshatra.entries.map { nak ->
            calculateTarabala(birthNakshatra, nak, language)
        }

        return TarabalaChandrabalaAnalysis(
            birthNakshatra = birthNakshatra,
            natalMoonSign = natalMoonSign,
            currentDate = effectiveDateTime.toLocalDate(),
            currentTarabala = currentTarabala,
            currentChandrabala = currentChandrabala,
            todayStrength = todayStrength,
            weeklyForecast = weeklyForecast,
            allTaras = allTaras
        )
    }

    private fun resolveZoneId(timezone: String?): ZoneId {
        if (timezone.isNullOrBlank()) return ZoneId.systemDefault()
        return runCatching { ZoneId.of(timezone.trim()) }.getOrElse { ZoneId.systemDefault() }
    }

    /**
     * Calculate Tarabala for a specific nakshatra from birth nakshatra
     */
    fun calculateTarabala(birthNakshatra: Nakshatra, targetNakshatra: Nakshatra, language: Language = Language.ENGLISH): TarabalaResult {
        val birthNum = birthNakshatra.number
        val targetNum = targetNakshatra.number

        // Calculate position in the 27-nakshatra cycle
        var diff = targetNum - birthNum
        if (diff < 0) diff += 27
        if (diff == 0) diff = 0 // Same nakshatra

        // Determine which cycle (1-3) and tara number (1-9)
        val cycle = (diff / 9) + 1
        val taraNumber = if (diff == 0) 1 else (diff % 9) + 1

        val tara = Tara.fromNumber(if (taraNumber == 0) 9 else taraNumber)

        // Calculate strength with cycle consideration
        // First cycle is strongest, third cycle is weakest
        val cycleModifier = when (cycle) {
            1 -> 1.0
            2 -> 0.8
            3 -> 0.6
            else -> 1.0
        }

        val overallStrength = (tara.strength * cycleModifier * 20).toInt()

        val recommendations = generateTarabalaRecommendations(tara, language)

        return TarabalaResult(
            targetNakshatra = targetNakshatra,
            tara = tara,
            cycle = cycle.coerceIn(1, 3),
            overallStrength = overallStrength,
            recommendations = recommendations
        )
    }

    /**
     * Calculate Chandrabala based on transit Moon position from natal Moon
     */
    fun calculateChandrabala(natalMoonSign: ZodiacSign, transitMoonSign: ZodiacSign, language: Language = Language.ENGLISH): ChandrabalaResult {
        // Calculate house position of transit Moon from natal Moon
        var houseFromMoon = transitMoonSign.number - natalMoonSign.number + 1
        if (houseFromMoon <= 0) houseFromMoon += 12

        val strength = CHANDRABALA_HOUSE_STRENGTH[houseFromMoon] ?: ChandrabalaStrength.NEUTRAL

        val significations = getHouseSignifications(houseFromMoon, language)
        val recommendations = generateChandrabalaRecommendations(houseFromMoon, language)

        return ChandrabalaResult(
            transitMoonSign = transitMoonSign,
            natalMoonSign = natalMoonSign,
            houseFromMoon = houseFromMoon,
            strength = strength,
            significations = significations,
            recommendations = recommendations
        )
    }

    /**
     * Calculate combined daily strength from Tarabala and Chandrabala
     */
    fun calculateDailyStrength(
        date: LocalDate,
        tarabala: TarabalaResult,
        chandrabala: ChandrabalaResult,
        language: Language = Language.ENGLISH
    ): DailyStrengthResult {
        // Calculate combined strength
        val taraScore = tarabala.overallStrength
        val chandraScore = chandrabala.strength.strength * 20

        val overallScore = ((taraScore + chandraScore) / 2).coerceIn(0, 100)

        val combinedStrength = when {
            tarabala.isFavorable && chandrabala.isFavorable && overallScore >= 70 -> CombinedStrength.HIGHLY_FAVORABLE
            tarabala.isFavorable && chandrabala.isFavorable -> CombinedStrength.FAVORABLE
            tarabala.isFavorable || chandrabala.isFavorable -> CombinedStrength.MIXED
            !tarabala.isFavorable && !chandrabala.isFavorable && overallScore <= 30 -> CombinedStrength.UNFAVORABLE
            else -> CombinedStrength.CHALLENGING
        }

        val (favorableActivities, avoidActivities) = generateActivityRecommendations(
            tarabala,
            chandrabala,
            combinedStrength,
            language
        )

        val generalAdvice = generateGeneralAdvice(tarabala, chandrabala, combinedStrength, language)

        return DailyStrengthResult(
            date = date,
            tarabala = tarabala,
            chandrabala = chandrabala,
            combinedStrength = combinedStrength,
            overallScore = overallScore,
            favorableActivities = favorableActivities,
            avoidActivities = avoidActivities,
            generalAdvice = generalAdvice
        )
    }

    /**
     * Calculate weekly forecast
     */
    fun calculateWeeklyForecast(
        chart: VedicChart,
        startDate: LocalDate,
        language: Language = Language.ENGLISH
    ): WeeklyForecast {
        val moonPosition = chart.planetPositions.find { it.planet == Planet.MOON }
        if (moonPosition == null) {
            // Return empty forecast if no Moon position
            val emptyResult = createEmptyDailyResult(startDate)
            return WeeklyForecast(
                startDate = startDate,
                dailyStrengths = (0..6).map { createEmptyDailyResult(startDate.plusDays(it.toLong())) },
                bestDay = emptyResult,
                worstDay = emptyResult,
                weeklyAdvice = "Unable to calculate - Moon position unavailable"
            )
        }

        val birthNakshatra = moonPosition.nakshatra
        val natalMoonSign = moonPosition.sign

        val dailyStrengths = (0..6).map { dayOffset ->
            val date = startDate.plusDays(dayOffset.toLong())
            val dateTime = date.atStartOfDay()

            // Calculate transit positions for this date
            val transitMoonSign = calculateTransitMoonSignForDate(date, chart) ?: natalMoonSign
            val transitNakshatra = calculateTransitNakshatraForDate(date, chart) ?: birthNakshatra

            val tarabala = calculateTarabala(birthNakshatra, transitNakshatra, language)
            val chandrabala = calculateChandrabala(natalMoonSign, transitMoonSign, language)

            calculateDailyStrength(date, tarabala, chandrabala, language)
        }

        val bestDay = dailyStrengths.maxByOrNull { it.overallScore } ?: dailyStrengths.first()
        val worstDay = dailyStrengths.minByOrNull { it.overallScore } ?: dailyStrengths.first()

        val weeklyAdvice = generateWeeklyAdvice(dailyStrengths, bestDay, worstDay, language)

        return WeeklyForecast(
            startDate = startDate,
            dailyStrengths = dailyStrengths,
            bestDay = bestDay,
            worstDay = worstDay,
            weeklyAdvice = weeklyAdvice
        )
    }

    // ============================================================================
    // TRANSIT MOON CALCULATIONS
    // ============================================================================

    /**
     * Calculate current transit Moon sign using ephemeris
     */
    private fun calculateTransitMoonSign(dateTime: LocalDateTime, chart: VedicChart): ZodiacSign? {
        return try {
            val moonPos = ephemerisEngine.calculatePlanetPosition(
                Planet.MOON, dateTime, chart.birthData.timezone,
                chart.birthData.latitude, chart.birthData.longitude
            )

            // Position is already in sidereal
            ZodiacSign.fromLongitude(moonPos.longitude)
        } catch (e: Exception) {
            null
        }
    }

    /**
     * Calculate transit Moon nakshatra
     */
    private fun calculateTransitNakshatra(dateTime: LocalDateTime, chart: VedicChart): Nakshatra? {
        return try {
            val moonPos = ephemerisEngine.calculatePlanetPosition(
                Planet.MOON, dateTime, chart.birthData.timezone,
                chart.birthData.latitude, chart.birthData.longitude
            )

            // Position is already in sidereal
            val nakshatraInfo = Nakshatra.fromLongitude(moonPos.longitude)
            nakshatraInfo.first
        } catch (e: Exception) {
            null
        }
    }

    /**
     * Calculate transit Moon sign for a specific date
     */
    private fun calculateTransitMoonSignForDate(date: LocalDate, chart: VedicChart): ZodiacSign? {
        return calculateTransitMoonSign(date.atTime(12, 0), chart)
    }

    /**
     * Calculate transit nakshatra for a specific date
     */
    private fun calculateTransitNakshatraForDate(date: LocalDate, chart: VedicChart): Nakshatra? {
        return calculateTransitNakshatra(date.atTime(12, 0), chart)
    }

    // ============================================================================
    // RECOMMENDATION GENERATORS
    // ============================================================================

    private fun generateTarabalaRecommendations(tara: Tara, language: Language): List<String> {
        return when (tara) {
            Tara.JANMA -> listOf(
                StringResources.get(StringKeyTarabala.TARA_JANMA_REC_1, language),
                StringResources.get(StringKeyTarabala.TARA_JANMA_REC_2, language),
                StringResources.get(StringKeyTarabala.TARA_JANMA_REC_3, language),
                StringResources.get(StringKeyTarabala.TARA_JANMA_REC_4, language)
            )
            Tara.SAMPAT -> listOf(
                StringResources.get(StringKeyTarabala.TARA_SAMPAT_REC_1, language),
                StringResources.get(StringKeyTarabala.TARA_SAMPAT_REC_2, language),
                StringResources.get(StringKeyTarabala.TARA_SAMPAT_REC_3, language),
                StringResources.get(StringKeyTarabala.TARA_SAMPAT_REC_4, language)
            )
            Tara.VIPAT -> listOf(
                StringResources.get(StringKeyTarabala.TARA_VIPAT_REC_1, language),
                StringResources.get(StringKeyTarabala.TARA_VIPAT_REC_2, language),
                StringResources.get(StringKeyTarabala.TARA_VIPAT_REC_3, language),
                StringResources.get(StringKeyTarabala.TARA_VIPAT_REC_4, language)
            )
            Tara.KSHEMA -> listOf(
                StringResources.get(StringKeyTarabala.TARA_KSHEMA_REC_1, language),
                StringResources.get(StringKeyTarabala.TARA_KSHEMA_REC_2, language),
                StringResources.get(StringKeyTarabala.TARA_KSHEMA_REC_3, language),
                StringResources.get(StringKeyTarabala.TARA_KSHEMA_REC_4, language)
            )
            Tara.PRATYARI -> listOf(
                StringResources.get(StringKeyTarabala.TARA_PRATYARI_REC_1, language),
                StringResources.get(StringKeyTarabala.TARA_PRATYARI_REC_2, language),
                StringResources.get(StringKeyTarabala.TARA_PRATYARI_REC_3, language),
                StringResources.get(StringKeyTarabala.TARA_PRATYARI_REC_4, language)
            )
            Tara.SADHAKA -> listOf(
                StringResources.get(StringKeyTarabala.TARA_SADHAKA_REC_1, language),
                StringResources.get(StringKeyTarabala.TARA_SADHAKA_REC_2, language),
                StringResources.get(StringKeyTarabala.TARA_SADHAKA_REC_3, language),
                StringResources.get(StringKeyTarabala.TARA_SADHAKA_REC_4, language)
            )
            Tara.VADHA -> listOf(
                StringResources.get(StringKeyTarabala.TARA_VADHA_REC_1, language),
                StringResources.get(StringKeyTarabala.TARA_VADHA_REC_2, language),
                StringResources.get(StringKeyTarabala.TARA_VADHA_REC_3, language),
                StringResources.get(StringKeyTarabala.TARA_VADHA_REC_4, language)
            )
            Tara.MITRA -> listOf(
                StringResources.get(StringKeyTarabala.TARA_MITRA_REC_1, language),
                StringResources.get(StringKeyTarabala.TARA_MITRA_REC_2, language),
                StringResources.get(StringKeyTarabala.TARA_MITRA_REC_3, language),
                StringResources.get(StringKeyTarabala.TARA_MITRA_REC_4, language)
            )
            Tara.PARAMA_MITRA -> listOf(
                StringResources.get(StringKeyTarabala.TARA_PARAMA_MITRA_REC_1, language),
                StringResources.get(StringKeyTarabala.TARA_PARAMA_MITRA_REC_2, language),
                StringResources.get(StringKeyTarabala.TARA_PARAMA_MITRA_REC_3, language),
                StringResources.get(StringKeyTarabala.TARA_PARAMA_MITRA_REC_4, language)
            )
        }
    }

    private fun generateChandrabalaRecommendations(house: Int, language: Language): List<String> {
        return when (house) {
            1 -> listOf(StringResources.get(StringKeyTarabala.CHANDRA_REC_1_1, language), StringResources.get(StringKeyTarabala.CHANDRA_REC_1_2, language), StringResources.get(StringKeyTarabala.CHANDRA_REC_1_3, language))
            2 -> listOf(StringResources.get(StringKeyTarabala.CHANDRA_REC_2_1, language), StringResources.get(StringKeyTarabala.CHANDRA_REC_2_2, language), StringResources.get(StringKeyTarabala.CHANDRA_REC_2_3, language))
            3 -> listOf(StringResources.get(StringKeyTarabala.CHANDRA_REC_3_1, language), StringResources.get(StringKeyTarabala.CHANDRA_REC_3_2, language), StringResources.get(StringKeyTarabala.CHANDRA_REC_3_3, language), StringResources.get(StringKeyTarabala.CHANDRA_REC_3_4, language))
            4 -> listOf(StringResources.get(StringKeyTarabala.CHANDRA_REC_4_1, language), StringResources.get(StringKeyTarabala.CHANDRA_REC_4_2, language), StringResources.get(StringKeyTarabala.CHANDRA_REC_4_3, language))
            5 -> listOf(StringResources.get(StringKeyTarabala.CHANDRA_REC_5_1, language), StringResources.get(StringKeyTarabala.CHANDRA_REC_5_2, language), StringResources.get(StringKeyTarabala.CHANDRA_REC_5_3, language), StringResources.get(StringKeyTarabala.CHANDRA_REC_5_4, language))
            6 -> listOf(StringResources.get(StringKeyTarabala.CHANDRA_REC_6_1, language), StringResources.get(StringKeyTarabala.CHANDRA_REC_6_2, language), StringResources.get(StringKeyTarabala.CHANDRA_REC_6_3, language), StringResources.get(StringKeyTarabala.CHANDRA_REC_6_4, language))
            7 -> listOf(StringResources.get(StringKeyTarabala.CHANDRA_REC_7_1, language), StringResources.get(StringKeyTarabala.CHANDRA_REC_7_2, language), StringResources.get(StringKeyTarabala.CHANDRA_REC_7_3, language))
            8 -> listOf(StringResources.get(StringKeyTarabala.CHANDRA_REC_8_1, language), StringResources.get(StringKeyTarabala.CHANDRA_REC_8_2, language), StringResources.get(StringKeyTarabala.CHANDRA_REC_8_3, language), StringResources.get(StringKeyTarabala.CHANDRA_REC_8_4, language))
            9 -> listOf(StringResources.get(StringKeyTarabala.CHANDRA_REC_9_1, language), StringResources.get(StringKeyTarabala.CHANDRA_REC_9_2, language), StringResources.get(StringKeyTarabala.CHANDRA_REC_9_3, language), StringResources.get(StringKeyTarabala.CHANDRA_REC_9_4, language))
            10 -> listOf(StringResources.get(StringKeyTarabala.CHANDRA_REC_10_1, language), StringResources.get(StringKeyTarabala.CHANDRA_REC_10_2, language), StringResources.get(StringKeyTarabala.CHANDRA_REC_10_3, language), StringResources.get(StringKeyTarabala.CHANDRA_REC_10_4, language))
            11 -> listOf(StringResources.get(StringKeyTarabala.CHANDRA_REC_11_1, language), StringResources.get(StringKeyTarabala.CHANDRA_REC_11_2, language), StringResources.get(StringKeyTarabala.CHANDRA_REC_11_3, language), StringResources.get(StringKeyTarabala.CHANDRA_REC_11_4, language))
            12 -> listOf(StringResources.get(StringKeyTarabala.CHANDRA_REC_12_1, language), StringResources.get(StringKeyTarabala.CHANDRA_REC_12_2, language), StringResources.get(StringKeyTarabala.CHANDRA_REC_12_3, language), StringResources.get(StringKeyTarabala.CHANDRA_REC_12_4, language))
            else -> listOf(StringResources.get(StringKeyTarabala.NEUTRAL_INFLUENCES, language))
        }
    }

    private fun generateActivityRecommendations(
        tarabala: TarabalaResult,
        chandrabala: ChandrabalaResult,
        combined: CombinedStrength,
        language: Language
    ): Pair<List<ActivityRecommendation>, List<ActivityRecommendation>> {
        val favorable = mutableListOf<ActivityRecommendation>()
        val avoid = mutableListOf<ActivityRecommendation>()

        when (combined) {
            CombinedStrength.HIGHLY_FAVORABLE -> {
                favorable.addAll(listOf(
                    ActivityRecommendation(StringResources.get(StringKeyTarabala.ACTIVITY_START_BUSINESS, language), ActivitySuitability.HIGHLY_RECOMMENDED, StringResources.get(StringKeyTarabala.REASON_TARA_CHANDRA_SUPPORT, language)),
                    ActivityRecommendation(StringResources.get(StringKeyTarabala.ACTIVITY_IMPORTANT_MEETINGS, language), ActivitySuitability.HIGHLY_RECOMMENDED, StringResources.get(StringKeyTarabala.REASON_COMM_ENHANCED, language)),
                    ActivityRecommendation(StringResources.get(StringKeyTarabala.ACTIVITY_FINANCIAL_INVEST, language), ActivitySuitability.HIGHLY_RECOMMENDED, StringResources.get(StringKeyTarabala.REASON_WEALTH_FAVORED, language)),
                    ActivityRecommendation(StringResources.get(StringKeyTarabala.ACTIVITY_MEDICAL_TREATMENT, language), ActivitySuitability.RECOMMENDED, StringResources.get(StringKeyTarabala.REASON_HEALTH_SUPPORTED, language)),
                    ActivityRecommendation(StringResources.get(StringKeyTarabala.ACTIVITY_TRAVEL, language), ActivitySuitability.HIGHLY_RECOMMENDED, StringResources.get(StringKeyTarabala.REASON_JOURNEYS_SUCCESS, language)),
                    ActivityRecommendation(StringResources.get(StringKeyTarabala.ACTIVITY_MARRIAGE, language), ActivitySuitability.HIGHLY_RECOMMENDED, StringResources.get(StringKeyTarabala.REASON_AUSPICIOUS_UNIONS, language))
                ))
            }
            CombinedStrength.FAVORABLE -> {
                favorable.addAll(listOf(
                    ActivityRecommendation(StringResources.get(StringKeyTarabala.ACTIVITY_BUSINESS_ACT, language), ActivitySuitability.RECOMMENDED, StringResources.get(StringKeyTarabala.REASON_GENERALLY_SUPPORTIVE, language)),
                    ActivityRecommendation(StringResources.get(StringKeyTarabala.ACTIVITY_ROUTINE_WORK, language), ActivitySuitability.HIGHLY_RECOMMENDED, StringResources.get(StringKeyTarabala.REASON_PRODUCTIVITY_ENHANCED, language)),
                    ActivityRecommendation(StringResources.get(StringKeyTarabala.ACTIVITY_SHORT_TRAVEL, language), ActivitySuitability.RECOMMENDED, StringResources.get(StringKeyTarabala.REASON_SAFE_JOURNEYS, language)),
                    ActivityRecommendation(StringResources.get(StringKeyTarabala.ACTIVITY_SOCIAL_EVENTS, language), ActivitySuitability.RECOMMENDED, StringResources.get(StringKeyTarabala.REASON_RELATIONSHIPS_FAVORED, language))
                ))
                avoid.addAll(listOf(
                    ActivityRecommendation(StringResources.get(StringKeyTarabala.ACTIVITY_HIGH_RISK_INVEST, language), ActivitySuitability.CAUTION, StringResources.get(StringKeyTarabala.REASON_NOT_BEST_SPECULATION, language))
                ))
            }
            CombinedStrength.MIXED -> {
                favorable.addAll(listOf(
                    ActivityRecommendation(StringResources.get(StringKeyTarabala.ACTIVITY_ROUTINE_ACT, language), ActivitySuitability.ACCEPTABLE, StringResources.get(StringKeyTarabala.REASON_NORMAL_PRODUCTIVITY, language)),
                    ActivityRecommendation(StringResources.get(StringKeyTarabala.ACTIVITY_ONGOING_PROJECTS, language), ActivitySuitability.RECOMMENDED, StringResources.get(StringKeyTarabala.REASON_CONTINUE_WORK, language))
                ))
                avoid.addAll(listOf(
                    ActivityRecommendation(StringResources.get(StringKeyTarabala.ACTIVITY_NEW_VENTURES, language), ActivitySuitability.CAUTION, StringResources.get(StringKeyTarabala.REASON_WAIT_BETTER_TIMING, language)),
                    ActivityRecommendation(StringResources.get(StringKeyTarabala.ACTIVITY_MAJOR_PURCHASES, language), ActivitySuitability.CAUTION, StringResources.get(StringKeyTarabala.REASON_DELAYS_POSSIBLE, language)),
                    ActivityRecommendation(StringResources.get(StringKeyTarabala.ACTIVITY_IMPORTANT_NEGOTIATIONS, language), ActivitySuitability.CAUTION, StringResources.get(StringKeyTarabala.REASON_MIXED_RESULTS, language))
                ))
            }
            CombinedStrength.CHALLENGING -> {
                favorable.addAll(listOf(
                    ActivityRecommendation(StringResources.get(StringKeyTarabala.ACTIVITY_SPIRITUAL_PRACTICES, language), ActivitySuitability.RECOMMENDED, StringResources.get(StringKeyTarabala.REASON_INNER_WORK_BENEFICIAL, language)),
                    ActivityRecommendation(StringResources.get(StringKeyTarabala.ACTIVITY_REST_RECOVERY, language), ActivitySuitability.RECOMMENDED, StringResources.get(StringKeyTarabala.REASON_REJUVENATION_FAVORED, language))
                ))
                avoid.addAll(listOf(
                    ActivityRecommendation(StringResources.get(StringKeyTarabala.ACTIVITY_NEW_BEGINNINGS, language), ActivitySuitability.AVOID, StringResources.get(StringKeyTarabala.REASON_OBSTACLES_LIKELY, language)),
                    ActivityRecommendation(StringResources.get(StringKeyTarabala.ACTIVITY_TRAVEL, language), ActivitySuitability.CAUTION, StringResources.get(StringKeyTarabala.REASON_DELAYS_PROBLEMS, language)),
                    ActivityRecommendation(StringResources.get(StringKeyTarabala.ACTIVITY_IMPORTANT_DECISIONS, language), ActivitySuitability.AVOID, StringResources.get(StringKeyTarabala.REASON_JUDGMENT_CLOUDED, language)),
                    ActivityRecommendation(StringResources.get(StringKeyTarabala.ACTIVITY_MEDICAL_PROCEDURES, language), ActivitySuitability.CAUTION, StringResources.get(StringKeyTarabala.REASON_UNLESS_EMERGENCY, language))
                ))
            }
            CombinedStrength.UNFAVORABLE -> {
                favorable.addAll(listOf(
                    ActivityRecommendation(StringResources.get(StringKeyTarabala.ACTIVITY_PRAYER_MEDITATION, language), ActivitySuitability.HIGHLY_RECOMMENDED, StringResources.get(StringKeyTarabala.REASON_SPIRITUAL_PROT_NEEDED, language)),
                    ActivityRecommendation(StringResources.get(StringKeyTarabala.ACTIVITY_CHARITY, language), ActivitySuitability.RECOMMENDED, StringResources.get(StringKeyTarabala.REASON_REDUCES_NEG_KARMA, language))
                ))
                avoid.addAll(listOf(
                    ActivityRecommendation(StringResources.get(StringKeyTarabala.ACTIVITY_MAJOR_ACT, language), ActivitySuitability.AVOID, StringResources.get(StringKeyTarabala.REASON_DAY_INAUSPICIOUS, language)),
                    ActivityRecommendation(StringResources.get(StringKeyTarabala.ACTIVITY_TRAVEL, language), ActivitySuitability.AVOID, StringResources.get(StringKeyTarabala.REASON_ACCIDENTS_LIKELY, language)),
                    ActivityRecommendation(StringResources.get(StringKeyTarabala.ACTIVITY_CONTRACTS_AGREEMENTS, language), ActivitySuitability.AVOID, StringResources.get(StringKeyTarabala.REASON_NOT_BENEFICIAL, language)),
                    ActivityRecommendation(StringResources.get(StringKeyTarabala.ACTIVITY_NEW_BEGINNINGS, language), ActivitySuitability.AVOID, StringResources.get(StringKeyTarabala.REASON_FACE_DESTRUCTION, language)),
                    ActivityRecommendation(StringResources.get(StringKeyTarabala.ACTIVITY_SURGERIES, language), ActivitySuitability.AVOID, StringResources.get(StringKeyTarabala.REASON_UNLESS_LIFE_THREATENING, language))
                ))
            }
        }

        return Pair(favorable, avoid)
    }

    private fun generateGeneralAdvice(
        tarabala: TarabalaResult,
        chandrabala: ChandrabalaResult,
        combined: CombinedStrength,
        language: Language
    ): String {
        return when (combined) {
            CombinedStrength.HIGHLY_FAVORABLE ->
                StringResources.get(StringKeyTarabala.ADVICE_HIGHLY_AUSPICIOUS, language, tarabala.tara.getLocalizedName(language))

            CombinedStrength.FAVORABLE ->
                StringResources.get(StringKeyTarabala.ADVICE_FAVORABLE, language, 
                    StringResources.get(if (tarabala.isFavorable) StringKeyTarabala.SUPPORT_POSITIVE else StringKeyTarabala.SUPPORT_NEUTRAL, language),
                    chandrabala.houseFromMoon,
                    chandrabala.strength.getLocalizedName(language))

            CombinedStrength.MIXED ->
                StringResources.get(StringKeyTarabala.ADVICE_MIXED, language, 
                    tarabala.tara.getLocalizedName(language),
                    StringResources.get(if (tarabala.isFavorable) StringKeyTarabala.SUPPORT_SUPPORT else StringKeyTarabala.SUPPORT_CHALLENGES, language),
                    chandrabala.strength.getLocalizedName(language))

            CombinedStrength.CHALLENGING ->
                StringResources.get(StringKeyTarabala.ADVICE_CHALLENGING, language, 
                    tarabala.tara.getLocalizedName(language),
                    chandrabala.houseFromMoon)

            CombinedStrength.UNFAVORABLE ->
                StringResources.get(StringKeyTarabala.ADVICE_UNFAVORABLE, language, 
                    tarabala.tara.getLocalizedName(language))
        }
    }

    private fun generateWeeklyAdvice(
        dailyStrengths: List<DailyStrengthResult>,
        bestDay: DailyStrengthResult,
        worstDay: DailyStrengthResult,
        language: Language
    ): String {
        val avgScore = dailyStrengths.map { it.overallScore }.average().toInt()
        val favorableDays = dailyStrengths.count { it.combinedStrength in listOf(CombinedStrength.HIGHLY_FAVORABLE, CombinedStrength.FAVORABLE) }

        return StringResources.get(StringKeyTarabala.ADVICE_WEEKLY_TEMPLATE, language,
            avgScore,
            favorableDays,
            bestDay.date.dayOfWeek.name.lowercase().replaceFirstChar { it.uppercase() },
            bestDay.overallScore,
            worstDay.date.dayOfWeek.name.lowercase().replaceFirstChar { it.uppercase() },
            worstDay.overallScore
        )
    }

    private fun getHouseSignifications(house: Int, language: Language): String {
        val key = when (house) {
            1 -> StringKeyTarabala.HOUSE_SIG_1
            2 -> StringKeyTarabala.HOUSE_SIG_2
            3 -> StringKeyTarabala.HOUSE_SIG_3
            4 -> StringKeyTarabala.HOUSE_SIG_4
            5 -> StringKeyTarabala.HOUSE_SIG_5
            6 -> StringKeyTarabala.HOUSE_SIG_6
            7 -> StringKeyTarabala.HOUSE_SIG_7
            8 -> StringKeyTarabala.HOUSE_SIG_8
            9 -> StringKeyTarabala.HOUSE_SIG_9
            10 -> StringKeyTarabala.HOUSE_SIG_10
            11 -> StringKeyTarabala.HOUSE_SIG_11
            12 -> StringKeyTarabala.HOUSE_SIG_12
            else -> StringKeyTarabala.HOUSE_SIG_UNKNOWN
        }
        return StringResources.get(key, language)
    }

    private fun createEmptyDailyResult(date: LocalDate): DailyStrengthResult {
        return DailyStrengthResult(
            date = date,
            tarabala = TarabalaResult(Nakshatra.ASHWINI, Tara.JANMA, 1, 50, emptyList()),
            chandrabala = ChandrabalaResult(ZodiacSign.ARIES, ZodiacSign.ARIES, 1, ChandrabalaStrength.NEUTRAL, "", emptyList()),
            combinedStrength = CombinedStrength.MIXED,
            overallScore = 50,
            favorableActivities = emptyList(),
            avoidActivities = emptyList(),
            generalAdvice = "Unable to calculate precise strength"
        )
    }

    /**
     * Generate plain text report
     */
    fun generateReport(analysis: TarabalaChandrabalaAnalysis, language: Language = Language.ENGLISH): String {
        return buildString {
            appendLine("═══════════════════════════════════════════════════════════")
            appendLine("           ${StringResources.get(StringKeyTarabala.REPORT_TITLE, language)}")
            appendLine("═══════════════════════════════════════════════════════════")
            appendLine()
            appendLine("${StringResources.get(StringKeyTarabala.REPORT_BIRTH_NAK, language)} ${analysis.birthNakshatra.displayName}")
            appendLine("${StringResources.get(StringKeyTarabala.REPORT_NATAL_MOON, language)} ${analysis.natalMoonSign.displayName}")
            appendLine("${StringResources.get(StringKeyTarabala.REPORT_ANALYSIS_DATE, language)} ${analysis.currentDate}")
            appendLine()
            appendLine("─────────────────────────────────────────────────────────")
            appendLine(StringResources.get(StringKeyTarabala.REPORT_TODAY_TARA, language))
            appendLine("─────────────────────────────────────────────────────────")
            appendLine("${StringResources.get(StringKeyTarabala.REPORT_TRANSIT_NAK, language)} ${analysis.currentTarabala.targetNakshatra.displayName}")
            appendLine("${StringResources.get(StringKeyTarabala.REPORT_TARA_LABEL, language)} ${analysis.currentTarabala.tara.sanskritName} (${analysis.currentTarabala.tara.englishMeaning})")
            appendLine("${StringResources.get(StringKeyTarabala.REPORT_CYCLE_LABEL, language)} ${analysis.currentTarabala.cycle} of 3")
            appendLine("${StringResources.get(StringKeyTarabala.REPORT_FAVORABLE_LABEL, language)} ${if (analysis.currentTarabala.isFavorable) StringResources.get(StringKeyAnalysis.UI_YES, language) else StringResources.get(StringKeyAnalysis.UI_NO, language)}")
            appendLine("${StringResources.get(StringKeyTarabala.REPORT_STRENGTH_LABEL, language)} ${analysis.currentTarabala.overallStrength}%")
            appendLine()
            appendLine(StringResources.get(StringKeyTarabala.REPORT_RECS_LABEL, language))
            analysis.currentTarabala.recommendations.forEach {
                appendLine("  • $it")
            }
            appendLine()
            appendLine("─────────────────────────────────────────────────────────")
            appendLine(StringResources.get(StringKeyTarabala.REPORT_TODAY_CHANDRA, language))
            appendLine("─────────────────────────────────────────────────────────")
            appendLine("${StringResources.get(StringKeyTarabala.REPORT_TRANSIT_MOON, language)} ${analysis.currentChandrabala.transitMoonSign.displayName}")
            appendLine("${StringResources.get(StringKeyTarabala.REPORT_HOUSE_FROM_MOON, language)} ${analysis.currentChandrabala.houseFromMoon}")
            appendLine("${StringResources.get(StringKeyTarabala.REPORT_STRENGTH_LABEL, language)} ${analysis.currentChandrabala.strength.getLocalizedName(language)}")
            appendLine("${StringResources.get(StringKeyTarabala.REPORT_SIGNIFICATIONS, language)} ${analysis.currentChandrabala.significations}")
            appendLine()
            appendLine(StringResources.get(StringKeyTarabala.REPORT_RECS_LABEL, language))
            analysis.currentChandrabala.recommendations.forEach {
                appendLine("  • $it")
            }
            appendLine()
            appendLine("─────────────────────────────────────────────────────────")
            appendLine(StringResources.get(StringKeyTarabala.REPORT_COMBINED_STRENGTH, language))
            appendLine("─────────────────────────────────────────────────────────")
            appendLine("${StringResources.get(StringKeyTarabala.REPORT_OVERALL_SCORE, language)} ${analysis.todayStrength.overallScore}%")
            appendLine("${StringResources.get(StringKeyTarabala.REPORT_FAVORABLE_LABEL, language)} ${analysis.todayStrength.combinedStrength.getLocalizedName(language)}")
            appendLine()
            appendLine(StringResources.get(StringKeyTarabala.REPORT_GENERAL_ADVICE, language))
            appendLine(analysis.todayStrength.generalAdvice)
            appendLine()
            if (analysis.todayStrength.favorableActivities.isNotEmpty()) {
                appendLine(StringResources.get(StringKeyTarabala.REPORT_FAV_ACT, language))
                analysis.todayStrength.favorableActivities.forEach {
                    appendLine("  ✓ ${it.activity} - ${it.reason}")
                }
                appendLine()
            }
            if (analysis.todayStrength.avoidActivities.isNotEmpty()) {
                appendLine(StringResources.get(StringKeyTarabala.REPORT_AVOID_ACT, language))
                analysis.todayStrength.avoidActivities.forEach {
                    appendLine("  ✗ ${it.activity} - ${it.reason}")
                }
            }
            appendLine()
            appendLine("─────────────────────────────────────────────────────────")
            appendLine(StringResources.get(StringKeyTarabala.REPORT_WEEKLY_FORECAST, language))
            appendLine("─────────────────────────────────────────────────────────")
            appendLine("${StringResources.get(StringKeyTarabala.REPORT_BEST_DAY, language)} ${analysis.weeklyForecast.bestDay.date.dayOfWeek} (${analysis.weeklyForecast.bestDay.overallScore}%)")
            appendLine("${StringResources.get(StringKeyTarabala.REPORT_AVOID_LABEL, language)} ${analysis.weeklyForecast.worstDay.date.dayOfWeek} (${analysis.weeklyForecast.worstDay.overallScore}%)")
            appendLine()
            appendLine("${StringResources.get(StringKeyTarabala.REPORT_DAILY_FORECAST, language)}")
            analysis.weeklyForecast.dailyStrengths.forEach { day ->
                val indicator = when {
                    day.overallScore >= 70 -> "★★★"
                    day.overallScore >= 50 -> "★★"
                    day.overallScore >= 30 -> "★"
                    else -> "○"
                }
                appendLine("  ${day.date.dayOfWeek.name.take(3)}: ${day.overallScore}% $indicator ${day.combinedStrength.getLocalizedName(language)}")
            }
            appendLine()
            appendLine(StringResources.get(StringKeyTarabala.REPORT_WEEKLY_ADVICE, language))
            appendLine(analysis.weeklyForecast.weeklyAdvice)
        }
    }
}


