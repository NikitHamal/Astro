package com.astro.storm.core.common

/**
 * Deep Analysis Localization Keys - Relationship & Marriage
 * 400+ localization keys for comprehensive relationship analysis
 */
enum class StringKeyDeepRelationship(override val en: String, override val ne: String, override val hi: String) : StringKeyInterface {
    // Section Headers
    SECTION_RELATIONSHIP("Relationship Analysis", "सम्बन्ध विश्लेषण", "संबंध विश्लेषण"),
    SECTION_MARRIAGE("Marriage Analysis", "विवाह विश्लेषण", "विवाह विश्लेषण"),
    SECTION_7TH_HOUSE("7th House Analysis", "सप्तम भाव विश्लेषण", "सप्तम भाव विश्लेषण"),
    SECTION_VENUS("Venus Analysis", "शुक्र विश्लेषण", "शुक्र विश्लेषण"),
    SECTION_JUPITER("Jupiter for Marriage", "विवाहको लागि बृहस्पति", "विवाहको लिए बृहस्पति"),
    SECTION_PARTNER_PROFILE("Partner Profile", "साथी प्रोफाइल", "मित्र प्रोफाइल"),
    SECTION_NAVAMSHA("Navamsha (D9) Analysis", "नवांश (D9) विश्लेषण", "नवांश (D9) विश्लेषण"),
    SECTION_MARRIAGE_TIMING("Marriage Timing", "विवाह समय", "विवाह समय"),
    
    // 7th House
    SEVENTH_HOUSE_SIGN("7th House Sign", "सप्तम भाव राशि", "सप्तम भाव राशि"),
    SEVENTH_HOUSE_LORD("7th House Lord", "सप्तम भाव स्वामी", "सप्तम भाव स्वामी"),
    SEVENTH_HOUSE_PLANETS("Planets in 7th House", "सप्तम भावमा ग्रहहरू", "सप्तम भावमा ग्रह"),
    SEVENTH_HOUSE_ASPECTS("Aspects to 7th House", "सप्तम भावमा दृष्टिहरू", "सप्तम भावमा दृष्टि"),
    SEVENTH_HOUSE_STRENGTH("7th House Strength", "सप्तम भाव बल", "सप्तम भाव बल"),
    
    // Partnership Environment
    PARTNERSHIP_ENVIRONMENT("Partnership Environment", "साझेदारी वातावरण", "साझेदारी वातावरण"),
    PUBLIC_DEALINGS("Public Dealings", "सार्वजनिक व्यवहार", "सार्वजनिक व्यवहार"),
    
    // 7th Lord Placement
    LORD7_IN_1ST("7th Lord in 1st House", "सप्तम स्वामी पहिलो भावमा", "सप्तम स्वामी पहिलो भावमा"),
    LORD7_IN_2ND("7th Lord in 2nd House", "सप्तम स्वामी दोस्रो भावमा", "सप्तम स्वामी दोस्रो भावमा"),
    LORD7_IN_3RD("7th Lord in 3rd House", "सप्तम स्वामी तेस्रो भावमा", "सप्तम स्वामी तेस्रो भावमा"),
    LORD7_IN_4TH("7th Lord in 4th House", "सप्तम स्वामी चौथो भावमा", "सप्तम स्वामी चौथो भावमा"),
    LORD7_IN_5TH("7th Lord in 5th House", "सप्तम स्वामी पाँचौं भावमा", "सप्तम स्वामी पाँचौं भावमा"),
    LORD7_IN_6TH("7th Lord in 6th House", "सप्तम स्वामी छैटौं भावमा", "सप्तम स्वामी छैटौं भावमा"),
    LORD7_IN_7TH("7th Lord in 7th House", "सप्तम स्वामी सातौं भावमा", "सप्तम स्वामी सातौं भावमा"),
    LORD7_IN_8TH("7th Lord in 8th House", "सप्तम स्वामी आठौं भावमा", "सप्तम स्वामी आठौं भावमा"),
    LORD7_IN_9TH("7th Lord in 9th House", "सप्तम स्वामी नवौं भावमा", "सप्तम स्वामी नवौं भावमा"),
    LORD7_IN_10TH("7th Lord in 10th House", "सप्तम स्वामी दशौं भावमा", "सप्तम स्वामी दशौं भावमा"),
    LORD7_IN_11TH("7th Lord in 11th House", "सप्तम स्वामी एघारौं भावमा", "सप्तम स्वामी एघारौं भावमा"),
    LORD7_IN_12TH("7th Lord in 12th House", "सप्तम स्वामी बाह्रौं भावमा", "सप्तम स्वामी बाह्रौं भावमा"),
    
