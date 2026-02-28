package com.astro.vajra.ephemeris.muhurta

import com.astro.vajra.core.common.Language
import com.astro.vajra.core.model.Nakshatra
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.LocalDateTime

/**
 * PanchakaAnalyzer - Comprehensive Panchaka Dosha Analysis for Muhurta
 *
 * Panchaka (पञ्चक) literally means "group of five" and refers to inauspicious
 * combinations that occur when certain Nakshatras coincide with specific weekdays.
 * These five Nakshatras are in the domain of Vayu (air) element and are considered
 * unfavorable for specific activities.
 *
 * The Five Panchaka Nakshatras:
 * 1. Dhanishtha (last 2 padas)
 * 2. Shatabhisha (all 4 padas)
 * 3. Purva Bhadrapada (all 4 padas)
 * 4. Uttara Bhadrapada (all 4 padas)
 * 5. Revati (all 4 padas)
 *
 * Five Types of Panchaka Doshas:
 * 1. Mrityu Panchaka (Death) - Most severe, avoid travel/health matters
 * 2. Agni Panchaka (Fire) - Risk of fire-related issues
 * 3. Raja Panchaka (King/Authority) - Government troubles
 * 4. Chora Panchaka (Thief) - Risk of theft/losses
 * 5. Roga Panchaka (Disease) - Health problems
 *
 * Calculation Formula:
 * Panchaka Number = (Nakshatra Number + Weekday Number + Lunar Tithi) mod 9
 * Where:
 * - Nakshatra: Dhanishtha=23 to Revati=27
 * - Weekday: Sunday=1 to Saturday=7
 * - Tithi: 1-30
 *
 * Vedic References:
 * - Muhurta Chintamani
 * - Muhurta Martanda
 * - Brihat Samhita (Varahamihira)
 * - Narada Samhita
 *
 * @author AstroVajra
 * @since 2026.02
 */
object PanchakaAnalyzer {

    /**
     * The five Panchaka Nakshatras (all in Aquarius-Pisces region)
     * These are dominated by Vayu (air) tattva
     */
    private val PANCHAKA_NAKSHATRAS = listOf(
        Nakshatra.DHANISHTHA,       // 23rd nakshatra (last 2 padas in Aquarius)
        Nakshatra.SHATABHISHA,      // 24th nakshatra (fully in Aquarius)
        Nakshatra.PURVA_BHADRAPADA, // 25th nakshatra
        Nakshatra.UTTARA_BHADRAPADA,// 26th nakshatra
        Nakshatra.REVATI            // 27th nakshatra (fully in Pisces)
    )

