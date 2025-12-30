package com.astro.storm.data.localization

enum class StringKeyPrediction(override val en: String, override val ne: String) : StringKeyInterface {
    // Ascendant Paths
    PRED_ASC_ARIES("courage, independence, and pioneering spirit", "साहस, स्वतन्त्रता र अग्रगामी भावना"),
    PRED_ASC_TAURUS("stability, material security, and perseverance", "स्थिरता, भौतिक सुरक्षा र दृढता"),
    PRED_ASC_GEMINI("communication, versatility, and intellectual pursuits", "सञ्चार, बहुमुखी प्रतिभा र बौद्धिक कार्यहरू"),
    PRED_ASC_CANCER("nurturing, emotional depth, and family connections", "हेरचाह, भावनात्मक गहिराई र पारिवारिक सम्बन्धहरू"),
    PRED_ASC_LEO("leadership, creativity, and self-expression", "नेतृत्व, रचनात्मकता र आत्म-अभिव्यक्ति"),
    PRED_ASC_VIRGO("service, analysis, and practical perfection", "सेवा, विश्लेषण र व्यावहारिक पूर्णता"),
    PRED_ASC_LIBRA("harmony, relationships, and balanced judgment", "सद्भाव, सम्बन्ध र सन्तुलित निर्णय"),
    PRED_ASC_SCORPIO("transformation, intensity, and deep research", "रूपान्तरण, गहनता र गहिरो अनुसन्धान"),
    PRED_ASC_SAGITTARIUS("wisdom, exploration, and philosophical understanding", "ज्ञान, अन्वेषण र दार्शनिक समझ"),
    PRED_ASC_CAPRICORN("discipline, ambition, and long-term achievement", "अनुशासन, महत्त्वाकांक्षा र दीर्घकालीन उपलब्धि"),
    PRED_ASC_AQUARIUS("innovation, humanitarian ideals, and progressive thinking", "नवप्रवर्तन, मानवीय आदर्श र प्रगतिशील सोच"),
    PRED_ASC_PISCES("spirituality, compassion, and transcendent wisdom", "आध्यात्मिकता, करुणा र उत्कृष्ट ज्ञान"),

    // Moon Paths
    PRED_MOON_ARIES("excitement and action", "उत्साह र कार्य"),
    PRED_MOON_TAURUS("comfort and stability", "आराम र स्थिरता"),
    PRED_MOON_GEMINI("variety and mental stimulation", "विविधता र मानसिक उत्तेजना"),
    PRED_MOON_CANCER("security and emotional connection", "सुरक्षा र भावनात्मक सम्बन्ध"),
    PRED_MOON_LEO("recognition and warmth", "मान्यता र न्यानोपन"),
    PRED_MOON_VIRGO("order and practical service", "व्यवस्था र व्यावहारिक सेवा"),
    PRED_MOON_LIBRA("partnership and aesthetic beauty", "साझेदारी र सौन्दर्य"),
    PRED_MOON_SCORPIO("depth and emotional transformation", "गहिराई र भावनात्मक रूपान्तरण"),
    PRED_MOON_SAGITTARIUS("meaning and philosophical truth", "अर्थ र दार्शनिक सत्य"),
    PRED_MOON_CAPRICORN("achievement and respect", "उपलब्धि र सम्मान"),
    PRED_MOON_AQUARIUS("freedom and humanitarian connection", "स्वतन्त्रता र मानवीय सम्बन्ध"),
    PRED_MOON_PISCES("unity and spiritual transcendence", "एकता र आध्यात्मिक उत्कृष्टता"),
    PRED_MOON_DEFAULT("emotional fulfillment", "भावनात्मक पूर्ति"),

