package com.astro.storm.ephemeris.prediction

import com.astro.storm.core.common.Language
import com.astro.storm.core.common.StringKeyInterface
import com.astro.storm.core.common.StringResources
import com.astro.storm.core.common.StringKeyPrediction
import com.astro.storm.core.common.StringKeyPredictionNarrative
import com.astro.storm.core.common.getLocalizedName
import com.astro.storm.core.model.LifeArea
import com.astro.storm.core.model.Planet
import com.astro.storm.core.model.PlanetPosition
import com.astro.storm.core.model.VedicChart
import com.astro.storm.core.model.ZodiacSign
import com.astro.storm.ephemeris.DashaCalculator
import com.astro.storm.ephemeris.VedicAstrologyUtils
import com.astro.storm.ephemeris.YogaCalculator
import com.astro.storm.ephemeris.nativeanalysis.NativeAnalysisHelpers
import com.astro.storm.ephemeris.nativeanalysis.PlanetaryDignity
import com.astro.storm.ephemeris.remedy.RemediesCalculator
import com.astro.storm.ephemeris.yoga.YogaLocalization
import java.time.LocalDate
import java.time.LocalDateTime
import kotlin.math.roundToInt

object PredictionEngine {

    fun calculatePredictions(chart: VedicChart, language: Language): PredictionData {
        val context = PredictionContext(chart, language).withProfiles()
        val lifeOverview = buildLifeOverview(context)
        val currentPeriod = buildCurrentPeriod(context)
        val lifeAreas = buildLifeAreaPredictions(context)
        val activeYogas = buildActiveYogas(context)
        val challengesOpportunities = buildChallengesOpportunities(context)
        val timing = buildTiming(context)
        val remedies = buildRemedies(context)

        return PredictionData(
            chart = chart,
            lifeOverview = lifeOverview,
            currentPeriod = currentPeriod,
            lifeAreas = lifeAreas,
            activeYogas = activeYogas,
            challengesOpportunities = challengesOpportunities,
            timing = timing,
            remedies = remedies
        )
    }

    private data class PlanetProfile(
        val planet: Planet,
        val position: PlanetPosition,
        val dignity: PlanetaryDignity,
        val strengthScore: Int,
        val houseQuality: HouseQuality,
        val isBenefic: Boolean
    )

    private data class HouseProfile(
        val house: Int,
        val lord: Planet,
        val lordStrength: Int,
        val beneficSupport: Int,
        val maleficPressure: Int
    )

    private enum class HouseQuality { STRONG, MIXED, CHALLENGED }

    private data class PredictionContext(
        val chart: VedicChart,
        val language: Language,
        val ascendant: ZodiacSign,
        val moonSign: ZodiacSign,
        val sunSign: ZodiacSign,
        val dashaTimeline: DashaCalculator.DashaTimeline,
        val yogas: com.astro.storm.ephemeris.yoga.YogaAnalysis,
        val planetProfiles: Map<Planet, PlanetProfile>,
        val houseProfiles: Map<Int, HouseProfile>
    ) {
        constructor(chart: VedicChart, language: Language) : this(
            chart = chart,
            language = language,
            ascendant = ZodiacSign.fromLongitude(chart.ascendant),
            moonSign = chart.planetPositions.find { it.planet == Planet.MOON }?.sign
                ?: ZodiacSign.fromLongitude(chart.ascendant),
            sunSign = chart.planetPositions.find { it.planet == Planet.SUN }?.sign
                ?: ZodiacSign.fromLongitude(chart.ascendant),
            dashaTimeline = DashaCalculator.calculateDashaTimeline(chart),
            yogas = YogaCalculator.calculateYogas(chart),
            planetProfiles = emptyMap(),
            houseProfiles = emptyMap()
        )
    }

    private fun PredictionContext.withProfiles(): PredictionContext {
        val profiles = chart.planetPositions.associate { position ->
            val dignity = NativeAnalysisHelpers.getDignity(position)
            val strengthScore = estimatePlanetStrength(position, dignity)
            val houseQuality = determineHouseQuality(position.house, chart)
            position.planet to PlanetProfile(
                planet = position.planet,
                position = position,
                dignity = dignity,
                strengthScore = strengthScore,
                houseQuality = houseQuality,
                isBenefic = VedicAstrologyUtils.isNaturalBenefic(position.planet)
            )
        }
        val houseProfiles = (1..12).associateWith { house ->
            val lord = VedicAstrologyUtils.getHouseLord(chart, house)
            val lordPosition = chart.planetPositions.find { it.planet == lord }
            val lordStrength = lordPosition?.let { estimatePlanetStrength(it, NativeAnalysisHelpers.getDignity(it)) } ?: 50
            val beneficSupport = chart.planetPositions.count {
                it.house == house && VedicAstrologyUtils.isNaturalBenefic(it.planet)
            }
            val maleficPressure = chart.planetPositions.count {
                it.house == house && !VedicAstrologyUtils.isNaturalBenefic(it.planet)
            }
            HouseProfile(
                house = house,
                lord = lord,
                lordStrength = lordStrength,
                beneficSupport = beneficSupport,
                maleficPressure = maleficPressure
            )
        }
        return copy(planetProfiles = profiles, houseProfiles = houseProfiles)
    }

