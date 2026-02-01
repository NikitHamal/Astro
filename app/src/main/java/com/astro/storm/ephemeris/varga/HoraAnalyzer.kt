package com.astro.storm.ephemeris.varga

import com.astro.storm.core.common.Language
import com.astro.storm.core.common.StringKeyDivisional
import com.astro.storm.core.common.StringKeyPrediction
import com.astro.storm.core.common.StringResources
import com.astro.storm.core.model.Planet
import com.astro.storm.core.model.PlanetPosition
import com.astro.storm.core.model.VedicChart
import com.astro.storm.core.model.ZodiacSign
import com.astro.storm.ephemeris.AstrologicalConstants
import com.astro.storm.ephemeris.DivisionalChartCalculator
import com.astro.storm.ephemeris.varga.VargaHelpers.getHouseLord

object HoraAnalyzer {

    fun analyzeHora(chart: VedicChart, language: Language): HoraAnalysis {
        val horaChart = DivisionalChartCalculator.calculateHora(chart)
        val sunHoraPlanets = mutableListOf<Planet>()
        val moonHoraPlanets = mutableListOf<Planet>()
        horaChart.planetPositions.forEach { position ->
            when (position.sign) { ZodiacSign.LEO -> sunHoraPlanets.add(position.planet); ZodiacSign.CANCER -> moonHoraPlanets.add(position.planet); else -> {} }
        }
        val secondLordInHora = horaChart.planetPositions.find { it.planet == getHouseLord(chart, 2) }
        val eleventhLordInHora = horaChart.planetPositions.find { it.planet == getHouseLord(chart, 11) }
        val wealthIndicators = mutableListOf<WealthIndicator>()
        sunHoraPlanets.forEach { wealthIndicators.add(WealthIndicator(it, WealthType.SELF_EARNED, calculateHoraStrength(it, ZodiacSign.LEO, chart), getSunHoraWealthSources(it, language))) }
        moonHoraPlanets.forEach { wealthIndicators.add(WealthIndicator(it, WealthType.INHERITED_LIQUID, calculateHoraStrength(it, ZodiacSign.CANCER, chart), getMoonHoraWealthSources(it, language))) }
        val overallWealthPotential = calculateOverallWealthPotential(sunHoraPlanets, moonHoraPlanets, secondLordInHora, eleventhLordInHora)
        return HoraAnalysis(sunHoraPlanets, moonHoraPlanets, wealthIndicators, overallWealthPotential, secondLordInHora?.sign, eleventhLordInHora?.sign, calculateWealthTimingFromHora(wealthIndicators, language), generateHoraRecommendations(wealthIndicators, overallWealthPotential, language))
    }

    private fun calculateHoraStrength(planet: Planet, horaSign: ZodiacSign, chart: VedicChart): Double {
        var s = 50.0
        if (planet in listOf(Planet.JUPITER, Planet.VENUS, Planet.MERCURY)) s += 15.0
        if (horaSign == ZodiacSign.LEO && planet in listOf(Planet.SUN, Planet.MARS, Planet.JUPITER)) s += 20.0
        if (horaSign == ZodiacSign.CANCER && planet in listOf(Planet.MOON, Planet.VENUS, Planet.JUPITER)) s += 20.0
        chart.planetPositions.find { it.planet == planet }?.let { if (AstrologicalConstants.isExalted(planet, it.sign)) s += 15.0; if (AstrologicalConstants.isInOwnSign(planet, it.sign)) s += 10.0; if (AstrologicalConstants.isDebilitated(planet, it.sign)) s -= 20.0 }
        return s.coerceIn(0.0, 100.0)
    }

    private fun calculateOverallWealthPotential(sunHoraPlanets: List<Planet>, moonHoraPlanets: List<Planet>, secondLord: PlanetPosition?, eleventhLord: PlanetPosition?): WealthPotential {
        var s = 50.0 + (sunHoraPlanets.count { it in AstrologicalConstants.NATURAL_BENEFICS } + moonHoraPlanets.count { it in AstrologicalConstants.NATURAL_BENEFICS }) * 8.0
        secondLord?.let { if (it.sign == ZodiacSign.LEO || it.sign == ZodiacSign.CANCER) s += 10.0 }
        eleventhLord?.let { if (it.sign == ZodiacSign.LEO || it.sign == ZodiacSign.CANCER) s += 10.0 }
        if (Planet.JUPITER in sunHoraPlanets || Planet.JUPITER in moonHoraPlanets) s += 15.0
        return when { s >= 85 -> WealthPotential.EXCEPTIONAL; s >= 70 -> WealthPotential.HIGH; s >= 55 -> WealthPotential.MODERATE; s >= 40 -> WealthPotential.AVERAGE; else -> WealthPotential.LOW }
    }

