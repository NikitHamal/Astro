package com.astro.storm.core.common

/**
 * Localization keys for Native Analysis - Comprehensive personality and life analysis
 * Full English (EN) and Nepali (NE) support for detailed chart interpretation
 */
enum class StringKeyNative(override val en: String, override val ne: String) : StringKeyInterface {

    // ═══════════════════════════════════════════════════════════════════════════
    // SECTION: GENERAL LABELS
    // ═══════════════════════════════════════════════════════════════════════════

    NATIVE_ANALYSIS_TITLE("Native Analysis", "जातक विश्लेषण"),
    NATIVE_ANALYSIS_SUBTITLE("Comprehensive Life Profile", "व्यापक जीवन प्रोफाइल"),

    SECTION_CHARACTER("Character & Personality", "चरित्र र व्यक्तित्व"),
    SECTION_CAREER("Career & Profession", "पेशा र व्यवसाय"),
    SECTION_MARRIAGE("Marriage & Relationships", "विवाह र सम्बन्ध"),
    SECTION_HEALTH("Health & Longevity", "स्वास्थ्य र आयु"),
    SECTION_WEALTH("Wealth & Finance", "धन र वित्त"),
    SECTION_EDUCATION("Education & Knowledge", "शिक्षा र ज्ञान"),
    SECTION_SPIRITUAL("Spiritual Path", "आध्यात्मिक मार्ग"),

    LABEL_OVERVIEW("Overview", "सारांश"),
    LABEL_STRENGTHS("Key Strengths", "मुख्य शक्तिहरू"),
    LABEL_CHALLENGES("Challenges", "चुनौतीहरू"),
    LABEL_FAVORABLE_PERIODS("Favorable Periods", "अनुकूल समय"),
    LABEL_REMEDIES("Remedial Measures", "उपाय"),
    LABEL_PLANETARY_INFLUENCE("Planetary Influence", "ग्रह प्रभाव"),
    LABEL_HOUSE_ANALYSIS("House Analysis", "भाव विश्लेषण"),
    LABEL_PREDICTIONS("Predictions", "भविष्यवाणी"),

    // Native Analysis UI Specific Titles
    TITLE_ASCENDANT_TRAITS("Ascendant Traits", "लग्नका विशेषताहरू"),
    TITLE_MOON_SIGN_INFLUENCE("Moon Sign Influence", "चन्द्र राशिको प्रभाव"),
    TITLE_NAKSHATRA_INFLUENCE("Nakshatra Influence", "नक्षत्रको प्रभाव"),
    LABEL_PERSONALITY_FOUNDATION("Personality Foundation", "व्यक्तित्वको आधार"),
    TITLE_CAREER_STRENGTH("Career Strength", "क्यारियरको बल"),
    LABEL_FAVORABLE_CAREER_FIELDS("Favorable Career Fields", "अनुकूल क्यारियर क्षेत्रहरू"),
    LABEL_CAREER_INDICATORS("Career Indicators", "क्यारियरका सूचकहरू"),
    TITLE_RELATIONSHIP_STRENGTH("Relationship Strength", "सम्बन्धको बल"),
    LABEL_MARRIAGE_TIMING("Marriage Timing", "विवाहको समय"),
    LABEL_VENUS_STRENGTH("Venus Strength", "शुक्रको बल"),
    LABEL_SPOUSE_NATURE("Spouse Nature", "जीवनसाथीको स्वभाव"),
    LABEL_LONGEVITY_INDICATOR("Longevity Indicator", "दीर्घायु सूचक"),
    LABEL_VULNERABLE_AREAS("Vulnerable Areas", "कमजोर क्षेत्रहरू"),
    LABEL_HEALTH_CONCERNS("Health Concerns", "स्वास्थ्य चिन्ताहरू"),
    TITLE_WEALTH_POTENTIAL("Wealth Potential", "धनको सम्भावना"),
    LABEL_DHANA_YOGA_PRESENT("Dhana Yoga Present", "धन योग उपस्थित"),
    LABEL_WEALTH_LORDS("Wealth Lords", "धनका स्वामीहरू"),
    LABEL_PRIMARY_WEALTH_SOURCES("Primary Wealth Sources", "मुख्य धन स्रोतहरू"),
    TITLE_ACADEMIC_POTENTIAL("Academic Potential", "शैक्षिक सम्भावना"),
    LABEL_MERCURY_STRENGTH("Mercury Strength", "बुधको बल"),
    LABEL_JUPITER_BLESSING("Jupiter's Blessing", "बृहस्पतिको आशीर्वाद"),
    LABEL_FAVORABLE_SUBJECTS("Favorable Subjects", "अनुकूल विषयहरू"),
    TITLE_SPIRITUAL_INCLINATION("Spiritual Inclination", "आध्यात्मिक झुकाव"),
    LABEL_SPIRITUAL_INDICATORS("Spiritual Indicators", "आध्यात्मिक सूचकहरू"),
    LABEL_RECOMMENDED_PRACTICES("Recommended Practices", "सिफारिस गरिएका अभ्यासहरू"),
    LABEL_IN_HOUSE("In House", "भावमा"),
    LABEL_PLANETS_IN_HOUSE("Planets in %dth House", "%dऔं भावमा ग्रहहरू"),
    LABEL_ANALYZING_CHART("Analyzing chart...", "कुण्डली विश्लेषण गर्दै..."),
    LABEL_ANALYSIS_ERROR("Analysis Error", "विश्लेषण त्रुटि"),
    LABEL_NO_CHART_SELECTED("No Chart Selected", "कुनै कुण्डली छानिएको छैन"),
    LABEL_SELECT_CHART_DESC("Please select a birth chart to view native analysis", "जातक विश्लेषण हेर्न कृपया जन्म कुण्डली छान्नुहोस्"),
    LABEL_COMPREHENSIVE_DESC("This comprehensive analysis examines your birth chart to provide insights into various life areas including personality, career, relationships, health, wealth, education, and spiritual path.", "यो व्यापक विश्लेषणले व्यक्तित्व, क्यारियर, सम्बन्ध, स्वास्थ्य, धन, शिक्षा, र आध्यात्मिक मार्ग सहित विभिन्न जीवन क्षेत्रहरूमा अन्तर्दृष्टि प्रदान गर्न तपाईंको जन्म कुण्डलीको परीक्षण गर्दछ।"),

