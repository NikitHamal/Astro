package com.astro.storm.ephemeris

import com.astro.storm.core.model.Nakshatra
import com.astro.storm.core.model.Planet
import com.astro.storm.core.model.VedicChart
import com.astro.storm.core.model.ZodiacSign
import java.time.LocalDate
import java.time.temporal.ChronoUnit
import com.astro.storm.core.common.Language
import com.astro.storm.core.common.StringKeyDosha
import com.astro.storm.core.common.StringResources

/**
 * Kalachakra Dasha Calculator - Production-grade Implementation
 *
 * Kalachakra Dasha is a highly respected but complex Nakshatra-based dasha system
 * from Brihat Parashara Hora Shastra (BPHS, Chapter 45). It is particularly useful
 * for timing health events, spiritual transformations, and life's major transitions.
 *
 * ## Key Principles (from BPHS)
 *
 * ### 1. Savya (Clockwise) and Apsavya (Anti-clockwise) Groups
 * The 27 Nakshatras are divided into groups that determine the direction of counting:
 * - **Savya (Direct/Clockwise)**: Ashwini, Bharani, Rohini, Mrigashira, Pushya, Ashlesha,
 *   Purva Phalguni, Uttara Phalguni, Hasta, Chitra, Swati, Anuradha, Purva Ashadha,
 *   Uttara Ashadha (1st half), Uttara Bhadrapada, Revati
 * - **Apsavya (Retrograde/Anti-clockwise)**: Krittika, Ardra, Punarvasu, Magha,
 *   Uttara Ashadha (2nd half), Shravana, Dhanishtha, Shatabhisha, Purva Bhadrapada
 *
 * ### 2. Deha (Body) and Jeeva (Soul) Rashis
 * Each Nakshatra pada has associated Deha and Jeeva rashis:
 * - **Deha Rashi**: Represents physical body, health, material matters
 * - **Jeeva Rashi**: Represents soul, consciousness, spiritual matters
 * Transit of benefics over Jeeva and malefics avoiding Deha is favorable.
 *
 * ### 3. Dasha Sequence and Periods
 * The dasha periods follow the Kalachakra pattern:
 * - Each sign rules for a specific period (7, 16, 9, 21, 5, 9, 16, 7, 10 years)
 * - The sequence follows the Navamsa pattern within each pada
 * - Total cycle varies based on the nakshatra group
 *
 * ### 4. Pada-based Calculation
 * The starting point is determined by Moon's Nakshatra and Pada at birth.
 * Each of the 4 padas has a specific starting rashi and direction.
 *
 * ## References
 * - BPHS Chapter 45: Kalachakra Dasha Adhyaya
 * - Jataka Parijata on alternative dasha systems
 * - Uttara Kalamrita on Kalachakra application
 * - Dr. K.S. Charak's works on Dasha systems
 *
 * @author AstroStorm
 */
object KalachakraDashaCalculator {

    private val MATH_CONTEXT = DashaUtils.MATH_CONTEXT
    private val DAYS_PER_YEAR = DashaUtils.DAYS_PER_YEAR

    // ============================================
    // KALACHAKRA DASHA CONSTANTS (from BPHS)
    // ============================================

    /**
     * Dasha periods for each sign in Kalachakra (in years)
     * This follows the specific pattern laid out in BPHS Chapter 45
     *
     * The pattern is: Aries(7), Taurus(16), Gemini(9), Cancer(21),
     * Leo(5), Virgo(9), Libra(16), Scorpio(7), Sagittarius(10)
     *
     * Note: Capricorn, Aquarius, Pisces follow the reverse of first 9 signs
     * in Apsavya movement
     */
    private val KALACHAKRA_PERIODS = mapOf(
        ZodiacSign.ARIES to 7,
        ZodiacSign.TAURUS to 16,
        ZodiacSign.GEMINI to 9,
        ZodiacSign.CANCER to 21,
        ZodiacSign.LEO to 5,
        ZodiacSign.VIRGO to 9,
        ZodiacSign.LIBRA to 16,
        ZodiacSign.SCORPIO to 7,
        ZodiacSign.SAGITTARIUS to 10,
        ZodiacSign.CAPRICORN to 10, // Mirror of Sagittarius
        ZodiacSign.AQUARIUS to 7,   // Mirror of Scorpio
        ZodiacSign.PISCES to 16     // Mirror of Libra
    )

    /**
     * Total cycle duration based on the 9-sign sequence (100 years)
     */
    private const val TOTAL_SAVYA_CYCLE_YEARS = 100 // 7+16+9+21+5+9+16+7+10
    private const val TOTAL_APSAVYA_CYCLE_YEARS = 100

    /**
     * Nakshatra group assignments - Savya (Direct) nakshatras
     * These follow clockwise progression through signs
     */
    private val SAVYA_NAKSHATRAS = setOf(
        Nakshatra.ASHWINI,
        Nakshatra.BHARANI,
        Nakshatra.ROHINI,
        Nakshatra.MRIGASHIRA,
        Nakshatra.PUSHYA,
        Nakshatra.ASHLESHA,
        Nakshatra.PURVA_PHALGUNI,
        Nakshatra.UTTARA_PHALGUNI,
        Nakshatra.HASTA,
        Nakshatra.CHITRA,
        Nakshatra.SWATI,
        Nakshatra.ANURADHA,
        Nakshatra.PURVA_ASHADHA,
        Nakshatra.UTTARA_BHADRAPADA,
        Nakshatra.REVATI
    )

    /**
     * Nakshatra group assignments - Apsavya (Retrograde) nakshatras
     * These follow anti-clockwise progression through signs
     */
    private val APSAVYA_NAKSHATRAS = setOf(
        Nakshatra.KRITTIKA,
        Nakshatra.ARDRA,
        Nakshatra.PUNARVASU,
        Nakshatra.MAGHA,
        Nakshatra.SHRAVANA,
        Nakshatra.DHANISHTHA,
        Nakshatra.SHATABHISHA,
        Nakshatra.PURVA_BHADRAPADA
    )

    /**
     * Special handling for Uttara Ashadha (split between Savya and Apsavya)
     * First 2 padas are Savya, last 2 padas are Apsavya
     */
    private val UTTARA_ASHADHA_SAVYA_PADAS = setOf(1, 2)
    private val UTTARA_ASHADHA_APSAVYA_PADAS = setOf(3, 4)

    /**
     * Starting signs for each Nakshatra Pada in Savya group
     * Based on BPHS Kalachakra mapping
     */
    private val SAVYA_PADA_STARTING_SIGNS = mapOf(
        // Group 1: Fire nakshatras starting from Aries
        "FIRE_1" to ZodiacSign.ARIES,
        "FIRE_2" to ZodiacSign.CANCER,
        "FIRE_3" to ZodiacSign.LIBRA,
        "FIRE_4" to ZodiacSign.CAPRICORN,
        // Group 2: Earth nakshatras starting from Taurus
        "EARTH_1" to ZodiacSign.TAURUS,
        "EARTH_2" to ZodiacSign.LEO,
        "EARTH_3" to ZodiacSign.SCORPIO,
        "EARTH_4" to ZodiacSign.AQUARIUS,
        // Group 3: Air nakshatras starting from Gemini
        "AIR_1" to ZodiacSign.GEMINI,
        "AIR_2" to ZodiacSign.VIRGO,
        "AIR_3" to ZodiacSign.SAGITTARIUS,
        "AIR_4" to ZodiacSign.PISCES
    )

    /**
     * Deha-Jeeva pairs for each sign
     * These are crucial for health and spiritual predictions
     */
    private val DEHA_JEEVA_PAIRS = mapOf(
        ZodiacSign.ARIES to Pair(ZodiacSign.ARIES, ZodiacSign.SAGITTARIUS),
        ZodiacSign.TAURUS to Pair(ZodiacSign.TAURUS, ZodiacSign.CAPRICORN),
        ZodiacSign.GEMINI to Pair(ZodiacSign.GEMINI, ZodiacSign.AQUARIUS),
        ZodiacSign.CANCER to Pair(ZodiacSign.CANCER, ZodiacSign.PISCES),
        ZodiacSign.LEO to Pair(ZodiacSign.LEO, ZodiacSign.ARIES),
        ZodiacSign.VIRGO to Pair(ZodiacSign.VIRGO, ZodiacSign.TAURUS),
        ZodiacSign.LIBRA to Pair(ZodiacSign.LIBRA, ZodiacSign.GEMINI),
        ZodiacSign.SCORPIO to Pair(ZodiacSign.SCORPIO, ZodiacSign.CANCER),
        ZodiacSign.SAGITTARIUS to Pair(ZodiacSign.SAGITTARIUS, ZodiacSign.LEO),
        ZodiacSign.CAPRICORN to Pair(ZodiacSign.CAPRICORN, ZodiacSign.VIRGO),
        ZodiacSign.AQUARIUS to Pair(ZodiacSign.AQUARIUS, ZodiacSign.LIBRA),
        ZodiacSign.PISCES to Pair(ZodiacSign.PISCES, ZodiacSign.SCORPIO)
    )

