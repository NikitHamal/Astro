package com.astro.storm.core.common

/**
 * Deep Analysis Interpretation Templates
 */
enum class StringKeyDeepAnalysisInterp(override val en: String, override val ne: String, override val hi: String) : StringKeyInterface {
    // Synthesis Templates
    LIFE_PURPOSE_TEMPLATE(
        "Your life purpose centers around %1\$s's energy, working through %2\$s rising nature. Your soul seeks %3\$s, with dharmic expression through %4\$s. Career fulfillment comes through %5\$s.",
        "तपाईंको जीवनको उद्देश्य %1\$sको ऊर्जामा केन्द्रित छ, %2\$s उदय स्वभाव मार्फत काम गर्दै। तपाईंको आत्माले %3\$s खोज्छ, %4\$s मार्फत धार्मिक अभिव्यक्तिको साथ। %5\$s मार्फत क्यारियर पूर्ति आउँछ।"
    ),
    LIFE_PATH_SUMMARY_TEMPLATE(
        "Your life path reveals a journey of %1\$s, combined with %2\$s. Relationships show %3\$s, while health indicators are %4\$s. Financial prospects are %5\$s, with %6\$s. Your spiritual path shows %7\$s toward higher awareness and inner growth.",
        "तपाईंको जीवन मार्गले %1\$sको यात्रा प्रकट गर्दछ, %2\$sको साथ संयुक्त। सम्बन्धहरूले %3\$s देखाउँछ, जबकि स्वास्थ्य सूचकहरू %4\$s छन्। आर्थिक सम्भावनाहरू %5\$s छन्, %6\$sको साथ। तपाईंको आध्यात्मिक मार्गले उच्च चेतना र आन्तरिक विकासतर्फ %7\$s देखाउँछ।"
    ),

    // Strength Descriptors (EN)
    STRENGTH_EXCEPTIONAL_EN("exceptional %s", "असाधारण %s", "असाधारण %s"),
    ,
    STRENGTH_STRONG_EN("strong %s", "बलियो %s", "बलियह %s"),
    ,
    STRENGTH_MODERATE_EN("moderate %s", "मध्यम %s", "मध्यम %s"),
    ,
    STRENGTH_DEVELOPING_EN("developing %s", "विकासशील %s", "विकासशील %s"),
    ,
    STRENGTH_POTENTIAL_EN("potential for %s", "%s को सम्भावना", "%s  के सम्भावना"),

    ,

    // Strength Descriptors (NE)
    STRENGTH_EXCEPTIONAL_NE("असाधारण %s", "असाधारण %s", "असाधारण %s"),
    ,
    STRENGTH_STRONG_NE("बलियो %s", "बलियो %s", "बलियह %s"),
    ,
    STRENGTH_MODERATE_NE("मध्यम %s", "मध्यम %s", "मध्यम %s"),
    ,
    STRENGTH_DEVELOPING_NE("विकासशील %s", "विकासशील %s", "विकासशील %s"),
    ,
    STRENGTH_POTENTIAL_NE("%s को सम्भावना", "%s को सम्भावना", "%s  के सम्भावना"),

    ,

    // Ascendant Interpretations
    ASC_EARLY_DEGREES(
        "Rising in the early degrees of %s, you express this sign's qualities with youthful energy and pure manifestation. The beginner's enthusiasm marks your approach to life.",
        "%sको प्रारम्भिक अंशहरूमा उदय हुँदा, तपाईंले यो राशिको गुणहरू युवा ऊर्जा र शुद्ध अभिव्यक्तिको साथ व्यक्त गर्नुहुन्छ।"
    ),
    ASC_MIDDLE_DEGREES(
        "Rising in the middle degrees of %s, you express this sign's qualities in their fullest, most stable form. Maturity and balance characterize your personality.",
        "%sको मध्य अंशहरूमा उदय हुँदा, तपाईंले यो राशिको गुणहरू तिनीहरूको पूर्ण, सबैभन्दा स्थिर रूपमा व्यक्त गर्नुहुन्छ।"
    ),
    ASC_LATE_DEGREES(
        "Rising in the late degrees of %s, you blend this sign's qualities with hints of the next sign. Wisdom and completion energy mark your approach.",
        "%sको अन्तिम अंशहरूमा उदय हुँदा, तपाईंले यो राशिको गुणहरू अर्को राशिको संकेतहरूसँग मिलाउनुहुन्छ।"
    ),

