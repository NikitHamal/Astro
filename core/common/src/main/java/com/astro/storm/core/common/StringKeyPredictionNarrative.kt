package com.astro.storm.core.common

enum class StringKeyPredictionNarrative(override val en: String, override val ne: String) : StringKeyInterface {
    PRED_OVERVIEW_ASC_MOON_TEMPLATE(
        "Your ascendant in %s and Moon in %s create a layered path of self-expression and emotional fulfillment.",
        "तपाईंको लग्न %s र चन्द्र %s मा हुँदा आत्म-अभिव्यक्ति र भावनात्मक पूर्तिको तहगत मार्ग बनाउँछ।"
    ),
    PRED_OVERVIEW_ELEMENT_TEMPLATE(
        "Elemental dominance emphasizes %s qualities in day-to-day choices and temperament.",
        "तत्वीय प्रभुत्वले दैनन्दिन चयन र स्वभावमा %s गुणहरूलाई बल दिन्छ।"
    ),
    PRED_OVERVIEW_MODALITY_TEMPLATE(
        "Modality focus highlights %s tendencies in decision-making and life direction.",
        "मोडालिटीको प्रभावले निर्णय र जीवन दिशामा %s प्रवृत्तिलाई प्रमुख बनाउँछ।"
    ),
    PRED_OVERVIEW_YOGA_TEMPLATE(
        "Yoga strength of %s/100 shows how naturally supportive combinations operate in your chart.",
        "योगको शक्ति %s/१०० ले तपाईंको कुण्डलीमा सहायक संयोजनहरू कत्तिको सहज रूपमा सक्रिय छन् भन्ने देखाउँछ।"
    ),
    PRED_OVERVIEW_DASHA_TEMPLATE(
        "The current dasha of %s guides the immediate rhythm of events and priorities.",
        "वर्तमान %s दशाले घटनाहरू र प्राथमिकताहरूको तत्कालीन ताल निर्देशित गर्छ।"
    ),
    PRED_OVERVIEW_DASHA_UNKNOWN("the ruling planet", "शासक ग्रह"),

    PRED_STRENGTH_PLANET_STRONG("%s is a pronounced source of strength and focus.", "%s प्रमुख शक्ति र केन्द्र हो।"),
    PRED_STRENGTH_PLANET_SUPPORTIVE("%s offers steady support across key life areas.", "%s ले मुख्य जीवन क्षेत्रहरूमा स्थिर सहयोग दिन्छ।"),

    PRED_DASHA_GENERAL(
        "The %s period emphasizes themes of house %s and sign %s, bringing a balanced mix of growth and responsibility.",
        "%s अवधिले %s औं भाव र %s राशिका विषयहरूलाई जोड दिन्छ, जसले विकास र जिम्मेवारीको सन्तुलित मिश्रण ल्याउँछ।"
    ),
    PRED_DASHA_POSITIVE(
        "The %s period is strong, activating house %s in %s for visible progress and confidence.",
        "%s अवधि बलियो छ, %s औं भावलाई %s मा सक्रिय गरेर स्पष्ट प्रगति र आत्मविश्वास दिन्छ।"
    ),
    PRED_DASHA_STEADY(
        "The %s period brings steady results through house %s in %s with disciplined effort.",
        "%s अवधि %s औं भावलाई %s मा सक्रिय गर्दै अनुशासित प्रयासबाट स्थिर परिणाम ल्याउँछ।"
    ),
    PRED_DASHA_CHALLENGE(
        "The %s period asks for patience as house %s in %s matures through lessons and refinement.",
        "%s अवधिले धैर्य माग्छ, किनकि %s औं भाव %s मा पाठ र परिष्करणमार्फत परिपक्व हुन्छ।"
    ),
    PRED_DASHA_ANTAR_TEMPLATE(
        "The sub-period of %s adds a secondary layer of focus.",
        "%s उप-अवधिले दोस्रो तहको केन्द्रितता थप्छ।"
    ),

    PRED_TRANSIT_HIGHLIGHT(
        "%s influences are emphasized through %s in the %s house, shaping near-term decisions.",
        "%s को प्रभाव %s राशिमा %s औं भावमार्फत प्रबल हुन्छ, जसले नजिकको निर्णयलाई आकार दिन्छ।"
    ),

    PRED_FOCUS_GENERAL("Stability, consistency, and mindful pacing.", "स्थिरता, निरन्तरता, र सचेत गतिमा ध्यान।"),
    PRED_FOCUS_PLANET("Focus on %s-related themes and responsibilities.", "%s सम्बन्धित विषय र जिम्मेवारीमा ध्यान।"),

