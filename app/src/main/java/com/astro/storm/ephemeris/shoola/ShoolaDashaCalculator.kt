package com.astro.storm.ephemeris.shoola

import com.astro.storm.core.common.Language
import com.astro.storm.core.common.StringKeyDosha
import com.astro.storm.core.common.StringResources
import com.astro.storm.core.model.Planet
import com.astro.storm.core.model.VedicChart
import com.astro.storm.core.model.ZodiacSign
import com.astro.storm.ephemeris.shoola.ShoolaDashaEvaluator.assessHealthSeverity
import com.astro.storm.ephemeris.shoola.ShoolaDashaEvaluator.assessPeriodNature
import com.astro.storm.ephemeris.shoola.ShoolaDashaEvaluator.calculateLongevityAssessment
import com.astro.storm.ephemeris.shoola.ShoolaHelpers.aspectsPoint
import com.astro.storm.ephemeris.shoola.ShoolaTriMurtiAnalyzer.calculateTriMurti
import java.time.LocalDateTime
import java.time.temporal.ChronoUnit

/**
 * Shoola Dasha Calculator
 *
 * Shoola Dasha (शूल दशा) is a Jaimini sign-based timing system specifically used for:
 * - Health predictions and medical timing
 * - Longevity analysis (Ayurdaya)
 * - Critical period identification
 * - Death/transformation timing analysis
 *
 * ═══════════════════════════════════════════════════════════════════════════
 * TRI-MURTI PRINCIPLE (त्रिमूर्ति)
 * ═══════════════════════════════════════════════════════════════════════════
 *
 * The foundation of Shoola Dasha is the Tri-Murti (Three Divine Forms):
 *
 * 1. **Brahma (ब्रह्मा)** - The Creator
 *    - Determined by the 8th lord from stronger of Ascendant/7th house
 *    - Represents the life-giving force and vitality
 *
 * 2. **Vishnu (विष्णु)** - The Preserver
 *    - Determined by the 12th lord from Brahma
 *    - Represents sustenance and longevity
 *
 * 3. **Rudra/Maheshwara (रुद्र/महेश्वर)** - The Destroyer
 *    - Determined by the 6th lord from Brahma
 *    - Represents transformation and ending cycles
 *    - **CRITICAL: Shoola Dasha starts from the sign containing Rudra**
 *
 * ═══════════════════════════════════════════════════════════════════════════
 * DIRECTION DETERMINATION (दिशा निर्धारण) - SAVYA/APASAVYA
 * ═══════════════════════════════════════════════════════════════════════════
 *
 * The direction of Dasha progression follows the Jaimini Savya/Apasavya rule:
 *
 * **ODD SIGNS (Savya - सव्य - Clockwise/Direct):**
 * - Aries (1), Gemini (3), Leo (5), Libra (7), Sagittarius (9), Aquarius (11)
 * - Dasha progresses: Aries → Taurus → Gemini → Cancer → ...
 * - Also called "Masculine" or "Positive" signs
 *
 * **EVEN SIGNS (Apasavya - अपसव्य - Counter-clockwise/Reverse):**
 * - Taurus (2), Cancer (4), Virgo (6), Scorpio (8), Capricorn (10), Pisces (12)
 * - Dasha progresses: Taurus → Aries → Pisces → Aquarius → ...
 * - Also called "Feminine" or "Negative" signs
 *
 * **Determination Logic:**
 * ```
 * Starting Sign = Sign where Rudra is placed
 * If Starting Sign number is ODD (1,3,5,7,9,11) → DIRECT progression
 * If Starting Sign number is EVEN (2,4,6,8,10,12) → REVERSE progression
 * ```
 *
 * ═══════════════════════════════════════════════════════════════════════════
 * DASHA STRUCTURE
 * ═══════════════════════════════════════════════════════════════════════════
 *
 * - **Mahadasha Duration**: 9 years per sign
 * - **Total Cycle**: 108 years (9 years × 12 signs)
 * - **Antardasha**: 9 months per sub-sign within Mahadasha
 * - The number 108 is sacred in Vedic tradition (12 signs × 9 planets)
 *
 * ═══════════════════════════════════════════════════════════════════════════
 * HEALTH SEVERITY ASSESSMENT
 * ═══════════════════════════════════════════════════════════════════════════
 *
 * Each sign period is evaluated for health risks based on:
 * - Proximity to 8th house from Ascendant (Ayu Bhava - house of longevity)
 * - Presence of malefics (Saturn, Mars, Rahu, Ketu)
 * - Relationship with Rudra (the Destroyer planet)
 * - Aspects from benefics (Jupiter, Venus, Mercury, Moon)
 *
 * Classical Reference: Jaimini Sutras, Adhyaya 2, Pada 3
 *
 * @author AstroStorm - Ultra-Precision Vedic Astrology
 */
