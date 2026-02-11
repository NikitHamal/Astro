package com.astro.storm.core.common



/**
 * Core UI string keys (navigation, settings, buttons, yoga, profile)
 * Part 1 of 4 split enums to avoid JVM method size limit
 */
enum class StringKey(override val en: String, override val ne: String, override val hi: String) : StringKeyInterface {

    // ============================================
    // NAVIGATION & TABS
    // ============================================
    TAB_HOME("Home", "गृह", "मुख्य"),
    ,
    TAB_INSIGHTS("Insights", "अन्तर्दृष्टि", "अंतर्दृष्टि"),
    ,
    TAB_CHAT("Chat", "च्याट", "चैट"),
    ,
    TAB_SETTINGS("Settings", "सेटिङ्स", "सेटिंग्स"),

    ,

    // ============================================
    // APP BRANDING
    // ============================================
    APP_NAME_KEY("AstroStorm", "एस्ट्रो-स्टोर्म", "एस्ट्रो-स्टॉर्म"),

    ,

    // ============================================
    // HOME TAB - SECTION HEADERS
    // ============================================
    HOME_CHART_ANALYSIS("Chart Analysis", "कुण्डली विश्लेषण", "कुंडली विश्लेषण"),
    ,
    HOME_COMING_SOON("Coming Soon", "छिट्टै आउँदैछ", "शीघ्र आ रहा है"),
    ,
    HOME_SOON_BADGE("Soon", "छिट्टै", "शीघ्र"),

    ,

    // ============================================
    // HOME TAB - FEATURE CARDS
    // ============================================
    FEATURE_BIRTH_CHART("Birth Chart", "जन्म कुण्डली", "जन्म कुंडली"),
    ,
    FEATURE_BIRTH_CHART_DESC("View your complete Vedic birth chart", "आफ्नो पूर्ण वैदिक जन्म कुण्डली हेर्नुहोस्", "अपना पूर्ण वैदिक जन्म कुंडली देखें"),

    ,

    FEATURE_PLANETS("Planets", "ग्रहहरू", "ग्रह"),
    ,
    FEATURE_PLANETS_DESC("Detailed planetary positions", "विस्तृत ग्रह स्थिति", "विस्तृत ग्रह स्थिति"),

    ,

    FEATURE_YOGAS("Yogas", "योगहरू", "योग"),
    ,
    FEATURE_YOGAS_DESC("Planetary combinations & effects", "ग्रह संयोजन र प्रभावहरू", "ग्रह संयहजन और प्रभाव"),

    ,

    FEATURE_DASHAS("Dashas", "दशाहरू", "दशा"),
    ,
    FEATURE_DASHAS_DESC("Planetary period timeline", "ग्रह अवधि समयरेखा", "ग्रह अवधि समयरेखा"),

    ,

    FEATURE_TRANSITS("Transits", "गोचरहरू", "गोचर"),
    ,
    FEATURE_TRANSITS_DESC("Current planetary movements", "हालको ग्रह गतिविधिहरू", "हाल के ग्रह गतिविधि"),

    ,

    FEATURE_ASHTAKAVARGA("Ashtakavarga", "अष्टकवर्ग", "अष्टकवर्ग"),
    ,
    FEATURE_ASHTAKAVARGA_DESC("Strength analysis by house", "भावानुसार बल विश्लेषण", "भावानुसार बल विश्लेषण"),

    ,

    // Ashtakavarga Details
    ASHTAKAVARGA_ABOUT_TITLE("About Ashtakavarga", "अष्टकवर्ग बारेमा", "अष्टकवर्ग बारे में"),
    ,
    ASHTAKAVARGA_ABOUT_DESC("Ashtakavarga is an ancient Vedic astrology technique for assessing planetary strength and predicting transit effects.", "अष्टकवर्ग वैदिक ज्योतिषको एक प्राचीन प्रविधि हो जसले ग्रहको शक्ति मापन र गोचर प्रभाव पूर्वानुमान गर्दछ।", "अष्टकवर्ग वैदिक ज्यहतिष के एक प्राचीन प्रविधि है जसले ग्रह के शक्ति मापन और गोचर प्रभाव पूर्वानुमान करता है।"),
    ,
    ASHTAKAVARGA_SAV_TITLE("Sarvashtakavarga (SAV)", "सर्वाष्टकवर्ग (SAV)", "सर्वाष्टकवर्ग (SAV)"),
    ,
    ASHTAKAVARGA_SAV_DESC("Combined strength points from all planets in each zodiac sign. Higher values (28+) indicate favorable areas.", "सबै ग्रहबाट प्रत्येक राशिमा संयोजित शक्ति बिन्दु। उच्च मान (२८+) अनुकूल क्षेत्रहरू संकेत गर्छ।", "सभी ग्रहबाट प्रत्येक राशि में संयहजित शक्ति बिन्दु। उच्च मान (२८+) अनुकूल क्षेत्र संकेत करता है।"),
    ,
    ASHTAKAVARGA_BAV_TITLE("Bhinnashtakavarga (BAV)", "भिन्नाष्टकवर्ग (BAV)", "भिन्नाष्टकवर्ग (BAV)"),
    ,
    ASHTAKAVARGA_BAV_DESC("Individual planet strength in each sign (0-8 bindus). Use this to predict transit effects.", "प्रत्येक राशिमा व्यक्तिगत ग्रहको शक्ति (०-८ बिन्दु)। गोचर प्रभाव पूर्वानुमान गर्न यो प्रयोग गर्नुहोस्।", "प्रत्येक राशि में व्यक्तिगत ग्रह के शक्ति (०-८ बिन्दु)। गोचर प्रभाव पूर्वानुमान करने यह प्रयहग करें।"),

    ,

    FEATURE_PANCHANGA("Panchanga", "पञ्चाङ्ग", "पंचांग"),
    ,
    FEATURE_PANCHANGA_DESC("Vedic calendar elements", "वैदिक पात्रो तत्वहरू", "वैदिक पात्रो तत्व"),

    ,

    FEATURE_MATCHMAKING("Matchmaking", "कुण्डली मिलान", "कुंडली मिलान"),
    ,
    FEATURE_MATCHMAKING_DESC("Kundli Milan compatibility", "विवाह मेलापक गुण मिलान", "विवाह मेलापक गुण मिलान"),

    ,

    FEATURE_MUHURTA("Muhurta", "मुहूर्त", "मुहूर्त"),
    ,
    FEATURE_MUHURTA_DESC("Auspicious timing finder", "शुभ समय खोजकर्ता", "शुभ समय खोजकर्ता"),

    ,

    FEATURE_REMEDIES("Remedies", "उपायहरू", "उपाय"),
    ,
    FEATURE_REMEDIES_DESC("Personalized remedies", "व्यक्तिगत उपायहरू", "व्यक्तिगत उपाय"),

    ,

    FEATURE_VARSHAPHALA("Varshaphala", "वर्षफल", "वर्षफल"),
    ,
    FEATURE_VARSHAPHALA_DESC("Solar return horoscope", "वार्षिक राशिफल", "वार्षिक राशिफल"),

    ,

    // Varshaphala Details
    VARSHAPHALA_TAB_OVERVIEW("Overview", "अवलोकन", "अवलोकन"),
    ,
    VARSHAPHALA_TAB_TAJIKA("Tajika", "तजिका", "तजि के"),
    ,
    VARSHAPHALA_TAB_SAHAMS("Sahams", "सहम", "सहम"),
    ,
    VARSHAPHALA_TAB_DASHA("Dasha", "दशा", "दशा"),
    ,
    VARSHAPHALA_TAB_HOUSES("Houses", "भावहरू", "भाव"),
    ,
    VARSHAPHALA_ANNUAL_HOROSCOPE("Annual Horoscope", "वार्षिक राशिफल", "वार्षिक राशिफल"),
    ,
    VARSHAPHALA_AGE("Age %d", "आयु %d", "आयु %d"),
    ,
    VARSHAPHALA_PREV_YEAR("Previous year", "अघिल्लो वर्ष", "अघिल्लो वर्ष"),
    ,
    VARSHAPHALA_NEXT_YEAR("Next year", "अर्को वर्ष", "अर् के वर्ष"),
    ,
    VARSHAPHALA_SELECT_CHART("Select a birth chart to view Varshaphala", "वर्षफल हेर्न जन्म कुण्डली छान्नुहोस्", "वर्षफल हेर्न जन्म कुंडली चुनें"),
    ,
    VARSHAPHALA_COMPUTING("Computing Annual Horoscope...", "वार्षिक राशिफल गणना गर्दै...", "वार्षिक राशिफल गणना गर्दै..."),
    ,
    VARSHAPHALA_COMPUTING_DESC("Calculating Tajika aspects, Sahams & Mudda Dasha", "तजिका पक्ष, सहम र मुद्द दशा गणना गर्दै", "तजि के पक्ष, सहम और मुद्द दशा गणना गर्दै"),
    ,
    VARSHAPHALA_ERROR("Calculation Error", "गणना त्रुटि", "गणना त्रुटि"),
    ,
    VARSHAPHALA_RESET_YEAR("Reset to Current Year", "वर्तमान वर्षमा रिसेट", "वर्तमान वर्ष में रिसेट"),
    ,
    VARSHAPHALA_SOLAR_RETURN("Solar Return", "सौर प्रतिफल", "सौर प्रतिफल"),
    ,
    VARSHAPHALA_YEAR_LORD("Year Lord", "वर्ष स्वामी", "वर्ष स्वामी"),
    ,
    VARSHAPHALA_MUNTHA("Muntha", "मुन्थ", "मुन्थ"),
    ,
    VARSHAPHALA_MUNTHA_LORD("Lord: %s", "स्वामी: %s", "स्वामी: %s"),
    ,
    VARSHAPHALA_TAJIKA_CHART("Tajika Annual Chart", "तजिका वार्षिक कुण्डली", "तजि के वार्षिक कुंडली"),
    ,
    VARSHAPHALA_PANCHA_VARGIYA("Pancha Vargiya Bala", "पञ्च वर्गीय बल", "पञ्च वर्गीय बल"),
    ,
    VARSHAPHALA_TRI_PATAKI("Tri-Pataki Chakra", "त्रि-पतकी चक्र", "त्रि-पतकी चक्र"),
    ,
    VARSHAPHALA_MAJOR_THEMES("Major Themes", "मुख्य विषयहरू", "मुख्य विषय"),
    ,
    VARSHAPHALA_MONTHLY_OUTLOOK("Monthly Outlook", "मासिक दृष्टिकोण", "मासिक दृष्टिकोण"),
    ,
    VARSHAPHALA_FAVORABLE("Favorable", "अनुकूल", "अनुकूल"),
    ,
    VARSHAPHALA_KEY_DATES("Key Dates", "महत्त्वपूर्ण मितिहरू", "महत्त्वपूर्ण मिति"),
    ,
    VARSHAPHALA_OVERALL_PREDICTION("Overall Prediction", "समग्र भविष्यवाणी", "समग्र भविष्यवाणी"),
    ,
    VARSHAPHALA_TAJIKA_SUMMARY("Tajika Yogas Summary", "तजिका योग सारांश", "तजि के यहग सारांश"),
    ,
    VARSHAPHALA_SAHAMS_TITLE("Sahams (Arabic Parts)", "सहम (अरबी भाग)", "सहम (अरबी भाग)"),
    ,
    VARSHAPHALA_SAHAMS_DESC("Sensitive points calculated from planetary positions", "ग्रह स्थितिबाट गणना गरिएको संवेदनशील बिन्दुहरू", "ग्रह स्थितिबाट गणना गरिए के संवेदनशील बिन्दु"),
    ,
    VARSHAPHALA_MUDDA_DASHA("Mudda Dasha", "मुद्द दशा", "मुद्द दशा"),
    ,
    VARSHAPHALA_MUDDA_DASHA_DESC("Annual planetary periods based on Moon's position", "चन्द्रको स्थितिमा आधारित वार्षिक ग्रह अवधिहरू", "चन्द्र के स्थिति में आधारित वार्षिक ग्रह अवधि"),
    ,
    VARSHAPHALA_POSITION("Position", "स्थिति", "स्थिति"),
    ,
    VARSHAPHALA_HOUSE("House", "भाव", "भाव"),
    ,
    VARSHAPHALA_DAYS("%d days", "%d दिन", "%d दिन"),

    ,

    FEATURE_PRASHNA("Prashna", "प्रश्न", "प्रश्न"),
    ,
    FEATURE_PRASHNA_DESC("Horary astrology", "प्रश्न ज्योतिष", "प्रश्न ज्यहतिष"),

    ,

    // Prashna Details


    FEATURE_SYNASTRY("Synastry", "सिनास्ट्री", "सिनास्ट्री"),
    ,
    FEATURE_SYNASTRY_DESC("Chart comparison", "कुण्डली तुलना", "कुंडली तुलना"),

    ,

    FEATURE_NAKSHATRAS("Nakshatras", "नक्षत्रहरू", "नक्षत्र"),
    ,
    FEATURE_NAKSHATRAS_DESC("Deep nakshatra analysis", "गहन नक्षत्र विश्लेषण", "गहन नक्षत्र विश्लेषण"),

    ,

    FEATURE_SHADBALA("Shadbala", "षड्बल", "षड्बल"),
    ,
    FEATURE_SHADBALA_DESC("Six-fold strength", "छवटा बलहरू", "हैवटा बल"),

    ,

    // Advanced Calculator Features
    FEATURE_SHODASHVARGA("Shodashvarga", "षोडशवर्ग", "षोडशवर्ग"),
    ,
    FEATURE_SHODASHVARGA_DESC("16-divisional chart strength", "१६-विभाजन कुण्डली बल", "१६-विभाजन कुंडली बल"),

    ,

    FEATURE_YOGINI_DASHA("Yogini Dasha", "योगिनी दशा", "यहगिनी दशा"),
    ,
    FEATURE_YOGINI_DASHA_DESC("36-year nakshatra dasha", "३६-वर्षे नक्षत्र दशा", "३६-वर्षे नक्षत्र दशा"),

    ,

    FEATURE_ARGALA("Argala", "अर्गला", "अर्गला"),
    ,
    FEATURE_ARGALA_DESC("Jaimini intervention analysis", "जैमिनी हस्तक्षेप विश्लेषण", "जैमिनी हस्तक्षेप विश्लेषण"),

    ,

    FEATURE_CHARA_DASHA("Chara Dasha", "चर दशा", "चर दशा"),
    ,
    FEATURE_CHARA_DASHA_DESC("Jaimini sign-based dasha", "जैमिनी राशि-आधारित दशा", "जैमिनी राशि-आधारित दशा"),

    ,

    FEATURE_BHRIGU_BINDU("Bhrigu Bindu", "भृगु बिन्दु", "भृगु बिन्दु"),
    ,
    FEATURE_BHRIGU_BINDU_DESC("Karmic destiny point", "कार्मिक भाग्य बिन्दु", "कार्मिक भाग्य बिन्दु"),

    ,

    FEATURE_PREDICTIONS("Predictions", "भविष्यवाणी", "भविष्यवाणी"),
    ,
    FEATURE_PREDICTIONS_DESC("Comprehensive life analysis", "व्यापक जीवन विश्लेषण", "व्यापक जीवन विश्लेषण"),

    ,

