package com.astro.storm.core.common

/**
 * Localization keys for Triple-Pillar Predictive Engine
 * (Dasha + Gochara + Ashtakavarga Synthesis)
 *
 * Implements the classical "Three Pillars of Timing" from Vedic astrology:
 * 1. Vimshottari Dasha (planetary periods)
 * 2. Gochara (transits from Moon)
 * 3. Ashtakavarga (bindu-based strength)
 */
enum class StringKeyTriplePillar(override val en: String, override val ne: String) : StringKeyInterface {

    // ========== Screen Title & Headers ==========
    TITLE("Triple-Pillar Predictive Engine", "त्रि-स्तम्भ भविष्यवाणी इन्जिन"),
    SUBTITLE("Dasha · Gochara · Ashtakavarga Synthesis", "दशा · गोचर · अष्टकवर्ग संश्लेषण"),
    HEADER_SUCCESS_TIMELINE("Success Probability Timeline", "सफलता सम्भावना समयरेखा"),
    HEADER_CURRENT_SYNTHESIS("Current Synthesis Analysis", "हालको संश्लेषण विश्लेषण"),
    HEADER_PILLAR_BREAKDOWN("Three-Pillar Breakdown", "तीन-स्तम्भ विश्लेषण"),
    HEADER_MONTHLY_FORECAST("Monthly Forecast", "मासिक भविष्यवाणी"),
    HEADER_PEAK_WINDOWS("Peak Opportunity Windows", "उच्च अवसर सञ्झ्यालहरू"),
    HEADER_RISK_WINDOWS("Caution Periods", "सावधानी अवधिहरू"),
    HEADER_LIFE_AREA_IMPACT("Life Area Impact", "जीवन क्षेत्र प्रभाव"),
    HEADER_CLASSICAL_REFERENCE("Classical Foundation", "शास्त्रीय आधार"),

    // ========== Tab Names ==========
    TAB_OVERVIEW("Overview", "अवलोकन"),
    TAB_TIMELINE("Timeline", "समयरेखा"),
    TAB_PILLARS("Pillars", "स्तम्भहरू"),
    TAB_FORECAST("Forecast", "भविष्यवाणी"),

    // ========== Pillar Names ==========
    PILLAR_DASHA("Vimshottari Dasha", "विम्शोत्तरी दशा"),
    PILLAR_GOCHARA("Gochara (Transit)", "गोचर (ग्रह सञ्चार)"),
    PILLAR_ASHTAKAVARGA("Ashtakavarga", "अष्टकवर्ग"),

    // ========== Pillar Descriptions ==========
    PILLAR_DASHA_DESC("Planetary period lord's inherent nature and dignity", "ग्रह दशा स्वामीको स्वाभाविक प्रकृति र गरिमा"),
    PILLAR_GOCHARA_DESC("Transit position from natal Moon (Chandra Lagna)", "जन्म चन्द्रमाबाट गोचर स्थिति (चन्द्र लग्न)"),
    PILLAR_ASHTAKAVARGA_DESC("Bindu score strength in transiting sign", "गोचर राशिमा बिन्दु बल"),

    // ========== Synthesis Score Labels ==========
    SYNTHESIS_SCORE("Synthesis Score", "संश्लेषण अंक"),
    SUCCESS_PROBABILITY("Success Probability", "सफलता सम्भावना"),
    OVERALL_ASSESSMENT("Overall Assessment", "समग्र मूल्याङ्कन"),
    COMPOSITE_STRENGTH("Composite Strength", "समग्र बल"),
    DASHA_CONTRIBUTION("Dasha Contribution", "दशा योगदान"),
    GOCHARA_CONTRIBUTION("Gochara Contribution", "गोचर योगदान"),
    ASHTAKAVARGA_CONTRIBUTION("Ashtakavarga Contribution", "अष्टकवर्ग योगदान"),

