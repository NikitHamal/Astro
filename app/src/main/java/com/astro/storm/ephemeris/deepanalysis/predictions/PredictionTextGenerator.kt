package com.astro.storm.ephemeris.deepanalysis.predictions

import com.astro.storm.core.model.Planet
import com.astro.storm.data.templates.TemplateSelector
import com.astro.storm.data.templates.TimeHorizon
import com.astro.storm.data.templates.TransitAspectType
import com.astro.storm.data.templates.TransitPhase
import com.astro.storm.ephemeris.deepanalysis.*

/**
 * Prediction Text Generator - Localized prediction interpretations
 */
object PredictionTextGenerator {
    
    fun getDashaTheme(planet: Planet, context: AnalysisContext): LocalizedParagraph {
        val position = context.getPlanetPosition(planet)
        val sign = position?.sign ?: context.ascendantSign
        val degree = (position?.longitude ?: 0.0) % 30.0
        val strength = context.getPlanetStrengthLevel(planet)

        return TemplateSelector.selectDashaTheme(planet, sign, degree, strength)
    }
    
    fun getAntardashaTheme(mahadasha: Planet, antardasha: Planet, context: AnalysisContext): LocalizedParagraph {
        val position = context.getPlanetPosition(antardasha)
        val sign = position?.sign ?: context.ascendantSign
        val degree = (position?.longitude ?: 0.0) % 30.0
        val strength = context.getPlanetStrengthLevel(antardasha)
        val antardashaTheme = TemplateSelector.selectDashaTheme(antardasha, sign, degree, strength)

        return LocalizedParagraph(
            en = "Within the ${mahadasha.getLocalizedName(com.astro.storm.core.common.Language.ENGLISH)} period, ${antardasha.getLocalizedName(com.astro.storm.core.common.Language.ENGLISH)}'s sub-period refines the experience: ${antardashaTheme.en}",
            ne = "${mahadasha.getLocalizedName(com.astro.storm.core.common.Language.NEPALI)} अवधि भित्र, ${antardasha.getLocalizedName(com.astro.storm.core.common.Language.NEPALI)}को उप-अवधिले अनुभव परिष्कृत गर्छ: ${antardashaTheme.ne}"
        )
    }
    
    fun getDashaAdvice(planet: Planet, context: AnalysisContext): LocalizedParagraph = when (planet) {
        Planet.SUN -> LocalizedParagraph(
            en = "Align with your true purpose and don't fear taking leadership. Health requires attention to heart and eyes.",
            ne = "आफ्नो साँचो उद्देश्यसँग मिलाउनुहोस् र नेतृत्व लिन नडराउनुहोस्।"
        )
        Planet.MOON -> LocalizedParagraph(
            en = "Honor your emotions and intuition. Create emotional security and nurture important relationships.",
            ne = "आफ्नो भावनाहरू र अन्तर्ज्ञानलाई सम्मान गर्नुहोस्।"
        )
        Planet.JUPITER -> LocalizedParagraph(
            en = "Embrace learning and spiritual growth. Share wisdom with others and keep expanding your horizons.",
            ne = "सिकाइ र आध्यात्मिक विकास अँगाल्नुहोस्।"
        )
        Planet.SATURN -> LocalizedParagraph(
            en = "Be patient and disciplined. Work hard now for lasting results later. Accept responsibility gracefully.",
            ne = "धैर्यवान र अनुशासित हुनुहोस्। स्थायी परिणामहरूको लागि अहिले कडा परिश्रम गर्नुहोस्।"
        )
        else -> LocalizedParagraph(
            en = "Work with ${planet.getLocalizedName(com.astro.storm.core.common.Language.ENGLISH)}'s energy constructively during this period.",
            ne = "यस अवधिमा ${planet.getLocalizedName(com.astro.storm.core.common.Language.NEPALI)}को ऊर्जासँग रचनात्मक रूपमा काम गर्नुहोस्।"
        )
    }
    
