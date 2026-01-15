package com.astro.storm.ephemeris.varshaphala

import com.astro.storm.core.common.Language
import com.astro.storm.core.common.StringKeyAnalysis
import com.astro.storm.core.common.StringResources
import com.astro.storm.core.model.Planet
import com.astro.storm.ephemeris.varshaphala.VarshaphalaConstants.MUDDA_DASHA_PLANETS
import com.astro.storm.ephemeris.varshaphala.VarshaphalaHelpers.calculateWholeSignHouse
import com.astro.storm.ephemeris.varshaphala.VarshaphalaHelpers.evaluatePlanetStrengthDescription
import com.astro.storm.ephemeris.varshaphala.VarshaphalaHelpers.getStandardZodiacIndex
import com.astro.storm.ephemeris.varshaphala.VarshaphalaHelpers.getZodiacSignFromLongitude
import com.astro.storm.ephemeris.varshaphala.VarshaphalaHelpers.normalizeAngle

object SahamCalculator {

    fun calculateSahams(chart: SolarReturnChart, language: Language): List<SahamResult> {
        val sahams = mutableListOf<SahamResult>()
        val isDayBirth = chart.isDayBirth
        val sunLong = chart.planetPositions[Planet.SUN]?.longitude ?: 0.0
        val moonLong = chart.planetPositions[Planet.MOON]?.longitude ?: 0.0
        val marsLong = chart.planetPositions[Planet.MARS]?.longitude ?: 0.0
        val mercuryLong = chart.planetPositions[Planet.MERCURY]?.longitude ?: 0.0
        val jupiterLong = chart.planetPositions[Planet.JUPITER]?.longitude ?: 0.0
        val venusLong = chart.planetPositions[Planet.VENUS]?.longitude ?: 0.0
        val saturnLong = chart.planetPositions[Planet.SATURN]?.longitude ?: 0.0
        val ascLong = getStandardZodiacIndex(chart.ascendant) * 30.0 + chart.ascendantDegree

        val sahamFormulas = listOf(
            Triple(SahamType.PUNYA, { if (isDayBirth) moonLong + ascLong - sunLong else sunLong + ascLong - moonLong }, "Moon + Asc - Sun"),
            Triple(SahamType.VIDYA, { if (isDayBirth) mercuryLong + ascLong - sunLong else sunLong + ascLong - mercuryLong }, "Mercury + Asc - Sun"),
            Triple(SahamType.YASHAS, { if (isDayBirth) jupiterLong + ascLong - sunLong else sunLong + ascLong - jupiterLong }, "Jupiter + Asc - Sun"),
            Triple(SahamType.MITRA, { if (isDayBirth) moonLong + ascLong - mercuryLong else mercuryLong + ascLong - moonLong }, "Moon + Asc - Mercury"),
            Triple(SahamType.DHANA, { if (isDayBirth) jupiterLong + ascLong - moonLong else moonLong + ascLong - jupiterLong }, "Jupiter + Asc - Moon"),
            Triple(SahamType.KARMA, { if (isDayBirth) saturnLong + ascLong - sunLong else sunLong + ascLong - saturnLong }, "Saturn + Asc - Sun"),
            Triple(SahamType.VIVAHA, { if (isDayBirth) venusLong + ascLong - saturnLong else saturnLong + ascLong - venusLong }, "Venus + Asc - Saturn"),
            Triple(SahamType.PUTRA, { if (isDayBirth) jupiterLong + ascLong - moonLong else moonLong + ascLong - jupiterLong }, "Jupiter + Asc - Moon"),
            Triple(SahamType.PITRI, { if (isDayBirth) saturnLong + ascLong - sunLong else sunLong + ascLong - saturnLong }, "Saturn + Asc - Sun"),
            Triple(SahamType.MATRI, { if (isDayBirth) moonLong + ascLong - venusLong else venusLong + ascLong - moonLong }, "Moon + Asc - Venus"),
            Triple(SahamType.SAMARTHA, { if (isDayBirth) marsLong + ascLong - saturnLong else saturnLong + ascLong - marsLong }, "Mars + Asc - Saturn"),
            Triple(SahamType.ASHA, { if (isDayBirth) saturnLong + ascLong - venusLong else venusLong + ascLong - saturnLong }, "Saturn + Asc - Venus"),
            Triple(SahamType.ROGA, { if (isDayBirth) saturnLong + ascLong - marsLong else marsLong + ascLong - saturnLong }, "Saturn + Asc - Mars"),
            Triple(SahamType.RAJA, { if (isDayBirth) sunLong + ascLong - saturnLong else saturnLong + ascLong - sunLong }, "Sun + Asc - Saturn"),
            Triple(SahamType.MRITYU, { if (isDayBirth) saturnLong + ascLong - moonLong else moonLong + ascLong - saturnLong }, "Saturn + Asc - Moon"),
            Triple(SahamType.BHRATRI, { if (isDayBirth) jupiterLong + ascLong - saturnLong else saturnLong + ascLong - jupiterLong }, "Jupiter + Asc - Saturn"),
            Triple(SahamType.MAHATMYA, { if (isDayBirth) jupiterLong + ascLong - moonLong else moonLong + ascLong - jupiterLong }, "Jupiter + Asc - Moon"),
            Triple(SahamType.KARYASIDDHI, { if (isDayBirth) saturnLong + ascLong - sunLong else sunLong + ascLong - saturnLong }, "Saturn + Asc - Sun")
        )

        for ((type, formula, formulaStr) in sahamFormulas) {
            try {
                val longitude = normalizeAngle(formula())
                val sign = getZodiacSignFromLongitude(longitude)
                val house = calculateWholeSignHouse(longitude, ascLong)
                val lord = sign.ruler
                val lordPosition = chart.planetPositions[lord]
                val lordHouse = lordPosition?.house ?: 1
                val lordStrength = evaluatePlanetStrengthDescription(lord, chart, language)
                val isActive = isSahamActive(lord, chart, house)
                sahams.add(SahamResult(
                    type = type, name = type.getDisplayName(Language.ENGLISH), sanskritName = type.getSanskritName(Language.ENGLISH),
                    formula = formulaStr, longitude = longitude, sign = sign, house = house, degree = longitude % 30.0,
                    lord = lord, lordHouse = lordHouse, lordStrength = lordStrength, interpretation = generateSahamInterpretation(type, sign, house, lord, lordHouse, lordStrength, language),
                    isActive = isActive, activationPeriods = if (lord in MUDDA_DASHA_PLANETS) listOf("${lord.displayName} Mudda Dasha") else emptyList()
                ))
            } catch (e: Exception) {}
        }
        return sahams.sortedByDescending { it.isActive }
    }