    // ========== Score Qualitative Labels ==========
    QUALITY_EXCEPTIONAL("Exceptional", "असाधारण"),
    QUALITY_EXCELLENT("Excellent", "उत्कृष्ट"),
    QUALITY_VERY_GOOD("Very Good", "धेरै राम्रो"),
    QUALITY_GOOD("Good", "राम्रो"),
    QUALITY_ABOVE_AVERAGE("Above Average", "औसतभन्दा माथि"),
    QUALITY_AVERAGE("Average", "औसत"),
    QUALITY_BELOW_AVERAGE("Below Average", "औसतभन्दा तल"),
    QUALITY_CHALLENGING("Challenging", "चुनौतीपूर्ण"),
    QUALITY_DIFFICULT("Difficult", "कठिन"),

    // ========== Dasha Analysis ==========
    DASHA_LORD("Dasha Lord", "दशा स्वामी"),
    DASHA_LORD_NATURE("Dasha Lord Nature", "दशा स्वामी स्वभाव"),
    DASHA_DIGNITY("Dasha Lord Dignity", "दशा स्वामी गरिमा"),
    DASHA_FUNCTIONAL_NATURE("Functional Nature", "कार्यात्मक स्वभाव"),
    DASHA_HOUSE_LORDSHIP("House Lordship", "भाव स्वामित्व"),
    MAHADASHA_LORD("Mahadasha Lord", "महादशा स्वामी"),
    ANTARDASHA_LORD("Antardasha Lord", "अन्तर्दशा स्वामी"),
    PRATYANTARDASHA_LORD("Pratyantardasha Lord", "प्रत्यन्तर्दशा स्वामी"),
    DASHA_SCORE("Dasha Score", "दशा अंक"),

    // ========== Dasha Nature Values ==========
    NATURE_BENEFIC("Benefic", "शुभ"),
    NATURE_MALEFIC("Malefic", "अशुभ"),
    NATURE_NEUTRAL("Neutral", "तटस्थ"),
    NATURE_MIXED("Mixed", "मिश्रित"),

    // ========== Dignity Values ==========
    DIGNITY_EXALTED("Exalted", "उच्च"),
    DIGNITY_OWN("Own Sign", "स्वराशि"),
    DIGNITY_MOOLTRIKONA("Mooltrikona", "मूलत्रिकोण"),
    DIGNITY_FRIEND("Friendly Sign", "मित्र राशि"),
    DIGNITY_NEUTRAL("Neutral Sign", "सम राशि"),
    DIGNITY_ENEMY("Enemy Sign", "शत्रु राशि"),
    DIGNITY_DEBILITATED("Debilitated", "नीच"),

    // ========== Gochara Analysis ==========
    TRANSIT_HOUSE("Transit House from Moon", "चन्द्रमाबाट गोचर भाव"),
    TRANSIT_RESULT("Transit Result", "गोचर फल"),
    TRANSIT_FAVORABLE("Favorable Transit", "शुभ गोचर"),
    TRANSIT_UNFAVORABLE("Unfavorable Transit", "अशुभ गोचर"),
    TRANSIT_VEDHA("Vedha (Obstruction)", "वेध (बाधा)"),
    TRANSIT_VEDHA_ACTIVE("Vedha Active - Effects Nullified", "वेध सक्रिय - प्रभाव निष्क्रिय"),
    TRANSIT_VEDHA_CLEAR("No Vedha - Full Effects", "वेध छैन - पूर्ण प्रभाव"),
    GOCHARA_SCORE("Gochara Score", "गोचर अंक"),

    // ========== Ashtakavarga Analysis ==========
    BAV_SCORE("BAV Score (Bindu)", "BAV अंक (बिन्दु)"),
    SAV_SCORE("SAV Score (Total Bindu)", "SAV अंक (कुल बिन्दु)"),
    BINDU_STRENGTH("Bindu Strength", "बिन्दु बल"),
    HIGH_BINDU("High Bindu (5+)", "उच्च बिन्दु (५+)"),
    MEDIUM_BINDU("Medium Bindu (3-4)", "मध्यम बिन्दु (३-४)"),
    LOW_BINDU("Low Bindu (0-2)", "न्यून बिन्दु (०-२)"),
    SAV_THRESHOLD("SAV Threshold (28+)", "SAV सीमा (२८+)"),
    SAV_ABOVE_THRESHOLD("SAV Above Threshold - Favorable", "SAV सीमाभन्दा माथि - शुभ"),
    SAV_BELOW_THRESHOLD("SAV Below Threshold - Challenging", "SAV सीमाभन्दा तल - चुनौतीपूर्ण"),
    ASHTAKAVARGA_SCORE("Ashtakavarga Score", "अष्टकवर्ग अंक"),