    // ============================================
    // DATA CLASSES
    // ============================================

    /**
     * Complete Kalachakra Dasha analysis result
     */
    data class KalachakraDashaResult(
        val birthNakshatra: Nakshatra,
        val birthNakshatraPada: Int,
        val nakshatraGroup: NakshatraGroup,
        val startingSign: ZodiacSign,
        val dehaRashi: ZodiacSign,
        val jeevaRashi: ZodiacSign,
        val mahadashas: List<KalachakraMahadasha>,
        val currentMahadasha: KalachakraMahadasha?,
        val currentAntardasha: KalachakraAntardasha?,
        val dehaJeevaAnalysis: DehaJeevaAnalysis,
        val interpretation: KalachakraInterpretation,
        val applicabilityScore: Int // 0-100: how applicable is this system
    )

    /**
     * Nakshatra group type
     */
    enum class NakshatraGroup(val displayName: String, val description: String) {
        SAVYA("Savya (Direct)", "Clockwise progression through signs - generally smoother life flow"),
        APSAVYA("Apsavya (Retrograde)", "Anti-clockwise progression - more karmic intensity and transformation");

        fun getLocalizedName(language: Language): String {
            return when (this) {
                SAVYA -> StringResources.get(StringKeyDashaInterpretationsPart1.KALACHAKRA_GROUP_SAVYA, language)
                APSAVYA -> StringResources.get(StringKeyDashaInterpretationsPart1.KALACHAKRA_GROUP_APSAVYA, language)
            }
        }

        fun getLocalizedDescription(language: Language): String {
            return when (this) {
                SAVYA -> StringResources.get(StringKeyDashaInterpretationsPart1.KALACHAKRA_DESC_SAVYA, language)
                APSAVYA -> StringResources.get(StringKeyDashaInterpretationsPart1.KALACHAKRA_DESC_APSAVYA, language)
            }
        }
    }

    /**
     * Kalachakra Mahadasha period
     */
    data class KalachakraMahadasha(
        val sign: ZodiacSign,
        val startDate: LocalDate,
        val endDate: LocalDate,
        val durationYears: Int,
        val signLord: Planet,
        val dehaRashi: ZodiacSign,
        val jeevaRashi: ZodiacSign,
        val antardashas: List<KalachakraAntardasha>,
        val isParamaAyushSign: Boolean, // Special longevity indicator
        val healthIndicator: HealthIndicator,
        val interpretation: MahadashaInterpretation
    ) {
        val durationDays: Long
            get() = ChronoUnit.DAYS.between(startDate, endDate)

        fun isActiveOn(date: LocalDate): Boolean {
            return !date.isBefore(startDate) && !date.isAfter(endDate)
        }

        val isActive: Boolean
            get() = isActiveOn(LocalDate.now())

        fun getAntardashaOn(date: LocalDate): KalachakraAntardasha? {
            return antardashas.find { it.isActiveOn(date) }
        }

        fun getProgressPercent(asOf: LocalDate = LocalDate.now()): Double {
            if (durationDays <= 0) return 0.0
            val elapsed = ChronoUnit.DAYS.between(startDate, asOf.coerceIn(startDate, endDate))
            return ((elapsed.toDouble() / durationDays) * 100).coerceIn(0.0, 100.0)
        }

        fun getRemainingDays(asOf: LocalDate = LocalDate.now()): Long {
            if (asOf.isAfter(endDate)) return 0
            if (asOf.isBefore(startDate)) return durationDays
            return ChronoUnit.DAYS.between(asOf, endDate)
        }
    }

    /**
     * Kalachakra Antardasha (sub-period)
     */
    data class KalachakraAntardasha(
        val sign: ZodiacSign,
        val mahadashaSign: ZodiacSign,
        val startDate: LocalDate,
        val endDate: LocalDate,
        val durationDays: Long,
        val signLord: Planet,
        val isDehaSign: Boolean,
        val isJeevaSign: Boolean,
        val interpretation: String
    ) {
        val durationMonths: Double
            get() = durationDays / 30.4375

        fun isActiveOn(date: LocalDate): Boolean {
            return !date.isBefore(startDate) && !date.isAfter(endDate)
        }

        val isActive: Boolean
            get() = isActiveOn(LocalDate.now())

        fun getProgressPercent(asOf: LocalDate = LocalDate.now()): Double {
            if (durationDays <= 0) return 0.0
            val elapsed = ChronoUnit.DAYS.between(startDate, asOf.coerceIn(startDate, endDate))
            return ((elapsed.toDouble() / durationDays) * 100).coerceIn(0.0, 100.0)
        }
    }

    /**
     * Health indicator for the dasha period
     */
    enum class HealthIndicator(val displayName: String, val description: String, val score: Int) {
        EXCELLENT("Excellent", "Very favorable for health and vitality", 5),
        GOOD("Good", "Generally supportive of health", 4),
        MODERATE("Moderate", "Mixed health indications", 3),
        CHALLENGING("Challenging", "Need to take care of health", 2),
        CRITICAL("Critical", "Extra caution needed - follow remedies", 1);

        fun getLocalizedName(language: Language): String {
             return when (this) {
                EXCELLENT -> StringResources.get(StringKeyDashaInterpretationsPart1.KALACHAKRA_HEALTH_EXCELLENT, language)
                GOOD -> StringResources.get(StringKeyDashaInterpretationsPart1.KALACHAKRA_HEALTH_GOOD, language)
                MODERATE -> StringResources.get(StringKeyDashaInterpretationsPart1.KALACHAKRA_HEALTH_MODERATE, language)
                CHALLENGING -> StringResources.get(StringKeyDashaInterpretationsPart1.KALACHAKRA_HEALTH_CHALLENGING, language)
                CRITICAL -> StringResources.get(StringKeyDashaInterpretationsPart1.KALACHAKRA_HEALTH_CRITICAL, language)
            }
        }

        fun getLocalizedDescription(language: Language): String {
            return when (this) {
                EXCELLENT -> StringResources.get(StringKeyDashaInterpretationsPart1.KALACHAKRA_HEALTH_DESC_EXCELLENT, language)
                GOOD -> StringResources.get(StringKeyDashaInterpretationsPart1.KALACHAKRA_HEALTH_DESC_GOOD, language)
                MODERATE -> StringResources.get(StringKeyDashaInterpretationsPart1.KALACHAKRA_HEALTH_DESC_MODERATE, language)
                CHALLENGING -> StringResources.get(StringKeyDashaInterpretationsPart1.KALACHAKRA_HEALTH_DESC_CHALLENGING, language)
                CRITICAL -> StringResources.get(StringKeyDashaInterpretationsPart1.KALACHAKRA_HEALTH_DESC_CRITICAL, language)
            }
        }
    }

    /**
     * Deha-Jeeva analysis for health and spiritual matters
     */
    data class DehaJeevaAnalysis(
        val dehaRashi: ZodiacSign,
        val jeevaRashi: ZodiacSign,
        val dehaLord: Planet,
        val jeevaLord: Planet,
        val dehaLordStrength: String,
        val jeevaLordStrength: String,
        val dehaJeevaRelationship: DehaJeevaRelationship,
        val healthPrediction: String,
        val spiritualPrediction: String,
        val recommendations: List<String>
    )

    /**
     * Relationship between Deha and Jeeva rashis
     */
    enum class DehaJeevaRelationship(val displayName: String, val description: String) {
        HARMONIOUS("Harmonious", "Body and soul are aligned - good health and spiritual progress"),
        SUPPORTIVE("Supportive", "Jeeva supports Deha - spiritual practices benefit health"),
        NEUTRAL("Neutral", "Independent functioning of body and spirit"),
        CHALLENGING("Challenging", "Some friction between material and spiritual needs"),
        TRANSFORMATIVE("Transformative", "Deep karmic work needed to align body and soul");

        fun getLocalizedName(language: Language): String {
            return when (this) {
                HARMONIOUS -> StringResources.get(StringKeyDashaInterpretationsPart1.KALACHAKRA_REL_HARMONIOUS, language)
                SUPPORTIVE -> StringResources.get(StringKeyDashaInterpretationsPart1.KALACHAKRA_REL_SUPPORTIVE, language)
                NEUTRAL -> StringResources.get(StringKeyDashaInterpretationsPart1.KALACHAKRA_REL_NEUTRAL, language)
                CHALLENGING -> StringResources.get(StringKeyDashaInterpretationsPart1.KALACHAKRA_REL_CHALLENGING, language)
                TRANSFORMATIVE -> StringResources.get(StringKeyDashaInterpretationsPart1.KALACHAKRA_REL_TRANSFORMATIVE, language)
            }
        }

