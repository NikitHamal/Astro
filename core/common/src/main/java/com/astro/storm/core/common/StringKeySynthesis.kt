package com.astro.storm.core.common

/**
 * Localization keys for Triple Pillar Synthesis and Success Probability
 */
enum class StringKeySynthesis(override val en: String, override val ne: String) : StringKeyInterface {
    SECTION_SYNTHESIS("Triple-Pillar Synthesis", "त्रि-स्तम्भ संश्लेषण"),
    SUCCESS_PROBABILITY_TITLE("Success Probability Timeline", "सफलता सम्भाव्यता समयरेखा"),

    PILLAR_DASHA_PROMISE("Dasha Promise", "दशाको वाचा"),
    PILLAR_GOCHARA_OCCASION("Gochara Occasion", "गोचरको अवसर"),
    PILLAR_ASHTAKAVARGA_STRENGTH("Ashtakavarga Strength", "अष्टकवर्गको बल"),

    LABEL_SYNTHESIS_SCORE("Synthesis Score", "संश्लेषण स्कोर"),
    LABEL_PEAK_PROBABILITY("Peak Probability", "उच्चतम सम्भाव्यता"),
    LABEL_CURRENT_PROBABILITY("Current Probability", "वर्तमान सम्भाव्यता"),

    SYNTHESIS_DESCRIPTION("A multi-dimensional timing analysis cross-referencing Dasha, Transits, and Ashtakavarga points.", "दशा, गोचर र अष्टकवर्ग बिन्दुहरूको क्रस-रेफरेन्सिङ बहु-आयामी समय विश्लेषण।"),

    INTERP_EXCELLENT("Excellent: All three pillars align for significant breakthroughs.", "उत्कृष्ट: तीनवटै स्तम्भहरू महत्त्वपूर्ण सफलताका लागि पङ्क्तिबद्ध छन्।"),
    INTERP_VERY_STRONG("Very Strong: High probability of favorable outcomes and growth.", "धेरै बलियो: अनुकूल परिणाम र वृद्धिको उच्च सम्भावना।"),
    INTERP_STRONG("Strong: Good alignment supporting positive progress.", "बलियो: सकारात्मक प्रगतिलाई समर्थन गर्ने राम्रो पङ्क्तिबद्धता।"),
    INTERP_MODERATE("Moderate: Mixed influences; efforts will yield average results.", "मध्यम: मिश्रित प्रभावहरू; प्रयासले औसत परिणाम दिनेछ।"),
    INTERP_CHALLENGING("Challenging: Pillars are not aligned; caution and persistence required.", "चुनौतीपूर्ण: स्तम्भहरू पङ्क्तिबद्ध छैनन्; सावधानी र दृढता आवश्यक छ।"),
    INTERP_DIFFICULT("Difficult: Significant obstructions; avoid major new initiatives.", "कठिन: महत्त्वपूर्ण अवरोधहरू; प्रमुख नयाँ पहलहरूबाट बच्नुहोस्।"),

    TIMELINE_FORECAST("180-Day Success Forecast", "१८०-दिने सफलताको पूर्वानुमान"),
    TIMELINE_PEAK_DATES("Potential Peak Success Dates", "सम्भावित उच्चतम सफलता मितिहरू"),

    PILLAR_DETAILS_MDL("Mahadasha Lord (%s)", "महादशा स्वामी (%s)"),
    PILLAR_DETAILS_ADL("Antardasha Lord (%s)", "अन्तर्दशा स्वामी (%s)"),

    STRENGTH_HIGH("High", "उच्च"),
    STRENGTH_MEDIUM("Medium", "मध्यम"),
    STRENGTH_LOW("Low", "कम"),

    VEDHA_OBSTRUCTION("Vedha Obstruction", "वेध अवरोध"),
    ASHTAKAVARGA_SUPPORT("Ashtakavarga Support", "अष्टकवर्ग समर्थन"),
}
