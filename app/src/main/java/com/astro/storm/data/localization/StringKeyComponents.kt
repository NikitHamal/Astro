package com.astro.storm.data.localization

/**
 * Localization keys for shared UI components and dialogs
 */
enum class StringKeyComponents(override val en: String, override val ne: String) : StringKeyInterface {
    
    // Placeholder to allow compilation
    PLACEHOLDER("Placeholder", "प्लेसहोल्डर"),

    // ============================================
    // PLANET SIGNIFICATIONS
    // ============================================
    // Sun
    SIG_SUN_NATURE("Malefic", "क्रूर"),
    SIG_SUN_ELEMENT("Fire", "अग्नि"),
    SIG_SUN_REP_1("Soul, Self, Ego", "आत्मा, स्व, अहंकार"),
    SIG_SUN_REP_2("Father, Authority Figures", "पिता, अधिकारिक व्यक्ति"),
    SIG_SUN_REP_3("Government, Power", "सरकार, शक्ति"),
    SIG_SUN_REP_4("Health, Vitality", "स्वास्थ्य, जीवनशक्ति"),
    SIG_SUN_REP_5("Fame, Recognition", "कीर्ति, मान्यता"),
    SIG_SUN_BODY("Heart, Spine, Right Eye, Bones", "मुटु, मेरुदण्ड, दायाँ आँखा, हड्डी"),
    SIG_SUN_PROF("Government jobs, Politics, Medicine, Administration, Leadership roles", "सरकारी जागिर, राजनीति, चिकित्सा, प्रशासन, नेतृत्व भूमिका"),

    // Moon
    SIG_MOON_NATURE("Benefic", "सौम्य"),
    SIG_MOON_ELEMENT("Water", "जल"),
    SIG_MOON_REP_1("Mind, Emotions", "मन, भावना"),
    SIG_MOON_REP_2("Mother, Nurturing", "आमा, पालनपोषण"),
    SIG_MOON_REP_3("Public, Masses", "जनता, जनसमूह"),
    SIG_MOON_REP_4("Comforts, Happiness", "सुख, खुशी"),
    SIG_MOON_REP_5("Memory, Imagination", "स्मृति, कल्पना"),
    SIG_MOON_BODY("Mind, Left Eye, Breast, Blood, Fluids", "मन, बायाँ आँखा, छाती, रगत, तरल पदार्थ"),
    SIG_MOON_PROF("Nursing, Hotel industry, Shipping, Agriculture, Psychology", " नर्सिङ, होटल उद्योग, ढुवानी, कृषि, मनोविज्ञान"),

    // Mars
    SIG_MARS_NATURE("Malefic", "क्रूर"),
    SIG_MARS_ELEMENT("Fire", "अग्नि"),
    SIG_MARS_REP_1("Energy, Action, Courage", "ऊर्जा, कार्य, साहस"),
    SIG_MARS_REP_2("Siblings, Younger Brothers", "दाजुभाइ, साना भाइहरू"),
    SIG_MARS_REP_3("Property, Land", "सम्पत्ति, जग्गा"),
    SIG_MARS_REP_4("Competition, Sports", "प्रतिस्पर्धा, खेलकुद"),
    SIG_MARS_REP_5("Technical Skills", "प्राविधिक सीप"),
    SIG_MARS_BODY("Blood, Muscles, Marrow, Head injuries", "रगत, मांसपेशी, मज्जा, टाउकोको चोट"),
    SIG_MARS_PROF("Military, Police, Surgery, Engineering, Sports, Real Estate", "सेना, प्रहरी, शल्यक्रिया, इन्जिनियरिङ, खेलकुद, घरजग्गा"),

    // Mercury
    SIG_MERCURY_NATURE("Benefic", "सौम्य"), // Can be malefic if associated with malefic
    SIG_MERCURY_ELEMENT("Earth", "पृथ्वी"),
    SIG_MERCURY_REP_1("Intelligence, Communication", "बुद्धि, सञ्चार"),
    SIG_MERCURY_REP_2("Learning, Education", "सिकाइ, शिक्षा"),
    SIG_MERCURY_REP_3("Business, Trade", "व्यापार, व्यवसाय"),
    SIG_MERCURY_REP_4("Writing, Speech", "लेखन, वाणी"),
    SIG_MERCURY_REP_5("Siblings, Friends", "दाजुभाइ, साथीहरू"),
    SIG_MERCURY_BODY("Nervous system, Skin, Speech, Hands", "स्नायु प्रणाली, छाला, वाणी, हात"),
    SIG_MERCURY_PROF("Writing, Teaching, Accounting, Trading, IT, Media", "लेखन, शिक्षण, लेखा, व्यापार, IT, मिडिया"),

