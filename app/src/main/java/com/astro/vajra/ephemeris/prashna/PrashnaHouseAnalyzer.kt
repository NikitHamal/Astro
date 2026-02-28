package com.astro.vajra.ephemeris.prashna

import com.astro.vajra.core.common.Language
import com.astro.vajra.core.common.StringKeyAnalysis
import com.astro.vajra.core.common.StringResources
import com.astro.vajra.core.model.PlanetPosition
import com.astro.vajra.core.model.VedicChart
import com.astro.vajra.core.model.ZodiacSign
import com.astro.vajra.ephemeris.prashna.PrashnaConstants.NATURAL_BENEFICS
import com.astro.vajra.ephemeris.prashna.PrashnaConstants.QUESTION_CATEGORIES
import com.astro.vajra.ephemeris.prashna.PrashnaHelpers.isAspectingHouse
import com.astro.vajra.ephemeris.prashna.PrashnaHelpers.localized
import com.astro.vajra.ephemeris.prashna.PrashnaLagnaAnalyzer.calculatePlanetStrength

object PrashnaHouseAnalyzer {

    fun analyzeHouses(
        chart: VedicChart,
        category: PrashnaCategory,
        language: Language
    ): HouseAnalysis {
        val relevantHouses = QUESTION_CATEGORIES[category] ?: listOf(1, 7)
        val houseConditions = mutableMapOf<Int, HouseCondition>()
        val houseLords = mutableMapOf<Int, PlanetPosition>()
        val planetsInHouses = mutableMapOf<Int, List<PlanetPosition>>()

        for (house in 1..12) {
            val houseSign = ZodiacSign.fromLongitude(chart.houseCusps[house - 1])
            val houseLord = houseSign.ruler
            val lordPosition = chart.planetPositions.first { it.planet == houseLord }
            houseLords[house] = lordPosition
            planetsInHouses[house] = chart.planetPositions.filter { it.house == house }
            val lordStrength = calculatePlanetStrength(lordPosition, chart).overallStrength
            val aspectsToHouse = chart.planetPositions.mapNotNull { planet ->
                if (isAspectingHouse(planet, house)) {
                    PlanetaryAspect(
                        aspectingPlanet = planet.planet,
                        aspectedPlanet = null,
                        aspectedHouse = house,
                        aspectType = AspectType.CONJUNCTION,
                        orb = 0.0,
                        isBenefic = planet.planet in NATURAL_BENEFICS
                    )
                } else null
            }
            val beneficAspects = aspectsToHouse.count { it.isBenefic }
            val maleficAspects = aspectsToHouse.count { !it.isBenefic }
            val planetsPresent = planetsInHouses[house]?.map { it.planet } ?: emptyList()
            val condition = when {
                lordStrength.value >= 4 && beneficAspects > maleficAspects -> HouseStrength.EXCELLENT
                lordStrength.value >= 3 && beneficAspects >= maleficAspects -> HouseStrength.GOOD
                lordStrength.value >= 2 -> HouseStrength.MODERATE
                maleficAspects > beneficAspects -> HouseStrength.AFFLICTED
                else -> HouseStrength.POOR
            }
            houseConditions[house] = HouseCondition(
                house = house,
                lord = houseLord,
                lordPosition = lordPosition.house,
                lordStrength = lordStrength,
                planetsPresent = planetsPresent,
                aspectsToHouse = aspectsToHouse,
                condition = condition
            )
        }
        val interpretation = generateHouseInterpretation(relevantHouses, houseConditions, category, language)
        return HouseAnalysis(
            relevantHouses = relevantHouses,
            houseConditions = houseConditions,
            houseLords = houseLords,
            planetsInHouses = planetsInHouses,
            interpretation = interpretation
        )
    }

    private fun generateHouseInterpretation(
        relevantHouses: List<Int>,
        conditions: Map<Int, HouseCondition>,
        category: PrashnaCategory,
        language: Language
    ): String {
        val houseDescs = relevantHouses.map { house ->
            val condition = conditions[house]
            StringResources.get(StringKeyAnalysis.PRASHNA_HOUSE_ITEM_TEMPLATE, language).format(
                house.localized(language),
                condition?.condition?.getLocalizedName(language) ?: ""
            )
        }
        return StringResources.get(StringKeyAnalysis.PRASHNA_HOUSE_INTERP_TEMPLATE, language).format(
            category.getLocalizedName(language),
            houseDescs.joinToString(". ")
        )
    }
}


