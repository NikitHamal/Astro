package com.astro.storm.core.common

/**
 * String keys for Shadbala components and related Vedic calculations
 * Including: Sarvatobhadra Chakra, Drig Bala, Sthana Bala, Kala Bala, Saham
 */
enum class StringKeyShadbala(override val en: String, override val ne: String, override val hi: String) : StringKeyInterface {

    // ============================================
    // SARVATOBHADRA CHAKRA STRINGS
    // ============================================
    SARVATOBHADRA_TITLE("Sarvatobhadra Chakra", "सर्वतोभद्र चक्र", "सर्वतोभद्र चक्र"),
    ,
    SARVATOBHADRA_SUBTITLE("9×9 Transit & Muhurta Chakra", "९×९ गोचर र मुहूर्त चक्र", "९×९ गोचर और मुहूर्त चक्र"),
    ,
    SARVATOBHADRA_DESC("Traditional Vedha chakra for transit analysis, muhurta selection, and name compatibility", "गोचर विश्लेषण, मुहूर्त चयन, र नाम अनुकूलताको लागि परम्परागत वेध चक्र", "गोचर विश्लेषण, मुहूर्त चयन, और नाम अनुकूलता के लिए परम्परागत वेध चक्र"),

    ,

    // Tabs
    SARVATOBHADRA_TAB_OVERVIEW("Overview", "सिंहावलोकन", "अवलोकन"),
    ,
    SARVATOBHADRA_TAB_CHAKRA("Chakra", "चक्र", "चक्र"),
    ,
    SARVATOBHADRA_TAB_DAILY("Daily", "दैनिक", "दैनिक"),
    ,
    SARVATOBHADRA_TAB_VEDHA("Vedhas", "वेधहरू", "वेध"),
    ,
    SARVATOBHADRA_TAB_NAME("Name", "नाम", "नाम"),

    ,

    // Cell Types
    SARVATOBHADRA_NAKSHATRA("Nakshatra", "नक्षत्र", "नक्षत्र"),
    ,
    SARVATOBHADRA_VOWEL("Vowel", "स्वर", "स्वर"),
    ,
    SARVATOBHADRA_WEEKDAY("Weekday", "वार", "वार"),
    ,
    SARVATOBHADRA_TITHI("Tithi", "तिथि", "तिथि"),
    ,
    SARVATOBHADRA_CENTER("Center", "केन्द्र", "केन्द्र"),

    ,

    // Vedha Types
    SARVATOBHADRA_FULL_VEDHA("Full Vedha", "पूर्ण वेध", "पूर्ण वेध"),
    ,
    SARVATOBHADRA_THREE_QUARTER_VEDHA("3/4 Vedha", "३/४ वेध", "३/४ वेध"),
    ,
    SARVATOBHADRA_HALF_VEDHA("Half Vedha", "आधा वेध", "आधा वेध"),
    ,
    SARVATOBHADRA_QUARTER_VEDHA("Quarter Vedha", "चौथाई वेध", "चौथाई वेध"),
    ,
    SARVATOBHADRA_NO_VEDHA("No Vedha", "वेध छैन", "वेध हैैन"),

    ,

    // Vedha Effects
    SARVATOBHADRA_BENEFIC("Benefic", "शुभ", "शुभ"),
    ,
    SARVATOBHADRA_MALEFIC("Malefic", "अशुभ", "अशुभ"),
    ,
    SARVATOBHADRA_MIXED("Mixed", "मिश्रित", "मिश्रित"),
    ,
    SARVATOBHADRA_NEUTRAL("Neutral", "तटस्थ", "तटस्थ"),

    ,

    // Analysis
    SARVATOBHADRA_BIRTH_NAKSHATRA("Birth Nakshatra", "जन्म नक्षत्र", "जन्म नक्षत्र"),
    ,
    SARVATOBHADRA_CURRENT_TRANSITS("Current Transit Vedhas", "वर्तमान गोचर वेधहरू", "वर्तमान गोचर वेध"),
    ,
    SARVATOBHADRA_TRANSIT_SCORE("Transit Score", "गोचर स्कोर", "गोचर स्कोर"),
    ,
    SARVATOBHADRA_FAVORABLE_DAYS("Favorable Days", "अनुकूल दिनहरू", "अनुकूल दिन"),
    ,
    SARVATOBHADRA_UNFAVORABLE_DAYS("Unfavorable Days", "प्रतिकूल दिनहरू", "प्रतिकूल दिन"),
    ,
    SARVATOBHADRA_OVERALL_SCORE("Overall Score", "समग्र स्कोर", "समग्र स्कोर"),

