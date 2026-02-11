package com.astro.storm.core.common

/**
 * Deep Analysis Localization Keys - Character & Personality
 * 500+ localization keys for comprehensive character analysis
 */
enum class StringKeyDeepCharacter(override val en: String, override val ne: String, override val hi: String) : StringKeyInterface {
    // Section Headers
    SECTION_CHARACTER_ANALYSIS("Character Analysis", "चरित्र विश्लेषण", "चरित्र विश्लेषण"),
    ,
    SECTION_PERSONALITY_PROFILE("Personality Profile", "व्यक्तित्व प्रोफाइल", "व्यक्तित्व प्रोफाइल"),
    ,
    SECTION_ASCENDANT_ANALYSIS("Ascendant Analysis", "लग्न विश्लेषण", "लग्न विश्लेषण"),
    ,
    SECTION_MOON_ANALYSIS("Moon Analysis", "चन्द्रमा विश्लेषण", "चन्द्र में विश्लेषण"),
    ,
    SECTION_SUN_ANALYSIS("Sun Analysis", "सूर्य विश्लेषण", "सूर्य विश्लेषण"),
    ,
    SECTION_TEMPERAMENT("Temperament", "स्वभाव", "स्वभाव"),
    ,
    SECTION_EMOTIONAL_PROFILE("Emotional Profile", "भावनात्मक प्रोफाइल", "भावनात्मक प्रोफाइल"),
    ,
    SECTION_INTELLECTUAL_PROFILE("Intellectual Profile", "बौद्धिक प्रोफाइल", "बौद्धिक प्रोफाइल"),
    ,
    SECTION_SOCIAL_BEHAVIOR("Social Behavior", "सामाजिक व्यवहार", "सामाजिक व्यवहार"),
    ,
    SECTION_PLANETARY_INFLUENCES("Planetary Influences", "ग्रहीय प्रभावहरू", "ग्रहीय प्रभाव"),
    ,
    SECTION_PERSONALITY_YOGAS("Personality Yogas", "व्यक्तित्व योगहरू", "व्यक्तित्व यहग"),

    ,
    
