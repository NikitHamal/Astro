package com.astro.storm.ephemeris.deepanalysis.predictions

import com.astro.storm.core.model.Planet
import com.astro.storm.ephemeris.deepanalysis.*

/**
 * Prediction Text Generator - Localized prediction interpretations
 */
object PredictionTextGenerator {
    
    fun getDashaTheme(planet: Planet): LocalizedParagraph = when (planet) {
        Planet.SUN -> LocalizedParagraph(
            en = "This period emphasizes self-expression, authority, and recognition. You may seek leadership roles, " +
                "deal with government matters, or focus on health and vitality. Father figures become significant.",
            ne = "यो अवधिले आत्म-अभिव्यक्ति, अधिकार र मान्यतामा जोड दिन्छ। तपाईं नेतृत्व भूमिकाहरू खोज्न सक्नुहुन्छ।"
        )
        Planet.MOON -> LocalizedParagraph(
            en = "This period brings emotional evolution, changes in residence, and public dealings. Mother, " +
                "women, and home matters become central. Mental peace and intuition are heightened.",
            ne = "यो अवधिले भावनात्मक विकास, बसोबास परिवर्तन र सार्वजनिक व्यवहार ल्याउँछ।"
        )
        Planet.MARS -> LocalizedParagraph(
            en = "This period activates courage, competition, and decisive action. Energy levels rise, " +
                "and you may engage in property matters, conflicts, or technical pursuits.",
            ne = "यो अवधिले साहस, प्रतिस्पर्धा र निर्णायक कार्य सक्रिय गर्छ।"
        )
        Planet.MERCURY -> LocalizedParagraph(
            en = "This period enhances communication, learning, and business activities. Intellectual pursuits, " +
                "writing, and commerce are favored. Siblings and friends become important.",
            ne = "यो अवधिले सञ्चार, सिकाइ र व्यापारिक गतिविधिहरू बढाउँछ।"
        )
        Planet.JUPITER -> LocalizedParagraph(
            en = "This auspicious period brings wisdom, expansion, and good fortune. Spiritual growth, " +
                "higher education, and positive developments are likely. Teachers and mentors appear.",
            ne = "यो शुभ अवधिले ज्ञान, विस्तार र सौभाग्य ल्याउँछ।"
        )
        Planet.VENUS -> LocalizedParagraph(
            en = "This period enhances relationships, comforts, and artistic pursuits. Love, luxury, " +
                "and pleasure are emphasized. Partnerships and partnerships become significant.",
            ne = "यो अवधिले सम्बन्धहरू, आराम र कलात्मक खोजहरू बढाउँछ।"
        )
        Planet.SATURN -> LocalizedParagraph(
            en = "This period demands discipline, patience, and hard work. Karmic lessons, delays, " +
                "and restructuring occur. Service, duty, and long-term planning are emphasized.",
            ne = "यो अवधिले अनुशासन, धैर्य र कठिन परिश्रम माग्छ।"
        )
        Planet.RAHU -> LocalizedParagraph(
            en = "This period brings unconventional experiences, foreign connections, and material desires. " +
                "Ambition rises, but clarity may be lacking. Technology and innovation are favored.",
            ne = "यो अवधिले अपरम्परागत अनुभवहरू, विदेशी सम्बन्धहरू र भौतिक इच्छाहरू ल्याउँछ।"
        )
        Planet.KETU -> LocalizedParagraph(
            en = "This period emphasizes spirituality, detachment, and past-life karma. Research, " +
                "occult studies, and inner transformation are highlighted. Material focus may reduce.",
            ne = "यो अवधिले आध्यात्मिकता, विरक्ति र पूर्व-जीवन कर्मामा जोड दिन्छ।"
        )
    }
    