    ,

    // Daily Analysis
    SARVATOBHADRA_TODAY("Today's Analysis", "आजको विश्लेषण", "आज के विश्लेषण"),
    ,
    SARVATOBHADRA_MOON_NAKSHATRA("Moon Nakshatra", "चन्द्र नक्षत्र", "चन्द्र नक्षत्र"),
    ,
    SARVATOBHADRA_CURRENT_TITHI("Current Tithi", "वर्तमान तिथि", "वर्तमान तिथि"),
    ,
    SARVATOBHADRA_DAY_QUALITY("Day Quality", "दिनको गुणस्तर", "दिन के गुणस्तर"),
    ,
    SARVATOBHADRA_FAVORABLE_ACTIVITIES("Favorable Activities", "अनुकूल कार्यहरू", "अनुकूल कार्य"),
    ,
    SARVATOBHADRA_UNFAVORABLE_ACTIVITIES("Avoid Today", "आज नगर्नुहोस्", "आज नकरें"),

    ,

    // Name Analysis
    SARVATOBHADRA_NAME_ANALYSIS("Name Analysis", "नाम विश्लेषण", "नाम विश्लेषण"),
    ,
    SARVATOBHADRA_FIRST_LETTER("First Letter", "पहिलो अक्षर", "पहिलो अक्षर"),
    ,
    SARVATOBHADRA_ASSOCIATED_VOWEL("Associated Vowel", "सम्बन्धित स्वर", "संबंधित स्वर"),
    ,
    SARVATOBHADRA_PLANETARY_INFLUENCE("Planetary Influence", "ग्रह प्रभाव", "ग्रह प्रभाव"),
    ,
    SARVATOBHADRA_FAVORABLE_NAKSHATRAS("Favorable Nakshatras", "अनुकूल नक्षत्रहरू", "अनुकूल नक्षत्र"),
    ,
    SARVATOBHADRA_UNFAVORABLE_NAKSHATRAS("Unfavorable Nakshatras", "प्रतिकूल नक्षत्रहरू", "प्रतिकूल नक्षत्र"),

    ,

    // Quality Levels
    SARVATOBHADRA_HIGHLY_FAVORABLE("Highly Favorable", "अत्यन्त अनुकूल", "अत्यन्त अनुकूल"),
    ,
    SARVATOBHADRA_MODERATELY_FAVORABLE("Moderately Favorable", "मध्यम अनुकूल", "मध्यम अनुकूल"),
    ,
    SARVATOBHADRA_CHALLENGING("Challenging", "चुनौतीपूर्ण", "चुनौतीपूर्ण"),

    ,

    // Info
    SARVATOBHADRA_INFO_TITLE("About Sarvatobhadra Chakra", "सर्वतोभद्र चक्रको बारेमा", "सर्वतोभद्र चक्र के बारे में"),
    ,
    SARVATOBHADRA_INFO_DESC("Sarvatobhadra Chakra is a 9×9 grid showing relationships between nakshatras, vowels, weekdays, and tithis. It's used for transit analysis, muhurta selection, and name compatibility through vedha (obstruction) calculations.", "सर्वतोभद्र चक्र नक्षत्र, स्वर, वार, र तिथिहरू बीचको सम्बन्ध देखाउने ९×९ ग्रिड हो। यो वेध (अवरोध) गणनाहरू मार्फत गोचर विश्लेषण, मुहूर्त चयन, र नाम अनुकूलताको लागि प्रयोग गरिन्छ।", "सर्वतोभद्र चक्र नक्षत्र, स्वर, वार, और तिथि बीच के संबंध देखाउने ९×९ ग्रिड है। यह वेध (अवरोध) गणना मार्फत गोचर विश्लेषण, मुहूर्त चयन, और नाम अनुकूलता के लिए प्रयहग गरिन्है।"),
    ,
    SARVATOBHADRA_VEDIC_REF("Vedic Reference: Muhurta Chintamani, Muhurta Martanda", "वैदिक सन्दर्भ: मुहूर्त चिन्तामणि, मुहूर्त मार्तण्ड", "वैदिक सन्दर्भ: मुहूर्त चिन्तामणि, मुहूर्त मार्तण्ड"),
    ,
    SARVATOBHADRA_ANALYZING("Analyzing Sarvatobhadra Chakra...", "सर्वतोभद्र चक्र विश्लेषण गर्दै...", "सर्वतोभद्र चक्र विश्लेषण गर्दै..."),
    ,
    SARVATOBHADRA_UNABLE("Unable to calculate Sarvatobhadra", "सर्वतोभद्र गणना गर्न सकिएन", "सर्वतोभद्र गणना करने सकिएन"),

