package com.astro.storm.ephemeris.deepanalysis.character

import com.astro.storm.core.model.Planet
import com.astro.storm.core.model.VedicChart
import com.astro.storm.core.model.ZodiacSign
import com.astro.storm.ephemeris.deepanalysis.*

/**
 * Character Deep Analyzer - Multi-dimensional personality analysis
 */
object CharacterDeepAnalyzer {
    
    fun analyze(chart: VedicChart, context: AnalysisContext): CharacterDeepResult {
        val ascendantAnalysis = analyzeAscendant(context)
        val moonAnalysis = analyzeMoon(context)
        val sunAnalysis = analyzeSun(context)
        val atmakarakaAnalysis = analyzeAtmakaraka(context)
        val temperament = analyzeTemperament(context)
        val emotionalPattern = analyzeEmotionalPattern(context)
        val intellectualProfile = analyzeIntellectualProfile(context)
        val socialBehavior = analyzeSocialBehavior(context)
        val strongPlanets = analyzeStrongPlanetInfluences(context)
        val weakPlanets = analyzeWeakPlanetChallenges(context)
        val retrogradeEffects = analyzeRetrogradeEffects(context)
        val personalityYogas = analyzePersonalityYogas(context)
        
        val summary = generatePersonalitySummary(ascendantAnalysis, moonAnalysis, temperament, context)
        val score = calculatePersonalityScore(context)
        
        return CharacterDeepResult(
            ascendantAnalysis = ascendantAnalysis,
            moonAnalysis = moonAnalysis,
            sunAnalysis = sunAnalysis,
            atmakarakaAnalysis = atmakarakaAnalysis,
            temperament = temperament,
            emotionalPattern = emotionalPattern,
            intellectualProfile = intellectualProfile,
            socialBehavior = socialBehavior,
            strongPlanetInfluences = strongPlanets,
            weakPlanetChallenges = weakPlanets,
            retrogradeEffects = retrogradeEffects,
            personalityYogas = personalityYogas,
            keyTraits = temperament.naturalTendencies,
            personalitySummary = summary,
            personalityStrengthScore = score
        )
    }
    
    private fun analyzeAscendant(context: AnalysisContext): AscendantDeepAnalysis {
        val ascSign = context.ascendantSign
        val ascLord = context.ascendantLord
        val ascLordPos = context.ascendantLordPosition
        val degree = context.chart.ascendant
        val nakshatra = context.chart.planetPositions.firstOrNull()?.nakshatra 
            ?: context.moonPosition?.nakshatra!!
        
        return AscendantDeepAnalysis(
            sign = ascSign,
            degree = degree,
            nakshatra = nakshatra,
            nakshatraPada = ((degree % 30) / 3.333).toInt() + 1,
            risingDegreeAnalysis = AscendantTextGenerator.getRisingDegreeAnalysis(ascSign, degree),
            ascendantLordPosition = AscendantLordAnalyzer.analyze(ascLord, ascLordPos, context),
            navamshaAscendantCorrelation = getNavamshaCorrelation(context),
            physicalAppearance = AscendantTextGenerator.getPhysicalAppearance(ascSign),
            firstImpressionGiven = AscendantTextGenerator.getFirstImpression(ascSign),
            lifeApproach = AscendantTextGenerator.getLifeApproach(ascSign),
            overallAscendantInterpretation = AscendantTextGenerator.getOverallInterpretation(ascSign, ascLord, context),
            overallAscendantStrength = context.getHouseStrength(1)
        )
    }
    
    private fun getNavamshaCorrelation(context: AnalysisContext): LocalizedParagraph {
        val d9 = context.getNavamshaChart()
        val d9Asc = d9.ascendantSign
        return LocalizedParagraph(
            en = "Your Navamsha rising in ${d9Asc.displayName} adds depth to your personality, " +
                "revealing hidden aspects that emerge in intimate relationships and as you mature.",
            ne = "${d9Asc.displayName} मा तपाईंको नवांश उदयले तपाईंको व्यक्तित्वमा गहिराइ थप्छ, " +
                "घनिष्ठ सम्बन्धहरूमा र तपाईं परिपक्व हुँदा देखा पर्ने लुकेका पक्षहरू प्रकट गर्दै।"
        )
    }
    
