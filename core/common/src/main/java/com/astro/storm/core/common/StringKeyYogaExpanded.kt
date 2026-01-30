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

    // ============================================
    // EXTENDED RAJA YOGAS
    // ============================================
    YOGA_SIMHASANA("Simhasana Yoga", "सिंहासन योग"),
    EFFECT_SIMHASANA("Royal authority, leadership, sits on a throne-like position.", "राजकीय अधिकार, नेतृत्व, सिंहासन जस्तो स्थानमा बस्ने।"),

    YOGA_CHATUSSAGARA("Chatussagara Yoga", "चतुःसागर योग"),
    EFFECT_CHATUSSAGARA("Fame spreading to four directions, international recognition.", "चारैतिर प्रसिद्धि फैलिने, अन्तर्राष्ट्रिय मान्यता।"),

    YOGA_AKHANDA_SAMRAJYA_EXP("Akhanda Samrajya Yoga", "अखण्ड साम्राज्य योग"),
    EFFECT_AKHANDA_SAMRAJYA_EXP("Unbroken empire, sustained authority throughout life.", "अखण्डित साम्राज्य, जीवनभर निरन्तर अधिकार।"),

    YOGA_DIGBALA_RAJA("Digbala Raja Yoga", "दिक्बल राज योग"),
    EFFECT_DIGBALA_RAJA("Directional strength giving kingly results.", "दिशाको बलले राजकीय परिणाम दिने।"),

    // ============================================
    // EXTENDED DHANA (WEALTH) YOGAS
    // ============================================
    YOGA_MAHALAKSHMI("Mahalakshmi Yoga", "महालक्ष्मी योग"),
    EFFECT_MAHALAKSHMI("Great wealth, luxuries, blessed by goddess Lakshmi.", "महान धन, विलासिता, देवी लक्ष्मीको कृपा।"),

    YOGA_KUBERA_EXP("Kubera Yoga", "कुबेर योग"),
    EFFECT_KUBERA_EXP("Treasurer of the gods, immense wealth accumulation.", "देवताहरूको कोषाध्यक्ष, अपार धन संचय।"),

    YOGA_DHANA_KARAKA("Dhana Karaka Yoga", "धन कारक योग"),
    EFFECT_DHANA_KARAKA("Natural significator of wealth giving prosperity.", "धनको प्राकृतिक कारकले समृद्धि दिने।"),

    YOGA_INDU_LAGNA_EXP("Indu Lagna Dhana Yoga", "इन्दु लग्न धन योग"),
    EFFECT_INDU_LAGNA_EXP("Wealth through Moon ascendant calculations.", "चन्द्र लग्न गणनाद्वारा धन।"),

    YOGA_BUSINESS("Business Yoga", "व्यापार योग"),
    EFFECT_BUSINESS("Success in commercial ventures and trade.", "व्यापारिक उद्यम र व्यापारमा सफलता।"),

    YOGA_PROPERTY("Property Yoga", "सम्पत्ति योग"),
    EFFECT_PROPERTY("Gains through land, buildings, and real estate.", "जग्गा, भवन र अचल सम्पत्तिबाट लाभ।"),

    YOGA_INHERITANCE("Inheritance Yoga", "उत्तराधिकार योग"),
    EFFECT_INHERITANCE("Wealth through inheritance, hidden treasures.", "उत्तराधिकार, गुप्त धनबाट सम्पत्ति।"),

    // ============================================
    // ARISHTA (NEGATIVE) YOGAS
    // ============================================
    YOGA_DARIDRA_EXP("Daridra Yoga", "दरिद्र योग"),
    EFFECT_DARIDRA_EXP("Poverty, financial struggles, difficulties in accumulating wealth.", "गरिबी, आर्थिक संघर्ष, धन संचयमा कठिनाई।"),

    YOGA_BALARISHTA("Balarishta Yoga", "बालारिष्ट योग"),
    EFFECT_BALARISHTA("Early childhood health concerns, requires remedies.", "बाल्यकालमा स्वास्थ्य चिन्ता, उपचार आवश्यक।"),

    YOGA_ROGAISHTA("Rogaishta Yoga", "रोगेष्ट योग"),
    EFFECT_ROGAISHTA("Health afflictions, chronic diseases possible.", "स्वास्थ्य समस्या, दीर्घकालीन रोग सम्भव।"),

    YOGA_BANDHANA("Bandhana Yoga", "बन्धन योग"),
    EFFECT_BANDHANA("Confinement, imprisonment, or restrictions.", "कैद, बन्धन, वा प्रतिबन्ध।"),

    YOGA_DURYOGA("Duryoga", "दुर्योग"),
    EFFECT_DURYOGA("General misfortune, obstacles in progress.", "सामान्य दुर्भाग्य, प्रगतिमा बाधा।"),

    YOGA_COMBUSTION("Combustion Yoga", "अस्त योग"),
    EFFECT_COMBUSTION("Planet too close to Sun, diminished significations.", "ग्रह सूर्यको धेरै नजिक, कमजोर महत्व।"),

    // ============================================
    // SANNYASA & MOKSHA YOGAS
    // ============================================
    YOGA_SANNYASA("Sannyasa Yoga", "सन्यास योग"),
    EFFECT_SANNYASA("Renunciation, detachment from material world.", "त्याग, भौतिक संसारबाट विरक्ति।"),

    YOGA_MOKSHA("Moksha Yoga", "मोक्ष योग"),
    EFFECT_MOKSHA("Liberation, spiritual enlightenment potential.", "मुक्ति, आध्यात्मिक ज्ञानको सम्भावना।"),

    YOGA_PRAVRAJYA("Pravrajya Yoga", "प्रव्रज्या योग"),
    EFFECT_PRAVRAJYA("Ascetic life, monastic tendencies.", "तपस्वी जीवन, सन्यासी प्रवृत्ति।"),

    YOGA_KETU_MOKSHA("Ketu Moksha Yoga", "केतु मोक्ष योग"),
    EFFECT_KETU_MOKSHA("Spiritual liberation through Ketu's grace.", "केतुको कृपाद्वारा आध्यात्मिक मुक्ति।"),

    YOGA_MOKSHA_TRIKONA("Moksha Trikona Yoga", "मोक्ष त्रिकोण योग"),
    EFFECT_MOKSHA_TRIKONA("Spiritual advancement through 4th, 8th, 12th connection.", "४, ८, १२ सम्बन्धद्वारा आध्यात्मिक प्रगति।"),

    // ============================================
    // LAGNA (ASCENDANT) YOGAS
    // ============================================
    YOGA_LAGNESH_STRENGTH("Lagnesh Strength Yoga", "लग्नेश बल योग"),
    EFFECT_LAGNESH_STRENGTH("Strong ascendant lord giving vitality and success.", "बलियो लग्नेशले जीवनशक्ति र सफलता दिने।"),

    YOGA_LAGNA_ADHI("Lagna Adhi Yoga", "लग्न अधि योग"),
    EFFECT_LAGNA_ADHI("Benefics in 6th, 7th, 8th from Lagna giving protection.", "६, ७, ८मा शुभ ग्रहले सुरक्षा दिने।"),

    YOGA_SUBHAKARTARI_LAGNA("Subhakartari Lagna Yoga", "शुभकर्तरी लग्न योग"),
    EFFECT_SUBHAKARTARI_LAGNA("Benefics hemming the ascendant, overall protection.", "लग्नलाई शुभ ग्रहले घेरेको, समग्र सुरक्षा।"),

    YOGA_PAPAKARTARI_LAGNA("Papakartari Lagna Yoga", "पापकर्तरी लग्न योग"),
    EFFECT_PAPAKARTARI_LAGNA("Malefics hemming the ascendant, challenges in life.", "लग्नलाई पाप ग्रहले घेरेको, जीवनमा चुनौती।"),

    YOGA_VARGOTTAMA_LAGNA("Vargottama Lagna Yoga", "वर्गोत्तम लग्न योग"),
    EFFECT_VARGOTTAMA_LAGNA("Same sign in Rashi and Navamsha, strengthened ascendant.", "राशि र नवांशमा एउटै राशि, बलियो लग्न।"),

    // ============================================
    // PARIVARTTANA (EXCHANGE) YOGAS
    // ============================================
    YOGA_MAHA_PARIVARTTANA("Maha Parivarttana Yoga", "महा परिवर्तन योग"),
    EFFECT_MAHA_PARIVARTTANA("Powerful exchange between benefic house lords.", "शुभ भाव अधिपतिहरू बीच शक्तिशाली आदानप्रदान।"),

    YOGA_KHALA_PARIVARTTANA("Khala Parivarttana Yoga", "खल परिवर्तन योग"),
    EFFECT_KHALA_PARIVARTTANA("Exchange involving 3rd lord, mixed results.", "तृतीयेश समावेश आदानप्रदान, मिश्रित परिणाम।"),

    YOGA_DAINYA_PARIVARTTANA("Dainya Parivarttana Yoga", "दैन्य परिवर्तन योग"),
    EFFECT_DAINYA_PARIVARTTANA("Exchange involving 6th/8th/12th, challenging but transformative.", "६/८/१२ समावेश आदानप्रदान, चुनौतीपूर्ण तर रूपान्तरणकारी।"),

    // ============================================
    // PLANETARY YOGAS
    // ============================================
    YOGA_SURYA_DIGNITY("Surya Dignity Yoga", "सूर्य गरिमा योग"),
    EFFECT_SURYA_DIGNITY("Sun in strong position giving authority.", "बलियो स्थानमा सूर्यले अधिकार दिने।"),

    YOGA_CHANDRA_DIGNITY("Chandra Dignity Yoga", "चन्द्र गरिमा योग"),
    EFFECT_CHANDRA_DIGNITY("Moon in favorable position giving mental peace.", "अनुकूल स्थानमा चन्द्रले मानसिक शान्ति दिने।"),

    YOGA_MANGAL_DIGNITY("Mangala Dignity Yoga", "मंगल गरिमा योग"),
    EFFECT_MANGAL_DIGNITY("Mars in powerful position giving courage.", "शक्तिशाली स्थानमा मंगलले साहस दिने।"),

    YOGA_BUDHA_DIGNITY("Budha Dignity Yoga", "बुध गरिमा योग"),
    EFFECT_BUDHA_DIGNITY("Mercury well-placed giving intelligence.", "राम्रो स्थानमा बुधले बुद्धि दिने।"),

    YOGA_GURU_DIGNITY("Guru Dignity Yoga", "गुरु गरिमा योग"),
    EFFECT_GURU_DIGNITY("Jupiter strong giving wisdom and prosperity.", "बलियो गुरुले ज्ञान र समृद्धि दिने।"),

    YOGA_SHUKRA_DIGNITY("Shukra Dignity Yoga", "शुक्र गरिमा योग"),
    EFFECT_SHUKRA_DIGNITY("Venus well-placed giving comforts and beauty.", "राम्रो स्थानमा शुक्रले सुख र सौन्दर्य दिने।"),

    YOGA_SHANI_DIGNITY("Shani Dignity Yoga", "शनि गरिमा योग"),
    EFFECT_SHANI_DIGNITY("Saturn in good position giving discipline and longevity.", "राम्रो स्थानमा शनिले अनुशासन र दीर्घायु दिने।"),

    // ============================================
    // NAKSHATRA YOGAS
    // ============================================
    YOGA_PUSHYA_NAKSHATRA("Pushya Nakshatra Yoga", "पुष्य नक्षत्र योग"),
    EFFECT_PUSHYA_NAKSHATRA("Auspicious star of nourishment and growth.", "पोषण र वृद्धिको शुभ तारा।"),

    YOGA_GANDANTA("Gandanta Yoga", "गण्डान्त योग"),
    EFFECT_GANDANTA("Junction point between water and fire signs, karmic knot.", "जल र अग्नि राशि बीचको जंक्शन, कार्मिक गाँठो।"),

    YOGA_NAKSHATRA_DEVA("Deva Nakshatra Yoga", "देव नक्षत्र योग"),
    EFFECT_NAKSHATRA_DEVA("Divine temperament nakshatra placement.", "दैवी स्वभाव नक्षत्र स्थान।"),

    YOGA_NAKSHATRA_MANUSHYA("Manushya Nakshatra Yoga", "मनुष्य नक्षत्र योग"),
    EFFECT_NAKSHATRA_MANUSHYA("Human temperament nakshatra, balanced nature.", "मानव स्वभाव नक्षत्र, सन्तुलित प्रकृति।"),

    YOGA_NAKSHATRA_RAKSHASA("Rakshasa Nakshatra Yoga", "राक्षस नक्षत्र योग"),
    EFFECT_NAKSHATRA_RAKSHASA("Fierce temperament nakshatra, powerful but challenging.", "उग्र स्वभाव नक्षत्र, शक्तिशाली तर चुनौतीपूर्ण।"),

    // ============================================
    // CLASSICAL NABHASA YOGAS
    // ============================================
    YOGA_RAJJU_EXP("Rajju Yoga", "रज्जु योग"),
    EFFECT_RAJJU_EXP("Planets in movable signs, constant movement and travel.", "चर राशिमा ग्रह, निरन्तर चाल र यात्रा।"),

    YOGA_MUSALA_EXP("Musala Yoga", "मुसल योग"),
    EFFECT_MUSALA_EXP("Planets in fixed signs, stability and persistence.", "स्थिर राशिमा ग्रह, स्थिरता र दृढता।"),

    YOGA_NALA_EXP("Nala Yoga", "नल योग"),
    EFFECT_NALA_EXP("Planets in dual signs, adaptability and versatility.", "द्विस्वभाव राशिमा ग्रह, अनुकूलता र बहुमुखी प्रतिभा।"),

    YOGA_KAMALA("Kamala Yoga", "कमल योग"),
    EFFECT_KAMALA("Lotus yoga, planets in kendras, wealth and fame.", "कमल योग, केन्द्रमा ग्रह, धन र प्रसिद्धि।"),

    YOGA_VAPI("Vapi Yoga", "वापी योग"),
    EFFECT_VAPI("Well yoga, planets in panaparas, steady gains.", "कुवा योग, पनपारमा ग्रह, स्थिर लाभ।"),

    YOGA_YUPA("Yupa Yoga", "यूप योग"),
    EFFECT_YUPA("Sacrificial post yoga, religious and charitable.", "यज्ञस्तम्भ योग, धार्मिक र दानशील।"),

    YOGA_SHARA("Shara Yoga", "शर योग"),
    EFFECT_SHARA("Arrow yoga, focused and goal-oriented.", "तीर योग, केन्द्रित र लक्ष्योन्मुख।"),

    YOGA_SHAKTI("Shakti Yoga", "शक्ति योग"),
    EFFECT_SHAKTI("Spear yoga, powerful and commanding.", "भाला योग, शक्तिशाली र आदेशात्मक।"),

    YOGA_DANDA("Danda Yoga", "दण्ड योग"),
    EFFECT_DANDA("Staff yoga, authority and punishment capacity.", "दण्ड योग, अधिकार र दण्ड क्षमता।"),

    YOGA_NAUKA("Nauka Yoga", "नौका योग"),
    EFFECT_NAUKA("Boat yoga, success through overseas connections.", "नाव योग, विदेशी सम्बन्धद्वारा सफलता।"),

    YOGA_KUTA("Kuta Yoga", "कूट योग"),
    EFFECT_KUTA("Peak yoga, rise to high positions.", "शिखर योग, उच्च पदमा उदय।"),

    YOGA_VAJRA("Vajra Yoga", "वज्र योग"),
    EFFECT_VAJRA("Thunderbolt yoga, strong beginning and end of life.", "वज्र योग, जीवनको सुरु र अन्त बलियो।"),

    // Categories
    YOGA_CAT_BHAVA("Bhava Yoga", "भाव योग"),
    YOGA_CAT_BHAVA_DESC("House Lord Placements", "भाव अधिपति स्थितिहरू"),
    YOGA_CAT_CONJUNCTION("Conjunction Yoga", "युति योग"),
    YOGA_CAT_CONJUNCTION_DESC("Planetary Conjunctions", "ग्रह युतिहरू");
}