    // New Advanced Features
    FEATURE_ASHTOTTARI_DASHA("Ashtottari Dasha", "अष्टोत्तरी दशा", "अष्टोत्तरी दशा"),
    ,
    FEATURE_ASHTOTTARI_DASHA_DESC("108-year Nakshatra-based timing", "१०८ वर्षे नक्षत्र-आधारित समय", "१०८ वर्षे नक्षत्र-आधारित समय"),
    ,
    FEATURE_SUDARSHANA_CHAKRA("Sudarshana Chakra", "सुदर्शन चक्र", "सुदर्शन चक्र"),
    ,
    FEATURE_SUDARSHANA_CHAKRA_DESC("Triple-reference annual prediction", "त्रि-संदर्भ वार्षिक भविष्यवाणी", "त्रि-संदर्भ वार्षिक भविष्यवाणी"),
    ,
    FEATURE_MRITYU_BHAGA("Mrityu Bhaga", "मृत्यु भाग", "मृत्यु भाग"),
    ,
    FEATURE_MRITYU_BHAGA_DESC("Sensitive degrees analysis", "संवेदनशील अंश विश्लेषण", "संवेदनशील अंश विश्लेषण"),
    ,
    FEATURE_LAL_KITAB("Lal Kitab Remedies", "लाल किताब उपाय", "लाल किताब उपाय"),
    ,
    FEATURE_LAL_KITAB_DESC("Practical everyday remedies", "व्यावहारिक दैनिक उपाय", "व्यावहारिक दैनिक उपाय"),
    ,
    FEATURE_DIVISIONAL_CHARTS("Divisional Charts", "विभागीय कुण्डली", "विभागीय कुंडली"),
    ,
    FEATURE_DIVISIONAL_CHARTS_DESC("D-2, D-3, D-9, D-10, D-12 analysis", "होरा, द्रेक्काण, नवांश, दशमांश, द्वादशांश", "होरा, द्रेक्काण, नवांश, दशमांश, द्वादशांश"),
    ,
    FEATURE_UPACHAYA_TRANSIT("Upachaya Transits", "उपचय गोचर", "उपचय गोचर"),
    ,
    FEATURE_UPACHAYA_TRANSIT_DESC("Growth house transit tracking", "उपचय भाव गोचर विश्लेषण", "उपचय भाव गोचर विश्लेषण"),
    ,
    FEATURE_KALACHAKRA_DASHA("Kalachakra Dasha", "कालचक्र दशा", "कालचक्र दशा"),
    ,
    FEATURE_KALACHAKRA_DASHA_DESC("Body-soul timing system for health and spiritual predictions", "स्वास्थ्य र आध्यात्मिक भविष्यवाणीको लागि देह-आत्मा समय प्रणाली", "स्वास्थ्य और आध्यात्मिक भविष्यवाणी के लिए देह-आत् में समय प्रणाली"),
    ,
    FEATURE_TARABALA("Tarabala", "ताराबल", "ताराबल"),
    ,
    FEATURE_TARABALA_DESC("Daily Moon strength & Nakshatra timing", "दैनिक चन्द्र बल र नक्षत्र समय", "दैनिक चन्द्र बल और नक्षत्र समय"),
    ,
    FEATURE_ARUDHA_PADA("Arudha Pada", "आरूढ पद", "आरूढ पद"),
    ,
    FEATURE_ARUDHA_PADA_DESC("Jaimini Arudha analysis for manifestation", "जैमिनी आरूढ विश्लेषण", "जैमिनी आरूढ विश्लेषण"),
    ,
    FEATURE_GRAHA_YUDDHA("Graha Yuddha", "ग्रह युद्ध", "ग्रह युद्ध"),
    ,
    FEATURE_GRAHA_YUDDHA_DESC("Planetary war analysis & remedies", "ग्रह युद्ध विश्लेषण र उपाय", "ग्रह युद्ध विश्लेषण और उपाय"),
    ,
    FEATURE_DASHA_SANDHI("Dasha Sandhi", "दशा सन्धि", "दशा सन्धि"),
    ,
    FEATURE_DASHA_SANDHI_DESC("Period transition analysis", "अवधि संक्रमण विश्लेषण", "अवधि संक्रमण विश्लेषण"),
    ,
    FEATURE_GOCHARA_VEDHA("Gochara Vedha", "गोचर वेध", "गोचर वेध"),
    ,
    FEATURE_GOCHARA_VEDHA_DESC("Transit obstruction effects", "गोचर अवरोध प्रभाव", "गोचर अवरोध प्रभाव"),
    ,
    FEATURE_KEMADRUMA_YOGA("Kemadruma Yoga", "केमद्रुम योग", "केमद्रुम यहग"),
    ,
    FEATURE_KEMADRUMA_YOGA_DESC("Moon isolation analysis", "चन्द्र एकान्त विश्लेषण", "चन्द्र एकान्त विश्लेषण"),
    ,
    FEATURE_PANCH_MAHAPURUSHA("Panch Mahapurusha", "पञ्च महापुरुष", "पञ्च महापुरुष"),
    ,
    FEATURE_PANCH_MAHAPURUSHA_DESC("Five great person yogas", "पाँच महान् व्यक्ति योग", "पाँच महान् व्यक्ति यहग"),
    ,
    FEATURE_NITYA_YOGA("Nitya Yoga", "नित्य योग", "नित्य यहग"),
    ,
    FEATURE_NITYA_YOGA_DESC("27 daily yogas", "२७ दैनिक योगहरू", "२७ दैनिक यहग"),
    ,
    FEATURE_AVASTHA("Avastha", "अवस्था", "अवस्था"),
    ,
    FEATURE_AVASTHA_DESC("Planetary states analysis", "ग्रह अवस्था विश्लेषण", "ग्रह अवस्था विश्लेषण"),
    ,
    FEATURE_MARAKA("Maraka", "मारक", "मारक"),
    ,
    FEATURE_MARAKA_DESC("Death-inflicting planets", "मृत्यु कारक ग्रहहरू", "मृत्यु कारक ग्रह"),
    ,
    FEATURE_BADHAKA("Badhaka", "बाधक", "बाधक"),
    ,
    FEATURE_BADHAKA_DESC("Obstructing planets", "बाधा कारक ग्रहहरू", "बाधा कारक ग्रह"),
    ,
    FEATURE_VIPAREETA_RAJA_YOGA("Vipareeta Raja Yoga", "विपरीत राजयोग", "विपरीत राजयहग"),
    ,
    FEATURE_VIPAREETA_RAJA_YOGA_DESC("Reverse raja yogas", "विपरीत राजयोग विश्लेषण", "विपरीत राजयहग विश्लेषण"),
    ,
    FEATURE_ISHTA_KASHTA_PHALA("Ishta Kashta Phala", "इष्ट कष्ट फल", "इष्ट कष्ट फल"),
    ,
    FEATURE_ISHTA_KASHTA_PHALA_DESC("Benefic/malefic results", "शुभ/अशुभ फल विश्लेषण", "शुभ/अशुभ फल विश्लेषण"),
    ,
    FEATURE_SHOOLA_DASHA("Shoola Dasha", "शूल दशा", "शूल दशा"),
    ,
    FEATURE_SHOOLA_DASHA_DESC("Health & critical timing", "स्वास्थ्य र गम्भीर समय", "स्वास्थ्य और गम्भीर समय"),
    ,
    FEATURE_ASHTAVARGA_TRANSIT("Ashtavarga Transit", "अष्टकवर्ग गोचर", "अष्टकवर्ग गोचर"),
    ,
    FEATURE_ASHTAVARGA_TRANSIT_DESC("Transit predictions by bindu", "बिन्दुद्वारा गोचर भविष्यवाणी", "बिन्दुद्वारा गोचर भविष्यवाणी"),

    ,

    FEATURE_KAKSHYA_TRANSIT("Kakshya Transit", "कक्ष्या गोचर", "कक्ष्या गोचर"),
    ,
    FEATURE_KAKSHYA_TRANSIT_DESC("Advanced 8-fold micro-transit analysis", "८-गुणा सूक्ष्म गोचर विश्लेषण", "८-गुणा सूक्ष्म गोचर विश्लेषण"),

    ,

    FEATURE_NATIVE_ANALYSIS("Native Analysis", "जातक विश्लेषण", "जातक विश्लेषण"),
    ,
    FEATURE_NATIVE_ANALYSIS_DESC("Complete life profile & personality", "सम्पूर्ण जीवन प्रोफाइल र व्यक्तित्व", "संपूर्ण जीवन प्रोफाइल और व्यक्तित्व"),

    ,

    FEATURE_JAIMINI_KARAKA("Jaimini Karaka", "जैमिनी कारक", "जैमिनी कारक"),
    ,
    FEATURE_JAIMINI_KARAKA_DESC("Chara Karaka Analysis", "चर कारक विश्लेषण", "चर कारक विश्लेषण"),

    ,

    FEATURE_DRIG_DASHA("Drig Dasha", "दृग् दशा", "दृग् दशा"),
    ,
    FEATURE_DRIG_DASHA_DESC("Jaimini Longevity System", "जैमिनी आयुर्दाय प्रणाली", "जैमिनी आयुर्दाय प्रणाली"),

    ,

    FEATURE_SAPTAMSA("Saptamsa (D7)", "सप्तांश (D7)", "सप्तांश (D7)"),
    ,
    FEATURE_SAPTAMSA_DESC("Progeny & Fertility Analysis", "सन्तान र प्रजनन विश्लेषण", "संतान और प्रजनन विश्लेषण"),

    ,

    // Predictions Screen - Tabs
    PREDICTIONS_TAB_OVERVIEW("Overview", "सारांश", "अवलोकन"),
    ,
    PREDICTIONS_TAB_LIFE_AREAS("Life Areas", "जीवन क्षेत्रहरू", "जीवन क्षेत्र"),
    ,
    PREDICTIONS_TAB_TIMING("Timing", "समय", "समय"),
    ,
    PREDICTIONS_TAB_REMEDIES("Remedies", "उपाय", "उपाय"),

    ,

    // Predictions Screen - Section Headers
    PREDICTIONS_YOUR_LIFE_PATH("Your Life Path", "तपाईंको जीवन मार्ग", "आप के जीवन मार्ग"),
    ,
    PREDICTIONS_KEY_STRENGTHS("Key Strengths", "मुख्य शक्तिहरू", "मुख्य शक्ति"),
    ,
    PREDICTIONS_SPIRITUAL_PATH("Spiritual Path", "आध्यात्मिक मार्ग", "आध्यात्मिक मार्ग"),
    ,
    PREDICTIONS_CURRENT_PERIOD("Current Period", "वर्तमान अवधि", "वर्तमान अवधि"),
    ,
    PREDICTIONS_ACTIVE_TRANSITS("Active Transits", "सक्रिय गोचर", "सक्रिय गोचर"),
    ,
    PREDICTIONS_ACTIVE_YOGAS("Active Yogas", "सक्रिय योगहरू", "सक्रिय यहग"),
    ,
    PREDICTIONS_OPPORTUNITIES("Opportunities", "अवसरहरू", "अवसर"),
    ,
    PREDICTIONS_CURRENT_CHALLENGES("Current Challenges", "वर्तमान चुनौतीहरू", "वर्तमान चुनौती"),
    ,
    PREDICTIONS_LIFE_AREAS_GLANCE("Life Areas at a Glance", "एक नजरमा जीवन क्षेत्रहरू", "एक नजर में जीवन क्षेत्र"),
    ,
    PREDICTIONS_FAVORABLE_PERIODS("Favorable Periods", "अनुकूल अवधिहरू", "अनुकूल अवधि"),
    ,
    PREDICTIONS_CAUTION_PERIODS("Periods Needing Caution", "सावधानी चाहिने अवधिहरू", "सावधानी चाहिने अवधि"),
    ,
    PREDICTIONS_IMPORTANT_DATES("Important Dates", "महत्त्वपूर्ण मितिहरू", "महत्त्वपूर्ण मिति"),
    ,
    PREDICTIONS_REMEDIAL_SUGGESTIONS("Remedial Suggestions", "उपचारात्मक सुझावहरू", "उपचारात्मक सुझाव"),

    ,

    // Predictions Screen - States
    PREDICTIONS_NO_CHART_SELECTED("No Chart Selected", "कुनै कुण्डली छानिएको छैन", "कुनै कुंडली हैानिए के हैैन"),
    ,
    PREDICTIONS_SELECT_CHART_MESSAGE("Please select a birth chart to view predictions", "कृपया भविष्यवाणी हेर्न जन्म कुण्डली छान्नुहोस्", "कृपया भविष्यवाणी हेर्न जन्म कुंडली चुनें"),
    ,
    PREDICTIONS_CALCULATING("Calculating Predictions...", "भविष्यवाणी गणना गर्दै...", "भविष्यवाणी गणना गर्दै..."),
    ,
    PREDICTIONS_ERROR_LOADING("Error Loading Predictions", "भविष्यवाणी लोड गर्न त्रुटि", "भविष्यवाणी लोड करने त्रुटि"),
    ,
    PREDICTIONS_CALC_FAILED("Failed to calculate predictions", "भविष्यवाणी गणना गर्न असफल", "भविष्यवाणी गणना करने असफल"),

    ,

    // Predictions Screen - Life Area Labels
    PREDICTIONS_CAREER_PROFESSION("Career & Profession", "क्यारियर र पेशा", "क्यारियर और पेशा"),
    ,
    PREDICTIONS_FINANCE_WEALTH("Finance & Wealth", "वित्त र सम्पत्ति", "वित्त और सम्पत्ति"),
    ,
    PREDICTIONS_RELATIONSHIPS_MARRIAGE("Relationships & Marriage", "सम्बन्ध र विवाह", "संबंध और विवाह"),
    ,
    PREDICTIONS_HEALTH_WELLBEING("Health & Wellbeing", "स्वास्थ्य र कल्याण", "स्वास्थ्य और कल्याण"),
    ,
    PREDICTIONS_EDUCATION_LEARNING("Education & Learning", "शिक्षा र सिकाइ", "शिक्षा और सिकाइ"),
    ,
    PREDICTIONS_FAMILY_HOME("Family & Home", "परिवार र घर", "परिवार और घर"),

    ,

    // Predictions Screen - Other Labels
    PREDICTIONS_SHORT_TERM("Short-term (3-6 months)", "अल्पकालीन (३-६ महिना)", "अल्पकालीन (३-६ महिना)"),
    ,
    PREDICTIONS_MEDIUM_TERM("Medium-term (6-12 months)", "मध्यमकालीन (६-१२ महिना)", "मध्यमकालीन (६-१२ महिना)"),
    ,
    PREDICTIONS_LONG_TERM("Long-term (1-2 years)", "दीर्घकालीन (१-२ वर्ष)", "दीर्घकालीन (१-२ वर्ष)"),
    ,
    PREDICTIONS_BEST_FOR("Best for", "को लागि उत्तम", "के लिए उत्तम"),
    ,
    PREDICTIONS_CAUTION_FOR("Caution for", "को लागि सावधानी", "के लिए सावधानी"),
    ,
    PREDICTIONS_ENERGY_LEVEL("Energy Level", "ऊर्जा स्तर", "ऊर्जा स्तर"),
    ,
    PREDICTIONS_DAYS_LEFT("days left", "दिन बाँकी", "दिन बाँकी"),
    ,
    PREDICTIONS_MONTHS("months", "महिना", "महिना"),
    ,
    PREDICTIONS_GO_BACK("Go Back", "फर्कनुहोस्", "फर्कनुहोस्"),

    ,

    // ============================================
    // EMPTY/ERROR STATES
    // ============================================
    NO_PROFILE_SELECTED("No Profile Selected", "कुनै प्रोफाइल छानिएको छैन", "कुनै प्रोफाइल हैानिए के हैैन"),
    ,
    NO_PROFILE_MESSAGE("Select or create a profile to view your personalized astrological insights.", "आफ्नो व्यक्तिगत ज्योतिष अन्तर्दृष्टि हेर्न प्रोफाइल छान्नुहोस् वा बनाउनुहोस्।", "अपना व्यक्तिगत ज्यहतिष अंतर्दृष्टि हेर्न प्रोफाइल चुनें या बनाएं।"),
    ,
    NO_PROFILE_MESSAGE_LONG("Select or create a profile to view your personalized astrological insights and predictions", "आफ्नो व्यक्तिगत ज्योतिष अन्तर्दृष्टि र भविष्यवाणी हेर्न प्रोफाइल छान्नुहोस् वा बनाउनुहोस्", "अपना व्यक्तिगत ज्यहतिष अंतर्दृष्टि और भविष्यवाणी हेर्न प्रोफाइल चुनें या बनाएं"),

    ,

    // Error States
    ERROR_PARTIAL("Some insights unavailable", "केही अन्तर्दृष्टिहरू उपलब्ध छैनन्", "केही अंतर्दृष्टि उपलब्ध हैैनन्"),
    ,
    ERROR_CALCULATIONS_FAILED("%d calculation(s) could not be completed", "%d गणना(हरू) पूरा हुन सकेन", "%d गणना() पूरा हुन सकेन"),
    ,
    ERROR_UNABLE_TO_LOAD("Unable to Load Insights", "अन्तर्दृष्टि लोड गर्न असमर्थ", "अंतर्दृष्टि लोड करने असमर्थ"),
    ,
    ERROR_HOROSCOPE_UNAVAILABLE("%s's horoscope unavailable", "%s को राशिफल उपलब्ध छैन", "%s  के राशिफल उपलब्ध हैैन"),
    ,
    ERROR_EPHEMERIS_DATA("Unable to calculate planetary positions for this period. This may be due to ephemeris data limitations.", "यस अवधिको लागि ग्रह स्थितिहरू गणना गर्न असमर्थ। यो ईफेमेरिस डाटा सीमितताको कारण हुन सक्छ।", "इस अवधि के लिए ग्रह स्थिति गणना करने असमर्थ। यह ईफेमेरिस डाटा सीमितता के कारण हुन सक्है।"),
    ,
    ERROR_SOMETHING_WRONG("Something went wrong", "केही गलत भयो", "केही गलत भयह"),

    ,

    // ============================================
    // BUTTONS & ACTIONS
    // ============================================
    BTN_DETAILS("Details", "विवरण", "विवरण"),
    ,
    BTN_RETRY("Retry", "पुनः प्रयास", "पुनः प्रयास"),
    ,
    BTN_TRY_AGAIN("Try Again", "फेरि प्रयास गर्नुहोस्", "फेरि प्रयास करें"),
    ,
    BTN_PREVIEW("Preview", "पूर्वावलोकन", "पूर्वावलोकन"),
    ,
    BTN_OK("OK", "ठीक छ", "ठीक है"),
    ,
    BTN_CANCEL("Cancel", "रद्द गर्नुहोस्", "रद्द करें"),
    ,
    BTN_JUMP_TODAY("Jump to today", "आजमा जानुहोस्", "आज में जानुहोस्"),
    ,
    INFO_DASHA("Dasha information", "दशा जानकारी", "दशा जानकारी"),
    ,
    BTN_DELETE("Delete", "मेट्नुहोस्", "मिटाएं"),
    ,
    BTN_EDIT("Edit", "सम्पादन गर्नुहोस्", "संपादित करें"),
    ,
    BTN_SAVE("Save", "सेभ गर्नुहोस्", "सहेजें"),
    ,
    BTN_GENERATE("Generate", "उत्पन्न गर्नुहोस्", "उत्पन्न करें"),
    ,
    BTN_GENERATE_SAVE("Generate & Save", "उत्पन्न गर्नुहोस् र सेभ गर्नुहोस्", "उत्पन्न करें और सेभ करें"),
    ,
    BTN_UPDATE_SAVE("Update & Save", "अपडेट गर्नुहोस् र सेभ गर्नुहोस्", "अपडेट करें और सेभ करें"),
    ,
    BTN_GO_BACK("Go back", "पछाडि जानुहोस्", "पीहैे जानुहोस्"),
    ,
    BTN_BACK("Back", "पछाडि", "पीछे"),
    ,
    BTN_CLOSE("Close", "बन्द गर्नुहोस्", "बंद करें"),
    ,
    BTN_REFRESH("Refresh", "रिफ्रेस", "रिफ्रेश"),
    ,
    BTN_REGENERATE("Regenerate", "पुन: उत्पन्न", "पुन: उत्पन्न"),
    ,
    BTN_CREATE_CHART("Create Chart", "कुण्डली बनाउनुहोस्", "कुंडली बनाएं"),

    ,

    // ============================================
    // LABELS & INDICATORS
    // ============================================
    LABEL_AD("AD", "ई.सं.", "ई.सं."),
    ,
    LABEL_BS("BS", "वि.सं.", "वि.सं."),
    ,
    LABEL_DATE("DATE", "मिति", "मिति"),
    ,
    LABEL_TIME("TIME", "समय", "समय"),
    ,
    LABEL_TODAY("TODAY", "आज", "आज"),
    ,
    LABEL_BIRTH("BIRTH", "जन्म", "जन्म"),

    ,

