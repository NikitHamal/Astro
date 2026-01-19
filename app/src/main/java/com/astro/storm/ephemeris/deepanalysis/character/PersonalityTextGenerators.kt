package com.astro.storm.ephemeris.deepanalysis.character

import com.astro.storm.core.model.Planet
import com.astro.storm.core.model.ZodiacSign
import com.astro.storm.ephemeris.deepanalysis.*

/**
 * Temperament Text Generator - Element/Modality based interpretations
 */
object TemperamentTextGenerator {
    
    fun getDescription(element: Element, modality: Modality): LocalizedParagraph {
        val elementDesc = when (element) {
            Element.FIRE -> "passionate, enthusiastic, and action-oriented"
            Element.EARTH -> "practical, grounded, and materially focused"
            Element.AIR -> "intellectual, communicative, and socially oriented"
            Element.WATER -> "emotional, intuitive, and deeply feeling"
        }
        
        val modalityDesc = when (modality) {
            Modality.CARDINAL -> "initiative to start new ventures"
            Modality.FIXED -> "persistence to see things through"
            Modality.MUTABLE -> "adaptability to changing circumstances"
        }
        
        return LocalizedParagraph(
            en = "Your temperament is primarily $elementDesc with ${modality.displayName.lowercase()} $modalityDesc. " +
                "This combination shapes how you approach life's challenges and opportunities.",
            ne = "तपाईंको स्वभाव मुख्यतया ${element.displayNameNe} प्रधान ${modality.displayNameNe} $modalityDesc सँग छ।"
        )
    }
    
    fun getTendencies(element: Element, modality: Modality): List<LocalizedTrait> {
        val elementTraits = when (element) {
            Element.FIRE -> listOf(
                LocalizedTrait("Enthusiastic initiative", "उत्साही पहल", StrengthLevel.STRONG),
                LocalizedTrait("Natural leadership", "प्राकृतिक नेतृत्व", StrengthLevel.STRONG),
                LocalizedTrait("Quick action", "छिटो कार्य", StrengthLevel.MODERATE)
            )
            Element.EARTH -> listOf(
                LocalizedTrait("Practical approach", "व्यावहारिक दृष्टिकोण", StrengthLevel.STRONG),
                LocalizedTrait("Material focus", "भौतिक फोकस", StrengthLevel.STRONG),
                LocalizedTrait("Patient building", "धैर्यवान निर्माण", StrengthLevel.MODERATE)
            )
            Element.AIR -> listOf(
                LocalizedTrait("Intellectual curiosity", "बौद्धिक जिज्ञासा", StrengthLevel.STRONG),
                LocalizedTrait("Communication skills", "सञ्चार कौशल", StrengthLevel.STRONG),
                LocalizedTrait("Social adaptability", "सामाजिक अनुकूलनशीलता", StrengthLevel.MODERATE)
            )
            Element.WATER -> listOf(
                LocalizedTrait("Emotional intelligence", "भावनात्मक बुद्धि", StrengthLevel.STRONG),
                LocalizedTrait("Intuitive insight", "अन्तर्ज्ञानी अन्तर्दृष्टि", StrengthLevel.STRONG),
                LocalizedTrait("Empathic connection", "सहानुभूतिपूर्ण सम्बन्ध", StrengthLevel.MODERATE)
            )
        }
        return elementTraits
    }
}

/**
 * Emotional Text Generator - Moon-based emotional patterns
 */
object EmotionalTextGenerator {
    
    fun getExpression(sign: ZodiacSign, strength: StrengthLevel): LocalizedParagraph {
        return LocalizedParagraph(
            en = "You express emotions through ${sign.displayName}'s filter, with ${strength.displayName.lowercase()} " +
                "emotional stability shaping how you process and share feelings with others.",
            ne = "तपाईं ${sign.displayName}को फिल्टर मार्फत भावनाहरू व्यक्त गर्नुहुन्छ।"
        )
    }
    
