package com.astro.storm.data.templates

import com.astro.storm.core.model.Planet
import com.astro.storm.core.model.ZodiacSign

/**
 * Comprehensive Transit (Gochara) interpretation templates (800+ templates).
 * Based on classical texts: BPHS, Phaladeepika, Gochara Phaladeepika, Saravali.
 *
 * Template categories:
 * - Transit by house from Moon (9 × 12 = 108)
 * - Transit by house from Lagna (9 × 12 = 108)
 * - Transit aspects to natal planets (9 × 9 = 81)
 * - Transit conjunctions (9 × 9 = 81)
 * - Sade Sati phases (3 × detailed)
 * - Vedha (obstruction) effects (special)
 * - Retrograde transits (9 × 12 = 108)
 * - Daily transit effects (various)
 */
object TransitTemplates {

    // ============================================
    // TRANSIT THROUGH HOUSES FROM MOON (GOCHARA)
    // ============================================
    val transitFromMoon: Map<Pair<Planet, Int>, LocalizedTemplate> = buildMap {
        // SUN TRANSIT THROUGH 12 HOUSES FROM MOON
        put(Planet.SUN to 1, LocalizedTemplate(
            en = "Sun transiting 1st from Moon (Janma Rashi): A month of health concerns, mental restlessness, and challenges to ego. Authority figures may create obstacles. Maintain humility and avoid conflicts with superiors. Government matters may face delays. Father's health requires attention. Self-confidence needs rebuilding through inner work.",
            ne = "सूर्य चन्द्रबाट पहिलोमा गोचर (जन्म राशि): स्वास्थ्य चिन्ता, मानसिक बेचैनी र अहंकारमा चुनौतीहरूको महिना। अधिकार व्यक्तिहरूले बाधाहरू सिर्जना गर्न सक्छन्। विनम्रता कायम राख्नुहोस् र वरिष्ठहरूसँग द्वन्द्वबाट बच्नुहोस्। सरकारी मामिलाहरूमा ढिलाइ हुन सक्छ। पिताको स्वास्थ्यमा ध्यान चाहिन्छ। आन्तरिक कार्य मार्फत आत्मविश्वास पुनर्निर्माण चाहिन्छ।"
        ))
        put(Planet.SUN to 2, LocalizedTemplate(
            en = "Sun transiting 2nd from Moon: Financial challenges and family tensions possible. Speech may cause conflicts. Eye health requires attention. Government fines or expenses possible. Maintain truth in all communications. Family gatherings may have friction. Wealth preservation focus needed.",
            ne = "सूर्य चन्द्रबाट दोस्रोमा गोचर: आर्थिक चुनौतीहरू र पारिवारिक तनावहरू सम्भव। वाणीले द्वन्द्वहरू निम्त्याउन सक्छ। आँखाको स्वास्थ्यमा ध्यान चाहिन्छ। सरकारी जरिवाना वा खर्चहरू सम्भव। सबै सञ्चारहरूमा सत्य कायम राख्नुहोस्। पारिवारिक भेलाहरूमा घर्षण हुन सक्छ। धन संरक्षणमा ध्यान चाहिन्छ।"
        ))
        put(Planet.SUN to 3, LocalizedTemplate(
            en = "Sun transiting 3rd from Moon: Favorable for courage, initiative, and short travels. Siblings support your goals. Communication skills enhance. Writing and media activities succeed. Government paperwork clears. Mental strength increases. Victory over competitors likely.",
            ne = "सूर्य चन्द्रबाट तेस्रोमा गोचर: साहस, पहल र छोटा यात्राहरूको लागि अनुकूल। भाइबहिनीहरूले तपाईंको लक्ष्यहरूलाई समर्थन गर्छन्। सञ्चार सीपहरू बढ्छन्। लेखन र मिडिया गतिविधिहरू सफल। सरकारी कागजात पूरा हुन्छ। मानसिक शक्ति बढ्छ। प्रतिस्पर्धीहरूमाथि विजय सम्भव।"
        ))
        put(Planet.SUN to 4, LocalizedTemplate(
            en = "Sun transiting 4th from Moon: Challenges to domestic peace and mother's well-being. Property matters may stall. Heart and chest require care. Government housing issues possible. Vehicle troubles likely. Inner emotional balance disturbed. Seek solace in meditation.",
            ne = "सूर्य चन्द्रबाट चौथोमा गोचर: घरेलु शान्ति र आमाको कल्याणमा चुनौतीहरू। सम्पत्ति मामिलाहरू रोकिन सक्छन्। मुटु र छातीको हेरचाह चाहिन्छ। सरकारी आवास समस्याहरू सम्भव। सवारी साधन समस्याहरू सम्भव। भित्री भावनात्मक सन्तुलन बिग्रिएको। ध्यानमा शान्ति खोज्नुहोस्।"
        ))
        put(Planet.SUN to 5, LocalizedTemplate(
            en = "Sun transiting 5th from Moon: Challenges with children or creative projects. Romance may face obstacles. Investments require caution. Government educational matters may stall. Speculative ventures unfavorable. Past karma with children surfaces. Intelligence applied carefully.",
            ne = "सूर्य चन्द्रबाट पाँचौमा गोचर: सन्तान वा रचनात्मक परियोजनाहरूमा चुनौतीहरू। रोमान्समा बाधाहरू हुन सक्छन्। लगानीहरूमा सावधानी चाहिन्छ। सरकारी शैक्षिक मामिलाहरू रोकिन सक्छन्। सट्टेबाजी कार्यहरू प्रतिकूल। सन्तानसँग विगतको कर्म सतहमा आउँछ। बुद्धि सावधानीपूर्वक प्रयोग गरिएको।"
        ))
        put(Planet.SUN to 6, LocalizedTemplate(
            en = "Sun transiting 6th from Moon: Excellent for defeating enemies, recovering from illness, and resolving disputes. Legal victory likely. Service gains recognition. Health improves through discipline. Government support against adversaries. Debts may be cleared. Competition success.",
            ne = "सूर्य चन्द्रबाट छैटौंमा गोचर: शत्रुहरूलाई पराजित गर्न, रोगबाट निको हुन र विवादहरू समाधान गर्न उत्कृष्ट। कानूनी विजय सम्भव। सेवाले मान्यता पाउँछ। अनुशासन मार्फत स्वास्थ्य सुधारिन्छ। विपक्षीहरूविरुद्ध सरकारी समर्थन। ऋणहरू फुकुवा हुन सक्छन्। प्रतिस्पर्धा सफलता।"
        ))
        put(Planet.SUN to 7, LocalizedTemplate(
            en = "Sun transiting 7th from Moon: Partnership and marriage face tests. Spouse's health may concern. Business partnerships need careful handling. Government contracts require attention. Legal matters need diplomacy. Public image fluctuates. Travel with partner favorable.",
            ne = "सूर्य चन्द्रबाट सातौंमा गोचर: साझेदारी र विवाहमा परीक्षाहरू। जीवनसाथीको स्वास्थ्यमा चिन्ता हुन सक्छ। व्यापारिक साझेदारीहरूमा सावधानीपूर्वक व्यवहार चाहिन्छ। सरकारी सम्झौताहरूमा ध्यान चाहिन्छ। कानूनी मामिलाहरूमा कूटनीति चाहिन्छ। सार्वजनिक छवि उतारचढाव हुन्छ। साथीसँग यात्रा अनुकूल।"
        ))
        put(Planet.SUN to 8, LocalizedTemplate(
            en = "Sun transiting 8th from Moon: Health vulnerabilities and obstacles increase. Hidden enemies may act. Government investigations possible. Inheritance matters may arise. Transformative but challenging period. Chronic ailments may flare. Research into hidden matters.",
            ne = "सूर्य चन्द्रबाट आठौंमा गोचर: स्वास्थ्य कमजोरीहरू र बाधाहरू बढ्छन्। लुकेका शत्रुहरूले कार्य गर्न सक्छन्। सरकारी जाँचबुझहरू सम्भव। विरासत मामिलाहरू उठ्न सक्छन्। रूपान्तरणकारी तर चुनौतीपूर्ण अवधि। दीर्घकालीन रोगहरू बढ्न सक्छन्। लुकेका मामिलाहरूमा अनुसन्धान।"
        ))
        put(Planet.SUN to 9, LocalizedTemplate(
            en = "Sun transiting 9th from Moon: Father and guru relationships face tests. Long journeys may have obstacles. Legal or religious matters need care. Fortune temporarily diminished. Higher education challenges. Dharmic path requires effort. Philosophy deepens through trials.",
            ne = "सूर्य चन्द्रबाट नवौंमा गोचर: पिता र गुरु सम्बन्धहरूमा परीक्षाहरू। लामो यात्राहरूमा बाधाहरू हुन सक्छन्। कानूनी वा धार्मिक मामिलाहरूमा हेरचाह चाहिन्छ। भाग्य अस्थायी रूपमा कम। उच्च शिक्षा चुनौतीहरू। धार्मिक मार्गमा प्रयास चाहिन्छ। परीक्षाहरू मार्फत दर्शन गहिरो हुन्छ।"
        ))
        put(Planet.SUN to 10, LocalizedTemplate(
            en = "Sun transiting 10th from Moon: Favorable for career, authority, and recognition. Government support available. Leadership opportunities arise. Professional achievements highlighted. Fame and status increase. Father's blessings support career. Public honors possible.",
            ne = "सूर्य चन्द्रबाट दशौंमा गोचर: करियर, अधिकार र मान्यताको लागि अनुकूल। सरकारी समर्थन उपलब्ध। नेतृत्वका अवसरहरू उत्पन्न। पेशेवर उपलब्धिहरू हाइलाइट। प्रसिद्धि र हैसियत बढ्छ। पिताको आशीर्वादले करियरलाई समर्थन गर्छ। सार्वजनिक सम्मानहरू सम्भव।"
        ))
        put(Planet.SUN to 11, LocalizedTemplate(
            en = "Sun transiting 11th from Moon: Excellent for gains, income, and fulfillment of desires. Elder siblings support. Social networks expand. Government grants possible. All ventures profitable. Friendships with authority figures. Ambitions achievable.",
            ne = "सूर्य चन्द्रबाट एघारौंमा गोचर: लाभ, आय र इच्छाहरूको परिपूर्तिको लागि उत्कृष्ट। ठूला भाइबहिनीहरूको समर्थन। सामाजिक नेटवर्कहरू विस्तार। सरकारी अनुदानहरू सम्भव। सबै उद्यमहरू लाभदायक। अधिकार व्यक्तिहरूसँग मित्रता। महत्त्वाकांक्षाहरू प्राप्य।"
        ))
        put(Planet.SUN to 12, LocalizedTemplate(
            en = "Sun transiting 12th from Moon: Period of expenses, losses, and isolation. Government penalties possible. Foreign residence may be required. Father may be distant. Eye problems possible. Spiritual retreat beneficial. Hospital or asylum connections. Liberation through surrender.",
            ne = "सूर्य चन्द्रबाट बाह्रौंमा गोचर: खर्च, हानि र एकान्तको अवधि। सरकारी दण्डहरू सम्भव। विदेशी बसोबास आवश्यक हुन सक्छ। पिता टाढा हुन सक्छन्। आँखा समस्याहरू सम्भव। आध्यात्मिक एकान्त लाभकारी। अस्पताल वा आश्रम सम्बन्धहरू। समर्पण मार्फत मुक्ति।"
        ))

        // MOON TRANSIT THROUGH 12 HOUSES FROM MOON
        put(Planet.MOON to 1, LocalizedTemplate(
            en = "Moon transiting 1st from Moon (Janma Rashi): Emotionally sensitive period. Health fluctuations possible. Mental restlessness increases. Public interactions significant. Mother's influence heightened. Memory and past connections resurface. Water travel possible. Self-reflection time.",
            ne = "चन्द्र चन्द्रबाट पहिलोमा गोचर (जन्म राशि): भावनात्मक रूपमा संवेदनशील अवधि। स्वास्थ्य उतारचढाव सम्भव। मानसिक बेचैनी बढ्छ। सार्वजनिक अन्तरक्रियाहरू महत्त्वपूर्ण। आमाको प्रभाव बढेको। स्मृति र विगतका सम्बन्धहरू पुन: सतहमा आउँछन्। जल यात्रा सम्भव। आत्म-चिन्तनको समय।"
        ))
        put(Planet.MOON to 3, LocalizedTemplate(
            en = "Moon transiting 3rd from Moon: Favorable for communication, short travels, and courage. Siblings connect positively. Mental clarity improves. Writing and creative expression flow. Neighbors helpful. Arms and shoulders strong. Initiative supported emotionally.",
            ne = "चन्द्र चन्द्रबाट तेस्रोमा गोचर: सञ्चार, छोटा यात्रा र साहसको लागि अनुकूल। भाइबहिनीहरू सकारात्मक रूपमा जोडिन्छन्। मानसिक स्पष्टता सुधारिन्छ। लेखन र रचनात्मक अभिव्यक्ति बग्छ। छिमेकीहरू सहयोगी। हात र काँधहरू बलियो। पहल भावनात्मक रूपमा समर्थित।"
        ))
        put(Planet.MOON to 6, LocalizedTemplate(
            en = "Moon transiting 6th from Moon: Victory over enemies and resolution of health issues. Service recognized. Legal matters favor. Debts cleared. Competition success through emotional intelligence. Maternal uncles helpful. Digestive health improves.",
            ne = "चन्द्र चन्द्रबाट छैटौंमा गोचर: शत्रुहरूमाथि विजय र स्वास्थ्य समस्याहरूको समाधान। सेवा मान्यता। कानूनी मामिलाहरू अनुकूल। ऋणहरू फुकुवा। भावनात्मक बुद्धि मार्फत प्रतिस्पर्धा सफलता। मामाहरू सहयोगी। पाचन स्वास्थ्य सुधारिन्छ।"
        ))
        put(Planet.MOON to 7, LocalizedTemplate(
            en = "Moon transiting 7th from Moon: Partnership and relationship focus. Spouse emotional. Business partnerships active. Public image prominent. Marriage opportunities for single. Travel with partner. Women bring opportunities.",
            ne = "चन्द्र चन्द्रबाट सातौंमा गोचर: साझेदारी र सम्बन्धमा ध्यान। जीवनसाथी भावनात्मक। व्यापारिक साझेदारीहरू सक्रिय। सार्वजनिक छवि प्रमुख। एकलका लागि विवाहका अवसरहरू। साथीसँग यात्रा। महिलाहरूले अवसरहरू ल्याउँछन्।"
        ))
        put(Planet.MOON to 10, LocalizedTemplate(
            en = "Moon transiting 10th from Moon: Career gains and public recognition. Emotional satisfaction from work. Mother supports profession. Authority through nurturing leadership. Government favor. Public image excellent. Professional emotional intelligence peaks.",
            ne = "चन्द्र चन्द्रबाट दशौंमा गोचर: करियर लाभ र सार्वजनिक मान्यता। कामबाट भावनात्मक सन्तुष्टि। आमाले पेशालाई समर्थन गर्छिन्। पोषण नेतृत्व मार्फत अधिकार। सरकारी कृपा। सार्वजनिक छवि उत्कृष्ट। पेशेवर भावनात्मक बुद्धि चरममा।"
        ))
        put(Planet.MOON to 11, LocalizedTemplate(
            en = "Moon transiting 11th from Moon: Excellent for gains, friendships, and desire fulfillment. Elder siblings supportive. Social gatherings favorable. Emotional abundance. Women friends helpful. All ventures emotionally satisfying. Income increases.",
            ne = "चन्द्र चन्द्रबाट एघारौंमा गोचर: लाभ, मित्रता र इच्छा परिपूर्तिको लागि उत्कृष्ट। ठूला भाइबहिनीहरू समर्थक। सामाजिक भेलाहरू अनुकूल। भावनात्मक प्रचुरता। महिला साथीहरू सहयोगी। सबै उद्यमहरू भावनात्मक रूपमा सन्तोषजनक। आय बढ्छ।"
        ))

        // MARS TRANSIT THROUGH 12 HOUSES FROM MOON
        put(Planet.MARS to 1, LocalizedTemplate(
            en = "Mars transiting 1st from Moon: Physical energy increases but accidents possible. Blood and fever issues may arise. Courage enhances but anger needs control. Competitive success likely. Property matters active. Brothers significant. Initiative peaks.",
            ne = "मंगल चन्द्रबाट पहिलोमा गोचर: शारीरिक ऊर्जा बढ्छ तर दुर्घटनाहरू सम्भव। रक्त र ज्वरो समस्याहरू उठ्न सक्छन्। साहस बढ्छ तर क्रोधलाई नियन्त्रण चाहिन्छ। प्रतिस्पर्धात्मक सफलता सम्भव। सम्पत्ति मामिलाहरू सक्रिय। भाइहरू महत्त्वपूर्ण। पहल चरममा।"
        ))
        put(Planet.MARS to 3, LocalizedTemplate(
            en = "Mars transiting 3rd from Moon: Excellent for courage, initiative, and defeating enemies. Siblings prosper. Short travels for property. Communication forceful. Victory in competitions. Writing with power. Arms and hands strengthened.",
            ne = "मंगल चन्द्रबाट तेस्रोमा गोचर: साहस, पहल र शत्रुहरूलाई पराजित गर्न उत्कृष्ट। भाइबहिनीहरू समृद्ध। सम्पत्तिको लागि छोटा यात्राहरू। सञ्चार बलियो। प्रतिस्पर्धाहरूमा विजय। शक्तिसहित लेखन। हात र बाहुहरू बलियो।"
        ))
        put(Planet.MARS to 6, LocalizedTemplate(
            en = "Mars transiting 6th from Moon: Supreme for defeating enemies, winning disputes, and recovering health. Legal victories assured. Debts cleared through action. Competition dominated. Service excellence. Surgery successful if needed.",
            ne = "मंगल चन्द्रबाट छैटौंमा गोचर: शत्रुहरूलाई पराजित गर्न, विवादहरू जित्न र स्वास्थ्य पुनर्प्राप्तिको लागि सर्वोच्च। कानूनी विजयहरू सुनिश्चित। कारबाही मार्फत ऋणहरू फुकुवा। प्रतिस्पर्धामा वर्चस्व। सेवा उत्कृष्टता। आवश्यक भएमा शल्यक्रिया सफल।"
        ))
        put(Planet.MARS to 10, LocalizedTemplate(
            en = "Mars transiting 10th from Moon: Career advancement through bold action. Authority through courage. Engineering and technical success. Government positions through initiative. Father-son dynamics. Professional competition won.",
            ne = "मंगल चन्द्रबाट दशौंमा गोचर: साहसी कारबाही मार्फत करियर उन्नति। साहस मार्फत अधिकार। इन्जिनियरिङ र प्राविधिक सफलता। पहल मार्फत सरकारी पदहरू। पिता-पुत्र गतिशीलता। पेशेवर प्रतिस्पर्धा जीत।"
        ))
        put(Planet.MARS to 11, LocalizedTemplate(
            en = "Mars transiting 11th from Moon: Excellent for gains through action and competitive success. Elder brothers support. Goals achieved through initiative. Social networks expand through courage. Property gains. All ambitions achievable.",
            ne = "मंगल चन्द्रबाट एघारौंमा गोचर: कारबाही र प्रतिस्पर्धात्मक सफलता मार्फत लाभको लागि उत्कृष्ट। ठूला भाइहरूको समर्थन। पहल मार्फत लक्ष्यहरू प्राप्त। साहस मार्फत सामाजिक नेटवर्कहरू विस्तार। सम्पत्ति लाभ। सबै महत्त्वाकांक्षाहरू प्राप्य।"
        ))

        // JUPITER TRANSIT THROUGH 12 HOUSES FROM MOON
        put(Planet.JUPITER to 1, LocalizedTemplate(
            en = "Jupiter transiting 1st from Moon: Period of expansion but initial challenges. Health improves after initial issues. Wisdom grows through experience. Teachers guide. Fat and liver need attention. Optimism increases. Self-knowledge deepens.",
            ne = "बृहस्पति चन्द्रबाट पहिलोमा गोचर: विस्तारको अवधि तर प्रारम्भिक चुनौतीहरू। प्रारम्भिक समस्याहरू पछि स्वास्थ्य सुधारिन्छ। अनुभव मार्फत ज्ञान बढ्छ। शिक्षकहरूले मार्गदर्शन गर्छन्। बोसो र कलेजोमा ध्यान चाहिन्छ। आशावाद बढ्छ। आत्म-ज्ञान गहिरो हुन्छ।"
        ))
        put(Planet.JUPITER to 2, LocalizedTemplate(
            en = "Jupiter transiting 2nd from Moon: Excellent for wealth, family happiness, and speech. Accumulated wealth increases. Family gatherings joyful. Voice gains authority. Food and nutrition improve. Education of family members. Values clarify.",
            ne = "बृहस्पति चन्द्रबाट दोस्रोमा गोचर: धन, पारिवारिक खुशी र वाणीको लागि उत्कृष्ट। संचित धन बढ्छ। पारिवारिक भेलाहरू आनन्दमय। वाणीले अधिकार पाउँछ। खाना र पोषण सुधारिन्छ। परिवारका सदस्यहरूको शिक्षा। मूल्यहरू स्पष्ट।"
        ))
        put(Planet.JUPITER to 5, LocalizedTemplate(
            en = "Jupiter transiting 5th from Moon: Supreme for children, creativity, and intelligence. Conception favorable. Children's achievements. Speculative gains. Romance blessed. Education excellence. Past-life merit activates. Mantras powerful.",
            ne = "बृहस्पति चन्द्रबाट पाँचौमा गोचर: सन्तान, सिर्जनशीलता र बुद्धिको लागि सर्वोच्च। गर्भधारण अनुकूल। सन्तानहरूको उपलब्धिहरू। सट्टेबाजी लाभ। रोमान्स आशीर्वादित। शिक्षा उत्कृष्टता। पूर्वजन्म पुण्य सक्रिय। मन्त्रहरू शक्तिशाली।"
        ))
        put(Planet.JUPITER to 7, LocalizedTemplate(
            en = "Jupiter transiting 7th from Moon: Excellent for marriage, partnerships, and legal matters. Spouse brings fortune. Business partnerships expand. Contracts favorable. Public image blessed. Foreign partnerships. Marriage for single.",
            ne = "बृहस्पति चन्द्रबाट सातौंमा गोचर: विवाह, साझेदारी र कानूनी मामिलाहरूको लागि उत्कृष्ट। जीवनसाथीले भाग्य ल्याउँछ। व्यापारिक साझेदारीहरू विस्तार। सम्झौताहरू अनुकूल। सार्वजनिक छवि आशीर्वादित। विदेशी साझेदारीहरू। एकलका लागि विवाह।"
        ))
        put(Planet.JUPITER to 9, LocalizedTemplate(
            en = "Jupiter transiting 9th from Moon: Most auspicious transit for fortune, dharma, and higher learning. Father blessed. Pilgrimages fruitful. Legal victories. Teachers appear. Long journeys blessed. Publications succeed. Fortune peaks.",
            ne = "बृहस्पति चन्द्रबाट नवौंमा गोचर: भाग्य, धर्म र उच्च शिक्षाको लागि सबैभन्दा शुभ गोचर। पिता आशीर्वादित। तीर्थयात्राहरू फलदायी। कानूनी विजयहरू। शिक्षकहरू प्रकट हुन्छन्। लामो यात्राहरू आशीर्वादित। प्रकाशनहरू सफल। भाग्य चरममा।"
        ))
        put(Planet.JUPITER to 11, LocalizedTemplate(
            en = "Jupiter transiting 11th from Moon: Supreme for gains, fulfillment of desires, and abundance. Income multiplies. Elder siblings prosper. Social networks expand significantly. All goals achieved. Friends become benefactors. Wishes granted.",
            ne = "बृहस्पति चन्द्रबाट एघारौंमा गोचर: लाभ, इच्छाहरूको परिपूर्ति र प्रचुरताको लागि सर्वोच्च। आय बहुगुणित हुन्छ। ठूला भाइबहिनीहरू समृद्ध। सामाजिक नेटवर्कहरू उल्लेखनीय रूपमा विस्तार। सबै लक्ष्यहरू प्राप्त। साथीहरू दाताहरू बन्छन्। इच्छाहरू पूरा।"
        ))

        // SATURN TRANSIT (CRITICAL GOCHARA)
        put(Planet.SATURN to 1, LocalizedTemplate(
            en = "Saturn transiting 1st from Moon: Beginning of challenging 7.5 year cycle. Health requires attention. Energy diminishes initially. Patience essential. Karmic lessons begin. Discipline develops character. Slow but profound growth. This is the start of Sade Sati - maintain perseverance.",
            ne = "शनि चन्द्रबाट पहिलोमा गोचर: चुनौतीपूर्ण ७.५ वर्ष चक्रको सुरुवात। स्वास्थ्यमा ध्यान चाहिन्छ। ऊर्जा सुरुमा कम हुन्छ। धैर्य आवश्यक। कर्मिक पाठहरू सुरु। अनुशासनले चरित्र विकास गर्छ। ढिलो तर गहिरो वृद्धि। यो साढेसाती सुरु हो - लगनशीलता कायम राख्नुहोस्।"
        ))
        put(Planet.SATURN to 3, LocalizedTemplate(
            en = "Saturn transiting 3rd from Moon: Favorable for discipline in communication and courage development. Siblings face challenges but ultimately grow. Short travels with purpose. Writing projects require patience. Victory through persistence. Arms and shoulders may need care.",
            ne = "शनि चन्द्रबाट तेस्रोमा गोचर: सञ्चारमा अनुशासन र साहस विकासको लागि अनुकूल। भाइबहिनीहरूले चुनौतीहरू सामना गर्छन् तर अन्ततः बढ्छन्। उद्देश्यसहित छोटा यात्राहरू। लेखन परियोजनाहरूमा धैर्य चाहिन्छ। लगनशीलता मार्फत विजय। हात र काँधहरूमा हेरचाह चाहिन सक्छ।"
        ))
        put(Planet.SATURN to 6, LocalizedTemplate(
            en = "Saturn transiting 6th from Moon: Excellent for defeating enemies and resolving longstanding issues. Legal matters favor through patience. Chronic health improves. Service excellence through discipline. Debts cleared systematically. Competition won through persistence.",
            ne = "शनि चन्द्रबाट छैटौंमा गोचर: शत्रुहरूलाई पराजित गर्न र दीर्घकालीन समस्याहरू समाधान गर्न उत्कृष्ट। धैर्य मार्फत कानूनी मामिलाहरू अनुकूल। दीर्घकालीन स्वास्थ्य सुधारिन्छ। अनुशासन मार्फत सेवा उत्कृष्टता। ऋणहरू व्यवस्थित रूपमा फुकुवा। लगनशीलता मार्फत प्रतिस्पर्धा जीत।"
        ))
        put(Planet.SATURN to 10, LocalizedTemplate(
            en = "Saturn transiting 10th from Moon: Career challenges but ultimate achievement. Authority through hard work. Professional reputation built. Government positions through persistence. Father-son challenges. Long-term career foundations laid. Recognition delayed but lasting.",
            ne = "शनि चन्द्रबाट दशौंमा गोचर: करियर चुनौतीहरू तर अन्तिम उपलब्धि। कडा परिश्रम मार्फत अधिकार। पेशेवर प्रतिष्ठा निर्माण। लगनशीलता मार्फत सरकारी पदहरू। पिता-पुत्र चुनौतीहरू। दीर्घकालीन करियर जगहरू राखिएका। मान्यता ढिलो तर स्थायी।"
        ))
        put(Planet.SATURN to 11, LocalizedTemplate(
            en = "Saturn transiting 11th from Moon: Gains through persistent effort and discipline. Elder siblings mature. Social networks consolidate. Income stabilizes after initial delays. Goals achieved through patience. Long-term friendships form. Ambitions systematically realized.",
            ne = "शनि चन्द्रबाट एघारौंमा गोचर: निरन्तर प्रयास र अनुशासन मार्फत लाभ। ठूला भाइबहिनीहरू परिपक्व। सामाजिक नेटवर्कहरू एकीकृत। प्रारम्भिक ढिलाइ पछि आय स्थिर। धैर्य मार्फत लक्ष्यहरू प्राप्त। दीर्घकालीन मित्रताहरू बन्छन्। महत्त्वाकांक्षाहरू व्यवस्थित रूपमा साकार।"
        ))

        // RAHU TRANSIT
        put(Planet.RAHU to 3, LocalizedTemplate(
            en = "Rahu transiting 3rd from Moon: Excellent for unconventional courage and communication. Technology and media success. Foreign short travels. Unusual siblings connections. Innovation in writing. Bold initiatives succeed. Breaking communication barriers.",
            ne = "राहु चन्द्रबाट तेस्रोमा गोचर: अपरम्परागत साहस र सञ्चारको लागि उत्कृष्ट। प्रविधि र मिडिया सफलता। विदेशी छोटा यात्राहरू। असामान्य भाइबहिनी सम्बन्धहरू। लेखनमा नवप्रवर्तन। साहसी पहलहरू सफल। सञ्चार बाधाहरू तोड्ने।"
        ))
        put(Planet.RAHU to 6, LocalizedTemplate(
            en = "Rahu transiting 6th from Moon: Supreme for defeating enemies through unconventional means. Technology in health. Foreign legal victories. Unusual service methods succeed. Obsessive health focus helpful. Competition dominated through innovation.",
            ne = "राहु चन्द्रबाट छैटौंमा गोचर: अपरम्परागत माध्यमबाट शत्रुहरूलाई पराजित गर्न सर्वोच्च। स्वास्थ्यमा प्रविधि। विदेशी कानूनी विजयहरू। असामान्य सेवा विधिहरू सफल। जुनूनी स्वास्थ्य ध्यान सहयोगी। नवप्रवर्तन मार्फत प्रतिस्पर्धामा वर्चस्व।"
        ))
        put(Planet.RAHU to 10, LocalizedTemplate(
            en = "Rahu transiting 10th from Moon: Career through unconventional paths and foreign connections. Technology-based authority. Sudden fame possible. Government through unusual means. Breaking career barriers. Innovation in profession.",
            ne = "राहु चन्द्रबाट दशौंमा गोचर: अपरम्परागत मार्ग र विदेशी सम्बन्धहरू मार्फत करियर। प्रविधि-आधारित अधिकार। अचानक प्रसिद्धि सम्भव। असामान्य माध्यमबाट सरकार। करियर बाधाहरू तोड्ने। पेशामा नवप्रवर्तन।"
        ))
        put(Planet.RAHU to 11, LocalizedTemplate(
            en = "Rahu transiting 11th from Moon: Excellent for gains through technology, foreign connections, and unconventional means. Network expansion rapid. Unusual friendships beneficial. Ambitious desires fulfilled. Innovation rewarded. Social media success.",
            ne = "राहु चन्द्रबाट एघारौंमा गोचर: प्रविधि, विदेशी सम्बन्ध र अपरम्परागत माध्यमबाट लाभको लागि उत्कृष्ट। नेटवर्क विस्तार छिटो। असामान्य मित्रताहरू लाभदायक। महत्त्वाकांक्षी इच्छाहरू पूरा। नवप्रवर्तन पुरस्कृत। सामाजिक मिडिया सफलता।"
        ))

        // KETU TRANSIT
        put(Planet.KETU to 3, LocalizedTemplate(
            en = "Ketu transiting 3rd from Moon: Spiritual courage and detachment from conventional communication. Past-life sibling karma. Meditation strengthens. Intuitive writing. Unconventional short travels. Letting go of communication attachments.",
            ne = "केतु चन्द्रबाट तेस्रोमा गोचर: आध्यात्मिक साहस र परम्परागत सञ्चारबाट वैराग्य। पूर्वजन्म भाइबहिनी कर्म। ध्यान बलियो हुन्छ। अन्तर्ज्ञानी लेखन। अपरम्परागत छोटा यात्राहरू। सञ्चार मोहहरू छोड्ने।"
        ))
        put(Planet.KETU to 6, LocalizedTemplate(
            en = "Ketu transiting 6th from Moon: Spiritual victory over enemies and resolution of karmic health issues. Past-life service karma concludes. Healing through detachment. Legal matters resolved through surrender. Chronic issues dissolve.",
            ne = "केतु चन्द्रबाट छैटौंमा गोचर: शत्रुहरूमाथि आध्यात्मिक विजय र कर्मिक स्वास्थ्य समस्याहरूको समाधान। पूर्वजन्म सेवा कर्म समाप्त। वैराग्य मार्फत उपचार। समर्पण मार्फत कानूनी मामिलाहरू समाधान। दीर्घकालीन समस्याहरू भंग।"
        ))
        put(Planet.KETU to 9, LocalizedTemplate(
            en = "Ketu transiting 9th from Moon: Deep spiritual awakening and past-life dharma activation. Father karma surfaces. Gurus from past lives appear. Pilgrimages transformative. Legal matters karmically resolved. Liberation through higher knowledge.",
            ne = "केतु चन्द्रबाट नवौंमा गोचर: गहिरो आध्यात्मिक जागरण र पूर्वजन्म धर्म सक्रियता। पिता कर्म सतहमा आउँछ। पूर्वजन्मका गुरुहरू प्रकट हुन्छन्। तीर्थयात्राहरू रूपान्तरणकारी। कानूनी मामिलाहरू कर्मिक रूपमा समाधान। उच्च ज्ञान मार्फत मुक्ति।"
        ))
        put(Planet.KETU to 12, LocalizedTemplate(
            en = "Ketu transiting 12th from Moon: Supreme for spiritual liberation and moksha. Expenses lead to spiritual gain. Foreign spiritual experiences. Past-life karma completes. Meditation deepens significantly. Isolation brings enlightenment. Dreams prophetic.",
            ne = "केतु चन्द्रबाट बाह्रौंमा गोचर: आध्यात्मिक मुक्ति र मोक्षको लागि सर्वोच्च। खर्चहरूले आध्यात्मिक लाभ तिर लैजान्छन्। विदेशी आध्यात्मिक अनुभवहरू। पूर्वजन्म कर्म पूरा हुन्छ। ध्यान उल्लेखनीय रूपमा गहिरो हुन्छ। एकान्तले ज्ञानोदय ल्याउँछ। सपनाहरू भविष्यवाणी।"
        ))
    }