    // ============================================
    // INSIGHTS TAB
    // ============================================
    INSIGHTS_OVERALL_ENERGY("Overall Energy", "समग्र ऊर्जा", "समग्र ऊर्जा"),
    ,
    INSIGHTS_LIFE_AREAS("Life Areas", "जीवन क्षेत्रहरू", "जीवन क्षेत्र"),
    ,
    INSIGHTS_LUCKY_ELEMENTS("Lucky Elements", "भाग्यशाली तत्वहरू", "भाग्यशाली तत्व"),
    ,
    INSIGHTS_TODAYS_AFFIRMATION("Today's Affirmation", "आजको प्रतिज्ञा", "आज के प्रतिज्ञा"),
    ,
    INSIGHTS_WEEKLY_ENERGY("Weekly Energy Flow", "साप्ताहिक ऊर्जा प्रवाह", "साप्ताहिक ऊर्जा प्रवाह"),
    ,
    INSIGHTS_KEY_DATES("Key Dates", "महत्त्वपूर्ण मितिहरू", "महत्त्वपूर्ण मिति"),
    ,
    INSIGHTS_WEEKLY_OVERVIEW("Weekly Overview by Area", "क्षेत्रअनुसार साप्ताहिक अवलोकन", "क्षेत्रअनुसार साप्ताहिक अवलोकन"),
    ,
    INSIGHTS_WEEKLY_ADVICE("Weekly Advice", "साप्ताहिक सल्लाह", "साप्ताहिक सल्लाह"),
    ,
    INSIGHTS_RECOMMENDATIONS("Recommendations", "सिफारिसहरू", "सिफारिस"),
    ,
    INSIGHTS_CAUTIONS("Cautions", "सावधानीहरू", "सावधानी"),

    ,

    // Horoscope periods
    PERIOD_TODAY("Today", "आज", "आज"),
    ,
    PERIOD_TOMORROW("Tomorrow", "भोलि", "भोलि"),
    ,
    PERIOD_WEEKLY("Weekly", "साप्ताहिक", "साप्ताहिक"),

    ,

    ERROR_ANALYSIS_FAILED("Analysis Failed", "विश्लेषण असफल", "विश्लेषण असफल"),
    ,
    MSG_MAY_TAKE_MOMENT("This may take a moment", "यसमा केही समय लाग्न सक्छ", "इसमें केही समय लाग्न सक्है"),
    ,
    LIFE_AREA_CAREER("Career", "करियर", "करियर"),
    ,
    LIFE_AREA_LOVE("Love & Relationships", "प्रेम र सम्बन्ध", "प्रेम और संबंध"),
    ,
    LIFE_AREA_HEALTH("Health & Vitality", "स्वास्थ्य र जीवनशक्ति", "स्वास्थ्य और जीवनशक्ति"),
    ,
    LIFE_AREA_FINANCE("Finance & Wealth", "वित्त र सम्पत्ति", "वित्त और सम्पत्ति"),
    ,
    LIFE_AREA_FAMILY("Family & Home", "परिवार र घर", "परिवार और घर"),
    ,
    LIFE_AREA_SPIRITUALITY("Spiritual Growth", "आध्यात्मिक वृद्धि", "आध्यात्मिक वृद्धि"),

    ,

    // For compatibility with LocalizedDisplayNames.kt
    LIFE_AREA_CAREER_FULL("Career", "क्यारियर", "करियर"),
    ,
    LIFE_AREA_LOVE_FULL("Love & Relationships", "प्रेम र सम्बन्ध", "प्रेम और संबंध"),
    ,
    LIFE_AREA_HEALTH_FULL("Health & Vitality", "स्वास्थ्य र जीवनशक्ति", "स्वास्थ्य और जीवनशक्ति"),
    ,
    LIFE_AREA_FINANCE_FULL("Finance & Wealth", "वित्त र सम्पत्ति", "वित्त और सम्पत्ति"),
    ,
    LIFE_AREA_FAMILY_FULL("Family & Home", "परिवार र घर", "परिवार और घर"),
    ,
    LIFE_AREA_SPIRITUALITY_FULL("Spiritual Growth", "आध्यात्मिक वृद्धि", "आध्यात्मिक वृद्धि"),

    ,
    
    // Theme names
    THEME_DYNAMIC_ACTION("Dynamic Action", "गत्यात्मक कार्य", "गत्यात्मक कार्य"),
    ,
    THEME_PRACTICAL_PROGRESS("Practical Progress", "व्यावहारिक प्रगति", "व्यावहारिक प्रगति"),
    ,
    THEME_SOCIAL_CONNECTIONS("Social Connections", "सामाजिक सम्बन्ध", "सामाजिक संबंध"),
    ,
    THEME_EMOTIONAL_INSIGHT("Emotional Insight", "भावनात्मक अन्तर्दृष्टि", "भावनात्मक अंतर्दृष्टि"),
    ,
    THEME_EXPANSION_WISDOM("Expansion & Wisdom", "विस्तार र ज्ञान", "विस्तार और ज्ञान"),
    ,
    THEME_HARMONY_BEAUTY("Harmony & Beauty", "सामञ्जस्य र सौन्दर्य", "सामञ्जस्य और सौन्दर्य"),
    ,
    THEME_DISCIPLINE_GROWTH("Discipline & Growth", "अनुशासन र वृद्धि", "अनुशासन और वृद्धि"),
    ,
    THEME_COMMUNICATION_LEARNING("Communication & Learning", "सञ्चार र सिकाई", "सञ्चार और सिकाई"),
    ,
    THEME_ENERGY_INITIATIVE("Energy & Initiative", "ऊर्जा र पहल", "ऊर्जा और पहल"),
    ,
    THEME_SELF_EXPRESSION("Self-Expression", "आत्म-अभिव्यक्ति", "आत्म-अभिव्यक्ति"),
    ,
    THEME_INTUITION_NURTURING("Intuition & Nurturing", "अन्तर्ज्ञान र पोषण", "अन्तर्ज्ञान और पोषण"),
    ,
    THEME_TRANSFORMATION("Transformation", "रूपान्तरण", "रूपान्तरण"),
    ,
    THEME_SPIRITUAL_LIBERATION("Spiritual Liberation", "आध्यात्मिक मुक्ति", "आध्यात्मिक मुक्ति"),
    ,
    THEME_BALANCE_EQUILIBRIUM("Balance & Equilibrium", "सन्तुलन र स्थिरता", "सन्तुलन और स्थिरता"),

    ,
    
    // Theme descriptions
    THEME_DESC_DYNAMIC_ACTION("Your energy is high and aligned with fire elements. This is an excellent day for taking initiative, starting new projects, and asserting yourself confidently. Channel this vibrant energy into productive pursuits.", "तपाईंको ऊर्जा उच्च छ र अग्नि तत्वसँग मिल्दोजुल्दो छ। यो पहल गर्न, नयाँ परियोजनाहरू सुरु गर्न र आत्मविश्वासका साथ आफूलाई प्रस्तुत गर्न उत्कृष्ट दिन हो। यस जीवन्त ऊर्जालाई उत्पादक कार्यहरूमा लगाउनुहोस्।", "आप के ऊर्जा उच्च है और अग्नि तत्वसँग मिल्दोजुल्दो है। यह पहल करने, नयाँ परियहजना सुरु करने और आत्मविश्वास के साथ आफू  के प्रस्तुत करने उत्कृष्ट दिन है। इस जीवन्त ऊर्जा  के उत्पादक कार्य में लगाउनुहोस्।"),
    ,
    THEME_DESC_PRACTICAL_PROGRESS("Grounded earth energy supports methodical progress today. Focus on practical tasks, financial planning, and building stable foundations. Your efforts will yield tangible results.", "पृथ्वी तत्वको ऊर्जाले आज व्यवस्थित प्रगतिलाई समर्थन गर्दछ। व्यावहारिक कार्यहरू, वित्तीय योजना, र स्थिर आधार निर्माणमा ध्यान दिनुहोस्। तपाईंको प्रयासले मूर्त परिणामहरू दिनेछ।", "पृथ्वी तत्व के ऊर्जाले आज व्यवस्थित प्रगति  के समर्थन करता है। व्यावहारिक कार्य, वित्तीय यहजना, और स्थिर आधार निर्माण में ध्यान दिनुहोस्। आप के प्रयासले मूर्त परिणाम दिनेहै।"),
    ,
    THEME_DESC_SOCIAL_CONNECTIONS("Air element energy enhances communication and social interactions. Networking, negotiations, and intellectual pursuits are favored. Express your ideas and connect with like-minded people.", "वायु तत्वको ऊर्जाले सञ्चार र सामाजिक अन्तरक्रियालाई बढावा दिन्छ। नेटवर्किङ, वार्ता, र बौद्धिक कार्यहरूका लागि आज अनुकूल छ। आफ्ना विचारहरू व्यक्त गर्नुहोस् र समान विचारधारा भएका मानिसहरूसँग जोडिनुहोस्।", "वायु तत्व के ऊर्जाले सञ्चार और सामाजिक अन्तरक्रिया  के बढावा देता है। नेटवर्किङ, वार्ता, और बौद्धिक कार्य के लिए आज अनुकूल है। आफ्ना विचार व्यक्त करें और समान विचारधारा हुए मानिससँग जोडिनुहोस्।"),
    ,
    THEME_DESC_EMOTIONAL_INSIGHT("Water element energy deepens your intuition and emotional awareness. Trust your feelings and pay attention to subtle cues. This is a powerful day for healing and self-reflection.", "जल तत्वको ऊर्जाले तपाईंको अन्तर्ज्ञान र भावनात्मक जागरूकतालाई गहिरो बनाउँछ। आफ्ना भावनाहरूमा विश्वास गर्नुहोस् र सूक्ष्म संकेतहरूमा ध्यान दिनुहोस्। यो निको पार्न र आत्म-चिन्तनका लागि एक शक्तिशाली दिन हो।", "जल तत्व के ऊर्जाले आप के अन्तर्ज्ञान और भावनात्मक जागरूकता  के गहिरो बनाउँहै। आफ्ना भावना में विश्वास करें और सूक्ष्म संकेत में ध्यान दिनुहोस्। यह नि के पार्न और आत्म-चिन्तन के लिए एक शक्तिशाली दिन है।"),
    ,
    THEME_DESC_EXPANSION_WISDOM("Jupiter's benevolent influence brings opportunities for growth, learning, and good fortune. Be open to new possibilities and share your wisdom generously.", "बृहस्पतिको उदार प्रभावले वृद्धि, सिकाई र सौभाग्यको अवसरहरू ल्याउँछ। नयाँ सम्भावनाहरूका लागि खुला रहनुहोस् र आफ्नो ज्ञान उदारतापूर्वक बाँड्नुहोस्।", "बृहस्पति के उदार प्रभावले वृद्धि, सिकाई और सौभाग्य के अवसर लाता है। नयाँ सम्भावना के लिए खुला रहनुहोस् और अपना ज्ञान उदारतापूर्वक बाँड्नुहोस्।"),
    ,
    THEME_DESC_HARMONY_BEAUTY("Venus graces you with appreciation for beauty, art, and relationships. Indulge in pleasurable activities and nurture your connections with loved ones.", "शुक्रले तपाईंलाई सौन्दर्य, कला, र सम्बन्धहरूको लागि प्रशंसा प्रदान गर्दछ। आनन्ददायी गतिविधिहरूमा संलग्न हुनुहोस् र आफ्ना प्रियजनहरूसँगको सम्बन्धलाई प्रगाढ बनाउनुहोस्।", "शुक्रले आप  के सौन्दर्य, कला, और संबंध के लिए प्रशंसा प्रदान करता है। आनन्ददायी गतिविधि में संलग्न हुनुहोस् और आफ्ना प्रियजनसँग के संबंध  के प्रगाढ बनाएं।"),
    ,
    THEME_DESC_DISCIPLINE_GROWTH("Saturn's influence calls for patience, hard work, and responsibility. Embrace challenges as opportunities for growth and stay committed to your long-term goals.", "शनिको प्रभावले धैर्य, कडा परिश्रम, र जिम्मेवारीको माग गर्दछ। चुनौतीहरूलाई वृद्धिको अवसरको रूपमा स्वीकार गर्नुहोस् र आफ्ना दीर्घकालीन लक्ष्यहरूमा प्रतिबद्ध रहनुहोस्।", "शनि के प्रभावले धैर्य, कडा परिश्रम, और जिम्मेवारी के माग करता है। चुनौती  के वृद्धि के अवसर के रूप में स्वीकार करें और आफ्ना दीर्घकालीन लक्ष्य में प्रतिबद्ध रहनुहोस्।"),
    ,
    THEME_DESC_COMMUNICATION_LEARNING("Mercury enhances your mental agility and communication skills. This is ideal for learning, teaching, writing, and all forms of information exchange.", "बुधले तपाईंको मानसिक चपलता र सञ्चार सीपलाई बढावा दिन्छ। यो सिकाई, शिक्षण, लेखन, र सबै प्रकारका सूचना आदानप्रदानका लागि उपयुक्त छ।", "बुधले आप के मानसिक चपलता और सञ्चार सीप  के बढावा देता है। यह सिकाई, शिक्षण, लेखन, और सभी प्रकार के सूचना आदानप्रदान के लिए उपयुक्त है।"),
    ,
    THEME_DESC_ENERGY_INITIATIVE("Mars provides courage and drive. Take bold action, compete with integrity, and channel aggressive energy into constructive activities.", "मंगलले साहस र जोश प्रदान गर्दछ। साहसी कदम चाल्नुहोस्, इमानदारीका साथ प्रतिस्पर्धा गर्नुहोस्, र आक्रामक ऊर्जालाई रचनात्मक गतिविधिहरूमा लगाउनुहोस्।", "मंगलले साहस और जोश प्रदान करता है। साहसी कदम चाल्नुहोस्, इमानदारी के साथ प्रतिस्पर्धा करें, और आक्रामक ऊर्जा  के रचनात्मक गतिविधि में लगाउनुहोस्।"),
    ,
    THEME_DESC_SELF_EXPRESSION("The Sun illuminates your path to self-expression and leadership. Shine your light confidently and pursue activities that bring you recognition.", "सूर्यले तपाईंको आत्म-अभिव्यक्ति र नेतृत्वको मार्गलाई उज्यालो बनाउँछ। आत्मविश्वासका साथ आफ्नो चमक देखाउनुहोस् र तपाईंलाई पहिचान दिलाउने गतिविधिहरूमा लाग्नुहोस्।", "सूर्यले आप के आत्म-अभिव्यक्ति और नेतृत्व के मार्ग  के उज्यालो बनाउँहै। आत्मविश्वास के साथ अपना चमक देखाउनुहोस् और आप  के पहिचान दिलाउने गतिविधि में लाग्नुहोस्।"),
    ,
    THEME_DESC_INTUITION_NURTURING("The Moon heightens your sensitivity and caring nature. Nurture yourself and others, and trust your instincts in important decisions.", "चन्द्रमाले तपाईंको संवेदनशीलता र हेरचाह गर्ने प्रकृतिलाई बढाउँछ। आफू र अरूको पोषण गर्नुहोस्, र महत्त्वपूर्ण निर्णयहरूमा आफ्नो अन्तर्ज्ञानमा विश्वास गर्नुहोस्।", "चन्द्रमाले आप के संवेदनशीलता और हेरचाह करनेे प्रकृति  के बढाउँहै। आफू और अरू के पोषण करें, और महत्त्वपूर्ण निर्णय में अपना अन्तर्ज्ञान में विश्वास करें।"),
    ,
    THEME_DESC_TRANSFORMATION("Rahu's influence brings unconventional opportunities and desires for change. Embrace innovation but stay grounded in your values.", "राहुको प्रभावले अपरम्परागत अवसरहरू र परिवर्तनको इच्छा ल्याउँछ। नवीनतालाई अँगाल्नुहोस् तर आफ्ना मूल्यहरूमा अडिग रहनुहोस्।", "राहु के प्रभावले अपरम्परागत अवसर और परिवर्तन के इच्हैा लाता है। नवीनता  के अँगाल्नुहोस् तर आफ्ना मूल्य में अडिग रहनुहोस्।"),
    ,
    THEME_DESC_SPIRITUAL_LIBERATION("Ketu's energy supports detachment and spiritual insight. Let go of what no longer serves you and focus on inner growth.", "केतुको ऊर्जाले वैराग्य र आध्यात्मिक अन्तर्दृष्टिलाई समर्थन गर्दछ। जे कुराले तपाईंलाई अब फाइदा गर्दैन त्यसलाई छोडिदिनुहोस् र आन्तरिक वृद्धिमा ध्यान दिनुहोस्।", "केतु के ऊर्जाले वैराग्य और आध्यात्मिक अंतर्दृष्टि  के समर्थन करता है। जे कुराले आप  के अब फाइदा गर्दैन त्इस  के हैोडिदिनुहोस् और आन्तरिक वृद्धि में ध्यान दिनुहोस्।"),
    ,
    THEME_DESC_BALANCE_EQUILIBRIUM("A day of balance where all energies are in equilibrium. Maintain steadiness and make measured progress in all areas of life.", "सबै ऊर्जाहरू सन्तुलनमा रहेको दिन। स्थिरता कायम राख्नुहोस् र जीवनका सबै क्षेत्रमा नापिएको प्रगति गर्नुहोस्।", "सभी ऊर्जा सन्तुलन में रहे के दिन। स्थिरता कायम राख्नुहोस् और जीवन के सभी क्षेत्र में नापिए के प्रगति करें।"),

    ,
    
    // Weekly themes
    THEME_WEEK_OPPORTUNITIES("Week of Opportunities", "अवसरहरूको हप्ता", "अवसर के हप्ता"),
    ,
    THEME_WEEK_STEADY_PROGRESS("Steady Progress", "स्थिर प्रगति", "स्थिर प्रगति"),
    ,
    THEME_WEEK_MINDFUL_NAVIGATION("Mindful Navigation", "सचेत यात्रा", "सचेत यात्रा"),

    ,
    