    ,

    // ============================================
    // DRIG BALA STRINGS
    // ============================================
    DRIG_TITLE("Drig Bala", "दृग्बल", "दृग्बल"),
    ,
    DRIG_SUBTITLE("Aspectual Strength Analysis", "दृष्टि बल विश्लेषण", "दृष्टि बल विश्लेषण"),
    ,
    DRIG_DESC("Shadbala component measuring strength from planetary aspects", "ग्रह दृष्टिबाट बल मापन गर्ने षड्बल घटक", "ग्रह दृष्टिबाट बल मापन करनेे षड्बल घटक"),

    ,

    // Tabs
    DRIG_TAB_OVERVIEW("Overview", "सिंहावलोकन", "अवलोकन"),
    ,
    DRIG_TAB_ASPECTS("Aspects", "दृष्टिहरू", "दृष्टि"),
    ,
    DRIG_TAB_PLANETS("Planets", "ग्रहहरू", "ग्रह"),
    ,
    DRIG_TAB_HOUSES("Houses", "भावहरू", "भाव"),

    ,

    // Aspect Types
    DRIG_FULL_ASPECT("Full Aspect (100%)", "पूर्ण दृष्टि (१००%)", "पूर्ण दृष्टि (१००%)"),
    ,
    DRIG_THREE_QUARTER("3/4 Aspect (75%)", "३/४ दृष्टि (७५%)", "३/४ दृष्टि (७५%)"),
    ,
    DRIG_HALF_ASPECT("Half Aspect (50%)", "आधा दृष्टि (५०%)", "आधा दृष्टि (५०%)"),
    ,
    DRIG_QUARTER_ASPECT("Quarter Aspect (25%)", "चौथाई दृष्टि (२५%)", "चौथाई दृष्टि (२५%)"),

    ,

    // Analysis
    DRIG_TOTAL_SCORE("Total Drig Bala", "कुल दृग्बल", "कुल दृग्बल"),
    ,
    DRIG_BENEFIC_ASPECTS("Benefic Aspects", "शुभ दृष्टिहरू", "शुभ दृष्टि"),
    ,
    DRIG_MALEFIC_ASPECTS("Malefic Aspects", "अशुभ दृष्टिहरू", "अशुभ दृष्टि"),
    ,
    DRIG_NET_STRENGTH("Net Aspectual Strength", "शुद्ध दृष्टि बल", "शुद्ध दृष्टि बल"),
    ,
    DRIG_ASPECT_FROM("Aspect From", "बाट दृष्टि", "बाट दृष्टि"),
    ,
    DRIG_ASPECT_TO("Aspect To", "मा दृष्टि", "में दृष्टि"),
    ,
    DRIG_ASPECT_STRENGTH("Aspect Strength", "दृष्टि बल", "दृष्टि बल"),
    ,
    DRIG_SPECIAL_ASPECT("Special Aspect", "विशेष दृष्टि", "विशेष दृष्टि"),

    ,

    // Planet Drig
    DRIG_PLANET_RECEIVING("Aspects Received", "प्राप्त दृष्टि", "प्राप्त दृष्टि"),
    ,
    DRIG_PLANET_CASTING("Aspects Cast", "दिइएको दृष्टि", "दिइए के दृष्टि"),
    ,
    DRIG_VIRUPAS("Virupas", "विरूपा", "विरूपा"),
    ,
    DRIG_POSITIVE("Positive", "सकारात्मक", "सकारात्मक"),
    ,
    DRIG_NEGATIVE("Negative", "नकारात्मक", "नकारात्मक"),

    ,