    private fun calculateWealthTimingFromHora(indicators: List<WealthIndicator>, language: Language): List<WealthTimingPeriod> {
        return indicators.filter { it.strength > 60 }.map { WealthTimingPeriod(it.planet, it.type, StringResources.get(StringKeyGeneralPart8.PRED_MAHADASHA_LABEL, language, it.planet.getLocalizedName(language)), it.strength > 65, it.sources) }
    }

    private fun generateHoraRecommendations(indicators: List<WealthIndicator>, potential: WealthPotential, language: Language): List<String> {
        val recs = mutableListOf<String>()
        when (potential) {
            WealthPotential.EXCEPTIONAL, WealthPotential.HIGH -> { recs.add(StringResources.get(StringKeyDivisional.HORA_REC_FOCUS_SOURCES, language)); recs.add(StringResources.get(StringKeyDivisional.HORA_REC_INVEST_FAVORABLE, language)) }
            WealthPotential.MODERATE -> { recs.add(StringResources.get(StringKeyDivisional.HORA_REC_CONSISTENT_EFFORT, language)); recs.add(StringResources.get(StringKeyDivisional.HORA_REC_STRENGTHEN_WEAK, language)) }
            else -> { recs.add(StringResources.get(StringKeyDivisional.HORA_REC_WORSHIP_LAKSHMI, language)); recs.add(StringResources.get(StringKeyDivisional.HORA_REC_STRENGTHEN_JUP_VEN, language)); recs.add(StringResources.get(StringKeyDivisional.HORA_REC_AVOID_SPECULATION, language)) }
        }
        indicators.filter { it.strength > 70 }.forEach { recs.add(StringResources.get(StringKeyDivisional.HORA_REC_CAPITALIZE_STRENGTH, language, it.planet.getLocalizedName(language), it.sources.firstOrNull() ?: StringResources.get(StringKeyDivisional.DASHAMSA_TYPE_GENERAL, language))) }
        return recs
    }

    private fun getSunHoraWealthSources(planet: Planet, lang: Language): List<String> = when (planet) {
        Planet.SUN -> listOf(StringKeyDivisional.HORA_SUN_SOURCE_GOVT, StringKeyDivisional.HORA_SUN_SOURCE_GOLD, StringKeyDivisional.HORA_SUN_SOURCE_FATHER, StringKeyDivisional.HORA_SUN_SOURCE_AUTHORITY)
        Planet.MARS -> listOf(StringKeyDivisional.HORA_SUN_SOURCE_REAL_ESTATE, StringKeyDivisional.HORA_SUN_SOURCE_ENGINEERING, StringKeyDivisional.HORA_SUN_SOURCE_MILITARY, StringKeyDivisional.HORA_SUN_SOURCE_SPORTS)
        Planet.JUPITER -> listOf(StringKeyDivisional.HORA_SUN_SOURCE_TEACHING, StringKeyDivisional.HORA_SUN_SOURCE_CONSULTANCY, StringKeyDivisional.HORA_SUN_SOURCE_BANKING, StringKeyDivisional.HORA_SUN_SOURCE_RELIGIOUS)
        Planet.SATURN -> listOf(StringKeyDivisional.HORA_SUN_SOURCE_MINING, StringKeyDivisional.HORA_SUN_SOURCE_OIL, StringKeyDivisional.HORA_SUN_SOURCE_LABOR, StringKeyDivisional.HORA_SUN_SOURCE_LAND)
        Planet.MERCURY -> listOf(StringKeyDivisional.HORA_SUN_SOURCE_BUSINESS, StringKeyDivisional.HORA_SUN_SOURCE_COMMUNICATION, StringKeyDivisional.HORA_SUN_SOURCE_TECHNOLOGY, StringKeyDivisional.HORA_SUN_SOURCE_TRADE)
        Planet.VENUS -> listOf(StringKeyDivisional.HORA_SUN_SOURCE_ARTS, StringKeyDivisional.HORA_SUN_SOURCE_ENTERTAINMENT, StringKeyDivisional.HORA_SUN_SOURCE_LUXURY, StringKeyDivisional.HORA_SUN_SOURCE_BEAUTY)
        Planet.MOON -> listOf(StringKeyDivisional.HORA_SUN_SOURCE_PUBLIC, StringKeyDivisional.HORA_SUN_SOURCE_HOSPITALITY, StringKeyDivisional.HORA_SUN_SOURCE_DAIRY, StringKeyDivisional.HORA_SUN_SOURCE_LIQUIDS)
        Planet.RAHU -> listOf(StringKeyDivisional.HORA_SUN_SOURCE_FOREIGN, StringKeyDivisional.HORA_SUN_SOURCE_TECHNOLOGY, StringKeyDivisional.HORA_SUN_SOURCE_UNCONVENTIONAL, StringKeyDivisional.HORA_SUN_SOURCE_SPECULATION)
        Planet.KETU -> listOf(StringKeyDivisional.HORA_SUN_SOURCE_SPIRITUAL, StringKeyDivisional.HORA_SUN_SOURCE_OCCULT, StringKeyDivisional.HORA_SUN_SOURCE_DETACHMENT, StringKeyDivisional.HORA_SUN_SOURCE_RESEARCH)
        else -> emptyList()
    }.map { StringResources.get(it, lang) }

