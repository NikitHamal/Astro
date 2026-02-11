package com.astro.storm.core.common

/**
 * String keys for AI Agent and Tool interactions
 */
enum class StringKeyAgent(override val en: String, override val ne: String, override val hi: String) : StringKeyInterface {
    AGENT_NAME("Stormy", "स्टोर्मी", "स्टोर्मी"),
    ,
    AGENT_DESCRIPTION("Your Vedic Astrology AI Assistant", "तपाईंको वैदिक ज्योतिष AI सहायक", "आप के वैदिक ज्यहतिष AI सहायक"),

    ,
    
    // Tool Names & Headers
    TOOL_GET_PLANET_POSITIONS("Get Planet Positions", "ग्रह स्थिति प्राप्त गर्नुहोस्", "ग्रह स्थिति प्राप्त करें"),
    ,
    TOOL_GET_HOUSE_POSITIONS("Get House Positions", "भाव स्थिति प्राप्त गर्नुहोस्", "भाव स्थिति प्राप्त करें"),
    ,
    TOOL_GET_NAKSHATRA_INFO("Get Nakshatra Info", "नक्षत्र जानकारी प्राप्त गर्नुहोस्", "नक्षत्र जानकारी प्राप्त करें"),
    ,
    TOOL_GET_DASHA_INFO("Get Dasha Info", "दशा जानकारी प्राप्त गर्नुहोस्", "दशा जानकारी प्राप्त करें"),
    ,
    TOOL_GET_CURRENT_DASHA("Get Current Dasha", "हालको दशा प्राप्त गर्नुहोस्", "हाल के दशा प्राप्त करें"),
    ,
    TOOL_GET_YOGAS("Get Yogas", "योगहरू प्राप्त गर्नुहोस्", "यहग प्राप्त करें"),
    ,
    TOOL_GET_ASHTAKAVARGA("Get Ashtakavarga", "अष्टकवर्ग प्राप्त गर्नुहोस्", "अष्टकवर्ग प्राप्त करें"),
    ,
    TOOL_GET_TRANSITS("Get Transits", "गोचरहरू प्राप्त गर्नुहोस्", "गोचर प्राप्त करें"),
    ,
    TOOL_GET_REMEDIES("Get Remedies", "उपायहरू प्राप्त गर्नुहोस्", "उपाय प्राप्त करें"),
    ,
    TOOL_GET_STRENGTH_ANALYSIS("Get Strength Analysis", "बल विश्लेषण प्राप्त गर्नुहोस्", "बल विश्लेषण प्राप्त करें"),
    ,
    TOOL_GET_DIVISIONAL_CHART("Get Divisional Chart", "विभागीय कुण्डली प्राप्त गर्नुहोस्", "विभागीय कुंडली प्राप्त करें"),
    ,
    TOOL_CALCULATE_MUHURTA("Calculate Muhurta", "मुहूर्त गणना गर्नुहोस्", "मुहूर्त गणना करें"),
    ,
    TOOL_GET_BHRIGU_BINDU("Get Bhrigu Bindu", "भृगु बिन्दु प्राप्त गर्नुहोस्", "भृगु बिन्दु प्राप्त करें"),
    ,
    TOOL_GET_ARGALA("Get Argala Analysis", "अर्गला विश्लेषण प्राप्त गर्नुहोस्", "अर्गला विश्लेषण प्राप्त करें"),
    ,
    TOOL_GET_PRASHNA_ANALYSIS("Get Prashna Analysis", "प्रश्न विश्लेषण प्राप्त गर्नुहोस्", "प्रश्न विश्लेषण प्राप्त करें"),

    ,
    
    // Status Messages
    STATUS_THINKING("Stormy is thinking...", "स्टोर्मी सोच्दै छ...", "स्टोर्मी सोच्दै है..."),
    ,
    STATUS_CALLING_TOOL("Using %s...", "%s प्रयोग गर्दै...", "%s प्रयहग गर्दै..."),
    ,
    STATUS_TOOL_SUCCESS("Tool execution successful", "उपकरण कार्यान्वयन सफल", "उपकरण कार्यान्वयन सफल"),
    ,
    STATUS_TOOL_ERROR("Failed to execute tool", "उपकरण कार्यान्वयन असफल", "उपकरण कार्यान्वयन असफल"),

    ,
    
    // Chat UI
    NEW_CHAT("New Chat", "नयाँ च्याट", "नयाँ च्याट"),
    ,
    CHAT_CONTEXT_ERROR("No conversation context", "च्याट सन्दर्भ छैन", "च्याट सन्दर्भ हैैन"),
    ,
    CHAT_NO_MODEL("No AI model available. Please configure models in settings.", "कुनै AI मोडेल उपलब्ध छैन। कृपया सेटिङ्समा मोडेलहरू कन्फिगर गर्नुहोस्।", "कुनै AI मोडेल उपलब्ध हैैन। कृपया सेटिङ्स में मोडेल कन्फिगर करें।"),
    ,
    RESPONSE_CANCELLED("Response cancelled", "प्रतिक्रिया रद्द गरियो", "प्रतिक्रिया रद्द गरियह"),

    ,
    
    // Agent Sections
    SECTION_REASONING("Reasoning", "तर्क", "तर्क"),
    ,
    SECTION_CONTENT("Content", "सामग्री", "सामग्री"),
    ,
    SECTION_TOOLS("Tools Used", "प्रयोग गरिएका उपकरणहरू", "प्रयहग गरिए के उपकरण"),
    ,
    SECTION_TASKS("Tasks", "कार्यहरू", "कार्य"),

    ,
    
    // User Interaction Tools
    ASK_USER_TITLE("Stormy asks:", "स्टोर्मी सोध्छ:", "स्टोर्मी सोध्है:"),
    ,
    OPTION_LABEL("Option %d", "विकल्प %d", "विकल्प %d"),

    ,
    
    // Profile Tools
    PROFILE_CREATED("Profile created successfully", "प्रोफाइल सफलतापूर्वक बनाइयो", "प्रोफाइल सफलतापूर्वक बनाइयह"),
    ,
    PROFILE_UPDATED("Profile updated successfully", "प्रोफाइल सफलतापूर्वक अपडेट गरियो", "प्रोफाइल सफलतापूर्वक अपडेट गरियह"),
    ,
    PROFILE_DELETED("Profile deleted successfully", "प्रोफाइल सफलतापूर्वक मेटाइयो", "प्रोफाइल सफलतापूर्वक मेटाइयह"),
    ,
    PROFILE_ACTIVATED("Profile activated successfully", "प्रोफाइल सफलतापूर्वक सक्रिय गरियो", "प्रोफाइल सफलतापूर्वक सक्रिय गरियह"),
,
}
