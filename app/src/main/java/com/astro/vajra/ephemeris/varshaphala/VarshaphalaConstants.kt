package com.astro.vajra.ephemeris.varshaphala

import com.astro.vajra.core.model.Planet
import com.astro.vajra.core.model.ZodiacSign
import swisseph.SweConst

object VarshaphalaConstants {
    const val AYANAMSA_LAHIRI = SweConst.SE_SIDM_LAHIRI
    const val SEFLG_SIDEREAL = SweConst.SEFLG_SIDEREAL
    const val SEFLG_SPEED = SweConst.SEFLG_SPEED
    const val SIDEREAL_YEAR_DAYS = 365.256363

    const val CONJUNCTION_ORB = 12.0
    const val OPPOSITION_ORB = 9.0
    const val TRINE_ORB = 8.0
    const val SQUARE_ORB = 7.0
    const val SEXTILE_ORB = 6.0

    val VIMSHOTTARI_YEARS = mapOf(
        Planet.KETU to 7,
        Planet.VENUS to 20,
        Planet.SUN to 6,
        Planet.MOON to 10,
        Planet.MARS to 7,
        Planet.RAHU to 18,
        Planet.JUPITER to 16,
        Planet.SATURN to 19,
        Planet.MERCURY to 17
    )

    val VIMSHOTTARI_ORDER = listOf(
        Planet.KETU, Planet.VENUS, Planet.SUN, Planet.MOON, Planet.MARS,
        Planet.RAHU, Planet.JUPITER, Planet.SATURN, Planet.MERCURY
    )

    val DAY_LORDS = listOf(
        Planet.SUN,
        Planet.MOON,
        Planet.MARS,
        Planet.MERCURY,
        Planet.JUPITER,
        Planet.VENUS,
        Planet.SATURN
    )

    val HADDA_LORDS = mapOf(
        ZodiacSign.ARIES to listOf(
            Triple(0.0, 6.0, Planet.JUPITER),
            Triple(6.0, 12.0, Planet.VENUS),
            Triple(12.0, 20.0, Planet.MERCURY),
            Triple(20.0, 25.0, Planet.MARS),
            Triple(25.0, 30.0, Planet.SATURN)
        ),
        ZodiacSign.TAURUS to listOf(
            Triple(0.0, 8.0, Planet.VENUS),
            Triple(8.0, 14.0, Planet.MERCURY),
            Triple(14.0, 22.0, Planet.JUPITER),
            Triple(22.0, 27.0, Planet.SATURN),
            Triple(27.0, 30.0, Planet.MARS)
        ),
        ZodiacSign.GEMINI to listOf(
            Triple(0.0, 6.0, Planet.MERCURY),
            Triple(6.0, 12.0, Planet.JUPITER),
            Triple(12.0, 17.0, Planet.VENUS),
            Triple(17.0, 24.0, Planet.MARS),
            Triple(24.0, 30.0, Planet.SATURN)
        ),
        ZodiacSign.CANCER to listOf(
            Triple(0.0, 7.0, Planet.MARS),
            Triple(7.0, 13.0, Planet.VENUS),
            Triple(13.0, 19.0, Planet.MERCURY),
            Triple(19.0, 26.0, Planet.JUPITER),
            Triple(26.0, 30.0, Planet.SATURN)
        ),
        ZodiacSign.LEO to listOf(
            Triple(0.0, 6.0, Planet.JUPITER),
            Triple(6.0, 11.0, Planet.VENUS),
            Triple(11.0, 18.0, Planet.SATURN),
            Triple(18.0, 24.0, Planet.MERCURY),
            Triple(24.0, 30.0, Planet.MARS)
        ),
        ZodiacSign.VIRGO to listOf(
            Triple(0.0, 7.0, Planet.MERCURY),
            Triple(7.0, 17.0, Planet.VENUS),
            Triple(17.0, 21.0, Planet.JUPITER),
            Triple(21.0, 28.0, Planet.MARS),
            Triple(28.0, 30.0, Planet.SATURN)
        ),
        ZodiacSign.LIBRA to listOf(
            Triple(0.0, 6.0, Planet.SATURN),
            Triple(6.0, 14.0, Planet.MERCURY),
            Triple(14.0, 21.0, Planet.JUPITER),
            Triple(21.0, 28.0, Planet.VENUS),
            Triple(28.0, 30.0, Planet.MARS)
        ),
        ZodiacSign.SCORPIO to listOf(
            Triple(0.0, 7.0, Planet.MARS),
            Triple(7.0, 11.0, Planet.VENUS),
            Triple(11.0, 19.0, Planet.MERCURY),
            Triple(19.0, 24.0, Planet.JUPITER),
            Triple(24.0, 30.0, Planet.SATURN)
        ),
        ZodiacSign.SAGITTARIUS to listOf(
            Triple(0.0, 12.0, Planet.JUPITER),
            Triple(12.0, 17.0, Planet.VENUS),
            Triple(17.0, 21.0, Planet.MERCURY),
            Triple(21.0, 26.0, Planet.SATURN),
            Triple(26.0, 30.0, Planet.MARS)
        ),
        ZodiacSign.CAPRICORN to listOf(
            Triple(0.0, 7.0, Planet.MERCURY),
            Triple(7.0, 14.0, Planet.JUPITER),
            Triple(14.0, 22.0, Planet.VENUS),
            Triple(22.0, 26.0, Planet.SATURN),
            Triple(26.0, 30.0, Planet.MARS)
        ),
        ZodiacSign.AQUARIUS to listOf(
            Triple(0.0, 7.0, Planet.MERCURY),
            Triple(7.0, 13.0, Planet.VENUS),
            Triple(13.0, 20.0, Planet.JUPITER),
            Triple(20.0, 25.0, Planet.MARS),
            Triple(25.0, 30.0, Planet.SATURN)
        ),
        ZodiacSign.PISCES to listOf(
            Triple(0.0, 12.0, Planet.VENUS),
            Triple(12.0, 16.0, Planet.JUPITER),
            Triple(16.0, 19.0, Planet.MERCURY),
            Triple(19.0, 28.0, Planet.MARS),
            Triple(28.0, 30.0, Planet.SATURN)
        )
    )

