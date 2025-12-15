package com.astro.storm.data.model

import com.astro.storm.R
import com.astro.storm.data.localization.LocalizableString

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

    val deity: LocalizableString
        get() = LocalizableString.Resource(
            when (this) {
                ASHWINI -> R.string.deity_ashwini
                BHARANI -> R.string.deity_bharani
                KRITTIKA -> R.string.deity_krittika
                ROHINI -> R.string.deity_rohini
                MRIGASHIRA -> R.string.deity_mrigashira
                ARDRA -> R.string.deity_ardra
                PUNARVASU -> R.string.deity_punarvasu
                PUSHYA -> R.string.deity_pushya
                ASHLESHA -> R.string.deity_ashlesha
                MAGHA -> R.string.deity_magha
                PURVA_PHALGUNI -> R.string.deity_purva_phalguni
                UTTARA_PHALGUNI -> R.string.deity_uttara_phalguni
                HASTA -> R.string.deity_hasta
                CHITRA -> R.string.deity_chitra
                SWATI -> R.string.deity_swati
                VISHAKHA -> R.string.deity_vishakha
                ANURADHA -> R.string.deity_anuradha
                JYESHTHA -> R.string.deity_jyeshtha
                MULA -> R.string.deity_mula
                PURVA_ASHADHA -> R.string.deity_purva_ashadha
                UTTARA_ASHADHA -> R.string.deity_uttara_ashadha
                SHRAVANA -> R.string.deity_shravana
                DHANISHTHA -> R.string.deity_dhanishtha
                SHATABHISHA -> R.string.deity_shatabhisha
                PURVA_BHADRAPADA -> R.string.deity_purva_bhadrapada
                UTTARA_BHADRAPADA -> R.string.deity_uttara_bhadrapada
                REVATI -> R.string.deity_revati
            }
        )

    val displayName: LocalizableString
        get() = LocalizableString.Resource(
            when (this) {
                ASHWINI -> R.string.nakshatra_ashwini
                BHARANI -> R.string.nakshatra_bharani
                KRITTIKA -> R.string.nakshatra_krittika
                ROHINI -> R.string.nakshatra_rohini
                MRIGASHIRA -> R.string.nakshatra_mrigashira
                ARDRA -> R.string.nakshatra_ardra
                PUNARVASU -> R.string.nakshatra_punarvasu
                PUSHYA -> R.string.nakshatra_pushya
                ASHLESHA -> R.string.nakshatra_ashlesha
                MAGHA -> R.string.nakshatra_magha
                PURVA_PHALGUNI -> R.string.nakshatra_purva_phalguni
                UTTARA_PHALGUNI -> R.string.nakshatra_uttara_phalguni
                HASTA -> R.string.nakshatra_hasta
                CHITRA -> R.string.nakshatra_chitra
                SWATI -> R.string.nakshatra_swati
                VISHAKHA -> R.string.nakshatra_vishakha
                ANURADHA -> R.string.nakshatra_anuradha
                JYESHTHA -> R.string.nakshatra_jyeshtha
                MULA -> R.string.nakshatra_mula
                PURVA_ASHADHA -> R.string.nakshatra_purva_ashadha
                UTTARA_ASHADHA -> R.string.nakshatra_uttara_ashadha
                SHRAVANA -> R.string.nakshatra_shravana
                DHANISHTHA -> R.string.nakshatra_dhanishtha
                SHATABHISHA -> R.string.nakshatra_shatabhisha
                PURVA_BHADRAPADA -> R.string.nakshatra_purva_bhadrapada
                UTTARA_BHADRAPADA -> R.string.nakshatra_uttara_bhadrapada
                REVATI -> R.string.nakshatra_revati
            }
        )

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
