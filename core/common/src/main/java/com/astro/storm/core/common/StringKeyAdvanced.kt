package com.astro.storm.core.common

/**
 * String keys for advanced Vedic astrology features
 * Including: Shoola Dasha, Kakshya Transit, Ashtavarga Transit, Nadi Amsha, Prashna Enhancements
 */
enum class StringKeyAdvanced(override val en: String, override val ne: String) : StringKeyInterface {

    // ============================================
    // SHOOLA DASHA STRINGS
    // ============================================
    SHOOLA_TITLE("Shoola Dasha", "शूल दशा"),
    SHOOLA_SUBTITLE("Health & Critical Period Analysis", "स्वास्थ्य र गम्भीर अवधि विश्लेषण"),
    SHOOLA_DESC("Jaimini timing system for health, accidents, and critical life events", "स्वास्थ्य, दुर्घटना, र गम्भीर जीवन घटनाहरूको लागि जैमिनी समय प्रणाली"),
    SHOOLA_TAB_OVERVIEW("Overview", "सिंहावलोकन"),
    SHOOLA_TAB_PERIODS("Periods", "अवधिहरू"),
    SHOOLA_TAB_HEALTH("Health", "स्वास्थ्य"),
    SHOOLA_TAB_REMEDIES("Remedies", "उपाय"),

    // Tri-Murti
    SHOOLA_TRI_MURTI("Tri-Murti Analysis", "त्रिमूर्ति विश्लेषण"),
    SHOOLA_BRAHMA("Brahma (Creator)", "ब्रह्मा (सृष्टिकर्ता)"),
    SHOOLA_BRAHMA_DESC("Protective benefic in trikona positions", "त्रिकोण स्थितिहरूमा सुरक्षात्मक शुभ ग्रह"),
    SHOOLA_RUDRA("Rudra (Destroyer)", "रुद्र (संहारक)"),
    SHOOLA_RUDRA_DESC("Primary malefic controlling health matters", "स्वास्थ्य मामिलाहरू नियन्त्रण गर्ने प्राथमिक पाप ग्रह"),
    SHOOLA_MAHESHWARA("Maheshwara (Death Lord)", "महेश्वर (मृत्युको स्वामी)"),
    SHOOLA_MAHESHWARA_DESC("8th lord - death and transformation significator", "आठौं भाव स्वामी - मृत्यु र रूपान्तरणको कारक"),
    SHOOLA_PRIMARY_RUDRA("Primary Rudra", "प्राथमिक रुद्र"),
    SHOOLA_SECONDARY_RUDRA("Secondary Rudra", "द्वितीयक रुद्र"),
    SHOOLA_RUDRA_STRENGTH("Rudra Strength", "रुद्र शक्ति"),
    SHOOLA_BRAHMA_STRENGTH("Brahma Strength", "ब्रह्मा शक्ति"),

    // Direction
    SHOOLA_DIRECTION("Dasha Direction", "दशा दिशा"),
    SHOOLA_DIRECT("Direct (Zodiacal)", "प्रत्यक्ष (राशिक्रम)"),
    SHOOLA_REVERSE("Reverse (Anti-zodiacal)", "विपरीत (राशिविरुद्ध)"),
    SHOOLA_STARTING_SIGN("Starting Sign", "प्रारम्भिक राशि"),

    // Periods
    SHOOLA_CURRENT_PERIOD("Current Shoola Dasha", "वर्तमान शूल दशा"),
    SHOOLA_PERIOD_DURATION("Duration: %d years", "अवधि: %d वर्ष"),
    SHOOLA_PERIOD_PROGRESS("Progress: %.1f%%", "प्रगति: %.1f%%"),
    SHOOLA_SIGN_LORD("Sign Lord", "राशि स्वामी"),
    SHOOLA_PERIOD_NATURE("Period Nature", "अवधि प्रकृति"),
    SHOOLA_MAHADASHA("Mahadasha", "महादशा"),
    SHOOLA_ANTARDASHA("Antardasha", "अन्तर्दशा"),
    SHOOLA_SUB_PERIOD("Sub-period", "उप-अवधि"),
    SHOOLA_ALL_PERIODS("All Shoola Periods", "सबै शूल अवधिहरू"),
    SHOOLA_CURRENT("Current", "वर्तमान"),
    SHOOLA_PAST("Past", "बितेको"),
    SHOOLA_FUTURE("Future", "भविष्य"),

    // Period Nature
    SHOOLA_NATURE_FAVORABLE("Favorable", "अनुकूल"),
    SHOOLA_NATURE_SUPPORTIVE("Supportive", "सहायक"),
    SHOOLA_NATURE_MIXED("Mixed", "मिश्रित"),
    SHOOLA_NATURE_CHALLENGING("Challenging", "चुनौतीपूर्ण"),
    SHOOLA_NATURE_VERY_CHALLENGING("Very Challenging", "अत्यन्त चुनौतीपूर्ण"),

