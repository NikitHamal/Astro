package com.astro.vajra.core.common

/**
 * String keys for Jaimini Astrology analysis
 */
enum class StringKeyJaimini(override val en: String, override val ne: String) : StringKeyInterface {
    TITLE("Jaimini Karakas", "जैमिनी कारक"),
    SUBTITLE("Chara Karaka Analysis", "चर कारक विश्लेषण"),
    
    TAB_KARAKAS("Karakas", "कारकहरू"),
    TAB_KARAKAMSHA("Karakamsha", "कारकांश"),
    TAB_YOGAS("Yogas", "योगहरू"),
    TAB_INTERPRETATION("Interpretation", "व्याख्या"),
    
    HEADER_7_KARAKAS("7 Chara Karakas", "७ चर कारकहरू"),
    SUBTITLE_7_KARAKAS("Variable significators based on planetary degrees", "ग्रहहरूको अंशमा आधारित चर कारकहरू"),
    
    SYSTEM_INFO_TITLE("Karaka System", "कारक प्रणाली"),
    SYSTEM_INFO_CONTENT("Using %s system", "%s प्रणाली प्रयोग गर्दै"),
    
    DEGREE_LABEL("Degree: %s°", "अंश: %s°"),
    
    KARAKAMSHA_TITLE("Karakamsha", "कारकांश"),
    KARAKAMSHA_SUBTITLE("Atmakaraka's position in Navamsa determines soul's direction", "नवांशमा आत्मकारकको स्थितिले आत्माको मार्ग निर्धारण गर्दछ"),
    
    SWAMSHA_TITLE("Swamsha", "स्वांश"),
    SWAMSHA_SUBTITLE("Navamsa Lagna - the spiritual manifestation point", "नवांश लग्न - आध्यात्मिक प्रकटीकरण बिन्दु"),
    
    SIGN_LABEL("Karakamsha Sign", "कारकांश राशि"),
    SWAMSHA_LAGNA_LABEL("Swamsha (Navamsa Lagna)", "स्वांश (नवांश लग्न)"),
    
    LIFE_PATH_INDICATORS("Life Path Indicators", "जीवन मार्ग सूचकहरू"),
    INDICATOR_CAREER("Career", "क्यारियर"),
    INDICATOR_SPIRITUAL("Spiritual", "आध्यात्मिक"),
    INDICATOR_RELATIONSHIPS("Relationships", "सम्बन्धहरू"),
    
    YOGAS_TITLE("Karakenshi Yogas", "कारकांश योगहरू"),
    YOGAS_SUBTITLE("Special combinations formed by Chara Karakas", "चर कारकहरूद्वारा निर्मित विशेष संयोजनहरू"),
    NO_YOGAS_FOUND("No special Karakenshi Yogas detected", "कुनै विशेष कारकांश योग फेला परेन"),
    
    STRENGTH_LABEL("Strength: %s%%", "बल: %s%%"),
    YOGA_BENEFIC("Benefic", "शुभ"),
    YOGA_CHALLENGING("Challenging", "चुनौतीपूर्ण"),
    PLANETS_LABEL("Planets: ", "ग्रहहरू: "),
    RESULTS_LABEL("Results:", "नतिजाहरू:"),
    
    INTERP_TITLE("Complete Interpretation", "पूर्ण व्याख्या"),
    INTERP_SUBTITLE("Comprehensive Jaimini analysis summary", "विस्तृत जैमिनी विश्लेषण सारांश"),
    
    ATMAKARAKA_ANALYSIS_TITLE("🌟 Atmakaraka Analysis", "🌟 आत्मकारक विश्लेषण"),
    AK_SOUL_PLANET_LABEL("Your Soul Planet (Atmakaraka)", "तपाईंको आत्म ग्रह (आत्मकारक)"),
    AK_DESC("The Atmakaraka is the planet with the highest degree in your chart. It represents your soul's deepest desires and the lessons you need to learn in this lifetime.", "आत्मकारक तपाईंको कुण्डलीमा सबैभन्दा उच्च अंश भएको ग्रह हो। यसले तपाईंको आत्माको गहिरो इच्छाहरू र यस जीवनमा तपाईंले सिक्नुपर्ने पाठहरू प्रतिनिधित्व गर्दछ।"),
    
    GEMSTONE_REC_TITLE("Gemstone Recommendations", "रत्न सिफारिसहरू"),
    
    LOADING_JAIMINI("Calculating Jaimini Karakas...", "जैमिनी कारकहरू गणना गर्दै..."),
    ERROR_JAIMINI("Error calculating Jaimini Karakas", "जैमिनी कारकहरू गणना गर्नमा त्रुटि"),
    NO_CHART_AVAILABLE("No chart available", "कुनै कुण्डली उपलब्ध छैन"),
}
