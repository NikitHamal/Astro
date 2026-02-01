package com.astro.storm.ephemeris.varshaphala

import com.astro.storm.core.common.Language
import com.astro.storm.core.common.StringKeyAnalysis
import com.astro.storm.core.common.StringResources
import com.astro.storm.core.model.Planet
import com.astro.storm.core.model.ZodiacSign
import com.astro.storm.ephemeris.varshaphala.VarshaphalaHelpers.evaluatePlanetStrengthDescription
import com.astro.storm.ephemeris.varshaphala.VarshaphalaHelpers.getHouseSignificance
import com.astro.storm.ephemeris.varshaphala.VarshaphalaHelpers.getStandardZodiacIndex
import java.time.LocalDateTime

object VarshaphalaInterpretationGenerator {

    fun generateHousePredictions(chart: SolarReturnChart, muntha: MunthaResult, yearLord: Planet, language: Language): List<HousePrediction> {
        val predictions = mutableListOf<HousePrediction>()
        val ascIndex = getStandardZodiacIndex(chart.ascendant)
        for (house in 1..12) {
            val sign = VarshaphalaConstants.STANDARD_ZODIAC_SIGNS[(ascIndex + house - 1) % 12]
            val lord = sign.ruler
            val lordPos = chart.planetPositions[lord]?.house ?: 1
            val planetsInHouse = chart.planetPositions.filter { (_, pos) -> pos.house == house }.keys.toList()
            predictions.add(HousePrediction(
                house, sign, lord, lordPos, planetsInHouse,
                calculateHouseStrength(house, lord, lordPos, planetsInHouse, chart, muntha, yearLord, language),
                getHouseKeywords(house, language),
                generateHousePrediction(house, sign, lord, lordPos, planetsInHouse, chart, muntha, yearLord, language),
                calculateHouseRating(house, lord, lordPos, planetsInHouse, chart, muntha, yearLord, language),
                generateSpecificEvents(house, lord, planetsInHouse, chart, language)
            ))
        }
        return predictions
    }

    private fun calculateHouseStrength(house: Int, lord: Planet, lordPos: Int, planets: List<Planet>, chart: SolarReturnChart, muntha: MunthaResult, yearLord: Planet, language: Language): String {
        var score = 0
        if (lordPos in listOf(1, 2, 4, 5, 7, 9, 10, 11)) score += 2
        val s = evaluatePlanetStrengthDescription(lord, chart, language)
        when (s) {
            StringResources.get(StringKeyGeneralPart12.VARSHA_STRENGTH_EXALTED, language) -> score += 3
            StringResources.get(StringKeyGeneralPart12.VARSHA_STRENGTH_STRONG, language) -> score += 2
            StringResources.get(StringKeyGeneralPart12.VARSHA_STRENGTH_ANGULAR, language) -> score += 1
            StringResources.get(StringKeyGeneralPart12.VARSHA_STRENGTH_DEBILITATED, language) -> score -= 2
        }
        planets.forEach { if (it in listOf(Planet.JUPITER, Planet.VENUS, Planet.MOON)) score += 1 else if (it in listOf(Planet.SATURN, Planet.MARS, Planet.RAHU, Planet.KETU)) score -= 1 }
        if (muntha.house == house) score += 2
        if (yearLord == lord) score += 1
        return when {
            score >= 5 -> StringResources.get(StringKeyGeneralPart12.VARSHA_STRENGTH_EXCELLENT, language)
            score >= 3 -> StringResources.get(StringKeyGeneralPart12.VARSHA_STRENGTH_STRONG, language)
            score >= 1 -> StringResources.get(StringKeyGeneralPart12.VARSHA_STRENGTH_MODERATE, language)
            score >= -1 -> StringResources.get(StringKeyGeneralPart12.VARSHA_STRENGTH_WEAK, language)
            else -> StringResources.get(StringKeyGeneralPart12.VARSHA_STRENGTH_CHALLENGED, language)
        }
    }

