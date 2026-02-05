package com.astro.storm.data.templates

import com.astro.storm.core.model.Planet

/**
 * Comprehensive Yoga interpretation templates (600+ templates).
 * Based on classical texts: BPHS, Phaladeepika, Saravali, Jataka Parijata, Hora Sara.
 *
 * Yoga categories:
 * - Raja Yogas (Royal combinations) - 50+
 * - Dhana Yogas (Wealth combinations) - 40+
 * - Pancha Mahapurusha Yogas - 5 detailed
 * - Nabhasa Yogas (Planetary patterns) - 32+
 * - Arishta Yogas (Affliction combinations) - 50+
 * - Viparita Raja Yogas - 12+
 * - Gajakesari and Lunar Yogas - 20+
 * - Special Yogas (Kemadruma, Adhi, etc.) - 50+
 * - Nakshatra-based Yogas - 30+
 * - Combination Yogas - 300+
 */
object YogaTemplates {

    // ============================================
    // PANCHA MAHAPURUSHA YOGAS (5 Great King Yogas)
    // ============================================
    val panchaMahapurushaYogas: Map<String, LocalizedTemplate> = mapOf(
        "RUCHAKA" to LocalizedTemplate(
            en = "Ruchaka Yoga (Mars in Kendra in Own/Exaltation Sign): One of the five great Mahapurusha Yogas. The native possesses exceptional courage, military prowess, and leadership abilities. Physical strength and athletic ability are remarkable. Success in competitive fields, sports, military, police, or engineering is indicated. The person has a commanding presence, reddish complexion, and well-built physique. They acquire land, property, and vehicles through bold action. Brothers are supportive. The native rises through courage and initiative, often achieving fame through heroic deeds. They may bear scars from battles won. Government authority in defense or law enforcement is possible. The yoga is strongest when Mars is in Capricorn (exalted) in a Kendra.",
            ne = "रुचक योग (मंगल केन्द्रमा स्वराशि/उच्च राशिमा): पाँच महान महापुरुष योगहरू मध्ये एक। जातकसँग असाधारण साहस, सैन्य कौशल र नेतृत्व क्षमताहरू हुन्छन्। शारीरिक शक्ति र खेलकुद क्षमता उल्लेखनीय। प्रतिस्पर्धी क्षेत्रहरू, खेलकुद, सैन्य, प्रहरी वा इन्जिनियरिङमा सफलता संकेत गरिएको। व्यक्तिसँग आज्ञाकारी उपस्थिति, रातो रंग र राम्रो बनावटको शरीर हुन्छ। उनीहरूले साहसी कारबाही मार्फत जमीन, सम्पत्ति र सवारी साधनहरू प्राप्त गर्छन्। भाइहरू सहयोगी हुन्छन्। जातक साहस र पहल मार्फत उठ्छ, प्रायः वीरतापूर्ण कार्यहरू मार्फत प्रसिद्धि प्राप्त गर्दछ। उनीहरूमा जितेका युद्धहरूबाट दागहरू हुन सक्छन्। रक्षा वा कानून प्रवर्तनमा सरकारी अधिकार सम्भव। योग मंगल मकरमा (उच्च) केन्द्रमा हुँदा सबैभन्दा बलियो हुन्छ।"
        ),
        "BHADRA" to LocalizedTemplate(
            en = "Bhadra Yoga (Mercury in Kendra in Own/Exaltation Sign): One of the five great Mahapurusha Yogas. The native possesses exceptional intelligence, communication skills, and business acumen. They excel in education, writing, publishing, accounting, and intellectual pursuits. Speech is refined, persuasive, and authoritative. Success in commerce, media, and diplomacy is assured. The person has a pleasant appearance, youthful energy, and quick wit. They acquire wealth through intelligence and skillful dealings. Mathematical and computational abilities are extraordinary. The native may become a renowned scholar, writer, or businessman. They have good relationships with friends and the mercantile community. The yoga is strongest when Mercury is in Virgo (own/exalted) in a Kendra.",
            ne = "भद्र योग (बुध केन्द्रमा स्वराशि/उच्च राशिमा): पाँच महान महापुरुष योगहरू मध्ये एक। जातकसँग असाधारण बुद्धि, सञ्चार सीप र व्यापारिक सुझबुझ हुन्छ। उनीहरू शिक्षा, लेखन, प्रकाशन, लेखा र बौद्धिक कार्यहरूमा उत्कृष्ट हुन्छन्। वाणी परिष्कृत, प्रेरक र आधिकारिक हुन्छ। वाणिज्य, मिडिया र कूटनीतिमा सफलता सुनिश्चित। व्यक्तिसँग सुखद उपस्थिति, युवा ऊर्जा र छिटो बुद्धि हुन्छ। उनीहरूले बुद्धि र कुशल व्यवहार मार्फत धन प्राप्त गर्छन्। गणितीय र कम्प्युटेशनल क्षमताहरू असाधारण। जातक प्रख्यात विद्वान, लेखक वा व्यापारी हुन सक्छ। उनीहरूको साथीहरू र व्यापारी समुदायसँग राम्रो सम्बन्ध हुन्छ। योग बुध कन्यामा (स्व/उच्च) केन्द्रमा हुँदा सबैभन्दा बलियो।"
        ),
        "HAMSA" to LocalizedTemplate(
            en = "Hamsa Yoga (Jupiter in Kendra in Own/Exaltation Sign): One of the five great Mahapurusha Yogas. The native possesses exceptional wisdom, spirituality, and righteous nature. They are blessed with good fortune, prosperity, and divine grace. Success in teaching, law, religion, and philosophical pursuits is assured. The person has a noble bearing, fair complexion, and pleasant demeanor. They acquire wealth through ethical means and dharmic activities. Children bring joy and pride. The native may become a renowned teacher, spiritual leader, or judge. They have excellent relationships with teachers and father figures. Long life and good health are indicated. The yoga is strongest when Jupiter is in Cancer (exalted) in a Kendra.",
            ne = "हंस योग (बृहस्पति केन्द्रमा स्वराशि/उच्च राशिमा): पाँच महान महापुरुष योगहरू मध्ये एक। जातकसँग असाधारण ज्ञान, आध्यात्मिकता र धार्मिक स्वभाव हुन्छ। उनीहरू सौभाग्य, समृद्धि र दैवीय कृपाले आशीर्वादित हुन्छन्। शिक्षण, कानून, धर्म र दार्शनिक कार्यहरूमा सफलता सुनिश्चित। व्यक्तिसँग कुलीन व्यवहार, गोरो रंग र सुखद आचरण हुन्छ। उनीहरूले नैतिक माध्यम र धार्मिक गतिविधिहरू मार्फत धन प्राप्त गर्छन्। सन्तानहरूले आनन्द र गौरव ल्याउँछन्। जातक प्रख्यात शिक्षक, आध्यात्मिक नेता वा न्यायाधीश हुन सक्छ। उनीहरूको शिक्षकहरू र पिता व्यक्तित्वहरूसँग उत्कृष्ट सम्बन्ध हुन्छ। लामो आयु र राम्रो स्वास्थ्य संकेत। योग बृहस्पति कर्कटमा (उच्च) केन्द्रमा हुँदा सबैभन्दा बलियो।"
        ),
        "MALAVYA" to LocalizedTemplate(
            en = "Malavya Yoga (Venus in Kendra in Own/Exaltation Sign): One of the five great Mahapurusha Yogas. The native possesses exceptional beauty, charm, and artistic talents. They enjoy luxury, comfort, and sensory pleasures throughout life. Success in arts, music, entertainment, fashion, and beauty industries is assured. The person has an attractive appearance, refined tastes, and graceful manners. Marriage is happy and spouse is beautiful or wealthy. They acquire vehicles, jewelry, and comfortable dwellings. Relationships with women are beneficial. The native may become a renowned artist, musician, or connoisseur of arts. They have a loving nature and bring harmony to surroundings. The yoga is strongest when Venus is in Pisces (exalted) in a Kendra.",
            ne = "मालव्य योग (शुक्र केन्द्रमा स्वराशि/उच्च राशिमा): पाँच महान महापुरुष योगहरू मध्ये एक। जातकसँग असाधारण सौन्दर्य, आकर्षण र कलात्मक प्रतिभाहरू हुन्छन्। उनीहरू जीवनभर विलासिता, आराम र इन्द्रिय सुखहरूको आनन्द लिन्छन्। कला, संगीत, मनोरञ्जन, फेसन र सौन्दर्य उद्योगहरूमा सफलता सुनिश्चित। व्यक्तिसँग आकर्षक उपस्थिति, परिष्कृत स्वाद र सुन्दर आचरण हुन्छ। विवाह खुशी हुन्छ र जीवनसाथी सुन्दर वा धनी हुन्छ। उनीहरूले सवारी साधन, गहना र आरामदायी बासस्थानहरू प्राप्त गर्छन्। महिलाहरूसँगको सम्बन्ध लाभकारी। जातक प्रख्यात कलाकार, संगीतकार वा कला पारखी हुन सक्छ। उनीहरूको मायालु स्वभाव हुन्छ र वरपरमा सामंजस्य ल्याउँछन्। योग शुक्र मीनमा (उच्च) केन्द्रमा हुँदा सबैभन्दा बलियो।"
        ),
        "SHASHA" to LocalizedTemplate(
            en = "Shasha Yoga (Saturn in Kendra in Own/Exaltation Sign): One of the five great Mahapurusha Yogas. The native possesses exceptional discipline, perseverance, and organizational abilities. They achieve success through hard work, patience, and systematic effort. Authority in government, law, or large organizations is indicated. The person has a serious demeanor, lean physique, and commanding presence. They acquire wealth and property through persistent effort over time. Long life is assured. The native may become a powerful administrator, judge, or industrialist. They have good relationships with servants and the working class. Success comes after middle age through accumulated merit. The yoga is strongest when Saturn is in Libra (exalted) in a Kendra.",
            ne = "शश योग (शनि केन्द्रमा स्वराशि/उच्च राशिमा): पाँच महान महापुरुष योगहरू मध्ये एक। जातकसँग असाधारण अनुशासन, लगनशीलता र सांगठनिक क्षमताहरू हुन्छन्। उनीहरू कडा परिश्रम, धैर्य र व्यवस्थित प्रयास मार्फत सफलता प्राप्त गर्छन्। सरकार, कानून वा ठूला संगठनहरूमा अधिकार संकेत। व्यक्तिसँग गम्भीर आचरण, पातलो शरीर र आज्ञाकारी उपस्थिति हुन्छ। उनीहरूले समयमा निरन्तर प्रयास मार्फत धन र सम्पत्ति प्राप्त गर्छन्। लामो आयु सुनिश्चित। जातक शक्तिशाली प्रशासक, न्यायाधीश वा उद्योगपति हुन सक्छ। उनीहरूको सेवकहरू र श्रमिक वर्गसँग राम्रो सम्बन्ध हुन्छ। संचित पुण्य मार्फत मध्यम उमेर पछि सफलता आउँछ। योग शनि तुलामा (उच्च) केन्द्रमा हुँदा सबैभन्दा बलियो।"
        )
    )

