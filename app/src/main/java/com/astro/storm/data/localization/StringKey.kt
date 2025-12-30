package com.astro.storm.data.localization



/**
 * Core UI string keys (navigation, settings, buttons, yoga, profile)
 * Part 1 of 4 split enums to avoid JVM method size limit
 */
enum class StringKey(override val en: String, override val ne: String) : StringKeyInterface {

    // ============================================
    // NAVIGATION & TABS
    // ============================================
    TAB_HOME("Home", "à¤—à¥ƒà¤¹"),
    TAB_INSIGHTS("Insights", "à¤…à¤¨à¥à¤¤à¤°à¥à¤¦à¥ƒà¤·à¥à¤Ÿà¤¿"),
    TAB_CHAT("Chat", "à¤šà¥à¤¯à¤¾à¤Ÿ"),
    TAB_SETTINGS("Settings", "à¤¸à¥‡à¤Ÿà¤¿à¤™à¥à¤¸"),

    // ============================================
    // HOME TAB - SECTION HEADERS
    // ============================================
    HOME_CHART_ANALYSIS("Chart Analysis", "à¤•à¥à¤£à¥à¤¡à¤²à¥€ à¤µà¤¿à¤¶à¥à¤²à¥‡à¤·à¤£"),
    HOME_COMING_SOON("Coming Soon", "à¤›à¤¿à¤Ÿà¥à¤Ÿà¥ˆ à¤†à¤‰à¤à¤¦à¥ˆà¤›"),
    HOME_SOON_BADGE("Soon", "à¤›à¤¿à¤Ÿà¥à¤Ÿà¥ˆ"),

    // ============================================
    // HOME TAB - FEATURE CARDS
    // ============================================
    FEATURE_BIRTH_CHART("Birth Chart", "à¤œà¤¨à¥à¤® à¤•à¥à¤£à¥à¤¡à¤²à¥€"),
    FEATURE_BIRTH_CHART_DESC("View your complete Vedic birth chart", "à¤†à¤«à¥à¤¨à¥‹ à¤ªà¥‚à¤°à¥à¤£ à¤µà¥ˆà¤¦à¤¿à¤• à¤œà¤¨à¥à¤® à¤•à¥à¤£à¥à¤¡à¤²à¥€ à¤¹à¥‡à¤°à¥à¤¨à¥à¤¹à¥‹à¤¸à¥"),

    FEATURE_PLANETS("Planets", "à¤—à¥à¤°à¤¹à¤¹à¤°à¥‚"),
    FEATURE_PLANETS_DESC("Detailed planetary positions", "à¤µà¤¿à¤¸à¥à¤¤à¥ƒà¤¤ à¤—à¥à¤°à¤¹ à¤¸à¥à¤¥à¤¿à¤¤à¤¿"),

    FEATURE_YOGAS("Yogas", "à¤¯à¥‹à¤—à¤¹à¤°à¥‚"),
    FEATURE_YOGAS_DESC("Planetary combinations & effects", "à¤—à¥à¤°à¤¹ à¤¸à¤‚à¤¯à¥‹à¤œà¤¨ à¤° à¤ªà¥à¤°à¤­à¤¾à¤µà¤¹à¤°à¥‚"),

    FEATURE_DASHAS("Dashas", "à¤¦à¤¶à¤¾à¤¹à¤°à¥‚"),
    FEATURE_DASHAS_DESC("Planetary period timeline", "à¤—à¥à¤°à¤¹ à¤…à¤µà¤§à¤¿ à¤¸à¤®à¤¯à¤°à¥‡à¤–à¤¾"),

    FEATURE_TRANSITS("Transits", "à¤—à¥‹à¤šà¤°à¤¹à¤°à¥‚"),
    FEATURE_TRANSITS_DESC("Current planetary movements", "à¤¹à¤¾à¤²à¤•à¥‹ à¤—à¥à¤°à¤¹ à¤—à¤¤à¤¿à¤µà¤¿à¤§à¤¿à¤¹à¤°à¥‚"),

    FEATURE_ASHTAKAVARGA("Ashtakavarga", "à¤…à¤·à¥à¤Ÿà¤•à¤µà¤°à¥à¤—"),
    FEATURE_ASHTAKAVARGA_DESC("Strength analysis by house", "à¤­à¤¾à¤µà¤¾à¤¨à¥à¤¸à¤¾à¤° à¤¬à¤² à¤µà¤¿à¤¶à¥à¤²à¥‡à¤·à¤£"),

    // Ashtakavarga Details
    ASHTAKAVARGA_ABOUT_TITLE("About Ashtakavarga", "à¤…à¤·à¥à¤Ÿà¤•à¤µà¤°à¥à¤— à¤¬à¤¾à¤°à¥‡à¤®à¤¾"),
    ASHTAKAVARGA_ABOUT_DESC("Ashtakavarga is an ancient Vedic astrology technique for assessing planetary strength and predicting transit effects.", "à¤…à¤·à¥à¤Ÿà¤•à¤µà¤°à¥à¤— à¤µà¥ˆà¤¦à¤¿à¤• à¤œà¥à¤¯à¥‹à¤¤à¤¿à¤·à¤•à¥‹ à¤à¤• à¤ªà¥à¤°à¤¾à¤šà¥€à¤¨ à¤ªà¥à¤°à¤µà¤¿à¤§à¤¿ à¤¹à¥‹ à¤œà¤¸à¤²à¥‡ à¤—à¥à¤°à¤¹à¤•à¥‹ à¤¶à¤•à¥à¤¤à¤¿ à¤®à¤¾à¤ªà¤¨ à¤° à¤—à¥‹à¤šà¤° à¤ªà¥à¤°à¤­à¤¾à¤µ à¤ªà¥‚à¤°à¥à¤µà¤¾à¤¨à¥à¤®à¤¾à¤¨ à¤—à¤°à¥à¤¦à¤›à¥¤"),
    ASHTAKAVARGA_SAV_TITLE("Sarvashtakavarga (SAV)", "à¤¸à¤°à¥à¤µà¤¾à¤·à¥à¤Ÿà¤•à¤µà¤°à¥à¤— (SAV)"),
    ASHTAKAVARGA_SAV_DESC("Combined strength points from all planets in each zodiac sign. Higher values (28+) indicate favorable areas.", "à¤¸à¤¬à¥ˆ à¤—à¥à¤°à¤¹à¤¬à¤¾à¤Ÿ à¤ªà¥à¤°à¤¤à¥à¤¯à¥‡à¤• à¤°à¤¾à¤¶à¤¿à¤®à¤¾ à¤¸à¤‚à¤¯à¥‹à¤œà¤¿à¤¤ à¤¶à¤•à¥à¤¤à¤¿ à¤¬à¤¿à¤¨à¥à¤¦à¥à¥¤ à¤‰à¤šà¥à¤š à¤®à¤¾à¤¨ (à¥¨à¥®+) à¤…à¤¨à¥à¤•à¥‚à¤² à¤•à¥à¤·à¥‡à¤¤à¥à¤°à¤¹à¤°à¥‚ à¤¸à¤‚à¤•à¥‡à¤¤ à¤—à¤°à¥à¤›à¥¤"),
    ASHTAKAVARGA_BAV_TITLE("Bhinnashtakavarga (BAV)", "à¤­à¤¿à¤¨à¥à¤¨à¤¾à¤·à¥à¤Ÿà¤•à¤µà¤°à¥à¤— (BAV)"),
    ASHTAKAVARGA_BAV_DESC("Individual planet strength in each sign (0-8 bindus). Use this to predict transit effects.", "à¤ªà¥à¤°à¤¤à¥à¤¯à¥‡à¤• à¤°à¤¾à¤¶à¤¿à¤®à¤¾ à¤µà¥à¤¯à¤•à¥à¤¤à¤¿à¤—à¤¤ à¤—à¥à¤°à¤¹à¤•à¥‹ à¤¶à¤•à¥à¤¤à¤¿ (à¥¦-à¥® à¤¬à¤¿à¤¨à¥à¤¦à¥)à¥¤ à¤—à¥‹à¤šà¤° à¤ªà¥à¤°à¤­à¤¾à¤µ à¤ªà¥‚à¤°à¥à¤µà¤¾à¤¨à¥à¤®à¤¾à¤¨ à¤—à¤°à¥à¤¨ à¤¯à¥‹ à¤ªà¥à¤°à¤¯à¥‹à¤— à¤—à¤°à¥à¤¨à¥à¤¹à¥‹à¤¸à¥à¥¤"),

    FEATURE_PANCHANGA("Panchanga", "à¤ªà¤žà¥à¤šà¤¾à¤™à¥à¤—"),
    FEATURE_PANCHANGA_DESC("Vedic calendar elements", "à¤µà¥ˆà¤¦à¤¿à¤• à¤ªà¤¾à¤¤à¥à¤°à¥‹ à¤¤à¤¤à¥à¤µà¤¹à¤°à¥‚"),

    FEATURE_MATCHMAKING("Matchmaking", "à¤•à¥à¤£à¥à¤¡à¤²à¥€ à¤®à¤¿à¤²à¤¾à¤¨"),
    FEATURE_MATCHMAKING_DESC("Kundli Milan compatibility", "à¤µà¤¿à¤µà¤¾à¤¹ à¤®à¥‡à¤²à¤¾à¤ªà¤• à¤—à¥à¤£ à¤®à¤¿à¤²à¤¾à¤¨"),

    FEATURE_MUHURTA("Muhurta", "à¤®à¥à¤¹à¥‚à¤°à¥à¤¤"),
    FEATURE_MUHURTA_DESC("Auspicious timing finder", "à¤¶à¥à¤­ à¤¸à¤®à¤¯ à¤–à¥‹à¤œà¤•à¤°à¥à¤¤à¤¾"),

    FEATURE_REMEDIES("Remedies", "à¤‰à¤ªà¤¾à¤¯à¤¹à¤°à¥‚"),
    FEATURE_REMEDIES_DESC("Personalized remedies", "à¤µà¥à¤¯à¤•à¥à¤¤à¤¿à¤—à¤¤ à¤‰à¤ªà¤¾à¤¯à¤¹à¤°à¥‚"),

    FEATURE_VARSHAPHALA("Varshaphala", "à¤µà¤°à¥à¤·à¤«à¤²"),
    FEATURE_VARSHAPHALA_DESC("Solar return horoscope", "à¤µà¤¾à¤°à¥à¤·à¤¿à¤• à¤°à¤¾à¤¶à¤¿à¤«à¤²"),

    // Varshaphala Details
    VARSHAPHALA_TAB_OVERVIEW("Overview", "à¤…à¤µà¤²à¥‹à¤•à¤¨"),
    VARSHAPHALA_TAB_TAJIKA("Tajika", "à¤¤à¤œà¤¿à¤•à¤¾"),
    VARSHAPHALA_TAB_SAHAMS("Sahams", "à¤¸à¤¹à¤®"),
    VARSHAPHALA_TAB_DASHA("Dasha", "à¤¦à¤¶à¤¾"),
    VARSHAPHALA_TAB_HOUSES("Houses", "à¤­à¤¾à¤µà¤¹à¤°à¥‚"),
    VARSHAPHALA_ANNUAL_HOROSCOPE("Annual Horoscope", "à¤µà¤¾à¤°à¥à¤·à¤¿à¤• à¤°à¤¾à¤¶à¤¿à¤«à¤²"),
    VARSHAPHALA_AGE("Age %d", "à¤†à¤¯à¥ %d"),
    VARSHAPHALA_PREV_YEAR("Previous year", "à¤…à¤˜à¤¿à¤²à¥à¤²à¥‹ à¤µà¤°à¥à¤·"),
    VARSHAPHALA_NEXT_YEAR("Next year", "à¤…à¤°à¥à¤•à¥‹ à¤µà¤°à¥à¤·"),
    VARSHAPHALA_SELECT_CHART("Select a birth chart to view Varshaphala", "à¤µà¤°à¥à¤·à¤«à¤² à¤¹à¥‡à¤°à¥à¤¨ à¤œà¤¨à¥à¤® à¤•à¥à¤£à¥à¤¡à¤²à¥€ à¤›à¤¾à¤¨à¥à¤¨à¥à¤¹à¥‹à¤¸à¥"),
    VARSHAPHALA_COMPUTING("Computing Annual Horoscope...", "à¤µà¤¾à¤°à¥à¤·à¤¿à¤• à¤°à¤¾à¤¶à¤¿à¤«à¤² à¤—à¤£à¤¨à¤¾ à¤—à¤°à¥à¤¦à¥ˆ..."),
    VARSHAPHALA_COMPUTING_DESC("Calculating Tajika aspects, Sahams & Mudda Dasha", "à¤¤à¤œà¤¿à¤•à¤¾ à¤ªà¤•à¥à¤·, à¤¸à¤¹à¤® à¤° à¤®à¥à¤¦à¥à¤¦ à¤¦à¤¶à¤¾ à¤—à¤£à¤¨à¤¾ à¤—à¤°à¥à¤¦à¥ˆ"),
    VARSHAPHALA_ERROR("Calculation Error", "à¤—à¤£à¤¨à¤¾ à¤¤à¥à¤°à¥à¤Ÿà¤¿"),
    VARSHAPHALA_RESET_YEAR("Reset to Current Year", "à¤µà¤°à¥à¤¤à¤®à¤¾à¤¨ à¤µà¤°à¥à¤·à¤®à¤¾ à¤°à¤¿à¤¸à¥‡à¤Ÿ"),
    VARSHAPHALA_SOLAR_RETURN("Solar Return", "à¤¸à¥Œà¤° à¤ªà¥à¤°à¤¤à¤¿à¤«à¤²"),
    VARSHAPHALA_YEAR_LORD("Year Lord", "à¤µà¤°à¥à¤· à¤¸à¥à¤µà¤¾à¤®à¥€"),
    VARSHAPHALA_MUNTHA("Muntha", "à¤®à¥à¤¨à¥à¤¥"),
    VARSHAPHALA_MUNTHA_LORD("Lord: %s", "à¤¸à¥à¤µà¤¾à¤®à¥€: %s"),
    VARSHAPHALA_TAJIKA_CHART("Tajika Annual Chart", "à¤¤à¤œà¤¿à¤•à¤¾ à¤µà¤¾à¤°à¥à¤·à¤¿à¤• à¤•à¥à¤£à¥à¤¡à¤²à¥€"),
    VARSHAPHALA_PANCHA_VARGIYA("Pancha Vargiya Bala", "à¤ªà¤žà¥à¤š à¤µà¤°à¥à¤—à¥€à¤¯ à¤¬à¤²"),
    VARSHAPHALA_TRI_PATAKI("Tri-Pataki Chakra", "à¤¤à¥à¤°à¤¿-à¤ªà¤¤à¤•à¥€ à¤šà¤•à¥à¤°"),
    VARSHAPHALA_MAJOR_THEMES("Major Themes", "à¤®à¥à¤–à¥à¤¯ à¤µà¤¿à¤·à¤¯à¤¹à¤°à¥‚"),
    VARSHAPHALA_MONTHLY_OUTLOOK("Monthly Outlook", "à¤®à¤¾à¤¸à¤¿à¤• à¤¦à¥ƒà¤·à¥à¤Ÿà¤¿à¤•à¥‹à¤£"),
    VARSHAPHALA_FAVORABLE("Favorable", "à¤…à¤¨à¥à¤•à¥‚à¤²"),
    VARSHAPHALA_KEY_DATES("Key Dates", "à¤®à¤¹à¤¤à¥à¤¤à¥à¤µà¤ªà¥‚à¤°à¥à¤£ à¤®à¤¿à¤¤à¤¿à¤¹à¤°à¥‚"),
    VARSHAPHALA_OVERALL_PREDICTION("Overall Prediction", "à¤¸à¤®à¤—à¥à¤° à¤­à¤µà¤¿à¤·à¥à¤¯à¤µà¤¾à¤£à¥€"),
    VARSHAPHALA_TAJIKA_SUMMARY("Tajika Yogas Summary", "à¤¤à¤œà¤¿à¤•à¤¾ à¤¯à¥‹à¤— à¤¸à¤¾à¤°à¤¾à¤‚à¤¶"),
    VARSHAPHALA_SAHAMS_TITLE("Sahams (Arabic Parts)", "à¤¸à¤¹à¤® (à¤…à¤°à¤¬à¥€ à¤­à¤¾à¤—)"),
    VARSHAPHALA_SAHAMS_DESC("Sensitive points calculated from planetary positions", "à¤—à¥à¤°à¤¹ à¤¸à¥à¤¥à¤¿à¤¤à¤¿à¤¬à¤¾à¤Ÿ à¤—à¤£à¤¨à¤¾ à¤—à¤°à¤¿à¤à¤•à¥‹ à¤¸à¤‚à¤µà¥‡à¤¦à¤¨à¤¶à¥€à¤² à¤¬à¤¿à¤¨à¥à¤¦à¥à¤¹à¤°à¥‚"),
    VARSHAPHALA_MUDDA_DASHA("Mudda Dasha", "à¤®à¥à¤¦à¥à¤¦ à¤¦à¤¶à¤¾"),
    VARSHAPHALA_MUDDA_DASHA_DESC("Annual planetary periods based on Moon's position", "à¤šà¤¨à¥à¤¦à¥à¤°à¤•à¥‹ à¤¸à¥à¤¥à¤¿à¤¤à¤¿à¤®à¤¾ à¤†à¤§à¤¾à¤°à¤¿à¤¤ à¤µà¤¾à¤°à¥à¤·à¤¿à¤• à¤—à¥à¤°à¤¹ à¤…à¤µà¤§à¤¿à¤¹à¤°à¥‚"),
    VARSHAPHALA_POSITION("Position", "à¤¸à¥à¤¥à¤¿à¤¤à¤¿"),
    VARSHAPHALA_HOUSE("House", "à¤­à¤¾à¤µ"),
    VARSHAPHALA_DAYS("%d days", "%d à¤¦à¤¿à¤¨"),

    FEATURE_PRASHNA("Prashna", "à¤ªà¥à¤°à¤¶à¥à¤¨"),
    FEATURE_PRASHNA_DESC("Horary astrology", "à¤ªà¥à¤°à¤¶à¥à¤¨ à¤œà¥à¤¯à¥‹à¤¤à¤¿à¤·"),

    // Prashna Details
    PRASHNA_NEW_QUESTION("New question", "à¤¨à¤¯à¤¾à¤ à¤ªà¥à¤°à¤¶à¥à¤¨"),
    PRASHNA_KUNDALI("Prashna Kundali", "à¤ªà¥à¤°à¤¶à¥à¤¨ à¤•à¥à¤£à¥à¤¡à¤²à¥€"),
    PRASHNA_HORARY("Horary Astrology", "à¤ªà¥à¤°à¤¶à¥à¤¨ à¤œà¥à¤¯à¥‹à¤¤à¤¿à¤·"),
    PRASHNA_INTRO("Ask your question and receive guidance based on the planetary positions at this very moment.", "à¤†à¤«à¥à¤¨à¥‹ à¤ªà¥à¤°à¤¶à¥à¤¨ à¤¸à¥‹à¤§à¥à¤¨à¥à¤¹à¥‹à¤¸à¥ à¤° à¤¯à¤¸ à¤•à¥à¤·à¤£à¤•à¥‹ à¤—à¥à¤°à¤¹ à¤¸à¥à¤¥à¤¿à¤¤à¤¿à¤•à¥‹ à¤†à¤§à¤¾à¤°à¤®à¤¾ à¤®à¤¾à¤°à¥à¤—à¤¦à¤°à¥à¤¶à¤¨ à¤ªà¥à¤°à¤¾à¤ªà¥à¤¤ à¤—à¤°à¥à¤¨à¥à¤¹à¥‹à¤¸à¥à¥¤"),
    PRASHNA_YOUR_QUESTION("Your Question", "à¤¤à¤ªà¤¾à¤ˆà¤‚à¤•à¥‹ à¤ªà¥à¤°à¤¶à¥à¤¨"),
    PRASHNA_QUESTION_HINT("Enter your question here...", "à¤¯à¤¹à¤¾à¤ à¤†à¤«à¥à¤¨à¥‹ à¤ªà¥à¤°à¤¶à¥à¤¨ à¤ªà¥à¤°à¤µà¤¿à¤·à¥à¤Ÿ à¤—à¤°à¥à¤¨à¥à¤¹à¥‹à¤¸à¥..."),
    PRASHNA_QUESTION_HELP("Be specific and clear. Frame your question with a sincere intent.", "à¤¸à¥à¤ªà¤·à¥à¤Ÿ à¤° à¤µà¤¿à¤¶à¤¿à¤·à¥à¤Ÿ à¤¹à¥à¤¨à¥à¤¹à¥‹à¤¸à¥à¥¤ à¤ˆà¤®à¤¾à¤¨à¤¦à¤¾à¤° à¤‡à¤°à¤¾à¤¦à¤¾à¤²à¥‡ à¤†à¤«à¥à¤¨à¥‹ à¤ªà¥à¤°à¤¶à¥à¤¨ à¤¬à¤¨à¤¾à¤‰à¤¨à¥à¤¹à¥‹à¤¸à¥à¥¤"),
    PRASHNA_CATEGORY("Question Category", "à¤ªà¥à¤°à¤¶à¥à¤¨ à¤µà¤°à¥à¤—"),
    PRASHNA_LOCATION("Question Location", "à¤ªà¥à¤°à¤¶à¥à¤¨ à¤¸à¥à¤¥à¤¾à¤¨"),
    PRASHNA_TIME_NOW("Now", "à¤…à¤¹à¤¿à¤²à¥‡"),
    PRASHNA_ANALYZE("Analyze Question", "à¤ªà¥à¤°à¤¶à¥à¤¨ à¤µà¤¿à¤¶à¥à¤²à¥‡à¤·à¤£ à¤—à¤°à¥à¤¨à¥à¤¹à¥‹à¤¸à¥"),
    PRASHNA_ABOUT("About Prashna", "à¤ªà¥à¤°à¤¶à¥à¤¨ à¤¬à¤¾à¤°à¥‡à¤®à¤¾"),
    PRASHNA_ANALYZING("Analyzing your question...", "à¤¤à¤ªà¤¾à¤ˆà¤‚à¤•à¥‹ à¤ªà¥à¤°à¤¶à¥à¤¨ à¤µà¤¿à¤¶à¥à¤²à¥‡à¤·à¤£ à¤—à¤°à¥à¤¦à¥ˆ..."),
    PRASHNA_CALCULATING("Calculating planetary positions and yogas", "à¤—à¥à¤°à¤¹ à¤¸à¥à¤¥à¤¿à¤¤à¤¿ à¤° à¤¯à¥‹à¤—à¤¹à¤°à¥‚ à¤—à¤£à¤¨à¤¾ à¤—à¤°à¥à¤¦à¥ˆ"),
    PRASHNA_ANALYSIS_FAILED("Analysis Failed", "à¤µà¤¿à¤¶à¥à¤²à¥‡à¤·à¤£ à¤…à¤¸à¤«à¤²"),
    PRASHNA_UNFAVORABLE("Unfavorable", "à¤ªà¥à¤°à¤¤à¤¿à¤•à¥‚à¤²"),
    PRASHNA_FAVORABLE("Favorable", "à¤…à¤¨à¥à¤•à¥‚à¤²"),
    PRASHNA_SCORE("Score: %d", "à¤…à¤‚à¤•: %d"),
    PRASHNA_QUESTION_DETAILS("Question Details", "à¤ªà¥à¤°à¤¶à¥à¤¨ à¤µà¤¿à¤µà¤°à¤£"),
    PRASHNA_MOON_ANALYSIS("Moon Analysis", "à¤šà¤¨à¥à¤¦à¥à¤° à¤µà¤¿à¤¶à¥à¤²à¥‡à¤·à¤£"),
    PRASHNA_LAGNA_ANALYSIS("Lagna Analysis", "à¤²à¤—à¥à¤¨ à¤µà¤¿à¤¶à¥à¤²à¥‡à¤·à¤£"),
    PRASHNA_RISING_SIGN("Rising Sign", "à¤‰à¤¦à¤¯ à¤°à¤¾à¤¶à¤¿"),
    PRASHNA_LAGNA_LORD("Lagna Lord", "à¤²à¤—à¥à¤¨à¥‡à¤¶"),
    PRASHNA_LORD_POSITION("Lord Position", "à¤¸à¥à¤µà¤¾à¤®à¥€ à¤¸à¥à¤¥à¤¿à¤¤à¤¿"),
    PRASHNA_CONDITION("Condition", "à¤…à¤µà¤¸à¥à¤¥à¤¾"),
    PRASHNA_PLANETS_IN_LAGNA("Planets in Lagna", "à¤²à¤—à¥à¤¨à¤®à¤¾ à¤—à¥à¤°à¤¹à¤¹à¤°à¥‚"),
    PRASHNA_MOON_VOID("Moon is Void of Course - delays or changes likely", "à¤šà¤¨à¥à¤¦à¥à¤° à¤¶à¥‚à¤¨à¥à¤¯ à¤—à¤¤à¤¿à¤®à¤¾ à¤› - à¤¢à¤¿à¤²à¤¾à¤‡ à¤µà¤¾ à¤ªà¤°à¤¿à¤µà¤°à¥à¤¤à¤¨ à¤¸à¤®à¥à¤­à¤¾à¤µà¤¿à¤¤"),
    PRASHNA_TIMING("Timing Prediction", "à¤¸à¤®à¤¯ à¤­à¤µà¤¿à¤·à¥à¤¯à¤µà¤¾à¤£à¥€"),
    PRASHNA_ESTIMATED_TIMEFRAME("Estimated Timeframe", "à¤…à¤¨à¥à¤®à¤¾à¤¨à¤¿à¤¤ à¤¸à¤®à¤¯à¤¾à¤µà¤§à¤¿"),
    PRASHNA_SPECIAL_YOGAS("Special Prashna Yogas", "à¤µà¤¿à¤¶à¥‡à¤· à¤ªà¥à¤°à¤¶à¥à¤¨ à¤¯à¥‹à¤—à¤¹à¤°à¥‚"),
    PRASHNA_SUPPORTING_FACTORS("Supporting Factors", "à¤¸à¤®à¤°à¥à¤¥à¤• à¤•à¤¾à¤°à¤•à¤¹à¤°à¥‚"),
    PRASHNA_CHALLENGES("Challenges", "à¤šà¥à¤¨à¥Œà¤¤à¥€à¤¹à¤°à¥‚"),
    PRASHNA_RECOMMENDATIONS("Recommendations", "à¤¸à¤¿à¤«à¤¾à¤°à¤¿à¤¸à¤¹à¤°à¥‚"),
    PRASHNA_MOON_PHASE_WAXING("Waxing", "à¤¶à¥à¤•à¥à¤² à¤ªà¤•à¥à¤·"),
    PRASHNA_MOON_PHASE_WANING("Waning", "à¤•à¥ƒà¤·à¥à¤£ à¤ªà¤•à¥à¤·"),
    PRASHNA_SIGN("Sign", "à¤°à¤¾à¤¶à¤¿"),
    PRASHNA_NAKSHATRA_LORD("Nakshatra Lord", "à¤¨à¤•à¥à¤·à¤¤à¥à¤° à¤¸à¥à¤µà¤¾à¤®à¥€"),
    PRASHNA_TITHI("Tithi", "à¤¤à¤¿à¤¥à¤¿"),
    PRASHNA_PHASE("Phase", "à¤ªà¤•à¥à¤·"),
    PRASHNA_INST_1("Prashna Kundali is cast for the moment the question arises in your mind or when you ask it.", "à¤ªà¥à¤°à¤¶à¥à¤¨ à¤•à¥à¤£à¥à¤¡à¤²à¥€ à¤¤à¤ªà¤¾à¤ˆà¤‚à¤•à¥‹ à¤®à¤¨à¤®à¤¾ à¤ªà¥à¤°à¤¶à¥à¤¨ à¤‰à¤ à¥‡à¤•à¥‹ à¤•à¥à¤·à¤£à¤®à¤¾ à¤µà¤¾ à¤¤à¤ªà¤¾à¤ˆà¤‚à¤²à¥‡ à¤¸à¥‹à¤§à¥‡à¤•à¥‹ à¤¬à¥‡à¤²à¤¾ à¤¬à¤¨à¤¾à¤‡à¤¨à¥à¤›à¥¤"),
    PRASHNA_INST_2("The Moon is the primary significator representing your mind and the matter at hand.", "à¤šà¤¨à¥à¤¦à¥à¤°à¤®à¤¾ à¤ªà¥à¤°à¤¾à¤¥à¤®à¤¿à¤• à¤•à¤¾à¤°à¤• à¤¹à¥‹ à¤œà¤¸à¤²à¥‡ à¤¤à¤ªà¤¾à¤ˆà¤‚à¤•à¥‹ à¤®à¤¨ à¤° à¤¹à¤¾à¤¤à¤®à¤¾ à¤°à¤¹à¥‡à¤•à¥‹ à¤µà¤¿à¤·à¤¯à¤²à¤¾à¤ˆ à¤ªà¥à¤°à¤¤à¤¿à¤¨à¤¿à¤§à¤¿à¤¤à¥à¤µ à¤—à¤°à¥à¤¦à¤›à¥¤"),
    PRASHNA_INST_3("Frame your question with sincerity and focused intent for accurate guidance.", "à¤¸à¤Ÿà¥€à¤• à¤®à¤¾à¤°à¥à¤—à¤¦à¤°à¥à¤¶à¤¨à¤•à¥‹ à¤²à¤¾à¤—à¤¿ à¤†à¤«à¥à¤¨à¥‹ à¤ªà¥à¤°à¤¶à¥à¤¨ à¤ˆà¤®à¤¾à¤¨à¤¦à¤¾à¤°à¤¿à¤¤à¤¾ à¤° à¤•à¥‡à¤¨à¥à¤¦à¥à¤°à¤¿à¤¤ à¤‰à¤¦à¥à¤¦à¥‡à¤¶à¥à¤¯à¤¸à¤¹à¤¿à¤¤ à¤¬à¤¨à¤¾à¤‰à¤¨à¥à¤¹à¥‹à¤¸à¥à¥¤"),
    PRASHNA_INST_4("The analysis considers Lagna, Moon, relevant houses, and special Prashna yogas.", "à¤µà¤¿à¤¶à¥à¤²à¥‡à¤·à¤£à¤²à¥‡ à¤²à¤—à¥à¤¨, à¤šà¤¨à¥à¤¦à¥à¤°à¤®à¤¾, à¤¸à¤®à¥à¤¬à¤¨à¥à¤§à¤¿à¤¤ à¤­à¤¾à¤µà¤¹à¤°à¥‚, à¤° à¤µà¤¿à¤¶à¥‡à¤· à¤ªà¥à¤°à¤¶à¥à¤¨ à¤¯à¥‹à¤—à¤¹à¤°à¥‚à¤²à¤¾à¤ˆ à¤µà¤¿à¤šà¤¾à¤° à¤—à¤°à¥à¤›à¥¤"),
    PRASHNA_INST_5("Timing predictions are based on planetary movements and house lord positions.", "à¤¸à¤®à¤¯ à¤­à¤µà¤¿à¤·à¥à¤¯à¤µà¤¾à¤£à¥€à¤¹à¤°à¥‚ à¤—à¥à¤°à¤¹ à¤—à¤¤à¤¿à¤µà¤¿à¤§à¤¿ à¤° à¤­à¤¾à¤µ à¤¸à¥à¤µà¤¾à¤®à¥€ à¤¸à¥à¤¥à¤¿à¤¤à¤¿à¤®à¤¾ à¤†à¤§à¤¾à¤°à¤¿à¤¤ à¤›à¤¨à¥à¥¤"),
    PRASHNA_ANALYZE_ERROR("Failed to analyze question", "à¤ªà¥à¤°à¤¶à¥à¤¨ à¤µà¤¿à¤¶à¥à¤²à¥‡à¤·à¤£ à¤—à¤°à¥à¤¨ à¤…à¤¸à¤«à¤²"),
    PRASHNA_AI_INSIGHT("AI Insight", "à¤à¤†à¤ˆ à¤…à¤¨à¥à¤¤à¤°à¥à¤¦à¥ƒà¤·à¥à¤Ÿà¤¿"),
    PRASHNA_AI_INSIGHT_SUBTITLE("Powered by Stormy", "Stormy à¤¦à¥à¤µà¤¾à¤°à¤¾ à¤¸à¤‚à¤šà¤¾à¤²à¤¿à¤¤"),
    PRASHNA_AI_INSIGHT_DESC("Get a deeper, personalized interpretation of your Prashna chart using AI. The AI will analyze the planetary positions and yogas to provide additional guidance.", "à¤à¤†à¤ˆ à¤ªà¥à¤°à¤¯à¥‹à¤— à¤—à¤°à¥‡à¤° à¤†à¤«à¥à¤¨à¥‹ à¤ªà¥à¤°à¤¶à¥à¤¨ à¤šà¤¾à¤°à¥à¤Ÿà¤•à¥‹ à¤—à¤¹à¤¿à¤°à¥‹, à¤µà¥à¤¯à¤•à¥à¤¤à¤¿à¤—à¤¤ à¤µà¥à¤¯à¤¾à¤–à¥à¤¯à¤¾ à¤ªà¥à¤°à¤¾à¤ªà¥à¤¤ à¤—à¤°à¥à¤¨à¥à¤¹à¥‹à¤¸à¥à¥¤ à¤à¤†à¤ˆà¤²à¥‡ à¤¥à¤ª à¤®à¤¾à¤°à¥à¤—à¤¦à¤°à¥à¤¶à¤¨ à¤ªà¥à¤°à¤¦à¤¾à¤¨ à¤—à¤°à¥à¤¨ à¤—à¥à¤°à¤¹ à¤¸à¥à¤¥à¤¿à¤¤à¤¿ à¤° à¤¯à¥‹à¤—à¤¹à¤°à¥‚à¤•à¥‹ à¤µà¤¿à¤¶à¥à¤²à¥‡à¤·à¤£ à¤—à¤°à¥à¤¨à¥‡à¤›à¥¤"),
    PRASHNA_GENERATE_AI_INSIGHT("Generate AI Insight", "à¤à¤†à¤ˆ à¤…à¤¨à¥à¤¤à¤°à¥à¤¦à¥ƒà¤·à¥à¤Ÿà¤¿ à¤‰à¤¤à¥à¤ªà¤¨à¥à¤¨ à¤—à¤°à¥à¤¨à¥à¤¹à¥‹à¤¸à¥"),
    PRASHNA_AI_ANALYZING("Consulting the stars...", "à¤¤à¤¾à¤°à¤¾à¤¹à¤°à¥‚à¤¸à¤à¤— à¤ªà¤°à¤¾à¤®à¤°à¥à¤¶ à¤—à¤°à¥à¤¦à¥ˆ..."),
    PRASHNA_CONFIDENCE("%d%% confidence", "%d%% à¤µà¤¿à¤¶à¥à¤µà¤¾à¤¸"),

