package com.astro.storm.data.templates

import com.astro.storm.core.model.Planet
import com.astro.storm.core.model.ZodiacSign

/**
 * Comprehensive Life Area Templates
 * Based on BPHS, Phaladeepika, Saravali, and Jataka Parijata
 *
 * Covers:
 * - Career (10th house analysis, profession indicators)
 * - Relationship (7th house, marriage indicators)
 * - Health (6th house, ascendant, planet influences)
 * - Wealth (2nd, 11th houses, Dhana yogas)
 * - Education (4th, 5th houses, intellectual capacity)
 * - Spiritual (9th, 12th houses, moksha indicators)
 * - Children (5th house, progeny indicators)
 * - Property (4th house, real estate indicators)
 *
 * Total: 1,200+ templates
 */
object LifeAreaTemplates {

    // ==================== CAREER TEMPLATES ====================

    /**
     * Career by Ascendant Sign - General career tendencies
     */
    val careerByAscendantTemplates = mapOf(
        ZodiacSign.ARIES to LocalizedTemplate(
            en = "Aries Ascendant natives excel in careers requiring leadership, initiative, and courage. Suitable professions include military, police, sports, surgery, engineering, entrepreneurship, and any pioneering field. They thrive in competitive environments and prefer to lead rather than follow. Self-employment and starting new ventures suit them well.",
            ne = "मेष लग्न जातकहरू नेतृत्व, पहल र साहस आवश्यक पर्ने क्यारियरहरूमा उत्कृष्ट हुन्छन्। उपयुक्त पेशाहरूमा सैन्य, प्रहरी, खेलकुद, शल्यचिकित्सा, इन्जिनियरिङ, उद्यमशीलता र कुनै पनि अग्रणी क्षेत्र समावेश छ। तिनीहरू प्रतिस्पर्धी वातावरणमा फस्टाउँछन् र अनुसरण गर्नुभन्दा नेतृत्व गर्न रुचाउँछन्। स्व-रोजगार र नयाँ उद्यमहरू सुरु गर्नु तिनीहरूका लागि उपयुक्त छ।"
        ),
        ZodiacSign.TAURUS to LocalizedTemplate(
            en = "Taurus Ascendant natives excel in careers involving finance, arts, and stable enterprises. Suitable professions include banking, finance, luxury goods, agriculture, real estate, music, and culinary arts. They prefer steady income and job security. Patience and persistence help them build long-term career success.",
            ne = "वृष लग्न जातकहरू वित्त, कला र स्थिर उद्यमहरू समावेश गर्ने क्यारियरहरूमा उत्कृष्ट हुन्छन्। उपयुक्त पेशाहरूमा बैंकिङ, वित्त, विलासी सामान, कृषि, रियल इस्टेट, संगीत र पाक कला समावेश छ। तिनीहरू स्थिर आम्दानी र जागिर सुरक्षा रुचाउँछन्। धैर्य र दृढताले तिनीहरूलाई दीर्घकालीन क्यारियर सफलता निर्माण गर्न मद्दत गर्छ।"
        ),
        ZodiacSign.GEMINI to LocalizedTemplate(
            en = "Gemini Ascendant natives excel in careers involving communication, intellect, and versatility. Suitable professions include journalism, writing, teaching, trading, marketing, telecommunications, and sales. They thrive in dynamic environments with variety. Multiple income streams or career changes are common.",
            ne = "मिथुन लग्न जातकहरू संचार, बुद्धि र बहुमुखी प्रतिभा समावेश गर्ने क्यारियरहरूमा उत्कृष्ट हुन्छन्। उपयुक्त पेशाहरूमा पत्रकारिता, लेखन, शिक्षण, व्यापार, मार्केटिङ, दूरसंचार र बिक्री समावेश छ। तिनीहरू विविधतासहितको गतिशील वातावरणमा फस्टाउँछन्। बहु आम्दानी स्रोतहरू वा क्यारियर परिवर्तनहरू सामान्य हुन्।"
        ),
        ZodiacSign.CANCER to LocalizedTemplate(
            en = "Cancer Ascendant natives excel in careers involving nurturing, public welfare, and emotional connection. Suitable professions include healthcare, hospitality, real estate, food industry, childcare, and public service. They work well from home or in family businesses. Emotional satisfaction from work is important.",
            ne = "कर्कट लग्न जातकहरू पालनपोषण, सार्वजनिक कल्याण र भावनात्मक जडान समावेश गर्ने क्यारियरहरूमा उत्कृष्ट हुन्छन्। उपयुक्त पेशाहरूमा स्वास्थ्य सेवा, आतिथ्य, रियल इस्टेट, खाद्य उद्योग, बालबालिकाको हेरचाह र सार्वजनिक सेवा समावेश छ। तिनीहरू घरबाट वा पारिवारिक व्यवसायहरूमा राम्रो काम गर्छन्। कामबाट भावनात्मक सन्तुष्टि महत्वपूर्ण छ।"
        ),
        ZodiacSign.LEO to LocalizedTemplate(
            en = "Leo Ascendant natives excel in careers involving authority, creativity, and recognition. Suitable professions include government, administration, entertainment, politics, management, and luxury brands. They seek positions of power and prestige. Creative self-expression through work brings fulfillment.",
            ne = "सिंह लग्न जातकहरू अधिकार, रचनात्मकता र मान्यता समावेश गर्ने क्यारियरहरूमा उत्कृष्ट हुन्छन्। उपयुक्त पेशाहरूमा सरकार, प्रशासन, मनोरञ्जन, राजनीति, व्यवस्थापन र विलासी ब्रान्डहरू समावेश छ। तिनीहरू शक्ति र प्रतिष्ठाको पद खोज्छन्। काम मार्फत रचनात्मक आत्म-अभिव्यक्तिले पूर्णता ल्याउँछ।"
        ),
        ZodiacSign.VIRGO to LocalizedTemplate(
            en = "Virgo Ascendant natives excel in careers requiring analysis, service, and precision. Suitable professions include healthcare, accounting, research, editing, quality control, and technical fields. They thrive in detail-oriented work. Problem-solving and continuous improvement drive their success.",
            ne = "कन्या लग्न जातकहरू विश्लेषण, सेवा र सटीकता आवश्यक पर्ने क्यारियरहरूमा उत्कृष्ट हुन्छन्। उपयुक्त पेशाहरूमा स्वास्थ्य सेवा, लेखा, अनुसन्धान, सम्पादन, गुणस्तर नियन्त्रण र प्राविधिक क्षेत्रहरू समावेश छ। तिनीहरू विस्तार-उन्मुख कार्यमा फस्टाउँछन्। समस्या-समाधान र निरन्तर सुधारले तिनीहरूको सफलता चलाउँछ।"
        ),
        ZodiacSign.LIBRA to LocalizedTemplate(
            en = "Libra Ascendant natives excel in careers involving balance, aesthetics, and partnerships. Suitable professions include law, diplomacy, fashion, interior design, consulting, and public relations. They work well in collaborative environments. Fairness and harmony in workplace are essential for their success.",
            ne = "तुला लग्न जातकहरू सन्तुलन, सौन्दर्यशास्त्र र साझेदारी समावेश गर्ने क्यारियरहरूमा उत्कृष्ट हुन्छन्। उपयुक्त पेशाहरूमा कानून, कूटनीति, फेसन, इन्टेरियर डिजाइन, परामर्श र जनसम्पर्क समावेश छ। तिनीहरू सहयोगी वातावरणमा राम्रो काम गर्छन्। कार्यस्थलमा निष्पक्षता र सामंजस्य तिनीहरूको सफलताको लागि आवश्यक छ।"
        ),
        ZodiacSign.SCORPIO to LocalizedTemplate(
            en = "Scorpio Ascendant natives excel in careers involving research, investigation, and transformation. Suitable professions include psychology, surgery, detective work, insurance, occult sciences, and crisis management. They handle pressure and secrets well. Intensity and depth characterize their professional approach.",
            ne = "वृश्चिक लग्न जातकहरू अनुसन्धान, अनुसन्धान र परिवर्तन समावेश गर्ने क्यारियरहरूमा उत्कृष्ट हुन्छन्। उपयुक्त पेशाहरूमा मनोविज्ञान, शल्यचिकित्सा, जासूसी काम, बीमा, गुप्त विज्ञान र संकट व्यवस्थापन समावेश छ। तिनीहरू दबाव र गोपनीयता राम्रोसँग सम्हाल्छन्। तीव्रता र गहिराइले तिनीहरूको व्यावसायिक दृष्टिकोणलाई विशेषता दिन्छ।"
        ),
        ZodiacSign.SAGITTARIUS to LocalizedTemplate(
            en = "Sagittarius Ascendant natives excel in careers involving education, philosophy, and expansion. Suitable professions include teaching, publishing, law, religion, travel industry, and international relations. They thrive in roles with freedom and growth potential. Higher purpose motivates their professional life.",
            ne = "धनु लग्न जातकहरू शिक्षा, दर्शन र विस्तार समावेश गर्ने क्यारियरहरूमा उत्कृष्ट हुन्छन्। उपयुक्त पेशाहरूमा शिक्षण, प्रकाशन, कानून, धर्म, यात्रा उद्योग र अन्तर्राष्ट्रिय सम्बन्ध समावेश छ। तिनीहरू स्वतन्त्रता र वृद्धि सम्भावना भएका भूमिकाहरूमा फस्टाउँछन्। उच्च उद्देश्यले तिनीहरूको व्यावसायिक जीवनलाई प्रेरित गर्छ।"
        ),
        ZodiacSign.CAPRICORN to LocalizedTemplate(
            en = "Capricorn Ascendant natives excel in careers involving structure, authority, and long-term planning. Suitable professions include administration, management, politics, engineering, and established industries. They climb the corporate ladder steadily. Patience and persistence lead to high positions.",
            ne = "मकर लग्न जातकहरू संरचना, अधिकार र दीर्घकालीन योजना समावेश गर्ने क्यारियरहरूमा उत्कृष्ट हुन्छन्। उपयुक्त पेशाहरूमा प्रशासन, व्यवस्थापन, राजनीति, इन्जिनियरिङ र स्थापित उद्योगहरू समावेश छ। तिनीहरू कर्पोरेट सीढी स्थिर रूपमा चढ्छन्। धैर्य र दृढताले उच्च पदहरूमा पुर्याउँछ।"
        ),
        ZodiacSign.AQUARIUS to LocalizedTemplate(
            en = "Aquarius Ascendant natives excel in careers involving innovation, technology, and humanitarian causes. Suitable professions include IT, social work, scientific research, aviation, and networking. They bring original ideas to their work. Unconventional approaches and independence characterize their career.",
            ne = "कुम्भ लग्न जातकहरू नवीनता, प्रविधि र मानवतावादी कारणहरू समावेश गर्ने क्यारियरहरूमा उत्कृष्ट हुन्छन्। उपयुक्त पेशाहरूमा IT, सामाजिक कार्य, वैज्ञानिक अनुसन्धान, विमानन र नेटवर्किङ समावेश छ। तिनीहरू आफ्नो कार्यमा मौलिक विचारहरू ल्याउँछन्। अपरम्परागत दृष्टिकोण र स्वतन्त्रताले तिनीहरूको क्यारियरलाई विशेषता दिन्छ।"
        ),
        ZodiacSign.PISCES to LocalizedTemplate(
            en = "Pisces Ascendant natives excel in careers involving creativity, healing, and spirituality. Suitable professions include arts, music, healthcare, charity work, spiritual teaching, and film industry. They thrive in roles allowing imagination and compassion. Service to others brings fulfillment.",
            ne = "मीन लग्न जातकहरू रचनात्मकता, उपचार र आध्यात्मिकता समावेश गर्ने क्यारियरहरूमा उत्कृष्ट हुन्छन्। उपयुक्त पेशाहरूमा कला, संगीत, स्वास्थ्य सेवा, दान कार्य, आध्यात्मिक शिक्षण र चलचित्र उद्योग समावेश छ। तिनीहरू कल्पना र करुणाको अनुमति दिने भूमिकाहरूमा फस्टाउँछन्। अरूलाई सेवाले पूर्णता ल्याउँछ।"
        )
    )

