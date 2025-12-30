package com.astro.storm.data.localization


/**
 * Matchmaking and Guna string keys
 * Part 2 of 4 split enums to avoid JVM method size limit
 */
enum class StringKeyMatch(override val en: String, override val ne: String) : StringKeyInterface {
    // ============================================
    // MATCHMAKING
    // ============================================
    MATCH_TITLE("Kundli Milan", "à¤•à¥à¤£à¥à¤¡à¤²à¥€ à¤®à¤¿à¤²à¤¾à¤¨"),
    MATCH_SELECT_PROFILES("Select Profiles", "à¤ªà¥à¤°à¥‹à¤«à¤¾à¤‡à¤²à¤¹à¤°à¥‚ à¤›à¤¾à¤¨à¥à¤¨à¥à¤¹à¥‹à¤¸à¥"),
    MATCH_PERSON_1("Person 1", "à¤µà¥à¤¯à¤•à¥à¤¤à¤¿ à¥§"),
    MATCH_PERSON_2("Person 2", "à¤µà¥à¤¯à¤•à¥à¤¤à¤¿ à¥¨"),
    MATCH_CALCULATE("Calculate Match", "à¤®à¤¿à¤²à¤¾à¤¨ à¤—à¤£à¤¨à¤¾ à¤—à¤°à¥à¤¨à¥à¤¹à¥‹à¤¸à¥"),
    MATCH_TOTAL_POINTS("Total Points", "à¤•à¥à¤² à¤…à¤‚à¤•à¤¹à¤°à¥‚"),
    MATCH_OUT_OF("out of", "à¤®à¤§à¥à¤¯à¥‡"),
    MATCH_COMPATIBILITY("Compatibility", "à¤…à¤¨à¥à¤•à¥‚à¤²à¤¤à¤¾"),
    MATCH_EXCELLENT("Excellent", "à¤‰à¤¤à¥à¤•à¥ƒà¤·à¥à¤Ÿ"),
    MATCH_GOOD("Good", "à¤°à¤¾à¤®à¥à¤°à¥‹"),
    MATCH_AVERAGE("Average", "à¤”à¤¸à¤¤"),
    MATCH_BELOW_AVERAGE("Below Average", "à¤”à¤¸à¤¤à¤®à¥à¤¨à¤¿"),

    // Matchmaking Tabs & Sections
    MATCH_OVERVIEW("Overview", "à¤…à¤µà¤²à¥‹à¤•à¤¨"),
    MATCH_GUNAS("Gunas", "à¤—à¥à¤£à¤¹à¤°à¥‚"),
    MATCH_DOSHAS("Doshas", "à¤¦à¥‹à¤·à¤¹à¤°à¥‚"),
    MATCH_NAKSHATRAS("Nakshatras", "à¤¨à¤•à¥à¤·à¤¤à¥à¤°à¤¹à¤°à¥‚"),
    MATCH_SELECT_BRIDE("Select Bride", "à¤µà¤§à¥‚ à¤›à¤¾à¤¨à¥à¤¨à¥à¤¹à¥‹à¤¸à¥"),
    MATCH_SELECT_GROOM("Select Groom", "à¤µà¤° à¤›à¤¾à¤¨à¥à¤¨à¥à¤¹à¥‹à¤¸à¥"),
    MATCH_SWAP_PROFILES("Swap Profiles", "à¤ªà¥à¤°à¥‹à¤«à¤¾à¤‡à¤²à¤¹à¤°à¥‚ à¤¸à¥à¤µà¤¾à¤ª à¤—à¤°à¥à¤¨à¥à¤¹à¥‹à¤¸à¥"),
    MATCH_ASHTAKOOTA("Ashtakoota Points", "à¤…à¤·à¥à¤Ÿà¤•à¥‚à¤Ÿ à¤…à¤‚à¤•à¤¹à¤°à¥‚"),
    MATCH_SHARE_REPORT("Share Report", "à¤°à¤¿à¤ªà¥‹à¤°à¥à¤Ÿ à¤¸à¤¾à¤à¤¾ à¤—à¤°à¥à¤¨à¥à¤¹à¥‹à¤¸à¥"),
    MATCH_COPY_REPORT("Copy Report", "à¤°à¤¿à¤ªà¥‹à¤°à¥à¤Ÿ à¤•à¤ªà¥€ à¤—à¤°à¥à¤¨à¥à¤¹à¥‹à¤¸à¥"),
    MATCH_SHARE_AS_TEXT("Share as Text", "à¤ªà¤¾à¤ à¤•à¥‹ à¤°à¥‚à¤ªà¤®à¤¾ à¤¸à¤¾à¤à¤¾ à¤—à¤°à¥à¤¨à¥à¤¹à¥‹à¤¸à¥"),
    MATCH_SHARE_AS_IMAGE("Share as Image", "à¤›à¤µà¤¿à¤•à¥‹ à¤°à¥‚à¤ªà¤®à¤¾ à¤¸à¤¾à¤à¤¾ à¤—à¤°à¥à¤¨à¥à¤¹à¥‹à¤¸à¥"),
    MATCH_CALCULATING("Calculating compatibility...", "à¤…à¤¨à¥à¤•à¥‚à¤²à¤¤à¤¾ à¤—à¤£à¤¨à¤¾ à¤—à¤°à¥à¤¦à¥ˆ..."),
    MATCH_NO_PROFILES("No profiles available", "à¤•à¥à¤¨à¥ˆ à¤ªà¥à¤°à¥‹à¤«à¤¾à¤‡à¤² à¤‰à¤ªà¤²à¤¬à¥à¤§ à¤›à¥ˆà¤¨"),
    MATCH_SELECT_BOTH("Please select both profiles", "à¤•à¥ƒà¤ªà¤¯à¤¾ à¤¦à¥à¤µà¥ˆ à¤ªà¥à¤°à¥‹à¤«à¤¾à¤‡à¤² à¤›à¤¾à¤¨à¥à¤¨à¥à¤¹à¥‹à¤¸à¥"),

    // Additional Matchmaking Entries
    MATCH_REMEDIES("Remedies", "à¤‰à¤ªà¤¾à¤¯à¤¹à¤°à¥‚"),
    MATCH_COPIED_TO_CLIPBOARD("Report copied to clipboard", "à¤°à¤¿à¤ªà¥‹à¤°à¥à¤Ÿ à¤•à¥à¤²à¤¿à¤ªà¤¬à¥‹à¤°à¥à¤¡à¤®à¤¾ à¤•à¤ªà¥€ à¤­à¤¯à¥‹"),
    MATCH_CLEAR_SELECTION("Clear Selection", "à¤›à¤¨à¥Œà¤Ÿ à¤¹à¤Ÿà¤¾à¤‰à¤¨à¥à¤¹à¥‹à¤¸à¥"),
    MATCH_BRIDE("Bride", "à¤µà¤§à¥‚"),
    MATCH_GROOM("Groom", "à¤µà¤°"),
    MATCH_CREATE_CHARTS_FIRST("Create birth charts first to use matchmaking", "à¤®à¤¿à¤²à¤¾à¤¨ à¤ªà¥à¤°à¤¯à¥‹à¤— à¤—à¤°à¥à¤¨ à¤ªà¤¹à¤¿à¤²à¥‡ à¤œà¤¨à¥à¤® à¤•à¥à¤£à¥à¤¡à¤²à¥€ à¤¬à¤¨à¤¾à¤‰à¤¨à¥à¤¹à¥‹à¤¸à¥"),
    MATCH_TAP_TO_SELECT("Tap to select", "à¤›à¤¾à¤¨à¥à¤¨ à¤Ÿà¥à¤¯à¤¾à¤ª à¤—à¤°à¥à¤¨à¥à¤¹à¥‹à¤¸à¥"),
    MATCH_CONNECTED("Connected", "à¤œà¥‹à¤¡à¤¿à¤à¤•à¥‹"),
    MATCH_NOT_CONNECTED("Not Connected", "à¤œà¥‹à¤¡à¤¿à¤à¤•à¥‹ à¤›à¥ˆà¤¨"),
    MATCH_ANALYZING_COMPATIBILITY("Analyzing compatibility...", "à¤…à¤¨à¥à¤•à¥‚à¤²à¤¤à¤¾ à¤µà¤¿à¤¶à¥à¤²à¥‡à¤·à¤£ à¤—à¤°à¥à¤¦à¥ˆ..."),
    MATCH_CALCULATING_DOSHAS("Calculating doshas...", "à¤¦à¥‹à¤·à¤¹à¤°à¥‚ à¤—à¤£à¤¨à¤¾ à¤—à¤°à¥à¤¦à¥ˆ..."),
    MATCH_CALCULATION_ERROR("Calculation Error", "à¤—à¤£à¤¨à¤¾ à¤¤à¥à¤°à¥à¤Ÿà¤¿"),
    MATCH_MANGLIK("Manglik", "à¤®à¤¾à¤‚à¤—à¤²à¤¿à¤•"),
    MATCH_NADI("Nadi", "à¤¨à¤¾à¤¡à¥€"),
    MATCH_DOSHA_PRESENT("Dosha Present", "à¤¦à¥‹à¤· à¤‰à¤ªà¤¸à¥à¤¥à¤¿à¤¤"),
    MATCH_BHAKOOT("Bhakoot", "à¤­à¤•à¥‚à¤Ÿ"),
    MATCH_NEEDS_ATTENTION("Needs Attention", "à¤§à¥à¤¯à¤¾à¤¨ à¤†à¤µà¤¶à¥à¤¯à¤•"),
    MATCH_GUNA_DISTRIBUTION("Guna Distribution", "à¤—à¥à¤£ à¤µà¤¿à¤¤à¤°à¤£"),
    MATCH_PROFILE_COMPARISON("Profile Comparison", "à¤ªà¥à¤°à¥‹à¤«à¤¾à¤‡à¤² à¤¤à¥à¤²à¤¨à¤¾"),
    MATCH_AI_INSIGHT("AI Insight", "à¤à¤†à¤ˆ à¤…à¤¨à¥à¤¤à¤°à¥à¤¦à¥ƒà¤·à¥à¤Ÿà¤¿"),
    MATCH_AI_INSIGHT_SUBTITLE("Powered by Stormy", "Stormy à¤¦à¥à¤µà¤¾à¤°à¤¾ à¤¸à¤‚à¤šà¤¾à¤²à¤¿à¤¤"),
    MATCH_AI_INSIGHT_DESC("Get a deeper, personalized interpretation of your compatibility analysis using AI. The AI will analyze the Guna scores and Doshas to provide additional relationship guidance.", "à¤à¤†à¤ˆ à¤ªà¥à¤°à¤¯à¥‹à¤— à¤—à¤°à¥‡à¤° à¤†à¤«à¥à¤¨à¥‹ à¤…à¤¨à¥à¤•à¥‚à¤²à¤¤à¤¾ à¤µà¤¿à¤¶à¥à¤²à¥‡à¤·à¤£à¤•à¥‹ à¤—à¤¹à¤¿à¤°à¥‹, à¤µà¥à¤¯à¤•à¥à¤¤à¤¿à¤—à¤¤ à¤µà¥à¤¯à¤¾à¤–à¥à¤¯à¤¾ à¤ªà¥à¤°à¤¾à¤ªà¥à¤¤ à¤—à¤°à¥à¤¨à¥à¤¹à¥‹à¤¸à¥à¥¤ à¤à¤†à¤ˆà¤²à¥‡ à¤¥à¤ª à¤¸à¤®à¥à¤¬à¤¨à¥à¤§ à¤®à¤¾à¤°à¥à¤—à¤¦à¤°à¥à¤¶à¤¨ à¤ªà¥à¤°à¤¦à¤¾à¤¨ à¤—à¤°à¥à¤¨ à¤—à¥à¤£ à¤¸à¥à¤•à¥‹à¤° à¤° à¤¦à¥‹à¤·à¤¹à¤°à¥‚à¤•à¥‹ à¤µà¤¿à¤¶à¥à¤²à¥‡à¤·à¤£ à¤—à¤°à¥à¤¨à¥‡à¤›à¥¤"),
    MATCH_GENERATE_AI_INSIGHT("Generate AI Insight", "à¤à¤†à¤ˆ à¤…à¤¨à¥à¤¤à¤°à¥à¤¦à¥ƒà¤·à¥à¤Ÿà¤¿ à¤‰à¤¤à¥à¤ªà¤¨à¥à¤¨ à¤—à¤°à¥à¤¨à¥à¤¹à¥‹à¤¸à¥"),
    MATCH_AI_ANALYZING("Analyzing compatibility...", "à¤…à¤¨à¥à¤•à¥‚à¤²à¤¤à¤¾ à¤µà¤¿à¤¶à¥à¤²à¥‡à¤·à¤£ à¤—à¤°à¥à¤¦à¥ˆ..."),
    MATCH_VS("vs", "à¤µà¤¿à¤°à¥à¤¦à¥à¤§"),
    MATCH_MOON_SIGN("Moon Sign", "à¤šà¤¨à¥à¤¦à¥à¤° à¤°à¤¾à¤¶à¤¿"),
    MATCH_NAKSHATRA("Nakshatra", "à¤¨à¤•à¥à¤·à¤¤à¥à¤°"),
    MATCH_PADA("Pada", "à¤ªà¤¦"),
    MATCH_PADA_NUMBER("Pada %d", "à¤ªà¤¦ %d"),
    MATCH_KEY_CONSIDERATIONS("Key Considerations", "à¤®à¥à¤–à¥à¤¯ à¤µà¤¿à¤šà¤¾à¤°à¤¹à¤°à¥‚"),
    MATCH_FAVORABLE("Favorable", "à¤…à¤¨à¥à¤•à¥‚à¤²"),
    MATCH_TOTAL_SCORE("Total Score", "à¤•à¥à¤² à¤…à¤‚à¤•"),
    MATCH_MANGLIK_DOSHA_ANALYSIS("Manglik Dosha Analysis", "à¤®à¤¾à¤‚à¤—à¤²à¤¿à¤• à¤¦à¥‹à¤· à¤µà¤¿à¤¶à¥à¤²à¥‡à¤·à¤£"),
    MATCH_MARS_PLACEMENT("Mars Placement", "à¤®à¤‚à¤—à¤² à¤¸à¥à¤¥à¤¿à¤¤à¤¿"),
    MATCH_MARS_IN_HOUSE("Mars in House %d", "à¤­à¤¾à¤µ %d à¤®à¤¾ à¤®à¤‚à¤—à¤²"),
    MATCH_CONTRIBUTING_FACTORS("Contributing Factors", "à¤¯à¥‹à¤—à¤¦à¤¾à¤¨ à¤—à¤°à¥à¤¨à¥‡ à¤•à¤¾à¤°à¤•à¤¹à¤°à¥‚"),
    MATCH_CANCELLATION_FACTORS("Cancellation Factors", "à¤°à¤¦à¥à¤¦ à¤—à¤°à¥à¤¨à¥‡ à¤•à¤¾à¤°à¤•à¤¹à¤°à¥‚"),
    MATCH_NADI_DOSHA("Nadi Dosha", "à¤¨à¤¾à¤¡à¥€ à¤¦à¥‹à¤·"),
    MATCH_HEALTH_PROGENY("Health & Progeny", "à¤¸à¥à¤µà¤¾à¤¸à¥à¤¥à¥à¤¯ à¤° à¤¸à¤¨à¥à¤¤à¤¾à¤¨"),
    MATCH_PRESENT("Present", "à¤‰à¤ªà¤¸à¥à¤¥à¤¿à¤¤"),
    MATCH_ABSENT("Absent", "à¤…à¤¨à¥à¤ªà¤¸à¥à¤¥à¤¿à¤¤"),
    MATCH_NADI_WARNING("Same Nadi can indicate health and progeny concerns", "à¤à¤‰à¤Ÿà¥ˆ à¤¨à¤¾à¤¡à¥€à¤²à¥‡ à¤¸à¥à¤µà¤¾à¤¸à¥à¤¥à¥à¤¯ à¤° à¤¸à¤¨à¥à¤¤à¤¾à¤¨ à¤¸à¤®à¥à¤¬à¤¨à¥à¤§à¥€ à¤šà¤¿à¤¨à¥à¤¤à¤¾ à¤¸à¤‚à¤•à¥‡à¤¤ à¤—à¤°à¥à¤¨ à¤¸à¤•à¥à¤›"),
    MATCH_BHAKOOT_DOSHA("Bhakoot Dosha", "à¤­à¤•à¥‚à¤Ÿ à¤¦à¥‹à¤·"),
    MATCH_FINANCIAL_HARMONY("Financial Harmony", "à¤†à¤°à¥à¤¥à¤¿à¤• à¤¸à¤¾à¤®à¤žà¥à¤œà¤¸à¥à¤¯"),
    MATCH_BRIDE_RASHI("Bride Rashi", "à¤µà¤§à¥‚ à¤°à¤¾à¤¶à¤¿"),
    MATCH_GROOM_RASHI("Groom Rashi", "à¤µà¤° à¤°à¤¾à¤¶à¤¿"),
    MATCH_NAKSHATRA_COMPATIBILITY("Nakshatra Compatibility", "à¤¨à¤•à¥à¤·à¤¤à¥à¤° à¤…à¤¨à¥à¤•à¥‚à¤²à¤¤à¤¾"),
    MATCH_BIRTH_STAR("Birth Star", "à¤œà¤¨à¥à¤® à¤¨à¤•à¥à¤·à¤¤à¥à¤°"),
    MATCH_BIRTH_NAKSHATRA("Birth Nakshatra", "à¤œà¤¨à¥à¤® à¤¨à¤•à¥à¤·à¤¤à¥à¤°"),
    MATCH_NAKSHATRA_LORD("Nakshatra Lord", "à¤¨à¤•à¥à¤·à¤¤à¥à¤° à¤¸à¥à¤µà¤¾à¤®à¥€"),
    MATCH_GANA("Gana", "à¤—à¤£"),
    MATCH_YONI("Yoni", "à¤¯à¥‹à¤¨à¤¿"),
    MATCH_RAJJU_MATCHING("Rajju Matching", "à¤°à¤œà¥à¤œà¥ à¤®à¤¿à¤²à¤¾à¤¨"),
    MATCH_LONGEVITY("Longevity", "à¤¦à¥€à¤°à¥à¤˜à¤¾à¤¯à¥"),
    MATCH_CONFLICT("Conflict", "à¤¦à¥à¤µà¤¨à¥à¤¦à¥à¤µ"),
    MATCH_COMPATIBLE("Compatible", "à¤…à¤¨à¥à¤•à¥‚à¤²"),
    MATCH_RAJJU_DESCRIPTION("Rajju indicates the body parts and their compatibility in marriage.", "à¤°à¤œà¥à¤œà¥à¤²à¥‡ à¤¶à¤°à¥€à¤°à¤•à¤¾ à¤…à¤‚à¤—à¤¹à¤°à¥‚ à¤° à¤µà¤¿à¤µà¤¾à¤¹à¤®à¤¾ à¤¤à¤¿à¤¨à¥€à¤¹à¤°à¥‚à¤•à¥‹ à¤…à¤¨à¥à¤•à¥‚à¤²à¤¤à¤¾ à¤¸à¤‚à¤•à¥‡à¤¤ à¤—à¤°à¥à¤¦à¤›à¥¤"),
    MATCH_VEDHA_ANALYSIS("Vedha Analysis", "à¤µà¥‡à¤§ à¤µà¤¿à¤¶à¥à¤²à¥‡à¤·à¤£"),
    MATCH_OBSTRUCTION_CHECK("Obstruction Check", "à¤¬à¤¾à¤§à¤¾ à¤œà¤¾à¤à¤š"),
    MATCH_NONE("None", "à¤•à¥à¤¨à¥ˆ à¤ªà¤¨à¤¿ à¤›à¥ˆà¤¨"),
    MATCH_VEDHA_DESCRIPTION("Vedha indicates mutual affliction between nakshatras.", "à¤µà¥‡à¤§à¤²à¥‡ à¤¨à¤•à¥à¤·à¤¤à¥à¤°à¤¹à¤°à¥‚ à¤¬à¥€à¤šà¤•à¥‹ à¤ªà¤¾à¤°à¤¸à¥à¤ªà¤°à¤¿à¤• à¤ªà¥€à¤¡à¤¾ à¤¸à¤‚à¤•à¥‡à¤¤ à¤—à¤°à¥à¤¦à¤›à¥¤"),
    MATCH_STREE_DEERGHA("Stree Deergha", "à¤¸à¥à¤¤à¥à¤°à¥€ à¤¦à¥€à¤°à¥à¤˜"),
    MATCH_PROSPERITY_FACTORS("Prosperity Factors", "à¤¸à¤®à¥ƒà¤¦à¥à¤§à¤¿ à¤•à¤¾à¤°à¤•à¤¹à¤°à¥‚"),
    MATCH_STREE_DEERGHA_LABEL("Stree Deergha", "à¤¸à¥à¤¤à¥à¤°à¥€ à¤¦à¥€à¤°à¥à¤˜"),
    MATCH_MAHENDRA("Mahendra", "à¤®à¤¹à¥‡à¤¨à¥à¤¦à¥à¤°"),
    MATCH_BENEFICIAL("Beneficial", "à¤²à¤¾à¤­à¤¦à¤¾à¤¯à¤•"),
    MATCH_SUGGESTED_REMEDIES("Suggested Remedies", "à¤¸à¥à¤à¤¾à¤µ à¤—à¤°à¤¿à¤à¤•à¤¾ à¤‰à¤ªà¤¾à¤¯à¤¹à¤°à¥‚"),
    MATCH_VEDIC_RECOMMENDATIONS("Vedic Recommendations", "à¤µà¥ˆà¤¦à¤¿à¤• à¤¸à¤¿à¤«à¤¾à¤°à¤¿à¤¸à¤¹à¤°à¥‚"),
    MATCH_REMEDIES_DISCLAIMER("Remedies should be performed under guidance of a qualified astrologer.", "à¤‰à¤ªà¤¾à¤¯à¤¹à¤°à¥‚ à¤¯à¥‹à¤—à¥à¤¯ à¤œà¥à¤¯à¥‹à¤¤à¤¿à¤·à¥€à¤•à¥‹ à¤®à¤¾à¤°à¥à¤—à¤¦à¤°à¥à¤¶à¤¨à¤®à¤¾ à¤—à¤°à¥à¤¨à¥à¤ªà¤°à¥à¤›à¥¤"),
    MATCH_NO_CHARTS("No Charts", "à¤•à¥à¤¨à¥ˆ à¤•à¥à¤£à¥à¤¡à¤²à¥€ à¤›à¥ˆà¤¨"),
    MATCH_SELECT_BRIDE_PROFILE("Select bride profile", "à¤µà¤§à¥‚ à¤ªà¥à¤°à¥‹à¤«à¤¾à¤‡à¤² à¤›à¤¾à¤¨à¥à¤¨à¥à¤¹à¥‹à¤¸à¥"),
    MATCH_SELECT_GROOM_PROFILE("Select groom profile", "à¤µà¤° à¤ªà¥à¤°à¥‹à¤«à¤¾à¤‡à¤² à¤›à¤¾à¤¨à¥à¤¨à¥à¤¹à¥‹à¤¸à¥"),
    MATCH_PREPARING_ANALYSIS("Preparing analysis...", "à¤µà¤¿à¤¶à¥à¤²à¥‡à¤·à¤£ à¤¤à¤¯à¤¾à¤° à¤—à¤°à¥à¤¦à¥ˆ..."),
    MATCH_CREATE_CHARTS("Create Charts", "à¤•à¥à¤£à¥à¤¡à¤²à¥€ à¤¬à¤¨à¤¾à¤‰à¤¨à¥à¤¹à¥‹à¤¸à¥"),
    MATCH_SELECT_TAP_CARDS("Select by tapping cards", "à¤•à¤¾à¤°à¥à¤¡à¤¹à¤°à¥‚ à¤Ÿà¥à¤¯à¤¾à¤ª à¤—à¤°à¥‡à¤° à¤›à¤¾à¤¨à¥à¤¨à¥à¤¹à¥‹à¤¸à¥"),
    MATCH_TAP_BRIDE_CARD("Tap bride card to select", "à¤›à¤¾à¤¨à¥à¤¨ à¤µà¤§à¥‚ à¤•à¤¾à¤°à¥à¤¡ à¤Ÿà¥à¤¯à¤¾à¤ª à¤—à¤°à¥à¤¨à¥à¤¹à¥‹à¤¸à¥"),
    MATCH_TAP_GROOM_CARD("Tap groom card to select", "à¤›à¤¾à¤¨à¥à¤¨ à¤µà¤° à¤•à¤¾à¤°à¥à¤¡ à¤Ÿà¥à¤¯à¤¾à¤ª à¤—à¤°à¥à¤¨à¥à¤¹à¥‹à¤¸à¥"),
    MATCH_CHARTS_AVAILABLE("%d charts available", "%d à¤•à¥à¤£à¥à¤¡à¤²à¥€ à¤‰à¤ªà¤²à¤¬à¥à¤§"),
    MATCH_NO_CHARTS_AVAILABLE("No charts available", "à¤•à¥à¤¨à¥ˆ à¤•à¥à¤£à¥à¤¡à¤²à¥€ à¤‰à¤ªà¤²à¤¬à¥à¤§ à¤›à¥ˆà¤¨"),
    MATCH_SELECTED("Selected", "à¤›à¤¾à¤¨à¤¿à¤à¤•à¥‹"),
    MATCH_COPY_FULL_REPORT("Copy Full Report", "à¤ªà¥‚à¤°à¥à¤£ à¤°à¤¿à¤ªà¥‹à¤°à¥à¤Ÿ à¤•à¤ªà¥€ à¤—à¤°à¥à¤¨à¥à¤¹à¥‹à¤¸à¥"),
    MATCH_COPY_FULL_DESC("Complete compatibility analysis", "à¤ªà¥‚à¤°à¥à¤£ à¤…à¤¨à¥à¤•à¥‚à¤²à¤¤à¤¾ à¤µà¤¿à¤¶à¥à¤²à¥‡à¤·à¤£"),
    MATCH_COPY_SUMMARY("Copy Summary", "à¤¸à¤¾à¤°à¤¾à¤‚à¤¶ à¤•à¤ªà¥€ à¤—à¤°à¥à¤¨à¥à¤¹à¥‹à¤¸à¥"),
    MATCH_COPY_SUMMARY_DESC("Brief compatibility overview", "à¤¸à¤‚à¤•à¥à¤·à¤¿à¤ªà¥à¤¤ à¤…à¤¨à¥à¤•à¥‚à¤²à¤¤à¤¾ à¤…à¤µà¤²à¥‹à¤•à¤¨"),
    MATCH_COPY_SCORES("Copy Scores", "à¤…à¤‚à¤•à¤¹à¤°à¥‚ à¤•à¤ªà¥€ à¤—à¤°à¥à¤¨à¥à¤¹à¥‹à¤¸à¥"),
    MATCH_COPY_SCORES_DESC("Guna scores only", "à¤—à¥à¤£ à¤…à¤‚à¤•à¤¹à¤°à¥‚ à¤®à¤¾à¤¤à¥à¤°"),

    // Guna Details
    GUNA_VARNA("Varna", "à¤µà¤°à¥à¤£"),
    GUNA_VASHYA("Vashya", "à¤µà¤¶à¥à¤¯"),
    GUNA_TARA("Tara", "à¤¤à¤¾à¤°à¤¾"),
    GUNA_YONI("Yoni", "à¤¯à¥‹à¤¨à¤¿"),
    GUNA_GRAHA_MAITRI("Graha Maitri", "à¤—à¥à¤°à¤¹ à¤®à¥ˆà¤¤à¥à¤°à¥€"),
    GUNA_GANA("Gana", "à¤—à¤£"),
    GUNA_BHAKOOT("Bhakoot", "à¤­à¤•à¥‚à¤Ÿ"),
    GUNA_NADI("Nadi", "à¤¨à¤¾à¤¡à¥€"),
    GUNA_POINTS("Points", "à¤…à¤‚à¤•à¤¹à¤°à¥‚"),

    // Dosha Analysis
    DOSHA_MANGLIK("Manglik Dosha", "à¤®à¤¾à¤‚à¤—à¤²à¤¿à¤• à¤¦à¥‹à¤·"),
    DOSHA_NADI("Nadi Dosha", "à¤¨à¤¾à¤¡à¥€ à¤¦à¥‹à¤·"),
    DOSHA_BHAKOOT("Bhakoot Dosha", "à¤­à¤•à¥‚à¤Ÿ à¤¦à¥‹à¤·"),
    DOSHA_PRESENT("Present", "à¤‰à¤ªà¤¸à¥à¤¥à¤¿à¤¤"),
    DOSHA_ABSENT("Absent", "à¤…à¤¨à¥à¤ªà¤¸à¥à¤¥à¤¿à¤¤"),
    DOSHA_CANCELLED("Cancelled", "à¤°à¤¦à¥à¤¦"),
    DOSHA_REMEDIES_AVAILABLE("Remedies Available", "à¤‰à¤ªà¤¾à¤¯à¤¹à¤°à¥‚ à¤‰à¤ªà¤²à¤¬à¥à¤§"),

    // ============================================
    // PANCHANGA
    // ============================================
    PANCHANGA_TITHI("Tithi", "à¤¤à¤¿à¤¥à¤¿"),
    PANCHANGA_VARA("Vara (Day)", "à¤µà¤¾à¤°"),
    PANCHANGA_NAKSHATRA_LABEL("Nakshatra", "à¤¨à¤•à¥à¤·à¤¤à¥à¤°"),
    PANCHANGA_YOGA("Yoga", "à¤¯à¥‹à¤—"),
    PANCHANGA_KARANA("Karana", "à¤•à¤°à¤£"),
    PANCHANGA_SUNRISE("Sunrise", "à¤¸à¥‚à¤°à¥à¤¯à¥‹à¤¦à¤¯"),
    PANCHANGA_SUNSET("Sunset", "à¤¸à¥‚à¤°à¥à¤¯à¤¾à¤¸à¥à¤¤"),
    PANCHANGA_MOONRISE("Moonrise", "à¤šà¤¨à¥à¤¦à¥à¤°à¥‹à¤¦à¤¯"),
    PANCHANGA_MOONSET("Moonset", "à¤šà¤¨à¥à¤¦à¥à¤°à¤¾à¤¸à¥à¤¤"),

    // ============================================
    // MUHURTA
    // ============================================
    MUHURTA_TITLE("Muhurta Finder", "à¤®à¥à¤¹à¥‚à¤°à¥à¤¤ à¤–à¥‹à¤œà¤•à¤°à¥à¤¤à¤¾"),
    MUHURTA_SELECT_EVENT("Select Event Type", "à¤˜à¤Ÿà¤¨à¤¾ à¤ªà¥à¤°à¤•à¤¾à¤° à¤›à¤¾à¤¨à¥à¤¨à¥à¤¹à¥‹à¤¸à¥"),
    MUHURTA_DATE_RANGE("Date Range", "à¤®à¤¿à¤¤à¤¿ à¤¦à¤¾à¤¯à¤°à¤¾"),
    MUHURTA_FIND("Find Auspicious Times", "à¤¶à¥à¤­ à¤¸à¤®à¤¯ à¤–à¥‹à¤œà¥à¤¨à¥à¤¹à¥‹à¤¸à¥"),
    MUHURTA_RESULTS("Auspicious Muhurtas", "à¤¶à¥à¤­ à¤®à¥à¤¹à¥‚à¤°à¥à¤¤à¤¹à¤°à¥‚"),
    MUHURTA_PREV_DAY("Previous day", "à¤…à¤˜à¤¿à¤²à¥à¤²à¥‹ à¤¦à¤¿à¤¨"),
    MUHURTA_NEXT_DAY("Next day", "à¤…à¤°à¥à¤•à¥‹ à¤¦à¤¿à¤¨"),
    MUHURTA_CALCULATING("Calculating muhurta...", "à¤®à¥à¤¹à¥‚à¤°à¥à¤¤ à¤—à¤£à¤¨à¤¾ à¤—à¤°à¥à¤¦à¥ˆ..."),
    MUHURTA_ERROR("Something went wrong", "à¤•à¥‡à¤¹à¥€ à¤—à¤²à¤¤ à¤­à¤¯à¥‹"),
    MUHURTA_SCORE("Score", "à¤…à¤‚à¤•"),
    MUHURTA_AUSPICIOUS_TIME("Auspicious Time", "à¤¶à¥à¤­ à¤¸à¤®à¤¯"),
    MUHURTA_AVERAGE_TIME("Average Time", "à¤¸à¤¾à¤®à¤¾à¤¨à¥à¤¯ à¤¸à¤®à¤¯"),
    MUHURTA_PANCHANGA("Panchanga", "à¤ªà¤žà¥à¤šà¤¾à¤™à¥à¤—"),
    MUHURTA_INAUSPICIOUS_PERIODS("Inauspicious Periods", "à¤…à¤¶à¥à¤­ à¤…à¤µà¤§à¤¿à¤¹à¤°à¥‚"),
    MUHURTA_RAHUKALA("Rahukala", "à¤°à¤¾à¤¹à¥à¤•à¤¾à¤²"),
    MUHURTA_RAHUKALA_DESC("Avoid important work", "à¤®à¤¹à¤¤à¥à¤¤à¥à¤µà¤ªà¥‚à¤°à¥à¤£ à¤•à¤¾à¤°à¥à¤¯ à¤¬à¤šà¥à¤¨à¥à¤¹à¥‹à¤¸à¥"),
    MUHURTA_YAMAGHANTA("Yamaghanta", "à¤¯à¤®à¤˜à¤¨à¥à¤¤à¤¾"),
    MUHURTA_YAMAGHANTA_DESC("Avoid travel", "à¤¯à¤¾à¤¤à¥à¤°à¤¾ à¤¬à¤šà¥à¤¨à¥à¤¹à¥‹à¤¸à¥"),
    MUHURTA_GULIKA_KALA("Gulika Kala", "à¤—à¥à¤²à¤¿à¤• à¤•à¤¾à¤²"),
    MUHURTA_GULIKA_KALA_DESC("Avoid new beginnings", "à¤¨à¤¯à¤¾à¤ à¤¶à¥à¤°à¥à¤†à¤¤ à¤¬à¤šà¥à¤¨à¥à¤¹à¥‹à¤¸à¥"),
    MUHURTA_ACTIVE("ACTIVE", "à¤¸à¤•à¥à¤°à¤¿à¤¯"),
    MUHURTA_DAY_CHOGHADIYA("Day Choghadiya", "à¤¦à¤¿à¤¨à¤•à¥‹ à¤šà¥‹à¤˜à¤¡à¤¼à¤¿à¤¯à¤¾"),
    MUHURTA_PERIODS("%d periods", "%d à¤…à¤µà¤§à¤¿à¤¹à¤°à¥‚"),
    MUHURTA_NOW("NOW", "à¤…à¤¹à¤¿à¤²à¥‡"),
    MUHURTA_FROM("From", "à¤¬à¤¾à¤Ÿ"),
    MUHURTA_TO("To", "à¤¸à¤®à¥à¤®"),
    MUHURTA_SEARCHING("Searching...", "à¤–à¥‹à¤œà¥à¤¦à¥ˆ..."),
    MUHURTA_FIND_DATES("Find Auspicious Dates", "à¤¶à¥à¤­ à¤®à¤¿à¤¤à¤¿ à¤–à¥‹à¤œà¥à¤¨à¥à¤¹à¥‹à¤¸à¥"),
    MUHURTA_FIND_AUSPICIOUS("Find Auspicious Dates", "à¤¶à¥à¤­ à¤®à¤¿à¤¤à¤¿à¤¹à¤°à¥‚ à¤–à¥‹à¤œà¥à¤¨à¥à¤¹à¥‹à¤¸à¥"),
    MUHURTA_SEARCH_EMPTY("Search for Auspicious Times", "à¤¶à¥à¤­ à¤¸à¤®à¤¯ à¤–à¥‹à¤œà¥à¤¨à¥à¤¹à¥‹à¤¸à¥"),
    MUHURTA_SEARCH_HELP("Select an activity and date range to find the most favorable muhurtas", "à¤¸à¤¬à¥ˆà¤­à¤¨à¥à¤¦à¤¾ à¤…à¤¨à¥à¤•à¥‚à¤² à¤®à¥à¤¹à¥‚à¤°à¥à¤¤à¤¹à¤°à¥‚ à¤–à¥‹à¤œà¥à¤¨ à¤—à¤¤à¤¿à¤µà¤¿à¤§à¤¿ à¤° à¤®à¤¿à¤¤à¤¿ à¤¦à¤¾à¤¯à¤°à¤¾ à¤›à¤¾à¤¨à¥à¤¨à¥à¤¹à¥‹à¤¸à¥"),
    MUHURTA_FINDING("Finding auspicious times...", "à¤¶à¥à¤­ à¤¸à¤®à¤¯ à¤–à¥‹à¤œà¥à¤¦à¥ˆ..."),
    MUHURTA_NO_RESULTS("No Auspicious Times Found", "à¤•à¥à¤¨à¥ˆ à¤¶à¥à¤­ à¤¸à¤®à¤¯ à¤«à¥‡à¤²à¤¾ à¤ªà¤°à¥‡à¤¨"),
    MUHURTA_NO_RESULTS_HELP("Try expanding your date range", "à¤®à¤¿à¤¤à¤¿ à¤¦à¤¾à¤¯à¤°à¤¾ à¤µà¤¿à¤¸à¥à¤¤à¤¾à¤° à¤—à¤°à¥à¤¨à¥‡ à¤ªà¥à¤°à¤¯à¤¾à¤¸ à¤—à¤°à¥à¤¨à¥à¤¹à¥‹à¤¸à¥"),
    MUHURTA_SEARCH_ERROR("Search Failed", "à¤–à¥‹à¤œ à¤…à¤¸à¤«à¤²"),
    MUHURTA_RESULTS_TITLE("Auspicious Times", "à¤¶à¥à¤­ à¤¸à¤®à¤¯à¤¹à¤°à¥‚"),
    MUHURTA_RESULTS_COUNT("%d found", "%d à¤«à¥‡à¤²à¤¾ à¤ªà¤°à¥‡à¤•à¥‹"),
    MUHURTA_DETAIL_DAY("Day", "à¤¦à¤¿à¤¨"),
    MUHURTA_DETAIL_CHOGHADIYA("Choghadiya", "à¤šà¥‹à¤˜à¤¡à¤¼à¤¿à¤¯à¤¾"),
    MUHURTA_TODAY("Today", "à¤†à¤œ"),
    MUHURTA_PREV_DAY_A11Y("Previous day", "à¤…à¤˜à¤¿à¤²à¥à¤²à¥‹ à¤¦à¤¿à¤¨"),
    MUHURTA_NEXT_DAY_A11Y("Next day", "à¤…à¤°à¥à¤•à¥‹ à¤¦à¤¿à¤¨"),
    MUHURTA_VARA("Vara", "à¤µà¤¾à¤°"),
    MUHURTA_TITHI("Tithi", "à¤¤à¤¿à¤¥à¤¿"),
    MUHURTA_YOGA("Yoga", "à¤¯à¥‹à¤—"),
    MUHURTA_KARANA("Karana", "à¤•à¤°à¤£"),
    MUHURTA_SUNRISE_SUNSET("Sunrise / Sunset", "à¤¸à¥‚à¤°à¥à¤¯à¥‹à¤¦à¤¯ / à¤¸à¥‚à¤°à¥à¤¯à¤¾à¤¸à¥à¤¤"),
    MUHURTA_SELECT_ACTIVITY("Select Activity", "à¤—à¤¤à¤¿à¤µà¤¿à¤§à¤¿ à¤šà¤¯à¤¨ à¤—à¤°à¥à¤¨à¥à¤¹à¥‹à¤¸à¥"),
    MUHURTA_SUITABLE_ACTIVITIES("Suitable Activities", "à¤‰à¤ªà¤¯à¥à¤•à¥à¤¤ à¤—à¤¤à¤¿à¤µà¤¿à¤§à¤¿à¤¹à¤°à¥‚"),
    MUHURTA_AVOID_ACTIVITIES("Activities to Avoid", "à¤¬à¤šà¥à¤¨à¥à¤ªà¤°à¥à¤¨à¥‡ à¤—à¤¤à¤¿à¤µà¤¿à¤§à¤¿à¤¹à¤°à¥‚"),
    MUHURTA_RECOMMENDATIONS("Recommendations", "à¤¸à¤¿à¤«à¤¾à¤°à¤¿à¤¸à¤¹à¤°à¥‚"),
    MUHURTA_DATE_RANGE_LABEL("Date Range", "à¤®à¤¿à¤¤à¤¿ à¤¦à¤¾à¤¯à¤°à¤¾"),

    // ============================================
    // REMEDIES
    // ============================================
    REMEDIES_TITLE("Vedic Remedies", "à¤µà¥ˆà¤¦à¤¿à¤• à¤‰à¤ªà¤¾à¤¯à¤¹à¤°à¥‚"),
    REMEDIES_GEMSTONES("Gemstones", "à¤°à¤¤à¥à¤¨à¤¹à¤°à¥‚"),
    REMEDIES_MANTRAS("Mantras", "à¤®à¤¨à¥à¤¤à¥à¤°à¤¹à¤°à¥‚"),
    REMEDIES_YANTRAS("Yantras", "à¤¯à¤¨à¥à¤¤à¥à¤°à¤¹à¤°à¥‚"),
    REMEDIES_RITUALS("Rituals", "à¤ªà¥‚à¤œà¤¾ à¤µà¤¿à¤§à¤¿à¤¹à¤°à¥‚"),
    REMEDIES_CHARITY("Charity", "à¤¦à¤¾à¤¨"),
    REMEDIES_OVERVIEW("Overview", "à¤…à¤µà¤²à¥‹à¤•à¤¨"),
    REMEDIES_PLANETS("Planets", "à¤—à¥à¤°à¤¹à¤¹à¤°à¥‚"),
    REMEDIES_ANALYSIS("Remedies Analysis", "à¤‰à¤ªà¤¾à¤¯ à¤µà¤¿à¤¶à¥à¤²à¥‡à¤·à¤£"),
    REMEDIES_CHART_STRENGTH("Chart Strength", "à¤•à¥à¤£à¥à¤¡à¤²à¥€ à¤¬à¤²"),
    REMEDIES_PLANETS_WELL_PLACED("%d of %d planets well-placed", "%d à¤®à¤§à¥à¤¯à¥‡ %d à¤—à¥à¤°à¤¹ à¤°à¤¾à¤®à¥à¤°à¤°à¥€ à¤¸à¥à¤¥à¤¿à¤¤"),
    REMEDIES_PLANETS_ATTENTION("Planets Requiring Attention", "à¤§à¥à¤¯à¤¾à¤¨ à¤†à¤µà¤¶à¥à¤¯à¤• à¤—à¥à¤°à¤¹à¤¹à¤°à¥‚"),
    REMEDIES_ESSENTIAL("Essential Remedies", "à¤†à¤µà¤¶à¥à¤¯à¤• à¤‰à¤ªà¤¾à¤¯à¤¹à¤°à¥‚"),
    REMEDIES_WEEKLY_SCHEDULE("Weekly Remedy Schedule", "à¤¸à¤¾à¤ªà¥à¤¤à¤¾à¤¹à¤¿à¤• à¤‰à¤ªà¤¾à¤¯ à¤¤à¤¾à¤²à¤¿à¤•à¤¾"),
    REMEDIES_WEEKLY_SCHEDULE_DESC("Perform planet-specific remedies on their designated days for maximum effect", "à¤…à¤§à¤¿à¤•à¤¤à¤® à¤ªà¥à¤°à¤­à¤¾à¤µà¤•à¥‹ à¤²à¤¾à¤—à¤¿ à¤¤à¤¿à¤¨à¥€à¤¹à¤°à¥‚à¤•à¥‹ à¤¤à¥‹à¤•à¤¿à¤à¤•à¥‹ à¤¦à¤¿à¤¨à¤®à¤¾ à¤—à¥à¤°à¤¹-à¤µà¤¿à¤¶à¥‡à¤· à¤‰à¤ªà¤¾à¤¯à¤¹à¤°à¥‚ à¤—à¤°à¥à¤¨à¥à¤¹à¥‹à¤¸à¥"),
    REMEDIES_LIFE_AREA_FOCUS("Life Area Focus", "à¤œà¥€à¤µà¤¨ à¤•à¥à¤·à¥‡à¤¤à¥à¤° à¤«à¥‹à¤•à¤¸"),
    REMEDIES_GENERAL_RECOMMENDATIONS("General Recommendations", "à¤¸à¤¾à¤®à¤¾à¤¨à¥à¤¯ à¤¸à¤¿à¤«à¤¾à¤°à¤¿à¤¸à¤¹à¤°à¥‚"),
    REMEDIES_METHOD("Method", "à¤µà¤¿à¤§à¤¿"),
    REMEDIES_TIMING("Timing", "à¤¸à¤®à¤¯"),
    REMEDIES_DURATION("Duration", "à¤…à¤µà¤§à¤¿"),
    REMEDIES_MANTRA_SECTION("Mantra", "à¤®à¤¨à¥à¤¤à¥à¤°"),
    REMEDIES_BENEFITS("Benefits", "à¤²à¤¾à¤­à¤¹à¤°à¥‚"),
    REMEDIES_CAUTIONS("Cautions", "à¤¸à¤¾à¤µà¤§à¤¾à¤¨à¥€à¤¹à¤°à¥‚"),
    REMEDIES_SEARCH("Search remedies...", "à¤‰à¤ªà¤¾à¤¯à¤¹à¤°à¥‚ à¤–à¥‹à¤œà¥à¤¨à¥à¤¹à¥‹à¤¸à¥..."),
    REMEDIES_FILTER_ALL("All", "à¤¸à¤¬à¥ˆ"),
    REMEDIES_NO_RESULTS("No remedies found", "à¤•à¥à¤¨à¥ˆ à¤‰à¤ªà¤¾à¤¯ à¤«à¥‡à¤²à¤¾ à¤ªà¤°à¥‡à¤¨"),
    REMEDIES_NO_RESULTS_SEARCH("No remedies found for \"%s\"", "\"%s\" à¤•à¥‹ à¤²à¤¾à¤—à¤¿ à¤•à¥à¤¨à¥ˆ à¤‰à¤ªà¤¾à¤¯ à¤«à¥‡à¤²à¤¾ à¤ªà¤°à¥‡à¤¨"),
    REMEDIES_NO_CATEGORY("No remedies in this category", "à¤¯à¤¸ à¤µà¤°à¥à¤—à¤®à¤¾ à¤•à¥à¤¨à¥ˆ à¤‰à¤ªà¤¾à¤¯ à¤›à¥ˆà¤¨"),
    REMEDIES_NO_CHART("No chart selected", "à¤•à¥à¤¨à¥ˆ à¤•à¥à¤£à¥à¤¡à¤²à¥€ à¤›à¤¾à¤¨à¤¿à¤à¤•à¥‹ à¤›à¥ˆà¤¨"),
    REMEDIES_SELECT_CHART("Select a chart to view remedies", "à¤‰à¤ªà¤¾à¤¯à¤¹à¤°à¥‚ à¤¹à¥‡à¤°à¥à¤¨ à¤•à¥à¤£à¥à¤¡à¤²à¥€ à¤›à¤¾à¤¨à¥à¤¨à¥à¤¹à¥‹à¤¸à¥"),
    REMEDIES_ANALYZING("Analyzing your chart...", "à¤¤à¤ªà¤¾à¤ˆà¤‚à¤•à¥‹ à¤•à¥à¤£à¥à¤¡à¤²à¥€ à¤µà¤¿à¤¶à¥à¤²à¥‡à¤·à¤£ à¤—à¤°à¥à¤¦à¥ˆ..."),
    REMEDIES_PREPARING("Preparing personalized remedies", "à¤µà¥à¤¯à¤•à¥à¤¤à¤¿à¤—à¤¤ à¤‰à¤ªà¤¾à¤¯à¤¹à¤°à¥‚ à¤¤à¤¯à¤¾à¤° à¤—à¤°à¥à¤¦à¥ˆ"),
    REMEDIES_COPY_MANTRA("Copy mantra", "à¤®à¤¨à¥à¤¤à¥à¤° à¤•à¤ªà¥€ à¤—à¤°à¥à¤¨à¥à¤¹à¥‹à¤¸à¥"),
    REMEDIES_RECOMMENDED("%d remedies recommended", "%d à¤‰à¤ªà¤¾à¤¯à¤¹à¤°à¥‚ à¤¸à¤¿à¤«à¤¾à¤°à¤¿à¤¸ à¤—à¤°à¤¿à¤à¤•à¥‹"),
    REMEDIES_BEST_DAY("Best performed on %s", "%s à¤®à¤¾ à¤—à¤°à¥à¤¨à¥ à¤‰à¤¤à¥à¤¤à¤®"),
    REMEDIES_TOTAL("Total", "à¤•à¥à¤²"),
    REMEDIES_ESSENTIAL_COUNT("Essential", "à¤†à¤µà¤¶à¥à¤¯à¤•"),

    // Planetary Status
    PLANETARY_STATUS_EXALTED("Exalted", "à¤‰à¤šà¥à¤š"),
    PLANETARY_STATUS_DEBILITATED("Debilitated", "à¤¨à¥€à¤š"),
    PLANETARY_STATUS_RETROGRADE("Retrograde", "à¤µà¤•à¥à¤°à¥€"),
    PLANETARY_STATUS_COMBUST("Combust", "à¤…à¤¸à¥à¤¤"),
    PLANETARY_STATUS_OWN_SIGN("Own Sign", "à¤¸à¥à¤µà¤°à¤¾à¤¶à¤¿"),
    PLANETARY_STATUS_MOOLATRIKONA("Moolatrikona", "à¤®à¥‚à¤²à¤¤à¥à¤°à¤¿à¤•à¥‹à¤£"),
    PLANETARY_STATUS_FRIENDLY("Friendly", "à¤®à¤¿à¤¤à¥à¤°"),
    PLANETARY_STATUS_ENEMY_SIGN("Enemy Sign", "à¤¶à¤¤à¥à¤°à¥ à¤°à¤¾à¤¶à¤¿"),

    // ============================================
    // TIME DURATION LABELS
    // ============================================
    TIME_DAYS("%dd", "%d à¤¦à¤¿à¤¨"),
    TIME_WEEKS("%dw", "%d à¤¹à¤ªà¥à¤¤à¤¾"),
    TIME_MONTHS("%dm", "%d à¤®à¤¹à¤¿à¤¨à¤¾"),
    TIME_YEARS("%dy", "%d à¤µà¤°à¥à¤·"),
    TIME_IN("in %s", "%s à¤®à¤¾"),

    // ============================================
    // BS DATE PICKER
    // ============================================
    BS_DATE_PICKER_TITLE("Select BS Date", "à¤µà¤¿.à¤¸à¤‚. à¤®à¤¿à¤¤à¤¿ à¤›à¤¾à¤¨à¥à¤¨à¥à¤¹à¥‹à¤¸à¥"),
    BS_YEAR("Year", "à¤µà¤°à¥à¤·"),
    BS_MONTH("Month", "à¤®à¤¹à¤¿à¤¨à¤¾"),
    BS_DAY("Day", "à¤¦à¤¿à¤¨"),

    // BS Months
    BS_MONTH_BAISHAKH("Baishakh", "à¤¬à¥ˆà¤¶à¤¾à¤–"),
    BS_MONTH_JESTHA("Jestha", "à¤œà¥‡à¤ "),
    BS_MONTH_ASHADH("Ashadh", "à¤…à¤¸à¤¾à¤°"),
    BS_MONTH_SHRAWAN("Shrawan", "à¤¸à¤¾à¤‰à¤¨"),
    BS_MONTH_BHADRA("Bhadra", "à¤­à¤¦à¥Œ"),
    BS_MONTH_ASHWIN("Ashwin", "à¤…à¤¸à¥‹à¤œ"),
    BS_MONTH_KARTIK("Kartik", "à¤•à¤¾à¤°à¥à¤¤à¤¿à¤•"),
    BS_MONTH_MANGSIR("Mangsir", "à¤®à¤‚à¤¸à¤¿à¤°"),
    BS_MONTH_POUSH("Poush", "à¤ªà¥à¤·"),
    BS_MONTH_MAGH("Magh", "à¤®à¤¾à¤˜"),
    BS_MONTH_FALGUN("Falgun", "à¤«à¤¾à¤²à¥à¤—à¥à¤¨"),
    BS_MONTH_CHAITRA("Chaitra", "à¤šà¥ˆà¤¤à¥à¤°"),

    // Days of week (Nepali)
    DAY_SUNDAY("Sunday", "à¤†à¤‡à¤¤à¤¬à¤¾à¤°"),
    DAY_MONDAY("Monday", "à¤¸à¥‹à¤®à¤¬à¤¾à¤°"),
    DAY_TUESDAY("Tuesday", "à¤®à¤‚à¤—à¤²à¤¬à¤¾à¤°"),
    DAY_WEDNESDAY("Wednesday", "à¤¬à¥à¤§à¤¬à¤¾à¤°"),
    DAY_THURSDAY("Thursday", "à¤¬à¤¿à¤¹à¤¿à¤¬à¤¾à¤°"),
    DAY_FRIDAY("Friday", "à¤¶à¥à¤•à¥à¤°à¤¬à¤¾à¤°"),
    DAY_SATURDAY("Saturday", "à¤¶à¤¨à¤¿à¤¬à¤¾à¤°"),
    DAY_ANY("any day", "à¤•à¥à¤¨à¥ˆ à¤ªà¤¨à¤¿ à¤¦à¤¿à¤¨"),

    // ============================================
    // MISCELLANEOUS
    // ============================================
    MISC_UNAVAILABLE("Unavailable", "à¤‰à¤ªà¤²à¤¬à¥à¤§ à¤›à¥ˆà¤¨"),
    MISC_LOADING("Loading...", "à¤²à¥‹à¤¡ à¤¹à¥à¤à¤¦à¥ˆà¤›..."),
    MISC_NO_DATA("No data available", "à¤¡à¤¾à¤Ÿà¤¾ à¤‰à¤ªà¤²à¤¬à¥à¤§ à¤›à¥ˆà¤¨"),
    MISC_UNKNOWN("Unknown", "à¤…à¤œà¥à¤žà¤¾à¤¤"),
    MISC_EXPAND("Expand", "à¤µà¤¿à¤¸à¥à¤¤à¤¾à¤°"),
    MISC_COLLAPSE("Collapse", "à¤¸à¤‚à¤•à¥à¤šà¤¨"),
    MISC_MORE("More", "à¤¥à¤ª"),
    MISC_LESS("Less", "à¤•à¤®"),

    // ============================================
    // MATCHMAKING CALCULATOR - VARNA
    // ============================================
    VARNA_BRAHMIN("Brahmin", "à¤¬à¥à¤°à¤¾à¤¹à¥à¤®à¤£"),
    VARNA_KSHATRIYA("Kshatriya", "à¤•à¥à¤·à¤¤à¥à¤°à¤¿à¤¯"),
    VARNA_VAISHYA("Vaishya", "à¤µà¥ˆà¤¶à¥à¤¯"),
    VARNA_SHUDRA("Shudra", "à¤¶à¥‚à¤¦à¥à¤°"),

    // ============================================
    // MATCHMAKING CALCULATOR - VASHYA
    // ============================================
    VASHYA_CHATUSHPADA("Quadruped", "à¤šà¤¤à¥à¤·à¥à¤ªà¤¦"),
    VASHYA_MANAVA("Human", "à¤®à¤¾à¤¨à¤µ"),
    VASHYA_JALACHARA("Aquatic", "à¤œà¤²à¤šà¤°"),
    VASHYA_VANACHARA("Wild", "à¤µà¤¨à¤šà¤°"),
    VASHYA_KEETA("Insect", "à¤•à¥€à¤Ÿ"),

    // ============================================
    // MATCHMAKING CALCULATOR - GANA
    // ============================================
    GANA_DEVA("Deva", "à¤¦à¥‡à¤µ"),
    GANA_DEVA_DESC("Divine - Sattvik, gentle, spiritual", "à¤¦à¥ˆà¤µà¥€ - à¤¸à¤¾à¤¤à¥à¤¤à¥à¤µà¤¿à¤•, à¤¸à¥Œà¤®à¥à¤¯, à¤†à¤§à¥à¤¯à¤¾à¤¤à¥à¤®à¤¿à¤•"),
    GANA_MANUSHYA("Manushya", "à¤®à¤¨à¥à¤·à¥à¤¯"),
    GANA_MANUSHYA_DESC("Human - Rajasik, balanced, worldly", "à¤®à¤¾à¤¨à¤µ - à¤°à¤¾à¤œà¤¸à¤¿à¤•, à¤¸à¤¨à¥à¤¤à¥à¤²à¤¿à¤¤, à¤¸à¤¾à¤‚à¤¸à¤¾à¤°à¤¿à¤•"),
    GANA_RAKSHASA("Rakshasa", "à¤°à¤¾à¤•à¥à¤·à¤¸"),
    GANA_RAKSHASA_DESC("Demon - Tamasik, aggressive, dominant", "à¤¦à¤¾à¤¨à¤µ - à¤¤à¤¾à¤®à¤¸à¤¿à¤•, à¤†à¤•à¥à¤°à¤¾à¤®à¤•, à¤ªà¥à¤°à¤­à¤¾à¤µà¤¶à¤¾à¤²à¥€"),

    // ============================================
    // MATCHMAKING CALCULATOR - YONI ANIMALS
    // ============================================
    YONI_HORSE("Horse", "à¤˜à¥‹à¤¡à¤¾"),
    YONI_ELEPHANT("Elephant", "à¤¹à¤¾à¤¤à¥à¤¤à¥€"),
    YONI_SHEEP("Sheep", "à¤­à¥‡à¤¡à¤¾"),
    YONI_SERPENT("Serpent", "à¤¸à¤°à¥à¤ª"),
    YONI_DOG("Dog", "à¤•à¥à¤•à¥à¤°"),
    YONI_CAT("Cat", "à¤¬à¤¿à¤°à¤¾à¤²à¥‹"),
    YONI_RAT("Rat", "à¤®à¥à¤¸à¥‹"),
    YONI_COW("Cow", "à¤—à¤¾à¤ˆ"),
    YONI_BUFFALO("Buffalo", "à¤­à¥ˆà¤‚à¤¸à¥€"),
    YONI_TIGER("Tiger", "à¤¬à¤¾à¤˜"),
    YONI_DEER("Deer", "à¤®à¥ƒà¤—"),
    YONI_MONKEY("Monkey", "à¤¬à¤¾à¤à¤¦à¤°"),
    YONI_MONGOOSE("Mongoose", "à¤¨à¥à¤¯à¥Œà¤°à¥€"),
    YONI_LION("Lion", "à¤¸à¤¿à¤‚à¤¹"),
    YONI_MALE("Male", "à¤ªà¥à¤°à¥à¤·"),
    YONI_FEMALE("Female", "à¤¸à¥à¤¤à¥à¤°à¥€"),

    // ============================================
    // MATCHMAKING CALCULATOR - NADI
    // ============================================
    NADI_ADI("Adi (Vata)", "à¤†à¤¦à¤¿ (à¤µà¤¾à¤¤)"),
    NADI_ADI_DESC("Beginning - Wind element, controls movement and nervous system", "à¤†à¤¦à¤¿ - à¤µà¤¾à¤¯à¥ à¤¤à¤¤à¥à¤µ, à¤—à¤¤à¤¿ à¤° à¤¸à¥à¤¨à¤¾à¤¯à¥ à¤ªà¥à¤°à¤£à¤¾à¤²à¥€ à¤¨à¤¿à¤¯à¤¨à¥à¤¤à¥à¤°à¤£ à¤—à¤°à¥à¤›"),
    NADI_MADHYA("Madhya (Pitta)", "à¤®à¤§à¥à¤¯ (à¤ªà¤¿à¤¤à¥à¤¤)"),
    NADI_MADHYA_DESC("Middle - Fire element, controls digestion and metabolism", "à¤®à¤§à¥à¤¯ - à¤…à¤—à¥à¤¨à¤¿ à¤¤à¤¤à¥à¤µ, à¤ªà¤¾à¤šà¤¨ à¤° à¤šà¤¯à¤¾à¤ªà¤šà¤¯ à¤¨à¤¿à¤¯à¤¨à¥à¤¤à¥à¤°à¤£ à¤—à¤°à¥à¤›"),
    NADI_ANTYA("Antya (Kapha)", "à¤…à¤¨à¥à¤¤à¥à¤¯ (à¤•à¤«)"),
    NADI_ANTYA_DESC("End - Water element, controls structure and lubrication", "à¤…à¤¨à¥à¤¤à¥à¤¯ - à¤œà¤² à¤¤à¤¤à¥à¤µ, à¤¸à¤‚à¤°à¤šà¤¨à¤¾ à¤° à¤¸à¥à¤¨à¥‡à¤¹à¤¨ à¤¨à¤¿à¤¯à¤¨à¥à¤¤à¥à¤°à¤£ à¤—à¤°à¥à¤›"),

    // ============================================
    // MATCHMAKING CALCULATOR - RAJJU
    // ============================================
    RAJJU_PADA("Pada Rajju", "à¤ªà¤¾à¤¦ à¤°à¤œà¥à¤œà¥"),
    RAJJU_PADA_BODY("Feet", "à¤ªà¤¾à¤‰"),
    RAJJU_KATI("Kati Rajju", "à¤•à¤Ÿà¤¿ à¤°à¤œà¥à¤œà¥"),
    RAJJU_KATI_BODY("Waist", "à¤•à¤®à¥à¤®à¤°"),
    RAJJU_NABHI("Nabhi Rajju", "à¤¨à¤¾à¤­à¤¿ à¤°à¤œà¥à¤œà¥"),
    RAJJU_NABHI_BODY("Navel", "à¤¨à¤¾à¤­à¤¿"),
    RAJJU_KANTHA("Kantha Rajju", "à¤•à¤£à¥à¤  à¤°à¤œà¥à¤œà¥"),
    RAJJU_KANTHA_BODY("Neck", "à¤˜à¤¾à¤à¤Ÿà¥€"),
    RAJJU_SIRO("Siro Rajju", "à¤¶à¤¿à¤°à¥‹ à¤°à¤œà¥à¤œà¥"),
    RAJJU_SIRO_BODY("Head", "à¤¶à¤¿à¤°"),
    RAJJU_SIRO_WARNING("Most serious - affects longevity of spouse", "à¤¸à¤¬à¥ˆà¤­à¤¨à¥à¤¦à¤¾ à¤—à¤®à¥à¤­à¥€à¤° - à¤œà¥€à¤µà¤¨à¤¸à¤¾à¤¥à¥€à¤•à¥‹ à¤†à¤¯à¥à¤®à¤¾ à¤…à¤¸à¤° à¤ªà¤¾à¤°à¥à¤›"),
    RAJJU_KANTHA_WARNING("May cause health issues to both", "à¤¦à¥à¤µà¥ˆà¤²à¤¾à¤ˆ à¤¸à¥à¤µà¤¾à¤¸à¥à¤¥à¥à¤¯ à¤¸à¤®à¤¸à¥à¤¯à¤¾ à¤¹à¥à¤¨ à¤¸à¤•à¥à¤›"),
    RAJJU_NABHI_WARNING("May affect children", "à¤¸à¤¨à¥à¤¤à¤¾à¤¨à¤®à¤¾ à¤…à¤¸à¤° à¤ªà¤°à¥à¤¨ à¤¸à¤•à¥à¤›"),
    RAJJU_KATI_WARNING("May cause financial difficulties", "à¤†à¤°à¥à¤¥à¤¿à¤• à¤•à¤ à¤¿à¤¨à¤¾à¤‡à¤¹à¤°à¥‚ à¤¹à¥à¤¨ à¤¸à¤•à¥à¤›"),
    RAJJU_PADA_WARNING("May cause wandering tendencies", "à¤­à¥à¤°à¤®à¤£ à¤ªà¥à¤°à¤µà¥ƒà¤¤à¥à¤¤à¤¿ à¤¹à¥à¤¨ à¤¸à¤•à¥à¤›"),

    // ============================================
    // MATCHMAKING CALCULATOR - MANGLIK DOSHA
    // ============================================
    MANGLIK_NONE("No Manglik Dosha", "à¤®à¤¾à¤‚à¤—à¤²à¤¿à¤• à¤¦à¥‹à¤· à¤›à¥ˆà¤¨"),
    MANGLIK_PARTIAL("Partial Manglik", "à¤†à¤‚à¤¶à¤¿à¤• à¤®à¤¾à¤‚à¤—à¤²à¤¿à¤•"),
    MANGLIK_FULL("Full Manglik", "à¤ªà¥‚à¤°à¥à¤£ à¤®à¤¾à¤‚à¤—à¤²à¤¿à¤•"),
    MANGLIK_DOUBLE("Double Manglik (Severe)", "à¤¦à¥‹à¤¹à¥‹à¤°à¥‹ à¤®à¤¾à¤‚à¤—à¤²à¤¿à¤• (à¤—à¤®à¥à¤­à¥€à¤°)"),
    MANGLIK_NO_DOSHA_DESC("No Manglik Dosha present.", "à¤®à¤¾à¤‚à¤—à¤²à¤¿à¤• à¤¦à¥‹à¤· à¤›à¥ˆà¤¨à¥¤"),
    MANGLIK_DETECTED("detected", "à¤ªà¤¤à¥à¤¤à¤¾ à¤²à¤¾à¤—à¥à¤¯à¥‹"),
    MANGLIK_INTENSITY("intensity", "à¤¤à¥€à¤µà¥à¤°à¤¤à¤¾"),
    MANGLIK_MARS_IN("Mars in", "à¤®à¤‚à¤—à¤²"),
    FROM_LAGNA("from Lagna", "à¤²à¤—à¥à¤¨à¤¬à¤¾à¤Ÿ"),
    FROM_MOON("from Moon", "à¤šà¤¨à¥à¤¦à¥à¤°à¤¬à¤¾à¤Ÿ"),
    FROM_VENUS("from Venus", "à¤¶à¥à¤•à¥à¤°à¤¬à¤¾à¤Ÿ"),
    MANGLIK_FACTOR_FROM_LAGNA("Mars in House {house} from Lagna", "à¤²à¤—à¥à¤¨à¤¬à¤¾à¤Ÿ à¤­à¤¾à¤µ {house} à¤®à¤¾ à¤®à¤‚à¤—à¤²"),
    MANGLIK_FACTOR_FROM_MOON("Mars in House {house} from Moon", "à¤šà¤¨à¥à¤¦à¥à¤°à¤¬à¤¾à¤Ÿ à¤­à¤¾à¤µ {house} à¤®à¤¾ à¤®à¤‚à¤—à¤²"),
    MANGLIK_FACTOR_FROM_VENUS("Mars in House {house} from Venus", "à¤¶à¥à¤•à¥à¤°à¤¬à¤¾à¤Ÿ à¤­à¤¾à¤µ {house} à¤®à¤¾ à¤®à¤‚à¤—à¤²"),
    MANGLIK_BOTH_NON("Both non-Manglik - No concerns", "à¤¦à¥à¤µà¥ˆ à¤—à¥ˆà¤°-à¤®à¤¾à¤‚à¤—à¤²à¤¿à¤• - à¤•à¥à¤¨à¥ˆ à¤šà¤¿à¤¨à¥à¤¤à¤¾ à¤›à¥ˆà¤¨"),
    MANGLIK_BOTH_MATCH("Both Manglik - Doshas cancel each other (Manglik to Manglik match is recommended)", "à¤¦à¥à¤µà¥ˆ à¤®à¤¾à¤‚à¤—à¤²à¤¿à¤• - à¤¦à¥‹à¤·à¤¹à¤°à¥‚ à¤à¤•à¤…à¤°à¥à¤•à¤¾à¤²à¤¾à¤ˆ à¤¨à¤¿à¤·à¥à¤•à¥à¤°à¤¿à¤¯ à¤—à¤°à¥à¤›à¤¨à¥ (à¤®à¤¾à¤‚à¤—à¤²à¤¿à¤•à¤¸à¤à¤— à¤®à¤¾à¤‚à¤—à¤²à¤¿à¤• à¤®à¤¿à¤²à¤¾à¤¨ à¤¸à¤¿à¤«à¤¾à¤°à¤¿à¤¸ à¤—à¤°à¤¿à¤à¤•à¥‹)"),
    MANGLIK_MINOR_IMBALANCE("Minor Manglik imbalance - Manageable with remedies", "à¤¸à¤¾à¤¨à¥‹ à¤®à¤¾à¤‚à¤—à¤²à¤¿à¤• à¤…à¤¸à¤¨à¥à¤¤à¥à¤²à¤¨ - à¤‰à¤ªà¤¾à¤¯à¤¹à¤°à¥‚à¤¦à¥à¤µà¤¾à¤°à¤¾ à¤µà¥à¤¯à¤µà¤¸à¥à¤¥à¤¾à¤ªà¤¨à¤¯à¥‹à¤—à¥à¤¯"),
    MANGLIK_BRIDE_ONLY("Bride is Manglik while Groom is not - Kumbh Vivah or other remedies advised", "à¤¦à¥à¤²à¤¹à¥€ à¤®à¤¾à¤‚à¤—à¤²à¤¿à¤• à¤›à¤¿à¤¨à¥ à¤œà¤¬à¤•à¤¿ à¤¦à¥à¤²à¤¾à¤¹à¤¾ à¤›à¥ˆà¤¨à¤¨à¥ - à¤•à¥à¤®à¥à¤­ à¤µà¤¿à¤µà¤¾à¤¹ à¤µà¤¾ à¤…à¤¨à¥à¤¯ à¤‰à¤ªà¤¾à¤¯à¤¹à¤°à¥‚ à¤¸à¤²à¥à¤²à¤¾à¤¹ à¤¦à¤¿à¤‡à¤à¤•à¥‹"),
    MANGLIK_GROOM_ONLY("Groom is Manglik while Bride is not - Remedies strongly recommended", "à¤¦à¥à¤²à¤¾à¤¹à¤¾ à¤®à¤¾à¤‚à¤—à¤²à¤¿à¤• à¤›à¤¨à¥ à¤œà¤¬à¤•à¤¿ à¤¦à¥à¤²à¤¹à¥€ à¤›à¥ˆà¤¨à¤¨à¥ - à¤‰à¤ªà¤¾à¤¯à¤¹à¤°à¥‚ à¤¦à¥ƒà¤¢à¤¤à¤¾à¤ªà¥‚à¤°à¥à¤µà¤• à¤¸à¤¿à¤«à¤¾à¤°à¤¿à¤¸ à¤—à¤°à¤¿à¤à¤•à¥‹"),
    MANGLIK_SIGNIFICANT_IMBALANCE("Significant Manglik imbalance - Careful consideration and remedies essential", "à¤®à¤¹à¤¤à¥à¤¤à¥à¤µà¤ªà¥‚à¤°à¥à¤£ à¤®à¤¾à¤‚à¤—à¤²à¤¿à¤• à¤…à¤¸à¤¨à¥à¤¤à¥à¤²à¤¨ - à¤¸à¤¾à¤µà¤§à¤¾à¤¨à¥€à¤ªà¥‚à¤°à¥à¤£ à¤µà¤¿à¤šà¤¾à¤° à¤° à¤‰à¤ªà¤¾à¤¯à¤¹à¤°à¥‚ à¤†à¤µà¤¶à¥à¤¯à¤•"),

    // ============================================
    // MATCHMAKING CALCULATOR - COMPATIBILITY RATINGS
    // ============================================
    COMPAT_EXCELLENT("Excellent Match", "à¤‰à¤¤à¥à¤•à¥ƒà¤·à¥à¤Ÿ à¤®à¤¿à¤²à¤¾à¤¨"),
    COMPAT_EXCELLENT_DESC("Highly recommended for marriage. Strong compatibility across all factors with harmonious planetary alignments.", "à¤µà¤¿à¤µà¤¾à¤¹à¤•à¥‹ à¤²à¤¾à¤—à¤¿ à¤…à¤¤à¥à¤¯à¤§à¤¿à¤• à¤¸à¤¿à¤«à¤¾à¤°à¤¿à¤¸ à¤—à¤°à¤¿à¤à¤•à¥‹à¥¤ à¤¸à¤¬à¥ˆ à¤•à¤¾à¤°à¤•à¤¹à¤°à¥‚à¤®à¤¾ à¤¬à¤²à¤¿à¤¯à¥‹ à¤…à¤¨à¥à¤•à¥‚à¤²à¤¤à¤¾ à¤° à¤¸à¤¾à¤®à¤žà¥à¤œà¤¸à¥à¤¯à¤ªà¥‚à¤°à¥à¤£ à¤—à¥à¤°à¤¹ à¤¸à¤‚à¤°à¥‡à¤–à¤£à¥¤"),
    COMPAT_GOOD("Good Match", "à¤°à¤¾à¤®à¥à¤°à¥‹ à¤®à¤¿à¤²à¤¾à¤¨"),
    COMPAT_GOOD_DESC("Recommended. Good overall compatibility with minor differences that can be easily managed.", "à¤¸à¤¿à¤«à¤¾à¤°à¤¿à¤¸ à¤—à¤°à¤¿à¤à¤•à¥‹à¥¤ à¤¸à¤œà¤¿à¤²à¥ˆà¤¸à¤à¤— à¤µà¥à¤¯à¤µà¤¸à¥à¤¥à¤¾à¤ªà¤¨ à¤—à¤°à¥à¤¨ à¤¸à¤•à¤¿à¤¨à¥‡ à¤¸à¤¾à¤¨à¤¾à¤¤à¤¿à¤¨à¤¾ à¤­à¤¿à¤¨à¥à¤¨à¤¤à¤¾à¤¹à¤°à¥‚à¤¸à¤¹à¤¿à¤¤ à¤°à¤¾à¤®à¥à¤°à¥‹ à¤¸à¤®à¤—à¥à¤° à¤…à¤¨à¥à¤•à¥‚à¤²à¤¤à¤¾à¥¤"),
    COMPAT_AVERAGE("Average Match", "à¤”à¤¸à¤¤ à¤®à¤¿à¤²à¤¾à¤¨"),
    COMPAT_AVERAGE_DESC("Acceptable with some remedies. Moderate compatibility requiring mutual understanding and effort.", "à¤•à¥‡à¤¹à¥€ à¤‰à¤ªà¤¾à¤¯à¤¹à¤°à¥‚à¤¸à¤¹à¤¿à¤¤ à¤¸à¥à¤µà¥€à¤•à¤¾à¤°à¥à¤¯à¥¤ à¤ªà¤¾à¤°à¤¸à¥à¤ªà¤°à¤¿à¤• à¤¬à¥à¤à¤¾à¤‡ à¤° à¤ªà¥à¤°à¤¯à¤¾à¤¸ à¤†à¤µà¤¶à¥à¤¯à¤• à¤ªà¤°à¥à¤¨à¥‡ à¤®à¤§à¥à¤¯à¤® à¤…à¤¨à¥à¤•à¥‚à¤²à¤¤à¤¾à¥¤"),
    COMPAT_BELOW_AVERAGE("Below Average", "à¤”à¤¸à¤¤à¤®à¥à¤¨à¤¿"),
    COMPAT_BELOW_AVERAGE_DESC("Caution advised. Several compatibility issues that need addressing through remedies and counseling.", "à¤¸à¤¾à¤µà¤§à¤¾à¤¨à¥€ à¤†à¤µà¤¶à¥à¤¯à¤•à¥¤ à¤‰à¤ªà¤¾à¤¯ à¤° à¤ªà¤°à¤¾à¤®à¤°à¥à¤¶à¤¦à¥à¤µà¤¾à¤°à¤¾ à¤¸à¤®à¥à¤¬à¥‹à¤§à¤¨ à¤—à¤°à¥à¤¨à¥à¤ªà¤°à¥à¤¨à¥‡ à¤§à¥‡à¤°à¥ˆ à¤…à¤¨à¥à¤•à¥‚à¤²à¤¤à¤¾ à¤¸à¤®à¤¸à¥à¤¯à¤¾à¤¹à¤°à¥‚à¥¤"),
    COMPAT_POOR("Poor Match", "à¤•à¤®à¤œà¥‹à¤° à¤®à¤¿à¤²à¤¾à¤¨"),
    COMPAT_POOR_DESC("Not recommended. Significant compatibility challenges that may cause ongoing difficulties.", "à¤¸à¤¿à¤«à¤¾à¤°à¤¿à¤¸ à¤—à¤°à¤¿à¤à¤•à¥‹ à¤›à¥ˆà¤¨à¥¤ à¤¨à¤¿à¤°à¤¨à¥à¤¤à¤° à¤•à¤ à¤¿à¤¨à¤¾à¤‡à¤¹à¤°à¥‚ à¤²à¥à¤¯à¤¾à¤‰à¤¨ à¤¸à¤•à¥à¤¨à¥‡ à¤®à¤¹à¤¤à¥à¤¤à¥à¤µà¤ªà¥‚à¤°à¥à¤£ à¤…à¤¨à¥à¤•à¥‚à¤²à¤¤à¤¾ à¤šà¥à¤¨à¥Œà¤¤à¥€à¤¹à¤°à¥‚à¥¤"),

    // ============================================
    // MATCHMAKING CALCULATOR - TARA NAMES
    // ============================================
    TARA_JANMA("Janma (Birth)", "à¤œà¤¨à¥à¤®"),
    TARA_SAMPAT("Sampat (Wealth)", "à¤¸à¤®à¥à¤ªà¤¤à¥ (à¤§à¤¨)"),
    TARA_VIPAT("Vipat (Danger)", "à¤µà¤¿à¤ªà¤¤à¥ (à¤–à¤¤à¤°à¤¾)"),
    TARA_KSHEMA("Kshema (Wellbeing)", "à¤•à¥à¤·à¥‡à¤® (à¤•à¤²à¥à¤¯à¤¾à¤£)"),
    TARA_PRATYARI("Pratyari (Obstacle)", "à¤ªà¥à¤°à¤¤à¥à¤¯à¤°à¤¿ (à¤¬à¤¾à¤§à¤¾)"),
    TARA_SADHANA("Sadhana (Achievement)", "à¤¸à¤¾à¤§à¤¨à¤¾ (à¤‰à¤ªà¤²à¤¬à¥à¤§à¤¿)"),
    TARA_VADHA("Vadha (Death)", "à¤µà¤§ (à¤®à¥ƒà¤¤à¥à¤¯à¥)"),
    TARA_MITRA("Mitra (Friend)", "à¤®à¤¿à¤¤à¥à¤°"),
    TARA_PARAMA_MITRA("Parama Mitra (Best Friend)", "à¤ªà¤°à¤® à¤®à¤¿à¤¤à¥à¤°"),

    // ============================================
    // GUNA MILAN ANALYSIS STRINGS
    // ============================================
    // Varna Analysis
    VARNA_DESC("Spiritual compatibility and ego harmony", "à¤†à¤§à¥à¤¯à¤¾à¤¤à¥à¤®à¤¿à¤• à¤…à¤¨à¥à¤•à¥‚à¤²à¤¤à¤¾ à¤° à¤…à¤¹à¤‚à¤•à¤¾à¤° à¤¸à¤¾à¤®à¤žà¥à¤œà¤¸à¥à¤¯"),
    VARNA_COMPATIBLE("Compatible: Groom's Varna ({groom}) is equal to or higher than Bride's ({bride}). This indicates spiritual harmony.", "à¤…à¤¨à¥à¤•à¥‚à¤²: à¤¦à¥à¤²à¤¾à¤¹à¤¾à¤•à¥‹ à¤µà¤°à¥à¤£ ({groom}) à¤¦à¥à¤²à¤¹à¥€à¤•à¥‹ ({bride}) à¤¬à¤°à¤¾à¤¬à¤° à¤µà¤¾ à¤®à¤¾à¤¥à¤¿ à¤›à¥¤ à¤¯à¤¸à¤²à¥‡ à¤†à¤§à¥à¤¯à¤¾à¤¤à¥à¤®à¤¿à¤• à¤¸à¤¾à¤®à¤žà¥à¤œà¤¸à¥à¤¯ à¤¸à¤‚à¤•à¥‡à¤¤ à¤—à¤°à¥à¤›à¥¤"),
    VARNA_INCOMPATIBLE("Mismatch: Bride's Varna ({bride}) is higher than Groom's ({groom}). May cause ego-related issues.", "à¤¬à¥‡à¤®à¥‡à¤²: à¤¦à¥à¤²à¤¹à¥€à¤•à¥‹ à¤µà¤°à¥à¤£ ({bride}) à¤¦à¥à¤²à¤¾à¤¹à¤¾à¤•à¥‹ ({groom}) à¤­à¤¨à¥à¤¦à¤¾ à¤®à¤¾à¤¥à¤¿ à¤›à¥¤ à¤…à¤¹à¤‚à¤•à¤¾à¤°-à¤¸à¤®à¥à¤¬à¤¨à¥à¤§à¤¿à¤¤ à¤¸à¤®à¤¸à¥à¤¯à¤¾ à¤¹à¥à¤¨ à¤¸à¤•à¥à¤›à¥¤"),

    // Vashya Analysis
    VASHYA_DESC("Mutual attraction and influence", "à¤ªà¤¾à¤°à¤¸à¥à¤ªà¤°à¤¿à¤• à¤†à¤•à¤°à¥à¤·à¤£ à¤° à¤ªà¥à¤°à¤­à¤¾à¤µ"),
    VASHYA_EXCELLENT("Excellent mutual attraction and influence. Both partners can positively influence each other.", "à¤‰à¤¤à¥à¤•à¥ƒà¤·à¥à¤Ÿ à¤ªà¤¾à¤°à¤¸à¥à¤ªà¤°à¤¿à¤• à¤†à¤•à¤°à¥à¤·à¤£ à¤° à¤ªà¥à¤°à¤­à¤¾à¤µà¥¤ à¤¦à¥à¤µà¥ˆ à¤¸à¤¾à¤à¥‡à¤¦à¤¾à¤°à¤¹à¤°à¥‚ à¤à¤•à¤…à¤°à¥à¤•à¤¾à¤²à¤¾à¤ˆ à¤¸à¤•à¤¾à¤°à¤¾à¤¤à¥à¤®à¤• à¤°à¥‚à¤ªà¤®à¤¾ à¤ªà¥à¤°à¤­à¤¾à¤µ à¤ªà¤¾à¤°à¥à¤¨ à¤¸à¤•à¥à¤›à¤¨à¥à¥¤"),
    VASHYA_VERY_GOOD("Very good compatibility with balanced influence between partners.", "à¤¸à¤¾à¤à¥‡à¤¦à¤¾à¤°à¤¹à¤°à¥‚ à¤¬à¥€à¤š à¤¸à¤¨à¥à¤¤à¥à¤²à¤¿à¤¤ à¤ªà¥à¤°à¤­à¤¾à¤µà¤¸à¤¹à¤¿à¤¤ à¤§à¥‡à¤°à¥ˆ à¤°à¤¾à¤®à¥à¤°à¥‹ à¤…à¤¨à¥à¤•à¥‚à¤²à¤¤à¤¾à¥¤"),
    VASHYA_GOOD("Good compatibility with moderate mutual influence.", "à¤®à¤§à¥à¤¯à¤® à¤ªà¤¾à¤°à¤¸à¥à¤ªà¤°à¤¿à¤• à¤ªà¥à¤°à¤­à¤¾à¤µà¤¸à¤¹à¤¿à¤¤ à¤°à¤¾à¤®à¥à¤°à¥‹ à¤…à¤¨à¥à¤•à¥‚à¤²à¤¤à¤¾à¥¤"),
    VASHYA_PARTIAL("Partial compatibility. One partner may dominate relationship dynamics.", "à¤†à¤‚à¤¶à¤¿à¤• à¤…à¤¨à¥à¤•à¥‚à¤²à¤¤à¤¾à¥¤ à¤à¤‰à¤Ÿà¤¾ à¤¸à¤¾à¤à¥‡à¤¦à¤¾à¤°à¤²à¥‡ à¤¸à¤®à¥à¤¬à¤¨à¥à¤§à¤•à¥‹ à¤—à¤¤à¤¿à¤¶à¥€à¤²à¤¤à¤¾à¤®à¤¾ à¤ªà¥à¤°à¤­à¥à¤¤à¥à¤µ à¤œà¤®à¤¾à¤‰à¤¨ à¤¸à¤•à¥à¤›à¥¤"),
    VASHYA_INCOMPATIBLE("Incompatible Vashya types. May cause power struggles in the relationship.", "à¤…à¤¸à¤‚à¤—à¤¤ à¤µà¤¶à¥à¤¯ à¤ªà¥à¤°à¤•à¤¾à¤°à¤¹à¤°à¥‚à¥¤ à¤¸à¤®à¥à¤¬à¤¨à¥à¤§à¤®à¤¾ à¤¶à¤•à¥à¤¤à¤¿ à¤¸à¤‚à¤˜à¤°à¥à¤· à¤¹à¥à¤¨ à¤¸à¤•à¥à¤›à¥¤"),

    // Tara Analysis
    TARA_DESC("Destiny and birth star compatibility", "à¤­à¤¾à¤—à¥à¤¯ à¤° à¤œà¤¨à¥à¤® à¤¤à¤¾à¤°à¤¾ à¤…à¤¨à¥à¤•à¥‚à¤²à¤¤à¤¾"),
    TARA_EXCELLENT("Both have auspicious Taras - excellent destiny compatibility. Harmonious life path.", "à¤¦à¥à¤µà¥ˆà¤•à¥‹ à¤¶à¥à¤­ à¤¤à¤¾à¤°à¤¾ à¤› - à¤‰à¤¤à¥à¤•à¥ƒà¤·à¥à¤Ÿ à¤­à¤¾à¤—à¥à¤¯ à¤…à¤¨à¥à¤•à¥‚à¤²à¤¤à¤¾à¥¤ à¤¸à¤¾à¤®à¤žà¥à¤œà¤¸à¥à¤¯à¤ªà¥‚à¤°à¥à¤£ à¤œà¥€à¤µà¤¨ à¤®à¤¾à¤°à¥à¤—à¥¤"),
    TARA_MODERATE("One auspicious Tara present - moderate destiny compatibility.", "à¤à¤‰à¤Ÿà¤¾ à¤¶à¥à¤­ à¤¤à¤¾à¤°à¤¾ à¤‰à¤ªà¤¸à¥à¤¥à¤¿à¤¤ - à¤®à¤§à¥à¤¯à¤® à¤­à¤¾à¤—à¥à¤¯ à¤…à¤¨à¥à¤•à¥‚à¤²à¤¤à¤¾à¥¤"),
    TARA_INAUSPICIOUS("Both Taras are inauspicious - may face obstacles together. Remedies recommended.", "à¤¦à¥à¤µà¥ˆ à¤¤à¤¾à¤°à¤¾ à¤…à¤¶à¥à¤­ - à¤¸à¤à¤—à¥ˆ à¤…à¤µà¤°à¥‹à¤§à¤¹à¤°à¥‚ à¤¸à¤¾à¤®à¤¨à¤¾ à¤—à¤°à¥à¤¨ à¤¸à¤•à¥à¤›à¤¨à¥à¥¤ à¤‰à¤ªà¤¾à¤¯à¤¹à¤°à¥‚ à¤¸à¤¿à¤«à¤¾à¤°à¤¿à¤¸ à¤—à¤°à¤¿à¤à¤•à¥‹à¥¤"),

    // Yoni Analysis
    YONI_DESC("Physical and sexual compatibility", "à¤¶à¤¾à¤°à¥€à¤°à¤¿à¤• à¤° à¤¯à¥Œà¤¨ à¤…à¤¨à¥à¤•à¥‚à¤²à¤¤à¤¾"),
    YONI_SAME("Same Yoni animal - perfect physical and instinctual compatibility. Strong natural attraction.", "à¤‰à¤¹à¥€ à¤¯à¥‹à¤¨à¤¿ à¤ªà¤¶à¥ - à¤‰à¤¤à¥à¤¤à¤® à¤¶à¤¾à¤°à¥€à¤°à¤¿à¤• à¤° à¤¸à¤¹à¤œ à¤…à¤¨à¥à¤•à¥‚à¤²à¤¤à¤¾à¥¤ à¤¬à¤²à¤¿à¤¯à¥‹ à¤ªà¥à¤°à¤¾à¤•à¥ƒà¤¤à¤¿à¤• à¤†à¤•à¤°à¥à¤·à¤£à¥¤"),
    YONI_FRIENDLY("Friendly Yonis - very good physical compatibility. Natural understanding.", "à¤®à¤¿à¤¤à¥à¤° à¤¯à¥‹à¤¨à¤¿ - à¤§à¥‡à¤°à¥ˆ à¤°à¤¾à¤®à¥à¤°à¥‹ à¤¶à¤¾à¤°à¥€à¤°à¤¿à¤• à¤…à¤¨à¥à¤•à¥‚à¤²à¤¤à¤¾à¥¤ à¤ªà¥à¤°à¤¾à¤•à¥ƒà¤¤à¤¿à¤• à¤¬à¥à¤à¤¾à¤‡à¥¤"),
    YONI_NEUTRAL("Neutral Yonis - moderate physical compatibility. Requires some adjustment.", "à¤¤à¤Ÿà¤¸à¥à¤¥ à¤¯à¥‹à¤¨à¤¿ - à¤®à¤§à¥à¤¯à¤® à¤¶à¤¾à¤°à¥€à¤°à¤¿à¤• à¤…à¤¨à¥à¤•à¥‚à¤²à¤¤à¤¾à¥¤ à¤•à¥‡à¤¹à¥€ à¤¸à¤®à¤¾à¤¯à¥‹à¤œà¤¨ à¤†à¤µà¤¶à¥à¤¯à¤•à¥¤"),
    YONI_UNFRIENDLY("Unfriendly Yonis - some physical and instinctual differences. Needs conscious effort.", "à¤…à¤®à¥ˆà¤¤à¥à¤°à¥€à¤ªà¥‚à¤°à¥à¤£ à¤¯à¥‹à¤¨à¤¿ - à¤•à¥‡à¤¹à¥€ à¤¶à¤¾à¤°à¥€à¤°à¤¿à¤• à¤° à¤¸à¤¹à¤œ à¤­à¤¿à¤¨à¥à¤¨à¤¤à¤¾à¤¹à¤°à¥‚à¥¤ à¤¸à¤šà¥‡à¤¤ à¤ªà¥à¤°à¤¯à¤¾à¤¸ à¤†à¤µà¤¶à¥à¤¯à¤•à¥¤"),
    YONI_ENEMY("Enemy Yonis - significant physical incompatibility. May face intimacy challenges.", "à¤¶à¤¤à¥à¤°à¥ à¤¯à¥‹à¤¨à¤¿ - à¤®à¤¹à¤¤à¥à¤¤à¥à¤µà¤ªà¥‚à¤°à¥à¤£ à¤¶à¤¾à¤°à¥€à¤°à¤¿à¤• à¤…à¤¸à¤‚à¤—à¤¤à¤¤à¤¾à¥¤ à¤…à¤¨à¥à¤¤à¤°à¤‚à¤—à¤¤à¤¾ à¤šà¥à¤¨à¥Œà¤¤à¥€à¤¹à¤°à¥‚ à¤¸à¤¾à¤®à¤¨à¤¾ à¤—à¤°à¥à¤¨ à¤¸à¤•à¥à¤›à¥¤"),

    // Graha Maitri Analysis
    GRAHA_MAITRI_DESC("Mental compatibility and friendship", "à¤®à¤¾à¤¨à¤¸à¤¿à¤• à¤…à¤¨à¥à¤•à¥‚à¤²à¤¤à¤¾ à¤° à¤®à¤¿à¤¤à¥à¤°à¤¤à¤¾"),
    GRAHA_MAITRI_EXCELLENT("Same lord or mutual friends - excellent mental compatibility. Natural understanding.", "à¤‰à¤¹à¥€ à¤¸à¥à¤µà¤¾à¤®à¥€ à¤µà¤¾ à¤ªà¤¾à¤°à¤¸à¥à¤ªà¤°à¤¿à¤• à¤®à¤¿à¤¤à¥à¤° - à¤‰à¤¤à¥à¤•à¥ƒà¤·à¥à¤Ÿ à¤®à¤¾à¤¨à¤¸à¤¿à¤• à¤…à¤¨à¥à¤•à¥‚à¤²à¤¤à¤¾à¥¤ à¤ªà¥à¤°à¤¾à¤•à¥ƒà¤¤à¤¿à¤• à¤¬à¥à¤à¤¾à¤‡à¥¤"),
    GRAHA_MAITRI_VERY_GOOD("One friend, one neutral - very good mental harmony. Good communication.", "à¤à¤‰à¤Ÿà¤¾ à¤®à¤¿à¤¤à¥à¤°, à¤à¤‰à¤Ÿà¤¾ à¤¤à¤Ÿà¤¸à¥à¤¥ - à¤§à¥‡à¤°à¥ˆ à¤°à¤¾à¤®à¥à¤°à¥‹ à¤®à¤¾à¤¨à¤¸à¤¿à¤• à¤¸à¤¾à¤®à¤žà¥à¤œà¤¸à¥à¤¯à¥¤ à¤°à¤¾à¤®à¥à¤°à¥‹ à¤¸à¤žà¥à¤šà¤¾à¤°à¥¤"),
    GRAHA_MAITRI_AVERAGE("Neutral relationship - average mental compatibility. Requires effort for understanding.", "à¤¤à¤Ÿà¤¸à¥à¤¥ à¤¸à¤®à¥à¤¬à¤¨à¥à¤§ - à¤”à¤¸à¤¤ à¤®à¤¾à¤¨à¤¸à¤¿à¤• à¤…à¤¨à¥à¤•à¥‚à¤²à¤¤à¤¾à¥¤ à¤¬à¥à¤à¤¾à¤‡à¤•à¥‹ à¤²à¤¾à¤—à¤¿ à¤ªà¥à¤°à¤¯à¤¾à¤¸ à¤†à¤µà¤¶à¥à¤¯à¤•à¥¤"),
    GRAHA_MAITRI_FRICTION("One enemy present - some mental friction. Different thought processes.", "à¤à¤‰à¤Ÿà¤¾ à¤¶à¤¤à¥à¤°à¥ à¤‰à¤ªà¤¸à¥à¤¥à¤¿à¤¤ - à¤•à¥‡à¤¹à¥€ à¤®à¤¾à¤¨à¤¸à¤¿à¤• à¤˜à¤°à¥à¤·à¤£à¥¤ à¤«à¤°à¤• à¤µà¤¿à¤šà¤¾à¤° à¤ªà¥à¤°à¤•à¥à¤°à¤¿à¤¯à¤¾à¤¹à¤°à¥‚à¥¤"),
    GRAHA_MAITRI_INCOMPATIBLE("Mutual enemies - significant mental incompatibility. May face frequent misunderstandings.", "à¤ªà¤¾à¤°à¤¸à¥à¤ªà¤°à¤¿à¤• à¤¶à¤¤à¥à¤°à¥ - à¤®à¤¹à¤¤à¥à¤¤à¥à¤µà¤ªà¥‚à¤°à¥à¤£ à¤®à¤¾à¤¨à¤¸à¤¿à¤• à¤…à¤¸à¤‚à¤—à¤¤à¤¤à¤¾à¥¤ à¤¬à¤¾à¤°à¤®à¥à¤¬à¤¾à¤° à¤—à¤²à¤¤à¤«à¤¹à¤®à¥€à¤¹à¤°à¥‚ à¤¹à¥à¤¨ à¤¸à¤•à¥à¤›à¥¤"),

    // Gana Analysis
    GANA_DESC("Temperament and behavior compatibility", "à¤¸à¥à¤µà¤­à¤¾à¤µ à¤° à¤µà¥à¤¯à¤µà¤¹à¤¾à¤° à¤…à¤¨à¥à¤•à¥‚à¤²à¤¤à¤¾"),
    GANA_SAME("Same Gana - perfect temperamental harmony. Similar approach to life and values.", "à¤‰à¤¹à¥€ à¤—à¤£ - à¤‰à¤¤à¥à¤¤à¤® à¤¸à¥à¤µà¤­à¤¾à¤µà¤—à¤¤ à¤¸à¤¾à¤®à¤žà¥à¤œà¤¸à¥à¤¯à¥¤ à¤œà¥€à¤µà¤¨ à¤° à¤®à¥‚à¤²à¥à¤¯à¤¹à¤°à¥‚à¤®à¤¾ à¤¸à¤®à¤¾à¤¨ à¤¦à¥ƒà¤·à¥à¤Ÿà¤¿à¤•à¥‹à¤£à¥¤"),
    GANA_COMPATIBLE("Compatible Ganas - good temperamental harmony with minor differences.", "à¤…à¤¨à¥à¤•à¥‚à¤² à¤—à¤£ - à¤¸à¤¾à¤¨à¤¾à¤¤à¤¿à¤¨à¤¾ à¤­à¤¿à¤¨à¥à¤¨à¤¤à¤¾à¤¹à¤°à¥‚à¤¸à¤¹à¤¿à¤¤ à¤°à¤¾à¤®à¥à¤°à¥‹ à¤¸à¥à¤µà¤­à¤¾à¤µà¤—à¤¤ à¤¸à¤¾à¤®à¤žà¥à¤œà¤¸à¥à¤¯à¥¤"),
    GANA_PARTIAL("Partially compatible - some temperamental adjustment needed.", "à¤†à¤‚à¤¶à¤¿à¤• à¤…à¤¨à¥à¤•à¥‚à¤² - à¤•à¥‡à¤¹à¥€ à¤¸à¥à¤µà¤­à¤¾à¤µà¤—à¤¤ à¤¸à¤®à¤¾à¤¯à¥‹à¤œà¤¨ à¤†à¤µà¤¶à¥à¤¯à¤•à¥¤"),
    GANA_DIFFERENT("Different temperaments - significant adjustment required. May cause lifestyle clashes.", "à¤«à¤°à¤• à¤¸à¥à¤µà¤­à¤¾à¤µ - à¤®à¤¹à¤¤à¥à¤¤à¥à¤µà¤ªà¥‚à¤°à¥à¤£ à¤¸à¤®à¤¾à¤¯à¥‹à¤œà¤¨ à¤†à¤µà¤¶à¥à¤¯à¤•à¥¤ à¤œà¥€à¤µà¤¨à¤¶à¥ˆà¤²à¥€ à¤Ÿà¤•à¤°à¤¾à¤µ à¤¹à¥à¤¨ à¤¸à¤•à¥à¤›à¥¤"),
    GANA_OPPOSITE("Opposite temperaments - major incompatibility. Frequent conflicts likely.", "à¤µà¤¿à¤ªà¤°à¥€à¤¤ à¤¸à¥à¤µà¤­à¤¾à¤µ - à¤ªà¥à¤°à¤®à¥à¤– à¤…à¤¸à¤‚à¤—à¤¤à¤¤à¤¾à¥¤ à¤¬à¤¾à¤°à¤®à¥à¤¬à¤¾à¤° à¤¦à¥à¤µà¤¨à¥à¤¦à¥à¤µ à¤¹à¥à¤¨ à¤¸à¤•à¥à¤›à¥¤"),

    // Bhakoot Analysis
    BHAKOOT_DESC("Love, health, and financial compatibility", "à¤ªà¥à¤°à¥‡à¤®, à¤¸à¥à¤µà¤¾à¤¸à¥à¤¥à¥à¤¯ à¤° à¤†à¤°à¥à¤¥à¤¿à¤• à¤…à¤¨à¥à¤•à¥‚à¤²à¤¤à¤¾"),
    BHAKOOT_NO_DOSHA("No Bhakoot dosha - excellent compatibility for love, health, and finances.", "à¤­à¤•à¥à¤Ÿ à¤¦à¥‹à¤· à¤›à¥ˆà¤¨ - à¤ªà¥à¤°à¥‡à¤®, à¤¸à¥à¤µà¤¾à¤¸à¥à¤¥à¥à¤¯ à¤° à¤µà¤¿à¤¤à¥à¤¤à¤•à¥‹ à¤²à¤¾à¤—à¤¿ à¤‰à¤¤à¥à¤•à¥ƒà¤·à¥à¤Ÿ à¤…à¤¨à¥à¤•à¥‚à¤²à¤¤à¤¾à¥¤"),
    BHAKOOT_CANCELLED("Bhakoot dosha cancelled by same sign lord - no adverse effects.", "à¤‰à¤¹à¥€ à¤°à¤¾à¤¶à¤¿ à¤¸à¥à¤µà¤¾à¤®à¥€à¤¦à¥à¤µà¤¾à¤°à¤¾ à¤­à¤•à¥à¤Ÿ à¤¦à¥‹à¤· à¤°à¤¦à¥à¤¦ - à¤•à¥à¤¨à¥ˆ à¤ªà¥à¤°à¤¤à¤¿à¤•à¥‚à¤² à¤ªà¥à¤°à¤­à¤¾à¤µ à¤›à¥ˆà¤¨à¥¤"),
    BHAKOOT_2_12("Dhan-Vyaya (2-12) Bhakoot Dosha - financial concerns possible.", "à¤§à¤¨-à¤µà¥à¤¯à¤¯ (à¥¨-à¥§à¥¨) à¤­à¤•à¥à¤Ÿ à¤¦à¥‹à¤· - à¤†à¤°à¥à¤¥à¤¿à¤• à¤šà¤¿à¤¨à¥à¤¤à¤¾ à¤¸à¤®à¥à¤­à¤µà¥¤"),
    BHAKOOT_2_12_DESC("May cause financial fluctuations and differences in spending habits.", "à¤†à¤°à¥à¤¥à¤¿à¤• à¤‰à¤¤à¤¾à¤°-à¤šà¤¢à¤¾à¤µ à¤° à¤–à¤°à¥à¤š à¤¬à¤¾à¤¨à¥€à¤®à¤¾ à¤­à¤¿à¤¨à¥à¤¨à¤¤à¤¾ à¤¹à¥à¤¨ à¤¸à¤•à¥à¤›à¥¤"),
    BHAKOOT_6_8("Shadashtak (6-8) Bhakoot Dosha - health concerns may arise.", "à¤·à¤¡à¤¾à¤·à¥à¤Ÿà¤• (à¥¬-à¥®) à¤­à¤•à¥à¤Ÿ à¤¦à¥‹à¤· - à¤¸à¥à¤µà¤¾à¤¸à¥à¤¥à¥à¤¯ à¤šà¤¿à¤¨à¥à¤¤à¤¾ à¤¹à¥à¤¨ à¤¸à¤•à¥à¤›à¥¤"),
    BHAKOOT_6_8_DESC("May affect health and cause separation tendencies. Most serious Bhakoot dosha.", "à¤¸à¥à¤µà¤¾à¤¸à¥à¤¥à¥à¤¯à¤®à¤¾ à¤…à¤¸à¤° à¤ªà¤¾à¤°à¥à¤¨ à¤° à¤µà¤¿à¤šà¥à¤›à¥‡à¤¦ à¤ªà¥à¤°à¤µà¥ƒà¤¤à¥à¤¤à¤¿ à¤¹à¥à¤¨ à¤¸à¤•à¥à¤›à¥¤ à¤¸à¤¬à¥ˆà¤­à¤¨à¥à¤¦à¤¾ à¤—à¤®à¥à¤­à¥€à¤° à¤­à¤•à¥à¤Ÿ à¤¦à¥‹à¤·à¥¤"),
    BHAKOOT_5_9("Signs are in 5-9 (Trine) relationship - actually favorable.", "à¤°à¤¾à¤¶à¤¿à¤¹à¤°à¥‚ à¥«-à¥¯ (à¤¤à¥à¤°à¤¿à¤•à¥‹à¤£) à¤¸à¤®à¥à¤¬à¤¨à¥à¤§à¤®à¤¾ à¤›à¤¨à¥ - à¤µà¤¾à¤¸à¥à¤¤à¤µà¤®à¤¾ à¤…à¤¨à¥à¤•à¥‚à¤²à¥¤"),
    BHAKOOT_5_9_DESC("Trine relationship is auspicious for progeny, dharma, and spiritual growth.", "à¤¤à¥à¤°à¤¿à¤•à¥‹à¤£ à¤¸à¤®à¥à¤¬à¤¨à¥à¤§ à¤¸à¤¨à¥à¤¤à¤¾à¤¨, à¤§à¤°à¥à¤® à¤° à¤†à¤§à¥à¤¯à¤¾à¤¤à¥à¤®à¤¿à¤• à¤µà¤¿à¤•à¤¾à¤¸à¤•à¥‹ à¤²à¤¾à¤—à¤¿ à¤¶à¥à¤­ à¤›à¥¤"),
    BHAKOOT_FAVORABLE("Signs are in favorable positions for marital harmony.", "à¤°à¤¾à¤¶à¤¿à¤¹à¤°à¥‚ à¤µà¥ˆà¤µà¤¾à¤¹à¤¿à¤• à¤¸à¤¾à¤®à¤žà¥à¤œà¤¸à¥à¤¯à¤•à¥‹ à¤²à¤¾à¤—à¤¿ à¤…à¤¨à¥à¤•à¥‚à¤² à¤¸à¥à¤¥à¤¾à¤¨à¤®à¤¾ à¤›à¤¨à¥à¥¤"),
    BHAKOOT_CANCEL_SAME_LORD("Same lord ({lord}) rules both Moon signs - Full Cancellation", "à¤‰à¤¹à¥€ à¤¸à¥à¤µà¤¾à¤®à¥€ ({lord}) à¤²à¥‡ à¤¦à¥à¤µà¥ˆ à¤šà¤¨à¥à¤¦à¥à¤° à¤°à¤¾à¤¶à¤¿ à¤¶à¤¾à¤¸à¤¨ à¤—à¤°à¥à¤› - à¤ªà¥‚à¤°à¥à¤£ à¤°à¤¦à¥à¤¦"),
    BHAKOOT_CANCEL_MUTUAL_FRIENDS("Moon sign lords ({lord1} & {lord2}) are mutual friends - Full Cancellation", "à¤šà¤¨à¥à¤¦à¥à¤° à¤°à¤¾à¤¶à¤¿ à¤¸à¥à¤µà¤¾à¤®à¥€ ({lord1} à¤° {lord2}) à¤ªà¤¾à¤°à¤¸à¥à¤ªà¤°à¤¿à¤• à¤®à¤¿à¤¤à¥à¤° à¤¹à¥à¤¨à¥ - à¤ªà¥‚à¤°à¥à¤£ à¤°à¤¦à¥à¤¦"),
    BHAKOOT_CANCEL_EXALTATION("Lord is exalted in partner's sign - Partial Cancellation", "à¤¸à¥à¤µà¤¾à¤®à¥€ à¤¸à¤¾à¤¥à¥€à¤•à¥‹ à¤°à¤¾à¤¶à¤¿à¤®à¤¾ à¤‰à¤šà¥à¤š à¤› - à¤†à¤‚à¤¶à¤¿à¤• à¤°à¤¦à¥à¤¦"),
    BHAKOOT_CANCEL_FRIENDLY("Moon sign lords have friendly disposition - Partial Cancellation", "à¤šà¤¨à¥à¤¦à¥à¤° à¤°à¤¾à¤¶à¤¿ à¤¸à¥à¤µà¤¾à¤®à¥€à¤¹à¤°à¥‚à¤•à¥‹ à¤®à¥ˆà¤¤à¥à¤°à¥€à¤ªà¥‚à¤°à¥à¤£ à¤¸à¥à¤µà¤­à¤¾à¤µ à¤› - à¤†à¤‚à¤¶à¤¿à¤• à¤°à¤¦à¥à¤¦"),
    BHAKOOT_CANCEL_ELEMENT("Both Moon signs share same element ({element}) - Partial Cancellation", "à¤¦à¥à¤µà¥ˆ à¤šà¤¨à¥à¤¦à¥à¤° à¤°à¤¾à¤¶à¤¿à¤²à¥‡ à¤à¤‰à¤Ÿà¥ˆ à¤¤à¤¤à¥à¤µ ({element}) à¤¸à¤¾à¤à¤¾ à¤—à¤°à¥à¤›à¤¨à¥ - à¤†à¤‚à¤¶à¤¿à¤• à¤°à¤¦à¥à¤¦"),

    // Nadi Analysis
    NADI_DESC("Health and progeny compatibility (most important)", "à¤¸à¥à¤µà¤¾à¤¸à¥à¤¥à¥à¤¯ à¤° à¤¸à¤¨à¥à¤¤à¤¾à¤¨ à¤…à¤¨à¥à¤•à¥‚à¤²à¤¤à¤¾ (à¤¸à¤¬à¥ˆà¤­à¤¨à¥à¤¦à¤¾ à¤®à¤¹à¤¤à¥à¤¤à¥à¤µà¤ªà¥‚à¤°à¥à¤£)"),
    NADI_DOSHA_PRESENT("NADI DOSHA PRESENT: Same Nadi ({nadi}) without cancellation. Serious concern affecting health and progeny.", "à¤¨à¤¾à¤¡à¥€ à¤¦à¥‹à¤· à¤‰à¤ªà¤¸à¥à¤¥à¤¿à¤¤: à¤°à¤¦à¥à¤¦ à¤¬à¤¿à¤¨à¤¾ à¤‰à¤¹à¥€ à¤¨à¤¾à¤¡à¥€ ({nadi})à¥¤ à¤¸à¥à¤µà¤¾à¤¸à¥à¤¥à¥à¤¯ à¤° à¤¸à¤¨à¥à¤¤à¤¾à¤¨à¤²à¤¾à¤ˆ à¤…à¤¸à¤° à¤—à¤°à¥à¤¨à¥‡ à¤—à¤®à¥à¤­à¥€à¤° à¤šà¤¿à¤¨à¥à¤¤à¤¾à¥¤"),
    NADI_DOSHA_CANCELLED("Same Nadi but CANCELLED:", "à¤‰à¤¹à¥€ à¤¨à¤¾à¤¡à¥€ à¤¤à¤° à¤°à¤¦à¥à¤¦:"),
    NADI_DIFFERENT("Different Nadis ({nadi1} & {nadi2}) - excellent health and progeny compatibility.", "à¤«à¤°à¤• à¤¨à¤¾à¤¡à¥€ ({nadi1} à¤° {nadi2}) - à¤‰à¤¤à¥à¤•à¥ƒà¤·à¥à¤Ÿ à¤¸à¥à¤µà¤¾à¤¸à¥à¤¥à¥à¤¯ à¤° à¤¸à¤¨à¥à¤¤à¤¾à¤¨ à¤…à¤¨à¥à¤•à¥‚à¤²à¤¤à¤¾à¥¤"),
    NADI_CANCEL_SAME_NAK_DIFF_RASHI("Same Nakshatra ({nakshatra}) but different Rashis - Full Cancellation", "à¤‰à¤¹à¥€ à¤¨à¤•à¥à¤·à¤¤à¥à¤° ({nakshatra}) à¤¤à¤° à¤«à¤°à¤• à¤°à¤¾à¤¶à¤¿ - à¤ªà¥‚à¤°à¥à¤£ à¤°à¤¦à¥à¤¦"),
    NADI_CANCEL_SAME_RASHI_DIFF_NAK("Same Rashi ({rashi}) but different Nakshatras - Full Cancellation", "à¤‰à¤¹à¥€ à¤°à¤¾à¤¶à¤¿ ({rashi}) à¤¤à¤° à¤«à¤°à¤• à¤¨à¤•à¥à¤·à¤¤à¥à¤° - à¤ªà¥‚à¤°à¥à¤£ à¤°à¤¦à¥à¤¦"),
    NADI_CANCEL_DIFF_PADA("Same Nakshatra and Rashi but different Padas ({pada1} vs {pada2}) - Partial Cancellation", "à¤‰à¤¹à¥€ à¤¨à¤•à¥à¤·à¤¤à¥à¤° à¤° à¤°à¤¾à¤¶à¤¿ à¤¤à¤° à¤«à¤°à¤• à¤ªà¤¾à¤¦ ({pada1} à¤¬à¤¨à¤¾à¤® {pada2}) - à¤†à¤‚à¤¶à¤¿à¤• à¤°à¤¦à¥à¤¦"),
    NADI_CANCEL_SPECIAL_PAIR("Special Nakshatra pair ({nak1}-{nak2}) cancels Nadi dosha per classical texts", "à¤µà¤¿à¤¶à¥‡à¤· à¤¨à¤•à¥à¤·à¤¤à¥à¤° à¤œà¥‹à¤¡à¥€ ({nak1}-{nak2}) à¤²à¥‡ à¤¶à¤¾à¤¸à¥à¤¤à¥à¤°à¥€à¤¯ à¤—à¥à¤°à¤¨à¥à¤¥ à¤…à¤¨à¥à¤¸à¤¾à¤° à¤¨à¤¾à¤¡à¥€ à¤¦à¥‹à¤· à¤°à¤¦à¥à¤¦ à¤—à¤°à¥à¤›"),
    NADI_CANCEL_LORDS_FRIENDS("Moon sign lords ({lord1} & {lord2}) are mutual friends - Partial Cancellation", "à¤šà¤¨à¥à¤¦à¥à¤° à¤°à¤¾à¤¶à¤¿ à¤¸à¥à¤µà¤¾à¤®à¥€ ({lord1} à¤° {lord2}) à¤ªà¤¾à¤°à¤¸à¥à¤ªà¤°à¤¿à¤• à¤®à¤¿à¤¤à¥à¤° à¤¹à¥à¤¨à¥ - à¤†à¤‚à¤¶à¤¿à¤• à¤°à¤¦à¥à¤¦"),
    NADI_CANCEL_SAME_NAK_LORD("Both Nakshatras ruled by {lord} - Partial Cancellation", "à¤¦à¥à¤µà¥ˆ à¤¨à¤•à¥à¤·à¤¤à¥à¤° {lord} à¤¦à¥à¤µà¤¾à¤°à¤¾ à¤¶à¤¾à¤¸à¤¿à¤¤ - à¤†à¤‚à¤¶à¤¿à¤• à¤°à¤¦à¥à¤¦"),

    // ============================================
    // DASHA CALCULATOR - LEVELS
    // ============================================
    DASHA_LEVEL_MAHADASHA("Mahadasha", "à¤®à¤¹à¤¾à¤¦à¤¶à¤¾"),
    DASHA_LEVEL_ANTARDASHA("Antardasha", "à¤…à¤¨à¥à¤¤à¤°à¥à¤¦à¤¶à¤¾"),
    DASHA_LEVEL_PRATYANTARDASHA("Pratyantardasha", "à¤ªà¥à¤°à¤¤à¥à¤¯à¤¨à¥à¤¤à¤°à¥à¤¦à¤¶à¤¾"),
    DASHA_LEVEL_SOOKSHMADASHA("Sookshmadasha", "à¤¸à¥‚à¤•à¥à¤·à¥à¤®à¤¦à¤¶à¤¾"),
    DASHA_LEVEL_PRANADASHA("Pranadasha", "à¤ªà¥à¤°à¤¾à¤£à¤¦à¤¶à¤¾"),
    DASHA_LEVEL_DEHADASHA("Dehadasha", "à¤¦à¥‡à¤¹à¤¦à¤¶à¤¾"),

    // Dasha Tab Content Strings
    DASHA_CURRENT_DASHA_PERIOD("Current Dasha Period", "à¤¹à¤¾à¤²à¤•à¥‹ à¤¦à¤¶à¤¾ à¤…à¤µà¤§à¤¿"),
    DASHA_BIRTH_NAKSHATRA("Birth Nakshatra", "à¤œà¤¨à¥à¤® à¤¨à¤•à¥à¤·à¤¤à¥à¤°"),
    DASHA_LORD("Lord", "à¤¸à¥à¤µà¤¾à¤®à¥€"),
    DASHA_PADA("Pada", "à¤ªà¤¾à¤¦"),
    DASHA_PERIOD_INSIGHTS("Period Insights", "à¤…à¤µà¤§à¤¿ à¤…à¤¨à¥à¤¤à¤°à¥à¤¦à¥ƒà¤·à¥à¤Ÿà¤¿"),
    DASHA_SANDHI_ALERTS("Dasha Sandhi Alerts", "à¤¦à¤¶à¤¾ à¤¸à¤¨à¥à¤§à¤¿ à¤…à¤²à¤°à¥à¤Ÿà¤¹à¤°à¥‚"),
    DASHA_UPCOMING_TRANSITIONS("%1\$d upcoming transition(s) within %2\$d days", "%2\$d à¤¦à¤¿à¤¨à¤­à¤¿à¤¤à¥à¤° %1\$d à¤†à¤—à¤¾à¤®à¥€ à¤¸à¤¨à¥à¤•à¥à¤°à¤®à¤£(à¤¹à¤°à¥‚)"),
    DASHA_TRANSITION("transition", "à¤¸à¤¨à¥à¤•à¥à¤°à¤®à¤£"),
    DASHA_SANDHI_EXPLANATION("Sandhi periods mark transitions between planetary periods. These are sensitive times requiring careful attention as the energy shifts from one planet to another.", "à¤¸à¤¨à¥à¤§à¤¿ à¤…à¤µà¤§à¤¿à¤¹à¤°à¥‚à¤²à¥‡ à¤—à¥à¤°à¤¹ à¤…à¤µà¤§à¤¿à¤¹à¤°à¥‚à¤¬à¥€à¤šà¤•à¥‹ à¤¸à¤¨à¥à¤•à¥à¤°à¤®à¤£ à¤šà¤¿à¤¨à¥à¤¹ à¤²à¤—à¤¾à¤‰à¤à¤›à¤¨à¥à¥¤ à¤¯à¥€ à¤¸à¤‚à¤µà¥‡à¤¦à¤¨à¤¶à¥€à¤² à¤¸à¤®à¤¯à¤¹à¤°à¥‚ à¤¹à¥à¤¨à¥ à¤œà¤¹à¤¾à¤ à¤à¤‰à¤Ÿà¤¾ à¤—à¥à¤°à¤¹à¤¬à¤¾à¤Ÿ à¤…à¤°à¥à¤•à¥‹ à¤—à¥à¤°à¤¹à¤®à¤¾ à¤Šà¤°à¥à¤œà¤¾ à¤ªà¤°à¤¿à¤µà¤°à¥à¤¤à¤¨ à¤¹à¥à¤¨à¥à¤›à¥¤"),
    DASHA_ACTIVE_NOW("Active Now", "à¤…à¤¹à¤¿à¤²à¥‡ à¤¸à¤•à¥à¤°à¤¿à¤¯"),
    DASHA_TODAY("Today", "à¤†à¤œ"),
    DASHA_TOMORROW("Tomorrow", "à¤­à¥‹à¤²à¤¿"),
    DASHA_IN_DAYS("In %d days", "%d à¤¦à¤¿à¤¨à¤®à¤¾"),
    DASHA_TIMELINE("Dasha Timeline", "à¤¦à¤¶à¤¾ à¤¸à¤®à¤¯à¤°à¥‡à¤–à¤¾"),
    DASHA_COMPLETE_CYCLE("Complete 120-year Vimshottari cycle", "à¤ªà¥‚à¤°à¥à¤£ à¥§à¥¨à¥¦-à¤µà¤°à¥à¤·à¥‡ à¤µà¤¿à¤®à¥à¤¶à¥‹à¤¤à¥à¤¤à¤°à¥€ à¤šà¤•à¥à¤°"),
    DASHA_SUB_PERIODS("%d sub-periods", "%d à¤‰à¤ª-à¤…à¤µà¤§à¤¿à¤¹à¤°à¥‚"),
    DASHA_ANTARDASHAS("Antardashas", "à¤…à¤¨à¥à¤¤à¤°à¥à¤¦à¤¶à¤¾à¤¹à¤°à¥‚"),
    DASHA_UNABLE_CALCULATE("Unable to calculate current dasha period", "à¤¹à¤¾à¤²à¤•à¥‹ à¤¦à¤¶à¤¾ à¤…à¤µà¤§à¤¿ à¤—à¤£à¤¨à¤¾ à¤—à¤°à¥à¤¨ à¤…à¤¸à¤•à¥à¤·à¤®"),
    DASHA_ABOUT_VIMSHOTTARI("About Vimshottari Dasha", "à¤µà¤¿à¤®à¥à¤¶à¥‹à¤¤à¥à¤¤à¤°à¥€ à¤¦à¤¶à¤¾à¤•à¥‹ à¤¬à¤¾à¤°à¥‡à¤®à¤¾"),
    DASHA_VIMSHOTTARI_DESC("The Vimshottari Dasha is the most widely used planetary period system in Vedic astrology (Jyotish). Derived from the Moon's nakshatra (lunar mansion) at birth, it divides the 120-year human lifespan into six levels of planetary periods. Starting from Mahadashas (major periods spanning years), it subdivides into Antardasha (months), Pratyantardasha (weeks), Sookshmadasha (days), Pranadasha (hours), and finally Dehadasha (minutes) â€” each governed by one of the nine Grahas.", "à¤µà¤¿à¤®à¥à¤¶à¥‹à¤¤à¥à¤¤à¤°à¥€ à¤¦à¤¶à¤¾ à¤µà¥ˆà¤¦à¤¿à¤• à¤œà¥à¤¯à¥‹à¤¤à¤¿à¤· (à¤œà¥à¤¯à¥‹à¤¤à¤¿à¤·) à¤®à¤¾ à¤¸à¤¬à¥ˆà¤­à¤¨à¥à¤¦à¤¾ à¤µà¥à¤¯à¤¾à¤ªà¤• à¤°à¥‚à¤ªà¤®à¤¾ à¤ªà¥à¤°à¤¯à¥‹à¤— à¤¹à¥à¤¨à¥‡ à¤—à¥à¤°à¤¹ à¤…à¤µà¤§à¤¿ à¤ªà¥à¤°à¤£à¤¾à¤²à¥€ à¤¹à¥‹à¥¤ à¤œà¤¨à¥à¤®à¤•à¥‹ à¤¸à¤®à¤¯à¤®à¤¾ à¤šà¤¨à¥à¤¦à¥à¤°à¤®à¤¾à¤•à¥‹ à¤¨à¤•à¥à¤·à¤¤à¥à¤°à¤¬à¤¾à¤Ÿ à¤µà¥à¤¯à¥à¤¤à¥à¤ªà¤¨à¥à¤¨, à¤¯à¤¸à¤²à¥‡ à¥§à¥¨à¥¦-à¤µà¤°à¥à¤·à¥‡ à¤®à¤¾à¤¨à¤µ à¤†à¤¯à¥à¤²à¤¾à¤ˆ à¤› à¤¤à¤¹à¤•à¥‹ à¤—à¥à¤°à¤¹ à¤…à¤µà¤§à¤¿à¤¹à¤°à¥‚à¤®à¤¾ à¤µà¤¿à¤­à¤¾à¤œà¤¨ à¤—à¤°à¥à¤›à¥¤"),
    DASHA_PERIODS_SEQUENCE("Dasha Periods (Vimshottari Sequence)", "à¤¦à¤¶à¤¾ à¤…à¤µà¤§à¤¿à¤¹à¤°à¥‚ (à¤µà¤¿à¤®à¥à¤¶à¥‹à¤¤à¥à¤¤à¤°à¥€ à¤•à¥à¤°à¤®)"),
    DASHA_TOTAL_CYCLE("Total Cycle: 120 Years", "à¤•à¥à¤² à¤šà¤•à¥à¤°: à¥§à¥¨à¥¦ à¤µà¤°à¥à¤·"),
    DASHA_HIERARCHY("Dasha Hierarchy", "à¤¦à¤¶à¤¾ à¤ªà¤¦à¤¾à¤¨à¥à¤•à¥à¤°à¤®"),
    DASHA_MAJOR_PERIOD_YEARS("Major period (years)", "à¤®à¥à¤–à¥à¤¯ à¤…à¤µà¤§à¤¿ (à¤µà¤°à¥à¤·)"),
    DASHA_SUB_PERIOD_MONTHS("Sub-period (months)", "à¤‰à¤ª-à¤…à¤µà¤§à¤¿ (à¤®à¤¹à¤¿à¤¨à¤¾)"),
    DASHA_SUB_SUB_PERIOD_WEEKS("Sub-sub-period (weeks)", "à¤‰à¤ª-à¤‰à¤ª-à¤…à¤µà¤§à¤¿ (à¤¹à¤ªà¥à¤¤à¤¾)"),
    DASHA_SUBTLE_PERIOD_DAYS("Subtle period (days)", "à¤¸à¥‚à¤•à¥à¤·à¥à¤® à¤…à¤µà¤§à¤¿ (à¤¦à¤¿à¤¨)"),
    DASHA_BREATH_PERIOD_HOURS("Breath period (hours)", "à¤¶à¥à¤µà¤¾à¤¸ à¤…à¤µà¤§à¤¿ (à¤˜à¤£à¥à¤Ÿà¤¾)"),
    DASHA_BODY_PERIOD_MINUTES("Body period (minutes)", "à¤¶à¤°à¥€à¤° à¤…à¤µà¤§à¤¿ (à¤®à¤¿à¤¨à¥‡à¤Ÿ)"),
    DASHA_SANDHI_NOTE("Dasha Sandhi (junction periods) occur when transitioning between planetary periods and are considered sensitive times requiring careful attention.", "à¤¦à¤¶à¤¾ à¤¸à¤¨à¥à¤§à¤¿ (à¤œà¥‹à¤¡ à¤…à¤µà¤§à¤¿à¤¹à¤°à¥‚) à¤—à¥à¤°à¤¹ à¤…à¤µà¤§à¤¿à¤¹à¤°à¥‚à¤¬à¥€à¤š à¤¸à¤¨à¥à¤•à¥à¤°à¤®à¤£ à¤¹à¥à¤à¤¦à¤¾ à¤¹à¥à¤¨à¥à¤› à¤° à¤¯à¥€ à¤¸à¤‚à¤µà¥‡à¤¦à¤¨à¤¶à¥€à¤² à¤¸à¤®à¤¯à¤¹à¤°à¥‚ à¤®à¤¾à¤¨à¤¿à¤¨à¥à¤›à¤¨à¥à¥¤"),
    DASHA_PERCENT_COMPLETE("%s%% complete", "%s%% à¤ªà¥‚à¤°à¤¾"),
    DASHA_YEARS_ABBR("yrs", "à¤µà¤°à¥à¤·"),
    DASHA_COLLAPSE("Collapse", "à¤¸à¤‚à¤•à¥à¤šà¤¨ à¤—à¤°à¥à¤¨à¥à¤¹à¥‹à¤¸à¥"),
    DASHA_EXPAND("Expand", "à¤µà¤¿à¤¸à¥à¤¤à¤¾à¤° à¤—à¤°à¥à¤¨à¥à¤¹à¥‹à¤¸à¥"),

    // ============================================
    // ASHTAKAVARGA CALCULATOR - STRENGTH LEVELS
    // ============================================
    STRENGTH_STRONG("Strong", "à¤¬à¤²à¤¿à¤¯à¥‹"),
    STRENGTH_GOOD("Good", "à¤°à¤¾à¤®à¥à¤°à¥‹"),
    STRENGTH_AVERAGE("Average", "à¤”à¤¸à¤¤"),
    STRENGTH_WEAK("Weak", "à¤•à¤®à¤œà¥‹à¤°"),
    STRENGTH_EXCELLENT("Excellent", "à¤‰à¤¤à¥à¤•à¥ƒà¤·à¥à¤Ÿ"),
    STRENGTH_CHALLENGING("Challenging", "à¤šà¥à¤¨à¥Œà¤¤à¥€à¤ªà¥‚à¤°à¥à¤£"),
    STRENGTH_DIFFICULT("Difficult", "à¤•à¤ à¤¿à¤¨"),
    STRENGTH_ERROR("Error", "à¤¤à¥à¤°à¥à¤Ÿà¤¿"),
    STRENGTH_ASCENDANT("Ascendant", "à¤²à¤—à¥à¤¨"),

    // ============================================
    // ASPECT CALCULATOR - TYPES
    // ============================================
    ASPECT_CONJUNCTION("Conjunction", "à¤¯à¥à¤¤à¤¿"),
    ASPECT_NATURE_HARMONIOUS("Harmonious", "à¤¸à¤¾à¤®à¤žà¥à¤œà¤¸à¥à¤¯à¤ªà¥‚à¤°à¥à¤£"),
    ASPECT_NATURE_CHALLENGING("Challenging", "à¤šà¥à¤¨à¥Œà¤¤à¥€à¤ªà¥‚à¤°à¥à¤£"),
    ASPECT_NATURE_VARIABLE("Variable", "à¤ªà¤°à¤¿à¤µà¤°à¥à¤¤à¤¨à¤¶à¥€à¤²"),
    ASPECT_NATURE_SIGNIFICANT("Significant", "à¤®à¤¹à¤¤à¥à¤¤à¥à¤µà¤ªà¥‚à¤°à¥à¤£"),

    // ============================================
    // COLORS (FOR HOROSCOPE)
    // ============================================
    COLOR_RED("Red", "à¤°à¤¾à¤¤à¥‹"),
    COLOR_ORANGE("Orange", "à¤¸à¥à¤¨à¥à¤¤à¤²à¤¾"),
    COLOR_GOLD("Gold", "à¤¸à¥à¤¨à¥Œà¤²à¥‹"),
    COLOR_GREEN("Green", "à¤¹à¤°à¤¿à¤¯à¥‹"),
    COLOR_BROWN("Brown", "à¤–à¥ˆà¤°à¥‹"),
    COLOR_WHITE("White", "à¤¸à¥‡à¤¤à¥‹"),
    COLOR_BLUE("Blue", "à¤¨à¥€à¤²à¥‹"),
    COLOR_LIGHT_BLUE("Light Blue", "à¤¹à¤²à¥à¤•à¤¾ à¤¨à¥€à¤²à¥‹"),
    COLOR_SILVER("Silver", "à¤šà¤¾à¤à¤¦à¥€"),
    COLOR_SEA_GREEN("Sea Green", "à¤¸à¤®à¥à¤¦à¥à¤°à¥€ à¤¹à¤°à¤¿à¤¯à¥‹"),
    COLOR_YELLOW("Yellow", "à¤ªà¤¹à¥‡à¤à¤²à¥‹"),
    COLOR_PINK("Pink", "à¤—à¥à¤²à¤¾à¤¬à¥€"),
    COLOR_PURPLE("Purple", "à¤¬à¥ˆà¤œà¤¨à¥€"),
    COLOR_BLACK("Black", "à¤•à¤¾à¤²à¥‹"),
    COLOR_GREY("Grey", "à¤–à¤°à¤¾à¤¨à¥€"),
    COLOR_CREAM("Cream", "à¤•à¥à¤°à¥€à¤®"),
    COLOR_MAROON("Maroon", "à¤®à¤°à¥à¤¨"),
    COLOR_INDIGO("Indigo", "à¤‡à¤¨à¥à¤¡à¤¿à¤—à¥‹"),

    // ============================================
    // DIRECTIONS
    // ============================================
    DIR_EAST("East", "à¤ªà¥‚à¤°à¥à¤µ"),
    DIR_WEST("West", "à¤ªà¤¶à¥à¤šà¤¿à¤®"),
    DIR_NORTH("North", "à¤‰à¤¤à¥à¤¤à¤°"),
    DIR_SOUTH("South", "à¤¦à¤•à¥à¤·à¤¿à¤£"),
    DIR_NORTH_EAST("North-East", "à¤‰à¤¤à¥à¤¤à¤°-à¤ªà¥‚à¤°à¥à¤µ"),
    DIR_NORTH_WEST("North-West", "à¤‰à¤¤à¥à¤¤à¤°-à¤ªà¤¶à¥à¤šà¤¿à¤®"),
    DIR_SOUTH_EAST("South-East", "à¤¦à¤•à¥à¤·à¤¿à¤£-à¤ªà¥‚à¤°à¥à¤µ"),
    DIR_SOUTH_WEST("South-West", "à¤¦à¤•à¥à¤·à¤¿à¤£-à¤ªà¤¶à¥à¤šà¤¿à¤®"),

    // ============================================
    // ELEMENTS
    // ============================================
    ELEMENT_FIRE("Fire", "à¤…à¤—à¥à¤¨à¤¿"),
    ELEMENT_EARTH("Earth", "à¤ªà¥ƒà¤¥à¥à¤µà¥€"),
    ELEMENT_AIR("Air", "à¤µà¤¾à¤¯à¥"),
    ELEMENT_WATER("Water", "à¤œà¤²"),

    // ============================================
    // PLANETARY RELATIONSHIPS
    // ============================================
    RELATION_FRIEND("Friend", "à¤®à¤¿à¤¤à¥à¤°"),
    RELATION_ENEMY("Enemy", "à¤¶à¤¤à¥à¤°à¥"),
    RELATION_NEUTRAL("Neutral", "à¤¤à¤Ÿà¤¸à¥à¤¥"),
    RELATION_MUTUAL_FRIENDS("Mutual Friends", "à¤ªà¤¾à¤°à¤¸à¥à¤ªà¤°à¤¿à¤• à¤®à¤¿à¤¤à¥à¤°"),
    RELATION_MUTUAL_ENEMIES("Mutual Enemies", "à¤ªà¤¾à¤°à¤¸à¥à¤ªà¤°à¤¿à¤• à¤¶à¤¤à¥à¤°à¥"),
    RELATION_ONE_FRIENDLY("One Friendly", "à¤à¤• à¤®à¥ˆà¤¤à¥à¤°à¥€à¤ªà¥‚à¤°à¥à¤£"),
    RELATION_ONE_INIMICAL("One Inimical", "à¤à¤• à¤¶à¤¤à¥à¤°à¥à¤¤à¤¾à¤ªà¥‚à¤°à¥à¤£"),

    // ============================================
    // MUHURTA - EVENT TYPES
    // ============================================
    MUHURTA_EVENT_MARRIAGE("Marriage", "à¤µà¤¿à¤µà¤¾à¤¹"),
    MUHURTA_EVENT_ENGAGEMENT("Engagement", "à¤¸à¤—à¤¾à¤ˆ"),
    MUHURTA_EVENT_GRIHA_PRAVESH("Griha Pravesh", "à¤—à¥ƒà¤¹ à¤ªà¥à¤°à¤µà¥‡à¤¶"),
    MUHURTA_EVENT_BUSINESS_START("Business Start", "à¤µà¥à¤¯à¤¾à¤ªà¤¾à¤° à¤¶à¥à¤°à¥à¤†à¤¤"),
    MUHURTA_EVENT_TRAVEL("Travel", "à¤¯à¤¾à¤¤à¥à¤°à¤¾"),
    MUHURTA_EVENT_VEHICLE_PURCHASE("Vehicle Purchase", "à¤¸à¤µà¤¾à¤°à¥€ à¤–à¤°à¤¿à¤¦"),
    MUHURTA_EVENT_EDUCATION_START("Education Start", "à¤¶à¤¿à¤•à¥à¤·à¤¾ à¤¶à¥à¤°à¥à¤†à¤¤"),
    MUHURTA_EVENT_MEDICAL_TREATMENT("Medical Treatment", "à¤šà¤¿à¤•à¤¿à¤¤à¥à¤¸à¤¾ à¤‰à¤ªà¤šà¤¾à¤°"),
    MUHURTA_EVENT_CONSTRUCTION("Construction", "à¤¨à¤¿à¤°à¥à¤®à¤¾à¤£"),
    MUHURTA_EVENT_INVESTMENT("Investment", "à¤²à¤—à¤¾à¤¨à¥€"),
    MUHURTA_EVENT_NAME_CEREMONY("Name Ceremony", "à¤¨à¤¾à¤®à¤•à¤°à¤£"),
    MUHURTA_EVENT_MUNDAN("Mundan", "à¤šà¥Œà¤²à¤•à¤°à¥à¤®"),
    MUHURTA_EVENT_UPANAYANA("Upanayana", "à¤¬à¥à¤°à¤¤à¤¬à¤¨à¥à¤§"),

    // ============================================
    // VARSHAPHALA - SAHAMS
    // ============================================
    SAHAM_KARYASIDDHI("Karyasiddhi Saham", "à¤•à¤¾à¤°à¥à¤¯à¤¸à¤¿à¤¦à¥à¤§à¤¿ à¤¸à¤¹à¤®"),
    SAHAM_SUCCESS("Success", "à¤¸à¤«à¤²à¤¤à¤¾"),
    SAHAM_FORTUNE("Fortune Saham", "à¤­à¤¾à¤—à¥à¤¯ à¤¸à¤¹à¤®"),
    SAHAM_WEALTH("Wealth Saham", "à¤§à¤¨ à¤¸à¤¹à¤®"),
    SAHAM_MARRIAGE("Marriage Saham", "à¤µà¤¿à¤µà¤¾à¤¹ à¤¸à¤¹à¤®"),
    SAHAM_CHILDREN("Children Saham", "à¤¸à¤¨à¥à¤¤à¤¾à¤¨ à¤¸à¤¹à¤®"),
    SAHAM_HEALTH("Health Saham", "à¤¸à¥à¤µà¤¾à¤¸à¥à¤¥à¥à¤¯ à¤¸à¤¹à¤®"),
    SAHAM_EDUCATION("Education Saham", "à¤¶à¤¿à¤•à¥à¤·à¤¾ à¤¸à¤¹à¤®"),
    SAHAM_TRAVEL("Travel Saham", "à¤¯à¤¾à¤¤à¥à¤°à¤¾ à¤¸à¤¹à¤®"),
    SAHAM_PROFESSION("Profession Saham", "à¤ªà¥‡à¤¶à¤¾ à¤¸à¤¹à¤®"),

    // ============================================
    // HOUSE SIGNIFICATIONS
    // ============================================
    HOUSE_DHARMA("Dharma (1, 5, 9)", "à¤§à¤°à¥à¤® (à¥§, à¥«, à¥¯)"),
    HOUSE_ARTHA("Artha (2, 6, 10)", "à¤…à¤°à¥à¤¥ (à¥¨, à¥¬, à¥§à¥¦)"),
    HOUSE_KAMA("Kama (3, 7, 11)", "à¤•à¤¾à¤® (à¥©, à¥­, à¥§à¥§)"),
    HOUSE_MOKSHA("Moksha (4, 8, 12)", "à¤®à¥‹à¤•à¥à¤· (à¥ª, à¥®, à¥§à¥¨)"),

    // ============================================
    // PRASHNA CALCULATOR
    // ============================================
    PRASHNA_CAT_GENERAL("General", "à¤¸à¤¾à¤®à¤¾à¤¨à¥à¤¯"),
    PRASHNA_CAT_CAREER("Career & Profession", "à¤•à¥à¤¯à¤¾à¤°à¤¿à¤¯à¤° à¤° à¤ªà¥‡à¤¶à¤¾"),
    PRASHNA_CAT_RELATIONSHIP("Relationships", "à¤¸à¤®à¥à¤¬à¤¨à¥à¤§à¤¹à¤°à¥‚"),
    PRASHNA_CAT_HEALTH("Health", "à¤¸à¥à¤µà¤¾à¤¸à¥à¤¥à¥à¤¯"),
    PRASHNA_CAT_FINANCE("Finance", "à¤µà¤¿à¤¤à¥à¤¤"),
    PRASHNA_CAT_TRAVEL("Travel", "à¤¯à¤¾à¤¤à¥à¤°à¤¾"),
    PRASHNA_CAT_EDUCATION("Education", "à¤¶à¤¿à¤•à¥à¤·à¤¾"),
    PRASHNA_CAT_LEGAL("Legal Matters", "à¤•à¤¾à¤¨à¥à¤¨à¥€ à¤®à¤¾à¤®à¤¿à¤²à¤¾à¤¹à¤°à¥‚"),
    PRASHNA_CAT_PROPERTY("Property", "à¤¸à¤®à¥à¤ªà¤¤à¥à¤¤à¤¿"),
    PRASHNA_CAT_LOST_ITEM("Lost Items", "à¤¹à¤°à¤¾à¤à¤•à¤¾ à¤µà¤¸à¥à¤¤à¥à¤¹à¤°à¥‚"),

    // ============================================
    // RAJJU ARUDHA
    // ============================================
    RAJJU_ASCENDING("Aarohana (Ascending)", "à¤†à¤°à¥‹à¤¹à¤£"),
    RAJJU_DESCENDING("Avarohana (Descending)", "à¤…à¤µà¤°à¥‹à¤¹à¤£"),

    // ============================================
    // MATCHMAKING - ADDITIONAL FACTORS
    // ============================================
    MAHENDRA_FAVORABLE("Mahendra favorable at position {count} - promotes longevity and progeny", "à¤®à¤¹à¥‡à¤¨à¥à¤¦à¥à¤° à¤¸à¥à¤¥à¤¿à¤¤à¤¿ {count} à¤®à¤¾ à¤…à¤¨à¥à¤•à¥‚à¤² - à¤¦à¥€à¤°à¥à¤˜à¤¾à¤¯à¥ à¤° à¤¸à¤¨à¥à¤¤à¤¾à¤¨ à¤ªà¥à¤°à¤µà¤°à¥à¤§à¤¨ à¤—à¤°à¥à¤›"),
    MAHENDRA_NOT_APPLICABLE("Mahendra position not in favorable sequence", "à¤®à¤¹à¥‡à¤¨à¥à¤¦à¥à¤° à¤¸à¥à¤¥à¤¿à¤¤à¤¿ à¤…à¤¨à¥à¤•à¥‚à¤² à¤•à¥à¤°à¤®à¤®à¤¾ à¤›à¥ˆà¤¨"),
    VEDHA_PRESENT("Vedha (obstruction) present between {nak1} and {nak2} - may cause obstacles", "à¤µà¥‡à¤§ (à¤¬à¤¾à¤§à¤¾) {nak1} à¤° {nak2} à¤¬à¥€à¤š à¤‰à¤ªà¤¸à¥à¤¥à¤¿à¤¤ - à¤…à¤µà¤°à¥‹à¤§à¤¹à¤°à¥‚ à¤¹à¥à¤¨ à¤¸à¤•à¥à¤›"),
    VEDHA_NOT_PRESENT("No Vedha between the nakshatras - favorable", "à¤¨à¤•à¥à¤·à¤¤à¥à¤°à¤¹à¤°à¥‚ à¤¬à¥€à¤š à¤µà¥‡à¤§ à¤›à¥ˆà¤¨ - à¤…à¤¨à¥à¤•à¥‚à¤²"),
    RAJJU_COMPATIBLE("Different Rajju types - compatible, no concerns related to body part compatibility", "à¤«à¤°à¤• à¤°à¤œà¥à¤œà¥ à¤ªà¥à¤°à¤•à¤¾à¤° - à¤…à¤¨à¥à¤•à¥‚à¤², à¤¶à¤°à¥€à¤° à¤­à¤¾à¤— à¤…à¤¨à¥à¤•à¥‚à¤²à¤¤à¤¾à¤¸à¤®à¥à¤¬à¤¨à¥à¤§à¥€ à¤•à¥à¤¨à¥ˆ à¤šà¤¿à¤¨à¥à¤¤à¤¾ à¤›à¥ˆà¤¨"),
    RAJJU_SAME_DIFF_ARUDHA("Same {rajju} but different Arudha ({arudha1} vs {arudha2}) - partially compatible, reduced concern", "à¤‰à¤¹à¥€ {rajju} à¤¤à¤° à¤«à¤°à¤• à¤…à¤°à¥à¤¢à¤¾ ({arudha1} à¤¬à¤¨à¤¾à¤® {arudha2}) - à¤†à¤‚à¤¶à¤¿à¤• à¤…à¤¨à¥à¤•à¥‚à¤², à¤•à¤® à¤šà¤¿à¤¨à¥à¤¤à¤¾"),
    RAJJU_SAME_SAME_ARUDHA("Same {rajju} ({body}) and same {arudha} - Rajju Dosha present. {warning}", "à¤‰à¤¹à¥€ {rajju} ({body}) à¤° à¤‰à¤¹à¥€ {arudha} - à¤°à¤œà¥à¤œà¥ à¤¦à¥‹à¤· à¤‰à¤ªà¤¸à¥à¤¥à¤¿à¤¤à¥¤ {warning}"),

    // ============================================
    // MATCHMAKING - SPECIAL CONSIDERATIONS
    // ============================================
    SPECIAL_NADI_DOSHA("NADI DOSHA: Same Nadi can affect health and progeny. Consider remedies before proceeding.", "à¤¨à¤¾à¤¡à¥€ à¤¦à¥‹à¤·: à¤‰à¤¹à¥€ à¤¨à¤¾à¤¡à¥€à¤²à¥‡ à¤¸à¥à¤µà¤¾à¤¸à¥à¤¥à¥à¤¯ à¤° à¤¸à¤¨à¥à¤¤à¤¾à¤¨à¤®à¤¾ à¤…à¤¸à¤° à¤ªà¤¾à¤°à¥à¤¨ à¤¸à¤•à¥à¤›à¥¤ à¤…à¤—à¤¾à¤¡à¤¿ à¤¬à¤¢à¥à¤¨à¥ à¤…à¤˜à¤¿ à¤‰à¤ªà¤¾à¤¯à¤¹à¤°à¥‚ à¤µà¤¿à¤šà¤¾à¤° à¤—à¤°à¥à¤¨à¥à¤¹à¥‹à¤¸à¥à¥¤"),
    SPECIAL_BHAKOOT_DOSHA("BHAKOOT DOSHA present: {analysis}. May affect love, finances, or health.", "à¤­à¤•à¥à¤Ÿ à¤¦à¥‹à¤· à¤‰à¤ªà¤¸à¥à¤¥à¤¿à¤¤: {analysis}à¥¤ à¤ªà¥à¤°à¥‡à¤®, à¤µà¤¿à¤¤à¥à¤¤, à¤µà¤¾ à¤¸à¥à¤µà¤¾à¤¸à¥à¤¥à¥à¤¯à¤®à¤¾ à¤…à¤¸à¤° à¤ªà¤°à¥à¤¨ à¤¸à¤•à¥à¤›à¥¤"),
    SPECIAL_BRIDE_MANGLIK("MANGLIK IMBALANCE: Bride has {dosha} while Groom is non-Manglik. Kumbh Vivah or remedies strongly advised.", "à¤®à¤¾à¤‚à¤—à¤²à¤¿à¤• à¤…à¤¸à¤¨à¥à¤¤à¥à¤²à¤¨: à¤¦à¥à¤²à¤¹à¥€à¤®à¤¾ {dosha} à¤› à¤œà¤¬à¤•à¤¿ à¤¦à¥à¤²à¤¾à¤¹à¤¾ à¤—à¥ˆà¤°-à¤®à¤¾à¤‚à¤—à¤²à¤¿à¤• à¤›à¤¨à¥à¥¤ à¤•à¥à¤®à¥à¤­ à¤µà¤¿à¤µà¤¾à¤¹ à¤µà¤¾ à¤‰à¤ªà¤¾à¤¯à¤¹à¤°à¥‚ à¤¦à¥ƒà¤¢à¤¤à¤¾à¤ªà¥‚à¤°à¥à¤µà¤• à¤¸à¤²à¥à¤²à¤¾à¤¹ à¤¦à¤¿à¤‡à¤à¤•à¥‹à¥¤"),
    SPECIAL_GROOM_MANGLIK("MANGLIK IMBALANCE: Groom has {dosha} while Bride is non-Manglik. Remedies strongly recommended.", "à¤®à¤¾à¤‚à¤—à¤²à¤¿à¤• à¤…à¤¸à¤¨à¥à¤¤à¥à¤²à¤¨: à¤¦à¥à¤²à¤¾à¤¹à¤¾à¤®à¤¾ {dosha} à¤› à¤œà¤¬à¤•à¤¿ à¤¦à¥à¤²à¤¹à¥€ à¤—à¥ˆà¤°-à¤®à¤¾à¤‚à¤—à¤²à¤¿à¤• à¤›à¤¿à¤¨à¥à¥¤ à¤‰à¤ªà¤¾à¤¯à¤¹à¤°à¥‚ à¤¦à¥ƒà¤¢à¤¤à¤¾à¤ªà¥‚à¤°à¥à¤µà¤• à¤¸à¤¿à¤«à¤¾à¤°à¤¿à¤¸ à¤—à¤°à¤¿à¤à¤•à¥‹à¥¤"),
    SPECIAL_GANA_INCOMPAT("GANA INCOMPATIBILITY: Opposite temperaments (Deva-Rakshasa). May cause frequent conflicts without conscious effort.", "à¤—à¤£ à¤…à¤¸à¤‚à¤—à¤¤à¤¤à¤¾: à¤µà¤¿à¤ªà¤°à¥€à¤¤ à¤¸à¥à¤µà¤­à¤¾à¤µ (à¤¦à¥‡à¤µ-à¤°à¤¾à¤•à¥à¤·à¤¸)à¥¤ à¤¸à¤šà¥‡à¤¤ à¤ªà¥à¤°à¤¯à¤¾à¤¸ à¤¬à¤¿à¤¨à¤¾ à¤¬à¤¾à¤°à¤®à¥à¤¬à¤¾à¤° à¤¦à¥à¤µà¤¨à¥à¤¦à¥à¤µ à¤¹à¥à¤¨ à¤¸à¤•à¥à¤›à¥¤"),
    SPECIAL_YONI_INCOMPAT("YONI INCOMPATIBILITY: Enemy Yonis present. Physical and instinctual harmony may require extra effort.", "à¤¯à¥‹à¤¨à¤¿ à¤…à¤¸à¤‚à¤—à¤¤à¤¤à¤¾: à¤¶à¤¤à¥à¤°à¥ à¤¯à¥‹à¤¨à¤¿ à¤‰à¤ªà¤¸à¥à¤¥à¤¿à¤¤à¥¤ à¤¶à¤¾à¤°à¥€à¤°à¤¿à¤• à¤° à¤¸à¤¹à¤œ à¤¸à¤¾à¤®à¤žà¥à¤œà¤¸à¥à¤¯à¤•à¥‹ à¤²à¤¾à¤—à¤¿ à¤…à¤¤à¤¿à¤°à¤¿à¤•à¥à¤¤ à¤ªà¥à¤°à¤¯à¤¾à¤¸ à¤†à¤µà¤¶à¥à¤¯à¤• à¤ªà¤°à¥à¤¨ à¤¸à¤•à¥à¤›à¥¤"),
    SPECIAL_VEDHA("VEDHA PRESENT: {details}. Nakshatra obstruction may cause challenges in specific life areas.", "à¤µà¥‡à¤§ à¤‰à¤ªà¤¸à¥à¤¥à¤¿à¤¤: {details}à¥¤ à¤¨à¤•à¥à¤·à¤¤à¥à¤° à¤¬à¤¾à¤§à¤¾à¤²à¥‡ à¤œà¥€à¤µà¤¨à¤•à¤¾ à¤µà¤¿à¤¶à¥‡à¤· à¤•à¥à¤·à¥‡à¤¤à¥à¤°à¤¹à¤°à¥‚à¤®à¤¾ à¤šà¥à¤¨à¥Œà¤¤à¥€à¤¹à¤°à¥‚ à¤²à¥à¤¯à¤¾à¤‰à¤¨ à¤¸à¤•à¥à¤›à¥¤"),
    SPECIAL_RAJJU("RAJJU DOSHA: {details}. Related body part may face health concerns in marriage.", "à¤°à¤œà¥à¤œà¥ à¤¦à¥‹à¤·: {details}à¥¤ à¤¸à¤®à¥à¤¬à¤¨à¥à¤§à¤¿à¤¤ à¤¶à¤°à¥€à¤° à¤­à¤¾à¤—à¤®à¤¾ à¤µà¤¿à¤µà¤¾à¤¹à¤®à¤¾ à¤¸à¥à¤µà¤¾à¤¸à¥à¤¥à¥à¤¯ à¤šà¤¿à¤¨à¥à¤¤à¤¾ à¤¹à¥à¤¨ à¤¸à¤•à¥à¤›à¥¤"),
    SPECIAL_STREE_DEERGHA("STREE DEERGHA not satisfied: Nakshatra difference is {diff} (requires 13+). Bride's prosperity may need attention.", "à¤¸à¥à¤¤à¥à¤°à¥€ à¤¦à¥€à¤°à¥à¤˜ à¤¸à¤¨à¥à¤¤à¥à¤·à¥à¤Ÿ à¤›à¥ˆà¤¨: à¤¨à¤•à¥à¤·à¤¤à¥à¤° à¤­à¤¿à¤¨à¥à¤¨à¤¤à¤¾ {diff} à¤› (à¥§à¥©+ à¤†à¤µà¤¶à¥à¤¯à¤•)à¥¤ à¤¦à¥à¤²à¤¹à¥€à¤•à¥‹ à¤¸à¤®à¥ƒà¤¦à¥à¤§à¤¿à¤®à¤¾ à¤§à¥à¤¯à¤¾à¤¨ à¤¦à¤¿à¤¨à¥à¤ªà¤°à¥à¤¨à¥‡ à¤¹à¥à¤¨ à¤¸à¤•à¥à¤›à¥¤"),
    SPECIAL_MULTIPLE_LOW("MULTIPLE CONCERNS: {count} Gunas scored below threshold. Overall compatibility requires attention.", "à¤¬à¤¹à¥ à¤šà¤¿à¤¨à¥à¤¤à¤¾à¤¹à¤°à¥‚: {count} à¤—à¥à¤£à¤²à¥‡ à¤¨à¥à¤¯à¥‚à¤¨à¤¤à¤® à¤­à¤¨à¥à¤¦à¤¾ à¤•à¤® à¤…à¤‚à¤• à¤ªà¤¾à¤à¥¤ à¤¸à¤®à¤—à¥à¤° à¤…à¤¨à¥à¤•à¥‚à¤²à¤¤à¤¾à¤®à¤¾ à¤§à¥à¤¯à¤¾à¤¨ à¤†à¤µà¤¶à¥à¤¯à¤•à¥¤"),
    SPECIAL_7TH_LORDS_ENEMY("7TH HOUSE LORDS INCOMPATIBLE: {lord1} and {lord2} are mutual enemies. Marriage house lords in conflict.", "à¥­à¤”à¤‚ à¤­à¤¾à¤µ à¤¸à¥à¤µà¤¾à¤®à¥€ à¤…à¤¸à¤‚à¤—à¤¤: {lord1} à¤° {lord2} à¤ªà¤¾à¤°à¤¸à¥à¤ªà¤°à¤¿à¤• à¤¶à¤¤à¥à¤°à¥ à¤¹à¥à¤¨à¥à¥¤ à¤µà¤¿à¤µà¤¾à¤¹ à¤­à¤¾à¤µ à¤¸à¥à¤µà¤¾à¤®à¥€à¤¹à¤°à¥‚ à¤¦à¥à¤µà¤¨à¥à¤¦à¥à¤µà¤®à¤¾à¥¤"),
    SPECIAL_NO_ISSUES("No significant special concerns noted. The match appears harmonious across additional factors.", "à¤•à¥à¤¨à¥ˆ à¤®à¤¹à¤¤à¥à¤¤à¥à¤µà¤ªà¥‚à¤°à¥à¤£ à¤µà¤¿à¤¶à¥‡à¤· à¤šà¤¿à¤¨à¥à¤¤à¤¾ à¤›à¥ˆà¤¨à¥¤ à¤®à¤¿à¤²à¤¾à¤¨ à¤…à¤¤à¤¿à¤°à¤¿à¤•à¥à¤¤ à¤•à¤¾à¤°à¤•à¤¹à¤°à¥‚à¤®à¤¾ à¤¸à¤¾à¤®à¤žà¥à¤œà¤¸à¥à¤¯à¤ªà¥‚à¤°à¥à¤£ à¤¦à¥‡à¤–à¤¿à¤¨à¥à¤›à¥¤"),

    // ============================================
    // MATCHMAKING - REMEDIES
    // ============================================
    REMEDY_NADI_1("Nadi Dosha: Donate grains, gold, or cow on an auspicious day after consulting a priest", "à¤¨à¤¾à¤¡à¥€ à¤¦à¥‹à¤·: à¤ªà¥à¤œà¤¾à¤°à¥€à¤¸à¤à¤— à¤ªà¤°à¤¾à¤®à¤°à¥à¤¶ à¤ªà¤›à¤¿ à¤¶à¥à¤­ à¤¦à¤¿à¤¨à¤®à¤¾ à¤…à¤¨à¥à¤¨, à¤¸à¥à¤¨, à¤µà¤¾ à¤—à¤¾à¤ˆ à¤¦à¤¾à¤¨ à¤—à¤°à¥à¤¨à¥à¤¹à¥‹à¤¸à¥"),
    REMEDY_NADI_2("Nadi Dosha: Perform Maha Mrityunjaya Jaap (108 times daily for 40 days)", "à¤¨à¤¾à¤¡à¥€ à¤¦à¥‹à¤·: à¤®à¤¹à¤¾ à¤®à¥ƒà¤¤à¥à¤¯à¥à¤žà¥à¤œà¤¯ à¤œà¤¾à¤ª à¤—à¤°à¥à¤¨à¥à¤¹à¥‹à¤¸à¥ (à¥ªà¥¦ à¤¦à¤¿à¤¨ à¤¦à¥ˆà¤¨à¤¿à¤• à¥§à¥¦à¥® à¤ªà¤Ÿà¤•)"),
    REMEDY_NADI_3("Nadi Dosha: Worship Lord Shiva and Goddess Parvati together on Mondays", "à¤¨à¤¾à¤¡à¥€ à¤¦à¥‹à¤·: à¤¸à¥‹à¤®à¤¬à¤¾à¤°à¤®à¤¾ à¤­à¤—à¤µà¤¾à¤¨ à¤¶à¤¿à¤µ à¤° à¤¦à¥‡à¤µà¥€ à¤ªà¤¾à¤°à¥à¤µà¤¤à¥€à¤•à¥‹ à¤¸à¤à¤—à¥ˆ à¤ªà¥‚à¤œà¤¾ à¤—à¤°à¥à¤¨à¥à¤¹à¥‹à¤¸à¥"),
    REMEDY_BHAKOOT_1("Bhakoot Dosha: Perform Graha Shanti puja for Moon sign lords of both partners", "à¤­à¤•à¥à¤Ÿ à¤¦à¥‹à¤·: à¤¦à¥à¤µà¥ˆ à¤¸à¤¾à¤à¥‡à¤¦à¤¾à¤°à¤•à¥‹ à¤šà¤¨à¥à¤¦à¥à¤° à¤°à¤¾à¤¶à¤¿ à¤¸à¥à¤µà¤¾à¤®à¥€à¤•à¥‹ à¤—à¥à¤°à¤¹ à¤¶à¤¾à¤¨à¥à¤¤à¤¿ à¤ªà¥‚à¤œà¤¾ à¤—à¤°à¥à¤¨à¥à¤¹à¥‹à¤¸à¥"),
    REMEDY_BHAKOOT_2("Bhakoot Dosha: Chant Vishnu Sahasranama on Thursdays for 21 weeks", "à¤­à¤•à¥à¤Ÿ à¤¦à¥‹à¤·: à¥¨à¥§ à¤¹à¤ªà¥à¤¤à¤¾ à¤¬à¤¿à¤¹à¥€à¤¬à¤¾à¤°à¤®à¤¾ à¤µà¤¿à¤·à¥à¤£à¥ à¤¸à¤¹à¤¸à¥à¤°à¤¨à¤¾à¤® à¤œà¤¾à¤ª à¤—à¤°à¥à¤¨à¥à¤¹à¥‹à¤¸à¥"),
    REMEDY_SHADASHTAK("Shadashtak (6-8) Dosha: Perform Rudrabhishek and donate medicines to the needy", "à¤·à¤¡à¤¾à¤·à¥à¤Ÿà¤• (à¥¬-à¥®) à¤¦à¥‹à¤·: à¤°à¥à¤¦à¥à¤°à¤¾à¤­à¤¿à¤·à¥‡à¤• à¤—à¤°à¥à¤¨à¥à¤¹à¥‹à¤¸à¥ à¤° à¤œà¤°à¥à¤°à¤¤à¤®à¤¨à¥à¤¦à¤²à¤¾à¤ˆ à¤”à¤·à¤§à¤¿ à¤¦à¤¾à¤¨ à¤—à¤°à¥à¤¨à¥à¤¹à¥‹à¤¸à¥"),
    REMEDY_MANGLIK_1("Manglik Dosha: Perform Kumbh Vivah (symbolic marriage to a pot or banana tree) before actual marriage", "à¤®à¤¾à¤‚à¤—à¤²à¤¿à¤• à¤¦à¥‹à¤·: à¤µà¤¾à¤¸à¥à¤¤à¤µà¤¿à¤• à¤µà¤¿à¤µà¤¾à¤¹ à¤…à¤˜à¤¿ à¤•à¥à¤®à¥à¤­ à¤µà¤¿à¤µà¤¾à¤¹ (à¤˜à¥ˆà¤‚à¤Ÿà¥‹ à¤µà¤¾ à¤•à¥‡à¤°à¤¾à¤•à¥‹ à¤¬à¥‹à¤Ÿà¤¸à¤à¤— à¤ªà¥à¤°à¤¤à¥€à¤•à¤¾à¤¤à¥à¤®à¤• à¤µà¤¿à¤µà¤¾à¤¹) à¤—à¤°à¥à¤¨à¥à¤¹à¥‹à¤¸à¥"),
    REMEDY_MANGLIK_2("Manglik Dosha: Chant Mangal Mantra 'Om Kraam Kreem Kraum Sah Bhaumaya Namah' 108 times on Tuesdays", "à¤®à¤¾à¤‚à¤—à¤²à¤¿à¤• à¤¦à¥‹à¤·: à¤®à¤‚à¤—à¤²à¤¬à¤¾à¤°à¤®à¤¾ à¤®à¤‚à¤—à¤² à¤®à¤¨à¥à¤¤à¥à¤° 'à¥ à¤•à¥à¤°à¤¾à¤‚ à¤•à¥à¤°à¥€à¤‚ à¤•à¥à¤°à¥Œà¤‚ à¤¸à¤ƒ à¤­à¥Œà¤®à¤¾à¤¯ à¤¨à¤®à¤ƒ' à¥§à¥¦à¥® à¤ªà¤Ÿà¤• à¤œà¤¾à¤ª à¤—à¤°à¥à¤¨à¥à¤¹à¥‹à¤¸à¥"),
    REMEDY_MANGLIK_3("Manglik Dosha: Wear a Red Coral (Moonga) after proper energization and astrological consultation", "à¤®à¤¾à¤‚à¤—à¤²à¤¿à¤• à¤¦à¥‹à¤·: à¤‰à¤šà¤¿à¤¤ à¤Šà¤°à¥à¤œà¤¾à¤µà¤¾à¤¨ à¤° à¤œà¥à¤¯à¥‹à¤¤à¤¿à¤·à¥€à¤¯ à¤ªà¤°à¤¾à¤®à¤°à¥à¤¶ à¤ªà¤›à¤¿ à¤®à¥à¤‚à¤—à¤¾ (à¤ªà¥à¤°à¤µà¤¾à¤²) à¤§à¤¾à¤°à¤£ à¤—à¤°à¥à¤¨à¥à¤¹à¥‹à¤¸à¥"),
    REMEDY_MANGLIK_BRIDE("For Bride's Manglik: Visit Hanuman temple on Tuesdays and offer vermilion and jasmine oil", "à¤¦à¥à¤²à¤¹à¥€à¤•à¥‹ à¤®à¤¾à¤‚à¤—à¤²à¤¿à¤•à¤•à¥‹ à¤²à¤¾à¤—à¤¿: à¤®à¤‚à¤—à¤²à¤¬à¤¾à¤°à¤®à¤¾ à¤¹à¤¨à¥à¤®à¤¾à¤¨ à¤®à¤¨à¥à¤¦à¤¿à¤°à¤®à¤¾ à¤œà¤¾à¤¨à¥à¤¹à¥‹à¤¸à¥ à¤° à¤¸à¤¿à¤¨à¥à¤¦à¥‚à¤° à¤° à¤šà¤®à¥‡à¤²à¥€à¤•à¥‹ à¤¤à¥‡à¤² à¤šà¤¢à¤¾à¤‰à¤¨à¥à¤¹à¥‹à¤¸à¥"),
    REMEDY_MANGLIK_GROOM("For Groom's Manglik: Perform Mars-related charity on Tuesdays (donate red lentils, jaggery, copper)", "à¤¦à¥à¤²à¤¾à¤¹à¤¾à¤•à¥‹ à¤®à¤¾à¤‚à¤—à¤²à¤¿à¤•à¤•à¥‹ à¤²à¤¾à¤—à¤¿: à¤®à¤‚à¤—à¤²à¤¬à¤¾à¤°à¤®à¤¾ à¤®à¤‚à¤—à¤²-à¤¸à¤®à¥à¤¬à¤¨à¥à¤§à¤¿à¤¤ à¤¦à¤¾à¤¨ à¤—à¤°à¥à¤¨à¥à¤¹à¥‹à¤¸à¥ (à¤°à¤¾à¤¤à¥‹ à¤¦à¤¾à¤², à¤—à¥à¤¡, à¤¤à¤¾à¤®à¤¾ à¤¦à¤¾à¤¨ à¤—à¤°à¥à¤¨à¥à¤¹à¥‹à¤¸à¥)"),
    REMEDY_DOUBLE_MANGLIK("Double Manglik: Requires extended Kumbh Vivah ritual and 11 Rudrabhisheks over 11 consecutive Mondays", "à¤¦à¥‹à¤¹à¥‹à¤°à¥‹ à¤®à¤¾à¤‚à¤—à¤²à¤¿à¤•: à¤µà¤¿à¤¸à¥à¤¤à¤¾à¤°à¤¿à¤¤ à¤•à¥à¤®à¥à¤­ à¤µà¤¿à¤µà¤¾à¤¹ à¤µà¤¿à¤§à¤¿ à¤° à¤²à¤—à¤¾à¤¤à¤¾à¤° à¥§à¥§ à¤¸à¥‹à¤®à¤¬à¤¾à¤°à¤®à¤¾ à¥§à¥§ à¤°à¥à¤¦à¥à¤°à¤¾à¤­à¤¿à¤·à¥‡à¤• à¤†à¤µà¤¶à¥à¤¯à¤•"),
    REMEDY_GANA_1("Gana Incompatibility: Chant Ganapati Atharvasheersham daily for 41 days", "à¤—à¤£ à¤…à¤¸à¤‚à¤—à¤¤à¤¤à¤¾: à¥ªà¥§ à¤¦à¤¿à¤¨ à¤¦à¥ˆà¤¨à¤¿à¤• à¤—à¤£à¤ªà¤¤à¤¿ à¤…à¤¥à¤°à¥à¤µà¤¶à¥€à¤°à¥à¤·à¤®à¥ à¤œà¤¾à¤ª à¤—à¤°à¥à¤¨à¥à¤¹à¥‹à¤¸à¥"),
    REMEDY_GANA_2("Gana Incompatibility: Perform Navgraha Shanti puja together before marriage", "à¤—à¤£ à¤…à¤¸à¤‚à¤—à¤¤à¤¤à¤¾: à¤µà¤¿à¤µà¤¾à¤¹ à¤…à¤˜à¤¿ à¤¸à¤à¤—à¥ˆ à¤¨à¤µà¤—à¥à¤°à¤¹ à¤¶à¤¾à¤¨à¥à¤¤à¤¿ à¤ªà¥‚à¤œà¤¾ à¤—à¤°à¥à¤¨à¥à¤¹à¥‹à¤¸à¥"),
    REMEDY_GANA_3("Gana Incompatibility: Practice mutual respect and conscious communication daily", "à¤—à¤£ à¤…à¤¸à¤‚à¤—à¤¤à¤¤à¤¾: à¤¦à¥ˆà¤¨à¤¿à¤• à¤ªà¤¾à¤°à¤¸à¥à¤ªà¤°à¤¿à¤• à¤¸à¤®à¥à¤®à¤¾à¤¨ à¤° à¤¸à¤šà¥‡à¤¤ à¤¸à¤žà¥à¤šà¤¾à¤° à¤…à¤­à¥à¤¯à¤¾à¤¸ à¤—à¤°à¥à¤¨à¥à¤¹à¥‹à¤¸à¥"),
    REMEDY_GRAHA_MAITRI_1("Graha Maitri: Strengthen Mercury through green charity and Budh mantra chanting on Wednesdays", "à¤—à¥à¤°à¤¹ à¤®à¥ˆà¤¤à¥à¤°à¥€: à¤¬à¥à¤§à¤¬à¤¾à¤°à¤®à¤¾ à¤¹à¤°à¤¿à¤¯à¥‹ à¤¦à¤¾à¤¨ à¤° à¤¬à¥à¤§ à¤®à¤¨à¥à¤¤à¥à¤° à¤œà¤¾à¤ªà¤¦à¥à¤µà¤¾à¤°à¤¾ à¤¬à¥à¤§ à¤¬à¤²à¤¿à¤¯à¥‹ à¤¬à¤¨à¤¾à¤‰à¤¨à¥à¤¹à¥‹à¤¸à¥"),
    REMEDY_GRAHA_MAITRI_2("Graha Maitri: Both partners should meditate together daily to improve mental harmony", "à¤—à¥à¤°à¤¹ à¤®à¥ˆà¤¤à¥à¤°à¥€: à¤®à¤¾à¤¨à¤¸à¤¿à¤• à¤¸à¤¾à¤®à¤žà¥à¤œà¤¸à¥à¤¯ à¤¸à¥à¤§à¤¾à¤° à¤—à¤°à¥à¤¨ à¤¦à¥à¤µà¥ˆ à¤¸à¤¾à¤à¥‡à¤¦à¤¾à¤°à¤²à¥‡ à¤¦à¥ˆà¤¨à¤¿à¤• à¤¸à¤à¤—à¥ˆ à¤§à¥à¤¯à¤¾à¤¨ à¤—à¤°à¥à¤¨à¥à¤ªà¤°à¥à¤›"),
    REMEDY_YONI_1("Yoni Incompatibility: Worship Kamadeva (God of Love) and offer flowers on Fridays", "à¤¯à¥‹à¤¨à¤¿ à¤…à¤¸à¤‚à¤—à¤¤à¤¤à¤¾: à¤¶à¥à¤•à¥à¤°à¤¬à¤¾à¤°à¤®à¤¾ à¤•à¤¾à¤®à¤¦à¥‡à¤µ (à¤ªà¥à¤°à¥‡à¤®à¤•à¥‹ à¤¦à¥‡à¤µà¤¤à¤¾) à¤ªà¥‚à¤œà¤¾ à¤—à¤°à¥à¤¨à¥à¤¹à¥‹à¤¸à¥ à¤° à¤«à¥‚à¤² à¤šà¤¢à¤¾à¤‰à¤¨à¥à¤¹à¥‹à¤¸à¥"),
    REMEDY_YONI_2("Yoni Incompatibility: Perform Ashwamedha or Gajamedha symbolic puja to neutralize animal enmity", "à¤¯à¥‹à¤¨à¤¿ à¤…à¤¸à¤‚à¤—à¤¤à¤¤à¤¾: à¤ªà¤¶à¥ à¤µà¥ˆà¤®à¤¨à¤¸à¥à¤¯ à¤¨à¤¿à¤·à¥à¤•à¥à¤°à¤¿à¤¯ à¤—à¤°à¥à¤¨ à¤…à¤¶à¥à¤µà¤®à¥‡à¤§ à¤µà¤¾ à¤—à¤œà¤®à¥‡à¤§ à¤ªà¥à¤°à¤¤à¥€à¤•à¤¾à¤¤à¥à¤®à¤• à¤ªà¥‚à¤œà¤¾ à¤—à¤°à¥à¤¨à¥à¤¹à¥‹à¤¸à¥"),
    REMEDY_VEDHA_1("Vedha Dosha: Perform Nakshatra Shanti puja for both birth stars", "à¤µà¥‡à¤§ à¤¦à¥‹à¤·: à¤¦à¥à¤µà¥ˆ à¤œà¤¨à¥à¤® à¤¨à¤•à¥à¤·à¤¤à¥à¤°à¤•à¥‹ à¤²à¤¾à¤—à¤¿ à¤¨à¤•à¥à¤·à¤¤à¥à¤° à¤¶à¤¾à¤¨à¥à¤¤à¤¿ à¤ªà¥‚à¤œà¤¾ à¤—à¤°à¥à¤¨à¥à¤¹à¥‹à¤¸à¥"),
    REMEDY_VEDHA_2("Vedha Dosha: Donate black sesame and urad dal on Saturdays", "à¤µà¥‡à¤§ à¤¦à¥‹à¤·: à¤¶à¤¨à¤¿à¤¬à¤¾à¤°à¤®à¤¾ à¤•à¤¾à¤²à¥‹ à¤¤à¤¿à¤² à¤° à¤‰à¤¡à¤¦à¤•à¥‹ à¤¦à¤¾à¤² à¤¦à¤¾à¤¨ à¤—à¤°à¥à¤¨à¥à¤¹à¥‹à¤¸à¥"),
    REMEDY_RAJJU_SIRO("Siro Rajju Dosha (Head): Perform Ayushya Homa and worship Lord Mrityunjaya Shiva", "à¤¶à¤¿à¤°à¥‹ à¤°à¤œà¥à¤œà¥ à¤¦à¥‹à¤· (à¤¶à¤¿à¤°): à¤†à¤¯à¥à¤·à¥à¤¯ à¤¹à¥‹à¤® à¤—à¤°à¥à¤¨à¥à¤¹à¥‹à¤¸à¥ à¤° à¤­à¤—à¤µà¤¾à¤¨ à¤®à¥ƒà¤¤à¥à¤¯à¥à¤žà¥à¤œà¤¯ à¤¶à¤¿à¤µà¤•à¥‹ à¤ªà¥‚à¤œà¤¾ à¤—à¤°à¥à¤¨à¥à¤¹à¥‹à¤¸à¥"),
    REMEDY_RAJJU_KANTHA("Kantha Rajju Dosha (Neck): Wear Rudraksha mala and chant Vishnu mantras", "à¤•à¤£à¥à¤  à¤°à¤œà¥à¤œà¥ à¤¦à¥‹à¤· (à¤˜à¤¾à¤à¤Ÿà¥€): à¤°à¥à¤¦à¥à¤°à¤¾à¤•à¥à¤· à¤®à¤¾à¤²à¤¾ à¤²à¤—à¤¾à¤‰à¤¨à¥à¤¹à¥‹à¤¸à¥ à¤° à¤µà¤¿à¤·à¥à¤£à¥ à¤®à¤¨à¥à¤¤à¥à¤° à¤œà¤¾à¤ª à¤—à¤°à¥à¤¨à¥à¤¹à¥‹à¤¸à¥"),
    REMEDY_RAJJU_NABHI("Nabhi Rajju Dosha (Navel): Perform Santan Gopal puja and donate to orphanages", "à¤¨à¤¾à¤­à¤¿ à¤°à¤œà¥à¤œà¥ à¤¦à¥‹à¤· (à¤¨à¤¾à¤­à¤¿): à¤¸à¤¨à¥à¤¤à¤¾à¤¨ à¤—à¥‹à¤ªà¤¾à¤² à¤ªà¥‚à¤œà¤¾ à¤—à¤°à¥à¤¨à¥à¤¹à¥‹à¤¸à¥ à¤° à¤…à¤¨à¤¾à¤¥à¤¾à¤¶à¥à¤°à¤®à¤®à¤¾ à¤¦à¤¾à¤¨ à¤—à¤°à¥à¤¨à¥à¤¹à¥‹à¤¸à¥"),
    REMEDY_RAJJU_KATI("Kati Rajju Dosha (Waist): Perform Lakshmi puja and donate to poverty relief", "à¤•à¤Ÿà¤¿ à¤°à¤œà¥à¤œà¥ à¤¦à¥‹à¤· (à¤•à¤®à¥à¤®à¤°): à¤²à¤•à¥à¤·à¥à¤®à¥€ à¤ªà¥‚à¤œà¤¾ à¤—à¤°à¥à¤¨à¥à¤¹à¥‹à¤¸à¥ à¤° à¤—à¤°à¤¿à¤¬à¥€ à¤‰à¤¨à¥à¤®à¥‚à¤²à¤¨à¤®à¤¾ à¤¦à¤¾à¤¨ à¤—à¤°à¥à¤¨à¥à¤¹à¥‹à¤¸à¥"),
    REMEDY_RAJJU_PADA("Pada Rajju Dosha (Feet): Worship at a pilgrimage site together before marriage", "à¤ªà¤¾à¤¦ à¤°à¤œà¥à¤œà¥ à¤¦à¥‹à¤· (à¤ªà¤¾à¤‰): à¤µà¤¿à¤µà¤¾à¤¹ à¤…à¤˜à¤¿ à¤¸à¤à¤—à¥ˆ à¤¤à¥€à¤°à¥à¤¥à¤¸à¥à¤¥à¤²à¤®à¤¾ à¤ªà¥‚à¤œà¤¾ à¤—à¤°à¥à¤¨à¥à¤¹à¥‹à¤¸à¥"),
    REMEDY_GENERAL_1("General: Perform Satyanarayan Puja together on Purnima (full moon)", "à¤¸à¤¾à¤®à¤¾à¤¨à¥à¤¯: à¤ªà¥‚à¤°à¥à¤£à¤¿à¤®à¤¾à¤®à¤¾ à¤¸à¤à¤—à¥ˆ à¤¸à¤¤à¥à¤¯à¤¨à¤¾à¤°à¤¾à¤¯à¤£ à¤ªà¥‚à¤œà¤¾ à¤—à¤°à¥à¤¨à¥à¤¹à¥‹à¤¸à¥"),
    REMEDY_GENERAL_2("General: Chant Swayamvara Parvati Mantra for marital harmony", "à¤¸à¤¾à¤®à¤¾à¤¨à¥à¤¯: à¤µà¥ˆà¤µà¤¾à¤¹à¤¿à¤• à¤¸à¤¾à¤®à¤žà¥à¤œà¤¸à¥à¤¯à¤•à¥‹ à¤²à¤¾à¤—à¤¿ à¤¸à¥à¤µà¤¯à¤‚à¤µà¤° à¤ªà¤¾à¤°à¥à¤µà¤¤à¥€ à¤®à¤¨à¥à¤¤à¥à¤° à¤œà¤¾à¤ª à¤—à¤°à¥à¤¨à¥à¤¹à¥‹à¤¸à¥"),
    REMEDY_GENERAL_3("General: Donate to couples in need or contribute to marriage funds for the poor", "à¤¸à¤¾à¤®à¤¾à¤¨à¥à¤¯: à¤œà¤°à¥à¤°à¤¤à¤®à¤¨à¥à¤¦ à¤œà¥‹à¤¡à¥€à¤¹à¤°à¥‚à¤²à¤¾à¤ˆ à¤¦à¤¾à¤¨ à¤—à¤°à¥à¤¨à¥à¤¹à¥‹à¤¸à¥ à¤µà¤¾ à¤—à¤°à¤¿à¤¬à¤•à¥‹ à¤µà¤¿à¤µà¤¾à¤¹ à¤•à¥‹à¤·à¤®à¤¾ à¤¯à¥‹à¤—à¤¦à¤¾à¤¨ à¤—à¤°à¥à¤¨à¥à¤¹à¥‹à¤¸à¥"),
    REMEDY_GENERAL_4("General: Both partners should observe Monday fasts for Lord Shiva", "à¤¸à¤¾à¤®à¤¾à¤¨à¥à¤¯: à¤¦à¥à¤µà¥ˆ à¤¸à¤¾à¤à¥‡à¤¦à¤¾à¤°à¤²à¥‡ à¤­à¤—à¤µà¤¾à¤¨ à¤¶à¤¿à¤µà¤•à¥‹ à¤²à¤¾à¤—à¤¿ à¤¸à¥‹à¤®à¤¬à¤¾à¤° à¤µà¥à¤°à¤¤ à¤—à¤°à¥à¤¨à¥à¤ªà¤°à¥à¤›"),
    REMEDY_SERIOUS_1("Serious Concerns: Consult a qualified Vedic astrologer for personalized guidance", "à¤—à¤®à¥à¤­à¥€à¤° à¤šà¤¿à¤¨à¥à¤¤à¤¾à¤¹à¤°à¥‚: à¤µà¥à¤¯à¤•à¥à¤¤à¤¿à¤—à¤¤ à¤®à¤¾à¤°à¥à¤—à¤¦à¤°à¥à¤¶à¤¨à¤•à¥‹ à¤²à¤¾à¤—à¤¿ à¤¯à¥‹à¤—à¥à¤¯ à¤µà¥ˆà¤¦à¤¿à¤• à¤œà¥à¤¯à¥‹à¤¤à¤¿à¤·à¥€à¤¸à¤à¤— à¤ªà¤°à¤¾à¤®à¤°à¥à¤¶ à¤—à¤°à¥à¤¨à¥à¤¹à¥‹à¤¸à¥"),
    REMEDY_SERIOUS_2("Serious Concerns: Consider Maha Mrityunjaya Homa for overall protection", "à¤—à¤®à¥à¤­à¥€à¤° à¤šà¤¿à¤¨à¥à¤¤à¤¾à¤¹à¤°à¥‚: à¤¸à¤®à¤—à¥à¤° à¤¸à¥à¤°à¤•à¥à¤·à¤¾à¤•à¥‹ à¤²à¤¾à¤—à¤¿ à¤®à¤¹à¤¾ à¤®à¥ƒà¤¤à¥à¤¯à¥à¤žà¥à¤œà¤¯ à¤¹à¥‹à¤® à¤µà¤¿à¤šà¤¾à¤° à¤—à¤°à¥à¤¨à¥à¤¹à¥‹à¤¸à¥"),
    REMEDY_NONE_NEEDED("Excellent compatibility - no specific remedies required.", "à¤‰à¤¤à¥à¤•à¥ƒà¤·à¥à¤Ÿ à¤…à¤¨à¥à¤•à¥‚à¤²à¤¤à¤¾ - à¤•à¥à¤¨à¥ˆ à¤µà¤¿à¤¶à¥‡à¤· à¤‰à¤ªà¤¾à¤¯ à¤†à¤µà¤¶à¥à¤¯à¤• à¤›à¥ˆà¤¨à¥¤"),
    REMEDY_SATYANARAYAN("For auspiciousness: Perform Satyanarayan Katha together on the first Purnima after marriage", "à¤¶à¥à¤­à¤¤à¤¾à¤•à¥‹ à¤²à¤¾à¤—à¤¿: à¤µà¤¿à¤µà¤¾à¤¹ à¤ªà¤›à¤¿à¤•à¥‹ à¤ªà¤¹à¤¿à¤²à¥‹ à¤ªà¥‚à¤°à¥à¤£à¤¿à¤®à¤¾à¤®à¤¾ à¤¸à¤à¤—à¥ˆ à¤¸à¤¤à¥à¤¯à¤¨à¤¾à¤°à¤¾à¤¯à¤£ à¤•à¤¥à¤¾ à¤—à¤°à¥à¤¨à¥à¤¹à¥‹à¤¸à¥"),

    // ============================================
    // MATCHMAKING - SUMMARY & ANALYSIS STRINGS
    // ============================================
    SUMMARY_TITLE("KUNDLI MILAN SUMMARY", "à¤•à¥à¤£à¥à¤¡à¤²à¥€ à¤®à¤¿à¤²à¤¾à¤¨ à¤¸à¤¾à¤°à¤¾à¤‚à¤¶"),
    SUMMARY_OVERALL_SCORE("Overall Score", "à¤¸à¤®à¤—à¥à¤° à¤…à¤‚à¤•"),
    SUMMARY_RATING("Rating", "à¤®à¥‚à¤²à¥à¤¯à¤¾à¤‚à¤•à¤¨"),
    SUMMARY_STRENGTHS("Strengths", "à¤¶à¤•à¥à¤¤à¤¿à¤¹à¤°à¥‚"),
    SUMMARY_CONCERNS("Areas of Concern", "à¤šà¤¿à¤¨à¥à¤¤à¤¾à¤•à¤¾ à¤•à¥à¤·à¥‡à¤¤à¥à¤°à¤¹à¤°à¥‚"),
    SUMMARY_MANGLIK("Manglik Status", "à¤®à¤¾à¤‚à¤—à¤²à¤¿à¤• à¤¸à¥à¤¥à¤¿à¤¤à¤¿"),
    SUMMARY_ADDITIONAL("Additional Factors", "à¤…à¤¤à¤¿à¤°à¤¿à¤•à¥à¤¤ à¤•à¤¾à¤°à¤•à¤¹à¤°à¥‚"),
    SUMMARY_RECOMMENDATION("Recommendation", "à¤¸à¤¿à¤«à¤¾à¤°à¤¿à¤¸"),
    DETAILED_TITLE("DETAILED MATCHMAKING ANALYSIS", "à¤µà¤¿à¤¸à¥à¤¤à¥ƒà¤¤ à¤®à¤¿à¤²à¤¾à¤¨ à¤µà¤¿à¤¶à¥à¤²à¥‡à¤·à¤£"),
    BIRTH_DATA_SUMMARY("Birth Data Summary", "à¤œà¤¨à¥à¤® à¤¡à¤¾à¤Ÿà¤¾ à¤¸à¤¾à¤°à¤¾à¤‚à¤¶"),
    MOON_SIGN("Moon Sign", "à¤šà¤¨à¥à¤¦à¥à¤° à¤°à¤¾à¤¶à¤¿"),
    NAKSHATRA_LABEL("Nakshatra", "à¤¨à¤•à¥à¤·à¤¤à¥à¤°"),
    PADA_LABEL("Pada", "à¤ªà¤¾à¤¦"),
    MOON_LONGITUDE("Moon Longitude", "à¤šà¤¨à¥à¤¦à¥à¤° à¤¦à¥‡à¤¶à¤¾à¤¨à¥à¤¤à¤°"),
    PURPOSE("Purpose", "à¤‰à¤¦à¥à¤¦à¥‡à¤¶à¥à¤¯"),
    ANALYSIS_LABEL("Analysis", "à¤µà¤¿à¤¶à¥à¤²à¥‡à¤·à¤£"),
    ADDITIONAL_FACTORS_TITLE("Additional Compatibility Factors", "à¤…à¤¤à¤¿à¤°à¤¿à¤•à¥à¤¤ à¤…à¤¨à¥à¤•à¥‚à¤²à¤¤à¤¾ à¤•à¤¾à¤°à¤•à¤¹à¤°à¥‚"),
    OBSTRUCTION("Obstruction", "à¤¬à¤¾à¤§à¤¾"),
    COSMIC_BOND("Cosmic Bond", "à¤¬à¥à¤°à¤¹à¥à¤®à¤¾à¤£à¥à¤¡à¥€à¤¯ à¤¬à¤¨à¥à¤§à¤¨"),
    WARNING_LABEL("Warning", "à¤šà¥‡à¤¤à¤¾à¤µà¤¨à¥€"),
    WIFE_PROSPERITY("Wife's Prosperity", "à¤ªà¤¤à¥à¤¨à¥€à¤•à¥‹ à¤¸à¤®à¥ƒà¤¦à¥à¤§à¤¿"),
    NAKSHATRA_DIFF("Nakshatra Difference", "à¤¨à¤•à¥à¤·à¤¤à¥à¤° à¤­à¤¿à¤¨à¥à¤¨à¤¤à¤¾"),
    SATISFIED("Satisfied", "à¤¸à¤¨à¥à¤¤à¥à¤·à¥à¤Ÿ"),
    NOT_SATISFIED("Not Satisfied", "à¤¸à¤¨à¥à¤¤à¥à¤·à¥à¤Ÿ à¤›à¥ˆà¤¨"),
    LONGEVITY_PROSPERITY("Longevity & Prosperity", "à¤¦à¥€à¤°à¥à¤˜à¤¾à¤¯à¥ à¤° à¤¸à¤®à¥ƒà¤¦à¥à¤§à¤¿"),
    NOT_APPLICABLE("Not Applicable", "à¤²à¤¾à¤—à¥‚ à¤¹à¥à¤à¤¦à¥ˆà¤¨"),

    // ============================================
    // MATCHMAKING - COMMON STATUS STRINGS
    // ============================================
    BRIDE("Bride", "à¤¦à¥à¤²à¤¹à¥€"),
    GROOM("Groom", "à¤¦à¥à¤²à¤¾à¤¹à¤¾"),
    STATUS("Status", "à¤¸à¥à¤¥à¤¿à¤¤à¤¿"),
    COMPATIBLE("Compatible", "à¤…à¤¨à¥à¤•à¥‚à¤²"),
    NEEDS_ATTENTION("Needs Attention", "à¤§à¥à¤¯à¤¾à¤¨ à¤†à¤µà¤¶à¥à¤¯à¤•"),
    PRESENT("Present", "à¤‰à¤ªà¤¸à¥à¤¥à¤¿à¤¤"),
    NOT_PRESENT("Not Present", "à¤‰à¤ªà¤¸à¥à¤¥à¤¿à¤¤ à¤›à¥ˆà¤¨"),
    FAVORABLE("Favorable", "à¤…à¤¨à¥à¤•à¥‚à¤²"),
    SAME_RAJJU("Same Rajju", "à¤‰à¤¹à¥€ à¤°à¤œà¥à¤œà¥"),
    DETAILS("Details", "à¤µà¤¿à¤µà¤°à¤£"),

    // ============================================
    // MATCHMAKING - GUNA DESCRIPTIONS
    // ============================================
    GUNA_DESC_VARNA("Varna indicates spiritual compatibility based on the Moon sign's element. It measures ego harmony and how partners relate on a spiritual level. Higher compatibility suggests natural understanding of values.", "à¤µà¤°à¥à¤£à¤²à¥‡ à¤šà¤¨à¥à¤¦à¥à¤° à¤°à¤¾à¤¶à¤¿à¤•à¥‹ à¤¤à¤¤à¥à¤µà¤®à¤¾ à¤†à¤§à¤¾à¤°à¤¿à¤¤ à¤†à¤§à¥à¤¯à¤¾à¤¤à¥à¤®à¤¿à¤• à¤…à¤¨à¥à¤•à¥‚à¤²à¤¤à¤¾ à¤¸à¤‚à¤•à¥‡à¤¤ à¤—à¤°à¥à¤¦à¤›à¥¤ à¤¯à¤¸à¤²à¥‡ à¤…à¤¹à¤‚à¤•à¤¾à¤° à¤¸à¤¾à¤®à¤žà¥à¤œà¤¸à¥à¤¯ à¤° à¤¸à¤¾à¤à¥‡à¤¦à¤¾à¤°à¤¹à¤°à¥‚ à¤†à¤§à¥à¤¯à¤¾à¤¤à¥à¤®à¤¿à¤• à¤¸à¥à¤¤à¤°à¤®à¤¾ à¤•à¤¸à¤°à¥€ à¤¸à¤®à¥à¤¬à¤¨à¥à¤§à¤¿à¤¤ à¤›à¤¨à¥ à¤®à¤¾à¤ªà¤¨ à¤—à¤°à¥à¤¦à¤›à¥¤"),
    GUNA_DESC_VASHYA("Vashya measures the mutual attraction and influence between partners. It indicates who can influence whom and the power dynamics in the relationship.", "à¤µà¤¶à¥à¤¯à¤²à¥‡ à¤¸à¤¾à¤à¥‡à¤¦à¤¾à¤°à¤¹à¤°à¥‚ à¤¬à¥€à¤šà¤•à¥‹ à¤ªà¤¾à¤°à¤¸à¥à¤ªà¤°à¤¿à¤• à¤†à¤•à¤°à¥à¤·à¤£ à¤° à¤ªà¥à¤°à¤­à¤¾à¤µ à¤®à¤¾à¤ªà¤¨ à¤—à¤°à¥à¤¦à¤›à¥¤ à¤¯à¤¸à¤²à¥‡ à¤•à¤¸à¤²à¥‡ à¤•à¤¸à¤²à¤¾à¤ˆ à¤ªà¥à¤°à¤­à¤¾à¤µ à¤ªà¤¾à¤°à¥à¤¨ à¤¸à¤•à¥à¤› à¤° à¤¸à¤®à¥à¤¬à¤¨à¥à¤§à¤®à¤¾ à¤¶à¤•à¥à¤¤à¤¿ à¤—à¤¤à¤¿à¤¶à¥€à¤²à¤¤à¤¾ à¤¸à¤‚à¤•à¥‡à¤¤ à¤—à¤°à¥à¤¦à¤›à¥¤"),
    GUNA_DESC_TARA("Tara analyzes destiny compatibility through the birth stars (Nakshatras). It determines the auspiciousness of the couple's combined destiny path.", "à¤¤à¤¾à¤°à¤¾ à¤œà¤¨à¥à¤® à¤¤à¤¾à¤°à¤¾ (à¤¨à¤•à¥à¤·à¤¤à¥à¤°) à¤®à¤¾à¤°à¥à¤«à¤¤ à¤­à¤¾à¤—à¥à¤¯ à¤…à¤¨à¥à¤•à¥‚à¤²à¤¤à¤¾ à¤µà¤¿à¤¶à¥à¤²à¥‡à¤·à¤£ à¤—à¤°à¥à¤¦à¤›à¥¤ à¤¯à¤¸à¤²à¥‡ à¤œà¥‹à¤¡à¥€à¤•à¥‹ à¤¸à¤‚à¤¯à¥à¤•à¥à¤¤ à¤­à¤¾à¤—à¥à¤¯ à¤®à¤¾à¤°à¥à¤—à¤•à¥‹ à¤¶à¥à¤­à¤¤à¤¾ à¤¨à¤¿à¤°à¥à¤§à¤¾à¤°à¤£ à¤—à¤°à¥à¤¦à¤›à¥¤"),
    GUNA_DESC_YONI("Yoni represents physical and sexual compatibility based on the animal nature assigned to each Nakshatra. Same or friendly animals indicate better physical harmony.", "à¤¯à¥‹à¤¨à¥€à¤²à¥‡ à¤ªà¥à¤°à¤¤à¥à¤¯à¥‡à¤• à¤¨à¤•à¥à¤·à¤¤à¥à¤°à¤²à¤¾à¤ˆ à¤¤à¥‹à¤•à¤¿à¤à¤•à¥‹ à¤ªà¤¶à¥ à¤ªà¥à¤°à¤•à¥ƒà¤¤à¤¿à¤®à¤¾ à¤†à¤§à¤¾à¤°à¤¿à¤¤ à¤¶à¤¾à¤°à¥€à¤°à¤¿à¤• à¤° à¤¯à¥Œà¤¨ à¤…à¤¨à¥à¤•à¥‚à¤²à¤¤à¤¾ à¤ªà¥à¤°à¤¤à¤¿à¤¨à¤¿à¤§à¤¿à¤¤à¥à¤µ à¤—à¤°à¥à¤¦à¤›à¥¤ à¤‰à¤¹à¥€ à¤µà¤¾ à¤®à¤¿à¤¤à¥à¤° à¤ªà¤¶à¥à¤¹à¤°à¥‚à¤²à¥‡ à¤°à¤¾à¤®à¥à¤°à¥‹ à¤¶à¤¾à¤°à¥€à¤°à¤¿à¤• à¤¸à¤¾à¤®à¤žà¥à¤œà¤¸à¥à¤¯ à¤¸à¤‚à¤•à¥‡à¤¤ à¤—à¤°à¥à¤¦à¤›à¥¤"),
    GUNA_DESC_GRAHA_MAITRI("Graha Maitri analyzes mental compatibility through the friendship of Moon sign lords. It indicates how well the couple can understand each other intellectually.", "à¤—à¥à¤°à¤¹ à¤®à¥ˆà¤¤à¥à¤°à¥€à¤²à¥‡ à¤šà¤¨à¥à¤¦à¥à¤° à¤°à¤¾à¤¶à¤¿ à¤¸à¥à¤µà¤¾à¤®à¥€à¤¹à¤°à¥‚à¤•à¥‹ à¤®à¤¿à¤¤à¥à¤°à¤¤à¤¾ à¤®à¤¾à¤°à¥à¤«à¤¤ à¤®à¤¾à¤¨à¤¸à¤¿à¤• à¤…à¤¨à¥à¤•à¥‚à¤²à¤¤à¤¾ à¤µà¤¿à¤¶à¥à¤²à¥‡à¤·à¤£ à¤—à¤°à¥à¤¦à¤›à¥¤ à¤¯à¤¸à¤²à¥‡ à¤œà¥‹à¤¡à¥€à¤²à¥‡ à¤¬à¥Œà¤¦à¥à¤§à¤¿à¤• à¤°à¥‚à¤ªà¤®à¤¾ à¤à¤•à¤…à¤°à¥à¤•à¤¾à¤²à¤¾à¤ˆ à¤•à¤¤à¥à¤¤à¤¿à¤•à¥‹ à¤°à¤¾à¤®à¥à¤°à¥‹à¤¸à¤à¤— à¤¬à¥à¤à¥à¤¨ à¤¸à¤•à¥à¤› à¤¸à¤‚à¤•à¥‡à¤¤ à¤—à¤°à¥à¤¦à¤›à¥¤"),
    GUNA_DESC_GANA("Gana measures temperament compatibility through Deva (divine), Manushya (human), or Rakshasa (demon) classification based on Nakshatra.", "à¤—à¤£à¤²à¥‡ à¤¨à¤•à¥à¤·à¤¤à¥à¤°à¤®à¤¾ à¤†à¤§à¤¾à¤°à¤¿à¤¤ à¤¦à¥‡à¤µ, à¤®à¤¨à¥à¤·à¥à¤¯, à¤µà¤¾ à¤°à¤¾à¤•à¥à¤·à¤¸ à¤µà¤°à¥à¤—à¥€à¤•à¤°à¤£ à¤®à¤¾à¤°à¥à¤«à¤¤ à¤¸à¥à¤µà¤­à¤¾à¤µ à¤…à¤¨à¥à¤•à¥‚à¤²à¤¤à¤¾ à¤®à¤¾à¤ªà¤¨ à¤—à¤°à¥à¤¦à¤›à¥¤"),
    GUNA_DESC_BHAKOOT("Bhakoot indicates love, health, and financial compatibility based on the Moon sign positions. It's crucial for long-term marital harmony.", "à¤­à¤•à¥à¤Ÿà¤²à¥‡ à¤šà¤¨à¥à¤¦à¥à¤° à¤°à¤¾à¤¶à¤¿ à¤¸à¥à¤¥à¤¿à¤¤à¤¿à¤¹à¤°à¥‚à¤®à¤¾ à¤†à¤§à¤¾à¤°à¤¿à¤¤ à¤ªà¥à¤°à¥‡à¤®, à¤¸à¥à¤µà¤¾à¤¸à¥à¤¥à¥à¤¯ à¤° à¤†à¤°à¥à¤¥à¤¿à¤• à¤…à¤¨à¥à¤•à¥‚à¤²à¤¤à¤¾ à¤¸à¤‚à¤•à¥‡à¤¤ à¤—à¤°à¥à¤¦à¤›à¥¤ à¤¯à¥‹ à¤¦à¥€à¤°à¥à¤˜à¤•à¤¾à¤²à¥€à¤¨ à¤µà¥ˆà¤µà¤¾à¤¹à¤¿à¤• à¤¸à¤¾à¤®à¤žà¥à¤œà¤¸à¥à¤¯à¤•à¥‹ à¤²à¤¾à¤—à¤¿ à¤®à¤¹à¤¤à¥à¤¤à¥à¤µà¤ªà¥‚à¤°à¥à¤£ à¤›à¥¤"),
    GUNA_DESC_NADI("Nadi is the most important factor (8 points), indicating health and progeny compatibility. Same Nadi can cause health issues and affect children.", "à¤¨à¤¾à¤¡à¥€ à¤¸à¤¬à¥ˆà¤­à¤¨à¥à¤¦à¤¾ à¤®à¤¹à¤¤à¥à¤¤à¥à¤µà¤ªà¥‚à¤°à¥à¤£ à¤•à¤¾à¤°à¤• (à¥® à¤…à¤‚à¤•) à¤¹à¥‹, à¤¸à¥à¤µà¤¾à¤¸à¥à¤¥à¥à¤¯ à¤° à¤¸à¤¨à¥à¤¤à¤¾à¤¨ à¤…à¤¨à¥à¤•à¥‚à¤²à¤¤à¤¾ à¤¸à¤‚à¤•à¥‡à¤¤ à¤—à¤°à¥à¤¦à¤›à¥¤ à¤‰à¤¹à¥€ à¤¨à¤¾à¤¡à¥€à¤²à¥‡ à¤¸à¥à¤µà¤¾à¤¸à¥à¤¥à¥à¤¯ à¤¸à¤®à¤¸à¥à¤¯à¤¾ à¤° à¤¸à¤¨à¥à¤¤à¤¾à¤¨à¤®à¤¾ à¤…à¤¸à¤° à¤ªà¤¾à¤°à¥à¤¨ à¤¸à¤•à¥à¤›à¥¤"),
    GUNA_DESC_NOT_AVAILABLE("Detailed description not available for this Guna.", "à¤¯à¤¸ à¤—à¥à¤£à¤•à¥‹ à¤µà¤¿à¤¸à¥à¤¤à¥ƒà¤¤ à¤µà¤¿à¤µà¤°à¤£ à¤‰à¤ªà¤²à¤¬à¥à¤§ à¤›à¥ˆà¤¨à¥¤"),

    // ============================================
    // MATCHMAKING - DETAILED GUNA INTERPRETATIONS
    // ============================================
    // Varna Detailed
    VARNA_DETAILED_COMPATIBLE("This creates excellent spiritual harmony with natural respect flowing from bride to groom. Partners will have compatible life philosophies, values, and approaches to dharma. The groom's natural leadership in spiritual matters will be appreciated.", "à¤¯à¤¸à¤²à¥‡ à¤¦à¥à¤²à¤¹à¥€à¤¬à¤¾à¤Ÿ à¤¦à¥à¤²à¤¾à¤¹à¤¾à¤¤à¤¿à¤° à¤ªà¥à¤°à¤¾à¤•à¥ƒà¤¤à¤¿à¤• à¤¸à¤®à¥à¤®à¤¾à¤¨ à¤ªà¥à¤°à¤µà¤¾à¤¹à¤¸à¤¹à¤¿à¤¤ à¤‰à¤¤à¥à¤•à¥ƒà¤·à¥à¤Ÿ à¤†à¤§à¥à¤¯à¤¾à¤¤à¥à¤®à¤¿à¤• à¤¸à¤¾à¤®à¤žà¥à¤œà¤¸à¥à¤¯ à¤¸à¤¿à¤°à¥à¤œà¤¨à¤¾ à¤—à¤°à¥à¤¦à¤›à¥¤ à¤¸à¤¾à¤à¥‡à¤¦à¤¾à¤°à¤¹à¤°à¥‚à¤•à¥‹ à¤œà¥€à¤µà¤¨ à¤¦à¤°à¥à¤¶à¤¨, à¤®à¥‚à¤²à¥à¤¯à¤¹à¤°à¥‚ à¤° à¤§à¤°à¥à¤®à¤ªà¥à¤°à¤¤à¤¿ à¤¦à¥ƒà¤·à¥à¤Ÿà¤¿à¤•à¥‹à¤£ à¤®à¤¿à¤²à¥à¤¨à¥‡à¤›à¥¤ à¤†à¤§à¥à¤¯à¤¾à¤¤à¥à¤®à¤¿à¤• à¤µà¤¿à¤·à¤¯à¤¹à¤°à¥‚à¤®à¤¾ à¤¦à¥à¤²à¤¾à¤¹à¤¾à¤•à¥‹ à¤ªà¥à¤°à¤¾à¤•à¥ƒà¤¤à¤¿à¤• à¤¨à¥‡à¤¤à¥ƒà¤¤à¥à¤µ à¤¸à¤°à¤¾à¤¹à¤¨à¤¾ à¤—à¤°à¤¿à¤¨à¥‡à¤›à¥¤"),
    VARNA_DETAILED_INCOMPATIBLE("This may create ego conflicts and power struggles. The bride may feel she has to compromise her values or suppress her intellect. Both partners need conscious effort to respect each other's perspectives and maintain balance.", "à¤¯à¤¸à¤²à¥‡ à¤…à¤¹à¤‚à¤•à¤¾à¤° à¤¦à¥à¤µà¤¨à¥à¤¦à¥à¤µ à¤° à¤¶à¤•à¥à¤¤à¤¿ à¤¸à¤‚à¤˜à¤°à¥à¤· à¤¸à¤¿à¤°à¥à¤œà¤¨à¤¾ à¤—à¤°à¥à¤¨ à¤¸à¤•à¥à¤›à¥¤ à¤¦à¥à¤²à¤¹à¥€à¤²à¥‡ à¤†à¤«à¥à¤¨à¤¾ à¤®à¥‚à¤²à¥à¤¯à¤¹à¤°à¥‚à¤®à¤¾ à¤¸à¤®à¥à¤à¥Œà¤¤à¤¾ à¤—à¤°à¥à¤¨à¥ à¤ªà¤°à¥‹à¤¸à¥ à¤µà¤¾ à¤†à¤«à¥à¤¨à¥‹ à¤¬à¥à¤¦à¥à¤§à¤¿ à¤¦à¤®à¤¨ à¤—à¤°à¥à¤¨à¥à¤ªà¤°à¥à¤²à¤¾ à¤œà¤¸à¥à¤¤à¥‹ à¤®à¤¹à¤¸à¥à¤¸ à¤—à¤°à¥à¤¨ à¤¸à¤•à¥à¤›à¤¿à¤¨à¥à¥¤ à¤¦à¥à¤µà¥ˆ à¤¸à¤¾à¤à¥‡à¤¦à¤¾à¤°à¤¹à¤°à¥‚à¤²à¥‡ à¤à¤•à¤…à¤°à¥à¤•à¤¾à¤•à¥‹ à¤¦à¥ƒà¤·à¥à¤Ÿà¤¿à¤•à¥‹à¤£à¤²à¤¾à¤ˆ à¤¸à¤®à¥à¤®à¤¾à¤¨ à¤—à¤°à¥à¤¨ à¤° à¤¸à¤¨à¥à¤¤à¥à¤²à¤¨ à¤•à¤¾à¤¯à¤® à¤°à¤¾à¤–à¥à¤¨ à¤¸à¤šà¥‡à¤¤ à¤ªà¥à¤°à¤¯à¤¾à¤¸ à¤šà¤¾à¤¹à¤¿à¤¨à¥à¤›à¥¤"),

    // Vashya Detailed
    VASHYA_REMEDY_LOW("Remedy: Worship Lord Shiva and Goddess Parvati together on Mondays. Practice mutual respect and avoid trying to dominate each other. Strengthen Venus through Friday fasts and donations of white items.", "à¤‰à¤ªà¤¾à¤¯: à¤¸à¥‹à¤®à¤¬à¤¾à¤°à¤®à¤¾ à¤­à¤—à¤µà¤¾à¤¨ à¤¶à¤¿à¤µ à¤° à¤¦à¥‡à¤µà¥€ à¤ªà¤¾à¤°à¥à¤µà¤¤à¥€à¤•à¥‹ à¤¸à¤à¤—à¥ˆ à¤ªà¥‚à¤œà¤¾ à¤—à¤°à¥à¤¨à¥à¤¹à¥‹à¤¸à¥à¥¤ à¤ªà¤¾à¤°à¤¸à¥à¤ªà¤°à¤¿à¤• à¤¸à¤®à¥à¤®à¤¾à¤¨ à¤…à¤­à¥à¤¯à¤¾à¤¸ à¤—à¤°à¥à¤¨à¥à¤¹à¥‹à¤¸à¥ à¤° à¤à¤•à¤…à¤°à¥à¤•à¤¾à¤²à¤¾à¤ˆ à¤¹à¤¾à¤µà¥€ à¤—à¤°à¥à¤¨ à¤¨à¤—à¤°à¥à¤¨à¥à¤¹à¥‹à¤¸à¥à¥¤ à¤¶à¥à¤•à¥à¤°à¤¬à¤¾à¤° à¤µà¥à¤°à¤¤ à¤° à¤¸à¥‡à¤¤à¥‹ à¤µà¤¸à¥à¤¤à¥à¤¹à¤°à¥‚à¤•à¥‹ à¤¦à¤¾à¤¨à¤¦à¥à¤µà¤¾à¤°à¤¾ à¤¶à¥à¤•à¥à¤°à¤²à¤¾à¤ˆ à¤¬à¤²à¤¿à¤¯à¥‹ à¤¬à¤¨à¤¾à¤‰à¤¨à¥à¤¹à¥‹à¤¸à¥à¥¤"),

    // Tara Detailed
    TARA_REMEDY_LOW("Remedy: Perform Nakshatra Shanti puja for both birth stars. Donate black sesame, iron, and blue cloth on Saturdays. Chant Maha Mrityunjaya Mantra 108 times daily for 40 days to neutralize destiny obstacles.", "à¤‰à¤ªà¤¾à¤¯: à¤¦à¥à¤µà¥ˆ à¤œà¤¨à¥à¤® à¤¨à¤•à¥à¤·à¤¤à¥à¤°à¤•à¥‹ à¤²à¤¾à¤—à¤¿ à¤¨à¤•à¥à¤·à¤¤à¥à¤° à¤¶à¤¾à¤¨à¥à¤¤à¤¿ à¤ªà¥‚à¤œà¤¾ à¤—à¤°à¥à¤¨à¥à¤¹à¥‹à¤¸à¥à¥¤ à¤¶à¤¨à¤¿à¤¬à¤¾à¤°à¤®à¤¾ à¤•à¤¾à¤²à¥‹ à¤¤à¤¿à¤², à¤«à¤²à¤¾à¤® à¤° à¤¨à¥€à¤²à¥‹ à¤•à¤ªà¤¡à¤¾ à¤¦à¤¾à¤¨ à¤—à¤°à¥à¤¨à¥à¤¹à¥‹à¤¸à¥à¥¤ à¤­à¤¾à¤—à¥à¤¯ à¤¬à¤¾à¤§à¤¾à¤¹à¤°à¥‚ à¤¨à¤¿à¤·à¥à¤•à¥à¤°à¤¿à¤¯ à¤—à¤°à¥à¤¨ à¥ªà¥¦ à¤¦à¤¿à¤¨ à¤¦à¥ˆà¤¨à¤¿à¤• à¥§à¥¦à¥® à¤ªà¤Ÿà¤• à¤®à¤¹à¤¾ à¤®à¥ƒà¤¤à¥à¤¯à¥à¤žà¥à¤œà¤¯ à¤®à¤¨à¥à¤¤à¥à¤° à¤œà¤¾à¤ª à¤—à¤°à¥à¤¨à¥à¤¹à¥‹à¤¸à¥à¥¤"),

    // Yoni Detailed
    YONI_REMEDY_LOW("Remedy: Worship Kamadeva (God of Love) on Fridays. Offer jasmine flowers, rose water, and sandalwood. Practice physical affection and conscious intimacy. Strengthen Venus by wearing diamond or white sapphire after proper consultation.", "à¤‰à¤ªà¤¾à¤¯: à¤¶à¥à¤•à¥à¤°à¤¬à¤¾à¤°à¤®à¤¾ à¤•à¤¾à¤®à¤¦à¥‡à¤µ (à¤ªà¥à¤°à¥‡à¤®à¤•à¥‹ à¤¦à¥‡à¤µà¤¤à¤¾) à¤ªà¥‚à¤œà¤¾ à¤—à¤°à¥à¤¨à¥à¤¹à¥‹à¤¸à¥à¥¤ à¤œà¥à¤ˆà¤•à¥‹ à¤«à¥‚à¤², à¤—à¥à¤²à¤¾à¤¬à¤•à¥‹ à¤ªà¤¾à¤¨à¥€ à¤° à¤šà¤¨à¥à¤¦à¤¨ à¤šà¤¢à¤¾à¤‰à¤¨à¥à¤¹à¥‹à¤¸à¥à¥¤ à¤¶à¤¾à¤°à¥€à¤°à¤¿à¤• à¤¸à¥à¤¨à¥‡à¤¹ à¤° à¤¸à¤šà¥‡à¤¤ à¤…à¤¨à¥à¤¤à¤°à¤‚à¤—à¤¤à¤¾ à¤…à¤­à¥à¤¯à¤¾à¤¸ à¤—à¤°à¥à¤¨à¥à¤¹à¥‹à¤¸à¥à¥¤ à¤‰à¤šà¤¿à¤¤ à¤ªà¤°à¤¾à¤®à¤°à¥à¤¶ à¤ªà¤›à¤¿ à¤¹à¥€à¤°à¤¾ à¤µà¤¾ à¤¸à¥‡à¤¤à¥‹ à¤¨à¥€à¤²à¤® à¤²à¤—à¤¾à¤à¤° à¤¶à¥à¤•à¥à¤°à¤²à¤¾à¤ˆ à¤¬à¤²à¤¿à¤¯à¥‹ à¤¬à¤¨à¤¾à¤‰à¤¨à¥à¤¹à¥‹à¤¸à¥à¥¤"),

    // Graha Maitri Detailed
    GRAHA_MAITRI_REMEDY_LOW("Remedy: Meditate together daily for at least 15 minutes. Practice active listening and clear communication. Strengthen Mercury by chanting Budh mantra on Wednesdays, donating green items, and wearing emerald after consultation.", "à¤‰à¤ªà¤¾à¤¯: à¤¦à¥ˆà¤¨à¤¿à¤• à¤•à¤®à¥à¤¤à¤¿à¤®à¤¾ à¥§à¥« à¤®à¤¿à¤¨à¥‡à¤Ÿ à¤¸à¤à¤—à¥ˆ à¤§à¥à¤¯à¤¾à¤¨ à¤—à¤°à¥à¤¨à¥à¤¹à¥‹à¤¸à¥à¥¤ à¤¸à¤•à¥à¤°à¤¿à¤¯ à¤¸à¥à¤¨à¤¾à¤‡ à¤° à¤¸à¥à¤ªà¤·à¥à¤Ÿ à¤¸à¤žà¥à¤šà¤¾à¤° à¤…à¤­à¥à¤¯à¤¾à¤¸ à¤—à¤°à¥à¤¨à¥à¤¹à¥‹à¤¸à¥à¥¤ à¤¬à¥à¤§à¤¬à¤¾à¤°à¤®à¤¾ à¤¬à¥à¤§ à¤®à¤¨à¥à¤¤à¥à¤° à¤œà¤¾à¤ª à¤—à¤°à¥‡à¤°, à¤¹à¤°à¤¿à¤¯à¥‹ à¤µà¤¸à¥à¤¤à¥à¤¹à¤°à¥‚ à¤¦à¤¾à¤¨ à¤—à¤°à¥‡à¤° à¤° à¤ªà¤°à¤¾à¤®à¤°à¥à¤¶ à¤ªà¤›à¤¿ à¤ªà¤¨à¥à¤¨à¤¾ à¤²à¤—à¤¾à¤à¤° à¤¬à¥à¤§à¤²à¤¾à¤ˆ à¤¬à¤²à¤¿à¤¯à¥‹ à¤¬à¤¨à¤¾à¤‰à¤¨à¥à¤¹à¥‹à¤¸à¥à¥¤"),

    // Gana Detailed
    GANA_REMEDY_LOW("Remedy: Chant Ganapati Atharvasheersham daily for 41 days. Perform joint Ganesh puja every month. Practice patience, tolerance, and conscious communication to bridge temperamental differences. Couples counseling may be beneficial.", "à¤‰à¤ªà¤¾à¤¯: à¥ªà¥§ à¤¦à¤¿à¤¨ à¤¦à¥ˆà¤¨à¤¿à¤• à¤—à¤£à¤ªà¤¤à¤¿ à¤…à¤¥à¤°à¥à¤µà¤¶à¥€à¤°à¥à¤·à¤®à¥ à¤œà¤¾à¤ª à¤—à¤°à¥à¤¨à¥à¤¹à¥‹à¤¸à¥à¥¤ à¤ªà¥à¤°à¤¤à¥à¤¯à¥‡à¤• à¤®à¤¹à¤¿à¤¨à¤¾ à¤¸à¤‚à¤¯à¥à¤•à¥à¤¤ à¤—à¤£à¥‡à¤¶ à¤ªà¥‚à¤œà¤¾ à¤—à¤°à¥à¤¨à¥à¤¹à¥‹à¤¸à¥à¥¤ à¤¸à¥à¤µà¤­à¤¾à¤µà¤—à¤¤ à¤­à¤¿à¤¨à¥à¤¨à¤¤à¤¾à¤¹à¤°à¥‚ à¤ªà¤¾à¤Ÿà¥à¤¨ à¤§à¥ˆà¤°à¥à¤¯, à¤¸à¤¹à¤¨à¤¶à¥€à¤²à¤¤à¤¾ à¤° à¤¸à¤šà¥‡à¤¤ à¤¸à¤žà¥à¤šà¤¾à¤° à¤…à¤­à¥à¤¯à¤¾à¤¸ à¤—à¤°à¥à¤¨à¥à¤¹à¥‹à¤¸à¥à¥¤ à¤œà¥‹à¤¡à¥€ à¤ªà¤°à¤¾à¤®à¤°à¥à¤¶ à¤²à¤¾à¤­à¤¦à¤¾à¤¯à¤• à¤¹à¥à¤¨ à¤¸à¤•à¥à¤›à¥¤"),

    // Bhakoot Detailed
    BHAKOOT_REMEDY_LOW("Remedy: Perform Graha Shanti puja for Moon sign lords of both partners. Chant Vishnu Sahasranama on Thursdays for 21 weeks. Donate white items, rice, and silver on Mondays. Financial planning together is essential.", "à¤‰à¤ªà¤¾à¤¯: à¤¦à¥à¤µà¥ˆ à¤¸à¤¾à¤à¥‡à¤¦à¤¾à¤°à¤•à¥‹ à¤šà¤¨à¥à¤¦à¥à¤° à¤°à¤¾à¤¶à¤¿ à¤¸à¥à¤µà¤¾à¤®à¥€à¤•à¥‹ à¤—à¥à¤°à¤¹ à¤¶à¤¾à¤¨à¥à¤¤à¤¿ à¤ªà¥‚à¤œà¤¾ à¤—à¤°à¥à¤¨à¥à¤¹à¥‹à¤¸à¥à¥¤ à¥¨à¥§ à¤¹à¤ªà¥à¤¤à¤¾ à¤¬à¤¿à¤¹à¥€à¤¬à¤¾à¤°à¤®à¤¾ à¤µà¤¿à¤·à¥à¤£à¥ à¤¸à¤¹à¤¸à¥à¤°à¤¨à¤¾à¤® à¤œà¤¾à¤ª à¤—à¤°à¥à¤¨à¥à¤¹à¥‹à¤¸à¥à¥¤ à¤¸à¥‹à¤®à¤¬à¤¾à¤°à¤®à¤¾ à¤¸à¥‡à¤¤à¤¾ à¤µà¤¸à¥à¤¤à¥à¤¹à¤°à¥‚, à¤šà¤¾à¤®à¤² à¤° à¤šà¤¾à¤à¤¦à¥€ à¤¦à¤¾à¤¨ à¤—à¤°à¥à¤¨à¥à¤¹à¥‹à¤¸à¥à¥¤ à¤¸à¤à¤—à¥ˆ à¤µà¤¿à¤¤à¥à¤¤à¥€à¤¯ à¤¯à¥‹à¤œà¤¨à¤¾ à¤†à¤µà¤¶à¥à¤¯à¤• à¤›à¥¤"),

    // Nadi Detailed
    NADI_REMEDY_LOW("Remedy: This is critical - consult qualified astrologer immediately. Perform Maha Mrityunjaya Jaap (108 times daily for 40 days). Donate grains, gold, or cow on auspicious day. Worship Lord Shiva and Goddess Parvati together on Mondays. Consider Kumbh Vivah if dosha is severe.", "à¤‰à¤ªà¤¾à¤¯: à¤¯à¥‹ à¤®à¤¹à¤¤à¥à¤¤à¥à¤µà¤ªà¥‚à¤°à¥à¤£ à¤› - à¤¤à¥à¤°à¥à¤¨à¥à¤¤ à¤¯à¥‹à¤—à¥à¤¯ à¤œà¥à¤¯à¥‹à¤¤à¤¿à¤·à¥€à¤¸à¤à¤— à¤ªà¤°à¤¾à¤®à¤°à¥à¤¶ à¤—à¤°à¥à¤¨à¥à¤¹à¥‹à¤¸à¥à¥¤ à¤®à¤¹à¤¾ à¤®à¥ƒà¤¤à¥à¤¯à¥à¤žà¥à¤œà¤¯ à¤œà¤¾à¤ª à¤—à¤°à¥à¤¨à¥à¤¹à¥‹à¤¸à¥ (à¥ªà¥¦ à¤¦à¤¿à¤¨ à¤¦à¥ˆà¤¨à¤¿à¤• à¥§à¥¦à¥® à¤ªà¤Ÿà¤•)à¥¤ à¤¶à¥à¤­ à¤¦à¤¿à¤¨à¤®à¤¾ à¤…à¤¨à¥à¤¨, à¤¸à¥à¤¨ à¤µà¤¾ à¤—à¤¾à¤ˆ à¤¦à¤¾à¤¨ à¤—à¤°à¥à¤¨à¥à¤¹à¥‹à¤¸à¥à¥¤ à¤¸à¥‹à¤®à¤¬à¤¾à¤°à¤®à¤¾ à¤­à¤—à¤µà¤¾à¤¨ à¤¶à¤¿à¤µ à¤° à¤¦à¥‡à¤µà¥€ à¤ªà¤¾à¤°à¥à¤µà¤¤à¥€à¤•à¥‹ à¤¸à¤à¤—à¥ˆ à¤ªà¥‚à¤œà¤¾ à¤—à¤°à¥à¤¨à¥à¤¹à¥‹à¤¸à¥à¥¤ à¤¦à¥‹à¤· à¤—à¤®à¥à¤­à¥€à¤° à¤­à¤à¤®à¤¾ à¤•à¥à¤®à¥à¤­ à¤µà¤¿à¤µà¤¾à¤¹ à¤µà¤¿à¤šà¤¾à¤° à¤—à¤°à¥à¤¨à¥à¤¹à¥‹à¤¸à¥à¥¤"),

    // ============================================
    // MATCHMAKING - EXPANDED GUNA ANALYSIS
    // ============================================
    // Varna Expanded
    VARNA_IMPORTANCE("Varna represents the spiritual evolution and work nature of the couple. It measures ego compatibility and mutual respect in the relationship.", "à¤µà¤°à¥à¤£à¤²à¥‡ à¤œà¥‹à¤¡à¥€à¤•à¥‹ à¤†à¤§à¥à¤¯à¤¾à¤¤à¥à¤®à¤¿à¤• à¤µà¤¿à¤•à¤¾à¤¸ à¤° à¤•à¤¾à¤°à¥à¤¯ à¤ªà¥à¤°à¤•à¥ƒà¤¤à¤¿ à¤ªà¥à¤°à¤¤à¤¿à¤¨à¤¿à¤§à¤¿à¤¤à¥à¤µ à¤—à¤°à¥à¤¦à¤›à¥¤ à¤¯à¤¸à¤²à¥‡ à¤¸à¤®à¥à¤¬à¤¨à¥à¤§à¤®à¤¾ à¤…à¤¹à¤‚à¤•à¤¾à¤° à¤…à¤¨à¥à¤•à¥‚à¤²à¤¤à¤¾ à¤° à¤ªà¤¾à¤°à¤¸à¥à¤ªà¤°à¤¿à¤• à¤¸à¤®à¥à¤®à¤¾à¤¨ à¤®à¤¾à¤ªà¤¨ à¤—à¤°à¥à¤¦à¤›à¥¤"),
    VARNA_BRAHMIN_NATURE("Brahmin (Fire signs): Knowledge seekers, intellectual, teaching orientation, spiritual inclinations", "à¤¬à¥à¤°à¤¾à¤¹à¥à¤®à¤£ (à¤…à¤—à¥à¤¨à¤¿ à¤°à¤¾à¤¶à¤¿): à¤œà¥à¤žà¤¾à¤¨ à¤–à¥‹à¤œà¥à¤¨à¥‡, à¤¬à¥Œà¤¦à¥à¤§à¤¿à¤•, à¤¶à¤¿à¤•à¥à¤·à¤£ à¤…à¤­à¤¿à¤®à¥à¤–à¥€à¤•à¤°à¤£, à¤†à¤§à¥à¤¯à¤¾à¤¤à¥à¤®à¤¿à¤• à¤à¥à¤•à¤¾à¤µ"),
    VARNA_KSHATRIYA_NATURE("Kshatriya (Earth signs): Leaders, protectors, administrative skills, power-oriented", "à¤•à¥à¤·à¤¤à¥à¤°à¤¿à¤¯ (à¤ªà¥ƒà¤¥à¥à¤µà¥€ à¤°à¤¾à¤¶à¤¿): à¤¨à¥‡à¤¤à¤¾, à¤°à¤•à¥à¤·à¤•, à¤ªà¥à¤°à¤¶à¤¾à¤¸à¤¨à¤¿à¤• à¤•à¥Œà¤¶à¤², à¤¶à¤•à¥à¤¤à¤¿-à¤‰à¤¨à¥à¤®à¥à¤–"),
    VARNA_VAISHYA_NATURE("Vaishya (Air signs): Business minded, commerce, communication, wealth creation", "à¤µà¥ˆà¤¶à¥à¤¯ (à¤µà¤¾à¤¯à¥ à¤°à¤¾à¤¶à¤¿): à¤µà¥à¤¯à¤¾à¤ªà¤¾à¤° à¤®à¤¾à¤¨à¤¸à¤¿à¤•à¤¤à¤¾, à¤µà¤¾à¤£à¤¿à¤œà¥à¤¯, à¤¸à¤žà¥à¤šà¤¾à¤°, à¤§à¤¨ à¤¸à¤¿à¤°à¥à¤œà¤¨à¤¾"),
    VARNA_SHUDRA_NATURE("Shudra (Water signs): Service orientation, emotional depth, nurturing, supportive", "à¤¶à¥‚à¤¦à¥à¤° (à¤œà¤² à¤°à¤¾à¤¶à¤¿): à¤¸à¥‡à¤µà¤¾ à¤…à¤­à¤¿à¤®à¥à¤–à¥€à¤•à¤°à¤£, à¤­à¤¾à¤µà¤¨à¤¾à¤¤à¥à¤®à¤• à¤—à¤¹à¤¿à¤°à¤¾à¤ˆ, à¤ªà¤¾à¤²à¤¨-à¤ªà¥‹à¤·à¤£, à¤¸à¤¹à¤¯à¥‹à¤—à¥€"),

    // Vashya Expanded
    VASHYA_IMPORTANCE("Vashya indicates natural attraction, influence capacity, and control dynamics. It shows who holds sway in decision-making and emotional matters.", "à¤µà¤¶à¥à¤¯à¤²à¥‡ à¤ªà¥à¤°à¤¾à¤•à¥ƒà¤¤à¤¿à¤• à¤†à¤•à¤°à¥à¤·à¤£, à¤ªà¥à¤°à¤­à¤¾à¤µ à¤•à¥à¤·à¤®à¤¤à¤¾ à¤° à¤¨à¤¿à¤¯à¤¨à¥à¤¤à¥à¤°à¤£ à¤—à¤¤à¤¿à¤¶à¥€à¤²à¤¤à¤¾ à¤¸à¤‚à¤•à¥‡à¤¤ à¤—à¤°à¥à¤¦à¤›à¥¤ à¤¯à¤¸à¤²à¥‡ à¤¨à¤¿à¤°à¥à¤£à¤¯ à¤²à¤¿à¤¨à¥‡ à¤° à¤­à¤¾à¤µà¤¨à¤¾à¤¤à¥à¤®à¤• à¤®à¤¾à¤®à¤¿à¤²à¤¾à¤¹à¤°à¥‚à¤®à¤¾ à¤•à¤¸à¤²à¥‡ à¤ªà¥à¤°à¤­à¤¾à¤µ à¤°à¤¾à¤–à¥à¤› à¤­à¤¨à¥‡à¤° à¤¦à¥‡à¤–à¤¾à¤‰à¤à¤›à¥¤"),
    VASHYA_POWER_BALANCE("Balanced Vashya creates equal partnership. Imbalance may lead to dominance issues but can work if both partners accept their natural roles.", "à¤¸à¤¨à¥à¤¤à¥à¤²à¤¿à¤¤ à¤µà¤¶à¥à¤¯à¤²à¥‡ à¤¸à¤®à¤¾à¤¨ à¤¸à¤¾à¤à¥‡à¤¦à¤¾à¤°à¥€ à¤¸à¤¿à¤°à¥à¤œà¤¨à¤¾ à¤—à¤°à¥à¤¦à¤›à¥¤ à¤…à¤¸à¤¨à¥à¤¤à¥à¤²à¤¨à¤²à¥‡ à¤ªà¥à¤°à¤­à¥à¤¤à¥à¤µ à¤¸à¤®à¤¸à¥à¤¯à¤¾ à¤²à¥à¤¯à¤¾à¤‰à¤¨ à¤¸à¤•à¥à¤› à¤¤à¤° à¤¦à¥à¤µà¥ˆ à¤¸à¤¾à¤à¥‡à¤¦à¤¾à¤°à¤¹à¤°à¥‚à¤²à¥‡ à¤†à¤«à¥à¤¨à¥‹ à¤ªà¥à¤°à¤¾à¤•à¥ƒà¤¤à¤¿à¤• à¤­à¥‚à¤®à¤¿à¤•à¤¾ à¤¸à¥à¤µà¥€à¤•à¤¾à¤° à¤—à¤°à¥‡ à¤•à¤¾à¤® à¤—à¤°à¥à¤¨ à¤¸à¤•à¥à¤›à¥¤"),

    // Tara Expanded
    TARA_IMPORTANCE("Tara analyzes birth star compatibility to determine destiny harmony. It predicts how well the couple's life paths align and support each other.", "à¤¤à¤¾à¤°à¤¾ à¤­à¤¾à¤—à¥à¤¯ à¤¸à¤¾à¤®à¤žà¥à¤œà¤¸à¥à¤¯ à¤¨à¤¿à¤°à¥à¤§à¤¾à¤°à¤£ à¤—à¤°à¥à¤¨ à¤œà¤¨à¥à¤® à¤¤à¤¾à¤°à¤¾ à¤…à¤¨à¥à¤•à¥‚à¤²à¤¤à¤¾ à¤µà¤¿à¤¶à¥à¤²à¥‡à¤·à¤£ à¤—à¤°à¥à¤¦à¤›à¥¤ à¤¯à¤¸à¤²à¥‡ à¤œà¥‹à¤¡à¥€à¤•à¥‹ à¤œà¥€à¤µà¤¨ à¤®à¤¾à¤°à¥à¤—à¤¹à¤°à¥‚ à¤•à¤¤à¥à¤¤à¤¿à¤•à¥‹ à¤°à¤¾à¤®à¥à¤°à¥‹à¤¸à¤à¤— à¤®à¤¿à¤²à¥à¤›à¤¨à¥ à¤° à¤à¤•à¤…à¤°à¥à¤•à¤¾à¤²à¤¾à¤ˆ à¤¸à¤®à¤°à¥à¤¥à¤¨ à¤—à¤°à¥à¤›à¤¨à¥ à¤­à¤¨à¥‡à¤° à¤­à¤µà¤¿à¤·à¥à¤¯à¤µà¤¾à¤£à¥€ à¤—à¤°à¥à¤¦à¤›à¥¤"),
    TARA_CATEGORIES_EXPLAINED("Nakshatras are grouped in sets of 9 from each person's birth star. Janma, Vipat, and Vadha taras are inauspicious; Sampat, Kshema, Sadhana, and Mitra taras are auspicious.", "à¤¨à¤•à¥à¤·à¤¤à¥à¤°à¤¹à¤°à¥‚ à¤ªà¥à¤°à¤¤à¥à¤¯à¥‡à¤• à¤µà¥à¤¯à¤•à¥à¤¤à¤¿à¤•à¥‹ à¤œà¤¨à¥à¤® à¤¤à¤¾à¤°à¤¾à¤¬à¤¾à¤Ÿ à¥¯ à¤•à¥‹ à¤¸à¥‡à¤Ÿà¤®à¤¾ à¤¸à¤®à¥‚à¤¹à¥€à¤•à¥ƒà¤¤ à¤—à¤°à¤¿à¤¨à¥à¤›à¥¤ à¤œà¤¨à¥à¤®, à¤µà¤¿à¤ªà¤¤à¥ à¤° à¤µà¤§ à¤¤à¤¾à¤°à¤¾ à¤…à¤¶à¥à¤­ à¤¹à¥à¤¨à¥; à¤¸à¤®à¥à¤ªà¤¤à¥, à¤•à¥à¤·à¥‡à¤®, à¤¸à¤¾à¤§à¤¨à¤¾ à¤° à¤®à¤¿à¤¤à¥à¤° à¤¤à¤¾à¤°à¤¾ à¤¶à¥à¤­ à¤¹à¥à¤¨à¥à¥¤"),

    // Yoni Expanded
    YONI_IMPORTANCE("Yoni represents physical, sexual, and biological compatibility. It's based on animal nature of birth nakshatras and indicates instinctual harmony.", "à¤¯à¥‹à¤¨à¤¿à¤²à¥‡ à¤¶à¤¾à¤°à¥€à¤°à¤¿à¤•, à¤¯à¥Œà¤¨ à¤° à¤œà¥ˆà¤µà¤¿à¤• à¤…à¤¨à¥à¤•à¥‚à¤²à¤¤à¤¾ à¤ªà¥à¤°à¤¤à¤¿à¤¨à¤¿à¤§à¤¿à¤¤à¥à¤µ à¤—à¤°à¥à¤¦à¤›à¥¤ à¤¯à¥‹ à¤œà¤¨à¥à¤® à¤¨à¤•à¥à¤·à¤¤à¥à¤°à¤•à¥‹ à¤ªà¤¶à¥ à¤ªà¥à¤°à¤•à¥ƒà¤¤à¤¿à¤®à¤¾ à¤†à¤§à¤¾à¤°à¤¿à¤¤ à¤› à¤° à¤¸à¤¹à¤œ à¤¸à¤¾à¤®à¤žà¥à¤œà¤¸à¥à¤¯ à¤¸à¤‚à¤•à¥‡à¤¤ à¤—à¤°à¥à¤¦à¤›à¥¤"),
    YONI_RELATIONSHIPS_EXPLAINED("Same yoni = Perfect (4 points). Friendly yonis = Very Good (3-4 points). Neutral = Moderate (2 points). Enemy yonis = Poor (0-1 points).", "à¤‰à¤¹à¥€ à¤¯à¥‹à¤¨à¤¿ = à¤‰à¤¤à¥à¤¤à¤® (à¥ª à¤…à¤‚à¤•)à¥¤ à¤®à¤¿à¤¤à¥à¤° à¤¯à¥‹à¤¨à¤¿ = à¤§à¥‡à¤°à¥ˆ à¤°à¤¾à¤®à¥à¤°à¥‹ (à¥©-à¥ª à¤…à¤‚à¤•)à¥¤ à¤¤à¤Ÿà¤¸à¥à¤¥ = à¤®à¤§à¥à¤¯à¤® (à¥¨ à¤…à¤‚à¤•)à¥¤ à¤¶à¤¤à¥à¤°à¥ à¤¯à¥‹à¤¨à¤¿ = à¤•à¤®à¤œà¥‹à¤° (à¥¦-à¥§ à¤…à¤‚à¤•)à¥¤"),

    // Graha Maitri Expanded
    GRAHA_MAITRI_IMPORTANCE("Graha Maitri examines mental compatibility through planetary friendship of Moon sign lords. It indicates intellectual rapport and communication ease.", "à¤—à¥à¤°à¤¹ à¤®à¥ˆà¤¤à¥à¤°à¥€à¤²à¥‡ à¤šà¤¨à¥à¤¦à¥à¤° à¤°à¤¾à¤¶à¤¿ à¤¸à¥à¤µà¤¾à¤®à¥€à¤¹à¤°à¥‚à¤•à¥‹ à¤—à¥à¤°à¤¹ à¤®à¤¿à¤¤à¥à¤°à¤¤à¤¾à¤¦à¥à¤µà¤¾à¤°à¤¾ à¤®à¤¾à¤¨à¤¸à¤¿à¤• à¤…à¤¨à¥à¤•à¥‚à¤²à¤¤à¤¾ à¤œà¤¾à¤à¤š à¤—à¤°à¥à¤¦à¤›à¥¤ à¤¯à¤¸à¤²à¥‡ à¤¬à¥Œà¤¦à¥à¤§à¤¿à¤• à¤¸à¤®à¥à¤¬à¤¨à¥à¤§ à¤° à¤¸à¤žà¥à¤šà¤¾à¤° à¤¸à¤¹à¤œà¤¤à¤¾ à¤¸à¤‚à¤•à¥‡à¤¤ à¤—à¤°à¥à¤¦à¤›à¥¤"),
    GRAHA_MAITRI_LEVELS("Same lord/Friends = 5 points. One friend + one neutral = 4 points. Both neutral = 3 points. One enemy = 1-2 points. Both enemies = 0.5 points.", "à¤‰à¤¹à¥€ à¤¸à¥à¤µà¤¾à¤®à¥€/à¤®à¤¿à¤¤à¥à¤° = à¥« à¤…à¤‚à¤•à¥¤ à¤à¤• à¤®à¤¿à¤¤à¥à¤° + à¤à¤• à¤¤à¤Ÿà¤¸à¥à¤¥ = à¥ª à¤…à¤‚à¤•à¥¤ à¤¦à¥à¤µà¥ˆ à¤¤à¤Ÿà¤¸à¥à¤¥ = à¥© à¤…à¤‚à¤•à¥¤ à¤à¤• à¤¶à¤¤à¥à¤°à¥ = à¥§-à¥¨ à¤…à¤‚à¤•à¥¤ à¤¦à¥à¤µà¥ˆ à¤¶à¤¤à¥à¤°à¥ = à¥¦.à¥« à¤…à¤‚à¤•à¥¤"),

    // Gana Expanded
    GANA_IMPORTANCE("Gana measures behavioral temperament and nature. It indicates how partners will behave in conflicts and daily life situations.", "à¤—à¤£à¤²à¥‡ à¤µà¥à¤¯à¤µà¤¹à¤¾à¤°à¤¿à¤• à¤¸à¥à¤µà¤­à¤¾à¤µ à¤° à¤ªà¥à¤°à¤•à¥ƒà¤¤à¤¿ à¤®à¤¾à¤ªà¤¨ à¤—à¤°à¥à¤¦à¤›à¥¤ à¤¯à¤¸à¤²à¥‡ à¤¸à¤¾à¤à¥‡à¤¦à¤¾à¤°à¤¹à¤°à¥‚à¤²à¥‡ à¤¦à¥à¤µà¤¨à¥à¤¦à¥à¤µ à¤° à¤¦à¥ˆà¤¨à¤¿à¤• à¤œà¥€à¤µà¤¨ à¤ªà¤°à¤¿à¤¸à¥à¤¥à¤¿à¤¤à¤¿à¤¹à¤°à¥‚à¤®à¤¾ à¤•à¤¸à¤°à¥€ à¤µà¥à¤¯à¤µà¤¹à¤¾à¤° à¤—à¤°à¥à¤›à¤¨à¥ à¤­à¤¨à¥‡à¤° à¤¸à¤‚à¤•à¥‡à¤¤ à¤—à¤°à¥à¤¦à¤›à¥¤"),
    GANA_COMPATIBILITY_DETAILS("Deva-Deva = 6 points (best). Manushya-Manushya = 6 points. Rakshasa-Rakshasa = 6 points. Deva-Manushya = 5 points. Manushya-Rakshasa = 2 points. Deva-Rakshasa = 0 points (worst).", "à¤¦à¥‡à¤µ-à¤¦à¥‡à¤µ = à¥¬ à¤…à¤‚à¤• (à¤‰à¤¤à¥à¤¤à¤®)à¥¤ à¤®à¤¨à¥à¤·à¥à¤¯-à¤®à¤¨à¥à¤·à¥à¤¯ = à¥¬ à¤…à¤‚à¤•à¥¤ à¤°à¤¾à¤•à¥à¤·à¤¸-à¤°à¤¾à¤•à¥à¤·à¤¸ = à¥¬ à¤…à¤‚à¤•à¥¤ à¤¦à¥‡à¤µ-à¤®à¤¨à¥à¤·à¥à¤¯ = à¥« à¤…à¤‚à¤•à¥¤ à¤®à¤¨à¥à¤·à¥à¤¯-à¤°à¤¾à¤•à¥à¤·à¤¸ = à¥¨ à¤…à¤‚à¤•à¥¤ à¤¦à¥‡à¤µ-à¤°à¤¾à¤•à¥à¤·à¤¸ = à¥¦ à¤…à¤‚à¤• (à¤¸à¤¬à¥ˆà¤­à¤¨à¥à¤¦à¤¾ à¤–à¤°à¤¾à¤¬)à¥¤"),

    // Bhakoot Expanded
    BHAKOOT_IMPORTANCE("Bhakoot assesses emotional love, family welfare, and financial prosperity. It's based on sign positions and is crucial for long-term happiness.", "à¤­à¤•à¥à¤Ÿà¤²à¥‡ à¤­à¤¾à¤µà¤¨à¤¾à¤¤à¥à¤®à¤• à¤ªà¥à¤°à¥‡à¤®, à¤ªà¤¾à¤°à¤¿à¤µà¤¾à¤°à¤¿à¤• à¤•à¤²à¥à¤¯à¤¾à¤£ à¤° à¤†à¤°à¥à¤¥à¤¿à¤• à¤¸à¤®à¥ƒà¤¦à¥à¤§à¤¿ à¤®à¥‚à¤²à¥à¤¯à¤¾à¤™à¥à¤•à¤¨ à¤—à¤°à¥à¤¦à¤›à¥¤ à¤¯à¥‹ à¤°à¤¾à¤¶à¤¿ à¤¸à¥à¤¥à¤¿à¤¤à¤¿à¤¹à¤°à¥‚à¤®à¤¾ à¤†à¤§à¤¾à¤°à¤¿à¤¤ à¤› à¤° à¤¦à¥€à¤°à¥à¤˜à¤•à¤¾à¤²à¥€à¤¨ à¤–à¥à¤¶à¥€à¤•à¥‹ à¤²à¤¾à¤—à¤¿ à¤®à¤¹à¤¤à¥à¤¤à¥à¤µà¤ªà¥‚à¤°à¥à¤£ à¤›à¥¤"),
    BHAKOOT_DOSHA_TYPES("No dosha = 7 points. 2-12 position (Dhan-Vyaya dosha) = Financial concerns. 6-8 position (Shadashtak dosha) = Health and separation concerns (most serious).", "à¤•à¥à¤¨à¥ˆ à¤¦à¥‹à¤· à¤›à¥ˆà¤¨ = à¥­ à¤…à¤‚à¤•à¥¤ à¥¨-à¥§à¥¨ à¤¸à¥à¤¥à¤¿à¤¤à¤¿ (à¤§à¤¨-à¤µà¥à¤¯à¤¯ à¤¦à¥‹à¤·) = à¤†à¤°à¥à¤¥à¤¿à¤• à¤šà¤¿à¤¨à¥à¤¤à¤¾à¥¤ à¥¬-à¥® à¤¸à¥à¤¥à¤¿à¤¤à¤¿ (à¤·à¤¡à¤¾à¤·à¥à¤Ÿà¤• à¤¦à¥‹à¤·) = à¤¸à¥à¤µà¤¾à¤¸à¥à¤¥à¥à¤¯ à¤° à¤µà¤¿à¤šà¥à¤›à¥‡à¤¦ à¤šà¤¿à¤¨à¥à¤¤à¤¾ (à¤¸à¤¬à¥ˆà¤­à¤¨à¥à¤¦à¤¾ à¤—à¤®à¥à¤­à¥€à¤°)à¥¤"),

    // Nadi Expanded
    NADI_IMPORTANCE("Nadi is the MOST IMPORTANT guna (8 points) indicating genetic compatibility, health, and progeny. Same Nadi can cause serious health and conception issues.", "à¤¨à¤¾à¤¡à¥€ à¤¸à¤¬à¥ˆà¤­à¤¨à¥à¤¦à¤¾ à¤®à¤¹à¤¤à¥à¤¤à¥à¤µà¤ªà¥‚à¤°à¥à¤£ à¤—à¥à¤£ (à¥® à¤…à¤‚à¤•) à¤¹à¥‹ à¤œà¤¸à¤²à¥‡ à¤†à¤¨à¥à¤µà¤‚à¤¶à¤¿à¤• à¤…à¤¨à¥à¤•à¥‚à¤²à¤¤à¤¾, à¤¸à¥à¤µà¤¾à¤¸à¥à¤¥à¥à¤¯ à¤° à¤¸à¤¨à¥à¤¤à¤¾à¤¨ à¤¸à¤‚à¤•à¥‡à¤¤ à¤—à¤°à¥à¤¦à¤›à¥¤ à¤‰à¤¹à¥€ à¤¨à¤¾à¤¡à¥€à¤²à¥‡ à¤—à¤®à¥à¤­à¥€à¤° à¤¸à¥à¤µà¤¾à¤¸à¥à¤¥à¥à¤¯ à¤° à¤—à¤°à¥à¤­à¤§à¤¾à¤°à¤£ à¤¸à¤®à¤¸à¥à¤¯à¤¾ à¤²à¥à¤¯à¤¾à¤‰à¤¨ à¤¸à¤•à¥à¤›à¥¤"),
    NADI_TYPES_EXPLAINED("Adi (Vata): Wind element, nervous system, movement. Madhya (Pitta): Fire element, digestion, metabolism. Antya (Kapha): Water element, structure, lubrication. Same Nadi = physiological incompatibility.", "à¤†à¤¦à¤¿ (à¤µà¤¾à¤¤): à¤µà¤¾à¤¯à¥ à¤¤à¤¤à¥à¤µ, à¤¸à¥à¤¨à¤¾à¤¯à¥ à¤ªà¥à¤°à¤£à¤¾à¤²à¥€, à¤—à¤¤à¤¿à¥¤ à¤®à¤§à¥à¤¯ (à¤ªà¤¿à¤¤à¥à¤¤): à¤…à¤—à¥à¤¨à¤¿ à¤¤à¤¤à¥à¤µ, à¤ªà¤¾à¤šà¤¨, à¤šà¤¯à¤¾à¤ªà¤šà¤¯à¥¤ à¤…à¤¨à¥à¤¤à¥à¤¯ (à¤•à¤«): à¤œà¤² à¤¤à¤¤à¥à¤µ, à¤¸à¤‚à¤°à¤šà¤¨à¤¾, à¤¸à¥à¤¨à¥‡à¤¹à¤¨à¥¤ à¤‰à¤¹à¥€ à¤¨à¤¾à¤¡à¥€ = à¤¶à¤¾à¤°à¥€à¤°à¤¿à¤• à¤…à¤¸à¤‚à¤—à¤¤à¤¤à¤¾à¥¤"),
    NADI_ZERO_EXPLANATION("Zero points indicates Same Nadi without cancellation - this is the most serious dosha in matchmaking. Immediate astrological consultation and extensive remedies are mandatory.", "à¤¶à¥‚à¤¨à¥à¤¯ à¤…à¤‚à¤•à¤²à¥‡ à¤°à¤¦à¥à¤¦ à¤¬à¤¿à¤¨à¤¾ à¤‰à¤¹à¥€ à¤¨à¤¾à¤¡à¥€ à¤¸à¤‚à¤•à¥‡à¤¤ à¤—à¤°à¥à¤¦à¤› - à¤¯à¥‹ à¤®à¤¿à¤²à¤¾à¤¨à¤®à¤¾ à¤¸à¤¬à¥ˆà¤­à¤¨à¥à¤¦à¤¾ à¤—à¤®à¥à¤­à¥€à¤° à¤¦à¥‹à¤· à¤¹à¥‹à¥¤ à¤¤à¤¤à¥à¤•à¤¾à¤² à¤œà¥à¤¯à¥‹à¤¤à¤¿à¤·à¥€à¤¯ à¤ªà¤°à¤¾à¤®à¤°à¥à¤¶ à¤° à¤µà¥à¤¯à¤¾à¤ªà¤• à¤‰à¤ªà¤¾à¤¯à¤¹à¤°à¥‚ à¤…à¤¨à¤¿à¤µà¤¾à¤°à¥à¤¯ à¤›à¤¨à¥à¥¤"),

    // ============================================
    // MATCHMAKING - SCORE INTERPRETATIONS & RELATIONSHIP GUIDANCE
    // ============================================
    SCORE_EXCELLENT("Excellent match! Score above 28 indicates highly favorable compatibility across all dimensions.", "à¤‰à¤¤à¥à¤•à¥ƒà¤·à¥à¤Ÿ à¤®à¤¿à¤²à¤¾à¤¨! à¥¨à¥® à¤®à¤¾à¤¥à¤¿à¤•à¥‹ à¤…à¤‚à¤•à¤²à¥‡ à¤¸à¤¬à¥ˆ à¤†à¤¯à¤¾à¤®à¤¹à¤°à¥‚à¤®à¤¾ à¤…à¤¤à¥à¤¯à¤¨à¥à¤¤ à¤…à¤¨à¥à¤•à¥‚à¤² à¤…à¤¨à¥à¤•à¥‚à¤²à¤¤à¤¾ à¤¸à¤‚à¤•à¥‡à¤¤ à¤—à¤°à¥à¤¦à¤›à¥¤"),
    SCORE_EXCELLENT_GUIDANCE("Your compatibility across spiritual, mental, physical, and temperamental dimensions is outstanding. You share compatible values, communication styles, and life goals. This union has the blessings of celestial alignments. Focus on maintaining open communication and mutual respect to nurture this harmonious connection throughout your married life.", "à¤¤à¤ªà¤¾à¤ˆà¤‚à¤•à¥‹ à¤†à¤§à¥à¤¯à¤¾à¤¤à¥à¤®à¤¿à¤•, à¤®à¤¾à¤¨à¤¸à¤¿à¤•, à¤¶à¤¾à¤°à¥€à¤°à¤¿à¤• à¤° à¤¸à¥à¤µà¤­à¤¾à¤µà¤—à¤¤ à¤†à¤¯à¤¾à¤®à¤¹à¤°à¥‚à¤®à¤¾ à¤…à¤¨à¥à¤•à¥‚à¤²à¤¤à¤¾ à¤‰à¤¤à¥à¤•à¥ƒà¤·à¥à¤Ÿ à¤›à¥¤ à¤¤à¤ªà¤¾à¤ˆà¤‚ à¤…à¤¨à¥à¤•à¥‚à¤² à¤®à¥‚à¤²à¥à¤¯à¤¹à¤°à¥‚, à¤¸à¤žà¥à¤šà¤¾à¤° à¤¶à¥ˆà¤²à¥€ à¤° à¤œà¥€à¤µà¤¨ à¤²à¤•à¥à¤·à¥à¤¯à¤¹à¤°à¥‚ à¤¸à¤¾à¤à¤¾ à¤—à¤°à¥à¤¨à¥à¤¹à¥à¤¨à¥à¤›à¥¤ à¤¯à¤¸ à¤¸à¤‚à¤˜à¤®à¤¾ à¤†à¤•à¤¾à¤¶à¥€à¤¯ à¤¸à¤‚à¤°à¥‡à¤–à¤£à¤•à¥‹ à¤†à¤¶à¥€à¤°à¥à¤µà¤¾à¤¦ à¤›à¥¤ à¤†à¤«à¥à¤¨à¥‹ à¤µà¥ˆà¤µà¤¾à¤¹à¤¿à¤• à¤œà¥€à¤µà¤¨à¤­à¤° à¤¯à¥‹ à¤¸à¤¾à¤®à¤žà¥à¤œà¤¸à¥à¤¯à¤ªà¥‚à¤°à¥à¤£ à¤¸à¤®à¥à¤¬à¤¨à¥à¤§à¤²à¤¾à¤ˆ à¤ªà¥‹à¤·à¤£ à¤—à¤°à¥à¤¨ à¤–à¥à¤²à¤¾ à¤¸à¤žà¥à¤šà¤¾à¤° à¤° à¤ªà¤¾à¤°à¤¸à¥à¤ªà¤°à¤¿à¤• à¤¸à¤®à¥à¤®à¤¾à¤¨ à¤•à¤¾à¤¯à¤® à¤°à¤¾à¤–à¥à¤¨à¤®à¤¾ à¤§à¥à¤¯à¤¾à¤¨ à¤¦à¤¿à¤¨à¥à¤¹à¥‹à¤¸à¥à¥¤"),

    SCORE_GOOD("Good match. Score of 21-27 suggests strong compatibility with minor areas to work on.", "à¤°à¤¾à¤®à¥à¤°à¥‹ à¤®à¤¿à¤²à¤¾à¤¨à¥¤ à¥¨à¥§-à¥¨à¥­ à¤•à¥‹ à¤…à¤‚à¤•à¤²à¥‡ à¤•à¤¾à¤® à¤—à¤°à¥à¤¨à¥à¤ªà¤°à¥à¤¨à¥‡ à¤¸à¤¾à¤¨à¤¾à¤¤à¤¿à¤¨à¤¾ à¤•à¥à¤·à¥‡à¤¤à¥à¤°à¤¹à¤°à¥‚à¤¸à¤¹à¤¿à¤¤ à¤¬à¤²à¤¿à¤¯à¥‹ à¤…à¤¨à¥à¤•à¥‚à¤²à¤¤à¤¾ à¤¸à¥à¤à¤¾à¤µ à¤¦à¤¿à¤¨à¥à¤›à¥¤"),
    SCORE_GOOD_GUIDANCE("You have strong overall compatibility with good harmony in most areas. While there are some minor differences, these can be easily managed through mutual understanding and effort. The relationship has solid foundations for a successful marriage. Focus on conscious communication, especially in areas where gunas scored lower. Regular discussions about expectations and compromise will strengthen your bond.", "à¤¤à¤ªà¤¾à¤ˆà¤‚ à¤§à¥‡à¤°à¥ˆ à¤•à¥à¤·à¥‡à¤¤à¥à¤°à¤¹à¤°à¥‚à¤®à¤¾ à¤°à¤¾à¤®à¥à¤°à¥‹ à¤¸à¤¾à¤®à¤žà¥à¤œà¤¸à¥à¤¯à¤¸à¤¹à¤¿à¤¤ à¤¬à¤²à¤¿à¤¯à¥‹ à¤¸à¤®à¤—à¥à¤° à¤…à¤¨à¥à¤•à¥‚à¤²à¤¤à¤¾ à¤°à¤¾à¤–à¥à¤¨à¥à¤¹à¥à¤¨à¥à¤›à¥¤ à¤¯à¤¦à¥à¤¯à¤ªà¤¿ à¤•à¥‡à¤¹à¥€ à¤¸à¤¾à¤¨à¤¾à¤¤à¤¿à¤¨à¤¾ à¤­à¤¿à¤¨à¥à¤¨à¤¤à¤¾à¤¹à¤°à¥‚ à¤›à¤¨à¥, à¤¯à¥€ à¤ªà¤¾à¤°à¤¸à¥à¤ªà¤°à¤¿à¤• à¤¬à¥à¤à¤¾à¤‡ à¤° à¤ªà¥à¤°à¤¯à¤¾à¤¸à¤¦à¥à¤µà¤¾à¤°à¤¾ à¤¸à¤œà¤¿à¤²à¥ˆà¤¸à¤à¤— à¤µà¥à¤¯à¤µà¤¸à¥à¤¥à¤¾à¤ªà¤¨ à¤—à¤°à¥à¤¨ à¤¸à¤•à¤¿à¤¨à¥à¤›à¥¤ à¤¸à¤®à¥à¤¬à¤¨à¥à¤§à¤®à¤¾ à¤¸à¤«à¤² à¤µà¤¿à¤µà¤¾à¤¹à¤•à¥‹ à¤²à¤¾à¤—à¤¿ à¤ à¥‹à¤¸ à¤†à¤§à¤¾à¤°à¤¹à¤°à¥‚ à¤›à¤¨à¥à¥¤ à¤¸à¤šà¥‡à¤¤ à¤¸à¤žà¥à¤šà¤¾à¤°à¤®à¤¾ à¤§à¥à¤¯à¤¾à¤¨ à¤¦à¤¿à¤¨à¥à¤¹à¥‹à¤¸à¥, à¤µà¤¿à¤¶à¥‡à¤· à¤—à¤°à¥€ à¤¤à¥€ à¤•à¥à¤·à¥‡à¤¤à¥à¤°à¤¹à¤°à¥‚à¤®à¤¾ à¤œà¤¹à¤¾à¤ à¤—à¥à¤£à¤¹à¤°à¥‚à¤²à¥‡ à¤•à¤® à¤…à¤‚à¤• à¤ªà¤¾à¤à¤•à¥‹ à¤›à¥¤ à¤…à¤ªà¥‡à¤•à¥à¤·à¤¾à¤¹à¤°à¥‚ à¤° à¤¸à¤®à¥à¤à¥Œà¤¤à¤¾à¤•à¥‹ à¤¬à¤¾à¤°à¥‡à¤®à¤¾ à¤¨à¤¿à¤¯à¤®à¤¿à¤¤ à¤›à¤²à¤«à¤²à¤²à¥‡ à¤¤à¤ªà¤¾à¤ˆà¤‚à¤•à¥‹ à¤¬à¤¨à¥à¤§à¤¨à¤²à¤¾à¤ˆ à¤¬à¤²à¤¿à¤¯à¥‹ à¤¬à¤¨à¤¾à¤‰à¤¨à¥‡à¤›à¥¤"),

    SCORE_AVERAGE("Average compatibility. Score of 18-20 requires attention to problem areas and remedies.", "à¤”à¤¸à¤¤ à¤…à¤¨à¥à¤•à¥‚à¤²à¤¤à¤¾à¥¤ à¥§à¥®-à¥¨à¥¦ à¤•à¥‹ à¤…à¤‚à¤•à¤²à¥‡ à¤¸à¤®à¤¸à¥à¤¯à¤¾ à¤•à¥à¤·à¥‡à¤¤à¥à¤°à¤¹à¤°à¥‚ à¤° à¤‰à¤ªà¤¾à¤¯à¤¹à¤°à¥‚à¤®à¤¾ à¤§à¥à¤¯à¤¾à¤¨ à¤†à¤µà¤¶à¥à¤¯à¤• à¤ªà¤°à¥à¤›à¥¤"),
    SCORE_AVERAGE_GUIDANCE("Your compatibility is moderate with several areas requiring conscious effort and adjustment. While the match is acceptable, success depends on both partners' willingness to work through differences. Identify specific problem areas (especially low-scoring gunas) and actively address them. Consider performing recommended Vedic remedies to strengthen weak areas. Pre-marital counseling is strongly advised to establish communication patterns and conflict resolution strategies. With dedication and mutual respect, this relationship can thrive.", "à¤¤à¤ªà¤¾à¤ˆà¤‚à¤•à¥‹ à¤…à¤¨à¥à¤•à¥‚à¤²à¤¤à¤¾ à¤¸à¤šà¥‡à¤¤ à¤ªà¥à¤°à¤¯à¤¾à¤¸ à¤° à¤¸à¤®à¤¾à¤¯à¥‹à¤œà¤¨ à¤†à¤µà¤¶à¥à¤¯à¤• à¤ªà¤°à¥à¤¨à¥‡ à¤§à¥‡à¤°à¥ˆ à¤•à¥à¤·à¥‡à¤¤à¥à¤°à¤¹à¤°à¥‚à¤¸à¤¹à¤¿à¤¤ à¤®à¤§à¥à¤¯à¤® à¤›à¥¤ à¤®à¤¿à¤²à¤¾à¤¨ à¤¸à¥à¤µà¥€à¤•à¤¾à¤°à¥à¤¯ à¤­à¤ à¤ªà¤¨à¤¿, à¤¸à¤«à¤²à¤¤à¤¾ à¤¦à¥à¤µà¥ˆ à¤¸à¤¾à¤à¥‡à¤¦à¤¾à¤°à¤¹à¤°à¥‚à¤•à¥‹ à¤­à¤¿à¤¨à¥à¤¨à¤¤à¤¾à¤¹à¤°à¥‚ à¤¸à¤®à¤¾à¤§à¤¾à¤¨ à¤—à¤°à¥à¤¨à¥‡ à¤‡à¤šà¥à¤›à¤¾à¤®à¤¾ à¤¨à¤¿à¤°à¥à¤­à¤° à¤—à¤°à¥à¤¦à¤›à¥¤ à¤µà¤¿à¤¶à¥‡à¤· à¤¸à¤®à¤¸à¥à¤¯à¤¾ à¤•à¥à¤·à¥‡à¤¤à¥à¤°à¤¹à¤°à¥‚ (à¤µà¤¿à¤¶à¥‡à¤· à¤—à¤°à¥€ à¤•à¤® à¤…à¤‚à¤• à¤ªà¤¾à¤à¤•à¤¾ à¤—à¥à¤£à¤¹à¤°à¥‚) à¤ªà¤¹à¤¿à¤šà¤¾à¤¨ à¤—à¤°à¥à¤¨à¥à¤¹à¥‹à¤¸à¥ à¤° à¤¸à¤•à¥à¤°à¤¿à¤¯ à¤°à¥‚à¤ªà¤®à¤¾ à¤¸à¤®à¥à¤¬à¥‹à¤§à¤¨ à¤—à¤°à¥à¤¨à¥à¤¹à¥‹à¤¸à¥à¥¤ à¤•à¤®à¤œà¥‹à¤° à¤•à¥à¤·à¥‡à¤¤à¥à¤°à¤¹à¤°à¥‚à¤²à¤¾à¤ˆ à¤¬à¤²à¤¿à¤¯à¥‹ à¤¬à¤¨à¤¾à¤‰à¤¨ à¤¸à¤¿à¤«à¤¾à¤°à¤¿à¤¸ à¤—à¤°à¤¿à¤à¤•à¤¾ à¤µà¥ˆà¤¦à¤¿à¤• à¤‰à¤ªà¤¾à¤¯à¤¹à¤°à¥‚ à¤—à¤°à¥à¤¨à¥‡ à¤µà¤¿à¤šà¤¾à¤° à¤—à¤°à¥à¤¨à¥à¤¹à¥‹à¤¸à¥à¥¤ à¤¸à¤žà¥à¤šà¤¾à¤° à¤¢à¤¾à¤à¤šà¤¾ à¤° à¤¦à¥à¤µà¤¨à¥à¤¦à¥à¤µ à¤¸à¤®à¤¾à¤§à¤¾à¤¨ à¤°à¤£à¤¨à¥€à¤¤à¤¿à¤¹à¤°à¥‚ à¤¸à¥à¤¥à¤¾à¤ªà¤¨à¤¾ à¤—à¤°à¥à¤¨ à¤µà¤¿à¤µà¤¾à¤¹-à¤ªà¥‚à¤°à¥à¤µ à¤ªà¤°à¤¾à¤®à¤°à¥à¤¶ à¤¦à¥ƒà¤¢à¤¤à¤¾à¤ªà¥‚à¤°à¥à¤µà¤• à¤¸à¤²à¥à¤²à¤¾à¤¹ à¤¦à¤¿à¤‡à¤¨à¥à¤›à¥¤ à¤¸à¤®à¤°à¥à¤ªà¤£ à¤° à¤ªà¤¾à¤°à¤¸à¥à¤ªà¤°à¤¿à¤• à¤¸à¤®à¥à¤®à¤¾à¤¨à¤•à¤¾ à¤¸à¤¾à¤¥, à¤¯à¥‹ à¤¸à¤®à¥à¤¬à¤¨à¥à¤§ à¤«à¤¸à¥à¤Ÿà¤¾à¤‰à¤¨ à¤¸à¤•à¥à¤›à¥¤"),

    SCORE_BELOW_AVERAGE("Below average compatibility. Score of 14-17 indicates significant challenges requiring serious consideration.", "à¤”à¤¸à¤¤à¤®à¥à¤¨à¤¿ à¤…à¤¨à¥à¤•à¥‚à¤²à¤¤à¤¾à¥¤ à¥§à¥ª-à¥§à¥­ à¤•à¥‹ à¤…à¤‚à¤•à¤²à¥‡ à¤—à¤®à¥à¤­à¥€à¤° à¤µà¤¿à¤šà¤¾à¤° à¤†à¤µà¤¶à¥à¤¯à¤• à¤ªà¤°à¥à¤¨à¥‡ à¤®à¤¹à¤¤à¥à¤¤à¥à¤µà¤ªà¥‚à¤°à¥à¤£ à¤šà¥à¤¨à¥Œà¤¤à¥€à¤¹à¤°à¥‚ à¤¸à¤‚à¤•à¥‡à¤¤ à¤—à¤°à¥à¤¦à¤›à¥¤"),
    SCORE_BELOW_AVERAGE_GUIDANCE("Your compatibility score indicates notable challenges across multiple dimensions. This match faces significant obstacles that require serious consideration before proceeding. Multiple gunas showing low scores suggest fundamental differences in temperament, values, or life approaches. Extensive Vedic remedies are essential - consult a qualified Vedic astrologer for a comprehensive remedy plan. Professional couples counseling is strongly recommended. Both partners must be willing to make substantial adjustments and compromises. Carefully evaluate whether both are prepared for the level of conscious effort required to build a harmonious relationship.", "à¤¤à¤ªà¤¾à¤ˆà¤‚à¤•à¥‹ à¤…à¤¨à¥à¤•à¥‚à¤²à¤¤à¤¾ à¤…à¤‚à¤•à¤²à¥‡ à¤µà¤¿à¤­à¤¿à¤¨à¥à¤¨ à¤†à¤¯à¤¾à¤®à¤¹à¤°à¥‚à¤®à¤¾ à¤‰à¤²à¥à¤²à¥‡à¤–à¤¨à¥€à¤¯ à¤šà¥à¤¨à¥Œà¤¤à¥€à¤¹à¤°à¥‚ à¤¸à¤‚à¤•à¥‡à¤¤ à¤—à¤°à¥à¤¦à¤›à¥¤ à¤¯à¥‹ à¤®à¤¿à¤²à¤¾à¤¨à¤²à¥‡ à¤…à¤—à¤¾à¤¡à¤¿ à¤¬à¤¢à¥à¤¨à¥ à¤…à¤˜à¤¿ à¤—à¤®à¥à¤­à¥€à¤° à¤µà¤¿à¤šà¤¾à¤° à¤†à¤µà¤¶à¥à¤¯à¤• à¤ªà¤°à¥à¤¨à¥‡ à¤®à¤¹à¤¤à¥à¤¤à¥à¤µà¤ªà¥‚à¤°à¥à¤£ à¤¬à¤¾à¤§à¤¾à¤¹à¤°à¥‚à¤•à¥‹ à¤¸à¤¾à¤®à¤¨à¤¾ à¤—à¤°à¥à¤¦à¤›à¥¤ à¤§à¥‡à¤°à¥ˆ à¤—à¥à¤£à¤¹à¤°à¥‚à¤²à¥‡ à¤•à¤® à¤…à¤‚à¤• à¤¦à¥‡à¤–à¤¾à¤‰à¤¨à¥à¤²à¥‡ à¤¸à¥à¤µà¤­à¤¾à¤µ, à¤®à¥‚à¤²à¥à¤¯à¤¹à¤°à¥‚ à¤µà¤¾ à¤œà¥€à¤µà¤¨ à¤¦à¥ƒà¤·à¥à¤Ÿà¤¿à¤•à¥‹à¤£à¤®à¤¾ à¤†à¤§à¤¾à¤°à¤­à¥‚à¤¤ à¤­à¤¿à¤¨à¥à¤¨à¤¤à¤¾à¤¹à¤°à¥‚ à¤¸à¥à¤à¤¾à¤µ à¤¦à¤¿à¤¨à¥à¤›à¥¤ à¤µà¥à¤¯à¤¾à¤ªà¤• à¤µà¥ˆà¤¦à¤¿à¤• à¤‰à¤ªà¤¾à¤¯à¤¹à¤°à¥‚ à¤†à¤µà¤¶à¥à¤¯à¤• à¤›à¤¨à¥ - à¤µà¥à¤¯à¤¾à¤ªà¤• à¤‰à¤ªà¤¾à¤¯ à¤¯à¥‹à¤œà¤¨à¤¾à¤•à¥‹ à¤²à¤¾à¤—à¤¿ à¤¯à¥‹à¤—à¥à¤¯ à¤µà¥ˆà¤¦à¤¿à¤• à¤œà¥à¤¯à¥‹à¤¤à¤¿à¤·à¥€à¤¸à¤à¤— à¤ªà¤°à¤¾à¤®à¤°à¥à¤¶ à¤—à¤°à¥à¤¨à¥à¤¹à¥‹à¤¸à¥à¥¤ à¤ªà¥‡à¤¶à¥‡à¤µà¤° à¤œà¥‹à¤¡à¥€ à¤ªà¤°à¤¾à¤®à¤°à¥à¤¶ à¤¦à¥ƒà¤¢à¤¤à¤¾à¤ªà¥‚à¤°à¥à¤µà¤• à¤¸à¤¿à¤«à¤¾à¤°à¤¿à¤¸ à¤—à¤°à¤¿à¤à¤•à¥‹ à¤›à¥¤ à¤¦à¥à¤µà¥ˆ à¤¸à¤¾à¤à¥‡à¤¦à¤¾à¤°à¤¹à¤°à¥‚ à¤ªà¤°à¥à¤¯à¤¾à¤ªà¥à¤¤ à¤¸à¤®à¤¾à¤¯à¥‹à¤œà¤¨ à¤° à¤¸à¤®à¥à¤à¥Œà¤¤à¤¾ à¤—à¤°à¥à¤¨ à¤‡à¤šà¥à¤›à¥à¤• à¤¹à¥à¤¨à¥à¤ªà¤°à¥à¤›à¥¤ à¤¸à¤¾à¤®à¤žà¥à¤œà¤¸à¥à¤¯à¤ªà¥‚à¤°à¥à¤£ à¤¸à¤®à¥à¤¬à¤¨à¥à¤§ à¤¨à¤¿à¤°à¥à¤®à¤¾à¤£ à¤—à¤°à¥à¤¨ à¤†à¤µà¤¶à¥à¤¯à¤• à¤¸à¤šà¥‡à¤¤ à¤ªà¥à¤°à¤¯à¤¾à¤¸à¤•à¥‹ à¤¸à¥à¤¤à¤°à¤•à¥‹ à¤²à¤¾à¤—à¤¿ à¤¦à¥à¤µà¥ˆ à¤¤à¤¯à¤¾à¤° à¤›à¤¨à¥ à¤•à¤¿ à¤›à¥ˆà¤¨à¤¨à¥ à¤­à¤¨à¥‡à¤° à¤¸à¤¾à¤µà¤§à¤¾à¤¨à¥€à¤ªà¥‚à¤°à¥à¤µà¤• à¤®à¥‚à¤²à¥à¤¯à¤¾à¤™à¥à¤•à¤¨ à¤—à¤°à¥à¤¨à¥à¤¹à¥‹à¤¸à¥à¥¤"),

    SCORE_POOR("Poor compatibility. Score below 14 suggests major challenges. Marriage not recommended without extensive remedies.", "à¤•à¤®à¤œà¥‹à¤° à¤…à¤¨à¥à¤•à¥‚à¤²à¤¤à¤¾à¥¤ à¥§à¥ª à¤®à¥à¤¨à¤¿à¤•à¥‹ à¤…à¤‚à¤•à¤²à¥‡ à¤ªà¥à¤°à¤®à¥à¤– à¤šà¥à¤¨à¥Œà¤¤à¥€à¤¹à¤°à¥‚ à¤¸à¥à¤à¤¾à¤µ à¤¦à¤¿à¤¨à¥à¤›à¥¤ à¤µà¥à¤¯à¤¾à¤ªà¤• à¤‰à¤ªà¤¾à¤¯à¤¹à¤°à¥‚ à¤¬à¤¿à¤¨à¤¾ à¤µà¤¿à¤µà¤¾à¤¹ à¤¸à¤¿à¤«à¤¾à¤°à¤¿à¤¸ à¤—à¤°à¤¿à¤à¤•à¥‹ à¤›à¥ˆà¤¨à¥¤"),
    SCORE_POOR_GUIDANCE("This match shows poor compatibility with fundamental incompatibilities across critical areas. Traditional Vedic astrology does not recommend proceeding with this marriage without addressing severe issues. The low score suggests conflicts in temperament, values, physical harmony, and mental compatibility. If you choose to proceed despite this assessment, the following are absolutely essential: 1) Consult multiple qualified Vedic astrologers for comprehensive remedy plans 2) Perform all recommended pujas, homas, and remedial measures diligently 3) Undergo extensive pre-marital and ongoing couples counseling 4) Both partners must have extraordinary commitment to conscious relationship work 5) Prepare for ongoing challenges requiring patience, tolerance, and spiritual growth. Consider whether alternative matches might offer better foundations for marital happiness.", "à¤¯à¥‹ à¤®à¤¿à¤²à¤¾à¤¨à¤²à¥‡ à¤®à¤¹à¤¤à¥à¤¤à¥à¤µà¤ªà¥‚à¤°à¥à¤£ à¤•à¥à¤·à¥‡à¤¤à¥à¤°à¤¹à¤°à¥‚à¤®à¤¾ à¤†à¤§à¤¾à¤°à¤­à¥‚à¤¤ à¤…à¤¸à¤‚à¤—à¤¤à¤¤à¤¾à¤¹à¤°à¥‚à¤¸à¤¹à¤¿à¤¤ à¤•à¤®à¤œà¥‹à¤° à¤…à¤¨à¥à¤•à¥‚à¤²à¤¤à¤¾ à¤¦à¥‡à¤–à¤¾à¤‰à¤à¤›à¥¤ à¤ªà¤°à¤®à¥à¤ªà¤°à¤¾à¤—à¤¤ à¤µà¥ˆà¤¦à¤¿à¤• à¤œà¥à¤¯à¥‹à¤¤à¤¿à¤· à¤—à¤®à¥à¤­à¥€à¤° à¤¸à¤®à¤¸à¥à¤¯à¤¾à¤¹à¤°à¥‚ à¤¸à¤®à¥à¤¬à¥‹à¤§à¤¨ à¤¨à¤—à¤°à¥€ à¤¯à¤¸ à¤µà¤¿à¤µà¤¾à¤¹à¤¸à¤à¤— à¤…à¤—à¤¾à¤¡à¤¿ à¤¬à¤¢à¥à¤¨ à¤¸à¤¿à¤«à¤¾à¤°à¤¿à¤¸ à¤—à¤°à¥à¤¦à¥ˆà¤¨à¥¤ à¤•à¤® à¤…à¤‚à¤•à¤²à¥‡ à¤¸à¥à¤µà¤­à¤¾à¤µ, à¤®à¥‚à¤²à¥à¤¯à¤¹à¤°à¥‚, à¤¶à¤¾à¤°à¥€à¤°à¤¿à¤• à¤¸à¤¾à¤®à¤žà¥à¤œà¤¸à¥à¤¯ à¤° à¤®à¤¾à¤¨à¤¸à¤¿à¤• à¤…à¤¨à¥à¤•à¥‚à¤²à¤¤à¤¾à¤®à¤¾ à¤¦à¥à¤µà¤¨à¥à¤¦à¥à¤µ à¤¸à¥à¤à¤¾à¤µ à¤¦à¤¿à¤¨à¥à¤›à¥¤ à¤¯à¤¦à¤¿ à¤¤à¤ªà¤¾à¤ˆà¤‚ à¤¯à¤¸ à¤®à¥‚à¤²à¥à¤¯à¤¾à¤™à¥à¤•à¤¨à¤•à¥‹ à¤¬à¤¾à¤µà¤œà¥à¤¦ à¤…à¤—à¤¾à¤¡à¤¿ à¤¬à¤¢à¥à¤¨ à¤°à¥‹à¤œà¥à¤¨à¥à¤¹à¥à¤¨à¥à¤› à¤­à¤¨à¥‡, à¤¨à¤¿à¤®à¥à¤¨ à¤•à¥à¤°à¤¾à¤¹à¤°à¥‚ à¤à¤•à¤¦à¤®à¥ˆ à¤†à¤µà¤¶à¥à¤¯à¤• à¤›à¤¨à¥: à¥§) à¤µà¥à¤¯à¤¾à¤ªà¤• à¤‰à¤ªà¤¾à¤¯ à¤¯à¥‹à¤œà¤¨à¤¾à¤•à¥‹ à¤²à¤¾à¤—à¤¿ à¤§à¥‡à¤°à¥ˆ à¤¯à¥‹à¤—à¥à¤¯ à¤µà¥ˆà¤¦à¤¿à¤• à¤œà¥à¤¯à¥‹à¤¤à¤¿à¤·à¥€à¤¹à¤°à¥‚à¤¸à¤à¤— à¤ªà¤°à¤¾à¤®à¤°à¥à¤¶ à¤—à¤°à¥à¤¨à¥à¤¹à¥‹à¤¸à¥ à¥¨) à¤¸à¤¬à¥ˆ à¤¸à¤¿à¤«à¤¾à¤°à¤¿à¤¸ à¤—à¤°à¤¿à¤à¤•à¤¾ à¤ªà¥‚à¤œà¤¾, à¤¹à¥‹à¤® à¤° à¤‰à¤ªà¤šà¤¾à¤°à¤¾à¤¤à¥à¤®à¤• à¤‰à¤ªà¤¾à¤¯à¤¹à¤°à¥‚ à¤²à¤—à¤¨à¤¶à¥€à¤²à¤¤à¤¾à¤•à¤¾ à¤¸à¤¾à¤¥ à¤—à¤°à¥à¤¨à¥à¤¹à¥‹à¤¸à¥ à¥©) à¤µà¥à¤¯à¤¾à¤ªà¤• à¤µà¤¿à¤µà¤¾à¤¹-à¤ªà¥‚à¤°à¥à¤µ à¤° à¤¨à¤¿à¤°à¤¨à¥à¤¤à¤° à¤œà¥‹à¤¡à¥€ à¤ªà¤°à¤¾à¤®à¤°à¥à¤¶ à¤²à¤¿à¤¨à¥à¤¹à¥‹à¤¸à¥ à¥ª) à¤¦à¥à¤µà¥ˆ à¤¸à¤¾à¤à¥‡à¤¦à¤¾à¤°à¤¹à¤°à¥‚à¤¸à¤à¤— à¤¸à¤šà¥‡à¤¤ à¤¸à¤®à¥à¤¬à¤¨à¥à¤§ à¤•à¤¾à¤°à¥à¤¯à¤•à¥‹ à¤²à¤¾à¤—à¤¿ à¤…à¤¸à¤¾à¤§à¤¾à¤°à¤£ à¤ªà¥à¤°à¤¤à¤¿à¤¬à¤¦à¥à¤§à¤¤à¤¾ à¤¹à¥à¤¨à¥à¤ªà¤°à¥à¤› à¥«) à¤§à¥ˆà¤°à¥à¤¯, à¤¸à¤¹à¤¨à¤¶à¥€à¤²à¤¤à¤¾ à¤° à¤†à¤§à¥à¤¯à¤¾à¤¤à¥à¤®à¤¿à¤• à¤µà¤¿à¤•à¤¾à¤¸ à¤†à¤µà¤¶à¥à¤¯à¤• à¤ªà¤°à¥à¤¨à¥‡ à¤¨à¤¿à¤°à¤¨à¥à¤¤à¤° à¤šà¥à¤¨à¥Œà¤¤à¥€à¤¹à¤°à¥‚à¤•à¥‹ à¤²à¤¾à¤—à¤¿ à¤¤à¤¯à¤¾à¤° à¤°à¤¹à¤¨à¥à¤¹à¥‹à¤¸à¥à¥¤ à¤µà¥ˆà¤•à¤²à¥à¤ªà¤¿à¤• à¤®à¤¿à¤²à¤¾à¤¨à¤¹à¤°à¥‚à¤²à¥‡ à¤µà¥ˆà¤µà¤¾à¤¹à¤¿à¤• à¤–à¥à¤¶à¥€à¤•à¥‹ à¤²à¤¾à¤—à¤¿ à¤°à¤¾à¤®à¥à¤°à¥‹ à¤†à¤§à¤¾à¤°à¤¹à¤°à¥‚ à¤ªà¥à¤°à¤¦à¤¾à¤¨ à¤—à¤°à¥à¤¨ à¤¸à¤•à¥à¤›à¤¨à¥ à¤•à¤¿ à¤­à¤¨à¥‡à¤° à¤µà¤¿à¤šà¤¾à¤° à¤—à¤°à¥à¤¨à¥à¤¹à¥‹à¤¸à¥à¥¤"),

    // ============================================
    // MATCHMAKING - DOSHA CANCELLATION EXPLANATIONS
    // ============================================
    DOSHA_CANCEL_EXPLAINED("Dosha Cancellation Factors Explained", "à¤¦à¥‹à¤· à¤°à¤¦à¥à¤¦ à¤•à¤¾à¤°à¤•à¤¹à¤°à¥‚ à¤µà¥à¤¯à¤¾à¤–à¥à¤¯à¤¾"),
    NADI_CANCEL_EXPLANATION("Nadi Dosha can be cancelled in several specific scenarios according to classical Vedic texts. These cancellations are based on deeper astrological principles that override the superficial Nadi match.", "à¤¨à¤¾à¤¡à¥€ à¤¦à¥‹à¤· à¤¶à¤¾à¤¸à¥à¤¤à¥à¤°à¥€à¤¯ à¤µà¥ˆà¤¦à¤¿à¤• à¤—à¥à¤°à¤¨à¥à¤¥à¤¹à¤°à¥‚ à¤…à¤¨à¥à¤¸à¤¾à¤° à¤§à¥‡à¤°à¥ˆ à¤µà¤¿à¤¶à¤¿à¤·à¥à¤Ÿ à¤ªà¤°à¤¿à¤¸à¥à¤¥à¤¿à¤¤à¤¿à¤¹à¤°à¥‚à¤®à¤¾ à¤°à¤¦à¥à¤¦ à¤—à¤°à¥à¤¨ à¤¸à¤•à¤¿à¤¨à¥à¤›à¥¤ à¤¯à¥€ à¤°à¤¦à¥à¤¦à¤¹à¤°à¥‚ à¤—à¤¹à¤¨ à¤œà¥à¤¯à¥‹à¤¤à¤¿à¤·à¥€à¤¯ à¤¸à¤¿à¤¦à¥à¤§à¤¾à¤¨à¥à¤¤à¤¹à¤°à¥‚à¤®à¤¾ à¤†à¤§à¤¾à¤°à¤¿à¤¤ à¤›à¤¨à¥ à¤œà¤¸à¤²à¥‡ à¤¸à¤¤à¤¹à¥€ à¤¨à¤¾à¤¡à¥€ à¤®à¤¿à¤²à¤¾à¤¨à¤²à¤¾à¤ˆ à¤“à¤­à¤°à¤°à¤¾à¤‡à¤¡ à¤—à¤°à¥à¤¦à¤›à¥¤"),
    BHAKOOT_CANCEL_EXPLANATION("Bhakoot Dosha cancellation occurs when deeper planetary relationships indicate harmony. Even though Moon signs are in challenging positions, the underlying planetary friendships or same lords can neutralize the dosha effects.", "à¤­à¤•à¥à¤Ÿ à¤¦à¥‹à¤· à¤°à¤¦à¥à¤¦ à¤—à¤¹à¤¨ à¤—à¥à¤°à¤¹ à¤¸à¤®à¥à¤¬à¤¨à¥à¤§à¤¹à¤°à¥‚à¤²à¥‡ à¤¸à¤¾à¤®à¤žà¥à¤œà¤¸à¥à¤¯ à¤¸à¤‚à¤•à¥‡à¤¤ à¤—à¤°à¥à¤¦à¤¾ à¤¹à¥à¤¨à¥à¤›à¥¤ à¤¯à¤¦à¥à¤¯à¤ªà¤¿ à¤šà¤¨à¥à¤¦à¥à¤° à¤°à¤¾à¤¶à¤¿à¤¹à¤°à¥‚ à¤šà¥à¤¨à¥Œà¤¤à¥€à¤ªà¥‚à¤°à¥à¤£ à¤¸à¥à¤¥à¤¾à¤¨à¤®à¤¾ à¤›à¤¨à¥, à¤…à¤¨à¥à¤¤à¤°à¥à¤¨à¤¿à¤¹à¤¿à¤¤ à¤—à¥à¤°à¤¹ à¤®à¤¿à¤¤à¥à¤°à¤¤à¤¾ à¤µà¤¾ à¤‰à¤¹à¥€ à¤¸à¥à¤µà¤¾à¤®à¥€à¤¹à¤°à¥‚à¤²à¥‡ à¤¦à¥‹à¤· à¤ªà¥à¤°à¤­à¤¾à¤µà¤¹à¤°à¥‚ à¤¨à¤¿à¤·à¥à¤•à¥à¤°à¤¿à¤¯ à¤ªà¤¾à¤°à¥à¤¨ à¤¸à¤•à¥à¤›à¤¨à¥à¥¤"),
    MANGLIK_CANCEL_EXPLANATION("Manglik Dosha can be cancelled through various factors: both partners being Manglik (mutual cancellation), Mars in certain houses (1st, 4th, 7th, 8th, 12th from specific references), Mars being in own sign or exalted, age over 28 years (Mars matures), or presence of benefic aspects. These factors indicate that Mars energy is channeled constructively.", "à¤®à¤¾à¤‚à¤—à¤²à¤¿à¤• à¤¦à¥‹à¤· à¤µà¤¿à¤­à¤¿à¤¨à¥à¤¨ à¤•à¤¾à¤°à¤•à¤¹à¤°à¥‚à¤¦à¥à¤µà¤¾à¤°à¤¾ à¤°à¤¦à¥à¤¦ à¤—à¤°à¥à¤¨ à¤¸à¤•à¤¿à¤¨à¥à¤›: à¤¦à¥à¤µà¥ˆ à¤¸à¤¾à¤à¥‡à¤¦à¤¾à¤° à¤®à¤¾à¤‚à¤—à¤²à¤¿à¤• à¤¹à¥à¤¨à¥ (à¤ªà¤¾à¤°à¤¸à¥à¤ªà¤°à¤¿à¤• à¤°à¤¦à¥à¤¦), à¤®à¤‚à¤—à¤² à¤¨à¤¿à¤¶à¥à¤šà¤¿à¤¤ à¤­à¤¾à¤µà¤¹à¤°à¥‚à¤®à¤¾ (à¤µà¤¿à¤¶à¥‡à¤· à¤¸à¤¨à¥à¤¦à¤°à¥à¤­à¤¬à¤¾à¤Ÿ à¥§, à¥ª, à¥­, à¥®, à¥§à¥¨), à¤®à¤‚à¤—à¤² à¤†à¤«à¥à¤¨à¥ˆ à¤°à¤¾à¤¶à¤¿ à¤µà¤¾ à¤‰à¤šà¥à¤šà¤®à¤¾ à¤¹à¥à¤¨à¥, à¥¨à¥® à¤µà¤°à¥à¤· à¤­à¤¨à¥à¤¦à¤¾ à¤®à¤¾à¤¥à¤¿à¤•à¥‹ à¤‰à¤®à¥‡à¤° (à¤®à¤‚à¤—à¤² à¤ªà¤°à¤¿à¤ªà¤•à¥à¤µ à¤¹à¥à¤¨à¥à¤›), à¤µà¤¾ à¤¶à¥à¤­ à¤¦à¥ƒà¤·à¥à¤Ÿà¤¿à¤¹à¤°à¥‚à¤•à¥‹ à¤‰à¤ªà¤¸à¥à¤¥à¤¿à¤¤à¤¿à¥¤ à¤¯à¥€ à¤•à¤¾à¤°à¤•à¤¹à¤°à¥‚à¤²à¥‡ à¤¸à¤‚à¤•à¥‡à¤¤ à¤—à¤°à¥à¤¦à¤›à¤¨à¥ à¤•à¤¿ à¤®à¤‚à¤—à¤² à¤Šà¤°à¥à¤œà¤¾ à¤°à¤šà¤¨à¤¾à¤¤à¥à¤®à¤• à¤°à¥‚à¤ªà¤®à¤¾ à¤šà¥à¤¯à¤¾à¤¨à¤² à¤—à¤°à¤¿à¤à¤•à¥‹ à¤›à¥¤"),

    // ============================================
    // MATCHMAKING - COMPATIBILITY RECOMMENDATIONS
    // ============================================
    COMPAT_RECOMMEND_MARRIAGE_TIMING("Recommended Marriage Timing", "à¤¸à¤¿à¤«à¤¾à¤°à¤¿à¤¸ à¤—à¤°à¤¿à¤à¤•à¥‹ à¤µà¤¿à¤µà¤¾à¤¹ à¤¸à¤®à¤¯"),
    COMPAT_TIMING_IMMEDIATE("Marriage can proceed without delay. Choose an auspicious muhurta during favorable planetary transits for both charts.", "à¤µà¤¿à¤µà¤¾à¤¹ à¤¢à¤¿à¤²à¤¾à¤‡ à¤¬à¤¿à¤¨à¤¾ à¤…à¤—à¤¾à¤¡à¤¿ à¤¬à¤¢à¥à¤¨ à¤¸à¤•à¥à¤›à¥¤ à¤¦à¥à¤µà¥ˆ à¤•à¥à¤£à¥à¤¡à¤²à¥€à¤•à¥‹ à¤²à¤¾à¤—à¤¿ à¤…à¤¨à¥à¤•à¥‚à¤² à¤—à¥à¤°à¤¹ à¤—à¥‹à¤šà¤°à¤•à¥‹ à¤¸à¤®à¤¯à¤®à¤¾ à¤¶à¥à¤­ à¤®à¥à¤¹à¥‚à¤°à¥à¤¤ à¤›à¤¾à¤¨à¥à¤¨à¥à¤¹à¥‹à¤¸à¥à¥¤"),
    COMPAT_TIMING_AFTER_REMEDIES("Proceed after completing essential remedies (minimum 40-90 days). This allows remedial measures to strengthen weak areas before marriage.", "à¤†à¤µà¤¶à¥à¤¯à¤• à¤‰à¤ªà¤¾à¤¯à¤¹à¤°à¥‚ à¤ªà¥‚à¤°à¤¾ à¤—à¤°à¥‡à¤ªà¤›à¤¿ à¤…à¤—à¤¾à¤¡à¤¿ à¤¬à¤¢à¥à¤¨à¥à¤¹à¥‹à¤¸à¥ (à¤¨à¥à¤¯à¥‚à¤¨à¤¤à¤® à¥ªà¥¦-à¥¯à¥¦ à¤¦à¤¿à¤¨)à¥¤ à¤¯à¤¸à¤²à¥‡ à¤µà¤¿à¤µà¤¾à¤¹ à¤…à¤˜à¤¿ à¤•à¤®à¤œà¥‹à¤° à¤•à¥à¤·à¥‡à¤¤à¥à¤°à¤¹à¤°à¥‚à¤²à¤¾à¤ˆ à¤¬à¤²à¤¿à¤¯à¥‹ à¤¬à¤¨à¤¾à¤‰à¤¨ à¤‰à¤ªà¤šà¤¾à¤°à¤¾à¤¤à¥à¤®à¤• à¤‰à¤ªà¤¾à¤¯à¤¹à¤°à¥‚à¤²à¤¾à¤ˆ à¤…à¤¨à¥à¤®à¤¤à¤¿ à¤¦à¤¿à¤¨à¥à¤›à¥¤"),
    COMPAT_TIMING_EXTENDED_REMEDIES("Delay marriage for 6-12 months while performing comprehensive remedies. Use this time for counseling and conscious preparation.", "à¤µà¥à¤¯à¤¾à¤ªà¤• à¤‰à¤ªà¤¾à¤¯à¤¹à¤°à¥‚ à¤—à¤°à¥à¤¦à¥ˆ à¥¬-à¥§à¥¨ à¤®à¤¹à¤¿à¤¨à¤¾ à¤µà¤¿à¤µà¤¾à¤¹à¤®à¤¾ à¤¢à¤¿à¤²à¤¾à¤‡ à¤—à¤°à¥à¤¨à¥à¤¹à¥‹à¤¸à¥à¥¤ à¤ªà¤°à¤¾à¤®à¤°à¥à¤¶ à¤° à¤¸à¤šà¥‡à¤¤ à¤¤à¤¯à¤¾à¤°à¥€à¤•à¥‹ à¤²à¤¾à¤—à¤¿ à¤¯à¥‹ à¤¸à¤®à¤¯ à¤ªà¥à¤°à¤¯à¥‹à¤— à¤—à¤°à¥à¤¨à¥à¤¹à¥‹à¤¸à¥à¥¤"),
    COMPAT_TIMING_RECONSIDER("Seriously reconsider this match. If proceeding, delay minimum 1 year with extensive remedies, counseling, and spiritual preparation.", "à¤¯à¤¸ à¤®à¤¿à¤²à¤¾à¤¨à¤²à¤¾à¤ˆ à¤—à¤®à¥à¤­à¥€à¤°à¤¤à¤¾à¤ªà¥‚à¤°à¥à¤µà¤• à¤ªà¥à¤¨à¤°à¥à¤µà¤¿à¤šà¤¾à¤° à¤—à¤°à¥à¤¨à¥à¤¹à¥‹à¤¸à¥à¥¤ à¤…à¤—à¤¾à¤¡à¤¿ à¤¬à¤¢à¥à¤¦à¥ˆ à¤¹à¥à¤¨à¥à¤¹à¥à¤¨à¥à¤› à¤­à¤¨à¥‡, à¤µà¥à¤¯à¤¾à¤ªà¤• à¤‰à¤ªà¤¾à¤¯, à¤ªà¤°à¤¾à¤®à¤°à¥à¤¶ à¤° à¤†à¤§à¥à¤¯à¤¾à¤¤à¥à¤®à¤¿à¤• à¤¤à¤¯à¤¾à¤°à¥€à¤¸à¤¹à¤¿à¤¤ à¤¨à¥à¤¯à¥‚à¤¨à¤¤à¤® à¥§ à¤µà¤°à¥à¤· à¤¢à¤¿à¤²à¤¾à¤‡ à¤—à¤°à¥à¤¨à¥à¤¹à¥‹à¤¸à¥à¥¤"),

    COMPAT_RECOMMEND_RELATIONSHIP_FOCUS("Relationship Focus Areas", "à¤¸à¤®à¥à¤¬à¤¨à¥à¤§ à¤«à¥‹à¤•à¤¸ à¤•à¥à¤·à¥‡à¤¤à¥à¤°à¤¹à¤°à¥‚"),
    COMPAT_FOCUS_SPIRITUAL("Spiritual Growth Together: Practice joint meditation, visit temples together, study spiritual texts, maintain shared spiritual practices.", "à¤¸à¤à¤—à¥ˆ à¤†à¤§à¥à¤¯à¤¾à¤¤à¥à¤®à¤¿à¤• à¤µà¤¿à¤•à¤¾à¤¸: à¤¸à¤‚à¤¯à¥à¤•à¥à¤¤ à¤§à¥à¤¯à¤¾à¤¨ à¤…à¤­à¥à¤¯à¤¾à¤¸ à¤—à¤°à¥à¤¨à¥à¤¹à¥‹à¤¸à¥, à¤¸à¤à¤—à¥ˆ à¤®à¤¨à¥à¤¦à¤¿à¤°à¤¹à¤°à¥‚ à¤­à¥à¤°à¤®à¤£ à¤—à¤°à¥à¤¨à¥à¤¹à¥‹à¤¸à¥, à¤†à¤§à¥à¤¯à¤¾à¤¤à¥à¤®à¤¿à¤• à¤—à¥à¤°à¤¨à¥à¤¥à¤¹à¤°à¥‚ à¤…à¤§à¥à¤¯à¤¯à¤¨ à¤—à¤°à¥à¤¨à¥à¤¹à¥‹à¤¸à¥, à¤¸à¤¾à¤à¤¾ à¤†à¤§à¥à¤¯à¤¾à¤¤à¥à¤®à¤¿à¤• à¤…à¤­à¥à¤¯à¤¾à¤¸à¤¹à¤°à¥‚ à¤•à¤¾à¤¯à¤® à¤°à¤¾à¤–à¥à¤¨à¥à¤¹à¥‹à¤¸à¥à¥¤"),
    COMPAT_FOCUS_COMMUNICATION("Mental & Communication: Schedule regular check-ins, practice active listening, express appreciation daily, address conflicts calmly.", "à¤®à¤¾à¤¨à¤¸à¤¿à¤• à¤° à¤¸à¤žà¥à¤šà¤¾à¤°: à¤¨à¤¿à¤¯à¤®à¤¿à¤¤ à¤œà¤¾à¤à¤š-à¤ªà¤¡à¤¤à¤¾à¤² à¤¤à¤¾à¤²à¤¿à¤•à¤¾ à¤¬à¤¨à¤¾à¤‰à¤¨à¥à¤¹à¥‹à¤¸à¥, à¤¸à¤•à¥à¤°à¤¿à¤¯ à¤¸à¥à¤¨à¤¾à¤‡ à¤…à¤­à¥à¤¯à¤¾à¤¸ à¤—à¤°à¥à¤¨à¥à¤¹à¥‹à¤¸à¥, à¤¦à¥ˆà¤¨à¤¿à¤• à¤ªà¥à¤°à¤¶à¤‚à¤¸à¤¾ à¤µà¥à¤¯à¤•à¥à¤¤ à¤—à¤°à¥à¤¨à¥à¤¹à¥‹à¤¸à¥, à¤¦à¥à¤µà¤¨à¥à¤¦à¥à¤µà¤¹à¤°à¥‚à¤²à¤¾à¤ˆ à¤¶à¤¾à¤¨à¥à¤¤à¤ªà¥‚à¤°à¥à¤µà¤• à¤¸à¤®à¥à¤¬à¥‹à¤§à¤¨ à¤—à¤°à¥à¤¨à¥à¤¹à¥‹à¤¸à¥à¥¤"),
    COMPAT_FOCUS_PHYSICAL("Physical Harmony: Prioritize physical affection, maintain health together, respect boundaries, create intimate connection rituals.", "à¤¶à¤¾à¤°à¥€à¤°à¤¿à¤• à¤¸à¤¾à¤®à¤žà¥à¤œà¤¸à¥à¤¯: à¤¶à¤¾à¤°à¥€à¤°à¤¿à¤• à¤¸à¥à¤¨à¥‡à¤¹à¤²à¤¾à¤ˆ à¤ªà¥à¤°à¤¾à¤¥à¤®à¤¿à¤•à¤¤à¤¾ à¤¦à¤¿à¤¨à¥à¤¹à¥‹à¤¸à¥, à¤¸à¤à¤—à¥ˆ à¤¸à¥à¤µà¤¾à¤¸à¥à¤¥à¥à¤¯ à¤•à¤¾à¤¯à¤® à¤°à¤¾à¤–à¥à¤¨à¥à¤¹à¥‹à¤¸à¥, à¤¸à¥€à¤®à¤¾à¤¹à¤°à¥‚à¤•à¥‹ à¤¸à¤®à¥à¤®à¤¾à¤¨ à¤—à¤°à¥à¤¨à¥à¤¹à¥‹à¤¸à¥, à¤…à¤¨à¥à¤¤à¤°à¤‚à¤— à¤¸à¤®à¥à¤¬à¤¨à¥à¤§ à¤…à¤¨à¥à¤·à¥à¤ à¤¾à¤¨à¤¹à¤°à¥‚ à¤¸à¤¿à¤°à¥à¤œà¤¨à¤¾ à¤—à¤°à¥à¤¨à¥à¤¹à¥‹à¤¸à¥à¥¤"),
    COMPAT_FOCUS_TEMPERAMENT("Temperamental Balance: Understand personality differences, practice patience, compromise consciously, celebrate individual strengths.", "à¤¸à¥à¤µà¤­à¤¾à¤µà¤—à¤¤ à¤¸à¤¨à¥à¤¤à¥à¤²à¤¨: à¤µà¥à¤¯à¤•à¥à¤¤à¤¿à¤¤à¥à¤µ à¤­à¤¿à¤¨à¥à¤¨à¤¤à¤¾à¤¹à¤°à¥‚ à¤¬à¥à¤à¥à¤¨à¥à¤¹à¥‹à¤¸à¥, à¤§à¥ˆà¤°à¥à¤¯ à¤…à¤­à¥à¤¯à¤¾à¤¸ à¤—à¤°à¥à¤¨à¥à¤¹à¥‹à¤¸à¥, à¤¸à¤šà¥‡à¤¤ à¤°à¥‚à¤ªà¤®à¤¾ à¤¸à¤®à¥à¤à¥Œà¤¤à¤¾ à¤—à¤°à¥à¤¨à¥à¤¹à¥‹à¤¸à¥, à¤µà¥à¤¯à¤•à¥à¤¤à¤¿à¤—à¤¤ à¤¶à¤•à¥à¤¤à¤¿à¤¹à¤°à¥‚ à¤®à¤¨à¤¾à¤‰à¤¨à¥à¤¹à¥‹à¤¸à¥à¥¤"),
    COMPAT_FOCUS_FINANCIAL("Financial Harmony: Create joint financial plans, discuss money values openly, set shared goals, respect spending differences.", "à¤†à¤°à¥à¤¥à¤¿à¤• à¤¸à¤¾à¤®à¤žà¥à¤œà¤¸à¥à¤¯: à¤¸à¤‚à¤¯à¥à¤•à¥à¤¤ à¤µà¤¿à¤¤à¥à¤¤à¥€à¤¯ à¤¯à¥‹à¤œà¤¨à¤¾à¤¹à¤°à¥‚ à¤¬à¤¨à¤¾à¤‰à¤¨à¥à¤¹à¥‹à¤¸à¥, à¤ªà¥ˆà¤¸à¤¾ à¤®à¥‚à¤²à¥à¤¯à¤¹à¤°à¥‚ à¤–à¥à¤²à¥‡à¤° à¤›à¤²à¤«à¤² à¤—à¤°à¥à¤¨à¥à¤¹à¥‹à¤¸à¥, à¤¸à¤¾à¤à¤¾ à¤²à¤•à¥à¤·à¥à¤¯à¤¹à¤°à¥‚ à¤¸à¥‡à¤Ÿ à¤—à¤°à¥à¤¨à¥à¤¹à¥‹à¤¸à¥, à¤–à¤°à¥à¤š à¤­à¤¿à¤¨à¥à¤¨à¤¤à¤¾à¤¹à¤°à¥‚à¤²à¤¾à¤ˆ à¤¸à¤®à¥à¤®à¤¾à¤¨ à¤—à¤°à¥à¤¨à¥à¤¹à¥‹à¤¸à¥à¥¤"),
    COMPAT_FOCUS_HEALTH("Health & Progeny: Maintain healthy lifestyles, support each other's wellness, prepare mindfully for parenthood.", "à¤¸à¥à¤µà¤¾à¤¸à¥à¤¥à¥à¤¯ à¤° à¤¸à¤¨à¥à¤¤à¤¾à¤¨: à¤¸à¥à¤µà¤¸à¥à¤¥ à¤œà¥€à¤µà¤¨à¤¶à¥ˆà¤²à¥€ à¤•à¤¾à¤¯à¤® à¤°à¤¾à¤–à¥à¤¨à¥à¤¹à¥‹à¤¸à¥, à¤à¤•à¤…à¤°à¥à¤•à¤¾à¤•à¥‹ à¤•à¤²à¥à¤¯à¤¾à¤£à¤²à¤¾à¤ˆ à¤¸à¤®à¤°à¥à¤¥à¤¨ à¤—à¤°à¥à¤¨à¥à¤¹à¥‹à¤¸à¥, à¤®à¤¾à¤¤à¥ƒà¤¤à¥à¤µ/à¤ªà¤¿à¤¤à¥ƒà¤¤à¥à¤µà¤•à¥‹ à¤²à¤¾à¤—à¤¿ à¤¸à¤¾à¤µà¤§à¤¾à¤¨à¥€à¤ªà¥‚à¤°à¥à¤µà¤• à¤¤à¤¯à¤¾à¤°à¥€ à¤—à¤°à¥à¤¨à¥à¤¹à¥‹à¤¸à¥à¥¤"),

    // ============================================
    // YOGA CALCULATOR - CATEGORIES & STRENGTH
    // ============================================
    YOGA_CAT_RAJA("Raja Yoga", "à¤°à¤¾à¤œ à¤¯à¥‹à¤—"),
    YOGA_CAT_RAJA_DESC("Power, authority, and leadership combinations", "à¤¶à¤•à¥à¤¤à¤¿, à¤…à¤§à¤¿à¤•à¤¾à¤° à¤° à¤¨à¥‡à¤¤à¥ƒà¤¤à¥à¤µ à¤¸à¤‚à¤¯à¥‹à¤œà¤¨à¤¹à¤°à¥‚"),
    YOGA_CAT_DHANA("Dhana Yoga", "à¤§à¤¨ à¤¯à¥‹à¤—"),
    YOGA_CAT_DHANA_DESC("Wealth and prosperity combinations", "à¤§à¤¨ à¤° à¤¸à¤®à¥ƒà¤¦à¥à¤§à¤¿ à¤¸à¤‚à¤¯à¥‹à¤œà¤¨à¤¹à¤°à¥‚"),
    YOGA_CAT_PANCHA_MAHAPURUSHA("Pancha Mahapurusha Yoga", "à¤ªà¤žà¥à¤š à¤®à¤¹à¤¾à¤ªà¥à¤°à¥à¤· à¤¯à¥‹à¤—"),
    YOGA_CAT_PANCHA_MAHAPURUSHA_DESC("Five great person combinations", "à¤ªà¤¾à¤à¤š à¤®à¤¹à¤¾à¤¨ à¤µà¥à¤¯à¤•à¥à¤¤à¤¿ à¤¸à¤‚à¤¯à¥‹à¤œà¤¨à¤¹à¤°à¥‚"),
    YOGA_CAT_NABHASA("Nabhasa Yoga", "à¤¨à¤¾à¤­à¤¸ à¤¯à¥‹à¤—"),
    YOGA_CAT_NABHASA_DESC("Pattern-based planetary combinations", "à¤¢à¤¾à¤à¤šà¤¾à¤®à¤¾ à¤†à¤§à¤¾à¤°à¤¿à¤¤ à¤—à¥à¤°à¤¹ à¤¸à¤‚à¤¯à¥‹à¤œà¤¨à¤¹à¤°à¥‚"),
    YOGA_CAT_CHANDRA("Chandra Yoga", "à¤šà¤¨à¥à¤¦à¥à¤° à¤¯à¥‹à¤—"),
    YOGA_CAT_CHANDRA_DESC("Moon-based combinations", "à¤šà¤¨à¥à¤¦à¥à¤°à¤®à¤¾à¤®à¤¾ à¤†à¤§à¤¾à¤°à¤¿à¤¤ à¤¸à¤‚à¤¯à¥‹à¤œà¤¨à¤¹à¤°à¥‚"),
    YOGA_CAT_SOLAR("Solar Yoga", "à¤¸à¥‚à¤°à¥à¤¯ à¤¯à¥‹à¤—"),
    YOGA_CAT_SOLAR_DESC("Sun-based combinations", "à¤¸à¥‚à¤°à¥à¤¯à¤®à¤¾ à¤†à¤§à¤¾à¤°à¤¿à¤¤ à¤¸à¤‚à¤¯à¥‹à¤œà¤¨à¤¹à¤°à¥‚"),
    YOGA_CAT_NEGATIVE("Negative Yoga", "à¤¨à¤•à¤¾à¤°à¤¾à¤¤à¥à¤®à¤• à¤¯à¥‹à¤—"),
    YOGA_CAT_NEGATIVE_DESC("Challenging combinations", "à¤šà¥à¤¨à¥Œà¤¤à¥€à¤ªà¥‚à¤°à¥à¤£ à¤¸à¤‚à¤¯à¥‹à¤œà¤¨à¤¹à¤°à¥‚"),
    YOGA_CAT_SPECIAL("Special Yoga", "à¤µà¤¿à¤¶à¥‡à¤· à¤¯à¥‹à¤—"),
    YOGA_CAT_SPECIAL_DESC("Other significant combinations", "à¤…à¤¨à¥à¤¯ à¤®à¤¹à¤¤à¥à¤¤à¥à¤µà¤ªà¥‚à¤°à¥à¤£ à¤¸à¤‚à¤¯à¥‹à¤œà¤¨à¤¹à¤°à¥‚"),

    YOGA_STRENGTH_EXTREMELY_STRONG("Extremely Strong", "à¤…à¤¤à¥à¤¯à¤¨à¥à¤¤ à¤¬à¤²à¤¿à¤¯à¥‹"),
    YOGA_STRENGTH_STRONG("Strong", "à¤¬à¤²à¤¿à¤¯à¥‹"),
    YOGA_STRENGTH_MODERATE("Moderate", "à¤®à¤§à¥à¤¯à¤®"),
    YOGA_STRENGTH_WEAK("Weak", "à¤•à¤®à¤œà¥‹à¤°"),
    YOGA_STRENGTH_VERY_WEAK("Very Weak", "à¤§à¥‡à¤°à¥ˆ à¤•à¤®à¤œà¥‹à¤°"),

    // ============================================
    // REMEDIES CALCULATOR - CATEGORIES & STRENGTH
    // ============================================
    REMEDY_CAT_GEMSTONE("Gemstone", "à¤°à¤¤à¥à¤¨"),
    REMEDY_CAT_MANTRA("Mantra", "à¤®à¤¨à¥à¤¤à¥à¤°"),
    REMEDY_CAT_YANTRA("Yantra", "à¤¯à¤¨à¥à¤¤à¥à¤°"),
    REMEDY_CAT_CHARITY("Charity", "à¤¦à¤¾à¤¨"),
    REMEDY_CAT_FASTING("Fasting", "à¤‰à¤ªà¤µà¤¾à¤¸"),
    REMEDY_CAT_COLOR("Color Therapy", "à¤°à¤‚à¤— à¤šà¤¿à¤•à¤¿à¤¤à¥à¤¸à¤¾"),
    REMEDY_CAT_METAL("Metal", "à¤§à¤¾à¤¤à¥"),
    REMEDY_CAT_RUDRAKSHA("Rudraksha", "à¤°à¥à¤¦à¥à¤°à¤¾à¤•à¥à¤·"),
    REMEDY_CAT_DEITY("Deity Worship", "à¤¦à¥‡à¤µà¤¤à¤¾ à¤ªà¥‚à¤œà¤¾"),
    REMEDY_CAT_LIFESTYLE("Lifestyle", "à¤œà¥€à¤µà¤¨à¤¶à¥ˆà¤²à¥€"),

    REMEDY_PRIORITY_ESSENTIAL("Essential", "à¤†à¤µà¤¶à¥à¤¯à¤•"),
    REMEDY_PRIORITY_HIGHLY_RECOMMENDED("Highly Recommended", "à¤…à¤¤à¥à¤¯à¤§à¤¿à¤• à¤¸à¤¿à¤«à¤¾à¤°à¤¿à¤¸"),
    REMEDY_PRIORITY_RECOMMENDED("Recommended", "à¤¸à¤¿à¤«à¤¾à¤°à¤¿à¤¸ à¤—à¤°à¤¿à¤à¤•à¥‹"),
    REMEDY_PRIORITY_OPTIONAL("Optional", "à¤µà¥ˆà¤•à¤²à¥à¤ªà¤¿à¤•"),

    PLANETARY_STRENGTH_VERY_STRONG("Very Strong", "à¤§à¥‡à¤°à¥ˆ à¤¬à¤²à¤¿à¤¯à¥‹"),
    PLANETARY_STRENGTH_STRONG("Strong", "à¤¬à¤²à¤¿à¤¯à¥‹"),
    PLANETARY_STRENGTH_MODERATE("Moderate", "à¤®à¤§à¥à¤¯à¤®"),
    PLANETARY_STRENGTH_WEAK("Weak", "à¤•à¤®à¤œà¥‹à¤°"),
    PLANETARY_STRENGTH_VERY_WEAK("Very Weak", "à¤§à¥‡à¤°à¥ˆ à¤•à¤®à¤œà¥‹à¤°"),
    PLANETARY_STRENGTH_AFFLICTED("Afflicted", "à¤ªà¥€à¤¡à¤¿à¤¤"),

    // Shadbala StrengthRating
    SHADBALA_EXTREMELY_WEAK("Extremely Weak", "à¤…à¤¤à¥à¤¯à¤¨à¥à¤¤ à¤•à¤®à¤œà¥‹à¤°"),
    SHADBALA_WEAK("Weak", "à¤•à¤®à¤œà¥‹à¤°"),
    SHADBALA_BELOW_AVERAGE("Below Average", "à¤”à¤¸à¤¤à¤­à¤¨à¥à¤¦à¤¾ à¤¤à¤²"),
    SHADBALA_AVERAGE("Average", "à¤”à¤¸à¤¤"),
    SHADBALA_ABOVE_AVERAGE("Above Average", "à¤”à¤¸à¤¤à¤­à¤¨à¥à¤¦à¤¾ à¤®à¤¾à¤¥à¤¿"),
    SHADBALA_STRONG("Strong", "à¤¬à¤²à¤¿à¤¯à¥‹"),
    SHADBALA_VERY_STRONG("Very Strong", "à¤§à¥‡à¤°à¥ˆ à¤¬à¤²à¤¿à¤¯à¥‹"),
    SHADBALA_EXTREMELY_STRONG("Extremely Strong", "à¤…à¤¤à¥à¤¯à¤¨à¥à¤¤ à¤¬à¤²à¤¿à¤¯à¥‹"),

    // ============================================
    // HOUSE SIGNIFICATIONS (Localized)
    // ============================================
    HOUSE_1_SIGNIFICATION("self-effort and personality", "à¤†à¤¤à¥à¤®-à¤ªà¥à¤°à¤¯à¤¾à¤¸ à¤° à¤µà¥à¤¯à¤•à¥à¤¤à¤¿à¤¤à¥à¤µ"),
    HOUSE_2_SIGNIFICATION("family wealth and speech", "à¤ªà¤¾à¤°à¤¿à¤µà¤¾à¤°à¤¿à¤• à¤§à¤¨ à¤° à¤µà¤¾à¤£à¥€"),
    HOUSE_3_SIGNIFICATION("courage and communication", "à¤¸à¤¾à¤¹à¤¸ à¤° à¤¸à¤žà¥à¤šà¤¾à¤°"),
    HOUSE_4_SIGNIFICATION("property and domestic comfort", "à¤¸à¤®à¥à¤ªà¤¤à¥à¤¤à¤¿ à¤° à¤˜à¤°à¥‡à¤²à¥ à¤¸à¥à¤µà¤¿à¤§à¤¾"),
    HOUSE_5_SIGNIFICATION("speculation and creative ventures", "à¤…à¤¨à¥à¤®à¤¾à¤¨ à¤° à¤¸à¤¿à¤°à¥à¤œà¤¨à¤¾à¤¤à¥à¤®à¤• à¤‰à¤¦à¥à¤¯à¤®à¤¹à¤°à¥‚"),
    HOUSE_6_SIGNIFICATION("service and defeating competition", "à¤¸à¥‡à¤µà¤¾ à¤° à¤ªà¥à¤°à¤¤à¤¿à¤¸à¥à¤ªà¤°à¥à¤§à¤¾ à¤œà¤¿à¤¤à¥à¤¨à¥"),
    HOUSE_7_SIGNIFICATION("partnership and business", "à¤¸à¤¾à¤à¥‡à¤¦à¤¾à¤°à¥€ à¤° à¤µà¥à¤¯à¤¾à¤ªà¤¾à¤°"),
    HOUSE_8_SIGNIFICATION("inheritance and unexpected gains", "à¤µà¤¿à¤°à¤¾à¤¸à¤¤ à¤° à¤…à¤ªà¥à¤°à¤¤à¥à¤¯à¤¾à¤¶à¤¿à¤¤ à¤²à¤¾à¤­"),
    HOUSE_9_SIGNIFICATION("fortune and higher pursuits", "à¤­à¤¾à¤—à¥à¤¯ à¤° à¤‰à¤šà¥à¤š à¤–à¥‹à¤œà¥€à¤¹à¤°à¥‚"),
    HOUSE_10_SIGNIFICATION("career and public recognition", "à¤•à¥à¤¯à¤¾à¤°à¤¿à¤¯à¤° à¤° à¤¸à¤¾à¤°à¥à¤µà¤œà¤¨à¤¿à¤• à¤®à¤¾à¤¨à¥à¤¯à¤¤à¤¾"),
    HOUSE_11_SIGNIFICATION("gains and social networks", "à¤²à¤¾à¤­ à¤° à¤¸à¤¾à¤®à¤¾à¤œà¤¿à¤• à¤¸à¤žà¥à¤œà¤¾à¤²à¤¹à¤°à¥‚"),
    HOUSE_12_SIGNIFICATION("foreign connections and spiritual pursuits", "à¤µà¤¿à¤¦à¥‡à¤¶à¥€ à¤¸à¤®à¥à¤¬à¤¨à¥à¤§ à¤° à¤†à¤§à¥à¤¯à¤¾à¤¤à¥à¤®à¤¿à¤• à¤–à¥‹à¤œà¥€à¤¹à¤°à¥‚"),
    VARIOUS_ACTIVITIES("various activities", "à¤µà¤¿à¤­à¤¿à¤¨à¥à¤¨ à¤—à¤¤à¤¿à¤µà¤¿à¤§à¤¿à¤¹à¤°à¥‚"),

    // ============================================
    // REPORT HEADERS & SECTIONS
    // ============================================
    REPORT_YOGA_ANALYSIS("YOGA ANALYSIS REPORT", "à¤¯à¥‹à¤— à¤µà¤¿à¤¶à¥à¤²à¥‡à¤·à¤£ à¤°à¤¿à¤ªà¥‹à¤°à¥à¤Ÿ"),
    REPORT_TOTAL_YOGAS("Total Yogas Found", "à¤•à¥à¤² à¤¯à¥‹à¤—à¤¹à¤°à¥‚ à¤«à¥‡à¤²à¤¾ à¤ªà¤°à¥‡à¤•à¥‹"),
    REPORT_OVERALL_STRENGTH("Overall Yoga Strength", "à¤¸à¤®à¤—à¥à¤° à¤¯à¥‹à¤— à¤¬à¤²"),
    REPORT_DOMINANT_CATEGORY("Dominant Category", "à¤ªà¥à¤°à¤®à¥à¤– à¤µà¤°à¥à¤—"),
    REPORT_PLANETS("Planets", "à¤—à¥à¤°à¤¹à¤¹à¤°à¥‚"),
    REPORT_HOUSES("Houses", "à¤­à¤¾à¤µà¤¹à¤°à¥‚"),
    REPORT_EFFECTS("Effects", "à¤ªà¥à¤°à¤­à¤¾à¤µà¤¹à¤°à¥‚"),
    REPORT_ACTIVATION("Activation", "à¤¸à¤•à¥à¤°à¤¿à¤¯à¤¤à¤¾"),
    REPORT_PATTERN("Pattern", "à¤¢à¤¾à¤à¤šà¤¾"),
    REPORT_CANCELLATION_FACTORS("Cancellation Factors", "à¤°à¤¦à¥à¤¦ à¤—à¤°à¥à¤¨à¥‡ à¤•à¤¾à¤°à¤•à¤¹à¤°à¥‚"),
    REPORT_AUSPICIOUS("Auspicious", "à¤¶à¥à¤­"),
    REPORT_INAUSPICIOUS("Inauspicious", "à¤…à¤¶à¥à¤­"),

    REPORT_REMEDIES("VEDIC ASTROLOGY REMEDIES REPORT", "à¤µà¥ˆà¤¦à¤¿à¤• à¤œà¥à¤¯à¥‹à¤¤à¤¿à¤· à¤‰à¤ªà¤¾à¤¯ à¤°à¤¿à¤ªà¥‹à¤°à¥à¤Ÿ"),
    REPORT_PLANETARY_STRENGTH_ANALYSIS("PLANETARY STRENGTH ANALYSIS", "à¤—à¥à¤°à¤¹ à¤¬à¤² à¤µà¤¿à¤¶à¥à¤²à¥‡à¤·à¤£"),
    REPORT_PLANETS_REQUIRING_ATTENTION("PLANETS REQUIRING ATTENTION", "à¤§à¥à¤¯à¤¾à¤¨ à¤†à¤µà¤¶à¥à¤¯à¤• à¤—à¥à¤°à¤¹à¤¹à¤°à¥‚"),
    REPORT_RECOMMENDED_REMEDIES("RECOMMENDED REMEDIES", "à¤¸à¤¿à¤«à¤¾à¤°à¤¿à¤¸ à¤—à¤°à¤¿à¤à¤•à¤¾ à¤‰à¤ªà¤¾à¤¯à¤¹à¤°à¥‚"),
    REPORT_GENERAL_RECOMMENDATIONS("GENERAL RECOMMENDATIONS", "à¤¸à¤¾à¤®à¤¾à¤¨à¥à¤¯ à¤¸à¤¿à¤«à¤¾à¤°à¤¿à¤¸à¤¹à¤°à¥‚"),
    REPORT_SUMMARY("SUMMARY", "à¤¸à¤¾à¤°à¤¾à¤‚à¤¶"),
    REPORT_GENERATED_BY("Generated by AstroStorm - Ultra-Precision Vedic Astrology", "AstroStorm à¤¦à¥à¤µà¤¾à¤°à¤¾ à¤‰à¤¤à¥à¤ªà¤¨à¥à¤¨ - à¤…à¤¤à¤¿-à¤¸à¤Ÿà¥€à¤• à¤µà¥ˆà¤¦à¤¿à¤• à¤œà¥à¤¯à¥‹à¤¤à¤¿à¤·"),
    REPORT_CATEGORY("Category", "à¤µà¤°à¥à¤—"),
    REPORT_PLANET("Planet", "à¤—à¥à¤°à¤¹"),
    REPORT_METHOD("Method", "à¤µà¤¿à¤§à¤¿"),
    REPORT_TIMING("Timing", "à¤¸à¤®à¤¯"),

    REPORT_MATCHMAKING("KUNDLI MILAN (MATCHMAKING) REPORT", "à¤•à¥à¤£à¥à¤¡à¤²à¥€ à¤®à¤¿à¤²à¤¾à¤¨ à¤°à¤¿à¤ªà¥‹à¤°à¥à¤Ÿ"),
    REPORT_MATCHMAKING_TITLE("KUNDLI MILAN REPORT", "à¤•à¥à¤£à¥à¤¡à¤²à¥€ à¤®à¤¿à¤²à¤¾à¤¨ à¤°à¤¿à¤ªà¥‹à¤°à¥à¤Ÿ"),
    REPORT_ASTROSTORM_ANALYSIS("AstroStorm Analysis", "AstroStorm à¤µà¤¿à¤¶à¥à¤²à¥‡à¤·à¤£"),
    REPORT_PROFILES("PROFILES", "à¤ªà¥à¤°à¥‹à¤«à¤¾à¤‡à¤²à¤¹à¤°à¥‚"),
    REPORT_BRIDE("BRIDE", "à¤µà¤§à¥‚"),
    REPORT_BRIDE_LABEL("Bride:", "à¤µà¤§à¥‚:"),
    REPORT_GROOM("GROOM", "à¤µà¤°"),
    REPORT_GROOM_LABEL("Groom:", "à¤µà¤°:"),
    REPORT_MOON_SIGN_LABEL("Moon Sign:", "à¤šà¤¨à¥à¤¦à¥à¤° à¤°à¤¾à¤¶à¤¿:"),
    REPORT_NAKSHATRA_LABEL("Nakshatra:", "à¤¨à¤•à¥à¤·à¤¤à¥à¤°:"),
    REPORT_COMPATIBILITY_SCORE("COMPATIBILITY SCORE", "à¤…à¤¨à¥à¤•à¥‚à¤²à¤¤à¤¾ à¤…à¤‚à¤•"),
    REPORT_TOTAL_POINTS("Total Points:", "à¤•à¥à¤² à¤…à¤‚à¤•:"),
    REPORT_PERCENTAGE("Percentage:", "à¤ªà¥à¤°à¤¤à¤¿à¤¶à¤¤:"),
    REPORT_RATING_LABEL("Rating:", "à¤®à¥‚à¤²à¥à¤¯à¤¾à¤™à¥à¤•à¤¨:"),
    REPORT_ASHTAKOOTA("ASHTAKOOTA ANALYSIS", "à¤…à¤·à¥à¤Ÿà¤•à¥‚à¤Ÿ à¤µà¤¿à¤¶à¥à¤²à¥‡à¤·à¤£"),
    REPORT_ASHTAKOOTA_8_GUNA("ASHTAKOOTA (8 GUNA) ANALYSIS", "à¤…à¤·à¥à¤Ÿà¤•à¥‚à¤Ÿ (à¥® à¤—à¥à¤£) à¤µà¤¿à¤¶à¥à¤²à¥‡à¤·à¤£"),
    REPORT_SCORE_LABEL("Score:", "à¤…à¤‚à¤•:"),
    REPORT_GUNA("GUNA", "à¤—à¥à¤£"),
    REPORT_MAX("MAX", "à¤…à¤§à¤¿à¤•à¤¤à¤®"),
    REPORT_OBTAINED("OBTAINED", "à¤ªà¥à¤°à¤¾à¤ªà¥à¤¤"),
    REPORT_STATUS("STATUS", "à¤¸à¥à¤¥à¤¿à¤¤à¤¿"),
    REPORT_TOTAL("TOTAL", "à¤•à¥à¤²"),
    REPORT_OVERALL_RATING("OVERALL RATING", "à¤¸à¤®à¤—à¥à¤° à¤®à¥‚à¤²à¥à¤¯à¤¾à¤™à¥à¤•à¤¨"),
    REPORT_ADDITIONAL_FACTORS("ADDITIONAL FACTORS", "à¤¥à¤ª à¤•à¤¾à¤°à¤•à¤¹à¤°à¥‚"),
    REPORT_MANGLIK_ANALYSIS("MANGLIK ANALYSIS", "à¤®à¤¾à¤‚à¤—à¤²à¤¿à¤• à¤µà¤¿à¤¶à¥à¤²à¥‡à¤·à¤£"),
    REPORT_MANGLIK_DOSHA_ANALYSIS("MANGLIK DOSHA ANALYSIS", "à¤®à¤¾à¤‚à¤—à¤²à¤¿à¤• à¤¦à¥‹à¤· à¤µà¤¿à¤¶à¥à¤²à¥‡à¤·à¤£"),
    REPORT_MARS_IN_HOUSE("Mars in House %d", "à¤­à¤¾à¤µ %d à¤®à¤¾ à¤®à¤‚à¤—à¤²"),
    REPORT_CANCELLATION("(Cancellation)", "(à¤°à¤¦à¥à¤¦)"),
    REPORT_SPECIAL_CONSIDERATIONS("SPECIAL CONSIDERATIONS", "à¤µà¤¿à¤¶à¥‡à¤· à¤µà¤¿à¤šà¤¾à¤°à¤¹à¤°à¥‚"),
    REPORT_SUGGESTED_REMEDIES("SUGGESTED REMEDIES", "à¤¸à¥à¤à¤¾à¤µ à¤—à¤°à¤¿à¤à¤•à¤¾ à¤‰à¤ªà¤¾à¤¯à¤¹à¤°à¥‚"),
    REPORT_COMPATIBILITY("Compatibility", "à¤…à¤¨à¥à¤•à¥‚à¤²à¤¤à¤¾"),
    REPORT_COMPATIBILITY_LABEL("Compatibility:", "à¤…à¤¨à¥à¤•à¥‚à¤²à¤¤à¤¾:"),
    REPORT_NOT_PRESENT("Not Present", "à¤‰à¤ªà¤¸à¥à¤¥à¤¿à¤¤ à¤›à¥ˆà¤¨"),
    REPORT_COMPATIBLE("Compatible", "à¤…à¤¨à¥à¤•à¥‚à¤²"),
    REPORT_SATISFIED("Satisfied", "à¤¸à¤¨à¥à¤¤à¥à¤·à¥à¤Ÿ"),
    REPORT_NOT_SATISFIED("Not satisfied", "à¤¸à¤¨à¥à¤¤à¥à¤·à¥à¤Ÿ à¤›à¥ˆà¤¨"),
    REPORT_NOT_APPLICABLE("Not applicable", "à¤²à¤¾à¤—à¥‚ à¤¹à¥à¤à¤¦à¥ˆà¤¨"),
    REPORT_NA("N/A", "à¤‰à¤ªà¤²à¤¬à¥à¤§ à¤›à¥ˆà¤¨"),
    REPORT_ASTROSTORM_VEDIC("Vedic Astrology â€¢ Ultra-Precision", "à¤µà¥ˆà¤¦à¤¿à¤• à¤œà¥à¤¯à¥‹à¤¤à¤¿à¤· â€¢ à¤…à¤¤à¤¿-à¤¸à¤Ÿà¥€à¤•"),
    REPORT_KUNDLI_MILAN_SUMMARY("KUNDLI MILAN SUMMARY", "à¤•à¥à¤£à¥à¤¡à¤²à¥€ à¤®à¤¿à¤²à¤¾à¤¨ à¤¸à¤¾à¤°à¤¾à¤‚à¤¶"),
    REPORT_MANGLIK_LABEL("Manglik:", "à¤®à¤¾à¤‚à¤—à¤²à¤¿à¤•:"),
    REPORT_ASHTAKOOTA_GUNA_SCORES("ASHTAKOOTA GUNA SCORES", "à¤…à¤·à¥à¤Ÿà¤•à¥‚à¤Ÿ à¤—à¥à¤£ à¤…à¤‚à¤•à¤¹à¤°à¥‚"),

    // ============================================
    // SPECIFIC YOGA NAMES (For display)
    // ============================================
    YOGA_KENDRA_TRIKONA("Kendra-Trikona Raja Yoga", "à¤•à¥‡à¤¨à¥à¤¦à¥à¤°-à¤¤à¥à¤°à¤¿à¤•à¥‹à¤£ à¤°à¤¾à¤œ à¤¯à¥‹à¤—"),
    YOGA_PARIVARTANA("Parivartana Raja Yoga", "à¤ªà¤°à¤¿à¤µà¤°à¥à¤¤à¤¨ à¤°à¤¾à¤œ à¤¯à¥‹à¤—"),
    YOGA_VIPARITA("Viparita Raja Yoga", "à¤µà¤¿à¤ªà¤°à¥€à¤¤ à¤°à¤¾à¤œ à¤¯à¥‹à¤—"),
    YOGA_NEECHA_BHANGA("Neecha Bhanga Raja Yoga", "à¤¨à¥€à¤š à¤­à¤‚à¤— à¤°à¤¾à¤œ à¤¯à¥‹à¤—"),
    YOGA_MAHA_RAJA("Maha Raja Yoga", "à¤®à¤¹à¤¾ à¤°à¤¾à¤œ à¤¯à¥‹à¤—"),
    YOGA_LAKSHMI("Lakshmi Yoga", "à¤²à¤•à¥à¤·à¥à¤®à¥€ à¤¯à¥‹à¤—"),
    YOGA_KUBERA("Kubera Yoga", "à¤•à¥à¤¬à¥‡à¤° à¤¯à¥‹à¤—"),
    YOGA_CHANDRA_MANGALA("Chandra-Mangala Yoga", "à¤šà¤¨à¥à¤¦à¥à¤°-à¤®à¤‚à¤—à¤² à¤¯à¥‹à¤—"),
    YOGA_LABHA("Labha Yoga", "à¤²à¤¾à¤­ à¤¯à¥‹à¤—"),
    YOGA_RUCHAKA("Ruchaka Yoga", "à¤°à¥à¤šà¤• à¤¯à¥‹à¤—"),
    YOGA_BHADRA("Bhadra Yoga", "à¤­à¤¦à¥à¤° à¤¯à¥‹à¤—"),
    YOGA_HAMSA("Hamsa Yoga", "à¤¹à¤‚à¤¸ à¤¯à¥‹à¤—"),
    YOGA_MALAVYA("Malavya Yoga", "à¤®à¤¾à¤²à¤µà¥à¤¯ à¤¯à¥‹à¤—"),
    YOGA_SASA("Sasa Yoga", "à¤¶à¤¶ à¤¯à¥‹à¤—"),
    YOGA_SUNAFA("Sunafa Yoga", "à¤¸à¥à¤¨à¤«à¤¾ à¤¯à¥‹à¤—"),
    YOGA_ANAFA("Anafa Yoga", "à¤…à¤¨à¤«à¤¾ à¤¯à¥‹à¤—"),
    YOGA_DURUDHARA("Durudhara Yoga", "à¤¦à¥à¤°à¥à¤§à¤°à¤¾ à¤¯à¥‹à¤—"),
    YOGA_GAJA_KESARI("Gaja-Kesari Yoga", "à¤—à¤œ-à¤•à¥‡à¤¸à¤°à¥€ à¤¯à¥‹à¤—"),
    YOGA_ADHI("Adhi Yoga", "à¤…à¤§à¤¿ à¤¯à¥‹à¤—"),
    YOGA_VESI("Vesi Yoga", "à¤µà¥‡à¤¶à¥€ à¤¯à¥‹à¤—"),
    YOGA_VOSI("Vosi Yoga", "à¤µà¥‹à¤¶à¥€ à¤¯à¥‹à¤—"),
    YOGA_UBHAYACHARI("Ubhayachari Yoga", "à¤‰à¤­à¤¯à¤šà¤¾à¤°à¥€ à¤¯à¥‹à¤—"),
    YOGA_KEMADRUMA("Kemadruma Yoga", "à¤•à¥‡à¤®à¤¦à¥à¤°à¥à¤® à¤¯à¥‹à¤—"),
    YOGA_DARIDRA("Daridra Yoga", "à¤¦à¤°à¤¿à¤¦à¥à¤° à¤¯à¥‹à¤—"),
    YOGA_SHAKATA("Shakata Yoga", "à¤¶à¤•à¤Ÿ à¤¯à¥‹à¤—"),
    YOGA_GURU_CHANDAL("Guru-Chandal Yoga", "à¤—à¥à¤°à¥-à¤šà¤¾à¤‚à¤¡à¤¾à¤² à¤¯à¥‹à¤—"),
    YOGA_BUDHA_ADITYA("Budha-Aditya Yoga", "à¤¬à¥à¤§-à¤†à¤¦à¤¿à¤¤à¥à¤¯ à¤¯à¥‹à¤—"),
    YOGA_AMALA("Amala Yoga", "à¤…à¤®à¤²à¤¾ à¤¯à¥‹à¤—"),
    YOGA_SARASWATI("Saraswati Yoga", "à¤¸à¤°à¤¸à¥à¤µà¤¤à¥€ à¤¯à¥‹à¤—"),
    YOGA_PARVATA("Parvata Yoga", "à¤ªà¤°à¥à¤µà¤¤ à¤¯à¥‹à¤—"),
    YOGA_KAHALA("Kahala Yoga", "à¤•à¤¹à¤² à¤¯à¥‹à¤—"),
    YOGA_YAVA("Yava Yoga", "à¤¯à¤µ à¤¯à¥‹à¤—"),
    YOGA_SHRINGATAKA("Shringataka Yoga", "à¤¶à¥ƒà¤‚à¤—à¤¾à¤Ÿà¤• à¤¯à¥‹à¤—"),
    YOGA_GADA("Gada Yoga", "à¤—à¤¦à¤¾ à¤¯à¥‹à¤—"),
    YOGA_RAJJU("Rajju Yoga", "à¤°à¤œà¥à¤œà¥ à¤¯à¥‹à¤—"),
    YOGA_MUSALA("Musala Yoga", "à¤®à¥à¤¸à¤² à¤¯à¥‹à¤—"),
    YOGA_NALA("Nala Yoga", "à¤¨à¤² à¤¯à¥‹à¤—"),
    YOGA_KEDARA("Kedara Yoga", "à¤•à¥‡à¤¦à¤¾à¤° à¤¯à¥‹à¤—"),
    YOGA_SHOOLA("Shoola Yoga", "à¤¶à¥‚à¤² à¤¯à¥‹à¤—"),
    YOGA_YUGA("Yuga Yoga", "à¤¯à¥à¤— à¤¯à¥‹à¤—"),
    YOGA_GOLA("Gola Yoga", "à¤—à¥‹à¤² à¤¯à¥‹à¤—"),
    YOGA_VEENA("Veena Yoga", "à¤µà¥€à¤£à¤¾ à¤¯à¥‹à¤—"),
    YOGA_DASA_MULA("Dasa-Mula Yoga", "à¤¦à¤¶-à¤®à¥‚à¤² à¤¯à¥‹à¤—"),
    YOGA_VARGOTTAMA_STRENGTH("Vargottama Strength", "à¤µà¤°à¥à¤—à¥‹à¤¤à¥à¤¤à¤® à¤¬à¤²"),

    // New Yogas - Grahan and Nodal Combinations
    YOGA_SURYA_GRAHAN("Surya Grahan Yoga", "à¤¸à¥‚à¤°à¥à¤¯ à¤—à¥à¤°à¤¹à¤£ à¤¯à¥‹à¤—"),
    YOGA_SURYA_KETU_GRAHAN("Surya-Ketu Grahan Yoga", "à¤¸à¥‚à¤°à¥à¤¯-à¤•à¥‡à¤¤à¥ à¤—à¥à¤°à¤¹à¤£ à¤¯à¥‹à¤—"),
    YOGA_CHANDRA_GRAHAN("Chandra Grahan Yoga", "à¤šà¤¨à¥à¤¦à¥à¤° à¤—à¥à¤°à¤¹à¤£ à¤¯à¥‹à¤—"),
    YOGA_CHANDRA_KETU("Chandra-Ketu Yoga", "à¤šà¤¨à¥à¤¦à¥à¤°-à¤•à¥‡à¤¤à¥ à¤¯à¥‹à¤—"),
    YOGA_ANGARAK("Angarak Yoga", "à¤…à¤™à¥à¤—à¤¾à¤°à¤• à¤¯à¥‹à¤—"),
    YOGA_SHRAPIT("Shrapit Yoga", "à¤¶à¤¾à¤ªà¤¿à¤¤ à¤¯à¥‹à¤—"),
    YOGA_KALA_SARPA("Kala Sarpa Yoga", "à¤•à¤¾à¤²à¤¸à¤°à¥à¤ª à¤¯à¥‹à¤—"),
    YOGA_PAPAKARTARI("Papakartari Yoga", "à¤ªà¤¾à¤ªà¤•à¤°à¥à¤¤à¤°à¥€ à¤¯à¥‹à¤—"),
    YOGA_SHUBHAKARTARI("Shubhakartari Yoga", "à¤¶à¥à¤­à¤•à¤°à¥à¤¤à¤°à¥€ à¤¯à¥‹à¤—"),
    YOGA_SANYASA("Sanyasa Yoga", "à¤¸à¤¨à¥à¤¯à¤¾à¤¸ à¤¯à¥‹à¤—"),
    YOGA_CHAMARA("Chamara Yoga", "à¤šà¤¾à¤®à¤° à¤¯à¥‹à¤—"),
    YOGA_DHARMA_KARMADHIPATI("Dharma-Karmadhipati Yoga", "à¤§à¤°à¥à¤®-à¤•à¤°à¥à¤®à¤¾à¤§à¤¿à¤ªà¤¤à¤¿ à¤¯à¥‹à¤—"),

    // New Yoga Effects
    YOGA_EFFECT_SURYA_GRAHAN("Father-related troubles, ego issues, government problems, health issues with head/eyes", "à¤ªà¤¿à¤¤à¤¾ à¤¸à¤®à¥à¤¬à¤¨à¥à¤§à¥€ à¤¸à¤®à¤¸à¥à¤¯à¤¾à¤¹à¤°à¥‚, à¤…à¤¹à¤‚à¤•à¤¾à¤° à¤¸à¤®à¤¸à¥à¤¯à¤¾à¤¹à¤°à¥‚, à¤¸à¤°à¤•à¤¾à¤°à¥€ à¤¸à¤®à¤¸à¥à¤¯à¤¾à¤¹à¤°à¥‚, à¤Ÿà¤¾à¤‰à¤•à¥‹/à¤†à¤à¤–à¤¾à¤®à¤¾ à¤¸à¥à¤µà¤¾à¤¸à¥à¤¥à¥à¤¯ à¤¸à¤®à¤¸à¥à¤¯à¤¾à¤¹à¤°à¥‚"),
    YOGA_EFFECT_SURYA_KETU_GRAHAN("Spiritual detachment, low self-esteem, father troubles, past-life karmic issues", "à¤†à¤§à¥à¤¯à¤¾à¤¤à¥à¤®à¤¿à¤• à¤µà¤¿à¤°à¤•à¥à¤¤à¤¿, à¤•à¤® à¤†à¤¤à¥à¤®à¤¸à¤®à¥à¤®à¤¾à¤¨, à¤ªà¤¿à¤¤à¤¾ à¤¸à¤®à¤¸à¥à¤¯à¤¾à¤¹à¤°à¥‚, à¤ªà¥‚à¤°à¥à¤µà¤œà¤¨à¥à¤® à¤•à¤°à¥à¤® à¤¸à¤®à¤¸à¥à¤¯à¤¾à¤¹à¤°à¥‚"),
    YOGA_EFFECT_CHANDRA_GRAHAN("Mental restlessness, mother troubles, emotional instability, obsessive tendencies", "à¤®à¤¾à¤¨à¤¸à¤¿à¤• à¤…à¤¶à¤¾à¤¨à¥à¤¤à¤¿, à¤†à¤®à¤¾ à¤¸à¤®à¤¸à¥à¤¯à¤¾à¤¹à¤°à¥‚, à¤­à¤¾à¤µà¤¨à¤¾à¤¤à¥à¤®à¤• à¤…à¤¸à¥à¤¥à¤¿à¤°à¤¤à¤¾, à¤œà¥à¤¨à¥‚à¤¨à¥€ à¤ªà¥à¤°à¤µà¥ƒà¤¤à¥à¤¤à¤¿à¤¹à¤°à¥‚"),
    YOGA_EFFECT_CHANDRA_KETU("Detachment from emotions, past-life memories, psychic sensitivity, mother karma", "à¤­à¤¾à¤µà¤¨à¤¾à¤¹à¤°à¥‚à¤¬à¤¾à¤Ÿ à¤µà¤¿à¤°à¤•à¥à¤¤à¤¿, à¤ªà¥‚à¤°à¥à¤µà¤œà¤¨à¥à¤® à¤¸à¥à¤®à¥ƒà¤¤à¤¿à¤¹à¤°à¥‚, à¤®à¤¨à¥‹à¤µà¥ˆà¤œà¥à¤žà¤¾à¤¨à¤¿à¤• à¤¸à¤‚à¤µà¥‡à¤¦à¤¨à¤¶à¥€à¤²à¤¤à¤¾, à¤†à¤®à¤¾ à¤•à¤°à¥à¤®"),
    YOGA_EFFECT_ANGARAK("Accidents, surgery, aggression, sibling troubles, litigation, sudden events", "à¤¦à¥à¤°à¥à¤˜à¤Ÿà¤¨à¤¾à¤¹à¤°à¥‚, à¤¶à¤²à¥à¤¯à¤•à¥à¤°à¤¿à¤¯à¤¾, à¤†à¤•à¥à¤°à¤¾à¤®à¤•à¤¤à¤¾, à¤­à¤¾à¤‡à¤¬à¤¹à¤¿à¤¨à¥€ à¤¸à¤®à¤¸à¥à¤¯à¤¾à¤¹à¤°à¥‚, à¤®à¥à¤¦à¥à¤¦à¤¾, à¤…à¤šà¤¾à¤¨à¤• à¤˜à¤Ÿà¤¨à¤¾à¤¹à¤°à¥‚"),
    YOGA_EFFECT_SHRAPIT("Past-life karma manifesting as chronic obstacles, delays, fear, ancestral issues", "à¤ªà¥‚à¤°à¥à¤µà¤œà¤¨à¥à¤® à¤•à¤°à¥à¤® à¤¦à¥€à¤°à¥à¤˜à¤•à¤¾à¤²à¥€à¤¨ à¤¬à¤¾à¤§à¤¾à¤¹à¤°à¥‚, à¤¢à¤¿à¤²à¤¾à¤‡, à¤¡à¤°, à¤ªà¥à¤°à¥à¤–à¥Œà¤²à¥€ à¤¸à¤®à¤¸à¥à¤¯à¤¾à¤¹à¤°à¥‚à¤•à¥‹ à¤°à¥‚à¤ªà¤®à¤¾ à¤ªà¥à¤°à¤•à¤Ÿ"),
    YOGA_EFFECT_KALA_SARPA("Karmic life patterns, sudden ups and downs, spiritual transformation potential", "à¤•à¤°à¥à¤®à¤œà¤¨à¥à¤¯ à¤œà¥€à¤µà¤¨ à¤¢à¤¾à¤à¤šà¤¾à¤¹à¤°à¥‚, à¤…à¤šà¤¾à¤¨à¤• à¤‰à¤¤à¤¾à¤°-à¤šà¤¢à¤¾à¤µ, à¤†à¤§à¥à¤¯à¤¾à¤¤à¥à¤®à¤¿à¤• à¤°à¥‚à¤ªà¤¾à¤¨à¥à¤¤à¤°à¤£ à¤¸à¤®à¥à¤­à¤¾à¤µà¤¨à¤¾"),
    YOGA_EFFECT_PAPAKARTARI("Obstacles in self-expression, health challenges, restricted opportunities", "à¤†à¤¤à¥à¤®-à¤…à¤­à¤¿à¤µà¥à¤¯à¤•à¥à¤¤à¤¿à¤®à¤¾ à¤¬à¤¾à¤§à¤¾à¤¹à¤°à¥‚, à¤¸à¥à¤µà¤¾à¤¸à¥à¤¥à¥à¤¯ à¤šà¥à¤¨à¥Œà¤¤à¥€à¤¹à¤°à¥‚, à¤¸à¥€à¤®à¤¿à¤¤ à¤…à¤µà¤¸à¤°à¤¹à¤°à¥‚"),
    YOGA_EFFECT_SHUBHAKARTARI("Protected life, good health, success in endeavors, helpful people around", "à¤¸à¥à¤°à¤•à¥à¤·à¤¿à¤¤ à¤œà¥€à¤µà¤¨, à¤°à¤¾à¤®à¥à¤°à¥‹ à¤¸à¥à¤µà¤¾à¤¸à¥à¤¥à¥à¤¯, à¤ªà¥à¤°à¤¯à¤¾à¤¸à¤¹à¤°à¥‚à¤®à¤¾ à¤¸à¤«à¤²à¤¤à¤¾, à¤µà¤°à¤ªà¤° à¤®à¤¦à¥à¤¦à¤¤à¤—à¤°à¥à¤¨à¥‡ à¤®à¤¾à¤¨à¤¿à¤¸à¤¹à¤°à¥‚"),
    YOGA_EFFECT_SANYASA("Renunciation tendencies, spiritual inclinations, detachment from worldly matters", "à¤¤à¥à¤¯à¤¾à¤— à¤ªà¥à¤°à¤µà¥ƒà¤¤à¥à¤¤à¤¿à¤¹à¤°à¥‚, à¤†à¤§à¥à¤¯à¤¾à¤¤à¥à¤®à¤¿à¤• à¤à¥à¤•à¤¾à¤µ, à¤¸à¤¾à¤‚à¤¸à¤¾à¤°à¤¿à¤• à¤•à¥à¤°à¤¾à¤¹à¤°à¥‚à¤¬à¤¾à¤Ÿ à¤µà¤¿à¤°à¤•à¥à¤¤à¤¿"),
    YOGA_EFFECT_CHAMARA("Royal honors, fame, eloquence, learned, respected by rulers", "à¤°à¤¾à¤œà¤•à¥€à¤¯ à¤¸à¤®à¥à¤®à¤¾à¤¨, à¤ªà¥à¤°à¤¸à¤¿à¤¦à¥à¤§à¤¿, à¤µà¤¾à¤•à¥à¤ªà¤Ÿà¥à¤¤à¤¾, à¤ªà¤¢à¥‡à¤²à¥‡à¤–à¥‡à¤•à¥‹, à¤¶à¤¾à¤¸à¤•à¤¹à¤°à¥‚à¤¦à¥à¤µà¤¾à¤°à¤¾ à¤¸à¤®à¥à¤®à¤¾à¤¨à¤¿à¤¤"),
    YOGA_EFFECT_DHARMA_KARMADHIPATI("Highly successful career, fortune through profession, fame, authority positions", "à¤…à¤¤à¥à¤¯à¤¨à¥à¤¤ à¤¸à¤«à¤² à¤•à¤°à¤¿à¤¯à¤°, à¤ªà¥‡à¤¶à¤¾à¤¬à¤¾à¤Ÿ à¤­à¤¾à¤—à¥à¤¯, à¤ªà¥à¤°à¤¸à¤¿à¤¦à¥à¤§à¤¿, à¤…à¤§à¤¿à¤•à¤¾à¤° à¤ªà¤¦à¤¹à¤°à¥‚"),

    // Yoga Effects Translations
    YOGA_EFFECT_RUCHAKA("Commander, army chief, valorous, muscular body, red complexion, successful in conflicts, skilled in warfare, leader of thieves or soldiers, wealth through martial arts or defense", "à¤¸à¥‡à¤¨à¤¾à¤ªà¤¤à¤¿, à¤¸à¥‡à¤¨à¤¾ à¤ªà¥à¤°à¤®à¥à¤–, à¤µà¥€à¤°, à¤¬à¤²à¤¿à¤¯à¥‹ à¤¶à¤°à¥€à¤°, à¤°à¤¾à¤¤à¥‹ à¤°à¤‚à¤—, à¤¦à¥à¤µà¤¨à¥à¤¦à¥à¤µà¤®à¤¾ à¤¸à¤«à¤², à¤¯à¥à¤¦à¥à¤§ à¤•à¤²à¤¾à¤®à¤¾ à¤¦à¤•à¥à¤·, à¤šà¥‹à¤° à¤µà¤¾ à¤¸à¥ˆà¤¨à¤¿à¤•à¤¹à¤°à¥‚à¤•à¥‹ à¤¨à¥‡à¤¤à¤¾, à¤¯à¥à¤¦à¥à¤§ à¤•à¤²à¤¾ à¤µà¤¾ à¤°à¤•à¥à¤·à¤¾à¤¬à¤¾à¤Ÿ à¤§à¤¨"),
    YOGA_EFFECT_BHADRA("Intelligent, eloquent speaker, skilled in arts and sciences, long-lived, wealthy through intellect, respected in assemblies, lion-like face, broad chest", "à¤¬à¥à¤¦à¥à¤§à¤¿à¤®à¤¾à¤¨, à¤µà¤¾à¤•à¥à¤ªà¤Ÿà¥ à¤µà¤•à¥à¤¤à¤¾, à¤•à¤²à¤¾ à¤° à¤µà¤¿à¤œà¥à¤žà¤¾à¤¨à¤®à¤¾ à¤¦à¤•à¥à¤·, à¤¦à¥€à¤°à¥à¤˜à¤¾à¤¯à¥, à¤¬à¥à¤¦à¥à¤§à¤¿à¤¬à¤¾à¤Ÿ à¤§à¤¨à¥€, à¤¸à¤­à¤¾à¤®à¤¾ à¤¸à¤®à¥à¤®à¤¾à¤¨à¤¿à¤¤, à¤¸à¤¿à¤‚à¤¹ à¤œà¤¸à¥à¤¤à¥‹ à¤®à¥à¤–, à¤«à¤°à¤¾à¤•à¤¿à¤²à¥‹ à¤›à¤¾à¤¤à¥€"),
    YOGA_EFFECT_HAMSA("Righteous king, fair complexion, elevated nose, beautiful face, devoted to gods and brahmins, fond of water sports, walks like a swan, respected by rulers, spiritual inclinations", "à¤§à¤¾à¤°à¥à¤®à¤¿à¤• à¤°à¤¾à¤œà¤¾, à¤—à¥‹à¤°à¥‹ à¤°à¤‚à¤—, à¤‰à¤ à¥‡à¤•à¥‹ à¤¨à¤¾à¤•, à¤¸à¥à¤¨à¥à¤¦à¤° à¤®à¥à¤–, à¤¦à¥‡à¤µà¤¤à¤¾ à¤° à¤¬à¥à¤°à¤¾à¤¹à¥à¤®à¤£à¤¹à¤°à¥‚à¤ªà¥à¤°à¤¤à¤¿ à¤­à¤•à¥à¤¤, à¤œà¤² à¤•à¥à¤°à¥€à¤¡à¤¾à¤•à¥‹ à¤¶à¥Œà¤•à¥€à¤¨, à¤¹à¤‚à¤¸ à¤œà¤¸à¥à¤¤à¥‹ à¤¹à¤¿à¤à¤¡à¥à¤¨à¥‡, à¤¶à¤¾à¤¸à¤•à¤¹à¤°à¥‚à¤¦à¥à¤µà¤¾à¤°à¤¾ à¤¸à¤®à¥à¤®à¤¾à¤¨à¤¿à¤¤, à¤†à¤§à¥à¤¯à¤¾à¤¤à¥à¤®à¤¿à¤• à¤à¥à¤•à¤¾à¤µ"),
    YOGA_EFFECT_MALAVYA("Wealthy, enjoys all comforts, beautiful spouse, strong limbs, attractive face, blessed with vehicles and servants, learned in scriptures, lives up to 77 years", "à¤§à¤¨à¥€, à¤¸à¤¬à¥ˆ à¤¸à¥à¤µà¤¿à¤§à¤¾ à¤­à¥‹à¤—à¥à¤¨à¥‡, à¤¸à¥à¤¨à¥à¤¦à¤° à¤ªà¤¤à¤¿/à¤ªà¤¤à¥à¤¨à¥€, à¤¬à¤²à¤¿à¤¯à¥‹ à¤…à¤‚à¤—à¤¹à¤°à¥‚, à¤†à¤•à¤°à¥à¤·à¤• à¤®à¥à¤–, à¤µà¤¾à¤¹à¤¨ à¤° à¤¸à¥‡à¤µà¤•à¤¹à¤°à¥‚à¤²à¥‡ à¤†à¤¶à¥€à¤°à¥à¤µà¤¾à¤¦à¤¿à¤¤, à¤¶à¤¾à¤¸à¥à¤¤à¥à¤°à¤®à¤¾ à¤ªà¤¢à¥‡à¤•à¥‹, à¥­à¥­ à¤µà¤°à¥à¤·à¤¸à¤®à¥à¤® à¤¬à¤¾à¤à¤šà¥à¤¨à¥‡"),
    YOGA_EFFECT_SASA("Head of village/town/city, wicked disposition but good servants, intriguing nature, knows others' weaknesses, commands over masses, wealth through iron or labor", "à¤—à¤¾à¤‰à¤/à¤¶à¤¹à¤°/à¤¨à¤—à¤°à¤•à¥‹ à¤ªà¥à¤°à¤®à¥à¤–, à¤¦à¥à¤·à¥à¤Ÿ à¤¸à¥à¤µà¤­à¤¾à¤µ à¤¤à¤° à¤°à¤¾à¤®à¥à¤°à¤¾ à¤¸à¥‡à¤µà¤•à¤¹à¤°à¥‚, à¤·à¤¡à¥à¤¯à¤¨à¥à¤¤à¥à¤°à¤•à¤¾à¤°à¥€ à¤¸à¥à¤µà¤­à¤¾à¤µ, à¤…à¤°à¥‚à¤•à¥‹ à¤•à¤®à¤œà¥‹à¤°à¥€ à¤œà¤¾à¤¨à¥à¤¨à¥‡, à¤œà¤¨à¤¤à¤¾à¤®à¤¾à¤¥à¤¿ à¤†à¤¦à¥‡à¤¶, à¤«à¤²à¤¾à¤® à¤µà¤¾ à¤¶à¥à¤°à¤®à¤¬à¤¾à¤Ÿ à¤§à¤¨"),
    YOGA_EFFECT_GAJA_KESARI("Destroyer of enemies like lion, eloquent speaker, virtuous, long-lived, famous", "à¤¸à¤¿à¤‚à¤¹ à¤œà¤¸à¥à¤¤à¥‹ à¤¶à¤¤à¥à¤°à¥ à¤µà¤¿à¤¨à¤¾à¤¶à¤•, à¤µà¤¾à¤•à¥à¤ªà¤Ÿà¥ à¤µà¤•à¥à¤¤à¤¾, à¤¸à¤¦à¥à¤—à¥à¤£à¥€, à¤¦à¥€à¤°à¥à¤˜à¤¾à¤¯à¥, à¤ªà¥à¤°à¤¸à¤¿à¤¦à¥à¤§"),
    YOGA_EFFECT_SUNAFA("Self-made wealth, intelligent, good status, praised by kings", "à¤¸à¥à¤µ-à¤¨à¤¿à¤°à¥à¤®à¤¿à¤¤ à¤§à¤¨, à¤¬à¥à¤¦à¥à¤§à¤¿à¤®à¤¾à¤¨, à¤°à¤¾à¤®à¥à¤°à¥‹ à¤¸à¥à¤¥à¤¿à¤¤à¤¿, à¤°à¤¾à¤œà¤¾à¤¹à¤°à¥‚à¤¦à¥à¤µà¤¾à¤°à¤¾ à¤ªà¥à¤°à¤¶à¤‚à¤¸à¤¿à¤¤"),
    YOGA_EFFECT_ANAFA("Good reputation, health, happiness, self-respect", "à¤°à¤¾à¤®à¥à¤°à¥‹ à¤ªà¥à¤°à¤¤à¤¿à¤·à¥à¤ à¤¾, à¤¸à¥à¤µà¤¾à¤¸à¥à¤¥à¥à¤¯, à¤–à¥à¤¶à¥€, à¤†à¤¤à¥à¤®-à¤¸à¤®à¥à¤®à¤¾à¤¨"),
    YOGA_EFFECT_DURUDHARA("Highly fortunate, wealthy, vehicles, servants, charitable, enjoys life", "à¤…à¤¤à¥à¤¯à¤§à¤¿à¤• à¤­à¤¾à¤—à¥à¤¯à¤¶à¤¾à¤²à¥€, à¤§à¤¨à¥€, à¤µà¤¾à¤¹à¤¨à¤¹à¤°à¥‚, à¤¸à¥‡à¤µà¤•à¤¹à¤°à¥‚, à¤¦à¤¾à¤¨à¤¶à¥€à¤², à¤œà¥€à¤µà¤¨à¤•à¥‹ à¤†à¤¨à¤¨à¥à¤¦ à¤²à¤¿à¤¨à¥‡"),
    YOGA_EFFECT_ADHI("Commander, minister, or king; polite, trustworthy, healthy, wealthy, defeats enemies", "à¤¸à¥‡à¤¨à¤¾à¤ªà¤¤à¤¿, à¤®à¤¨à¥à¤¤à¥à¤°à¥€, à¤µà¤¾ à¤°à¤¾à¤œà¤¾; à¤µà¤¿à¤¨à¤®à¥à¤°, à¤µà¤¿à¤¶à¥à¤µà¤¸à¤¨à¥€à¤¯, à¤¸à¥à¤µà¤¸à¥à¤¥, à¤§à¤¨à¥€, à¤¶à¤¤à¥à¤°à¥à¤¹à¤°à¥‚à¤²à¤¾à¤ˆ à¤¹à¤°à¤¾à¤‰à¤¨à¥‡"),
    YOGA_EFFECT_BUDHA_ADITYA("Intelligence, skilled in many arts, famous, sweet speech, scholarly", "à¤¬à¥à¤¦à¥à¤§à¤¿, à¤§à¥‡à¤°à¥ˆ à¤•à¤²à¤¾à¤¹à¤°à¥‚à¤®à¤¾ à¤¦à¤•à¥à¤·, à¤ªà¥à¤°à¤¸à¤¿à¤¦à¥à¤§, à¤®à¥€à¤ à¥‹ à¤¬à¥‹à¤²à¥€, à¤µà¤¿à¤¦à¥à¤µà¤¾à¤¨"),
    YOGA_EFFECT_SARASWATI("Highly learned, poet, prose writer, famous speaker, skilled in all arts", "à¤…à¤¤à¥à¤¯à¤§à¤¿à¤• à¤ªà¤¢à¥‡à¤²à¥‡à¤–à¥‡à¤•à¥‹, à¤•à¤µà¤¿, à¤—à¤¦à¥à¤¯ à¤²à¥‡à¤–à¤•, à¤ªà¥à¤°à¤¸à¤¿à¤¦à¥à¤§ à¤µà¤•à¥à¤¤à¤¾, à¤¸à¤¬à¥ˆ à¤•à¤²à¤¾à¤¹à¤°à¥‚à¤®à¤¾ à¤¦à¤•à¥à¤·"),
    YOGA_EFFECT_PARVATA("King or minister, famous, generous, wealthy, charitable, mountain-like stability", "à¤°à¤¾à¤œà¤¾ à¤µà¤¾ à¤®à¤¨à¥à¤¤à¥à¤°à¥€, à¤ªà¥à¤°à¤¸à¤¿à¤¦à¥à¤§, à¤‰à¤¦à¤¾à¤°, à¤§à¤¨à¥€, à¤¦à¤¾à¤¨à¤¶à¥€à¤², à¤ªà¤¹à¤¾à¤¡ à¤œà¤¸à¥à¤¤à¥‹ à¤¸à¥à¤¥à¤¿à¤°à¤¤à¤¾"),
    YOGA_EFFECT_LAKSHMI("Blessed by Goddess Lakshmi, abundant wealth, luxury, beauty, artistic success", "à¤¦à¥‡à¤µà¥€ à¤²à¤•à¥à¤·à¥à¤®à¥€à¤•à¥‹ à¤†à¤¶à¥€à¤°à¥à¤µà¤¾à¤¦, à¤ªà¥à¤°à¤šà¥à¤° à¤§à¤¨, à¤µà¤¿à¤²à¤¾à¤¸à¤¿à¤¤à¤¾, à¤¸à¥Œà¤¨à¥à¤¦à¤°à¥à¤¯, à¤•à¤²à¤¾à¤¤à¥à¤®à¤• à¤¸à¤«à¤²à¤¤à¤¾"),
    YOGA_EFFECT_MAHA_RAJA("Exceptional fortune, royal status, widespread fame, great wealth and power", "à¤…à¤¸à¤¾à¤§à¤¾à¤°à¤£ à¤­à¤¾à¤—à¥à¤¯, à¤°à¤¾à¤œà¤•à¥€à¤¯ à¤¸à¥à¤¥à¤¿à¤¤à¤¿, à¤µà¥à¤¯à¤¾à¤ªà¤• à¤ªà¥à¤°à¤¸à¤¿à¤¦à¥à¤§à¤¿, à¤ à¥‚à¤²à¥‹ à¤§à¤¨ à¤° à¤¶à¤•à¥à¤¤à¤¿"),
    YOGA_EFFECT_KENDRA_TRIKONA("Rise to power and authority, leadership position, recognition from government", "à¤¶à¤•à¥à¤¤à¤¿ à¤° à¤…à¤§à¤¿à¤•à¤¾à¤°à¤®à¤¾ à¤‰à¤¦à¤¯, à¤¨à¥‡à¤¤à¥ƒà¤¤à¥à¤µ à¤ªà¤¦, à¤¸à¤°à¤•à¤¾à¤°à¤¬à¤¾à¤Ÿ à¤®à¤¾à¤¨à¥à¤¯à¤¤à¤¾"),
    YOGA_EFFECT_PARIVARTANA("Strong Raja Yoga through mutual exchange, stable rise to power, lasting authority", "à¤†à¤ªà¤¸à¥€ à¤†à¤¦à¤¾à¤¨à¤ªà¥à¤°à¤¦à¤¾à¤¨à¤¬à¤¾à¤Ÿ à¤¬à¤²à¤¿à¤¯à¥‹ à¤°à¤¾à¤œ à¤¯à¥‹à¤—, à¤¶à¤•à¥à¤¤à¤¿à¤®à¤¾ à¤¸à¥à¤¥à¤¿à¤° à¤‰à¤¦à¤¯, à¤¦à¤¿à¤—à¥‹ à¤…à¤§à¤¿à¤•à¤¾à¤°"),
    YOGA_EFFECT_VIPARITA("Rise through fall of enemies, sudden fortune from unexpected sources, gains through others' losses", "à¤¶à¤¤à¥à¤°à¥à¤¹à¤°à¥‚à¤•à¥‹ à¤ªà¤¤à¤¨à¤¬à¤¾à¤Ÿ à¤‰à¤¦à¤¯, à¤…à¤ªà¥à¤°à¤¤à¥à¤¯à¤¾à¤¶à¤¿à¤¤ à¤¸à¥à¤°à¥‹à¤¤à¤¹à¤°à¥‚à¤¬à¤¾à¤Ÿ à¤…à¤šà¤¾à¤¨à¤• à¤­à¤¾à¤—à¥à¤¯, à¤…à¤°à¥‚à¤•à¥‹ à¤¹à¤¾à¤¨à¤¿à¤¬à¤¾à¤Ÿ à¤²à¤¾à¤­"),
    YOGA_EFFECT_NEECHA_BHANGA("Rise from humble beginnings, success after initial struggles, respected leader", "à¤¸à¤¾à¤§à¤¾à¤°à¤£ à¤¸à¥à¤°à¥à¤µà¤¾à¤¤à¤¬à¤¾à¤Ÿ à¤‰à¤¦à¤¯, à¤ªà¥à¤°à¤¾à¤°à¤®à¥à¤­à¤¿à¤• à¤¸à¤‚à¤˜à¤°à¥à¤·à¤ªà¤›à¤¿ à¤¸à¤«à¤²à¤¤à¤¾, à¤¸à¤®à¥à¤®à¤¾à¤¨à¤¿à¤¤ à¤¨à¥‡à¤¤à¤¾"),
    YOGA_EFFECT_KEMADRUMA("Poverty, suffering, struggles, lack of support, lonely, menial work", "à¤—à¤°à¤¿à¤¬à¥€, à¤¦à¥à¤–, à¤¸à¤‚à¤˜à¤°à¥à¤·, à¤¸à¤®à¤°à¥à¤¥à¤¨à¤•à¥‹ à¤…à¤­à¤¾à¤µ, à¤à¤•à¥à¤²à¥‹, à¤¤à¤²à¥à¤²à¥‹ à¤•à¤¾à¤®"),
    YOGA_EFFECT_KEMADRUMA_CANCELLED("Kemadruma effects significantly reduced due to cancellation factors", "à¤°à¤¦à¥à¤¦ à¤•à¤¾à¤°à¤•à¤¹à¤°à¥‚à¤•à¥‹ à¤•à¤¾à¤°à¤£ à¤•à¥‡à¤®à¤¦à¥à¤°à¥à¤® à¤ªà¥à¤°à¤­à¤¾à¤µ à¤‰à¤²à¥à¤²à¥‡à¤–à¤¨à¥€à¤¯ à¤°à¥‚à¤ªà¤®à¤¾ à¤•à¤® à¤­à¤¯à¥‹"),
    YOGA_EFFECT_DARIDRA("Obstacles to gains, financial struggles, unfulfilled desires", "à¤²à¤¾à¤­à¤®à¤¾ à¤¬à¤¾à¤§à¤¾à¤¹à¤°à¥‚, à¤†à¤°à¥à¤¥à¤¿à¤• à¤¸à¤‚à¤˜à¤°à¥à¤·, à¤…à¤ªà¥‚à¤°à¥à¤£ à¤‡à¤šà¥à¤›à¤¾à¤¹à¤°à¥‚"),
    YOGA_EFFECT_SHAKATA("Fluctuating fortune, periods of poverty alternating with wealth", "à¤‰à¤¤à¤¾à¤°-à¤šà¤¢à¤¾à¤µ à¤­à¤¾à¤—à¥à¤¯, à¤—à¤°à¤¿à¤¬à¥€à¤•à¥‹ à¤…à¤µà¤§à¤¿ à¤§à¤¨à¤¸à¤à¤— à¤ªà¤¾à¤²à¥ˆà¤ªà¤¾à¤²à¥‹"),
    YOGA_EFFECT_GURU_CHANDAL("Unorthodox beliefs, breaks from tradition, possible disgrace through teachers/religion", "à¤…à¤ªà¤°à¤®à¥à¤ªà¤°à¤¾à¤—à¤¤ à¤µà¤¿à¤¶à¥à¤µà¤¾à¤¸à¤¹à¤°à¥‚, à¤ªà¤°à¤®à¥à¤ªà¤°à¤¾ à¤¤à¥‹à¤¡à¥à¤¨à¥‡, à¤—à¥à¤°à¥/à¤§à¤°à¥à¤®à¤¬à¤¾à¤Ÿ à¤¸à¤®à¥à¤­à¤¾à¤µà¤¿à¤¤ à¤…à¤ªà¤®à¤¾à¤¨"),
    YOGA_EFFECT_VESI("Wealth through hard work, truthful, balanced life, comfortable old age", "à¤®à¥‡à¤¹à¤¨à¤¤à¤¬à¤¾à¤Ÿ à¤§à¤¨, à¤¸à¤¤à¥à¤¯à¤µà¤¾à¤¦à¥€, à¤¸à¤¨à¥à¤¤à¥à¤²à¤¿à¤¤ à¤œà¥€à¤µà¤¨, à¤†à¤°à¤¾à¤®à¤¦à¤¾à¤¯à¤• à¤¬à¥à¤¢à¥‡à¤¸à¤•à¤¾à¤²"),
    YOGA_EFFECT_VOSI("Famous, generous, skilled in service, gains through associations", "à¤ªà¥à¤°à¤¸à¤¿à¤¦à¥à¤§, à¤‰à¤¦à¤¾à¤°, à¤¸à¥‡à¤µà¤¾à¤®à¤¾ à¤¦à¤•à¥à¤·, à¤¸à¤‚à¤—à¤¤à¤¬à¤¾à¤Ÿ à¤²à¤¾à¤­"),
    YOGA_EFFECT_UBHAYACHARI("Eloquent speaker, wealthy, influential, respected by rulers", "à¤µà¤¾à¤•à¥à¤ªà¤Ÿà¥ à¤µà¤•à¥à¤¤à¤¾, à¤§à¤¨à¥€, à¤ªà¥à¤°à¤­à¤¾à¤µà¤¶à¤¾à¤²à¥€, à¤¶à¤¾à¤¸à¤•à¤¹à¤°à¥‚à¤¦à¥à¤µà¤¾à¤°à¤¾ à¤¸à¤®à¥à¤®à¤¾à¤¨à¤¿à¤¤"),
    YOGA_EFFECT_LABHA("Gains from multiple sources, profitable ventures, wealth accumulation", "à¤µà¤¿à¤­à¤¿à¤¨à¥à¤¨ à¤¸à¥à¤°à¥‹à¤¤à¤¬à¤¾à¤Ÿ à¤²à¤¾à¤­, à¤²à¤¾à¤­à¤¦à¤¾à¤¯à¤• à¤‰à¤¦à¥à¤¯à¤®à¤¹à¤°à¥‚, à¤§à¤¨ à¤¸à¤‚à¤šà¤¯"),
    YOGA_EFFECT_KUBERA("Immense wealth like lord of wealth, treasure finder, banking success", "à¤§à¤¨à¤•à¥‹ à¤¦à¥‡à¤µà¤¤à¤¾ à¤œà¤¸à¥à¤¤à¥‹ à¤…à¤ªà¤¾à¤° à¤§à¤¨, à¤–à¤œà¤¾à¤¨à¤¾ à¤«à¥‡à¤²à¤¾ à¤ªà¤¾à¤°à¥à¤¨à¥‡, à¤¬à¥ˆà¤‚à¤•à¤¿à¤™à¤®à¤¾ à¤¸à¤«à¤²à¤¤à¤¾"),
    YOGA_EFFECT_CHANDRA_MANGALA("Wealth through business, enterprise, real estate, aggressive financial pursuits", "à¤µà¥à¤¯à¤¾à¤ªà¤¾à¤°à¤¬à¤¾à¤Ÿ à¤§à¤¨, à¤‰à¤¦à¥à¤¯à¤®, à¤˜à¤° à¤œà¤—à¥à¤—à¤¾, à¤†à¤•à¥à¤°à¤¾à¤®à¤• à¤†à¤°à¥à¤¥à¤¿à¤• à¤ªà¥à¤°à¤¯à¤¾à¤¸à¤¹à¤°à¥‚"),
    YOGA_EFFECT_DASA_MULA("Obstacles in undertakings, needs remedial measures, struggle with finances", "à¤•à¤¾à¤°à¥à¤¯à¤¹à¤°à¥‚à¤®à¤¾ à¤¬à¤¾à¤§à¤¾à¤¹à¤°à¥‚, à¤‰à¤ªà¤šà¤¾à¤°à¤¾à¤¤à¥à¤®à¤• à¤‰à¤ªà¤¾à¤¯à¤¹à¤°à¥‚ à¤†à¤µà¤¶à¥à¤¯à¤•, à¤†à¤°à¥à¤¥à¤¿à¤• à¤¸à¤‚à¤˜à¤°à¥à¤·"),
    YOGA_EFFECT_KAHALA("Brave but stubborn, military success, leadership through conflict", "à¤¬à¤¹à¤¾à¤¦à¥à¤° à¤¤à¤° à¤¹à¤ à¥€, à¤¸à¥ˆà¤¨à¥à¤¯ à¤¸à¤«à¤²à¤¤à¤¾, à¤¦à¥à¤µà¤¨à¥à¤¦à¥à¤µà¤¬à¤¾à¤Ÿ à¤¨à¥‡à¤¤à¥ƒà¤¤à¥à¤µ"),

    // Yoga Descriptions
    YOGA_DESC_KENDRA_LORD("Kendra lord", "à¤•à¥‡à¤¨à¥à¤¦à¥à¤° à¤…à¤§à¤¿à¤ªà¤¤à¤¿"),
    YOGA_DESC_TRIKONA_LORD("Trikona lord", "à¤¤à¥à¤°à¤¿à¤•à¥‹à¤£ à¤…à¤§à¤¿à¤ªà¤¤à¤¿"),
    YOGA_DESC_IN_CONJUNCTION("in conjunction", "à¤¸à¤‚à¤¯à¥‹à¤— à¤®à¤¾"),
    YOGA_DESC_IN_ASPECT("in aspect", "à¤¦à¥ƒà¤·à¥à¤Ÿà¤¿ à¤®à¤¾"),
    YOGA_DESC_OWN_SIGN("own sign", "à¤†à¤«à¥à¤¨à¥‹ à¤°à¤¾à¤¶à¤¿"),
    YOGA_DESC_EXALTED("exalted sign", "à¤‰à¤šà¥à¤š à¤°à¤¾à¤¶à¤¿"),
    YOGA_DESC_IN_KENDRA("in Kendra", "à¤•à¥‡à¤¨à¥à¤¦à¥à¤°à¤®à¤¾"),
    YOGA_DESC_DUSTHANA("Dusthana", "à¤¦à¥à¤¸à¥à¤¥à¤¾à¤¨"),
    YOGA_DESC_DEBILITATED("debilitated", "à¤¨à¥€à¤š"),
    YOGA_DESC_COMBUST("combust", "à¤…à¤¸à¥à¤¤"),

    // Nabhasa Yoga Effects
    YOGA_EFFECT_YAVA("Medium wealth initially, prosperity in middle age, decline in old age", "à¤¸à¥à¤°à¥à¤®à¤¾ à¤®à¤§à¥à¤¯à¤® à¤§à¤¨, à¤®à¤§à¥à¤¯ à¤‰à¤®à¥‡à¤°à¤®à¤¾ à¤¸à¤®à¥ƒà¤¦à¥à¤§à¤¿, à¤¬à¥à¤¢à¥‡à¤¸à¤•à¤¾à¤²à¤®à¤¾ à¤ªà¤¤à¤¨"),
    YOGA_EFFECT_SHRINGATAKA("Fond of quarrels initially, happiness in middle age, wandering in old age", "à¤¸à¥à¤°à¥à¤®à¤¾ à¤à¤—à¤¡à¤¾ à¤®à¤¨à¤ªà¤°à¥à¤¨à¥‡, à¤®à¤§à¥à¤¯ à¤‰à¤®à¥‡à¤°à¤®à¤¾ à¤–à¥à¤¶à¥€, à¤¬à¥à¤¢à¥‡à¤¸à¤•à¤¾à¤²à¤®à¤¾ à¤­à¥Œà¤‚à¤¤à¤¾à¤°à¤¿à¤¨à¥‡"),
    YOGA_EFFECT_GADA("Wealthy through ceremonies, always engaged in auspicious activities", "à¤¸à¤‚à¤¸à¥à¤•à¤¾à¤°à¤¹à¤°à¥‚à¤¬à¤¾à¤Ÿ à¤§à¤¨à¥€, à¤¸à¤§à¥ˆà¤‚ à¤¶à¥à¤­ à¤•à¤¾à¤°à¥à¤¯à¤¹à¤°à¥‚à¤®à¤¾ à¤¸à¤‚à¤²à¤—à¥à¤¨"),
    YOGA_EFFECT_SHAKATA_NABHASA("Fluctuating fortune, poverty followed by wealth in cycles", "à¤‰à¤¤à¤¾à¤°-à¤šà¤¢à¤¾à¤µ à¤­à¤¾à¤—à¥à¤¯, à¤—à¤°à¤¿à¤¬à¥€à¤ªà¤›à¤¿ à¤§à¤¨ à¤šà¤•à¥à¤°à¤®à¤¾"),
    YOGA_EFFECT_RAJJU("Fond of travel, living in foreign lands, restless nature", "à¤¯à¤¾à¤¤à¥à¤°à¤¾à¤•à¥‹ à¤¶à¥Œà¤•à¥€à¤¨, à¤µà¤¿à¤¦à¥‡à¤¶à¥€ à¤­à¥‚à¤®à¤¿à¤®à¤¾ à¤¬à¤¸à¥à¤¨à¥‡, à¤…à¤¶à¤¾à¤¨à¥à¤¤ à¤¸à¥à¤µà¤­à¤¾à¤µ"),
    YOGA_EFFECT_MUSALA("Proud, wealthy, learned, famous, many children", "à¤—à¤°à¥à¤µà¤¿à¤²à¥‹, à¤§à¤¨à¥€, à¤ªà¤¢à¥‡à¤²à¥‡à¤–à¥‡à¤•à¥‹, à¤ªà¥à¤°à¤¸à¤¿à¤¦à¥à¤§, à¤§à¥‡à¤°à¥ˆ à¤¸à¤¨à¥à¤¤à¤¾à¤¨"),
    YOGA_EFFECT_NALA("Handsome, skilled in arts, wealthy through multiple sources", "à¤¸à¥à¤¨à¥à¤¦à¤°, à¤•à¤²à¤¾à¤®à¤¾ à¤¦à¤•à¥à¤·, à¤µà¤¿à¤­à¤¿à¤¨à¥à¤¨ à¤¸à¥à¤°à¥‹à¤¤à¤¬à¤¾à¤Ÿ à¤§à¤¨à¥€"),
    YOGA_EFFECT_KEDARA("Agricultural wealth, helpful to others, truthful", "à¤•à¥ƒà¤·à¤¿ à¤§à¤¨, à¤…à¤°à¥‚à¤²à¤¾à¤ˆ à¤®à¤¦à¥à¤¦à¤¤à¤—à¤°à¥à¤¨à¥‡, à¤¸à¤¤à¥à¤¯à¤µà¤¾à¤¦à¥€"),
    YOGA_EFFECT_SHOOLA("Sharp intellect, quarrelsome, cruel, poor", "à¤¤à¥€à¤•à¥à¤·à¥à¤£ à¤¬à¥à¤¦à¥à¤§à¤¿, à¤à¤—à¤¡à¤¾à¤²à¥, à¤¨à¤¿à¤°à¥à¤¦à¤¯à¥€, à¤—à¤°à¤¿à¤¬"),
    YOGA_EFFECT_YUGA("Heretic, poor, rejected by family", "à¤µà¤¿à¤§à¤°à¥à¤®à¥€, à¤—à¤°à¤¿à¤¬, à¤ªà¤°à¤¿à¤µà¤¾à¤°à¤¦à¥à¤µà¤¾à¤°à¤¾ à¤¤à¥à¤¯à¤¾à¤—à¤¿à¤à¤•à¥‹"),
    YOGA_EFFECT_GOLA("Poor, dirty, ignorant, idle", "à¤—à¤°à¤¿à¤¬, à¤«à¥‹à¤¹à¥‹à¤°à¥€, à¤…à¤œà¥à¤žà¤¾à¤¨à¥€, à¤…à¤²à¥à¤›à¥€"),
    YOGA_EFFECT_VEENA("Fond of music, dance, leader, wealthy, happy", "à¤¸à¤‚à¤—à¥€à¤¤ à¤° à¤¨à¥ƒà¤¤à¥à¤¯à¤•à¥‹ à¤¶à¥Œà¤•à¥€à¤¨, à¤¨à¥‡à¤¤à¤¾, à¤§à¤¨à¥€, à¤–à¥à¤¶à¥€"),

    // ============================================
    // CHOGHADIYA NAMES
    // ============================================
    CHOGHADIYA_AMRIT("Amrit", "à¤…à¤®à¥ƒà¤¤"),
    CHOGHADIYA_SHUBH("Shubh", "à¤¶à¥à¤­"),
    CHOGHADIYA_LABH("Labh", "à¤²à¤¾à¤­"),
    CHOGHADIYA_CHAR("Char", "à¤šà¤°"),
    CHOGHADIYA_ROG("Rog", "à¤°à¥‹à¤—"),
    CHOGHADIYA_KAAL("Kaal", "à¤•à¤¾à¤²"),
    CHOGHADIYA_UDVEG("Udveg", "à¤‰à¤¦à¥à¤µà¥‡à¤—"),

    // ============================================
    // ADDITIONAL UI STRINGS
    // ============================================
    UI_CONJUNCTION("conjunction", "à¤¯à¥à¤¤à¤¿"),
    UI_ASPECT("aspect", "à¤¦à¥ƒà¤·à¥à¤Ÿà¤¿"),
    UI_EXCHANGE("exchange", "à¤ªà¤°à¤¿à¤µà¤°à¥à¤¤à¤¨"),
    UI_THROUGHOUT_LIFE("Throughout life", "à¤œà¥€à¤µà¤¨à¤­à¤°"),
    UI_NONE("None", "à¤•à¥à¤¨à¥ˆ à¤ªà¤¨à¤¿ à¤›à¥ˆà¤¨"),
    UI_PRESENT("Present", "à¤‰à¤ªà¤¸à¥à¤¥à¤¿à¤¤"),
    UI_ASCENDING("Ascending", "à¤†à¤°à¥‹à¤¹à¥€"),
    UI_DESCENDING("Descending", "à¤…à¤µà¤°à¥‹à¤¹à¥€"),
    UI_NAKSHATRAS("nakshatras", "à¤¨à¤•à¥à¤·à¤¤à¥à¤°à¤¹à¤°à¥‚"),

    // ============================================
    // SCREEN TAB NAMES
    // ============================================
    TAB_OVERVIEW("Overview", "à¤¸à¤¿à¤‚à¤¹à¤¾à¤µà¤²à¥‹à¤•à¤¨"),
    TAB_REMEDIES("Remedies", "à¤‰à¤ªà¤¾à¤¯à¤¹à¤°à¥‚"),
    TAB_PLANETS("Planets", "à¤—à¥à¤°à¤¹à¤¹à¤°à¥‚"),
    TAB_TODAY("Today", "à¤†à¤œ"),
    TAB_FIND_MUHURTA("Find Muhurta", "à¤®à¥à¤¹à¥‚à¤°à¥à¤¤ à¤–à¥‹à¤œà¥à¤¨à¥à¤¹à¥‹à¤¸à¥"),
    TAB_TAJIKA("Tajika", "à¤¤à¤¾à¤œà¤¿à¤•"),
    TAB_SAHAMS("Sahams", "à¤¸à¤¹à¤®à¤¹à¤°à¥‚"),
    TAB_DASHA("Dasha", "à¤¦à¤¶à¤¾"),
    TAB_HOUSES("Houses", "à¤­à¤¾à¤µà¤¹à¤°à¥‚"),
    TAB_ANALYSIS("Analysis", "à¤µà¤¿à¤¶à¥à¤²à¥‡à¤·à¤£"),
    TAB_DETAILS("Details", "à¤µà¤¿à¤µà¤°à¤£à¤¹à¤°à¥‚"),
    TAB_TRANSITS("Transits", "à¤—à¥‹à¤šà¤°"),
    TAB_ASPECTS("Aspects", "à¤¦à¥ƒà¤·à¥à¤Ÿà¤¿"),
    TAB_STRENGTH("Strength", "à¤¬à¤²"),
    TAB_DIGNITIES("Dignities", "à¤®à¤°à¥à¤¯à¤¾à¤¦à¤¾à¤¹à¤°à¥‚"),

    // ============================================
    // REMEDIES SCREEN SPECIFIC
    // ============================================
    REMEDY_TITLE("Remedies", "à¤‰à¤ªà¤¾à¤¯à¤¹à¤°à¥‚"),
    REMEDY_SEARCH("Search remedies", "à¤‰à¤ªà¤¾à¤¯à¤¹à¤°à¥‚ à¤–à¥‹à¤œà¥à¤¨à¥à¤¹à¥‹à¤¸à¥"),
    REMEDY_CALCULATION_FAILED("Failed to calculate remedies: %s", "à¤‰à¤ªà¤¾à¤¯à¤¹à¤°à¥‚ à¤—à¤£à¤¨à¤¾ à¤—à¤°à¥à¤¨ à¤…à¤¸à¤«à¤²: %s"),
    REMEDY_COPY("Copy", "à¤•à¤ªà¥€ à¤—à¤°à¥à¤¨à¥à¤¹à¥‹à¤¸à¥"),
    REMEDY_SHARE("Share", "à¤¸à¤¾à¤à¤¾ à¤—à¤°à¥à¤¨à¥à¤¹à¥‹à¤¸à¥"),
    REMEDY_PLANETARY_ANALYSIS("Planetary Analysis", "à¤—à¥à¤°à¤¹ à¤µà¤¿à¤¶à¥à¤²à¥‡à¤·à¤£"),
    REMEDY_ESSENTIAL_COUNT("%d Essential Remedies", "%d à¤†à¤µà¤¶à¥à¤¯à¤• à¤‰à¤ªà¤¾à¤¯à¤¹à¤°à¥‚"),
    REMEDY_ALL_STRONG("All planets are in good condition", "à¤¸à¤¬à¥ˆ à¤—à¥à¤°à¤¹ à¤°à¤¾à¤®à¥à¤°à¥‹ à¤…à¤µà¤¸à¥à¤¥à¤¾à¤®à¤¾ à¤›à¤¨à¥"),

    // ============================================
    // MUHURTA SCREEN SPECIFIC
    // ============================================
    MUHURTA_CHOGHADIYA("Choghadiya", "à¤šà¥Œà¤˜à¤¡à¤¿à¤¯à¤¾"),
    MUHURTA_RAHU_KAAL("Rahu Kaal", "à¤°à¤¾à¤¹à¥à¤•à¤¾à¤²"),
    MUHURTA_YAMA_GHANTAKA("Yama Ghantaka", "à¤¯à¤® à¤˜à¤£à¥à¤Ÿà¤•"),
    MUHURTA_GULIKA_KAAL("Gulika Kaal", "à¤—à¥à¤²à¤¿à¤•à¤¾ à¤•à¤¾à¤²"),
    MUHURTA_ABHIJIT("Abhijit Muhurta", "à¤…à¤­à¤¿à¤œà¤¿à¤¤ à¤®à¥à¤¹à¥‚à¤°à¥à¤¤"),
    MUHURTA_BRAHMA("Brahma Muhurta", "à¤¬à¥à¤°à¤¹à¥à¤® à¤®à¥à¤¹à¥‚à¤°à¥à¤¤"),
    MUHURTA_SELECT_DATE("Select Date", "à¤®à¤¿à¤¤à¤¿ à¤šà¤¯à¤¨ à¤—à¤°à¥à¤¨à¥à¤¹à¥‹à¤¸à¥"),
    MUHURTA_SEARCH_RESULTS("Search Results", "à¤–à¥‹à¤œ à¤ªà¤°à¤¿à¤£à¤¾à¤®à¤¹à¤°à¥‚"),

    // ============================================
    // VARSHAPHALA SCREEN SPECIFIC
    // ============================================
    VARSHAPHALA_TITLE("Varshaphala", "à¤µà¤°à¥à¤·à¤«à¤²"),
    VARSHAPHALA_ANNUAL_CHART("Annual Chart", "à¤µà¤¾à¤°à¥à¤·à¤¿à¤• à¤šà¤¾à¤°à¥à¤Ÿ"),
    VARSHAPHALA_SAHAMS("Sahams", "à¤¸à¤¹à¤®à¤¹à¤°à¥‚"),
    VARSHAPHALA_TAJIKA("Tajika Aspects", "à¤¤à¤¾à¤œà¤¿à¤• à¤¦à¥ƒà¤·à¥à¤Ÿà¤¿"),
    VARSHAPHALA_YOGAS("Tajika Yogas", "à¤¤à¤¾à¤œà¤¿à¤• à¤¯à¥‹à¤—"),
    VARSHAPHALA_PREDICTIONS("Year Predictions", "à¤µà¤°à¥à¤· à¤­à¤µà¤¿à¤·à¥à¤¯à¤µà¤¾à¤£à¥€"),
    VARSHAPHALA_SELECT_YEAR("Select Year", "à¤µà¤°à¥à¤· à¤šà¤¯à¤¨ à¤—à¤°à¥à¤¨à¥à¤¹à¥‹à¤¸à¥"),
    VARSHAPHALA_YEAR_OF_LIFE("Year %d of life", "à¤œà¥€à¤µà¤¨à¤•à¥‹ à¤µà¤°à¥à¤· %d"),

    // ============================================
    // PANCHANGA DETAILS
    // ============================================
    PANCHANGA_TITHI_SHUKLA("Shukla", "à¤¶à¥à¤•à¥à¤²"),
    PANCHANGA_TITHI_KRISHNA("Krishna", "à¤•à¥ƒà¤·à¥à¤£"),
    PANCHANGA_PAKSHA("Paksha", "à¤ªà¤•à¥à¤·"),
    PANCHANGA_MASA("Masa", "à¤®à¤¾à¤¸"),
    PANCHANGA_LUNAR_PHASE("Lunar Phase", "à¤šà¤¨à¥à¤¦à¥à¤° à¤•à¤²à¤¾"),
    PANCHANGA_NEW_MOON("New Moon", "à¤…à¤®à¤¾à¤µà¤¸à¥à¤¯à¤¾"),
    PANCHANGA_FULL_MOON("Full Moon", "à¤ªà¥‚à¤°à¥à¤£à¤¿à¤®à¤¾"),
    PANCHANGA_WAXING("Waxing", "à¤¬à¤¢à¥à¤¦à¥‹"),
    PANCHANGA_WANING("Waning", "à¤˜à¤Ÿà¥à¤¦à¥‹"),
    PANCHANGA_FAVORABLE("Favorable", "à¤…à¤¨à¥à¤•à¥‚à¤²"),
    PANCHANGA_UNFAVORABLE("Unfavorable", "à¤ªà¥à¤°à¤¤à¤¿à¤•à¥‚à¤²"),
    PANCHANGA_ACTIVITIES("Favorable Activities", "à¤…à¤¨à¥à¤•à¥‚à¤² à¤—à¤¤à¤¿à¤µà¤¿à¤§à¤¿à¤¹à¤°à¥‚"),

    // ============================================
    // TITHI NAMES
    // ============================================
    TITHI_PRATIPADA("Pratipada", "à¤ªà¥à¤°à¤¤à¤¿à¤ªà¤¦à¤¾"),
    TITHI_DWITIYA("Dwitiya", "à¤¦à¥à¤µà¤¿à¤¤à¥€à¤¯à¤¾"),
    TITHI_TRITIYA("Tritiya", "à¤¤à¥ƒà¤¤à¥€à¤¯à¤¾"),
    TITHI_CHATURTHI("Chaturthi", "à¤šà¤¤à¥à¤°à¥à¤¥à¥€"),
    TITHI_PANCHAMI("Panchami", "à¤ªà¤žà¥à¤šà¤®à¥€"),
    TITHI_SHASHTHI("Shashthi", "à¤·à¤·à¥à¤ à¥€"),
    TITHI_SAPTAMI("Saptami", "à¤¸à¤ªà¥à¤¤à¤®à¥€"),
    TITHI_ASHTAMI("Ashtami", "à¤…à¤·à¥à¤Ÿà¤®à¥€"),
    TITHI_NAVAMI("Navami", "à¤¨à¤µà¤®à¥€"),
    TITHI_DASHAMI("Dashami", "à¤¦à¤¶à¤®à¥€"),
    TITHI_EKADASHI("Ekadashi", "à¤à¤•à¤¾à¤¦à¤¶à¥€"),
    TITHI_DWADASHI("Dwadashi", "à¤¦à¥à¤µà¤¾à¤¦à¤¶à¥€"),
    TITHI_TRAYODASHI("Trayodashi", "à¤¤à¥à¤°à¤¯à¥‹à¤¦à¤¶à¥€"),
    TITHI_CHATURDASHI("Chaturdashi", "à¤šà¤¤à¥à¤°à¥à¤¦à¤¶à¥€"),
    TITHI_PURNIMA("Purnima", "à¤ªà¥‚à¤°à¥à¤£à¤¿à¤®à¤¾"),
    TITHI_AMAVASYA("Amavasya", "à¤…à¤®à¤¾à¤µà¤¸à¥à¤¯à¤¾"),

    // ============================================
    // KARANA NAMES
    // ============================================
    KARANA_BAVA("Bava", "à¤¬à¤µ"),
    KARANA_BALAVA("Balava", "à¤¬à¤¾à¤²à¤µ"),
    KARANA_KAULAVA("Kaulava", "à¤•à¥Œà¤²à¤µ"),
    KARANA_TAITILA("Taitila", "à¤¤à¥ˆà¤¤à¤¿à¤²"),
    KARANA_GARA("Gara", "à¤—à¤°"),
    KARANA_VANIJA("Vanija", "à¤µà¤£à¤¿à¤œ"),
    KARANA_VISHTI("Vishti (Bhadra)", "à¤µà¤¿à¤·à¥à¤Ÿà¤¿ (à¤­à¤¦à¥à¤°à¤¾)"),
    KARANA_SHAKUNI("Shakuni", "à¤¶à¤•à¥à¤¨à¤¿"),
    KARANA_CHATUSHPADA("Chatushpada", "à¤šà¤¤à¥à¤·à¥à¤ªà¤¦"),
    KARANA_NAGA("Naga", "à¤¨à¤¾à¤—"),
    KARANA_KIMSTUGHNA("Kimstughna", "à¤•à¤¿à¤‚à¤¸à¥à¤¤à¥à¤˜à¥à¤¨"),

    // ============================================
    // DAILY YOGA NAMES (PANCHANGA)
    // ============================================
    DAILY_YOGA_VISHKUMBHA("Vishkumbha", "à¤µà¤¿à¤·à¥à¤•à¤®à¥à¤­"),
    DAILY_YOGA_PRITI("Priti", "à¤ªà¥à¤°à¥€à¤¤à¤¿"),
    DAILY_YOGA_AYUSHMAN("Ayushman", "à¤†à¤¯à¥à¤·à¥à¤®à¤¾à¤¨"),
    DAILY_YOGA_SAUBHAGYA("Saubhagya", "à¤¸à¥Œà¤­à¤¾à¤—à¥à¤¯"),
    DAILY_YOGA_SHOBHANA("Shobhana", "à¤¶à¥‹à¤­à¤¨"),
    DAILY_YOGA_ATIGANDA("Atiganda", "à¤…à¤¤à¤¿à¤—à¤£à¥à¤¡"),
    DAILY_YOGA_SUKARMA("Sukarma", "à¤¸à¥à¤•à¤°à¥à¤®"),
    DAILY_YOGA_DHRITI("Dhriti", "à¤§à¥ƒà¤¤à¤¿"),
    DAILY_YOGA_SHOOLA("Shoola", "à¤¶à¥‚à¤²"),
    DAILY_YOGA_GANDA("Ganda", "à¤—à¤£à¥à¤¡"),
    DAILY_YOGA_VRIDDHI("Vriddhi", "à¤µà¥ƒà¤¦à¥à¤§à¤¿"),
    DAILY_YOGA_DHRUVA("Dhruva", "à¤§à¥à¤°à¥à¤µ"),
    DAILY_YOGA_VYAGHATA("Vyaghata", "à¤µà¥à¤¯à¤¾à¤˜à¤¾à¤¤"),
    DAILY_YOGA_HARSHANA("Harshana", "à¤¹à¤°à¥à¤·à¤£"),
    DAILY_YOGA_VAJRA("Vajra", "à¤µà¤œà¥à¤°"),
    DAILY_YOGA_SIDDHI("Siddhi", "à¤¸à¤¿à¤¦à¥à¤§à¤¿"),
    DAILY_YOGA_VYATIPATA("Vyatipata", "à¤µà¥à¤¯à¤¤à¥€à¤ªà¤¾à¤¤"),
    DAILY_YOGA_VARIYANA("Variyana", "à¤µà¤°à¥€à¤¯à¤¾à¤¨"),
    DAILY_YOGA_PARIGHA("Parigha", "à¤ªà¤°à¤¿à¤˜"),
    DAILY_YOGA_SHIVA("Shiva", "à¤¶à¤¿à¤µ"),
    DAILY_YOGA_SIDDHA("Siddha", "à¤¸à¤¿à¤¦à¥à¤§"),
    DAILY_YOGA_SADHYA("Sadhya", "à¤¸à¤¾à¤§à¥à¤¯"),
    DAILY_YOGA_SHUBHA("Shubha", "à¤¶à¥à¤­"),
    DAILY_YOGA_SHUKLA("Shukla", "à¤¶à¥à¤•à¥à¤²"),
    DAILY_YOGA_BRAHMA("Brahma", "à¤¬à¥à¤°à¤¹à¥à¤®"),
    DAILY_YOGA_INDRA("Indra", "à¤‡à¤¨à¥à¤¦à¥à¤°"),
    DAILY_YOGA_VAIDHRITI("Vaidhriti", "à¤µà¥ˆà¤§à¥ƒà¤¤à¤¿"),

    // ============================================
    // VARA (WEEKDAY) DESCRIPTIONS
    // ============================================
    VARA_SUNDAY_DESC("Ruled by Sun - Good for government work, authority, spiritual practices", "à¤¸à¥‚à¤°à¥à¤¯ à¤¦à¥à¤µà¤¾à¤°à¤¾ à¤¶à¤¾à¤¸à¤¿à¤¤ - à¤¸à¤°à¤•à¤¾à¤°à¥€ à¤•à¤¾à¤®, à¤…à¤§à¤¿à¤•à¤¾à¤°, à¤†à¤§à¥à¤¯à¤¾à¤¤à¥à¤®à¤¿à¤• à¤…à¤­à¥à¤¯à¤¾à¤¸à¤•à¥‹ à¤²à¤¾à¤—à¤¿ à¤°à¤¾à¤®à¥à¤°à¥‹"),
    VARA_MONDAY_DESC("Ruled by Moon - Good for travel, public dealings, emotional matters", "à¤šà¤¨à¥à¤¦à¥à¤°à¤®à¤¾ à¤¦à¥à¤µà¤¾à¤°à¤¾ à¤¶à¤¾à¤¸à¤¿à¤¤ - à¤¯à¤¾à¤¤à¥à¤°à¤¾, à¤¸à¤¾à¤°à¥à¤µà¤œà¤¨à¤¿à¤• à¤µà¥à¤¯à¤µà¤¹à¤¾à¤°, à¤­à¤¾à¤µà¤¨à¤¾à¤¤à¥à¤®à¤• à¤®à¤¾à¤®à¤¿à¤²à¤¾à¤¹à¤°à¥‚à¤•à¥‹ à¤²à¤¾à¤—à¤¿ à¤°à¤¾à¤®à¥à¤°à¥‹"),
    VARA_TUESDAY_DESC("Ruled by Mars - Good for property, surgery, competitive activities", "à¤®à¤‚à¤—à¤² à¤¦à¥à¤µà¤¾à¤°à¤¾ à¤¶à¤¾à¤¸à¤¿à¤¤ - à¤¸à¤®à¥à¤ªà¤¤à¥à¤¤à¤¿, à¤¶à¤²à¥à¤¯à¤•à¥à¤°à¤¿à¤¯à¤¾, à¤ªà¥à¤°à¤¤à¤¿à¤¸à¥à¤ªà¤°à¥à¤§à¤¾à¤¤à¥à¤®à¤• à¤—à¤¤à¤¿à¤µà¤¿à¤§à¤¿à¤¹à¤°à¥‚à¤•à¥‹ à¤²à¤¾à¤—à¤¿ à¤°à¤¾à¤®à¥à¤°à¥‹"),
    VARA_WEDNESDAY_DESC("Ruled by Mercury - Good for education, communication, business", "à¤¬à¥à¤§ à¤¦à¥à¤µà¤¾à¤°à¤¾ à¤¶à¤¾à¤¸à¤¿à¤¤ - à¤¶à¤¿à¤•à¥à¤·à¤¾, à¤¸à¤žà¥à¤šà¤¾à¤°, à¤µà¥à¤¯à¤¾à¤ªà¤¾à¤°à¤•à¥‹ à¤²à¤¾à¤—à¤¿ à¤°à¤¾à¤®à¥à¤°à¥‹"),
    VARA_THURSDAY_DESC("Ruled by Jupiter - Good for religious ceremonies, marriage, education", "à¤¬à¥ƒà¤¹à¤¸à¥à¤ªà¤¤à¤¿ à¤¦à¥à¤µà¤¾à¤°à¤¾ à¤¶à¤¾à¤¸à¤¿à¤¤ - à¤§à¤¾à¤°à¥à¤®à¤¿à¤• à¤¸à¤®à¤¾à¤°à¥‹à¤¹, à¤µà¤¿à¤µà¤¾à¤¹, à¤¶à¤¿à¤•à¥à¤·à¤¾à¤•à¥‹ à¤²à¤¾à¤—à¤¿ à¤°à¤¾à¤®à¥à¤°à¥‹"),
    VARA_FRIDAY_DESC("Ruled by Venus - Good for romance, marriage, arts, luxury", "à¤¶à¥à¤•à¥à¤° à¤¦à¥à¤µà¤¾à¤°à¤¾ à¤¶à¤¾à¤¸à¤¿à¤¤ - à¤ªà¥à¤°à¥‡à¤®, à¤µà¤¿à¤µà¤¾à¤¹, à¤•à¤²à¤¾, à¤µà¤¿à¤²à¤¾à¤¸à¤¿à¤¤à¤¾à¤•à¥‹ à¤²à¤¾à¤—à¤¿ à¤°à¤¾à¤®à¥à¤°à¥‹"),
    VARA_SATURDAY_DESC("Ruled by Saturn - Good for property, agriculture, spiritual discipline", "à¤¶à¤¨à¤¿ à¤¦à¥à¤µà¤¾à¤°à¤¾ à¤¶à¤¾à¤¸à¤¿à¤¤ - à¤¸à¤®à¥à¤ªà¤¤à¥à¤¤à¤¿, à¤•à¥ƒà¤·à¤¿, à¤†à¤§à¥à¤¯à¤¾à¤¤à¥à¤®à¤¿à¤• à¤…à¤¨à¥à¤¶à¤¾à¤¸à¤¨à¤•à¥‹ à¤²à¤¾à¤—à¤¿ à¤°à¤¾à¤®à¥à¤°à¥‹"),

    // ============================================
    // COMMON ACTION LABELS
    // ============================================
    ACTION_NEW_BEGINNINGS("New beginnings", "à¤¨à¤¯à¤¾à¤ à¤¸à¥à¤°à¥à¤µà¤¾à¤¤à¤¹à¤°à¥‚"),
    ACTION_STARTING_VENTURES("Starting ventures", "à¤‰à¤¦à¥à¤¯à¤®à¤¹à¤°à¥‚ à¤¸à¥à¤°à¥ à¤—à¤°à¥à¤¦à¥ˆ"),
    ACTION_TRAVEL("Travel", "à¤¯à¤¾à¤¤à¥à¤°à¤¾"),
    ACTION_MARRIAGE("Marriage", "à¤µà¤¿à¤µà¤¾à¤¹"),
    ACTION_EDUCATION("Education", "à¤¶à¤¿à¤•à¥à¤·à¤¾"),
    ACTION_BUSINESS("Business", "à¤µà¥à¤¯à¤¾à¤ªà¤¾à¤°"),
    ACTION_SPIRITUAL_PRACTICES("Spiritual practices", "à¤†à¤§à¥à¤¯à¤¾à¤¤à¥à¤®à¤¿à¤• à¤…à¤­à¥à¤¯à¤¾à¤¸"),
    ACTION_WORSHIP("Worship", "à¤ªà¥‚à¤œà¤¾"),
    ACTION_CHARITY("Charity", "à¤¦à¤¾à¤¨"),
    ACTION_FASTING("Fasting", "à¤‰à¤ªà¤µà¤¾à¤¸"),
    ACTION_MEDITATION("Meditation", "à¤§à¥à¤¯à¤¾à¤¨"),
    ACTION_SURGERY("Surgery", "à¤¶à¤²à¥à¤¯à¤•à¥à¤°à¤¿à¤¯à¤¾"),
    ACTION_CREATIVE_WORK("Creative work", "à¤¸à¤¿à¤°à¥à¤œà¤¨à¤¾à¤¤à¥à¤®à¤• à¤•à¤¾à¤®"),
    ACTION_GOVERNMENT_WORK("Government work", "à¤¸à¤°à¤•à¤¾à¤°à¥€ à¤•à¤¾à¤®"),
    ACTION_PROPERTY_MATTERS("Property matters", "à¤¸à¤®à¥à¤ªà¤¤à¥à¤¤à¤¿ à¤®à¤¾à¤®à¤¿à¤²à¤¾à¤¹à¤°à¥‚"),
    ACTION_FINANCIAL_MATTERS("Financial matters", "à¤µà¤¿à¤¤à¥à¤¤à¥€à¤¯ à¤®à¤¾à¤®à¤¿à¤²à¤¾à¤¹à¤°à¥‚"),
    ACTION_LEGAL_MATTERS("Legal matters", "à¤•à¤¾à¤¨à¥à¤¨à¥€ à¤®à¤¾à¤®à¤¿à¤²à¤¾à¤¹à¤°à¥‚"),

    // ============================================
    // NAVIGATION & COMMON UI ACTIONS
    // ============================================
    NAV_BACK("Back", "à¤ªà¤›à¤¾à¤¡à¤¿"),
    NAV_NAVIGATE_BACK("Navigate back", "à¤ªà¤›à¤¾à¤¡à¤¿ à¤œà¤¾à¤¨à¥à¤¹à¥‹à¤¸à¥"),
    NAV_PREVIOUS("Previous", "à¤…à¤˜à¤¿à¤²à¥à¤²à¥‹"),
    NAV_NEXT("Next", "à¤…à¤°à¥à¤•à¥‹"),
    NAV_PREVIOUS_YEAR("Previous year", "à¤…à¤˜à¤¿à¤²à¥à¤²à¥‹ à¤µà¤°à¥à¤·"),
    NAV_NEXT_YEAR("Next year", "à¤…à¤°à¥à¤•à¥‹ à¤µà¤°à¥à¤·"),
    NAV_PREVIOUS_DAY("Previous day", "à¤…à¤˜à¤¿à¤²à¥à¤²à¥‹ à¤¦à¤¿à¤¨"),
    NAV_NEXT_DAY("Next day", "à¤…à¤°à¥à¤•à¥‹ à¤¦à¤¿à¤¨"),

    ACTION_EXPORT("Export", "à¤¨à¤¿à¤°à¥à¤¯à¤¾à¤¤ à¤—à¤°à¥à¤¨à¥à¤¹à¥‹à¤¸à¥"),
    ACTION_CLEAR("Clear", "à¤–à¤¾à¤²à¥€ à¤—à¤°à¥à¤¨à¥à¤¹à¥‹à¤¸à¥"),
    ACTION_CLEAR_SEARCH("Clear search", "à¤–à¥‹à¤œ à¤–à¤¾à¤²à¥€ à¤—à¤°à¥à¤¨à¥à¤¹à¥‹à¤¸à¥"),
    ACTION_COPY("Copy", "à¤•à¤ªà¥€ à¤—à¤°à¥à¤¨à¥à¤¹à¥‹à¤¸à¥"),
    ACTION_COPY_MANTRA("Copy mantra", "à¤®à¤¨à¥à¤¤à¥à¤° à¤•à¤ªà¥€ à¤—à¤°à¥à¤¨à¥à¤¹à¥‹à¤¸à¥"),
    ACTION_VIEW_DETAILS("View details", "à¤µà¤¿à¤µà¤°à¤£à¤¹à¤°à¥‚ à¤¹à¥‡à¤°à¥à¤¨à¥à¤¹à¥‹à¤¸à¥"),
    ACTION_VIEW_FULLSCREEN("View fullscreen", "à¤ªà¥‚à¤°à¥à¤£ à¤¸à¥à¤•à¥à¤°à¤¿à¤¨à¤®à¤¾ à¤¹à¥‡à¤°à¥à¤¨à¥à¤¹à¥‹à¤¸à¥"),
    ACTION_NEW_QUESTION("New question", "à¤¨à¤¯à¤¾à¤ à¤ªà¥à¤°à¤¶à¥à¤¨"),
    ACTION_SEARCH("Search", "à¤–à¥‹à¤œà¥à¤¨à¥à¤¹à¥‹à¤¸à¥"),

    // ============================================
    // MISC - ADDITIONAL STRINGS FOR DASHA SCREENS
    // ============================================
    MISC_DAYS_LEFT("days left", "à¤¦à¤¿à¤¨ à¤¬à¤¾à¤à¤•à¥€"),
    MISC_MONTHS("months", "à¤®à¤¹à¤¿à¤¨à¤¾"),
    MISC_CURRENT("Current", "à¤µà¤°à¥à¤¤à¤®à¤¾à¤¨"),
    MISC_YEARS("years", "à¤µà¤°à¥à¤·"),
    DASHA_NO_CURRENT_PERIOD("No current period active", "à¤•à¥à¤¨à¥ˆ à¤µà¤°à¥à¤¤à¤®à¤¾à¤¨ à¤…à¤µà¤§à¤¿ à¤¸à¤•à¥à¤°à¤¿à¤¯ à¤›à¥ˆà¤¨"),
    DASHA_LEVEL_ANTARDASHAS("Antardashas", "à¤…à¤¨à¥à¤¤à¤°à¥à¤¦à¤¶à¤¾à¤¹à¤°à¥‚"),

    // ============================================
    // INTERPRETATION - CHARA DASHA
    // ============================================
    INTERPRETATION_TITLE("Interpretation", "à¤µà¥à¤¯à¤¾à¤–à¥à¤¯à¤¾"),
    INTERPRETATION_GENERAL("General Effects", "à¤¸à¤¾à¤®à¤¾à¤¨à¥à¤¯ à¤ªà¥à¤°à¤­à¤¾à¤µà¤¹à¤°à¥‚"),
    INTERPRETATION_LORD_EFFECTS("Lord Effects", "à¤¸à¥à¤µà¤¾à¤®à¥€ à¤ªà¥à¤°à¤­à¤¾à¤µà¤¹à¤°à¥‚"),
    INTERPRETATION_FAVORABLE("Favorable Areas", "à¤…à¤¨à¥à¤•à¥‚à¤² à¤•à¥à¤·à¥‡à¤¤à¥à¤°à¤¹à¤°à¥‚"),
    INTERPRETATION_CHALLENGES("Challenges", "à¤šà¥à¤¨à¥Œà¤¤à¥€à¤¹à¤°à¥‚"),
    INTERPRETATION_RECOMMENDATIONS("Recommendations", "à¤¸à¤¿à¤«à¤¾à¤°à¤¿à¤¸à¤¹à¤°à¥‚"),

    // ============================================
    // DASHA RECOMMENDATIONS
    // ============================================
    DASHA_RECOMMENDATIONS("Recommendations", "à¤¸à¤¿à¤«à¤¾à¤°à¤¿à¤¸à¤¹à¤°à¥‚"),

    // ============================================
    // BATCH 2 SCREEN KEYS
    // ============================================

    // Generic Actions & Accessibility
    ACC_VIEW_DETAILS("View details", "à¤µà¤¿à¤µà¤°à¤£ à¤¹à¥‡à¤°à¥à¤¨à¥à¤¹à¥‹à¤¸à¥"),
    ACC_COLLAPSE("Collapse", "à¤²à¥à¤•à¤¾à¤‰à¤¨à¥à¤¹à¥‹à¤¸à¥"),
    ACC_EXPAND("Expand", "à¤¦à¥‡à¤–à¤¾à¤‰à¤¨à¥à¤¹à¥‹à¤¸à¥"),
    ACC_FULLSCREEN("View fullscreen", "à¤ªà¥‚à¤°à¤¾ à¤¸à¥à¤•à¥à¤°à¥€à¤¨ à¤¹à¥‡à¤°à¥à¤¨à¥à¤¹à¥‹à¤¸à¥"),
    ACC_CHART_ANALYSIS("Chart analysis", "à¤•à¥à¤£à¥à¤¡à¤²à¥€ à¤µà¤¿à¤¶à¥à¤²à¥‡à¤·à¤£"),
    ACC_COMING_SOON("Coming soon", "à¤šà¤¾à¤à¤¡à¥ˆ à¤†à¤‰à¤à¤¦à¥ˆà¤›"),
    ACC_PROFILE_AVATAR("Profile avatar", "à¤ªà¥à¤°à¥‹à¤«à¤¾à¤‡à¤² à¤…à¤µà¤¤à¤¾à¤°"),

    // Chart & Planets
    CHART_LAGNA("Lagna", "à¤²à¤—à¥à¤¨"),
    CHART_MOON("Moon", "à¤šà¤¨à¥à¤¦à¥à¤°à¤®à¤¾"),
    CHART_SUN("Sun", "à¤¸à¥‚à¤°à¥à¤¯"),
    CHART_NAKSHATRA_LABEL("Nakshatra", "à¤¨à¤•à¥à¤·à¤¤à¥à¤°"),
    CHART_ASCENDANT_LABEL("Ascendant", "à¤²à¤—à¥à¤¨"),
    LABEL_NA("N/A", "-"),
    LABEL_DASH("-", "-"),

    // Settings Theme
    THEME_LIGHT("Light", "à¤‰à¤œà¥à¤¯à¤¾à¤²à¥‹"),
    THEME_DARK("Dark", "à¤—à¤¾à¤¢à¤¾"),
    THEME_SYSTEM("System", "à¤¸à¤¿à¤¸à¥à¤Ÿà¤®"),
    THEME_DESC_LIGHT("Always use light theme", "à¤¸à¤§à¥ˆà¤‚ à¤‰à¤œà¥à¤¯à¤¾à¤²à¥‹ à¤¥à¥€à¤® à¤ªà¥à¤°à¤¯à¥‹à¤— à¤—à¤°à¥à¤¨à¥à¤¹à¥‹à¤¸à¥"),
    THEME_DESC_DARK("Always use dark theme", "à¤¸à¤§à¥ˆà¤‚ à¤—à¤¾à¤¢à¤¾ à¤¥à¥€à¤® à¤ªà¥à¤°à¤¯à¥‹à¤— à¤—à¤°à¥à¤¨à¥à¤¹à¥‹à¤¸à¥"),
    THEME_DESC_SYSTEM("Follow device settings", "à¤¡à¤¿à¤­à¤¾à¤‡à¤¸ à¤¸à¥‡à¤Ÿà¤¿à¤™ à¤…à¤¨à¥à¤¸à¤°à¤£ à¤—à¤°à¥à¤¨à¥à¤¹à¥‹à¤¸à¥"),
    SETTINGS_THEME_TITLE("Theme", "à¤¥à¥€à¤®"),

    // Dignity Status
    DIGNITY_EXALTED_STATUS("Exalted", "à¤‰à¤šà¥à¤š"),
    DIGNITY_DEBILITATED_STATUS("Debilitated", "à¤¨à¥€à¤š"),
    DIGNITY_OWN_SIGN_STATUS("Own Sign", "à¤¸à¥à¤µà¤°à¤¾à¤¶à¤¿"),
    DIGNITY_NEUTRAL_STATUS("Neutral", "à¤¸à¤®"),

    // Errors & Dialogs
    ERROR_HOROSCOPE_UNAVAILABLE_FMT("Horoscope for {0} is currently unavailable.", "{0}à¤•à¥‹ à¤°à¤¾à¤¶à¤¿à¤«à¤² à¤¹à¤¾à¤² à¤‰à¤ªà¤²à¤¬à¥à¤§ à¤›à¥ˆà¤¨à¥¤"),
    ERROR_GENERIC_RETRY("Please try again later.", "à¤•à¥ƒà¤ªà¤¯à¤¾ à¤ªà¤›à¤¿ à¤«à¥‡à¤°à¤¿ à¤ªà¥à¤°à¤¯à¤¾à¤¸ à¤—à¤°à¥à¤¨à¥à¤¹à¥‹à¤¸à¥à¥¤"),

    // Navigation & Tabs
    NAV_HOME("Home", "à¤—à¥ƒà¤¹"),
    NAV_INSIGHTS("Insights", "à¤…à¤¨à¥à¤¤à¤°à¥à¤¦à¥ƒà¤·à¥à¤Ÿà¤¿"),
    NAV_CHAT("Chat", "à¤•à¥à¤°à¤¾à¤•à¤¾à¤¨à¥€"),
    NAV_SETTINGS("Settings", "à¤¸à¥‡à¤Ÿà¤¿à¤™à¤¹à¤°à¥‚"),

    // Home Screen
    HOME_CHART_ANALYSIS("Your Chart Analysis", "à¤¤à¤ªà¤¾à¤ˆà¤‚à¤•à¥‹ à¤•à¥à¤£à¥à¤¡à¤²à¥€ à¤µà¤¿à¤¶à¥à¤²à¥‡à¤·à¤£"),
    HOME_COMING_SOON("Coming Soon", "à¤šà¤¾à¤à¤¡à¥ˆ à¤†à¤‰à¤à¤¦à¥ˆà¤›"),
    HOME_SOON_BADGE("Soon", "à¤šà¤¾à¤à¤¡à¥ˆ"),
    NO_PROFILE_SELECTED("No Profile Selected", "à¤•à¥à¤¨à¥ˆ à¤ªà¥à¤°à¥‹à¤«à¤¾à¤‡à¤² à¤›à¤¾à¤¨à¤¿à¤à¤•à¥‹ à¤›à¥ˆà¤¨"),
    NO_PROFILE_MESSAGE("Create or select a profile to view personalized insights and charts.", "à¤µà¥à¤¯à¤•à¥à¤¤à¤¿à¤—à¤¤ à¤…à¤¨à¥à¤¤à¤°à¥à¤¦à¥ƒà¤·à¥à¤Ÿà¤¿ à¤° à¤•à¥à¤£à¥à¤¡à¤²à¥€à¤¹à¤°à¥‚ à¤¹à¥‡à¤°à¥à¤¨ à¤ªà¥à¤°à¥‹à¤«à¤¾à¤‡à¤² à¤¬à¤¨à¤¾à¤‰à¤¨à¥à¤¹à¥‹à¤¸à¥ à¤µà¤¾ à¤›à¤¾à¤¨à¥à¤¨à¥à¤¹à¥‹à¤¸à¥à¥¤"),
    BTN_CREATE_CHART("Create New Chart", "à¤¨à¤¯à¤¾à¤ à¤•à¥à¤£à¥à¤¡à¤²à¥€ à¤¬à¤¨à¤¾à¤‰à¤¨à¥à¤¹à¥‹à¤¸à¥"),
    BTN_VIEW_DETAILS("View Details", "à¤µà¤¿à¤µà¤°à¤£ à¤¹à¥‡à¤°à¥à¤¨à¥à¤¹à¥‹à¤¸à¥"),

    // Insights Error States
    ERROR_PARTIAL("Partial Data Loaded", "à¤†à¤‚à¤¶à¤¿à¤• à¤¡à¤¾à¤Ÿà¤¾ à¤²à¥‹à¤¡ à¤­à¤¯à¥‹"),
    ERROR_CALCULATIONS_FAILED("{0} calculations failed", "{0} à¤—à¤£à¤¨à¤¾à¤¹à¤°à¥‚ à¤…à¤¸à¤«à¤² à¤­à¤"),
    ERROR_UNABLE_TO_LOAD("Unable to Load", "à¤²à¥‹à¤¡ à¤—à¤°à¥à¤¨ à¤¸à¤•à¤¿à¤à¤¨"),
    BTN_RETRY("Retry", "à¤«à¥‡à¤°à¤¿ à¤ªà¥à¤°à¤¯à¤¾à¤¸ à¤—à¤°à¥à¤¨à¥à¤¹à¥‹à¤¸à¥"),
    BTN_TRY_AGAIN("Try Again", "à¤«à¥‡à¤°à¤¿ à¤ªà¥à¤°à¤¯à¤¾à¤¸ à¤—à¤°à¥à¤¨à¥à¤¹à¥‹à¤¸à¥"),
    BTN_PREVIEW("Preview", "à¤ªà¥‚à¤°à¥à¤µà¤¾à¤µà¤²à¥‹à¤•à¤¨"),

    // Insights - Transits
    TRANSITS_MOON_IN("Moon in {0}", "{0}à¤®à¤¾ à¤šà¤¨à¥à¤¦à¥à¤°à¤®à¤¾"),

    SETTINGS_PROFILE("Profile", "à¤ªà¥à¤°à¥‹à¤«à¤¾à¤‡à¤²"),
    SETTINGS_EDIT_PROFILE("Edit Profile", "à¤ªà¥à¤°à¥‹à¤«à¤¾à¤‡à¤² à¤¸à¤®à¥à¤ªà¤¾à¤¦à¤¨ à¤—à¤°à¥à¤¨à¥à¤¹à¥‹à¤¸à¥"),
    SETTINGS_EDIT_PROFILE_DESC("Modify current charts details", "à¤¹à¤¾à¤²à¤•à¥‹ à¤•à¥à¤£à¥à¤¡à¤²à¥€ à¤µà¤¿à¤µà¤°à¤£ à¤ªà¤°à¤¿à¤®à¤¾à¤°à¥à¤œà¤¨ à¤—à¤°à¥à¤¨à¥à¤¹à¥‹à¤¸à¥"),
    SETTINGS_MANAGE_PROFILES("Manage Profiles", "à¤ªà¥à¤°à¥‹à¤«à¤¾à¤‡à¤²à¤¹à¤°à¥‚ à¤µà¥à¤¯à¤µà¤¸à¥à¤¥à¤¾à¤ªà¤¨ à¤—à¤°à¥à¤¨à¥à¤¹à¥‹à¤¸à¥"),
    SETTINGS_NO_PROFILE("No Profile", "à¤•à¥à¤¨à¥ˆ à¤ªà¥à¤°à¥‹à¤«à¤¾à¤‡à¤² à¤›à¥ˆà¤¨"),
    SETTINGS_TAP_TO_SELECT("Tap to create or select a profile", "à¤ªà¥à¤°à¥‹à¤«à¤¾à¤‡à¤² à¤¬à¤¨à¤¾à¤‰à¤¨ à¤µà¤¾ à¤›à¤¾à¤¨à¥à¤¨ à¤Ÿà¥à¤¯à¤¾à¤ª à¤—à¤°à¥à¤¨à¥à¤¹à¥‹à¤¸à¥"),
    SETTINGS_EXPORT("Export", "à¤¨à¤¿à¤°à¥à¤¯à¤¾à¤¤"),
    SETTINGS_EXPORT_PDF("Export as PDF", "PDF à¤•à¥‹ à¤°à¥‚à¤ªà¤®à¤¾ à¤¨à¤¿à¤°à¥à¤¯à¤¾à¤¤ à¤—à¤°à¥à¤¨à¥à¤¹à¥‹à¤¸à¥"),
    SETTINGS_EXPORT_PDF_DESC("Save chart details as PDF file", "à¤•à¥à¤£à¥à¤¡à¤²à¥€ à¤µà¤¿à¤µà¤°à¤£ PDF à¤«à¤¾à¤‡à¤²à¤•à¥‹ à¤°à¥‚à¤ªà¤®à¤¾ à¤¬à¤šà¤¤ à¤—à¤°à¥à¤¨à¥à¤¹à¥‹à¤¸à¥"),
    SETTINGS_AI_CHAT("AI & Chat", "AI à¤° à¤•à¥à¤°à¤¾à¤•à¤¾à¤¨à¥€"),
    SETTINGS_AI_MODELS("AI Models", "AI à¤®à¥‹à¤¡à¥‡à¤²à¤¹à¤°à¥‚"),
    SETTINGS_AI_MODELS_DESC("Configure AI model settings", "AI à¤®à¥‹à¤¡à¥‡à¤² à¤¸à¥‡à¤Ÿà¤¿à¤™à¤¹à¤°à¥‚ à¤•à¤¨à¥à¤«à¤¿à¤—à¤° à¤—à¤°à¥à¤¨à¥à¤¹à¥‹à¤¸à¥"),
    SETTINGS_PREFERENCES("Preferences", "à¤ªà¥à¤°à¤¾à¤¥à¤®à¤¿à¤•à¤¤à¤¾à¤¹à¤°à¥‚"),
    SETTINGS_LANGUAGE("Language", "à¤­à¤¾à¤·à¤¾"),
    SETTINGS_HOUSE_SYSTEM("House System", "à¤­à¤¾à¤µ à¤ªà¥à¤°à¤£à¤¾à¤²à¥€"),
    SETTINGS_ABOUT("About", "à¤¬à¤¾à¤°à¥‡à¤®à¤¾"),
    SETTINGS_ABOUT_APP("AstroStorm", "à¤à¤¸à¥à¤Ÿà¥à¤°à¥‹à¤¸à¥à¤Ÿà¤°à¥à¤®"),
    SETTINGS_VERSION("Version {0}", "à¤¸à¤‚à¤¸à¥à¤•à¤°à¤£ {0}"),
    SETTINGS_CALC_ENGINE("Calculation Engine", "à¤—à¤£à¤¨à¤¾ à¤‡à¤¨à¥à¤œà¤¿à¤¨"),
    SETTINGS_CALC_ENGINE_DESC("Swiss Ephemeris & Vedic Algorithms", "à¤¸à¥à¤µà¤¿à¤¸ à¤à¤«à¤¿à¤®à¥‡à¤°à¤¿à¤¸ à¤° à¤µà¥ˆà¤¦à¤¿à¤• à¤à¤²à¥à¤—à¥‹à¤°à¤¿à¤¦à¤®à¤¹à¤°à¥‚"),
    DIALOG_DELETE_PROFILE("Delete Profile", "à¤ªà¥à¤°à¥‹à¤«à¤¾à¤‡à¤² à¤®à¥‡à¤Ÿà¤¾à¤‰à¤¨à¥à¤¹à¥‹à¤¸à¥"),
    DIALOG_DELETE_CONFIRM("Are you sure you want to delete profile ''{0}''? This action cannot be undone.", "à¤•à¥‡ à¤¤à¤ªà¤¾à¤ˆà¤‚ à¤¸à¤¾à¤à¤šà¥à¤šà¤¿à¤•à¥ˆ ''{0}'' à¤ªà¥à¤°à¥‹à¤«à¤¾à¤‡à¤² à¤®à¥‡à¤Ÿà¤¾à¤‰à¤¨ à¤šà¤¾à¤¹à¤¨à¥à¤¹à¥à¤¨à¥à¤›? à¤¯à¥‹ à¤•à¤¾à¤°à¥à¤¯ à¤ªà¥‚à¤°à¥à¤µà¤µà¤¤ à¤—à¤°à¥à¤¨ à¤¸à¤•à¤¿à¤à¤¦à¥ˆà¤¨à¥¤"),
    BTN_DELETE("Delete", "à¤®à¥‡à¤Ÿà¤¾à¤‰à¤¨à¥à¤¹à¥‹à¤¸à¥"),
    BTN_CANCEL("Cancel", "à¤°à¤¦à¥à¤§ à¤—à¤°à¥à¤¨à¥à¤¹à¥‹à¤¸à¥");
}