    FEATURE_SYNASTRY("Synastry", "à¤¸à¤¿à¤¨à¤¾à¤¸à¥à¤Ÿà¥à¤°à¥€"),
    FEATURE_SYNASTRY_DESC("Chart comparison", "à¤•à¥à¤£à¥à¤¡à¤²à¥€ à¤¤à¥à¤²à¤¨à¤¾"),

    FEATURE_NAKSHATRAS("Nakshatras", "à¤¨à¤•à¥à¤·à¤¤à¥à¤°à¤¹à¤°à¥‚"),
    FEATURE_NAKSHATRAS_DESC("Deep nakshatra analysis", "à¤—à¤¹à¤¨ à¤¨à¤•à¥à¤·à¤¤à¥à¤° à¤µà¤¿à¤¶à¥à¤²à¥‡à¤·à¤£"),

    FEATURE_SHADBALA("Shadbala", "à¤·à¤¡à¥à¤¬à¤²"),
    FEATURE_SHADBALA_DESC("Six-fold strength", "à¤›à¤µà¤Ÿà¤¾ à¤¬à¤²à¤¹à¤°à¥‚"),

    // Advanced Calculator Features
    FEATURE_SHODASHVARGA("Shodashvarga", "à¤·à¥‹à¤¡à¤¶à¤µà¤°à¥à¤—"),
    FEATURE_SHODASHVARGA_DESC("16-divisional chart strength", "à¥§à¥¬-à¤µà¤¿à¤­à¤¾à¤œà¤¨ à¤•à¥à¤£à¥à¤¡à¤²à¥€ à¤¬à¤²"),

    FEATURE_YOGINI_DASHA("Yogini Dasha", "à¤¯à¥‹à¤—à¤¿à¤¨à¥€ à¤¦à¤¶à¤¾"),
    FEATURE_YOGINI_DASHA_DESC("36-year nakshatra dasha", "à¥©à¥¬-à¤µà¤°à¥à¤·à¥‡ à¤¨à¤•à¥à¤·à¤¤à¥à¤° à¤¦à¤¶à¤¾"),

    FEATURE_ARGALA("Argala", "à¤…à¤°à¥à¤—à¤²à¤¾"),
    FEATURE_ARGALA_DESC("Jaimini intervention analysis", "à¤œà¥ˆà¤®à¤¿à¤¨à¥€ à¤¹à¤¸à¥à¤¤à¤•à¥à¤·à¥‡à¤ª à¤µà¤¿à¤¶à¥à¤²à¥‡à¤·à¤£"),

    FEATURE_CHARA_DASHA("Chara Dasha", "à¤šà¤° à¤¦à¤¶à¤¾"),
    FEATURE_CHARA_DASHA_DESC("Jaimini sign-based dasha", "à¤œà¥ˆà¤®à¤¿à¤¨à¥€ à¤°à¤¾à¤¶à¤¿-à¤†à¤§à¤¾à¤°à¤¿à¤¤ à¤¦à¤¶à¤¾"),

    FEATURE_BHRIGU_BINDU("Bhrigu Bindu", "à¤­à¥ƒà¤—à¥ à¤¬à¤¿à¤¨à¥à¤¦à¥"),
    FEATURE_BHRIGU_BINDU_DESC("Karmic destiny point", "à¤•à¤¾à¤°à¥à¤®à¤¿à¤• à¤­à¤¾à¤—à¥à¤¯ à¤¬à¤¿à¤¨à¥à¤¦à¥"),

    FEATURE_PREDICTIONS("Predictions", "à¤­à¤µà¤¿à¤·à¥à¤¯à¤µà¤¾à¤£à¥€"),
    FEATURE_PREDICTIONS_DESC("Comprehensive life analysis", "à¤µà¥à¤¯à¤¾à¤ªà¤• à¤œà¥€à¤µà¤¨ à¤µà¤¿à¤¶à¥à¤²à¥‡à¤·à¤£"),

    // New Advanced Features
    FEATURE_ASHTOTTARI_DASHA("Ashtottari Dasha", "à¤…à¤·à¥à¤Ÿà¥‹à¤¤à¥à¤¤à¤°à¥€ à¤¦à¤¶à¤¾"),
    FEATURE_ASHTOTTARI_DASHA_DESC("108-year Nakshatra-based timing", "à¥§à¥¦à¥® à¤µà¤°à¥à¤·à¥‡ à¤¨à¤•à¥à¤·à¤¤à¥à¤°-à¤†à¤§à¤¾à¤°à¤¿à¤¤ à¤¸à¤®à¤¯"),
    FEATURE_SUDARSHANA_CHAKRA("Sudarshana Chakra", "à¤¸à¥à¤¦à¤°à¥à¤¶à¤¨ à¤šà¤•à¥à¤°"),
    FEATURE_SUDARSHANA_CHAKRA_DESC("Triple-reference annual prediction", "à¤¤à¥à¤°à¤¿-à¤¸à¤‚à¤¦à¤°à¥à¤­ à¤µà¤¾à¤°à¥à¤·à¤¿à¤• à¤­à¤µà¤¿à¤·à¥à¤¯à¤µà¤¾à¤£à¥€"),
    FEATURE_MRITYU_BHAGA("Mrityu Bhaga", "à¤®à¥ƒà¤¤à¥à¤¯à¥ à¤­à¤¾à¤—"),
    FEATURE_MRITYU_BHAGA_DESC("Sensitive degrees analysis", "à¤¸à¤‚à¤µà¥‡à¤¦à¤¨à¤¶à¥€à¤² à¤…à¤‚à¤¶ à¤µà¤¿à¤¶à¥à¤²à¥‡à¤·à¤£"),
    FEATURE_LAL_KITAB("Lal Kitab Remedies", "à¤²à¤¾à¤² à¤•à¤¿à¤¤à¤¾à¤¬ à¤‰à¤ªà¤¾à¤¯"),
    FEATURE_LAL_KITAB_DESC("Practical everyday remedies", "à¤µà¥à¤¯à¤¾à¤µà¤¹à¤¾à¤°à¤¿à¤• à¤¦à¥ˆà¤¨à¤¿à¤• à¤‰à¤ªà¤¾à¤¯"),
    FEATURE_DIVISIONAL_CHARTS("Divisional Charts", "à¤µà¤¿à¤­à¤¾à¤—à¥€à¤¯ à¤•à¥à¤£à¥à¤¡à¤²à¥€"),
    FEATURE_DIVISIONAL_CHARTS_DESC("D-2, D-3, D-9, D-10, D-12 analysis", "à¤¹à¥‹à¤°à¤¾, à¤¦à¥à¤°à¥‡à¤•à¥à¤•à¤¾à¤£, à¤¨à¤µà¤¾à¤‚à¤¶, à¤¦à¤¶à¤®à¤¾à¤‚à¤¶, à¤¦à¥à¤µà¤¾à¤¦à¤¶à¤¾à¤‚à¤¶"),
    FEATURE_UPACHAYA_TRANSIT("Upachaya Transits", "à¤‰à¤ªà¤šà¤¯ à¤—à¥‹à¤šà¤°"),
    FEATURE_UPACHAYA_TRANSIT_DESC("Growth house transit tracking", "à¤‰à¤ªà¤šà¤¯ à¤­à¤¾à¤µ à¤—à¥‹à¤šà¤° à¤µà¤¿à¤¶à¥à¤²à¥‡à¤·à¤£"),
    FEATURE_KALACHAKRA_DASHA("Kalachakra Dasha", "à¤•à¤¾à¤²à¤šà¤•à¥à¤° à¤¦à¤¶à¤¾"),
    FEATURE_KALACHAKRA_DASHA_DESC("Body-soul timing system for health and spiritual predictions", "à¤¸à¥à¤µà¤¾à¤¸à¥à¤¥à¥à¤¯ à¤° à¤†à¤§à¥à¤¯à¤¾à¤¤à¥à¤®à¤¿à¤• à¤­à¤µà¤¿à¤·à¥à¤¯à¤µà¤¾à¤£à¥€à¤•à¥‹ à¤²à¤¾à¤—à¤¿ à¤¦à¥‡à¤¹-à¤†à¤¤à¥à¤®à¤¾ à¤¸à¤®à¤¯ à¤ªà¥à¤°à¤£à¤¾à¤²à¥€"),
    FEATURE_TARABALA("Tarabala", "à¤¤à¤¾à¤°à¤¾à¤¬à¤²"),
    FEATURE_TARABALA_DESC("Daily Moon strength & Nakshatra timing", "à¤¦à¥ˆà¤¨à¤¿à¤• à¤šà¤¨à¥à¤¦à¥à¤° à¤¬à¤² à¤° à¤¨à¤•à¥à¤·à¤¤à¥à¤° à¤¸à¤®à¤¯"),
    FEATURE_ARUDHA_PADA("Arudha Pada", "à¤†à¤°à¥‚à¤¢ à¤ªà¤¦"),
    FEATURE_ARUDHA_PADA_DESC("Jaimini Arudha analysis for manifestation", "à¤œà¥ˆà¤®à¤¿à¤¨à¥€ à¤†à¤°à¥‚à¤¢ à¤µà¤¿à¤¶à¥à¤²à¥‡à¤·à¤£"),
    FEATURE_GRAHA_YUDDHA("Graha Yuddha", "à¤—à¥à¤°à¤¹ à¤¯à¥à¤¦à¥à¤§"),
    FEATURE_GRAHA_YUDDHA_DESC("Planetary war analysis & remedies", "à¤—à¥à¤°à¤¹ à¤¯à¥à¤¦à¥à¤§ à¤µà¤¿à¤¶à¥à¤²à¥‡à¤·à¤£ à¤° à¤‰à¤ªà¤¾à¤¯"),
    FEATURE_DASHA_SANDHI("Dasha Sandhi", "à¤¦à¤¶à¤¾ à¤¸à¤¨à¥à¤§à¤¿"),
    FEATURE_DASHA_SANDHI_DESC("Period transition analysis", "à¤…à¤µà¤§à¤¿ à¤¸à¤‚à¤•à¥à¤°à¤®à¤£ à¤µà¤¿à¤¶à¥à¤²à¥‡à¤·à¤£"),
    FEATURE_GOCHARA_VEDHA("Gochara Vedha", "à¤—à¥‹à¤šà¤° à¤µà¥‡à¤§"),
    FEATURE_GOCHARA_VEDHA_DESC("Transit obstruction effects", "à¤—à¥‹à¤šà¤° à¤…à¤µà¤°à¥‹à¤§ à¤ªà¥à¤°à¤­à¤¾à¤µ"),
    FEATURE_KEMADRUMA_YOGA("Kemadruma Yoga", "à¤•à¥‡à¤®à¤¦à¥à¤°à¥à¤® à¤¯à¥‹à¤—"),
    FEATURE_KEMADRUMA_YOGA_DESC("Moon isolation analysis", "à¤šà¤¨à¥à¤¦à¥à¤° à¤à¤•à¤¾à¤¨à¥à¤¤ à¤µà¤¿à¤¶à¥à¤²à¥‡à¤·à¤£"),
    FEATURE_PANCH_MAHAPURUSHA("Panch Mahapurusha", "à¤ªà¤žà¥à¤š à¤®à¤¹à¤¾à¤ªà¥à¤°à¥à¤·"),
    FEATURE_PANCH_MAHAPURUSHA_DESC("Five great person yogas", "à¤ªà¤¾à¤à¤š à¤®à¤¹à¤¾à¤¨à¥ à¤µà¥à¤¯à¤•à¥à¤¤à¤¿ à¤¯à¥‹à¤—"),
    FEATURE_NITYA_YOGA("Nitya Yoga", "à¤¨à¤¿à¤¤à¥à¤¯ à¤¯à¥‹à¤—"),
    FEATURE_NITYA_YOGA_DESC("27 daily yogas", "à¥¨à¥­ à¤¦à¥ˆà¤¨à¤¿à¤• à¤¯à¥‹à¤—à¤¹à¤°à¥‚"),
    FEATURE_AVASTHA("Avastha", "à¤…à¤µà¤¸à¥à¤¥à¤¾"),
    FEATURE_AVASTHA_DESC("Planetary states analysis", "à¤—à¥à¤°à¤¹ à¤…à¤µà¤¸à¥à¤¥à¤¾ à¤µà¤¿à¤¶à¥à¤²à¥‡à¤·à¤£"),

    // Predictions Screen - Tabs
    PREDICTIONS_TAB_OVERVIEW("Overview", "à¤¸à¤¾à¤°à¤¾à¤‚à¤¶"),
    PREDICTIONS_TAB_LIFE_AREAS("Life Areas", "à¤œà¥€à¤µà¤¨ à¤•à¥à¤·à¥‡à¤¤à¥à¤°à¤¹à¤°à¥‚"),
    PREDICTIONS_TAB_TIMING("Timing", "à¤¸à¤®à¤¯"),
    PREDICTIONS_TAB_REMEDIES("Remedies", "à¤‰à¤ªà¤¾à¤¯"),

    // Predictions Screen - Section Headers
    PREDICTIONS_YOUR_LIFE_PATH("Your Life Path", "à¤¤à¤ªà¤¾à¤ˆà¤‚à¤•à¥‹ à¤œà¥€à¤µà¤¨ à¤®à¤¾à¤°à¥à¤—"),
    PREDICTIONS_KEY_STRENGTHS("Key Strengths", "à¤®à¥à¤–à¥à¤¯ à¤¶à¤•à¥à¤¤à¤¿à¤¹à¤°à¥‚"),
    PREDICTIONS_SPIRITUAL_PATH("Spiritual Path", "à¤†à¤§à¥à¤¯à¤¾à¤¤à¥à¤®à¤¿à¤• à¤®à¤¾à¤°à¥à¤—"),
    PREDICTIONS_CURRENT_PERIOD("Current Period", "à¤µà¤°à¥à¤¤à¤®à¤¾à¤¨ à¤…à¤µà¤§à¤¿"),
    PREDICTIONS_ACTIVE_TRANSITS("Active Transits", "à¤¸à¤•à¥à¤°à¤¿à¤¯ à¤—à¥‹à¤šà¤°"),
    PREDICTIONS_ACTIVE_YOGAS("Active Yogas", "à¤¸à¤•à¥à¤°à¤¿à¤¯ à¤¯à¥‹à¤—à¤¹à¤°à¥‚"),
    PREDICTIONS_OPPORTUNITIES("Opportunities", "à¤…à¤µà¤¸à¤°à¤¹à¤°à¥‚"),
    PREDICTIONS_CURRENT_CHALLENGES("Current Challenges", "à¤µà¤°à¥à¤¤à¤®à¤¾à¤¨ à¤šà¥à¤¨à¥Œà¤¤à¥€à¤¹à¤°à¥‚"),
    PREDICTIONS_LIFE_AREAS_GLANCE("Life Areas at a Glance", "à¤à¤• à¤¨à¤œà¤°à¤®à¤¾ à¤œà¥€à¤µà¤¨ à¤•à¥à¤·à¥‡à¤¤à¥à¤°à¤¹à¤°à¥‚"),
    PREDICTIONS_FAVORABLE_PERIODS("Favorable Periods", "à¤…à¤¨à¥à¤•à¥‚à¤² à¤…à¤µà¤§à¤¿à¤¹à¤°à¥‚"),
    PREDICTIONS_CAUTION_PERIODS("Periods Needing Caution", "à¤¸à¤¾à¤µà¤§à¤¾à¤¨à¥€ à¤šà¤¾à¤¹à¤¿à¤¨à¥‡ à¤…à¤µà¤§à¤¿à¤¹à¤°à¥‚"),
    PREDICTIONS_IMPORTANT_DATES("Important Dates", "à¤®à¤¹à¤¤à¥à¤¤à¥à¤µà¤ªà¥‚à¤°à¥à¤£ à¤®à¤¿à¤¤à¤¿à¤¹à¤°à¥‚"),
    PREDICTIONS_REMEDIAL_SUGGESTIONS("Remedial Suggestions", "à¤‰à¤ªà¤šà¤¾à¤°à¤¾à¤¤à¥à¤®à¤• à¤¸à¥à¤à¤¾à¤µà¤¹à¤°à¥‚"),

    // Predictions Screen - States
    PREDICTIONS_NO_CHART_SELECTED("No Chart Selected", "à¤•à¥à¤¨à¥ˆ à¤•à¥à¤£à¥à¤¡à¤²à¥€ à¤›à¤¾à¤¨à¤¿à¤à¤•à¥‹ à¤›à¥ˆà¤¨"),
    PREDICTIONS_SELECT_CHART_MESSAGE("Please select a birth chart to view predictions", "à¤•à¥ƒà¤ªà¤¯à¤¾ à¤­à¤µà¤¿à¤·à¥à¤¯à¤µà¤¾à¤£à¥€ à¤¹à¥‡à¤°à¥à¤¨ à¤œà¤¨à¥à¤® à¤•à¥à¤£à¥à¤¡à¤²à¥€ à¤›à¤¾à¤¨à¥à¤¨à¥à¤¹à¥‹à¤¸à¥"),
    PREDICTIONS_CALCULATING("Calculating Predictions...", "à¤­à¤µà¤¿à¤·à¥à¤¯à¤µà¤¾à¤£à¥€ à¤—à¤£à¤¨à¤¾ à¤—à¤°à¥à¤¦à¥ˆ..."),
    PREDICTIONS_ERROR_LOADING("Error Loading Predictions", "à¤­à¤µà¤¿à¤·à¥à¤¯à¤µà¤¾à¤£à¥€ à¤²à¥‹à¤¡ à¤—à¤°à¥à¤¨ à¤¤à¥à¤°à¥à¤Ÿà¤¿"),
    PREDICTIONS_CALC_FAILED("Failed to calculate predictions", "à¤­à¤µà¤¿à¤·à¥à¤¯à¤µà¤¾à¤£à¥€ à¤—à¤£à¤¨à¤¾ à¤—à¤°à¥à¤¨ à¤…à¤¸à¤«à¤²"),

