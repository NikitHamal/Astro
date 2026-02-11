package com.astro.storm.core.common

/**
 * Deep Analysis Localization Keys - Health & Longevity
 * 350+ localization keys for comprehensive health analysis
 */
enum class StringKeyDeepHealth(override val en: String, override val ne: String, override val hi: String) : StringKeyInterface {
    // Section Headers
    SECTION_HEALTH("Health Analysis", "स्वास्थ्य विश्लेषण", "स्वास्थ्य विश्लेषण"),
    ,
    SECTION_CONSTITUTION("Constitution Analysis", "संविधान विश्लेषण", "संविधान विश्लेषण"),
    ,
    SECTION_6TH_HOUSE("6th House Analysis", "षष्ठ भाव विश्लेषण", "षष्ठ भाव विश्लेषण"),
    ,
    SECTION_8TH_HOUSE("8th House Analysis", "अष्टम भाव विश्लेषण", "अष्टम भाव विश्लेषण"),
    ,
    SECTION_LONGEVITY("Longevity Analysis", "दीर्घायु विश्लेषण", "दीर्घायु विश्लेषण"),
    ,
    SECTION_BODY_MAPPING("Body Part Mapping", "शरीर अंग म्यापिङ", "शरीर अंग म्यापिङ"),
    ,
    SECTION_PREVENTIVE("Preventive Focus", "रोकथाम फोकस", "रोकथाम फोकस"),

    ,
    
    // Constitution
    CONSTITUTION_TITLE("Ayurvedic Constitution", "आयुर्वेदिक संविधान", "आयुर्वेदिक संविधान"),
    ,
    PRIMARY_DOSHA("Primary Dosha", "प्राथमिक दोष", "प्राथमिक दोष"),
    ,
    SECONDARY_DOSHA("Secondary Dosha", "माध्यमिक दोष", "माध्यमिक दोष"),
    ,
    DOSHA_BALANCE("Dosha Balance", "दोष सन्तुलन", "दोष सन्तुलन"),
    ,
    DOSHA_VATA("Vata Dosha", "वात दोष", "वात दोष"),
    ,
    DOSHA_PITTA("Pitta Dosha", "पित्त दोष", "पित्त दोष"),
    ,
    DOSHA_KAPHA("Kapha Dosha", "कफ दोष", "कफ दोष"),
    ,
    CONSTITUTION_DESC("Constitution Description", "संविधान विवरण", "संविधान विवरण"),
    ,
    DIETARY_RECS("Dietary Recommendations", "आहार सिफारिसहरू", "आहार सिफारिस"),
    ,
    LIFESTYLE_RECS("Lifestyle Recommendations", "जीवनशैली सिफारिसहरू", "जीवनशैली सिफारिस"),
    ,
    EXERCISE_RECS("Exercise Recommendations", "व्यायाम सिफारिसहरू", "व्यायाम सिफारिस"),

    ,
    
    // 6th House
    SIXTH_HOUSE_SIGN("6th House Sign", "षष्ठ भाव राशि", "षष्ठ भाव राशि"),
    ,
    SIXTH_HOUSE_LORD("6th House Lord", "षष्ठ भाव स्वामी", "षष्ठ भाव स्वामी"),
    ,
    SIXTH_HOUSE_PLANETS("Planets in 6th House", "षष्ठ भावमा ग्रहहरू", "षष्ठ भाव में ग्रह"),
    ,
    DISEASE_RESISTANCE("Disease Resistance", "रोग प्रतिरोध", "रोग प्रतिरोध"),
    ,
    IMMUNE_SYSTEM("Immune System Profile", "प्रतिरक्षा प्रणाली प्रोफाइल", "प्रतिरक्षा प्रणाली प्रोफाइल"),
    ,
    POTENTIAL_AILMENTS("Potential Ailments", "सम्भावित रोगहरू", "सम्भावित रोग"),
    ,
    HEALING_CAPACITY("Healing Capacity", "उपचार क्षमता", "उपचार क्षमता"),

    ,
    