    // ========== Timeline Labels ==========
    CURRENT_PERIOD("Current Period", "हालको अवधि"),
    NEXT_MONTH("Next Month", "अर्को महिना"),
    MONTHS_AHEAD("months ahead", "महिना अगाडि"),
    PEAK_PERIOD("Peak Period", "शिखर अवधि"),
    LOW_PERIOD("Low Period", "न्यून अवधि"),
    TRANSITION_PERIOD("Transition Period", "परिवर्तन अवधि"),

    // ========== Life Areas ==========
    AREA_CAREER("Career & Profession", "करियर र पेशा"),
    AREA_FINANCE("Finance & Wealth", "वित्त र सम्पत्ति"),
    AREA_RELATIONSHIPS("Relationships & Marriage", "सम्बन्ध र विवाह"),
    AREA_HEALTH("Health & Vitality", "स्वास्थ्य र शक्ति"),
    AREA_EDUCATION("Education & Knowledge", "शिक्षा र ज्ञान"),
    AREA_SPIRITUALITY("Spirituality & Growth", "आध्यात्मिकता र विकास"),
    AREA_FAMILY("Family & Home", "परिवार र गृह"),
    AREA_TRAVEL("Travel & Movement", "यात्रा र गतिविधि"),
    AREA_LEGAL("Legal & Government", "कानूनी र सरकारी"),
    AREA_PROPERTY("Property & Assets", "सम्पत्ति र सम्पदा"),

    // ========== Recommendations ==========
    REC_HIGHLY_FAVORABLE("Highly favorable period - initiate important matters", "अत्यन्त शुभ अवधि - महत्त्वपूर्ण कार्य सुरु गर्नुहोस्"),
    REC_FAVORABLE("Favorable conditions - proceed with confidence", "शुभ परिस्थिति - विश्वासका साथ अगाडि बढ्नुहोस्"),
    REC_MODERATELY_FAVORABLE("Moderately favorable - selective action recommended", "मध्यम शुभ - छनोट गरिएको कार्य सिफारिस"),
    REC_NEUTRAL("Neutral period - maintain current course", "तटस्थ अवधि - हालको मार्ग कायम राख्नुहोस्"),
    REC_CAUTION("Exercise caution - avoid major decisions", "सावधानी अपनाउनुहोस् - ठूला निर्णयबाट बच्नुहोस्"),
    REC_CHALLENGING("Challenging period - patience and remedies advised", "चुनौतीपूर्ण अवधि - धैर्य र उपाय सल्लाह दिइन्छ"),
    REC_SPIRITUAL("Focus on spiritual practices and self-improvement", "आध्यात्मिक अभ्यास र आत्म-सुधारमा ध्यान दिनुहोस्"),

    // ========== Classical References ==========
    REF_THREE_PILLARS("Based on the Three Pillars of Vedic Timing (Tri-Skandha Jyotish)", "वैदिक समय निर्धारणको तीन स्तम्भमा आधारित (त्रि-स्कन्ध ज्योतिष)"),
    REF_BPHS("Brihat Parasara Hora Shastra - Dasha Phala", "बृहत् पाराशर होरा शास्त्र - दशा फल"),
    REF_PHALADEEPIKA("Phaladeepika - Gochara Phala", "फलदीपिका - गोचर फल"),
    REF_ASHTAKAVARGA_SOURCE("BPHS Chapter 66-72 - Ashtakavarga System", "BPHS अध्याय ६६-७२ - अष्टकवर्ग प्रणाली"),
    REF_SYNTHESIS_METHOD("Synthesis method: Weighted harmonic mean of three pillars with Vedha correction", "संश्लेषण विधि: वेध सुधारसहित तीन स्तम्भको भारित हार्मोनिक मध्यमान"),

