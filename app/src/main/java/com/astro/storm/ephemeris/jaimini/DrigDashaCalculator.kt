package com.astro.storm.ephemeris.jaimini

import com.astro.storm.core.model.Planet
import com.astro.storm.core.model.PlanetPosition
import com.astro.storm.core.model.VedicChart
import com.astro.storm.core.model.ZodiacSign
import com.astro.storm.ephemeris.VedicAstrologyUtils
import java.time.DateTimeException
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZoneOffset
import java.time.temporal.ChronoUnit
import kotlin.math.abs
import kotlin.math.roundToInt

/**
 * DrigDashaCalculator - Jaimini Drig (Sthira) Dasha System for Longevity
 *
 * Drig Dasha is one of the important conditional Nakshatra-based dashas in Jaimini astrology,
 * primarily used for determining longevity (Ayurdaya) and timing of significant life events.
 * It is also known as Sthira Dasha because it uses the fixed (sthira) significators.
 *
 * Key Principles:
 * 1. Starting Point: Depends on Lagna (odd/even sign)
 * 2. Order: Signs progress based on aspectual relationships (Drig = aspect/vision)
 * 3. Duration: Based on planetary influences on signs
 * 4. Focus: Longevity prediction, timing of health crises
 *
 * Three Longevity Spans (Ayur) - Standard Vedic Ranges:
 * - Bala Arishta: 0-8 years (infant mortality period)
 * - Alpayu (Short life): 8-32 years
 * - Madhyayu (Medium life): 32-70 years
 * - Purnayu (Full life): 70-100 years
 *
 * Calculation Methodology:
 * 1. Determine Drig Dasha starting sign from Lagna/9th house
 * 2. Calculate dasha years for each sign based on aspects
 * 3. Apply Brahma, Rudra, Maheshwara analysis
 * 4. Determine critical periods (Maraka periods)
 *
 * Vedic References:
 * - Jaimini Sutras (Chapter 2, Pada 4)
 * - Jaimini Upadesa Sutras
 * - Phala Ratnamala by Krishnamishra
 *
 * @author AstroStorm
 * @since 2026.02
 */
object DrigDashaCalculator {

    /**
     * Total dasha cycle duration in years (based on traditional Jaimini)
     */
    private const val TOTAL_CYCLE_YEARS = 120.0

    /**
     * Base years per sign (for standard calculation)
     */
    private const val BASE_YEARS_PER_SIGN = 9.0

    /**
     * Longevity classification
     */
    enum class AyurSpan(
        val displayName: String,
        val sanskritName: String,
        val minYears: Int,
        val maxYears: Int,
        val description: String
    ) {
        BALA_ARISHTA("Infant Mortality", "Bala Arishta", 0, 8, "Critical period in early childhood"),
        ALPAYU("Short Life", "Alpayu", 8, 32, "Life span between 8-32 years"),
        MADHYAYU("Medium Life", "Madhyayu", 32, 70, "Life span between 32-70 years"),
        PURNAYU("Full Life", "Purnayu", 70, 100, "Life span between 70-100 years")
    }

    /**
     * Sthira Karaka (Fixed Significator) for each house
     */
    enum class SthiraKaraka(
        val house: Int,
        val planet: Planet,
        val signification: String
    ) {
        FIRST(1, Planet.SUN, "Self, Vitality, Soul"),
        SECOND(2, Planet.JUPITER, "Wealth, Family, Speech"),
        THIRD(3, Planet.MARS, "Siblings, Courage, Arms"),
        FOURTH(4, Planet.MOON, "Mother, Mind, Home"),
        FIFTH(5, Planet.JUPITER, "Children, Intelligence, Merit"),
        SIXTH(6, Planet.SATURN, "Enemies, Disease, Service"),
        SEVENTH(7, Planet.VENUS, "Spouse, Partnership, Desire"),
        EIGHTH(8, Planet.SATURN, "Longevity, Hidden, Occult"),
        NINTH(9, Planet.JUPITER, "Father, Dharma, Fortune"),
        TENTH(10, Planet.MERCURY, "Karma, Profession, Status"),
        ELEVENTH(11, Planet.JUPITER, "Gains, Elder Siblings, Desires"),
        TWELFTH(12, Planet.SATURN, "Loss, Liberation, Foreign")
    }

    /**
     * Sthira Karaka Info for Analysis
     */
    data class SthiraKarakaInfo(
        val sthiraKaraka: SthiraKaraka,
        val position: PlanetPosition,
        val dignity: VedicAstrologyUtils.PlanetaryDignity,
        val strength: String
    )

    /**
     * Drig Dasha period for a sign
     */
    data class DrigDashaPeriod(
        val sign: ZodiacSign,
        val startDate: LocalDateTime,
        val endDate: LocalDateTime,
        val durationYears: Double,
        val signLord: Planet,
        val planetsInSign: List<Planet>,
        val aspectingPlanets: List<Planet>,
        val isMarakaPeriod: Boolean,
        val marakaReason: String?,
        val interpretation: String,
        val healthIndicators: List<String>,
        val criticalSubPeriods: List<DrigAntardasha>
    ) {
        val durationMonths: Double get() = durationYears * 12
        val durationDays: Long get() = (durationYears * 365.25).toLong()

        fun contains(date: LocalDateTime): Boolean {
            return !date.isBefore(startDate) && date.isBefore(endDate)
        }
    }

    /**
     * Drig Antardasha (sub-period)
     */
    data class DrigAntardasha(
        val sign: ZodiacSign,
        val startDate: LocalDateTime,
        val endDate: LocalDateTime,
        val durationMonths: Double,
        val isCritical: Boolean,
        val criticalReason: String?
    )

    /**
     * Complete Drig Dasha analysis with full explainability
     */
    data class DrigDashaAnalysis(
        val chartId: String,
        val birthDateTime: LocalDateTime,
        val drigDashaSequence: List<DrigDashaPeriod>,
        val currentDasha: DrigDashaPeriod?,
        val upcomingDashas: List<DrigDashaPeriod>,
        val longevitySpan: AyurSpan,
        val estimatedLongevity: Double,
        val brahmaSign: ZodiacSign,
        val rudraSign: ZodiacSign,
        val maheshwaraSign: ZodiacSign,
        val criticalPeriods: List<CriticalPeriod>,
        val longevityAnalysis: LongevityAnalysis,
        val sthiraKarakas: List<SthiraKarakaInfo> = emptyList(),
        val timestamp: Long = System.currentTimeMillis()
    ) {
        /**
         * Get complete explanation of the analysis
         */
        fun getFullExplanation(): String = longevityAnalysis.generateExplanation()
    }

    /**
     * Critical period identification
     */
    data class CriticalPeriod(
        val startDate: LocalDateTime,
        val endDate: LocalDateTime,
        val sign: ZodiacSign,
        val reason: String,
        val severity: Int, // 1-5
        val remedies: List<String>
    )

