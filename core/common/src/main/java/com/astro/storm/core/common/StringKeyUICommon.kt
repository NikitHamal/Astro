package com.astro.storm.core.common

/**
 * Common UI string keys for general use across the app
 */
enum class StringKeyUICommon(override val en: String, override val ne: String, override val hi: String) : StringKeyInterface {
    // Connectors & Punctuation
    AND("and", "र", "र"),
    IN("in", "मा", "मा"),
    OR("or", "वा", "वा"),
    WITH("with", "साथ", "साथ"),
    FOR("for", "को लागि", "को लिए"),
    BY("by", "द्वारा", "द्वारा"),
    FROM("from", "बाट", "बाट"),
    TO("to", "देखि", "देखि"),
    COLON(":", ":", ":"),
    DOT(".", "।", "।"),
    BULLET("•", "•", "•"),
    COMMA(",", ",", ","),

    // Common UI Labels
    BACK("Back", "पछाडि", "पीछे"),
    INFO("Info", "जानकारी", "जानकारी"),
    REFRESH("Refresh", "पुनः ताजा", "रिफ्रेश"),
    COLLAPSE("Collapse", "संक्षिप्त", "संक्षिप्त"),
    EXPAND("Expand", "विस्तार", "विस्तार"),
    VARGOTTAMA("Vargottama", "वर्गोत्तम", "वर्गोत्तम"),
    CLOSE("Close", "बन्द गर्नुहोस्", "बंद करें"),
    OK("OK", "ठीक छ", "ठीक है"),
    CANCEL("Cancel", "रद्द गर्नुहोस्", "रद्द करें"),
    YES("Yes", "हो", "हो"),
    NO("No", "होइन", "होइन"),
    YES_MANGLIK("Yes (Manglik)", "हो (मांगलिक)", "हो (मांगलिक)"),
    PRESENT("Present", "उपस्थित", "उपस्थित"),
    ABSENT("Absent", "अनुपस्थित", "अनुपस्थित"),
    HOUSE("House", "भाव", "भाव"),
    PLANET("Planet", "ग्रह", "ग्रह"),
    SIGN("Sign", "राशि", "राशि"),
    DEGREE("Degree", "अंश", "अंश"),
    PADA("Pada", "पद", "पद"),
    STRENGTH("Strength", "बल", "बल"),
    STATUS("Status", "स्थिति", "स्थिति"),
    DETAILS("Details", "विवरण", "विवरण"),
    SUMMARY("Summary", "सारांश", "सारांश"),
    OVERVIEW("Overview", "अवलोकन", "अवलोकन"),
    INTERPRETATION("Interpretation", "व्याख्या", "व्याख्या"),
    VALUE("Value", "मान", "मान"),
    REMEDIES("Remedies", "उपाय", "उपाय"),
    TIMING("Timing", "समय", "समय"),
    EFFECTS("Effects", "प्रभाव", "प्रभाव"),
    CHARACTERISTICS("Characteristics", "विशेषताहरू", "विशेषताहरू"),

    // Strength Levels
    STRENGTH_EXCELLENT("Excellent", "उत्कृष्ट", "उत्कृष्ट"),
    STRENGTH_VERY_STRONG("Very Strong", "धेरै बलियो", "धेरै बलियो"),
    STRENGTH_STRONG("Strong", "बलियो", "बलियो"),
    STRENGTH_MODERATE("Moderate", "मध्यम", "मध्यम"),
    STRENGTH_WEAK("Weak", "कमजोर", "कमजोर"),
    STRENGTH_EXTREMELY_STRONG("Extremely Strong", "अत्यधिक बलियो", "अत्यधिक बलियो"),
    STRENGTH_AFFLICTED("Afflicted", "पीडित", "पीडित"),

    // Dignities
    EXALTED("Exalted", "उच्च", "उच्च"),
    DEBILITATED("Debilitated", "नीच", "नीच"),
    OWN_SIGN("Own Sign", "स्वगृह", "स्वगृह"),
    MOOLATRIKONA("Moolatrikona", "मूलत्रिकोण", "मूलत्रिकोण"),
    FRIEND_SIGN("Friend's Sign", "मित्र राशि", "मित्र राशि"),
    NEUTRAL_SIGN("Neutral Sign", "तटस्थ राशि", "तटस्थ राशि"),
    ENEMY_SIGN("Enemy's Sign", "शत्रु राशि", "शत्रु राशि"),

    // Generic Messages
    LOADING("Loading...", "लोड हुँदैछ...", "लोड हुँदैछ..."),
    CALCULATING("Calculating...", "गणना गर्दै...", "गणना गर्दै..."),
    ANALYZING("Analyzing...", "विश्लेषण गर्दै...", "विश्लेषण गर्दै..."),
    PLEASE_WAIT("Please wait", "कृपया पर्खनुहोस्", "कृपया पर्खनुहोस्"),
    ERROR_OCCURRED("An error occurred", "एउटा त्रुटि भयो", "एउटा त्रुटि भयो"),
    NO_DATA("No data available", "कुनै डाटा उपलब्ध छैन", "कुनै डाटा उपलब्ध छैन"),

    // Timezone Selector
    SELECT_TIMEZONE("Select Timezone", "समय क्षेत्र छान्नुहोस्", "समय क्षेत्र चुनें"),
    SEARCH_TIMEZONE("Search timezone", "समय क्षेत्र खोज्नुहोस्", "समय क्षेत्र खोजें"),
    ALL_TIMEZONES("All Timezones", "सबै समय क्षेत्रहरू", "सबै समय क्षेत्रहरू"),
    COMMON_TIMEZONES("Common Timezones", "साझा समय क्षेत्रहरू", "साझा समय क्षेत्रहरू"),
    NO_TIMEZONES_FOUND("No timezones found", "कुनै समय क्षेत्र फेला परेन", "कुनै समय क्षेत्र फेला परेन"),
    CLEAR_SEARCH("Clear search", "खोज हटाउनुहोस्", "खोज हटाउनुहोस्"),
    CHANGE_TIMEZONE("Change timezone", "समय क्षेत्र परिवर्तन गर्नुहोस्", "समय क्षेत्र परिवर्तन करें"),
    NAV_BACK("Back", "पछाडि", "पीछे"),

    // Agentic UI Status
    TASKS("Tasks", "कार्यहरू", "कार्यहरू"),
    COMPLETED("completed", "पूरा भयो", "पूरा भयो"),
    RUNNING("running", "चल्दैछ", "चल्दैछ"),
    FAILED("failed", "असफल भयो", "असफल भयो"),
    TOOL_PLURAL("tools", "उपकरणहरू", "उपकरणहरू"),
    TOOL_SINGULAR("tool", "उपकरण", "उपकरण"),
    DURATION_MS("ms", "मि.से.", "मि.से."),
    DURATION_S("s", "से.", "से."),
    DURATION_M("m", "मि.", "मि."),
}