    fun getTriggers(sign: ZodiacSign): List<LocalizedTrait> = when (sign) {
        ZodiacSign.ARIES -> listOf(
            LocalizedTrait("Being held back", "रोकिनु", StrengthLevel.STRONG),
            LocalizedTrait("Perceived injustice", "अनुभूत अन्याय", StrengthLevel.MODERATE)
        )
        ZodiacSign.CANCER -> listOf(
            LocalizedTrait("Family criticism", "पारिवारिक आलोचना", StrengthLevel.STRONG),
            LocalizedTrait("Feeling unneeded", "आवश्यक नभएको महसुस", StrengthLevel.STRONG)
        )
        else -> listOf(
            LocalizedTrait("Sign-specific triggers", "राशि-विशिष्ट ट्रिगरहरू", StrengthLevel.MODERATE)
        )
    }
    
    fun getStrengths(sign: ZodiacSign, strength: StrengthLevel): List<LocalizedTrait> = when (sign) {
        ZodiacSign.CANCER, ZodiacSign.PISCES -> listOf(
            LocalizedTrait("Deep empathy", "गहिरो सहानुभूति", StrengthLevel.EXCELLENT),
            LocalizedTrait("Nurturing nature", "पालनपोषण स्वभाव", StrengthLevel.STRONG)
        )
        ZodiacSign.TAURUS, ZodiacSign.CAPRICORN -> listOf(
            LocalizedTrait("Emotional stability", "भावनात्मक स्थिरता", StrengthLevel.STRONG),
            LocalizedTrait("Reliability", "विश्वसनीयता", StrengthLevel.STRONG)
        )
        else -> listOf(
            LocalizedTrait("Emotional awareness", "भावनात्मक जागरूकता", strength)
        )
    }
    
    fun getChallenges(sign: ZodiacSign, strength: StrengthLevel): List<LocalizedTrait> = when (sign) {
        ZodiacSign.ARIES -> listOf(
            LocalizedTrait("Impulsiveness", "आवेग", StrengthLevel.MODERATE),
            LocalizedTrait("Quick anger", "छिटो रिस", StrengthLevel.MODERATE)
        )
        ZodiacSign.SCORPIO -> listOf(
            LocalizedTrait("Intensity", "तीव्रता", StrengthLevel.STRONG),
            LocalizedTrait("Holding grudges", "रिस राख्ने", StrengthLevel.MODERATE)
        )
        else -> listOf(
            LocalizedTrait("Sign-specific challenges", "राशि-विशिष्ट चुनौतीहरू", StrengthLevel.MODERATE)
        )
    }
    
    fun getGrowthPath(sign: ZodiacSign): LocalizedParagraph {
        return LocalizedParagraph(
            en = "Your emotional growth path involves developing the positive qualities of ${sign.displayName} " +
                "while learning to balance its challenging aspects.",
            ne = "तपाईंको भावनात्मक विकास मार्गमा ${sign.displayName}का सकारात्मक गुणहरू विकास गर्नु समावेश छ।"
        )
    }
}

/**
 * Intellectual Text Generator - Mercury-based mental patterns
 */
object IntellectTextGenerator {
    
    fun getLearningStyle(sign: ZodiacSign?, strength: StrengthLevel): LocalizedParagraph {
        val signName = sign?.displayName ?: "your signs"
        return LocalizedParagraph(
            en = "You learn best through ${getLearningApproach(sign)} methods. Your ${strength.displayName.lowercase()} " +
                "Mercury makes intellectual pursuits ${if (strength >= StrengthLevel.STRONG) "natural and enjoyable" else "growth opportunities"}.",
            ne = "तपाईं ${getLearningApproachNe(sign)} विधिहरू मार्फत सबैभन्दा राम्रोसँग सिक्नुहुन्छ।"
        )
    }
    
    private fun getLearningApproach(sign: ZodiacSign?): String = when (sign) {
        ZodiacSign.GEMINI, ZodiacSign.VIRGO -> "analytical and detailed"
        ZodiacSign.SAGITTARIUS, ZodiacSign.AQUARIUS -> "conceptual and big-picture"
        ZodiacSign.ARIES, ZodiacSign.LEO -> "hands-on and experiential"
        else -> "varied and adaptive"
    }
    
    private fun getLearningApproachNe(sign: ZodiacSign?): String = when (sign) {
        ZodiacSign.GEMINI, ZodiacSign.VIRGO -> "विश्लेषणात्मक र विस्तृत"
        ZodiacSign.SAGITTARIUS, ZodiacSign.AQUARIUS -> "अवधारणात्मक र ठूलो तस्वीर"
        else -> "विविध र अनुकूली"
    }
    