    /**
     * Panchaka Dosha Type with severity and remedies
     */
    enum class PanchakaType(
        val key: com.astro.vajra.core.common.StringKeyMuhurta,
        val sanskritName: String,
        val severity: Int,  // 1-5, 5 being most severe
        val element: String,
        val description: String,
        val avoidActivities: List<String>,
        val remedialMeasures: List<String>
    ) {
        MRITYU_PANCHAKA(
            com.astro.vajra.core.common.StringKeyMuhurta.PANCHAKA_MRITYU,
            "Mrityu Panchaka",
            5,
            "Death/Mortality",
            "Most severe form of Panchaka. Indicates risk to life and severe health issues.",
            listOf(
                "Long-distance travel",
                "Starting major health treatments",
                "Risky adventures or sports",
                "Underground work",
                "Visiting sick relatives"
            ),
            listOf(
                "Perform Maha Mrityunjaya Japa (108 times)",
                "Donate black sesame seeds",
                "Light a lamp with sesame oil",
                "Chant Hanuman Chalisa",
                "Avoid the direction indicated by Panchaka"
            )
        ),
        AGNI_PANCHAKA(
            com.astro.vajra.core.common.StringKeyMuhurta.PANCHAKA_AGNI,
            "Agni Panchaka",
            4,
            "Fire",
            "Indicates risk of fire-related accidents and disputes causing heated arguments.",
            listOf(
                "Construction work",
                "Electrical installations",
                "Kitchen remodeling",
                "Starting new cooking ventures",
                "Fire-related ceremonies (without proper muhurta)"
            ),
            listOf(
                "Offer water to Sun god",
                "Perform Agni Shanti puja",
                "Donate red items or copper",
                "Avoid wearing red clothes",
                "Keep fire safety measures handy"
            )
        ),
        RAJA_PANCHAKA(
            com.astro.vajra.core.common.StringKeyMuhurta.PANCHAKA_RAJA,
            "Raja Panchaka",
            3,
            "Authority",
            "Indicates troubles with government, authorities, or legal matters.",
            listOf(
                "Filing legal cases",
                "Government office visits",
                "Tax-related submissions",
                "Meeting with officials",
                "Starting political activities"
            ),
            listOf(
                "Donate to government servants",
                "Perform Surya puja",
                "Chant Aditya Hridayam",
                "Wear yellow or saffron",
                "Delay official work if possible"
            )
        ),
        CHORA_PANCHAKA(
            com.astro.vajra.core.common.StringKeyMuhurta.PANCHAKA_CHORA,
            "Chora Panchaka",
            4,
            "Theft/Loss",
            "Indicates risk of theft, robbery, or financial losses.",
            listOf(
                "Carrying large amounts of cash",
                "Traveling with valuables",
                "Major financial transactions",
                "Leaving home unattended",
                "Starting business deals"
            ),
            listOf(
                "Donate dark-colored items",
                "Perform Kala Bhairava puja",
                "Secure valuables properly",
                "Chant Hanuman mantras",
                "Be extra vigilant about security"
            )
        ),
        ROGA_PANCHAKA(
            com.astro.vajra.core.common.StringKeyMuhurta.PANCHAKA_ROGA,
            "Roga Panchaka",
            3,
            "Disease/Health",
            "Indicates risk of diseases, especially those hard to diagnose or treat.",
            listOf(
                "Starting long-term medications",
                "Elective surgeries",
                "Changing doctors",
                "Starting health regimens",
                "Visiting hospitals (if avoidable)"
            ),
            listOf(
                "Perform Dhanvantari puja",
                "Chant Maha Mrityunjaya mantra",
                "Donate medicines",
                "Maintain strict hygiene",
                "Eat sattvic food"
            )
        ),
        NO_PANCHAKA(
            com.astro.vajra.core.common.StringKeyMuhurta.PANCHAKA_NONE,
            "Shunya Panchaka",
            0,
            "None",
            "No Panchaka dosha is present. The time is favorable.",
            emptyList(),
            emptyList()
        );

        val displayName: String get() = key.en

        /**
         * Get severity description
         */
        fun getSeverityDescription(language: Language = Language.ENGLISH): String = when (severity) {
            5 -> com.astro.vajra.core.common.StringResources.get(com.astro.vajra.core.common.StringKeyMuhurta.SEVERITY_EXTREME, language)
            4 -> com.astro.vajra.core.common.StringResources.get(com.astro.vajra.core.common.StringKeyMuhurta.SEVERITY_SEVERE, language)
            3 -> com.astro.vajra.core.common.StringResources.get(com.astro.vajra.core.common.StringKeyMuhurta.SEVERITY_MODERATE, language)
            2 -> com.astro.vajra.core.common.StringResources.get(com.astro.vajra.core.common.StringKeyMuhurta.SEVERITY_MILD, language)
            1 -> com.astro.vajra.core.common.StringResources.get(com.astro.vajra.core.common.StringKeyMuhurta.SEVERITY_VERY_MILD, language)
            else -> com.astro.vajra.core.common.StringResources.get(com.astro.vajra.core.common.StringKeyMuhurta.SEVERITY_NONE, language)
        }
    }

    /**
     * Direction associated with Panchaka (for directional avoidance)
     */
    enum class PanchakaDirection(val key: com.astro.vajra.core.common.StringKeyMuhurta, val degrees: IntRange) {
        EAST(com.astro.vajra.core.common.StringKeyMuhurta.DIR_EAST, 67..113),
        SOUTH(com.astro.vajra.core.common.StringKeyMuhurta.DIR_SOUTH, 157..203),
        WEST(com.astro.vajra.core.common.StringKeyMuhurta.DIR_WEST, 247..293),
        NORTH(com.astro.vajra.core.common.StringKeyMuhurta.DIR_NORTH, 337..360),
        NORTH_CONT(com.astro.vajra.core.common.StringKeyMuhurta.DIR_NORTH, 0..23);

        val displayName: String get() = key.en

        companion object {
            fun fromNakshatra(nakshatra: Nakshatra): PanchakaDirection {
                return when (nakshatra) {
                    Nakshatra.DHANISHTHA -> EAST
                    Nakshatra.SHATABHISHA -> SOUTH
                    Nakshatra.PURVA_BHADRAPADA -> WEST
                    Nakshatra.UTTARA_BHADRAPADA -> NORTH
                    Nakshatra.REVATI -> EAST
                    else -> EAST
                }
            }
        }
    }