    private fun buildLifeOverview(context: PredictionContext): LifeOverview {
        val enriched = context
        val ascendantPath = StringResources.get(getAscendantPathKey(enriched.ascendant), enriched.language)
        val moonPath = StringResources.get(getMoonPathKey(enriched.moonSign), enriched.language)

        val overallPath = if (enriched.moonSign != enriched.ascendant) {
            StringResources.get(
                StringKeyPrediction.PRED_PATH_TEMPLATE,
                enriched.language,
                enriched.ascendant.getLocalizedName(enriched.language),
                ascendantPath,
                enriched.moonSign.getLocalizedName(enriched.language),
                moonPath
            )
        } else {
            StringResources.get(
                StringKeyPrediction.PRED_PATH_TEMPLATE_UNKNOWN_MOON,
                enriched.language,
                enriched.ascendant.getLocalizedName(enriched.language),
                ascendantPath
            )
        }

        val dominantElement = NativeAnalysisHelpers.getSignElement(enriched.ascendant)
        val dominantModality = NativeAnalysisHelpers.getSignModality(enriched.ascendant)
        val dashaPlanet = enriched.dashaTimeline.currentMahadasha?.planet
        val yogaStrength = enriched.yogas.overallYogaStrength.roundToInt().coerceIn(0, 100)

        val coreNarrative = composeNarrative(
            enriched.language,
            StringKeyPredictionNarrative.PRED_OVERVIEW_ASC_MOON_TEMPLATE to arrayOf(
                enriched.ascendant.getLocalizedName(enriched.language),
                enriched.moonSign.getLocalizedName(enriched.language)
            ),
            StringKeyPredictionNarrative.PRED_OVERVIEW_ELEMENT_TEMPLATE to arrayOf(
                dominantElement.getLocalizedName(enriched.language)
            ),
            StringKeyPredictionNarrative.PRED_OVERVIEW_MODALITY_TEMPLATE to arrayOf(
                dominantModality.getLocalizedName(enriched.language)
            ),
            StringKeyPredictionNarrative.PRED_OVERVIEW_YOGA_TEMPLATE to arrayOf(
                yogaStrength
            ),
            StringKeyPredictionNarrative.PRED_OVERVIEW_DASHA_TEMPLATE to arrayOf(
                dashaPlanet?.getLocalizedName(enriched.language)
                    ?: StringResources.get(StringKeyPredictionNarrative.PRED_OVERVIEW_DASHA_UNKNOWN, enriched.language)
            )
        )

        val lifeTheme = StringResources.get(
            StringKeyPrediction.PRED_LIFE_THEME_TEMPLATE,
            enriched.language,
            StringResources.get(getLifeThemeKey(enriched.ascendant), enriched.language)
        )

        val spiritualPath = StringResources.get(
            StringKeyPrediction.PRED_SPIRIT_TEMPLATE,
            enriched.language,
            StringResources.get(getSpiritualPathKey(enriched.moonSign), enriched.language),
            dashaPlanet?.getLocalizedName(enriched.language)
                ?: StringResources.get(StringKeyPrediction.PRED_CURRENT, enriched.language)
        )

        val keyStrengths = buildKeyStrengths(enriched)

        return LifeOverview(
            overallPath = overallPath,
            keyStrengths = keyStrengths,
            lifeTheme = lifeTheme,
            spiritualPath = spiritualPath,
            coreNarrative = coreNarrative
        )
    }