    PRED_CHALLENGE_AREA_PLANET("%s Focus", "%s केन्द्रित"),
    PRED_CHALLENGE_DESC_PLANET(
        "%s in house %s can require patience and structured effort to avoid drain.",
        "%s %s औं भावमा हुँदा थकानबाट बच्न धैर्य र संरचित प्रयास आवश्यक हुन्छ।"
    ),
    PRED_CHALLENGE_MITIGATION(
        "Strengthen %s through disciplined routines, respectful boundaries, and steady practice.",
        "%s लाई अनुशासित दिनचर्या, सम्मानजनक सीमाना र स्थिर अभ्यासमार्फत सुदृढ गर्नुहोस्।"
    ),

    PRED_OPPORTUNITY_AREA_PLANET("%s Advantage", "%s लाभ"),
    PRED_OPPORTUNITY_DESC_PLANET(
        "%s in %s activates promising momentum and supportive alliances.",
        "%s %s मा हुँदा आशाजनक गति र सहायक गठबन्धन सक्रिय हुन्छ।"
    ),
    PRED_OPPORTUNITY_TIMING(
        "Best utilized when house %s themes are in focus.",
        "%s औं भावका विषयहरू सक्रिय हुँदा यसलाई राम्रोसँग प्रयोग गर्न सकिन्छ।"
    ),
    PRED_OPPORTUNITY_LEVERAGE(
        "Leverage %s by taking initiative and aligning with long-term priorities.",
        "%s लाई दीर्घकालीन प्राथमिकतासँग मिलाएर पहल गर्दै उपयोग गर्नुहोस्।"
    ),

    PRED_AREA_SHORT_HIGH(
        "In the short term, %s shows strong momentum; align actions with supportive allies.",
        "छोटो अवधिमा %s मा बलियो गति छ; सहायक व्यक्तिहरूसँग कदम मिलाउनुहोस्।"
    ),
    PRED_AREA_MEDIUM_HIGH(
        "Mid-term trends favor expansion in %s with steady, strategic focus.",
        "मध्यम अवधिमा %s मा विस्तारको पक्ष बलियो छ, स्थिर र रणनीतिक ध्यानसँग।"
    ),
    PRED_AREA_LONG_HIGH(
        "Long-term outcomes in %s are promising, especially with consistent discipline.",
        "दीर्घकालीन रूपमा %s मा परिणाम आशाजनक छन्, विशेष गरी निरन्तर अनुशासनसँग।"
    ),
    PRED_AREA_SHORT_STEADY(
        "In the short term, %s is stable; avoid rushed decisions and refine priorities.",
        "छोटो अवधिमा %s स्थिर छ; हतारो निर्णय टार्नुहोस् र प्राथमिकता परिष्कृत गर्नुहोस्।"
    ),
    PRED_AREA_MEDIUM_STEADY(
        "Mid-term development in %s benefits from patience and incremental growth.",
        "मध्यम अवधिमा %s को विकास धैर्य र क्रमिक वृद्धिबाट लाभान्वित हुन्छ।"
    ),
    PRED_AREA_LONG_STEADY(
        "Long-term progress in %s is achievable through sustained effort and realism.",
        "दीर्घकालमा %s मा प्रगति दीर्घ प्रयास र यथार्थपरक दृष्टिले सम्भव हुन्छ।"
    ),
    PRED_AREA_SHORT_CAUTION(
        "Short-term %s demands cautious planning and mindful pacing.",
        "छोटो अवधिमा %s ले सावधानीपूर्ण योजना र सचेत गतिको माग गर्छ।"
    ),
    PRED_AREA_MEDIUM_CAUTION(
        "Mid-term %s is mixed; balance ambition with practical safeguards.",
        "मध्यम अवधिमा %s मिश्रित छ; महत्त्वाकांक्षा र व्यवहारिक सुरक्षा बीच सन्तुलन राख्नुहोस्।"
    ),
    PRED_AREA_LONG_CAUTION(
        "Long-term %s improves when foundations are strengthened and waste is reduced.",
        "दीर्घकालमा %s सुधार हुन्छ जब आधार बलियो बनाइन्छ र अपव्यय घटाइन्छ।"
    ),

    PRED_FACTOR_HOUSE_LORD("House %s lord: %s", "%s औं भावका स्वामी: %s"),
    PRED_FACTOR_SUPPORT_STRONG("Strong benefic support reinforces outcomes.", "बलियो शुभ ग्रह समर्थनले परिणामलाई सुदृढ गर्छ।"),
    PRED_FACTOR_SUPPORT_BALANCE("Balance benefic and malefic pressures through conscious choices.", "सचेत निर्णयले शुभ र अशुभ ग्रह दबाबलाई सन्तुलन गर्छ।"),

