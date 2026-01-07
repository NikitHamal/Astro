package com.astro.storm.ephemeris

import com.astro.storm.data.model.Planet
import com.astro.storm.data.model.PlanetPosition
import com.astro.storm.data.model.VedicChart
import com.astro.storm.data.model.ZodiacSign
import java.time.LocalDateTime
import java.time.temporal.ChronoUnit
import kotlin.math.abs

/**
 * ShoolaDashaCalculator - Jaimini Shoola Dasha System
 *
 * Shoola Dasha is a sign-based dasha system from Jaimini astrology, specifically
 * used for timing health issues, accidents, and critical life events. The word
 * "Shoola" means "thorn" or "pain," indicating this dasha's primary use in
 * predicting difficult periods.
 *
 * Key Concepts:
 * - Based on Rudra (malefic planet positions)
 * - Uses Trikona progression (signs 1, 5, 9 from starting point)
 * - Particularly important for medical astrology and longevity analysis
 * - Each sign dasha = 9 years in the standard calculation
 *
 * The system identifies:
 * 1. Rudra - The most malefic planet (usually Saturn or Mars)
 * 2. Brahma - The creator significator (strong benefic)
 * 3. Maheshwara - The death significator (key for longevity)
 *
 * Traditional Vedic References:
 * - Jaimini Sutras (Chapter on Ayurdaya - Longevity)
 * - K.N. Rao's research on Jaimini Dasha systems
 * - Sanjay Rath's commentaries on Jaimini
 */
object ShoolaDashaCalculator {

    // Standard Shoola Dasha period per sign (in years)
    private const val SIGN_DASHA_YEARS = 9.0

    /**
     * Rudra identification - the malefic controller
     */
    enum class RudraType(val description: String, val descriptionNe: String) {
        PRIMARY("Primary Rudra - Most malefic influence", "प्राथमिक रुद्र - सबैभन्दा अशुभ प्रभाव"),
        SECONDARY("Secondary Rudra - Supporting malefic", "द्वितीय रुद्र - सहायक अशुभ")
    }

    /**
     * Health vulnerability levels
     */
    enum class HealthSeverity(
        val displayName: String,
        val displayNameNe: String,
        val level: Int
    ) {
        CRITICAL("Critical", "गम्भीर", 5),
        HIGH("High", "उच्च", 4),
        MODERATE("Moderate", "मध्यम", 3),
        LOW("Low", "न्यून", 2),
        MINIMAL("Minimal", "न्यूनतम", 1),
        NONE("None", "छैन", 0)
    }

    /**
     * Period nature classification
     */
    enum class PeriodNature(
        val displayName: String,
        val displayNameNe: String
    ) {
        VERY_CHALLENGING("Very Challenging", "अत्यन्त चुनौतीपूर्ण"),
        CHALLENGING("Challenging", "चुनौतीपूर्ण"),
        MIXED("Mixed", "मिश्रित"),
        SUPPORTIVE("Supportive", "सहायक"),
        FAVORABLE("Favorable", "अनुकूल")
    }

    /**
     * Brahma, Rudra, Maheshwara identification result
     */
    data class TriMurtiAnalysis(
        val brahma: Planet?,
        val brahmaSign: ZodiacSign?,
        val brahmaStrength: Double,
        val brahmaInterpretation: String,
        val rudra: Planet,
        val rudraSign: ZodiacSign?,
        val rudraStrength: Double,
        val rudraType: RudraType,
        val rudraInterpretation: String,
        val maheshwara: Planet?,
        val maheshwaraSign: ZodiacSign?,
        val maheshwaraInterpretation: String,
        val secondaryRudra: Planet?,
        val secondaryRudraSign: ZodiacSign?
    )

    /**
     * Shoola Dasha period (sign-based)
     */
    data class ShoolaDashaPeriod(
        val sign: ZodiacSign,
        val signLord: Planet,
        val startDate: LocalDateTime,
        val endDate: LocalDateTime,
        val durationYears: Double,
        val nature: PeriodNature,
        val healthSeverity: HealthSeverity,
        val isCurrent: Boolean,
        val progress: Double,
        val significantPlanets: List<Planet>,
        val healthConcerns: List<String>,
        val healthConcernsNe: List<String>,
        val precautions: List<String>,
        val precautionsNe: List<String>,
        val interpretation: String,
        val interpretationNe: String
    )

    /**
     * Sub-period (Antardasha) within Shoola Dasha
     */
    data class ShoolaAntardasha(
        val sign: ZodiacSign,
        val signLord: Planet,
        val startDate: LocalDateTime,
        val endDate: LocalDateTime,
        val durationMonths: Double,
        val nature: PeriodNature,
        val healthSeverity: HealthSeverity,
        val isCurrent: Boolean,
        val interpretation: String,
        val interpretationNe: String
    )

    /**
     * Health vulnerability period
     */
    data class HealthVulnerabilityPeriod(
        val startDate: LocalDateTime,
        val endDate: LocalDateTime,
        val severity: HealthSeverity,
        val dashaSign: ZodiacSign,
        val antardashSign: ZodiacSign?,
        val concerns: List<String>,
        val concernsNe: List<String>,
        val bodyParts: List<String>,
        val bodyPartsNe: List<String>,
        val recommendations: List<String>,
        val recommendationsNe: List<String>
    )

    /**
     * Complete Shoola Dasha analysis result
     */
    data class ShoolaDashaResult(
        val triMurti: TriMurtiAnalysis,
        val startingSign: ZodiacSign,
        val dashaDirection: DashaDirection,
        val mahadashas: List<ShoolaDashaPeriod>,
        val currentMahadasha: ShoolaDashaPeriod?,
        val currentAntardasha: ShoolaAntardasha?,
        val antardashas: List<ShoolaAntardasha>,
        val healthVulnerabilities: List<HealthVulnerabilityPeriod>,
        val upcomingCriticalPeriods: List<HealthVulnerabilityPeriod>,
        val longevityAssessment: LongevityAssessment,
        val remedies: List<ShoolaRemedy>,
        val overallAssessment: String,
        val overallAssessmentNe: String,
        val systemApplicability: Double
    )

    /**
     * Dasha progression direction
     */
    enum class DashaDirection(val displayName: String, val displayNameNe: String) {
        DIRECT("Direct (Zodiacal)", "प्रत्यक्ष (राशिक्रम)"),
        REVERSE("Reverse (Anti-zodiacal)", "विपरीत (राशिविरुद्ध)")
    }

    /**
     * Longevity assessment
     */
    data class LongevityAssessment(
        val category: LongevityCategory,
        val estimatedRange: String,
        val estimatedRangeNe: String,
        val supportingFactors: List<String>,
        val supportingFactorsNe: List<String>,
        val challengingFactors: List<String>,
        val challengingFactorsNe: List<String>,
        val interpretation: String,
        val interpretationNe: String
    )

    /**
     * Longevity categories per classical texts
     */
    enum class LongevityCategory(
        val displayName: String,
        val displayNameNe: String,
        val yearsRange: String
    ) {
        BALARISHTA("Balarishta", "बालारिष्ट", "0-8"),
        ALPAYU("Alpayu (Short)", "अल्पायु", "8-32"),
        MADHYAYU("Madhyayu (Medium)", "मध्यायु", "32-70"),
        POORNAYU("Poornayu (Full)", "पूर्णायु", "70-100"),
        AMITAYU("Amitayu (Extended)", "अमितायु", "100+")
    }