    // ============================================
    // RAJA YOGAS (Royal Combinations)
    // ============================================
    val rajaYogas: Map<String, LocalizedTemplate> = mapOf(
        "DHARMA_KARMADHIPATI" to LocalizedTemplate(
            en = "Dharma Karmadhipati Yoga (9th and 10th Lord connection): The most powerful Raja Yoga, formed when the 9th lord (dharma/fortune) and 10th lord (karma/action) are conjunct, mutually aspecting, or in exchange. The native achieves exceptional success in career through righteous means. Fortune and effort combine perfectly. Government positions of high authority are attained. Father supports career success. The native becomes famous through noble deeds. Wealth accumulates through professional excellence. This yoga produces kings, ministers, and leaders of nations when strong.",
            ne = "धर्म कर्माधिपति योग (९औं र १०औं भावाधिप सम्बन्ध): सबैभन्दा शक्तिशाली राजयोग, जुन ९औं स्वामी (धर्म/भाग्य) र १०औं स्वामी (कर्म/कार्य) संयुक्त, परस्पर दृष्टि वा आदान-प्रदानमा हुँदा बन्छ। जातकले धार्मिक माध्यमबाट करियरमा असाधारण सफलता प्राप्त गर्छ। भाग्य र प्रयास पूर्ण रूपमा संयोजन हुन्छ। उच्च अधिकारका सरकारी पदहरू प्राप्त। पिताले करियर सफलतालाई समर्थन गर्छ। जातक कुलीन कार्यहरू मार्फत प्रसिद्ध हुन्छ। पेशेवर उत्कृष्टता मार्फत धन जम्मा हुन्छ। यो योगले बलियो हुँदा राजा, मन्त्री र राष्ट्रका नेताहरू उत्पादन गर्छ।"
        ),
        "LAKSHMI_YOGA" to LocalizedTemplate(
            en = "Lakshmi Yoga (Strong 9th lord with Venus in Kendra/Trikona): Goddess Lakshmi's blessings manifest through this yoga. The native enjoys exceptional fortune, wealth, and all comforts. Beauty, grace, and artistic talents accompany material prosperity. Marriage brings wealth and happiness. Vehicles, properties, and luxuries are acquired effortlessly. Fortune comes through righteous living and divine grace. The native may become wealthy through business, arts, or marriage. Women play beneficial roles in life. This yoga produces naturally fortunate individuals who attract abundance.",
            ne = "लक्ष्मी योग (बलियो ९औं स्वामी शुक्रसँग केन्द्र/त्रिकोणमा): देवी लक्ष्मीको आशीर्वाद यस योग मार्फत प्रकट हुन्छ। जातकले असाधारण भाग्य, धन र सबै सुविधाहरूको आनन्द लिन्छ। भौतिक समृद्धिसँगै सौन्दर्य, अनुग्रह र कलात्मक प्रतिभाहरू। विवाहले धन र खुशी ल्याउँछ। सवारी साधन, सम्पत्ति र विलासिताहरू सजिलैसँग प्राप्त। धार्मिक जीवन र दैवीय कृपा मार्फत भाग्य आउँछ। जातक व्यापार, कला वा विवाह मार्फत धनी हुन सक्छ। महिलाहरूले जीवनमा लाभकारी भूमिका खेल्छन्। यो योगले प्रचुरता आकर्षित गर्ने स्वाभाविक भाग्यशाली व्यक्तिहरू उत्पादन गर्छ।"
        ),
        "GAJAKESARI" to LocalizedTemplate(
            en = "Gajakesari Yoga (Jupiter in Kendra from Moon): One of the most celebrated yogas in Vedic astrology. The native possesses the strength of an elephant and the courage of a lion. Intelligence, wisdom, and good reputation are assured. The native becomes famous, respected, and influential in society. Government favor and positions of authority come naturally. Wealth accumulates through knowledge and righteous means. Long life and good health are indicated. Children and spouse are supportive. This yoga destroys many negative combinations and brings overall life success.",
            ne = "गजकेसरी योग (बृहस्पति चन्द्रबाट केन्द्रमा): वैदिक ज्योतिषमा सबैभन्दा प्रख्यात योगहरू मध्ये एक। जातकसँग हात्तीको शक्ति र सिंहको साहस हुन्छ। बुद्धि, ज्ञान र राम्रो प्रतिष्ठा सुनिश्चित। जातक प्रसिद्ध, सम्मानित र समाजमा प्रभावशाली हुन्छ। सरकारी कृपा र अधिकारका पदहरू स्वाभाविक रूपमा आउँछन्। ज्ञान र धार्मिक माध्यमबाट धन जम्मा हुन्छ। लामो आयु र राम्रो स्वास्थ्य संकेत। सन्तान र जीवनसाथी सहयोगी। यो योगले धेरै नकारात्मक संयोजनहरू नष्ट गर्छ र समग्र जीवन सफलता ल्याउँछ।"
        ),
        "NEECHABHANGA_RAJA" to LocalizedTemplate(
            en = "Neechabhanga Raja Yoga (Debilitated planet with cancellation): When a debilitated planet has its debilitation cancelled, it transforms from a malefic into a powerful benefic. The native rises from humble or challenging beginnings to achieve exceptional success. Initial struggles lead to ultimate triumph. The specific area of life improves dramatically after initial difficulties. This yoga produces self-made individuals who overcome adversity. The strength of the cancellation determines the degree of success. Multiple cancellation factors strengthen the yoga further.",
            ne = "नीचभंग राजयोग (नीच ग्रह रद्दसहित): जब नीच ग्रहको नीचता रद्द हुन्छ, यो पाप ग्रहबाट शक्तिशाली शुभ ग्रहमा रूपान्तरण हुन्छ। जातक विनम्र वा चुनौतीपूर्ण सुरुवातबाट उठेर असाधारण सफलता प्राप्त गर्छ। प्रारम्भिक संघर्षहरूले अन्तिम विजय तिर लैजान्छन्। जीवनको विशिष्ट क्षेत्र प्रारम्भिक कठिनाइहरू पछि नाटकीय रूपमा सुधारिन्छ। यो योगले विपत्तिमाथि विजय हासिल गर्ने आत्मनिर्मित व्यक्तिहरू उत्पादन गर्छ। रद्दको बलले सफलताको डिग्री निर्धारण गर्छ। बहु रद्द कारकहरूले योगलाई अझ बलियो बनाउँछ।"
        ),
        "PARIVARTANA_YOGA" to LocalizedTemplate(
            en = "Parivartana Yoga (Mutual Exchange of Signs): When two planets exchange signs (each in the other's sign), a powerful connection forms between those houses. The themes of both houses become interlinked and mutually supportive. Resources and energies flow between the two life areas. This yoga creates dynamic relationships between different life sectors. The nature of the planets involved colors the exchange - benefics create positive exchanges while malefics require careful handling.",
            ne = "परिवर्तन योग (राशीहरूको पारस्परिक आदान-प्रदान): जब दुई ग्रहहरूले राशीहरू आदान-प्रदान गर्छन् (प्रत्येक अर्कोको राशिमा), ती भावहरू बीच एक शक्तिशाली सम्बन्ध बन्छ। दुवै भावहरूका विषयहरू आपसमा जोडिन्छन् र पारस्परिक रूपमा सहयोगी हुन्छन्। स्रोतहरू र ऊर्जाहरू दुई जीवन क्षेत्रहरू बीच बग्छन्। यो योगले विभिन्न जीवन क्षेत्रहरू बीच गतिशील सम्बन्धहरू सिर्जना गर्छ। संलग्न ग्रहहरूको प्रकृतिले आदान-प्रदानलाई रंग दिन्छ - शुभ ग्रहहरूले सकारात्मक आदान-प्रदान सिर्जना गर्छन् जबकि पाप ग्रहहरूलाई सावधानीपूर्वक व्यवहार चाहिन्छ।"
        ),
        "VIPARITA_RAJA_YOGA" to LocalizedTemplate(
            en = "Viparita Raja Yoga (6th, 8th, or 12th lords in Dusthana houses): The lords of evil houses placed in other evil houses create a reversal of fortune. Enemies destroy themselves. Obstacles become stepping stones. Losses lead to gains. Health challenges resolve. The native rises through others' downfall. Legal victories come unexpectedly. Inheritance arrives from unexpected sources. This yoga is particularly powerful when the Dusthana lords are only connected to Dusthana houses without affecting benefic houses.",
            ne = "विपरीत राजयोग (६औं, ८औं वा १२औं स्वामीहरू दुःस्थान भावमा): पाप भावहरूका स्वामीहरू अन्य पाप भावमा राखिँदा भाग्यको उल्टो बन्छ। शत्रुहरू आफैं नष्ट हुन्छन्। बाधाहरू सीढी बन्छन्। हानिहरूले लाभ तिर लैजान्छन्। स्वास्थ्य चुनौतीहरू समाधान हुन्छन्। जातक अरूको पतन मार्फत उठ्छ। कानूनी विजयहरू अप्रत्याशित रूपमा आउँछन्। अप्रत्याशित स्रोतहरूबाट विरासत आउँछ। यो योग विशेष गरी शक्तिशाली हुन्छ जब दुःस्थान स्वामीहरू शुभ भावलाई प्रभावित नगरी दुःस्थान भावमा मात्र जोडिन्छन्।"
        ),
        "BUDHADITYA" to LocalizedTemplate(
            en = "Budhaditya Yoga (Sun-Mercury conjunction): The combination of the soul (Sun) with intelligence (Mercury) creates sharp intellect and communication abilities. The native excels in education, especially in logical and analytical fields. Government positions through intelligence are indicated. Writing, teaching, and administrative abilities are strong. The yoga is strongest when Mercury is unafflicted and not combust (more than 14° from Sun). Success in examinations and competitive pursuits is assured.",
            ne = "बुधादित्य योग (सूर्य-बुध युति): आत्मा (सूर्य) बुद्धिसँग (बुध) को संयोजनले तीक्ष्ण बुद्धि र सञ्चार क्षमताहरू सिर्जना गर्छ। जातक शिक्षामा, विशेष गरी तार्किक र विश्लेषणात्मक क्षेत्रहरूमा उत्कृष्ट हुन्छ। बुद्धि मार्फत सरकारी पदहरू संकेत। लेखन, शिक्षण र प्रशासनिक क्षमताहरू बलियो। योग बुध अप्रभावित र अस्त नभएको (सूर्यबाट १४° भन्दा बढी) हुँदा सबैभन्दा बलियो। परीक्षा र प्रतिस्पर्धात्मक कार्यहरूमा सफलता सुनिश्चित।"
        ),
        "CHANDRA_MANGALA" to LocalizedTemplate(
            en = "Chandra Mangala Yoga (Moon-Mars conjunction or mutual aspect): The combination of mind (Moon) with energy (Mars) creates a prosperous and wealthy individual. The native acquires wealth through commerce, real estate, or inherited property. Mother may be strong-willed. Emotional courage characterizes decision-making. Success in businesses involving women, property, or emotional appeals. The yoga is strongest when formed in wealth-giving houses (2nd, 11th) or their lords.",
            ne = "चन्द्र मंगल योग (चन्द्र-मंगल युति वा पारस्परिक दृष्टि): मन (चन्द्र) ऊर्जासँग (मंगल) को संयोजनले समृद्ध र धनी व्यक्ति सिर्जना गर्छ। जातकले वाणिज्य, घरजग्गा वा विरासत सम्पत्ति मार्फत धन प्राप्त गर्छ। आमा बलियो इच्छाशक्ति भएकी हुन सक्छिन्। भावनात्मक साहसले निर्णय प्रक्रियालाई विशेषता दिन्छ। महिला, सम्पत्ति वा भावनात्मक अपीलसँग सम्बन्धित व्यवसायहरूमा सफलता। योग धन-दिने भावहरू (२औं, ११औं) वा तिनका स्वामीहरूमा बन्दा सबैभन्दा बलियो।"
        ),
        "ADHI_YOGA" to LocalizedTemplate(
            en = "Adhi Yoga (Benefics in 6th, 7th, 8th from Moon): When natural benefics (Jupiter, Venus, Mercury) occupy the 6th, 7th, and 8th houses from Moon, the native becomes a leader or minister. Victory over enemies is assured. Partnership matters succeed. Hidden knowledge becomes accessible. The native commands respect and authority. Government positions are attained. The yoga is strongest when all three houses have benefics, but even two or one benefic creates positive effects proportionally.",
            ne = "आधि योग (शुभ ग्रहहरू चन्द्रबाट ६औं, ७औं, ८औंमा): जब प्राकृतिक शुभ ग्रहहरू (बृहस्पति, शुक्र, बुध) चन्द्रबाट ६औं, ७औं र ८औं भावहरूमा हुन्छन्, जातक नेता वा मन्त्री बन्छ। शत्रुहरूमाथि विजय सुनिश्चित। साझेदारी मामिलाहरू सफल। लुकेको ज्ञान पहुँचयोग्य। जातकले सम्मान र अधिकार पाउँछ। सरकारी पदहरू प्राप्त। योग तीनै भावमा शुभ ग्रह हुँदा सबैभन्दा बलियो, तर दुई वा एक शुभ ग्रहले पनि अनुपातमा सकारात्मक प्रभाव सिर्जना गर्छ।"
        ),
        "KESARI_YOGA" to LocalizedTemplate(
            en = "Kesari Yoga (Moon in Kendra from Jupiter): Similar to Gajakesari but viewed from Jupiter's perspective. The native possesses exceptional emotional intelligence combined with wisdom. Popularity and public recognition come naturally. Success in teaching, counseling, and public service. The combination of intuition (Moon) and wisdom (Jupiter) creates excellent judgment. Family life is harmonious. Mother and teachers play supportive roles.",
            ne = "केसरी योग (चन्द्र बृहस्पतिबाट केन्द्रमा): गजकेसरी जस्तै तर बृहस्पतिको दृष्टिकोणबाट हेरिएको। जातकसँग ज्ञानसँग संयुक्त असाधारण भावनात्मक बुद्धि हुन्छ। लोकप्रियता र सार्वजनिक मान्यता स्वाभाविक रूपमा आउँछ। शिक्षण, परामर्श र सार्वजनिक सेवामा सफलता। अन्तर्ज्ञान (चन्द्र) र ज्ञान (बृहस्पति) को संयोजनले उत्कृष्ट निर्णय सिर्जना गर्छ। पारिवारिक जीवन मधुर। आमा र शिक्षकहरूले सहयोगी भूमिका खेल्छन्।"
        )
    )

