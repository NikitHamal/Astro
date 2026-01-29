package com.astro.storm.ephemeris.prediction

import android.content.Context
import com.astro.storm.core.model.Planet
import com.astro.storm.core.model.VedicChart
import com.astro.storm.core.model.ZodiacSign
import com.astro.storm.ephemeris.AshtakavargaCalculator
import com.astro.storm.ephemeris.DashaCalculator
import com.astro.storm.ephemeris.ShadbalaAnalysis
import com.astro.storm.ephemeris.ShadbalaCalculator
import com.astro.storm.ephemeris.SwissEphemerisEngine
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import kotlin.math.roundToInt

/**
 * Triple-Pillar Predictive Engine
 *
 * Synthesizes Vimshottari Dasha, Gochara (transits from Moon),
 * and Ashtakavarga to produce a success-probability timeline.
 */
object TriplePillarPredictiveEngine {

    private val FAVORABLE_TRANSITS = mapOf(
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

    private val NEUTRAL_TRANSITS = mapOf(
        Planet.SUN to setOf(1, 2, 5),
        Planet.MOON to setOf(2, 5),
        Planet.MARS to setOf(1, 10),
        Planet.MERCURY to setOf(1, 3, 5),
        Planet.JUPITER to setOf(1, 4, 6, 8, 10),
        Planet.VENUS to setOf(6, 7, 10),
        Planet.SATURN to setOf(1, 2, 10),
        Planet.RAHU to setOf(1, 2, 5),
        Planet.KETU to setOf(1, 2, 5)
    )

    data class TriplePillarPoint(
        val dateTime: LocalDateTime,
        val mahadashaLord: Planet,
        val antardashaLord: Planet?,
        val dashaStrength: Int,
        val transitPlanet: Planet,
        val transitSign: ZodiacSign,
        val gocharaHouse: Int,
        val gocharaScore: Int,
        val ashtakavargaScore: AshtakavargaCalculator.TransitScore,
        val successProbability: Int,
        val isPeak: Boolean
    )

    data class TriplePillarPeakWindow(
        val start: LocalDateTime,
        val end: LocalDateTime,
        val dashaLord: Planet,
        val transitSign: ZodiacSign,
        val successProbability: Int
    )

    data class TriplePillarSynthesisResult(
        val chartId: Long,
        val timeline: List<TriplePillarPoint>,
        val peakWindows: List<TriplePillarPeakWindow>,
        val summary: String,
        val timestamp: Long = System.currentTimeMillis()
    )

    fun buildTimeline(
        chart: VedicChart,
        context: Context,
        startDate: LocalDate,
        endDate: LocalDate,
        stepDays: Long = 7
    ): TriplePillarSynthesisResult {
        val dashaTimeline = DashaCalculator.calculateDashaTimeline(chart)
        val shadbala = ShadbalaCalculator.calculateShadbala(context, chart)
        val ashtakavarga = AshtakavargaCalculator.calculateAshtakavarga(chart)
        val ephemeris = SwissEphemerisEngine.getInstance(context)
        val moonSign = chart.planetPositions.firstOrNull { it.planet == Planet.MOON }?.sign
            ?: ZodiacSign.fromLongitude(chart.ascendant)

        val points = mutableListOf<TriplePillarPoint>()
        var current = startDate

        while (!current.isAfter(endDate)) {
            val dateTime = LocalDateTime.of(current, LocalTime.NOON)
            val mahadasha = dashaTimeline.mahadashas.firstOrNull { it.isActiveOn(dateTime) }
            val antardasha = mahadasha?.getAntardashaOn(dateTime)
            val dashaLord = mahadasha?.planet ?: Planet.MOON
            val transitPlanet = antardasha?.planet ?: dashaLord

            val transitPosition = ephemeris.calculatePlanetPosition(
                transitPlanet,
                dateTime,
                chart.birthData.timezone,
                chart.birthData.latitude,
                chart.birthData.longitude
            )

            val gocharaHouse = calculateHouseFromSign(transitPosition.sign, moonSign)
            val gocharaScore = gocharaScore(transitPlanet, gocharaHouse)
            val transitScore = ashtakavarga.getTransitScore(transitPlanet, transitPosition.sign)

            val dashaStrength = calculateDashaStrength(shadbala, dashaLord, antardasha?.planet)
            val ashtakavargaStrength = ashtakavargaStrength(transitScore)

            val baseScore =
                (dashaStrength * 0.45 + gocharaScore * 0.25 + ashtakavargaStrength * 0.30)
            val isPeak = isPeakWindow(dashaStrength, gocharaScore, transitScore)
            val successProbability = (baseScore + if (isPeak) 12 else 0).roundToInt().coerceIn(0, 100)

            points.add(
                TriplePillarPoint(
                    dateTime = dateTime,
                    mahadashaLord = dashaLord,
                    antardashaLord = antardasha?.planet,
                    dashaStrength = dashaStrength,
                    transitPlanet = transitPlanet,
                    transitSign = transitPosition.sign,
                    gocharaHouse = gocharaHouse,
                    gocharaScore = gocharaScore,
                    ashtakavargaScore = transitScore,
                    successProbability = successProbability,
                    isPeak = isPeak
                )
            )

            current = current.plusDays(stepDays)
        }

        val peakWindows = collapsePeaks(points)
        val summary = buildSummary(points, peakWindows)

        return TriplePillarSynthesisResult(
            chartId = chart.id,
            timeline = points,
            peakWindows = peakWindows,
            summary = summary
        )
    }

    private fun calculateDashaStrength(
        shadbala: ShadbalaAnalysis,
        mahadashaLord: Planet,
        antardashaLord: Planet?
    ): Int {
        val mahaScore = planetStrengthScore(shadbala, mahadashaLord)
        val antarScore = antardashaLord?.let { planetStrengthScore(shadbala, it) }
        val weighted = if (antarScore != null) {
            (mahaScore * 0.6 + antarScore * 0.4)
        } else {
            mahaScore.toDouble()
        }
        return weighted.roundToInt().coerceIn(0, 100)
    }

    private fun planetStrengthScore(
        shadbala: ShadbalaAnalysis,
        planet: Planet
    ): Int {
        val data = shadbala.planetaryStrengths[planet]
        if (data == null || data.requiredRupas <= 0.0) return 50
        val ratio = (data.totalRupas / data.requiredRupas).coerceIn(0.4, 1.4)
        return (ratio * 70.0 + 30.0).roundToInt().coerceIn(0, 100)
    }

    private fun ashtakavargaStrength(score: AshtakavargaCalculator.TransitScore): Int {
        val binduComponent = (score.binduScore / 8.0) * 50.0
        val savComponent = (score.savScore / 56.0) * 50.0
        return (binduComponent + savComponent).roundToInt().coerceIn(0, 100)
    }

    private fun gocharaScore(planet: Planet, houseFromMoon: Int): Int {
        return when {
            houseFromMoon in (FAVORABLE_TRANSITS[planet] ?: emptySet()) -> 85
            houseFromMoon in (NEUTRAL_TRANSITS[planet] ?: emptySet()) -> 55
            else -> 25
        }
    }

    private fun isPeakWindow(
        dashaStrength: Int,
        gocharaScore: Int,
        ashtakavargaScore: AshtakavargaCalculator.TransitScore
    ): Boolean {
        val highBindu = ashtakavargaScore.binduScore >= 4 && ashtakavargaScore.savScore >= 28
        return dashaStrength >= 60 && gocharaScore >= 75 && highBindu
    }

    private fun collapsePeaks(points: List<TriplePillarPoint>): List<TriplePillarPeakWindow> {
        if (points.isEmpty()) return emptyList()
        val peaks = mutableListOf<TriplePillarPeakWindow>()
        var current: TriplePillarPeakWindow? = null

        points.forEach { point ->
            if (!point.isPeak) {
                current?.let { peaks.add(it) }
                current = null
                return@forEach
            }

            if (current == null) {
                current = TriplePillarPeakWindow(
                    start = point.dateTime,
                    end = point.dateTime,
                    dashaLord = point.mahadashaLord,
                    transitSign = point.transitSign,
                    successProbability = point.successProbability
                )
            } else {
                current = current?.copy(
                    end = point.dateTime,
                    successProbability = maxOf(current!!.successProbability, point.successProbability)
                )
            }
        }

        current?.let { peaks.add(it) }
        return peaks
    }

    private fun buildSummary(points: List<TriplePillarPoint>, peaks: List<TriplePillarPeakWindow>): String {
        if (points.isEmpty()) return "No synthesis data available."
        val average = points.map { it.successProbability }.average().roundToInt()
        val peakCount = peaks.size
        return "Average success probability: $average%. Peak windows identified: $peakCount."
    }

    private fun calculateHouseFromSign(targetSign: ZodiacSign, referenceSign: ZodiacSign): Int {
        val diff = (targetSign.number - referenceSign.number + 12) % 12
        return diff + 1
    }
}
