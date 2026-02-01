package com.astro.storm.core.common

/**
 * String keys for Divisional Charts analysis
 * Includes: Hora (D-2), Drekkana (D-3), Navamsa (D-9), Dashamsa (D-10), Dwadasamsa (D-12)
 */
enum class StringKeyDoshaDivisional(
    override val en: String,
    override val ne: String,
) : StringKeyInterface {

    // ============================================
    // DIVISIONAL CHART ANALYZER - HORA (D-2)
    // ============================================
    HORA_ANALYSIS_TITLE("Hora (D-2) Analysis", "होरा (D-2) विश्लेषण"),
    HORA_ANALYSIS_SUBTITLE("Wealth and Financial Potential", "धन र आर्थिक सम्भावना"),
    HORA_ANALYSIS_ABOUT("About Hora Chart", "होरा चार्टको बारेमा"),
    HORA_ANALYSIS_ABOUT_DESC("Hora chart divides each sign into Sun and Moon halves, revealing wealth accumulation patterns and financial potential.", "होरा चार्टले प्रत्येक राशिलाई सूर्य र चन्द्र आधामा विभाजन गर्छ, धन संचय ढाँचा र आर्थिक सम्भावना प्रकट गर्दछ।"),
    HORA_SUN_PLANETS("Planets in Sun Hora", "सूर्य होरामा ग्रहहरू"),
    HORA_MOON_PLANETS("Planets in Moon Hora", "चन्द्र होरामा ग्रहहरू"),
    HORA_DOMINANT("Dominant Hora", "प्रभावशाली होरा"),
    HORA_BALANCE("Hora Balance", "होरा सन्तुलन"),
    HORA_WEALTH_INDICATORS("Wealth Indicators", "धन सूचकहरू"),
    HORA_INCOME_POTENTIAL("Income Potential", "आय सम्भावना"),
    HORA_SELF_EARNED("Self Earned", "आर्जित"),
    HORA_INHERITED("Inherited", "पैतृक"),
    HORA_POTENTIAL("Wealth Potential", "धन सम्भावना"),
    HORA_POTENTIAL_EXCEPTIONAL("Exceptional Wealth", "असाधारण धन"),
    HORA_POTENTIAL_HIGH("High Wealth", "उच्च धन"),
    HORA_POTENTIAL_MODERATE("Moderate Wealth", "मध्यम धन"),
    HORA_POTENTIAL_AVERAGE("Average Wealth", "औसत धन"),
    HORA_POTENTIAL_NEEDS_EFFORT("Needs Effort", "प्रयास आवश्यक"),
    HORA_SUN_TITLE("Sun Hora - Self-Earned Wealth", "सूर्य होरा - स्व-अर्जित धन"),
    HORA_SUN_DESC("These planets indicate potential for wealth through your own efforts", "यी ग्रहहरूले तपाईंको आफ्नै प्रयासबाट धनको सम्भावना संकेत गर्छन्"),
    HORA_MOON_TITLE("Moon Hora - Inherited/Liquid Wealth", "चन्द्र होरा - विरासत/तरल धन"),
    HORA_MOON_DESC("These planets indicate potential for inherited or liquid assets", "यी ग्रहहरूले विरासत वा तरल सम्पत्तिको सम्भावना संकेत गर्छन्"),
    HORA_WEALTH_SOURCES("Wealth Sources", "धनका स्रोतहरू"),

    // ============================================
    // DIVISIONAL CHART ANALYZER - DREKKANA (D-3)
    // ============================================
    DREKKANA_ANALYSIS_TITLE("Drekkana (D-3) Analysis", "द्रेष्काण (D-3) विश्लेषण"),
    DREKKANA_ANALYSIS_SUBTITLE("Siblings and Courage", "भाइबहिनी र साहस"),
    DREKKANA_ANALYSIS_ABOUT("About Drekkana Chart", "द्रेष्काण चार्टको बारेमा"),
    DREKKANA_ANALYSIS_ABOUT_DESC("Drekkana chart reveals sibling relationships, courage, and personal initiative. Each sign is divided into three 10-degree portions.", "द्रेष्काण चार्टले भाइबहिनी सम्बन्ध, साहस, र व्यक्तिगत पहलकदमी प्रकट गर्दछ। प्रत्येक राशि तीन १०-अंश भागमा विभाजित छ।"),
    DREKKANA_TYPE("Drekkana Type", "द्रेष्काण प्रकार"),
    DREKKANA_FIRST("First Drekkana (0-10°)", "पहिलो द्रेष्काण (०-१०°)"),
    DREKKANA_SECOND("Second Drekkana (10-20°)", "दोस्रो द्रेष्काण (१०-२०°)"),
    DREKKANA_THIRD("Third Drekkana (20-30°)", "तेस्रो द्रेष्काण (२०-३०°)"),
    DREKKANA_SIBLING_KARMA("Sibling Karma", "भाइबहिनी कर्म"),
    DREKKANA_COURAGE_LEVEL("Courage Level", "साहस स्तर"),
    DREKKANA_COURAGE_TITLE("Courage Analysis", "साहस विश्लेषण"),
    DREKKANA_SHORT_JOURNEYS("Short Journeys", "छोटो यात्रा"),
    DREKKANA_YOUNGER("Younger Siblings", "साना भाइबहिनी"),
    DREKKANA_ELDER("Elder Siblings", "ठूला भाइबहिनी"),
    DREKKANA_RELATIONSHIP("Relationship", "सम्बन्ध"),
    DREKKANA_COMMUNICATION_TITLE("Communication", "सञ्चार"),
    DREKKANA_OVERALL("Overall", "समग्र"),
    DREKKANA_WRITING("Writing", "लेखन"),
    DREKKANA_SPEAKING("Speaking", "बोल्ने"),
    DREKKANA_ARTISTIC_TALENTS("Artistic Talents", "कलात्मक प्रतिभा"),
    DREKKANA_PHYSICAL("Physical", "शारीरिक"),
    DREKKANA_MENTAL("Mental", "मानसिक"),
    DREKKANA_INITIATIVE("Initiative", "पहल"),
    DREKKANA_ARTISTIC("Artistic Talents", "कलात्मक प्रतिभाहरू"),

    // Courage Levels
    COURAGE_EXCEPTIONAL("Exceptional Courage", "असाधारण साहस"),
    COURAGE_HIGH("High Courage", "उच्च साहस"),
    COURAGE_MODERATE("Moderate Courage", "मध्यम साहस"),
    COURAGE_DEVELOPING("Developing Courage", "विकासशील साहस"),
    COURAGE_NEEDS_WORK("Needs Work", "काम आवश्यक"),
    COURAGE_LEVEL("Level", "स्तर"),
    COURAGE_PHYSICAL("Physical Courage", "शारीरिक साहस"),
    COURAGE_MENTAL("Mental Courage", "मानसिक साहस"),
    COURAGE_INITIATIVE("Initiative", "पहल"),

    // ============================================
    // DIVISIONAL CHART ANALYZER - NAVAMSA (D-9)
    // ============================================
    NAVAMSA_MARRIAGE_TITLE("Navamsa Marriage Analysis", "नवांश विवाह विश्लेषण"),
    NAVAMSA_MARRIAGE_SUBTITLE("D-9 Partnership Timing", "D-9 साझेदारी समय"),
    NAVAMSA_MARRIAGE_ABOUT("About Navamsa for Marriage", "विवाहको लागि नवांशको बारेमा"),
    NAVAMSA_MARRIAGE_ABOUT_DESC("Navamsa is the most important divisional chart for marriage timing, spouse characteristics, and relationship quality.", "नवांश विवाह समय, जीवनसाथी विशेषताहरू, र सम्बन्ध गुणस्तरको लागि सबैभन्दा महत्त्वपूर्ण विभाजन चार्ट हो।"),
    NAVAMSA_VENUS_POSITION("Venus in Navamsa", "नवांशमा शुक्र"),
    NAVAMSA_7TH_LORD("7th Lord in Navamsa", "नवांशमा ७म स्वामी"),
    NAVAMSA_SPOUSE_INDICATORS("Spouse Indicators", "जीवनसाथी सूचकहरू"),
    NAVAMSA_MARRIAGE_TIMING("Marriage Timing", "विवाह समय"),
    NAVAMSA_RELATIONSHIP_QUALITY("Relationship Quality", "सम्बन्ध गुणस्तर"),
    NAVAMSA_SPOUSE_TITLE("Spouse Characteristics", "जीवनसाथी विशेषताहरू"),
    NAVAMSA_TIMING_TITLE("Marriage Timing", "विवाह समय"),
    NAVAMSA_KEY_PLANETS_TITLE("Key Planets", "मुख्य ग्रहहरू"),
    NAVAMSA_NATURE("General Nature", "सामान्य प्रकृति"),
    NAVAMSA_PHYSICAL_TRAITS("Physical Traits", "शारीरिक लक्षण"),
    NAVAMSA_FAMILY_BACKGROUND("Family Background", "पारिवारिक पृष्ठभूमि"),
    NAVAMSA_DIRECTION("Direction", "दिशा"),
    NAVAMSA_PROBABLE_PROFESSIONS("Probable Professions", "सम्भावित पेशाहरू"),
    NAVAMSA_VENUS("Venus", "शुक्र"),
    NAVAMSA_JUPITER("Jupiter", "बृहस्पति"),
    NAVAMSA_7TH_LORD_LABEL("7th Lord", "७म स्वामी"),
    NAVAMSA_DARAKARAKA("Darakaraka", "दाराकारक"),
    NAVAMSA_FAVORABLE_DASHA("Favorable Dasha", "शुभ दशा"),
    NAVAMSA_UPAPADA("Upapada Lagna", "उपपद लग्न"),
    NAVAMSA_RELATIONSHIP_STABILITY("Relationship Stability", "सम्बन्ध स्थिरता"),
    NAVAMSA_AREAS_ATTENTION("Areas for Attention", "ध्यान दिनुपर्ने क्षेत्रहरू"),
    NAVAMSA_PROTECTIVE_FACTORS("Protective Factors", "रक्षात्मक कारकहरू"),

    // ============================================
    // DIVISIONAL CHART ANALYZER - DASHAMSA (D-10)
    // ============================================
    DASHAMSA_CAREER_TITLE("Dashamsa (D-10) Career Analysis", "दशांश (D-10) करियर विश्लेषण"),
    DASHAMSA_CAREER_SUBTITLE("Professional Life and Status", "व्यावसायिक जीवन र स्थिति"),
    DASHAMSA_CAREER_ABOUT("About Dashamsa Chart", "दशांश चार्टको बारेमा"),
    DASHAMSA_CAREER_ABOUT_DESC("Dashamsa reveals career potential, professional achievements, and worldly status through 10th harmonic analysis.", "दशांशले १०औं हार्मोनिक विश्लेषण मार्फत करियर सम्भावना, व्यावसायिक उपलब्धि, र सांसारिक स्थिति प्रकट गर्दछ।"),
    DASHAMSA_10TH_LORD("10th Lord in D-10", "D-10 मा १०औं स्वामी"),
    DASHAMSA_LAGNA_LORD("D-10 Lagna Lord", "D-10 लग्न स्वामी"),
    DASHAMSA_CAREER_HOUSES("Career Houses Analysis", "करियर भाव विश्लेषण"),
    DASHAMSA_PROFESSION_TYPE("Profession Indicators", "पेशा सूचकहरू"),
    DASHAMSA_STATUS_POTENTIAL("Status Potential", "स्थिति सम्भावना"),
    DASHAMSA_BUSINESS_VS_SERVICE("Business vs Service", "व्यापार बनाम सेवा"),
    DASHAMSA_BUSINESS("Business Aptitude", "व्यापार योग्यता"),
    DASHAMSA_SERVICE("Service Aptitude", "सेवा योग्यता"),
    DASHAMSA_GOVT_SERVICE_TITLE("Government Service", "सरकारी सेवा"),
    DASHAMSA_POTENTIAL("Potential", "सम्भावना"),
    DASHAMSA_RECOMMENDED_AREAS("Recommended Areas", "सिफारिस गरिएका क्षेत्रहरू"),
    DASHAMSA_PROFESSIONAL_STRENGTHS("Professional Strengths", "व्यावसायिक शक्तिहरू"),

    // ============================================
    // DIVISIONAL CHART ANALYZER - DWADASAMSA (D-12)
    // ============================================
    DWADASAMSA_TITLE("Dwadasamsa (D-12) Analysis", "द्वादशांश (D-12) विश्लेषण"),
    DWADASAMSA_SUBTITLE("Parents and Ancestral Karma", "अभिभावक र पूर्वज कर्म"),
    DWADASAMSA_ABOUT("About Dwadasamsa Chart", "द्वादशांश चार्टको बारेमा"),
    DWADASAMSA_ABOUT_DESC("Dwadasamsa reveals parental relationships, ancestral inheritance, and genetic predispositions through 12th harmonic analysis.", "द्वादशांशले १२औं हार्मोनिक विश्लेषण मार्फत अभिभावक सम्बन्ध, पैतृक विरासत, र आनुवंशिक प्रवृत्तिहरू प्रकट गर्दछ।"),
    DWADASAMSA_SUN_POSITION("Sun in D-12 (Father)", "D-12 मा सूर्य (बुबा)"),
    DWADASAMSA_MOON_POSITION("Moon in D-12 (Mother)", "D-12 मा चन्द्र (आमा)"),
    DWADASAMSA_FATHER_ANALYSIS("Father Analysis", "बुबा विश्लेषण"),
    DWADASAMSA_MOTHER_ANALYSIS("Mother Analysis", "आमा विश्लेषण"),
    DWADASAMSA_ANCESTRAL_KARMA("Ancestral Karma", "पैतृक कर्म"),
    DWADASAMSA_INHERITANCE("Inheritance Potential", "विरासत सम्भावना"),
    DWADASAMSA_FATHER("Father", "बुबा"),
    DWADASAMSA_MOTHER("Mother", "आमा"),
    DWADASAMSA_INHERITANCE_TITLE("Inheritance Analysis", "विरासत विश्लेषण"),
    DWADASAMSA_ANCESTRAL_PROPERTY("Ancestral Property", "पैतृक सम्पत्ति"),
    DWADASAMSA_LONGEVITY_TITLE("Parental Longevity", "अभिभावक दीर्घायु"),
    DWADASAMSA_POTENTIAL("Potential", "सम्भावना"),
    DWADASAMSA_TIMING("Timing", "समय"),
    DWADASAMSA_SOURCES("Sources", "स्रोतहरू"),
    DWADASAMSA_SIGNIFICATOR("Significator", "कारक"),
    DWADASAMSA_HOUSE_LORD("House Lord", "भाव स्वामी"),
    DWADASAMSA_CHARACTERISTICS("Characteristics", "विशेषताहरू"),
    DWADASAMSA_RELATIONSHIP("Relationship", "सम्बन्ध"),
    DWADASAMSA_HEALTH_CONCERNS("Health Concerns", "स्वास्थ्य चिन्ताहरू"),

    // ============================================
    // DIVISIONAL CHARTS TABS
    // ============================================
    DIVISIONAL_HORA_TAB("Hora (D-2)", "होरा (D-2)"),
    DIVISIONAL_DREKKANA_TAB("Drekkana (D-3)", "द्रेष्काण (D-3)"),
    DIVISIONAL_NAVAMSA_TAB("Navamsa (D-9)", "नवांश (D-9)"),
    DIVISIONAL_DASHAMSA_TAB("Dashamsa (D-10)", "दशांश (D-10)"),
    DIVISIONAL_DWADASAMSA_TAB("Dwadasamsa (D-12)", "द्वादशांश (D-12)"),

    // ============================================
    // DIVISIONAL CHARTS SCREEN
    // ============================================
    DIVISIONAL_CHARTS_TITLE("Divisional Charts Analysis", "विभागीय कुण्डली विश्लेषण"),
    DIVISIONAL_CHARTS_SUBTITLE("Detailed Varga Analysis", "विस्तृत वर्ग विश्लेषण"),
    DIVISIONAL_CHARTS_ABOUT("About Divisional Charts", "विभागीय कुण्डलीको बारेमा"),
    DIVISIONAL_CHARTS_ABOUT_DESC("Divisional charts (Vargas) provide deeper insights into specific life areas. D-2 for wealth, D-3 for siblings, D-9 for marriage, D-10 for career, D-12 for parents.", "विभागीय कुण्डलीहरूले विशेष जीवन क्षेत्रहरूमा गहिरो अन्तर्दृष्टि प्रदान गर्दछन्। D-2 धनको लागि, D-3 भाइबहिनीको लागि, D-9 विवाहको लागि, D-10 क्यारियरको लागि, D-12 अभिभावकको लागि।"),
    DIVISIONAL_WEALTH_ANALYSIS("Wealth & Finance", "धन र वित्त"),
    DIVISIONAL_SIBLING_ANALYSIS("Siblings & Courage", "भाइबहिनी र साहस"),
    DIVISIONAL_MARRIAGE_ANALYSIS("Marriage & Dharma", "विवाह र धर्म"),
    DIVISIONAL_CAREER_ANALYSIS("Career & Status", "क्यारियर र स्थिति"),
    DIVISIONAL_PARENTS_ANALYSIS("Parents & Ancestry", "अभिभावक र वंश"),
    DIVISIONAL_KEY_PLANETS("Key Planets", "मुख्य ग्रहहरू"),
    DIVISIONAL_STRENGTH_ASSESSMENT("Strength Assessment", "बल मूल्याङ्कन"),
    DIVISIONAL_DETAILED_RESULTS("Detailed Results", "विस्तृत परिणामहरू"),
;
}