    /**
     * Remedy for Shoola Dasha afflictions
     */
    data class ShoolaRemedy(
        val targetPlanet: Planet?,
        val targetSign: ZodiacSign?,
        val remedyType: RemedyType,
        val description: String,
        val descriptionNe: String,
        val mantra: String?,
        val deity: String?,
        val deityNe: String?,
        val bestDay: String?,
        val bestDayNe: String?,
        val effectiveness: Int
    )

    /**
     * Types of remedies
     */
    enum class RemedyType(val displayName: String, val displayNameNe: String) {
        MANTRA("Mantra Japa", "मन्त्र जप"),
        PUJA("Puja/Worship", "पूजा"),
        DONATION("Charity", "दान"),
        FASTING("Fasting", "व्रत"),
        GEMSTONE("Gemstone", "रत्न"),
        YANTRA("Yantra", "यन्त्र"),
        LIFESTYLE("Lifestyle Change", "जीवनशैली परिवर्तन")
    }

    /**
     * Main calculation function - Calculate complete Shoola Dasha
     */
    fun calculateShoolaDasha(chart: VedicChart): ShoolaDashaResult? {
        if (chart.planetPositions.isEmpty()) return null

        val ascendantSign = ZodiacSign.fromLongitude(chart.ascendant)
        val birthDateTime = chart.birthData.dateTime

        // Step 1: Calculate Tri-Murti (Brahma, Rudra, Maheshwara)
        val triMurti = calculateTriMurti(chart, ascendantSign)

        // Step 2: Determine starting sign and direction
        val (startingSign, direction) = determineStartingSignAndDirection(chart, ascendantSign, triMurti)

        // Step 3: Calculate Mahadashas (sign-based periods)
        val mahadashas = calculateMahadashas(
            chart, startingSign, direction, birthDateTime, triMurti
        )

        // Step 4: Find current Mahadasha
        val now = LocalDateTime.now()
        val currentMahadasha = mahadashas.find { it.isCurrent }

        // Step 5: Calculate Antardashas for current period
        val antardashas = currentMahadasha?.let {
            calculateAntardashas(chart, it, triMurti)
        } ?: emptyList()

        val currentAntardasha = antardashas.find { it.isCurrent }

        // Step 6: Identify health vulnerability periods
        val healthVulnerabilities = identifyHealthVulnerabilities(chart, mahadashas, triMurti)

        // Step 7: Get upcoming critical periods (next 5 years)
        val upcomingCritical = healthVulnerabilities.filter {
            it.startDate.isAfter(now) && it.startDate.isBefore(now.plusYears(5))
        }.sortedBy { it.startDate }

        // Step 8: Calculate longevity assessment
        val longevityAssessment = calculateLongevityAssessment(chart, triMurti, ascendantSign)

        // Step 9: Generate remedies
        val remedies = generateRemedies(chart, triMurti, currentMahadasha, healthVulnerabilities)

        // Step 10: Calculate system applicability
        val applicability = calculateSystemApplicability(chart, triMurti)

        // Step 11: Generate overall assessment
        val (assessmentEn, assessmentNe) = generateOverallAssessment(
            triMurti, currentMahadasha, longevityAssessment, applicability
        )

        return ShoolaDashaResult(
            triMurti = triMurti,
            startingSign = startingSign,
            dashaDirection = direction,
            mahadashas = mahadashas,
            currentMahadasha = currentMahadasha,
            currentAntardasha = currentAntardasha,
            antardashas = antardashas,
            healthVulnerabilities = healthVulnerabilities,
            upcomingCriticalPeriods = upcomingCritical,
            longevityAssessment = longevityAssessment,
            remedies = remedies,
            overallAssessment = assessmentEn,
            overallAssessmentNe = assessmentNe,
            systemApplicability = applicability
        )
    }

    /**
     * Calculate Tri-Murti (Brahma, Rudra, Maheshwara)
     *
     * Brahma: Creator - Strongest odd sign planet in 1st, 5th, or 9th from Lagna
     * Rudra: Destroyer - Most malefic planet (typically Saturn or Mars)
     * Maheshwara: Death significator - 8th lord or Atmakaraka in certain conditions
     */
    private fun calculateTriMurti(chart: VedicChart, ascendantSign: ZodiacSign): TriMurtiAnalysis {
        val positions = chart.planetPositions

        // Find Rudra (most malefic)
        val rudraResult = findRudra(chart, positions, ascendantSign)

        // Find Brahma (benefic in trikona)
        val brahmaResult = findBrahma(chart, positions, ascendantSign)

        // Find Maheshwara (death significator)
        val maheshwaraResult = findMaheshwara(chart, positions, ascendantSign)

        return TriMurtiAnalysis(
            brahma = brahmaResult.first,
            brahmaSign = brahmaResult.second,
            brahmaStrength = brahmaResult.third,
            brahmaInterpretation = generateBrahmaInterpretation(brahmaResult),
            rudra = rudraResult.planet,
            rudraSign = rudraResult.sign,
            rudraStrength = rudraResult.strength,
            rudraType = rudraResult.type,
            rudraInterpretation = generateRudraInterpretation(rudraResult),
            maheshwara = maheshwaraResult.first,
            maheshwaraSign = maheshwaraResult.second,
            maheshwaraInterpretation = generateMaheshwaraInterpretation(maheshwaraResult),
            secondaryRudra = findSecondaryRudra(chart, rudraResult.planet),
            secondaryRudraSign = findSecondaryRudra(chart, rudraResult.planet)?.let { p ->
                positions.find { it.planet == p }?.let { ZodiacSign.fromLongitude(it.longitude) }
            }
        )
    }

    private data class RudraResult(
        val planet: Planet,
        val sign: ZodiacSign?,
        val strength: Double,
        val type: RudraType
    )

    /**
     * Find Rudra - The most malefic planet
     */
    private fun findRudra(
        chart: VedicChart,
        positions: List<PlanetPosition>,
        ascendantSign: ZodiacSign
    ): RudraResult {
        // Natural malefics: Saturn, Mars, Rahu, Ketu, Sun
        val malefics = listOf(Planet.SATURN, Planet.MARS, Planet.RAHU, Planet.KETU, Planet.SUN)

        var maxMaleficScore = 0.0
        var rudra = Planet.SATURN
        var rudraSign: ZodiacSign? = null

        for (malefic in malefics) {
            val pos = positions.find { it.planet == malefic } ?: continue
            val sign = ZodiacSign.fromLongitude(pos.longitude)

            var score = 0.0

            // Score based on house position (dusthanas increase malefic nature)
            when (pos.house) {
                6, 8, 12 -> score += 30.0  // Dusthanas
                2, 7 -> score += 25.0      // Maraka houses
                3 -> score += 15.0         // 3rd house
            }

            // Score based on natural malefic strength
            score += when (malefic) {
                Planet.SATURN -> 25.0
                Planet.MARS -> 22.0
                Planet.RAHU -> 20.0
                Planet.KETU -> 18.0
                Planet.SUN -> 15.0
                else -> 10.0
            }

            // Debilitation increases malefic nature
            if (isDebilitated(malefic, sign)) score += 20.0

            // Retrograde increases effect
            if (pos.isRetrograde) score += 10.0

            // Aspects from other malefics
            score += countMaleficAspects(positions, pos) * 5.0

            if (score > maxMaleficScore) {
                maxMaleficScore = score
                rudra = malefic
                rudraSign = sign
            }
        }

        return RudraResult(
            planet = rudra,
            sign = rudraSign,
            strength = (maxMaleficScore / 100.0).coerceIn(0.0, 1.0),
            type = RudraType.PRIMARY
        )
    }

