package com.astro.storm.ephemeris.prediction

import com.astro.storm.core.model.Planet
import com.astro.storm.core.model.VedicChart
import com.astro.storm.core.model.ZodiacSign
import com.astro.storm.ephemeris.AshtakavargaCalculator
import com.astro.storm.ephemeris.DashaCalculator
import com.astro.storm.ephemeris.SwissEphemerisEngine
import com.astro.storm.ephemeris.remedy.RemedyPlanetaryAnalyzer
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import kotlin.math.roundToInt

/**
 * Triple-Pillar Predictive Engine
 *
 * Synthesis of Vimshottari Dasha, Gochara (transits), and Ashtakavarga bindu strength.
 * Produces a success-probability timeline with peaks when all three pillars align.
 */
object TriplePillarEngine {

    private val GOCHARA_FAVORABLE = mapOf(
        Planet.SUN to setOf(3, 6, 10, 11),
        Planet.MOON to setOf(1, 3, 6, 7, 10, 11),
        Planet.MARS to setOf(3, 6, 11),
        Planet.MERCURY to setOf(2, 4, 6, 8, 10, 11),
        Planet.JUPITER to setOf(2, 5, 7, 9, 11),
        Planet.VENUS to setOf(1, 2, 3, 4, 5, 8, 9, 11, 12),
        Planet.SATURN to setOf(3, 6, 11),
        Planet.RAHU to setOf(3, 6, 10, 11),
        Planet.KETU to setOf(3, 6, 10, 11)
    )

    private val GOCHARA_DIFFICULT = mapOf(
        Planet.SUN to setOf(4, 7, 8, 9, 12),
        Planet.MOON to setOf(4, 8, 9, 12),
        Planet.MARS to setOf(2, 4, 5, 7, 8, 9, 12),
        Planet.MERCURY to setOf(7, 9, 12),
        Planet.JUPITER to setOf(3, 12),
        Planet.VENUS to setOf(6, 7, 10),
        Planet.SATURN to setOf(4, 5, 7, 8, 9, 12),
        Planet.RAHU to setOf(4, 7, 8, 9, 12),
        Planet.KETU to setOf(4, 7, 8, 9, 12)
    )

    data class TriplePillarTimelinePoint(
        val dateTime: LocalDateTime,
        val mahaLord: Planet,
        val antarLord: Planet?,
        val transitSign: ZodiacSign,
        val houseFromMoon: Int,
        val ashtakavargaBav: Int,
        val ashtakavargaSav: Int,
        val dashaStrengthScore: Int,
        val gocharaScore: Double,
        val ashtakavargaScore: Double,
        val successProbability: Int,
        val peakConditionMet: Boolean,
        val notes: List<String>
    )

    data class TriplePillarPeak(
        val start: LocalDateTime,
        val end: LocalDateTime,
        val maxProbability: Int,
        val mahaLord: Planet,
        val antarLord: Planet?
    )

    data class TriplePillarSynthesisResult(
        val chart: VedicChart,
        val start: LocalDate,
        val end: LocalDate,
        val points: List<TriplePillarTimelinePoint>,
        val peaks: List<TriplePillarPeak>,
        val summary: String
    )

