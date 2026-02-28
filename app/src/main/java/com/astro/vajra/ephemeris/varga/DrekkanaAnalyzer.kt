package com.astro.vajra.ephemeris.varga

import com.astro.vajra.core.common.Language
import com.astro.vajra.core.common.StringKeyDivisional
import com.astro.vajra.core.common.StringResources
import com.astro.vajra.core.model.Planet
import com.astro.vajra.core.model.PlanetPosition
import com.astro.vajra.core.model.VedicChart
import com.astro.vajra.ephemeris.AstrologicalConstants
import com.astro.vajra.ephemeris.DivisionalChartCalculator
import com.astro.vajra.ephemeris.varga.VargaHelpers.getHouseLord
import com.astro.vajra.ephemeris.varga.VargaHelpers.calculatePlanetStrengthInVarga

object DrekkanaAnalyzer {

    fun analyzeDrekkana(chart: VedicChart, language: Language): DrekkanaAnalysis {
        val d3 = DivisionalChartCalculator.calculateDrekkana(chart)
        val mars = d3.planetPositions.find { it.planet == Planet.MARS }
        val thirdLord = d3.planetPositions.find { it.planet == getHouseLord(chart, 3) }
        val siblings = analyzeSiblings(d3, language)
        val courage = analyzeCourage(mars, thirdLord, d3, language)
        val comm = analyzeCommunication(d3, language)
        return DrekkanaAnalysis(mars, thirdLord, siblings, courage, comm, d3.planetPositions.filter { it.house == 3 }, d3.planetPositions.filter { it.house == 11 }, analyzeShortJourneys(d3, language), generateDrekkanaRecommendations(courage, siblings, language))
    }

    private fun analyzeSiblings(d3: com.astro.vajra.ephemeris.DivisionalChartData, lang: Language): SiblingIndicators {
        val t = d3.planetPositions.filter { it.house == 3 }; val e = d3.planetPositions.filter { it.house == 11 }
        val q = (t + e).let { all -> val bc = all.count { it.planet in AstrologicalConstants.NATURAL_BENEFICS }; val mc = all.count { it.planet in AstrologicalConstants.NATURAL_MALEFICS }; when { bc > mc + 1 -> RelationshipQuality.EXCELLENT; bc > mc -> RelationshipQuality.GOOD; bc == mc -> RelationshipQuality.NEUTRAL; mc > bc -> RelationshipQuality.CHALLENGING; else -> RelationshipQuality.DIFFICULT } }
        return SiblingIndicators(estimateSiblingCount(t), estimateSiblingCount(e), q, t.map { it.planet }, e.map { it.planet }, assessSiblingWelfare(d3, lang))
    }

    private fun estimateSiblingCount(planets: List<PlanetPosition>): IntRange = when { planets.isEmpty() -> 0..1; planets.size == 1 -> 1..2; planets.size == 2 -> 2..3; else -> 3..5 }

    private fun assessSiblingWelfare(d3: com.astro.vajra.ephemeris.DivisionalChartData, lang: Language): List<String> {
        val res = mutableListOf<String>(); val t = d3.planetPositions.filter { it.house == 3 }.map { it.planet }
        if (Planet.JUPITER in t) res.add(StringResources.get(StringKeyDivisional.DREKKANA_SIBLING_JUPITER, lang))
        if (Planet.SATURN in t) res.add(StringResources.get(StringKeyDivisional.DREKKANA_SIBLING_SATURN, lang))
        if (Planet.MARS in t) res.add(StringResources.get(StringKeyDivisional.DREKKANA_SIBLING_MARS, lang))
        return res
    }