    // Info
    DRIG_INFO_TITLE("About Drig Bala", "दृग्बलको बारेमा", "दृग्बल के बारे में"),
    ,
    DRIG_INFO_DESC("Drig Bala measures aspectual strength - the positive or negative influence from planetary aspects. Benefic aspects add strength while malefic aspects reduce it. Net Drig Bala determines overall aspectual support.", "दृग्बलले दृष्टि बल मापन गर्छ - ग्रह दृष्टिबाट सकारात्मक वा नकारात्मक प्रभाव। शुभ दृष्टिले बल थप्छ जबकि अशुभ दृष्टिले घटाउँछ। शुद्ध दृग्बलले समग्र दृष्टि समर्थन निर्धारण गर्छ।", "दृग्बलले दृष्टि बल मापन करता है - ग्रह दृष्टिबाट सकारात्मक या नकारात्मक प्रभाव। शुभ दृष्टिले बल थप्है जबकि अशुभ दृष्टिले घटाउँहै। शुद्ध दृग्बलले समग्र दृष्टि समर्थन निर्धारण करता है।"),
    ,
    DRIG_VEDIC_REF("Vedic Reference: BPHS Shadbala chapter, Graha Sutras", "वैदिक सन्दर्भ: बृहत् पाराशर होरा शास्त्र षड्बल अध्याय, ग्रह सूत्र", "वैदिक सन्दर्भ: बृहत् पाराशर हैरा शास्त्र षड्बल अध्याय, ग्रह सूत्र"),
    ,
    DRIG_ANALYZING("Analyzing Drig Bala...", "दृग्बल विश्लेषण गर्दै...", "दृग्बल विश्लेषण गर्दै..."),
    ,
    DRIG_UNABLE("Unable to calculate Drig Bala", "दृग्बल गणना गर्न सकिएन", "दृग्बल गणना करने सकिएन"),

    ,

    // ============================================
    // STHANA BALA STRINGS
    // ============================================
    STHANA_TITLE("Sthana Bala", "स्थानबल", "स्थानबल"),
    ,
    STHANA_SUBTITLE("Positional Strength Analysis", "स्थानगत बल विश्लेषण", "स्थानगत बल विश्लेषण"),
    ,
    STHANA_DESC("Shadbala component measuring strength from sign, house, and dignity placement", "राशि, भाव, र गरिमा स्थानबाट बल मापन गर्ने षड्बल घटक", "राशि, भाव, और गरि में स्थानबाट बल मापन करनेे षड्बल घटक"),

    ,

    // Tabs
    STHANA_TAB_OVERVIEW("Overview", "सिंहावलोकन", "अवलोकन"),
    ,
    STHANA_TAB_COMPONENTS("Components", "घटकहरू", "घटक"),
    ,
    STHANA_TAB_PLANETS("Planets", "ग्रहहरू", "ग्रह"),

    ,

    // Components
    STHANA_UCCHA_BALA("Uccha Bala", "उच्च बल", "उच्च बल"),
    ,
    STHANA_UCCHA_DESC("Exaltation strength (0-60 virupas)", "उच्च बल (०-६० विरूपा)", "उच्च बल (०-६० विरूपा)"),
    ,
    STHANA_SAPTAVARGAJA("Saptavargaja Bala", "सप्तवर्गज बल", "सप्तवर्गज बल"),
    ,
    STHANA_SAPTAVARGAJA_DESC("Strength from 7 divisional charts", "७ विभागीय चार्टबाट बल", "७ विभागीय चार्टबाट बल"),
    ,
    STHANA_OJHAYUGMA("Ojhayugma Bala", "ओज्हयुग्म बल", "ओज्हयुग्म बल"),
    ,
    STHANA_OJHAYUGMA_DESC("Odd/Even sign placement", "ओज/युग्म राशि स्थान", "ओज/युग्म राशि स्थान"),
    ,
    STHANA_KENDRADI("Kendradi Bala", "केन्द्रादि बल", "केन्द्रादि बल"),
    ,
    STHANA_KENDRADI_DESC("Angular house strength", "केन्द्र भाव बल", "केन्द्र भाव बल"),
    ,
    STHANA_DREKKANA("Drekkana Bala", "द्रेक्काण बल", "द्रेक्काण बल"),
    ,
    STHANA_DREKKANA_DESC("Decanate position strength", "द्रेक्काण स्थान बल", "द्रेक्काण स्थान बल"),

    ,

    // Values
    STHANA_TOTAL("Total Sthana Bala", "कुल स्थानबल", "कुल स्थानबल"),
    ,
    STHANA_REQUIRED("Required", "आवश्यक", "आवश्यक"),
    ,
    STHANA_ACTUAL("Actual", "वास्तविक", "वास्तविक"),
    ,
    STHANA_RATIO("Ratio", "अनुपात", "अनुपात"),
    ,
    STHANA_STRONG("Strong", "बलियो", "बलियह"),
    ,
    STHANA_WEAK("Weak", "कमजोर", "कमजोर"),
    ,
    STHANA_AVERAGE("Average", "औसत", "औसत"),