    private fun buildCurrentPeriod(context: PredictionContext): CurrentPeriodAnalysis {
        val enriched = context
        val currentMahadasha = enriched.dashaTimeline.currentMahadasha
        val currentAntardasha = enriched.dashaTimeline.currentAntardasha

        val dashaInfo = currentMahadasha?.let { md ->
            val mdName = md.planet.getLocalizedName(enriched.language)
            if (currentAntardasha != null) {
                val adName = currentAntardasha.planet.getLocalizedName(enriched.language)
                StringResources.get(StringKeyPrediction.PRED_DASHA_INFO_TEMPLATE, enriched.language, mdName, adName)
            } else {
                StringResources.get(StringKeyPrediction.PRED_MAHADASHA_LABEL, enriched.language, mdName)
            }
        } ?: StringResources.get(StringKeyPrediction.PRED_DASHA_PERIOD_DEFAULT, enriched.language)

        val dashaEffect = buildDashaEffect(enriched, currentMahadasha, currentAntardasha)
        val highlights = buildTransitHighlights(enriched)
        val overallEnergy = estimateEnergy(enriched, currentMahadasha)
        val period = currentMahadasha?.let {
            StringResources.get(StringKeyPrediction.PRED_YEARS_REMAINING, enriched.language, it.getRemainingYears())
        } ?: ""

        val keyFocusAreas = buildFocusAreas(enriched, currentMahadasha, currentAntardasha)

        return CurrentPeriodAnalysis(
            dashaInfo = dashaInfo,
            dashaEffect = dashaEffect,
            transitHighlights = highlights,
            overallEnergy = overallEnergy,
            period = period,
            keyFocusAreas = keyFocusAreas
        )
    }

    private fun buildLifeAreaPredictions(context: PredictionContext): List<LifeAreaPrediction> {
        val enriched = context
        return LifeArea.entries.map { area ->
            val score = evaluateLifeAreaScore(enriched, area)
            val rating = ((score / 20.0).roundToInt()).coerceIn(1, 5)
            val keyFactors = buildLifeAreaKeyFactors(enriched, area, score)
            val advice = buildLifeAreaAdvice(enriched, area, score)
            val supportingPlanets = buildLifeAreaSupportingPlanets(enriched, area)
            LifeAreaPrediction(
                area = area,
                rating = rating,
                shortTerm = buildTimeframePrediction(enriched, area, score, Timeframe.SHORT),
                mediumTerm = buildTimeframePrediction(enriched, area, score, Timeframe.MEDIUM),
                longTerm = buildTimeframePrediction(enriched, area, score, Timeframe.LONG),
                keyFactors = keyFactors,
                advice = advice,
                supportingPlanets = supportingPlanets
            )
        }
    }

    private fun buildActiveYogas(context: PredictionContext): List<ActiveYoga> {
        val enriched = context
        return enriched.yogas.allYogas
            .sortedByDescending { it.strengthPercentage }
            .take(5)
            .map { yoga ->
                ActiveYoga(
                    name = YogaLocalization.getLocalizedYogaName(yoga.name, enriched.language),
                    description = YogaLocalization.getLocalizedYogaDescription(yoga.name, enriched.language).ifEmpty { yoga.description },
                    strength = yoga.strength.value,
                    effects = YogaLocalization.getLocalizedYogaEffects(yoga.name, enriched.language).ifEmpty { yoga.effects },
                    planets = yoga.planets
                )
            }
    }

    private fun buildChallengesOpportunities(context: PredictionContext): ChallengesOpportunities {
        val enriched = context
        val challenges = buildChallenges(enriched)
        val opportunities = buildOpportunities(enriched)
        return ChallengesOpportunities(challenges, opportunities)
    }

    private fun buildTiming(context: PredictionContext): TimingAnalysis {
        val enriched = context
        val currentMahadasha = enriched.dashaTimeline.currentMahadasha
        val currentPlanet = currentMahadasha?.planet
        val today = LocalDate.now()

        val favorable = mutableListOf<FavorablePeriod>()
        val unfavorable = mutableListOf<UnfavorablePeriod>()

        if (currentPlanet != null) {
            val planetScore = enriched.planetProfiles[currentPlanet]?.strengthScore ?: 50
            val periodStart = currentMahadasha.startDate.toLocalDate()
            val periodEnd = currentMahadasha.endDate.toLocalDate()
            if (planetScore >= 60) {
                favorable.add(
                    FavorablePeriod(
                        startDate = maxOf(today, periodStart),
                        endDate = minOf(periodEnd, today.plusMonths(9)),
                        reason = StringResources.get(
                            StringKeyPredictionNarrative.PRED_TIMING_FAVORABLE_DASHA,
                            enriched.language,
                            currentPlanet.getLocalizedName(enriched.language)
                        ),
                        bestFor = buildTimingBestFor(enriched, currentPlanet)
                    )
                )
            } else {
                unfavorable.add(
                    UnfavorablePeriod(
                        startDate = maxOf(today, periodStart),
                        endDate = minOf(periodEnd, today.plusMonths(6)),
                        reason = StringResources.get(
                            StringKeyPredictionNarrative.PRED_TIMING_CAUTION_DASHA,
                            enriched.language,
                            currentPlanet.getLocalizedName(enriched.language)
                        ),
                        avoid = buildTimingAvoid(enriched, currentPlanet)
                    )
                )
            }
        }

        val keyDates = enriched.dashaTimeline.upcomingSandhis.take(3).map { sandhi ->
            KeyDate(
                date = sandhi.transitionDate.toLocalDate(),
                event = StringResources.get(
                    StringKeyPredictionNarrative.PRED_TIMING_SANDHI_EVENT,
                    enriched.language,
                    sandhi.fromPlanet.getLocalizedName(enriched.language),
                    sandhi.toPlanet.getLocalizedName(enriched.language)
                ),
                significance = StringResources.get(
                    StringKeyPredictionNarrative.PRED_TIMING_SANDHI_SIGNIFICANCE,
                    enriched.language,
                    sandhi.level.getLocalizedName(enriched.language)
                ),
                isPositive = VedicAstrologyUtils.isNaturalBenefic(sandhi.toPlanet)
            )
        }

        return TimingAnalysis(
            favorablePeriods = favorable,
            unfavorablePeriods = unfavorable,
            keyDates = keyDates
        )
    }

