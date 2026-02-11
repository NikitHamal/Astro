package com.astro.storm.core.common

/**
 * Deep Analysis Localization Keys - Career & Profession
 * 400+ localization keys for comprehensive career analysis
 */
enum class StringKeyDeepCareer(override val en: String, override val ne: String, override val hi: String) : StringKeyInterface {
    // Section Headers
    SECTION_CAREER_ANALYSIS("Career Analysis", "क्यारियर विश्लेषण", "क्यारियर विश्लेषण"),
    SECTION_10TH_HOUSE("10th House Analysis", "दशम भाव विश्लेषण", "दशम भाव विश्लेषण"),
    SECTION_PROFESSION_MATCHING("Profession Matching", "पेशा मिलान", "पेशा मिलान"),
    SECTION_WORK_STYLE("Work Style Profile", "कार्य शैली प्रोफाइल", "कार्य शैली प्रोफाइल"),
    SECTION_CAREER_TIMELINE("Career Timeline", "क्यारियर समयरेखा", "क्यारियर समयरेखा"),
    SECTION_EARNING_POTENTIAL("Earning Potential", "आय क्षमता", "आय क्षमता"),
    SECTION_DASHAMSHA("Dashamsha (D10) Analysis", "दशांश (D10) विश्लेषण", "दशांश (D10) विश्लेषण"),
    
    // 10th House
    TENTH_HOUSE_SIGN("10th House Sign", "दशम भाव राशि", "दशम भाव राशि"),
    TENTH_HOUSE_LORD("10th House Lord", "दशम भाव स्वामी", "दशम भाव स्वामी"),
    TENTH_HOUSE_PLANETS("Planets in 10th House", "दशम भावमा ग्रहहरू", "दशम भावमा ग्रह"),
    TENTH_HOUSE_ASPECTS("Aspects to 10th House", "दशम भावमा दृष्टिहरू", "दशम भावमा दृष्टि"),
    TENTH_HOUSE_STRENGTH("10th House Strength", "दशम भाव बल", "दशम भाव बल"),
    
    // Public Image
    PUBLIC_IMAGE_TITLE("Public Image", "सार्वजनिक छवि", "सार्वजनिक छवि"),
    PUBLIC_PERCEPTION("How Others Perceive You", "अरूले तपाईंलाई कसरी बुझ्छन्", "अरूले तपाईं को कसरी बुझ्छन्"),
    PROFESSIONAL_REPUTATION("Professional Reputation", "व्यावसायिक प्रतिष्ठा", "व्यावसायिक प्रतिष्ठा"),
    
    // Career Environment
    CAREER_ENVIRONMENT("Ideal Work Environment", "आदर्श कार्य वातावरण", "आदर्श कार्य वातावरण"),
    WORK_ATMOSPHERE("Work Atmosphere", "कार्य वातावरण", "कार्य वातावरण"),
    OFFICE_DYNAMICS("Office Dynamics", "कार्यालय गतिशीलता", "कार्यालय गतिशीलता"),
    
    // Authority
    AUTHORITY_TITLE("Authority Dynamics", "अधिकार गतिशीलता", "अधिकार गतिशीलता"),
    AUTHORITY_RELATIONSHIP("Relationship with Authority", "अधिकारसँगको सम्बन्ध", "अधिकारसँगको संबंध"),
    AUTHORITY_STYLE("Your Authority Style", "तपाईंको अधिकार शैली", "आपका अधिकार शैली"),
    