    fun getDashaBriefPreview(planet: Planet, context: AnalysisContext): LocalizedParagraph {
        val strength = context.getPlanetStrengthLevel(planet)
        val position = context.getPlanetPosition(planet)
        val sign = position?.sign ?: context.ascendantSign
        val degree = (position?.longitude ?: 0.0) % 30.0
        val theme = TemplateSelector.selectDashaTheme(planet, sign, degree, strength)
        return LocalizedParagraph(
            en = "${planet.getLocalizedName(com.astro.storm.core.common.Language.ENGLISH)} period ahead with ${strength.displayName.lowercase()} promise. ${theme.en}",
            ne = "${planet.getLocalizedName(com.astro.storm.core.common.Language.NEPALI)} अवधि ${strength.displayNameNe} वाचाको साथ अगाडि। ${theme.ne}"
        )
    }
    
    fun getLifeAreaEffect(planet: Planet, area: LifeArea, context: AnalysisContext): LocalizedParagraph {
        val position = context.getPlanetPosition(planet)
        val sign = position?.sign ?: context.ascendantSign
        val degree = (position?.longitude ?: 0.0) % 30.0
        val strength = context.getPlanetStrengthLevel(planet)

        return TemplateSelector.selectLifeAreaEffect(
            planet = planet,
            area = area,
            sign = sign,
            degree = degree,
            strength = strength,
            horizon = TimeHorizon.MEDIUM
        )
    }
    
    internal fun getAreaName(area: LifeArea, language: com.astro.storm.core.common.Language): String = when(area) {
        LifeArea.GENERAL -> if (language == com.astro.storm.core.common.Language.NEPALI) "सामान्य जीवन" else "general life"
        LifeArea.CAREER -> if (language == com.astro.storm.core.common.Language.NEPALI) "क्यारियर" else "career"
        LifeArea.RELATIONSHIP -> if (language == com.astro.storm.core.common.Language.NEPALI) "सम्बन्ध" else "relationship"
        LifeArea.HEALTH -> if (language == com.astro.storm.core.common.Language.NEPALI) "स्वास्थ्य" else "health"
        LifeArea.WEALTH -> if (language == com.astro.storm.core.common.Language.NEPALI) "धन" else "wealth"
        LifeArea.EDUCATION -> if (language == com.astro.storm.core.common.Language.NEPALI) "शिक्षा" else "education"
        LifeArea.SPIRITUAL -> if (language == com.astro.storm.core.common.Language.NEPALI) "आध्यात्मिकता" else "spirituality"
    }
    
    fun getShortTermEffect(planet: Planet, area: LifeArea, context: AnalysisContext): LocalizedParagraph {
        val position = context.getPlanetPosition(planet)
        val sign = position?.sign ?: context.ascendantSign
        val degree = (position?.longitude ?: 0.0) % 30.0
        val strength = context.getPlanetStrengthLevel(planet)

        return TemplateSelector.selectLifeAreaEffect(
            planet = planet,
            area = area,
            sign = sign,
            degree = degree,
            strength = strength,
            horizon = TimeHorizon.SHORT
        )
    }
    
    fun getSaturnTransitEffect(context: AnalysisContext): LocalizedParagraph {
        return TemplateSelector.selectTransitEffect(
            transitingPlanet = Planet.SATURN,
            natalPlanet = Planet.MOON,
            aspect = TransitAspectType.CONJUNCTION,
            phase = TransitPhase.APPLYING
        )
    }
    
    fun getJupiterTransitEffect(context: AnalysisContext): LocalizedParagraph {
        return TemplateSelector.selectTransitEffect(
            transitingPlanet = Planet.JUPITER,
            natalPlanet = Planet.MOON,
            aspect = TransitAspectType.TRINE,
            phase = TransitPhase.APPLYING
        )
    }
    