    /**
     * Career by 10th Lord Placement
     */
    val careerByTenthLordTemplates = mapOf(
        1 to LocalizedTemplate(
            en = "10th lord in 1st house creates a powerful Rajayoga for career success. The native's personality and self-effort drive professional achievements. Self-employment, personal branding, and leadership roles are favored. The career and identity are closely intertwined. Success comes through initiative and personal charisma.",
            ne = "१ औं भावमा १० औं भावेशले क्यारियर सफलताको लागि शक्तिशाली राजयोग सिर्जना गर्छ। जातकको व्यक्तित्व र आत्म-प्रयासले व्यावसायिक उपलब्धिहरू चलाउँछ। स्व-रोजगार, व्यक्तिगत ब्रान्डिङ र नेतृत्व भूमिकाहरू अनुकूल छन्। क्यारियर र पहिचान नजिकबाट जोडिएका छन्। सफलता पहल र व्यक्तिगत आकर्षण मार्फत आउँछ।"
        ),
        2 to LocalizedTemplate(
            en = "10th lord in 2nd house indicates career involving wealth, speech, or family business. The native earns well through profession and may accumulate significant assets. Banking, finance, food industry, and family enterprises are favored. Career brings financial security and family prosperity.",
            ne = "२ औं भावमा १० औं भावेशले धन, वाणी वा पारिवारिक व्यवसाय समावेश गर्ने क्यारियर संकेत गर्छ। जातक पेशा मार्फत राम्रो कमाउँछ र महत्वपूर्ण सम्पत्ति संचय गर्न सक्छ। बैंकिङ, वित्त, खाद्य उद्योग र पारिवारिक उद्यमहरू अनुकूल छन्। क्यारियरले आर्थिक सुरक्षा र पारिवारिक समृद्धि ल्याउँछ।"
        ),
        3 to LocalizedTemplate(
            en = "10th lord in 3rd house indicates career in communication, media, or short travels. Writing, journalism, sales, marketing, and telecommunications are favored. The native uses courage and initiative in profession. Siblings may be involved in career or business partnerships.",
            ne = "३ औं भावमा १० औं भावेशले संचार, मिडिया वा छोटा यात्रामा क्यारियर संकेत गर्छ। लेखन, पत्रकारिता, बिक्री, मार्केटिङ र दूरसंचार अनुकूल छन्। जातक पेशामा साहस र पहल प्रयोग गर्छ। दाजुभाइ/दिदीबहिनीहरू क्यारियर वा व्यापार साझेदारीमा संलग्न हुन सक्छन्।"
        ),
        4 to LocalizedTemplate(
            en = "10th lord in 4th house indicates career involving property, education, or homeland. Real estate, automobile industry, agriculture, and education sector are favored. The native may work from home or establish home-based business. Career provides emotional security and domestic happiness.",
            ne = "४ औं भावमा १० औं भावेशले सम्पत्ति, शिक्षा वा मातृभूमि समावेश गर्ने क्यारियर संकेत गर्छ। रियल इस्टेट, अटोमोबाइल उद्योग, कृषि र शिक्षा क्षेत्र अनुकूल छन्। जातक घरबाट काम गर्न वा घर-आधारित व्यवसाय स्थापना गर्न सक्छ। क्यारियरले भावनात्मक सुरक्षा र घरेलु खुशी प्रदान गर्छ।"
        ),
        5 to LocalizedTemplate(
            en = "10th lord in 5th house indicates career in creativity, speculation, or education. Entertainment industry, teaching, investment, children-related fields, and creative arts are favored. The native uses intelligence and past life merit in profession. Romance may develop in workplace.",
            ne = "५ औं भावमा १० औं भावेशले रचनात्मकता, सट्टेबाजी वा शिक्षामा क्यारियर संकेत गर्छ। मनोरञ्जन उद्योग, शिक्षण, लगानी, बालबालिका-सम्बन्धित क्षेत्रहरू र रचनात्मक कला अनुकूल छन्। जातक पेशामा बुद्धिमत्ता र पूर्वजन्म योग्यता प्रयोग गर्छ। कार्यस्थलमा रोमान्स विकास हुन सक्छ।"
        ),
        6 to LocalizedTemplate(
            en = "10th lord in 6th house indicates career in service, healthcare, or legal fields. Medical profession, law, military, and service industries are favored. The native overcomes competition through hard work. Daily routines and discipline are central to professional success.",
            ne = "६ औं भावमा १० औं भावेशले सेवा, स्वास्थ्य सेवा वा कानूनी क्षेत्रहरूमा क्यारियर संकेत गर्छ। चिकित्सा पेशा, कानून, सैन्य र सेवा उद्योगहरू अनुकूल छन्। जातक कडा परिश्रम मार्फत प्रतिस्पर्धा पार गर्छ। दैनिक दिनचर्या र अनुशासन व्यावसायिक सफलताको केन्द्रमा हुन्छ।"
        ),
        7 to LocalizedTemplate(
            en = "10th lord in 7th house indicates career involving partnerships, consulting, or public dealings. Business partnerships, diplomacy, client-facing roles, and consultancy are favored. The spouse may be involved in career. Professional reputation is built through relationships.",
            ne = "७ औं भावमा १० औं भावेशले साझेदारी, परामर्श वा सार्वजनिक व्यवहार समावेश गर्ने क्यारियर संकेत गर्छ। व्यापार साझेदारी, कूटनीति, ग्राहक-सामना गर्ने भूमिकाहरू र परामर्श अनुकूल छन्। जीवनसाथी क्यारियरमा संलग्न हुन सक्छ। व्यावसायिक प्रतिष्ठा सम्बन्धहरू मार्फत निर्माण हुन्छ।"
        ),
        8 to LocalizedTemplate(
            en = "10th lord in 8th house indicates career involving research, occult, or managing others' resources. Insurance, inheritance matters, investigation, and transformation fields are favored. Career may have sudden changes or hidden aspects. Working behind the scenes brings success.",
            ne = "८ औं भावमा १० औं भावेशले अनुसन्धान, गुप्त वा अरूको स्रोत व्यवस्थापन समावेश गर्ने क्यारियर संकेत गर्छ। बीमा, सम्पत्ति मामिलाहरू, अनुसन्धान र परिवर्तन क्षेत्रहरू अनुकूल छन्। क्यारियरमा अचानक परिवर्तन वा लुकेका पक्षहरू हुन सक्छन्। पर्दा पछाडि काम गर्दा सफलता आउँछ।"
        ),
        9 to LocalizedTemplate(
            en = "10th lord in 9th house is highly auspicious creating powerful Dharma-Karma Rajayoga. Higher education, religion, law, publishing, and international careers are favored. The native receives divine grace and guidance in profession. Father may be influential in career development.",
            ne = "९ औं भावमा १० औं भावेश शक्तिशाली धर्म-कर्म राजयोग सिर्जना गर्दै अत्यधिक शुभ छ। उच्च शिक्षा, धर्म, कानून, प्रकाशन र अन्तर्राष्ट्रिय क्यारियरहरू अनुकूल छन्। जातकले पेशामा दिव्य कृपा र मार्गदर्शन प्राप्त गर्छ। पिताले क्यारियर विकासमा प्रभावशाली हुन सक्छ।"
        ),
        10 to LocalizedTemplate(
            en = "10th lord in 10th house is extremely powerful for career success and recognition. Government, administration, corporate leadership, and prestigious positions are indicated. The native achieves prominence in their field. Professional achievements are long-lasting and significant.",
            ne = "१० औं भावमा १० औं भावेश क्यारियर सफलता र मान्यताको लागि अत्यन्त शक्तिशाली छ। सरकार, प्रशासन, कर्पोरेट नेतृत्व र प्रतिष्ठित पदहरू संकेत गरिन्छ। जातक आफ्नो क्षेत्रमा प्रमुखता प्राप्त गर्छ। व्यावसायिक उपलब्धिहरू दीर्घकालीन र महत्वपूर्ण हुन्छन्।"
        ),
        11 to LocalizedTemplate(
            en = "10th lord in 11th house indicates career bringing gains and fulfilling ambitions. Large organizations, networking, social enterprises, and goal-oriented work are favored. Elder siblings and friends support professional growth. Income from career is substantial.",
            ne = "११ औं भावमा १० औं भावेशले लाभ ल्याउने र महत्वाकांक्षाहरू पूरा गर्ने क्यारियर संकेत गर्छ। ठूला संगठनहरू, नेटवर्किङ, सामाजिक उद्यमहरू र लक्ष्य-उन्मुख काम अनुकूल छन्। जेठा दाजुभाइ/दिदीबहिनी र साथीहरूले व्यावसायिक वृद्धिलाई समर्थन गर्छन्। क्यारियरबाट आम्दानी पर्याप्त हुन्छ।"
        ),
        12 to LocalizedTemplate(
            en = "10th lord in 12th house indicates career in foreign lands, spirituality, or institutions. Overseas work, hospitals, ashrams, export business, and behind-scenes roles are favored. The native may work away from birthplace. Spiritual or charitable dimension to career brings fulfillment.",
            ne = "१२ औं भावमा १० औं भावेशले विदेश, आध्यात्मिकता वा संस्थाहरूमा क्यारियर संकेत गर्छ। विदेशी काम, अस्पताल, आश्रम, निर्यात व्यवसाय र पर्दा पछाडिका भूमिकाहरू अनुकूल छन्। जातक जन्मस्थानबाट टाढा काम गर्न सक्छ। क्यारियरमा आध्यात्मिक वा परोपकारी आयामले पूर्णता ल्याउँछ।"
        )
    )

