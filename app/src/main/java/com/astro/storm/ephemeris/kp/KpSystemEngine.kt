package com.astro.storm.ephemeris.kp

import com.astro.storm.core.model.Nakshatra
import com.astro.storm.core.model.Planet
import com.astro.storm.core.model.VedicChart
import com.astro.storm.core.model.ZodiacSign
import kotlin.math.abs

/**
 * KP (Krishnamurti Paddhati) Sub-Lord and 4-Step Theory Engine
 *
 * Calculates cusp sub-lords using Vimshottari proportions and verifies events
 * using a 4-step significator framework.
 */
object KpSystemEngine {

    private const val TOTAL_CYCLE_YEARS = 120.0

    private val VIMSHOTTARI_SEQUENCE = listOf(
        Planet.KETU,
        Planet.VENUS,
        Planet.SUN,
        Planet.MOON,
        Planet.MARS,
        Planet.RAHU,
        Planet.JUPITER,
        Planet.SATURN,
        Planet.MERCURY
    )

    private val DASHA_YEARS = mapOf(
        Planet.KETU to 7.0,
        Planet.VENUS to 20.0,
        Planet.SUN to 6.0,
        Planet.MOON to 10.0,
        Planet.MARS to 7.0,
        Planet.RAHU to 18.0,
        Planet.JUPITER to 16.0,
        Planet.SATURN to 19.0,
        Planet.MERCURY to 17.0
    )

    data class KpCuspInfo(
        val house: Int,
        val longitude: Double,
        val sign: ZodiacSign,
        val signLord: Planet,
        val nakshatra: Nakshatra,
        val starLord: Planet,
        val subLord: Planet,
        val subSubLord: Planet,
        val subSegmentStart: Double,
        val subSegmentEnd: Double
    )

    data class KpSignificators(
        val planet: Planet,
        val houses: Set<Int>
    )

    data class KpStepEvaluation(
        val step: String,
        val planet: Planet,
        val favorableHouses: Set<Int>,
        val unfavorableHouses: Set<Int>,
        val planetHouses: Set<Int>,
        val isPass: Boolean
    )

    data class KpEventEvaluation(
        val cuspInfo: KpCuspInfo,
        val eventType: KpEventType,
        val steps: List<KpStepEvaluation>,
        val isFavorable: Boolean
    )

    enum class KpEventType(
        val displayName: String,
        val favorable: Set<Int>,
        val unfavorable: Set<Int>
    ) {
        GENERAL("General Matters", setOf(1, 2, 5, 9, 10, 11), setOf(6, 8, 12)),
        CAREER("Career", setOf(2, 6, 10, 11), setOf(5, 8, 12)),
        MARRIAGE("Marriage", setOf(2, 7, 11), setOf(1, 6, 8, 12)),
        EDUCATION("Education", setOf(2, 4, 5, 9, 11), setOf(6, 8, 12)),
        PROPERTY("Property", setOf(2, 4, 11), setOf(6, 8, 12)),
        CHILD("Children", setOf(2, 5, 11), setOf(1, 6, 8, 12)),
        HEALTH("Health", setOf(1, 6, 10), setOf(8, 12)),
        FOREIGN("Foreign Travel", setOf(3, 9, 12), setOf(4, 8))
    }

    fun calculateCusps(chart: VedicChart): List<KpCuspInfo> {
        return chart.houseCusps.mapIndexed { index, cusp ->
            val house = index + 1
            val normalized = normalizeLongitude(cusp)
            val sign = ZodiacSign.fromLongitude(normalized)
            val (nakshatra, _) = Nakshatra.fromLongitude(normalized)
            val starLord = nakshatra.ruler
            val subInfo = findSubLord(normalized, nakshatra)
            val subSubInfo = findSubSubLord(normalized, nakshatra, subInfo)

            KpCuspInfo(
                house = house,
                longitude = normalized,
                sign = sign,
                signLord = sign.ruler,
                nakshatra = nakshatra,
                starLord = starLord,
                subLord = subInfo.lord,
                subSubLord = subSubInfo.lord,
                subSegmentStart = subInfo.segmentStart,
                subSegmentEnd = subInfo.segmentEnd
            )
        }
    }

    fun evaluateEvent(
        chart: VedicChart,
        cuspInfo: KpCuspInfo,
        eventType: KpEventType
    ): KpEventEvaluation {
        val significators = buildSignificators(chart)

        val subLord = cuspInfo.subLord
        val subLordStar = getStarLord(chart, subLord)
        val cuspStar = cuspInfo.starLord
        val signLord = cuspInfo.signLord

        val steps = listOf(
            evaluateStep("Cusp Sub-Lord", subLord, eventType, significators),
            evaluateStep("Sub-Lord Star", subLordStar, eventType, significators),
            evaluateStep("Cusp Star-Lord", cuspStar, eventType, significators),
            evaluateStep("Cusp Sign-Lord", signLord, eventType, significators)
        )

        val isFavorable = steps.all { it.isPass }

        return KpEventEvaluation(
            cuspInfo = cuspInfo,
            eventType = eventType,
            steps = steps,
            isFavorable = isFavorable
        )
    }

