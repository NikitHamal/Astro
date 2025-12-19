package com.astro.storm.data.localization

enum class StringKeyDosha(override val en: String, override val ne: String) : StringKeyInterface {
    // ============================================
    // DOSHA ANALYSIS
    // ============================================
    DOSHA_ANALYSIS("Dosha Analysis", "दोष विश्लेषण"),
    KAL_SARPA_DOSHA("Kal Sarpa Dosha", "काल सर्प दोष"),
    PITRA_DOSHA("Pitra Dosha", "पितृ दोष"),
    SADE_SATI("Sade Sati", "साढे साती"),
    SADE_SATI_ANALYSIS("Sade Sati Analysis", "साढे साती विश्लेषण"),
    SADE_SATI_RISING("Rising Phase", "उदय चरण"),
    SADE_SATI_PEAK("Peak Phase", "शिखर चरण"),
    SADE_SATI_SETTING("Setting Phase", "अस्त चरण"),
    SADE_SATI_NOT_ACTIVE("Not Active", "सक्रिय छैन"),
    SMALL_PANOTI("Small Panoti (Dhaiya)", "सानो पनोती (ढैया)"),
    ASHTAMA_SHANI("Ashtama Shani", "अष्टम शनि"),
    REMEDIES_FOR("Remedies for %s", "%s को लागि उपाय"),

    // Sade Sati Favorable/Challenging Factors
    FACTOR_SATURN_EXALTED("Saturn exalted in transit - effects reduced", "शनि गोचरमा उच्च - प्रभाव कम"),
    FACTOR_SATURN_OWN_SIGN("Saturn in own sign - effects well-managed", "शनि स्वराशिमा - प्रभाव राम्रोसँग व्यवस्थित"),
    FACTOR_YOGAKARAKA("Saturn is Yogakaraka - may bring positive results", "शनि योगकारक - सकारात्मक परिणाम ल्याउन सक्छ"),
    FACTOR_NATAL_SATURN_STRONG("Natal Saturn strong - better equipped to handle", "जन्म शनि बलियो - सामना गर्न राम्रोसँग सुसज्जित"),
    FACTOR_SATURN_DEBILITATED("Saturn debilitated in transit - effects more challenging", "शनि गोचरमा नीच - प्रभावहरू थप चुनौतीपूर्ण"),
    FACTOR_NATAL_MOON_WEAK("Natal Moon weak - emotional resilience tested", "जन्म चन्द्र कमजोर - भावनात्मक लचिलोपन परीक्षण"),
    FACTOR_NATAL_SATURN_WEAK("Natal Saturn weak - transit effects more pronounced", "जन्म शनि कमजोर - गोचर प्रभावहरू थप स्पष्ट"),
    FACTOR_NATAL_SATURN_RETROGRADE("Natal Saturn retrograde - internal processing of karma", "जन्म शनि वक्री - कर्मको आन्तरिक प्रशोधन"),
}
