package com.astro.vajra.ephemeris.remedy

import com.astro.vajra.core.common.*
import com.astro.vajra.core.model.*
import com.astro.vajra.ephemeris.remedy.RemedyGenerator.getCharityRemedy
import com.astro.vajra.ephemeris.remedy.RemedyGenerator.getColorRemedy
import com.astro.vajra.ephemeris.remedy.RemedyGenerator.getDeityRemedy
import com.astro.vajra.ephemeris.remedy.RemedyGenerator.getFastingRemedy
import com.astro.vajra.ephemeris.remedy.RemedyGenerator.getGandantaRemedy
import com.astro.vajra.ephemeris.remedy.RemedyGenerator.getGemstoneRemedy
import com.astro.vajra.ephemeris.remedy.RemedyGenerator.getLifestyleRemedy
import com.astro.vajra.ephemeris.remedy.RemedyGenerator.getMantraRemedy
import com.astro.vajra.ephemeris.remedy.RemedyGenerator.getNakshatraRemedy
import com.astro.vajra.ephemeris.remedy.RemedyGenerator.getPlanetaryWeekday
import com.astro.vajra.ephemeris.remedy.RemedyGenerator.getRudrakshaRemedy
import com.astro.vajra.ephemeris.remedy.RemedyGenerator.getYantraRemedy
import com.astro.vajra.ephemeris.remedy.RemedyPlanetaryAnalyzer.analyzePlanet

object RemediesCalculator {

    fun calculateRemedies(chart: VedicChart, language: Language = Language.ENGLISH): RemediesResult {
        val ascendantLongitude = chart.ascendant ?: 0.0
        val ascendantSign: ZodiacSign = ZodiacSign.entries[(ascendantLongitude / 30.0).toInt() % 12]
        val moonPosition = chart.planetPositions.find { it.planet == Planet.MOON }
        val moonSign = moonPosition?.sign ?: ZodiacSign.ARIES

        val planetaryAnalyses = Planet.MAIN_PLANETS.map { planet ->
            analyzePlanet(planet, chart, ascendantSign, language)
        }

        val weakestPlanets = planetaryAnalyses
            .filter { it.needsRemedy }
            .sortedWith(compareBy({ -it.strength.severity }, { it.strengthScore }))
            .map { it.planet }

        val allRemedies = mutableListOf<Remedy>()

        planetaryAnalyses.filter { it.needsRemedy }.forEach { analysis ->
            if (analysis.isFunctionalBenefic || analysis.isYogakaraka) {
                getGemstoneRemedy(analysis, language)?.let { allRemedies.add(it) }
            }
            getMantraRemedy(analysis.planet, language)?.let { allRemedies.add(it) }
            getCharityRemedy(analysis.planet, language)?.let { allRemedies.add(it) }
        }

        weakestPlanets.take(3).forEach { planet ->
            getFastingRemedy(planet, language)?.let { allRemedies.add(it) }
        }

        weakestPlanets.take(3).forEach { planet ->
            getColorRemedy(planet, language)?.let { allRemedies.add(it) }
            getLifestyleRemedy(planet, language)?.let { allRemedies.add(it) }
            getRudrakshaRemedy(planet, language)?.let { allRemedies.add(it) }
        }

        weakestPlanets.take(2).forEach { planet ->
            getYantraRemedy(planet, language)?.let { allRemedies.add(it) }
        }

        planetaryAnalyses.filter { it.needsRemedy }.forEach { analysis ->
            getDeityRemedy(analysis.planet, language)?.let { allRemedies.add(it) }
        }

        planetaryAnalyses.forEach { analysis ->
            getNakshatraRemedy(analysis, language)?.let { allRemedies.add(it) }
        }

        planetaryAnalyses.filter { it.isInGandanta && it.needsRemedy }.forEach { analysis ->
            getGandantaRemedy(analysis, language)?.let { allRemedies.add(it) }
        }

        val generalRecommendations = generateGeneralRecommendations(chart, planetaryAnalyses, ascendantSign, language)
        val dashaRemedies = generateDashaRemedies(chart, planetaryAnalyses, language)
        val lifeAreaFocus = categorizeByLifeArea(allRemedies, planetaryAnalyses, language)
        val prioritizedRemedies = prioritizeRemedies(allRemedies, planetaryAnalyses)
        val summary = generateSummary(planetaryAnalyses, weakestPlanets, ascendantSign, language)

        return RemediesResult(
            chart = chart, planetaryAnalyses = planetaryAnalyses, weakestPlanets = weakestPlanets,
            remedies = allRemedies, generalRecommendations = generalRecommendations,
            dashaRemedies = dashaRemedies, lifeAreaFocus = lifeAreaFocus,
            prioritizedRemedies = prioritizedRemedies, summary = summary,
            ascendantSign = ascendantSign, moonSign = moonSign
        )
    }