    // ========== Error / Empty States ==========
    ERROR_NO_CHART("No chart data available for synthesis", "संश्लेषणको लागि कुण्डली डाटा उपलब्ध छैन"),
    ERROR_CALCULATION_FAILED("Synthesis calculation failed. Please try again.", "संश्लेषण गणना असफल भयो। कृपया पुन: प्रयास गर्नुहोस्।"),
    ERROR_MOON_NOT_FOUND("Moon position required for Gochara analysis", "गोचर विश्लेषणको लागि चन्द्रमा स्थिति आवश्यक छ"),
    EMPTY_FORECAST("No significant periods detected in forecast range", "भविष्यवाणी दायरामा कुनै महत्त्वपूर्ण अवधि भेटिएन"),

    // ========== Interpretation Templates ==========
    INTERP_STRONG_ALL_THREE("All three pillars align favorably — this is a rare and powerful window for success.", "तीनवटै स्तम्भ शुभ रूपमा मिल्दछन् — यो सफलताको लागि दुर्लभ र शक्तिशाली सञ्झ्याल हो।"),
    INTERP_DASHA_STRONG_TRANSIT_WEAK("Dasha period is strong but transits are unfavorable — inner potential is high, but external timing needs patience.", "दशा बलियो छ तर गोचर प्रतिकूल छ — आन्तरिक क्षमता उच्च छ तर बाह्य समयको लागि धैर्य चाहिन्छ।"),
    INTERP_TRANSIT_STRONG_DASHA_WEAK("Transits and Ashtakavarga favor action, but Dasha period is challenging — opportunities may come with obstacles.", "गोचर र अष्टकवर्गले कार्यलाई समर्थन गर्छ तर दशा चुनौतीपूर्ण छ — अवसरहरू बाधासहित आउन सक्छन्।"),
    INTERP_ASHTAKAVARGA_HIGH("High Ashtakavarga bindu in transit sign amplifies the positive effects of Gochara.", "गोचर राशिमा उच्च अष्टकवर्ग बिन्दुले गोचरको सकारात्मक प्रभाव बढाउँछ।"),
    INTERP_ASHTAKAVARGA_LOW("Low Ashtakavarga bindu weakens the transit even if Gochara house is favorable.", "गोचर भाव शुभ भए पनि न्यून अष्टकवर्ग बिन्दुले गोचरलाई कमजोर बनाउँछ।"),
    INTERP_VEDHA_NULLIFIED("Vedha point active — favorable Gochara effects are obstructed by another planet.", "वेध बिन्दु सक्रिय — शुभ गोचर प्रभाव अर्को ग्रहले बाधा पुर्‍याएको छ।"),
    INTERP_SANDHI_PERIOD("Dasha Sandhi (transition) period — effects of both ending and beginning periods mix, causing uncertainty.", "दशा सन्धि (परिवर्तन) अवधि — अन्त्य र सुरुवात दुवै अवधिको प्रभाव मिसिन्छ, अनिश्चितता ल्याउँछ।"),

    // ========== Month Names ==========
    MONTH_JAN("January", "माघ"),
    MONTH_FEB("February", "फागुन"),
    MONTH_MAR("March", "चैत्र"),
    MONTH_APR("April", "वैशाख"),
    MONTH_MAY("May", "जेठ"),
    MONTH_JUN("June", "असार"),
    MONTH_JUL("July", "साउन"),
    MONTH_AUG("August", "भदौ"),
    MONTH_SEP("September", "असोज"),
    MONTH_OCT("October", "कार्तिक"),
    MONTH_NOV("November", "मंसिर"),
    MONTH_DEC("December", "पुष"),

    // ========== Misc ==========
    WEIGHT_LABEL("Weight", "भार"),
    SCORE_LABEL("Score", "अंक"),
    OUT_OF("out of", "मध्ये"),
    PERCENT("%", "%"),
    NO_DATA("No data", "डाटा छैन"),
    FAVORABLE("Favorable", "शुभ"),
    UNFAVORABLE("Unfavorable", "अशुभ"),
    CURRENTLY_ACTIVE("Currently Active", "हाल सक्रिय"),
    UPCOMING("Upcoming", "आगामी"),
    PEAK("Peak", "शिखर"),
    LOW("Low", "न्यून");
}
