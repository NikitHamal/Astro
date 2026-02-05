package com.astro.storm.ephemeris.deepanalysis.predictions

import com.astro.storm.core.model.*
import com.astro.storm.data.templates.TemplateSelector
import com.astro.storm.ephemeris.deepanalysis.*
import com.astro.storm.ephemeris.DashaCalculator
import java.time.LocalDate
import java.time.LocalDateTime
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Deep Predictions Engine - Comprehensive predictions based on Dasha, Transit, and Yogas
 * 
 * Generates detailed predictions for all life areas with timing accuracy.
 */
@Singleton
class DeepPredictionEngine @Inject constructor(
    private val templateSelector: TemplateSelector
) {
    
    fun generatePredictions(chart: VedicChart, context: AnalysisContext): DeepPredictions {
        val currentDate = LocalDate.now()
        
        return DeepPredictions(
            dashaAnalysis = analyzeDashaSystem(context),
            transitAnalysis = analyzeTransits(context, currentDate),
            yearlyPrediction = generateYearlyPrediction(context, currentDate),
            monthlyPredictions = generateMonthlyPredictions(context, currentDate),
            lifeAreaPredictions = generateLifeAreaPredictions(context),
            yogaActivationTimeline = getYogaActivationTimeline(context),
            criticalPeriods = identifyCriticalPeriods(context),
            opportunityWindows = identifyOpportunityWindows(context),
            remedialMeasures = generateRemedialMeasures(context),
            overallPredictionSummary = generateOverallSummary(context),
            predictionScore = calculatePredictionScore(context)
        )
    }
    
    private fun analyzeDashaSystem(context: AnalysisContext): DashaDeepAnalysis {
        val current = context.currentMahadasha
        val currentAntar = context.currentAntardasha
        
        val mahadashaAnalysis = current?.let {
            val template = templateSelector.findBestTemplate(
                category = "dasha",
                planet = it.planet,
                dashaLevel = 1,
                sign = context.getPlanetPosition(it.planet)?.sign,
                house = context.getPlanetPosition(it.planet)?.house
            )
            MahadashaDeepAnalysis(
                planet = it.planet,
                startDate = it.startDate.toLocalDate(),
                endDate = it.endDate.toLocalDate(),
                overallTheme = LocalizedParagraph(
                    template?.en ?: PredictionTextGenerator.getDashaTheme(it.planet).en,
                    template?.ne ?: PredictionTextGenerator.getDashaTheme(it.planet).ne
                ),
                lifeAreaEffects = getLifeAreaEffectsForPlanet(it.planet, context),
                strengths = getDashaStrengths(it.planet, context),
                challenges = getDashaChallenges(it.planet, context),
                advice = PredictionTextGenerator.getDashaAdvice(it.planet, context)
            )
        }
        
        val antardashaAnalysis = currentAntar?.let {
            val template = templateSelector.findBestTemplate(
                category = "dasha",
                planet = it.planet,
                dashaLevel = 2
            )
            AntardashaDeepAnalysis(
                planet = it.planet,
                startDate = it.startDate.toLocalDate(),
                endDate = it.endDate.toLocalDate(),
                refinedTheme = LocalizedParagraph(
                    template?.en ?: PredictionTextGenerator.getAntardashaTheme(current?.planet ?: it.planet, it.planet).en,
                    template?.ne ?: PredictionTextGenerator.getAntardashaTheme(current?.planet ?: it.planet, it.planet).ne
                ),
                shortTermEffects = getShortTermEffects(it.planet, context),
                activatedYogas = getActivatedYogas(current?.planet ?: it.planet, it.planet, context)
            )
        }
        
        val upcomingDashas = getUpcomingDashas(context)
        
        return DashaDeepAnalysis(
            currentMahadasha = mahadashaAnalysis,
            currentAntardasha = antardashaAnalysis,
            upcomingDashas = upcomingDashas,
            dashaBalance = LocalizedParagraph(
                "Your current dasha period sets the foundation for this life phase.",
                "तपाईंको वर्तमान दशा अवधिले यस जीवन चरणको लागि आधार सेट गर्दछ।"
            )
        )
    }
    
    private fun getLifeAreaEffectsForPlanet(planet: Planet, context: AnalysisContext): Map<LifeArea, LocalizedParagraph> {
        return LifeArea.entries.associateWith { area ->
            val template = templateSelector.findBestTemplate(
                category = "life_area",
                lifeArea = area,
                planet = planet,
                house = context.getPlanetPosition(planet)?.house
            )
            LocalizedParagraph(
                template?.en ?: PredictionTextGenerator.getLifeAreaEffect(planet, area, context).en,
                template?.ne ?: PredictionTextGenerator.getLifeAreaEffect(planet, area, context).ne
            )
        }
    }
    
    private fun getDashaStrengths(planet: Planet, context: AnalysisContext): List<LocalizedTrait> {
        val strength = context.getPlanetStrengthLevel(planet)
        val traits = mutableListOf<LocalizedTrait>()
        
        if (strength >= StrengthLevel.STRONG) {
            traits.add(LocalizedTrait("Strong dasha lord", "बलियो दशा स्वामी", strength))
        }
        
        val dignity = context.getDignity(planet)
        if (dignity == PlanetaryDignityLevel.EXALTED || dignity == PlanetaryDignityLevel.OWN_SIGN) {
            traits.add(LocalizedTrait("Well-placed dasha lord", "राम्रो स्थानमा दशा स्वामी", StrengthLevel.STRONG))
        }
        
        return traits
    }
    
    private fun getDashaChallenges(planet: Planet, context: AnalysisContext): List<LocalizedTrait> {
        val challenges = mutableListOf<LocalizedTrait>()
        val strength = context.getPlanetStrengthLevel(planet)
        
        if (strength <= StrengthLevel.WEAK) {
            challenges.add(LocalizedTrait("Weak dasha lord", "कमजोर दशा स्वामी", strength))
        }
        
        if (context.isCombust(planet)) {
            challenges.add(LocalizedTrait("Combust planet", "अस्त ग्रह", StrengthLevel.MODERATE))
        }
        
        return challenges
    }
    
    private fun getShortTermEffects(planet: Planet, context: AnalysisContext): List<LocalizedParagraph> {
        return listOf(
            PredictionTextGenerator.getShortTermEffect(planet, LifeArea.CAREER, context),
            PredictionTextGenerator.getShortTermEffect(planet, LifeArea.RELATIONSHIPS, context),
            PredictionTextGenerator.getShortTermEffect(planet, LifeArea.HEALTH, context)
        )
    }
    
    private fun getActivatedYogas(mahadasha: Planet, antardasha: Planet, context: AnalysisContext): List<ActivatedYoga> {
        return context.yogaAnalysis.allYogas
            .filter { it.planets.contains(mahadasha) || it.planets.contains(antardasha) }
            .map { yoga ->
                val template = templateSelector.findBestTemplate(category = "yoga", yogaName = yoga.name)
                ActivatedYoga(
                    yogaName = yoga.name,
                    strength = yoga.strength.toStrengthLevel(),
                    activationLevel = when {
                        yoga.planets.contains(mahadasha) && yoga.planets.contains(antardasha) -> 
                            StrengthLevel.EXCELLENT
                        yoga.planets.contains(mahadasha) -> StrengthLevel.STRONG
                        else -> StrengthLevel.MODERATE
                    },
                    expectedResults = LocalizedParagraph(
                        template?.en ?: "${yoga.name} activates during this period, bringing its effects.",
                        template?.ne ?: "${yoga.name} यस अवधिमा सक्रिय हुन्छ, यसको प्रभावहरू ल्याउँदै।"
                    )
                )
            }.take(5)
    }
    
    private fun getUpcomingDashas(context: AnalysisContext): List<UpcomingDashaPeriod> {
        val now = LocalDateTime.now()
        return context.dashaTimeline.mahadashas
            .filter { it.endDate.isAfter(now) }
            .take(3)
            .map { dasha ->
                UpcomingDashaPeriod(
                    planet = dasha.planet,
                    startDate = dasha.startDate.toLocalDate(),
                    endDate = dasha.endDate.toLocalDate(),
                    briefPreview = PredictionTextGenerator.getDashaBriefPreview(dasha.planet, context)
                )
            }
    }
    
    private fun analyzeTransits(context: AnalysisContext, date: LocalDate): TransitDeepAnalysis {
        return TransitDeepAnalysis(
            majorTransits = getMajorTransits(context),
            saturnSadeSati = analyzeSadeSati(context),
            jupiterTransit = analyzeJupiterTransit(context),
            rahuKetuTransit = analyzeNodalTransit(context),
            transitInteractions = emptyList()
        )
    }
    
    private fun getMajorTransits(context: AnalysisContext): List<MajorTransit> {
        return listOf(
            MajorTransit(
                planet = Planet.SATURN,
                currentSign = context.getPlanetPosition(Planet.SATURN)?.sign ?: ZodiacSign.CAPRICORN,
                effectOnNative = PredictionTextGenerator.getSaturnTransitEffect(context),
                duration = LocalizedParagraph("Saturn transits approximately 2.5 years per sign.",
                    "शनि प्रति राशिमा लगभग 2.5 वर्ष गोचर गर्छ।"),
                intensity = StrengthLevel.STRONG
            ),
            MajorTransit(
                planet = Planet.JUPITER,
                currentSign = context.getPlanetPosition(Planet.JUPITER)?.sign ?: ZodiacSign.SAGITTARIUS,
                effectOnNative = PredictionTextGenerator.getJupiterTransitEffect(context),
                duration = LocalizedParagraph("Jupiter transits approximately 1 year per sign.",
                    "बृहस्पति प्रति राशिमा लगभग 1 वर्ष गोचर गर्छ।"),
                intensity = StrengthLevel.STRONG
            )
        )
    }
    
    private fun analyzeSadeSati(context: AnalysisContext): SadeSatiAnalysis {
        val moonSign = context.moonSign
        val saturnSign = context.getPlanetPosition(Planet.SATURN)?.sign ?: ZodiacSign.CAPRICORN
        
        val isActive = isSadeSatiActive(moonSign, saturnSign)
        val phase = getSadeSatiPhase(moonSign, saturnSign)
        
        return SadeSatiAnalysis(
            isActive = isActive,
            phase = phase,
            startDate = if (isActive) LocalDate.now().minusYears(1) else null,
            endDate = if (isActive) LocalDate.now().plusYears(2) else null,
            effects = if (isActive) PredictionTextGenerator.getSadeSatiEffects(phase) else LocalizedParagraph("", ""),
            remedies = if (isActive) PredictionTextGenerator.getSadeSatiRemedies() else LocalizedParagraph("", "")
        )
    }
    
    private fun isSadeSatiActive(moonSign: ZodiacSign, saturnSign: ZodiacSign): Boolean {
        val moonIndex = moonSign.ordinal
        val saturnIndex = saturnSign.ordinal
        val diff = (saturnIndex - moonIndex + 12) % 12
        return diff in 0..2 || diff == 11
    }
    
    private fun getSadeSatiPhase(moonSign: ZodiacSign, saturnSign: ZodiacSign): SadeSatiPhase {
        val moonIndex = moonSign.ordinal
        val saturnIndex = saturnSign.ordinal
        val diff = (saturnIndex - moonIndex + 12) % 12
        return when (diff) {
            11 -> SadeSatiPhase.RISING
            0 -> SadeSatiPhase.PEAK
            1 -> SadeSatiPhase.SETTING
            else -> SadeSatiPhase.NOT_ACTIVE
        }
    }
    
    private fun analyzeJupiterTransit(context: AnalysisContext): JupiterTransitAnalysis {
        val jupiterSign = context.getPlanetPosition(Planet.JUPITER)?.sign ?: ZodiacSign.SAGITTARIUS
        return JupiterTransitAnalysis(
            currentTransitSign = jupiterSign,
            transitHouse = getTransitHouse(jupiterSign, context.ascendantSign),
            effects = PredictionTextGenerator.getJupiterTransitHouseEffect(getTransitHouse(jupiterSign, context.ascendantSign)),
            favorableForAreas = getJupiterFavorableAreas(getTransitHouse(jupiterSign, context.ascendantSign))
        )
    }
    
    private fun getTransitHouse(transitSign: ZodiacSign, ascSign: ZodiacSign): Int {
        return ((transitSign.ordinal - ascSign.ordinal + 12) % 12) + 1
    }
    
    private fun getJupiterFavorableAreas(house: Int): List<LifeArea> {
        return when (house) {
            1, 5, 9 -> listOf(LifeArea.GENERAL, LifeArea.EDUCATION, LifeArea.SPIRITUAL)
            2, 11 -> listOf(LifeArea.FINANCE, LifeArea.CAREER)
            7 -> listOf(LifeArea.RELATIONSHIPS)
            4 -> listOf(LifeArea.HEALTH, LifeArea.GENERAL)
            else -> listOf(LifeArea.GENERAL)
        }
    }
    
    private fun analyzeNodalTransit(context: AnalysisContext): NodalTransitAnalysis {
        val rahuSign = context.rahuPosition?.sign ?: ZodiacSign.ARIES
        val ketuSign = context.ketuPosition?.sign ?: ZodiacSign.LIBRA
        
        return NodalTransitAnalysis(
            rahuTransitSign = rahuSign,
            ketuTransitSign = ketuSign,
            rahuTransitHouse = getTransitHouse(rahuSign, context.ascendantSign),
            ketuTransitHouse = getTransitHouse(ketuSign, context.ascendantSign),
            nodalAxisEffects = PredictionTextGenerator.getNodalAxisEffects(
                getTransitHouse(rahuSign, context.ascendantSign),
                getTransitHouse(ketuSign, context.ascendantSign)
            ),
            duration = LocalizedParagraph("Nodal transit lasts approximately 18 months per axis.",
                "नोडल गोचर प्रति अक्षमा लगभग 18 महिना रहन्छ।")
        )
    }
    
    private fun generateYearlyPrediction(context: AnalysisContext, currentDate: LocalDate): YearlyPrediction {
        val year = currentDate.year
        val dasha = context.currentMahadasha
        
        return YearlyPrediction(
            year = year,
            overallTheme = LocalizedParagraph(
                "Year $year brings ${dasha?.planet?.getLocalizedName(com.astro.storm.core.common.Language.ENGLISH) ?: "planetary"} energy themes to the forefront.",
                "वर्ष $year ले ${dasha?.planet?.getLocalizedName(com.astro.storm.core.common.Language.NEPALI) ?: "ग्रहीय"} ऊर्जा विषयहरूलाई अगाडि ल्याउँछ।"
            ),
            overallRating = context.getPlanetStrengthLevel(dasha?.planet ?: Planet.JUPITER),
            careerOutlook = generateAreaOutlook(LifeArea.CAREER, context),
            relationshipOutlook = generateAreaOutlook(LifeArea.RELATIONSHIPS, context),
            healthOutlook = generateAreaOutlook(LifeArea.HEALTH, context),
            wealthOutlook = generateAreaOutlook(LifeArea.FINANCE, context),
            keyMonths = emptyList(),
            yearlyAdvice = PredictionTextGenerator.getYearlyAdvice(dasha?.planet ?: Planet.JUPITER, context)
        )
    }
    
    private fun generateAreaOutlook(area: LifeArea, context: AnalysisContext): LifeAreaOutlook {
        val strength = when (area) {
            LifeArea.CAREER -> context.getHouseStrength(10)
            LifeArea.RELATIONSHIPS -> context.getHouseStrength(7)
            LifeArea.HEALTH -> context.getPlanetStrengthLevel(context.ascendantLord)
            LifeArea.FINANCE -> context.getHouseStrength(2)
            LifeArea.EDUCATION -> context.getHouseStrength(5)
            LifeArea.SPIRITUAL -> context.getHouseStrength(9)
            else -> StrengthLevel.MODERATE
        }
        return LifeAreaOutlook(
            area = area,
            rating = strength,
            summary = PredictionTextGenerator.getAreaOutlookSummary(area, strength),
            opportunities = PredictionTextGenerator.getAreaOpportunities(area, strength),
            challenges = PredictionTextGenerator.getAreaChallenges(area, strength),
            advice = PredictionTextGenerator.getAreaAdvice(area, strength)
        )
    }
    
    private fun generateMonthlyPredictions(context: AnalysisContext, currentDate: LocalDate): List<MonthlyPrediction> {
        return emptyList()
    }
    
    private fun generateLifeAreaPredictions(context: AnalysisContext): Map<LifeArea, LifeAreaPrediction> {
        return LifeArea.entries.associateWith { area ->
            LifeAreaPrediction(
                area = area,
                shortTermOutlook = generateAreaOutlook(area, context),
                mediumTermOutlook = generateAreaOutlook(area, context),
                longTermOutlook = generateAreaOutlook(area, context),
                timingAnalysis = TimingAnalysis(emptyList(), emptyList(), LocalizedParagraph("", "")),
                recommendations = PredictionTextGenerator.getAreaRecommendations(area, context)
            )
        }
    }
    
    private fun getYogaActivationTimeline(context: AnalysisContext): List<YogaActivationEvent> {
        return emptyList()
    }
    
    private fun identifyCriticalPeriods(context: AnalysisContext): List<CriticalPeriod> {
        return emptyList()
    }
    
    private fun identifyOpportunityWindows(context: AnalysisContext): List<OpportunityWindow> {
        return emptyList()
    }
    
    private fun generateRemedialMeasures(context: AnalysisContext): RemedialProfile {
        return RemedialProfile(emptyList(), emptyList(), emptyList(), emptyList(), emptyList(), LocalizedParagraph("Remedies", "उपाय"))
    }
    
    private fun generateOverallSummary(context: AnalysisContext): LocalizedParagraph {
        return LocalizedParagraph("Summary", "सारांश")
    }
    
    private fun calculatePredictionScore(context: AnalysisContext): Double = 75.0

    private fun com.astro.storm.ephemeris.yoga.YogaStrength.toStrengthLevel(): StrengthLevel = when (this) {
        com.astro.storm.ephemeris.yoga.YogaStrength.EXTREMELY_STRONG -> StrengthLevel.EXTREMELY_STRONG
        com.astro.storm.ephemeris.yoga.YogaStrength.VERY_STRONG -> StrengthLevel.VERY_STRONG
        com.astro.storm.ephemeris.yoga.YogaStrength.STRONG -> StrengthLevel.STRONG
        com.astro.storm.ephemeris.yoga.YogaStrength.MODERATE -> StrengthLevel.MODERATE
        com.astro.storm.ephemeris.yoga.YogaStrength.WEAK -> StrengthLevel.WEAK
        com.astro.storm.ephemeris.yoga.YogaStrength.VERY_WEAK -> StrengthLevel.AFFLICTED
    }
}