    fun generateSuccessTimeline(
        chart: VedicChart,
        ephemerisEngine: SwissEphemerisEngine,
        startDate: LocalDate,
        endDate: LocalDate,
        stepDays: Long = 1
    ): TriplePillarSynthesisResult {
        val dashaTimeline = DashaCalculator.calculateDashaTimeline(chart)
        val ashtakavarga = AshtakavargaCalculator.calculateAshtakavarga(chart)
        val moonSign = chart.planetPositions.firstOrNull { it.planet == Planet.MOON }?.sign ?: ZodiacSign.ARIES
        val ascendantSign = ZodiacSign.fromLongitude(chart.ascendant)

        val points = mutableListOf<TriplePillarTimelinePoint>()
        var current = startDate

        while (!current.isAfter(endDate)) {
            val dateTime = LocalDateTime.of(current, LocalTime.NOON)
            val maha = dashaTimeline.mahadashas.find { it.isActiveOn(dateTime) }
            val antar = maha?.getAntardashaOn(dateTime)

            if (maha != null) {
                val transitPos = ephemerisEngine.calculatePlanetPosition(
                    maha.planet,
                    dateTime,
                    chart.birthData.timezone,
                    chart.birthData.latitude,
                    chart.birthData.longitude
                )

                val houseFromMoon = houseDistance(moonSign, transitPos.sign)
                val gocharaScore = gocharaScore(maha.planet, houseFromMoon)
                val transitScore = ashtakavarga.getTransitScore(maha.planet, transitPos.sign)

                val dashaStrength = RemedyPlanetaryAnalyzer.analyzePlanet(
                    planet = maha.planet,
                    chart = chart,
                    ascendantSign = ascendantSign,
                    language = com.astro.storm.core.common.Language.ENGLISH
                )

                val antarStrength = antar?.let {
                    RemedyPlanetaryAnalyzer.analyzePlanet(
                        planet = it.planet,
                        chart = chart,
                        ascendantSign = ascendantSign,
                        language = com.astro.storm.core.common.Language.ENGLISH
                    )
                }

                val dashaStrengthScore = combineDashaStrength(dashaStrength.strengthScore, antarStrength?.strengthScore)
                val ashtakavargaScore = calculateAshtakavargaScore(transitScore.binduScore, transitScore.savScore)

                val isDashaFavorable = dashaStrength.isFunctionalBenefic || dashaStrength.isYogakaraka
                val isHighBindu = transitScore.binduScore >= 4 && transitScore.savScore >= 30
                val gocharaStrong = gocharaScore >= 0.7

                val base = (dashaStrengthScore / 100.0) * 0.45 + gocharaScore * 0.3 + ashtakavargaScore * 0.25
                val boosted = if (isDashaFavorable && isHighBindu && gocharaStrong) {
                    (base + 0.15).coerceIn(0.0, 1.0)
                } else base.coerceIn(0.0, 1.0)

                val notes = buildList {
                    add("Dasha: ${maha.planet.displayName}${antar?.let { "/${it.planet.displayName}" } ?: ""}")
                    add("Transit in ${transitPos.sign.displayName} (${houseFromMoon} from Moon)")
                    add("Ashtakavarga BAV ${transitScore.binduScore}, SAV ${transitScore.savScore}")
                    if (isHighBindu) add("High bindu zone")
                    if (isDashaFavorable) add("Favorable dasha lord")
                    if (gocharaStrong) add("Strong gochara")
                }

                points.add(
                    TriplePillarTimelinePoint(
                        dateTime = dateTime,
                        mahaLord = maha.planet,
                        antarLord = antar?.planet,
                        transitSign = transitPos.sign,
                        houseFromMoon = houseFromMoon,
                        ashtakavargaBav = transitScore.binduScore,
                        ashtakavargaSav = transitScore.savScore,
                        dashaStrengthScore = dashaStrengthScore,
                        gocharaScore = gocharaScore,
                        ashtakavargaScore = ashtakavargaScore,
                        successProbability = (boosted * 100).roundToInt(),
                        peakConditionMet = isDashaFavorable && isHighBindu && gocharaStrong,
                        notes = notes
                    )
                )
            }

            current = current.plusDays(stepDays)
        }

        val peaks = extractPeaks(points)
        val summary = buildSummary(points, peaks, moonSign)

        return TriplePillarSynthesisResult(
            chart = chart,
            start = startDate,
            end = endDate,
            points = points,
            peaks = peaks,
            summary = summary
        )
    }

    private fun houseDistance(from: ZodiacSign, to: ZodiacSign): Int {
        val diff = (to.number - from.number + 12) % 12
        return if (diff == 0) 1 else diff + 1
    }

    private fun gocharaScore(planet: Planet, houseFromMoon: Int): Double {
        val favorable = GOCHARA_FAVORABLE[planet] ?: emptySet()
        val difficult = GOCHARA_DIFFICULT[planet] ?: emptySet()
        return when {
            houseFromMoon in favorable -> 1.0
            houseFromMoon in difficult -> 0.25
            else -> 0.6
        }
    }

    private fun calculateAshtakavargaScore(bav: Int, sav: Int): Double {
        val bavScore = (bav / 8.0).coerceIn(0.0, 1.0)
        val savScore = (sav / 56.0).coerceIn(0.0, 1.0)
        return (bavScore * 0.55 + savScore * 0.45).coerceIn(0.0, 1.0)
    }

    private fun combineDashaStrength(mahaScore: Int, antarScore: Int?): Int {
        if (antarScore == null) return mahaScore
        return (mahaScore * 0.7 + antarScore * 0.3).roundToInt().coerceIn(0, 100)
    }

    private fun extractPeaks(points: List<TriplePillarTimelinePoint>): List<TriplePillarPeak> {
        val peaks = mutableListOf<TriplePillarPeak>()
        var currentPeak: TriplePillarPeak? = null
        for (point in points) {
            if (point.peakConditionMet) {
                if (currentPeak == null) {
                    currentPeak = TriplePillarPeak(
                        start = point.dateTime,
                        end = point.dateTime,
                        maxProbability = point.successProbability,
                        mahaLord = point.mahaLord,
                        antarLord = point.antarLord
                    )
                } else {
                    val maxProb = maxOf(currentPeak.maxProbability, point.successProbability)
                    currentPeak = currentPeak.copy(
                        end = point.dateTime,
                        maxProbability = maxProb
                    )
                }
            } else if (currentPeak != null) {
                peaks.add(currentPeak)
                currentPeak = null
            }
        }
        currentPeak?.let { peaks.add(it) }
        return peaks
    }

    private fun buildSummary(
        points: List<TriplePillarTimelinePoint>,
        peaks: List<TriplePillarPeak>,
        moonSign: ZodiacSign
    ): String {
        if (points.isEmpty()) return "No timeline points generated."
        val maxPoint = points.maxByOrNull { it.successProbability }
        val avgScore = points.map { it.successProbability }.average().roundToInt()
        val peakCount = peaks.size
        val maxDesc = maxPoint?.let { "Peak ${it.successProbability}% on ${it.dateTime.toLocalDate()}" } ?: ""
        return "Average success probability $avgScore% with $peakCount peak window(s). $maxDesc (Moon sign ${moonSign.displayName})."
    }
}