    private fun generateGeneralRecommendations(
        chart: VedicChart,
        analyses: List<PlanetaryAnalysis>,
        ascendantSign: ZodiacSign,
        language: Language
    ): List<String> {
        val recommendations = mutableListOf<String>()
        val weakCount = analyses.count { it.needsRemedy }
        if (weakCount >= 5) recommendations.add(StringResources.get(StringKeyRemedy.GEN_REC_MULTIPLE, language, weakCount))
        val sunAnalysis = analyses.find { it.planet == Planet.SUN }
        val moonAnalysis = analyses.find { it.planet == Planet.MOON }
        if (sunAnalysis?.needsRemedy == true && moonAnalysis?.needsRemedy == true) recommendations.add(StringResources.get(StringKeyRemedy.GEN_REC_LUMINARIES, language))
        if (moonAnalysis?.needsRemedy == true) recommendations.add(StringResources.get(StringKeyRemedy.GEN_REC_MOON, language))
        analyses.filter { it.isInGandanta }.forEach { recommendations.add(StringResources.get(StringKeyRemedy.GEN_REC_GANDANTA, language, it.planet.getLocalizedName(language))) }
        analyses.filter { it.hasNeechaBhangaRajaYoga }.forEach { recommendations.add(StringResources.get(StringKeyRemedy.GEN_REC_NEECHA_BHANGA, language, it.planet.getLocalizedName(language))) }
        analyses.filter { it.isYogakaraka }.forEach { recommendations.add(StringResources.get(StringKeyRemedy.GEN_REC_YOGAKARAKA, language, it.planet.getLocalizedName(language))) }
        recommendations.add(StringResources.get(StringKeyRemedy.GEN_REC_MEDITATION, language))
        recommendations.add(StringResources.get(StringKeyRemedy.GEN_REC_CLEAN, language))
        recommendations.add(StringResources.get(StringKeyRemedy.GEN_REC_ELDERS, language))
        recommendations.add(StringResources.get(StringKeyRemedy.GEN_REC_CHARITY, language))
        recommendations.add(StringResources.get(StringKeyRemedy.GEN_REC_DIET, language))
        recommendations.add(StringResources.get(StringKeyRemedy.GEN_REC_DREAMS, language))
        val ketuAnalysis = analyses.find { it.planet == Planet.KETU }
        if (ketuAnalysis?.housePosition == 12 || ketuAnalysis?.housePosition == 4) recommendations.add(StringResources.get(StringKeyRemedy.GEN_REC_SPIRITUAL, language))
        return recommendations
    }

