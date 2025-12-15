package com.astro.storm.data.model

import com.astro.storm.data.localization.Language
import com.astro.storm.data.localization.StringKey
import com.astro.storm.data.localization.StringResources

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

    /**
     * Get localized planet name based on current language
     */
    fun getLocalizedName(language: Language): String {
        val key = when (this) {
            SUN -> StringKey.PLANET_SUN
            MOON -> StringKey.PLANET_MOON
            MERCURY -> StringKey.PLANET_MERCURY
            VENUS -> StringKey.PLANET_VENUS
            MARS -> StringKey.PLANET_MARS
            JUPITER -> StringKey.PLANET_JUPITER
            SATURN -> StringKey.PLANET_SATURN
            RAHU -> StringKey.PLANET_RAHU
            KETU -> StringKey.PLANET_KETU
            URANUS -> StringKey.PLANET_URANUS
            NEPTUNE -> StringKey.PLANET_NEPTUNE
            PLUTO -> StringKey.PLANET_PLUTO
        }
        return StringResources.get(key, language)
    }

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
