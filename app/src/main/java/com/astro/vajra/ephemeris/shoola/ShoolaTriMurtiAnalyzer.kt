package com.astro.vajra.ephemeris.shoola

import com.astro.vajra.core.model.Planet
import com.astro.vajra.core.model.PlanetPosition
import com.astro.vajra.core.model.VedicChart
import com.astro.vajra.core.model.ZodiacSign
import com.astro.vajra.ephemeris.shoola.ShoolaHelpers.countBeneficAspects
import com.astro.vajra.ephemeris.shoola.ShoolaHelpers.countMaleficAspects
import com.astro.vajra.ephemeris.shoola.ShoolaHelpers.isDebilitated
import com.astro.vajra.ephemeris.shoola.ShoolaHelpers.isExalted
import com.astro.vajra.ephemeris.shoola.ShoolaHelpers.isOwnSign

object ShoolaTriMurtiAnalyzer {

    fun calculateTriMurti(chart: VedicChart, ascSign: ZodiacSign): TriMurtiAnalysis {
        val rudra = findRudra(chart, ascSign)
        val brahma = findBrahma(chart, ascSign)
        val maheshwara = findMaheshwara(chart, ascSign)
        return TriMurtiAnalysis(
            brahma.first, brahma.second, brahma.third, brahma.first?.let { "Brahma is ${it.displayName} with strength ${(brahma.third * 100).toInt()}%" } ?: "No Brahma identified",
            rudra.planet, rudra.sign, rudra.strength, rudra.type, "Rudra is ${rudra.planet.displayName} in ${rudra.sign?.displayName ?: "unknown"} with ${(rudra.strength * 100).toInt()}% malefic strength",
            maheshwara.first, maheshwara.second, maheshwara.third,
            findSecondaryRudra(chart, rudra.planet),
            findSecondaryRudra(chart, rudra.planet)?.let { p -> chart.planetPositions.find { it.planet == p }?.let { ZodiacSign.fromLongitude(it.longitude) } }
        )
    }

    private data class RudraResult(val planet: Planet, val sign: ZodiacSign?, val strength: Double, val type: RudraType)

    private fun findRudra(chart: VedicChart, ascSign: ZodiacSign): RudraResult {
        var max = 0.0; var p = Planet.SATURN; var s: ZodiacSign? = null
        listOf(Planet.SATURN, Planet.MARS, Planet.RAHU, Planet.KETU, Planet.SUN).forEach { m ->
            chart.planetPositions.find { it.planet == m }?.let { pos ->
                val sign = ZodiacSign.fromLongitude(pos.longitude)
                var score = when (pos.house) { 6, 8, 12 -> 30.0; 2, 7 -> 25.0; 3 -> 15.0; else -> 0.0 }
                score += when (m) { Planet.SATURN -> 25.0; Planet.MARS -> 22.0; Planet.RAHU -> 20.0; Planet.KETU -> 18.0; Planet.SUN -> 15.0; else -> 10.0 }
                if (isDebilitated(m, sign)) score += 20.0; if (pos.isRetrograde) score += 10.0; score += countMaleficAspects(chart.planetPositions, pos) * 5.0
                if (score > max) { max = score; p = m; s = sign }
            }
        }
        return RudraResult(p, s, (max / 100.0).coerceIn(0.0, 1.0), RudraType.PRIMARY)
    }

    private fun findBrahma(chart: VedicChart, ascSign: ZodiacSign): Triple<Planet?, ZodiacSign?, Double> {
        var best: Planet? = null; var s: ZodiacSign? = null; var max = 0.0
        listOf(Planet.JUPITER, Planet.VENUS, Planet.MERCURY, Planet.MOON).forEach { b ->
            chart.planetPositions.find { it.planet == b }?.let { pos ->
                if (pos.house in listOf(1, 5, 9)) {
                    val sign = ZodiacSign.fromLongitude(pos.longitude); var strength = 50.0
                    if (isExalted(b, sign)) strength += 30.0; if (isOwnSign(b, sign)) strength += 20.0; strength += countBeneficAspects(chart.planetPositions, pos) * 5.0; if (pos.house in listOf(1, 4, 7, 10)) strength += 10.0
                    if (strength > max) { max = strength; best = b; s = sign }
                }
            }
        }
        return Triple(best, s, (max / 100.0).coerceIn(0.0, 1.0))
    }

    private fun findMaheshwara(chart: VedicChart, ascSign: ZodiacSign): Triple<Planet?, ZodiacSign?, String> {
        val lord = ZodiacSign.entries[(ascSign.number + 6) % 12].ruler; val pos = chart.planetPositions.find { it.planet == lord }
        return Triple(lord, pos?.let { ZodiacSign.fromLongitude(it.longitude) }, "Maheshwara is $lord (8th lord) in house ${pos?.house}")
    }

    private fun findSecondaryRudra(chart: VedicChart, primary: Planet): Planet? = listOf(Planet.SATURN, Planet.MARS, Planet.RAHU, Planet.KETU).filter { it != primary }.maxByOrNull { m -> chart.planetPositions.find { it.planet == m }?.let { (if (it.house in listOf(6, 8, 12)) 20.0 else 0.0) + (if (it.isRetrograde) 10.0 else 0.0) } ?: 0.0 }
}
