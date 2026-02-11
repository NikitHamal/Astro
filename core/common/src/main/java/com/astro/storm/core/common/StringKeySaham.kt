package com.astro.storm.core.common

/**
 * Localization keys for Saham (Arabic Parts) screen
 * Provides full EN/NE bilingual support
 */
enum class StringKeySaham(override val en: String, override val ne: String, override val hi: String) : StringKeyInterface {

    // ============================================
    // SCREEN TITLE & HEADERS
    // ============================================
    TITLE("Sahams (Arabic Parts)", "सहम (अरबी भाग)", "सहम (अरबी भाग)"),
    ,
    SUBTITLE("Sensitive points calculated from planetary positions", "ग्रह स्थितिबाट गणना गरिएको संवेदनशील बिन्दुहरू", "ग्रह स्थितिबाट गणना गरिए के संवेदनशील बिन्दु"),

    ,

    // ============================================
    // CHART TYPE INDICATORS
    // ============================================
    DAY_BIRTH_CHART("Day Birth Chart", "दिवा जन्म कुण्डली", "दिवा जन्म कुंडली"),
    ,
    NIGHT_BIRTH_CHART("Night Birth Chart", "रात्रि जन्म कुण्डली", "रात्रि जन्म कुंडली"),
    ,
    DAY_BIRTH("Day Birth", "दिवा जन्म", "दिवा जन्म"),
    ,
    NIGHT_BIRTH("Night Birth", "रात्रि जन्म", "रात्रि जन्म"),
    ,
    FORMULA_ADJUSTED("Formula Adjusted", "सूत्र समायोजित", "सूत्र समायहजित"),

    ,

    // ============================================
    // OVERVIEW TAB
    // ============================================
    TAB_OVERVIEW("Overview", "अवलोकन", "अवलोकन"),
    ,
    TAB_ALL_SAHAMS("All Sahams", "सबै सहमहरू", "सभी सहम"),
    ,
    TAB_BY_CATEGORY("By Category", "वर्गानुसार", "वर्गानुसार"),

    ,

    CHART_STRENGTH("Chart Strength", "कुण्डली बल", "कुंडली बल"),
    ,
    LIFE_AREA_BALANCE("Life Area Balance", "जीवन क्षेत्र सन्तुलन", "जीवन क्षेत्र सन्तुलन"),
    ,
    ACTIVE_SAHAMS_TODAY("Active Sahams today", "आज सक्रिय सहमहरू", "आज सक्रिय सहम"),
    ,
    TOTAL_SAHAMS("Total Sahams", "कुल सहमहरू", "कुल सहम"),
    ,
    AVERAGE_STRENGTH("Average Strength", "औसत बल", "औसत बल"),

    ,

    // ============================================
    // SEARCH & FILTER
    // ============================================
    SEARCH_SAHAMS("Search Sahams...", "सहमहरू खोज्नुहोस्...", "सहम खोज्नुहोस्..."),
    ,
    FILTER_ALL("All", "सबै", "सभी"),
    ,
    FILTER_WEALTH("Wealth", "धन", "धन"),
    ,
    FILTER_HEALTH("Health", "स्वास्थ्य", "स्वास्थ्य"),
    ,
    FILTER_RELATIONSHIPS("Relationships", "सम्बन्ध", "संबंध"),
    ,
    FILTER_CAREER("Career", "क्यारियर", "करियर"),
    ,
    FILTER_SPIRITUAL("Spiritual", "आध्यात्मिक", "आध्यात्मिक"),
    ,
    FILTER_OTHER("Other", "अन्य", "अन्य"),
    ,
    NO_SAHAMS_FOUND("No Sahams found matching criteria", "मापदण्ड अनुरूप कुनै सहम फेला परेन", "मापदण्ड अनुरूप कुनै सहम फेला परेन"),

    ,

    // ============================================
    // SAHAM CARD DETAILS
    // ============================================
    ACTIVE_BADGE("ACTIVE", "सक्रिय", "सक्रिय"),
    ,
    SIGN_LABEL("Sign", "राशि", "राशि"),
    ,
    HOUSE_LABEL("House", "भाव", "भाव"),
    ,
    LORD_LABEL("Lord", "स्वामी", "स्वामी"),
    ,
    DEGREE_LABEL("Degree", "अंश", "अंश"),
    ,
    NAKSHATRA_LABEL("Nakshatra", "नक्षत्र", "नक्षत्र"),
    ,
    STRENGTH_LABEL("Strength", "बल", "बल"),
    ,
    FORMULA_LABEL("Formula", "सूत्र", "सूत्र"),
    ,
    ANALYSIS_LABEL("Analysis", "विश्लेषण", "विश्लेषण"),

    ,