    /**
     * Complete Panchaka Analysis Result
     */
    data class PanchakaAnalysis(
        val dateTime: LocalDateTime,
        val nakshatra: Nakshatra,
        val nakshatraPada: Int,
        val weekday: Vara,
        val tithiNumber: Int,
        val isPanchakaActive: Boolean,
        val panchakaType: PanchakaType,
        val panchakaNumber: Int,
        val directionToAvoid: PanchakaDirection?,
        val nakshatraInPanchaka: Boolean,
        val severity: Int,
        val avoidActivities: List<String>,
        val remedies: List<String>,
        val interpretation: String,
        val detailedAnalysis: String
    ) {
        val isSevere: Boolean get() = severity >= 4
        val isModerate: Boolean get() = severity in 2..3
        val isMild: Boolean get() = severity == 1
    }

    /**
     * Daily Panchaka Summary
     */
    data class DailyPanchakaSummary(
        val date: LocalDate,
        val panchakaSegments: List<PanchakaSegment>,
        val overallRisk: PanchakaRiskLevel,
        val totalPanchakaHours: Double,
        val safePeriods: List<TimePeriod>,
        val recommendations: List<String>
    )

    /**
     * Time segment with Panchaka status
     */
    data class PanchakaSegment(
        val startTime: LocalDateTime,
        val endTime: LocalDateTime,
        val nakshatra: Nakshatra,
        val panchakaType: PanchakaType,
        val severity: Int
    )

    /**
     * Overall risk level for the day
     */
    enum class PanchakaRiskLevel(val key: com.astro.vajra.core.common.StringKeyMuhurta, val score: Int) {
        SAFE(com.astro.vajra.core.common.StringKeyMuhurta.SAFE, 0),
        LOW(com.astro.vajra.core.common.StringKeyMuhurta.RISK_LOW, 1),
        MODERATE(com.astro.vajra.core.common.StringKeyMuhurta.RISK_MODERATE, 2),
        HIGH(com.astro.vajra.core.common.StringKeyMuhurta.RISK_HIGH, 3),
        SEVERE(com.astro.vajra.core.common.StringKeyMuhurta.RISK_SEVERE, 4);

        val displayName: String get() = key.en

        companion object {
            fun fromSeverity(severity: Int): PanchakaRiskLevel = when {
                severity == 0 -> SAFE
                severity == 1 -> LOW
                severity in 2..3 -> MODERATE
                severity == 4 -> HIGH
                else -> SEVERE
            }
        }
    }

    /**
     * Analyze Panchaka for a specific moment
     *
     * @param nakshatra Current Moon nakshatra
     * @param nakshatraPada Current pada (1-4)
     * @param weekday Current weekday (Vara)
     * @param tithiNumber Lunar day number (1-30)
     * @param dateTime The datetime being analyzed
     * @return Complete PanchakaAnalysis
     */
    fun analyzePanchaka(
        nakshatra: Nakshatra,
        nakshatraPada: Int,
        weekday: Vara,
        tithiNumber: Int,
        dateTime: LocalDateTime,
        language: Language = Language.ENGLISH
    ): PanchakaAnalysis {
        // Check if nakshatra is in Panchaka group
        val nakshatraInPanchaka = isNakshatraInPanchaka(nakshatra, nakshatraPada)

        if (!nakshatraInPanchaka) {
            return PanchakaAnalysis(
                dateTime = dateTime,
                nakshatra = nakshatra,
                nakshatraPada = nakshatraPada,
                weekday = weekday,
                tithiNumber = tithiNumber,
                isPanchakaActive = false,
                panchakaType = PanchakaType.NO_PANCHAKA,
                panchakaNumber = 0,
                directionToAvoid = null,
                nakshatraInPanchaka = false,
                severity = 0,
                avoidActivities = emptyList(),
                remedies = emptyList(),
                interpretation = "No Panchaka dosha is present. The time is auspicious for most activities.",
                detailedAnalysis = buildNoopanchakaAnalysis(nakshatra, weekday, language)
            )
        }

        // Calculate Panchaka number using classical formula
        val nakshatraNumber = getNakshatraNumber(nakshatra)
        val weekdayNumber = getWeekdayNumber(weekday)
        val panchakaSum = nakshatraNumber + weekdayNumber + tithiNumber
        val panchakaNumber = panchakaSum % 9

        // Determine Panchaka type from number
        val panchakaType = getPanchakaTypeFromNumber(panchakaNumber)

        // Get direction to avoid
        val directionToAvoid = PanchakaDirection.fromNakshatra(nakshatra)

        // Build interpretation
        val interpretation = buildPanchakaInterpretation(panchakaType, nakshatra, weekday, tithiNumber, language)
        val detailedAnalysis = buildDetailedPanchakaAnalysis(
            panchakaType, nakshatra, nakshatraPada, weekday, tithiNumber, directionToAvoid, language
        )

        return PanchakaAnalysis(
            dateTime = dateTime,
            nakshatra = nakshatra,
            nakshatraPada = nakshatraPada,
            weekday = weekday,
            tithiNumber = tithiNumber,
            isPanchakaActive = panchakaType != PanchakaType.NO_PANCHAKA,
            panchakaType = panchakaType,
            panchakaNumber = panchakaNumber,
            directionToAvoid = directionToAvoid,
            nakshatraInPanchaka = true,
            severity = panchakaType.severity,
            avoidActivities = panchakaType.avoidActivities,
            remedies = panchakaType.remedialMeasures,
            interpretation = interpretation,
            detailedAnalysis = detailedAnalysis
        )
    }

