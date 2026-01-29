package com.astro.storm.ephemeris.kp

import com.astro.storm.core.model.Nakshatra
import com.astro.storm.core.model.Planet
import com.astro.storm.core.model.VedicChart
import com.astro.storm.core.model.ZodiacSign
import com.astro.storm.ephemeris.VedicAstrologyUtils
import java.math.BigDecimal
import java.math.MathContext
import java.math.RoundingMode

/**
 * Krishnamurti Paddhati (KP) system engine.
 *
 * Implements:
 * - Cusp sub-lord calculations using Vimshottari-based subdivision of nakshatras.
 * - Significators and KP 4-step event verification.
 */
object KpSystemEngine {

    private val MATH_CONTEXT = MathContext(16, RoundingMode.HALF_UP)
    private val NAKSHATRA_SPAN = BigDecimal("13.333333333333333")
    private val FULL_CYCLE = BigDecimal("120")

    private val DASHA_SEQUENCE = listOf(
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
        Planet.KETU to BigDecimal("7"),
        Planet.VENUS to BigDecimal("20"),
        Planet.SUN to BigDecimal("6"),
        Planet.MOON to BigDecimal("10"),
        Planet.MARS to BigDecimal("7"),
        Planet.RAHU to BigDecimal("18"),
        Planet.JUPITER to BigDecimal("16"),
        Planet.SATURN to BigDecimal("19"),
        Planet.MERCURY to BigDecimal("17")
    )

    data class KpCuspSubLord(
        val house: Int,
        val cuspLongitude: Double,
        val sign: ZodiacSign,
        val signLord: Planet,
        val nakshatra: Nakshatra,
        val starLord: Planet,
        val subLord: Planet
    )

    data class KpSignificator(
        val planet: Planet,
        val occupiedHouses: Set<Int>,
        val ownedHouses: Set<Int>,
        val starLord: Planet,
        val starLordOccupiedHouses: Set<Int>,
        val starLordOwnedHouses: Set<Int>
    ) {
        val significations: Set<Int> = (occupiedHouses + ownedHouses + starLordOccupiedHouses + starLordOwnedHouses)
            .sorted()
            .toSet()
    }

    enum class KpVerdict { YES, NO, MIXED }

    data class KpEventVerification(
        val house: Int,
        val subLord: Planet,
        val significations: Set<Int>,
        val favorableHouses: Set<Int>,
        val unfavorableHouses: Set<Int>,
        val verdict: KpVerdict,
        val reasoning: List<String>
    )

    fun calculateCuspSubLords(chart: VedicChart): List<KpCuspSubLord> {
        return chart.houseCusps.mapIndexed { index, cusp ->
            val house = index + 1
            val sign = ZodiacSign.fromLongitude(cusp)
            val signLord = sign.ruler
            val subLordResult = getSubLord(cusp)
            KpCuspSubLord(
                house = house,
                cuspLongitude = cusp,
                sign = sign,
                signLord = signLord,
                nakshatra = subLordResult.nakshatra,
                starLord = subLordResult.starLord,
                subLord = subLordResult.subLord
            )
        }
    }

    fun calculateSignificators(chart: VedicChart): Map<Planet, KpSignificator> {
        val occupiedHouses = chart.planetPositions.groupBy { it.planet }.mapValues { (_, positions) ->
            positions.map { it.house }.toSet()
        }
        val ownedHouses = Planet.MAIN_PLANETS.associateWith { planet ->
            (1..12).filter { house -> VedicAstrologyUtils.getHouseLord(chart, house) == planet }.toSet()
        }

        return Planet.MAIN_PLANETS.associateWith { planet ->
            val planetPosition = chart.planetPositions.firstOrNull { it.planet == planet }
            val (nakshatra, _) = Nakshatra.fromLongitude(planetPosition?.longitude ?: 0.0)
            val starLord = nakshatra.ruler
            val starLordOccupied = occupiedHouses[starLord].orEmpty()
            val starLordOwned = ownedHouses[starLord].orEmpty()
            KpSignificator(
                planet = planet,
                occupiedHouses = occupiedHouses[planet].orEmpty(),
                ownedHouses = ownedHouses[planet].orEmpty(),
                starLord = starLord,
                starLordOccupiedHouses = starLordOccupied,
                starLordOwnedHouses = starLordOwned
            )
        }
    }