    /**
     * Find Brahma - Creator significator
     */
    private fun findBrahma(
        chart: VedicChart,
        positions: List<PlanetPosition>,
        ascendantSign: ZodiacSign
    ): Triple<Planet?, ZodiacSign?, Double> {
        // Benefics: Jupiter, Venus, Mercury (when unafflicted), Moon (when waxing)
        val benefics = listOf(Planet.JUPITER, Planet.VENUS, Planet.MERCURY, Planet.MOON)
        val trikonaHouses = listOf(1, 5, 9)

        var bestBrahma: Planet? = null
        var bestSign: ZodiacSign? = null
        var maxStrength = 0.0

        for (benefic in benefics) {
            val pos = positions.find { it.planet == benefic } ?: continue
            if (pos.house !in trikonaHouses) continue

            val sign = ZodiacSign.fromLongitude(pos.longitude)
            var strength = 50.0

            // Exaltation
            if (isExalted(benefic, sign)) strength += 30.0
            // Own sign
            if (isOwnSign(benefic, sign)) strength += 20.0
            // Aspects from benefics
            strength += countBeneficAspects(positions, pos) * 5.0
            // Kendra position bonus
            if (pos.house in listOf(1, 4, 7, 10)) strength += 10.0

            if (strength > maxStrength) {
                maxStrength = strength
                bestBrahma = benefic
                bestSign = sign
            }
        }

        return Triple(bestBrahma, bestSign, (maxStrength / 100.0).coerceIn(0.0, 1.0))
    }

    /**
     * Find Maheshwara - Death significator
     */
    private fun findMaheshwara(
        chart: VedicChart,
        positions: List<PlanetPosition>,
        ascendantSign: ZodiacSign
    ): Triple<Planet?, ZodiacSign?, String> {
        // 8th house lord is primary Maheshwara
        val eighthSign = ZodiacSign.entries[(ascendantSign.number + 6) % 12]
        val eighthLord = eighthSign.ruler

        val pos = positions.find { it.planet == eighthLord }
        val sign = pos?.let { ZodiacSign.fromLongitude(it.longitude) }

        val interpretation = buildString {
            append("Maheshwara is $eighthLord (8th lord)")
            pos?.let {
                append(" placed in house ${it.house}")
                sign?.let { s -> append(" in $s") }
            }
        }

        return Triple(eighthLord, sign, interpretation)
    }

    /**
     * Find secondary Rudra
     */
    private fun findSecondaryRudra(chart: VedicChart, primaryRudra: Planet): Planet? {
        val malefics = listOf(Planet.SATURN, Planet.MARS, Planet.RAHU, Planet.KETU)
            .filter { it != primaryRudra }

        return malefics.maxByOrNull { planet ->
            chart.planetPositions.find { it.planet == planet }?.let { pos ->
                var score = 0.0
                if (pos.house in listOf(6, 8, 12)) score += 20.0
                if (pos.isRetrograde) score += 10.0
                score
            } ?: 0.0
        }
    }

    /**
     * Determine starting sign and direction for Shoola Dasha
     */
    private fun determineStartingSignAndDirection(
        chart: VedicChart,
        ascendantSign: ZodiacSign,
        triMurti: TriMurtiAnalysis
    ): Pair<ZodiacSign, DashaDirection> {
        // Standard Shoola Dasha starts from Rudra's sign
        val rudraPos = chart.planetPositions.find { it.planet == triMurti.rudra }
        val startSign = rudraPos?.let { ZodiacSign.fromLongitude(it.longitude) } ?: ascendantSign

        // Direction based on odd/even sign
        val direction = if (startSign.number % 2 == 1) {
            DashaDirection.DIRECT
        } else {
            DashaDirection.REVERSE
        }

        return Pair(startSign, direction)
    }

    /**
     * Calculate Mahadashas
     */
    private fun calculateMahadashas(
        chart: VedicChart,
        startingSign: ZodiacSign,
        direction: DashaDirection,
        birthDateTime: LocalDateTime,
        triMurti: TriMurtiAnalysis
    ): List<ShoolaDashaPeriod> {
        val periods = mutableListOf<ShoolaDashaPeriod>()
        val now = LocalDateTime.now()

        var currentDate = birthDateTime
        val signOrder = getSignProgression(startingSign, direction)

        for (sign in signOrder) {
            val endDate = currentDate.plusMonths((SIGN_DASHA_YEARS * 12).toLong())
            val isCurrent = now.isAfter(currentDate) && now.isBefore(endDate)

            val progress = if (isCurrent) {
                val totalDays = ChronoUnit.DAYS.between(currentDate, endDate).toDouble()
                val elapsedDays = ChronoUnit.DAYS.between(currentDate, now).toDouble()
                (elapsedDays / totalDays).coerceIn(0.0, 1.0)
            } else {
                if (now.isAfter(endDate)) 1.0 else 0.0
            }

            val nature = assessPeriodNature(chart, sign, triMurti)
            val severity = assessHealthSeverity(chart, sign, triMurti)
            val significantPlanets = findSignificantPlanets(chart, sign)
            val (healthConcernsEn, healthConcernsNe) = getHealthConcerns(sign, severity)
            val (precautionsEn, precautionsNe) = getPrecautions(sign, nature, severity)
            val (interpEn, interpNe) = generatePeriodInterpretation(sign, nature, severity, triMurti)

            periods.add(
                ShoolaDashaPeriod(
                    sign = sign,
                    signLord = sign.ruler,
                    startDate = currentDate,
                    endDate = endDate,
                    durationYears = SIGN_DASHA_YEARS,
                    nature = nature,
                    healthSeverity = severity,
                    isCurrent = isCurrent,
                    progress = progress,
                    significantPlanets = significantPlanets,
                    healthConcerns = healthConcernsEn,
                    healthConcernsNe = healthConcernsNe,
                    precautions = precautionsEn,
                    precautionsNe = precautionsNe,
                    interpretation = interpEn,
                    interpretationNe = interpNe
                )
            )

            currentDate = endDate
        }

        return periods
    }

    /**
     * Get sign progression based on direction
     */
    private fun getSignProgression(startSign: ZodiacSign, direction: DashaDirection): List<ZodiacSign> {
        val signs = mutableListOf<ZodiacSign>()
        val allSigns = ZodiacSign.entries

        for (i in 0 until 12) {
            val index = when (direction) {
                DashaDirection.DIRECT -> (startSign.number - 1 + i) % 12
                DashaDirection.REVERSE -> (startSign.number - 1 - i + 120) % 12
            }
            signs.add(allSigns[index])
        }

        return signs
    }