    // Weekly overview templates
    WEEKLY_OVERVIEW_EXCELLENT("excellent opportunities for growth and success. ", "वृद्धि र सफलताको लागि उत्कृष्ट अवसरहरू। ", "वृद्धि और सफलता के लिए उत्कृष्ट अवसर।"),
    ,
    WEEKLY_OVERVIEW_STEADY("steady progress and balanced energy. ", "स्थिर प्रगति र सन्तुलित ऊर्जा। ", "स्थिर प्रगति और सन्तुलित ऊर्जा।"),
    ,
    WEEKLY_OVERVIEW_CHALLENGING("challenges that, when navigated wisely, lead to growth. ", "चुनौतीहरू जसलाई बुद्धिमानीपूर्वक सामना गर्दा वृद्धि हुन्छ। ", "चुनौती जस  के बुद्धिमानीपूर्वक सामना गर्दा वृद्धि हैता है।"),
    ,
    WEEKLY_OVERVIEW_FOOTER("Trust in your cosmic guidance and make the most of each day's unique energy.", "आफ्नो ब्रह्माण्डीय मार्गदर्शनमा विश्वास गर्नुहोस् र प्रत्येक दिनको अद्वितीय ऊर्जाको सदुपयोग गर्नुहोस्।", "अपना ब्रह्माण्डीय मार्गदर्शन में विश्वास करें और प्रत्येक दिन के अद्वितीय ऊर्जा के सदुपयहग करें।"),

    ,
    
    // Weekly advice
    ADVICE_JUPITER("embrace opportunities for learning and expansion. Your wisdom and optimism attract positive outcomes.", "सिकाई र विस्तारका अवसरहरूलाई अँगाल्नुहोस्। तपाईंको ज्ञान र आशावादले सकारात्मक परिणामहरू आकर्षित गर्दछ।", "सिकाई और विस्तार के अवसर  के अँगाल्नुहोस्। आप के ज्ञान और आशावादले सकारात्मक परिणाम आकर्षित करता है।"),
    ,
    ADVICE_VENUS("focus on cultivating beauty, harmony, and meaningful relationships. Artistic pursuits are favored.", "सौन्दर्य, सामञ्जस्य, र अर्थपूर्ण सम्बन्धहरू विकास गर्नमा ध्यान दिनुहोस्। कलात्मक कार्यहरूलाई प्राथमिकता दिइन्छ।", "सौन्दर्य, सामञ्जस्य, और अर्थपूर्ण संबंध विकास करने में ध्यान दिनुहोस्। कलात्मक कार्य  के प्राथमिकता दिइन्है।"),
    ,
    ADVICE_SATURN("embrace discipline and patience. Hard work now builds lasting foundations for the future.", "अनुशासन र धैर्यलाई अँगाल्नुहोस्। अहिलेको कडा परिश्रमले भविष्यको लागि स्थायी आधार निर्माण गर्दछ।", "अनुशासन और धैर्य  के अँगाल्नुहोस्। अहिले के कडा परिश्रमले भविष्य के लिए स्थायी आधार निर्माण करता है।"),
    ,
    ADVICE_MERCURY("prioritize communication, learning, and intellectual activities. Your mind is sharp.", "सञ्चार, सिकाई, र बौद्धिक गतिविधिहरूलाई प्राथमिकता दिनुहोस्। तपाईंको दिमाग तेज छ।", "सञ्चार, सिकाई, और बौद्धिक गतिविधि  के प्राथमिकता दिनुहोस्। आप के दिमाग तेज है।"),
    ,
    ADVICE_MARS("channel your energy into productive activities. Exercise and competition are favored.", "आफ्नो ऊर्जालाई उत्पादक गतिविधिहरूमा लगाउनुहोस्। व्यायाम र प्रतिस्पर्धालाई प्राथमिकता दिइन्छ।", "अपना ऊर्जा  के उत्पादक गतिविधि में लगाउनुहोस्। व्यायाम और प्रतिस्पर्धा  के प्राथमिकता दिइन्है।"),
    ,
    ADVICE_SUN("let your light shine. Leadership roles and self-expression bring recognition.", "आफ्नो चमक फैलिन दिनुहोस्। नेतृत्वदायी भूमिका र आत्म-अभिव्यक्तिले पहिचान दिलाउँछ।", "अपना चमक फैलिन दिनुहोस्। नेतृत्वदायी भूमि के और आत्म-अभिव्यक्तिले पहिचान दिलाउँहै।"),
    ,
    ADVICE_MOON("honor your emotions and intuition. Nurturing activities bring fulfillment.", "आफ्ना भावना र अन्तर्ज्ञानको सम्मान गर्नुहोस्। पोषण गर्ने गतिविधिहरूले सन्तुष्टि ल्याउँछ।", "आफ्ना भावना और अन्तर्ज्ञान के सम्मान करें। पोषण करनेे गतिविधिले सन्तुष्टि लाता है।"),
    ,
    ADVICE_RAHU("embrace transformation while staying grounded. Unconventional approaches may succeed.", "आधारभूत मूल्यहरूमा रहँदै परिवर्तनलाई अँगाल्नुहोस्। अपरम्परागत दृष्टिकोणहरू सफल हुन सक्छन्।", "आधारभूत मूल्य में रहँदै परिवर्तन  के अँगाल्नुहोस्। अपरम्परागत दृष्टिकोण सफल हुन सक्हैं।"),
    ,
    ADVICE_KETU("focus on spiritual growth and letting go. Detachment brings peace.", "आध्यात्मिक वृद्धि र त्यागमा ध्यान दिनुहोस्। वैराग्यले शान्ति ल्याउँछ।", "आध्यात्मिक वृद्धि और त्याग में ध्यान दिनुहोस्। वैराग्यले शांति लाता है।"),
    ,
    ADVICE_GENERAL("maintain balance and trust in divine timing.", "सन्तुलन कायम राख्नुहोस् र ईश्वरीय समयमा विश्वास गर्नुहोस्।", "सन्तुलन कायम राख्नुहोस् और ईश्वरीय समय में विश्वास करें।"),

    ,
    
    // Lucky elements labels
    LUCKY_NUMBER("Number", "अंक", "अंक"),
    ,
    LUCKY_COLOR("Color", "रंग", "रंग"),
    ,
    LUCKY_DIRECTION("Direction", "दिशा", "दिशा"),
    ,
    LUCKY_GEMSTONE("Gemstone", "रत्न", "रत्न"),

    ,

    // ============================================
    // DASHA SECTION
    // ============================================
    DASHA_CURRENT_PERIOD("Current Planetary Period", "हालको ग्रह अवधि", "हाल के ग्रह अवधि"),
    ,
    DASHA_ACTIVE("Active", "सक्रिय", "सक्रिय"),
    ,
    DASHA_MAHADASHA("Mahadasha", "महादशा", "महादशा"),
    ,
    DASHA_ANTARDASHA("Antardasha", "अन्तर्दशा", "अन्तर्दशा"),
    ,
    DASHA_PRATYANTARDASHA("Pratyantardasha:", "प्रत्यन्तर्दशा:", "प्रत्यन्तर्दशा:"),
    ,
    DASHA_UPCOMING("Upcoming Periods", "आगामी अवधिहरू", "आगामी अवधि"),
    ,
    DASHA_REMAINING("remaining", "बाँकी", "बाँकी"),
    ,
    DASHA_PERIOD_NAME("%s: %s", "%s: %s", "%s: %s"),
    ,
    DASHA_COMBINED_NAME("%s-%s", "%s-%s", "%s-%s"),
    ,
    DASHA_LAST_IN_MAHADASHA("Current Antardasha is the last in this Mahadasha", "हालको अन्तर्दशा यस महादशाको अन्तिम हो", "हाल के अन्तर्दशा इस महादशा के अन्तिम है"),
    ,
    DASHA_STARTS("Starts %s", "%s मा सुरु हुन्छ", "%s में सुरु हैता है"),
    ,
    DASHA_VIMSHOTTARI("Vimshottari Dasha", "विम्शोत्तरी दशा", "विम्शोत्तरी दशा"),
    ,
    DASHA_JUMP_TO_TODAY("Jump to current period", "हालको अवधिमा जानुहोस्", "हाल के अवधि में जानुहोस्"),
    ,
    DASHA_CALCULATING("Calculating...", "गणना गर्दै...", "गणना गर्दै..."),
    ,
    DASHA_ERROR("Error", "त्रुटि", "त्रुटि"),
    ,
    DASHA_CALCULATING_TIMELINE("Calculating Dasha Timeline", "दशा समयरेखा गणना गर्दै", "दशा समयरेखा गणना गर्दै"),
    ,
    DASHA_CALCULATING_DESC("Computing planetary periods based on\nMoon's Nakshatra position...", "चन्द्रको नक्षत्र स्थितिको आधारमा\nग्रह अवधिहरू गणना गर्दै...", "चन्द्र के नक्षत्र स्थिति के आधारमा\nग्रह अवधि गणना गर्दै..."),
    ,
    DASHA_CALCULATION_FAILED("Calculation Failed", "गणना असफल भयो", "गणना असफल भयह"),
    ,
    DASHA_NO_CHART_SELECTED("No Chart Selected", "कुनै कुण्डली छानिएको छैन", "कुनै कुंडली हैानिए के हैैन"),
    ,
    DASHA_NO_CHART_MESSAGE("Please select or create a birth profile\nto view the Dasha timeline.", "दशा समयरेखा हेर्न कृपया जन्म प्रोफाइल\nछान्नुहोस् वा बनाउनुहोस्।", "दशा समयरेखा हेर्न कृपया जन्म प्रोफाइल\nचुनें या बनाएं।"),

    ,

    // Dasha Level Names
    DASHA_SOOKSHMADASHA("Sookshmadasha", "सूक्ष्मदशा", "सूक्ष्मदशा"),
    ,
    DASHA_PRANADASHA("Pranadasha", "प्राणदशा", "प्राणदशा"),
    ,
    DASHA_DEHADASHA("Dehadasha", "देहदशा", "देहदशा"),
    ,
    DASHA_BHUKTI("Bhukti", "भुक्ति", "भुक्ति"),
    ,
    DASHA_PRATYANTAR("Pratyantar", "प्रत्यन्तर", "प्रत्यन्तर"),
    ,
    DASHA_SOOKSHMA("Sookshma", "सूक्ष्म", "सूक्ष्म"),
    ,
    DASHA_PRANA("Prana", "प्राण", "प्राण"),
    ,
    DASHA_DEHA("Deha", "देह", "देह"),

    ,

    // Dasha Format Labels
    DASHA_DURATION("Duration", "अवधि", "अवधि"),
    ,
    DASHA_PERIOD("Period", "अवधि", "अवधि"),
    ,
    DASHA_STATUS("Status", "स्थिति", "स्थिति"),
    ,
    DASHA_CURRENTLY_ACTIVE("Currently Active", "हाल सक्रिय", "हाल सक्रिय"),
    ,
    DASHA_PROGRESS("Progress", "प्रगति", "प्रगति"),
    ,
    DASHA_NO_ACTIVE_PERIOD("No active Dasha period", "कुनै सक्रिय दशा अवधि छैन", "कुनै सक्रिय दशा अवधि हैैन"),

    ,

    // Time Units
    YEARS("years", "वर्ष", "वर्ष"),
    ,
    DAYS("days", "दिन", "दिन"),
    ,
    TO("to", "देखि", "देखि"),
    ,
    YEARS_SHORT("y", "व", "व"),
    ,
    MONTHS_SHORT("m", "म", "म"),
    ,
    WEEKS_SHORT("w", "ह", "ह"),
    ,
    DAYS_SHORT("d", "दि", "दि"),
    ,
    HOURS_SHORT("h", "घ", "घ"),
    ,
    MINUTES_SHORT("m", "मि", "मि"),

    ,

    // Yogini Dasha Names
    YOGINI_MANGALA("Mangala", "मङ्गला", "मङ्गला"),
    ,
    YOGINI_PINGALA("Pingala", "पिङ्गला", "पिङ्गला"),
    ,
    YOGINI_DHANYA("Dhanya", "धन्या", "धन्या"),
    ,
    YOGINI_BHRAMARI("Bhramari", "भ्रामरी", "भ्रामरी"),
    ,
    YOGINI_BHADRIKA("Bhadrika", "भद्रिका", "भद्रि के"),
    ,
    YOGINI_ULKA("Ulka", "उल्का", "उल् के"),
    ,
    YOGINI_SIDDHA("Siddha", "सिद्धा", "सिद्धा"),
    ,
    YOGINI_SANKATA("Sankata", "सङ्कटा", "सङ्कटा"),

    ,

    // Yogini Deity Names
    YOGINI_DEITY_CHANDRA("Chandra (Moon)", "चन्द्र", "चन्द्र"),
    ,
    YOGINI_DEITY_SURYA("Surya (Sun)", "सूर्य", "सूर्य"),
    ,
    YOGINI_DEITY_GURU("Guru (Jupiter)", "गुरु (बृहस्पति)", "गुरु (बृहस्पति)"),
    ,
    YOGINI_DEITY_MANGAL("Mangal (Mars)", "मङ्गल", "मङ्गल"),
    ,
    YOGINI_DEITY_BUDHA("Budha (Mercury)", "बुध", "बुध"),
    ,
    YOGINI_DEITY_SHANI("Shani (Saturn)", "शनि", "शनि"),
    ,
    YOGINI_DEITY_SHUKRA("Shukra (Venus)", "शुक्र", "शुक्र"),
    ,
    YOGINI_DEITY_RAHU("Rahu", "राहु", "राहु"),

    ,

    // Nature Types
    NATURE_BENEFIC("Benefic", "शुभ", "शुभ"),
    ,
    NATURE_MALEFIC("Malefic", "अशुभ", "अशुभ"),
    ,
    NATURE_MIXED("Mixed", "मिश्रित", "मिश्रित"),

    ,

    // ============================================
    // TRANSITS
    // ============================================
    TRANSITS_CURRENT("Current Transits", "हालका गोचरहरू", "हाल के गोचर"),
    ,
    TRANSITS_MOON_IN("Moon in %s", "चन्द्रमा %s मा", "चन्द्र में %s में"),

    ,

    // ============================================
    // ENERGY DESCRIPTIONS
    // ============================================
    ENERGY_EXCEPTIONAL("Exceptional cosmic alignment - seize every opportunity!", "असाधारण ब्रह्माण्डीय संरेखण - हरेक अवसर समात्नुहोस्!", "असाधारण ब्रह्माण्डीय संरेखण - हरेक अवसर समात्नुहोस्!"),
    ,
    ENERGY_EXCELLENT("Excellent day ahead - favorable for important decisions", "उत्कृष्ट दिन अगाडि - महत्त्वपूर्ण निर्णयहरूको लागि अनुकूल", "उत्कृष्ट दिन अगाडि - महत्त्वपूर्ण निर्णय के लिए अनुकूल"),
    ,
    ENERGY_STRONG("Strong positive energy - good for new initiatives", "बलियो सकारात्मक ऊर्जा - नयाँ पहलहरूको लागि राम्रो", "बलियह सकारात्मक ऊर्जा - नयाँ पहल के लिए राम्रो"),
    ,
    ENERGY_FAVORABLE("Favorable energy - maintain steady progress", "अनुकूल ऊर्जा - स्थिर प्रगति कायम राख्नुहोस्", "अनुकूल ऊर्जा - स्थिर प्रगति कायम राख्नुहोस्"),
    ,
    ENERGY_BALANCED("Balanced energy - stay centered and focused", "सन्तुलित ऊर्जा - केन्द्रित र ध्यान केन्द्रित रहनुहोस्", "सन्तुलित ऊर्जा - केन्द्रित और ध्यान केन्द्रित रहनुहोस्"),
    ,
    ENERGY_MODERATE("Moderate energy - pace yourself wisely", "मध्यम ऊर्जा - बुद्धिमानीपूर्वक आफ्नो गति मिलाउनुहोस्", "मध्यम ऊर्जा - बुद्धिमानीपूर्वक अपना गति मिलाउनुहोस्"),
    ,
    ENERGY_LOWER("Lower energy day - prioritize rest and reflection", "कम ऊर्जा दिन - आराम र चिन्तनलाई प्राथमिकता दिनुहोस्", "कम ऊर्जा दिन - आराम और चिन्तन  के प्राथमिकता दिनुहोस्"),
    ,
    ENERGY_CHALLENGING("Challenging day - practice patience and self-care", "चुनौतीपूर्ण दिन - धैर्य र आत्म-हेरचाह अभ्यास गर्नुहोस्", "चुनौतीपूर्ण दिन - धैर्य और आत्म-हेरचाह अभ्यास करें"),
    ,
    ENERGY_REST("Rest and recharge recommended - avoid major decisions", "आराम र रिचार्ज सिफारिस - प्रमुख निर्णयहरूबाट बच्नुहोस्", "आराम और रिचार्ज सिफारिस - प्रमुख निर्णयबाट बच्नुहोस्"),

    ,

    // ============================================
    // LUCKY ELEMENTS
    // ============================================
    LUCKY_COLOR_FIRE("Red, Orange, or Gold", "रातो, सुन्तला, वा सुनौलो", "रातो, सुन्तला, या सुनौलो"),
    ,
    LUCKY_COLOR_EARTH("Green, Brown, or White", "हरियो, खैरो, वा सेतो", "हरियह, खैरो, या सेतो"),
    ,
    LUCKY_COLOR_AIR("Blue, Light Blue, or Silver", "निलो, हल्का निलो, वा चाँदी", "निलो, हल् के निलो, या चाँदी"),
    ,
    LUCKY_COLOR_WATER("White, Cream, or Sea Green", "सेतो, क्रीम, वा समुद्री हरियो", "सेतो, क्रीम, या समुद्री हरियह"),

    ,

    LUCKY_DIRECTION_EAST("East", "पूर्व", "पूर्व"),
    ,
    LUCKY_DIRECTION_WEST("West", "पश्चिम", "पश्चिम"),
    ,
    LUCKY_DIRECTION_NORTH("North", "उत्तर", "उत्तर"),
    ,
    LUCKY_DIRECTION_SOUTH("South", "दक्षिण", "दक्षिण"),
    ,
    LUCKY_DIRECTION_NORTHEAST("North-East", "उत्तर-पूर्व", "उत्तर-पूर्व"),
    ,
    LUCKY_DIRECTION_NORTHWEST("North-West", "उत्तर-पश्चिम", "उत्तर-पश्चिम"),
    ,
    LUCKY_DIRECTION_SOUTHEAST("South-East", "दक्षिण-पूर्व", "दक्षिण-पूर्व"),
    ,
    LUCKY_DIRECTION_SOUTHWEST("South-West", "दक्षिण-पश्चिम", "दक्षिण-पश्चिम"),

    ,

