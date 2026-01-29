package com.astro.storm.ephemeris.deepanalysis.spiritual

import com.astro.storm.core.model.Planet
import com.astro.storm.core.model.VedicChart
import com.astro.storm.core.model.ZodiacSign
import com.astro.storm.ephemeris.deepanalysis.*
import com.astro.storm.ephemeris.spiritual.IshtaDevataCalculator
import com.astro.storm.ephemeris.spiritual.KarakamshaResult
import com.astro.storm.ephemeris.spiritual.SpiritualAnalysisResult
import com.astro.storm.core.model.Nakshatra

/**
 * Spiritual Deep Analyzer - Dharma and spiritual path analysis
 */
object SpiritualDeepAnalyzer {
    
    fun analyze(chart: VedicChart, context: AnalysisContext): SpiritualDeepResult {
        // Create Navamsa VedicChart for Ishta Devata calculation
        val navamsaData = context.getNavamshaChart()
        val navamsaChart = chart.copy(
            planetPositions = navamsaData.planetPositions,
            ascendant = navamsaData.ascendantLongitude
        )
        
        // Use real Ishta Devata Calculator
        val spiritualAnalysis = IshtaDevataCalculator.analyze(chart, navamsaChart)
        val karakamsha = spiritualAnalysis.karakamshaAnalysis
        val beeja = spiritualAnalysis.beejaMantraAnalysis
        
        return SpiritualDeepResult(
            ninthHouseDharma = analyzeNinthHouse(context),
            twelfthHouseMoksha = analyzeTwelfthHouse(context),
            atmakarakaAnalysis = context.getAtmakarakaAnalysis(),
            jupiterAnalysis = analyzeJupiterSpiritual(context),
            ketuAnalysis = analyzeKetu(context, karakamsha),
            spiritualYogas = getSpiritualYogas(context),
            karmicPatterns = getKarmicPatterns(context),
            spiritualStrengths = getSpiritualStrengths(context),
            spiritualChallenges = getSpiritualChallenges(context),
            meditationPractices = getMeditationRecommendations(context, spiritualAnalysis),
            spiritualTimeline = generateTimeline(context),
            currentSpiritualPhase = analyzeCurrentPhase(context),
            spiritualSummary = generateSummary(context, karakamsha),
            spiritualStrengthScore = calculateScore(context)
        )
    }
    
    // ... existing ninthHouseDharma and twelfthHouseMoksha methods ... (keeping them as they are good placeholders or logic)

    // Update analyzeKetu to use Karakamsha info
    private fun analyzeKetu(context: AnalysisContext, karakamsha: KarakamshaResult): KetuSpiritualAnalysis {
        val ketu = context.ketuPosition
        val detachPara = LocalizedParagraph("Detachment areas from Ketu placement.",
                                            "केतु स्थानबाट विरक्ति क्षेत्रहरू।")
        val giftsPara = LocalizedParagraph("Natural spiritual talents from Ketu.",
                                            "केतुबाट प्राकृतिक आध्यात्मिक प्रतिभाहरू।")
        
        // Add karmic insight from Ishta Devata
        val liberation = LocalizedParagraph(
            "Moksha indicators from Ketu and Ishta Devata (${karakamsha.determinedDeity.displayName}).",
            "केतु र इष्ट देवता (${karakamsha.determinedDeity.displayName}) बाट मोक्ष सूचकहरू।"
        )
        
        return KetuSpiritualAnalysis(
            sign = ketu?.sign ?: context.ascendantSign,
            house = ketu?.house ?: 1,
            nakshatra = ketu?.nakshatra ?: context.moonPosition?.nakshatra ?: Nakshatra.ASHWINI,
            pastLifeKarma = LocalizedParagraph("Past life patterns indicated by Ketu.",
                "केतुले संकेत गर्ने पूर्व जीवन ढाँचाहरू।"),
            detachmentArea = detachPara,
            spiritualGifts = giftsPara,
            detachmentAreas = detachPara,
            spiritualTalents = giftsPara,
            liberationIndicators = liberation
        )
    }
    
    // ... existing methods ...

    private fun getMeditationRecommendations(context: AnalysisContext, spiritualAnalysis: SpiritualAnalysisResult): MeditationProfile {
        val dominantElement = context.getDominantElement()
        val practices = spiritualAnalysis.recommendedPractices
        
        val practiceType = when (dominantElement) {
            Element.FIRE -> "Active meditation with movement"
            Element.EARTH -> "Grounding body-based practices"
            Element.AIR -> "Breath-focused meditation"
            Element.WATER -> "Visualization and feeling-based practices"
        }
        
        return MeditationProfile(
            suitablePractices = practices.map { 
                LocalizedTrait(it.practiceName, it.practiceName, StrengthLevel.STRONG)
            } + LocalizedTrait(practiceType, "अभ्यास", StrengthLevel.STRONG),
            bestTimes = LocalizedParagraph(
                practices.firstOrNull()?.bestTime ?: "Dawn and dusk optimal for your chart.",
                "तपाईंको कुण्डलीको लागि बिहान र साँझ इष्टतम।"
            ),
            mantras = LocalizedParagraph(
                "Primary: ${spiritualAnalysis.karakamshaAnalysis.determinedDeity.mantra}. Beeja: ${spiritualAnalysis.beejaMantraAnalysis.primaryBeejaMantra}",
                "मुख्य: ${spiritualAnalysis.karakamshaAnalysis.determinedDeity.mantra}। बीज: ${spiritualAnalysis.beejaMantraAnalysis.primaryBeejaMantra}"
            ),
            deities = LocalizedParagraph(
                "Ishta Devata: ${spiritualAnalysis.karakamshaAnalysis.determinedDeity.displayName}",
                "इष्ट देवता: ${spiritualAnalysis.karakamshaAnalysis.determinedDeity.displayName}"
            )
        )
    }
    
    // ... existing generateTimeline, analyzeCurrentPhase ...

    private fun generateSummary(context: AnalysisContext, karakamsha: KarakamshaResult): LocalizedParagraph {
        val jupiterStrength = context.getPlanetStrengthLevel(Planet.JUPITER)
        val ninthStrength = context.getHouseStrength(9)
        
        return LocalizedParagraph(
            en = "Your spiritual path shows ${ninthStrength.displayName.lowercase()} dharmic strength. " +
                "Your Ishta Devata is ${karakamsha.determinedDeity.displayName}, guiding your soul toward liberation. " +
                "Inner growth comes through ${getSpiritualPath(context)}.",
            ne = "तपाईंको आध्यात्मिक मार्गले ${ninthStrength.displayNameNe} धार्मिक शक्ति देखाउँछ। " +
                "तपाईंको इष्ट देवता ${karakamsha.determinedDeity.displayName} हुनुहुन्छ, जसले तपाईंको आत्मालाई मोक्षतर्फ डोऱ्याउनुहुन्छ।"
        )
    }

    private fun getSpiritualPath(context: AnalysisContext): String {
        return when (context.ketuPosition?.sign) {
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
