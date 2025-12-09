package com.astro.storm.data.model

/**
 * 27 Nakshatras in Vedic astrology
 */
import com.astro.storm.data.localization.StringKey

enum class Nakshatra(
    val number: Int,
    val displayNameKey: StringKey,
    val ruler: Planet,
    val deity: String,
    val pada1Sign: ZodiacSign,
    val pada2Sign: ZodiacSign,
    val pada3Sign: ZodiacSign,
    val pada4Sign: ZodiacSign
) {
    ASHWINI(1, StringKey.NAKSHATRA_ASHWINI, Planet.KETU, "Ashwini Kumaras",
        ZodiacSign.ARIES, ZodiacSign.ARIES, ZodiacSign.ARIES, ZodiacSign.ARIES),
    BHARANI(2, StringKey.NAKSHATRA_BHARANI, Planet.VENUS, "Yama",
        ZodiacSign.ARIES, ZodiacSign.ARIES, ZodiacSign.ARIES, ZodiacSign.ARIES),
    KRITTIKA(3, StringKey.NAKSHATRA_KRITTIKA, Planet.SUN, "Agni",
        ZodiacSign.ARIES, ZodiacSign.TAURUS, ZodiacSign.TAURUS, ZodiacSign.TAURUS),
    ROHINI(4, StringKey.NAKSHATRA_ROHINI, Planet.MOON, "Brahma",
        ZodiacSign.TAURUS, ZodiacSign.TAURUS, ZodiacSign.TAURUS, ZodiacSign.TAURUS),
    MRIGASHIRA(5, StringKey.NAKSHATRA_MRIGASHIRA, Planet.MARS, "Soma",
        ZodiacSign.TAURUS, ZodiacSign.TAURUS, ZodiacSign.GEMINI, ZodiacSign.GEMINI),
    ARDRA(6, StringKey.NAKSHATRA_ARDRA, Planet.RAHU, "Rudra",
        ZodiacSign.GEMINI, ZodiacSign.GEMINI, ZodiacSign.GEMINI, ZodiacSign.GEMINI),
    PUNARVASU(7, StringKey.NAKSHATRA_PUNARVASU, Planet.JUPITER, "Aditi",
        ZodiacSign.GEMINI, ZodiacSign.GEMINI, ZodiacSign.GEMINI, ZodiacSign.CANCER),
    PUSHYA(8, StringKey.NAKSHATRA_PUSHYA, Planet.SATURN, "Brihaspati",
        ZodiacSign.CANCER, ZodiacSign.CANCER, ZodiacSign.CANCER, ZodiacSign.CANCER),
    ASHLESHA(9, StringKey.NAKSHATRA_ASHLESHA, Planet.MERCURY, "Sarpa",
        ZodiacSign.CANCER, ZodiacSign.CANCER, ZodiacSign.CANCER, ZodiacSign.CANCER),
    MAGHA(10, StringKey.NAKSHATRA_MAGHA, Planet.KETU, "Pitris",
        ZodiacSign.LEO, ZodiacSign.LEO, ZodiacSign.LEO, ZodiacSign.LEO),
    PURVA_PHALGUNI(11, StringKey.NAKSHATRA_PURVA_PHALGUNI, Planet.VENUS, "Bhaga",
        ZodiacSign.LEO, ZodiacSign.LEO, ZodiacSign.LEO, ZodiacSign.LEO),
    UTTARA_PHALGUNI(12, StringKey.NAKSHATRA_UTTARA_PHALGUNI, Planet.SUN, "Aryaman",
        ZodiacSign.LEO, ZodiacSign.VIRGO, ZodiacSign.VIRGO, ZodiacSign.VIRGO),
    HASTA(13, StringKey.NAKSHATRA_HASTA, Planet.MOON, "Savitar",
        ZodiacSign.VIRGO, ZodiacSign.VIRGO, ZodiacSign.VIRGO, ZodiacSign.VIRGO),
    CHITRA(14, StringKey.NAKSHATRA_CHITRA, Planet.MARS, "Tvashtar",
        ZodiacSign.VIRGO, ZodiacSign.VIRGO, ZodiacSign.LIBRA, ZodiacSign.LIBRA),
    SWATI(15, StringKey.NAKSHATRA_SWATI, Planet.RAHU, "Vayu",
        ZodiacSign.LIBRA, ZodiacSign.LIBRA, ZodiacSign.LIBRA, ZodiacSign.LIBRA),
    VISHAKHA(16, StringKey.NAKSHATRA_VISHAKHA, Planet.JUPITER, "Indra-Agni",
        ZodiacSign.LIBRA, ZodiacSign.LIBRA, ZodiacSign.LIBRA, ZodiacSign.SCORPIO),
    ANURADHA(17, StringKey.NAKSHATRA_ANURADHA, Planet.SATURN, "Mitra",
        ZodiacSign.SCORPIO, ZodiacSign.SCORPIO, ZodiacSign.SCORPIO, ZodiacSign.SCORPIO),
    JYESHTHA(18, StringKey.NAKSHATRA_JYESHTHA, Planet.MERCURY, "Indra",
        ZodiacSign.SCORPIO, ZodiacSign.SCORPIO, ZodiacSign.SCORPIO, ZodiacSign.SCORPIO),
    MULA(19, StringKey.NAKSHATRA_MULA, Planet.KETU, "Nirriti",
        ZodiacSign.SAGITTARIUS, ZodiacSign.SAGITTARIUS, ZodiacSign.SAGITTARIUS, ZodiacSign.SAGITTARIUS),
    PURVA_ASHADHA(20, StringKey.NAKSHATRA_PURVA_ASHADHA, Planet.VENUS, "Apas",
        ZodiacSign.SAGITTARIUS, ZodiacSign.SAGITTARIUS, ZodiacSign.SAGITTARIUS, ZodiacSign.SAGITTARIUS),
    UTTARA_ASHADHA(21, StringKey.NAKSHATRA_UTTARA_ASHADHA, Planet.SUN, "Vishwadevas",
        ZodiacSign.SAGITTARIUS, ZodiacSign.CAPRICORN, ZodiacSign.CAPRICORN, ZodiacSign.CAPRICORN),
    SHRAVANA(22, StringKey.NAKSHATRA_SHRAVANA, Planet.MOON, "Vishnu",
        ZodiacSign.CAPRICORN, ZodiacSign.CAPRICORN, ZodiacSign.CAPRICORN, ZodiacSign.CAPRICORN),
    DHANISHTHA(23, StringKey.NAKSHATRA_DHANISHTHA, Planet.MARS, "Vasus",
        ZodiacSign.CAPRICORN, ZodiacSign.CAPRICORN, ZodiacSign.AQUARIUS, ZodiacSign.AQUARIUS),
    SHATABHISHA(24, StringKey.NAKSHATRA_SHATABHISHA, Planet.RAHU, "Varuna",
        ZodiacSign.AQUARIUS, ZodiacSign.AQUARIUS, ZodiacSign.AQUARIUS, ZodiacSign.AQUARIUS),
    PURVA_BHADRAPADA(25, StringKey.NAKSHATRA_PURVA_BHADRAPADA, Planet.JUPITER, "Aja Ekapada",
        ZodiacSign.AQUARIUS, ZodiacSign.AQUARIUS, ZodiacSign.AQUARIUS, ZodiacSign.PISCES),
    UTTARA_BHADRAPADA(26, StringKey.NAKSHATRA_UTTARA_BHADRAPADA, Planet.SATURN, "Ahir Budhnya",
        ZodiacSign.PISCES, ZodiacSign.PISCES, ZodiacSign.PISCES, ZodiacSign.PISCES),
    REVATI(27, StringKey.NAKSHATRA_REVATI, Planet.MERCURY, "Pushan",
        ZodiacSign.PISCES, ZodiacSign.PISCES, ZodiacSign.PISCES, ZodiacSign.PISCES);

    val startDegree: Double get() = (number - 1) * NAKSHATRA_SPAN
    val endDegree: Double get() = number * NAKSHATRA_SPAN

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