    private fun getMoonHoraWealthSources(planet: Planet, lang: Language): List<String> = when (planet) {
        Planet.MOON -> listOf(StringKeyDivisional.HORA_MOON_SOURCE_INHERITANCE, StringKeyDivisional.HORA_MOON_SOURCE_MOTHER, StringKeyDivisional.HORA_MOON_SOURCE_PEARLS, StringKeyDivisional.HORA_MOON_SOURCE_SILVER, StringKeyDivisional.HORA_SUN_SOURCE_LIQUIDS)
        Planet.VENUS -> listOf(StringKeyDivisional.HORA_MOON_SOURCE_SPOUSE, StringKeyDivisional.HORA_SUN_SOURCE_LUXURY, StringKeyDivisional.HORA_SUN_SOURCE_GOLD)
        Planet.JUPITER -> listOf(StringKeyDivisional.HORA_SUN_SOURCE_RELIGIOUS, StringKeyDivisional.HORA_MOON_SOURCE_EDUCATIONAL, StringKeyDivisional.HORA_SUN_SOURCE_FATHER)
        Planet.MERCURY -> listOf(StringKeyDivisional.HORA_MOON_SOURCE_FAMILY_BIZ, StringKeyDivisional.HORA_MOON_SOURCE_ANCESTRAL_TRADE, StringKeyDivisional.HORA_MOON_SOURCE_INTELLECTUAL)
        Planet.SUN -> listOf(StringKeyDivisional.HORA_MOON_SOURCE_GOVT_BENEFITS, StringKeyDivisional.HORA_MOON_SOURCE_ROYAL, StringKeyDivisional.HORA_SUN_SOURCE_AUTHORITY)
        Planet.MARS -> listOf(StringKeyDivisional.HORA_SUN_SOURCE_REAL_ESTATE, StringKeyDivisional.HORA_SUN_SOURCE_LAND, StringKeyDivisional.HORA_MOON_SOURCE_MILITARY_PENSION)
        Planet.SATURN -> listOf(StringKeyDivisional.HORA_MOON_SOURCE_OLD_WEALTH, StringKeyDivisional.HORA_MOON_SOURCE_DELAYED, StringKeyDivisional.HORA_SUN_SOURCE_LABOR)
        Planet.RAHU -> listOf(StringKeyDivisional.HORA_SUN_SOURCE_FOREIGN, StringKeyDivisional.HORA_SUN_SOURCE_UNCONVENTIONAL, StringKeyDivisional.HORA_MOON_SOURCE_SPOUSE)
        Planet.KETU -> listOf(StringKeyDivisional.HORA_SUN_SOURCE_SPIRITUAL, StringKeyDivisional.HORA_SUN_SOURCE_RESEARCH, StringKeyDivisional.HORA_SUN_SOURCE_DETACHMENT)
        else -> emptyList()
    }.map { StringResources.get(it, lang) }
}