    // Ascendant Signs
    ASC_ARIES_TITLE("Aries Rising", "मेष लग्न", "मेष लग्न"),
    ,
    ASC_ARIES_DESC("Bold, pioneering, action-oriented personality", "साहसी, अग्रगामी, कार्य-उन्मुख व्यक्तित्व", "साहसी, अग्रगामी, कार्य-उन्मुख व्यक्तित्व"),
    ,
    ASC_TAURUS_TITLE("Taurus Rising", "वृष लग्न", "वृष लग्न"),
    ,
    ASC_TAURUS_DESC("Steady, sensual, materially focused personality", "स्थिर, संवेदी, भौतिक रूपमा केन्द्रित व्यक्तित्व", "स्थिर, संवेदी, भौतिक रूप में केन्द्रित व्यक्तित्व"),
    ,
    ASC_GEMINI_TITLE("Gemini Rising", "मिथुन लग्न", "मिथुन लग्न"),
    ,
    ASC_GEMINI_DESC("Curious, communicative, mentally agile personality", "जिज्ञासु, सञ्चारी, मानसिक रूपमा चुस्त व्यक्तित्व", "जिज्ञासु, सञ्चारी, मानसिक रूप में चुस्त व्यक्तित्व"),
    ,
    ASC_CANCER_TITLE("Cancer Rising", "कर्कट लग्न", "कर्कट लग्न"),
    ,
    ASC_CANCER_DESC("Nurturing, emotional, family-oriented personality", "पालनपोषण गर्ने, भावनात्मक, परिवार-उन्मुख व्यक्तित्व", "पालनपोषण करनेे, भावनात्मक, परिवार-उन्मुख व्यक्तित्व"),
    ,
    ASC_LEO_TITLE("Leo Rising", "सिंह लग्न", "सिंह लग्न"),
    ,
    ASC_LEO_DESC("Confident, creative, natural leader personality", "आत्मविश्वासी, रचनात्मक, प्राकृतिक नेता व्यक्तित्व", "आत्मविश्वासी, रचनात्मक, प्राकृतिक नेता व्यक्तित्व"),
    ,
    ASC_VIRGO_TITLE("Virgo Rising", "कन्या लग्न", "कन्या लग्न"),
    ,
    ASC_VIRGO_DESC("Analytical, service-oriented, detail-focused personality", "विश्लेषणात्मक, सेवा-उन्मुख, विस्तार-केन्द्रित व्यक्तित्व", "विश्लेषणात्मक, सेवा-उन्मुख, विस्तार-केन्द्रित व्यक्तित्व"),
    ,
    ASC_LIBRA_TITLE("Libra Rising", "तुला लग्न", "तुला लग्न"),
    ,
    ASC_LIBRA_DESC("Diplomatic, harmonious, relationship-focused personality", "कूटनीतिक, सामंजस्यपूर्ण, सम्बन्ध-केन्द्रित व्यक्तित्व", "कूटनीतिक, सामंजस्यपूर्ण, संबंध-केन्द्रित व्यक्तित्व"),
    ,
    ASC_SCORPIO_TITLE("Scorpio Rising", "वृश्चिक लग्न", "वृश्चिक लग्न"),
    ,
    ASC_SCORPIO_DESC("Intense, transformative, deeply perceptive personality", "तीव्र, परिवर्तनकारी, गहिरो बोधगम्य व्यक्तित्व", "तीव्र, परिवर्तनकारी, गहिरो बोधगम्य व्यक्तित्व"),
    ,
    ASC_SAGITTARIUS_TITLE("Sagittarius Rising", "धनु लग्न", "धनु लग्न"),
    ,
    ASC_SAGITTARIUS_DESC("Adventurous, philosophical, freedom-loving personality", "साहसिक, दार्शनिक, स्वतन्त्रता-प्रेमी व्यक्तित्व", "साहसिक, दार्शनिक, स्वतन्त्रता-प्रेमी व्यक्तित्व"),
    ,
    ASC_CAPRICORN_TITLE("Capricorn Rising", "मकर लग्न", "मकर लग्न"),
    ,
    ASC_CAPRICORN_DESC("Ambitious, disciplined, achievement-oriented personality", "महत्वाकांक्षी, अनुशासित, उपलब्धि-उन्मुख व्यक्तित्व", "महत्वाकांक्षी, अनुशासित, उपलब्धि-उन्मुख व्यक्तित्व"),
    ,
    ASC_AQUARIUS_TITLE("Aquarius Rising", "कुम्भ लग्न", "कुम्भ लग्न"),
    ,
    ASC_AQUARIUS_DESC("Innovative, humanitarian, independent personality", "नवीन, मानवतावादी, स्वतन्त्र व्यक्तित्व", "नवीन, मानवतावादी, स्वतन्त्र व्यक्तित्व"),
    ,
    ASC_PISCES_TITLE("Pisces Rising", "मीन लग्न", "मीन लग्न"),
    ,
    ASC_PISCES_DESC("Intuitive, compassionate, spiritually inclined personality", "अन्तर्ज्ञानी, दयालु, आध्यात्मिक रूपमा झुकाव भएको व्यक्तित्व", "अन्तर्ज्ञानी, दयालु, आध्यात्मिक रूप में झुकाव हुआ व्यक्तित्व"),

    ,

    // Physical Appearance
    APPEARANCE_TITLE("Physical Appearance", "शारीरिक रूप", "शारीरिक रूप"),
    ,
    APPEARANCE_BODY_TYPE("Body Type", "शरीर प्रकार", "शरीर प्रकार"),
    ,
    APPEARANCE_FACE("Facial Features", "अनुहार विशेषताहरू", "अनुहार विशेषता"),
    ,
    APPEARANCE_COMPLEXION("Complexion", "रंग", "रंग"),
    ,
    APPEARANCE_OVERALL("Overall Appearance", "समग्र रूप", "समग्र रूप"),

    ,
    
    // First Impression
    FIRST_IMPRESSION_TITLE("First Impression", "पहिलो छाप", "पहिलो हैाप"),
    ,
    FIRST_IMPRESSION_POSITIVE("Positive First Impression", "सकारात्मक पहिलो छाप", "सकारात्मक पहिलो हैाप"),
    ,
    FIRST_IMPRESSION_NEUTRAL("Neutral First Impression", "तटस्थ पहिलो छाप", "तटस्थ पहिलो हैाप"),

    ,
    