    // Predictions Screen - Life Area Labels
    PREDICTIONS_CAREER_PROFESSION("Career & Profession", "à¤•à¥à¤¯à¤¾à¤°à¤¿à¤¯à¤° à¤° à¤ªà¥‡à¤¶à¤¾"),
    PREDICTIONS_FINANCE_WEALTH("Finance & Wealth", "à¤µà¤¿à¤¤à¥à¤¤ à¤° à¤¸à¤®à¥à¤ªà¤¤à¥à¤¤à¤¿"),
    PREDICTIONS_RELATIONSHIPS_MARRIAGE("Relationships & Marriage", "à¤¸à¤®à¥à¤¬à¤¨à¥à¤§ à¤° à¤µà¤¿à¤µà¤¾à¤¹"),
    PREDICTIONS_HEALTH_WELLBEING("Health & Wellbeing", "à¤¸à¥à¤µà¤¾à¤¸à¥à¤¥à¥à¤¯ à¤° à¤•à¤²à¥à¤¯à¤¾à¤£"),
    PREDICTIONS_EDUCATION_LEARNING("Education & Learning", "à¤¶à¤¿à¤•à¥à¤·à¤¾ à¤° à¤¸à¤¿à¤•à¤¾à¤‡"),
    PREDICTIONS_FAMILY_HOME("Family & Home", "à¤ªà¤°à¤¿à¤µà¤¾à¤° à¤° à¤˜à¤°"),

    // Predictions Screen - Other Labels
    PREDICTIONS_SHORT_TERM("Short-term (3-6 months)", "à¤…à¤²à¥à¤ªà¤•à¤¾à¤²à¥€à¤¨ (à¥©-à¥¬ à¤®à¤¹à¤¿à¤¨à¤¾)"),
    PREDICTIONS_MEDIUM_TERM("Medium-term (6-12 months)", "à¤®à¤§à¥à¤¯à¤®à¤•à¤¾à¤²à¥€à¤¨ (à¥¬-à¥§à¥¨ à¤®à¤¹à¤¿à¤¨à¤¾)"),
    PREDICTIONS_LONG_TERM("Long-term (1-2 years)", "à¤¦à¥€à¤°à¥à¤˜à¤•à¤¾à¤²à¥€à¤¨ (à¥§-à¥¨ à¤µà¤°à¥à¤·)"),
    PREDICTIONS_BEST_FOR("Best for", "à¤•à¥‹ à¤²à¤¾à¤—à¤¿ à¤‰à¤¤à¥à¤¤à¤®"),
    PREDICTIONS_CAUTION_FOR("Caution for", "à¤•à¥‹ à¤²à¤¾à¤—à¤¿ à¤¸à¤¾à¤µà¤§à¤¾à¤¨à¥€"),
    PREDICTIONS_ENERGY_LEVEL("Energy Level", "à¤Šà¤°à¥à¤œà¤¾ à¤¸à¥à¤¤à¤°"),
    PREDICTIONS_DAYS_LEFT("days left", "à¤¦à¤¿à¤¨ à¤¬à¤¾à¤à¤•à¥€"),
    PREDICTIONS_MONTHS("months", "à¤®à¤¹à¤¿à¤¨à¤¾"),
    PREDICTIONS_GO_BACK("Go Back", "à¤«à¤°à¥à¤•à¤¨à¥à¤¹à¥‹à¤¸à¥"),

    // ============================================
    // EMPTY/ERROR STATES
    // ============================================
    NO_PROFILE_SELECTED("No Profile Selected", "à¤•à¥à¤¨à¥ˆ à¤ªà¥à¤°à¥‹à¤«à¤¾à¤‡à¤² à¤›à¤¾à¤¨à¤¿à¤à¤•à¥‹ à¤›à¥ˆà¤¨"),
    NO_PROFILE_MESSAGE("Select or create a profile to view your personalized astrological insights.", "à¤†à¤«à¥à¤¨à¥‹ à¤µà¥à¤¯à¤•à¥à¤¤à¤¿à¤—à¤¤ à¤œà¥à¤¯à¥‹à¤¤à¤¿à¤· à¤…à¤¨à¥à¤¤à¤°à¥à¤¦à¥ƒà¤·à¥à¤Ÿà¤¿ à¤¹à¥‡à¤°à¥à¤¨ à¤ªà¥à¤°à¥‹à¤«à¤¾à¤‡à¤² à¤›à¤¾à¤¨à¥à¤¨à¥à¤¹à¥‹à¤¸à¥ à¤µà¤¾ à¤¬à¤¨à¤¾à¤‰à¤¨à¥à¤¹à¥‹à¤¸à¥à¥¤"),
    NO_PROFILE_MESSAGE_LONG("Select or create a profile to view your personalized astrological insights and predictions", "à¤†à¤«à¥à¤¨à¥‹ à¤µà¥à¤¯à¤•à¥à¤¤à¤¿à¤—à¤¤ à¤œà¥à¤¯à¥‹à¤¤à¤¿à¤· à¤…à¤¨à¥à¤¤à¤°à¥à¤¦à¥ƒà¤·à¥à¤Ÿà¤¿ à¤° à¤­à¤µà¤¿à¤·à¥à¤¯à¤µà¤¾à¤£à¥€ à¤¹à¥‡à¤°à¥à¤¨ à¤ªà¥à¤°à¥‹à¤«à¤¾à¤‡à¤² à¤›à¤¾à¤¨à¥à¤¨à¥à¤¹à¥‹à¤¸à¥ à¤µà¤¾ à¤¬à¤¨à¤¾à¤‰à¤¨à¥à¤¹à¥‹à¤¸à¥"),

    // Error States
    ERROR_PARTIAL("Some insights unavailable", "à¤•à¥‡à¤¹à¥€ à¤…à¤¨à¥à¤¤à¤°à¥à¤¦à¥ƒà¤·à¥à¤Ÿà¤¿à¤¹à¤°à¥‚ à¤‰à¤ªà¤²à¤¬à¥à¤§ à¤›à¥ˆà¤¨à¤¨à¥"),
    ERROR_CALCULATIONS_FAILED("%d calculation(s) could not be completed", "%d à¤—à¤£à¤¨à¤¾(à¤¹à¤°à¥‚) à¤ªà¥‚à¤°à¤¾ à¤¹à¥à¤¨ à¤¸à¤•à¥‡à¤¨"),
    ERROR_UNABLE_TO_LOAD("Unable to Load Insights", "à¤…à¤¨à¥à¤¤à¤°à¥à¤¦à¥ƒà¤·à¥à¤Ÿà¤¿ à¤²à¥‹à¤¡ à¤—à¤°à¥à¤¨ à¤…à¤¸à¤®à¤°à¥à¤¥"),
    ERROR_HOROSCOPE_UNAVAILABLE("%s's horoscope unavailable", "%s à¤•à¥‹ à¤°à¤¾à¤¶à¤¿à¤«à¤² à¤‰à¤ªà¤²à¤¬à¥à¤§ à¤›à¥ˆà¤¨"),
    ERROR_EPHEMERIS_DATA("Unable to calculate planetary positions for this period. This may be due to ephemeris data limitations.", "à¤¯à¤¸ à¤…à¤µà¤§à¤¿à¤•à¥‹ à¤²à¤¾à¤—à¤¿ à¤—à¥à¤°à¤¹ à¤¸à¥à¤¥à¤¿à¤¤à¤¿à¤¹à¤°à¥‚ à¤—à¤£à¤¨à¤¾ à¤—à¤°à¥à¤¨ à¤…à¤¸à¤®à¤°à¥à¤¥à¥¤ à¤¯à¥‹ à¤ˆà¤«à¥‡à¤®à¥‡à¤°à¤¿à¤¸ à¤¡à¤¾à¤Ÿà¤¾ à¤¸à¥€à¤®à¤¿à¤¤à¤¤à¤¾à¤•à¥‹ à¤•à¤¾à¤°à¤£ à¤¹à¥à¤¨ à¤¸à¤•à¥à¤›à¥¤"),
    ERROR_SOMETHING_WRONG("Something went wrong", "à¤•à¥‡à¤¹à¥€ à¤—à¤²à¤¤ à¤­à¤¯à¥‹"),

    // ============================================
    // BUTTONS & ACTIONS
    // ============================================
    BTN_RETRY("Retry", "à¤ªà¥à¤¨à¤ƒ à¤ªà¥à¤°à¤¯à¤¾à¤¸"),
    BTN_TRY_AGAIN("Try Again", "à¤«à¥‡à¤°à¤¿ à¤ªà¥à¤°à¤¯à¤¾à¤¸ à¤—à¤°à¥à¤¨à¥à¤¹à¥‹à¤¸à¥"),
    BTN_PREVIEW("Preview", "à¤ªà¥‚à¤°à¥à¤µà¤¾à¤µà¤²à¥‹à¤•à¤¨"),
    BTN_OK("OK", "à¤ à¥€à¤• à¤›"),
    BTN_CANCEL("Cancel", "à¤°à¤¦à¥à¤¦ à¤—à¤°à¥à¤¨à¥à¤¹à¥‹à¤¸à¥"),
    BTN_DELETE("Delete", "à¤®à¥‡à¤Ÿà¥à¤¨à¥à¤¹à¥‹à¤¸à¥"),
    BTN_EDIT("Edit", "à¤¸à¤®à¥à¤ªà¤¾à¤¦à¤¨ à¤—à¤°à¥à¤¨à¥à¤¹à¥‹à¤¸à¥"),
    BTN_SAVE("Save", "à¤¸à¥‡à¤­ à¤—à¤°à¥à¤¨à¥à¤¹à¥‹à¤¸à¥"),
    BTN_GENERATE("Generate", "à¤‰à¤¤à¥à¤ªà¤¨à¥à¤¨ à¤—à¤°à¥à¤¨à¥à¤¹à¥‹à¤¸à¥"),
    BTN_GENERATE_SAVE("Generate & Save", "à¤‰à¤¤à¥à¤ªà¤¨à¥à¤¨ à¤—à¤°à¥à¤¨à¥à¤¹à¥‹à¤¸à¥ à¤° à¤¸à¥‡à¤­ à¤—à¤°à¥à¤¨à¥à¤¹à¥‹à¤¸à¥"),
    BTN_UPDATE_SAVE("Update & Save", "à¤…à¤ªà¤¡à¥‡à¤Ÿ à¤—à¤°à¥à¤¨à¥à¤¹à¥‹à¤¸à¥ à¤° à¤¸à¥‡à¤­ à¤—à¤°à¥à¤¨à¥à¤¹à¥‹à¤¸à¥"),
    BTN_GO_BACK("Go back", "à¤ªà¤›à¤¾à¤¡à¤¿ à¤œà¤¾à¤¨à¥à¤¹à¥‹à¤¸à¥"),
    BTN_BACK("Back", "à¤ªà¤›à¤¾à¤¡à¤¿"),
    BTN_CLOSE("Close", "à¤¬à¤¨à¥à¤¦ à¤—à¤°à¥à¤¨à¥à¤¹à¥‹à¤¸à¥"),
    BTN_REFRESH("Refresh", "à¤°à¤¿à¤«à¥à¤°à¥‡à¤¸"),
    BTN_CREATE_CHART("Create Chart", "à¤•à¥à¤£à¥à¤¡à¤²à¥€ à¤¬à¤¨à¤¾à¤‰à¤¨à¥à¤¹à¥‹à¤¸à¥"),

    // ============================================
    // INSIGHTS TAB
    // ============================================
    INSIGHTS_OVERALL_ENERGY("Overall Energy", "à¤¸à¤®à¤—à¥à¤° à¤Šà¤°à¥à¤œà¤¾"),
    INSIGHTS_LIFE_AREAS("Life Areas", "à¤œà¥€à¤µà¤¨ à¤•à¥à¤·à¥‡à¤¤à¥à¤°à¤¹à¤°à¥‚"),
    INSIGHTS_LUCKY_ELEMENTS("Lucky Elements", "à¤­à¤¾à¤—à¥à¤¯à¤¶à¤¾à¤²à¥€ à¤¤à¤¤à¥à¤µà¤¹à¤°à¥‚"),
    INSIGHTS_TODAYS_AFFIRMATION("Today's Affirmation", "à¤†à¤œà¤•à¥‹ à¤ªà¥à¤°à¤¤à¤¿à¤œà¥à¤žà¤¾"),
    INSIGHTS_WEEKLY_ENERGY("Weekly Energy Flow", "à¤¸à¤¾à¤ªà¥à¤¤à¤¾à¤¹à¤¿à¤• à¤Šà¤°à¥à¤œà¤¾ à¤ªà¥à¤°à¤µà¤¾à¤¹"),
    INSIGHTS_KEY_DATES("Key Dates", "à¤®à¤¹à¤¤à¥à¤¤à¥à¤µà¤ªà¥‚à¤°à¥à¤£ à¤®à¤¿à¤¤à¤¿à¤¹à¤°à¥‚"),
    INSIGHTS_WEEKLY_OVERVIEW("Weekly Overview by Area", "à¤•à¥à¤·à¥‡à¤¤à¥à¤°à¤…à¤¨à¥à¤¸à¤¾à¤° à¤¸à¤¾à¤ªà¥à¤¤à¤¾à¤¹à¤¿à¤• à¤…à¤µà¤²à¥‹à¤•à¤¨"),
    INSIGHTS_WEEKLY_ADVICE("Weekly Advice", "à¤¸à¤¾à¤ªà¥à¤¤à¤¾à¤¹à¤¿à¤• à¤¸à¤²à¥à¤²à¤¾à¤¹"),
    INSIGHTS_RECOMMENDATIONS("Recommendations", "à¤¸à¤¿à¤«à¤¾à¤°à¤¿à¤¸à¤¹à¤°à¥‚"),
    INSIGHTS_CAUTIONS("Cautions", "à¤¸à¤¾à¤µà¤§à¤¾à¤¨à¥€à¤¹à¤°à¥‚"),

    // Horoscope periods
    PERIOD_TODAY("Today", "à¤†à¤œ"),
    PERIOD_TOMORROW("Tomorrow", "à¤­à¥‹à¤²à¤¿"),
    PERIOD_WEEKLY("Weekly", "à¤¸à¤¾à¤ªà¥à¤¤à¤¾à¤¹à¤¿à¤•"),

    // Lucky elements labels
    LUCKY_NUMBER("Number", "à¤…à¤‚à¤•"),
    LUCKY_COLOR("Color", "à¤°à¤‚à¤—"),
    LUCKY_DIRECTION("Direction", "à¤¦à¤¿à¤¶à¤¾"),
    LUCKY_GEMSTONE("Gemstone", "à¤°à¤¤à¥à¤¨"),

    // ============================================
    // DASHA SECTION
    // ============================================
    DASHA_CURRENT_PERIOD("Current Planetary Period", "à¤¹à¤¾à¤²à¤•à¥‹ à¤—à¥à¤°à¤¹ à¤…à¤µà¤§à¤¿"),
    DASHA_ACTIVE("Active", "à¤¸à¤•à¥à¤°à¤¿à¤¯"),
    DASHA_MAHADASHA("Mahadasha", "à¤®à¤¹à¤¾à¤¦à¤¶à¤¾"),
    DASHA_ANTARDASHA("Antardasha", "à¤…à¤¨à¥à¤¤à¤°à¥à¤¦à¤¶à¤¾"),
    DASHA_PRATYANTARDASHA("Pratyantardasha:", "à¤ªà¥à¤°à¤¤à¥à¤¯à¤¨à¥à¤¤à¤°à¥à¤¦à¤¶à¤¾:"),
    DASHA_UPCOMING("Upcoming Periods", "à¤†à¤—à¤¾à¤®à¥€ à¤…à¤µà¤§à¤¿à¤¹à¤°à¥‚"),
    DASHA_REMAINING("remaining", "à¤¬à¤¾à¤à¤•à¥€"),
    DASHA_LAST_IN_MAHADASHA("Current Antardasha is the last in this Mahadasha", "à¤¹à¤¾à¤²à¤•à¥‹ à¤…à¤¨à¥à¤¤à¤°à¥à¤¦à¤¶à¤¾ à¤¯à¤¸ à¤®à¤¹à¤¾à¤¦à¤¶à¤¾à¤•à¥‹ à¤…à¤¨à¥à¤¤à¤¿à¤® à¤¹à¥‹"),
    DASHA_STARTS("Starts %s", "%s à¤®à¤¾ à¤¸à¥à¤°à¥ à¤¹à¥à¤¨à¥à¤›"),
    DASHA_VIMSHOTTARI("Vimshottari Dasha", "à¤µà¤¿à¤®à¥à¤¶à¥‹à¤¤à¥à¤¤à¤°à¥€ à¤¦à¤¶à¤¾"),
    DASHA_JUMP_TO_TODAY("Jump to current period", "à¤¹à¤¾à¤²à¤•à¥‹ à¤…à¤µà¤§à¤¿à¤®à¤¾ à¤œà¤¾à¤¨à¥à¤¹à¥‹à¤¸à¥"),
    DASHA_CALCULATING("Calculating...", "à¤—à¤£à¤¨à¤¾ à¤—à¤°à¥à¤¦à¥ˆ..."),
    DASHA_ERROR("Error", "à¤¤à¥à¤°à¥à¤Ÿà¤¿"),
    DASHA_CALCULATING_TIMELINE("Calculating Dasha Timeline", "à¤¦à¤¶à¤¾ à¤¸à¤®à¤¯à¤°à¥‡à¤–à¤¾ à¤—à¤£à¤¨à¤¾ à¤—à¤°à¥à¤¦à¥ˆ"),
    DASHA_CALCULATING_DESC("Computing planetary periods based on\nMoon's Nakshatra position...", "à¤šà¤¨à¥à¤¦à¥à¤°à¤•à¥‹ à¤¨à¤•à¥à¤·à¤¤à¥à¤° à¤¸à¥à¤¥à¤¿à¤¤à¤¿à¤•à¥‹ à¤†à¤§à¤¾à¤°à¤®à¤¾\nà¤—à¥à¤°à¤¹ à¤…à¤µà¤§à¤¿à¤¹à¤°à¥‚ à¤—à¤£à¤¨à¤¾ à¤—à¤°à¥à¤¦à¥ˆ..."),
    DASHA_CALCULATION_FAILED("Calculation Failed", "à¤—à¤£à¤¨à¤¾ à¤…à¤¸à¤«à¤² à¤­à¤¯à¥‹"),
    DASHA_NO_CHART_SELECTED("No Chart Selected", "à¤•à¥à¤¨à¥ˆ à¤•à¥à¤£à¥à¤¡à¤²à¥€ à¤›à¤¾à¤¨à¤¿à¤à¤•à¥‹ à¤›à¥ˆà¤¨"),
    DASHA_NO_CHART_MESSAGE("Please select or create a birth profile\nto view the Dasha timeline.", "à¤¦à¤¶à¤¾ à¤¸à¤®à¤¯à¤°à¥‡à¤–à¤¾ à¤¹à¥‡à¤°à¥à¤¨ à¤•à¥ƒà¤ªà¤¯à¤¾ à¤œà¤¨à¥à¤® à¤ªà¥à¤°à¥‹à¤«à¤¾à¤‡à¤²\nà¤›à¤¾à¤¨à¥à¤¨à¥à¤¹à¥‹à¤¸à¥ à¤µà¤¾ à¤¬à¤¨à¤¾à¤‰à¤¨à¥à¤¹à¥‹à¤¸à¥à¥¤"),

    // Dasha Level Names
    DASHA_SOOKSHMADASHA("Sookshmadasha", "à¤¸à¥‚à¤•à¥à¤·à¥à¤®à¤¦à¤¶à¤¾"),
    DASHA_PRANADASHA("Pranadasha", "à¤ªà¥à¤°à¤¾à¤£à¤¦à¤¶à¤¾"),
    DASHA_DEHADASHA("Dehadasha", "à¤¦à¥‡à¤¹à¤¦à¤¶à¤¾"),
    DASHA_BHUKTI("Bhukti", "à¤­à¥à¤•à¥à¤¤à¤¿"),
    DASHA_PRATYANTAR("Pratyantar", "à¤ªà¥à¤°à¤¤à¥à¤¯à¤¨à¥à¤¤à¤°"),
    DASHA_SOOKSHMA("Sookshma", "à¤¸à¥‚à¤•à¥à¤·à¥à¤®"),
    DASHA_PRANA("Prana", "à¤ªà¥à¤°à¤¾à¤£"),
    DASHA_DEHA("Deha", "à¤¦à¥‡à¤¹"),

    // Dasha Format Labels
    DASHA_DURATION("Duration", "à¤…à¤µà¤§à¤¿"),
    DASHA_PERIOD("Period", "à¤…à¤µà¤§à¤¿"),
    DASHA_STATUS("Status", "à¤¸à¥à¤¥à¤¿à¤¤à¤¿"),
    DASHA_CURRENTLY_ACTIVE("Currently Active", "à¤¹à¤¾à¤² à¤¸à¤•à¥à¤°à¤¿à¤¯"),
    DASHA_PROGRESS("Progress", "à¤ªà¥à¤°à¤—à¤¤à¤¿"),
    DASHA_NO_ACTIVE_PERIOD("No active Dasha period", "à¤•à¥à¤¨à¥ˆ à¤¸à¤•à¥à¤°à¤¿à¤¯ à¤¦à¤¶à¤¾ à¤…à¤µà¤§à¤¿ à¤›à¥ˆà¤¨"),

    // Time Units
    YEARS("years", "à¤µà¤°à¥à¤·"),
    DAYS("days", "à¤¦à¤¿à¤¨"),
    TO("to", "à¤¦à¥‡à¤–à¤¿"),
    DAYS_SHORT("d", "à¤¦à¤¿"),
    HOURS_SHORT("h", "à¤˜"),
    MINUTES_SHORT("m", "à¤®à¤¿"),

    // Yogini Dasha Names
    YOGINI_MANGALA("Mangala", "à¤®à¤™à¥à¤—à¤²à¤¾"),
    YOGINI_PINGALA("Pingala", "à¤ªà¤¿à¤™à¥à¤—à¤²à¤¾"),
    YOGINI_DHANYA("Dhanya", "à¤§à¤¨à¥à¤¯à¤¾"),
    YOGINI_BHRAMARI("Bhramari", "à¤­à¥à¤°à¤¾à¤®à¤°à¥€"),
    YOGINI_BHADRIKA("Bhadrika", "à¤­à¤¦à¥à¤°à¤¿à¤•à¤¾"),
    YOGINI_ULKA("Ulka", "à¤‰à¤²à¥à¤•à¤¾"),
    YOGINI_SIDDHA("Siddha", "à¤¸à¤¿à¤¦à¥à¤§à¤¾"),
    YOGINI_SANKATA("Sankata", "à¤¸à¤™à¥à¤•à¤Ÿà¤¾"),

    // Yogini Deity Names
    YOGINI_DEITY_CHANDRA("Chandra (Moon)", "à¤šà¤¨à¥à¤¦à¥à¤°"),
    YOGINI_DEITY_SURYA("Surya (Sun)", "à¤¸à¥‚à¤°à¥à¤¯"),
    YOGINI_DEITY_GURU("Guru (Jupiter)", "à¤—à¥à¤°à¥ (à¤¬à¥ƒà¤¹à¤¸à¥à¤ªà¤¤à¤¿)"),
    YOGINI_DEITY_MANGAL("Mangal (Mars)", "à¤®à¤™à¥à¤—à¤²"),
    YOGINI_DEITY_BUDHA("Budha (Mercury)", "à¤¬à¥à¤§"),
    YOGINI_DEITY_SHANI("Shani (Saturn)", "à¤¶à¤¨à¤¿"),
    YOGINI_DEITY_SHUKRA("Shukra (Venus)", "à¤¶à¥à¤•à¥à¤°"),
    YOGINI_DEITY_RAHU("Rahu", "à¤°à¤¾à¤¹à¥"),

    // Nature Types
    NATURE_BENEFIC("Benefic", "à¤¶à¥à¤­"),
    NATURE_MALEFIC("Malefic", "à¤…à¤¶à¥à¤­"),
    NATURE_MIXED("Mixed", "à¤®à¤¿à¤¶à¥à¤°à¤¿à¤¤"),

    // ============================================
    // TRANSITS
    // ============================================
    TRANSITS_CURRENT("Current Transits", "à¤¹à¤¾à¤²à¤•à¤¾ à¤—à¥‹à¤šà¤°à¤¹à¤°à¥‚"),
    TRANSITS_MOON_IN("Moon in %s", "à¤šà¤¨à¥à¤¦à¥à¤°à¤®à¤¾ %s à¤®à¤¾"),

