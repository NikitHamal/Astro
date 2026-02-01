package com.astro.storm.core.model

import com.astro.storm.core.common.Language
import com.astro.storm.core.common.StringKeyGeneralPart10
import com.astro.storm.core.common.StringResources

/**
 * Vedic zodiac signs (Rashis)
 */
enum class ZodiacSign(
    val number: Int,
    @Deprecated("Use stringResource(sign.stringKey) or getLocalizedName(language)")
    val displayName: String,
    val abbreviation: String,
    val element: String,
    val ruler: Planet,
    val quality: Quality,
    val symbol: String,
    val stringKey: com.astro.storm.core.common.StringKeyInterface
) {
    ARIES(1, "Aries", "Ar", "Fire", Planet.MARS, Quality.CARDINAL, "♈", StringKeyGeneralPart10.SIGN_ARIES),
    TAURUS(2, "Taurus", "Ta", "Earth", Planet.VENUS, Quality.FIXED, "♉", StringKeyGeneralPart10.SIGN_TAURUS),
    GEMINI(3, "Gemini", "Ge", "Air", Planet.MERCURY, Quality.MUTABLE, "♊", StringKeyGeneralPart10.SIGN_GEMINI),
    CANCER(4, "Cancer", "Ca", "Water", Planet.MOON, Quality.CARDINAL, "♋", StringKeyGeneralPart10.SIGN_CANCER),
    LEO(5, "Leo", "Le", "Fire", Planet.SUN, Quality.FIXED, "♌", StringKeyGeneralPart10.SIGN_LEO),
    VIRGO(6, "Virgo", "Vi", "Earth", Planet.MERCURY, Quality.MUTABLE, "♍", StringKeyGeneralPart10.SIGN_VIRGO),
    LIBRA(7, "Libra", "Li", "Air", Planet.VENUS, Quality.CARDINAL, "♎", StringKeyGeneralPart10.SIGN_LIBRA),
    SCORPIO(8, "Scorpio", "Sc", "Water", Planet.MARS, Quality.FIXED, "♏", StringKeyGeneralPart10.SIGN_SCORPIO),
    SAGITTARIUS(9, "Sagittarius", "Sg", "Fire", Planet.JUPITER, Quality.MUTABLE, "♐", StringKeyGeneralPart10.SIGN_SAGITTARIUS),
    CAPRICORN(10, "Capricorn", "Cp", "Earth", Planet.SATURN, Quality.CARDINAL, "♑", StringKeyGeneralPart10.SIGN_CAPRICORN),
    AQUARIUS(11, "Aquarius", "Aq", "Air", Planet.SATURN, Quality.FIXED, "♒", StringKeyGeneralPart10.SIGN_AQUARIUS),
    PISCES(12, "Pisces", "Pi", "Water", Planet.JUPITER, Quality.MUTABLE, "♓", StringKeyGeneralPart10.SIGN_PISCES);

    val startDegree: Double get() = (number - 1) * ZODIAC_SIGN_SPAN
    val endDegree: Double get() = number * ZODIAC_SIGN_SPAN

    /**
     * Get localized sign name based on current language
     */
    fun getLocalizedName(language: Language): String {
        return StringResources.get(stringKey, language)
    }

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