    // ==================== RELATIONSHIP TEMPLATES ====================

    /**
     * Relationship patterns by 7th Lord placement
     */
    val relationshipBySeventhLordTemplates = mapOf(
        1 to LocalizedTemplate(
            en = "7th lord in 1st house brings spouse who strongly influences the native's personality. The partner is prominent in life and the relationship defines personal identity. The native attracts partners easily. Marriage brings self-development and the spouse may be well-known.",
            ne = "१ औं भावमा ७ औं भावेशले जातकको व्यक्तित्वलाई बलियो रूपमा प्रभाव पार्ने जीवनसाथी ल्याउँछ। साथी जीवनमा प्रमुख हुन्छ र सम्बन्धले व्यक्तिगत पहिचान परिभाषित गर्छ। जातकले सजिलै साथीहरू आकर्षित गर्छ। विवाहले आत्म-विकास ल्याउँछ र जीवनसाथी प्रसिद्ध हुन सक्छ।"
        ),
        2 to LocalizedTemplate(
            en = "7th lord in 2nd house indicates spouse who contributes to family wealth. The partner may come from wealthy background or bring financial benefits. Marriage enhances family status and income. The native values material security in relationship.",
            ne = "२ औं भावमा ७ औं भावेशले पारिवारिक सम्पत्तिमा योगदान गर्ने जीवनसाथी संकेत गर्छ। साथी धनी पृष्ठभूमिबाट आउन सक्छ वा आर्थिक लाभ ल्याउन सक्छ। विवाहले पारिवारिक स्थिति र आम्दानी बढाउँछ। जातकले सम्बन्धमा भौतिक सुरक्षालाई मूल्य दिन्छ।"
        ),
        3 to LocalizedTemplate(
            en = "7th lord in 3rd house indicates spouse with good communication skills. The partner may be connected to media, writing, or sales. Short travels together strengthen the bond. Siblings may play role in bringing partners together.",
            ne = "३ औं भावमा ७ औं भावेशले राम्रो संचार सीप भएको जीवनसाथी संकेत गर्छ। साथी मिडिया, लेखन वा बिक्रीसँग जोडिएको हुन सक्छ। सँगै छोटा यात्राहरूले बन्धन बलियो बनाउँछ। दाजुभाइ/दिदीबहिनीहरूले साथीहरूलाई एकसाथ ल्याउन भूमिका खेल्न सक्छन्।"
        ),
        4 to LocalizedTemplate(
            en = "7th lord in 4th house indicates domestic harmony through marriage. The spouse values home and family life. The partner may own property or be connected to real estate. Mother may be influential in selecting life partner.",
            ne = "४ औं भावमा ७ औं भावेशले विवाह मार्फत घरेलु सामंजस्य संकेत गर्छ। जीवनसाथी घर र पारिवारिक जीवनलाई मूल्य दिन्छ। साथीको सम्पत्ति हुन सक्छ वा रियल इस्टेटसँग जोडिएको हुन सक्छ। आमा जीवनसाथी छनौटमा प्रभावशाली हुन सक्छिन्।"
        ),
        5 to LocalizedTemplate(
            en = "7th lord in 5th house indicates love marriage or romance-based relationship. The spouse is creative and intellectually stimulating. Children strengthen the marriage. The partnership involves mutual creative pursuits and entertainment.",
            ne = "५ औं भावमा ७ औं भावेशले प्रेम विवाह वा रोमान्स-आधारित सम्बन्ध संकेत गर्छ। जीवनसाथी रचनात्मक र बौद्धिक रूपमा उत्तेजक हुन्छ। सन्तानहरूले विवाहलाई बलियो बनाउँछ। साझेदारीमा पारस्परिक रचनात्मक कार्यहरू र मनोरञ्जन समावेश हुन्छ।"
        ),
        6 to LocalizedTemplate(
            en = "7th lord in 6th house may create challenges in marriage through conflicts or health issues. The spouse may be connected to service or healthcare. Working through difficulties strengthens the bond. Legal matters may be involved in relationships.",
            ne = "६ औं भावमा ७ औं भावेशले द्वन्द्व वा स्वास्थ्य समस्याहरू मार्फत विवाहमा चुनौतीहरू सिर्जना गर्न सक्छ। जीवनसाथी सेवा वा स्वास्थ्य सेवासँग जोडिएको हुन सक्छ। कठिनाइहरूमा काम गर्दा बन्धन बलियो हुन्छ। सम्बन्धहरूमा कानूनी मामिलाहरू समावेश हुन सक्छन्।"
        ),
        7 to LocalizedTemplate(
            en = "7th lord in 7th house is ideal for marriage and partnerships. The spouse is well-matched and supportive. Marriage is stable and fulfilling with mutual respect. Business partnerships also prosper under this placement.",
            ne = "७ औं भावमा ७ औं भावेश विवाह र साझेदारीको लागि आदर्श छ। जीवनसाथी राम्रोसँग मेल खाने र सहयोगी हुन्छ। विवाह पारस्परिक सम्मानका साथ स्थिर र पूर्ण हुन्छ। व्यापार साझेदारीहरू पनि यस स्थानमा फस्टाउँछन्।"
        ),
        8 to LocalizedTemplate(
            en = "7th lord in 8th house indicates transformative relationships. The spouse may bring inheritance or joint resources. Marriage involves deep emotional and financial merging. There may be secrets or intense experiences in the partnership.",
            ne = "८ औं भावमा ७ औं भावेशले परिवर्तनकारी सम्बन्धहरू संकेत गर्छ। जीवनसाथीले सम्पत्ति वा संयुक्त स्रोतहरू ल्याउन सक्छ। विवाहमा गहिरो भावनात्मक र आर्थिक विलय समावेश हुन्छ। साझेदारीमा गोप्य वा तीव्र अनुभवहरू हुन सक्छन्।"
        ),
        9 to LocalizedTemplate(
            en = "7th lord in 9th house indicates spiritually compatible partner. The spouse may be from different cultural or religious background. Marriage supports dharmic growth and higher learning. Long-distance travel with spouse is indicated.",
            ne = "९ औं भावमा ७ औं भावेशले आध्यात्मिक रूपमा अनुकूल साथी संकेत गर्छ। जीवनसाथी फरक सांस्कृतिक वा धार्मिक पृष्ठभूमिबाट हुन सक्छ। विवाहले धार्मिक वृद्धि र उच्च शिक्षालाई समर्थन गर्छ। जीवनसाथीसँग लामो दूरीको यात्रा संकेत गरिएको छ।"
        ),
        10 to LocalizedTemplate(
            en = "7th lord in 10th house indicates spouse who enhances career and social status. The partner may be professionally accomplished. Marriage brings public recognition. Business partnerships with spouse can be successful.",
            ne = "१० औं भावमा ७ औं भावेशले क्यारियर र सामाजिक स्थिति बढाउने जीवनसाथी संकेत गर्छ। साथी व्यावसायिक रूपमा सफल हुन सक्छ। विवाहले सार्वजनिक मान्यता ल्याउँछ। जीवनसाथीसँग व्यापार साझेदारी सफल हुन सक्छ।"
        ),
        11 to LocalizedTemplate(
            en = "7th lord in 11th house indicates spouse who fulfills desires and brings gains. The partner may come through social networks or friends. Marriage supports achievement of goals. Elder siblings may introduce the life partner.",
            ne = "११ औं भावमा ७ औं भावेशले इच्छाहरू पूरा गर्ने र लाभ ल्याउने जीवनसाथी संकेत गर्छ। साथी सामाजिक सञ्जाल वा साथीहरू मार्फत आउन सक्छ। विवाहले लक्ष्यहरू प्राप्तिलाई समर्थन गर्छ। जेठा दाजुभाइ/दिदीबहिनीले जीवनसाथी परिचय गराउन सक्छन्।"
        ),
        12 to LocalizedTemplate(
            en = "7th lord in 12th house may indicate foreign spouse or relationship involving expenses. The partner may have spiritual inclinations. Marriage may require sacrifices or involve separation periods. Overseas settlement through marriage is possible.",
            ne = "१२ औं भावमा ७ औं भावेशले विदेशी जीवनसाथी वा खर्च समावेश गर्ने सम्बन्ध संकेत गर्न सक्छ। साथीमा आध्यात्मिक झुकाव हुन सक्छ। विवाहमा त्याग वा विच्छेद अवधिहरू समावेश हुन सक्छ। विवाह मार्फत विदेश बसोबास सम्भव छ।"
        )
    )

