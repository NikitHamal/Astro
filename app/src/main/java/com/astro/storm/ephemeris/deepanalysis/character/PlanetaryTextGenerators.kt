package com.astro.storm.ephemeris.deepanalysis.character

import com.astro.storm.core.model.Planet
import com.astro.storm.ephemeris.deepanalysis.*

/**
 * Planet Personality Generator - Planetary influence on personality
 */
object PlanetPersonalityGenerator {
    
    fun getContribution(planet: Planet, isStrong: Boolean): LocalizedParagraph = when (planet) {
        Planet.SUN -> if (isStrong) LocalizedParagraph(
            en = "Strong Sun contributes leadership ability, confidence, and vitality to your personality. " +
                "You naturally command respect and express yourself with authority.",
            ne = "बलियो सूर्यले तपाईंको व्यक्तित्वमा नेतृत्व क्षमता, आत्मविश्वास र जीवनी शक्ति योगदान गर्छ।"
        ) else LocalizedParagraph(
            en = "Challenged Sun creates growth opportunities in self-confidence and authority expression. " +
                "Relationship with father may require conscious healing.",
            ne = "चुनौतीपूर्ण सूर्यले आत्मविश्वास र अधिकार अभिव्यक्तिमा विकास अवसरहरू सिर्जना गर्छ।"
        )
        
        Planet.MOON -> if (isStrong) LocalizedParagraph(
            en = "Strong Moon blesses you with emotional intelligence, nurturing ability, and intuition. " +
                "You connect deeply with others and have a stable inner world.",
            ne = "बलियो चन्द्रमाले तपाईंलाई भावनात्मक बुद्धि, पालनपोषण क्षमता र अन्तर्ज्ञानको आशीर्वाद दिन्छ।"
        ) else LocalizedParagraph(
            en = "Challenged Moon indicates emotional processing work and mother-relationship healing. " +
                "Mindfulness practices can strengthen your emotional foundation.",
            ne = "चुनौतीपूर्ण चन्द्रमाले भावनात्मक प्रशोधन कार्य र आमा-सम्बन्ध उपचार संकेत गर्छ।"
        )
        
        Planet.MARS -> if (isStrong) LocalizedParagraph(
            en = "Strong Mars gives you courage, drive, and physical energy. You have the warrior spirit " +
                "to overcome obstacles and protect what matters to you.",
            ne = "बलियो मंगलले तपाईंलाई साहस, अभियान र शारीरिक ऊर्जा दिन्छ।"
        ) else LocalizedParagraph(
            en = "Challenged Mars may indicate anger management growth or physical energy that needs direction. " +
                "Channeling aggression into constructive activities helps.",
            ne = "चुनौतीपूर्ण मंगलले रिस व्यवस्थापन वृद्धि वा दिशा चाहिने शारीरिक ऊर्जा संकेत गर्न सक्छ।"
        )
        
        Planet.MERCURY -> if (isStrong) LocalizedParagraph(
            en = "Strong Mercury enhances your communication, analytical thinking, and learning ability. " +
                "You express ideas clearly and process information quickly.",
            ne = "बलियो बुधले तपाईंको सञ्चार, विश्लेषणात्मक सोच र सिक्ने क्षमता बढाउँछ।"
        ) else LocalizedParagraph(
            en = "Challenged Mercury creates opportunities for developing communication skills and clarity. " +
                "Writing and speaking practice strengthens this area.",
            ne = "चुनौतीपूर्ण बुधले सञ्चार कौशल र स्पष्टता विकास गर्ने अवसरहरू सिर्जना गर्छ।"
        )
        
        Planet.JUPITER -> if (isStrong) LocalizedParagraph(
            en = "Strong Jupiter brings wisdom, optimism, and good fortune to your nature. " +
                "You naturally attract opportunities and have a philosophical outlook.",
            ne = "बलियो बृहस्पतिले तपाईंको स्वभावमा ज्ञान, आशावाद र सौभाग्य ल्याउँछ।"
        ) else LocalizedParagraph(
            en = "Challenged Jupiter indicates growth in wisdom and faith development. " +
                "Spiritual practices and study can strengthen Jupiter's influence.",
            ne = "चुनौतीपूर्ण बृहस्पतिले ज्ञान र विश्वास विकासमा वृद्धि संकेत गर्छ।"
        )
        
        Planet.VENUS -> if (isStrong) LocalizedParagraph(
            en = "Strong Venus gives natural charm, artistic ability, and relationship harmony. " +
                "You appreciate beauty and attract love with ease.",
            ne = "बलियो शुक्रले प्राकृतिक आकर्षण, कलात्मक क्षमता र सम्बन्ध सामंजस्य दिन्छ।"
        ) else LocalizedParagraph(
            en = "Challenged Venus indicates relationship growth work and developing self-love. " +
                "Artistic expression can help heal Venus-related challenges.",
            ne = "चुनौतीपूर्ण शुक्रले सम्बन्ध वृद्धि कार्य र आत्म-प्रेम विकास संकेत गर्छ।"
        )
        
        Planet.SATURN -> if (isStrong) LocalizedParagraph(
            en = "Strong Saturn contributes discipline, structure, and long-term thinking. " +
                "You have the patience to build lasting achievements.",
            ne = "बलियो शनिले अनुशासन, संरचना र दीर्घकालीन सोच योगदान गर्छ।"
        ) else LocalizedParagraph(
            en = "Challenged Saturn indicates lessons in responsibility and overcoming limitations. " +
                "Patience and persistence help work through Saturn's tests.",
            ne = "चुनौतीपूर्ण शनिले जिम्मेवारी र सीमितताहरू पार गर्ने पाठहरू संकेत गर्छ।"
        )
        
        Planet.RAHU -> LocalizedParagraph(
            en = "Rahu's influence brings unconventional thinking and worldly ambitions. " +
                "You seek to transcend ordinary limitations and explore new territories.",
            ne = "राहुको प्रभावले अपरम्परागत सोच र सांसारिक महत्वाकांक्षाहरू ल्याउँछ।"
        )
        
        Planet.KETU -> LocalizedParagraph(
            en = "Ketu's influence brings spiritual insight and past-life wisdom. " +
                "You have natural detachment in certain areas and deep intuition.",
            ne = "केतुको प्रभावले आध्यात्मिक अन्तर्दृष्टि र पूर्व-जीवन ज्ञान ल्याउँछ।"
        )
    }
    
