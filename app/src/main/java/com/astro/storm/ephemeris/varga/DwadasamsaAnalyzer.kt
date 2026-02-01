package com.astro.storm.ephemeris.varga

import com.astro.storm.core.common.Language
import com.astro.storm.core.common.StringKeyDivisional
import com.astro.storm.core.common.StringKeyDosha
import com.astro.storm.core.common.StringResources
import com.astro.storm.core.model.Planet
import com.astro.storm.core.model.PlanetPosition
import com.astro.storm.core.model.VedicChart
import com.astro.storm.ephemeris.AstrologicalConstants
import com.astro.storm.ephemeris.DivisionalChartCalculator
import com.astro.storm.ephemeris.varga.VargaHelpers.getHouseLord
import com.astro.storm.ephemeris.varga.VargaHelpers.calculatePlanetStrengthInVarga

object DwadasamsaAnalyzer {

    fun analyzeDwadasamsa(chart: VedicChart, language: Language): DwadasamsaAnalysis {
        val d12 = DivisionalChartCalculator.calculateDwadasamsa(chart)
        val sun = d12.planetPositions.find { it.planet == Planet.SUN }; val moon = d12.planetPositions.find { it.planet == Planet.MOON }
        val ninthLord = d12.planetPositions.find { it.planet == getHouseLord(chart, 9) }; val fourthLord = d12.planetPositions.find { it.planet == getHouseLord(chart, 4) }
        val father = analyzeFatherIndicators(sun, ninthLord, language); val mother = analyzeMotherIndicators(moon, fourthLord, language)
        return DwadasamsaAnalysis(sun, moon, ninthLord, fourthLord, father, mother, analyzeInheritance(d12, language), analyzeAncestralProperty(d12, language), analyzeFamilyLineage(d12, language), analyzeParentalLongevity(d12, language), generateParentalRecommendations(father, mother, language))
    }

    private fun analyzeFatherIndicators(sun: PlanetPosition?, ninthLord: PlanetPosition?, lang: Language): ParentAnalysis {
        var s = 50.0; sun?.let { if (AstrologicalConstants.isExalted(Planet.SUN, it.sign)) s += 20.0; if (AstrologicalConstants.isInOwnSign(Planet.SUN, it.sign)) s += 15.0; if (AstrologicalConstants.isDebilitated(Planet.SUN, it.sign)) s -= 20.0; if (it.house in AstrologicalConstants.DUSTHANA_HOUSES) s -= 15.0 }
        ninthLord?.let { if (it.house in AstrologicalConstants.KENDRA_HOUSES) s += 10.0; if (it.house in AstrologicalConstants.TRIKONA_HOUSES) s += 10.0 }
        return ParentAnalysis(StringResources.get(StringKeyDoshaPart2.DWADASAMSA_FATHER, lang), sun?.let { calculatePlanetStrengthInVarga(it) } ?: 50.0, ninthLord?.let { calculatePlanetStrengthInVarga(it) } ?: 50.0, StringResources.get(when { s >= 70 -> StringKeyDivisional.DWADASAMSA_FATHER_WELLBEING_GOOD; s >= 50 -> StringKeyDivisional.HORA_POTENTIAL_MODERATE; else -> StringKeyDivisional.HORA_POTENTIAL_CHALLENGES }, lang), determineFatherCharacteristics(sun, ninthLord, lang), assessParentRelationship(sun, lang))
    }

    private fun analyzeMotherIndicators(moon: PlanetPosition?, fourthLord: PlanetPosition?, lang: Language): ParentAnalysis {
        var s = 50.0; moon?.let { if (AstrologicalConstants.isExalted(Planet.MOON, it.sign)) s += 20.0; if (AstrologicalConstants.isInOwnSign(Planet.MOON, it.sign)) s += 15.0; if (AstrologicalConstants.isDebilitated(Planet.MOON, it.sign)) s -= 20.0; if (it.house in AstrologicalConstants.DUSTHANA_HOUSES) s -= 15.0 }
        fourthLord?.let { if (it.house in AstrologicalConstants.KENDRA_HOUSES) s += 10.0; if (it.house in AstrologicalConstants.TRIKONA_HOUSES) s += 10.0 }
        return ParentAnalysis(StringResources.get(StringKeyDoshaPart2.DWADASAMSA_MOTHER, lang), moon?.let { calculatePlanetStrengthInVarga(it) } ?: 50.0, fourthLord?.let { calculatePlanetStrengthInVarga(it) } ?: 50.0, StringResources.get(when { s >= 70 -> StringKeyDivisional.DWADASAMSA_MOTHER_WELLBEING_GOOD; s >= 50 -> StringKeyDivisional.HORA_POTENTIAL_MODERATE; else -> StringKeyDivisional.HORA_POTENTIAL_CHALLENGES }, lang), determineMotherCharacteristics(moon, fourthLord, lang), assessParentRelationship(moon, lang))
    }