    // ============================================
    // GEMSTONES
    // ============================================
    GEMSTONE_RUBY("Ruby", "माणिक", "माणिक"),
    ,
    GEMSTONE_PEARL("Pearl", "मोती", "मोती"),
    ,
    GEMSTONE_RED_CORAL("Red Coral", "मूंगा", "मूंगा"),
    ,
    GEMSTONE_EMERALD("Emerald", "पन्ना", "पन्ना"),
    ,
    GEMSTONE_YELLOW_SAPPHIRE("Yellow Sapphire", "पुष्पराज", "पुष्पराज"),
    ,
    GEMSTONE_DIAMOND("Diamond or White Sapphire", "हीरा वा सेतो नीलम", "हीरा या सेतो नीलम"),
    ,
    GEMSTONE_BLUE_SAPPHIRE("Blue Sapphire", "नीलम", "नीलम"),
    ,
    GEMSTONE_HESSONITE("Hessonite", "गोमेद", "गोमेद"),
    ,
    GEMSTONE_CATS_EYE("Cat's Eye", "वैदूर्य", "वैदूर्य"),

    ,

    // ============================================
    // DASHA RECOMMENDATIONS
    // ============================================
    DASHA_REC_SUN("Engage in activities that build confidence and leadership skills.", "आत्मविश्वास र नेतृत्व कौशल विकास गर्ने गतिविधिहरूमा संलग्न हुनुहोस्।", "आत्मविश्वास और नेतृत्व कौशल विकास करनेे गतिविधि में संलग्न हुनुहोस्।"),
    ,
    DASHA_REC_MOON("Prioritize emotional well-being and nurturing relationships.", "भावनात्मक कल्याण र पोषणपूर्ण सम्बन्धहरूलाई प्राथमिकता दिनुहोस्।", "भावनात्मक कल्याण और पोषणपूर्ण संबंध  के प्राथमिकता दिनुहोस्।"),
    ,
    DASHA_REC_MARS("Channel your energy into physical activities and competitive pursuits.", "आफ्नो ऊर्जालाई शारीरिक गतिविधि र प्रतिस्पर्धात्मक प्रयासहरूमा प्रयोग गर्नुहोस्।", "अपना ऊर्जा  के शारीरिक गतिविधि और प्रतिस्पर्धात्मक प्रयास में प्रयहग करें।"),
    ,
    DASHA_REC_MERCURY("Focus on learning, communication, and intellectual growth.", "सिकाइ, सञ्चार र बौद्धिक वृद्धिमा ध्यान दिनुहोस्।", "सिकाइ, सञ्चार और बौद्धिक वृद्धि में ध्यान दिनुहोस्।"),
    ,
    DASHA_REC_JUPITER("Expand your horizons through education, travel, or spiritual practices.", "शिक्षा, यात्रा वा आध्यात्मिक अभ्यासहरूको माध्यमबाट आफ्नो क्षितिज विस्तार गर्नुहोस्।", "शिक्षा, यात्रा या आध्यात्मिक अभ्यास के माध्यमबाट अपना क्षितिज विस्तार करें।"),
    ,
    DASHA_REC_VENUS("Cultivate beauty, art, and harmonious relationships.", "सौन्दर्य, कला र सामञ्जस्यपूर्ण सम्बन्धहरू विकास गर्नुहोस्।", "सौन्दर्य, कला और सामञ्जस्यपूर्ण संबंध विकास करें।"),
    ,
    DASHA_REC_SATURN("Embrace discipline, hard work, and long-term planning.", "अनुशासन, कडा परिश्रम र दीर्घकालीन योजनालाई अँगाल्नुहोस्।", "अनुशासन, कडा परिश्रम और दीर्घकालीन यहजना  के अँगाल्नुहोस्।"),
    ,
    DASHA_REC_RAHU("Explore unconventional paths while staying grounded.", "जमिनमा रहँदै अपरम्परागत मार्गहरू अन्वेषण गर्नुहोस्।", "जमिन में रहँदै अपरम्परागत मार्ग अन्वेषण करें।"),
    ,
    DASHA_REC_KETU("Practice detachment and focus on spiritual development.", "वैराग्यको अभ्यास गर्नुहोस् र आध्यात्मिक विकासमा ध्यान दिनुहोस्।", "वैराग्य के अभ्यास करें और आध्यात्मिक विकास में ध्यान दिनुहोस्।"),

    ,

    // ============================================
    // DASHA AFFIRMATIONS
    // ============================================
    DASHA_AFF_SUN("I shine my light confidently and inspire those around me.", "म आत्मविश्वासका साथ आफ्नो प्रकाश फैलाउँछु र वरिपरिका मानिसहरूलाई प्रेरित गर्छु।", "म आत्मविश्वास के साथ अपना प्रकाश फैलाउँहैु और वरिपरि के मानिस  के प्रेरित करता हैु।"),
    ,
    DASHA_AFF_MOON("I trust my intuition and nurture myself with compassion.", "म आफ्नो अन्तर्ज्ञानमाथि विश्वास गर्छु र करुणाका साथ आफूलाई पोषण गर्छु।", "म अपना अन्तर्ज्ञानमाथि विश्वास करता हैु और करुणा के साथ आफू  के पोषण करता हैु।"),
    ,
    DASHA_AFF_MARS("I channel my energy constructively and act with courage.", "म आफ्नो ऊर्जा रचनात्मक रूपमा प्रयोग गर्छु र साहसका साथ काम गर्छु।", "म अपना ऊर्जा रचनात्मक रूप में प्रयहग करता हैु और साहस के साथ काम करता हैु।"),
    ,
    DASHA_AFF_MERCURY("I communicate clearly and embrace continuous learning.", "म स्पष्ट रूपमा सञ्चार गर्छु र निरन्तर सिकाइलाई अँगाल्छु।", "म स्पष्ट रूप में सञ्चार करता हैु और निरन्तर सिकाइ  के अँगाल्हैु।"),
    ,
    DASHA_AFF_JUPITER("I am open to abundance and share my wisdom generously.", "म प्रचुरताको लागि खुला छु र आफ्नो ज्ञान उदारतापूर्वक साझा गर्छु।", "म प्रचुरता के लिए खुला हैु और अपना ज्ञान उदारतापूर्वक साझा करता हैु।"),
    ,
    DASHA_AFF_VENUS("I attract beauty and harmony into my life.", "म आफ्नो जीवनमा सौन्दर्य र सामञ्जस्य आकर्षित गर्छु।", "म अपना जीवन में सौन्दर्य और सामञ्जस्य आकर्षित करता हैु।"),
    ,
    DASHA_AFF_SATURN("I embrace discipline and trust in the timing of my journey.", "म अनुशासनलाई अँगाल्छु र आफ्नो यात्राको समयमाथि विश्वास गर्छु।", "म अनुशासन  के अँगाल्हैु और अपना यात्रा के समयमाथि विश्वास करता हैु।"),
    ,
    DASHA_AFF_RAHU("I embrace change and transform challenges into opportunities.", "म परिवर्तनलाई अँगाल्छु र चुनौतीहरूलाई अवसरहरूमा रूपान्तरण गर्छु।", "म परिवर्तन  के अँगाल्हैु और चुनौती  के अवसर में रूपान्तरण करता हैु।"),
    ,
    DASHA_AFF_KETU("I release what no longer serves me and embrace spiritual growth.", "म जुन कुराले अब मेरो सेवा गर्दैन त्यसलाई छाड्छु र आध्यात्मिक वृद्धिलाई अँगाल्छु।", "म जो कुराले अब मेरो सेवा गर्दैन त्इस  के हैाड्हैु और आध्यात्मिक वृद्धि  के अँगाल्हैु।"),

    ,

    // ============================================
    // ELEMENT RECOMMENDATIONS
    // ============================================
    ELEMENT_REC_FIRE("Take bold action and express yourself confidently.", "साहसी कदम चाल्नुहोस् र आत्मविश्वासका साथ आफूलाई व्यक्त गर्नुहोस्।", "साहसी कदम चाल्नुहोस् और आत्मविश्वास के साथ आफू  के व्यक्त करें।"),
    ,
    ELEMENT_REC_EARTH("Focus on practical matters and material progress.", "व्यावहारिक मामिलाहरू र भौतिक प्रगतिमा ध्यान दिनुहोस्।", "व्यावहारिक मामिला और भौतिक प्रगति में ध्यान दिनुहोस्।"),
    ,
    ELEMENT_REC_AIR("Engage in social activities and intellectual pursuits.", "सामाजिक गतिविधिहरू र बौद्धिक प्रयासहरूमा संलग्न हुनुहोस्।", "सामाजिक गतिविधि और बौद्धिक प्रयास में संलग्न हुनुहोस्।"),
    ,
    ELEMENT_REC_WATER("Trust your intuition and honor your emotions.", "आफ्नो अन्तर्ज्ञानमाथि विश्वास गर्नुहोस् र आफ्ना भावनाहरूलाई सम्मान गर्नुहोस्।", "अपना अन्तर्ज्ञानमाथि विश्वास करें और आफ्ना भावना  के सम्मान करें।"),

    ,

    // ============================================
    // LIFE AREA RECOMMENDATIONS
    // ============================================
    AREA_REC_CAREER("Capitalize on favorable career energy today.", "आज अनुकूल क्यारियर ऊर्जाको फाइदा उठाउनुहोस्।", "आज अनुकूल क्यारियर ऊर्जा के फाइदा उठाउनुहोस्।"),
    ,
    AREA_REC_LOVE("Nurture your relationships with extra attention.", "थप ध्यानका साथ आफ्ना सम्बन्धहरूलाई पोषण गर्नुहोस्।", "थप ध्यान के साथ आफ्ना संबंध  के पोषण करें।"),
    ,
    AREA_REC_HEALTH("Make the most of your vibrant health energy.", "आफ्नो जीवन्त स्वास्थ्य ऊर्जाको अधिकतम फाइदा लिनुहोस्।", "अपना जीवन्त स्वास्थ्य ऊर्जा के अधिकतम फाइदा लिनुहोस्।"),
    ,
    AREA_REC_FINANCE("Take advantage of positive financial influences.", "सकारात्मक वित्तीय प्रभावहरूको फाइदा उठाउनुहोस्।", "सकारात्मक वित्तीय प्रभाव के फाइदा उठाउनुहोस्।"),
    ,
    AREA_REC_FAMILY("Spend quality time with family members.", "परिवारका सदस्यहरूसँग गुणस्तरीय समय बिताउनुहोस्।", "परिवार के सदस्यसँग गुणस्तरीय समय बिताउनुहोस्।"),
    ,
    AREA_REC_SPIRITUALITY("Deepen your spiritual practices.", "आफ्नो आध्यात्मिक अभ्यासहरूलाई गहिरो बनाउनुहोस्।", "अपना आध्यात्मिक अभ्यास  के गहिरो बनाएं।"),

    ,

    // ============================================
    // PLANET CAUTIONS
    // ============================================
    CAUTION_SATURN("Avoid rushing into decisions. Patience is key.", "निर्णयहरूमा हतार नगर्नुहोस्। धैर्य महत्त्वपूर्ण छ।", "निर्णय में हतार नकरें। धैर्य महत्त्वपूर्ण है।"),
    ,
    CAUTION_MARS("Control impulsive reactions and avoid conflicts.", "आवेगपूर्ण प्रतिक्रियाहरू नियन्त्रण गर्नुहोस् र विवादहरूबाट बच्नुहोस्।", "आवेगपूर्ण प्रतिक्रिया नियन्त्रण करें और विवादबाट बच्नुहोस्।"),
    ,
    CAUTION_RAHU("Be wary of deception and unrealistic expectations.", "छलकपट र अवास्तविक अपेक्षाहरूबाट सावधान रहनुहोस्।", "हैलकपट और अवास्तविक अपेक्षाबाट सावधान रहनुहोस्।"),
    ,
    CAUTION_KETU("Don't neglect practical responsibilities for escapism.", "पलायनवादको लागि व्यावहारिक जिम्मेवारीहरूलाई बेवास्ता नगर्नुहोस्।", "पलायनवाद के लिए व्यावहारिक जिम्मेवारी  के बेवास्ता नकरें।"),

    ,

    // ============================================
    // HOROSCOPE UI STRINGS
    // ============================================
    HOROSCOPE_BALANCE("Balance", "सन्तुलन", "सन्तुलन"),
    ,
    HOROSCOPE_STEADY_ENERGY("Steady energy expected", "स्थिर ऊर्जा अपेक्षित", "स्थिर ऊर्जा अपेक्षित"),
    ,
    HOROSCOPE_CALCULATING("Calculating...", "गणना गर्दै...", "गणना गर्दै..."),
    ,
    HOROSCOPE_VEDHA_OBSTRUCTION("However, %s creates Vedha obstruction, reducing benefits.", "तर, %s ले वेध अवरोध सिर्जना गर्छ, फाइदाहरू घटाउँछ।", "तर, %s ले वेध अवरोध सिर्जना करता है, फाइदा घटाउँहै।"),
    ,
    HOROSCOPE_ASHTAKAVARGA_STRONG("Ashtakavarga (%d/8) strengthens results.", "अष्टकवर्ग (%d/8) ले परिणामहरू बलियो बनाउँछ।", "अष्टकवर्ग (%d/8) ले परिणाम बलियह बनाउँहै।"),
    ,
    HOROSCOPE_ASHTAKAVARGA_MODERATE("Ashtakavarga (%d/8) moderates results.", "अष्टकवर्ग (%d/8) ले परिणामहरूलाई मध्यम बनाउँछ।", "अष्टकवर्ग (%d/8) ले परिणाम  के मध्यम बनाउँहै।"),
    ,
    HOROSCOPE_ASHTAKAVARGA_WEAK("Low Ashtakavarga (%d/8) weakens results.", "कम अष्टकवर्ग (%d/8) ले परिणामहरू कमजोर बनाउँछ।", "कम अष्टकवर्ग (%d/8) ले परिणाम कमजोर बनाउँहै।"),
    ,
    HOROSCOPE_RETROGRADE_DELAY("%s's retrograde motion delays manifestation.", "%s को वक्री गतिले प्रकटीकरणमा ढिलाइ गर्छ।", "%s  के वक्री गतिले प्रकटीकरण में ढिलाइ करता है।"),
    ,
    HOROSCOPE_RETROGRADE_RELIEF("%s's retrograde provides some relief from challenges.", "%s को वक्रीले चुनौतीहरूबाट केही राहत प्रदान गर्छ।", "%s  के वक्रीले चुनौतीबाट केही राहत प्रदान करता है।"),
    ,
    HOROSCOPE_OWN_SIGN("Strong in own sign.", "आफ्नै राशिमा बलियो।", "आफ्नै राशि में बलियह।"),
    ,
    HOROSCOPE_EXALTED("Exalted - excellent results.", "उच्च - उत्कृष्ट परिणामहरू।", "उच्च - उत्कृष्ट परिणाम।"),
    ,
    HOROSCOPE_DEBILITATED("Debilitated - results weakened.", "नीच - परिणामहरू कमजोर।", "नीच - परिणाम कमजोर।"),
    ,
    HOROSCOPE_FAVORABLE_TRANSIT("Favorable %s transit in house %d.", "भाव %d मा अनुकूल %s गोचर।", "भाव %d में अनुकूल %s गोचर।"),
    ,
    HOROSCOPE_UNFAVORABLE_TRANSIT("Challenging %s transit in house %d.", "भाव %d मा चुनौतीपूर्ण %s गोचर।", "भाव %d में चुनौतीपूर्ण %s गोचर।"),
    ,
    HOROSCOPE_BALANCED_ENERGY("Balanced energy in this area.", "यस क्षेत्रमा सन्तुलित ऊर्जा।", "इस क्षेत्र में सन्तुलित ऊर्जा।"),

    ,

    // ============================================
    // SETTINGS TAB
    // ============================================
    SETTINGS_PROFILE("Profile", "प्रोफाइल", "प्रोफाइल"),
    ,
    SETTINGS_EXPORT("Export", "निर्यात", "निर्यात"),
    ,
    SETTINGS_AI_CHAT("AI & Chat", "AI र च्याट", "AI और च्याट"),
    ,
    SETTINGS_AI_MODELS("AI Models", "AI मोडेलहरू", "AI मोडेल"),
    ,
    SETTINGS_AI_MODELS_DESC("Configure chat AI providers", "च्याट AI प्रदायकहरू कन्फिगर गर्नुहोस्", "च्याट AI प्रदायक कन्फिगर करें"),
    ,
    SETTINGS_PREFERENCES("Preferences", "प्राथमिकताहरू", "प्राथमिकता"),
    ,
    SETTINGS_ABOUT("About", "बारेमा", "बारे में"),

    ,

    // Profile settings
    SETTINGS_EDIT_PROFILE("Edit Profile", "प्रोफाइल सम्पादन", "प्रोफाइल सम्पादन"),
    ,
    SETTINGS_EDIT_PROFILE_DESC("Modify birth details", "जन्म विवरण परिमार्जन गर्नुहोस्", "जन्म विवरण परिमार्जन करें"),
    ,
    SETTINGS_MANAGE_PROFILES("Manage Profiles", "प्रोफाइलहरू व्यवस्थापन", "प्रोफाइल व्यवस्थापन"),
    ,
    SETTINGS_NO_PROFILE("No profile selected", "कुनै प्रोफाइल छानिएको छैन", "कुनै प्रोफाइल हैानिए के हैैन"),
    ,
    SETTINGS_TAP_TO_SELECT("Tap to select or create a profile", "प्रोफाइल छान्न वा बनाउन ट्याप गर्नुहोस्", "प्रोफाइल हैान्न या बनाउन ट्याप करें"),

    ,

