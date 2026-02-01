package com.astro.storm.core.common

/**
 * String keys for Chat & AI UI elements
 * Includes: Stormy AI Assistant, Chat UI, AI Models, Agentic UI
 */
enum class StringKeyDoshaChat(
    override val en: String,
    override val ne: String,
) : StringKeyInterface {

    // ============================================
    // CHAT & STORMY AI STRINGS
    // ============================================
    CHAT_NEW("New Chat", "नयाँ च्याट"),
    CHAT_DELETE("Delete Chat", "च्याट मेटाउनुहोस्"),
    CHAT_DELETE_CONFIRM("Are you sure you want to delete \"%s\"? This cannot be undone.", "के तपाईं \"%s\" मेटाउन निश्चित हुनुहुन्छ? यो पूर्ववत गर्न सकिँदैन।"),
    CHAT_DELETE_BTN("Delete", "मेटाउनुहोस्"),
    CHAT_CANCEL_BTN("Cancel", "रद्द गर्नुहोस्"),
    CHAT_ARCHIVE("Archive", "संग्रह"),
    CHAT_MORE_OPTIONS("More options", "थप विकल्पहरू"),
    CHAT_MESSAGES_COUNT("%d messages", "%d सन्देशहरू"),
    CHAT_JUST_NOW("Just now", "अहिले भर्खरै"),
    CHAT_MINUTES_AGO("%dm ago", "%dमि. पहिले"),
    CHAT_HOURS_AGO("%dh ago", "%d घण्टा पहिले"),
    CHAT_DAYS_AGO("%dd ago", "%d दिन पहिले"),

    // Stormy AI Welcome
    STORMY_TITLE("Stormy", "स्टर्मी"),
    STORMY_MEET("Meet Stormy", "स्टर्मीलाई भेट्नुहोस्"),
    STORMY_SUBTITLE("Your Vedic astrology AI assistant", "तपाईंको वैदिक ज्योतिष एआई सहायक"),
    STORMY_INTRO("Ask about your birth chart, planetary periods, transits, compatibility, remedies, and more.", "आफ्नो जन्म कुण्डली, ग्रह अवधि, गोचर, अनुकूलता, उपाय र थप बारेमा सोध्नुहोस्।"),
    STORMY_HELLO("Hello! I'm Stormy", "नमस्ते! म स्टर्मी हुँ"),
    STORMY_HELLO_DESC("Your Vedic astrology assistant. Ask me anything about your birth chart, planetary periods, transits, compatibility, or remedies.", "तपाईंको वैदिक ज्योतिष सहायक। आफ्नो जन्म कुण्डली, ग्रह अवधि, गोचर, अनुकूलता, वा उपायहरूको बारेमा मलाई केही पनि सोध्नुहोस्।"),
    STORMY_START_CHAT("Start New Chat", "नयाँ च्याट सुरु गर्नुहोस्"),
    STORMY_CONFIGURE_MODELS("Configure AI Models", "एआई मोडेलहरू कन्फिगर गर्नुहोस्"),
    STORMY_ENABLE_MODELS("Enable AI models to start chatting", "च्याट सुरु गर्न एआई मोडेलहरू सक्षम गर्नुहोस्"),
    STORMY_ASK_PLACEHOLDER("Ask Stormy...", "स्टर्मीलाई सोध्नुहोस्..."),

    // Stormy Status Messages
    STORMY_THINKING("Stormy is thinking...", "स्टर्मीले सोच्दैछ..."),
    STORMY_REASONING("Stormy is reasoning...", "स्टर्मीले तर्क गर्दैछ..."),
    STORMY_TYPING("Stormy is typing...", "स्टर्मीले टाइप गर्दैछ..."),
    STORMY_ANALYZING("Stormy is analyzing...", "स्टर्मीले विश्लेषण गर्दैछ..."),
    STORMY_ANALYZING_COMPATIBILITY("Stormy is analyzing compatibility...", "स्टर्मीले अनुकूलता विश्लेषण गर्दैछ..."),
    STORMY_CALLING_TOOL("Calling %s...", "%s कल गर्दैछ..."),
    STORMY_USING_TOOLS("Using tools: %s", "उपकरणहरू प्रयोग गर्दैछ: %s"),
    STORMY_USING_TOOL("Using %s...", "%s प्रयोग गर्दैछ..."),
    STORMY_REASONING_VEDIC("Reasoning through Vedic principles...", "वैदिक सिद्धान्तहरूमा तर्क गर्दैछ..."),
    STORMY_GATHERING_DATA("Gathering astrological data...", "ज्योतिषीय डाटा संकलन गर्दैछ..."),
    STORMY_COMPOSING("Composing response...", "प्रतिक्रिया रचना गर्दैछ..."),
    STORMY_APPLYING_VEDIC("Applying Vedic principles...", "वैदिक सिद्धान्तहरू लागू गर्दैछ..."),
    STORMY_PROCESSING("Processing...", "प्रशोधन गर्दैछ..."),
    STORMY_NEEDS_INPUT("Stormy needs your input", "स्टर्मीलाई तपाईंको इनपुट चाहिन्छ"),

    // Agentic Section UI Strings
    SECTION_REASONING("Reasoning", "तर्क"),
    SECTION_ASTROLOGICAL_TOOLS("Astrological Tools", "ज्योतिषीय उपकरणहरू"),
    SECTION_TASK_STARTED("Started: %s", "सुरु भयो: %s"),
    SECTION_TASK_COMPLETED("Completed: %s", "सम्पन्न भयो: %s"),
    SECTION_REASONED_FOR("Reasoned for %s", "%s को लागि तर्क गरियो"),
    SECTION_ANALYZING("Analyzing...", "विश्लेषण गर्दैछ..."),
    SECTION_TAP_TO_VIEW("Tap to view", "हेर्न ट्याप गर्नुहोस्"),
    SECTION_TAP_TO_VIEW_REASONING("Tap to view reasoning", "तर्क हेर्न ट्याप गर्नुहोस्"),
    SECTION_VEDIC_ANALYSIS("Vedic analysis process", "वैदिक विश्लेषण प्रक्रिया"),
    SECTION_COLLAPSE("Collapse", "संकुचित गर्नुहोस्"),
    SECTION_EXPAND("Expand", "विस्तार गर्नुहोस्"),

    // Tool Execution Status
    TOOL_STATUS_PENDING("Pending", "पर्खिएको"),
    TOOL_STATUS_RUNNING("Running...", "चलिरहेको..."),
    TOOL_STATUS_COMPLETED_IN("Completed in %s", "%s मा सम्पन्न"),
    TOOL_STATUS_FAILED("Failed", "असफल"),
    TOOLS_USED_LABEL("Used: %s", "प्रयोग गरियो: %s"),
    TOOLS_MORE_COUNT(" +%d more", " +%d थप"),

    // Todo List
    TODO_IN_PROGRESS("In Progress", "प्रगतिमा"),

    // Ask User Placeholder
    PLACEHOLDER_TYPE_RESPONSE("Type your response...", "तपाईंको प्रतिक्रिया टाइप गर्नुहोस्..."),
    RESPONSE_SUBMITTED("Response submitted", "प्रतिक्रिया पेश गरियो"),

    // Profile Operation Status
    PROFILE_STATUS_PENDING("Pending", "पर्खिएको"),
    PROFILE_STATUS_IN_PROGRESS("In Progress", "प्रगतिमा"),
    PROFILE_STATUS_SUCCESS("Success", "सफल"),
    PROFILE_STATUS_FAILED("Failed", "असफल"),

    // Profile Operation Types
    PROFILE_OP_CREATING("Creating Profile", "प्रोफाइल बनाउँदै"),
    PROFILE_OP_UPDATING("Updating Profile", "प्रोफाइल अपडेट गर्दै"),
    PROFILE_OP_DELETING("Deleting Profile", "प्रोफाइल हटाउँदै"),
    PROFILE_OP_VIEWING("Viewing Profile", "प्रोफाइल हेर्दै"),
    PROFILE_ID_LABEL("ID: %s", "आईडी: %s"),

    // AI Generation Status
    AI_GENERATING("Generating...", "उत्पन्न गर्दैछ..."),
    AI_ANALYZING_QUESTION("Analyzing your question...", "तपाईंको प्रश्न विश्लेषण गर्दै..."),

    // Chat Suggestions
    CHAT_SUGGESTION_DASHA("What's my current dasha period?", "मेरो हालको दशा अवधि के हो?"),
    CHAT_SUGGESTION_CHART("Analyze my birth chart", "मेरो जन्म कुण्डली विश्लेषण गर्नुहोस्"),
    CHAT_SUGGESTION_YOGAS("What yogas are present in my chart?", "मेरो कुण्डलीमा कुन योगहरू छन्?"),

    // Chat Actions
    CHAT_CLEAR("Clear Chat", "च्याट खाली गर्नुहोस्"),
    CHAT_CLEAR_CONFIRM("Are you sure you want to clear all messages in this conversation?", "के तपाईं यस कुराकानीका सबै सन्देशहरू हटाउन निश्चित हुनुहुन्छ?"),
    CHAT_CLEAR_BTN("Clear", "खाली गर्नुहोस्"),
    CHAT_STOP("Stop", "रोक्नुहोस्"),
    CHAT_SEND("Send", "पठाउनुहोस्"),
    CHAT_BACK("Back", "पछाडि"),
    CHAT_CHANGE_MODEL("Change model", "मोडेल परिवर्तन गर्नुहोस्"),
    CHAT_MODEL_OPTIONS("Model options", "मोडेल विकल्पहरू"),

    // Model Options
    MODEL_OPTIONS_TITLE("Model Options", "मोडेल विकल्पहरू"),
    MODEL_THINKING_MODE("Thinking Mode", "सोच्ने मोड"),
    MODEL_THINKING_DESC("Extended reasoning before answering", "जवाफ दिनु अघि विस्तारित तर्क"),
    MODEL_WEB_SEARCH("Web Search", "वेब खोज"),
    MODEL_WEB_SEARCH_DESC("Search the web for current information", "वर्तमान जानकारीको लागि वेब खोज्नुहोस्"),
    MODEL_DONE("Done", "सम्पन्न"),

    // Model Selector
    MODEL_SELECT_TITLE("Select AI Model", "एआई मोडेल छान्नुहोस्"),
    MODEL_AI_LABEL("AI Model", "एआई मोडेल"),
    MODEL_SELECT_PROMPT("Select a model", "मोडेल छान्नुहोस्"),
    MODEL_NONE_AVAILABLE("No models available", "कुनै मोडेल उपलब्ध छैन"),
    MODEL_CONFIGURE("Configure Models", "मोडेलहरू कन्फिगर गर्नुहोस्"),
    MODEL_MANAGE("Manage AI Models", "एआई मोडेलहरू व्यवस्थापन गर्नुहोस्"),

    // ============================================
    // AI MODELS SCREEN
    // ============================================
    AI_MODELS_TITLE("AI Models", "AI मोडेलहरू"),
    AI_MODELS_ENABLED_COUNT("%d models enabled", "%d मोडेलहरू सक्षम"),
    AI_MODELS_BACK("Back", "पछाडि"),
    AI_MODELS_REFRESH("Refresh models", "मोडेलहरू रिफ्रेस गर्नुहोस्"),
    AI_MODELS_FREE_TITLE("Free AI Models", "निःशुल्क AI मोडेलहरू"),
    AI_MODELS_FREE_DESC("These models are provided by free API providers and don't require any API keys. Model availability may vary.", "यी मोडेलहरू निःशुल्क API प्रदायकहरूबाट प्रदान गरिएका हुन् र कुनै API कुञ्जी आवश्यक पर्दैन। मोडेल उपलब्धता फरक हुन सक्छ।"),
    AI_MODELS_DEFAULT("Default Model", "पूर्वनिर्धारित मोडेल"),
    AI_MODELS_NOT_SET("Not set", "सेट गरिएको छैन"),
    AI_MODELS_SELECT_DEFAULT("Select Default Model", "पूर्वनिर्धारित मोडेल छान्नुहोस्"),
    AI_MODELS_CANCEL("Cancel", "रद्द गर्नुहोस्"),
    AI_MODELS_MODELS_ENABLED("%d/%d models enabled", "%d/%d मोडेलहरू सक्षम"),
    AI_MODELS_ENABLE_ALL("Enable All", "सबै सक्षम गर्नुहोस्"),
    AI_MODELS_DISABLE_ALL("Disable All", "सबै असक्षम गर्नुहोस्"),
    AI_MODELS_TOOLS("Tools", "उपकरणहरू"),
    AI_MODELS_REASONING("Reasoning", "तर्क"),
    AI_MODELS_VISION("Vision", "दृष्टि"),
    AI_MODELS_NONE("No Models Available", "कुनै मोडेल उपलब्ध छैन"),
    AI_MODELS_NONE_DESC("Unable to fetch AI models. Check your internet connection and try again.", "AI मोडेलहरू प्राप्त गर्न असमर्थ। आफ्नो इन्टरनेट जडान जाँच गर्नुहोस् र पुन: प्रयास गर्नुहोस्।"),
    AI_MODELS_RETRY("Retry", "पुन: प्रयास गर्नुहोस्"),

    // AI Providers
    AI_PROVIDER_DEEPINFRA("DeepInfra", "डीपइन्फ्रा"),
    AI_PROVIDER_QWEN("Qwen", "क्वेन"),
    AI_PROVIDER_BLACKBOX("Blackbox AI", "ब्ल्याकबक्स AI"),
    AI_PROVIDER_DDG("DuckDuckGo AI", "डकडकगो AI"),
;
}