    /**
     * Calculate Antardashas within a Mahadasha
     */
    private fun calculateAntardashas(
        chart: VedicChart,
        mahadasha: ShoolaDashaPeriod,
        triMurti: TriMurtiAnalysis
    ): List<ShoolaAntardasha> {
        val antardashas = mutableListOf<ShoolaAntardasha>()
        val now = LocalDateTime.now()

        val totalMonths = SIGN_DASHA_YEARS * 12
        val monthsPerAntardasha = totalMonths / 12.0

        var currentDate = mahadasha.startDate
        val signOrder = getSignProgression(mahadasha.sign, DashaDirection.DIRECT)

        for (sign in signOrder) {
            val endDate = currentDate.plusDays((monthsPerAntardasha * 30.44).toLong())
            val isCurrent = now.isAfter(currentDate) && now.isBefore(endDate)

            val nature = assessPeriodNature(chart, sign, triMurti)
            val severity = assessHealthSeverity(chart, sign, triMurti)
            val (interpEn, interpNe) = generateAntardashaInterpretation(
                mahadasha.sign, sign, nature, severity
            )

            antardashas.add(
                ShoolaAntardasha(
                    sign = sign,
                    signLord = sign.ruler,
                    startDate = currentDate,
                    endDate = endDate,
                    durationMonths = monthsPerAntardasha,
                    nature = nature,
                    healthSeverity = severity,
                    isCurrent = isCurrent,
                    interpretation = interpEn,
                    interpretationNe = interpNe
                )
            )

            currentDate = endDate
        }

        return antardashas
    }

    /**
     * Assess period nature based on chart factors
     */
    private fun assessPeriodNature(
        chart: VedicChart,
        sign: ZodiacSign,
        triMurti: TriMurtiAnalysis
    ): PeriodNature {
        var score = 50 // Neutral baseline

        // Check if Rudra is in this sign
        triMurti.rudraSign?.let {
            if (it == sign) score -= 30
        }

        // Check if Brahma is in this sign
        triMurti.brahmaSign?.let {
            if (it == sign) score += 25
        }

        // Check planets in sign
        val planetsInSign = chart.planetPositions.filter {
            ZodiacSign.fromLongitude(it.longitude) == sign
        }

        for (pos in planetsInSign) {
            score += when (pos.planet) {
                Planet.JUPITER -> 15
                Planet.VENUS -> 12
                Planet.MERCURY -> 8
                Planet.MOON -> 5
                Planet.SATURN -> -15
                Planet.MARS -> -12
                Planet.RAHU -> -10
                Planet.KETU -> -8
                Planet.SUN -> -3
                else -> 0
            }
        }

        // Sign lord dignity
        val lord = sign.ruler
        val lordPos = chart.planetPositions.find { it.planet == lord }
        lordPos?.let {
            val lordSign = ZodiacSign.fromLongitude(it.longitude)
            if (isExalted(lord, lordSign)) score += 15
            if (isDebilitated(lord, lordSign)) score -= 15
            if (isOwnSign(lord, lordSign)) score += 10
        }

        return when {
            score >= 75 -> PeriodNature.FAVORABLE
            score >= 55 -> PeriodNature.SUPPORTIVE
            score >= 40 -> PeriodNature.MIXED
            score >= 25 -> PeriodNature.CHALLENGING
            else -> PeriodNature.VERY_CHALLENGING
        }
    }

    /**
     * Assess health severity for a sign period
     */
    private fun assessHealthSeverity(
        chart: VedicChart,
        sign: ZodiacSign,
        triMurti: TriMurtiAnalysis
    ): HealthSeverity {
        var riskScore = 0

        // Rudra in this sign = high risk
        if (triMurti.rudraSign == sign) riskScore += 40

        // Secondary Rudra
        if (triMurti.secondaryRudraSign == sign) riskScore += 25

        // Maheshwara presence
        if (triMurti.maheshwaraSign == sign) riskScore += 30

        // 6th, 8th, 12th house signs from Lagna
        val ascendantSign = ZodiacSign.fromLongitude(chart.ascendant)
        val houseFromLagna = ((sign.number - ascendantSign.number + 12) % 12) + 1
        if (houseFromLagna in listOf(6, 8, 12)) riskScore += 20

        // Malefics in sign
        val maleficsInSign = chart.planetPositions.count {
            ZodiacSign.fromLongitude(it.longitude) == sign &&
                    it.planet in listOf(Planet.SATURN, Planet.MARS, Planet.RAHU, Planet.KETU)
        }
        riskScore += maleficsInSign * 10

        return when {
            riskScore >= 80 -> HealthSeverity.CRITICAL
            riskScore >= 60 -> HealthSeverity.HIGH
            riskScore >= 40 -> HealthSeverity.MODERATE
            riskScore >= 20 -> HealthSeverity.LOW
            riskScore > 0 -> HealthSeverity.MINIMAL
            else -> HealthSeverity.NONE
        }
    }

    /**
     * Find significant planets for a sign period
     */
    private fun findSignificantPlanets(chart: VedicChart, sign: ZodiacSign): List<Planet> {
        val planets = mutableListOf<Planet>()

        // Sign lord
        planets.add(sign.ruler)

        // Planets in sign
        chart.planetPositions
            .filter { ZodiacSign.fromLongitude(it.longitude) == sign }
            .forEach { planets.add(it.planet) }

        // Planets aspecting sign
        val signMidpoint = (sign.number - 1) * 30.0 + 15.0
        chart.planetPositions.forEach { pos ->
            if (aspectsPoint(pos.planet, pos.longitude, signMidpoint)) {
                planets.add(pos.planet)
            }
        }

        return planets.distinct()
    }

    /**
     * Check if planet aspects a point
     */
    private fun aspectsPoint(planet: Planet, planetLong: Double, point: Double): Boolean {
        val diff = ((point - planetLong + 360) % 360)

        return when (planet) {
            Planet.MARS -> diff in listOf(90.0..120.0, 150.0..180.0, 210.0..240.0)
                    .any { range -> diff in range }
            Planet.JUPITER -> diff in listOf(120.0..150.0, 150.0..180.0, 240.0..270.0)
                    .any { range -> diff in range }
            Planet.SATURN -> diff in listOf(60.0..90.0, 150.0..180.0, 270.0..300.0)
                    .any { range -> diff in range }
            else -> abs(diff - 180.0) < 15.0 // Standard opposition aspect
        }
    }

    /**
     * Get health concerns for sign period
     */
    private fun getHealthConcerns(
        sign: ZodiacSign,
        severity: HealthSeverity
    ): Pair<List<String>, List<String>> {
        val concernsEn = mutableListOf<String>()
        val concernsNe = mutableListOf<String>()

        if (severity.level < 2) return Pair(concernsEn, concernsNe)

        // Body part associations by sign
        when (sign) {
            ZodiacSign.ARIES -> {
                concernsEn.add("Head-related issues, migraines")
                concernsNe.add("टाउको सम्बन्धी समस्या, माइग्रेन")
            }
            ZodiacSign.TAURUS -> {
                concernsEn.add("Throat, neck, thyroid concerns")
                concernsNe.add("घाँटी, गर्दन, थाइरोइड समस्या")
            }
            ZodiacSign.GEMINI -> {
                concernsEn.add("Respiratory, arm/shoulder issues")
                concernsNe.add("श्वासप्रश्वास, हात/काँध समस्या")
            }
            ZodiacSign.CANCER -> {
                concernsEn.add("Digestive, chest, emotional health")
                concernsNe.add("पाचन, छाती, भावनात्मक स्वास्थ्य")
            }
            ZodiacSign.LEO -> {
                concernsEn.add("Heart, spine, blood pressure")
                concernsNe.add("मुटु, मेरुदण्ड, रक्तचाप")
            }
            ZodiacSign.VIRGO -> {
                concernsEn.add("Intestinal, nervous system")
                concernsNe.add("आन्द्रा, स्नायु प्रणाली")
            }
            ZodiacSign.LIBRA -> {
                concernsEn.add("Kidney, lower back issues")
                concernsNe.add("मिर्गौला, कम्मर समस्या")
            }
            ZodiacSign.SCORPIO -> {
                concernsEn.add("Reproductive, urinary system")
                concernsNe.add("प्रजनन, मूत्र प्रणाली")
            }
            ZodiacSign.SAGITTARIUS -> {
                concernsEn.add("Liver, hips, thigh problems")
                concernsNe.add("कलेजो, कम्मर, जाँघ समस्या")
            }
            ZodiacSign.CAPRICORN -> {
                concernsEn.add("Bone, joint, knee issues")
                concernsNe.add("हड्डी, जोर्नी, घुँडा समस्या")
            }
            ZodiacSign.AQUARIUS -> {
                concernsEn.add("Circulation, ankle problems")
                concernsNe.add("रक्तसञ्चार, गोली समस्या")
            }
            ZodiacSign.PISCES -> {
                concernsEn.add("Feet, immune system, lymphatic")
                concernsNe.add("खुट्टा, प्रतिरक्षा, लसिका")
            }
        }

        if (severity.level >= 4) {
            concernsEn.add("Increased accident proneness")
            concernsNe.add("दुर्घटना प्रवृत्ति बढ्ने")
        }

        return Pair(concernsEn, concernsNe)
    }