    private fun generateDashaRemedies(
        chart: VedicChart,
        analyses: List<PlanetaryAnalysis>,
        language: Language
    ): List<Remedy> {
        val dashaRemedies = mutableListOf<Remedy>()
        dashaRemedies.add(Remedy(
            category = RemedyCategory.LIFESTYLE, title = StringResources.get(StringKeyRemedy.DASHA_AWARENESS_TITLE, language),
            description = StringResources.get(StringKeyRemedy.DASHA_AWARENESS_DESC, language),
            method = buildString {
                appendLine(StringResources.get(StringKeyRemedy.DASHA_METHOD_TITLE, language))
                appendLine(StringResources.get(StringKeyRemedy.DASHA_METHOD_1, language))
                appendLine(StringResources.get(StringKeyRemedy.DASHA_METHOD_2, language))
                appendLine(StringResources.get(StringKeyRemedy.DASHA_METHOD_3, language))
                appendLine(StringResources.get(StringKeyRemedy.DASHA_METHOD_4, language))
                appendLine(StringResources.get(StringKeyRemedy.DASHA_METHOD_5, language))
            },
            timing = StringResources.get(StringKeyRemedy.DASHA_TIMING, language),
            duration = StringResources.get(StringKeyRemedy.DASHA_DURATION, language),
            planet = null, priority = RemedyPriority.RECOMMENDED,
            benefits = listOf(StringResources.get(StringKeyRemedy.DASHA_BENEFIT_MAX, language), StringResources.get(StringKeyRemedy.DASHA_BENEFIT_MIN, language), StringResources.get(StringKeyRemedy.DASHA_BENEFIT_TIME, language)),
            cautions = listOf(StringResources.get(StringKeyRemedy.DASHA_CAUTION_CONSULT, language), StringResources.get(StringKeyRemedy.DASHA_CAUTION_TRANSIT, language))
        ))
        analyses.filter { it.strength.severity >= 4 }.forEach { analysis ->
            val pName = analysis.planet.getLocalizedName(language)
            val weekday = StringResources.get(StringKeyRemedy.valueOf("WEEKDAY_${getPlanetaryWeekday(analysis.planet).uppercase()}"), language)
            dashaRemedies.add(Remedy(
                category = RemedyCategory.MANTRA, title = StringResources.get(StringKeyRemedy.DASHA_SPECIFIC_TITLE, language, pName),
                description = StringResources.get(StringKeyRemedy.DASHA_SPECIFIC_DESC, language, pName),
                method = StringResources.get(StringKeyRemedy.DASHA_SPECIFIC_METHOD, language, pName, analysis.strength.getLocalizedName(language), weekday, pName, weekday),
                timing = StringResources.get(StringKeyRemedy.DASHA_TIMING, language),
                duration = StringResources.get(StringKeyRemedy.DASHA_DURATION, language),
                planet = analysis.planet, priority = RemedyPriority.ESSENTIAL,
                benefits = listOf(StringResources.get(StringKeyRemedy.DASHA_SPECIFIC_BENEFIT_REDUCE, language), StringResources.get(StringKeyRemedy.DASHA_SPECIFIC_BENEFIT_TRANSFORM, language)),
                cautions = listOf(StringResources.get(StringKeyRemedy.DASHA_SPECIFIC_CAUTION, language)),
                mantraText = RemedyConstants.planetaryMantras[analysis.planet]?.beejMantra,
                mantraSanskrit = RemedyConstants.planetaryMantras[analysis.planet]?.beejMantraSanskrit
            ))
        }
        return dashaRemedies
    }

    private fun categorizeByLifeArea(
        remedies: List<Remedy>,
        analyses: List<PlanetaryAnalysis>,
        language: Language
    ): Map<LifeArea, List<Remedy>> {
        return mapOf(
            LifeArea.CAREER to remedies.filter { it.planet in listOf(Planet.SUN, Planet.SATURN, Planet.JUPITER, Planet.MARS) },
            LifeArea.RELATIONSHIPS to remedies.filter { it.planet in listOf(Planet.VENUS, Planet.MOON, Planet.JUPITER) },
            LifeArea.HEALTH to remedies.filter { it.planet in listOf(Planet.SUN, Planet.MOON, Planet.MARS, Planet.SATURN) },
            LifeArea.FINANCE to remedies.filter { it.planet in listOf(Planet.JUPITER, Planet.VENUS, Planet.MERCURY, Planet.MOON) },
            LifeArea.EDUCATION to remedies.filter { it.planet in listOf(Planet.MERCURY, Planet.JUPITER) },
            LifeArea.SPIRITUAL to remedies.filter { it.planet in listOf(Planet.KETU, Planet.JUPITER, Planet.MOON, Planet.SUN) },
            LifeArea.PROPERTY to remedies.filter { it.planet in listOf(Planet.MARS, Planet.SATURN, Planet.MOON) },
            LifeArea.FOREIGN to remedies.filter { it.planet in listOf(Planet.RAHU, Planet.KETU, Planet.MOON) }
        ).filterValues { it.isNotEmpty() }
    }