    /**
     * Simple overload using MuhurtaDetails
     */
    fun analyzePanchaka(muhurta: MuhurtaDetails): PanchakaAnalysis {
        return analyzePanchaka(
            nakshatra = muhurta.nakshatra.nakshatra,
            nakshatraPada = muhurta.nakshatra.pada,
            weekday = muhurta.vara,
            tithiNumber = muhurta.tithi.number,
            dateTime = muhurta.dateTime
        )
    }

    /**
     * Check if Moon is in a Panchaka nakshatra
     *
     * Note: For Dhanishtha, only the last 2 padas (3 & 4) are in Panchaka
     * as the first 2 padas fall in Capricorn, not Aquarius.
     */
    fun isNakshatraInPanchaka(nakshatra: Nakshatra, pada: Int = 1): Boolean {
        return when (nakshatra) {
            Nakshatra.DHANISHTHA -> pada >= 3  // Only padas 3 & 4 (Aquarius portion)
            Nakshatra.SHATABHISHA,
            Nakshatra.PURVA_BHADRAPADA,
            Nakshatra.UTTARA_BHADRAPADA,
            Nakshatra.REVATI -> true
            else -> false
        }
    }

    /**
     * Get Panchaka type from calculated number
     *
     * Formula result mapping (per Muhurta Chintamani):
     * 1, 2 -> Mrityu Panchaka
     * 3 -> Agni Panchaka
     * 4, 5 -> Raja Panchaka
     * 6 -> Chora Panchaka
     * 7, 8 -> Roga Panchaka
     * 0 -> No specific Panchaka
     */
    private fun getPanchakaTypeFromNumber(number: Int): PanchakaType {
        return when (number) {
            1, 2 -> PanchakaType.MRITYU_PANCHAKA
            3 -> PanchakaType.AGNI_PANCHAKA
            4, 5 -> PanchakaType.RAJA_PANCHAKA
            6 -> PanchakaType.CHORA_PANCHAKA
            7, 8 -> PanchakaType.ROGA_PANCHAKA
            else -> PanchakaType.NO_PANCHAKA // 0 - no specific Panchaka dosha
        }
    }

    /**
     * Get nakshatra number (1-27)
     */
    private fun getNakshatraNumber(nakshatra: Nakshatra): Int {
        return nakshatra.ordinal + 1
    }

    /**
     * Get weekday number (1-7, Sunday=1)
     */
    private fun getWeekdayNumber(vara: Vara): Int {
        return when (vara) {
            Vara.SUNDAY -> 1
            Vara.MONDAY -> 2
            Vara.TUESDAY -> 3
            Vara.WEDNESDAY -> 4
            Vara.THURSDAY -> 5
            Vara.FRIDAY -> 6
            Vara.SATURDAY -> 7
        }
    }

    /**
     * Calculate intensity multiplier based on pada and other factors
     */
    fun calculateIntensityMultiplier(
        nakshatra: Nakshatra,
        pada: Int,
        tithiNumber: Int
    ): Double {
        var multiplier = 1.0

        // Pada-based intensity (center padas are stronger)
        multiplier *= when (pada) {
            1, 4 -> 0.8
            2, 3 -> 1.2
            else -> 1.0
        }

        // Certain tithis intensify Panchaka
        val intensifyingTithis = listOf(4, 9, 14, 8, 6, 12) // Chaturthi, Navami, Chaturdashi, Ashtami, Shashti, Dwadashi
        if (tithiNumber in intensifyingTithis) {
            multiplier *= 1.3
        }

        // Amavasya and Purnima
        if (tithiNumber == 15 || tithiNumber == 30) {
            multiplier *= 1.2
        }

        return multiplier.coerceIn(0.5, 2.0)
    }

