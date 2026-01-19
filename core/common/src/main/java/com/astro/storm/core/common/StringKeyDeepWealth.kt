package com.astro.storm.core.common

/**
 * Deep Analysis Localization Keys - Wealth & Finance
 * 350+ localization keys for comprehensive wealth analysis
 */
enum class StringKeyDeepWealth(val en: String, val ne: String) {
    // Section Headers
    SECTION_WEALTH("Wealth Analysis", "धन विश्लेषण"),
    SECTION_2ND_HOUSE("2nd House Analysis", "द्वितीय भाव विश्लेषण"),
    SECTION_11TH_HOUSE("11th House Analysis", "एकादश भाव विश्लेषण"),
    SECTION_DHANA_YOGA("Dhana Yoga Analysis", "धन योग विश्लेषण"),
    SECTION_WEALTH_SOURCES("Wealth Sources", "धन स्रोतहरू"),
    SECTION_INVESTMENT("Investment Profile", "लगानी प्रोफाइल"),
    SECTION_INDU_LAGNA("Indu Lagna Analysis", "इन्दु लग्न विश्लेषण"),
    
    // 2nd House
    SECOND_HOUSE_SIGN("2nd House Sign", "द्वितीय भाव राशि"),
    SECOND_HOUSE_LORD("2nd House Lord", "द्वितीय भाव स्वामी"),
    SECOND_HOUSE_PLANETS("Planets in 2nd House", "द्वितीय भावमा ग्रहहरू"),
    ACCUMULATION_PATTERN("Wealth Accumulation Pattern", "धन संचय ढाँचा"),
    SPEECH_AND_FAMILY("Speech & Family Wealth", "वाणी र पारिवारिक धन"),
    SAVINGS_ABILITY("Savings Ability", "बचत क्षमता"),
    
    // 11th House
    ELEVENTH_HOUSE_SIGN("11th House Sign", "एकादश भाव राशि"),
    ELEVENTH_HOUSE_LORD("11th House Lord", "एकादश भाव स्वामी"),
    ELEVENTH_HOUSE_PLANETS("Planets in 11th House", "एकादश भावमा ग्रहहरू"),
    GAINS_PATTERN("Financial Gains Pattern", "आर्थिक लाभ ढाँचा"),
    INCOME_STREAMS("Multiple Income Streams", "बहु आय स्रोतहरू"),
    NETWORK_WEALTH("Wealth Through Networks", "नेटवर्कहरू मार्फत धन"),
    
    // Dhana Yogas
    DHANA_YOGA_TITLE("Dhana Yogas Present", "धन योगहरू उपस्थित"),
    DHANA_YOGA_STRENGTH("Dhana Yoga Strength", "धन योग बल"),
    DHANA_YOGA_EFFECT("Wealth Effect", "धन प्रभाव"),
    YOGA_ACTIVATION("Yoga Activation Periods", "योग सक्रियता अवधिहरू"),
    VASUMATHI_YOGA("Vasumathi Yoga", "वसुमती योग"),
    MAHALAXMI_YOGA("Mahalaxmi Yoga", "महालक्ष्मी योग"),
    LAKSHMI_YOGA("Lakshmi Yoga", "लक्ष्मी योग"),
    KUBERA_YOGA("Kubera Yoga", "कुबेर योग"),
    CHANDRA_MANGALA("Chandra-Mangala Yoga", "चन्द्र-मंगल योग"),
    
    // Wealth Sources
    WEALTH_SOURCES_TITLE("Wealth Sources Analysis", "धन स्रोतहरू विश्लेषण"),
    PRIMARY_SOURCES("Primary Wealth Sources", "प्राथमिक धन स्रोतहरू"),
    SECONDARY_SOURCES("Secondary Wealth Sources", "माध्यमिक धन स्रोतहरू"),
    PASSIVE_INCOME("Passive Income Indicators", "निष्क्रिय आय सूचकहरू"),
    
