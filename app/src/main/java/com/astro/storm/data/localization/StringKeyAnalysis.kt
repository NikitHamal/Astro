package com.astro.storm.data.localization

import com.astro.storm.data.model.Planet

enum class StringKeyAnalysis(override val en: String, override val ne: String) : StringKeyInterface {
    // ============================================
    // PANCH MAHAPURUSHA YOGA ANALYSIS
    // ============================================
    PANCH_MAHAPURUSHA_YOGA("Panch Mahapurusha Yoga", "पञ्च महापुरुष योग"),
    PANCH_MAHAPURUSHA_YOGA_DESC("Analysis of the five great yogas", "पाँच महायोगहरूको विश्लेषण"),

    // Yoga Types
    YOGA_TYPE_RUCHAKA("Ruchaka Yoga", "रूचक योग"),
    YOGA_TYPE_BHADRA("Bhadra Yoga", "भद्र योग"),
    YOGA_TYPE_HAMSA("Hamsa Yoga", "हंस योग"),
    YOGA_TYPE_MALAVYA("Malavya Yoga", "मालव्य योग"),
    YOGA_TYPE_SASHA("Sasha Yoga", "शश योग"),

    // UI Labels
    YOGA_PLANET("Planet", "ग्रह"),
    YOGA_FORMED_IN("Formed in", "मा बनेको"),
    YOGA_STRENGTH("Strength", "शक्ति"),
    YOGA_RECOMMENDATIONS("Recommendations", "सिफारिसहरू"),
    YOGA_PRIMARY_BLESSINGS("Primary Blessings", "मुख्य आशीर्वादहरू"),
    YOGA_SUITABLE_CAREERS("Suitable Careers", "उपयुक्त करियरहरू"),
    YOGA_DEITY("Deity", "देवता"),
    YOGA_GEMSTONE("Gemstone", "रत्न"),
    YOGA_MANTRA("Mantra", "मन्त्र"),
    YOGA_CHARITY("Charity Items", "दानका वस्तुहरू"),

    // Planet Dignity
    DIGNITY_EXALTED("Exalted", "उच्च"),
    DIGNITY_OWN_SIGN("Own Sign", "स्वराशि"),
    DIGNITY_MOOLATRIKONA("Moolatrikona", "मूलत्रिकोण"),
    DIGNITY_FRIEND("Friendly Sign", "मित्र राशि"),
    DIGNITY_NEUTRAL("Neutral Sign", "सम राशि"),
    DIGNITY_ENEMY("Enemy Sign", "शत्रु राशि"),
    DIGNITY_DEBILITATED("Debilitated", "नीच"),

    // Yoga Strength Level
    STRENGTH_EXCEPTIONAL("Exceptional (80-100%)", "असाधारण (८०-१००%)"),
    STRENGTH_STRONG("Strong (60-79%)", "बलियो (६०-७९%)"),
    STRENGTH_MODERATE("Moderate (40-59%)", "मध्यम (४०-५९%)"),
    STRENGTH_WEAK("Weak (20-39%)", "कमजोर (२०-३९%)"),
    STRENGTH_VERY_WEAK("Very Weak (0-19%)", "धेरै कमजोर (०-१९%)"),

    // Recommendation Type
    REC_TYPE_STRENGTHENING("Strengthening Practice", "सशक्तिकरण अभ्यास"),
    REC_TYPE_CAREER("Career Alignment", "करियर संरेखण"),
    REC_TYPE_GEMSTONE("Gemstone Recommendation", "रत्न सिफारिस"),
    REC_TYPE_MANTRA("Mantra Recitation", "मन्त्र जप"),
    REC_TYPE_CHARITY("Charitable Practice", "दान अभ्यास"),

