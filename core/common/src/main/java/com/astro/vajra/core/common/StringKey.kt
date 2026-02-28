package com.astro.vajra.core.common



/**
 * Core UI string keys (navigation, settings, buttons, yoga, profile)
 * Part 1 of 4 split enums to avoid JVM method size limit
 */
enum class StringKey(override val en: String, override val ne: String) : StringKeyInterface {

    // ============================================
    // NAVIGATION & TABS
    // ============================================
    TAB_HOME("Home", "गृह"),
    TAB_INSIGHTS("Insights", "अन्तर्दृष्टि"),
    TAB_SETTINGS("Settings", "सेटिङ्स"),

    // ============================================
    // APP BRANDING
    // ============================================
    APP_NAME_KEY("AstroVajra", "एस्ट्रो-स्टोर्म"),

    // ============================================
    // HOME TAB - SECTION HEADERS
    // ============================================
    HOME_CHART_ANALYSIS("Chart Analysis", "कुण्डली विश्लेषण"),
    HOME_COMING_SOON("Coming Soon", "छिट्टै आउँदैछ"),
    HOME_SOON_BADGE("Soon", "छिट्टै"),

    // ============================================
    // HOME TAB - FEATURE CARDS
    // ============================================
    FEATURE_BIRTH_CHART("Birth Chart", "जन्म कुण्डली"),
    FEATURE_BIRTH_CHART_DESC("View your complete Vedic birth chart", "आफ्नो पूर्ण वैदिक जन्म कुण्डली हेर्नुहोस्"),

    FEATURE_PLANETS("Planets", "ग्रहहरू"),
    FEATURE_PLANETS_DESC("Detailed planetary positions", "विस्तृत ग्रह स्थिति"),

    FEATURE_YOGAS("Yogas", "योगहरू"),
    FEATURE_YOGAS_DESC("Planetary combinations & effects", "ग्रह संयोजन र प्रभावहरू"),

    FEATURE_DASHAS("Dashas", "दशाहरू"),
    FEATURE_DASHAS_DESC("Planetary period timeline", "ग्रह अवधि समयरेखा"),

    FEATURE_TRANSITS("Transits", "गोचरहरू"),
    FEATURE_TRANSITS_DESC("Current planetary movements", "हालको ग्रह गतिविधिहरू"),

    FEATURE_ASHTAKAVARGA("Ashtakavarga", "अष्टकवर्ग"),
    FEATURE_ASHTAKAVARGA_DESC("Strength analysis by house", "भावानुसार बल विश्लेषण"),

    // Ashtakavarga Details
    ASHTAKAVARGA_ABOUT_TITLE("About Ashtakavarga", "अष्टकवर्ग बारेमा"),
    ASHTAKAVARGA_ABOUT_DESC("Ashtakavarga is an ancient Vedic astrology technique for assessing planetary strength and predicting transit effects.", "अष्टकवर्ग वैदिक ज्योतिषको एक प्राचीन प्रविधि हो जसले ग्रहको शक्ति मापन र गोचर प्रभाव पूर्वानुमान गर्दछ।"),
    ASHTAKAVARGA_SAV_TITLE("Sarvashtakavarga (SAV)", "सर्वाष्टकवर्ग (SAV)"),
    ASHTAKAVARGA_SAV_DESC("Combined strength points from all planets in each zodiac sign. Higher values (28+) indicate favorable areas.", "सबै ग्रहबाट प्रत्येक राशिमा संयोजित शक्ति बिन्दु। उच्च मान (२८+) अनुकूल क्षेत्रहरू संकेत गर्छ।"),
    ASHTAKAVARGA_BAV_TITLE("Bhinnashtakavarga (BAV)", "भिन्नाष्टकवर्ग (BAV)"),
    ASHTAKAVARGA_BAV_DESC("Individual planet strength in each sign (0-8 bindus). Use this to predict transit effects.", "प्रत्येक राशिमा व्यक्तिगत ग्रहको शक्ति (०-८ बिन्दु)। गोचर प्रभाव पूर्वानुमान गर्न यो प्रयोग गर्नुहोस्।"),

    FEATURE_PANCHANGA("Panchanga", "पञ्चाङ्ग"),
    FEATURE_PANCHANGA_DESC("Vedic calendar elements", "वैदिक पात्रो तत्वहरू"),

    FEATURE_MATCHMAKING("Matchmaking", "कुण्डली मिलान"),
    FEATURE_MATCHMAKING_DESC("Kundli Milan compatibility", "विवाह मेलापक गुण मिलान"),

    FEATURE_MUHURTA("Muhurta", "मुहूर्त"),
    FEATURE_MUHURTA_DESC("Auspicious timing finder", "शुभ समय खोजकर्ता"),

    FEATURE_REMEDIES("Remedies", "उपायहरू"),
    FEATURE_REMEDIES_DESC("Personalized remedies", "व्यक्तिगत उपायहरू"),

    FEATURE_VARSHAPHALA("Varshaphala", "वर्षफल"),
    FEATURE_VARSHAPHALA_DESC("Solar return horoscope", "वार्षिक राशिफल"),

    // Varshaphala Details
    VARSHAPHALA_TAB_OVERVIEW("Overview", "अवलोकन"),
    VARSHAPHALA_TAB_TAJIKA("Tajika", "तजिका"),
    VARSHAPHALA_TAB_SAHAMS("Sahams", "सहम"),
    VARSHAPHALA_TAB_DASHA("Dasha", "दशा"),
    VARSHAPHALA_TAB_HOUSES("Houses", "भावहरू"),
    VARSHAPHALA_ANNUAL_HOROSCOPE("Annual Horoscope", "वार्षिक राशिफल"),
    VARSHAPHALA_AGE("Age %d", "आयु %d"),
    VARSHAPHALA_PREV_YEAR("Previous year", "अघिल्लो वर्ष"),
    VARSHAPHALA_NEXT_YEAR("Next year", "अर्को वर्ष"),
    VARSHAPHALA_SELECT_CHART("Select a birth chart to view Varshaphala", "वर्षफल हेर्न जन्म कुण्डली छान्नुहोस्"),
    VARSHAPHALA_COMPUTING("Computing Annual Horoscope...", "वार्षिक राशिफल गणना गर्दै..."),
    VARSHAPHALA_COMPUTING_DESC("Calculating Tajika aspects, Sahams & Mudda Dasha", "तजिका पक्ष, सहम र मुद्द दशा गणना गर्दै"),
    VARSHAPHALA_ERROR("Calculation Error", "गणना त्रुटि"),
    VARSHAPHALA_RESET_YEAR("Reset to Current Year", "वर्तमान वर्षमा रिसेट"),
    VARSHAPHALA_SOLAR_RETURN("Solar Return", "सौर प्रतिफल"),
    VARSHAPHALA_YEAR_LORD("Year Lord", "वर्ष स्वामी"),
    VARSHAPHALA_MUNTHA("Muntha", "मुन्थ"),
    VARSHAPHALA_MUNTHA_LORD("Lord: %s", "स्वामी: %s"),
    VARSHAPHALA_TAJIKA_CHART("Tajika Annual Chart", "तजिका वार्षिक कुण्डली"),
    VARSHAPHALA_PANCHA_VARGIYA("Pancha Vargiya Bala", "पञ्च वर्गीय बल"),
    VARSHAPHALA_TRI_PATAKI("Tri-Pataki Chakra", "त्रि-पतकी चक्र"),
    VARSHAPHALA_MAJOR_THEMES("Major Themes", "मुख्य विषयहरू"),
    VARSHAPHALA_MONTHLY_OUTLOOK("Monthly Outlook", "मासिक दृष्टिकोण"),
    VARSHAPHALA_FAVORABLE("Favorable", "अनुकूल"),
    VARSHAPHALA_KEY_DATES("Key Dates", "महत्त्वपूर्ण मितिहरू"),
    VARSHAPHALA_OVERALL_PREDICTION("Overall Prediction", "समग्र भविष्यवाणी"),
    VARSHAPHALA_TAJIKA_SUMMARY("Tajika Yogas Summary", "तजिका योग सारांश"),
    VARSHAPHALA_SAHAMS_TITLE("Sahams (Arabic Parts)", "सहम (अरबी भाग)"),
    VARSHAPHALA_SAHAMS_DESC("Sensitive points calculated from planetary positions", "ग्रह स्थितिबाट गणना गरिएको संवेदनशील बिन्दुहरू"),
    VARSHAPHALA_MUDDA_DASHA("Mudda Dasha", "मुद्द दशा"),
    VARSHAPHALA_MUDDA_DASHA_DESC("Annual planetary periods based on Moon's position", "चन्द्रको स्थितिमा आधारित वार्षिक ग्रह अवधिहरू"),
    VARSHAPHALA_POSITION("Position", "स्थिति"),
    VARSHAPHALA_HOUSE("House", "भाव"),
    VARSHAPHALA_DAYS("%d days", "%d दिन"),

    FEATURE_PRASHNA("Prashna", "प्रश्न"),
    FEATURE_PRASHNA_DESC("Horary astrology", "प्रश्न ज्योतिष"),

    // Prashna Details


    FEATURE_SYNASTRY("Synastry", "सिनास्ट्री"),
    FEATURE_SYNASTRY_DESC("Chart comparison", "कुण्डली तुलना"),

    FEATURE_NAKSHATRAS("Nakshatras", "नक्षत्रहरू"),
    FEATURE_NAKSHATRAS_DESC("Deep nakshatra analysis", "गहन नक्षत्र विश्लेषण"),

    FEATURE_SHADBALA("Shadbala", "षड्बल"),
    FEATURE_SHADBALA_DESC("Six-fold strength", "छवटा बलहरू"),

    // Advanced Calculator Features
    FEATURE_SHODASHVARGA("Shodashvarga", "षोडशवर्ग"),
    FEATURE_SHODASHVARGA_DESC("16-divisional chart strength", "१६-विभाजन कुण्डली बल"),

    FEATURE_YOGINI_DASHA("Yogini Dasha", "योगिनी दशा"),
    FEATURE_YOGINI_DASHA_DESC("36-year nakshatra dasha", "३६-वर्षे नक्षत्र दशा"),

    FEATURE_ARGALA("Argala", "अर्गला"),
    FEATURE_ARGALA_DESC("Jaimini intervention analysis", "जैमिनी हस्तक्षेप विश्लेषण"),

    FEATURE_CHARA_DASHA("Chara Dasha", "चर दशा"),
    FEATURE_CHARA_DASHA_DESC("Jaimini sign-based dasha", "जैमिनी राशि-आधारित दशा"),

    FEATURE_BHRIGU_BINDU("Bhrigu Bindu", "भृगु बिन्दु"),
    FEATURE_BHRIGU_BINDU_DESC("Karmic destiny point", "कार्मिक भाग्य बिन्दु"),

    FEATURE_PREDICTIONS("Predictions", "भविष्यवाणी"),
    FEATURE_PREDICTIONS_DESC("Comprehensive life analysis", "व्यापक जीवन विश्लेषण"),