    // ============================================
    // ENERGY DESCRIPTIONS
    // ============================================
    ENERGY_EXCEPTIONAL("Exceptional cosmic alignment - seize every opportunity!", "à¤…à¤¸à¤¾à¤§à¤¾à¤°à¤£ à¤¬à¥à¤°à¤¹à¥à¤®à¤¾à¤£à¥à¤¡à¥€à¤¯ à¤¸à¤‚à¤°à¥‡à¤–à¤£ - à¤¹à¤°à¥‡à¤• à¤…à¤µà¤¸à¤° à¤¸à¤®à¤¾à¤¤à¥à¤¨à¥à¤¹à¥‹à¤¸à¥!"),
    ENERGY_EXCELLENT("Excellent day ahead - favorable for important decisions", "à¤‰à¤¤à¥à¤•à¥ƒà¤·à¥à¤Ÿ à¤¦à¤¿à¤¨ à¤…à¤—à¤¾à¤¡à¤¿ - à¤®à¤¹à¤¤à¥à¤¤à¥à¤µà¤ªà¥‚à¤°à¥à¤£ à¤¨à¤¿à¤°à¥à¤£à¤¯à¤¹à¤°à¥‚à¤•à¥‹ à¤²à¤¾à¤—à¤¿ à¤…à¤¨à¥à¤•à¥‚à¤²"),
    ENERGY_STRONG("Strong positive energy - good for new initiatives", "à¤¬à¤²à¤¿à¤¯à¥‹ à¤¸à¤•à¤¾à¤°à¤¾à¤¤à¥à¤®à¤• à¤Šà¤°à¥à¤œà¤¾ - à¤¨à¤¯à¤¾à¤ à¤ªà¤¹à¤²à¤¹à¤°à¥‚à¤•à¥‹ à¤²à¤¾à¤—à¤¿ à¤°à¤¾à¤®à¥à¤°à¥‹"),
    ENERGY_FAVORABLE("Favorable energy - maintain steady progress", "à¤…à¤¨à¥à¤•à¥‚à¤² à¤Šà¤°à¥à¤œà¤¾ - à¤¸à¥à¤¥à¤¿à¤° à¤ªà¥à¤°à¤—à¤¤à¤¿ à¤•à¤¾à¤¯à¤® à¤°à¤¾à¤–à¥à¤¨à¥à¤¹à¥‹à¤¸à¥"),
    ENERGY_BALANCED("Balanced energy - stay centered and focused", "à¤¸à¤¨à¥à¤¤à¥à¤²à¤¿à¤¤ à¤Šà¤°à¥à¤œà¤¾ - à¤•à¥‡à¤¨à¥à¤¦à¥à¤°à¤¿à¤¤ à¤° à¤§à¥à¤¯à¤¾à¤¨ à¤•à¥‡à¤¨à¥à¤¦à¥à¤°à¤¿à¤¤ à¤°à¤¹à¤¨à¥à¤¹à¥‹à¤¸à¥"),
    ENERGY_MODERATE("Moderate energy - pace yourself wisely", "à¤®à¤§à¥à¤¯à¤® à¤Šà¤°à¥à¤œà¤¾ - à¤¬à¥à¤¦à¥à¤§à¤¿à¤®à¤¾à¤¨à¥€à¤ªà¥‚à¤°à¥à¤µà¤• à¤†à¤«à¥à¤¨à¥‹ à¤—à¤¤à¤¿ à¤®à¤¿à¤²à¤¾à¤‰à¤¨à¥à¤¹à¥‹à¤¸à¥"),
    ENERGY_LOWER("Lower energy day - prioritize rest and reflection", "à¤•à¤® à¤Šà¤°à¥à¤œà¤¾ à¤¦à¤¿à¤¨ - à¤†à¤°à¤¾à¤® à¤° à¤šà¤¿à¤¨à¥à¤¤à¤¨à¤²à¤¾à¤ˆ à¤ªà¥à¤°à¤¾à¤¥à¤®à¤¿à¤•à¤¤à¤¾ à¤¦à¤¿à¤¨à¥à¤¹à¥‹à¤¸à¥"),
    ENERGY_CHALLENGING("Challenging day - practice patience and self-care", "à¤šà¥à¤¨à¥Œà¤¤à¥€à¤ªà¥‚à¤°à¥à¤£ à¤¦à¤¿à¤¨ - à¤§à¥ˆà¤°à¥à¤¯ à¤° à¤†à¤¤à¥à¤®-à¤¹à¥‡à¤°à¤šà¤¾à¤¹ à¤…à¤­à¥à¤¯à¤¾à¤¸ à¤—à¤°à¥à¤¨à¥à¤¹à¥‹à¤¸à¥"),
    ENERGY_REST("Rest and recharge recommended - avoid major decisions", "à¤†à¤°à¤¾à¤® à¤° à¤°à¤¿à¤šà¤¾à¤°à¥à¤œ à¤¸à¤¿à¤«à¤¾à¤°à¤¿à¤¸ - à¤ªà¥à¤°à¤®à¥à¤– à¤¨à¤¿à¤°à¥à¤£à¤¯à¤¹à¤°à¥‚à¤¬à¤¾à¤Ÿ à¤¬à¤šà¥à¤¨à¥à¤¹à¥‹à¤¸à¥"),

    // ============================================
    // LIFE AREAS
    // ============================================
    LIFE_AREA_CAREER("Career", "à¤•à¥à¤¯à¤¾à¤°à¤¿à¤¯à¤°"),
    LIFE_AREA_LOVE("Love", "à¤ªà¥à¤°à¥‡à¤®"),
    LIFE_AREA_HEALTH("Health", "à¤¸à¥à¤µà¤¾à¤¸à¥à¤¥à¥à¤¯"),
    LIFE_AREA_FINANCE("Finance", "à¤µà¤¿à¤¤à¥à¤¤"),
    LIFE_AREA_FAMILY("Family", "à¤ªà¤°à¤¿à¤µà¤¾à¤°"),
    LIFE_AREA_SPIRITUALITY("Spirituality", "à¤†à¤§à¥à¤¯à¤¾à¤¤à¥à¤®à¤¿à¤•à¤¤à¤¾"),

    // Life Area Full Display Names (with descriptions)
    LIFE_AREA_CAREER_FULL("Career", "à¤•à¥à¤¯à¤¾à¤°à¤¿à¤¯à¤°"),
    LIFE_AREA_LOVE_FULL("Love & Relationships", "à¤ªà¥à¤°à¥‡à¤® à¤° à¤¸à¤®à¥à¤¬à¤¨à¥à¤§"),
    LIFE_AREA_HEALTH_FULL("Health & Vitality", "à¤¸à¥à¤µà¤¾à¤¸à¥à¤¥à¥à¤¯ à¤° à¤œà¥€à¤µà¤¨à¤¶à¤•à¥à¤¤à¤¿"),
    LIFE_AREA_FINANCE_FULL("Finance & Wealth", "à¤µà¤¿à¤¤à¥à¤¤ à¤° à¤¸à¤®à¥à¤ªà¤¤à¥à¤¤à¤¿"),
    LIFE_AREA_FAMILY_FULL("Family & Home", "à¤ªà¤°à¤¿à¤µà¤¾à¤° à¤° à¤˜à¤°"),
    LIFE_AREA_SPIRITUALITY_FULL("Spiritual Growth", "à¤†à¤§à¥à¤¯à¤¾à¤¤à¥à¤®à¤¿à¤• à¤µà¥ƒà¤¦à¥à¤§à¤¿"),

    // ============================================
    // HOROSCOPE THEMES
    // ============================================
    THEME_DYNAMIC_ACTION("Dynamic Action", "à¤—à¤¤à¤¿à¤¶à¥€à¤² à¤•à¤¾à¤°à¥à¤¯"),
    THEME_PRACTICAL_PROGRESS("Practical Progress", "à¤µà¥à¤¯à¤¾à¤µà¤¹à¤¾à¤°à¤¿à¤• à¤ªà¥à¤°à¤—à¤¤à¤¿"),
    THEME_SOCIAL_CONNECTIONS("Social Connections", "à¤¸à¤¾à¤®à¤¾à¤œà¤¿à¤• à¤¸à¤®à¥à¤¬à¤¨à¥à¤§à¤¹à¤°à¥‚"),
    THEME_EMOTIONAL_INSIGHT("Emotional Insight", "à¤­à¤¾à¤µà¤¨à¤¾à¤¤à¥à¤®à¤• à¤…à¤¨à¥à¤¤à¤°à¥à¤¦à¥ƒà¤·à¥à¤Ÿà¤¿"),
    THEME_EXPANSION_WISDOM("Expansion & Wisdom", "à¤µà¤¿à¤¸à¥à¤¤à¤¾à¤° à¤° à¤œà¥à¤žà¤¾à¤¨"),
    THEME_HARMONY_BEAUTY("Harmony & Beauty", "à¤¸à¤¾à¤®à¤žà¥à¤œà¤¸à¥à¤¯ à¤° à¤¸à¥Œà¤¨à¥à¤¦à¤°à¥à¤¯"),
    THEME_DISCIPLINE_GROWTH("Discipline & Growth", "à¤…à¤¨à¥à¤¶à¤¾à¤¸à¤¨ à¤° à¤µà¥ƒà¤¦à¥à¤§à¤¿"),
    THEME_COMMUNICATION_LEARNING("Communication & Learning", "à¤¸à¤žà¥à¤šà¤¾à¤° à¤° à¤¸à¤¿à¤•à¤¾à¤‡"),
    THEME_ENERGY_INITIATIVE("Energy & Initiative", "à¤Šà¤°à¥à¤œà¤¾ à¤° à¤ªà¤¹à¤²"),
    THEME_SELF_EXPRESSION("Self-Expression", "à¤†à¤¤à¥à¤®-à¤…à¤­à¤¿à¤µà¥à¤¯à¤•à¥à¤¤à¤¿"),
    THEME_INTUITION_NURTURING("Intuition & Nurturing", "à¤…à¤¨à¥à¤¤à¤°à¥à¤œà¥à¤žà¤¾à¤¨ à¤° à¤ªà¤¾à¤²à¤¨-à¤ªà¥‹à¤·à¤£"),
    THEME_TRANSFORMATION("Transformation", "à¤°à¥‚à¤ªà¤¾à¤¨à¥à¤¤à¤°à¤£"),
    THEME_SPIRITUAL_LIBERATION("Spiritual Liberation", "à¤†à¤§à¥à¤¯à¤¾à¤¤à¥à¤®à¤¿à¤• à¤®à¥à¤•à¥à¤¤à¤¿"),
    THEME_BALANCE_EQUILIBRIUM("Balance & Equilibrium", "à¤¸à¤¨à¥à¤¤à¥à¤²à¤¨ à¤° à¤¸à¤®à¤¾à¤¨à¤¤à¤¾"),

    // Theme Descriptions
    THEME_DESC_DYNAMIC_ACTION(
        "Your energy is high and aligned with fire elements. This is an excellent day for taking initiative, starting new projects, and asserting yourself confidently. Channel this vibrant energy into productive pursuits.",
        "à¤¤à¤ªà¤¾à¤ˆà¤‚à¤•à¥‹ à¤Šà¤°à¥à¤œà¤¾ à¤‰à¤šà¥à¤š à¤› à¤° à¤…à¤—à¥à¤¨à¤¿ à¤¤à¤¤à¥à¤µà¤¹à¤°à¥‚à¤¸à¤à¤— à¤®à¤¿à¤²à¥‡à¤•à¥‹ à¤›à¥¤ à¤¯à¥‹ à¤ªà¤¹à¤² à¤²à¤¿à¤¨, à¤¨à¤¯à¤¾à¤ à¤ªà¤°à¤¿à¤¯à¥‹à¤œà¤¨à¤¾à¤¹à¤°à¥‚ à¤¸à¥à¤°à¥ à¤—à¤°à¥à¤¨, à¤° à¤†à¤¤à¥à¤®à¤µà¤¿à¤¶à¥à¤µà¤¾à¤¸à¤•à¤¾ à¤¸à¤¾à¤¥ à¤†à¤«à¥‚à¤²à¤¾à¤ˆ à¤ªà¥à¤°à¤¸à¥à¤¤à¥à¤¤ à¤—à¤°à¥à¤¨ à¤‰à¤¤à¥à¤•à¥ƒà¤·à¥à¤Ÿ à¤¦à¤¿à¤¨ à¤¹à¥‹à¥¤ à¤¯à¥‹ à¤œà¥€à¤µà¤¨à¥à¤¤ à¤Šà¤°à¥à¤œà¤¾à¤²à¤¾à¤ˆ à¤‰à¤¤à¥à¤ªà¤¾à¤¦à¤• à¤•à¤¾à¤°à¥à¤¯à¤¹à¤°à¥‚à¤®à¤¾ à¤ªà¥à¤°à¤¯à¥‹à¤— à¤—à¤°à¥à¤¨à¥à¤¹à¥‹à¤¸à¥à¥¤"
    ),
    THEME_DESC_PRACTICAL_PROGRESS(
        "Grounded earth energy supports methodical progress today. Focus on practical tasks, financial planning, and building stable foundations. Your efforts will yield tangible results.",
        "à¤­à¥‚à¤®à¤¿à¤—à¤¤ à¤ªà¥ƒà¤¥à¥à¤µà¥€ à¤Šà¤°à¥à¤œà¤¾à¤²à¥‡ à¤†à¤œ à¤µà¥à¤¯à¤µà¤¸à¥à¤¥à¤¿à¤¤ à¤ªà¥à¤°à¤—à¤¤à¤¿à¤²à¤¾à¤ˆ à¤¸à¤®à¤°à¥à¤¥à¤¨ à¤—à¤°à¥à¤›à¥¤ à¤µà¥à¤¯à¤¾à¤µà¤¹à¤¾à¤°à¤¿à¤• à¤•à¤¾à¤°à¥à¤¯à¤¹à¤°à¥‚, à¤µà¤¿à¤¤à¥à¤¤à¥€à¤¯ à¤¯à¥‹à¤œà¤¨à¤¾, à¤° à¤¸à¥à¤¥à¤¿à¤° à¤†à¤§à¤¾à¤°à¤¹à¤°à¥‚ à¤¨à¤¿à¤°à¥à¤®à¤¾à¤£ à¤—à¤°à¥à¤¨à¤®à¤¾ à¤§à¥à¤¯à¤¾à¤¨ à¤¦à¤¿à¤¨à¥à¤¹à¥‹à¤¸à¥à¥¤ à¤¤à¤ªà¤¾à¤ˆà¤‚à¤•à¥‹ à¤ªà¥à¤°à¤¯à¤¾à¤¸à¤²à¥‡ à¤ à¥‹à¤¸ à¤ªà¤°à¤¿à¤£à¤¾à¤®à¤¹à¤°à¥‚ à¤¦à¤¿à¤¨à¥‡à¤›à¥¤"
    ),
    THEME_DESC_SOCIAL_CONNECTIONS(
        "Air element energy enhances communication and social interactions. Networking, negotiations, and intellectual pursuits are favored. Express your ideas and connect with like-minded people.",
        "à¤µà¤¾à¤¯à¥ à¤¤à¤¤à¥à¤µ à¤Šà¤°à¥à¤œà¤¾à¤²à¥‡ à¤¸à¤žà¥à¤šà¤¾à¤° à¤° à¤¸à¤¾à¤®à¤¾à¤œà¤¿à¤• à¤…à¤¨à¥à¤¤à¤°à¤•à¥à¤°à¤¿à¤¯à¤¾à¤¹à¤°à¥‚à¤²à¤¾à¤ˆ à¤¬à¤¢à¤¾à¤‰à¤à¤›à¥¤ à¤¨à¥‡à¤Ÿà¤µà¤°à¥à¤•à¤¿à¤™, à¤µà¤¾à¤°à¥à¤¤à¤¾, à¤° à¤¬à¥Œà¤¦à¥à¤§à¤¿à¤• à¤ªà¥à¤°à¤¯à¤¾à¤¸à¤¹à¤°à¥‚ à¤…à¤¨à¥à¤•à¥‚à¤² à¤›à¤¨à¥à¥¤ à¤†à¤«à¥à¤¨à¤¾ à¤µà¤¿à¤šà¤¾à¤°à¤¹à¤°à¥‚ à¤µà¥à¤¯à¤•à¥à¤¤ à¤—à¤°à¥à¤¨à¥à¤¹à¥‹à¤¸à¥ à¤° à¤¸à¤®à¤¾à¤¨ à¤µà¤¿à¤šà¤¾à¤° à¤­à¤à¤•à¤¾ à¤®à¤¾à¤¨à¤¿à¤¸à¤¹à¤°à¥‚à¤¸à¤à¤— à¤œà¥‹à¤¡à¤¿à¤¨à¥à¤¹à¥‹à¤¸à¥à¥¤"
    ),
    THEME_DESC_EMOTIONAL_INSIGHT(
        "Water element energy deepens your intuition and emotional awareness. Trust your feelings and pay attention to subtle cues. This is a powerful day for healing and self-reflection.",
        "à¤œà¤² à¤¤à¤¤à¥à¤µ à¤Šà¤°à¥à¤œà¤¾à¤²à¥‡ à¤¤à¤ªà¤¾à¤ˆà¤‚à¤•à¥‹ à¤…à¤¨à¥à¤¤à¤°à¥à¤œà¥à¤žà¤¾à¤¨ à¤° à¤­à¤¾à¤µà¤¨à¤¾à¤¤à¥à¤®à¤• à¤œà¤¾à¤—à¤°à¥‚à¤•à¤¤à¤¾ à¤—à¤¹à¤¿à¤°à¥‹ à¤¬à¤¨à¤¾à¤‰à¤à¤›à¥¤ à¤†à¤«à¥à¤¨à¤¾ à¤­à¤¾à¤µà¤¨à¤¾à¤¹à¤°à¥‚à¤®à¤¾à¤¥à¤¿ à¤µà¤¿à¤¶à¥à¤µà¤¾à¤¸ à¤—à¤°à¥à¤¨à¥à¤¹à¥‹à¤¸à¥ à¤° à¤¸à¥‚à¤•à¥à¤·à¥à¤® à¤¸à¤‚à¤•à¥‡à¤¤à¤¹à¤°à¥‚à¤®à¤¾ à¤§à¥à¤¯à¤¾à¤¨ à¤¦à¤¿à¤¨à¥à¤¹à¥‹à¤¸à¥à¥¤ à¤¯à¥‹ à¤‰à¤ªà¤šà¤¾à¤° à¤° à¤†à¤¤à¥à¤®-à¤šà¤¿à¤¨à¥à¤¤à¤¨à¤•à¥‹ à¤²à¤¾à¤—à¤¿ à¤¶à¤•à¥à¤¤à¤¿à¤¶à¤¾à¤²à¥€ à¤¦à¤¿à¤¨ à¤¹à¥‹à¥¤"
    ),
    THEME_DESC_EXPANSION_WISDOM(
        "Jupiter's benevolent influence brings opportunities for growth, learning, and good fortune. Be open to new possibilities and share your wisdom generously.",
        "à¤¬à¥ƒà¤¹à¤¸à¥à¤ªà¤¤à¤¿à¤•à¥‹ à¤‰à¤¦à¤¾à¤° à¤ªà¥à¤°à¤­à¤¾à¤µà¤²à¥‡ à¤µà¥ƒà¤¦à¥à¤§à¤¿, à¤¸à¤¿à¤•à¤¾à¤‡ à¤° à¤¸à¥Œà¤­à¤¾à¤—à¥à¤¯à¤•à¤¾ à¤…à¤µà¤¸à¤°à¤¹à¤°à¥‚ à¤²à¥à¤¯à¤¾à¤‰à¤à¤›à¥¤ à¤¨à¤¯à¤¾à¤ à¤¸à¤®à¥à¤­à¤¾à¤µà¤¨à¤¾à¤¹à¤°à¥‚à¤•à¥‹ à¤²à¤¾à¤—à¤¿ à¤–à¥à¤²à¤¾ à¤¹à¥à¤¨à¥à¤¹à¥‹à¤¸à¥ à¤° à¤†à¤«à¥à¤¨à¥‹ à¤œà¥à¤žà¤¾à¤¨ à¤‰à¤¦à¤¾à¤°à¤¤à¤¾à¤ªà¥‚à¤°à¥à¤µà¤• à¤¸à¤¾à¤à¤¾ à¤—à¤°à¥à¤¨à¥à¤¹à¥‹à¤¸à¥à¥¤"
    ),
    THEME_DESC_HARMONY_BEAUTY(
        "Venus graces you with appreciation for beauty, art, and relationships. Indulge in pleasurable activities and nurture your connections with loved ones.",
        "à¤¶à¥à¤•à¥à¤°à¤²à¥‡ à¤¤à¤ªà¤¾à¤ˆà¤‚à¤²à¤¾à¤ˆ à¤¸à¥Œà¤¨à¥à¤¦à¤°à¥à¤¯, à¤•à¤²à¤¾ à¤° à¤¸à¤®à¥à¤¬à¤¨à¥à¤§à¤¹à¤°à¥‚à¤•à¥‹ à¤ªà¥à¤°à¤¶à¤‚à¤¸à¤¾à¤•à¥‹ à¤µà¤°à¤¦à¤¾à¤¨ à¤¦à¤¿à¤¨à¥à¤›à¥¤ à¤°à¤®à¤¾à¤‡à¤²à¥‹ à¤—à¤¤à¤¿à¤µà¤¿à¤§à¤¿à¤¹à¤°à¥‚à¤®à¤¾ à¤¸à¤‚à¤²à¤—à¥à¤¨ à¤¹à¥à¤¨à¥à¤¹à¥‹à¤¸à¥ à¤° à¤†à¤«à¥à¤¨à¤¾ à¤ªà¥à¤°à¤¿à¤¯à¤œà¤¨à¤¹à¤°à¥‚à¤¸à¤à¤—à¤•à¥‹ à¤¸à¤®à¥à¤¬à¤¨à¥à¤§ à¤ªà¥‹à¤·à¤£ à¤—à¤°à¥à¤¨à¥à¤¹à¥‹à¤¸à¥à¥¤"
    ),
    THEME_DESC_DISCIPLINE_GROWTH(
        "Saturn's influence calls for patience, hard work, and responsibility. Embrace challenges as opportunities for growth and stay committed to your long-term goals.",
        "à¤¶à¤¨à¤¿à¤•à¥‹ à¤ªà¥à¤°à¤­à¤¾à¤µà¤²à¥‡ à¤§à¥ˆà¤°à¥à¤¯, à¤•à¤¡à¤¾ à¤ªà¤°à¤¿à¤¶à¥à¤°à¤® à¤° à¤œà¤¿à¤®à¥à¤®à¥‡à¤µà¤¾à¤°à¥€à¤•à¥‹ à¤†à¤¹à¥à¤µà¤¾à¤¨ à¤—à¤°à¥à¤›à¥¤ à¤šà¥à¤¨à¥Œà¤¤à¥€à¤¹à¤°à¥‚à¤²à¤¾à¤ˆ à¤µà¥ƒà¤¦à¥à¤§à¤¿à¤•à¤¾ à¤…à¤µà¤¸à¤°à¤¹à¤°à¥‚à¤•à¥‹ à¤°à¥‚à¤ªà¤®à¤¾ à¤¸à¥à¤µà¥€à¤•à¤¾à¤° à¤—à¤°à¥à¤¨à¥à¤¹à¥‹à¤¸à¥ à¤° à¤†à¤«à¥à¤¨à¥‹ à¤¦à¥€à¤°à¥à¤˜à¤•à¤¾à¤²à¥€à¤¨ à¤²à¤•à¥à¤·à¥à¤¯à¤¹à¤°à¥‚à¤ªà¥à¤°à¤¤à¤¿ à¤ªà¥à¤°à¤¤à¤¿à¤¬à¤¦à¥à¤§ à¤°à¤¹à¤¨à¥à¤¹à¥‹à¤¸à¥à¥¤"
    ),
    THEME_DESC_COMMUNICATION_LEARNING(
        "Mercury enhances your mental agility and communication skills. This is ideal for learning, teaching, writing, and all forms of information exchange.",
        "à¤¬à¥à¤§à¤²à¥‡ à¤¤à¤ªà¤¾à¤ˆà¤‚à¤•à¥‹ à¤®à¤¾à¤¨à¤¸à¤¿à¤• à¤šà¥à¤¸à¥à¤¤à¥€ à¤° à¤¸à¤žà¥à¤šà¤¾à¤° à¤•à¥Œà¤¶à¤² à¤¬à¤¢à¤¾à¤‰à¤à¤›à¥¤ à¤¯à¥‹ à¤¸à¤¿à¤•à¥à¤¨, à¤¸à¤¿à¤•à¤¾à¤‰à¤¨, à¤²à¥‡à¤–à¥à¤¨ à¤° à¤¸à¥‚à¤šà¤¨à¤¾ à¤†à¤¦à¤¾à¤¨à¤ªà¥à¤°à¤¦à¤¾à¤¨à¤•à¥‹ à¤¸à¤¬à¥ˆ à¤°à¥‚à¤ªà¤¹à¤°à¥‚à¤•à¥‹ à¤²à¤¾à¤—à¤¿ à¤†à¤¦à¤°à¥à¤¶ à¤›à¥¤"
    ),
    THEME_DESC_ENERGY_INITIATIVE(
        "Mars provides courage and drive. Take bold action, compete with integrity, and channel aggressive energy into constructive activities.",
        "à¤®à¤‚à¤—à¤²à¤²à¥‡ à¤¸à¤¾à¤¹à¤¸ à¤° à¤ªà¥à¤°à¥‡à¤°à¤£à¤¾ à¤ªà¥à¤°à¤¦à¤¾à¤¨ à¤—à¤°à¥à¤›à¥¤ à¤¸à¤¾à¤¹à¤¸à¥€ à¤•à¤¦à¤® à¤šà¤¾à¤²à¥à¤¨à¥à¤¹à¥‹à¤¸à¥, à¤‡à¤®à¤¾à¤¨à¤¦à¤¾à¤°à¥€à¤¤à¤¾à¤•à¤¾ à¤¸à¤¾à¤¥ à¤ªà¥à¤°à¤¤à¤¿à¤¸à¥à¤ªà¤°à¥à¤§à¤¾ à¤—à¤°à¥à¤¨à¥à¤¹à¥‹à¤¸à¥, à¤° à¤†à¤•à¥à¤°à¤¾à¤®à¤• à¤Šà¤°à¥à¤œà¤¾à¤²à¤¾à¤ˆ à¤°à¤šà¤¨à¤¾à¤¤à¥à¤®à¤• à¤—à¤¤à¤¿à¤µà¤¿à¤§à¤¿à¤¹à¤°à¥‚à¤®à¤¾ à¤ªà¥à¤°à¤¯à¥‹à¤— à¤—à¤°à¥à¤¨à¥à¤¹à¥‹à¤¸à¥à¥¤"
    ),
    THEME_DESC_SELF_EXPRESSION(
        "The Sun illuminates your path to self-expression and leadership. Shine your light confidently and pursue activities that bring you recognition.",
        "à¤¸à¥‚à¤°à¥à¤¯à¤²à¥‡ à¤¤à¤ªà¤¾à¤ˆà¤‚à¤•à¥‹ à¤†à¤¤à¥à¤®-à¤…à¤­à¤¿à¤µà¥à¤¯à¤•à¥à¤¤à¤¿ à¤° à¤¨à¥‡à¤¤à¥ƒà¤¤à¥à¤µà¤•à¥‹ à¤¬à¤¾à¤Ÿà¥‹ à¤‰à¤œà¥à¤¯à¤¾à¤²à¥‹ à¤¬à¤¨à¤¾à¤‰à¤à¤›à¥¤ à¤†à¤¤à¥à¤®à¤µà¤¿à¤¶à¥à¤µà¤¾à¤¸à¤•à¤¾ à¤¸à¤¾à¤¥ à¤†à¤«à¥à¤¨à¥‹ à¤ªà¥à¤°à¤•à¤¾à¤¶ à¤«à¥ˆà¤²à¤¾à¤‰à¤¨à¥à¤¹à¥‹à¤¸à¥ à¤° à¤ªà¤¹à¤¿à¤šà¤¾à¤¨ à¤²à¥à¤¯à¤¾à¤‰à¤¨à¥‡ à¤—à¤¤à¤¿à¤µà¤¿à¤§à¤¿à¤¹à¤°à¥‚ à¤…à¤¨à¥à¤¸à¤°à¤£ à¤—à¤°à¥à¤¨à¥à¤¹à¥‹à¤¸à¥à¥¤"
    ),
    THEME_DESC_INTUITION_NURTURING(
        "The Moon heightens your sensitivity and caring nature. Nurture yourself and others, and trust your instincts in important decisions.",
        "à¤šà¤¨à¥à¤¦à¥à¤°à¤®à¤¾à¤²à¥‡ à¤¤à¤ªà¤¾à¤ˆà¤‚à¤•à¥‹ à¤¸à¤‚à¤µà¥‡à¤¦à¤¨à¤¶à¥€à¤²à¤¤à¤¾ à¤° à¤¹à¥‡à¤°à¤šà¤¾à¤¹à¤•à¥‹ à¤¸à¥à¤µà¤­à¤¾à¤µ à¤¬à¤¢à¤¾à¤‰à¤à¤›à¥¤ à¤†à¤«à¥‚ à¤° à¤…à¤°à¥‚à¤²à¤¾à¤ˆ à¤ªà¥‹à¤·à¤£ à¤—à¤°à¥à¤¨à¥à¤¹à¥‹à¤¸à¥, à¤° à¤®à¤¹à¤¤à¥à¤¤à¥à¤µà¤ªà¥‚à¤°à¥à¤£ à¤¨à¤¿à¤°à¥à¤£à¤¯à¤¹à¤°à¥‚à¤®à¤¾ à¤†à¤«à¥à¤¨à¥‹ à¤¸à¤¹à¤œà¤œà¥à¤žà¤¾à¤¨à¤®à¤¾à¤¥à¤¿ à¤µà¤¿à¤¶à¥à¤µà¤¾à¤¸ à¤—à¤°à¥à¤¨à¥à¤¹à¥‹à¤¸à¥à¥¤"
    ),
    THEME_DESC_TRANSFORMATION(
        "Rahu's influence brings unconventional opportunities and desires for change. Embrace innovation but stay grounded in your values.",
        "à¤°à¤¾à¤¹à¥à¤•à¥‹ à¤ªà¥à¤°à¤­à¤¾à¤µà¤²à¥‡ à¤…à¤ªà¤°à¤®à¥à¤ªà¤°à¤¾à¤—à¤¤ à¤…à¤µà¤¸à¤°à¤¹à¤°à¥‚ à¤° à¤ªà¤°à¤¿à¤µà¤°à¥à¤¤à¤¨à¤•à¥‹ à¤‡à¤šà¥à¤›à¤¾ à¤²à¥à¤¯à¤¾à¤‰à¤à¤›à¥¤ à¤¨à¤µà¥€à¤¨à¤¤à¤¾à¤²à¤¾à¤ˆ à¤…à¤à¤—à¤¾à¤²à¥à¤¨à¥à¤¹à¥‹à¤¸à¥ à¤¤à¤° à¤†à¤«à¥à¤¨à¤¾ à¤®à¥‚à¤²à¥à¤¯à¤¹à¤°à¥‚à¤®à¤¾ à¤œà¤®à¤¿à¤¨à¤®à¤¾ à¤°à¤¹à¤¨à¥à¤¹à¥‹à¤¸à¥à¥¤"
    ),
    THEME_DESC_SPIRITUAL_LIBERATION(
        "Ketu's energy supports detachment and spiritual insight. Let go of what no longer serves you and focus on inner growth.",
        "à¤•à¥‡à¤¤à¥à¤•à¥‹ à¤Šà¤°à¥à¤œà¤¾à¤²à¥‡ à¤µà¥ˆà¤°à¤¾à¤—à¥à¤¯ à¤° à¤†à¤§à¥à¤¯à¤¾à¤¤à¥à¤®à¤¿à¤• à¤…à¤¨à¥à¤¤à¤°à¥à¤¦à¥ƒà¤·à¥à¤Ÿà¤¿à¤²à¤¾à¤ˆ à¤¸à¤®à¤°à¥à¤¥à¤¨ à¤—à¤°à¥à¤›à¥¤ à¤œà¥à¤¨ à¤•à¥à¤°à¤¾ à¤…à¤¬ à¤¤à¤ªà¤¾à¤ˆà¤‚à¤•à¥‹ à¤¸à¥‡à¤µà¤¾ à¤—à¤°à¥à¤¦à¥ˆà¤¨ à¤¤à¥à¤¯à¤¸à¤²à¤¾à¤ˆ à¤›à¥‹à¤¡à¤¿à¤¦à¤¿à¤¨à¥à¤¹à¥‹à¤¸à¥ à¤° à¤†à¤¨à¥à¤¤à¤°à¤¿à¤• à¤µà¥ƒà¤¦à¥à¤§à¤¿à¤®à¤¾ à¤§à¥à¤¯à¤¾à¤¨ à¤•à¥‡à¤¨à¥à¤¦à¥à¤°à¤¿à¤¤ à¤—à¤°à¥à¤¨à¥à¤¹à¥‹à¤¸à¥à¥¤"
    ),
    THEME_DESC_BALANCE_EQUILIBRIUM(
        "A day of balance where all energies are in equilibrium. Maintain steadiness and make measured progress in all areas of life.",
        "à¤¸à¤¨à¥à¤¤à¥à¤²à¤¨à¤•à¥‹ à¤¦à¤¿à¤¨ à¤œà¤¹à¤¾à¤ à¤¸à¤¬à¥ˆ à¤Šà¤°à¥à¤œà¤¾à¤¹à¤°à¥‚ à¤¸à¤®à¤¾à¤¨à¤®à¤¾ à¤›à¤¨à¥à¥¤ à¤¸à¥à¤¥à¤¿à¤°à¤¤à¤¾ à¤•à¤¾à¤¯à¤® à¤°à¤¾à¤–à¥à¤¨à¥à¤¹à¥‹à¤¸à¥ à¤° à¤œà¥€à¤µà¤¨à¤•à¤¾ à¤¸à¤¬à¥ˆ à¤•à¥à¤·à¥‡à¤¤à¥à¤°à¤¹à¤°à¥‚à¤®à¤¾ à¤®à¤¾à¤ªà¤¿à¤¤ à¤ªà¥à¤°à¤—à¤¤à¤¿ à¤—à¤°à¥à¤¨à¥à¤¹à¥‹à¤¸à¥à¥¤"
    ),

