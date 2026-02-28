package com.astro.vajra.ephemeris.muhurta

import com.astro.vajra.core.model.Nakshatra
import com.astro.vajra.core.model.Planet
import com.astro.vajra.ephemeris.muhurta.MuhurtaConstants.BHADRA_TITHIS
import com.astro.vajra.ephemeris.muhurta.MuhurtaConstants.DEGREES_PER_KARANA
import com.astro.vajra.ephemeris.muhurta.MuhurtaConstants.DEGREES_PER_TITHI
import com.astro.vajra.ephemeris.muhurta.MuhurtaConstants.DEGREES_PER_YOGA
import com.astro.vajra.ephemeris.muhurta.MuhurtaConstants.INAUSPICIOUS_YOGAS
import com.astro.vajra.ephemeris.muhurta.MuhurtaConstants.JAYA_TITHIS
import com.astro.vajra.ephemeris.muhurta.MuhurtaConstants.MOVABLE_KARANAS
import com.astro.vajra.ephemeris.muhurta.MuhurtaConstants.NANDA_TITHIS
import com.astro.vajra.ephemeris.muhurta.MuhurtaConstants.PURNA_TITHIS
import com.astro.vajra.ephemeris.muhurta.MuhurtaConstants.RIKTA_TITHIS
import com.astro.vajra.ephemeris.muhurta.MuhurtaConstants.TITHI_NAMES
import com.astro.vajra.ephemeris.muhurta.MuhurtaConstants.TOTAL_KARANAS
import com.astro.vajra.ephemeris.muhurta.MuhurtaConstants.TOTAL_TITHIS
import com.astro.vajra.ephemeris.muhurta.MuhurtaConstants.TOTAL_YOGAS
import com.astro.vajra.ephemeris.muhurta.MuhurtaConstants.YOGA_NAMES
import com.astro.vajra.ephemeris.muhurta.MuhurtaAstronomicalCalculator.normalizeDegrees
import kotlin.math.floor

object MuhurtaPanchangaEvaluator {

    fun calculateTithi(sunLong: Double, moonLong: Double): TithiInfo {
        val diff = normalizeDegrees(moonLong - sunLong)
        val num = (floor(diff / DEGREES_PER_TITHI).toInt() % TOTAL_TITHIS) + 1
        val paksha = if (num <= 15) "Shukla" else "Krishna"
        val disp = if (num <= 15) num else num - 15
        val name = when { num == 15 -> "Purnima"; num == 30 -> "Amavasya"; else -> TITHI_NAMES.getOrElse(disp - 1) { "Unknown" } }
        val fullName = if (num == 15 || num == 30) name else "$paksha $name"
        val lords = listOf(Planet.SUN, Planet.MOON, Planet.MARS, Planet.MERCURY, Planet.JUPITER, Planet.VENUS, Planet.SATURN, Planet.RAHU, Planet.KETU)
        val nature = when { num in NANDA_TITHIS -> TithiNature.NANDA; num in BHADRA_TITHIS -> TithiNature.BHADRA; num in JAYA_TITHIS -> TithiNature.JAYA; num in RIKTA_TITHIS -> TithiNature.RIKTA; num in PURNA_TITHIS -> TithiNature.PURNA; else -> TithiNature.NANDA }
        return TithiInfo(num, disp, fullName, paksha, lords[(num - 1) % 9], nature, nature != TithiNature.RIKTA && num !in listOf(4, 9, 14, 19, 24, 29, 8, 23))
    }

    fun calculateNakshatra(moonLong: Double): NakshatraInfo {
        val (nak, pada) = Nakshatra.fromLongitude(moonLong)
        return NakshatraInfo(nak, pada, nak.ruler, getNakshatraNature(nak), getNakshatraGana(nak), getNakshatraElement(nak))
    }