    private fun evaluateStep(
        label: String,
        planet: Planet,
        eventType: KpEventType,
        significators: Map<Planet, KpSignificators>
    ): KpStepEvaluation {
        val houses = significators[planet]?.houses ?: emptySet()
        val favorableHit = houses.intersect(eventType.favorable).isNotEmpty()
        val unfavorableHit = houses.intersect(eventType.unfavorable).isNotEmpty()
        val isPass = favorableHit && !unfavorableHit

        return KpStepEvaluation(
            step = label,
            planet = planet,
            favorableHouses = eventType.favorable,
            unfavorableHouses = eventType.unfavorable,
            planetHouses = houses,
            isPass = isPass
        )
    }

    private fun buildSignificators(chart: VedicChart): Map<Planet, KpSignificators> {
        val houseLords = buildHouseLords(chart)
        val positions = chart.planetPositions.filter { it.planet in Planet.MAIN_PLANETS }
        val byPlanet = mutableMapOf<Planet, MutableSet<Int>>()

        positions.forEach { position ->
            val planet = position.planet
            val houses = byPlanet.getOrPut(planet) { mutableSetOf() }
            houses.add(position.house)
            houses.addAll(houseLords.filter { it.value == planet }.keys)

            val starLord = getStarLord(position.longitude)
            val starLordPosition = positions.firstOrNull { it.planet == starLord }
            if (starLordPosition != null) {
                houses.add(starLordPosition.house)
                houses.addAll(houseLords.filter { it.value == starLord }.keys)
            }
        }

        return byPlanet.mapValues { KpSignificators(it.key, it.value.toSet()) }
    }

    private fun buildHouseLords(chart: VedicChart): Map<Int, Planet> {
        val houseLords = mutableMapOf<Int, Planet>()
        chart.houseCusps.forEachIndexed { index, cusp ->
            val house = index + 1
            val sign = ZodiacSign.fromLongitude(cusp)
            houseLords[house] = sign.ruler
        }
        return houseLords
    }

    private fun getStarLord(chart: VedicChart, planet: Planet): Planet {
        val position = chart.planetPositions.firstOrNull { it.planet == planet }
        return position?.let { getStarLord(it.longitude) } ?: planet
    }

    private fun getStarLord(longitude: Double): Planet {
        val (nakshatra, _) = Nakshatra.fromLongitude(longitude)
        return nakshatra.ruler
    }

    private data class SubLordInfo(
        val lord: Planet,
        val segmentStart: Double,
        val segmentEnd: Double
    )

    private fun findSubLord(longitude: Double, nakshatra: Nakshatra): SubLordInfo {
        val nakStart = nakshatra.startDegree
        val nakSpan = 360.0 / 27.0
        val offset = (normalizeLongitude(longitude - nakStart)) % nakSpan
        val sequence = sequenceStartingAt(nakshatra.ruler)
        var currentStart = nakStart
        var remaining = offset

        for (planet in sequence) {
            val share = (DASHA_YEARS[planet] ?: 0.0) / TOTAL_CYCLE_YEARS
            val segSize = nakSpan * share
            if (remaining <= segSize || abs(remaining - segSize) < 1e-6) {
                return SubLordInfo(planet, normalizeLongitude(currentStart), normalizeLongitude(currentStart + segSize))
            }
            remaining -= segSize
            currentStart += segSize
        }

        val fallback = sequence.last()
        return SubLordInfo(fallback, normalizeLongitude(currentStart - nakSpan), normalizeLongitude(currentStart))
    }

    private fun findSubSubLord(longitude: Double, nakshatra: Nakshatra, subInfo: SubLordInfo): SubLordInfo {
        val nakStart = nakshatra.startDegree
        val nakSpan = 360.0 / 27.0
        val offset = (normalizeLongitude(longitude - nakStart)) % nakSpan
        val subStart = normalizeLongitude(subInfo.segmentStart - nakStart)
        val rawSpan = subInfo.segmentEnd - subInfo.segmentStart
        val subSpan = if (rawSpan < 0) rawSpan + 360.0 else rawSpan
        val relOffset = (normalizeLongitude(offset - subStart)) % subSpan
        val sequence = sequenceStartingAt(subInfo.lord)
        var currentStart = subInfo.segmentStart
        var remaining = relOffset

        for (planet in sequence) {
            val share = (DASHA_YEARS[planet] ?: 0.0) / TOTAL_CYCLE_YEARS
            val segSize = subSpan * share
            if (remaining <= segSize || abs(remaining - segSize) < 1e-6) {
                return SubLordInfo(planet, normalizeLongitude(currentStart), normalizeLongitude(currentStart + segSize))
            }
            remaining -= segSize
            currentStart += segSize
        }

        val fallback = sequence.last()
        return SubLordInfo(fallback, normalizeLongitude(currentStart - subSpan), normalizeLongitude(currentStart))
    }

    private fun sequenceStartingAt(planet: Planet): List<Planet> {
        val index = VIMSHOTTARI_SEQUENCE.indexOf(planet)
        if (index == -1) return VIMSHOTTARI_SEQUENCE
        return VIMSHOTTARI_SEQUENCE.drop(index) + VIMSHOTTARI_SEQUENCE.take(index)
    }

    private fun normalizeLongitude(longitude: Double): Double {
        var value = longitude % 360.0
        if (value < 0) value += 360.0
        return value
    }
}
