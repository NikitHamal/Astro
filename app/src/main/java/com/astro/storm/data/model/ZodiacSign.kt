package com.astro.storm.data.model

/**
 * Vedic zodiac signs (Rashis)
 */
import com.astro.storm.data.localization.StringKey

enum class ZodiacSign(
    val number: Int,
    val displayNameKey: StringKey,
    val abbreviation: String,
    val element: String,
    val ruler: Planet,
    val quality: Quality
) {
    ARIES(1, StringKey.SIGN_ARIES, "Ar", "Fire", Planet.MARS, Quality.CARDINAL),
    TAURUS(2, StringKey.SIGN_TAURUS, "Ta", "Earth", Planet.VENUS, Quality.FIXED),
    GEMINI(3, StringKey.SIGN_GEMINI, "Ge", "Air", Planet.MERCURY, Quality.MUTABLE),
    CANCER(4, StringKey.SIGN_CANCER, "Ca", "Water", Planet.MOON, Quality.CARDINAL),
    LEO(5, StringKey.SIGN_LEO, "Le", "Fire", Planet.SUN, Quality.FIXED),
    VIRGO(6, StringKey.SIGN_VIRGO, "Vi", "Earth", Planet.MERCURY, Quality.MUTABLE),
    LIBRA(7, StringKey.SIGN_LIBRA, "Li", "Air", Planet.VENUS, Quality.CARDINAL),
    SCORPIO(8, StringKey.SIGN_SCORPIO, "Sc", "Water", Planet.MARS, Quality.FIXED),
    SAGITTARIUS(9, StringKey.SIGN_SAGITTARIUS, "Sg", "Fire", Planet.JUPITER, Quality.MUTABLE),
    CAPRICORN(10, StringKey.SIGN_CAPRICORN, "Cp", "Earth", Planet.SATURN, Quality.CARDINAL),
    AQUARIUS(11, StringKey.SIGN_AQUARIUS, "Aq", "Air", Planet.SATURN, Quality.FIXED),
    PISCES(12, StringKey.SIGN_PISCES, "Pi", "Water", Planet.JUPITER, Quality.MUTABLE);

    val startDegree: Double get() = (number - 1) * ZODIAC_SIGN_SPAN
    val endDegree: Double get() = number * ZODIAC_SIGN_SPAN

    companion object {
        private const val ZODIAC_SIGN_SPAN = 30.0
        private val ZODIAC_SIGNS = values()

        fun fromLongitude(longitude: Double): ZodiacSign {
            val normalizedLongitude = (longitude % 360.0 + 360.0) % 360.0
            val signIndex = (normalizedLongitude / ZODIAC_SIGN_SPAN).toInt()
            return ZODIAC_SIGNS[signIndex.coerceIn(0, 11)]
        }
    }
}

enum class Quality {
    CARDINAL,
    FIXED,
    MUTABLE
}
