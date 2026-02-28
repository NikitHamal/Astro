package com.astro.vajra.core.common

/**
 * String keys for Ashtamangala Prashna (8-fold Horary) System
 * Complete EN/NE localization for Kerala Ashtamangala divination
 */
enum class StringKeyAshtamangala(override val en: String, override val ne: String) : StringKeyInterface {

    // ============================================
    // SCREEN HEADERS
    // ============================================
    SCREEN_TITLE("Ashtamangala Prashna", "अष्टमंगल प्रश्न"),
    SCREEN_SUBTITLE("Eight-fold Horary Divination", "आठ-गुना प्रश्न विज्ञान"),
    SCREEN_DESC("Kerala tradition cowrie shell divination integrated with chart analysis", "कुण्डली विश्लेषणसँग एकीकृत केरल परम्परा कौडी भविष्यवाणी"),

    // ============================================
    // TABS
    // ============================================
    TAB_QUERY("Query", "प्रश्न"),
    TAB_ANALYSIS("Analysis", "विश्लेषण"),
    TAB_POSITIONS("Positions", "स्थितिहरू"),
    TAB_TIMING("Timing", "समय"),

    // ============================================
    // EIGHT POSITION NAMES (Ashtamangala Deities/Directions)
    // ============================================
    POS_1_AGNI("Agni (Fire)", "अग्नि"),
    POS_1_DESC("Southeast - Initiative, energy, passion, transformation", "आग्नेय - पहल, ऊर्जा, जोश, रूपान्तरण"),

    POS_2_INDRA("Indra (King)", "इन्द्र"),
    POS_2_DESC("East - Success, authority, power, achievements", "पूर्व - सफलता, अधिकार, शक्ति, उपलब्धि"),

    POS_3_YAMA("Yama (Death)", "यम"),
    POS_3_DESC("South - Endings, karma, justice, life lessons", "दक्षिण - अन्त्य, कर्म, न्याय, जीवन पाठ"),

    POS_4_NIRRITI("Nirriti (Decay)", "निऋति"),
    POS_4_DESC("Southwest - Obstacles, hidden enemies, dissolution", "नैऋत्य - बाधाहरू, लुकेका शत्रु, विघटन"),

    POS_5_VARUNA("Varuna (Water)", "वरुण"),
    POS_5_DESC("West - Emotions, intuition, healing, purification", "पश्चिम - भावना, अन्तर्ज्ञान, उपचार, शुद्धि"),

    POS_6_VAYU("Vayu (Wind)", "वायु"),
    POS_6_DESC("Northwest - Communication, travel, change, movement", "वायव्य - सञ्चार, यात्रा, परिवर्तन, गति"),

    POS_7_KUBERA("Kubera (Wealth)", "कुबेर"),
    POS_7_DESC("North - Wealth, prosperity, resources, stability", "उत्तर - धन, समृद्धि, स्रोत, स्थिरता"),

    POS_8_ISHANA("Ishana (Divine)", "ईशान"),
    POS_8_DESC("Northeast - Spirituality, wisdom, divine grace", "ईशान - आध्यात्म, ज्ञान, दैवी कृपा"),

    // ============================================
    // QUESTION CATEGORIES
    // ============================================
    CAT_TITLE("Question Category", "प्रश्न श्रेणी"),
    CAT_SELECT("Select your query type", "आफ्नो प्रश्न प्रकार छान्नुहोस्"),
    CAT_HEALTH("Health & Wellbeing", "स्वास्थ्य र कल्याण"),
    CAT_HEALTH_DESC("Recovery, illness, vitality queries", "निको हुने, रोग, जीवनी शक्ति प्रश्नहरू"),
    CAT_WEALTH("Wealth & Finance", "धन र वित्त"),
    CAT_WEALTH_DESC("Money, investments, material gains", "पैसा, लगानी, भौतिक लाभ"),
    CAT_RELATIONSHIP("Relationships", "सम्बन्धहरू"),
    CAT_RELATIONSHIP_DESC("Marriage, partnerships, family", "विवाह, साझेदारी, परिवार"),
    CAT_CAREER("Career & Work", "क्यारियर र काम"),
    CAT_CAREER_DESC("Job, business, profession queries", "नोकरी, व्यापार, पेशा प्रश्नहरू"),
    CAT_TRAVEL("Travel & Movement", "यात्रा र गति"),
    CAT_TRAVEL_DESC("Journeys, relocation, migrations", "यात्राहरू, स्थानान्तरण, बसाइँसराइ"),
    CAT_LOST("Lost Objects", "हराएका वस्तुहरू"),
    CAT_LOST_DESC("Finding missing items or valuables", "हराएका सामान वा बहुमूल्य वस्तुहरू खोज्ने"),
    CAT_LITIGATION("Litigation & Disputes", "मुद्दा र विवाद"),
    CAT_LITIGATION_DESC("Legal matters, conflicts, competitions", "कानूनी मामिला, द्वन्द्व, प्रतिस्पर्धा"),
    CAT_SPIRITUAL("Spiritual Progress", "आध्यात्मिक प्रगति"),
    CAT_SPIRITUAL_DESC("Moksha, sadhana, divine blessings", "मोक्ष, साधना, दैवी आशीर्वाद"),