    // Life Themes
    PRED_THEME_ARIES("Pioneer & Warrior", "अग्रगामी र योद्धा"),
    PRED_THEME_TAURUS("Builder & Preserver", "निर्माता र रक्षक"),
    PRED_THEME_GEMINI("Communicator & Learner", "सञ्चारकर्मी र शिक्षार्थी"),
    PRED_THEME_CANCER("Nurturer & Protector", "पालनकर्ता र संरक्षक"),
    PRED_THEME_LEO("Leader & Creator", "नेता र सिर्जनाकर्ता"),
    PRED_THEME_VIRGO("Server & Healer", "सेवक र उपचारक"),
    PRED_THEME_LIBRA("Diplomat & Artist", "कुटनीतिज्ञ र कलाकार"),
    PRED_THEME_SCORPIO("Transformer & Investigator", "रूपान्तरक र अन्वेषक"),
    PRED_THEME_SAGITTARIUS("Philosopher & Explorer", "दार्शनिक र अन्वेषक"),
    PRED_THEME_CAPRICORN("Achiever & Master", "सफल र निपुण"),
    PRED_THEME_AQUARIUS("Innovator & Humanitarian", "नवप्रवर्तक र मानवतावादी"),
    PRED_THEME_PISCES("Mystic & Compassionate Soul", "रहस्यवादी र दयालु आत्मा"),

    // Spiritual Paths
    PRED_SPIRIT_ARIES("self-realization through action", "कार्य मार्फत आत्म-साक्षात्कार"),
    PRED_SPIRIT_TAURUS("grounding and material transcendence", "आधार र भौतिक उत्कृष्टता"),
    PRED_SPIRIT_GEMINI("integration of duality", "द्वैतको एकीकरण"),
    PRED_SPIRIT_CANCER("emotional mastery and nurturing wisdom", "भावनात्मक निपुणता र हेरचाह ज्ञान"),
    PRED_SPIRIT_LEO("heart-centered consciousness", "हृदय-केन्द्रित चेतना"),
    PRED_SPIRIT_VIRGO("perfection through service", "सेवा मार्फत पूर्णता"),
    PRED_SPIRIT_LIBRA("balance and unity consciousness", "सन्तुलन र एकता चेतना"),
    PRED_SPIRIT_SCORPIO("death and rebirth cycles", "मृत्यु र पुनर्जन्म चक्र"),
    PRED_SPIRIT_SAGITTARIUS("truth-seeking and higher knowledge", "सत्यको खोज र उच्च ज्ञान"),
    PRED_SPIRIT_CAPRICORN("mastery and enlightened leadership", "निपुणता र प्रबुद्ध नेतृत्व"),
    PRED_SPIRIT_AQUARIUS("universal love and detachment", "विश्वव्यापी प्रेम र वैराग्य"),
    PRED_SPIRIT_PISCES("dissolution into divine consciousness", "दिव्य चेतनामि विलय"),
    PRED_SPIRIT_DEFAULT("spiritual awakening", "आध्यात्मिक जागरण"),

    // Dasha Effects (Simpler Generic ones)
    PRED_DASHA_SUN("self-confidence, authority, and father-related matters", "आत्मविश्वास, अधिकार र पिता सम्बन्धी मामिलाहरू"),
    PRED_DASHA_MOON("emotions, mind, and mother-related matters", "भावना, मन र आमा सम्बन्धी मामिलाहरू"),
    PRED_DASHA_MARS("energy, courage, and taking action", "ऊर्जा, साहस र कार्य गर्ने क्षमता"),
    PRED_DASHA_MERCURY("communication, intellect, and business", "सञ्चार, बुद्धि र व्यापार"),
    PRED_DASHA_JUPITER("wisdom, expansion, and spiritual growth", "ज्ञान, विस्तार र आध्यात्मिक वृद्धि"),
    PRED_DASHA_VENUS("relationships, luxury, and artistic pursuits", "सम्बन्ध, विलासिता र कलात्मक कार्यहरू"),
    PRED_DASHA_SATURN("discipline, karmic lessons, and hard work", "अनुशासन, कर्मिक पाठ र कडा परिश्रम"),
    PRED_DASHA_RAHU("worldly desires, unconventional paths, and material success", "सांसारिक इच्छाहरू, अपरम्परागत मार्ग र भौतिक सफलता"),
    PRED_DASHA_KETU("spirituality, detachment, and moksha", "आध्यात्मिकता, वैराग्य र मोक्ष"),
    PRED_DASHA_DEFAULT("various life experiences", "विभिन्न जीवन अनुभवहरू"),