    // Overall Interpretation Template
    OVERALL_INTERP_TEMPLATE(
        "Your %1\$s Ascendant, ruled by %2\$s which is %3\$s, shapes your entire life experience. %4\$s This rising sign colors how others perceive you and how you instinctively respond to new situations.",
        "तपाईंको %1\$s लग्न, %2\$s द्वारा शासित जुन %3\$s छ, तपाईंको सम्पूर्ण जीवन अनुभव आकार दिन्छ। %4\$s यो उदय राशिले अरूले तपाईंलाई कसरी बुझ्छन् र तपाईं नयाँ परिस्थितिहरूमा कसरी प्रतिक्रिया दिनुहुन्छ भनेर रंग दिन्छ।"
    ),

    // Career Templates
    PLANET_IN_10TH_TEMPLATE("%1\$s in 10th house shapes your public role and career expression in a unique way.", "दशौं भावमा %1\$sले तपाईंको सार्वजनिक भूमिका र क्यारियर अभिव्यक्तिलाई अद्वितीय तरिकाले आकार दिन्छ।", "दशौं भाव में %1\$sले आप के सार्वजनिक भूमि के और क्यारियर अभिव्यक्ति  के अद्वितीय तरिकाले आकार देता है।"),
    ,
    PUBLIC_IMAGE_TEMPLATE("The public perceives you as a %1\$s. Your professional image is %2\$s, creating %3\$s.", "सार्वजनिकले तपाईंलाई एक %1\$s को रूपमा बुझ्छ। तपाईंको व्यावसायिक छवि %2\$s छ, जसले %3\$s सिर्जना गर्छ।", "सार्वजनिकले आप  के एक %1\$s  के रूप में बुझ्है। आप के व्यावसायिक हैवि %2\$s है, जसले %3\$s सिर्जना करता है।"),
    ,
    CAREER_ENVIRONMENT_TEMPLATE("Your ideal work environment reflects %s's qualities, supporting your natural professional style.", "तपाईंको आदर्श कार्य वातावरणले %sका गुणहरू प्रतिबिम्बित गर्छ।", "आप के आदर्श कार्य वातावरणले %s के गुण प्रतिबिम्बित करता है।"),
    ,
    TENTH_LORD_IN_HOUSE_TEMPLATE("10th lord %1\$s in %2\$s house shapes your career through that house's significations.", "दशौं स्वामी %1\$s %2\$sऔं भावमा त्यो भावको संकेतहरू मार्फत तपाईंको क्यारियर आकार दिन्छ।", "दशौं स्वामी %1\$s %2\$sऔं भाव में त्यह भाव के संकेत मार्फत आप के क्यारियर आकार देता है।"),

    ,

    // Career Success Indicators
    SUCCESS_STRONG_POTENTIAL("strong professional potential", "बलियो व्यावसायिक क्षमता", "बलियह व्यावसायिक क्षमता"),
    ,
    SUCCESS_DEVELOPING_PATH("developing career path", "विकासशील क्यारियर मार्ग", "विकासशील क्यारियर मार्ग"),
    ,
    OPPORTUNITIES_STRONG("strong opportunities", "बलियो अवसरहरू", "बलियह अवसर"),
    ,
    OPPORTUNITIES_GROWTH("room for growth", "वृद्धिको लागि ठाउँ", "वृद्धि के लिए ठाउँ"),

    ,

    // Loading & Errors
    ANALYZING_DEEPLY("Analyzing your chart deeply...", "तपाईंको कुण्डलीको गहन विश्लेषण गर्दै...", "आप के कुंडली के गहन विश्लेषण गर्दै..."),
,
}