    /**
     * Get precautions for period
     */
    private fun getPrecautions(
        sign: ZodiacSign,
        nature: PeriodNature,
        severity: HealthSeverity
    ): Pair<List<String>, List<String>> {
        val precautionsEn = mutableListOf<String>()
        val precautionsNe = mutableListOf<String>()

        if (severity.level >= 3) {
            precautionsEn.add("Regular health checkups recommended")
            precautionsNe.add("नियमित स्वास्थ्य परीक्षण गर्नुहोस्")

            precautionsEn.add("Avoid risky activities")
            precautionsNe.add("जोखिमपूर्ण गतिविधिबाट बच्नुहोस्")
        }

        if (nature == PeriodNature.VERY_CHALLENGING || nature == PeriodNature.CHALLENGING) {
            precautionsEn.add("Practice stress management")
            precautionsNe.add("तनाव व्यवस्थापन गर्नुहोस्")

            precautionsEn.add("Maintain regular routine")
            precautionsNe.add("नियमित दिनचर्या राख्नुहोस्")
        }

        // Sign-specific precautions
        when (sign) {
            ZodiacSign.ARIES, ZodiacSign.SCORPIO -> {
                precautionsEn.add("Avoid confrontations and accidents")
                precautionsNe.add("झगडा र दुर्घटनाबाट बच्नुहोस्")
            }
            ZodiacSign.CANCER, ZodiacSign.PISCES -> {
                precautionsEn.add("Watch emotional well-being")
                precautionsNe.add("भावनात्मक स्वास्थ्यमा ध्यान दिनुहोस्")
            }
            ZodiacSign.CAPRICORN, ZodiacSign.AQUARIUS -> {
                precautionsEn.add("Take care of bones and joints")
                precautionsNe.add("हड्डी र जोर्नीको हेरचाह गर्नुहोस्")
            }
            else -> {}
        }

        return Pair(precautionsEn, precautionsNe)
    }

    /**
     * Generate period interpretation
     */
    private fun generatePeriodInterpretation(
        sign: ZodiacSign,
        nature: PeriodNature,
        severity: HealthSeverity,
        triMurti: TriMurtiAnalysis
    ): Pair<String, String> {
        val enBuilder = StringBuilder()
        val neBuilder = StringBuilder()

        enBuilder.append("${sign.displayName} Dasha (${SIGN_DASHA_YEARS.toInt()} years): ")
        neBuilder.append("${sign.displayName} दशा (${SIGN_DASHA_YEARS.toInt()} वर्ष): ")

        when (nature) {
            PeriodNature.FAVORABLE -> {
                enBuilder.append("A favorable period with good health prospects. ")
                neBuilder.append("राम्रो स्वास्थ्य सम्भावना भएको अनुकूल अवधि। ")
            }
            PeriodNature.SUPPORTIVE -> {
                enBuilder.append("Generally supportive period for well-being. ")
                neBuilder.append("स्वास्थ्यका लागि सामान्यतया सहायक अवधि। ")
            }
            PeriodNature.MIXED -> {
                enBuilder.append("Mixed results expected; maintain balance. ")
                neBuilder.append("मिश्रित परिणाम अपेक्षित; सन्तुलन कायम राख्नुहोस्। ")
            }
            PeriodNature.CHALLENGING -> {
                enBuilder.append("Challenging period requiring caution. ")
                neBuilder.append("सावधानी आवश्यक पर्ने चुनौतीपूर्ण अवधि। ")
            }
            PeriodNature.VERY_CHALLENGING -> {
                enBuilder.append("Demanding period; extra care needed for health. ")
                neBuilder.append("कठिन अवधि; स्वास्थ्यमा विशेष ध्यान दिनुहोस्। ")
            }
        }

        if (severity.level >= 3) {
            enBuilder.append("Health vigilance is important during this time.")
            neBuilder.append("यस समयमा स्वास्थ्य सतर्कता महत्त्वपूर्ण छ।")
        }

        return Pair(enBuilder.toString(), neBuilder.toString())
    }

    /**
     * Generate Antardasha interpretation
     */
    private fun generateAntardashaInterpretation(
        mahadashaSign: ZodiacSign,
        antardashaSign: ZodiacSign,
        nature: PeriodNature,
        severity: HealthSeverity
    ): Pair<String, String> {
        val enBuilder = StringBuilder()
        val neBuilder = StringBuilder()

        enBuilder.append("${antardashaSign.displayName} sub-period in ${mahadashaSign.displayName}: ")
        neBuilder.append("${mahadashaSign.displayName} मा ${antardashaSign.displayName} उप-अवधि: ")

        when {
            nature == PeriodNature.FAVORABLE && severity.level <= 1 -> {
                enBuilder.append("Protected period with good vitality.")
                neBuilder.append("राम्रो जीवनी शक्ति भएको सुरक्षित अवधि।")
            }
            severity.level >= 4 -> {
                enBuilder.append("Exercise maximum caution; health risks elevated.")
                neBuilder.append("अधिकतम सावधानी अपनाउनुहोस्; स्वास्थ्य जोखिम बढेको।")
            }
            else -> {
                enBuilder.append("Moderate attention to health recommended.")
                neBuilder.append("स्वास्थ्यमा मध्यम ध्यान दिन सिफारिस गरिन्छ।")
            }
        }

        return Pair(enBuilder.toString(), neBuilder.toString())
    }

    /**
     * Identify health vulnerability periods
     */
    private fun identifyHealthVulnerabilities(
        chart: VedicChart,
        mahadashas: List<ShoolaDashaPeriod>,
        triMurti: TriMurtiAnalysis
    ): List<HealthVulnerabilityPeriod> {
        val vulnerabilities = mutableListOf<HealthVulnerabilityPeriod>()

        for (dasha in mahadashas) {
            if (dasha.healthSeverity.level < 3) continue

            val (bodyPartsEn, bodyPartsNe) = getBodyPartsForSign(dasha.sign)
            val (recsEn, recsNe) = getRecommendations(dasha.sign, dasha.healthSeverity)

            vulnerabilities.add(
                HealthVulnerabilityPeriod(
                    startDate = dasha.startDate,
                    endDate = dasha.endDate,
                    severity = dasha.healthSeverity,
                    dashaSign = dasha.sign,
                    antardashSign = null,
                    concerns = dasha.healthConcerns,
                    concernsNe = dasha.healthConcernsNe,
                    bodyParts = bodyPartsEn,
                    bodyPartsNe = bodyPartsNe,
                    recommendations = recsEn,
                    recommendationsNe = recsNe
                )
            )
        }

        return vulnerabilities.sortedByDescending { it.severity.level }
    }