    // Health Severity
    SHOOLA_SEVERITY_CRITICAL("Critical", "गम्भीर"),
    SHOOLA_SEVERITY_HIGH("High Risk", "उच्च जोखिम"),
    SHOOLA_SEVERITY_MODERATE("Moderate Risk", "मध्यम जोखिम"),
    SHOOLA_SEVERITY_LOW("Low Risk", "न्यून जोखिम"),
    SHOOLA_SEVERITY_MINIMAL("Minimal Risk", "न्यूनतम जोखिम"),
    SHOOLA_SEVERITY_NONE("No Specific Risk", "कुनै विशेष जोखिम छैन"),
    SHOOLA_HEALTH_SEVERITY("Health Vigilance Level", "स्वास्थ्य सतर्कता स्तर"),

    // Health Analysis
    SHOOLA_HEALTH_CONCERNS("Health Concerns", "स्वास्थ्य चिन्ताहरू"),
    SHOOLA_BODY_PARTS("Affected Body Parts", "प्रभावित शरीर भागहरू"),
    SHOOLA_PRECAUTIONS("Precautions", "सावधानीहरू"),
    SHOOLA_VULNERABILITY_PERIODS("Vulnerability Periods", "संवेदनशील अवधिहरू"),
    SHOOLA_UPCOMING_CRITICAL("Upcoming Critical Periods", "आउँदा गम्भीर अवधिहरू"),
    SHOOLA_NO_CRITICAL("No critical periods in next 5 years", "अर्को ५ वर्षमा कुनै गम्भीर अवधि छैन"),

    // Longevity
    SHOOLA_LONGEVITY("Longevity Assessment", "आयु मूल्यांकन"),
    SHOOLA_LONGEVITY_CATEGORY("Longevity Category", "आयु वर्ग"),
    SHOOLA_LONGEVITY_RANGE("Estimated Range", "अनुमानित दायरा"),
    SHOOLA_SUPPORTING_FACTORS("Protective Factors", "सुरक्षात्मक कारकहरू"),
    SHOOLA_CHALLENGING_FACTORS("Challenging Factors", "चुनौतीपूर्ण कारकहरू"),
    SHOOLA_BALARISHTA("Balarishta (0-8 years)", "बालारिष्ट (०-८ वर्ष)"),
    SHOOLA_ALPAYU("Alpayu - Short (8-32 years)", "अल्पायु (८-३२ वर्ष)"),
    SHOOLA_MADHYAYU("Madhyayu - Medium (32-70 years)", "मध्यायु (३२-७० वर्ष)"),
    SHOOLA_POORNAYU("Poornayu - Full (70-100 years)", "पूर्णायु (७०-१०० वर्ष)"),
    SHOOLA_AMITAYU("Amitayu - Extended (100+ years)", "अमितायु (१००+ वर्ष)"),

    // Remedies
    SHOOLA_REMEDY_MANTRA("Mantra Remedy", "मन्त्र उपाय"),
    SHOOLA_REMEDY_PUJA("Puja Remedy", "पूजा उपाय"),
    SHOOLA_REMEDY_DONATION("Donation Remedy", "दान उपाय"),
    SHOOLA_REMEDY_FASTING("Fasting Remedy", "व्रत उपाय"),
    SHOOLA_REMEDY_GEMSTONE("Gemstone Remedy", "रत्न उपाय"),
    SHOOLA_REMEDY_YANTRA("Yantra Remedy", "यन्त्र उपाय"),
    SHOOLA_REMEDY_LIFESTYLE("Lifestyle Change", "जीवनशैली परिवर्तन"),
    SHOOLA_BEST_DAY("Best Day", "उत्तम दिन"),
    SHOOLA_DEITY("Deity", "देवता"),
    SHOOLA_EFFECTIVENESS("Effectiveness", "प्रभावकारिता"),
    SHOOLA_MAHAMRITYUNJAYA("Mahamrityunjaya Mantra", "महामृत्युञ्जय मन्त्र"),
    SHOOLA_MAHAMRITYUNJAYA_DESC("Most powerful mantra for health protection and longevity", "स्वास्थ्य सुरक्षा र दीर्घायुको लागि सबैभन्दा शक्तिशाली मन्त्र"),

    // System Info
    SHOOLA_SYSTEM_APPLICABILITY("System Applicability", "प्रणाली प्रयोज्यता"),
    SHOOLA_APPLICABILITY_HIGH("Highly Applicable", "अत्यधिक प्रयोज्य"),
    SHOOLA_APPLICABILITY_MEDIUM("Moderately Applicable", "मध्यम प्रयोज्य"),
    SHOOLA_APPLICABILITY_LOW("Less Applicable", "कम प्रयोज्य"),
    SHOOLA_INFO_TITLE("About Shoola Dasha", "शूल दशाको बारेमा"),
    SHOOLA_INFO_DESC("Shoola Dasha is a Jaimini sign-based dasha system specifically used for timing health issues, accidents, and critical life events. The word 'Shoola' means 'thorn' or 'pain.' This system identifies Rudra (malefic controller) and tracks periods of heightened health vigilance.", "शूल दशा जैमिनी राशि-आधारित दशा प्रणाली हो जुन विशेष गरी स्वास्थ्य समस्याहरू, दुर्घटनाहरू, र गम्भीर जीवन घटनाहरूको समय निर्धारण गर्न प्रयोग गरिन्छ। 'शूल' शब्दको अर्थ 'काँडा' वा 'पीडा' हो। यस प्रणालीले रुद्र (पाप नियन्त्रक) पहिचान गर्छ र स्वास्थ्य सतर्कताका अवधिहरू ट्र्याक गर्छ।"),
    SHOOLA_VEDIC_REF("Vedic Reference: Jaimini Sutras, BPHS Ayurdaya chapter", "वैदिक सन्दर्भ: जैमिनी सूत्र, बृहत् पाराशर होरा शास्त्र आयुर्दाय अध्याय"),
    SHOOLA_UNABLE_CALCULATE("Unable to calculate Shoola Dasha", "शूल दशा गणना गर्न सकिएन"),
    SHOOLA_SIGNIFICANT_PLANETS("Significant Planets", "महत्त्वपूर्ण ग्रहहरू"),