object ShoolaDashaCalculator {

    /**
     * Duration of each sign dasha in years (9 years per sign).
     * Total cycle = 9 years × 12 signs = 108 years
     */
    private const val SIGN_DASHA_YEARS = 9.0

    /**
     * Approximate days per month for Antardasha calculation.
     * Using 30.44 days (365.25 / 12) for accuracy.
     */
    private const val DAYS_PER_MONTH = 30.44

    /**
     * Calculate comprehensive Shoola Dasha analysis for a given chart.
     *
     * @param chart The Vedic chart to analyze
     * @return ShoolaDashaResult containing all dasha periods, or null if chart is empty
     */
    fun calculateShoolaDasha(chart: VedicChart): ShoolaDashaResult? {
        // Validate input
        if (chart.planetPositions.isEmpty()) {
            return null
        }

        // Get ascendant sign and birth time
        val ascendantSign = ZodiacSign.fromLongitude(chart.ascendant)
        val birthDateTime = chart.birthData.dateTime

        // Calculate Tri-Murti (Brahma, Vishnu, Rudra)
        val triMurti = calculateTriMurti(chart, ascendantSign)

        // Determine starting sign and direction
        val (startingSign, direction) = determineStartingSignAndDirection(chart, ascendantSign, triMurti)

        // Calculate all Mahadashas (12 sign periods)
        val mahadashas = calculateMahadashas(
            chart = chart,
            startingSign = startingSign,
            direction = direction,
            birthDateTime = birthDateTime,
            triMurti = triMurti
        )

        // Find current Mahadasha
        val currentMahadasha = mahadashas.find { it.isCurrent }

        // Calculate Antardashas for current Mahadasha
        val antardashas = currentMahadasha?.let { mahadasha ->
            calculateAntardashas(chart, mahadasha, triMurti)
        } ?: emptyList()

        // Find current Antardasha
        val currentAntardasha = antardashas.find { it.isCurrent }

        // Identify health vulnerability periods
        val healthVulnerabilities = identifyHealthVulnerabilities(chart, mahadashas, triMurti)

        // Get upcoming critical periods (next 5)
        val upcomingCriticalPeriods = healthVulnerabilities
            .filter { it.startDate.isAfter(LocalDateTime.now()) }
            .take(5)

        // Calculate longevity assessment
        val longevityAssessment = calculateLongevityAssessment(chart, triMurti, ascendantSign)

        // Generate remedies based on current period
        val remedies = generateRemedies(triMurti, currentMahadasha)

        // Calculate applicability score
        val applicability = calculateApplicability(chart, triMurti)

        // Build and return result
        return ShoolaDashaResult(
            triMurti = triMurti,
            startingSign = startingSign,
            direction = direction,
            mahadashas = mahadashas,
            currentMahadasha = currentMahadasha,
            currentAntardasha = currentAntardasha,
            antardashas = antardashas,
            healthVulnerabilities = healthVulnerabilities,
            upcomingCriticalPeriods = upcomingCriticalPeriods,
            longevityAssessment = longevityAssessment,
            remedies = remedies,
            summaryEnglish = StringResources.get(StringKeyDosha.SHOOLA_SUMMARY_EN, Language.ENGLISH),
            summaryNepali = StringResources.get(StringKeyDosha.SHOOLA_SUMMARY_NE, Language.NEPALI),
            applicability = applicability
        )
    }

    /**
     * Determine the starting sign and direction for Shoola Dasha.
     *
     * Rules per Jaimini Sutras:
     * - Starting sign is the sign containing Rudra (the destroyer planet)
     * - If Rudra position unavailable, use Ascendant
     * - Direction: Odd signs (Aries, Gemini, Leo, Libra, Sagittarius, Aquarius) = Direct (Savya)
     *              Even signs (Taurus, Cancer, Virgo, Scorpio, Capricorn, Pisces) = Reverse (Apasavya)
     *
     * @param chart The Vedic chart
     * @param ascendant The ascendant sign
     * @param triMurti The calculated Tri-Murti analysis
     * @return Pair of starting sign and direction
     */
    private fun determineStartingSignAndDirection(
        chart: VedicChart,
        ascendant: ZodiacSign,
        triMurti: TriMurtiAnalysis
    ): Pair<ZodiacSign, DashaDirection> {
        // Find the sign containing Rudra
        val rudraPosition = chart.planetPositions.find { it.planet == triMurti.rudra }
        val startingSign = rudraPosition?.let {
            ZodiacSign.fromLongitude(it.longitude)
        } ?: ascendant

        // Determine direction based on odd/even nature of starting sign
        // Odd signs (1,3,5,7,9,11): Direct counting
        // Even signs (2,4,6,8,10,12): Reverse counting
        val direction = if (startingSign.number % 2 == 1) {
            DashaDirection.DIRECT
        } else {
            DashaDirection.REVERSE
        }

        return startingSign to direction
    }