    // ============================================
    // LUCKY ELEMENTS
    // ============================================
    LUCKY_COLOR_FIRE("Red, Orange, or Gold", "à¤°à¤¾à¤¤à¥‹, à¤¸à¥à¤¨à¥à¤¤à¤²à¤¾, à¤µà¤¾ à¤¸à¥à¤¨à¥Œà¤²à¥‹"),
    LUCKY_COLOR_EARTH("Green, Brown, or White", "à¤¹à¤°à¤¿à¤¯à¥‹, à¤–à¥ˆà¤°à¥‹, à¤µà¤¾ à¤¸à¥‡à¤¤à¥‹"),
    LUCKY_COLOR_AIR("Blue, Light Blue, or Silver", "à¤¨à¤¿à¤²à¥‹, à¤¹à¤²à¥à¤•à¤¾ à¤¨à¤¿à¤²à¥‹, à¤µà¤¾ à¤šà¤¾à¤à¤¦à¥€"),
    LUCKY_COLOR_WATER("White, Cream, or Sea Green", "à¤¸à¥‡à¤¤à¥‹, à¤•à¥à¤°à¥€à¤®, à¤µà¤¾ à¤¸à¤®à¥à¤¦à¥à¤°à¥€ à¤¹à¤°à¤¿à¤¯à¥‹"),

    LUCKY_DIRECTION_EAST("East", "à¤ªà¥‚à¤°à¥à¤µ"),
    LUCKY_DIRECTION_WEST("West", "à¤ªà¤¶à¥à¤šà¤¿à¤®"),
    LUCKY_DIRECTION_NORTH("North", "à¤‰à¤¤à¥à¤¤à¤°"),
    LUCKY_DIRECTION_SOUTH("South", "à¤¦à¤•à¥à¤·à¤¿à¤£"),
    LUCKY_DIRECTION_NORTHEAST("North-East", "à¤‰à¤¤à¥à¤¤à¤°-à¤ªà¥‚à¤°à¥à¤µ"),
    LUCKY_DIRECTION_NORTHWEST("North-West", "à¤‰à¤¤à¥à¤¤à¤°-à¤ªà¤¶à¥à¤šà¤¿à¤®"),
    LUCKY_DIRECTION_SOUTHEAST("South-East", "à¤¦à¤•à¥à¤·à¤¿à¤£-à¤ªà¥‚à¤°à¥à¤µ"),
    LUCKY_DIRECTION_SOUTHWEST("South-West", "à¤¦à¤•à¥à¤·à¤¿à¤£-à¤ªà¤¶à¥à¤šà¤¿à¤®"),

    // ============================================
    // GEMSTONES
    // ============================================
    GEMSTONE_RUBY("Ruby", "à¤®à¤¾à¤£à¤¿à¤•"),
    GEMSTONE_PEARL("Pearl", "à¤®à¥‹à¤¤à¥€"),
    GEMSTONE_RED_CORAL("Red Coral", "à¤®à¥‚à¤‚à¤—à¤¾"),
    GEMSTONE_EMERALD("Emerald", "à¤ªà¤¨à¥à¤¨à¤¾"),
    GEMSTONE_YELLOW_SAPPHIRE("Yellow Sapphire", "à¤ªà¥à¤·à¥à¤ªà¤°à¤¾à¤œ"),
    GEMSTONE_DIAMOND("Diamond or White Sapphire", "à¤¹à¥€à¤°à¤¾ à¤µà¤¾ à¤¸à¥‡à¤¤à¥‹ à¤¨à¥€à¤²à¤®"),
    GEMSTONE_BLUE_SAPPHIRE("Blue Sapphire", "à¤¨à¥€à¤²à¤®"),
    GEMSTONE_HESSONITE("Hessonite", "à¤—à¥‹à¤®à¥‡à¤¦"),
    GEMSTONE_CATS_EYE("Cat's Eye", "à¤µà¥ˆà¤¦à¥‚à¤°à¥à¤¯"),

    // ============================================
    // DASHA RECOMMENDATIONS
    // ============================================
    DASHA_REC_SUN("Engage in activities that build confidence and leadership skills.", "à¤†à¤¤à¥à¤®à¤µà¤¿à¤¶à¥à¤µà¤¾à¤¸ à¤° à¤¨à¥‡à¤¤à¥ƒà¤¤à¥à¤µ à¤•à¥Œà¤¶à¤² à¤µà¤¿à¤•à¤¾à¤¸ à¤—à¤°à¥à¤¨à¥‡ à¤—à¤¤à¤¿à¤µà¤¿à¤§à¤¿à¤¹à¤°à¥‚à¤®à¤¾ à¤¸à¤‚à¤²à¤—à¥à¤¨ à¤¹à¥à¤¨à¥à¤¹à¥‹à¤¸à¥à¥¤"),
    DASHA_REC_MOON("Prioritize emotional well-being and nurturing relationships.", "à¤­à¤¾à¤µà¤¨à¤¾à¤¤à¥à¤®à¤• à¤•à¤²à¥à¤¯à¤¾à¤£ à¤° à¤ªà¥‹à¤·à¤£à¤ªà¥‚à¤°à¥à¤£ à¤¸à¤®à¥à¤¬à¤¨à¥à¤§à¤¹à¤°à¥‚à¤²à¤¾à¤ˆ à¤ªà¥à¤°à¤¾à¤¥à¤®à¤¿à¤•à¤¤à¤¾ à¤¦à¤¿à¤¨à¥à¤¹à¥‹à¤¸à¥à¥¤"),
    DASHA_REC_MARS("Channel your energy into physical activities and competitive pursuits.", "à¤†à¤«à¥à¤¨à¥‹ à¤Šà¤°à¥à¤œà¤¾à¤²à¤¾à¤ˆ à¤¶à¤¾à¤°à¥€à¤°à¤¿à¤• à¤—à¤¤à¤¿à¤µà¤¿à¤§à¤¿ à¤° à¤ªà¥à¤°à¤¤à¤¿à¤¸à¥à¤ªà¤°à¥à¤§à¤¾à¤¤à¥à¤®à¤• à¤ªà¥à¤°à¤¯à¤¾à¤¸à¤¹à¤°à¥‚à¤®à¤¾ à¤ªà¥à¤°à¤¯à¥‹à¤— à¤—à¤°à¥à¤¨à¥à¤¹à¥‹à¤¸à¥à¥¤"),
    DASHA_REC_MERCURY("Focus on learning, communication, and intellectual growth.", "à¤¸à¤¿à¤•à¤¾à¤‡, à¤¸à¤žà¥à¤šà¤¾à¤° à¤° à¤¬à¥Œà¤¦à¥à¤§à¤¿à¤• à¤µà¥ƒà¤¦à¥à¤§à¤¿à¤®à¤¾ à¤§à¥à¤¯à¤¾à¤¨ à¤¦à¤¿à¤¨à¥à¤¹à¥‹à¤¸à¥à¥¤"),
    DASHA_REC_JUPITER("Expand your horizons through education, travel, or spiritual practices.", "à¤¶à¤¿à¤•à¥à¤·à¤¾, à¤¯à¤¾à¤¤à¥à¤°à¤¾ à¤µà¤¾ à¤†à¤§à¥à¤¯à¤¾à¤¤à¥à¤®à¤¿à¤• à¤…à¤­à¥à¤¯à¤¾à¤¸à¤¹à¤°à¥‚à¤•à¥‹ à¤®à¤¾à¤§à¥à¤¯à¤®à¤¬à¤¾à¤Ÿ à¤†à¤«à¥à¤¨à¥‹ à¤•à¥à¤·à¤¿à¤¤à¤¿à¤œ à¤µà¤¿à¤¸à¥à¤¤à¤¾à¤° à¤—à¤°à¥à¤¨à¥à¤¹à¥‹à¤¸à¥à¥¤"),
    DASHA_REC_VENUS("Cultivate beauty, art, and harmonious relationships.", "à¤¸à¥Œà¤¨à¥à¤¦à¤°à¥à¤¯, à¤•à¤²à¤¾ à¤° à¤¸à¤¾à¤®à¤žà¥à¤œà¤¸à¥à¤¯à¤ªà¥‚à¤°à¥à¤£ à¤¸à¤®à¥à¤¬à¤¨à¥à¤§à¤¹à¤°à¥‚ à¤µà¤¿à¤•à¤¾à¤¸ à¤—à¤°à¥à¤¨à¥à¤¹à¥‹à¤¸à¥à¥¤"),
    DASHA_REC_SATURN("Embrace discipline, hard work, and long-term planning.", "à¤…à¤¨à¥à¤¶à¤¾à¤¸à¤¨, à¤•à¤¡à¤¾ à¤ªà¤°à¤¿à¤¶à¥à¤°à¤® à¤° à¤¦à¥€à¤°à¥à¤˜à¤•à¤¾à¤²à¥€à¤¨ à¤¯à¥‹à¤œà¤¨à¤¾à¤²à¤¾à¤ˆ à¤…à¤à¤—à¤¾à¤²à¥à¤¨à¥à¤¹à¥‹à¤¸à¥à¥¤"),
    DASHA_REC_RAHU("Explore unconventional paths while staying grounded.", "à¤œà¤®à¤¿à¤¨à¤®à¤¾ à¤°à¤¹à¤à¤¦à¥ˆ à¤…à¤ªà¤°à¤®à¥à¤ªà¤°à¤¾à¤—à¤¤ à¤®à¤¾à¤°à¥à¤—à¤¹à¤°à¥‚ à¤…à¤¨à¥à¤µà¥‡à¤·à¤£ à¤—à¤°à¥à¤¨à¥à¤¹à¥‹à¤¸à¥à¥¤"),
    DASHA_REC_KETU("Practice detachment and focus on spiritual development.", "à¤µà¥ˆà¤°à¤¾à¤—à¥à¤¯à¤•à¥‹ à¤…à¤­à¥à¤¯à¤¾à¤¸ à¤—à¤°à¥à¤¨à¥à¤¹à¥‹à¤¸à¥ à¤° à¤†à¤§à¥à¤¯à¤¾à¤¤à¥à¤®à¤¿à¤• à¤µà¤¿à¤•à¤¾à¤¸à¤®à¤¾ à¤§à¥à¤¯à¤¾à¤¨ à¤¦à¤¿à¤¨à¥à¤¹à¥‹à¤¸à¥à¥¤"),

    // ============================================
    // DASHA AFFIRMATIONS
    // ============================================
    DASHA_AFF_SUN("I shine my light confidently and inspire those around me.", "à¤® à¤†à¤¤à¥à¤®à¤µà¤¿à¤¶à¥à¤µà¤¾à¤¸à¤•à¤¾ à¤¸à¤¾à¤¥ à¤†à¤«à¥à¤¨à¥‹ à¤ªà¥à¤°à¤•à¤¾à¤¶ à¤«à¥ˆà¤²à¤¾à¤‰à¤à¤›à¥ à¤° à¤µà¤°à¤¿à¤ªà¤°à¤¿à¤•à¤¾ à¤®à¤¾à¤¨à¤¿à¤¸à¤¹à¤°à¥‚à¤²à¤¾à¤ˆ à¤ªà¥à¤°à¥‡à¤°à¤¿à¤¤ à¤—à¤°à¥à¤›à¥à¥¤"),
    DASHA_AFF_MOON("I trust my intuition and nurture myself with compassion.", "à¤® à¤†à¤«à¥à¤¨à¥‹ à¤…à¤¨à¥à¤¤à¤°à¥à¤œà¥à¤žà¤¾à¤¨à¤®à¤¾à¤¥à¤¿ à¤µà¤¿à¤¶à¥à¤µà¤¾à¤¸ à¤—à¤°à¥à¤›à¥ à¤° à¤•à¤°à¥à¤£à¤¾à¤•à¤¾ à¤¸à¤¾à¤¥ à¤†à¤«à¥‚à¤²à¤¾à¤ˆ à¤ªà¥‹à¤·à¤£ à¤—à¤°à¥à¤›à¥à¥¤"),
    DASHA_AFF_MARS("I channel my energy constructively and act with courage.", "à¤® à¤†à¤«à¥à¤¨à¥‹ à¤Šà¤°à¥à¤œà¤¾ à¤°à¤šà¤¨à¤¾à¤¤à¥à¤®à¤• à¤°à¥‚à¤ªà¤®à¤¾ à¤ªà¥à¤°à¤¯à¥‹à¤— à¤—à¤°à¥à¤›à¥ à¤° à¤¸à¤¾à¤¹à¤¸à¤•à¤¾ à¤¸à¤¾à¤¥ à¤•à¤¾à¤® à¤—à¤°à¥à¤›à¥à¥¤"),
    DASHA_AFF_MERCURY("I communicate clearly and embrace continuous learning.", "à¤® à¤¸à¥à¤ªà¤·à¥à¤Ÿ à¤°à¥‚à¤ªà¤®à¤¾ à¤¸à¤žà¥à¤šà¤¾à¤° à¤—à¤°à¥à¤›à¥ à¤° à¤¨à¤¿à¤°à¤¨à¥à¤¤à¤° à¤¸à¤¿à¤•à¤¾à¤‡à¤²à¤¾à¤ˆ à¤…à¤à¤—à¤¾à¤²à¥à¤›à¥à¥¤"),
    DASHA_AFF_JUPITER("I am open to abundance and share my wisdom generously.", "à¤® à¤ªà¥à¤°à¤šà¥à¤°à¤¤à¤¾à¤•à¥‹ à¤²à¤¾à¤—à¤¿ à¤–à¥à¤²à¤¾ à¤›à¥ à¤° à¤†à¤«à¥à¤¨à¥‹ à¤œà¥à¤žà¤¾à¤¨ à¤‰à¤¦à¤¾à¤°à¤¤à¤¾à¤ªà¥‚à¤°à¥à¤µà¤• à¤¸à¤¾à¤à¤¾ à¤—à¤°à¥à¤›à¥à¥¤"),
    DASHA_AFF_VENUS("I attract beauty and harmony into my life.", "à¤® à¤†à¤«à¥à¤¨à¥‹ à¤œà¥€à¤µà¤¨à¤®à¤¾ à¤¸à¥Œà¤¨à¥à¤¦à¤°à¥à¤¯ à¤° à¤¸à¤¾à¤®à¤žà¥à¤œà¤¸à¥à¤¯ à¤†à¤•à¤°à¥à¤·à¤¿à¤¤ à¤—à¤°à¥à¤›à¥à¥¤"),
    DASHA_AFF_SATURN("I embrace discipline and trust in the timing of my journey.", "à¤® à¤…à¤¨à¥à¤¶à¤¾à¤¸à¤¨à¤²à¤¾à¤ˆ à¤…à¤à¤—à¤¾à¤²à¥à¤›à¥ à¤° à¤†à¤«à¥à¤¨à¥‹ à¤¯à¤¾à¤¤à¥à¤°à¤¾à¤•à¥‹ à¤¸à¤®à¤¯à¤®à¤¾à¤¥à¤¿ à¤µà¤¿à¤¶à¥à¤µà¤¾à¤¸ à¤—à¤°à¥à¤›à¥à¥¤"),
    DASHA_AFF_RAHU("I embrace change and transform challenges into opportunities.", "à¤® à¤ªà¤°à¤¿à¤µà¤°à¥à¤¤à¤¨à¤²à¤¾à¤ˆ à¤…à¤à¤—à¤¾à¤²à¥à¤›à¥ à¤° à¤šà¥à¤¨à¥Œà¤¤à¥€à¤¹à¤°à¥‚à¤²à¤¾à¤ˆ à¤…à¤µà¤¸à¤°à¤¹à¤°à¥‚à¤®à¤¾ à¤°à¥‚à¤ªà¤¾à¤¨à¥à¤¤à¤°à¤£ à¤—à¤°à¥à¤›à¥à¥¤"),
    DASHA_AFF_KETU("I release what no longer serves me and embrace spiritual growth.", "à¤® à¤œà¥à¤¨ à¤•à¥à¤°à¤¾à¤²à¥‡ à¤…à¤¬ à¤®à¥‡à¤°à¥‹ à¤¸à¥‡à¤µà¤¾ à¤—à¤°à¥à¤¦à¥ˆà¤¨ à¤¤à¥à¤¯à¤¸à¤²à¤¾à¤ˆ à¤›à¤¾à¤¡à¥à¤›à¥ à¤° à¤†à¤§à¥à¤¯à¤¾à¤¤à¥à¤®à¤¿à¤• à¤µà¥ƒà¤¦à¥à¤§à¤¿à¤²à¤¾à¤ˆ à¤…à¤à¤—à¤¾à¤²à¥à¤›à¥à¥¤"),

    // ============================================
    // ELEMENT RECOMMENDATIONS
    // ============================================
    ELEMENT_REC_FIRE("Take bold action and express yourself confidently.", "à¤¸à¤¾à¤¹à¤¸à¥€ à¤•à¤¦à¤® à¤šà¤¾à¤²à¥à¤¨à¥à¤¹à¥‹à¤¸à¥ à¤° à¤†à¤¤à¥à¤®à¤µà¤¿à¤¶à¥à¤µà¤¾à¤¸à¤•à¤¾ à¤¸à¤¾à¤¥ à¤†à¤«à¥‚à¤²à¤¾à¤ˆ à¤µà¥à¤¯à¤•à¥à¤¤ à¤—à¤°à¥à¤¨à¥à¤¹à¥‹à¤¸à¥à¥¤"),
    ELEMENT_REC_EARTH("Focus on practical matters and material progress.", "à¤µà¥à¤¯à¤¾à¤µà¤¹à¤¾à¤°à¤¿à¤• à¤®à¤¾à¤®à¤¿à¤²à¤¾à¤¹à¤°à¥‚ à¤° à¤­à¥Œà¤¤à¤¿à¤• à¤ªà¥à¤°à¤—à¤¤à¤¿à¤®à¤¾ à¤§à¥à¤¯à¤¾à¤¨ à¤¦à¤¿à¤¨à¥à¤¹à¥‹à¤¸à¥à¥¤"),
    ELEMENT_REC_AIR("Engage in social activities and intellectual pursuits.", "à¤¸à¤¾à¤®à¤¾à¤œà¤¿à¤• à¤—à¤¤à¤¿à¤µà¤¿à¤§à¤¿à¤¹à¤°à¥‚ à¤° à¤¬à¥Œà¤¦à¥à¤§à¤¿à¤• à¤ªà¥à¤°à¤¯à¤¾à¤¸à¤¹à¤°à¥‚à¤®à¤¾ à¤¸à¤‚à¤²à¤—à¥à¤¨ à¤¹à¥à¤¨à¥à¤¹à¥‹à¤¸à¥à¥¤"),
    ELEMENT_REC_WATER("Trust your intuition and honor your emotions.", "à¤†à¤«à¥à¤¨à¥‹ à¤…à¤¨à¥à¤¤à¤°à¥à¤œà¥à¤žà¤¾à¤¨à¤®à¤¾à¤¥à¤¿ à¤µà¤¿à¤¶à¥à¤µà¤¾à¤¸ à¤—à¤°à¥à¤¨à¥à¤¹à¥‹à¤¸à¥ à¤° à¤†à¤«à¥à¤¨à¤¾ à¤­à¤¾à¤µà¤¨à¤¾à¤¹à¤°à¥‚à¤²à¤¾à¤ˆ à¤¸à¤®à¥à¤®à¤¾à¤¨ à¤—à¤°à¥à¤¨à¥à¤¹à¥‹à¤¸à¥à¥¤"),