    // New Advanced Features
    FEATURE_ASHTOTTARI_DASHA("Ashtottari Dasha", "अष्टोत्तरी दशा"),
    FEATURE_ASHTOTTARI_DASHA_DESC("108-year Nakshatra-based timing", "१०८ वर्षे नक्षत्र-आधारित समय"),
    FEATURE_SUDARSHANA_CHAKRA("Sudarshana Chakra", "सुदर्शन चक्र"),
    FEATURE_SUDARSHANA_CHAKRA_DESC("Triple-reference annual prediction", "त्रि-संदर्भ वार्षिक भविष्यवाणी"),
    FEATURE_MRITYU_BHAGA("Mrityu Bhaga", "मृत्यु भाग"),
    FEATURE_MRITYU_BHAGA_DESC("Sensitive degrees analysis", "संवेदनशील अंश विश्लेषण"),
    FEATURE_LAL_KITAB("Lal Kitab Remedies", "लाल किताब उपाय"),
    FEATURE_LAL_KITAB_DESC("Practical everyday remedies", "व्यावहारिक दैनिक उपाय"),
    FEATURE_DIVISIONAL_CHARTS("Divisional Charts", "विभागीय कुण्डली"),
    FEATURE_DIVISIONAL_CHARTS_DESC("D-2, D-3, D-9, D-10, D-12 analysis", "होरा, द्रेक्काण, नवांश, दशमांश, द्वादशांश"),
    FEATURE_UPACHAYA_TRANSIT("Upachaya Transits", "उपचय गोचर"),
    FEATURE_UPACHAYA_TRANSIT_DESC("Growth house transit tracking", "उपचय भाव गोचर विश्लेषण"),
    FEATURE_KALACHAKRA_DASHA("Kalachakra Dasha", "कालचक्र दशा"),
    FEATURE_KALACHAKRA_DASHA_DESC("Body-soul timing system for health and spiritual predictions", "स्वास्थ्य र आध्यात्मिक भविष्यवाणीको लागि देह-आत्मा समय प्रणाली"),
    FEATURE_TARABALA("Tarabala", "ताराबल"),
    FEATURE_TARABALA_DESC("Daily Moon strength & Nakshatra timing", "दैनिक चन्द्र बल र नक्षत्र समय"),
    FEATURE_ARUDHA_PADA("Arudha Pada", "आरूढ पद"),
    FEATURE_ARUDHA_PADA_DESC("Jaimini Arudha analysis for manifestation", "जैमिनी आरूढ विश्लेषण"),
    FEATURE_GRAHA_YUDDHA("Graha Yuddha", "ग्रह युद्ध"),
    FEATURE_GRAHA_YUDDHA_DESC("Planetary war analysis & remedies", "ग्रह युद्ध विश्लेषण र उपाय"),
    FEATURE_DASHA_SANDHI("Dasha Sandhi", "दशा सन्धि"),
    FEATURE_DASHA_SANDHI_DESC("Period transition analysis", "अवधि संक्रमण विश्लेषण"),
    FEATURE_GOCHARA_VEDHA("Gochara Vedha", "गोचर वेध"),
    FEATURE_GOCHARA_VEDHA_DESC("Transit obstruction effects", "गोचर अवरोध प्रभाव"),
    FEATURE_KEMADRUMA_YOGA("Kemadruma Yoga", "केमद्रुम योग"),
    FEATURE_KEMADRUMA_YOGA_DESC("Moon isolation analysis", "चन्द्र एकान्त विश्लेषण"),
    FEATURE_PANCH_MAHAPURUSHA("Panch Mahapurusha", "पञ्च महापुरुष"),
    FEATURE_PANCH_MAHAPURUSHA_DESC("Five great person yogas", "पाँच महान् व्यक्ति योग"),
    FEATURE_NITYA_YOGA("Nitya Yoga", "नित्य योग"),
    FEATURE_NITYA_YOGA_DESC("27 daily yogas", "२७ दैनिक योगहरू"),
    FEATURE_AVASTHA("Avastha", "अवस्था"),
    FEATURE_AVASTHA_DESC("Planetary states analysis", "ग्रह अवस्था विश्लेषण"),
    FEATURE_MARAKA("Maraka", "मारक"),
    FEATURE_MARAKA_DESC("Death-inflicting planets", "मृत्यु कारक ग्रहहरू"),
    FEATURE_BADHAKA("Badhaka", "बाधक"),
    FEATURE_BADHAKA_DESC("Obstructing planets", "बाधा कारक ग्रहहरू"),
    FEATURE_VIPAREETA_RAJA_YOGA("Vipareeta Raja Yoga", "विपरीत राजयोग"),
    FEATURE_VIPAREETA_RAJA_YOGA_DESC("Reverse raja yogas", "विपरीत राजयोग विश्लेषण"),
    FEATURE_ISHTA_KASHTA_PHALA("Ishta Kashta Phala", "इष्ट कष्ट फल"),
    FEATURE_ISHTA_KASHTA_PHALA_DESC("Benefic/malefic results", "शुभ/अशुभ फल विश्लेषण"),
    FEATURE_SHOOLA_DASHA("Shoola Dasha", "शूल दशा"),
    FEATURE_SHOOLA_DASHA_DESC("Health & critical timing", "स्वास्थ्य र गम्भीर समय"),
    FEATURE_ASHTAVARGA_TRANSIT("Ashtavarga Transit", "अष्टकवर्ग गोचर"),
    FEATURE_ASHTAVARGA_TRANSIT_DESC("Transit predictions by bindu", "बिन्दुद्वारा गोचर भविष्यवाणी"),

    FEATURE_KAKSHYA_TRANSIT("Kakshya Transit", "कक्ष्या गोचर"),
    FEATURE_KAKSHYA_TRANSIT_DESC("Advanced 8-fold micro-transit analysis", "८-गुणा सूक्ष्म गोचर विश्लेषण"),

    FEATURE_NATIVE_ANALYSIS("Native Analysis", "जातक विश्लेषण"),
    FEATURE_NATIVE_ANALYSIS_DESC("Complete life profile & personality", "सम्पूर्ण जीवन प्रोफाइल र व्यक्तित्व"),

    FEATURE_JAIMINI_KARAKA("Jaimini Karaka", "जैमिनी कारक"),
    FEATURE_JAIMINI_KARAKA_DESC("Chara Karaka Analysis", "चर कारक विश्लेषण"),

    FEATURE_DRIG_DASHA("Drig Dasha", "दृग् दशा"),
    FEATURE_DRIG_DASHA_DESC("Jaimini Longevity System", "जैमिनी आयुर्दाय प्रणाली"),

    FEATURE_SAPTAMSA("Saptamsa (D7)", "सप्तांश (D7)"),
    FEATURE_SAPTAMSA_DESC("Progeny & Fertility Analysis", "सन्तान र प्रजनन विश्लेषण"),

    // Predictions Screen - Tabs
    PREDICTIONS_TAB_OVERVIEW("Overview", "सारांश"),
    PREDICTIONS_TAB_LIFE_AREAS("Life Areas", "जीवन क्षेत्रहरू"),
    PREDICTIONS_TAB_TIMING("Timing", "समय"),
    PREDICTIONS_TAB_REMEDIES("Remedies", "उपाय"),

    // Predictions Screen - Section Headers
    PREDICTIONS_YOUR_LIFE_PATH("Your Life Path", "तपाईंको जीवन मार्ग"),
    PREDICTIONS_KEY_STRENGTHS("Key Strengths", "मुख्य शक्तिहरू"),
    PREDICTIONS_SPIRITUAL_PATH("Spiritual Path", "आध्यात्मिक मार्ग"),
    PREDICTIONS_CURRENT_PERIOD("Current Period", "वर्तमान अवधि"),
    PREDICTIONS_ACTIVE_TRANSITS("Active Transits", "सक्रिय गोचर"),
    PREDICTIONS_ACTIVE_YOGAS("Active Yogas", "सक्रिय योगहरू"),
    PREDICTIONS_OPPORTUNITIES("Opportunities", "अवसरहरू"),
    PREDICTIONS_CURRENT_CHALLENGES("Current Challenges", "वर्तमान चुनौतीहरू"),
    PREDICTIONS_LIFE_AREAS_GLANCE("Life Areas at a Glance", "एक नजरमा जीवन क्षेत्रहरू"),
    PREDICTIONS_FAVORABLE_PERIODS("Favorable Periods", "अनुकूल अवधिहरू"),
    PREDICTIONS_CAUTION_PERIODS("Periods Needing Caution", "सावधानी चाहिने अवधिहरू"),
    PREDICTIONS_IMPORTANT_DATES("Important Dates", "महत्त्वपूर्ण मितिहरू"),
    PREDICTIONS_REMEDIAL_SUGGESTIONS("Remedial Suggestions", "उपचारात्मक सुझावहरू"),

    // Predictions Screen - States
    PREDICTIONS_NO_CHART_SELECTED("No Chart Selected", "कुनै कुण्डली छानिएको छैन"),
    PREDICTIONS_SELECT_CHART_MESSAGE("Please select a birth chart to view predictions", "कृपया भविष्यवाणी हेर्न जन्म कुण्डली छान्नुहोस्"),
    PREDICTIONS_CALCULATING("Calculating Predictions...", "भविष्यवाणी गणना गर्दै..."),
    PREDICTIONS_ERROR_LOADING("Error Loading Predictions", "भविष्यवाणी लोड गर्न त्रुटि"),
    PREDICTIONS_CALC_FAILED("Failed to calculate predictions", "भविष्यवाणी गणना गर्न असफल"),

    // Predictions Screen - Life Area Labels
    PREDICTIONS_CAREER_PROFESSION("Career & Profession", "क्यारियर र पेशा"),
    PREDICTIONS_FINANCE_WEALTH("Finance & Wealth", "वित्त र सम्पत्ति"),
    PREDICTIONS_RELATIONSHIPS_MARRIAGE("Relationships & Marriage", "सम्बन्ध र विवाह"),
    PREDICTIONS_HEALTH_WELLBEING("Health & Wellbeing", "स्वास्थ्य र कल्याण"),
    PREDICTIONS_EDUCATION_LEARNING("Education & Learning", "शिक्षा र सिकाइ"),
    PREDICTIONS_FAMILY_HOME("Family & Home", "परिवार र घर"),

    // Predictions Screen - Other Labels
    PREDICTIONS_SHORT_TERM("Short-term (3-6 months)", "अल्पकालीन (३-६ महिना)"),
    PREDICTIONS_MEDIUM_TERM("Medium-term (6-12 months)", "मध्यमकालीन (६-१२ महिना)"),
    PREDICTIONS_LONG_TERM("Long-term (1-2 years)", "दीर्घकालीन (१-२ वर्ष)"),
    PREDICTIONS_BEST_FOR("Best for", "को लागि उत्तम"),
    PREDICTIONS_CAUTION_FOR("Caution for", "को लागि सावधानी"),
    PREDICTIONS_ENERGY_LEVEL("Energy Level", "ऊर्जा स्तर"),
    PREDICTIONS_DAYS_LEFT("days left", "दिन बाँकी"),
    PREDICTIONS_MONTHS("months", "महिना"),
    PREDICTIONS_GO_BACK("Go Back", "फर्कनुहोस्"),

    // ============================================
    // EMPTY/ERROR STATES
    // ============================================
    NO_PROFILE_SELECTED("No Profile Selected", "कुनै प्रोफाइल छानिएको छैन"),
    NO_PROFILE_MESSAGE("Select or create a profile to view your personalized astrological insights.", "आफ्नो व्यक्तिगत ज्योतिष अन्तर्दृष्टि हेर्न प्रोफाइल छान्नुहोस् वा बनाउनुहोस्।"),
    NO_PROFILE_MESSAGE_LONG("Select or create a profile to view your personalized astrological insights and predictions", "आफ्नो व्यक्तिगत ज्योतिष अन्तर्दृष्टि र भविष्यवाणी हेर्न प्रोफाइल छान्नुहोस् वा बनाउनुहोस्"),

    // Error States
    ERROR_PARTIAL("Some insights unavailable", "केही अन्तर्दृष्टिहरू उपलब्ध छैनन्"),
    ERROR_CALCULATIONS_FAILED("%d calculation(s) could not be completed", "%d गणना(हरू) पूरा हुन सकेन"),
    ERROR_UNABLE_TO_LOAD("Unable to Load Insights", "अन्तर्दृष्टि लोड गर्न असमर्थ"),
    ERROR_HOROSCOPE_UNAVAILABLE("%s's horoscope unavailable", "%s को राशिफल उपलब्ध छैन"),
    ERROR_EPHEMERIS_DATA("Unable to calculate planetary positions for this period. This may be due to ephemeris data limitations.", "यस अवधिको लागि ग्रह स्थितिहरू गणना गर्न असमर्थ। यो ईफेमेरिस डाटा सीमितताको कारण हुन सक्छ।"),
    ERROR_SOMETHING_WRONG("Something went wrong", "केही गलत भयो"),

    // ============================================
    // BUTTONS & ACTIONS
    // ============================================
    BTN_DETAILS("Details", "विवरण"),
    BTN_RETRY("Retry", "पुनः प्रयास"),
    BTN_TRY_AGAIN("Try Again", "फेरि प्रयास गर्नुहोस्"),
    BTN_PREVIEW("Preview", "पूर्वावलोकन"),
    BTN_OK("OK", "ठीक छ"),
    BTN_CANCEL("Cancel", "रद्द गर्नुहोस्"),
    BTN_JUMP_TODAY("Jump to today", "आजमा जानुहोस्"),
    INFO_DASHA("Dasha information", "दशा जानकारी"),
    BTN_DELETE("Delete", "मेट्नुहोस्"),
    BTN_EDIT("Edit", "सम्पादन गर्नुहोस्"),
    BTN_SAVE("Save", "सेभ गर्नुहोस्"),
    BTN_GENERATE("Generate", "उत्पन्न गर्नुहोस्"),
    BTN_GENERATE_SAVE("Generate & Save", "उत्पन्न गर्नुहोस् र सेभ गर्नुहोस्"),
    BTN_UPDATE_SAVE("Update & Save", "अपडेट गर्नुहोस् र सेभ गर्नुहोस्"),
    BTN_GO_BACK("Go back", "पछाडि जानुहोस्"),
    BTN_BACK("Back", "पछाडि"),
    BTN_CLOSE("Close", "बन्द गर्नुहोस्"),
    BTN_REFRESH("Refresh", "रिफ्रेस"),
    BTN_REGENERATE("Regenerate", "पुन: उत्पन्न"),
    BTN_CREATE_CHART("Create Chart", "कुण्डली बनाउनुहोस्"),