    fun getTraits(planet: Planet, isStrong: Boolean): List<LocalizedTrait> = when (planet) {
        Planet.SUN -> if (isStrong) listOf(
            LocalizedTrait("Leadership presence", "नेतृत्व उपस्थिति", StrengthLevel.STRONG),
            LocalizedTrait("Natural confidence", "प्राकृतिक आत्मविश्वास", StrengthLevel.STRONG),
            LocalizedTrait("Strong vitality", "बलियो जीवनी शक्ति", StrengthLevel.STRONG)
        ) else listOf(
            LocalizedTrait("Developing confidence", "विकासशील आत्मविश्वास", StrengthLevel.MODERATE),
            LocalizedTrait("Authority learning", "अधिकार सिक्ने", StrengthLevel.MODERATE)
        )
        
        Planet.MOON -> if (isStrong) listOf(
            LocalizedTrait("Emotional stability", "भावनात्मक स्थिरता", StrengthLevel.STRONG),
            LocalizedTrait("Strong intuition", "बलियो अन्तर्ज्ञान", StrengthLevel.STRONG),
            LocalizedTrait("Nurturing nature", "पालनपोषण स्वभाव", StrengthLevel.STRONG)
        ) else listOf(
            LocalizedTrait("Emotional sensitivity", "भावनात्मक संवेदनशीलता", StrengthLevel.MODERATE),
            LocalizedTrait("Growing stability", "बढ्दो स्थिरता", StrengthLevel.MODERATE)
        )
        
        Planet.MARS -> if (isStrong) listOf(
            LocalizedTrait("Physical courage", "शारीरिक साहस", StrengthLevel.STRONG),
            LocalizedTrait("Initiative drive", "पहल अभियान", StrengthLevel.STRONG),
            LocalizedTrait("Competitive spirit", "प्रतिस्पर्धी भावना", StrengthLevel.STRONG)
        ) else listOf(
            LocalizedTrait("Growing assertiveness", "बढ्दो दृढता", StrengthLevel.MODERATE),
            LocalizedTrait("Energy management", "ऊर्जा व्यवस्थापन", StrengthLevel.MODERATE)
        )
        
        Planet.MERCURY -> if (isStrong) listOf(
            LocalizedTrait("Quick intellect", "द्रुत बुद्धि", StrengthLevel.STRONG),
            LocalizedTrait("Communication skill", "सञ्चार कौशल", StrengthLevel.STRONG),
            LocalizedTrait("Analytical ability", "विश्लेषणात्मक क्षमता", StrengthLevel.STRONG)
        ) else listOf(
            LocalizedTrait("Developing clarity", "विकासशील स्पष्टता", StrengthLevel.MODERATE),
            LocalizedTrait("Learning growth", "सिक्ने वृद्धि", StrengthLevel.MODERATE)
        )
        
        Planet.JUPITER -> if (isStrong) listOf(
            LocalizedTrait("Natural wisdom", "प्राकृतिक ज्ञान", StrengthLevel.STRONG),
            LocalizedTrait("Optimistic outlook", "आशावादी दृष्टिकोण", StrengthLevel.STRONG),
            LocalizedTrait("Good fortune", "सौभाग्य", StrengthLevel.STRONG)
        ) else listOf(
            LocalizedTrait("Growing faith", "बढ्दो विश्वास", StrengthLevel.MODERATE),
            LocalizedTrait("Wisdom development", "ज्ञान विकास", StrengthLevel.MODERATE)
        )
        
        Planet.VENUS -> if (isStrong) listOf(
            LocalizedTrait("Natural charm", "प्राकृतिक आकर्षण", StrengthLevel.STRONG),
            LocalizedTrait("Artistic sense", "कलात्मक बोध", StrengthLevel.STRONG),
            LocalizedTrait("Relationship harmony", "सम्बन्ध सामंजस्य", StrengthLevel.STRONG)
        ) else listOf(
            LocalizedTrait("Developing charm", "विकासशील आकर्षण", StrengthLevel.MODERATE),
            LocalizedTrait("Love learning", "प्रेम सिक्ने", StrengthLevel.MODERATE)
        )
        
        Planet.SATURN -> if (isStrong) listOf(
            LocalizedTrait("Strong discipline", "बलियो अनुशासन", StrengthLevel.STRONG),
            LocalizedTrait("Patient persistence", "धैर्यवान दृढता", StrengthLevel.STRONG),
            LocalizedTrait("Practical wisdom", "व्यावहारिक ज्ञान", StrengthLevel.STRONG)
        ) else listOf(
            LocalizedTrait("Growing patience", "बढ्दो धैर्य", StrengthLevel.MODERATE),
            LocalizedTrait("Structure learning", "संरचना सिक्ने", StrengthLevel.MODERATE)
        )
        
        else -> listOf(LocalizedTrait("Unique influence", "अद्वितीय प्रभाव", StrengthLevel.MODERATE))
    }
}

