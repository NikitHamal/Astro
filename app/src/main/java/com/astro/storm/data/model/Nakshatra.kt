package com.astro.storm.data.model

import com.astro.storm.data.localization.Language
import com.astro.storm.data.localization.StringKey
import com.astro.storm.data.localization.StringResources

/**
 * 27 Nakshatras in Vedic astrology
 */
enum class Nakshatra(
    val number: Int,
    val ruler: Planet,
    val pada1Sign: ZodiacSign,
    val pada2Sign: ZodiacSign,
    val pada3Sign: ZodiacSign,
    val pada4Sign: ZodiacSign
) {
    ASHWINI(1, Planet.KETU,
        ZodiacSign.ARIES, ZodiacSign.ARIES, ZodiacSign.ARIES, ZodiacSign.ARIES),
    BHARANI(2, Planet.VENUS,
        ZodiacSign.ARIES, ZodiacSign.ARIES, ZodiacSign.ARIES, ZodiacSign.ARIES),
    KRITTIKA(3, Planet.SUN,
        ZodiacSign.ARIES, ZodiacSign.TAURUS, ZodiacSign.TAURUS, ZodiacSign.TAURUS),
    ROHINI(4, Planet.MOON,
        ZodiacSign.TAURUS, ZodiacSign.TAURUS, ZodiacSign.TAURUS, ZodiacSign.TAURUS),
    MRIGASHIRA(5, Planet.MARS,
        ZodiacSign.TAURUS, ZodiacSign.TAURUS, ZodiacSign.GEMINI, ZodiacSign.GEMINI),
    ARDRA(6, Planet.RAHU,
        ZodiacSign.GEMINI, ZodiacSign.GEMINI, ZodiacSign.GEMINI, ZodiacSign.GEMINI),
    PUNARVASU(7, Planet.JUPITER,
        ZodiacSign.GEMINI, ZodiacSign.GEMINI, ZodiacSign.GEMINI, ZodiacSign.CANCER),
    PUSHYA(8, Planet.SATURN,
        ZodiacSign.CANCER, ZodiacSign.CANCER, ZodiacSign.CANCER, ZodiacSign.CANCER),
    ASHLESHA(9, Planet.MERCURY,
        ZodiacSign.CANCER, ZodiacSign.CANCER, ZodiacSign.CANCER, ZodiacSign.CANCER),
    MAGHA(10, Planet.KETU,
        ZodiacSign.LEO, ZodiacSign.LEO, ZodiacSign.LEO, ZodiacSign.LEO),
    PURVA_PHALGUNI(11, Planet.VENUS,
        ZodiacSign.LEO, ZodiacSign.LEO, ZodiacSign.LEO, ZodiacSign.LEO),
    UTTARA_PHALGUNI(12, Planet.SUN,
        ZodiacSign.LEO, ZodiacSign.VIRGO, ZodiacSign.VIRGO, ZodiacSign.VIRGO),
    HASTA(13, Planet.MOON,
        ZodiacSign.VIRGO, ZodiacSign.VIRGO, ZodiacSign.VIRGO, ZodiacSign.VIRGO),
    CHITRA(14, Planet.MARS,
        ZodiacSign.VIRGO, ZodiacSign.VIRGO, ZodiacSign.LIBRA, ZodiacSign.LIBRA),
    SWATI(15, Planet.RAHU,
        ZodiacSign.LIBRA, ZodiacSign.LIBRA, ZodiacSign.LIBRA, ZodiacSign.LIBRA),
    VISHAKHA(16, Planet.JUPITER,
        ZodiacSign.LIBRA, ZodiacSign.LIBRA, ZodiacSign.LIBRA, ZodiacSign.SCORPIO),
    ANURADHA(17, Planet.SATURN,
        ZodiacSign.SCORPIO, ZodiacSign.SCORPIO, ZodiacSign.SCORPIO, ZodiacSign.SCORPIO),
    JYESHTHA(18, Planet.MERCURY,
        ZodiacSign.SCORPIO, ZodiacSign.SCORPIO, ZodiacSign.SCORPIO, ZodiacSign.SCORPIO),
    MULA(19, Planet.KETU,
        ZodiacSign.SAGITTARIUS, ZodiacSign.SAGITTARIUS, ZodiacSign.SAGITTARIUS, ZodiacSign.SAGITTARIUS),
    PURVA_ASHADHA(20, Planet.VENUS,
        ZodiacSign.SAGITTARIUS, ZodiacSign.SAGITTARIUS, ZodiacSign.SAGITTARIUS, ZodiacSign.SAGITTARIUS),
    UTTARA_ASHADHA(21, Planet.SUN,
        ZodiacSign.SAGITTARIUS, ZodiacSign.CAPRICORN, ZodiacSign.CAPRICORN, ZodiacSign.CAPRICORN),
    SHRAVANA(22, Planet.MOON,
        ZodiacSign.CAPRICORN, ZodiacSign.CAPRICORN, ZodiacSign.CAPRICORN, ZodiacSign.CAPRICORN),
    DHANISHTHA(23, Planet.MARS,
        ZodiacSign.CAPRICORN, ZodiacSign.CAPRICORN, ZodiacSign.AQUARIUS, ZodiacSign.AQUARIUS),
    SHATABHISHA(24, Planet.RAHU,
        ZodiacSign.AQUARIUS, ZodiacSign.AQUARIUS, ZodiacSign.AQUARIUS, ZodiacSign.AQUARIUS),
    PURVA_BHADRAPADA(25, Planet.JUPITER,
        ZodiacSign.AQUARIUS, ZodiacSign.AQUARIUS, ZodiacSign.AQUARIUS, ZodiacSign.PISCES),
    UTTARA_BHADRAPADA(26, Planet.SATURN,
        ZodiacSign.PISCES, ZodiacSign.PISCES, ZodiacSign.PISCES, ZodiacSign.PISCES),
    REVATI(27, Planet.MERCURY,
        ZodiacSign.PISCES, ZodiacSign.PISCES, ZodiacSign.PISCES, ZodiacSign.PISCES);

    val startDegree: Double get() = (number - 1) * NAKSHATRA_SPAN
    val endDegree: Double get() = number * NAKSHATRA_SPAN

    /**
     * Get localized nakshatra name based on current language
     */
    fun getLocalizedName(language: Language): String {
        val key = when (this) {
            ASHWINI -> StringKey.NAKSHATRA_ASHWINI
            BHARANI -> StringKey.NAKSHATRA_BHARANI
            KRITTIKA -> StringKey.NAKSHATRA_KRITTIKA
            ROHINI -> StringKey.NAKSHATRA_ROHINI
            MRIGASHIRA -> StringKey.NAKSHATRA_MRIGASHIRA
            ARDRA -> StringKey.NAKSHATRA_ARDRA
            PUNARVASU -> StringKey.NAKSHATRA_PUNARVASU
            PUSHYA -> StringKey.NAKSHATRA_PUSHYA
            ASHLESHA -> StringKey.NAKSHATRA_ASHLESHA
            MAGHA -> StringKey.NAKSHATRA_MAGHA
            PURVA_PHALGUNI -> StringKey.NAKSHATRA_PURVA_PHALGUNI
            UTTARA_PHALGUNI -> StringKey.NAKSHATRA_UTTARA_PHALGUNI
            HASTA -> StringKey.NAKSHATRA_HASTA
            CHITRA -> StringKey.NAKSHATRA_CHITRA
            SWATI -> StringKey.NAKSHATRA_SWATI
            VISHAKHA -> StringKey.NAKSHATRA_VISHAKHA
            ANURADHA -> StringKey.NAKSHATRA_ANURADHA
            JYESHTHA -> StringKey.NAKSHATRA_JYESHTHA
            MULA -> StringKey.NAKSHATRA_MULA
            PURVA_ASHADHA -> StringKey.NAKSHATRA_PURVA_ASHADHA
            UTTARA_ASHADHA -> StringKey.NAKSHATRA_UTTARA_ASHADHA
            SHRAVANA -> StringKey.NAKSHATRA_SHRAVANA
            DHANISHTHA -> StringKey.NAKSHATRA_DHANISHTHA
            SHATABHISHA -> StringKey.NAKSHATRA_SHATABHISHA
            PURVA_BHADRAPADA -> StringKey.NAKSHATRA_PURVA_BHADRAPADA
            UTTARA_BHADRAPADA -> StringKey.NAKSHATRA_UTTARA_BHADRAPADA
            REVATI -> StringKey.NAKSHATRA_REVATI
        }
        return StringResources.get(key, language)
    }

    companion object {
        private const val NAKSHATRA_SPAN = 360.0 / 27.0 // ~13.333 degrees

        fun fromLongitude(longitude: Double): Pair<Nakshatra, Int> {
            val longitudeBd = longitude.toBigDecimal()
            val circleBd = 360.toBigDecimal()
            val nakshatraSpanBd = NAKSHATRA_SPAN.toBigDecimal()
            val padaSpanBd = nakshatraSpanBd / 4.toBigDecimal()

            val normalizedLongitude = (longitudeBd % circleBd + circleBd) % circleBd

            val nakshatraIndex = (normalizedLongitude / nakshatraSpanBd).toInt().coerceIn(0, 26)
            val nakshatra = values()[nakshatraIndex]

            val positionInNakshatra = normalizedLongitude - nakshatra.startDegree.toBigDecimal()
            val pada = (positionInNakshatra / padaSpanBd).toInt() + 1
            return nakshatra to pada.coerceIn(1, 4)
        }
    }
}