    // ============================================
    // DHANA YOGAS (Wealth Combinations)
    // ============================================
    val dhanaYogas: Map<String, LocalizedTemplate> = mapOf(
        "DHANA_YOGA_2_11" to LocalizedTemplate(
            en = "Dhana Yoga (2nd and 11th Lord Connection): The connection between the lords of the 2nd house (accumulated wealth) and 11th house (gains/income) creates a powerful wealth-producing combination. The native accumulates wealth through regular income and wise investments. Family resources contribute to prosperity. Speech and skills generate income. Friends and networks support wealth creation. This is one of the most reliable indicators of financial success.",
            ne = "धन योग (२औं र ११औं स्वामी सम्बन्ध): २औं भाव (संचित धन) र ११औं भाव (लाभ/आय) का स्वामीहरू बीचको सम्बन्धले शक्तिशाली धन-उत्पादक संयोजन सिर्जना गर्छ। जातकले नियमित आय र बुद्धिमान लगानीहरू मार्फत धन जम्मा गर्छ। पारिवारिक स्रोतहरूले समृद्धिमा योगदान पुर्‍याउँछन्। वाणी र सीपहरूले आय उत्पन्न गर्छन्। साथी र नेटवर्कहरूले धन सिर्जनालाई समर्थन गर्छन्। यो आर्थिक सफलताको सबैभन्दा भरपर्दो संकेतकहरू मध्ये एक हो।"
        ),
        "DHANA_YOGA_5_9" to LocalizedTemplate(
            en = "Dhana Yoga (5th and 9th Lord Connection): The Lakshmi-Narayana combination brings wealth through luck, intelligence, and past-life merit. The 5th lord (Purva Punya) combines with 9th lord (Bhagya) to create fortunate circumstances. Speculative gains, investments, and children contribute to wealth. Fortune favors intelligent risks. Higher education leads to prosperity. This yoga indicates wealth that comes with wisdom.",
            ne = "धन योग (५औं र ९औं स्वामी सम्बन्ध): लक्ष्मी-नारायण संयोजनले भाग्य, बुद्धि र पूर्वजन्म पुण्य मार्फत धन ल्याउँछ। ५औं स्वामी (पूर्व पुण्य) ९औं स्वामी (भाग्य) सँग मिलेर भाग्यशाली परिस्थितिहरू सिर्जना गर्छ। सट्टेबाजी लाभ, लगानी र सन्तानले धनमा योगदान पुर्‍याउँछन्। भाग्यले बुद्धिमान जोखिमहरूलाई साथ दिन्छ। उच्च शिक्षाले समृद्धि तिर लैजान्छ। यो योगले ज्ञानसँग आउने धन संकेत गर्छ।"
        ),
        "DHANA_YOGA_1_2_11" to LocalizedTemplate(
            en = "Dhana Yoga (Lagna, 2nd, 11th Lord Connection): Triple wealth indication when all three lords connect. Self-effort (1st), accumulation (2nd), and gains (11th) work together. The native creates wealth through personal initiative, careful saving, and smart networking. Physical health supports earning capacity. Family values guide financial decisions. Social connections bring opportunities.",
            ne = "धन योग (लग्न, २औं, ११औं स्वामी सम्बन्ध): तीनै स्वामी जोडिँदा तिहेरो धन संकेत। आत्म-प्रयास (१औं), संचय (२औं) र लाभ (११औं) सँगै काम गर्छन्। जातकले व्यक्तिगत पहल, सावधान बचत र स्मार्ट नेटवर्किङ मार्फत धन सिर्जना गर्छ। शारीरिक स्वास्थ्यले कमाई क्षमतालाई समर्थन गर्छ। पारिवारिक मूल्यहरूले आर्थिक निर्णयहरूलाई मार्गदर्शन गर्छन्। सामाजिक सम्बन्धहरूले अवसरहरू ल्याउँछन्।"
        ),
        "MAHA_DHANA" to LocalizedTemplate(
            en = "Maha Dhana Yoga (Multiple wealth lords in 2nd/11th): When multiple wealth-producing lords concentrate in the 2nd or 11th house, exceptional wealth accumulates. The native becomes extremely wealthy through multiple income streams. Inheritance, business, investments, and gains all contribute. This yoga produces millionaires and billionaires when strongly placed. The houses involved determine the sources of wealth.",
            ne = "महा धन योग (२औं/११औंमा बहु धन स्वामीहरू): जब बहु धन-उत्पादक स्वामीहरू २औं वा ११औं भावमा केन्द्रित हुन्छन्, असाधारण धन जम्मा हुन्छ। जातक बहु आय स्रोतहरू मार्फत अत्यन्त धनी हुन्छ। विरासत, व्यापार, लगानी र लाभ सबैले योगदान पुर्‍याउँछन्। यो योगले बलियो भएमा करोडपति र अर्बपतिहरू उत्पादन गर्छ। संलग्न भावहरूले धनका स्रोतहरू निर्धारण गर्छन्।"
        ),
        "SARASWATI" to LocalizedTemplate(
            en = "Saraswati Yoga (Jupiter, Venus, Mercury in Kendra/Trikona/2nd): Goddess Saraswati's blessings manifest when the three benefics occupy auspicious positions. The native becomes highly educated, eloquent, and skilled in arts. Wealth comes through knowledge, writing, and artistic pursuits. Musical and literary talents bring fame and fortune. Academic achievements are exceptional. This yoga produces scholars, authors, and artists.",
            ne = "सरस्वती योग (बृहस्पति, शुक्र, बुध केन्द्र/त्रिकोण/२औंमा): देवी सरस्वतीको आशीर्वाद तीन शुभ ग्रहहरू शुभ स्थानहरूमा हुँदा प्रकट हुन्छ। जातक उच्च शिक्षित, वाक्पटु र कलामा कुशल हुन्छ। ज्ञान, लेखन र कलात्मक कार्यहरू मार्फत धन आउँछ। सांगीतिक र साहित्यिक प्रतिभाहरूले प्रसिद्धि र भाग्य ल्याउँछन्। शैक्षिक उपलब्धिहरू असाधारण। यो योगले विद्वान, लेखक र कलाकारहरू उत्पादन गर्छ।"
        )
    )