    STRENGTH_EXCELLENT("Excellent", "उत्कृष्ट"),
    STRENGTH_STRONG("Strong", "बलियो"),
    STRENGTH_MODERATE("Moderate", "मध्यम"),
    STRENGTH_WEAK("Weak", "कमजोर"),
    STRENGTH_AFFLICTED("Afflicted", "पीडित"),

    // ═══════════════════════════════════════════════════════════════════════════
    // SECTION: CHARACTER & PERSONALITY TRAITS
    // ═══════════════════════════════════════════════════════════════════════════

    // Ascendant-based character traits
    CHAR_ARIES_ASC("You are a natural leader with pioneering spirit, courage, and dynamic energy. Your assertive nature drives you to take initiative in all matters.",
        "तपाईं प्राकृतिक नेता हुनुहुन्छ अग्रगामी भावना, साहस र गतिशील ऊर्जाको साथ। तपाईंको दृढ स्वभावले तपाईंलाई सबै कुरामा पहल गर्न प्रेरित गर्छ।"),
    CHAR_TAURUS_ASC("You possess a stable, practical nature with strong aesthetic sensibilities. Your patience and determination bring steady progress in life.",
        "तपाईंसँग स्थिर, व्यावहारिक स्वभाव छ बलियो सौन्दर्य बोधको साथ। तपाईंको धैर्य र दृढताले जीवनमा स्थिर प्रगति ल्याउँछ।"),
    CHAR_GEMINI_ASC("You are intellectually curious, adaptable, and communicative. Your versatile mind excels in learning and social interactions.",
        "तपाईं बौद्धिक रूपमा जिज्ञासु, अनुकूलनशील र संवादशील हुनुहुन्छ। तपाईंको बहुमुखी मनले सिक्ने र सामाजिक अन्तरक्रियामा उत्कृष्ट प्रदर्शन गर्छ।"),
    CHAR_CANCER_ASC("You are emotionally intuitive, nurturing, and protective. Your strong connection to family and home brings you deep fulfillment.",
        "तपाईं भावनात्मक रूपमा अन्तर्ज्ञानी, पालनपोषण गर्ने र सुरक्षात्मक हुनुहुन्छ। परिवार र घरसँगको तपाईंको बलियो सम्बन्धले तपाईंलाई गहिरो सन्तुष्टि दिन्छ।"),
    CHAR_LEO_ASC("You possess natural charisma, creativity, and leadership qualities. Your generous heart and confident demeanor inspire those around you.",
        "तपाईंसँग प्राकृतिक करिश्मा, रचनात्मकता र नेतृत्व गुणहरू छन्। तपाईंको उदार हृदय र आत्मविश्वासपूर्ण व्यवहारले वरिपरिका मानिसहरूलाई प्रेरित गर्छ।"),
    CHAR_VIRGO_ASC("You are analytical, detail-oriented, and service-minded. Your practical approach and perfectionist tendencies ensure quality in all endeavors.",
        "तपाईं विश्लेषणात्मक, विस्तार-उन्मुख र सेवा-भावित हुनुहुन्छ। तपाईंको व्यावहारिक दृष्टिकोण र पूर्णतावादी प्रवृत्तिले सबै प्रयासहरूमा गुणस्तर सुनिश्चित गर्छ।"),
    CHAR_LIBRA_ASC("You value harmony, balance, and beauty in all aspects of life. Your diplomatic nature and sense of justice make you an excellent mediator.",
        "तपाईं जीवनका सबै पक्षहरूमा सामंजस्य, सन्तुलन र सौन्दर्यलाई मूल्यवान मान्नुहुन्छ। तपाईंको कूटनीतिक स्वभाव र न्यायको भावनाले तपाईंलाई उत्कृष्ट मध्यस्थकर्ता बनाउँछ।"),
    CHAR_SCORPIO_ASC("You possess intense emotional depth, transformative power, and strong intuition. Your determination and resilience help you overcome any obstacle.",
        "तपाईंसँग तीव्र भावनात्मक गहिराइ, परिवर्तनकारी शक्ति र बलियो अन्तर्ज्ञान छ। तपाईंको दृढता र लचिलोपनले तपाईंलाई कुनै पनि बाधा पार गर्न मद्दत गर्छ।"),
    CHAR_SAGITTARIUS_ASC("You are philosophical, adventurous, and optimistic. Your love for knowledge and exploration expands your horizons continually.",
        "तपाईं दार्शनिक, साहसिक र आशावादी हुनुहुन्छ। ज्ञान र अन्वेषणको तपाईंको प्रेमले तपाईंको क्षितिजलाई निरन्तर विस्तार गर्छ।"),
    CHAR_CAPRICORN_ASC("You are ambitious, disciplined, and practical. Your patient approach and strong work ethic lead to lasting achievements.",
        "तपाईं महत्वाकांक्षी, अनुशासित र व्यावहारिक हुनुहुन्छ। तपाईंको धैर्यपूर्ण दृष्टिकोण र बलियो कार्य नैतिकताले स्थायी उपलब्धिहरूमा पुर्‍याउँछ।"),
    CHAR_AQUARIUS_ASC("You are innovative, humanitarian, and independent-minded. Your original thinking and progressive ideas benefit society at large.",
        "तपाईं नवीन, मानवतावादी र स्वतन्त्र सोचाइ भएको हुनुहुन्छ। तपाईंको मौलिक सोच र प्रगतिशील विचारहरूले समाजलाई फाइदा पुर्‍याउँछ।"),
    CHAR_PISCES_ASC("You are compassionate, intuitive, and spiritually inclined. Your artistic sensibility and empathetic nature connect you deeply with others.",
        "तपाईं करुणामय, अन्तर्ज्ञानी र आध्यात्मिक प्रवृत्तिका हुनुहुन्छ। तपाईंको कलात्मक संवेदनशीलता र सहानुभूतिपूर्ण स्वभावले तपाईंलाई अरूसँग गहिरो रूपमा जोड्छ।"),

