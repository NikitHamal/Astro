package com.astro.storm.data.model

import androidx.annotation.StringRes
import com.astro.storm.R

/**
 * Vedic zodiac signs (Rashis)
 */
enum class ZodiacSign(
    val number: Int,
    @StringRes val stringRes: Int,
    @StringRes val abbreviationRes: Int,
    val element: String,
    val ruler: Planet,
    val quality: Quality
) {
    ARIES(1, R.string.zodiac_aries, R.string.zodiac_aries_abbreviation, "Fire", Planet.MARS, Quality.CARDINAL),
    TAURUS(2, R.string.zodiac_taurus, R.string.zodiac_taurus_abbreviation, "Earth", Planet.VENUS, Quality.FIXED),
    GEMINI(3, R.string.zodiac_gemini, R.string.zodiac_gemini_abbreviation, "Air", Planet.MERCURY, Quality.MUTABLE),
    CANCER(4, R.string.zodiac_cancer, R.string.zodiac_cancer_abbreviation, "Water", Planet.MOON, Quality.CARDINAL),
    LEO(5, R.string.zodiac_leo, R.string.zodiac_leo_abbreviation, "Fire", Planet.SUN, Quality.FIXED),
    VIRGO(6, R.string.zodiac_virgo, R.string.zodiac_virgo_abbreviation, "Earth", Planet.MERCURY, Quality.MUTABLE),
    LIBRA(7, R.string.zodiac_libra, R.string.zodiac_libra_abbreviation, "Air", Planet.VENUS, Quality.CARDINAL),
    SCORPIO(8, R.string.zodiac_scorpio, R.string.zodiac_scorpio_abbreviation, "Water", Planet.MARS, Quality.FIXED),
    SAGITTARIUS(9, R.string.zodiac_sagittarius, R.string.zodiac_sagittarius_abbreviation, "Fire", Planet.JUPITER, Quality.MUTABLE),
    CAPRICORN(10, R.string.zodiac_capricorn, R.string.zodiac_capricorn_abbreviation, "Earth", Planet.SATURN, Quality.CARDINAL),
    AQUARIUS(11, R.string.zodiac_aquarius, R.string.zodiac_aquarius_abbreviation, "Air", Planet.SATURN, Quality.FIXED),
    PISCES(12, R.string.zodiac_pisces, R.string.zodiac_pisces_abbreviation, "Water", Planet.JUPITER, Quality.MUTABLE);

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
