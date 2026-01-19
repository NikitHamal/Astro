package com.astro.storm.core.model

import com.astro.storm.core.common.Language
import com.astro.storm.core.common.StringKey
import com.astro.storm.core.common.StringResources

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
enum class Planet(
    val swissEphId: Int,
    @Deprecated("Use stringResource(planet.stringKey) or getLocalizedName(language)")
    val displayName: String,
    val symbol: String,
    val stringKey: com.astro.storm.core.common.StringKeyInterface
) {
    SUN(0, "Sun", "Su", StringKey.PLANET_SUN),
    MOON(1, "Moon", "Mo", StringKey.PLANET_MOON),
    MERCURY(2, "Mercury", "Me", StringKey.PLANET_MERCURY),
    VENUS(3, "Venus", "Ve", StringKey.PLANET_VENUS),
    MARS(4, "Mars", "Ma", StringKey.PLANET_MARS),
    JUPITER(5, "Jupiter", "Ju", StringKey.PLANET_JUPITER),
    SATURN(6, "Saturn", "Sa", StringKey.PLANET_SATURN),
    RAHU(10, "Rahu", "Ra", StringKey.PLANET_RAHU),  // Mean node (North Node)
    KETU(-1, "Ketu", "Ke", StringKey.PLANET_KETU),  // 180° from Rahu (South Node)
    TRUE_NODE(11, "True Rahu", "TRa", StringKey.PLANET_RAHU), // True node
    URANUS(7, "Uranus", "Ur", StringKey.PLANET_URANUS),
    NEPTUNE(8, "Neptune", "Ne", StringKey.PLANET_NEPTUNE),
    PLUTO(9, "Pluto", "Pl", StringKey.PLANET_PLUTO);

    /**
     * Get localized planet name based on current language
     */
    fun getLocalizedName(language: Language): String {
        return StringResources.get(stringKey, language)
    }

    /**
     * Check if the planet is a shadow planet (Rahu or Ketu)
     */
    val isShadowPlanet: Boolean
        get() = this == RAHU || this == KETU || this == TRUE_NODE

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


