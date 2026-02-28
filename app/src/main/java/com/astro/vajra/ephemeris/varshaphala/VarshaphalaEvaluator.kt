package com.astro.vajra.ephemeris.varshaphala

import com.astro.vajra.core.common.*
import com.astro.vajra.core.model.*
import com.astro.vajra.ephemeris.varshaphala.VarshaphalaConstants.DAY_LORDS
import com.astro.vajra.ephemeris.varshaphala.VarshaphalaConstants.EXALTATION_DEGREES
import com.astro.vajra.ephemeris.varshaphala.VarshaphalaConstants.MUDDA_DASHA_PLANETS
import com.astro.vajra.ephemeris.varshaphala.VarshaphalaConstants.STANDARD_ZODIAC_SIGNS
import com.astro.vajra.ephemeris.varshaphala.VarshaphalaHelpers.areFriends
import com.astro.vajra.ephemeris.varshaphala.VarshaphalaHelpers.areNeutral
import com.astro.vajra.ephemeris.varshaphala.VarshaphalaHelpers.calculateWholeSignHouse
import com.astro.vajra.ephemeris.varshaphala.VarshaphalaHelpers.evaluatePlanetStrengthDescription
import com.astro.vajra.ephemeris.varshaphala.VarshaphalaHelpers.getHouseSignificance
import com.astro.vajra.ephemeris.varshaphala.VarshaphalaHelpers.getStandardZodiacIndex
import com.astro.vajra.ephemeris.varshaphala.VarshaphalaHelpers.getZodiacSignFromLongitude
import com.astro.vajra.ephemeris.varshaphala.VarshaphalaHelpers.isDebilitated
import com.astro.vajra.ephemeris.varshaphala.VarshaphalaHelpers.isExalted
import com.astro.vajra.ephemeris.varshaphala.VarshaphalaHelpers.normalizeAngle
import java.time.DayOfWeek

object VarshaphalaEvaluator {

    fun determineYearLord(
        solarReturnChart: SolarReturnChart,
        muntha: MunthaResult,
        natalChart: VedicChart,
        allBalas: List<PanchaVargiyaBala>
    ): Planet {
        val dayOfWeek = solarReturnChart.solarReturnTime.dayOfWeek
        val dayIndex = when (dayOfWeek) {
            DayOfWeek.SUNDAY -> 0
            DayOfWeek.MONDAY -> 1
            DayOfWeek.TUESDAY -> 2
            DayOfWeek.WEDNESDAY -> 3
            DayOfWeek.THURSDAY -> 4
            DayOfWeek.FRIDAY -> 5
            DayOfWeek.SATURDAY -> 6
        }
        val dinaPati = DAY_LORDS[dayIndex]
        val lagnaPati = solarReturnChart.ascendant.ruler
        val munthaPati = muntha.lord
        val natalMoonSign = natalChart.planetPositions.find { it.planet == Planet.MOON }?.sign ?: ZodiacSign.ARIES
        val janmaRashiPati = natalMoonSign.ruler
        val candidates = listOf(dinaPati, lagnaPati, munthaPati, janmaRashiPati).distinct()
        val candidatesWithStrength = candidates.map { planet ->
            val bala = allBalas.find { it.planet == planet }?.total ?: 0.0
            val additionalStrength = calculateAdditionalStrength(planet, solarReturnChart)
            planet to (bala + additionalStrength)
        }
        return candidatesWithStrength.maxByOrNull { it.second }?.first ?: dinaPati
    }

    private fun calculateAdditionalStrength(planet: Planet, chart: SolarReturnChart): Double {
        var strength = 0.0
        val position = chart.planetPositions[planet] ?: return 0.0
        when (position.house) {
            1, 4, 7, 10 -> strength += 5.0
            5, 9 -> strength += 3.0
            6, 8, 12 -> strength -= 3.0
        }
        if (position.sign.ruler == planet) strength += 4.0
        if (isExalted(planet, position.sign)) strength += 5.0
        if (isDebilitated(planet, position.sign)) strength -= 5.0
        if (!position.isRetrograde) strength += 1.0
        return strength
    }

    fun calculateMuntha(
        natalChart: VedicChart,
        age: Int,
        solarReturnChart: SolarReturnChart,
        language: Language
    ): MunthaResult {
        val natalAscLongitude = normalizeAngle(natalChart.ascendant)
        val progressedLongitude = normalizeAngle(natalAscLongitude + (age * 30.0))
        val munthaSign = getZodiacSignFromLongitude(progressedLongitude)
        val degreeInSign = progressedLongitude % 30.0
        val ascendantLongitude = getStandardZodiacIndex(solarReturnChart.ascendant) * 30.0 + solarReturnChart.ascendantDegree
        val munthaHouse = calculateWholeSignHouse(progressedLongitude, ascendantLongitude)
        val munthaLord = munthaSign.ruler
        val lordPosition = solarReturnChart.planetPositions[munthaLord]
        val lordHouse = lordPosition?.house ?: 1
        val lordStrength = evaluatePlanetStrengthDescription(munthaLord, solarReturnChart, language)
        val themes = getMunthaThemes(munthaHouse, language)
        val interpretation = generateMunthaInterpretation(munthaSign, munthaHouse, munthaLord, lordHouse, lordStrength, language)
        return MunthaResult(
            longitude = progressedLongitude, sign = munthaSign, house = munthaHouse, degree = degreeInSign,
            lord = munthaLord, lordHouse = lordHouse, lordStrength = lordStrength, interpretation = interpretation, themes = themes
        )
    }