    // Ruchaka Yoga Details
    RUCHAKA_DESC("Ruchaka Yoga is formed when Mars is in Kendra in its own sign (Aries/Scorpio) or exaltation (Capricorn). It bestows courage, leadership abilities, physical strength, and success in competitive fields. The native has a commanding presence and excels where bravery and initiative are required.", "रूचक योग मंगल ग्रह केन्द्रमा स्वराशि (मेष/वृश्चिक) वा उच्च (मकर) मा हुँदा बन्छ। यसले साहस, नेतृत्व क्षमता, शारीरिक शक्ति, र प्रतिस्पर्धात्मक क्षेत्रमा सफलता प्रदान गर्दछ। जातकको प्रभावशाली व्यक्तित्व हुन्छ र बहादुरी र पहल आवश्यक पर्ने ठाउँमा उत्कृष्ट हुन्छ।"),
    RUCHAKA_SHORT_DESC("Courage, leadership, physical prowess", "साहस, नेतृत्व, शारीरिक पराक्रम"),
    RUCHAKA_BLESSINGS("Courage, Leadership, Physical strength, Victory over enemies, Land/property", "साहस, नेतृत्व, शारीरिक शक्ति, शत्रुहरूमाथि विजय, भूमि/सम्पत्ति"),
    RUCHAKA_DEITY("Lord Hanuman or Lord Kartikeya", "भगवान हनुमान वा भगवान कार्तिकेय"),
    RUCHAKA_GEMSTONE("Red Coral (Moonga)", "रातो मुगा (मूंगा)"),
    RUCHAKA_MANTRA("Om Kraam Kreem Kraum Sah Bhaumaya Namah", "ॐ क्रां क्रीं क्रौं सः भौमाय नमः"),
    RUCHAKA_CHARITY("red lentils, red cloth, jaggery to the needy", "रातो दाल, रातो कपडा, खा needy्दोलाई गुड"),
    RUCHAKA_CAREERS("Military, Police, Sports, Surgery, Engineering, Real estate, Martial arts", "सेना, पुलिस, खेलकुद, शल्य चिकित्सा, इन्जिनियरिङ, घर जग्गा, मार्शल आर्ट"),

    // Bhadra Yoga Details
    BHADRA_DESC("Bhadra Yoga is formed when Mercury is in Kendra in its own sign (Gemini/Virgo) or exaltation (Virgo). It bestows exceptional intelligence, communication skills, business acumen, and scholarly abilities. The native excels in intellectual pursuits and has a youthful, adaptable nature.", "भद्र योग बुध ग्रह केन्द्रमा स्वराशि (मिथुन/कन्या) वा उच्च (कन्या) मा हुँदा बन्छ। यसले असाधारण बुद्धि, सञ्चार कौशल, व्यापारिक कौशल, र विद्वतापूर्ण क्षमताहरू प्रदान गर्दछ। जातक बौद्धिक कार्यहरूमा उत्कृष्ट हुन्छ र जवान, अनुकूलनीय स्वभावको हुन्छ।"),
    BHADRA_SHORT_DESC("Intelligence, communication, commerce", "बुद्धि, सञ्चार, वाणिज्य"),
    BHADRA_BLESSINGS("Intelligence, Eloquence, Business success, Learning ability, Youthfulness", "बुद्धि, वाक्पटुता, व्यापारमा सफलता, सिक्ने क्षमता, यौवन"),
    BHADRA_DEITY("Lord Vishnu or Lord Ganesha", "भगवान विष्णु वा भगवान गणेश"),
    BHADRA_GEMSTONE("Emerald (Panna)", "पन्ना"),
    BHADRA_MANTRA("Om Braam Breem Braum Sah Budhaya Namah", "ॐ ब्रां ब्रीं ब्रौं सः बुधाय नमः"),
    BHADRA_CHARITY("green moong dal, green cloth, books to students", "हरियो मूंग दाल, हरियो कपडा, विद्यार्थीहरूलाई पुस्तकहरू"),
    BHADRA_CAREERS("Writing, Teaching, Commerce, Accounting, IT, Journalism, Astrology", "लेखन, शिक्षण, वाणिज्य, लेखा, आईटी, पत्रकारिता, ज्योतिष"),

