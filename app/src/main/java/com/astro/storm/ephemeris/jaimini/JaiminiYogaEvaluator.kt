package com.astro.storm.ephemeris.jaimini

import com.astro.storm.core.model.Planet
import com.astro.storm.core.model.VedicChart
import com.astro.storm.ephemeris.SwissEphemerisEngine
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class JaiminiYogaEvaluator @Inject constructor() {

    data class JaiminiYoga(
        val name: String,
        val resultEn: String,
        val resultNe: String,
        val strength: Double
    )

    fun evaluateYogas(chart: VedicChart): List<JaiminiYoga> {
        val analysis = JaiminiKarakaCalculator.calculateKarakas(chart)
        val yogas = mutableListOf<JaiminiYoga>()

        // Jaimini Rajju Yoga
        val planetsInKendra = chart.planetPositions.filter { it.house in listOf(1, 4, 7, 10) }
        if (planetsInKendra.size >= 3) {
            yogas.add(JaiminiYoga(
                name = "Jaimini Rajju Yoga",
                resultEn = "Promotes name, fame, and success in administrative roles.",
                resultNe = "प्रशासनिक भूमिकामा नाम, प्रसिद्धि र सफलता प्रवर्द्धन गर्दछ।",
                strength = 80.0
            ))
        }

        // Atmakaraka in beneficial navamsa
        val ak = analysis.karakas[JaiminiKarakaCalculator.KarakaType.ATMAKARAKA]
        if (ak?.isWellPlaced == true) {
            yogas.add(JaiminiYoga(
                name = "Subha Atmakaraka Yoga",
                resultEn = "Soul is well-aligned with destiny, leading to spiritual and material growth.",
                resultNe = "आत्मा भाग्यसँग राम्रोसँग मिलेको छ, जसले आध्यात्मिक र भौतिक वृद्धितर्फ लैजान्छ।",
                strength = 90.0
            ))
        }

        return yogas
    }
}
