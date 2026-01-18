package com.astro.storm.core.common

/**
 * Expanded Yoga String Keys
 * Part 3 of split enums for string resources
 */
enum class StringKeyYogaExpanded(override val en: String, override val ne: String) : StringKeyInterface {
    // ============================================
    // BHAVA YOGA TITLES
    // ============================================
    YOGA_BHAVA_TITLE("Bhava Yoga", "भाव योग"),
    YOGA_BHAVA_LORD_PLACEMENT("{0} in {1}", "{1}मा {0}"),
    
    // Lord Names
    LORD_1("Lagna Lord", "लग्नेश"),
    LORD_2("2nd Lord", "द्वितीयेश"),
    LORD_3("3rd Lord", "तृतीयेश"),
    LORD_4("4th Lord", "चतुर्थेश"),
    LORD_5("5th Lord", "पंचमेश"),
    LORD_6("6th Lord", "षष्ठेश"),
    LORD_7("7th Lord", "सप्तमेश"),
    LORD_8("8th Lord", "अष्टमेश"),
    LORD_9("9th Lord", "नवमेश"),
    LORD_10("10th Lord", "दशमेश"),
    LORD_11("11th Lord", "एकादशेश"),
    LORD_12("12th Lord", "द्वादशेश"),

    // Generic Effects (Fallbacks)
    EFFECT_GENERIC_GOOD("Favorable placement bringing positive results related to {0} and {1}.", "{0} र {1} सँग सम्बन्धित सकारात्मक परिणाम ल्याउने अनुकूल स्थिति।"),
    EFFECT_GENERIC_BAD("Challenging placement causing difficulties related to {0} and {1}.", "{0} र {1} सँग सम्बन्धित कठिनाइहरू निम्त्याउने चुनौतीपूर्ण स्थिति।"),
    EFFECT_GENERIC_MIXED("Mixed results giving both success and challenges in {0}.", "{0} मा सफलता र चुनौती दुवै दिने मिश्रित परिणाम।"),

    // Specific Bhava Yoga Effects (Sample - mainly 1st Lord)
    EFFECT_LORD_1_IN_1("Strong health, vitality, leadership ability, self-made success.", "बलियो स्वास्थ्य, जीवनशक्ति, नेतृत्व क्षमता, स्व-निर्मित सफलता।"),
    EFFECT_LORD_1_IN_2("Wealthy, good speaker, family-oriented, foresight.", "धनी, राम्रो वक्ता, परिवार-उन्मुख, दूरदर्शिता।"),
    EFFECT_LORD_1_IN_3("Courageous, wealthy, honorable, intelligent, happy with siblings.", "साहसी, धनी, सम्माननीय, बुद्धिमान, दाजुभाइसँग खुसी।"),
    EFFECT_LORD_1_IN_4("Parental happiness, many vehicles, landed property, domestic peace.", "आमाबुबाको सुख, धेरै सवारी साधन, जग्गा जमिन, घरेलु शान्ति।"),
    EFFECT_LORD_1_IN_5("Intelligent, famous, happiness from children, authority.", "बुद्धिमान, प्रसिद्ध, सन्तानसुख, अधिकार।"),
    EFFECT_LORD_1_IN_6("Debts, enemies, health issues, struggle for success.", "ऋण, शत्रु, स्वास्थ्य समस्या, सफलताको लागि संघर्ष।"),
    EFFECT_LORD_1_IN_7("Traveler, success in partnership/business, maybe somewhat wandering nature.", "यात्री, साझेदारी/व्यापारमा सफलता, केही हदसम्म घुमन्ते स्वभाव।"),
    EFFECT_LORD_1_IN_8("Sudden changes, obstacles, occult interests, health fluctuations.", "अचानक परिवर्तन, अवरोध, गुप्त रुचि, स्वास्थ्य उतार-चढाव।"),
    EFFECT_LORD_1_IN_9("Fortunate, popular, religious, patrimony, good character.", "भाग्यशाली, लोकप्रिय, धार्मिक, पैतृक सम्पत्ति, राम्रो चरित्र।"),
    EFFECT_LORD_1_IN_10("Career success, professional honor, recognition, paternal happiness.", "करियर सफलता, व्यावसायिक सम्मान, मान्यता, पैतृक सुख।"),
    EFFECT_LORD_1_IN_11("Gains, profits, elder sibling happiness, fulfillment of desires.", "लाभ, नाफा, दाजुभाइको सुख, इच्छाहरूको पूर्ति।"),
    EFFECT_LORD_1_IN_12("Expenses, foreign travel, spiritual inclination, maybe losses.", "खर्च, विदेश यात्रा, आध्यात्मिक झुकाव, सायद नोक्सान।"),