    // ============================================
    // LABELS & INDICATORS
    // ============================================
    LABEL_AD("AD", "ई.सं."),
    LABEL_BS("BS", "वि.सं."),
    LABEL_DATE("DATE", "मिति"),
    LABEL_TIME("TIME", "समय"),
    LABEL_TODAY("TODAY", "आज"),
    LABEL_BIRTH("BIRTH", "जन्म"),

    // ============================================
    // INSIGHTS TAB
    // ============================================
    INSIGHTS_OVERALL_ENERGY("Overall Energy", "समग्र ऊर्जा"),
    INSIGHTS_LIFE_AREAS("Life Areas", "जीवन क्षेत्रहरू"),
    INSIGHTS_LUCKY_ELEMENTS("Lucky Elements", "भाग्यशाली तत्वहरू"),
    INSIGHTS_TODAYS_AFFIRMATION("Today's Affirmation", "आजको प्रतिज्ञा"),
    INSIGHTS_WEEKLY_ENERGY("Weekly Energy Flow", "साप्ताहिक ऊर्जा प्रवाह"),
    INSIGHTS_KEY_DATES("Key Dates", "महत्त्वपूर्ण मितिहरू"),
    INSIGHTS_WEEKLY_OVERVIEW("Weekly Overview by Area", "क्षेत्रअनुसार साप्ताहिक अवलोकन"),
    INSIGHTS_WEEKLY_ADVICE("Weekly Advice", "साप्ताहिक सल्लाह"),
    INSIGHTS_RECOMMENDATIONS("Recommendations", "सिफारिसहरू"),
    INSIGHTS_CAUTIONS("Cautions", "सावधानीहरू"),

    // Horoscope periods
    PERIOD_TODAY("Today", "आज"),
    PERIOD_TOMORROW("Tomorrow", "भोलि"),
    PERIOD_WEEKLY("Weekly", "साप्ताहिक"),

    ERROR_ANALYSIS_FAILED("Analysis Failed", "विश्लेषण असफल"),
    MSG_MAY_TAKE_MOMENT("This may take a moment", "यसमा केही समय लाग्न सक्छ"),
    LIFE_AREA_CAREER("Career", "करियर"),
    LIFE_AREA_LOVE("Love & Relationships", "प्रेम र सम्बन्ध"),
    LIFE_AREA_HEALTH("Health & Vitality", "स्वास्थ्य र जीवनशक्ति"),
    LIFE_AREA_FINANCE("Finance & Wealth", "वित्त र सम्पत्ति"),
    LIFE_AREA_FAMILY("Family & Home", "परिवार र घर"),
    LIFE_AREA_SPIRITUALITY("Spiritual Growth", "आध्यात्मिक वृद्धि"),

    // For compatibility with LocalizedDisplayNames.kt
    LIFE_AREA_CAREER_FULL("Career", "क्यारियर"),
    LIFE_AREA_LOVE_FULL("Love & Relationships", "प्रेम र सम्बन्ध"),
    LIFE_AREA_HEALTH_FULL("Health & Vitality", "स्वास्थ्य र जीवनशक्ति"),
    LIFE_AREA_FINANCE_FULL("Finance & Wealth", "वित्त र सम्पत्ति"),
    LIFE_AREA_FAMILY_FULL("Family & Home", "परिवार र घर"),
    LIFE_AREA_SPIRITUALITY_FULL("Spiritual Growth", "आध्यात्मिक वृद्धि"),
    
    // Theme names
    THEME_DYNAMIC_ACTION("Dynamic Action", "गत्यात्मक कार्य"),
    THEME_PRACTICAL_PROGRESS("Practical Progress", "व्यावहारिक प्रगति"),
    THEME_SOCIAL_CONNECTIONS("Social Connections", "सामाजिक सम्बन्ध"),
    THEME_EMOTIONAL_INSIGHT("Emotional Insight", "भावनात्मक अन्तर्दृष्टि"),
    THEME_EXPANSION_WISDOM("Expansion & Wisdom", "विस्तार र ज्ञान"),
    THEME_HARMONY_BEAUTY("Harmony & Beauty", "सामञ्जस्य र सौन्दर्य"),
    THEME_DISCIPLINE_GROWTH("Discipline & Growth", "अनुशासन र वृद्धि"),
    THEME_COMMUNICATION_LEARNING("Communication & Learning", "सञ्चार र सिकाई"),
    THEME_ENERGY_INITIATIVE("Energy & Initiative", "ऊर्जा र पहल"),
    THEME_SELF_EXPRESSION("Self-Expression", "आत्म-अभिव्यक्ति"),
    THEME_INTUITION_NURTURING("Intuition & Nurturing", "अन्तर्ज्ञान र पोषण"),
    THEME_TRANSFORMATION("Transformation", "रूपान्तरण"),
    THEME_SPIRITUAL_LIBERATION("Spiritual Liberation", "आध्यात्मिक मुक्ति"),
    THEME_BALANCE_EQUILIBRIUM("Balance & Equilibrium", "सन्तुलन र स्थिरता"),
    
    // Theme descriptions
    THEME_DESC_DYNAMIC_ACTION("Your energy is high and aligned with fire elements. This is an excellent day for taking initiative, starting new projects, and asserting yourself confidently. Channel this vibrant energy into productive pursuits.", "तपाईंको ऊर्जा उच्च छ र अग्नि तत्वसँग मिल्दोजुल्दो छ। यो पहल गर्न, नयाँ परियोजनाहरू सुरु गर्न र आत्मविश्वासका साथ आफूलाई प्रस्तुत गर्न उत्कृष्ट दिन हो। यस जीवन्त ऊर्जालाई उत्पादक कार्यहरूमा लगाउनुहोस्।"),
    THEME_DESC_PRACTICAL_PROGRESS("Grounded earth energy supports methodical progress today. Focus on practical tasks, financial planning, and building stable foundations. Your efforts will yield tangible results.", "पृथ्वी तत्वको ऊर्जाले आज व्यवस्थित प्रगतिलाई समर्थन गर्दछ। व्यावहारिक कार्यहरू, वित्तीय योजना, र स्थिर आधार निर्माणमा ध्यान दिनुहोस्। तपाईंको प्रयासले मूर्त परिणामहरू दिनेछ।"),
    THEME_DESC_SOCIAL_CONNECTIONS("Air element energy enhances communication and social interactions. Networking, negotiations, and intellectual pursuits are favored. Express your ideas and connect with like-minded people.", "वायु तत्वको ऊर्जाले सञ्चार र सामाजिक अन्तरक्रियालाई बढावा दिन्छ। नेटवर्किङ, वार्ता, र बौद्धिक कार्यहरूका लागि आज अनुकूल छ। आफ्ना विचारहरू व्यक्त गर्नुहोस् र समान विचारधारा भएका मानिसहरूसँग जोडिनुहोस्।"),
    THEME_DESC_EMOTIONAL_INSIGHT("Water element energy deepens your intuition and emotional awareness. Trust your feelings and pay attention to subtle cues. This is a powerful day for healing and self-reflection.", "जल तत्वको ऊर्जाले तपाईंको अन्तर्ज्ञान र भावनात्मक जागरूकतालाई गहिरो बनाउँछ। आफ्ना भावनाहरूमा विश्वास गर्नुहोस् र सूक्ष्म संकेतहरूमा ध्यान दिनुहोस्। यो निको पार्न र आत्म-चिन्तनका लागि एक शक्तिशाली दिन हो।"),
    THEME_DESC_EXPANSION_WISDOM("Jupiter's benevolent influence brings opportunities for growth, learning, and good fortune. Be open to new possibilities and share your wisdom generously.", "बृहस्पतिको उदार प्रभावले वृद्धि, सिकाई र सौभाग्यको अवसरहरू ल्याउँछ। नयाँ सम्भावनाहरूका लागि खुला रहनुहोस् र आफ्नो ज्ञान उदारतापूर्वक बाँड्नुहोस्।"),
    THEME_DESC_HARMONY_BEAUTY("Venus graces you with appreciation for beauty, art, and relationships. Indulge in pleasurable activities and nurture your connections with loved ones.", "शुक्रले तपाईंलाई सौन्दर्य, कला, र सम्बन्धहरूको लागि प्रशंसा प्रदान गर्दछ। आनन्ददायी गतिविधिहरूमा संलग्न हुनुहोस् र आफ्ना प्रियजनहरूसँगको सम्बन्धलाई प्रगाढ बनाउनुहोस्।"),
    THEME_DESC_DISCIPLINE_GROWTH("Saturn's influence calls for patience, hard work, and responsibility. Embrace challenges as opportunities for growth and stay committed to your long-term goals.", "शनिको प्रभावले धैर्य, कडा परिश्रम, र जिम्मेवारीको माग गर्दछ। चुनौतीहरूलाई वृद्धिको अवसरको रूपमा स्वीकार गर्नुहोस् र आफ्ना दीर्घकालीन लक्ष्यहरूमा प्रतिबद्ध रहनुहोस्।"),
    THEME_DESC_COMMUNICATION_LEARNING("Mercury enhances your mental agility and communication skills. This is ideal for learning, teaching, writing, and all forms of information exchange.", "बुधले तपाईंको मानसिक चपलता र सञ्चार सीपलाई बढावा दिन्छ। यो सिकाई, शिक्षण, लेखन, र सबै प्रकारका सूचना आदानप्रदानका लागि उपयुक्त छ।"),
    THEME_DESC_ENERGY_INITIATIVE("Mars provides courage and drive. Take bold action, compete with integrity, and channel aggressive energy into constructive activities.", "मंगलले साहस र जोश प्रदान गर्दछ। साहसी कदम चाल्नुहोस्, इमानदारीका साथ प्रतिस्पर्धा गर्नुहोस्, र आक्रामक ऊर्जालाई रचनात्मक गतिविधिहरूमा लगाउनुहोस्।"),
    THEME_DESC_SELF_EXPRESSION("The Sun illuminates your path to self-expression and leadership. Shine your light confidently and pursue activities that bring you recognition.", "सूर्यले तपाईंको आत्म-अभिव्यक्ति र नेतृत्वको मार्गलाई उज्यालो बनाउँछ। आत्मविश्वासका साथ आफ्नो चमक देखाउनुहोस् र तपाईंलाई पहिचान दिलाउने गतिविधिहरूमा लाग्नुहोस्।"),
    THEME_DESC_INTUITION_NURTURING("The Moon heightens your sensitivity and caring nature. Nurture yourself and others, and trust your instincts in important decisions.", "चन्द्रमाले तपाईंको संवेदनशीलता र हेरचाह गर्ने प्रकृतिलाई बढाउँछ। आफू र अरूको पोषण गर्नुहोस्, र महत्त्वपूर्ण निर्णयहरूमा आफ्नो अन्तर्ज्ञानमा विश्वास गर्नुहोस्।"),
    THEME_DESC_TRANSFORMATION("Rahu's influence brings unconventional opportunities and desires for change. Embrace innovation but stay grounded in your values.", "राहुको प्रभावले अपरम्परागत अवसरहरू र परिवर्तनको इच्छा ल्याउँछ। नवीनतालाई अँगाल्नुहोस् तर आफ्ना मूल्यहरूमा अडिग रहनुहोस्।"),
    THEME_DESC_SPIRITUAL_LIBERATION("Ketu's energy supports detachment and spiritual insight. Let go of what no longer serves you and focus on inner growth.", "केतुको ऊर्जाले वैराग्य र आध्यात्मिक अन्तर्दृष्टिलाई समर्थन गर्दछ। जे कुराले तपाईंलाई अब फाइदा गर्दैन त्यसलाई छोडिदिनुहोस् र आन्तरिक वृद्धिमा ध्यान दिनुहोस्।"),
    THEME_DESC_BALANCE_EQUILIBRIUM("A day of balance where all energies are in equilibrium. Maintain steadiness and make measured progress in all areas of life.", "सबै ऊर्जाहरू सन्तुलनमा रहेको दिन। स्थिरता कायम राख्नुहोस् र जीवनका सबै क्षेत्रमा नापिएको प्रगति गर्नुहोस्।"),
    
    // Weekly themes
    THEME_WEEK_OPPORTUNITIES("Week of Opportunities", "अवसरहरूको हप्ता"),
    THEME_WEEK_STEADY_PROGRESS("Steady Progress", "स्थिर प्रगति"),
    THEME_WEEK_MINDFUL_NAVIGATION("Mindful Navigation", "सचेत यात्रा"),
    