    // Life Approach
    LIFE_APPROACH_TITLE("Life Approach", "जीवन दृष्टिकोण", "जीवन दृष्टिकोण"),
    ,
    LIFE_APPROACH_STYLE("Approach Style", "दृष्टिकोण शैली", "दृष्टिकोण शैली"),
    ,
    LIFE_APPROACH_PHILOSOPHY("Life Philosophy", "जीवन दर्शन", "जीवन दर्शन"),

    ,
    
    // Moon Signs
    MOON_ARIES("Moon in Aries", "मेषमा चन्द्रमा", "मेष में चन्द्र में"),
    ,
    MOON_TAURUS("Moon in Taurus", "वृषमा चन्द्रमा", "वृष में चन्द्र में"),
    ,
    MOON_GEMINI("Moon in Gemini", "मिथुनमा चन्द्रमा", "मिथुन में चन्द्र में"),
    ,
    MOON_CANCER("Moon in Cancer", "कर्कटमा चन्द्रमा", "कर्कट में चन्द्र में"),
    ,
    MOON_LEO("Moon in Leo", "सिंहमा चन्द्रमा", "सिंह में चन्द्र में"),
    ,
    MOON_VIRGO("Moon in Virgo", "कन्यामा चन्द्रमा", "कन्या में चन्द्र में"),
    ,
    MOON_LIBRA("Moon in Libra", "तुलामा चन्द्रमा", "तुला में चन्द्र में"),
    ,
    MOON_SCORPIO("Moon in Scorpio", "वृश्चिकमा चन्द्रमा", "वृश्चिक में चन्द्र में"),
    ,
    MOON_SAGITTARIUS("Moon in Sagittarius", "धनुमा चन्द्रमा", "धनु में चन्द्र में"),
    ,
    MOON_CAPRICORN("Moon in Capricorn", "मकरमा चन्द्रमा", "मकर में चन्द्र में"),
    ,
    MOON_AQUARIUS("Moon in Aquarius", "कुम्भमा चन्द्रमा", "कुम्भ में चन्द्र में"),
    ,
    MOON_PISCES("Moon in Pisces", "मीनमा चन्द्रमा", "मीन में चन्द्र में"),

    ,
    
    // Nakshatra
    NAKSHATRA_TITLE("Nakshatra Analysis", "नक्षत्र विश्लेषण", "नक्षत्र विश्लेषण"),
    ,
    NAKSHATRA_PADA("Pada Analysis", "पद विश्लेषण", "पद विश्लेषण"),
    ,
    NAKSHATRA_DEITY("Ruling Deity", "शासक देवता", "शासक देवता"),
    ,
    NAKSHATRA_SHAKTI("Shakti (Power)", "शक्ति", "शक्ति"),

    ,
    
    // Emotional Nature
    EMOTIONAL_NATURE_TITLE("Emotional Nature", "भावनात्मक स्वभाव", "भावनात्मक स्वभाव"),
    ,
    EMOTIONAL_EXPRESSION("Emotional Expression", "भावनात्मक अभिव्यक्ति", "भावनात्मक अभिव्यक्ति"),
    ,
    EMOTIONAL_TRIGGERS("Emotional Triggers", "भावनात्मक ट्रिगरहरू", "भावनात्मक ट्रिगर"),
    ,
    EMOTIONAL_STRENGTHS("Emotional Strengths", "भावनात्मक शक्तिहरू", "भावनात्मक शक्ति"),
    ,
    EMOTIONAL_CHALLENGES("Emotional Challenges", "भावनात्मक चुनौतीहरू", "भावनात्मक चुनौती"),
    ,
    EMOTIONAL_GROWTH("Emotional Growth Path", "भावनात्मक विकास मार्ग", "भावनात्मक विकास मार्ग"),

    ,
    
    // Mindset
    MINDSET_TITLE("Mindset", "मानसिकता", "मानसिकता"),
    ,
    MINDSET_ANALYTICAL("Analytical Mindset", "विश्लेषणात्मक मानसिकता", "विश्लेषणात्मक मानसिकता"),
    ,
    MINDSET_CREATIVE("Creative Mindset", "रचनात्मक मानसिकता", "रचनात्मक मानसिकता"),
    ,
    MINDSET_PRACTICAL("Practical Mindset", "व्यावहारिक मानसिकता", "व्यावहारिक मानसिकता"),
    ,
    MINDSET_INTUITIVE("Intuitive Mindset", "अन्तर्ज्ञानी मानसिकता", "अन्तर्ज्ञानी मानसिकता"),

