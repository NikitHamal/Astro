package com.astro.vajra.ephemeris.transit

import com.astro.vajra.core.model.*
import com.astro.vajra.data.templates.TemplateSelector
import com.astro.vajra.ephemeris.SwissEphemerisEngine
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
        val transitChart = swissEphemerisEngine.calculateVedicChart(
            BirthData(
                name = "Transit",
                dateTime = date.atTime(12, 0),
                latitude = chart.birthData.latitude,
                longitude = chart.birthData.longitude,
            timezone = chart.birthData.timezone,
            location = chart.birthData.location,
            gender = chart.birthData.gender
            )
        )

        val transitPositions = transitChart.planetPositions
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
            overallQuality = 75.0,
            favorableAreas = listOf(LifeArea.GENERAL),
            challengingAreas = emptyList(),
            summaryEn = "Today's transits bring a focus on planetary energies in your life.",
            summaryNe = "आजको गोचरले तपाईंको जीवनमा ग्रहीय ऊर्जाहरूमा ध्यान केन्द्रित गर्दछ।"
        )
    }
}