    /**
     * Detailed longevity analysis with rule provenance
     */
    data class LongevityAnalysis(
        val lagnaPairSpan: AyurSpan,
        val moonPairSpan: AyurSpan,
        val saturnPairSpan: AyurSpan,
        val finalSpan: AyurSpan,
        val kakshyaContribution: Double,
        val factors: List<LongevityFactor>,
        val appliedRules: List<RuleApplication> = emptyList()
    ) {
        /**
         * Generate detailed explanation of longevity calculation
         */
        fun generateExplanation(): String = buildString {
            appendLine("═══════════════════════════════════════════")
            appendLine("JAIMINI DRIG DASHA LONGEVITY ANALYSIS")
            appendLine("═══════════════════════════════════════════")
            appendLine()
            appendLine("Final Longevity Span: ${finalSpan.displayName} (${finalSpan.sanskritName})")
            appendLine("Estimated Range: ${finalSpan.minYears}-${finalSpan.maxYears} years")
            if (kakshyaContribution != 0.0) {
                appendLine("Adjustment: ${if (kakshyaContribution > 0) "+" else ""}${kakshyaContribution.toInt()} years")
            }
            appendLine()
            appendLine("Classical Rules Applied:")
            appendLine()

            appliedRules.forEachIndexed { index, rule ->
                appendLine("${index + 1}. ${rule.ruleName}")
                if (rule.sutraReference.isNotBlank()) {
                    appendLine("   Reference: ${rule.sutraReference}")
                }
                appendLine("   Observation: ${rule.observation}")
                appendLine("   Result: ${rule.result}")
                appendLine()
            }

            appendLine("Supporting Factors:")
            factors.forEach { factor ->
                appendLine("• ${factor.name}: ${factor.conclusion}")
            }
        }
    }

    /**
     * Individual longevity factor with rule provenance
     */
    data class LongevityFactor(
        val name: String,
        val contribution: Double, // Positive or negative years
        val description: String,
        val ruleSource: String = "",
        val sutraReference: String = "",
        val observation: String = "",
        val conclusion: String = ""
    )

    /**
     * Rule application record for explainability
     */
    data class RuleApplication(
        val ruleName: String,
        val sutraReference: String,
        val condition: String,
        val observation: String,
        val result: String,
        val weight: Double
    ) {
        fun toExplanation(): String = buildString {
            append("📜 $ruleName")
            if (sutraReference.isNotBlank()) append(" [$sutraReference]")
            appendLine()
            appendLine("   Condition: $condition")
            appendLine("   Observation: $observation")
            appendLine("   Result: $result")
        }
    }

    /**
     * Calculate complete Drig Dasha analysis
     *
     * @param chart The birth chart
     * @param currentDate Current date for determining active dasha
     * @return Complete DrigDashaAnalysis
     */
    fun calculateDrigDasha(
        chart: VedicChart,
        currentDate: LocalDateTime? = null
    ): DrigDashaAnalysis {
        val birthDateTime = chart.birthData.dateTime
        val effectiveCurrentDate = currentDate ?: LocalDateTime.now(resolveZoneId(chart.birthData.timezone))
        val lagnaSign = VedicAstrologyUtils.getAscendantSign(chart)

        // Calculate Brahma, Rudra, Maheshwara signs
        val brahma = calculateBrahmaSign(chart)
        val rudra = calculateRudraSign(chart, brahma)
        val maheshwara = calculateMaheshwaraSign(chart)

        // Calculate longevity span
        val longevityAnalysis = calculateLongevitySpan(chart)
        val longevitySpan = longevityAnalysis.finalSpan

        // Calculate dasha sequence
        val dashSequence = calculateDashaSequence(chart, lagnaSign, birthDateTime)

        // Find current and upcoming dashas
        val currentDasha = dashSequence.find { it.contains(effectiveCurrentDate) }
        val upcomingDashas = dashSequence.filter { it.startDate.isAfter(effectiveCurrentDate) }.take(3)

        // Identify critical periods
        val criticalPeriods = identifyCriticalPeriods(dashSequence, chart, rudra, maheshwara)

        // Calculate estimated longevity
        val estimatedLongevity = calculateEstimatedLongevity(longevityAnalysis, chart)

        return DrigDashaAnalysis(
            chartId = generateChartId(chart),
            birthDateTime = birthDateTime,
            drigDashaSequence = dashSequence,
            currentDasha = currentDasha,
            upcomingDashas = upcomingDashas,
            longevitySpan = longevitySpan,
            estimatedLongevity = estimatedLongevity,
            brahmaSign = brahma,
            rudraSign = rudra,
            maheshwaraSign = maheshwara,
            criticalPeriods = criticalPeriods,
            longevityAnalysis = longevityAnalysis
        )
    }

    private fun resolveZoneId(timezone: String): ZoneId {
        return try {
            ZoneId.of(timezone)
        } catch (_: DateTimeException) {
            val numericHours = timezone.trim().toDoubleOrNull()
            if (numericHours != null) {
                ZoneOffset.ofTotalSeconds((numericHours * 3600.0).roundToInt().coerceIn(-18 * 3600, 18 * 3600))
            } else {
                throw IllegalArgumentException("Invalid timezone: $timezone")
            }
        }
    }

    /**
     * Calculate Brahma - the creator significator
     *
     * Brahma is determined from the stronger of:
     * - 6th from Lagna
     * - 6th from 7th (i.e., 12th from Lagna)
     *
     * The stronger sign between these becomes Brahma.
     */
    private fun calculateBrahmaSign(chart: VedicChart): ZodiacSign {
        val lagnaSign = VedicAstrologyUtils.getAscendantSign(chart)

        // 6th from Lagna
        val sixthSign = getSignFromLagna(lagnaSign, 6)
        // 12th from Lagna (6th from 7th)
        val twelfthSign = getSignFromLagna(lagnaSign, 12)

        val sixthStrength = calculateSignStrength(chart, sixthSign)
        val twelfthStrength = calculateSignStrength(chart, twelfthSign)

        return if (sixthStrength >= twelfthStrength) sixthSign else twelfthSign
    }

    /**
     * Calculate Rudra - the destroyer significator
     *
     * Rudra is the 8th from Brahma or the stronger maraka sign.
     */
    private fun calculateRudraSign(chart: VedicChart, brahma: ZodiacSign): ZodiacSign {
        // 8th from Brahma
        val eighthFromBrahma = getSignFromLagna(brahma, 8)

        // Also check 2nd and 7th from Lagna (maraka houses)
        val lagnaSign = VedicAstrologyUtils.getAscendantSign(chart)
        val secondSign = getSignFromLagna(lagnaSign, 2)
        val seventhSign = getSignFromLagna(lagnaSign, 7)

        // Choose the strongest among these
        val candidates = listOf(eighthFromBrahma, secondSign, seventhSign)
        return candidates.maxByOrNull { calculateSignStrength(chart, it) } ?: eighthFromBrahma
    }

