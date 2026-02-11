package com.astro.storm.core.common

/**
 * Deep Analysis Localization Keys - Predictions
 * 600+ localization keys for comprehensive predictions
 */
enum class StringKeyDeepPrediction(override val en: String, override val ne: String, override val hi: String) : StringKeyInterface {
    // Section Headers
    SECTION_PREDICTIONS("Deep Predictions", "गहन भविष्यवाणीहरू", "गहन भविष्यवाणी"),
    ,
    SECTION_DASHA("Dasha Analysis", "दशा विश्लेषण", "दशा विश्लेषण"),
    ,
    SECTION_TRANSIT("Transit Analysis", "गोचर विश्लेषण", "गोचर विश्लेषण"),
    ,
    SECTION_YEARLY("Yearly Prediction", "वार्षिक भविष्यवाणी", "वार्षिक भविष्यवाणी"),
    ,
    SECTION_MONTHLY("Monthly Predictions", "मासिक भविष्यवाणीहरू", "मासिक भविष्यवाणी"),
    ,
    SECTION_LIFE_AREAS("Life Area Predictions", "जीवन क्षेत्र भविष्यवाणीहरू", "जीवन क्षेत्र भविष्यवाणी"),
    ,
    SECTION_YOGA_ACTIVATION("Yoga Activation Timeline", "योग सक्रियता समयरेखा", "यहग सक्रियता समयरेखा"),
    ,
    SECTION_CRITICAL("Critical Periods", "महत्वपूर्ण अवधिहरू", "महत्वपूर्ण अवधि"),
    ,
    SECTION_OPPORTUNITIES("Opportunity Windows", "अवसर सञ्झ्यालहरू", "अवसर सञ्झ्याल"),
    ,
    SECTION_REMEDIES("Remedial Measures", "उपचारात्मक उपायहरू", "उपचारात्मक उपाय"),

    ,
    
    // Mahadasha
    MAHADASHA_TITLE("Current Mahadasha", "वर्तमान महादशा", "वर्तमान महादशा"),
    ,
    MAHADASHA_PLANET("Mahadasha Planet", "महादशा ग्रह", "महादशा ग्रह"),
    ,
    MAHADASHA_PERIOD("Mahadasha Period", "महादशा अवधि", "महादशा अवधि"),
    ,
    MAHADASHA_THEME("Overall Theme", "समग्र विषय", "समग्र विषय"),
    ,
    MAHADASHA_EFFECTS("Life Area Effects", "जीवन क्षेत्र प्रभावहरू", "जीवन क्षेत्र प्रभाव"),
    ,
    MAHADASHA_STRENGTHS("Period Strengths", "अवधि शक्तिहरू", "अवधि शक्ति"),
    ,
    MAHADASHA_CHALLENGES("Period Challenges", "अवधि चुनौतीहरू", "अवधि चुनौती"),
    ,
    MAHADASHA_ADVICE("Period Advice", "अवधि सल्लाह", "अवधि सल्लाह"),

    ,
    
    // Antardasha
    ANTARDASHA_TITLE("Current Antardasha", "वर्तमान अन्तर्दशा", "वर्तमान अन्तर्दशा"),
    ,
    ANTARDASHA_PLANET("Antardasha Planet", "अन्तर्दशा ग्रह", "अन्तर्दशा ग्रह"),
    ,
    ANTARDASHA_PERIOD("Antardasha Period", "अन्तर्दशा अवधि", "अन्तर्दशा अवधि"),
    ,
    ANTARDASHA_THEME("Refined Theme", "परिष्कृत विषय", "परिष्कृत विषय"),
    ,
    ANTARDASHA_EFFECTS("Short-Term Effects", "छोटो-अवधि प्रभावहरू", "हैोटो-अवधि प्रभाव"),
    ,
    ACTIVATED_YOGAS("Activated Yogas", "सक्रिय योगहरू", "सक्रिय यहग"),

    ,
    
    // Upcoming Dashas
    UPCOMING_DASHAS("Upcoming Dasha Periods", "आगामी दशा अवधिहरू", "आगामी दशा अवधि"),
    ,
    DASHA_PREVIEW("Dasha Preview", "दशा पूर्वावलोकन", "दशा पूर्वावलोकन"),
    ,
    DASHA_BALANCE("Dasha Balance", "दशा सन्तुलन", "दशा सन्तुलन"),

    ,
    