    /**
     * Calculate all 12 Mahadasha periods.
     *
     * Each Mahadasha lasts 9 years, covering all 12 signs in sequence.
     * The sequence direction depends on whether the starting sign is odd or even.
     *
     * @param chart The Vedic chart for evaluation
     * @param startingSign The first sign of the dasha sequence
     * @param direction Whether to count forward (DIRECT) or backward (REVERSE)
     * @param birthDateTime The birth date and time
     * @param triMurti The Tri-Murti analysis for this chart
     * @return List of 12 ShoolaDashaPeriod objects
     */
    private fun calculateMahadashas(
        chart: VedicChart,
        startingSign: ZodiacSign,
        direction: DashaDirection,
        birthDateTime: LocalDateTime,
        triMurti: TriMurtiAnalysis
    ): List<ShoolaDashaPeriod> {
        val mahadashas = mutableListOf<ShoolaDashaPeriod>()
        var periodStart = birthDateTime
        val signSequence = getSignProgression(startingSign, direction)
        val currentTime = LocalDateTime.now()

        for (sign in signSequence) {
            val periodEnd = periodStart.plusYears(SIGN_DASHA_YEARS.toLong())

            // Check if this period is currently active
            val isCurrent = currentTime.isAfter(periodStart) && currentTime.isBefore(periodEnd)

            // Assess the nature and health severity of this period
            val periodNature = assessPeriodNature(chart, sign, triMurti)
            val healthSeverity = assessHealthSeverity(chart, sign, triMurti)

            // Calculate progress percentage
            val progressPercent = when {
                isCurrent -> {
                    val elapsed = ChronoUnit.DAYS.between(periodStart, currentTime).toDouble()
                    val total = ChronoUnit.DAYS.between(periodStart, periodEnd).toDouble()
                    elapsed / total
                }
                currentTime.isAfter(periodEnd) -> 1.0
                else -> 0.0
            }

            // Find planets in or aspecting this sign
            val planetsInvolved = findPlanetsInOrAspectingSign(chart, sign)

            // Create period descriptions
            val descriptionEn = "${sign.displayName} period"
            val descriptionNe = "${sign.displayName} अवधि"

            mahadashas.add(
                ShoolaDashaPeriod(
                    sign = sign,
                    ruler = sign.ruler,
                    startDate = periodStart,
                    endDate = periodEnd,
                    durationYears = SIGN_DASHA_YEARS,
                    nature = periodNature,
                    healthSeverity = healthSeverity,
                    isCurrent = isCurrent,
                    progressPercent = progressPercent,
                    planetsInSign = planetsInvolved,
                    healthConcerns = emptyList(),
                    healthConcernsNe = emptyList(),
                    favorableActivities = emptyList(),
                    favorableActivitiesNe = emptyList(),
                    description = descriptionEn,
                    descriptionNe = descriptionNe
                )
            )

            periodStart = periodEnd
        }

        return mahadashas
    }

    /**
     * Get the sequence of signs based on starting sign and direction.
     *
     * @param startingSign The first sign in the sequence
     * @param direction DIRECT (forward) or REVERSE (backward)
     * @return List of 12 ZodiacSigns in order
     */
    private fun getSignProgression(startingSign: ZodiacSign, direction: DashaDirection): List<ZodiacSign> {
        val progression = mutableListOf<ZodiacSign>()
        val startIndex = startingSign.number - 1 // Convert to 0-indexed

        for (i in 0 until 12) {
            val signIndex = when (direction) {
                DashaDirection.DIRECT -> (startIndex + i) % 12
                DashaDirection.REVERSE -> (startIndex - i + 12) % 12
            }
            progression.add(ZodiacSign.entries[signIndex])
        }

        return progression
    }

    /**
     * Calculate Antardashas (sub-periods) within a Mahadasha.
     *
     * Each Mahadasha has 12 Antardashas, one for each sign.
     * Antardasha duration = 9 years / 12 = 9 months each (approximately)
     *
     * @param chart The Vedic chart
     * @param mahadasha The parent Mahadasha period
     * @param triMurti The Tri-Murti analysis
     * @return List of ShoolaAntardasha periods
     */
    private fun calculateAntardashas(
        chart: VedicChart,
        mahadasha: ShoolaDashaPeriod,
        triMurti: TriMurtiAnalysis
    ): List<ShoolaAntardasha> {
        val antardashas = mutableListOf<ShoolaAntardasha>()
        var periodStart = mahadasha.startDate

        // Antardashas always progress in direct order from Mahadasha sign
        val signSequence = getSignProgression(mahadasha.sign, DashaDirection.DIRECT)
        val currentTime = LocalDateTime.now()

        for (sign in signSequence) {
            // Each Antardasha = 9 years × 30.44 days ≈ 274 days
            val durationDays = (SIGN_DASHA_YEARS * DAYS_PER_MONTH).toLong()
            val periodEnd = periodStart.plusDays(durationDays)

            // Check if this sub-period is currently active
            val isCurrent = currentTime.isAfter(periodStart) && currentTime.isBefore(periodEnd)

            // Assess the nature and severity
            val periodNature = assessPeriodNature(chart, sign, triMurti)
            val healthSeverity = assessHealthSeverity(chart, sign, triMurti)

            antardashas.add(
                ShoolaAntardasha(
                    sign = sign,
                    ruler = sign.ruler,
                    startDate = periodStart,
                    endDate = periodEnd,
                    durationMonths = SIGN_DASHA_YEARS,
                    nature = periodNature,
                    healthSeverity = healthSeverity,
                    isCurrent = isCurrent,
                    description = "Sub-period",
                    descriptionNe = "उप-अवधि"
                )
            )

            periodStart = periodEnd
        }

        return antardashas
    }