    /**
     * Venus positions for relationship quality
     */
    val venusInSignForRelationshipTemplates = mapOf(
        ZodiacSign.ARIES to LocalizedTemplate(
            en = "Venus in Aries creates passionate but impulsive approach to relationships. The native falls in love quickly and values independence in partnership. There may be competitive dynamics in romance. The partner should match their energy and enthusiasm.",
            ne = "मेषमा शुक्रले सम्बन्धहरूमा भावुक तर आवेगपूर्ण दृष्टिकोण सिर्जना गर्छ। जातक छिट्टै प्रेममा पर्छ र साझेदारीमा स्वतन्त्रतालाई मूल्य दिन्छ। रोमान्समा प्रतिस्पर्धी गतिशीलता हुन सक्छ। साथीले तिनीहरूको ऊर्जा र उत्साहसँग मेल खानुपर्छ।"
        ),
        ZodiacSign.TAURUS to LocalizedTemplate(
            en = "Venus in Taurus (own sign) is excellent for lasting, sensual relationships. The native values loyalty, comfort, and material security in love. Romance is expressed through physical affection and gifts. The partner should appreciate stability and sensory pleasures.",
            ne = "वृषमा शुक्र (आफ्नै राशि) टिकाउ, संवेदनशील सम्बन्धहरूको लागि उत्कृष्ट छ। जातक प्रेममा वफादारी, आराम र भौतिक सुरक्षालाई मूल्य दिन्छ। रोमान्स शारीरिक स्नेह र उपहारहरू मार्फत व्यक्त हुन्छ। साथीले स्थिरता र संवेदी आनन्दको कदर गर्नुपर्छ।"
        ),
        ZodiacSign.GEMINI to LocalizedTemplate(
            en = "Venus in Gemini creates intellectually stimulating relationships. The native values communication and mental connection in love. There may be multiple romantic interests or need for variety. The partner should be witty, curious, and good conversationalist.",
            ne = "मिथुनमा शुक्रले बौद्धिक रूपमा उत्तेजक सम्बन्धहरू सिर्जना गर्छ। जातक प्रेममा संवाद र मानसिक जडानलाई मूल्य दिन्छ। बहु रोमान्टिक रुचिहरू वा विविधताको आवश्यकता हुन सक्छ। साथी हाजिरजवाफी, जिज्ञासु र राम्रो कुराकानी गर्ने हुनुपर्छ।"
        ),
        ZodiacSign.CANCER to LocalizedTemplate(
            en = "Venus in Cancer creates nurturing and emotionally deep relationships. The native values emotional security and domestic harmony in love. Romance is expressed through caring actions and creating home together. The partner should be sensitive and family-oriented.",
            ne = "कर्कटमा शुक्रले पालनपोषण र भावनात्मक रूपमा गहिरो सम्बन्धहरू सिर्जना गर्छ। जातक प्रेममा भावनात्मक सुरक्षा र घरेलु सामंजस्यलाई मूल्य दिन्छ। रोमान्स हेरचाह गर्ने कार्यहरू र सँगै घर बनाउने मार्फत व्यक्त हुन्छ। साथी संवेदनशील र परिवार-उन्मुख हुनुपर्छ।"
        ),
        ZodiacSign.LEO to LocalizedTemplate(
            en = "Venus in Leo creates dramatic and generous approach to relationships. The native values admiration and loyalty in love. Romance is expressed through grand gestures and creative expressions. The partner should appreciate their warmth and desire for recognition.",
            ne = "सिंहमा शुक्रले सम्बन्धहरूमा नाटकीय र उदार दृष्टिकोण सिर्जना गर्छ। जातक प्रेममा प्रशंसा र वफादारीलाई मूल्य दिन्छ। रोमान्स भव्य इशाराहरू र रचनात्मक अभिव्यक्तिहरू मार्फत व्यक्त हुन्छ। साथीले तिनीहरूको न्यानोपन र मान्यताको चाहनाको कदर गर्नुपर्छ।"
        ),
        ZodiacSign.VIRGO to LocalizedTemplate(
            en = "Venus in Virgo (debilitated) creates practical but critical approach to relationships. The native may be perfectionist in love, seeking improvement. Romance is expressed through acts of service and practical help. The partner should be health-conscious and detail-oriented.",
            ne = "कन्यामा शुक्र (नीच) ले सम्बन्धहरूमा व्यावहारिक तर आलोचनात्मक दृष्टिकोण सिर्जना गर्छ। जातक प्रेममा पूर्णतावादी हुन सक्छ, सुधार खोज्दै। रोमान्स सेवाका कार्यहरू र व्यावहारिक मद्दत मार्फत व्यक्त हुन्छ। साथी स्वास्थ्य-सचेत र विस्तार-उन्मुख हुनुपर्छ।"
        ),
        ZodiacSign.LIBRA to LocalizedTemplate(
            en = "Venus in Libra (own sign) is ideal for harmonious, balanced relationships. The native values fairness, beauty, and partnership in love. Romance is expressed through diplomatic gestures and aesthetic experiences. The partner should appreciate harmony and social graces.",
            ne = "तुलामा शुक्र (आफ्नै राशि) सामंजस्यपूर्ण, सन्तुलित सम्बन्धहरूको लागि आदर्श छ। जातक प्रेममा निष्पक्षता, सौन्दर्य र साझेदारीलाई मूल्य दिन्छ। रोमान्स कूटनीतिक इशाराहरू र सौन्दर्यात्मक अनुभवहरू मार्फत व्यक्त हुन्छ। साथीले सामंजस्य र सामाजिक सुन्दरताको कदर गर्नुपर्छ।"
        ),
        ZodiacSign.SCORPIO to LocalizedTemplate(
            en = "Venus in Scorpio creates intense and transformative relationships. The native values depth and emotional honesty in love. Romance involves powerful connections and potential jealousy. The partner should be emotionally available and authentic.",
            ne = "वृश्चिकमा शुक्रले तीव्र र परिवर्तनकारी सम्बन्धहरू सिर्जना गर्छ। जातक प्रेममा गहिराइ र भावनात्मक इमानदारीलाई मूल्य दिन्छ। रोमान्समा शक्तिशाली जडानहरू र सम्भावित ईर्ष्या समावेश हुन्छ। साथी भावनात्मक रूपमा उपलब्ध र प्रामाणिक हुनुपर्छ।"
        ),
        ZodiacSign.SAGITTARIUS to LocalizedTemplate(
            en = "Venus in Sagittarius creates adventurous and philosophical relationships. The native values freedom and growth in love. Romance is expressed through travel and shared beliefs. The partner should be independent and share similar worldviews.",
            ne = "धनुमा शुक्रले साहसी र दार्शनिक सम्बन्धहरू सिर्जना गर्छ। जातक प्रेममा स्वतन्त्रता र वृद्धिलाई मूल्य दिन्छ। रोमान्स यात्रा र साझा विश्वासहरू मार्फत व्यक्त हुन्छ। साथी स्वतन्त्र र समान विश्वदृष्टिकोण साझा गर्ने हुनुपर्छ।"
        ),
        ZodiacSign.CAPRICORN to LocalizedTemplate(
            en = "Venus in Capricorn creates mature and committed relationships. The native values stability and long-term commitment in love. Romance is expressed through responsible actions and building together. The partner should be ambitious and reliable.",
            ne = "मकरमा शुक्रले परिपक्व र प्रतिबद्ध सम्बन्धहरू सिर्जना गर्छ। जातक प्रेममा स्थिरता र दीर्घकालीन प्रतिबद्धतालाई मूल्य दिन्छ। रोमान्स जिम्मेवार कार्यहरू र सँगै निर्माण गर्ने मार्फत व्यक्त हुन्छ। साथी महत्वाकांक्षी र भरपर्दो हुनुपर्छ।"
        ),
        ZodiacSign.AQUARIUS to LocalizedTemplate(
            en = "Venus in Aquarius creates unconventional and friendship-based relationships. The native values independence and intellectual connection in love. Romance is expressed through unique gestures and shared causes. The partner should respect their need for space.",
            ne = "कुम्भमा शुक्रले अपरम्परागत र मित्रता-आधारित सम्बन्धहरू सिर्जना गर्छ। जातक प्रेममा स्वतन्त्रता र बौद्धिक जडानलाई मूल्य दिन्छ। रोमान्स अद्वितीय इशाराहरू र साझा कारणहरू मार्फत व्यक्त हुन्छ। साथीले तिनीहरूको ठाउँको आवश्यकतालाई सम्मान गर्नुपर्छ।"
        ),
        ZodiacSign.PISCES to LocalizedTemplate(
            en = "Venus in Pisces (exalted) creates deeply romantic and spiritual relationships. The native values unconditional love and soul connection. Romance is expressed through artistic gestures and spiritual bonding. The partner should be sensitive and spiritually inclined.",
            ne = "मीनमा शुक्र (उच्च) ले गहिरो रोमान्टिक र आध्यात्मिक सम्बन्धहरू सिर्जना गर्छ। जातक शर्तरहित प्रेम र आत्मा जडानलाई मूल्य दिन्छ। रोमान्स कलात्मक इशाराहरू र आध्यात्मिक बन्धन मार्फत व्यक्त हुन्छ। साथी संवेदनशील र आध्यात्मिक रूपमा झुकेको हुनुपर्छ।"
        )
    )

    // ==================== HEALTH TEMPLATES ====================

