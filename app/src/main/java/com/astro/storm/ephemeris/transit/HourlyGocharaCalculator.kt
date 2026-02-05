package com.astro.storm.ephemeris.transit

import com.astro.storm.core.model.VedicChart
import com.astro.storm.ephemeris.AshtakavargaCalculator
import java.time.LocalDateTime
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class HourlyGocharaCalculator @Inject constructor() {

    data class HourlyGochara(
        val time: LocalDateTime,
        val quality: Double, // 0 to 100
        val intensity: String, // 'High', 'Medium', 'Low'
        val descriptionEn: String,
        val descriptionNe: String
    )

    fun calculateHourlyGochara(chart: VedicChart, time: LocalDateTime): HourlyGochara {
        // Ashtakavarga-based intensity
        val ashtakavarga = AshtakavargaCalculator.calculateAshtakavarga(chart)
        val moonHouse = chart.planetPositions.find { it.planet == com.astro.storm.core.model.Planet.MOON }?.house ?: 1
        val points = ashtakavarga.sarvashtakavarga[moonHouse - 1]

        val quality = (points.toDouble() / 56.0) * 100.0

        return HourlyGochara(
            time = time,
            quality = quality,
            intensity = if (quality > 60) "High" else if (quality > 40) "Medium" else "Low",
            descriptionEn = "Hourly transit intensity based on Ashtakavarga points ($points) at Moon's house.",
            descriptionNe = "चन्द्रमाको भावमा अष्टकवर्ग बिन्दु ($points) मा आधारित प्रति घण्टा गोचर तीव्रता।"
        )
    }
}
