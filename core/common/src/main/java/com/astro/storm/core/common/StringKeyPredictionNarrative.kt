package com.astro.storm.core.common

/**
 * Additional narrative templates for prediction timing and insights.
 */
enum class StringKeyPredictionNarrative(override val en: String, override val ne: String) : StringKeyInterface {
    PRED_SANDHI_EVENT_TEMPLATE("%s → %s %s transition", "%s → %s %s संक्रमण"),
    PRED_SANDHI_SIGNIFICANCE_TEMPLATE("This %s change marks a shift in focus and results.", "यो %s परिवर्तनले ध्यान र नतिजामा परिवर्तन संकेत गर्दछ।"),
    PRED_PERIOD_REASON_TEMPLATE("%s period emphasizes %s.", "%s अवधिले %s मा जोड दिन्छ।"),
    PRED_AREA_SCORE_TEMPLATE("Current score reflects %s influences.", "हालको अंक %s प्रभावहरू दर्शाउँछ।")
}
