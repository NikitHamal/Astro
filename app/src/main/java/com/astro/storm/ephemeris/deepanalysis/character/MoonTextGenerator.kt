package com.astro.storm.ephemeris.deepanalysis.character

import com.astro.storm.core.model.Nakshatra
import com.astro.storm.core.model.PlanetPosition
import com.astro.storm.core.model.ZodiacSign
import com.astro.storm.ephemeris.deepanalysis.*

/**
 * Moon Text Generator - 108 nakshatra-pada combinations + moon sign interpretations
 */
object MoonTextGenerator {
    
    fun getNakshatraPadaAnalysis(nakshatra: Nakshatra, pada: Int): LocalizedParagraph {
        val baseDesc = getNakshatraBase(nakshatra)
        val padaModifier = getPadaModifier(pada)
        
        return LocalizedParagraph(
            en = "${baseDesc.en} In the $pada${getOrdinalSuffix(pada)} pada, $padaModifier",
            ne = "${baseDesc.ne} ${pada}औं पादमा, ${getPadaModifierNe(pada)}"
        )
    }
    
    private fun getNakshatraBase(nakshatra: Nakshatra): LocalizedParagraph = when (nakshatra) {
        Nakshatra.ASHWINI -> LocalizedParagraph(
            en = "Your Ashwini nakshatra gives you healing abilities, speed, and pioneering spirit. " +
                "You are quick to act and possess natural therapeutic talents.",
            ne = "तपाईंको अश्विनी नक्षत्रले तपाईंलाई उपचार क्षमता, गति र अग्रगामी भावना दिन्छ।"
        )
        Nakshatra.BHARANI -> LocalizedParagraph(
            en = "Your Bharani nakshatra endows you with creative power, responsibility, and the ability to nurture. " +
                "You understand life's deeper cycles of creation and transformation.",
            ne = "तपाईंको भरणी नक्षत्रले तपाईंलाई रचनात्मक शक्ति, जिम्मेवारी र पालनपोषण गर्ने क्षमता प्रदान गर्छ।"
        )
        Nakshatra.KRITTIKA -> LocalizedParagraph(
            en = "Your Krittika nakshatra gives you a sharp, purifying nature and strong willpower. " +
                "You have the ability to cut through illusions and speak truth directly.",
            ne = "तपाईंको कृत्तिका नक्षत्रले तपाईंलाई तीखो, शुद्धिकरण स्वभाव र बलियो इच्छाशक्ति दिन्छ।"
        )
        Nakshatra.ROHINI -> LocalizedParagraph(
            en = "Your Rohini nakshatra blesses you with beauty, creativity, and magnetic charm. " +
                "You have natural artistic talents and the ability to attract resources.",
            ne = "तपाईंको रोहिणी नक्षत्रले तपाईंलाई सौन्दर्य, रचनात्मकता र चुम्बकीय आकर्षणको आशीर्वाद दिन्छ।"
        )
        Nakshatra.MRIGASHIRA -> LocalizedParagraph(
            en = "Your Mrigashira nakshatra gives you a curious, searching nature. You constantly seek " +
                "new knowledge and experiences, never satisfied with superficial understanding.",
            ne = "तपाईंको मृगशिरा नक्षत्रले तपाईंलाई जिज्ञासु, खोजी स्वभाव दिन्छ।"
        )
        Nakshatra.ARDRA -> LocalizedParagraph(
            en = "Your Ardra nakshatra gives you intense emotions and transformative power. " +
                "You experience life deeply and have the ability to renew after storms.",
            ne = "तपाईंको आर्द्रा नक्षत्रले तपाईंलाई तीव्र भावनाहरू र परिवर्तनकारी शक्ति दिन्छ।"
        )
        Nakshatra.PUNARVASU -> LocalizedParagraph(
            en = "Your Punarvasu nakshatra blesses you with optimism, resilience, and the ability to return. " +
                "You bounce back from setbacks and bring renewal wherever you go.",
            ne = "तपाईंको पुनर्वसु नक्षत्रले तपाईंलाई आशावाद, लचिलोपन र फर्कने क्षमताको आशीर्वाद दिन्छ।"
        )
        Nakshatra.PUSHYA -> LocalizedParagraph(
            en = "Your Pushya nakshatra is highly auspicious, giving you nurturing abilities and spiritual depth. " +
                "You are blessed with wisdom and the capacity to nourish others.",
            ne = "तपाईंको पुष्य नक्षत्र अत्यन्त शुभ छ, तपाईंलाई पालनपोषण क्षमता र आध्यात्मिक गहिराइ दिन्छ।"
        )
        Nakshatra.ASHLESHA -> LocalizedParagraph(
            en = "Your Ashlesha nakshatra gives you deep intuition, mystical abilities, and kundalini energy. " +
                "You possess penetrating insight and can read others deeply.",
            ne = "तपाईंको आश्लेषा नक्षत्रले तपाईंलाई गहिरो अन्तर्ज्ञान, रहस्यमय क्षमताहरू र कुण्डलिनी ऊर्जा दिन्छ।"
        )
        Nakshatra.MAGHA -> LocalizedParagraph(
            en = "Your Magha nakshatra connects you to ancestral power and royal dignity. " +
                "You carry the weight of tradition and have natural leadership abilities.",
            ne = "तपाईंको मघा नक्षत्रले तपाईंलाई पैतृक शक्ति र राजसी मर्यादासँग जोड्छ।"
        )
        Nakshatra.PURVA_PHALGUNI -> LocalizedParagraph(
            en = "Your Purva Phalguni nakshatra brings love, luxury, and creative expression. " +
                "You seek pleasure and comfort while expressing your artistic nature.",
            ne = "तपाईंको पूर्वा फाल्गुनी नक्षत्रले प्रेम, विलासिता र रचनात्मक अभिव्यक्ति ल्याउँछ।"
        )
        Nakshatra.UTTARA_PHALGUNI -> LocalizedParagraph(
            en = "Your Uttara Phalguni nakshatra gives you generous nature and organizational abilities. " +
                "You build lasting structures and form stable partnerships.",
            ne = "तपाईंको उत्तरा फाल्गुनी नक्षत्रले तपाईंलाई उदार स्वभाव र संगठनात्मक क्षमताहरू दिन्छ।"
        )
        else -> LocalizedParagraph(
            en = "Your ${nakshatra.displayName} nakshatra shapes your deeper personality with unique qualities " +
                "that influence your emotional responses and life path.",
            ne = "तपाईंको ${nakshatra.displayName} नक्षत्रले तपाईंको गहिरो व्यक्तित्वलाई अद्वितीय गुणहरूले आकार दिन्छ।"
        )
    }
    