    /**
     * Get body parts for sign
     */
    private fun getBodyPartsForSign(sign: ZodiacSign): Pair<List<String>, List<String>> {
        return when (sign) {
            ZodiacSign.ARIES -> Pair(listOf("Head", "Brain", "Face"), listOf("टाउको", "मस्तिष्क", "अनुहार"))
            ZodiacSign.TAURUS -> Pair(listOf("Throat", "Neck", "Voice"), listOf("घाँटी", "गर्दन", "स्वर"))
            ZodiacSign.GEMINI -> Pair(listOf("Arms", "Lungs", "Shoulders"), listOf("हात", "फोक्सो", "काँध"))
            ZodiacSign.CANCER -> Pair(listOf("Chest", "Stomach", "Breasts"), listOf("छाती", "पेट", "स्तन"))
            ZodiacSign.LEO -> Pair(listOf("Heart", "Spine", "Back"), listOf("मुटु", "मेरुदण्ड", "पीठ"))
            ZodiacSign.VIRGO -> Pair(listOf("Intestines", "Digestive"), listOf("आन्द्रा", "पाचन"))
            ZodiacSign.LIBRA -> Pair(listOf("Kidneys", "Lower back"), listOf("मिर्गौला", "कम्मर"))
            ZodiacSign.SCORPIO -> Pair(listOf("Reproductive", "Bladder"), listOf("प्रजनन", "मूत्राशय"))
            ZodiacSign.SAGITTARIUS -> Pair(listOf("Thighs", "Liver", "Hips"), listOf("जाँघ", "कलेजो", "नितम्ब"))
            ZodiacSign.CAPRICORN -> Pair(listOf("Knees", "Bones", "Joints"), listOf("घुँडा", "हड्डी", "जोर्नी"))
            ZodiacSign.AQUARIUS -> Pair(listOf("Ankles", "Calves", "Circulation"), listOf("गोली", "पिँडुला", "रक्तसञ्चार"))
            ZodiacSign.PISCES -> Pair(listOf("Feet", "Lymphatic", "Immune"), listOf("खुट्टा", "लसिका", "प्रतिरक्षा"))
        }
    }

    /**
     * Get recommendations based on severity
     */
    private fun getRecommendations(
        sign: ZodiacSign,
        severity: HealthSeverity
    ): Pair<List<String>, List<String>> {
        val recsEn = mutableListOf<String>()
        val recsNe = mutableListOf<String>()

        if (severity.level >= 3) {
            recsEn.add("Regular health checkups")
            recsNe.add("नियमित स्वास्थ्य जाँच")

            recsEn.add("Maintain healthy lifestyle")
            recsNe.add("स्वस्थ जीवनशैली अपनाउनुहोस्")
        }

        if (severity.level >= 4) {
            recsEn.add("Consult healthcare professionals")
            recsNe.add("स्वास्थ्य विशेषज्ञसँग परामर्श गर्नुहोस्")

            recsEn.add("Avoid high-risk activities")
            recsNe.add("उच्च जोखिम गतिविधिबाट बच्नुहोस्")
        }

        // Sign lord worship
        recsEn.add("Worship ${sign.ruler.displayName} for protection")
        recsNe.add("सुरक्षाको लागि ${sign.ruler.displayName} पूजा गर्नुहोस्")

        return Pair(recsEn, recsNe)
    }

    /**
     * Calculate longevity assessment
     */
    private fun calculateLongevityAssessment(
        chart: VedicChart,
        triMurti: TriMurtiAnalysis,
        ascendantSign: ZodiacSign
    ): LongevityAssessment {
        var score = 50 // Baseline

        val supportingEn = mutableListOf<String>()
        val supportingNe = mutableListOf<String>()
        val challengingEn = mutableListOf<String>()
        val challengingNe = mutableListOf<String>()

        // Brahma strength adds to longevity
        if (triMurti.brahma != null && triMurti.brahmaStrength > 0.6) {
            score += 15
            supportingEn.add("Strong Brahma (${triMurti.brahma.displayName})")
            supportingNe.add("बलियो ब्रह्मा (${triMurti.brahma.displayName})")
        }

        // Strong Rudra is challenging
        if (triMurti.rudraStrength > 0.7) {
            score -= 15
            challengingEn.add("Powerful Rudra (${triMurti.rudra.displayName})")
            challengingNe.add("शक्तिशाली रुद्र (${triMurti.rudra.displayName})")
        }

        // Jupiter in Kendra/Trikona
        val jupiterPos = chart.planetPositions.find { it.planet == Planet.JUPITER }
        jupiterPos?.let {
            if (it.house in listOf(1, 4, 5, 7, 9, 10)) {
                score += 10
                supportingEn.add("Jupiter in auspicious house")
                supportingNe.add("गुरु शुभ भावमा")
            }
        }

        // 8th house analysis
        val eighthSign = ZodiacSign.entries[(ascendantSign.number + 6) % 12]
        val planetsIn8th = chart.planetPositions.count { it.house == 8 }
        if (planetsIn8th == 0) {
            score += 5
            supportingEn.add("Empty 8th house")
            supportingNe.add("खाली आठौं भाव")
        } else if (planetsIn8th >= 3) {
            score -= 10
            challengingEn.add("Multiple planets in 8th house")
            challengingNe.add("आठौं भावमा धेरै ग्रह")
        }

        // Saturn placement
        val saturnPos = chart.planetPositions.find { it.planet == Planet.SATURN }
        saturnPos?.let {
            val saturnSign = ZodiacSign.fromLongitude(it.longitude)
            if (isExalted(Planet.SATURN, saturnSign) || isOwnSign(Planet.SATURN, saturnSign)) {
                score += 8
                supportingEn.add("Saturn in dignity")
                supportingNe.add("शनि स्व/उच्च राशिमा")
            }
        }

        val category = when {
            score >= 70 -> LongevityCategory.POORNAYU
            score >= 55 -> LongevityCategory.MADHYAYU
            score >= 40 -> LongevityCategory.ALPAYU
            else -> LongevityCategory.BALARISHTA
        }

        val (interpEn, interpNe) = generateLongevityInterpretation(category, supportingEn, challengingEn)

        return LongevityAssessment(
            category = category,
            estimatedRange = category.yearsRange,
            estimatedRangeNe = category.yearsRange,
            supportingFactors = supportingEn,
            supportingFactorsNe = supportingNe,
            challengingFactors = challengingEn,
            challengingFactorsNe = challengingNe,
            interpretation = interpEn,
            interpretationNe = interpNe
        )
    }

    /**
     * Generate longevity interpretation
     */
    private fun generateLongevityInterpretation(
        category: LongevityCategory,
        supporting: List<String>,
        challenging: List<String>
    ): Pair<String, String> {
        val enBuilder = StringBuilder()
        val neBuilder = StringBuilder()

        enBuilder.append("Longevity Category: ${category.displayName} (${category.yearsRange} years). ")
        neBuilder.append("आयु वर्ग: ${category.displayNameNe} (${category.yearsRange} वर्ष)। ")

        if (supporting.isNotEmpty()) {
            enBuilder.append("Protective factors present. ")
            neBuilder.append("सुरक्षात्मक कारकहरू उपस्थित। ")
        }

        if (challenging.isNotEmpty()) {
            enBuilder.append("Some challenging factors require attention to health.")
            neBuilder.append("केही चुनौतीपूर्ण कारकहरूले स्वास्थ्यमा ध्यान दिन आवश्यक पार्छन्।")
        }

        return Pair(enBuilder.toString(), neBuilder.toString())
    }