    // Moon sign personality traits
    MOON_ARIES("Your emotional nature is fiery and spontaneous. You react quickly to situations and need independence in emotional expression.",
        "तपाईंको भावनात्मक स्वभाव अग्निमय र स्वत:स्फूर्त छ। तपाईं परिस्थितिहरूमा छिटो प्रतिक्रिया दिनुहुन्छ र भावनात्मक अभिव्यक्तिमा स्वतन्त्रता चाहिन्छ।"),
    MOON_TAURUS("Your emotional nature is stable and sensual. You seek comfort and security, and are deeply loyal in relationships.",
        "तपाईंको भावनात्मक स्वभाव स्थिर र संवेदनशील छ। तपाईं आराम र सुरक्षा खोज्नुहुन्छ, र सम्बन्धहरूमा गहिरो वफादार हुनुहुन्छ।"),
    MOON_GEMINI("Your emotional nature is curious and changeable. You process feelings intellectually and need mental stimulation.",
        "तपाईंको भावनात्मक स्वभाव जिज्ञासु र परिवर्तनशील छ। तपाईं भावनाहरूलाई बौद्धिक रूपमा प्रशोधन गर्नुहुन्छ र मानसिक उत्तेजना चाहिन्छ।"),
    MOON_CANCER("Your emotional nature is deeply sensitive and nurturing. You have strong intuition and need emotional security above all.",
        "तपाईंको भावनात्मक स्वभाव गहिरो संवेदनशील र पालनपोषण गर्ने छ। तपाईंसँग बलियो अन्तर्ज्ञान छ र सबैभन्दा माथि भावनात्मक सुरक्षा चाहिन्छ।"),
    MOON_LEO("Your emotional nature is warm, generous, and dramatic. You need recognition and express feelings openly and passionately.",
        "तपाईंको भावनात्मक स्वभाव न्यानो, उदार र नाटकीय छ। तपाईंलाई मान्यता चाहिन्छ र भावनाहरू खुलेर र जोशपूर्ण रूपमा व्यक्त गर्नुहुन्छ।"),
    MOON_VIRGO("Your emotional nature is practical and analytical. You express care through service and may overthink feelings.",
        "तपाईंको भावनात्मक स्वभाव व्यावहारिक र विश्लेषणात्मक छ। तपाईं सेवा मार्फत हेरचाह व्यक्त गर्नुहुन्छ र भावनाहरूको बारेमा बढी सोच्न सक्नुहुन्छ।"),
    MOON_LIBRA("Your emotional nature seeks harmony and partnership. You need balanced relationships and avoid emotional extremes.",
        "तपाईंको भावनात्मक स्वभावले सामंजस्य र साझेदारी खोज्छ। तपाईंलाई सन्तुलित सम्बन्धहरू चाहिन्छ र भावनात्मक चरमहरूबाट बच्नुहुन्छ।"),
    MOON_SCORPIO("Your emotional nature is intense and transformative. You feel deeply and may hide vulnerabilities behind a strong exterior.",
        "तपाईंको भावनात्मक स्वभाव तीव्र र परिवर्तनकारी छ। तपाईं गहिरो रूपमा महसुस गर्नुहुन्छ र बलियो बाहिरी आवरण पछाडि कमजोरीहरू लुकाउन सक्नुहुन्छ।"),
    MOON_SAGITTARIUS("Your emotional nature is optimistic and freedom-loving. You need space to explore and grow emotionally.",
        "तपाईंको भावनात्मक स्वभाव आशावादी र स्वतन्त्रता-प्रेमी छ। तपाईंलाई भावनात्मक रूपमा अन्वेषण र बढ्न ठाउँ चाहिन्छ।"),
    MOON_CAPRICORN("Your emotional nature is controlled and responsible. You may suppress feelings for duty but are deeply loyal.",
        "तपाईंको भावनात्मक स्वभाव नियन्त्रित र जिम्मेवार छ। तपाईंले कर्तव्यको लागि भावनाहरू दबाउन सक्नुहुन्छ तर गहिरो वफादार हुनुहुन्छ।"),
    MOON_AQUARIUS("Your emotional nature is detached and humanitarian. You process feelings intellectually and value emotional freedom.",
        "तपाईंको भावनात्मक स्वभाव अलग र मानवतावादी छ। तपाईं भावनाहरूलाई बौद्धिक रूपमा प्रशोधन गर्नुहुन्छ र भावनात्मक स्वतन्त्रतालाई मूल्य दिनुहुन्छ।"),
    MOON_PISCES("Your emotional nature is deeply empathetic and intuitive. You absorb others' feelings and need spiritual outlets.",
        "तपाईंको भावनात्मक स्वभाव गहिरो सहानुभूतिपूर्ण र अन्तर्ज्ञानी छ। तपाईं अरूको भावनाहरू सोस्नुहुन्छ र आध्यात्मिक आउटलेटहरू चाहिन्छ।"),

