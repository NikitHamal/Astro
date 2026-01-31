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
    YOGA_CAT_CONJUNCTION_DESC("Planetary Conjunctions", "ग्रह युतिहरू"),
    YOGA_CAT_NABHASA("Nabhasa Yoga", "नाभस योग"),
    YOGA_CAT_NABHASA_DESC("Planetary Patterns", "ग्रह आकृतिहरू"),

    // ============================================
    // PANCH MAHAPURUSHA YOGA DETAILS
    // ============================================
    
    // Ruchaka (Mars)
    DESC_RUCHAKA("Ruchaka Yoga is formed when Mars is in Kendra in its own sign (Aries/Scorpio) or exaltation (Capricorn). It bestows courage, leadership abilities, physical strength, and success in competitive fields.", "रुचक योग तब बन्छ जब मंगल केन्द्र भावमा आफ्नो राशि (मेष/वृश्चिक) वा उच्च राशि (मकर) मा हुन्छ। यसले साहस, नेतृत्व क्षमता, शारीरिक शक्ति र प्रतिस्पर्धात्मक क्षेत्रहरूमा सफलता प्रदान गर्दछ।"),
    SHORT_DESC_RUCHAKA("Courage, leadership, physical prowess", "साहस, नेतृत्व, शारीरिक पराक्रम"),
    BLESSINGS_RUCHAKA("Courage, Leadership, Physical strength, Victory over enemies, Land/property", "साहस, नेतृत्व, शारीरिक शक्ति, शत्रुहरूमाथि विजय, जग्गा/सम्पत्ति"),
    DEITY_RUCHAKA("Lord Hanuman or Lord Kartikeya", "भगवान हनुमान वा भगवान कार्तिकेय"),
    GEMSTONE_RUCHAKA("Red Coral (Moonga)", "मुगा"),
    MANTRA_RUCHAKA("Om Kraam Kreem Kraum Sah Bhaumaya Namah", "ॐ क्रां क्रीं क्रौं सः भौमाय नमः"),
    CHARITY_RUCHAKA("red lentils, red cloth, jaggery to the needy", "रातो दाल, रातो कपडा, गरिबलाई सख्खर"),
    CAREERS_RUCHAKA("Military, Police, Sports, Surgery, Engineering, Real estate, Martial arts", "सेना, प्रहरी, खेलकुद, शल्यक्रिया, इन्जिनियरिङ, घरजग्गा, मार्सल आर्ट"),
    TRAITS_PHYSICAL_RUCHAKA("Strong physical constitution, athletic build, courage in action", "बलियो शारीरिक बनावट, एथलेटिक शरीर, कार्यमा साहस"),
    TRAITS_MENTAL_RUCHAKA("Fearless mindset, competitive spirit, quick decision-making", "निडर मानसिकता, प्रतिस्पर्धात्मक भावना, द्रुत निर्णय क्षमता"),
    
    // Bhadra (Mercury)
    DESC_BHADRA("Bhadra Yoga is formed when Mercury is in Kendra in its own sign (Gemini/Virgo) or exaltation (Virgo). It bestows exceptional intelligence, communication skills, business acumen, and scholarly abilities.", "भद्रा योग तब बन्छ जब बुध केन्द्र भावमा आफ्नो राशि (मिथुन/कन्या) वा उच्च राशि (कन्या) मा हुन्छ। यसले असाधारण बुद्धि, सञ्चार कौशल, व्यापारिक चातुर्य र विद्वता प्रदान गर्दछ।"),
    SHORT_DESC_BHADRA("Intelligence, communication, commerce", "बुद्धि, सञ्चार, वाणिज्य"),
    BLESSINGS_BHADRA("Intelligence, Eloquence, Business success, Learning ability, Youthfulness", "बुद्धि, वाक्पटुता, व्यापार सफलता, सिक्ने क्षमता, यौवन"),
    DEITY_BHADRA("Lord Vishnu or Lord Ganesha", "भगवान विष्णु वा भगवान गणेश"),
    GEMSTONE_BHADRA("Emerald (Panna)", "पन्ना"),
    MANTRA_BHADRA("Om Braam Breem Braum Sah Budhaya Namah", "ॐ ब्रां ब्रीं ब्रौं सः बुधाय नमः"),
    CHARITY_BHADRA("green moong dal, green cloth, books to students", "हरियो मुगीको दाल, हरियो कपडा, विद्यार्थीहरूलाई पुस्तक"),
    CAREERS_BHADRA("Writing, Teaching, Commerce, Accounting, IT, Journalism, Astrology", "लेखन, अध्यापन, वाणिज्य, लेखा, आईटी, पत्रकारिता, ज्योतिष"),
    TRAITS_PHYSICAL_BHADRA("Youthful appearance, expressive gestures, quick movements", "युवा उपस्थिति, अर्थपूर्ण हाउभाउ, छिटो चाल"),
    TRAITS_MENTAL_BHADRA("Sharp intellect, excellent memory, witty communication", "तीखो बुद्धि, उत्कृष्ट स्मरणशक्ति, रोचक सञ्चार"),

    // Hamsa (Jupiter)
    DESC_HAMSA("Hamsa Yoga is formed when Jupiter is in Kendra in its own sign (Sagittarius/Pisces) or exaltation (Cancer). It bestows wisdom, righteousness, fortune, and spiritual inclination.", "हंस योग तब बन्छ जब बृहस्पति केन्द्र भावमा आफ्नो राशि (धनु/मीन) वा उच्च राशि (कर्कट) मा हुन्छ। यसले ज्ञान, धार्मिकता, भाग्य र आध्यात्मिक झुकाव प्रदान गर्दछ।"),
    SHORT_DESC_HAMSA("Wisdom, spirituality, fortune", "ज्ञान, आध्यात्मिकता, भाग्य"),
    BLESSINGS_HAMSA("Wisdom, Fortune, Spiritual growth, Good character, Respected status", "ज्ञान, भाग्य, आध्यात्मिक वृद्धि, राम्रो चरित्र, सम्मानित स्थिति"),
    DEITY_HAMSA("Lord Brihaspati or Lord Shiva", "भगवान वृहस्पति वा भगवान शिव"),
    GEMSTONE_HAMSA("Yellow Sapphire (Pukhraj)", "पुष्पराज"),
    MANTRA_HAMSA("Om Graam Greem Graum Sah Gurave Namah", "ॐ ग्रां ग्रीं ग्रौं सः गुरवे नमः"),
    CHARITY_HAMSA("turmeric, yellow cloth, ghee, gold to Brahmins", "बेसार, पहेँलो कपडा, घ्यू, ब्राह्मणहरूलाई सुन"),
    CAREERS_HAMSA("Education, Law, Finance, Consulting, Priesthood, Philosophy, Judiciary", "शिक्षा, कानुन, वित्त, परामर्श, पुरोहित्याइँ, दर्शन, न्यायपालिका"),
    TRAITS_PHYSICAL_HAMSA("Graceful bearing, fair complexion, pleasant appearance", "शालीन स्वभाव, गोरो वर्ण, रमाइलो उपस्थिति"),
    TRAITS_MENTAL_HAMSA("Wisdom, philosophical outlook, positive thinking", "ज्ञान, दार्शनिक दृष्टिकोण, सकारात्मक सोच"),

    // Malavya (Venus)
    DESC_MALAVYA("Malavya Yoga is formed when Venus is in Kendra in its own sign (Taurus/Libra) or exaltation (Pisces). It bestows beauty, luxury, artistic talents, and harmonious relationships.", "मालव्य योग तब बन्छ जब शुक्र केन्द्र भावमा आफ्नो राशि (वृष/तुला) वा उच्च राशि (मीन) मा हुन्छ। यसले सौन्दर्य, विलासिता, कलात्मक प्रतिभा र सामञ्जस्यपूर्ण सम्बन्ध प्रदान गर्दछ।"),
    SHORT_DESC_MALAVYA("Beauty, luxury, artistic talents", "सौन्दर्य, विलासिता, कलात्मक प्रतिभा"),
    BLESSINGS_MALAVYA("Beauty, Luxury, Artistic ability, Happy marriage, Vehicles", "सौन्दर्य, विलासिता, कलात्मक क्षमता, सुखी विवाह, सवारी साधन"),
    DEITY_MALAVYA("Goddess Lakshmi or Goddess Saraswati", "देवी लक्ष्मी वा देवी सरस्वती"),
    GEMSTONE_MALAVYA("Diamond (Heera) or White Sapphire", "हीरा वा सेतो नीलम"),
    MANTRA_MALAVYA("Om Draam Dreem Draum Sah Shukraya Namah", "ॐ द्रां द्रीं द्रौं सः शुक्राय नमः"),
    CHARITY_MALAVYA("white rice, white cloth, sugar, silver items to women", "सेतो चामल, सेतो कपडा, चिनी, महिलाहरूलाई चाँदीका सामान"),
    CAREERS_MALAVYA("Arts, Entertainment, Fashion, Hospitality, Luxury goods, Interior design, Music", "कला, मनोरञ्जन, फेसन, आतिथ्य, विलासी सामान, आन्तरिक डिजाइन, संगीत"),
    TRAITS_PHYSICAL_MALAVYA("Attractive appearance, pleasant features, artistic grace", "आकर्षक उपस्थिति, सुखद अनुहार, कलात्मक अनुग्रह"),
    TRAITS_MENTAL_MALAVYA("Refined sensibilities, appreciation of beauty, romantic nature", "परिष्कृत संवेदनशीलता, सौन्दर्यको प्रशंसा, रोमान्टिक स्वभाव"),

    // Sasha (Saturn)
    DESC_SASHA("Sasha Yoga is formed when Saturn is in Kendra in its own sign (Capricorn/Aquarius) or exaltation (Libra). It bestows discipline, longevity, authority, and success through persistent effort.", "शश योग तब बन्छ जब शनि केन्द्र भावमा आफ्नो राशि (मकर/कुम्भ) वा उच्च राशि (तुला) मा हुन्छ। यसले अनुशासन, दीर्घायु, अधिकार र निरन्तर प्रयासद्वारा सफलता प्रदान गर्दछ।"),
    SHORT_DESC_SASHA("Discipline, authority, longevity", "अनुशासन, अधिकार, दीर्घायु"),
    BLESSINGS_SASHA("Longevity, Authority, Discipline, Servants, Success through perseverance", "दीर्घायु, अधिकार, अनुशासन, सेवकहरू, दृढताद्वारा सफलता"),
    DEITY_SASHA("Lord Shani or Lord Hanuman", "भगवान शनि वा भगवान हनुमान"),
    GEMSTONE_SASHA("Blue Sapphire (Neelam) - only after careful consultation", "नीलम - सावधानीपूर्वक परामर्श पछि मात्र"),
    MANTRA_SASHA("Om Praam Preem Praum Sah Shanaischaraya Namah", "ॐ प्रां प्रीं प्रौं सः शनैश्चराय नमः"),
    CHARITY_SASHA("black sesame, black cloth, iron items, oil to the poor", "कालो तिल, कालो कपडा, फलामका सामान, गरिबलाई तेल"),
    CAREERS_SASHA("Government, Construction, Mining, Management, Agriculture, Real estate, Manufacturing", "सरकार, निर्माण, खानी, व्यवस्थापन, कृषि, घरजग्गा, उत्पादन"),
    TRAITS_PHYSICAL_SASHA("Lean build, serious demeanor, endurance and stamina", "दुब्लो बनावट, गम्भीर व्यवहार, सहनशक्ति र सामर्थ्य"),
    TRAITS_MENTAL_SASHA("Disciplined mind, methodical thinking, patience", "अनुशासित दिमाग, व्यवस्थित सोच, धैर्य"),

    // Common traits and effects
    TRAITS_CAREER_GENERIC("Success in fields aligned with the planet's nature", "ग्रहको प्रकृतिसँग मिल्दाजुल्दा क्षेत्रमा सफलता"),
    TRAITS_SPIRITUAL_GENERIC("Inclination toward higher values and spiritual paths", "उच्च मूल्य र आध्यात्मिक मार्गहरू प्रति झुकाव"),
    TRAITS_RELATIONSHIP_GENERIC("Impact on interpersonal dynamics and social standing", "अन्तरव्यक्तिगत गतिशीलता र सामाजिक स्थितिमा प्रभाव"),
    
    MANIFEST_EXCEPTIONAL("Results manifest prominently throughout life", "परिणामहरू जीवनभर प्रमुख रूपमा प्रकट हुन्छन्"),
    MANIFEST_STRONG("Results manifest notably, especially during dasha periods", "परिणामहरू उल्लेखनीय रूपमा प्रकट हुन्छन्, विशेष गरी दशा अवधिमा"),
    MANIFEST_MODERATE("Results manifest moderately with conscious effort", "सचेत प्रयासको साथ परिणामहरू मध्यम रूपमा प्रकट हुन्छन्"),
    MANIFEST_SUBTLE("Results manifest subtly, may need activation", "परिणामहरू सूक्ष्म रूपमा प्रकट हुन्छन्, सक्रियताको आवश्यकता हुन सक्छ"),

    HOUSE_EFFECT_1("{0} in 1st house: Yoga effects directly impact personality, health, and overall life approach. Strong self-projection of {1}''s qualities.", "{0} पहिलो भावमा: योगको प्रभावले व्यक्तित्व, स्वास्थ्य र समग्र जीवन दृष्टिकोणमा सीधा प्रभाव पार्छ। {1} को गुणहरूको बलियो आत्म-प्रक्षेपण।"),
    HOUSE_EFFECT_4("{0} in 4th house: Benefits related to home, mother, vehicles, property, and inner peace. {1}''s blessings in domestic life.", "{0} चौथो भावमा: घर, आमा, सवारी साधन, सम्पत्ति र आन्तरिक शान्तिसँग सम्बन्धित लाभहरू। पारिवारिक जीवनमा {1} को आशीर्वाद।"),
    HOUSE_EFFECT_7("{0} in 7th house: Strong impact on marriage, partnerships, business dealings. Spouse may have {1}''s characteristics.", "{0} सातौं भावमा: विवाह, साझेदारी, व्यापारिक लेनदेनमा बलियो प्रभाव। जीवनसाथीमा {1} को विशेषताहरू हुन सक्छन्।"),
    HOUSE_EFFECT_10("{0} in 10th house: Career excellence, public recognition, authority. {1}''s qualities shine in professional life.", "{0} दशौं भावमा: करियरमा उत्कृष्टता, सार्वजनिक मान्यता, अधिकार। व्यावसायिक जीवनमा {1} को गुणहरू चम्किन्छन्।"),
    HOUSE_EFFECT_GENERIC("General yoga effects apply", "सामान्य योग प्रभावहरू लागू हुन्छन्"),

    STRENGTH_LEVEL_EXCEPTIONAL("Exceptional (80-100%)", "असाधारण (८०-१००%)"),
    STRENGTH_LEVEL_STRONG("Strong (60-79%)", "बलियो (६०-७९%)"),
    STRENGTH_LEVEL_MODERATE("Moderate (40-59%)", "मध्यम (४०-५९%)"),
    STRENGTH_LEVEL_WEAK("Weak (20-39%)", "कमजोर (२०-३९%)"),
    STRENGTH_LEVEL_VERY_WEAK("Very Weak (0-19%)", "धेरै कमजोर (०-१९%)"),

    INTERPRET_POWERFUL("This is an exceptionally powerful yoga that will significantly influence your life.", "यो एक असाधारण शक्तिशाली योग हो जसले तपाईंको जीवनमा महत्त्वपूर्ण प्रभाव पार्नेछ।"),
    INTERPRET_STRONG("This is a strong yoga with notable effects throughout life.", "यो एक बलियो योग हो जसको जीवनभर उल्लेखनीय प्रभाव रहनेछ।"),
    INTERPRET_MODERATE("This yoga has moderate strength and will manifest clearly during favorable periods.", "यो योगको मध्यम बल छ र अनुकूल अवधिमा स्पष्ट रूपमा प्रकट हुनेछ।"),
    INTERPRET_WEAK("This yoga exists but may need activation through conscious effort or favorable transits.", "यो योग अवस्थित छ तर सचेत प्रयास वा अनुकूल गोचर मार्फत सक्रियताको आवश्यकता पर्न सक्छ।"),

    INTERPRET_FORMATION("{0} is formed with {1} {2} in the {3}.", "{3} मा {1} {2} हुँदा {0} बन्छ।"),
    INTERPRET_STRENGTH("With a strength of {0}%, {1}", "{0}% को बलको साथ, {1}"),
    INTERPRET_BLESSINGS("Key blessings: {0}", "मुख्य आशीर्वाद: {0}"),

    REMEDY_ACTION_WORSHIP("Worship {0} on {1}''s day ({2})", "{1} को दिन ({2}) मा {0} को पूजा गर्नुहोस्"),
    REMEDY_TIMING_MORNING("{0} mornings", "{0} बिहान"),
    REMEDY_BENEFIT_ACTIVATE("Activates and strengthens the yoga''s positive effects", "योगको सकारात्मक प्रभावहरूलाई सक्रिय र बलियो बनाउँछ"),
    REMEDY_ACTION_CAREER("Pursue careers in {0}", "{0} मा करियर बनाउनुहोस्"),
    REMEDY_TIMING_DASHA("During {0}''s dasha/antardasha periods", "{0} को दशा/अन्तर्दशा अवधिमा"),
    REMEDY_BENEFIT_CAREER("Maximum success in areas aligned with the yoga", "योगसँग मिल्दाजुल्दा क्षेत्रहरूमा अधिकतम सफलता"),
    REMEDY_ACTION_GEMSTONE("Consider wearing {0} after proper consultation", "उचित परामर्श पछि {0} लगाउने विचार गर्नुहोस्"),
    REMEDY_TIMING_MUHURTA("During auspicious muhurta on {0}", "{0} मा शुभ मुहूर्तको समयमा"),
    REMEDY_BENEFIT_AMPLIFY("Amplifies {0}''s positive significations", "{0} को सकारात्मक विशेषताहरूलाई बढावा दिन्छ"),
    REMEDY_ACTION_MANTRA("Recite {0} 108 times", "{0} १०८ पटक जप गर्नुहोस्"),
    REMEDY_TIMING_HORA("Daily during {0}''s hora", "दैनिक {0} को होरामा"),
    REMEDY_BENEFIT_INVOKE("Invokes {0}''s blessings for yoga activation", "योग सक्रियताका लागि {0} को आशीर्वाद प्राप्त हुन्छ"),
    REMEDY_ACTION_CHARITY("Donate {0} on {1}", "{1} मा {0} दान गर्नुहोस्"),
    REMEDY_TIMING_REGULAR("{0}s regularly", "नियमित {0} मा"),
    REMEDY_BENEFIT_KARMA("Creates good karma and removes obstacles", "राम्रो कर्म सिर्जना गर्दछ र बाधाहरू हटाउँछ"),

    COMBINED_SYNERGY_HAMSA_MALAVYA("Hamsa-Malavya combination: Wisdom with beauty, spiritual with material balance", "हंस-मालव्य संयोजन: सौन्दर्यको साथ ज्ञान, भौतिक र आध्यात्मिक सन्तुलन"),
    COMBINED_CAREER_HAMSA_MALAVYA("Excellent for careers in education, arts, counseling", "शिक्षा, कला, परामर्शमा करियरको लागि उत्कृष्ट"),
    COMBINED_SYNERGY_RUCHAKA_SASHA("Ruchaka-Sasha combination: Courage with discipline, action with patience", "रुचक-शश संयोजन: अनुशासनको साथ साहस, धैर्यको साथ कार्य"),
    COMBINED_CAREER_RUCHAKA_SASHA("Excellent for leadership, military, engineering management", "नेतृत्व, सेना, इन्जिनियरिङ व्यवस्थापनको लागि उत्कृष्ट"),
    COMBINED_SYNERGY_BHADRA_HAMSA("Bhadra-Hamsa combination: Intelligence with wisdom, communication with knowledge", "भद्रा-हंस संयोजन: ज्ञानको साथ बुद्धि, ज्ञानको साथ सञ्चार"),
    COMBINED_CAREER_BHADRA_HAMSA("Excellent for teaching, writing, law, philosophy", "शिक्षण, लेखन, कानुन, दर्शनको लागि उत्कृष्ट"),
    COMBINED_RARE_COUNT("Rare combination of {0} Mahapurusha Yogas indicates a highly blessed chart", "{0} महापुरुष योगहरूको दुर्लभ संयोजनले एक उच्च आशीर्वादित कुण्डली संकेत गर्दछ"),
    
    RARITY_2("Uncommon - Two Mahapurusha Yogas", "असामान्य - दुई महापुरुष योग"),
    RARITY_3("Rare - Three Mahapurusha Yogas", "दुर्लभ - तीन महापुरुष योग"),
    RARITY_4("Very Rare - Four Mahapurusha Yogas", "धेरै दुर्लभ - चार महापुरुष योग"),
    RARITY_5("Extremely Rare - All Five Mahapurusha Yogas", "अत्यन्तै दुर्लभ - सबै पाँच महापुरुष योग"),
    
    COMBINED_INTERPRET("Having {0} Mahapurusha Yogas is {1}. The combined influence of {2} creates a multifaceted personality with diverse talents and blessings.", "{0} महापुरुष योग हुनु {1} हो। {2} को संयुक्त प्रभावले विविध प्रतिभा र आशीर्वादसहितको बहुआयामिक व्यक्तित्व सिर्जना गर्दछ।"),
    INTERPRET_SIGNIFICANT_BLESSING("a significant blessing", "एक महत्त्वपूर्ण आशीर्वाद"),
    INTERPRET_EXCEPTIONALLY_RARE("exceptionally rare and powerful", "असाधारण रूपमा दुर्लभ र शक्तिशाली"),

    ACTIVATION_PRIMARY("{0} Mahadasha - Primary yoga activation period", "{0} महादशा - प्राथमिक योग सक्रियता अवधि"),
    ACTIVATION_SECONDARY("{0} Antardasha - Secondary activation in any major period", "{0} अन्तर्दशा - कुनै पनि मुख्य अवधिमा माध्यमिक सक्रियता"),
    ACTIVATION_TRANSIT("When {0} transits its natal position or exaltation sign", "जब {0} आफ्नो जन्म स्थिति वा उच्च राशिमा गोचर गर्दछ"),
    ACTIVATION_JUPITER("When Jupiter transits over or aspects natal {0}", "जब बृहस्पति जन्म {0} माथि गोचर गर्दछ वा दृष्टि दिन्छ"),

    SUMMARY_NONE("No Panch Mahapurusha Yoga is formed in this chart. These special yogas require Mars, Mercury, Jupiter, Venus, or Saturn to be in Kendra houses (1, 4, 7, 10) in their own or exaltation signs. While these yogas are not present, other yogas and planetary combinations in your chart provide their own blessings.", "यस कुण्डलीमा कुनै पञ्च महापुरुष योग बनेको छैन। यी विशेष योगहरूका लागि मंगल, बुध, बृहस्पति, शुक्र वा शनि केन्द्र भाव (१, ४, ७, १०) मा आफ्नै राशि वा उच्च राशिमा हुनुपर्छ। यद्यपि यी योगहरू उपस्थित छैनन्, तपाईंको कुण्डलीमा रहेका अन्य योग र ग्रह संयोजनहरूले आफ्नै आशीर्वाद प्रदान गर्छन्।"),
    SUMMARY_PRESENT("This chart has {0} Panch Mahapurusha Yoga(s): {1}. These are among the most auspicious yogas in Vedic astrology, indicating a soul that has earned special blessings through past-life merit. The native is blessed with the positive qualities of the yoga-forming planets.", "यस कुण्डलीमा {0} पञ्च महापुरुष योग(हरू) छन्: {1}। यी वैदिक ज्योतिषमा सबैभन्दा शुभ योगहरू मध्ये हुन्, जसले पूर्वजन्मको पुण्यबाट विशेष आशीर्वाद प्राप्त गरेको आत्मालाई संकेत गर्दछ। जातक योग बनाउने ग्रहहरूको सकारात्मक गुणहरूले आशीर्वादित हुन्छ।"),
    INSIGHT_ITEM("{0}: {1} (Strength: {2}%)", "{0}: {1} (बल: {2}%)"),
    
    REC_STRENGTHEN_BENEFICS("Focus on strengthening the natural benefics in your chart", "आफ्नो कुण्डलीमा प्राकृतिक शुभ ग्रहहरूलाई बलियो बनाउनमा ध्यान दिनुहोस्"),
    REC_EXPLORE_OTHER_YOGAS("Explore other yogas present in your horoscope", "आफ्नो जन्मकुण्डलीमा रहेका अन्य योगहरू अन्वेषण गर्नुहोस्"),
    REC_PLANETARY_REMEDIES("Planetary remedies can enhance positive influences", "ग्रह सम्बन्धी उपायहरूले सकारात्मक प्रभावहरूलाई बढाउन सक्छन्"),
    REC_ACTIVATE_YOGA("Activate {0} through {1} worship", "{1} पूजाको माध्यमबाट {0} सक्रिय गर्नुहोस्"),
    REC_BEST_CAREER("Best career alignment: {0}", "उत्तम करियर संरेखण: {0}"),

    // ============================================
    // VIPAREETA RAJA YOGA DETAILS
    // ============================================
    VIP_DESC_HARSHA("6th lord in dusthana - Victory over enemies, good health, happiness from overcoming obstacles", "६औं भावको स्वामी दुस्थानमा - शत्रुमाथि विजय, राम्रो स्वास्थ्य, बाधाहरू पार गरेर सुख"),
    VIP_DESC_SARALA("8th lord in dusthana - Fearlessness, longevity, gains through inheritance, occult knowledge", "८औं भावको स्वामी दुस्थानमा - निडरता, दीर्घायु, उत्तराधिकारबाट लाभ, गुप्त ज्ञान"),
    VIP_DESC_VIMALA("12th lord in dusthana - Reduced losses, spiritual advancement, liberation, fame after death", "१२औं भावको स्वामी दुस्थानमा - घाटामा कमी, आध्यात्मिक उन्नति, मुक्ति, मृत्युपछि प्रसिद्धि"),

    STRENGTH_EXCEPTIONAL("Exceptional", "असाधारण"),
    STRENGTH_STRONG_VIP("Strong", "बलियो"),
    STRENGTH_MODERATE_VIP("Moderate", "मध्यम"),
    STRENGTH_MILD("Mild", "हल्का"),
    STRENGTH_WEAK_VIP("Weak", "कमजोर"),
    STRENGTH_NOT_FORMED("Not Formed", "बनेको छैन"),

    STATUS_FULLY_ACTIVE("Fully Active", "पूर्ण सक्रिय"),
    STATUS_PARTIALLY_ACTIVE("Partially Active", "आंशिक सक्रिय"),
    STATUS_LATENT("Latent - Awaiting Activation", "सुप्त - सक्रियताको पर्खाइमा"),
    STATUS_DORMANT("Dormant", "निष्क्रिय"),
    STATUS_CANCELLED("Cancelled by Afflictions", "पीडाहरूद्वारा रद्द"),

    BENEFIT_VICTORY("Victory over enemies and competitors", "शत्रु र प्रतिस्पर्धीहरूमाथि विजय"),
    BENEFIT_HEALTH("Good health despite challenging health indications", "चुनौतीपूर्ण स्वास्थ्य संकेतहरूको बावजुद राम्रो स्वास्थ्य"),
    BENEFIT_LITIGATION("Success in litigation and legal matters", "मुद्दा मामिला र कानुनी मामिलाहरूमा सफलता"),
    BENEFIT_DEBTS("Overcoming debts and financial obligations", "ऋण र आर्थिक दायित्वहरूबाट पार पाउनु"),
    BENEFIT_SERVICE("Happiness through service and daily work", "सेवा र दैनिक कार्यबाट सुख"),
    
    BENEFIT_FEARLESS("Freedom from fear and anxiety", "डर र चिन्ताबाट मुक्ति"),
    BENEFIT_LONGEVITY("Potential for longevity", "दीर्घायुको सम्भावना"),
    BENEFIT_INHERITANCE("Gains through inheritance and legacy", "उत्तराधिकार र विरासतबाट लाभ"),
    BENEFIT_OCCULT("Success in occult and esoteric studies", "तन्त्रमन्त्र र रहस्यमयी अध्ययनमा सफलता"),
    BENEFIT_TRANSFORM("Transformation through difficulties", "कठिनाइहरू मार्फत रूपान्तरण"),
    
    BENEFIT_REDUCED_LOSS("Reduced expenditure and losses", "खर्च र घाटामा कमी"),
    BENEFIT_MOKSHA("Spiritual advancement and moksha", "आध्यात्मिक उन्नति र मोक्ष"),
    BENEFIT_FOREIGN("Success in foreign lands", "विदेशी भूमिमा सफलता"),
    BENEFIT_LIBERATION("Liberation from bondage", "बन्धनबाट मुक्ति"),
    BENEFIT_FAME_DEATH("Fame and recognition after death", "मृत्युपछि प्रसिद्धि र मान्यता"),

    FACTOR_DUSTHANA_IN_DUSTHANA("Dusthana lord placed in dusthana house ({0}th sign)", "दुस्थान स्वामी दुस्थान भावमा ({0}औं राशि)"),
    FACTOR_EXALTED_DUSTHANA("Dusthana lord is exalted - strong results", "दुस्थान स्वामी उच्च छ - बलियो परिणाम"),
    FACTOR_OWN_SIGN_DUSTHANA("Dusthana lord in own sign - stable results", "दुस्थान स्वामी आफ्नै राशिमा - स्थिर परिणाम"),
    FACTOR_RETROGRADE_INTENSE("Retrograde position intensifies yoga effects", "वक्री स्थितिले योगको प्रभावलाई तीव्र बनाउँछ"),
    FACTOR_JUPITER_ASPECT("Jupiter''s aspect enhances benefic potential", "बृहस्पतिको दृष्टिले शुभ सम्भावना बढाउँछ"),
    
    WEAKNESS_DEBILITATED("Debilitated dusthana lord - weakened results", "नीच दुस्थान स्वामी - कमजोर परिणाम"),
    WEAKNESS_COMBUST("Combust position reduces yoga strength", "अस्त स्थितिले योगको बल घटाउँछ"),
    WEAKNESS_AFFLICTED_MALEFIC("Afflicted by malefic conjunction", "पाप ग्रहको युतिबाट पीडित"),

    EXCHANGE_6_8("6th-8th lord exchange creates powerful Vipareeta yoga - enemies face sudden downfall, native benefits from others'' losses", "६औं-८औं भावको स्वामी आदानप्रदानले शक्तिशाली विपरीत योग सिर्जना गर्दछ - शत्रुहरूले अचानक पतन सामना गर्छन्, जातकले अरूको घाटाबाट लाभ उठाउँछ"),
    EXCHANGE_6_12("6th-12th lord exchange - enemies are neutralized, losses become gains through service", "६औं-१२औं भावको स्वामी आदानप्रदान - शत्रुहरू निष्क्रिय हुन्छन्, सेवा मार्फत घाटा लाभमा परिणत हुन्छ"),
    EXCHANGE_8_12("8th-12th lord exchange - transformation leads to liberation, hidden knowledge revealed", "८औं-१२औं भावको स्वामी आदानप्रदान - रूपान्तरणले मुक्तितर्फ लैजान्छ, लुकेको ज्ञान प्रकट हुन्छ"),

    INTERPRET_VIP_NOT_FORMED("{0} is NOT formed. The {1}th lord {2} is in {3}th sign, not in a dusthana house (6th, 8th, or 12th).", "{0} बनेको छैन। {1}औं भावको स्वामी {2} {3}औं राशिमा छ, दुस्थान भाव (६औं, ८औं वा १२औं) मा छैन।"),
    INTERPRET_VIP_FORMED("{0} ({1}) is FORMED. {2}, lord of the {3}th house, is placed in the {4}th house/sign in {5}. Yoga strength: {6}.", "{0} ({1}) बनेको छ। {3}औं भावको स्वामी {2} {4}औं भाव/राशि {5} मा स्थित छ। योग बल: {6}।"),
    
    SUMMARY_VIP_NONE("No Vipareeta Raja Yogas formed in this chart.", "यस कुण्डलीमा कुनै विपरीत राज योग बनेको छैन।"),
    SUMMARY_VIP_SINGLE("{0} is present with {1} strength.", "{0} {1} बलका साथ उपस्थित छ।"),
    SUMMARY_VIP_DOUBLE("Two Vipareeta Raja Yogas: {0}. Overall strength: {1}.", "दुई विपरीत राज योगहरू: {0}। समग्र बल: {1}।"),
    SUMMARY_VIP_TRIPLE("All three Vipareeta Raja Yogas (Harsha, Sarala, Vimala) are present - a rare and powerful combination! Overall strength: {0}.", "तीनै विपरीत राज योगहरू (हर्ष, सरल, विमल) उपस्थित छन् - एक दुर्लभ र शक्तिशाली संयोजन! समग्र बल: {0}।"),

    // ============================================
    // KEMADRUMA YOGA DETAILS
    // ============================================
    IMPACT_SEVERE("Severe", "गम्भीर"),
    IMPACT_HIGH("High", "उच्च"),
    IMPACT_MODERATE("Moderate", "मध्यम"),
    IMPACT_MILD("Mild", "हल्का"),
    IMPACT_MINIMAL("Minimal", "न्यूनतम"),
    IMPACT_POSITIVE("Positive", "सकारात्मक"),

    STRENGTH_EXALTED_KEMA("Strong (Exalted)", "बलियो (उच्च)"),
    STRENGTH_DEBILITATED_KEMA("Weak (Debilitated)", "कमजोर (नीच)"),
    STRENGTH_OWN_SIGN_KEMA("Strong (Own Sign)", "बलियो (स्वराशि)"),
    STRENGTH_MOOLATRIKONA_KEMA("Strong (Moolatrikona)", "बलियो (मूलत्रिकोण)"),
    STRENGTH_AVERAGE_KEMA("Average", "औसत"),

    ACT_MOON_FORMING("Moon is the yoga-forming planet", "चन्द्रमा योग बनाउने ग्रह हो"),
    ACT_RAHU_ANXIETY("Rahu amplifies mental anxieties", "राहुले मानसिक चिन्ता बढाउँछ"),
    ACT_SATURN_ISOLATION("Saturn increases feelings of separation", "शनिले अलगावको भावना बढाउँछ"),
    ACT_DISPOSITOR_MOON("As Moon''s dispositor, activates Moon-related yogas", "चन्द्रमाको मालिकको रूपमा, चन्द्रमा सम्बन्धी योगहरू सक्रिय गर्दछ"),

    REM_MOON_WORSHIP("Continue regular Moon worship on Mondays for emotional wellbeing", "भावनात्मक कल्याणको लागि सोमबार नियमित चन्द्र पूजा जारी राख्नुहोस्"),
    REM_MANTRA_SOM("Chant ''Om Som Somaya Namah'' or ''Om Chandraya Namah'' 108 times daily", "दैनिक १०८ पटक ''ॐ सों सोमाय नमः'' वा ''ॐ चन्द्राय नमः'' जप गर्नुहोस्"),
    REM_CHANDRA_SHANTI("Perform Chandra Shanti puja or Kemadruma Nivarana puja", "चन्द्र शान्ति पूजा वा केमद्रुम निवारण पूजा गर्नुहोस्"),
    REM_DONATE_WHITE("Donate white items (rice, milk, white cloth) on Mondays", "सोमबार सेतो वस्तुहरू (चामल, दूध, सेतो कपडा) दान गर्नुहोस्"),
    REM_FAST_MONDAY("Observe Monday fasts consuming only milk and white foods", "सोमबार केवल दूध र सेतो खाना खाएर व्रत बस्नुहोस्"),
    REM_WEAR_PEARL("Wear natural Pearl (Moti) in silver on Monday after proper energization", "उचित ऊर्जा प्रदान गरेपछि सोमबार चाँदीमा प्राकृतिक मोती धारण गर्नुहोस्"),
    REM_CHANDRA_YANTRA("Worship Chandra Yantra daily with sandalwood paste and white flowers", "चन्दन र सेतो फूलका साथ दैनिक चन्द्र यन्त्रको पूजा गर्नुहोस्"),
    REM_MOTHER_RELATION("Maintain good relationship with mother, serve elderly women, avoid isolation", "आमासँग राम्रो सम्बन्ध राख्नुहोस्, वृद्ध महिलाहरूको सेवा गर्नुहोस्, एक्लोपनबाट बच्नुहोस्"),
    REM_MEDITATION("Practice meditation and pranayama for mental peace", "मानसिक शान्तिको लागि ध्यान र प्राणायाम अभ्यास गर्नुहोस्"),

    TIMING_MONDAY_EVENING("Monday evenings", "सोमबार साँझ"),
    TIMING_MONDAY_HORA("Monday evenings, during Moon hora", "सोमबार साँझ, चन्द्र होरामा"),
    TIMING_SHUKLA_PAKSHA("On a Monday in Shukla Paksha", "शुक्ल पक्षको सोमबार"),
    TIMING_EVERY_MONDAY("Every Monday", "हरेक सोमबार"),
    TIMING_PURNIMA("Mondays, especially on Purnima", "सोमबार, विशेष गरी पूर्णिमामा"),
    TIMING_DAILY_EVENING("Daily in the evening", "दैनिक साँझमा"),
    TIMING_ONGOING("Ongoing", "निरन्तर"),
    TIMING_DAWN_DUSK("Daily, preferably at dawn and dusk", "दैनिक, प्राथमिकताका साथ सूर्योदय र सूर्यास्तमा"),

    REC_MOON_WORSHIP("Regular Moon worship is essential for emotional balance", "भावनात्मक सन्तुलनको लागि नियमित चन्द्र पूजा आवश्यक छ"),
    REC_MEDITATION_MIND("Meditation and mindfulness practices highly recommended", "ध्यान र माइन्डफुलनेस अभ्यासहरू अत्यधिक सिफारिस गरिन्छ"),
    REC_AVOID_ISOLATION("Avoid isolation - maintain social connections", "एक्लोपनबाट बच्नुहोस् - सामाजिक सम्बन्धहरू कायम राख्नुहोस्"),
    REC_NEW_MOON_CARE("Be extra careful during New Moon periods", "औंसीको समयमा अतिरिक्त सावधानी अपनाउनुहोस्"),
    REC_SHUKLA_PAKSHA("Shukla Paksha (waxing Moon) is better for important activities", "महत्त्वपूर्ण कार्यका लागि शुक्ल पक्ष (बढ्दो चन्द्रमा) उत्तम हुन्छ"),
    REC_MOON_DEBILITATED("Moon debilitated - strengthen through charity and worship", "चन्द्रमा कमजोर छ - दान र पूजा मार्फत बलियो बनाउनुहोस्"),
    REC_EMERGENCY_SAVINGS("Build emergency savings for unexpected expenses", "अप्रत्यक्षित खर्चका लागि आपतकालीन बचत बनाउनुहोस्"),
    REC_AVOID_SPECULATION("Avoid speculative investments", "सट्टा लगानीबाट बच्नुहोस्"),
    REC_STABLE_INCOME("Seek stable income sources", "स्थिर आय स्रोतहरू खोज्नुहोस्"),
    REC_FINANCIAL_PLAN("Consider financial planning consultation", "वित्तीय योजना परामर्श विचार गर्नुहोस्"),
    REC_AVOID_LOANS("Avoid loans and debts during Moon periods", "चन्द्रमाको अवधिमा ऋण र उधारोबाट बच्नुहोस्"),
    REC_MAINTAIN_FAMILY("Actively maintain family relationships", "सक्रिय रूपमा पारिवारिक सम्बन्धहरू कायम राख्नुहोस्"),
    REC_COMMUNITY_SERVICE("Join community or spiritual groups. Service to others reduces isolation effects", "सामुदायिक वा आध्यात्मिक समूहहरूमा सामेल हुनुहोस्। अरूको सेवाले एक्लोपनको प्रभावलाई कम गर्छ"),
    REC_NURTURE_RELATIONS("Prioritize relationship nurturing during Moon periods", "चन्द्रमाको अवधिमा सम्बन्ध सुधारलाई प्राथमिकता दिनुहोस्"),
    REC_EMOTIONAL_SUPPORT("Seek emotional support networks", "भावनात्मक समर्थन नेटवर्कहरू खोज्नुहोस्"),

    INTERP_KEMA_NOT_PRESENT("Kemadruma Yoga is NOT present in this chart. The Moon has adequate planetary support, indicating good emotional stability and ability to receive help from others. Mental peace and financial support from family/friends is generally available.", "यस कुण्डलीमा केमद्रुम योग छैन। चन्द्रमालाई पर्याप्त ग्रह सहयोग छ, जसले राम्रो भावनात्मक स्थिरता र अरूबाट सहयोग प्राप्त गर्ने क्षमता संकेत गर्दछ। मानसिक शान्ति र परिवार/साथीहरूबाट आर्थिक सहयोग सामान्यतया उपलब्ध हुन्छ।"),
    INTERP_KEMA_FORMED("Kemadruma Yoga IS formed in this chart as the Moon lacks planetary support in adjacent houses. ", "यस कुण्डलीमा केमद्रुम योग बनेको छ किनकि चन्द्रमालाई वरपरका भावहरूमा ग्रह सहयोगको अभाव छ। "),
    INTERP_FULLY_CANCELLED("However, the yoga is FULLY CANCELLED due to strong cancellation factors. The negative effects are largely neutralized. ", "यद्यपि, बलियो रद्द गर्ने कारकहरूको कारण योग पूर्ण रूपमा रद्द गरिएको छ। नकारात्मक प्रभावहरू धेरै हदसम्म निष्क्रिय छन्। "),
    INTERP_MOSTLY_CANCELLED("The yoga is MOSTLY cancelled with {0} cancellation factors. Negative effects are significantly reduced but some sensitivity remains. ", "यो योग {0} रद्द गर्ने कारकहरूको साथ प्रायः रद्द गरिएको छ। नकारात्मक प्रभावहरू उल्लेखनीय रूपमा कम भएका छन् तर केही संवेदनशीलता अझै बाँकी छ। "),
    INTERP_PARTIALLY_CANCELLED("The yoga is PARTIALLY cancelled. Some negative effects may manifest during challenging planetary periods. ", "यो योग आंशिक रूपमा रद्द गरिएको छ। केही नकारात्मक प्रभावहरू चुनौतीपूर्ण ग्रह अवधिहरूमा प्रकट हुन सक्छन्। "),
    INTERP_WEAKLY_CANCELLED("The yoga has WEAK cancellation. Emotional and financial challenges may arise, especially during Moon periods. ", "यस योगमा कमजोर रद्द छ। भावनात्मक र आर्थिक चुनौतीहरू आउन सक्छन्, विशेष गरी चन्द्रमाको अवधिमा। "),
    INTERP_ACTIVE_MODERATE("The yoga is ACTIVE with moderate intensity. Feelings of isolation, financial instability, and emotional sensitivity are likely. Regular Moon remedies are recommended. ", "यो योग मध्यम तीव्रताका साथ सक्रिय छ। एक्लोपनको भावना, आर्थिक अस्थिरता र भावनात्मक संवेदनशीलताको सम्भावना छ। नियमित चन्द्रमाका उपायहरू सिफारिस गरिन्छ। "),
    INTERP_ACTIVE_SEVERE("The yoga is SEVERELY ACTIVE with minimal cancellation. Significant challenges in emotional wellbeing, finances, and social support may occur. Strong remedial measures are essential. ", "यो योग न्यूनतम रद्दको साथ गम्भीर रूपमा सक्रिय छ। भावनात्मक कल्याण, वित्त र सामाजिक सहयोगमा महत्त्वपूर्ण चुनौतीहरू आउन सक्छन्। कडा उपचारात्मक उपायहरू आवश्यक छन्। "),
    INTERP_MOON_PLACEMENT("\n\nMoon is placed in {0} ({1}th house) in {2}. ", "\n\nचन्द्रमा {0} ({1}औं भाव) मा {2} नक्षत्रमा स्थित छ। "),
    INTERP_MOON_EXALTED("Moon''s exaltation provides inherent strength to handle challenges. ", "चन्द्रमाको उच्चताले चुनौतीहरू सामना गर्न अन्तर्निहित शक्ति प्रदान गर्दछ। "),
    INTERP_MOON_DEBILITATED("Moon''s debilitation intensifies emotional sensitivity. Extra care needed. ", "चन्द्रमाको नीचताले भावनात्मक संवेदनशीलता बढाउँछ। अतिरिक्त हेरचाह आवश्यक छ। "),
    INTERP_PAKSHA_BRIGHT("The {0} paksha {1} Moon indicates {2} emotional resilience. ", "{0} पक्षको {1} चन्द्रमाले {2} भावनात्मक लचिलोपन संकेत गर्दछ। "),
    INTERP_CANCELLATION_SUMMARY("\n\nCancellation factors present: {0}", "\n\nउपस्थित रद्द गर्ने कारकहरू: {0}"),

    // ============================================
    // PLANETARY YOGA DETAILS
    // ============================================
    YOGA_UTTAMADI("Uttamadi Yoga", "उत्तमादि योग"),
    YOGA_NEECHABHILASHI("Neechabhilashi Yoga", "नीचाभिलाषी योग"),
    YOGA_TAPASVI("Tapasvi Yoga", "तपस्वी योग"),
    YOGA_SURYA("Surya Yoga", "सूर्य योग"),
    YOGA_PRABHAKAR("Prabhakar Yoga", "प्रभाकर योग"),
    YOGA_PUSHKARA_NAVAMSA("Pushkara Navamsha Yoga", "पुष्कर नवांश योग"),
    YOGA_SHARADA("Sharada Yoga", "शारदा योग"),
    YOGA_CHANDRA_LAKSHMI("Chandra Lakshmi Yoga", "चंद्र लक्ष्मी योग"),
    YOGA_PURNIMA("Purnima Yoga", "पूर्णिमा योग"),
    YOGA_AMAVASYA("Amavasya Yoga", "अमावस्या योग"),
    YOGA_CHANDRADHI("Chandradhi Yoga", "चंद्राधि योग"),
    YOGA_VIJAYA("Vijaya Yoga", "विजय योग"),
    YOGA_PARAKRAMA("Parakrama Yoga", "पराक्रम योग"),
    YOGA_BHRATRI_KARAKA("Bhratri Karaka Yoga", "भ्रातृ कारक योग"),
    YOGA_VIDYA("Vidya Yoga", "विद्या योग"),
    YOGA_NIPUNA("Nipuna Yoga", "निपुण योग"),
    YOGA_VANIJYA("Vanijya Yoga", "वणिज्य योग"),
    YOGA_MATIMANTA("Matimanta Yoga", "मतिमंत योग"),
    YOGA_GURU_MANGALA("Guru Mangala Yoga", "गुरु मंगल योग"),
    YOGA_DHARMA("Dharma Yoga", "धर्म योग"),
    YOGA_BRAHMA("Brahma Yoga", "ब्रह्म योग"),
    YOGA_GANAPATI("Ganapati Yoga", "गणपति योग"),
    YOGA_BHARATHI_EXP("Bharathi Yoga", "भारती योग"),
    YOGA_KAMINI("Kamini Yoga", "कामिनी योग"),
    YOGA_KALATRA("Kalatra Yoga", "कलत्र योग"),
    YOGA_SHANI("Shani Yoga", "शनि योग"),
    YOGA_DUR("Dur Yoga", "दुर् योग"),
    YOGA_AYUSH("Ayush Yoga", "आयुष योग"),
    YOGA_RAHU_UPACHAYA("Rahu Upachaya Yoga", "राहु उपचय योग"),
    YOGA_VIJNAANA("Vijnaana Yoga", "विज्ञान योग"),
    YOGA_UTTAMA_GRAHAS("Uttama Grahas Yoga", "उत्तम ग्रह योग"),
    YOGA_SWAGRAHA("Swagraha Yoga", "स्वग्रह योग"),
    YOGA_NEECHA_GRAHAS("Neecha Grahas Yoga", "नीच ग्रह योग"),
    YOGA_VARGOTTAMA_SPEC("Vargottama {0}", "वर्गोत्तम {0}"),
    YOGA_SUBHAPATI("Subhapati Yoga", "शुभपति योग"),
    YOGA_DHANA_PAPA_KARTARI("Dhana Papa Kartari", "धन पाप कर्तरी"),
    YOGA_TRIGRAHA("Trigraha Yoga", "त्रिग्रह योग"),
    YOGA_GRAHAYUDDHA("Grahayuddha", "ग्रहयुद्ध");
}