    /**
     * Calculate Maheshwara - the supreme controller
     *
     * Maheshwara is determined by the 8th lord's position and strength.
     */
    private fun calculateMaheshwaraSign(chart: VedicChart): ZodiacSign {
        val lagnaSign = VedicAstrologyUtils.getAscendantSign(chart)
        val eighthSign = getSignFromLagna(lagnaSign, 8)
        val eighthLord = eighthSign.ruler

        // Find where 8th lord is placed
        val eighthLordPos = chart.planetPositions.find { it.planet == eighthLord }
        return eighthLordPos?.sign ?: eighthSign
    }

    /**
     * Calculate the complete dasha sequence
     */
    private fun calculateDashaSequence(
        chart: VedicChart,
        lagnaSign: ZodiacSign,
        birthDate: LocalDateTime
    ): List<DrigDashaPeriod> {
        val sequence = mutableListOf<DrigDashaPeriod>()
        var currentDate = birthDate

        // Determine starting sign based on odd/even Lagna
        val isOddLagna = lagnaSign.number % 2 == 1
        val startSign = if (isOddLagna) lagnaSign else getSignFromLagna(lagnaSign, 7)

        // Generate dasha for all 12 signs
        val signOrder = getDrigDashaSignOrder(startSign, isOddLagna)

        for (sign in signOrder) {
            val duration = calculateSignDashaDuration(chart, sign)
            val endDate = currentDate.plusDays((duration * 365.25).toLong())

            val signLord = sign.ruler
            val planetsInSign = chart.planetPositions
                .filter { it.sign == sign }
                .map { it.planet }
            val aspectingPlanets = getAspectingPlanets(chart, sign)

            val isMaraka = isMarakaPeriod(chart, sign)
            val marakaReason = if (isMaraka) getMarakaReason(chart, sign) else null

            val interpretation = buildDashaPeriodInterpretation(sign, signLord, planetsInSign, isMaraka)
            val healthIndicators = getHealthIndicators(chart, sign)
            val antardashas = calculateAntardashas(sign, currentDate, endDate, chart)

            sequence.add(
                DrigDashaPeriod(
                    sign = sign,
                    startDate = currentDate,
                    endDate = endDate,
                    durationYears = duration,
                    signLord = signLord,
                    planetsInSign = planetsInSign,
                    aspectingPlanets = aspectingPlanets,
                    isMarakaPeriod = isMaraka,
                    marakaReason = marakaReason,
                    interpretation = interpretation,
                    healthIndicators = healthIndicators,
                    criticalSubPeriods = antardashas
                )
            )

            currentDate = endDate
        }

        return sequence
    }

    /**
     * Get dasha sign order based on starting sign and Lagna type
     *
     * CRITICAL: Drig Dasha (Sthira Dasha) progresses by TRINES, not sequentially.
     * According to Jaimini Sutras (2.4):
     * - The dasha moves by trinal progression (every 4th sign: 1-5-9, 2-6-10, 3-7-11, 4-8-12)
     * - For odd Lagna: Forward trine progression
     * - For even Lagna: Reverse trine progression
     */
    private fun getDrigDashaSignOrder(startSign: ZodiacSign, isOddLagna: Boolean): List<ZodiacSign> {
        val allSigns = ZodiacSign.entries
        val startIndex = allSigns.indexOf(startSign)
        val result = mutableListOf<ZodiacSign>()

        if (isOddLagna) {
            // Forward trine progression for odd signs
            // Pattern: Start, +4, +4, then move to next sign, +4, +4...
            val trineSets = listOf(
                listOf(0, 4, 8),    // 1st trine: Start, 5th, 9th
                listOf(1, 5, 9),    // 2nd trine: 2nd, 6th, 10th
                listOf(2, 6, 10),   // 3rd trine: 3rd, 7th, 11th
                listOf(3, 7, 11)    // 4th trine: 4th, 8th, 12th
            )

            for (trineSet in trineSets) {
                for (offset in trineSet) {
                    val signIndex = (startIndex + offset) % 12
                    result.add(allSigns[signIndex])
                }
            }
        } else {
            // Reverse trine progression for even signs
            // Pattern: Start, -4, -4, then move to previous sign, -4, -4...
            val trineSets = listOf(
                listOf(0, -4, -8),      // 1st trine: Start, 9th (back), 5th (back)
                listOf(-1, -5, -9),     // 2nd trine: 12th, 8th, 4th
                listOf(-2, -6, -10),    // 3rd trine: 11th, 7th, 3rd
                listOf(-3, -7, -11)     // 4th trine: 10th, 6th, 2nd
            )

            for (trineSet in trineSets) {
                for (offset in trineSet) {
                    val signIndex = (startIndex + offset + 12) % 12
                    result.add(allSigns[signIndex])
                }
            }
        }

        return result
    }

    /**
     * Calculate dasha duration for a sign
     *
     * Duration = Base years + modifications based on:
     * - Planets in the sign
     * - Aspects on the sign
     * - Sign lord's condition
     */
    private fun calculateSignDashaDuration(chart: VedicChart, sign: ZodiacSign): Double {
        var duration = BASE_YEARS_PER_SIGN

        // Count planets in sign (each planet adds/modifies duration)
        val planetsInSign = chart.planetPositions.filter { it.sign == sign }
        duration += planetsInSign.size * 0.5

        // Benefic planets add slightly more
        val beneficCount = planetsInSign.count {
            it.planet in listOf(Planet.JUPITER, Planet.VENUS, Planet.MERCURY, Planet.MOON)
        }
        duration += beneficCount * 0.25

        // Sign lord condition
        val signLord = sign.ruler
        val lordPos = chart.planetPositions.find { it.planet == signLord }
        if (lordPos != null) {
            val dignity = VedicAstrologyUtils.getDignity(lordPos)
            duration += when (dignity) {
                VedicAstrologyUtils.PlanetaryDignity.EXALTED -> 1.0
                VedicAstrologyUtils.PlanetaryDignity.OWN_SIGN -> 0.5
                VedicAstrologyUtils.PlanetaryDignity.DEBILITATED -> -0.5
                else -> 0.0
            }
        }

        return duration.coerceIn(3.0, 15.0)
    }