    // ============================================
    // COWRIE THROW
    // ============================================
    COWRIE_TITLE("Cowrie Shell Throw", "कौडी फाल्ने"),
    COWRIE_INSTRUCTION("Focus on your question and cast the shells", "आफ्नो प्रश्नमा ध्यान केन्द्रित गर्नुहोस् र कौडी फाल्नुहोस्"),
    COWRIE_CAST("Cast Shells", "कौडी फाल्नुहोस्"),
    COWRIE_RECAST("Recast", "फेरि फाल्नुहोस्"),
    COWRIE_RESULT("Shell Count", "कौडी गणना"),
    COWRIE_OPEN("Open (Face Up)", "खुला (मुख माथि)"),
    COWRIE_CLOSED("Closed (Face Down)", "बन्द (मुख तल)"),
    COWRIE_TOTAL("Total Shells: 8", "जम्मा कौडी: ८"),
    COWRIE_RATIO("Open:Closed Ratio", "खुला:बन्द अनुपात"),

    // ============================================
    // ANALYSIS RESULTS
    // ============================================
    RESULT_TITLE("Ashtamangala Reading", "अष्टमंगल पठन"),
    RESULT_PRIMARY("Primary Indication", "प्राथमिक संकेत"),
    RESULT_SECONDARY("Secondary Influences", "द्वितीयक प्रभावहरू"),
    RESULT_STRENGTH("Indication Strength", "संकेत बल"),
    RESULT_FAVORABLE("Favorable", "अनुकूल"),
    RESULT_MIXED("Mixed", "मिश्रित"),
    RESULT_CHALLENGING("Challenging", "चुनौतीपूर्ण"),
    RESULT_VERY_FAVORABLE("Very Favorable", "अत्यन्त अनुकूल"),
    RESULT_VERY_CHALLENGING("Very Challenging", "अत्यन्त चुनौतीपूर्ण"),
    RESULT_NEUTRAL("Neutral", "तटस्थ"),

    // ============================================
    // POSITION ANALYSIS
    // ============================================
    POS_ACTIVE("Active Positions", "सक्रिय स्थितिहरू"),
    POS_DOMINANT("Dominant Direction", "प्रभावशाली दिशा"),
    POS_DEITY("Ruling Deity", "शासक देवता"),
    POS_ELEMENT("Element", "तत्व"),
    POS_SIGNIFICATION("Signification", "अर्थ"),
    POS_EFFECT("Effect on Query", "प्रश्नमा प्रभाव"),
    POS_STRENGTH_SCORE("Position Strength", "स्थिति बल"),

    // ============================================
    // NUMERICAL STRENGTH
    // ============================================
    NUM_TITLE("Numerical Analysis", "संख्यात्मक विश्लेषण"),
    NUM_OPEN_SHELLS("Open Shells", "खुला कौडीहरू"),
    NUM_INTERPRETATION("Shell Interpretation", "कौडी व्याख्या"),
    NUM_0("0 Open - Total denial, delay, not now", "० खुला - पूर्ण अस्वीकार, ढिलाइ, अहिले होइन"),
    NUM_1("1 Open - Weak affirmation, much effort needed", "१ खुला - कमजोर पुष्टि, धेरै प्रयास चाहिन्छ"),
    NUM_2("2 Open - Partial success, obstacles remain", "२ खुला - आंशिक सफलता, बाधाहरू बाँकी"),
    NUM_3("3 Open - Favorable with conditions", "३ खुला - शर्तहरूसँग अनुकूल"),
    NUM_4("4 Open - Balanced outcome, transformation", "४ खुला - सन्तुलित परिणाम, रूपान्तरण"),
    NUM_5("5 Open - Good success likely", "५ खुला - राम्रो सफलता सम्भव"),
    NUM_6("6 Open - Strong positive indication", "६ खुला - बलियो सकारात्मक संकेत"),
    NUM_7("7 Open - Very favorable outcome", "७ खुला - अत्यन्त अनुकूल परिणाम"),
    NUM_8("8 Open - Complete success, divine blessing", "८ खुला - पूर्ण सफलता, दैवी आशीर्वाद"),