    // Weekly overview templates
    WEEKLY_OVERVIEW_EXCELLENT("excellent opportunities for growth and success. ", "वृद्धि र सफलताको लागि उत्कृष्ट अवसरहरू। "),
    WEEKLY_OVERVIEW_STEADY("steady progress and balanced energy. ", "स्थिर प्रगति र सन्तुलित ऊर्जा। "),
    WEEKLY_OVERVIEW_CHALLENGING("challenges that, when navigated wisely, lead to growth. ", "चुनौतीहरू जसलाई बुद्धिमानीपूर्वक सामना गर्दा वृद्धि हुन्छ। "),
    WEEKLY_OVERVIEW_FOOTER("Trust in your cosmic guidance and make the most of each day's unique energy.", "आफ्नो ब्रह्माण्डीय मार्गदर्शनमा विश्वास गर्नुहोस् र प्रत्येक दिनको अद्वितीय ऊर्जाको सदुपयोग गर्नुहोस्।"),
    
    // Weekly advice
    ADVICE_JUPITER("Embrace opportunities for learning and expansion. Your wisdom and optimism attract positive outcomes.", "सिकाई र विस्तारका अवसरहरूलाई अँगाल्नुहोस्। तपाईंको ज्ञान र आशावादले सकारात्मक परिणामहरू आकर्षित गर्दछ।"),
    ADVICE_VENUS("Focus on cultivating beauty, harmony, and meaningful relationships. Artistic pursuits are favored.", "सौन्दर्य, सामञ्जस्य, र अर्थपूर्ण सम्बन्धहरू विकास गर्नमा ध्यान दिनुहोस्। कलात्मक कार्यहरूलाई प्राथमिकता दिइन्छ।"),
    ADVICE_SATURN("Embrace discipline and patience. Hard work now builds lasting foundations for the future.", "अनुशासन र धैर्यलाई अँगाल्नुहोस्। अहिलेको कडा परिश्रमले भविष्यको लागि स्थायी आधार निर्माण गर्दछ।"),
    ADVICE_MERCURY("Prioritize communication, learning, and intellectual activities. Your mind is sharp.", "सञ्चार, सिकाई, र बौद्धिक गतिविधिहरूलाई प्राथमिकता दिनुहोस्। तपाईंको दिमाग तेज छ।"),
    ADVICE_MARS("Channel your energy into productive activities. Exercise and competition are favored.", "आफ्नो ऊर्जालाई उत्पादक गतिविधिहरूमा लगाउनुहोस्। व्यायाम र प्रतिस्पर्धालाई प्राथमिकता दिइन्छ।"),
    ADVICE_SUN("Let your light shine. Leadership roles and self-expression bring recognition.", "आफ्नो चमक फैलिन दिनुहोस्। नेतृत्वदायी भूमिका र आत्म-अभिव्यक्तिले पहिचान दिलाउँछ।"),
    ADVICE_MOON("Honor your emotions and intuition. Nurturing activities bring fulfillment.", "आफ्ना भावना र अन्तर्ज्ञानको सम्मान गर्नुहोस्। पोषण गर्ने गतिविधिहरूले सन्तुष्टि ल्याउँछ।"),
    ADVICE_RAHU("Embrace transformation while staying grounded. Unconventional approaches may succeed.", "आधारभूत मूल्यहरूमा रहँदै परिवर्तनलाई अँगाल्नुहोस्। अपरम्परागत दृष्टिकोणहरू सफल हुन सक्छन्।"),
    ADVICE_KETU("Focus on spiritual growth and letting go. Detachment brings peace.", "आध्यात्मिक वृद्धि र त्यागमा ध्यान दिनुहोस्। वैराग्यले शान्ति ल्याउँछ।"),
    ADVICE_GENERAL("Maintain balance and trust in divine timing.", "सन्तुलन कायम राख्नुहोस् र ईश्वरीय समयमा विश्वास गर्नुहोस्।"),
    
    // Lucky elements labels
    LUCKY_NUMBER("Number", "अंक"),
    LUCKY_COLOR("Color", "रंग"),
    LUCKY_DIRECTION("Direction", "दिशा"),
    LUCKY_GEMSTONE("Gemstone", "रत्न"),

    // ============================================
    // DASHA SECTION
    // ============================================
    DASHA_CURRENT_PERIOD("Current Planetary Period", "हालको ग्रह अवधि"),
    DASHA_ACTIVE("Active", "सक्रिय"),
    DASHA_MAHADASHA("Mahadasha", "महादशा"),
    DASHA_ANTARDASHA("Antardasha", "अन्तर्दशा"),
    DASHA_PRATYANTARDASHA("Pratyantardasha:", "प्रत्यन्तर्दशा:"),
    DASHA_UPCOMING("Upcoming Periods", "आगामी अवधिहरू"),
    DASHA_REMAINING("remaining", "बाँकी"),
    DASHA_PERIOD_NAME("%s: %s", "%s: %s"),
    DASHA_COMBINED_NAME("%s-%s", "%s-%s"),
    DASHA_LAST_IN_MAHADASHA("Current Antardasha is the last in this Mahadasha", "हालको अन्तर्दशा यस महादशाको अन्तिम हो"),
    DASHA_STARTS("Starts %s", "%s मा सुरु हुन्छ"),
    DASHA_VIMSHOTTARI("Vimshottari Dasha", "विम्शोत्तरी दशा"),
    DASHA_JUMP_TO_TODAY("Jump to current period", "हालको अवधिमा जानुहोस्"),
    DASHA_CALCULATING("Calculating...", "गणना गर्दै..."),
    DASHA_ERROR("Error", "त्रुटि"),
    DASHA_CALCULATING_TIMELINE("Calculating Dasha Timeline", "दशा समयरेखा गणना गर्दै"),
    DASHA_CALCULATING_DESC("Computing planetary periods based on\nMoon's Nakshatra position...", "चन्द्रको नक्षत्र स्थितिको आधारमा\nग्रह अवधिहरू गणना गर्दै..."),
    DASHA_CALCULATION_FAILED("Calculation Failed", "गणना असफल भयो"),
    DASHA_NO_CHART_SELECTED("No Chart Selected", "कुनै कुण्डली छानिएको छैन"),
    DASHA_NO_CHART_MESSAGE("Please select or create a birth profile\nto view the Dasha timeline.", "दशा समयरेखा हेर्न कृपया जन्म प्रोफाइल\nछान्नुहोस् वा बनाउनुहोस्।"),

    // Dasha Level Names
    DASHA_SOOKSHMADASHA("Sookshmadasha", "सूक्ष्मदशा"),
    DASHA_PRANADASHA("Pranadasha", "प्राणदशा"),
    DASHA_DEHADASHA("Dehadasha", "देहदशा"),
    DASHA_BHUKTI("Bhukti", "भुक्ति"),
    DASHA_PRATYANTAR("Pratyantar", "प्रत्यन्तर"),
    DASHA_SOOKSHMA("Sookshma", "सूक्ष्म"),
    DASHA_PRANA("Prana", "प्राण"),
    DASHA_DEHA("Deha", "देह"),

    // Dasha Format Labels
    DASHA_DURATION("Duration", "अवधि"),
    DASHA_PERIOD("Period", "अवधि"),
    DASHA_STATUS("Status", "स्थिति"),
    DASHA_CURRENTLY_ACTIVE("Currently Active", "हाल सक्रिय"),
    DASHA_PROGRESS("Progress", "प्रगति"),
    DASHA_NO_ACTIVE_PERIOD("No active Dasha period", "कुनै सक्रिय दशा अवधि छैन"),

    // Time Units
    YEARS("years", "वर्ष"),
    DAYS("days", "दिन"),
    TO("to", "देखि"),
    YEARS_SHORT("y", "व"),
    MONTHS_SHORT("m", "म"),
    WEEKS_SHORT("w", "ह"),
    DAYS_SHORT("d", "दि"),
    HOURS_SHORT("h", "घ"),
    MINUTES_SHORT("m", "मि"),

    // Yogini Dasha Names
    YOGINI_MANGALA("Mangala", "मङ्गला"),
    YOGINI_PINGALA("Pingala", "पिङ्गला"),
    YOGINI_DHANYA("Dhanya", "धन्या"),
    YOGINI_BHRAMARI("Bhramari", "भ्रामरी"),
    YOGINI_BHADRIKA("Bhadrika", "भद्रिका"),
    YOGINI_ULKA("Ulka", "उल्का"),
    YOGINI_SIDDHA("Siddha", "सिद्धा"),
    YOGINI_SANKATA("Sankata", "सङ्कटा"),

    // Yogini Deity Names
    YOGINI_DEITY_CHANDRA("Chandra (Moon)", "चन्द्र"),
    YOGINI_DEITY_SURYA("Surya (Sun)", "सूर्य"),
    YOGINI_DEITY_GURU("Guru (Jupiter)", "गुरु (बृहस्पति)"),
    YOGINI_DEITY_MANGAL("Mangal (Mars)", "मङ्गल"),
    YOGINI_DEITY_BUDHA("Budha (Mercury)", "बुध"),
    YOGINI_DEITY_SHANI("Shani (Saturn)", "शनि"),
    YOGINI_DEITY_SHUKRA("Shukra (Venus)", "शुक्र"),
    YOGINI_DEITY_RAHU("Rahu", "राहु"),

    // Nature Types
    NATURE_BENEFIC("Benefic", "शुभ"),
    NATURE_MALEFIC("Malefic", "अशुभ"),
    NATURE_MIXED("Mixed", "मिश्रित"),

    // ============================================
    // TRANSITS
    // ============================================
    TRANSITS_CURRENT("Current Transits", "हालका गोचरहरू"),
    TRANSITS_MOON_IN("Moon in %s", "चन्द्रमा %s मा"),

    // ============================================
    // ENERGY DESCRIPTIONS
    // ============================================
    ENERGY_EXCEPTIONAL("Exceptional cosmic alignment - seize every opportunity!", "असाधारण ब्रह्माण्डीय संरेखण - हरेक अवसर समात्नुहोस्!"),
    ENERGY_EXCELLENT("Excellent day ahead - favorable for important decisions", "उत्कृष्ट दिन अगाडि - महत्त्वपूर्ण निर्णयहरूको लागि अनुकूल"),
    ENERGY_STRONG("Strong positive energy - good for new initiatives", "बलियो सकारात्मक ऊर्जा - नयाँ पहलहरूको लागि राम्रो"),
    ENERGY_FAVORABLE("Favorable energy - maintain steady progress", "अनुकूल ऊर्जा - स्थिर प्रगति कायम राख्नुहोस्"),
    ENERGY_BALANCED("Balanced energy - stay centered and focused", "सन्तुलित ऊर्जा - केन्द्रित र ध्यान केन्द्रित रहनुहोस्"),
    ENERGY_MODERATE("Moderate energy - pace yourself wisely", "मध्यम ऊर्जा - बुद्धिमानीपूर्वक आफ्नो गति मिलाउनुहोस्"),
    ENERGY_LOWER("Lower energy day - prioritize rest and reflection", "कम ऊर्जा दिन - आराम र चिन्तनलाई प्राथमिकता दिनुहोस्"),
    ENERGY_CHALLENGING("Challenging day - practice patience and self-care", "चुनौतीपूर्ण दिन - धैर्य र आत्म-हेरचाह अभ्यास गर्नुहोस्"),
    ENERGY_REST("Rest and recharge recommended - avoid major decisions", "आराम र रिचार्ज सिफारिस - प्रमुख निर्णयहरूबाट बच्नुहोस्"),

    // ============================================
    // LUCKY ELEMENTS
    // ============================================
    LUCKY_COLOR_FIRE("Red, Orange, or Gold", "रातो, सुन्तला, वा सुनौलो"),
    LUCKY_COLOR_EARTH("Green, Brown, or White", "हरियो, खैरो, वा सेतो"),
    LUCKY_COLOR_AIR("Blue, Light Blue, or Silver", "निलो, हल्का निलो, वा चाँदी"),
    LUCKY_COLOR_WATER("White, Cream, or Sea Green", "सेतो, क्रीम, वा समुद्री हरियो"),

    LUCKY_DIRECTION_EAST("East", "पूर्व"),
    LUCKY_DIRECTION_WEST("West", "पश्चिम"),
    LUCKY_DIRECTION_NORTH("North", "उत्तर"),
    LUCKY_DIRECTION_SOUTH("South", "दक्षिण"),
    LUCKY_DIRECTION_NORTHEAST("North-East", "उत्तर-पूर्व"),
    LUCKY_DIRECTION_NORTHWEST("North-West", "उत्तर-पश्चिम"),
    LUCKY_DIRECTION_SOUTHEAST("South-East", "दक्षिण-पूर्व"),
    LUCKY_DIRECTION_SOUTHWEST("South-West", "दक्षिण-पश्चिम"),