/**
 * Retrograde Text Generator - Retrograde planet personality effects
 */
object RetrogradeTextGenerator {
    
    fun getInternalProcessing(planet: Planet): LocalizedParagraph = when (planet) {
        Planet.MERCURY -> LocalizedParagraph(
            en = "Retrograde Mercury gives you a unique, introspective thinking style. " +
                "You process information internally before expressing, often finding unconventional solutions.",
            ne = "वक्री बुधले तपाईंलाई अद्वितीय, आत्मनिरीक्षण सोच शैली दिन्छ।"
        )
        Planet.VENUS -> LocalizedParagraph(
            en = "Retrograde Venus creates internalized love expression and unconventional relationship values. " +
                "You may revisit past relationships or have unique aesthetic preferences.",
            ne = "वक्री शुक्रले आन्तरिक प्रेम अभिव्यक्ति र अपरम्परागत सम्बन्ध मूल्यहरू सिर्जना गर्छ।"
        )
        Planet.MARS -> LocalizedParagraph(
            en = "Retrograde Mars channels energy inward, creating strategic rather than impulsive action. " +
                "You may suppress anger initially but can have powerful delayed responses.",
            ne = "वक्री मंगलले ऊर्जालाई भित्र तिर चैनल गर्छ, आवेगी कार्यभन्दा रणनीतिक सिर्जना गर्दै।"
        )
        Planet.JUPITER -> LocalizedParagraph(
            en = "Retrograde Jupiter creates internalized wisdom and personal philosophy development. " +
                "You may question traditional beliefs and develop your own spiritual understanding.",
            ne = "वक्री बृहस्पतिले आन्तरिक ज्ञान र व्यक्तिगत दर्शन विकास सिर्जना गर्छ।"
        )
        Planet.SATURN -> LocalizedParagraph(
            en = "Retrograde Saturn creates internalized discipline and personal authority development. " +
                "You may struggle with external structures but build strong inner foundations.",
            ne = "वक्री शनिले आन्तरिक अनुशासन र व्यक्तिगत अधिकार विकास सिर्जना गर्छ।"
        )
        else -> LocalizedParagraph(
            en = "Retrograde ${planet.displayName} creates internalized expression of this planet's energy.",
            ne = "वक्री ${planet.displayName}ले यो ग्रहको ऊर्जाको आन्तरिक अभिव्यक्ति सिर्जना गर्छ।"
        )
    }
    