    fun getCommunicationStyle(sign: ZodiacSign?, strength: StrengthLevel): LocalizedParagraph {
        return LocalizedParagraph(
            en = "You communicate with ${getCommunicationQuality(sign)} style. Your words carry " +
                "${if (strength >= StrengthLevel.STRONG) "significant impact and clarity" else "developing influence"}.",
            ne = "तपाईं ${getCommunicationQualityNe(sign)} शैलीको साथ सञ्चार गर्नुहुन्छ।"
        )
    }
    
    private fun getCommunicationQuality(sign: ZodiacSign?): String = when (sign) {
        ZodiacSign.GEMINI -> "quick, versatile, and curious"
        ZodiacSign.VIRGO -> "precise, analytical, and helpful"
        ZodiacSign.SAGITTARIUS -> "expansive, philosophical, and direct"
        else -> "characteristic"
    }
    
    private fun getCommunicationQualityNe(sign: ZodiacSign?): String = when (sign) {
        ZodiacSign.GEMINI -> "छिटो, बहुमुखी र जिज्ञासु"
        ZodiacSign.VIRGO -> "सटीक, विश्लेषणात्मक र सहायक"
        else -> "विशेषता"
    }
    
    fun getStrengths(sign: ZodiacSign?, strength: StrengthLevel): List<LocalizedTrait> {
        return if (strength >= StrengthLevel.STRONG) {
            listOf(
                LocalizedTrait("Quick learning", "छिटो सिक्ने", StrengthLevel.STRONG),
                LocalizedTrait("Clear expression", "स्पष्ट अभिव्यक्ति", StrengthLevel.STRONG)
            )
        } else {
            listOf(LocalizedTrait("Growing intellect", "बढ्दो बुद्धि", StrengthLevel.MODERATE))
        }
    }
    
    fun getChallenges(sign: ZodiacSign?, strength: StrengthLevel): List<LocalizedTrait> {
        return if (strength <= StrengthLevel.WEAK) {
            listOf(
                LocalizedTrait("Communication blocks", "सञ्चार अवरोधहरू", StrengthLevel.MODERATE),
                LocalizedTrait("Learning difficulties", "सिक्ने कठिनाइहरू", StrengthLevel.MODERATE)
            )
        } else {
            listOf(LocalizedTrait("Minor intellectual challenges", "सानो बौद्धिक चुनौतीहरू", StrengthLevel.WEAK))
        }
    }
}

/**
 * Social Text Generator - Ascendant-based social behavior
 */
object SocialTextGenerator {
    
    fun getSocialOrientation(sign: ZodiacSign): LocalizedParagraph = when (sign) {
        ZodiacSign.ARIES -> LocalizedParagraph(
            en = "You are socially assertive and naturally take the lead in group settings.",
            ne = "तपाईं सामाजिक रूपमा दृढ हुनुहुन्छ र समूह सेटिंगहरूमा स्वाभाविक रूपमा नेतृत्व लिनुहुन्छ।"
        )
        ZodiacSign.LIBRA -> LocalizedParagraph(
            en = "You are socially harmonious and excel at building diplomatic relationships.",
            ne = "तपाईं सामाजिक रूपमा सामंजस्यपूर्ण हुनुहुन्छ र कूटनीतिक सम्बन्धहरू निर्माण गर्नमा उत्कृष्ट हुनुहुन्छ।"
        )
        else -> LocalizedParagraph(
            en = "Your social orientation reflects ${sign.displayName}'s natural approach to group dynamics.",
            ne = "तपाईंको सामाजिक अभिमुखीकरणले ${sign.displayName}को समूह गतिशीलतातर्फको प्राकृतिक दृष्टिकोण प्रतिबिम्बित गर्छ।"
        )
    }
    
    fun getRelationshipApproach(sign: ZodiacSign, venusStrength: StrengthLevel): LocalizedParagraph {
        return LocalizedParagraph(
            en = "You approach relationships with ${sign.displayName}'s characteristic style, " +
                "${if (venusStrength >= StrengthLevel.STRONG) "enhanced by strong Venus charm" else "developing relationship skills"}.",
            ne = "तपाईं ${sign.displayName}को विशेषता शैलीको साथ सम्बन्धहरूमा दृष्टिकोण राख्नुहुन्छ।"
        )
    }
    