    // Hamsa Yoga Details
    HAMSA_DESC("Hamsa Yoga is formed when Jupiter is in Kendra in its own sign (Sagittarius/Pisces) or exaltation (Cancer). It bestows wisdom, righteousness, fortune, and spiritual inclination. The native is noble-minded, generous, and respected in society as a wise counselor.", "हंस योग बृहस्पति ग्रह केन्द्रमा स्वराशि (धनु/मीन) वा उच्च (कर्कट) मा हुँदा बन्छ। यसले बुद्धि, धार्मिकता, भाग्य, र आध्यात्मिक झुकाव प्रदान गर्दछ। जातक महान विचारधारा, उदार, र समाजमा एक बुद्धिमान सल्लाहकारको रूपमा सम्मानित हुन्छ।"),
    HAMSA_SHORT_DESC("Wisdom, spirituality, fortune", "ज्ञान, आध्यात्मिकता, भाग्य"),
    HAMSA_BLESSINGS("Wisdom, Fortune, Spiritual growth, Good character, Respected status", "ज्ञान, भाग्य, आध्यात्मिक वृद्धि, राम्रो चरित्र, सम्मानित स्थिति"),
    HAMSA_DEITY("Lord Brihaspati or Lord Shiva", "भगवान बृहस्पति वा भगवान शिव"),
    HAMSA_GEMSTONE("Yellow Sapphire (Pukhraj)", "पहेंलो नीलम (पुखराज)"),
    HAMSA_MANTRA("Om Graam Greem Graum Sah Gurave Namah", "ॐ ग्रां ग्रीं ग्रौं सः गुरवे नमः"),
    HAMSA_CHARITY("turmeric, yellow cloth, ghee, gold to Brahmins", "हल्दी, पहेंलो कपडा, घिउ, ब्राह्मणहरूलाई सुन"),
    HAMSA_CAREERS("Education, Law, Finance, Consulting, Priesthood, Philosophy, Judiciary", "शिक्षा, कानून, वित्त, परामर्श, पुजारी, दर्शन, न्यायपालिका"),

    // Malavya Yoga Details
    MALAVYA_DESC("Malavya Yoga is formed when Venus is in Kendra in its own sign (Taurus/Libra) or exaltation (Pisces). It bestows beauty, luxury, artistic talents, and harmonious relationships. The native has refined tastes, attracts comfort and enjoys sensual pleasures in a balanced way.", "मालव्य योग शुक्र ग्रह केन्द्रमा स्वराशि (वृष/तुला) वा उच्च (मीन) मा हुँदा बन्छ। यसले सौन्दर्य, विलासिता, कलात्मक प्रतिभा, र सामंजस्यपूर्ण सम्बन्ध प्रदान गर्दछ। जातकको परिष्कृत स्वाद हुन्छ, आराम आकर्षित गर्दछ र सन्तुलित तरिकाले इन्द्रिय सुखको आनन्द लिन्छ।"),
    MALAVYA_SHORT_DESC("Beauty, luxury, artistic talents", "सौन्दर्य, विलासिता, कलात्मक प्रतिभा"),
    MALAVYA_BLESSINGS("Beauty, Luxury, Artistic ability, Happy marriage, Vehicles", "सौन्दर्य, विलासिता, कलात्मक क्षमता, सुखी विवाह, सवारी साधन"),
    MALAVYA_DEITY("Goddess Lakshmi or Goddess Saraswati", "देवी लक्ष्मी वा देवी सरस्वती"),
    MALAVYA_GEMSTONE("Diamond (Heera) or White Sapphire", "हीरा वा सेतो नीलम"),
    MALAVYA_MANTRA("Om Draam Dreem Draum Sah Shukraya Namah", "ॐ द्रां द्रीं द्रौं सः शुक्राय नमः"),
    MALAVYA_CHARITY("white rice, white cloth, sugar, silver items to women", "सेतो चामल, सेतो कपडा, चिनी, महिलाहरूलाई चाँदीका वस्तुहरू"),
    MALAVYA_CAREERS("Arts, Entertainment, Fashion, Hospitality, Luxury goods, Interior design, Music", "कला, मनोरञ्जन, फेशन, आतिथ्य, विलासी वस्तुहरू, आन्तरिक डिजाइन, संगीत"),