    // Planet Dasha Themes
    DASHA_SUN("Sun Dasha Theme", "सूर्य दशा विषय", "सूर्य दशा विषय"),
    ,
    DASHA_MOON("Moon Dasha Theme", "चन्द्रमा दशा विषय", "चन्द्र में दशा विषय"),
    ,
    DASHA_MARS("Mars Dasha Theme", "मंगल दशा विषय", "मंगल दशा विषय"),
    ,
    DASHA_MERCURY("Mercury Dasha Theme", "बुध दशा विषय", "बुध दशा विषय"),
    ,
    DASHA_JUPITER("Jupiter Dasha Theme", "बृहस्पति दशा विषय", "बृहस्पति दशा विषय"),
    ,
    DASHA_VENUS("Venus Dasha Theme", "शुक्र दशा विषय", "शुक्र दशा विषय"),
    ,
    DASHA_SATURN("Saturn Dasha Theme", "शनि दशा विषय", "शनि दशा विषय"),
    ,
    DASHA_RAHU("Rahu Dasha Theme", "राहु दशा विषय", "राहु दशा विषय"),
    ,
    DASHA_KETU("Ketu Dasha Theme", "केतु दशा विषय", "केतु दशा विषय"),

    ,
    
    // Transits
    MAJOR_TRANSITS("Major Transits", "प्रमुख गोचरहरू", "प्रमुख गोचर"),
    ,
    SATURN_TRANSIT("Saturn Transit", "शनि गोचर", "शनि गोचर"),
    ,
    JUPITER_TRANSIT("Jupiter Transit", "बृहस्पति गोचर", "बृहस्पति गोचर"),
    ,
    RAHU_KETU_TRANSIT("Rahu-Ketu Transit", "राहु-केतु गोचर", "राहु-केतु गोचर"),
    ,
    TRANSIT_EFFECT("Transit Effect", "गोचर प्रभाव", "गोचर प्रभाव"),
    ,
    TRANSIT_DURATION("Transit Duration", "गोचर अवधि", "गोचर अवधि"),
    ,
    TRANSIT_INTENSITY("Transit Intensity", "गोचर तीव्रता", "गोचर तीव्रता"),

    ,
    
    // Sade Sati
    SADE_SATI_TITLE("Sade Sati Analysis", "साढे साती विश्लेषण", "साढे साती विश्लेषण"),
    ,
    SADE_SATI_ACTIVE("Sade Sati Active", "साढे साती सक्रिय", "साढे साती सक्रिय"),
    ,
    SADE_SATI_PHASE("Sade Sati Phase", "साढे साती चरण", "साढे साती चरण"),
    ,
    SADE_SATI_RISING("Rising Phase", "उदय चरण", "उदय चरण"),
    ,
    SADE_SATI_PEAK("Peak Phase", "शिखर चरण", "शिखर चरण"),
    ,
    SADE_SATI_SETTING("Setting Phase", "अस्त चरण", "अस्त चरण"),
    ,
    SADE_SATI_EFFECTS("Sade Sati Effects", "साढे साती प्रभावहरू", "साढे साती प्रभाव"),
    ,
    SADE_SATI_REMEDIES("Sade Sati Remedies", "साढे साती उपचारहरू", "साढे साती उपचार"),

    ,
    
    // Jupiter Transit
    JUPITER_TRANSIT_HOUSE("Jupiter Transit House", "बृहस्पति गोचर भाव", "बृहस्पति गोचर भाव"),
    ,
    JUPITER_FAVORABLE_AREAS("Jupiter Favorable Areas", "बृहस्पति अनुकूल क्षेत्रहरू", "बृहस्पति अनुकूल क्षेत्र"),

    ,
    
    // Nodal Transit
    RAHU_TRANSIT_HOUSE("Rahu Transit House", "राहु गोचर भाव", "राहु गोचर भाव"),
    ,
    KETU_TRANSIT_HOUSE("Ketu Transit House", "केतु गोचर भाव", "केतु गोचर भाव"),
    ,
    NODAL_AXIS_EFFECTS("Nodal Axis Effects", "नोडल अक्ष प्रभावहरू", "नोडल अक्ष प्रभाव"),

    ,
    
    // Yearly Prediction
    YEARLY_THEME("Yearly Theme", "वार्षिक विषय", "वार्षिक विषय"),
    ,
    YEARLY_RATING("Overall Year Rating", "समग्र वर्ष मूल्याङ्कन", "समग्र वर्ष मूल्यांकन"),
    ,
    CAREER_OUTLOOK("Career Outlook", "क्यारियर दृष्टिकोण", "क्यारियर दृष्टिकोण"),
    ,
    RELATIONSHIP_OUTLOOK("Relationship Outlook", "सम्बन्ध दृष्टिकोण", "संबंध दृष्टिकोण"),
    ,
    HEALTH_OUTLOOK("Health Outlook", "स्वास्थ्य दृष्टिकोण", "स्वास्थ्य दृष्टिकोण"),
    ,
    WEALTH_OUTLOOK("Wealth Outlook", "धन दृष्टिकोण", "धन दृष्टिकोण"),
    ,
    KEY_MONTHS("Key Months", "मुख्य महिनाहरू", "मुख्य महिना"),
    ,
    YEARLY_ADVICE("Yearly Advice", "वार्षिक सल्लाह", "वार्षिक सल्लाह"),

