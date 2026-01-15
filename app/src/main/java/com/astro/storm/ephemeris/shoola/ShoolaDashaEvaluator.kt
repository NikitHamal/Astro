package com.astro.storm.ephemeris.shoola

import com.astro.storm.core.model.Planet
import com.astro.storm.core.model.VedicChart
import com.astro.storm.core.model.ZodiacSign
import com.astro.storm.ephemeris.shoola.ShoolaHelpers.isDebilitated
import com.astro.storm.ephemeris.shoola.ShoolaHelpers.isExalted
import com.astro.storm.ephemeris.shoola.ShoolaHelpers.isOwnSign
import java.time.LocalDateTime

object ShoolaDashaEvaluator {

    fun assessPeriodNature(chart: VedicChart, sign: ZodiacSign, triMurti: TriMurtiAnalysis): PeriodNature {
        var score = 50; if (triMurti.rudraSign == sign) score -= 30; if (triMurti.brahmaSign == sign) score += 25
        chart.planetPositions.filter { ZodiacSign.fromLongitude(it.longitude) == sign }.forEach { score += when (it.planet) { Planet.JUPITER -> 15; Planet.VENUS -> 12; Planet.MERCURY -> 8; Planet.MOON -> 5; Planet.SATURN -> -15; Planet.MARS -> -12; Planet.RAHU -> -10; Planet.KETU -> -8; else -> 0 } }
        val lp = chart.planetPositions.find { it.planet == sign.ruler }; lp?.let { val s = ZodiacSign.fromLongitude(it.longitude); if (isExalted(sign.ruler, s)) score += 15; if (isDebilitated(sign.ruler, s)) score -= 15; if (isOwnSign(sign.ruler, s)) score += 10 }
        return when { score >= 75 -> PeriodNature.FAVORABLE; score >= 55 -> PeriodNature.SUPPORTIVE; score >= 40 -> PeriodNature.MIXED; score >= 25 -> PeriodNature.CHALLENGING; else -> PeriodNature.VERY_CHALLENGING }
    }

    fun assessHealthSeverity(chart: VedicChart, sign: ZodiacSign, triMurti: TriMurtiAnalysis): HealthSeverity {
        var risk = (if (triMurti.rudraSign == sign) 40 else 0) + (if (triMurti.secondaryRudraSign == sign) 25 else 0) + (if (triMurti.maheshwaraSign == sign) 30 else 0)
        val h = ((sign.number - ZodiacSign.fromLongitude(chart.ascendant).number + 12) % 12) + 1
        if (h in listOf(6, 8, 12)) risk += 20; risk += chart.planetPositions.count { ZodiacSign.fromLongitude(it.longitude) == sign && it.planet in listOf(Planet.SATURN, Planet.MARS, Planet.RAHU, Planet.KETU) } * 10
        return when { risk >= 80 -> HealthSeverity.CRITICAL; risk >= 60 -> HealthSeverity.HIGH; risk >= 40 -> HealthSeverity.MODERATE; risk >= 20 -> HealthSeverity.LOW; risk > 0 -> HealthSeverity.MINIMAL; else -> HealthSeverity.NONE }
    }

    fun calculateLongevityAssessment(chart: VedicChart, triMurti: TriMurtiAnalysis, ascSign: ZodiacSign): LongevityAssessment {
        var s = 50.0; val supEn = mutableListOf<String>(); val supNe = mutableListOf<String>(); val chalEn = mutableListOf<String>(); val chalNe = mutableListOf<String>()
        if (triMurti.brahma != null && triMurti.brahmaStrength > 0.6) { s += 15; supEn.add("Strong Brahma"); supNe.add("बलियो ब्रह्मा") }
        if (triMurti.rudraStrength > 0.7) { s -= 15; chalEn.add("Powerful Rudra"); chalNe.add("शक्तिशाली रुद्र") }
        chart.planetPositions.find { it.planet == Planet.JUPITER }?.let { if (it.house in listOf(1, 4, 5, 7, 9, 10)) { s += 10; supEn.add("Jupiter in auspicious house"); supNe.add("गुरु शुभ भावमा") } }
        val cat = when { s >= 70 -> LongevityCategory.POORNAYU; s >= 55 -> LongevityCategory.MADHYAYU; s >= 40 -> LongevityCategory.ALPAYU; else -> LongevityCategory.BALARISHTA }
        return LongevityAssessment(cat, cat.yearsRange, cat.yearsRange, supEn, supNe, chalEn, chalNe, "Longevity Category: ${cat.displayName}", "आयु वर्ग: ${cat.displayNameNe}")
    }
}