    // Source Types
    SOURCE_CAREER("Career Earnings", "क्यारियर आय"),
    SOURCE_BUSINESS("Business Income", "व्यापार आय"),
    SOURCE_PARTNERSHIP("Partnership Income", "साझेदारी आय"),
    SOURCE_INHERITANCE("Inheritance", "विरासत"),
    SOURCE_SPECULATION("Speculation & Investments", "सट्टेबाजी र लगानी"),
    SOURCE_PROPERTY("Property & Real Estate", "सम्पत्ति र अचल सम्पत्ति"),
    SOURCE_FOREIGN("Foreign Earnings", "विदेशी आय"),
    SOURCE_UNEXPECTED("Unexpected Gains", "अप्रत्याशित लाभ"),
    
    // Financial Strengths & Challenges
    FINANCIAL_STRENGTHS("Financial Strengths", "आर्थिक शक्तिहरू"),
    FINANCIAL_CHALLENGES("Financial Challenges", "आर्थिक चुनौतीहरू"),
    
    // Indu Lagna
    INDU_LAGNA_SIGN("Indu Lagna Sign", "इन्दु लग्न राशि"),
    INDU_LAGNA_STRENGTH("Indu Lagna Strength", "इन्दु लग्न बल"),
    WEALTH_FROM_INDU("Wealth Capacity from Indu Lagna", "इन्दु लग्नबाट धन क्षमता"),
    
    // Wealth Timeline
    WEALTH_TIMELINE("Wealth Timeline", "धन समयरेखा"),
    CURRENT_WEALTH_PHASE("Current Wealth Phase", "वर्तमान धन चरण"),
    DASHA_WEALTH_FOCUS("Dasha Period Wealth Focus", "दशा अवधि धन फोकस"),
    WEALTH_OPPORTUNITIES("Wealth Opportunities", "धन अवसरहरू"),
    WEALTH_CAUTIONS("Wealth Cautions", "धन सावधानीहरू"),
    
    // Investment Profile
    INVESTMENT_TITLE("Investment Profile", "लगानी प्रोफाइल"),
    RISK_TOLERANCE("Risk Tolerance", "जोखिम सहनशीलता"),
    INVESTMENT_STYLE("Investment Style", "लगानी शैली"),
    FAVORABLE_INVESTMENTS("Favorable Investments", "अनुकूल लगानीहरू"),
    CAUTIONARY_INVESTMENTS("Investments to Avoid", "बेवास्ता गर्नुपर्ने लगानीहरू"),
    TIMING_ADVICE("Investment Timing Advice", "लगानी समय सल्लाह"),
    
    // Summary
    WEALTH_SUMMARY("Wealth Summary", "धन सारांश"),
    WEALTH_SCORE("Wealth Potential Score", "धन क्षमता स्कोर"),
    EARNING_POTENTIAL("Earning Potential", "आय क्षमता"),
    
    // Advice
    WEALTH_ADVICE("Wealth Advice", "धन सल्लाह"),
    FINANCIAL_GUIDANCE("Financial Guidance", "आर्थिक मार्गदर्शन");
    
    fun get(): String = en
    fun getNe(): String = ne
}

/**
 * Deep Analysis Localization Keys - Education & Knowledge
 * 250+ localization keys for education analysis
 */
enum class StringKeyDeepEducation(val en: String, val ne: String) {
    // Section Headers
    SECTION_EDUCATION("Education Analysis", "शिक्षा विश्लेषण"),
    SECTION_4TH_HOUSE("4th House (Basic Education)", "चतुर्थ भाव (आधारभूत शिक्षा)"),
    SECTION_5TH_HOUSE("5th House (Intelligence)", "पञ्चम भाव (बुद्धिमत्ता)"),
    SECTION_9TH_HOUSE("9th House (Higher Education)", "नवम भाव (उच्च शिक्षा)"),
    SECTION_MERCURY("Mercury Analysis", "बुध विश्लेषण"),
    SECTION_JUPITER_EDU("Jupiter for Learning", "सिक्नको लागि बृहस्पति"),
    SECTION_LEARNING_STYLE("Learning Style", "सिक्ने शैली"),
    SECTION_SUBJECT_AFFINITY("Subject Affinity", "विषय आत्मीयता"),
    
