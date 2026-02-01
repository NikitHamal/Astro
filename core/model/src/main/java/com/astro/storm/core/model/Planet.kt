package com.astro.storm.core.model

import com.astro.storm.core.common.Language
import com.astro.storm.core.common.StringKey
import com.astro.storm.core.common.StringKeyAnalysis
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
    val stringKey: com.astro.storm.core.common.StringKeyInterface,
    val abbrKey: com.astro.storm.core.common.StringKeyInterface
) {
    SUN(0, "Sun", "Su", StringKeyGeneralPart8.PLANET_SUN, StringKeyGeneralPart8.PLANET_SUN_ABBR),
    MOON(1, "Moon", "Mo", StringKeyGeneralPart8.PLANET_MOON, StringKeyGeneralPart8.PLANET_MOON_ABBR),
    MERCURY(2, "Mercury", "Me", StringKeyGeneralPart8.PLANET_MERCURY, StringKeyGeneralPart8.PLANET_MERCURY_ABBR),
    VENUS(3, "Venus", "Ve", StringKeyGeneralPart8.PLANET_VENUS, StringKeyGeneralPart8.PLANET_VENUS_ABBR),
    MARS(4, "Mars", "Ma", StringKeyGeneralPart8.PLANET_MARS, StringKeyGeneralPart8.PLANET_MARS_ABBR),
    JUPITER(5, "Jupiter", "Ju", StringKeyGeneralPart8.PLANET_JUPITER, StringKeyGeneralPart8.PLANET_JUPITER_ABBR),
    SATURN(6, "Saturn", "Sa", StringKeyGeneralPart8.PLANET_SATURN, StringKeyGeneralPart8.PLANET_SATURN_ABBR),
    RAHU(10, "Rahu", "Ra", StringKeyGeneralPart8.PLANET_RAHU, StringKeyGeneralPart8.PLANET_RAHU_ABBR),  // Mean node (North Node)
    KETU(-1, "Ketu", "Ke", StringKeyGeneralPart8.PLANET_KETU, StringKeyGeneralPart8.PLANET_KETU_ABBR),  // 180° from Rahu (South Node)
    TRUE_NODE(11, "True Rahu", "TRa", StringKeyGeneralPart8.PLANET_RAHU, StringKeyGeneralPart8.PLANET_RAHU_ABBR), // True node
    URANUS(7, "Uranus", "Ur", StringKeyGeneralPart8.PLANET_URANUS, StringKeyGeneralPart8.PLANET_URANUS_ABBR),
    NEPTUNE(8, "Neptune", "Ne", StringKeyGeneralPart8.PLANET_NEPTUNE, StringKeyGeneralPart8.PLANET_NEPTUNE_ABBR),
    PLUTO(9, "Pluto", "Pl", StringKeyGeneralPart8.PLANET_PLUTO, StringKeyGeneralPart8.PLANET_PLUTO_ABBR);

    /**
     * Get localized planet name based on current language
     */
    fun getLocalizedName(language: Language): String {
        return StringResources.get(stringKey, language)
    }

    /**
     * Get localized abbreviation based on current language
     */
    fun getLocalizedAbbr(language: Language): String {
        return StringResources.get(abbrKey, language)
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