    // Nakshatra personality influences
    NAKSHATRA_INFLUENCE_STRONG("The nakshatra placement strongly influences your personality, adding unique characteristics to your basic nature.",
        "नक्षत्र स्थितिले तपाईंको व्यक्तित्वलाई बलियो रूपमा प्रभाव पार्छ, तपाईंको आधारभूत स्वभावमा अद्वितीय विशेषताहरू थप्दै।"),
    NAKSHATRA_BIRTH_STAR("Your birth star reveals your inner essence, karmic path, and subtle personality traits that emerge over time.",
        "तपाईंको जन्म तारा तपाईंको आन्तरिक सार, कार्मिक मार्ग, र सूक्ष्म व्यक्तित्व विशेषताहरू प्रकट गर्दछ जुन समयसँगै देखिन्छ।"),

    // ═══════════════════════════════════════════════════════════════════════════
    // SECTION: CAREER & PROFESSION
    // ═══════════════════════════════════════════════════════════════════════════

    CAREER_OVERVIEW_TEMPLATE("Based on your 10th house lord %s in %s, and the position of Sun and Saturn, your career path shows strong potential in %s.",
        "तपाईंको १०औं भावको स्वामी %s %s मा, र सूर्य र शनिको स्थितिको आधारमा, तपाईंको पेशागत मार्गले %s मा बलियो क्षमता देखाउँछ।"),

    CAREER_SUN_STRONG("Strong Sun indicates success in government, administration, politics, or leadership positions. You naturally command respect.",
        "बलियो सूर्यले सरकार, प्रशासन, राजनीति, वा नेतृत्व पदहरूमा सफलता संकेत गर्छ। तपाईंले स्वाभाविक रूपमा सम्मान आदेश गर्नुहुन्छ।"),
    CAREER_MOON_STRONG("Strong Moon favors careers in hospitality, nursing, public relations, marine activities, or creative arts.",
        "बलियो चन्द्रमाले होटल व्यवसाय, नर्सिङ, जनसम्पर्क, समुद्री गतिविधिहरू, वा रचनात्मक कलाहरूमा क्यारियरलाई अनुकूल बनाउँछ।"),
    CAREER_MARS_STRONG("Strong Mars excels in military, police, sports, surgery, engineering, or entrepreneurship requiring courage and action.",
        "बलियो मंगलले सेना, प्रहरी, खेलकुद, शल्यचिकित्सा, इन्जिनियरिङ, वा साहस र कार्य आवश्यक पर्ने उद्यमशीलतामा उत्कृष्ट प्रदर्शन गर्छ।"),
    CAREER_MERCURY_STRONG("Strong Mercury indicates success in communication, writing, commerce, accounting, technology, or intellectual pursuits.",
        "बलियो बुधले सञ्चार, लेखन, वाणिज्य, लेखा, प्रविधि, वा बौद्धिक कार्यहरूमा सफलता संकेत गर्छ।"),
    CAREER_JUPITER_STRONG("Strong Jupiter favors teaching, law, spirituality, finance, advisory roles, or positions of wisdom and guidance.",
        "बलियो बृहस्पतिले शिक्षण, कानून, आध्यात्मिकता, वित्त, सल्लाहकार भूमिकाहरू, वा ज्ञान र मार्गदर्शनको पदहरूलाई अनुकूल बनाउँछ।"),
    CAREER_VENUS_STRONG("Strong Venus excels in arts, entertainment, luxury goods, beauty industry, hospitality, or diplomatic services.",
        "बलियो शुक्रले कला, मनोरञ्जन, विलासी सामान, सौन्दर्य उद्योग, आतिथ्य, वा कूटनीतिक सेवाहरूमा उत्कृष्ट प्रदर्शन गर्छ।"),
    CAREER_SATURN_STRONG("Strong Saturn indicates success through hard work in construction, agriculture, mining, research, or administrative roles.",
        "बलियो शनिले निर्माण, कृषि, खनन, अनुसन्धान, वा प्रशासनिक भूमिकाहरूमा कडा परिश्रमबाट सफलता संकेत गर्छ।"),
    CAREER_RAHU_INFLUENCE("Rahu influence suggests unconventional careers, foreign connections, technology, or media-related professions.",
        "राहुको प्रभावले अपरम्परागत क्यारियर, विदेशी सम्बन्धहरू, प्रविधि, वा मिडिया-सम्बन्धित पेशाहरू सुझाव गर्छ।"),
    CAREER_KETU_INFLUENCE("Ketu influence indicates spiritual pursuits, research, investigation, or work requiring intuition and detachment.",
        "केतुको प्रभावले आध्यात्मिक खोज, अनुसन्धान, अनुसन्धान, वा अन्तर्ज्ञान र विरक्ति आवश्यक पर्ने कार्य संकेत गर्छ।"),

    CAREER_10TH_LORD_STATUS("Your 10th house lord is %s in the %s house, which %s for career growth.",
        "तपाईंको १०औं भावको स्वामी %s %s भावमा छ, जुन क्यारियर वृद्धिको लागि %s।"),
    CAREER_10TH_STATUS_EXCELLENT("is excellently placed", "उत्कृष्ट रूपमा राखिएको छ"),
    CAREER_10TH_STATUS_GOOD("is well placed", "राम्रो राखिएको छ"),
    CAREER_10TH_STATUS_CHALLENGING("faces some challenges", "केही चुनौतीहरूको सामना गर्छ"),

    // ═══════════════════════════════════════════════════════════════════════════
    // SECTION: MARRIAGE & RELATIONSHIPS
    // ═══════════════════════════════════════════════════════════════════════════