    /**
     * Generate remedies
     */
    private fun generateRemedies(
        chart: VedicChart,
        triMurti: TriMurtiAnalysis,
        currentMahadasha: ShoolaDashaPeriod?,
        vulnerabilities: List<HealthVulnerabilityPeriod>
    ): List<ShoolaRemedy> {
        val remedies = mutableListOf<ShoolaRemedy>()

        // Rudra pacification
        remedies.add(
            ShoolaRemedy(
                targetPlanet = triMurti.rudra,
                targetSign = triMurti.rudraSign,
                remedyType = RemedyType.MANTRA,
                description = "Chant ${getMantraForPlanet(triMurti.rudra)} for Rudra pacification",
                descriptionNe = "रुद्र शान्तिको लागि ${getMantraForPlanet(triMurti.rudra)} जप गर्नुहोस्",
                mantra = getMantraForPlanet(triMurti.rudra),
                deity = getDeityForPlanet(triMurti.rudra).first,
                deityNe = getDeityForPlanet(triMurti.rudra).second,
                bestDay = getDayForPlanet(triMurti.rudra).first,
                bestDayNe = getDayForPlanet(triMurti.rudra).second,
                effectiveness = 85
            )
        )

        // Mahamrityunjaya mantra for health protection
        remedies.add(
            ShoolaRemedy(
                targetPlanet = null,
                targetSign = null,
                remedyType = RemedyType.MANTRA,
                description = "Chant Mahamrityunjaya Mantra for health protection",
                descriptionNe = "स्वास्थ्य सुरक्षाको लागि महामृत्युञ्जय मन्त्र जप गर्नुहोस्",
                mantra = "Om Tryambakam Yajamahe Sugandhim Pushtivardhanam...",
                deity = "Lord Shiva",
                deityNe = "भगवान शिव",
                bestDay = "Monday",
                bestDayNe = "सोमबार",
                effectiveness = 95
            )
        )

        // Current period remedy
        currentMahadasha?.let { dasha ->
            if (dasha.healthSeverity.level >= 3) {
                remedies.add(
                    ShoolaRemedy(
                        targetPlanet = dasha.signLord,
                        targetSign = dasha.sign,
                        remedyType = RemedyType.PUJA,
                        description = "Perform ${dasha.signLord.displayName} puja during ${dasha.sign.displayName} dasha",
                        descriptionNe = "${dasha.sign.displayName} दशामा ${dasha.signLord.displayName} पूजा गर्नुहोस्",
                        mantra = getMantraForPlanet(dasha.signLord),
                        deity = getDeityForPlanet(dasha.signLord).first,
                        deityNe = getDeityForPlanet(dasha.signLord).second,
                        bestDay = getDayForPlanet(dasha.signLord).first,
                        bestDayNe = getDayForPlanet(dasha.signLord).second,
                        effectiveness = 80
                    )
                )
            }
        }

        // Fasting remedy
        remedies.add(
            ShoolaRemedy(
                targetPlanet = triMurti.rudra,
                targetSign = null,
                remedyType = RemedyType.FASTING,
                description = "Fast on ${getDayForPlanet(triMurti.rudra).first} for ${triMurti.rudra.displayName}",
                descriptionNe = "${triMurti.rudra.displayName} को लागि ${getDayForPlanet(triMurti.rudra).second} व्रत गर्नुहोस्",
                mantra = null,
                deity = null,
                deityNe = null,
                bestDay = getDayForPlanet(triMurti.rudra).first,
                bestDayNe = getDayForPlanet(triMurti.rudra).second,
                effectiveness = 70
            )
        )

        // Donation remedy
        val donationItem = getDonationForPlanet(triMurti.rudra)
        remedies.add(
            ShoolaRemedy(
                targetPlanet = triMurti.rudra,
                targetSign = null,
                remedyType = RemedyType.DONATION,
                description = "Donate ${donationItem.first} on ${getDayForPlanet(triMurti.rudra).first}",
                descriptionNe = "${getDayForPlanet(triMurti.rudra).second} मा ${donationItem.second} दान गर्नुहोस्",
                mantra = null,
                deity = null,
                deityNe = null,
                bestDay = getDayForPlanet(triMurti.rudra).first,
                bestDayNe = getDayForPlanet(triMurti.rudra).second,
                effectiveness = 75
            )
        )

        return remedies.sortedByDescending { it.effectiveness }
    }

    /**
     * Calculate system applicability score
     */
    private fun calculateSystemApplicability(
        chart: VedicChart,
        triMurti: TriMurtiAnalysis
    ): Double {
        var score = 60.0 // Base applicability

        // Strong Rudra increases applicability
        score += triMurti.rudraStrength * 20

        // Malefics in dusthanas increase relevance
        val dusthanaMalefics = chart.planetPositions.count {
            it.house in listOf(6, 8, 12) &&
                    it.planet in listOf(Planet.SATURN, Planet.MARS, Planet.RAHU, Planet.KETU)
        }
        score += dusthanaMalefics * 5

        // 8th lord affliction
        val ascSign = ZodiacSign.fromLongitude(chart.ascendant)
        val eighthLord = ZodiacSign.entries[(ascSign.number + 6) % 12].ruler
        val eighthLordPos = chart.planetPositions.find { it.planet == eighthLord }
        eighthLordPos?.let {
            if (it.house in listOf(6, 8, 12)) score += 10
        }

        return (score / 100.0).coerceIn(0.0, 1.0)
    }

    /**
     * Generate overall assessment
     */
    private fun generateOverallAssessment(
        triMurti: TriMurtiAnalysis,
        currentMahadasha: ShoolaDashaPeriod?,
        longevity: LongevityAssessment,
        applicability: Double
    ): Pair<String, String> {
        val enBuilder = StringBuilder()
        val neBuilder = StringBuilder()

        enBuilder.append("Shoola Dasha Analysis Summary:\n\n")
        neBuilder.append("शूल दशा विश्लेषण सारांश:\n\n")

        // Rudra information
        enBuilder.append("Primary Rudra: ${triMurti.rudra.displayName} - ")
        neBuilder.append("प्राथमिक रुद्र: ${triMurti.rudra.displayName} - ")

        if (triMurti.rudraStrength > 0.7) {
            enBuilder.append("strongly placed, indicating heightened attention to health matters.\n\n")
            neBuilder.append("बलियो स्थितिमा, स्वास्थ्य मामिलाहरूमा विशेष ध्यान आवश्यक।\n\n")
        } else {
            enBuilder.append("moderately placed.\n\n")
            neBuilder.append("मध्यम स्थितिमा।\n\n")
        }

        // Current period
        currentMahadasha?.let {
            enBuilder.append("Current Period: ${it.sign.displayName} Dasha - ${it.nature.displayName}\n")
            neBuilder.append("वर्तमान अवधि: ${it.sign.displayName} दशा - ${it.nature.displayNameNe}\n")

            enBuilder.append("Health Vigilance: ${it.healthSeverity.displayName}\n\n")
            neBuilder.append("स्वास्थ्य सतर्कता: ${it.healthSeverity.displayNameNe}\n\n")
        }

        // Longevity
        enBuilder.append("Longevity Indication: ${longevity.category.displayName}\n")
        neBuilder.append("आयु संकेत: ${longevity.category.displayNameNe}\n")

        // Applicability
        val applicabilityPercent = (applicability * 100).toInt()
        enBuilder.append("\nSystem Applicability: ${applicabilityPercent}%")
        neBuilder.append("\nप्रणाली प्रयोज्यता: ${applicabilityPercent}%")

        return Pair(enBuilder.toString(), neBuilder.toString())
    }