    // Sasha Yoga Details
    SASHA_DESC("Sasha Yoga is formed when Saturn is in Kendra in its own sign (Capricorn/Aquarius) or exaltation (Libra). It bestows discipline, longevity, authority, and success through persistent effort. The native rises to positions of power through hard work and has command over subordinates.", "शश योग शनि ग्रह केन्द्रमा स्वराशि (मकर/कुम्भ) वा उच्च (तुला) मा हुँदा बन्छ। यसले अनुशासन, दीर्घायु, अधिकार, र निरन्तर प्रयासबाट सफलता प्रदान गर्दछ। जातक कडा परिश्रमबाट शक्तिको पदमा पुग्छ र अधीनस्थहरूमाथि नियन्त्रण राख्छ।"),
    SASHA_SHORT_DESC("Discipline, authority, longevity", "अनुशासन, अधिकार, दीर्घायु"),
    SASHA_BLESSINGS("Longevity, Authority, Discipline, Servants, Success through perseverance", "दीर्घायु, अधिकार, अनुशासन, सेवकहरू, लगनशीलताबाट सफलता"),
    SASHA_DEITY("Lord Shani or Lord Hanuman", "भगवान शनि वा भगवान हनुमान"),
    SASHA_GEMSTONE("Blue Sapphire (Neelam) - only after careful consultation", "नीलम - सावधानीपूर्वक परामर्श पछि मात्र"),
    SASHA_MANTRA("Om Praam Preem Praum Sah Shanaischaraya Namah", "ॐ प्रां प्रीं प्रौं सः शनैश्चराय नमः"),
    SASHA_CHARITY("black sesame, black cloth, iron items, oil to the poor", "कालो तिल, कालो कपडा, फलामका वस्तुहरू, गरिबलाई तेल"),
    SASHA_CAREERS("Government, Construction, Mining, Management, Agriculture, Real estate, Manufacturing", "सरकार, निर्माण, खानी, व्यवस्थापन, कृषि, घर जग्गा, उत्पादन"),