    // Jupiter
    SIG_JUPITER_NATURE("Benefic", "सौम्य"),
    SIG_JUPITER_ELEMENT("Ether", "आकाश"),
    SIG_JUPITER_REP_1("Wisdom, Knowledge", "ज्ञान, विवेक"),
    SIG_JUPITER_REP_2("Teachers, Gurus", "शिक्षक, गुरु"),
    SIG_JUPITER_REP_3("Fortune, Luck", "भाग्य, संयोग"),
    SIG_JUPITER_REP_4("Children, Dharma", "सन्तान, धर्म"),
    SIG_JUPITER_REP_5("Expansion, Growth", "विस्तार, वृद्धि"),
    SIG_JUPITER_BODY("Liver, Fat tissue, Ears, Thighs", "कलेजो, बोसो, कान, जांघ"),
    SIG_JUPITER_PROF("Teaching, Law, Priesthood, Banking, Counseling", "शिक्षण, कानून, पुरोहित्याई, बैंकिङ, परामर्श"),

    // Venus
    SIG_VENUS_NATURE("Benefic", "सौम्य"),
    SIG_VENUS_ELEMENT("Water", "जल"),
    SIG_VENUS_REP_1("Love, Beauty, Art", "प्रेम, सौन्दर्य, कला"),
    SIG_VENUS_REP_2("Marriage, Relationships", "विवाह, सम्बन्ध"),
    SIG_VENUS_REP_3("Luxuries, Comforts", "विलास, आराम"),
    SIG_VENUS_REP_4("Vehicles, Pleasures", "सवारी साधन, सुख"),
    SIG_VENUS_REP_5("Creativity", "सिर्जनशीलता"),
    SIG_VENUS_BODY("Reproductive system, Face, Skin, Throat", "प्रजनन प्रणाली, अनुहार, छाला, घाँटी"),
    SIG_VENUS_PROF("Entertainment, Fashion, Art, Hospitality, Beauty industry", "मनोरञ्जन, फेसन, कला, आतिथ्य, सौन्दर्य उद्योग"),

    // Saturn
    SIG_SATURN_NATURE("Malefic", "क्रूर"),
    SIG_SATURN_ELEMENT("Air", "वायु"),
    SIG_SATURN_REP_1("Discipline, Hard work", "अनुशासन, कडा परिश्रम"),
    SIG_SATURN_REP_2("Karma, Delays", "कर्म, ढिलाइ"),
    SIG_SATURN_REP_3("Longevity, Service", "आयु, सेवा"),
    SIG_SATURN_REP_4("Laborers, Servants", "श्रमिक, सेवक"),
    SIG_SATURN_REP_5("Chronic issues", "दीर्घकालीन समस्याहरू"),
    SIG_SATURN_BODY("Bones, Teeth, Knees, Joints, Nerves", "हड्डी, दाँत, घुँडा, जोर्नी, नसा"),
    SIG_SATURN_PROF("Mining, Agriculture, Labor, Judiciary, Real Estate", "खानी, कृषि, श्रम, न्यायपालिका, घरजग्गा"),

    // Rahu
    SIG_RAHU_NATURE("Malefic", "क्रूर"),
    SIG_RAHU_ELEMENT("Air", "वायु"),
    SIG_RAHU_REP_1("Obsession, Illusion", "जुनून, भ्रम"),
    SIG_RAHU_REP_2("Foreign lands, Travel", "विदेश, यात्रा"),
    SIG_RAHU_REP_3("Technology, Innovation", "प्रविधि, नवीनता"),
    SIG_RAHU_REP_4("Unconventional paths", "अपरम्परागत मार्ग"),
    SIG_RAHU_REP_5("Material desires", "भौतिक इच्छा"),
    SIG_RAHU_BODY("Skin diseases, Nervous disorders", "छाला रोग, स्नायु विकार"),
    SIG_RAHU_PROF("Technology, Foreign affairs, Aviation, Politics, Research", "प्रविधि, परराष्ट्र मामिला, उड्डयन, राजनीति, अनुसन्धान"),

    // Ketu
    SIG_KETU_NATURE("Malefic", "क्रूर"),
    SIG_KETU_ELEMENT("Fire", "अग्नि"),
    SIG_KETU_REP_1("Spirituality, Liberation", "आध्यात्मिकता, मुक्ति"),
    SIG_KETU_REP_2("Past life karma", "पूर्व जन्म कर्म"),
    SIG_KETU_REP_3("Detachment, Isolation", "वैराग्य, एकान्त"),
    SIG_KETU_REP_4("Occult, Mysticism", "तन्त्र-मन्त्र, रहस्यवाद"),
    SIG_KETU_REP_5("Healing abilities", "उपचार क्षमता"),
    SIG_KETU_BODY("Skin, Spine, Nervous system", "छाला, मेरुदण्ड, स्नायु प्रणाली"),
    SIG_KETU_PROF("Spirituality, Research, Healing, Astrology, Philosophy", "आध्यात्मिकता, अनुसन्धान, उपचार, ज्योतिष, दर्शन"),