    private fun isSahamActive(lord: Planet, chart: SolarReturnChart, house: Int): Boolean {
        val pos = chart.planetPositions[lord] ?: return false
        val strength = evaluatePlanetStrengthDescription(lord, chart, Language.ENGLISH)
        return ((strength in listOf("Exalted", "Strong", "Angular") && house in listOf(1, 2, 4, 5, 7, 9, 10, 11)) || (pos.house in listOf(1, 4, 5, 7, 9, 10, 11) && !pos.isRetrograde))
    }

    private fun generateSahamInterpretation(type: SahamType, sign: com.astro.storm.core.model.ZodiacSign, house: Int, lord: Planet, lordHouse: Int, lordStrength: String, language: Language): String {
        val lordQuality = when (lordStrength) {
            StringResources.get(StringKeyAnalysis.VARSHA_STRENGTH_EXALTED, language), StringResources.get(StringKeyAnalysis.VARSHA_STRENGTH_STRONG, language) -> StringResources.get(StringKeyAnalysis.VARSHA_TONE_EXCELLENT, language)
            StringResources.get(StringKeyAnalysis.VARSHA_STRENGTH_MODERATE, language), StringResources.get(StringKeyAnalysis.VARSHA_STRENGTH_ANGULAR, language) -> StringResources.get(StringKeyAnalysis.VARSHA_TONE_FAVORABLE, language)
            StringResources.get(StringKeyAnalysis.VARSHA_STRENGTH_DEBILITATED, language) -> StringResources.get(StringKeyAnalysis.VARSHA_TONE_CHALLENGING, language)
            else -> StringResources.get(StringKeyAnalysis.VARSHA_TONE_BALANCED, language)
        }
        return StringResources.get(StringKeyAnalysis.VARSHA_SAHAM_RELATES, language, type.getDisplayName(language), sign.getLocalizedName(language), house, type.getDescription(language).lowercase()) +
                " " + StringResources.get(StringKeyAnalysis.VARSHA_SAHAM_LORD_SUPPORT, language, lord.getLocalizedName(language), lordHouse, lordQuality)
    }
}