    fun verifyEvent(
        cusp: KpCuspSubLord,
        significators: Map<Planet, KpSignificator>,
        favorableHouses: Set<Int>,
        unfavorableHouses: Set<Int>
    ): KpEventVerification {
        val subLordSig = significators[cusp.subLord]
        val sigHouses = subLordSig?.significations.orEmpty()
        val favorableHits = sigHouses.intersect(favorableHouses)
        val unfavorableHits = sigHouses.intersect(unfavorableHouses)

        val verdict = when {
            favorableHits.isNotEmpty() && unfavorableHits.isEmpty() -> KpVerdict.YES
            favorableHits.isEmpty() && unfavorableHits.isNotEmpty() -> KpVerdict.NO
            favorableHits.isNotEmpty() && unfavorableHits.isNotEmpty() -> KpVerdict.MIXED
            else -> KpVerdict.NO
        }

        val reasoning = buildList {
            add("Cusp ${cusp.house} sub-lord: ${cusp.subLord.name}")
            if (favorableHits.isNotEmpty()) {
                add("Favorable houses signified: ${favorableHits.sorted().joinToString()}")
            }
            if (unfavorableHits.isNotEmpty()) {
                add("Unfavorable houses signified: ${unfavorableHits.sorted().joinToString()}")
            }
            if (favorableHits.isEmpty() && unfavorableHits.isEmpty()) {
                add("No event-specific houses signified by sub-lord.")
            }
        }

        return KpEventVerification(
            house = cusp.house,
            subLord = cusp.subLord,
            significations = sigHouses,
            favorableHouses = favorableHouses,
            unfavorableHouses = unfavorableHouses,
            verdict = verdict,
            reasoning = reasoning
        )
    }

    data class KpSubLordResult(
        val nakshatra: Nakshatra,
        val starLord: Planet,
        val subLord: Planet
    )

    fun getSubLord(longitude: Double): KpSubLordResult {
        val (nakshatra, _) = Nakshatra.fromLongitude(longitude)
        val starLord = nakshatra.ruler
        val positionInNakshatra = normalizedNakshatraOffset(longitude, nakshatra)
        val subLord = findSubLord(starLord, positionInNakshatra)
        return KpSubLordResult(
            nakshatra = nakshatra,
            starLord = starLord,
            subLord = subLord
        )
    }

    private fun findSubLord(starLord: Planet, positionInNakshatra: BigDecimal): Planet {
        val sequence = rotateSequence(starLord)
        var accumulated = BigDecimal.ZERO

        for (planet in sequence) {
            val subSpan = NAKSHATRA_SPAN.multiply(DASHA_YEARS[planet] ?: BigDecimal.ZERO, MATH_CONTEXT)
                .divide(FULL_CYCLE, MATH_CONTEXT)
            val next = accumulated + subSpan
            if (positionInNakshatra < next) {
                return planet
            }
            accumulated = next
        }

        return starLord
    }

    private fun normalizedNakshatraOffset(longitude: Double, nakshatra: Nakshatra): BigDecimal {
        val normalized = normalizeLongitude(longitude)
        val start = BigDecimal(nakshatra.startDegree.toString())
        val offset = BigDecimal(normalized.toString()).subtract(start, MATH_CONTEXT)
        return if (offset < BigDecimal.ZERO) offset + NAKSHATRA_SPAN else offset
    }

    private fun rotateSequence(startingLord: Planet): List<Planet> {
        val startIndex = DASHA_SEQUENCE.indexOf(startingLord)
        if (startIndex == -1) return DASHA_SEQUENCE
        return DASHA_SEQUENCE.drop(startIndex) + DASHA_SEQUENCE.take(startIndex)
    }

    private fun normalizeLongitude(longitude: Double): Double {
        val mod = longitude % 360.0
        return if (mod < 0) mod + 360.0 else mod
    }
}