    // Antardasha Effects
    PRED_AD_SUN("emphasis on leadership and recognition", "नेतृत्व र मान्यता मा जोड"),
    PRED_AD_MOON("heightened emotional sensitivity", "बढेको भावनात्मक संवेदनशीलता"),
    PRED_AD_MARS("increased drive and assertiveness", "बढेको उत्साह र दृढता"),
    PRED_AD_MERCURY("enhanced mental clarity", "बढेको मानसिक स्पष्टता"),
    PRED_AD_JUPITER("blessings and fortunate opportunities", " आशीर्वाद र भाग्यशाली अवसरहरू"),
    PRED_AD_VENUS("pleasure and harmonious experiences", "आनन्द र सामंजस्यपूर्ण अनुभवहरू"),
    PRED_AD_SATURN("patience and perseverance requirements", "धैर्य र लगनशीलताको आवश्यकता"),
    PRED_AD_RAHU("sudden changes and ambition", "अचानक परिवर्तन र महत्त्वाकांक्षा"),
    PRED_AD_KETU("spiritual insights and letting go", "आध्यात्मिक अन्तरदृष्टि र त्याग"),
    PRED_AD_DEFAULT("additional influences", "थप प्रभावहरू"),

    // Prediction Templates
    PRED_PATH_TEMPLATE("Your life path is shaped by your %s ascendant, indicating a personality focused on %s. With your Moon in %s, your emotional nature seeks %s.", "तपाईंको जीवन मार्ग तपाईंको %s लग्नद्वारा निर्देशित छ, जसले %s मा केन्द्रित व्यक्तित्वलाई संकेत गर्दछ। %s मा तपाईंको चन्द्रमाको साथ, तपाईंको भावनात्मक प्रकृतिले %s खोज्छ।"),
    PRED_PATH_TEMPLATE_UNKNOWN_MOON("Your life path is shaped by your %s ascendant, indicating a personality focused on %s.", "तपाईंको जीवन मार्ग तपाईंको %s लग्नद्वारा निर्देशित छ, जसले %s मा केन्द्रित व्यक्तित्वलाई संकेत गर्दछ।"),
    PRED_SPIRIT_TEMPLATE("Your spiritual journey emphasizes %s through %s influences.", "तपाईंको आध्यात्मिक यात्राले %s प्रभावहरू मार्फत %s मा जोड दिन्छ।"),
    PRED_DASHA_INFO_TEMPLATE("%s Mahadasha - %s Antardasha", "%s महादशा - %s अन्तर्दशा"),
    PRED_DASHA_PERIOD_DEFAULT("Dasha Period", "दशा अवधि"),
    PRED_DASHA_EFFECT_TEMPLATE("The %s period brings focus on %s. %s", "%s अवधिले %s मा ध्यान केन्द्रित गर्दछ। %s"),
    PRED_AD_EFFECT_SUB_TEMPLATE("The %s sub-period adds %s.", "%s उप-अवधिले %s थप्छ।"),
    PRED_CALC_PROGRESS("Current period influences are being calculated.", "वर्तमान अवधिको प्रभावहरू गणना गरिँदैछ।"),
    PRED_YEARS_REMAINING("%.1f years remaining", "%.1f वर्ष बाँकी"),
    PRED_CURRENT("Current", "वर्तमान"),
    