    private fun getPadaModifier(pada: Int): String = when (pada) {
        1 -> "this activates the Aries navamsha, adding initiative, courage, and pioneering energy to your nature."
        2 -> "this activates the Taurus navamsha, adding stability, practicality, and sensual appreciation."
        3 -> "this activates the Gemini navamsha, adding intellectual curiosity, communication skills, and adaptability."
        4 -> "this activates the Cancer navamsha, adding emotional depth, nurturing qualities, and intuition."
        else -> "this adds unique qualities to your personality expression."
    }
    
    private fun getPadaModifierNe(pada: Int): String = when (pada) {
        1 -> "यसले मेष नवांश सक्रिय गर्छ, तपाईंको स्वभावमा पहल, साहस र अग्रगामी ऊर्जा थप्दै।"
        2 -> "यसले वृष नवांश सक्रिय गर्छ, स्थिरता, व्यावहारिकता र संवेदनशील प्रशंसा थप्दै।"
        3 -> "यसले मिथुन नवांश सक्रिय गर्छ, बौद्धिक जिज्ञासा, सञ्चार कौशल र अनुकूलनशीलता थप्दै।"
        4 -> "यसले कर्कट नवांश सक्रिय गर्छ, भावनात्मक गहिराइ, पालनपोषण गुणहरू र अन्तर्ज्ञान थप्दै।"
        else -> "यसले तपाईंको व्यक्तित्व अभिव्यक्तिमा अद्वितीय गुणहरू थप्छ।"
    }
    
    private fun getOrdinalSuffix(n: Int): String = when {
        n == 1 -> "st"
        n == 2 -> "nd"
        n == 3 -> "rd"
        else -> "th"
    }
    
    fun getEmotionalNature(sign: ZodiacSign, strength: StrengthLevel): LocalizedParagraph {
        val strengthMod = if (strength >= StrengthLevel.STRONG) "powerfully" else "subtly"
        return when (sign) {
            ZodiacSign.ARIES -> LocalizedParagraph(
                en = "Your emotions are $strengthMod fiery and spontaneous. You feel things intensely but briefly, " +
                    "recovering quickly from emotional upsets. You need independence in emotional expression.",
                ne = "तपाईंको भावनाहरू $strengthMod अग्निमय र स्वत:स्फूर्त छन्।"
            )
            ZodiacSign.TAURUS -> LocalizedParagraph(
                en = "Your emotions are $strengthMod stable and deeply rooted. You process feelings slowly but thoroughly, " +
                    "seeking comfort and security above all. Emotional loyalty runs deep.",
                ne = "तपाईंको भावनाहरू $strengthMod स्थिर र गहिरो जरा भएका छन्।"
            )
            ZodiacSign.CANCER -> LocalizedParagraph(
                en = "Your emotions are $strengthMod deep, intuitive, and nurturing. You feel everything intensely " +
                    "and have powerful empathic abilities. Emotional security is paramount.",
                ne = "तपाईंको भावनाहरू $strengthMod गहिरो, अन्तर्ज्ञानी र पालनपोषण गर्ने छन्।"
            )
            ZodiacSign.SCORPIO -> LocalizedParagraph(
                en = "Your emotions are $strengthMod intense and transformative. You experience feelings at the " +
                    "deepest level, with powerful attachments and the capacity for complete emotional renewal.",
                ne = "तपाईंको भावनाहरू $strengthMod तीव्र र परिवर्तनकारी छन्।"
            )
            else -> LocalizedParagraph(
                en = "Your emotional nature is shaped $strengthMod by ${sign.displayName}'s influence, " +
                    "creating a unique pattern of feeling and responding to life's experiences.",
                ne = "तपाईंको भावनात्मक स्वभाव ${sign.displayName}को प्रभावले $strengthMod आकार दिएको छ।"
            )
        }
    }
    