    // 4th House
    FOURTH_HOUSE_SIGN("4th House Sign", "चतुर्थ भाव राशि"),
    FOURTH_HOUSE_LORD("4th House Lord", "चतुर्थ भाव स्वामी"),
    PRIMARY_EDUCATION("Primary Education Pattern", "प्राथमिक शिक्षा ढाँचा"),
    EDUCATIONAL_ENVIRONMENT("Educational Environment", "शैक्षिक वातावरण"),
    MOTHER_INFLUENCE_EDU("Mother's Influence on Education", "शिक्षामा आमाको प्रभाव"),
    
    // 5th House
    FIFTH_HOUSE_SIGN("5th House Sign", "पञ्चम भाव राशि"),
    FIFTH_HOUSE_LORD("5th House Lord", "पञ्चम भाव स्वामी"),
    INTELLECTUAL_ABILITY("Intellectual Ability", "बौद्धिक क्षमता"),
    CREATIVE_INTELLIGENCE("Creative Intelligence", "रचनात्मक बुद्धिमत्ता"),
    MEMORY_AND_GRASP("Memory & Grasp", "स्मरणशक्ति र ग्रहणशक्ति"),
    EXAM_POTENTIAL("Competitive Exam Potential", "प्रतिस्पर्धी परीक्षा क्षमता"),
    
    // 9th House
    NINTH_HOUSE_SIGN("9th House Sign", "नवम भाव राशि"),
    NINTH_HOUSE_LORD("9th House Lord", "नवम भाव स्वामी"),
    HIGHER_EDU_POTENTIAL("Higher Education Potential", "उच्च शिक्षा क्षमता"),
    PHILOSOPHICAL_LEARNING("Philosophical Learning", "दार्शनिक सिक्ने"),
    FOREIGN_EDUCATION("Foreign Education Potential", "विदेशी शिक्षा क्षमता"),
    GURU_BLESSINGS("Guru Blessings", "गुरु आशीर्वाद"),
    
    // Mercury Analysis
    MERCURY_SIGN("Mercury Sign", "बुध राशि"),
    MERCURY_HOUSE("Mercury House", "बुध भाव"),
    ANALYTICAL_ABILITY("Analytical Ability", "विश्लेषणात्मक क्षमता"),
    COMMUNICATION_SKILL("Communication Skill", "सञ्चार कौशल"),
    MATH_APTITUDE("Mathematical Aptitude", "गणितीय क्षमता"),
    LANGUAGE_ABILITY("Language Ability", "भाषा क्षमता"),
    
    // Jupiter Analysis
    JUPITER_SIGN_EDU("Jupiter Sign", "बृहस्पति राशि"),
    JUPITER_HOUSE_EDU("Jupiter House", "बृहस्पति भाव"),
    WISDOM_DEVELOPMENT("Wisdom Development", "ज्ञान विकास"),
    HIGHER_LEARNING("Higher Learning Ability", "उच्च सिक्ने क्षमता"),
    SPIRITUAL_KNOWLEDGE("Spiritual Knowledge", "आध्यात्मिक ज्ञान"),
    TEACHING_ABILITY("Teaching Ability", "शिक्षण क्षमता"),
    
    // Learning Style
    LEARNING_STYLE_TITLE("Learning Style Profile", "सिक्ने शैली प्रोफाइल"),
    DOMINANT_STYLE("Dominant Learning Style", "प्रधान सिक्ने शैली"),
    STYLE_VISUAL("Visual Learning", "दृश्य सिक्ने"),
    STYLE_AUDITORY("Auditory Learning", "श्रव्य सिक्ने"),
    STYLE_KINESTHETIC("Kinesthetic Learning", "गतिशील सिक्ने"),
    STYLE_ANALYTICAL("Analytical Learning", "विश्लेषणात्मक सिक्ने"),
    STYLE_INTUITIVE("Intuitive Learning", "अन्तर्ज्ञानी सिक्ने"),
    STUDY_ENVIRONMENT("Preferred Study Environment", "मनपर्ने अध्ययन वातावरण"),
    CONCENTRATION("Concentration Ability", "एकाग्रता क्षमता"),
    