    // Life Area - Career
    PRED_CAREER_SHORT("Focus on current projects and networking. New opportunities may emerge through professional contacts.", "हालका परियोजनाहरू र नेटवर्किङमा ध्यान दिनुहोस्। व्यावसायिक सम्पर्कहरू मार्फत नयाँ अवसरहरू आउन सक्छन्।"),
    PRED_CAREER_MED("Significant professional advancement possible. Leadership opportunities may present themselves.", "महत्त्वपूर्ण व्यावसायिक प्रगति सम्भव छ। नेतृत्वका अवसरहरू आउन सक्छन्।"),
    PRED_CAREER_LONG("Establishment as authority in your field. Long-term career goals manifest.", "आफ्नो क्षेत्रमा अधिकारको रूपमा स्थापना। दीर्घकालीन करियर लक्ष्यहरू पूरा हुन्छन्।"),
    PRED_CAREER_KEY_1("Professional networking", "व्यावसायिक नेटवर्किङ"),
    PRED_CAREER_KEY_2("Skill enhancement", "सीप वृद्धि"),
    PRED_CAREER_KEY_3("Leadership development", "नेतृत्व विकास"),
    PRED_CAREER_ADVICE("Take calculated risks and invest in professional development. Network actively and stay open to new opportunities.", "जोखिम मोल्नुहोस् र व्यावसायिक विकासमा लगानी गर्नुहोस्। सक्रिय रूपमा नेटवर्क बनाउनुहोस् र नयाँ अवसरहरूका लागि खुला रहनुहोस्।"),
    
    // Life Area - Finance
    PRED_FINANCE_SHORT("Steady income flow with potential for incremental growth. Good time to review budget and savings.", "क्रमिक वृद्धिको सम्भावना सहित स्थिर आय प्रवाह। बजेट र बचत समीक्षा गर्ने राम्रो समय।"),
    PRED_FINANCE_MED("Potential for larger gains through investments or promotions. Financial security strengthens.", "लगानी वा पदोन्नति मार्फत ठूलो लाभको सम्भावना। आर्थिक सुरक्षा बलियो हुन्छ।"),
    PRED_FINANCE_LONG("Wealth accumulation and financial independence. Legacy building period.", "धन सञ्चय र आर्थिक स्वतन्त्रता। विरासत निर्माण अवधि।"),
    PRED_FINANCE_KEY_1("Strategic investments", "रणनीतिक लगानी"),
    PRED_FINANCE_KEY_2("Multiple income streams", "बहु आय स्रोतहरू"),
    PRED_FINANCE_KEY_3("Financial discipline", "आर्थिक अनुशासन"),
    PRED_FINANCE_ADVICE("Create diversified income sources and maintain financial discipline. Seek expert advice for major investments.", "विविध आय स्रोतहरू सिर्जना गर्नुहोस् र आर्थिक अनुशासन कायम राख्नुहोस्। ठूला लगानीका लागि विशेषज्ञको सल्लाह लिनुहोस्।"),

    // Life Area - Relationships
    PRED_REL_SHORT("Existing relationships deepen. Communication is key for resolving minor conflicts.", "अवस्थित सम्बन्धहरू गहिरो हुन्छन्। सानातिना विवादहरू समाधान गर्न सञ्चार मुख्य हो।"),
    PRED_REL_MED("Important relationship milestones. Marriage or commitment possibilities for singles.", "महत्त्वपूर्ण सम्बन्ध कोसेढुङ्गाहरू। अविवाहितहरूका लागि विवाह वा प्रतिबद्धताको सम्भावना।"),
    PRED_REL_LONG("Mature, stable partnerships. Family life flourishes with mutual support.", "परिपक्व, स्थिर साझेदारीहरू। आपसी सहयोगमा पारिवारिक जीवन फस्टाउँछ।"),
    PRED_REL_KEY_1("Communication quality", "सञ्चार गुणस्तर"),
    PRED_REL_KEY_2("Emotional maturity", "भावनात्मक परिपक्वता"),
    PRED_REL_KEY_3("Shared values", "साझा मानहरू"),
    PRED_REL_ADVICE("Practice active listening and express appreciation regularly. Invest quality time in nurturing bonds.", "सक्रिय सुन्ने अभ्यास गर्नुहोस् र नियमित रूपमा प्रशंसा व्यक्त गर्नुहोस्। सम्बन्धहरू पोषण गर्न गुणस्तरीय समय लगानी गर्नुहोस्।"),

