package com.astro.storm.ephemeris.varshaphala

import com.astro.storm.core.common.Language
import com.astro.storm.core.common.StringKeyAnalysis
import com.astro.storm.core.common.StringResources
import com.astro.storm.core.model.Nakshatra
import com.astro.storm.core.model.Planet
import com.astro.storm.core.model.ZodiacSign
import com.astro.storm.ephemeris.varshaphala.VarshaphalaConstants.MUDDA_DASHA_DAYS
import com.astro.storm.ephemeris.varshaphala.VarshaphalaConstants.MUDDA_DASHA_PLANETS
import com.astro.storm.ephemeris.varshaphala.VarshaphalaConstants.STANDARD_ZODIAC_SIGNS
import com.astro.storm.ephemeris.varshaphala.VarshaphalaHelpers.evaluatePlanetStrengthDescription
import com.astro.storm.ephemeris.varshaphala.VarshaphalaHelpers.getHouseSignificance
import com.astro.storm.ephemeris.varshaphala.VarshaphalaHelpers.getStandardZodiacIndex
import java.time.LocalDate
import java.time.temporal.ChronoUnit

object MuddaDashaCalculator {

    fun calculateMuddaDasha(
        chart: SolarReturnChart,
        startDate: LocalDate,
        language: Language,
        asOf: LocalDate = LocalDate.now()
    ): List<MuddaDashaPeriod> {
        val moonLong = chart.planetPositions[Planet.MOON]?.longitude ?: 0.0
        val (nakshatra, _) = Nakshatra.fromLongitude(moonLong)
        val startingLord = nakshatra.ruler
        val startIndex = MUDDA_DASHA_PLANETS.indexOf(startingLord).let { if (it >= 0) it else 0 }
        val periods = mutableListOf<MuddaDashaPeriod>()
        var currentDate = startDate

        for (i in MUDDA_DASHA_PLANETS.indices) {
            val planet = MUDDA_DASHA_PLANETS[(startIndex + i) % MUDDA_DASHA_PLANETS.size]
            val days = (MUDDA_DASHA_DAYS[planet] ?: 30).coerceAtLeast(1)
            val endDate = currentDate.plusDays(days.toLong() - 1)
            val isCurrent = !asOf.isBefore(currentDate) && !asOf.isAfter(endDate)
            val strength = evaluatePlanetStrengthDescription(planet, chart, language)
            periods.add(MuddaDashaPeriod(
                planet = planet, startDate = currentDate, endDate = endDate, days = days,
                subPeriods = calculateMuddaAntardasha(planet, currentDate, endDate, language),
                planetStrength = strength, houseRuled = getHousesRuledBy(planet, chart),
                prediction = generateDashaPrediction(planet, chart, strength, language),
                keywords = getDashaKeywords(planet, chart, language), isCurrent = isCurrent,
                progressPercent = if (isCurrent) (ChronoUnit.DAYS.between(currentDate, asOf).toFloat() / days).coerceIn(0f, 1f) else if (asOf.isAfter(endDate)) 1f else 0f
            ))
            currentDate = endDate.plusDays(1)
        }
        return periods
    }

    private fun calculateMuddaAntardasha(mainPlanet: Planet, startDate: LocalDate, endDate: LocalDate, language: Language): List<MuddaAntardasha> {
        val totalDays = ChronoUnit.DAYS.between(startDate, endDate).toInt().coerceAtLeast(1)
        val subPeriods = mutableListOf<MuddaAntardasha>()
        val startIndex = MUDDA_DASHA_PLANETS.indexOf(mainPlanet).let { if (it >= 0) it else 0 }
        var currentDate = startDate
        val subDays = (totalDays / MUDDA_DASHA_PLANETS.size).coerceAtLeast(1)
        for (i in MUDDA_DASHA_PLANETS.indices) {
            val planet = MUDDA_DASHA_PLANETS[(startIndex + i) % MUDDA_DASHA_PLANETS.size]
            val actualSubDays = if (i == MUDDA_DASHA_PLANETS.size - 1) ChronoUnit.DAYS.between(currentDate, endDate).toInt().coerceAtLeast(1) else subDays
            val subEndDate = currentDate.plusDays(actualSubDays.toLong() - 1).let { if (it.isAfter(endDate)) endDate else it }
            subPeriods.add(MuddaAntardasha(planet, currentDate, subEndDate, actualSubDays, StringResources.get(StringKeyAnalysis.VARSHA_DASHA_PERIOD_FORMAT, language, mainPlanet.getLocalizedName(language), planet.getLocalizedName(language))))
            currentDate = subEndDate.plusDays(1)
            if (currentDate.isAfter(endDate)) break
        }
        return subPeriods
    }