    private fun analyzeMoon(context: AnalysisContext): MoonDeepAnalysis {
        val moon = context.moonPosition ?: return createDefaultMoonAnalysis(context)
        val strength = context.getPlanetStrengthLevel(Planet.MOON)
        
        return MoonDeepAnalysis(
            sign = moon.sign,
            housePosition = moon.house,
            nakshatra = moon.nakshatra,
            nakshatraPada = moon.nakshatraPada,
            nakshatraPadaAnalysis = MoonTextGenerator.getNakshatraPadaAnalysis(moon.nakshatra, moon.nakshatraPada),
            moonStrengthLevel = strength,
            emotionalNature = MoonTextGenerator.getEmotionalNature(moon.sign, strength),
            mindsetAnalysis = MoonTextGenerator.getMindsetAnalysis(moon.sign, moon.house),
            innerNeeds = MoonTextGenerator.getInnerNeeds(moon.sign),
            stressResponse = MoonTextGenerator.getStressResponse(moon.sign),
            motherRelationship = MoonTextGenerator.getMotherRelationship(moon.house, strength),
            comfortSeeking = MoonTextGenerator.getComfortSeeking(moon.sign),
            kemadrumaYogaPresent = context.hasKemadrumaYoga(),
            nakshatraCharacteristics = MoonTextGenerator.getNakshatraCharacteristics(moon.nakshatra),
            overallMoonInterpretation = MoonTextGenerator.getOverallInterpretation(moon, strength, context)
        )
    }
    
    private fun createDefaultMoonAnalysis(context: AnalysisContext): MoonDeepAnalysis {
        val asc = context.ascendantSign
        return MoonDeepAnalysis(
            sign = asc, housePosition = 1,
            nakshatra = context.chart.planetPositions.first().nakshatra,
            nakshatraPada = 1,
            nakshatraPadaAnalysis = LocalizedParagraph("Moon analysis unavailable", "चन्द्र विश्लेषण उपलब्ध छैन"),
            moonStrengthLevel = StrengthLevel.MODERATE,
            emotionalNature = LocalizedParagraph("", ""),
            mindsetAnalysis = LocalizedParagraph("", ""),
            innerNeeds = LocalizedParagraph("", ""),
            stressResponse = LocalizedParagraph("", ""),
            motherRelationship = LocalizedParagraph("", ""),
            comfortSeeking = LocalizedParagraph("", ""),
            kemadrumaYogaPresent = false,
            overallMoonInterpretation = LocalizedParagraph("", "")
        )
    }
    
    private fun analyzeSun(context: AnalysisContext): SunDeepAnalysis {
        val sun = context.sunPosition ?: return createDefaultSunAnalysis()
        val strength = context.getPlanetStrengthLevel(Planet.SUN)
        
        return SunDeepAnalysis(
            sign = sun.sign,
            housePosition = sun.house,
            nakshatra = sun.nakshatra,
            sunStrengthLevel = strength,
            coreIdentity = SunTextGenerator.getCoreIdentity(sun.sign, strength),
            egoExpression = SunTextGenerator.getEgoExpression(sun.sign, sun.house),
            authorityRelationship = SunTextGenerator.getAuthorityRelationship(sun.house, strength),
            fatherRelationship = SunTextGenerator.getFatherRelationship(sun.house, strength),
            leadershipStyle = SunTextGenerator.getLeadershipStyle(sun.sign),
            vitalityLevel = SunTextGenerator.getVitalityLevel(strength),
            overallSunInterpretation = SunTextGenerator.getOverallInterpretation(sun, strength)
        )
    }
    
    private fun createDefaultSunAnalysis(): SunDeepAnalysis {
        return SunDeepAnalysis(
            sign = ZodiacSign.ARIES, housePosition = 1,
            nakshatra = com.astro.storm.core.model.Nakshatra.ASHWINI,
            sunStrengthLevel = StrengthLevel.MODERATE,
            coreIdentity = LocalizedParagraph("", ""),
            egoExpression = LocalizedParagraph("", ""),
            authorityRelationship = LocalizedParagraph("", ""),
            fatherRelationship = LocalizedParagraph("", ""),
            leadershipStyle = LocalizedParagraph("", ""),
            vitalityLevel = LocalizedParagraph("", ""),
            overallSunInterpretation = LocalizedParagraph("", "")
        )
    }
    