    // ============================================
    // HOUSE DETAILS (BHAVA)
    // ============================================
    // House 1
    HOUSE_1_NAME("Lagna Bhava (Ascendant)", "लग्न भाव"),
    HOUSE_1_TYPE("Kendra (Angular) & Trikona (Trine)", "केन्द्र र त्रिकोण"),
    HOUSE_1_SIG_1("Physical body", "भौतिक शरीर"),
    HOUSE_1_SIG_2("Personality", "व्यक्तित्व"),
    HOUSE_1_SIG_3("Self-identity", "आत्म-पहिचान"),
    HOUSE_1_SIG_4("Head and brain", "टाउको र मस्तिष्क"),
    HOUSE_1_SIG_5("General health", "सामान्य स्वास्थ्य"),
    HOUSE_1_SIG_6("Beginning of life", "जीवनको सुरुवात"),
    HOUSE_1_SIG_7("Appearance", "रूप"),
    HOUSE_1_INTERP("The First House is the most important house, representing you as a whole. It shows your physical constitution, personality traits, and how you present yourself to the world. A strong 1st house gives good health, confidence, and success in self-started ventures.", "प्रथम भाव सबैभन्दा महत्त्वपूर्ण भाव हो, जसले तपाईंलाई समग्र रूपमा प्रतिनिधित्व गर्दछ। यसले तपाईंको शारीरिक संरचना, व्यक्तित्वका विशेषताहरू, र तपाईं कसरी आफूलाई संसारमा प्रस्तुत गर्नुहुन्छ भन्ने देखाउँछ। बलियो प्रथम भावले राम्रो स्वास्थ्य, आत्मविश्वास, र आत्म-सुरु गरिएका उद्यमहरूमा सफलता दिन्छ।"),

    // House 2
    HOUSE_2_NAME("Dhana Bhava (Wealth)", "धन भाव"),
    HOUSE_2_TYPE("Maraka (Death-inflicting) & Panapara", "मारक र पणफर"),
    HOUSE_2_SIG_1("Wealth & Possessions", "धन र सम्पत्ति"),
    HOUSE_2_SIG_2("Family", "परिवार"),
    HOUSE_2_SIG_3("Speech", "वाणी"),
    HOUSE_2_SIG_4("Right eye", "दायाँ आँखा"),
    HOUSE_2_SIG_5("Face", "अनुहार"),
    HOUSE_2_SIG_6("Food intake", "भोजन"),
    HOUSE_2_SIG_7("Early childhood", "प्रारम्भिक बाल्यकाल"),
    HOUSE_2_INTERP("The Second House governs accumulated wealth, family values, and speech. It shows how you earn and save money, your relationship with family, and your communication style. A strong 2nd house indicates financial stability and sweet speech.", "द्वितीय भावले संचित धन, पारिवारिक मूल्यमान्यता, र वाणीलाई नियन्त्रण गर्दछ। यसले तपाईं कसरी पैसा कमाउनुहुन्छ र बचत गर्नुहुन्छ, परिवारसँगको तपाईंको सम्बन्ध, र तपाईंको सञ्चार शैलीलाई देखाउँछ। बलियो द्वितीय भावले आर्थिक स्थिरता र मीठो बोलीको संकेत गर्दछ।"),

    // House 3
    HOUSE_3_NAME("Sahaja Bhava (Siblings)", "सहज भाव (दाजुभाइ)"),
    HOUSE_3_TYPE("Upachaya (Growth) & Apoklima", "उपचय र आपोक्लिम"),
    HOUSE_3_SIG_1("Siblings", "दाजुभाइ"),
    HOUSE_3_SIG_2("Courage", "साहस"),
    HOUSE_3_SIG_3("Short journeys", "छोटो यात्रा"),
    HOUSE_3_SIG_4("Communication", "सञ्चार"),
    HOUSE_3_SIG_5("Arms and shoulders", "हात र काँध"),
    HOUSE_3_SIG_6("Neighbors", "छिमेकीहरू"),
    HOUSE_3_SIG_7("Hobbies", "रुचिहरू"),
    HOUSE_3_INTERP("The Third House represents courage, initiative, and self-effort. It governs siblings (especially younger), short travels, and all forms of communication. A strong 3rd house gives courage, good relationships with siblings, and success through personal effort.", "तृतीय भावले साहस, पहल, र आत्म-प्रयासलाई प्रतिनिधित्व गर्दछ। यसले दाजुभाइ (विशेष गरी कान्छा), छोटो यात्राहरू, र सञ्चारका सबै रूपहरूलाई नियन्त्रण गर्दछ। बलियो तृतीय भावले साहस, दाजुभाइसँग राम्रो सम्बन्ध, र व्यक्तिगत प्रयासबाट सफलता दिन्छ।"),

    // House 4
    HOUSE_4_NAME("Sukha Bhava (Happiness)", "सुख भाव"),
    HOUSE_4_TYPE("Kendra (Angular)", "केन्द्र"),
    HOUSE_4_SIG_1("Mother", "आमा"),
    HOUSE_4_SIG_2("Home & Property", "घर र सम्पत्ति"),
    HOUSE_4_SIG_3("Vehicles", "सवारी साधन"),
    HOUSE_4_SIG_4("Education", "शिक्षा"),
    HOUSE_4_SIG_5("Chest & Heart", "छाती र मुटु"),
    HOUSE_4_SIG_6("Inner peace", "आन्तरिक शान्ति"),
    HOUSE_4_SIG_7("Emotional foundation", "भावनात्मक आधार"),
    HOUSE_4_INTERP("The Fourth House is the foundation of your life. It represents your mother, home environment, and emotional security. It also governs formal education and landed property. A strong 4th house gives domestic happiness, property ownership, and mental peace.", "चतुर्थ भाव तपाईंको जीवनको जग हो। यसले तपाईंको आमा, घरको वातावरण, र भावनात्मक सुरक्षालाई प्रतिनिधित्व गर्दछ। यसले औपचारिक शिक्षा र जग्गा जमिनलाई पनि नियन्त्रण गर्दछ। बलियो चतुर्थ भावले घरायसी सुख, सम्पत्ति स्वामित्व, र मानसिक शान्ति दिन्छ।"),