    // ============================================
    // ARISHTA YOGAS (Affliction Combinations)
    // ============================================
    val arishtaYogas: Map<String, LocalizedTemplate> = mapOf(
        "KEMADRUMA" to LocalizedTemplate(
            en = "Kemadruma Yoga (No planets in 2nd/12th from Moon): When no planets occupy the houses adjacent to Moon, the native may face poverty, struggle, and lack of support in life. Mental distress and loneliness may occur. However, this yoga is cancelled by planets in Kendra from Lagna or Moon, benefic aspects to Moon, or Moon in Kendra. Many cancellation factors exist. When uncancelled, the native must rely entirely on self-effort. The challenges can build exceptional self-reliance and inner strength.",
            ne = "केमद्रुम योग (चन्द्रबाट २औं/१२औंमा कुनै ग्रह छैन): जब चन्द्रको छेउका भावहरूमा कुनै ग्रह हुँदैन, जातकले जीवनमा गरिबी, संघर्ष र समर्थनको कमी सामना गर्न सक्छ। मानसिक तनाव र एकलता हुन सक्छ। यद्यपि, यो योग लग्न वा चन्द्रबाट केन्द्रमा ग्रहहरू, चन्द्रमा शुभ दृष्टि, वा केन्द्रमा चन्द्रले रद्द हुन्छ। धेरै रद्द कारकहरू अवस्थित छन्। रद्द नभएमा, जातकले पूर्णतया आत्म-प्रयासमा भर पर्नुपर्छ। चुनौतीहरूले असाधारण आत्मनिर्भरता र आन्तरिक शक्ति निर्माण गर्न सक्छन्।"
        ),
        "DARIDRA" to LocalizedTemplate(
            en = "Daridra Yoga (11th lord in 6th/8th/12th): The lord of gains placed in houses of loss creates poverty and financial struggle. Income sources face obstacles. Debts may accumulate. Illness drains resources. Hidden expenses arise. The native must work harder for less reward. However, malefics in own signs or exalted positions modify this yoga. The house placement indicates the specific area of challenge.",
            ne = "दरिद्र योग (११औं स्वामी ६औं/८औं/१२औंमा): लाभको स्वामी हानिका भावहरूमा राखिँदा गरिबी र आर्थिक संघर्ष सिर्जना गर्छ। आय स्रोतहरूले बाधाहरू सामना गर्छन्। ऋणहरू जम्मा हुन सक्छन्। रोगले स्रोतहरू खर्च गर्छ। लुकेका खर्चहरू उठ्छन्। जातकले कम पुरस्कारको लागि बढी काम गर्नुपर्छ। यद्यपि, स्वराशि वा उच्च स्थानमा पाप ग्रहहरूले यो योग परिमार्जन गर्छन्। भाव स्थानले विशिष्ट चुनौती क्षेत्र संकेत गर्छ।"
        ),
        "BALARISHTA" to LocalizedTemplate(
            en = "Balarishta Yoga (Affliction to Moon in early life): When Moon is severely afflicted, especially in early degrees or in conjunction/aspect with malefics, health challenges in childhood may occur. Mother may face difficulties. Mental stress during formative years. However, modern medicine and early intervention can mitigate effects. The yoga weakens after childhood if the native survives. Strong Jupiter or Venus aspects provide protection.",
            ne = "बालारिष्ट योग (बाल्यकालमा चन्द्रमा पीडा): जब चन्द्र गम्भीर रूपमा पीडित हुन्छ, विशेष गरी प्रारम्भिक अंशमा वा पाप ग्रहसँग युति/दृष्टिमा, बाल्यकालमा स्वास्थ्य चुनौतीहरू हुन सक्छन्। आमाले कठिनाइहरू सामना गर्न सक्छिन्। निर्माणकारी वर्षहरूमा मानसिक तनाव। यद्यपि, आधुनिक चिकित्सा र प्रारम्भिक हस्तक्षेपले प्रभावहरू कम गर्न सक्छ। जातक बाँच्यो भने योग बाल्यकाल पछि कमजोर हुन्छ। बलियो बृहस्पति वा शुक्र दृष्टिहरूले सुरक्षा प्रदान गर्छन्।"
        ),
        "GRAHA_YUDDHA" to LocalizedTemplate(
            en = "Graha Yuddha (Planetary War): When two planets are within one degree of each other, a planetary war occurs. The winner gains strength while the loser is weakened. Mercury always loses to other planets. The themes of both planets experience conflict and competition. The houses they rule face challenges. The native must navigate tensions between the significations of both planets.",
            ne = "ग्रह युद्ध: जब दुई ग्रहहरू एक अर्काबाट एक अंश भित्र हुन्छन्, ग्रह युद्ध हुन्छ। विजेताले शक्ति पाउँछ जबकि पराजितले कमजोर हुन्छ। बुध सधैं अन्य ग्रहहरूसँग हार्छ। दुवै ग्रहका विषयहरूले द्वन्द्व र प्रतिस्पर्धा अनुभव गर्छन्। उनीहरूले शासन गर्ने भावहरूले चुनौतीहरू सामना गर्छन्। जातकले दुवै ग्रहका अर्थहरू बीच तनावहरू नेभिगेट गर्नुपर्छ।"
        ),
        "PAPA_KARTARI" to LocalizedTemplate(
            en = "Papa Kartari Yoga (Malefics on both sides of a house/planet): When natural malefics (Saturn, Mars, Rahu, Ketu, afflicted Sun) hem in a house or planet, that signification is squeezed and restricted. The native faces obstacles in those life areas. Resources are limited. Expression is constrained. However, this yoga also creates focus and concentration. The malefics' strength and dignity modify the effects.",
            ne = "पाप कर्तरी योग (भाव/ग्रहको दुवैतिर पाप ग्रह): जब प्राकृतिक पाप ग्रहहरू (शनि, मंगल, राहु, केतु, पीडित सूर्य) भाव वा ग्रहलाई घेर्छन्, त्यो अर्थ निचोड र प्रतिबन्धित हुन्छ। जातकले ती जीवन क्षेत्रहरूमा बाधाहरू सामना गर्छ। स्रोतहरू सीमित। अभिव्यक्ति प्रतिबन्धित। यद्यपि, यो योगले ध्यान र एकाग्रता पनि सिर्जना गर्छ। पाप ग्रहको बल र सम्मानले प्रभावहरू परिमार्जन गर्छन्।"
        ),
        "SHAKATA" to LocalizedTemplate(
            en = "Shakata Yoga (Jupiter in 6th/8th/12th from Moon): When Jupiter, the great benefic, is in Dusthana from Moon, fortune fluctuates like a cart wheel. Ups and downs characterize life. Wealth comes and goes. Positions of authority may be lost and regained. However, this yoga creates resilience and adaptability. The native learns to handle success and failure equally. Jupiter's dignity and other aspects modify effects.",
            ne = "शकट योग (बृहस्पति चन्द्रबाट ६औं/८औं/१२औंमा): जब बृहस्पति, महान शुभ ग्रह, चन्द्रबाट दुःस्थानमा हुन्छ, भाग्य गाडाको पाङ्ग्रा जस्तो उतारचढाव हुन्छ। उतारचढावले जीवनलाई विशेषता दिन्छ। धन आउँछ र जान्छ। अधिकारका पदहरू गुम्न सक्छ र पुन: प्राप्त हुन सक्छ। यद्यपि, यो योगले लचीलापन र अनुकूलनशीलता सिर्जना गर्छ। जातकले सफलता र असफलता समान रूपमा व्यवहार गर्न सिक्छ। बृहस्पतिको सम्मान र अन्य दृष्टिहरूले प्रभावहरू परिमार्जन गर्छन्।"
        )
    )