    MARRIAGE_OVERVIEW("Your marriage and relationship prospects are analyzed through the 7th house, Venus, Jupiter, and their aspects.",
        "तपाईंको विवाह र सम्बन्ध सम्भावनाहरू ७औं भाव, शुक्र, बृहस्पति, र तिनीहरूको दृष्टि मार्फत विश्लेषण गरिएको छ।"),

    MARRIAGE_7TH_BENEFIC("The presence of benefics in your 7th house indicates a harmonious and supportive marriage partnership.",
        "तपाईंको ७औं भावमा शुभ ग्रहहरूको उपस्थितिले सामंजस्यपूर्ण र सहयोगी विवाह साझेदारी संकेत गर्छ।"),
    MARRIAGE_7TH_MALEFIC("Malefic influences on the 7th house suggest the need for patience and understanding in relationships.",
        "७औं भावमा पापी प्रभावहरूले सम्बन्धहरूमा धैर्य र समझको आवश्यकता सुझाव गर्छ।"),
    MARRIAGE_VENUS_STRONG("Strong Venus blesses you with charm, romantic nature, and the ability to maintain harmonious relationships.",
        "बलियो शुक्रले तपाईंलाई आकर्षण, रोमान्टिक स्वभाव, र सामंजस्यपूर्ण सम्बन्धहरू कायम राख्ने क्षमताको आशीर्वाद दिन्छ।"),
    MARRIAGE_VENUS_WEAK("Venus affliction may cause delays or challenges in relationships, requiring conscious effort to maintain harmony.",
        "शुक्र पीडाले सम्बन्धहरूमा ढिलाइ वा चुनौतीहरू निम्त्याउन सक्छ, सामंजस्य कायम राख्न सचेत प्रयास आवश्यक पर्दछ।"),
    MARRIAGE_JUPITER_ASPECT("Jupiter's aspect on the 7th house brings blessings, wisdom, and spiritual growth through marriage.",
        "७औं भावमा बृहस्पतिको दृष्टिले विवाह मार्फत आशीर्वाद, ज्ञान र आध्यात्मिक वृद्धि ल्याउँछ।"),

    MARRIAGE_TIMING_EARLY("Your chart indicates the possibility of early marriage, typically before age 25-27.",
        "तपाईंको कुण्डलीले प्रारम्भिक विवाहको सम्भावना संकेत गर्छ, सामान्यतया २५-२७ वर्ष अघि।"),
    MARRIAGE_TIMING_NORMAL("Your chart indicates marriage timing around the typical age of 27-32.",
        "तपाईंको कुण्डलीले सामान्य उमेर २७-३२ को आसपास विवाह समय संकेत गर्छ।"),
    MARRIAGE_TIMING_DELAYED("Your chart suggests delayed marriage, possibly after age 30-32, but with greater maturity.",
        "तपाईंको कुण्डलीले ढिलो विवाह सुझाव गर्छ, सम्भवतः ३०-३२ वर्षपछि, तर बढी परिपक्वताको साथ।"),

    SPOUSE_NATURE_TEMPLATE("Your spouse is likely to be %s, with qualities influenced by %s in your 7th house.",
        "तपाईंको जीवनसाथी %s हुने सम्भावना छ, तपाईंको ७औं भावमा %s द्वारा प्रभावित गुणहरूको साथ।"),

    // ═══════════════════════════════════════════════════════════════════════════
    // SECTION: HEALTH & LONGEVITY
    // ═══════════════════════════════════════════════════════════════════════════

    HEALTH_OVERVIEW("Health analysis is based on your Ascendant, 6th house (diseases), 8th house (longevity), and planetary afflictions.",
        "स्वास्थ्य विश्लेषण तपाईंको लग्न, ६औं भाव (रोग), ८औं भाव (आयु), र ग्रह पीडाहरूमा आधारित छ।"),

    HEALTH_CONSTITUTION_STRONG("You possess a strong constitution with good vitality and natural resistance to illness.",
        "तपाईंसँग राम्रो जीवनी शक्ति र रोगको लागि प्राकृतिक प्रतिरोधको साथ बलियो संरचना छ।"),
    HEALTH_CONSTITUTION_MODERATE("Your constitution is moderate, requiring regular attention to diet and lifestyle for optimal health.",
        "तपाईंको संरचना मध्यम छ, इष्टतम स्वास्थ्यको लागि आहार र जीवनशैलीमा नियमित ध्यान आवश्यक छ।"),
    HEALTH_CONSTITUTION_SENSITIVE("Your constitution is sensitive, benefiting from preventive care and balanced routines.",
        "तपाईंको संरचना संवेदनशील छ, निवारक हेरचाह र सन्तुलित दिनचर्याबाट लाभ हुन्छ।"),

