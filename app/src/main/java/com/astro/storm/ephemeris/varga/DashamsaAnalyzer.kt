package com.astro.storm.ephemeris.varga

import com.astro.storm.core.common.Language
import com.astro.storm.core.common.StringKeyAnalysis
import com.astro.storm.core.common.StringKeyDivisional
import com.astro.storm.core.common.StringKeyMatch
import com.astro.storm.core.common.StringResources
import com.astro.storm.core.model.Planet
import com.astro.storm.core.model.PlanetPosition
import com.astro.storm.core.model.VedicChart
import com.astro.storm.core.model.ZodiacSign
import com.astro.storm.ephemeris.AstrologicalConstants
import com.astro.storm.ephemeris.DivisionalChartCalculator
import com.astro.storm.ephemeris.varga.VargaHelpers.getHouseLord

object DashamsaAnalyzer {

    fun analyzeDashamsa(chart: VedicChart, language: Language): DashamsaAnalysis {
        val d10 = DivisionalChartCalculator.calculateDasamsa(chart)
        val tenthLord = d10.planetPositions.find { it.planet == getHouseLord(chart, 10) }
        val sun = d10.planetPositions.find { it.planet == Planet.SUN }
        val saturn = d10.planetPositions.find { it.planet == Planet.SATURN }
        val mercury = d10.planetPositions.find { it.planet == Planet.MERCURY }
        val careerType = determineCareerType(d10, language)
        return DashamsaAnalysis(tenthLord, sun, saturn, mercury, d10.ascendantSign, careerType, mapPlanetsToIndustries(d10, language), analyzeGovernmentServicePotential(sun, d10, language), analyzeBusinessVsService(d10, language), calculateCareerPeakTiming(chart, d10, language), analyzeMultipleCareers(d10, language), analyzeProfessionalStrengths(d10, language), generateCareerRecommendations(careerType, mapPlanetsToIndustries(d10, language), language))
    }

    private fun determineCareerType(d10: com.astro.storm.ephemeris.DivisionalChartData, lang: Language): List<CareerType> {
        val res = mutableListOf<CareerType>(); d10.planetPositions.filter { it.house == 10 }.forEach { res.add(getCareerTypeFromPlanet(it.planet, lang)) }
        d10.planetPositions.find { it.planet == d10.ascendantSign.ruler }?.let { res.add(getCareerTypeFromPlanet(it.planet, lang)) }
        return res.distinct()
    }