    // ============================================
    // KAKSHYA TRANSIT STRINGS
    // ============================================
    KAKSHYA_TITLE("Kakshya Transit", "कक्ष्या गोचर"),
    KAKSHYA_SUBTITLE("High-precision micro-transit timing", "उच्च-सटीक सूक्ष्म-गोचर समय"),
    KAKSHYA_TAB_CURRENT("Current", "वर्तमान"),
    KAKSHYA_TAB_PLANETS("Planets", "ग्रहहरू"),
    KAKSHYA_TAB_TIMELINE("Timeline", "समयरेखा"),
    KAKSHYA_TAB_FAVORABLE("Favorable", "अनुकूल"),
    KAKSHYA_OVERALL_QUALITY("Overall Quality", "समग्र गुणस्तर"),
    KAKSHYA_PLANET_POSITIONS("Planet Positions", "ग्रहहरूको स्थिति"),
    KAKSHYA_LORD("Kakshya Lord", "कक्ष्या स्वामी"),
    KAKSHYA_NEXT("Next Kakshya", "अर्को कक्ष्या"),
    KAKSHYA_UPCOMING_FAVORABLE("Upcoming Favorable Periods", "आगामी अनुकूल अवधिहरू"),
    KAKSHYA_UPCOMING_CHANGES("Next Kakshya Changes", "आगामी कक्ष्या परिवर्तनहरू"),
    KAKSHYA_SELECT_PLANET("Select a planet to see details", "विवरण हेर्न ग्रह छान्नुहोस्"),
    KAKSHYA_NO_CHANGES("No upcoming changes in next 7 days", "आगामी ७ दिनमा कुनै परिवर्तन छैन"),
    KAKSHYA_NO_FAVORABLE("No favorable micro-transits found for the next 30 days", "अर्को ३० दिनको लागि कुनै अनुकूल सूक्ष्म-गोचर भेटिएन"),
    KAKSHYA_QUALITY_EXCELLENT("Excellent", "अत्युत्तम"),
    KAKSHYA_QUALITY_GOOD("Good", "राम्रो"),
    KAKSHYA_QUALITY_MODERATE("Moderate", "मध्यम"),
    KAKSHYA_QUALITY_POOR("Poor", "कमजोर"),
    KAKSHYA_DESC("Precise micro-transit timing using 8 divisions per sign (3°45' each)", "प्रत्येक राशिमा ८ विभाजन (३°४५' प्रत्येक) प्रयोग गरी सूक्ष्म गोचर समय"),

    // Kakshya Lords (in order)
    KAKSHYA_LORD_SATURN("Saturn Kakshya", "शनि कक्ष्या"),
    KAKSHYA_LORD_JUPITER("Jupiter Kakshya", "गुरु कक्ष्या"),
    KAKSHYA_LORD_MARS("Mars Kakshya", "मंगल कक्ष्या"),
    KAKSHYA_LORD_SUN("Sun Kakshya", "सूर्य कक्ष्या"),
    KAKSHYA_LORD_VENUS("Venus Kakshya", "शुक्र कक्ष्या"),
    KAKSHYA_LORD_MERCURY("Mercury Kakshya", "बुध कक्ष्या"),
    KAKSHYA_LORD_MOON("Moon Kakshya", "चन्द्र कक्ष्या"),
    KAKSHYA_LORD_LAGNA("Lagna Kakshya", "लग्न कक्ष्या"),

    // Kakshya Details
    KAKSHYA_CURRENT_POSITION("Current Kakshya Position", "वर्तमान कक्ष्या स्थिति"),
    KAKSHYA_SIGN("Sign", "राशि"),
    KAKSHYA_DEGREE_RANGE("Degree Range", "डिग्री दायरा"),
    KAKSHYA_RULER("Kakshya Ruler", "कक्ष्या स्वामी"),
    KAKSHYA_BINDU_STATUS("Bindu Status", "बिन्दु स्थिति"),
    KAKSHYA_HAS_BINDU("Has Bindu ✓", "बिन्दु छ ✓"),
    KAKSHYA_NO_BINDU("No Bindu ✗", "बिन्दु छैन ✗"),
    KAKSHYA_TRANSIT_QUALITY("Transit Quality", "गोचर गुणस्तर"),
    KAKSHYA_EXCELLENT("Excellent", "उत्कृष्ट"),
    KAKSHYA_GOOD("Good", "राम्रो"),
    KAKSHYA_MODERATE("Moderate", "मध्यम"),
    KAKSHYA_POOR("Poor", "कमजोर"),