    fun getJupiterTransitHouseEffect(house: Int): LocalizedParagraph = when (house) {
        1 -> LocalizedParagraph("Jupiter transiting 1st house brings personal growth and new beginnings.",
            "बृहस्पतिले पहिलो भाव गोचर गर्दा व्यक्तिगत विकास र नयाँ सुरुवातहरू ल्याउँछ।")
        2 -> LocalizedParagraph("Jupiter transiting 2nd house enhances wealth and family matters.",
            "बृहस्पतिले दोस्रो भाव गोचर गर्दा धन र पारिवारिक मामिलाहरू बढाउँछ।")
        5 -> LocalizedParagraph("Jupiter transiting 5th house blesses creativity, children, and romance.",
            "बृहस्पतिले पाँचौं भाव गोचर गर्दा रचनात्मकता, बच्चाहरू र रोमान्सलाई आशीर्वाद दिन्छ।")
        7 -> LocalizedParagraph("Jupiter transiting 7th house favors partnerships and marriage.",
            "बृहस्पतिले सातौं भाव गोचर गर्दा साझेदारी र विवाहलाई अनुकूल पार्छ।")
        9 -> LocalizedParagraph("Jupiter transiting 9th house enhances luck, higher learning, and spirituality.",
            "बृहस्पतिले नवौं भाव गोचर गर्दा भाग्य, उच्च शिक्षा र आध्यात्मिकता बढाउँछ।")
        10 -> LocalizedParagraph("Jupiter transiting 10th house brings career advancement and recognition.",
            "बृहस्पतिले दशौं भाव गोचर गर्दा क्यारियर उन्नति र मान्यता ल्याउँछ।")
        11 -> LocalizedParagraph("Jupiter transiting 11th house fulfills desires and brings gains.",
            "बृहस्पतिले एघारौं भाव गोचर गर्दा इच्छाहरू पूरा गर्छ र लाभ ल्याउँछ।")
        else -> LocalizedParagraph("Jupiter's transit brings growth to the affected life areas.",
            "बृहस्पतिको गोचरले प्रभावित जीवन क्षेत्रहरूमा वृद्धि ल्याउँछ।")
    }
    
    fun getNodalAxisEffects(rahuHouse: Int, ketuHouse: Int): LocalizedParagraph {
        return LocalizedParagraph(
            en = "Rahu in $rahuHouse house / Ketu in $ketuHouse house axis creates karmic focus on worldly vs spiritual balance.",
            ne = "राहू $rahuHouse भावमा / केतु $ketuHouse भावमा अक्षले सांसारिक बनाम आध्यात्मिक सन्तुलनमा कार्मिक फोकस सिर्जना गर्छ।"
        )
    }
    
    fun getSadeSatiEffects(phase: SadeSatiPhase): LocalizedParagraph = when (phase) {
        SadeSatiPhase.RISING -> LocalizedParagraph(
            en = "Sade Sati rising phase: Challenges begin, requiring mental strength and adaptability.",
            ne = "साढे साती उदय चरण: चुनौतीहरू सुरु हुन्छन्, मानसिक शक्ति र अनुकूलनशीलता आवश्यक पर्दछ।"
        )
        SadeSatiPhase.PEAK -> LocalizedParagraph(
            en = "Sade Sati peak phase: Maximum intensity requiring patience and perseverance. Karmic lessons intensify.",
            ne = "साढे साती शिखर चरण: अधिकतम तीव्रता धैर्य र दृढता चाहिन्छ।"
        )
        SadeSatiPhase.SETTING -> LocalizedParagraph(
            en = "Sade Sati setting phase: Challenges begin easing. Lessons learned start manifesting as wisdom.",
            ne = "साढे साती अस्त चरण: चुनौतीहरू कम हुन थाल्छन्।"
        )
        SadeSatiPhase.NOT_ACTIVE -> LocalizedParagraph("", "")
    }
    
    fun getSadeSatiRemedies(): LocalizedParagraph = LocalizedParagraph(
        en = "Worship Lord Hanuman, chant Saturn mantras, donate black sesame on Saturdays, and serve the elderly.",
        ne = "भगवान हनुमानको पूजा गर्नुहोस्, शनि मन्त्र जप गर्नुहोस्, शनिबारमा कालो तिल दान गर्नुहोस्।"
    )
    
    fun getSadeSatiAdvice(): LocalizedParagraph = LocalizedParagraph(
        en = "Remain patient, avoid major risks, focus on service, and strengthen Saturn through discipline and remedies.",
        ne = "धैर्यवान रहनुहोस्, ठूला जोखिमहरूबाट टाढा रहनुहोस्, सेवामा ध्यान दिनुहोस्।"
    )
    