        fun getLocalizedDescription(language: Language): String {
            return when (this) {
                HARMONIOUS -> StringResources.get(StringKeyDashaInterpretationsPart1.KALACHAKRA_REL_DESC_HARMONIOUS, language)
                SUPPORTIVE -> StringResources.get(StringKeyDashaInterpretationsPart1.KALACHAKRA_REL_DESC_SUPPORTIVE, language)
                NEUTRAL -> StringResources.get(StringKeyDashaInterpretationsPart1.KALACHAKRA_REL_DESC_NEUTRAL, language)
                CHALLENGING -> StringResources.get(StringKeyDashaInterpretationsPart1.KALACHAKRA_REL_DESC_CHALLENGING, language)
                TRANSFORMATIVE -> StringResources.get(StringKeyDashaInterpretationsPart1.KALACHAKRA_REL_DESC_TRANSFORMATIVE, language)
            }
        }
    }

    /**
     * Mahadasha interpretation
     */
    data class MahadashaInterpretation(
        val generalEffects: String,
        val healthPrediction: String,
        val spiritualPrediction: String,
        val materialPrediction: String,
        val favorableAreas: List<String>,
        val cautionAreas: List<String>,
        val remedies: List<String>
    )

    /**
     * Overall Kalachakra interpretation
     */
    data class KalachakraInterpretation(
        val systemOverview: String,
        val nakshatraGroupAnalysis: String,
        val dehaJeevaSummary: String,
        val currentPhaseAnalysis: String,
        val healthOutlook: String,
        val spiritualOutlook: String,
        val generalGuidance: List<String>
    )

    // ============================================
    // MAIN CALCULATION METHODS
    // ============================================

    /**
     * Calculate complete Kalachakra Dasha from a Vedic chart
     *
     * @param chart The VedicChart to analyze
     * @param numberOfCycles Number of complete cycles to calculate
     * @return Complete KalachakraDashaResult
     */
    fun calculateKalachakraDasha(
        chart: VedicChart,
        numberOfCycles: Int = 2,
        language: Language = Language.ENGLISH
    ): KalachakraDashaResult {
        val birthDate = chart.birthData.dateTime.toLocalDate()

        // Get Moon's nakshatra and pada
        val moonPosition = chart.planetPositions.first { it.planet == Planet.MOON }
        val birthNakshatra = moonPosition.nakshatra
        val birthPada = moonPosition.nakshatraPada

        // Determine nakshatra group
        val nakshatraGroup = determineNakshatraGroup(birthNakshatra, birthPada)

        // Calculate starting sign based on nakshatra and pada
        val startingSign = calculateStartingSign(birthNakshatra, birthPada, nakshatraGroup)

        // Get Deha and Jeeva rashis
        val (dehaRashi, jeevaRashi) = calculateDehaJeeva(birthNakshatra, birthPada)

        // Calculate balance of dasha at birth
        val balanceInfo = calculateDashaBalance(moonPosition.longitude, birthNakshatra, birthPada)

        // Calculate all Mahadashas
        val mahadashas = calculateMahadashas(
            startingSign = startingSign,
            nakshatraGroup = nakshatraGroup,
            birthDate = birthDate,
            balanceInfo = balanceInfo,
            dehaRashi = dehaRashi,
            jeevaRashi = jeevaRashi,
            numberOfCycles = numberOfCycles,
            chart = chart,
            language = language
        )

        // Find current periods
        val today = LocalDate.now()
        val currentMahadasha = mahadashas.find { it.isActiveOn(today) }
        val currentAntardasha = currentMahadasha?.getAntardashaOn(today)

        // Deha-Jeeva analysis
        val dehaJeevaAnalysis = analyzeDehaJeeva(dehaRashi, jeevaRashi, chart, language)

        // Calculate applicability score
        val applicabilityScore = calculateApplicabilityScore(chart, birthNakshatra)

        // Generate interpretation
        val interpretation = generateKalachakraInterpretation(
            nakshatraGroup = nakshatraGroup,
            dehaRashi = dehaRashi,
            jeevaRashi = jeevaRashi,
            currentMahadasha = currentMahadasha,
            dehaJeevaAnalysis = dehaJeevaAnalysis,
            chart = chart,
            language = language
        )

        return KalachakraDashaResult(
            birthNakshatra = birthNakshatra,
            birthNakshatraPada = birthPada,
            nakshatraGroup = nakshatraGroup,
            startingSign = startingSign,
            dehaRashi = dehaRashi,
            jeevaRashi = jeevaRashi,
            mahadashas = mahadashas,
            currentMahadasha = currentMahadasha,
            currentAntardasha = currentAntardasha,
            dehaJeevaAnalysis = dehaJeevaAnalysis,
            interpretation = interpretation,
            applicabilityScore = applicabilityScore
        )
    }

    /**
     * Determine if nakshatra belongs to Savya or Apsavya group
     */
    private fun determineNakshatraGroup(nakshatra: Nakshatra, pada: Int): NakshatraGroup {
        // Special case for Uttara Ashadha which is split
        if (nakshatra == Nakshatra.UTTARA_ASHADHA) {
            return if (pada in UTTARA_ASHADHA_SAVYA_PADAS) {
                NakshatraGroup.SAVYA
            } else {
                NakshatraGroup.APSAVYA
            }
        }

        return when {
            nakshatra in SAVYA_NAKSHATRAS -> NakshatraGroup.SAVYA
            nakshatra in APSAVYA_NAKSHATRAS -> NakshatraGroup.APSAVYA
            else -> NakshatraGroup.SAVYA // Default to Savya for any edge cases
        }
    }

    /**
     * Calculate the starting sign based on nakshatra, pada, and group
     */
    private fun calculateStartingSign(
        nakshatra: Nakshatra,
        pada: Int,
        group: NakshatraGroup
    ): ZodiacSign {
        // The starting sign is based on the Navamsa of the Moon
        // Each nakshatra pada corresponds to one Navamsa sign
        val nakshatraIndex = nakshatra.ordinal
        val navamsaStartIndex = (nakshatraIndex * 4 + (pada - 1)) % 12

        return ZodiacSign.entries[navamsaStartIndex]
    }

    /**
     * Calculate Deha and Jeeva rashis based on nakshatra pada
     */
    private fun calculateDehaJeeva(nakshatra: Nakshatra, pada: Int): Pair<ZodiacSign, ZodiacSign> {
        // The Deha-Jeeva calculation is based on the sign-wise mapping
        // Each pada has specific Deha and Jeeva rashis

        // Calculate the Navamsa sign (which gives us Deha)
        val nakshatraIndex = nakshatra.ordinal
        val navamsaIndex = (nakshatraIndex * 4 + (pada - 1)) % 12
        val dehaRashi = ZodiacSign.entries[navamsaIndex]

        // Jeeva is calculated as the 5th from Deha (trine relationship)
        val jeevaIndex = (navamsaIndex + 4) % 12
        val jeevaRashi = ZodiacSign.entries[jeevaIndex]

        return Pair(dehaRashi, jeevaRashi)
    }

    /**
     * Balance of dasha at birth
     */
    private data class DashaBalance(
        val completedPortion: Double,
        val remainingPortion: Double,
        val remainingYears: Double,
        val startingSignIndex: Int
    )

    /**
     * Calculate the balance of dasha at birth based on Moon's position
     */
    private fun calculateDashaBalance(
        moonLongitude: Double,
        nakshatra: Nakshatra,
        pada: Int
    ): DashaBalance {
        // Each pada is 3°20' = 3.333... degrees
        val padaSize = 360.0 / 108.0 // 108 padas total

        // Calculate position within the pada
        val nakshatraStart = nakshatra.ordinal * (360.0 / 27.0)
        val padaStart = nakshatraStart + (pada - 1) * padaSize
        val positionInPada = ((moonLongitude - padaStart + 360.0) % 360.0)
        val completedPortion = (positionInPada / padaSize).coerceIn(0.0, 1.0)
        val remainingPortion = 1.0 - completedPortion

        // The starting sign for this pada
        val nakshatraIndex = nakshatra.ordinal
        val startingSignIndex = (nakshatraIndex * 4 + (pada - 1)) % 12

        // Get the period of the starting sign
        val startingSign = ZodiacSign.entries[startingSignIndex]
        val signPeriod = KALACHAKRA_PERIODS[startingSign] ?: 9
        val remainingYears = signPeriod * remainingPortion

        return DashaBalance(
            completedPortion = completedPortion,
            remainingPortion = remainingPortion,
            remainingYears = remainingYears,
            startingSignIndex = startingSignIndex
        )
    }