    fun getAntardashaTheme(mahadasha: Planet, antardasha: Planet): LocalizedParagraph {
        return LocalizedParagraph(
            en = "Within the ${mahadasha.displayName} period, ${antardasha.displayName}'s sub-period " +
                "refines the experience by adding ${getAntardashaFlavor(antardasha)}.",
            ne = "${mahadasha.displayName} अवधि भित्र, ${antardasha.displayName}को उप-अवधिले " +
                "${getAntardashaFlavorNe(antardasha)} थपेर अनुभव परिष्कृत गर्छ।"
        )
    }
    
    private fun getAntardashaFlavor(planet: Planet): String = when (planet) {
        Planet.SUN -> "authority and recognition themes"
        Planet.MOON -> "emotional and domestic focus"
        Planet.MARS -> "action and competitive energy"
        Planet.MERCURY -> "communication and business opportunities"
        Planet.JUPITER -> "expansion and wisdom influences"
        Planet.VENUS -> "relationship and creative energy"
        Planet.SATURN -> "discipline and karmic lessons"
        Planet.RAHU -> "unconventional experiences and ambition"
        Planet.KETU -> "spiritual depth and detachment"
    }
    
    private fun getAntardashaFlavorNe(planet: Planet): String = when (planet) {
        Planet.SUN -> "अधिकार र मान्यता विषयहरू"
        Planet.MOON -> "भावनात्मक र घरेलु फोकस"
        Planet.MARS -> "कार्य र प्रतिस्पर्धी ऊर्जा"
        Planet.MERCURY -> "सञ्चार र व्यापार अवसरहरू"
        Planet.JUPITER -> "विस्तार र ज्ञान प्रभावहरू"
        Planet.VENUS -> "सम्बन्ध र रचनात्मक ऊर्जा"
        Planet.SATURN -> "अनुशासन र कार्मिक पाठहरू"
        Planet.RAHU -> "अपरम्परागत अनुभवहरू र महत्वाकांक्षा"
        Planet.KETU -> "आध्यात्मिक गहिराइ र विरक्ति"
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
            en = "Work with ${planet.displayName}'s energy constructively during this period.",
            ne = "यस अवधिमा ${planet.displayName}को ऊर्जासँग रचनात्मक रूपमा काम गर्नुहोस्।"
        )
    }
    
    fun getDashaBriefPreview(planet: Planet, context: AnalysisContext): LocalizedParagraph {
        val strength = context.getPlanetStrengthLevel(planet)
        return LocalizedParagraph(
            en = "${planet.displayName} period ahead with ${strength.displayName.lowercase()} promise. " +
                "Prepare for ${getDashaThemeShort(planet)}.",
            ne = "${planet.displayName} अवधि ${strength.displayNameNe} वाचाको साथ अगाडि।"
        )
    }
    
    private fun getDashaThemeShort(planet: Planet): String = when (planet) {
        Planet.SUN -> "leadership and recognition"
        Planet.MOON -> "emotional growth and home matters"
        Planet.MARS -> "action and competition"
        Planet.MERCURY -> "learning and commerce"
        Planet.JUPITER -> "expansion and wisdom"
        Planet.VENUS -> "love and creativity"
        Planet.SATURN -> "discipline and lessons"
        Planet.RAHU -> "ambition and change"
        Planet.KETU -> "spirituality and detachment"
    }
    
    fun getLifeAreaEffect(planet: Planet, area: LifeArea, context: AnalysisContext): LocalizedParagraph {
        val areaName = area.name.lowercase().replace("_", " ")
        return LocalizedParagraph(
            en = "${planet.displayName}'s influence on $areaName ${getEffectQuality(planet, area)}.",
            ne = "${planet.displayName}को $areaName मा प्रभाव।"
        )
    }
    
    private fun getEffectQuality(planet: Planet, area: LifeArea): String = when {
        planet == Planet.JUPITER && area in listOf(LifeArea.EDUCATION, LifeArea.SPIRITUAL) -> "brings excellent growth"
        planet == Planet.VENUS && area == LifeArea.RELATIONSHIP -> "enhances harmony and love"
        planet == Planet.SATURN && area == LifeArea.CAREER -> "demands discipline for success"
        planet == Planet.SUN && area == LifeArea.CAREER -> "brings authority and recognition"
        else -> "creates unique developments"
    }
    
    fun getShortTermEffect(planet: Planet, area: LifeArea, context: AnalysisContext): LocalizedParagraph {
        return LocalizedParagraph(
            en = "Short-term ${area.name.lowercase()} matters are influenced by ${planet.displayName}'s current transit.",
            ne = "छोटो-अवधि ${area.name} मामिलाहरू ${planet.displayName}को वर्तमान गोचरले प्रभावित छन्।"
        )
    }
    
    fun getSaturnTransitEffect(context: AnalysisContext): LocalizedParagraph {
        return LocalizedParagraph(
            en = "Saturn's transit brings lessons, restructuring, and long-term developments in affected areas.",
            ne = "शनिको गोचरले प्रभावित क्षेत्रहरूमा पाठहरू, पुनर्संरचना र दीर्घकालीन विकासहरू ल्याउँछ।"
        )
    }
    
    fun getJupiterTransitEffect(context: AnalysisContext): LocalizedParagraph {
        return LocalizedParagraph(
            en = "Jupiter's transit brings expansion, opportunities, and positive growth in affected areas.",
            ne = "बृहस्पतिको गोचरले प्रभावित क्षेत्रहरूमा विस्तार, अवसरहरू र सकारात्मक विकास ल्याउँछ।"
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
            en = "Align your efforts with ${planet.displayName}'s energy for optimal results this year.",
            ne = "यस वर्ष इष्टतम परिणामहरूको लागि ${planet.displayName}को ऊर्जासँग प्रयासहरू मिलाउनुहोस्।"
        )
    }
    
    fun getAreaOutlookSummary(area: LifeArea, strength: StrengthLevel): LocalizedParagraph {
        val areaName = area.name.lowercase().replace("_", " ")
        return LocalizedParagraph(
            en = "${areaName.replaceFirstChar { it.uppercase() }} shows ${strength.displayName.lowercase()} potential this period.",
            ne = "${areaName}ले यस अवधिमा ${strength.displayNameNe} क्षमता देखाउँछ।"
        )
    }
    
    fun getAreaOpportunities(area: LifeArea, strength: StrengthLevel): List<LocalizedTrait> {
        return if (strength >= StrengthLevel.MODERATE) {
            listOf(LocalizedTrait("Growth opportunity", "विकास अवसर", strength))
        } else emptyList()
    }
    
    fun getAreaChallenges(area: LifeArea, strength: StrengthLevel): List<LocalizedTrait> {
        return if (strength <= StrengthLevel.WEAK) {
            listOf(LocalizedTrait("Requires attention", "ध्यान चाहिन्छ", strength))
        } else emptyList()
    }
    
    fun getAreaAdvice(area: LifeArea, strength: StrengthLevel): LocalizedParagraph {
        val areaName = area.name.lowercase().replace("_", " ")
        return LocalizedParagraph(
            en = "${if (strength >= StrengthLevel.MODERATE) "Maximize" else "Work on"} $areaName matters during favorable periods.",
            ne = "अनुकूल अवधिहरूमा $areaName मामिलाहरू ${if (strength >= StrengthLevel.MODERATE) "अधिकतम गर्नुहोस्" else "काम गर्नुहोस्"}।"
        )
    }
    
    fun getAreaRecommendations(area: LifeArea, context: AnalysisContext): List<LocalizedParagraph> {
        return listOf(
            LocalizedParagraph(
                "Follow dasha and transit guidance for ${area.name.lowercase()} timing.",
                "${area.name} समयको लागि दशा र गोचर मार्गदर्शन पछ्याउनुहोस्।"
            )
        )
    }
}
