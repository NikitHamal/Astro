package com.astro.storm.core.common

/**
 * String keys for Advanced Analysis Screens
 * Includes: Maraka, Badhaka, Vipareeta Raja Yoga, Ishta Kashta Phala, Panch Mahapurusha
 */
enum class StringKeyDoshaAdvanced(
    override val en: String,
    override val ne: String,
) : StringKeyInterface {

    // ============================================
    // MARAKA ANALYSIS SCREEN
    // ============================================
    MARAKA_SCREEN_TITLE("Maraka Analysis", "मारक विश्लेषण"),
    MARAKA_SUBTITLE("Longevity & Health Planets", "आयु र स्वास्थ्य ग्रहहरू"),
    MARAKA_TAB_PLANETS("Planets", "ग्रहहरू"),
    MARAKA_TAB_LONGEVITY("Longevity", "आयु"),
    MARAKA_PRIMARY("Primary", "प्राथमिक"),
    MARAKA_SECONDARY("Secondary", "माध्यमिक"),
    MARAKA_PERIODS("Periods", "अवधि"),
    MARAKA_LONGEVITY_SCORE("Longevity Score", "आयु स्कोर"),
    MARAKA_PRIMARY_TITLE("Primary Maraka Planets", "प्राथमिक मारक ग्रहहरू"),
    MARAKA_SECONDARY_TITLE("Secondary Maraka Planets", "माध्यमिक मारक ग्रहहरू"),
    MARAKA_NO_SIGNIFICANT("No Significant Maraka Influences", "कुनै महत्त्वपूर्ण मारक प्रभाव छैन"),
    MARAKA_PROTECTIVE("Protective Factors", "सुरक्षात्मक कारकहरू"),
    MARAKA_AFFLICTIONS("Afflictions", "पीडाहरू"),
    MARAKA_METHODS("Longevity Assessment Methods", "आयु मूल्याङ्कन विधिहरू"),
    MARAKA_AMSAYURDAYA("Amsayurdaya", "अंशायुर्दाय"),
    MARAKA_PINDAYURDAYA("Pindayurdaya", "पिण्डायुर्दाय"),
    MARAKA_NISARGAYURDAYA("Nisargayurdaya", "निसर्गायुर्दाय"),
    MARAKA_FACTORS("Longevity Factors", "आयु कारकहरू"),
    MARAKA_POSITIVE("Positive Factors", "सकारात्मक कारकहरू"),
    MARAKA_NEGATIVE("Negative Factors", "नकारात्मक कारकहरू"),
    MARAKA_CRITICAL_PERIODS("Critical Dasha Periods", "महत्त्वपूर्ण दशा अवधि"),
    MARAKA_NO_CRITICAL("No Critical Periods Identified", "कुनै महत्त्वपूर्ण अवधि पहिचान गरिएको छैन"),
    MARAKA_NO_REMEDIES("No specific remedies needed at this time.", "यस समयमा कुनै विशेष उपाय आवश्यक छैन।"),
    MARAKA_ANALYZING("Analyzing Maraka influences...", "मारक प्रभाव विश्लेषण गर्दै..."),
    MARAKA_NO_CHART_DESC("Please generate a chart to view Maraka analysis.", "मारक विश्लेषण हेर्न कृपया कुण्डली बनाउनुहोस्।"),
    MARAKA_ABOUT_TITLE("About Maraka Analysis", "मारक विश्लेषणको बारेमा"),
    MARAKA_ABOUT_DESC("Maraka planets are associated with the 2nd and 7th houses, which are maraka sthanas (death-inflicting houses). The lords of these houses and planets placed in them can indicate challenging periods for health and longevity. This analysis helps identify such influences and provides remedial guidance based on classical Vedic astrology principles.", "मारक ग्रहहरू दोस्रो र सातौं भाव (मारक स्थान) सँग सम्बन्धित छन्। यी भावका स्वामी र यहाँ रहेका ग्रहहरूले स्वास्थ्य र आयुको लागि चुनौतीपूर्ण अवधि संकेत गर्न सक्छन्। यो विश्लेषणले शास्त्रीय वैदिक ज्योतिष सिद्धान्तहरूमा आधारित त्यस्ता प्रभावहरू पहिचान गर्न र उपायात्मक मार्गदर्शन प्रदान गर्न मद्दत गर्दछ।"),

    // ============================================
    // BADHAKA ANALYSIS SCREEN
    // ============================================
    BADHAKA_SCREEN_TITLE("Badhaka Analysis", "बाधक विश्लेषण"),
    BADHAKA_SUBTITLE("Obstruction Analysis", "अवरोध विश्लेषण"),
    BADHAKA_TAB_PLANETS("Planets", "ग्रहहरू"),
    BADHAKA_TAB_OBSTACLES("Obstacles", "अवरोधहरू"),
    BADHAKA_STHANA("Badhaka Sthana", "बाधक स्थान"),
    BADHAKA_PRIMARY("Primary", "प्राथमिक"),
    BADHAKA_SECONDARY("Secondary", "माध्यमिक"),
    BADHAKA_SEVERITY("Severity", "गम्भीरता"),
    BADHAKA_PRIMARY_TITLE("Primary Badhaka Planets", "प्राथमिक बाधक ग्रहहरू"),
    BADHAKA_SECONDARY_TITLE("Secondary Badhaka Planets", "माध्यमिक बाधक ग्रहहरू"),
    BADHAKA_NO_SIGNIFICANT("No Significant Badhaka Influences", "कुनै महत्त्वपूर्ण बाधक प्रभाव छैन"),
    BADHAKA_MITIGATING("Mitigating Factors", "न्यूनीकरण कारकहरू"),
    BADHAKA_LORD("Badhaka Lord", "बाधक स्वामी"),
    BADHAKA_OBSTACLE_TYPES("Obstacle Types", "अवरोध प्रकारहरू"),
    BADHAKA_AFFECTED_AREAS("Affected Life Areas", "प्रभावित जीवन क्षेत्रहरू"),
    BADHAKA_NO_AREAS("No significantly affected areas identified.", "कुनै महत्त्वपूर्ण प्रभावित क्षेत्र पहिचान गरिएको छैन।"),
    BADHAKA_NATURE("Nature of Obstacles", "अवरोधको प्रकृति"),
    BADHAKA_CRITICAL_PERIODS("Critical Obstruction Periods", "महत्त्वपूर्ण अवरोध अवधि"),
    BADHAKA_NO_CRITICAL("No Critical Periods Identified", "कुनै महत्त्वपूर्ण अवधि पहिचान गरिएको छैन"),
    BADHAKA_NO_REMEDIES("No specific remedies needed at this time.", "यस समयमा कुनै विशेष उपाय आवश्यक छैन।"),
    BADHAKA_ANALYZING("Analyzing Badhaka influences...", "बाधक प्रभाव विश्लेषण गर्दै..."),
    BADHAKA_NO_CHART_DESC("Please generate a chart to view Badhaka analysis.", "बाधक विश्लेषण हेर्न कृपया कुण्डली बनाउनुहोस्।"),
    BADHAKA_ABOUT_TITLE("About Badhaka Analysis", "बाधक विश्लेषणको बारेमा"),
    BADHAKA_ABOUT_DESC("Badhaka planets create obstacles and hindrances in life. The Badhaka Sthana (obstructing house) varies based on the modality of the ascendant sign: 11th for movable signs, 9th for fixed signs, and 7th for dual signs. Understanding Badhaka influences helps navigate challenging periods and apply appropriate remedies.", "बाधक ग्रहहरूले जीवनमा अवरोध र बाधाहरू सिर्जना गर्दछन्। बाधक स्थान लग्न राशिको प्रकृति अनुसार फरक हुन्छ: चर राशिको लागि ११औं, स्थिर राशिको लागि ९औं, र द्विस्वभाव राशिको लागि ७औं। बाधक प्रभावहरू बुझ्दा चुनौतीपूर्ण अवधिहरू पार गर्न र उचित उपायहरू लागू गर्न मद्दत गर्दछ।"),

    // ============================================
    // VIPAREETA RAJA YOGA SCREEN
    // ============================================
    VIPAREETA_SCREEN_TITLE("Vipareeta Raja Yoga", "विपरीत राजयोग"),
    VIPAREETA_SUBTITLE("Reverse Fortune Yogas", "उल्टो भाग्य योगहरू"),
    VIPAREETA_TAB_BENEFITS("Benefits", "लाभहरू"),
    VIPAREETA_TAB_FACTORS("Factors", "कारकहरू"),
    VIPAREETA_NO_YOGAS("No Vipareeta Raja Yogas", "कुनै विपरीत राजयोग छैन"),
    VIPAREETA_ONE_YOGA("One Vipareeta Raja Yoga", "एक विपरीत राजयोग"),
    VIPAREETA_TWO_YOGAS("Two Vipareeta Raja Yogas", "दुई विपरीत राजयोग"),
    VIPAREETA_ALL_YOGAS("All Three Vipareeta Raja Yogas!", "तीनवटै विपरीत राजयोग!"),
    VIPAREETA_STRENGTH("Overall Strength", "समग्र शक्ति"),
    VIPAREETA_YOGAS_FORMED("Yogas Formed", "बनेका योगहरू"),
    VIPAREETA_EXCHANGES("Exchanges", "परिवर्तनहरू"),
    VIPAREETA_ACTIVATIONS("Activations", "सक्रियताहरू"),
    VIPAREETA_EXCHANGES_TITLE("Dusthana Parivartana (Exchanges)", "दुस्थान परिवर्तन"),
    VIPAREETA_BENEFITS("Benefits", "लाभहरू"),
    VIPAREETA_PRIMARY_BENEFITS("Primary Benefits", "प्राथमिक लाभहरू"),
    VIPAREETA_NO_BENEFITS("No specific benefits identified without active yogas.", "सक्रिय योग बिना कुनै विशेष लाभ पहिचान गरिएको छैन।"),
    VIPAREETA_ACTIVATION_PERIODS("Activation Periods", "सक्रियता अवधि"),
    VIPAREETA_NO_TIMING("No activation periods without active yogas.", "सक्रिय योग बिना कुनै सक्रियता अवधि छैन।"),
    VIPAREETA_ENHANCEMENT("Enhancement Factors", "वृद्धि कारकहरू"),
    VIPAREETA_CANCELLATION("Cancellation Factors", "रद्द कारकहरू"),
    VIPAREETA_NO_FACTORS("No significant enhancement or cancellation factors.", "कुनै महत्त्वपूर्ण वृद्धि वा रद्द कारकहरू छैनन्।"),
    VIPAREETA_ANALYZING("Analyzing Vipareeta Raja Yogas...", "विपरीत राजयोग विश्लेषण गर्दै..."),
    VIPAREETA_NO_CHART_DESC("Please generate a chart to view Vipareeta Raja Yoga analysis.", "विपरीत राजयोग विश्लेषण हेर्न कृपया कुण्डली बनाउनुहोस्।"),
    VIPAREETA_ABOUT_TITLE("About Vipareeta Raja Yoga", "विपरीत राजयोगको बारेमा"),
    VIPAREETA_ABOUT_DESC("Vipareeta Raja Yogas are formed when lords of dusthana houses (6th, 8th, 12th) are placed in other dusthana houses. The three yogas are: Harsha (6th lord in 6/8/12), Sarala (8th lord in 6/8/12), and Vimala (12th lord in 6/8/12). These yogas operate on the principle that 'negative times negative equals positive', transforming adversity into success and gains from unexpected sources.", "विपरीत राजयोग दुस्थान भावका स्वामी (६, ८, १२) अन्य दुस्थान भावमा रहेमा बन्दछन्। तीन योगहरू: हर्ष (६औंको स्वामी ६/८/१२मा), सरल (८औंको स्वामी ६/८/१२मा), र विमल (१२औंको स्वामी ६/८/१२मा)। यी योगहरू 'नकारात्मक गुणा नकारात्मक बराबर सकारात्मक' सिद्धान्तमा काम गर्दछन्, प्रतिकूलतालाई सफलतामा र अप्रत्याशित स्रोतबाट लाभमा रूपान्तरण गर्दछन्।"),

    // ============================================
    // ISHTA KASHTA PHALA SCREEN
    // ============================================
    ISHTA_KASHTA_SCREEN_TITLE("Ishta Kashta Phala", "इष्ट कष्ट फल"),
    ISHTA_KASHTA_SUBTITLE("Benefic & Malefic Results", "शुभ र अशुभ फलहरू"),
    ISHTA_KASHTA_TAB_PLANETS("Planets", "ग्रहहरू"),
    ISHTA_KASHTA_TAB_LIFE_AREAS("Life Areas", "जीवन क्षेत्रहरू"),
    ISHTA_KASHTA_ISHTA("Ishta (Benefic)", "इष्ट (शुभ)"),
    ISHTA_KASHTA_KASHTA("Kashta (Malefic)", "कष्ट (अशुभ)"),
    ISHTA_KASHTA_BENEFIC("Benefic", "शुभ"),
    ISHTA_KASHTA_NEUTRAL("Neutral", "तटस्थ"),
    ISHTA_KASHTA_MALEFIC("Malefic", "अशुभ"),
    ISHTA_KASHTA_MOST_BENEFIC("Most Benefic", "सबैभन्दा शुभ"),
    ISHTA_KASHTA_MOST_MALEFIC("Most Malefic", "सबैभन्दा अशुभ"),
    ISHTA_KASHTA_UCCHA_BALA("Uccha Bala", "उच्च बल"),
    ISHTA_KASHTA_CHESTA_BALA("Chesta Bala", "चेष्टा बल"),
    ISHTA_KASHTA_AFFECTS("Affects", "प्रभाव"),
    ISHTA_KASHTA_LIFE_AREAS("Life Area Impact Analysis", "जीवन क्षेत्र प्रभाव विश्लेषण"),
    ISHTA_KASHTA_INFLUENCED_BY("Influenced by:", "प्रभावित:"),
    ISHTA_KASHTA_PERIOD_PREDICTIONS("Dasha Period Predictions", "दशा अवधि भविष्यवाणी"),
    ISHTA_KASHTA_BENEFITS("Expected Benefits", "अपेक्षित लाभहरू"),
    ISHTA_KASHTA_CHALLENGES("Potential Challenges", "सम्भावित चुनौतीहरू"),
    ISHTA_KASHTA_ANALYZING("Analyzing Ishta Kashta Phala...", "इष्ट कष्ट फल विश्लेषण गर्दै..."),
    ISHTA_KASHTA_NO_CHART_DESC("Please generate a chart to view Ishta Kashta Phala analysis.", "इष्ट कष्ट फल विश्लेषण हेर्न कृपया कुण्डली बनाउनुहोस्।"),
    ISHTA_KASHTA_ABOUT_TITLE("About Ishta Kashta Phala", "इष्ट कष्ट फलको बारेमा"),
    ISHTA_KASHTA_ABOUT_DESC("Ishta Kashta Phala is derived from Shadbala calculations per BPHS. Ishta Phala (benefic result) is calculated as √(Uccha Bala × Chesta Bala), while Kashta Phala (malefic result) is √((60-Uccha Bala) × (60-Chesta Bala)). A planet with high Ishta and low Kashta delivers excellent results, while the opposite indicates challenges. Net Phala (Ishta minus Kashta) reveals a planet's overall tendency to produce favorable or unfavorable outcomes.", "इष्ट कष्ट फल BPHS अनुसार षडबल गणनाबाट निकालिन्छ। इष्ट फल (शुभ फल) = √(उच्च बल × चेष्टा बल), कष्ट फल (अशुभ फल) = √((60-उच्च बल) × (60-चेष्टा बल))। उच्च इष्ट र कम कष्ट भएको ग्रहले उत्कृष्ट फल दिन्छ, जबकि उल्टोले चुनौतीहरू संकेत गर्दछ। नेट फल (इष्ट माइनस कष्ट) ले ग्रहको अनुकूल वा प्रतिकूल परिणामहरू उत्पादन गर्ने समग्र प्रवृत्ति देखाउँछ।"),

    // ============================================
    // PANCH MAHAPURUSHA SCREEN
    // ============================================
    PANCHA_SCREEN_TITLE("Panch Mahapurusha Yoga", "पञ्च महापुरुष योग"),
    PANCHA_SUBTITLE("Five Great Person Yogas", "पाँच महापुरुष योगहरू"),
    PANCHA_ABOUT_TITLE("About Panch Mahapurusha Yoga", "पञ्च महापुरुष योगको बारेमा"),
    PANCHA_ABOUT_DESC("Panch Mahapurusha Yoga (Five Great Person Yogas) are special planetary combinations that indicate exceptional qualities and achievements.\n\nThe five yogas are:\n• Ruchaka (Mars) - Courage, leadership, military prowess\n• Bhadra (Mercury) - Intelligence, communication, commerce\n• Hamsa (Jupiter) - Wisdom, spirituality, fortune\n• Malavya (Venus) - Beauty, luxury, artistic talents\n• Sasha (Saturn) - Discipline, authority, longevity\n\nFormation requirements:\nThe planet must be in a Kendra house (1st, 4th, 7th, or 10th) AND in its own sign or exaltation sign.\n\nHaving one or more of these yogas in a chart indicates the native will possess the exceptional qualities of that planet and achieve success in related areas.", "पञ्च महापुरुष योगहरू विशेष ग्रह संयोजनहरू हुन् जसले असाधारण गुणहरू र उपलब्धिहरूलाई संकेत गर्दछ।\n\nपाँच योगहरू हुन्:\n• रुचक (मंगल) - साहस, नेतृत्व, सैन्य कौशल\n• भद्र (बुध) - बुद्धि, सञ्चार, वाणिज्य\n• हंस (बृहस्पति) - ज्ञान, आध्यात्मिकता, भाग्य\n• मालव्य (शुक्र) - सौन्दर्य, विलासिता, कलात्मक प्रतिभा\n• शश (शनि) - अनुशासन, अधिकार, दीर्घायु\n\nबन्ने शर्तहरू:\nग्रह केन्द्र भाव (१, ४, ७, वा १०) मा हुनुपर्छ र आफ्नै राशि वा उच्च राशिमा हुनुपर्छ।\n\nकुण्डलीमा यी मध्ये एक वा बढी योगहरू हुनुले जातकमा त्यस ग्रहका असाधारण गुणहरू हुनेछन् र सम्बन्धित क्षेत्रमा सफलता प्राप्त गर्नेछन् भन्ने संकेत गर्दछ।"),
    PANCHA_NO_YOGAS_DISPLAY("No Yogas to Display", "देखाउनको लागि कुनै योगहरू छैनन्"),
    PANCHA_NO_YOGAS_DESC("For a Mahapurusha Yoga to form, Mars, Mercury, Jupiter, Venus, or Saturn must be in Kendra (1,4,7,10) in its own or exaltation sign.", "महापुरुष योग बन्नको लागि, मंगल, बुध, बृहस्पति, शुक्र वा शनि केन्द्र (१, ४, ७, १०) मा आफ्नै वा उच्च राशिमा हुनुपर्छ।"),
    PANCHA_STATUS_FOUND("Yoga(s) Found!", "योग(हरू) फेला पर्यो!"),
    PANCHA_STATUS_FOUND_DESC("You have %1\$d Panch Mahapurusha Yoga(s) in your chart", "तपाईंको कुण्डलीमा %1\$d पञ्च महापुरुष योग(हरू) छन्"),
    PANCHA_STATUS_NONE_DESC("None of the five Mahapurusha Yogas are formed", "पाँच महापुरुष योगहरू मध्ये कुनै पनि बनेको छैन"),
    PANCHA_COMBINED("Combined Effects", "संयुक्त प्रभावहरू"),
    PANCHA_SYNERGIES("Synergies:", "समन्वयहरू:"),
    PANCHA_NO_CHART_DESC("Create or select a birth chart to analyze Panch Mahapurusha Yogas.", "पञ्च महापुरुष योगहरू विश्लेषण गर्न जन्म कुण्डली सिर्जना गर्नुहोस् वा छान्नुहोस्।"),
    PANCHA_NO_YOGAS("No Yogas", "कुनै योग छैन"),
    PANCHA_STRENGTH("Strength", "बल"),
    PANCHA_PERIODS("Periods", "अवधिहरू"),
    PANCHA_EFFECTS_TITLE("%s Effects", "%s प्रभावहरू"),
    PANCHA_PHYSICAL("Physical", "शारीरिक"),
    PANCHA_MENTAL("Mental", "मानसिक"),
    PANCHA_CAREER("Career", "करियर"),
    PANCHA_SPIRITUAL("Spiritual", "आध्यात्मिक"),
    PANCHA_RELATIONSHIPS("Relationships", "सम्बन्ध"),
    PANCHA_ACTIVATION_PERIODS("Activation Periods", "सक्रियता अवधिहरू"),
    PANCHA_RECOMMENDATIONS_TITLE("%s Recommendations", "%s सिफारिसहरू"),
    PANCHA_TIMING_LABEL("Timing: %s", "समय: %s"),
    PANCHA_DETAILS_EMPTY_DESC("This section will show details when Mahapurusha Yogas are present in the chart.", "यस खण्डले कुण्डलीमा महापुरुष योगहरू उपस्थित हुँदा विवरणहरू देखाउनेछ।"),
    PANCHA_ANALYZING("Analyzing Panch Mahapurusha Yogas...", "पञ्च महापुरुष योगहरू विश्लेषण गर्दै..."),

    // Pancha Score labels
    PANCHA_EXCELLENT("Excellent (5+)", "उत्कृष्ट (५+)"),
    PANCHA_GOOD("Good (4)", "राम्रो (४)"),
    PANCHA_AVERAGE("Average (3)", "औसत (३)"),
    PANCHA_BELOW_AVERAGE("Below Average (2)", "औसत भन्दा कम (२)"),
    PANCHA_WEAK("Weak (0-1)", "कमजोर (०-१)"),
;
}