    fun getYearlyAdvice(planet: Planet, context: AnalysisContext): LocalizedParagraph = when (planet) {
        Planet.JUPITER -> LocalizedParagraph(
            en = "This year favors expansion, learning, and spiritual growth. Take calculated risks and embrace opportunities.",
            ne = "यो वर्षले विस्तार, सिकाइ र आध्यात्मिक विकासलाई अनुकूल पार्छ।"
        )
        Planet.SATURN -> LocalizedParagraph(
            en = "This year demands patience and hard work. Focus on building lasting foundations and accepting responsibility.",
            ne = "यो वर्षले धैर्य र कठिन परिश्रम माग्छ।"
        )
        else -> LocalizedParagraph(
            en = "Align your efforts with ${planet.getLocalizedName(com.astro.storm.core.common.Language.ENGLISH)}'s energy for optimal results this year.",
            ne = "यस वर्ष इष्टतम परिणामहरूको लागि ${planet.getLocalizedName(com.astro.storm.core.common.Language.NEPALI)}को ऊर्जासँग प्रयासहरू मिलाउनुहोस्।"
        )
    }
    
    fun getAreaOutlookSummary(area: LifeArea, strength: StrengthLevel, context: AnalysisContext): LocalizedParagraph {
        val lord = context.getHouseLord(houseForArea(area))
        val position = context.getPlanetPosition(lord)
        val sign = position?.sign ?: context.ascendantSign
        val degree = (position?.longitude ?: 0.0) % 30.0
        return TemplateSelector.selectLifeAreaEffect(
            planet = lord,
            area = area,
            sign = sign,
            degree = degree,
            strength = strength,
            horizon = TimeHorizon.MEDIUM
        )
    }
    
    fun getAreaOpportunities(area: LifeArea, strength: StrengthLevel, context: AnalysisContext): List<LocalizedTrait> {
        if (strength.value < StrengthLevel.MODERATE.value) return emptyList()
        val lord = context.getHouseLord(houseForArea(area))
        val effect = getLifeAreaEffect(lord, area, context)
        return listOf(LocalizedTrait(effect.en, effect.ne, strength))
    }
    
    fun getAreaChallenges(area: LifeArea, strength: StrengthLevel, context: AnalysisContext): List<LocalizedTrait> {
        if (strength.value > StrengthLevel.WEAK.value) return emptyList()
        val lord = context.getHouseLord(houseForArea(area))
        val effect = getLifeAreaEffect(lord, area, context)
        return listOf(LocalizedTrait(effect.en, effect.ne, strength))
    }
    
    fun getAreaAdvice(area: LifeArea, strength: StrengthLevel, context: AnalysisContext): LocalizedParagraph {
        val lord = context.getHouseLord(houseForArea(area))
        val position = context.getPlanetPosition(lord)
        val sign = position?.sign ?: context.ascendantSign
        val degree = (position?.longitude ?: 0.0) % 30.0
        return TemplateSelector.selectLifeAreaEffect(
            planet = lord,
            area = area,
            sign = sign,
            degree = degree,
            strength = strength,
            horizon = TimeHorizon.LONG
        )
    }
    
    fun getAreaRecommendations(area: LifeArea, context: AnalysisContext): List<LocalizedParagraph> {
        val enArea = getAreaName(area, com.astro.storm.core.common.Language.ENGLISH)
        val neArea = getAreaName(area, com.astro.storm.core.common.Language.NEPALI)
        return listOf(
            LocalizedParagraph(
                "Follow dasha and transit guidance for $enArea timing.",
                "$neArea समयको लागि दशा र गोचर मार्गदर्शन पछ्याउनुहोस्।"
            )
        )
    }

    private fun houseForArea(area: LifeArea): Int = when (area) {
        LifeArea.CAREER -> 10
        LifeArea.RELATIONSHIP -> 7
        LifeArea.HEALTH -> 6
        LifeArea.WEALTH -> 2
        LifeArea.EDUCATION -> 5
        LifeArea.SPIRITUAL -> 9
        LifeArea.GENERAL -> 1
    }
}
