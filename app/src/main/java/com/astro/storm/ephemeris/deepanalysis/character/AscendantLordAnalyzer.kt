package com.astro.storm.ephemeris.deepanalysis.character

import com.astro.storm.core.model.Planet
import com.astro.storm.core.model.PlanetPosition
import com.astro.storm.core.model.ZodiacSign
import com.astro.storm.ephemeris.deepanalysis.*

/**
 * Ascendant Lord Analyzer - 144 house placement combinations
 */
object AscendantLordAnalyzer {
    
    fun analyze(lord: Planet, position: PlanetPosition?, context: AnalysisContext): AscendantLordAnalysis {
        val house = position?.house ?: 1
        val sign = position?.sign ?: context.ascendantSign
        val dignity = context.getDignity(lord)
        
        return AscendantLordAnalysis(
            planet = lord,
            housePosition = house,
            signPosition = sign,
            dignity = dignity,
            lordPositionInterpretation = getLordInHouseInterpretation(lord, house),
            lifeDirectionIndicated = getLifeDirection(house, dignity)
        )
    }
    
    private fun getLordInHouseInterpretation(lord: Planet, house: Int): LocalizedParagraph = when (house) {
        1 -> LocalizedParagraph(
            en = "Your ascendant lord ${lord.displayName} placed in the 1st house gives you strong self-confidence " +
                "and a clear sense of identity. You are self-made and personally drive your own destiny. " +
                "Physical vitality is enhanced, and you naturally attract attention wherever you go.",
            ne = "तपाईंको लग्न स्वामी ${lord.displayName} पहिलो भावमा राखिएकोले तपाईंलाई बलियो आत्मविश्वास " +
                "र स्पष्ट पहिचानको भावना दिन्छ। तपाईं स्व-निर्मित हुनुहुन्छ।"
        )
        2 -> LocalizedParagraph(
            en = "Your ascendant lord ${lord.displayName} in the 2nd house focuses your life on wealth accumulation, " +
                "family values, and resource building. Speech is prominent in your life, and you may excel " +
                "in financial matters or businesses related to food, speech, or valuables.",
            ne = "तपाईंको लग्न स्वामी ${lord.displayName} दोस्रो भावमा तपाईंको जीवनलाई धन संचय, " +
                "पारिवारिक मूल्यहरू र स्रोत निर्माणमा केन्द्रित गर्छ।"
        )
        3 -> LocalizedParagraph(
            en = "Your ascendant lord ${lord.displayName} in the 3rd house makes you courageous, communicative, " +
                "and skilled in writing or media. Siblings may play an important role, and short travels " +
                "bring opportunities. You excel through personal effort and initiative.",
            ne = "तपाईंको लग्न स्वामी ${lord.displayName} तेस्रो भावमा तपाईंलाई साहसी, संवादशील, " +
                "र लेखन वा मिडियामा दक्ष बनाउँछ।"
        )
        4 -> LocalizedParagraph(
            en = "Your ascendant lord ${lord.displayName} in the 4th house creates deep connection to home, " +
                "mother, and emotional security. Property, vehicles, and domestic happiness are emphasized. " +
                "Education and inner peace are important themes in your life.",
            ne = "तपाईंको लग्न स्वामी ${lord.displayName} चौथो भावमा घर, आमा र भावनात्मक सुरक्षासँग " +
                "गहिरो सम्बन्ध सिर्जना गर्छ।"
        )
        5 -> LocalizedParagraph(
            en = "Your ascendant lord ${lord.displayName} in the 5th house blesses you with creativity, " +
                "intelligence, and good fortune regarding children. Romance, speculation, and past-life " +
                "merit bring opportunities. You naturally attract good luck through creative expression.",
            ne = "तपाईंको लग्न स्वामी ${lord.displayName} पाँचौं भावमा तपाईंलाई रचनात्मकता, " +
                "बुद्धिमत्ता र बच्चाहरूको सम्बन्धमा सौभाग्यको आशीर्वाद दिन्छ।"
        )
        6 -> LocalizedParagraph(
            en = "Your ascendant lord ${lord.displayName} in the 6th house gives you ability to overcome enemies " +
                "and competition. Health requires attention, but you develop strength through challenges. " +
                "Service professions or competitive fields suit you.",
            ne = "तपाईंको लग्न स्वामी ${lord.displayName} छैटौं भावमा तपाईंलाई शत्रुहरू र " +
                "प्रतिस्पर्धा पार गर्ने क्षमता दिन्छ।"
        )
        7 -> LocalizedParagraph(
            en = "Your ascendant lord ${lord.displayName} in the 7th house makes partnerships and marriage " +
                "central to your life path. Business partnerships can be fortunate, and you define yourself " +
                "significantly through relationships with others.",
            ne = "तपाईंको लग्न स्वामी ${lord.displayName} सातौं भावमा साझेदारी र विवाहलाई " +
                "तपाईंको जीवन मार्गको केन्द्र बनाउँछ।"
        )
        8 -> LocalizedParagraph(
            en = "Your ascendant lord ${lord.displayName} in the 8th house creates a life of transformation " +
                "and research into hidden matters. Inheritance, occult knowledge, and dealing with crisis " +
                "are themes. You possess remarkable regenerative ability.",
            ne = "तपाईंको लग्न स्वामी ${lord.displayName} आठौं भावमा परिवर्तन र लुकेका मामिलाहरूको " +
                "अनुसन्धानको जीवन सिर्जना गर्छ।"
        )
        9 -> LocalizedParagraph(
            en = "Your ascendant lord ${lord.displayName} in the 9th house is highly fortunate, blessing you " +
                "with luck, higher education, spirituality, and long journeys. Father may be influential. " +
                "You naturally attract opportunities and spiritual guidance.",
            ne = "तपाईंको लग्न स्वामी ${lord.displayName} नवौं भावमा अत्यन्त भाग्यशाली छ, " +
                "भाग्य, उच्च शिक्षा, आध्यात्मिकता र लामो यात्राहरूको आशीर्वाद दिन्छ।"
        )
        10 -> LocalizedParagraph(
            en = "Your ascendant lord ${lord.displayName} in the 10th house creates strong career focus " +
                "and public recognition. You naturally gravitate toward positions of authority, and " +
                "professional success comes through your own efforts and visibility.",
            ne = "तपाईंको लग्न स्वामी ${lord.displayName} दशौं भावमा बलियो क्यारियर फोकस र " +
                "सार्वजनिक मान्यता सिर्जना गर्छ।"
        )
        11 -> LocalizedParagraph(
            en = "Your ascendant lord ${lord.displayName} in the 11th house promises fulfillment of desires " +
                "and gains through elder siblings or influential friends. Social networks bring opportunities, " +
                "and your ambitions find support from well-wishers.",
            ne = "तपाईंको लग्न स्वामी ${lord.displayName} एघारौं भावमा इच्छाहरूको पूर्ति र " +
                "जेठा भाइबहिनी वा प्रभावशाली साथीहरूबाट लाभको वाचा गर्छ।"
        )
        12 -> LocalizedParagraph(
            en = "Your ascendant lord ${lord.displayName} in the 12th house creates a path of spiritual seeking, " +
                "foreign residence, or working behind the scenes. Liberation and transcendence are themes. " +
                "You may find success abroad or in spiritual/healing professions.",
            ne = "तपाईंको लग्न स्वामी ${lord.displayName} बाह्रौं भावमा आध्यात्मिक खोज, " +
                "विदेशी बसोबास वा पर्दा पछाडि काम गर्ने मार्ग सिर्जना गर्छ।"
        )
        else -> LocalizedParagraph("", "")
    }
    