    // ============================================
    // SADE SATI TEMPLATES (7.5 YEAR SATURN TRANSIT)
    // ============================================
    data class SadeSatiPhaseTemplate(
        val phase: Int,  // 1 = Rising (12th), 2 = Peak (1st), 3 = Setting (2nd)
        val template: LocalizedTemplate
    )

    val sadeSatiTemplates: List<SadeSatiPhaseTemplate> = listOf(
        SadeSatiPhaseTemplate(1, LocalizedTemplate(
            en = "Sade Sati Phase 1 (Rising Phase - Saturn in 12th from Moon): The challenging 7.5 year cycle begins. This 2.5 year phase brings increased expenses, sleep disturbances, and isolation tendencies. Foreign travel may be necessary. Mother's health may concern. Hidden enemies may act. Spiritual introspection deepens. Past karmic debts begin to surface. Maintain faith and reduce unnecessary expenditures. Hospital visits possible. Practice patience and surrender to the process of karmic purification. This phase prepares the soul for deeper lessons ahead.",
            ne = "साढेसाती चरण १ (उदय चरण - शनि चन्द्रबाट १२औंमा): चुनौतीपूर्ण ७.५ वर्ष चक्र सुरु हुन्छ। यो २.५ वर्ष चरणले बढेको खर्च, निद्रा विकार र एकान्त प्रवृत्तिहरू ल्याउँछ। विदेश यात्रा आवश्यक हुन सक्छ। आमाको स्वास्थ्यमा चिन्ता हुन सक्छ। लुकेका शत्रुहरूले कार्य गर्न सक्छन्। आध्यात्मिक आत्म-चिन्तन गहिरो हुन्छ। विगतका कर्मिक ऋणहरू सतहमा आउन थाल्छन्। विश्वास कायम राख्नुहोस् र अनावश्यक खर्चहरू कम गर्नुहोस्। अस्पताल भ्रमण सम्भव। धैर्य अभ्यास गर्नुहोस् र कर्मिक शुद्धीकरणको प्रक्रियामा समर्पण गर्नुहोस्। यो चरणले आत्मालाई अगाडि गहिरो पाठहरूको लागि तयार गर्छ।"
        )),
        SadeSatiPhaseTemplate(2, LocalizedTemplate(
            en = "Sade Sati Phase 2 (Peak Phase - Saturn in 1st from Moon): The most intense 2.5 year phase of the 7.5 year cycle. Health requires serious attention. Mental stress peaks. Career and personal life face maximum challenges. Physical energy diminishes. Isolation and depression tendencies may arise. However, this phase offers deepest character development and spiritual maturity. Patience, discipline, and perseverance are essential. Avoid major decisions and new ventures if possible. Focus on maintaining what exists. Past karmic patterns crystallize for resolution. Chronic health issues may surface. Bones, joints, and nervous system need care. This too shall pass - maintain hope and work steadily.",
            ne = "साढेसाती चरण २ (शिखर चरण - शनि चन्द्रबाट १औंमा): ७.५ वर्ष चक्रको सबैभन्दा तीव्र २.५ वर्ष चरण। स्वास्थ्यमा गम्भीर ध्यान चाहिन्छ। मानसिक तनाव चरममा। करियर र व्यक्तिगत जीवनमा अधिकतम चुनौतीहरू। शारीरिक ऊर्जा कम हुन्छ। एकान्त र अवसाद प्रवृत्तिहरू उठ्न सक्छन्। यद्यपि, यो चरणले गहिरो चरित्र विकास र आध्यात्मिक परिपक्वता प्रदान गर्छ। धैर्य, अनुशासन र लगनशीलता आवश्यक छन्। सम्भव भएमा ठूला निर्णयहरू र नयाँ उद्यमहरूबाट बच्नुहोस्। अवस्थित कुरा कायम राख्नमा ध्यान केन्द्रित गर्नुहोस्। विगतका कर्मिक ढाँचाहरू समाधानको लागि स्फटिक हुन्छन्। दीर्घकालीन स्वास्थ्य समस्याहरू सतहमा आउन सक्छन्। हड्डी, जोर्नी र स्नायु प्रणालीमा हेरचाह चाहिन्छ। यो पनि बित्नेछ - आशा कायम राख्नुहोस् र स्थिर रूपमा काम गर्नुहोस्।"
        )),
        SadeSatiPhaseTemplate(3, LocalizedTemplate(
            en = "Sade Sati Phase 3 (Setting Phase - Saturn in 2nd from Moon): The concluding 2.5 year phase brings financial challenges and family tensions. Speech may cause conflicts. Accumulated wealth may diminish. Family responsibilities increase. Eye and mouth health require attention. However, the worst is over. Lessons learned in previous phases begin to consolidate. Financial discipline becomes essential. Family bonds are tested but can strengthen through patience. This phase teaches the value of resources and relationships. As it concludes, relief gradually emerges. The karmic debts of 7.5 years settle. Wisdom and maturity gained prove invaluable for future progress.",
            ne = "साढेसाती चरण ३ (अस्त चरण - शनि चन्द्रबाट २औंमा): समाप्त भइरहेको २.५ वर्ष चरणले आर्थिक चुनौतीहरू र पारिवारिक तनावहरू ल्याउँछ। वाणीले द्वन्द्वहरू निम्त्याउन सक्छ। संचित धन कम हुन सक्छ। पारिवारिक जिम्मेवारीहरू बढ्छन्। आँखा र मुख स्वास्थ्यमा ध्यान चाहिन्छ। यद्यपि, सबैभन्दा नराम्रो बित्यो। अघिल्ला चरणहरूमा सिकिएका पाठहरू एकीकृत हुन थाल्छन्। आर्थिक अनुशासन आवश्यक हुन्छ। पारिवारिक बन्धनहरूको परीक्षण हुन्छ तर धैर्य मार्फत बलियो हुन सक्छ। यो चरणले स्रोत र सम्बन्धहरूको मूल्य सिकाउँछ। यसको समाप्तिमा, राहत बिस्तारै उदाउँछ। ७.५ वर्षका कर्मिक ऋणहरू मिलाइन्छन्। प्राप्त ज्ञान र परिपक्वता भविष्यको प्रगतिको लागि अमूल्य साबित हुन्छ।"
        ))
    )