    private fun analyzeAtmakaraka(context: AnalysisContext): AtmakarakaAnalysis {
        val ak = context.atmakaraka
        val akPos = context.getPlanetPosition(ak)
        
        return AtmakarakaAnalysis(
            planet = ak,
            signPosition = akPos?.sign ?: context.ascendantSign,
            soulDesire = AtmakarakaTextGenerator.getSoulDesire(ak),
            karmicLesson = AtmakarakaTextGenerator.getKarmicLesson(ak),
            spiritualPath = AtmakarakaTextGenerator.getSpiritualPath(ak),
            overallAtmakarakaInterpretation = AtmakarakaTextGenerator.getOverall(ak, akPos)
        )
    }
    
    private fun analyzeTemperament(context: AnalysisContext): TemperamentProfile {
        val dominantElement = context.getDominantElement()
        val dominantModality = context.getDominantModality()
        val elementBalance = context.getElementBalance()
        val modalityBalance = context.getModalityBalance()
        
        val secondaryElement = elementBalance.entries
            .sortedByDescending { it.value }
            .getOrNull(1)?.key ?: Element.EARTH
        
        return TemperamentProfile(
            dominantElement = dominantElement,
            secondaryElement = secondaryElement,
            dominantModality = dominantModality,
            elementBalance = elementBalance,
            modalityBalance = modalityBalance,
            temperamentDescription = TemperamentTextGenerator.getDescription(dominantElement, dominantModality),
            naturalTendencies = TemperamentTextGenerator.getTendencies(dominantElement, dominantModality)
        )
    }
    
    private fun analyzeEmotionalPattern(context: AnalysisContext): EmotionalPattern {
        val moonStrength = context.getPlanetStrengthLevel(Planet.MOON)
        val moonSign = context.moonSign
        
        return EmotionalPattern(
            emotionalStability = moonStrength,
            emotionalExpression = EmotionalTextGenerator.getExpression(moonSign, moonStrength),
            emotionalTriggers = EmotionalTextGenerator.getTriggers(moonSign),
            emotionalStrengths = EmotionalTextGenerator.getStrengths(moonSign, moonStrength),
            emotionalChallenges = EmotionalTextGenerator.getChallenges(moonSign, moonStrength),
            emotionalGrowthPath = EmotionalTextGenerator.getGrowthPath(moonSign)
        )
    }
    
    private fun analyzeIntellectualProfile(context: AnalysisContext): IntellectualProfile {
        val mercuryStrength = context.getPlanetStrengthLevel(Planet.MERCURY)
        val mercuryPos = context.getPlanetPosition(Planet.MERCURY)
        
        return IntellectualProfile(
            mercuryStrength = mercuryStrength,
            learningStyle = IntellectTextGenerator.getLearningStyle(mercuryPos?.sign, mercuryStrength),
            communicationStyle = IntellectTextGenerator.getCommunicationStyle(mercuryPos?.sign, mercuryStrength),
            analyticalAbility = mercuryStrength,
            creativeThinking = if (context.hasYogaType("Saraswati")) StrengthLevel.STRONG else mercuryStrength,
            intellectualStrengths = IntellectTextGenerator.getStrengths(mercuryPos?.sign, mercuryStrength),
            intellectualChallenges = IntellectTextGenerator.getChallenges(mercuryPos?.sign, mercuryStrength)
        )
    }
    
    private fun analyzeSocialBehavior(context: AnalysisContext): SocialBehaviorProfile {
        val ascSign = context.ascendantSign
        val venusStrength = context.getPlanetStrengthLevel(Planet.VENUS)
        
        return SocialBehaviorProfile(
            socialOrientation = SocialTextGenerator.getSocialOrientation(ascSign),
            relationshipApproach = SocialTextGenerator.getRelationshipApproach(ascSign, venusStrength),
            communicationInGroups = SocialTextGenerator.getGroupCommunication(ascSign),
            leadershipOrFollower = SocialTextGenerator.getLeadershipTendency(ascSign, context),
            socialStrengths = SocialTextGenerator.getSocialStrengths(ascSign),
            socialChallenges = SocialTextGenerator.getSocialChallenges(ascSign)
        )
    }
    