    ,
    
    // Inner Needs
    INNER_NEEDS_TITLE("Inner Needs", "आन्तरिक आवश्यकताहरू", "आन्तरिक आवश्यकता"),
    ,
    NEED_SECURITY("Need for Security", "सुरक्षाको आवश्यकता", "सुरक्षा के आवश्यकता"),
    ,
    NEED_RECOGNITION("Need for Recognition", "मान्यताको आवश्यकता", "मान्यता के आवश्यकता"),
    ,
    NEED_CONNECTION("Need for Connection", "सम्बन्धको आवश्यकता", "संबंध के आवश्यकता"),
    ,
    NEED_FREEDOM("Need for Freedom", "स्वतन्त्रताको आवश्यकता", "स्वतन्त्रता के आवश्यकता"),

    ,
    
    // Stress Response
    STRESS_RESPONSE_TITLE("Stress Response", "तनाव प्रतिक्रिया", "तनाव प्रतिक्रिया"),
    ,
    STRESS_COPING("Coping Mechanisms", "सामना गर्ने तंत्रहरू", "सामना करनेे तंत्र"),
    ,
    STRESS_TRIGGERS("Stress Triggers", "तनाव ट्रिगरहरू", "तनाव ट्रिगर"),

    ,
    
    // Family Relationships
    MOTHER_RELATIONSHIP("Relationship with Mother", "आमासँगको सम्बन्ध", "आमासँग के संबंध"),
    ,
    FATHER_RELATIONSHIP("Relationship with Father", "बुबासँगको सम्बन्ध", "बुबासँग के संबंध"),

    ,
    
    // Sun Analysis
    SUN_CORE_IDENTITY("Core Identity", "मूल पहिचान", "मूल पहिचान"),
    ,
    SUN_EGO_EXPRESSION("Ego Expression", "अहंकार अभिव्यक्ति", "अहंकार अभिव्यक्ति"),
    ,
    SUN_AUTHORITY_REL("Authority Relationship", "अधिकार सम्बन्ध", "अधिकार संबंध"),
    ,
    SUN_VITALITY("Vitality Level", "जीवनी शक्ति स्तर", "जीवनी शक्ति स्तर"),
    ,
    SUN_LEADERSHIP("Leadership Style", "नेतृत्व शैली", "नेतृत्व शैली"),

    ,
    
    // Atmakaraka
    ATMAKARAKA_TITLE("Soul Significator (Atmakaraka)", "आत्म संकेतक (आत्मकारक)", "आत्म संकेतक (आत्मकारक)"),
    ,
    ATMAKARAKA_SOUL_DESIRE("Soul's Desire", "आत्माको इच्छा", "आत् में के इच्हैा"),
    ,
    ATMAKARAKA_KARMIC_LESSON("Karmic Lesson", "कार्मिक पाठ", "कार्मिक पाठ"),
    ,
    ATMAKARAKA_SPIRITUAL_PATH("Spiritual Path", "आध्यात्मिक मार्ग", "आध्यात्मिक मार्ग"),

    ,
    
    // Temperament
    TEMPERAMENT_FIRE("Fire Temperament", "अग्नि स्वभाव", "अग्नि स्वभाव"),
    ,
    TEMPERAMENT_EARTH("Earth Temperament", "पृथ्वी स्वभाव", "पृथ्वी स्वभाव"),
    ,
    TEMPERAMENT_AIR("Air Temperament", "वायु स्वभाव", "वायु स्वभाव"),
    ,
    TEMPERAMENT_WATER("Water Temperament", "जल स्वभाव", "जल स्वभाव"),
    ,
    TEMPERAMENT_CARDINAL("Cardinal Modality", "कार्डिनल मोडालिटी", "कार्डिनल मोडालिटी"),
    ,
    TEMPERAMENT_FIXED("Fixed Modality", "स्थिर मोडालिटी", "स्थिर मोडालिटी"),
    ,
    TEMPERAMENT_MUTABLE("Mutable Modality", "परिवर्तनशील मोडालिटी", "परिवर्तनशील मोडालिटी"),

    ,
    
