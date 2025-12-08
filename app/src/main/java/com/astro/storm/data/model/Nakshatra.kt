package com.astro.storm.data.model

import androidx.annotation.StringRes
import com.astro.storm.R

/**
 * 27 Nakshatras in Vedic astrology
 */
enum class Nakshatra(
    val number: Int,
    @StringRes val stringRes: Int,
    val ruler: Planet,
    @StringRes val deity: Int,
    val pada1Sign: ZodiacSign,
    val pada2Sign: ZodiacSign,
    val pada3Sign: ZodiacSign,
    val pada4Sign: ZodiacSign
) {
    ASHWINI(1, R.string.nakshatra_ashwini, Planet.KETU, R.string.deity_ashwini_kumaras,
        ZodiacSign.ARIES, ZodiacSign.ARIES, ZodiacSign.ARIES, ZodiacSign.ARIES),
    BHARANI(2, R.string.nakshatra_bharani, Planet.VENUS, R.string.deity_yama,
        ZodiacSign.ARIES, ZodiacSign.ARIES, ZodiacSign.ARIES, ZodiacSign.ARIES),
    KRITTIKA(3, R.string.nakshatra_krittika, Planet.SUN, R.string.deity_agni,
        ZodiacSign.ARIES, ZodiacSign.TAURUS, ZodiacSign.TAURUS, ZodiacSign.TAURUS),
    ROHINI(4, R.string.nakshatra_rohini, Planet.MOON, R.string.deity_brahma,
        ZodiacSign.TAURUS, ZodiacSign.TAURUS, ZodiacSign.TAURUS, ZodiacSign.TAURUS),
    MRIGASHIRA(5, R.string.nakshatra_mrigashira, Planet.MARS, R.string.deity_soma,
        ZodiacSign.TAURUS, ZodiacSign.TAURUS, ZodiacSign.GEMINI, ZodiacSign.GEMINI),
    ARDRA(6, R.string.nakshatra_ardra, Planet.RAHU, R.string.deity_rudra,
        ZodiacSign.GEMINI, ZodiacSign.GEMINI, ZodiacSign.GEMINI, ZodiacSign.GEMINI),
    PUNARVASU(7, R.string.nakshatra_punarvasu, Planet.JUPITER, R.string.deity_aditi,
        ZodiacSign.GEMINI, ZodiacSign.GEMINI, ZodiacSign.GEMINI, ZodiacSign.CANCER),
    PUSHYA(8, R.string.nakshatra_pushya, Planet.SATURN, R.string.deity_brihaspati,
        ZodiacSign.CANCER, ZodiacSign.CANCER, ZodiacSign.CANCER, ZodiacSign.CANCER),
    ASHLESHA(9, R.string.nakshatra_ashlesha, Planet.MERCURY, R.string.deity_sarpa,
        ZodiacSign.CANCER, ZodiacSign.CANCER, ZodiacSign.CANCER, ZodiacSign.CANCER),
    MAGHA(10, R.string.nakshatra_magha, Planet.KETU, R.string.deity_pitris,
        ZodiacSign.LEO, ZodiacSign.LEO, ZodiacSign.LEO, ZodiacSign.LEO),
    PURVA_PHALGUNI(11, R.string.nakshatra_purva_phalguni, Planet.VENUS, R.string.deity_bhaga,
        ZodiacSign.LEO, ZodiacSign.LEO, ZodiacSign.LEO, ZodiacSign.LEO),
    UTTARA_PHALGUNI(12, R.string.nakshatra_uttara_phalguni, Planet.SUN, R.string.deity_aryaman,
        ZodiacSign.LEO, ZodiacSign.VIRGO, ZodiacSign.VIRGO, ZodiacSign.VIRGO),
    HASTA(13, R.string.nakshatra_hasta, Planet.MOON, R.string.deity_savitar,
        ZodiacSign.VIRGO, ZodiacSign.VIRGO, ZodiacSign.VIRGO, ZodiacSign.VIRGO),
    CHITRA(14, R.string.nakshatra_chitra, Planet.MARS, R.string.deity_tvashtar,
        ZodiacSign.VIRGO, ZodiacSign.VIRGO, ZodiacSign.LIBRA, ZodiacSign.LIBRA),
    SWATI(15, R.string.nakshatra_swati, Planet.RAHU, R.string.deity_vayu,
        ZodiacSign.LIBRA, ZodiacSign.LIBRA, ZodiacSign.LIBRA, ZodiacSign.LIBRA),
    VISHAKHA(16, R.string.nakshatra_vishakha, Planet.JUPITER, R.string.deity_indra_agni,
        ZodiacSign.LIBRA, ZodiacSign.LIBRA, ZodiacSign.LIBRA, ZodiacSign.SCORPIO),
    ANURADHA(17, R.string.nakshatra_anuradha, Planet.SATURN, R.string.deity_mitra,
        ZodiacSign.SCORPIO, ZodiacSign.SCORPIO, ZodiacSign.SCORPIO, ZodiacSign.SCORPIO),
    JYESHTHA(18, R.string.nakshatra_jyeshtha, Planet.MERCURY, R.string.deity_indra,
        ZodiacSign.SCORPIO, ZodiacSign.SCORPIO, ZodiacSign.SCORPIO, ZodiacSign.SCORPIO),
    MULA(19, R.string.nakshatra_mula, Planet.KETU, R.string.deity_nirriti,
        ZodiacSign.SAGITTARIUS, ZodiacSign.SAGITTARIUS, ZodiacSign.SAGITTARIUS, ZodiacSign.SAGITTARIUS),
    PURVA_ASHADHA(20, R.string.nakshatra_purva_ashadha, Planet.VENUS, R.string.deity_apas,
        ZodiacSign.SAGITTARIUS, ZodiacSign.SAGITTARIUS, ZodiacSign.SAGITTARIUS, ZodiacSign.SAGITTARIUS),
    UTTARA_ASHADHA(21, R.string.nakshatra_uttara_ashadha, Planet.SUN, R.string.deity_vishwadevas,
        ZodiacSign.SAGITTARIUS, ZodiacSign.CAPRICORN, ZodiacSign.CAPRICORN, ZodiacSign.CAPRICORN),
    SHRAVANA(22, R.string.nakshatra_shravana, Planet.MOON, R.string.deity_vishnu,
        ZodiacSign.CAPRICORN, ZodiacSign.CAPRICORN, ZodiacSign.CAPRICORN, ZodiacSign.CAPRICORN),
    DHANISHTHA(23, R.string.nakshatra_dhanishtha, Planet.MARS, R.string.deity_vasus,
        ZodiacSign.CAPRICORN, ZodiacSign.CAPRICORN, ZodiacSign.AQUARIUS, ZodiacSign.AQUARIUS),
    SHATABHISHA(24, R.string.nakshatra_shatabhisha, Planet.RAHU, R.string.deity_varuna,
        ZodiacSign.AQUARIUS, ZodiacSign.AQUARIUS, ZodiacSign.AQUARIUS, ZodiacSign.AQUARIUS),
    PURVA_BHADRAPADA(25, R.string.nakshatra_purva_bhadrapada, Planet.JUPITER, R.string.deity_aja_ekapada,
        ZodiacSign.AQUARIUS, ZodiacSign.AQUARIUS, ZodiacSign.AQUARIUS, ZodiacSign.PISCES),
    UTTARA_BHADRAPADA(26, R.string.nakshatra_uttara_bhadrapada, Planet.SATURN, R.string.deity_ahir_budhnya,
        ZodiacSign.PISCES, ZodiacSign.PISCES, ZodiacSign.PISCES, ZodiacSign.PISCES),
    REVATI(27, R.string.nakshatra_revati, Planet.MERCURY, R.string.deity_pushan,
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