    private fun analyzeCourage(mars: PlanetPosition?, thirdLord: PlanetPosition?, d3: com.astro.vajra.ephemeris.DivisionalChartData, lang: Language): CourageAnalysis {
        var s = 50.0; mars?.let { if (AstrologicalConstants.isExalted(Planet.MARS, it.sign)) s += 25.0; if (AstrologicalConstants.isInOwnSign(Planet.MARS, it.sign)) s += 20.0; if (AstrologicalConstants.isDebilitated(Planet.MARS, it.sign)) s -= 20.0; if (it.house == 3) s += 15.0 }
        thirdLord?.let { if (it.house in AstrologicalConstants.KENDRA_HOUSES) s += 10.0; if (it.house in AstrologicalConstants.TRIKONA_HOUSES) s += 10.0 }
        val level = when { s >= 80 -> CourageLevel.EXCEPTIONAL; s >= 65 -> CourageLevel.HIGH; s >= 50 -> CourageLevel.MODERATE; s >= 35 -> CourageLevel.LOW; else -> CourageLevel.VERY_LOW }
        return CourageAnalysis(level, mars?.let { calculatePlanetStrengthInVarga(it) } ?: 0.0, assessInitiativeAbility(mars, lang), assessPhysicalCourage(mars, lang), assessMentalCourage(thirdLord, d3, lang))
    }

    private fun assessInitiativeAbility(mars: PlanetPosition?, lang: Language): String = if (mars?.let { AstrologicalConstants.isExalted(Planet.MARS, it.sign) || AstrologicalConstants.isInOwnSign(Planet.MARS, it.sign) } == true) StringResources.get(StringKeyDivisional.DREKKANA_INITIATIVE_STRONG, lang) else StringResources.get(StringKeyDivisional.DREKKANA_INITIATIVE_MODERATE, lang)

    private fun assessPhysicalCourage(mars: PlanetPosition?, lang: Language): String = when { mars == null -> StringResources.get(StringKeyDivisional.DREKKANA_PHYSICAL_DEPENDS, lang); AstrologicalConstants.isExalted(Planet.MARS, mars.sign) -> StringResources.get(StringKeyDivisional.DREKKANA_PHYSICAL_EXCEPTIONAL, lang); AstrologicalConstants.isInOwnSign(Planet.MARS, mars.sign) -> StringResources.get(StringKeyDivisional.DREKKANA_PHYSICAL_STRONG, lang); AstrologicalConstants.isDebilitated(Planet.MARS, mars.sign) -> StringResources.get(StringKeyDivisional.DREKKANA_PHYSICAL_DEVELOPMENT, lang); else -> StringResources.get(StringKeyDivisional.DREKKANA_PHYSICAL_ADEQUATE, lang) }

    private fun assessMentalCourage(thirdLord: PlanetPosition?, d3: com.astro.vajra.ephemeris.DivisionalChartData, lang: Language): String = when { d3.planetPositions.find { it.planet == Planet.JUPITER }?.house == 3 -> StringResources.get(StringKeyDivisional.DREKKANA_MENTAL_WISDOM, lang); d3.planetPositions.find { it.planet == Planet.MERCURY }?.house == 3 -> StringResources.get(StringKeyDivisional.DREKKANA_MENTAL_INTELLECTUAL, lang); thirdLord?.house in AstrologicalConstants.KENDRA_HOUSES -> StringResources.get(StringKeyDivisional.DREKKANA_MENTAL_STABLE, lang); else -> StringResources.get(StringKeyDivisional.DREKKANA_MENTAL_EXPERIENCE, lang) }

    private fun analyzeCommunication(d3: com.astro.vajra.ephemeris.DivisionalChartData, lang: Language): CommunicationAnalysis {
        val m = d3.planetPositions.find { it.planet == Planet.MERCURY }; val t = d3.planetPositions.filter { it.house == 3 }
        val level = when { m?.let { AstrologicalConstants.isExalted(Planet.MERCURY, it.sign) || AstrologicalConstants.isInOwnSign(Planet.MERCURY, it.sign) } == true && t.any { it.planet == Planet.JUPITER } -> StringResources.get(StringKeyDivisional.DREKKANA_COMM_EXCEPTIONAL, lang); m?.let { AstrologicalConstants.isExalted(Planet.MERCURY, it.sign) || AstrologicalConstants.isInOwnSign(Planet.MERCURY, it.sign) } == true -> StringResources.get(StringKeyDivisional.DREKKANA_COMM_VERY_GOOD, lang); t.any { it.planet in AstrologicalConstants.NATURAL_BENEFICS } -> StringResources.get(StringKeyDivisional.DREKKANA_COMM_GOOD, lang); else -> StringResources.get(StringKeyDivisional.DREKKANA_COMM_AVERAGE, lang) }
        return CommunicationAnalysis(level, assessWritingAbility(m, t, lang), assessSpeakingAbility(m, d3, lang), assessArtisticTalents(d3, lang), m?.let { calculatePlanetStrengthInVarga(it) } ?: 50.0)
    }