    // Transit Predictions
    KAKSHYA_ENTRY_TIME("Entry Time", "प्रवेश समय"),
    KAKSHYA_EXIT_TIME("Exit Time", "निकास समय"),
    KAKSHYA_DURATION("Duration", "अवधि"),
    KAKSHYA_NEXT_CHANGE("Next Kakshya Change", "अर्को कक्ष्या परिवर्तन"),
    KAKSHYA_FAVORABLE_PERIODS("Favorable Kakshya Periods", "अनुकूल कक्ष्या अवधिहरू"),
    KAKSHYA_CRITICAL_TRANSITS("Critical Kakshya Transits", "गम्भीर कक्ष्या गोचरहरू"),

    // Information
    KAKSHYA_INFO_TITLE("About Kakshya System", "कक्ष्या प्रणालीको बारेमा"),
    KAKSHYA_INFO_DESC("Kakshya divides each sign into 8 parts of 3°45' each, ruled by Saturn, Jupiter, Mars, Sun, Venus, Mercury, Moon, and Lagna. Combined with Ashtakavarga, it provides precise timing for transit effects.", "कक्ष्याले प्रत्येक राशिलाई ३°४५' को ८ भागमा विभाजन गर्छ, जुन शनि, गुरु, मंगल, सूर्य, शुक्र, बुध, चन्द्र, र लग्नले शासन गर्छन्। अष्टकवर्गसँग मिलाएर, यसले गोचर प्रभावको सटीक समय प्रदान गर्छ।"),
    KAKSHYA_VEDIC_REF("Vedic Reference: BPHS Ashtakavarga chapters, Jataka Parijata", "वैदिक सन्दर्भ: बृहत् पाराशर होरा शास्त्र अष्टकवर्ग अध्यायहरू, जातक पारिजात"),
    KAKSHYA_UNABLE_CALCULATE("Unable to calculate Kakshya transits", "कक्ष्या गोचर गणना गर्न सकिएन"),

    // ============================================
    // ASHTAVARGA TRANSIT PREDICTION STRINGS
    // ============================================
    ASHTAVARGA_TRANSIT_TITLE("Ashtavarga Transit Predictions", "अष्टकवर्ग गोचर भविष्यवाणी"),
    ASHTAVARGA_TRANSIT_SUBTITLE("Transit Intensity by Bindu Score", "बिन्दु स्कोरद्वारा गोचर तीव्रता"),
    ASHTAVARGA_TRANSIT_DESC("Predict event intensity when planets transit through bindu-rich or bindu-poor signs", "बिन्दुले भरिएको वा बिन्दु कम भएका राशिहरूबाट ग्रह गोचर हुँदा घटना तीव्रता भविष्यवाणी गर्नुहोस्"),
    ASHTAVARGA_TAB_OVERVIEW("Overview", "सिंहावलोकन"),
    ASHTAVARGA_TAB_BY_PLANET("By Planet", "ग्रहानुसार"),
    ASHTAVARGA_TAB_TIMELINE("Timeline", "समयरेखा"),
    ASHTAVARGA_TAB_ANALYSIS("Analysis", "विश्लेषण"),

    // Bindu Scores
    ASHTAVARGA_SAV_SCORE("SAV Score", "सर्वाष्टकवर्ग स्कोर"),
    ASHTAVARGA_BAV_SCORE("BAV Score", "भिन्नाष्टकवर्ग स्कोर"),
    ASHTAVARGA_HIGH_BINDU("High Bindu (30+)", "उच्च बिन्दु (३०+)"),
    ASHTAVARGA_MEDIUM_BINDU("Medium Bindu (25-30)", "मध्यम बिन्दु (२५-३०)"),
    ASHTAVARGA_LOW_BINDU("Low Bindu (<25)", "न्यून बिन्दु (<२५)"),
    ASHTAVARGA_BINDU_THRESHOLD("Bindu Threshold", "बिन्दु सीमा"),

    // Transit Analysis
    ASHTAVARGA_CURRENT_TRANSIT("Current Transit Analysis", "वर्तमान गोचर विश्लेषण"),
    ASHTAVARGA_PLANET_POSITION("Planet Position", "ग्रह स्थिति"),
    ASHTAVARGA_SIGN_SAV("Sign SAV", "राशि सर्वाष्टकवर्ग"),
    ASHTAVARGA_SIGN_BAV("Planet's BAV", "ग्रहको भिन्नाष्टकवर्ग"),
    ASHTAVARGA_TRANSIT_STRENGTH("Transit Strength", "गोचर बल"),
    ASHTAVARGA_VERY_STRONG("Very Strong (30+ SAV)", "अत्यन्त बलियो (३०+ सर्वाष्टकवर्ग)"),
    ASHTAVARGA_STRONG("Strong (26-30 SAV)", "बलियो (२६-३० सर्वाष्टकवर्ग)"),
    ASHTAVARGA_AVERAGE("Average (22-25 SAV)", "औसत (२२-२५ सर्वाष्टकवर्ग)"),
    ASHTAVARGA_WEAK("Weak (<22 SAV)", "कमजोर (<२२ सर्वाष्टकवर्ग)"),