    fun getPastLifeKarma(planet: Planet): LocalizedParagraph = when (planet) {
        Planet.MERCURY -> LocalizedParagraph(
            en = "Past life unfinished communication or learning karmas may resurface for completion.",
            ne = "पूर्व जीवनको अधूरो सञ्चार वा सिक्ने कर्महरू पूर्णताको लागि पुन: देखा पर्न सक्छ।"
        )
        Planet.VENUS -> LocalizedParagraph(
            en = "Past life relationship patterns or artistic expressions seek resolution in this life.",
            ne = "पूर्व जीवनका सम्बन्ध ढाँचाहरू वा कलात्मक अभिव्यक्तिहरूले यस जीवनमा समाधान खोज्छन्।"
        )
        Planet.SATURN -> LocalizedParagraph(
            en = "Past life authority issues or unfinished responsibilities continue their lessons.",
            ne = "पूर्व जीवनका अधिकार समस्याहरू वा अधूरा जिम्मेवारीहरूले तिनीहरूका पाठहरू जारी राख्छन्।"
        )
        else -> LocalizedParagraph(
            en = "Past life ${planet.displayName} karma influences your current life expression.",
            ne = "पूर्व जीवनको ${planet.displayName} कर्माले तपाईंको वर्तमान जीवन अभिव्यक्तिलाई प्रभाव पार्छ।"
        )
    }
    
    fun getUnusualApproach(planet: Planet): LocalizedParagraph = when (planet) {
        Planet.MERCURY -> LocalizedParagraph(
            en = "You have an unconventional way of thinking and communicating that may seem unusual to others " +
                "but often leads to innovative insights.",
            ne = "तपाईंसँग सोच्ने र सञ्चार गर्ने अपरम्परागत तरिका छ जुन अरूलाई असामान्य लाग्न सक्छ।"
        )
        Planet.VENUS -> LocalizedParagraph(
            en = "Your approach to love, beauty, and relationships follows its own unique rhythm " +
                "that may differ from conventional expectations.",
            ne = "प्रेम, सौन्दर्य र सम्बन्धहरूमा तपाईंको दृष्टिकोण आफ्नै अद्वितीय लयलाई पछ्याउँछ।"
        )
        else -> LocalizedParagraph(
            en = "Your ${planet.displayName} expression follows an unconventional, internalized path.",
            ne = "तपाईंको ${planet.displayName} अभिव्यक्ति अपरम्परागत, आन्तरिक मार्ग पछ्याउँछ।"
        )
    }
}

/**
 * Yoga Personality Generator - Yoga effects on personality
 */
object YogaPersonalityGenerator {
    
