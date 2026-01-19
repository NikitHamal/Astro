package com.astro.storm.ephemeris.deepanalysis.spiritual

import com.astro.storm.core.model.Planet
import com.astro.storm.core.model.VedicChart
import com.astro.storm.core.model.ZodiacSign
import com.astro.storm.ephemeris.deepanalysis.*

/**
 * Spiritual Deep Analyzer - Dharma and spiritual path analysis
 */
object SpiritualDeepAnalyzer {
    
    fun analyze(chart: VedicChart, context: AnalysisContext): SpiritualDeepResult {
        return SpiritualDeepResult(
            ninthHouseDharma = analyzeNinthHouse(context),
            twelfthHouseMoksha = analyzeTwelfthHouse(context),
            atmakarakaAnalysis = context.getAtmakarakaAnalysis(),
            jupiterAnalysis = analyzeJupiterSpiritual(context),
            ketuAnalysis = analyzeKetu(context),
            spiritualYogas = getSpiritualYogas(context),
            karmicPatterns = getKarmicPatterns(context),
            spiritualStrengths = getSpiritualStrengths(context),
            spiritualChallenges = getSpiritualChallenges(context),
            meditationPractices = getMeditationRecommendations(context),
            spiritualTimeline = generateTimeline(context),
            currentSpiritualPhase = analyzeCurrentPhase(context),
            spiritualSummary = generateSummary(context),
            spiritualStrengthScore = calculateScore(context)
        )
    }
    
    private fun analyzeNinthHouse(context: AnalysisContext): NinthHouseDharmaAnalysis {
        val ninthSign = ZodiacSign.entries[(context.ascendantSign.ordinal + 8) % 12]
        return NinthHouseDharmaAnalysis(
            sign = ninthSign,
            planetsInHouse = context.getPlanetsInHouse(9).map { pos ->
                PlanetInHouseAnalysis(pos.planet, context.getDignity(pos.planet),
                    LocalizedParagraph("${pos.planet.displayName} in 9th influences dharma.",
                        "${pos.planet.displayName} नवौंमा धर्मलाई प्रभाव पार्छ।"))
            },
            houseStrength = context.getHouseStrength(9),
            dharmaPath = LocalizedParagraph("Your dharmic path follows ${ninthSign.displayName} energy.",
                "तपाईंको धार्मिक मार्ग ${ninthSign.displayName} ऊर्जा पछ्याउँछ।"),
            religiousInclination = LocalizedParagraph("Religious inclination from 9th house.",
                "नवौं भावबाट धार्मिक झुकाव।"),
            guruConnection = LocalizedParagraph("Guru connection indicated by 9th house.",
                "नवौं भावले संकेत गर्ने गुरु सम्बन्ध।"),
            higherPhilosophy = LocalizedParagraph("Philosophical orientation from 9th.",
                "नवौंबाट दार्शनिक अभिमुखीकरण।")
        )
    }
    
    private fun analyzeTwelfthHouse(context: AnalysisContext): TwelfthHouseMokshaAnalysis {
        val twelfthSign = ZodiacSign.entries[(context.ascendantSign.ordinal + 11) % 12]
        return TwelfthHouseMokshaAnalysis(
            sign = twelfthSign,
            planetsInHouse = context.getPlanetsInHouse(12).map { pos ->
                PlanetInHouseAnalysis(pos.planet, context.getDignity(pos.planet),
                    LocalizedParagraph("${pos.planet.displayName} in 12th influences liberation.",
                        "${pos.planet.displayName} बाह्रौंमा मोक्षलाई प्रभाव पार्छ।"))
            },
            houseStrength = context.getHouseStrength(12),
            liberationPath = LocalizedParagraph("Liberation path through ${twelfthSign.displayName}.",
                "${twelfthSign.displayName} मार्फत मोक्ष मार्ग।"),
            meditationInclination = LocalizedParagraph("Meditation inclination from 12th.",
                "बाह्रौंबाट ध्यान झुकाव।"),
            asceticTendencies = LocalizedParagraph("Renunciation patterns from 12th house.",
                "बाह्रौं भावबाट त्याग ढाँचाहरू।"),
            dreamLife = LocalizedParagraph("Dream life and subconscious patterns.",
                "सपना जीवन र अवचेतन ढाँचाहरू।")
        )
    }
    
    private fun analyzeJupiterSpiritual(context: AnalysisContext): JupiterSpiritualAnalysis {
        val jupiter = context.getPlanetPosition(Planet.JUPITER)
        return JupiterSpiritualAnalysis(
            sign = jupiter?.sign ?: context.ascendantSign,
            house = jupiter?.house ?: 1,
            dignity = context.getDignity(Planet.JUPITER),
            strengthLevel = context.getPlanetStrengthLevel(Planet.JUPITER),
            wisdomDevelopment = LocalizedParagraph("Jupiter develops spiritual wisdom.",
                "बृहस्पतिले आध्यात्मिक ज्ञान विकास गर्छ।"),
            faithAndBelief = LocalizedParagraph("Faith patterns from Jupiter placement.",
                "बृहस्पति स्थानबाट विश्वास ढाँचाहरू।"),
            spiritualGrowthPattern = LocalizedParagraph("Spiritual growth follows Jupiter.",
                "आध्यात्मिक विकास बृहस्पति पछ्याउँछ।"),
            blessingsReceived = LocalizedParagraph("Jupiter's blessings in spiritual life.",
                "आध्यात्मिक जीवनमा बृहस्पतिको आशीर्वाद।")
        )
    }
    