    // ============================================
    // LIFE AREA RECOMMENDATIONS
    // ============================================
    AREA_REC_CAREER("Capitalize on favorable career energy today.", "à¤†à¤œ à¤…à¤¨à¥à¤•à¥‚à¤² à¤•à¥à¤¯à¤¾à¤°à¤¿à¤¯à¤° à¤Šà¤°à¥à¤œà¤¾à¤•à¥‹ à¤«à¤¾à¤‡à¤¦à¤¾ à¤‰à¤ à¤¾à¤‰à¤¨à¥à¤¹à¥‹à¤¸à¥à¥¤"),
    AREA_REC_LOVE("Nurture your relationships with extra attention.", "à¤¥à¤ª à¤§à¥à¤¯à¤¾à¤¨à¤•à¤¾ à¤¸à¤¾à¤¥ à¤†à¤«à¥à¤¨à¤¾ à¤¸à¤®à¥à¤¬à¤¨à¥à¤§à¤¹à¤°à¥‚à¤²à¤¾à¤ˆ à¤ªà¥‹à¤·à¤£ à¤—à¤°à¥à¤¨à¥à¤¹à¥‹à¤¸à¥à¥¤"),
    AREA_REC_HEALTH("Make the most of your vibrant health energy.", "à¤†à¤«à¥à¤¨à¥‹ à¤œà¥€à¤µà¤¨à¥à¤¤ à¤¸à¥à¤µà¤¾à¤¸à¥à¤¥à¥à¤¯ à¤Šà¤°à¥à¤œà¤¾à¤•à¥‹ à¤…à¤§à¤¿à¤•à¤¤à¤® à¤«à¤¾à¤‡à¤¦à¤¾ à¤²à¤¿à¤¨à¥à¤¹à¥‹à¤¸à¥à¥¤"),
    AREA_REC_FINANCE("Take advantage of positive financial influences.", "à¤¸à¤•à¤¾à¤°à¤¾à¤¤à¥à¤®à¤• à¤µà¤¿à¤¤à¥à¤¤à¥€à¤¯ à¤ªà¥à¤°à¤­à¤¾à¤µà¤¹à¤°à¥‚à¤•à¥‹ à¤«à¤¾à¤‡à¤¦à¤¾ à¤‰à¤ à¤¾à¤‰à¤¨à¥à¤¹à¥‹à¤¸à¥à¥¤"),
    AREA_REC_FAMILY("Spend quality time with family members.", "à¤ªà¤°à¤¿à¤µà¤¾à¤°à¤•à¤¾ à¤¸à¤¦à¤¸à¥à¤¯à¤¹à¤°à¥‚à¤¸à¤à¤— à¤—à¥à¤£à¤¸à¥à¤¤à¤°à¥€à¤¯ à¤¸à¤®à¤¯ à¤¬à¤¿à¤¤à¤¾à¤‰à¤¨à¥à¤¹à¥‹à¤¸à¥à¥¤"),
    AREA_REC_SPIRITUALITY("Deepen your spiritual practices.", "à¤†à¤«à¥à¤¨à¥‹ à¤†à¤§à¥à¤¯à¤¾à¤¤à¥à¤®à¤¿à¤• à¤…à¤­à¥à¤¯à¤¾à¤¸à¤¹à¤°à¥‚à¤²à¤¾à¤ˆ à¤—à¤¹à¤¿à¤°à¥‹ à¤¬à¤¨à¤¾à¤‰à¤¨à¥à¤¹à¥‹à¤¸à¥à¥¤"),

    // ============================================
    // PLANET CAUTIONS
    // ============================================
    CAUTION_SATURN("Avoid rushing into decisions. Patience is key.", "à¤¨à¤¿à¤°à¥à¤£à¤¯à¤¹à¤°à¥‚à¤®à¤¾ à¤¹à¤¤à¤¾à¤° à¤¨à¤—à¤°à¥à¤¨à¥à¤¹à¥‹à¤¸à¥à¥¤ à¤§à¥ˆà¤°à¥à¤¯ à¤®à¤¹à¤¤à¥à¤¤à¥à¤µà¤ªà¥‚à¤°à¥à¤£ à¤›à¥¤"),
    CAUTION_MARS("Control impulsive reactions and avoid conflicts.", "à¤†à¤µà¥‡à¤—à¤ªà¥‚à¤°à¥à¤£ à¤ªà¥à¤°à¤¤à¤¿à¤•à¥à¤°à¤¿à¤¯à¤¾à¤¹à¤°à¥‚ à¤¨à¤¿à¤¯à¤¨à¥à¤¤à¥à¤°à¤£ à¤—à¤°à¥à¤¨à¥à¤¹à¥‹à¤¸à¥ à¤° à¤µà¤¿à¤µà¤¾à¤¦à¤¹à¤°à¥‚à¤¬à¤¾à¤Ÿ à¤¬à¤šà¥à¤¨à¥à¤¹à¥‹à¤¸à¥à¥¤"),
    CAUTION_RAHU("Be wary of deception and unrealistic expectations.", "à¤›à¤²à¤•à¤ªà¤Ÿ à¤° à¤…à¤µà¤¾à¤¸à¥à¤¤à¤µà¤¿à¤• à¤…à¤ªà¥‡à¤•à¥à¤·à¤¾à¤¹à¤°à¥‚à¤¬à¤¾à¤Ÿ à¤¸à¤¾à¤µà¤§à¤¾à¤¨ à¤°à¤¹à¤¨à¥à¤¹à¥‹à¤¸à¥à¥¤"),
    CAUTION_KETU("Don't neglect practical responsibilities for escapism.", "à¤ªà¤²à¤¾à¤¯à¤¨à¤µà¤¾à¤¦à¤•à¥‹ à¤²à¤¾à¤—à¤¿ à¤µà¥à¤¯à¤¾à¤µà¤¹à¤¾à¤°à¤¿à¤• à¤œà¤¿à¤®à¥à¤®à¥‡à¤µà¤¾à¤°à¥€à¤¹à¤°à¥‚à¤²à¤¾à¤ˆ à¤¬à¥‡à¤µà¤¾à¤¸à¥à¤¤à¤¾ à¤¨à¤—à¤°à¥à¤¨à¥à¤¹à¥‹à¤¸à¥à¥¤"),

    // ============================================
    // HOROSCOPE UI STRINGS
    // ============================================
    HOROSCOPE_BALANCE("Balance", "à¤¸à¤¨à¥à¤¤à¥à¤²à¤¨"),
    HOROSCOPE_STEADY_ENERGY("Steady energy expected", "à¤¸à¥à¤¥à¤¿à¤° à¤Šà¤°à¥à¤œà¤¾ à¤…à¤ªà¥‡à¤•à¥à¤·à¤¿à¤¤"),
    HOROSCOPE_CALCULATING("Calculating...", "à¤—à¤£à¤¨à¤¾ à¤—à¤°à¥à¤¦à¥ˆ..."),
    HOROSCOPE_VEDHA_OBSTRUCTION("However, %s creates Vedha obstruction, reducing benefits.", "à¤¤à¤°, %s à¤²à¥‡ à¤µà¥‡à¤§ à¤…à¤µà¤°à¥‹à¤§ à¤¸à¤¿à¤°à¥à¤œà¤¨à¤¾ à¤—à¤°à¥à¤›, à¤«à¤¾à¤‡à¤¦à¤¾à¤¹à¤°à¥‚ à¤˜à¤Ÿà¤¾à¤‰à¤à¤›à¥¤"),
    HOROSCOPE_ASHTAKAVARGA_STRONG("Ashtakavarga (%d/8) strengthens results.", "à¤…à¤·à¥à¤Ÿà¤•à¤µà¤°à¥à¤— (%d/8) à¤²à¥‡ à¤ªà¤°à¤¿à¤£à¤¾à¤®à¤¹à¤°à¥‚ à¤¬à¤²à¤¿à¤¯à¥‹ à¤¬à¤¨à¤¾à¤‰à¤à¤›à¥¤"),
    HOROSCOPE_ASHTAKAVARGA_MODERATE("Ashtakavarga (%d/8) moderates results.", "à¤…à¤·à¥à¤Ÿà¤•à¤µà¤°à¥à¤— (%d/8) à¤²à¥‡ à¤ªà¤°à¤¿à¤£à¤¾à¤®à¤¹à¤°à¥‚à¤²à¤¾à¤ˆ à¤®à¤§à¥à¤¯à¤® à¤¬à¤¨à¤¾à¤‰à¤à¤›à¥¤"),
    HOROSCOPE_ASHTAKAVARGA_WEAK("Low Ashtakavarga (%d/8) weakens results.", "à¤•à¤® à¤…à¤·à¥à¤Ÿà¤•à¤µà¤°à¥à¤— (%d/8) à¤²à¥‡ à¤ªà¤°à¤¿à¤£à¤¾à¤®à¤¹à¤°à¥‚ à¤•à¤®à¤œà¥‹à¤° à¤¬à¤¨à¤¾à¤‰à¤à¤›à¥¤"),
    HOROSCOPE_RETROGRADE_DELAY("%s's retrograde motion delays manifestation.", "%s à¤•à¥‹ à¤µà¤•à¥à¤°à¥€ à¤—à¤¤à¤¿à¤²à¥‡ à¤ªà¥à¤°à¤•à¤Ÿà¥€à¤•à¤°à¤£à¤®à¤¾ à¤¢à¤¿à¤²à¤¾à¤‡ à¤—à¤°à¥à¤›à¥¤"),
    HOROSCOPE_RETROGRADE_RELIEF("%s's retrograde provides some relief from challenges.", "%s à¤•à¥‹ à¤µà¤•à¥à¤°à¥€à¤²à¥‡ à¤šà¥à¤¨à¥Œà¤¤à¥€à¤¹à¤°à¥‚à¤¬à¤¾à¤Ÿ à¤•à¥‡à¤¹à¥€ à¤°à¤¾à¤¹à¤¤ à¤ªà¥à¤°à¤¦à¤¾à¤¨ à¤—à¤°à¥à¤›à¥¤"),
    HOROSCOPE_OWN_SIGN("Strong in own sign.", "à¤†à¤«à¥à¤¨à¥ˆ à¤°à¤¾à¤¶à¤¿à¤®à¤¾ à¤¬à¤²à¤¿à¤¯à¥‹à¥¤"),
    HOROSCOPE_EXALTED("Exalted - excellent results.", "à¤‰à¤šà¥à¤š - à¤‰à¤¤à¥à¤•à¥ƒà¤·à¥à¤Ÿ à¤ªà¤°à¤¿à¤£à¤¾à¤®à¤¹à¤°à¥‚à¥¤"),
    HOROSCOPE_DEBILITATED("Debilitated - results weakened.", "à¤¨à¥€à¤š - à¤ªà¤°à¤¿à¤£à¤¾à¤®à¤¹à¤°à¥‚ à¤•à¤®à¤œà¥‹à¤°à¥¤"),
    HOROSCOPE_FAVORABLE_TRANSIT("Favorable %s transit in house %d.", "à¤­à¤¾à¤µ %d à¤®à¤¾ à¤…à¤¨à¥à¤•à¥‚à¤² %s à¤—à¥‹à¤šà¤°à¥¤"),
    HOROSCOPE_UNFAVORABLE_TRANSIT("Challenging %s transit in house %d.", "à¤­à¤¾à¤µ %d à¤®à¤¾ à¤šà¥à¤¨à¥Œà¤¤à¥€à¤ªà¥‚à¤°à¥à¤£ %s à¤—à¥‹à¤šà¤°à¥¤"),
    HOROSCOPE_BALANCED_ENERGY("Balanced energy in this area.", "à¤¯à¤¸ à¤•à¥à¤·à¥‡à¤¤à¥à¤°à¤®à¤¾ à¤¸à¤¨à¥à¤¤à¥à¤²à¤¿à¤¤ à¤Šà¤°à¥à¤œà¤¾à¥¤"),

    // ============================================
    // SETTINGS TAB
    // ============================================
    SETTINGS_PROFILE("Profile", "à¤ªà¥à¤°à¥‹à¤«à¤¾à¤‡à¤²"),
    SETTINGS_EXPORT("Export", "à¤¨à¤¿à¤°à¥à¤¯à¤¾à¤¤"),
    SETTINGS_AI_CHAT("AI & Chat", "AI à¤° à¤šà¥à¤¯à¤¾à¤Ÿ"),
    SETTINGS_AI_MODELS("AI Models", "AI à¤®à¥‹à¤¡à¥‡à¤²à¤¹à¤°à¥‚"),
    SETTINGS_AI_MODELS_DESC("Configure chat AI providers", "à¤šà¥à¤¯à¤¾à¤Ÿ AI à¤ªà¥à¤°à¤¦à¤¾à¤¯à¤•à¤¹à¤°à¥‚ à¤•à¤¨à¥à¤«à¤¿à¤—à¤° à¤—à¤°à¥à¤¨à¥à¤¹à¥‹à¤¸à¥"),
    SETTINGS_PREFERENCES("Preferences", "à¤ªà¥à¤°à¤¾à¤¥à¤®à¤¿à¤•à¤¤à¤¾à¤¹à¤°à¥‚"),
    SETTINGS_ABOUT("About", "à¤¬à¤¾à¤°à¥‡à¤®à¤¾"),

    // Profile settings
    SETTINGS_EDIT_PROFILE("Edit Profile", "à¤ªà¥à¤°à¥‹à¤«à¤¾à¤‡à¤² à¤¸à¤®à¥à¤ªà¤¾à¤¦à¤¨"),
    SETTINGS_EDIT_PROFILE_DESC("Modify birth details", "à¤œà¤¨à¥à¤® à¤µà¤¿à¤µà¤°à¤£ à¤ªà¤°à¤¿à¤®à¤¾à¤°à¥à¤œà¤¨ à¤—à¤°à¥à¤¨à¥à¤¹à¥‹à¤¸à¥"),
    SETTINGS_MANAGE_PROFILES("Manage Profiles", "à¤ªà¥à¤°à¥‹à¤«à¤¾à¤‡à¤²à¤¹à¤°à¥‚ à¤µà¥à¤¯à¤µà¤¸à¥à¤¥à¤¾à¤ªà¤¨"),
    SETTINGS_NO_PROFILE("No profile selected", "à¤•à¥à¤¨à¥ˆ à¤ªà¥à¤°à¥‹à¤«à¤¾à¤‡à¤² à¤›à¤¾à¤¨à¤¿à¤à¤•à¥‹ à¤›à¥ˆà¤¨"),
    SETTINGS_TAP_TO_SELECT("Tap to select or create a profile", "à¤ªà¥à¤°à¥‹à¤«à¤¾à¤‡à¤² à¤›à¤¾à¤¨à¥à¤¨ à¤µà¤¾ à¤¬à¤¨à¤¾à¤‰à¤¨ à¤Ÿà¥à¤¯à¤¾à¤ª à¤—à¤°à¥à¤¨à¥à¤¹à¥‹à¤¸à¥"),

    // Export settings
    SETTINGS_EXPORT_PDF("Export as PDF", "PDF à¤•à¥‹ à¤°à¥‚à¤ªà¤®à¤¾ à¤¨à¤¿à¤°à¥à¤¯à¤¾à¤¤"),
    SETTINGS_EXPORT_PDF_DESC("Complete chart report", "à¤ªà¥‚à¤°à¥à¤£ à¤•à¥à¤£à¥à¤¡à¤²à¥€ à¤°à¤¿à¤ªà¥‹à¤°à¥à¤Ÿ"),
    SETTINGS_EXPORT_IMAGE("Export as Image", "à¤›à¤µà¤¿à¤•à¥‹ à¤°à¥‚à¤ªà¤®à¤¾ à¤¨à¤¿à¤°à¥à¤¯à¤¾à¤¤"),
    SETTINGS_EXPORT_IMAGE_DESC("High-quality chart image", "à¤‰à¤šà¥à¤š à¤—à¥à¤£à¤¸à¥à¤¤à¤° à¤•à¥à¤£à¥à¤¡à¤²à¥€ à¤›à¤µà¤¿"),
    SETTINGS_EXPORT_CLIPBOARD("Copy to Clipboard", "à¤•à¥à¤²à¤¿à¤ªà¤¬à¥‹à¤°à¥à¤¡à¤®à¤¾ à¤•à¤ªà¥€ à¤—à¤°à¥à¤¨à¥à¤¹à¥‹à¤¸à¥"),
    SETTINGS_EXPORT_CLIPBOARD_DESC("Plain text format", "à¤¸à¤¾à¤¦à¤¾ à¤ªà¤¾à¤  à¤¢à¤¾à¤à¤šà¤¾"),
    SETTINGS_EXPORT_JSON("Export as JSON", "JSON à¤•à¥‹ à¤°à¥‚à¤ªà¤®à¤¾ à¤¨à¤¿à¤°à¥à¤¯à¤¾à¤¤"),
    SETTINGS_EXPORT_JSON_DESC("Machine-readable format", "à¤®à¥‡à¤¸à¤¿à¤¨-à¤ªà¤ à¤¨à¤¯à¥‹à¤—à¥à¤¯ à¤¢à¤¾à¤à¤šà¤¾"),
    SETTINGS_EXPORT_CSV("CSV Data", "CSV à¤¡à¤¾à¤Ÿà¤¾"),
    SETTINGS_EXPORT_CSV_DESC("Spreadsheet format", "à¤¸à¥à¤ªà¥à¤°à¥‡à¤¡à¤¸à¤¿à¤Ÿ à¤¢à¤¾à¤à¤šà¤¾"),

    // Preferences
    SETTINGS_HOUSE_SYSTEM("House System", "à¤­à¤¾à¤µ à¤ªà¤¦à¥à¤§à¤¤à¤¿"),
    SETTINGS_AYANAMSA("Ayanamsa", "à¤…à¤¯à¤¨à¤¾à¤‚à¤¶"),
    SETTINGS_LANGUAGE("Language", "à¤­à¤¾à¤·à¤¾"),
    SETTINGS_DATE_SYSTEM("Date System", "à¤®à¤¿à¤¤à¤¿ à¤ªà¥à¤°à¤£à¤¾à¤²à¥€"),
    SETTINGS_THEME("Theme", "à¤¥à¤¿à¤®"),

    // Theme options
    THEME_LIGHT("Light", "à¤‰à¤œà¥à¤¯à¤¾à¤²à¥‹"),
    THEME_LIGHT_DESC("Always use light theme", "à¤¸à¤§à¥ˆà¤‚ à¤‰à¤œà¥à¤¯à¤¾à¤²à¥‹ à¤¥à¤¿à¤® à¤ªà¥à¤°à¤¯à¥‹à¤— à¤—à¤°à¥à¤¨à¥à¤¹à¥‹à¤¸à¥"),
    THEME_DARK("Dark", "à¤…à¤à¤§à¥à¤¯à¤¾à¤°à¥‹"),
    THEME_DARK_DESC("Always use dark theme", "à¤¸à¤§à¥ˆà¤‚ à¤…à¤à¤§à¥à¤¯à¤¾à¤°à¥‹ à¤¥à¤¿à¤® à¤ªà¥à¤°à¤¯à¥‹à¤— à¤—à¤°à¥à¤¨à¥à¤¹à¥‹à¤¸à¥"),
    THEME_SYSTEM("System", "à¤ªà¥à¤°à¤£à¤¾à¤²à¥€"),
    THEME_SYSTEM_DESC("Follow device settings", "à¤¯à¤¨à¥à¤¤à¥à¤° à¤¸à¥‡à¤Ÿà¤¿à¤™à¥à¤¸ à¤ªà¤›à¥à¤¯à¤¾à¤‰à¤¨à¥à¤¹à¥‹à¤¸à¥"),

    // About section
    SETTINGS_ABOUT_APP("About AstroStorm", "AstroStorm à¤¬à¤¾à¤°à¥‡à¤®à¤¾"),
    SETTINGS_VERSION("Version %s", "à¤¸à¤‚à¤¸à¥à¤•à¤°à¤£ %s"),
    SETTINGS_CALC_ENGINE("Calculation Engine", "à¤—à¤£à¤¨à¤¾ à¤‡à¤¨à¥à¤œà¤¿à¤¨"),
    SETTINGS_CALC_ENGINE_DESC("Swiss Ephemeris (JPL Mode)", "à¤¸à¥à¤µà¤¿à¤¸ à¤ˆà¤«à¥‡à¤®à¥‡à¤°à¤¿à¤¸ (JPL à¤®à¥‹à¤¡)"),
    SETTINGS_APP_TAGLINE("Ultra-Precision Vedic Astrology", "à¤…à¤¤à¤¿-à¤¸à¤Ÿà¥€à¤• à¤µà¥ˆà¤¦à¤¿à¤• à¤œà¥à¤¯à¥‹à¤¤à¤¿à¤·"),
    SETTINGS_APP_DESC("Powered by Swiss Ephemeris with JPL planetary data for astronomical-grade accuracy in all calculations.", "à¤¸à¤¬à¥ˆ à¤—à¤£à¤¨à¤¾à¤¹à¤°à¥‚à¤®à¤¾ à¤–à¤—à¥‹à¤²à¥€à¤¯-à¤—à¥à¤°à¥‡à¤¡ à¤¸à¤Ÿà¥€à¤•à¤¤à¤¾à¤•à¥‹ à¤²à¤¾à¤—à¤¿ JPL à¤—à¥à¤°à¤¹ à¤¡à¤¾à¤Ÿà¤¾à¤¸à¤¹à¤¿à¤¤ à¤¸à¥à¤µà¤¿à¤¸ à¤ˆà¤«à¥‡à¤®à¥‡à¤°à¤¿à¤¸à¤¦à¥à¤µà¤¾à¤°à¤¾ à¤¸à¤‚à¤šà¤¾à¤²à¤¿à¤¤à¥¤"),
    SETTINGS_LAHIRI("Lahiri Ayanamsa", "à¤²à¤¹à¤¿à¤°à¥€ à¤…à¤¯à¤¨à¤¾à¤‚à¤¶"),
    SETTINGS_PLACIDUS("Placidus Houses", "à¤ªà¥à¤²à¤¾à¤¸à¤¿à¤¡à¤¸ à¤­à¤¾à¤µà¤¹à¤°à¥‚"),

    // Delete dialog
    DIALOG_DELETE_PROFILE("Delete Profile", "à¤ªà¥à¤°à¥‹à¤«à¤¾à¤‡à¤² à¤®à¥‡à¤Ÿà¥à¤¨à¥à¤¹à¥‹à¤¸à¥"),
    DIALOG_DELETE_CONFIRM("Are you sure you want to delete %s? This action cannot be undone.", "à¤•à¥‡ à¤¤à¤ªà¤¾à¤ˆà¤‚ %s à¤®à¥‡à¤Ÿà¥à¤¨ à¤šà¤¾à¤¹à¤¨à¥à¤¹à¥à¤¨à¥à¤›? à¤¯à¥‹ à¤•à¤¾à¤°à¥à¤¯ à¤ªà¥‚à¤°à¥à¤µà¤µà¤¤ à¤—à¤°à¥à¤¨ à¤¸à¤•à¤¿à¤à¤¦à¥ˆà¤¨à¥¤"),
    DIALOG_EXPORT_CHART("Export Chart", "à¤•à¥à¤£à¥à¤¡à¤²à¥€ à¤¨à¤¿à¤°à¥à¤¯à¤¾à¤¤ à¤—à¤°à¥à¤¨à¥à¤¹à¥‹à¤¸à¥"),

    // Chart detail labels
    CHART_ASCENDANT("Ascendant", "à¤²à¤—à¥à¤¨"),
    CHART_MOON_SIGN("Moon Sign", "à¤šà¤¨à¥à¤¦à¥à¤° à¤°à¤¾à¤¶à¤¿"),
    CHART_NAKSHATRA("Nakshatra", "à¤¨à¤•à¥à¤·à¤¤à¥à¤°"),
    MISC_INFO("Information", "à¤œà¤¾à¤¨à¤•à¤¾à¤°à¥€"),

    // ============================================
    // CHART INPUT SCREEN
    // ============================================
    INPUT_NEW_CHART("New Birth Chart", "à¤¨à¤¯à¤¾à¤ à¤œà¤¨à¥à¤® à¤•à¥à¤£à¥à¤¡à¤²à¥€"),
    INPUT_EDIT_CHART("Edit Birth Chart", "à¤œà¤¨à¥à¤® à¤•à¥à¤£à¥à¤¡à¤²à¥€ à¤¸à¤®à¥à¤ªà¤¾à¤¦à¤¨"),
    INPUT_IDENTITY("Identity", "à¤ªà¤¹à¤¿à¤šà¤¾à¤¨"),
    INPUT_DATE_TIME("Date & Time", "à¤®à¤¿à¤¤à¤¿ à¤° à¤¸à¤®à¤¯"),
    INPUT_COORDINATES("Coordinates", "à¤¨à¤¿à¤°à¥à¤¦à¥‡à¤¶à¤¾à¤‚à¤•à¤¹à¤°à¥‚"),

    INPUT_FULL_NAME("Full name", "à¤ªà¥‚à¤°à¤¾ à¤¨à¤¾à¤®"),
    INPUT_GENDER("Gender", "à¤²à¤¿à¤™à¥à¤—"),
    INPUT_LOCATION("Location", "à¤¸à¥à¤¥à¤¾à¤¨"),
    INPUT_SEARCH_LOCATION("Search city or enter manually", "à¤¶à¤¹à¤° à¤–à¥‹à¤œà¥à¤¨à¥à¤¹à¥‹à¤¸à¥ à¤µà¤¾ à¤®à¥à¤¯à¤¾à¤¨à¥à¤…à¤² à¤°à¥‚à¤ªà¤®à¤¾ à¤ªà¥à¤°à¤µà¤¿à¤·à¥à¤Ÿ à¤—à¤°à¥à¤¨à¥à¤¹à¥‹à¤¸à¥"),
    INPUT_TIMEZONE("Timezone", "à¤¸à¤®à¤¯ à¤•à¥à¤·à¥‡à¤¤à¥à¤°"),
    INPUT_LATITUDE("Latitude", "à¤…à¤•à¥à¤·à¤¾à¤‚à¤¶"),
    INPUT_LONGITUDE("Longitude", "à¤¦à¥‡à¤¶à¤¾à¤¨à¥à¤¤à¤°"),
    INPUT_ALTITUDE("Altitude (m) - Optional", "à¤‰à¤šà¤¾à¤ˆ (à¤®à¤¿.) - à¤µà¥ˆà¤•à¤²à¥à¤ªà¤¿à¤•"),

    INPUT_SELECT_DATE("Select date", "à¤®à¤¿à¤¤à¤¿ à¤›à¤¾à¤¨à¥à¤¨à¥à¤¹à¥‹à¤¸à¥"),
    INPUT_SELECT_TIME("Select time", "à¤¸à¤®à¤¯ à¤›à¤¾à¤¨à¥à¤¨à¥à¤¹à¥‹à¤¸à¥"),