    ,

    // Info
    STHANA_INFO_TITLE("About Sthana Bala", "स्थानबलको बारेमा", "स्थानबल के बारे में"),
    ,
    STHANA_INFO_DESC("Sthana Bala measures positional strength through 5 components: Uccha (exaltation), Saptavargaja (7 vargas), Ojhayugma (odd/even), Kendradi (angular), and Drekkana (decanate). It forms a major portion of Shadbala.", "स्थानबलले ५ घटकहरू मार्फत स्थानगत बल मापन गर्छ: उच्च, सप्तवर्गज, ओज्हयुग्म, केन्द्रादि, र द्रेक्काण। यसले षड्बलको प्रमुख भाग बनाउँछ।", "स्थानबलले ५ घटक मार्फत स्थानगत बल मापन करता है: उच्च, सप्तवर्गज, ओज्हयुग्म, केन्द्रादि, और द्रेक्काण। यह षड्बल के प्रमुख भाग बनाउँहै।"),
    ,
    STHANA_VEDIC_REF("Vedic Reference: BPHS Chapter 27, Graha Sutras", "वैदिक सन्दर्भ: बृहत् पाराशर होरा शास्त्र अध्याय २७, ग्रह सूत्र", "वैदिक सन्दर्भ: बृहत् पाराशर हैरा शास्त्र अध्याय २७, ग्रह सूत्र"),
    ,
    STHANA_ANALYZING("Analyzing Sthana Bala...", "स्थानबल विश्लेषण गर्दै...", "स्थानबल विश्लेषण गर्दै..."),
    ,
    STHANA_UNABLE("Unable to calculate Sthana Bala", "स्थानबल गणना गर्न सकिएन", "स्थानबल गणना करने सकिएन"),

    ,

    // ============================================
    // KALA BALA STRINGS
    // ============================================
    KALA_TITLE("Kala Bala", "कालबल", "कालबल"),
    ,
    KALA_SUBTITLE("Temporal Strength Analysis", "समयगत बल विश्लेषण", "समयगत बल विश्लेषण"),
    ,
    KALA_DESC("Shadbala component measuring strength from time factors", "समय कारकहरूबाट बल मापन गर्ने षड्बल घटक", "समय कारकबाट बल मापन करनेे षड्बल घटक"),

    ,

    // Tabs
    KALA_TAB_OVERVIEW("Overview", "सिंहावलोकन", "अवलोकन"),
    ,
    KALA_TAB_COMPONENTS("Components", "घटकहरू", "घटक"),
    ,
    KALA_TAB_PLANETS("Planets", "ग्रहहरू", "ग्रह"),

    ,

    // Components
    KALA_NATHONNATHA("Nathonnatha Bala", "नाथोन्नथ बल", "नाथोन्नथ बल"),
    ,
    KALA_NATHONNATHA_DESC("Day/Night strength", "दिन/रात बल", "दिन/रात बल"),
    ,
    KALA_PAKSHA("Paksha Bala", "पक्ष बल", "पक्ष बल"),
    ,
    KALA_PAKSHA_DESC("Lunar fortnight strength", "चन्द्र पाक्षिक बल", "चन्द्र पाक्षिक बल"),
    ,
    KALA_TRIBHAGA("Tribhaga Bala", "त्रिभाग बल", "त्रिभाग बल"),
    ,
    KALA_TRIBHAGA_DESC("Day/Night third strength", "दिन/रात तृतीयांश बल", "दिन/रात तृतीयांश बल"),
    ,
    KALA_VARSHA("Varsha Bala", "वर्ष बल", "वर्ष बल"),
    ,
    KALA_VARSHA_DESC("Year lord strength", "वर्ष स्वामी बल", "वर्ष स्वामी बल"),
    ,
    KALA_MASA("Masa Bala", "मास बल", "मास बल"),
    ,
    KALA_MASA_DESC("Month lord strength", "महिना स्वामी बल", "महिना स्वामी बल"),
    ,
    KALA_DINA("Dina Bala", "दिन बल", "दिन बल"),
    ,
    KALA_DINA_DESC("Day lord strength", "दिन स्वामी बल", "दिन स्वामी बल"),
    ,
    KALA_HORA("Hora Bala", "होरा बल", "होरा बल"),
    ,
    KALA_HORA_DESC("Hour lord strength", "होरा स्वामी बल", "होरा स्वामी बल"),
    ,
    KALA_AYANA("Ayana Bala", "अयन बल", "अयन बल"),
    ,
    KALA_AYANA_DESC("Solstice/Declination strength", "अयन/क्रान्ति बल", "अयन/क्रान्ति बल"),
    ,
    KALA_YUDDHA("Yuddha Bala", "युद्ध बल", "युद्ध बल"),
    ,
    KALA_YUDDHA_DESC("Planetary war strength", "ग्रह युद्ध बल", "ग्रह युद्ध बल"),

