package com.astro.storm.core.common

/**
 * Narrative templates for deep native analysis.
 */
enum class StringKeyNativeNarrative(override val en: String, override val ne: String) : StringKeyInterface {
    CHAR_SUMMARY_TEMPLATE(
        "With %s ascendant and Moon in %s, your nature blends %s element with %s modality, shaping how you initiate, sustain, and refine your life path.",
        "%s लग्न र %s मा चन्द्रमा भएकोले, तपाईंको स्वभाव %s तत्व र %s प्रवृत्ति मिसिएको छ, जसले तपाईं कसरी सुरु, टिकाउने र परिष्कृत गर्ने भन्ने देखाउँछ।"
    ),
    CHAR_ELEMENT_INFLUENCE_TEMPLATE(
        "The dominant %s element highlights your core motivations and the way you recharge and express vitality.",
        "प्रमुख %s तत्वले तपाईंको मुख्य प्रेरणा र ऊर्जा कसरी पुनःस्थापित र व्यक्त हुन्छ भन्ने देखाउँछ।"
    ),
    CHAR_MODALITY_INFLUENCE_TEMPLATE(
        "Your %s modality indicates how you approach change: initiating, stabilizing, or adapting.",
        "तपाईंको %s प्रवृत्तिले परिवर्तनलाई कसरी सम्हाल्नुहुन्छ भन्ने देखाउँछ: सुरु गर्ने, स्थिर राख्ने वा अनुकूलन गर्ने।"
    ),
    CHAR_SUN_INFLUENCE_TEMPLATE(
        "Sun in %s refines your purpose, confidence, and the way you lead or inspire others.",
        "%s मा सूर्यले तपाईंको उद्देश्य, आत्मविश्वास र अरूलाई नेतृत्व वा प्रेरणा दिने तरिका परिष्कृत गर्छ।"
    ),
    CHAR_NAKSHATRA_INFLUENCE_TEMPLATE(
        "Your birth nakshatra %s adds subtle karmic tones that mature with experience and self-awareness.",
        "तपाईंको जन्म नक्षत्र %s ले अनुभव र आत्म-जागरुकतासँगै परिपक्व हुने सूक्ष्म कर्मिक प्रभाव थप्छ।"
    ),
    CHAR_STRENGTH_TEMPLATE(
        "Personality strength is %s, suggesting how consistently you can apply your gifts in daily life.",
        "व्यक्तित्व बल %s छ, जसले तपाईंका गुणहरूलाई दैनिक जीवनमा कति निरन्तर प्रयोग गर्न सक्नुहुन्छ भन्ने देखाउँछ।"
    ),
    CAREER_STRENGTH_TEMPLATE(
        "Career strength is %s, indicating the ease with which recognition and growth can manifest.",
        "क्यारियर बल %s छ, जसले मान्यता र वृद्धि कति सहज रूपमा आउँछ भन्ने देखाउँछ।"
    ),
    MARRIAGE_SUMMARY_TEMPLATE(
        "Relationship outcomes depend on the 7th lord %s in house %d with %s dignity and Venus strength %s.",
        "सम्बन्ध परिणाम ७औं स्वामी %s (भाव %d) को %s सम्मान र शुक्र बल %s मा निर्भर हुन्छ।"
    ),
    MARRIAGE_STRENGTH_TEMPLATE(
        "Relationship strength is %s, highlighting how harmony grows through conscious effort.",
        "सम्बन्ध बल %s छ, जसले सचेत प्रयासबाट सामंजस्य कसरी बढ्छ भन्ने देखाउँछ।"
    ),
    HEALTH_SUMMARY_TEMPLATE(
        "Vitality depends on ascendant strength %s, with disease indicators seen through the 6th lord %s and longevity via the 8th lord %s.",
        "जीवन शक्ति लग्न बल %s मा निर्भर हुन्छ; रोग संकेत ६औं स्वामी %s र आयु ८औं स्वामी %s बाट देखिन्छ।"
    ),
    HEALTH_CONCERN_TEMPLATE(
        "Be mindful of %s, especially when routines are inconsistent or stress rises.",
        "%s प्रति सचेत रहनुहोस्, विशेष गरी जब दिनचर्या अनियमित हुन्छ वा तनाव बढ्छ।"
    ),
    WEALTH_SUMMARY_TEMPLATE(
        "Wealth shows through the 2nd lord %s (%s) and 11th lord %s (%s), supported by Jupiter strength %s.",
        "धन २औं स्वामी %s (%s) र ११औं स्वामी %s (%s) बाट देखिन्छ, बृहस्पति बल %s ले सहयोग गर्छ।"
    ),
    WEALTH_POTENTIAL_TEMPLATE(
        "Wealth potential is %s, favoring disciplined accumulation over impulsive speculation.",
        "धन क्षमता %s छ, जसले हतारो सट्टा अनुशासित संचयलाई समर्थन गर्दछ।"
    ),
    EDUCATION_SUMMARY_TEMPLATE(
        "Learning thrives through the 4th lord %s, 5th lord %s, and Mercury strength %s.",
        "सीखाइ ४औं स्वामी %s, ५औं स्वामी %s र बुध बल %s मार्फत फल्छ।"
    ),
    SPIRITUAL_SUMMARY_TEMPLATE(
        "Spiritual growth is guided by the 9th lord %s, 12th lord %s, and Ketu's placement.",
        "आध्यात्मिक विकास ९औं स्वामी %s, १२औं स्वामी %s र केतुको स्थितिले मार्गदर्शन गर्छ।"
    )
}