    HEALTH_ARIES_AREAS("Areas requiring attention: Head, brain, face, eyes. Tendency toward headaches, fevers, and inflammations.",
        "ध्यान आवश्यक क्षेत्रहरू: टाउको, मस्तिष्क, अनुहार, आँखा। टाउको दुखाइ, ज्वरो, र सूजनको प्रवृत्ति।"),
    HEALTH_TAURUS_AREAS("Areas requiring attention: Throat, neck, thyroid, tonsils. Tendency toward throat ailments and metabolic issues.",
        "ध्यान आवश्यक क्षेत्रहरू: घाँटी, गर्दन, थाइरोइड, टन्सिल। घाँटी रोग र चयापचय समस्याहरूको प्रवृत्ति।"),
    HEALTH_GEMINI_AREAS("Areas requiring attention: Lungs, arms, hands, nervous system. Tendency toward respiratory and nervous disorders.",
        "ध्यान आवश्यक क्षेत्रहरू: फोक्सो, हात, हातहरू, स्नायु प्रणाली। श्वासप्रश्वास र स्नायु विकारहरूको प्रवृत्ति।"),
    HEALTH_CANCER_AREAS("Areas requiring attention: Chest, stomach, breasts, digestion. Tendency toward digestive and emotional health issues.",
        "ध्यान आवश्यक क्षेत्रहरू: छाती, पेट, स्तन, पाचन। पाचन र भावनात्मक स्वास्थ्य समस्याहरूको प्रवृत्ति।"),
    HEALTH_LEO_AREAS("Areas requiring attention: Heart, spine, back. Tendency toward cardiac and spinal issues.",
        "ध्यान आवश्यक क्षेत्रहरू: मुटु, मेरुदण्ड, पिठ्यूँ। हृदय र मेरुदण्ड समस्याहरूको प्रवृत्ति।"),
    HEALTH_VIRGO_AREAS("Areas requiring attention: Intestines, digestive system, nervous system. Tendency toward digestive and anxiety issues.",
        "ध्यान आवश्यक क्षेत्रहरू: आन्द्रा, पाचन प्रणाली, स्नायु प्रणाली। पाचन र चिन्ता समस्याहरूको प्रवृत्ति।"),
    HEALTH_LIBRA_AREAS("Areas requiring attention: Kidneys, lower back, skin. Tendency toward kidney and skin conditions.",
        "ध्यान आवश्यक क्षेत्रहरू: मिर्गौला, तल्लो ढाड, छाला। मिर्गौला र छाला अवस्थाहरूको प्रवृत्ति।"),
    HEALTH_SCORPIO_AREAS("Areas requiring attention: Reproductive organs, bladder, elimination. Tendency toward urogenital issues.",
        "ध्यान आवश्यक क्षेत्रहरू: प्रजनन अंगहरू, मूत्राशय, उत्सर्जन। मूत्रजननांगी समस्याहरूको प्रवृत्ति।"),
    HEALTH_SAGITTARIUS_AREAS("Areas requiring attention: Hips, thighs, liver, sciatic nerve. Tendency toward liver and hip issues.",
        "ध्यान आवश्यक क्षेत्रहरू: कमर, जाँघ, कलेजो, साइटिक नसा। कलेजो र कमर समस्याहरूको प्रवृत्ति।"),
    HEALTH_CAPRICORN_AREAS("Areas requiring attention: Knees, bones, joints, skin. Tendency toward joint and bone conditions.",
        "ध्यान आवश्यक क्षेत्रहरू: घुँडा, हड्डी, जोर्नी, छाला। जोर्नी र हड्डी अवस्थाहरूको प्रवृत्ति।"),
    HEALTH_AQUARIUS_AREAS("Areas requiring attention: Ankles, circulation, calves. Tendency toward circulatory and nervous issues.",
        "ध्यान आवश्यक क्षेत्रहरू: गोली, रक्त संचार, पिण्डली। रक्त संचार र स्नायु समस्याहरूको प्रवृत्ति।"),
    HEALTH_PISCES_AREAS("Areas requiring attention: Feet, lymphatic system, immune system. Tendency toward foot problems and immune issues.",
        "ध्यान आवश्यक क्षेत्रहरू: खुट्टा, लिम्फेटिक प्रणाली, प्रतिरक्षा प्रणाली। खुट्टा समस्या र प्रतिरक्षा समस्याहरूको प्रवृत्ति।"),

    LONGEVITY_LONG("Your chart indicates potential for long life with proper care and balanced lifestyle.",
        "तपाईंको कुण्डलीले उचित हेरचाह र सन्तुलित जीवनशैलीको साथ दीर्घ जीवनको सम्भावना संकेत गर्छ।"),
    LONGEVITY_MEDIUM("Your chart indicates average longevity, enhanced through healthy habits and positive thinking.",
        "तपाईंको कुण्डलीले औसत आयु संकेत गर्छ, स्वस्थ बानीहरू र सकारात्मक सोचाइबाट बढाइएको।"),
    LONGEVITY_REQUIRES_CARE("Your chart suggests the need for extra health consciousness and preventive measures.",
        "तपाईंको कुण्डलीले अतिरिक्त स्वास्थ्य चेतना र निवारक उपायहरूको आवश्यकता सुझाव गर्छ।"),

    // ═══════════════════════════════════════════════════════════════════════════
    // SECTION: WEALTH & FINANCE
    // ═══════════════════════════════════════════════════════════════════════════

    WEALTH_OVERVIEW("Financial prospects are analyzed through the 2nd house (savings), 11th house (gains), and their lords' positions.",
        "वित्तीय सम्भावनाहरू २औं भाव (बचत), ११औं भाव (लाभ), र तिनीहरूका स्वामीहरूको स्थिति मार्फत विश्लेषण गरिएको छ।"),

    WEALTH_DHANA_YOGA("Dhana Yogas in your chart indicate wealth accumulation potential. Financial success through proper effort is indicated.",
        "तपाईंको कुण्डलीमा धन योगहरूले धन संचय क्षमता संकेत गर्दछ। उचित प्रयासबाट आर्थिक सफलता संकेत गरिएको छ।"),
    WEALTH_JUPITER_STRONG("Strong Jupiter blesses you with wisdom in financial matters and opportunities for wealth growth.",
        "बलियो बृहस्पतिले तपाईंलाई वित्तीय मामिलाहरूमा ज्ञान र धन वृद्धिको लागि अवसरहरूको आशीर्वाद दिन्छ।"),
    WEALTH_2ND_LORD_STRONG("Your 2nd house lord is well-placed, indicating steady income and good savings potential.",
        "तपाईंको २औं भावको स्वामी राम्रोसँग राखिएको छ, स्थिर आय र राम्रो बचत क्षमता संकेत गर्दै।"),
    WEALTH_11TH_LORD_STRONG("Your 11th house lord is favorable, indicating gains through various sources and fulfillment of desires.",
        "तपाईंको ११औं भावको स्वामी अनुकूल छ, विभिन्न स्रोतहरूबाट लाभ र इच्छाहरूको पूर्ति संकेत गर्दै।"),

