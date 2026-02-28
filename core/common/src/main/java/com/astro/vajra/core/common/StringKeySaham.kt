package com.astro.vajra.core.common

/**
 * Localization keys for Saham (Arabic Parts) screen
 * Provides full EN/NE bilingual support
 */
enum class StringKeySaham(override val en: String, override val ne: String) : StringKeyInterface {

    // ============================================
    // SCREEN TITLE & HEADERS
    // ============================================
    TITLE("Sahams (Arabic Parts)", "सहम (अरबी भाग)"),
    SUBTITLE("Sensitive points calculated from planetary positions", "ग्रह स्थितिबाट गणना गरिएको संवेदनशील बिन्दुहरू"),

    // ============================================
    // CHART TYPE INDICATORS
    // ============================================
    DAY_BIRTH_CHART("Day Birth Chart", "दिवा जन्म कुण्डली"),
    NIGHT_BIRTH_CHART("Night Birth Chart", "रात्रि जन्म कुण्डली"),
    DAY_BIRTH("Day Birth", "दिवा जन्म"),
    NIGHT_BIRTH("Night Birth", "रात्रि जन्म"),
    FORMULA_ADJUSTED("Formula Adjusted", "सूत्र समायोजित"),

    // ============================================
    // OVERVIEW TAB
    // ============================================
    TAB_OVERVIEW("Overview", "अवलोकन"),
    TAB_ALL_SAHAMS("All Sahams", "सबै सहमहरू"),
    TAB_BY_CATEGORY("By Category", "वर्गानुसार"),

    CHART_STRENGTH("Chart Strength", "कुण्डली बल"),
    LIFE_AREA_BALANCE("Life Area Balance", "जीवन क्षेत्र सन्तुलन"),
    ACTIVE_SAHAMS_TODAY("Active Sahams today", "आज सक्रिय सहमहरू"),
    TOTAL_SAHAMS("Total Sahams", "कुल सहमहरू"),
    AVERAGE_STRENGTH("Average Strength", "औसत बल"),

    // ============================================
    // SEARCH & FILTER
    // ============================================
    SEARCH_SAHAMS("Search Sahams...", "सहमहरू खोज्नुहोस्..."),
    FILTER_ALL("All", "सबै"),
    FILTER_WEALTH("Wealth", "धन"),
    FILTER_HEALTH("Health", "स्वास्थ्य"),
    FILTER_RELATIONSHIPS("Relationships", "सम्बन्ध"),
    FILTER_CAREER("Career", "क्यारियर"),
    FILTER_SPIRITUAL("Spiritual", "आध्यात्मिक"),
    FILTER_OTHER("Other", "अन्य"),
    NO_SAHAMS_FOUND("No Sahams found matching criteria", "मापदण्ड अनुरूप कुनै सहम फेला परेन"),

    // ============================================
    // SAHAM CARD DETAILS
    // ============================================
    ACTIVE_BADGE("ACTIVE", "सक्रिय"),
    SIGN_LABEL("Sign", "राशि"),
    HOUSE_LABEL("House", "भाव"),
    LORD_LABEL("Lord", "स्वामी"),
    DEGREE_LABEL("Degree", "अंश"),
    NAKSHATRA_LABEL("Nakshatra", "नक्षत्र"),
    STRENGTH_LABEL("Strength", "बल"),
    FORMULA_LABEL("Formula", "सूत्र"),
    ANALYSIS_LABEL("Analysis", "विश्लेषण"),

    // ============================================
    // STRENGTH INDICATORS
    // ============================================
    STRENGTH_EXCELLENT("Excellent", "उत्कृष्ट"),
    STRENGTH_STRONG("Strong", "बलियो"),
    STRENGTH_MODERATE("Moderate", "मध्यम"),
    STRENGTH_WEAK("Weak", "कमजोर"),

    // ============================================
    // SAHAM NAMES (Traditional Arabic Parts)
    // ============================================
    SAHAM_FORTUNE("Part of Fortune", "भाग्य सहम"),
    SAHAM_SPIRIT("Part of Spirit", "आत्मा सहम"),
    SAHAM_LOVE("Part of Love", "प्रेम सहम"),
    SAHAM_MARRIAGE("Part of Marriage", "विवाह सहम"),
    SAHAM_CHILDREN("Part of Children", "सन्तान सहम"),
    SAHAM_FATHER("Part of Father", "पिता सहम"),
    SAHAM_MOTHER("Part of Mother", "माता सहम"),
    SAHAM_SIBLINGS("Part of Siblings", "भाइबहिनी सहम"),
    SAHAM_DEATH("Part of Death", "मृत्यु सहम"),
    SAHAM_ILLNESS("Part of Illness", "रोग सहम"),
    SAHAM_SURGERY("Part of Surgery", "शल्यक्रिया सहम"),
    SAHAM_TRAVEL("Part of Travel", "यात्रा सहम"),
    SAHAM_COMMERCE("Part of Commerce", "व्यापार सहम"),
    SAHAM_PROFESSION("Part of Profession", "पेशा सहम"),
    SAHAM_PROPERTY("Part of Property", "सम्पत्ति सहम"),
    SAHAM_INHERITANCE("Part of Inheritance", "विरासत सहम"),
    SAHAM_FAME("Part of Fame", "प्रसिद्धि सहम"),
    SAHAM_VICTORY("Part of Victory", "विजय सहम"),
    SAHAM_IMPRISONMENT("Part of Imprisonment", "बन्दी सहम"),
    SAHAM_ENEMIES("Part of Enemies", "शत्रु सहम"),
    SAHAM_FRIENDS("Part of Friends", "मित्र सहम"),
    SAHAM_HIDDEN("Part of Hidden Matters", "गोप्य विषय सहम"),
    SAHAM_KNOWLEDGE("Part of Knowledge", "ज्ञान सहम"),
    SAHAM_FAITH("Part of Faith", "धर्म सहम"),
    SAHAM_EROS("Part of Eros", "इरोस सहम"),
    SAHAM_NECESSITY("Part of Necessity", "आवश्यकता सहम"),
    SAHAM_COURAGE("Part of Courage", "साहस सहम"),
    SAHAM_SUDDEN_EVENTS("Part of Sudden Events", "आकस्मिक घटना सहम"),