    // House 5
    HOUSE_5_NAME("Putra Bhava (Children)", "पुत्र भाव"),
    HOUSE_5_TYPE("Trikona (Trine) & Panapara", "त्रिकोण र पणफर"),
    HOUSE_5_SIG_1("Children", "सन्तान"),
    HOUSE_5_SIG_2("Intelligence", "बुद्धि"),
    HOUSE_5_SIG_3("Creativity", "सिर्जनशीलता"),
    HOUSE_5_SIG_4("Romance", "प्रेम"),
    HOUSE_5_SIG_5("Past life merit", "पूर्व जन्म पुण्य"),
    HOUSE_5_SIG_6("Speculation", "सट्टा"),
    HOUSE_5_SIG_7("Higher education", "उच्च शिक्षा"),
    HOUSE_5_INTERP("The Fifth House is the house of creativity and Purva Punya (past life merits). It governs children, intelligence, romance, and speculative gains. A strong 5th house gives intelligent children, creative talents, and success in speculation.", "पञ्चम भाव सिर्जनशीलता र पूर्व पुण्यको घर हो। यसले सन्तान, बुद्धि, प्रेम, र सट्टा लाभलाई नियन्त्रण गर्दछ। बलियो पञ्चम भावले बुद्धिमान सन्तान, रचनात्मक प्रतिभा, र सट्टामा सफलता दिन्छ।"),

    // House 6
    HOUSE_6_NAME("Ripu Bhava (Enemies)", "रिपु भाव (शत्रु)"),
    HOUSE_6_TYPE("Dusthana (Malefic) & Upachaya", "दुष्ट र उपचय"),
    HOUSE_6_SIG_1("Enemies", "शत्रु"),
    HOUSE_6_SIG_2("Diseases", "रोग"),
    HOUSE_6_SIG_3("Debts", "ऋण"),
    HOUSE_6_SIG_4("Service", "सेवा"),
    HOUSE_6_SIG_5("Competition", "प्रतिस्पर्धा"),
    HOUSE_6_SIG_6("Daily work", "दैनिक काम"),
    HOUSE_6_SIG_7("Maternal uncle", "मामा"),
    HOUSE_6_INTERP("The Sixth House governs obstacles, health issues, and service. While considered malefic, it also shows the ability to overcome challenges. A well-placed 6th house gives victory over enemies, good health practices, and success in competitive fields.", "ठैठौं भावले बाधा, स्वास्थ्य समस्या, र सेवालाई नियन्त्रण गर्दछ। यद्यपि यसलाई अशुभ मानिन्छ, यसले चुनौतीहरू पार गर्ने क्षमता पनि देखाउँछ। राम्रो स्थितिमा रहेको छैठौं भावले शत्रुमाथि विजय, राम्रो स्वास्थ्य अभ्यास, र प्रतिस्पर्धात्मक क्षेत्रमा सफलता दिन्छ।"),

    // House 7
    HOUSE_7_NAME("Kalatra Bhava (Spouse)", "कलत्र भाव (जीवनसाथी)"),
    HOUSE_7_TYPE("Kendra (Angular) & Maraka", "केन्द्र र मारक"),
    HOUSE_7_SIG_1("Marriage", "विवाह"),
    HOUSE_7_SIG_2("Spouse", "जीवनसाथी"),
    HOUSE_7_SIG_3("Business partnerships", "व्यापार साझेदारी"),
    HOUSE_7_SIG_4("Foreign travel", "विदेश यात्रा"),
    HOUSE_7_SIG_5("Public dealing", "सार्वजनिक कारोबार"),
    HOUSE_7_SIG_6("Lower abdomen", "तल्लो पेट"),
    HOUSE_7_SIG_7("Sexual organs", "यौन अंग"),
    HOUSE_7_INTERP("The Seventh House is the house of partnerships and marriage. It shows your spouse's nature and quality of marriage. It also governs business partnerships and public dealings. A strong 7th house gives a good spouse and success in partnerships.", "सप्तम भाव साझेदारी र विवाहको घर हो। यसले तपाईंको जीवनसाथीको स्वभाव र विवाहको गुणस्तर देखाउँछ। यसले व्यापार साझेदारी र सार्वजनिक व्यवहारलाई पनि नियन्त्रण गर्दछ। बलियो सप्तम भावले असल जीवनसाथी र साझेदारीमा सफलता दिन्छ।"),