    /**
     * Calculate all Mahadashas
     */
    private fun calculateMahadashas(
        startingSign: ZodiacSign,
        nakshatraGroup: NakshatraGroup,
        birthDate: LocalDate,
        balanceInfo: DashaBalance,
        dehaRashi: ZodiacSign,
        jeevaRashi: ZodiacSign,
        numberOfCycles: Int,
        chart: VedicChart,
        language: Language
    ): List<KalachakraMahadasha> {
        val mahadashas = mutableListOf<KalachakraMahadasha>()
        var currentDate = birthDate

        // Generate sign sequence based on group (Savya or Apsavya)
        val signSequence = generateKalachakraSignSequence(
            startingSign = startingSign,
            group = nakshatraGroup,
            count = 9 * numberOfCycles // 9 signs per cycle
        )

        var isFirstDasha = true

        for ((index, sign) in signSequence.withIndex()) {
            var durationYears = KALACHAKRA_PERIODS[sign] ?: 9

            // For first dasha, use the balance
            if (isFirstDasha) {
                durationYears = balanceInfo.remainingYears.toInt().coerceAtLeast(1)
                isFirstDasha = false
            }

            val durationDays = yearsToRoundedDays(durationYears.toDouble())
            val endDate = currentDate.plusDays(durationDays)

            val signLord = sign.ruler
            val signDehaJeeva = DEHA_JEEVA_PAIRS[sign] ?: Pair(sign, sign)
            val isParamaAyush = isParamaAyushSign(sign, chart)
            val healthIndicator = calculateHealthIndicator(sign, dehaRashi, jeevaRashi, chart)

            val antardashas = calculateAntardashas(
                mahadashaSign = sign,
                group = nakshatraGroup,
                mahaStart = currentDate,
                mahaEnd = endDate,
                dehaRashi = dehaRashi,
                jeevaRashi = jeevaRashi,
                chart = chart,
                language = language
            )

            val interpretation = generateMahadashaInterpretation(
                sign = sign,
                signLord = signLord,
                healthIndicator = healthIndicator,
                dehaRashi = dehaRashi,
                jeevaRashi = jeevaRashi,
                chart = chart,
                language = language
            )

            mahadashas.add(
                KalachakraMahadasha(
                    sign = sign,
                    startDate = currentDate,
                    endDate = endDate,
                    durationYears = durationYears,
                    signLord = signLord,
                    dehaRashi = signDehaJeeva.first,
                    jeevaRashi = signDehaJeeva.second,
                    antardashas = antardashas,
                    isParamaAyushSign = isParamaAyush,
                    healthIndicator = healthIndicator,
                    interpretation = interpretation
                )
            )

            currentDate = endDate.plusDays(1)
        }

        return mahadashas
    }

    /**
     * Generate sign sequence based on Kalachakra rules
     */
    private fun generateKalachakraSignSequence(
        startingSign: ZodiacSign,
        group: NakshatraGroup,
        count: Int
    ): List<ZodiacSign> {
        val sequence = mutableListOf<ZodiacSign>()

        // Kalachakra follows a specific 9-sign pattern
        // The pattern changes based on Savya/Apsavya

        val savyaPattern = listOf(0, 1, 2, 3, 4, 5, 6, 7, 8) // Aries to Sagittarius
        val apsavyaPattern = listOf(0, 11, 10, 9, 8, 7, 6, 5, 4) // Reverse direction

        val pattern = if (group == NakshatraGroup.SAVYA) savyaPattern else apsavyaPattern
        var cycleCount = 0

        while (sequence.size < count) {
            for (offset in pattern) {
                if (sequence.size >= count) break

                val signIndex = if (group == NakshatraGroup.SAVYA) {
                    (startingSign.ordinal + offset) % 12
                } else {
                    (startingSign.ordinal - offset + 12) % 12
                }
                sequence.add(ZodiacSign.entries[signIndex])
            }
            cycleCount++
        }

        return sequence
    }

    /**
     * Calculate Antardashas within a Mahadasha
     */
    private fun calculateAntardashas(
        mahadashaSign: ZodiacSign,
        group: NakshatraGroup,
        mahaStart: LocalDate,
        mahaEnd: LocalDate,
        dehaRashi: ZodiacSign,
        jeevaRashi: ZodiacSign,
        chart: VedicChart,
        language: Language
    ): List<KalachakraAntardasha> {
        val antardashas = mutableListOf<KalachakraAntardasha>()
        val mahaDurationDays = ChronoUnit.DAYS.between(mahaStart, mahaEnd)

        if (mahaDurationDays <= 0) return antardashas

        // Antardasha sequence follows the same Kalachakra pattern
        val antarSignSequence = generateKalachakraSignSequence(
            startingSign = mahadashaSign,
            group = group,
            count = 9
        )

        // Calculate total years for proportional division
        val totalYears = antarSignSequence.sumOf { KALACHAKRA_PERIODS[it] ?: 9 }

        var currentDate = mahaStart

        for ((index, antarSign) in antarSignSequence.withIndex()) {
            val antarYears = KALACHAKRA_PERIODS[antarSign] ?: 9
            val proportion = antarYears.toDouble() / totalYears
            val antarDurationDays = (mahaDurationDays * proportion).toLong().coerceAtLeast(1L)

            val endDate = if (index == antarSignSequence.size - 1) {
                mahaEnd
            } else {
                currentDate.plusDays(antarDurationDays).let {
                    if (it.isAfter(mahaEnd)) mahaEnd else it
                }
            }

            val isDeha = antarSign == dehaRashi
            val isJeeva = antarSign == jeevaRashi

            val interpretation = generateAntardashaInterpretation(
                mahadashaSign = mahadashaSign,
                antardashaSign = antarSign,
                isDeha = isDeha,
                isJeeva = isJeeva,
                language = language
            )

            antardashas.add(
                KalachakraAntardasha(
                    sign = antarSign,
                    mahadashaSign = mahadashaSign,
                    startDate = currentDate,
                    endDate = endDate,
                    durationDays = ChronoUnit.DAYS.between(currentDate, endDate).coerceAtLeast(1L),
                    signLord = antarSign.ruler,
                    isDehaSign = isDeha,
                    isJeevaSign = isJeeva,
                    interpretation = interpretation
                )
            )

            if (endDate >= mahaEnd) break
            currentDate = endDate.plusDays(1)
        }

        return antardashas
    }

    /**
     * Check if sign is Parama Ayush (special longevity indicator)
     */
    private fun isParamaAyushSign(sign: ZodiacSign, chart: VedicChart): Boolean {
        // Signs where Jupiter is strong or exalted contribute to longevity
        val jupiterPosition = chart.planetPositions.find { it.planet == Planet.JUPITER }
        return sign == ZodiacSign.CANCER || // Jupiter's exaltation
               sign == ZodiacSign.SAGITTARIUS || // Jupiter's own sign
               sign == ZodiacSign.PISCES || // Jupiter's own sign
               jupiterPosition?.sign == sign
    }

    /**
     * Calculate health indicator for the dasha
     */
    private fun calculateHealthIndicator(
        sign: ZodiacSign,
        dehaRashi: ZodiacSign,
        jeevaRashi: ZodiacSign,
        chart: VedicChart
    ): HealthIndicator {
        var score = 3 // Start neutral

        // Check relationship with Deha and Jeeva
        if (sign == dehaRashi) {
            score += 1 // Deha sign period focuses on body
        }
        if (sign == jeevaRashi) {
            score += 1 // Jeeva sign period is spiritually uplifting
        }

        // Check if sign has benefics
        val beneficsInSign = chart.planetPositions.filter {
            it.sign == sign && it.planet in listOf(Planet.JUPITER, Planet.VENUS, Planet.MOON, Planet.MERCURY)
        }
        score += beneficsInSign.size.coerceAtMost(2)

        // Check for malefics
        val maleficsInSign = chart.planetPositions.filter {
            it.sign == sign && it.planet in listOf(Planet.SATURN, Planet.MARS, Planet.RAHU, Planet.KETU)
        }
        score -= maleficsInSign.size

        // Check sign lord strength
        val signLord = sign.ruler
        val signLordPosition = chart.planetPositions.find { it.planet == signLord }
        if (signLordPosition != null) {
            when (signLordPosition.sign) {
                sign -> score += 1 // In own sign
                signLord.exaltationSign -> score += 1 // Exalted
                signLord.debilitationSign -> score -= 1 // Debilitated
                else -> {}
            }
        }

        return when {
            score >= 5 -> HealthIndicator.EXCELLENT
            score >= 4 -> HealthIndicator.GOOD
            score >= 3 -> HealthIndicator.MODERATE
            score >= 2 -> HealthIndicator.CHALLENGING
            else -> HealthIndicator.CRITICAL
        }
    }