    private fun getHousesRuledBy(planet: Planet, chart: SolarReturnChart): List<Int> {
        val houses = mutableListOf<Int>()
        val ascIndex = getStandardZodiacIndex(chart.ascendant)
        for (i in 0..11) if (STANDARD_ZODIAC_SIGNS[(ascIndex + i) % 12].ruler == planet) houses.add(i + 1)
        return houses
    }

    private fun generateDashaPrediction(planet: Planet, chart: SolarReturnChart, strength: String, language: Language): String {
        val nature = when (planet) {
            Planet.SUN -> StringResources.get(StringKeyAnalysis.PLANET_NATURE_SUN, language); Planet.MOON -> StringResources.get(StringKeyAnalysis.PLANET_NATURE_MOON, language)
            Planet.MARS -> StringResources.get(StringKeyAnalysis.PLANET_NATURE_MARS, language); Planet.MERCURY -> StringResources.get(StringKeyAnalysis.PLANET_NATURE_MERCURY, language)
            Planet.JUPITER -> StringResources.get(StringKeyAnalysis.PLANET_NATURE_JUPITER, language); Planet.VENUS -> StringResources.get(StringKeyAnalysis.PLANET_NATURE_VENUS, language)
            Planet.SATURN -> StringResources.get(StringKeyAnalysis.PLANET_NATURE_SATURN, language); Planet.RAHU -> StringResources.get(StringKeyAnalysis.PLANET_NATURE_RAHU, language)
            Planet.KETU -> StringResources.get(StringKeyAnalysis.PLANET_NATURE_KETU, language); else -> StringResources.get(StringKeyAnalysis.VARSHA_TONE_BALANCED, language)
        }
        val quality = when (strength) {
            StringResources.get(StringKeyAnalysis.VARSHA_STRENGTH_EXALTED, language) -> StringResources.get(StringKeyAnalysis.VARSHA_DASHA_EXCEPTIONAL, language)
            StringResources.get(StringKeyAnalysis.VARSHA_STRENGTH_STRONG, language) -> StringResources.get(StringKeyAnalysis.VARSHA_DASHA_SUPPORTED, language)
            StringResources.get(StringKeyAnalysis.VARSHA_STRENGTH_DEBILITATED, language) -> StringResources.get(StringKeyAnalysis.VARSHA_DASHA_CHALLENGING, language)
            else -> StringResources.get(StringKeyAnalysis.VARSHA_DASHA_MIXED, language)
        }
        return StringResources.get(StringKeyAnalysis.VARSHA_DASHA_PREDICTION_FORMAT, language, planet.getLocalizedName(language), nature, getHouseSignificance(chart.planetPositions[planet]?.house ?: 1, language), quality)
    }

    private fun getDashaKeywords(planet: Planet, chart: SolarReturnChart, language: Language): List<String> {
        val keys = when (planet) {
            Planet.SUN -> listOf(StringKeyAnalysis.KEYWORD_LEADERSHIP, StringKeyAnalysis.KEYWORD_VITALITY, StringKeyAnalysis.KEYWORD_FATHER)
            Planet.MOON -> listOf(StringKeyAnalysis.KEYWORD_EMOTIONS, StringKeyAnalysis.KEYWORD_MOTHER, StringKeyAnalysis.KEYWORD_PUBLIC)
            Planet.MARS -> listOf(StringKeyAnalysis.KEYWORD_ACTION, StringKeyAnalysis.KEYWORD_ENERGY, StringKeyAnalysis.KEYWORD_COURAGE)
            Planet.MERCURY -> listOf(StringKeyAnalysis.KEYWORD_COMMUNICATION, StringKeyAnalysis.KEYWORD_LEARNING, StringKeyAnalysis.KEYWORD_BUSINESS)
            Planet.JUPITER -> listOf(StringKeyAnalysis.KEYWORD_WISDOM, StringKeyAnalysis.KEYWORD_GROWTH, StringKeyAnalysis.KEYWORD_FORTUNE)
            Planet.VENUS -> listOf(StringKeyAnalysis.KEYWORD_LOVE, StringKeyAnalysis.KEYWORD_ART, StringKeyAnalysis.KEYWORD_COMFORT)
            Planet.SATURN -> listOf(StringKeyAnalysis.KEYWORD_DISCIPLINE, StringKeyAnalysis.KEYWORD_KARMA, StringKeyAnalysis.KEYWORD_DELAYS)
            Planet.RAHU -> listOf(StringKeyAnalysis.KEYWORD_AMBITION, StringKeyAnalysis.KEYWORD_INNOVATION, StringKeyAnalysis.KEYWORD_FOREIGN)
            Planet.KETU -> listOf(StringKeyAnalysis.KEYWORD_SPIRITUALITY, StringKeyAnalysis.KEYWORD_DETACHMENT, StringKeyAnalysis.KEYWORD_PAST)
            else -> listOf(StringKeyAnalysis.KEYWORD_GENERAL)
        }
        return (keys.map { StringResources.get(it, language) }).take(5)
    }
}