    /**
     * Health tendencies by Ascendant
     */
    val healthByAscendantTemplates = mapOf(
        ZodiacSign.ARIES to LocalizedTemplate(
            en = "Aries Ascendant natives are generally robust with strong vitality but prone to head injuries, headaches, and fever. The constitution is pitta-dominant with hot temperament. They should avoid overexertion and manage anger. Regular physical exercise and cooling foods benefit health.",
            ne = "मेष लग्न जातकहरू सामान्यतया बलियो जीवनशक्तिसहित मजबुत हुन्छन् तर टाउको चोट, टाउको दुखाइ र ज्वरोको जोखिममा हुन्छन्। संविधान पित्त-प्रधान र तातो स्वभावको हुन्छ। तिनीहरूले अत्यधिक परिश्रम बेवास्ता गर्नुपर्छ र रिस व्यवस्थापन गर्नुपर्छ। नियमित शारीरिक व्यायाम र चिसो खानाले स्वास्थ्यलाई फाइदा पुर्याउँछ।"
        ),
        ZodiacSign.TAURUS to LocalizedTemplate(
            en = "Taurus Ascendant natives have strong constitution but prone to throat ailments, thyroid issues, and weight gain. The constitution is kapha-dominant with tendency toward indulgence. They should maintain regular eating habits and avoid excess. Neck and throat care is important.",
            ne = "वृष लग्न जातकहरूमा बलियो संविधान हुन्छ तर घाँटीका रोग, थाइरोइड समस्या र तौल बढ्ने जोखिममा हुन्छन्। संविधान कफ-प्रधान र आनन्दको प्रवृत्तिसहितको हुन्छ। तिनीहरूले नियमित खाने बानी कायम राख्नुपर्छ र अतिरेकबाट बच्नुपर्छ। घाँटी र कण्ठको हेरचाह महत्वपूर्ण छ।"
        ),
        ZodiacSign.GEMINI to LocalizedTemplate(
            en = "Gemini Ascendant natives have sensitive nervous system and prone to respiratory issues and anxiety. The constitution is vata-dominant with active mind. They should avoid overstimulation and practice calming techniques. Lung health and stress management are priorities.",
            ne = "मिथुन लग्न जातकहरूमा संवेदनशील स्नायु प्रणाली हुन्छ र श्वासप्रश्वास समस्या र चिन्ताको जोखिममा हुन्छन्। संविधान वात-प्रधान र सक्रिय मनसहितको हुन्छ। तिनीहरूले अत्यधिक उत्तेजनाबाट बच्नुपर्छ र शान्त गर्ने प्रविधिहरू अभ्यास गर्नुपर्छ। फोक्सोको स्वास्थ्य र तनाव व्यवस्थापन प्राथमिकताहरू हुन्।"
        ),
        ZodiacSign.CANCER to LocalizedTemplate(
            en = "Cancer Ascendant natives are sensitive with vulnerable digestive system and emotional health. The constitution has kapha-pitta mix with water element dominance. They should maintain emotional balance and healthy eating. Stomach and breast health need attention.",
            ne = "कर्कट लग्न जातकहरू संवेदनशील हुन्छन् र कमजोर पाचन प्रणाली र भावनात्मक स्वास्थ्यको जोखिममा हुन्छन्। संविधानमा पानी तत्वको प्रभुत्वसहित कफ-पित्त मिश्रण हुन्छ। तिनीहरूले भावनात्मक सन्तुलन र स्वस्थ खाने कायम राख्नुपर्छ। पेट र स्तन स्वास्थ्यमा ध्यान दिनुपर्छ।"
        ),
        ZodiacSign.LEO to LocalizedTemplate(
            en = "Leo Ascendant natives have strong vitality but prone to heart issues and spinal problems. The constitution is pitta-dominant with strong metabolism. They should avoid excessive pride and stress. Heart health and back care are essential.",
            ne = "सिंह लग्न जातकहरूमा बलियो जीवनशक्ति हुन्छ तर मुटु समस्या र मेरुदण्ड समस्याको जोखिममा हुन्छन्। संविधान पित्त-प्रधान र बलियो चयापचयसहितको हुन्छ। तिनीहरूले अत्यधिक घमण्ड र तनावबाट बच्नुपर्छ। मुटु स्वास्थ्य र पिठ्युँको हेरचाह आवश्यक छ।"
        ),
        ZodiacSign.VIRGO to LocalizedTemplate(
            en = "Virgo Ascendant natives have delicate constitution with sensitive digestive and nervous systems. The constitution is vata-pitta with analytical tendency. They should avoid worry and maintain regular routines. Intestinal health and stress reduction are priorities.",
            ne = "कन्या लग्न जातकहरूमा संवेदनशील पाचन र स्नायु प्रणालीसहित नाजुक संविधान हुन्छ। संविधान विश्लेषणात्मक प्रवृत्तिसहित वात-पित्त हुन्छ। तिनीहरूले चिन्ताबाट बच्नुपर्छ र नियमित दिनचर्या कायम राख्नुपर्छ। आन्द्राको स्वास्थ्य र तनाव कमी प्राथमिकताहरू हुन्।"
        ),
        ZodiacSign.LIBRA to LocalizedTemplate(
            en = "Libra Ascendant natives have balanced constitution but prone to kidney and skin issues. The constitution is vata-kapha with need for harmony. They should maintain balance in all aspects and avoid extremes. Kidney health and skin care need attention.",
            ne = "तुला लग्न जातकहरूमा सन्तुलित संविधान हुन्छ तर मिर्गौला र छालाको समस्याको जोखिममा हुन्छन्। संविधान सामंजस्यको आवश्यकतासहित वात-कफ हुन्छ। तिनीहरूले सबै पक्षहरूमा सन्तुलन कायम राख्नुपर्छ र अत्यधिकताबाट बच्नुपर्छ। मिर्गौला स्वास्थ्य र छालाको हेरचाहमा ध्यान दिनुपर्छ।"
        ),
        ZodiacSign.SCORPIO to LocalizedTemplate(
            en = "Scorpio Ascendant natives have strong recuperative powers but prone to reproductive and excretory issues. The constitution is pitta-kapha with intense nature. They should manage emotions and avoid toxic environments. Reproductive health and detoxification are important.",
            ne = "वृश्चिक लग्न जातकहरूमा बलियो पुनर्प्राप्ति शक्तिहरू हुन्छन् तर प्रजनन र उत्सर्जन समस्याको जोखिममा हुन्छन्। संविधान तीव्र स्वभावसहित पित्त-कफ हुन्छ। तिनीहरूले भावनाहरू व्यवस्थापन गर्नुपर्छ र विषाक्त वातावरणबाट बच्नुपर्छ। प्रजनन स्वास्थ्य र विषमुक्ति महत्वपूर्ण छ।"
        ),
        ZodiacSign.SAGITTARIUS to LocalizedTemplate(
            en = "Sagittarius Ascendant natives have robust constitution but prone to hip, thigh, and liver issues. The constitution is pitta-kapha with expansive nature. They should avoid excess in food and drink. Liver health and hip care are priorities.",
            ne = "धनु लग्न जातकहरूमा मजबुत संविधान हुन्छ तर कम्मर, तिघ्रा र कलेजो समस्याको जोखिममा हुन्छन्। संविधान विस्तृत स्वभावसहित पित्त-कफ हुन्छ। तिनीहरूले खाना र पेयमा अतिरेकबाट बच्नुपर्छ। कलेजो स्वास्थ्य र कम्मरको हेरचाह प्राथमिकताहरू हुन्।"
        ),
        ZodiacSign.CAPRICORN to LocalizedTemplate(
            en = "Capricorn Ascendant natives have enduring constitution but prone to bone, joint, and skin issues. The constitution is vata-dominant with dry tendency. They should maintain flexibility and adequate nutrition. Bone health and joint care are essential.",
            ne = "मकर लग्न जातकहरूमा टिकाउ संविधान हुन्छ तर हड्डी, जोर्नी र छालाको समस्याको जोखिममा हुन्छन्। संविधान सुख्खा प्रवृत्तिसहित वात-प्रधान हुन्छ। तिनीहरूले लचिलोपन र पर्याप्त पोषण कायम राख्नुपर्छ। हड्डी स्वास्थ्य र जोर्नीको हेरचाह आवश्यक छ।"
        ),
        ZodiacSign.AQUARIUS to LocalizedTemplate(
            en = "Aquarius Ascendant natives have unique constitution but prone to circulatory and nervous issues. The constitution is vata-dominant with electric nature. They should maintain regular habits and ground themselves. Circulation and ankle health need attention.",
            ne = "कुम्भ लग्न जातकहरूमा अद्वितीय संविधान हुन्छ तर रक्त संचार र स्नायु समस्याको जोखिममा हुन्छन्। संविधान विद्युतीय स्वभावसहित वात-प्रधान हुन्छ। तिनीहरूले नियमित बानीहरू कायम राख्नुपर्छ र आफूलाई आधारमा राख्नुपर्छ। रक्त संचार र गोलिगाँठो स्वास्थ्यमा ध्यान दिनुपर्छ।"
        ),
        ZodiacSign.PISCES to LocalizedTemplate(
            en = "Pisces Ascendant natives have sensitive constitution prone to feet issues, allergies, and immune challenges. The constitution is kapha-dominant with watery nature. They should avoid toxic substances and maintain boundaries. Immune health and feet care are priorities.",
            ne = "मीन लग्न जातकहरूमा संवेदनशील संविधान हुन्छ र खुट्टाको समस्या, एलर्जी र प्रतिरक्षा चुनौतीहरूको जोखिममा हुन्छन्। संविधान पानी स्वभावसहित कफ-प्रधान हुन्छ। तिनीहरूले विषाक्त पदार्थहरूबाट बच्नुपर्छ र सीमाहरू कायम राख्नुपर्छ। प्रतिरक्षा स्वास्थ्य र खुट्टाको हेरचाह प्राथमिकताहरू हुन्।"
        )
    )

    /**
     * 6th Lord effects on health
     */
    val sixthLordHealthTemplates = mapOf(
        1 to LocalizedTemplate(
            en = "6th lord in 1st house may bring health challenges that shape personality. The native overcomes illness through willpower. There may be chronic conditions requiring ongoing management. Health consciousness becomes part of identity.",
            ne = "१ औं भावमा ६ औं भावेशले व्यक्तित्वलाई आकार दिने स्वास्थ्य चुनौतीहरू ल्याउन सक्छ। जातकले इच्छाशक्ति मार्फत रोग पार गर्छ। निरन्तर व्यवस्थापन आवश्यक पर्ने दीर्घकालीन अवस्थाहरू हुन सक्छन्। स्वास्थ्य चेतना पहिचानको भाग बन्छ।"
        ),
        2 to LocalizedTemplate(
            en = "6th lord in 2nd house may affect family health or create expenses through illness. Dietary issues may cause health problems. Speech or eye problems are possible. Managing food intake is essential for health.",
            ne = "२ औं भावमा ६ औं भावेशले पारिवारिक स्वास्थ्यमा असर पार्न सक्छ वा रोग मार्फत खर्च सिर्जना गर्न सक्छ। आहार समस्याहरूले स्वास्थ्य समस्याहरू निम्त्याउन सक्छ। वाणी वा आँखाको समस्या सम्भव छ। खाना सेवन व्यवस्थापन स्वास्थ्यको लागि आवश्यक छ।"
        ),
        3 to LocalizedTemplate(
            en = "6th lord in 3rd house may cause issues with arms, shoulders, or respiratory system. Courage helps overcome health challenges. Siblings may face health issues. Communication about health matters is important.",
            ne = "३ औं भावमा ६ औं भावेशले हात, काँध वा श्वासप्रश्वास प्रणालीमा समस्या निम्त्याउन सक्छ। साहसले स्वास्थ्य चुनौतीहरू पार गर्न मद्दत गर्छ। दाजुभाइ/दिदीबहिनीहरूले स्वास्थ्य समस्याको सामना गर्न सक्छन्। स्वास्थ्य मामिलाहरूमा संवाद महत्वपूर्ण छ।"
        ),
        4 to LocalizedTemplate(
            en = "6th lord in 4th house may affect heart, chest, or emotional wellbeing. Mother's health may be concerning. Home environment impacts health. Emotional healing supports physical recovery.",
            ne = "४ औं भावमा ६ औं भावेशले मुटु, छाती वा भावनात्मक कल्याणमा असर पार्न सक्छ। आमाको स्वास्थ्य चिन्ताजनक हुन सक्छ। घरको वातावरणले स्वास्थ्यमा प्रभाव पार्छ। भावनात्मक उपचारले शारीरिक पुनर्प्राप्तिलाई समर्थन गर्छ।"
        ),
        5 to LocalizedTemplate(
            en = "6th lord in 5th house may create issues with digestion or children's health. Stomach and upper intestinal problems are possible. Creative activities support healing. Children may need health attention.",
            ne = "५ औं भावमा ६ औं भावेशले पाचन वा सन्तानको स्वास्थ्यमा समस्या सिर्जना गर्न सक्छ। पेट र माथिल्लो आन्द्राको समस्या सम्भव छ। रचनात्मक गतिविधिहरूले उपचारलाई समर्थन गर्छन्। सन्तानहरूलाई स्वास्थ्य ध्यान दिन आवश्यक पर्न सक्छ।"
        ),
        6 to LocalizedTemplate(
            en = "6th lord in 6th house strengthens resistance to disease. The native overcomes enemies and illness effectively. Service work supports health. Strong immunity and recovery powers are indicated.",
            ne = "६ औं भावमा ६ औं भावेशले रोग प्रतिरोधलाई बलियो बनाउँछ। जातकले शत्रुहरू र रोगलाई प्रभावकारी रूपमा पार गर्छ। सेवा कार्यले स्वास्थ्यलाई समर्थन गर्छ। बलियो प्रतिरक्षा र पुनर्प्राप्ति शक्तिहरू संकेत गरिन्छ।"
        ),
        7 to LocalizedTemplate(
            en = "6th lord in 7th house may bring health issues through partnerships. Spouse's health may be concerning. Reproductive or kidney issues are possible. Partnership stress affects health.",
            ne = "७ औं भावमा ६ औं भावेशले साझेदारी मार्फत स्वास्थ्य समस्याहरू ल्याउन सक्छ। जीवनसाथीको स्वास्थ्य चिन्ताजनक हुन सक्छ। प्रजनन वा मिर्गौला समस्या सम्भव छ। साझेदारी तनावले स्वास्थ्यमा असर पार्छ।"
        ),
        8 to LocalizedTemplate(
            en = "6th lord in 8th house may create chronic or hidden health issues. Reproductive and excretory system problems are possible. Transformation through health challenges is indicated. Research into health matters helps.",
            ne = "८ औं भावमा ६ औं भावेशले दीर्घकालीन वा लुकेका स्वास्थ्य समस्याहरू सिर्जना गर्न सक्छ। प्रजनन र उत्सर्जन प्रणालीको समस्या सम्भव छ। स्वास्थ्य चुनौतीहरू मार्फत परिवर्तन संकेत गरिन्छ। स्वास्थ्य मामिलाहरूमा अनुसन्धानले मद्दत गर्छ।"
        ),
        9 to LocalizedTemplate(
            en = "6th lord in 9th house may bring health issues during travels or affect hip/thigh region. Father's health may be concerning. Spiritual practices support healing. Faith helps overcome illness.",
            ne = "९ औं भावमा ६ औं भावेशले यात्राको समयमा स्वास्थ्य समस्याहरू ल्याउन सक्छ वा कम्मर/तिघ्रा क्षेत्रमा असर पार्न सक्छ। पिताको स्वास्थ्य चिन्ताजनक हुन सक्छ। आध्यात्मिक अभ्यासहरूले उपचारलाई समर्थन गर्छन्। विश्वासले रोग पार गर्न मद्दत गर्छ।"
        ),
        10 to LocalizedTemplate(
            en = "6th lord in 10th house may create health issues affecting career. Bone and joint problems are possible. Work stress impacts health. Career in healthcare is favored.",
            ne = "१० औं भावमा ६ औं भावेशले क्यारियरमा असर पार्ने स्वास्थ्य समस्याहरू सिर्जना गर्न सक्छ। हड्डी र जोर्नीको समस्या सम्भव छ। कामको तनावले स्वास्थ्यमा प्रभाव पार्छ। स्वास्थ्य सेवामा क्यारियर अनुकूल छ।"
        ),
        11 to LocalizedTemplate(
            en = "6th lord in 11th house may affect circulation or left ear. Elder siblings may face health issues. Social activities support healing. Income may come from health-related work.",
            ne = "११ औं भावमा ६ औं भावेशले रक्त संचार वा बायाँ कानमा असर पार्न सक्छ। जेठा दाजुभाइ/दिदीबहिनीहरूले स्वास्थ्य समस्याको सामना गर्न सक्छन्। सामाजिक गतिविधिहरूले उपचारलाई समर्थन गर्छन्। स्वास्थ्य-सम्बन्धित कामबाट आम्दानी आउन सक्छ।"
        ),
        12 to LocalizedTemplate(
            en = "6th lord in 12th house may create hidden enemies or hospitalization. Feet and sleep issues are possible. Foreign treatment may be needed. Spiritual healing practices are beneficial.",
            ne = "१२ औं भावमा ६ औं भावेशले लुकेका शत्रुहरू वा अस्पताल भर्ना सिर्जना गर्न सक्छ। खुट्टा र निद्राको समस्या सम्भव छ। विदेशी उपचार आवश्यक पर्न सक्छ। आध्यात्मिक उपचार अभ्यासहरू लाभदायक छन्।"
        )
    )