    ,

    // Values
    KALA_TOTAL("Total Kala Bala", "कुल कालबल", "कुल कालबल"),
    ,
    KALA_DAY_BIRTH("Day Birth", "दिन जन्म", "दिन जन्म"),
    ,
    KALA_NIGHT_BIRTH("Night Birth", "रात जन्म", "रात जन्म"),
    ,
    KALA_SHUKLA_PAKSHA("Shukla Paksha", "शुक्ल पक्ष", "शुक्ल पक्ष"),
    ,
    KALA_KRISHNA_PAKSHA("Krishna Paksha", "कृष्ण पक्ष", "कृष्ण पक्ष"),
    ,
    KALA_HORA_LABEL("Hora", "होरा", "होरा"),
    ,
    KALA_DAY_LABEL("Day", "वार", "वार"),
    ,
    KALA_TITHI_LABEL("Tithi", "तिथि", "तिथि"),

    ,

    // Info
    KALA_INFO_TITLE("About Kala Bala", "कालबलको बारेमा", "कालबल के बारे में"),
    ,
    KALA_INFO_DESC("Kala Bala measures temporal strength through 9 components related to time of birth, lunar phase, day/night, seasons, and planetary wars. It reflects how time supports each planet's strength.", "कालबलले जन्म समय, चन्द्र चरण, दिन/रात, ऋतु, र ग्रह युद्धसँग सम्बन्धित ९ घटकहरू मार्फत समयगत बल मापन गर्छ। यसले प्रत्येक ग्रहको बललाई समयले कसरी समर्थन गर्छ भनी प्रतिबिम्बित गर्छ।", "कालबलले जन्म समय, चन्द्र चरण, दिन/रात, ऋतु, और ग्रह युद्धसँग संबंधित ९ घटक मार्फत समयगत बल मापन करता है। यह प्रत्येक ग्रह के बल  के समयले कसरी समर्थन करता है भनी प्रतिबिम्बित करता है।"),
    ,
    KALA_VEDIC_REF("Vedic Reference: BPHS Chapter 28, Surya Siddhanta", "वैदिक सन्दर्भ: बृहत् पाराशर होरा शास्त्र अध्याय २८, सूर्य सिद्धान्त", "वैदिक सन्दर्भ: बृहत् पाराशर हैरा शास्त्र अध्याय २८, सूर्य सिद्धान्त"),
    ,
    KALA_ANALYZING("Analyzing Kala Bala...", "कालबल विश्लेषण गर्दै...", "कालबल विश्लेषण गर्दै..."),
    ,
    KALA_UNABLE("Unable to calculate Kala Bala", "कालबल गणना गर्न सकिएन", "कालबल गणना करने सकिएन"),

    ,

    // ============================================
    // SAHAM (ARABIC PARTS) STRINGS
    // ============================================
    SAHAM_TITLE("Saham", "सहम", "सहम"),
    ,
    SAHAM_SUBTITLE("Arabic Parts / Lots", "अरबी बिन्दुहरू", "अरबी बिन्दु"),
    ,
    SAHAM_DESC("Sensitive points calculated from three chart factors", "तीन चार्ट कारकहरूबाट गणना गरिएका संवेदनशील बिन्दुहरू", "तीन चार्ट कारकबाट गणना गरिए के संवेदनशील बिन्दु"),

    ,

    // Tabs
    SAHAM_TAB_OVERVIEW("Overview", "सिंहावलोकन", "अवलोकन"),
    ,
    SAHAM_TAB_MAJOR("Major", "प्रमुख", "प्रमुख"),
    ,
    SAHAM_TAB_MINOR("Minor", "सामान्य", "सामान्य"),
    ,
    SAHAM_TAB_PRASHNA("Prashna", "प्रश्न", "प्रश्न"),