    // ============================================
    // LIFE AREA CATEGORIES
    // ============================================
    CATEGORY_WEALTH("Wealth & Finance", "धन र वित्त"),
    CATEGORY_HEALTH("Health & Vitality", "स्वास्थ्य र जीवनशक्ति"),
    CATEGORY_RELATIONSHIPS("Love & Relationships", "प्रेम र सम्बन्ध"),
    CATEGORY_CAREER("Career & Profession", "क्यारियर र पेशा"),
    CATEGORY_SPIRITUAL("Spiritual & Religious", "आध्यात्मिक र धार्मिक"),
    CATEGORY_FAMILY("Family & Home", "परिवार र घर"),
    CATEGORY_OTHER("Other Areas", "अन्य क्षेत्रहरू"),

    // ============================================
    // INFO DIALOG
    // ============================================
    INFO_TITLE("About Sahams", "सहमहरूको बारेमा"),
    INFO_DESCRIPTION("Sahams (Arabic Parts) are sensitive points calculated from the positions of planets, Ascendant, and house cusps. They reveal specific life themes and their activation periods.", "सहमहरू (अरबी भाग) ग्रहको स्थिति, लग्न र भाव सन्धिबाट गणना गरिएको संवेदनशील बिन्दुहरू हुन्। तिनीहरूले विशिष्ट जीवन विषयहरू र तिनीहरूको सक्रियता अवधिहरू प्रकट गर्छन्।"),
    INFO_DAY_NIGHT("Day/Night Charts: The formula for many Sahams reverses between day and night births. Day births have the Sun above the horizon, night births have it below.", "दिवा/रात्रि कुण्डली: धेरै सहमहरूको सूत्र दिवा र रात्रि जन्ममा उल्टो हुन्छ। दिवा जन्ममा सूर्य क्षितिज माथि हुन्छ, रात्रि जन्ममा तल हुन्छ।"),
    INFO_ACTIVATION("Activation: A Saham is considered active when transiting planets conjunct or aspect it, or during relevant Dasha periods.", "सक्रियता: गोचर ग्रहले युति वा दृष्टि गर्दा, वा सान्दर्भिक दशा अवधिमा सहम सक्रिय मानिन्छ।"),
    INFO_FORMULA("Formula: Most Sahams follow the pattern: Asc + Planet A - Planet B. For night charts, planets A and B may swap positions.", "सूत्र: धेरैजसो सहमहरू यो ढाँचा पछ्याउँछन्: लग्न + ग्रह A - ग्रह B। रात्रि कुण्डलीमा, ग्रह A र B को स्थान बदलिन सक्छ।"),

    // ============================================
    // STATES
    // ============================================
    NO_CHART_SELECTED("No Chart Selected", "कुनै कुण्डली छानिएको छैन"),
    SELECT_CHART_MESSAGE("Please select a birth chart to view Sahams", "सहमहरू हेर्न कृपया जन्म कुण्डली छान्नुहोस्"),
    CALCULATING("Calculating Sahams...", "सहमहरू गणना गर्दै..."),
    CALCULATION_ERROR("Calculation Error", "गणना त्रुटि"),

    // ============================================
    // MISCELLANEOUS
    // ============================================
    SHOW_INFO("Show information", "जानकारी देखाउनुहोस्"),
    GOT_IT("Got it", "बुझें"),
    HOUSE_FORMAT("House %d", "भाव %d"),
    DEGREE_FORMAT("%s° %s'", "%s° %s'"),
    NEEDS_FOCUS("Needs Focus", "ध्यान दिनुपर्ने"),
    ACTIVATED("Activated", "सक्रिय"),
    FORMULA_ADJUSTED_DESC("Formulas adjusted per Tajika tradition", "ताजिक परम्परा अनुसार सूत्रहरू समायोजित"),
    STRONGEST("Strongest", "सबैभन्दा बलियो"),
    RECOMMENDATIONS("Recommendations", "सिफारिसहरू"),
    NO_SAHAMS_TYPE_FOUND("No %s Sahams found", "कुनै %s सहम फेला परेन"),
;
}