    /**
     * Analyze Deha-Jeeva relationship
     */
    private fun analyzeDehaJeeva(
        dehaRashi: ZodiacSign,
        jeevaRashi: ZodiacSign,
        chart: VedicChart,
        language: Language
    ): DehaJeevaAnalysis {
        val dehaLord = dehaRashi.ruler
        val jeevaLord = jeevaRashi.ruler

        val dehaLordPosition = chart.planetPositions.find { it.planet == dehaLord }
        val jeevaLordPosition = chart.planetPositions.find { it.planet == jeevaLord }

        val dehaLordStrength = assessPlanetStrength(dehaLord, dehaLordPosition, chart, language)
        val jeevaLordStrength = assessPlanetStrength(jeevaLord, jeevaLordPosition, chart, language)

        // Determine relationship
        val relationship = determineDehaJeevaRelationship(
            dehaRashi, jeevaRashi, dehaLord, jeevaLord, chart
        )

        val healthPrediction = generateHealthPrediction(dehaLordStrength, relationship, language)
        val spiritualPrediction = generateSpiritualPrediction(jeevaLordStrength, relationship, language)

        val recommendations = generateDehaJeevaRecommendations(
            dehaLord, jeevaLord, relationship, language
        )

        return DehaJeevaAnalysis(
            dehaRashi = dehaRashi,
            jeevaRashi = jeevaRashi,
            dehaLord = dehaLord,
            jeevaLord = jeevaLord,
            dehaLordStrength = dehaLordStrength,
            jeevaLordStrength = jeevaLordStrength,
            dehaJeevaRelationship = relationship,
            healthPrediction = healthPrediction,
            spiritualPrediction = spiritualPrediction,
            recommendations = recommendations
        )
    }

    /**
     * Assess planet strength for display
     */
    private fun assessPlanetStrength(
        planet: Planet,
        position: com.astro.storm.core.model.PlanetPosition?,
        chart: VedicChart,
        language: Language
    ): String {
        if (position == null) return StringResources.get(StringKeyDoshaPart3.PLANET_STRENGTH_UNKNOWN, language)

        return when {
            position.sign == planet.exaltationSign -> StringResources.get(StringKeyDoshaPart3.PLANET_STRENGTH_EXALTED, language)
            position.sign.ruler == planet -> StringResources.get(StringKeyDoshaPart3.PLANET_STRENGTH_OWN_SIGN, language)
            position.sign == planet.debilitationSign -> StringResources.get(StringKeyDoshaPart3.PLANET_STRENGTH_DEBILITATED, language)
            position.isRetrograde -> StringResources.get(StringKeyDoshaPart3.PLANET_STRENGTH_RETROGRADE, language)
            else -> StringResources.get(StringKeyDoshaPart3.PLANET_STRENGTH_MODERATE, language)
        }
    }

    /**
     * Determine the relationship between Deha and Jeeva
     */
    private fun determineDehaJeevaRelationship(
        dehaRashi: ZodiacSign,
        jeevaRashi: ZodiacSign,
        dehaLord: Planet,
        jeevaLord: Planet,
        chart: VedicChart
    ): DehaJeevaRelationship {
        // Calculate the house distance between Deha and Jeeva
        val distance = ((jeevaRashi.number - dehaRashi.number + 12) % 12) + 1

        // Trine relationship (5th or 9th) is most harmonious
        if (distance in listOf(5, 9)) {
            return DehaJeevaRelationship.HARMONIOUS
        }

        // Same sign or 7th (opposition but partnership)
        if (distance == 1 || distance == 7) {
            return DehaJeevaRelationship.SUPPORTIVE
        }

        // Square relationship (4th, 10th)
        if (distance in listOf(4, 10)) {
            return DehaJeevaRelationship.CHALLENGING
        }

        // 6th, 8th, 12th (dusthana relationship)
        if (distance in listOf(6, 8, 12)) {
            return DehaJeevaRelationship.TRANSFORMATIVE
        }

        return DehaJeevaRelationship.NEUTRAL
    }

    /**
     * Generate health prediction based on Deha analysis
     */
    private fun generateHealthPrediction(
        dehaLordStrength: String,
        relationship: DehaJeevaRelationship,
        language: Language
    ): String {
        return buildString {
            // "Based on Deha lord's %1$s status and %2$s Deha-Jeeva relationship: "
            append(StringResources.get(StringKeyDashaInterpretationsPart1.KALACHAKRA_PRED_INTRO_DEHA, language, dehaLordStrength, relationship.getLocalizedName(language)))
            
            val effect = when (relationship) {
                DehaJeevaRelationship.HARMONIOUS -> StringResources.get(StringKeyDashaInterpretationsPart1.KALACHAKRA_HEALTH_PRED_HARMONIOUS, language)
                DehaJeevaRelationship.SUPPORTIVE -> StringResources.get(StringKeyDashaInterpretationsPart1.KALACHAKRA_HEALTH_PRED_SUPPORTIVE, language)
                DehaJeevaRelationship.NEUTRAL -> StringResources.get(StringKeyDashaInterpretationsPart1.KALACHAKRA_HEALTH_PRED_NEUTRAL, language)
                DehaJeevaRelationship.CHALLENGING -> StringResources.get(StringKeyDashaInterpretationsPart1.KALACHAKRA_HEALTH_PRED_CHALLENGING, language)
                DehaJeevaRelationship.TRANSFORMATIVE -> StringResources.get(StringKeyDashaInterpretationsPart1.KALACHAKRA_HEALTH_PRED_TRANSFORMATIVE, language)
            }
            append(effect)
        }
    }

    /**
     * Generate spiritual prediction based on Jeeva analysis
     */
    private fun generateSpiritualPrediction(
        jeevaLordStrength: String,
        relationship: DehaJeevaRelationship,
        language: Language
    ): String {
        return buildString {
            // "Jeeva lord's %1$s condition indicates "
            append(StringResources.get(StringKeyDashaInterpretationsPart1.KALACHAKRA_PRED_INTRO_JEEVA, language, jeevaLordStrength))
            
            val effect = when (relationship) {
                DehaJeevaRelationship.HARMONIOUS -> StringResources.get(StringKeyDashaInterpretationsPart1.KALACHAKRA_SPIRITUAL_PRED_HARMONIOUS, language)
                DehaJeevaRelationship.SUPPORTIVE -> StringResources.get(StringKeyDashaInterpretationsPart1.KALACHAKRA_SPIRITUAL_PRED_SUPPORTIVE, language)
                DehaJeevaRelationship.NEUTRAL -> StringResources.get(StringKeyDashaInterpretationsPart1.KALACHAKRA_SPIRITUAL_PRED_NEUTRAL, language)
                DehaJeevaRelationship.CHALLENGING -> StringResources.get(StringKeyDashaInterpretationsPart1.KALACHAKRA_SPIRITUAL_PRED_CHALLENGING, language)
                DehaJeevaRelationship.TRANSFORMATIVE -> StringResources.get(StringKeyDashaInterpretationsPart1.KALACHAKRA_SPIRITUAL_PRED_TRANSFORMATIVE, language)
            }
            append(effect)
        }
    }

    /**
     * Generate recommendations based on Deha-Jeeva analysis
     */
    private fun generateDehaJeevaRecommendations(
        dehaLord: Planet,
        jeevaLord: Planet,
        relationship: DehaJeevaRelationship,
        language: Language
    ): List<String> {
        val recommendations = mutableListOf<String>()

        // Deha lord remedies
        val dehaLordName = dehaLord.displayName // Ideally localized
        recommendations.add(StringResources.get(StringKeyDashaInterpretationsPart1.KALACHAKRA_REC_DEHA_LORD, language, dehaLordName))

        // Jeeva lord remedies
        val jeevaLordName = jeevaLord.displayName // Ideally localized
        recommendations.add(StringResources.get(StringKeyDashaInterpretationsPart1.KALACHAKRA_REC_JEEVA_LORD, language, jeevaLordName))

        // Relationship-specific recommendations
        when (relationship) {
            DehaJeevaRelationship.HARMONIOUS -> {
                recommendations.add(StringResources.get(StringKeyDashaInterpretationsPart1.KALACHAKRA_REC_HARMONIOUS_1, language))
                recommendations.add(StringResources.get(StringKeyDashaInterpretationsPart1.KALACHAKRA_REC_HARMONIOUS_2, language))
            }
            DehaJeevaRelationship.SUPPORTIVE -> {
                recommendations.add(StringResources.get(StringKeyDashaInterpretationsPart1.KALACHAKRA_REC_SUPPORTIVE_1, language))
                recommendations.add(StringResources.get(StringKeyDashaInterpretationsPart1.KALACHAKRA_REC_SUPPORTIVE_2, language))
            }
            DehaJeevaRelationship.NEUTRAL -> {
                recommendations.add(StringResources.get(StringKeyDashaInterpretationsPart1.KALACHAKRA_REC_NEUTRAL_1, language))
                recommendations.add(StringResources.get(StringKeyDashaInterpretationsPart1.KALACHAKRA_REC_NEUTRAL_2, language))
            }
            DehaJeevaRelationship.CHALLENGING -> {
                recommendations.add(StringResources.get(StringKeyDashaInterpretationsPart1.KALACHAKRA_REC_CHALLENGING_1, language))
                recommendations.add(StringResources.get(StringKeyDashaInterpretationsPart1.KALACHAKRA_REC_CHALLENGING_2, language))
            }
            DehaJeevaRelationship.TRANSFORMATIVE -> {
                recommendations.add(StringResources.get(StringKeyDashaInterpretationsPart1.KALACHAKRA_REC_TRANSFORMATIVE_1, language))
                recommendations.add(StringResources.get(StringKeyDashaInterpretationsPart1.KALACHAKRA_REC_TRANSFORMATIVE_2, language))
            }
        }

        return recommendations
    }