    fun getPersonalityEffect(yogaName: String): LocalizedParagraph = when {
        yogaName.contains("Gajakesari", ignoreCase = true) -> LocalizedParagraph(
            en = "Gajakesari Yoga enhances your wisdom, reputation, and natural authority. " +
                "You command respect and have a noble bearing that inspires others.",
            ne = "गजकेसरी योगले तपाईंको ज्ञान, प्रतिष्ठा र प्राकृतिक अधिकार बढाउँछ।"
        )
        yogaName.contains("Budhaditya", ignoreCase = true) -> LocalizedParagraph(
            en = "Budhaditya Yoga sharpens your intellect and communication abilities. " +
                "You have a bright mind and express yourself with clarity and impact.",
            ne = "बुधादित्य योगले तपाईंको बुद्धि र सञ्चार क्षमताहरू तीक्ष्ण बनाउँछ।"
        )
        yogaName.contains("Hamsa", ignoreCase = true) -> LocalizedParagraph(
            en = "Hamsa Yoga blesses you with wisdom, spirituality, and refined character. " +
                "You have natural grace and philosophical depth.",
            ne = "हंस योगले तपाईंलाई ज्ञान, आध्यात्मिकता र परिष्कृत चरित्रको आशीर्वाद दिन्छ।"
        )
        yogaName.contains("Malavya", ignoreCase = true) -> LocalizedParagraph(
            en = "Malavya Yoga enhances your charm, creativity, and enjoyment of life's pleasures. " +
                "You have natural artistic talents and attractive presence.",
            ne = "मालव्य योगले तपाईंको आकर्षण, रचनात्मकता र जीवनको आनन्द बढाउँछ।"
        )
        yogaName.contains("Ruchaka", ignoreCase = true) -> LocalizedParagraph(
            en = "Ruchaka Yoga gives you courage, physical strength, and leadership ability. " +
                "You have warrior-like qualities and command respect.",
            ne = "रुचक योगले तपाईंलाई साहस, शारीरिक शक्ति र नेतृत्व क्षमता दिन्छ।"
        )
        yogaName.contains("Bhadra", ignoreCase = true) -> LocalizedParagraph(
            en = "Bhadra Yoga enhances your intellect, communication, and business acumen. " +
                "You have sharp analytical abilities and persuasive speech.",
            ne = "भद्र योगले तपाईंको बुद्धि, सञ्चार र व्यापारिक कुशाग्रता बढाउँछ।"
        )
        yogaName.contains("Sasa", ignoreCase = true) -> LocalizedParagraph(
            en = "Sasa Yoga gives you discipline, authority, and administrative abilities. " +
                "You have natural management skills and can lead organizations.",
            ne = "शश योगले तपाईंलाई अनुशासन, अधिकार र प्रशासनिक क्षमताहरू दिन्छ।"
        )
        yogaName.contains("Saraswati", ignoreCase = true) -> LocalizedParagraph(
            en = "Saraswati Yoga blesses you with learning ability, artistic talents, and eloquence. " +
                "You excel in education, arts, and creative expression.",
            ne = "सरस्वती योगले तपाईंलाई सिक्ने क्षमता, कलात्मक प्रतिभा र वाग्मिताको आशीर्वाद दिन्छ।"
        )
        else -> LocalizedParagraph(
            en = "$yogaName influences your personality in unique ways based on its formation.",
            ne = "$yogaName ले तपाईंको व्यक्तित्वलाई यसको गठनको आधारमा अद्वितीय तरिकाले प्रभाव पार्छ।"
        )
    }
    
    fun getManifestationStyle(yogaName: String): LocalizedParagraph = when {
        yogaName.contains("Gajakesari", ignoreCase = true) -> LocalizedParagraph(
            en = "This yoga manifests gradually, with its effects becoming stronger as you mature.",
            ne = "यो योग बिस्तारै प्रकट हुन्छ, तपाईं परिपक्व हुँदा यसको प्रभाव बढी बलियो हुन्छ।"
        )
        yogaName.contains("Pancha Mahapurusha", ignoreCase = true) || 
        yogaName.contains("Hamsa", ignoreCase = true) ||
        yogaName.contains("Malavya", ignoreCase = true) ||
        yogaName.contains("Ruchaka", ignoreCase = true) ||
        yogaName.contains("Bhadra", ignoreCase = true) ||
        yogaName.contains("Sasa", ignoreCase = true) -> LocalizedParagraph(
            en = "As a Pancha Mahapurusha Yoga, this manifests prominently from youth, making you stand out.",
            ne = "पञ्च महापुरुष योगको रूपमा, यो युवावस्थादेखि प्रमुख रूपमा प्रकट हुन्छ।"
        )
        else -> LocalizedParagraph(
            en = "This yoga manifests through consistent effort aligned with its energies.",
            ne = "यो योग यसको ऊर्जाहरूसँग मिलाएर निरन्तर प्रयास मार्फत प्रकट हुन्छ।"
        )
    }
}
