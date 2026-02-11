package com.astro.storm.core.common

/**
 * String keys for Muhurta and Panchaka analysis
 */
enum class StringKeyMuhurta(override val en: String, override val ne: String, override val hi: String) : StringKeyInterface {
    PANCHAKA_TITLE("Panchaka", "पञ्चक", "पञ्चक"),
    PANCHAKA_ACTIVE("%s Active", "%s सक्रिय", "%s सक्रिय"),
    PANCHAKA_NONE("No Panchaka Dosha", "पञ्चक दोष छैन", "पञ्चक दोष छैन"),
    PANCHAKA_SEVERITY("Severity: %s", "गम्भीरता: %s", "गम्भीरता: %s"),
    PANCHAKA_AVOID("Avoid: %s...", "बच्नुहोस्: %s...", "बच्नुहोस्: %s..."),
    PANCHAKA_FAVORABLE("Time is generally favorable.", "समय सामान्यतया अनुकूल छ।", "समय सामान्यतया अनुकूल है।"),
    
    // Panchaka Types
    PANCHAKA_MRITYU("Death Panchaka", "मृत्यु पञ्चक", "मृत्यु पञ्चक"),
    PANCHAKA_AGNI("Fire Panchaka", "अग्नि पञ्चक", "अग्नि पञ्चक"),
    PANCHAKA_RAJA("King/Authority Panchaka", "राज पञ्चक", "राज पञ्चक"),
    PANCHAKA_CHORA("Thief Panchaka", "चोर पञ्चक", "चोर पञ्चक"),
    PANCHAKA_ROGA("Disease Panchaka", "रोग पञ्चक", "रोग पञ्चक"),
    
    // Severities
    SEVERITY_EXTREME("Extremely Severe - Avoid all important activities", "अत्यन्त गम्भीर - सबै महत्त्वपूर्ण गतिविधिहरूबाट बच्नुहोस्", "अत्यन्त गम्भीर - सबै महत्त्वपूर्ण गतिविधिहरूबाट बच्नुहोस्"),
    SEVERITY_SEVERE("Severe - Exercise extreme caution", "गम्भीर - अत्यधिक सावधानी अपनाउनुहोस्", "गम्भीर - अत्यधिक सावधानी अपनाउनुहोस्"),
    SEVERITY_MODERATE("Moderate - Proceed with caution", "मध्यम - सावधानीका साथ अगाडि बढ्नुहोस्", "मध्यम - सावधानीका साथ अगाडि बढ्नुहोस्"),
    SEVERITY_MILD("Mild - Minor obstacles possible", "सामान्य - साना बाधाहरू सम्भव", "सामान्य - साना बाधाहरू सम्भव"),
    SEVERITY_VERY_MILD("Very Mild - Usually negligible", "अति सामान्य - सामान्यतया नगन्य", "अति सामान्य - सामान्यतया नगन्य"),
    SEVERITY_NONE("None - No dosha present", "छैन - कुनै दोष छैन", "छैन - कुनै दोष छैन"),

    // Directions
    DIR_EAST("East", "पूर्व", "पूर्व"),
    DIR_SOUTH("South", "दक्षिण", "दक्षिण"),
    DIR_WEST("West", "पश्चिम", "पश्चिम"),
    DIR_NORTH("North", "उत्तर", "उत्तर"),

    // Status
    SAFE("Safe - No Panchaka", "सुरक्षित - पञ्चक छैन", "सुरक्षित - पञ्चक छैन"),
    RISK_LOW("Low Risk - Minor Panchaka", "कम जोखिम - सामान्य पञ्चक", "कम जोखिम - सामान्य पञ्चक"),
    RISK_MODERATE("Moderate Risk", "मध्यम जोखिम", "मध्यम जोखिम"),
    RISK_HIGH("High Risk", "उच्च जोखिम", "उच्च जोखिम"),
    RISK_SEVERE("Severe - Full Panchaka", "गम्भीर - पूर्ण पञ्चक", "गम्भीर - पूर्ण पञ्चक"),

    // Messages
    SEARCH_EMPTY_TITLE("Search Muhurta", "मुहूर्त खोज्नुहोस्", "मुहूर्त खोजें"),
    SEARCH_EMPTY_DESC("Select an activity and date range to find auspicious times.", "शुभ समय फेला पार्न एक गतिविधि र मिति दायरा चयन गर्नुहोस्।", "शुभ समय फेला पार्न एक गतिविधि और मिति दायरा चयन करें।"),
    NO_RESULTS_TITLE("No Results Found", "कुनै नतिजा फेला परेन", "कुनै नतिजा फेला परेन"),
    NO_RESULTS_DESC("Try a wider date range or different activity.", "फराकिलो मिति दायरा वा फरक गतिविधि प्रयास गर्नुहोस्।", "फराकिलो मिति दायरा या फरक गतिविधि प्रयास करें।"),
}