    private fun getHouseKeywords(house: Int, language: Language): List<String> {
        val keys = when (house) {
            1 -> listOf(StringKeyGeneralPart6.KEYWORD_SELF, StringKeyGeneralPart6.KEYWORD_PERSONALITY, StringKeyGeneralPart6.KEYWORD_HEALTH, StringKeyGeneralPart6.KEYWORD_APPEARANCE, StringKeyGeneralPart6.KEYWORD_NEW_BEGINNINGS)
            2 -> listOf(StringKeyGeneralPart6.KEYWORD_WEALTH, StringKeyGeneralPart6.KEYWORD_FAMILY, StringKeyGeneralPart6.KEYWORD_SPEECH, StringKeyGeneralPart6.KEYWORD_VALUES, StringKeyGeneralPart6.KEYWORD_FOOD)
            3 -> listOf(StringKeyGeneralPart6.KEYWORD_SIBLINGS, StringKeyGeneralPart6.KEYWORD_COURAGE, StringKeyGeneralPart6.KEYWORD_COMMUNICATION, StringKeyGeneralPart6.KEYWORD_SHORT_TRAVEL, StringKeyGeneralPart6.KEYWORD_SKILLS)
            4 -> listOf(StringKeyGeneralPart6.KEYWORD_HOME, StringKeyGeneralPart6.KEYWORD_MOTHER, StringKeyGeneralPart6.KEYWORD_PROPERTY, StringKeyGeneralPart6.KEYWORD_VEHICLES, StringKeyGeneralPart6.KEYWORD_INNER_PEACE)
            5 -> listOf(StringKeyGeneralPart6.KEYWORD_CHILDREN, StringKeyGeneralPart6.KEYWORD_INTELLIGENCE, StringKeyGeneralPart6.KEYWORD_ROMANCE, StringKeyGeneralPart6.KEYWORD_CREATIVITY, StringKeyGeneralPart6.KEYWORD_INVESTMENTS)
            6 -> listOf(StringKeyGeneralPart6.KEYWORD_ENEMIES, StringKeyGeneralPart6.KEYWORD_HEALTH_ISSUES, StringKeyGeneralPart6.KEYWORD_SERVICE, StringKeyGeneralPart6.KEYWORD_DEBTS, StringKeyGeneralPart6.KEYWORD_COMPETITION)
            7 -> listOf(StringKeyGeneralPart6.KEYWORD_MARRIAGE, StringKeyGeneralPart6.KEYWORD_PARTNERSHIP, StringKeyGeneralPart6.KEYWORD_BUSINESS, StringKeyGeneralPart6.KEYWORD_PUBLIC_DEALINGS, StringKeyGeneralPart6.KEYWORD_CONTRACTS)
            8 -> listOf(StringKeyGeneralPart6.KEYWORD_LONGEVITY, StringKeyGeneralPart6.KEYWORD_TRANSFORMATION, StringKeyGeneralPart6.KEYWORD_RESEARCH, StringKeyGeneralPart6.KEYWORD_INHERITANCE, StringKeyGeneralPart6.KEYWORD_HIDDEN_MATTERS)
            9 -> listOf(StringKeyGeneralPart6.KEYWORD_FORTUNE, StringKeyGeneralPart6.KEYWORD_FATHER, StringKeyGeneralPart6.KEYWORD_RELIGION, StringKeyGeneralPart6.KEYWORD_HIGHER_EDUCATION, StringKeyGeneralPart6.KEYWORD_LONG_TRAVEL)
            10 -> listOf(StringKeyGeneralPart6.KEYWORD_CAREER, StringKeyGeneralPart6.KEYWORD_STATUS, StringKeyGeneralPart6.KEYWORD_AUTHORITY, StringKeyGeneralPart6.KEYWORD_GOVERNMENT, StringKeyGeneralPart6.KEYWORD_FAME)
            11 -> listOf(StringKeyGeneralPart6.KEYWORD_GAINS, StringKeyGeneralPart6.KEYWORD_INCOME, StringKeyGeneralPart6.KEYWORD_FRIENDS, StringKeyGeneralPart6.KEYWORD_ELDER_SIBLINGS, StringKeyGeneralPart6.KEYWORD_ASPIRATIONS)
            12 -> listOf(StringKeyGeneralPart6.KEYWORD_LOSSES, StringKeyGeneralPart6.KEYWORD_EXPENSES, StringKeyGeneralPart6.KEYWORD_SPIRITUALITY, StringKeyGeneralPart6.KEYWORD_FOREIGN_LANDS, StringKeyGeneralPart6.KEYWORD_LIBERATION)
            else -> listOf(StringKeyGeneralPart6.KEYWORD_GENERAL)
        }
        return keys.map { StringResources.get(it, language) }
    }

