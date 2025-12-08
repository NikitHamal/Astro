package com.astro.storm.data.model

import androidx.annotation.StringRes
import com.astro.storm.R

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
enum class Planet(val swissEphId: Int, @StringRes val stringRes: Int, @StringRes val symbolRes: Int) {
    SUN(0, R.string.planet_sun, R.string.planet_sun_symbol),
    MOON(1, R.string.planet_moon, R.string.planet_moon_symbol),
    MERCURY(2, R.string.planet_mercury, R.string.planet_mercury_symbol),
    VENUS(3, R.string.planet_venus, R.string.planet_venus_symbol),
    MARS(4, R.string.planet_mars, R.string.planet_mars_symbol),
    JUPITER(5, R.string.planet_jupiter, R.string.planet_jupiter_symbol),
    SATURN(6, R.string.planet_saturn, R.string.planet_saturn_symbol),
    RAHU(10, R.string.planet_rahu, R.string.planet_rahu_symbol),  // Mean node (North Node)
    KETU(-1, R.string.planet_ketu, R.string.planet_ketu_symbol),  // 180° from Rahu (South Node)
    URANUS(7, R.string.planet_uranus, R.string.planet_uranus_symbol),
    NEPTUNE(8, R.string.planet_neptune, R.string.planet_neptune_symbol),
    PLUTO(9, R.string.planet_pluto, R.string.planet_pluto_symbol);

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
