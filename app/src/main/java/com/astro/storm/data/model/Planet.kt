package com.astro.storm.data.model

import com.astro.storm.R
import com.astro.storm.data.localization.LocalizableString

/**
 * Represents planets used in Vedic astrology
 *
 * Traditional Vedic astrology uses 9 grahas (planets):
 * - 7 classical planets: Sun, Moon, Mars, Mercury, Jupiter, Venus, Saturn
 * - 2 lunar nodes: Rahu (North Node), Ketu (South Node)
 *
 * Modern additions (optional):
 * - Outer planets: Uranus, Neptune, Pluto
 */
enum class Planet(val swissEphId: Int, val symbol: String) {
    SUN(0, "Su"),
    MOON(1, "Mo"),
    MERCURY(2, "Me"),
    VENUS(3, "Ve"),
    MARS(4, "Ma"),
    JUPITER(5, "Ju"),
    SATURN(6, "Sa"),
    RAHU(10, "Ra"),  // Mean node (North Node)
    KETU(-1, "Ke"),  // 180° from Rahu (South Node)
    URANUS(7, "Ur"),
    NEPTUNE(8, "Ne"),
    PLUTO(9, "Pl");

    val displayName: LocalizableString
        get() = LocalizableString.Resource(
            when (this) {
                SUN -> R.string.planet_sun
                MOON -> R.string.planet_moon
                MERCURY -> R.string.planet_mercury
                VENUS -> R.string.planet_venus
                MARS -> R.string.planet_mars
                JUPITER -> R.string.planet_jupiter
                SATURN -> R.string.planet_saturn
                RAHU -> R.string.planet_rahu
                KETU -> R.string.planet_ketu
                URANUS -> R.string.planet_uranus
                NEPTUNE -> R.string.planet_neptune
                PLUTO -> R.string.planet_pluto
            }
        )

    companion object {
        /**
         * Traditional 9 Vedic planets (grahas)
         * Used for classical Vedic astrology calculations
         */
        val MAIN_PLANETS = listOf(SUN, MOON, MARS, MERCURY, JUPITER, VENUS, SATURN, RAHU, KETU)

        /**
         * All planets including modern outer planets
         * Matches AstroSage's complete planet display
         */
        val ALL_PLANETS = listOf(SUN, MOON, MARS, MERCURY, JUPITER, VENUS, SATURN, RAHU, KETU, URANUS, NEPTUNE, PLUTO)

        /**
         * Outer planets (trans-Saturnian)
         * These are modern additions not traditionally used in Vedic astrology
         */
        val OUTER_PLANETS = listOf(URANUS, NEPTUNE, PLUTO)
    }
}