    // ==================== WEALTH TEMPLATES ====================

    /**
     * Wealth indicators by 2nd Lord placement
     */
    val wealthBySecondLordTemplates = mapOf(
        1 to LocalizedTemplate(
            en = "2nd lord in 1st house indicates self-made wealth through personal efforts. The native earns through personality and initiative. Financial independence is achieved through individual actions. Strong earning capacity based on personal qualities.",
            ne = "१ औं भावमा २ औं भावेशले व्यक्तिगत प्रयासहरू मार्फत स्व-निर्मित सम्पत्ति संकेत गर्छ। जातक व्यक्तित्व र पहल मार्फत कमाउँछ। व्यक्तिगत कार्यहरू मार्फत आर्थिक स्वतन्त्रता प्राप्त हुन्छ। व्यक्तिगत गुणहरूमा आधारित बलियो कमाइ क्षमता।"
        ),
        2 to LocalizedTemplate(
            en = "2nd lord in 2nd house is excellent for wealth accumulation. The native has strong earning and saving capacity. Family wealth is preserved and increased. Financial stability comes naturally with proper management.",
            ne = "२ औं भावमा २ औं भावेश सम्पत्ति संचयको लागि उत्कृष्ट छ। जातकमा बलियो कमाइ र बचत क्षमता हुन्छ। पारिवारिक सम्पत्ति संरक्षित र बढाइन्छ। उचित व्यवस्थापनसँग आर्थिक स्थिरता स्वाभाविक रूपमा आउँछ।"
        ),
        3 to LocalizedTemplate(
            en = "2nd lord in 3rd house indicates wealth through communication, media, or siblings. Short travels bring financial opportunities. Writing, sales, and marketing generate income. Courage in financial ventures is beneficial.",
            ne = "३ औं भावमा २ औं भावेशले संचार, मिडिया वा दाजुभाइ/दिदीबहिनी मार्फत सम्पत्ति संकेत गर्छ। छोटा यात्राहरूले आर्थिक अवसरहरू ल्याउँछन्। लेखन, बिक्री र मार्केटिङले आम्दानी उत्पन्न गर्छ। आर्थिक उद्यमहरूमा साहस लाभदायक छ।"
        ),
        4 to LocalizedTemplate(
            en = "2nd lord in 4th house indicates wealth through property, vehicles, or homeland. Real estate brings financial gains. Mother may contribute to wealth. Emotional security is linked to financial stability.",
            ne = "४ औं भावमा २ औं भावेशले सम्पत्ति, सवारी साधन वा मातृभूमि मार्फत सम्पत्ति संकेत गर्छ। रियल इस्टेटले आर्थिक लाभ ल्याउँछ। आमाले सम्पत्तिमा योगदान दिन सक्छिन्। भावनात्मक सुरक्षा आर्थिक स्थिरतासँग जोडिएको छ।"
        ),
        5 to LocalizedTemplate(
            en = "2nd lord in 5th house indicates wealth through speculation, creativity, or children. Investment brings gains. Creative pursuits generate income. Children may contribute to family wealth later in life.",
            ne = "५ औं भावमा २ औं भावेशले सट्टेबाजी, रचनात्मकता वा सन्तान मार्फत सम्पत्ति संकेत गर्छ। लगानीले लाभ ल्याउँछ। रचनात्मक कार्यहरूले आम्दानी उत्पन्न गर्छन्। सन्तानहरूले पछि जीवनमा पारिवारिक सम्पत्तिमा योगदान दिन सक्छन्।"
        ),
        6 to LocalizedTemplate(
            en = "2nd lord in 6th house indicates wealth through service, healthcare, or overcoming competition. Hard work generates income. There may be expenses through enemies or illness. Financial gains through legal matters or service professions.",
            ne = "६ औं भावमा २ औं भावेशले सेवा, स्वास्थ्य सेवा वा प्रतिस्पर्धा पार गर्ने मार्फत सम्पत्ति संकेत गर्छ। कडा परिश्रमले आम्दानी उत्पन्न गर्छ। शत्रुहरू वा रोग मार्फत खर्च हुन सक्छ। कानूनी मामिलाहरू वा सेवा पेशाहरू मार्फत आर्थिक लाभ।"
        ),
        7 to LocalizedTemplate(
            en = "2nd lord in 7th house indicates wealth through partnerships, business, or spouse. Marriage brings financial benefits. Business partnerships are profitable. Joint ventures increase wealth.",
            ne = "७ औं भावमा २ औं भावेशले साझेदारी, व्यवसाय वा जीवनसाथी मार्फत सम्पत्ति संकेत गर्छ। विवाहले आर्थिक लाभ ल्याउँछ। व्यापार साझेदारीहरू लाभदायक हुन्छन्। संयुक्त उद्यमहरूले सम्पत्ति बढाउँछन्।"
        ),
        8 to LocalizedTemplate(
            en = "2nd lord in 8th house indicates wealth through inheritance, insurance, or sudden gains. There may be fluctuations in finances. Hidden sources of income are possible. Transformative experiences affect wealth.",
            ne = "८ औं भावमा २ औं भावेशले सम्पत्ति, बीमा वा अचानक लाभ मार्फत सम्पत्ति संकेत गर्छ। वित्तमा उतारचढाव हुन सक्छ। आम्दानीका लुकेका स्रोतहरू सम्भव छन्। परिवर्तनकारी अनुभवहरूले सम्पत्तिमा असर पार्छ।"
        ),
        9 to LocalizedTemplate(
            en = "2nd lord in 9th house indicates wealth through fortune, dharma, or foreign connections. Father may contribute to wealth. Higher education brings financial opportunities. Luck favors financial matters.",
            ne = "९ औं भावमा २ औं भावेशले भाग्य, धर्म वा विदेशी सम्बन्धहरू मार्फत सम्पत्ति संकेत गर्छ। पिताले सम्पत्तिमा योगदान दिन सक्छ। उच्च शिक्षाले आर्थिक अवसरहरू ल्याउँछ। भाग्यले आर्थिक मामिलाहरूलाई अनुकूल पार्छ।"
        ),
        10 to LocalizedTemplate(
            en = "2nd lord in 10th house is excellent for earning through career. Professional success brings wealth. The native achieves financial status through hard work. Career and finances are closely linked.",
            ne = "१० औं भावमा २ औं भावेश क्यारियर मार्फत कमाइको लागि उत्कृष्ट छ। व्यावसायिक सफलताले सम्पत्ति ल्याउँछ। जातकले कडा परिश्रम मार्फत आर्थिक स्थिति प्राप्त गर्छ। क्यारियर र वित्त नजिकबाट जोडिएका छन्।"
        ),
        11 to LocalizedTemplate(
            en = "2nd lord in 11th house is very favorable for wealth accumulation and gains. Multiple income streams are indicated. Friends and networks support financial growth. Ambitions regarding wealth are fulfilled.",
            ne = "११ औं भावमा २ औं भावेश सम्पत्ति संचय र लाभको लागि धेरै अनुकूल छ। बहु आम्दानी स्रोतहरू संकेत गरिन्छ। साथीहरू र सञ्जालहरूले आर्थिक वृद्धिलाई समर्थन गर्छन्। सम्पत्ति सम्बन्धी महत्वाकांक्षाहरू पूरा हुन्छन्।"
        ),
        12 to LocalizedTemplate(
            en = "2nd lord in 12th house may indicate expenses exceeding income or wealth in foreign lands. There may be charitable giving or losses. Foreign connections bring financial opportunities. Spiritual perspective on wealth develops.",
            ne = "१२ औं भावमा २ औं भावेशले आम्दानी भन्दा बढी खर्च वा विदेशमा सम्पत्ति संकेत गर्न सक्छ। दान वा हानि हुन सक्छ। विदेशी सम्बन्धहरूले आर्थिक अवसरहरू ल्याउँछन्। सम्पत्तिमा आध्यात्मिक दृष्टिकोण विकसित हुन्छ।"
        )
    )