    private fun getMunthaThemes(house: Int, language: Language): List<String> {
        val keys = when (house) {
            1 -> listOf(StringKeyAnalysis.MUNTHA_PERSONAL_GROWTH, StringKeyAnalysis.MUNTHA_NEW_BEGINNINGS, StringKeyAnalysis.MUNTHA_HEALTH_FOCUS)
            2 -> listOf(StringKeyAnalysis.MUNTHA_FINANCIAL_GAINS, StringKeyAnalysis.MUNTHA_FAMILY_MATTERS, StringKeyAnalysis.MUNTHA_SPEECH)
            3 -> listOf(StringKeyAnalysis.MUNTHA_COMMUNICATION, StringKeyAnalysis.MUNTHA_SHORT_TRAVELS, StringKeyAnalysis.MUNTHA_SIBLINGS)
            4 -> listOf(StringKeyAnalysis.MUNTHA_HOME_AFFAIRS, StringKeyAnalysis.MUNTHA_PROPERTY, StringKeyAnalysis.MUNTHA_INNER_PEACE)
            5 -> listOf(StringKeyAnalysis.MUNTHA_CREATIVITY, StringKeyAnalysis.MUNTHA_ROMANCE, StringKeyAnalysis.MUNTHA_CHILDREN)
            6 -> listOf(StringKeyAnalysis.MUNTHA_SERVICE, StringKeyAnalysis.MUNTHA_HEALTH_ISSUES, StringKeyAnalysis.MUNTHA_COMPETITION)
            7 -> listOf(StringKeyAnalysis.MUNTHA_PARTNERSHIPS, StringKeyAnalysis.MUNTHA_MARRIAGE, StringKeyAnalysis.MUNTHA_BUSINESS)
            8 -> listOf(StringKeyAnalysis.MUNTHA_TRANSFORMATION, StringKeyAnalysis.MUNTHA_RESEARCH, StringKeyAnalysis.MUNTHA_INHERITANCE)
            9 -> listOf(StringKeyAnalysis.MUNTHA_FORTUNE, StringKeyAnalysis.MUNTHA_LONG_TRAVEL, StringKeyAnalysis.MUNTHA_HIGHER_LEARNING)
            10 -> listOf(StringKeyAnalysis.MUNTHA_CAREER_ADVANCEMENT, StringKeyAnalysis.MUNTHA_RECOGNITION, StringKeyAnalysis.MUNTHA_AUTHORITY)
            11 -> listOf(StringKeyAnalysis.MUNTHA_GAINS, StringKeyAnalysis.MUNTHA_FRIENDS, StringKeyAnalysis.MUNTHA_FULFILLED_WISHES)
            12 -> listOf(StringKeyAnalysis.MUNTHA_SPIRITUALITY, StringKeyAnalysis.MUNTHA_FOREIGN_LANDS, StringKeyAnalysis.MUNTHA_EXPENSES)
            else -> listOf(StringKeyAnalysis.MUNTHA_GENERAL_GROWTH)
        }
        return keys.map { StringResources.get(it, language) }
    }

    private fun generateMunthaInterpretation(sign: ZodiacSign, house: Int, lord: Planet, lordHouse: Int, lordStrength: String, language: Language): String {
        val houseSignificance = getHouseSignificance(house, language)
        return StringResources.get(StringKeyAnalysis.VARSHA_MUNTHA_DIRECTS, language, house, sign.getLocalizedName(language), houseSignificance.lowercase()) +
                " " + StringResources.get(StringKeyAnalysis.VARSHA_PERIOD_WELL_SUPPORTED, language)
    }