    /**
     * Calculate Antardashas (sub-periods) within a Dasha
     */
    private fun calculateAntardashas(
        mainSign: ZodiacSign,
        startDate: LocalDateTime,
        endDate: LocalDateTime,
        chart: VedicChart
    ): List<DrigAntardasha> {
        val antardashas = mutableListOf<DrigAntardasha>()
        val totalMonths = ChronoUnit.DAYS.between(startDate, endDate) / 30.44

        // Sub-periods for each sign starting from main sign
        val subSigns = (0 until 12).map {
            ZodiacSign.entries[(mainSign.ordinal + it) % 12]
        }

        var currentStart = startDate
        val monthsPerSub = totalMonths / 12

        for (subSign in subSigns) {
            val subEnd = currentStart.plusDays((monthsPerSub * 30.44).toLong())
            val isCritical = isMarakaPeriod(chart, subSign)

            antardashas.add(
                DrigAntardasha(
                    sign = subSign,
                    startDate = currentStart,
                    endDate = subEnd,
                    durationMonths = monthsPerSub,
                    isCritical = isCritical,
                    criticalReason = if (isCritical) "Maraka influence on ${subSign.displayName}" else null
                )
            )

            currentStart = subEnd
        }

        return antardashas
    }

    /**
     * Calculate longevity span using classical Jaimini Sutra methods
     *
     * Based on Jaimini Sutras Chapter 2, Pada 4 (Ayurdaya Adhyaya):
     * - Rule 1: Lagna-Lord and 8th-Lord placement (2.4.1-3)
     * - Rule 2: Moon-Saturn mutual placement (2.4.4-6)
     * - Rule 3: Saturn's dignity and placement (2.4.7-9)
     * - Rule 4: Hora Lagna position (2.4.10-12)
     * - Rule 5: Benefic-Malefic balance on 1st, 8th, 10th houses
     */
    private fun calculateLongevitySpan(chart: VedicChart): LongevityAnalysis {
        val factors = mutableListOf<LongevityFactor>()
        val appliedRules = mutableListOf<RuleApplication>()

        // Rule 1: Lagna-Lord and 8th-Lord placement (Jaimini 2.4.1-3)
        val lagnaPairResult = calculateLagnaPairLongevityClassical(chart, appliedRules)
        factors.add(lagnaPairResult.factor)

        // Rule 2: Moon-Saturn mutual placement (Jaimini 2.4.4-6)
        val moonSaturnResult = calculateMoonSaturnLongevityClassical(chart, appliedRules)
        factors.add(moonSaturnResult.factor)

        // Rule 3: Saturn's dignity and placement (Jaimini 2.4.7-9)
        val saturnResult = calculateSaturnLongevityClassical(chart, appliedRules)
        factors.add(saturnResult.factor)

        // Rule 4: Hora Lagna (Jaimini 2.4.10-12)
        val horaResult = calculateHoraLagnaLongevity(chart, appliedRules)
        factors.add(horaResult.factor)

        // Rule 5: Benefic-Malefic strength analysis
        val beneficResult = calculateBeneficMaleficBalance(chart, appliedRules)
        factors.add(beneficResult.factor)

        // Final determination using weighted majority
        val spans = listOf(
            lagnaPairResult.span to lagnaPairResult.weight,
            moonSaturnResult.span to moonSaturnResult.weight,
            saturnResult.span to saturnResult.weight,
            horaResult.span to horaResult.weight,
            beneficResult.span to beneficResult.weight
        )

        val finalSpan = determineFinalLongevityByWeightedMajority(spans, appliedRules)

        // Calculate precise contribution based on applied rules
        val kakshya = calculateKakshyaFromRules(appliedRules)

        return LongevityAnalysis(
            lagnaPairSpan = lagnaPairResult.span,
            moonPairSpan = moonSaturnResult.span,
            saturnPairSpan = saturnResult.span,
            finalSpan = finalSpan,
            kakshyaContribution = kakshya,
            factors = factors,
            appliedRules = appliedRules
        )
    }

    /**
     * Data class for longevity calculation result
     */
    private data class LongevityResult(
        val span: AyurSpan,
        val weight: Double,
        val factor: LongevityFactor
    )

    /**
     * Rule 1: Lagna-Lord and 8th-Lord placement (Jaimini 2.4.1-3)
     *
     * Sutra: "Lagnadhipashtamashtameshu kendratrikonayorayuh"
     * If Lagna Lord and 8th Lord are in Kendra (1,4,7,10) or Trikona (5,9) = Purnayu
     * If in Panapara (2,5,8,11) = Madhyayu
     * If in Apoklima (3,6,9,12) = Alpayu
     */
    private fun calculateLagnaPairLongevityClassical(
        chart: VedicChart,
        rules: MutableList<RuleApplication>
    ): LongevityResult {
        val lagnaSign = VedicAstrologyUtils.getAscendantSign(chart)
        val lagnaLord = lagnaSign.ruler
        val lagnaLordPos = chart.planetPositions.find { it.planet == lagnaLord }
        val lagnaLordHouse = lagnaLordPos?.house ?: 1

        val eighthSign = getSignFromLagna(lagnaSign, 8)
        val eighthLord = eighthSign.ruler
        val eighthLordPos = chart.planetPositions.find { it.planet == eighthLord }
        val eighthLordHouse = eighthLordPos?.house ?: 8

        // Both lords' positions determine longevity
        val lagnaLordKendra = lagnaLordHouse in listOf(1, 4, 7, 10)
        val lagnaLordTrikona = lagnaLordHouse in listOf(5, 9)
        val lagnaLordPanapara = lagnaLordHouse in listOf(2, 5, 8, 11)
        val lagnaLordApoklima = lagnaLordHouse in listOf(3, 6, 9, 12)

        val eighthLordKendra = eighthLordHouse in listOf(1, 4, 7, 10)
        val eighthLordTrikona = eighthLordHouse in listOf(5, 9)
        val eighthLordPanapara = eighthLordHouse in listOf(2, 5, 8, 11)
        val eighthLordApoklima = eighthLordHouse in listOf(3, 6, 9, 12)

        val (span, explanation) = when {
            // Both in Kendra/Trikona = Purnayu
            (lagnaLordKendra || lagnaLordTrikona) && (eighthLordKendra || eighthLordTrikona) ->
                AyurSpan.PURNAYU to "Both Lagna lord ($lagnaLord in house $lagnaLordHouse) and 8th lord ($eighthLord in house $eighthLordHouse) are in Kendra/Trikona"

            // Both in Apoklima = Alpayu
            lagnaLordApoklima && eighthLordApoklima ->
                AyurSpan.ALPAYU to "Both Lagna lord and 8th lord are in Apoklima (3,6,12)"

            // Mixed or both in Panapara = Madhyayu
            else -> AyurSpan.MADHYAYU to "Lagna lord in house $lagnaLordHouse, 8th lord in house $eighthLordHouse"
        }

        rules.add(RuleApplication(
            ruleName = "Lagna-8th Lord Placement",
            sutraReference = "Jaimini 2.4.1-3",
            condition = "Check if Lagna lord and 8th lord are in Kendra (1,4,7,10), Trikona (5,9), Panapara (2,5,8,11), or Apoklima (3,6,12)",
            observation = "Lagna lord ($lagnaLord) in house $lagnaLordHouse; 8th lord ($eighthLord) in house $eighthLordHouse",
            result = span.name,
            weight = 25.0
        ))

        return LongevityResult(
            span = span,
            weight = 25.0,
            factor = LongevityFactor(
                name = "Lagna-8th Lord Placement",
                contribution = span.maxYears.toDouble(),
                description = explanation,
                ruleSource = "Jaimini Sutras 2.4.1-3",
                sutraReference = "Lagnadhipashtamashtameshu kendratrikonayorayuh",
                observation = "Lagna lord in H$lagnaLordHouse, 8th lord in H$eighthLordHouse",
                conclusion = span.displayName
            )
        )
    }