    // Predictions
    ASHTAVARGA_FAVORABLE_SIGNS("Favorable Transit Signs", "अनुकूल गोचर राशिहरू"),
    ASHTAVARGA_UNFAVORABLE_SIGNS("Unfavorable Transit Signs", "प्रतिकूल गोचर राशिहरू"),
    ASHTAVARGA_UPCOMING_TRANSITS("Upcoming Important Transits", "आउँदा महत्त्वपूर्ण गोचरहरू"),
    ASHTAVARGA_EVENT_PROBABILITY("Event Probability", "घटना सम्भावना"),
    ASHTAVARGA_HIGH_PROBABILITY("High Probability", "उच्च सम्भावना"),
    ASHTAVARGA_MEDIUM_PROBABILITY("Medium Probability", "मध्यम सम्भावना"),
    ASHTAVARGA_LOW_PROBABILITY("Low Probability", "न्यून सम्भावना"),

    // Reduction Rules
    ASHTAVARGA_REDUCTION_RULES("Reduction Rules Applied", "घटाउने नियमहरू लागू"),
    ASHTAVARGA_TRIKONA_REDUCTION("Trikona Reduction", "त्रिकोण घटाउ"),
    ASHTAVARGA_EKADHIPATYA_REDUCTION("Ekadhipatya Reduction", "एकाधिपत्य घटाउ"),
    ASHTAVARGA_REDUCED_SAV("Reduced SAV (Shodya Pinda)", "घटाइएको सर्वाष्टकवर्ग (शोध्य पिण्ड)"),

    // Information
    ASHTAVARGA_TRANSIT_INFO_TITLE("About Ashtavarga Transit", "अष्टकवर्ग गोचरको बारेमा"),
    ASHTAVARGA_TRANSIT_INFO_DESC("Ashtavarga Transit Predictions use the bindu contribution from each planet to predict transit intensity. High SAV scores (30+) indicate favorable periods, while low scores (<25) suggest caution.", "अष्टकवर्ग गोचर भविष्यवाणीहरूले गोचर तीव्रता भविष्यवाणी गर्न प्रत्येक ग्रहबाट बिन्दु योगदान प्रयोग गर्छन्। उच्च सर्वाष्टकवर्ग स्कोर (३०+) अनुकूल अवधिहरू संकेत गर्छन्, जबकि कम स्कोर (<२५) सावधानी सुझाव दिन्छन्।"),
    ASHTAVARGA_TRANSIT_VEDIC_REF("Vedic Reference: BPHS Ashtakavarga chapters, Jataka Parijata", "वैदिक सन्दर्भ: बृहत् पाराशर होरा शास्त्र अष्टकवर्ग अध्यायहरू, जातक पारिजात"),
    ASHTAVARGA_TRANSIT_UNABLE("Unable to calculate Ashtavarga transits", "अष्टकवर्ग गोचर गणना गर्न सकिएन"),

    // ============================================
    // NADI AMSHA (D-150) STRINGS
    // ============================================
    NADI_TITLE("Nadi Amsha (D-150)", "नाडी अंश (D-150)"),
    NADI_SUBTITLE("150th Division Precision Timing", "१५०औं विभाजन सटीक समय"),
    NADI_DESC("High-precision divisional analysis for exact event timing and birth time rectification", "सटीक घटना समय र जन्म समय शुद्धिकरणको लागि उच्च-सटीक विभाजन विश्लेषण"),
    NADI_TAB_OVERVIEW("Overview", "सिंहावलोकन"),
    NADI_TAB_POSITIONS("Positions", "स्थितिहरू"),
    NADI_TAB_TIMING("Timing", "समय"),
    NADI_TAB_RECTIFICATION("Rectification", "शुद्धिकरण"),

    // Nadi Types
    NADI_ADI("Adi Nadi", "आदि नाडी"),
    NADI_MADHYA("Madhya Nadi", "मध्य नाडी"),
    NADI_ANTYA("Antya Nadi", "अन्त्य नाडी"),
    NADI_PAIR("Nadi Pair", "नाडी जोडी"),
    NADI_MALE_FEMALE_BALANCE("Male-Female Energy Balance", "पुरुष-स्त्री ऊर्जा सन्तुलन"),

    // Positions
    NADI_PLANET_POSITION("Planet Nadi Position", "ग्रह नाडी स्थिति"),
    NADI_DEGREE("Nadi Degree", "नाडी डिग्री"),
    NADI_NUMBER("Nadi Number (1-150)", "नाडी नम्बर (१-१५०)"),
    NADI_LORD("Nadi Lord", "नाडी स्वामी"),
    NADI_SUB_LORD("Nadi Sub-Lord", "नाडी उप-स्वामी"),

    // Timing
    NADI_PRECISE_TIMING("Precise Event Timing", "सटीक घटना समय"),
    NADI_DASHA_TIMING("Dasha-Antardasha Fine-Tuning", "दशा-अन्तर्दशा सूक्ष्म समायोजन"),
    NADI_TRANSIT_TIMING("Transit Timing Enhancement", "गोचर समय वृद्धि"),
    NADI_EVENT_WINDOW("Event Window", "घटना समय सीमा"),
    NADI_MARRIAGE_TIMING("Marriage Event Timing", "विवाह घटना समय"),
    NADI_CAREER_TIMING("Career Event Timing", "क्यारियर घटना समय"),

