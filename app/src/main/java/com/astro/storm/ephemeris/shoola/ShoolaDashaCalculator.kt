package com.astro.storm.ephemeris.shoola

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
        val asc = ZodiacSign.fromLongitude(chart.ascendant); val birth = chart.birthData.dateTime
        val tm = calculateTriMurti(chart, asc); val (sSign, dir) = determineStart(chart, asc, tm)
        val mahadashas = calculateMahadashas(chart, sSign, dir, birth, tm)
        val currM = mahadashas.find { it.isCurrent }
        val antardashas = currM?.let { calculateAntardashas(chart, it, tm) } ?: emptyList()
        val vulnerabilities = identifyHealthVulnerabilities(chart, mahadashas, tm)
        val longevity = calculateLongevityAssessment(chart, tm, asc)
        val applicability = calculateApplicability(chart, tm)
        return ShoolaDashaResult(
            tm, sSign, dir, mahadashas, currM, antardashas.find { it.isCurrent }, 
            antardashas, vulnerabilities, vulnerabilities.filter { it.startDate.isAfter(LocalDateTime.now()) }.take(5), 
            longevity, generateRemedies(tm, currM), 
            StringResources.get(StringKeyDosha.SHOOLA_SUMMARY_EN, Language.ENGLISH), 
            StringResources.get(StringKeyDosha.SHOOLA_SUMMARY_NE, Language.NEPALI), 
            applicability
        )
    }

    private fun determineStart(chart: VedicChart, asc: ZodiacSign, tm: TriMurtiAnalysis): Pair<ZodiacSign, DashaDirection> {
        val s = chart.planetPositions.find { it.planet == tm.rudra }?.let { ZodiacSign.fromLongitude(it.longitude) } ?: asc
        return s to if (s.number % 2 == 1) DashaDirection.DIRECT else DashaDirection.REVERSE
    }

    private fun calculateMahadashas(chart: VedicChart, start: ZodiacSign, dir: DashaDirection, birth: LocalDateTime, tm: TriMurtiAnalysis): List<ShoolaDashaPeriod> {
        val res = mutableListOf<ShoolaDashaPeriod>(); var curr = birth; val signs = getProgression(start, dir); val now = LocalDateTime.now()
        for (s in signs) {
            val end = curr.plusYears(SIGN_DASHA_YEARS.toLong()); val isC = now.isAfter(curr) && now.isBefore(end)
            val n = assessPeriodNature(chart, s, tm); val sev = assessHealthSeverity(chart, s, tm)
            res.add(ShoolaDashaPeriod(s, s.ruler, curr, end, SIGN_DASHA_YEARS, n, sev, isC, if (isC) ChronoUnit.DAYS.between(curr, now).toDouble() / ChronoUnit.DAYS.between(curr, end).toDouble() else if (now.isAfter(end)) 1.0 else 0.0, findPlanets(chart, s), emptyList(), emptyList(), emptyList(), emptyList(), "${s.displayName} period", "${s.displayName} अवधि"))
            curr = end
        }
        return res
    }

    private fun getProgression(start: ZodiacSign, dir: DashaDirection): List<ZodiacSign> {
        val res = mutableListOf<ZodiacSign>()
        for (i in 0 until 12) res.add(ZodiacSign.entries[if (dir == DashaDirection.DIRECT) (start.number - 1 + i) % 12 else (start.number - 1 - i + 12) % 12])
        return res
    }

    private fun calculateAntardashas(chart: VedicChart, m: ShoolaDashaPeriod, tm: TriMurtiAnalysis): List<ShoolaAntardasha> {
        val res = mutableListOf<ShoolaAntardasha>(); var curr = m.startDate; val signs = getProgression(m.sign, DashaDirection.DIRECT); val now = LocalDateTime.now()
        for (s in signs) {
            val end = curr.plusDays((SIGN_DASHA_YEARS * 30.44).toLong()); val isC = now.isAfter(curr) && now.isBefore(end)
            res.add(ShoolaAntardasha(s, s.ruler, curr, end, SIGN_DASHA_YEARS, assessPeriodNature(chart, s, tm), assessHealthSeverity(chart, s, tm), isC, "Sub-period", "उप-अवधि"))
            curr = end
        }
        return res
    }

    private fun findPlanets(chart: VedicChart, sign: ZodiacSign): List<Planet> = chart.planetPositions.filter { ZodiacSign.fromLongitude(it.longitude) == sign || aspectsPoint(it.planet, it.longitude, (sign.number - 1) * 30.0 + 15.0) }.map { it.planet }.distinct()

    private fun identifyHealthVulnerabilities(chart: VedicChart, m: List<ShoolaDashaPeriod>, tm: TriMurtiAnalysis): List<HealthVulnerabilityPeriod> {
        return m.filter { it.healthSeverity.level >= 3 }.map { HealthVulnerabilityPeriod(it.startDate, it.endDate, it.healthSeverity, it.sign, null, it.healthConcerns, it.healthConcernsNe, emptyList(), emptyList(), emptyList(), emptyList()) }
    }

    private fun generateRemedies(tm: TriMurtiAnalysis, curr: ShoolaDashaPeriod?): List<ShoolaRemedy> = listOf(ShoolaRemedy(tm.rudra, tm.rudraSign, RemedyType.MANTRA, "Pacify Rudra", "रुद्र शान्ति", "Om Namah Shivaya", "Lord Shiva", "भगवान शिव", "Monday", "सोमबार", 90))

    private fun calculateApplicability(chart: VedicChart, tm: TriMurtiAnalysis): Double = (60.0 + tm.rudraStrength * 20.0).coerceIn(0.0, 100.0) / 100.0
}