    private fun getLifeDirection(house: Int, dignity: PlanetaryDignityLevel): LocalizedParagraph {
        val dignityModifier = when (dignity) {
            PlanetaryDignityLevel.EXALTED -> "with exceptional support and ease"
            PlanetaryDignityLevel.MOOLATRIKONA, PlanetaryDignityLevel.OWN_SIGN -> "with natural strength"
            PlanetaryDignityLevel.FRIEND_SIGN -> "with supportive influences"
            PlanetaryDignityLevel.NEUTRAL -> "with balanced energies"
            PlanetaryDignityLevel.ENEMY_SIGN -> "requiring conscious effort"
            PlanetaryDignityLevel.DEBILITATED -> "through overcoming challenges"
        }
        
        val houseTheme = when (house) {
            1 -> "self-development and personal identity"
            2 -> "wealth accumulation and family values"
            3 -> "communication, courage, and initiative"
            4 -> "home, education, and emotional security"
            5 -> "creativity, children, and good fortune"
            6 -> "service, health, and overcoming obstacles"
            7 -> "partnerships, marriage, and public dealings"
            8 -> "transformation, research, and hidden knowledge"
            9 -> "higher wisdom, travel, and spiritual growth"
            10 -> "career achievement and public recognition"
            11 -> "fulfillment of desires and social gains"
            12 -> "spiritual liberation and transcendence"
            else -> "various life matters"
        }
        
        return LocalizedParagraph(
            en = "Your life direction flows toward $houseTheme, $dignityModifier. " +
                "This placement indicates where you naturally invest your energy and find purpose.",
            ne = "तपाईंको जीवन दिशा ${houseTheme}तर्फ बग्छ। " +
                "यो स्थानले तपाईं स्वाभाविक रूपमा आफ्नो ऊर्जा कहाँ लगानी गर्नुहुन्छ र उद्देश्य फेला पार्नुहुन्छ भनेर संकेत गर्छ।"
        )
    }
}
