package com.astro.storm.core.common

/**
 * Common UI string keys for general use across the app
 */
enum class StringKeyUICommon(override val en: String, override val ne: String) : StringKeyInterface {
    // Connectors & Punctuation
    AND("and", "र"),
    IN("in", "मा"),
    OR("or", "वा"),
    WITH("with", "साथ"),
    FOR("for", "को लागि"),
    BY("by", "द्वारा"),
    FROM("from", "बाट"),
    TO("to", "देखि"),
    COLON(":", ":"),
    DOT(".", "।"),
    BULLET("•", "•"),
    COMMA(",", ","),

    // Common UI Labels
    BACK("Back", "पछाडि"),
    INFO("Info", "जानकारी"),
    REFRESH("Refresh", "पुनः ताजा"),
    COLLAPSE("Collapse", "संक्षिप्त"),
    EXPAND("Expand", "विस्तार"),
    VARGOTTAMA("Vargottama", "वर्गोत्तम"),
    CLOSE("Close", "बन्द गर्नुहोस्"),
    OK("OK", "ठीक छ"),
    CANCEL("Cancel", "रद्द गर्नुहोस्"),
    YES("Yes", "हो"),
    NO("No", "होइन"),
    YES_MANGLIK("Yes (Manglik)", "हो (मांगलिक)"),
    PRESENT("Present", "उपस्थित"),
    ABSENT("Absent", "अनुपस्थित"),
    HOUSE("House", "भाव"),
    PLANET("Planet", "ग्रह"),
    SIGN("Sign", "राशि"),
    DEGREE("Degree", "अंश"),
    PADA("Pada", "पद"),
    STRENGTH("Strength", "बल"),
    STATUS("Status", "स्थिति"),
    DETAILS("Details", "विवरण"),
    SUMMARY("Summary", "सारांश"),
    OVERVIEW("Overview", "अवलोकन"),
    INTERPRETATION("Interpretation", "व्याख्या"),
    VALUE("Value", "मान"),
    REMEDIES("Remedies", "उपाय"),
    TIMING("Timing", "समय"),
    EFFECTS("Effects", "प्रभाव"),
    CHARACTERISTICS("Characteristics", "विशेषताहरू"),

    // Strength Levels
    STRENGTH_EXCELLENT("Excellent", "उत्कृष्ट"),
    STRENGTH_VERY_STRONG("Very Strong", "धेरै बलियो"),
    STRENGTH_STRONG("Strong", "बलियो"),
    STRENGTH_MODERATE("Moderate", "मध्यम"),
    STRENGTH_WEAK("Weak", "कमजोर"),
    STRENGTH_EXTREMELY_STRONG("Extremely Strong", "अत्यधिक बलियो"),
    STRENGTH_AFFLICTED("Afflicted", "पीडित"),

    // Dignities
    EXALTED("Exalted", "उच्च"),
    DEBILITATED("Debilitated", "नीच"),
    OWN_SIGN("Own Sign", "स्वगृह"),
    MOOLATRIKONA("Moolatrikona", "मूलत्रिकोण"),
    FRIEND_SIGN("Friend's Sign", "मित्र राशि"),
    NEUTRAL_SIGN("Neutral Sign", "तटस्थ राशि"),
    ENEMY_SIGN("Enemy's Sign", "शत्रु राशि"),

    // Generic Messages
    LOADING("Loading...", "लोड हुँदैछ..."),
    CALCULATING("Calculating...", "गणना गर्दै..."),
    ANALYZING("Analyzing...", "विश्लेषण गर्दै..."),
    PLEASE_WAIT("Please wait", "कृपया पर्खनुहोस्"),
    ERROR_OCCURRED("An error occurred", "एउटा त्रुटि भयो"),
    NO_DATA("No data available", "कुनै डाटा उपलब्ध छैन"),

    // Timezone Selector
    SELECT_TIMEZONE("Select Timezone", "समय क्षेत्र छान्नुहोस्"),
    SEARCH_TIMEZONE("Search timezone", "समय क्षेत्र खोज्नुहोस्"),
    ALL_TIMEZONES("All Timezones", "सबै समय क्षेत्रहरू"),
    COMMON_TIMEZONES("Common Timezones", "साझा समय क्षेत्रहरू"),
    NO_TIMEZONES_FOUND("No timezones found", "कुनै समय क्षेत्र फेला परेन"),
    CLEAR_SEARCH("Clear search", "खोज हटाउनुहोस्"),
    CHANGE_TIMEZONE("Change timezone", "समय क्षेत्र परिवर्तन गर्नुहोस्"),
    NAV_BACK("Back", "पछाडि"),

    // Agentic UI Status
    TASKS("Tasks", "कार्यहरू"),
    COMPLETED("completed", "पूरा भयो"),
    RUNNING("running", "चल्दैछ"),
    FAILED("failed", "असफल भयो"),
    TOOL_PLURAL("tools", "उपकरणहरू"),
    TOOL_SINGULAR("tool", "उपकरण"),
    DURATION_MS("ms", "मि.से."),
    DURATION_S("s", "से."),
    DURATION_M("m", "मि."),
}
