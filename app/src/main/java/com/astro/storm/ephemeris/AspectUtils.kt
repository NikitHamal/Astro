package com.astro.storm.ephemeris

import com.astro.storm.core.model.Planet
import com.astro.storm.core.model.PlanetPosition

/**
 * Utility functions for Parashari aspects.
 */
object AspectUtils {
    fun aspectsHouse(planetPos: PlanetPosition, targetHouse: Int): Boolean {
        val planetHouse = planetPos.house
        val diff = (targetHouse - planetHouse + 12) % 12
        if (diff == 6) return true
        return when (planetPos.planet) {
            Planet.MARS -> diff in listOf(3, 7)
            Planet.JUPITER -> diff in listOf(4, 8)
            Planet.SATURN -> diff in listOf(2, 9)
            Planet.RAHU, Planet.KETU -> diff in listOf(4, 8)
            else -> false
        }
    }
}