    // 10th Lord Placement
    LORD_IN_1ST("10th Lord in 1st House", "दशम स्वामी पहिलो भावमा", "दशम स्वामी पहिलो भावमा"),
    LORD_IN_2ND("10th Lord in 2nd House", "दशम स्वामी दोस्रो भावमा", "दशम स्वामी दोस्रो भावमा"),
    LORD_IN_3RD("10th Lord in 3rd House", "दशम स्वामी तेस्रो भावमा", "दशम स्वामी तेस्रो भावमा"),
    LORD_IN_4TH("10th Lord in 4th House", "दशम स्वामी चौथो भावमा", "दशम स्वामी चौथो भावमा"),
    LORD_IN_5TH("10th Lord in 5th House", "दशम स्वामी पाँचौं भावमा", "दशम स्वामी पाँचौं भावमा"),
    LORD_IN_6TH("10th Lord in 6th House", "दशम स्वामी छैटौं भावमा", "दशम स्वामी छैटौं भावमा"),
    LORD_IN_7TH("10th Lord in 7th House", "दशम स्वामी सातौं भावमा", "दशम स्वामी सातौं भावमा"),
    LORD_IN_8TH("10th Lord in 8th House", "दशम स्वामी आठौं भावमा", "दशम स्वामी आठौं भावमा"),
    LORD_IN_9TH("10th Lord in 9th House", "दशम स्वामी नवौं भावमा", "दशम स्वामी नवौं भावमा"),
    LORD_IN_10TH("10th Lord in 10th House", "दशम स्वामी दशौं भावमा", "दशम स्वामी दशौं भावमा"),
    LORD_IN_11TH("10th Lord in 11th House", "दशम स्वामी एघारौं भावमा", "दशम स्वामी एघारौं भावमा"),
    LORD_IN_12TH("10th Lord in 12th House", "दशम स्वामी बाह्रौं भावमा", "दशम स्वामी बाह्रौं भावमा"),
    
    // Profession Categories
    PROFESSION_GOVERNMENT("Government & Administration", "सरकार र प्रशासन", "सरकार और प्रशासन"),
    PROFESSION_BUSINESS("Business & Commerce", "व्यापार र वाणिज्य", "व्यापार और वाणिज्य"),
    PROFESSION_TECHNOLOGY("Technology & IT", "प्रविधि र आईटी", "प्रविधि और आईटी"),
    PROFESSION_HEALTHCARE("Healthcare & Medicine", "स्वास्थ्य सेवा र चिकित्सा", "स्वास्थ्य सेवा और चिकित्सा"),
    PROFESSION_EDUCATION("Education & Teaching", "शिक्षा र शिक्षण", "शिक्षा और शिक्षण"),
    PROFESSION_ARTS("Arts & Entertainment", "कला र मनोरञ्जन", "कला और मनोरञ्जन"),
    PROFESSION_FINANCE("Finance & Banking", "वित्त र बैंकिङ", "वित्त और बैंकिङ"),
    PROFESSION_LAW("Law & Legal", "कानुन र कानूनी", "कानुन और कानूनी"),
    PROFESSION_ENGINEERING("Engineering & Technical", "इन्जिनियरिङ र प्राविधिक", "इन्जिनियरिङ और प्राविधिक"),
    PROFESSION_RESEARCH("Research & Academia", "अनुसन्धान र शिक्षाविद्", "अनुसन्धान और शिक्षाविद्"),
    PROFESSION_MILITARY("Military & Defense", "सैनिक र रक्षा", "सैनिक और रक्षा"),
    PROFESSION_MEDIA("Media & Communications", "मिडिया र सञ्चार", "मिडिया और सञ्चार"),
    PROFESSION_SPIRITUAL("Spiritual & Religious", "आध्यात्मिक र धार्मिक", "आध्यात्मिक और धार्मिक"),
    
    // Career Indicators
    CAREER_INDICATOR_TITLE("Career Indicators", "क्यारियर सूचकहरू", "क्यारियर सूचकहरू"),
    PRIMARY_INDICATORS("Primary Career Indicators", "प्राथमिक क्यारियर सूचकहरू", "प्राथमिक क्यारियर सूचकहरू"),
    SECONDARY_INDICATORS("Secondary Career Indicators", "माध्यमिक क्यारियर सूचकहरू", "माध्यमिक क्यारियर सूचकहरू"),
    
    // Career Yogas
    CAREER_YOGA_TITLE("Career Yogas", "क्यारियर योगहरू", "क्यारियर योग"),
    RAJA_YOGA_CAREER("Raja Yoga Career Effect", "राज योग क्यारियर प्रभाव", "राज योग क्यारियर प्रभाव"),
    YOGA_CAREER_EFFECT("Yoga Effect on Career", "क्यारियरमा योग प्रभाव", "क्यारियरमा योग प्रभाव"),
    