    // Rectification
    NADI_RECTIFICATION_TITLE("Birth Time Rectification", "जन्म समय शुद्धिकरण"),
    NADI_CURRENT_TIME("Current Birth Time", "वर्तमान जन्म समय"),
    NADI_SUGGESTED_TIME("Suggested Corrected Time", "सुझाव गरिएको शुद्ध समय"),
    NADI_CONFIDENCE("Rectification Confidence", "शुद्धिकरण विश्वास"),
    NADI_EVENTS_VERIFIED("Events Verified", "प्रमाणित घटनाहरू"),
    NADI_ENTER_EVENT("Enter Known Life Events", "ज्ञात जीवन घटनाहरू प्रविष्ट गर्नुहोस्"),
    NADI_NAME("Nadi Name", "नाडी नाम"),
    NADI_RECTIFICATION_DESC("Rectification Description", "शुद्धिकरण विवरण"),

    // Information
    NADI_INFO_TITLE("About Nadi Amsha", "नाडी अंशको बारेमा"),
    NADI_INFO_DESC("Nadi Amsha (D-150) is the finest divisional chart, dividing each sign into 150 parts of 12 arc-minutes each. This level of precision allows for exact event timing and birth time rectification within minutes.", "नाडी अंश (D-150) सबैभन्दा सूक्ष्म विभाजन चार्ट हो, जसले प्रत्येक राशिलाई १२ आर्क-मिनेटको १५० भागमा विभाजन गर्छ। यो सटीकताको स्तरले मिनेटभित्र सटीक घटना समय र जन्म समय शुद्धिकरणको अनुमति दिन्छ।"),
    NADI_VEDIC_REF("Vedic Reference: Brighu Nadi, Dhruva Nadi, South Indian Nadi traditions", "वैदिक सन्दर्भ: भृगु नाडी, ध्रुव नाडी, दक्षिण भारतीय नाडी परम्पराहरू"),
    NADI_UNABLE_CALCULATE("Unable to calculate Nadi Amsha", "नाडी अंश गणना गर्न सकिएन"),
    NADI_SUMMARY_FMT("Ascendant Nadi is ruled by %1$s. This indicates %2$s dominance in the chart's finest division.", "लग्न नाडी %1$s द्वारा शासित छ। यसले कुण्डलीको सूक्ष्म विभाजनमा %2$s प्रभुत्व संकेत गर्दछ।"),
    NADI_PLANET_FMT("Nadi #%1$d (%2$s)", "नाडी #%1$d (%2$s)"),
    MIN_LABEL("min", "मिनेट"),
    PRASHNA_AI_PROMPT_INTRO("Please provide a deeper Vedic astrology interpretation of this Prashna (horary) chart analysis. Be insightful, practical, and compassionate in your guidance.", "कृपया यस प्रश्न (होरारी) कुण्डली विश्लेषणको गहिरो वैदिक ज्योतिष व्याख्या प्रदान गर्नुहोस्। आफ्नो मार्गदर्शनमा अन्तर्दृष्टिपूर्ण, व्यावहारिक र करुणाशील हुनुहोस्।"),
    PRASHNA_AI_QUESTION("## Question", "## प्रश्न"),
    PRASHNA_AI_VERDICT("## Initial Verdict", "## प्रारम्भिक फैसला"),
    PRASHNA_AI_MOON("## Moon Analysis", "## चन्द्र विश्लेषण"),
    PRASHNA_AI_LAGNA("## Lagna Analysis", "## लग्न विश्लेषण"),
    PRASHNA_AI_TIMING("## Timing Prediction", "## समय भविष्यवाणी"),
    PRASHNA_AI_YOGAS("## Special Prashna Yogas", "## विशेष प्रश्न योग"),
    PRASHNA_AI_FACTORS("## Supporting Factors", "## सहायक कारक"),
    PRASHNA_AI_CHALLENGES("## Challenges/Opposing Factors", "## चुनौती/विपरीत कारक"),
    PRASHNA_AI_PROVIDE("Please provide:", "कृपया प्रदान गर्नुहोस्:"),
    PRASHNA_AI_PROVIDE_1("1. Your overall interpretation of this Prashna chart", "१. यस प्रश्न कुण्डलीको तपाईंको समग्र व्याख्या"),
    PRASHNA_AI_PROVIDE_2("2. Key insights the querent should know", "२. प्रश्नकर्ताले जान्नुपर्ने मुख्य अन्तर्दृष्टि"),
    PRASHNA_AI_PROVIDE_3("3. What the Moon's position reveals about their mindset", "३. चन्द्रमाको स्थितिले उनीहरूको मानसिकताको बारेमा के प्रकट गर्छ"),
    PRASHNA_AI_PROVIDE_4("4. Practical advice based on the astrological factors", "४. ज्योतिषीय कारकहरूमा आधारित व्यावहारिक सल्लाह"),
    PRASHNA_AI_PROVIDE_5("5. Any additional considerations or cautions", "५. कुनै थप विचार वा सावधानीहरू"),
    PRASHNA_AI_CATEGORY("Category: %s", "श्रेणी: %s"),
    PRASHNA_AI_TIME("Time: %s", "समय: %s"),
    PRASHNA_AI_VERDICT_LABEL("Verdict: %s", "फैसला: %s"),
    PRASHNA_AI_CONFIDENCE("Confidence: %s%%", "आत्मविश्वास: %s%%"),
    PRASHNA_AI_CERTAINTY("Certainty: %s", "निश्चितता: %s"),
    PRASHNA_AI_RISING_SIGN("Rising Sign: %s", "उदित राशि: %s"),
    PRASHNA_AI_LAGNA_LORD("Lagna Lord: %s", "लग्नेश: %s"),
    PRASHNA_AI_LORD_POS("Lagna Lord Position: House %d", "लग्नेश स्थिति: भाव %d"),
    PRASHNA_AI_CONDITION("Condition: %s", "अवस्था: %s"),
    PRASHNA_AI_PLANETS_LAGNA("Planets in Lagna: %s", "लग्नमा ग्रहहरू: %s"),
    PRASHNA_AI_EST_TIME("Estimated Time: %s", "अनुमानित समय: %s"),
    PRASHNA_AI_METHOD("Method: %s", "विधि: %s"),