    // ============================================
    // GEMSTONES
    // ============================================
    GEMSTONE_RUBY("Ruby", "माणिक"),
    GEMSTONE_PEARL("Pearl", "मोती"),
    GEMSTONE_RED_CORAL("Red Coral", "मूंगा"),
    GEMSTONE_EMERALD("Emerald", "पन्ना"),
    GEMSTONE_YELLOW_SAPPHIRE("Yellow Sapphire", "पुष्पराज"),
    GEMSTONE_DIAMOND("Diamond or White Sapphire", "हीरा वा सेतो नीलम"),
    GEMSTONE_BLUE_SAPPHIRE("Blue Sapphire", "नीलम"),
    GEMSTONE_HESSONITE("Hessonite", "गोमेद"),
    GEMSTONE_CATS_EYE("Cat's Eye", "वैदूर्य"),

    // ============================================
    // DASHA RECOMMENDATIONS
    // ============================================
    DASHA_REC_SUN("Engage in activities that build confidence and leadership skills.", "आत्मविश्वास र नेतृत्व कौशल विकास गर्ने गतिविधिहरूमा संलग्न हुनुहोस्।"),
    DASHA_REC_MOON("Prioritize emotional well-being and nurturing relationships.", "भावनात्मक कल्याण र पोषणपूर्ण सम्बन्धहरूलाई प्राथमिकता दिनुहोस्।"),
    DASHA_REC_MARS("Channel your energy into physical activities and competitive pursuits.", "आफ्नो ऊर्जालाई शारीरिक गतिविधि र प्रतिस्पर्धात्मक प्रयासहरूमा प्रयोग गर्नुहोस्।"),
    DASHA_REC_MERCURY("Focus on learning, communication, and intellectual growth.", "सिकाइ, सञ्चार र बौद्धिक वृद्धिमा ध्यान दिनुहोस्।"),
    DASHA_REC_JUPITER("Expand your horizons through education, travel, or spiritual practices.", "शिक्षा, यात्रा वा आध्यात्मिक अभ्यासहरूको माध्यमबाट आफ्नो क्षितिज विस्तार गर्नुहोस्।"),
    DASHA_REC_VENUS("Cultivate beauty, art, and harmonious relationships.", "सौन्दर्य, कला र सामञ्जस्यपूर्ण सम्बन्धहरू विकास गर्नुहोस्।"),
    DASHA_REC_SATURN("Embrace discipline, hard work, and long-term planning.", "अनुशासन, कडा परिश्रम र दीर्घकालीन योजनालाई अँगाल्नुहोस्।"),
    DASHA_REC_RAHU("Explore unconventional paths while staying grounded.", "जमिनमा रहँदै अपरम्परागत मार्गहरू अन्वेषण गर्नुहोस्।"),
    DASHA_REC_KETU("Practice detachment and focus on spiritual development.", "वैराग्यको अभ्यास गर्नुहोस् र आध्यात्मिक विकासमा ध्यान दिनुहोस्।"),

    // ============================================
    // DASHA AFFIRMATIONS
    // ============================================
    DASHA_AFF_SUN("I shine my light confidently and inspire those around me.", "म आत्मविश्वासका साथ आफ्नो प्रकाश फैलाउँछु र वरिपरिका मानिसहरूलाई प्रेरित गर्छु।"),
    DASHA_AFF_MOON("I trust my intuition and nurture myself with compassion.", "म आफ्नो अन्तर्ज्ञानमाथि विश्वास गर्छु र करुणाका साथ आफूलाई पोषण गर्छु।"),
    DASHA_AFF_MARS("I channel my energy constructively and act with courage.", "म आफ्नो ऊर्जा रचनात्मक रूपमा प्रयोग गर्छु र साहसका साथ काम गर्छु।"),
    DASHA_AFF_MERCURY("I communicate clearly and embrace continuous learning.", "म स्पष्ट रूपमा सञ्चार गर्छु र निरन्तर सिकाइलाई अँगाल्छु।"),
    DASHA_AFF_JUPITER("I am open to abundance and share my wisdom generously.", "म प्रचुरताको लागि खुला छु र आफ्नो ज्ञान उदारतापूर्वक साझा गर्छु।"),
    DASHA_AFF_VENUS("I attract beauty and harmony into my life.", "म आफ्नो जीवनमा सौन्दर्य र सामञ्जस्य आकर्षित गर्छु।"),
    DASHA_AFF_SATURN("I embrace discipline and trust in the timing of my journey.", "म अनुशासनलाई अँगाल्छु र आफ्नो यात्राको समयमाथि विश्वास गर्छु।"),
    DASHA_AFF_RAHU("I embrace change and transform challenges into opportunities.", "म परिवर्तनलाई अँगाल्छु र चुनौतीहरूलाई अवसरहरूमा रूपान्तरण गर्छु।"),
    DASHA_AFF_KETU("I release what no longer serves me and embrace spiritual growth.", "म जुन कुराले अब मेरो सेवा गर्दैन त्यसलाई छाड्छु र आध्यात्मिक वृद्धिलाई अँगाल्छु।"),

    // ============================================
    // ELEMENT RECOMMENDATIONS
    // ============================================
    ELEMENT_REC_FIRE("Take bold action and express yourself confidently.", "साहसी कदम चाल्नुहोस् र आत्मविश्वासका साथ आफूलाई व्यक्त गर्नुहोस्।"),
    ELEMENT_REC_EARTH("Focus on practical matters and material progress.", "व्यावहारिक मामिलाहरू र भौतिक प्रगतिमा ध्यान दिनुहोस्।"),
    ELEMENT_REC_AIR("Engage in social activities and intellectual pursuits.", "सामाजिक गतिविधिहरू र बौद्धिक प्रयासहरूमा संलग्न हुनुहोस्।"),
    ELEMENT_REC_WATER("Trust your intuition and honor your emotions.", "आफ्नो अन्तर्ज्ञानमाथि विश्वास गर्नुहोस् र आफ्ना भावनाहरूलाई सम्मान गर्नुहोस्।"),

    // ============================================
    // LIFE AREA RECOMMENDATIONS
    // ============================================
    AREA_REC_CAREER("Capitalize on favorable career energy today.", "आज अनुकूल क्यारियर ऊर्जाको फाइदा उठाउनुहोस्।"),
    AREA_REC_LOVE("Nurture your relationships with extra attention.", "थप ध्यानका साथ आफ्ना सम्बन्धहरूलाई पोषण गर्नुहोस्।"),
    AREA_REC_HEALTH("Make the most of your vibrant health energy.", "आफ्नो जीवन्त स्वास्थ्य ऊर्जाको अधिकतम फाइदा लिनुहोस्।"),
    AREA_REC_FINANCE("Take advantage of positive financial influences.", "सकारात्मक वित्तीय प्रभावहरूको फाइदा उठाउनुहोस्।"),
    AREA_REC_FAMILY("Spend quality time with family members.", "परिवारका सदस्यहरूसँग गुणस्तरीय समय बिताउनुहोस्।"),
    AREA_REC_SPIRITUALITY("Deepen your spiritual practices.", "आफ्नो आध्यात्मिक अभ्यासहरूलाई गहिरो बनाउनुहोस्।"),

    // ============================================
    // PLANET CAUTIONS
    // ============================================
    CAUTION_SATURN("Avoid rushing into decisions. Patience is key.", "निर्णयहरूमा हतार नगर्नुहोस्। धैर्य महत्त्वपूर्ण छ।"),
    CAUTION_MARS("Control impulsive reactions and avoid conflicts.", "आवेगपूर्ण प्रतिक्रियाहरू नियन्त्रण गर्नुहोस् र विवादहरूबाट बच्नुहोस्।"),
    CAUTION_RAHU("Be wary of deception and unrealistic expectations.", "छलकपट र अवास्तविक अपेक्षाहरूबाट सावधान रहनुहोस्।"),
    CAUTION_KETU("Don't neglect practical responsibilities for escapism.", "पलायनवादको लागि व्यावहारिक जिम्मेवारीहरूलाई बेवास्ता नगर्नुहोस्।"),

    // ============================================
    // HOROSCOPE UI STRINGS
    // ============================================
    HOROSCOPE_BALANCE("Balance", "सन्तुलन"),
    HOROSCOPE_STEADY_ENERGY("Steady energy expected", "स्थिर ऊर्जा अपेक्षित"),
    HOROSCOPE_CALCULATING("Calculating...", "गणना गर्दै..."),
    HOROSCOPE_VEDHA_OBSTRUCTION("However, %s creates Vedha obstruction, reducing benefits.", "तर, %s ले वेध अवरोध सिर्जना गर्छ, फाइदाहरू घटाउँछ।"),
    HOROSCOPE_ASHTAKAVARGA_STRONG("Ashtakavarga (%d/8) strengthens results.", "अष्टकवर्ग (%d/8) ले परिणामहरू बलियो बनाउँछ।"),
    HOROSCOPE_ASHTAKAVARGA_MODERATE("Ashtakavarga (%d/8) moderates results.", "अष्टकवर्ग (%d/8) ले परिणामहरूलाई मध्यम बनाउँछ।"),
    HOROSCOPE_ASHTAKAVARGA_WEAK("Low Ashtakavarga (%d/8) weakens results.", "कम अष्टकवर्ग (%d/8) ले परिणामहरू कमजोर बनाउँछ।"),
    HOROSCOPE_RETROGRADE_DELAY("%s's retrograde motion delays manifestation.", "%s को वक्री गतिले प्रकटीकरणमा ढिलाइ गर्छ।"),
    HOROSCOPE_RETROGRADE_RELIEF("%s's retrograde provides some relief from challenges.", "%s को वक्रीले चुनौतीहरूबाट केही राहत प्रदान गर्छ।"),
    HOROSCOPE_OWN_SIGN("Strong in own sign.", "आफ्नै राशिमा बलियो।"),
    HOROSCOPE_EXALTED("Exalted - excellent results.", "उच्च - उत्कृष्ट परिणामहरू।"),
    HOROSCOPE_DEBILITATED("Debilitated - results weakened.", "नीच - परिणामहरू कमजोर।"),
    HOROSCOPE_FAVORABLE_TRANSIT("Favorable %s transit in house %d.", "भाव %d मा अनुकूल %s गोचर।"),
    HOROSCOPE_UNFAVORABLE_TRANSIT("Challenging %s transit in house %d.", "भाव %d मा चुनौतीपूर्ण %s गोचर।"),
    HOROSCOPE_BALANCED_ENERGY("Balanced energy in this area.", "यस क्षेत्रमा सन्तुलित ऊर्जा।"),

    // ============================================
    // SETTINGS TAB
    // ============================================
    SETTINGS_PROFILE("Profile", "प्रोफाइल"),
    SETTINGS_EXPORT("Export", "निर्यात"),
    SETTINGS_PREFERENCES("Preferences", "प्राथमिकताहरू"),
    SETTINGS_ABOUT("About", "बारेमा"),

    // Profile settings
    SETTINGS_EDIT_PROFILE("Edit Profile", "प्रोफाइल सम्पादन"),
    SETTINGS_EDIT_PROFILE_DESC("Modify birth details", "जन्म विवरण परिमार्जन गर्नुहोस्"),
    SETTINGS_MANAGE_PROFILES("Manage Profiles", "प्रोफाइलहरू व्यवस्थापन"),
    SETTINGS_NO_PROFILE("No profile selected", "कुनै प्रोफाइल छानिएको छैन"),
    SETTINGS_TAP_TO_SELECT("Tap to select or create a profile", "प्रोफाइल छान्न वा बनाउन ट्याप गर्नुहोस्"),

    // Export settings
    SETTINGS_EXPORT_PDF("PDF Report", "PDF रिपोर्ट"),
    SETTINGS_EXPORT_PDF_DESC("Complete chart report", "पूर्ण कुण्डली रिपोर्ट"),
    SETTINGS_EXPORT_IMAGE("Chart Image", "कुण्डली छवि"),
    SETTINGS_EXPORT_IMAGE_DESC("High-quality chart image", "उच्च गुणस्तर कुण्डली छवि"),
    SETTINGS_EXPORT_JSON("JSON Data", "JSON डाटा"),
    SETTINGS_EXPORT_JSON_DESC("Machine-readable format", "मेसिन-पठनयोग्य ढाँचा"),
    SETTINGS_EXPORT_CSV("CSV Data", "CSV डाटा"),
    SETTINGS_EXPORT_CSV_DESC("Spreadsheet format", "स्प्रेडसिट ढाँचा"),
    SETTINGS_EXPORT_TEXT("Text Report", "पाठ रिपोर्ट"),
    SETTINGS_EXPORT_TEXT_DESC("Plain text format", "सादा पाठ ढाँचा"),

    // Preferences
    SETTINGS_HOUSE_SYSTEM("House System", "भाव पद्धति"),
    SETTINGS_AYANAMSA("Ayanamsa", "अयनांश"),
    SETTINGS_LANGUAGE("Language", "भाषा"),
    SETTINGS_DATE_SYSTEM("Date System", "मिति प्रणाली"),
    SETTINGS_THEME("Theme", "थिम"),
    SETTINGS_NODE_CALCULATION("Node Calculation", "राहु-केतु गणना"),
    SETTINGS_NODE_MEAN("Mean Node", "मध्यम राहु"),
    SETTINGS_NODE_TRUE("True Node", "स्पष्ट राहु"),