    // Export settings
    SETTINGS_EXPORT_PDF("Export as PDF", "PDF को रूपमा निर्यात", "PDF  के रूप में निर्यात"),
    ,
    SETTINGS_EXPORT_PDF_DESC("Complete chart report", "पूर्ण कुण्डली रिपोर्ट", "पूर्ण कुंडली रिपोर्ट"),
    ,
    SETTINGS_EXPORT_IMAGE("Export as Image", "छविको रूपमा निर्यात", "हैवि के रूप में निर्यात"),
    ,
    SETTINGS_EXPORT_IMAGE_DESC("High-quality chart image", "उच्च गुणस्तर कुण्डली छवि", "उच्च गुणस्तर कुंडली हैवि"),
    ,
    SETTINGS_EXPORT_JSON("Export as JSON", "JSON को रूपमा निर्यात", "JSON  के रूप में निर्यात"),
    ,
    SETTINGS_EXPORT_JSON_DESC("Machine-readable format", "मेसिन-पठनयोग्य ढाँचा", "मेसिन-पठनयहग्य ढाँचा"),
    ,
    SETTINGS_EXPORT_CSV("CSV Data", "CSV डाटा", "CSV डाटा"),
    ,
    SETTINGS_EXPORT_CSV_DESC("Spreadsheet format", "स्प्रेडसिट ढाँचा", "स्प्रेडसिट ढाँचा"),

    ,

    // Preferences
    SETTINGS_HOUSE_SYSTEM("House System", "भाव पद्धति", "भाव पद्धति"),
    ,
    SETTINGS_AYANAMSA("Ayanamsa", "अयनांश", "अयनांश"),
    ,
    SETTINGS_LANGUAGE("Language", "भाषा", "भाषा"),
    ,
    SETTINGS_DATE_SYSTEM("Date System", "मिति प्रणाली", "मिति प्रणाली"),
    ,
    SETTINGS_THEME("Theme", "थिम", "थिम"),
    ,
    SETTINGS_NODE_CALCULATION("Node Calculation", "राहु-केतु गणना", "राहु-केतु गणना"),
    ,
    SETTINGS_NODE_MEAN("Mean Node", "मध्यम राहु", "मध्यम राहु"),
    ,
    SETTINGS_NODE_TRUE("True Node", "स्पष्ट राहु", "स्पष्ट राहु"),

    ,

    // Theme options
    THEME_LIGHT("Light", "उज्यालो", "उज्यालो"),
    ,
    THEME_LIGHT_DESC("Always use light theme", "सधैं उज्यालो थिम प्रयोग गर्नुहोस्", "सधैं उज्यालो थिम प्रयहग करें"),
    ,
    THEME_DARK("Dark", "अँध्यारो", "अँध्यारो"),
    ,
    THEME_DARK_DESC("Always use dark theme", "सधैं अँध्यारो थिम प्रयोग गर्नुहोस्", "सधैं अँध्यारो थिम प्रयहग करें"),
    ,
    THEME_SYSTEM("System", "प्रणाली", "प्रणाली"),
    ,
    THEME_SYSTEM_DESC("Follow device settings", "यन्त्र सेटिङ्स पछ्याउनुहोस्", "यन्त्र सेटिङ्स पहै्याउनुहोस्"),

    ,

    // About section
    SETTINGS_ABOUT_APP("About AstroStorm", "AstroStorm बारेमा", "AstroStorm बारे में"),
    ,
    SETTINGS_VERSION("Version %s", "संस्करण %s", "संस्करण %s"),
    ,
    SETTINGS_CALC_ENGINE("Calculation Engine", "गणना इन्जिन", "गणना इन्जिन"),
    ,
    SETTINGS_CALC_ENGINE_DESC("Swiss Ephemeris (JPL Mode)", "स्विस ईफेमेरिस (JPL मोड)", "स्विस ईफेमेरिस (JPL मोड)"),
    ,
    SETTINGS_APP_TAGLINE("Ultra-Precision Vedic Astrology", "अति-सटीक वैदिक ज्योतिष", "अति-सटीक वैदिक ज्यहतिष"),
    ,
    SETTINGS_APP_DESC("Powered by Swiss Ephemeris with JPL planetary data for astronomical-grade accuracy in all calculations.", "सबै गणनाहरूमा खगोलीय-ग्रेड सटीकताको लागि JPL ग्रह डाटासहित स्विस ईफेमेरिसद्वारा संचालित।", "सभी गणना में खगोलीय-ग्रेड सटीकता के लिए JPL ग्रह डाटासहित स्विस ईफेमेरिसद्वारा संचालित।"),
    ,
    SETTINGS_LAHIRI("Lahiri Ayanamsa", "लहिरी अयनांश", "लहिरी अयनांश"),
    ,
    SETTINGS_PLACIDUS("Placidus Houses", "प्लासिडस भावहरू", "प्लासिडस भाव"),

    ,

    // Delete dialog
    DIALOG_DELETE_PROFILE("Delete Profile", "प्रोफाइल मेट्नुहोस्", "प्रोफाइल मिटानेुहोस्"),
    ,
    DIALOG_DELETE_CONFIRM("Are you sure you want to delete %s? This action cannot be undone.", "के तपाईं %s मेट्न चाहनुहुन्छ? यो कार्य पूर्ववत गर्न सकिँदैन।", "के आप %s मिटाने चाहनुहोता है? यह कार्य पूर्ववत करने नहीं जा सकता।"),
    ,
    DIALOG_EXPORT_CHART("Export Chart", "कुण्डली निर्यात गर्नुहोस्", "कुंडली निर्यात करें"),

    ,

    // Chart detail labels
    CHART_ASCENDANT("Ascendant", "लग्न", "लग्न"),
    ,
    CHART_MOON_SIGN("Moon Sign", "चन्द्र राशि", "चन्द्र राशि"),
    ,
    CHART_NAKSHATRA("Nakshatra", "नक्षत्र", "नक्षत्र"),
    ,
    MISC_INFO("Information", "जानकारी", "जानकारी"),

    ,

    // ============================================
    // CHART INPUT SCREEN
    // ============================================
    INPUT_NEW_CHART("New Birth Chart", "नयाँ जन्म कुण्डली", "नयाँ जन्म कुंडली"),
    ,
    INPUT_EDIT_CHART("Edit Birth Chart", "जन्म कुण्डली सम्पादन", "जन्म कुंडली सम्पादन"),
    ,
    INPUT_IDENTITY("Identity", "पहिचान", "पहिचान"),
    ,
    INPUT_DATE_TIME("Date & Time", "मिति र समय", "मिति और समय"),
    ,
    INPUT_COORDINATES("Coordinates", "निर्देशांकहरू", "निर्देशांक"),

    ,

    INPUT_FULL_NAME("Full name", "पूरा नाम", "पूरा नाम"),
    ,
    INPUT_GENDER("Gender", "लिङ्ग", "लिंग"),
    ,
    INPUT_LOCATION("Location", "स्थान", "स्थान"),
    ,
    INPUT_SEARCH_LOCATION("Search city or enter manually", "शहर खोज्नुहोस् वा म्यानुअल रूपमा प्रविष्ट गर्नुहोस्", "शहर खोज्नुहोस् या म्यानुअल रूप में प्रविष्ट करें"),
    ,
    INPUT_TIMEZONE("Timezone", "समय क्षेत्र", "समय क्षेत्र"),
    ,
    INPUT_LATITUDE("Latitude", "अक्षांश", "अक्षांश"),
    ,
    INPUT_LONGITUDE("Longitude", "देशान्तर", "देशान्तर"),
    ,
    INPUT_ALTITUDE("Altitude (m) - Optional", "उचाई (मि.) - वैकल्पिक", "उचाई (मि.) - वैकल्पिक"),

    ,

    INPUT_SELECT_DATE("Select date", "मिति छान्नुहोस्", "मिति चुनें"),
    ,
    INPUT_SELECT_TIME("Select time", "समय छान्नुहोस्", "समय चुनें"),

    ,

    // Validation errors
    ERROR_INPUT("Input Error", "इनपुट त्रुटि", "इनपुट त्रुटि"),
    ,
    ERROR_INVALID_COORDS("Please enter valid latitude and longitude", "कृपया मान्य अक्षांश र देशान्तर प्रविष्ट गर्नुहोस्", "कृपया मान्य अक्षांश और देशान्तर प्रविष्ट करें"),
    ,
    ERROR_LATITUDE_RANGE("Latitude must be between -90 and 90", "अक्षांश -९० र ९० बीचमा हुनुपर्छ", "अक्षांश -९० और ९० बीच में हुनुपर्है"),
    ,
    ERROR_LONGITUDE_RANGE("Longitude must be between -180 and 180", "देशान्तर -१८० र १८० बीचमा हुनुपर्छ", "देशान्तर -१८० और १८० बीच में हुनुपर्है"),
    ,
    ERROR_CHECK_INPUT("Please check your input values", "कृपया आफ्नो इनपुट मानहरू जाँच गर्नुहोस्", "कृपया अपना इनपुट मान जाँच करें"),
    ,
    ERROR_CALCULATION_FAILED("Calculation failed", "गणना असफल भयो", "गणना असफल भयह"),
    ,
    ERROR_RATE_LIMIT("Too many requests. Please wait.", "धेरै अनुरोधहरू। कृपया पर्खनुहोस्।", "धेरै अनुरोध। कृपया पर्खनुहोस्।"),
    ,
    ERROR_SEARCH_FAILED("Search failed. Please try again.", "खोज असफल भयो। कृपया फेरि प्रयास गर्नुहोस्।", "खोज असफल भयह। कृपया फेरि प्रयास करें।"),
    ,
    ERROR_RATE_LIMIT_EXCEEDED("Rate limit exceeded", "दर सीमा नाघ्यो", "दर सी में नाघ्यह"),
    ,
    ERROR_NAME_TOO_LONG("Name must be 100 characters or less", "नाम १०० वर्ण वा कम हुनुपर्छ", "नाम १०० वर्ण या कम हुनुपर्है"),
    ,
    ERROR_DATE_IN_FUTURE("Birth date cannot be in the future", "जन्म मिति भविष्यमा हुन सक्दैन", "जन्म मिति भविष्य में हुन सक्दैन"),
    ,
    ERROR_DATE_TOO_OLD("Date must be after year 1800", "मिति वर्ष १८०० पछिको हुनुपर्छ", "मिति वर्ष १८०० पहैि के हुनुपर्है"),
    ,
    ERROR_LOCATION_REQUIRED("Please enter a location or coordinates", "कृपया स्थान वा निर्देशांक प्रविष्ट गर्नुहोस्", "कृपया स्थान या निर्देशांक प्रविष्ट करें"),
    ,
    ERROR_TIMEZONE_INVALID("Please select a valid timezone", "कृपया मान्य समय क्षेत्र छान्नुहोस्", "कृपया मान्य समय क्षेत्र चुनें"),

    ,

    // Location Search
    LOCATION_SEARCH("Search location", "स्थान खोज्नुहोस्", "स्थान खोज्नुहोस्"),
    ,
    LOCATION_PLACEHOLDER("Enter city or place name", "शहर वा ठाउँको नाम प्रविष्ट गर्नुहोस्", "शहर या ठाउँ के नाम प्रविष्ट करें"),
    ,
    LOCATION_CLEAR("Clear search", "खोज हटाउनुहोस्", "खोज हटाएं"),
    ,
    LOCATION_SELECT("Select %s", "%s छान्नुहोस्", "%s चुनें"),

    ,

    // ============================================
    // PROFILE EDIT SCREEN
    // ============================================
    EDIT_PROFILE_TITLE("Edit Profile", "प्रोफाइल सम्पादन", "प्रोफाइल सम्पादन"),
    ,
    EDIT_PROFILE_NO_DATA("No chart data available. Please select a profile to edit.", "कुनै कुण्डली डाटा उपलब्ध छैन। कृपया सम्पादन गर्न प्रोफाइल छान्नुहोस्।", "कुनै कुंडली डाटा उपलब्ध हैैन। कृपया सम्पादन करने प्रोफाइल चुनें।"),

    ,

    // ============================================
    // GENDER OPTIONS
    // ============================================
    GENDER_MALE("Male", "पुरुष", "पुरुष"),
    ,
    GENDER_FEMALE("Female", "महिला", "महिला"),
    ,
    GENDER_OTHER("Other", "अन्य", "अन्य"),

    ,

    // ============================================
    // GENERIC ASTROLOGICAL TERMS
    // ============================================
    CHART_HOUSE("House", "भाव", "भाव"),
    ,
    NAKSHATRA_PADA("Pada", "पद", "पद"),
    ,
    PLANET_RETROGRADE("Retrograde", "वक्री", "वक्री"),
    ,
    PLANET_RETROGRADE_SHORT("(R)", "(व)", "(व)"),
    ,
    UNIT_RUPAS("rupas", "रुपास", "रुपास"),
    ,
    UNIT_DAYS("days", "दिन", "दिन"),
    ,
    UNIT_MONTHS("months", "महिना", "महिना"),
    ,
    UNIT_YEARS("years", "वर्ष", "वर्ष"),

    ,

    PLANET_IN_SIGN_ACCESSIBILITY("%1\$s in %2\$s", "%2\$sमा %1\$s", "%2\$s में %1\$s"),
    ,
    NAKSHATRA_PADA_ACCESSIBILITY("%1\$s pada %2\$s", "%1\$s पद %2\$s", "%1\$s पद %2\$s"),

    ,

    // ============================================
    // PLANETS
    // ============================================
    PLANET("Planet", "ग्रह", "ग्रह"),
    ,
    PLANET_SUN("Sun", "सूर्य", "सूर्य"),
    ,
    PLANET_MOON("Moon", "चन्द्र", "चंद्र"),
    ,
    PLANET_MERCURY("Mercury", "बुध", "बुध"),
    ,
    PLANET_VENUS("Venus", "शुक्र", "शुक्र"),
    ,
    PLANET_MARS("Mars", "मंगल", "मंगल"),
    ,
    PLANET_JUPITER("Jupiter", "बृहस्पति", "बृहस्पति"),
    ,
    PLANET_SATURN("Saturn", "शनि", "शनि"),
    ,
    PLANET_RAHU("Rahu", "राहु", "राहु"),
    ,
    PLANET_KETU("Ketu", "केतु", "केतु"),
    ,
    PLANET_URANUS("Uranus", "युरेनस", "युरेनस"),
    ,
    PLANET_NEPTUNE("Neptune", "नेप्च्युन", "नेप्च्युन"),
    ,
    PLANET_PLUTO("Pluto", "प्लुटो", "प्लुटो"),

    ,

    // ============================================
    // ZODIAC SIGNS
    // ============================================
    SIGN_ARIES("Aries", "मेष", "मेष"),
    ,
    SIGN_TAURUS("Taurus", "वृष", "वृष"),
    ,
    SIGN_GEMINI("Gemini", "मिथुन", "मिथुन"),
    ,
    SIGN_CANCER("Cancer", "कर्कट", "कर्कट"),
    ,
    SIGN_LEO("Leo", "सिंह", "सिंह"),
    ,
    SIGN_VIRGO("Virgo", "कन्या", "कन्या"),
    ,
    SIGN_LIBRA("Libra", "तुला", "तुला"),
    ,
    SIGN_SCORPIO("Scorpio", "वृश्चिक", "वृश्चिक"),
    ,
    SIGN_SAGITTARIUS("Sagittarius", "धनु", "धनु"),
    ,
    SIGN_CAPRICORN("Capricorn", "मकर", "मकर"),
    ,
    SIGN_AQUARIUS("Aquarius", "कुम्भ", "कुम्भ"),
    ,
    SIGN_PISCES("Pisces", "मीन", "मीन"),

    ,

    // ============================================
    // NAKSHATRAS
    // ============================================
    NAKSHATRA_ASHWINI("Ashwini", "अश्विनी", "अश्विनी"),
    ,
    NAKSHATRA_BHARANI("Bharani", "भरणी", "भरणी"),
    ,
    NAKSHATRA_KRITTIKA("Krittika", "कृत्तिका", "कृत्ति के"),
    ,
    NAKSHATRA_ROHINI("Rohini", "रोहिणी", "रोहिणी"),
    ,
    NAKSHATRA_MRIGASHIRA("Mrigashira", "मृगशिरा", "मृगशिरा"),
    ,
    NAKSHATRA_ARDRA("Ardra", "आर्द्रा", "आर्द्रा"),
    ,
    NAKSHATRA_PUNARVASU("Punarvasu", "पुनर्वसु", "पुनर्वसु"),
    ,
    NAKSHATRA_PUSHYA("Pushya", "पुष्य", "पुष्य"),
    ,
    NAKSHATRA_ASHLESHA("Ashlesha", "आश्लेषा", "आश्लेषा"),
    ,
    NAKSHATRA_MAGHA("Magha", "मघा", "मघा"),
    ,
    NAKSHATRA_PURVA_PHALGUNI("Purva Phalguni", "पूर्वा फाल्गुनी", "पूर्वा फाल्गुनी"),
    ,
    NAKSHATRA_UTTARA_PHALGUNI("Uttara Phalguni", "उत्तरा फाल्गुनी", "उत्तरा फाल्गुनी"),
    ,
    NAKSHATRA_HASTA("Hasta", "हस्त", "हस्त"),
    ,
    NAKSHATRA_CHITRA("Chitra", "चित्रा", "चित्रा"),
    ,
    NAKSHATRA_SWATI("Swati", "स्वाति", "स्वाति"),
    ,
    NAKSHATRA_VISHAKHA("Vishakha", "विशाखा", "विशाखा"),
    ,
    NAKSHATRA_ANURADHA("Anuradha", "अनुराधा", "अनुराधा"),
    ,
    NAKSHATRA_JYESHTHA("Jyeshtha", "ज्येष्ठा", "ज्येष्ठा"),
    ,
    NAKSHATRA_MULA("Mula", "मूल", "मूल"),
    ,
    NAKSHATRA_PURVA_ASHADHA("Purva Ashadha", "पूर्वाषाढा", "पूर्वाषाढा"),
    ,
    NAKSHATRA_UTTARA_ASHADHA("Uttara Ashadha", "उत्तराषाढा", "उत्तराषाढा"),
    ,
    NAKSHATRA_SHRAVANA("Shravana", "श्रवण", "श्रवण"),
    ,
    NAKSHATRA_DHANISHTHA("Dhanishtha", "धनिष्ठा", "धनिष्ठा"),
    ,
    NAKSHATRA_SHATABHISHA("Shatabhisha", "शतभिषा", "शतभिषा"),
    ,
    NAKSHATRA_PURVA_BHADRAPADA("Purva Bhadrapada", "पूर्वभाद्रपद", "पूर्वभाद्रपद"),
    ,
    NAKSHATRA_UTTARA_BHADRAPADA("Uttara Bhadrapada", "उत्तरभाद्रपद", "उत्तरभाद्रपद"),
    ,
    NAKSHATRA_REVATI("Revati", "रेवती", "रेवती"),