    // Venus Analysis
    VENUS_SIGN("Venus Sign", "शुक्र राशि", "शुक्र राशि"),
    VENUS_HOUSE("Venus House", "शुक्र भाव", "शुक्र भाव"),
    VENUS_NAKSHATRA("Venus Nakshatra", "शुक्र नक्षत्र", "शुक्र नक्षत्र"),
    VENUS_DIGNITY("Venus Dignity", "शुक्र गौरव", "शुक्र गौरव"),
    ROMANTIC_NATURE("Romantic Nature", "रोमान्टिक स्वभाव", "रोमान्टिक स्वभाव"),
    ATTRACTION_STYLE("Attraction Style", "आकर्षण शैली", "आकर्षण शैली"),
    PLEASURE_SEEKING("Pleasure Seeking", "आनन्द खोज", "आनन्द खोज"),
    LOVE_EXPRESSION("Love Expression", "प्रेम अभिव्यक्ति", "प्रेम अभिव्यक्ति"),
    
    // Jupiter Analysis
    JUPITER_SIGN("Jupiter Sign", "बृहस्पति राशि", "बृहस्पति राशि"),
    JUPITER_HOUSE("Jupiter House", "बृहस्पति भाव", "बृहस्पति भाव"),
    HUSBAND_INDICATOR("Husband Indicator", "पति सूचक", "पति सूचक"),
    MARRIAGE_BLESSINGS("Marriage Blessings", "विवाह आशीर्वाद", "विवाह आशीर्वाद"),
    
    // Partner Profile
    PARTNER_PHYSICAL("Partner Physical Description", "साथी शारीरिक विवरण", "मित्र शारीरिक विवरण"),
    PARTNER_PERSONALITY("Partner Personality Traits", "साथी व्यक्तित्व गुणहरू", "मित्र व्यक्तित्व गुणहरू"),
    PARTNER_PROFESSION("Partner Profession Indicators", "साथी पेशा सूचकहरू", "मित्र पेशा सूचकहरू"),
    PARTNER_BACKGROUND("Partner Background", "साथी पृष्ठभूमि", "मित्र पृष्ठभूमि"),
    MEETING_CIRCUMSTANCES("Meeting Circumstances", "भेट परिस्थितिहरू", "भेट परिस्थितिहरू"),
    MEETING_DIRECTION("Direction of Meeting", "भेटको दिशा", "भेटको दिशा"),
    AGE_RELATION("Partner Age Relation", "साथी उमेर सम्बन्ध", "मित्र उमेर संबंध"),
    
    // Relationship Style
    RELATIONSHIP_STYLE("Relationship Style", "सम्बन्ध शैली", "संबंध शैली"),
    ATTACHMENT_STYLE("Attachment Style", "आसक्ति शैली", "आसक्ति शैली"),
    COMMUNICATION_REL("Communication in Relationship", "सम्बन्धमा सञ्चार", "संबंधमा सञ्चार"),
    CONFLICT_RESOLUTION("Conflict Resolution", "द्वन्द्व समाधान", "द्वन्द्व समाधान"),
    INTIMACY_APPROACH("Intimacy Approach", "घनिष्टता दृष्टिकोण", "घनिष्टता दृष्टिकोण"),
    LOYALTY_PATTERN("Loyalty Pattern", "वफादारी ढाँचा", "वफादारी ढाँचा"),
    