    // YogaEffects
    EFFECT_RUCHAKA_PHYSICAL("Strong physical constitution, athletic build, courage in action", "बलियो शारीरिक संविधान, एथलेटिक शरीर, कार्यमा साहस"),
    EFFECT_RUCHAKA_MENTAL("Fearless mindset, competitive spirit, quick decision-making", "निडर मानसिकता, प्रतिस्पर्धी भावना, छिटो निर्णय लिने"),
    EFFECT_RUCHAKA_CAREER("Success in military, police, sports, surgery, engineering", "सेना, प्रहरी, खेलकुद, शल्य चिकित्सा, इन्जिनियरिङमा सफलता"),
    EFFECT_RUCHAKA_SPIRITUAL("Warrior spirit, protection of dharma, courage in spiritual practice", "योद्धा आत्मा, धर्मको रक्षा, आध्यात्मिक अभ्यासमा साहस"),
    EFFECT_RUCHAKA_RELATIONSHIP("Protective of loved ones, passionate but sometimes aggressive", "प्रियजनहरूको रक्षा गर्ने, भावुक तर कहिलेकाहीं आक्रामक"),
    EFFECT_BHADRA_PHYSICAL("Youthful appearance, expressive gestures, quick movements", "युवा उपस्थिति, अभिव्यक्त इशाराहरू, छिटो चाल"),
    EFFECT_BHADRA_MENTAL("Sharp intellect, excellent memory, witty communication", "तीखो बुद्धि, उत्कृष्ट स्मरणशक्ति, मजाकिया सञ्चार"),
    EFFECT_BHADRA_CAREER("Success in writing, teaching, commerce, accounting, IT", "लेखन, शिक्षण, वाणिज्य, लेखा, आईटीमा सफलता"),
    EFFECT_BHADRA_SPIRITUAL("Knowledge through study, intellectual approach to spirituality", "अध्ययनबाट ज्ञान, आध्यात्मिकतामा बौद्धिक दृष्टिकोण"),
    EFFECT_BHADRA_RELATIONSHIP("Communicative, adaptable, good at networking", "सञ्चारशील, अनुकूलनीय, नेटवर्किङमा राम्रो"),
    EFFECT_HAMSA_PHYSICAL("Graceful bearing, fair complexion, pleasant appearance", "सुन्दर चाल, गोरो रंग, सुखद उपस्थिति"),
    EFFECT_HAMSA_MENTAL("Wisdom, philosophical outlook, positive thinking", "ज्ञान, दार्शनिक दृष्टिकोण, सकारात्मक सोच"),
    EFFECT_HAMSA_CAREER("Success in law, education, finance, consulting, priesthood", "कानून, शिक्षा, वित्त, परामर्श, पुजारीमा सफलता"),
    EFFECT_HAMSA_SPIRITUAL("Natural inclination toward spirituality, dharmic conduct", "आध्यात्मिकताप्रति प्राकृतिक झुकाव, धार्मिक आचरण"),
    EFFECT_HAMSA_RELATIONSHIP("Generous, supportive, good advisor in relationships", "उदार, सहयोगी, सम्बन्धमा राम्रो सल्लाहकार"),
    EFFECT_MALAVYA_PHYSICAL("Attractive appearance, pleasant features, artistic grace", "आकर्षक उपस्थिति, सुखद विशेषताहरू, कलात्मक अनुग्रह"),
    EFFECT_MALAVYA_MENTAL("Refined sensibilities, appreciation of beauty, romantic nature", "परिष्कृत संवेदनशीलता, सौन्दर्यको प्रशंसा, रोमान्टिक स्वभाव"),
    EFFECT_MALAVYA_CAREER("Success in arts, entertainment, luxury goods, hospitality", "कला, मनोरञ्जन, विलासी वस्तुहरू, आतिथ्यमा सफलता"),
    EFFECT_MALAVYA_SPIRITUAL("Devotional approach, appreciation of divine beauty", "भक्तिपूर्ण दृष्टिकोण, दिव्य सौन्दर्यको प्रशंसा"),
    EFFECT_MALAVYA_RELATIONSHIP("Loving, romantic, harmony-seeking in relationships", "मायालु, रोमान्टिक, सम्बन्धमा सद्भाव खोज्ने"),
    EFFECT_SASHA_PHYSICAL("Lean build, serious demeanor, endurance and stamina", "दुबलो शरीर, गम्भीर व्यवहार, सहनशीलता र सहनशक्ति"),
    EFFECT_SASHA_MENTAL("Disciplined mind, methodical thinking, patience", "अनुशासित मन, व्यवस्थित सोच, धैर्य"),
    EFFECT_SASHA_CAREER("Success in construction, mining, government, management", "निर्माण, खानी, सरकार, व्यवस्थापनमा सफलता"),
    EFFECT_SASHA_SPIRITUAL("Detachment, karma yoga, service-oriented spirituality", "वैराग्य, कर्म योग, सेवा-उन्मुख आध्यात्मिकता"),
    EFFECT_SASHA_RELATIONSHIP("Loyal, responsible, may be reserved in expression", "वफादार, जिम्मेवार, अभिव्यक्तिमा आरक्षित हुन सक्छ"),

    // Manifestation Strength
    MANIFEST_STRONG("Results manifest prominently throughout life", "परिणामहरू जीवनभर प्रमुख रूपमा प्रकट हुन्छन्"),
    MANIFEST_NOTABLE("Results manifest notably, especially during dasha periods", "परिणामहरू उल्लेखनीय रूपमा प्रकट हुन्छन्, विशेष गरी दशा अवधिमा"),
    MANIFEST_MODERATE("Results manifest moderately with conscious effort", "परिणामहरू सचेत प्रयासबाट मध्यम रूपमा प्रकट हुन्छन्"),
    MANIFEST_SUBTLE("Results manifest subtly, may need activation", "परिणामहरू सूक्ष्म रूपमा प्रकट हुन्छन्, सक्रियता आवश्यक हुन सक्छ"),