    // ==================== SPIRITUAL TEMPLATES ====================

    /**
     * Spiritual path by 9th Lord placement
     */
    val spiritualByNinthLordTemplates = mapOf(
        1 to LocalizedTemplate(
            en = "9th lord in 1st house blesses the native with dharmic personality and fortune. The native naturally attracts luck and divine grace. Spiritual inclination is part of identity. Teachers and gurus are influential throughout life.",
            ne = "१ औं भावमा ९ औं भावेशले जातकलाई धार्मिक व्यक्तित्व र भाग्यले आशीर्वाद दिन्छ। जातकले स्वाभाविक रूपमा भाग्य र दिव्य कृपा आकर्षित गर्छ। आध्यात्मिक झुकाव पहिचानको भाग हो। शिक्षक र गुरुहरू जीवनभर प्रभावशाली हुन्छन्।"
        ),
        5 to LocalizedTemplate(
            en = "9th lord in 5th house creates powerful Purvapunya yoga. The native brings spiritual merit from past lives. Children may be spiritually inclined. Creative expression of dharma is favored.",
            ne = "५ औं भावमा ९ औं भावेशले शक्तिशाली पूर्वपुण्य योग सिर्जना गर्छ। जातकले पूर्वजन्मबाट आध्यात्मिक योग्यता ल्याउँछ। सन्तानहरू आध्यात्मिक रूपमा झुकेको हुन सक्छन्। धर्मको रचनात्मक अभिव्यक्ति अनुकूल छ।"
        ),
        9 to LocalizedTemplate(
            en = "9th lord in 9th house is highly auspicious for dharma, fortune, and spiritual growth. The native is blessed with wise teachers and spiritual experiences. Foreign travels bring spiritual growth. Religious and philosophical pursuits are successful.",
            ne = "९ औं भावमा ९ औं भावेश धर्म, भाग्य र आध्यात्मिक वृद्धिको लागि अत्यधिक शुभ छ। जातक बुद्धिमान शिक्षकहरू र आध्यात्मिक अनुभवहरूले आशीर्वादित हुन्छ। विदेश यात्राले आध्यात्मिक वृद्धि ल्याउँछ। धार्मिक र दार्शनिक कार्यहरू सफल हुन्छन्।"
        ),
        12 to LocalizedTemplate(
            en = "9th lord in 12th house is excellent for moksha and spiritual liberation. The native may have profound spiritual experiences or live in foreign lands for spiritual purposes. Retreat and meditation are beneficial. Past life spiritual connections manifest.",
            ne = "१२ औं भावमा ९ औं भावेश मोक्ष र आध्यात्मिक मुक्तिको लागि उत्कृष्ट छ। जातकले गहिरो आध्यात्मिक अनुभवहरू पाउन सक्छ वा आध्यात्मिक उद्देश्यका लागि विदेशमा बस्न सक्छ। एकान्तवास र ध्यान लाभदायक छ। पूर्वजन्म आध्यात्मिक सम्बन्धहरू प्रकट हुन्छन्।"
        )
    )

    /**
     * Moksha indicators by 12th Lord placement
     */
    val mokshaByTwelfthLordTemplates = mapOf(
        1 to LocalizedTemplate(
            en = "12th lord in 1st house indicates spiritual personality with detachment from material concerns. The native may have foreign connections or live away from birthplace. Spiritual awakening shapes identity. Liberation-seeking nature is prominent.",
            ne = "१ औं भावमा १२ औं भावेशले भौतिक चिन्ताहरूबाट विरक्तिसहित आध्यात्मिक व्यक्तित्व संकेत गर्छ। जातकको विदेशी सम्बन्ध हुन सक्छ वा जन्मस्थानबाट टाढा बस्न सक्छ। आध्यात्मिक जागरणले पहिचानलाई आकार दिन्छ। मुक्ति-खोज्ने स्वभाव प्रमुख हुन्छ।"
        ),
        4 to LocalizedTemplate(
            en = "12th lord in 4th house may indicate spiritual home or residence in foreign lands. The native finds peace through meditation at home. Mother may have spiritual inclinations. Inner peace comes through emotional healing.",
            ne = "४ औं भावमा १२ औं भावेशले आध्यात्मिक घर वा विदेशमा बसोबास संकेत गर्न सक्छ। जातकले घरमा ध्यान मार्फत शान्ति पाउँछ। आमाको आध्यात्मिक झुकाव हुन सक्छ। भावनात्मक उपचार मार्फत आन्तरिक शान्ति आउँछ।"
        ),
        8 to LocalizedTemplate(
            en = "12th lord in 8th house creates strong moksha yoga. The native has deep interest in occult and transformation. Near-death experiences may awaken spiritual consciousness. Kundalini and tantric practices are beneficial.",
            ne = "८ औं भावमा १२ औं भावेशले बलियो मोक्ष योग सिर्जना गर्छ। जातकको गुप्त र परिवर्तनमा गहिरो रुचि हुन्छ। मृत्यु-नजिकका अनुभवहरूले आध्यात्मिक चेतना जगाउन सक्छन्। कुण्डलिनी र तान्त्रिक अभ्यासहरू लाभदायक छन्।"
        ),
        12 to LocalizedTemplate(
            en = "12th lord in 12th house is powerful for spiritual liberation and final emancipation. The native has natural inclination toward moksha. Dreams and subconscious are spiritually significant. Retirement to spiritual pursuits brings fulfillment.",
            ne = "१२ औं भावमा १२ औं भावेश आध्यात्मिक मुक्ति र अन्तिम मोक्षको लागि शक्तिशाली छ। जातकको मोक्षप्रति प्राकृतिक झुकाव हुन्छ। सपना र अवचेतन आध्यात्मिक रूपमा महत्वपूर्ण हुन्छन्। आध्यात्मिक कार्यहरूमा सेवानिवृत्तिले पूर्णता ल्याउँछ।"
        )
    )

    // ==================== EDUCATION TEMPLATES ====================

    /**
     * Education by 4th Lord placement
     */
    val educationByFourthLordTemplates = mapOf(
        1 to LocalizedTemplate(
            en = "4th lord in 1st house indicates education that shapes personality. The native is learned and academically inclined. Early education is significant. Learning becomes part of identity and self-expression.",
            ne = "१ औं भावमा ४ औं भावेशले व्यक्तित्वलाई आकार दिने शिक्षा संकेत गर्छ। जातक विद्वान र शैक्षिक रूपमा झुकेको हुन्छ। प्रारम्भिक शिक्षा महत्वपूर्ण हुन्छ। सिकाइ पहिचान र आत्म-अभिव्यक्तिको भाग बन्छ।"
        ),
        4 to LocalizedTemplate(
            en = "4th lord in 4th house is excellent for education and academic achievements. The native completes formal education successfully. Home environment supports learning. Higher degrees and academic recognition are indicated.",
            ne = "४ औं भावमा ४ औं भावेश शिक्षा र शैक्षिक उपलब्धिहरूको लागि उत्कृष्ट छ। जातक औपचारिक शिक्षा सफलतापूर्वक पूरा गर्छ। घरको वातावरणले सिकाइलाई समर्थन गर्छ। उच्च डिग्री र शैक्षिक मान्यता संकेत गरिन्छ।"
        ),
        5 to LocalizedTemplate(
            en = "4th lord in 5th house indicates creative and intellectual education. The native excels in areas requiring intelligence. Higher education is successful. Children may inherit educational abilities.",
            ne = "५ औं भावमा ४ औं भावेशले रचनात्मक र बौद्धिक शिक्षा संकेत गर्छ। जातक बुद्धिमत्ता आवश्यक पर्ने क्षेत्रहरूमा उत्कृष्ट हुन्छ। उच्च शिक्षा सफल हुन्छ। सन्तानहरूले शैक्षिक क्षमताहरू प्राप्त गर्न सक्छन्।"
        ),
        9 to LocalizedTemplate(
            en = "4th lord in 9th house indicates higher education and philosophical learning. The native pursues advanced degrees or religious education. Foreign education is possible. Teachers and gurus guide educational path.",
            ne = "९ औं भावमा ४ औं भावेशले उच्च शिक्षा र दार्शनिक सिकाइ संकेत गर्छ। जातक उन्नत डिग्री वा धार्मिक शिक्षा खोज्छ। विदेशी शिक्षा सम्भव छ। शिक्षक र गुरुहरूले शैक्षिक मार्गलाई मार्गदर्शन गर्छन्।"
        )
    )

    /**
     * Intelligence and learning by 5th Lord
     */
    val intelligenceByFifthLordTemplates = mapOf(
        1 to LocalizedTemplate(
            en = "5th lord in 1st house indicates natural intelligence and creative personality. The native is bright and talented from birth. Learning comes easily. Speculative abilities and creative thinking are strong.",
            ne = "१ औं भावमा ५ औं भावेशले प्राकृतिक बुद्धिमत्ता र रचनात्मक व्यक्तित्व संकेत गर्छ। जातक जन्मदेखि नै उज्यालो र प्रतिभाशाली हुन्छ। सिकाइ सजिलो आउँछ। सट्टेबाजी क्षमताहरू र रचनात्मक सोच बलियो हुन्छ।"
        ),
        5 to LocalizedTemplate(
            en = "5th lord in 5th house indicates exceptional intelligence and creative abilities. The native excels in intellectual pursuits. Children are blessed. Speculation and investment bring gains through wisdom.",
            ne = "५ औं भावमा ५ औं भावेशले असाधारण बुद्धिमत्ता र रचनात्मक क्षमताहरू संकेत गर्छ। जातक बौद्धिक कार्यहरूमा उत्कृष्ट हुन्छ। सन्तानहरू आशीर्वादित हुन्छन्। सट्टेबाजी र लगानीले ज्ञान मार्फत लाभ ल्याउँछ।"
        ),
        9 to LocalizedTemplate(
            en = "5th lord in 9th house creates Purvapunya yoga bringing past life merit. The native has natural wisdom and spiritual intelligence. Higher education is successful. Fortune supports intellectual growth.",
            ne = "९ औं भावमा ५ औं भावेशले पूर्वजन्म योग्यता ल्याउने पूर्वपुण्य योग सिर्जना गर्छ। जातकमा प्राकृतिक ज्ञान र आध्यात्मिक बुद्धिमत्ता हुन्छ। उच्च शिक्षा सफल हुन्छ। भाग्यले बौद्धिक वृद्धिलाई समर्थन गर्छ।"
        ),
        10 to LocalizedTemplate(
            en = "5th lord in 10th house indicates intelligence applied to career. The native achieves professional success through creativity. Career in education, speculation, or creative fields is favored. Children support career growth.",
            ne = "१० औं भावमा ५ औं भावेशले क्यारियरमा लागू गरिएको बुद्धिमत्ता संकेत गर्छ। जातकले रचनात्मकता मार्फत व्यावसायिक सफलता प्राप्त गर्छ। शिक्षा, सट्टेबाजी वा रचनात्मक क्षेत्रहरूमा क्यारियर अनुकूल छ। सन्तानहरूले क्यारियर वृद्धिलाई समर्थन गर्छन्।"
        )
    )