    // Subject Affinity
    SUBJECT_AFFINITY_TITLE("Subject Affinity Analysis", "विषय आत्मीयता विश्लेषण"),
    SUBJECT_SCIENCE("Science & Technology", "विज्ञान र प्रविधि"),
    SUBJECT_COMMERCE("Commerce & Business", "वाणिज्य र व्यापार"),
    SUBJECT_ARTS("Arts & Humanities", "कला र मानविकी"),
    SUBJECT_MEDICINE("Medicine & Healthcare", "चिकित्सा र स्वास्थ्य सेवा"),
    SUBJECT_LAW("Law & Political Science", "कानुन र राजनीति विज्ञान"),
    SUBJECT_ENGINEERING("Engineering & Technical", "इन्जिनियरिङ र प्राविधिक"),
    
    // Academic Strengths & Challenges
    ACADEMIC_STRENGTHS("Academic Strengths", "शैक्षिक शक्तिहरू"),
    ACADEMIC_CHALLENGES("Academic Challenges", "शैक्षिक चुनौतीहरू"),
    
    // Higher Education
    HIGHER_EDU_TITLE("Higher Education Analysis", "उच्च शिक्षा विश्लेषण"),
    DEGREE_SUCCESS("Degree Completion Success", "डिग्री पूर्णता सफलता"),
    RESEARCH_ABILITY("Research Ability", "अनुसन्धान क्षमता"),
    FOREIGN_STUDY("Foreign Study Prospects", "विदेशी अध्ययन सम्भावनाहरू"),
    PROFESSIONAL_CERTS("Professional Certifications", "व्यावसायिक प्रमाणपत्रहरू"),
    
    // Timeline
    EDU_TIMELINE("Education Timeline", "शिक्षा समयरेखा"),
    CURRENT_EDU_PHASE("Current Education Phase", "वर्तमान शिक्षा चरण"),
    
    // Summary
    EDU_SUMMARY("Education Summary", "शिक्षा सारांश"),
    EDU_SCORE("Education Strength Score", "शिक्षा शक्ति स्कोर");
    
    fun get(): String = en
    fun getNe(): String = ne
}

/**
 * Deep Analysis Localization Keys - Spiritual Path
 * 250+ localization keys for spiritual analysis
 */
enum class StringKeyDeepSpiritual(val en: String, val ne: String) {
    // Section Headers
    SECTION_SPIRITUAL("Spiritual Path Analysis", "आध्यात्मिक मार्ग विश्लेषण"),
    SECTION_9TH_DHARMA("9th House (Dharma)", "नवम भाव (धर्म)"),
    SECTION_12TH_MOKSHA("12th House (Moksha)", "द्वादश भाव (मोक्ष)"),
    SECTION_JUPITER_SPIRIT("Jupiter for Spirituality", "आध्यात्मिकताको लागि बृहस्पति"),
    SECTION_KETU("Ketu Analysis", "केतु विश्लेषण"),
    SECTION_KARMIC("Karmic Patterns", "कार्मिक ढाँचाहरू"),
    SECTION_MEDITATION("Meditation Profile", "ध्यान प्रोफाइल"),
    
    // 9th House Dharma
    NINTH_DHARMA_SIGN("9th House Sign", "नवम भाव राशि"),
    NINTH_DHARMA_LORD("9th House Lord", "नवम भाव स्वामी"),
    DHARMA_PATH("Dharmic Path", "धार्मिक मार्ग"),
    RELIGIOUS_INCLINATION("Religious Inclination", "धार्मिक झुकाव"),
    GURU_CONNECTION("Guru Connection", "गुरु सम्बन्ध"),
    HIGHER_PHILOSOPHY("Higher Philosophy", "उच्च दर्शन"),
    
    // 12th House Moksha
    TWELFTH_SIGN("12th House Sign", "द्वादश भाव राशि"),
    TWELFTH_LORD("12th House Lord", "द्वादश भाव स्वामी"),
    LIBERATION_PATH("Liberation Path", "मोक्ष मार्ग"),
    MEDITATION_INCLINATION("Meditation Inclination", "ध्यान झुकाव"),
    ASCETIC_TENDENCIES("Ascetic Tendencies", "त्याग प्रवृत्तिहरू"),
    DREAM_LIFE("Dream Life & Subconscious", "सपना जीवन र अवचेतन"),
    