    private fun buildRemedies(context: PredictionContext): List<RemedySuggestion> {
        val remedies = RemediesCalculator.calculateRemedies(context.chart, context.language)
        return remedies.prioritizedRemedies.take(6).map { remedy ->
            RemedySuggestion(
                title = remedy.title,
                description = remedy.description,
                method = remedy.method,
                timing = remedy.timing,
                duration = remedy.duration
            )
        }
    }

    private fun buildKeyStrengths(context: PredictionContext): List<String> {
        val profiles = context.planetProfiles.values.sortedByDescending { it.strengthScore }.take(4)
        return profiles.map { profile ->
            val template = if (profile.strengthScore >= 75) {
                StringKeyPredictionNarrative.PRED_STRENGTH_PLANET_STRONG
            } else {
                StringKeyPredictionNarrative.PRED_STRENGTH_PLANET_SUPPORTIVE
            }
            StringResources.get(template, context.language, profile.planet.getLocalizedName(context.language))
        }
    }

    private fun buildDashaEffect(
        context: PredictionContext,
        mahadasha: DashaCalculator.Mahadasha?,
        antardasha: DashaCalculator.Antardasha?
    ): String {
        if (mahadasha == null) {
            return StringResources.get(StringKeyPrediction.PRED_CALC_PROGRESS, context.language)
        }
        val planetProfile = context.planetProfiles[mahadasha.planet]
        val baseTemplate = when {
            planetProfile == null -> StringKeyPredictionNarrative.PRED_DASHA_GENERAL
            planetProfile.strengthScore >= 70 -> StringKeyPredictionNarrative.PRED_DASHA_POSITIVE
            planetProfile.strengthScore >= 55 -> StringKeyPredictionNarrative.PRED_DASHA_STEADY
            else -> StringKeyPredictionNarrative.PRED_DASHA_CHALLENGE
        }

        val baseText = StringResources.get(
            baseTemplate,
            context.language,
            mahadasha.planet.getLocalizedName(context.language),
            planetProfile?.position?.house ?: 1,
            planetProfile?.position?.sign?.getLocalizedName(context.language)
                ?: context.ascendant.getLocalizedName(context.language)
        )

        val subText = antardasha?.let {
            StringResources.get(
                StringKeyPredictionNarrative.PRED_DASHA_ANTAR_TEMPLATE,
                context.language,
                it.planet.getLocalizedName(context.language)
            )
        }

        return listOfNotNull(baseText, subText).joinToString(" ")
    }

    private fun buildTransitHighlights(context: PredictionContext): List<TransitHighlight> {
        return context.planetProfiles.values
            .sortedByDescending { it.strengthScore }
            .take(5)
            .map { profile ->
                val description = StringResources.get(
                    StringKeyPredictionNarrative.PRED_TRANSIT_HIGHLIGHT,
                    context.language,
                    profile.planet.getLocalizedName(context.language),
                    profile.position.sign.getLocalizedName(context.language),
                    profile.position.house
                )
                TransitHighlight(
                    planet = profile.planet,
                    description = description,
                    impact = (profile.strengthScore / 12).coerceIn(1, 9),
                    isPositive = profile.isBenefic || profile.strengthScore >= 65
                )
            }
    }

    private fun estimateEnergy(context: PredictionContext, mahadasha: DashaCalculator.Mahadasha?): Int {
        val base = mahadasha?.let { context.planetProfiles[it.planet]?.strengthScore ?: 60 } ?: 60
        val yogaBoost = (context.yogas.overallYogaStrength / 20).roundToInt()
        return ((base / 12) + yogaBoost).coerceIn(1, 10)
    }