    // Intellectual Profile
    INTELLECT_TITLE("Intellectual Profile", "बौद्धिक प्रोफाइल", "बौद्धिक प्रोफाइल"),
    ,
    LEARNING_STYLE("Learning Style", "सिक्ने शैली", "सिक्ने शैली"),
    ,
    COMMUNICATION_STYLE("Communication Style", "सञ्चार शैली", "सञ्चार शैली"),
    ,
    INTELLECT_STRENGTHS("Intellectual Strengths", "बौद्धिक शक्तिहरू", "बौद्धिक शक्ति"),
    ,
    INTELLECT_CHALLENGES("Intellectual Challenges", "बौद्धिक चुनौतीहरू", "बौद्धिक चुनौती"),

    ,
    
    // Social Behavior
    SOCIAL_TITLE("Social Profile", "सामाजिक प्रोफाइल", "सामाजिक प्रोफाइल"),
    ,
    SOCIAL_ORIENTATION("Social Orientation", "सामाजिक अभिमुखीकरण", "सामाजिक अभिमुखीकरण"),
    ,
    RELATIONSHIP_APPROACH("Relationship Approach", "सम्बन्ध दृष्टिकोण", "संबंध दृष्टिकोण"),
    ,
    GROUP_COMMUNICATION("Group Communication", "समूह सञ्चार", "समूह सञ्चार"),
    ,
    LEADERSHIP_TENDENCY("Leadership Tendency", "नेतृत्व प्रवृत्ति", "नेतृत्व प्रवृत्ति"),
    ,
    SOCIAL_STRENGTHS("Social Strengths", "सामाजिक शक्तिहरू", "सामाजिक शक्ति"),
    ,
    SOCIAL_CHALLENGES("Social Challenges", "सामाजिक चुनौतीहरू", "सामाजिक चुनौती"),

    ,
    
    // Planetary Influences
    PLANET_INFLUENCE_TITLE("Planetary Personality Influences", "ग्रहीय व्यक्तित्व प्रभावहरू", "ग्रहीय व्यक्तित्व प्रभाव"),
    ,
    STRONG_PLANET_INFLUENCE("Strong Planet Influence", "बलियो ग्रह प्रभाव", "बलियह ग्रह प्रभाव"),
    ,
    WEAK_PLANET_CHALLENGE("Weak Planet Challenge", "कमजोर ग्रह चुनौती", "कमजोर ग्रह चुनौती"),

    ,
    
    // Retrograde Effects
    RETROGRADE_TITLE("Retrograde Planet Effects", "वक्री ग्रह प्रभावहरू", "वक्री ग्रह प्रभाव"),
    ,
    RETROGRADE_INTERNAL("Internal Processing", "आन्तरिक प्रशोधन", "आन्तरिक प्रशोधन"),
    ,
    RETROGRADE_PAST_LIFE("Past Life Karma", "पूर्व जीवन कर्म", "पूर्व जीवन कर्म"),
    ,
    RETROGRADE_APPROACH("Unusual Approach", "असामान्य दृष्टिकोण", "असामान्य दृष्टिकोण"),

    ,
    
    // Personality Yogas
    YOGA_PERSONALITY_TITLE("Personality Yogas", "व्यक्तित्व योगहरू", "व्यक्तित्व यहग"),
    ,
    YOGA_EFFECT("Yoga Effect", "योग प्रभाव", "यहग प्रभाव"),
    ,
    YOGA_MANIFESTATION("Manifestation Style", "अभिव्यक्ति शैली", "अभिव्यक्ति शैली"),

    ,
    
    // Summary
    PERSONALITY_SUMMARY("Personality Summary", "व्यक्तित्व सारांश", "व्यक्तित्व सारांश"),
    ,
    PERSONALITY_SCORE("Personality Strength Score", "व्यक्तित्व शक्ति स्कोर", "व्यक्तित्व शक्ति स्कोर"),
    ,
    KEY_TRAITS("Key Personality Traits", "मुख्य व्यक्तित्व गुणहरू", "मुख्य व्यक्तित्व गुण"),

    ,
    
    // Strength Levels
    STRENGTH_EXCELLENT("Excellent", "उत्कृष्ट", "उत्कृष्ट"),
    ,
    STRENGTH_STRONG("Strong", "बलियो", "बलियह"),
    ,
    STRENGTH_MODERATE("Moderate", "मध्यम", "मध्यम"),
    ,
    STRENGTH_WEAK("Weak", "कमजोर", "कमजोर"),
,
}