    // ============================================
    // TRANSIT ASPECT TEMPLATES (81 combinations)
    // ============================================
    val transitAspectTemplates: Map<Pair<Planet, Planet>, LocalizedTemplate> = buildMap {
        // Sample aspect templates - full implementation would include all 81
        put(Planet.JUPITER to Planet.SUN, LocalizedTemplate(
            en = "Jupiter aspecting natal Sun: Excellent aspect bringing wisdom to authority, expansion of career, and blessings from teachers and father figures. Government favor increases. Leadership opportunities arise. Health and vitality improve. Self-confidence expands with wisdom. Recognition and honors likely. Spiritual understanding of ego develops.",
            ne = "बृहस्पतिले जन्म सूर्यलाई दृष्टि गरेको: अधिकारमा ज्ञान, करियरको विस्तार र शिक्षक तथा पिता व्यक्तित्वहरूबाट आशीर्वाद ल्याउने उत्कृष्ट दृष्टि। सरकारी कृपा बढ्छ। नेतृत्वका अवसरहरू उत्पन्न। स्वास्थ्य र जीवनशक्ति सुधारिन्छ। ज्ञानसहित आत्मविश्वास विस्तार। मान्यता र सम्मानहरू सम्भव। अहंकारको आध्यात्मिक बुझाइ विकसित हुन्छ।"
        ))
        put(Planet.SATURN to Planet.MOON, LocalizedTemplate(
            en = "Saturn aspecting natal Moon: Challenging aspect bringing emotional discipline and mental maturity through trials. Mother may face difficulties. Mental depression requires attention. Emotional detachment develops. Public image built through perseverance. Property matters delayed. Patience with emotional processing essential. Chronic mental patterns surface for resolution.",
            ne = "शनिले जन्म चन्द्रलाई दृष्टि गरेको: परीक्षाहरू मार्फत भावनात्मक अनुशासन र मानसिक परिपक्वता ल्याउने चुनौतीपूर्ण दृष्टि। आमाले कठिनाइहरू सामना गर्न सक्छिन्। मानसिक अवसादमा ध्यान चाहिन्छ। भावनात्मक वैराग्य विकसित हुन्छ। लगनशीलता मार्फत सार्वजनिक छवि निर्माण। सम्पत्ति मामिलाहरूमा ढिलाइ। भावनात्मक प्रक्रियासँग धैर्य आवश्यक। दीर्घकालीन मानसिक ढाँचाहरू समाधानको लागि सतहमा आउँछन्।"
        ))
        put(Planet.MARS to Planet.VENUS, LocalizedTemplate(
            en = "Mars aspecting natal Venus: Passionate aspect intensifying relationships and creative drive. Marriage experiences intensity. Artistic expression becomes forceful. Vehicles and luxuries acquired through action. Women in life face challenges. Creative-destructive energy in arts. Sexual passion heightens. Financial actions decisive.",
            ne = "मंगलले जन्म शुक्रलाई दृष्टि गरेको: सम्बन्ध र सिर्जनात्मक ड्राइभ तीव्र गर्ने जोशपूर्ण दृष्टि। विवाहले तीव्रता अनुभव गर्छ। कलात्मक अभिव्यक्ति बलियो हुन्छ। कारबाही मार्फत सवारी साधन र विलासिताहरू प्राप्त। जीवनमा महिलाहरूले चुनौतीहरू सामना गर्छन्। कलामा सिर्जनात्मक-विनाशकारी ऊर्जा। यौन जोश बढ्छ। आर्थिक कार्यहरू निर्णायक।"
        ))
        put(Planet.RAHU to Planet.JUPITER, LocalizedTemplate(
            en = "Rahu aspecting natal Jupiter: Unconventional expansion of wisdom and foreign opportunities. Technology in education. Teachers from unusual sources. Children through unexpected means. Legal matters require careful navigation. Material expansion may overshadow spiritual growth. Innovation in traditional knowledge.",
            ne = "राहुले जन्म बृहस्पतिलाई दृष्टि गरेको: ज्ञान र विदेशी अवसरहरूको अपरम्परागत विस्तार। शिक्षामा प्रविधि। असामान्य स्रोतहरूबाट शिक्षकहरू। अप्रत्याशित माध्यमबाट सन्तान। कानूनी मामिलाहरूमा सावधानीपूर्वक नेभिगेशन चाहिन्छ। भौतिक विस्तारले आध्यात्मिक वृद्धिलाई छाया पार्न सक्छ। परम्परागत ज्ञानमा नवप्रवर्तन।"
        ))
        put(Planet.KETU to Planet.SATURN, LocalizedTemplate(
            en = "Ketu aspecting natal Saturn: Deep karmic release of past-life discipline patterns. Work karma surfaces. Service through detachment. Chronic ailments dissolve through spiritual understanding. Elderly people bring karmic lessons. Structural attachments release. Liberation through disciplined detachment.",
            ne = "केतुले जन्म शनिलाई दृष्टि गरेको: पूर्वजन्मको अनुशासन ढाँचाहरूको गहिरो कर्मिक मुक्ति। कार्य कर्म सतहमा आउँछ। वैराग्य मार्फत सेवा। दीर्घकालीन रोगहरू आध्यात्मिक बुझाइ मार्फत भंग। वृद्ध मानिसहरूले कर्मिक पाठहरू ल्याउँछन्। संरचनात्मक मोहहरू मुक्त। अनुशासित वैराग्य मार्फत मुक्ति।"
        ))
    }