    // ============================================
    // CONJUNCTION YOGAS
    // ============================================
    YOGA_CONJUNCTION_TITLE("Conjunction Yoga", "युति योग"),
    YOGA_CONJUNCTION_2_PLANETS("Conjunction of {0} and {1}", "{0} र {1} को युति"),
    YOGA_CONJUNCTION_3_PLANETS("Conjunction of {0}, {1} and {2}", "{0}, {1} र {2} को युति"),
    YOGA_CONJUNCTION_4_PLANETS("Conjunction of {0}, {1}, {2} and {3}", "{0}, {1}, {2} र {3} को युति"),

    // Specific Conjunction Effects
    EFFECT_SUN_MOON("Skillful in work, wealthy but fluctuating fortune.", "काममा दक्ष, धनी तर उतार-चढावपूर्ण भाग्य।"),
    EFFECT_SUN_MARS("Energetic, brave, aggressive, maybe health issues.", "ऊर्जावान, बहादुर, आक्रामक, स्वास्थ्य समस्या हुन सक्छ।"),
    EFFECT_SUN_MERCURY("Budhaditya Yoga: Intelligent, learned, famous, good speaker.", "बुधादित्य योग: बुद्धिमान, विद्वान, प्रसिद्ध, राम्रो वक्ता।"),
    EFFECT_SUN_JUPITER("Spiritual, wealthy, respected, advisor, teacher.", "आध्यात्मिक, धनी, सम्मानित, सल्लाहकार, शिक्षक।"),
    EFFECT_SUN_VENUS("Artistic, comfort-loving, maybe eye trouble.", "कलात्मक, सुख-प्रेमी, आँखाको समस्या हुन सक्छ।"),
    EFFECT_SUN_SATURN("Hardworking, struggles with authority, disciplined.", "मेहेनती, अधिकारसँग संघर्ष, अनुशासित।"),
    
    EFFECT_MOON_MARS("Chandra-Mangala Yoga: Wealthy, enterprising, maybe mother's health issues.", "चन्द्र-मंगल योग: धनी, उद्यमशील, आमाको स्वास्थ्य समस्या हुन सक्छ।"),
    EFFECT_MOON_MERCURY("Learned, pleasant speech, clever, artistic.", "विद्वान, सुखद बोली, चलाख, कलात्मक।"),
    EFFECT_MOON_JUPITER("Gajakesari Yoga: Famous, virtuous, wealthy, leader.", "गजकेसरी योग: प्रसिद्ध, सद्गुणी, धनी, नेता।"),
    EFFECT_MOON_VENUS("Soft nature, artistic, beautiful, wealthy.", "कोमल स्वभाव, कलात्मक, सुन्दर, धनी।"),
    EFFECT_MOON_SATURN("Serious, detached, hardworking, maybe depressive.", "गम्भीर, विरक्त, मेहेनती, उदास हुन सक्छ।"),

    EFFECT_MARS_MERCURY("Technical skill, quick wit, maybe argumentative.", "प्राविधिक कौशल, छिटो बुद्धि, तर्क वितर्क गर्ने।"),
    EFFECT_MARS_JUPITER("Guru-Mangala Yoga: Leader, energetic, righteous, wealthy.", "गुरु-मंगल योग: नेता, ऊर्जावान, धार्मिक, धनी।"),
    EFFECT_MARS_VENUS("Passionate, artistic, charming, relationship focused.", "भावुक, कलात्मक, आकर्षक, सम्बन्ध केन्द्रित।"),
    EFFECT_MARS_SATURN("Disciplined energy, technical ability, struggles.", "अनुशासित ऊर्जा, प्राविधिक क्षमता, संघर्ष।"),

    EFFECT_MERCURY_JUPITER("Learned, eloquent, writer, advisor.", "विद्वान, वाक्पटु, लेखक, सल्लाहकार।"),
    EFFECT_MERCURY_VENUS("Raja Yoga related to arts/commerce, jovial, eloquent.", "कला/वाणिज्यसँग सम्बन्धित राज योग, हँसिलो, वाक्पटु।"),
    EFFECT_MERCURY_SATURN("Analytical, profound thinker, steady progress.", "विश्लेषणात्मक, गहिरो चिन्तक, स्थिर प्रगति।"),