    // House 8
    HOUSE_8_NAME("Ayur Bhava (Longevity)", "आयु भाव"),
    HOUSE_8_TYPE("Dusthana (Malefic) & Panapara", "दुष्ट र पणफर"),
    HOUSE_8_SIG_1("Longevity", "आयु"),
    HOUSE_8_SIG_2("Transformation", "रूपान्तरण"),
    HOUSE_8_SIG_3("Occult", "तन्त्र-मन्त्र"),
    HOUSE_8_SIG_4("Inheritance", "पैतृक सम्पत्ति"),
    HOUSE_8_SIG_5("Hidden matters", "गुप्त निषय"),
    HOUSE_8_SIG_6("Chronic diseases", "दीर्घकालीन रोग"),
    HOUSE_8_SIG_7("In-laws' wealth", "ससुरालको धन"),
    HOUSE_8_INTERP("The Eighth House governs transformation, death, and rebirth (metaphorical). It shows hidden matters, inheritance, and occult interests. While considered difficult, a well-placed 8th house gives longevity, research abilities, and unexpected gains.", "अष्टम भावले रूपान्तरण, मृत्यु, र पुनर्जन्म (लाक्षणिक) लाई नियन्त्रण गर्दछ। यसले गुप्त कुराहरू, उत्तराधिकार, र तन्त्र-मन्त्र चासोहरू देखाउँछ। यद्यपि यसलाई कठिन मानिन्छ, राम्रो स्थितिमा रहेको ८औं भावले दीर्घायु, अनुसन्धान क्षमता, र अप्रत्याशित लाभ दिन्छ।"),

    // House 9
    HOUSE_9_NAME("Dharma Bhava (Fortune)", "धर्म भाव (भाग्य)"),
    HOUSE_9_TYPE("Trikona (Trine) & Apoklima", "त्रिकोण र आपोक्लिम"),
    HOUSE_9_SIG_1("Fortune & Luck", "भाग्य"),
    HOUSE_9_SIG_2("Father", "पिता"),
    HOUSE_9_SIG_3("Higher learning", "उच्च शिक्षा"),
    HOUSE_9_SIG_4("Long journeys", "लामो यात्रा"),
    HOUSE_9_SIG_5("Religion & Philosophy", "धर्म र दर्शन"),
    HOUSE_9_SIG_6("Guru/Teacher", "गुरु/शिक्षक"),
    HOUSE_9_SIG_7("Righteousness", "धार्मिकता"),
    HOUSE_9_INTERP("The Ninth House is the most auspicious house of fortune and dharma. It represents your father, teachers, and higher wisdom. A strong 9th house gives good fortune, philosophical inclinations, and blessings from elders and teachers.", "नवम भाव भाग्य र धर्मको सबैभन्दा शुभ घर हो। यसले तपाईंको पिता, शिक्षक, र उच्च ज्ञानलाई प्रतिनिधित्व गर्दछ। बलियो नवम भावले राम्रो भाग्य, दार्शनिक झुकाव, र मान्यजन तथा शिक्षकहरूबाट आशीर्वाद दिन्छ।"),

    // House 10
    HOUSE_10_NAME("Karma Bhava (Career)", "कर्म भाव"),
    HOUSE_10_TYPE("Kendra (Angular) & Upachaya", "केन्द्र र उपचय"),
    HOUSE_10_SIG_1("Career", "क्यारियर"),
    HOUSE_10_SIG_2("Profession", "पेशा"),
    HOUSE_10_SIG_3("Status & Fame", "स्थिति र कीर्ति"),
    HOUSE_10_SIG_4("Authority", "अधिकार"),
    HOUSE_10_SIG_5("Government", "सरकार"),
    HOUSE_10_SIG_6("Father", "पिता"), // Also 9th
    HOUSE_10_SIG_7("Knees", "घुँडा"),
    HOUSE_10_INTERP("The Tenth House is the house of career and public image. It shows your profession, status in society, and relationship with authority. A strong 10th house gives career success, fame, and high position in society.", "दशम भाव क्यारियर र सार्वजनिक छविको घर हो। यसले तपाईंको पेशा, समाजमा स्थिति, र अधिकारसँगको सम्बन्ध देखाउँछ। बलियो दशम भावले क्यारियर सफलता, कीर्ति, र समाजमा उच्च पद दिन्छ।"),

    // House 11
    HOUSE_11_NAME("Labha Bhava (Gains)", "लाभ भाव"),
    HOUSE_11_TYPE("Upachaya (Growth) & Panapara", "उपचय र पणफर"),
    HOUSE_11_SIG_1("Income & Gains", "आम्दानी र लाभ"),
    HOUSE_11_SIG_2("Elder siblings", "दाजुदिदी"),
    HOUSE_11_SIG_3("Friends", "साथीहरू"),
    HOUSE_11_SIG_4("Hopes & Wishes", "आशा र इच्छाहरू"),
    HOUSE_11_SIG_5("Social network", "सामाजिक सञ्जाल"),
    HOUSE_11_SIG_6("Left ear", "बायाँ कान"),
    HOUSE_11_SIG_7("Ankles", "कुर्कुच्चा"),
    HOUSE_11_INTERP("The Eleventh House is the house of gains and fulfillment of desires. It governs income, elder siblings, and friendships. A strong 11th house gives multiple sources of income, supportive friends, and fulfillment of hopes.", "एकादश भाव लाभ र इच्छाहरू पूरा गर्ने घर हो। यसले आम्दानी, दाजुदिदी, र मित्रतालाई नियन्त्रण गर्दछ। बलियो ११औं भावले आम्दानीका धेरै स्रोतहरू, सहयोगी साथीहरू, र आशाहरूको पूर्ति दिन्छ।"),