    /**
     * Calculate how applicable Kalachakra Dasha is for this chart
     */
    private fun calculateApplicabilityScore(chart: VedicChart, nakshatra: Nakshatra): Int {
        var score = 50 // Base score

        // Moon's strength affects applicability (Moon-based system)
        val moonPosition = chart.planetPositions.first { it.planet == Planet.MOON }
        when (moonPosition.sign) {
            ZodiacSign.TAURUS -> score += 20 // Moon exalted
            ZodiacSign.CANCER -> score += 15 // Moon in own sign
            ZodiacSign.SCORPIO -> score -= 15 // Moon debilitated
            else -> {}
        }

        // Nakshatra type affects score
        if (nakshatra in SAVYA_NAKSHATRAS || nakshatra in APSAVYA_NAKSHATRAS) {
            score += 10 // Clear group assignment
        }

        // 8th house and 8th lord analysis (longevity house)
        val eighthSign = ZodiacSign.entries[(ZodiacSign.fromLongitude(chart.ascendant).ordinal + 7) % 12]
        val eighthLord = eighthSign.ruler
        val eighthLordPosition = chart.planetPositions.find { it.planet == eighthLord }
        if (eighthLordPosition != null) {
            if (eighthLordPosition.sign == eighthLord.exaltationSign) {
                score += 10 // Strong 8th lord increases longevity focus relevance
            }
        }

        return score.coerceIn(0, 100)
    }

    // ============================================
    // INTERPRETATION METHODS
    // ============================================

    /**
     * Generate Mahadasha interpretation
     */
    private fun generateMahadashaInterpretation(
        sign: ZodiacSign,
        signLord: Planet,
        healthIndicator: HealthIndicator,
        dehaRashi: ZodiacSign,
        jeevaRashi: ZodiacSign,
        chart: VedicChart,
        language: Language
    ): MahadashaInterpretation {
        val generalEffects = getSignGeneralEffects(sign, language)
        val healthPrediction = "${StringResources.get(StringKeyDashaInterpretationsPart2.KALACHAKRA_HEALTH_INDICATOR, language)}: ${healthIndicator.getLocalizedName(language)} - ${healthIndicator.getLocalizedDescription(language)}"
        val spiritualPrediction = getSignSpiritualEffects(sign, language)
        val materialPrediction = getSignMaterialEffects(sign, chart, language)
        val favorableAreas = getFavorableAreas(sign, signLord, language)
        val cautionAreas = getCautionAreas(sign, healthIndicator, language)
        val remedies = getKalachakraRemedies(sign, signLord, language)

        return MahadashaInterpretation(
            generalEffects = generalEffects,
            healthPrediction = healthPrediction,
            spiritualPrediction = spiritualPrediction,
            materialPrediction = materialPrediction,
            favorableAreas = favorableAreas,
            cautionAreas = cautionAreas,
            remedies = remedies
        )
    }

    /**
     * Generate Antardasha interpretation
     */
    private fun generateAntardashaInterpretation(
        mahadashaSign: ZodiacSign,
        antardashaSign: ZodiacSign,
        isDeha: Boolean,
        isJeeva: Boolean,
        language: Language
    ): String {
        return buildString {
            append("${antardashaSign.getLocalizedName(language)} ${StringResources.get(StringKeyDashaInterpretationsPart2.SUDARSHANA_FOCUS_GENERAL, language)} ${StringResources.get(StringKeyDoshaPart4.FROM_LABEL, language)} ${mahadashaSign.getLocalizedName(language)}: ")

            if (isDeha) {
                append(StringResources.get(StringKeyDashaInterpretationsPart2.KALACHAKRA_DEHA_ANALYSIS, language))
                append(". ")
            }
            if (isJeeva) {
                append(StringResources.get(StringKeyDashaInterpretationsPart2.KALACHAKRA_JEEVA_ANALYSIS, language))
                append(". ")
            }
            if (!isDeha && !isJeeva) {
                append("${StringResources.get(StringKeyDashaInterpretationsPart2.SUDARSHANA_FOCUS_GENERAL, language)} ${antardashaSign.getLocalizedName(language)}. ")
            }

            append(getSignBriefEffect(antardashaSign, language))
        }
    }

    /**
     * Generate overall Kalachakra interpretation
     */
    private fun generateKalachakraInterpretation(
        nakshatraGroup: NakshatraGroup,
        dehaRashi: ZodiacSign,
        jeevaRashi: ZodiacSign,
        currentMahadasha: KalachakraMahadasha?,
        dehaJeevaAnalysis: DehaJeevaAnalysis,
        chart: VedicChart,
        language: Language
    ): KalachakraInterpretation {
        val systemOverview = StringResources.get(StringKeyDashaInterpretationsPart1.KALACHAKRA_OVERVIEW_TEXT, language)

        val nakshatraGroupAnalysis = String.format(
            StringResources.get(StringKeyDashaInterpretationsPart2.KALACHAKRA_INTERP_GROUP_ANALYSIS, language),
            nakshatraGroup.getLocalizedName(language),
            nakshatraGroup.getLocalizedDescription(language)
        )

        val dehaJeevaSummary = String.format(
            StringResources.get(StringKeyDashaInterpretationsPart2.KALACHAKRA_INTERP_DEHA_JEEVA_SUMMARY, language),
            dehaRashi.getLocalizedName(language),
            jeevaRashi.getLocalizedName(language),
            dehaJeevaAnalysis.dehaJeevaRelationship.getLocalizedName(language)
        )

        val currentPhaseAnalysis = if (currentMahadasha != null) {
            String.format(
                StringResources.get(StringKeyDashaInterpretationsPart2.KALACHAKRA_INTERP_CURRENT_PHASE, language),
                currentMahadasha.sign.getLocalizedName(language),
                currentMahadasha.durationYears,
                currentMahadasha.healthIndicator.getLocalizedName(language),
                currentMahadasha.interpretation.generalEffects
            )
        } else {
            StringResources.get(StringKeyDashaInterpretationsPart2.KALACHAKRA_INTERP_NO_CURRENT, language)
        }

        val healthOutlook = dehaJeevaAnalysis.healthPrediction
        val spiritualOutlook = dehaJeevaAnalysis.spiritualPrediction

        val generalGuidance = listOf(
            StringResources.get(StringKeyDashaInterpretationsPart1.KALACHAKRA_GUIDANCE_1, language),
            StringResources.get(StringKeyDashaInterpretationsPart1.KALACHAKRA_GUIDANCE_2, language),
            StringResources.get(StringKeyDashaInterpretationsPart1.KALACHAKRA_GUIDANCE_3, language),
            StringResources.get(StringKeyDashaInterpretationsPart1.KALACHAKRA_GUIDANCE_4, language),
            StringResources.get(StringKeyDashaInterpretationsPart1.KALACHAKRA_GUIDANCE_5, language)
        )

        return KalachakraInterpretation(
            systemOverview = systemOverview,
            nakshatraGroupAnalysis = nakshatraGroupAnalysis,
            dehaJeevaSummary = dehaJeevaSummary,
            currentPhaseAnalysis = currentPhaseAnalysis,
            healthOutlook = healthOutlook,
            spiritualOutlook = spiritualOutlook,
            generalGuidance = generalGuidance
        )
    }

    // ============================================
    // HELPER METHODS
    // ============================================

    private fun yearsToRoundedDays(years: Double): Long = DashaUtils.yearsToRoundedDays(years)

