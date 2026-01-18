package com.astro.storm.ephemeris.varshaphala

import com.astro.storm.core.common.Language
import com.astro.storm.core.common.StringKeyAnalysis
import com.astro.storm.data.localization.stringResources
import com.astro.storm.core.model.Planet
import com.astro.storm.core.model.ZodiacSign
import com.astro.storm.ephemeris.varshaphala.VarshaphalaConstants.EXALTATION_DEGREES
import com.astro.storm.ephemeris.varshaphala.VarshaphalaConstants.HADDA_LORDS
import com.astro.storm.ephemeris.varshaphala.VarshaphalaConstants.STANDARD_ZODIAC_SIGNS
import com.astro.storm.ephemeris.varshaphala.VarshaphalaHelpers.areFriends
import com.astro.storm.ephemeris.varshaphala.VarshaphalaHelpers.areNeutral
import com.astro.storm.ephemeris.varshaphala.VarshaphalaHelpers.getStandardZodiacIndex
import com.astro.storm.ephemeris.varshaphala.VarshaphalaHelpers.normalizeAngle
import kotlin.math.abs

object PanchaVargiyaBalaCalculator {

    fun calculateAllPanchaVargiyaBalas(chart: SolarReturnChart, language: Language): List<PanchaVargiyaBala> {
        return Planet.MAIN_PLANETS.filter { it != Planet.RAHU && it != Planet.KETU }
            .map { calculatePanchaVargiyaBala(it, chart, language) }
    }

    private fun calculatePanchaVargiyaBala(planet: Planet, chart: SolarReturnChart, language: Language): PanchaVargiyaBala {
        val pos = chart.planetPositions[planet] ?: return PanchaVargiyaBala(planet, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, StringResources.get(StringKeyAnalysis.VARSHA_STRENGTH_UNKNOWN, language))
        val long = normalizeAngle(pos.longitude)
        val u = calculateUchchaBala(planet, long)
        val h = calculateHaddaBala(planet, pos.sign, pos.degree)
        val dr = calculateDrekkanaBala(planet, pos.sign, pos.degree)
        val n = calculateNavamshaBala(planet, long)
        val dw = calculateDwadashamshabala(planet, long)
        val tot = u + h + dr + n + dw
        val cat = when { tot >= 15 -> StringResources.get(StringKeyAnalysis.PANCHA_EXCELLENT, language); tot >= 12 -> StringResources.get(StringKeyAnalysis.PANCHA_GOOD, language); tot >= 8 -> StringResources.get(StringKeyAnalysis.PANCHA_AVERAGE, language); tot >= 5 -> StringResources.get(StringKeyAnalysis.PANCHA_BELOW_AVERAGE, language); else -> StringResources.get(StringKeyAnalysis.PANCHA_WEAK, language) }
        return PanchaVargiyaBala(planet, u, h, dr, n, dw, tot, cat)
    }

    private fun calculateUchchaBala(planet: Planet, longitude: Double): Double {
        val exalt = EXALTATION_DEGREES[planet] ?: return 0.0
        val diff = abs(normalizeAngle(normalizeAngle(longitude) - exalt))
        return ((180 - (if (diff > 180) 360 - diff else diff)) / 180.0 * 5.0).coerceIn(0.0, 5.0)
    }

    private fun calculateHaddaBala(planet: Planet, sign: ZodiacSign, degree: Double): Double {
        for ((s, e, lord) in (HADDA_LORDS[sign] ?: emptyList())) if (degree >= s && degree < e) return when { lord == planet -> 4.0; areFriends(planet, lord) -> 3.0; areNeutral(planet, lord) -> 2.0; else -> 1.0 }
        return 2.0
    }

    private fun calculateDrekkanaBala(planet: Planet, sign: ZodiacSign, degree: Double): Double {
        val lord = STANDARD_ZODIAC_SIGNS[(getStandardZodiacIndex(sign) + (if (degree < 10) 0 else if (degree < 20) 1 else 2) * 4) % 12].ruler
        return when { lord == planet -> 4.0; areFriends(planet, lord) -> 3.0; areNeutral(planet, lord) -> 2.0; else -> 1.0 }
    }

    private fun calculateNavamshaBala(planet: Planet, longitude: Double): Double {
        val signIdx = (longitude / 30.0).toInt().coerceIn(0, 11)
        val navIdx = ((longitude % 30.0) / 3.333333).toInt().coerceIn(0, 8)
        val start = when (signIdx % 4) { 0 -> 0; 1 -> 9; 2 -> 6; else -> 3 }
        val lord = STANDARD_ZODIAC_SIGNS[(start + navIdx) % 12].ruler
        return when { lord == planet -> 4.0; areFriends(planet, lord) -> 3.0; areNeutral(planet, lord) -> 2.0; else -> 1.0 }
    }

    private fun calculateDwadashamshabala(planet: Planet, longitude: Double): Double {
        val signIdx = (longitude / 30.0).toInt().coerceIn(0, 11)
        val d12Idx = ((longitude % 30.0) / 2.5).toInt().coerceIn(0, 11)
        val lord = STANDARD_ZODIAC_SIGNS[(signIdx + d12Idx) % 12].ruler
        return when { lord == planet -> 3.0; areFriends(planet, lord) -> 2.5; areNeutral(planet, lord) -> 1.5; else -> 1.0 }
    }
}