    // ============================================
    // STRENGTH INDICATORS
    // ============================================
    STRENGTH_EXCELLENT("Excellent", "उत्कृष्ट", "उत्कृष्ट"),
    ,
    STRENGTH_STRONG("Strong", "बलियो", "बलियह"),
    ,
    STRENGTH_MODERATE("Moderate", "मध्यम", "मध्यम"),
    ,
    STRENGTH_WEAK("Weak", "कमजोर", "कमजोर"),

    ,

    // ============================================
    // SAHAM NAMES (Traditional Arabic Parts)
    // ============================================
    SAHAM_FORTUNE("Part of Fortune", "भाग्य सहम", "भाग्य सहम"),
    ,
    SAHAM_SPIRIT("Part of Spirit", "आत्मा सहम", "आत् में सहम"),
    ,
    SAHAM_LOVE("Part of Love", "प्रेम सहम", "प्रेम सहम"),
    ,
    SAHAM_MARRIAGE("Part of Marriage", "विवाह सहम", "विवाह सहम"),
    ,
    SAHAM_CHILDREN("Part of Children", "सन्तान सहम", "संतान सहम"),
    ,
    SAHAM_FATHER("Part of Father", "पिता सहम", "पिता सहम"),
    ,
    SAHAM_MOTHER("Part of Mother", "माता सहम", "माता सहम"),
    ,
    SAHAM_SIBLINGS("Part of Siblings", "भाइबहिनी सहम", "भाई-बहन सहम"),
    ,
    SAHAM_DEATH("Part of Death", "मृत्यु सहम", "मृत्यु सहम"),
    ,
    SAHAM_ILLNESS("Part of Illness", "रोग सहम", "रोग सहम"),
    ,
    SAHAM_SURGERY("Part of Surgery", "शल्यक्रिया सहम", "शल्यक्रिया सहम"),
    ,
    SAHAM_TRAVEL("Part of Travel", "यात्रा सहम", "यात्रा सहम"),
    ,
    SAHAM_COMMERCE("Part of Commerce", "व्यापार सहम", "व्यापार सहम"),
    ,
    SAHAM_PROFESSION("Part of Profession", "पेशा सहम", "पेशा सहम"),
    ,
    SAHAM_PROPERTY("Part of Property", "सम्पत्ति सहम", "सम्पत्ति सहम"),
    ,
    SAHAM_INHERITANCE("Part of Inheritance", "विरासत सहम", "विरासत सहम"),
    ,
    SAHAM_FAME("Part of Fame", "प्रसिद्धि सहम", "प्रसिद्धि सहम"),
    ,
    SAHAM_VICTORY("Part of Victory", "विजय सहम", "विजय सहम"),
    ,
    SAHAM_IMPRISONMENT("Part of Imprisonment", "बन्दी सहम", "बन्दी सहम"),
    ,
    SAHAM_ENEMIES("Part of Enemies", "शत्रु सहम", "शत्रु सहम"),
    ,
    SAHAM_FRIENDS("Part of Friends", "मित्र सहम", "मित्र सहम"),
    ,
    SAHAM_HIDDEN("Part of Hidden Matters", "गोप्य विषय सहम", "गोप्य विषय सहम"),
    ,
    SAHAM_KNOWLEDGE("Part of Knowledge", "ज्ञान सहम", "ज्ञान सहम"),
    ,
    SAHAM_FAITH("Part of Faith", "धर्म सहम", "धर्म सहम"),
    ,
    SAHAM_EROS("Part of Eros", "इरोस सहम", "इरोस सहम"),
    ,
    SAHAM_NECESSITY("Part of Necessity", "आवश्यकता सहम", "आवश्यकता सहम"),
    ,
    SAHAM_COURAGE("Part of Courage", "साहस सहम", "साहस सहम"),
    ,
    SAHAM_SUDDEN_EVENTS("Part of Sudden Events", "आकस्मिक घटना सहम", "आकस्मिक घटना सहम"),

    ,

    // ============================================
    // LIFE AREA CATEGORIES
    // ============================================
    CATEGORY_WEALTH("Wealth & Finance", "धन र वित्त", "धन और वित्त"),
    ,
    CATEGORY_HEALTH("Health & Vitality", "स्वास्थ्य र जीवनशक्ति", "स्वास्थ्य और जीवनशक्ति"),
    ,
    CATEGORY_RELATIONSHIPS("Love & Relationships", "प्रेम र सम्बन्ध", "प्रेम और संबंध"),
    ,
    CATEGORY_CAREER("Career & Profession", "क्यारियर र पेशा", "क्यारियर और पेशा"),
    ,
    CATEGORY_SPIRITUAL("Spiritual & Religious", "आध्यात्मिक र धार्मिक", "आध्यात्मिक और धार्मिक"),
    ,
    CATEGORY_FAMILY("Family & Home", "परिवार र घर", "परिवार और घर"),
    ,
    CATEGORY_OTHER("Other Areas", "अन्य क्षेत्रहरू", "अन्य क्षेत्र"),

    ,