    WEALTH_SOURCE_TEMPLATE("Primary wealth sources indicated: %s. Secondary sources: %s.",
        "संकेतित प्राथमिक धन स्रोतहरू: %s। माध्यमिक स्रोतहरू: %s।"),

    WEALTH_CAUTION_RAHU("Rahu's influence suggests caution with speculative investments and get-rich-quick schemes.",
        "राहुको प्रभावले सट्टेबाजी लगानी र छिटो धनी बन्ने योजनाहरूमा सावधानी सुझाव गर्छ।"),
    WEALTH_CAUTION_SATURN("Saturn's influence indicates wealth through steady effort rather than quick gains.",
        "शनिको प्रभावले छिटो लाभभन्दा स्थिर प्रयास मार्फत धन संकेत गर्छ।"),

    // ═══════════════════════════════════════════════════════════════════════════
    // SECTION: EDUCATION & KNOWLEDGE
    // ═══════════════════════════════════════════════════════════════════════════

    EDUCATION_OVERVIEW("Educational prospects are analyzed through the 4th house (basic education), 5th house (higher learning), and Mercury's placement.",
        "शैक्षिक सम्भावनाहरू ४औं भाव (आधारभूत शिक्षा), ५औं भाव (उच्च शिक्षा), र बुधको स्थान मार्फत विश्लेषण गरिएको छ।"),

    EDUCATION_MERCURY_STRONG("Strong Mercury blesses you with excellent learning abilities, communication skills, and analytical thinking.",
        "बलियो बुधले तपाईंलाई उत्कृष्ट सिक्ने क्षमता, सञ्चार कौशल, र विश्लेषणात्मक सोचको आशीर्वाद दिन्छ।"),
    EDUCATION_JUPITER_ASPECT("Jupiter's influence on education houses indicates success in higher studies and wisdom acquisition.",
        "शिक्षा भावहरूमा बृहस्पतिको प्रभावले उच्च अध्ययन र ज्ञान प्राप्तिमा सफलता संकेत गर्छ।"),
    EDUCATION_5TH_STRONG("Strong 5th house indicates academic excellence, creative intelligence, and success in competitive exams.",
        "बलियो ५औं भावले शैक्षिक उत्कृष्टता, रचनात्मक बुद्धिमत्ता, र प्रतिस्पर्धात्मक परीक्षाहरूमा सफलता संकेत गर्छ।"),

    EDUCATION_FIELDS_TEMPLATE("Favorable fields of study: %s. These align with your natural intellectual strengths.",
        "अनुकूल अध्ययन क्षेत्रहरू: %s। यी तपाईंको प्राकृतिक बौद्धिक शक्तिहरूसँग मिल्दछन्।"),

    // ═══════════════════════════════════════════════════════════════════════════
    // SECTION: SPIRITUAL PATH
    // ═══════════════════════════════════════════════════════════════════════════

    SPIRITUAL_OVERVIEW("Your spiritual inclinations are analyzed through the 9th house (dharma), 12th house (moksha), and Ketu's placement.",
        "तपाईंको आध्यात्मिक झुकावहरू ९औं भाव (धर्म), १२औं भाव (मोक्ष), र केतुको स्थान मार्फत विश्लेषण गरिएको छ।"),

    SPIRITUAL_JUPITER_STRONG("Strong Jupiter indicates a natural inclination toward spirituality, philosophy, and righteous living.",
        "बलियो बृहस्पतिले आध्यात्मिकता, दर्शन, र धार्मिक जीवनतर्फ प्राकृतिक झुकाव संकेत गर्छ।"),
    SPIRITUAL_KETU_INFLUENCE("Ketu's placement shows your past life spiritual attainments and current path toward liberation.",
        "केतुको स्थानले तपाईंको विगत जीवनको आध्यात्मिक उपलब्धिहरू र मुक्तितर्फको वर्तमान मार्ग देखाउँछ।"),
    SPIRITUAL_12TH_ACTIVE("Active 12th house indicates potential for spiritual practices, meditation, and inner growth.",
        "सक्रिय १२औं भावले आध्यात्मिक अभ्यासहरू, ध्यान, र आन्तरिक वृद्धिको सम्भावना संकेत गर्छ।"),
    SPIRITUAL_9TH_STRONG("Strong 9th house blesses you with faith, higher wisdom, and guidance from teachers.",
        "बलियो ९औं भावले तपाईंलाई विश्वास, उच्च ज्ञान, र गुरुहरूबाट मार्गदर्शनको आशीर्वाद दिन्छ।"),

    SPIRITUAL_PATH_TEMPLATE("Your spiritual path aligns with %s. Practices like %s would be particularly beneficial.",
        "तपाईंको आध्यात्मिक मार्ग %s सँग मिल्दछ। %s जस्ता अभ्यासहरू विशेष रूपमा लाभदायक हुनेछन्।"),

    // ═══════════════════════════════════════════════════════════════════════════
    // SECTION: GENERAL PREDICTIONS & TEMPLATES
    // ═══════════════════════════════════════════════════════════════════════════