    // Validation errors
    ERROR_INPUT("Input Error", "à¤‡à¤¨à¤ªà¥à¤Ÿ à¤¤à¥à¤°à¥à¤Ÿà¤¿"),
    ERROR_INVALID_COORDS("Please enter valid latitude and longitude", "à¤•à¥ƒà¤ªà¤¯à¤¾ à¤®à¤¾à¤¨à¥à¤¯ à¤…à¤•à¥à¤·à¤¾à¤‚à¤¶ à¤° à¤¦à¥‡à¤¶à¤¾à¤¨à¥à¤¤à¤° à¤ªà¥à¤°à¤µà¤¿à¤·à¥à¤Ÿ à¤—à¤°à¥à¤¨à¥à¤¹à¥‹à¤¸à¥"),
    ERROR_LATITUDE_RANGE("Latitude must be between -90 and 90", "à¤…à¤•à¥à¤·à¤¾à¤‚à¤¶ -à¥¯à¥¦ à¤° à¥¯à¥¦ à¤¬à¥€à¤šà¤®à¤¾ à¤¹à¥à¤¨à¥à¤ªà¤°à¥à¤›"),
    ERROR_LONGITUDE_RANGE("Longitude must be between -180 and 180", "à¤¦à¥‡à¤¶à¤¾à¤¨à¥à¤¤à¤° -à¥§à¥®à¥¦ à¤° à¥§à¥®à¥¦ à¤¬à¥€à¤šà¤®à¤¾ à¤¹à¥à¤¨à¥à¤ªà¤°à¥à¤›"),
    ERROR_CHECK_INPUT("Please check your input values", "à¤•à¥ƒà¤ªà¤¯à¤¾ à¤†à¤«à¥à¤¨à¥‹ à¤‡à¤¨à¤ªà¥à¤Ÿ à¤®à¤¾à¤¨à¤¹à¤°à¥‚ à¤œà¤¾à¤à¤š à¤—à¤°à¥à¤¨à¥à¤¹à¥‹à¤¸à¥"),
    ERROR_CALCULATION_FAILED("Calculation failed", "à¤—à¤£à¤¨à¤¾ à¤…à¤¸à¤«à¤² à¤­à¤¯à¥‹"),
    ERROR_RATE_LIMIT("Too many requests. Please wait.", "à¤§à¥‡à¤°à¥ˆ à¤…à¤¨à¥à¤°à¥‹à¤§à¤¹à¤°à¥‚à¥¤ à¤•à¥ƒà¤ªà¤¯à¤¾ à¤ªà¤°à¥à¤–à¤¨à¥à¤¹à¥‹à¤¸à¥à¥¤"),
    ERROR_SEARCH_FAILED("Search failed. Please try again.", "à¤–à¥‹à¤œ à¤…à¤¸à¤«à¤² à¤­à¤¯à¥‹à¥¤ à¤•à¥ƒà¤ªà¤¯à¤¾ à¤«à¥‡à¤°à¤¿ à¤ªà¥à¤°à¤¯à¤¾à¤¸ à¤—à¤°à¥à¤¨à¥à¤¹à¥‹à¤¸à¥à¥¤"),
    ERROR_RATE_LIMIT_EXCEEDED("Rate limit exceeded", "à¤¦à¤° à¤¸à¥€à¤®à¤¾ à¤¨à¤¾à¤˜à¥à¤¯à¥‹"),
    ERROR_NAME_TOO_LONG("Name must be 100 characters or less", "à¤¨à¤¾à¤® à¥§à¥¦à¥¦ à¤µà¤°à¥à¤£ à¤µà¤¾ à¤•à¤® à¤¹à¥à¤¨à¥à¤ªà¤°à¥à¤›"),
    ERROR_DATE_IN_FUTURE("Birth date cannot be in the future", "à¤œà¤¨à¥à¤® à¤®à¤¿à¤¤à¤¿ à¤­à¤µà¤¿à¤·à¥à¤¯à¤®à¤¾ à¤¹à¥à¤¨ à¤¸à¤•à¥à¤¦à¥ˆà¤¨"),
    ERROR_DATE_TOO_OLD("Date must be after year 1800", "à¤®à¤¿à¤¤à¤¿ à¤µà¤°à¥à¤· à¥§à¥®à¥¦à¥¦ à¤ªà¤›à¤¿à¤•à¥‹ à¤¹à¥à¤¨à¥à¤ªà¤°à¥à¤›"),
    ERROR_LOCATION_REQUIRED("Please enter a location or coordinates", "à¤•à¥ƒà¤ªà¤¯à¤¾ à¤¸à¥à¤¥à¤¾à¤¨ à¤µà¤¾ à¤¨à¤¿à¤°à¥à¤¦à¥‡à¤¶à¤¾à¤‚à¤• à¤ªà¥à¤°à¤µà¤¿à¤·à¥à¤Ÿ à¤—à¤°à¥à¤¨à¥à¤¹à¥‹à¤¸à¥"),
    ERROR_TIMEZONE_INVALID("Please select a valid timezone", "à¤•à¥ƒà¤ªà¤¯à¤¾ à¤®à¤¾à¤¨à¥à¤¯ à¤¸à¤®à¤¯ à¤•à¥à¤·à¥‡à¤¤à¥à¤° à¤›à¤¾à¤¨à¥à¤¨à¥à¤¹à¥‹à¤¸à¥"),

    // Location Search
    LOCATION_SEARCH("Search location", "à¤¸à¥à¤¥à¤¾à¤¨ à¤–à¥‹à¤œà¥à¤¨à¥à¤¹à¥‹à¤¸à¥"),
    LOCATION_PLACEHOLDER("Enter city or place name", "à¤¶à¤¹à¤° à¤µà¤¾ à¤ à¤¾à¤‰à¤à¤•à¥‹ à¤¨à¤¾à¤® à¤ªà¥à¤°à¤µà¤¿à¤·à¥à¤Ÿ à¤—à¤°à¥à¤¨à¥à¤¹à¥‹à¤¸à¥"),
    LOCATION_CLEAR("Clear search", "à¤–à¥‹à¤œ à¤¹à¤Ÿà¤¾à¤‰à¤¨à¥à¤¹à¥‹à¤¸à¥"),
    LOCATION_SELECT("Select %s", "%s à¤›à¤¾à¤¨à¥à¤¨à¥à¤¹à¥‹à¤¸à¥"),

    // ============================================
    // PROFILE EDIT SCREEN
    // ============================================
    EDIT_PROFILE_TITLE("Edit Profile", "à¤ªà¥à¤°à¥‹à¤«à¤¾à¤‡à¤² à¤¸à¤®à¥à¤ªà¤¾à¤¦à¤¨"),
    EDIT_PROFILE_NO_DATA("No chart data available. Please select a profile to edit.", "à¤•à¥à¤¨à¥ˆ à¤•à¥à¤£à¥à¤¡à¤²à¥€ à¤¡à¤¾à¤Ÿà¤¾ à¤‰à¤ªà¤²à¤¬à¥à¤§ à¤›à¥ˆà¤¨à¥¤ à¤•à¥ƒà¤ªà¤¯à¤¾ à¤¸à¤®à¥à¤ªà¤¾à¤¦à¤¨ à¤—à¤°à¥à¤¨ à¤ªà¥à¤°à¥‹à¤«à¤¾à¤‡à¤² à¤›à¤¾à¤¨à¥à¤¨à¥à¤¹à¥‹à¤¸à¥à¥¤"),

    // ============================================
    // GENDER OPTIONS
    // ============================================
    GENDER_MALE("Male", "à¤ªà¥à¤°à¥à¤·"),
    GENDER_FEMALE("Female", "à¤®à¤¹à¤¿à¤²à¤¾"),
    GENDER_OTHER("Other", "à¤…à¤¨à¥à¤¯"),

    // ============================================
    // PLANETS
    // ============================================
    PLANET_SUN("Sun", "à¤¸à¥‚à¤°à¥à¤¯"),
    PLANET_MOON("Moon", "à¤šà¤¨à¥à¤¦à¥à¤°"),
    PLANET_MERCURY("Mercury", "à¤¬à¥à¤§"),
    PLANET_VENUS("Venus", "à¤¶à¥à¤•à¥à¤°"),
    PLANET_MARS("Mars", "à¤®à¤‚à¤—à¤²"),
    PLANET_JUPITER("Jupiter", "à¤¬à¥ƒà¤¹à¤¸à¥à¤ªà¤¤à¤¿"),
    PLANET_SATURN("Saturn", "à¤¶à¤¨à¤¿"),
    PLANET_RAHU("Rahu", "à¤°à¤¾à¤¹à¥"),
    PLANET_KETU("Ketu", "à¤•à¥‡à¤¤à¥"),
    PLANET_URANUS("Uranus", "à¤¯à¥à¤°à¥‡à¤¨à¤¸"),
    PLANET_NEPTUNE("Neptune", "à¤¨à¥‡à¤ªà¥à¤šà¥à¤¯à¥à¤¨"),
    PLANET_PLUTO("Pluto", "à¤ªà¥à¤²à¥à¤Ÿà¥‹"),

    // ============================================
    // ZODIAC SIGNS
    // ============================================
    SIGN_ARIES("Aries", "à¤®à¥‡à¤·"),
    SIGN_TAURUS("Taurus", "à¤µà¥ƒà¤·"),
    SIGN_GEMINI("Gemini", "à¤®à¤¿à¤¥à¥à¤¨"),
    SIGN_CANCER("Cancer", "à¤•à¤°à¥à¤•à¤Ÿ"),
    SIGN_LEO("Leo", "à¤¸à¤¿à¤‚à¤¹"),
    SIGN_VIRGO("Virgo", "à¤•à¤¨à¥à¤¯à¤¾"),
    SIGN_LIBRA("Libra", "à¤¤à¥à¤²à¤¾"),
    SIGN_SCORPIO("Scorpio", "à¤µà¥ƒà¤¶à¥à¤šà¤¿à¤•"),
    SIGN_SAGITTARIUS("Sagittarius", "à¤§à¤¨à¥"),
    SIGN_CAPRICORN("Capricorn", "à¤®à¤•à¤°"),
    SIGN_AQUARIUS("Aquarius", "à¤•à¥à¤®à¥à¤­"),
    SIGN_PISCES("Pisces", "à¤®à¥€à¤¨"),

    // ============================================
    // NAKSHATRAS
    // ============================================
    NAKSHATRA_ASHWINI("Ashwini", "à¤…à¤¶à¥à¤µà¤¿à¤¨à¥€"),
    NAKSHATRA_BHARANI("Bharani", "à¤­à¤°à¤£à¥€"),
    NAKSHATRA_KRITTIKA("Krittika", "à¤•à¥ƒà¤¤à¥à¤¤à¤¿à¤•à¤¾"),
    NAKSHATRA_ROHINI("Rohini", "à¤°à¥‹à¤¹à¤¿à¤£à¥€"),
    NAKSHATRA_MRIGASHIRA("Mrigashira", "à¤®à¥ƒà¤—à¤¶à¤¿à¤°à¤¾"),
    NAKSHATRA_ARDRA("Ardra", "à¤†à¤°à¥à¤¦à¥à¤°à¤¾"),
    NAKSHATRA_PUNARVASU("Punarvasu", "à¤ªà¥à¤¨à¤°à¥à¤µà¤¸à¥"),
    NAKSHATRA_PUSHYA("Pushya", "à¤ªà¥à¤·à¥à¤¯"),
    NAKSHATRA_ASHLESHA("Ashlesha", "à¤†à¤¶à¥à¤²à¥‡à¤·à¤¾"),
    NAKSHATRA_MAGHA("Magha", "à¤®à¤˜à¤¾"),
    NAKSHATRA_PURVA_PHALGUNI("Purva Phalguni", "à¤ªà¥‚à¤°à¥à¤µà¤¾ à¤«à¤¾à¤²à¥à¤—à¥à¤¨à¥€"),
    NAKSHATRA_UTTARA_PHALGUNI("Uttara Phalguni", "à¤‰à¤¤à¥à¤¤à¤°à¤¾ à¤«à¤¾à¤²à¥à¤—à¥à¤¨à¥€"),
    NAKSHATRA_HASTA("Hasta", "à¤¹à¤¸à¥à¤¤"),
    NAKSHATRA_CHITRA("Chitra", "à¤šà¤¿à¤¤à¥à¤°à¤¾"),
    NAKSHATRA_SWATI("Swati", "à¤¸à¥à¤µà¤¾à¤¤à¤¿"),
    NAKSHATRA_VISHAKHA("Vishakha", "à¤µà¤¿à¤¶à¤¾à¤–à¤¾"),
    NAKSHATRA_ANURADHA("Anuradha", "à¤…à¤¨à¥à¤°à¤¾à¤§à¤¾"),
    NAKSHATRA_JYESHTHA("Jyeshtha", "à¤œà¥à¤¯à¥‡à¤·à¥à¤ à¤¾"),
    NAKSHATRA_MULA("Mula", "à¤®à¥‚à¤²"),
    NAKSHATRA_PURVA_ASHADHA("Purva Ashadha", "à¤ªà¥‚à¤°à¥à¤µà¤¾à¤·à¤¾à¤¢à¤¾"),
    NAKSHATRA_UTTARA_ASHADHA("Uttara Ashadha", "à¤‰à¤¤à¥à¤¤à¤°à¤¾à¤·à¤¾à¤¢à¤¾"),
    NAKSHATRA_SHRAVANA("Shravana", "à¤¶à¥à¤°à¤µà¤£"),
    NAKSHATRA_DHANISHTHA("Dhanishtha", "à¤§à¤¨à¤¿à¤·à¥à¤ à¤¾"),
    NAKSHATRA_SHATABHISHA("Shatabhisha", "à¤¶à¤¤à¤­à¤¿à¤·à¤¾"),
    NAKSHATRA_PURVA_BHADRAPADA("Purva Bhadrapada", "à¤ªà¥‚à¤°à¥à¤µà¤­à¤¾à¤¦à¥à¤°à¤ªà¤¦"),
    NAKSHATRA_UTTARA_BHADRAPADA("Uttara Bhadrapada", "à¤‰à¤¤à¥à¤¤à¤°à¤­à¤¾à¤¦à¥à¤°à¤ªà¤¦"),
    NAKSHATRA_REVATI("Revati", "à¤°à¥‡à¤µà¤¤à¥€"),

    // ============================================
    // HOUSE SYSTEMS
    // ============================================
    HOUSE_PLACIDUS("Placidus", "à¤ªà¥à¤²à¤¾à¤¸à¤¿à¤¡à¤¸"),
    HOUSE_KOCH("Koch", "à¤•à¥‹à¤š"),
    HOUSE_PORPHYRIUS("Porphyrius", "à¤ªà¥‹à¤°à¥à¤«à¤¿à¤°à¤¿à¤¯à¤¸"),
    HOUSE_REGIOMONTANUS("Regiomontanus", "à¤°à¥‡à¤œà¤¿à¤“à¤®à¥‹à¤¨à¥à¤Ÿà¤¾à¤¨à¤¸"),
    HOUSE_CAMPANUS("Campanus", "à¤•à¥à¤¯à¤¾à¤®à¥à¤ªà¤¾à¤¨à¤¸"),
    HOUSE_EQUAL("Equal", "à¤¸à¤®à¤¾à¤¨"),
    HOUSE_WHOLE_SIGN("Whole Sign", "à¤¸à¤®à¥à¤ªà¥‚à¤°à¥à¤£ à¤°à¤¾à¤¶à¤¿"),
    HOUSE_VEHLOW("Vehlow", "à¤­à¥‡à¤¹à¤²à¥‹"),
    HOUSE_MERIDIAN("Meridian", "à¤®à¥‡à¤°à¤¿à¤¡à¤¿à¤¯à¤¨"),
    HOUSE_MORINUS("Morinus", "à¤®à¥‹à¤°à¤¿à¤¨à¤¸"),
    HOUSE_ALCABITUS("Alcabitus", "à¤…à¤²à¥à¤•à¤¾à¤¬à¤¿à¤Ÿà¤¸"),

    // ============================================
    // AYANAMSA OPTIONS
    // ============================================
    AYANAMSA_LAHIRI("Lahiri", "à¤²à¤¹à¤¿à¤°à¥€"),
    AYANAMSA_RAMAN("Raman", "à¤°à¤®à¤£"),
    AYANAMSA_KRISHNAMURTI("Krishnamurti", "à¤•à¥ƒà¤·à¥à¤£à¤®à¥‚à¤°à¥à¤¤à¤¿"),
    AYANAMSA_TRUE_CHITRAPAKSHA("True Chitrapaksha", "à¤¸à¤¤à¥à¤¯ à¤šà¤¿à¤¤à¥à¤°à¤ªà¤•à¥à¤·"),

    // ============================================
    // YOGA ANALYSIS
    // ============================================
    YOGA_ANALYSIS_SUMMARY("Yoga Analysis Summary", "à¤¯à¥‹à¤— à¤µà¤¿à¤¶à¥à¤²à¥‡à¤·à¤£ à¤¸à¤¾à¤°à¤¾à¤‚à¤¶"),
    YOGA_OVERALL_STRENGTH("Overall Yoga Strength", "à¤¸à¤®à¤—à¥à¤° à¤¯à¥‹à¤— à¤¬à¤²"),
    YOGA_TOTAL("Total Yogas", "à¤•à¥à¤² à¤¯à¥‹à¤—à¤¹à¤°à¥‚"),
    YOGA_AUSPICIOUS("Auspicious", "à¤¶à¥à¤­"),
    YOGA_CHALLENGING("Challenging", "à¤šà¥à¤¨à¥Œà¤¤à¥€à¤ªà¥‚à¤°à¥à¤£"),
    YOGA_ALL("All", "à¤¸à¤¬à¥ˆ"),
    YOGA_COUNT_DETECTED("%d yogas detected", "%d à¤¯à¥‹à¤—à¤¹à¤°à¥‚ à¤ªà¤¤à¥à¤¤à¤¾ à¤²à¤¾à¤—à¥‡à¤•à¥‹"),
    YOGA_INFORMATION("Yoga Information", "à¤¯à¥‹à¤— à¤œà¤¾à¤¨à¤•à¤¾à¤°à¥€"),
    YOGA_ABOUT_TITLE("About Vedic Yogas", "à¤µà¥ˆà¤¦à¤¿à¤• à¤¯à¥‹à¤—à¤¹à¤°à¥‚à¤•à¥‹ à¤¬à¤¾à¤°à¥‡à¤®à¤¾"),
    YOGA_ABOUT_DESCRIPTION("Yogas are special planetary combinations in Vedic astrology that indicate specific life patterns, talents, and karmic influences.", "à¤¯à¥‹à¤—à¤¹à¤°à¥‚ à¤µà¥ˆà¤¦à¤¿à¤• à¤œà¥à¤¯à¥‹à¤¤à¤¿à¤·à¤®à¤¾ à¤µà¤¿à¤¶à¥‡à¤· à¤—à¥à¤°à¤¹ à¤¸à¤‚à¤¯à¥‹à¤œà¤¨à¤¹à¤°à¥‚ à¤¹à¥à¤¨à¥ à¤œà¤¸à¤²à¥‡ à¤µà¤¿à¤¶à¤¿à¤·à¥à¤Ÿ à¤œà¥€à¤µà¤¨ à¤¢à¤¾à¤à¤šà¤¾, à¤ªà¥à¤°à¤¤à¤¿à¤­à¤¾ à¤° à¤•à¤°à¥à¤® à¤ªà¥à¤°à¤­à¤¾à¤µà¤¹à¤°à¥‚ à¤¸à¤‚à¤•à¥‡à¤¤ à¤—à¤°à¥à¤›à¤¨à¥à¥¤"),
    YOGA_CATEGORIES_TITLE("Categories", "à¤µà¤°à¥à¤—à¤¹à¤°à¥‚"),
    YOGA_GOT_IT("Got it", "à¤¬à¥à¤à¥‡à¤‚"),
    YOGA_NO_DATA("No yoga data available", "à¤•à¥à¤¨à¥ˆ à¤¯à¥‹à¤— à¤¡à¤¾à¤Ÿà¤¾ à¤‰à¤ªà¤²à¤¬à¥à¤§ à¤›à¥ˆà¤¨"),
    YOGA_NO_CHART_MESSAGE("Select or create a birth profile to view yogas.", "à¤¯à¥‹à¤—à¤¹à¤°à¥‚ à¤¹à¥‡à¤°à¥à¤¨ à¤œà¤¨à¥à¤® à¤ªà¥à¤°à¥‹à¤«à¤¾à¤‡à¤² à¤›à¤¾à¤¨à¥à¤¨à¥à¤¹à¥‹à¤¸à¥ à¤µà¤¾ à¤¬à¤¨à¤¾à¤‰à¤¨à¥à¤¹à¥‹à¤¸à¥à¥¤"),
    YOGAS_COUNT_DETECTED("%1\$d yogas detected in %2\$s", "%2\$s à¤®à¤¾ %1\$d à¤¯à¥‹à¤—à¤¹à¤°à¥‚ à¤ªà¤¤à¥à¤¤à¤¾ à¤²à¤¾à¤—à¥‡à¤•à¥‹"),

    // Yoga Categories
    YOGA_CATEGORY_WEALTH("Wealth Yogas", "à¤§à¤¨ à¤¯à¥‹à¤—à¤¹à¤°à¥‚"),
    YOGA_CATEGORY_WEALTH_DESC("Combinations for prosperity", "à¤¸à¤®à¥ƒà¤¦à¥à¤§à¤¿à¤•à¤¾ à¤²à¤¾à¤—à¤¿ à¤¸à¤‚à¤¯à¥‹à¤œà¤¨à¤¹à¤°à¥‚"),
    YOGA_CATEGORY_RAJA("Raja Yogas", "à¤°à¤¾à¤œ à¤¯à¥‹à¤—à¤¹à¤°à¥‚"),
    YOGA_CATEGORY_RAJA_DESC("Combinations for power & authority", "à¤¶à¤•à¥à¤¤à¤¿ à¤° à¤…à¤§à¤¿à¤•à¤¾à¤°à¤•à¤¾ à¤²à¤¾à¤—à¤¿ à¤¸à¤‚à¤¯à¥‹à¤œà¤¨à¤¹à¤°à¥‚"),
    YOGA_CATEGORY_SPIRITUAL("Spiritual Yogas", "à¤†à¤§à¥à¤¯à¤¾à¤¤à¥à¤®à¤¿à¤• à¤¯à¥‹à¤—à¤¹à¤°à¥‚"),
    YOGA_CATEGORY_SPIRITUAL_DESC("Combinations for spiritual growth", "à¤†à¤§à¥à¤¯à¤¾à¤¤à¥à¤®à¤¿à¤• à¤µà¥ƒà¤¦à¥à¤§à¤¿à¤•à¤¾ à¤²à¤¾à¤—à¤¿ à¤¸à¤‚à¤¯à¥‹à¤œà¤¨à¤¹à¤°à¥‚"),
    YOGA_CATEGORY_CHALLENGING("Challenging Yogas", "à¤šà¥à¤¨à¥Œà¤¤à¥€à¤ªà¥‚à¤°à¥à¤£ à¤¯à¥‹à¤—à¤¹à¤°à¥‚"),
    YOGA_CATEGORY_CHALLENGING_DESC("Combinations indicating obstacles", "à¤¬à¤¾à¤§à¤¾à¤¹à¤°à¥‚ à¤¸à¤‚à¤•à¥‡à¤¤ à¤—à¤°à¥à¤¨à¥‡ à¤¸à¤‚à¤¯à¥‹à¤œà¤¨à¤¹à¤°à¥‚"),
    YOGA_CATEGORY_OTHER("Other Yogas", "à¤…à¤¨à¥à¤¯ à¤¯à¥‹à¤—à¤¹à¤°à¥‚"),
    YOGA_CATEGORY_OTHER_DESC("Other planetary combinations", "à¤…à¤¨à¥à¤¯ à¤—à¥à¤°à¤¹ à¤¸à¤‚à¤¯à¥‹à¤œà¤¨à¤¹à¤°à¥‚"),
    YOGA_CATEGORY_DHANA("Dhana Yogas", "à¤§à¤¨ à¤¯à¥‹à¤—à¤¹à¤°à¥‚"),
    YOGA_CATEGORY_DHANA_DESC("Combinations for wealth", "à¤§à¤¨à¤•à¤¾ à¤²à¤¾à¤—à¤¿ à¤¸à¤‚à¤¯à¥‹à¤œà¤¨à¤¹à¤°à¥‚"),
    YOGA_CATEGORY_MAHAPURUSHA("Mahapurusha Yogas", "à¤®à¤¹à¤¾à¤ªà¥à¤°à¥à¤· à¤¯à¥‹à¤—à¤¹à¤°à¥‚"),
    YOGA_CATEGORY_MAHAPURUSHA_DESC("Great personality combinations", "à¤®à¤¹à¤¾à¤¨ à¤µà¥à¤¯à¤•à¥à¤¤à¤¿à¤¤à¥à¤µ à¤¸à¤‚à¤¯à¥‹à¤œà¤¨à¤¹à¤°à¥‚"),
    YOGA_CATEGORY_NABHASA("Nabhasa Yogas", "à¤¨à¤¾à¤­à¤¸ à¤¯à¥‹à¤—à¤¹à¤°à¥‚"),
    YOGA_CATEGORY_NABHASA_DESC("Celestial combinations", "à¤†à¤•à¤¾à¤¶à¥€à¤¯ à¤¸à¤‚à¤¯à¥‹à¤œà¤¨à¤¹à¤°à¥‚"),
    YOGA_CATEGORY_CHANDRA("Chandra Yogas", "à¤šà¤¨à¥à¤¦à¥à¤° à¤¯à¥‹à¤—à¤¹à¤°à¥‚"),
    YOGA_CATEGORY_CHANDRA_DESC("Moon-based combinations", "à¤šà¤¨à¥à¤¦à¥à¤°à¤®à¤¾à¤®à¤¾ à¤†à¤§à¤¾à¤°à¤¿à¤¤ à¤¸à¤‚à¤¯à¥‹à¤œà¤¨à¤¹à¤°à¥‚"),
    YOGA_CATEGORY_SOLAR("Solar Yogas", "à¤¸à¥‚à¤°à¥à¤¯ à¤¯à¥‹à¤—à¤¹à¤°à¥‚"),
    YOGA_CATEGORY_SOLAR_DESC("Sun-based combinations", "à¤¸à¥‚à¤°à¥à¤¯à¤®à¤¾ à¤†à¤§à¤¾à¤°à¤¿à¤¤ à¤¸à¤‚à¤¯à¥‹à¤œà¤¨à¤¹à¤°à¥‚"),
    YOGA_CATEGORY_SPECIAL("Special Yogas", "à¤µà¤¿à¤¶à¥‡à¤· à¤¯à¥‹à¤—à¤¹à¤°à¥‚"),
    YOGA_CATEGORY_SPECIAL_DESC("Rare and special combinations", "à¤¦à¥à¤°à¥à¤²à¤­ à¤° à¤µà¤¿à¤¶à¥‡à¤· à¤¸à¤‚à¤¯à¥‹à¤œà¤¨à¤¹à¤°à¥‚"),
    YOGA_CATEGORY_NEGATIVE("Negative Yogas", "à¤¨à¤•à¤¾à¤°à¤¾à¤¤à¥à¤®à¤• à¤¯à¥‹à¤—à¤¹à¤°à¥‚"),
    YOGA_CATEGORY_NEGATIVE_DESC("Combinations indicating challenges", "à¤šà¥à¤¨à¥Œà¤¤à¥€à¤¹à¤°à¥‚ à¤¸à¤‚à¤•à¥‡à¤¤ à¤—à¤°à¥à¤¨à¥‡ à¤¸à¤‚à¤¯à¥‹à¤œà¤¨à¤¹à¤°à¥‚"),

