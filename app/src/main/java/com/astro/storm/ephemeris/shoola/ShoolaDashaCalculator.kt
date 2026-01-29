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
import kotlin.math.abs

object ShoolaDashaCalculator {

    private const val SIGN_DASHA_YEARS = 9.0

    fun calculateShoolaDasha(chart: VedicChart): ShoolaDashaResult? {
        if (chart.planetPositions.isEmpty()) return null

        val ascendantSign = ZodiacSign.fromLongitude(chart.ascendant)
        val birthDateTime = chart.birthData.dateTime
        val triMurti = calculateTriMurti(chart, ascendantSign)
        val (startSign, direction) = determineStart(chart, ascendantSign, triMurti)

        val mahadashas = calculateMahadashas(chart, startSign, direction, birthDateTime, triMurti)
        val currentMahadasha = mahadashas.find { it.isCurrent }
        val antardashas = currentMahadasha?.let { calculateAntardashas(chart, it, triMurti) } ?: emptyList()

        val vulnerabilities = identifyHealthVulnerabilities(chart, mahadashas, triMurti)
        val longevity = calculateLongevityAssessment(chart, triMurti, ascendantSign)
        val applicability = calculateApplicability(chart, triMurti)

        return ShoolaDashaResult(
            triMurtiAnalysis = triMurti,
            startingSign = startSign,
            direction = direction,
            mahadashas = mahadashas,
            currentMahadasha = currentMahadasha,
            currentAntardasha = antardashas.find { it.isCurrent },
            antardashas = antardashas,
            allVulnerabilities = vulnerabilities,
            upcomingVulnerabilities = vulnerabilities.filter { it.startDate.isAfter(LocalDateTime.now()) }.take(5),
            longevityAssessment = longevity,
            remedies = generateRemedies(triMurti, currentMahadasha),
            summaryEn = StringResources.get(StringKeyDosha.SHOOLA_SUMMARY_EN, Language.ENGLISH),
            summaryNe = StringResources.get(StringKeyDosha.SHOOLA_SUMMARY_NE, Language.NEPALI),
            applicabilityScore = applicability
        )
    }

    private fun determineStart(
        chart: VedicChart,
        ascendantSign: ZodiacSign,
        triMurti: TriMurtiAnalysis
    ): Pair<ZodiacSign, DashaDirection> {
        val startSign = chart.planetPositions.find { it.planet == triMurti.rudra }
            ?.let { ZodiacSign.fromLongitude(it.longitude) } ?: ascendantSign

        val direction = if (startSign.number % 2 == 1) DashaDirection.DIRECT else DashaDirection.REVERSE
        return startSign to direction
    }

    private fun calculateMahadashas(
        chart: VedicChart,
        startSign: ZodiacSign,
        direction: DashaDirection,
        birthDateTime: LocalDateTime,
        triMurti: TriMurtiAnalysis
    ): List<ShoolaDashaPeriod> {
        val mahadashas = mutableListOf<ShoolaDashaPeriod>()
        var currentStartDate = birthDateTime
        val signSequence = getProgression(startSign, direction)
        val now = LocalDateTime.now()

        for (sign in signSequence) {
            val endDate = currentStartDate.plusYears(SIGN_DASHA_YEARS.toLong())
            val isCurrent = now.isAfter(currentStartDate) && now.isBefore(endDate)

            val nature = assessPeriodNature(chart, sign, triMurti)
            val healthSeverity = assessHealthSeverity(chart, sign, triMurti)

            val progress = when {
                isCurrent -> ChronoUnit.DAYS.between(currentStartDate, now).toDouble() /
                        ChronoUnit.DAYS.between(currentStartDate, endDate).toDouble()
                now.isAfter(endDate) -> 1.0
                else -> 0.0
            }

            mahadashas.add(
                ShoolaDashaPeriod(
                    sign = sign,
                    lord = sign.ruler,
                    startDate = currentStartDate,
                    endDate = endDate,
                    durationYears = SIGN_DASHA_YEARS,
                    nature = nature,
                    healthSeverity = healthSeverity,
                    isCurrent = isCurrent,
                    progress = progress,
                    occupiedPlanets = findPlanets(chart, sign),
                    aspectedPlanets = emptyList(),
                    vulnerabilityAreas = emptyList(),
                    interpretationsEn = emptyList(),
                    interpretationsNe = emptyList(),
                    displayName = "${sign.displayName} period",
                    displayNameNe = "${sign.displayName} अवधि"
                )
            )
            currentStartDate = endDate
        }
        return mahadashas
    }