    private fun generateHousePrediction(house: Int, sign: ZodiacSign, lord: Planet, lordPos: Int, planets: List<Planet>, chart: SolarReturnChart, muntha: MunthaResult, yearLord: Planet, language: Language): String {
        val s = evaluatePlanetStrengthDescription(lord, chart, language)
        val lordAnalysis = StringResources.get(StringKeyGeneralPart12.VARSHA_HOUSE_LORD_POSITION, language, lord.getLocalizedName(language), lordPos) + " " +
                when (s) {
                    StringResources.get(StringKeyGeneralPart12.VARSHA_STRENGTH_EXALTED, language) -> StringResources.get(StringKeyGeneralPart12.VARSHA_HOUSE_LORD_EXCELLENT, language)
                    StringResources.get(StringKeyGeneralPart12.VARSHA_STRENGTH_STRONG, language) -> StringResources.get(StringKeyGeneralPart12.VARSHA_HOUSE_LORD_STRONG, language)
                    StringResources.get(StringKeyGeneralPart12.VARSHA_STRENGTH_MODERATE, language) -> StringResources.get(StringKeyGeneralPart12.VARSHA_HOUSE_LORD_MODERATE, language)
                    StringResources.get(StringKeyGeneralPart12.VARSHA_STRENGTH_DEBILITATED, language) -> StringResources.get(StringKeyGeneralPart12.VARSHA_HOUSE_LORD_CHALLENGED, language)
                    else -> StringResources.get(StringKeyGeneralPart12.VARSHA_HOUSE_LORD_VARIABLE, language)
                }
        val influence = if (planets.isNotEmpty()) {
            val benefics = planets.filter { it in listOf(Planet.JUPITER, Planet.VENUS, Planet.MOON) }
            val malefics = planets.filter { it in listOf(Planet.SATURN, Planet.MARS, Planet.RAHU, Planet.KETU) }
            when { benefics.isNotEmpty() && malefics.isEmpty() -> " " + StringResources.get(StringKeyGeneralPart12.VARSHA_HOUSE_BENEFICS_ENHANCE, language, benefics.joinToString { it.getLocalizedName(language) })
                malefics.isNotEmpty() && benefics.isEmpty() -> " " + StringResources.get(StringKeyGeneralPart12.VARSHA_HOUSE_MALEFICS_CHALLENGE, language, malefics.joinToString { it.getLocalizedName(language) })
                else -> " " + StringResources.get(StringKeyGeneralPart12.VARSHA_HOUSE_MIXED_INF, language, planets.joinToString { it.getLocalizedName(language) }) }
        } else " " + StringResources.get(StringKeyGeneralPart12.VARSHA_HOUSE_LORD_DEPENDENT, language)
        val special = (if (muntha.house == house) " " + StringResources.get(StringKeyGeneralPart12.VARSHA_HOUSE_MUNTHA_EMPHASIS, language) else "") +
                (if (yearLord == lord) " " + StringResources.get(StringKeyGeneralPart12.VARSHA_HOUSE_YEARLORD_RULE, language) else "")
        return StringResources.get(StringKeyGeneralPart12.VARSHA_HOUSE_PREDICTION_FORMAT, language, house, sign.getLocalizedName(language), getHouseSignificance(house, language), lordAnalysis, influence, special).trim()
    }

