package com.astro.vajra.ephemeris.kp

import com.astro.vajra.core.model.Nakshatra
import com.astro.vajra.core.model.Planet
import com.astro.vajra.core.model.VedicChart
import com.astro.vajra.core.model.ZodiacSign
import com.astro.vajra.ephemeris.AstrologicalConstants
import java.time.DayOfWeek
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter

/**
 * KP Ruling Planets Calculator
 *
 * Ruling planets at any given moment are crucial in KP system for:
 * 1. Confirming significators (common planets between significators and rulers strengthen prediction)
 * 2. Timing of events (ruling planets at the time of query/judgment)
 * 3. Horary astrology verification
 *
 * The 7 ruling planets at any moment are:
 * 1. Day Lord (lord of the weekday)
 * 2. Moon's Sign Lord (lord of the rashi Moon is transiting)
 * 3. Moon's Star Lord (lord of the nakshatra Moon is transiting)
 * 4. Moon's Sub Lord (sub-lord of Moon's position)
 * 5. Ascendant's Sign Lord (lord of the rising sign)
 * 6. Ascendant's Star Lord (lord of the nakshatra of ascendant)
 * 7. Ascendant's Sub Lord (sub-lord of the ascendant degree)
 *
 * Reference: K.S. Krishnamurti, KP Reader Vol. 2, "Ruling Planets" chapter
 */
object KPRulingPlanetsCalculator {

    /** Day lords for each day of the week */
    private val DAY_LORDS = mapOf(
        DayOfWeek.SUNDAY to Planet.SUN,
        DayOfWeek.MONDAY to Planet.MOON,
        DayOfWeek.TUESDAY to Planet.MARS,
        DayOfWeek.WEDNESDAY to Planet.MERCURY,
        DayOfWeek.THURSDAY to Planet.JUPITER,
        DayOfWeek.FRIDAY to Planet.VENUS,
        DayOfWeek.SATURDAY to Planet.SATURN
    )

    /**
     * Calculate ruling planets from a VedicChart (at the chart's birth time).
     *
     * @param chart The Vedic chart
     * @param cusps The Placidus cusp values (if different from chart cusps)
     * @return Ruling planets analysis
     */
    fun calculateFromChart(chart: VedicChart, cusps: List<Double>): KPRulingPlanets {
        val birthDateTime = chart.birthData.dateTime
        val dayOfWeek = birthDateTime.dayOfWeek
        val dayLord = DAY_LORDS[dayOfWeek] ?: Planet.SUN

        // Moon position from chart
        val moonPosition = chart.planetPositions.firstOrNull { it.planet == Planet.MOON }
        val moonLongitude = moonPosition?.longitude ?: 0.0

        // Moon's sign lord
        val moonSign = ZodiacSign.fromLongitude(moonLongitude)
        val moonSignLord = moonSign.ruler

        // Moon's star lord
        val moonNakshatra = Nakshatra.fromLongitude(moonLongitude).first
        val moonStarLord = moonNakshatra.ruler

        // Moon's sub lord
        val moonSubLord = KPSubLordTable.findSubLord(moonLongitude)

        // Ascendant (Lagna) - use cusp 1
        val ascDegree = cusps[0]
        val ascSign = ZodiacSign.fromLongitude(ascDegree)
        val ascSignLord = ascSign.ruler

        val ascNakshatra = Nakshatra.fromLongitude(ascDegree).first
        val ascStarLord = ascNakshatra.ruler

        val ascSubLord = KPSubLordTable.findSubLord(ascDegree)

        val timeFormatter = DateTimeFormatter.ofPattern("dd MMM yyyy HH:mm:ss")
        val formattedTime = birthDateTime.format(timeFormatter)

        return KPRulingPlanets(
            dayLord = dayLord,
            moonSignLord = moonSignLord,
            moonStarLord = moonStarLord,
            moonSubLord = moonSubLord,
            ascSignLord = ascSignLord,
            ascStarLord = ascStarLord,
            ascSubLord = ascSubLord,
            calculationTime = formattedTime
        )
    }

    /**
     * Calculate ruling planets for the current moment.
     *
     * @param currentMoonLongitude Current Moon's sidereal longitude
     * @param currentAscendant Current ascendant degree (sidereal)
     * @param timezone Timezone for day lord calculation
     * @return Ruling planets at current moment
     */
    fun calculateForCurrentMoment(
        currentMoonLongitude: Double,
        currentAscendant: Double,
        timezone: String
    ): KPRulingPlanets {
        val now = try {
            LocalDateTime.now(ZoneId.of(timezone))
        } catch (e: Exception) {
            LocalDateTime.now()
        }

        val dayOfWeek = now.dayOfWeek
        val dayLord = DAY_LORDS[dayOfWeek] ?: Planet.SUN

        // Moon analysis
        val moonSign = ZodiacSign.fromLongitude(currentMoonLongitude)
        val moonSignLord = moonSign.ruler
        val moonNakshatra = Nakshatra.fromLongitude(currentMoonLongitude).first
        val moonStarLord = moonNakshatra.ruler
        val moonSubLord = KPSubLordTable.findSubLord(currentMoonLongitude)

        // Ascendant analysis
        val ascSign = ZodiacSign.fromLongitude(currentAscendant)
        val ascSignLord = ascSign.ruler
        val ascNakshatra = Nakshatra.fromLongitude(currentAscendant).first
        val ascStarLord = ascNakshatra.ruler
        val ascSubLord = KPSubLordTable.findSubLord(currentAscendant)

        val timeFormatter = DateTimeFormatter.ofPattern("dd MMM yyyy HH:mm:ss")
        val formattedTime = now.format(timeFormatter)

        return KPRulingPlanets(
            dayLord = dayLord,
            moonSignLord = moonSignLord,
            moonStarLord = moonStarLord,
            moonSubLord = moonSubLord,
            ascSignLord = ascSignLord,
            ascStarLord = ascStarLord,
            ascSubLord = ascSubLord,
            calculationTime = formattedTime
        )
    }

    /**
     * Find common planets between significators and ruling planets.
     * These are the strongest event indicators in KP.
     *
     * @param significators Set of significator planets for the queried houses
     * @param rulingPlanets Current ruling planets
     * @return List of planets that appear in both sets
     */
    fun findCommonSignificatorsAndRulers(
        significators: Set<Planet>,
        rulingPlanets: KPRulingPlanets
    ): List<Planet> {
        val rulers = rulingPlanets.allRulingPlanets.toSet()
        return significators.intersect(rulers).toList()
    }
}