    // House 12
    HOUSE_12_NAME("Vyaya Bhava (Loss)", "व्यय भाव"),
    HOUSE_12_TYPE("Dusthana (Malefic) & Apoklima", "दुष्ट र आपोक्लिम"),
    HOUSE_12_SIG_1("Losses & Expenses", "हानि र खर्च"),
    HOUSE_12_SIG_2("Liberation (Moksha)", "मुक्ति (मोक्ष)"),
    HOUSE_12_SIG_3("Foreign lands", "विदेश"),
    HOUSE_12_SIG_4("Isolation", "एकान्त"),
    HOUSE_12_SIG_5("Feet", "खुट्टा"),
    HOUSE_12_SIG_6("Sleep", "निद्रा"),
    HOUSE_12_SIG_7("Subconscious", "अवचेतन"),
    HOUSE_12_INTERP("The Twelfth House governs losses, expenses, and liberation. While it shows material losses, it also represents spiritual gains and final liberation. A strong 12th house gives spiritual inclinations, success abroad, and peaceful sleep.", "द्वादश भावले हानि, खर्च, र मुक्तिलाई नियन्त्रण गर्दछ। यद्यपि यसले भौतिक हानि देखाउँछ, यसले आध्यात्मिक लाभ र अन्तिम मुक्तिलाई पनि प्रतिनिधित्व गर्दछ। बलियो १२औं भावले आध्यात्मिक झुकाव, विदेशमा सफलता, र शान्तिपूर्ण निद्रा दिन्छ।"),