    private fun calculateHouseRating(house: Int, lord: Planet, lordPos: Int, planets: List<Planet>, chart: SolarReturnChart, muntha: MunthaResult, yearLord: Planet, language: Language): Float {
        var rating = 3.0f
        if (lordPos in listOf(1, 2, 4, 5, 7, 9, 10, 11)) rating += 0.5f
        val s = evaluatePlanetStrengthDescription(lord, chart, language)
        rating += when (s) {
            StringResources.get(StringKeyGeneralPart12.VARSHA_STRENGTH_EXALTED, language) -> 1.0f
            StringResources.get(StringKeyGeneralPart12.VARSHA_STRENGTH_STRONG, language) -> 0.7f
            StringResources.get(StringKeyGeneralPart12.VARSHA_STRENGTH_ANGULAR, language) -> 0.3f
            StringResources.get(StringKeyGeneralPart12.VARSHA_STRENGTH_DEBILITATED, language) -> -0.8f
            else -> 0.0f
        }
        planets.forEach { when (it) { Planet.JUPITER -> rating += 0.5f; Planet.VENUS -> rating += 0.4f; Planet.MOON -> rating += 0.2f; Planet.SATURN -> rating -= 0.3f; Planet.MARS -> rating -= 0.2f; Planet.RAHU, Planet.KETU -> rating -= 0.2f; else -> {} } }
        if (muntha.house == house) rating += 0.5f
        if (yearLord == lord) rating += 0.3f
        return rating.coerceIn(1.0f, 5.0f)
    }

    private fun generateSpecificEvents(house: Int, lord: Planet, planets: List<Planet>, chart: SolarReturnChart, language: Language): List<String> {
        val events = mutableListOf<String>()
        val s = evaluatePlanetStrengthDescription(lord, chart, language)
        val isLordStrong = s in listOf(StringResources.get(StringKeyGeneralPart12.VARSHA_STRENGTH_EXALTED, language), StringResources.get(StringKeyGeneralPart12.VARSHA_STRENGTH_STRONG, language))
        when (house) {
            1 -> { if (isLordStrong) { events.add(StringResources.get(StringKeyGeneralPart12.VARSHA_EVENT_VITALITY, language)); events.add(StringResources.get(StringKeyGeneralPart12.VARSHA_EVENT_NEW_VENTURES, language)) }
                if (Planet.JUPITER in planets) events.add(StringResources.get(StringKeyGeneralPart12.VARSHA_EVENT_SPIRITUAL_GROWTH, language))
                if (Planet.MARS in planets) events.add(StringResources.get(StringKeyGeneralPart12.VARSHA_EVENT_INCREASED_ENERGY, language)) }
            2 -> { if (isLordStrong) { events.add(StringResources.get(StringKeyGeneralPart12.VARSHA_EVENT_FINANCIAL_GAINS, language)); events.add(StringResources.get(StringKeyGeneralPart12.VARSHA_EVENT_FAMILY_RELATIONS, language)) }
                if (Planet.VENUS in planets) events.add(StringResources.get(StringKeyGeneralPart12.VARSHA_EVENT_LUXURY_ACQUISITION, language)) }
            5 -> { if (isLordStrong) { events.add(StringResources.get(StringKeyGeneralPart12.VARSHA_EVENT_CREATIVE_SUCCESS, language)); events.add(StringResources.get(StringKeyGeneralPart12.VARSHA_EVENT_CHILDREN_MATTERS, language)) }
                if (Planet.JUPITER in planets) events.add(StringResources.get(StringKeyGeneralPart12.VARSHA_EVENT_ACADEMIC_SUCCESS, language))
                if (Planet.VENUS in planets) events.add(StringResources.get(StringKeyGeneralPart12.VARSHA_EVENT_ROMANTIC_HAPPINESS, language)) }
            7 -> { if (isLordStrong) { events.add(StringResources.get(StringKeyGeneralPart12.VARSHA_EVENT_PARTNERSHIP_STRENGTH, language)); events.add(StringResources.get(StringKeyGeneralPart12.VARSHA_EVENT_MARRIAGE_FAVORABLE, language)) }
                if (Planet.VENUS in planets) events.add(StringResources.get(StringKeyGeneralPart12.VARSHA_EVENT_ROMANTIC_FULFILLMENT, language)) }
            10 -> { if (isLordStrong) { events.add(StringResources.get(StringKeyGeneralPart12.VARSHA_EVENT_CAREER_ADVANCEMENT, language)); events.add(StringResources.get(StringKeyGeneralPart12.VARSHA_EVENT_AUTHORITY_RECOGNITION, language)) }
                if (Planet.SUN in planets) events.add(StringResources.get(StringKeyGeneralPart12.VARSHA_EVENT_GOVERNMENT_FAVOR, language)) }
            11 -> { if (isLordStrong) { events.add(StringResources.get(StringKeyGeneralPart12.VARSHA_EVENT_DESIRE_FULFILLMENT, language)); events.add(StringResources.get(StringKeyGeneralPart12.VARSHA_EVENT_MULTIPLE_GAINS, language)) } }
        }
        return events.take(4)
    }