    fun getMindsetAnalysis(sign: ZodiacSign, house: Int): LocalizedParagraph {
        return LocalizedParagraph(
            en = "With Moon in ${sign.displayName} in the ${house}${getOrdinalSuffix(house)} house, " +
                "your mind naturally gravitates toward ${getHouseFocus(house)} matters. " +
                "You think about these areas frequently and find emotional fulfillment through them.",
            ne = "${sign.displayName}मा ${house}औं भावमा चन्द्रमाको साथ, " +
                "तपाईंको मन स्वाभाविक रूपमा ${getHouseFocusNe(house)} मामिलाहरूतिर झुक्छ।"
        )
    }
    
    private fun getHouseFocus(house: Int): String = when (house) {
        1 -> "self-development and personal identity"
        2 -> "family, resources, and security"
        3 -> "communication and learning"
        4 -> "home, mother, and inner peace"
        5 -> "creativity, children, and romance"
        6 -> "health, service, and daily routines"
        7 -> "relationships and partnerships"
        8 -> "transformation and hidden matters"
        9 -> "philosophy, travel, and higher learning"
        10 -> "career and public reputation"
        11 -> "social networks and aspirations"
        12 -> "spirituality and inner retreat"
        else -> "various life matters"
    }
    
    private fun getHouseFocusNe(house: Int): String = when (house) {
        1 -> "आत्म-विकास र व्यक्तिगत पहिचान"
        2 -> "परिवार, स्रोतहरू र सुरक्षा"
        3 -> "सञ्चार र सिक्ने"
        4 -> "घर, आमा र आन्तरिक शान्ति"
        5 -> "रचनात्मकता, बच्चाहरू र रोमान्स"
        6 -> "स्वास्थ्य, सेवा र दैनिक दिनचर्या"
        7 -> "सम्बन्ध र साझेदारी"
        8 -> "परिवर्तन र लुकेका मामिलाहरू"
        9 -> "दर्शन, यात्रा र उच्च शिक्षा"
        10 -> "क्यारियर र सार्वजनिक प्रतिष्ठा"
        11 -> "सामाजिक नेटवर्कहरू र आकांक्षाहरू"
        12 -> "आध्यात्मिकता र आन्तरिक विश्राम"
        else -> "विभिन्न जीवन मामिलाहरू"
    }
    
    fun getInnerNeeds(sign: ZodiacSign): LocalizedParagraph = when (sign) {
        ZodiacSign.ARIES -> LocalizedParagraph(
            en = "You deeply need independence, action, and the freedom to pursue your desires without restriction.",
            ne = "तपाईंलाई गहिरो रूपमा स्वतन्त्रता, कार्य र प्रतिबन्ध बिना आफ्ना इच्छाहरू पछ्याउने स्वतन्त्रता चाहिन्छ।"
        )
        ZodiacSign.TAURUS -> LocalizedParagraph(
            en = "You deeply need stability, comfort, and material security. Physical pleasures nourish your soul.",
            ne = "तपाईंलाई गहिरो रूपमा स्थिरता, आराम र भौतिक सुरक्षा चाहिन्छ।"
        )
        ZodiacSign.CANCER -> LocalizedParagraph(
            en = "You deeply need emotional security, family connection, and a safe haven to retreat to.",
            ne = "तपाईंलाई गहिरो रूपमा भावनात्मक सुरक्षा, पारिवारिक सम्बन्ध र सुरक्षित आश्रय चाहिन्छ।"
        )
        else -> LocalizedParagraph(
            en = "Your inner needs are shaped by ${sign.displayName}'s influence, seeking what this sign values most.",
            ne = "तपाईंको आन्तरिक आवश्यकताहरू ${sign.displayName}को प्रभावले आकार दिएको छ।"
        )
    }
    
