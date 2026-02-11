package com.astro.storm.core.common

/**
 * String keys for Ashtakavarga and Transit analysis
 */
enum class StringKeyAshtavarga(override val en: String, override val ne: String, override val hi: String) : StringKeyInterface {
    BAV_LABEL("BAV", "BAV", "BAV"),
    SAV_LABEL("SAV", "SAV", "SAV"),
    RANK_LABEL("Rank %d", "श्रेणी %d", "श्रेणी %d"),
    SCORE_LABEL("Score: %d", "अंक: %d", "अंक: %d"),
    EXIT_DATE_LABEL("Exit Date", "बाहिरिने मिति", "बाहिरिने मिति"),
    SIGNIFICANT_ONLY("Significant Only", "महत्त्वपूर्ण मात्र", "महत्त्वपूर्ण मात्र"),
    BEST_SIGN("Best Sign", "उत्कृष्ट राशि", "उत्कृष्ट राशि"),
    WEAK_SIGN("Weakest Sign", "कमजोर राशि", "कमजोर राशि"),
    TOTAL_BINDUS("%d Total Bindus", "%d कुल बिन्दुहरू", "%d कुल बिन्दुहरू"),
    TRANSIT_IN_SIGN("in %s", "%s मा", "%s मा"),
    PLANET_TRANSIT_DETAILS("%s Transit Details", "%s गोचर विवरण", "%s गोचर विवरण"),
    TRANSIT_CHART_TITLE("Ashtakavarga Transit Chart", "अष्टकवर्ग गोचर चार्ट", "अष्टकवर्ग गोचर चार्ट"),
    
    // Status
    SIGNIFICANT("Significant", "महत्त्वपूर्ण", "महत्त्वपूर्ण"),
    COLLAPSE("Collapse", "खुम्च्याउनुहोस्", "खुम्च्याउनुहोस्"),
    EXPAND("Expand", "फिजाउनुहोस्", "फिजाउनुहोस्"),
    
    // Header Info
    TRANSIT_SCORE_TITLE("Ashtakavarga Transit Score", "अष्टकवर्ग गोचर स्कोर", "अष्टकवर्ग गोचर स्कोर"),
    TRANSIT_SUMMARY_TITLE("Transit Summary", "गोचर सारांश", "गोचर सारांश"),
    AFFECTED_AREAS_TITLE("Affected Life Areas", "प्रभावित जीवन क्षेत्रहरू", "प्रभावित जीवन क्षेत्रहरू"),
    NEXT_SIGNIFICANT_TITLE("Next Significant Transit", "अर्को महत्त्वपूर्ण गोचर", "अर्को महत्त्वपूर्ण गोचर"),
}