    fun getGroupCommunication(sign: ZodiacSign): LocalizedParagraph {
        return LocalizedParagraph(
            en = "In groups, you naturally ${getGroupRole(sign)}, reflecting your rising sign's influence.",
            ne = "समूहहरूमा, तपाईं स्वाभाविक रूपमा ${getGroupRoleNe(sign)}, तपाईंको उदय राशिको प्रभाव प्रतिबिम्बित गर्दै।"
        )
    }
    
    private fun getGroupRole(sign: ZodiacSign): String = when (sign) {
        ZodiacSign.ARIES, ZodiacSign.LEO -> "take charge and lead discussions"
        ZodiacSign.GEMINI, ZodiacSign.LIBRA -> "facilitate communication and build connections"
        ZodiacSign.CANCER, ZodiacSign.PISCES -> "create emotional harmony and support others"
        else -> "contribute in your characteristic way"
    }
    
    private fun getGroupRoleNe(sign: ZodiacSign): String = when (sign) {
        ZodiacSign.ARIES, ZodiacSign.LEO -> "जिम्मेवारी लिनुहुन्छ र छलफलको नेतृत्व गर्नुहुन्छ"
        ZodiacSign.GEMINI, ZodiacSign.LIBRA -> "सञ्चार सुविधा दिनुहुन्छ र सम्बन्धहरू निर्माण गर्नुहुन्छ"
        else -> "तपाईंको विशेषता तरिकामा योगदान गर्नुहुन्छ"
    }
    
    fun getLeadershipTendency(sign: ZodiacSign, context: AnalysisContext): LocalizedParagraph {
        val sunStrength = context.getPlanetStrengthLevel(Planet.SUN)
        val isLeader = sign in listOf(ZodiacSign.ARIES, ZodiacSign.LEO, ZodiacSign.CAPRICORN) || 
            sunStrength >= StrengthLevel.STRONG
        
        return LocalizedParagraph(
            en = "You ${if (isLeader) "naturally gravitate toward leadership roles" else "contribute effectively as a team player"}, " +
                "with your ${sign.displayName} rising shaping your approach to authority.",
            ne = "तपाईं ${if (isLeader) "स्वाभाविक रूपमा नेतृत्व भूमिकाहरूतिर झुक्नुहुन्छ" else "टोली खेलाडीको रूपमा प्रभावकारी रूपमा योगदान गर्नुहुन्छ"}।"
        )
    }
    
    fun getSocialStrengths(sign: ZodiacSign): List<LocalizedTrait> = when (sign) {
        ZodiacSign.LEO, ZodiacSign.LIBRA -> listOf(
            LocalizedTrait("Natural charm", "प्राकृतिक आकर्षण", StrengthLevel.STRONG),
            LocalizedTrait("Social confidence", "सामाजिक आत्मविश्वास", StrengthLevel.STRONG)
        )
        ZodiacSign.GEMINI, ZodiacSign.SAGITTARIUS -> listOf(
            LocalizedTrait("Communication ease", "सञ्चार सहजता", StrengthLevel.STRONG),
            LocalizedTrait("Adaptability", "अनुकूलनशीलता", StrengthLevel.STRONG)
        )
        else -> listOf(LocalizedTrait("Social awareness", "सामाजिक जागरूकता", StrengthLevel.MODERATE))
    }
    
    fun getSocialChallenges(sign: ZodiacSign): List<LocalizedTrait> = when (sign) {
        ZodiacSign.SCORPIO, ZodiacSign.CAPRICORN -> listOf(
            LocalizedTrait("Social reserve", "सामाजिक आरक्षण", StrengthLevel.MODERATE),
            LocalizedTrait("Trust building", "विश्वास निर्माण", StrengthLevel.MODERATE)
        )
        ZodiacSign.CANCER, ZodiacSign.PISCES -> listOf(
            LocalizedTrait("Sensitivity in groups", "समूहमा संवेदनशीलता", StrengthLevel.MODERATE)
        )
        else -> listOf(LocalizedTrait("Sign-specific social patterns", "राशि-विशिष्ट सामाजिक ढाँचाहरू", StrengthLevel.WEAK))
    }
}