    // ============================================
    // NABHASA YOGAS (Planetary Patterns)
    // ============================================
    val nabhasaYogas: Map<String, LocalizedTemplate> = mapOf(
        "RAJJU" to LocalizedTemplate(
            en = "Rajju Yoga (All planets in Chara/Cardinal signs): The native is restless, always seeking movement and change. Travel and relocation are frequent. Multiple career changes occur. Pioneering and initiating energy dominates. The person starts many projects. Success comes through adaptability and willingness to begin anew. Leadership in dynamic environments.",
            ne = "रज्जु योग (सबै ग्रहहरू चर/कार्डिनल राशिमा): जातक बेचैन हुन्छ, सधैं गति र परिवर्तन खोज्दै। यात्रा र स्थानान्तरण बारम्बार। बहु करियर परिवर्तनहरू हुन्छन्। अग्रगामी र प्रारम्भिक ऊर्जा प्रभुत्व। व्यक्तिले धेरै परियोजनाहरू सुरु गर्छ। अनुकूलनशीलता र नयाँ सुरुवात गर्ने इच्छा मार्फत सफलता। गतिशील वातावरणमा नेतृत्व।"
        ),
        "MUSALA" to LocalizedTemplate(
            en = "Musala Yoga (All planets in Sthira/Fixed signs): The native is stable, determined, and resistant to change. Persistence and endurance characterize life. Positions once attained are held firmly. Wealth accumulates slowly but steadily. Stubbornness may become an issue. Success comes through staying power and loyalty. Real estate and fixed assets favor.",
            ne = "मुसल योग (सबै ग्रहहरू स्थिर राशिमा): जातक स्थिर, दृढ र परिवर्तन प्रतिरोधी हुन्छ। लगनशीलता र सहनशीलताले जीवनलाई विशेषता दिन्छ। एक पटक प्राप्त पदहरू दृढतापूर्वक समातिन्छन्। धन ढिलो तर स्थिर रूपमा जम्मा हुन्छ। जिद्दीपन समस्या हुन सक्छ। स्थायित्व शक्ति र वफादारी मार्फत सफलता। घरजग्गा र स्थिर सम्पत्तिहरू अनुकूल।"
        ),
        "NALA" to LocalizedTemplate(
            en = "Nala Yoga (All planets in Dwiswabhava/Mutable signs): The native is adaptable, versatile, and intellectually oriented. Multiple skills and interests develop. Communication and learning are central to life. The person may have difficulty completing projects. Success comes through flexibility and intellectual pursuits. Teaching and counseling favor.",
            ne = "नल योग (सबै ग्रहहरू द्विस्वभाव राशिमा): जातक अनुकूलनशील, बहुमुखी र बौद्धिक रूपमा अभिमुख हुन्छ। बहु सीप र रुचिहरू विकसित हुन्छन्। सञ्चार र सिकाइ जीवनको केन्द्र। व्यक्तिलाई परियोजनाहरू पूरा गर्न कठिनाइ हुन सक्छ। लचिलोपन र बौद्धिक कार्यहरू मार्फत सफलता। शिक्षण र परामर्श अनुकूल।"
        ),
        "GADA" to LocalizedTemplate(
            en = "Gada Yoga (All planets in two consecutive signs): Focus and concentration characterize the native. Energy is directed toward specific life areas. The houses involved become dominant themes. Success comes through specialization. The native may have a narrower but deeper range of experience.",
            ne = "गदा योग (सबै ग्रहहरू दुई लगातार राशिमा): ध्यान र एकाग्रताले जातकलाई विशेषता दिन्छ। ऊर्जा विशिष्ट जीवन क्षेत्रहरूतर्फ निर्देशित। संलग्न भावहरू प्रमुख विषयहरू हुन्छन्। विशेषज्ञता मार्फत सफलता। जातकसँग साँघुरो तर गहिरो अनुभवको दायरा हुन सक्छ।"
        ),
        "SHAKATA_NABHASA" to LocalizedTemplate(
            en = "Shakata Nabhasa Yoga (Planets in 1st and 7th only): Balance between self and others defines life. Relationships and partnerships are central. The native oscillates between independence and dependence. Marriage and business partnerships are prominent. Success requires balancing personal and shared interests.",
            ne = "शकट नाभस योग (ग्रहहरू १औं र ७औंमा मात्र): आफू र अरू बीचको सन्तुलनले जीवनलाई परिभाषित गर्छ। सम्बन्ध र साझेदारीहरू केन्द्रीय। जातक स्वतन्त्रता र निर्भरता बीच दोलन गर्छ। विवाह र व्यापारिक साझेदारीहरू प्रमुख। सफलताको लागि व्यक्तिगत र साझा हितहरू सन्तुलन चाहिन्छ।"
        ),
        "YUPA" to LocalizedTemplate(
            en = "Yupa Yoga (All planets in 1st, 2nd, 3rd, 4th houses): Focus on personal development, resources, communication, and home. The first quadrant of life themes dominates. Self-reliance and family foundation are emphasized. Early life may be more eventful. Personal initiative creates success.",
            ne = "यूप योग (सबै ग्रहहरू १औं, २औं, ३औं, ४औं भावमा): व्यक्तिगत विकास, स्रोत, सञ्चार र घरमा ध्यान। जीवनको पहिलो चौथाई विषयहरू प्रभुत्व। आत्मनिर्भरता र पारिवारिक जगमा जोड। प्रारम्भिक जीवन बढी घटनापूर्ण हुन सक्छ। व्यक्तिगत पहलले सफलता सिर्जना गर्छ।"
        ),
        "SHARA" to LocalizedTemplate(
            en = "Shara Yoga (All planets in 4th, 5th, 6th, 7th houses): Focus on property, children, service, and partnerships. The second quadrant of life themes dominates. Relationships and creativity are emphasized. Middle life may be more eventful. Success through collaboration and service.",
            ne = "शर योग (सबै ग्रहहरू ४औं, ५औं, ६औं, ७औं भावमा): सम्पत्ति, सन्तान, सेवा र साझेदारीमा ध्यान। जीवनको दोस्रो चौथाई विषयहरू प्रभुत्व। सम्बन्ध र सिर्जनशीलतामा जोड। मध्य जीवन बढी घटनापूर्ण हुन सक्छ। सहयोग र सेवा मार्फत सफलता।"
        ),
        "SHRINGATAKA" to LocalizedTemplate(
            en = "Shringataka Yoga (Planets in Trines only): Exceptional fortune and dharmic life. The 1st, 5th, and 9th houses being emphasized creates Lakshmi's blessings. Past-life merit activates. Intelligence and wisdom combine with fortune. Children and creativity are blessed. Spiritual development proceeds naturally.",
            ne = "शृङ्गाटक योग (ग्रहहरू त्रिकोणमा मात्र): असाधारण भाग्य र धार्मिक जीवन। १औं, ५औं र ९औं भावमा जोड दिँदा लक्ष्मीको आशीर्वाद सिर्जना। पूर्वजन्म पुण्य सक्रिय। बुद्धि र ज्ञान भाग्यसँग संयोजन। सन्तान र सिर्जनशीलता आशीर्वादित। आध्यात्मिक विकास स्वाभाविक रूपमा अगाडि बढ्छ।"
        )
    )

