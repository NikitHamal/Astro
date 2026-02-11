package com.astro.storm.core.common

/**
 * Prediction and Dasha Interpretation Templates
 */
enum class StringKeyPredictionsInterp(override val en: String, override val ne: String, override val hi: String) : StringKeyInterface {
    // Dasha Theme Templates
    DASHA_THEME_DEFAULT(
        "This period brings unique and unconventional developments related to %s's energy. Expect the unexpected and stay open to new perspectives.",
        "यो अवधिले %sको ऊर्जासँग सम्बन्धित अद्वितीय र अपरम्परागत विकासहरू ल्याउँछ।"
    ),
    ANTARDASHA_THEME_TEMPLATE(
        "Within the %1\$s period, %2\$s's sub-period refines the experience by adding %3\$s.",
        "%1\$s अवधि भित्र, %2\$sको उप-अवधिले %3\$s थपेर अनुभव परिष्कृत गर्छ।"
    ),

    // Advice Templates
    DASHA_ADVICE_DEFAULT(
        "Work with %s's energy constructively during this period.",
        "यस अवधिमा %sको ऊर्जासँग रचनात्मक रूपमा काम गर्नुहोस्।"
    ),

    // Preview Templates
    DASHA_PREVIEW_TEMPLATE(
        "%1\$s period ahead with %2\$s promise. Prepare for %3\$s.",
        "%1\$s अवधि %2\$s वाचाको साथ अगाडि। %3\$sको लागि तयार रहनुहोस्।"
    ),

    // Life Area Templates
    LIFE_AREA_EFFECT_TEMPLATE(
        "%1\$s's influence on %2\$s %3\$s.",
        "%1\$sको %2\$s मा प्रभाव %3\$s।"
    ),
    SHORT_TERM_EFFECT_TEMPLATE(
        "Short-term %1\$s matters are influenced by %2\$s's current transit.",
        "छोटो-अवधि %1\$s मामिलाहरू %2\$sको वर्तमान गोचरले प्रभावित छन्।"
    ),

    // Nodal Axis
    NODAL_AXIS_TEMPLATE(
        "Rahu in %1\$d house / Ketu in %2\$d house axis creates karmic focus on worldly vs spiritual balance.",
        "राहू %1\$d भावमा / केतु %2\$d भावमा अक्षले सांसारिक बनाम आध्यात्मिक सन्तुलनमा कार्मिक फोकस सिर्जना गर्छ।"
    ),

    // Yearly Advice
    YEARLY_ADVICE_DEFAULT(
        "Align your efforts with %s's energy for optimal results this year.",
        "यस वर्ष इष्टतम परिणामहरूको लागि %sको ऊर्जासँग प्रयासहरू मिलाउनुहोस्।"
    ),

    // Outlook Summary
    AREA_OUTLOOK_TEMPLATE(
        "%1\$s shows %2\$s potential this period.",
        "%1\$sले यस अवधिमा %2\$s क्षमता देखाउँछ।"
    ),

    // Prediction Error
    PREDICTION_FAILED("Prediction failed", "भविष्यवाणी असफल भयो", "भविष्यवाणी असफल भयह"),
    ,
    ANALYSIS_FAILED("Analysis failed", "विश्लेषण असफल भयो", "विश्लेषण असफल भयह"),
,
}