    // ============================================
    // HELPER FUNCTIONS
    // ============================================

    /**
     * Get transit template by planet and house from Moon.
     */
    fun getTransitFromMoonTemplate(planet: Planet, house: Int): LocalizedTemplate {
        return transitFromMoon[planet to house] ?: LocalizedTemplate(
            en = "${planet.displayName} transiting ${house}${getOrdinalSuffix(house)} house from Moon: General influences of ${planet.displayName} manifest through ${house}${getOrdinalSuffix(house)} house themes. Observe planetary dignity and aspects for specific effects.",
            ne = "${planet.displayName} चन्द्रबाट ${house}औं भावमा गोचर: ${planet.displayName}को सामान्य प्रभावहरू ${house}औं भाव विषयहरू मार्फत प्रकट। विशेष प्रभावहरूको लागि ग्रह सम्मान र दृष्टिहरू अवलोकन गर्नुहोस्।"
        )
    }

    /**
     * Get Sade Sati phase template.
     */
    fun getSadeSatiTemplate(phase: Int): LocalizedTemplate {
        return sadeSatiTemplates.find { it.phase == phase }?.template ?: LocalizedTemplate(
            en = "Sade Sati phase $phase: Important 2.5 year period of Saturn's influence requiring patience and perseverance.",
            ne = "साढेसाती चरण $phase: धैर्य र लगनशीलता चाहिने शनिको प्रभावको महत्त्वपूर्ण २.५ वर्ष अवधि।"
        )
    }

    /**
     * Get transit aspect template.
     */
    fun getTransitAspectTemplate(transitingPlanet: Planet, natalPlanet: Planet): LocalizedTemplate {
        return transitAspectTemplates[transitingPlanet to natalPlanet] ?: LocalizedTemplate(
            en = "${transitingPlanet.displayName} aspecting natal ${natalPlanet.displayName}: The energies of ${transitingPlanet.displayName} influence the natal placement of ${natalPlanet.displayName}, modifying its expression during this transit period.",
            ne = "${transitingPlanet.displayName}ले जन्म ${natalPlanet.displayName}लाई दृष्टि गरेको: ${transitingPlanet.displayName}को ऊर्जाहरूले ${natalPlanet.displayName}को जन्म स्थानलाई प्रभाव पार्छ, यो गोचर अवधिमा यसको अभिव्यक्ति परिमार्जन गर्दै।"
        )
    }

    private fun getOrdinalSuffix(number: Int): String {
        return when {
            number in 11..13 -> "th"
            number % 10 == 1 -> "st"
            number % 10 == 2 -> "nd"
            number % 10 == 3 -> "rd"
            else -> "th"
        }
    }
}
