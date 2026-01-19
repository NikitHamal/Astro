package com.astro.storm.ephemeris.deepanalysis.predictions

import com.astro.storm.core.model.Planet
import com.astro.storm.core.model.VedicChart
import com.astro.storm.core.model.ZodiacSign
import com.astro.storm.ephemeris.deepanalysis.*
import com.astro.storm.ephemeris.DashaCalculator
import java.time.LocalDate
import java.time.LocalDateTime

/**
 * Deep Predictions Engine - Comprehensive predictions based on Dasha, Transit, and Yogas
 * 
 * Generates detailed predictions for all life areas with timing accuracy.
 */
object DeepPredictionEngine {
    
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
            MahadashaDeepAnalysis(
                planet = it.planet,
                startDate = it.startDate.toLocalDate(),
                endDate = it.endDate.toLocalDate(),
                overallTheme = PredictionTextGenerator.getDashaTheme(it.planet),
                lifeAreaEffects = getLifeAreaEffectsForPlanet(it.planet, context),
                strengths = getDashaStrengths(it.planet, context),
                challenges = getDashaChallenges(it.planet, context),
                advice = PredictionTextGenerator.getDashaAdvice(it.planet, context)
            )
        }
        
        val antardashaAnalysis = currentAntar?.let {
            AntardashaDeepAnalysis(
                planet = it.planet,
                startDate = it.startDate.toLocalDate(),
                endDate = it.endDate.toLocalDate(),
                refinedTheme = PredictionTextGenerator.getAntardashaTheme(current?.planet ?: it.planet, it.planet),
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
                "Your current ${current?.planet?.displayName ?: "dasha"} period sets the foundation for this life phase.",
                "तपाईंको वर्तमान ${current?.planet?.displayName ?: "दशा"} अवधिले यस जीवन चरणको लागि आधार सेट गर्दछ।"
            )
        )
    }
    
    private fun getLifeAreaEffectsForPlanet(planet: Planet, context: AnalysisContext): Map<LifeArea, LocalizedParagraph> {
        return LifeArea.entries.associateWith { area ->
            PredictionTextGenerator.getLifeAreaEffect(planet, area, context)
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
            PredictionTextGenerator.getShortTermEffect(planet, LifeArea.RELATIONSHIP, context),
            PredictionTextGenerator.getShortTermEffect(planet, LifeArea.HEALTH, context)
        )
    }
    
    private fun getActivatedYogas(mahadasha: Planet, antardasha: Planet, context: AnalysisContext): List<ActivatedYoga> {
        return context.yogaAnalysis.allYogas
            .filter { it.planets.contains(mahadasha) || it.planets.contains(antardasha) }
            .map { yoga ->
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
                        "${yoga.name} activates during this period, bringing its effects.",
                        "${yoga.name} यस अवधिमा सक्रिय हुन्छ, यसको प्रभावहरू ल्याउँदै।"
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
        // Simplified transit analysis - in production this would integrate with actual transit calculator
        return TransitDeepAnalysis(
            majorTransits = getMajorTransits(context),
            saturnSadeSati = analyzeSadeSati(context),
            jupiterTransit = analyzeJupiterTransit(context),
            rahuKetuTransit = analyzeNodalTransit(context),
            transitInteractions = getTransitInteractions(context)
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
            2, 11 -> listOf(LifeArea.WEALTH, LifeArea.CAREER)
            7 -> listOf(LifeArea.RELATIONSHIP)
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
    
    private fun getTransitInteractions(context: AnalysisContext): List<TransitInteraction> {
        // Simplified - real implementation would check actual current transits
        return emptyList()
    }
    
    private fun generateYearlyPrediction(context: AnalysisContext, currentDate: LocalDate): YearlyPrediction {
        val year = currentDate.year
        val dasha = context.currentMahadasha
        
        return YearlyPrediction(
            year = year,
            overallTheme = LocalizedParagraph(
                "Year $year brings ${dasha?.planet?.displayName ?: "planetary"} energy themes to the forefront.",
                "वर्ष $year ले ${dasha?.planet?.displayName ?: "ग्रहीय"} ऊर्जा विषयहरूलाई अगाडि ल्याउँछ।"
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
                        "${antar.planet.displayName} antardasha begins - new sub-cycle starts.",
                        "${antar.planet.displayName} अन्तर्दशा सुरु हुन्छ - नयाँ उप-चक्र सुरु।"
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
                    "Month ${month.month.name.lowercase().replaceFirstChar { it.uppercase() }} continues current dasha themes.",
                    "महिना ${month.month.name}ले वर्तमान दशा विषयहरू जारी राख्छ।"
                ),
                focusAreas = listOf(LifeArea.CAREER, LifeArea.HEALTH),
                favorableDates = listOf(1, 5, 9, 14, 18, 23, 27),
                challengingDates = listOf(8, 15, 22)
            )
        }
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
                    "During ${yoga.planets.firstOrNull()?.displayName ?: "planetary"} dasha periods.",
                    "${yoga.planets.firstOrNull()?.displayName ?: "ग्रहीय"} दशा अवधिहरूमा।"
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
                periodName = "Sade Sati Active",
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
                windowName = "Jupiter in Trine",
                startDate = LocalDate.now(),
                endDate = LocalDate.now().plusMonths(6),
                affectedAreas = listOf(LifeArea.GENERAL, LifeArea.EDUCATION, LifeArea.SPIRITUAL),
                opportunityType = "Growth and expansion",
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
                wearingGuidelines = LocalizedParagraph("Wear on ${getGemstoneDay(planet)} after proper energization.",
                    "उचित ऊर्जीकरण पछि ${getGemstoneDay(planet)} मा लगाउनुहोस्।"),
                cautions = if (planet == Planet.SATURN || planet == Planet.MARS) 
                    LocalizedParagraph("Consult astrologer before wearing.", "लगाउनु अघि ज्योतिषीसँग परामर्श गर्नुहोस्।") 
                    else LocalizedParagraph("", "")
            )
        }
        
        val mantras = weakPlanets.take(3).map { planet ->
            MantraRemedy(
                planet = planet,
                beejaMantra = getBeejaMantra(planet),
                fullMantra = getFullMantra(planet),
                chantCount = getMantraCount(planet),
                bestTime = LocalizedParagraph("Best chanted during ${getMantraTime(planet)}.",
                    "${getMantraTime(planet)}मा जप गर्नु उत्तम।")
            )
        }
        
        return RemedialProfile(
            gemstoneRemedies = gemstones,
            mantraRemedies = mantras,
            charitableRemedies = getCharitableRemedies(weakPlanets),
            fastingRemedies = getFastingRemedies(weakPlanets),
            yogicRemedies = getYogicRemedies(context),
            overallRemedialAdvice = LocalizedParagraph(
                "Focus on strengthening ${weakPlanets.firstOrNull()?.displayName ?: "weak planets"} through consistent practice.",
                "${weakPlanets.firstOrNull()?.displayName ?: "कमजोर ग्रहहरू"}लाई निरन्तर अभ्यास मार्फत बलियो बनाउनमा ध्यान दिनुहोस्।"
            )
        )
    }
    
    private fun getGemstone(planet: Planet): String = when (planet) {
        Planet.SUN -> "Ruby (Manik)"
        Planet.MOON -> "Pearl (Moti)"
        Planet.MARS -> "Red Coral (Moonga)"
        Planet.MERCURY -> "Emerald (Panna)"
        Planet.JUPITER -> "Yellow Sapphire (Pukhraj)"
        Planet.VENUS -> "Diamond (Heera)"
        Planet.SATURN -> "Blue Sapphire (Neelam)"
        Planet.RAHU, Planet.TRUE_NODE -> "Hessonite (Gomed)"
        Planet.KETU -> "Cat's Eye (Lehsunia)"
        else -> "Consult astrologer"
    }
    
    private fun getAlternativeGemstone(planet: Planet): String = when (planet) {
        Planet.SUN -> "Garnet"
        Planet.MOON -> "Moonstone"
        Planet.MARS -> "Carnelian"
        Planet.MERCURY -> "Peridot"
        Planet.JUPITER -> "Citrine"
        Planet.VENUS -> "White Sapphire"
        Planet.SATURN -> "Amethyst"
        else -> ""
    }
    
    private fun getGemstoneDay(planet: Planet): String = when (planet) {
        Planet.SUN -> "Sunday"
        Planet.MOON -> "Monday"
        Planet.MARS -> "Tuesday"
        Planet.MERCURY -> "Wednesday"
        Planet.JUPITER -> "Thursday"
        Planet.VENUS -> "Friday"
        Planet.SATURN -> "Saturday"
        Planet.RAHU, Planet.TRUE_NODE -> "Saturday"
        Planet.KETU -> "Tuesday"
        else -> "auspicious day"
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
    
    private fun getMantraTime(planet: Planet): String = when (planet) {
        Planet.SUN -> "sunrise"
        Planet.MOON -> "evening"
        Planet.JUPITER -> "morning"
        Planet.SATURN, Planet.RAHU, Planet.KETU -> "night"
        else -> "dawn or dusk"
    }
    
    private fun getCharitableRemedies(planets: List<Planet>): List<CharitableRemedy> {
        return planets.take(2).map { planet ->
            CharitableRemedy(
                planet = planet,
                donationItems = getDonationItems(planet),
                bestDay = getGemstoneDay(planet),
                guidelines = LocalizedParagraph("Donate with devotion on ${getGemstoneDay(planet)}.",
                    "${getGemstoneDay(planet)}मा भक्तिको साथ दान गर्नुहोस्।")
            )
        }
    }
    
    private fun getDonationItems(planet: Planet): String = when (planet) {
        Planet.SUN -> "Wheat, jaggery, copper"
        Planet.MOON -> "Rice, milk, white cloth"
        Planet.MARS -> "Red lentils, red cloth"
        Planet.MERCURY -> "Green vegetables, books"
        Planet.JUPITER -> "Yellow cloth, turmeric, bananas"
        Planet.VENUS -> "White items, perfume, sweets"
        Planet.SATURN -> "Black sesame, iron, mustard oil"
        Planet.RAHU, Planet.TRUE_NODE -> "Blankets, lead, black pulses"
        Planet.KETU -> "Seven types of grains, multi-colored cloth"
        else -> "Food to needy"
    }
    
    private fun getFastingRemedies(planets: List<Planet>): List<FastingRemedy> {
        return planets.take(1).map { planet ->
            FastingRemedy(
                planet = planet,
                fastingDay = getGemstoneDay(planet),
                fastingType = LocalizedParagraph("Water or milk fast until sunset.",
                    "सूर्यास्त सम्म पानी वा दूध उपवास।"),
                guidelines = LocalizedParagraph("Observe fast with devotion and break with ${getDonationItems(planet).split(",").first().trim()}.",
                    "भक्तिको साथ उपवास गर्नुहोस् र ${getDonationItems(planet).split(",").first().trim()}को साथ व्रत खोल्नुहोस्।")
            )
        }
    }
    
    private fun getYogicRemedies(context: AnalysisContext): List<YogicRemedy> {
        val dominantElement = context.getDominantElement()
        return listOf(
            YogicRemedy(
                practiceName = when (dominantElement) {
                    Element.FIRE -> "Surya Namaskar"
                    Element.EARTH -> "Grounding Asanas"
                    Element.AIR -> "Pranayama"
                    Element.WATER -> "Moon Salutation"
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
            en = "Your current ${dasha?.planet?.displayName ?: "planetary"} period shows ${strength.displayName.lowercase()} influence. " +
                "Focus on ${if (strength >= StrengthLevel.STRONG) "maximizing opportunities" else "building foundations"} " +
                "while attending to health and relationships. The coming period favors " +
                "${if (context.rajaYogas.isNotEmpty()) "achievement and recognition" else "steady growth and development"}.",
            ne = "तपाईंको वर्तमान ${dasha?.planet?.displayName ?: "ग्रहीय"} अवधिले ${strength.displayNameNe} प्रभाव देखाउँछ। " +
                "${if (strength >= StrengthLevel.STRONG) "अवसरहरूलाई अधिकतम बनाउन" else "आधारहरू निर्माण गर्न"} ध्यान दिनुहोस् " +
                "स्वास्थ्य र सम्बन्धहरूमा ध्यान दिँदै।"
        )
    }
    
    private fun calculatePredictionScore(context: AnalysisContext): Double {
        val dashaScore = context.getPlanetStrengthLevel(context.currentMahadasha?.planet ?: Planet.JUPITER).value * 10
        val yogaBonus = context.yogaAnalysis.allYogas.count { it.strength.ordinal >= 2 } * 3
        val transitBonus = 10 // Simplified - would calculate based on actual transits
        return ((dashaScore + yogaBonus + transitBonus) / 1.5).coerceIn(0.0, 100.0)
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