    private fun getCareerTypeFromPlanet(p: Planet, lang: Language): CareerType = when (p) {
        Planet.SUN -> CareerType(StringResources.get(StringKeyDivisional.DASHAMSA_TYPE_ADMIN, lang), listOf(StringKeyDivisional.NAVAMSA_PROF_GOVT, StringKeyDivisional.NAVAMSA_PROF_ADMIN, StringKeyPrashnaPart2.PRASHNA_CAT_CAREER, StringKeyDivisional.NAVAMSA_PROF_MEDICINE).map { StringResources.get(it, lang) }, StringResources.get(StringKeyDivisional.DASHAMSA_SUIT_ADMIN, lang))
        Planet.MOON -> CareerType(StringResources.get(StringKeyDivisional.DASHAMSA_TYPE_PUBLIC, lang), listOf(StringKeyDivisional.NAVAMSA_PROF_HEALTHCARE, StringKeyDivisional.NAVAMSA_PROF_HOSPITALITY, StringKeyDivisional.NAVAMSA_PROF_PR, StringKeyDivisional.NAVAMSA_PROF_MEDICINE).map { StringResources.get(it, lang) }, StringResources.get(StringKeyDivisional.DASHAMSA_SUIT_PUBLIC, lang))
        Planet.MARS -> CareerType(StringResources.get(StringKeyDivisional.DASHAMSA_TYPE_TECH, lang), listOf(StringKeyDivisional.NAVAMSA_PROF_ENGINEERING, StringKeyDivisional.NAVAMSA_PROF_MILITARY, StringKeyMatchPart1.ACTIVITY_BUSINESS_NAME, StringKeyDivisional.NAVAMSA_PROF_MEDICINE, StringKeyDivisional.NAVAMSA_PROF_SPORTS).map { StringResources.get(it, lang) }, StringResources.get(StringKeyDivisional.DASHAMSA_SUIT_TECH, lang))
        Planet.MERCURY -> CareerType(StringResources.get(StringKeyDivisional.DASHAMSA_TYPE_COMM, lang), listOf(StringKeyDivisional.NAVAMSA_PROF_BUSINESS, StringKeyPrashnaPart2.PRASHNA_CAT_FINANCE, StringKeyDivisional.NAVAMSA_PROF_WRITING, StringKeyDivisional.NAVAMSA_PROF_TECH, StringKeyDivisional.HORA_SUN_SOURCE_TRADE).map { StringResources.get(it, lang) }, StringResources.get(StringKeyDivisional.DASHAMSA_SUIT_COMM, lang))
        Planet.JUPITER -> CareerType(StringResources.get(StringKeyDivisional.DASHAMSA_TYPE_ADVISORY, lang), listOf(StringKeyDivisional.NAVAMSA_PROF_TEACHING, StringKeyDivisional.NAVAMSA_PROF_LAW, StringKeyDivisional.HORA_SUN_SOURCE_CONSULTANCY, StringKeyDivisional.NAVAMSA_PROF_FINANCE, StringKeyDivisional.HORA_SUN_SOURCE_RELIGIOUS).map { StringResources.get(it, lang) }, StringResources.get(StringKeyDivisional.DASHAMSA_SUIT_ADVISORY, lang))
        Planet.VENUS -> CareerType(StringResources.get(StringKeyDivisional.DASHAMSA_TYPE_CREATIVE, lang), listOf(StringKeyDivisional.NAVAMSA_PROF_ARTS, StringKeyDivisional.NAVAMSA_PROF_ENTERTAINMENT, StringKeyDivisional.NAVAMSA_PROF_FASHION, StringKeyDivisional.HORA_SUN_SOURCE_LUXURY, StringKeyDivisional.NAVAMSA_PROF_HOSPITALITY).map { StringResources.get(it, lang) }, StringResources.get(StringKeyDivisional.DASHAMSA_SUIT_CREATIVE, lang))
        Planet.SATURN -> CareerType(StringResources.get(StringKeyDivisional.DASHAMSA_TYPE_LABOR, lang), listOf(StringKeyDivisional.HORA_SUN_SOURCE_MINING, StringKeyDivisional.NAVAMSA_PROF_CONSTRUCTION, StringKeyDivisional.HORA_SUN_SOURCE_OIL, StringKeyDivisional.NAVAMSA_PROF_AGRICULTURE, StringKeyDivisional.HORA_SUN_SOURCE_LABOR).map { StringResources.get(it, lang) }, StringResources.get(StringKeyDivisional.DASHAMSA_SUIT_LABOR, lang))
        Planet.RAHU -> CareerType(StringResources.get(StringKeyDivisional.DASHAMSA_TYPE_UNCONVENTIONAL, lang), listOf(StringKeyDivisional.HORA_SUN_SOURCE_TECHNOLOGY, StringKeyDivisional.HORA_SUN_SOURCE_FOREIGN, StringKeyDivisional.HORA_SUN_SOURCE_RESEARCH, StringKeyPrashnaPart2.PRASHNA_CAT_GENERAL).map { StringResources.get(it, lang) }, StringResources.get(StringKeyDivisional.DASHAMSA_SUIT_UNCONVENTIONAL, lang))
        Planet.KETU -> CareerType(StringResources.get(StringKeyDivisional.DASHAMSA_TYPE_SPIRITUAL, lang), listOf(StringKeyDivisional.HORA_SUN_SOURCE_SPIRITUAL, StringKeyDivisional.HORA_SUN_SOURCE_OCCULT, StringKeyDivisional.HORA_SUN_SOURCE_RESEARCH, StringKeyPrashnaPart2.PRASHNA_CAT_HEALTH).map { StringResources.get(it, lang) }, StringResources.get(StringKeyDivisional.DASHAMSA_SUIT_SPIRITUAL, lang))
        else -> CareerType(StringResources.get(StringKeyDivisional.DASHAMSA_TYPE_GENERAL, lang), emptyList(), StringResources.get(StringKeyDivisional.DASHAMSA_SUIT_VARIOUS, lang))
    }