    ,

    // Major Sahams
    SAHAM_PUNYA("Punya Saham", "पुण्य सहम", "पुण्य सहम"),
    ,
    SAHAM_PUNYA_DESC("Fortune/Merit point", "भाग्य/पुण्य बिन्दु", "भाग्य/पुण्य बिन्दु"),
    ,
    SAHAM_VIDYA("Vidya Saham", "विद्या सहम", "विद्या सहम"),
    ,
    SAHAM_VIDYA_DESC("Education/Knowledge", "शिक्षा/ज्ञान", "शिक्षा/ज्ञान"),
    ,
    SAHAM_YASHAS("Yashas Saham", "यश सहम", "यश सहम"),
    ,
    SAHAM_YASHAS_DESC("Fame/Reputation", "यश/प्रतिष्ठा", "यश/प्रतिष्ठा"),
    ,
    SAHAM_MITRA("Mitra Saham", "मित्र सहम", "मित्र सहम"),
    ,
    SAHAM_MITRA_DESC("Friends/Allies", "मित्र/साथी", "मित्र/मित्र"),
    ,
    SAHAM_MAHATMYA("Mahatmya Saham", "महात्म्य सहम", "महात्म्य सहम"),
    ,
    SAHAM_MAHATMYA_DESC("Greatness/Spirituality", "महानता/आध्यात्मिकता", "महानता/आध्यात्मिकता"),
    ,
    SAHAM_ASHA("Asha Saham", "आशा सहम", "आशा सहम"),
    ,
    SAHAM_ASHA_DESC("Hopes/Wishes", "आशा/इच्छा", "आशा/इच्हैा"),
    ,
    SAHAM_SAMARTHA("Samartha Saham", "समर्थ सहम", "समर्थ सहम"),
    ,
    SAHAM_SAMARTHA_DESC("Capability/Skill", "क्षमता/सीप", "क्षमता/सीप"),
    ,
    SAHAM_BHRATRI("Bhratri Saham", "भ्रातृ सहम", "भ्रातृ सहम"),
    ,
    SAHAM_BHRATRI_DESC("Siblings", "भाइबहिनी", "भाई-बहन"),
    ,
    SAHAM_GAURAVA("Gaurava Saham", "गौरव सहम", "गौरव सहम"),
    ,
    SAHAM_GAURAVA_DESC("Honor/Respect", "सम्मान/इज्जत", "सम्मान/इज्जत"),
    ,
    SAHAM_PITRI("Pitri Saham", "पितृ सहम", "पितृ सहम"),
    ,
    SAHAM_PITRI_DESC("Father", "पिता", "पिता"),
    ,
    SAHAM_MATRI("Matri Saham", "मातृ सहम", "मातृ सहम"),
    ,
    SAHAM_MATRI_DESC("Mother", "आमा", "आ में"),
    ,
    SAHAM_PUTRA("Putra Saham", "पुत्र सहम", "पुत्र सहम"),
    ,
    SAHAM_PUTRA_DESC("Children", "सन्तान", "संतान"),
    ,
    SAHAM_AROGYA("Arogya Saham", "आरोग्य सहम", "आरोग्य सहम"),
    ,
    SAHAM_AROGYA_DESC("Health", "स्वास्थ्य", "स्वास्थ्य"),
    ,
    SAHAM_VIVAHA("Vivaha Saham", "विवाह सहम", "विवाह सहम"),
    ,
    SAHAM_VIVAHA_DESC("Marriage", "विवाह", "विवाह"),
    ,
    SAHAM_MRITYU("Mrityu Saham", "मृत्यु सहम", "मृत्यु सहम"),
    ,
    SAHAM_MRITYU_DESC("Death/Longevity", "मृत्यु/आयु", "मृत्यु/आयु"),
    ,
    SAHAM_KARMA("Karma Saham", "कर्म सहम", "कर्म सहम"),
    ,
    SAHAM_KARMA_DESC("Career/Action", "क्यारियर/कर्म", "क्यारियर/कर्म"),
    ,
    SAHAM_DHANA("Dhana Saham", "धन सहम", "धन सहम"),
    ,
    SAHAM_DHANA_DESC("Wealth", "धन", "धन"),
    ,
    SAHAM_PARADESA("Paradesa Saham", "परदेश सहम", "परदेश सहम"),
    ,
    SAHAM_PARADESA_DESC("Foreign/Travel", "विदेश/यात्रा", "विदेश/यात्रा"),
    ,
    SAHAM_BANDHU("Bandhu Saham", "बन्धु सहम", "बन्धु सहम"),
    ,
    SAHAM_BANDHU_DESC("Relatives", "नातेदार", "नातेदार"),
    ,
    SAHAM_SARPA("Sarpa Saham", "सर्प सहम", "सर्प सहम"),
    ,
    SAHAM_SARPA_DESC("Serpent/Hidden enemies", "सर्प/लुकेका शत्रु", "सर्प/लुके के शत्रु"),