    // ============================================
    // PRASHNA ENHANCEMENTS STRINGS
    // ============================================
    PRASHNA_ENHANCED_TITLE("Enhanced Prashna", "उन्नत प्रश्न"),
    PRASHNA_ENHANCED_SUBTITLE("Advanced Horary Techniques", "उन्नत होरारी प्रविधिहरू"),
    PRASHNA_ENHANCED_DESC("Complete Prashna analysis with Arudha, divisional charts, Itthasala aspects, and specialized predictions", "आरूढ, विभागीय चार्टहरू, इत्थशाला पक्षहरू, र विशेष भविष्यवाणीहरू सहित पूर्ण प्रश्न विश्लेषण"),
    PRASHNA_TAB_OVERVIEW("Overview", "सिंहावलोकन"),
    PRASHNA_TAB_ARUDHA("Arudha", "आरूढ"),
    PRASHNA_TAB_ASPECTS("Aspects", "पक्षहरू"),
    PRASHNA_TAB_SPECIAL("Special", "विशेष"),

    // Arudha Lagna for Prashna
    PRASHNA_ARUDHA_LAGNA("Prashna Arudha Lagna", "प्रश्न आरूढ लग्न"),
    PRASHNA_ARUDHA_DESC("Manifestation point for the question", "प्रश्नको प्रकटीकरण बिन्दु"),
    PRASHNA_ARUDHA_CALCULATION("Arudha Calculation", "आरूढ गणना"),

    // Itthasala Aspects
    PRASHNA_ITTHASALA("Itthasala (Application)", "इत्थशाला"),
    PRASHNA_ITTHASALA_DESC("Moon applying to other planets - key for timing", "चन्द्रमा अन्य ग्रहहरूमा आवेदन - समयको लागि महत्त्वपूर्ण"),
    PRASHNA_ISARAFA("Isarafa (Separation)", "ईसराफ (अलगाव)"),
    PRASHNA_ISARAFA_DESC("Moon separating from planets - past influence", "चन्द्रमा ग्रहहरूबाट अलग हुँदै - बितेको प्रभाव"),
    PRASHNA_NAKTA("Nakta (Translation)", "नक्त (अनुवाद)"),
    PRASHNA_NAKTA_DESC("Light transferred through intermediary planet", "मध्यस्थ ग्रहमार्फत प्रकाश स्थानान्तरण"),
    PRASHNA_YAMAYA("Yamaya (Prohibition)", "यमया (निषेध)"),
    PRASHNA_YAMAYA_DESC("Another planet blocks the application", "अर्को ग्रहले आवेदन रोक्छ"),
    PRASHNA_ASPECT_FORMING("Aspect Forming", "पक्ष गठन हुँदैछ"),
    PRASHNA_ASPECT_SEPARATING("Aspect Separating", "पक्ष विघटन हुँदैछ"),

    // Question Categories
    PRASHNA_CATEGORY("Question Category", "प्रश्न श्रेणी"),
    PRASHNA_CAT_HEALTH("Health Questions", "स्वास्थ्य प्रश्नहरू"),
    PRASHNA_CAT_WEALTH("Wealth Questions", "धन प्रश्नहरू"),
    PRASHNA_CAT_RELATIONSHIP("Relationship Questions", "सम्बन्ध प्रश्नहरू"),
    PRASHNA_CAT_CAREER("Career Questions", "क्यारियर प्रश्नहरू"),
    PRASHNA_CAT_TRAVEL("Travel Questions", "यात्रा प्रश्नहरू"),
    PRASHNA_CAT_LOST("Lost Object Questions", "हराएको वस्तु प्रश्नहरू"),
    PRASHNA_CAT_MISSING("Missing Person Questions", "हराएको व्यक्ति प्रश्नहरू"),
    PRASHNA_CAT_PROPERTY("Property Questions", "सम्पत्ति प्रश्नहरू"),
    PRASHNA_CAT_PROGENY("Progeny Questions", "सन्तान प्रश्नहरू"),