    // Theme options
    THEME_LIGHT("Light", "उज्यालो"),
    THEME_LIGHT_DESC("Always use light theme", "सधैं उज्यालो थिम प्रयोग गर्नुहोस्"),
    THEME_DARK("Dark", "अँध्यारो"),
    THEME_DARK_DESC("Always use dark theme", "सधैं अँध्यारो थिम प्रयोग गर्नुहोस्"),
    THEME_SYSTEM("System", "प्रणाली"),
    THEME_SYSTEM_DESC("Follow device settings", "यन्त्र सेटिङ्स पछ्याउनुहोस्"),

    // About section
    SETTINGS_ABOUT_APP("About AstroVajra", "AstroVajra बारेमा"),
    SETTINGS_VERSION("Version %s", "संस्करण %s"),
    SETTINGS_CALC_ENGINE("Calculation Engine", "गणना इन्जिन"),
    SETTINGS_CALC_ENGINE_DESC("Swiss Ephemeris (JPL Mode)", "स्विस ईफेमेरिस (JPL मोड)"),
    SETTINGS_APP_TAGLINE("Ultra-Precision Vedic Astrology", "अति-सटीक वैदिक ज्योतिष"),
    SETTINGS_APP_DESC("Powered by Swiss Ephemeris with JPL planetary data for astronomical-grade accuracy in all calculations.", "सबै गणनाहरूमा खगोलीय-ग्रेड सटीकताको लागि JPL ग्रह डाटासहित स्विस ईफेमेरिसद्वारा संचालित।"),
    SETTINGS_LAHIRI("Lahiri Ayanamsa", "लहिरी अयनांश"),
    SETTINGS_PLACIDUS("Placidus Houses", "प्लासिडस भावहरू"),

    // Delete dialog
    DIALOG_DELETE_PROFILE("Delete Profile", "प्रोफाइल मेट्नुहोस्"),
    DIALOG_DELETE_CONFIRM("Are you sure you want to delete %s? This action cannot be undone.", "के तपाईं %s मेट्न चाहनुहुन्छ? यो कार्य पूर्ववत गर्न सकिँदैन।"),
    DIALOG_EXPORT_CHART("Export Chart", "कुण्डली निर्यात गर्नुहोस्"),

    // Chart detail labels
    CHART_ASCENDANT("Ascendant", "लग्न"),
    CHART_MOON_SIGN("Moon Sign", "चन्द्र राशि"),
    CHART_NAKSHATRA("Nakshatra", "नक्षत्र"),
    MISC_INFO("Information", "जानकारी"),

    // ============================================
    // CHART INPUT SCREEN
    // ============================================
    INPUT_NEW_CHART("New Birth Chart", "नयाँ जन्म कुण्डली"),
    INPUT_EDIT_CHART("Edit Birth Chart", "जन्म कुण्डली सम्पादन"),
    INPUT_IDENTITY("Identity", "पहिचान"),
    INPUT_DATE_TIME("Date & Time", "मिति र समय"),
    INPUT_COORDINATES("Coordinates", "निर्देशांकहरू"),

    INPUT_FULL_NAME("Full name", "पूरा नाम"),
    INPUT_GENDER("Gender", "लिङ्ग"),
    INPUT_LOCATION("Location", "स्थान"),
    INPUT_SEARCH_LOCATION("Search city or enter manually", "शहर खोज्नुहोस् वा म्यानुअल रूपमा प्रविष्ट गर्नुहोस्"),
    INPUT_TIMEZONE("Timezone", "समय क्षेत्र"),
    INPUT_LATITUDE("Latitude", "अक्षांश"),
    INPUT_LONGITUDE("Longitude", "देशान्तर"),
    INPUT_ALTITUDE("Altitude (m) - Optional", "उचाई (मि.) - वैकल्पिक"),

    INPUT_SELECT_DATE("Select date", "मिति छान्नुहोस्"),
    INPUT_SELECT_TIME("Select time", "समय छान्नुहोस्"),

    // Validation errors
    ERROR_INPUT("Input Error", "इनपुट त्रुटि"),
    ERROR_INVALID_COORDS("Please enter valid latitude and longitude", "कृपया मान्य अक्षांश र देशान्तर प्रविष्ट गर्नुहोस्"),
    ERROR_LATITUDE_RANGE("Latitude must be between -90 and 90", "अक्षांश -९० र ९० बीचमा हुनुपर्छ"),
    ERROR_LONGITUDE_RANGE("Longitude must be between -180 and 180", "देशान्तर -१८० र १८० बीचमा हुनुपर्छ"),
    ERROR_CHECK_INPUT("Please check your input values", "कृपया आफ्नो इनपुट मानहरू जाँच गर्नुहोस्"),
    ERROR_CALCULATION_FAILED("Calculation failed", "गणना असफल भयो"),
    ERROR_RATE_LIMIT("Too many requests. Please wait.", "धेरै अनुरोधहरू। कृपया पर्खनुहोस्।"),
    ERROR_SEARCH_FAILED("Search failed. Please try again.", "खोज असफल भयो। कृपया फेरि प्रयास गर्नुहोस्।"),
    ERROR_RATE_LIMIT_EXCEEDED("Rate limit exceeded", "दर सीमा नाघ्यो"),
    ERROR_NAME_TOO_LONG("Name must be 100 characters or less", "नाम १०० वर्ण वा कम हुनुपर्छ"),
    ERROR_DATE_IN_FUTURE("Birth date cannot be in the future", "जन्म मिति भविष्यमा हुन सक्दैन"),
    ERROR_DATE_TOO_OLD("Date must be after year 1800", "मिति वर्ष १८०० पछिको हुनुपर्छ"),
    ERROR_LOCATION_REQUIRED("Please enter a location or coordinates", "कृपया स्थान वा निर्देशांक प्रविष्ट गर्नुहोस्"),
    ERROR_TIMEZONE_INVALID("Please select a valid timezone", "कृपया मान्य समय क्षेत्र छान्नुहोस्"),

    // Location Search
    LOCATION_SEARCH("Search location", "स्थान खोज्नुहोस्"),
    LOCATION_PLACEHOLDER("Enter city or place name", "शहर वा ठाउँको नाम प्रविष्ट गर्नुहोस्"),
    LOCATION_CLEAR("Clear search", "खोज हटाउनुहोस्"),
    LOCATION_SELECT("Select %s", "%s छान्नुहोस्"),

    // ============================================
    // PROFILE EDIT SCREEN
    // ============================================
    EDIT_PROFILE_TITLE("Edit Profile", "प्रोफाइल सम्पादन"),
    EDIT_PROFILE_NO_DATA("No chart data available. Please select a profile to edit.", "कुनै कुण्डली डाटा उपलब्ध छैन। कृपया सम्पादन गर्न प्रोफाइल छान्नुहोस्।"),

    // ============================================
    // GENDER OPTIONS
    // ============================================
    GENDER_MALE("Male", "पुरुष"),
    GENDER_FEMALE("Female", "महिला"),
    GENDER_OTHER("Other", "अन्य"),

    // ============================================
    // GENERIC ASTROLOGICAL TERMS
    // ============================================
    CHART_HOUSE("House", "भाव"),
    NAKSHATRA_PADA("Pada", "पद"),
    PLANET_RETROGRADE("Retrograde", "वक्री"),
    PLANET_RETROGRADE_SHORT("(R)", "(व)"),
    UNIT_RUPAS("rupas", "रुपास"),
    UNIT_DAYS("days", "दिन"),
    UNIT_MONTHS("months", "महिना"),
    UNIT_YEARS("years", "वर्ष"),

    PLANET_IN_SIGN_ACCESSIBILITY("%1\$s in %2\$s", "%2\$sमा %1\$s"),
    NAKSHATRA_PADA_ACCESSIBILITY("%1\$s pada %2\$s", "%1\$s पद %2\$s"),

    // ============================================
    // PLANETS
    // ============================================
    PLANET("Planet", "ग्रह"),
    PLANET_SUN("Sun", "सूर्य"),
    PLANET_MOON("Moon", "चन्द्र"),
    PLANET_MERCURY("Mercury", "बुध"),
    PLANET_VENUS("Venus", "शुक्र"),
    PLANET_MARS("Mars", "मंगल"),
    PLANET_JUPITER("Jupiter", "बृहस्पति"),
    PLANET_SATURN("Saturn", "शनि"),
    PLANET_RAHU("Rahu", "राहु"),
    PLANET_KETU("Ketu", "केतु"),
    PLANET_URANUS("Uranus", "युरेनस"),
    PLANET_NEPTUNE("Neptune", "नेप्च्युन"),
    PLANET_PLUTO("Pluto", "प्लुटो"),

    // ============================================
    // ZODIAC SIGNS
    // ============================================
    SIGN_ARIES("Aries", "मेष"),
    SIGN_TAURUS("Taurus", "वृष"),
    SIGN_GEMINI("Gemini", "मिथुन"),
    SIGN_CANCER("Cancer", "कर्कट"),
    SIGN_LEO("Leo", "सिंह"),
    SIGN_VIRGO("Virgo", "कन्या"),
    SIGN_LIBRA("Libra", "तुला"),
    SIGN_SCORPIO("Scorpio", "वृश्चिक"),
    SIGN_SAGITTARIUS("Sagittarius", "धनु"),
    SIGN_CAPRICORN("Capricorn", "मकर"),
    SIGN_AQUARIUS("Aquarius", "कुम्भ"),
    SIGN_PISCES("Pisces", "मीन"),

    // ============================================
    // NAKSHATRAS
    // ============================================
    NAKSHATRA_ASHWINI("Ashwini", "अश्विनी"),
    NAKSHATRA_BHARANI("Bharani", "भरणी"),
    NAKSHATRA_KRITTIKA("Krittika", "कृत्तिका"),
    NAKSHATRA_ROHINI("Rohini", "रोहिणी"),
    NAKSHATRA_MRIGASHIRA("Mrigashira", "मृगशिरा"),
    NAKSHATRA_ARDRA("Ardra", "आर्द्रा"),
    NAKSHATRA_PUNARVASU("Punarvasu", "पुनर्वसु"),
    NAKSHATRA_PUSHYA("Pushya", "पुष्य"),
    NAKSHATRA_ASHLESHA("Ashlesha", "आश्लेषा"),
    NAKSHATRA_MAGHA("Magha", "मघा"),
    NAKSHATRA_PURVA_PHALGUNI("Purva Phalguni", "पूर्वा फाल्गुनी"),
    NAKSHATRA_UTTARA_PHALGUNI("Uttara Phalguni", "उत्तरा फाल्गुनी"),
    NAKSHATRA_HASTA("Hasta", "हस्त"),
    NAKSHATRA_CHITRA("Chitra", "चित्रा"),
    NAKSHATRA_SWATI("Swati", "स्वाति"),
    NAKSHATRA_VISHAKHA("Vishakha", "विशाखा"),
    NAKSHATRA_ANURADHA("Anuradha", "अनुराधा"),
    NAKSHATRA_JYESHTHA("Jyeshtha", "ज्येष्ठा"),
    NAKSHATRA_MULA("Mula", "मूल"),
    NAKSHATRA_PURVA_ASHADHA("Purva Ashadha", "पूर्वाषाढा"),
    NAKSHATRA_UTTARA_ASHADHA("Uttara Ashadha", "उत्तराषाढा"),
    NAKSHATRA_SHRAVANA("Shravana", "श्रवण"),
    NAKSHATRA_DHANISHTHA("Dhanishtha", "धनिष्ठा"),
    NAKSHATRA_SHATABHISHA("Shatabhisha", "शतभिषा"),
    NAKSHATRA_PURVA_BHADRAPADA("Purva Bhadrapada", "पूर्वभाद्रपद"),
    NAKSHATRA_UTTARA_BHADRAPADA("Uttara Bhadrapada", "उत्तरभाद्रपद"),
    NAKSHATRA_REVATI("Revati", "रेवती"),

    // ============================================
    // HOUSE SYSTEMS
    // ============================================
    HOUSE_PLACIDUS("Placidus", "प्लासिडस"),
    HOUSE_KOCH("Koch", "कोच"),
    HOUSE_PORPHYRIUS("Porphyrius", "पोर्फिरियस"),
    HOUSE_REGIOMONTANUS("Regiomontanus", "रेजिओमोन्टानस"),
    HOUSE_CAMPANUS("Campanus", "क्याम्पानस"),
    HOUSE_EQUAL("Equal", "समान"),
    HOUSE_WHOLE_SIGN("Whole Sign", "सम्पूर्ण राशि"),
    HOUSE_VEHLOW("Vehlow", "भेहलो"),
    HOUSE_MERIDIAN("Meridian", "मेरिडियन"),
    HOUSE_MORINUS("Morinus", "मोरिनस"),
    HOUSE_ALCABITUS("Alcabitus", "अल्काबिटस"),