    // Life Area - Health
    PRED_HEALTH_SHORT("Maintain regular exercise and balanced diet. Energy levels are stable.", "नियमित व्यायाम र सन्तुलित आहार कायम राख्नुहोस्। ऊर्जा स्तर स्थिर छ।"),
    PRED_HEALTH_MED("Vitality improves with consistent practices. Consider preventive health measures.", "लगातार अभ्यासले जाँगर सुधार हुन्छ। रोकथाम स्वास्थ्य उपायहरू विचार गर्नुहोस्।"),
    PRED_HEALTH_LONG("Strong vitality and longevity indicators. Healthy lifestyle becomes natural.", "बलियो जाँगर र दीर्घायु सूचकहरू। स्वस्थ जीवनशैली स्वाभाविक बन्छ।"),
    PRED_HEALTH_KEY_1("Regular exercise", "नियमित व्यायाम"),
    PRED_HEALTH_KEY_2("Balanced nutrition", "सन्तुलित पोषण"),
    PRED_HEALTH_KEY_3("Stress management", "तनाव व्यवस्थापन"),
    PRED_HEALTH_ADVICE("Establish sustainable wellness routines. Prevention is better than cure - regular check-ups recommended.", "दिगो कल्याण दिनचर्याहरू स्थापना गर्नुहोस्। उपचार भन्दा रोकथाम राम्रो हो - नियमित चेक-अप सिफारिस गरिन्छ।"),
    
    // Life Area - Education
    PRED_EDU_SHORT("Good period for learning and skill development. Short courses bring benefits.", "सिकाइ र सीप विकासका लागि राम्रो अवधि। छोटो पाठ्यक्रमहरूले फाइदा ल्याउँछन्।"),
    PRED_EDU_MED("Major educational achievements or certifications. Academic success likely.", "प्रमुख शैक्षिक उपलब्धिहरू वा प्रमाणपत्रहरू। शैक्षिक सफलताको सम्भावना।"),
    PRED_EDU_LONG("Mastery in chosen field. Potential to become teacher or expert.", "रोजिएको क्षेत्रमा निपुणता। शिक्षक वा विशेषज्ञ बन्ने सम्भावना।"),
    PRED_EDU_KEY_1("Consistent study", "निरन्तर अध्ययन"),
    PRED_EDU_KEY_2("Practical application", "व्यावहारिक प्रयोग"),
    PRED_EDU_KEY_3("Mentor guidance", "मेन्टर मार्गदर्शन"),
    PRED_EDU_ADVICE("Apply theoretical knowledge practically. Seek mentors and engage in continuous learning.", "सैद्धान्तिक ज्ञानलाई व्यावहारिक रूपमा लागू गर्नुहोस्। मेन्टरहरू खोज्नुहोस् र निरन्तर सिकाइमा संलग्न हुनुहोस्।"),
    
    // Life Area - Family
    PRED_FAMILY_SHORT("Harmonious domestic atmosphere. Small celebrations or gatherings bring joy.", "सामंजस्यपूर्ण घरेलु वातावरण। साना उत्सव वा जमघटले आनन्द ल्याउँछ।"),
    PRED_FAMILY_MED("Family expansions or property matters. Supportive period for family bonds.", "पारिवारिक विस्तार वा सम्पत्ति मामिलाहरू। पारिवारिक बन्धनका लागि सहयोगी अवधि।"),
    PRED_FAMILY_LONG("Strong family foundation. Ancestral blessings and property matters resolved.", "बलियो पारिवारिक आधार। पुर्ख्यौली आशीर्वाद र सम्पत्ति मामिलाहरू समाधान।"),
    PRED_FAMILY_KEY_1("Quality time", "गुणस्तरीय समय"),
    PRED_FAMILY_KEY_2("Mutual respect", "आपसी सम्मान"),
    PRED_FAMILY_KEY_3("Emotional support", "भावनात्मक सहयोग"),
    PRED_FAMILY_ADVICE("Balance personal and family time. Address conflicts with patience and understanding.", "व्यक्तिगत र पारिवारिक समय सन्तुलन गर्नुहोस्। धैर्य र समझदारीका साथ द्वन्द्वहरू सम्बोधन गर्नुहोस्।"),