    EFFECT_JUPITER_VENUS("Generous, wealthy, learned, maybe conflicts in philosophy.", "उदार, धनी, विद्वान, दर्शनमा द्वन्द्व हुन सक्छ।"),
    EFFECT_JUPITER_SATURN("Dharma-Karma Adhipati (potentially), responsible, philosophical.", "धर्म-कर्माधिपति (सम्भावित), जिम्मेवार, दार्शनिक।"),
    EFFECT_VENUS_SATURN("Artist, loyal, steady wealth, maybe late marriage.", "कलाकार, वफादार, स्थिर धन, ढिलो विवाह हुन सक्छ।"),

    // ============================================
    // NEW RAJA YOGAS
    // ============================================
    YOGA_BHERI("Bheri Yoga", "भेरी योग"),
    EFFECT_BHERI("Wealthy, good health, happy family, religious.", "धनी, राम्रो स्वास्थ्य, सुखी परिवार, धार्मिक।"),
    
    YOGA_MRIDANGA("Mridanga Yoga", "मृदंग योग"),
    EFFECT_MRIDANGA("Respected, famous, attractive, influential.", "सम्मानित, प्रसिद्ध, आकर्षक, प्रभावशाली।"),
    
    YOGA_SREENATHA("Sreenatha Yoga", "श्रीनाथ योग"),
    EFFECT_SREENATHA("Wealthy, lordly, religious, eloquent.", "धनी, प्रभुत्वशाली, धार्मिक, वाक्पटु।"),
    
    YOGA_MATSYA("Matsya Yoga", "मत्स्य योग"),
    EFFECT_MATSYA("Kind, religious, learned, strong character.", "दयालु, धार्मिक, विद्वान, बलियो चरित्र।"),
    
    YOGA_KURMA("Kurma Yoga", "कूर्म योग"),
    EFFECT_KURMA("Stable wealth, happy, grateful, helpful.", "स्थिर धन, खुसी, कृतज्ञ, सहयोगी।"),
    
    YOGA_KHADGA("Khadga Yoga", "खड्ग योग"),
    EFFECT_KHADGA("Learned, skilled, wealthy, successful.", "विद्वान, दक्ष, धनी, सफल।"),
    
    YOGA_KUSUMA_EXP("Kusuma Yoga", "कुसुम योग"),
    EFFECT_KUSUMA_EXP("Charitable, protector, head of town/group.", "दानशील, रक्षक, गाउँ/समूहको प्रमुख।"),
    
    YOGA_KALANIDHI("Kalanidhi Yoga", "कलानिधि योग"),
    EFFECT_KALANIDHI("Respected, healthy, wealthy, learned.", "सम्मानित, स्वस्थ, धनी, विद्वान।"),
    
    YOGA_TRIMURTI("Trimurti Yoga", "त्रिमूर्ति योग"),
    EFFECT_TRIMURTI("Happy, wealthy, long life, famous.", "खुसी, धनी, दीर्घायु, प्रसिद्ध।"),
    
    YOGA_HARIHARA_BRAHMA("Harihara Brahma Yoga", "हरिहर ब्रह्म योग"),
    EFFECT_HARIHARA_BRAHMA("Scholar, truthful, happy, helpful.", "विद्वान, सत्यवादी, खुसी, सहयोगी।"),

    YOGA_GOURI("Gouri Yoga", "गौरी योग"),
    EFFECT_GOURI("Beautiful, religious, wealthy, famous, blessed with a good family.", "सुन्दर, धार्मिक, धनी, प्रसिद्ध, राम्रो परिवारको साथ आशिष्।"),
    
    YOGA_BHARATHI("Bharathi Yoga", "भारती योग"),
    EFFECT_BHARATHI("Exceptional intelligence, scholarship, fame, wealth, religious nature.", "असाधारण बुद्धि, विद्वता, प्रसिद्धि, धन, धार्मिक स्वभाव।"),

    // Categories
    YOGA_CAT_BHAVA("Bhava Yoga", "भाव योग"),
    YOGA_CAT_BHAVA_DESC("House Lord Placements", "भाव अधिपति स्थितिहरू"),
    YOGA_CAT_CONJUNCTION("Conjunction Yoga", "युति योग"),
    YOGA_CAT_CONJUNCTION_DESC("Planetary Conjunctions", "ग्रह युतिहरू");
}