    // Completion/Frustration
    PRASHNA_COMPLETION("Matter Completion", "विषय पूर्णता"),
    PRASHNA_WILL_COMPLETE("Will Complete", "पूरा हुनेछ"),
    PRASHNA_WILL_NOT_COMPLETE("Will Not Complete", "पूरा हुनेछैन"),
    PRASHNA_DELAYED("Delayed/Obstructed", "ढिलो/अवरुद्ध"),
    PRASHNA_COMPLETION_INDICATORS("Completion Indicators", "पूर्णता संकेतकहरू"),
    PRASHNA_FRUSTRATION_INDICATORS("Frustration Indicators", "निराशा संकेतकहरू"),

    // Yes/No
    PRASHNA_YES_NO("Yes/No Probability", "हो/होइन सम्भावना"),
    PRASHNA_YES_PROBABILITY("Yes Probability", "हो सम्भावना"),
    PRASHNA_NO_PROBABILITY("No Probability", "होइन सम्भावना"),
    PRASHNA_UNCERTAIN("Uncertain", "अनिश्चित"),

    // Lost Object
    PRASHNA_LOST_OBJECT("Lost Object Analysis", "हराएको वस्तु विश्लेषण"),
    PRASHNA_DIRECTION("Direction", "दिशा"),
    PRASHNA_DISTANCE("Distance", "दूरी"),
    PRASHNA_LOCATION_TYPE("Location Type", "स्थान प्रकार"),
    PRASHNA_RECOVERY_CHANCE("Recovery Chance", "प्राप्ति सम्भावना"),
    PRASHNA_NEAR("Near", "नजिक"),
    PRASHNA_FAR("Far", "टाढा"),
    PRASHNA_VERY_FAR("Very Far", "धेरै टाढा"),

    // Missing Person
    PRASHNA_MISSING_PERSON("Missing Person Analysis", "हराएको व्यक्ति विश्लेषण"),
    PRASHNA_IS_ALIVE("Is Alive", "जीवित छ"),
    PRASHNA_WILL_RETURN("Will Return", "फर्कनेछ"),
    PRASHNA_CONDITION("Current Condition", "वर्तमान अवस्था"),

    // Divisional Charts for Prashna
    PRASHNA_D4_PROPERTY("D-4 for Property Questions", "सम्पत्ति प्रश्नको लागि D-4"),
    PRASHNA_D7_PROGENY("D-7 for Progeny Questions", "सन्तान प्रश्नको लागि D-7"),
    PRASHNA_D9_MARRIAGE("D-9 for Marriage Questions", "विवाह प्रश्नको लागि D-9"),
    PRASHNA_D10_CAREER("D-10 for Career Questions", "क्यारियर प्रश्नको लागि D-10"),

    // Timing
    PRASHNA_TIMING("Event Timing", "घटना समय"),
    PRASHNA_WHEN("When Will It Happen", "कहिले हुनेछ"),
    PRASHNA_DAYS("Days", "दिनहरू"),
    PRASHNA_WEEKS("Weeks", "हप्ताहरू"),
    PRASHNA_MONTHS("Months", "महिनाहरू"),
    PRASHNA_YEARS("Years", "वर्षहरू"),

    // Information
    PRASHNA_INFO_TITLE("About Enhanced Prashna", "उन्नत प्रश्नको बारेमा"),
    PRASHNA_INFO_DESC("Enhanced Prashna (Horary) analysis includes advanced techniques from Prashna Marga and Tajika traditions. Itthasala aspects show application/separation timing, while specialized analyses help with lost objects and missing persons.", "उन्नत प्रश्न (होरारी) विश्लेषणमा प्रश्न मार्ग र ताजिक परम्पराहरूबाट उन्नत प्रविधिहरू समावेश छन्। इत्थशाला पक्षहरूले आवेदन/अलगाव समय देखाउँछन्, जबकि विशेष विश्लेषणहरूले हराएको वस्तुहरू र हराएको व्यक्तिहरूमा मद्दत गर्छन्।"),
    PRASHNA_VEDIC_REF("Vedic Reference: Prashna Marga, Tajika Neelakanthi", "वैदिक सन्दर्भ: प्रश्न मार्ग, ताजिक नीलकण्ठी"),

    // ============================================
    // COMMON STRINGS FOR NEW FEATURES
    // ============================================
    COMMON_LOADING("Loading...", "लोड हुँदैछ..."),
    COMMON_ERROR("Error", "त्रुटि"),
    COMMON_RETRY("Retry", "पुनः प्रयास"),
    COMMON_CLOSE("Close", "बन्द गर्नुहोस्"),
    COMMON_DETAILS("Details", "विवरण"),
    COMMON_SUMMARY("Summary", "सारांश"),
    COMMON_INTERPRETATION("Interpretation", "व्याख्या"),
    COMMON_RECOMMENDATIONS("Recommendations", "सिफारिसहरू"),
    COMMON_CURRENT("Current", "वर्तमान"),
    COMMON_UPCOMING("Upcoming", "आउँदा"),
    COMMON_PAST("Past", "बितेको"),
    COMMON_FROM("From", "देखि"),
    COMMON_TO("To", "सम्म"),
    COMMON_DURATION("Duration", "अवधि"),
    COMMON_STRENGTH("Strength", "बल"),
    COMMON_YEARS("years", "वर्ष"),
    COMMON_MONTHS("months", "महिना"),
    COMMON_DAYS("days", "दिन"),
    COMMON_HOURS("hours", "घण्टा"),
    COMMON_MINUTES("minutes", "मिनेट"),
;
}