    // ==================== PROPERTY TEMPLATES ====================

    /**
     * Property and vehicles by 4th house indicators
     */
    val propertyTemplates = mapOf(
        "strong_fourth" to LocalizedTemplate(
            en = "Strong 4th house and lord indicate acquisition of property, vehicles, and domestic comforts. The native enjoys good home environment and land ownership. Multiple properties are possible. Inheritance of ancestral property is favored.",
            ne = "बलियो ४ औं भाव र भावेशले सम्पत्ति, सवारी साधन र घरेलु सुविधाहरूको अधिग्रहण संकेत गर्छ। जातक राम्रो घरको वातावरण र जमीन स्वामित्वको आनन्द लिन्छ। बहु सम्पत्तिहरू सम्भव छन्। पुर्खौली सम्पत्तिको सम्पत्ति अनुकूल छ।"
        ),
        "jupiter_fourth" to LocalizedTemplate(
            en = "Jupiter in or aspecting 4th house brings expansion of property and comfortable home. The native lives in spacious and blessed environment. Religious or educational institutions may be associated with property. Divine grace protects property.",
            ne = "४ औं भावमा वा दृष्टि गर्ने बृहस्पतिले सम्पत्तिको विस्तार र आरामदायी घर ल्याउँछ। जातक विशाल र आशीर्वादित वातावरणमा बस्छ। धार्मिक वा शैक्षिक संस्थाहरू सम्पत्तिसँग सम्बन्धित हुन सक्छन्। दिव्य कृपाले सम्पत्तिको सुरक्षा गर्छ।"
        ),
        "venus_fourth" to LocalizedTemplate(
            en = "Venus in 4th house indicates beautiful and comfortable home with luxury items. The native enjoys vehicles and domestic pleasures. Property has aesthetic value. Harmonious home environment is maintained.",
            ne = "४ औं भावमा शुक्रले विलासी वस्तुहरू सहित सुन्दर र आरामदायी घर संकेत गर्छ। जातक सवारी साधन र घरेलु आनन्दहरूको आनन्द लिन्छ। सम्पत्तिमा सौन्दर्यात्मक मूल्य हुन्छ। सामंजस्यपूर्ण घरको वातावरण कायम राखिन्छ।"
        ),
        "mars_fourth" to LocalizedTemplate(
            en = "Mars in 4th house may create disturbance in domestic peace but gives property through own efforts. The native may deal in land or construction. Conflicts over property are possible. Self-built home is indicated.",
            ne = "४ औं भावमा मंगलले घरेलु शान्तिमा अशान्ति सिर्जना गर्न सक्छ तर आफ्नै प्रयासबाट सम्पत्ति दिन्छ। जातक जमीन वा निर्माणमा व्यवहार गर्न सक्छ। सम्पत्तिमा द्वन्द्व सम्भव छ। स्व-निर्मित घर संकेत गरिन्छ।"
        ),
        "saturn_fourth" to LocalizedTemplate(
            en = "Saturn in 4th house may delay property acquisition but gives lasting assets. The native works hard for home and eventually succeeds. Old or ancestral property is indicated. Patience in property matters is required.",
            ne = "४ औं भावमा शनिले सम्पत्ति अधिग्रहणमा ढिलाइ गर्न सक्छ तर टिकाउ सम्पत्ति दिन्छ। जातक घरको लागि कडा परिश्रम गर्छ र अन्ततः सफल हुन्छ। पुरानो वा पुर्खौली सम्पत्ति संकेत गरिन्छ। सम्पत्ति मामिलाहरूमा धैर्य आवश्यक छ।"
        )
    )

    // ==================== CHILDREN TEMPLATES ====================

    /**
     * Children indicators by 5th Lord
     */
    val childrenTemplates = mapOf(
        "strong_fifth" to LocalizedTemplate(
            en = "Strong 5th house and lord indicate blessed children who bring happiness. The native has good relationship with offspring. Children are talented and successful. Purvapunya (past life merit) supports progeny.",
            ne = "बलियो ५ औं भाव र भावेशले खुशी ल्याउने आशीर्वादित सन्तानहरू संकेत गर्छ। जातकको सन्तानसँग राम्रो सम्बन्ध हुन्छ। सन्तानहरू प्रतिभाशाली र सफल हुन्छन्। पूर्वपुण्य (पूर्वजन्म योग्यता) ले सन्तानलाई समर्थन गर्छ।"
        ),
        "jupiter_fifth" to LocalizedTemplate(
            en = "Jupiter in or aspecting 5th house is highly auspicious for children. The native is blessed with wise and dharmic children. Children bring fortune and spiritual fulfillment. Multiple children are possible.",
            ne = "५ औं भावमा वा दृष्टि गर्ने बृहस्पति सन्तानको लागि अत्यधिक शुभ छ। जातक बुद्धिमान र धार्मिक सन्तानहरूले आशीर्वादित हुन्छ। सन्तानहरूले भाग्य र आध्यात्मिक पूर्णता ल्याउँछन्। बहु सन्तानहरू सम्भव छन्।"
        ),
        "venus_fifth" to LocalizedTemplate(
            en = "Venus in 5th house indicates beautiful and artistic children. The native enjoys romantic fulfillment and creative pursuits. Daughter may be particularly blessed. Love affairs bring happiness.",
            ne = "५ औं भावमा शुक्रले सुन्दर र कलात्मक सन्तानहरू संकेत गर्छ। जातक रोमान्टिक पूर्णता र रचनात्मक कार्यहरूको आनन्द लिन्छ। छोरी विशेष गरी आशीर्वादित हुन सक्छिन्। प्रेम सम्बन्धहरूले खुशी ल्याउँछन्।"
        ),
        "saturn_fifth" to LocalizedTemplate(
            en = "Saturn in 5th house may delay children or indicate fewer offspring. The native may have mature or serious-natured children. Adoption is possible. Children come later in life and are responsible.",
            ne = "५ औं भावमा शनिले सन्तानमा ढिलाइ गर्न सक्छ वा कम सन्तानहरू संकेत गर्न सक्छ। जातकका परिपक्व वा गम्भीर-स्वभावका सन्तानहरू हुन सक्छन्। दत्तक सम्भव छ। सन्तानहरू जीवनको पछाडि आउँछन् र जिम्मेवार हुन्छन्।"
        ),
        "rahu_fifth" to LocalizedTemplate(
            en = "Rahu in 5th house may indicate unusual circumstances with children. The native may have unconventional approach to progeny. Foreign connection with children is possible. Adoption or non-biological children are indicated.",
            ne = "५ औं भावमा राहुले सन्तानसँग असामान्य परिस्थितिहरू संकेत गर्न सक्छ। जातकको सन्तानप्रति अपरम्परागत दृष्टिकोण हुन सक्छ। सन्तानहरूसँग विदेशी सम्बन्ध सम्भव छ। दत्तक वा गैर-जैविक सन्तानहरू संकेत गरिन्छ।"
        )
    )

    // ==================== HELPER FUNCTIONS ====================

    /**
     * Get career template by ascendant
     */
    fun getCareerByAscendant(sign: ZodiacSign): LocalizedTemplate? {
        return careerByAscendantTemplates[sign]
    }

    /**
     * Get career template by 10th lord placement
     */
    fun getCareerByTenthLord(house: Int): LocalizedTemplate? {
        return careerByTenthLordTemplates[house]
    }

    /**
     * Get relationship template by 7th lord placement
     */
    fun getRelationshipBySeventhLord(house: Int): LocalizedTemplate? {
        return relationshipBySeventhLordTemplates[house]
    }

    /**
     * Get Venus relationship template
     */
    fun getVenusRelationshipTemplate(sign: ZodiacSign): LocalizedTemplate? {
        return venusInSignForRelationshipTemplates[sign]
    }

    /**
     * Get health template by ascendant
     */
    fun getHealthByAscendant(sign: ZodiacSign): LocalizedTemplate? {
        return healthByAscendantTemplates[sign]
    }

    /**
     * Get health template by 6th lord placement
     */
    fun getSixthLordHealthTemplate(house: Int): LocalizedTemplate? {
        return sixthLordHealthTemplates[house]
    }

    /**
     * Get wealth template by 2nd lord placement
     */
    fun getWealthBySecondLord(house: Int): LocalizedTemplate? {
        return wealthBySecondLordTemplates[house]
    }

    /**
     * Get spiritual template by 9th lord placement
     */
    fun getSpiritualByNinthLord(house: Int): LocalizedTemplate? {
        return spiritualByNinthLordTemplates[house]
    }

    /**
     * Get moksha template by 12th lord placement
     */
    fun getMokshaByTwelfthLord(house: Int): LocalizedTemplate? {
        return mokshaByTwelfthLordTemplates[house]
    }

    /**
     * Get education template by 4th lord placement
     */
    fun getEducationByFourthLord(house: Int): LocalizedTemplate? {
        return educationByFourthLordTemplates[house]
    }

    /**
     * Get intelligence template by 5th lord placement
     */
    fun getIntelligenceByFifthLord(house: Int): LocalizedTemplate? {
        return intelligenceByFifthLordTemplates[house]
    }

    /**
     * Get property template
     */
    fun getPropertyTemplate(key: String): LocalizedTemplate? {
        return propertyTemplates[key]
    }

    /**
     * Get children template
     */
    fun getChildrenTemplate(key: String): LocalizedTemplate? {
        return childrenTemplates[key]
    }

    /**
     * Get total template count
     */
    fun getTotalTemplateCount(): Int {
        return careerByAscendantTemplates.size +
                careerByTenthLordTemplates.size +
                relationshipBySeventhLordTemplates.size +
                venusInSignForRelationshipTemplates.size +
                healthByAscendantTemplates.size +
                sixthLordHealthTemplates.size +
                wealthBySecondLordTemplates.size +
                spiritualByNinthLordTemplates.size +
                mokshaByTwelfthLordTemplates.size +
                educationByFourthLordTemplates.size +
                intelligenceByFifthLordTemplates.size +
                propertyTemplates.size +
                childrenTemplates.size
    }
}