    private fun assessWritingAbility(m: PlanetPosition?, t: List<PlanetPosition>, lang: Language): String = when { m?.house == 3 && AstrologicalConstants.isInOwnSign(Planet.MERCURY, m.sign) -> StringResources.get(StringKeyDivisional.DREKKANA_WRITING_EXCEPTIONAL, lang); t.any { it.planet == Planet.JUPITER } -> StringResources.get(StringKeyDivisional.DREKKANA_WRITING_DEPTH, lang); t.any { it.planet == Planet.MERCURY } -> StringResources.get(StringKeyDivisional.DREKKANA_WRITING_GOOD, lang); else -> StringResources.get(StringKeyDivisional.DREKKANA_WRITING_AVERAGE, lang) }

    private fun assessSpeakingAbility(m: PlanetPosition?, d3: com.astro.vajra.ephemeris.DivisionalChartData, lang: Language): String = when { m != null && d3.planetPositions.find { it.planet == Planet.JUPITER }?.house == 2 -> StringResources.get(StringKeyDivisional.DREKKANA_SPEAKING_ELOQUENT, lang); d3.planetPositions.find { it.planet == Planet.SUN }?.house == 3 -> StringResources.get(StringKeyDivisional.DREKKANA_SPEAKING_AUTHORITATIVE, lang); m?.house == 3 -> StringResources.get(StringKeyDivisional.DREKKANA_SPEAKING_QUICK, lang); else -> StringResources.get(StringKeyDivisional.DREKKANA_SPEAKING_NORMAL, lang) }

    private fun assessArtisticTalents(d3: com.astro.vajra.ephemeris.DivisionalChartData, lang: Language): List<String> {
        val res = mutableListOf<String>()
        d3.planetPositions.find { it.planet == Planet.VENUS }?.let { if (it.house == 3 || it.house == 5) res.add(StringResources.get(StringKeyDivisional.DREKKANA_ART_VISUAL, lang)) }
        d3.planetPositions.find { it.planet == Planet.MOON }?.let { if (it.house == 3) res.add(StringResources.get(StringKeyDivisional.DREKKANA_ART_MUSIC, lang)) }
        return res
    }

    private fun analyzeShortJourneys(d3: com.astro.vajra.ephemeris.DivisionalChartData, lang: Language): List<String> {
        val res = mutableListOf<String>(); val t = d3.planetPositions.filter { it.house == 3 }.map { it.planet }
        if (Planet.MOON in t) res.add(StringResources.get(StringKeyDivisional.DREKKANA_JOURNEY_FREQUENT, lang))
        if (Planet.MERCURY in t) res.add(StringResources.get(StringKeyDivisional.DREKKANA_JOURNEY_BUSINESS, lang))
        if (Planet.RAHU in t) res.add(StringResources.get(StringKeyDivisional.DREKKANA_JOURNEY_UNUSUAL, lang))
        return res
    }

    private fun generateDrekkanaRecommendations(c: CourageAnalysis, s: SiblingIndicators, lang: Language): List<String> {
        val res = mutableListOf<String>()
        if (c.overallCourageLevel in listOf(CourageLevel.EXCEPTIONAL, CourageLevel.HIGH)) res.add(StringResources.get(StringKeyDivisional.DREKKANA_REC_CHANNEL_COURAGE, lang))
        else if (c.overallCourageLevel in listOf(CourageLevel.LOW, CourageLevel.VERY_LOW)) { res.add(StringResources.get(StringKeyDivisional.DREKKANA_REC_MARS_ACTIVITIES, lang)); res.add(StringResources.get(StringKeyDivisional.DREKKANA_REC_HANUMAN, lang)) }
        else res.add(StringResources.get(StringKeyDivisional.DREKKANA_REC_BALANCE_CAUTION, lang))
        if (s.relationshipQuality in listOf(RelationshipQuality.CHALLENGING, RelationshipQuality.DIFFICULT)) { res.add(StringResources.get(StringKeyDivisional.DREKKANA_REC_SIBLING_PATIENCE, lang)); res.add(StringResources.get(StringKeyDivisional.DREKKANA_REC_COUNSELING, lang)) }
        return res
    }
}