    val EXALTATION_DEGREES = mapOf(
        Planet.SUN to 10.0,
        Planet.MOON to 33.0,
        Planet.MARS to 298.0,
        Planet.MERCURY to 165.0,
        Planet.JUPITER to 95.0,
        Planet.VENUS to 357.0,
        Planet.SATURN to 200.0
    )

    val DEBILITATION_SIGNS = mapOf(
        Planet.SUN to ZodiacSign.LIBRA,
        Planet.MOON to ZodiacSign.SCORPIO,
        Planet.MARS to ZodiacSign.CANCER,
        Planet.MERCURY to ZodiacSign.PISCES,
        Planet.JUPITER to ZodiacSign.CAPRICORN,
        Planet.VENUS to ZodiacSign.VIRGO,
        Planet.SATURN to ZodiacSign.ARIES
    )

    val OWN_SIGNS = mapOf(
        Planet.SUN to listOf(ZodiacSign.LEO),
        Planet.MOON to listOf(ZodiacSign.CANCER),
        Planet.MARS to listOf(ZodiacSign.ARIES, ZodiacSign.SCORPIO),
        Planet.MERCURY to listOf(ZodiacSign.GEMINI, ZodiacSign.VIRGO),
        Planet.JUPITER to listOf(ZodiacSign.SAGITTARIUS, ZodiacSign.PISCES),
        Planet.VENUS to listOf(ZodiacSign.TAURUS, ZodiacSign.LIBRA),
        Planet.SATURN to listOf(ZodiacSign.CAPRICORN, ZodiacSign.AQUARIUS)
    )

    val FRIENDSHIPS = mapOf(
        Planet.SUN to listOf(Planet.MOON, Planet.MARS, Planet.JUPITER),
        Planet.MOON to listOf(Planet.SUN, Planet.MERCURY),
        Planet.MARS to listOf(Planet.SUN, Planet.MOON, Planet.JUPITER),
        Planet.MERCURY to listOf(Planet.SUN, Planet.VENUS),
        Planet.JUPITER to listOf(Planet.SUN, Planet.MOON, Planet.MARS),
        Planet.VENUS to listOf(Planet.MERCURY, Planet.SATURN),
        Planet.SATURN to listOf(Planet.MERCURY, Planet.VENUS)
    )

    val NEUTRALS = mapOf(
        Planet.SUN to listOf(Planet.MERCURY),
        Planet.MOON to listOf(Planet.MARS, Planet.JUPITER, Planet.VENUS, Planet.SATURN),
        Planet.MARS to listOf(Planet.MERCURY, Planet.VENUS, Planet.SATURN),
        Planet.MERCURY to listOf(Planet.MARS, Planet.JUPITER, Planet.SATURN),
        Planet.JUPITER to listOf(Planet.MERCURY, Planet.SATURN),
        Planet.VENUS to listOf(Planet.MARS, Planet.JUPITER),
        Planet.SATURN to listOf(Planet.MARS, Planet.JUPITER)
    )

    /** Mudda Dasha planets follow Vimshottari order */
    val MUDDA_DASHA_PLANETS = VIMSHOTTARI_ORDER

    /**
     * Mudda Dasha days for a 360-day Savana year.
     * Calculated as (Vimshottari Years / 120) * 360.
     */
    val MUDDA_DASHA_DAYS = mapOf(
        Planet.KETU to 21,
        Planet.VENUS to 60,
        Planet.SUN to 18,
        Planet.MOON to 30,
        Planet.MARS to 21,
        Planet.RAHU to 54,
        Planet.JUPITER to 48,
        Planet.SATURN to 57,
        Planet.MERCURY to 51
    )

    val STANDARD_ZODIAC_SIGNS = listOf(
        ZodiacSign.ARIES, ZodiacSign.TAURUS, ZodiacSign.GEMINI, ZodiacSign.CANCER,
        ZodiacSign.LEO, ZodiacSign.VIRGO, ZodiacSign.LIBRA, ZodiacSign.SCORPIO,
        ZodiacSign.SAGITTARIUS, ZodiacSign.CAPRICORN, ZodiacSign.AQUARIUS, ZodiacSign.PISCES
    )
}