    // Helper functions for planetary attributes
    private fun getMantraForPlanet(planet: Planet): String = when (planet) {
        Planet.SUN -> "Om Suryaya Namaha"
        Planet.MOON -> "Om Chandraya Namaha"
        Planet.MARS -> "Om Mangalaya Namaha"
        Planet.MERCURY -> "Om Budhaya Namaha"
        Planet.JUPITER -> "Om Gurave Namaha"
        Planet.VENUS -> "Om Shukraya Namaha"
        Planet.SATURN -> "Om Shanaishcharaya Namaha"
        Planet.RAHU -> "Om Rahave Namaha"
        Planet.KETU -> "Om Ketave Namaha"
        else -> "Om Navagraha Namaha"
    }

    private fun getDeityForPlanet(planet: Planet): Pair<String, String> = when (planet) {
        Planet.SUN -> Pair("Lord Surya/Vishnu", "भगवान सूर्य/विष्णु")
        Planet.MOON -> Pair("Lord Shiva/Parvati", "भगवान शिव/पार्वती")
        Planet.MARS -> Pair("Lord Hanuman/Kartikeya", "हनुमान/कार्तिकेय")
        Planet.MERCURY -> Pair("Lord Vishnu/Ganesha", "भगवान विष्णु/गणेश")
        Planet.JUPITER -> Pair("Lord Brihaspati/Vishnu", "बृहस्पति/विष्णु")
        Planet.VENUS -> Pair("Goddess Lakshmi", "देवी लक्ष्मी")
        Planet.SATURN -> Pair("Lord Shani/Hanuman", "शनि/हनुमान")
        Planet.RAHU -> Pair("Goddess Durga", "देवी दुर्गा")
        Planet.KETU -> Pair("Lord Ganesha", "भगवान गणेश")
        else -> Pair("Navagraha", "नवग्रह")
    }

    private fun getDayForPlanet(planet: Planet): Pair<String, String> = when (planet) {
        Planet.SUN -> Pair("Sunday", "आइतबार")
        Planet.MOON -> Pair("Monday", "सोमबार")
        Planet.MARS -> Pair("Tuesday", "मंगलबार")
        Planet.MERCURY -> Pair("Wednesday", "बुधबार")
        Planet.JUPITER -> Pair("Thursday", "बिहीबार")
        Planet.VENUS -> Pair("Friday", "शुक्रबार")
        Planet.SATURN -> Pair("Saturday", "शनिबार")
        Planet.RAHU -> Pair("Saturday", "शनिबार")
        Planet.KETU -> Pair("Tuesday", "मंगलबार")
        else -> Pair("Any day", "जुनसुकै दिन")
    }

    private fun getDonationForPlanet(planet: Planet): Pair<String, String> = when (planet) {
        Planet.SUN -> Pair("wheat, red clothes", "गहुँ, रातो कपडा")
        Planet.MOON -> Pair("rice, white clothes", "चामल, सेतो कपडा")
        Planet.MARS -> Pair("red lentils, copper", "मसुरो, तामा")
        Planet.MERCURY -> Pair("green gram, green clothes", "मुङ्ग, हरियो कपडा")
        Planet.JUPITER -> Pair("turmeric, yellow clothes", "बेसार, पहेँलो कपडा")
        Planet.VENUS -> Pair("rice, white items", "चामल, सेता वस्तुहरू")
        Planet.SATURN -> Pair("black sesame, iron", "कालो तिल, फलाम")
        Planet.RAHU -> Pair("coconut, blue clothes", "नरिवल, निलो कपडा")
        Planet.KETU -> Pair("sesame, blanket", "तिल, कम्बल")
        else -> Pair("grains", "अनाज")
    }

    private fun generateBrahmaInterpretation(result: Triple<Planet?, ZodiacSign?, Double>): String {
        return result.first?.let {
            "Brahma is ${it.displayName} with strength ${(result.third * 100).toInt()}%"
        } ?: "No clear Brahma identified in trikona positions"
    }

    private fun generateRudraInterpretation(result: RudraResult): String {
        return "Rudra is ${result.planet.displayName} in ${result.sign?.displayName ?: "unknown"} " +
                "with ${(result.strength * 100).toInt()}% malefic strength"
    }

    private fun generateMaheshwaraInterpretation(result: Triple<Planet?, ZodiacSign?, String>): String {
        return result.third
    }

    // Dignity check functions
    private fun isExalted(planet: Planet, sign: ZodiacSign): Boolean = when (planet) {
        Planet.SUN -> sign == ZodiacSign.ARIES
        Planet.MOON -> sign == ZodiacSign.TAURUS
        Planet.MARS -> sign == ZodiacSign.CAPRICORN
        Planet.MERCURY -> sign == ZodiacSign.VIRGO
        Planet.JUPITER -> sign == ZodiacSign.CANCER
        Planet.VENUS -> sign == ZodiacSign.PISCES
        Planet.SATURN -> sign == ZodiacSign.LIBRA
        Planet.RAHU -> sign == ZodiacSign.TAURUS || sign == ZodiacSign.GEMINI
        Planet.KETU -> sign == ZodiacSign.SCORPIO || sign == ZodiacSign.SAGITTARIUS
        else -> false
    }

    private fun isDebilitated(planet: Planet, sign: ZodiacSign): Boolean = when (planet) {
        Planet.SUN -> sign == ZodiacSign.LIBRA
        Planet.MOON -> sign == ZodiacSign.SCORPIO
        Planet.MARS -> sign == ZodiacSign.CANCER
        Planet.MERCURY -> sign == ZodiacSign.PISCES
        Planet.JUPITER -> sign == ZodiacSign.CAPRICORN
        Planet.VENUS -> sign == ZodiacSign.VIRGO
        Planet.SATURN -> sign == ZodiacSign.ARIES
        Planet.RAHU -> sign == ZodiacSign.SCORPIO
        Planet.KETU -> sign == ZodiacSign.TAURUS
        else -> false
    }

    private fun isOwnSign(planet: Planet, sign: ZodiacSign): Boolean = sign.ruler == planet

    private fun countMaleficAspects(positions: List<PlanetPosition>, target: PlanetPosition): Int {
        val malefics = listOf(Planet.SATURN, Planet.MARS, Planet.RAHU, Planet.KETU)
        return positions.count { pos ->
            pos.planet in malefics && pos.planet != target.planet &&
                    aspectsPoint(pos.planet, pos.longitude, target.longitude)
        }
    }

    private fun countBeneficAspects(positions: List<PlanetPosition>, target: PlanetPosition): Int {
        val benefics = listOf(Planet.JUPITER, Planet.VENUS, Planet.MERCURY)
        return positions.count { pos ->
            pos.planet in benefics && pos.planet != target.planet &&
                    aspectsPoint(pos.planet, pos.longitude, target.longitude)
        }
    }
}