    private fun analyzeKetu(context: AnalysisContext): KetuSpiritualAnalysis {
        val ketu = context.ketuPosition
        val detachPara = LocalizedParagraph("Detachment areas from Ketu placement.",
                                            "केतु स्थानबाट विरक्ति क्षेत्रहरू।")
        val giftsPara = LocalizedParagraph("Natural spiritual talents from Ketu.",
                                            "केतुबाट प्राकृतिक आध्यात्मिक प्रतिभाहरू।")
        return KetuSpiritualAnalysis(
            sign = ketu?.sign ?: context.ascendantSign,
            house = ketu?.house ?: 1,
            nakshatra = ketu?.nakshatra ?: context.moonPosition?.nakshatra!!,
            pastLifeKarma = LocalizedParagraph("Past life patterns indicated by Ketu.",
                "केतुले संकेत गर्ने पूर्व जीवन ढाँचाहरू।"),
            detachmentArea = detachPara,
            spiritualGifts = giftsPara,
            detachmentAreas = detachPara,
            spiritualTalents = giftsPara,
            liberationIndicators = LocalizedParagraph("Moksha indicators from Ketu.",
                "केतुबाट मोक्ष सूचकहरू।")
        )
    }
    
    private fun getSpiritualYogas(context: AnalysisContext): List<SpiritualYoga> {
        val yogas = mutableListOf<SpiritualYoga>()
        
        // Check for spiritual yogas
        val ketuIn12 = context.getPlanetHouse(Planet.KETU) == 12
        if (ketuIn12) {
            yogas.add(SpiritualYoga("Ketu in 12th", StrengthLevel.STRONG,
                LocalizedParagraph("Strong liberation potential from Ketu in 12th.",
                    "बाह्रौंमा केतुबाट बलियो मोक्ष क्षमता।"), listOf(Planet.KETU)))
        }
        
        val jupiterStrength = context.getPlanetStrengthLevel(Planet.JUPITER)
        if (jupiterStrength >= StrengthLevel.STRONG) {
            yogas.add(SpiritualYoga("Strong Jupiter", jupiterStrength,
                LocalizedParagraph("Jupiter's blessing enhances spirituality.",
                    "बृहस्पतिको आशीर्वादले आध्यात्मिकता बढाउँछ।"), listOf(Planet.JUPITER)))
        }
        
        return yogas
    }
    
    private fun getKarmicPatterns(context: AnalysisContext): List<KarmicPattern> {
        val patterns = mutableListOf<KarmicPattern>()
        
        val rahu = context.rahuPosition
        val ketu = context.ketuPosition
        
        patterns.add(KarmicPattern(
            patternName = "Rahu-Ketu Axis: ${ketu?.sign?.displayName ?: "Sign"} - ${rahu?.sign?.displayName ?: "Sign"}",
            areaAffected = LocalizedParagraph("Life areas connected to nodal axis.",
                "नोडल अक्षसँग जोडिएको जीवन क्षेत्रहरू।"),
            karmicLesson = LocalizedParagraph("Growth from ${ketu?.sign?.displayName ?: "past"} toward ${rahu?.sign?.displayName ?: "future"}.",
                "${ketu?.sign?.displayName ?: "विगत"}बाट ${rahu?.sign?.displayName ?: "भविष्य"}तर्फ विकास।"),
            lessonToLearn = LocalizedParagraph("The soul seeks to integrate these opposing energies.",
                "आत्माले यी विपरीत ऊर्जाहरू एकीकृत गर्न खोज्छ।"),
            growthOpportunities = LocalizedParagraph("Growth through balancing past and future karma.",
                "विगत र भविष्य कर्म सन्तुलित गर्ने मार्फत विकास।")
        ))
        
        return patterns
    }
    
    private fun getSpiritualStrengths(context: AnalysisContext): List<LocalizedTrait> {
        val strengths = mutableListOf<LocalizedTrait>()
        
        val jupiterStrength = context.getPlanetStrengthLevel(Planet.JUPITER)
        if (jupiterStrength >= StrengthLevel.STRONG) {
            strengths.add(LocalizedTrait("Jupiter's wisdom", "बृहस्पतिको ज्ञान", jupiterStrength))
        }
        
        val ninthStrength = context.getHouseStrength(9)
        if (ninthStrength >= StrengthLevel.STRONG) {
            strengths.add(LocalizedTrait("Strong dharma house", "बलियो धर्म भाव", ninthStrength))
        }
        
        val twelfthStrength = context.getHouseStrength(12)
        if (twelfthStrength >= StrengthLevel.MODERATE) {
            strengths.add(LocalizedTrait("Moksha potential", "मोक्ष क्षमता", twelfthStrength))
        }
        
        return strengths.take(5)
    }
    
