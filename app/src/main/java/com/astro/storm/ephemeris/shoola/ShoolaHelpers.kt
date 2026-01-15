package com.astro.storm.ephemeris.shoola

import com.astro.storm.core.model.Planet
import com.astro.storm.core.model.PlanetPosition
import com.astro.storm.core.model.ZodiacSign
import kotlin.math.abs

object ShoolaHelpers {

    fun isExalted(planet: Planet, sign: ZodiacSign): Boolean = when (planet) {
        Planet.SUN -> sign == ZodiacSign.ARIES; Planet.MOON -> sign == ZodiacSign.TAURUS; Planet.MARS -> sign == ZodiacSign.CAPRICORN
        Planet.MERCURY -> sign == ZodiacSign.VIRGO; Planet.JUPITER -> sign == ZodiacSign.CANCER; Planet.VENUS -> sign == ZodiacSign.PISCES
        Planet.SATURN -> sign == ZodiacSign.LIBRA; Planet.RAHU -> sign == ZodiacSign.TAURUS || sign == ZodiacSign.GEMINI
        Planet.KETU -> sign == ZodiacSign.SCORPIO || sign == ZodiacSign.SAGITTARIUS; else -> false
    }

    fun isDebilitated(planet: Planet, sign: ZodiacSign): Boolean = when (planet) {
        Planet.SUN -> sign == ZodiacSign.LIBRA; Planet.MOON -> sign == ZodiacSign.SCORPIO; Planet.MARS -> sign == ZodiacSign.CANCER
        Planet.MERCURY -> sign == ZodiacSign.PISCES; Planet.JUPITER -> sign == ZodiacSign.CAPRICORN; Planet.VENUS -> sign == ZodiacSign.VIRGO
        Planet.SATURN -> sign == ZodiacSign.ARIES; Planet.RAHU -> sign == ZodiacSign.SCORPIO; Planet.KETU -> sign == ZodiacSign.TAURUS
        else -> false
    }

    fun isOwnSign(planet: Planet, sign: ZodiacSign): Boolean = sign.ruler == planet

    fun aspectsPoint(planet: Planet, planetLong: Double, point: Double): Boolean {
        val diff = ((point - planetLong + 360) % 360)
        return when (planet) {
            Planet.MARS -> listOf(90.0..120.0, 150.0..180.0, 210.0..240.0).any { diff >= it.start && diff <= it.endInclusive }
            Planet.JUPITER -> listOf(120.0..150.0, 150.0..180.0, 240.0..270.0).any { diff >= it.start && diff <= it.endInclusive }
            Planet.SATURN -> listOf(60.0..90.0, 150.0..180.0, 270.0..300.0).any { diff >= it.start && diff <= it.endInclusive }
            else -> abs(diff - 180.0) < 15.0
        }
    }

    fun countMaleficAspects(positions: List<PlanetPosition>, target: PlanetPosition): Int = positions.count { it.planet in listOf(Planet.SATURN, Planet.MARS, Planet.RAHU, Planet.KETU) && it.planet != target.planet && aspectsPoint(it.planet, it.longitude, target.longitude) }
    fun countBeneficAspects(positions: List<PlanetPosition>, target: PlanetPosition): Int = positions.count { it.planet in listOf(Planet.JUPITER, Planet.VENUS, Planet.MERCURY) && it.planet != target.planet && aspectsPoint(it.planet, it.longitude, target.longitude) }
}