    ,

    // Analysis
    SAHAM_POSITION("Position", "स्थान", "स्थान"),
    ,
    SAHAM_SIGN("Sign", "राशि", "राशि"),
    ,
    SAHAM_NAKSHATRA("Nakshatra", "नक्षत्र", "नक्षत्र"),
    ,
    SAHAM_HOUSE("House", "भाव", "भाव"),
    ,
    SAHAM_LORD("Lord", "स्वामी", "स्वामी"),
    ,
    SAHAM_FORMULA("Formula", "सूत्र", "सूत्र"),
    ,
    SAHAM_INTERPRETATION("Interpretation", "व्याख्या", "व्याख्या"),
    ,
    SAHAM_ACTIVATION("Activation Period", "सक्रियता अवधि", "सक्रियता अवधि"),
    ,
    SAHAM_STRENGTH("Saham Strength", "सहम बल", "सहम बल"),

    ,

    // Info
    SAHAM_INFO_TITLE("About Saham", "सहमको बारेमा", "सहम के बारे में"),
    ,
    SAHAM_INFO_DESC("Saham (Arabic Parts) are sensitive points calculated from Ascendant, planet, and house cusps. Each Saham represents a specific life area. When transits or dashas activate a Saham, related events manifest.", "सहम (अरबी बिन्दु) लग्न, ग्रह, र भाव सन्धिबाट गणना गरिएका संवेदनशील बिन्दुहरू हुन्। प्रत्येक सहमले विशेष जीवन क्षेत्र प्रतिनिधित्व गर्छ। गोचर वा दशाले सहम सक्रिय गर्दा, सम्बन्धित घटनाहरू प्रकट हुन्छन्।", "सहम (अरबी बिन्दु) लग्न, ग्रह, और भाव सन्धिबाट गणना गरिए के संवेदनशील बिन्दु हैं। प्रत्येक सहमले विशेष जीवन क्षेत्र प्रतिनिधित्व करता है। गोचर या दशाले सहम सक्रिय गर्दा, संबंधित घटना प्रकट हैता हैन्।"),
    ,
    SAHAM_VEDIC_REF("Vedic Reference: Tajika Neelakanthi, Prashna Marga", "वैदिक सन्दर्भ: ताजिक नीलकण्ठी, प्रश्न मार्ग", "वैदिक सन्दर्भ: ताजिक नीलकण्ठी, प्रश्न मार्ग"),
    ,
    SAHAM_ANALYZING("Analyzing Sahams...", "सहम विश्लेषण गर्दै...", "सहम विश्लेषण गर्दै..."),
    ,
    SAHAM_UNABLE("Unable to calculate Sahams", "सहम गणना गर्न सकिएन", "सहम गणना करने सकिएन"),

    ,

    // ============================================
    // COMMON STRINGS
    // ============================================
    COMMON_VIRUPAS("Virupas", "विरूपा", "विरूपा"),
    ,
    COMMON_RUPAS("Rupas", "रूपा", "रूपा"),
    ,
    COMMON_SHASHTIAMSAS("Shashtiamsas", "षष्ट्यंश", "षष्ट्यंश"),
    ,
    COMMON_REQUIRED_MIN("Required Minimum", "आवश्यक न्यूनतम", "आवश्यक न्यूनतम"),
    ,
    COMMON_EXCELLENT("Excellent", "उत्कृष्ट", "उत्कृष्ट"),
    ,
    COMMON_GOOD("Good", "राम्रो", "राम्रो"),
    ,
    COMMON_FAIR("Fair", "ठीक", "ठीक"),
    ,
    COMMON_POOR("Poor", "कमजोर", "कमजोर"),
    ,
    COMMON_VIEW_DETAILS("View Details", "विवरण हेर्नुहोस्", "विवरण देखें"),
    ,
    COMMON_GOT_IT("Got It", "बुझें", "बुझें"),
,
;
}