    private fun mapPlanetsToIndustries(d10: com.astro.storm.ephemeris.DivisionalChartData, lang: Language): Map<Planet, List<String>> {
        val res = mutableMapOf<Planet, List<String>>(); d10.planetPositions.filter { it.house in listOf(1, 10) }.forEach { res[it.planet] = getIndustriesForPlanet(it.planet, lang) }; return res
    }

    private fun getIndustriesForPlanet(p: Planet, lang: Language): List<String> = when (p) {
        Planet.SUN -> listOf(StringKeyDivisional.NAVAMSA_PROF_GOVT, StringKeyDivisional.NAVAMSA_PROF_ADMIN, StringKeyDivisional.NAVAMSA_PROF_MEDICINE, StringKeyDivisional.HORA_SUN_SOURCE_GOLD)
        Planet.MOON -> listOf(StringKeyDivisional.NAVAMSA_PROF_HOSPITALITY, StringKeyDivisional.NAVAMSA_PROF_HEALTHCARE, StringKeyDivisional.HORA_SUN_SOURCE_DAIRY, StringKeyDivisional.NAVAMSA_PROF_PR, StringKeyDivisional.HORA_SUN_SOURCE_LIQUIDS)
        Planet.MARS -> listOf(StringKeyDivisional.NAVAMSA_PROF_MILITARY, StringKeyMatchPart1.ACTIVITY_BUSINESS_NAME, StringKeyDivisional.NAVAMSA_PROF_MEDICINE, StringKeyDivisional.NAVAMSA_PROF_ENGINEERING, StringKeyDivisional.HORA_SUN_SOURCE_REAL_ESTATE)
        Planet.MERCURY -> listOf(StringKeyDivisional.NAVAMSA_PROF_BUSINESS, StringKeyPrashnaPart2.PRASHNA_CAT_FINANCE, StringKeyDivisional.NAVAMSA_PROF_WRITING, StringKeyDivisional.NAVAMSA_PROF_TECH, StringKeyDivisional.HORA_SUN_SOURCE_COMMUNICATION)
        Planet.JUPITER -> listOf(StringKeyDivisional.NAVAMSA_PROF_TEACHING, StringKeyDivisional.NAVAMSA_PROF_LAW, StringKeyDivisional.HORA_SUN_SOURCE_CONSULTANCY, StringKeyDivisional.NAVAMSA_PROF_FINANCE, StringKeyDivisional.HORA_SUN_SOURCE_BANKING)
        Planet.VENUS -> listOf(StringKeyDivisional.NAVAMSA_PROF_ARTS, StringKeyDivisional.NAVAMSA_PROF_ENTERTAINMENT, StringKeyDivisional.NAVAMSA_PROF_FASHION, StringKeyDivisional.HORA_SUN_SOURCE_LUXURY, StringKeyDivisional.HORA_SUN_SOURCE_BEAUTY)
        Planet.SATURN -> listOf(StringKeyDivisional.HORA_SUN_SOURCE_MINING, StringKeyDivisional.HORA_SUN_SOURCE_LABOR, StringKeyDivisional.NAVAMSA_PROF_CONSTRUCTION, StringKeyDivisional.HORA_SUN_SOURCE_OIL, StringKeyDivisional.NAVAMSA_PROF_AGRICULTURE)
        Planet.RAHU -> listOf(StringKeyDivisional.HORA_SUN_SOURCE_TECHNOLOGY, StringKeyDivisional.HORA_SUN_SOURCE_TRADE, StringKeyMatchPart1.ACTIVITY_TRAVEL_NAME, StringKeyDivisional.HORA_SUN_SOURCE_LIQUIDS)
        Planet.KETU -> listOf(StringKeyDivisional.HORA_SUN_SOURCE_SPIRITUAL, StringKeyDivisional.HORA_SUN_SOURCE_OCCULT, StringKeyMatchPart1.ACTIVITY_MEDICAL_NAME, StringKeyDivisional.HORA_SUN_SOURCE_RESEARCH)
        else -> emptyList()
    }.map { StringResources.get(it, lang) }