    // House Specific Effects
    HOUSE_EFFECT_1ST("{yogaName} in 1st house: Yoga effects directly impact personality, health, and overall life approach. Strong self-projection of {planetName}'s qualities.", "{yogaName} पहिलो भावमा: योगको प्रभावले व्यक्तित्व, स्वास्थ्य र समग्र जीवन दृष्टिकोणमा प्रत्यक्ष असर गर्छ। {planetName} को गुणहरूको बलियो आत्म-प्रक्षेपण।"),
    HOUSE_EFFECT_4TH("{yogaName} in 4th house: Benefits related to home, mother, vehicles, property, and inner peace. {planetName}'s blessings in domestic life.", "{yogaName} चौथो भावमा: घर, आमा, सवारी साधन, सम्पत्ति र भित्री शान्तिसँग सम्बन्धित फाइदाहरू। घरेलु जीवनमा {planetName} को आशीर्वाद।"),
    HOUSE_EFFECT_7TH("{yogaName} in 7th house: Strong impact on marriage, partnerships, business dealings. Spouse may have {planetName}'s characteristics.", "{yogaName} सातौं भावमा: विवाह, साझेदारी, व्यापारिक कारोबारमा बलियो प्रभाव। जीवनसाथीमा {planetName} को विशेषताहरू हुन सक्छन्।"),
    HOUSE_EFFECT_10TH("{yogaName} in 10th house: Career excellence, public recognition, authority. {planetName}'s qualities shine in professional life.", "{yogaName} दशौं भावमा: करियर उत्कृष्टता, सार्वजनिक मान्यता, अधिकार। व्यावसायिक जीवनमा {planetName} को गुणहरू चम्किन्छन्।"),
    HOUSE_EFFECT_GENERAL("General yoga effects apply", "सामान्य योग प्रभावहरू लागू हुन्छन्"),

    // Interpretation fragments
    INTERP_FORMED_WITH("{yogaName} is formed with {planetName} {dignityText} in the {houseText}.", "{yogaName} {planetName} {dignityText} {houseText} मा बन्दछ।"),
    INTERP_DIGNITY_EXALTED("exalted", "उच्च"),
    INTERP_DIGNITY_OWN_SIGN("in its own sign", "आफ्नै राशिमा"),
    INTERP_HOUSE_1("Ascendant (1st house)", "लग्न (पहिलो भाव)"),
    INTERP_HOUSE_4("4th house", "चौथो भाव"),
    INTERP_HOUSE_7("7th house", "सातौं भाव"),
    INTERP_HOUSE_10("10th house (Midheaven)", "दशौं भाव (मध्यआकाश)"),
    INTERP_HOUSE_N("house {houseNum}", "भाव {houseNum}"),
    INTERP_STRENGTH_EXCEPTIONAL("This is an exceptionally powerful yoga that will significantly influence your life.", "यो एक असाधारण शक्तिशाली योग हो जसले तपाईंको जीवनलाई महत्त्वपूर्ण रूपमा प्रभाव पार्नेछ।"),
    INTERP_STRENGTH_STRONG("This is a strong yoga with notable effects throughout life.", "यो जीवनभरि उल्लेखनीय प्रभावहरू भएको एक बलियो योग हो।"),
    INTERP_STRENGTH_MODERATE("This yoga has moderate strength and will manifest clearly during favorable periods.", "यो योगमा मध्यम शक्ति छ र अनुकूल अवधिमा स्पष्ट रूपमा प्रकट हुनेछ।"),
    INTERP_STRENGTH_WEAK("This yoga exists but may need activation through conscious effort or favorable transits.", "यो योग अवस्थित छ तर सचेत प्रयास वा अनुकूल गोचर मार्फत सक्रियता आवश्यक हुन सक्छ।"),
    INTERP_KEY_BLESSINGS("Key blessings: {blessings}", "मुख्य आशीर्वाद: {blessings}"),

