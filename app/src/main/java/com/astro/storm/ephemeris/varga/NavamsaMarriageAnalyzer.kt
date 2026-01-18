package com.astro.storm.ephemeris.varga

import com.astro.storm.core.common.Language
import com.astro.storm.core.common.StringKeyDivisional
import com.astro.storm.core.common.StringResources
import com.astro.storm.core.model.Planet
import com.astro.storm.core.model.PlanetPosition
import com.astro.storm.core.model.VedicChart
import com.astro.storm.core.model.ZodiacSign
import com.astro.storm.ephemeris.AstrologicalConstants
import com.astro.storm.ephemeris.DivisionalChartCalculator
import com.astro.storm.ephemeris.varga.VargaHelpers.getHouseLord
import com.astro.storm.ephemeris.varga.VargaHelpers.calculatePlanetStrengthInVarga

object NavamsaMarriageAnalyzer {

    fun analyzeNavamsaForMarriage(chart: VedicChart, language: Language): NavamsaMarriageAnalysis {
        val d9 = DivisionalChartCalculator.calculateNavamsa(chart)
        val venus = d9.planetPositions.find { it.planet == Planet.VENUS }
        val jupiter = d9.planetPositions.find { it.planet == Planet.JUPITER }
        val seventhLord = d9.planetPositions.find { it.planet == getHouseLord(chart, 7) }
        val lagnaLordPos = d9.planetPositions.find { it.planet == d9.ascendantSign.ruler }
        val upapada = calculateUpapada(chart)
        val darakaraka = calculateDarakaraka(chart)
        val factors = analyzeMarriageTimingFactors(venus, jupiter, seventhLord, d9.planetPositions.find { it.planet == darakaraka }, language)
        return NavamsaMarriageAnalysis(venus, jupiter, seventhLord, lagnaLordPos, upapada, d9.planetPositions.find { it.planet == upapada.ruler }, darakaraka, d9.planetPositions.find { it.planet == darakaraka }, factors, analyzeSpouseCharacteristics(seventhLord, venus, d9.planetPositions.find { it.planet == darakaraka }, d9.planetPositions.find { it.planet == upapada.ruler }, d9, language), calculateSpouseDirection(seventhLord, d9.planetPositions.find { it.planet == darakaraka }, language), analyzeMultipleMarriageFactors(chart, d9, language), analyzeMarriageMuhurtaCompatibility(chart, language), generateMarriageRecommendations(factors, language))
    }

    private fun calculateUpapada(chart: VedicChart): ZodiacSign {
        val lordPos = chart.planetPositions.find { it.planet == getHouseLord(chart, 2) }?.house ?: 2
        val asc = ZodiacSign.fromLongitude(chart.ascendant)
        return ZodiacSign.entries[(asc.ordinal + (2 + (lordPos - 2 + 12) % 12 - 1 + 12) % 12) % 12]
    }

    private fun calculateDarakaraka(chart: VedicChart): Planet = chart.planetPositions.filter { it.planet !in listOf(Planet.RAHU, Planet.KETU, Planet.URANUS, Planet.NEPTUNE, Planet.PLUTO) }.minByOrNull { it.longitude % 30.0 }?.planet ?: Planet.VENUS

    private fun analyzeMarriageTimingFactors(v: PlanetPosition?, j: PlanetPosition?, s: PlanetPosition?, d: PlanetPosition?, lang: Language): MarriageTimingFactors {
        val p = mutableListOf<Planet>(); v?.let { if (it.house in listOf(1, 5, 7, 9, 11)) p.add(Planet.VENUS) }; j?.let { if (it.house in listOf(1, 5, 7, 9, 11)) p.add(Planet.JUPITER) }; s?.let { p.add(it.planet) }
        return MarriageTimingFactors(p, v?.let { calculatePlanetStrengthInVarga(it) } ?: 50.0, s?.let { calculatePlanetStrengthInVarga(it) } ?: 50.0, d?.let { calculatePlanetStrengthInVarga(it) } ?: 50.0, StringResources.get(StringKeyDivisional.NAVAMSA_TRANSIT_JUPITER, lang))
    }