    private fun analyzeGovernmentServicePotential(sun: PlanetPosition?, d10: com.astro.storm.ephemeris.DivisionalChartData, lang: Language): GovernmentServiceAnalysis {
        var p = 50.0; sun?.let { if (it.house == 10) p += 25.0; if (it.house == 1) p += 15.0; if (AstrologicalConstants.isExalted(Planet.SUN, it.sign)) p += 20.0; if (AstrologicalConstants.isInOwnSign(Planet.SUN, it.sign)) p += 15.0 }; d10.planetPositions.find { it.planet == Planet.SATURN }?.let { if (it.house in listOf(1, 10)) p += 10.0 }
        return GovernmentServiceAnalysis(StringResources.get(when { p >= 80 -> StringKeyDivisional.DASHAMSA_GOVT_VERY_HIGH; p >= 65 -> StringKeyDivisional.DASHAMSA_GOVT_HIGH; p >= 50 -> StringKeyDivisional.DASHAMSA_GOVT_MODERATE; else -> StringKeyDivisional.DASHAMSA_GOVT_LOW }, lang), buildList { sun?.let { if (it.house == 10) add(StringResources.get(StringKeyDivisional.DASHAMSA_FACTOR_SUN_10, lang)); if (AstrologicalConstants.isExalted(Planet.SUN, it.sign)) add(StringResources.get(StringKeyDivisional.DASHAMSA_FACTOR_EXALTED_SUN, lang)) } }, if (p > 60) listOf(StringKeyDivisional.NAVAMSA_PROF_ADMIN, StringKeyPrashnaPart2.PRASHNA_CAT_FINANCE, StringKeyDivisional.HORA_SUN_SOURCE_FOREIGN, StringKeyDivisional.NAVAMSA_PROF_MILITARY).map { StringResources.get(it, lang) } else emptyList())
    }

    private fun analyzeBusinessVsService(d10: com.astro.storm.ephemeris.DivisionalChartData, lang: Language): BusinessVsServiceAnalysis {
        var b = 0; var s = 0; val m = d10.planetPositions.find { it.planet == Planet.MERCURY }; val sat = d10.planetPositions.find { it.planet == Planet.SATURN }; val r = d10.planetPositions.find { it.planet == Planet.RAHU }
        m?.let { if (it.house in listOf(1, 7, 10)) b += 2 }; r?.let { if (it.house in listOf(1, 7, 10)) b += 2 }; sat?.let { if (it.house in listOf(1, 6, 10)) s += 2 }
        if (d10.planetPositions.filter { it.house == 10 }.any { it.planet in listOf(Planet.MERCURY, Planet.VENUS, Planet.RAHU) }) b++; if (d10.planetPositions.filter { it.house == 10 }.any { it.planet in listOf(Planet.SUN, Planet.SATURN, Planet.MOON) }) s++
        return BusinessVsServiceAnalysis((b * 10).coerceAtMost(100), (s * 10).coerceAtMost(100), StringResources.get(when { b > s + 1 -> StringKeyDivisional.DASHAMSA_BIZ_APTITUDE; s > b + 1 -> StringKeyDivisional.DASHAMSA_SERVICE_APTITUDE; else -> StringKeyDivisional.DASHAMSA_BOTH_APTITUDE }, lang), if (b > s) getIndustriesForPlanet(m?.planet ?: Planet.MERCURY, lang) else emptyList())
    }

    private fun calculateCareerPeakTiming(chart: VedicChart, d10: com.astro.storm.ephemeris.DivisionalChartData, lang: Language): List<CareerPeakPeriod> {
        val res = mutableListOf(CareerPeakPeriod(getHouseLord(chart, 10), StringResources.get(StringKeyDivisional.DASHAMSA_PEAK_10TH_LORD, lang), StringResources.get(StringKeyDivisional.DASHAMSA_PEAK_10TH_SIG, lang)))
        d10.planetPositions.filter { it.house in listOf(1, 10) || AstrologicalConstants.isExalted(it.planet, it.sign) || AstrologicalConstants.isInOwnSign(it.planet, it.sign) }.forEach { res.add(CareerPeakPeriod(it.planet, StringResources.get(StringKeyDivisional.DASHAMSA_PEAK_PLANET_FMT, lang, it.planet.getLocalizedName(lang)), StringResources.get(StringKeyDivisional.DASHAMSA_PEAK_PLANET_SIG, lang, getIndustriesForPlanet(it.planet, lang).firstOrNull() ?: StringResources.get(StringKeyDivisional.DASHAMSA_TYPE_GENERAL, lang)))) }
        return res
    }