    // Recommendations
    REC_ACTION_WORSHIP("Worship {deity} on {planetName}'s day ({weekday})", "{weekday} मा {deity} को पूजा गर्नुहोस्"),
    REC_TIMING_WEEKDAY_MORNING("{weekday} mornings", "{weekday} बिहान"),
    REC_BENEFIT_ACTIVATE("Activates and strengthens the yoga's positive effects", "योगको सकारात्मक प्रभावहरूलाई सक्रिय र बलियो बनाउँछ"),
    REC_ACTION_CAREER("Pursue careers in {careers}", "{careers} मा करियर बनाउनुहोस्"),
    REC_TIMING_DASHA("During {planetName}'s dasha/antardasha periods", "{planetName} को दशा/अन्तर्दशा अवधिमा"),
    REC_BENEFIT_CAREER("Maximum success in areas aligned with the yoga", "योगसँग मिल्ने क्षेत्रहरूमा अधिकतम सफलता"),
    REC_ACTION_GEMSTONE("Consider wearing {gemstone} after proper consultation", "उचित परामर्श पछि {gemstone} लगाउने विचार गर्नुहोस्"),
    REC_TIMING_MUHURTA("During auspicious muhurta on {weekday}", "{weekday} मा शुभ मुहूर्तमा"),
    REC_BENEFIT_GEMSTONE("Amplifies {planetName}'s positive significations", "{planetName} को सकारात्मक संकेतहरू बढाउँछ"),
    REC_ACTION_MANTRA("Recite {mantra} 108 times", "{mantra} १०८ पटक जप गर्नुहोस्"),
    REC_TIMING_HORA("Daily during {planetName}'s hora", "{planetName} को होरामा दैनिक"),
    REC_BENEFIT_MANTRA("Invokes {planetName}'s blessings for yoga activation", "योग सक्रियताको लागि {planetName} को आशीर्वाद आह्वान गर्दछ"),
    REC_ACTION_CHARITY("Donate {items} on {weekday}", "{weekday} मा {items} दान गर्नुहोस्"),
    REC_TIMING_REGULAR("{weekdays} regularly", "{weekdays} नियमित रूपमा"),
    REC_BENEFIT_CHARITY("Creates good karma and removes obstacles", "राम्रो कर्म सिर्जना गर्दछ र अवरोधहरू हटाउँछ"),

    // Combined Effects
    COMBINED_SYNERGY_HAMSA_MALAVYA("Hamsa-Malavya combination: Wisdom with beauty, spiritual with material balance", "हंस-मालव्य संयोजन: सौन्दर्यसँग ज्ञान, भौतिकसँग आध्यात्मिक सन्तुलन"),
    COMBINED_EFFECT_HAMSA_MALAVYA("Excellent for careers in education, arts, counseling", "शिक्षा, कला, परामर्शमा करियरका लागि उत्कृष्ट"),
    COMBINED_SYNERGY_RUCHAKA_SASHA("Ruchaka-Sasha combination: Courage with discipline, action with patience", "रूचक-शश संयोजन: अनुशासनसँग साहस, धैर्यसँग कार्य"),
    COMBINED_EFFECT_RUCHAKA_SASHA("Excellent for leadership, military, engineering management", "नेतृत्व, सेना, इन्जिनियरिङ व्यवस्थापनका लागि उत्कृष्ट"),
    COMBINED_SYNERGY_BHADRA_HAMSA("Bhadra-Hamsa combination: Intelligence with wisdom, communication with knowledge", "भद्र-हंस संयोजन: ज्ञानसँग बुद्धि, ज्ञानसँग सञ्चार"),
    COMBINED_EFFECT_BHADRA_HAMSA("Excellent for teaching, writing, law, philosophy", "शिक्षण, लेखन, कानून, दर्शनका लागि उत्कृष्ट"),
    COMBINED_RARE_COMBO("Rare combination of {count} Mahapurusha Yogas indicates a highly blessed chart", "{count} महापुरुष योगहरूको दुर्लभ संयोजनले अत्यधिक धन्य कुण्डलीलाई संकेत गर्दछ"),
    COMBINED_RARITY_2("Uncommon - Two Mahapurusha Yogas", "असामान्य - दुई महापुरुष योग"),
    COMBINED_RARITY_3("Rare - Three Mahapurusha Yogas", "दुर्लभ - तीन महापुरुष योग"),
    COMBINED_RARITY_4("Very Rare - Four Mahapurusha Yogas", "धेरै दुर्लभ - चार महापुरुष योग"),
    COMBINED_RARITY_5("Extremely Rare - All Five Mahapurusha Yogas", "अत्यन्त दुर्लभ - सबै पाँच महापुरुष योग"),
    COMBINED_RARITY_SINGLE("Single Yoga", "एकल योग"),
    COMBINED_OVERALL_INTERP("Having {count} Mahapurusha Yogas is {rarityAdjective}. The combined influence of {yogaNames} creates a multifaceted personality with diverse talents and blessings.", "{count} महापुरुष योग हुनु {rarityAdjective} हो। {yogaNames} को संयुक्त प्रभावले विविध प्रतिभा र आशीर्वादसहित बहुआयामिक व्यक्तित्व सिर्जना गर्दछ।"),
    COMBINED_ADJ_RARE_POWERFUL("exceptionally rare and powerful", "असाधारण रूपमा दुर्लभ र शक्तिशाली"),
    COMBINED_ADJ_SIGNIFICANT("a significant blessing", "एक महत्त्वपूर्ण आशीर्वाद"),

