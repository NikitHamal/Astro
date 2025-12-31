package com.astro.storm.data.localization


/**
 * Transit, Panchanga, Prashna, and Varshaphala string keys
 * Part 3 of 4 split enums to avoid JVM method size limit
 */
enum class StringKeyAnalysis(override val en: String, override val ne: String) : StringKeyInterface {

    // ============================================
    // TRANSITS TAB - ADDITIONAL STRINGS
    // ============================================
    TRANSIT_OVERVIEW("Transit Overview", "à¤—à¥‹à¤šà¤° à¤¸à¤¿à¤‚à¤¹à¤¾à¤µà¤²à¥‹à¤•à¤¨"),
    TRANSIT_FAVORABLE("Favorable", "à¤…à¤¨à¥à¤•à¥‚à¤²"),
    TRANSIT_CHALLENGING("Challenging", "à¤šà¥à¤¨à¥Œà¤¤à¥€à¤ªà¥‚à¤°à¥à¤£"),
    TRANSIT_ASPECTS("Aspects", "à¤¦à¥ƒà¤·à¥à¤Ÿà¤¿à¤¹à¤°à¥‚"),
    TRANSIT_OVERALL_SCORE("Overall Transit Score", "à¤¸à¤®à¤—à¥à¤° à¤—à¥‹à¤šà¤° à¤…à¤‚à¤•"),
    TRANSIT_CURRENT_POSITIONS("Current Planetary Positions", "à¤¹à¤¾à¤²à¤•à¥‹ à¤—à¥à¤°à¤¹ à¤¸à¥à¤¥à¤¿à¤¤à¤¿à¤¹à¤°à¥‚"),
    TRANSIT_GOCHARA_ANALYSIS("Gochara Analysis (From Moon)", "à¤—à¥‹à¤šà¤° à¤µà¤¿à¤¶à¥à¤²à¥‡à¤·à¤£ (à¤šà¤¨à¥à¤¦à¥à¤°à¤¬à¤¾à¤Ÿ)"),
    TRANSIT_ASPECTS_TO_NATAL("Transit Aspects to Natal", "à¤œà¤¨à¥à¤® à¤•à¥à¤£à¥à¤¡à¤²à¥€à¤®à¤¾ à¤—à¥‹à¤šà¤° à¤¦à¥ƒà¤·à¥à¤Ÿà¤¿"),
    TRANSIT_SIGNIFICANT_PERIODS("Significant Periods", "à¤®à¤¹à¤¤à¥à¤¤à¥à¤µà¤ªà¥‚à¤°à¥à¤£ à¤…à¤µà¤§à¤¿à¤¹à¤°à¥‚"),
    TRANSIT_APPLYING("Applying", "à¤¸à¤®à¥€à¤ª à¤†à¤‰à¤à¤¦à¥ˆ"),
    TRANSIT_SEPARATING("Separating", "à¤Ÿà¤¾à¤¢à¤¾ à¤¹à¥à¤à¤¦à¥ˆ"),
    TRANSIT_ORB("Orb: %sÂ°", "à¤•à¥‹à¤£à¤¾à¤¨à¥à¤¤à¤°: %sÂ°"),
    TRANSIT_VEDHA_FROM("Vedha from %s", "%s à¤¬à¤¾à¤Ÿ à¤µà¥‡à¤§"),
    TRANSIT_INTENSITY("Intensity %d/5", "à¤¤à¥€à¤µà¥à¤°à¤¤à¤¾ %d/à¥«"),
    TRANSIT_HOUSE_FROM_MOON("House %d", "à¤­à¤¾à¤µ %d"),
    TRANSIT_PLANET_POSITIONS("Planet Positions", "à¤—à¥à¤°à¤¹ à¤¸à¥à¤¥à¤¿à¤¤à¤¿à¤¹à¤°à¥‚"),
    TRANSIT_CURRENT_MOVEMENTS("Current planetary movements - %s", "à¤µà¤°à¥à¤¤à¤®à¤¾à¤¨ à¤—à¥à¤°à¤¹ à¤—à¤¤à¤¿à¤¹à¤°à¥‚ - %s"),
    TRANSIT_OVERALL_ASSESSMENT("Overall Assessment", "à¤¸à¤®à¤—à¥à¤° à¤®à¥‚à¤²à¥à¤¯à¤¾à¤™à¥à¤•à¤¨"),
    TRANSIT_HOUSE_LABEL("House", "à¤­à¤¾à¤µ"),
    TRANSIT_LABEL("TRANSIT", "à¤—à¥‹à¤šà¤°"),
    TRANSIT_NATAL_LABEL("NATAL", "à¤œà¤¨à¥à¤®à¤•à¤¾à¤²à¥€à¤¨"),
    TRANSIT_NO_PLANETS_TRANSITING("No planets transiting", "à¤•à¥à¤¨à¥ˆ à¤—à¥à¤°à¤¹ à¤—à¥‹à¤šà¤° à¤—à¤°à¥à¤¦à¥ˆà¤¨"),
    TRANSIT_UPCOMING("Significant Upcoming Transits", "à¤®à¤¹à¤¤à¥à¤¤à¥à¤µà¤ªà¥‚à¤°à¥à¤£ à¤†à¤—à¤¾à¤®à¥€ à¤—à¥‹à¤šà¤°à¤¹à¤°à¥‚"),
    TRANSIT_NO_UPCOMING("No significant upcoming transits in the near future", "à¤¨à¤œà¤¿à¤•à¤•à¥‹ à¤­à¤µà¤¿à¤·à¥à¤¯à¤®à¤¾ à¤•à¥à¤¨à¥ˆ à¤®à¤¹à¤¤à¥à¤¤à¥à¤µà¤ªà¥‚à¤°à¥à¤£ à¤†à¤—à¤¾à¤®à¥€ à¤—à¥‹à¤šà¤° à¤›à¥ˆà¤¨"),
    TRANSIT_TO_NATAL_ASPECTS("Transit to Natal Aspects", "à¤œà¤¨à¥à¤® à¤•à¥à¤£à¥à¤¡à¤²à¥€à¤®à¤¾ à¤—à¥‹à¤šà¤° à¤¦à¥ƒà¤·à¥à¤Ÿà¤¿"),
    TRANSIT_NO_ASPECTS("No significant transit aspects currently active", "à¤¹à¤¾à¤² à¤•à¥à¤¨à¥ˆ à¤®à¤¹à¤¤à¥à¤¤à¥à¤µà¤ªà¥‚à¤°à¥à¤£ à¤—à¥‹à¤šà¤° à¤¦à¥ƒà¤·à¥à¤Ÿà¤¿ à¤¸à¤•à¥à¤°à¤¿à¤¯ à¤›à¥ˆà¤¨"),
    TRANSIT_NO_DATA("No Transit Data", "à¤•à¥à¤¨à¥ˆ à¤—à¥‹à¤šà¤° à¤¡à¤¾à¤Ÿà¤¾ à¤›à¥ˆà¤¨"),
    TRANSIT_SELECT_CHART("Please select a birth chart to view transits", "à¤—à¥‹à¤šà¤° à¤¹à¥‡à¤°à¥à¤¨ à¤œà¤¨à¥à¤® à¤•à¥à¤£à¥à¤¡à¤²à¥€ à¤›à¤¾à¤¨à¥à¤¨à¥à¤¹à¥‹à¤¸à¥"),
    TRANSIT_CURRENT_INFLUENCES("Current planetary influences", "à¤µà¤°à¥à¤¤à¤®à¤¾à¤¨ à¤—à¥à¤°à¤¹ à¤ªà¥à¤°à¤­à¤¾à¤µà¤¹à¤°à¥‚"),
    TRANSIT_PLANETS_COUNT("Planets", "à¤—à¥à¤°à¤¹à¤¹à¤°à¥‚"),
    TRANSIT_MAJOR_TRANSITS("Major Transits", "à¤ªà¥à¤°à¤®à¥à¤– à¤—à¥‹à¤šà¤°à¤¹à¤°à¥‚"),
    TRANSIT_QUALITY_LABEL("Quality", "à¤—à¥à¤£à¤¸à¥à¤¤à¤°"),
    TRANSIT_RETROGRADE_SYMBOL("R", "à¤µ"),

    // ============================================
    // PANCHANGA TAB - ADDITIONAL STRINGS
    // ============================================
    PANCHANGA_AT_BIRTH("Panchanga at Birth", "à¤œà¤¨à¥à¤®à¤•à¥‹ à¤¸à¤®à¤¯à¤•à¥‹ à¤ªà¤žà¥à¤šà¤¾à¤™à¥à¤—"),
    PANCHANGA_ABOUT("About Panchanga", "à¤ªà¤žà¥à¤šà¤¾à¤™à¥à¤—à¤•à¥‹ à¤¬à¤¾à¤°à¥‡à¤®à¤¾"),
    PANCHANGA_ABOUT_INTRO("à¤ªà¤žà¥à¤šà¤¾à¤™à¥à¤— à¤ªà¤°à¤¿à¤šà¤¯", "à¤ªà¤žà¥à¤šà¤¾à¤™à¥à¤— à¤ªà¤°à¤¿à¤šà¤¯"),
    PANCHANGA_SANSKRIT("à¤ªà¤žà¥à¤šà¤¾à¤™à¥à¤—", "à¤ªà¤žà¥à¤šà¤¾à¤™à¥à¤—"),
    PANCHANGA_LUNAR_DAY("Lunar Day", "à¤šà¤¨à¥à¤¦à¥à¤° à¤¦à¤¿à¤¨"),
    PANCHANGA_LUNAR_DAY_SANSKRIT("à¤¤à¤¿à¤¥à¤¿", "à¤¤à¤¿à¤¥à¤¿"),
    PANCHANGA_LUNAR_MANSION("Lunar Mansion", "à¤šà¤¨à¥à¤¦à¥à¤° à¤¨à¤•à¥à¤·à¤¤à¥à¤°"),
    PANCHANGA_LUNAR_MANSION_SANSKRIT("à¤¨à¤•à¥à¤·à¤¤à¥à¤°", "à¤¨à¤•à¥à¤·à¤¤à¥à¤°"),
    PANCHANGA_LUNISOLAR("Luni-Solar Combination", "à¤šà¤¨à¥à¤¦à¥à¤°-à¤¸à¥‚à¤°à¥à¤¯ à¤¸à¤‚à¤¯à¥‹à¤œà¤¨"),
    PANCHANGA_LUNISOLAR_SANSKRIT("à¤¯à¥‹à¤—", "à¤¯à¥‹à¤—"),
    PANCHANGA_HALF_LUNAR_DAY("Half Lunar Day", "à¤…à¤°à¥à¤§ à¤šà¤¨à¥à¤¦à¥à¤° à¤¦à¤¿à¤¨"),
    PANCHANGA_HALF_LUNAR_DAY_SANSKRIT("à¤•à¤°à¤£", "à¤•à¤°à¤£"),
    PANCHANGA_WEEKDAY("Weekday", "à¤µà¤¾à¤°"),
    PANCHANGA_WEEKDAY_SANSKRIT("à¤µà¤¾à¤°", "à¤µà¤¾à¤°"),
    PANCHANGA_NUMBER("Number", "à¤¸à¤‚à¤–à¥à¤¯à¤¾"),
    PANCHANGA_NUMBER_OF("of", "à¤®à¤§à¥à¤¯à¥‡"),
    PANCHANGA_DEITY("Deity", "à¤¦à¥‡à¤µà¤¤à¤¾"),
    PANCHANGA_LORD("Lord", "à¤¸à¥à¤µà¤¾à¤®à¥€"),
    PANCHANGA_NATURE("Nature", "à¤ªà¥à¤°à¤•à¥ƒà¤¤à¤¿"),
    PANCHANGA_PROGRESS("Progress", "à¤ªà¥à¤°à¤—à¤¤à¤¿"),
    PANCHANGA_SYMBOL("Symbol", "à¤šà¤¿à¤¨à¥à¤¹"),
    PANCHANGA_GANA("Gana", "à¤—à¤£"),
    PANCHANGA_GUNA("Guna", "à¤—à¥à¤£"),
    PANCHANGA_ANIMAL("Animal", "à¤ªà¤¶à¥"),
    PANCHANGA_MEANING("Meaning", "à¤…à¤°à¥à¤¥"),
    PANCHANGA_TYPE("Type", "à¤ªà¥à¤°à¤•à¤¾à¤°"),
    PANCHANGA_ELEMENT("Element", "à¤¤à¤¤à¥à¤µ"),
    PANCHANGA_DIRECTION("Direction", "à¤¦à¤¿à¤¶à¤¾"),
    PANCHANGA_RULING_PLANET("Ruling Planet", "à¤¶à¤¾à¤¸à¤• à¤—à¥à¤°à¤¹"),
    PANCHANGA_SIGNIFICANCE("Significance", "à¤®à¤¹à¤¤à¥à¤¤à¥à¤µ"),
    PANCHANGA_CHARACTERISTICS("Characteristics", "à¤µà¤¿à¤¶à¥‡à¤·à¤¤à¤¾à¤¹à¤°à¥‚"),
    PANCHANGA_EFFECTS("Effects", "à¤ªà¥à¤°à¤­à¤¾à¤µà¤¹à¤°à¥‚"),
    PANCHANGA_FAVORABLE_ACTIVITIES("Favorable Activities", "à¤…à¤¨à¥à¤•à¥‚à¤² à¤—à¤¤à¤¿à¤µà¤¿à¤§à¤¿à¤¹à¤°à¥‚"),
    PANCHANGA_AVOID("Activities to Avoid", "à¤Ÿà¤¾à¤¢à¤¾ à¤°à¤¹à¤¨à¥ à¤ªà¤°à¥à¤¨à¥‡ à¤—à¤¤à¤¿à¤µà¤¿à¤§à¤¿à¤¹à¤°à¥‚"),
    PANCHANGA_TITHI_SUBTITLE("Lunar Day â€¢ à¤¤à¤¿à¤¥à¤¿", "à¤šà¤¨à¥à¤¦à¥à¤° à¤¦à¤¿à¤¨ â€¢ à¤¤à¤¿à¤¥à¤¿"),
    PANCHANGA_NAKSHATRA_SUBTITLE("Lunar Mansion â€¢ à¤¨à¤•à¥à¤·à¤¤à¥à¤°", "à¤šà¤¨à¥à¤¦à¥à¤° à¤¨à¤•à¥à¤·à¤¤à¥à¤° â€¢ à¤¨à¤•à¥à¤·à¤¤à¥à¤°"),
    PANCHANGA_YOGA_SUBTITLE("Luni-Solar Combination â€¢ à¤¯à¥‹à¤—", "à¤šà¤¨à¥à¤¦à¥à¤°-à¤¸à¥‚à¤°à¥à¤¯ à¤¸à¤‚à¤¯à¥‹à¤œà¤¨ â€¢ à¤¯à¥‹à¤—"),
    PANCHANGA_KARANA_SUBTITLE("Half Lunar Day â€¢ à¤•à¤°à¤£", "à¤…à¤°à¥à¤§ à¤šà¤¨à¥à¤¦à¥à¤° à¤¦à¤¿à¤¨ â€¢ à¤•à¤°à¤£"),
    PANCHANGA_VARA_SUBTITLE("Weekday â€¢ à¤µà¤¾à¤°", "à¤µà¤¾à¤° â€¢ à¤µà¤¾à¤°"),
    PANCHANGA_SANSKRIT_LABEL("Sanskrit", "à¤¸à¤‚à¤¸à¥à¤•à¥ƒà¤¤"),
    PANCHANGA_PADA("Pada", "à¤ªà¤¦"),
    PANCHANGA_RULER("Ruler", "à¤¸à¥à¤µà¤¾à¤®à¥€"),
    PANCHANGA_ABOUT_SUBTITLE("à¤ªà¤žà¥à¤šà¤¾à¤™à¥à¤— à¤ªà¤°à¤¿à¤šà¤¯", "à¤ªà¤žà¥à¤šà¤¾à¤™à¥à¤— à¤ªà¤°à¤¿à¤šà¤¯"),
    PANCHANGA_ABOUT_DESCRIPTION("Panchanga (Sanskrit: à¤ªà¤žà¥à¤šà¤¾à¤™à¥à¤—, \"five limbs\") is the traditional Hindu calendar and almanac. It tracks five fundamental elements of Vedic time-keeping, essential for determining auspicious moments (muhurta) for important activities.", "à¤ªà¤žà¥à¤šà¤¾à¤™à¥à¤— (à¤¸à¤‚à¤¸à¥à¤•à¥ƒà¤¤: à¤ªà¤žà¥à¤šà¤¾à¤™à¥à¤—, \"à¤ªà¤¾à¤à¤š à¤…à¤‚à¤—\") à¤ªà¤°à¤®à¥à¤ªà¤°à¤¾à¤—à¤¤ à¤¹à¤¿à¤¨à¥à¤¦à¥‚ à¤ªà¤¾à¤¤à¥à¤°à¥‹ à¤° à¤ªà¤‚à¤šà¤¾à¤‚à¤— à¤¹à¥‹à¥¤ à¤¯à¤¸à¤²à¥‡ à¤µà¥ˆà¤¦à¤¿à¤• à¤¸à¤®à¤¯-à¤—à¤£à¤¨à¤¾à¤•à¥‹ à¤ªà¤¾à¤à¤š à¤®à¥Œà¤²à¤¿à¤• à¤¤à¤¤à¥à¤µà¤¹à¤°à¥‚à¤•à¥‹ à¤Ÿà¥à¤°à¥à¤¯à¤¾à¤• à¤—à¤°à¥à¤¦à¤›, à¤œà¥à¤¨ à¤®à¤¹à¤¤à¥à¤¤à¥à¤µà¤ªà¥‚à¤°à¥à¤£ à¤•à¤¾à¤°à¥à¤¯à¤¹à¤°à¥‚à¤•à¥‹ à¤²à¤¾à¤—à¤¿ à¤¶à¥à¤­ à¤•à¥à¤·à¤£ (à¤®à¥à¤¹à¥‚à¤°à¥à¤¤) à¤¨à¤¿à¤°à¥à¤§à¤¾à¤°à¤£ à¤—à¤°à¥à¤¨ à¤†à¤µà¤¶à¥à¤¯à¤• à¤›à¥¤"),
    PANCHANGA_TITHI_DESC("Based on the angular distance between Sun and Moon. Each tithi spans 12Â° of lunar elongation. There are 30 tithis in a lunar month.", "à¤¸à¥‚à¤°à¥à¤¯ à¤° à¤šà¤¨à¥à¤¦à¥à¤°à¤®à¤¾à¤¬à¥€à¤šà¤•à¥‹ à¤•à¥‹à¤£à¥€à¤¯ à¤¦à¥‚à¤°à¥€à¤®à¤¾ à¤†à¤§à¤¾à¤°à¤¿à¤¤à¥¤ à¤ªà¥à¤°à¤¤à¥à¤¯à¥‡à¤• à¤¤à¤¿à¤¥à¤¿ à¥§à¥¨Â° à¤šà¤¨à¥à¤¦à¥à¤° à¤µà¤¿à¤¸à¥à¤¤à¤¾à¤° à¤¸à¤®à¥‡à¤Ÿà¥à¤›à¥¤ à¤à¤• à¤šà¤¾à¤¨à¥à¤¦à¥à¤° à¤®à¤¹à¤¿à¤¨à¤¾à¤®à¤¾ à¥©à¥¦ à¤¤à¤¿à¤¥à¤¿ à¤¹à¥à¤¨à¥à¤›à¤¨à¥à¥¤"),
    PANCHANGA_NAKSHATRA_DESC("The Moon's position among 27 stellar constellations, each spanning 13Â°20'. Determines the Moon's influence on consciousness.", "à¥¨à¥­ à¤¨à¤•à¥à¤·à¤¤à¥à¤° à¤¤à¤¾à¤°à¤¾à¤®à¤£à¥à¤¡à¤²à¤¹à¤°à¥‚ à¤¬à¥€à¤š à¤šà¤¨à¥à¤¦à¥à¤°à¤®à¤¾à¤•à¥‹ à¤¸à¥à¤¥à¤¿à¤¤à¤¿, à¤ªà¥à¤°à¤¤à¥à¤¯à¥‡à¤• à¥§à¥©Â°à¥¨à¥¦' à¤¸à¤®à¥‡à¤Ÿà¥à¤›à¥¤ à¤šà¥‡à¤¤à¤¨à¤¾à¤®à¤¾ à¤šà¤¨à¥à¤¦à¥à¤°à¤®à¤¾à¤•à¥‹ à¤ªà¥à¤°à¤­à¤¾à¤µ à¤¨à¤¿à¤°à¥à¤§à¤¾à¤°à¤£ à¤—à¤°à¥à¤¦à¤›à¥¤"),
    PANCHANGA_YOGA_DESC("Derived from the sum of Sun and Moon longitudes divided into 27 equal parts. Indicates the overall quality of time.", "à¤¸à¥‚à¤°à¥à¤¯ à¤° à¤šà¤¨à¥à¤¦à¥à¤°à¤®à¤¾à¤•à¥‹ à¤¦à¥‡à¤¶à¤¾à¤¨à¥à¤¤à¤°à¤•à¥‹ à¤¯à¥‹à¤—à¤«à¤²à¤²à¤¾à¤ˆ à¥¨à¥­ à¤¬à¤°à¤¾à¤¬à¤° à¤­à¤¾à¤—à¤®à¤¾ à¤µà¤¿à¤­à¤¾à¤œà¤¿à¤¤ à¤—à¤°à¥€ à¤µà¥à¤¯à¥à¤¤à¥à¤ªà¤¨à¥à¤¨à¥¤ à¤¸à¤®à¤¯à¤•à¥‹ à¤¸à¤®à¤—à¥à¤° à¤—à¥à¤£à¤¸à¥à¤¤à¤° à¤¸à¤‚à¤•à¥‡à¤¤ à¤—à¤°à¥à¤¦à¤›à¥¤"),
    PANCHANGA_KARANA_DESC("Each tithi has two karanas. There are 11 karanas (4 fixed, 7 repeating) cycling through the month.", "à¤ªà¥à¤°à¤¤à¥à¤¯à¥‡à¤• à¤¤à¤¿à¤¥à¤¿à¤®à¤¾ à¤¦à¥à¤ˆ à¤•à¤°à¤£ à¤¹à¥à¤¨à¥à¤›à¤¨à¥à¥¤ à¥§à¥§ à¤•à¤°à¤£ (à¥ª à¤¸à¥à¤¥à¤¿à¤°, à¥­ à¤¦à¥‹à¤¹à¥‹à¤°à¤¿à¤¨à¥‡) à¤®à¤¹à¤¿à¤¨à¤¾à¤­à¤° à¤šà¤•à¥à¤°à¤¿à¤¤ à¤¹à¥à¤¨à¥à¤›à¤¨à¥à¥¤"),
    PANCHANGA_VARA_DESC("Each day is ruled by a planet, influencing the day's energy and suitable activities.", "à¤ªà¥à¤°à¤¤à¥à¤¯à¥‡à¤• à¤¦à¤¿à¤¨ à¤à¤• à¤—à¥à¤°à¤¹à¤²à¥‡ à¤¶à¤¾à¤¸à¤¨ à¤—à¤°à¥à¤¦à¤›, à¤¦à¤¿à¤¨à¤•à¥‹ à¤Šà¤°à¥à¤œà¤¾ à¤° à¤‰à¤ªà¤¯à¥à¤•à¥à¤¤ à¤—à¤¤à¤¿à¤µà¤¿à¤§à¤¿à¤¹à¤°à¥‚à¤²à¤¾à¤ˆ à¤ªà¥à¤°à¤­à¤¾à¤µ à¤ªà¤¾à¤°à¥à¤¦à¤›à¥¤"),
    PANCHANGA_BIRTH_INSIGHT("The Panchanga at birth reveals the cosmic influences active at the moment of incarnation, providing insights into one's inherent nature, tendencies, and life patterns.", "à¤œà¤¨à¥à¤®à¤•à¥‹ à¤¸à¤®à¤¯à¤•à¥‹ à¤ªà¤žà¥à¤šà¤¾à¤™à¥à¤—à¤²à¥‡ à¤…à¤µà¤¤à¤¾à¤°à¤•à¥‹ à¤•à¥à¤·à¤£à¤®à¤¾ à¤¸à¤•à¥à¤°à¤¿à¤¯ à¤¬à¥à¤°à¤¹à¥à¤®à¤¾à¤£à¥à¤¡à¥€à¤¯ à¤ªà¥à¤°à¤­à¤¾à¤µà¤¹à¤°à¥‚ à¤ªà¥à¤°à¤•à¤Ÿ à¤—à¤°à¥à¤¦à¤›, à¤µà¥à¤¯à¤•à¥à¤¤à¤¿à¤•à¥‹ à¤¸à¥à¤µà¤­à¤¾à¤µà¤¿à¤• à¤ªà¥à¤°à¤•à¥ƒà¤¤à¤¿, à¤ªà¥à¤°à¤µà¥ƒà¤¤à¥à¤¤à¤¿ à¤° à¤œà¥€à¤µà¤¨ à¤¢à¤¾à¤à¤šà¤¾à¤®à¤¾ à¤…à¤¨à¥à¤¤à¤°à¥à¤¦à¥ƒà¤·à¥à¤Ÿà¤¿ à¤ªà¥à¤°à¤¦à¤¾à¤¨ à¤—à¤°à¥à¤¦à¤›à¥¤"),

    // Panchanga Info Card Element Titles (with Sanskrit)
    PANCHANGA_INFO_TITHI_TITLE("Tithi (à¤¤à¤¿à¤¥à¤¿)", "à¤¤à¤¿à¤¥à¤¿ (à¤¤à¤¿à¤¥à¤¿)"),
    PANCHANGA_INFO_NAKSHATRA_TITLE("Nakshatra (à¤¨à¤•à¥à¤·à¤¤à¥à¤°)", "à¤¨à¤•à¥à¤·à¤¤à¥à¤° (à¤¨à¤•à¥à¤·à¤¤à¥à¤°)"),
    PANCHANGA_INFO_YOGA_TITLE("Yoga (à¤¯à¥‹à¤—)", "à¤¯à¥‹à¤— (à¤¯à¥‹à¤—)"),
    PANCHANGA_INFO_KARANA_TITLE("Karana (à¤•à¤°à¤£)", "à¤•à¤°à¤£ (à¤•à¤°à¤£)"),
    PANCHANGA_INFO_VARA_TITLE("Vara (à¤µà¤¾à¤°)", "à¤µà¤¾à¤° (à¤µà¤¾à¤°)"),
    PANCHANGA_INFO_TITHI_LABEL("Lunar Day", "à¤šà¤¨à¥à¤¦à¥à¤° à¤¦à¤¿à¤¨"),
    PANCHANGA_INFO_NAKSHATRA_LABEL("Lunar Mansion", "à¤šà¤¨à¥à¤¦à¥à¤° à¤¨à¤•à¥à¤·à¤¤à¥à¤°"),
    PANCHANGA_INFO_YOGA_LABEL("Luni-Solar Combination", "à¤šà¤¨à¥à¤¦à¥à¤°-à¤¸à¥‚à¤°à¥à¤¯ à¤¸à¤‚à¤¯à¥‹à¤œà¤¨"),
    PANCHANGA_INFO_KARANA_LABEL("Half Tithi", "à¤…à¤°à¥à¤§ à¤¤à¤¿à¤¥à¤¿"),
    PANCHANGA_INFO_VARA_LABEL("Weekday", "à¤µà¤¾à¤°"),

    // Quality Indicators
    QUALITY_EXCELLENT("Excellent", "à¤‰à¤¤à¥à¤•à¥ƒà¤·à¥à¤Ÿ"),
    QUALITY_GOOD("Good", "à¤°à¤¾à¤®à¥à¤°à¥‹"),
    QUALITY_NEUTRAL("Neutral", "à¤¤à¤Ÿà¤¸à¥à¤¥"),
    QUALITY_CHALLENGING("Challenging", "à¤šà¥à¤¨à¥Œà¤¤à¥€à¤ªà¥‚à¤°à¥à¤£"),
    QUALITY_INAUSPICIOUS("Inauspicious", "à¤…à¤¶à¥à¤­"),

    // ============================================
    // CHART TAB - LEGEND & UI STRINGS
    // ============================================
    CHART_LEGEND_RETRO("Retro", "à¤µà¤•à¥à¤°à¥€"),
    CHART_LEGEND_COMBUST("Combust", "à¤…à¤¸à¥à¤¤"),
    CHART_LEGEND_VARGOTTAMA("Vargottama", "à¤µà¤°à¥à¤—à¥‹à¤¤à¥à¤¤à¤®"),
    CHART_LEGEND_EXALTED("Exalted", "à¤‰à¤šà¥à¤š"),
    CHART_LEGEND_DEBILITATED("Debilitated", "à¤¨à¥€à¤š"),
    CHART_LEGEND_OWN_SIGN("Own Sign", "à¤¸à¥à¤µà¤°à¤¾à¤¶à¤¿"),
    CHART_LEGEND_MOOL_TRI("Mool Tri.", "à¤®à¥‚à¤²à¤¤à¥à¤°à¤¿."),
    CHART_BIRTH_DETAILS("Birth Details", "à¤œà¤¨à¥à¤® à¤µà¤¿à¤µà¤°à¤£"),
    CHART_PLANETARY_POSITIONS("Planetary Positions", "à¤—à¥à¤°à¤¹ à¤¸à¥à¤¥à¤¿à¤¤à¤¿à¤¹à¤°à¥‚"),
    CHART_TAP_FOR_DETAILS("Tap for details", "à¤µà¤¿à¤µà¤°à¤£à¤•à¥‹ à¤²à¤¾à¤—à¤¿ à¤Ÿà¥à¤¯à¤¾à¤ª à¤—à¤°à¥à¤¨à¥à¤¹à¥‹à¤¸à¥"),
    CHART_TAP_TO_EXPAND("Tap to expand", "à¤µà¤¿à¤¸à¥à¤¤à¤¾à¤° à¤—à¤°à¥à¤¨ à¤Ÿà¥à¤¯à¤¾à¤ª à¤—à¤°à¥à¤¨à¥à¤¹à¥‹à¤¸à¥"),
    CHART_TAP_HOUSE_FOR_DETAILS("Tap house for details", "à¤µà¤¿à¤µà¤°à¤£à¤•à¥‹ à¤²à¤¾à¤—à¤¿ à¤­à¤¾à¤µ à¤Ÿà¥à¤¯à¤¾à¤ª à¤—à¤°à¥à¤¨à¥à¤¹à¥‹à¤¸à¥"),
    CHART_ASCENDANT_LAGNA("Ascendant (Lagna)", "à¤²à¤—à¥à¤¨"),
    CHART_HOUSE_CUSPS("House Cusps", "à¤­à¤¾à¤µ à¤¸à¤¨à¥à¤§à¤¿"),
    CHART_ASTRONOMICAL_DATA("Astronomical Data", "à¤–à¤—à¥‹à¤²à¥€à¤¯ à¤¤à¤¥à¥à¤¯à¤¾à¤™à¥à¤•"),
    CHART_JULIAN_DAY("Julian Day", "à¤œà¥à¤²à¤¿à¤¯à¤¨ à¤¦à¤¿à¤¨"),
    CHART_MIDHEAVEN("Midheaven", "à¤®à¤§à¥à¤¯à¤¾à¤•à¤¾à¤¶"),
    CHART_HOUSE_SYSTEM("House System", "à¤­à¤¾à¤µ à¤ªà¤¦à¥à¤§à¤¤à¤¿"),
    CHART_TAP_FULLSCREEN("Tap chart to view fullscreen", "à¤ªà¥‚à¤°à¥à¤£ à¤¸à¥à¤•à¥à¤°à¤¿à¤¨à¤®à¤¾ à¤¹à¥‡à¤°à¥à¤¨ à¤šà¤¾à¤°à¥à¤Ÿ à¤Ÿà¥à¤¯à¤¾à¤ª à¤—à¤°à¥à¤¨à¥à¤¹à¥‹à¤¸à¥"),
    CHART_DATE("Date", "à¤®à¤¿à¤¤à¤¿"),
    CHART_TIME("Time", "à¤¸à¤®à¤¯"),
    CHART_AYANAMSA("Ayanamsa", "à¤…à¤¯à¤¨à¤¾à¤‚à¤¶"),
    CHART_LOCATION("Location", "à¤¸à¥à¤¥à¤¾à¤¨"),

    // Common Birth Data Labels
    HOUSE("House", "à¤­à¤¾à¤µ"),
    LOCATION("Location", "à¤¸à¥à¤¥à¤¾à¤¨"),
    ASCENDANT("Ascendant", "à¤²à¤—à¥à¤¨"),

    // Chart Type Labels
    CHART_LAGNA("Lagna", "à¤²à¤—à¥à¤¨"),
    CHART_RASHI("Rashi Chart (D1)", "à¤°à¤¾à¤¶à¤¿ à¤•à¥à¤£à¥à¤¡à¤²à¥€ (D1)"),
    CHART_NAVAMSA("Navamsa Chart (D9)", "à¤¨à¤µà¤¾à¤‚à¤¶ à¤•à¥à¤£à¥à¤¡à¤²à¥€ (D9)"),

    // ============================================
    // PLANET DIALOG - SECTION HEADERS
    // ============================================
    DIALOG_POSITION_DETAILS("Position Details", "à¤¸à¥à¤¥à¤¿à¤¤à¤¿ à¤µà¤¿à¤µà¤°à¤£"),
    DIALOG_ZODIAC_SIGN("Zodiac Sign", "à¤°à¤¾à¤¶à¤¿"),
    DIALOG_DEGREE("Degree", "à¤…à¤‚à¤¶"),
    DIALOG_HOUSE("House", "à¤­à¤¾à¤µ"),
    DIALOG_NAKSHATRA("Nakshatra", "à¤¨à¤•à¥à¤·à¤¤à¥à¤°"),
    DIALOG_NAKSHATRA_LORD("Nakshatra Lord", "à¤¨à¤•à¥à¤·à¤¤à¥à¤° à¤¸à¥à¤µà¤¾à¤®à¥€"),
    DIALOG_NAKSHATRA_DEITY("Nakshatra Deity", "à¤¨à¤•à¥à¤·à¤¤à¥à¤° à¤¦à¥‡à¤µà¤¤à¤¾"),
    DIALOG_MOTION("Motion", "à¤—à¤¤à¤¿"),
    DIALOG_RETROGRADE("Retrograde", "à¤µà¤•à¥à¤°à¥€"),
    DIALOG_STRENGTH_ANALYSIS("Strength Analysis (Shadbala)", "à¤¬à¤² à¤µà¤¿à¤¶à¥à¤²à¥‡à¤·à¤£ (à¤·à¤¡à¥à¤¬à¤²)"),
    DIALOG_STRENGTH_BREAKDOWN("Strength Breakdown (Virupas)", "à¤¬à¤² à¤µà¤¿à¤­à¤¾à¤œà¤¨ (à¤µà¤¿à¤°à¥à¤ªà¤¾)"),
    DIALOG_STHANA_BALA("Sthana Bala (Positional)", "à¤¸à¥à¤¥à¤¾à¤¨ à¤¬à¤²"),
    DIALOG_DIG_BALA("Dig Bala (Directional)", "à¤¦à¤¿à¤—à¥à¤¬à¤²"),
    DIALOG_KALA_BALA("Kala Bala (Temporal)", "à¤•à¤¾à¤² à¤¬à¤²"),
    DIALOG_CHESTA_BALA("Chesta Bala (Motional)", "à¤šà¥‡à¤·à¥à¤Ÿà¤¾ à¤¬à¤²"),
    DIALOG_NAISARGIKA_BALA("Naisargika Bala (Natural)", "à¤¨à¥ˆà¤¸à¤°à¥à¤—à¤¿à¤• à¤¬à¤²"),
    DIALOG_DRIK_BALA("Drik Bala (Aspectual)", "à¤¦à¥ƒà¤—à¥à¤¬à¤²"),
    DIALOG_BENEFIC("Benefic", "à¤¶à¥à¤­"),
    DIALOG_MALEFIC("Malefic", "à¤ªà¤¾à¤ªà¥€"),
    DIALOG_ELEMENT("Element", "à¤¤à¤¤à¥à¤µ"),
    DIALOG_REPRESENTS("Represents:", "à¤ªà¥à¤°à¤¤à¤¿à¤¨à¤¿à¤§à¤¿à¤¤à¥à¤µ:"),
    DIALOG_BODY_PARTS("Body Parts:", "à¤¶à¤°à¥€à¤° à¤…à¤‚à¤—:"),
    DIALOG_PROFESSIONS("Professions:", "à¤ªà¥‡à¤¶à¤¾à¤¹à¤°à¥‚:"),
    DIALOG_HOUSE_PLACEMENT("House %d Placement", "à¤­à¤¾à¤µ %d à¤¸à¥à¤¥à¤¿à¤¤à¤¿"),
    DIALOG_STATUS_CONDITIONS("Status & Conditions", "à¤¸à¥à¤¥à¤¿à¤¤à¤¿ à¤° à¤…à¤µà¤¸à¥à¤¥à¤¾"),
    DIALOG_DIGNITY("Dignity", "à¤®à¤°à¥à¤¯à¤¾à¤¦à¤¾"),
    DIALOG_COMBUSTION("Combustion", "à¤…à¤¸à¥à¤¤"),
    COMBUSTION_NOT_COMBUST("Not Combust", "à¤…à¤¸à¥à¤¤ à¤›à¥ˆà¤¨"),
    COMBUSTION_APPROACHING("Approaching Combustion", "à¤…à¤¸à¥à¤¤ à¤¨à¤œà¤¿à¤•"),
    COMBUSTION_COMBUST("Combust", "à¤…à¤¸à¥à¤¤"),
    COMBUSTION_DEEP_COMBUST("Deep Combustion", "à¤—à¤¹à¤¿à¤°à¥‹ à¤…à¤¸à¥à¤¤"),
    COMBUSTION_CAZIMI("Cazimi", "à¤•à¤¾à¤œà¤¿à¤®à¥€"),
    COMBUSTION_SEPARATING("Separating", "à¤µà¤¿à¤­à¤¾à¤œà¤¨"),
    DIALOG_PLANETARY_WAR("Planetary War", "à¤—à¥à¤°à¤¹à¤¯à¥à¤¦à¥à¤§"),
    DIALOG_AT_WAR_WITH("At war with %s", "%s à¤¸à¤à¤— à¤¯à¥à¤¦à¥à¤§à¤®à¤¾"),
    DIALOG_INSIGHTS_PREDICTIONS("Insights & Predictions", "à¤…à¤¨à¥à¤¤à¤°à¥à¤¦à¥ƒà¤·à¥à¤Ÿà¤¿ à¤° à¤­à¤µà¤¿à¤·à¥à¤¯à¤µà¤¾à¤£à¥€"),
    DIALOG_OVERALL("Overall: %s / %s Rupas", "à¤¸à¤®à¤—à¥à¤°: %s / %s à¤°à¥à¤ªà¤¾"),
    DIALOG_REQUIRED_STRENGTH("Required", "à¤†à¤µà¤¶à¥à¤¯à¤•"),
    DIALOG_PERCENT_OF_REQUIRED("%s%% of required strength", "à¤†à¤µà¤¶à¥à¤¯à¤• à¤¬à¤²à¤•à¥‹ %s%%"),
    DIALOG_TOTAL_VIRUPAS("Total Virupas", "à¤•à¥à¤² à¤µà¤¿à¤°à¥à¤ªà¤¾"),

    // House Names
    HOUSE_1_NAME("First House (Lagna)", "à¤ªà¤¹à¤¿à¤²à¥‹ à¤­à¤¾à¤µ (à¤²à¤—à¥à¤¨)"),
    HOUSE_2_NAME("Second House (Dhana)", "à¤¦à¥‹à¤¸à¥à¤°à¥‹ à¤­à¤¾à¤µ (à¤§à¤¨)"),
    HOUSE_3_NAME("Third House (Sahaja)", "à¤¤à¥‡à¤¸à¥à¤°à¥‹ à¤­à¤¾à¤µ (à¤¸à¤¹à¤œ)"),
    HOUSE_4_NAME("Fourth House (Sukha)", "à¤šà¥Œà¤¥à¥‹ à¤­à¤¾à¤µ (à¤¸à¥à¤–)"),
    HOUSE_5_NAME("Fifth House (Putra)", "à¤ªà¤¾à¤à¤šà¥Œà¤‚ à¤­à¤¾à¤µ (à¤ªà¥à¤¤à¥à¤°)"),
    HOUSE_6_NAME("Sixth House (Ripu)", "à¤›à¥ˆà¤Ÿà¥Œà¤‚ à¤­à¤¾à¤µ (à¤°à¤¿à¤ªà¥)"),
    HOUSE_7_NAME("Seventh House (Kalatra)", "à¤¸à¤¾à¤¤à¥Œà¤‚ à¤­à¤¾à¤µ (à¤•à¤²à¤¤à¥à¤°)"),
    HOUSE_8_NAME("Eighth House (Ayur)", "à¤†à¤ à¥Œà¤‚ à¤­à¤¾à¤µ (à¤†à¤¯à¥)"),
    HOUSE_9_NAME("Ninth House (Dharma)", "à¤¨à¤µà¥Œà¤‚ à¤­à¤¾à¤µ (à¤§à¤°à¥à¤®)"),
    HOUSE_10_NAME("Tenth House (Karma)", "à¤¦à¤¸à¥Œà¤‚ à¤­à¤¾à¤µ (à¤•à¤°à¥à¤®)"),
    HOUSE_11_NAME("Eleventh House (Labha)", "à¤à¤˜à¤¾à¤°à¥Œà¤‚ à¤­à¤¾à¤µ (à¤²à¤¾à¤­)"),
    HOUSE_12_NAME("Twelfth House (Vyaya)", "à¤¬à¤¾à¤¹à¥à¤°à¥Œà¤‚ à¤­à¤¾à¤µ (à¤µà¥à¤¯à¤¯)"),

    // House Signification Descriptions
    HOUSE_1_SIG("Self, Body, Personality", "à¤†à¤¤à¥à¤®, à¤¶à¤°à¥€à¤°, à¤µà¥à¤¯à¤•à¥à¤¤à¤¿à¤¤à¥à¤µ"),
    HOUSE_2_SIG("Wealth, Family, Speech", "à¤§à¤¨, à¤ªà¤°à¤¿à¤µà¤¾à¤°, à¤µà¤¾à¤£à¥€"),
    HOUSE_3_SIG("Siblings, Courage, Communication", "à¤­à¤¾à¤‡à¤¬à¤¹à¤¿à¤¨à¥€, à¤¸à¤¾à¤¹à¤¸, à¤¸à¤žà¥à¤šà¤¾à¤°"),
    HOUSE_4_SIG("Home, Mother, Happiness", "à¤˜à¤°, à¤†à¤®à¤¾, à¤¸à¥à¤–"),
    HOUSE_5_SIG("Children, Intelligence, Romance", "à¤¸à¤¨à¥à¤¤à¤¾à¤¨, à¤¬à¥à¤¦à¥à¤§à¤¿, à¤ªà¥à¤°à¥‡à¤®"),
    HOUSE_6_SIG("Enemies, Health, Service", "à¤¶à¤¤à¥à¤°à¥, à¤¸à¥à¤µà¤¾à¤¸à¥à¤¥à¥à¤¯, à¤¸à¥‡à¤µà¤¾"),
    HOUSE_7_SIG("Marriage, Partnerships, Business", "à¤µà¤¿à¤µà¤¾à¤¹, à¤¸à¤¾à¤à¥‡à¤¦à¤¾à¤°à¥€, à¤µà¥à¤¯à¤¾à¤ªà¤¾à¤°"),
    HOUSE_8_SIG("Longevity, Transformation, Occult", "à¤†à¤¯à¥, à¤°à¥‚à¤ªà¤¾à¤¨à¥à¤¤à¤°à¤£, à¤¤à¤¨à¥à¤¤à¥à¤°"),
    HOUSE_9_SIG("Fortune, Dharma, Father", "à¤­à¤¾à¤—à¥à¤¯, à¤§à¤°à¥à¤®, à¤ªà¤¿à¤¤à¤¾"),
    HOUSE_10_SIG("Career, Status, Public Image", "à¤•à¥à¤¯à¤¾à¤°à¤¿à¤¯à¤°, à¤ªà¤¦, à¤¸à¤¾à¤°à¥à¤µà¤œà¤¨à¤¿à¤• à¤›à¤µà¤¿"),
    HOUSE_11_SIG("Gains, Income, Desires", "à¤²à¤¾à¤­, à¤†à¤¯, à¤‡à¤šà¥à¤›à¤¾à¤¹à¤°à¥‚"),
    HOUSE_12_SIG("Losses, Expenses, Liberation", "à¤¹à¤¾à¤¨à¤¿, à¤–à¤°à¥à¤š, à¤®à¥‹à¤•à¥à¤·"),

    // House Types
    HOUSE_TYPE_KENDRA("Kendra (Angular)", "à¤•à¥‡à¤¨à¥à¤¦à¥à¤°"),
    HOUSE_TYPE_TRIKONA("Trikona (Trine)", "à¤¤à¥à¤°à¤¿à¤•à¥‹à¤£"),
    HOUSE_TYPE_DUSTHANA("Dusthana (Malefic)", "à¤¦à¥à¤ƒà¤¸à¥à¤¥à¤¾à¤¨"),
    HOUSE_TYPE_UPACHAYA("Upachaya (Growth)", "à¤‰à¤ªà¤šà¤¯"),
    HOUSE_TYPE_MARAKA("Maraka (Death-inflicting)", "à¤®à¤¾à¤°à¤•"),
    HOUSE_TYPE_PANAPARA("Panapara", "à¤ªà¤£à¤«à¤°"),
    HOUSE_TYPE_APOKLIMA("Apoklima", "à¤†à¤ªà¥‹à¤•à¥à¤²à¤¿à¤®"),

    // Dialog Buttons
    DIALOG_CLOSE("Close", "à¤¬à¤¨à¥à¤¦ à¤—à¤°à¥à¤¨à¥à¤¹à¥‹à¤¸à¥"),
    DIALOG_RESET("Reset", "à¤°à¤¿à¤¸à¥‡à¤Ÿ"),
    DIALOG_ZOOM_IN("Zoom In", "à¤ à¥‚à¤²à¥‹ à¤ªà¤¾à¤°à¥à¤¨à¥à¤¹à¥‹à¤¸à¥"),
    DIALOG_ZOOM_OUT("Zoom Out", "à¤¸à¤¾à¤¨à¥‹ à¤ªà¤¾à¤°à¥à¤¨à¥à¤¹à¥‹à¤¸à¥"),
    DIALOG_DOWNLOAD("Download", "à¤¡à¤¾à¤‰à¤¨à¤²à¥‹à¤¡"),
    DIALOG_SAVING("Saving...", "à¤¸à¥‡à¤­ à¤—à¤°à¥à¤¦à¥ˆ..."),
    DIALOG_CHART_SAVED("Chart saved to gallery!", "à¤šà¤¾à¤°à¥à¤Ÿ à¤—à¥à¤¯à¤¾à¤²à¥‡à¤°à¥€à¤®à¤¾ à¤¸à¥‡à¤­ à¤­à¤¯à¥‹!"),
    DIALOG_SAVE_FAILED("Failed to save chart", "à¤šà¤¾à¤°à¥à¤Ÿ à¤¸à¥‡à¤­ à¤—à¤°à¥à¤¨ à¤…à¤¸à¤«à¤²"),

    // Nakshatra Dialog
    DIALOG_BASIC_INFO("Basic Information", "à¤†à¤§à¤¾à¤°à¤­à¥‚à¤¤ à¤œà¤¾à¤¨à¤•à¤¾à¤°à¥€"),
    DIALOG_NAKSHATRA_NATURE("Nakshatra Nature", "à¤¨à¤•à¥à¤·à¤¤à¥à¤° à¤ªà¥à¤°à¤•à¥ƒà¤¤à¤¿"),
    DIALOG_PADA_CHARACTERISTICS("Pada %d Characteristics", "à¤ªà¤¾à¤¦ %d à¤µà¤¿à¤¶à¥‡à¤·à¤¤à¤¾à¤¹à¤°à¥‚"),
    DIALOG_GENERAL_CHARACTERISTICS("General Characteristics", "à¤¸à¤¾à¤®à¤¾à¤¨à¥à¤¯ à¤µà¤¿à¤¶à¥‡à¤·à¤¤à¤¾à¤¹à¤°à¥‚"),
    DIALOG_CAREER_INDICATIONS("Career Indications", "à¤•à¥à¤¯à¤¾à¤°à¤¿à¤¯à¤° à¤¸à¤‚à¤•à¥‡à¤¤à¤¹à¤°à¥‚"),
    DIALOG_NUMBER("Number", "à¤•à¥à¤°à¤®à¤¾à¤‚à¤•"),
    DIALOG_DEGREE_RANGE("Degree Range", "à¤…à¤‚à¤¶ à¤¦à¤¾à¤¯à¤°à¤¾"),

    DIALOG_NAVAMSA_SIGN("Navamsa Sign", "à¤¨à¤µà¤¾à¤‚à¤¶ à¤°à¤¾à¤¶à¤¿"),
    DIALOG_GENDER("Gender", "à¤²à¤¿à¤™à¥à¤—"),

    // House Dialog
    DIALOG_HOUSE_INFO("House Information", "à¤­à¤¾à¤µ à¤œà¤¾à¤¨à¤•à¤¾à¤°à¥€"),
    DIALOG_SIGNIFICATIONS("Significations & Nature", "à¤•à¤°à¤•à¤¤à¥à¤µ à¤° à¤ªà¥à¤°à¤•à¥ƒà¤¤à¤¿"),
    DIALOG_NATURE("Nature", "à¤ªà¥à¤°à¤•à¥ƒà¤¤à¤¿"),
    DIALOG_PLANETS_IN_HOUSE("Planets in House", "à¤­à¤¾à¤µà¤®à¤¾ à¤—à¥à¤°à¤¹à¤¹à¤°à¥‚"),
    DIALOG_DETAILED_INTERPRETATION("Detailed Interpretation", "à¤µà¤¿à¤¸à¥à¤¤à¥ƒà¤¤ à¤µà¥à¤¯à¤¾à¤–à¥à¤¯à¤¾"),
    DIALOG_CUSP_DEGREE("Cusp Degree", "à¤¸à¤¨à¥à¤§à¤¿ à¤…à¤‚à¤¶"),
    DIALOG_SIGN_LORD("Sign Lord", "à¤°à¤¾à¤¶à¤¿ à¤¸à¥à¤µà¤¾à¤®à¥€"),
    DIALOG_HOUSE_TYPE("House Type", "à¤­à¤¾à¤µ à¤ªà¥à¤°à¤•à¤¾à¤°"),

    // Shadbala Dialog
    DIALOG_SHADBALA_ANALYSIS("Shadbala Analysis", "à¤·à¤¡à¥à¤¬à¤² à¤µà¤¿à¤¶à¥à¤²à¥‡à¤·à¤£"),
    DIALOG_SIXFOLD_STRENGTH("Six-fold Planetary Strength", "à¤›à¤µà¤Ÿà¤¾ à¤—à¥à¤°à¤¹ à¤¬à¤²"),
    DIALOG_OVERALL_SUMMARY("Overall Summary", "à¤¸à¤®à¤—à¥à¤° à¤¸à¤¾à¤°à¤¾à¤‚à¤¶"),
    DIALOG_CHART_STRENGTH("Chart Strength", "à¤•à¥à¤£à¥à¤¡à¤²à¥€ à¤¬à¤²"),
    DIALOG_STRONGEST("Strongest", "à¤¸à¤¬à¥ˆà¤­à¤¨à¥à¤¦à¤¾ à¤¬à¤²à¤¿à¤¯à¥‹"),
    DIALOG_WEAKEST("Weakest", "à¤¸à¤¬à¥ˆà¤­à¤¨à¥à¤¦à¤¾ à¤•à¤®à¤œà¥‹à¤°"),

    // ============================================
    // DEBUG ACTIVITY - ERROR SCREEN
    // ============================================
    DEBUG_UNHANDLED_EXCEPTION("Unhandled Exception", "à¤…à¤ªà¥à¤°à¤¤à¥à¤¯à¤¾à¤¶à¤¿à¤¤ à¤¤à¥à¤°à¥à¤Ÿà¤¿"),
    DEBUG_ERROR_OCCURRED("An unexpected error occurred.", "à¤à¤‰à¤Ÿà¤¾ à¤…à¤ªà¥à¤°à¤¤à¥à¤¯à¤¾à¤¶à¤¿à¤¤ à¤¤à¥à¤°à¥à¤Ÿà¤¿ à¤­à¤¯à¥‹à¥¤"),
    DEBUG_COPY_LOG("Copy Log", "à¤²à¤— à¤•à¤ªà¥€ à¤—à¤°à¥à¤¨à¥à¤¹à¥‹à¤¸à¥"),
    DEBUG_RESTART_APP("Restart App", "à¤à¤ª à¤ªà¥à¤¨à¤ƒ à¤¸à¥à¤°à¥ à¤—à¤°à¥à¤¨à¥à¤¹à¥‹à¤¸à¥"),
    DEBUG_CRASH_LOG("Crash Log", "à¤•à¥à¤°à¥à¤¯à¤¾à¤¸ à¤²à¤—"),

    // ============================================
    // CHART EXPORTER - PDF STRINGS
    // ============================================
    EXPORT_VEDIC_REPORT("VEDIC BIRTH CHART REPORT", "à¤µà¥ˆà¤¦à¤¿à¤• à¤œà¤¨à¥à¤® à¤•à¥à¤£à¥à¤¡à¤²à¥€ à¤°à¤¿à¤ªà¥‹à¤°à¥à¤Ÿ"),
    EXPORT_NAME("Name:", "à¤¨à¤¾à¤®:"),
    EXPORT_DATE_TIME("Date & Time:", "à¤®à¤¿à¤¤à¤¿ à¤° à¤¸à¤®à¤¯:"),
    EXPORT_LOCATION("Location:", "à¤¸à¥à¤¥à¤¾à¤¨:"),
    EXPORT_COORDINATES("Coordinates:", "à¤¨à¤¿à¤°à¥à¤¦à¥‡à¤¶à¤¾à¤‚à¤•:"),
    EXPORT_PLANETARY_POSITIONS("PLANETARY POSITIONS", "à¤—à¥à¤°à¤¹ à¤¸à¥à¤¥à¤¿à¤¤à¤¿à¤¹à¤°à¥‚"),
    EXPORT_ASTRONOMICAL_DATA("ASTRONOMICAL DATA", "à¤–à¤—à¥‹à¤²à¥€à¤¯ à¤¤à¤¥à¥à¤¯à¤¾à¤™à¥à¤•"),
    EXPORT_HOUSE_CUSPS("HOUSE CUSPS", "à¤­à¤¾à¤µ à¤¸à¤¨à¥à¤§à¤¿à¤¹à¤°à¥‚"),
    EXPORT_YOGA_ANALYSIS("YOGA ANALYSIS", "à¤¯à¥‹à¤— à¤µà¤¿à¤¶à¥à¤²à¥‡à¤·à¤£"),
    EXPORT_TOTAL_YOGAS("Total Yogas Found:", "à¤•à¥à¤² à¤¯à¥‹à¤—à¤¹à¤°à¥‚ à¤«à¥‡à¤²à¤¾ à¤ªà¤°à¥‡à¤•à¥‹:"),
    EXPORT_OVERALL_YOGA_STRENGTH("Overall Yoga Strength:", "à¤¸à¤®à¤—à¥à¤° à¤¯à¥‹à¤— à¤¬à¤²:"),
    EXPORT_KEY_YOGAS("Key Yogas:", "à¤ªà¥à¤°à¤®à¥à¤– à¤¯à¥‹à¤—à¤¹à¤°à¥‚:"),
    EXPORT_CHALLENGING_YOGAS("Challenging Yogas:", "à¤šà¥à¤¨à¥Œà¤¤à¥€à¤ªà¥‚à¤°à¥à¤£ à¤¯à¥‹à¤—à¤¹à¤°à¥‚:"),
    EXPORT_MITIGATED_BY("Mitigated by:", "à¤¨à¥à¤¯à¥‚à¤¨à¥€à¤•à¤°à¤£:"),
    EXPORT_PLANETARY_ASPECTS("PLANETARY ASPECTS", "à¤—à¥à¤°à¤¹ à¤¦à¥ƒà¤·à¥à¤Ÿà¤¿à¤¹à¤°à¥‚"),
    EXPORT_SHADBALA_ANALYSIS("SHADBALA ANALYSIS", "à¤·à¤¡à¥à¤¬à¤² à¤µà¤¿à¤¶à¥à¤²à¥‡à¤·à¤£"),
    EXPORT_OVERALL_CHART_STRENGTH("Overall Chart Strength:", "à¤¸à¤®à¤—à¥à¤° à¤•à¥à¤£à¥à¤¡à¤²à¥€ à¤¬à¤²:"),
    EXPORT_STRONGEST_PLANET("Strongest Planet:", "à¤¸à¤¬à¥ˆà¤­à¤¨à¥à¤¦à¤¾ à¤¬à¤²à¤¿à¤¯à¥‹ à¤—à¥à¤°à¤¹:"),
    EXPORT_WEAKEST_PLANET("Weakest Planet:", "à¤¸à¤¬à¥ˆà¤­à¤¨à¥à¤¦à¤¾ à¤•à¤®à¤œà¥‹à¤° à¤—à¥à¤°à¤¹:"),
    EXPORT_STRENGTH_BREAKDOWN("Strength Breakdown:", "à¤¬à¤² à¤µà¤¿à¤­à¤¾à¤œà¤¨:"),
    EXPORT_ASHTAKAVARGA_ANALYSIS("ASHTAKAVARGA ANALYSIS", "à¤…à¤·à¥à¤Ÿà¤•à¤µà¤°à¥à¤— à¤µà¤¿à¤¶à¥à¤²à¥‡à¤·à¤£"),
    EXPORT_SARVASHTAKAVARGA("Sarvashtakavarga (Combined Strength)", "à¤¸à¤°à¥à¤µà¤¾à¤·à¥à¤Ÿà¤•à¤µà¤°à¥à¤— (à¤¸à¤‚à¤¯à¥à¤•à¥à¤¤ à¤¬à¤²)"),
    EXPORT_BHINNASHTAKAVARGA("Bhinnashtakavarga (Individual Planet Bindus)", "à¤­à¤¿à¤¨à¥à¤¨à¤¾à¤·à¥à¤Ÿà¤•à¤µà¤°à¥à¤— (à¤µà¥à¤¯à¤•à¥à¤¤à¤¿à¤—à¤¤ à¤—à¥à¤°à¤¹ à¤¬à¤¿à¤¨à¥à¤¦à¥)"),
    EXPORT_TRANSIT_GUIDE("Transit Interpretation Guide:", "à¤—à¥‹à¤šà¤° à¤µà¥à¤¯à¤¾à¤–à¥à¤¯à¤¾ à¤—à¤¾à¤‡à¤¡:"),
    EXPORT_SAV_EXCELLENT("SAV 30+ bindus: Excellent for transits - major positive events", "SAV à¥©à¥¦+ à¤¬à¤¿à¤¨à¥à¤¦à¥: à¤—à¥‹à¤šà¤°à¤•à¥‹ à¤²à¤¾à¤—à¤¿ à¤‰à¤¤à¥à¤•à¥ƒà¤·à¥à¤Ÿ - à¤ªà¥à¤°à¤®à¥à¤– à¤¸à¤•à¤¾à¤°à¤¾à¤¤à¥à¤®à¤• à¤˜à¤Ÿà¤¨à¤¾à¤¹à¤°à¥‚"),
    EXPORT_SAV_GOOD("SAV 28-29 bindus: Good for transits - favorable outcomes", "SAV à¥¨à¥®-à¥¨à¥¯ à¤¬à¤¿à¤¨à¥à¤¦à¥: à¤—à¥‹à¤šà¤°à¤•à¥‹ à¤²à¤¾à¤—à¤¿ à¤°à¤¾à¤®à¥à¤°à¥‹ - à¤…à¤¨à¥à¤•à¥‚à¤² à¤ªà¤°à¤¿à¤£à¤¾à¤®à¤¹à¤°à¥‚"),
    EXPORT_SAV_AVERAGE("SAV 25-27 bindus: Average - mixed results", "SAV à¥¨à¥«-à¥¨à¥­ à¤¬à¤¿à¤¨à¥à¤¦à¥: à¤”à¤¸à¤¤ - à¤®à¤¿à¤¶à¥à¤°à¤¿à¤¤ à¤ªà¤°à¤¿à¤£à¤¾à¤®à¤¹à¤°à¥‚"),
    EXPORT_SAV_CHALLENGING("SAV below 25: Challenging - caution advised during transits", "SAV à¥¨à¥« à¤­à¤¨à¥à¤¦à¤¾ à¤•à¤®: à¤šà¥à¤¨à¥Œà¤¤à¥€à¤ªà¥‚à¤°à¥à¤£ - à¤—à¥‹à¤šà¤°à¤®à¤¾ à¤¸à¤¾à¤µà¤§à¤¾à¤¨à¥€ à¤†à¤µà¤¶à¥à¤¯à¤•"),
    EXPORT_BAV_EXCELLENT("BAV 5+ bindus: Planet transit through this sign is highly beneficial", "BAV à¥«+ à¤¬à¤¿à¤¨à¥à¤¦à¥: à¤¯à¤¸ à¤°à¤¾à¤¶à¤¿à¤®à¤¾ à¤—à¥à¤°à¤¹ à¤—à¥‹à¤šà¤° à¤…à¤¤à¥à¤¯à¤¨à¥à¤¤ à¤²à¤¾à¤­à¤¦à¤¾à¤¯à¤•"),
    EXPORT_BAV_GOOD("BAV 4 bindus: Good results from planet transit", "BAV à¥ª à¤¬à¤¿à¤¨à¥à¤¦à¥: à¤—à¥à¤°à¤¹ à¤—à¥‹à¤šà¤°à¤¬à¤¾à¤Ÿ à¤°à¤¾à¤®à¥à¤°à¥‹ à¤ªà¤°à¤¿à¤£à¤¾à¤®à¤¹à¤°à¥‚"),
    EXPORT_BAV_AVERAGE("BAV 3 bindus: Average results", "BAV à¥© à¤¬à¤¿à¤¨à¥à¤¦à¥: à¤”à¤¸à¤¤ à¤ªà¤°à¤¿à¤£à¤¾à¤®à¤¹à¤°à¥‚"),
    EXPORT_BAV_CHALLENGING("BAV 0-2 bindus: Difficult transit period for that planet", "BAV à¥¦-à¥¨ à¤¬à¤¿à¤¨à¥à¤¦à¥: à¤¤à¥à¤¯à¥‹ à¤—à¥à¤°à¤¹à¤•à¥‹ à¤²à¤¾à¤—à¤¿ à¤•à¤ à¤¿à¤¨ à¤—à¥‹à¤šà¤° à¤…à¤µà¤§à¤¿"),
    EXPORT_GENERATED_BY("Generated by AstroStorm - Ultra-Precision Vedic Astrology", "AstroStorm à¤¦à¥à¤µà¤¾à¤°à¤¾ à¤‰à¤¤à¥à¤ªà¤¨à¥à¤¨ - à¤…à¤¤à¤¿-à¤¸à¤Ÿà¥€à¤• à¤µà¥ˆà¤¦à¤¿à¤• à¤œà¥à¤¯à¥‹à¤¤à¤¿à¤·"),
    EXPORT_PAGE("Page %d", "à¤ªà¥ƒà¤·à¥à¤  %d"),
    EXPORT_PLANET("Planet", "à¤—à¥à¤°à¤¹"),
    EXPORT_SIGN("Sign", "à¤°à¤¾à¤¶à¤¿"),
    EXPORT_DEGREE("Degree", "à¤…à¤‚à¤¶"),
    EXPORT_NAKSHATRA("Nakshatra", "à¤¨à¤•à¥à¤·à¤¤à¥à¤°"),
    EXPORT_PADA("Pada", "à¤ªà¤¾à¤¦"),
    EXPORT_HOUSE("House", "à¤­à¤¾à¤µ"),
    EXPORT_STATUS("Status", "à¤¸à¥à¤¥à¤¿à¤¤à¤¿"),
    EXPORT_TOTAL_RUPAS("Total Rupas", "à¤•à¥à¤² à¤°à¥à¤ªà¤¾"),
    EXPORT_REQUIRED("Required", "à¤†à¤µà¤¶à¥à¤¯à¤•"),
    EXPORT_PERCENT("%", "à¤ªà¥à¤°à¤¤à¤¿à¤¶à¤¤"),
    EXPORT_RATING("Rating", "à¤®à¥‚à¤²à¥à¤¯à¤¾à¤™à¥à¤•à¤¨"),
    EXPORT_VIRUPAS("virupas", "à¤µà¤¿à¤°à¥à¤ªà¤¾"),

    // Birth Chart Summary Labels
    EXPORT_BIRTH_INFO("BIRTH INFORMATION", "à¤œà¤¨à¥à¤® à¤œà¤¾à¤¨à¤•à¤¾à¤°à¥€"),
    EXPORT_CHART_SUMMARY("CHART SUMMARY", "à¤•à¥à¤£à¥à¤¡à¤²à¥€ à¤¸à¤¾à¤°à¤¾à¤‚à¤¶"),
    EXPORT_ASCENDANT_LAGNA("Ascendant (Lagna):", "à¤²à¤—à¥à¤¨:"),
    EXPORT_MOON_SIGN_RASHI("Moon Sign (Rashi):", "à¤šà¤¨à¥à¤¦à¥à¤° à¤°à¤¾à¤¶à¤¿:"),
    EXPORT_SUN_SIGN("Sun Sign:", "à¤¸à¥‚à¤°à¥à¤¯ à¤°à¤¾à¤¶à¤¿:"),
    EXPORT_BIRTH_NAKSHATRA("Birth Nakshatra:", "à¤œà¤¨à¥à¤® à¤¨à¤•à¥à¤·à¤¤à¥à¤°:"),
    EXPORT_TIMEZONE("Timezone:", "à¤¸à¤®à¤¯ à¤•à¥à¤·à¥‡à¤¤à¥à¤°:"),

    // Text Report Footer
    EXPORT_CALC_ENGINE("Calculation Engine: Swiss Ephemeris (JPL Mode)", "à¤—à¤£à¤¨à¤¾ à¤‡à¤¨à¥à¤œà¤¿à¤¨: à¤¸à¥à¤µà¤¿à¤¸ à¤ˆà¤«à¥‡à¤®à¥‡à¤°à¤¿à¤¸ (JPL à¤®à¥‹à¤¡)"),
    EXPORT_ULTRA_PRECISION("Ultra-Precision Vedic Astrology Software", "à¤…à¤¤à¤¿-à¤¸à¤Ÿà¥€à¤• à¤µà¥ˆà¤¦à¤¿à¤• à¤œà¥à¤¯à¥‹à¤¤à¤¿à¤· à¤¸à¤«à¥à¤Ÿà¤µà¥‡à¤¯à¤°"),
    EXPORT_GENERATED_BY_SHORT("Generated by AstroStorm", "AstroStorm à¤¦à¥à¤µà¤¾à¤°à¤¾ à¤‰à¤¤à¥à¤ªà¤¨à¥à¤¨"),
    EXPORT_GENERATED("Generated", "à¤‰à¤¤à¥à¤ªà¤¨à¥à¤¨"),
    EXPORT_MOON_SIGN("Moon Sign (Rashi)", "à¤šà¤¨à¥à¤¦à¥à¤° à¤°à¤¾à¤¶à¤¿"),

    // ============================================
    // PREDICTION TYPES
    // ============================================
    PREDICTION_STRONG_PLANET("Strong %s", "à¤¬à¤²à¤¿à¤¯à¥‹ %s"),
    PREDICTION_STRONG_DESC("This planet has sufficient strength to deliver positive results. Its significations will manifest more easily in your life.", "à¤¯à¥‹ à¤—à¥à¤°à¤¹à¤¸à¤à¤— à¤¸à¤•à¤¾à¤°à¤¾à¤¤à¥à¤®à¤• à¤ªà¤°à¤¿à¤£à¤¾à¤®à¤¹à¤°à¥‚ à¤¦à¤¿à¤¨à¤•à¥‹ à¤²à¤¾à¤—à¤¿ à¤ªà¤°à¥à¤¯à¤¾à¤ªà¥à¤¤ à¤¬à¤² à¤›à¥¤ à¤¯à¤¸à¤•à¥‹ à¤•à¤°à¤•à¤¤à¥à¤µà¤¹à¤°à¥‚ à¤¤à¤ªà¤¾à¤ˆà¤‚à¤•à¥‹ à¤œà¥€à¤µà¤¨à¤®à¤¾ à¤¸à¤œà¤¿à¤²à¥ˆà¤¸à¤à¤— à¤ªà¥à¤°à¤•à¤Ÿ à¤¹à¥à¤¨à¥‡à¤›à¤¨à¥à¥¤"),
    PREDICTION_WEAK_PLANET("Weak %s", "à¤•à¤®à¤œà¥‹à¤° %s"),
    PREDICTION_WEAK_DESC("This planet lacks sufficient strength. You may face challenges in areas it governs. Remedial measures may help.", "à¤¯à¥‹ à¤—à¥à¤°à¤¹à¤®à¤¾ à¤ªà¤°à¥à¤¯à¤¾à¤ªà¥à¤¤ à¤¬à¤² à¤›à¥ˆà¤¨à¥¤ à¤¤à¤ªà¤¾à¤ˆà¤‚à¤²à¥‡ à¤¯à¤¸à¤²à¥‡ à¤¶à¤¾à¤¸à¤¨ à¤—à¤°à¥à¤¨à¥‡ à¤•à¥à¤·à¥‡à¤¤à¥à¤°à¤¹à¤°à¥‚à¤®à¤¾ à¤šà¥à¤¨à¥Œà¤¤à¥€à¤¹à¤°à¥‚ à¤¸à¤¾à¤®à¤¨à¤¾ à¤—à¤°à¥à¤¨ à¤¸à¤•à¥à¤¨à¥à¤¹à¥à¤¨à¥à¤›à¥¤ à¤‰à¤ªà¤¾à¤¯ à¤‰à¤ªà¤¾à¤¯à¤¹à¤°à¥‚à¤²à¥‡ à¤®à¤¦à¥à¤¦à¤¤ à¤—à¤°à¥à¤¨ à¤¸à¤•à¥à¤›à¥¤"),
    PREDICTION_EXALTED("Exalted Planet", "à¤‰à¤šà¥à¤š à¤—à¥à¤°à¤¹"),
    PREDICTION_EXALTED_DESC("%s is in its sign of exaltation, giving exceptional results in its significations.", "%s à¤†à¤«à¥à¤¨à¥‹ à¤‰à¤šà¥à¤š à¤°à¤¾à¤¶à¤¿à¤®à¤¾ à¤›, à¤¯à¤¸à¤•à¥‹ à¤•à¤°à¤•à¤¤à¥à¤µà¤®à¤¾ à¤…à¤¸à¤¾à¤§à¤¾à¤°à¤£ à¤ªà¤°à¤¿à¤£à¤¾à¤®à¤¹à¤°à¥‚ à¤¦à¤¿à¤à¤¦à¥ˆà¥¤"),
    PREDICTION_DEBILITATED("Debilitated Planet", "à¤¨à¥€à¤š à¤—à¥à¤°à¤¹"),
    PREDICTION_DEBILITATED_DESC("%s is in its fall. Its positive significations may be reduced or delayed.", "%s à¤†à¤«à¥à¤¨à¥‹ à¤¨à¥€à¤š à¤°à¤¾à¤¶à¤¿à¤®à¤¾ à¤›à¥¤ à¤¯à¤¸à¤•à¥‹ à¤¸à¤•à¤¾à¤°à¤¾à¤¤à¥à¤®à¤• à¤•à¤°à¤•à¤¤à¥à¤µà¤¹à¤°à¥‚ à¤•à¤® à¤µà¤¾ à¤¢à¤¿à¤²à¥‹ à¤¹à¥à¤¨ à¤¸à¤•à¥à¤›à¤¨à¥à¥¤"),
    PREDICTION_OWN_SIGN("Planet in Own Sign", "à¤¸à¥à¤µà¤°à¤¾à¤¶à¤¿à¤®à¤¾ à¤—à¥à¤°à¤¹"),
    PREDICTION_OWN_SIGN_DESC("%s is comfortable in its own sign, giving stable and reliable results.", "%s à¤†à¤«à¥à¤¨à¥ˆ à¤°à¤¾à¤¶à¤¿à¤®à¤¾ à¤¸à¤¹à¤œ à¤›, à¤¸à¥à¤¥à¤¿à¤° à¤° à¤­à¤°à¤ªà¤°à¥à¤¦à¥‹ à¤ªà¤°à¤¿à¤£à¤¾à¤®à¤¹à¤°à¥‚ à¤¦à¤¿à¤à¤¦à¥ˆà¥¤"),
    PREDICTION_RETROGRADE("Retrograde Motion", "à¤µà¤•à¥à¤°à¥€ à¤—à¤¤à¤¿"),
    PREDICTION_RETROGRADE_DESC("Retrograde planets work on an internal level. Results may be delayed but often more profound.", "à¤µà¤•à¥à¤°à¥€ à¤—à¥à¤°à¤¹à¤¹à¤°à¥‚ à¤†à¤¨à¥à¤¤à¤°à¤¿à¤• à¤¸à¥à¤¤à¤°à¤®à¤¾ à¤•à¤¾à¤® à¤—à¤°à¥à¤›à¤¨à¥à¥¤ à¤ªà¤°à¤¿à¤£à¤¾à¤®à¤¹à¤°à¥‚ à¤¢à¤¿à¤²à¥‹ à¤¹à¥à¤¨ à¤¸à¤•à¥à¤›à¤¨à¥ à¤¤à¤° à¤ªà¥à¤°à¤¾à¤¯à¤ƒ à¤…à¤§à¤¿à¤• à¤—à¤¹à¤¿à¤°à¥‹ à¤¹à¥à¤¨à¥à¤›à¤¨à¥à¥¤"),
    PREDICTION_TRIKONA("Trikona Placement", "à¤¤à¥à¤°à¤¿à¤•à¥‹à¤£ à¤¸à¥à¤¥à¤¿à¤¤à¤¿"),
    PREDICTION_TRIKONA_DESC("%s in house %d (Trikona) is auspicious for fortune and dharma.", "à¤­à¤¾à¤µ %d (à¤¤à¥à¤°à¤¿à¤•à¥‹à¤£) à¤®à¤¾ %s à¤­à¤¾à¤—à¥à¤¯ à¤° à¤§à¤°à¥à¤®à¤•à¥‹ à¤²à¤¾à¤—à¤¿ à¤¶à¥à¤­ à¤›à¥¤"),
    PREDICTION_DUSTHANA("Dusthana Placement", "à¤¦à¥à¤ƒà¤¸à¥à¤¥à¤¾à¤¨ à¤¸à¥à¤¥à¤¿à¤¤à¤¿"),
    PREDICTION_DUSTHANA_DESC("%s in house %d may face obstacles but can also give transformative experiences.", "à¤­à¤¾à¤µ %d à¤®à¤¾ %s à¤²à¥‡ à¤¬à¤¾à¤§à¤¾à¤¹à¤°à¥‚ à¤¸à¤¾à¤®à¤¨à¤¾ à¤—à¤°à¥à¤¨ à¤¸à¤•à¥à¤› à¤¤à¤° à¤°à¥‚à¤ªà¤¾à¤¨à¥à¤¤à¤°à¤£à¤•à¤¾à¤°à¥€ à¤…à¤¨à¥à¤­à¤µà¤¹à¤°à¥‚ à¤ªà¤¨à¤¿ à¤¦à¤¿à¤¨ à¤¸à¤•à¥à¤›à¥¤"),
    PREDICTION_KENDRA("Kendra Placement", "à¤•à¥‡à¤¨à¥à¤¦à¥à¤° à¤¸à¥à¤¥à¤¿à¤¤à¤¿"),
    PREDICTION_KENDRA_DESC("%s in house %d (Kendra) gains strength and visibility.", "à¤­à¤¾à¤µ %d (à¤•à¥‡à¤¨à¥à¤¦à¥à¤°) à¤®à¤¾ %s à¤²à¥‡ à¤¬à¤² à¤° à¤¦à¥ƒà¤¶à¥à¤¯à¤¤à¤¾ à¤ªà¥à¤°à¤¾à¤ªà¥à¤¤ à¤—à¤°à¥à¤¦à¤›à¥¤"),

    // ============================================
    // VARSHAPHALA - TAJIKA ASPECT TYPES
    // ============================================
    TAJIKA_ITHASALA("Ithasala", "à¤‡à¤¤à¤¶à¤¾à¤²"),
    TAJIKA_ITHASALA_DESC("Applying aspect - promises fulfillment of matters", "à¤¨à¤¿à¤•à¤Ÿà¤µà¤°à¥à¤¤à¥€ à¤ªà¤•à¥à¤· - à¤•à¤¾à¤°à¥à¤¯à¤¹à¤°à¥‚ à¤ªà¥‚à¤°à¥à¤£ à¤¹à¥à¤¨à¥‡ à¤µà¤¾à¤šà¤¾"),
    TAJIKA_EASARAPHA("Easarapha", "à¤‡à¤¸à¤°à¤¾à¤«"),
    TAJIKA_EASARAPHA_DESC("Separating aspect - event has passed or is fading", "à¤µà¤¿à¤²à¤— à¤ªà¤•à¥à¤· - à¤˜à¤Ÿà¤¨à¤¾ à¤¬à¤¿à¤¤à¤¿à¤¸à¤•à¥‡à¤•à¥‹ à¤µà¤¾ à¤•à¥à¤·à¥€à¤£ à¤¹à¥à¤à¤¦à¥ˆà¤›"),
    TAJIKA_NAKTA("Nakta", "à¤¨à¤•à¥à¤¤"),
    TAJIKA_NAKTA_DESC("Transmission of light with reception - indirect completion", "à¤—à¥à¤°à¤¹à¤£à¤¸à¤¹à¤¿à¤¤ à¤ªà¥à¤°à¤•à¤¾à¤¶ à¤ªà¥à¤°à¤¸à¤¾à¤°à¤£ - à¤…à¤ªà¥à¤°à¤¤à¥à¤¯à¤•à¥à¤· à¤ªà¥‚à¤°à¥à¤£à¤¤à¤¾"),
    TAJIKA_YAMAYA("Yamaya", "à¤¯à¤®à¤¾à¤¯à¤¾"),
    TAJIKA_YAMAYA_DESC("Translation of light - third planet connects significators", "à¤ªà¥à¤°à¤•à¤¾à¤¶ à¤…à¤¨à¥à¤µà¤¾à¤¦ - à¤¤à¥‡à¤¸à¥à¤°à¥‹ à¤—à¥à¤°à¤¹à¤²à¥‡ à¤•à¤¾à¤°à¤•à¤¹à¤°à¥‚ à¤œà¥‹à¤¡à¥à¤¦à¤›"),
    TAJIKA_MANAU("Manau", "à¤®à¤¨à¥Œ"),
    TAJIKA_MANAU_DESC("Reverse application - slower planet applies to faster", "à¤‰à¤²à¥à¤Ÿà¥‹ à¤ªà¥à¤°à¤¯à¥‹à¤— - à¤¢à¤¿à¤²à¥‹ à¤—à¥à¤°à¤¹à¤²à¥‡ à¤›à¤¿à¤Ÿà¥‹à¤®à¤¾ à¤ªà¥à¤°à¤¯à¥‹à¤— à¤—à¤°à¥à¤›"),
    TAJIKA_KAMBOOLA("Kamboola", "à¤•à¤®à¥à¤¬à¥‚à¤²"),
    TAJIKA_KAMBOOLA_DESC("Powerful Ithasala with angular placement", "à¤•à¥‡à¤¨à¥à¤¦à¥à¤° à¤¸à¥à¤¥à¤¾à¤¨à¤¸à¤¹à¤¿à¤¤ à¤¶à¤•à¥à¤¤à¤¿à¤¶à¤¾à¤²à¥€ à¤‡à¤¤à¤¶à¤¾à¤²"),
    TAJIKA_GAIRI_KAMBOOLA("Gairi-Kamboola", "à¤—à¥ˆà¤°à¥€-à¤•à¤®à¥à¤¬à¥‚à¤²"),
    TAJIKA_GAIRI_KAMBOOLA_DESC("Weaker form of Kamboola", "à¤•à¤®à¥à¤¬à¥‚à¤²à¤•à¥‹ à¤•à¤®à¤œà¥‹à¤° à¤°à¥‚à¤ª"),
    TAJIKA_KHALASARA("Khalasara", "à¤–à¤²à¤¾à¤¸à¤°"),
    TAJIKA_KHALASARA_DESC("Mutual separation - dissolution of matters", "à¤ªà¤¾à¤°à¤¸à¥à¤ªà¤°à¤¿à¤• à¤µà¤¿à¤²à¤—à¤¤à¤¾ - à¤•à¤¾à¤°à¥à¤¯à¤¹à¤°à¥‚à¤•à¥‹ à¤µà¤¿à¤˜à¤Ÿà¤¨"),
    TAJIKA_RADDA("Radda", "à¤°à¤¦à¥à¤¦"),
    TAJIKA_RADDA_DESC("Refranation - retrograde breaks the aspect", "à¤­à¤‚à¤— - à¤µà¤•à¥à¤°à¥€ à¤—à¤¤à¤¿à¤²à¥‡ à¤ªà¤•à¥à¤· à¤¤à¥‹à¤¡à¥à¤›"),
    TAJIKA_DUHPHALI_KUTTHA("Duhphali-Kuttha", "à¤¦à¥à¤ƒà¤«à¤²à¥€-à¤•à¥à¤Ÿà¥à¤ "),
    TAJIKA_DUHPHALI_KUTTHA_DESC("Malefic intervention prevents completion", "à¤ªà¤¾à¤ªà¤—à¥à¤°à¤¹ à¤¹à¤¸à¥à¤¤à¤•à¥à¤·à¥‡à¤ªà¤²à¥‡ à¤ªà¥‚à¤°à¥à¤£à¤¤à¤¾ à¤°à¥‹à¤•à¥à¤›"),
    TAJIKA_TAMBIRA("Tambira", "à¤¤à¤®à¥à¤¬à¥€à¤°"),
    TAJIKA_TAMBIRA_DESC("Indirect aspect through intermediary", "à¤®à¤§à¥à¤¯à¤¸à¥à¤¥ à¤®à¤¾à¤°à¥à¤«à¤¤ à¤…à¤ªà¥à¤°à¤¤à¥à¤¯à¤•à¥à¤· à¤ªà¤•à¥à¤·"),
    TAJIKA_KUTTHA("Kuttha", "à¤•à¥à¤Ÿà¥à¤ "),
    TAJIKA_KUTTHA_DESC("Impediment to aspect completion", "à¤ªà¤•à¥à¤· à¤ªà¥‚à¤°à¥à¤£à¤¤à¤¾à¤®à¤¾ à¤¬à¤¾à¤§à¤¾"),
    TAJIKA_DURAPHA("Durapha", "à¤¦à¥à¤°à¤¾à¤«"),
    TAJIKA_DURAPHA_DESC("Hard aspect causing difficulties", "à¤•à¤ à¤¿à¤¨à¤¾à¤‡à¤¹à¤°à¥‚ à¤¨à¤¿à¤®à¥à¤¤à¥à¤¯à¤¾à¤‰à¤¨à¥‡ à¤•à¤¡à¤¾ à¤ªà¤•à¥à¤·"),
    TAJIKA_MUTHASHILA("Muthashila", "à¤®à¥à¤¥à¤¶à¤¿à¤²"),
    TAJIKA_MUTHASHILA_DESC("Mutual application between planets", "à¤—à¥à¤°à¤¹à¤¹à¤°à¥‚ à¤¬à¥€à¤š à¤ªà¤¾à¤°à¤¸à¥à¤ªà¤°à¤¿à¤• à¤ªà¥à¤°à¤¯à¥‹à¤—"),
    TAJIKA_IKKABALA("Ikkabala", "à¤‡à¤•à¥à¤•à¤¬à¤²"),
    TAJIKA_IKKABALA_DESC("Unity of strength between planets", "à¤—à¥à¤°à¤¹à¤¹à¤°à¥‚ à¤¬à¥€à¤š à¤¬à¤²à¤•à¥‹ à¤à¤•à¤¤à¤¾"),

    // ============================================
    // VARSHAPHALA - ASPECT STRENGTH
    // ============================================
    ASPECT_VERY_STRONG("Very Strong", "à¤…à¤¤à¤¿ à¤¬à¤²à¤¿à¤¯à¥‹"),
    ASPECT_STRONG("Strong", "à¤¬à¤²à¤¿à¤¯à¥‹"),
    ASPECT_MODERATE("Moderate", "à¤®à¤§à¥à¤¯à¤®"),
    ASPECT_WEAK("Weak", "à¤•à¤®à¤œà¥‹à¤°"),
    ASPECT_VERY_WEAK("Very Weak", "à¤…à¤¤à¤¿ à¤•à¤®à¤œà¥‹à¤°"),

    // ============================================
    // VARSHAPHALA - SAHAM TYPES
    // ============================================
    SAHAM_PUNYA("Fortune", "à¤ªà¥à¤£à¥à¤¯"),
    SAHAM_PUNYA_SANSKRIT("Punya Saham", "à¤ªà¥à¤£à¥à¤¯ à¤¸à¤¹à¤®"),
    SAHAM_PUNYA_DESC("Overall luck and prosperity", "à¤¸à¤®à¤—à¥à¤° à¤­à¤¾à¤—à¥à¤¯ à¤° à¤¸à¤®à¥ƒà¤¦à¥à¤§à¤¿"),
    SAHAM_VIDYA("Education", "à¤µà¤¿à¤¦à¥à¤¯à¤¾"),
    SAHAM_VIDYA_SANSKRIT("Vidya Saham", "à¤µà¤¿à¤¦à¥à¤¯à¤¾ à¤¸à¤¹à¤®"),
    SAHAM_VIDYA_DESC("Learning and knowledge", "à¤¸à¤¿à¤•à¤¾à¤‡ à¤° à¤œà¥à¤žà¤¾à¤¨"),
    SAHAM_YASHAS("Fame", "à¤¯à¤¶"),
    SAHAM_YASHAS_SANSKRIT("Yashas Saham", "à¤¯à¤¶ à¤¸à¤¹à¤®"),
    SAHAM_YASHAS_DESC("Reputation and recognition", "à¤ªà¥à¤°à¤¤à¤¿à¤·à¥à¤ à¤¾ à¤° à¤®à¤¾à¤¨à¥à¤¯à¤¤à¤¾"),
    SAHAM_MITRA("Friends", "à¤®à¤¿à¤¤à¥à¤°"),
    SAHAM_MITRA_SANSKRIT("Mitra Saham", "à¤®à¤¿à¤¤à¥à¤° à¤¸à¤¹à¤®"),
    SAHAM_MITRA_DESC("Friendship and alliances", "à¤®à¤¿à¤¤à¥à¤°à¤¤à¤¾ à¤° à¤—à¤ à¤¬à¤¨à¥à¤§à¤¨"),
    SAHAM_MAHATMYA("Greatness", "à¤®à¤¹à¤¾à¤¤à¥à¤®à¥à¤¯"),
    SAHAM_MAHATMYA_SANSKRIT("Mahatmya Saham", "à¤®à¤¹à¤¾à¤¤à¥à¤®à¥à¤¯ à¤¸à¤¹à¤®"),
    SAHAM_MAHATMYA_DESC("Spiritual achievement", "à¤†à¤§à¥à¤¯à¤¾à¤¤à¥à¤®à¤¿à¤• à¤‰à¤ªà¤²à¤¬à¥à¤§à¤¿"),
    SAHAM_ASHA("Hope", "à¤†à¤¶à¤¾"),
    SAHAM_ASHA_SANSKRIT("Asha Saham", "à¤†à¤¶à¤¾ à¤¸à¤¹à¤®"),
    SAHAM_ASHA_DESC("Aspirations and wishes", "à¤†à¤•à¤¾à¤‚à¤•à¥à¤·à¤¾ à¤° à¤‡à¤šà¥à¤›à¤¾à¤¹à¤°à¥‚"),
    SAHAM_SAMARTHA("Capability", "à¤¸à¤®à¤°à¥à¤¥"),
    SAHAM_SAMARTHA_SANSKRIT("Samartha Saham", "à¤¸à¤®à¤°à¥à¤¥ à¤¸à¤¹à¤®"),
    SAHAM_SAMARTHA_DESC("Ability and competence", "à¤•à¥à¤·à¤®à¤¤à¤¾ à¤° à¤¯à¥‹à¤—à¥à¤¯à¤¤à¤¾"),
    SAHAM_BHRATRI("Siblings", "à¤­à¥à¤°à¤¾à¤¤à¥ƒ"),
    SAHAM_BHRATRI_SANSKRIT("Bhratri Saham", "à¤­à¥à¤°à¤¾à¤¤à¥ƒ à¤¸à¤¹à¤®"),
    SAHAM_BHRATRI_DESC("Brothers and sisters", "à¤¦à¤¾à¤œà¥à¤­à¤¾à¤‡ à¤° à¤¦à¤¿à¤¦à¥€à¤¬à¤¹à¤¿à¤¨à¥€"),
    SAHAM_PITRI("Father", "à¤ªà¤¿à¤¤à¥ƒ"),
    SAHAM_PITRI_SANSKRIT("Pitri Saham", "à¤ªà¤¿à¤¤à¥ƒ à¤¸à¤¹à¤®"),
    SAHAM_PITRI_DESC("Father's welfare", "à¤¬à¥à¤¬à¤¾à¤•à¥‹ à¤•à¤²à¥à¤¯à¤¾à¤£"),
    SAHAM_MATRI("Mother", "à¤®à¤¾à¤¤à¥ƒ"),
    SAHAM_MATRI_SANSKRIT("Matri Saham", "à¤®à¤¾à¤¤à¥ƒ à¤¸à¤¹à¤®"),
    SAHAM_MATRI_DESC("Mother's welfare", "à¤†à¤®à¤¾à¤•à¥‹ à¤•à¤²à¥à¤¯à¤¾à¤£"),
    SAHAM_PUTRA("Children", "à¤ªà¥à¤¤à¥à¤°"),
    SAHAM_PUTRA_SANSKRIT("Putra Saham", "à¤ªà¥à¤¤à¥à¤° à¤¸à¤¹à¤®"),
    SAHAM_PUTRA_DESC("Offspring and progeny", "à¤¸à¤¨à¥à¤¤à¤¾à¤¨"),
    SAHAM_VIVAHA("Marriage", "à¤µà¤¿à¤µà¤¾à¤¹"),
    SAHAM_VIVAHA_SANSKRIT("Vivaha Saham", "à¤µà¤¿à¤µà¤¾à¤¹ à¤¸à¤¹à¤®"),
    SAHAM_VIVAHA_DESC("Matrimony and partnership", "à¤µà¤¿à¤µà¤¾à¤¹ à¤° à¤¸à¤¾à¤à¥‡à¤¦à¤¾à¤°à¥€"),
    SAHAM_KARMA("Career", "à¤•à¤°à¥à¤®"),
    SAHAM_KARMA_SANSKRIT("Karma Saham", "à¤•à¤°à¥à¤® à¤¸à¤¹à¤®"),
    SAHAM_KARMA_DESC("Profession and livelihood", "à¤ªà¥‡à¤¶à¤¾ à¤° à¤œà¥€à¤µà¤¿à¤•à¤¾"),
    SAHAM_ROGA("Disease", "à¤°à¥‹à¤—"),
    SAHAM_ROGA_SANSKRIT("Roga Saham", "à¤°à¥‹à¤— à¤¸à¤¹à¤®"),
    SAHAM_ROGA_DESC("Health challenges", "à¤¸à¥à¤µà¤¾à¤¸à¥à¤¥à¥à¤¯ à¤šà¥à¤¨à¥Œà¤¤à¥€à¤¹à¤°à¥‚"),
    SAHAM_MRITYU("Longevity", "à¤®à¥ƒà¤¤à¥à¤¯à¥"),
    SAHAM_MRITYU_SANSKRIT("Mrityu Saham", "à¤®à¥ƒà¤¤à¥à¤¯à¥ à¤¸à¤¹à¤®"),
    SAHAM_MRITYU_DESC("Life span indicators", "à¤œà¥€à¤µà¤¨ à¤…à¤µà¤§à¤¿ à¤¸à¤‚à¤•à¥‡à¤¤à¤•à¤¹à¤°à¥‚"),
    SAHAM_PARADESA("Foreign", "à¤ªà¤°à¤¦à¥‡à¤¶"),
    SAHAM_PARADESA_SANSKRIT("Paradesa Saham", "à¤ªà¤°à¤¦à¥‡à¤¶ à¤¸à¤¹à¤®"),
    SAHAM_PARADESA_DESC("Travel and foreign lands", "à¤¯à¤¾à¤¤à¥à¤°à¤¾ à¤° à¤µà¤¿à¤¦à¥‡à¤¶"),
    SAHAM_DHANA("Wealth", "à¤§à¤¨"),
    SAHAM_DHANA_SANSKRIT("Dhana Saham", "à¤§à¤¨ à¤¸à¤¹à¤®"),
    SAHAM_DHANA_DESC("Financial prosperity", "à¤†à¤°à¥à¤¥à¤¿à¤• à¤¸à¤®à¥ƒà¤¦à¥à¤§à¤¿"),
    SAHAM_RAJA("Power", "à¤°à¤¾à¤œ"),
    SAHAM_RAJA_SANSKRIT("Raja Saham", "à¤°à¤¾à¤œ à¤¸à¤¹à¤®"),
    SAHAM_RAJA_DESC("Authority and position", "à¤…à¤§à¤¿à¤•à¤¾à¤° à¤° à¤ªà¤¦"),
    SAHAM_BANDHANA("Bondage", "à¤¬à¤¨à¥à¤§à¤¨"),
    SAHAM_BANDHANA_SANSKRIT("Bandhana Saham", "à¤¬à¤¨à¥à¤§à¤¨ à¤¸à¤¹à¤®"),
    SAHAM_BANDHANA_DESC("Restrictions and obstacles", "à¤ªà¥à¤°à¤¤à¤¿à¤¬à¤¨à¥à¤§ à¤° à¤¬à¤¾à¤§à¤¾à¤¹à¤°à¥‚"),
    SAHAM_KARYASIDDHI_TYPE("Success", "à¤•à¤¾à¤°à¥à¤¯à¤¸à¤¿à¤¦à¥à¤§à¤¿"),
    SAHAM_KARYASIDDHI_TYPE_SANSKRIT("Karyasiddhi Saham", "à¤•à¤¾à¤°à¥à¤¯à¤¸à¤¿à¤¦à¥à¤§à¤¿ à¤¸à¤¹à¤®"),
    SAHAM_KARYASIDDHI_TYPE_DESC("Accomplishment of goals", "à¤²à¤•à¥à¤·à¥à¤¯ à¤ªà¥à¤°à¤¾à¤ªà¥à¤¤à¤¿"),

    // ============================================
    // VARSHAPHALA - KEY DATE TYPES
    // ============================================
    KEY_DATE_FAVORABLE("Favorable", "à¤…à¤¨à¥à¤•à¥‚à¤²"),
    KEY_DATE_CHALLENGING("Challenging", "à¤šà¥à¤¨à¥Œà¤¤à¥€à¤ªà¥‚à¤°à¥à¤£"),
    KEY_DATE_IMPORTANT("Important", "à¤®à¤¹à¤¤à¥à¤¤à¥à¤µà¤ªà¥‚à¤°à¥à¤£"),
    KEY_DATE_TRANSIT("Transit", "à¤—à¥‹à¤šà¤°"),

    // ============================================
    // VARSHAPHALA - UI STRINGS
    // ============================================
    VARSHAPHALA_NO_CHART("No Chart Selected", "à¤•à¥à¤¨à¥ˆ à¤•à¥à¤£à¥à¤¡à¤²à¥€ à¤›à¤¾à¤¨à¤¿à¤à¤•à¥‹ à¤›à¥ˆà¤¨"),
    VARSHAPHALA_NO_CHART_DESC("Select a birth chart to view Varshaphala", "à¤µà¤°à¥à¤·à¤«à¤² à¤¹à¥‡à¤°à¥à¤¨ à¤œà¤¨à¥à¤® à¤•à¥à¤£à¥à¤¡à¤²à¥€ à¤›à¤¾à¤¨à¥à¤¨à¥à¤¹à¥‹à¤¸à¥"),
    VARSHAPHALA_SUN_RETURNS("Sun returns to natal position", "à¤¸à¥‚à¤°à¥à¤¯ à¤œà¤¨à¥à¤®à¤•à¤¾à¤²à¥€à¤¨ à¤¸à¥à¤¥à¤¾à¤¨à¤®à¤¾ à¤«à¤°à¥à¤•à¤¨à¥à¤›"),
    VARSHAPHALA_RETURN_DATE("Return Date", "à¤ªà¥à¤°à¤¤à¤¿à¤«à¤² à¤®à¤¿à¤¤à¤¿"),
    VARSHAPHALA_AGE_FORMAT("Age %d", "à¤‰à¤®à¥‡à¤° %d"),
    VARSHAPHALA_IN_HOUSE("House %d", "à¤­à¤¾à¤µ %d"),
    VARSHAPHALA_FIVEFOLD_STRENGTH("Five-fold Planetary Strength", "à¤ªà¤žà¥à¤šà¤µà¤°à¥à¤—à¥€à¤¯ à¤—à¥à¤°à¤¹ à¤¬à¤²"),
    VARSHAPHALA_THREE_FLAG("Three-flag Diagram", "à¤¤à¥à¤°à¤¿à¤ªà¤¤à¤¾à¤•à¥€ à¤šà¤•à¥à¤°"),
    VARSHAPHALA_IMPORTANT_DATES("%d important dates", "%d à¤®à¤¹à¤¤à¥à¤¤à¥à¤µà¤ªà¥‚à¤°à¥à¤£ à¤®à¤¿à¤¤à¤¿à¤¹à¤°à¥‚"),
    VARSHAPHALA_CHART_LEGEND_ASC("Asc", "à¤²à¤—à¥à¤¨"),
    VARSHAPHALA_CHART_LEGEND_MUNTHA("Muntha", "à¤®à¥à¤¨à¥à¤¥"),
    VARSHAPHALA_CHART_LEGEND_BENEFIC("Benefic", "à¤¶à¥à¤­"),
    VARSHAPHALA_CHART_LEGEND_MALEFIC("Malefic", "à¤ªà¤¾à¤ª"),
    VARSHAPHALA_CHALLENGING("Challenging", "à¤šà¥à¤¨à¥Œà¤¤à¥€à¤ªà¥‚à¤°à¥à¤£"),
    VARSHAPHALA_TOTAL("Total", "à¤•à¥à¤²"),
    VARSHAPHALA_ACTIVE("Active", "à¤¸à¤•à¥à¤°à¤¿à¤¯"),
    VARSHAPHALA_CURRENT("CURRENT", "à¤µà¤°à¥à¤¤à¤®à¤¾à¤¨"),
    VARSHAPHALA_DAYS_FORMAT("%d days", "%d à¤¦à¤¿à¤¨"),
    VARSHAPHALA_HOUSE_SIGN("House %d - %s", "à¤­à¤¾à¤µ %d - %s"),
    VARSHAPHALA_LORD_IN_HOUSE("Lord: %s in H%d", "à¤¸à¥à¤µà¤¾à¤®à¥€: %s à¤­à¤¾à¤µ%d à¤®à¤¾"),
    VARSHAPHALA_RULES_HOUSES("Rules Houses: %s", "à¤­à¤¾à¤µà¤¹à¤°à¥‚ à¤¶à¤¾à¤¸à¤¨: %s"),
    VARSHAPHALA_PLANETS("Planets: ", "à¤—à¥à¤°à¤¹à¤¹à¤°à¥‚: "),
    VARSHAPHALA_NO_PLANETS("No planets", "à¤•à¥à¤¨à¥ˆ à¤—à¥à¤°à¤¹ à¤›à¥ˆà¤¨"),
    VARSHAPHALA_HOUSES_PREFIX("Houses: ", "à¤­à¤¾à¤µà¤¹à¤°à¥‚: "),
    VARSHAPHALA_SPECIFIC_INDICATIONS("Specific Indications:", "à¤µà¤¿à¤¶à¤¿à¤·à¥à¤Ÿ à¤¸à¤‚à¤•à¥‡à¤¤à¤¹à¤°à¥‚:"),
    VARSHAPHALA_LORD_PREFIX("Lord: %s", "à¤¸à¥à¤µà¤¾à¤®à¥€: %s"),

    // ============================================
    // VARSHAPHALA - MUNTHA THEMES
    // ============================================
    MUNTHA_PERSONAL_GROWTH("Personal Growth", "à¤µà¥à¤¯à¤•à¥à¤¤à¤¿à¤—à¤¤ à¤µà¤¿à¤•à¤¾à¤¸"),
    MUNTHA_NEW_BEGINNINGS("New Beginnings", "à¤¨à¤¯à¤¾à¤ à¤¶à¥à¤°à¥à¤µà¤¾à¤¤à¤¹à¤°à¥‚"),
    MUNTHA_HEALTH_FOCUS("Health Focus", "à¤¸à¥à¤µà¤¾à¤¸à¥à¤¥à¥à¤¯ à¤«à¥‹à¤•à¤¸"),
    MUNTHA_FINANCIAL_GAINS("Financial Gains", "à¤†à¤°à¥à¤¥à¤¿à¤• à¤²à¤¾à¤­"),
    MUNTHA_FAMILY_MATTERS("Family Matters", "à¤ªà¤¾à¤°à¤¿à¤µà¤¾à¤°à¤¿à¤• à¤®à¤¾à¤®à¤¿à¤²à¤¾à¤¹à¤°à¥‚"),
    MUNTHA_SPEECH("Speech", "à¤µà¤¾à¤£à¥€"),
    MUNTHA_COMMUNICATION("Communication", "à¤¸à¤žà¥à¤šà¤¾à¤°"),
    MUNTHA_SHORT_TRAVELS("Short Travels", "à¤›à¥‹à¤Ÿà¥‹ à¤¯à¤¾à¤¤à¥à¤°à¤¾à¤¹à¤°à¥‚"),
    MUNTHA_SIBLINGS("Siblings", "à¤­à¤¾à¤‡à¤¬à¤¹à¤¿à¤¨à¥€"),
    MUNTHA_HOME_AFFAIRS("Home Affairs", "à¤˜à¤°à¥‡à¤²à¥ à¤®à¤¾à¤®à¤¿à¤²à¤¾à¤¹à¤°à¥‚"),
    MUNTHA_PROPERTY("Property", "à¤¸à¤®à¥à¤ªà¤¤à¥à¤¤à¤¿"),
    MUNTHA_INNER_PEACE("Inner Peace", "à¤†à¤¨à¥à¤¤à¤°à¤¿à¤• à¤¶à¤¾à¤¨à¥à¤¤à¤¿"),
    MUNTHA_CREATIVITY("Creativity", "à¤¸à¤¿à¤°à¥à¤œà¤¨à¤¶à¥€à¤²à¤¤à¤¾"),
    MUNTHA_ROMANCE("Romance", "à¤°à¥‹à¤®à¤¾à¤¨à¥à¤¸"),
    MUNTHA_CHILDREN("Children", "à¤¸à¤¨à¥à¤¤à¤¾à¤¨"),
    MUNTHA_SERVICE("Service", "à¤¸à¥‡à¤µà¤¾"),
    MUNTHA_HEALTH_ISSUES("Health Issues", "à¤¸à¥à¤µà¤¾à¤¸à¥à¤¥à¥à¤¯ à¤¸à¤®à¤¸à¥à¤¯à¤¾à¤¹à¤°à¥‚"),
    MUNTHA_COMPETITION("Competition", "à¤ªà¥à¤°à¤¤à¤¿à¤¸à¥à¤ªà¤°à¥à¤§à¤¾"),
    MUNTHA_PARTNERSHIPS("Partnerships", "à¤¸à¤¾à¤à¥‡à¤¦à¤¾à¤°à¥€à¤¹à¤°à¥‚"),
    MUNTHA_MARRIAGE("Marriage", "à¤µà¤¿à¤µà¤¾à¤¹"),
    MUNTHA_BUSINESS("Business", "à¤µà¥à¤¯à¤¾à¤ªà¤¾à¤°"),
    MUNTHA_TRANSFORMATION("Transformation", "à¤°à¥‚à¤ªà¤¾à¤¨à¥à¤¤à¤°à¤£"),
    MUNTHA_RESEARCH("Research", "à¤…à¤¨à¥à¤¸à¤¨à¥à¤§à¤¾à¤¨"),
    MUNTHA_INHERITANCE("Inheritance", "à¤µà¤¿à¤°à¤¾à¤¸à¤¤"),
    MUNTHA_FORTUNE("Fortune", "à¤­à¤¾à¤—à¥à¤¯"),
    MUNTHA_LONG_TRAVEL("Long Travel", "à¤²à¤¾à¤®à¥‹ à¤¯à¤¾à¤¤à¥à¤°à¤¾"),
    MUNTHA_HIGHER_LEARNING("Higher Learning", "à¤‰à¤šà¥à¤š à¤¶à¤¿à¤•à¥à¤·à¤¾"),
    MUNTHA_CAREER_ADVANCEMENT("Career Advancement", "à¤•à¥à¤¯à¤¾à¤°à¤¿à¤¯à¤° à¤ªà¥à¤°à¤—à¤¤à¤¿"),
    MUNTHA_RECOGNITION("Recognition", "à¤®à¤¾à¤¨à¥à¤¯à¤¤à¤¾"),
    MUNTHA_AUTHORITY("Authority", "à¤…à¤§à¤¿à¤•à¤¾à¤°"),
    MUNTHA_GAINS("Gains", "à¤²à¤¾à¤­"),
    MUNTHA_FRIENDS("Friends", "à¤®à¤¿à¤¤à¥à¤°à¤¹à¤°à¥‚"),
    MUNTHA_FULFILLED_WISHES("Fulfilled Wishes", "à¤ªà¥‚à¤°à¤¾ à¤­à¤à¤•à¤¾ à¤‡à¤šà¥à¤›à¤¾à¤¹à¤°à¥‚"),
    MUNTHA_SPIRITUALITY("Spirituality", "à¤†à¤§à¥à¤¯à¤¾à¤¤à¥à¤®à¤¿à¤•à¤¤à¤¾"),
    MUNTHA_FOREIGN_LANDS("Foreign Lands", "à¤µà¤¿à¤¦à¥‡à¤¶"),
    MUNTHA_EXPENSES("Expenses", "à¤–à¤°à¥à¤šà¤¹à¤°à¥‚"),
    MUNTHA_GENERAL_GROWTH("General Growth", "à¤¸à¤¾à¤®à¤¾à¤¨à¥à¤¯ à¤µà¤¿à¤•à¤¾à¤¸"),

    // ============================================
    // VARSHAPHALA - HOUSE SIGNIFICATIONS
    // ============================================
    VARSHA_HOUSE_1_SIG("personal development and health", "à¤µà¥à¤¯à¤•à¥à¤¤à¤¿à¤—à¤¤ à¤µà¤¿à¤•à¤¾à¤¸ à¤° à¤¸à¥à¤µà¤¾à¤¸à¥à¤¥à¥à¤¯"),
    VARSHA_HOUSE_2_SIG("finances and family", "à¤µà¤¿à¤¤à¥à¤¤ à¤° à¤ªà¤°à¤¿à¤µà¤¾à¤°"),
    VARSHA_HOUSE_3_SIG("communication and siblings", "à¤¸à¤žà¥à¤šà¤¾à¤° à¤° à¤­à¤¾à¤‡à¤¬à¤¹à¤¿à¤¨à¥€"),
    VARSHA_HOUSE_4_SIG("home and property", "à¤˜à¤° à¤° à¤¸à¤®à¥à¤ªà¤¤à¥à¤¤à¤¿"),
    VARSHA_HOUSE_5_SIG("creativity and children", "à¤¸à¤¿à¤°à¥à¤œà¤¨à¤¶à¥€à¤²à¤¤à¤¾ à¤° à¤¸à¤¨à¥à¤¤à¤¾à¤¨"),
    VARSHA_HOUSE_6_SIG("health and service", "à¤¸à¥à¤µà¤¾à¤¸à¥à¤¥à¥à¤¯ à¤° à¤¸à¥‡à¤µà¤¾"),
    VARSHA_HOUSE_7_SIG("partnerships and marriage", "à¤¸à¤¾à¤à¥‡à¤¦à¤¾à¤°à¥€ à¤° à¤µà¤¿à¤µà¤¾à¤¹"),
    VARSHA_HOUSE_8_SIG("transformation and inheritance", "à¤°à¥‚à¤ªà¤¾à¤¨à¥à¤¤à¤°à¤£ à¤° à¤µà¤¿à¤°à¤¾à¤¸à¤¤"),
    VARSHA_HOUSE_9_SIG("fortune and higher learning", "à¤­à¤¾à¤—à¥à¤¯ à¤° à¤‰à¤šà¥à¤š à¤¶à¤¿à¤•à¥à¤·à¤¾"),
    VARSHA_HOUSE_10_SIG("career and status", "à¤•à¥à¤¯à¤¾à¤°à¤¿à¤¯à¤° à¤° à¤¸à¥à¤¥à¤¿à¤¤à¤¿"),
    VARSHA_HOUSE_11_SIG("gains and friendships", "à¤²à¤¾à¤­ à¤° à¤®à¤¿à¤¤à¥à¤°à¤¤à¤¾"),
    VARSHA_HOUSE_12_SIG("spirituality and foreign matters", "à¤†à¤§à¥à¤¯à¤¾à¤¤à¥à¤®à¤¿à¤•à¤¤à¤¾ à¤° à¤µà¤¿à¤¦à¥‡à¤¶à¥€ à¤®à¤¾à¤®à¤¿à¤²à¤¾à¤¹à¤°à¥‚"),
    VARSHA_HOUSE_VARIOUS("various life areas", "à¤µà¤¿à¤­à¤¿à¤¨à¥à¤¨ à¤œà¥€à¤µà¤¨ à¤•à¥à¤·à¥‡à¤¤à¥à¤°à¤¹à¤°à¥‚"),

    // ============================================
    // VARSHAPHALA - PLANET PERIOD PREDICTIONS
    // ============================================
    VARSHA_SUN_NATURE("vitality, authority, and self-expression", "à¤œà¥€à¤µà¤¨à¤¶à¤•à¥à¤¤à¤¿, à¤…à¤§à¤¿à¤•à¤¾à¤° à¤° à¤†à¤¤à¥à¤®-à¤…à¤­à¤¿à¤µà¥à¤¯à¤•à¥à¤¤à¤¿"),
    VARSHA_MOON_NATURE("emotions, nurturing, and public connections", "à¤­à¤¾à¤µà¤¨à¤¾à¤¹à¤°à¥‚, à¤ªà¤¾à¤²à¤¨à¤ªà¥‹à¤·à¤£ à¤° à¤¸à¤¾à¤°à¥à¤µà¤œà¤¨à¤¿à¤• à¤¸à¤®à¥à¤¬à¤¨à¥à¤§à¤¹à¤°à¥‚"),
    VARSHA_MARS_NATURE("energy, initiative, and competitive drive", "à¤Šà¤°à¥à¤œà¤¾, à¤ªà¤¹à¤² à¤° à¤ªà¥à¤°à¤¤à¤¿à¤¸à¥à¤ªà¤°à¥à¤§à¤¾à¤¤à¥à¤®à¤• à¤‰à¤¤à¥à¤ªà¥à¤°à¥‡à¤°à¤£à¤¾"),
    VARSHA_MERCURY_NATURE("communication, learning, and business", "à¤¸à¤žà¥à¤šà¤¾à¤°, à¤¸à¤¿à¤•à¤¾à¤‡ à¤° à¤µà¥à¤¯à¤¾à¤ªà¤¾à¤°"),
    VARSHA_JUPITER_NATURE("wisdom, expansion, and good fortune", "à¤¬à¥à¤¦à¥à¤§à¤¿, à¤µà¤¿à¤¸à¥à¤¤à¤¾à¤° à¤° à¤¸à¥à¤­à¤¾à¤—à¥à¤¯"),
    VARSHA_VENUS_NATURE("relationships, creativity, and pleasures", "à¤¸à¤®à¥à¤¬à¤¨à¥à¤§à¤¹à¤°à¥‚, à¤¸à¤¿à¤°à¥à¤œà¤¨à¤¶à¥€à¤²à¤¤à¤¾ à¤° à¤†à¤¨à¤¨à¥à¤¦"),
    VARSHA_SATURN_NATURE("discipline, responsibility, and long-term goals", "à¤…à¤¨à¥à¤¶à¤¾à¤¸à¤¨, à¤œà¤¿à¤®à¥à¤®à¥‡à¤µà¤¾à¤°à¥€ à¤° à¤¦à¥€à¤°à¥à¤˜à¤•à¤¾à¤²à¥€à¤¨ à¤²à¤•à¥à¤·à¥à¤¯à¤¹à¤°à¥‚"),
    VARSHA_RAHU_NATURE("ambition, innovation, and unconventional paths", "à¤®à¤¹à¤¤à¥à¤¤à¥à¤µà¤¾à¤•à¤¾à¤‚à¤•à¥à¤·à¤¾, à¤¨à¤µà¥€à¤¨à¤¤à¤¾ à¤° à¤…à¤ªà¤°à¤®à¥à¤ªà¤°à¤¾à¤—à¤¤ à¤®à¤¾à¤°à¥à¤—à¤¹à¤°à¥‚"),
    VARSHA_KETU_NATURE("spirituality, detachment, and past karma", "à¤†à¤§à¥à¤¯à¤¾à¤¤à¥à¤®à¤¿à¤•à¤¤à¤¾, à¤µà¥ˆà¤°à¤¾à¤—à¥à¤¯ à¤° à¤ªà¥‚à¤°à¥à¤µ à¤•à¤°à¥à¤®"),
    VARSHA_GENERAL_NATURE("general influences", "à¤¸à¤¾à¤®à¤¾à¤¨à¥à¤¯ à¤ªà¥à¤°à¤­à¤¾à¤µà¤¹à¤°à¥‚"),

    // ============================================
    // VARSHAPHALA - STRENGTH DESCRIPTIONS
    // ============================================
    VARSHA_STRENGTH_EXALTED("Exalted", "à¤‰à¤šà¥à¤š"),
    VARSHA_STRENGTH_STRONG("Strong", "à¤¬à¤²à¤¿à¤¯à¥‹"),
    VARSHA_STRENGTH_MODERATE("Moderate", "à¤®à¤§à¥à¤¯à¤®"),
    VARSHA_STRENGTH_ANGULAR("Angular", "à¤•à¥‡à¤¨à¥à¤¦à¥à¤°à¥€à¤¯"),
    VARSHA_STRENGTH_RETROGRADE("Retrograde", "à¤µà¤•à¥à¤°à¥€"),
    VARSHA_STRENGTH_DEBILITATED("Debilitated", "à¤¨à¥€à¤š"),
    VARSHA_STRENGTH_UNKNOWN("Unknown", "à¤…à¤œà¥à¤žà¤¾à¤¤"),

    // ============================================
    // VARSHAPHALA - PREDICTION PHRASES
    // ============================================
    VARSHA_PERIOD_EXCEPTIONAL("This period promises exceptional results", "à¤¯à¥‹ à¤…à¤µà¤§à¤¿à¤²à¥‡ à¤…à¤¸à¤¾à¤§à¤¾à¤°à¤£ à¤ªà¤°à¤¿à¤£à¤¾à¤®à¤¹à¤°à¥‚à¤•à¥‹ à¤µà¤¾à¤šà¤¾ à¤—à¤°à¥à¤›"),
    VARSHA_PERIOD_WELL_SUPPORTED("This period is well-supported for success", "à¤¯à¥‹ à¤…à¤µà¤§à¤¿ à¤¸à¤«à¤²à¤¤à¤¾à¤•à¥‹ à¤²à¤¾à¤—à¤¿ à¤°à¤¾à¤®à¥à¤°à¤°à¥€ à¤¸à¤®à¤°à¥à¤¥à¤¿à¤¤ à¤›"),
    VARSHA_PERIOD_EXTRA_EFFORT("This period requires extra effort and patience", "à¤¯à¥‹ à¤…à¤µà¤§à¤¿à¤®à¤¾ à¤¥à¤ª à¤ªà¥à¤°à¤¯à¤¾à¤¸ à¤° à¤§à¥ˆà¤°à¥à¤¯ à¤šà¤¾à¤¹à¤¿à¤¨à¥à¤›"),
    VARSHA_PERIOD_MIXED("This period brings mixed but manageable influences", "à¤¯à¥‹ à¤…à¤µà¤§à¤¿à¤²à¥‡ à¤®à¤¿à¤¶à¥à¤°à¤¿à¤¤ à¤¤à¤° à¤µà¥à¤¯à¤µà¤¸à¥à¤¥à¤¾à¤ªà¤¨à¤¯à¥‹à¤—à¥à¤¯ à¤ªà¥à¤°à¤­à¤¾à¤µà¤¹à¤°à¥‚ à¤²à¥à¤¯à¤¾à¤‰à¤à¤›"),
    VARSHA_DURING_PERIOD("During this %s period, focus shifts to %s, particularly affecting %s. %s.", "à¤¯à¥‹ %s à¤…à¤µà¤§à¤¿à¤®à¤¾, %s à¤®à¤¾ à¤«à¥‹à¤•à¤¸ à¤¸à¤°à¥à¤›, à¤µà¤¿à¤¶à¥‡à¤· à¤—à¤°à¥€ %s à¤²à¤¾à¤ˆ à¤…à¤¸à¤° à¤—à¤°à¥à¤¦à¤›à¥¤ %sà¥¤"),

    // ============================================
    // VARSHAPHALA - YEAR LORD DESCRIPTIONS
    // ============================================
    VARSHA_YEARLORD_SUN("Year Lord Sun brings focus on leadership, authority, and self-expression.", "à¤µà¤°à¥à¤· à¤¸à¥à¤µà¤¾à¤®à¥€ à¤¸à¥‚à¤°à¥à¤¯à¤²à¥‡ à¤¨à¥‡à¤¤à¥ƒà¤¤à¥à¤µ, à¤…à¤§à¤¿à¤•à¤¾à¤° à¤° à¤†à¤¤à¥à¤®-à¤…à¤­à¤¿à¤µà¥à¤¯à¤•à¥à¤¤à¤¿à¤®à¤¾ à¤«à¥‹à¤•à¤¸ à¤²à¥à¤¯à¤¾à¤‰à¤à¤›à¥¤"),
    VARSHA_YEARLORD_MOON("Year Lord Moon emphasizes emotional wellbeing and public connections.", "à¤µà¤°à¥à¤· à¤¸à¥à¤µà¤¾à¤®à¥€ à¤šà¤¨à¥à¤¦à¥à¤°à¤®à¤¾à¤²à¥‡ à¤­à¤¾à¤µà¤¨à¤¾à¤¤à¥à¤®à¤• à¤•à¤²à¥à¤¯à¤¾à¤£ à¤° à¤¸à¤¾à¤°à¥à¤µà¤œà¤¨à¤¿à¤• à¤¸à¤®à¥à¤¬à¤¨à¥à¤§à¤¹à¤°à¥‚à¤®à¤¾ à¤œà¥‹à¤¡ à¤¦à¤¿à¤¨à¥à¤›à¥¤"),
    VARSHA_YEARLORD_MARS("Year Lord Mars energizes initiatives and competitive endeavors.", "à¤µà¤°à¥à¤· à¤¸à¥à¤µà¤¾à¤®à¥€ à¤®à¤‚à¤—à¤²à¤²à¥‡ à¤ªà¤¹à¤²à¤¹à¤°à¥‚ à¤° à¤ªà¥à¤°à¤¤à¤¿à¤¸à¥à¤ªà¤°à¥à¤§à¤¾à¤¤à¥à¤®à¤• à¤ªà¥à¤°à¤¯à¤¾à¤¸à¤¹à¤°à¥‚à¤²à¤¾à¤ˆ à¤Šà¤°à¥à¤œà¤¿à¤¤ à¤—à¤°à¥à¤›à¥¤"),
    VARSHA_YEARLORD_MERCURY("Year Lord Mercury enhances communication and business activities.", "à¤µà¤°à¥à¤· à¤¸à¥à¤µà¤¾à¤®à¥€ à¤¬à¥à¤§à¤²à¥‡ à¤¸à¤žà¥à¤šà¤¾à¤° à¤° à¤µà¥à¤¯à¤¾à¤ªà¤¾à¤°à¤¿à¤• à¤—à¤¤à¤¿à¤µà¤¿à¤§à¤¿à¤¹à¤°à¥‚ à¤¬à¤¢à¤¾à¤‰à¤à¤›à¥¤"),
    VARSHA_YEARLORD_JUPITER("Year Lord Jupiter bestows wisdom, expansion, and good fortune.", "à¤µà¤°à¥à¤· à¤¸à¥à¤µà¤¾à¤®à¥€ à¤¬à¥ƒà¤¹à¤¸à¥à¤ªà¤¤à¤¿à¤²à¥‡ à¤¬à¥à¤¦à¥à¤§à¤¿, à¤µà¤¿à¤¸à¥à¤¤à¤¾à¤° à¤° à¤¸à¥à¤­à¤¾à¤—à¥à¤¯ à¤ªà¥à¤°à¤¦à¤¾à¤¨ à¤—à¤°à¥à¤›à¥¤"),
    VARSHA_YEARLORD_VENUS("Year Lord Venus brings harmony to relationships and creativity.", "à¤µà¤°à¥à¤· à¤¸à¥à¤µà¤¾à¤®à¥€ à¤¶à¥à¤•à¥à¤°à¤²à¥‡ à¤¸à¤®à¥à¤¬à¤¨à¥à¤§à¤¹à¤°à¥‚ à¤° à¤¸à¤¿à¤°à¥à¤œà¤¨à¤¶à¥€à¤²à¤¤à¤¾à¤®à¤¾ à¤¸à¤¾à¤®à¤žà¥à¤œà¤¸à¥à¤¯ à¤²à¥à¤¯à¤¾à¤‰à¤à¤›à¥¤"),
    VARSHA_YEARLORD_SATURN("Year Lord Saturn teaches discipline and responsibility.", "à¤µà¤°à¥à¤· à¤¸à¥à¤µà¤¾à¤®à¥€ à¤¶à¤¨à¤¿à¤²à¥‡ à¤…à¤¨à¥à¤¶à¤¾à¤¸à¤¨ à¤° à¤œà¤¿à¤®à¥à¤®à¥‡à¤µà¤¾à¤°à¥€ à¤¸à¤¿à¤•à¤¾à¤‰à¤à¤›à¥¤"),
    VARSHA_YEARLORD_GENERIC("The Year Lord influences various aspects with balanced energy.", "à¤µà¤°à¥à¤· à¤¸à¥à¤µà¤¾à¤®à¥€à¤²à¥‡ à¤¸à¤¨à¥à¤¤à¥à¤²à¤¿à¤¤ à¤Šà¤°à¥à¤œà¤¾à¤¸à¤¹à¤¿à¤¤ à¤µà¤¿à¤­à¤¿à¤¨à¥à¤¨ à¤ªà¤•à¥à¤·à¤¹à¤°à¥‚à¤²à¤¾à¤ˆ à¤ªà¥à¤°à¤­à¤¾à¤µ à¤ªà¤¾à¤°à¥à¤›à¥¤"),

    // ============================================
    // VARSHAPHALA - TRI-PATAKI CHAKRA
    // ============================================
    TRIPATAKI_DHARMA("Dharma (1, 5, 9)", "à¤§à¤°à¥à¤® (à¥§, à¥«, à¥¯)"),
    TRIPATAKI_ARTHA("Artha (2, 6, 10)", "à¤…à¤°à¥à¤¥ (à¥¨, à¥¬, à¥§à¥¦)"),
    TRIPATAKI_KAMA("Kama (3, 7, 11)", "à¤•à¤¾à¤® (à¥©, à¥­, à¥§à¥§)"),
    TRIPATAKI_DHARMA_DOMINANT("Spiritual growth and righteous pursuits dominate", "à¤†à¤§à¥à¤¯à¤¾à¤¤à¥à¤®à¤¿à¤• à¤µà¥ƒà¤¦à¥à¤§à¤¿ à¤° à¤§à¤¾à¤°à¥à¤®à¤¿à¤• à¤–à¥‹à¤œà¥€à¤¹à¤°à¥‚ à¤ªà¥à¤°à¤­à¤¾à¤µà¥€"),
    TRIPATAKI_ARTHA_DOMINANT("Material prosperity and career emphasis", "à¤­à¥Œà¤¤à¤¿à¤• à¤¸à¤®à¥ƒà¤¦à¥à¤§à¤¿ à¤° à¤•à¥à¤¯à¤¾à¤°à¤¿à¤¯à¤° à¤œà¥‹à¤¡"),
    TRIPATAKI_KAMA_DOMINANT("Relationships and desires take center stage", "à¤¸à¤®à¥à¤¬à¤¨à¥à¤§à¤¹à¤°à¥‚ à¤° à¤‡à¤šà¥à¤›à¤¾à¤¹à¤°à¥‚ à¤•à¥‡à¤¨à¥à¤¦à¥à¤°à¤®à¤¾"),
    TRIPATAKI_BALANCED("Balanced influences across all areas", "à¤¸à¤¬à¥ˆ à¤•à¥à¤·à¥‡à¤¤à¥à¤°à¤¹à¤°à¥‚à¤®à¤¾ à¤¸à¤¨à¥à¤¤à¥à¤²à¤¿à¤¤ à¤ªà¥à¤°à¤­à¤¾à¤µ"),
    TRIPATAKI_NO_PLANETS("No planets in %s sector - quieter year for these matters.", "%s à¤•à¥à¤·à¥‡à¤¤à¥à¤°à¤®à¤¾ à¤•à¥à¤¨à¥ˆ à¤—à¥à¤°à¤¹ à¤›à¥ˆà¤¨ - à¤¯à¥€ à¤®à¤¾à¤®à¤¿à¤²à¤¾à¤¹à¤°à¥‚à¤•à¥‹ à¤²à¤¾à¤—à¤¿ à¤¶à¤¾à¤¨à¥à¤¤ à¤µà¤°à¥à¤·à¥¤"),
    TRIPATAKI_BENEFIC_INFLUENCE("Benefic %s bring favorable influences.", "à¤¶à¥à¤­ %s à¤²à¥‡ à¤…à¤¨à¥à¤•à¥‚à¤² à¤ªà¥à¤°à¤­à¤¾à¤µà¤¹à¤°à¥‚ à¤²à¥à¤¯à¤¾à¤‰à¤à¤›à¥¤"),
    TRIPATAKI_MALEFIC_INFLUENCE("Malefic %s bring challenges requiring effort.", "à¤ªà¤¾à¤ª %s à¤²à¥‡ à¤ªà¥à¤°à¤¯à¤¾à¤¸ à¤šà¤¾à¤¹à¤¿à¤¨à¥‡ à¤šà¥à¤¨à¥Œà¤¤à¥€à¤¹à¤°à¥‚ à¤²à¥à¤¯à¤¾à¤‰à¤à¤›à¥¤"),
    TRIPATAKI_MIXED_INFLUENCE("Mixed influences suggest variable results.", "à¤®à¤¿à¤¶à¥à¤°à¤¿à¤¤ à¤ªà¥à¤°à¤­à¤¾à¤µà¤¹à¤°à¥‚à¤²à¥‡ à¤ªà¤°à¤¿à¤µà¤°à¥à¤¤à¤¨à¤¶à¥€à¤² à¤ªà¤°à¤¿à¤£à¤¾à¤®à¤¹à¤°à¥‚ à¤¸à¥à¤à¤¾à¤µ à¤¦à¤¿à¤¨à¥à¤›à¥¤"),
    TRIPATAKI_DHARMA_AREA("righteousness, fortune, and higher learning", "à¤§à¤¾à¤°à¥à¤®à¤¿à¤•à¤¤à¤¾, à¤­à¤¾à¤—à¥à¤¯ à¤° à¤‰à¤šà¥à¤š à¤¶à¤¿à¤•à¥à¤·à¤¾"),
    TRIPATAKI_ARTHA_AREA("wealth, career, and practical achievements", "à¤§à¤¨, à¤•à¥à¤¯à¤¾à¤°à¤¿à¤¯à¤° à¤° à¤µà¥à¤¯à¤¾à¤µà¤¹à¤¾à¤°à¤¿à¤• à¤‰à¤ªà¤²à¤¬à¥à¤§à¤¿à¤¹à¤°à¥‚"),
    TRIPATAKI_KAMA_AREA("relationships, desires, and social connections", "à¤¸à¤®à¥à¤¬à¤¨à¥à¤§à¤¹à¤°à¥‚, à¤‡à¤šà¥à¤›à¤¾à¤¹à¤°à¥‚ à¤° à¤¸à¤¾à¤®à¤¾à¤œà¤¿à¤• à¤¸à¤®à¥à¤¬à¤¨à¥à¤§à¤¹à¤°à¥‚"),
    TRIPATAKI_PLANETS_IN_TRIKONA("%d planet(s) in %s trikona emphasizes %s.", "%s à¤¤à¥à¤°à¤¿à¤•à¥‹à¤£à¤®à¤¾ %d à¤—à¥à¤°à¤¹(à¤¹à¤°à¥‚)à¤²à¥‡ %s à¤®à¤¾ à¤œà¥‹à¤¡ à¤¦à¤¿à¤¨à¥à¤›à¥¤"),
    TRIPATAKI_BALANCED_DISTRIBUTION("Balanced distribution of planetary energies across all life sectors.", "à¤¸à¤¬à¥ˆ à¤œà¥€à¤µà¤¨ à¤•à¥à¤·à¥‡à¤¤à¥à¤°à¤¹à¤°à¥‚à¤®à¤¾ à¤—à¥à¤°à¤¹ à¤Šà¤°à¥à¤œà¤¾à¤¹à¤°à¥‚à¤•à¥‹ à¤¸à¤¨à¥à¤¤à¥à¤²à¤¿à¤¤ à¤µà¤¿à¤¤à¤°à¤£à¥¤"),

    // ============================================
    // VARSHAPHALA - OVERALL PREDICTION TONES
    // ============================================
    VARSHA_TONE_EXCELLENT("excellent", "à¤‰à¤¤à¥à¤•à¥ƒà¤·à¥à¤Ÿ"),
    VARSHA_TONE_FAVORABLE("favorable", "à¤…à¤¨à¥à¤•à¥‚à¤²"),
    VARSHA_TONE_POSITIVE("positive", "à¤¸à¤•à¤¾à¤°à¤¾à¤¤à¥à¤®à¤•"),
    VARSHA_TONE_CHALLENGING("challenging but growth-oriented", "à¤šà¥à¤¨à¥Œà¤¤à¥€à¤ªà¥‚à¤°à¥à¤£ à¤¤à¤° à¤µà¤¿à¤•à¤¾à¤¸à¥‹à¤¨à¥à¤®à¥à¤–"),
    VARSHA_TONE_BALANCED("balanced", "à¤¸à¤¨à¥à¤¤à¥à¤²à¤¿à¤¤"),

    // ============================================
    // VARSHAPHALA - PANCHA VARGIYA BALA CATEGORIES
    // ============================================
    PANCHA_EXCELLENT("Excellent", "à¤‰à¤¤à¥à¤•à¥ƒà¤·à¥à¤Ÿ"),
    PANCHA_GOOD("Good", "à¤°à¤¾à¤®à¥à¤°à¥‹"),
    PANCHA_AVERAGE("Average", "à¤”à¤¸à¤¤"),
    PANCHA_BELOW_AVERAGE("Below Average", "à¤”à¤¸à¤¤à¤®à¥à¤¨à¤¿"),
    PANCHA_WEAK("Weak", "à¤•à¤®à¤œà¥‹à¤°"),

    // ============================================
    // VARSHAPHALA - DASHA KEYWORDS
    // ============================================
    DASHA_KW_LEADERSHIP("Leadership", "à¤¨à¥‡à¤¤à¥ƒà¤¤à¥à¤µ"),
    DASHA_KW_VITALITY("Vitality", "à¤œà¥€à¤µà¤¨à¤¶à¤•à¥à¤¤à¤¿"),
    DASHA_KW_FATHER("Father", "à¤¬à¥à¤¬à¤¾"),
    DASHA_KW_EMOTIONS("Emotions", "à¤­à¤¾à¤µà¤¨à¤¾à¤¹à¤°à¥‚"),
    DASHA_KW_MOTHER("Mother", "à¤†à¤®à¤¾"),
    DASHA_KW_PUBLIC("Public", "à¤¸à¤¾à¤°à¥à¤µà¤œà¤¨à¤¿à¤•"),
    DASHA_KW_ACTION("Action", "à¤•à¤¾à¤°à¥à¤¯"),
    DASHA_KW_ENERGY("Energy", "à¤Šà¤°à¥à¤œà¤¾"),
    DASHA_KW_COURAGE("Courage", "à¤¸à¤¾à¤¹à¤¸"),
    DASHA_KW_COMMUNICATION("Communication", "à¤¸à¤žà¥à¤šà¤¾à¤°"),
    DASHA_KW_LEARNING("Learning", "à¤¸à¤¿à¤•à¤¾à¤‡"),
    DASHA_KW_WISDOM("Wisdom", "à¤¬à¥à¤¦à¥à¤§à¤¿"),
    DASHA_KW_GROWTH("Growth", "à¤µà¥ƒà¤¦à¥à¤§à¤¿"),
    DASHA_KW_LOVE("Love", "à¤ªà¥à¤°à¥‡à¤®"),
    DASHA_KW_ART("Art", "à¤•à¤²à¤¾"),
    DASHA_KW_COMFORT("Comfort", "à¤¸à¥à¤µà¤¿à¤§à¤¾"),
    DASHA_KW_DISCIPLINE("Discipline", "à¤…à¤¨à¥à¤¶à¤¾à¤¸à¤¨"),
    DASHA_KW_KARMA("Karma", "à¤•à¤°à¥à¤®"),
    DASHA_KW_DELAYS("Delays", "à¤¢à¤¿à¤²à¤¾à¤‡"),
    DASHA_KW_AMBITION("Ambition", "à¤®à¤¹à¤¤à¥à¤¤à¥à¤µà¤¾à¤•à¤¾à¤‚à¤•à¥à¤·à¤¾"),
    DASHA_KW_INNOVATION("Innovation", "à¤¨à¤µà¥€à¤¨à¤¤à¤¾"),
    DASHA_KW_FOREIGN("Foreign", "à¤µà¤¿à¤¦à¥‡à¤¶à¥€"),
    DASHA_KW_DETACHMENT("Detachment", "à¤µà¥ˆà¤°à¤¾à¤—à¥à¤¯"),
    DASHA_KW_PAST("Past", "à¤­à¥‚à¤¤à¤•à¤¾à¤²"),
    DASHA_KW_GENERAL("General", "à¤¸à¤¾à¤®à¤¾à¤¨à¥à¤¯"),
    DASHA_KW_SELF("Self", "à¤†à¤¤à¥à¤®"),
    DASHA_KW_BODY("Body", "à¤¶à¤°à¥€à¤°"),
    DASHA_KW_WEALTH("Wealth", "à¤§à¤¨"),
    DASHA_KW_SPEECH("Speech", "à¤µà¤¾à¤£à¥€"),
    DASHA_KW_HOME("Home", "à¤˜à¤°"),
    DASHA_KW_PEACE("Peace", "à¤¶à¤¾à¤¨à¥à¤¤à¤¿"),
    DASHA_KW_CHILDREN("Children", "à¤¸à¤¨à¥à¤¤à¤¾à¤¨"),
    DASHA_KW_HEALTH("Health", "à¤¸à¥à¤µà¤¾à¤¸à¥à¤¥à¥à¤¯"),
    DASHA_KW_SERVICE("Service", "à¤¸à¥‡à¤µà¤¾"),
    DASHA_KW_MARRIAGE("Marriage", "à¤µà¤¿à¤µà¤¾à¤¹"),
    DASHA_KW_BUSINESS("Business", "à¤µà¥à¤¯à¤¾à¤ªà¤¾à¤°"),
    DASHA_KW_TRANSFORMATION("Transformation", "à¤°à¥‚à¤ªà¤¾à¤¨à¥à¤¤à¤°à¤£"),
    DASHA_KW_RESEARCH("Research", "à¤…à¤¨à¥à¤¸à¤¨à¥à¤§à¤¾à¤¨"),
    DASHA_KW_LUCK("Luck", "à¤­à¤¾à¤—à¥à¤¯"),
    DASHA_KW_TRAVEL("Travel", "à¤¯à¤¾à¤¤à¥à¤°à¤¾"),
    DASHA_KW_CAREER("Career", "à¤•à¥à¤¯à¤¾à¤°à¤¿à¤¯à¤°"),
    DASHA_KW_STATUS("Status", "à¤¸à¥à¤¥à¤¿à¤¤à¤¿"),
    DASHA_KW_GAINS("Gains", "à¤²à¤¾à¤­"),
    DASHA_KW_FRIENDS("Friends", "à¤®à¤¿à¤¤à¥à¤°à¤¹à¤°à¥‚"),
    DASHA_KW_LOSSES("Losses", "à¤¹à¤¾à¤¨à¤¿"),

    // ============================================
    // VARSHAPHALA - HOUSE PREDICTION PHRASES
    // ============================================
    VARSHA_LORD_EXCELLENT("is excellently placed for positive outcomes.", "à¤¸à¤•à¤¾à¤°à¤¾à¤¤à¥à¤®à¤• à¤ªà¤°à¤¿à¤£à¤¾à¤®à¤¹à¤°à¥‚à¤•à¥‹ à¤²à¤¾à¤—à¤¿ à¤‰à¤¤à¥à¤•à¥ƒà¤·à¥à¤Ÿ à¤°à¥‚à¤ªà¤®à¤¾ à¤¸à¥à¤¥à¤¿à¤¤ à¤›à¥¤"),
    VARSHA_LORD_WELL_POSITIONED("is well-positioned for success.", "à¤¸à¤«à¤²à¤¤à¤¾à¤•à¥‹ à¤²à¤¾à¤—à¤¿ à¤°à¤¾à¤®à¥à¤°à¤°à¥€ à¤¸à¥à¤¥à¤¿à¤¤ à¤›à¥¤"),
    VARSHA_LORD_MODERATE_SUPPORT("provides moderate support.", "à¤®à¤§à¥à¤¯à¤® à¤¸à¤®à¤°à¥à¤¥à¤¨ à¤ªà¥à¤°à¤¦à¤¾à¤¨ à¤—à¤°à¥à¤¦à¤›à¥¤"),
    VARSHA_LORD_CHALLENGES("faces challenges requiring attention.", "à¤§à¥à¤¯à¤¾à¤¨ à¤šà¤¾à¤¹à¤¿à¤¨à¥‡ à¤šà¥à¤¨à¥Œà¤¤à¥€à¤¹à¤°à¥‚à¤•à¥‹ à¤¸à¤¾à¤®à¤¨à¤¾ à¤—à¤°à¥à¤¦à¤›à¥¤"),
    VARSHA_LORD_VARIABLE("influences results variably.", "à¤ªà¤°à¤¿à¤£à¤¾à¤®à¤¹à¤°à¥‚à¤²à¤¾à¤ˆ à¤ªà¤°à¤¿à¤µà¤°à¥à¤¤à¤¨à¤¶à¥€à¤² à¤°à¥‚à¤ªà¤®à¤¾ à¤ªà¥à¤°à¤­à¤¾à¤µ à¤ªà¤¾à¤°à¥à¤›à¥¤"),
    VARSHA_BENEFICS_ENHANCE(" %s enhance positive outcomes.", " %s à¤²à¥‡ à¤¸à¤•à¤¾à¤°à¤¾à¤¤à¥à¤®à¤• à¤ªà¤°à¤¿à¤£à¤¾à¤®à¤¹à¤°à¥‚ à¤¬à¤¢à¤¾à¤‰à¤à¤›à¥¤"),
    VARSHA_MALEFICS_CHALLENGE(" %s may bring challenges.", " %s à¤²à¥‡ à¤šà¥à¤¨à¥Œà¤¤à¥€à¤¹à¤°à¥‚ à¤²à¥à¤¯à¤¾à¤‰à¤¨ à¤¸à¤•à¥à¤›à¥¤"),
    VARSHA_MIXED_PLANETS(" Mixed influences from %s.", " %s à¤¬à¤¾à¤Ÿ à¤®à¤¿à¤¶à¥à¤°à¤¿à¤¤ à¤ªà¥à¤°à¤­à¤¾à¤µà¤¹à¤°à¥‚à¥¤"),
    VARSHA_DEPENDS_LORD(" Results depend primarily on the lord's position.", " à¤ªà¤°à¤¿à¤£à¤¾à¤®à¤¹à¤°à¥‚ à¤®à¥à¤–à¥à¤¯à¤¤à¤¯à¤¾ à¤¸à¥à¤µà¤¾à¤®à¥€à¤•à¥‹ à¤¸à¥à¤¥à¤¿à¤¤à¤¿à¤®à¤¾ à¤¨à¤¿à¤°à¥à¤­à¤° à¤›à¤¨à¥à¥¤"),
    VARSHA_MUNTHA_EMPHASIS(" Muntha emphasizes these matters this year.", " à¤®à¥à¤¨à¥à¤¥à¤²à¥‡ à¤¯à¤¸ à¤µà¤°à¥à¤· à¤¯à¥€ à¤®à¤¾à¤®à¤¿à¤²à¤¾à¤¹à¤°à¥‚à¤®à¤¾ à¤œà¥‹à¤¡ à¤¦à¤¿à¤¨à¥à¤›à¥¤"),
    VARSHA_YEARLORD_RULES(" Year Lord rules this house - significant developments expected.", " à¤µà¤°à¥à¤· à¤¸à¥à¤µà¤¾à¤®à¥€à¤²à¥‡ à¤¯à¥‹ à¤­à¤¾à¤µ à¤¶à¤¾à¤¸à¤¨ à¤—à¤°à¥à¤› - à¤®à¤¹à¤¤à¥à¤¤à¥à¤µà¤ªà¥‚à¤°à¥à¤£ à¤µà¤¿à¤•à¤¾à¤¸à¤¹à¤°à¥‚ à¤…à¤ªà¥‡à¤•à¥à¤·à¤¿à¤¤à¥¤"),
    VARSHA_HOUSE_GOVERNS("House %d in %s governs %s.", "à¤­à¤¾à¤µ %d %s à¤®à¤¾ %s à¤¶à¤¾à¤¸à¤¨ à¤—à¤°à¥à¤¦à¤›à¥¤"),
    VARSHA_LORD_IN_HOUSE("The lord %s in house %d ", "à¤¸à¥à¤µà¤¾à¤®à¥€ %s à¤­à¤¾à¤µ %d à¤®à¤¾ "),

    // ============================================
    // VARSHAPHALA - SPECIFIC EVENTS
    // ============================================
    VARSHA_EVENT_VITALITY("Increased vitality and personal confidence", "à¤¬à¤¢à¥‡à¤•à¥‹ à¤œà¥€à¤µà¤¨à¤¶à¤•à¥à¤¤à¤¿ à¤° à¤µà¥à¤¯à¤•à¥à¤¤à¤¿à¤—à¤¤ à¤†à¤¤à¥à¤®à¤µà¤¿à¤¶à¥à¤µà¤¾à¤¸"),
    VARSHA_EVENT_NEW_VENTURES("Favorable for starting new ventures", "à¤¨à¤¯à¤¾à¤ à¤‰à¤¦à¥à¤¯à¤®à¤¹à¤°à¥‚ à¤¸à¥à¤°à¥ à¤—à¤°à¥à¤¨ à¤…à¤¨à¥à¤•à¥‚à¤²"),
    VARSHA_EVENT_SPIRITUAL("Spiritual growth and wisdom", "à¤†à¤§à¥à¤¯à¤¾à¤¤à¥à¤®à¤¿à¤• à¤µà¥ƒà¤¦à¥à¤§à¤¿ à¤° à¤¬à¥à¤¦à¥à¤§à¤¿"),
    VARSHA_EVENT_ACCIDENTS("Increased energy - watch for accidents", "à¤¬à¤¢à¥‡à¤•à¥‹ à¤Šà¤°à¥à¤œà¤¾ - à¤¦à¥à¤°à¥à¤˜à¤Ÿà¤¨à¤¾à¤¬à¤¾à¤Ÿ à¤¸à¤¾à¤µà¤§à¤¾à¤¨"),
    VARSHA_EVENT_FINANCIAL("Financial gains and wealth accumulation", "à¤†à¤°à¥à¤¥à¤¿à¤• à¤²à¤¾à¤­ à¤° à¤§à¤¨ à¤¸à¤‚à¤šà¤¯"),
    VARSHA_EVENT_FAMILY("Improvement in family relationships", "à¤ªà¤¾à¤°à¤¿à¤µà¤¾à¤°à¤¿à¤• à¤¸à¤®à¥à¤¬à¤¨à¥à¤§à¤®à¤¾ à¤¸à¥à¤§à¤¾à¤°"),
    VARSHA_EVENT_LUXURY("Acquisition of luxury items", "à¤µà¤¿à¤²à¤¾à¤¸à¥€ à¤µà¤¸à¥à¤¤à¥à¤¹à¤°à¥‚à¤•à¥‹ à¤ªà¥à¤°à¤¾à¤ªà¥à¤¤à¤¿"),
    VARSHA_EVENT_CREATIVE("Creative success and recognition", "à¤¸à¤¿à¤°à¥à¤œà¤¨à¤¾à¤¤à¥à¤®à¤• à¤¸à¤«à¤²à¤¤à¤¾ à¤° à¤®à¤¾à¤¨à¥à¤¯à¤¤à¤¾"),
    VARSHA_EVENT_CHILDREN_MATTERS("Favorable for children's matters", "à¤¸à¤¨à¥à¤¤à¤¾à¤¨ à¤¸à¤®à¥à¤¬à¤¨à¥à¤§à¥€ à¤®à¤¾à¤®à¤¿à¤²à¤¾à¤¹à¤°à¥‚à¤•à¥‹ à¤²à¤¾à¤—à¤¿ à¤…à¤¨à¥à¤•à¥‚à¤²"),
    VARSHA_EVENT_ACADEMIC("Academic success or childbirth possible", "à¤¶à¥ˆà¤•à¥à¤·à¤¿à¤• à¤¸à¤«à¤²à¤¤à¤¾ à¤µà¤¾ à¤¸à¤¨à¥à¤¤à¤¾à¤¨ à¤œà¤¨à¥à¤® à¤¸à¤®à¥à¤­à¤µ"),
    VARSHA_EVENT_ROMANTIC("Romantic happiness", "à¤°à¥‹à¤®à¤¾à¤¨à¥à¤Ÿà¤¿à¤• à¤–à¥à¤¶à¥€"),
    VARSHA_EVENT_PARTNERSHIPS("Strengthening of partnerships", "à¤¸à¤¾à¤à¥‡à¤¦à¤¾à¤°à¥€à¤¹à¤°à¥‚à¤•à¥‹ à¤¸à¥à¤¦à¥ƒà¤¢à¥€à¤•à¤°à¤£"),
    VARSHA_EVENT_MARRIAGE_BUSINESS("Favorable for marriage or business", "à¤µà¤¿à¤µà¤¾à¤¹ à¤µà¤¾ à¤µà¥à¤¯à¤¾à¤ªà¤¾à¤°à¤•à¥‹ à¤²à¤¾à¤—à¤¿ à¤…à¤¨à¥à¤•à¥‚à¤²"),
    VARSHA_EVENT_ROMANTIC_FULFILL("Romantic fulfillment", "à¤°à¥‹à¤®à¤¾à¤¨à¥à¤Ÿà¤¿à¤• à¤ªà¥‚à¤°à¥à¤£à¤¤à¤¾"),
    VARSHA_EVENT_CAREER_ADVANCE("Career advancement or promotion", "à¤•à¥à¤¯à¤¾à¤°à¤¿à¤¯à¤° à¤ªà¥à¤°à¤—à¤¤à¤¿ à¤µà¤¾ à¤ªà¤¦à¥‹à¤¨à¥à¤¨à¤¤à¤¿"),
    VARSHA_EVENT_RECOGNITION("Recognition from authorities", "à¤…à¤§à¤¿à¤•à¤¾à¤°à¥€à¤¹à¤°à¥‚à¤¬à¤¾à¤Ÿ à¤®à¤¾à¤¨à¥à¤¯à¤¤à¤¾"),
    VARSHA_EVENT_GOVT_FAVOR("Government favor or leadership role", "à¤¸à¤°à¤•à¤¾à¤°à¥€ à¤•à¥ƒà¤ªà¤¾ à¤µà¤¾ à¤¨à¥‡à¤¤à¥ƒà¤¤à¥à¤µ à¤­à¥‚à¤®à¤¿à¤•à¤¾"),
    VARSHA_EVENT_DESIRES("Fulfillment of desires and wishes", "à¤‡à¤šà¥à¤›à¤¾à¤¹à¤°à¥‚ à¤° à¤•à¤¾à¤®à¤¨à¤¾à¤¹à¤°à¥‚à¤•à¥‹ à¤ªà¥‚à¤°à¥à¤¤à¤¿"),
    VARSHA_EVENT_MULTIPLE_GAINS("Gains from multiple sources", "à¤µà¤¿à¤­à¤¿à¤¨à¥à¤¨ à¤¸à¥à¤°à¥‹à¤¤à¤¹à¤°à¥‚à¤¬à¤¾à¤Ÿ à¤²à¤¾à¤­"),

    // ============================================
    // VARSHAPHALA - DIGNITY DESCRIPTIONS
    // ============================================
    DIGNITY_EXALTED("exalted in %s", "%s à¤®à¤¾ à¤‰à¤šà¥à¤š"),
    DIGNITY_OWN_SIGN("in its own sign of %s", "à¤†à¤«à¥à¤¨à¥ˆ à¤°à¤¾à¤¶à¤¿ %s à¤®à¤¾"),
    DIGNITY_DEBILITATED("debilitated in %s", "%s à¤®à¤¾ à¤¨à¥€à¤š"),
    DIGNITY_FRIENDLY("in the friendly sign of %s", "%s à¤®à¤¿à¤¤à¥à¤° à¤°à¤¾à¤¶à¤¿à¤®à¤¾"),
    DIGNITY_NEUTRAL("in the neutral sign of %s", "%s à¤¤à¤Ÿà¤¸à¥à¤¥ à¤°à¤¾à¤¶à¤¿à¤®à¤¾"),
    DIGNITY_ENEMY("in the enemy sign of %s", "%s à¤¶à¤¤à¥à¤°à¥ à¤°à¤¾à¤¶à¤¿à¤®à¤¾"),
    DIGNITY_KENDRA("in an angular house (Kendra)", "à¤•à¥‡à¤¨à¥à¤¦à¥à¤° à¤­à¤¾à¤µà¤®à¤¾"),
    DIGNITY_TRIKONA("in a trine house (Trikona)", "à¤¤à¥à¤°à¤¿à¤•à¥‹à¤£ à¤­à¤¾à¤µà¤®à¤¾"),
    DIGNITY_GAINS("in a house of gains", "à¤²à¤¾à¤­ à¤­à¤¾à¤µà¤®à¤¾"),
    DIGNITY_UPACHAYA("in an upachaya house", "à¤‰à¤ªà¤šà¤¯ à¤­à¤¾à¤µà¤®à¤¾"),
    DIGNITY_DUSTHANA("in a challenging house (Dusthana)", "à¤¦à¥à¤ƒà¤¸à¥à¤¥à¤¾à¤¨ à¤­à¤¾à¤µà¤®à¤¾"),
    DIGNITY_RETROGRADE("and is retrograde", "à¤° à¤µà¤•à¥à¤°à¥€ à¤›"),
    DIGNITY_YEARLORD_DESC("The Year Lord %s is %s. This suggests its influence will be potent and its results will manifest clearly throughout the year.", "à¤µà¤°à¥à¤· à¤¸à¥à¤µà¤¾à¤®à¥€ %s %s à¤›à¥¤ à¤¯à¤¸à¤²à¥‡ à¤¸à¤‚à¤•à¥‡à¤¤ à¤—à¤°à¥à¤› à¤•à¤¿ à¤¯à¤¸à¤•à¥‹ à¤ªà¥à¤°à¤­à¤¾à¤µ à¤¶à¤•à¥à¤¤à¤¿à¤¶à¤¾à¤²à¥€ à¤¹à¥à¤¨à¥‡à¤› à¤° à¤¯à¤¸à¤•à¥‹ à¤ªà¤°à¤¿à¤£à¤¾à¤®à¤¹à¤°à¥‚ à¤µà¤°à¥à¤·à¤­à¤° à¤¸à¥à¤ªà¤·à¥à¤Ÿ à¤°à¥‚à¤ªà¤®à¤¾ à¤ªà¥à¤°à¤•à¤Ÿ à¤¹à¥à¤¨à¥‡à¤›à¤¨à¥à¥¤"),

    // ============================================
    // VARSHAPHALA - OVERALL PREDICTION TEMPLATE
    // ============================================
    VARSHA_OVERALL_TEMPLATE("This Varshaphala year presents an overall %s outlook. %s %s The Tajika aspects show %d favorable and %d challenging configurations. By understanding these influences, the year's potential can be maximized.", "à¤¯à¥‹ à¤µà¤°à¥à¤·à¤«à¤² à¤µà¤°à¥à¤·à¤²à¥‡ à¤¸à¤®à¤—à¥à¤° %s à¤¦à¥ƒà¤·à¥à¤Ÿà¤¿à¤•à¥‹à¤£ à¤ªà¥à¤°à¤¸à¥à¤¤à¥à¤¤ à¤—à¤°à¥à¤¦à¤›à¥¤ %s %s à¤¤à¤¾à¤œà¤¿à¤• à¤ªà¤•à¥à¤·à¤¹à¤°à¥‚à¤²à¥‡ %d à¤…à¤¨à¥à¤•à¥‚à¤² à¤° %d à¤šà¥à¤¨à¥Œà¤¤à¥€à¤ªà¥‚à¤°à¥à¤£ à¤µà¤¿à¤¨à¥à¤¯à¤¾à¤¸à¤¹à¤°à¥‚ à¤¦à¥‡à¤–à¤¾à¤‰à¤à¤›à¤¨à¥à¥¤ à¤¯à¥€ à¤ªà¥à¤°à¤­à¤¾à¤µà¤¹à¤°à¥‚à¤²à¤¾à¤ˆ à¤¬à¥à¤à¥‡à¤°, à¤µà¤°à¥à¤·à¤•à¥‹ à¤¸à¤®à¥à¤­à¤¾à¤µà¤¨à¤¾à¤²à¤¾à¤ˆ à¤…à¤§à¤¿à¤•à¤¤à¤® à¤¬à¤¨à¤¾à¤‰à¤¨ à¤¸à¤•à¤¿à¤¨à¥à¤›à¥¤"),
    VARSHA_MUNTHA_DIRECTS("Muntha in House %d (%s) directs attention to %s.", "à¤­à¤¾à¤µ %d (%s) à¤®à¤¾ à¤®à¥à¤¨à¥à¤¥à¤²à¥‡ %s à¤®à¤¾ à¤§à¥à¤¯à¤¾à¤¨ à¤•à¥‡à¤¨à¥à¤¦à¥à¤°à¤¿à¤¤ à¤—à¤°à¥à¤¦à¤›à¥¤"),

    // ============================================
    // VARSHAPHALA - KEY DATES
    // ============================================
    KEY_DATE_SOLAR_RETURN("Solar Return", "à¤¸à¥Œà¤° à¤ªà¥à¤°à¤¤à¤¿à¤«à¤²"),
    KEY_DATE_SOLAR_RETURN_DESC("Beginning of the annual horoscope year", "à¤µà¤¾à¤°à¥à¤·à¤¿à¤• à¤°à¤¾à¤¶à¤¿à¤«à¤² à¤µà¤°à¥à¤·à¤•à¥‹ à¤¶à¥à¤°à¥à¤µà¤¾à¤¤"),
    KEY_DATE_DASHA_BEGINS("%s Dasha Begins", "%s à¤¦à¤¶à¤¾ à¤¶à¥à¤°à¥"),
    KEY_DATE_DASHA_DESC("Start of %s period (%d days)", "%s à¤…à¤µà¤§à¤¿à¤•à¥‹ à¤¶à¥à¤°à¥à¤µà¤¾à¤¤ (%d à¤¦à¤¿à¤¨)"),

    // ============================================
    // VARSHAPHALA - HOUSE STRENGTH
    // ============================================
    HOUSE_STRENGTH_EXCELLENT("Excellent", "à¤‰à¤¤à¥à¤•à¥ƒà¤·à¥à¤Ÿ"),
    HOUSE_STRENGTH_STRONG("Strong", "à¤¬à¤²à¤¿à¤¯à¥‹"),
    HOUSE_STRENGTH_MODERATE("Moderate", "à¤®à¤§à¥à¤¯à¤®"),
    HOUSE_STRENGTH_WEAK("Weak", "à¤•à¤®à¤œà¥‹à¤°"),
    HOUSE_STRENGTH_CHALLENGED("Challenged", "à¤šà¥à¤¨à¥Œà¤¤à¥€à¤ªà¥‚à¤°à¥à¤£"),

    // ============================================
    // VARSHAPHALA - ASPECT EFFECT DESCRIPTIONS
    // ============================================
    ASPECT_ITHASALA_EFFECT("%s applying to %s promises fulfillment", "%s %s à¤®à¤¾ à¤¨à¤¿à¤•à¤Ÿà¤µà¤°à¥à¤¤à¥€ à¤¹à¥à¤à¤¦à¤¾ à¤ªà¥‚à¤°à¥à¤¤à¤¿à¤•à¥‹ à¤µà¤¾à¤šà¤¾"),
    ASPECT_EASARAPHA_EFFECT("Separating aspect suggests matters are concluding", "à¤µà¤¿à¤²à¤— à¤ªà¤•à¥à¤·à¤²à¥‡ à¤®à¤¾à¤®à¤¿à¤²à¤¾à¤¹à¤°à¥‚ à¤¸à¤®à¤¾à¤ªà¥à¤¤ à¤¹à¥à¤à¤¦à¥ˆà¤›à¤¨à¥ à¤­à¤¨à¥€ à¤¸à¥à¤à¤¾à¤µ à¤¦à¤¿à¤¨à¥à¤›"),
    ASPECT_KAMBOOLA_EFFECT("Powerful angular conjunction promises prominent success", "à¤¶à¤•à¥à¤¤à¤¿à¤¶à¤¾à¤²à¥€ à¤•à¥‡à¤¨à¥à¤¦à¥à¤°à¥€à¤¯ à¤¯à¥à¤¤à¤¿à¤²à¥‡ à¤ªà¥à¤°à¤®à¥à¤– à¤¸à¤«à¤²à¤¤à¤¾à¤•à¥‹ à¤µà¤¾à¤šà¤¾ à¤—à¤°à¥à¤›"),
    ASPECT_RADDA_EFFECT("Retrograde motion causes delays or reversals", "à¤µà¤•à¥à¤°à¥€ à¤—à¤¤à¤¿à¤²à¥‡ à¤¢à¤¿à¤²à¤¾à¤‡ à¤µà¤¾ à¤‰à¤²à¥à¤Ÿà¥‹ à¤ªà¤°à¤¿à¤£à¤¾à¤® à¤²à¥à¤¯à¤¾à¤‰à¤à¤›"),
    ASPECT_DURAPHA_EFFECT("Hard aspect creates challenges that strengthen through difficulty", "à¤•à¤¡à¤¾ à¤ªà¤•à¥à¤·à¤²à¥‡ à¤•à¤ à¤¿à¤¨à¤¾à¤‡à¤¦à¥à¤µà¤¾à¤°à¤¾ à¤¬à¤²à¤¿à¤¯à¥‹ à¤¬à¤¨à¤¾à¤‰à¤¨à¥‡ à¤šà¥à¤¨à¥Œà¤¤à¥€à¤¹à¤°à¥‚ à¤¸à¤¿à¤°à¥à¤œà¤¨à¤¾ à¤—à¤°à¥à¤›"),
    ASPECT_GENERIC_EFFECT("%s influences matters with %s energy", "%s à¤²à¥‡ %s à¤Šà¤°à¥à¤œà¤¾à¤¸à¤¹à¤¿à¤¤ à¤®à¤¾à¤®à¤¿à¤²à¤¾à¤¹à¤°à¥‚à¤²à¤¾à¤ˆ à¤ªà¥à¤°à¤­à¤¾à¤µ à¤ªà¤¾à¤°à¥à¤›"),
    ASPECT_SUPPORTIVE("supportive", "à¤¸à¤®à¤°à¥à¤¥à¤¨à¤¾à¤¤à¥à¤®à¤•"),
    ASPECT_PREDICTION_TEMPLATE("The %s between %s and %s is %s for matters of %s.", "%s à¤° %s à¤¬à¥€à¤šà¤•à¥‹ %s %s à¤•à¥‹ à¤®à¤¾à¤®à¤¿à¤²à¤¾à¤¹à¤°à¥‚à¤•à¥‹ à¤²à¤¾à¤—à¤¿ %s à¤›à¥¤"),
    ASPECT_FAVORABLE_FOR("favorable", "à¤…à¤¨à¥à¤•à¥‚à¤²"),
    ASPECT_REQUIRING_ATTENTION("requiring attention", "à¤§à¥à¤¯à¤¾à¤¨ à¤šà¤¾à¤¹à¤¿à¤¨à¥‡"),

    // ============================================
    // VARSHAPHALA - SAHAM INTERPRETATION
    // ============================================
    SAHAM_INTERPRETATION_TEMPLATE("The %s Saham in %s (House %d) relates to %s this year. Its lord %s in House %d is %s.", "%s à¤¸à¤¹à¤® %s à¤®à¤¾ (à¤­à¤¾à¤µ %d) à¤¯à¤¸ à¤µà¤°à¥à¤· %s à¤¸à¤à¤— à¤¸à¤®à¥à¤¬à¤¨à¥à¤§à¤¿à¤¤ à¤›à¥¤ à¤¯à¤¸à¤•à¥‹ à¤¸à¥à¤µà¤¾à¤®à¥€ %s à¤­à¤¾à¤µ %d à¤®à¤¾ %s à¤›à¥¤"),
    SAHAM_LORD_WELL_PLACED("well-placed, promising positive outcomes", "à¤°à¤¾à¤®à¥à¤°à¤°à¥€ à¤¸à¥à¤¥à¤¿à¤¤, à¤¸à¤•à¤¾à¤°à¤¾à¤¤à¥à¤®à¤• à¤ªà¤°à¤¿à¤£à¤¾à¤®à¤¹à¤°à¥‚à¤•à¥‹ à¤µà¤¾à¤šà¤¾"),
    SAHAM_LORD_REASONABLE("providing reasonable support", "à¤‰à¤šà¤¿à¤¤ à¤¸à¤®à¤°à¥à¤¥à¤¨ à¤ªà¥à¤°à¤¦à¤¾à¤¨ à¤—à¤°à¥à¤¦à¥ˆ"),
    SAHAM_LORD_ATTENTION("requiring attention and effort", "à¤§à¥à¤¯à¤¾à¤¨ à¤° à¤ªà¥à¤°à¤¯à¤¾à¤¸ à¤šà¤¾à¤¹à¤¿à¤¨à¥‡"),
    SAHAM_LORD_VARIABLE("influencing matters variably", "à¤®à¤¾à¤®à¤¿à¤²à¤¾à¤¹à¤°à¥‚à¤²à¤¾à¤ˆ à¤ªà¤°à¤¿à¤µà¤°à¥à¤¤à¤¨à¤¶à¥€à¤² à¤°à¥‚à¤ªà¤®à¤¾ à¤ªà¥à¤°à¤­à¤¾à¤µ à¤ªà¤¾à¤°à¥à¤¦à¥ˆ"),
    SAHAM_INDICATES("indicates specific life areas", "à¤µà¤¿à¤¶à¤¿à¤·à¥à¤Ÿ à¤œà¥€à¤µà¤¨ à¤•à¥à¤·à¥‡à¤¤à¥à¤°à¤¹à¤°à¥‚ à¤¸à¤‚à¤•à¥‡à¤¤ à¤—à¤°à¥à¤¦à¤›"),

    // ============================================
    // VARSHAPHALA - REPORT SECTIONS
    // ============================================
    VARSHA_REPORT_TITLE("VARSHAPHALA (ANNUAL HOROSCOPE) REPORT", "à¤µà¤°à¥à¤·à¤«à¤² (à¤µà¤¾à¤°à¥à¤·à¤¿à¤• à¤°à¤¾à¤¶à¤¿à¤«à¤²) à¤°à¤¿à¤ªà¥‹à¤°à¥à¤Ÿ"),
    VARSHA_REPORT_NAME("Name: %s", "à¤¨à¤¾à¤®: %s"),
    VARSHA_REPORT_YEAR("Year: %d (Age: %d)", "à¤µà¤°à¥à¤·: %d (à¤‰à¤®à¥‡à¤°: %d)"),
    VARSHA_REPORT_SOLAR_RETURN("Solar Return: %s", "à¤¸à¥Œà¤° à¤ªà¥à¤°à¤¤à¤¿à¤«à¤²: %s"),
    VARSHA_REPORT_YEAR_RATING("Year Rating: %s/5.0", "à¤µà¤°à¥à¤· à¤®à¥‚à¤²à¥à¤¯à¤¾à¤™à¥à¤•à¤¨: %s/à¥«.à¥¦"),
    VARSHA_REPORT_SECTION_YEARLORD("YEAR LORD", "à¤µà¤°à¥à¤· à¤¸à¥à¤µà¤¾à¤®à¥€"),
    VARSHA_REPORT_YEARLORD_LINE("Year Lord: %s (%s)", "à¤µà¤°à¥à¤· à¤¸à¥à¤µà¤¾à¤®à¥€: %s (%s)"),
    VARSHA_REPORT_POSITION("Position: House %d", "à¤¸à¥à¤¥à¤¿à¤¤à¤¿: à¤­à¤¾à¤µ %d"),
    VARSHA_REPORT_SECTION_MUNTHA("MUNTHA", "à¤®à¥à¤¨à¥à¤¥"),
    VARSHA_REPORT_MUNTHA_POSITION("Muntha Position: %sÂ° %s", "à¤®à¥à¤¨à¥à¤¥ à¤¸à¥à¤¥à¤¿à¤¤à¤¿: %sÂ° %s"),
    VARSHA_REPORT_MUNTHA_HOUSE("Muntha House: %d", "à¤®à¥à¤¨à¥à¤¥ à¤­à¤¾à¤µ: %d"),
    VARSHA_REPORT_MUNTHA_LORD("Muntha Lord: %s in House %d", "à¤®à¥à¤¨à¥à¤¥ à¤¸à¥à¤µà¤¾à¤®à¥€: %s à¤­à¤¾à¤µ %d à¤®à¤¾"),
    VARSHA_REPORT_SECTION_THEMES("MAJOR THEMES", "à¤®à¥à¤–à¥à¤¯ à¤µà¤¿à¤·à¤¯à¤¹à¤°à¥‚"),
    VARSHA_REPORT_SECTION_MUDDA("MUDDA DASHA PERIODS", "à¤®à¥à¤¦à¥à¤¦ à¤¦à¤¶à¤¾ à¤…à¤µà¤§à¤¿à¤¹à¤°à¥‚"),
    VARSHA_REPORT_DASHA_LINE("%s: %s to %s (%d days)%s", "%s: %s à¤¦à¥‡à¤–à¤¿ %s (%d à¤¦à¤¿à¤¨)%s"),
    VARSHA_REPORT_CURRENT_MARKER(" [CURRENT]", " [à¤µà¤°à¥à¤¤à¤®à¤¾à¤¨]"),
    VARSHA_REPORT_FAVORABLE_MONTHS("FAVORABLE MONTHS: %s", "à¤…à¤¨à¥à¤•à¥‚à¤² à¤®à¤¹à¤¿à¤¨à¤¾à¤¹à¤°à¥‚: %s"),
    VARSHA_REPORT_CHALLENGING_MONTHS("CHALLENGING MONTHS: %s", "à¤šà¥à¤¨à¥Œà¤¤à¥€à¤ªà¥‚à¤°à¥à¤£ à¤®à¤¹à¤¿à¤¨à¤¾à¤¹à¤°à¥‚: %s"),
    VARSHA_REPORT_SECTION_PREDICTION("OVERALL PREDICTION", "à¤¸à¤®à¤—à¥à¤° à¤­à¤µà¤¿à¤·à¥à¤¯à¤µà¤¾à¤£à¥€"),
    VARSHA_REPORT_FOOTER("Generated by AstroStorm - Ultra-Precision Vedic Astrology", "AstroStorm à¤¦à¥à¤µà¤¾à¤°à¤¾ à¤‰à¤¤à¥à¤ªà¤¨à¥à¤¨ - à¤…à¤¤à¤¿-à¤¸à¤Ÿà¥€à¤• à¤µà¥ˆà¤¦à¤¿à¤• à¤œà¥à¤¯à¥‹à¤¤à¤¿à¤·"),

    // ============================================
    // VARSHAPHALA - MONTH NAMES (SHORT)
    // ============================================
    MONTH_JAN("Jan", "à¤œà¤¨à¤µà¤°à¥€"),
    MONTH_FEB("Feb", "à¤«à¥‡à¤¬à¥à¤°à¥à¤…à¤°à¥€"),
    MONTH_MAR("Mar", "à¤®à¤¾à¤°à¥à¤š"),
    MONTH_APR("Apr", "à¤…à¤ªà¥à¤°à¤¿à¤²"),
    MONTH_MAY("May", "à¤®à¥‡"),
    MONTH_JUN("Jun", "à¤œà¥à¤¨"),
    MONTH_JUL("Jul", "à¤œà¥à¤²à¤¾à¤ˆ"),
    MONTH_AUG("Aug", "à¤…à¤—à¤¸à¥à¤Ÿ"),
    MONTH_SEP("Sep", "à¤¸à¥‡à¤ªà¥à¤Ÿà¥‡à¤®à¥à¤¬à¤°"),
    MONTH_OCT("Oct", "à¤…à¤•à¥à¤Ÿà¥‹à¤¬à¤°"),
    MONTH_NOV("Nov", "à¤¨à¥‹à¤­à¥‡à¤®à¥à¤¬à¤°"),
    MONTH_DEC("Dec", "à¤¡à¤¿à¤¸à¥‡à¤®à¥à¤¬à¤°"),

    // ============================================
    // VARSHAPHALA - ORDINAL SUFFIXES
    // ============================================
    ORDINAL_ST("st", "à¤”à¤‚"),
    ORDINAL_ND("nd", "à¤”à¤‚"),
    ORDINAL_RD("rd", "à¤”à¤‚"),
    ORDINAL_TH("th", "à¤”à¤‚"),

    // ============================================
    // VARSHAPHALA - MUNTHA INTERPRETATION TEMPLATE
    // ============================================
    MUNTHA_INTERPRETATION_TEMPLATE("Muntha in %s in the %d%s house focuses the year's energy on %s. The Muntha lord %s in house %d provides %s support for these matters.", "%d%s à¤­à¤¾à¤µà¤®à¤¾ %s à¤®à¤¾ à¤®à¥à¤¨à¥à¤¥à¤²à¥‡ à¤µà¤°à¥à¤·à¤•à¥‹ à¤Šà¤°à¥à¤œà¤¾à¤²à¤¾à¤ˆ %s à¤®à¤¾ à¤•à¥‡à¤¨à¥à¤¦à¥à¤°à¤¿à¤¤ à¤—à¤°à¥à¤¦à¤›à¥¤ à¤®à¥à¤¨à¥à¤¥ à¤¸à¥à¤µà¤¾à¤®à¥€ %s à¤­à¤¾à¤µ %d à¤®à¤¾ à¤¯à¥€ à¤®à¤¾à¤®à¤¿à¤²à¤¾à¤¹à¤°à¥‚à¤•à¥‹ à¤²à¤¾à¤—à¤¿ %s à¤¸à¤®à¤°à¥à¤¥à¤¨ à¤ªà¥à¤°à¤¦à¤¾à¤¨ à¤—à¤°à¥à¤¦à¤›à¥¤"),
    MUNTHA_SUPPORT_EXCELLENT("excellent", "à¤‰à¤¤à¥à¤•à¥ƒà¤·à¥à¤Ÿ"),
    MUNTHA_SUPPORT_FAVORABLE("favorable", "à¤…à¤¨à¥à¤•à¥‚à¤²"),
    MUNTHA_SUPPORT_CHALLENGING("challenging but growth-oriented", "à¤šà¥à¤¨à¥Œà¤¤à¥€à¤ªà¥‚à¤°à¥à¤£ à¤¤à¤° à¤µà¤¿à¤•à¤¾à¤¸à¥‹à¤¨à¥à¤®à¥à¤–"),
    MUNTHA_SUPPORT_VARIABLE("variable", "à¤ªà¤°à¤¿à¤µà¤°à¥à¤¤à¤¨à¤¶à¥€à¤²"),

    // ============================================
    // DASHA MAHADASHA INTERPRETATIONS
    // ============================================
    DASHA_INTERP_MAHADASHA_SUN(
        "A period of heightened self-expression, authority, and recognition. Focus turns to career advancement, leadership roles, government dealings, and matters related to father. Soul purpose becomes clearer. Health of heart and vitality gains prominence. Good for developing confidence and establishing one's identity in the world.",
        "à¤†à¤¤à¥à¤®-à¤…à¤­à¤¿à¤µà¥à¤¯à¤•à¥à¤¤à¤¿, à¤…à¤§à¤¿à¤•à¤¾à¤° à¤° à¤®à¤¾à¤¨à¥à¤¯à¤¤à¤¾à¤•à¥‹ à¤‰à¤šà¥à¤š à¤…à¤µà¤§à¤¿à¥¤ à¤•à¥à¤¯à¤¾à¤°à¤¿à¤¯à¤° à¤ªà¥à¤°à¤—à¤¤à¤¿, à¤¨à¥‡à¤¤à¥ƒà¤¤à¥à¤µ à¤­à¥‚à¤®à¤¿à¤•à¤¾, à¤¸à¤°à¤•à¤¾à¤°à¥€ à¤µà¥à¤¯à¤µà¤¹à¤¾à¤° à¤° à¤¬à¥à¤¬à¤¾à¤¸à¤à¤— à¤¸à¤®à¥à¤¬à¤¨à¥à¤§à¤¿à¤¤ à¤®à¤¾à¤®à¤¿à¤²à¤¾à¤¹à¤°à¥‚à¤®à¤¾ à¤§à¥à¤¯à¤¾à¤¨ à¤•à¥‡à¤¨à¥à¤¦à¥à¤°à¤¿à¤¤ à¤¹à¥à¤¨à¥à¤›à¥¤ à¤†à¤¤à¥à¤®à¤¾à¤•à¥‹ à¤‰à¤¦à¥à¤¦à¥‡à¤¶à¥à¤¯ à¤¸à¥à¤ªà¤·à¥à¤Ÿ à¤¹à¥à¤¨à¥à¤›à¥¤ à¤¹à¥ƒà¤¦à¤¯ à¤° à¤œà¥€à¤µà¤¨à¤¶à¤•à¥à¤¤à¤¿à¤•à¥‹ à¤¸à¥à¤µà¤¾à¤¸à¥à¤¥à¥à¤¯à¤²à¥‡ à¤ªà¥à¤°à¤®à¥à¤–à¤¤à¤¾ à¤ªà¤¾à¤‰à¤à¤›à¥¤ à¤†à¤¤à¥à¤®à¤µà¤¿à¤¶à¥à¤µà¤¾à¤¸ à¤µà¤¿à¤•à¤¾à¤¸ à¤—à¤°à¥à¤¨ à¤° à¤¸à¤‚à¤¸à¤¾à¤°à¤®à¤¾ à¤†à¤«à¥à¤¨à¥‹ à¤ªà¤¹à¤¿à¤šà¤¾à¤¨ à¤¸à¥à¤¥à¤¾à¤ªà¤¿à¤¤ à¤—à¤°à¥à¤¨ à¤°à¤¾à¤®à¥à¤°à¥‹à¥¤"
    ),
    DASHA_INTERP_MAHADASHA_MOON(
        "An emotionally rich and intuitive period emphasizing mental peace, nurturing, and receptivity. Focus on mother, home life, public image, travel across water, and emotional well-being. Creativity and imagination flourish. Memory and connection to the past strengthen. Relationships with women and the public become significant.",
        "à¤­à¤¾à¤µà¤¨à¤¾à¤¤à¥à¤®à¤• à¤°à¥‚à¤ªà¤²à¥‡ à¤¸à¤®à¥ƒà¤¦à¥à¤§ à¤° à¤…à¤¨à¥à¤¤à¤°à¥à¤œà¥à¤žà¤¾à¤¨à¤¾à¤¤à¥à¤®à¤• à¤…à¤µà¤§à¤¿ à¤œà¤¸à¤²à¥‡ à¤®à¤¾à¤¨à¤¸à¤¿à¤• à¤¶à¤¾à¤¨à¥à¤¤à¤¿, à¤ªà¤¾à¤²à¤¨à¤ªà¥‹à¤·à¤£ à¤° à¤—à¥à¤°à¤¹à¤£à¤¶à¥€à¤²à¤¤à¤¾à¤®à¤¾ à¤œà¥‹à¤¡ à¤¦à¤¿à¤¨à¥à¤›à¥¤ à¤†à¤®à¤¾, à¤˜à¤°à¥‡à¤²à¥ à¤œà¥€à¤µà¤¨, à¤¸à¤¾à¤°à¥à¤µà¤œà¤¨à¤¿à¤• à¤›à¤µà¤¿, à¤ªà¤¾à¤¨à¥€ à¤ªà¤¾à¤° à¤¯à¤¾à¤¤à¥à¤°à¤¾ à¤° à¤­à¤¾à¤µà¤¨à¤¾à¤¤à¥à¤®à¤• à¤•à¤²à¥à¤¯à¤¾à¤£à¤®à¤¾ à¤§à¥à¤¯à¤¾à¤¨ à¤•à¥‡à¤¨à¥à¤¦à¥à¤°à¤¿à¤¤à¥¤ à¤¸à¤¿à¤°à¥à¤œà¤¨à¤¶à¥€à¤²à¤¤à¤¾ à¤° à¤•à¤²à¥à¤ªà¤¨à¤¾ à¤«à¤¸à¥à¤Ÿà¤¾à¤‰à¤à¤›à¥¤ à¤¸à¥à¤®à¥ƒà¤¤à¤¿ à¤° à¤­à¥‚à¤¤à¤•à¤¾à¤²à¤¸à¤à¤—à¤•à¥‹ à¤¸à¤®à¥à¤¬à¤¨à¥à¤§ à¤¬à¤²à¤¿à¤¯à¥‹ à¤¹à¥à¤¨à¥à¤›à¥¤ à¤®à¤¹à¤¿à¤²à¤¾à¤¹à¤°à¥‚ à¤° à¤œà¤¨à¤¤à¤¾à¤¸à¤à¤—à¤•à¥‹ à¤¸à¤®à¥à¤¬à¤¨à¥à¤§ à¤®à¤¹à¤¤à¥à¤¤à¥à¤µà¤ªà¥‚à¤°à¥à¤£ à¤¹à¥à¤¨à¥à¤›à¥¤"
    ),
    DASHA_INTERP_MAHADASHA_MARS(
        "A period of heightened energy, courage, initiative, and competitive drive. Focus on property matters, real estate, siblings, technical and engineering pursuits, sports, and surgery. Decisive action is favored. Physical vitality increases. Good for tackling challenges requiring strength and determination.",
        "à¤Šà¤°à¥à¤œà¤¾, à¤¸à¤¾à¤¹à¤¸, à¤ªà¤¹à¤² à¤° à¤ªà¥à¤°à¤¤à¤¿à¤¸à¥à¤ªà¤°à¥à¤§à¤¾à¤¤à¥à¤®à¤• à¤‰à¤¤à¥à¤ªà¥à¤°à¥‡à¤°à¤£à¤¾à¤•à¥‹ à¤‰à¤šà¥à¤š à¤…à¤µà¤§à¤¿à¥¤ à¤¸à¤®à¥à¤ªà¤¤à¥à¤¤à¤¿ à¤®à¤¾à¤®à¤¿à¤²à¤¾, à¤˜à¤°à¤œà¤—à¥à¤—à¤¾, à¤­à¤¾à¤‡à¤¬à¤¹à¤¿à¤¨à¥€, à¤ªà¥à¤°à¤¾à¤µà¤¿à¤§à¤¿à¤• à¤° à¤‡à¤¨à¥à¤œà¤¿à¤¨à¤¿à¤¯à¤°à¤¿à¤™, à¤–à¥‡à¤²à¤•à¥à¤¦ à¤° à¤¶à¤²à¥à¤¯à¤•à¥à¤°à¤¿à¤¯à¤¾à¤®à¤¾ à¤§à¥à¤¯à¤¾à¤¨ à¤•à¥‡à¤¨à¥à¤¦à¥à¤°à¤¿à¤¤à¥¤ à¤¨à¤¿à¤°à¥à¤£à¤¾à¤¯à¤• à¤•à¤¾à¤°à¥à¤¯à¤²à¤¾à¤ˆ à¤ªà¥à¤°à¤¾à¤¥à¤®à¤¿à¤•à¤¤à¤¾ à¤¦à¤¿à¤‡à¤¨à¥à¤›à¥¤ à¤¶à¤¾à¤°à¥€à¤°à¤¿à¤• à¤œà¥€à¤µà¤¨à¤¶à¤•à¥à¤¤à¤¿ à¤¬à¤¢à¥à¤›à¥¤ à¤¬à¤² à¤° à¤¦à¥ƒà¤¢ à¤¸à¤‚à¤•à¤²à¥à¤ª à¤šà¤¾à¤¹à¤¿à¤¨à¥‡ à¤šà¥à¤¨à¥Œà¤¤à¥€à¤¹à¤°à¥‚ à¤¸à¤¾à¤®à¤¨à¤¾ à¤—à¤°à¥à¤¨ à¤°à¤¾à¤®à¥à¤°à¥‹à¥¤"
    ),
    DASHA_INTERP_MAHADASHA_MERCURY(
        "A period of enhanced learning, communication, analytical thinking, and commerce. Focus on education, writing, publishing, accounting, trade, and intellectual pursuits. Social connections expand through skillful communication. Good for developing skills, starting businesses, and mastering information.",
        "à¤¸à¤¿à¤•à¤¾à¤‡, à¤¸à¤žà¥à¤šà¤¾à¤°, à¤µà¤¿à¤¶à¥à¤²à¥‡à¤·à¤£à¤¾à¤¤à¥à¤®à¤• à¤¸à¥‹à¤š à¤° à¤µà¤¾à¤£à¤¿à¤œà¥à¤¯à¤•à¥‹ à¤‰à¤¨à¥à¤¨à¤¤ à¤…à¤µà¤§à¤¿à¥¤ à¤¶à¤¿à¤•à¥à¤·à¤¾, à¤²à¥‡à¤–à¤¨, à¤ªà¥à¤°à¤•à¤¾à¤¶à¤¨, à¤²à¥‡à¤–à¤¾, à¤µà¥à¤¯à¤¾à¤ªà¤¾à¤° à¤° à¤¬à¥Œà¤¦à¥à¤§à¤¿à¤• à¤–à¥‹à¤œà¤®à¤¾ à¤§à¥à¤¯à¤¾à¤¨ à¤•à¥‡à¤¨à¥à¤¦à¥à¤°à¤¿à¤¤à¥¤ à¤¦à¤•à¥à¤· à¤¸à¤žà¥à¤šà¤¾à¤° à¤®à¤¾à¤°à¥à¤«à¤¤ à¤¸à¤¾à¤®à¤¾à¤œà¤¿à¤• à¤¸à¤®à¥à¤¬à¤¨à¥à¤§ à¤µà¤¿à¤¸à¥à¤¤à¤¾à¤° à¤¹à¥à¤¨à¥à¤›à¥¤ à¤¸à¥€à¤ª à¤µà¤¿à¤•à¤¾à¤¸, à¤µà¥à¤¯à¤µà¤¸à¤¾à¤¯ à¤¸à¥à¤°à¥ à¤—à¤°à¥à¤¨ à¤° à¤œà¤¾à¤¨à¤•à¤¾à¤°à¥€à¤®à¤¾ à¤¦à¤•à¥à¤·à¤¤à¤¾ à¤¹à¤¾à¤¸à¤¿à¤² à¤—à¤°à¥à¤¨ à¤°à¤¾à¤®à¥à¤°à¥‹à¥¤"
    ),
    DASHA_INTERP_MAHADASHA_JUPITER(
        "A period of wisdom, expansion, prosperity, and divine grace (Guru's blessings). Focus on spirituality, higher learning, teaching, children, law, and philosophical pursuits. Fortune favors righteous endeavors. Faith and optimism increase. Excellent for marriage, progeny, and spiritual advancement.",
        "à¤¬à¥à¤¦à¥à¤§à¤¿, à¤µà¤¿à¤¸à¥à¤¤à¤¾à¤°, à¤¸à¤®à¥ƒà¤¦à¥à¤§à¤¿ à¤° à¤¦à¥ˆà¤µà¥€ à¤•à¥ƒà¤ªà¤¾à¤•à¥‹ à¤…à¤µà¤§à¤¿ (à¤—à¥à¤°à¥à¤•à¥‹ à¤†à¤¶à¥€à¤°à¥à¤µà¤¾à¤¦)à¥¤ à¤†à¤§à¥à¤¯à¤¾à¤¤à¥à¤®à¤¿à¤•à¤¤à¤¾, à¤‰à¤šà¥à¤š à¤¶à¤¿à¤•à¥à¤·à¤¾, à¤¶à¤¿à¤•à¥à¤·à¤£, à¤¸à¤¨à¥à¤¤à¤¾à¤¨, à¤•à¤¾à¤¨à¥à¤¨ à¤° à¤¦à¤¾à¤°à¥à¤¶à¤¨à¤¿à¤• à¤–à¥‹à¤œà¤®à¤¾ à¤§à¥à¤¯à¤¾à¤¨ à¤•à¥‡à¤¨à¥à¤¦à¥à¤°à¤¿à¤¤à¥¤ à¤­à¤¾à¤—à¥à¤¯à¤²à¥‡ à¤§à¤¾à¤°à¥à¤®à¤¿à¤• à¤ªà¥à¤°à¤¯à¤¾à¤¸à¤¹à¤°à¥‚à¤²à¤¾à¤ˆ à¤¸à¤¾à¤¥ à¤¦à¤¿à¤¨à¥à¤›à¥¤ à¤µà¤¿à¤¶à¥à¤µà¤¾à¤¸ à¤° à¤†à¤¶à¤¾à¤µà¤¾à¤¦ à¤¬à¤¢à¥à¤›à¥¤ à¤µà¤¿à¤µà¤¾à¤¹, à¤¸à¤¨à¥à¤¤à¤¾à¤¨ à¤° à¤†à¤§à¥à¤¯à¤¾à¤¤à¥à¤®à¤¿à¤• à¤ªà¥à¤°à¤—à¤¤à¤¿à¤•à¥‹ à¤²à¤¾à¤—à¤¿ à¤‰à¤¤à¥à¤•à¥ƒà¤·à¥à¤Ÿà¥¤"
    ),
    DASHA_INTERP_MAHADASHA_VENUS(
        "A period of luxury, beauty, relationships, artistic expression, and material comforts. Focus on marriage, partnerships, arts, music, dance, vehicles, jewelry, and sensory pleasures. Creativity and romance blossom. Refinement in all areas of life. Good for enhancing beauty, wealth, and experiencing life's pleasures.",
        "à¤µà¤¿à¤²à¤¾à¤¸à¤¿à¤¤à¤¾, à¤¸à¥Œà¤¨à¥à¤¦à¤°à¥à¤¯, à¤¸à¤®à¥à¤¬à¤¨à¥à¤§, à¤•à¤²à¤¾à¤¤à¥à¤®à¤• à¤…à¤­à¤¿à¤µà¥à¤¯à¤•à¥à¤¤à¤¿ à¤° à¤­à¥Œà¤¤à¤¿à¤• à¤¸à¥à¤µà¤¿à¤§à¤¾à¤¹à¤°à¥‚à¤•à¥‹ à¤…à¤µà¤§à¤¿à¥¤ à¤µà¤¿à¤µà¤¾à¤¹, à¤¸à¤¾à¤à¥‡à¤¦à¤¾à¤°à¥€, à¤•à¤²à¤¾, à¤¸à¤™à¥à¤—à¥€à¤¤, à¤¨à¥ƒà¤¤à¥à¤¯, à¤¸à¤µà¤¾à¤°à¥€à¤¸à¤¾à¤§à¤¨, à¤—à¤¹à¤¨à¤¾ à¤° à¤‡à¤¨à¥à¤¦à¥à¤°à¤¿à¤¯ à¤†à¤¨à¤¨à¥à¤¦à¤®à¤¾ à¤§à¥à¤¯à¤¾à¤¨ à¤•à¥‡à¤¨à¥à¤¦à¥à¤°à¤¿à¤¤à¥¤ à¤¸à¤¿à¤°à¥à¤œà¤¨à¤¶à¥€à¤²à¤¤à¤¾ à¤° à¤°à¥‹à¤®à¤¾à¤¨à¥à¤¸ à¤«à¤¸à¥à¤Ÿà¤¾à¤‰à¤à¤›à¥¤ à¤œà¥€à¤µà¤¨à¤•à¤¾ à¤¸à¤¬à¥ˆ à¤•à¥à¤·à¥‡à¤¤à¥à¤°à¤®à¤¾ à¤ªà¤°à¤¿à¤·à¥à¤•à¤°à¤£à¥¤ à¤¸à¥Œà¤¨à¥à¤¦à¤°à¥à¤¯, à¤¸à¤®à¥à¤ªà¤¤à¥à¤¤à¤¿ à¤¬à¤¢à¤¾à¤‰à¤¨ à¤° à¤œà¥€à¤µà¤¨à¤•à¤¾ à¤†à¤¨à¤¨à¥à¤¦à¤¹à¤°à¥‚ à¤…à¤¨à¥à¤­à¤µ à¤—à¤°à¥à¤¨ à¤°à¤¾à¤®à¥à¤°à¥‹à¥¤"
    ),
    DASHA_INTERP_MAHADASHA_SATURN(
        "A period of discipline, karmic lessons, perseverance, and structural growth. Focus on service, responsibility, hard work, long-term projects, and lessons through patience. Delays and obstacles ultimately lead to lasting success and maturity. Time to build solid foundations and pay karmic debts.",
        "à¤…à¤¨à¥à¤¶à¤¾à¤¸à¤¨, à¤•à¤¾à¤°à¥à¤®à¤¿à¤• à¤ªà¤¾à¤ , à¤¦à¥ƒà¤¢à¤¤à¤¾ à¤° à¤¸à¤‚à¤°à¤šà¤¨à¤¾à¤¤à¥à¤®à¤• à¤µà¤¿à¤•à¤¾à¤¸à¤•à¥‹ à¤…à¤µà¤§à¤¿à¥¤ à¤¸à¥‡à¤µà¤¾, à¤œà¤¿à¤®à¥à¤®à¥‡à¤µà¤¾à¤°à¥€, à¤•à¤ à¤¿à¤¨ à¤ªà¤°à¤¿à¤¶à¥à¤°à¤®, à¤¦à¥€à¤°à¥à¤˜à¤•à¤¾à¤²à¥€à¤¨ à¤ªà¤°à¤¿à¤¯à¥‹à¤œà¤¨à¤¾ à¤° à¤§à¥ˆà¤°à¥à¤¯ à¤®à¤¾à¤°à¥à¤«à¤¤ à¤ªà¤¾à¤ à¤¹à¤°à¥‚à¤®à¤¾ à¤§à¥à¤¯à¤¾à¤¨ à¤•à¥‡à¤¨à¥à¤¦à¥à¤°à¤¿à¤¤à¥¤ à¤¢à¤¿à¤²à¤¾à¤‡ à¤° à¤¬à¤¾à¤§à¤¾à¤¹à¤°à¥‚à¤²à¥‡ à¤…à¤¨à¥à¤¤à¤¤à¤ƒ à¤¸à¥à¤¥à¤¾à¤¯à¥€ à¤¸à¤«à¤²à¤¤à¤¾ à¤° à¤ªà¤°à¤¿à¤ªà¤•à¥à¤µà¤¤à¤¾ à¤²à¥à¤¯à¤¾à¤‰à¤à¤›à¥¤ à¤ à¥‹à¤¸ à¤†à¤§à¤¾à¤° à¤¨à¤¿à¤°à¥à¤®à¤¾à¤£ à¤—à¤°à¥à¤¨à¥‡ à¤° à¤•à¤¾à¤°à¥à¤®à¤¿à¤• à¤‹à¤£ à¤šà¥à¤•à¥à¤¤à¤¾ à¤—à¤°à¥à¤¨à¥‡ à¤¸à¤®à¤¯à¥¤"
    ),
    DASHA_INTERP_MAHADASHA_RAHU(
        "A period of intense worldly ambition, unconventional paths, and material desires. Focus on foreign connections, technology, innovation, and breaking traditional boundaries. Sudden opportunities and unexpected changes arise. Material gains through unusual or non-traditional means. Beware of illusions.",
        "à¤¤à¥€à¤µà¥à¤° à¤¸à¤¾à¤‚à¤¸à¤¾à¤°à¤¿à¤• à¤®à¤¹à¤¤à¥à¤¤à¥à¤µà¤¾à¤•à¤¾à¤‚à¤•à¥à¤·à¤¾, à¤…à¤ªà¤°à¤®à¥à¤ªà¤°à¤¾à¤—à¤¤ à¤®à¤¾à¤°à¥à¤— à¤° à¤­à¥Œà¤¤à¤¿à¤• à¤‡à¤šà¥à¤›à¤¾à¤•à¥‹ à¤…à¤µà¤§à¤¿à¥¤ à¤µà¤¿à¤¦à¥‡à¤¶à¥€ à¤¸à¤®à¥à¤¬à¤¨à¥à¤§, à¤ªà¥à¤°à¤µà¤¿à¤§à¤¿, à¤¨à¤µà¥€à¤¨à¤¤à¤¾ à¤° à¤ªà¤°à¤®à¥à¤ªà¤°à¤¾à¤—à¤¤ à¤¸à¥€à¤®à¤¾à¤¹à¤°à¥‚ à¤¤à¥‹à¤¡à¥à¤¨à¤®à¤¾ à¤§à¥à¤¯à¤¾à¤¨ à¤•à¥‡à¤¨à¥à¤¦à¥à¤°à¤¿à¤¤à¥¤ à¤…à¤šà¤¾à¤¨à¤• à¤…à¤µà¤¸à¤°à¤¹à¤°à¥‚ à¤° à¤…à¤ªà¥à¤°à¤¤à¥à¤¯à¤¾à¤¶à¤¿à¤¤ à¤ªà¤°à¤¿à¤µà¤°à¥à¤¤à¤¨à¤¹à¤°à¥‚ à¤†à¤‰à¤à¤›à¤¨à¥à¥¤ à¤…à¤¸à¤¾à¤®à¤¾à¤¨à¥à¤¯ à¤µà¤¾ à¤—à¥ˆà¤°-à¤ªà¤°à¤®à¥à¤ªà¤°à¤¾à¤—à¤¤ à¤®à¤¾à¤§à¥à¤¯à¤®à¤¬à¤¾à¤Ÿ à¤­à¥Œà¤¤à¤¿à¤• à¤²à¤¾à¤­à¥¤ à¤­à¥à¤°à¤®à¤¬à¤¾à¤Ÿ à¤¸à¤¾à¤µà¤§à¤¾à¤¨ à¤°à¤¹à¤¨à¥à¤¹à¥‹à¤¸à¥à¥¤"
    ),
    DASHA_INTERP_MAHADASHA_KETU(
        "A period of spirituality, detachment, and profound inner transformation. Focus on liberation (moksha), occult research, healing practices, and resolving past-life karma. Deep introspection yields spiritual insights. Material attachments may dissolve. Excellent for meditation, research, and spiritual practices.",
        "à¤†à¤§à¥à¤¯à¤¾à¤¤à¥à¤®à¤¿à¤•à¤¤à¤¾, à¤µà¥ˆà¤°à¤¾à¤—à¥à¤¯ à¤° à¤—à¤¹à¤¿à¤°à¥‹ à¤†à¤¨à¥à¤¤à¤°à¤¿à¤• à¤°à¥‚à¤ªà¤¾à¤¨à¥à¤¤à¤°à¤£à¤•à¥‹ à¤…à¤µà¤§à¤¿à¥¤ à¤®à¥à¤•à¥à¤¤à¤¿ (à¤®à¥‹à¤•à¥à¤·), à¤¤à¤¾à¤¨à¥à¤¤à¥à¤°à¤¿à¤• à¤…à¤¨à¥à¤¸à¤¨à¥à¤§à¤¾à¤¨, à¤‰à¤ªà¤šà¤¾à¤° à¤…à¤­à¥à¤¯à¤¾à¤¸ à¤° à¤ªà¥‚à¤°à¥à¤µà¤œà¤¨à¥à¤®à¤•à¥‹ à¤•à¤°à¥à¤® à¤¸à¤®à¤¾à¤§à¤¾à¤¨à¤®à¤¾ à¤§à¥à¤¯à¤¾à¤¨ à¤•à¥‡à¤¨à¥à¤¦à¥à¤°à¤¿à¤¤à¥¤ à¤—à¤¹à¤¿à¤°à¥‹ à¤†à¤¤à¥à¤®à¤¨à¤¿à¤°à¥€à¤•à¥à¤·à¤£à¤²à¥‡ à¤†à¤§à¥à¤¯à¤¾à¤¤à¥à¤®à¤¿à¤• à¤…à¤¨à¥à¤¤à¤°à¥à¤¦à¥ƒà¤·à¥à¤Ÿà¤¿ à¤¦à¤¿à¤¨à¥à¤›à¥¤ à¤­à¥Œà¤¤à¤¿à¤• à¤†à¤¸à¤•à¥à¤¤à¤¿à¤¹à¤°à¥‚ à¤µà¤¿à¤²à¥€à¤¨ à¤¹à¥à¤¨ à¤¸à¤•à¥à¤›à¤¨à¥à¥¤ à¤§à¥à¤¯à¤¾à¤¨, à¤…à¤¨à¥à¤¸à¤¨à¥à¤§à¤¾à¤¨ à¤° à¤†à¤§à¥à¤¯à¤¾à¤¤à¥à¤®à¤¿à¤• à¤…à¤­à¥à¤¯à¤¾à¤¸à¤•à¥‹ à¤²à¤¾à¤—à¤¿ à¤‰à¤¤à¥à¤•à¥ƒà¤·à¥à¤Ÿà¥¤"
    ),
    DASHA_INTERP_MAHADASHA_DEFAULT(
        "A period of transformation and karmic unfolding according to planetary influences.",
        "à¤—à¥à¤°à¤¹à¥€à¤¯ à¤ªà¥à¤°à¤­à¤¾à¤µ à¤…à¤¨à¥à¤¸à¤¾à¤° à¤°à¥‚à¤ªà¤¾à¤¨à¥à¤¤à¤°à¤£ à¤° à¤•à¤¾à¤°à¥à¤®à¤¿à¤• à¤µà¤¿à¤•à¤¾à¤¸à¤•à¥‹ à¤…à¤µà¤§à¤¿à¥¤"
    ),

    // ============================================
    // DASHA ANTARDASHA INTERPRETATIONS
    // ============================================
    DASHA_INTERP_ANTARDASHA_SUN(
        "Current sub-period (Bhukti) activates themes of authority, self-confidence, recognition, and dealings with father figures or government. Leadership opportunities may arise.",
        "à¤¹à¤¾à¤²à¤•à¥‹ à¤‰à¤ª-à¤…à¤µà¤§à¤¿ (à¤­à¥à¤•à¥à¤¤à¤¿) à¤²à¥‡ à¤…à¤§à¤¿à¤•à¤¾à¤°, à¤†à¤¤à¥à¤®à¤µà¤¿à¤¶à¥à¤µà¤¾à¤¸, à¤®à¤¾à¤¨à¥à¤¯à¤¤à¤¾ à¤° à¤¬à¥à¤¬à¤¾à¤•à¤¾ à¤µà¥à¤¯à¤•à¥à¤¤à¤¿ à¤µà¤¾ à¤¸à¤°à¤•à¤¾à¤°à¤¸à¤à¤—à¤•à¥‹ à¤µà¥à¤¯à¤µà¤¹à¤¾à¤°à¤•à¤¾ à¤µà¤¿à¤·à¤¯à¤¹à¤°à¥‚ à¤¸à¤•à¥à¤°à¤¿à¤¯ à¤—à¤°à¥à¤›à¥¤ à¤¨à¥‡à¤¤à¥ƒà¤¤à¥à¤µ à¤…à¤µà¤¸à¤°à¤¹à¤°à¥‚ à¤†à¤‰à¤¨ à¤¸à¤•à¥à¤›à¤¨à¥à¥¤"
    ),
    DASHA_INTERP_ANTARDASHA_MOON(
        "Current sub-period emphasizes emotional matters, mental peace, mother, public image, domestic affairs, and connection with women. Intuition heightens.",
        "à¤¹à¤¾à¤²à¤•à¥‹ à¤‰à¤ª-à¤…à¤µà¤§à¤¿à¤²à¥‡ à¤­à¤¾à¤µà¤¨à¤¾à¤¤à¥à¤®à¤• à¤®à¤¾à¤®à¤¿à¤²à¤¾, à¤®à¤¾à¤¨à¤¸à¤¿à¤• à¤¶à¤¾à¤¨à¥à¤¤à¤¿, à¤†à¤®à¤¾, à¤¸à¤¾à¤°à¥à¤µà¤œà¤¨à¤¿à¤• à¤›à¤µà¤¿, à¤˜à¤°à¥‡à¤²à¥ à¤®à¤¾à¤®à¤¿à¤²à¤¾ à¤° à¤®à¤¹à¤¿à¤²à¤¾à¤¹à¤°à¥‚à¤¸à¤à¤—à¤•à¥‹ à¤¸à¤®à¥à¤¬à¤¨à¥à¤§à¤®à¤¾ à¤œà¥‹à¤¡ à¤¦à¤¿à¤¨à¥à¤›à¥¤ à¤…à¤¨à¥à¤¤à¤°à¥à¤œà¥à¤žà¤¾à¤¨ à¤¤à¥€à¤µà¥à¤° à¤¹à¥à¤¨à¥à¤›à¥¤"
    ),
    DASHA_INTERP_ANTARDASHA_MARS(
        "Current sub-period brings increased energy, drive for action, courage, and matters involving property, siblings, competition, or technical endeavors.",
        "à¤¹à¤¾à¤²à¤•à¥‹ à¤‰à¤ª-à¤…à¤µà¤§à¤¿à¤²à¥‡ à¤¬à¤¢à¥‡à¤•à¥‹ à¤Šà¤°à¥à¤œà¤¾, à¤•à¤¾à¤°à¥à¤¯à¤•à¥‹ à¤²à¤¾à¤—à¤¿ à¤‰à¤¤à¥à¤ªà¥à¤°à¥‡à¤°à¤£à¤¾, à¤¸à¤¾à¤¹à¤¸ à¤° à¤¸à¤®à¥à¤ªà¤¤à¥à¤¤à¤¿, à¤­à¤¾à¤‡à¤¬à¤¹à¤¿à¤¨à¥€, à¤ªà¥à¤°à¤¤à¤¿à¤¸à¥à¤ªà¤°à¥à¤§à¤¾ à¤µà¤¾ à¤ªà¥à¤°à¤¾à¤µà¤¿à¤§à¤¿à¤• à¤ªà¥à¤°à¤¯à¤¾à¤¸à¤•à¤¾ à¤®à¤¾à¤®à¤¿à¤²à¤¾à¤¹à¤°à¥‚ à¤²à¥à¤¯à¤¾à¤‰à¤à¤›à¥¤"
    ),
    DASHA_INTERP_ANTARDASHA_MERCURY(
        "Current sub-period emphasizes communication, learning, business transactions, intellectual activities, and connections with younger people or merchants.",
        "à¤¹à¤¾à¤²à¤•à¥‹ à¤‰à¤ª-à¤…à¤µà¤§à¤¿à¤²à¥‡ à¤¸à¤žà¥à¤šà¤¾à¤°, à¤¸à¤¿à¤•à¤¾à¤‡, à¤µà¥à¤¯à¤¾à¤ªà¤¾à¤°à¤¿à¤• à¤²à¥‡à¤¨à¤¦à¥‡à¤¨, à¤¬à¥Œà¤¦à¥à¤§à¤¿à¤• à¤—à¤¤à¤¿à¤µà¤¿à¤§à¤¿à¤¹à¤°à¥‚ à¤° à¤¸à¤¾à¤¨à¤¾ à¤®à¤¾à¤¨à¤¿à¤¸à¤¹à¤°à¥‚ à¤µà¤¾ à¤µà¥à¤¯à¤¾à¤ªà¤¾à¤°à¥€à¤¹à¤°à¥‚à¤¸à¤à¤—à¤•à¥‹ à¤¸à¤®à¥à¤¬à¤¨à¥à¤§à¤®à¤¾ à¤œà¥‹à¤¡ à¤¦à¤¿à¤¨à¥à¤›à¥¤"
    ),
    DASHA_INTERP_ANTARDASHA_JUPITER(
        "Current sub-period brings wisdom, expansion, good fortune, and focus on spirituality, teachers, children, higher education, or legal matters.",
        "à¤¹à¤¾à¤²à¤•à¥‹ à¤‰à¤ª-à¤…à¤µà¤§à¤¿à¤²à¥‡ à¤¬à¥à¤¦à¥à¤§à¤¿, à¤µà¤¿à¤¸à¥à¤¤à¤¾à¤°, à¤¸à¥à¤­à¤¾à¤—à¥à¤¯ à¤° à¤†à¤§à¥à¤¯à¤¾à¤¤à¥à¤®à¤¿à¤•à¤¤à¤¾, à¤¶à¤¿à¤•à¥à¤·à¤•à¤¹à¤°à¥‚, à¤¸à¤¨à¥à¤¤à¤¾à¤¨, à¤‰à¤šà¥à¤š à¤¶à¤¿à¤•à¥à¤·à¤¾ à¤µà¤¾ à¤•à¤¾à¤¨à¥à¤¨à¥€ à¤®à¤¾à¤®à¤¿à¤²à¤¾à¤¹à¤°à¥‚à¤®à¤¾ à¤§à¥à¤¯à¤¾à¤¨ à¤•à¥‡à¤¨à¥à¤¦à¥à¤°à¤¿à¤¤ à¤—à¤°à¥à¤›à¥¤"
    ),
    DASHA_INTERP_ANTARDASHA_VENUS(
        "Current sub-period emphasizes relationships, romance, creativity, luxury, artistic pursuits, material comforts, and partnership matters.",
        "à¤¹à¤¾à¤²à¤•à¥‹ à¤‰à¤ª-à¤…à¤µà¤§à¤¿à¤²à¥‡ à¤¸à¤®à¥à¤¬à¤¨à¥à¤§, à¤°à¥‹à¤®à¤¾à¤¨à¥à¤¸, à¤¸à¤¿à¤°à¥à¤œà¤¨à¤¶à¥€à¤²à¤¤à¤¾, à¤µà¤¿à¤²à¤¾à¤¸à¤¿à¤¤à¤¾, à¤•à¤²à¤¾à¤¤à¥à¤®à¤• à¤–à¥‹à¤œ, à¤­à¥Œà¤¤à¤¿à¤• à¤¸à¥à¤µà¤¿à¤§à¤¾ à¤° à¤¸à¤¾à¤à¥‡à¤¦à¤¾à¤°à¥€ à¤®à¤¾à¤®à¤¿à¤²à¤¾à¤¹à¤°à¥‚à¤®à¤¾ à¤œà¥‹à¤¡ à¤¦à¤¿à¤¨à¥à¤›à¥¤"
    ),
    DASHA_INTERP_ANTARDASHA_SATURN(
        "Current sub-period brings discipline, responsibility, hard work, delays, and lessons requiring patience. Focus on service and long-term efforts.",
        "à¤¹à¤¾à¤²à¤•à¥‹ à¤‰à¤ª-à¤…à¤µà¤§à¤¿à¤²à¥‡ à¤…à¤¨à¥à¤¶à¤¾à¤¸à¤¨, à¤œà¤¿à¤®à¥à¤®à¥‡à¤µà¤¾à¤°à¥€, à¤•à¤ à¤¿à¤¨ à¤ªà¤°à¤¿à¤¶à¥à¤°à¤®, à¤¢à¤¿à¤²à¤¾à¤‡ à¤° à¤§à¥ˆà¤°à¥à¤¯ à¤šà¤¾à¤¹à¤¿à¤¨à¥‡ à¤ªà¤¾à¤ à¤¹à¤°à¥‚ à¤²à¥à¤¯à¤¾à¤‰à¤à¤›à¥¤ à¤¸à¥‡à¤µà¤¾ à¤° à¤¦à¥€à¤°à¥à¤˜à¤•à¤¾à¤²à¥€à¤¨ à¤ªà¥à¤°à¤¯à¤¾à¤¸à¤®à¤¾ à¤§à¥à¤¯à¤¾à¤¨ à¤•à¥‡à¤¨à¥à¤¦à¥à¤°à¤¿à¤¤à¥¤"
    ),
    DASHA_INTERP_ANTARDASHA_RAHU(
        "Current sub-period emphasizes worldly ambitions, unconventional approaches, foreign matters, technology, and sudden changes or opportunities.",
        "à¤¹à¤¾à¤²à¤•à¥‹ à¤‰à¤ª-à¤…à¤µà¤§à¤¿à¤²à¥‡ à¤¸à¤¾à¤‚à¤¸à¤¾à¤°à¤¿à¤• à¤®à¤¹à¤¤à¥à¤¤à¥à¤µà¤¾à¤•à¤¾à¤‚à¤•à¥à¤·à¤¾, à¤…à¤ªà¤°à¤®à¥à¤ªà¤°à¤¾à¤—à¤¤ à¤¦à¥ƒà¤·à¥à¤Ÿà¤¿à¤•à¥‹à¤£, à¤µà¤¿à¤¦à¥‡à¤¶à¥€ à¤®à¤¾à¤®à¤¿à¤²à¤¾, à¤ªà¥à¤°à¤µà¤¿à¤§à¤¿ à¤° à¤…à¤šà¤¾à¤¨à¤• à¤ªà¤°à¤¿à¤µà¤°à¥à¤¤à¤¨ à¤µà¤¾ à¤…à¤µà¤¸à¤°à¤¹à¤°à¥‚à¤®à¤¾ à¤œà¥‹à¤¡ à¤¦à¤¿à¤¨à¥à¤›à¥¤"
    ),
    DASHA_INTERP_ANTARDASHA_KETU(
        "Current sub-period brings spiritual insights, detachment, introspection, research, and resolution of past karmic patterns. Material concerns recede.",
        "à¤¹à¤¾à¤²à¤•à¥‹ à¤‰à¤ª-à¤…à¤µà¤§à¤¿à¤²à¥‡ à¤†à¤§à¥à¤¯à¤¾à¤¤à¥à¤®à¤¿à¤• à¤…à¤¨à¥à¤¤à¤°à¥à¤¦à¥ƒà¤·à¥à¤Ÿà¤¿, à¤µà¥ˆà¤°à¤¾à¤—à¥à¤¯, à¤†à¤¤à¥à¤®à¤¨à¤¿à¤°à¥€à¤•à¥à¤·à¤£, à¤…à¤¨à¥à¤¸à¤¨à¥à¤§à¤¾à¤¨ à¤° à¤ªà¥‚à¤°à¥à¤µ à¤•à¤¾à¤°à¥à¤®à¤¿à¤• à¤¢à¤¾à¤à¤šà¤¾à¤•à¥‹ à¤¸à¤®à¤¾à¤§à¤¾à¤¨ à¤²à¥à¤¯à¤¾à¤‰à¤à¤›à¥¤ à¤­à¥Œà¤¤à¤¿à¤• à¤šà¤¿à¤¨à¥à¤¤à¤¾à¤¹à¤°à¥‚ à¤ªà¤›à¤¾à¤¡à¤¿ à¤¹à¤Ÿà¥à¤›à¤¨à¥à¥¤"
    ),
    DASHA_INTERP_ANTARDASHA_DEFAULT(
        "Current sub-period brings mixed planetary influences requiring careful navigation.",
        "à¤¹à¤¾à¤²à¤•à¥‹ à¤‰à¤ª-à¤…à¤µà¤§à¤¿à¤²à¥‡ à¤¸à¤¾à¤µà¤§à¤¾à¤¨à¥€à¤ªà¥‚à¤°à¥à¤µà¤• à¤®à¤¾à¤°à¥à¤—à¤¦à¤°à¥à¤¶à¤¨ à¤šà¤¾à¤¹à¤¿à¤¨à¥‡ à¤®à¤¿à¤¶à¥à¤°à¤¿à¤¤ à¤—à¥à¤°à¤¹à¥€à¤¯ à¤ªà¥à¤°à¤­à¤¾à¤µà¤¹à¤°à¥‚ à¤²à¥à¤¯à¤¾à¤‰à¤à¤›à¥¤"
    ),

    // ============================================
    // CHART ANALYSIS SCREEN - TABS & UI
    // ============================================
    ANALYSIS_CHART_ANALYSIS("Chart Analysis", "à¤•à¥à¤£à¥à¤¡à¤²à¥€ à¤µà¤¿à¤¶à¥à¤²à¥‡à¤·à¤£"),
    ANALYSIS_TAB_CHART("Chart", "à¤•à¥à¤£à¥à¤¡à¤²à¥€"),
    ANALYSIS_TAB_PLANETS("Planets", "à¤—à¥à¤°à¤¹à¤¹à¤°à¥‚"),
    ANALYSIS_TAB_YOGAS("Yogas", "à¤¯à¥‹à¤—à¤¹à¤°à¥‚"),
    ANALYSIS_TAB_DASHAS("Dashas", "à¤¦à¤¶à¤¾à¤¹à¤°à¥‚"),
    ANALYSIS_TAB_TRANSITS("Transits", "à¤—à¥‹à¤šà¤°à¤¹à¤°à¥‚"),
    ANALYSIS_TAB_ASHTAKAVARGA("Ashtakavarga", "à¤…à¤·à¥à¤Ÿà¤•à¤µà¤°à¥à¤—"),
    ANALYSIS_TAB_PANCHANGA("Panchanga", "à¤ªà¤žà¥à¤šà¤¾à¤™à¥à¤—"),

    // ============================================
    // DIVISIONAL CHARTS - NAMES & DESCRIPTIONS
    // ============================================
    VARGA_D1_NAME("Lagna Chart (Rashi)", "à¤²à¤—à¥à¤¨ à¤•à¥à¤£à¥à¤¡à¤²à¥€ (à¤°à¤¾à¤¶à¤¿)"),
    VARGA_D2_NAME("Hora Chart", "à¤¹à¥‹à¤°à¤¾ à¤•à¥à¤£à¥à¤¡à¤²à¥€"),
    VARGA_D3_NAME("Drekkana Chart", "à¤¦à¥à¤°à¥‡à¤•à¥à¤•à¤¾à¤£ à¤•à¥à¤£à¥à¤¡à¤²à¥€"),
    VARGA_D4_NAME("Chaturthamsa Chart", "à¤šà¤¤à¥à¤°à¥à¤¥à¤¾à¤‚à¤¶ à¤•à¥à¤£à¥à¤¡à¤²à¥€"),
    VARGA_D7_NAME("Saptamsa Chart", "à¤¸à¤ªà¥à¤¤à¤¾à¤‚à¤¶ à¤•à¥à¤£à¥à¤¡à¤²à¥€"),
    VARGA_D9_NAME("Navamsa Chart", "à¤¨à¤µà¤¾à¤‚à¤¶ à¤•à¥à¤£à¥à¤¡à¤²à¥€"),
    VARGA_D10_NAME("Dasamsa Chart", "à¤¦à¤¶à¤¾à¤‚à¤¶ à¤•à¥à¤£à¥à¤¡à¤²à¥€"),
    VARGA_D12_NAME("Dwadasamsa Chart", "à¤¦à¥à¤µà¤¾à¤¦à¤¶à¤¾à¤‚à¤¶ à¤•à¥à¤£à¥à¤¡à¤²à¥€"),
    VARGA_D16_NAME("Shodasamsa Chart", "à¤·à¥‹à¤¡à¤¶à¤¾à¤‚à¤¶ à¤•à¥à¤£à¥à¤¡à¤²à¥€"),
    VARGA_D20_NAME("Vimsamsa Chart", "à¤µà¤¿à¤‚à¤¶à¤¾à¤‚à¤¶ à¤•à¥à¤£à¥à¤¡à¤²à¥€"),
    VARGA_D24_NAME("Siddhamsa Chart", "à¤šà¤¤à¥à¤°à¥à¤µà¤¿à¤‚à¤¶à¤¾à¤‚à¤¶ à¤•à¥à¤£à¥à¤¡à¤²à¥€"),
    VARGA_D27_NAME("Bhamsa Chart", "à¤¸à¤ªà¥à¤¤à¤µà¤¿à¤‚à¤¶à¤¾à¤‚à¤¶ à¤•à¥à¤£à¥à¤¡à¤²à¥€"),
    VARGA_D30_NAME("Trimsamsa Chart", "à¤¤à¥à¤°à¤¿à¤‚à¤¶à¤¾à¤‚à¤¶ à¤•à¥à¤£à¥à¤¡à¤²à¥€"),
    VARGA_D60_NAME("Shashtiamsa Chart", "à¤·à¤·à¥à¤Ÿà¤¿à¤¾à¤‚à¤¶ à¤•à¥à¤£à¥à¤¡à¤²à¥€"),

    VARGA_D3_DESC_FULL("Siblings, Courage, Vitality", "à¤­à¤¾à¤‡à¤¬à¤¹à¤¿à¤¨à¥€, à¤¸à¤¾à¤¹à¤¸, à¤œà¥€à¤µà¤¨à¤¶à¤•à¥à¤¤à¤¿"),
    VARGA_D9_DESC_FULL("Marriage, Dharma, Fortune", "à¤µà¤¿à¤µà¤¾à¤¹, à¤§à¤°à¥à¤®, à¤­à¤¾à¤—à¥à¤¯"),
    VARGA_D10_DESC_FULL("Career, Profession", "à¤•à¥à¤¯à¤¾à¤°à¤¿à¤¯à¤°, à¤ªà¥‡à¤¶à¤¾"),
    VARGA_D12_DESC_FULL("Parents, Ancestry", "à¤†à¤®à¤¾à¤¬à¥à¤¬à¤¾, à¤ªà¥à¤°à¥à¤–à¤¾"),
    VARGA_D16_DESC_FULL("Vehicles, Pleasures", "à¤¸à¤µà¤¾à¤°à¥€, à¤†à¤¨à¤¨à¥à¤¦"),
    VARGA_D20_DESC_FULL("Spiritual Life", "à¤†à¤§à¥à¤¯à¤¾à¤¤à¥à¤®à¤¿à¤• à¤œà¥€à¤µà¤¨"),
    VARGA_D24_DESC_FULL("Education, Learning", "à¤¶à¤¿à¤•à¥à¤·à¤¾, à¤¸à¤¿à¤•à¤¾à¤‡"),
    VARGA_D27_DESC_FULL("Strength, Weakness", "à¤¬à¤², à¤•à¤®à¤œà¥‹à¤°à¥€"),
    VARGA_D30_DESC_FULL("Evils, Misfortunes", "à¤¦à¥à¤°à¥à¤­à¤¾à¤—à¥à¤¯, à¤µà¤¿à¤ªà¤¤à¥à¤¤à¤¿"),
    VARGA_D60_DESC_FULL("Past Life Karma", "à¤ªà¥‚à¤°à¥à¤µà¤œà¤¨à¥à¤®à¤•à¥‹ à¤•à¤°à¥à¤®"),

    // Divisional chart selector labels
    VARGA_LAGNA("Lagna", "à¤²à¤—à¥à¤¨"),
    VARGA_HORA("Hora", "à¤¹à¥‹à¤°à¤¾"),
    VARGA_DREKKANA("Drekkana", "à¤¦à¥à¤°à¥‡à¤•à¥à¤•à¤¾à¤£"),
    VARGA_SAPTAMSA("Saptamsa", "à¤¸à¤ªà¥à¤¤à¤¾à¤‚à¤¶"),
    VARGA_NAVAMSA("Navamsa", "à¤¨à¤µà¤¾à¤‚à¤¶"),
    VARGA_DASAMSA("Dasamsa", "à¤¦à¤¶à¤¾à¤‚à¤¶"),
    VARGA_BHAMSA("Bhamsa", "à¤­à¤¾à¤‚à¤¶"),

    // ============================================
    // PLANETS TAB - HARDCODED STRINGS
    // ============================================
    PLANETS_CONDITIONS("Planetary Conditions", "à¤—à¥à¤°à¤¹ à¤…à¤µà¤¸à¥à¤¥à¤¾à¤¹à¤°à¥‚"),
    PLANETS_RETROGRADE("Retrograde", "à¤µà¤•à¥à¤°à¥€"),
    PLANETS_COMBUST("Combust", "à¤…à¤¸à¥à¤¤"),
    PLANETS_AT_WAR("At War", "à¤¯à¥à¤¦à¥à¤§à¤®à¤¾"),
    PLANETS_PLANETARY_WAR("Planetary War", "à¤—à¥à¤°à¤¹à¤¯à¥à¤¦à¥à¤§"),
    PLANETS_SHADBALA_SUMMARY("Shadbala Summary", "à¤·à¤¡à¥à¤¬à¤² à¤¸à¤¾à¤°à¤¾à¤‚à¤¶"),
    PLANETS_OVERALL("Overall", "à¤¸à¤®à¤—à¥à¤°"),
    PLANETS_VIEW_DETAILS("View Details", "à¤µà¤¿à¤µà¤°à¤£ à¤¹à¥‡à¤°à¥à¤¨à¥à¤¹à¥‹à¤¸à¥"),
    PLANETS_TAP_FOR_DETAILS("Tap for details", "à¤µà¤¿à¤µà¤°à¤£à¤•à¥‹ à¤²à¤¾à¤—à¤¿ à¤Ÿà¥à¤¯à¤¾à¤ª à¤—à¤°à¥à¤¨à¥à¤¹à¥‹à¤¸à¥"),
    PLANETS_SHADBALA("Shadbala", "à¤·à¤¡à¥à¤¬à¤²"),
    PLANETS_RUPAS("%s / %s rupas (%s%%)", "%s / %s à¤°à¥à¤ªà¤¾ (%s%%)"),
    PLANETS_HOUSE_FORMAT("House %d", "à¤­à¤¾à¤µ %d"),

    // Dignity status
    DIGNITY_EXALTED_STATUS("Exalted", "à¤‰à¤šà¥à¤š"),
    DIGNITY_DEBILITATED_STATUS("Debilitated", "à¤¨à¥€à¤š"),
    DIGNITY_OWN_SIGN_STATUS("Own Sign", "à¤¸à¥à¤µà¤°à¤¾à¤¶à¤¿"),
    DIGNITY_NEUTRAL_STATUS("Neutral", "à¤¤à¤Ÿà¤¸à¥à¤¥"),

    // ============================================
    // ASHTAKAVARGA TAB - HARDCODED STRINGS
    // ============================================
    ASHTAK_SUMMARY("Ashtakavarga Summary", "à¤…à¤·à¥à¤Ÿà¤•à¤µà¤°à¥à¤— à¤¸à¤¾à¤°à¤¾à¤‚à¤¶"),
    ASHTAK_TOTAL_SAV("Total SAV", "à¤•à¥à¤² SAV"),
    ASHTAK_STRONGEST("Strongest", "à¤¸à¤¬à¥ˆà¤­à¤¨à¥à¤¦à¤¾ à¤¬à¤²à¤¿à¤¯à¥‹"),
    ASHTAK_WEAKEST("Weakest", "à¤¸à¤¬à¥ˆà¤­à¤¨à¥à¤¦à¤¾ à¤•à¤®à¤œà¥‹à¤°"),
    ASHTAK_QUICK_ANALYSIS("Quick Analysis", "à¤¦à¥à¤°à¥à¤¤ à¤µà¤¿à¤¶à¥à¤²à¥‡à¤·à¤£"),
    ASHTAK_FAVORABLE_SIGNS("Favorable Signs (28+):", "à¤…à¤¨à¥à¤•à¥‚à¤² à¤°à¤¾à¤¶à¤¿à¤¹à¤°à¥‚ (à¥¨à¥®+):"),
    ASHTAK_CHALLENGING_SIGNS("Challenging Signs (<25):", "à¤šà¥à¤¨à¥Œà¤¤à¥€à¤ªà¥‚à¤°à¥à¤£ à¤°à¤¾à¤¶à¤¿à¤¹à¤°à¥‚ (<à¥¨à¥«):"),
    ASHTAK_SIGNS_COUNT("%d signs", "%d à¤°à¤¾à¤¶à¤¿à¤¹à¤°à¥‚"),

    // Sarvashtakavarga
    ASHTAK_SAV_TITLE("Sarvashtakavarga (SAV)", "à¤¸à¤°à¥à¤µà¤¾à¤·à¥à¤Ÿà¤•à¤µà¤°à¥à¤— (SAV)"),
    ASHTAK_SAV_COMBINED_DESC("Combined strength of all planets in each sign", "à¤ªà¥à¤°à¤¤à¥à¤¯à¥‡à¤• à¤°à¤¾à¤¶à¤¿à¤®à¤¾ à¤¸à¤¬à¥ˆ à¤—à¥à¤°à¤¹à¤¹à¤°à¥‚à¤•à¥‹ à¤¸à¤‚à¤¯à¥à¤•à¥à¤¤ à¤¬à¤²"),

    // Bhinnashtakavarga
    ASHTAK_BAV_TITLE("Bhinnashtakavarga (BAV)", "à¤­à¤¿à¤¨à¥à¤¨à¤¾à¤·à¥à¤Ÿà¤•à¤µà¤°à¥à¤— (BAV)"),
    ASHTAK_BAV_INDIVIDUAL_DESC("Individual planet strength in each sign (0-8 bindus)", "à¤ªà¥à¤°à¤¤à¥à¤¯à¥‡à¤• à¤°à¤¾à¤¶à¤¿à¤®à¤¾ à¤µà¥à¤¯à¤•à¥à¤¤à¤¿à¤—à¤¤ à¤—à¥à¤°à¤¹à¤•à¥‹ à¤¬à¤² (à¥¦-à¥® à¤¬à¤¿à¤¨à¥à¤¦à¥)"),
    ASHTAK_TOTAL("Total", "à¤•à¥à¤²"),

    // SAV Legend
    ASHTAK_SAV_EXCELLENT("30+ (Excellent)", "à¥©à¥¦+ (à¤‰à¤¤à¥à¤•à¥ƒà¤·à¥à¤Ÿ)"),
    ASHTAK_SAV_GOOD("28-29 (Good)", "à¥¨à¥®-à¥¨à¥¯ (à¤°à¤¾à¤®à¥à¤°à¥‹)"),
    ASHTAK_SAV_AVERAGE("25-27 (Average)", "à¥¨à¥«-à¥¨à¥­ (à¤”à¤¸à¤¤)"),
    ASHTAK_SAV_WEAK("<25 (Weak)", "<à¥¨à¥« (à¤•à¤®à¤œà¥‹à¤°)"),

    // BAV Legend
    ASHTAK_BAV_STRONG("5+ (Strong)", "à¥«+ (à¤¬à¤²à¤¿à¤¯à¥‹)"),
    ASHTAK_BAV_GOOD("4 (Good)", "à¥ª (à¤°à¤¾à¤®à¥à¤°à¥‹)"),
    ASHTAK_BAV_AVERAGE("3 (Average)", "à¥© (à¤”à¤¸à¤¤)"),
    ASHTAK_BAV_WEAK("0-2 (Weak)", "à¥¦-à¥¨ (à¤•à¤®à¤œà¥‹à¤°)"),

    // Interpretation Guide
    ASHTAK_GUIDE_TITLE("Interpretation Guide", "à¤µà¥à¤¯à¤¾à¤–à¥à¤¯à¤¾ à¤—à¤¾à¤‡à¤¡"),
    ASHTAK_GUIDE_SAV_TITLE("Sarvashtakavarga (SAV)", "à¤¸à¤°à¥à¤µà¤¾à¤·à¥à¤Ÿà¤•à¤µà¤°à¥à¤— (SAV)"),
    ASHTAK_GUIDE_SAV_30("30+ bindus: Excellent for transits - major positive events", "à¥©à¥¦+ à¤¬à¤¿à¤¨à¥à¤¦à¥: à¤—à¥‹à¤šà¤°à¤•à¥‹ à¤²à¤¾à¤—à¤¿ à¤‰à¤¤à¥à¤•à¥ƒà¤·à¥à¤Ÿ - à¤ªà¥à¤°à¤®à¥à¤– à¤¸à¤•à¤¾à¤°à¤¾à¤¤à¥à¤®à¤• à¤˜à¤Ÿà¤¨à¤¾à¤¹à¤°à¥‚"),
    ASHTAK_GUIDE_SAV_28("28-29 bindus: Good for transits - favorable outcomes", "à¥¨à¥®-à¥¨à¥¯ à¤¬à¤¿à¤¨à¥à¤¦à¥: à¤—à¥‹à¤šà¤°à¤•à¥‹ à¤²à¤¾à¤—à¤¿ à¤°à¤¾à¤®à¥à¤°à¥‹ - à¤…à¤¨à¥à¤•à¥‚à¤² à¤ªà¤°à¤¿à¤£à¤¾à¤®à¤¹à¤°à¥‚"),
    ASHTAK_GUIDE_SAV_25("25-27 bindus: Average - mixed results expected", "à¥¨à¥«-à¥¨à¥­ à¤¬à¤¿à¤¨à¥à¤¦à¥: à¤”à¤¸à¤¤ - à¤®à¤¿à¤¶à¥à¤°à¤¿à¤¤ à¤ªà¤°à¤¿à¤£à¤¾à¤®à¤¹à¤°à¥‚ à¤…à¤ªà¥‡à¤•à¥à¤·à¤¿à¤¤"),
    ASHTAK_GUIDE_SAV_BELOW("Below 25: Challenging - caution during transits", "à¥¨à¥« à¤­à¤¨à¥à¤¦à¤¾ à¤•à¤®: à¤šà¥à¤¨à¥Œà¤¤à¥€à¤ªà¥‚à¤°à¥à¤£ - à¤—à¥‹à¤šà¤°à¤®à¤¾ à¤¸à¤¾à¤µà¤§à¤¾à¤¨à¥€"),
    ASHTAK_GUIDE_BAV_TITLE("Bhinnashtakavarga (BAV)", "à¤­à¤¿à¤¨à¥à¤¨à¤¾à¤·à¥à¤Ÿà¤•à¤µà¤°à¥à¤— (BAV)"),
    ASHTAK_GUIDE_BAV_5("5+ bindus: Planet transit highly beneficial", "à¥«+ à¤¬à¤¿à¤¨à¥à¤¦à¥: à¤—à¥à¤°à¤¹ à¤—à¥‹à¤šà¤° à¤…à¤¤à¥à¤¯à¤§à¤¿à¤• à¤²à¤¾à¤­à¤¦à¤¾à¤¯à¤•"),
    ASHTAK_GUIDE_BAV_4("4 bindus: Good results from transit", "à¥ª à¤¬à¤¿à¤¨à¥à¤¦à¥: à¤—à¥‹à¤šà¤°à¤¬à¤¾à¤Ÿ à¤°à¤¾à¤®à¥à¤°à¥‹ à¤ªà¤°à¤¿à¤£à¤¾à¤®"),
    ASHTAK_GUIDE_BAV_3("3 bindus: Average, neutral results", "à¥© à¤¬à¤¿à¤¨à¥à¤¦à¥: à¤”à¤¸à¤¤, à¤¤à¤Ÿà¤¸à¥à¤¥ à¤ªà¤°à¤¿à¤£à¤¾à¤®à¤¹à¤°à¥‚"),
    ASHTAK_GUIDE_BAV_02("0-2 bindus: Difficult transit period", "à¥¦-à¥¨ à¤¬à¤¿à¤¨à¥à¤¦à¥: à¤•à¤ à¤¿à¤¨ à¤—à¥‹à¤šà¤° à¤…à¤µà¤§à¤¿"),
    ASHTAK_GUIDE_TRANSIT_TITLE("Transit Application", "à¤—à¥‹à¤šà¤° à¤…à¤¨à¥à¤ªà¥à¤°à¤¯à¥‹à¤—"),
    ASHTAK_GUIDE_TRANSIT_1("Check SAV of the sign a planet transits", "à¤—à¥à¤°à¤¹à¤²à¥‡ à¤—à¥‹à¤šà¤° à¤—à¤°à¥à¤¨à¥‡ à¤°à¤¾à¤¶à¤¿à¤•à¥‹ SAV à¤œà¤¾à¤à¤š à¤—à¤°à¥à¤¨à¥à¤¹à¥‹à¤¸à¥"),
    ASHTAK_GUIDE_TRANSIT_2("Check BAV score of that planet in transited sign", "à¤—à¥‹à¤šà¤° à¤—à¤°à¤¿à¤à¤•à¥‹ à¤°à¤¾à¤¶à¤¿à¤®à¤¾ à¤¤à¥à¤¯à¥‹ à¤—à¥à¤°à¤¹à¤•à¥‹ BAV à¤¸à¥à¤•à¥‹à¤° à¤œà¤¾à¤à¤š à¤—à¤°à¥à¤¨à¥à¤¹à¥‹à¤¸à¥"),
    ASHTAK_GUIDE_TRANSIT_3("High combined scores = favorable transit", "à¤‰à¤šà¥à¤š à¤¸à¤‚à¤¯à¥à¤•à¥à¤¤ à¤¸à¥à¤•à¥‹à¤° = à¤…à¤¨à¥à¤•à¥‚à¤² à¤—à¥‹à¤šà¤°"),
    ASHTAK_GUIDE_TRANSIT_4("Use for timing important decisions", "à¤®à¤¹à¤¤à¥à¤¤à¥à¤µà¤ªà¥‚à¤°à¥à¤£ à¤¨à¤¿à¤°à¥à¤£à¤¯à¤¹à¤°à¥‚à¤•à¥‹ à¤¸à¤®à¤¯à¤•à¥‹ à¤²à¤¾à¤—à¤¿ à¤ªà¥à¤°à¤¯à¥‹à¤— à¤—à¤°à¥à¤¨à¥à¤¹à¥‹à¤¸à¥"),

    // ============================================
    // ASPECT TYPES (For AspectCalculator)
    // ============================================
    ASPECT_TYPE_CONJUNCTION("Conjunction", "à¤¯à¥à¤¤à¤¿"),
    ASPECT_TYPE_7TH("7th Aspect", "à¤¸à¤ªà¥à¤¤à¤® à¤¦à¥ƒà¤·à¥à¤Ÿà¤¿"),
    ASPECT_TYPE_MARS_4TH("Mars 4th Aspect", "à¤®à¤‚à¤—à¤²à¤•à¥‹ à¤šà¤¤à¥à¤°à¥à¤¥ à¤¦à¥ƒà¤·à¥à¤Ÿà¤¿"),
    ASPECT_TYPE_MARS_8TH("Mars 8th Aspect", "à¤®à¤‚à¤—à¤²à¤•à¥‹ à¤…à¤·à¥à¤Ÿà¤® à¤¦à¥ƒà¤·à¥à¤Ÿà¤¿"),
    ASPECT_TYPE_JUPITER_5TH("Jupiter 5th Aspect", "à¤—à¥à¤°à¥à¤•à¥‹ à¤ªà¤žà¥à¤šà¤® à¤¦à¥ƒà¤·à¥à¤Ÿà¤¿"),
    ASPECT_TYPE_JUPITER_9TH("Jupiter 9th Aspect", "à¤—à¥à¤°à¥à¤•à¥‹ à¤¨à¤µà¤® à¤¦à¥ƒà¤·à¥à¤Ÿà¤¿"),
    ASPECT_TYPE_SATURN_3RD("Saturn 3rd Aspect", "à¤¶à¤¨à¤¿à¤•à¥‹ à¤¤à¥ƒà¤¤à¥€à¤¯ à¤¦à¥ƒà¤·à¥à¤Ÿà¤¿"),
    ASPECT_TYPE_SATURN_10TH("Saturn 10th Aspect", "à¤¶à¤¨à¤¿à¤•à¥‹ à¤¦à¤¶à¤® à¤¦à¥ƒà¤·à¥à¤Ÿà¤¿"),

    // Aspect Strength Descriptions (Drishti Bala)
    ASPECT_STRENGTH_EXACT("Exact (Purna)", "à¤ªà¥‚à¤°à¥à¤£ (à¤à¤•à¤¦à¤® à¤¸à¤Ÿà¥€à¤•)"),
    ASPECT_STRENGTH_ADHIKA("Strong (Adhika)", "à¤…à¤§à¤¿à¤• (à¤¬à¤²à¤¿à¤¯à¥‹)"),
    ASPECT_STRENGTH_MADHYA("Medium (Madhya)", "à¤®à¤§à¥à¤¯à¤®"),
    ASPECT_STRENGTH_ALPA("Weak (Alpa)", "à¤…à¤²à¥à¤ª (à¤•à¤®à¤œà¥‹à¤°)"),
    ASPECT_STRENGTH_SUNYA("Negligible (Sunya)", "à¤¶à¥‚à¤¨à¥à¤¯ (à¤¨à¤—à¤£à¥à¤¯)"),

    // Aspect descriptions
    ASPECT_CASTS_ON("%s casts %s on %s", "%s à¤²à¥‡ %s à¤®à¤¾ %s à¤¦à¥ƒà¤·à¥à¤Ÿà¤¿ à¤°à¤¾à¤–à¥à¤›"),
    ASPECT_APPLYING("Applying", "à¤¸à¤®à¥€à¤ª à¤†à¤‰à¤à¤¦à¥ˆ"),
    ASPECT_SEPARATING("Separating", "à¤Ÿà¤¾à¤¢à¤¾ à¤¹à¥à¤à¤¦à¥ˆ"),
    ASPECT_DRISHTI_BALA("Drishti Bala", "à¤¦à¥ƒà¤·à¥à¤Ÿà¤¿ à¤¬à¤²"),

    // ============================================
    // TRANSIT QUALITY (For AshtakavargaCalculator)
    // ============================================
    TRANSIT_QUALITY_EXCELLENT("Excellent", "à¤‰à¤¤à¥à¤•à¥ƒà¤·à¥à¤Ÿ"),
    TRANSIT_QUALITY_GOOD("Good", "à¤°à¤¾à¤®à¥à¤°à¥‹"),
    TRANSIT_QUALITY_AVERAGE("Average", "à¤”à¤¸à¤¤"),
    TRANSIT_QUALITY_BELOW_AVG("Below Average", "à¤”à¤¸à¤¤à¤­à¤¨à¥à¤¦à¤¾ à¤•à¤®"),
    TRANSIT_QUALITY_CHALLENGING("Challenging", "à¤šà¥à¤¨à¥Œà¤¤à¥€à¤ªà¥‚à¤°à¥à¤£"),
    TRANSIT_QUALITY_DIFFICULT("Difficult", "à¤•à¤ à¤¿à¤¨"),
    TRANSIT_QUALITY_UNKNOWN("Unknown", "à¤…à¤œà¥à¤žà¤¾à¤¤"),

    // Transit interpretations
    TRANSIT_INTERP_EXCELLENT("Excellent - Highly favorable transit", "à¤‰à¤¤à¥à¤•à¥ƒà¤·à¥à¤Ÿ - à¤…à¤¤à¥à¤¯à¤¨à¥à¤¤ à¤…à¤¨à¥à¤•à¥‚à¤² à¤—à¥‹à¤šà¤°"),
    TRANSIT_INTERP_GOOD("Good - Favorable results expected", "à¤°à¤¾à¤®à¥à¤°à¥‹ - à¤…à¤¨à¥à¤•à¥‚à¤² à¤ªà¤°à¤¿à¤£à¤¾à¤®à¤¹à¤°à¥‚ à¤…à¤ªà¥‡à¤•à¥à¤·à¤¿à¤¤"),
    TRANSIT_INTERP_AVERAGE("Average - Mixed results", "à¤”à¤¸à¤¤ - à¤®à¤¿à¤¶à¥à¤°à¤¿à¤¤ à¤ªà¤°à¤¿à¤£à¤¾à¤®à¤¹à¤°à¥‚"),
    TRANSIT_INTERP_BELOW_AVG("Below Average - Some challenges", "à¤”à¤¸à¤¤à¤­à¤¨à¥à¤¦à¤¾ à¤•à¤® - à¤•à¥‡à¤¹à¥€ à¤šà¥à¤¨à¥Œà¤¤à¥€à¤¹à¤°à¥‚"),
    TRANSIT_INTERP_DIFFICULT("Difficult - Careful navigation needed", "à¤•à¤ à¤¿à¤¨ - à¤¸à¤¾à¤µà¤§à¤¾à¤¨à¥€à¤ªà¥‚à¤°à¥à¤£ à¤µà¥à¤¯à¤µà¤¹à¤¾à¤° à¤†à¤µà¤¶à¥à¤¯à¤•"),
    TRANSIT_ANALYSIS_NOT_AVAILABLE("Transit analysis not available for this planet.", "à¤¯à¤¸ à¤—à¥à¤°à¤¹à¤•à¥‹ à¤²à¤¾à¤—à¤¿ à¤—à¥‹à¤šà¤° à¤µà¤¿à¤¶à¥à¤²à¥‡à¤·à¤£ à¤‰à¤ªà¤²à¤¬à¥à¤§ à¤›à¥ˆà¤¨à¥¤"),

    // ============================================
    // ELEMENTS (Additional)
    // ============================================
    ELEMENT_ETHER("Ether", "à¤†à¤•à¤¾à¤¶"),

    // ============================================
    // PLANET SIGNIFICATIONS (For PlanetDetailDialog)
    // ============================================
    // Sun Significations
    PLANET_SUN_NATURE("Malefic", "à¤ªà¤¾à¤ªà¥€"),
    PLANET_SUN_ELEMENT("Fire", "à¤…à¤—à¥à¤¨à¤¿"),
    PLANET_SUN_REPRESENTS_1("Soul, Self, Ego", "à¤†à¤¤à¥à¤®à¤¾, à¤¸à¥à¤µà¤¯à¤‚, à¤…à¤¹à¤‚à¤•à¤¾à¤°"),
    PLANET_SUN_REPRESENTS_2("Father, Authority Figures", "à¤ªà¤¿à¤¤à¤¾, à¤…à¤§à¤¿à¤•à¤¾à¤°à¥€à¤¹à¤°à¥‚"),
    PLANET_SUN_REPRESENTS_3("Government, Power", "à¤¸à¤°à¤•à¤¾à¤°, à¤¶à¤•à¥à¤¤à¤¿"),
    PLANET_SUN_REPRESENTS_4("Health, Vitality", "à¤¸à¥à¤µà¤¾à¤¸à¥à¤¥à¥à¤¯, à¤œà¥€à¤µà¤¨à¤¶à¤•à¥à¤¤à¤¿"),
    PLANET_SUN_REPRESENTS_5("Fame, Recognition", "à¤ªà¥à¤°à¤¸à¤¿à¤¦à¥à¤§à¤¿, à¤®à¤¾à¤¨à¥à¤¯à¤¤à¤¾"),
    PLANET_SUN_BODY_PARTS("Heart, Spine, Right Eye, Bones", "à¤¹à¥ƒà¤¦à¤¯, à¤®à¥‡à¤°à¥à¤¦à¤£à¥à¤¡, à¤¦à¤¾à¤¹à¤¿à¤¨à¥‡ à¤†à¤à¤–à¤¾, à¤¹à¤¡à¥à¤¡à¥€"),
    PLANET_SUN_PROFESSIONS("Government jobs, Politics, Medicine, Administration, Leadership roles", "à¤¸à¤°à¤•à¤¾à¤°à¥€ à¤œà¤¾à¤—à¤¿à¤°, à¤°à¤¾à¤œà¤¨à¥€à¤¤à¤¿, à¤šà¤¿à¤•à¤¿à¤¤à¥à¤¸à¤¾, à¤ªà¥à¤°à¤¶à¤¾à¤¸à¤¨, à¤¨à¥‡à¤¤à¥ƒà¤¤à¥à¤µ à¤­à¥‚à¤®à¤¿à¤•à¤¾à¤¹à¤°à¥‚"),

    // Moon Significations
    PLANET_MOON_NATURE("Benefic", "à¤¶à¥à¤­"),
    PLANET_MOON_ELEMENT("Water", "à¤œà¤²"),
    PLANET_MOON_REPRESENTS_1("Mind, Emotions", "à¤®à¤¨, à¤­à¤¾à¤µà¤¨à¤¾à¤¹à¤°à¥‚"),
    PLANET_MOON_REPRESENTS_2("Mother, Nurturing", "à¤†à¤®à¤¾, à¤ªà¤¾à¤²à¤¨à¤ªà¥‹à¤·à¤£"),
    PLANET_MOON_REPRESENTS_3("Public, Masses", "à¤œà¤¨à¤¤à¤¾, à¤¸à¤®à¥à¤¦à¤¾à¤¯"),
    PLANET_MOON_REPRESENTS_4("Comforts, Happiness", "à¤†à¤°à¤¾à¤®, à¤–à¥à¤¶à¥€"),
    PLANET_MOON_REPRESENTS_5("Memory, Imagination", "à¤¸à¥à¤®à¥ƒà¤¤à¤¿, à¤•à¤²à¥à¤ªà¤¨à¤¾"),
    PLANET_MOON_BODY_PARTS("Mind, Left Eye, Breast, Blood, Fluids", "à¤®à¤¨, à¤¬à¤¾à¤¯à¤¾à¤ à¤†à¤à¤–à¤¾, à¤¸à¥à¤¤à¤¨, à¤°à¤—à¤¤, à¤¤à¤°à¤² à¤ªà¤¦à¤¾à¤°à¥à¤¥"),
    PLANET_MOON_PROFESSIONS("Nursing, Hotel industry, Shipping, Agriculture, Psychology", "à¤¨à¤°à¥à¤¸à¤¿à¤™, à¤¹à¥‹à¤Ÿà¤² à¤‰à¤¦à¥à¤¯à¥‹à¤—, à¤œà¤¹à¤¾à¤œà¤°à¤¾à¤¨à¥€, à¤•à¥ƒà¤·à¤¿, à¤®à¤¨à¥‹à¤µà¤¿à¤œà¥à¤žà¤¾à¤¨"),

    // Mars Significations
    PLANET_MARS_NATURE("Malefic", "à¤ªà¤¾à¤ªà¥€"),
    PLANET_MARS_ELEMENT("Fire", "à¤…à¤—à¥à¤¨à¤¿"),
    PLANET_MARS_REPRESENTS_1("Energy, Action, Courage", "à¤Šà¤°à¥à¤œà¤¾, à¤•à¤¾à¤°à¥à¤¯, à¤¸à¤¾à¤¹à¤¸"),
    PLANET_MARS_REPRESENTS_2("Siblings, Younger Brothers", "à¤­à¤¾à¤‡à¤¬à¤¹à¤¿à¤¨à¥€, à¤¸à¤¾à¤¨à¥‹ à¤­à¤¾à¤‡"),
    PLANET_MARS_REPRESENTS_3("Property, Land", "à¤¸à¤®à¥à¤ªà¤¤à¥à¤¤à¤¿, à¤œà¤®à¤¿à¤¨"),
    PLANET_MARS_REPRESENTS_4("Competition, Sports", "à¤ªà¥à¤°à¤¤à¤¿à¤¸à¥à¤ªà¤°à¥à¤§à¤¾, à¤–à¥‡à¤²à¤•à¥à¤¦"),
    PLANET_MARS_REPRESENTS_5("Technical Skills", "à¤ªà¥à¤°à¤¾à¤µà¤¿à¤§à¤¿à¤• à¤¸à¥€à¤ªà¤¹à¤°à¥‚"),
    PLANET_MARS_BODY_PARTS("Blood, Muscles, Marrow, Head injuries", "à¤°à¤—à¤¤, à¤®à¤¾à¤‚à¤¸à¤ªà¥‡à¤¶à¥€, à¤®à¤œà¥à¤œà¤¾, à¤Ÿà¤¾à¤‰à¤•à¥‹à¤®à¤¾ à¤šà¥‹à¤Ÿà¤ªà¤Ÿà¤•"),
    PLANET_MARS_PROFESSIONS("Military, Police, Surgery, Engineering, Sports, Real Estate", "à¤¸à¥‡à¤¨à¤¾, à¤ªà¥à¤°à¤¹à¤°à¥€, à¤¶à¤²à¥à¤¯à¤•à¥à¤°à¤¿à¤¯à¤¾, à¤‡à¤¨à¥à¤œà¤¿à¤¨à¤¿à¤¯à¤°à¤¿à¤™, à¤–à¥‡à¤²à¤•à¥à¤¦, à¤˜à¤°à¤œà¤—à¥à¤—à¤¾"),

    // Mercury Significations
    PLANET_MERCURY_NATURE("Benefic", "à¤¶à¥à¤­"),
    PLANET_MERCURY_ELEMENT("Earth", "à¤ªà¥ƒà¤¥à¥à¤µà¥€"),
    PLANET_MERCURY_REPRESENTS_1("Intelligence, Communication", "à¤¬à¥à¤¦à¥à¤§à¤¿, à¤¸à¤žà¥à¤šà¤¾à¤°"),
    PLANET_MERCURY_REPRESENTS_2("Learning, Education", "à¤¸à¤¿à¤•à¤¾à¤‡, à¤¶à¤¿à¤•à¥à¤·à¤¾"),
    PLANET_MERCURY_REPRESENTS_3("Business, Trade", "à¤µà¥à¤¯à¤¾à¤ªà¤¾à¤°, à¤µà¥à¤¯à¤µà¤¸à¤¾à¤¯"),
    PLANET_MERCURY_REPRESENTS_4("Writing, Speech", "à¤²à¥‡à¤–à¤¨, à¤µà¤¾à¤£à¥€"),
    PLANET_MERCURY_REPRESENTS_5("Siblings, Friends", "à¤­à¤¾à¤‡à¤¬à¤¹à¤¿à¤¨à¥€, à¤¸à¤¾à¤¥à¥€à¤¹à¤°à¥‚"),
    PLANET_MERCURY_BODY_PARTS("Nervous system, Skin, Speech, Hands", "à¤¸à¥à¤¨à¤¾à¤¯à¥ à¤ªà¥à¤°à¤£à¤¾à¤²à¥€, à¤›à¤¾à¤²à¤¾, à¤µà¤¾à¤£à¥€, à¤¹à¤¾à¤¤à¤¹à¤°à¥‚"),
    PLANET_MERCURY_PROFESSIONS("Writing, Teaching, Accounting, Trading, IT, Media", "à¤²à¥‡à¤–à¤¨, à¤¶à¤¿à¤•à¥à¤·à¤£, à¤²à¥‡à¤–à¤¾, à¤µà¥à¤¯à¤¾à¤ªà¤¾à¤°, à¤†à¤ˆà¤Ÿà¥€, à¤®à¤¿à¤¡à¤¿à¤¯à¤¾"),

    // Jupiter Significations
    PLANET_JUPITER_NATURE("Benefic", "à¤¶à¥à¤­"),
    PLANET_JUPITER_ELEMENT("Ether", "à¤†à¤•à¤¾à¤¶"),
    PLANET_JUPITER_REPRESENTS_1("Wisdom, Knowledge", "à¤œà¥à¤žà¤¾à¤¨, à¤µà¤¿à¤¦à¥à¤¯à¤¾"),
    PLANET_JUPITER_REPRESENTS_2("Teachers, Gurus", "à¤¶à¤¿à¤•à¥à¤·à¤•à¤¹à¤°à¥‚, à¤—à¥à¤°à¥à¤¹à¤°à¥‚"),
    PLANET_JUPITER_REPRESENTS_3("Fortune, Luck", "à¤­à¤¾à¤—à¥à¤¯, à¤•à¤¿à¤¸à¥à¤®à¤¤"),
    PLANET_JUPITER_REPRESENTS_4("Children, Dharma", "à¤¸à¤¨à¥à¤¤à¤¾à¤¨, à¤§à¤°à¥à¤®"),
    PLANET_JUPITER_REPRESENTS_5("Expansion, Growth", "à¤µà¤¿à¤¸à¥à¤¤à¤¾à¤°, à¤µà¥ƒà¤¦à¥à¤§à¤¿"),
    PLANET_JUPITER_BODY_PARTS("Liver, Fat tissue, Ears, Thighs", "à¤•à¤²à¥‡à¤œà¥‹, à¤¬à¥‹à¤¸à¥‹, à¤•à¤¾à¤¨, à¤œà¤¾à¤à¤˜"),
    PLANET_JUPITER_PROFESSIONS("Teaching, Law, Priesthood, Banking, Counseling", "à¤¶à¤¿à¤•à¥à¤·à¤£, à¤•à¤¾à¤¨à¥‚à¤¨, à¤ªà¥à¤°à¥‹à¤¹à¤¿à¤¤, à¤¬à¥ˆà¤‚à¤•à¤¿à¤™, à¤ªà¤°à¤¾à¤®à¤°à¥à¤¶"),

    // Venus Significations
    PLANET_VENUS_NATURE("Benefic", "à¤¶à¥à¤­"),
    PLANET_VENUS_ELEMENT("Water", "à¤œà¤²"),
    PLANET_VENUS_REPRESENTS_1("Love, Beauty, Art", "à¤ªà¥à¤°à¥‡à¤®, à¤¸à¥Œà¤¨à¥à¤¦à¤°à¥à¤¯, à¤•à¤²à¤¾"),
    PLANET_VENUS_REPRESENTS_2("Marriage, Relationships", "à¤µà¤¿à¤µà¤¾à¤¹, à¤¸à¤®à¥à¤¬à¤¨à¥à¤§à¤¹à¤°à¥‚"),
    PLANET_VENUS_REPRESENTS_3("Luxuries, Comforts", "à¤µà¤¿à¤²à¤¾à¤¸à¤¿à¤¤à¤¾, à¤†à¤°à¤¾à¤®"),
    PLANET_VENUS_REPRESENTS_4("Vehicles, Pleasures", "à¤¸à¤µà¤¾à¤°à¥€, à¤†à¤¨à¤¨à¥à¤¦"),
    PLANET_VENUS_REPRESENTS_5("Creativity", "à¤¸à¤¿à¤°à¥à¤œà¤¨à¤¶à¥€à¤²à¤¤à¤¾"),
    PLANET_VENUS_BODY_PARTS("Reproductive system, Face, Skin, Throat", "à¤ªà¥à¤°à¤œà¤¨à¤¨ à¤ªà¥à¤°à¤£à¤¾à¤²à¥€, à¤…à¤¨à¥à¤¹à¤¾à¤°, à¤›à¤¾à¤²à¤¾, à¤˜à¤¾à¤à¤Ÿà¥€"),
    PLANET_VENUS_PROFESSIONS("Entertainment, Fashion, Art, Hospitality, Beauty industry", "à¤®à¤¨à¥‹à¤°à¤žà¥à¤œà¤¨, à¤«à¥‡à¤¸à¤¨, à¤•à¤²à¤¾, à¤†à¤¤à¤¿à¤¥à¥à¤¯, à¤¸à¥Œà¤¨à¥à¤¦à¤°à¥à¤¯ à¤‰à¤¦à¥à¤¯à¥‹à¤—"),

    // Saturn Significations
    PLANET_SATURN_NATURE("Malefic", "à¤ªà¤¾à¤ªà¥€"),
    PLANET_SATURN_ELEMENT("Air", "à¤µà¤¾à¤¯à¥"),
    PLANET_SATURN_REPRESENTS_1("Discipline, Hard work", "à¤…à¤¨à¥à¤¶à¤¾à¤¸à¤¨, à¤•à¤¡à¤¾ à¤ªà¤°à¤¿à¤¶à¥à¤°à¤®"),
    PLANET_SATURN_REPRESENTS_2("Karma, Delays", "à¤•à¤°à¥à¤®, à¤¢à¤¿à¤²à¤¾à¤‡"),
    PLANET_SATURN_REPRESENTS_3("Longevity, Service", "à¤¦à¥€à¤°à¥à¤˜à¤¾à¤¯à¥, à¤¸à¥‡à¤µà¤¾"),
    PLANET_SATURN_REPRESENTS_4("Laborers, Servants", "à¤¶à¥à¤°à¤®à¤¿à¤•à¤¹à¤°à¥‚, à¤¸à¥‡à¤µà¤•à¤¹à¤°à¥‚"),
    PLANET_SATURN_REPRESENTS_5("Chronic issues", "à¤¦à¥€à¤°à¥à¤˜à¤•à¤¾à¤²à¥€à¤¨ à¤¸à¤®à¤¸à¥à¤¯à¤¾à¤¹à¤°à¥‚"),
    PLANET_SATURN_BODY_PARTS("Bones, Teeth, Knees, Joints, Nerves", "à¤¹à¤¡à¥à¤¡à¥€, à¤¦à¤¾à¤à¤¤, à¤˜à¥à¤à¤¡à¤¾, à¤œà¥‹à¤°à¥à¤¨à¥€à¤¹à¤°à¥‚, à¤¸à¥à¤¨à¤¾à¤¯à¥"),
    PLANET_SATURN_PROFESSIONS("Mining, Agriculture, Labor, Judiciary, Real Estate", "à¤–à¤¨à¤¨, à¤•à¥ƒà¤·à¤¿, à¤¶à¥à¤°à¤®, à¤¨à¥à¤¯à¤¾à¤¯à¤ªà¤¾à¤²à¤¿à¤•à¤¾, à¤˜à¤°à¤œà¤—à¥à¤—à¤¾"),

    // Rahu Significations
    PLANET_RAHU_NATURE("Malefic", "à¤ªà¤¾à¤ªà¥€"),
    PLANET_RAHU_ELEMENT("Air", "à¤µà¤¾à¤¯à¥"),
    PLANET_RAHU_REPRESENTS_1("Obsession, Illusion", "à¤œà¥à¤¨à¥‚à¤¨, à¤­à¥à¤°à¤®"),
    PLANET_RAHU_REPRESENTS_2("Foreign lands, Travel", "à¤µà¤¿à¤¦à¥‡à¤¶, à¤¯à¤¾à¤¤à¥à¤°à¤¾"),
    PLANET_RAHU_REPRESENTS_3("Technology, Innovation", "à¤ªà¥à¤°à¤µà¤¿à¤§à¤¿, à¤¨à¤µà¥€à¤¨à¤¤à¤¾"),
    PLANET_RAHU_REPRESENTS_4("Unconventional paths", "à¤…à¤ªà¤°à¤‚à¤ªà¤°à¤¾à¤—à¤¤ à¤®à¤¾à¤°à¥à¤—à¤¹à¤°à¥‚"),
    PLANET_RAHU_REPRESENTS_5("Material desires", "à¤­à¥Œà¤¤à¤¿à¤• à¤‡à¤šà¥à¤›à¤¾à¤¹à¤°à¥‚"),
    PLANET_RAHU_BODY_PARTS("Skin diseases, Nervous disorders", "à¤›à¤¾à¤²à¤¾à¤•à¤¾ à¤°à¥‹à¤—à¤¹à¤°à¥‚, à¤¸à¥à¤¨à¤¾à¤¯à¥ à¤µà¤¿à¤•à¤¾à¤°à¤¹à¤°à¥‚"),
    PLANET_RAHU_PROFESSIONS("Technology, Foreign affairs, Aviation, Politics, Research", "à¤ªà¥à¤°à¤µà¤¿à¤§à¤¿, à¤µà¤¿à¤¦à¥‡à¤¶à¥€ à¤®à¤¾à¤®à¤¿à¤²à¤¾, à¤‰à¤¡à¥à¤¡à¤¯à¤¨, à¤°à¤¾à¤œà¤¨à¥€à¤¤à¤¿, à¤…à¤¨à¥à¤¸à¤¨à¥à¤§à¤¾à¤¨"),

    // Ketu Significations
    PLANET_KETU_NATURE("Malefic", "à¤ªà¤¾à¤ªà¥€"),
    PLANET_KETU_ELEMENT("Fire", "à¤…à¤—à¥à¤¨à¤¿"),
    PLANET_KETU_REPRESENTS_1("Spirituality, Liberation", "à¤†à¤§à¥à¤¯à¤¾à¤¤à¥à¤®à¤¿à¤•à¤¤à¤¾, à¤®à¥‹à¤•à¥à¤·"),
    PLANET_KETU_REPRESENTS_2("Past life karma", "à¤ªà¥‚à¤°à¥à¤µà¤œà¤¨à¥à¤®à¤•à¥‹ à¤•à¤°à¥à¤®"),
    PLANET_KETU_REPRESENTS_3("Detachment, Isolation", "à¤µà¥ˆà¤°à¤¾à¤—à¥à¤¯, à¤à¤•à¤¾à¤¨à¥à¤¤"),
    PLANET_KETU_REPRESENTS_4("Occult, Mysticism", "à¤—à¥à¤ªà¥à¤¤ à¤µà¤¿à¤¦à¥à¤¯à¤¾, à¤°à¤¹à¤¸à¥à¤¯à¤µà¤¾à¤¦"),
    PLANET_KETU_REPRESENTS_5("Healing abilities", "à¤‰à¤ªà¤šà¤¾à¤° à¤•à¥à¤·à¤®à¤¤à¤¾"),
    PLANET_KETU_BODY_PARTS("Skin, Spine, Nervous system", "à¤›à¤¾à¤²à¤¾, à¤®à¥‡à¤°à¥à¤¦à¤£à¥à¤¡, à¤¸à¥à¤¨à¤¾à¤¯à¥ à¤ªà¥à¤°à¤£à¤¾à¤²à¥€"),
    PLANET_KETU_PROFESSIONS("Spirituality, Research, Healing, Astrology, Philosophy", "à¤†à¤§à¥à¤¯à¤¾à¤¤à¥à¤®à¤¿à¤•à¤¤à¤¾, à¤…à¤¨à¥à¤¸à¤¨à¥à¤§à¤¾à¤¨, à¤‰à¤ªà¤šà¤¾à¤°, à¤œà¥à¤¯à¥‹à¤¤à¤¿à¤·, à¤¦à¤°à¥à¤¶à¤¨"),

    // ============================================
    // CHART LEGEND LABELS (For ChartRenderer)
    // ============================================
    CHART_LEGEND_RETRO_SHORT("Retro", "à¤µà¤•à¥à¤°à¥€"),
    CHART_LEGEND_COMBUST_SHORT("Comb", "à¤…à¤¸à¥à¤¤"),
    CHART_LEGEND_VARGO_SHORT("Vargo", "à¤µà¤°à¥à¤—à¥‹"),
    CHART_LEGEND_EXALT_SHORT("Exalt", "à¤‰à¤šà¥à¤š"),
    CHART_LEGEND_DEB_SHORT("Deb", "à¤¨à¥€à¤š"),
    CHART_LEGEND_OWN_SHORT("Own", "à¤¸à¥à¤µ"),
    CHART_ASC_ABBR("Asc", "à¤²"),

    // ============================================
    // ASHTAKAVARGA ANALYSIS HEADERS
    // ============================================
    ASHTAK_ANALYSIS_HEADER("ASHTAKAVARGA ANALYSIS", "à¤…à¤·à¥à¤Ÿà¤•à¤µà¤°à¥à¤— à¤µà¤¿à¤¶à¥à¤²à¥‡à¤·à¤£"),
    ASHTAK_SAV_HEADER("SARVASHTAKAVARGA (Combined Strength)", "à¤¸à¤°à¥à¤µà¤¾à¤·à¥à¤Ÿà¤•à¤µà¤°à¥à¤— (à¤¸à¤‚à¤¯à¥à¤•à¥à¤¤ à¤¬à¤²)"),
    ASHTAK_BAV_HEADER("BHINNASHTAKAVARGA (Individual Planet Strengths)", "à¤­à¤¿à¤¨à¥à¤¨à¤¾à¤·à¥à¤Ÿà¤•à¤µà¤°à¥à¤— (à¤µà¥à¤¯à¤•à¥à¤¤à¤¿à¤—à¤¤ à¤—à¥à¤°à¤¹ à¤¬à¤²)"),
    ASHTAK_TOTAL_SAV_BINDUS("Total SAV Bindus:", "à¤•à¥à¤² SAV à¤¬à¤¿à¤¨à¥à¤¦à¥à¤¹à¤°à¥‚:"),
    ASHTAK_AVG_PER_SIGN("Average per Sign:", "à¤ªà¥à¤°à¤¤à¤¿ à¤°à¤¾à¤¶à¤¿ à¤”à¤¸à¤¤:"),
    ASHTAK_NOT_APPLICABLE("Ashtakavarga not applicable for %s", "%s à¤•à¥‹ à¤²à¤¾à¤—à¤¿ à¤…à¤·à¥à¤Ÿà¤•à¤µà¤°à¥à¤— à¤²à¤¾à¤—à¥‚ à¤¹à¥à¤à¤¦à¥ˆà¤¨"),

    // Ashtakavarga Planet Effects
    ASHTAK_SUN_EFFECTS("authority, father, health, government, career", "à¤…à¤§à¤¿à¤•à¤¾à¤°, à¤ªà¤¿à¤¤à¤¾, à¤¸à¥à¤µà¤¾à¤¸à¥à¤¥à¥à¤¯, à¤¸à¤°à¤•à¤¾à¤°, à¤•à¥à¤¯à¤¾à¤°à¤¿à¤¯à¤°"),
    ASHTAK_MOON_EFFECTS("mind, emotions, mother, public image", "à¤®à¤¨, à¤­à¤¾à¤µà¤¨à¤¾, à¤†à¤®à¤¾, à¤¸à¤¾à¤°à¥à¤µà¤œà¤¨à¤¿à¤• à¤›à¤µà¤¿"),
    ASHTAK_MARS_EFFECTS("energy, siblings, property, courage", "à¤Šà¤°à¥à¤œà¤¾, à¤­à¤¾à¤‡à¤¬à¤¹à¤¿à¤¨à¥€, à¤¸à¤®à¥à¤ªà¤¤à¥à¤¤à¤¿, à¤¸à¤¾à¤¹à¤¸"),
    ASHTAK_MERCURY_EFFECTS("communication, intellect, business, education", "à¤¸à¤žà¥à¤šà¤¾à¤°, à¤¬à¥à¤¦à¥à¤§à¤¿, à¤µà¥à¤¯à¤¾à¤ªà¤¾à¤°, à¤¶à¤¿à¤•à¥à¤·à¤¾"),
    ASHTAK_JUPITER_EFFECTS("wisdom, children, fortune, spirituality", "à¤œà¥à¤žà¤¾à¤¨, à¤¸à¤¨à¥à¤¤à¤¾à¤¨, à¤­à¤¾à¤—à¥à¤¯, à¤†à¤§à¥à¤¯à¤¾à¤¤à¥à¤®à¤¿à¤•à¤¤à¤¾"),
    ASHTAK_VENUS_EFFECTS("relationships, luxury, arts, vehicles", "à¤¸à¤®à¥à¤¬à¤¨à¥à¤§, à¤µà¤¿à¤²à¤¾à¤¸à¤¿à¤¤à¤¾, à¤•à¤²à¤¾, à¤¸à¤µà¤¾à¤°à¥€"),
    ASHTAK_SATURN_EFFECTS("career, longevity, discipline, challenges", "à¤•à¥à¤¯à¤¾à¤°à¤¿à¤¯à¤°, à¤¦à¥€à¤°à¥à¤˜à¤¾à¤¯à¥, à¤…à¤¨à¥à¤¶à¤¾à¤¸à¤¨, à¤šà¥à¤¨à¥Œà¤¤à¥€à¤¹à¤°à¥‚"),
    ASHTAK_GENERAL_EFFECTS("general matters", "à¤¸à¤¾à¤®à¤¾à¤¨à¥à¤¯ à¤®à¤¾à¤®à¤¿à¤²à¤¾à¤¹à¤°à¥‚"),

    // House matters for transit interpretation
    ASHTAK_HOUSE_1_MATTERS("self, personality, health", "à¤†à¤«à¥‚, à¤µà¥à¤¯à¤•à¥à¤¤à¤¿à¤¤à¥à¤µ, à¤¸à¥à¤µà¤¾à¤¸à¥à¤¥à¥à¤¯"),
    ASHTAK_HOUSE_2_MATTERS("wealth, family, speech", "à¤§à¤¨, à¤ªà¤°à¤¿à¤µà¤¾à¤°, à¤µà¤¾à¤£à¥€"),
    ASHTAK_HOUSE_3_MATTERS("courage, siblings, communication", "à¤¸à¤¾à¤¹à¤¸, à¤­à¤¾à¤‡à¤¬à¤¹à¤¿à¤¨à¥€, à¤¸à¤žà¥à¤šà¤¾à¤°"),
    ASHTAK_HOUSE_4_MATTERS("home, mother, comfort", "à¤˜à¤°, à¤†à¤®à¤¾, à¤†à¤°à¤¾à¤®"),
    ASHTAK_HOUSE_5_MATTERS("children, intelligence, romance", "à¤¸à¤¨à¥à¤¤à¤¾à¤¨, à¤¬à¥à¤¦à¥à¤§à¤¿, à¤ªà¥à¤°à¥‡à¤®"),
    ASHTAK_HOUSE_6_MATTERS("enemies, health issues, service", "à¤¶à¤¤à¥à¤°à¥, à¤¸à¥à¤µà¤¾à¤¸à¥à¤¥à¥à¤¯ à¤¸à¤®à¤¸à¥à¤¯à¤¾, à¤¸à¥‡à¤µà¤¾"),
    ASHTAK_HOUSE_7_MATTERS("partnership, marriage, business", "à¤¸à¤¾à¤à¥‡à¤¦à¤¾à¤°à¥€, à¤µà¤¿à¤µà¤¾à¤¹, à¤µà¥à¤¯à¤¾à¤ªà¤¾à¤°"),
    ASHTAK_HOUSE_8_MATTERS("transformation, inheritance, occult", "à¤°à¥‚à¤ªà¤¾à¤¨à¥à¤¤à¤°à¤£, à¤¸à¤®à¥à¤ªà¤¤à¥à¤¤à¤¿, à¤—à¥à¤ªà¥à¤¤ à¤µà¤¿à¤¦à¥à¤¯à¤¾"),
    ASHTAK_HOUSE_9_MATTERS("luck, father, higher learning", "à¤­à¤¾à¤—à¥à¤¯, à¤ªà¤¿à¤¤à¤¾, à¤‰à¤šà¥à¤š à¤¶à¤¿à¤•à¥à¤·à¤¾"),
    ASHTAK_HOUSE_10_MATTERS("career, status, authority", "à¤•à¥à¤¯à¤¾à¤°à¤¿à¤¯à¤°, à¤¹à¥ˆà¤¸à¤¿à¤¯à¤¤, à¤…à¤§à¤¿à¤•à¤¾à¤°"),
    ASHTAK_HOUSE_11_MATTERS("gains, friends, aspirations", "à¤²à¤¾à¤­, à¤¸à¤¾à¤¥à¥€à¤¹à¤°à¥‚, à¤†à¤•à¤¾à¤‚à¤•à¥à¤·à¤¾à¤¹à¤°à¥‚"),
    ASHTAK_HOUSE_12_MATTERS("losses, spirituality, foreign", "à¤¹à¤¾à¤¨à¤¿, à¤†à¤§à¥à¤¯à¤¾à¤¤à¥à¤®à¤¿à¤•à¤¤à¤¾, à¤µà¤¿à¤¦à¥‡à¤¶"),

    // Transit interpretation templates
    TRANSIT_EXCELLENT_TEMPLATE("Transit through %s with %d BAV bindus and %d SAV bindus indicates excellent results. Matters related to %s will flourish. Areas of %s receive strong positive influence.", "%s à¤®à¤¾ %d BAV à¤¬à¤¿à¤¨à¥à¤¦à¥ à¤° %d SAV à¤¬à¤¿à¤¨à¥à¤¦à¥à¤¸à¤à¤—à¤•à¥‹ à¤—à¥‹à¤šà¤°à¤²à¥‡ à¤‰à¤¤à¥à¤•à¥ƒà¤·à¥à¤Ÿ à¤ªà¤°à¤¿à¤£à¤¾à¤® à¤¦à¥‡à¤–à¤¾à¤‰à¤à¤›à¥¤ %s à¤¸à¤®à¥à¤¬à¤¨à¥à¤§à¥€ à¤®à¤¾à¤®à¤¿à¤²à¤¾à¤¹à¤°à¥‚ à¤«à¤¸à¥à¤Ÿà¤¾à¤‰à¤¨à¥‡à¤›à¤¨à¥à¥¤ %s à¤•à¥‹ à¤•à¥à¤·à¥‡à¤¤à¥à¤°à¤®à¤¾ à¤¬à¤²à¤¿à¤¯à¥‹ à¤¸à¤•à¤¾à¤°à¤¾à¤¤à¥à¤®à¤• à¤ªà¥à¤°à¤­à¤¾à¤µ à¤ªà¤°à¥à¤¨à¥‡à¤›à¥¤"),
    TRANSIT_GOOD_TEMPLATE("Transit through %s brings favorable results with %d BAV and %d SAV bindus. Good progress expected in %s. %s areas are positively influenced.", "%s à¤®à¤¾ %d BAV à¤° %d SAV à¤¬à¤¿à¤¨à¥à¤¦à¥à¤¸à¤à¤—à¤•à¥‹ à¤—à¥‹à¤šà¤°à¤²à¥‡ à¤…à¤¨à¥à¤•à¥‚à¤² à¤ªà¤°à¤¿à¤£à¤¾à¤®à¤¹à¤°à¥‚ à¤²à¥à¤¯à¤¾à¤‰à¤à¤›à¥¤ %s à¤®à¤¾ à¤°à¤¾à¤®à¥à¤°à¥‹ à¤ªà¥à¤°à¤—à¤¤à¤¿ à¤…à¤ªà¥‡à¤•à¥à¤·à¤¿à¤¤ à¤›à¥¤ %s à¤•à¥à¤·à¥‡à¤¤à¥à¤°à¤®à¤¾ à¤¸à¤•à¤¾à¤°à¤¾à¤¤à¥à¤®à¤• à¤ªà¥à¤°à¤­à¤¾à¤µ à¤ªà¤°à¥à¤¨à¥‡à¤›à¥¤"),
    TRANSIT_AVERAGE_TEMPLATE("Transit through %s (%d BAV, %d SAV) brings mixed results. Some progress in %s with occasional challenges. Balance needed in %s.", "%s à¤®à¤¾ (%d BAV, %d SAV) à¤—à¥‹à¤šà¤°à¤²à¥‡ à¤®à¤¿à¤¶à¥à¤°à¤¿à¤¤ à¤ªà¤°à¤¿à¤£à¤¾à¤®à¤¹à¤°à¥‚ à¤²à¥à¤¯à¤¾à¤‰à¤à¤›à¥¤ %s à¤®à¤¾ à¤•à¥‡à¤¹à¥€ à¤ªà¥à¤°à¤—à¤¤à¤¿ à¤¤à¤° à¤•à¤¹à¤¿à¤²à¥‡à¤•à¤¾à¤¹à¥€à¤ à¤šà¥à¤¨à¥Œà¤¤à¥€à¤¹à¤°à¥‚à¥¤ %s à¤®à¤¾ à¤¸à¤¨à¥à¤¤à¥à¤²à¤¨ à¤†à¤µà¤¶à¥à¤¯à¤•à¥¤"),
    TRANSIT_BELOW_AVG_TEMPLATE("Transit through %s (%d BAV, %d SAV) suggests caution needed. %s matters may face delays. Extra effort required in %s areas.", "%s à¤®à¤¾ (%d BAV, %d SAV) à¤—à¥‹à¤šà¤°à¤²à¥‡ à¤¸à¤¾à¤µà¤§à¤¾à¤¨à¥€ à¤†à¤µà¤¶à¥à¤¯à¤• à¤¦à¥‡à¤–à¤¾à¤‰à¤à¤›à¥¤ %s à¤®à¤¾à¤®à¤¿à¤²à¤¾à¤¹à¤°à¥‚à¤®à¤¾ à¤¢à¤¿à¤²à¤¾à¤‡ à¤¹à¥à¤¨ à¤¸à¤•à¥à¤›à¥¤ %s à¤•à¥à¤·à¥‡à¤¤à¥à¤°à¤®à¤¾ à¤¥à¤ª à¤ªà¥à¤°à¤¯à¤¾à¤¸ à¤†à¤µà¤¶à¥à¤¯à¤•à¥¤"),
    TRANSIT_CHALLENGING_TEMPLATE("Challenging transit through %s with only %d BAV and %d SAV bindus. Difficulties possible in %s. Patience needed for %s matters.", "%s à¤®à¤¾ à¤•à¥‡à¤µà¤² %d BAV à¤° %d SAV à¤¬à¤¿à¤¨à¥à¤¦à¥à¤¸à¤à¤—à¤•à¥‹ à¤šà¥à¤¨à¥Œà¤¤à¥€à¤ªà¥‚à¤°à¥à¤£ à¤—à¥‹à¤šà¤°à¥¤ %s à¤®à¤¾ à¤•à¤ à¤¿à¤¨à¤¾à¤‡à¤¹à¤°à¥‚ à¤¸à¤®à¥à¤­à¤µà¥¤ %s à¤®à¤¾à¤®à¤¿à¤²à¤¾à¤¹à¤°à¥‚à¤®à¤¾ à¤§à¥ˆà¤°à¥à¤¯ à¤†à¤µà¤¶à¥à¤¯à¤•à¥¤"),
    TRANSIT_DIFFICULT_TEMPLATE("Difficult transit period through %s (%d BAV, %d SAV). Significant challenges in %s areas. Careful handling of %s required.", "%s à¤®à¤¾ (%d BAV, %d SAV) à¤•à¤ à¤¿à¤¨ à¤—à¥‹à¤šà¤° à¤…à¤µà¤§à¤¿à¥¤ %s à¤•à¥à¤·à¥‡à¤¤à¥à¤°à¤®à¤¾ à¤®à¤¹à¤¤à¥à¤¤à¥à¤µà¤ªà¥‚à¤°à¥à¤£ à¤šà¥à¤¨à¥Œà¤¤à¥€à¤¹à¤°à¥‚à¥¤ %s à¤•à¥‹ à¤¸à¤¾à¤µà¤§à¤¾à¤¨à¥€à¤ªà¥‚à¤°à¥à¤µà¤• à¤µà¥à¤¯à¤µà¤¸à¥à¤¥à¤¾à¤ªà¤¨ à¤†à¤µà¤¶à¥à¤¯à¤•à¥¤"),

    // ============================================
    // NAKSHATRA DETAILS (For ChartDialogs)
    // ============================================
    NAKSHATRA_SYMBOL("Symbol", "à¤ªà¥à¤°à¤¤à¥€à¤•"),
    NAKSHATRA_DEITY("Deity", "à¤¦à¥‡à¤µà¤¤à¤¾"),
    NAKSHATRA_PADA("Pada", "à¤ªà¤¦"),
    NAKSHATRA_GUNA("Guna", "à¤—à¥à¤£"),
    NAKSHATRA_GANA("Gana", "à¤—à¤£"),
    NAKSHATRA_YONI("Yoni", "à¤¯à¥‹à¤¨à¤¿"),
    NAKSHATRA_ANIMAL("Animal", "à¤ªà¤¶à¥"),
    NAKSHATRA_BIRD("Bird", "à¤ªà¤•à¥à¤·à¥€"),
    NAKSHATRA_TREE("Tree", "à¤µà¥ƒà¤•à¥à¤·"),
    NAKSHATRA_NATURE("Nature", "à¤ªà¥à¤°à¤•à¥ƒà¤¤à¤¿"),
    NAKSHATRA_GENDER("Gender", "à¤²à¤¿à¤™à¥à¤—"),
    NAKSHATRA_MALE("Male", "à¤ªà¥à¤°à¥à¤·"),
    NAKSHATRA_FEMALE("Female", "à¤®à¤¹à¤¿à¤²à¤¾"),
    NAKSHATRA_CAREERS("Careers", "à¤•à¥à¤¯à¤¾à¤°à¤¿à¤¯à¤°"),

    // Nakshatra Nature types
    NAKSHATRA_NATURE_SWIFT("Swift (Kshipra)", "à¤•à¥à¤·à¤¿à¤ªà¥à¤° (à¤›à¤¿à¤Ÿà¥‹)"),
    NAKSHATRA_NATURE_FIERCE("Fierce (Ugra)", "à¤‰à¤—à¥à¤° (à¤¤à¥€à¤µà¥à¤°)"),
    NAKSHATRA_NATURE_MIXED("Mixed (Mishra)", "à¤®à¤¿à¤¶à¥à¤°"),
    NAKSHATRA_NATURE_FIXED("Fixed (Dhruva)", "à¤§à¥à¤°à¥à¤µ (à¤¸à¥à¤¥à¤¿à¤°)"),
    NAKSHATRA_NATURE_SOFT("Soft (Mridu)", "à¤®à¥ƒà¤¦à¥ (à¤•à¥‹à¤®à¤²)"),
    NAKSHATRA_NATURE_SHARP("Sharp (Tikshna)", "à¤¤à¥€à¤•à¥à¤·à¥à¤£"),
    NAKSHATRA_NATURE_MOVABLE("Movable (Chara)", "à¤šà¤°"),
    NAKSHATRA_NATURE_LIGHT("Light (Laghu)", "à¤²à¤˜à¥"),

    // Guna types
    GUNA_RAJAS("Rajas", "à¤°à¤œà¤¸à¥"),
    GUNA_TAMAS("Tamas", "à¤¤à¤®à¤¸à¥"),

    // ============================================
    // DASHA SANDHI ANALYZER STRINGS
    // ============================================
    SANDHI_INTENSITY_CRITICAL("Critical", "à¤—à¤®à¥à¤­à¥€à¤°"),
    SANDHI_INTENSITY_HIGH("High", "à¤‰à¤šà¥à¤š"),
    SANDHI_INTENSITY_MODERATE("Moderate", "à¤®à¤§à¥à¤¯à¤®"),
    SANDHI_INTENSITY_MILD("Mild", "à¤¹à¤²à¥à¤•à¤¾"),
    SANDHI_INTENSITY_MINIMAL("Minimal", "à¤¨à¥à¤¯à¥‚à¤¨à¤¤à¤®"),
    TRANSITION_FRIEND_FRIEND("Friend to Friend", "à¤®à¤¿à¤¤à¥à¤° à¤¦à¥‡à¤–à¤¿ à¤®à¤¿à¤¤à¥à¤°"),
    TRANSITION_FRIEND_NEUTRAL("Friend to Neutral", "à¤®à¤¿à¤¤à¥à¤° à¤¦à¥‡à¤–à¤¿ à¤¤à¤Ÿà¤¸à¥à¤¥"),
    TRANSITION_FRIEND_ENEMY("Friend to Enemy", "à¤®à¤¿à¤¤à¥à¤° à¤¦à¥‡à¤–à¤¿ à¤¶à¤¤à¥à¤°à¥"),
    TRANSITION_NEUTRAL_FRIEND("Neutral to Friend", "à¤¤à¤Ÿà¤¸à¥à¤¥ à¤¦à¥‡à¤–à¤¿ à¤®à¤¿à¤¤à¥à¤°"),
    TRANSITION_NEUTRAL_NEUTRAL("Neutral to Neutral", "à¤¤à¤Ÿà¤¸à¥à¤¥ à¤¦à¥‡à¤–à¤¿ à¤¤à¤Ÿà¤¸à¥à¤¥"),
    TRANSITION_NEUTRAL_ENEMY("Neutral to Enemy", "à¤¤à¤Ÿà¤¸à¥à¤¥ à¤¦à¥‡à¤–à¤¿ à¤¶à¤¤à¥à¤°à¥"),
    TRANSITION_ENEMY_FRIEND("Enemy to Friend", "à¤¶à¤¤à¥à¤°à¥ à¤¦à¥‡à¤–à¤¿ à¤®à¤¿à¤¤à¥à¤°"),
    TRANSITION_ENEMY_NEUTRAL("Enemy to Neutral", "à¤¶à¤¤à¥à¤°à¥ à¤¦à¥‡à¤–à¤¿ à¤¤à¤Ÿà¤¸à¥à¤¥"),
    TRANSITION_ENEMY_ENEMY("Enemy to Enemy", "à¤¶à¤¤à¥à¤°à¥ à¤¦à¥‡à¤–à¤¿ à¤¶à¤¤à¥à¤°à¥"),

    // ============================================
    // GOCHARA VEDHA CALCULATOR STRINGS
    // ============================================
    VEDHA_COMPLETE("Complete", "à¤ªà¥‚à¤°à¥à¤£"),
    VEDHA_STRONG("Strong", "à¤¬à¤²à¤¿à¤¯à¥‹"),
    VEDHA_MODERATE("Moderate", "à¤®à¤§à¥à¤¯à¤®"),
    VEDHA_PARTIAL("Partial", "à¤†à¤‚à¤¶à¤¿à¤•"),
    VEDHA_NONE("None", "à¤•à¥à¤¨à¥ˆ à¤›à¥ˆà¤¨"),
    TRANSIT_EXCELLENT("Excellent", "à¤‰à¤¤à¥à¤•à¥ƒà¤·à¥à¤Ÿ"),
    TRANSIT_GOOD("Good", "à¤°à¤¾à¤®à¥à¤°à¥‹"),
    TRANSIT_MODERATE("Moderate", "à¤®à¤§à¥à¤¯à¤®"),
    TRANSIT_WEAK("Weak", "à¤•à¤®à¤œà¥‹à¤°"),
    TRANSIT_NULLIFIED("Nullified", "à¤¶à¥‚à¤¨à¥à¤¯"),
    TRANSIT_UNFAVORABLE("Unfavorable", "à¤ªà¥à¤°à¤¤à¤¿à¤•à¥‚à¤²"),

    // ============================================
    // KEMADRUMA YOGA CALCULATOR STRINGS
    // ============================================
    KEMADRUMA_NOT_PRESENT("Not Present", "à¤‰à¤ªà¤¸à¥à¤¥à¤¿à¤¤ à¤›à¥ˆà¤¨"),
    KEMADRUMA_FULLY_CANCELLED("Fully Cancelled", "à¤ªà¥‚à¤°à¥à¤£ à¤°à¤¦à¥à¤¦"),
    KEMADRUMA_MOSTLY_CANCELLED("Mostly Cancelled", "à¤ªà¥à¤°à¤¾à¤¯: à¤°à¤¦à¥à¤¦"),
    KEMADRUMA_PARTIALLY_CANCELLED("Partially Cancelled", "à¤†à¤‚à¤¶à¤¿à¤• à¤°à¤¦à¥à¤¦"),
    KEMADRUMA_WEAKLY_CANCELLED("Weakly Cancelled", "à¤•à¤®à¤œà¥‹à¤° à¤°à¤¦à¥à¤¦"),
    KEMADRUMA_ACTIVE_MODERATE("Active - Moderate", "à¤¸à¤•à¥à¤°à¤¿à¤¯ - à¤®à¤§à¥à¤¯à¤®"),
    KEMADRUMA_ACTIVE_SEVERE("Active - Severe", "à¤¸à¤•à¥à¤°à¤¿à¤¯ - à¤—à¤®à¥à¤­à¥€à¤°"),
    BHANGA_KENDRA_MOON("Kendra Moon", "à¤•à¥‡à¤¨à¥à¤¦à¥à¤° à¤šà¤¨à¥à¤¦à¥à¤°"),
    BHANGA_KENDRA_LAGNA("Kendra Lagna", "à¤•à¥‡à¤¨à¥à¤¦à¥à¤° à¤²à¤—à¥à¤¨"),
    BHANGA_MOON_KENDRA("Moon in Kendra", "à¤šà¤¨à¥à¤¦à¥à¤° à¤•à¥‡à¤¨à¥à¤¦à¥à¤°à¤®à¤¾"),
    BHANGA_BENEFIC_ASPECT("Benefic Aspect", "à¤¶à¥à¤­ à¤¦à¥ƒà¤·à¥à¤Ÿà¤¿"),
    BHANGA_BENEFIC_CONJUNCTION("Benefic Conjunction", "à¤¶à¥à¤­ à¤¸à¤‚à¤¯à¥‹à¤—"),
    BHANGA_MOON_EXALTED("Moon Exalted", "à¤šà¤¨à¥à¤¦à¥à¤° à¤‰à¤šà¥à¤š"),
    BHANGA_MOON_OWN("Moon Own Sign", "à¤šà¤¨à¥à¤¦à¥à¤° à¤¸à¥à¤µà¤°à¤¾à¤¶à¤¿"),
    BHANGA_MOON_FRIEND("Moon with Friend", "à¤šà¤¨à¥à¤¦à¥à¤° à¤®à¤¿à¤¤à¥à¤° à¤¸à¤¾à¤¥"),
    BHANGA_FULL_MOON("Full Moon", "à¤ªà¥‚à¤°à¥à¤£ à¤šà¤¨à¥à¤¦à¥à¤°"),
    BHANGA_ANGULAR_MOON("Angular Moon", "à¤•à¥‡à¤¨à¥à¤¦à¥à¤°à¥€à¤¯ à¤šà¤¨à¥à¤¦à¥à¤°"),
    BHANGA_STRONG_DISPOSITOR("Strong Dispositor", "à¤¬à¤²à¤¿à¤¯à¥‹ à¤¸à¥à¤µà¤¾à¤®à¥€"),
    BHANGA_JUPITER_ASPECT("Jupiter Aspect", "à¤—à¥à¤°à¥ à¤¦à¥ƒà¤·à¥à¤Ÿà¤¿"),
    BHANGA_VENUS_ASPECT("Venus Aspect", "à¤¶à¥à¤•à¥à¤° à¤¦à¥ƒà¤·à¥à¤Ÿà¤¿"),

    // ============================================
    // TARABALA CALCULATOR STRINGS
    // ============================================
    TARA_JANMA("Janma", "à¤œà¤¨à¥à¤®"),
    TARA_SAMPAT("Sampat", "à¤¸à¤®à¥à¤ªà¤¤à¥à¤¤à¤¿"),
    TARA_VIPAT("Vipat", "à¤µà¤¿à¤ªà¤¤à¥à¤¤à¤¿"),
    TARA_KSHEMA("Kshema", "à¤•à¥à¤·à¥‡à¤®"),
    TARA_PRATYARI("Pratyari", "à¤ªà¥à¤°à¤¤à¥à¤¯à¤¾à¤°à¥€"),
    TARA_SADHAKA("Sadhaka", "à¤¸à¤¾à¤§à¤•"),
    TARA_VADHA("Vadha", "à¤µà¤§"),
    TARA_MITRA("Mitra", "à¤®à¤¿à¤¤à¥à¤°"),
    TARA_PARAMA_MITRA("Parama Mitra", "à¤ªà¤°à¤® à¤®à¤¿à¤¤à¥à¤°"),
    TARA_JANMA_DESC("Birth Star", "à¤œà¤¨à¥à¤® à¤¨à¤•à¥à¤·à¤¤à¥à¤°"),
    TARA_SAMPAT_DESC("Wealth Star", "à¤¸à¤®à¥à¤ªà¤¤à¥à¤¤à¤¿ à¤¨à¤•à¥à¤·à¤¤à¥à¤°"),
    TARA_VIPAT_DESC("Misfortune Star", "à¤µà¤¿à¤ªà¤¤à¥à¤¤à¤¿ à¤¨à¤•à¥à¤·à¤¤à¥à¤°"),
    TARA_KSHEMA_DESC("Welfare Star", "à¤•à¥à¤·à¥‡à¤® à¤¨à¤•à¥à¤·à¤¤à¥à¤°"),
    TARA_PRATYARI_DESC("Enemy Star", "à¤ªà¥à¤°à¤¤à¥à¤¯à¤¾à¤°à¥€ à¤¨à¤•à¥à¤·à¤¤à¥à¤°"),
    TARA_SADHAKA_DESC("Achiever Star", "à¤¸à¤¾à¤§à¤• à¤¨à¤•à¥à¤·à¤¤à¥à¤°"),
    TARA_VADHA_DESC("Death Star", "à¤µà¤§ à¤¨à¤•à¥à¤·à¤¤à¥à¤°"),
    TARA_MITRA_DESC("Friend Star", "à¤®à¤¿à¤¤à¥à¤° à¤¨à¤•à¥à¤·à¤¤à¥à¤°"),
    TARA_PARAMA_MITRA_DESC("Highest Friend Star", "à¤ªà¤°à¤® à¤®à¤¿à¤¤à¥à¤° à¤¨à¤•à¥à¤·à¤¤à¥à¤°"),
    CHANDRABALA_EXCELLENT("Excellent", "à¤‰à¤¤à¥à¤•à¥ƒà¤·à¥à¤Ÿ"),
    CHANDRABALA_GOOD("Good", "à¤°à¤¾à¤®à¥à¤°à¥‹"),
    CHANDRABALA_NEUTRAL("Neutral", "à¤¤à¤Ÿà¤¸à¥à¤¥"),
    CHANDRABALA_WEAK("Weak", "à¤•à¤®à¤œà¥‹à¤°"),
    CHANDRABALA_UNFAVORABLE("Unfavorable", "à¤ªà¥à¤°à¤¤à¤¿à¤•à¥‚à¤²"),
    COMBINED_HIGHLY_FAVORABLE("Highly Favorable", "à¤…à¤¤à¥à¤¯à¤¨à¥à¤¤ à¤…à¤¨à¥à¤•à¥‚à¤²"),
    COMBINED_FAVORABLE("Favorable", "à¤…à¤¨à¥à¤•à¥‚à¤²"),
    COMBINED_MIXED("Mixed", "à¤®à¤¿à¤¶à¥à¤°à¤¿à¤¤"),
    COMBINED_CHALLENGING("Challenging", "à¤šà¥à¤¨à¥Œà¤¤à¥€à¤ªà¥‚à¤°à¥à¤£"),
    COMBINED_UNFAVORABLE("Unfavorable", "à¤ªà¥à¤°à¤¤à¤¿à¤•à¥‚à¤²"),

    // ============================================
    // TARABALA SCREEN STRINGS
    // ============================================
    TARABALA_TITLE("Tarabala", "à¤¤à¤¾à¤°à¤¬à¤²"),
    TARABALA_TODAY("Today", "à¤†à¤œ"),
    TARABALA_WEEKLY("Weekly", "à¤¸à¤¾à¤ªà¥à¤¤à¤¾à¤¹à¤¿à¤•"),
    TARABALA_ALL_NAKSHATRAS("All 27 Nakshatras", "à¤¸à¤¬à¥ˆ à¥¨à¥­ à¤¨à¤•à¥à¤·à¤¤à¥à¤°"),
    TARABALA_WHAT_IS("What is Tarabala?", "à¤¤à¤¾à¤°à¤¬à¤² à¤•à¥‡ à¤¹à¥‹?"),
    TARABALA_UNABLE_CALCULATE("Unable to calculate Tarabala without birth details", "à¤œà¤¨à¥à¤® à¤µà¤¿à¤µà¤°à¤£ à¤¬à¤¿à¤¨à¤¾ à¤¤à¤¾à¤°à¤¬à¤² à¤—à¤£à¤¨à¤¾ à¤—à¤°à¥à¤¨ à¤¸à¤•à¥à¤·à¤® à¤›à¥ˆà¤¨"),
    TARABALA_TODAY_STRENGTH("Today's Strength", "à¤†à¤œà¤•à¥‹ à¤¶à¤•à¥à¤¤à¤¿"),
    TARABALA_LABEL("Tarabala", "à¤¤à¤¾à¤°à¤¬à¤²"),
    TARABALA_STAR_STRENGTH("Star Strength", "à¤¨à¤•à¥à¤·à¤¤à¥à¤° à¤¶à¤•à¥à¤¤à¤¿"),
    TARABALA_FAVORABLE("Favorable", "à¤…à¤¨à¥à¤•à¥‚à¤²"),
    TARABALA_CHALLENGING("Challenging", "à¤šà¥à¤¨à¥Œà¤¤à¥€à¤ªà¥‚à¤°à¥à¤£"),
    TARABALA_BIRTH_STAR("Birth Star", "à¤œà¤¨à¥à¤® à¤¨à¤•à¥à¤·à¤¤à¥à¤°"),
    TARABALA_TRANSIT_STAR("Transit Star", "à¤—à¥‹à¤šà¤° à¤¨à¤•à¥à¤·à¤¤à¥à¤°"),
    TARABALA_TARA_TYPE("Tara Type", "à¤¤à¤¾à¤° à¤ªà¥à¤°à¤•à¤¾à¤°"),
    TARABALA_CYCLE("Cycle", "à¤šà¤•à¥à¤°"),
    CHANDRABALA_LABEL("Chandrabala", "à¤šà¤¨à¥à¤¦à¥à¤°à¤¬à¤²"),
    CHANDRABALA_MOON_STRENGTH("Moon Strength", "à¤šà¤¨à¥à¤¦à¥à¤° à¤¶à¤•à¥à¤¤à¤¿"),
    CHANDRABALA_NATAL_MOON("Natal Moon", "à¤œà¤¨à¥à¤®à¤•à¤¾à¤²à¥€à¤¨ à¤šà¤¨à¥à¤¦à¥à¤°"),
    CHANDRABALA_TRANSIT_MOON("Transit Moon", "à¤—à¥‹à¤šà¤° à¤šà¤¨à¥à¤¦à¥à¤°"),
    CHANDRABALA_HOUSE("House", "à¤­à¤¾à¤µ"),
    CHANDRABALA_SIGNIFICATIONS("Significations", "à¤•à¤°à¤•à¤¤à¥à¤µ"),
    TARABALA_ACTIVITIES("Activities", "à¤—à¤¤à¤¿à¤µà¤¿à¤§à¤¿à¤¹à¤°à¥‚"),
    TARABALA_FAVORABLE_ACTIVITIES("Favorable Activities", "à¤…à¤¨à¥à¤•à¥‚à¤² à¤—à¤¤à¤¿à¤µà¤¿à¤§à¤¿à¤¹à¤°à¥‚"),
    TARABALA_AVOID_ACTIVITIES("Activities to Avoid", "à¤Ÿà¤¾à¤¢à¤¾ à¤°à¤¹à¤¨à¥ à¤ªà¤°à¥à¤¨à¥‡ à¤—à¤¤à¤¿à¤µà¤¿à¤§à¤¿à¤¹à¤°à¥‚"),
    TARABALA_DAILY_BREAKDOWN("Daily Breakdown", "à¤¦à¥ˆà¤¨à¤¿à¤• à¤µà¤¿à¤­à¤¾à¤œà¤¨"),
    TARABALA_WEEKLY_OVERVIEW("Weekly Overview", "à¤¸à¤¾à¤ªà¥à¤¤à¤¾à¤¹à¤¿à¤• à¤¸à¤¿à¤‚à¤¹à¤¾à¤µà¤²à¥‹à¤•à¤¨"),
    TARABALA_BEST_DAY("Best Day", "à¤¸à¤¬à¥ˆà¤­à¤¨à¥à¤¦à¤¾ à¤°à¤¾à¤®à¥à¤°à¥‹ à¤¦à¤¿à¤¨"),
    TARABALA_AVOID_DAY("Day to Avoid", "à¤Ÿà¤¾à¤¢à¤¾ à¤°à¤¹à¤¨à¥ à¤ªà¤°à¥à¤¨à¥‡ à¤¦à¤¿à¤¨"),
    TARABALA_BEST_LABEL("Best", "à¤¸à¤¬à¥ˆà¤­à¤¨à¥à¤¦à¤¾ à¤°à¤¾à¤®à¥à¤°à¥‹"),
    TARABALA_ALL_27_DESC("All 27 Nakshatras with their Tarabala values", "à¤¸à¤¬à¥ˆ à¥¨à¥­ à¤¨à¤•à¥à¤·à¤¤à¥à¤° à¤¤à¤¿à¤¨à¥€à¤¹à¤°à¥‚à¤•à¥‹ à¤¤à¤¾à¤°à¤¬à¤² à¤®à¤¾à¤¨à¤¹à¤°à¥‚ à¤¸à¤¾à¤¥"),
    TARABALA_EXPLANATION("Explanation", "à¤µà¥à¤¯à¤¾à¤–à¥à¤¯à¤¾"),
    CHANDRABALA_EXPLANATION("Explanation", "à¤µà¥à¤¯à¤¾à¤–à¥à¤¯à¤¾"),

    // ============================================
    // PRASHNA (HORARY) CALCULATOR STRINGS
    // ============================================
    
    // PrashnaCategory enum
    PRASHNA_CAT_YES_NO("Yes/No", "à¤¹à¥‹/à¤¹à¥‹à¤‡à¤¨"),
    PRASHNA_CAT_YES_NO_DESC("Simple yes or no questions", "à¤¸à¤¾à¤§à¤¾à¤°à¤£ à¤¹à¥‹ à¤µà¤¾ à¤¹à¥‹à¤‡à¤¨ à¤ªà¥à¤°à¤¶à¥à¤¨à¤¹à¤°à¥‚"),
    PRASHNA_CAT_CAREER("Career", "à¤•à¥à¤¯à¤¾à¤°à¤¿à¤¯à¤°"),
    PRASHNA_CAT_CAREER_DESC("Job, profession, and career-related questions", "à¤œà¤¾à¤—à¤¿à¤°, à¤ªà¥‡à¤¶à¤¾ à¤° à¤•à¥à¤¯à¤¾à¤°à¤¿à¤¯à¤° à¤¸à¤®à¥à¤¬à¤¨à¥à¤§à¥€ à¤ªà¥à¤°à¤¶à¥à¤¨à¤¹à¤°à¥‚"),
    PRASHNA_CAT_MARRIAGE("Marriage", "à¤µà¤¿à¤µà¤¾à¤¹"),
    PRASHNA_CAT_MARRIAGE_DESC("Marriage and spouse-related questions", "à¤µà¤¿à¤µà¤¾à¤¹ à¤° à¤œà¥€à¤µà¤¨à¤¸à¤¾à¤¥à¥€ à¤¸à¤®à¥à¤¬à¤¨à¥à¤§à¥€ à¤ªà¥à¤°à¤¶à¥à¤¨à¤¹à¤°à¥‚"),
    PRASHNA_CAT_CHILDREN("Children", "à¤¸à¤¨à¥à¤¤à¤¾à¤¨"),
    PRASHNA_CAT_CHILDREN_DESC("Questions about children and progeny", "à¤¸à¤¨à¥à¤¤à¤¾à¤¨ à¤° à¤¸à¤¨à¥à¤¤à¤¤à¤¿à¤•à¤¾ à¤¬à¤¾à¤°à¥‡à¤®à¤¾ à¤ªà¥à¤°à¤¶à¥à¤¨à¤¹à¤°à¥‚"),
    PRASHNA_CAT_HEALTH("Health", "à¤¸à¥à¤µà¤¾à¤¸à¥à¤¥à¥à¤¯"),
    PRASHNA_CAT_HEALTH_DESC("Health and illness-related questions", "à¤¸à¥à¤µà¤¾à¤¸à¥à¤¥à¥à¤¯ à¤° à¤°à¥‹à¤— à¤¸à¤®à¥à¤¬à¤¨à¥à¤§à¥€ à¤ªà¥à¤°à¤¶à¥à¤¨à¤¹à¤°à¥‚"),
    PRASHNA_CAT_WEALTH("Wealth", "à¤§à¤¨-à¤¸à¤®à¥à¤ªà¤¤à¥à¤¤à¤¿"),
    PRASHNA_CAT_WEALTH_DESC("Financial and wealth-related questions", "à¤†à¤°à¥à¤¥à¤¿à¤• à¤° à¤§à¤¨-à¤¸à¤®à¥à¤ªà¤¤à¥à¤¤à¤¿ à¤¸à¤®à¥à¤¬à¤¨à¥à¤§à¥€ à¤ªà¥à¤°à¤¶à¥à¤¨à¤¹à¤°à¥‚"),
    PRASHNA_CAT_PROPERTY("Property", "à¤¸à¤®à¥à¤ªà¤¤à¥à¤¤à¤¿"),
    PRASHNA_CAT_PROPERTY_DESC("Real estate and property questions", "à¤œà¤—à¥à¤—à¤¾-à¤œà¤®à¤¿à¤¨ à¤° à¤¸à¤®à¥à¤ªà¤¤à¥à¤¤à¤¿ à¤¸à¤®à¥à¤¬à¤¨à¥à¤§à¥€ à¤ªà¥à¤°à¤¶à¥à¤¨à¤¹à¤°à¥‚"),
    PRASHNA_CAT_TRAVEL("Travel", "à¤¯à¤¾à¤¤à¥à¤°à¤¾"),
    PRASHNA_CAT_TRAVEL_DESC("Journey and travel-related questions", "à¤¯à¤¾à¤¤à¥à¤°à¤¾ à¤¸à¤®à¥à¤¬à¤¨à¥à¤§à¥€ à¤ªà¥à¤°à¤¶à¥à¤¨à¤¹à¤°à¥‚"),
    PRASHNA_CAT_EDUCATION("Education", "à¤¶à¤¿à¤•à¥à¤·à¤¾"),
    PRASHNA_CAT_EDUCATION_DESC("Studies and educational questions", "à¤…à¤§à¥à¤¯à¤¯à¤¨ à¤° à¤¶à¥ˆà¤•à¥à¤·à¤¿à¤• à¤ªà¥à¤°à¤¶à¥à¤¨à¤¹à¤°à¥‚"),
    PRASHNA_CAT_LEGAL("Legal", "à¤•à¤¾à¤¨à¥à¤¨à¥€"),
    PRASHNA_CAT_LEGAL_DESC("Court cases and legal matters", "à¤®à¥à¤¦à¥à¤¦à¤¾-à¤®à¤¾à¤®à¤¿à¤²à¤¾ à¤° à¤•à¤¾à¤¨à¥à¤¨à¥€ à¤µà¤¿à¤·à¤¯à¤¹à¤°à¥‚"),
    PRASHNA_CAT_LOST_OBJECT("Lost Object", "à¤¹à¤°à¤¾à¤à¤•à¥‹ à¤µà¤¸à¥à¤¤à¥"),
    PRASHNA_CAT_LOST_OBJECT_DESC("Finding lost or stolen items", "à¤¹à¤°à¤¾à¤à¤•à¥‹ à¤µà¤¾ à¤šà¥‹à¤°à¥€ à¤­à¤à¤•à¥‹ à¤µà¤¸à¥à¤¤à¥ à¤–à¥‹à¤œà¥à¤¨à¥‡"),
    PRASHNA_CAT_RELATIONSHIP("Relationship", "à¤¸à¤®à¥à¤¬à¤¨à¥à¤§"),
    PRASHNA_CAT_RELATIONSHIP_DESC("Love and relationship questions", "à¤ªà¥à¤°à¥‡à¤® à¤° à¤¸à¤®à¥à¤¬à¤¨à¥à¤§ à¤¸à¤®à¥à¤¬à¤¨à¥à¤§à¥€ à¤ªà¥à¤°à¤¶à¥à¤¨à¤¹à¤°à¥‚"),
    PRASHNA_CAT_BUSINESS("Business", "à¤µà¥à¤¯à¤¾à¤ªà¤¾à¤°"),
    PRASHNA_CAT_BUSINESS_DESC("Business partnership and deals", "à¤µà¥à¤¯à¤¾à¤ªà¤¾à¤° à¤¸à¤¾à¤à¥‡à¤¦à¤¾à¤°à¥€ à¤° à¤¸à¥Œà¤¦à¤¾à¤¹à¤°à¥‚"),
    PRASHNA_CAT_SPIRITUAL("Spiritual", "à¤†à¤§à¥à¤¯à¤¾à¤¤à¥à¤®à¤¿à¤•"),
    PRASHNA_CAT_SPIRITUAL_DESC("Spiritual and religious questions", "à¤†à¤§à¥à¤¯à¤¾à¤¤à¥à¤®à¤¿à¤• à¤° à¤§à¤¾à¤°à¥à¤®à¤¿à¤• à¤ªà¥à¤°à¤¶à¥à¤¨à¤¹à¤°à¥‚"),
    PRASHNA_CAT_GENERAL("General", "à¤¸à¤¾à¤®à¤¾à¤¨à¥à¤¯"),
    PRASHNA_CAT_GENERAL_DESC("General questions and queries", "à¤¸à¤¾à¤®à¤¾à¤¨à¥à¤¯ à¤ªà¥à¤°à¤¶à¥à¤¨à¤¹à¤°à¥‚"),

    // Tattva enum
    PRASHNA_TATTVA_FIRE("Agni/Fire", "à¤…à¤—à¥à¤¨à¤¿"),
    PRASHNA_TATTVA_FIRE_SIGNS("Aries, Leo, Sagittarius", "à¤®à¥‡à¤·, à¤¸à¤¿à¤‚à¤¹, à¤§à¤¨à¥"),
    PRASHNA_TATTVA_EARTH("Prithvi/Earth", "à¤ªà¥ƒà¤¥à¥à¤µà¥€"),
    PRASHNA_TATTVA_EARTH_SIGNS("Taurus, Virgo, Capricorn", "à¤µà¥ƒà¤·, à¤•à¤¨à¥à¤¯à¤¾, à¤®à¤•à¤°"),
    PRASHNA_TATTVA_AIR("Vayu/Air", "à¤µà¤¾à¤¯à¥"),
    PRASHNA_TATTVA_AIR_SIGNS("Gemini, Libra, Aquarius", "à¤®à¤¿à¤¥à¥à¤¨, à¤¤à¥à¤²à¤¾, à¤•à¥à¤®à¥à¤­"),
    PRASHNA_TATTVA_WATER("Jala/Water", "à¤œà¤²"),
    PRASHNA_TATTVA_WATER_SIGNS("Cancer, Scorpio, Pisces", "à¤•à¤°à¥à¤•à¤Ÿ, à¤µà¥ƒà¤¶à¥à¤šà¤¿à¤•, à¤®à¥€à¤¨"),
    PRASHNA_TATTVA_ETHER("Akasha/Ether", "à¤†à¤•à¤¾à¤¶"),
    PRASHNA_TATTVA_ETHER_SIGNS("None - represents void/space", "à¤•à¥à¤¨à¥ˆ à¤ªà¤¨à¤¿ à¤¹à¥‹à¤‡à¤¨ - à¤¶à¥‚à¤¨à¥à¤¯/à¤…à¤¨à¥à¤¤à¤°à¤¿à¤•à¥à¤·"),

    // TimingUnit enum
    PRASHNA_TIMING_HOURS("Hours", "à¤˜à¤£à¥à¤Ÿà¤¾"),
    PRASHNA_TIMING_DAYS("Days", "à¤¦à¤¿à¤¨"),
    PRASHNA_TIMING_WEEKS("Weeks", "à¤¹à¤ªà¥à¤¤à¤¾"),
    PRASHNA_TIMING_MONTHS("Months", "à¤®à¤¹à¤¿à¤¨à¤¾"),
    PRASHNA_TIMING_YEARS("Years", "à¤µà¤°à¥à¤·"),

    // PrashnaVerdict enum
    PRASHNA_VERDICT_STRONGLY_YES("Strongly Yes - Success Indicated", "à¤¨à¤¿à¤¶à¥à¤šà¤¿à¤¤ à¤¹à¥‹ - à¤¸à¤«à¤²à¤¤à¤¾à¤•à¥‹ à¤¸à¤‚à¤•à¥‡à¤¤"),
    PRASHNA_VERDICT_YES("Yes - Favorable Outcome", "à¤¹à¥‹ - à¤…à¤¨à¥à¤•à¥‚à¤² à¤ªà¤°à¤¿à¤£à¤¾à¤®"),
    PRASHNA_VERDICT_LIKELY_YES("Likely Yes - Conditions Apply", "à¤¸à¤®à¥à¤­à¤µà¤¤à¤ƒ à¤¹à¥‹ - à¤¸à¤°à¥à¤¤à¤¹à¤°à¥‚ à¤²à¤¾à¤—à¥‚"),
    PRASHNA_VERDICT_UNCERTAIN("Uncertain - Mixed Indications", "à¤…à¤¨à¤¿à¤¶à¥à¤šà¤¿à¤¤ - à¤®à¤¿à¤¶à¥à¤°à¤¿à¤¤ à¤¸à¤‚à¤•à¥‡à¤¤"),
    PRASHNA_VERDICT_LIKELY_NO("Likely No - Difficulties Indicated", "à¤¸à¤®à¥à¤­à¤µà¤¤à¤ƒ à¤¹à¥‹à¤‡à¤¨ - à¤•à¤ à¤¿à¤¨à¤¾à¤‡à¤¹à¤°à¥‚ à¤¸à¤‚à¤•à¥‡à¤¤"),
    PRASHNA_VERDICT_NO("No - Unfavorable Outcome", "à¤¹à¥‹à¤‡à¤¨ - à¤ªà¥à¤°à¤¤à¤¿à¤•à¥‚à¤² à¤ªà¤°à¤¿à¤£à¤¾à¤®"),
    PRASHNA_VERDICT_STRONGLY_NO("Strongly No - Failure Indicated", "à¤¨à¤¿à¤¶à¥à¤šà¤¿à¤¤ à¤¹à¥‹à¤‡à¤¨ - à¤…à¤¸à¤«à¤²à¤¤à¤¾à¤•à¥‹ à¤¸à¤‚à¤•à¥‡à¤¤"),
    PRASHNA_VERDICT_TIMING_DEPENDENT("Timing Dependent - Wait Indicated", "à¤¸à¤®à¤¯ à¤¨à¤¿à¤°à¥à¤­à¤° - à¤ªà¥à¤°à¤¤à¥€à¤•à¥à¤·à¤¾ à¤—à¤°à¥à¤¨à¥à¤¹à¥‹à¤¸à¥"),

    // CertaintyLevel enum
    PRASHNA_CERTAINTY_VERY_HIGH("Very High Certainty", "à¤…à¤¤à¥à¤¯à¤§à¤¿à¤• à¤¨à¤¿à¤¶à¥à¤šà¤¿à¤¤à¤¤à¤¾"),
    PRASHNA_CERTAINTY_HIGH("High Certainty", "à¤‰à¤šà¥à¤š à¤¨à¤¿à¤¶à¥à¤šà¤¿à¤¤à¤¤à¤¾"),
    PRASHNA_CERTAINTY_MODERATE("Moderate Certainty", "à¤®à¤§à¥à¤¯à¤® à¤¨à¤¿à¤¶à¥à¤šà¤¿à¤¤à¤¤à¤¾"),
    PRASHNA_CERTAINTY_LOW("Low Certainty", "à¤¨à¥à¤¯à¥‚à¤¨ à¤¨à¤¿à¤¶à¥à¤šà¤¿à¤¤à¤¤à¤¾"),
    PRASHNA_CERTAINTY_VERY_LOW("Very Low Certainty", "à¤…à¤¤à¤¿ à¤¨à¥à¤¯à¥‚à¤¨ à¤¨à¤¿à¤¶à¥à¤šà¤¿à¤¤à¤¤à¤¾"),

    // MoonStrength enum
    PRASHNA_MOON_EXCELLENT("Excellent", "à¤‰à¤¤à¥à¤•à¥ƒà¤·à¥à¤Ÿ"),
    PRASHNA_MOON_GOOD("Good", "à¤°à¤¾à¤®à¥à¤°à¥‹"),
    PRASHNA_MOON_AVERAGE("Average", "à¤”à¤¸à¤¤"),
    PRASHNA_MOON_WEAK("Weak", "à¤•à¤®à¤œà¥‹à¤°"),
    PRASHNA_MOON_VERY_WEAK("Very Weak", "à¤…à¤¤à¥à¤¯à¤¨à¥à¤¤ à¤•à¤®à¤œà¥‹à¤°"),
    PRASHNA_MOON_AFFLICTED("Afflicted", "à¤ªà¥€à¤¡à¤¿à¤¤"),

    // LagnaCondition enum
    PRASHNA_LAGNA_STRONG("Strong - Well placed lord", "à¤¬à¤²à¤¿à¤¯à¥‹ - à¤°à¤¾à¤®à¥à¤°à¥‹à¤¸à¤à¤— à¤¸à¥à¤¥à¤¾à¤ªà¤¿à¤¤ à¤¸à¥à¤µà¤¾à¤®à¥€"),
    PRASHNA_LAGNA_MODERATE("Moderate - Mixed influences", "à¤®à¤§à¥à¤¯à¤® - à¤®à¤¿à¤¶à¥à¤°à¤¿à¤¤ à¤ªà¥à¤°à¤­à¤¾à¤µà¤¹à¤°à¥‚"),
    PRASHNA_LAGNA_WEAK("Weak - Afflicted or poorly placed", "à¤•à¤®à¤œà¥‹à¤° - à¤ªà¥€à¤¡à¤¿à¤¤ à¤µà¤¾ à¤–à¤°à¤¾à¤¬ à¤¸à¥à¤¥à¤¾à¤ªà¤¿à¤¤"),
    PRASHNA_LAGNA_COMBUST("Combust - Lord too close to Sun", "à¤…à¤¸à¥à¤¤ - à¤¸à¥à¤µà¤¾à¤®à¥€ à¤¸à¥‚à¤°à¥à¤¯à¤•à¥‹ à¤…à¤¤à¥à¤¯à¤§à¤¿à¤• à¤¨à¤œà¤¿à¤•"),
    PRASHNA_LAGNA_RETROGRADE_LORD("Lord is Retrograde", "à¤¸à¥à¤µà¤¾à¤®à¥€ à¤µà¤•à¥à¤°à¥€ à¤›"),

    // StrengthLevel enum
    PRASHNA_STRENGTH_VERY_STRONG("Very Strong", "à¤…à¤¤à¥à¤¯à¤¨à¥à¤¤ à¤¬à¤²à¤¿à¤¯à¥‹"),
    PRASHNA_STRENGTH_STRONG("Strong", "à¤¬à¤²à¤¿à¤¯à¥‹"),
    PRASHNA_STRENGTH_MODERATE("Moderate", "à¤®à¤§à¥à¤¯à¤®"),
    PRASHNA_STRENGTH_WEAK("Weak", "à¤•à¤®à¤œà¥‹à¤°"),
    PRASHNA_STRENGTH_VERY_WEAK("Very Weak", "à¤…à¤¤à¥à¤¯à¤¨à¥à¤¤ à¤•à¤®à¤œà¥‹à¤°"),
    PRASHNA_STRENGTH_DEBILITATED("Debilitated", "à¤¨à¥€à¤š"),

    // AspectType enum
    PRASHNA_ASPECT_CONJUNCTION("Conjunction", "à¤¯à¥à¤¤à¤¿"),
    PRASHNA_ASPECT_SEXTILE("Sextile", "à¤·à¤·à¥à¤ "),
    PRASHNA_ASPECT_SQUARE("Square", "à¤šà¤¤à¥à¤°à¥à¤¥"),
    PRASHNA_ASPECT_TRINE("Trine", "à¤¤à¥à¤°à¤¿à¤•à¥‹à¤£"),
    PRASHNA_ASPECT_OPPOSITION("Opposition", "à¤¸à¤ªà¥à¤¤à¤®"),
    PRASHNA_ASPECT_MARS_4TH("Mars 4th Aspect", "à¤®à¤‚à¤—à¤² à¥ª à¤”à¤‚ à¤¦à¥ƒà¤·à¥à¤Ÿà¤¿"),
    PRASHNA_ASPECT_MARS_8TH("Mars 8th Aspect", "à¤®à¤‚à¤—à¤² à¥® à¤”à¤‚ à¤¦à¥ƒà¤·à¥à¤Ÿà¤¿"),
    PRASHNA_ASPECT_JUPITER_5TH("Jupiter 5th Aspect", "à¤—à¥à¤°à¥ à¥« à¤”à¤‚ à¤¦à¥ƒà¤·à¥à¤Ÿà¤¿"),
    PRASHNA_ASPECT_JUPITER_9TH("Jupiter 9th Aspect", "à¤—à¥à¤°à¥ à¥¯ à¤”à¤‚ à¤¦à¥ƒà¤·à¥à¤Ÿà¤¿"),
    PRASHNA_ASPECT_SATURN_3RD("Saturn 3rd Aspect", "à¤¶à¤¨à¤¿ à¥© à¤°à¥‹ à¤¦à¥ƒà¤·à¥à¤Ÿà¤¿"),
    PRASHNA_ASPECT_SATURN_10TH("Saturn 10th Aspect", "à¤¶à¤¨à¤¿ à¥§à¥¦ à¤”à¤‚ à¤¦à¥ƒà¤·à¥à¤Ÿà¤¿"),

    // HouseStrength enum
    PRASHNA_HOUSE_EXCELLENT("Excellent", "à¤‰à¤¤à¥à¤•à¥ƒà¤·à¥à¤Ÿ"),
    PRASHNA_HOUSE_GOOD("Good", "à¤°à¤¾à¤®à¥à¤°à¥‹"),
    PRASHNA_HOUSE_MODERATE("Moderate", "à¤®à¤§à¥à¤¯à¤®"),
    PRASHNA_HOUSE_POOR("Poor", "à¤•à¤®à¤œà¥‹à¤°"),
    PRASHNA_HOUSE_AFFLICTED("Afflicted", "à¤ªà¥€à¤¡à¤¿à¤¤"),

    // TimingMethod enum
    PRASHNA_METHOD_MOON_TRANSIT("Moon Transit Method", "à¤šà¤¨à¥à¤¦à¥à¤° à¤—à¥‹à¤šà¤° à¤µà¤¿à¤§à¤¿"),
    PRASHNA_METHOD_MOON_NAKSHATRA("Moon Nakshatra Method", "à¤šà¤¨à¥à¤¦à¥à¤° à¤¨à¤•à¥à¤·à¤¤à¥à¤° à¤µà¤¿à¤§à¤¿"),
    PRASHNA_METHOD_HOUSE_LORD_DEGREES("House Lord Degrees", "à¤­à¤¾à¤µà¥‡à¤¶ à¤…à¤‚à¤¶ à¤µà¤¿à¤§à¤¿"),
    PRASHNA_METHOD_LAGNA_DEGREES("Lagna Degrees Method", "à¤²à¤—à¥à¤¨ à¤…à¤‚à¤¶ à¤µà¤¿à¤§à¤¿"),
    PRASHNA_METHOD_PLANETARY_CONJUNCTION("Planetary Conjunction", "à¤—à¥à¤°à¤¹ à¤¯à¥à¤¤à¤¿ à¤µà¤¿à¤§à¤¿"),
    PRASHNA_METHOD_DASHA_BASED("Dasha-based Timing", "à¤¦à¤¶à¤¾ à¤†à¤§à¤¾à¤°à¤¿à¤¤ à¤¸à¤®à¤¯"),
    PRASHNA_METHOD_MIXED("Combined Methods", "à¤¸à¤‚à¤¯à¥à¤•à¥à¤¤ à¤µà¤¿à¤§à¤¿à¤¹à¤°à¥‚"),

    // OmenType enum
    PRASHNA_OMEN_LAGNA("Prashna Lagna Sign", "à¤ªà¥à¤°à¤¶à¥à¤¨ à¤²à¤—à¥à¤¨ à¤°à¤¾à¤¶à¤¿"),
    PRASHNA_OMEN_MOON_PLACEMENT("Moon Placement", "à¤šà¤¨à¥à¤¦à¥à¤° à¤¸à¥à¤¥à¤¿à¤¤à¤¿"),
    PRASHNA_OMEN_HORA_LORD("Hora Lord", "à¤¹à¥‹à¤°à¤¾ à¤¸à¥à¤µà¤¾à¤®à¥€"),
    PRASHNA_OMEN_DAY_LORD("Day Lord", "à¤¦à¤¿à¤¨ à¤¸à¥à¤µà¤¾à¤®à¥€"),
    PRASHNA_OMEN_NAKSHATRA("Question Nakshatra", "à¤ªà¥à¤°à¤¶à¥à¤¨ à¤¨à¤•à¥à¤·à¤¤à¥à¤°"),
    PRASHNA_OMEN_PLANETARY_WAR("Planetary War", "à¤—à¥à¤°à¤¹ à¤¯à¥à¤¦à¥à¤§"),
    PRASHNA_OMEN_COMBUSTION("Combustion", "à¤…à¤¸à¥à¤¤"),
    PRASHNA_OMEN_RETROGRADE("Retrograde Planet", "à¤µà¤•à¥à¤°à¥€ à¤—à¥à¤°à¤¹"),
    PRASHNA_OMEN_GANDANTA("Gandanta Position", "à¤—à¤£à¥à¤¡à¤¾à¤¨à¥à¤¤ à¤¸à¥à¤¥à¤¿à¤¤à¤¿"),
    PRASHNA_OMEN_PUSHKARA("Pushkara Navamsha", "à¤ªà¥à¤·à¥à¤•à¤° à¤¨à¤µà¤¾à¤‚à¤¶"),

    // ============================================
    // NITYA YOGA CALCULATOR STRINGS
    // ============================================
    
    // Auspiciousness enum
    AUSPICIOUSNESS_HIGHLY_AUSPICIOUS("Highly Auspicious", "à¤…à¤¤à¥à¤¯à¤¨à¥à¤¤ à¤¶à¥à¤­"),
    AUSPICIOUSNESS_AUSPICIOUS("Auspicious", "à¤¶à¥à¤­"),
    AUSPICIOUSNESS_NEUTRAL("Neutral", "à¤¤à¤Ÿà¤¸à¥à¤¥"),
    AUSPICIOUSNESS_INAUSPICIOUS("Inauspicious", "à¤…à¤¶à¥à¤­"),
    AUSPICIOUSNESS_HIGHLY_INAUSPICIOUS("Highly Inauspicious", "à¤…à¤¤à¥à¤¯à¤¨à¥à¤¤ à¤…à¤¶à¥à¤­"),

    // YogaStrength enum (generic for Nitya Yoga)
    NITYA_STRENGTH_VERY_STRONG("Very Strong", "à¤…à¤¤à¥à¤¯à¤¨à¥à¤¤ à¤¬à¤²à¤¿à¤¯à¥‹"),
    NITYA_STRENGTH_STRONG("Strong", "à¤¬à¤²à¤¿à¤¯à¥‹"),
    NITYA_STRENGTH_MODERATE("Moderate", "à¤®à¤§à¥à¤¯à¤®"),
    NITYA_STRENGTH_WEAK("Weak", "à¤•à¤®à¤œà¥‹à¤°"),
    NITYA_STRENGTH_VERY_WEAK("Very Weak", "à¤…à¤¤à¥à¤¯à¤¨à¥à¤¤ à¤•à¤®à¤œà¥‹à¤°"),

    // RecommendationCategory enum
    RECOMMEND_SPIRITUAL("Spiritual Practice", "à¤†à¤§à¥à¤¯à¤¾à¤¤à¥à¤®à¤¿à¤• à¤…à¤­à¥à¤¯à¤¾à¤¸"),
    RECOMMEND_ACTIVITY("Activity Guidance", "à¤—à¤¤à¤¿à¤µà¤¿à¤§à¤¿ à¤®à¤¾à¤°à¥à¤—à¤¦à¤°à¥à¤¶à¤¨"),
    RECOMMEND_MANTRA("Mantra Recitation", "à¤®à¤¨à¥à¤¤à¥à¤° à¤œà¤ª"),
    RECOMMEND_TIMING("Timing Advice", "à¤¸à¤®à¤¯ à¤¸à¤²à¥à¤²à¤¾à¤¹"),
    RECOMMEND_GENERAL("General Guidance", "à¤¸à¤¾à¤®à¤¾à¤¨à¥à¤¯ à¤®à¤¾à¤°à¥à¤—à¤¦à¤°à¥à¤¶à¤¨"),

    // ============================================
    // MUHURTA CALCULATOR STRINGS
    // ============================================
    
    // Vara enum (days of week)
    VARA_SUNDAY("Sunday", "à¤†à¤‡à¤¤à¤¬à¤¾à¤°"),
    VARA_MONDAY("Monday", "à¤¸à¥‹à¤®à¤¬à¤¾à¤°"),
    VARA_TUESDAY("Tuesday", "à¤®à¤‚à¤—à¤²à¤¬à¤¾à¤°"),
    VARA_WEDNESDAY("Wednesday", "à¤¬à¥à¤§à¤¬à¤¾à¤°"),
    VARA_THURSDAY("Thursday", "à¤¬à¤¿à¤¹à¥€à¤¬à¤¾à¤°"),
    VARA_FRIDAY("Friday", "à¤¶à¥à¤•à¥à¤°à¤¬à¤¾à¤°"),
    VARA_SATURDAY("Saturday", "à¤¶à¤¨à¤¿à¤¬à¤¾à¤°"),

    // Choghadiya enum
    CHOGHADIYA_UDVEG("Udveg", "à¤‰à¤¦à¥à¤µà¥‡à¤—"),
    CHOGHADIYA_CHAR("Char", "à¤šà¤°"),
    CHOGHADIYA_LABH("Labh", "à¤²à¤¾à¤­"),
    CHOGHADIYA_AMRIT("Amrit", "à¤…à¤®à¥ƒà¤¤"),
    CHOGHADIYA_KAAL("Kaal", "à¤•à¤¾à¤²"),
    CHOGHADIYA_SHUBH("Shubh", "à¤¶à¥à¤­"),
    CHOGHADIYA_ROG("Rog", "à¤°à¥‹à¤—"),

    // ChoghadiyaNature enum
    CHOGHADIYA_NATURE_AUSPICIOUS("Auspicious", "à¤¶à¥à¤­"),
    CHOGHADIYA_NATURE_INAUSPICIOUS("Inauspicious", "à¤…à¤¶à¥à¤­"),
    CHOGHADIYA_NATURE_MIXED("Mixed", "à¤®à¤¿à¤¶à¥à¤°à¤¿à¤¤"),

    // NakshatraNature enum
    NAKSHATRA_NATURE_DREADFUL("Dreadful", "à¤•à¥à¤°à¥‚à¤°"),

    // HoraNature enum
    HORA_NATURE_BENEFIC("Benefic", "à¤¶à¥à¤­"),
    HORA_NATURE_MALEFIC("Malefic", "à¤ªà¤¾à¤ªà¥€"),
    HORA_NATURE_NEUTRAL("Neutral", "à¤¤à¤Ÿà¤¸à¥à¤¥"),

    // TithiNature enum
    TITHI_NATURE_NANDA("Nanda", "à¤¨à¤¨à¥à¤¦à¤¾"),
    TITHI_NATURE_BHADRA("Bhadra", "à¤­à¤¦à¥à¤°à¤¾"),
    TITHI_NATURE_JAYA("Jaya", "à¤œà¤¯à¤¾"),
    TITHI_NATURE_RIKTA("Rikta", "à¤°à¤¿à¤•à¥à¤¤à¤¾"),
    TITHI_NATURE_PURNA("Purna", "à¤ªà¥‚à¤°à¥à¤£à¤¾"),

    // NakshatraGana enum
    NAKSHATRA_GANA_DEVA("Deva", "à¤¦à¥‡à¤µ"),
    NAKSHATRA_GANA_MANUSHYA("Manushya", "à¤®à¤¨à¥à¤·à¥à¤¯"),
    NAKSHATRA_GANA_RAKSHASA("Rakshasa", "à¤°à¤¾à¤•à¥à¤·à¤¸"),

    // NakshatraElement enum
    NAKSHATRA_ELEMENT_FIRE("Fire", "à¤…à¤—à¥à¤¨à¤¿"),
    NAKSHATRA_ELEMENT_EARTH("Earth", "à¤ªà¥ƒà¤¥à¥à¤µà¥€"),
    NAKSHATRA_ELEMENT_AIR("Air", "à¤µà¤¾à¤¯à¥"),
    NAKSHATRA_ELEMENT_WATER("Water", "à¤œà¤²"),
    NAKSHATRA_ELEMENT_ETHER("Ether", "à¤†à¤•à¤¾à¤¶"),

    // KaranaType enum
    KARANA_MOVABLE("Movable", "à¤šà¤°"),
    KARANA_FIXED("Fixed", "à¤¸à¥à¤¥à¤¿à¤°"),

    // ============================================
    // PANCHANGA CALCULATOR STRINGS
    // ============================================
    
    // TithiGroup enum
    TITHI_GROUP_NANDA("Nanda", "à¤¨à¤¨à¥à¤¦à¤¾"),
    TITHI_GROUP_NANDA_NATURE("Joyful", "à¤†à¤¨à¤¨à¥à¤¦à¤®à¤¯"),
    TITHI_GROUP_BHADRA("Bhadra", "à¤­à¤¦à¥à¤°à¤¾"),
    TITHI_GROUP_BHADRA_NATURE("Auspicious", "à¤¶à¥à¤­"),
    TITHI_GROUP_JAYA("Jaya", "à¤œà¤¯à¤¾"),
    TITHI_GROUP_JAYA_NATURE("Victorious", "à¤µà¤¿à¤œà¤¯à¥€"),
    TITHI_GROUP_RIKTA("Rikta", "à¤°à¤¿à¤•à¥à¤¤à¤¾"),
    TITHI_GROUP_RIKTA_NATURE("Empty", "à¤°à¤¿à¤¤à¥à¤¤"),
    TITHI_GROUP_PURNA("Purna", "à¤ªà¥‚à¤°à¥à¤£à¤¾"),
    TITHI_GROUP_PURNA_NATURE("Complete", "à¤ªà¥‚à¤°à¥à¤£"),

    // YogaNature enum
    YOGA_NATURE_AUSPICIOUS("Auspicious", "à¤¶à¥à¤­"),
    YOGA_NATURE_INAUSPICIOUS("Inauspicious", "à¤…à¤¶à¥à¤­"),
    YOGA_NATURE_MIXED("Mixed", "à¤®à¤¿à¤¶à¥à¤°"),

    // Paksha enum
    PAKSHA_SHUKLA("Shukla Paksha", "à¤¶à¥à¤•à¥à¤² à¤ªà¤•à¥à¤·"),
    PAKSHA_KRISHNA("Krishna Paksha", "à¤•à¥ƒà¤·à¥à¤£ à¤ªà¤•à¥à¤·"),

    // ============================================
    // RETROGRADE/COMBUSTION CALCULATOR STRINGS
    // ============================================
    
    // RetrogradeStatus enum
    RETRO_DIRECT("Direct", "à¤®à¤¾à¤°à¥à¤—à¥€"),
    RETRO_RETROGRADE("Retrograde", "à¤µà¤•à¥à¤°à¥€"),
    RETRO_STATIONARY_RETRO("Stationary Retrograde", "à¤¸à¥à¤¥à¤¿à¤° à¤µà¤•à¥à¤°à¥€"),
    RETRO_STATIONARY_DIRECT("Stationary Direct", "à¤¸à¥à¤¥à¤¿à¤° à¤®à¤¾à¤°à¥à¤—à¥€"),
    RETRO_ALWAYS_RETROGRADE("Perpetual Retrograde", "à¤¸à¤¦à¤¾ à¤µà¤•à¥à¤°à¥€"),

    // CombustionStatus enum
    COMBUST_NOT("Not Combust", "à¤…à¤¸à¥à¤¤ à¤›à¥ˆà¤¨"),
    COMBUST_APPROACHING("Approaching Combustion", "à¤…à¤¸à¥à¤¤ à¤¹à¥à¤à¤¦à¥ˆ"),
    COMBUST_COMBUST("Combust", "à¤…à¤¸à¥à¤¤"),
    COMBUST_DEEP("Deep Combustion", "à¤—à¤¹à¤¿à¤°à¥‹ à¤…à¤¸à¥à¤¤"),
    COMBUST_CAZIMI("Cazimi", "à¤•à¤œà¤¼à¤¿à¤®à¥€"),
    COMBUST_SEPARATING("Separating", "à¤…à¤²à¤— à¤¹à¥à¤à¤¦à¥ˆ"),

    // SpeedStatus enum
    SPEED_VERY_FAST("Very Fast", "à¤…à¤¤à¤¿ à¤›à¤¿à¤Ÿà¥‹"),
    SPEED_FAST("Fast", "à¤›à¤¿à¤Ÿà¥‹"),
    SPEED_NORMAL("Normal", "à¤¸à¤¾à¤®à¤¾à¤¨à¥à¤¯"),
    SPEED_SLOW("Slow", "à¤¢à¤¿à¤²à¥‹"),
    SPEED_VERY_SLOW("Very Slow", "à¤…à¤¤à¤¿ à¤¢à¤¿à¤²à¥‹"),
    SPEED_STATIONARY("Stationary", "à¤¸à¥à¤¥à¤¿à¤°"),
    SPEED_RETROGRADE_MOTION("Retrograde Motion", "à¤µà¤•à¥à¤°à¥€ à¤—à¤¤à¤¿"),

    // WarAdvantage enum
    WAR_NORTHERN_LAT("Northern Latitude", "à¤‰à¤¤à¥à¤¤à¤°à¥€ à¤…à¤•à¥à¤·à¤¾à¤‚à¤¶"),
    WAR_BRIGHTNESS("Greater Brightness", "à¤…à¤§à¤¿à¤• à¤šà¤®à¤•"),
    WAR_COMBINED("Both Factors", "à¤¦à¥à¤µà¥ˆ à¤•à¤¾à¤°à¤•"),
    WAR_INDETERMINATE("Evenly Matched", "à¤¬à¤°à¤¾à¤¬à¤°"),

    // ============================================
    // SHADBALA CALCULATOR STRINGS
    // ============================================
    
    // StrengthRating enum
    STRENGTH_EXTREMELY_WEAK("Extremely Weak", "à¤…à¤¤à¥à¤¯à¤¨à¥à¤¤ à¤•à¤®à¤œà¥‹à¤°"),
    STRENGTH_WEAK("Weak", "à¤•à¤®à¤œà¥‹à¤°"),
    STRENGTH_BELOW_AVERAGE("Below Average", "à¤”à¤¸à¤¤à¤­à¤¨à¥à¤¦à¤¾ à¤•à¤®"),
    STRENGTH_AVERAGE("Average", "à¤”à¤¸à¤¤"),
    STRENGTH_ABOVE_AVERAGE("Above Average", "à¤”à¤¸à¤¤à¤­à¤¨à¥à¤¦à¤¾ à¤®à¤¾à¤¥à¤¿"),
    STRENGTH_STRONG("Strong", "à¤¬à¤²à¤¿à¤¯à¥‹"),
    STRENGTH_VERY_STRONG("Very Strong", "à¤…à¤¤à¥à¤¯à¤¨à¥à¤¤ à¤¬à¤²à¤¿à¤¯à¥‹"),
    STRENGTH_EXTREMELY_STRONG("Extremely Strong", "à¤…à¤¤à¤¿ à¤¶à¤•à¥à¤¤à¤¿à¤¶à¤¾à¤²à¥€"),
    
    // Generic Strength/Status (Added for Batch 3)
    STRENGTH_EXCELLENT("Excellent", "à¤‰à¤¤à¥à¤•à¥ƒà¤·à¥à¤Ÿ"),
    STRENGTH_GOOD("Good", "à¤°à¤¾à¤®à¥à¤°à¥‹"),
    STRENGTH_MODERATE("Moderate", "à¤®à¤§à¥à¤¯à¤®"),
    STRENGTH_CHALLENGING("Challenging", "à¤šà¥à¤¨à¥Œà¤¤à¥€à¤ªà¥‚à¤°à¥à¤£"),
    STRENGTH_DIFFICULT("Difficult", "à¤•à¤ à¤¿à¤¨"),
    BENEFIC("Benefic", "à¤¶à¥à¤­"),
    MALEFIC("Malefic", "à¤ªà¤¾à¤ª"),
    LORDS("Lords", "à¤¸à¥à¤µà¤¾à¤®à¥€à¤¹à¤°à¥‚"),

    // ============================================
    // ARGALA ANALYSIS
    // ============================================
    ARGALA_CALCULATING("Calculating Argala...", "à¤…à¤°à¥à¤—à¤²à¤¾ à¤—à¤£à¤¨à¤¾ à¤—à¤°à¥à¤¦à¥ˆ..."),
    ARGALA_FAILED("Failed to calculate Argala", "à¤…à¤°à¥à¤—à¤²à¤¾ à¤—à¤£à¤¨à¤¾ à¤—à¤°à¥à¤¨ à¤…à¤¸à¤«à¤²"),
    ARGALA_SELECT_HOUSE("Select House to Analyze", "à¤µà¤¿à¤¶à¥à¤²à¥‡à¤·à¤£ à¤—à¤°à¥à¤¨ à¤­à¤¾à¤µ à¤šà¤¯à¤¨ à¤—à¤°à¥à¤¨à¥à¤¹à¥‹à¤¸à¥"),
    ARGALA_HOUSE_SELECTOR_LABEL("Select House to Analyze", "à¤µà¤¿à¤¶à¥à¤²à¥‡à¤·à¤£ à¤—à¤°à¥à¤¨ à¤­à¤¾à¤µ à¤šà¤¯à¤¨ à¤—à¤°à¥à¤¨à¥à¤¹à¥‹à¤¸à¥"),
    ARGALA_TYPES_TITLE("Argala Types", "à¤…à¤°à¥à¤—à¤²à¤¾à¤•à¤¾ à¤ªà¥à¤°à¤•à¤¾à¤°à¤¹à¤°à¥‚"),
    ARGALA_PRIMARY_DESC("4th & 11th houses create primary interventions", "à¤šà¥Œà¤¥à¥‹ à¤° à¤à¤˜à¤¾à¤°à¥Œà¤‚ à¤­à¤¾à¤µà¤²à¥‡ à¤ªà¥à¤°à¤¾à¤¥à¤®à¤¿à¤• à¤¹à¤¸à¥à¤¤à¤•à¥à¤·à¥‡à¤ª à¤¸à¤¿à¤°à¥à¤œà¤¨à¤¾ à¤—à¤°à¥à¤¦à¤›"),
    ARGALA_SECONDARY_DESC("2nd house creates secondary interventions", "à¤¦à¥‹à¤¸à¥à¤°à¥‹ à¤­à¤¾à¤µà¤²à¥‡ à¤®à¤¾à¤§à¥à¤¯à¤®à¤¿à¤• à¤¹à¤¸à¥à¤¤à¤•à¥à¤·à¥‡à¤ª à¤¸à¤¿à¤°à¥à¤œà¤¨à¤¾ à¤—à¤°à¥à¤¦à¤›"),
    ARGALA_FIFTH_HOUSE_DESC("5th house creates special interventions", "à¤ªà¤¾à¤à¤šà¥Œà¤‚ à¤­à¤¾à¤µà¤²à¥‡ à¤µà¤¿à¤¶à¥‡à¤· à¤¹à¤¸à¥à¤¤à¤•à¥à¤·à¥‡à¤ª à¤¸à¤¿à¤°à¥à¤œà¤¨à¤¾ à¤—à¤°à¥à¤¦à¤›"),
    ARGALA_VIRODHA_DESC("12th, 10th, 3rd, 9th houses obstruct Argala", "à¥§à¥¨à¤”à¤‚, à¥§à¥¦à¤”à¤‚, à¥©à¤”à¤‚, à¤° à¥¯à¤”à¤‚ à¤­à¤¾à¤µà¤²à¥‡ à¤…à¤°à¥à¤—à¤¾à¤²à¤¾à¤²à¤¾à¤ˆ à¤¬à¤¾à¤§à¤¾ à¤ªà¥à¤°à¥â€à¤¯à¤¾à¤‰à¤à¤›"),
    ARGALA_CHART_WIDE_PATTERNS("Chart-Wide Argala Patterns", "à¤•à¥à¤£à¥à¤¡à¤²à¥€-à¤µà¥à¤¯à¤¾à¤ªà¥€ à¤…à¤°à¥à¤—à¤¾à¤²à¤¾ à¤¢à¤¾à¤à¤šà¤¾à¤¹à¤°à¥‚"),
    ARGALA_STRONGEST_SUPPORT("Strongest Support", "à¤¸à¤¬à¥ˆà¤­à¤¨à¥à¤¦à¤¾ à¤¬à¤²à¤¿à¤¯à¥‹ à¤¸à¤®à¤°à¥à¤¥à¤¨"),
    ARGALA_GREATEST_CHALLENGE("Greatest Challenge", "à¤¸à¤¬à¥ˆà¤­à¤¨à¥à¤¦à¤¾ à¤ à¥‚à¤²à¥‹ à¤šà¥à¤¨à¥Œà¤¤à¥€"),
    ARGALA_MOST_OBSTRUCTED("Most Obstructed", "à¤¸à¤¬à¥ˆà¤­à¤¨à¥à¤¦à¤¾ à¤¬à¤¾à¤§à¤¿à¤¤"),

    // Chara Dasha Analysis
    CHARA_DASHA_CALC_ERROR("Unknown error calculating Chara Dasha", "à¤•à¤¾à¤°à¤¾ à¤¦à¤¶à¤¾ à¤—à¤£à¤¨à¤¾ à¤—à¤°à¥à¤¦à¤¾ à¤…à¤œà¥à¤žà¤¾à¤¤ à¤¤à¥à¤°à¥à¤Ÿà¤¿"),
    CALCULATING_CHARA_DASHA("Calculating Chara Dasha periods...", "à¤•à¤¾à¤°à¤¾ à¤¦à¤¶à¤¾ à¤…à¤µà¤§à¤¿ à¤—à¤£à¤¨à¤¾ à¤—à¤°à¥à¤¦à¥ˆ..."),
    KARAKA_ACTIVATION("Karaka Activation", "à¤•à¤¾à¤°à¤• à¤¸à¤•à¥à¤°à¤¿à¤¯à¤¤à¤¾"),
    CHARA_DASHA_TYPE("Type", "à¤ªà¥à¤°à¤•à¤¾à¤°"),
    
    // Influence
    INFLUENCE_VERY_FAVORABLE("Very Favorable", "à¤§à¥‡à¤°à¥ˆ à¤…à¤¨à¥à¤•à¥‚à¤²"),
    INFLUENCE_FAVORABLE("Favorable", "à¤…à¤¨à¥à¤•à¥‚à¤²"),
    INFLUENCE_NEUTRAL("Neutral", "à¤¤à¤Ÿà¤¸à¥à¤¥"),
    INFLUENCE_NEEDS_ATTENTION("Needs Attention", "à¤§à¥à¤¯à¤¾à¤¨ à¤¦à¤¿à¤¨à¥à¤ªà¤°à¥à¤¨à¥‡"),

    // Life Areas
    AREA_CAREER("Career", "à¤•à¤°à¤¿à¤¯à¤°"),
    AREA_RELATIONSHIPS("Relationships", "à¤¸à¤®à¥à¤¬à¤¨à¥à¤§"),
    AREA_HEALTH("Health", "à¤¸à¥à¤µà¤¾à¤¸à¥à¤¥à¥à¤¯"),
    AREA_SPIRITUALITY("Spirituality", "à¤†à¤§à¥à¤¯à¤¾à¤¤à¥à¤®à¤¿à¤•à¤¤à¤¾"),
    AREA_WEALTH("Wealth", "à¤§à¤¨"),
    AREA_FAMILY("Family", "à¤ªà¤°à¤¿à¤µà¤¾à¤°"),
    AREA_EDUCATION("Education", "à¤¶à¤¿à¤•à¥à¤·à¤¾"),
    AREA_FOREIGN_CONNECTIONS("Foreign Connections", "à¤µà¤¿à¤¦à¥‡à¤¶à¥€ à¤¸à¤®à¥à¤¬à¤¨à¥à¤§"),

    // Aspects
    ASPECT_CONJUNCTION("Conjunction", "à¤¯à¥à¤¤à¤¿"),
    ASPECT_OPPOSITION("Opposition", "à¤¦à¥ƒà¤·à¥à¤Ÿà¤¿"), 
    ASPECT_TRINE("Trine", "à¤¤à¥à¤°à¤¿à¤•à¥‹à¤£"),
    ASPECT_SQUARE("Square", "à¤•à¥‡à¤¨à¥à¤¦à¥à¤°"),

    // Chara Dasha
    
    // Generic Strength/Status
    
    // Influence

    // Life Areas

    // Aspects
    ARGALA_LEAST_OBSTRUCTED("Least Obstructed", "à¤•à¤® à¤¸à¥‡ à¤•à¤® à¤¬à¤¾à¤§à¤¿à¤¤"),
    ARGALA_KARMA_PATTERNS("Karma Patterns", "à¤•à¤°à¥à¤® à¤¢à¤¾à¤à¤šà¤¾à¤¹à¤°à¥‚"),
    ARGALA_SIGNIFICANT_ARGALAS("Significant Argalas", "à¤®à¤¹à¤¤à¥à¤µà¤ªà¥‚à¤°à¥à¤£ à¤…à¤°à¥à¤—à¤¾à¤²à¤¾à¤¹à¤°à¥‚"),
    
    // Argala Strength
    ARGALA_STRENGTH_VERY_STRONG("Very Strong", "à¤§à¥‡à¤°à¥ˆ à¤¬à¤²à¤¿à¤¯à¥‹"),
    ARGALA_STRENGTH_STRONG("Strong", "à¤¬à¤²à¤¿à¤¯à¥‹"),
    ARGALA_STRENGTH_MODERATE("Moderate", "à¤®à¤§à¥à¤¯à¤®"),
    ARGALA_STRENGTH_WEAK("Weak", "à¤•à¤®à¤œà¥‹à¤°"),
    ARGALA_STRENGTH_OBSTRUCTED("Obstructed", "à¤¬à¤¾à¤§à¤¿à¤¤"),

    // ============================================
    // AVASTHA ANALYSIS
    // ============================================
    AVASTHA_TAB_OVERVIEW("Overview", "à¤…à¤µà¤²à¥‹à¤•à¤¨"),
    AVASTHA_TAB_PLANETS("Planets", "à¤—à¥à¤°à¤¹à¤¹à¤°à¥‚"),
    AVASTHA_TAB_BALADI("Baladi", "à¤¬à¤¾à¤²à¤¾à¤¦à¤¿"),
    AVASTHA_TAB_JAGRADADI("Jagradadi", "à¤œà¤¾à¤—à¥à¤°à¤¦à¤¾à¤¦à¤¿"),
    AVASTHA_TAB_DEEPTADI("Deeptadi", "à¤¦à¥€à¤ªà¥à¤¤à¤¾à¤¦à¤¿"),
    AVASTHA_TAB_LAJJITADI("Lajjitadi", "à¤²à¤œà¥à¤œà¤¿à¤¤à¤¾à¤¦à¤¿"),
    
    AVASTHA_STRONG_CONFIG("Strong overall planetary configuration", "à¤¬à¤²à¤¿à¤¯à¥‹ à¤¸à¤®à¤—à¥à¤° à¤—à¥à¤°à¤¹ à¤µà¤¿à¤¨à¥à¤¯à¤¾à¤¸"),
    AVASTHA_MODERATE_STRENGTH("Moderate overall planetary strength", "à¤®à¤§à¥à¤¯à¤® à¤¸à¤®à¤—à¥à¤° à¤—à¥à¤°à¤¹ à¤¬à¤²"),
    AVASTHA_NEEDS_MEASURES("Planets need strengthening measures", "à¤—à¥à¤°à¤¹à¤¹à¤°à¥‚à¤²à¤¾à¤ˆ à¤¸à¤µà¤²à¥€à¤•à¤°à¤£ à¤‰à¤ªà¤¾à¤¯à¤¹à¤°à¥‚ à¤†à¤µà¤¶à¥à¤¯à¤• à¤›"),
    
    AVASTHA_AGE_STATE("Age State (Baladi)", "à¤…à¤µà¤¸à¥à¤¥à¤¾ (à¤¬à¤¾à¤²à¤¾à¤¦à¤¿)"),
    AVASTHA_ALERTNESS("Alertness (Jagradadi)", "à¤¸à¤¤à¤°à¥à¤•à¤¤à¤¾ (à¤œà¤¾à¤—à¥à¤°à¤¦à¤¾à¤¦à¤¿)"),
    AVASTHA_DIGNITY("Dignity (Deeptadi)", "à¤‡à¤œà¥à¤œà¤¤ (à¤¦à¥€à¤ªà¥à¤¤à¤¾à¤¦à¤¿)"),
    AVASTHA_EMOTIONAL("Emotional (Lajjitadi)", "à¤­à¤¾à¤µà¤¨à¤¾à¤¤à¥à¤®à¤• (à¤²à¤œà¥à¤œà¤¿à¤¤à¤¾à¤¦à¤¿)");
}