    private fun buildFocusAreas(
        context: PredictionContext,
        mahadasha: DashaCalculator.Mahadasha?,
        antardasha: DashaCalculator.Antardasha?
    ): List<String> {
        val planets = listOfNotNull(mahadasha?.planet, antardasha?.planet).distinct()
        if (planets.isEmpty()) {
            return listOf(StringResources.get(StringKeyPredictionNarrative.PRED_FOCUS_GENERAL, context.language))
        }
        return planets.map { planet ->
            StringResources.get(
                StringKeyPredictionNarrative.PRED_FOCUS_PLANET,
                context.language,
                planet.getLocalizedName(context.language)
            )
        }
    }

    private fun buildChallenges(context: PredictionContext): List<Challenge> {
        val challengingPlanets = context.planetProfiles.values
            .filter { it.strengthScore < 55 }
            .sortedBy { it.strengthScore }
            .take(3)

        return challengingPlanets.map { profile ->
            Challenge(
                area = StringResources.get(
                    StringKeyPredictionNarrative.PRED_CHALLENGE_AREA_PLANET,
                    context.language,
                    profile.planet.getLocalizedName(context.language)
                ),
                description = StringResources.get(
                    StringKeyPredictionNarrative.PRED_CHALLENGE_DESC_PLANET,
                    context.language,
                    profile.planet.getLocalizedName(context.language),
                    profile.position.house
                ),
                severity = ((60 - profile.strengthScore) / 10).coerceIn(2, 5),
                mitigation = StringResources.get(
                    StringKeyPredictionNarrative.PRED_CHALLENGE_MITIGATION,
                    context.language,
                    profile.planet.getLocalizedName(context.language)
                )
            )
        }
    }

    private fun buildOpportunities(context: PredictionContext): List<Opportunity> {
        val supportivePlanets = context.planetProfiles.values
            .filter { it.strengthScore >= 70 }
            .sortedByDescending { it.strengthScore }
            .take(3)

        return supportivePlanets.map { profile ->
            Opportunity(
                area = StringResources.get(
                    StringKeyPredictionNarrative.PRED_OPPORTUNITY_AREA_PLANET,
                    context.language,
                    profile.planet.getLocalizedName(context.language)
                ),
                description = StringResources.get(
                    StringKeyPredictionNarrative.PRED_OPPORTUNITY_DESC_PLANET,
                    context.language,
                    profile.planet.getLocalizedName(context.language),
                    profile.position.sign.getLocalizedName(context.language)
                ),
                timing = StringResources.get(
                    StringKeyPredictionNarrative.PRED_OPPORTUNITY_TIMING,
                    context.language,
                    profile.position.house
                ),
                howToLeverage = StringResources.get(
                    StringKeyPredictionNarrative.PRED_OPPORTUNITY_LEVERAGE,
                    context.language,
                    profile.planet.getLocalizedName(context.language)
                )
            )
        }
    }

    private enum class Timeframe { SHORT, MEDIUM, LONG }

    private fun buildTimeframePrediction(
        context: PredictionContext,
        area: LifeArea,
        score: Int,
        timeframe: Timeframe
    ): String {
        val tier = when {
            score >= 80 -> PredictionTier.HIGH
            score >= 60 -> PredictionTier.STEADY
            else -> PredictionTier.CAUTIOUS
        }
        val key = when (timeframe) {
            Timeframe.SHORT -> tier.shortKey
            Timeframe.MEDIUM -> tier.mediumKey
            Timeframe.LONG -> tier.longKey
        }
        return StringResources.get(
            key,
            context.language,
            area.getLocalizedName(context.language)
        )
    }

    private enum class PredictionTier(
        val shortKey: StringKeyInterface,
        val mediumKey: StringKeyInterface,
        val longKey: StringKeyInterface
    ) {
        HIGH(
            StringKeyPredictionNarrative.PRED_AREA_SHORT_HIGH,
            StringKeyPredictionNarrative.PRED_AREA_MEDIUM_HIGH,
            StringKeyPredictionNarrative.PRED_AREA_LONG_HIGH
        ),
        STEADY(
            StringKeyPredictionNarrative.PRED_AREA_SHORT_STEADY,
            StringKeyPredictionNarrative.PRED_AREA_MEDIUM_STEADY,
            StringKeyPredictionNarrative.PRED_AREA_LONG_STEADY
        ),
        CAUTIOUS(
            StringKeyPredictionNarrative.PRED_AREA_SHORT_CAUTION,
            StringKeyPredictionNarrative.PRED_AREA_MEDIUM_CAUTION,
            StringKeyPredictionNarrative.PRED_AREA_LONG_CAUTION
        )
    }