    // House Interp Templates
    HOUSE_PLACEMENT_1("Strong personality with natural leadership abilities. You have a prominent presence and strong willpower. May indicate good health and vitality.", "प्राकृतिक नेतृत्व क्षमताको साथ बलियो व्यक्तित्व। तपाईंसँग प्रमुख उपस्थिति र बलियो इच्छाशक्ति छ। राम्रो स्वास्थ्य र जीवनशक्ति संकेत गर्न सक्छ।"),
    HOUSE_PLACEMENT_10("Excellent position for career success and recognition. Natural authority in professional life. Government positions or leadership roles favored.", "क्यारियर सफलता र मान्यताको लागि उत्कृष्ट स्थिति। व्यापारिक जीवनमा प्राकृतिक अधिकार। सरकारी पद वा नेतृत्व भूमिकाहरू अनुकूल।"),
    HOUSE_PLACEMENT_MOON_4("Strong emotional foundation and attachment to home. Good relationship with mother. Domestic happiness and property gains likely.", "बलियो भावनात्मक आधार र घरसँग लगाव। आमासँग राम्रो सम्बन्ध। घरायसी सुख र सम्पत्ति लाभको सम्भावना।"),
    HOUSE_PLACEMENT_MOON_1("Emotional and intuitive personality. Strong connection to feelings. Popular with the public and adaptable nature.", "भावनात्मक र अन्तर्ज्ञानी व्यक्तित्व। भावनाहरूसँग बलियो सम्बन्ध। जनतासँग लोकप्रिय र अनुकूलनीय स्वभाव।"),
    HOUSE_PLACEMENT_MARS_10("Dynamic career with technical or engineering success. Leadership in competitive fields. Achievement through bold actions.", "प्राविधिक वा इन्जिनियरिङ सफलताको साथ गतिशील क्यारियर। प्रतिस्पर्धात्मक क्षेत्रहरूमा नेतृत्व। साहसी कार्यहरू मार्फत उपलब्धि।"),
    HOUSE_PLACEMENT_MARS_1("Energetic and courageous personality. Athletic abilities. Can be aggressive or impulsive. Strong drive for success.", "ऊर्जावान् र साहसी व्यक्तित्व। एथलेटिक क्षमताहरू। आक्रामक वा आवेगपूर्ण हुन सक्छ। सफलताको लागि बलियो प्रेरणा।"),
    HOUSE_PLACEMENT_MERCURY_1("Intelligent and communicative personality. Good business sense. Quick thinking and versatile nature.", "बुद्धिमान र मिलनसार व्यक्तित्व। राम्रो व्यापारिक समझ। छिटो सोच र बहुमुखी स्वभाव।"),
    HOUSE_PLACEMENT_MERCURY_5("Creative intelligence and good with children. Success in speculation and education. Artistic communication skills.", "रचनात्मक बुद्धि र बच्चाहरू सँग राम्रो। सट्टा र शिक्षामा सफलता। कलात्मक सञ्चार कौशल।"),
    HOUSE_PLACEMENT_JUPITER_1("Wise and optimistic personality. Natural teacher or advisor. Good fortune and ethical nature. Respected by others.", "बुद्धिमान र आशावादी व्यक्तित्व। प्राकृतिक शिक्षक वा सल्लाहकार। राम्रो भाग्य र नैतिक स्वभाव। अरूबाट सम्मानित।"),
    HOUSE_PLACEMENT_JUPITER_9("Excellent position for spiritual growth and higher learning. Good fortune with father and long journeys. Success in teaching or law.", "आध्यात्मिक वृद्धि र उच्च शिक्षाको लागि उत्कृष्ट स्थिति। पिता र लामो यात्रासँग राम्रो भाग्य। शिक्षण वा कानूनमा सफलता।"),
    HOUSE_PLACEMENT_VENUS_7("Beautiful spouse and harmonious marriage. Success in partnerships and business. Diplomatic abilities.", "सुन्दर जीवनसाथी र सामञ्जस्यपूर्ण विवाह। साझेदारी र व्यापारमा सफलता। कूटनीतिक क्षमताहरू।"),
    HOUSE_PLACEMENT_VENUS_4("Luxurious home and vehicles. Good relationship with mother. Domestic happiness and artistic home environment.", "विलासी घर र सवारी साधन। आमासँग राम्रो सम्बन्ध। घरायसी सुख र कलात्मक घरको वातावरण।"),
    HOUSE_PLACEMENT_SATURN_10("Slow but steady rise in career. Success through hard work and persistence. Authority gained through discipline.", "क्यारियरमा सुस्त तर स्थिर वृद्धि। कडा परिश्रम र लगनशीलता मार्फत सफलता। अनुशासन मार्फत प्राप्त अधिकार।"),
    HOUSE_PLACEMENT_SATURN_7("Delayed marriage but stable. Serious approach to partnerships. May marry someone older or more mature.", "विवाहमा ढिलाइ तर स्थिर। साझेदारीमा गम्भीर दृष्टिकोण। आफूभन्दा जेठो वा बढी परिपक्व व्यक्तिसँग विवाह गर्न सक्छ।"),
    HOUSE_PLACEMENT_RAHU_10("Unconventional career path. Success in foreign lands or technology. Ambitious and worldly.", "अपरम्परागत क्यारियर मार्ग। विदेश वा प्रविधिमा सफलता। महत्त्वाकांक्षी र सांसारिक।"),
    HOUSE_PLACEMENT_KETU_12("Strong spiritual inclinations. Interest in meditation and liberation. May spend time in foreign lands or ashrams.", "बलियो आध्यात्मिक झुकाव। ध्यान र मुक्तिमा रुचि। विदेश वा आश्रममा समय बिताउन सक्छ।"),
    HOUSE_PLACEMENT_DEFAULT("The %1\$s in the %2\$dth house influences the areas of %3\$s. Results depend on the sign placement, aspects, and overall chart strength.", "%2\$dऔं भावमा %1\$sले %3\$s का क्षेत्रहरूलाई प्रभाव पार्छ। परिणामहरू राशि स्थिति, दृष्टि, र समग्र कुण्डली बलमा निर्भर हुन्छन्।"),

    // ============================================
    // NAKSHATRA DETAILS
    // ============================================
    // Ashwini
    NAK_ASHWINI_SYMBOL("Horse's Head", "घोडाको टाउको"),
    NAK_ASHWINI_NATURE("Swift (Kshipra)", "क्षिप्रा (तीव्र)"),

    // ============================================
    // TIMEZONE SELECTOR
    // ============================================
    TIMEZONE_SELECT_TITLE("Select Timezone", "समयक्षेत्र छान्नुहोस्"),
    TIMEZONE_SEARCH_PLACEHOLDER("Search by city, region, or UTC offset...", "शहर, क्षेत्र, वा UTC खोज्नुहोस्..."),
    TIMEZONE_COMMON("Common", "सामान्य"),
    TIMEZONE_ALL("All Timezones", "सबै समयक्षेत्रहरू"),
    TIMEZONE_NONE_FOUND("No timezones found", "समयक्षेत्र फेला परेन"),
    TIMEZONE_COUNT_FORMAT("%d timezones", "%d समयक्षेत्रहरू"),
    TIMEZONE_LABEL("Timezone", "समयक्षेत्र"),
    TIMEZONE_CHANGE_ACTION("Change timezone", "समयक्षेत्र परिवर्तन गर्नुहोस्"),
    TIMEZONE_CLEAR_ACTION("Clear", "हटाउनुहोस्"),
    TIMEZONE_CLEAR_SEARCH_DESC("Clear search", "खोजी हटाउनुहोस्"),
    TIMEZONE_CLOSE_DESC("Close", "बन्द गर्नुहोस्"),

