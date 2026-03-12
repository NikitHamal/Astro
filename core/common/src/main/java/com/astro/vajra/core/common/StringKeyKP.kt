package com.astro.vajra.core.common

/**
 * String keys for KP (Krishnamurti Paddhati) System analysis
 */
enum class StringKeyKP(override val en: String, override val ne: String) : StringKeyInterface {

    // Screen Titles
    TITLE("KP System", "केपी प्रणाली"),
    SUBTITLE("Krishnamurti Paddhati Analysis", "कृष्णमूर्ति पद्धति विश्लेषण"),
    HORARY_TITLE("KP Horary", "केपी होरेरी"),
    HORARY_SUBTITLE("Prashna by Number (1–249)", "अंक प्रश्न (१–२४९)"),

    // Tabs
    TAB_CUSPS("Cusps", "कस्प"),
    TAB_PLANETS("Planets", "ग्रह"),
    TAB_SIGNIFICATORS("Significators", "कारकत्व"),
    TAB_RULING_PLANETS("Ruling Planets", "शासक ग्रह"),
    TAB_HORARY("Horary", "होरेरी"),

    // Cusp Analysis
    CUSP_HEADER("House Cusps (Placidus)", "भाव कस्प (प्लेसिडस)"),
    CUSP_SUBTITLE("Sub-Lord analysis of all 12 house cusps", "सबै १२ भाव कस्पको उप-स्वामी विश्लेषण"),
    CUSP_NUMBER("Cusp %s", "कस्प %s"),
    SIGN_LORD("Sign Lord", "राशि स्वामी"),
    STAR_LORD("Star Lord", "नक्षत्र स्वामी"),
    SUB_LORD("Sub Lord", "उप स्वामी"),
    SUB_SUB_LORD("Sub-Sub Lord", "उप-उप स्वामी"),
    CUSP_DEGREE("Degree", "अंश"),

    // Planet Analysis
    PLANET_HEADER("Planet Positions (KP)", "ग्रह स्थिति (केपी)"),
    PLANET_SUBTITLE("Star & Sub-Lord of each planet", "प्रत्येक ग्रहको नक्षत्र र उप-स्वामी"),
    PLANET_SIGN("Sign", "राशि"),
    PLANET_NAKSHATRA("Nakshatra", "नक्षत्र"),
    PLANET_IN_STAR_OF("In Star of", "को नक्षत्रमा"),
    PLANET_SUB("Sub", "उप"),
    PLANET_SUB_SUB("Sub-Sub", "उप-उप"),

    // Significators
    SIGNIFICATOR_HEADER("KP Significators", "केपी कारकत्व"),
    SIGNIFICATOR_SUBTITLE("House signification through 4-step method", "चार-चरण विधिद्वारा भाव कारकत्व"),
    HOUSE_SIGNIFICATORS("House %s Significators", "भाव %s कारकहरू"),
    SIGNIFICATOR_LEVEL_1("Step 1: Planets in star of occupants", "चरण १: भाव स्थित ग्रहको नक्षत्रमा भएका ग्रह"),
    SIGNIFICATOR_LEVEL_2("Step 2: Occupants of house", "चरण २: भावमा स्थित ग्रह"),
    SIGNIFICATOR_LEVEL_3("Step 3: Planets in star of owner", "चरण ३: भाव स्वामीको नक्षत्रमा भएका ग्रह"),
    SIGNIFICATOR_LEVEL_4("Step 4: Owner of house", "चरण ४: भावको स्वामी"),
    CONJOINED_ASPECTED("Conjoined/Aspected", "युति/दृष्ट"),
    STRONGEST_SIGNIFICATORS("Strongest Significators", "सबैभन्दा बलवान कारक"),
    NO_PLANETS("None", "कुनै पनि छैन"),