    /**
     * Find all planets that are in or aspecting a given sign.
     *
     * @param chart The Vedic chart
     * @param sign The sign to check
     * @return List of planets in or aspecting this sign
     */
    private fun findPlanetsInOrAspectingSign(chart: VedicChart, sign: ZodiacSign): List<Planet> {
        // Calculate midpoint of the sign for aspect calculation
        val signMidpoint = (sign.number - 1) * 30.0 + 15.0

        return chart.planetPositions.filter { position ->
            // Check if planet is in this sign
            val isInSign = ZodiacSign.fromLongitude(position.longitude) == sign

            // Check if planet aspects this sign's midpoint
            val aspectsSign = aspectsPoint(position.planet, position.longitude, signMidpoint)

            isInSign || aspectsSign
        }.map { it.planet }.distinct()
    }

    /**
     * Identify periods of health vulnerability based on severity levels.
     *
     * A period is considered vulnerable if its health severity level is 3 or higher.
     *
     * @param chart The Vedic chart
     * @param mahadashas All Mahadasha periods
     * @param triMurti The Tri-Murti analysis
     * @return List of health vulnerability periods
     */
    private fun identifyHealthVulnerabilities(
        chart: VedicChart,
        mahadashas: List<ShoolaDashaPeriod>,
        triMurti: TriMurtiAnalysis
    ): List<HealthVulnerabilityPeriod> {
        return mahadashas
            .filter { it.healthSeverity.level >= 3 }
            .map { period ->
                HealthVulnerabilityPeriod(
                    startDate = period.startDate,
                    endDate = period.endDate,
                    severity = period.healthSeverity,
                    sign = period.sign,
                    planet = null,
                    concernsEnglish = period.healthConcerns,
                    concernsNepali = period.healthConcernsNe,
                    precautionsEnglish = emptyList(),
                    precautionsNepali = emptyList(),
                    remediesEnglish = emptyList(),
                    remediesNepali = emptyList()
                )
            }
    }

    /**
     * Generate remedial measures based on current dasha analysis.
     *
     * Primary remedy focuses on pacifying Rudra (the destroyer principle).
     *
     * @param triMurti The Tri-Murti analysis
     * @param currentMahadasha The current Mahadasha period (may be null)
     * @return List of recommended remedies
     */
    private fun generateRemedies(
        triMurti: TriMurtiAnalysis,
        currentMahadasha: ShoolaDashaPeriod?
    ): List<ShoolaRemedy> {
        // Primary remedy: Pacify Rudra through Shiva worship
        val rudraRemedy = ShoolaRemedy(
            planet = triMurti.rudra,
            sign = triMurti.rudraSign,
            type = RemedyType.MANTRA,
            titleEnglish = "Pacify Rudra",
            titleNepali = "रुद्र शान्ति",
            mantra = "Om Namah Shivaya",
            deityEnglish = "Lord Shiva",
            deityNepali = "भगवान शिव",
            dayEnglish = "Monday",
            dayNepali = "सोमबार",
            effectivenessDays = 90
        )

        return listOf(rudraRemedy)
    }

    /**
     * Calculate applicability score for Shoola Dasha.
     *
     * Shoola Dasha is more applicable when:
     * - Rudra is strong in the chart
     * - Health-related houses (6th, 8th, 12th) are activated
     *
     * @param chart The Vedic chart
     * @param triMurti The Tri-Murti analysis
     * @return Applicability score from 0.0 to 1.0
     */
    private fun calculateApplicability(chart: VedicChart, triMurti: TriMurtiAnalysis): Double {
        // Base score of 60% + bonus based on Rudra strength (up to 40%)
        val baseScore = 60.0
        val rudraBonus = triMurti.rudraStrength * 20.0
        val totalScore = (baseScore + rudraBonus).coerceIn(0.0, 100.0)

        return totalScore / 100.0
    }
}