    /**
     * Check if specific activity is safe during Panchaka
     */
    fun isActivitySafe(
        activity: ActivityType,
        panchakaType: PanchakaType
    ): Pair<Boolean, String> {
        if (panchakaType == PanchakaType.NO_PANCHAKA) {
            return Pair(true, "No Panchaka dosha - Activity is safe")
        }

        // Activity-specific Panchaka concerns
        val concerns = when (panchakaType) {
            PanchakaType.MRITYU_PANCHAKA -> listOf(
                ActivityType.TRAVEL,
                ActivityType.MEDICAL
            )
            PanchakaType.AGNI_PANCHAKA -> listOf(
                ActivityType.GRIHA_PRAVESHA,
                ActivityType.PROPERTY
            )
            PanchakaType.RAJA_PANCHAKA -> listOf(
                ActivityType.BUSINESS
            )
            PanchakaType.CHORA_PANCHAKA -> listOf(
                ActivityType.BUSINESS,
                ActivityType.VEHICLE,
                ActivityType.TRAVEL
            )
            PanchakaType.ROGA_PANCHAKA -> listOf(
                ActivityType.MEDICAL
            )
            PanchakaType.NO_PANCHAKA -> emptyList()
        }

        val isSafe = activity !in concerns
        val reason = if (isSafe) {
            "This activity is not specifically affected by ${panchakaType.displayName}"
        } else {
            "${panchakaType.displayName} specifically affects ${activity.getLocalizedName(Language.ENGLISH)}. " +
                    "Consider postponing or perform recommended remedies."
        }

        return Pair(isSafe, reason)
    }

    /**
     * Get remedial measures with timing
     */
    fun getRemedialMeasures(
        panchakaType: PanchakaType,
        urgency: Boolean = false
    ): List<RemedialMeasure> {
        if (panchakaType == PanchakaType.NO_PANCHAKA) return emptyList()

        val baseMeasures = panchakaType.remedialMeasures.map { remedy ->
            RemedialMeasure(
                action = remedy,
                timing = if (urgency) "Perform immediately before activity" else "Perform in the morning",
                effectiveness = 70,
                difficulty = RemedialDifficulty.MODERATE
            )
        }

        // Add universal remedies
        val universalRemedies = listOf(
            RemedialMeasure(
                action = "Chant 'Om Namah Shivaya' 108 times",
                timing = "Before starting any activity",
                effectiveness = 60,
                difficulty = RemedialDifficulty.EASY
            ),
            RemedialMeasure(
                action = "Offer prayers to Ishta Devata (personal deity)",
                timing = "Morning or before activity",
                effectiveness = 75,
                difficulty = RemedialDifficulty.EASY
            ),
            RemedialMeasure(
                action = "Donate food to the needy",
                timing = "Before noon",
                effectiveness = 80,
                difficulty = RemedialDifficulty.MODERATE
            )
        )

        return baseMeasures + universalRemedies
    }

    /**
     * Remedial measure data class
     */
    data class RemedialMeasure(
        val action: String,
        val timing: String,
        val effectiveness: Int, // 0-100
        val difficulty: RemedialDifficulty
    )

    enum class RemedialDifficulty {
        EASY, MODERATE, DIFFICULT
    }

    // ============================================
    // INTERPRETATION BUILDERS
    // ============================================

    private fun buildPanchakaInterpretation(
        panchakaType: PanchakaType,
        nakshatra: Nakshatra,
        weekday: Vara,
        tithiNumber: Int,
        language: Language
    ): String {
        return buildString {
            append(com.astro.vajra.core.common.StringResources.get(com.astro.vajra.core.common.StringKeyMuhurta.PANCHAKA_ACTIVE, language, com.astro.vajra.core.common.StringResources.get(panchakaType.key, language)))
            append(" (${panchakaType.sanskritName}) is active. ")
            append("Moon is in ${nakshatra.displayName} on ${weekday.getLocalizedName(Language.ENGLISH)}, Tithi $tithiNumber. ")
            append(panchakaType.description)
            append(" ${com.astro.vajra.core.common.StringResources.get(com.astro.vajra.core.common.StringKeyMuhurta.PANCHAKA_SEVERITY, language, panchakaType.getSeverityDescription(language))}.")
        }
    }