    ,
    
    // Monthly Prediction
    MONTHLY_THEME("Monthly Theme", "मासिक विषय", "मासिक विषय"),
    ,
    MONTHLY_RATING("Monthly Rating", "मासिक मूल्याङ्कन", "मासिक मूल्यांकन"),
    ,
    FOCUS_AREAS("Focus Areas", "फोकस क्षेत्रहरू", "फोकस क्षेत्र"),
    ,
    FAVORABLE_DATES("Favorable Dates", "अनुकूल मितिहरू", "अनुकूल मिति"),
    ,
    CHALLENGING_DATES("Challenging Dates", "चुनौतीपूर्ण मितिहरू", "चुनौतीपूर्ण मिति"),

    ,
    
    // Life Area Predictions
    LIFE_AREA_TITLE("Life Area Predictions", "जीवन क्षेत्र भविष्यवाणीहरू", "जीवन क्षेत्र भविष्यवाणी"),
    ,
    AREA_GENERAL("General Life", "सामान्य जीवन", "सामान्य जीवन"),
    ,
    AREA_CAREER("Career", "क्यारियर", "करियर"),
    ,
    AREA_RELATIONSHIP("Relationships", "सम्बन्धहरू", "संबंध"),
    ,
    AREA_HEALTH("Health", "स्वास्थ्य", "स्वास्थ्य"),
    ,
    AREA_WEALTH("Wealth", "धन", "धन"),
    ,
    AREA_EDUCATION("Education", "शिक्षा", "शिक्षा"),
    ,
    AREA_SPIRITUAL("Spirituality", "आध्यात्मिकता", "आध्यात्मिकता"),
    ,
    SHORT_TERM("Short Term Outlook", "छोटो अवधि दृष्टिकोण", "हैोटो अवधि दृष्टिकोण"),
    ,
    MEDIUM_TERM("Medium Term Outlook", "मध्यम अवधि दृष्टिकोण", "मध्यम अवधि दृष्टिकोण"),
    ,
    LONG_TERM("Long Term Outlook", "दीर्घ अवधि दृष्टिकोण", "दीर्घ अवधि दृष्टिकोण"),
    ,
    TIMING_ANALYSIS("Timing Analysis", "समय विश्लेषण", "समय विश्लेषण"),

    ,
    
    // Yoga Activation
    YOGA_ACTIVATION_TITLE("Yoga Activation Events", "योग सक्रियता घटनाहरू", "यहग सक्रियता घटना"),
    ,
    ACTIVATION_PERIOD("Activation Period", "सक्रियता अवधि", "सक्रियता अवधि"),
    ,
    EXPECTED_RESULTS("Expected Results", "अपेक्षित परिणामहरू", "अपेक्षित परिणाम"),
    ,
    ACTIVATION_STRENGTH("Activation Strength", "सक्रियता शक्ति", "सक्रियता शक्ति"),

    ,
    
    // Critical Periods
    CRITICAL_TITLE("Critical Periods", "महत्वपूर्ण अवधिहरू", "महत्वपूर्ण अवधि"),
    ,
    CRITICAL_NAME("Period Name", "अवधि नाम", "अवधि नाम"),
    ,
    CRITICAL_DURATION("Duration", "अवधि", "अवधि"),
    ,
    CRITICAL_AREA("Area Affected", "प्रभावित क्षेत्र", "प्रभावित क्षेत्र"),
    ,
    CRITICAL_NATURE("Period Nature", "अवधि प्रकृति", "अवधि प्रकृति"),
    ,
    NATURE_CHALLENGING("Challenging", "चुनौतीपूर्ण", "चुनौतीपूर्ण"),
    ,
    NATURE_TRANSFORMATIVE("Transformative", "परिवर्तनकारी", "परिवर्तनकारी"),
    ,
    NATURE_KARMIC("Karmic", "कार्मिक", "कार्मिक"),
    ,
    CRITICAL_ADVICE("Period Advice", "अवधि सल्लाह", "अवधि सल्लाह"),

    ,
    
    // Opportunity Windows
    OPPORTUNITY_TITLE("Opportunity Windows", "अवसर सञ्झ्यालहरू", "अवसर सञ्झ्याल"),
    ,
    OPPORTUNITY_NAME("Window Name", "सञ्झ्याल नाम", "सञ्झ्याल नाम"),
    ,
    OPPORTUNITY_DURATION("Duration", "अवधि", "अवधि"),
    ,
    AFFECTED_AREAS("Affected Areas", "प्रभावित क्षेत्रहरू", "प्रभावित क्षेत्र"),
    ,
    OPPORTUNITY_TYPE("Opportunity Type", "अवसर प्रकार", "अवसर प्रकार"),
    ,
    OPPORTUNITY_ADVICE("How to Maximize", "अधिकतम कसरी गर्ने", "अधिकतम कसरी करनेे"),

