package com.astro.storm.ephemeris.deepanalysis.predictions

import com.astro.storm.core.model.Planet
import com.astro.storm.core.model.VedicChart
import com.astro.storm.core.model.ZodiacSign
import com.astro.storm.ephemeris.deepanalysis.*
import com.astro.storm.ephemeris.VedicAstrologyUtils
import com.astro.storm.ephemeris.DashaCalculator
import java.time.LocalDate
import java.time.LocalDateTime

/**
 * Deep Predictions Engine - Comprehensive predictions based on Dasha, Transit, and Yogas
 * 
 * Generates detailed predictions for all life areas with timing accuracy.
 */
object DeepPredictionEngine {
    
    fun generatePredictions(
        chart: VedicChart, 
        context: AnalysisContext, 
        ephemerisEngine: com.astro.storm.ephemeris.SwissEphemerisEngine,
        triplePillarEngine: com.astro.storm.ephemeris.triplepillar.TriplePillarSynthesisEngine
    ): DeepPredictions {
        val currentDate = LocalDate.now()
        
        // 1. Generate Triple Pillar Synthesis
        val synthesis = triplePillarEngine.generateSynthesis(chart)
        
        // 2. Map to DeepPredictions
        return DeepPredictions(
            dashaAnalysis = analyzeDashaSystem(context),
            transitAnalysis = analyzeTransitsMap(context, synthesis.transitPillar),
            yearlyPrediction = generateYearlyPrediction(context, currentDate),
            monthlyPredictions = generateMonthlyPredictions(context, currentDate),
            lifeAreaPredictions = mapLifeAreaPredictions(synthesis.lifeAreaPredictions, context),
            yogaActivationTimeline = getYogaActivationTimeline(context),
            criticalPeriods = mapCriticalPeriods(synthesis.cautionPeriods, synthesis.transitPillar),
            opportunityWindows = mapOpportunityWindows(synthesis.peakPeriods),
            remedialMeasures = generateRemedialMeasures(context),
            overallPredictionSummary = LocalizedParagraph(
                "Overall success probability: ${(synthesis.overallSuccessProbability * 100).toInt()}%. ${synthesis.transitPillar.toPillarContribution().interpretation}",
                "समग्र सफलता सम्भावना: ${(synthesis.overallSuccessProbability * 100).toInt()}%। ${synthesis.transitPillar.toPillarContribution().interpretation}"
            ),
            predictionScore = synthesis.overallSuccessProbability * 100
        )
    }
    



    private fun analyzeDashaSystem(context: AnalysisContext): DashaDeepAnalysis {
        val currentMaha = context.currentMahadasha
        val currentAntar = context.currentMahadasha?.antardashas?.find { 
            val now = LocalDate.now()
            !it.startDate.isAfter(now) && !it.endDate.isBefore(now)
        }

        return DashaDeepAnalysis(
            currentMahadasha = currentMaha?.let { mah ->
                MahadashaDeepAnalysis(
                    planet = mah.planet,
                    startDate = mah.startDate,
                    endDate = mah.endDate,
                    overallTheme = LocalizedParagraph(
                        "${mah.planet.getLocalizedName(com.astro.storm.core.common.Language.ENGLISH)} dasha: Focus on ${mah.planet.getLocalizedName(com.astro.storm.core.common.Language.ENGLISH)} significations.",
                        "${mah.planet.getLocalizedName(com.astro.storm.core.common.Language.NEPALI)} को महादशा: ${mah.planet.getLocalizedName(com.astro.storm.core.common.Language.NEPALI)} को क्षेत्रमा ध्यान दिनुहोस्।"
                    ),
                    lifeAreaEffects = emptyMap(),
                    strengths = emptyList(),
                    challenges = emptyList(),
                    advice = LocalizedParagraph("Follow the planetary energy.", "ग्रहीय ऊर्जाको पालना गर्नुहोस्।")
                )
            },
            currentAntardasha = currentAntar?.let { ant ->
                AntardashaDeepAnalysis(
                    planet = ant.planet,
                    startDate = ant.startDate,
                    endDate = ant.endDate,
                    refinedTheme = LocalizedParagraph("Refining focus through ${ant.planet.getLocalizedName(com.astro.storm.core.common.Language.ENGLISH)}.", "अन्त्यर्दशा मार्फत थप ध्यान।"),
                    shortTermEffects = emptyList(),
                    activatedYogas = emptyList()
                )
            },
            upcomingDashas = emptyList(),
            dashaBalance = LocalizedParagraph("Balanced dasha influence.", "सन्तुलित दशा प्रभाव।")
        )
    }

    private fun analyzeTransitsMap(context: AnalysisContext, transitAnalysis: com.astro.storm.ephemeris.triplepillar.TransitPillarAnalysis): TransitDeepAnalysis {
        return TransitDeepAnalysis(
            majorTransits = getMajorTransits(transitAnalysis),
            saturnSadeSati = analyzeSadeSati(context, transitAnalysis),
            jupiterTransit = analyzeJupiterTransit(context, transitAnalysis),
            rahuKetuTransit = analyzeNodalTransit(context, transitAnalysis),
            transitInteractions = getTransitInteractions(transitAnalysis)
        )
    }