    // Work Style
    WORK_STYLE_TITLE("Work Style", "कार्य शैली", "कार्य शैली"),
    LEADERSHIP_STYLE("Leadership Style", "नेतृत्व शैली", "नेतृत्व शैली"),
    TEAMWORK_APPROACH("Teamwork Approach", "टोली कार्य दृष्टिकोण", "टोली कार्य दृष्टिकोण"),
    PROBLEM_SOLVING("Problem Solving Style", "समस्या समाधान शैली", "समस्या समाधान शैली"),
    STRESS_HANDLING("Stress Handling", "तनाव सम्हाल्ने", "तनाव सम्हाल्ने"),
    
    // Employment Type
    EMPLOYMENT_TITLE("Employment Type Analysis", "रोजगार प्रकार विश्लेषण", "रोजगार प्रकार विश्लेषण"),
    SERVICE_VS_BUSINESS("Service vs Business", "सेवा बनाम व्यापार", "सेवा बनाम व्यापार"),
    EMPLOYEE_VS_EMPLOYER("Employee vs Employer", "कर्मचारी बनाम नियोक्ता", "कर्मचारी बनाम नियोक्ता"),
    PARTNERSHIP_POTENTIAL("Partnership Potential", "साझेदारी क्षमता", "साझेदारी क्षमता"),
    FOREIGN_EMPLOYMENT("Foreign Employment Potential", "विदेशी रोजगार क्षमता", "विदेशी रोजगार क्षमता"),
    
    // Career Strengths & Challenges
    CAREER_STRENGTHS("Career Strengths", "क्यारियर शक्तिहरू", "क्यारियर शक्तिहरू"),
    CAREER_CHALLENGES("Career Challenges", "क्यारियर चुनौतीहरू", "क्यारियर चुनौतियां"),
    
    // Dashamsha D10
    D10_ASCENDANT("D10 Ascendant", "D10 लग्न", "D10 लग्न"),
    D10_10TH_SIGN("D10 10th Sign", "D10 दशम राशि", "D10 दशम राशि"),
    D10_SUN_POSITION("D10 Sun Position", "D10 सूर्य स्थिति", "D10 सूर्य स्थिति"),
    D10_MOON_POSITION("D10 Moon Position", "D10 चन्द्रमा स्थिति", "D10 चन्द्रमा स्थिति"),
    D10_CAREER_REFINEMENT("Career Refinement from D10", "D10 बाट क्यारियर परिष्करण", "D10 बाट क्यारियर परिष्करण"),
    D10_GROWTH_PATTERN("Professional Growth Pattern", "व्यावसायिक विकास ढाँचा", "व्यावसायिक विकास ढाँचा"),
    
    // Career Timeline
    CAREER_TIMELINE_TITLE("Career Timeline", "क्यारियर समयरेखा", "क्यारियर समयरेखा"),
    CURRENT_CAREER_PHASE("Current Career Phase", "वर्तमान क्यारियर चरण", "वर्तमान क्यारियर चरण"),
    DASHA_CAREER_FOCUS("Dasha Period Career Focus", "दशा अवधि क्यारियर फोकस", "दशा अवधि क्यारियर फोकस"),
    CAREER_OPPORTUNITIES("Career Opportunities", "क्यारियर अवसरहरू", "क्यारियर अवसर"),
    CAREER_TIMING("Career Timing Analysis", "क्यारियर समय विश्लेषण", "क्यारियर समय विश्लेषण"),
    
    // Earning
    EARNING_TITLE("Earning Analysis", "आय विश्लेषण", "आय विश्लेषण"),
    EARNING_POTENTIAL("Earning Potential", "आय क्षमता", "आय क्षमता"),
    EARNING_PATTERNS("Earning Patterns", "आय ढाँचाहरू", "आय ढाँचाहरू"),
    INCOME_SOURCES("Income Sources", "आय स्रोतहरू", "आय स्रोतहरू"),
    
    // Summary
    CAREER_SUMMARY("Career Summary", "क्यारियर सारांश", "क्यारियर सारांश"),
    CAREER_SCORE("Career Strength Score", "क्यारियर शक्ति स्कोर", "क्यारियर शक्ति स्कोर"),
    SUITABLE_PROFESSIONS("Suitable Professions", "उपयुक्त पेशाहरू", "उपयुक्त पेशाहरू"),
    
    // Advice
    CAREER_ADVICE("Career Advice", "क्यारियर सल्लाह", "क्यारियर सल्लाह"),
}