    private fun analyzeSpouseCharacteristics(s: PlanetPosition?, v: PlanetPosition?, d: PlanetPosition?, u: PlanetPosition?, d9: com.astro.storm.ephemeris.DivisionalChartData, lang: Language): SpouseCharacteristics = SpouseCharacteristics(determineSpouseNature(s, v, lang), determineSpouseAppearance(d, v, lang), estimateSpouseProfession(s, lang), determineSpouseFamilyBackground(u, lang))

    private fun determineSpouseNature(s: PlanetPosition?, v: PlanetPosition?, lang: Language): String {
        val p = s?.planet ?: v?.planet ?: Planet.VENUS
        return StringResources.get(when (p) { Planet.SUN -> StringKeyDivisional.NAVAMSA_NATURE_SUN; Planet.MOON -> StringKeyDivisional.NAVAMSA_NATURE_MOON; Planet.MARS -> StringKeyDivisional.NAVAMSA_NATURE_MARS; Planet.MERCURY -> StringKeyDivisional.NAVAMSA_NATURE_MERCURY; Planet.JUPITER -> StringKeyDivisional.NAVAMSA_NATURE_JUPITER; Planet.VENUS -> StringKeyDivisional.NAVAMSA_NATURE_VENUS; Planet.SATURN -> StringKeyDivisional.NAVAMSA_NATURE_SATURN; Planet.RAHU -> StringKeyDivisional.NAVAMSA_NATURE_RAHU; Planet.KETU -> StringKeyDivisional.NAVAMSA_NATURE_KETU; else -> StringKeyDivisional.NAVAMSA_NATURE_MIXED }, lang)
    }

    private fun determineSpouseAppearance(d: PlanetPosition?, v: PlanetPosition?, lang: Language): String {
        val p = d?.planet ?: v?.planet ?: Planet.VENUS
        return StringResources.get(when (p) { Planet.SUN -> StringKeyDivisional.NAVAMSA_APPEAR_SUN; Planet.MOON -> StringKeyDivisional.NAVAMSA_APPEAR_MOON; Planet.MARS -> StringKeyDivisional.NAVAMSA_APPEAR_MARS; Planet.MERCURY -> StringKeyDivisional.NAVAMSA_APPEAR_MERCURY; Planet.JUPITER -> StringKeyDivisional.NAVAMSA_APPEAR_JUPITER; Planet.VENUS -> StringKeyDivisional.NAVAMSA_APPEAR_VENUS; Planet.SATURN -> StringKeyDivisional.NAVAMSA_APPEAR_SATURN; else -> StringKeyDivisional.NAVAMSA_APPEAR_VARIES }, lang)
    }

    private fun estimateSpouseProfession(s: PlanetPosition?, lang: Language): List<String> {
        val p = s?.planet ?: return listOf(StringResources.get(StringKeyDivisional.NAVAMSA_PROF_VARIOUS, lang))
        return when (p) {
            Planet.SUN -> listOf(StringKeyDivisional.NAVAMSA_PROF_GOVT, StringKeyDivisional.NAVAMSA_PROF_ADMIN, StringKeyDivisional.NAVAMSA_PROF_MEDICINE)
            Planet.MOON -> listOf(StringKeyDivisional.NAVAMSA_PROF_HEALTHCARE, StringKeyDivisional.NAVAMSA_PROF_HOSPITALITY, StringKeyDivisional.NAVAMSA_PROF_PR)
            Planet.MARS -> listOf(StringKeyDivisional.NAVAMSA_PROF_ENGINEERING, StringKeyDivisional.NAVAMSA_PROF_MILITARY, StringKeyDivisional.NAVAMSA_PROF_SPORTS)
            Planet.MERCURY -> listOf(StringKeyDivisional.NAVAMSA_PROF_BUSINESS, StringKeyDivisional.NAVAMSA_PROF_WRITING, StringKeyDivisional.NAVAMSA_PROF_TECH)
            Planet.JUPITER -> listOf(StringKeyDivisional.NAVAMSA_PROF_TEACHING, StringKeyDivisional.NAVAMSA_PROF_LAW, StringKeyDivisional.NAVAMSA_PROF_FINANCE)
            Planet.VENUS -> listOf(StringKeyDivisional.NAVAMSA_PROF_ARTS, StringKeyDivisional.NAVAMSA_PROF_FASHION, StringKeyDivisional.NAVAMSA_PROF_ENTERTAINMENT)
            Planet.SATURN -> listOf(StringKeyDivisional.NAVAMSA_PROF_LABOR, StringKeyDivisional.NAVAMSA_PROF_CONSTRUCTION, StringKeyDivisional.NAVAMSA_PROF_AGRICULTURE)
            else -> listOf(StringKeyDivisional.NAVAMSA_PROF_FIELDS)
        }.map { StringResources.get(it, lang) }
    }