    // Life Area - Spiritual
    PRED_SPIRIT_SHORT("Daily practices bring mental peace. Insights come through meditation.", "दैनिक अभ्यासले मानसिक शान्ति ल्याउँछ। ध्यान मार्फत अन्तरदृष्टि आउँछ।"),
    PRED_SPIRIT_MED("Deepening spiritual understanding. Connection with teachers or spiritual communities.", "आध्यात्मिक समझ गहिरो हुँदै। शिक्षक वा आध्यात्मिक समुदायहरूसँग सम्बन्ध।"),
    PRED_SPIRIT_LONG("Significant spiritual evolution. Inner peace and wisdom established.", "महत्त्वपूर्ण आध्यात्मिक विकास। आन्तरिक शान्ति र ज्ञान स्थापित।"),
    PRED_SPIRIT_KEY_1("Daily practice", "दैनिक अभ्यास"),
    PRED_SPIRIT_KEY_2("Self-reflection", "आत्म-चिन्तन"),
    PRED_SPIRIT_KEY_3("Service to others", "अरूको सेवा"),
    PRED_SPIRIT_ADVICE("Maintain daily spiritual practices. Connect with like-minded seekers and serve others.", "दैनिक आध्यात्मिक अभ्यासहरू कायम राख्नुहोस्। समान विचारधारा भएका साधकहरूसँग जोड्नुहोस् र अरूको सेवा गर्नुहोस्।"),
    
    // Transits
    PRED_TRANSIT_SUN("Illuminating life purpose", "जीवन उद्देश्य प्रष्ट पार्दै"),
    PRED_TRANSIT_MOON("Emotional fluctuations", "भावनात्मक उतार-चढाव"),
    PRED_TRANSIT_MARS("Increased energy and drive", "बढेको ऊर्जा र उत्साह"),
    PRED_TRANSIT_MERCURY("Mental activity and communication", "मानसिक गतिविधि र सञ्चार"),
    PRED_TRANSIT_JUPITER("Growth and opportunities", "वृद्धि र अवसरहरू"),
    PRED_TRANSIT_VENUS("Harmony and relationships", "सद्भाव र सम्बन्धहरू"),
    PRED_TRANSIT_SATURN("Discipline and restrictions", "अनुशासन र प्रतिबन्धहरू"),
    PRED_TRANSIT_RAHU("Unexpected developments", "अप्रत्याशित घटनाक्रम"),
    PRED_TRANSIT_KETU("Spiritual awakening", "आध्यात्मिक जागरण"),
    PRED_TRANSIT_DEFAULT("Planetary influence active", "ग्रह प्रभाव सक्रिय"),
    
    // Remedial
    PRED_REMEDY_MANTRA("Perform %s mantra recitation during morning hours", "बिहानको समयमा %s मन्त्र जप गर्नुहोस्"),
    PRED_REMEDY_DONATE("Donate items associated with %s on appropriate days", "उपयुक्त दिनहरूमा %s सँग सम्बन्धित वस्तुहरू दान गर्नुहोस्"),
    PRED_REMEDY_GEM("Wear gemstone related to %s after proper consultation", "उचित परामर्श पछि %s सँग सम्बन्धित रत्न लगाउनुहोस्"),
    PRED_REMEDY_YOGA("Practice meditation and yoga to balance planetary energies", "ग्रह उर्जाहरू सन्तुलन गर्न ध्यान र योग अभ्यास गर्नुहोस्"),
    PRED_REMEDY_WORSHIP("Maintain regular worship and spiritual practices", "नियमित पूजा र आध्यात्मिक अभ्यासहरू कायम राख्नुहोस्"),