    /**
     * Rule 2: Moon-Saturn mutual placement (Jaimini 2.4.4-6)
     *
     * Sutra: "Chandramandalarashisthairayuh"
     * Moon and Saturn's mutual angular relationship determines longevity:
     * - Same sign or 4th/10th from each other = Alpayu
     * - 5th/9th from each other = Purnayu
     * - Other angles = Madhyayu
     */
    private fun calculateMoonSaturnLongevityClassical(
        chart: VedicChart,
        rules: MutableList<RuleApplication>
    ): LongevityResult {
        val moonPos = chart.planetPositions.find { it.planet == Planet.MOON }
        val saturnPos = chart.planetPositions.find { it.planet == Planet.SATURN }

        val observation: String
        val span: AyurSpan
        val conclusion: String

        if (moonPos == null || saturnPos == null) {
            observation = "Moon or Saturn position not available"
            span = AyurSpan.MADHYAYU
            conclusion = "Insufficient data for Moon-Saturn longevity rule"
            rules.add(
                RuleApplication(
                    ruleName = "Moon-Saturn Mutual Placement",
                    sutraReference = "Jaimini 2.4.4-6",
                    condition = "Check Moon-Saturn angular distance: 0 or 3=Alpayu | 4=Purnayu | Others=Madhyayu",
                    observation = observation,
                    result = "INSUFFICIENT_DATA",
                    weight = 0.0
                )
            )
            return LongevityResult(
                span = span,
                weight = 0.0,
                factor = LongevityFactor(
                    name = "Moon-Saturn Placement",
                    contribution = 0.0,
                    description = observation,
                    ruleSource = "Jaimini Sutras 2.4.4-6",
                    sutraReference = "Chandramandalarashisthairayuh",
                    observation = observation,
                    conclusion = conclusion
                )
            )
        } else {
            val moonHouse = moonPos.house
            val saturnHouse = saturnPos.house

            // Calculate angular distance (0..6)
            var distance = abs(moonHouse - saturnHouse)
            if (distance > 6) distance = 12 - distance

            observation = "Moon in H$moonHouse, Saturn in H$saturnHouse (distance: $distance houses)"

            span = when (distance) {
                0 -> { // Conjunction
                    conclusion = "Moon-Saturn conjunction - severe affliction to longevity"
                    AyurSpan.ALPAYU
                }
                3 -> { // 4th/10th (kendra)
                    conclusion = "Moon-Saturn in kendra (4th/10th) - reduced longevity"
                    AyurSpan.ALPAYU
                }
                4 -> { // 5th/9th (trikona)
                    conclusion = "Moon-Saturn in trikona (5th/9th) - full longevity indicated"
                    AyurSpan.PURNAYU
                }
                else -> {
                    conclusion = "Moon-Saturn in other angles - medium longevity"
                    AyurSpan.MADHYAYU
                }
            }
        }

        rules.add(RuleApplication(
            ruleName = "Moon-Saturn Mutual Placement",
            sutraReference = "Jaimini 2.4.4-6",
            condition = "Check Moon-Saturn angular distance: 0 or 3=Alpayu | 4=Purnayu | Others=Madhyayu",
            observation = observation,
            result = span.name,
            weight = 20.0
        ))

        return LongevityResult(
            span = span,
            weight = 20.0,
            factor = LongevityFactor(
                name = "Moon-Saturn Placement",
                contribution = span.maxYears.toDouble(),
                description = observation,
                ruleSource = "Jaimini Sutras 2.4.4-6",
                sutraReference = "Chandramandalarashisthairayuh",
                observation = observation,
                conclusion = conclusion
            )
        )
    }

    /**
     * Rule 3: Saturn's dignity and placement (Jaimini 2.4.7-9)
     *
     * Saturn as Ayushkaraka (significator of longevity):
     * - Exalted/Own/Moolatrikona in Kendra/Trikona = Purnayu
     * - Neutral dignity = Madhyayu
     * - Debilitated or in dusthana = Alpayu
     */
    private fun calculateSaturnLongevityClassical(
        chart: VedicChart,
        rules: MutableList<RuleApplication>
    ): LongevityResult {
        val saturnPos = chart.planetPositions.find { it.planet == Planet.SATURN }

        val observation: String
        val span: AyurSpan
        val conclusion: String

        if (saturnPos == null) {
            observation = "Saturn position not available"
            span = AyurSpan.MADHYAYU
            conclusion = "Insufficient data for Saturn longevity rule"
            rules.add(
                RuleApplication(
                    ruleName = "Saturn (Ayushkaraka) Analysis",
                    sutraReference = "Jaimini 2.4.7-9",
                    condition = "Saturn's dignity and placement - Kendra/Trikona with good dignity = Purnayu",
                    observation = observation,
                    result = "INSUFFICIENT_DATA",
                    weight = 0.0
                )
            )
            return LongevityResult(
                span = span,
                weight = 0.0,
                factor = LongevityFactor(
                    name = "Saturn (Ayushkaraka)",
                    contribution = 0.0,
                    description = observation,
                    ruleSource = "Jaimini Sutras 2.4.7-9",
                    sutraReference = "Mandaleshu kendratrikonayorayuh",
                    observation = observation,
                    conclusion = conclusion
                )
            )
        } else {
            val saturnHouse = saturnPos.house
            val dignity = VedicAstrologyUtils.getDignity(saturnPos)
            val dignityStr = dignity.name.lowercase().replace("_", " ")

            observation = "Saturn in H$saturnHouse with $dignityStr dignity"

            span = when {
                // Exalted/Own in Kendra/Trikona = Purnayu
                dignity in listOf(
                    VedicAstrologyUtils.PlanetaryDignity.EXALTED,
                    VedicAstrologyUtils.PlanetaryDignity.OWN_SIGN,
                    VedicAstrologyUtils.PlanetaryDignity.MOOLATRIKONA
                ) && saturnHouse in listOf(1, 4, 5, 7, 9, 10) -> {
                    conclusion = "Saturn strong in Kendra/Trikona - excellent longevity"
                    AyurSpan.PURNAYU
                }
                // Debilitated or in 6,8,12 = Alpayu
                dignity == VedicAstrologyUtils.PlanetaryDignity.DEBILITATED || saturnHouse in listOf(6, 8, 12) -> {
                    conclusion = "Saturn weak (debilitated or in dusthana) - reduced longevity"
                    AyurSpan.ALPAYU
                }
                // Neutral = Madhyayu
                else -> {
                    conclusion = "Saturn moderately placed - medium longevity"
                    AyurSpan.MADHYAYU
                }
            }
        }

        rules.add(RuleApplication(
            ruleName = "Saturn (Ayushkaraka) Analysis",
            sutraReference = "Jaimini 2.4.7-9",
            condition = "Saturn's dignity and placement - Kendra/Trikona with good dignity = Purnayu",
            observation = observation,
            result = span.name,
            weight = 20.0
        ))

        return LongevityResult(
            span = span,
            weight = 20.0,
            factor = LongevityFactor(
                name = "Saturn (Ayushkaraka)",
                contribution = span.maxYears.toDouble(),
                description = observation,
                ruleSource = "Jaimini Sutras 2.4.7-9",
                sutraReference = "Mandaleshu kendratrikonayorayuh",
                observation = observation,
                conclusion = conclusion
            )
        )
    }