    ,
    
    // Remedial Measures
    REMEDIES_TITLE("Remedial Measures", "उपचारात्मक उपायहरू", "उपचारात्मक उपाय"),
    ,
    GEMSTONE_REMEDIES("Gemstone Remedies", "रत्न उपचारहरू", "रत्न उपचार"),
    ,
    PRIMARY_GEMSTONE("Primary Gemstone", "प्राथमिक रत्न", "प्राथमिक रत्न"),
    ,
    ALTERNATIVE_GEMSTONE("Alternative Gemstone", "वैकल्पिक रत्न", "वैकल्पिक रत्न"),
    ,
    WEARING_GUIDELINES("Wearing Guidelines", "लगाउने दिशानिर्देशहरू", "लगाउने दिशानिर्देश"),
    ,
    GEMSTONE_CAUTIONS("Cautions", "सावधानीहरू", "सावधानी"),

    ,
    
    // Mantra Remedies
    MANTRA_REMEDIES("Mantra Remedies", "मन्त्र उपचारहरू", "मन्त्र उपचार"),
    ,
    BEEJA_MANTRA("Beeja Mantra", "बीज मन्त्र", "बीज मन्त्र"),
    ,
    FULL_MANTRA("Full Mantra", "पूर्ण मन्त्र", "पूर्ण मन्त्र"),
    ,
    CHANT_COUNT("Chant Count", "जप गणना", "जप गणना"),
    ,
    BEST_TIME("Best Time", "सर्वोत्तम समय", "सर्वोत्तम समय"),

    ,
    
    // Charitable Remedies
    CHARITABLE_REMEDIES("Charitable Remedies", "दान उपचारहरू", "दान उपचार"),
    ,
    DONATION_ITEMS("Donation Items", "दान वस्तुहरू", "दान वस्तु"),
    ,
    BEST_DAY("Best Day", "सर्वोत्तम दिन", "सर्वोत्तम दिन"),
    ,
    DONATION_GUIDELINES("Guidelines", "दिशानिर्देशहरू", "दिशानिर्देश"),

    ,
    
    // Fasting Remedies
    FASTING_REMEDIES("Fasting Remedies", "उपवास उपचारहरू", "उपवास उपचार"),
    ,
    FASTING_DAY("Fasting Day", "उपवास दिन", "उपवास दिन"),
    ,
    FASTING_TYPE("Fasting Type", "उपवास प्रकार", "उपवास प्रकार"),
    ,
    FASTING_GUIDELINES("Fasting Guidelines", "उपवास दिशानिर्देशहरू", "उपवास दिशानिर्देश"),

    ,
    
    // Yogic Remedies
    YOGIC_REMEDIES("Yogic Remedies", "योगिक उपचारहरू", "यहगिक उपचार"),
    ,
    PRACTICE_NAME("Practice Name", "अभ्यास नाम", "अभ्यास नाम"),
    ,
    TARGET_PLANET("For Planet", "ग्रहको लागि", "ग्रह के लिए"),
    ,
    PRACTICE_GUIDELINES("Guidelines", "दिशानिर्देशहरू", "दिशानिर्देश"),
    ,
    PRACTICE_BENEFITS("Benefits", "फाइदाहरू", "फाइदा"),

    ,
    
    // Overall
    OVERALL_ADVICE("Overall Remedial Advice", "समग्र उपचारात्मक सल्लाह", "समग्र उपचारात्मक सल्लाह"),
    ,
    PREDICTION_SUMMARY("Prediction Summary", "भविष्यवाणी सारांश", "भविष्यवाणी सारांश"),
    ,
    PREDICTION_SCORE("Prediction Favorability Score", "भविष्यवाणी अनुकूलता स्कोर", "भविष्यवाणी अनुकूलता स्कोर"),

    ,
    
    // Favorable/Unfavorable
    FAVORABLE("Favorable", "अनुकूल", "अनुकूल"),
    ,
    NEUTRAL("Neutral", "तटस्थ", "तटस्थ"),
    ,
    CHALLENGING("Challenging", "चुनौतीपूर्ण", "चुनौतीपूर्ण"),
    ,
    VERY_FAVORABLE("Very Favorable", "धेरै अनुकूल", "धेरै अनुकूल"),

    ,

    // Nadi Amsha
    NADI_NAME("Nadi Amsha Name", "नाडी अंश नाम", "नाडी अंश नाम"),
    ,
    NADI_RECTIFICATION_DESC("Nadi Rectification Description", "नाडी शुद्धिकरण विवरण", "नाडी शुद्धिकरण विवरण"),
,
}