    ,

    // ============================================
    // HOUSE SYSTEMS
    // ============================================
    HOUSE_PLACIDUS("Placidus", "प्लासिडस", "प्लासिडस"),
    ,
    HOUSE_KOCH("Koch", "कोच", "कोच"),
    ,
    HOUSE_PORPHYRIUS("Porphyrius", "पोर्फिरियस", "पोर्फिरिइस"),
    ,
    HOUSE_REGIOMONTANUS("Regiomontanus", "रेजिओमोन्टानस", "रेजिओमोन्टानस"),
    ,
    HOUSE_CAMPANUS("Campanus", "क्याम्पानस", "क्याम्पानस"),
    ,
    HOUSE_EQUAL("Equal", "समान", "समान"),
    ,
    HOUSE_WHOLE_SIGN("Whole Sign", "सम्पूर्ण राशि", "संपूर्ण राशि"),
    ,
    HOUSE_VEHLOW("Vehlow", "भेहलो", "भेहलो"),
    ,
    HOUSE_MERIDIAN("Meridian", "मेरिडियन", "मेरिडियन"),
    ,
    HOUSE_MORINUS("Morinus", "मोरिनस", "मोरिनस"),
    ,
    HOUSE_ALCABITUS("Alcabitus", "अल्काबिटस", "अल्काबिटस"),

    ,

    // ============================================
    // AYANAMSA OPTIONS
    // ============================================
    AYANAMSA_LAHIRI("Lahiri", "लहिरी", "लहिरी"),
    ,
    AYANAMSA_RAMAN("Raman", "रमण", "रमण"),
    ,
    AYANAMSA_KRISHNAMURTI("Krishnamurti", "कृष्णमूर्ति", "कृष्णमूर्ति"),
    ,
    AYANAMSA_TRUE_CHITRAPAKSHA("True Chitrapaksha", "सत्य चित्रपक्ष", "सत्य चित्रपक्ष"),

    ,

    // ============================================
    // YOGA ANALYSIS
    // ============================================
    YOGA_ANALYSIS_SUMMARY("Yoga Analysis Summary", "योग विश्लेषण सारांश", "यहग विश्लेषण सारांश"),
    ,
    YOGA_OVERALL_STRENGTH("Overall Yoga Strength", "समग्र योग बल", "समग्र यहग बल"),
    ,
    YOGA_TOTAL("Total Yogas", "कुल योगहरू", "कुल यहग"),
    ,
    YOGA_AUSPICIOUS("Auspicious", "शुभ", "शुभ"),
    ,
    YOGA_CHALLENGING("Challenging", "चुनौतीपूर्ण", "चुनौतीपूर्ण"),
    ,
    YOGA_ALL("All", "सबै", "सभी"),
    ,
    YOGA_COUNT_DETECTED("%d yogas detected", "%d योगहरू पत्ता लागेको", "%d यहग पत्ता लागे के"),
    ,
    YOGA_INFORMATION("Yoga Information", "योग जानकारी", "यहग जानकारी"),
    ,
    YOGA_ABOUT_TITLE("About Vedic Yogas", "वैदिक योगहरूको बारेमा", "वैदिक यहग के बारे में"),
    ,
    YOGA_ABOUT_DESCRIPTION("Yogas are special planetary combinations in Vedic astrology that indicate specific life patterns, talents, and karmic influences.", "योगहरू वैदिक ज्योतिषमा विशेष ग्रह संयोजनहरू हुन् जसले विशिष्ट जीवन ढाँचा, प्रतिभा र कर्म प्रभावहरू संकेत गर्छन्।", "यहग वैदिक ज्यहतिष में विशेष ग्रह संयहजन हैं जसले विशिष्ट जीवन ढाँचा, प्रतिभा और कर्म प्रभाव संकेत करता हैन्।"),
    ,
    YOGA_CATEGORIES_TITLE("Categories", "वर्गहरू", "वर्ग"),
    ,
    YOGA_GOT_IT("Got it", "बुझें", "बुझें"),
    ,
    YOGA_NO_DATA("No yoga data available", "कुनै योग डाटा उपलब्ध छैन", "कुनै यहग डाटा उपलब्ध हैैन"),
    ,
    YOGA_NO_CHART_MESSAGE("Select or create a birth profile to view yogas.", "योगहरू हेर्न जन्म प्रोफाइल छान्नुहोस् वा बनाउनुहोस्।", "यहग हेर्न जन्म प्रोफाइल चुनें या बनाएं।"),
    ,
    YOGAS_COUNT_DETECTED("%1\$d yogas detected in %2\$s", "%2\$s मा %1\$d योगहरू पत्ता लागेको", "%2\$s में %1\$d यहग पत्ता लागे के"),

    ,

    // Yoga Categories
    YOGA_CATEGORY_WEALTH("Wealth Yogas", "धन योगहरू", "धन यहग"),
    ,
    YOGA_CATEGORY_WEALTH_DESC("Combinations for prosperity", "समृद्धिका लागि संयोजनहरू", "समृद्धि के लिए संयहजन"),
    ,
    YOGA_CATEGORY_RAJA("Raja Yogas", "राज योगहरू", "राज यहग"),
    ,
    YOGA_CATEGORY_RAJA_DESC("Combinations for power & authority", "शक्ति र अधिकारका लागि संयोजनहरू", "शक्ति और अधिकार के लिए संयहजन"),
    ,
    YOGA_CATEGORY_SPIRITUAL("Spiritual Yogas", "आध्यात्मिक योगहरू", "आध्यात्मिक यहग"),
    ,
    YOGA_CATEGORY_SPIRITUAL_DESC("Combinations for spiritual growth", "आध्यात्मिक वृद्धिका लागि संयोजनहरू", "आध्यात्मिक वृद्धि के लिए संयहजन"),
    ,
    YOGA_CATEGORY_CHALLENGING("Challenging Yogas", "चुनौतीपूर्ण योगहरू", "चुनौतीपूर्ण यहग"),
    ,
    YOGA_CATEGORY_CHALLENGING_DESC("Combinations indicating obstacles", "बाधाहरू संकेत गर्ने संयोजनहरू", "बाधा संकेत करनेे संयहजन"),
    ,
    YOGA_CATEGORY_OTHER("Other Yogas", "अन्य योगहरू", "अन्य यहग"),
    ,
    YOGA_CATEGORY_OTHER_DESC("Other planetary combinations", "अन्य ग्रह संयोजनहरू", "अन्य ग्रह संयहजन"),
    ,
    YOGA_CATEGORY_DHANA("Dhana Yogas", "धन योगहरू", "धन यहग"),
    ,
    YOGA_CATEGORY_DHANA_DESC("Combinations for wealth", "धनका लागि संयोजनहरू", "धन के लिए संयहजन"),
    ,
    YOGA_CATEGORY_MAHAPURUSHA("Mahapurusha Yogas", "महापुरुष योगहरू", "महापुरुष यहग"),
    ,
    YOGA_CATEGORY_MAHAPURUSHA_DESC("Great personality combinations", "महान व्यक्तित्व संयोजनहरू", "महान व्यक्तित्व संयहजन"),
    ,
    YOGA_CATEGORY_NABHASA("Nabhasa Yogas", "नाभस योगहरू", "नाभस यहग"),
    ,
    YOGA_CATEGORY_NABHASA_DESC("Celestial combinations", "आकाशीय संयोजनहरू", "आकाशीय संयहजन"),
    ,
    YOGA_CATEGORY_CHANDRA("Chandra Yogas", "चन्द्र योगहरू", "चन्द्र यहग"),
    ,
    YOGA_CATEGORY_CHANDRA_DESC("Moon-based combinations", "चन्द्रमामा आधारित संयोजनहरू", "चन्द्रमा में आधारित संयहजन"),
    ,
    YOGA_CATEGORY_SOLAR("Solar Yogas", "सूर्य योगहरू", "सूर्य यहग"),
    ,
    YOGA_CATEGORY_SOLAR_DESC("Sun-based combinations", "सूर्यमा आधारित संयोजनहरू", "सूर्य में आधारित संयहजन"),
    ,
    YOGA_CATEGORY_SPECIAL("Special Yogas", "विशेष योगहरू", "विशेष यहग"),
    ,
    YOGA_CATEGORY_SPECIAL_DESC("Rare and special combinations", "दुर्लभ र विशेष संयोजनहरू", "दुर्लभ और विशेष संयहजन"),
    ,
    YOGA_CATEGORY_NEGATIVE("Negative Yogas", "नकारात्मक योगहरू", "नकारात्मक यहग"),
    ,
    YOGA_CATEGORY_NEGATIVE_DESC("Combinations indicating challenges", "चुनौतीहरू संकेत गर्ने संयोजनहरू", "चुनौती संकेत करनेे संयहजन"),

    ,

    // Singular Yoga Categories (for chips/labels)
    YOGA_CAT_RAJA("Raja Yoga", "राज योग", "राज यहग"),
    ,
    YOGA_CAT_DHANA("Dhana Yoga", "धन योग", "धन यहग"),
    ,
    YOGA_CAT_MAHAPURUSHA("Mahapurusha Yoga", "महापुरुष योग", "महापुरुष यहग"),
    ,
    YOGA_CAT_NABHASA("Nabhasa Yoga", "नाभस योग", "नाभस यहग"),
    ,
    YOGA_CAT_CHANDRA("Chandra Yoga", "चन्द्र योग", "चन्द्र यहग"),
    ,
    YOGA_CAT_SOLAR("Solar Yoga", "सूर्य योग", "सूर्य यहग"),
    ,
    YOGA_CAT_NEGATIVE("Negative Yoga", "नकारात्मक योग", "नकारात्मक यहग"),
    ,
    YOGA_CAT_SPECIAL("Special Yoga", "विशेष योग", "विशेष यहग"),

    ,

    // Yoga Strength Levels
    YOGA_STRENGTH_EXTREMELY_STRONG("Extremely Strong", "अत्यन्त बलियो", "अत्यन्त बलियह"),
    ,
    YOGA_STRENGTH_VERY_STRONG("Very Strong", "धेरै बलियो", "धेरै बलियह"),
    ,
    YOGA_STRENGTH_STRONG("Strong", "बलियो", "बलियह"),
    ,
    YOGA_STRENGTH_MODERATE("Moderate", "मध्यम", "मध्यम"),
    ,
    YOGA_STRENGTH_WEAK("Weak", "कमजोर", "कमजोर"),
    ,
    YOGA_STRENGTH_VERY_WEAK("Very Weak", "धेरै कमजोर", "धेरै कमजोर"),

    ,

    // Yoga Tab Content UI Strings
    YOGA_MOST_SIGNIFICANT("Most Significant Yogas", "सबैभन्दा महत्त्वपूर्ण योगहरू", "सभीभन्दा महत्त्वपूर्ण यहग"),
    ,
    YOGA_SANSKRIT("Sanskrit", "संस्कृत", "संस्कृत"),
    ,
    YOGA_EFFECTS("Effects", "प्रभावहरू", "प्रभाव"),
    ,
    YOGA_ACTIVATION("Activation", "सक्रियता", "सक्रियता"),
    ,
    YOGA_CANCELLATION_FACTORS("Cancellation/Mitigation Factors", "रद्द/न्यूनीकरण कारकहरू", "रद्द/न्यूनीकरण कारक"),
    ,
    YOGA_NO_CATEGORY_FOUND("No %s found", "कुनै %s फेला परेन", "कुनै %s फेला परेन"),
    ,
    YOGA_NONE_DETECTED("No yogas detected", "कुनै योग पत्ता लागेन", "कुनै यहग पत्ता लागेन"),
    ,
    YOGA_HOUSE_PREFIX("H", "भाव", "भाव"),
    ,
    YOGA_SUBTITLE("Planetary combinations in your chart", "तपाईंको कुण्डलीमा ग्रह संयोजनहरू", "आप के कुंडली में ग्रह संयहजन"),
    ,
    YOGA_STRENGTH("Strength", "बल", "बल"),
    ,
    YOGA_DOMINANT_CATEGORY("Dominant Category", "प्रमुख वर्ग", "प्रमुख वर्ग"),
    ,
    YOGA_FILTER_BY_CATEGORY("Filter by Category", "वर्गअनुसार फिल्टर गर्नुहोस्", "वर्गअनुसार फिल्टर करें"),
    ,
    YOGA_COUNT_SUFFIX("yogas", "योगहरू", "यहग"),
    ,
    YOGA_PLANETS_INVOLVED("Planets Involved", "संलग्न ग्रहहरू", "संलग्न ग्रह"),
    ,
    YOGA_HOUSES_LABEL("Houses:", "भावहरू:", "भाव:"),
    ,
    YOGA_STRENGTH_LABEL("Yoga Strength", "योग बल", "यहग बल"),
    ,
    YOGA_ACTIVATION_LABEL("Activation:", "सक्रियता:", "सक्रियता:"),
    ,
    YOGA_NO_YOGAS_FOUND("No Yogas Found", "कुनै योग फेला परेन", "कुनै यहग फेला परेन"),

    ,

    // ============================================
    // PROFILE SWITCHER
    // ============================================
    PROFILE_SWITCH("Switch Profile", "प्रोफाइल बदल्नुहोस्", "प्रोफाइल बदल्नुहोस्"),
    ,
    PROFILE_ADD_NEW("Add New Profile", "नयाँ प्रोफाइल थप्नुहोस्", "नयाँ प्रोफाइल जोड़ें"),
    ,
    PROFILE_ADD_NEW_CHART("Add new chart", "नयाँ कुण्डली थप्नुहोस्", "नयाँ कुंडली जोड़ें"),
    ,
    PROFILE_CURRENT("Current", "हालको", "हाल के"),
    ,
    PROFILE_NO_SAVED_CHARTS("No saved charts", "कुनै सुरक्षित कुण्डली छैन", "कुनै सुरक्षित कुंडली हैैन"),
    ,
    PROFILE_ADD_FIRST_CHART("Add your first chart to get started", "सुरु गर्न आफ्नो पहिलो कुण्डली थप्नुहोस्", "सुरु करने अपना पहिलो कुंडली जोड़ें"),
    ,
    PROFILE_SELECTED("selected", "छानिएको", "हैानिए के"),
    ,
    PROFILE_SELECT("Select Profile", "प्रोफाइल छान्नुहोस्", "प्रोफाइल चुनें"),
    ,
    PROFILE_CURRENT_A11Y("Current profile: %s. Tap to switch profiles", "हालको प्रोफाइल: %s। प्रोफाइलहरू बदल्न ट्याप गर्नुहोस्", "हाल के प्रोफाइल: %s। प्रोफाइल बदल्न ट्याप करें"),
    ,
    PROFILE_NO_SELECTED_A11Y("No profile selected. Tap to select a profile", "कुनै प्रोफाइल छानिएको छैन। प्रोफाइल छान्न ट्याप गर्नुहोस्", "कुनै प्रोफाइल हैानिए के हैैन। प्रोफाइल हैान्न ट्याप करें"),
    ,
    PROFILE_BIRTH_CHART("Birth chart", "जन्म कुण्डली", "जन्म कुंडली"),
    ,
    PROFILE_DELETE_TITLE("Delete Birth Chart", "जन्म कुण्डली मेट्नुहोस्", "जन्म कुंडली मिटानेुहोस्"),
    ,
    PROFILE_DELETE_MESSAGE("Are you sure you want to delete \"{name}\"? This action cannot be undone and all associated data will be permanently removed.", "के तपाईं \"{name}\" मेट्न निश्चित हुनुहुन्छ? यो कार्य पूर्ववत गर्न सकिँदैन र सबै सम्बन्धित डाटा स्थायी रूपमा हटाइनेछ।", "के आप \"{name}\" मिटाने निश्चित हैं? यह कार्य पूर्ववत करने नहीं जा सकता और सभी संबंधित डाटा स्थायी रूप में हटा दिया जाएगा।"),
    ,
    PROFILE_EDIT_CHART("Edit Chart", "कुण्डली सम्पादन गर्नुहोस्", "कुंडली सम्पादन करें"),

    ,

    // ============================================
    // TRANSITS SCREEN
    // ============================================
    TRANSIT_CURRENT_MOVEMENTS("Current movements in %s", "%s मा हालको गति", "%s में हाल के गति"),
    ,
    TRANSIT_PLANET_POSITIONS("Current Positions", "हालको स्थितिहरू", "हाल के स्थिति"),
    ,
    TRANSIT_OVERVIEW("Transit Overview", "गोचर अवलोकन", "गोचर अवलोकन"),
    ,
    TRANSIT_CURRENT_INFLUENCES("Current influences on your chart", "तपाईंको कुण्डलीमा हालको प्रभावहरू", "आप के कुंडली में हाल के प्रभाव"),
    ,
    TRANSIT_PLANETS_COUNT("Planets Transiting", "गोचरमा ग्रहहरू", "गोचर में ग्रह"),
    ,
    TRANSIT_MAJOR_TRANSITS("Major Transits", "मुख्य गोचरहरू", "मुख्य गोचर"),
    ,
    TRANSIT_QUALITY_LABEL("Quality Score", "गुणस्तर स्कोर", "गुणस्तर स्कोर"),
    ,
    TRANSIT_OVERALL_ASSESSMENT("Overall Assessment", "समग्र मूल्यांकन", "समग्र मूल्यांकन"),
    ,
    TRANSIT_RETROGRADE_SYMBOL("Rx", "Rx", "Rx"),
    ,
    TRANSIT_HOUSE_LABEL("House", "भाव", "भाव"),
    ,
    TRANSIT_LABEL("Transit", "गोचर", "गोचर"),
    ,
    TRANSIT_NATAL_LABEL("Natal", "जन्म", "जन्म"),
    ,
    TRANSIT_NO_PLANETS_TRANSITING("No planets in this house", "यस भावमा कोनै ग्रह छैन", "इस भाव में कोनै ग्रह हैैन"),
    ,
    TRANSIT_UPCOMING("Upcoming Transits", "आगामी गोचरहरू", "आगामी गोचर"),
    ,
    TRANSIT_NO_UPCOMING("No upcoming significant transits", "कुनै आगामी महत्त्वपूर्ण गोचरहरू छैनन्", "कुनै आगामी महत्त्वपूर्ण गोचर हैैनन्"),
    ,
    TRANSIT_TO_NATAL_ASPECTS("Transit to Natal Aspects", "गोचर-जन्म पहलुहरू", "गोचर-जन्म पहलु"),
    ,
    TRANSIT_NO_ASPECTS("No transit aspects at this time", "यस समयमा कुनै गोचर पहलुहरू छैनन्", "इस समय में कुनै गोचर पहलु हैैनन्"),
    ,
    TRANSIT_NO_DATA("No Transit Data", "कुनै गोचर डाटा छैन", "कुनै गोचर डाटा हैैन"),
    ,
    TRANSIT_SELECT_CHART("Select a chart to view transits", "गोचरहरू हेर्न कुण्डली छान्नुहोस्", "गोचर हेर्न कुंडली चुनें"),