    PRED_ADVICE_HIGH(
        "Take bold yet grounded steps in %s; momentum is on your side.",
        "%s मा साहसी तर स्थिर कदम चाल्नुहोस्; गति तपाईंको पक्षमा छ।"
    ),
    PRED_ADVICE_STEADY(
        "Maintain rhythm in %s with consistency, review, and gentle refinement.",
        "%s मा निरन्तरता, समीक्षा, र हल्का परिष्करणका साथ लय कायम राख्नुहोस्।"
    ),
    PRED_ADVICE_CAUTION(
        "Prioritize safeguards in %s, conserve energy, and avoid impulsive moves.",
        "%s मा सुरक्षा प्राथमिकता दिनुहोस्, ऊर्जा बचत गर्नुहोस्, र आवेगपूर्ण कदम टार्नुहोस्।"
    ),

    PRED_TIMING_FAVORABLE_DASHA(
        "%s dasha supports constructive progress in multiple life areas.",
        "%s दशाले विभिन्न जीवन क्षेत्रमा रचनात्मक प्रगति समर्थन गर्छ।"
    ),
    PRED_TIMING_CAUTION_DASHA(
        "%s dasha requires reflection and steady adjustment before expansion.",
        "%s दशामा विस्तार अघि मनन र स्थिर समायोजन आवश्यक हुन्छ।"
    ),
    PRED_TIMING_SANDHI_EVENT(
        "%s → %s transition",
        "%s → %s संक्रमण"
    ),
    PRED_TIMING_SANDHI_SIGNIFICANCE(
        "Dasha Sandhi (%s) brings recalibration and review.",
        "दशा सन्धि (%s) ले पुनर्सन्तुलन र समीक्षा ल्याउँछ।"
    ),

    PRED_TIMING_BEST_JUPITER("Learning • Expansion • Mentorship", "शिक्षा • विस्तार • मार्गदर्शन"),
    PRED_TIMING_BEST_VENUS("Harmony • Relationships • Creativity", "सामंजस्य • सम्बन्ध • रचनात्मकता"),
    PRED_TIMING_BEST_SATURN("Structure • Discipline • Long-term goals", "संरचना • अनुशासन • दीर्घकालीन लक्ष्य"),
    PRED_TIMING_BEST_SUN("Leadership • Recognition • Self-confidence", "नेतृत्व • मान्यता • आत्मविश्वास"),
    PRED_TIMING_BEST_MOON("Emotional care • Family • Inner balance", "भावनात्मक हेरचाह • परिवार • आन्तरिक सन्तुलन"),
    PRED_TIMING_BEST_MERCURY("Communication • Commerce • Learning", "सञ्चार • व्यापार • अध्ययन"),
    PRED_TIMING_BEST_MARS("Courage • Action • Competitive wins", "साहस • कार्य • प्रतिस्पर्धी सफलता"),
    PRED_TIMING_BEST_RAHU("Innovation • Foreign links • Unconventional moves", "नवप्रवर्तन • विदेश सम्बन्ध • अपरम्परागत कदम"),
    PRED_TIMING_BEST_KETU("Detachment • Research • Spiritual focus", "वैराग्य • अनुसन्धान • आध्यात्मिक केन्द्रितता"),
    PRED_TIMING_BEST_GENERAL("Steady progress • Practical steps • Consistency", "स्थिर प्रगति • व्यवहारिक कदम • निरन्तरता"),

    PRED_TIMING_AVOID_SATURN("Over-commitment • Heavy debts • Burnout", "अत्यधिक प्रतिबद्धता • भारी ऋण • थकान"),
    PRED_TIMING_AVOID_RAHU("Over-risking • Sudden speculation • Unrealistic promises", "अत्यधिक जोखिम • अचानक सट्टा • अवास्तविक वाचा"),
    PRED_TIMING_AVOID_KETU("Abrupt detachment • Isolation • Neglecting duties", "अचानक विच्छेद • एकान्त • जिम्मेवारी उपेक्षा"),
    PRED_TIMING_AVOID_MARS("Conflict escalation • Impulsive actions • Accidents", "द्वन्द्व बढाउने • आवेगपूर्ण काम • दुर्घटना"),
    PRED_TIMING_AVOID_GENERAL("Hasty commitments • Neglecting rest • Unclear plans", "हतारो प्रतिबद्धता • आराम उपेक्षा • अस्पष्ट योजना")
}