    // Marriage Yogas
    MARRIAGE_YOGA_TITLE("Marriage Yogas", "विवाह योगहरू", "विवाह योग"),
    YOGA_MARRIAGE_EFFECT("Yoga Effect on Marriage", "विवाहमा योग प्रभाव", "विवाहमा योग प्रभाव"),
    FAVORABLE_YOGA("Favorable Marriage Yoga", "अनुकूल विवाह योग", "अनुकूल विवाह योग"),
    
    // Relationship Challenges
    REL_CHALLENGES_TITLE("Relationship Challenges", "सम्बन्ध चुनौतीहरू", "संबंध चुनौतियां"),
    CHALLENGE_REMEDIES("Challenge Remedies", "चुनौती उपचारहरू", "चुनौती उपचारहरू"),
    
    // Navamsha D9
    D9_ASCENDANT("D9 Ascendant", "D9 लग्न", "D9 लग्न"),
    D9_VENUS("D9 Venus Position", "D9 शुक्र स्थिति", "D9 शुक्र स्थिति"),
    D9_7TH_LORD("D9 7th Lord Position", "D9 सप्तम स्वामी स्थिति", "D9 सप्तम स्वामी स्थिति"),
    D9_MARRIAGE_QUALITY("Marriage Quality from D9", "D9 बाट विवाह गुणस्तर", "D9 बाट विवाह गुणस्तर"),
    D9_SPOUSE_NATURE("Spouse Nature from D9", "D9 बाट जीवनसाथी स्वभाव", "D9 बाट जीवनमित्र स्वभाव"),
    
    // Marriage Timing
    TIMING_CATEGORY("Timing Category", "समय श्रेणी", "समय श्रेणी"),
    TIMING_EARLY("Early Marriage", "प्रारम्भिक विवाह", "प्रारम्भिक विवाह"),
    TIMING_NORMAL("Normal Marriage Age", "सामान्य विवाह उमेर", "सामान्य विवाह उमेर"),
    TIMING_DELAYED("Delayed Marriage", "ढिलो विवाह", "ढिलो विवाह"),
    TIMING_DENIED("Marriage Challenges", "विवाह चुनौतीहरू", "विवाह चुनौतियां"),
    ESTIMATED_AGE("Estimated Age Range", "अनुमानित उमेर दायरा", "अनुमानित उमेर दायरा"),
    FAVORABLE_PERIODS("Favorable Marriage Periods", "अनुकूल विवाह अवधिहरू", "अनुकूल विवाह अवधिहरू"),
    CHALLENGING_PERIODS("Challenging Marriage Periods", "चुनौतीपूर्ण विवाह अवधिहरू", "चुनौतीपूर्ण विवाह अवधिहरू"),
    
    // Marriage Quality
    MARRIAGE_QUALITY("Marriage Quality", "विवाह गुणस्तर", "विवाह गुणस्तर"),
    HARMONY_INDICATORS("Harmony Indicators", "सामंजस्य सूचकहरू", "सामंजस्य सूचकहरू"),
    CHALLENGE_INDICATORS("Challenge Indicators", "चुनौती सूचकहरू", "चुनौती सूचकहरू"),
    GROWTH_POTENTIAL("Growth Potential", "विकास क्षमता", "विकास क्षमता"),
    LONGEVITY_INDICATOR("Marriage Longevity", "विवाह दीर्घायु", "विवाह दीर्घायु"),
    
    // Relationship Timeline
    REL_TIMELINE_TITLE("Relationship Timeline", "सम्बन्ध समयरेखा", "संबंध समयरेखा"),
    CURRENT_REL_PHASE("Current Relationship Phase", "वर्तमान सम्बन्ध चरण", "वर्तमान संबंध चरण"),
    DASHA_REL_FOCUS("Dasha Period Relationship Focus", "दशा अवधि सम्बन्ध फोकस", "दशा अवधि संबंध फोकस"),
    
    // Summary
    REL_SUMMARY("Relationship Summary", "सम्बन्ध सारांश", "संबंध सारांश"),
    REL_SCORE("Relationship Strength Score", "सम्बन्ध शक्ति स्कोर", "संबंध शक्ति स्कोर"),
    
    // Advice
    REL_ADVICE("Relationship Advice", "सम्बन्ध सल्लाह", "संबंध सल्लाह"),
}