    // 8th House
    EIGHTH_HOUSE_SIGN("8th House Sign", "अष्टम भाव राशि", "अष्टम भाव राशि"),
    ,
    EIGHTH_HOUSE_LORD("8th House Lord", "अष्टम भाव स्वामी", "अष्टम भाव स्वामी"),
    ,
    EIGHTH_HOUSE_PLANETS("Planets in 8th House", "अष्टम भावमा ग्रहहरू", "अष्टम भाव में ग्रह"),
    ,
    CHRONIC_PATTERNS("Chronic Health Patterns", "दीर्घकालीन स्वास्थ्य ढाँचाहरू", "दीर्घकालीन स्वास्थ्य ढाँचा"),
    ,
    REGENERATIVE_CAPACITY("Regenerative Capacity", "पुनर्जनन क्षमता", "पुनर्जनन क्षमता"),
    ,
    CRITICAL_PERIODS("Critical Health Periods", "महत्वपूर्ण स्वास्थ्य अवधिहरू", "महत्वपूर्ण स्वास्थ्य अवधि"),

    ,
    
    // Ascendant Health
    ASCENDANT_HEALTH("Ascendant Health Profile", "लग्न स्वास्थ्य प्रोफाइल", "लग्न स्वास्थ्य प्रोफाइल"),
    ,
    GENERAL_VITALITY("General Vitality", "सामान्य जीवनी शक्ति", "सामान्य जीवनी शक्ति"),
    ,
    PHYSICAL_CONSTITUTION("Physical Constitution", "शारीरिक संविधान", "शारीरिक संविधान"),
    ,
    INNATE_PROWESS("Innate Physical Prowess", "जन्मजात शारीरिक शक्ति", "जन्मजात शारीरिक शक्ति"),
    ,
    BODY_FRAME("Body Frame", "शरीर फ्रेम", "शरीर फ्रेम"),
    ,
    METABOLIC_TYPE("Metabolic Type", "चयापचय प्रकार", "चयापचय प्रकार"),

    ,
    
    // Planetary Health Influences
    PLANET_HEALTH_TITLE("Planetary Health Influences", "ग्रहीय स्वास्थ्य प्रभावहरू", "ग्रहीय स्वास्थ्य प्रभाव"),
    ,
    SUN_HEALTH("Sun Health Influence", "सूर्य स्वास्थ्य प्रभाव", "सूर्य स्वास्थ्य प्रभाव"),
    ,
    MOON_HEALTH("Moon Health Influence", "चन्द्रमा स्वास्थ्य प्रभाव", "चन्द्र में स्वास्थ्य प्रभाव"),
    ,
    MARS_HEALTH("Mars Health Influence", "मंगल स्वास्थ्य प्रभाव", "मंगल स्वास्थ्य प्रभाव"),
    ,
    MERCURY_HEALTH("Mercury Health Influence", "बुध स्वास्थ्य प्रभाव", "बुध स्वास्थ्य प्रभाव"),
    ,
    JUPITER_HEALTH("Jupiter Health Influence", "बृहस्पति स्वास्थ्य प्रभाव", "बृहस्पति स्वास्थ्य प्रभाव"),
    ,
    VENUS_HEALTH("Venus Health Influence", "शुक्र स्वास्थ्य प्रभाव", "शुक्र स्वास्थ्य प्रभाव"),
    ,
    SATURN_HEALTH("Saturn Health Influence", "शनि स्वास्थ्य प्रभाव", "शनि स्वास्थ्य प्रभाव"),

    ,
    
    // Body Part Mapping
    BODY_HEAD("Head & Face", "टाउको र अनुहार", "टाउ के और अनुहार"),
    ,
    BODY_THROAT("Throat & Neck", "घाँटी र घाँटी", "घाँटी और घाँटी"),
    ,
    BODY_ARMS("Arms & Shoulders", "हातहरू र काँधहरू", "हात और काँध"),
    ,
    BODY_CHEST("Chest & Heart", "छाती र मुटु", "हैाती और मुटु"),
    ,
    BODY_STOMACH("Stomach & Digestion", "पेट र पाचन", "पेट और पाचन"),
    ,
    BODY_INTESTINES("Intestines", "आन्द्राहरू", "आन्द्रा"),
    ,
    BODY_LOWER_ABDOMEN("Lower Abdomen", "तल्लो पेट", "तल्लो पेट"),
    ,
    BODY_REPRODUCTIVE("Reproductive System", "प्रजनन प्रणाली", "प्रजनन प्रणाली"),
    ,
    BODY_THIGHS("Thighs", "थाईहरू", "थाई"),
    ,
    BODY_KNEES("Knees", "घुँडाहरू", "घुँडा"),
    ,
    BODY_ANKLES("Ankles & Calves", "गोलीगाँठाहरू र पिँडौलाहरू", "गोलीगाँठा और पिँडौला"),
    ,
    BODY_FEET("Feet", "खुट्टाहरू", "खुट्टा"),