    /**
     * Rule 4: Hora Lagna position (Jaimini 2.4.10-12)
     *
     * Hora Lagna's position relative to Lagna determines longevity:
     * - Hora Lagna in 1st, 4th, 7th, 10th house from Lagna = Purnayu
     * - In 5th, 9th = Madhyayu
     * - In 3rd, 6th, 11th = Alpayu
     */
    private fun calculateHoraLagnaLongevity(
        chart: VedicChart,
        rules: MutableList<RuleApplication>
    ): LongevityResult {
        // Calculate Hora Lagna
        val lagnaSign = VedicAstrologyUtils.getAscendantSign(chart)
        val lagnaLongitude = chart.ascendant

        // Hora Lagna calculation: if Lagna in odd sign, same as Lagna; if even, 7th from Lagna
        val horaLagnaSign = if (lagnaSign.number % 2 == 1) lagnaSign else getSignFromLagna(lagnaSign, 7)

        // Find Hora Lagna house from Lagna
        val horaLagnaHouse = ((horaLagnaSign.number - lagnaSign.number + 12) % 12) + 1

        val observation = "Hora Lagna ($horaLagnaSign) is in $horaLagnaHouse house from Lagna"

        val span = when (horaLagnaHouse) {
            1, 4, 7, 10 -> {
                AyurSpan.PURNAYU
            }
            5, 9 -> {
                AyurSpan.MADHYAYU
            }
            3, 6, 11 -> {
                AyurSpan.ALPAYU
            }
            else -> {
                AyurSpan.MADHYAYU
            }
        }

        val conclusion = when (span) {
            AyurSpan.PURNAYU -> "Hora Lagna in Kendra - full longevity indicated"
            AyurSpan.MADHYAYU -> "Hora Lagna in Trikona - medium longevity"
            AyurSpan.ALPAYU -> "Hora Lagna in 3rd/6th/11th - reduced longevity"
            else -> "Hora Lagna analysis"
        }

        rules.add(RuleApplication(
            ruleName = "Hora Lagna Position",
            sutraReference = "Jaimini 2.4.10-12",
            condition = "Hora Lagna in Kendra(1,4,7,10)=Purnayu | Trikona(5,9)=Madhyayu | 3,6,11=Alpayu",
            observation = observation,
            result = span.name,
            weight = 15.0
        ))

        return LongevityResult(
            span = span,
            weight = 15.0,
            factor = LongevityFactor(
                name = "Hora Lagna",
                contribution = span.maxYears.toDouble(),
                description = observation,
                ruleSource = "Jaimini Sutras 2.4.10-12",
                sutraReference = "Horalagnam kendratrikonayorayuh",
                observation = observation,
                conclusion = conclusion
            )
        )
    }

    /**
     * Rule 5: Benefic-Malefic balance on longevity houses (1st, 8th, 10th)
     */
    private fun calculateBeneficMaleficBalance(
        chart: VedicChart,
        rules: MutableList<RuleApplication>
    ): LongevityResult {
        val benefics = listOf(Planet.JUPITER, Planet.VENUS, Planet.MERCURY, Planet.MOON)
        val malefics = listOf(Planet.SATURN, Planet.MARS, Planet.RAHU, Planet.KETU, Planet.SUN)

        val longevityHouses = listOf(1, 8, 10)

        var beneficScore = 0
        var maleficScore = 0
        val observations = mutableListOf<String>()

        for (house in longevityHouses) {
            val planetsInHouse = chart.planetPositions.filter { it.house == house }
            val houseBenefics = planetsInHouse.count { it.planet in benefics }
            val houseMalefics = planetsInHouse.count { it.planet in malefics }

            beneficScore += houseBenefics
            maleficScore += houseMalefics

            if (houseBenefics > 0 || houseMalefics > 0) {
                observations.add("H$house: $houseBenefics benefic(s), $houseMalefics malefic(s)")
            }
        }

        val totalPlanets = beneficScore + maleficScore
        val beneficRatio = if (totalPlanets > 0) beneficScore.toDouble() / totalPlanets else 0.5

        val span = when {
            beneficRatio >= 0.6 -> AyurSpan.PURNAYU
            beneficRatio >= 0.4 -> AyurSpan.MADHYAYU
            else -> AyurSpan.ALPAYU
        }

        val observation = observations.joinToString("; ").ifEmpty { "No planets in longevity houses (1,8,10)" }

        rules.add(RuleApplication(
            ruleName = "Benefic-Malefic Balance",
            sutraReference = "Jaimini 2.4.13-15",
            condition = "Benefics in houses 1,8,10 support longevity; malefics reduce it",
            observation = "$observation (Benefic ratio: ${(beneficRatio * 100).toInt()}%)",
            result = span.name,
            weight = 20.0
        ))

        return LongevityResult(
            span = span,
            weight = 20.0,
            factor = LongevityFactor(
                name = "Benefic-Malefic Balance",
                contribution = span.maxYears.toDouble(),
                description = "Benefic ratio: ${(beneficRatio * 100).toInt()}% in longevity houses",
                ruleSource = "Jaimini Sutras 2.4.13-15",
                sutraReference = "Shubhapapayorayuh",
                observation = observation,
                conclusion = when (span) {
                    AyurSpan.PURNAYU -> "Strong benefic influence on longevity houses"
                    AyurSpan.MADHYAYU -> "Mixed influence on longevity houses"
                    AyurSpan.ALPAYU -> "Strong malefic influence on longevity houses"
                    else -> "Neutral influence"
                }
            )
        )
    }