    fun calculateTriPatakiChakra(chart: SolarReturnChart, language: Language): TriPatakiChakra {
        val ascIndex = getStandardZodiacIndex(chart.ascendant)
        val dharmaSigns = listOf(STANDARD_ZODIAC_SIGNS[ascIndex], STANDARD_ZODIAC_SIGNS[(ascIndex + 4) % 12], STANDARD_ZODIAC_SIGNS[(ascIndex + 8) % 12])
        val arthaSigns = listOf(STANDARD_ZODIAC_SIGNS[(ascIndex + 1) % 12], STANDARD_ZODIAC_SIGNS[(ascIndex + 5) % 12], STANDARD_ZODIAC_SIGNS[(ascIndex + 9) % 12])
        val kamaSigns = listOf(STANDARD_ZODIAC_SIGNS[(ascIndex + 2) % 12], STANDARD_ZODIAC_SIGNS[(ascIndex + 6) % 12], STANDARD_ZODIAC_SIGNS[(ascIndex + 10) % 12])
        fun getPlanetsInSector(signs: List<ZodiacSign>): List<Planet> = chart.planetPositions.filter { (_, pos) -> pos.sign in signs }.keys.toList()
        val dharmaPlanets = getPlanetsInSector(dharmaSigns)
        val arthaPlanets = getPlanetsInSector(arthaSigns)
        val kamaPlanets = getPlanetsInSector(kamaSigns)
        val sectors = listOf(
            TriPatakiSector(StringResources.get(StringKeyAnalysis.TRI_PATAKI_DHARMA, language), dharmaSigns, dharmaPlanets, generateSectorInfluence(StringResources.get(StringKeyAnalysis.TRI_PATAKI_DHARMA, language), dharmaPlanets, language)),
            TriPatakiSector(StringResources.get(StringKeyAnalysis.TRI_PATAKI_ARTHA, language), arthaSigns, arthaPlanets, generateSectorInfluence(StringResources.get(StringKeyAnalysis.TRI_PATAKI_ARTHA, language), arthaPlanets, language)),
            TriPatakiSector(StringResources.get(StringKeyAnalysis.TRI_PATAKI_KAMA, language), kamaSigns, kamaPlanets, generateSectorInfluence(StringResources.get(StringKeyAnalysis.TRI_PATAKI_KAMA, language), kamaPlanets, language))
        )
        val dominantSector = sectors.maxByOrNull { it.planets.size }
        val dominantInfluence = when {
            dominantSector?.name == StringResources.get(StringKeyAnalysis.TRI_PATAKI_DHARMA, language) -> StringResources.get(StringKeyAnalysis.TRI_PATAKI_DHARMA_DESC, language)
            dominantSector?.name == StringResources.get(StringKeyAnalysis.TRI_PATAKI_ARTHA, language) -> StringResources.get(StringKeyAnalysis.TRI_PATAKI_ARTHA_DESC, language)
            dominantSector?.name == StringResources.get(StringKeyAnalysis.TRI_PATAKI_KAMA, language) -> StringResources.get(StringKeyAnalysis.TRI_PATAKI_KAMA_DESC, language)
            else -> StringResources.get(StringKeyAnalysis.TRI_PATAKI_BALANCED, language)
        }
        return TriPatakiChakra(chart.ascendant, sectors, dominantInfluence, buildTriPatakiInterpretation(sectors, language))
    }

    private fun generateSectorInfluence(sectorName: String, planets: List<Planet>, language: Language): String {
        if (planets.isEmpty()) return StringResources.get(StringKeyAnalysis.TRI_PATAKI_QUIET, language, sectorName)
        val benefics = planets.filter { it in listOf(Planet.JUPITER, Planet.VENUS, Planet.MOON, Planet.MERCURY) }
        val malefics = planets.filter { it in listOf(Planet.SATURN, Planet.MARS, Planet.RAHU, Planet.KETU) }
        return when {
            benefics.size > malefics.size -> StringResources.get(StringKeyAnalysis.TRI_PATAKI_FAVORABLE, language, benefics.joinToString { it.getLocalizedName(language) })
            malefics.size > benefics.size -> StringResources.get(StringKeyAnalysis.TRI_PATAKI_CHALLENGING, language, malefics.joinToString { it.getLocalizedName(language) })
            else -> StringResources.get(StringKeyAnalysis.TRI_PATAKI_VARIABLE, language)
        }
    }

    private fun buildTriPatakiInterpretation(sectors: List<TriPatakiSector>, language: Language): String {
        val interpretations = mutableListOf<String>()
        sectors.forEach { sector ->
            if (sector.planets.isNotEmpty()) {
                val areaName = when {
                    sector.name.contains(StringResources.get(StringKeyAnalysis.TRI_PATAKI_DHARMA, language)) -> StringResources.get(StringKeyAnalysis.TRI_PATAKI_DHARMA_AREA, language)
                    sector.name.contains(StringResources.get(StringKeyAnalysis.TRI_PATAKI_ARTHA, language)) -> StringResources.get(StringKeyAnalysis.TRI_PATAKI_ARTHA_AREA, language)
                    else -> StringResources.get(StringKeyAnalysis.TRI_PATAKI_KAMA_AREA, language)
                }
                interpretations.add(StringResources.get(StringKeyAnalysis.TRI_PATAKI_EMPHASIS, language, sector.planets.size, sector.name, areaName))
            }
        }
        return if (interpretations.isNotEmpty()) interpretations.joinToString(" ") else StringResources.get(StringKeyAnalysis.TRI_PATAKI_BALANCED_DESC, language)
    }
}