    // Yoga Tab Content UI Strings
    YOGA_MOST_SIGNIFICANT("Most Significant Yogas", "à¤¸à¤¬à¥ˆà¤­à¤¨à¥à¤¦à¤¾ à¤®à¤¹à¤¤à¥à¤¤à¥à¤µà¤ªà¥‚à¤°à¥à¤£ à¤¯à¥‹à¤—à¤¹à¤°à¥‚"),
    YOGA_SANSKRIT("Sanskrit", "à¤¸à¤‚à¤¸à¥à¤•à¥ƒà¤¤"),
    YOGA_EFFECTS("Effects", "à¤ªà¥à¤°à¤­à¤¾à¤µà¤¹à¤°à¥‚"),
    YOGA_ACTIVATION("Activation", "à¤¸à¤•à¥à¤°à¤¿à¤¯à¤¤à¤¾"),
    YOGA_CANCELLATION_FACTORS("Cancellation/Mitigation Factors", "à¤°à¤¦à¥à¤¦/à¤¨à¥à¤¯à¥‚à¤¨à¥€à¤•à¤°à¤£ à¤•à¤¾à¤°à¤•à¤¹à¤°à¥‚"),
    YOGA_NO_CATEGORY_FOUND("No %s found", "à¤•à¥à¤¨à¥ˆ %s à¤«à¥‡à¤²à¤¾ à¤ªà¤°à¥‡à¤¨"),
    YOGA_NONE_DETECTED("No yogas detected", "à¤•à¥à¤¨à¥ˆ à¤¯à¥‹à¤— à¤ªà¤¤à¥à¤¤à¤¾ à¤²à¤¾à¤—à¥‡à¤¨"),
    YOGA_HOUSE_PREFIX("H", "à¤­à¤¾à¤µ"),
    YOGA_SUBTITLE("Planetary combinations in your chart", "à¤¤à¤ªà¤¾à¤ˆà¤‚à¤•à¥‹ à¤•à¥à¤£à¥à¤¡à¤²à¥€à¤®à¤¾ à¤—à¥à¤°à¤¹ à¤¸à¤‚à¤¯à¥‹à¤œà¤¨à¤¹à¤°à¥‚"),
    YOGA_STRENGTH("Strength", "à¤¬à¤²"),
    YOGA_DOMINANT_CATEGORY("Dominant Category", "à¤ªà¥à¤°à¤®à¥à¤– à¤µà¤°à¥à¤—"),
    YOGA_FILTER_BY_CATEGORY("Filter by Category", "à¤µà¤°à¥à¤—à¤…à¤¨à¥à¤¸à¤¾à¤° à¤«à¤¿à¤²à¥à¤Ÿà¤° à¤—à¤°à¥à¤¨à¥à¤¹à¥‹à¤¸à¥"),
    YOGA_COUNT_SUFFIX("yogas", "à¤¯à¥‹à¤—à¤¹à¤°à¥‚"),
    YOGA_PLANETS_INVOLVED("Planets Involved", "à¤¸à¤‚à¤²à¤—à¥à¤¨ à¤—à¥à¤°à¤¹à¤¹à¤°à¥‚"),
    YOGA_HOUSES_LABEL("Houses:", "à¤­à¤¾à¤µà¤¹à¤°à¥‚:"),
    YOGA_STRENGTH_LABEL("Yoga Strength", "à¤¯à¥‹à¤— à¤¬à¤²"),
    YOGA_ACTIVATION_LABEL("Activation:", "à¤¸à¤•à¥à¤°à¤¿à¤¯à¤¤à¤¾:"),
    YOGA_NO_YOGAS_FOUND("No Yogas Found", "à¤•à¥à¤¨à¥ˆ à¤¯à¥‹à¤— à¤«à¥‡à¤²à¤¾ à¤ªà¤°à¥‡à¤¨"),

    // ============================================
    // PROFILE SWITCHER
    // ============================================
    PROFILE_SWITCH("Switch Profile", "à¤ªà¥à¤°à¥‹à¤«à¤¾à¤‡à¤² à¤¬à¤¦à¤²à¥à¤¨à¥à¤¹à¥‹à¤¸à¥"),
    PROFILE_ADD_NEW("Add New Profile", "à¤¨à¤¯à¤¾à¤ à¤ªà¥à¤°à¥‹à¤«à¤¾à¤‡à¤² à¤¥à¤ªà¥à¤¨à¥à¤¹à¥‹à¤¸à¥"),
    PROFILE_ADD_NEW_CHART("Add new chart", "à¤¨à¤¯à¤¾à¤ à¤•à¥à¤£à¥à¤¡à¤²à¥€ à¤¥à¤ªà¥à¤¨à¥à¤¹à¥‹à¤¸à¥"),
    PROFILE_CURRENT("Current", "à¤¹à¤¾à¤²à¤•à¥‹"),
    PROFILE_NO_SAVED_CHARTS("No saved charts", "à¤•à¥à¤¨à¥ˆ à¤¸à¥à¤°à¤•à¥à¤·à¤¿à¤¤ à¤•à¥à¤£à¥à¤¡à¤²à¥€ à¤›à¥ˆà¤¨"),
    PROFILE_ADD_FIRST_CHART("Add your first chart to get started", "à¤¸à¥à¤°à¥ à¤—à¤°à¥à¤¨ à¤†à¤«à¥à¤¨à¥‹ à¤ªà¤¹à¤¿à¤²à¥‹ à¤•à¥à¤£à¥à¤¡à¤²à¥€ à¤¥à¤ªà¥à¤¨à¥à¤¹à¥‹à¤¸à¥"),
    PROFILE_SELECTED("selected", "à¤›à¤¾à¤¨à¤¿à¤à¤•à¥‹"),
    PROFILE_SELECT("Select Profile", "à¤ªà¥à¤°à¥‹à¤«à¤¾à¤‡à¤² à¤›à¤¾à¤¨à¥à¤¨à¥à¤¹à¥‹à¤¸à¥"),
    PROFILE_CURRENT_A11Y("Current profile: %s. Tap to switch profiles", "à¤¹à¤¾à¤²à¤•à¥‹ à¤ªà¥à¤°à¥‹à¤«à¤¾à¤‡à¤²: %sà¥¤ à¤ªà¥à¤°à¥‹à¤«à¤¾à¤‡à¤²à¤¹à¤°à¥‚ à¤¬à¤¦à¤²à¥à¤¨ à¤Ÿà¥à¤¯à¤¾à¤ª à¤—à¤°à¥à¤¨à¥à¤¹à¥‹à¤¸à¥"),
    PROFILE_NO_SELECTED_A11Y("No profile selected. Tap to select a profile", "à¤•à¥à¤¨à¥ˆ à¤ªà¥à¤°à¥‹à¤«à¤¾à¤‡à¤² à¤›à¤¾à¤¨à¤¿à¤à¤•à¥‹ à¤›à¥ˆà¤¨à¥¤ à¤ªà¥à¤°à¥‹à¤«à¤¾à¤‡à¤² à¤›à¤¾à¤¨à¥à¤¨ à¤Ÿà¥à¤¯à¤¾à¤ª à¤—à¤°à¥à¤¨à¥à¤¹à¥‹à¤¸à¥"),
    PROFILE_BIRTH_CHART("Birth chart", "à¤œà¤¨à¥à¤® à¤•à¥à¤£à¥à¤¡à¤²à¥€"),
    PROFILE_DELETE_TITLE("Delete Birth Chart", "à¤œà¤¨à¥à¤® à¤•à¥à¤£à¥à¤¡à¤²à¥€ à¤®à¥‡à¤Ÿà¥à¤¨à¥à¤¹à¥‹à¤¸à¥"),
    PROFILE_DELETE_MESSAGE("Are you sure you want to delete \"{name}\"? This action cannot be undone and all associated data will be permanently removed.", "à¤•à¥‡ à¤¤à¤ªà¤¾à¤ˆà¤‚ \"{name}\" à¤®à¥‡à¤Ÿà¥à¤¨ à¤¨à¤¿à¤¶à¥à¤šà¤¿à¤¤ à¤¹à¥à¤¨à¥à¤¹à¥à¤¨à¥à¤›? à¤¯à¥‹ à¤•à¤¾à¤°à¥à¤¯ à¤ªà¥‚à¤°à¥à¤µà¤µà¤¤ à¤—à¤°à¥à¤¨ à¤¸à¤•à¤¿à¤à¤¦à¥ˆà¤¨ à¤° à¤¸à¤¬à¥ˆ à¤¸à¤®à¥à¤¬à¤¨à¥à¤§à¤¿à¤¤ à¤¡à¤¾à¤Ÿà¤¾ à¤¸à¥à¤¥à¤¾à¤¯à¥€ à¤°à¥‚à¤ªà¤®à¤¾ à¤¹à¤Ÿà¤¾à¤‡à¤¨à¥‡à¤›à¥¤"),
    PROFILE_EDIT_CHART("Edit Chart", "à¤•à¥à¤£à¥à¤¡à¤²à¥€ à¤¸à¤®à¥à¤ªà¤¾à¤¦à¤¨ à¤—à¤°à¥à¤¨à¥à¤¹à¥‹à¤¸à¥"),

    // ============================================
    // TRANSITS SCREEN
    // ============================================
    TRANSIT_CURRENT_MOVEMENTS("Current movements in %s", "%s à¤®à¤¾ à¤¹à¤¾à¤²à¤•à¥‹ à¤—à¤¤à¤¿"),
    TRANSIT_PLANET_POSITIONS("Current Positions", "à¤¹à¤¾à¤²à¤•à¥‹ à¤¸à¥à¤¥à¤¿à¤¤à¤¿à¤¹à¤°à¥‚"),
    TRANSIT_OVERVIEW("Transit Overview", "à¤—à¥‹à¤šà¤° à¤…à¤µà¤²à¥‹à¤•à¤¨"),
    TRANSIT_CURRENT_INFLUENCES("Current influences on your chart", "à¤¤à¤ªà¤¾à¤ˆà¤‚à¤•à¥‹ à¤•à¥à¤£à¥à¤¡à¤²à¥€à¤®à¤¾ à¤¹à¤¾à¤²à¤•à¥‹ à¤ªà¥à¤°à¤­à¤¾à¤µà¤¹à¤°à¥‚"),
    TRANSIT_PLANETS_COUNT("Planets Transiting", "à¤—à¥‹à¤šà¤°à¤®à¤¾ à¤—à¥à¤°à¤¹à¤¹à¤°à¥‚"),
    TRANSIT_MAJOR_TRANSITS("Major Transits", "à¤®à¥à¤–à¥à¤¯ à¤—à¥‹à¤šà¤°à¤¹à¤°à¥‚"),
    TRANSIT_QUALITY_LABEL("Quality Score", "à¤—à¥à¤£à¤¸à¥à¤¤à¤° à¤¸à¥à¤•à¥‹à¤°"),
    TRANSIT_OVERALL_ASSESSMENT("Overall Assessment", "à¤¸à¤®à¤—à¥à¤° à¤®à¥‚à¤²à¥à¤¯à¤¾à¤‚à¤•à¤¨"),
    TRANSIT_RETROGRADE_SYMBOL("Rx", "Rx"),
    TRANSIT_HOUSE_LABEL("House", "à¤­à¤¾à¤µ"),
    TRANSIT_LABEL("Transit", "à¤—à¥‹à¤šà¤°"),
    TRANSIT_NATAL_LABEL("Natal", "à¤œà¤¨à¥à¤®"),
    TRANSIT_NO_PLANETS_TRANSITING("No planets in this house", "à¤¯à¤¸ à¤­à¤¾à¤µà¤®à¤¾ à¤•à¥‹à¤¨à¥ˆ à¤—à¥à¤°à¤¹ à¤›à¥ˆà¤¨"),
    TRANSIT_UPCOMING("Upcoming Transits", "à¤†à¤—à¤¾à¤®à¥€ à¤—à¥‹à¤šà¤°à¤¹à¤°à¥‚"),
    TRANSIT_NO_UPCOMING("No upcoming significant transits", "à¤•à¥à¤¨à¥ˆ à¤†à¤—à¤¾à¤®à¥€ à¤®à¤¹à¤¤à¥à¤¤à¥à¤µà¤ªà¥‚à¤°à¥à¤£ à¤—à¥‹à¤šà¤°à¤¹à¤°à¥‚ à¤›à¥ˆà¤¨à¤¨à¥"),
    TRANSIT_TO_NATAL_ASPECTS("Transit to Natal Aspects", "à¤—à¥‹à¤šà¤°-à¤œà¤¨à¥à¤® à¤ªà¤¹à¤²à¥à¤¹à¤°à¥‚"),
    TRANSIT_NO_ASPECTS("No transit aspects at this time", "à¤¯à¤¸ à¤¸à¤®à¤¯à¤®à¤¾ à¤•à¥à¤¨à¥ˆ à¤—à¥‹à¤šà¤° à¤ªà¤¹à¤²à¥à¤¹à¤°à¥‚ à¤›à¥ˆà¤¨à¤¨à¥"),
    TRANSIT_NO_DATA("No Transit Data", "à¤•à¥à¤¨à¥ˆ à¤—à¥‹à¤šà¤° à¤¡à¤¾à¤Ÿà¤¾ à¤›à¥ˆà¤¨"),
    TRANSIT_SELECT_CHART("Select a chart to view transits", "à¤—à¥‹à¤šà¤°à¤¹à¤°à¥‚ à¤¹à¥‡à¤°à¥à¤¨ à¤•à¥à¤£à¥à¤¡à¤²à¥€ à¤›à¤¾à¤¨à¥à¤¨à¥à¤¹à¥‹à¤¸à¥"),

    // ============================================
    // ONBOARDING
    // ============================================
    ONBOARDING_WELCOME_TITLE("Welcome to AstroStorm", "AstroStorm à¤®à¤¾ à¤¸à¥à¤µà¤¾à¤—à¤¤ à¤›"),
    ONBOARDING_WELCOME_SUBTITLE("Your personal Vedic astrology companion", "à¤¤à¤ªà¤¾à¤ˆà¤‚à¤•à¥‹ à¤µà¥à¤¯à¤•à¥à¤¤à¤¿à¤—à¤¤ à¤µà¥ˆà¤¦à¤¿à¤• à¤œà¥à¤¯à¥‹à¤¤à¤¿à¤· à¤¸à¤¾à¤¥à¥€"),
    ONBOARDING_WELCOME_DESC("Discover the ancient wisdom of Vedic astrology with precision calculations and personalized insights.", "à¤¸à¤Ÿà¥€à¤• à¤—à¤£à¤¨à¤¾ à¤° à¤µà¥à¤¯à¤•à¥à¤¤à¤¿à¤—à¤¤ à¤…à¤¨à¥à¤¤à¤°à¥à¤¦à¥ƒà¤·à¥à¤Ÿà¤¿à¤•à¤¾ à¤¸à¤¾à¤¥ à¤µà¥ˆà¤¦à¤¿à¤• à¤œà¥à¤¯à¥‹à¤¤à¤¿à¤·à¤•à¥‹ à¤ªà¥à¤°à¤¾à¤šà¥€à¤¨ à¤œà¥à¤žà¤¾à¤¨ à¤ªà¤¤à¥à¤¤à¤¾ à¤²à¤—à¤¾à¤‰à¤¨à¥à¤¹à¥‹à¤¸à¥à¥¤"),

    ONBOARDING_LANGUAGE_TITLE("Choose Your Language", "à¤†à¤«à¥à¤¨à¥‹ à¤­à¤¾à¤·à¤¾ à¤›à¤¾à¤¨à¥à¤¨à¥à¤¹à¥‹à¤¸à¥"),
    ONBOARDING_LANGUAGE_SUBTITLE("Select your preferred language", "à¤†à¤«à¥à¤¨à¥‹ à¤®à¤¨à¤ªà¤°à¥à¤¨à¥‡ à¤­à¤¾à¤·à¤¾ à¤›à¤¾à¤¨à¥à¤¨à¥à¤¹à¥‹à¤¸à¥"),
    ONBOARDING_LANGUAGE_DESC("You can change this later in settings.", "à¤¤à¤ªà¤¾à¤ˆà¤‚ à¤¯à¤¸à¤²à¤¾à¤ˆ à¤ªà¤›à¤¿ à¤¸à¥‡à¤Ÿà¤¿à¤™à¥à¤¸à¤®à¤¾ à¤ªà¤°à¤¿à¤µà¤°à¥à¤¤à¤¨ à¤—à¤°à¥à¤¨ à¤¸à¤•à¥à¤¨à¥à¤¹à¥à¤¨à¥à¤›à¥¤"),

    ONBOARDING_THEME_TITLE("Choose Your Theme", "à¤†à¤«à¥à¤¨à¥‹ à¤¥à¤¿à¤® à¤›à¤¾à¤¨à¥à¤¨à¥à¤¹à¥‹à¤¸à¥"),
    ONBOARDING_THEME_SUBTITLE("Select your preferred appearance", "à¤†à¤«à¥à¤¨à¥‹ à¤®à¤¨à¤ªà¤°à¥à¤¨à¥‡ à¤°à¥‚à¤ª à¤›à¤¾à¤¨à¥à¤¨à¥à¤¹à¥‹à¤¸à¥"),
    ONBOARDING_THEME_DESC("You can change this later in settings.", "à¤¤à¤ªà¤¾à¤ˆà¤‚ à¤¯à¤¸à¤²à¤¾à¤ˆ à¤ªà¤›à¤¿ à¤¸à¥‡à¤Ÿà¤¿à¤™à¥à¤¸à¤®à¤¾ à¤ªà¤°à¤¿à¤µà¤°à¥à¤¤à¤¨ à¤—à¤°à¥à¤¨ à¤¸à¤•à¥à¤¨à¥à¤¹à¥à¤¨à¥à¤›à¥¤"),
    ONBOARDING_THEME_LIGHT("Light", "à¤‰à¤œà¥à¤¯à¤¾à¤²à¥‹"),
    ONBOARDING_THEME_DARK("Dark", "à¤…à¤à¤§à¥à¤¯à¤¾à¤°à¥‹"),
    ONBOARDING_THEME_SYSTEM("System", "à¤ªà¥à¤°à¤£à¤¾à¤²à¥€"),

    ONBOARDING_FEATURES_TITLE("Powerful Features", "à¤¶à¤•à¥à¤¤à¤¿à¤¶à¤¾à¤²à¥€ à¤¸à¥à¤µà¤¿à¤§à¤¾à¤¹à¤°à¥‚"),
    ONBOARDING_FEATURES_SUBTITLE("Everything you need for Vedic astrology", "à¤µà¥ˆà¤¦à¤¿à¤• à¤œà¥à¤¯à¥‹à¤¤à¤¿à¤·à¤•à¥‹ à¤²à¤¾à¤—à¤¿ à¤¤à¤ªà¤¾à¤ˆà¤‚à¤²à¤¾à¤ˆ à¤šà¤¾à¤¹à¤¿à¤¨à¥‡ à¤¸à¤¬à¥ˆ"),

    ONBOARDING_FEATURE_CHARTS("Birth Charts", "à¤œà¤¨à¥à¤® à¤•à¥à¤£à¥à¤¡à¤²à¥€"),
    ONBOARDING_FEATURE_CHARTS_DESC("Accurate Vedic birth chart calculations", "à¤¸à¤Ÿà¥€à¤• à¤µà¥ˆà¤¦à¤¿à¤• à¤œà¤¨à¥à¤® à¤•à¥à¤£à¥à¤¡à¤²à¥€ à¤—à¤£à¤¨à¤¾"),
    ONBOARDING_FEATURE_DASHAS("Dashas", "à¤¦à¤¶à¤¾à¤¹à¤°à¥‚"),
    ONBOARDING_FEATURE_DASHAS_DESC("Complete planetary period analysis", "à¤ªà¥‚à¤°à¥à¤£ à¤—à¥à¤°à¤¹ à¤…à¤µà¤§à¤¿ à¤µà¤¿à¤¶à¥à¤²à¥‡à¤·à¤£"),
    ONBOARDING_FEATURE_TRANSITS("Transits", "à¤—à¥‹à¤šà¤°à¤¹à¤°à¥‚"),
    ONBOARDING_FEATURE_TRANSITS_DESC("Real-time planetary movements", "à¤µà¤¾à¤¸à¥à¤¤à¤µà¤¿à¤•-à¤¸à¤®à¤¯ à¤—à¥à¤°à¤¹ à¤—à¤¤à¤¿à¤µà¤¿à¤§à¤¿"),
    ONBOARDING_FEATURE_MATCHMAKING("Matchmaking", "à¤•à¥à¤£à¥à¤¡à¤²à¥€ à¤®à¤¿à¤²à¤¾à¤¨"),
    ONBOARDING_FEATURE_MATCHMAKING_DESC("Kundli Milan compatibility", "à¤•à¥à¤£à¥à¤¡à¤²à¥€ à¤®à¤¿à¤²à¤¾à¤¨ à¤…à¤¨à¥à¤•à¥‚à¤²à¤¤à¤¾"),

    ONBOARDING_READY_TITLE("You're All Set!", "à¤¤à¤ªà¤¾à¤ˆà¤‚ à¤¤à¤¯à¤¾à¤° à¤¹à¥à¤¨à¥à¤¹à¥à¤¨à¥à¤›!"),
    ONBOARDING_READY_SUBTITLE("Start exploring your cosmic journey", "à¤†à¤«à¥à¤¨à¥‹ à¤¬à¥à¤°à¤¹à¥à¤®à¤¾à¤£à¥à¤¡à¥€à¤¯ à¤¯à¤¾à¤¤à¥à¤°à¤¾ à¤…à¤¨à¥à¤µà¥‡à¤·à¤£ à¤—à¤°à¥à¤¨ à¤¸à¥à¤°à¥ à¤—à¤°à¥à¤¨à¥à¤¹à¥‹à¤¸à¥"),
    ONBOARDING_READY_DESC("Create your first birth chart and discover personalized astrological insights.", "à¤†à¤«à¥à¤¨à¥‹ à¤ªà¤¹à¤¿à¤²à¥‹ à¤œà¤¨à¥à¤® à¤•à¥à¤£à¥à¤¡à¤²à¥€ à¤¬à¤¨à¤¾à¤‰à¤¨à¥à¤¹à¥‹à¤¸à¥ à¤° à¤µà¥à¤¯à¤•à¥à¤¤à¤¿à¤—à¤¤ à¤œà¥à¤¯à¥‹à¤¤à¤¿à¤·à¥€à¤¯ à¤…à¤¨à¥à¤¤à¤°à¥à¤¦à¥ƒà¤·à¥à¤Ÿà¤¿à¤¹à¤°à¥‚ à¤ªà¤¤à¥à¤¤à¤¾ à¤²à¤—à¤¾à¤‰à¤¨à¥à¤¹à¥‹à¤¸à¥à¥¤"),

    ONBOARDING_BTN_NEXT("Next", "à¤…à¤°à¥à¤•à¥‹"),
    ONBOARDING_BTN_BACK("Back", "à¤ªà¤›à¤¾à¤¡à¤¿"),
    ONBOARDING_BTN_GET_STARTED("Get Started", "à¤¸à¥à¤°à¥ à¤—à¤°à¥à¤¨à¥à¤¹à¥‹à¤¸à¥"),
    ONBOARDING_BTN_SKIP("Skip", "à¤›à¥‹à¤¡à¥à¤¨à¥à¤¹à¥‹à¤¸à¥"),

    // ============================================
    // COMMON TAB TITLES
    // ============================================
    TAB_OVERVIEW("Overview", "à¤…à¤µà¤²à¥‹à¤•à¤¨"),
    TAB_BY_PLANET("By Planet", "à¤—à¥à¤°à¤¹à¤¾à¤¨à¥à¤¸à¤¾à¤°"),
    TAB_BY_HOUSE("By House", "à¤­à¤¾à¤µà¤¾à¤¨à¥à¤¸à¤¾à¤°"),
    TAB_ELEMENTS("Elements", "à¤¤à¤¤à¥à¤µà¤¹à¤°à¥‚"),
    TAB_TODAY("Today", "à¤†à¤œ"),
    TAB_BIRTH_DAY("Birth Day", "à¤œà¤¨à¥à¤® à¤¦à¤¿à¤¨"),
    TAB_CURRENT_POSITIONS("Current Positions", "à¤¹à¤¾à¤²à¤•à¥‹ à¤¸à¥à¤¥à¤¿à¤¤à¤¿"),
    TAB_UPCOMING("Upcoming", "à¤†à¤—à¤¾à¤®à¥€"),
    TAB_ASPECTS("Aspects", "à¤¦à¥ƒà¤·à¥à¤Ÿà¤¿à¤¹à¤°à¥‚"),
    TAB_SARVASHTAKAVARGA("Sarvashtakavarga", "à¤¸à¤°à¥à¤µà¤¾à¤·à¥à¤Ÿà¤•à¤µà¤°à¥à¤—"),

    // ============================================
    // STRENGTH LABELS
    // ============================================
    STRENGTH_EXCELLENT("Excellent", "à¤‰à¤¤à¥à¤•à¥ƒà¤·à¥à¤Ÿ"),
    STRENGTH_STRONG("Strong", "à¤¬à¤²à¤¿à¤¯à¥‹"),
    STRENGTH_GOOD("Good", "à¤°à¤¾à¤®à¥à¤°à¥‹"),
    STRENGTH_AVERAGE("Average", "à¤”à¤¸à¤¤"),
    STRENGTH_WEAK("Weak", "à¤•à¤®à¤œà¥‹à¤°"),
    STRENGTH_BELOW_AVERAGE("Below Average", "à¤”à¤¸à¤¤à¤®à¥à¤¨à¤¿"),

    // ============================================
    // ACCESSIBILITY STRINGS
    // ============================================
    A11Y_EXPAND("Expand", "à¤µà¤¿à¤¸à¥à¤¤à¤¾à¤° à¤—à¤°à¥à¤¨à¥à¤¹à¥‹à¤¸à¥"),
    A11Y_COLLAPSE("Collapse", "à¤¸à¤‚à¤•à¥à¤·à¤¿à¤ªà¥à¤¤ à¤—à¤°à¥à¤¨à¥à¤¹à¥‹à¤¸à¥"),
    A11Y_NAVIGATE_BACK("Navigate back", "à¤ªà¤›à¤¾à¤¡à¤¿ à¤œà¤¾à¤¨à¥à¤¹à¥‹à¤¸à¥"),
    A11Y_SHOW_INFO("Show information", "à¤œà¤¾à¤¨à¤•à¤¾à¤°à¥€ à¤¦à¥‡à¤–à¤¾à¤‰à¤¨à¥à¤¹à¥‹à¤¸à¥");

}