    private fun buildLifeAreaKeyFactors(
        context: PredictionContext,
        area: LifeArea,
        score: Int
    ): List<String> {
        val factors = mutableListOf<String>()
        val relevantHouses = lifeAreaHouses(area)
        relevantHouses.take(2).forEach { house ->
            val houseProfile = context.houseProfiles[house] ?: return@forEach
            factors.add(
                StringResources.get(
                    StringKeyPredictionNarrative.PRED_FACTOR_HOUSE_LORD,
                    context.language,
                    house,
                    houseProfile.lord.getLocalizedName(context.language)
                )
            )
        }
        if (score >= 70) {
            factors.add(StringResources.get(StringKeyPredictionNarrative.PRED_FACTOR_SUPPORT_STRONG, context.language))
        } else {
            factors.add(StringResources.get(StringKeyPredictionNarrative.PRED_FACTOR_SUPPORT_BALANCE, context.language))
        }
        return factors
    }

    private fun buildLifeAreaAdvice(
        context: PredictionContext,
        area: LifeArea,
        score: Int
    ): String {
        val key = when {
            score >= 80 -> StringKeyPredictionNarrative.PRED_ADVICE_HIGH
            score >= 60 -> StringKeyPredictionNarrative.PRED_ADVICE_STEADY
            else -> StringKeyPredictionNarrative.PRED_ADVICE_CAUTION
        }
        return StringResources.get(key, context.language, area.getLocalizedName(context.language))
    }

    private fun buildLifeAreaSupportingPlanets(context: PredictionContext, area: LifeArea): List<Planet> {
        val relevantPlanets = lifeAreaPlanets(area)
        return relevantPlanets.sortedByDescending { planet ->
            context.planetProfiles[planet]?.strengthScore ?: 50
        }.take(3)
    }

    private fun evaluateLifeAreaScore(context: PredictionContext, area: LifeArea): Int {
        val houses = lifeAreaHouses(area)
        val houseScore = houses.mapNotNull { context.houseProfiles[it]?.lordStrength }.average().toInt()
        val planetScore = lifeAreaPlanets(area).mapNotNull { context.planetProfiles[it]?.strengthScore }.average().toInt()
        val yogaBoost = when (area) {
            LifeArea.CAREER -> context.yogas.rajaYogas.size * 2
            LifeArea.FINANCE -> context.yogas.dhanaYogas.size * 2
            LifeArea.SPIRITUAL -> context.yogas.specialYogas.size
            LifeArea.RELATIONSHIPS -> context.yogas.chandraYogas.size
            else -> context.yogas.allYogas.size / 6
        }
        val score = (houseScore * 0.6 + planetScore * 0.4 + yogaBoost).roundToInt()
        return score.coerceIn(35, 95)
    }

    private fun estimatePlanetStrength(position: PlanetPosition, dignity: PlanetaryDignity): Int {
        var score = 50
        score += when (dignity) {
            PlanetaryDignity.EXALTED -> 22
            PlanetaryDignity.MOOLATRIKONA -> 18
            PlanetaryDignity.OWN_SIGN -> 15
            PlanetaryDignity.FRIEND_SIGN -> 8
            PlanetaryDignity.NEUTRAL_SIGN -> 0
            PlanetaryDignity.ENEMY_SIGN -> -8
            PlanetaryDignity.DEBILITATED -> -20
        }
        if (VedicAstrologyUtils.isKendra(position.house) || VedicAstrologyUtils.isTrikona(position.house)) score += 6
        if (VedicAstrologyUtils.isDusthana(position.house)) score -= 6
        if (VedicAstrologyUtils.isUpachaya(position.house)) score += 3
        if (position.isRetrograde) score += 4
        return score.coerceIn(25, 95)
    }

    private fun determineHouseQuality(house: Int, chart: VedicChart): HouseQuality {
        val benefics = chart.planetPositions.count { it.house == house && VedicAstrologyUtils.isNaturalBenefic(it.planet) }
        val malefics = chart.planetPositions.count { it.house == house && !VedicAstrologyUtils.isNaturalBenefic(it.planet) }
        return when {
            benefics >= malefics + 1 -> HouseQuality.STRONG
            malefics >= benefics + 1 -> HouseQuality.CHALLENGED
            else -> HouseQuality.MIXED
        }
    }