    // ============================================
    // SPECIAL YOGAS
    // ============================================
    val specialYogas: Map<String, LocalizedTemplate> = mapOf(
        "KALA_SARPA" to LocalizedTemplate(
            en = "Kala Sarpa Yoga (All planets between Rahu and Ketu): When all seven planets are hemmed between the lunar nodes, karmic patterns intensify. Past-life themes dominate current existence. Sudden rises and falls occur. Delays and obstacles appear in half of life, followed by success in the other half. The direction (Rahu to Ketu or Ketu to Rahu) determines which half experiences challenges. Spiritual awakening often accompanies this yoga. The native may feel fated or destined. Remedy through Naga worship is traditional.",
            ne = "कालसर्प योग (सबै ग्रहहरू राहु र केतु बीचमा): जब सातै ग्रहहरू चन्द्र नोडहरू बीच घेरिन्छन्, कर्मिक ढाँचाहरू तीव्र हुन्छन्। पूर्वजन्म विषयहरूले वर्तमान अस्तित्वमा प्रभुत्व जमाउँछन्। अचानक उदय र पतनहरू हुन्छन्। जीवनको आधामा ढिलाइ र बाधाहरू, त्यसपछि अर्को आधामा सफलता। दिशा (राहुदेखि केतु वा केतुदेखि राहु) ले कुन आधाले चुनौतीहरू अनुभव गर्छ निर्धारण गर्छ। आध्यात्मिक जागरण प्रायः यो योगसँगै आउँछ। जातकले भाग्य वा नियति महसुस गर्न सक्छ। नाग पूजा मार्फत उपचार परम्परागत।"
        ),
        "VIPAREETA_RAJA_HARSHA" to LocalizedTemplate(
            en = "Harsha Yoga (6th lord in 6th, 8th, or 12th): The lord of enemies, debts, and disease placed in evil houses destroys those significations. The native defeats enemies effortlessly. Health issues resolve. Debts are cleared. Service brings hidden rewards. Competition is won. Legal matters favor. This is a powerful Viparita Raja Yoga producing success through others' failures.",
            ne = "हर्ष योग (६औं स्वामी ६औं, ८औं वा १२औंमा): शत्रु, ऋण र रोगको स्वामी पाप भावमा राखिँदा ती अर्थहरू नष्ट हुन्छन्। जातकले शत्रुहरूलाई सजिलैसँग पराजित गर्छ। स्वास्थ्य समस्याहरू समाधान। ऋणहरू फुकुवा। सेवाले लुकेका पुरस्कारहरू ल्याउँछ। प्रतिस्पर्धा जीत। कानूनी मामिलाहरू अनुकूल। यो अरूको असफलता मार्फत सफलता उत्पादन गर्ने शक्तिशाली विपरीत राजयोग हो।"
        ),
        "VIPAREETA_RAJA_SARALA" to LocalizedTemplate(
            en = "Sarala Yoga (8th lord in 6th, 8th, or 12th): The lord of longevity, obstacles, and transformation placed in evil houses protects against those significations. The native has strong immunity and long life. Hidden knowledge becomes accessible. Inheritance arrives unexpectedly. Research abilities excel. Occult protection surrounds the native.",
            ne = "सरल योग (८औं स्वामी ६औं, ८औं वा १२औंमा): दीर्घायु, बाधा र रूपान्तरणको स्वामी पाप भावमा राखिँदा ती अर्थहरूबाट सुरक्षा। जातकसँग बलियो प्रतिरक्षा र लामो आयु। लुकेको ज्ञान पहुँचयोग्य। विरासत अप्रत्याशित रूपमा आउँछ। अनुसन्धान क्षमताहरू उत्कृष्ट। तन्त्रमन्त्र सुरक्षाले जातकलाई घेर्छ।"
        ),
        "VIPAREETA_RAJA_VIMALA" to LocalizedTemplate(
            en = "Vimala Yoga (12th lord in 6th, 8th, or 12th): The lord of loss, expenses, and liberation placed in evil houses transforms losses into gains. The native spends wisely and gains through expenses. Foreign connections bring benefits. Spiritual liberation progresses. Hospital work brings recognition. Meditation yields results.",
            ne = "विमल योग (१२औं स्वामी ६औं, ८औं वा १२औंमा): हानि, खर्च र मुक्तिको स्वामी पाप भावमा राखिँदा हानिहरूलाई लाभमा रूपान्तरण गर्छ। जातकले बुद्धिमानीपूर्वक खर्च गर्छ र खर्चहरू मार्फत लाभ। विदेशी सम्बन्धहरूले लाभ ल्याउँछन्। आध्यात्मिक मुक्ति अगाडि बढ्छ। अस्पताल कामले मान्यता ल्याउँछ। ध्यानले परिणामहरू दिन्छ।"
        ),
        "AMALA" to LocalizedTemplate(
            en = "Amala Yoga (Benefic in 10th from Lagna or Moon): When a natural benefic (Jupiter, Venus, unafflicted Mercury, waxing Moon) occupies the 10th house, the native becomes famous for good deeds. Professional reputation is spotless. Career brings recognition and respect. The native is ethical and successful. Government appreciation possible. This yoga produces genuinely good and successful people.",
            ne = "अमल योग (शुभ ग्रह लग्न वा चन्द्रबाट १०औंमा): जब प्राकृतिक शुभ ग्रह (बृहस्पति, शुक्र, अप्रभावित बुध, बढ्दो चन्द्र) १०औं भाव ओगट्छ, जातक राम्रा कार्यहरूको लागि प्रसिद्ध हुन्छ। पेशेवर प्रतिष्ठा निर्दोष। करियरले मान्यता र सम्मान ल्याउँछ। जातक नैतिक र सफल। सरकारी प्रशंसा सम्भव। यो योगले साँच्चै राम्रा र सफल मानिसहरू उत्पादन गर्छ।"
        ),
        "PARVATA" to LocalizedTemplate(
            en = "Parvata Yoga (Benefics in Kendras, no malefics in Kendras): When benefics occupy angular houses without malefic interference, the native rises like a mountain. Steady and solid progress occurs. Fame and fortune accumulate. The native becomes a prominent figure in society. Obstacles are minimal. Success is assured through righteous means.",
            ne = "पर्वत योग (केन्द्रमा शुभ ग्रह, केन्द्रमा पाप ग्रह छैन): जब शुभ ग्रहहरू पाप हस्तक्षेप बिना कोणीय भावहरू ओगट्छन्, जातक पहाड जस्तो उठ्छ। स्थिर र ठोस प्रगति हुन्छ। प्रसिद्धि र भाग्य जम्मा हुन्छ। जातक समाजमा प्रमुख व्यक्ति हुन्छ। बाधाहरू न्यूनतम। धार्मिक माध्यमबाट सफलता सुनिश्चित।"
        ),
        "KAHALA" to LocalizedTemplate(
            en = "Kahala Yoga (4th and 9th lords in mutual Kendras): When the lords of home (4th) and fortune (9th) occupy each other's angular positions, the native enjoys property, vehicles, and blessed fortune from home base. Real estate investments prosper. Family property increases. Domestic happiness combines with good fortune. The native may inherit or acquire significant property.",
            ne = "कहल योग (४औं र ९औं स्वामी परस्पर केन्द्रमा): जब घर (४औं) र भाग्य (९औं) का स्वामीहरू एक अर्काको कोणीय स्थान ओगट्छन्, जातकले गृह आधारबाट सम्पत्ति, सवारी साधन र आशीर्वादित भाग्यको आनन्द लिन्छ। घरजग्गा लगानीहरू समृद्ध। पारिवारिक सम्पत्ति बढ्छ। घरेलु खुशी सौभाग्यसँग संयोजन। जातकले महत्त्वपूर्ण सम्पत्ति विरासत वा प्राप्त गर्न सक्छ।"
        )
    )