    private fun analyzeStrongPlanetInfluences(context: AnalysisContext): List<PlanetaryPersonalityInfluence> {
        return context.chart.planetPositions
            .filter { context.getPlanetStrengthLevel(it.planet) >= StrengthLevel.STRONG }
            .map { pos ->
                PlanetaryPersonalityInfluence(
                    planet = pos.planet,
                    strengthLevel = context.getPlanetStrengthLevel(pos.planet),
                    dignityLevel = context.getDignity(pos.planet),
                    personalityContribution = PlanetPersonalityGenerator.getContribution(pos.planet, true),
                    traits = PlanetPersonalityGenerator.getTraits(pos.planet, true)
                )
            }
    }
    
    private fun analyzeWeakPlanetChallenges(context: AnalysisContext): List<PlanetaryPersonalityInfluence> {
        return context.chart.planetPositions
            .filter { context.getPlanetStrengthLevel(it.planet) <= StrengthLevel.WEAK }
            .map { pos ->
                PlanetaryPersonalityInfluence(
                    planet = pos.planet,
                    strengthLevel = context.getPlanetStrengthLevel(pos.planet),
                    dignityLevel = context.getDignity(pos.planet),
                    personalityContribution = PlanetPersonalityGenerator.getContribution(pos.planet, false),
                    traits = PlanetPersonalityGenerator.getTraits(pos.planet, false)
                )
            }
    }
    
    private fun analyzeRetrogradeEffects(context: AnalysisContext): List<RetrogradeEffect> {
        return context.chart.planetPositions
            .filter { it.isRetrograde && it.planet != Planet.RAHU && it.planet != Planet.KETU }
            .map { pos ->
                RetrogradeEffect(
                    planet = pos.planet,
                    internalProcessing = RetrogradeTextGenerator.getInternalProcessing(pos.planet),
                    pastLifeKarma = RetrogradeTextGenerator.getPastLifeKarma(pos.planet),
                    unusualApproach = RetrogradeTextGenerator.getUnusualApproach(pos.planet)
                )
            }
    }
    
    private fun analyzePersonalityYogas(context: AnalysisContext): List<YogaPersonalityEffect> {
        val personalityYogaNames = listOf("Gajakesari", "Budhaditya", "Saraswati", "Hamsa", "Malavya", "Ruchaka", "Bhadra", "Sasa")
        
        return context.yogaAnalysis.allYogas
            .filter { yoga -> personalityYogaNames.any { yoga.name.contains(it, ignoreCase = true) } }
            .map { yoga ->
                YogaPersonalityEffect(
                    yogaName = yoga.name,
                    yogaStrength = yoga.strength.toStrengthLevel(),
                    personalityEffect = YogaPersonalityGenerator.getPersonalityEffect(yoga.name),
                    manifestationStyle = YogaPersonalityGenerator.getManifestationStyle(yoga.name)
                )
            }
    }
    
    private fun generatePersonalitySummary(
        asc: AscendantDeepAnalysis,
        moon: MoonDeepAnalysis,
        temp: TemperamentProfile,
        context: AnalysisContext
    ): LocalizedParagraph {
        return LocalizedParagraph(
            en = "With ${asc.sign.displayName} rising and Moon in ${moon.sign.displayName}, " +
                "you possess a ${temp.dominantElement.displayName}-dominant temperament with " +
                "${temp.dominantModality.displayName} drive. Your personality blends " +
                "${asc.sign.displayName}'s ${getSignQuality(asc.sign)} with " +
                "${moon.sign.displayName}'s emotional depth, creating a unique character profile " +
                "that ${getStrengthDescription(context)}.",
            ne = "${asc.sign.displayName} उदय र ${moon.sign.displayName} मा चन्द्रमाको साथ, " +
                "तपाईंसँग ${temp.dominantElement.displayNameNe}-प्रधान स्वभाव ${temp.dominantModality.displayNameNe} " +
                "अभियानको साथ छ। तपाईंको व्यक्तित्व ${asc.sign.displayName}को ${getSignQualityNe(asc.sign)} " +
                "${moon.sign.displayName}को भावनात्मक गहिराइसँग मिलाउँछ, " +
                "${getStrengthDescriptionNe(context)} एक अद्वितीय चरित्र प्रोफाइल सिर्जना गर्दै।"
        )
    }
    
