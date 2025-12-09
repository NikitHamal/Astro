package com.astro.storm.data.model

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
import com.astro.storm.data.localization.StringKey

enum class Planet(val swissEphId: Int, val displayNameKey: StringKey, val symbol: String) {
    SUN(0, StringKey.PLANET_SUN, "Su"),
    MOON(1, StringKey.PLANET_MOON, "Mo"),
    MERCURY(2, StringKey.PLANET_MERCURY, "Me"),
    VENUS(3, StringKey.PLANET_VENUS, "Ve"),
    MARS(4, StringKey.PLANET_MARS, "Ma"),
    JUPITER(5, StringKey.PLANET_JUPITER, "Ju"),
    SATURN(6, StringKey.PLANET_SATURN, "Sa"),
    RAHU(10, StringKey.PLANET_RAHU, "Ra"),  // Mean node (North Node)
    KETU(-1, StringKey.PLANET_KETU, "Ke"),  // 180° from Rahu (South Node)
    URANUS(7, StringKey.PLANET_URANUS, "Ur"),
    NEPTUNE(8, StringKey.PLANET_NEPTUNE, "Ne"),
    PLUTO(9, StringKey.PLANET_PLUTO, "Pl");

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