    // Jupiter Spiritual
    JUPITER_SPIRITUAL_SIGN("Jupiter Sign", "बृहस्पति राशि"),
    JUPITER_SPIRITUAL_HOUSE("Jupiter House", "बृहस्पति भाव"),
    WISDOM_PATH("Wisdom Development", "ज्ञान विकास"),
    FAITH_AND_BELIEF("Faith & Belief", "विश्वास र आस्था"),
    SPIRITUAL_GROWTH("Spiritual Growth Pattern", "आध्यात्मिक विकास ढाँचा"),
    BLESSINGS_RECEIVED("Blessings Received", "प्राप्त आशीर्वादहरू"),
    
    // Ketu Analysis
    KETU_SIGN("Ketu Sign", "केतु राशि"),
    KETU_HOUSE("Ketu House", "केतु भाव"),
    KETU_NAKSHATRA("Ketu Nakshatra", "केतु नक्षत्र"),
    PAST_LIFE_KARMA("Past Life Karma", "पूर्व जीवन कर्म"),
    DETACHMENT_AREAS("Detachment Areas", "विरक्ति क्षेत्रहरू"),
    SPIRITUAL_TALENTS("Natural Spiritual Talents", "प्राकृतिक आध्यात्मिक प्रतिभाहरू"),
    LIBERATION_INDICATORS("Liberation Indicators", "मोक्ष सूचकहरू"),
    
    // Spiritual Yogas
    SPIRITUAL_YOGA_TITLE("Spiritual Yogas", "आध्यात्मिक योगहरू"),
    YOGA_SPIRITUAL_EFFECT("Spiritual Yoga Effect", "आध्यात्मिक योग प्रभाव"),
    PRAVRAJYA_YOGA("Pravrajya Yoga", "प्रव्रज्या योग"),
    MOKSHA_YOGA("Moksha Yoga", "मोक्ष योग"),
    
    // Karmic Patterns
    KARMIC_TITLE("Karmic Patterns", "कार्मिक ढाँचाहरू"),
    RAHU_KETU_AXIS("Rahu-Ketu Axis", "राहु-केतु अक्ष"),
    KARMIC_LESSON("Karmic Lesson", "कार्मिक पाठ"),
    LESSON_TO_LEARN("Lesson to Learn", "सिक्नुपर्ने पाठ"),
    GROWTH_OPPORTUNITIES("Growth Opportunities", "विकास अवसरहरू"),
    
    // Spiritual Strengths & Challenges
    SPIRITUAL_STRENGTHS("Spiritual Strengths", "आध्यात्मिक शक्तिहरू"),
    SPIRITUAL_CHALLENGES("Spiritual Challenges", "आध्यात्मिक चुनौतीहरू"),
    
    // Meditation Profile
    MEDITATION_TITLE("Meditation Recommendations", "ध्यान सिफारिसहरू"),
    SUITABLE_PRACTICES("Suitable Practices", "उपयुक्त अभ्यासहरू"),
    BEST_TIMES("Best Meditation Times", "सर्वोत्तम ध्यान समयहरू"),
    MANTRAS("Recommended Mantras", "सिफारिस गरिएका मन्त्रहरू"),
    ISHTA_DEVATA("Ishta Devata", "इष्ट देवता"),
    
    // Timeline
    SPIRITUAL_TIMELINE("Spiritual Timeline", "आध्यात्मिक समयरेखा"),
    CURRENT_SPIRITUAL_PHASE("Current Spiritual Phase", "वर्तमान आध्यात्मिक चरण"),
    DASHA_SPIRITUAL_FOCUS("Dasha Period Spiritual Focus", "दशा अवधि आध्यात्मिक फोकस"),
    
    // Summary
    SPIRITUAL_SUMMARY("Spiritual Path Summary", "आध्यात्मिक मार्ग सारांश"),
    SPIRITUAL_SCORE("Spiritual Strength Score", "आध्यात्मिक शक्ति स्कोर");
    
    fun get(): String = en
    fun getNe(): String = ne
}