    private fun getSignQuality(sign: ZodiacSign): String = when (sign) {
        ZodiacSign.ARIES -> "pioneering spirit"
        ZodiacSign.TAURUS -> "grounded stability"
        ZodiacSign.GEMINI -> "intellectual curiosity"
        ZodiacSign.CANCER -> "nurturing nature"
        ZodiacSign.LEO -> "creative leadership"
        ZodiacSign.VIRGO -> "analytical precision"
        ZodiacSign.LIBRA -> "harmonious diplomacy"
        ZodiacSign.SCORPIO -> "transformative intensity"
        ZodiacSign.SAGITTARIUS -> "philosophical vision"
        ZodiacSign.CAPRICORN -> "ambitious discipline"
        ZodiacSign.AQUARIUS -> "innovative thinking"
        ZodiacSign.PISCES -> "intuitive compassion"
    }
    
    private fun getSignQualityNe(sign: ZodiacSign): String = when (sign) {
        ZodiacSign.ARIES -> "अग्रगामी भावना"
        ZodiacSign.TAURUS -> "जमिनमा स्थिरता"
        ZodiacSign.GEMINI -> "बौद्धिक जिज्ञासा"
        ZodiacSign.CANCER -> "पालनपोषण स्वभाव"
        ZodiacSign.LEO -> "रचनात्मक नेतृत्व"
        ZodiacSign.VIRGO -> "विश्लेषणात्मक सटीकता"
        ZodiacSign.LIBRA -> "सामंजस्यपूर्ण कूटनीति"
        ZodiacSign.SCORPIO -> "परिवर्तनकारी तीव्रता"
        ZodiacSign.SAGITTARIUS -> "दार्शनिक दृष्टि"
        ZodiacSign.CAPRICORN -> "महत्वाकांक्षी अनुशासन"
        ZodiacSign.AQUARIUS -> "नवीन सोच"
        ZodiacSign.PISCES -> "अन्तर्ज्ञानी करुणा"
    }
    
    private fun getStrengthDescription(context: AnalysisContext): String {
        val strongPlanets = context.shadbalaAnalysis.getStrongPlanets()
        return if (strongPlanets.size >= 3) {
            "shows remarkable potential for success"
        } else if (strongPlanets.isNotEmpty()) {
            "balances strengths with growth opportunities"
        } else {
            "offers continuous paths for personal development"
        }
    }
    
    private fun getStrengthDescriptionNe(context: AnalysisContext): String {
        val strongPlanets = context.shadbalaAnalysis.getStrongPlanets()
        return if (strongPlanets.size >= 3) {
            "सफलताको लागि उल्लेखनीय क्षमता देखाउँछ"
        } else if (strongPlanets.isNotEmpty()) {
            "विकास अवसरहरूसँग शक्तिहरू सन्तुलित गर्दछ"
        } else {
            "व्यक्तिगत विकासको लागि निरन्तर मार्गहरू प्रदान गर्दछ"
        }
    }
    
    private fun calculatePersonalityScore(context: AnalysisContext): Double {
        val ascLordStrength = context.getPlanetStrengthLevel(context.ascendantLord).value * 10
        val moonStrength = context.getPlanetStrengthLevel(Planet.MOON).value * 10
        val sunStrength = context.getPlanetStrengthLevel(Planet.SUN).value * 5
        val yogaBonus = context.yogaAnalysis.allYogas.count { it.strength.ordinal >= 2 } * 3
        
        return ((ascLordStrength + moonStrength + sunStrength + yogaBonus) / 1.5).coerceIn(0.0, 100.0)
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