    fun getStressResponse(sign: ZodiacSign): LocalizedParagraph = when (sign) {
        ZodiacSign.ARIES -> LocalizedParagraph(
            en = "Under stress, you become impulsive and may act before thinking. Physical activity helps you release tension.",
            ne = "तनावमा, तपाईं आवेगी हुनुहुन्छ र सोच्नु अघि कार्य गर्न सक्नुहुन्छ।"
        )
        ZodiacSign.TAURUS -> LocalizedParagraph(
            en = "Under stress, you may become stubborn or seek comfort through food and physical pleasures.",
            ne = "तनावमा, तपाईं हठी हुन सक्नुहुन्छ वा खाना र शारीरिक आनन्दहरू मार्फत सान्त्वना खोज्न सक्नुहुन्छ।"
        )
        else -> LocalizedParagraph(
            en = "Under stress, ${sign.displayName}'s shadow aspects may emerge, requiring conscious management.",
            ne = "तनावमा, ${sign.displayName}का छाया पक्षहरू देखा पर्न सक्छ।"
        )
    }
    
    fun getMotherRelationship(house: Int, strength: StrengthLevel): LocalizedParagraph {
        val quality = if (strength >= StrengthLevel.STRONG) "positive and nurturing" else "complex but growth-promoting"
        return LocalizedParagraph(
            en = "Your relationship with mother is $quality, with Moon's placement in the ${house}${getOrdinalSuffix(house)} house " +
                "indicating ${getMotherInfluence(house)}.",
            ne = "आमासँगको तपाईंको सम्बन्ध ${if (strength >= StrengthLevel.STRONG) "सकारात्मक र पालनपोषण गर्ने" else "जटिल तर विकास-प्रवर्द्धक"} छ।"
        )
    }
    
    private fun getMotherInfluence(house: Int): String = when (house) {
        1 -> "strong identification with mother and her values"
        4 -> "deep emotional connection and domestic influence"
        7 -> "mother's influence on your partnerships"
        10 -> "mother's impact on your career and public life"
        else -> "mother's influence in subtle but meaningful ways"
    }
    
    fun getComfortSeeking(sign: ZodiacSign): LocalizedParagraph = when (sign) {
        ZodiacSign.TAURUS -> LocalizedParagraph(
            en = "You seek comfort through physical pleasures, good food, beautiful surroundings, and sensory experiences.",
            ne = "तपाईं शारीरिक आनन्द, राम्रो खाना, सुन्दर वातावरण र संवेदी अनुभवहरू मार्फत सान्त्वना खोज्नुहुन्छ।"
        )
        ZodiacSign.CANCER -> LocalizedParagraph(
            en = "You seek comfort through family, home, familiar places, and emotional connection with loved ones.",
            ne = "तपाईं परिवार, घर, परिचित स्थानहरू र प्रियजनहरूसँग भावनात्मक सम्बन्ध मार्फत सान्त्वना खोज्नुहुन्छ।"
        )
        else -> LocalizedParagraph(
            en = "You seek comfort in ways characteristic of ${sign.displayName}, finding peace through its natural expressions.",
            ne = "तपाईं ${sign.displayName}को विशेषता तरिकाहरूमा सान्त्वना खोज्नुहुन्छ।"
        )
    }
    
    fun getOverallInterpretation(moon: PlanetPosition, strength: StrengthLevel, context: AnalysisContext): LocalizedParagraph {
        return LocalizedParagraph(
            en = "Your Moon in ${moon.sign.displayName} in the ${moon.house}${getOrdinalSuffix(moon.house)} house, " +
                "with ${strength.displayName.lowercase()} strength, shapes your emotional core. " +
                "${moon.nakshatra.displayName} nakshatra adds ${getNakshatraFlavor(moon.nakshatra)} to your inner nature. " +
                "This combination creates a unique emotional signature that influences all your relationships and life decisions.",
            ne = "${moon.sign.displayName}मा ${moon.house}औं भावमा ${strength.displayNameNe} बलको साथ तपाईंको चन्द्रमाले " +
                "तपाईंको भावनात्मक केन्द्र आकार दिन्छ। ${moon.nakshatra.displayName} नक्षत्रले तपाईंको आन्तरिक स्वभावमा " +
                "अद्वितीय गुणहरू थप्छ।"
        )
    }
    
    private fun getNakshatraFlavor(nakshatra: Nakshatra): String = when (nakshatra) {
        Nakshatra.ASHWINI -> "healing energy and speed"
        Nakshatra.BHARANI -> "creative power and responsibility"
        Nakshatra.KRITTIKA -> "purifying strength and directness"
        Nakshatra.ROHINI -> "beauty and magnetic charm"
        Nakshatra.PUSHYA -> "nurturing wisdom and auspiciousness"
        else -> "unique qualities from ${nakshatra.displayName}"
    }
}