    // ============================================
    // AYANAMSA OPTIONS
    // ============================================
    AYANAMSA_LAHIRI("Lahiri", "लहिरी"),
    AYANAMSA_RAMAN("Raman", "रमण"),
    AYANAMSA_KRISHNAMURTI("Krishnamurti", "कृष्णमूर्ति"),
    AYANAMSA_TRUE_CHITRAPAKSHA("True Chitrapaksha", "सत्य चित्रपक्ष"),

    // ============================================
    // YOGA ANALYSIS
    // ============================================
    YOGA_ANALYSIS_SUMMARY("Yoga Analysis Summary", "योग विश्लेषण सारांश"),
    YOGA_OVERALL_STRENGTH("Overall Yoga Strength", "समग्र योग बल"),
    YOGA_TOTAL("Total Yogas", "कुल योगहरू"),
    YOGA_AUSPICIOUS("Auspicious", "शुभ"),
    YOGA_CHALLENGING("Challenging", "चुनौतीपूर्ण"),
    YOGA_ALL("All", "सबै"),
    YOGA_COUNT_DETECTED("%d yogas detected", "%d योगहरू पत्ता लागेको"),
    YOGA_INFORMATION("Yoga Information", "योग जानकारी"),
    YOGA_ABOUT_TITLE("About Vedic Yogas", "वैदिक योगहरूको बारेमा"),
    YOGA_ABOUT_DESCRIPTION("Yogas are special planetary combinations in Vedic astrology that indicate specific life patterns, talents, and karmic influences.", "योगहरू वैदिक ज्योतिषमा विशेष ग्रह संयोजनहरू हुन् जसले विशिष्ट जीवन ढाँचा, प्रतिभा र कर्म प्रभावहरू संकेत गर्छन्।"),
    YOGA_CATEGORIES_TITLE("Categories", "वर्गहरू"),
    YOGA_GOT_IT("Got it", "बुझें"),
    YOGA_NO_DATA("No yoga data available", "कुनै योग डाटा उपलब्ध छैन"),
    YOGA_NO_CHART_MESSAGE("Select or create a birth profile to view yogas.", "योगहरू हेर्न जन्म प्रोफाइल छान्नुहोस् वा बनाउनुहोस्।"),
    YOGAS_COUNT_DETECTED("%1\$d yogas detected in %2\$s", "%2\$s मा %1\$d योगहरू पत्ता लागेको"),

    // Yoga Categories
    YOGA_CATEGORY_WEALTH("Wealth Yogas", "धन योगहरू"),
    YOGA_CATEGORY_WEALTH_DESC("Combinations for prosperity", "समृद्धिका लागि संयोजनहरू"),
    YOGA_CATEGORY_RAJA("Raja Yogas", "राज योगहरू"),
    YOGA_CATEGORY_RAJA_DESC("Combinations for power & authority", "शक्ति र अधिकारका लागि संयोजनहरू"),
    YOGA_CATEGORY_SPIRITUAL("Spiritual Yogas", "आध्यात्मिक योगहरू"),
    YOGA_CATEGORY_SPIRITUAL_DESC("Combinations for spiritual growth", "आध्यात्मिक वृद्धिका लागि संयोजनहरू"),
    YOGA_CATEGORY_CHALLENGING("Challenging Yogas", "चुनौतीपूर्ण योगहरू"),
    YOGA_CATEGORY_CHALLENGING_DESC("Combinations indicating obstacles", "बाधाहरू संकेत गर्ने संयोजनहरू"),
    YOGA_CATEGORY_OTHER("Other Yogas", "अन्य योगहरू"),
    YOGA_CATEGORY_OTHER_DESC("Other planetary combinations", "अन्य ग्रह संयोजनहरू"),
    YOGA_CATEGORY_DHANA("Dhana Yogas", "धन योगहरू"),
    YOGA_CATEGORY_DHANA_DESC("Combinations for wealth", "धनका लागि संयोजनहरू"),
    YOGA_CATEGORY_MAHAPURUSHA("Mahapurusha Yogas", "महापुरुष योगहरू"),
    YOGA_CATEGORY_MAHAPURUSHA_DESC("Great personality combinations", "महान व्यक्तित्व संयोजनहरू"),
    YOGA_CATEGORY_NABHASA("Nabhasa Yogas", "नाभस योगहरू"),
    YOGA_CATEGORY_NABHASA_DESC("Celestial combinations", "आकाशीय संयोजनहरू"),
    YOGA_CATEGORY_CHANDRA("Chandra Yogas", "चन्द्र योगहरू"),
    YOGA_CATEGORY_CHANDRA_DESC("Moon-based combinations", "चन्द्रमामा आधारित संयोजनहरू"),
    YOGA_CATEGORY_SOLAR("Solar Yogas", "सूर्य योगहरू"),
    YOGA_CATEGORY_SOLAR_DESC("Sun-based combinations", "सूर्यमा आधारित संयोजनहरू"),
    YOGA_CATEGORY_SPECIAL("Special Yogas", "विशेष योगहरू"),
    YOGA_CATEGORY_SPECIAL_DESC("Rare and special combinations", "दुर्लभ र विशेष संयोजनहरू"),
    YOGA_CATEGORY_NEGATIVE("Negative Yogas", "नकारात्मक योगहरू"),
    YOGA_CATEGORY_NEGATIVE_DESC("Combinations indicating challenges", "चुनौतीहरू संकेत गर्ने संयोजनहरू"),

    // Singular Yoga Categories (for chips/labels)
    YOGA_CAT_RAJA("Raja Yoga", "राज योग"),
    YOGA_CAT_DHANA("Dhana Yoga", "धन योग"),
    YOGA_CAT_MAHAPURUSHA("Mahapurusha Yoga", "महापुरुष योग"),
    YOGA_CAT_NABHASA("Nabhasa Yoga", "नाभस योग"),
    YOGA_CAT_CHANDRA("Chandra Yoga", "चन्द्र योग"),
    YOGA_CAT_SOLAR("Solar Yoga", "सूर्य योग"),
    YOGA_CAT_NEGATIVE("Negative Yoga", "नकारात्मक योग"),
    YOGA_CAT_SPECIAL("Special Yoga", "विशेष योग"),

    // Yoga Strength Levels
    YOGA_STRENGTH_EXTREMELY_STRONG("Extremely Strong", "अत्यन्त बलियो"),
    YOGA_STRENGTH_VERY_STRONG("Very Strong", "धेरै बलियो"),
    YOGA_STRENGTH_STRONG("Strong", "बलियो"),
    YOGA_STRENGTH_MODERATE("Moderate", "मध्यम"),
    YOGA_STRENGTH_WEAK("Weak", "कमजोर"),
    YOGA_STRENGTH_VERY_WEAK("Very Weak", "धेरै कमजोर"),

    // Yoga Tab Content UI Strings
    YOGA_MOST_SIGNIFICANT("Most Significant Yogas", "सबैभन्दा महत्त्वपूर्ण योगहरू"),
    YOGA_SANSKRIT("Sanskrit", "संस्कृत"),
    YOGA_EFFECTS("Effects", "प्रभावहरू"),
    YOGA_ACTIVATION("Activation", "सक्रियता"),
    YOGA_CANCELLATION_FACTORS("Cancellation/Mitigation Factors", "रद्द/न्यूनीकरण कारकहरू"),
    YOGA_NO_CATEGORY_FOUND("No %s found", "कुनै %s फेला परेन"),
    YOGA_NONE_DETECTED("No yogas detected", "कुनै योग पत्ता लागेन"),
    YOGA_HOUSE_PREFIX("H", "भाव"),
    YOGA_SUBTITLE("Planetary combinations in your chart", "तपाईंको कुण्डलीमा ग्रह संयोजनहरू"),
    YOGA_STRENGTH("Strength", "बल"),
    YOGA_DOMINANT_CATEGORY("Dominant Category", "प्रमुख वर्ग"),
    YOGA_FILTER_BY_CATEGORY("Filter by Category", "वर्गअनुसार फिल्टर गर्नुहोस्"),
    YOGA_COUNT_SUFFIX("yogas", "योगहरू"),
    YOGA_PLANETS_INVOLVED("Planets Involved", "संलग्न ग्रहहरू"),
    YOGA_HOUSES_LABEL("Houses:", "भावहरू:"),
    YOGA_STRENGTH_LABEL("Yoga Strength", "योग बल"),
    YOGA_ACTIVATION_LABEL("Activation:", "सक्रियता:"),
    YOGA_NO_YOGAS_FOUND("No Yogas Found", "कुनै योग फेला परेन"),

    // ============================================
    // PROFILE SWITCHER
    // ============================================
    PROFILE_SWITCH("Switch Profile", "प्रोफाइल बदल्नुहोस्"),
    PROFILE_ADD_NEW("Add New Profile", "नयाँ प्रोफाइल थप्नुहोस्"),
    PROFILE_ADD_NEW_CHART("Add new chart", "नयाँ कुण्डली थप्नुहोस्"),
    PROFILE_CURRENT("Current", "हालको"),
    PROFILE_NO_SAVED_CHARTS("No saved charts", "कुनै सुरक्षित कुण्डली छैन"),
    PROFILE_ADD_FIRST_CHART("Add your first chart to get started", "सुरु गर्न आफ्नो पहिलो कुण्डली थप्नुहोस्"),
    PROFILE_SELECTED("selected", "छानिएको"),
    PROFILE_SELECT("Select Profile", "प्रोफाइल छान्नुहोस्"),
    PROFILE_CURRENT_A11Y("Current profile: %s. Tap to switch profiles", "हालको प्रोफाइल: %s। प्रोफाइलहरू बदल्न ट्याप गर्नुहोस्"),
    PROFILE_NO_SELECTED_A11Y("No profile selected. Tap to select a profile", "कुनै प्रोफाइल छानिएको छैन। प्रोफाइल छान्न ट्याप गर्नुहोस्"),
    PROFILE_BIRTH_CHART("Birth chart", "जन्म कुण्डली"),
    PROFILE_DELETE_TITLE("Delete Birth Chart", "जन्म कुण्डली मेट्नुहोस्"),
    PROFILE_DELETE_MESSAGE("Are you sure you want to delete \"{name}\"? This action cannot be undone and all associated data will be permanently removed.", "के तपाईं \"{name}\" मेट्न निश्चित हुनुहुन्छ? यो कार्य पूर्ववत गर्न सकिँदैन र सबै सम्बन्धित डाटा स्थायी रूपमा हटाइनेछ।"),
    PROFILE_EDIT_CHART("Edit Chart", "कुण्डली सम्पादन गर्नुहोस्"),

    // ============================================
    // TRANSITS SCREEN
    // ============================================
    TRANSIT_CURRENT_MOVEMENTS("Current movements in %s", "%s मा हालको गति"),
    TRANSIT_PLANET_POSITIONS("Current Positions", "हालको स्थितिहरू"),
    TRANSIT_OVERVIEW("Transit Overview", "गोचर अवलोकन"),
    TRANSIT_CURRENT_INFLUENCES("Current influences on your chart", "तपाईंको कुण्डलीमा हालको प्रभावहरू"),
    TRANSIT_PLANETS_COUNT("Planets Transiting", "गोचरमा ग्रहहरू"),
    TRANSIT_MAJOR_TRANSITS("Major Transits", "मुख्य गोचरहरू"),
    TRANSIT_QUALITY_LABEL("Quality Score", "गुणस्तर स्कोर"),
    TRANSIT_OVERALL_ASSESSMENT("Overall Assessment", "समग्र मूल्यांकन"),
    TRANSIT_RETROGRADE_SYMBOL("Rx", "Rx"),
    TRANSIT_HOUSE_LABEL("House", "भाव"),
    TRANSIT_LABEL("Transit", "गोचर"),
    TRANSIT_NATAL_LABEL("Natal", "जन्म"),
    TRANSIT_NO_PLANETS_TRANSITING("No planets in this house", "यस भावमा कोनै ग्रह छैन"),
    TRANSIT_UPCOMING("Upcoming Transits", "आगामी गोचरहरू"),
    TRANSIT_NO_UPCOMING("No upcoming significant transits", "कुनै आगामी महत्त्वपूर्ण गोचरहरू छैनन्"),
    TRANSIT_TO_NATAL_ASPECTS("Transit to Natal Aspects", "गोचर-जन्म पहलुहरू"),
    TRANSIT_NO_ASPECTS("No transit aspects at this time", "यस समयमा कुनै गोचर पहलुहरू छैनन्"),
    TRANSIT_NO_DATA("No Transit Data", "कुनै गोचर डाटा छैन"),
    TRANSIT_SELECT_CHART("Select a chart to view transits", "गोचरहरू हेर्न कुण्डली छान्नुहोस्"),