    private fun determineSpouseFamilyBackground(u: PlanetPosition?, lang: Language): String = StringResources.get(when (u?.planet) { Planet.JUPITER -> StringKeyDivisional.NAVAMSA_FAM_JUPITER; Planet.VENUS -> StringKeyDivisional.NAVAMSA_FAM_VENUS; Planet.SUN -> StringKeyDivisional.NAVAMSA_FAM_SUN; Planet.MOON -> StringKeyDivisional.NAVAMSA_FAM_MOON; Planet.SATURN -> StringKeyDivisional.NAVAMSA_FAM_SATURN; else -> StringKeyDivisional.NAVAMSA_FAM_VARIES }, lang)

    private fun calculateSpouseDirection(s: PlanetPosition?, d: PlanetPosition?, lang: Language): String {
        val sign = s?.sign ?: d?.sign ?: return StringResources.get(StringKeyDivisional.NAVAMSA_DIR_UNKNOWN, lang)
        return StringResources.get(when (sign) { ZodiacSign.ARIES, ZodiacSign.LEO, ZodiacSign.SAGITTARIUS -> StringKeyDivisional.NAVAMSA_DIR_EAST; ZodiacSign.TAURUS, ZodiacSign.VIRGO, ZodiacSign.CAPRICORN -> StringKeyDivisional.NAVAMSA_DIR_SOUTH; ZodiacSign.GEMINI, ZodiacSign.LIBRA, ZodiacSign.AQUARIUS -> StringKeyDivisional.NAVAMSA_DIR_WEST; else -> StringKeyDivisional.NAVAMSA_DIR_NORTH }, lang)
    }

    private fun analyzeMultipleMarriageFactors(chart: VedicChart, d9: com.astro.storm.ephemeris.DivisionalChartData, lang: Language): MultipleMarriageIndicators {
        val res = mutableListOf<String>(); var r = 0; val asc = ZodiacSign.fromLongitude(chart.ascendant)
        if (chart.planetPositions.count { ((it.sign.ordinal - asc.ordinal + 12) % 12) + 1 == 7 } >= 2) { res.add(StringResources.get(StringKeyDivisional.NAVAMSA_RISK_MULTIPLE_PLANETS, lang)); r++ }
        if (chart.planetPositions.find { it.planet == Planet.VENUS }?.isRetrograde == true) { res.add(StringResources.get(StringKeyDivisional.NAVAMSA_RISK_RETRO_VENUS, lang)); r++ }
        if (chart.planetPositions.find { it.planet == getHouseLord(chart, 7) }?.house in AstrologicalConstants.DUSTHANA_HOUSES) { res.add(StringResources.get(StringKeyDivisional.NAVAMSA_RISK_7TH_LORD_DUSTHANA, lang)); r++ }
        return MultipleMarriageIndicators(r >= 2, res, if (r < 2) listOf(StringResources.get(StringKeyDivisional.NAVAMSA_MITIGATE_NONE, lang)) else emptyList())
    }

    private fun analyzeMarriageMuhurtaCompatibility(chart: VedicChart, lang: Language): String = StringResources.get(StringKeyDivisional.NAVAMSA_MUHURTA_JUPITER, lang, chart.planetPositions.find { it.planet == Planet.MOON }?.sign?.getLocalizedName(lang) ?: "")

    private fun generateMarriageRecommendations(f: MarriageTimingFactors, lang: Language): List<String> {
        val res = mutableListOf<String>(); if (f.venusNavamsaStrength > 70) res.add(StringResources.get(StringKeyDivisional.NAVAMSA_REC_VENUS_STRONG, lang)); if (f.venusNavamsaStrength < 50) res.add(StringResources.get(StringKeyDivisional.NAVAMSA_REC_VENUS_WEAK, lang))
        res.add(StringResources.get(StringKeyDivisional.NAVAMSA_REC_TIMING, lang, f.favorableDashaPlanets.joinToString(", ") { it.getLocalizedName(lang) })); res.add(f.transitConsiderations)
        return res
    }
}