    private fun lifeAreaHouses(area: LifeArea): List<Int> = when (area) {
        LifeArea.CAREER -> listOf(10, 6, 2)
        LifeArea.FINANCE -> listOf(2, 11, 5)
        LifeArea.RELATIONSHIPS -> listOf(7, 2, 5)
        LifeArea.HEALTH -> listOf(1, 6, 8)
        LifeArea.EDUCATION -> listOf(4, 5, 9)
        LifeArea.SPIRITUAL -> listOf(9, 12, 8)
        LifeArea.FAMILY -> listOf(2, 4)
        LifeArea.PROPERTY -> listOf(4, 11)
        LifeArea.FOREIGN -> listOf(12, 9)
    }

    private fun lifeAreaPlanets(area: LifeArea): List<Planet> = when (area) {
        LifeArea.CAREER -> listOf(Planet.SUN, Planet.SATURN, Planet.MERCURY)
        LifeArea.FINANCE -> listOf(Planet.JUPITER, Planet.VENUS, Planet.MERCURY)
        LifeArea.RELATIONSHIPS -> listOf(Planet.VENUS, Planet.MOON, Planet.JUPITER)
        LifeArea.HEALTH -> listOf(Planet.SUN, Planet.MARS, Planet.SATURN)
        LifeArea.EDUCATION -> listOf(Planet.MERCURY, Planet.JUPITER, Planet.MOON)
        LifeArea.SPIRITUAL -> listOf(Planet.JUPITER, Planet.KETU, Planet.SATURN)
        LifeArea.FAMILY -> listOf(Planet.MOON, Planet.JUPITER)
        LifeArea.PROPERTY -> listOf(Planet.MARS, Planet.VENUS)
        LifeArea.FOREIGN -> listOf(Planet.RAHU, Planet.KETU, Planet.JUPITER)
    }

    private fun buildTimingBestFor(context: PredictionContext, planet: Planet): List<String> {
        val key = when (planet) {
            Planet.JUPITER -> StringKeyPredictionNarrative.PRED_TIMING_BEST_JUPITER
            Planet.VENUS -> StringKeyPredictionNarrative.PRED_TIMING_BEST_VENUS
            Planet.SATURN -> StringKeyPredictionNarrative.PRED_TIMING_BEST_SATURN
            Planet.SUN -> StringKeyPredictionNarrative.PRED_TIMING_BEST_SUN
            Planet.MOON -> StringKeyPredictionNarrative.PRED_TIMING_BEST_MOON
            Planet.MERCURY -> StringKeyPredictionNarrative.PRED_TIMING_BEST_MERCURY
            Planet.MARS -> StringKeyPredictionNarrative.PRED_TIMING_BEST_MARS
            Planet.RAHU -> StringKeyPredictionNarrative.PRED_TIMING_BEST_RAHU
            Planet.KETU -> StringKeyPredictionNarrative.PRED_TIMING_BEST_KETU
            else -> StringKeyPredictionNarrative.PRED_TIMING_BEST_GENERAL
        }
        return StringResources.get(key, context.language).split(" • ").filter { it.isNotBlank() }
    }

    private fun buildTimingAvoid(context: PredictionContext, planet: Planet): List<String> {
        val key = when (planet) {
            Planet.SATURN -> StringKeyPredictionNarrative.PRED_TIMING_AVOID_SATURN
            Planet.RAHU -> StringKeyPredictionNarrative.PRED_TIMING_AVOID_RAHU
            Planet.KETU -> StringKeyPredictionNarrative.PRED_TIMING_AVOID_KETU
            Planet.MARS -> StringKeyPredictionNarrative.PRED_TIMING_AVOID_MARS
            else -> StringKeyPredictionNarrative.PRED_TIMING_AVOID_GENERAL
        }
        return StringResources.get(key, context.language).split(" • ").filter { it.isNotBlank() }
    }

    private fun getAscendantPathKey(sign: ZodiacSign): StringKeyInterface = when (sign) {
        ZodiacSign.ARIES -> StringKeyPrediction.PRED_ASC_ARIES
        ZodiacSign.TAURUS -> StringKeyPrediction.PRED_ASC_TAURUS
        ZodiacSign.GEMINI -> StringKeyPrediction.PRED_ASC_GEMINI
        ZodiacSign.CANCER -> StringKeyPrediction.PRED_ASC_CANCER
        ZodiacSign.LEO -> StringKeyPrediction.PRED_ASC_LEO
        ZodiacSign.VIRGO -> StringKeyPrediction.PRED_ASC_VIRGO
        ZodiacSign.LIBRA -> StringKeyPrediction.PRED_ASC_LIBRA
        ZodiacSign.SCORPIO -> StringKeyPrediction.PRED_ASC_SCORPIO
        ZodiacSign.SAGITTARIUS -> StringKeyPrediction.PRED_ASC_SAGITTARIUS
        ZodiacSign.CAPRICORN -> StringKeyPrediction.PRED_ASC_CAPRICORN
        ZodiacSign.AQUARIUS -> StringKeyPrediction.PRED_ASC_AQUARIUS
        ZodiacSign.PISCES -> StringKeyPrediction.PRED_ASC_PISCES
    }