    // ============================================
    // ONBOARDING
    // ============================================
    ONBOARDING_WELCOME_TITLE("Welcome to AstroVajra", "AstroVajra मा स्वागत छ"),
    ONBOARDING_WELCOME_SUBTITLE("Your personal Vedic astrology companion", "तपाईंको व्यक्तिगत वैदिक ज्योतिष साथी"),
    ONBOARDING_WELCOME_DESC("Discover the ancient wisdom of Vedic astrology with precision calculations and personalized insights.", "सटीक गणना र व्यक्तिगत अन्तर्दृष्टिका साथ वैदिक ज्योतिषको प्राचीन ज्ञान पत्ता लगाउनुहोस्।"),

    ONBOARDING_TAGLINE("Precision Vedic Astrology", "सटीक वैदिक ज्योतिष"),
    ONBOARDING_FEATURES_OVERVIEW_TITLE("What AstroVajra Offers", "AstroVajra ले के प्रदान गर्दछ"),

    ONBOARDING_FEATURE_CAT_CHART("Complete Chart Analysis", "पूर्ण कुण्डली विश्लेषण"),
    ONBOARDING_FEATURE_CAT_CHART_ITEMS("Rashi & Divisional Charts • Shadbala & Ashtakavarga • All 16 Vargas", "राशि र विभागीय कुण्डलीहरू • षड्बल र अष्टकवर्ग • सबै १६ वर्गहरू"),

    ONBOARDING_FEATURE_CAT_DASHA("Dasha Systems", "दशा प्रणालीहरू"),
    ONBOARDING_FEATURE_CAT_DASHA_ITEMS("Vimshottari & Yogini • Ashtottari & Chara • Kalachakra Dasha", "विम्शोत्तरी र योगिनी • अष्टोत्तरी र चर • कालचक्र दशा"),

    ONBOARDING_FEATURE_CAT_PREDICTION("Predictions & Yogas", "भविष्यवाणी र योगहरू"),
    ONBOARDING_FEATURE_CAT_PREDICTION_ITEMS("200+ Yoga Calculations • Transit Analysis • Matchmaking (Kundali Milan)", "२००+ योग गणनाहरू • गोचर विश्लेषण • कुण्डली मिलान"),

    ONBOARDING_FEATURE_CAT_REMEDY("Remedies & Muhurta", "उपाय र मुहूर्त"),
    ONBOARDING_FEATURE_CAT_REMEDY_ITEMS("Vedic & Lal Kitab Remedies • Auspicious Timing • Panchanga", "वैदिक र लाल किताब उपायहरू • शुभ समय • पञ्चाङ्ग"),

    ONBOARDING_QUICK_START_TITLE("You'll need:", "तपाईंलाई आवश्यक पर्नेछ:"),
    ONBOARDING_QUICK_START_1("Birth date & time", "जन्म मिति र समय"),
    ONBOARDING_QUICK_START_2("Birth location (city)", "जन्म स्थान (शहर)"),
    ONBOARDING_QUICK_START_3("That's all!", "त्यति मात्र हो!"),

    ONBOARDING_LANGUAGE_TITLE("Choose Your Language", "आफ्नो भाषा छान्नुहोस्"),
    ONBOARDING_LANGUAGE_SUBTITLE("Select your preferred language", "आफ्नो मनपर्ने भाषा छान्नुहोस्"),
    ONBOARDING_LANGUAGE_DESC("You can change this later in settings.", "तपाईं यसलाई पछि सेटिङ्समा परिवर्तन गर्न सक्नुहुन्छ।"),

    ONBOARDING_THEME_TITLE("Choose Your Theme", "आफ्नो थिम छान्नुहोस्"),
    ONBOARDING_THEME_SUBTITLE("Select your preferred appearance", "आफ्नो मनपर्ने रूप छान्नुहोस्"),
    ONBOARDING_THEME_DESC("You can change this later in settings.", "तपाईं यसलाई पछि सेटिङ्समा परिवर्तन गर्न सक्नुहुन्छ।"),
    ONBOARDING_THEME_LIGHT("Light", "उज्यालो"),
    ONBOARDING_THEME_DARK("Dark", "अँध्यारो"),
    ONBOARDING_THEME_SYSTEM("System", "प्रणाली"),

    ONBOARDING_FEATURES_TITLE("Powerful Features", "शक्तिशाली सुविधाहरू"),
    ONBOARDING_FEATURES_SUBTITLE("Everything you need for Vedic astrology", "वैदिक ज्योतिषको लागि तपाईंलाई चाहिने सबै"),

    ONBOARDING_FEATURE_CHARTS("Birth Charts", "जन्म कुण्डली"),
    ONBOARDING_FEATURE_CHARTS_DESC("Accurate Vedic birth chart calculations", "सटीक वैदिक जन्म कुण्डली गणना"),
    ONBOARDING_FEATURE_DASHAS("Dashas", "दशाहरू"),
    ONBOARDING_FEATURE_DASHAS_DESC("Complete planetary period analysis", "पूर्ण ग्रह अवधि विश्लेषण"),
    ONBOARDING_FEATURE_TRANSITS("Transits", "गोचरहरू"),
    ONBOARDING_FEATURE_TRANSITS_DESC("Real-time planetary movements", "वास्तविक-समय ग्रह गतिविधि"),
    ONBOARDING_FEATURE_MATCHMAKING("Matchmaking", "कुण्डली मिलान"),
    ONBOARDING_FEATURE_MATCHMAKING_DESC("Kundli Milan compatibility", "कुण्डली मिलान अनुकूलता"),

    ONBOARDING_READY_TITLE("You're All Set!", "तपाईं तयार हुनुहुन्छ!"),
    ONBOARDING_READY_SUBTITLE("Start exploring your cosmic journey", "आफ्नो ब्रह्माण्डीय यात्रा अन्वेषण गर्न सुरु गर्नुहोस्"),
    ONBOARDING_READY_DESC("Create your first birth chart and discover personalized astrological insights.", "आफ्नो पहिलो जन्म कुण्डली बनाउनुहोस् र व्यक्तिगत ज्योतिषीय अन्तर्दृष्टिहरू पत्ता लगाउनुहोस्।"),
    ONBOARDING_CREATE_FIRST_CHART("Create Your First Chart", "तपाईंको पहिलो कुण्डली बनाउनुहोस्"),

    ONBOARDING_BTN_NEXT("Next", "अर्को"),
    ONBOARDING_BTN_BACK("Back", "पछाडि"),
    ONBOARDING_BTN_GET_STARTED("Get Started", "सुरु गर्नुहोस्"),
    ONBOARDING_BTN_SKIP("Skip", "छोड्नुहोस्"),

    // ============================================
    // COMMON TAB TITLES
    // ============================================
    TAB_OVERVIEW("Overview", "अवलोकन"),
    TAB_BY_PLANET("By Planet", "ग्रहानुसार"),
    TAB_BY_HOUSE("By House", "भावानुसार"),
    TAB_ELEMENTS("Elements", "तत्वहरू"),
    TAB_TODAY("Today", "आज"),
    TAB_BIRTH_DAY("Birth Day", "जन्म दिन"),
    TAB_CURRENT_POSITIONS("Current Positions", "हालको स्थिति"),
    TAB_UPCOMING("Upcoming", "आगामी"),
    TAB_ASPECTS("Aspects", "दृष्टिहरू"),
    TAB_SARVASHTAKAVARGA("Sarvashtakavarga", "सर्वाष्टकवर्ग"),

    // ============================================
    // STRENGTH LABELS
    // ============================================
    STRENGTH_EXCELLENT("Excellent", "उत्कृष्ट"),
    STRENGTH_STRONG("Strong", "बलियो"),
    STRENGTH_GOOD("Good", "राम्रो"),
    STRENGTH_AVERAGE("Average", "औसत"),
    STRENGTH_WEAK("Weak", "कमजोर"),
    STRENGTH_BELOW_AVERAGE("Below Average", "औसतमुनि"),

    // ============================================
    // ACCESSIBILITY STRINGS
    // ============================================
    A11Y_EXPAND("Expand", "विस्तार गर्नुहोस्"),
    A11Y_COLLAPSE("Collapse", "संक्षिप्त गर्नुहोस्"),
    A11Y_NAVIGATE_BACK("Navigate back", "पछाडि जानुहोस्"),
    A11Y_SHOW_INFO("Show information", "जानकारी देखाउनुहोस्"),
    LABEL_DASH("-", "-"),

    // ============================================
    // ADDITIONAL ACCESSIBILITY STRINGS
    // ============================================
    ACC_FULLSCREEN("Fullscreen", "पूर्णस्क्रीन"),
    ACC_COLLAPSE("Collapse section", "खण्ड संक्षिप्त गर्नुहोस्"),
    ACC_EXPAND("Expand section", "खण्ड विस्तार गर्नुहोस्"),
    ACC_VIEW_DETAILS("View details", "विवरण हेर्नुहोस्"),

    // ============================================
    // DIGNITY STATUS STRINGS
    // ============================================
    DIGNITY_EXALTED_STATUS("Exalted", "उच्च"),
    DIGNITY_DEBILITATED_STATUS("Debilitated", "नीच"),
    DIGNITY_OWN_SIGN_STATUS("Own Sign", "स्वगृह"),
    DIGNITY_NEUTRAL_STATUS("Neutral", "तटस्थ"),

    // ============================================
    // BUTTON & FEATURE STRINGS
    // ============================================
    BTN_VIEW_DETAILS("View Details", "विवरण हेर्नुहोस्"),
    REPORT_PLANET("planet", "ग्रह"),
    
    // ============================================
    // ERROR STRINGS
    // ============================================
    ERROR_CALCULATION("Calculation Error", "गणना त्रुटि"),

    // ============================================
    // HOME TAB - REVAMPED UI
    // ============================================
    HOME_EXPLORE_FEATURES("Explore your chart features", "आफ्नो कुण्डली सुविधाहरू अन्वेषण गर्नुहोस्"),
    QUICK_ACTIONS("Quick Actions", "द्रुत कार्यहरू"),
    TODAYS_SNAPSHOT("Today's Snapshot", "आजको झलक"),
    VIEW_CURRENT_TRANSITS("View current planetary positions", "हालको ग्रह स्थितिहरू हेर्नुहोस्"),
    CURRENT_MAHA_DASHA("Current Maha Dasha", "हालको महा दशा"),
    HOME_DASHA_LABEL("Vimsottari Dasha", "विंशोत्तरी दशा"),
    REMAINING_PERIOD_YEARS("%s years, %s months remaining", "%s वर्ष, %s महिना बाँकी"),
    REMAINING_PERIOD_MONTHS("%s months remaining", "%s महिना बाँकी"),
    REMAINING_PERIOD_DAYS("%s days remaining", "%s दिन बाँकी"),
    REMAINING_PERIOD_MONTHS_DAYS("%s months, %s days remaining", "%s महिना, %s दिन बाँकी"),
    DURATION_YEARS_FMT("%d years", "%d वर्ष"),
    DURATION_YEAR_FMT("1 year", "१ वर्ष"),
    TAP_TO_VIEW_DASHAS("Tap to view your Dasha timeline", "आफ्नो दशा समयरेखा हेर्न ट्याप गर्नुहोस्"),
    
    // Feature Categories
    CATEGORY_CORE_ANALYSIS("Core Analysis", "मुख्य विश्लेषण"),
    CATEGORY_CORE_DESC("Charts, planets, nakshatras & vargas", "कुण्डली, ग्रह, नक्षत्र र वर्गहरू"),
    CATEGORY_TIMING_SYSTEMS("Timing Systems", "समय प्रणाली"),
    CATEGORY_TIMING_DESC("Dashas & planetary periods", "दशा र ग्रह अवधिहरू"),
    CATEGORY_PREDICTIONS("Predictions & Yogas", "भविष्यवाणी र योग"),
    CATEGORY_PREDICTIONS_DESC("Life predictions & combinations", "जीवन भविष्यवाणी र संयोजन"),
    CATEGORY_STRENGTH_ANALYSIS("Strength Analysis", "बल विश्लेषण"),
    CATEGORY_STRENGTH_DESC("Shadbala & planetary strength", "षड्बल र ग्रह बल"),
    CATEGORY_ADVANCED("Advanced Techniques", "उन्नत प्रविधि"),
    CATEGORY_ADVANCED_DESC("Jaimini, Nadi & specialized systems", "जैमिनी, नाडी र विशेष प्रणाली"),
    CATEGORY_REMEDIAL("Remedies & Compatibility", "उपाय र अनुकूलता"),
    CATEGORY_REMEDIAL_DESC("Remedies, matching & muhurta", "उपाय, मिलान र मुहूर्त");

}