    private fun buildDetailedPanchakaAnalysis(
        panchakaType: PanchakaType,
        nakshatra: Nakshatra,
        pada: Int,
        weekday: Vara,
        tithiNumber: Int,
        direction: PanchakaDirection,
        language: Language
    ): String {
        return buildString {
            appendLine("═══════════════════════════════════════")
            appendLine(com.astro.vajra.core.common.StringResources.get(com.astro.vajra.core.common.StringKeyMuhurta.PANCHAKA_TITLE, language).uppercase() + " DOSHA ANALYSIS")
            appendLine("═══════════════════════════════════════")
            appendLine()
            appendLine("Type: ${com.astro.vajra.core.common.StringResources.get(panchakaType.key, language)} (${panchakaType.sanskritName})")
            appendLine("${com.astro.vajra.core.common.StringResources.get(com.astro.vajra.core.common.StringKeyMuhurta.PANCHAKA_SEVERITY, language, panchakaType.severity.toString())}/5 - ${panchakaType.getSeverityDescription(language)}")
            appendLine()
            appendLine("Contributing Factors:")
            appendLine("───────────────────────────────────────")
            appendLine("• Nakshatra: ${nakshatra.displayName} (Pada $pada)")
            appendLine("• Weekday: ${weekday.getLocalizedName(Language.ENGLISH)}")
            appendLine("• Tithi: $tithiNumber")
            appendLine("• Direction to Avoid: ${com.astro.vajra.core.common.StringResources.get(direction.key, language)}")
            appendLine()
            appendLine("Element: ${panchakaType.element}")
            appendLine()

            if (panchakaType.avoidActivities.isNotEmpty()) {
                appendLine("Activities to Avoid:")
                appendLine("───────────────────────────────────────")
                panchakaType.avoidActivities.forEach { activity ->
                    appendLine("✗ $activity")
                }
                appendLine()
            }

            if (panchakaType.remedialMeasures.isNotEmpty()) {
                appendLine("Recommended Remedies:")
                appendLine("───────────────────────────────────────")
                panchakaType.remedialMeasures.forEach { remedy ->
                    appendLine("✓ $remedy")
                }
            }
        }
    }

    private fun buildNoopanchakaAnalysis(nakshatra: Nakshatra, weekday: Vara, language: Language): String {
        return buildString {
            appendLine("═══════════════════════════════════════")
            appendLine(com.astro.vajra.core.common.StringResources.get(com.astro.vajra.core.common.StringKeyMuhurta.PANCHAKA_TITLE, language).uppercase() + " STATUS: CLEAR")
            appendLine("═══════════════════════════════════════")
            appendLine()
            appendLine("Moon Nakshatra: ${nakshatra.displayName}")
            appendLine("Weekday: ${weekday.getLocalizedName(Language.ENGLISH)}")
            appendLine()
            appendLine("The current nakshatra (${nakshatra.displayName}) is not part of")
            appendLine("the Panchaka group. No Panchaka dosha restrictions apply.")
            appendLine()
            appendLine(com.astro.vajra.core.common.StringResources.get(com.astro.vajra.core.common.StringKeyMuhurta.PANCHAKA_FAVORABLE, language))
        }
    }

    /**
     * Get summary text for UI display
     */
    fun getSummaryText(analysis: PanchakaAnalysis, language: Language = Language.ENGLISH): String {
        return if (analysis.isPanchakaActive) {
            "${com.astro.vajra.core.common.StringResources.get(analysis.panchakaType.key, language)} active (Severity: ${analysis.severity}/5). " +
                    "Avoid: ${analysis.avoidActivities.take(2).joinToString(", ")}..."
        } else {
            com.astro.vajra.core.common.StringResources.get(com.astro.vajra.core.common.StringKeyMuhurta.PANCHAKA_FAVORABLE, language)
        }
    }

    /**
     * Extension functions for integration with MuhurtaCalculator
     */
    fun MuhurtaDetails.hasPanchaka(): Boolean {
        return PanchakaAnalyzer.isNakshatraInPanchaka(this.nakshatra.nakshatra, this.nakshatra.pada)
    }

    fun MuhurtaDetails.getPanchakaAnalysis(): PanchakaAnalysis {
        return PanchakaAnalyzer.analyzePanchaka(this)
    }
}