    private fun getNakshatraNature(nak: Nakshatra): NakshatraNature = when (nak) {
        Nakshatra.UTTARA_PHALGUNI, Nakshatra.UTTARA_ASHADHA, Nakshatra.UTTARA_BHADRAPADA, Nakshatra.ROHINI -> NakshatraNature.DHRUVA
        Nakshatra.PUNARVASU, Nakshatra.SWATI, Nakshatra.SHRAVANA, Nakshatra.DHANISHTHA, Nakshatra.SHATABHISHA -> NakshatraNature.CHARA
        Nakshatra.MULA, Nakshatra.ARDRA, Nakshatra.JYESHTHA, Nakshatra.ASHLESHA -> NakshatraNature.TIKSHNA
        Nakshatra.PURVA_PHALGUNI, Nakshatra.PURVA_ASHADHA, Nakshatra.PURVA_BHADRAPADA, Nakshatra.BHARANI, Nakshatra.MAGHA -> NakshatraNature.UGRA
        Nakshatra.MRIGASHIRA, Nakshatra.CHITRA, Nakshatra.ANURADHA, Nakshatra.REVATI -> NakshatraNature.MRIDU
        Nakshatra.ASHWINI, Nakshatra.PUSHYA, Nakshatra.HASTA -> NakshatraNature.KSHIPRA
        Nakshatra.VISHAKHA, Nakshatra.KRITTIKA -> NakshatraNature.MISHRA
    }

    private fun getNakshatraGana(nak: Nakshatra): NakshatraGana = when (nak) {
        Nakshatra.ASHWINI, Nakshatra.MRIGASHIRA, Nakshatra.PUNARVASU, Nakshatra.PUSHYA, Nakshatra.HASTA, Nakshatra.SWATI, Nakshatra.ANURADHA, Nakshatra.SHRAVANA, Nakshatra.REVATI -> NakshatraGana.DEVA
        Nakshatra.BHARANI, Nakshatra.ROHINI, Nakshatra.ARDRA, Nakshatra.PURVA_PHALGUNI, Nakshatra.UTTARA_PHALGUNI, Nakshatra.PURVA_ASHADHA, Nakshatra.UTTARA_ASHADHA, Nakshatra.PURVA_BHADRAPADA, Nakshatra.UTTARA_BHADRAPADA -> NakshatraGana.MANUSHYA
        else -> NakshatraGana.RAKSHASA
    }

    private fun getNakshatraElement(nak: Nakshatra): NakshatraElement = when (nak) {
        Nakshatra.SWATI, Nakshatra.PUNARVASU, Nakshatra.HASTA, Nakshatra.ANURADHA, Nakshatra.SHRAVANA -> NakshatraElement.VAYU
        Nakshatra.KRITTIKA, Nakshatra.BHARANI, Nakshatra.PUSHYA, Nakshatra.PURVA_PHALGUNI, Nakshatra.VISHAKHA, Nakshatra.PURVA_ASHADHA -> NakshatraElement.AGNI
        Nakshatra.ASHWINI, Nakshatra.MRIGASHIRA, Nakshatra.UTTARA_PHALGUNI, Nakshatra.CHITRA, Nakshatra.UTTARA_ASHADHA, Nakshatra.UTTARA_BHADRAPADA -> NakshatraElement.PRITHVI
        Nakshatra.ROHINI, Nakshatra.ARDRA, Nakshatra.ASHLESHA, Nakshatra.MAGHA, Nakshatra.JYESHTHA, Nakshatra.MULA, Nakshatra.PURVA_BHADRAPADA, Nakshatra.REVATI -> NakshatraElement.JALA
        else -> NakshatraElement.AKASHA
    }

    fun calculateYoga(sunLong: Double, moonLong: Double): YogaInfo {
        val sum = normalizeDegrees(sunLong + moonLong)
        val idx = floor(sum / DEGREES_PER_YOGA).toInt()
        val num = (idx % TOTAL_YOGAS) + 1
        return YogaInfo(num, YOGA_NAMES.getOrElse(num - 1) { "Unknown" }, if (num !in INAUSPICIOUS_YOGAS) "Auspicious" else "Inauspicious", num !in INAUSPICIOUS_YOGAS)
    }

    fun calculateKarana(sunLong: Double, moonLong: Double): KaranaInfo {
        val diff = normalizeDegrees(moonLong - sunLong)
        val idx = floor(diff / DEGREES_PER_KARANA).toInt()
        val num = (idx % TOTAL_KARANAS) + 1
        val (name, type, auspicious) = when (num) {
            1 -> Triple("Kimstughna", KaranaType.STHIRA, true)
            58 -> Triple("Shakuni", KaranaType.STHIRA, false)
            59 -> Triple("Chatushpada", KaranaType.STHIRA, false)
            60 -> Triple("Nagava", KaranaType.STHIRA, false)
            else -> {
                val mName = MOVABLE_KARANAS[(num - 2) % 7]
                Triple(mName, KaranaType.CHARA, mName != "Vishti")
            }
        }
        return KaranaInfo(num, name, type, auspicious)
    }
}