    ,
    
    // Health Vulnerabilities
    VULNERABILITIES_TITLE("Health Vulnerabilities", "स्वास्थ्य कमजोरीहरू", "स्वास्थ्य कमजोरी"),
    ,
    VULNERABILITY_AREA("Vulnerable Area", "कमजोर क्षेत्र", "कमजोर क्षेत्र"),
    ,
    VULNERABILITY_SEVERITY("Severity Level", "गम्भीरता स्तर", "गम्भीरता स्तर"),
    ,
    PREVENTIVE_MEASURES("Preventive Measures", "रोकथाम उपायहरू", "रोकथाम उपाय"),

    ,
    
    // Health Strengths
    HEALTH_STRENGTHS("Health Strengths", "स्वास्थ्य शक्तिहरू", "स्वास्थ्य शक्ति"),
    ,
    ROBUST_AREAS("Robust Health Areas", "बलियो स्वास्थ्य क्षेत्रहरू", "बलियह स्वास्थ्य क्षेत्र"),

    ,
    
    // Preventive Focus
    PRIORITY_AREAS("Priority Focus Areas", "प्राथमिकता फोकस क्षेत्रहरू", "प्राथमिकता फोकस क्षेत्र"),
    ,
    SEASONAL_GUIDELINES("Seasonal Health Guidelines", "मौसमी स्वास्थ्य दिशानिर्देशहरू", "मौसमी स्वास्थ्य दिशानिर्देश"),
    ,
    YOGA_PRACTICES("Yoga Practices", "योग अभ्यासहरू", "यहग अभ्यास"),
    ,
    AYURVEDIC_RECS("Ayurvedic Recommendations", "आयुर्वेदिक सिफारिसहरू", "आयुर्वेदिक सिफारिस"),

    ,
    
    // Health Timeline
    HEALTH_TIMELINE("Health Timeline", "स्वास्थ्य समयरेखा", "स्वास्थ्य समयरेखा"),
    ,
    CURRENT_HEALTH_PHASE("Current Health Phase", "वर्तमान स्वास्थ्य चरण", "वर्तमान स्वास्थ्य चरण"),
    ,
    DASHA_HEALTH_FOCUS("Dasha Period Health Focus", "दशा अवधि स्वास्थ्य फोकस", "दशा अवधि स्वास्थ्य फोकस"),
    ,
    HEALTH_CONCERN_AREAS("Health Concern Areas", "स्वास्थ्य चिन्ता क्षेत्रहरू", "स्वास्थ्य चिन्ता क्षेत्र"),

    ,
    
    // Longevity
    LONGEVITY_TITLE("Longevity Profile", "दीर्घायु प्रोफाइल", "दीर्घायु प्रोफाइल"),
    ,
    LONGEVITY_CATEGORY("Longevity Category", "दीर्घायु श्रेणी", "दीर्घायु श्रेणी"),
    ,
    LONGEVITY_SHORT("Short Lifespan Indicators", "छोटो आयु सूचकहरू", "हैोटो आयु सूचक"),
    ,
    LONGEVITY_MEDIUM("Medium Lifespan Indicators", "मध्यम आयु सूचकहरू", "मध्यम आयु सूचक"),
    ,
    LONGEVITY_LONG("Long Lifespan Indicators", "लामो आयु सूचकहरू", "लामो आयु सूचक"),
    ,
    SUPPORTING_FACTORS("Supporting Longevity Factors", "दीर्घायु समर्थन कारकहरू", "दीर्घायु समर्थन कारक"),
    ,
    CHALLENGING_FACTORS("Challenging Factors", "चुनौतीपूर्ण कारकहरू", "चुनौतीपूर्ण कारक"),

    ,
    
    // Summary
    HEALTH_SUMMARY("Health Summary", "स्वास्थ्य सारांश", "स्वास्थ्य सारांश"),
    ,
    HEALTH_SCORE("Health Strength Score", "स्वास्थ्य शक्ति स्कोर", "स्वास्थ्य शक्ति स्कोर"),

    ,
    
    // Advice
    HEALTH_ADVICE("Health Advice", "स्वास्थ्य सल्लाह", "स्वास्थ्य सल्लाह"),
,
}
