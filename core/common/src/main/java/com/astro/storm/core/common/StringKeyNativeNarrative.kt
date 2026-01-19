package com.astro.storm.core.common

enum class StringKeyNativeNarrative(override val en: String, override val ne: String) : StringKeyInterface {
    CHARACTER_SUMMARY_CORE(
        "Ascendant %s, Moon %s, and Sun %s together shape a layered personality focused on authentic expression and emotional depth.",
        "लग्न %s, चन्द्र %s, र सूर्य %s को संयोजनले सत्य अभिव्यक्ति र भावनात्मक गहिराइमा केन्द्रित बहुपरतीय व्यक्तित्व बनाउँछ।"
    ),
    CHARACTER_ELEMENTAL_NOTE(
        "Elemental emphasis on %s tones your instincts and daily decisions.",
        "%s तत्वको प्रभुत्वले तपाईंको सहज प्रवृत्ति र दैनिक निर्णयलाई रंग दिन्छ।"
    ),
    CHARACTER_MODALITY_NOTE(
        "Modality %s indicates your natural rhythm in initiating, sustaining, and adapting.",
        "%s मोडालिटीले सुरुवात, निरन्तरता, र अनुकूलनमा तपाईंको प्राकृतिक लय देखाउँछ।"
    ),
    CHARACTER_NAKSHATRA_NOTE(
        "Nakshatra %s adds a subtle psychological layer to your motivations and responses.",
        "नक्षत्र %s ले तपाईंको प्रेरणा र प्रतिक्रियामा सूक्ष्म मनोवैज्ञानिक तह थप्छ।"
    ),
    CHARACTER_STRENGTH_NOTE(
        "Personality strength is %s, indicating how effortlessly you express your core traits.",
        "व्यक्तित्व शक्ति %s छ, जसले तपाईंका मूल गुणहरू कत्तिको सहज रूपमा व्यक्त हुन्छन् भन्ने देखाउँछ।"
    ),

    CAREER_SUMMARY_CORE(
        "Tenth lord %s in house %s with %s dignity sets the foundation for career direction and authority.",
        "%s १०औं स्वामी %s औं भावमा %s गरिमासहित भएकाले क्यारियर दिशा र अधिकारको आधार बनाउँछ।"
    ),
    CAREER_PLANETS_NOTE(
        "Planets influencing the 10th house shift professional focus toward %s.",
        "१०औं भावमा प्रभाव पार्ने ग्रहहरूले पेशागत केन्द्र %s तर्फ सार्छन्।"
    ),
    CAREER_STRENGTH_NOTE(
        "Career strength is %s; progress depends on strategic consistency.",
        "क्यारियर शक्ति %s छ; प्रगति रणनीतिक निरन्तरतामा निर्भर छ।"
    ),

    MARRIAGE_SUMMARY_CORE(
        "Seventh lord %s in house %s with %s dignity shapes relationship dynamics and commitment patterns.",
        "सातौं स्वामी %s %s औं भावमा %s गरिमासहित भएकाले सम्बन्ध र प्रतिबद्धताको ढाँचा तय गर्छ।"
    ),
    MARRIAGE_VENUS_NOTE(
        "Venus strength is %s, guiding harmony, attraction, and relational tone.",
        "शुक्रको शक्ति %s छ, जसले सामंजस्य, आकर्षण, र सम्बन्धको स्वर निर्देशित गर्छ।"
    ),
    MARRIAGE_TIMING_NOTE(
        "Marriage timing leans toward %s based on planetary placements.",
        "ग्रह स्थितिहरूका आधारमा विवाह समय %s तर्फ झुक्छ।"
    ),

    HEALTH_SUMMARY_CORE(
        "Ascendant lord vitality is %s, shaping overall constitution and recovery patterns.",
        "लग्न स्वामीको शक्ति %s छ, जसले समग्र शरीर बनावट र सुधारको ढाँचा तय गर्छ।"
    ),
    HEALTH_LORDS_NOTE(
        "Sixth lord %s and eighth lord %s define stress and resilience patterns.",
        "षष्ठ स्वामी %s र अष्टम स्वामी %s ले तनाव र सहनशीलताको ढाँचा निर्धारण गर्छन्।"
    ),
    HEALTH_CONSTITUTION_NOTE(
        "Constitution type %s indicates the primary health rhythm.",
        "शरीर प्रकृति %s ले स्वास्थ्यको मूल लय देखाउँछ।"
    ),

    WEALTH_SUMMARY_CORE(
        "Second lord %s and eleventh lord %s shape income, accumulation, and gains.",
        "दोस्रो स्वामी %s र एघारौँ स्वामी %s ले आय, सञ्चय, र लाभको ढाँचा बनाउँछन्।"
    ),
    WEALTH_DHANA_NOTE(
        "Dhana yoga presence is %s, influencing asset growth and prosperity cycles.",
        "धन योगको उपस्थिती %s छ, जसले सम्पत्ति वृद्धि र समृद्धि चक्रलाई प्रभाव पार्छ।"
    ),
    WEALTH_STRENGTH_NOTE(
        "Wealth potential is %s; stability grows through structured planning.",
        "धन सम्भावना %s छ; संरचित योजना मार्फत स्थिरता बढ्छ।"
    ),

    EDUCATION_SUMMARY_CORE(
        "Fourth lord %s and fifth lord %s guide foundational learning and higher knowledge.",
        "चौथो स्वामी %s र पाँचौँ स्वामी %s ले आधारभूत अध्ययन र उच्च ज्ञानलाई निर्देशित गर्छन्।"
    ),
    EDUCATION_MERCURY_NOTE(
        "Mercury strength is %s, shaping intellect, analysis, and academic adaptability.",
        "बुधको शक्ति %s छ, जसले बुद्धि, विश्लेषण, र शैक्षिक अनुकूलनशीलता आकार दिन्छ।"
    ),
    EDUCATION_POTENTIAL_NOTE(
        "Academic potential is %s with focus on disciplined study patterns.",
        "शैक्षिक सम्भावना %s छ, अनुशासित अध्ययन ढाँचामा केन्द्रित।"
    ),

    SPIRITUAL_SUMMARY_CORE(
        "Ninth lord %s and twelfth lord %s orient the spiritual path and inner growth.",
        "नवौँ स्वामी %s र बाह्रौँ स्वामी %s ले आध्यात्मिक मार्ग र आन्तरिक वृद्धिलाई निर्देशित गर्छन्।"
    ),
    SPIRITUAL_KETU_NOTE(
        "Ketu placement highlights detachment, intuition, and inner exploration.",
        "केतुको स्थानले वैराग्य, अन्तर्ज्ञान, र आन्तरिक अन्वेषणलाई उजागर गर्छ।"
    ),
    SPIRITUAL_INCLINATION_NOTE(
        "Spiritual inclination is %s, with practices aligned to sustained awareness.",
        "आध्यात्मिक झुकाव %s छ, निरन्तर चेतनासँग मिल्ने अभ्यासहरू सहित।"
    );
}