    private fun getSignGeneralEffects(sign: ZodiacSign, language: Language): String {
        return when (sign) {
            ZodiacSign.ARIES -> StringResources.get(StringKeyDashaInterpretationsPart1.KALACHAKRA_EFFECT_ARIES, language)
            ZodiacSign.TAURUS -> StringResources.get(StringKeyDashaInterpretationsPart1.KALACHAKRA_EFFECT_TAURUS, language)
            ZodiacSign.GEMINI -> StringResources.get(StringKeyDashaInterpretationsPart1.KALACHAKRA_EFFECT_GEMINI, language)
            ZodiacSign.CANCER -> StringResources.get(StringKeyDashaInterpretationsPart1.KALACHAKRA_EFFECT_CANCER, language)
            ZodiacSign.LEO -> StringResources.get(StringKeyDashaInterpretationsPart1.KALACHAKRA_EFFECT_LEO, language)
            ZodiacSign.VIRGO -> StringResources.get(StringKeyDashaInterpretationsPart1.KALACHAKRA_EFFECT_VIRGO, language)
            ZodiacSign.LIBRA -> StringResources.get(StringKeyDashaInterpretationsPart1.KALACHAKRA_EFFECT_LIBRA, language)
            ZodiacSign.SCORPIO -> StringResources.get(StringKeyDashaInterpretationsPart1.KALACHAKRA_EFFECT_SCORPIO, language)
            ZodiacSign.SAGITTARIUS -> StringResources.get(StringKeyDashaInterpretationsPart1.KALACHAKRA_EFFECT_SAGITTARIUS, language)
            ZodiacSign.CAPRICORN -> StringResources.get(StringKeyDashaInterpretationsPart1.KALACHAKRA_EFFECT_CAPRICORN, language)
            ZodiacSign.AQUARIUS -> StringResources.get(StringKeyDashaInterpretationsPart1.KALACHAKRA_EFFECT_AQUARIUS, language)
            ZodiacSign.PISCES -> StringResources.get(StringKeyDashaInterpretationsPart1.KALACHAKRA_EFFECT_PISCES, language)
        }
    }

    private fun getSignSpiritualEffects(sign: ZodiacSign, language: Language): String {
        return when (sign.element) {
            "Fire" -> StringResources.get(StringKeyDashaInterpretationsPart2.KALACHAKRA_SPIRITUAL_FIRE, language)
            "Earth" -> StringResources.get(StringKeyDashaInterpretationsPart2.KALACHAKRA_SPIRITUAL_EARTH, language)
            "Air" -> StringResources.get(StringKeyDashaInterpretationsPart2.KALACHAKRA_SPIRITUAL_AIR, language)
            "Water" -> StringResources.get(StringKeyDashaInterpretationsPart2.KALACHAKRA_SPIRITUAL_WATER, language)
            else -> StringResources.get(StringKeyDashaInterpretationsPart2.KALACHAKRA_SPIRITUAL_GENERAL, language)
        }
    }

    private fun getSignMaterialEffects(sign: ZodiacSign, chart: VedicChart, language: Language): String {
        val signLord = sign.ruler
        val lordPosition = chart.planetPositions.find { it.planet == signLord }
        val lordHouse = lordPosition?.house ?: 1

        return buildString {
            append(String.format(StringResources.get(StringKeyDashaInterpretationsPart2.KALACHAKRA_MATERIAL_SIGN_FOCUS, language), sign.getLocalizedName(language)))
            append(" ")
            append(String.format(StringResources.get(StringKeyDashaInterpretationsPart2.KALACHAKRA_MATERIAL_LORD_POS, language), getLocalizedPlanetName(signLord, language), lordHouse))
            when (lordHouse) {
                1 -> append(StringResources.get(StringKeyDashaInterpretationsPart2.KALACHAKRA_MATERIAL_H1, language))
                2 -> append(StringResources.get(StringKeyDashaInterpretationsPart2.KALACHAKRA_MATERIAL_H2, language))
                3 -> append(StringResources.get(StringKeyDashaInterpretationsPart2.KALACHAKRA_MATERIAL_H3, language))
                4 -> append(StringResources.get(StringKeyDashaInterpretationsPart2.KALACHAKRA_MATERIAL_H4, language))
                5 -> append(StringResources.get(StringKeyDashaInterpretationsPart2.KALACHAKRA_MATERIAL_H5, language))
                6 -> append(StringResources.get(StringKeyDashaInterpretationsPart2.KALACHAKRA_MATERIAL_H6, language))
                7 -> append(StringResources.get(StringKeyDashaInterpretationsPart2.KALACHAKRA_MATERIAL_H7, language))
                8 -> append(StringResources.get(StringKeyDashaInterpretationsPart2.KALACHAKRA_MATERIAL_H8, language))
                9 -> append(StringResources.get(StringKeyDashaInterpretationsPart2.KALACHAKRA_MATERIAL_H9, language))
                10 -> append(StringResources.get(StringKeyDashaInterpretationsPart2.KALACHAKRA_MATERIAL_H10, language))
                11 -> append(StringResources.get(StringKeyDashaInterpretationsPart2.KALACHAKRA_MATERIAL_H11, language))
                12 -> append(StringResources.get(StringKeyDashaInterpretationsPart2.KALACHAKRA_MATERIAL_H12, language))
                else -> append(StringResources.get(StringKeyDashaInterpretationsPart2.KALACHAKRA_MATERIAL_GENERAL_PROGRESS, language))
            }
        }
    }

    private fun getFavorableAreas(sign: ZodiacSign, signLord: Planet, language: Language): List<String> {
        val areas = mutableListOf<String>()

        areas.add("${getLocalizedPlanetName(signLord, language)}-${StringResources.get(com.astro.storm.core.common.StringKeyPart1.VARSHAPHALA_KEY_DATES, language)}") // Fallback to "Key Dates" or similar if needed, but better to have specific key

        when (sign.element) {
            "Fire" -> areas.addAll(listOf(
                StringResources.get(StringKeyDashaInterpretationsPart2.KALACHAKRA_AREA_LEADERSHIP, language),
                StringResources.get(StringKeyDashaInterpretationsPart2.KALACHAKRA_AREA_SPORTS, language),
                StringResources.get(StringKeyDashaInterpretationsPart2.KALACHAKRA_AREA_ENGINEERING, language),
                StringResources.get(StringKeyDashaInterpretationsPart2.KALACHAKRA_AREA_MILITARY, language)
            ))
            "Earth" -> areas.addAll(listOf(
                StringResources.get(StringKeyDashaInterpretationsPart2.KALACHAKRA_AREA_FINANCE, language),
                StringResources.get(StringKeyDashaInterpretationsPart2.KALACHAKRA_AREA_REAL_ESTATE, language),
                StringResources.get(StringKeyDashaInterpretationsPart2.KALACHAKRA_AREA_AGRICULTURE, language),
                StringResources.get(StringKeyDashaInterpretationsPart2.KALACHAKRA_AREA_CONSTRUCTION, language)
            ))
            "Air" -> areas.addAll(listOf(
                StringResources.get(StringKeyDashaInterpretationsPart2.KALACHAKRA_AREA_COMMUNICATION, language),
                StringResources.get(StringKeyDashaInterpretationsPart2.KALACHAKRA_AREA_EDUCATION, language),
                StringResources.get(StringKeyDashaInterpretationsPart2.KALACHAKRA_AREA_TRAVEL, language),
                StringResources.get(StringKeyDashaInterpretationsPart2.KALACHAKRA_AREA_TECHNOLOGY, language)
            ))
            "Water" -> areas.addAll(listOf(
                StringResources.get(StringKeyDashaInterpretationsPart2.KALACHAKRA_AREA_HEALING, language),
                StringResources.get(StringKeyDashaInterpretationsPart2.KALACHAKRA_AREA_ARTS, language),
                StringResources.get(StringKeyDashaInterpretationsPart2.KALACHAKRA_AREA_PSYCHOLOGY, language),
                StringResources.get(StringKeyDashaInterpretationsPart2.KALACHAKRA_AREA_SPIRITUALITY, language)
            ))
        }

        return areas
    }

    private fun getCautionAreas(sign: ZodiacSign, healthIndicator: HealthIndicator, language: Language): List<String> {
        val areas = mutableListOf<String>()

        if (healthIndicator.score <= 2) {
            areas.add(StringResources.get(StringKeyDashaInterpretationsPart2.KALACHAKRA_CAUTION_HEALTH, language))
        }

        val key = when (sign) {
            ZodiacSign.ARIES -> StringKeyDashaInterpretationsPart2.KALACHAKRA_CAUTION_ARIES
            ZodiacSign.TAURUS -> StringKeyDashaInterpretationsPart2.KALACHAKRA_CAUTION_TAURUS
            ZodiacSign.GEMINI -> StringKeyDashaInterpretationsPart2.KALACHAKRA_CAUTION_GEMINI
            ZodiacSign.CANCER -> StringKeyDashaInterpretationsPart2.KALACHAKRA_CAUTION_CANCER
            ZodiacSign.LEO -> StringKeyDashaInterpretationsPart2.KALACHAKRA_CAUTION_LEO
            ZodiacSign.VIRGO -> StringKeyDashaInterpretationsPart2.KALACHAKRA_CAUTION_VIRGO
            ZodiacSign.LIBRA -> StringKeyDashaInterpretationsPart2.KALACHAKRA_CAUTION_LIBRA
            ZodiacSign.SCORPIO -> StringKeyDashaInterpretationsPart2.KALACHAKRA_CAUTION_SCORPIO
            ZodiacSign.SAGITTARIUS -> StringKeyDashaInterpretationsPart2.KALACHAKRA_CAUTION_SAGITTARIUS
            ZodiacSign.CAPRICORN -> StringKeyDashaInterpretationsPart2.KALACHAKRA_CAUTION_CAPRICORN
            ZodiacSign.AQUARIUS -> StringKeyDashaInterpretationsPart2.KALACHAKRA_CAUTION_AQUARIUS
            ZodiacSign.PISCES -> StringKeyDashaInterpretationsPart2.KALACHAKRA_CAUTION_PISCES
        }
        areas.add(StringResources.get(key, language))

        return areas
    }