    private fun determineFatherCharacteristics(sun: PlanetPosition?, ninthLord: PlanetPosition?, lang: Language): String = StringResources.get(when { sun != null && AstrologicalConstants.isExalted(Planet.SUN, sun.sign) -> StringKeyDivisional.DWADASAMSA_FATHER_CHAR_AUTHORITATIVE; sun?.house == 10 -> StringKeyDivisional.DWADASAMSA_FATHER_CHAR_AMBITIOUS; ninthLord?.planet == Planet.JUPITER -> StringKeyDivisional.DWADASAMSA_FATHER_CHAR_RELIGIOUS; else -> StringKeyDivisional.DWADASAMSA_FATHER_CHAR_VARIES }, lang)

    private fun determineMotherCharacteristics(moon: PlanetPosition?, fourthLord: PlanetPosition?, lang: Language): String = StringResources.get(when { moon != null && AstrologicalConstants.isExalted(Planet.MOON, moon.sign) -> StringKeyDivisional.DWADASAMSA_MOTHER_CHAR_NURTURING; moon?.house == 4 -> StringKeyDivisional.DWADASAMSA_MOTHER_CHAR_HOME; fourthLord?.planet == Planet.VENUS -> StringKeyDivisional.DWADASAMSA_MOTHER_CHAR_ARTISTIC; else -> StringKeyDivisional.DWADASAMSA_MOTHER_CHAR_VARIES }, lang)

    private fun assessParentRelationship(p: PlanetPosition?, lang: Language): String = StringResources.get(when { p == null -> StringKeyDivisional.DWADASAMSA_REL_DEPENDS; p.house in AstrologicalConstants.KENDRA_HOUSES -> StringKeyDivisional.DWADASAMSA_REL_STRONG; p.house in AstrologicalConstants.TRIKONA_HOUSES -> StringKeyDivisional.DWADASAMSA_REL_HARMONIOUS; p.house in AstrologicalConstants.DUSTHANA_HOUSES -> StringKeyDivisional.DWADASAMSA_REL_CHALLENGES; else -> StringKeyDivisional.DWADASAMSA_REL_MODERATE }, lang)

    private fun analyzeInheritance(d12: com.astro.storm.ephemeris.DivisionalChartData, lang: Language): InheritanceAnalysis {
        var s = 50.0; val t = d12.planetPositions.filter { it.house == 4 }.map { it.planet }; if (Planet.JUPITER in d12.planetPositions.filter { it.house == 2 }.map { it.planet }) s += 20.0; if (Planet.VENUS in t) s += 15.0; if (t.any { it in AstrologicalConstants.NATURAL_BENEFICS }) s += 10.0
        return InheritanceAnalysis(StringResources.get(when { s >= 70 -> StringKeyDivisional.DWADASAMSA_INH_GOOD; s >= 50 -> StringKeyDivisional.DWADASAMSA_INH_MODERATE; else -> StringKeyDivisional.DWADASAMSA_INH_LIMITED }, lang), buildList { if (d12.planetPositions.any { it.house == 2 }) add(StringResources.get(StringKeyDivisional.DWADASAMSA_SOURCE_WEALTH, lang)); if (t.isNotEmpty()) add(StringResources.get(StringKeyDivisional.DWADASAMSA_SOURCE_PROPERTY, lang)); if (d12.planetPositions.any { it.house == 8 && it.planet in AstrologicalConstants.NATURAL_BENEFICS }) add(StringResources.get(StringKeyDivisional.DWADASAMSA_SOURCE_UNEXPECTED, lang)) }, StringResources.get(StringKeyDivisional.DWADASAMSA_INH_TIMING, lang))
    }

