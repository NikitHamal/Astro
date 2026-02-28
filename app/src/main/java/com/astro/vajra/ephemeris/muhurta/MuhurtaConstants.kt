package com.astro.vajra.ephemeris.muhurta

import com.astro.vajra.core.model.Planet
import swisseph.SweConst

object MuhurtaConstants {
    const val AYANAMSA_LAHIRI = SweConst.SE_SIDM_LAHIRI
    const val SEFLG_SIDEREAL = SweConst.SEFLG_SIDEREAL
    const val SEFLG_SPEED = SweConst.SEFLG_SPEED

    const val DEGREES_PER_TITHI = 12.0
    const val DEGREES_PER_NAKSHATRA = 360.0 / 27.0
    const val DEGREES_PER_YOGA = 360.0 / 27.0
    const val DEGREES_PER_KARANA = 6.0

    const val TOTAL_TITHIS = 30
    const val TOTAL_YOGAS = 27
    const val TOTAL_KARANAS = 60

    const val DAY_MUHURTAS = 15
    const val DAY_CHOGHADIYAS = 8
    const val NIGHT_CHOGHADIYAS = 8
    const val DAY_HORAS = 12
    const val NIGHT_HORAS = 12

    val CHALDEAN_ORDER = listOf(
        Planet.SATURN, Planet.JUPITER, Planet.MARS, Planet.SUN,
        Planet.VENUS, Planet.MERCURY, Planet.MOON
    )

    val MOVABLE_KARANAS = listOf(
        "Bava", "Balava", "Kaulava", "Taitila", "Garija", "Vanija", "Vishti"
    )

    val YOGA_NAMES = listOf(
        "Vishkumbha", "Priti", "Ayushman", "Saubhagya", "Shobhana",
        "Atiganda", "Sukarma", "Dhriti", "Shoola", "Ganda",
        "Vriddhi", "Dhruva", "Vyaghata", "Harshana", "Vajra",
        "Siddhi", "Vyatipata", "Variyan", "Parigha", "Shiva",
        "Siddha", "Sadhya", "Shubha", "Shukla", "Brahma",
        "Indra", "Vaidhriti"
    )

    val TITHI_NAMES = listOf(
        "Pratipada", "Dwitiya", "Tritiya", "Chaturthi", "Panchami",
        "Shashthi", "Saptami", "Ashtami", "Navami", "Dashami",
        "Ekadashi", "Dwadashi", "Trayodashi", "Chaturdashi", "Purnima"
    )

    val INAUSPICIOUS_YOGAS = setOf(1, 6, 9, 10, 13, 15, 17, 19, 27)

    val RIKTA_TITHIS = setOf(4, 9, 14, 19, 24, 29)
    val NANDA_TITHIS = setOf(1, 6, 11, 16, 21, 26)
    val BHADRA_TITHIS = setOf(2, 7, 12, 17, 22, 27)
    val JAYA_TITHIS = setOf(3, 8, 13, 18, 23, 28)
    val PURNA_TITHIS = setOf(5, 10, 15, 20, 25, 30)
}