    private fun getMajorTransits(transitAnalysis: com.astro.storm.ephemeris.triplepillar.TransitPillarAnalysis): List<MajorTransit> {
        return transitAnalysis.significantTransits.map { sig ->
            val position = transitAnalysis.transitPositions[sig.planet]
            MajorTransit(
                planet = sig.planet,
                currentSign = position?.sign ?: ZodiacSign.ARIES,
                effectOnNative = LocalizedParagraph(sig.description, sig.description),
                duration = LocalizedParagraph(sig.duration, sig.duration),
                intensity = StrengthLevel.fromDouble(kotlin.math.abs(sig.impact) * 100.0)
            )
        }
    }

    private fun analyzeSadeSati(context: AnalysisContext, transitAnalysis: com.astro.storm.ephemeris.triplepillar.TransitPillarAnalysis): SadeSatiAnalysis {
        val moonSign = context.moonSign
        val saturnPos = transitAnalysis.transitPositions[Planet.SATURN]
        val saturnSign = saturnPos?.sign ?: ZodiacSign.CAPRICORN
        
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

    private fun analyzeJupiterTransit(context: AnalysisContext, transitAnalysis: com.astro.storm.ephemeris.triplepillar.TransitPillarAnalysis): JupiterTransitAnalysis {
        val jupiterPos = transitAnalysis.transitPositions[Planet.JUPITER]
        val jupiterSign = jupiterPos?.sign ?: ZodiacSign.SAGITTARIUS
        return JupiterTransitAnalysis(
            currentTransitSign = jupiterSign,
            transitHouse = getTransitHouse(jupiterSign, context.ascendantSign),
            effects = PredictionTextGenerator.getJupiterTransitHouseEffect(getTransitHouse(jupiterSign, context.ascendantSign)),
            favorableForAreas = getJupiterFavorableAreas(getTransitHouse(jupiterSign, context.ascendantSign))
        )
    }

    private fun analyzeNodalTransit(context: AnalysisContext, transitAnalysis: com.astro.storm.ephemeris.triplepillar.TransitPillarAnalysis): NodalTransitAnalysis {
        val rahuPos = transitAnalysis.transitPositions[Planet.RAHU]
        val ketuPos = transitAnalysis.transitPositions[Planet.KETU]
        
        val rahuSign = rahuPos?.sign ?: ZodiacSign.ARIES
        val ketuSign = ketuPos?.sign ?: ZodiacSign.entries[(rahuSign.ordinal + 6) % 12]
        
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

    
    // --- Mapping Functions ---

    private fun mapLifeAreaPredictions(
        source: Map<com.astro.storm.ephemeris.triplepillar.LifeAreaCategory, com.astro.storm.ephemeris.triplepillar.LifeAreaPrediction>, 
        context: AnalysisContext
    ): Map<LifeArea, LifeAreaPrediction> {
        val result = mutableMapOf<LifeArea, LifeAreaPrediction>()
        
        source.forEach { (category, prediction) ->
            val targetArea = when(category) {
                com.astro.storm.ephemeris.triplepillar.LifeAreaCategory.CAREER -> LifeArea.CAREER
                com.astro.storm.ephemeris.triplepillar.LifeAreaCategory.WEALTH -> LifeArea.WEALTH
                com.astro.storm.ephemeris.triplepillar.LifeAreaCategory.RELATIONSHIP -> LifeArea.RELATIONSHIP
                com.astro.storm.ephemeris.triplepillar.LifeAreaCategory.HEALTH -> LifeArea.HEALTH
                com.astro.storm.ephemeris.triplepillar.LifeAreaCategory.EDUCATION -> LifeArea.EDUCATION
                com.astro.storm.ephemeris.triplepillar.LifeAreaCategory.SPIRITUALITY -> LifeArea.SPIRITUAL
                else -> return@forEach // Skip unmapped areas for now or map to General if needed
            }
            
            val strength = StrengthLevel.fromDouble(prediction.successProbability * 100)
            
            result[targetArea] = LifeAreaPrediction(
                area = targetArea,
                shortTermOutlook = LifeAreaOutlook(
                    area = targetArea,
                    rating = strength,
                    summary = LocalizedParagraph(prediction.briefInterpretation, prediction.briefInterpretation),
                    opportunities = prediction.opportunities.map { LocalizedTrait(it, it, StrengthLevel.STRONG) },
                    challenges = prediction.challenges.map { LocalizedTrait(it, it, StrengthLevel.WEAK) },
                    advice = LocalizedParagraph("Leverage strength during favorable periods.", "अनुकूल समयमा शक्तिको लाभ लिनुहोस्।")
                ),
                mediumTermOutlook = LifeAreaOutlook(
                    area = targetArea,
                    rating = strength,
                    summary = LocalizedParagraph("Medium term outlook: ${prediction.briefInterpretation}", "मध्यम अवधिको दृष्टिकोण: ${prediction.briefInterpretation}"),
                    opportunities = emptyList(),
                    challenges = emptyList(),
                    advice = LocalizedParagraph("Plan ahead.", "अगाडि योजना गर्नुहोस्।")
                ),
                longTermOutlook = LifeAreaOutlook(
                    area = targetArea,
                    rating = strength,
                    summary = LocalizedParagraph(prediction.detailedInterpretation.take(100) + "...", prediction.detailedInterpretation.take(100) + "..."),
                    opportunities = emptyList(),
                    challenges = emptyList(),
                    advice = LocalizedParagraph("Long term growth.", "दीर्घकालीन वृद्धि।")
                ),
                timingAnalysis = TimingAnalysis(
                    favorablePeriods = prediction.favorableTimings.map { 
                        TimingPeriod(it, it.plusMonths(1), LocalizedParagraph("Favorable", "अनुकूल"), StrengthLevel.STRONG) 
                    },
                    challengingPeriods = emptyList(),
                    peakPeriod = LocalizedParagraph(prediction.keyTransits.joinToString(), prediction.keyTransits.joinToString())
                ),
                recommendations = listOf(LocalizedParagraph(prediction.detailedInterpretation, prediction.detailedInterpretation))
            )
        }
        return result
    }

    private fun mapCriticalPeriods(
        cautionPeriods: List<com.astro.storm.ephemeris.triplepillar.PredictionTimeWindow>,
        transitPillar: com.astro.storm.ephemeris.triplepillar.TransitPillarAnalysis
    ): List<CriticalPeriod> {
        return cautionPeriods.take(3).map { window ->
            CriticalPeriod(
                periodName = LocalizedParagraph("Challenging Period", "चुनौतीपूर्ण अवधि"),
                startDate = window.startDate.toLocalDate(),
                endDate = window.endDate.toLocalDate(),
                areaAffected = LifeArea.GENERAL,
                nature = CriticalPeriodNature.CHALLENGING,
                intensity = StrengthLevel.STRONG,
                advice = LocalizedParagraph(
                    "Success probability is low (${(window.successProbability*100).toInt()}%). Proceed with caution. Influences: ${window.dominantInfluences.joinToString()}",
                     "सफलताको सम्भावना कम छ (${(window.successProbability*100).toInt()}%). सावधानीपूर्वक अघि बढ्नुहोस्।"
                )
            )
        } + identifySadeSatiCritical(transitPillar)
    }

    private fun identifySadeSatiCritical(transitPillar: com.astro.storm.ephemeris.triplepillar.TransitPillarAnalysis): List<CriticalPeriod> {
         // Re-use logic or manual check
         // Simplification: returns empty list as Sade Sati is handled in TransitDeepAnalysis
         return emptyList()
    }

    private fun mapOpportunityWindows(
        peakPeriods: List<com.astro.storm.ephemeris.triplepillar.PredictionTimeWindow>
    ): List<OpportunityWindow> {
        return peakPeriods.take(3).map { window ->
            OpportunityWindow(
                windowName = LocalizedParagraph("Golden Opportunity", "सुनौलो अवसर"),
                startDate = window.startDate.toLocalDate(),
                endDate = window.endDate.toLocalDate(),
                affectedAreas = listOf(LifeArea.GENERAL),
                opportunityType = LocalizedParagraph("High Success Probability", "उच्च सफलता सम्भावना"),
                intensity = StrengthLevel.VERY_STRONG,
                advice = LocalizedParagraph(
                    "Success probability is high (${(window.successProbability*100).toInt()}%). Take initiative! Influences: ${window.dominantInfluences.joinToString()}",
                    "सफलताको सम्भावना उच्च छ (${(window.successProbability*100).toInt()}%). पहल गर्नुहोस्!"
                )
            )
        }
    }
    
    private fun getTransitInteractions(transitAnalysis: com.astro.storm.ephemeris.triplepillar.TransitPillarAnalysis): List<TransitInteraction> {
        // Map significant transits that might be interactions? 
        // TransitPillarAnalyzer's significantTransits includes conjunctions.
        // We can filter them if needed or just return empty if the UI expects specific 'Interaction' objects not yet fully supported.
        // For now, let's keep it empty or simple to avoid type mismatch if models differ significantly.
        return emptyList() 
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
            relationshipOutlook = generateAreaOutlook(LifeArea.RELATIONSHIP, context),
            healthOutlook = generateAreaOutlook(LifeArea.HEALTH, context),
            wealthOutlook = generateAreaOutlook(LifeArea.WEALTH, context),
            keyMonths = getKeyMonths(context, year),
            yearlyAdvice = PredictionTextGenerator.getYearlyAdvice(dasha?.planet ?: Planet.JUPITER, context)
        )
    }
    
    private fun generateAreaOutlook(area: LifeArea, context: AnalysisContext): LifeAreaOutlook {
        val strength = when (area) {
            LifeArea.CAREER -> context.getHouseStrength(10)
            LifeArea.RELATIONSHIP -> context.getHouseStrength(7)
            LifeArea.HEALTH -> context.getPlanetStrengthLevel(context.ascendantLord)
            LifeArea.WEALTH -> context.getHouseStrength(2)
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
    
    private fun getKeyMonths(context: AnalysisContext, year: Int): List<KeyMonth> {
        val months = mutableListOf<KeyMonth>()
        
        // Add key months based on antardasha changes
        context.currentMahadasha?.antardashas?.forEach { antar ->
            if (antar.startDate.year == year) {
                months.add(KeyMonth(
                    month = antar.startDate.monthValue,
                    significance = LocalizedParagraph(
                        "${antar.planet.getLocalizedName(com.astro.storm.core.common.Language.ENGLISH)} antardasha begins - new sub-cycle starts.",
                        "${antar.planet.getLocalizedName(com.astro.storm.core.common.Language.NEPALI)} अन्तर्दशा सुरु हुन्छ - नयाँ उप-चक्र सुरु।"
                    ),
                    rating = context.getPlanetStrengthLevel(antar.planet)
                ))
            }
        }
        
        return months.take(4)
    }
    
    private fun generateMonthlyPredictions(context: AnalysisContext, currentDate: LocalDate): List<MonthlyPrediction> {
        return (0..2).map { monthOffset ->
            val month = currentDate.plusMonths(monthOffset.toLong())
            MonthlyPrediction(
                month = month.monthValue,
                year = month.year,
                overallRating = context.getPlanetStrengthLevel(context.currentMahadasha?.planet ?: Planet.JUPITER),
                summary = LocalizedParagraph(
                    "Month ${getMonthName(month.monthValue, com.astro.storm.core.common.Language.ENGLISH)} continues current dasha themes.",
                    "${getMonthName(month.monthValue, com.astro.storm.core.common.Language.NEPALI)} महिनाले वर्तमान दशा विषयहरू जारी राख्छ।"
                ),
                focusAreas = listOf(LifeArea.CAREER, LifeArea.HEALTH),
                favorableDates = listOf(1, 5, 9, 14, 18, 23, 27),
                challengingDates = listOf(8, 15, 22)
            )
        }
    }

    private fun getMonthName(month: Int, language: com.astro.storm.core.common.Language): String = when(month) {
        1 -> if (language == com.astro.storm.core.common.Language.NEPALI) "जनवरी" else "January"
        2 -> if (language == com.astro.storm.core.common.Language.NEPALI) "फेब्रुअरी" else "February"
        3 -> if (language == com.astro.storm.core.common.Language.NEPALI) "मार्च" else "March"
        4 -> if (language == com.astro.storm.core.common.Language.NEPALI) "अप्रिल" else "April"
        5 -> if (language == com.astro.storm.core.common.Language.NEPALI) "मे" else "May"
        6 -> if (language == com.astro.storm.core.common.Language.NEPALI) "जुन" else "June"
        7 -> if (language == com.astro.storm.core.common.Language.NEPALI) "जुलाई" else "July"
        8 -> if (language == com.astro.storm.core.common.Language.NEPALI) "अगस्ट" else "August"
        9 -> if (language == com.astro.storm.core.common.Language.NEPALI) "सेप्टेम्बर" else "September"
        10 -> if (language == com.astro.storm.core.common.Language.NEPALI) "अक्टोबर" else "October"
        11 -> if (language == com.astro.storm.core.common.Language.NEPALI) "नोभेम्बर" else "November"
        12 -> if (language == com.astro.storm.core.common.Language.NEPALI) "डिसेम्बर" else "December"
        else -> ""
    }
    
    private fun generateLifeAreaPredictions(context: AnalysisContext): Map<LifeArea, LifeAreaPrediction> {
        return LifeArea.entries.associateWith { area ->
            LifeAreaPrediction(
                area = area,
                shortTermOutlook = generateAreaOutlook(area, context),
                mediumTermOutlook = generateAreaOutlook(area, context),
                longTermOutlook = generateAreaOutlook(area, context),
                timingAnalysis = getAreaTimingAnalysis(area, context),
                recommendations = PredictionTextGenerator.getAreaRecommendations(area, context)
            )
        }
    }
    
    private fun getAreaTimingAnalysis(area: LifeArea, context: AnalysisContext): TimingAnalysis {
        return TimingAnalysis(
            favorablePeriods = listOf(
                TimingPeriod(
                    LocalDate.now(),
                    LocalDate.now().plusMonths(3),
                    LocalizedParagraph("Current period supports ${area.name.lowercase()} matters.",
                        "वर्तमान अवधिले ${area.name} मामिलाहरूलाई समर्थन गर्छ।"),
                    StrengthLevel.MODERATE
                )
            ),
            challengingPeriods = emptyList(),
            peakPeriod = LocalizedParagraph("Best results during strong dasha/transit combinations.",
                "बलियो दशा/गोचर संयोजनहरूमा सर्वोत्तम परिणामहरू।")
        )
    }
    
    private fun getYogaActivationTimeline(context: AnalysisContext): List<YogaActivationEvent> {
        return context.yogaAnalysis.allYogas.take(5).map { yoga ->
            YogaActivationEvent(
                yogaName = yoga.name,
                activationPeriod = LocalizedParagraph(
                    "During ${yoga.planets.firstOrNull()?.getLocalizedName(com.astro.storm.core.common.Language.ENGLISH) ?: "planetary"} dasha periods.",
                    "${yoga.planets.firstOrNull()?.getLocalizedName(com.astro.storm.core.common.Language.NEPALI) ?: "ग्रहीय"} दशा अवधिहरूमा।"
                ),
                expectedResults = LocalizedParagraph(
                    "${yoga.name} brings ${yoga.category.name.lowercase().replace("_", " ")} results.",
                    "${yoga.name}ले ${yoga.category.name} परिणामहरू ल्याउँछ।"
                ),
                activationStrength = yoga.strength.toStrengthLevel()
            )
        }
    }
    
    private fun identifyCriticalPeriods(context: AnalysisContext): List<CriticalPeriod> {
        val periods = mutableListOf<CriticalPeriod>()
        
        // Check for Sade Sati
        val moonSign = context.moonSign
        val saturnSign = context.getPlanetPosition(Planet.SATURN)?.sign ?: ZodiacSign.CAPRICORN
        if (isSadeSatiActive(moonSign, saturnSign)) {
            periods.add(CriticalPeriod(
                periodName = LocalizedParagraph("Sade Sati Active", "साढे साती सक्रिय"),
                startDate = LocalDate.now().minusYears(1),
                endDate = LocalDate.now().plusYears(2),
                areaAffected = LifeArea.GENERAL,
                nature = CriticalPeriodNature.CHALLENGING,
                intensity = StrengthLevel.STRONG,
                advice = PredictionTextGenerator.getSadeSatiAdvice()
            ))
        }
        
        return periods
    }
    
    private fun identifyOpportunityWindows(context: AnalysisContext): List<OpportunityWindow> {
        val windows = mutableListOf<OpportunityWindow>()
        
        // Jupiter in trines to Moon
        val jupiterHouse = getTransitHouse(
            context.getPlanetPosition(Planet.JUPITER)?.sign ?: ZodiacSign.SAGITTARIUS,
            context.ascendantSign
        )
        if (jupiterHouse in listOf(1, 5, 9)) {
            windows.add(OpportunityWindow(
                windowName = LocalizedParagraph("Jupiter in Trine", "त्रिकोणमा बृहस्पति"),
                startDate = LocalDate.now(),
                endDate = LocalDate.now().plusMonths(6),
                affectedAreas = listOf(LifeArea.GENERAL, LifeArea.EDUCATION, LifeArea.SPIRITUAL),
                opportunityType = LocalizedParagraph("Growth and expansion", "वृद्धि र विस्तार"),
                intensity = StrengthLevel.STRONG,
                advice = LocalizedParagraph("Maximize learning and spiritual growth during Jupiter's favorable transit.",
                    "बृहस्पतिको अनुकूल गोचरमा सिकाइ र आध्यात्मिक विकास अधिकतम गर्नुहोस्।")
            ))
        }
        
        return windows
    }
    
    private fun generateRemedialMeasures(context: AnalysisContext): RemedialProfile {
        val weakPlanets = Planet.entries.filter { 
            it != Planet.RAHU && it != Planet.KETU && 
            context.getPlanetStrengthLevel(it) <= StrengthLevel.WEAK 
        }
        
        val gemstones = weakPlanets.take(2).map { planet ->
            GemstoneRemedy(
                planet = planet,
                primaryGemstone = getGemstone(planet),
                alternativeGemstone = getAlternativeGemstone(planet),
                wearingGuidelines = LocalizedParagraph(
                    "Wear on ${getGemstoneDay(planet).en} after proper energization.",
                    "उचित ऊर्जीकरण पछि ${getGemstoneDay(planet).ne} मा लगाउनुहोस्।"
                ),
                cautions = if (planet == Planet.SATURN || planet == Planet.MARS) 
                    LocalizedParagraph("Consult astrologer before wearing.", "लगाउनु अभि ज्योतिषीसँग परामर्श गर्नुहोस्।") 
                    else LocalizedParagraph("", "")
            )
        }
        
        val mantras = weakPlanets.take(3).map { planet ->
            MantraRemedy(
                planet = planet,
                beejaMantra = getBeejaMantra(planet),
                fullMantra = getFullMantra(planet),
                chantCount = getMantraCount(planet),
                bestTime = LocalizedParagraph(
                    "Best chanted during ${getMantraTime(planet).en}.",
                    "${getMantraTime(planet).ne}मा जप गर्नु उत्तम।"
                )
            )
        }
        
        return RemedialProfile(
            gemstoneRemedies = gemstones,
            mantraRemedies = mantras,
            charitableRemedies = getCharitableRemedies(weakPlanets),
            fastingRemedies = getFastingRemedies(weakPlanets),
            yogicRemedies = getYogicRemedies(context),
            overallRemedialAdvice = LocalizedParagraph(
                "Focus on strengthening ${weakPlanets.firstOrNull()?.getLocalizedName(com.astro.storm.core.common.Language.ENGLISH) ?: "weak planets"} through consistent practice.",
                "${weakPlanets.firstOrNull()?.getLocalizedName(com.astro.storm.core.common.Language.NEPALI) ?: "कमजोर ग्रहहरू"}लाई निरन्तर अभ्यास मार्फत बलियो बनाउनमा ध्यान दिनुहोस्।"
            )
        )
    }
    
    private fun getGemstone(planet: Planet): LocalizedParagraph = when (planet) {
        Planet.SUN -> LocalizedParagraph("Ruby (Manik)", "माणिक (Ruby)")
        Planet.MOON -> LocalizedParagraph("Pearl (Moti)", "मोती (Pearl)")
        Planet.MARS -> LocalizedParagraph("Red Coral (Moonga)", "मुगा (Red Coral)")
        Planet.MERCURY -> LocalizedParagraph("Emerald (Panna)", "पन्ना (Emerald)")
        Planet.JUPITER -> LocalizedParagraph("Yellow Sapphire (Pukhraj)", "पुखराज (Yellow Sapphire)")
        Planet.VENUS -> LocalizedParagraph("Diamond (Heera)", "हीरा (Diamond)")
        Planet.SATURN -> LocalizedParagraph("Blue Sapphire (Neelam)", "नीलम (Blue Sapphire)")
        Planet.RAHU, Planet.TRUE_NODE -> LocalizedParagraph("Hessonite (Gomed)", "गोमेद (Hessonite)")
        Planet.KETU -> LocalizedParagraph("Cat's Eye (Lehsunia)", "लहसुनिया (Cat's Eye)")
        else -> LocalizedParagraph("Consult astrologer", "ज्योतिषीसँग परामर्श गर्नुहोस्")
    }
    
    private fun getAlternativeGemstone(planet: Planet): LocalizedParagraph = when (planet) {
        Planet.SUN -> LocalizedParagraph("Garnet", "गार्नेट")
        Planet.MOON -> LocalizedParagraph("Moonstone", "मुनस्टोन")
        Planet.MARS -> LocalizedParagraph("Carnelian", "कार्नेलियन")
        Planet.MERCURY -> LocalizedParagraph("Peridot", "पेरिडोट")
        Planet.JUPITER -> LocalizedParagraph("Citrine", "सिट्रिन")
        Planet.VENUS -> LocalizedParagraph("White Sapphire", "सेतो नीलम")
        Planet.SATURN -> LocalizedParagraph("Amethyst", "एमेथिस्ट")
        else -> LocalizedParagraph("", "")
    }
    
    private fun getGemstoneDay(planet: Planet): LocalizedParagraph = when (planet) {
        Planet.SUN -> LocalizedParagraph("Sunday", "आइतबार")
        Planet.MOON -> LocalizedParagraph("Monday", "सोमबार")
        Planet.MARS -> LocalizedParagraph("Tuesday", "मंगलबार")
        Planet.MERCURY -> LocalizedParagraph("Wednesday", "बुधबार")
        Planet.JUPITER -> LocalizedParagraph("Thursday", "बिहीबार")
        Planet.VENUS -> LocalizedParagraph("Friday", "शुक्रबार")
        Planet.SATURN -> LocalizedParagraph("Saturday", "शनिबार")
        Planet.RAHU, Planet.TRUE_NODE -> LocalizedParagraph("Saturday", "शनिबार")
        Planet.KETU -> LocalizedParagraph("Tuesday", "मंगलबार")
        else -> LocalizedParagraph("auspicious day", "शुभ दिन")
    }
    
    private fun getBeejaMantra(planet: Planet): String = when (planet) {
        Planet.SUN -> "Om Hraam Hreem Hraum Sah Suryaya Namah"
        Planet.MOON -> "Om Shram Shreem Shraum Sah Chandraya Namah"
        Planet.MARS -> "Om Kram Kreem Kraum Sah Bhaumaya Namah"
        Planet.MERCURY -> "Om Bram Breem Braum Sah Budhaya Namah"
        Planet.JUPITER -> "Om Gram Greem Graum Sah Gurave Namah"
        Planet.VENUS -> "Om Dram Dreem Draum Sah Shukraya Namah"
        Planet.SATURN -> "Om Pram Preem Praum Sah Shanaye Namah"
        Planet.RAHU, Planet.TRUE_NODE -> "Om Bhram Bhreem Bhraum Sah Rahave Namah"
        Planet.KETU -> "Om Sram Sreem Sraum Sah Ketave Namah"
        else -> "Om Namah Shivaya"
    }
    
    private fun getFullMantra(planet: Planet): String = when (planet) {
        Planet.SUN -> "Om Hram Hreem Hroum Sah Suryaya Namah"
        Planet.MOON -> "Om Som Somaya Namah"
        Planet.MARS -> "Om Ang Angarkaya Namah"
        Planet.MERCURY -> "Om Bum Budhaya Namah"
        Planet.JUPITER -> "Om Brim Brihaspataye Namah"
        Planet.VENUS -> "Om Shum Shukraya Namah"
        Planet.SATURN -> "Om Sham Shanaishcharaya Namah"
        Planet.RAHU, Planet.TRUE_NODE -> "Om Ram Rahave Namah"
        Planet.KETU -> "Om Kem Ketave Namah"
        else -> getBeejaMantra(planet)
    }
    
    private fun getMantraCount(planet: Planet): Int = when (planet) {
        Planet.SUN -> 7000
        Planet.MOON -> 11000
        Planet.MARS -> 10000
        Planet.MERCURY -> 9000
        Planet.JUPITER -> 19000
        Planet.VENUS -> 16000
        Planet.SATURN -> 23000
        Planet.RAHU, Planet.TRUE_NODE -> 18000
        Planet.KETU -> 17000
        else -> 108
    }
    
    private fun getMantraTime(planet: Planet): LocalizedParagraph = when (planet) {
        Planet.SUN -> LocalizedParagraph("sunrise", "सूर्योदय")
        Planet.MOON -> LocalizedParagraph("evening", "साँझ")
        Planet.JUPITER -> LocalizedParagraph("morning", "बिहान")
        Planet.SATURN, Planet.RAHU, Planet.KETU -> LocalizedParagraph("night", "रात")
        else -> LocalizedParagraph("dawn or dusk", "बिहान वा साँझ")
    }
    
    private fun getCharitableRemedies(planets: List<Planet>): List<CharitableRemedy> {
        return planets.take(2).map { planet ->
            CharitableRemedy(
                planet = planet,
                donationItems = getDonationItems(planet),
                bestDay = getGemstoneDay(planet),
                guidelines = LocalizedParagraph(
                    "Donate with devotion on ${getGemstoneDay(planet).en}.",
                    "${getGemstoneDay(planet).ne}मा भक्तिको साथ दान गर्नुहोस्।"
                )
            )
        }
    }
    
    private fun getDonationItems(planet: Planet): LocalizedParagraph = when (planet) {
        Planet.SUN -> LocalizedParagraph("Wheat, jaggery, copper", "गहुँ, सख्खर, तामा")
        Planet.MOON -> LocalizedParagraph("Rice, milk, white cloth", "चामल, दूध, सेतो कपडा")
        Planet.MARS -> LocalizedParagraph("Red lentils, red cloth", "रातो दाल, रातो कपडा")
        Planet.MERCURY -> LocalizedParagraph("Green vegetables, books", "हरियो सागपात, पुस्तकहरू")
        Planet.JUPITER -> LocalizedParagraph("Yellow cloth, turmeric, bananas", "पहेंलो कपडा, बेसार, केरा")
        Planet.VENUS -> LocalizedParagraph("White items, perfume, sweets", "सेतो वस्तुहरू, अत्तर, मिठाई")
        Planet.SATURN -> LocalizedParagraph("Black sesame, iron, mustard oil", "कालो तिल, फलाम, तोरीको तेल")
        Planet.RAHU, Planet.TRUE_NODE -> LocalizedParagraph("Blankets, lead, black pulses", "कम्बल, सिसा, कालो मास")
        Planet.KETU -> LocalizedParagraph("Seven types of grains, multi-colored cloth", "सप्तधान्य, बहुरंगी कपडा")
        else -> LocalizedParagraph("Food to needy", "असहायलाई खाना")
    }
    
    private fun getFastingRemedies(planets: List<Planet>): List<FastingRemedy> {
        return planets.take(1).map { planet ->
            val donationItem = getDonationItems(planet)
            FastingRemedy(
                planet = planet,
                fastingDay = getGemstoneDay(planet),
                fastingType = LocalizedParagraph("Water or milk fast until sunset.",
                    "सूर्यास्त सम्म पानी वा दूध उपवास।"),
                guidelines = LocalizedParagraph(
                    "Observe fast with devotion and break with ${donationItem.en.split(",").first().trim()}.",
                    "भक्तिको साथ उपवास गर्नुहोस् र ${donationItem.ne.split(",").first().trim()}को साथ व्रत खोल्नुहोस्।"
                )
            )
        }
    }
    
    private fun getYogicRemedies(context: AnalysisContext): List<YogicRemedy> {
        val dominantElement = context.getDominantElement()
        return listOf(
            YogicRemedy(
                practiceName = when (dominantElement) {
                    Element.FIRE -> LocalizedParagraph("Surya Namaskar", "सूर्य नमस्कार")
                    Element.EARTH -> LocalizedParagraph("Grounding Asanas", "ग्राउन्डिङ आसनहरू")
                    Element.AIR -> LocalizedParagraph("Pranayama", "प्राणायाम")
                    Element.WATER -> LocalizedParagraph("Moon Salutation", "चन्द्र नमस्कार")
                },
                targetPlanet = when (dominantElement) {
                    Element.FIRE -> Planet.SUN
                    Element.EARTH -> Planet.SATURN
                    Element.AIR -> Planet.MERCURY
                    Element.WATER -> Planet.MOON
                },
                guidelines = LocalizedParagraph("Practice daily for best results.",
                    "सर्वोत्तम परिणामहरूको लागि दैनिक अभ्यास गर्नुहोस्।"),
                benefits = LocalizedParagraph("Balances elemental energies and strengthens related planet.",
                    "तात्विक ऊर्जाहरू सन्तुलित गर्छ र सम्बन्धित ग्रहलाई बलियो बनाउँछ।")
            )
        )
    }
    
    private fun generateOverallSummary(context: AnalysisContext): LocalizedParagraph {
        val dasha = context.currentMahadasha
        val strength = context.getPlanetStrengthLevel(dasha?.planet ?: Planet.JUPITER)
        
        return LocalizedParagraph(
            en = "Your current ${dasha?.planet?.getLocalizedName(com.astro.storm.core.common.Language.ENGLISH) ?: "planetary"} period shows ${strength.displayName.lowercase()} influence. " +
                "Focus on ${if (strength >= StrengthLevel.STRONG) "maximizing opportunities" else "building foundations"} " +
                "while attending to health and relationships. The coming period favors " +
                "${if (context.rajaYogas.isNotEmpty()) "achievement and recognition" else "steady growth and development"}.",
            ne = "तपाईंको वर्तमान ${dasha?.planet?.getLocalizedName(com.astro.storm.core.common.Language.NEPALI) ?: "ग्रहीय"} अवधिले ${strength.displayNameNe} प्रभाव देखाउँछ। " +
                "${if (strength >= StrengthLevel.STRONG) "अवसरहरूलाई अधिकतम बनाउन" else "आधारहरू निर्माण गर्न"} ध्यान दिनुहोस् " +
                "स्वास्थ्य र सम्बन्धहरूमा ध्यान दिँदै।"
        )
    }
    
    private fun isSadeSatiActive(moonSign: ZodiacSign, saturnSign: ZodiacSign): Boolean {
        val houseFromMoon = getTransitHouse(saturnSign, moonSign)
        return houseFromMoon in listOf(12, 1, 2)
    }

    private fun getSadeSatiPhase(moonSign: ZodiacSign, saturnSign: ZodiacSign): SadeSatiPhase {
        val houseFromMoon = getTransitHouse(saturnSign, moonSign)
        return when (houseFromMoon) {
            12 -> SadeSatiPhase.RISING
            1 -> SadeSatiPhase.PEAK
            2 -> SadeSatiPhase.SETTING
            else -> SadeSatiPhase.NOT_ACTIVE
        }
    }

    private fun getTransitHouse(transitSign: ZodiacSign, natalAscendantSign: ZodiacSign): Int {
        return VedicAstrologyUtils.getHouseFromSigns(transitSign, natalAscendantSign)
    }

    private fun getJupiterFavorableAreas(house: Int): List<LifeArea> = when (house) {
        1 -> listOf(LifeArea.GENERAL, LifeArea.HEALTH)
        2 -> listOf(LifeArea.WEALTH)
        5 -> listOf(LifeArea.EDUCATION)
        7 -> listOf(LifeArea.RELATIONSHIP)
        9 -> listOf(LifeArea.SPIRITUAL, LifeArea.EDUCATION)
        10 -> listOf(LifeArea.CAREER)
        11 -> listOf(LifeArea.WEALTH, LifeArea.GENERAL)
        else -> emptyList()
    }

    private fun getSadeSatiAdvice(): LocalizedParagraph {
        return LocalizedParagraph(
            "During Sade Sati, practice patience and perform Shani remedies. Success comes through discipline.",
            "साढे सातीको समयमा धैर्यता अभ्यास गर्नुहोस् र शनि उपचार गर्नुहोस्। सफलता अनुशासनबाट आउँछ।"
        )
    }

    
    private fun com.astro.storm.ephemeris.yoga.YogaStrength.toStrengthLevel(): StrengthLevel = when (this) {
        com.astro.storm.ephemeris.yoga.YogaStrength.EXTREMELY_STRONG -> StrengthLevel.EXTREMELY_STRONG
        com.astro.storm.ephemeris.yoga.YogaStrength.VERY_STRONG -> StrengthLevel.VERY_STRONG
        com.astro.storm.ephemeris.yoga.YogaStrength.STRONG -> StrengthLevel.STRONG
        com.astro.storm.ephemeris.yoga.YogaStrength.MODERATE -> StrengthLevel.MODERATE
        com.astro.storm.ephemeris.yoga.YogaStrength.WEAK -> StrengthLevel.WEAK
        com.astro.storm.ephemeris.yoga.YogaStrength.VERY_WEAK -> StrengthLevel.AFFLICTED
    }
}
