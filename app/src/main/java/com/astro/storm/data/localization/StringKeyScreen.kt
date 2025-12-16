package com.astro.storm.data.localization

enum class StringKeyScreen(override val en: String, override val ne: String) : StringKeyInterface {

    // ============================================
    // D-2 HORA CHART
    // ============================================
    HORA_WEALTH_STABILITY("Wealth Stability", "धन स्थिरता"),
    HORA_EARNING_POTENTIAL("Earning Potential", "आय क्षमता"),
    HORA_FINANCIAL_CHALLENGES("Financial Challenges", "वित्तीय चुनौतीहरू"),

    // ============================================
    // D-3 DREKKANA CHART
    // ============================================
    DREKKANA_SIBLING_RELATIONS("Sibling Relations", "भाइबहिनी सम्बन्ध"),
    DREKKANA_COURAGE_INITIATIVE("Courage & Initiative", "साहस र पहल"),
    DREKKANA_PERSONAL_DRIVE("Personal Drive", "व्यक्तिगत प्रेरणा"),

    // =================_==========================
    // D-9 NAVAMSA CHART
    // ============================================
    NAVAMSA_MARRIAGE_HARMONY("Marriage Harmony", "वैवाहिक सामञ्जस्य"),
    NAVAMSA_SPOUSE_NATURE("Spouse's Nature", "जीवनसाथीको स्वभाव"),
    NAVAMSA_DHARMA_PATH("Dharma & Life Path", "धर्म र जीवन मार्ग"),

    // ============================================
    // D-10 DASHAMSA CHART
    // ============================================
    DASHAMSA_CAREER_SUCCESS("Career Success", "करियर सफलता"),
    DASHAMSA_PUBLIC_IMAGE("Public Image", "सार्वजनिक छवि"),
    DASHAMSA_ACHIEVEMENTS("Achievements", "उपलब्धिहरू"),

    // ============================================
    // D-12 DWADASAMSA CHART
    // ============================================
    DWADASAMSA_PARENTAL_LINEAGE("Parental Lineage", "अभिभावकीय वंश"),
    DWADASAMSA_ANCESTRAL_KARMA("Ancestral Karma", "पैतृक कर्म"),
    DWADASAMSA_INHERITED_TRAITS("Inherited Traits", "वंशानुगत गुणहरू"),

    // ============================================
    // DATE SYSTEM TOGGLE
    // ============================================
    DATE_SYSTEM_AD("AD", "ई.सं."),
    DATE_SYSTEM_BS("BS", "वि.सं."),

    // ============================================
    // DIVISIONAL CHARTS SCREEN
    // ============================================
    DIVISIONAL_WEALTH_POTENTIAL("Wealth Potential", "धन सम्भावना"),
    DIVISIONAL_POTENTIAL_LEVEL("Potential: %s", "सम्भावना: %s"),
    DIVISIONAL_LEVEL_EXCEPTIONAL("Exceptional", "असाधारण"),
    DIVISIONAL_LEVEL_HIGH("High", "उच्च"),
    DIVISIONAL_LEVEL_MODERATE("Moderate", "मध्यम"),
    DIVISIONAL_LEVEL_AVERAGE("Average", "औसत"),
    DIVISIONAL_LEVEL_NEEDS_EFFORT("Needs Effort", "प्रयास आवश्यक"),
    DIVISIONAL_SUN_HORA_TITLE("Sun Hora - Self-Earned Wealth", "सूर्य होरा - स्व-अर्जित धन"),
    DIVISIONAL_SUN_HORA_DESC("These planets indicate potential for wealth through your own efforts", "यी ग्रहहरूले तपाईंको आफ्नै प्रयासबाट धनको सम्भावनालाई संकेत गर्छन्"),
    DIVISIONAL_MOON_HORA_TITLE("Moon Hora - Inherited/Liquid Wealth", "चन्द्र होरा - वंशानुगत/तरल धन"),
    DIVISIONAL_MOON_HORA_DESC("These planets indicate potential for inherited or liquid assets", "यी ग्रहहरूले वंशानुगत वा तरल सम्पत्तिको सम्भावनालाई संकेत गर्छन्"),
    DIVISIONAL_WEALTH_SOURCES("Wealth Sources", "धनका स्रोतहरू"),
    DIVISIONAL_WEALTH_TYPE_SELF("Self-Earned", "स्व-अर्जित"),
    DIVISIONAL_WEALTH_TYPE_INHERITED("Inherited/Liquid", "वंशानुगत/तरल"),