    // ============================================
    // INFO DIALOG
    // ============================================
    INFO_TITLE("About Sahams", "सहमहरूको बारेमा", "सहम के बारे में"),
    ,
    INFO_DESCRIPTION("Sahams (Arabic Parts) are sensitive points calculated from the positions of planets, Ascendant, and house cusps. They reveal specific life themes and their activation periods.", "सहमहरू (अरबी भाग) ग्रहको स्थिति, लग्न र भाव सन्धिबाट गणना गरिएको संवेदनशील बिन्दुहरू हुन्। तिनीहरूले विशिष्ट जीवन विषयहरू र तिनीहरूको सक्रियता अवधिहरू प्रकट गर्छन्।", "सहम (अरबी भाग) ग्रह के स्थिति, लग्न और भाव सन्धिबाट गणना गरिए के संवेदनशील बिन्दु हैं। तिनीले विशिष्ट जीवन विषय और तिनी के सक्रियता अवधि प्रकट करता हैन्।"),
    ,
    INFO_DAY_NIGHT("Day/Night Charts: The formula for many Sahams reverses between day and night births. Day births have the Sun above the horizon, night births have it below.", "दिवा/रात्रि कुण्डली: धेरै सहमहरूको सूत्र दिवा र रात्रि जन्ममा उल्टो हुन्छ। दिवा जन्ममा सूर्य क्षितिज माथि हुन्छ, रात्रि जन्ममा तल हुन्छ।", "दिवा/रात्रि कुंडली: धेरै सहम के सूत्र दिवा और रात्रि जन्म में उल्टो हैता है। दिवा जन्म में सूर्य क्षितिज माथि हैता है, रात्रि जन्म में तल हैता है।"),
    ,
    INFO_ACTIVATION("Activation: A Saham is considered active when transiting planets conjunct or aspect it, or during relevant Dasha periods.", "सक्रियता: गोचर ग्रहले युति वा दृष्टि गर्दा, वा सान्दर्भिक दशा अवधिमा सहम सक्रिय मानिन्छ।", "सक्रियता: गोचर ग्रहले युति या दृष्टि गर्दा, या सान्दर्भिक दशा अवधि में सहम सक्रिय मानिन्है।"),
    ,
    INFO_FORMULA("Formula: Most Sahams follow the pattern: Asc + Planet A - Planet B. For night charts, planets A and B may swap positions.", "सूत्र: धेरैजसो सहमहरू यो ढाँचा पछ्याउँछन्: लग्न + ग्रह A - ग्रह B। रात्रि कुण्डलीमा, ग्रह A र B को स्थान बदलिन सक्छ।", "सूत्र: धेरैजसो सहम यह ढाँचा पहै्याउँहैं: लग्न + ग्रह A - ग्रह B। रात्रि कुंडली में, ग्रह A और B  के स्थान बदलिन सक्है।"),

    ,

    // ============================================
    // STATES
    // ============================================
    NO_CHART_SELECTED("No Chart Selected", "कुनै कुण्डली छानिएको छैन", "कुनै कुंडली हैानिए के हैैन"),
    ,
    SELECT_CHART_MESSAGE("Please select a birth chart to view Sahams", "सहमहरू हेर्न कृपया जन्म कुण्डली छान्नुहोस्", "सहम हेर्न कृपया जन्म कुंडली चुनें"),
    ,
    CALCULATING("Calculating Sahams...", "सहमहरू गणना गर्दै...", "सहम गणना गर्दै..."),
    ,
    CALCULATION_ERROR("Calculation Error", "गणना त्रुटि", "गणना त्रुटि"),

    ,

    // ============================================
    // MISCELLANEOUS
    // ============================================
    SHOW_INFO("Show information", "जानकारी देखाउनुहोस्", "जानकारी देखाउनुहोस्"),
    ,
    GOT_IT("Got it", "बुझें", "बुझें"),
    ,
    HOUSE_FORMAT("House %d", "भाव %d", "भाव %d"),
    ,
    DEGREE_FORMAT("%s° %s'", "%s° %s'", "%s° %s'"),
    ,
    NEEDS_FOCUS("Needs Focus", "ध्यान दिनुपर्ने", "ध्यान दिनुपर्ने"),
    ,
    ACTIVATED("Activated", "सक्रिय", "सक्रिय"),
    ,
    FORMULA_ADJUSTED_DESC("Formulas adjusted per Tajika tradition", "ताजिक परम्परा अनुसार सूत्रहरू समायोजित", "ताजिक परम्परा अनुसार सूत्र समायहजित"),
    ,
    STRONGEST("Strongest", "सबैभन्दा बलियो", "सभीभन्दा बलियह"),
    ,
    RECOMMENDATIONS("Recommendations", "सिफारिसहरू", "सिफारिस"),
    ,
    NO_SAHAMS_TYPE_FOUND("No %s Sahams found", "कुनै %s सहम फेला परेन", "कुनै %s सहम फेला परेन"),
,
;
}
