package com.astro.storm.ephemeris.varga

import com.astro.storm.core.common.Language
import com.astro.storm.core.common.StringResources
import com.astro.storm.core.model.*
import com.astro.storm.ephemeris.DivisionalChartCalculator
import com.astro.storm.ephemeris.DivisionalChartType
import com.astro.storm.ephemeris.VedicAstrologyUtils

/**
 * GenericVargaAnalyzer - Provides a standardized analysis for any divisional chart
 * that doesn't have a specialized analyzer yet.
 */
object GenericVargaAnalyzer {

    fun analyzeVarga(
        chart: VedicChart,
        type: DivisionalChartType,
        language: Language
    ): GenericVargaAnalysis {
        val divisionalData = DivisionalChartCalculator.calculateDivisionalChart(chart, type)
        val planetPositions = divisionalData.planetPositions
        val ascendantSign = divisionalData.ascendantSign

        // 1. Identify Vargottama planets
        val vargottamas = planetPositions.filter { pos ->
            val natalPos = chart.planetPositions.find { it.planet == pos.planet }
            natalPos != null && natalPos.sign == pos.sign
        }.map { it.planet }

        // 2. House analysis
        val houseSummaries = (1..12).map { houseNum ->
            val planetsInHouse = planetPositions.filter { it.house == houseNum }.map { it.planet }
            val houseLord = VedicAstrologyUtils.getHouseLord(chart, houseNum) // Simplified, uses D1 lord logic for generic
            
            HouseAnalysisSummary(
                houseNumber = houseNum,
                planetsInHouse = planetsInHouse,
                status = if (planetsInHouse.isNotEmpty()) 
                    StringResources.get(com.astro.storm.core.common.StringKeyGeneralPart3.DIVISIONAL_HOUSE_OCCUPIED, language)
                else 
                    StringResources.get(com.astro.storm.core.common.StringKeyGeneralPart3.DIVISIONAL_HOUSE_EMPTY, language),
                houseLord = houseLord
            )
        }

        // 3. Find dominant planet (in Kendra/Trikona and strong)
        val dominantPlanet = planetPositions
            .filter { it.house in listOf(1, 4, 7, 10, 5, 9) }
            .maxByOrNull { pos -> 
                var score = if (VedicAstrologyUtils.isExalted(pos)) 50 else 0
                if (VedicAstrologyUtils.isInOwnSign(pos)) score += 30
                score
            }?.planet

        // 4. Calculate overall strength score (0-100)
        val totalPoints = planetPositions.sumOf { pos ->
            var points = 0
            if (VedicAstrologyUtils.isExalted(pos)) points += 10
            if (VedicAstrologyUtils.isInOwnSign(pos)) points += 7
            if (pos.house in listOf(1, 4, 7, 10, 5, 9)) points += 5
            points
        }
        val strengthScore = (totalPoints * 1.5).toInt().coerceIn(0, 100)

        // 5. Descriptions and recommendations
        val keySignifications = type.description.split(", ")
        val recommendations = mutableListOf<String>()
        if (strengthScore < 40) {
            recommendations.add(StringResources.get(com.astro.storm.core.common.StringKeyGeneralPart3.DIVISIONAL_REMEDIES_DESC, language, type.shortName))
        }
        if (vargottamas.isNotEmpty()) {
            recommendations.add(StringResources.get(com.astro.storm.core.common.StringKeyGeneralPart3.DIVISIONAL_STRENGTH_DESC, language, vargottamas.joinToString { it.getLocalizedName(language) }))
        }

        return GenericVargaAnalysis(
            vargaType = type,
            planetPositions = planetPositions,
            ascendantSign = ascendantSign,
            ascendantLongitude = divisionalData.ascendantLongitude,
            dominantPlanet = dominantPlanet,
            vargottamaPlanets = vargottamas,
            houseAnalysis = houseSummaries,
            keySignifications = keySignifications,
            overallStrengthScore = strengthScore,
            description = type.getLocalizedDescription(language),
            recommendations = recommendations
        )
    }
}