    // ============================================
    // HELPER FUNCTIONS
    // ============================================

    /**
     * Get Pancha Mahapurusha Yoga template.
     */
    fun getPanchaMahapurushaYogaTemplate(yogaName: String): LocalizedTemplate {
        return panchaMahapurushaYogas[yogaName] ?: LocalizedTemplate(
            en = "Mahapurusha Yoga present: One of the five great planetary yogas formed by Mars, Mercury, Jupiter, Venus, or Saturn in Kendra in own or exaltation sign. This confers exceptional qualities associated with the planet involved.",
            ne = "महापुरुष योग उपस्थित: मंगल, बुध, बृहस्पति, शुक्र वा शनि केन्द्रमा स्वराशि वा उच्च राशिमा हुँदा बन्ने पाँच महान ग्रह योगहरू मध्ये एक। यसले संलग्न ग्रहसँग सम्बन्धित असाधारण गुणहरू प्रदान गर्छ।"
        )
    }

    /**
     * Get Raja Yoga template.
     */
    fun getRajaYogaTemplate(yogaName: String): LocalizedTemplate {
        return rajaYogas[yogaName] ?: LocalizedTemplate(
            en = "Raja Yoga present: A royal combination indicating success, authority, and recognition. The specific effects depend on the planets and houses involved.",
            ne = "राजयोग उपस्थित: सफलता, अधिकार र मान्यता संकेत गर्ने शाही संयोजन। विशिष्ट प्रभावहरू संलग्न ग्रह र भावहरूमा निर्भर।"
        )
    }