    private fun analyzeMultipleCareers(d10: com.astro.storm.ephemeris.DivisionalChartData, lang: Language): List<String> {
        val res = mutableListOf<String>(); if (d10.planetPositions.count { it.house == 10 } >= 2) res.add(StringResources.get(StringKeyDivisional.DASHAMSA_MULTIPLE_10TH, lang))
        d10.planetPositions.find { it.planet == Planet.MERCURY }?.let { if (it.house in listOf(1, 10)) res.add(StringResources.get(StringKeyDivisional.DASHAMSA_MERC_VERSATILE, lang)) }
        d10.planetPositions.find { it.planet == Planet.RAHU }?.let { if (it.house in listOf(1, 10)) res.add(StringResources.get(StringKeyDivisional.DASHAMSA_RAHU_UNCONVENTIONAL, lang)) }
        return res
    }

    private fun analyzeProfessionalStrengths(d10: com.astro.storm.ephemeris.DivisionalChartData, lang: Language): List<String> {
        val res = mutableListOf<String>(); d10.planetPositions.find { it.planet == d10.ascendantSign.ruler }?.let { if (it.house in AstrologicalConstants.KENDRA_HOUSES) res.add(StringResources.get(StringKeyDivisional.DASHAMSA_STRENGTH_IDENTITY, lang)); if (AstrologicalConstants.isExalted(it.planet, it.sign)) res.add(StringResources.get(StringKeyDivisional.DASHAMSA_STRENGTH_EXCEPTIONAL, lang)) }
        d10.planetPositions.filter { it.house == 10 }.forEach { when (it.planet) { Planet.SUN -> res.add(StringResources.get(StringKeyDivisional.DASHAMSA_STRENGTH_LEADERSHIP, lang)); Planet.MOON -> res.add(StringResources.get(StringKeyDivisional.DASHAMSA_STRENGTH_PUBLIC, lang)); Planet.MARS -> res.add(StringResources.get(StringKeyDivisional.DASHAMSA_STRENGTH_DRIVE, lang)); Planet.MERCURY -> res.add(StringResources.get(StringKeyDivisional.DASHAMSA_STRENGTH_COMM, lang)); Planet.JUPITER -> res.add(StringResources.get(StringKeyDivisional.DASHAMSA_STRENGTH_WISDOM, lang)); Planet.VENUS -> res.add(StringResources.get(StringKeyDivisional.DASHAMSA_STRENGTH_CREATIVITY, lang)); Planet.SATURN -> res.add(StringResources.get(StringKeyDivisional.DASHAMSA_STRENGTH_PERSISTENCE, lang)); else -> {} } }
        return res
    }

    private fun generateCareerRecommendations(c: List<CareerType>, m: Map<Planet, List<String>>, lang: Language): List<String> {
        val res = mutableListOf<String>(); if (c.isNotEmpty()) { res.add(StringResources.get(StringKeyDivisional.DASHAMSA_REC_PRIMARY_FOCUS, lang, c.first().name)); c.first().industries.take(3).let { res.add(StringResources.get(StringKeyDivisional.DASHAMSA_REC_TOP_INDUSTRIES, lang, it.joinToString(", "))) } }
        m.entries.take(2).forEach { (p, i) -> res.add(StringResources.get(StringKeyDivisional.DASHAMSA_REC_PERIOD_FAVORS, lang, p.getLocalizedName(lang), i.firstOrNull() ?: StringResources.get(StringKeyDivisional.DASHAMSA_TYPE_GENERAL, lang))) }
        return res
    }
}