    fun identifyMajorThemes(chart: SolarReturnChart, muntha: MunthaResult, yearLord: Planet, housePredictions: List<HousePrediction>, triPataki: TriPatakiChakra, tajikaAspects: List<TajikaAspectResult>, language: Language): List<String> {
        val themes = mutableListOf<String>()
        val yearLordHouse = chart.planetPositions[yearLord]?.house ?: 1
        themes.add(StringResources.get(StringKeyGeneralPart12.VARSHA_THEME_YEARLORD, language, yearLord.getLocalizedName(language), getHouseSignificance(yearLordHouse, language)))
        themes.add(StringResources.get(StringKeyGeneralPart12.VARSHA_THEME_MUNTHA, language, muntha.house, muntha.themes.firstOrNull() ?: StringResources.get(StringKeyVarshaphala.MUNTHA_GENERAL_GROWTH, language)))
        themes.add(StringResources.get(StringKeyGeneralPart12.VARSHA_THEME_TRIPATAKI, language, triPataki.dominantInfluence))
        housePredictions.filter { it.strength in listOf(StringResources.get(StringKeyGeneralPart12.VARSHA_STRENGTH_EXCELLENT, language), StringResources.get(StringKeyGeneralPart12.VARSHA_STRENGTH_STRONG, language)) }
            .sortedByDescending { it.rating }.take(2).forEach { themes.add(StringResources.get(StringKeyGeneralPart12.VARSHA_THEME_FAVORABLE, language, getHouseSignificance(it.house, language), it.house)) }
        val pos = tajikaAspects.count { it.type.isPositive }; val tot = tajikaAspects.size
        if (tot > 0) themes.add(StringResources.get(StringKeyGeneralPart12.VARSHA_THEME_TAJIKA, language, if (pos > tot / 2) StringResources.get(StringKeyGeneralPart12.VARSHA_TONE_SUPPORTIVE, language) else StringResources.get(StringKeyGeneralPart12.VARSHA_TONE_CHALLENGING, language), pos, tot))
        return themes.take(6)
    }