    private fun analyzeAncestralProperty(d12: com.astro.storm.ephemeris.DivisionalChartData, lang: Language): List<String> {
        val res = mutableListOf<String>(); val t = d12.planetPositions.filter { it.house == 4 }.map { it.planet }
        if (Planet.SATURN in t) res.add(StringResources.get(StringKeyDivisional.DWADASAMSA_PROP_OLD, lang))
        if (Planet.MARS in t) res.add(StringResources.get(StringKeyDivisional.DWADASAMSA_PROP_LAND, lang))
        if (Planet.JUPITER in t) res.add(StringResources.get(StringKeyDivisional.DWADASAMSA_PROP_RELIGIOUS, lang))
        return res
    }

    private fun analyzeFamilyLineage(d12: com.astro.storm.ephemeris.DivisionalChartData, lang: Language): List<String> {
        val res = mutableListOf<String>(); d12.planetPositions.find { it.planet == d12.ascendantSign.ruler }?.let { res.add(StringResources.get(when { AstrologicalConstants.isExalted(it.planet, it.sign) -> StringKeyDivisional.DWADASAMSA_FAM_NOBLE; it.house in listOf(9, 10) -> StringKeyDivisional.DWADASAMSA_FAM_DHARMA; it.house in listOf(5, 11) -> StringKeyDivisional.DWADASAMSA_FAM_SUCCESS; else -> StringKeyDivisional.DWADASAMSA_FAM_DEPENDS }, lang)) }; return res
    }

    private fun analyzeParentalLongevity(d12: com.astro.storm.ephemeris.DivisionalChartData, lang: Language): ParentalLongevityIndicators {
        val s = d12.planetPositions.find { it.planet == Planet.SUN }; val m = d12.planetPositions.find { it.planet == Planet.MOON }
        return ParentalLongevityIndicators(StringResources.get(when { s != null && AstrologicalConstants.isExalted(Planet.SUN, s.sign) -> StringKeyDivisional.DWADASAMSA_LONG_INDICATED; s?.house in AstrologicalConstants.DUSTHANA_HOUSES -> StringKeyDivisional.DWADASAMSA_LONG_ATTENTION; else -> StringKeyDivisional.DWADASAMSA_LONG_MODERATE }, lang), StringResources.get(when { m != null && AstrologicalConstants.isExalted(Planet.MOON, m.sign) -> StringKeyDivisional.DWADASAMSA_LONG_INDICATED; m?.house in AstrologicalConstants.DUSTHANA_HOUSES -> StringKeyDivisional.DWADASAMSA_LONG_ATTENTION; else -> StringKeyDivisional.DWADASAMSA_LONG_MODERATE }, lang), buildList { if (s?.house in AstrologicalConstants.DUSTHANA_HOUSES) add(StringResources.get(StringKeyDivisional.DWADASAMSA_CONCERN_FATHER, lang)); if (m?.house in AstrologicalConstants.DUSTHANA_HOUSES) add(StringResources.get(StringKeyDivisional.DWADASAMSA_CONCERN_MOTHER, lang)) })
    }

    private fun generateParentalRecommendations(f: ParentAnalysis, m: ParentAnalysis, lang: Language): List<String> {
        val res = mutableListOf<String>(); if (f.significatorStrength < 50) { res.add(StringResources.get(StringKeyDivisional.DWADASAMSA_REC_FATHER_SUN, lang)); res.add(StringResources.get(StringKeyDivisional.DWADASAMSA_REC_FATHER_RESPECT, lang)) }
        if (m.significatorStrength < 50) { res.add(StringResources.get(StringKeyDivisional.DWADASAMSA_REC_MOTHER_MOON, lang)); res.add(StringResources.get(StringKeyDivisional.DWADASAMSA_REC_MOTHER_CHARITY, lang)) }
        res.add(StringResources.get(StringKeyDivisional.DWADASAMSA_REC_PITRU_TARPAN, lang)); return res
    }
}