    private fun prioritizeRemedies(
        remedies: List<Remedy>,
        analyses: List<PlanetaryAnalysis>
    ): List<Remedy> {
        return remedies.sortedWith(
            compareBy<Remedy> { it.priority.level }
                .thenByDescending { remedy ->
                    val analysis = analyses.find { it.planet == remedy.planet }
                    analysis?.strength?.severity ?: 0
                }
                .thenBy { remedy ->
                    when (remedy.category) {
                        RemedyCategory.MANTRA -> 1
                        RemedyCategory.CHARITY -> 2
                        RemedyCategory.DEITY -> 3
                        RemedyCategory.FASTING -> 4
                        RemedyCategory.LIFESTYLE -> 5
                        RemedyCategory.RUDRAKSHA -> 6
                        RemedyCategory.COLOR -> 7
                        RemedyCategory.GEMSTONE -> 8
                        RemedyCategory.YANTRA -> 9
                        RemedyCategory.METAL -> 10
                    }
                }
        )
    }

    private fun generateSummary(
        analyses: List<PlanetaryAnalysis>,
        weakPlanets: List<Planet>,
        ascendantSign: ZodiacSign,
        language: Language
    ): String {
        return buildString {
            appendLine(StringResources.get(StringKeyRemedy.SUMMARY_TITLE, language, ascendantSign.getLocalizedName(language)))
            appendLine()
            if (weakPlanets.isEmpty()) {
                appendLine(StringResources.get(StringKeyRemedy.SUMMARY_FAVORABLE, language))
                appendLine()
                appendLine(StringResources.get(StringKeyRemedy.SUMMARY_MAINTENANCE, language))
                appendLine(StringResources.get(StringKeyRemedy.SUMMARY_MAINTENANCE_1, language))
                appendLine(StringResources.get(StringKeyRemedy.SUMMARY_MAINTENANCE_2, language))
                appendLine(StringResources.get(StringKeyRemedy.SUMMARY_MAINTENANCE_3, language))
                appendLine(StringResources.get(StringKeyRemedy.SUMMARY_MAINTENANCE_4, language))
            } else {
                val names = weakPlanets.take(3).joinToString { it.getLocalizedName(language) }
                appendLine(StringResources.get(StringKeyRemedy.SUMMARY_FOCUS, language, names))
                appendLine()
                appendLine(StringResources.get(StringKeyRemedy.SUMMARY_PRIORITY, language))
                appendLine()
                appendLine(StringResources.get(StringKeyRemedy.SUMMARY_GUIDANCE_1, language))
                appendLine()
                appendLine(StringResources.get(StringKeyRemedy.SUMMARY_GUIDANCE_2, language))
                appendLine()
                appendLine(StringResources.get(StringKeyRemedy.SUMMARY_GUIDANCE_3, language))
                appendLine()
                appendLine(StringResources.get(StringKeyRemedy.SUMMARY_GUIDANCE_4, language))
                appendLine()
                appendLine(StringResources.get(StringKeyRemedy.SUMMARY_GUIDANCE_5, language))
                appendLine()
                val yogakarakaPlanets = analyses.filter { it.isYogakaraka }
                if (yogakarakaPlanets.isNotEmpty()) {
                    appendLine(StringResources.get(StringKeyRemedy.SUMMARY_YOGAKARAKA, language, yogakarakaPlanets.map { it.planet.getLocalizedName(language) }.joinToString(), if (yogakarakaPlanets.size == 1) StringResources.get(StringKeyRemedy.SUMMARY_THIS_PLANET, language) else StringResources.get(StringKeyRemedy.SUMMARY_THESE_PLANETS, language)))
                    appendLine()
                }
                appendLine(StringResources.get(StringKeyRemedy.SUMMARY_GUIDANCE_TITLE, language))
                appendLine(StringResources.get(StringKeyRemedy.SUMMARY_GUIDANCE_POINT_1, language))
                appendLine(StringResources.get(StringKeyRemedy.SUMMARY_GUIDANCE_POINT_2, language))
                appendLine(StringResources.get(StringKeyRemedy.SUMMARY_GUIDANCE_POINT_3, language))
                appendLine(StringResources.get(StringKeyRemedy.SUMMARY_GUIDANCE_POINT_4, language))
                appendLine(StringResources.get(StringKeyRemedy.SUMMARY_GUIDANCE_POINT_5, language))
            }
        }
    }
}
