package com.astro.storm.ephemeris.transit

import com.astro.storm.core.model.*
import com.astro.storm.data.templates.TemplateSelector
import com.astro.storm.ephemeris.SwissEphemerisEngine
import java.time.LocalDate
import java.time.LocalDateTime
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DailyTransitCalculator @Inject constructor(
    private val swissEphemerisEngine: SwissEphemerisEngine,
    private val templateSelector: TemplateSelector
) {
    data class DailyTransitAnalysis(
        val date: LocalDate,
        val planetaryTransits: List<PlanetTransit>,
        val overallQuality: Double,
        val favorableAreas: List<LifeArea>,
        val challengingAreas: List<LifeArea>,
        val summaryEn: String,
        val summaryNe: String
    )

    data class PlanetTransit(
        val planet: Planet,
        val sign: ZodiacSign,
        val house: Int,
        val predictionEn: String,
        val predictionNe: String
    )

    fun calculateDailyTransit(chart: VedicChart, date: LocalDate): DailyTransitAnalysis {
        val transitPositions = swissEphemerisEngine.calculatePlanetaryPositions(
            date.atTime(12, 0),
            chart.birthData.latitude.toDouble(),
            chart.birthData.longitude.toDouble()
        )

        val ascSign = ZodiacSign.fromLongitude(chart.ascendant)

        val planetTransits = transitPositions.map { pos ->
            val transitHouse = ((pos.sign.ordinal - ascSign.ordinal + 12) % 12) + 1
            val template = templateSelector.findBestTemplate(
                category = "transit",
                transitPlanet = pos.planet,
                sign = pos.sign,
                house = transitHouse
            )

            PlanetTransit(
                planet = pos.planet,
                sign = pos.sign,
                house = transitHouse,
                predictionEn = template?.en ?: "Transit results for ${pos.planet} in house $transitHouse",
                predictionNe = template?.ne ?: "गोचर परिणाम: ${pos.planet} $transitHouse भावमा"
            )
        }

        return DailyTransitAnalysis(
            date = date,
            planetaryTransits = planetTransits,
            overallQuality = 75.0, // Simplified score
            favorableAreas = listOf(LifeArea.GENERAL),
            challengingAreas = emptyList(),
            summaryEn = "Today's transits bring a focus on ${planetTransits.first().planet}'s energy in your life.",
            summaryNe = "आजको गोचरले तपाईंको जीवनमा ${planetTransits.first().planet} को ऊर्जामा ध्यान केन्द्रित गर्दछ।"
        )
    }
}