    // ============================================
    // CARDS & UI ELEMENTS
    // ============================================
    ACTION_EXPAND("Expand", "विस्तार गर्नुहोस्"),
    ACTION_COLLAPSE("Collapse", "सङ्कुचन गर्नुहोस्"),
    ACTION_INFO("Info", "जानकारी"),
    NAK_ASHWINI_GUNA("Rajas", "राजस"),
    NAK_ASHWINI_ELEMENT("Earth", "पृथ्वी"),
    NAK_ASHWINI_CHARS("Ashwini natives are quick, energetic, and pioneering. They have natural healing abilities and are often the first to try new things. Speed and initiative are their hallmarks.", "अश्विनी नक्षत्रका जातकहरू छिटो, ऊर्जावान् र अग्रणी हुन्छन्। उनीहरूमा प्राकृतिक उपचार क्षमता हुन्छ र अक्सर नयाँ कुराहरू प्रयास गर्ने पहिलो हुन्छन्। गति र पहल उनीहरूको पहिचान हो।"),
    NAK_ASHWINI_CAREERS("Medical field, Emergency services, Sports, Transportation, Veterinary science", "चिकित्सा क्षेत्र, आपतकालीन सेवा, खेलकुद, यातायात, पशु चिकित्सा"),

    // Bharani
    NAK_BHARANI_SYMBOL("Yoni (Female reproductive organ)", "योनि (महिला प्रजनन अंग)"),
    NAK_BHARANI_NATURE("Fierce (Ugra)", "उग्र"),
    NAK_BHARANI_GENDER("Female", "महिला"),
    NAK_BHARANI_GANA("Manushya (Human)", "मनुष्य (मानव)"),
    NAK_BHARANI_GUNA("Rajas", "राजस"),
    NAK_BHARANI_ELEMENT("Earth", "पृथ्वी"),
    NAK_BHARANI_CHARS("Bharani natives are creative, responsible, and can bear heavy burdens. They understand life's transformative nature and often work with matters of birth, death, and transformation.", "भरणी नक्षत्रका जातकहरू सिर्जनशील, जिम्मेवार हुन्छन् र ठूलो भारी बोक्न सक्छन्। उनीहरू जीवनको परिवर्तनकारी स्वभाव बुझ्छन् र अक्सर जन्म, मृत्यु र रूपान्तरणका विषयहरूमा काम गर्छन्।"),
    NAK_BHARANI_CAREERS("Midwifery, Funeral services, Entertainment, Creative arts, Psychology", "सुडेनी काम, अन्न्त्येष्टि सेवा, मनोरञ्जन, रचनात्मक कला, मनोविज्ञान"),

    // Rohini
    NAK_ROHINI_SYMBOL("Ox Cart / Chariot", "गोरु गाडा / रथ"),
    NAK_ROHINI_NATURE("Fixed (Dhruva)", "ध्रुव (स्थिर)"),
    NAK_ROHINI_GENDER("Female", "महिला"),
    NAK_ROHINI_GANA("Manushya (Human)", "मनुष्य (मानव)"),
    NAK_ROHINI_GUNA("Rajas", "राजस"),
    NAK_ROHINI_ELEMENT("Earth", "पृथ्वी"),
    NAK_ROHINI_CHARS("Rohini natives are attractive, artistic, and materialistic in a positive way. They appreciate beauty and luxury. Strong creative and productive abilities.", "रोहिणी नक्षत्रका जातकहरू आकर्षक, कलात्मक र सकारात्मक रूपमा भौतिकवादी हुन्छन्। उनीहरू सुन्दरता र विलासको कदर गर्छन्। बलियो रचनात्मक र उत्पादक क्षमताहरू।"),
    NAK_ROHINI_CAREERS("Fashion, Beauty industry, Agriculture, Music, Hospitality", "फेसन, सौन्दर्य उद्योग, कृषि, संगीत, आतिथ्य"),

    // Defaults / Fallbacks
    NAK_NATURE_MIXED("Mixed", "मिश्रित"),
    NAK_GENDER_NEUTRAL("Neutral", "तटस्थ"),
    NAK_GANA_MIXED("Mixed", "मिश्रित"),
    NAK_GUNA_MIXED("Mixed", "मिश्रित"),
    NAK_ELEMENT_MIXED("Mixed", "मिश्रित"),
    NAK_CHARS_DEFAULT("%1\$s is ruled by %2\$s. Natives are influenced by the deity %3\$s.", "%1\$s %2\$s द्वारा शासित छ। जातकहरू देवता %3\$s द्वारा प्रभावित हुन्छन्।"),
    NAK_CAREERS_DEFAULT("Various fields depending on overall chart analysis", "समग्र कुण्डली विश्लेषणमा निर्भर विभिन्न क्षेत्रहरू"),
    
    // Pada Description
    PADA_DESC_TEMPLATE("Pada %1\$d falls in %2\$s Navamsa, ruled by %3\$s. This pada emphasizes the %4\$s element qualities combined with the main nakshatra characteristics.", "पाउ %1\$d %2\$s नवअंशमा पर्दछ, जसको स्वामी %3\$s हो। यो पाउले मुख्य नक्षत्र विशेषताहरूसँग मिलाएर %4\$s (तत्व) गुणहरूलाई जोड दिन्छ।");

}