    // Activation Periods
    ACTIVATION_MAHADASHA("{planetName} Mahadasha - Primary yoga activation period", "{planetName} महादशा - प्राथमिक योग सक्रियता अवधि"),
    ACTIVATION_ANTARDASHA("{planetName} Antardasha - Secondary activation in any major period", "{planetName} अन्तर्दशा - कुनै पनि मुख्य अवधिमा माध्यमिक सक्रियता"),
    ACTIVATION_TRANSIT("When {planetName} transits its natal position or exaltation sign", "जब {planetName} आफ्नो जन्म स्थिति वा उच्च राशिमा गोचर गर्छ"),
    ACTIVATION_JUPITER_TRANSIT("When Jupiter transits over or aspects natal {planetName}", "जब बृहस्पति जन्म {planetName} माथि वा दृष्टिमा गोचर गर्छ"),

    // Overall Interpretation
    OVERALL_NO_YOGA_SUMMARY("No Panch Mahapurusha Yoga is formed in this chart. These special yogas require Mars, Mercury, Jupiter, Venus, or Saturn to be in Kendra houses (1, 4, 7, 10) in their own or exaltation signs. While these yogas are not present, other yogas and planetary combinations in your chart provide their own blessings.", "यस कुण्डलीमा कुनै पञ्च महापुरुष योग बनेको छैन। यी विशेष योगहरूका लागि मंगल, बुध, बृहस्पति, शुक्र, वा शनि केन्द्र भाव (१, ४, ७, १०) मा स्वराशि वा उच्च राशिमा हुनुपर्छ। यी योगहरू उपस्थित नभए पनि, तपाईंको कुण्डलीमा अन्य योगहरू र ग्रह संयोजनहरूले आफ्नै आशीर्वाद प्रदान गर्दछन्।"),
    OVERALL_YOGA_SUMMARY("This chart has {count} Panch Mahapurusha Yoga(s): {yogaList}. These are among the most auspicious yogas in Vedic astrology, indicating a soul that has earned special blessings through past-life merit. The native is blessed with the positive qualities of the yoga-forming planets.", "यस कुण्डलीमा {count} पञ्च महापुरुष योग(हरू) छन्: {yogaList}। यी वैदिक ज्योतिषमा सबैभन्दा शुभ योगहरू मध्येका हुन्, जसले पूर्वजन्मको पुण्यबाट विशेष आशीर्वाद कमाएको आत्मालाई संकेत गर्दछ। जातक योग बनाउने ग्रहहरूको सकारात्मक गुणहरूले धन्य हुन्छ।"),
    OVERALL_INSIGHT("{yogaName}: {shortDesc} (Strength: {strength}%)", "{yogaName}: {shortDesc} (शक्ति: {strength}%)"),
    OVERALL_REC_NO_YOGA_1("Focus on strengthening the natural benefics in your chart", "आफ्नो कुण्डलीमा प्राकृतिक शुभ ग्रहहरूलाई बलियो बनाउनमा ध्यान दिनुहोस्"),
    OVERALL_REC_NO_YOGA_2("Explore other yogas present in your horoscope", "आफ्नो राशिफलमा उपस्थित अन्य योगहरू अन्वेषण गर्नुहोस्"),
    OVERALL_REC_NO_YOGA_3("Planetary remedies can enhance positive influences", "ग्रह उपायहरूले सकारात्मक प्रभावहरू बढाउन सक्छ"),
    OVERALL_REC_YOGA("Activate {yogaName} through {planetName} worship", "{planetName} को पूजा मार्फत {yogaName} सक्रिय गर्नुहोस्"),
    OVERALL_REC_CAREER("Best career alignment: {career}", "उत्तम करियर संरेखण: {career}"),
}