    ,

    // ============================================
    // ONBOARDING
    // ============================================
    ONBOARDING_WELCOME_TITLE("Welcome to AstroStorm", "AstroStorm मा स्वागत छ", "AstroStorm में स्वागत है"),
    ,
    ONBOARDING_WELCOME_SUBTITLE("Your personal Vedic astrology companion", "तपाईंको व्यक्तिगत वैदिक ज्योतिष साथी", "आप के व्यक्तिगत वैदिक ज्यहतिष मित्र"),
    ,
    ONBOARDING_WELCOME_DESC("Discover the ancient wisdom of Vedic astrology with precision calculations and personalized insights.", "सटीक गणना र व्यक्तिगत अन्तर्दृष्टिका साथ वैदिक ज्योतिषको प्राचीन ज्ञान पत्ता लगाउनुहोस्।", "सटीक गणना और व्यक्तिगत अंतर्दृष्टि के साथ वैदिक ज्यहतिष के प्राचीन ज्ञान पत्ता लगाउनुहोस्।"),

    ,

    ONBOARDING_TAGLINE("Precision Vedic Astrology", "सटीक वैदिक ज्योतिष", "सटीक वैदिक ज्यहतिष"),
    ,
    ONBOARDING_FEATURES_OVERVIEW_TITLE("What AstroStorm Offers", "AstroStorm ले के प्रदान गर्दछ", "AstroStorm ले के प्रदान करता है"),

    ,

    ONBOARDING_FEATURE_CAT_CHART("Complete Chart Analysis", "पूर्ण कुण्डली विश्लेषण", "पूर्ण कुंडली विश्लेषण"),
    ,
    ONBOARDING_FEATURE_CAT_CHART_ITEMS("Rashi & Divisional Charts • Shadbala & Ashtakavarga • All 16 Vargas", "राशि र विभागीय कुण्डलीहरू • षड्बल र अष्टकवर्ग • सबै १६ वर्गहरू", "राशि और विभागीय कुंडली • षड्बल और अष्टकवर्ग • सभी १६ वर्ग"),

    ,

    ONBOARDING_FEATURE_CAT_DASHA("Dasha Systems", "दशा प्रणालीहरू", "दशा प्रणाली"),
    ,
    ONBOARDING_FEATURE_CAT_DASHA_ITEMS("Vimshottari & Yogini • Ashtottari & Chara • Kalachakra Dasha", "विम्शोत्तरी र योगिनी • अष्टोत्तरी र चर • कालचक्र दशा", "विम्शोत्तरी और यहगिनी • अष्टोत्तरी और चर • कालचक्र दशा"),

    ,

    ONBOARDING_FEATURE_CAT_PREDICTION("Predictions & Yogas", "भविष्यवाणी र योगहरू", "भविष्यवाणी और यहग"),
    ,
    ONBOARDING_FEATURE_CAT_PREDICTION_ITEMS("200+ Yoga Calculations • Transit Analysis • Matchmaking (Kundali Milan)", "२००+ योग गणनाहरू • गोचर विश्लेषण • कुण्डली मिलान", "२००+ यहग गणना • गोचर विश्लेषण • कुंडली मिलान"),

    ,

    ONBOARDING_FEATURE_CAT_REMEDY("Remedies & Muhurta", "उपाय र मुहूर्त", "उपाय और मुहूर्त"),
    ,
    ONBOARDING_FEATURE_CAT_REMEDY_ITEMS("Vedic & Lal Kitab Remedies • Auspicious Timing • Panchanga", "वैदिक र लाल किताब उपायहरू • शुभ समय • पञ्चाङ्ग", "वैदिक और लाल किताब उपाय • शुभ समय • पंचांग"),

    ,

    ONBOARDING_QUICK_START_TITLE("You'll need:", "तपाईंलाई आवश्यक पर्नेछ:", "आप  के आवश्यक पर्नेहै:"),
    ,
    ONBOARDING_QUICK_START_1("Birth date & time", "जन्म मिति र समय", "जन्म मिति और समय"),
    ,
    ONBOARDING_QUICK_START_2("Birth location (city)", "जन्म स्थान (शहर)", "जन्म स्थान (शहर)"),
    ,
    ONBOARDING_QUICK_START_3("That's all!", "त्यति मात्र हो!", "त्यति मात्र है!"),

    ,

    ONBOARDING_LANGUAGE_TITLE("Choose Your Language", "आफ्नो भाषा छान्नुहोस्", "अपना भाषा चुनें"),
    ,
    ONBOARDING_LANGUAGE_SUBTITLE("Select your preferred language", "आफ्नो मनपर्ने भाषा छान्नुहोस्", "अपना मनपर्ने भाषा चुनें"),
    ,
    ONBOARDING_LANGUAGE_DESC("You can change this later in settings.", "तपाईं यसलाई पछि सेटिङ्समा परिवर्तन गर्न सक्नुहुन्छ।", "आप इस  के पहैि सेटिङ्स में परिवर्तन करने सक्नुहोता है।"),

    ,

    ONBOARDING_THEME_TITLE("Choose Your Theme", "आफ्नो थिम छान्नुहोस्", "अपना थिम चुनें"),
    ,
    ONBOARDING_THEME_SUBTITLE("Select your preferred appearance", "आफ्नो मनपर्ने रूप छान्नुहोस्", "अपना मनपर्ने रूप चुनें"),
    ,
    ONBOARDING_THEME_DESC("You can change this later in settings.", "तपाईं यसलाई पछि सेटिङ्समा परिवर्तन गर्न सक्नुहुन्छ।", "आप इस  के पहैि सेटिङ्स में परिवर्तन करने सक्नुहोता है।"),
    ,
    ONBOARDING_THEME_LIGHT("Light", "उज्यालो", "उज्यालो"),
    ,
    ONBOARDING_THEME_DARK("Dark", "अँध्यारो", "अँध्यारो"),
    ,
    ONBOARDING_THEME_SYSTEM("System", "प्रणाली", "प्रणाली"),

    ,

    ONBOARDING_FEATURES_TITLE("Powerful Features", "शक्तिशाली सुविधाहरू", "शक्तिशाली सुविधा"),
    ,
    ONBOARDING_FEATURES_SUBTITLE("Everything you need for Vedic astrology", "वैदिक ज्योतिषको लागि तपाईंलाई चाहिने सबै", "वैदिक ज्यहतिष के लिए आप  के चाहिने सभी"),

    ,

    ONBOARDING_FEATURE_CHARTS("Birth Charts", "जन्म कुण्डली", "जन्म कुंडली"),
    ,
    ONBOARDING_FEATURE_CHARTS_DESC("Accurate Vedic birth chart calculations", "सटीक वैदिक जन्म कुण्डली गणना", "सटीक वैदिक जन्म कुंडली गणना"),
    ,
    ONBOARDING_FEATURE_DASHAS("Dashas", "दशाहरू", "दशा"),
    ,
    ONBOARDING_FEATURE_DASHAS_DESC("Complete planetary period analysis", "पूर्ण ग्रह अवधि विश्लेषण", "पूर्ण ग्रह अवधि विश्लेषण"),
    ,
    ONBOARDING_FEATURE_TRANSITS("Transits", "गोचरहरू", "गोचर"),
    ,
    ONBOARDING_FEATURE_TRANSITS_DESC("Real-time planetary movements", "वास्तविक-समय ग्रह गतिविधि", "वास्तविक-समय ग्रह गतिविधि"),
    ,
    ONBOARDING_FEATURE_MATCHMAKING("Matchmaking", "कुण्डली मिलान", "कुंडली मिलान"),
    ,
    ONBOARDING_FEATURE_MATCHMAKING_DESC("Kundli Milan compatibility", "कुण्डली मिलान अनुकूलता", "कुंडली मिलान अनुकूलता"),

    ,

    ONBOARDING_READY_TITLE("You're All Set!", "तपाईं तयार हुनुहुन्छ!", "आप तयार हैं!"),
    ,
    ONBOARDING_READY_SUBTITLE("Start exploring your cosmic journey", "आफ्नो ब्रह्माण्डीय यात्रा अन्वेषण गर्न सुरु गर्नुहोस्", "अपना ब्रह्माण्डीय यात्रा अन्वेषण करने सुरु करें"),
    ,
    ONBOARDING_READY_DESC("Create your first birth chart and discover personalized astrological insights.", "आफ्नो पहिलो जन्म कुण्डली बनाउनुहोस् र व्यक्तिगत ज्योतिषीय अन्तर्दृष्टिहरू पत्ता लगाउनुहोस्।", "अपना पहिलो जन्म कुंडली बनाएं और व्यक्तिगत ज्यहतिषीय अंतर्दृष्टि पत्ता लगाउनुहोस्।"),
    ,
    ONBOARDING_CREATE_FIRST_CHART("Create Your First Chart", "तपाईंको पहिलो कुण्डली बनाउनुहोस्", "आप के पहिलो कुंडली बनाएं"),

    ,

    ONBOARDING_BTN_NEXT("Next", "अर्को", "अगला"),
    ,
    ONBOARDING_BTN_BACK("Back", "पछाडि", "पीछे"),
    ,
    ONBOARDING_BTN_GET_STARTED("Get Started", "सुरु गर्नुहोस्", "सुरु करें"),
    ,
    ONBOARDING_BTN_SKIP("Skip", "छोड्नुहोस्", "हैोड्नुहोस्"),

    ,

    // ============================================
    // COMMON TAB TITLES
    // ============================================
    TAB_OVERVIEW("Overview", "अवलोकन", "अवलोकन"),
    ,
    TAB_BY_PLANET("By Planet", "ग्रहानुसार", "ग्रहानुसार"),
    ,
    TAB_BY_HOUSE("By House", "भावानुसार", "भावानुसार"),
    ,
    TAB_ELEMENTS("Elements", "तत्वहरू", "तत्व"),
    ,
    TAB_TODAY("Today", "आज", "आज"),
    ,
    TAB_BIRTH_DAY("Birth Day", "जन्म दिन", "जन्म दिन"),
    ,
    TAB_CURRENT_POSITIONS("Current Positions", "हालको स्थिति", "हाल के स्थिति"),
    ,
    TAB_UPCOMING("Upcoming", "आगामी", "आगामी"),
    ,
    TAB_ASPECTS("Aspects", "दृष्टिहरू", "दृष्टि"),
    ,
    TAB_SARVASHTAKAVARGA("Sarvashtakavarga", "सर्वाष्टकवर्ग", "सर्वाष्टकवर्ग"),

    ,

    // ============================================
    // STRENGTH LABELS
    // ============================================
    STRENGTH_EXCELLENT("Excellent", "उत्कृष्ट", "उत्कृष्ट"),
    ,
    STRENGTH_STRONG("Strong", "बलियो", "बलियह"),
    ,
    STRENGTH_GOOD("Good", "राम्रो", "राम्रो"),
    ,
    STRENGTH_AVERAGE("Average", "औसत", "औसत"),
    ,
    STRENGTH_WEAK("Weak", "कमजोर", "कमजोर"),
    ,
    STRENGTH_BELOW_AVERAGE("Below Average", "औसतमुनि", "औसतमुनि"),

    ,

    // ============================================
    // ACCESSIBILITY STRINGS
    // ============================================
    A11Y_EXPAND("Expand", "विस्तार गर्नुहोस्", "विस्तार करें"),
    ,
    A11Y_COLLAPSE("Collapse", "संक्षिप्त गर्नुहोस्", "संक्षिप्त करें"),
    ,
    A11Y_NAVIGATE_BACK("Navigate back", "पछाडि जानुहोस्", "पीहैे जानुहोस्"),
    ,
    A11Y_SHOW_INFO("Show information", "जानकारी देखाउनुहोस्", "जानकारी देखाउनुहोस्"),
    ,
    LABEL_DASH("-", "-", "-"),

    ,

    // ============================================
    // ADDITIONAL ACCESSIBILITY STRINGS
    // ============================================
    ACC_FULLSCREEN("Fullscreen", "पूर्णस्क्रीन", "पूर्णस्क्रीन"),
    ,
    ACC_COLLAPSE("Collapse section", "खण्ड संक्षिप्त गर्नुहोस्", "खण्ड संक्षिप्त करें"),
    ,
    ACC_EXPAND("Expand section", "खण्ड विस्तार गर्नुहोस्", "खण्ड विस्तार करें"),
    ,
    ACC_VIEW_DETAILS("View details", "विवरण हेर्नुहोस्", "विवरण देखें"),

    ,

    // ============================================
    // DIGNITY STATUS STRINGS
    // ============================================
    DIGNITY_EXALTED_STATUS("Exalted", "उच्च", "उच्च"),
    ,
    DIGNITY_DEBILITATED_STATUS("Debilitated", "नीच", "नीच"),
    ,
    DIGNITY_OWN_SIGN_STATUS("Own Sign", "स्वगृह", "स्वगृह"),
    ,
    DIGNITY_NEUTRAL_STATUS("Neutral", "तटस्थ", "तटस्थ"),

    ,

    // ============================================
    // BUTTON & FEATURE STRINGS
    // ============================================
    BTN_VIEW_DETAILS("View Details", "विवरण हेर्नुहोस्", "विवरण देखें"),
    ,
    REPORT_PLANET("planet", "ग्रह", "ग्रह"),

    ,
    
    // ============================================
    // ERROR STRINGS
    // ============================================
    ERROR_CALCULATION("Calculation Error", "गणना त्रुटि", "गणना त्रुटि"),

    ,

    // ============================================
    // HOME TAB - REVAMPED UI
    // ============================================
    HOME_EXPLORE_FEATURES("Explore your chart features", "आफ्नो कुण्डली सुविधाहरू अन्वेषण गर्नुहोस्", "अपना कुंडली सुविधा अन्वेषण करें"),
    ,
    QUICK_ACTIONS("Quick Actions", "द्रुत कार्यहरू", "द्रुत कार्य"),
    ,
    TODAYS_SNAPSHOT("Today's Snapshot", "आजको झलक", "आज के झलक"),
    ,
    VIEW_CURRENT_TRANSITS("View current planetary positions", "हालको ग्रह स्थितिहरू हेर्नुहोस्", "हाल के ग्रह स्थिति देखें"),
    ,
    CURRENT_MAHA_DASHA("Current Maha Dasha", "हालको महा दशा", "हाल के महा दशा"),
    ,
    HOME_DASHA_LABEL("Dasha", "दशा", "दशा"),
    ,
    REMAINING_PERIOD_YEARS("%s years, %s months remaining", "%s वर्ष, %s महिना बाँकी", "%s वर्ष, %s महिना बाँकी"),
    ,
    REMAINING_PERIOD_MONTHS("%s months remaining", "%s महिना बाँकी", "%s महिना बाँकी"),
    ,
    REMAINING_PERIOD_DAYS("%s days remaining", "%s दिन बाँकी", "%s दिन बाँकी"),
    ,
    REMAINING_PERIOD_MONTHS_DAYS("%s months, %s days remaining", "%s महिना, %s दिन बाँकी", "%s महिना, %s दिन बाँकी"),
    ,
    DURATION_YEARS_FMT("%d years", "%d वर्ष", "%d वर्ष"),
    ,
    DURATION_YEAR_FMT("1 year", "१ वर्ष", "१ वर्ष"),
    ,
    TAP_TO_VIEW_DASHAS("Tap to view your Dasha timeline", "आफ्नो दशा समयरेखा हेर्न ट्याप गर्नुहोस्", "अपना दशा समयरेखा हेर्न ट्याप करें"),

    ,
    
    // Feature Categories
    CATEGORY_CORE_ANALYSIS("Core Analysis", "मुख्य विश्लेषण", "मुख्य विश्लेषण"),
    ,
    CATEGORY_CORE_DESC("Charts, planets, nakshatras & vargas", "कुण्डली, ग्रह, नक्षत्र र वर्गहरू", "कुंडली, ग्रह, नक्षत्र और वर्ग"),
    ,
    CATEGORY_TIMING_SYSTEMS("Timing Systems", "समय प्रणाली", "समय प्रणाली"),
    ,
    CATEGORY_TIMING_DESC("Dashas & planetary periods", "दशा र ग्रह अवधिहरू", "दशा और ग्रह अवधि"),
    ,
    CATEGORY_PREDICTIONS("Predictions & Yogas", "भविष्यवाणी र योग", "भविष्यवाणी और यहग"),
    ,
    CATEGORY_PREDICTIONS_DESC("Life predictions & combinations", "जीवन भविष्यवाणी र संयोजन", "जीवन भविष्यवाणी और संयहजन"),
    ,
    CATEGORY_STRENGTH_ANALYSIS("Strength Analysis", "बल विश्लेषण", "बल विश्लेषण"),
    ,
    CATEGORY_STRENGTH_DESC("Shadbala & planetary strength", "षड्बल र ग्रह बल", "षड्बल और ग्रह बल"),
    ,
    CATEGORY_ADVANCED("Advanced Techniques", "उन्नत प्रविधि", "उन्नत प्रविधि"),
    ,
    CATEGORY_ADVANCED_DESC("Jaimini, Nadi & specialized systems", "जैमिनी, नाडी र विशेष प्रणाली", "जैमिनी, नाडी और विशेष प्रणाली"),
    ,
    CATEGORY_REMEDIAL("Remedies & Compatibility", "उपाय र अनुकूलता", "उपाय और अनुकूलता"),
    ,
    CATEGORY_REMEDIAL_DESC("Remedies, matching & muhurta", "उपाय, मिलान र मुहूर्त", "उपाय, मिलान और मुहूर्त");

}