    private fun getProgression(startSign: ZodiacSign, direction: DashaDirection): List<ZodiacSign> {
        val progression = mutableListOf<ZodiacSign>()
        for (i in 0 until 12) {
            val index = if (direction == DashaDirection.DIRECT) {
                (startSign.number - 1 + i) % 12
            } else {
                (startSign.number - 1 - i + 12) % 12
            }
            progression.add(ZodiacSign.entries[index])
        }
        return progression
    }

    private fun calculateAntardashas(
        chart: VedicChart,
        mahadasha: ShoolaDashaPeriod,
        triMurti: TriMurtiAnalysis
    ): List<ShoolaAntardasha> {
        val antardashas = mutableListOf<ShoolaAntardasha>()
        var currentStartDate = mahadasha.startDate
        val signSequence = getProgression(mahadasha.sign, DashaDirection.DIRECT)
        val now = LocalDateTime.now()

        // Each antardasha in a 9-year dasha lasts 9 months.
        // Standardizing to use DashaUtils.DAYS_PER_YEAR for consistency.
        val daysPerAntardasha = (com.astro.storm.ephemeris.DashaUtils.DAYS_PER_YEAR.toDouble() * SIGN_DASHA_YEARS) / 12.0

        for (sign in signSequence) {
            val endDate = currentStartDate.plusDays(daysPerAntardasha.toLong())
            val isCurrent = now.isAfter(currentStartDate) && now.isBefore(endDate)

            antardashas.add(
                ShoolaAntardasha(
                    sign = sign,
                    lord = sign.ruler,
                    startDate = currentStartDate,
                    endDate = endDate,
                    durationYears = SIGN_DASHA_YEARS / 12.0,
                    nature = assessPeriodNature(chart, sign, triMurti),
                    healthSeverity = assessHealthSeverity(chart, sign, triMurti),
                    isCurrent = isCurrent,
                    displayName = "Sub-period",
                    displayNameNe = "उप-अवधि"
                )
            )
            currentStartDate = endDate
        }
        return antardashas
    }

    private fun findPlanets(chart: VedicChart, sign: ZodiacSign): List<Planet> = chart.planetPositions.filter { ZodiacSign.fromLongitude(it.longitude) == sign || aspectsPoint(it.planet, it.longitude, (sign.number - 1) * 30.0 + 15.0) }.map { it.planet }.distinct()

    private fun identifyHealthVulnerabilities(chart: VedicChart, m: List<ShoolaDashaPeriod>, tm: TriMurtiAnalysis): List<HealthVulnerabilityPeriod> {
        return m.filter { it.healthSeverity.level >= 3 }.map { HealthVulnerabilityPeriod(it.startDate, it.endDate, it.healthSeverity, it.sign, null, it.healthConcerns, it.healthConcernsNe, emptyList(), emptyList(), emptyList(), emptyList()) }
    }

    private fun generateRemedies(tm: TriMurtiAnalysis, curr: ShoolaDashaPeriod?): List<ShoolaRemedy> = listOf(ShoolaRemedy(tm.rudra, tm.rudraSign, RemedyType.MANTRA, "Pacify Rudra", "रुद्र शान्ति", "Om Namah Shivaya", "Lord Shiva", "भगवान शिव", "Monday", "सोमबार", 90))

    private fun calculateApplicability(chart: VedicChart, tm: TriMurtiAnalysis): Double = (60.0 + tm.rudraStrength * 20.0).coerceIn(0.0, 100.0) / 100.0
}