    /**
     * Determine final longevity using weighted majority
     */
    private fun determineFinalLongevityByWeightedMajority(
        spans: List<Pair<AyurSpan, Double>>,
        rules: MutableList<RuleApplication>
    ): AyurSpan {
        // Group by span and sum weights
        val weightedCounts = spans.groupBy { it.first }
            .mapValues { entry -> entry.value.sumOf { it.second } }

        val totalWeight = weightedCounts.values.sum()
        if (totalWeight <= 0.0) {
            rules.add(
                RuleApplication(
                    ruleName = "Final Longevity Determination",
                    sutraReference = "Jaimini 2.4 - Majority Method",
                    condition = "Weighted majority of all longevity indicators",
                    observation = "No weighted rules available; defaulting to MADHYAYU baseline",
                    result = AyurSpan.MADHYAYU.name,
                    weight = 0.0
                )
            )
            return AyurSpan.MADHYAYU
        }

        // Find span with highest weight
        val entry = weightedCounts.maxByOrNull { it.value }
        val finalSpan = entry?.key ?: AyurSpan.MADHYAYU
        val maxWeight = entry?.value ?: 0.0

        val percentage = (maxWeight / totalWeight * 100).toInt()

        rules.add(RuleApplication(
            ruleName = "Final Longevity Determination",
            sutraReference = "Jaimini 2.4 - Majority Method",
            condition = "Weighted majority of all longevity indicators",
            observation = "Total weight: $totalWeight, Winning span: ${finalSpan.name} with ${maxWeight.toInt()} weight ($percentage%)",
            result = finalSpan.name,
            weight = totalWeight
        ))

        return finalSpan
    }

    /**
     * Calculate Kakshya from applied rules
     */
    private fun calculateKakshyaFromRules(rules: List<RuleApplication>): Double {
        // Fine-tuning based on rule applications
        var adjustment = 0.0

        // Check for strong benefic influences
        rules.find { it.ruleName == "Benefic-Malefic Balance" }?.let {
            if (it.result == "PURNAYU") adjustment += 4.0
            if (it.result == "ALPAYU") adjustment -= 4.0
        }

        // Check for Saturn strength
        rules.find { it.ruleName == "Saturn (Ayushkaraka) Analysis" }?.let {
            if (it.result == "PURNAYU") adjustment += 3.0
            if (it.result == "ALPAYU") adjustment -= 3.0
        }

        return adjustment
    }

    /**
     * Calculate estimated longevity in years based on classical midpoints
     */
    private fun calculateEstimatedLongevity(analysis: LongevityAnalysis, chart: VedicChart): Double {
        // Standard Vedic longevity midpoints based on classical texts
        val baseYears = when (analysis.finalSpan) {
            AyurSpan.BALA_ARISHTA -> 4.0   // Midpoint of 0-8
            AyurSpan.ALPAYU -> 20.0        // Midpoint of 8-32
            AyurSpan.MADHYAYU -> 51.0      // Midpoint of 32-70
            AyurSpan.PURNAYU -> 85.0       // Midpoint of 70-100
        }

        return (baseYears + analysis.kakshyaContribution).coerceIn(0.0, 100.0)
    }

    /**
     * Identify critical periods in the dasha sequence
     */
    private fun identifyCriticalPeriods(
        dashas: List<DrigDashaPeriod>,
        chart: VedicChart,
        rudra: ZodiacSign,
        maheshwara: ZodiacSign
    ): List<CriticalPeriod> {
        val criticalPeriods = mutableListOf<CriticalPeriod>()

        for (dasha in dashas) {
            // Rudra dasha is critical
            if (dasha.sign == rudra) {
                criticalPeriods.add(
                    CriticalPeriod(
                        startDate = dasha.startDate,
                        endDate = dasha.endDate,
                        sign = dasha.sign,
                        reason = "Rudra Dasha - Period of potential health challenges",
                        severity = 4,
                        remedies = listOf(
                            "Maha Mrityunjaya Japa daily",
                            "Rudra Abhishekam on Mondays",
                            "Donate mustard oil on Saturdays"
                        )
                    )
                )
            }

            // Maheshwara dasha
            if (dasha.sign == maheshwara) {
                criticalPeriods.add(
                    CriticalPeriod(
                        startDate = dasha.startDate,
                        endDate = dasha.endDate,
                        sign = dasha.sign,
                        reason = "Maheshwara Dasha - Transformative period requiring caution",
                        severity = 3,
                        remedies = listOf(
                            "Shiva puja on Pradosha",
                            "Chant Om Namah Shivaya",
                            "Spiritual practices recommended"
                        )
                    )
                )
            }

            // Maraka periods
            if (dasha.isMarakaPeriod) {
                criticalPeriods.add(
                    CriticalPeriod(
                        startDate = dasha.startDate,
                        endDate = dasha.endDate,
                        sign = dasha.sign,
                        reason = dasha.marakaReason ?: "Maraka influence",
                        severity = 4,
                        remedies = listOf(
                            "Health check-ups recommended",
                            "Avoid risky activities",
                            "Perform longevity-enhancing rituals"
                        )
                    )
                )
            }
        }

        return criticalPeriods.sortedBy { it.startDate }
    }

    // ============================================
    // HELPER METHODS
    // ============================================

    private fun getSignFromLagna(lagna: ZodiacSign, house: Int): ZodiacSign {
        val index = (lagna.ordinal + house - 1) % 12
        return ZodiacSign.entries[index]
    }

    private fun calculateSignStrength(chart: VedicChart, sign: ZodiacSign): Double {
        var strength = 50.0

        // Planets in sign
        val planetsInSign = chart.planetPositions.filter { it.sign == sign }
        strength += planetsInSign.size * 10

        // Sign lord's condition
        val signLord = sign.ruler
        val lordPos = chart.planetPositions.find { it.planet == signLord }
        if (lordPos != null) {
            val dignity = VedicAstrologyUtils.getDignity(lordPos)
            strength += when (dignity) {
                VedicAstrologyUtils.PlanetaryDignity.EXALTED -> 20.0
                VedicAstrologyUtils.PlanetaryDignity.OWN_SIGN -> 15.0
                VedicAstrologyUtils.PlanetaryDignity.MOOLATRIKONA -> 15.0
                VedicAstrologyUtils.PlanetaryDignity.FRIEND_SIGN -> 10.0
                VedicAstrologyUtils.PlanetaryDignity.DEBILITATED -> -10.0
                VedicAstrologyUtils.PlanetaryDignity.ENEMY_SIGN -> -5.0
                else -> 0.0
            }
        }

        return strength
    }

