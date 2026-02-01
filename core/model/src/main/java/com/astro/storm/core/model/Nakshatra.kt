package com.astro.storm.core.model

import com.astro.storm.core.common.Language
import com.astro.storm.core.common.StringKey
import com.astro.storm.core.common.StringResources

/**
 * 27 Nakshatras in Vedic astrology
 */
enum class Nakshatra(
    val number: Int,
    @Deprecated("Use stringResource(nakshatra.stringKey) or getLocalizedName(language)")
    val displayName: String,
    val ruler: Planet,
    val deity: String,
    val pada1Sign: ZodiacSign,
    val pada2Sign: ZodiacSign,
    val pada3Sign: ZodiacSign,
    val pada4Sign: ZodiacSign,
    val stringKey: com.astro.storm.core.common.StringKeyInterface
) {
    ASHWINI(1, "Ashwini", Planet.KETU, "Ashwini Kumaras",
        ZodiacSign.ARIES, ZodiacSign.ARIES, ZodiacSign.ARIES, ZodiacSign.ARIES, StringKeyNakshatra.NAKSHATRA_ASHWINI),
    BHARANI(2, "Bharani", Planet.VENUS, "Yama",
        ZodiacSign.ARIES, ZodiacSign.ARIES, ZodiacSign.ARIES, ZodiacSign.ARIES, StringKeyNakshatra.NAKSHATRA_BHARANI),
    KRITTIKA(3, "Krittika", Planet.SUN, "Agni",
        ZodiacSign.ARIES, ZodiacSign.TAURUS, ZodiacSign.TAURUS, ZodiacSign.TAURUS, StringKeyNakshatra.NAKSHATRA_KRITTIKA),
    ROHINI(4, "Rohini", Planet.MOON, "Brahma",
        ZodiacSign.TAURUS, ZodiacSign.TAURUS, ZodiacSign.TAURUS, ZodiacSign.TAURUS, StringKeyNakshatra.NAKSHATRA_ROHINI),
    MRIGASHIRA(5, "Mrigashira", Planet.MARS, "Soma",
        ZodiacSign.TAURUS, ZodiacSign.TAURUS, ZodiacSign.GEMINI, ZodiacSign.GEMINI, StringKeyNakshatra.NAKSHATRA_MRIGASHIRA),
    ARDRA(6, "Ardra", Planet.RAHU, "Rudra",
        ZodiacSign.GEMINI, ZodiacSign.GEMINI, ZodiacSign.GEMINI, ZodiacSign.GEMINI, StringKeyNakshatra.NAKSHATRA_ARDRA),
    PUNARVASU(7, "Punarvasu", Planet.JUPITER, "Aditi",
        ZodiacSign.GEMINI, ZodiacSign.GEMINI, ZodiacSign.GEMINI, ZodiacSign.CANCER, StringKeyNakshatra.NAKSHATRA_PUNARVASU),
    PUSHYA(8, "Pushya", Planet.SATURN, "Brihaspati",
        ZodiacSign.CANCER, ZodiacSign.CANCER, ZodiacSign.CANCER, ZodiacSign.CANCER, StringKeyNakshatra.NAKSHATRA_PUSHYA),
    ASHLESHA(9, "Ashlesha", Planet.MERCURY, "Sarpa",
        ZodiacSign.CANCER, ZodiacSign.CANCER, ZodiacSign.CANCER, ZodiacSign.CANCER, StringKeyNakshatra.NAKSHATRA_ASHLESHA),
    MAGHA(10, "Magha", Planet.KETU, "Pitris",
        ZodiacSign.LEO, ZodiacSign.LEO, ZodiacSign.LEO, ZodiacSign.LEO, StringKeyNakshatra.NAKSHATRA_MAGHA),
    PURVA_PHALGUNI(11, "Purva Phalguni", Planet.VENUS, "Bhaga",
        ZodiacSign.LEO, ZodiacSign.LEO, ZodiacSign.LEO, ZodiacSign.LEO, StringKeyNakshatra.NAKSHATRA_PURVA_PHALGUNI),
    UTTARA_PHALGUNI(12, "Uttara Phalguni", Planet.SUN, "Aryaman",
        ZodiacSign.LEO, ZodiacSign.VIRGO, ZodiacSign.VIRGO, ZodiacSign.VIRGO, StringKeyNakshatra.NAKSHATRA_UTTARA_PHALGUNI),
    HASTA(13, "Hasta", Planet.MOON, "Savitar",
        ZodiacSign.VIRGO, ZodiacSign.VIRGO, ZodiacSign.VIRGO, ZodiacSign.VIRGO, StringKeyNakshatra.NAKSHATRA_HASTA),
    CHITRA(14, "Chitra", Planet.MARS, "Tvashtar",
        ZodiacSign.VIRGO, ZodiacSign.VIRGO, ZodiacSign.LIBRA, ZodiacSign.LIBRA, StringKeyNakshatra.NAKSHATRA_CHITRA),
    SWATI(15, "Swati", Planet.RAHU, "Vayu",
        ZodiacSign.LIBRA, ZodiacSign.LIBRA, ZodiacSign.LIBRA, ZodiacSign.LIBRA, StringKeyNakshatra.NAKSHATRA_SWATI),
    VISHAKHA(16, "Vishakha", Planet.JUPITER, "Indra-Agni",
        ZodiacSign.LIBRA, ZodiacSign.LIBRA, ZodiacSign.LIBRA, ZodiacSign.SCORPIO, StringKeyNakshatra.NAKSHATRA_VISHAKHA),
    ANURADHA(17, "Anuradha", Planet.SATURN, "Mitra",
        ZodiacSign.SCORPIO, ZodiacSign.SCORPIO, ZodiacSign.SCORPIO, ZodiacSign.SCORPIO, StringKeyNakshatra.NAKSHATRA_ANURADHA),
    JYESHTHA(18, "Jyeshtha", Planet.MERCURY, "Indra",
        ZodiacSign.SCORPIO, ZodiacSign.SCORPIO, ZodiacSign.SCORPIO, ZodiacSign.SCORPIO, StringKeyNakshatra.NAKSHATRA_JYESHTHA),
    MULA(19, "Mula", Planet.KETU, "Nirriti",
        ZodiacSign.SAGITTARIUS, ZodiacSign.SAGITTARIUS, ZodiacSign.SAGITTARIUS, ZodiacSign.SAGITTARIUS, StringKeyNakshatra.NAKSHATRA_MULA),
    PURVA_ASHADHA(20, "Purva Ashadha", Planet.VENUS, "Apas",
        ZodiacSign.SAGITTARIUS, ZodiacSign.SAGITTARIUS, ZodiacSign.SAGITTARIUS, ZodiacSign.SAGITTARIUS, StringKeyNakshatra.NAKSHATRA_PURVA_ASHADHA),
    UTTARA_ASHADHA(21, "Uttara Ashadha", Planet.SUN, "Vishwadevas",
        ZodiacSign.SAGITTARIUS, ZodiacSign.CAPRICORN, ZodiacSign.CAPRICORN, ZodiacSign.CAPRICORN, StringKeyNakshatra.NAKSHATRA_UTTARA_ASHADHA),
    SHRAVANA(22, "Shravana", Planet.MOON, "Vishnu",
        ZodiacSign.CAPRICORN, ZodiacSign.CAPRICORN, ZodiacSign.CAPRICORN, ZodiacSign.CAPRICORN, StringKeyNakshatra.NAKSHATRA_SHRAVANA),
    DHANISHTHA(23, "Dhanishtha", Planet.MARS, "Vasus",
        ZodiacSign.CAPRICORN, ZodiacSign.CAPRICORN, ZodiacSign.AQUARIUS, ZodiacSign.AQUARIUS, StringKeyNakshatra.NAKSHATRA_DHANISHTHA),
    SHATABHISHA(24, "Shatabhisha", Planet.RAHU, "Varuna",
        ZodiacSign.AQUARIUS, ZodiacSign.AQUARIUS, ZodiacSign.AQUARIUS, ZodiacSign.AQUARIUS, StringKeyNakshatra.NAKSHATRA_SHATABHISHA),
    PURVA_BHADRAPADA(25, "Purva Bhadrapada", Planet.JUPITER, "Aja Ekapada",
        ZodiacSign.AQUARIUS, ZodiacSign.AQUARIUS, ZodiacSign.AQUARIUS, ZodiacSign.PISCES, StringKeyNakshatra.NAKSHATRA_PURVA_BHADRAPADA),
    UTTARA_BHADRAPADA(26, "Uttara Bhadrapada", Planet.SATURN, "Ahir Budhnya",
        ZodiacSign.PISCES, ZodiacSign.PISCES, ZodiacSign.PISCES, ZodiacSign.PISCES, StringKeyNakshatra.NAKSHATRA_UTTARA_BHADRAPADA),
    REVATI(27, "Revati", Planet.MERCURY, "Pushan",
        ZodiacSign.PISCES, ZodiacSign.PISCES, ZodiacSign.PISCES, ZodiacSign.PISCES, StringKeyNakshatra.NAKSHATRA_REVATI);

    val startDegree: Double get() = (number - 1) * NAKSHATRA_SPAN
    val endDegree: Double get() = number * NAKSHATRA_SPAN

    /**
     * Get localized nakshatra name based on current language
     */
    fun getLocalizedName(language: Language): String {
        return StringResources.get(stringKey, language)
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