    private fun getSpiritualChallenges(context: AnalysisContext): List<LocalizedTrait> {
        val challenges = mutableListOf<LocalizedTrait>()
        
        val jupiterStrength = context.getPlanetStrengthLevel(Planet.JUPITER)
        if (jupiterStrength <= StrengthLevel.WEAK) {
            challenges.add(LocalizedTrait("Faith development", "विश्वास विकास", StrengthLevel.MODERATE))
        }
        
        return challenges.take(3)
    }
    
    private fun getMeditationRecommendations(context: AnalysisContext): MeditationProfile {
        val dominantElement = context.getDominantElement()
        
        val practiceType = when (dominantElement) {
            Element.FIRE -> "Active meditation with movement"
            Element.EARTH -> "Grounding body-based practices"
            Element.AIR -> "Breath-focused meditation"
            Element.WATER -> "Visualization and feeling-based practices"
        }
        
        return MeditationProfile(
            suitablePractices = listOf(LocalizedTrait(practiceType, "अभ्यास", StrengthLevel.STRONG)),
            bestTimes = LocalizedParagraph("Dawn and dusk optimal for your chart.",
                "तपाईंको कुण्डलीको लागि बिहान र साँझ इष्टतम।"),
            mantras = LocalizedParagraph("Mantras aligned with beneficial planets.",
                "लाभदायक ग्रहहरूसँग मिलाएको मन्त्रहरू।"),
            deities = LocalizedParagraph("Ishta Devata based on chart indicators.",
                "कुण्डली सूचकहरूमा आधारित इष्ट देवता।")
        )
    }
    
    private fun generateTimeline(context: AnalysisContext): List<SpiritualTimingPeriod> {
        return context.dashaTimeline.mahadashas.take(2).map { dasha ->
            SpiritualTimingPeriod(
                startDate = dasha.startDate.toLocalDate(),
                endDate = dasha.endDate.toLocalDate(),
                dasha = "${dasha.planet.displayName} Mahadasha",
                spiritualFocus = LocalizedParagraph("${dasha.planet.displayName} period spiritual focus.",
                    "${dasha.planet.displayName} अवधि आध्यात्मिक फोकस।"),
                opportunities = emptyList(),
                challenges = emptyList(),
                favorability = context.getPlanetStrengthLevel(dasha.planet)
            )
        }
    }
    
    private fun analyzeCurrentPhase(context: AnalysisContext): CurrentSpiritualPhase {
        val dasha = context.currentMahadasha
        return CurrentSpiritualPhase(
            currentDasha = "${dasha?.planet?.displayName ?: "Current"} Mahadasha",
            spiritualGrowthPotential = context.getPlanetStrengthLevel(dasha?.planet ?: Planet.JUPITER),
            currentFocus = LocalizedParagraph("Current period spiritual guidance.",
                "वर्तमान अवधि आध्यात्मिक मार्गदर्शन।"),
            practiceAdvice = LocalizedParagraph("Spiritual practices for current period.",
                "वर्तमान अवधिको लागि आध्यात्मिक अभ्यासहरू।")
        )
    }
    
    private fun generateSummary(context: AnalysisContext): LocalizedParagraph {
        val jupiterStrength = context.getPlanetStrengthLevel(Planet.JUPITER)
        val ninthStrength = context.getHouseStrength(9)
        
        return LocalizedParagraph(
            en = "Your spiritual path shows ${ninthStrength.displayName.lowercase()} dharmic strength with " +
                "${jupiterStrength.displayName.lowercase()} Jupiter's blessing. Inner growth comes through " +
                "${getSpiritualPath(context)}.",
            ne = "तपाईंको आध्यात्मिक मार्गले ${ninthStrength.displayNameNe} धार्मिक शक्ति ${jupiterStrength.displayNameNe} " +
                "बृहस्पतिको आशीर्वादको साथ देखाउँछ।"
        )
    }
    
    private fun getSpiritualPath(context: AnalysisContext): String {
        val ketu = context.ketuPosition
        return when (ketu?.sign) {
            ZodiacSign.ARIES, ZodiacSign.LEO, ZodiacSign.SAGITTARIUS -> "active service and righteous action"
            ZodiacSign.TAURUS, ZodiacSign.VIRGO, ZodiacSign.CAPRICORN -> "disciplined practice and devotion"
            ZodiacSign.GEMINI, ZodiacSign.LIBRA, ZodiacSign.AQUARIUS -> "knowledge and understanding"
            else -> "meditation and inner contemplation"
        }
    }
    
    private fun calculateScore(context: AnalysisContext): Double {
        val jupiterScore = context.getPlanetStrengthLevel(Planet.JUPITER).value * 10
        val ninthScore = context.getHouseStrength(9).value * 10
        val twelfthScore = context.getHouseStrength(12).value * 5
        return ((jupiterScore + ninthScore + twelfthScore) / 1.5).coerceIn(0.0, 100.0)
    }
}