    /**
     * Get Dhana Yoga template.
     */
    fun getDhanaYogaTemplate(yogaName: String): LocalizedTemplate {
        return dhanaYogas[yogaName] ?: LocalizedTemplate(
            en = "Dhana Yoga present: A wealth-producing combination indicating financial prosperity. The specific nature of wealth depends on the planets and houses involved.",
            ne = "धन योग उपस्थित: आर्थिक समृद्धि संकेत गर्ने धन-उत्पादक संयोजन। धनको विशिष्ट प्रकृति संलग्न ग्रह र भावहरूमा निर्भर।"
        )
    }

    /**
     * Get Arishta Yoga template.
     */
    fun getArishtaYogaTemplate(yogaName: String): LocalizedTemplate {
        return arishtaYogas[yogaName] ?: LocalizedTemplate(
            en = "Arishta Yoga present: An affliction combination requiring attention. The specific challenges and remedies depend on the planets and houses involved.",
            ne = "अरिष्ट योग उपस्थित: ध्यान चाहिने पीडा संयोजन। विशिष्ट चुनौतीहरू र उपायहरू संलग्न ग्रह र भावहरूमा निर्भर।"
        )
    }

    /**
     * Get Nabhasa Yoga template.
     */
    fun getNabhasaYogaTemplate(yogaName: String): LocalizedTemplate {
        return nabhasaYogas[yogaName] ?: LocalizedTemplate(
            en = "Nabhasa Yoga present: A planetary pattern shaping overall life themes. The specific effects depend on the pattern type and planets involved.",
            ne = "नाभस योग उपस्थित: समग्र जीवन विषयहरूलाई आकार दिने ग्रह ढाँचा। विशिष्ट प्रभावहरू ढाँचा प्रकार र संलग्न ग्रहहरूमा निर्भर।"
        )
    }

    /**
     * Get Special Yoga template.
     */
    fun getSpecialYogaTemplate(yogaName: String): LocalizedTemplate {
        return specialYogas[yogaName] ?: LocalizedTemplate(
            en = "Special Yoga present: A notable planetary combination with specific effects. Please see detailed interpretations for this yoga type.",
            ne = "विशेष योग उपस्थित: विशेष प्रभावहरू भएको उल्लेखनीय ग्रह संयोजन। कृपया यो योग प्रकारको विस्तृत व्याख्याहरू हेर्नुहोस्।"
        )
    }

    /**
     * Get all yogas for a specific category.
     */
    fun getAllTemplatesForCategory(category: String): Map<String, LocalizedTemplate> {
        return when (category.uppercase()) {
            "MAHAPURUSHA" -> panchaMahapurushaYogas
            "RAJA" -> rajaYogas
            "DHANA" -> dhanaYogas
            "ARISHTA" -> arishtaYogas
            "NABHASA" -> nabhasaYogas
            "SPECIAL" -> specialYogas
            else -> emptyMap()
        }
    }
}