    DIVISIONAL_COURAGE_INITIATIVE("Courage & Initiative", "साहस र पहल"),
    DIVISIONAL_COURAGE_LEVEL("Level: %s", "स्तर: %s"),
    DIVISIONAL_LEVEL_DEVELOPING("Developing", "विकासशील"),
    DIVISIONAL_LEVEL_NEEDS_WORK("Needs Work", "काम आवश्यक"),
    DIVISIONAL_COURAGE_PHYSICAL("Physical", "शारीरिक"),
    DIVISIONAL_COURAGE_MENTAL("Mental", "मानसिक"),
    DIVISIONAL_INITIATIVE_ABILITY("Initiative: %s", "पहल क्षमता: %s"),
    DIVISIONAL_SIBLINGS_YOUNGER("Younger", "कान्छा/कान्छी"),
    DIVISIONAL_SIBLINGS_ELDER("Elder", "जेठा/जेठी"),
    DIVISIONAL_SIBLINGS_RELATIONSHIP("Relationship", "सम्बन्ध"),
    DIVISIONAL_COMMUNICATION_SKILLS("Communication Skills", "सञ्चार कौशल"),
    DIVISIONAL_COMM_OVERALL("Overall: %s", "समग्र: %s"),
    DIVISIONAL_COMM_WRITING("Writing: %s", "लेखन: %s"),
    DIVISIONAL_COMM_SPEAKING("Speaking: %s", "वाचन: %s"),
    DIVISIONAL_ARTISTIC_TALENTS("Artistic Talents:", "कलात्मक प्रतिभा:"),
    DIVISIONAL_SHORT_JOURNEYS("Short Journeys", "छोटो यात्राहरू"),

    DIVISIONAL_SPOUSE_CHARS("Spouse Characteristics", "जीवनसाथीको विशेषताहरू"),
    DIVISIONAL_SPOUSE_NATURE("Nature", "स्वभाव"),
    DIVISIONAL_SPOUSE_PHYSICAL("Physical Traits", "शारीरिक गुणहरू"),
    DIVISIONAL_SPOUSE_FAMILY("Family Background", "पारिवारिक पृष्ठभूमि"),
    DIVISIONAL_SPOUSE_DIRECTION("Direction", "दिशा"),
    DIVISIONAL_SPOUSE_PROFESSIONS("Probable Professions:", "सम्भावित पेशाहरू:"),
    DIVISIONAL_MARRIAGE_TIMING("Marriage Timing Factors", "विवाह समय कारकहरू"),
    DIVISIONAL_MARRIAGE_VENUS("Venus", "शुक्र"),
    DIVISIONAL_MARRIAGE_7TH_LORD("7th Lord", "७औं स्वामी"),
    DIVISIONAL_MARRIAGE_DARAKARAKA("Darakaraka", "दारकारक"),
    DIVISIONAL_MARRIAGE_FAV_DASHAS("Favorable Dasha Periods:", "अनुकूल दशा अवधिहरू:"),
    DIVISIONAL_KEY_PLANETS_D9("Key Planet Positions (D-9)", "मुख्य ग्रह स्थिति (D-9)"),
    DIVISIONAL_UPAPADA("Upapada: %s", "उपपद: %s"),
    DIVISIONAL_RELATIONSHIP_STABILITY("Relationship Stability", "सम्बन्ध स्थिरता"),
    DIVISIONAL_ATTENTION_AREAS("Areas of Attention:", "ध्यान दिनुपर्ने क्षेत्रहरू:"),
    DIVISIONAL_PROTECTIVE_FACTORS("Protective Factors:", "सुरक्षात्मक कारकहरू:"),

    DIVISIONAL_BUSINESS_VS_SERVICE("Business vs Service Aptitude", "व्यापार बनाम सेवा योग्यता"),
    DIVISIONAL_BUSINESS("Business", "व्यापार"),
    DIVISIONAL_SERVICE("Service", "सेवा"),
    DIVISIONAL_GOVERNMENT_SERVICE("Government Service Potential", "सरकारी सेवा सम्भावना"),
    DIVISIONAL_RECOMMENDED_AREAS("Recommended Areas:", "सिफारिस गरिएका क्षेत्रहरू:"),
    DIVISIONAL_PROFESSIONAL_STRENGTHS("Professional Strengths", "व्यावसायिक शक्तिहरू"),

    DIVISIONAL_FATHER("Father", "पिता"),
    DIVISIONAL_MOTHER("Mother", "माता"),
    DIVISIONAL_INHERITANCE_POTENTIAL("Inheritance Potential", "उत्तराधिकार सम्भावना"),
    DIVISIONAL_TIMING("Timing: %s", "समय: %s"),
    DIVISIONAL_SOURCES("Sources:", "स्रोतहरू:"),
    DIVISIONAL_ANCESTRAL_PROPERTY("Ancestral Property", "पैतृक सम्पत्ति"),
    DIVISIONAL_LONGEVITY_INDICATORS("Longevity Indicators", "आयुष्य सूचकहरू")
}