    private fun getMoonPathKey(sign: ZodiacSign): StringKeyInterface = when (sign) {
        ZodiacSign.ARIES -> StringKeyPrediction.PRED_MOON_ARIES
        ZodiacSign.TAURUS -> StringKeyPrediction.PRED_MOON_TAURUS
        ZodiacSign.GEMINI -> StringKeyPrediction.PRED_MOON_GEMINI
        ZodiacSign.CANCER -> StringKeyPrediction.PRED_MOON_CANCER
        ZodiacSign.LEO -> StringKeyPrediction.PRED_MOON_LEO
        ZodiacSign.VIRGO -> StringKeyPrediction.PRED_MOON_VIRGO
        ZodiacSign.LIBRA -> StringKeyPrediction.PRED_MOON_LIBRA
        ZodiacSign.SCORPIO -> StringKeyPrediction.PRED_MOON_SCORPIO
        ZodiacSign.SAGITTARIUS -> StringKeyPrediction.PRED_MOON_SAGITTARIUS
        ZodiacSign.CAPRICORN -> StringKeyPrediction.PRED_MOON_CAPRICORN
        ZodiacSign.AQUARIUS -> StringKeyPrediction.PRED_MOON_AQUARIUS
        ZodiacSign.PISCES -> StringKeyPrediction.PRED_MOON_PISCES
    }

    private fun getLifeThemeKey(sign: ZodiacSign): StringKeyInterface = when (sign) {
        ZodiacSign.ARIES -> StringKeyPrediction.PRED_THEME_ARIES
        ZodiacSign.TAURUS -> StringKeyPrediction.PRED_THEME_TAURUS
        ZodiacSign.GEMINI -> StringKeyPrediction.PRED_THEME_GEMINI
        ZodiacSign.CANCER -> StringKeyPrediction.PRED_THEME_CANCER
        ZodiacSign.LEO -> StringKeyPrediction.PRED_THEME_LEO
        ZodiacSign.VIRGO -> StringKeyPrediction.PRED_THEME_VIRGO
        ZodiacSign.LIBRA -> StringKeyPrediction.PRED_THEME_LIBRA
        ZodiacSign.SCORPIO -> StringKeyPrediction.PRED_THEME_SCORPIO
        ZodiacSign.SAGITTARIUS -> StringKeyPrediction.PRED_THEME_SAGITTARIUS
        ZodiacSign.CAPRICORN -> StringKeyPrediction.PRED_THEME_CAPRICORN
        ZodiacSign.AQUARIUS -> StringKeyPrediction.PRED_THEME_AQUARIUS
        ZodiacSign.PISCES -> StringKeyPrediction.PRED_THEME_PISCES
    }

    private fun getSpiritualPathKey(sign: ZodiacSign): StringKeyInterface = when (sign) {
        ZodiacSign.ARIES -> StringKeyPrediction.PRED_SPIRIT_ARIES
        ZodiacSign.TAURUS -> StringKeyPrediction.PRED_SPIRIT_TAURUS
        ZodiacSign.GEMINI -> StringKeyPrediction.PRED_SPIRIT_GEMINI
        ZodiacSign.CANCER -> StringKeyPrediction.PRED_SPIRIT_CANCER
        ZodiacSign.LEO -> StringKeyPrediction.PRED_SPIRIT_LEO
        ZodiacSign.VIRGO -> StringKeyPrediction.PRED_SPIRIT_VIRGO
        ZodiacSign.LIBRA -> StringKeyPrediction.PRED_SPIRIT_LIBRA
        ZodiacSign.SCORPIO -> StringKeyPrediction.PRED_SPIRIT_SCORPIO
        ZodiacSign.SAGITTARIUS -> StringKeyPrediction.PRED_SPIRIT_SAGITTARIUS
        ZodiacSign.CAPRICORN -> StringKeyPrediction.PRED_SPIRIT_CAPRICORN
        ZodiacSign.AQUARIUS -> StringKeyPrediction.PRED_SPIRIT_AQUARIUS
        ZodiacSign.PISCES -> StringKeyPrediction.PRED_SPIRIT_PISCES
    }

    private fun composeNarrative(
        language: Language,
        vararg parts: Pair<StringKeyInterface, Array<Any>>
    ): String {
        return parts.joinToString("\n\n") { (key, args) ->
            StringResources.get(key, language, *args)
        }
    }
}