    // Active Yogas
    PRED_YOGA_DHANA_NAME("Dhana Yoga", "धन योग"),
    PRED_YOGA_DHANA_DESC("Wealth-creating planetary combination", "धन-सिर्जना गर्ने ग्रह संयोजन"),
    PRED_YOGA_DHANA_EFFECT("Favorable for financial growth and material prosperity", "आर्थिक वृद्धि र भौतिक समृद्धिको लागि अनुकूल"),
    PRED_YOGA_RAJA_NAME("Raja Yoga", "राज योग"),
    PRED_YOGA_RAJA_DESC("Royal combination indicating success", "सफलता संकेत गर्ने शाही संयोजन"),
    PRED_YOGA_RAJA_EFFECT("Support for leadership and achievement", "नेतृत्व र उपलब्धिका लागि सहयोग"),

    // Challenges
    PRED_CHALLENGE_CAREER_TRANS_NAME("Career Transitions", "करियर संक्रमण"),
    PRED_CHALLENGE_CAREER_TRANS_DESC("Period of professional reassessment and potential changes", "व्यावसायिक पुनर्मूल्यांकन र सम्भावित परिवर्तनहरूको अवधि"),
    PRED_CHALLENGE_CAREER_TRANS_MIT("Focus on skill development and networking", "सीप विकास र नेटवर्किङमा ध्यान दिनुहोस्"),

    // Opportunities
    PRED_OPP_FINANCE_NAME("Financial Growth", "आर्थिक वृद्धि"),
    PRED_OPP_FINANCE_DESC("Favorable period for investments and wealth accumulation", "लगानी र धन सञ्चयका लागि शुभ अवधि"),
    PRED_OPP_TIMING_6M("Next 6 months", "आगामी ६ महिना"),
    PRED_OPP_FINANCE_LEV("Explore new income sources and strategic investments", "नयाँ आय स्रोतहरू र रणनीतिक लगानीहरू अन्वेषण गर्नुहोस्"),
    
    PRED_OPP_PERSONAL_NAME("Personal Development", "व्यक्तिगत विकास"),
    PRED_OPP_PERSONAL_DESC("Excellent time for learning and self-improvement", "सिकाइ र आत्म-सुधारका लागि उत्कृष्ट समय"),
    PRED_OPP_PERSONAL_LEV("Enroll in courses, read extensively, seek mentorship", "पाठ्यक्रमहरूमा भर्ना हुनुहोस्, व्यापक रूपमा पढ्नुहोस्, मार्गदर्शन लिनुहोस्"),

    // Timing
    PRED_TIMING_JUPITER_REASON("Jupiter transit supports growth", "बृहस्पति गोचरले वृद्धिलाई सहयोग गर्छ"),
    PRED_TIMING_JUPITER_BEST_1("New ventures", "नयाँ उद्यमहरू"),
    PRED_TIMING_JUPITER_BEST_2("Important decisions", "महत्त्वपूर्ण निर्णयहरू"),
    PRED_TIMING_JUPITER_BEST_3("Relationships", "सम्बन्धहरू"),
    
    PRED_TIMING_SATURN_REASON("Saturn transit requires caution", "शनि गोचरले सावधानीको माग गर्छ"),
    PRED_TIMING_SATURN_AVOID_1("Major investments", "ठूला लगानीहरू"),
    PRED_TIMING_SATURN_AVOID_2("Hasty decisions", "हतारका निर्णयहरू"),
    PRED_TIMING_SATURN_AVOID_3("Conflicts", "द्वन्द्वहरू"),
    
    PRED_TIMING_JUPITER_ASPECT_EVENT("Favorable Jupiter Aspect", "शुभ बृहस्पति दृष्टि"),
    PRED_TIMING_JUPITER_ASPECT_SIG("Excellent for new beginnings", "नयाँ सुरुवातका लागि उत्कृष्ट"),

    // UI Labels
    PRED_LABEL_KEY_FACTORS("Key Factors", "मुख्य कारकहरू"),
    PRED_LABEL_ADVICE("Advice", "सल्लाह"),
    PRED_REMEDY_FOR_PERIOD("For your current %s period:", "तपाईंको वर्तमान %s अवधिको लागि:")
}