    // Ruling Planets
    RULING_HEADER("Ruling Planets", "शासक ग्रहहरू"),
    RULING_SUBTITLE("Current moment ruling planets for confirmation", "पुष्टिका लागि हालको शासक ग्रहहरू"),
    DAY_LORD("Day Lord", "दिन स्वामी"),
    MOON_SIGN_LORD("Moon Sign Lord", "चन्द्र राशि स्वामी"),
    MOON_STAR_LORD("Moon Star Lord", "चन्द्र नक्षत्र स्वामी"),
    MOON_SUB_LORD("Moon Sub Lord", "चन्द्र उप स्वामी"),
    ASC_SIGN_LORD("Ascendant Sign Lord", "लग्न राशि स्वामी"),
    ASC_STAR_LORD("Ascendant Star Lord", "लग्न नक्षत्र स्वामी"),
    ASC_SUB_LORD("Ascendant Sub Lord", "लग्न उप स्वामी"),
    RULING_PLANET_LABEL("Ruling Planet", "शासक ग्रह"),
    RULING_COMMON("Common Ruling Planets", "साझा शासक ग्रह"),
    RULING_INFO_TITLE("About Ruling Planets", "शासक ग्रह बारेमा"),
    RULING_INFO_CONTENT("Ruling planets at the time of judgment confirm or deny event signification. Planets appearing as both significators and ruling planets strongly indicate event manifestation.", "निर्णयको समयमा शासक ग्रहले घटनाको कारकत्वलाई पुष्टि वा अस्वीकार गर्छ। कारक र शासक दुवैमा देखिने ग्रहले घटना प्रकट हुने बलियो संकेत दिन्छ।"),

    // Horary
    HORARY_NUMBER_LABEL("Enter KP Number (1–249)", "केपी अंक प्रविष्ट गर्नुहोस् (१–२४९)"),
    HORARY_ANALYZE("Analyze", "विश्लेषण गर्नुहोस्"),
    HORARY_RESULT_HEADER("Horary Analysis for Number %s", "अंक %s को होरेरी विश्लेषण"),
    HORARY_ASC_DEGREE("Ascendant Degree", "लग्न अंश"),
    HORARY_INFO_TITLE("KP Horary System", "केपी होरेरी प्रणाली"),
    HORARY_INFO_CONTENT("In KP Horary, the querent thinks of a number between 1 and 249. This number maps to a specific zodiac sub-division, which becomes the ascendant of the horary chart. The sub-lord of the ascendant cusp determines whether the query will fructify.", "केपी होरेरीमा प्रश्नकर्ताले १ देखि २४९ बीचको एउटा अंक सोच्छ। यो अंक राशिचक्रको विशेष उप-विभाजनमा म्याप हुन्छ, जुन होरेरी कुण्डलीको लग्न बन्छ। लग्न कस्पको उप-स्वामीले प्रश्नको फलिबद्धता निर्धारण गर्छ।"),
    HORARY_INVALID_NUMBER("Please enter a number between 1 and 249", "कृपया १ र २४९ बीचको अंक प्रविष्ट गर्नुहोस्"),

    // Common
    LOADING_KP("Calculating KP Analysis...", "केपी विश्लेषण गणना गर्दै..."),
    ERROR_KP("Error calculating KP analysis", "केपी विश्लेषण गणनामा त्रुटि"),
    NO_CHART_AVAILABLE("No chart available", "कुनै कुण्डली उपलब्ध छैन"),
    HOUSE("House", "भाव"),
    DEGREE_FORMAT("%s° %s' %s\"", "%s° %s' %s\""),
    RETROGRADE_LABEL("(R)", "(व)"),
    KP_AYANAMSA("KP Ayanamsa", "केपी अयनांश"),
    PLACIDUS_SYSTEM("Placidus House System", "प्लेसिडस भाव प्रणाली"),

    // Feature Card
    FEATURE_KP("KP System", "केपी प्रणाली"),
    FEATURE_KP_DESC("Krishnamurti Paddhati sub-lord analysis", "कृष्णमूर्ति पद्धति उप-स्वामी विश्लेषण"),
    FEATURE_KP_HORARY("KP Horary", "केपी होरेरी"),
    FEATURE_KP_HORARY_DESC("Horary analysis using number 1–249", "अंक १–२४९ प्रयोग गरी होरेरी विश्लेषण"),
}