    // ============================================
    // TIMING
    // ============================================
    TIME_TITLE("Timing Prediction", "समय भविष्यवाणी"),
    TIME_IMMEDIATE("Immediate (1-7 days)", "तुरुन्तै (१-७ दिन)"),
    TIME_SHORT("Short term (1-4 weeks)", "छोटो अवधि (१-४ हप्ता)"),
    TIME_MEDIUM("Medium term (1-3 months)", "मध्यम अवधि (१-३ महिना)"),
    TIME_LONG("Long term (3-12 months)", "लामो अवधि (३-१२ महिना)"),
    TIME_DELAYED("Delayed (1+ year)", "ढिलो (१+ वर्ष)"),
    TIME_UNCERTAIN("Timing Uncertain", "समय अनिश्चित"),
    TIME_BEST_DAY("Best Day for Action", "कार्यको लागि उत्तम दिन"),
    TIME_AVOID_DAY("Avoid Acting On", "यो दिनमा नगर्नुहोस्"),

    // ============================================
    // PRASHNA CHART INTEGRATION
    // ============================================
    CHART_INTEGRATION("Chart Integration", "कुण्डली एकीकरण"),
    CHART_VALIDATION("Prashna Chart Validation", "प्रश्न कुण्डली प्रमाणीकरण"),
    CHART_SUPPORTS("Chart Supports Reading", "कुण्डलीले पठनलाई समर्थन गर्छ"),
    CHART_CONFLICTS("Chart Shows Conflict", "कुण्डलीले विरोध देखाउँछ"),
    CHART_NEUTRAL_EFFECT("Chart Neutral", "कुण्डली तटस्थ"),
    CHART_LAGNA_EFFECT("Prashna Lagna Effect", "प्रश्न लग्न प्रभाव"),
    CHART_MOON_EFFECT("Moon's Position Effect", "चन्द्रमाको स्थिति प्रभाव"),

    // ============================================
    // REMEDIES & RECOMMENDATIONS
    // ============================================
    REMEDY_TITLE("Recommendations", "सिफारिसहरू"),
    REMEDY_DEITY_WORSHIP("Deity Worship", "देवता पूजा"),
    REMEDY_MANTRA("Recommended Mantra", "सिफारिस गरिएको मन्त्र"),
    REMEDY_DIRECTION("Auspicious Direction", "शुभ दिशा"),
    REMEDY_COLOR("Favorable Color", "अनुकूल रंग"),
    REMEDY_DAY("Best Weekday", "उत्तम वार"),
    REMEDY_DONATION("Recommended Donation", "सिफारिस गरिएको दान"),
    REMEDY_ACTION("Suggested Action", "सुझाव गरिएको कार्य"),

    // ============================================
    // YES/NO PROBABILITY
    // ============================================
    YESNO_TITLE("Yes/No Probability", "हो/होइन सम्भावना"),
    YESNO_YES("Yes", "हो"),
    YESNO_NO("No", "होइन"),
    YESNO_PROBABILITY("Probability", "सम्भावना"),
    YESNO_CONFIDENCE("Confidence Level", "विश्वास स्तर"),
    YESNO_HIGH("High Confidence", "उच्च विश्वास"),
    YESNO_MEDIUM("Medium Confidence", "मध्यम विश्वास"),
    YESNO_LOW("Low Confidence", "न्यून विश्वास"),

    // ============================================
    // SPECIAL READINGS
    // ============================================
    SPECIAL_LOST_DIRECTION("Lost Object Direction", "हराएको वस्तुको दिशा"),
    SPECIAL_LOST_RECOVERY("Recovery Possibility", "प्राप्ति सम्भावना"),
    SPECIAL_TRAVEL_SAFE("Travel Safety", "यात्रा सुरक्षा"),
    SPECIAL_LITIGATION_WIN("Litigation Success", "मुद्दामा जित"),
    SPECIAL_MARRIAGE_TIMING("Marriage Timing", "विवाह समय"),
    SPECIAL_HEALTH_RECOVERY("Health Recovery", "स्वास्थ्य सुधार"),