    PREDICTION_TEMPLATE_POSITIVE("The planetary configuration indicates %s, bringing %s in the coming period.",
        "ग्रह विन्यासले %s संकेत गर्छ, आउने अवधिमा %s ल्याउँदै।"),
    PREDICTION_TEMPLATE_CAUTION("Planetary influences suggest caution regarding %s. Remedial measures can help mitigate challenges.",
        "ग्रह प्रभावहरूले %s सम्बन्धमा सावधानी सुझाव गर्दछ। उपचारात्मक उपायहरूले चुनौतीहरू कम गर्न मद्दत गर्न सक्छ।"),

    DASHA_EFFECT_TEMPLATE("Currently running %s Mahadasha with %s Antardasha. This period emphasizes %s.",
        "हाल %s महादशा %s अन्तर्दशाको साथ चलिरहेको छ। यो अवधिले %s मा जोड दिन्छ।"),

    TRANSIT_EFFECT_TEMPLATE("%s is transiting your %s house, influencing %s matters.",
        "%s तपाईंको %s भावमा गोचर गर्दैछ, %s मामिलाहरूलाई प्रभावित गर्दै।"),

    // ═══════════════════════════════════════════════════════════════════════════
    // SECTION: STRENGTHS & WEAKNESSES LABELS
    // ═══════════════════════════════════════════════════════════════════════════

    TRAIT_LEADERSHIP("Leadership ability", "नेतृत्व क्षमता"),
    TRAIT_COMMUNICATION("Communication skills", "सञ्चार कौशल"),
    TRAIT_CREATIVITY("Creative expression", "रचनात्मक अभिव्यक्ति"),
    TRAIT_ANALYTICAL("Analytical thinking", "विश्लेषणात्मक सोच"),
    TRAIT_INTUITION("Strong intuition", "बलियो अन्तर्ज्ञान"),
    TRAIT_DETERMINATION("Determination", "दृढता"),
    TRAIT_COMPASSION("Compassion", "करुणा"),
    TRAIT_PRACTICALITY("Practical approach", "व्यावहारिक दृष्टिकोण"),
    TRAIT_ADAPTABILITY("Adaptability", "अनुकूलनशीलता"),
    TRAIT_PATIENCE("Patience", "धैर्य"),
    TRAIT_DIPLOMACY("Diplomatic skills", "कूटनीतिक कौशल"),
    TRAIT_INDEPENDENCE("Independence", "स्वतन्त्रता"),

    CHALLENGE_IMPULSIVENESS("Impulsiveness", "आवेग"),
    CHALLENGE_STUBBORNNESS("Stubbornness", "हठ"),
    CHALLENGE_INDECISION("Indecisiveness", "अनिर्णय"),
    CHALLENGE_OVERSENSITIVITY("Over-sensitivity", "अति-संवेदनशीलता"),
    CHALLENGE_PRIDE("Excessive pride", "अत्यधिक गर्व"),
    CHALLENGE_CRITICISM("Over-critical nature", "अति-आलोचनात्मक स्वभाव"),
    CHALLENGE_DETACHMENT("Emotional detachment", "भावनात्मक अलगाव"),
    CHALLENGE_INTENSITY("Emotional intensity", "भावनात्मक तीव्रता"),
    CHALLENGE_RESTLESSNESS("Restlessness", "बेचैनी"),
    CHALLENGE_RIGIDITY("Rigidity", "कठोरता"),
    CHALLENGE_ESCAPISM("Tendency to escapism", "पलायनवादको प्रवृत्ति"),
    CHALLENGE_ANXIETY("Anxiety tendencies", "चिन्ता प्रवृत्तिहरू"),

    // ═══════════════════════════════════════════════════════════════════════════
    // SECTION: REMEDIES
    // ═══════════════════════════════════════════════════════════════════════════

    REMEDY_GEMSTONE_TEMPLATE("Wearing %s gemstone can strengthen %s and bring positive results.",
        "%s रत्न लगाउँदा %s लाई बलियो बनाउन र सकारात्मक परिणामहरू ल्याउन सक्छ।"),
    REMEDY_MANTRA_TEMPLATE("Chanting %s mantra regularly can appease %s and bring blessings.",
        "%s मन्त्र नियमित जप गर्दा %s लाई शान्त पार्न र आशीर्वाद ल्याउन सक्छ।"),
    REMEDY_CHARITY_TEMPLATE("Donating %s on %s can help mitigate challenging planetary influences.",
        "%s मा %s दान गर्दा चुनौतीपूर्ण ग्रह प्रभावहरू कम गर्न मद्दत गर्न सक्छ।"),
    REMEDY_FASTING_TEMPLATE("Fasting on %s can strengthen beneficial planetary energies.",
        "%s मा उपवास गर्दा लाभदायक ग्रह ऊर्जाहरू बलियो बनाउन सक्छ।"),
    REMEDY_WORSHIP_TEMPLATE("Worshipping %s deity can bring blessings for %s.",
        "%s देवताको पूजा गर्दा %s को लागि आशीर्वाद ल्याउन सक्छ।"),

    // Report generation labels
    REPORT_GENERATED("Report generated", "प्रतिवेदन उत्पन्न"),
    REPORT_BASED_ON("Based on Vedic astrology principles", "वैदिक ज्योतिष सिद्धान्तहरूमा आधारित"),
    REPORT_DISCLAIMER("This analysis is for guidance purposes. Individual effort and free will play important roles in shaping destiny.",
        "यो विश्लेषण मार्गदर्शन उद्देश्यका लागि हो। व्यक्तिगत प्रयास र स्वतन्त्र इच्छाले भाग्य आकार दिनमा महत्त्वपूर्ण भूमिका खेल्छ।");

    companion object {
        fun getByKey(key: String): StringKeyNative? = entries.find { it.name == key }
    }
}