    private fun getKalachakraRemedies(sign: ZodiacSign, signLord: Planet, language: Language): List<String> {
        val remedies = mutableListOf<String>()

        // Sign lord mantra
        remedies.add(String.format(StringResources.get(StringKeyDashaInterpretationsPart2.KALACHAKRA_REMEDY_MANTRA, language), getLocalizedPlanetName(signLord, language)))

        // Elemental remedies
        val elemKey = when (sign.element) {
            "Fire" -> StringKeyDashaInterpretationsPart2.KALACHAKRA_REMEDY_FIRE
            "Earth" -> StringKeyDashaInterpretationsPart2.KALACHAKRA_REMEDY_EARTH
            "Air" -> StringKeyDashaInterpretationsPart2.KALACHAKRA_REMEDY_AIR
            "Water" -> StringKeyDashaInterpretationsPart2.KALACHAKRA_REMEDY_WATER
            else -> null
        }
        elemKey?.let { remedies.add(StringResources.get(it, language)) }

        // Health-specific
        remedies.add(StringResources.get(StringKeyDashaInterpretationsPart2.KALACHAKRA_REMEDY_DEITY, language))

        return remedies
    }

    private fun getSignBriefEffect(sign: ZodiacSign, language: Language): String {
        return when (sign) {
            ZodiacSign.ARIES -> StringResources.get(StringKeyDashaInterpretationsPart1.KALACHAKRA_BRIEF_ARIES, language)
            ZodiacSign.TAURUS -> StringResources.get(StringKeyDashaInterpretationsPart1.KALACHAKRA_BRIEF_TAURUS, language)
            ZodiacSign.GEMINI -> StringResources.get(StringKeyDashaInterpretationsPart1.KALACHAKRA_BRIEF_GEMINI, language)
            ZodiacSign.CANCER -> StringResources.get(StringKeyDashaInterpretationsPart1.KALACHAKRA_BRIEF_CANCER, language)
            ZodiacSign.LEO -> StringResources.get(StringKeyDashaInterpretationsPart1.KALACHAKRA_BRIEF_LEO, language)
            ZodiacSign.VIRGO -> StringResources.get(StringKeyDashaInterpretationsPart1.KALACHAKRA_BRIEF_VIRGO, language)
            ZodiacSign.LIBRA -> StringResources.get(StringKeyDashaInterpretationsPart1.KALACHAKRA_BRIEF_LIBRA, language)
            ZodiacSign.SCORPIO -> StringResources.get(StringKeyDashaInterpretationsPart1.KALACHAKRA_BRIEF_SCORPIO, language)
            ZodiacSign.SAGITTARIUS -> StringResources.get(StringKeyDashaInterpretationsPart1.KALACHAKRA_BRIEF_SAGITTARIUS, language)
            ZodiacSign.CAPRICORN -> StringResources.get(StringKeyDashaInterpretationsPart1.KALACHAKRA_BRIEF_CAPRICORN, language)
            ZodiacSign.AQUARIUS -> StringResources.get(StringKeyDashaInterpretationsPart1.KALACHAKRA_BRIEF_AQUARIUS, language)
            ZodiacSign.PISCES -> StringResources.get(StringKeyDashaInterpretationsPart1.KALACHAKRA_BRIEF_PISCES, language)
        }
    }

    // ============================================
    // UTILITY METHODS
    // ============================================

    /**
     * Get current period summary
     */
    fun getCurrentPeriodSummary(result: KalachakraDashaResult): String {
        val maha = result.currentMahadasha ?: return "No active Kalachakra Dasha period"
        val antar = result.currentAntardasha

        return buildString {
            appendLine("=== KALACHAKRA DASHA - CURRENT PERIOD ===")
            appendLine()
            appendLine("Nakshatra Group: ${result.nakshatraGroup.displayName}")
            appendLine("Deha (Body) Rashi: ${result.dehaRashi.displayName}")
            appendLine("Jeeva (Soul) Rashi: ${result.jeevaRashi.displayName}")
            appendLine()
            appendLine("Mahadasha: ${maha.sign.displayName} (${maha.durationYears} years)")
            appendLine("Health: ${maha.healthIndicator.displayName}")
            appendLine("Progress: ${String.format("%.1f", maha.getProgressPercent())}%")
            appendLine("Remaining: ${maha.getRemainingDays()} days")
            appendLine()
            if (antar != null) {
                appendLine("Antardasha: ${antar.sign.displayName}")
                if (antar.isDehaSign) appendLine("  * DEHA period - focus on physical matters")
                if (antar.isJeevaSign) appendLine("  * JEEVA period - focus on spiritual matters")
                appendLine("Progress: ${String.format("%.1f", antar.getProgressPercent())}%")
            }
        }
    }

    /**
     * Get Deha-Jeeva summary
     */
    fun getDehaJeevaSummary(result: KalachakraDashaResult): String {
        return buildString {
            appendLine("=== DEHA-JEEVA ANALYSIS ===")
            appendLine()
            appendLine("Deha (Body) Rashi: ${result.dehaRashi.displayName}")
            appendLine("Deha Lord: ${result.dehaJeevaAnalysis.dehaLord.displayName}")
            appendLine("Deha Lord Status: ${result.dehaJeevaAnalysis.dehaLordStrength}")
            appendLine()
            appendLine("Jeeva (Soul) Rashi: ${result.jeevaRashi.displayName}")
            appendLine("Jeeva Lord: ${result.dehaJeevaAnalysis.jeevaLord.displayName}")
            appendLine("Jeeva Lord Status: ${result.dehaJeevaAnalysis.jeevaLordStrength}")
            appendLine()
            appendLine("Relationship: ${result.dehaJeevaAnalysis.dehaJeevaRelationship.displayName}")
            appendLine(result.dehaJeevaAnalysis.dehaJeevaRelationship.description)
        }
    }

    private fun getLocalizedPlanetName(planet: Planet, language: Language): String {
        val key = when (planet) {
            Planet.SUN -> com.astro.storm.core.common.StringKeyPart1.PLANET_SUN
            Planet.MOON -> com.astro.storm.core.common.StringKeyPart1.PLANET_MOON
            Planet.MARS -> com.astro.storm.core.common.StringKeyPart1.PLANET_MARS
            Planet.MERCURY -> com.astro.storm.core.common.StringKeyPart1.PLANET_MERCURY
            Planet.JUPITER -> com.astro.storm.core.common.StringKeyPart1.PLANET_JUPITER
            Planet.VENUS -> com.astro.storm.core.common.StringKeyPart1.PLANET_VENUS
            Planet.SATURN -> com.astro.storm.core.common.StringKeyPart1.PLANET_SATURN
            Planet.RAHU -> com.astro.storm.core.common.StringKeyPart1.PLANET_RAHU
            Planet.KETU -> com.astro.storm.core.common.StringKeyPart1.PLANET_KETU
            else -> return planet.displayName
        }
        return StringResources.get(key, language)
    }
}

/**
 * Extension property for Planet exaltation sign
 */
private val Planet.exaltationSign: ZodiacSign?
    get() = when (this) {
        Planet.SUN -> ZodiacSign.ARIES
        Planet.MOON -> ZodiacSign.TAURUS
        Planet.MARS -> ZodiacSign.CAPRICORN
        Planet.MERCURY -> ZodiacSign.VIRGO
        Planet.JUPITER -> ZodiacSign.CANCER
        Planet.VENUS -> ZodiacSign.PISCES
        Planet.SATURN -> ZodiacSign.LIBRA
        else -> null
    }

/**
 * Extension property for Planet debilitation sign
 */
private val Planet.debilitationSign: ZodiacSign?
    get() = when (this) {
        Planet.SUN -> ZodiacSign.LIBRA
        Planet.MOON -> ZodiacSign.SCORPIO
        Planet.MARS -> ZodiacSign.CANCER
        Planet.MERCURY -> ZodiacSign.PISCES
        Planet.JUPITER -> ZodiacSign.CAPRICORN
        Planet.VENUS -> ZodiacSign.VIRGO
        Planet.SATURN -> ZodiacSign.ARIES
        else -> null
    }