    fun generateOverallPrediction(chart: SolarReturnChart, yearLord: Planet, muntha: MunthaResult, tajikaAspects: List<TajikaAspectResult>, housePredictions: List<HousePrediction>, language: Language): String {
        val strong = housePredictions.count { it.strength in listOf(StringResources.get(StringKeyGeneralPart12.VARSHA_STRENGTH_EXCELLENT, language), StringResources.get(StringKeyGeneralPart12.VARSHA_STRENGTH_STRONG, language)) }
        val weak = housePredictions.count { it.strength in listOf(StringResources.get(StringKeyGeneralPart12.VARSHA_STRENGTH_WEAK, language), StringResources.get(StringKeyGeneralPart12.VARSHA_STRENGTH_CHALLENGED, language)) }
        val s = evaluatePlanetStrengthDescription(yearLord, chart, language)
        val overallTone = when {
            s in listOf(StringResources.get(StringKeyGeneralPart12.VARSHA_STRENGTH_EXALTED, language), StringResources.get(StringKeyGeneralPart12.VARSHA_STRENGTH_STRONG, language)) && strong >= 6 -> StringResources.get(StringKeyGeneralPart12.VARSHA_TONE_EXCELLENT, language)
            s in listOf(StringResources.get(StringKeyGeneralPart12.VARSHA_STRENGTH_EXALTED, language), StringResources.get(StringKeyGeneralPart12.VARSHA_STRENGTH_STRONG, language)) && strong >= 4 -> StringResources.get(StringKeyGeneralPart12.VARSHA_TONE_FAVORABLE, language)
            strong > weak -> StringResources.get(StringKeyGeneralPart12.VARSHA_TONE_POSITIVE, language)
            weak > strong -> StringResources.get(StringKeyGeneralPart12.VARSHA_TONE_CHALLENGING_GROWTH, language)
            else -> StringResources.get(StringKeyGeneralPart12.VARSHA_TONE_BALANCED, language)
        }
        val ylInf = when (yearLord) { Planet.SUN -> StringResources.get(StringKeyGeneralPart12.VARSHA_YEARLORD_SUN, language); Planet.MOON -> StringResources.get(StringKeyGeneralPart12.VARSHA_YEARLORD_MOON, language); Planet.MARS -> StringResources.get(StringKeyGeneralPart12.VARSHA_YEARLORD_MARS, language); Planet.MERCURY -> StringResources.get(StringKeyGeneralPart12.VARSHA_YEARLORD_MERCURY, language); Planet.JUPITER -> StringResources.get(StringKeyGeneralPart12.VARSHA_YEARLORD_JUPITER, language); Planet.VENUS -> StringResources.get(StringKeyGeneralPart12.VARSHA_YEARLORD_VENUS, language); Planet.SATURN -> StringResources.get(StringKeyGeneralPart12.VARSHA_YEARLORD_SATURN, language); else -> StringResources.get(StringKeyGeneralPart12.VARSHA_YEARLORD_GENERAL, language) }
        val mTheme = muntha.themes.firstOrNull() ?: StringResources.get(StringKeyVarshaphala.MUNTHA_GENERAL_GROWTH, language)
        val mInf = StringResources.get(StringKeyGeneralPart12.VARSHA_MUNTHA_INFLUENCE, language, muntha.house, muntha.sign.getLocalizedName(language), mTheme.lowercase())
        return StringResources.get(StringKeyGeneralPart12.VARSHA_OVERALL_TEMPLATE, language, overallTone.lowercase(), ylInf, mInf, tajikaAspects.count { it.type.isPositive }, tajikaAspects.count { !it.type.isPositive })
    }

    fun calculateYearRating(chart: SolarReturnChart, yearLord: Planet, muntha: MunthaResult, tajikaAspects: List<TajikaAspectResult>, housePredictions: List<HousePrediction>, language: Language): Float {
        var r = 3.0f
        val s = evaluatePlanetStrengthDescription(yearLord, chart, language)
        r += when (s) { StringResources.get(StringKeyGeneralPart12.VARSHA_STRENGTH_EXALTED, language) -> 0.8f; StringResources.get(StringKeyGeneralPart12.VARSHA_STRENGTH_STRONG, language) -> 0.5f; StringResources.get(StringKeyGeneralPart12.VARSHA_STRENGTH_ANGULAR, language) -> 0.3f; StringResources.get(StringKeyGeneralPart12.VARSHA_STRENGTH_DEBILITATED, language) -> -0.5f; else -> 0.0f }
        r += when (muntha.lordStrength) { StringResources.get(StringKeyGeneralPart12.VARSHA_STRENGTH_EXALTED, language), StringResources.get(StringKeyGeneralPart12.VARSHA_STRENGTH_STRONG, language) -> 0.3f; StringResources.get(StringKeyGeneralPart12.VARSHA_STRENGTH_MODERATE, language) -> 0.1f; StringResources.get(StringKeyGeneralPart12.VARSHA_STRENGTH_DEBILITATED, language) -> -0.3f; else -> 0.0f }
        if (muntha.house in listOf(1, 2, 4, 5, 9, 10, 11)) r += 0.2f
        r += (tajikaAspects.count { it.type.isPositive && it.strength.weight >= 0.6 } * 0.1f - tajikaAspects.count { !it.type.isPositive && it.strength.weight >= 0.6 } * 0.1f).coerceIn(-0.5f, 0.5f)
        if (housePredictions.isNotEmpty()) r += (housePredictions.map { it.rating }.average().toFloat() - 3.0f) * 0.3f
        r += chart.planetPositions.count { (p, pos) -> p in listOf(Planet.JUPITER, Planet.VENUS) && pos.house in listOf(1, 4, 7, 10) } * 0.15f
        return r.coerceIn(1.0f, 5.0f)
    }
}