    // ============================================
    // INFORMATION DIALOG
    // ============================================
    INFO_TITLE("About Ashtamangala Prashna", "अष्टमंगल प्रश्नको बारेमा"),
    INFO_DESC("Ashtamangala Prashna is a powerful horary divination system from Kerala, using eight cowrie shells to determine outcomes. Each position represents a deity and direction, and the combination of open/closed shells reveals the answer to queries. This app integrates traditional shell readings with Prashna (horary) chart analysis for comprehensive guidance.", "अष्टमंगल प्रश्न केरलको एक शक्तिशाली प्रश्न विज्ञान प्रणाली हो, जसले परिणाम निर्धारण गर्न आठ कौडीहरू प्रयोग गर्छ। प्रत्येक स्थितिले एक देवता र दिशा प्रतिनिधित्व गर्छ, र खुला/बन्द कौडीहरूको संयोजनले प्रश्नको उत्तर प्रकट गर्छ। यो एपले व्यापक मार्गदर्शनको लागि प्रश्न कुण्डली विश्लेषणसँग परम्परागत कौडी पठन एकीकृत गर्छ।"),
    INFO_VEDIC_REF("References: Prashna Marga, Kerala Jyotisha traditions", "सन्दर्भ: प्रश्न मार्ग, केरल ज्योतिष परम्पराहरू"),
    INFO_CLOSE("Close", "बन्द गर्नुहोस्"),

    // ============================================
    // ERROR MESSAGES
    // ============================================
    ERROR_NO_CHART("Please create a chart first", "कृपया पहिले कुण्डली बनाउनुहोस्"),
    ERROR_CALCULATION("Unable to calculate reading", "पठन गणना गर्न असमर्थ"),
    ERROR_INVALID_CATEGORY("Please select a question category", "कृपया प्रश्न श्रेणी छान्नुहोस्"),

    // ============================================
    // UI ELEMENTS
    // ============================================
    BTN_INTERPRET("Get Interpretation", "व्याख्या प्राप्त गर्नुहोस्"),
    BTN_NEW_QUERY("New Query", "नयाँ प्रश्न"),
    BTN_SAVE_READING("Save Reading", "पठन सुरक्षित गर्नुहोस्"),
    LOADING("Consulting the shells...", "कौडीहरूसँग परामर्श गर्दै..."),
    LOADING_ANALYSIS("Analyzing patterns...", "ढाँचाहरू विश्लेषण गर्दै..."),

    // ============================================
    // FEATURE CARD
    // ============================================
    FEATURE_TITLE("Ashtamangala", "अष्टमंगल"),
    FEATURE_DESC("8-fold horary divination system", "आठ-गुना प्रश्न विज्ञान प्रणाली"),

    // New keys
    POSITIVE_FACTORS("Positive Factors", "सकारात्मक कारकहरू"),
    CHALLENGES("Challenges", "चुनौतिहरू"),
    CAUTIONS("Cautions", "सावधानीहरू"),
    STRENGTH("Strength", "बल"),
    SPECIAL_INSIGHTS("Special Insights", "विशेष अन्तर्दृष्टि"),
    DIRECTION_TO_SEARCH("Direction to search", "खोज्ने दिशा"),
    RECOVERY_LIKELIHOOD("Recovery likelihood", "प्राप्ति सम्भावना"),
    SAFETY_LEVEL("Safety level", "सुरक्षा स्तर"),
    SUCCESS_PROBABILITY("Success probability", "सफलताको सम्भावना"),
    TIMING_INDICATION("Timing indication", "समय संकेत"),
    RECOVERY_OUTLOOK("Recovery outlook", "सुधारको दृष्टिकोण"),
    TITHI_SUGGESTION("Tithi Suggestion", "तिथि सुझाव"),
    THE_EIGHT_POSITIONS("The Eight Positions", "आठ स्थितिहरू"),
    SHELLS("Shells", "कौडीहरू"),
    ACTIVE("Active", "सक्रिय"),
    INACTIVE("Inactive", "निष्क्रिय"),
    DOMINANT("Dominant", "प्रभावशाली"),
;
}