    private fun getAspectingPlanets(chart: VedicChart, sign: ZodiacSign): List<Planet> {
        val aspectingPlanets = mutableListOf<Planet>()
        val targetHouse = sign.number

        for (pos in chart.planetPositions) {
            if (pos.sign != sign) {
                val aspectedHouses = VedicAstrologyUtils.getAspectedHouses(pos.planet, pos.house)
                val targetHouseFromLagna = ((targetHouse - VedicAstrologyUtils.getAscendantSign(chart).number + 12) % 12) + 1
                if (targetHouseFromLagna in aspectedHouses) {
                    aspectingPlanets.add(pos.planet)
                }
            }
        }

        return aspectingPlanets
    }

    private fun isMarakaPeriod(chart: VedicChart, sign: ZodiacSign): Boolean {
        val lagnaSign = VedicAstrologyUtils.getAscendantSign(chart)
        val secondSign = getSignFromLagna(lagnaSign, 2)
        val seventhSign = getSignFromLagna(lagnaSign, 7)

        // Check if sign is 2nd or 7th from Lagna (Maraka houses)
        if (sign == secondSign || sign == seventhSign) return true

        // Check if sign lord is Maraka lord
        val secondLord = secondSign.ruler
        val seventhLord = seventhSign.ruler

        if (sign.ruler == secondLord || sign.ruler == seventhLord) return true

        // Check if Maraka planets occupy this sign
        val marakaPlanets = listOf(secondLord, seventhLord)
        val planetsInSign = chart.planetPositions.filter { it.sign == sign }.map { it.planet }

        return planetsInSign.any { it in marakaPlanets }
    }

    private fun getMarakaReason(chart: VedicChart, sign: ZodiacSign): String {
        val lagnaSign = VedicAstrologyUtils.getAscendantSign(chart)
        val secondSign = getSignFromLagna(lagnaSign, 2)
        val seventhSign = getSignFromLagna(lagnaSign, 7)

        return when (sign) {
            secondSign -> "2nd house (Maraka sthana) - affects vitality and family"
            seventhSign -> "7th house (Maraka sthana) - affects partnerships and longevity"
            else -> "Contains Maraka lord influence"
        }
    }

    private fun getHealthIndicators(chart: VedicChart, sign: ZodiacSign): List<String> {
        val indicators = mutableListOf<String>()

        // Based on sign element
        when (sign) {
            ZodiacSign.ARIES, ZodiacSign.LEO, ZodiacSign.SAGITTARIUS ->
                indicators.add("Watch for Pitta-related issues (inflammation, fever)")
            ZodiacSign.TAURUS, ZodiacSign.VIRGO, ZodiacSign.CAPRICORN ->
                indicators.add("Watch for Vata-Kapha issues (joints, digestion)")
            ZodiacSign.GEMINI, ZodiacSign.LIBRA, ZodiacSign.AQUARIUS ->
                indicators.add("Watch for Vata issues (nervous system, anxiety)")
            ZodiacSign.CANCER, ZodiacSign.SCORPIO, ZodiacSign.PISCES ->
                indicators.add("Watch for Kapha issues (water retention, emotions)")
        }

        // Based on planets in sign
        val planetsInSign = chart.planetPositions.filter { it.sign == sign }.map { it.planet }

        if (Planet.SATURN in planetsInSign) indicators.add("Chronic conditions may surface")
        if (Planet.MARS in planetsInSign) indicators.add("Accidents, surgeries possible")
        if (Planet.RAHU in planetsInSign) indicators.add("Unusual or hard-to-diagnose conditions")

        return indicators
    }

    private fun buildDashaPeriodInterpretation(
        sign: ZodiacSign,
        signLord: Planet,
        planetsInSign: List<Planet>,
        isMaraka: Boolean
    ): String {
        return buildString {
            append("${sign.displayName} Dasha ruled by ${signLord.displayName}. ")

            if (planetsInSign.isNotEmpty()) {
                append("Planets ${planetsInSign.joinToString { it.displayName }} influence this period. ")
            }

            if (isMaraka) {
                append("⚠️ This is a Maraka period - exercise caution regarding health. ")
            }

            // Sign-based general prediction
            append(
                when (sign) {
                    ZodiacSign.ARIES -> "Period of initiative and new beginnings."
                    ZodiacSign.TAURUS -> "Focus on stability, wealth, and comforts."
                    ZodiacSign.GEMINI -> "Communication, learning, and short travels highlighted."
                    ZodiacSign.CANCER -> "Domestic matters, mother, and emotional growth."
                    ZodiacSign.LEO -> "Authority, recognition, and self-expression."
                    ZodiacSign.VIRGO -> "Health, service, and analytical pursuits."
                    ZodiacSign.LIBRA -> "Relationships, partnerships, and balance."
                    ZodiacSign.SCORPIO -> "Transformation, research, and hidden matters."
                    ZodiacSign.SAGITTARIUS -> "Higher learning, dharma, and long journeys."
                    ZodiacSign.CAPRICORN -> "Career advancement and responsibilities."
                    ZodiacSign.AQUARIUS -> "Gains, social causes, and aspirations."
                    ZodiacSign.PISCES -> "Spirituality, foreign connections, and closure."
                }
            )
        }
    }

    private fun generateChartId(chart: VedicChart): String {
        val birthData = chart.birthData
        return "DRIG-${birthData.name}-${birthData.dateTime}".replace(Regex("[^a-zA-Z0-9-]"), "_")
    }

    /**
     * Get summary text for display
     */
    fun getSummary(analysis: DrigDashaAnalysis): String {
        return buildString {
            appendLine("═══════════════════════════════════════")
            appendLine("JAIMINI DRIG DASHA ANALYSIS")
            appendLine("═══════════════════════════════════════")
            appendLine()
            appendLine("Longevity Span: ${analysis.longevitySpan.displayName} (${analysis.longevitySpan.sanskritName})")
            appendLine("Estimated Years: ${String.format("%.1f", analysis.estimatedLongevity)}")
            appendLine()
            appendLine("Key Signs:")
            appendLine("  Brahma: ${analysis.brahmaSign.displayName}")
            appendLine("  Rudra: ${analysis.rudraSign.displayName}")
            appendLine("  Maheshwara: ${analysis.maheshwaraSign.displayName}")
            appendLine()

            analysis.currentDasha?.let { dasha ->
                appendLine("Current Dasha: ${dasha.sign.displayName}")
                appendLine("  Period: ${dasha.startDate.toLocalDate()} to ${dasha.endDate.toLocalDate()}")
                if (dasha.isMarakaPeriod) {
                    appendLine("  ⚠️ Maraka Period - Exercise caution")
                }
            }

            if (analysis.criticalPeriods.isNotEmpty()) {
                appendLine()
                appendLine("Critical Periods:")
                analysis.criticalPeriods.take(3).forEach { period ->
                    appendLine("  ${period.sign.displayName}: ${period.startDate.toLocalDate()}")
                    appendLine("    Reason: ${period.reason}")
                }
            }
        }
    }
}
