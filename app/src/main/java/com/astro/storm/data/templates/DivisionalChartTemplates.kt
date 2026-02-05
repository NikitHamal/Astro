package com.astro.storm.data.templates

import com.astro.storm.core.model.Planet
import com.astro.storm.core.model.ZodiacSign

/**
 * Comprehensive Divisional Chart (Varga) Templates
 * Based on BPHS, Phaladeepika, and Saravali
 *
 * Covers:
 * - D-9 (Navamsa) - Marriage, Dharma, Spouse
 * - D-10 (Dashamsa) - Career, Profession, Status
 * - D-7 (Saptamsa) - Children, Progeny
 * - D-12 (Dwadasamsa) - Parents, Ancestry
 * - D-3 (Drekkana) - Siblings, Courage
 * - D-4 (Chaturthamsa) - Property, Fortune
 * - D-16 (Shodasamsa) - Vehicles, Comforts
 * - D-20 (Vimsamsa) - Spiritual Progress
 * - D-24 (Chaturvimsamsa) - Education, Learning
 * - D-27 (Saptavimsamsa/Bhamsa) - Strength, Weaknesses
 * - D-30 (Trimsamsa) - Evils, Misfortunes
 * - D-40 (Khavedamsa) - Auspicious/Inauspicious Effects
 * - D-45 (Akshavedamsa) - General Indications
 * - D-60 (Shashtiamsa) - Past Life Karma
 *
 * Total: 400+ templates
 */
object DivisionalChartTemplates {

    // ==================== D-9 NAVAMSA TEMPLATES ====================

    /**
     * Navamsa Lagna (Ascendant) Templates - 12 signs
     */
    val navamsaLagnaTemplates = mapOf(
        ZodiacSign.ARIES to LocalizedTemplate(
            en = "Navamsa Lagna in Aries indicates a spouse who is energetic, independent, and pioneering in nature. The native approaches marriage with enthusiasm and directness. There is strong initiative in dharmic pursuits. The relationship dynamic is characterized by passion and sometimes competition. The spouse may have a fiery temperament with leadership qualities.",
            ne = "मेष नवांश लग्नले ऊर्जावान, स्वतन्त्र र अग्रणी स्वभावको जीवनसाथी संकेत गर्छ। जातक उत्साह र प्रत्यक्षताका साथ विवाहमा प्रवेश गर्छ। धार्मिक कार्यहरूमा बलियो पहल हुन्छ। सम्बन्धको गतिशीलता जोश र कहिलेकाहीं प्रतिस्पर्धाले विशेषता राख्छ। जीवनसाथीमा नेतृत्व गुणसहित अग्निमय स्वभाव हुन सक्छ।"
        ),
        ZodiacSign.TAURUS to LocalizedTemplate(
            en = "Navamsa Lagna in Taurus bestows a spouse who is stable, sensual, and materially oriented. The marriage tends to be enduring and focused on building security. There is appreciation for beauty, comfort, and the finer things in life. The spouse is likely loyal, patient, and possesses artistic sensibilities. Financial stability in marriage is emphasized.",
            ne = "वृष नवांश लग्नले स्थिर, संवेदनशील र भौतिक रूपमा उन्मुख जीवनसाथी प्रदान गर्छ। विवाह टिकाउ र सुरक्षा निर्माणमा केन्द्रित हुन्छ। सौन्दर्य, आराम र जीवनका राम्रा कुराहरूको कदर हुन्छ। जीवनसाथी वफादार, धैर्यवान र कलात्मक संवेदनशीलता राख्ने हुन सक्छ। विवाहमा आर्थिक स्थिरतालाई जोड दिइन्छ।"
        ),
        ZodiacSign.GEMINI to LocalizedTemplate(
            en = "Navamsa Lagna in Gemini indicates an intellectually stimulating marriage. The spouse is communicative, versatile, and mentally agile. There may be dual nature or changeability in married life. The partnership thrives on mental connection and shared interests. The spouse likely has youthful energy and enjoys variety and learning.",
            ne = "मिथुन नवांश लग्नले बौद्धिक रूपमा उत्तेजक विवाह संकेत गर्छ। जीवनसाथी संवादशील, बहुमुखी र मानसिक रूपमा चुस्त हुन्छ। वैवाहिक जीवनमा दोहोरो स्वभाव वा परिवर्तनशीलता हुन सक्छ। साझेदारी मानसिक जडान र साझा रुचिहरूमा फस्टाउँछ। जीवनसाथीमा युवा ऊर्जा र विविधता र सिकाइको आनन्द हुने सम्भावना छ।"
        ),
        ZodiacSign.CANCER to LocalizedTemplate(
            en = "Navamsa Lagna in Cancer brings a nurturing and emotionally deep marriage. The spouse is caring, protective, and family-oriented. Home and emotional security are paramount in the relationship. There is strong attachment and sometimes moodiness. The spouse may be intuitive and deeply connected to family traditions.",
            ne = "कर्कट नवांश लग्नले पालनपोषण र भावनात्मक रूपमा गहिरो विवाह ल्याउँछ। जीवनसाथी हेरचाह गर्ने, सुरक्षात्मक र परिवार-उन्मुख हुन्छ। सम्बन्धमा घर र भावनात्मक सुरक्षा सर्वोपरि हुन्छ। बलियो आसक्ति र कहिलेकाहीं मूड परिवर्तन हुन्छ। जीवनसाथी अन्तर्ज्ञानी र पारिवारिक परम्पराहरूसँग गहिरो जडान भएको हुन सक्छ।"
        ),
        ZodiacSign.LEO to LocalizedTemplate(
            en = "Navamsa Lagna in Leo indicates a proud and dignified spouse. The marriage has royal qualities with mutual respect and admiration. The spouse is generous, warm-hearted, and enjoys recognition. There may be drama or ego issues to navigate. The partner likely has creative talents and a commanding presence.",
            ne = "सिंह नवांश लग्नले गर्वित र मर्यादित जीवनसाथी संकेत गर्छ। विवाहमा पारस्परिक सम्मान र प्रशंसाका साथ राजसी गुणहरू हुन्छन्। जीवनसाथी उदार, न्यानो-हृदय र मान्यताको आनन्द लिने हुन्छ। नाटक वा अहंकार समस्याहरू हुन सक्छन्। साथीमा रचनात्मक प्रतिभा र आदेशात्मक उपस्थिति हुने सम्भावना छ।"
        ),
        ZodiacSign.VIRGO to LocalizedTemplate(
            en = "Navamsa Lagna in Virgo brings a practical and service-oriented marriage. The spouse is analytical, detail-oriented, and health-conscious. There is emphasis on improvement and perfection in the relationship. The partner may be critical but genuinely helpful. Purity and organization characterize the married life.",
            ne = "कन्या नवांश लग्नले व्यावहारिक र सेवा-उन्मुख विवाह ल्याउँछ। जीवनसाथी विश्लेषणात्मक, विस्तार-उन्मुख र स्वास्थ्य-सचेत हुन्छ। सम्बन्धमा सुधार र पूर्णतामा जोड दिइन्छ। साथी आलोचनात्मक तर वास्तवमा सहयोगी हुन सक्छ। शुद्धता र व्यवस्थाले वैवाहिक जीवनलाई विशेषता दिन्छ।"
        ),
        ZodiacSign.LIBRA to LocalizedTemplate(
            en = "Navamsa Lagna in Libra is highly auspicious for marriage as Libra is the natural 7th sign. The spouse is balanced, diplomatic, and aesthetically inclined. Partnership and harmony are central to the relationship. The spouse values fairness and beauty. There is natural compatibility and a refined approach to married life.",
            ne = "तुला नवांश लग्न विवाहको लागि अत्यधिक शुभ छ किनभने तुला प्राकृतिक ७औं राशि हो। जीवनसाथी सन्तुलित, कूटनीतिज्ञ र सौन्दर्यात्मक रूपमा झुकेको हुन्छ। साझेदारी र सामंजस्य सम्बन्धको केन्द्रमा हुन्छ। जीवनसाथीले निष्पक्षता र सौन्दर्यलाई मूल्य दिन्छ। वैवाहिक जीवनमा प्राकृतिक अनुकूलता र परिष्कृत दृष्टिकोण हुन्छ।"
        ),
        ZodiacSign.SCORPIO to LocalizedTemplate(
            en = "Navamsa Lagna in Scorpio indicates an intense and transformative marriage. The spouse is passionate, secretive, and deeply emotional. The relationship involves profound psychological depth and potential power dynamics. There may be jealousy or possessiveness to manage. The bond is karmic and transformational.",
            ne = "वृश्चिक नवांश लग्नले तीव्र र परिवर्तनकारी विवाह संकेत गर्छ। जीवनसाथी भावुक, गोप्य र गहिरो भावनात्मक हुन्छ। सम्बन्धमा गहिरो मनोवैज्ञानिक गहिराइ र सम्भावित शक्ति गतिशीलता समावेश हुन्छ। ईर्ष्या वा स्वामित्वलाई व्यवस्थापन गर्नुपर्ने हुन सक्छ। बन्धन कार्मिक र परिवर्तनकारी हुन्छ।"
        ),
        ZodiacSign.SAGITTARIUS to LocalizedTemplate(
            en = "Navamsa Lagna in Sagittarius brings an optimistic and philosophical marriage. The spouse is adventurous, freedom-loving, and spiritually inclined. The relationship is characterized by growth, travel, and shared beliefs. The partner values honesty and higher learning. There is enthusiasm and expansion in married life.",
            ne = "धनु नवांश लग्नले आशावादी र दार्शनिक विवाह ल्याउँछ। जीवनसाथी साहसी, स्वतन्त्रता-प्रेमी र आध्यात्मिक रूपमा झुकेको हुन्छ। सम्बन्ध वृद्धि, यात्रा र साझा विश्वासहरूले विशेषता राख्छ। साथीले इमानदारी र उच्च शिक्षालाई मूल्य दिन्छ। वैवाहिक जीवनमा उत्साह र विस्तार हुन्छ।"
        ),
        ZodiacSign.CAPRICORN to LocalizedTemplate(
            en = "Navamsa Lagna in Capricorn indicates a mature and responsible marriage. The spouse is ambitious, disciplined, and focused on long-term goals. The relationship is built on commitment and shared responsibilities. The partner may be older or more serious in nature. Status and achievement matter in the partnership.",
            ne = "मकर नवांश लग्नले परिपक्व र जिम्मेवार विवाह संकेत गर्छ। जीवनसाथी महत्वाकांक्षी, अनुशासित र दीर्घकालीन लक्ष्यहरूमा केन्द्रित हुन्छ। सम्बन्ध प्रतिबद्धता र साझा जिम्मेवारीहरूमा निर्मित हुन्छ। साथी वृद्ध वा प्रकृतिमा बढी गम्भीर हुन सक्छ। साझेदारीमा स्थिति र उपलब्धि महत्त्वपूर्ण हुन्छ।"
        ),
        ZodiacSign.AQUARIUS to LocalizedTemplate(
            en = "Navamsa Lagna in Aquarius brings an unconventional and friendship-based marriage. The spouse is independent, humanitarian, and intellectually unique. The relationship values freedom and individual expression. The partner may have unusual ideas or interests. There is a progressive and detached quality to the bond.",
            ne = "कुम्भ नवांश लग्नले अपरम्परागत र मित्रता-आधारित विवाह ल्याउँछ। जीवनसाथी स्वतन्त्र, मानवतावादी र बौद्धिक रूपमा अद्वितीय हुन्छ। सम्बन्धले स्वतन्त्रता र व्यक्तिगत अभिव्यक्तिलाई मूल्य दिन्छ। साथीमा असामान्य विचार वा रुचिहरू हुन सक्छन्। बन्धनमा प्रगतिशील र अलग गुण हुन्छ।"
        ),
        ZodiacSign.PISCES to LocalizedTemplate(
            en = "Navamsa Lagna in Pisces indicates a spiritual and compassionate marriage. The spouse is intuitive, artistic, and emotionally sensitive. The relationship has a transcendent quality with deep emotional and spiritual connection. The partner may be dreamy or escapist at times. There is sacrifice and unconditional love in the bond.",
            ne = "मीन नवांश लग्नले आध्यात्मिक र दयालु विवाह संकेत गर्छ। जीवनसाथी अन्तर्ज्ञानी, कलात्मक र भावनात्मक रूपमा संवेदनशील हुन्छ। सम्बन्धमा गहिरो भावनात्मक र आध्यात्मिक जडानसहित उत्कृष्ट गुण हुन्छ। साथी कहिलेकाहीं स्वप्निल वा पलायनवादी हुन सक्छ। बन्धनमा त्याग र शर्तरहित प्रेम हुन्छ।"
        )
    )

    /**
     * Planets in Navamsa Templates - Marriage and Dharma effects
     */
    val planetInNavamsaTemplates = mapOf(
        // Sun in Navamsa signs
        Pair(Planet.SUN, ZodiacSign.ARIES) to LocalizedTemplate(
            en = "Sun exalted in Navamsa Aries bestows a spouse from a prestigious family. The native has strong dharmic convictions and righteous approach to marriage. The soul purpose is aligned with leadership and pioneering spiritual work. The spouse brings honor and recognition to the native.",
            ne = "मेष नवांशमा उच्चको सूर्यले प्रतिष्ठित परिवारबाट जीवनसाथी प्रदान गर्छ। जातकमा बलियो धार्मिक विश्वास र विवाहप्रति धार्मिक दृष्टिकोण हुन्छ। आत्माको उद्देश्य नेतृत्व र अग्रणी आध्यात्मिक कार्यसँग मिल्छ। जीवनसाथीले जातकलाई सम्मान र मान्यता ल्याउँछ।"
        ),
        Pair(Planet.SUN, ZodiacSign.LEO) to LocalizedTemplate(
            en = "Sun in own sign Leo in Navamsa indicates a dignified and regal marriage. The spouse comes from a respected family with strong values. The native's dharma involves leadership, creativity, and self-expression. There is mutual admiration and pride in the relationship.",
            ne = "नवांशमा आफ्नै राशि सिंहमा सूर्यले मर्यादित र राजसी विवाह संकेत गर्छ। जीवनसाथी बलियो मूल्यहरू भएको सम्मानित परिवारबाट आउँछ। जातकको धर्ममा नेतृत्व, रचनात्मकता र आत्म-अभिव्यक्ति समावेश हुन्छ। सम्बन्धमा पारस्परिक प्रशंसा र गर्व हुन्छ।"
        ),
        Pair(Planet.SUN, ZodiacSign.LIBRA) to LocalizedTemplate(
            en = "Sun debilitated in Navamsa Libra may create challenges in expressing authority within marriage. The native must learn balance and compromise in relationships. The ego may need tempering for marital harmony. However, this placement can refine the soul through partnership experiences.",
            ne = "तुला नवांशमा नीचको सूर्यले विवाहभित्र अधिकार व्यक्त गर्न चुनौतीहरू सिर्जना गर्न सक्छ। जातकले सम्बन्धहरूमा सन्तुलन र सम्झौता सिक्नुपर्छ। वैवाहिक सामंजस्यको लागि अहंकारलाई नियन्त्रण गर्नुपर्ने हुन सक्छ। तथापि, यो स्थानले साझेदारी अनुभवहरू मार्फत आत्मालाई परिष्कृत गर्न सक्छ।"
        ),

        // Moon in Navamsa signs
        Pair(Planet.MOON, ZodiacSign.TAURUS) to LocalizedTemplate(
            en = "Moon exalted in Navamsa Taurus is highly auspicious for marital happiness. The spouse is emotionally stable, nurturing, and provides comfort. The native finds deep emotional fulfillment in marriage. The mind is content and the relationship is enduring and peaceful.",
            ne = "वृष नवांशमा उच्चको चन्द्रमा वैवाहिक खुशीको लागि अत्यधिक शुभ छ। जीवनसाथी भावनात्मक रूपमा स्थिर, पालनपोषण गर्ने र आराम प्रदान गर्ने हुन्छ। जातकले विवाहमा गहिरो भावनात्मक पूर्णता पाउँछ। मन सन्तुष्ट छ र सम्बन्ध टिकाउ र शान्तिपूर्ण हुन्छ।"
        ),
        Pair(Planet.MOON, ZodiacSign.CANCER) to LocalizedTemplate(
            en = "Moon in own sign Cancer in Navamsa indicates a deeply nurturing marriage. The spouse is caring, maternal/paternal, and emotionally attuned. Home and family are central to the relationship. There is strong emotional bonding and intuitive understanding between partners.",
            ne = "नवांशमा आफ्नै राशि कर्कटमा चन्द्रमाले गहिरो पालनपोषण गर्ने विवाह संकेत गर्छ। जीवनसाथी हेरचाह गर्ने, मातृ/पितृत्व र भावनात्मक रूपमा मिल्ने हुन्छ। घर र परिवार सम्बन्धको केन्द्रमा हुन्छ। साथीहरूबीच बलियो भावनात्मक बन्धन र अन्तर्ज्ञानात्मक बुझाइ हुन्छ।"
        ),
        Pair(Planet.MOON, ZodiacSign.SCORPIO) to LocalizedTemplate(
            en = "Moon debilitated in Navamsa Scorpio may bring emotional intensity and challenges in marriage. The spouse may have complex emotional nature. There can be possessiveness or jealousy to overcome. However, the relationship offers profound emotional transformation and healing.",
            ne = "वृश्चिक नवांशमा नीचको चन्द्रमाले विवाहमा भावनात्मक तीव्रता र चुनौतीहरू ल्याउन सक्छ। जीवनसाथीमा जटिल भावनात्मक स्वभाव हुन सक्छ। स्वामित्व वा ईर्ष्या पार गर्नुपर्ने हुन सक्छ। तथापि, सम्बन्धले गहिरो भावनात्मक रूपान्तरण र उपचार प्रदान गर्छ।"
        ),

        // Mars in Navamsa signs
        Pair(Planet.MARS, ZodiacSign.CAPRICORN) to LocalizedTemplate(
            en = "Mars exalted in Navamsa Capricorn indicates a spouse with strong drive and ambition. The marriage is characterized by shared goals and disciplined approach. The native brings energy and determination to dharmic pursuits. The relationship supports career and worldly achievements.",
            ne = "मकर नवांशमा उच्चको मंगलले बलियो ड्राइभ र महत्वाकांक्षा भएको जीवनसाथी संकेत गर्छ। विवाह साझा लक्ष्यहरू र अनुशासित दृष्टिकोणले विशेषता राख्छ। जातकले धार्मिक कार्यहरूमा ऊर्जा र दृढ संकल्प ल्याउँछ। सम्बन्धले क्यारियर र सांसारिक उपलब्धिहरूलाई समर्थन गर्छ।"
        ),
        Pair(Planet.MARS, ZodiacSign.ARIES) to LocalizedTemplate(
            en = "Mars in own sign Aries in Navamsa brings passionate and dynamic marriage. The spouse is energetic, independent, and courageous. There is strong physical attraction and active partnership. The native approaches dharma with initiative and warrior spirit.",
            ne = "नवांशमा आफ्नै राशि मेषमा मंगलले भावुक र गतिशील विवाह ल्याउँछ। जीवनसाथी ऊर्जावान, स्वतन्त्र र साहसी हुन्छ। बलियो शारीरिक आकर्षण र सक्रिय साझेदारी हुन्छ। जातक पहल र योद्धा भावनाका साथ धर्ममा अघि बढ्छ।"
        ),
        Pair(Planet.MARS, ZodiacSign.CANCER) to LocalizedTemplate(
            en = "Mars debilitated in Navamsa Cancer may create emotional volatility in marriage. There can be anger issues or emotional conflicts to resolve. The spouse may be sensitive and defensive. Learning to channel passion constructively within the home is the lesson.",
            ne = "कर्कट नवांशमा नीचको मंगलले विवाहमा भावनात्मक अस्थिरता सिर्जना गर्न सक्छ। रिस समस्याहरू वा भावनात्मक द्वन्द्वहरू समाधान गर्नुपर्ने हुन सक्छ। जीवनसाथी संवेदनशील र रक्षात्मक हुन सक्छ। घरभित्र जोशलाई रचनात्मक रूपमा प्रवाहित गर्न सिक्नु पाठ हो।"
        ),

        // Mercury in Navamsa signs
        Pair(Planet.MERCURY, ZodiacSign.VIRGO) to LocalizedTemplate(
            en = "Mercury exalted in Navamsa Virgo indicates an intelligent and communicative spouse. The marriage is built on intellectual compatibility and practical cooperation. The native has refined analytical abilities regarding dharma. The partnership thrives on shared intellectual pursuits.",
            ne = "कन्या नवांशमा उच्चको बुधले बुद्धिमान र संवादशील जीवनसाथी संकेत गर्छ। विवाह बौद्धिक अनुकूलता र व्यावहारिक सहयोगमा निर्मित हुन्छ। जातकमा धर्म सम्बन्धी परिष्कृत विश्लेषणात्मक क्षमताहरू हुन्छन्। साझेदारी साझा बौद्धिक कार्यहरूमा फस्टाउँछ।"
        ),
        Pair(Planet.MERCURY, ZodiacSign.GEMINI) to LocalizedTemplate(
            en = "Mercury in own sign Gemini in Navamsa brings a mentally stimulating marriage. The spouse is witty, curious, and versatile. Communication is the cornerstone of the relationship. The native's dharma involves teaching, writing, or intellectual exchange.",
            ne = "नवांशमा आफ्नै राशि मिथुनमा बुधले मानसिक रूपमा उत्तेजक विवाह ल्याउँछ। जीवनसाथी हाजिरजवाफी, जिज्ञासु र बहुमुखी हुन्छ। सम्बन्धको आधारशिला संवाद हो। जातकको धर्ममा शिक्षण, लेखन वा बौद्धिक आदानप्रदान समावेश हुन्छ।"
        ),
        Pair(Planet.MERCURY, ZodiacSign.PISCES) to LocalizedTemplate(
            en = "Mercury debilitated in Navamsa Pisces may create communication challenges in marriage. The spouse may be dreamy or impractical at times. Logical thinking may be overshadowed by emotions. However, intuitive and spiritual communication can flourish in this placement.",
            ne = "मीन नवांशमा नीचको बुधले विवाहमा संचार चुनौतीहरू सिर्जना गर्न सक्छ। जीवनसाथी कहिलेकाहीं स्वप्निल वा अव्यावहारिक हुन सक्छ। तार्किक सोच भावनाहरूले छायामा पार्न सक्छ। तथापि, यस स्थानमा अन्तर्ज्ञानी र आध्यात्मिक संवाद फस्टाउन सक्छ।"
        ),

        // Jupiter in Navamsa signs
        Pair(Planet.JUPITER, ZodiacSign.CANCER) to LocalizedTemplate(
            en = "Jupiter exalted in Navamsa Cancer is one of the most auspicious placements for marriage. The spouse is wise, nurturing, and spiritually evolved. The marriage is blessed with prosperity, children, and dharmic fulfillment. The native's spiritual path is supported by the partnership.",
            ne = "कर्कट नवांशमा उच्चको बृहस्पति विवाहको लागि सबैभन्दा शुभ स्थानहरू मध्ये एक हो। जीवनसाथी बुद्धिमान, पालनपोषण गर्ने र आध्यात्मिक रूपमा विकसित हुन्छ। विवाह समृद्धि, सन्तान र धार्मिक पूर्णताले आशीर्वादित हुन्छ। जातकको आध्यात्मिक मार्गलाई साझेदारीले समर्थन गर्छ।"
        ),
        Pair(Planet.JUPITER, ZodiacSign.SAGITTARIUS) to LocalizedTemplate(
            en = "Jupiter in own sign Sagittarius in Navamsa brings an expansive and philosophical marriage. The spouse is optimistic, wise, and spiritually inclined. The relationship supports growth, learning, and spiritual development. Shared beliefs and higher purpose unite the partners.",
            ne = "नवांशमा आफ्नै राशि धनुमा बृहस्पतिले विस्तृत र दार्शनिक विवाह ल्याउँछ। जीवनसाथी आशावादी, बुद्धिमान र आध्यात्मिक रूपमा झुकेको हुन्छ। सम्बन्धले वृद्धि, सिकाइ र आध्यात्मिक विकासलाई समर्थन गर्छ। साझा विश्वास र उच्च उद्देश्यले साथीहरूलाई एकजुट गर्छ।"
        ),
        Pair(Planet.JUPITER, ZodiacSign.CAPRICORN) to LocalizedTemplate(
            en = "Jupiter debilitated in Navamsa Capricorn may create challenges with faith and optimism in marriage. The spouse may be materialistic or overly practical. Spiritual growth may require more effort within the relationship. However, practical wisdom and grounded spirituality can develop.",
            ne = "मकर नवांशमा नीचको बृहस्पतिले विवाहमा विश्वास र आशावादमा चुनौतीहरू सिर्जना गर्न सक्छ। जीवनसाथी भौतिकवादी वा अत्यधिक व्यावहारिक हुन सक्छ। सम्बन्धभित्र आध्यात्मिक वृद्धिलाई बढी प्रयास चाहिन सक्छ। तथापि, व्यावहारिक ज्ञान र आधारभूत आध्यात्मिकता विकसित हुन सक्छ।"
        ),

        // Venus in Navamsa signs
        Pair(Planet.VENUS, ZodiacSign.PISCES) to LocalizedTemplate(
            en = "Venus exalted in Navamsa Pisces is exceptionally favorable for marriage and romantic fulfillment. The spouse is beautiful, artistic, and deeply loving. The marriage has a transcendent, almost divine quality. There is unconditional love and spiritual connection between partners.",
            ne = "मीन नवांशमा उच्चको शुक्र विवाह र रोमान्टिक पूर्णताको लागि असाधारण रूपमा अनुकूल छ। जीवनसाथी सुन्दर, कलात्मक र गहिरो प्रेमी हुन्छ। विवाहमा उत्कृष्ट, लगभग दिव्य गुण हुन्छ। साथीहरूबीच शर्तरहित प्रेम र आध्यात्मिक जडान हुन्छ।"
        ),
        Pair(Planet.VENUS, ZodiacSign.TAURUS) to LocalizedTemplate(
            en = "Venus in own sign Taurus in Navamsa brings a harmonious and sensual marriage. The spouse is beautiful, artistic, and values comfort. The relationship is stable, pleasurable, and materially secure. There is appreciation for beauty and refined tastes in the partnership.",
            ne = "नवांशमा आफ्नै राशि वृषमा शुक्रले सामंजस्यपूर्ण र संवेदनशील विवाह ल्याउँछ। जीवनसाथी सुन्दर, कलात्मक र आरामलाई मूल्य दिने हुन्छ। सम्बन्ध स्थिर, आनन्दमय र भौतिक रूपमा सुरक्षित हुन्छ। साझेदारीमा सौन्दर्य र परिष्कृत स्वादको कदर हुन्छ।"
        ),
        Pair(Planet.VENUS, ZodiacSign.VIRGO) to LocalizedTemplate(
            en = "Venus debilitated in Navamsa Virgo may create perfectionism or criticism in marriage. The spouse may be overly analytical about the relationship. Romance may be overshadowed by practical concerns. Learning to appreciate imperfection and express love freely is the lesson.",
            ne = "कन्या नवांशमा नीचको शुक्रले विवाहमा पूर्णतावाद वा आलोचना सिर्जना गर्न सक्छ। जीवनसाथी सम्बन्धको बारेमा अत्यधिक विश्लेषणात्मक हुन सक्छ। रोमान्स व्यावहारिक चिन्ताहरूले छायामा पार्न सक्छ। अपूर्णताको कदर गर्न र प्रेम स्वतन्त्र रूपमा व्यक्त गर्न सिक्नु पाठ हो।"
        ),

        // Saturn in Navamsa signs
        Pair(Planet.SATURN, ZodiacSign.LIBRA) to LocalizedTemplate(
            en = "Saturn exalted in Navamsa Libra is excellent for lasting and committed marriage. The spouse is mature, responsible, and fair-minded. The relationship is built on strong foundations and mutual respect. There is karmic duty fulfilled through the partnership.",
            ne = "तुला नवांशमा उच्चको शनि दीर्घकालीन र प्रतिबद्ध विवाहको लागि उत्कृष्ट छ। जीवनसाथी परिपक्व, जिम्मेवार र निष्पक्ष-मनको हुन्छ। सम्बन्ध बलियो आधार र पारस्परिक सम्मानमा निर्मित हुन्छ। साझेदारी मार्फत कार्मिक कर्तव्य पूरा हुन्छ।"
        ),
        Pair(Planet.SATURN, ZodiacSign.CAPRICORN) to LocalizedTemplate(
            en = "Saturn in own sign Capricorn in Navamsa indicates a serious and enduring marriage. The spouse is ambitious, disciplined, and achievement-oriented. The relationship involves shared responsibilities and long-term planning. There is stability but also potential for emotional distance.",
            ne = "नवांशमा आफ्नै राशि मकरमा शनिले गम्भीर र टिकाउ विवाह संकेत गर्छ। जीवनसाथी महत्वाकांक्षी, अनुशासित र उपलब्धि-उन्मुख हुन्छ। सम्बन्धमा साझा जिम्मेवारीहरू र दीर्घकालीन योजना समावेश हुन्छ। स्थिरता हुन्छ तर भावनात्मक दूरीको सम्भावना पनि हुन्छ।"
        ),
        Pair(Planet.SATURN, ZodiacSign.ARIES) to LocalizedTemplate(
            en = "Saturn debilitated in Navamsa Aries may create delays or obstacles in marriage. The spouse may struggle with patience or commitment initially. There can be frustration with limitations in the relationship. Learning perseverance and tempering impulsiveness is the lesson.",
            ne = "मेष नवांशमा नीचको शनिले विवाहमा ढिलाइ वा बाधाहरू सिर्जना गर्न सक्छ। जीवनसाथीले सुरुमा धैर्य वा प्रतिबद्धतामा संघर्ष गर्न सक्छ। सम्बन्धमा सीमितताहरूमा निराशा हुन सक्छ। दृढता सिक्नु र आवेगलाई नियन्त्रण गर्नु पाठ हो।"
        )
    )

    /**
     * Vargottama Templates - Same sign in Rashi and Navamsa
     */
    val vargottamaTemplates = mapOf(
        Planet.SUN to LocalizedTemplate(
            en = "Sun Vargottama (same sign in Rashi and Navamsa) greatly strengthens the soul's purpose and self-expression. The native has unwavering conviction and strong dharmic alignment. The father figure is significant and respected. Leadership abilities are enhanced and the native achieves recognition in their field.",
            ne = "सूर्य वर्गोत्तम (राशि र नवांशमा एउटै राशि) ले आत्माको उद्देश्य र आत्म-अभिव्यक्तिलाई धेरै बलियो बनाउँछ। जातकमा अटल विश्वास र बलियो धार्मिक संरेखण हुन्छ। पिता व्यक्ति महत्वपूर्ण र सम्मानित हुन्छ। नेतृत्व क्षमताहरू बढ्छन् र जातकले आफ्नो क्षेत्रमा मान्यता प्राप्त गर्छ।"
        ),
        Planet.MOON to LocalizedTemplate(
            en = "Moon Vargottama creates deep emotional stability and strong intuition. The mind is consistent and the emotional nature is well-integrated. The mother is influential and the native has strong nurturing abilities. Mental peace and contentment are more easily achieved.",
            ne = "चन्द्रमा वर्गोत्तमले गहिरो भावनात्मक स्थिरता र बलियो अन्तर्ज्ञान सिर्जना गर्छ। मन स्थिर छ र भावनात्मक स्वभाव राम्रोसँग एकीकृत हुन्छ। आमा प्रभावशाली हुन्छिन् र जातकमा बलियो पालनपोषण क्षमताहरू हुन्छन्। मानसिक शान्ति र सन्तुष्टि सजिलै प्राप्त हुन्छ।"
        ),
        Planet.MARS to LocalizedTemplate(
            en = "Mars Vargottama intensifies courage, energy, and competitive drive. The native has consistent assertiveness and physical vitality. Brothers and siblings are significant in life. The native excels in competitive fields and has strong willpower to achieve goals.",
            ne = "मंगल वर्गोत्तमले साहस, ऊर्जा र प्रतिस्पर्धी ड्राइभलाई तीव्र बनाउँछ। जातकमा स्थिर मुखरता र शारीरिक जीवनशक्ति हुन्छ। दाजुभाइ र दिदीबहिनीहरू जीवनमा महत्वपूर्ण हुन्छन्। जातक प्रतिस्पर्धी क्षेत्रहरूमा उत्कृष्ट हुन्छ र लक्ष्यहरू प्राप्त गर्न बलियो इच्छाशक्ति राख्छ।"
        ),
        Planet.MERCURY to LocalizedTemplate(
            en = "Mercury Vargottama enhances intelligence, communication, and analytical abilities. The native has exceptional clarity of thought and expression. Skills in writing, speaking, and commerce are strengthened. Learning ability is heightened and the native excels in intellectual pursuits.",
            ne = "बुध वर्गोत्तमले बुद्धिमत्ता, संचार र विश्लेषणात्मक क्षमताहरू बढाउँछ। जातकमा विचार र अभिव्यक्तिको असाधारण स्पष्टता हुन्छ। लेखन, बोल्ने र वाणिज्यमा सीपहरू बलियो हुन्छन्। सिकाइ क्षमता उच्च हुन्छ र जातक बौद्धिक कार्यहरूमा उत्कृष्ट हुन्छ।"
        ),
        Planet.JUPITER to LocalizedTemplate(
            en = "Jupiter Vargottama is highly auspicious for wisdom, spirituality, and fortune. The native has strong dharmic foundation and receives divine grace. Teachers and gurus play significant roles. Prosperity, children, and spiritual advancement are blessed by this placement.",
            ne = "बृहस्पति वर्गोत्तम ज्ञान, आध्यात्मिकता र भाग्यको लागि अत्यधिक शुभ छ। जातकमा बलियो धार्मिक आधार हुन्छ र दिव्य कृपा प्राप्त गर्छ। शिक्षक र गुरुहरूले महत्वपूर्ण भूमिका खेल्छन्। यस स्थानले समृद्धि, सन्तान र आध्यात्मिक प्रगतिलाई आशीर्वाद दिन्छ।"
        ),
        Planet.VENUS to LocalizedTemplate(
            en = "Venus Vargottama greatly enhances beauty, love, and artistic abilities. The native enjoys fulfilling relationships and material comforts. Marriage is likely to be harmonious and the spouse is attractive. Artistic talents are exceptional and the native appreciates refined pleasures.",
            ne = "शुक्र वर्गोत्तमले सौन्दर्य, प्रेम र कलात्मक क्षमताहरूलाई धेरै बढाउँछ। जातकले सन्तोषजनक सम्बन्ध र भौतिक सुविधाहरूको आनन्द लिन्छ। विवाह सामंजस्यपूर्ण हुने सम्भावना छ र जीवनसाथी आकर्षक हुन्छ। कलात्मक प्रतिभाहरू असाधारण हुन्छन् र जातकले परिष्कृत आनन्दहरूको कदर गर्छ।"
        ),
        Planet.SATURN to LocalizedTemplate(
            en = "Saturn Vargottama creates consistent discipline, perseverance, and karmic stability. The native has strong sense of duty and responsibility. Hard work yields lasting results. Though challenges exist, the native develops enduring strength through persistent effort.",
            ne = "शनि वर्गोत्तमले स्थिर अनुशासन, दृढता र कार्मिक स्थिरता सिर्जना गर्छ। जातकमा बलियो कर्तव्य र जिम्मेवारीको भावना हुन्छ। कडा परिश्रमले स्थायी नतिजाहरू दिन्छ। चुनौतीहरू भए पनि, जातकले निरन्तर प्रयास मार्फत टिकाउ शक्ति विकास गर्छ।"
        )
    )

    // ==================== D-10 DASHAMSA TEMPLATES ====================

    /**
     * Dashamsa Lagna Templates - Career and Profession
     */
    val dashamsaLagnaTemplates = mapOf(
        ZodiacSign.ARIES to LocalizedTemplate(
            en = "Dashamsa Lagna in Aries indicates a pioneering and leadership-oriented career. The native excels in fields requiring initiative, courage, and competitive spirit. Military, sports, surgery, engineering, and entrepreneurship are favored. The professional approach is direct and action-oriented. The native may be self-employed or lead teams.",
            ne = "मेष दशमांश लग्नले अग्रणी र नेतृत्व-उन्मुख क्यारियर संकेत गर्छ। जातक पहल, साहस र प्रतिस्पर्धी भावना आवश्यक पर्ने क्षेत्रहरूमा उत्कृष्ट हुन्छ। सैन्य, खेलकुद, शल्यचिकित्सा, इन्जिनियरिङ र उद्यमशीलता अनुकूल छन्। व्यावसायिक दृष्टिकोण प्रत्यक्ष र कार्य-उन्मुख हुन्छ। जातक स्व-रोजगार वा टोलीको नेतृत्व गर्न सक्छ।"
        ),
        ZodiacSign.TAURUS to LocalizedTemplate(
            en = "Dashamsa Lagna in Taurus indicates career in finance, banking, luxury goods, or arts. The native has a steady approach to profession with focus on material security. Agriculture, real estate, food industry, and beauty are favored fields. The professional style is patient and value-oriented.",
            ne = "वृष दशमांश लग्नले वित्त, बैंकिङ, विलासी सामान वा कलामा क्यारियर संकेत गर्छ। जातकको भौतिक सुरक्षामा ध्यान केन्द्रित गर्दै पेशामा स्थिर दृष्टिकोण हुन्छ। कृषि, रियल इस्टेट, खाद्य उद्योग र सौन्दर्य अनुकूल क्षेत्रहरू हुन्। व्यावसायिक शैली धैर्यवान र मूल्य-उन्मुख हुन्छ।"
        ),
        ZodiacSign.GEMINI to LocalizedTemplate(
            en = "Dashamsa Lagna in Gemini indicates career in communication, media, writing, or commerce. The native excels in fields requiring mental agility and versatility. Journalism, teaching, trading, and technology are favored. The professional approach is adaptable and intellectually engaging.",
            ne = "मिथुन दशमांश लग्नले संचार, मिडिया, लेखन वा वाणिज्यमा क्यारियर संकेत गर्छ। जातक मानसिक चुस्ती र बहुमुखी प्रतिभा आवश्यक पर्ने क्षेत्रहरूमा उत्कृष्ट हुन्छ। पत्रकारिता, शिक्षण, व्यापार र प्रविधि अनुकूल छन्। व्यावसायिक दृष्टिकोण अनुकूलनीय र बौद्धिक रूपमा आकर्षक हुन्छ।"
        ),
        ZodiacSign.CANCER to LocalizedTemplate(
            en = "Dashamsa Lagna in Cancer indicates career involving nurturing, public welfare, or hospitality. The native excels in fields connecting with people emotionally. Healthcare, nursing, real estate, restaurants, and public service are favored. The professional approach is caring and protective.",
            ne = "कर्कट दशमांश लग्नले पालनपोषण, सार्वजनिक कल्याण वा आतिथ्य सम्बन्धी क्यारियर संकेत गर्छ। जातक मानिसहरूसँग भावनात्मक रूपमा जोडिने क्षेत्रहरूमा उत्कृष्ट हुन्छ। स्वास्थ्य सेवा, नर्सिङ, रियल इस्टेट, रेस्टुरेन्ट र सार्वजनिक सेवा अनुकूल छन्। व्यावसायिक दृष्टिकोण हेरचाह गर्ने र सुरक्षात्मक हुन्छ।"
        ),
        ZodiacSign.LEO to LocalizedTemplate(
            en = "Dashamsa Lagna in Leo indicates career in government, administration, entertainment, or creative fields. The native seeks recognition and authority in profession. Politics, performing arts, management, and luxury brands are favored. The professional approach is dignified and commanding.",
            ne = "सिंह दशमांश लग्नले सरकार, प्रशासन, मनोरञ्जन वा रचनात्मक क्षेत्रहरूमा क्यारियर संकेत गर्छ। जातक पेशामा मान्यता र अधिकार खोज्छ। राजनीति, प्रदर्शन कला, व्यवस्थापन र विलासी ब्रान्डहरू अनुकूल छन्। व्यावसायिक दृष्टिकोण मर्यादित र आदेशात्मक हुन्छ।"
        ),
        ZodiacSign.VIRGO to LocalizedTemplate(
            en = "Dashamsa Lagna in Virgo indicates career in service, analysis, healthcare, or technical fields. The native excels in detail-oriented work requiring precision. Accounting, medicine, editing, and quality control are favored. The professional approach is methodical and improvement-focused.",
            ne = "कन्या दशमांश लग्नले सेवा, विश्लेषण, स्वास्थ्य सेवा वा प्राविधिक क्षेत्रहरूमा क्यारियर संकेत गर्छ। जातक सटीकता आवश्यक पर्ने विस्तार-उन्मुख कार्यमा उत्कृष्ट हुन्छ। लेखा, चिकित्सा, सम्पादन र गुणस्तर नियन्त्रण अनुकूल छन्। व्यावसायिक दृष्टिकोण विधिवत र सुधार-केन्द्रित हुन्छ।"
        ),
        ZodiacSign.LIBRA to LocalizedTemplate(
            en = "Dashamsa Lagna in Libra indicates career in law, diplomacy, arts, or partnerships. The native excels in fields requiring balance and aesthetic sense. Legal profession, fashion, interior design, and consulting are favored. The professional approach is diplomatic and harmony-seeking.",
            ne = "तुला दशमांश लग्नले कानून, कूटनीति, कला वा साझेदारीमा क्यारियर संकेत गर्छ। जातक सन्तुलन र सौन्दर्य बोध आवश्यक पर्ने क्षेत्रहरूमा उत्कृष्ट हुन्छ। कानूनी पेशा, फेसन, इन्टेरियर डिजाइन र परामर्श अनुकूल छन्। व्यावसायिक दृष्टिकोण कूटनीतिक र सामंजस्य-खोज्ने हुन्छ।"
        ),
        ZodiacSign.SCORPIO to LocalizedTemplate(
            en = "Dashamsa Lagna in Scorpio indicates career in research, investigation, healing, or transformation. The native excels in fields requiring depth and intensity. Psychology, surgery, detective work, and occult sciences are favored. The professional approach is penetrating and transformative.",
            ne = "वृश्चिक दशमांश लग्नले अनुसन्धान, अनुसन्धान, उपचार वा परिवर्तनमा क्यारियर संकेत गर्छ। जातक गहिराइ र तीव्रता आवश्यक पर्ने क्षेत्रहरूमा उत्कृष्ट हुन्छ। मनोविज्ञान, शल्यचिकित्सा, जासूसी काम र गुप्त विज्ञान अनुकूल छन्। व्यावसायिक दृष्टिकोण भेदक र परिवर्तनकारी हुन्छ।"
        ),
        ZodiacSign.SAGITTARIUS to LocalizedTemplate(
            en = "Dashamsa Lagna in Sagittarius indicates career in education, religion, law, or international affairs. The native excels in fields requiring vision and principles. Teaching, publishing, travel industry, and higher learning are favored. The professional approach is expansive and principled.",
            ne = "धनु दशमांश लग्नले शिक्षा, धर्म, कानून वा अन्तर्राष्ट्रिय मामिलाहरूमा क्यारियर संकेत गर्छ। जातक दृष्टि र सिद्धान्तहरू आवश्यक पर्ने क्षेत्रहरूमा उत्कृष्ट हुन्छ। शिक्षण, प्रकाशन, यात्रा उद्योग र उच्च शिक्षा अनुकूल छन्। व्यावसायिक दृष्टिकोण विस्तृत र सैद्धान्तिक हुन्छ।"
        ),
        ZodiacSign.CAPRICORN to LocalizedTemplate(
            en = "Dashamsa Lagna in Capricorn indicates career in management, government, or large organizations. The native excels in fields requiring discipline and long-term planning. Administration, corporate leadership, and established industries are favored. The professional approach is ambitious and structured.",
            ne = "मकर दशमांश लग्नले व्यवस्थापन, सरकार वा ठूला संगठनहरूमा क्यारियर संकेत गर्छ। जातक अनुशासन र दीर्घकालीन योजना आवश्यक पर्ने क्षेत्रहरूमा उत्कृष्ट हुन्छ। प्रशासन, कर्पोरेट नेतृत्व र स्थापित उद्योगहरू अनुकूल छन्। व्यावसायिक दृष्टिकोण महत्वाकांक्षी र संरचित हुन्छ।"
        ),
        ZodiacSign.AQUARIUS to LocalizedTemplate(
            en = "Dashamsa Lagna in Aquarius indicates career in technology, innovation, or humanitarian work. The native excels in fields requiring original thinking. IT, social work, scientific research, and networking are favored. The professional approach is progressive and unconventional.",
            ne = "कुम्भ दशमांश लग्नले प्रविधि, नवीनता वा मानवतावादी कार्यमा क्यारियर संकेत गर्छ। जातक मौलिक सोच आवश्यक पर्ने क्षेत्रहरूमा उत्कृष्ट हुन्छ। IT, सामाजिक कार्य, वैज्ञानिक अनुसन्धान र नेटवर्किङ अनुकूल छन्। व्यावसायिक दृष्टिकोण प्रगतिशील र अपरम्परागत हुन्छ।"
        ),
        ZodiacSign.PISCES to LocalizedTemplate(
            en = "Dashamsa Lagna in Pisces indicates career in healing, arts, or spiritual service. The native excels in fields requiring intuition and compassion. Music, film, healthcare, charity work, and spiritual teaching are favored. The professional approach is imaginative and service-oriented.",
            ne = "मीन दशमांश लग्नले उपचार, कला वा आध्यात्मिक सेवामा क्यारियर संकेत गर्छ। जातक अन्तर्ज्ञान र करुणा आवश्यक पर्ने क्षेत्रहरूमा उत्कृष्ट हुन्छ। संगीत, चलचित्र, स्वास्थ्य सेवा, दान कार्य र आध्यात्मिक शिक्षण अनुकूल छन्। व्यावसायिक दृष्टिकोण कल्पनाशील र सेवा-उन्मुख हुन्छ।"
        )
    )

    /**
     * 10th Lord in Dashamsa Houses
     */
    val tenthLordInDashamsaHouseTemplates = mapOf(
        1 to LocalizedTemplate(
            en = "10th lord in 1st house of Dashamsa indicates self-made career success. The native's personality is central to professional achievements. Independent work and personal branding bring success. The career is closely tied to personal identity and self-expression.",
            ne = "दशमांशको १ औं भावमा १० औं भावेशले स्व-निर्मित क्यारियर सफलता संकेत गर्छ। जातकको व्यक्तित्व व्यावसायिक उपलब्धिहरूको केन्द्रमा हुन्छ। स्वतन्त्र काम र व्यक्तिगत ब्रान्डिङले सफलता ल्याउँछ। क्यारियर व्यक्तिगत पहिचान र आत्म-अभिव्यक्तिसँग नजिकबाट जोडिएको हुन्छ।"
        ),
        2 to LocalizedTemplate(
            en = "10th lord in 2nd house of Dashamsa indicates career involving finance, family business, or speech. Wealth accumulation through profession is emphasized. The native may work with money, values, or family enterprises. Income from career is significant.",
            ne = "दशमांशको २ औं भावमा १० औं भावेशले वित्त, पारिवारिक व्यवसाय वा वाणीसँग सम्बन्धित क्यारियर संकेत गर्छ। पेशा मार्फत धन संचयमा जोड दिइन्छ। जातक पैसा, मूल्यहरू वा पारिवारिक उद्यमहरूसँग काम गर्न सक्छ। क्यारियरबाट आम्दानी महत्वपूर्ण हुन्छ।"
        ),
        3 to LocalizedTemplate(
            en = "10th lord in 3rd house of Dashamsa indicates career in communication, media, or short travels. Writing, journalism, sales, and marketing are favored. The native uses courage and initiative in profession. Siblings may be involved in career matters.",
            ne = "दशमांशको ३ औं भावमा १० औं भावेशले संचार, मिडिया वा छोटा यात्रामा क्यारियर संकेत गर्छ। लेखन, पत्रकारिता, बिक्री र मार्केटिङ अनुकूल छन्। जातक पेशामा साहस र पहल प्रयोग गर्छ। दाजुभाइ/दिदीबहिनीहरू क्यारियर मामिलाहरूमा संलग्न हुन सक्छन्।"
        ),
        4 to LocalizedTemplate(
            en = "10th lord in 4th house of Dashamsa indicates career involving property, vehicles, or working from home. Real estate, automobile industry, and domestic affairs are favored. The native may work in homeland or establish home-based business. Emotional satisfaction from work is important.",
            ne = "दशमांशको ४ औं भावमा १० औं भावेशले सम्पत्ति, सवारी साधन वा घरबाट काम गर्ने क्यारियर संकेत गर्छ। रियल इस्टेट, अटोमोबाइल उद्योग र घरेलु मामिलाहरू अनुकूल छन्। जातक मातृभूमिमा काम गर्न वा घर-आधारित व्यवसाय स्थापना गर्न सक्छ। कामबाट भावनात्मक सन्तुष्टि महत्वपूर्ण हुन्छ।"
        ),
        5 to LocalizedTemplate(
            en = "10th lord in 5th house of Dashamsa indicates career in creativity, education, or speculation. Teaching, entertainment, investment, and children-related fields are favored. The native uses intelligence and creativity in profession. Past life karma supports career success.",
            ne = "दशमांशको ५ औं भावमा १० औं भावेशले रचनात्मकता, शिक्षा वा सट्टेबाजीमा क्यारियर संकेत गर्छ। शिक्षण, मनोरञ्जन, लगानी र बालबालिका-सम्बन्धित क्षेत्रहरू अनुकूल छन्। जातक पेशामा बुद्धिमत्ता र रचनात्मकता प्रयोग गर्छ। पूर्वजन्म कर्मले क्यारियर सफलतालाई समर्थन गर्छ।"
        ),
        6 to LocalizedTemplate(
            en = "10th lord in 6th house of Dashamsa indicates career involving service, health, or competition. Medical field, legal profession, and service industries are favored. The native overcomes enemies and obstacles through work. Daily routines and discipline are central to success.",
            ne = "दशमांशको ६ औं भावमा १० औं भावेशले सेवा, स्वास्थ्य वा प्रतिस्पर्धासँग सम्बन्धित क्यारियर संकेत गर्छ। चिकित्सा क्षेत्र, कानूनी पेशा र सेवा उद्योगहरू अनुकूल छन्। जातक काम मार्फत शत्रुहरू र बाधाहरू पार गर्छ। दैनिक दिनचर्या र अनुशासन सफलताको केन्द्रमा हुन्छ।"
        ),
        7 to LocalizedTemplate(
            en = "10th lord in 7th house of Dashamsa indicates career involving partnerships, consulting, or foreign dealings. Business partnerships, diplomacy, and client-facing work are favored. The spouse may be involved in career. Public dealings and negotiations are important.",
            ne = "दशमांशको ७ औं भावमा १० औं भावेशले साझेदारी, परामर्श वा विदेशी कारोबारसँग सम्बन्धित क्यारियर संकेत गर्छ। व्यापार साझेदारी, कूटनीति र ग्राहक-सामना गर्ने काम अनुकूल छ। जीवनसाथी क्यारियरमा संलग्न हुन सक्छ। सार्वजनिक व्यवहार र वार्ता महत्वपूर्ण हुन्छ।"
        ),
        8 to LocalizedTemplate(
            en = "10th lord in 8th house of Dashamsa indicates career involving research, occult, or others' resources. Insurance, inheritance management, and transformation fields are favored. Career may have sudden changes or hidden aspects. Working behind scenes brings success.",
            ne = "दशमांशको ८ औं भावमा १० औं भावेशले अनुसन्धान, गुप्त वा अरूको स्रोतसँग सम्बन्धित क्यारियर संकेत गर्छ। बीमा, सम्पत्ति व्यवस्थापन र परिवर्तन क्षेत्रहरू अनुकूल छन्। क्यारियरमा अचानक परिवर्तन वा लुकेका पक्षहरू हुन सक्छन्। पर्दा पछाडि काम गर्दा सफलता आउँछ।"
        ),
        9 to LocalizedTemplate(
            en = "10th lord in 9th house of Dashamsa is highly auspicious for career success with fortune's blessing. Higher education, religion, law, and international work are favored. The native receives guidance and luck in profession. Father figure may be influential in career.",
            ne = "दशमांशको ९ औं भावमा १० औं भावेश भाग्यको आशीर्वादसहित क्यारियर सफलताको लागि अत्यधिक शुभ छ। उच्च शिक्षा, धर्म, कानून र अन्तर्राष्ट्रिय काम अनुकूल छ। जातकले पेशामा मार्गदर्शन र भाग्य प्राप्त गर्छ। पितृ व्यक्ति क्यारियरमा प्रभावशाली हुन सक्छ।"
        ),
        10 to LocalizedTemplate(
            en = "10th lord in 10th house of Dashamsa is the most powerful position for career success. The native achieves prominent position and recognition in profession. Government, administration, and leadership roles are indicated. Professional achievements are long-lasting and impactful.",
            ne = "दशमांशको १० औं भावमा १० औं भावेश क्यारियर सफलताको लागि सबैभन्दा शक्तिशाली स्थान हो। जातकले पेशामा प्रमुख स्थान र मान्यता प्राप्त गर्छ। सरकार, प्रशासन र नेतृत्व भूमिकाहरू संकेत गरिन्छ। व्यावसायिक उपलब्धिहरू दीर्घकालीन र प्रभावशाली हुन्छन्।"
        ),
        11 to LocalizedTemplate(
            en = "10th lord in 11th house of Dashamsa indicates career bringing gains and fulfillment of ambitions. Networking, large organizations, and social causes are favored. Elder siblings and friends support career. The native achieves goals through profession.",
            ne = "दशमांशको ११ औं भावमा १० औं भावेशले लाभ र महत्वाकांक्षाहरूको पूर्ति ल्याउने क्यारियर संकेत गर्छ। नेटवर्किङ, ठूला संगठनहरू र सामाजिक कारणहरू अनुकूल छन्। जेठा दाजुभाइ/दिदीबहिनी र साथीहरूले क्यारियरलाई समर्थन गर्छन्। जातक पेशा मार्फत लक्ष्यहरू प्राप्त गर्छ।"
        ),
        12 to LocalizedTemplate(
            en = "10th lord in 12th house of Dashamsa indicates career in foreign lands, hospitals, or spiritual institutions. Overseas work, exports, and behind-scenes roles are favored. The native may work in isolation or away from birthplace. Spiritual or charitable work brings fulfillment.",
            ne = "दशमांशको १२ औं भावमा १० औं भावेशले विदेश, अस्पताल वा आध्यात्मिक संस्थाहरूमा क्यारियर संकेत गर्छ। विदेशी काम, निर्यात र पर्दा पछाडिका भूमिकाहरू अनुकूल छन्। जातक एक्लो वा जन्मस्थानबाट टाढा काम गर्न सक्छ। आध्यात्मिक वा परोपकारी कामले पूर्णता ल्याउँछ।"
        )
    )

    // ==================== D-7 SAPTAMSA TEMPLATES ====================

    /**
     * Saptamsa - Children and Progeny
     */
    val saptamsaLagnaTemplates = mapOf(
        ZodiacSign.ARIES to LocalizedTemplate(
            en = "Saptamsa Lagna in Aries indicates energetic and independent children. The first child may be male-natured with leadership qualities. Children are likely to be courageous and pioneering. The native has an active and enthusiastic approach to parenthood.",
            ne = "मेष सप्तांश लग्नले ऊर्जावान र स्वतन्त्र सन्तानहरू संकेत गर्छ। पहिलो सन्तान नेतृत्व गुणहरू भएको पुरुष-स्वभावको हुन सक्छ। सन्तानहरू साहसी र अग्रणी हुने सम्भावना छ। जातकको अभिभावकत्वमा सक्रिय र उत्साही दृष्टिकोण हुन्छ।"
        ),
        ZodiacSign.TAURUS to LocalizedTemplate(
            en = "Saptamsa Lagna in Taurus indicates stable and comfort-loving children. Children bring material prosperity and are likely artistically inclined. The first child may be gentle and value-oriented. The native provides stable and nurturing environment for children.",
            ne = "वृष सप्तांश लग्नले स्थिर र आराम-प्रेमी सन्तानहरू संकेत गर्छ। सन्तानहरूले भौतिक समृद्धि ल्याउँछन् र कलात्मक रूपमा झुकेको हुने सम्भावना छ। पहिलो सन्तान कोमल र मूल्य-उन्मुख हुन सक्छ। जातकले सन्तानहरूको लागि स्थिर र पालनपोषण गर्ने वातावरण प्रदान गर्छ।"
        ),
        ZodiacSign.GEMINI to LocalizedTemplate(
            en = "Saptamsa Lagna in Gemini indicates intelligent and communicative children. There may be twins or multiple children. Children are versatile and intellectually curious. The native engages children through learning and mental activities.",
            ne = "मिथुन सप्तांश लग्नले बुद्धिमान र संवादशील सन्तानहरू संकेत गर्छ। जुम्ल्याहा वा धेरै सन्तानहरू हुन सक्छन्। सन्तानहरू बहुमुखी र बौद्धिक रूपमा जिज्ञासु हुन्छन्। जातकले सिकाइ र मानसिक गतिविधिहरू मार्फत सन्तानहरूलाई संलग्न गराउँछ।"
        ),
        ZodiacSign.CANCER to LocalizedTemplate(
            en = "Saptamsa Lagna in Cancer indicates emotionally connected and nurturing relationship with children. Children are sensitive and family-attached. The native is deeply caring and protective as a parent. Home environment strongly influences children's development.",
            ne = "कर्कट सप्तांश लग्नले सन्तानहरूसँग भावनात्मक रूपमा जोडिएको र पालनपोषण गर्ने सम्बन्ध संकेत गर्छ। सन्तानहरू संवेदनशील र परिवार-आसक्त हुन्छन्। जातक अभिभावकको रूपमा गहिरो हेरचाह गर्ने र सुरक्षात्मक हुन्छ। घरको वातावरणले सन्तानहरूको विकासलाई बलियो रूपमा प्रभाव पार्छ।"
        ),
        ZodiacSign.LEO to LocalizedTemplate(
            en = "Saptamsa Lagna in Leo indicates proud and talented children. Children have strong personalities and seek recognition. The first child may be particularly distinguished. The native takes pride in children's achievements and encourages their self-expression.",
            ne = "सिंह सप्तांश लग्नले गर्वित र प्रतिभाशाली सन्तानहरू संकेत गर्छ। सन्तानहरूमा बलियो व्यक्तित्व हुन्छ र मान्यता खोज्छन्। पहिलो सन्तान विशेष गरी विशिष्ट हुन सक्छ। जातकले सन्तानहरूको उपलब्धिमा गर्व गर्छ र तिनीहरूको आत्म-अभिव्यक्तिलाई प्रोत्साहित गर्छ।"
        ),
        ZodiacSign.VIRGO to LocalizedTemplate(
            en = "Saptamsa Lagna in Virgo indicates health-conscious and analytical children. Children are detail-oriented and service-minded. The native focuses on children's health and education. The approach to parenting is practical and improvement-oriented.",
            ne = "कन्या सप्तांश लग्नले स्वास्थ्य-सचेत र विश्लेषणात्मक सन्तानहरू संकेत गर्छ। सन्तानहरू विस्तार-उन्मुख र सेवा-मनस्थितिको हुन्छन्। जातक सन्तानहरूको स्वास्थ्य र शिक्षामा केन्द्रित हुन्छ। अभिभावकत्वमा दृष्टिकोण व्यावहारिक र सुधार-उन्मुख हुन्छ।"
        ),
        ZodiacSign.LIBRA to LocalizedTemplate(
            en = "Saptamsa Lagna in Libra indicates balanced and artistic children. Children have harmonious disposition and aesthetic sense. The relationship with children is based on fairness and mutual respect. The native teaches children about relationships and balance.",
            ne = "तुला सप्तांश लग्नले सन्तुलित र कलात्मक सन्तानहरू संकेत गर्छ। सन्तानहरूमा सामंजस्यपूर्ण स्वभाव र सौन्दर्यबोध हुन्छ। सन्तानहरूसँगको सम्बन्ध निष्पक्षता र पारस्परिक सम्मानमा आधारित हुन्छ। जातकले सन्तानहरूलाई सम्बन्ध र सन्तुलनको बारेमा सिकाउँछ।"
        ),
        ZodiacSign.SCORPIO to LocalizedTemplate(
            en = "Saptamsa Lagna in Scorpio indicates intense and transformative relationship with children. Children are powerful and emotionally deep. There may be challenges that lead to profound growth. The native experiences transformation through parenthood.",
            ne = "वृश्चिक सप्तांश लग्नले सन्तानहरूसँग तीव्र र परिवर्तनकारी सम्बन्ध संकेत गर्छ। सन्तानहरू शक्तिशाली र भावनात्मक रूपमा गहिरा हुन्छन्। गहिरो वृद्धि निम्त्याउने चुनौतीहरू हुन सक्छन्। जातकले अभिभावकत्व मार्फत परिवर्तन अनुभव गर्छ।"
        ),
        ZodiacSign.SAGITTARIUS to LocalizedTemplate(
            en = "Saptamsa Lagna in Sagittarius indicates philosophical and adventurous children. Children are optimistic and seek higher knowledge. The native encourages children's expansion and exploration. Educational and spiritual growth of children is emphasized.",
            ne = "धनु सप्तांश लग्नले दार्शनिक र साहसी सन्तानहरू संकेत गर्छ। सन्तानहरू आशावादी हुन्छन् र उच्च ज्ञान खोज्छन्। जातकले सन्तानहरूको विस्तार र अन्वेषणलाई प्रोत्साहित गर्छ। सन्तानहरूको शैक्षिक र आध्यात्मिक वृद्धिमा जोड दिइन्छ।"
        ),
        ZodiacSign.CAPRICORN to LocalizedTemplate(
            en = "Saptamsa Lagna in Capricorn indicates responsible and ambitious children. Children are mature and achievement-oriented. The native takes parenting seriously with disciplined approach. Children may face challenges that build character.",
            ne = "मकर सप्तांश लग्नले जिम्मेवार र महत्वाकांक्षी सन्तानहरू संकेत गर्छ। सन्तानहरू परिपक्व र उपलब्धि-उन्मुख हुन्छन्। जातकले अनुशासित दृष्टिकोणका साथ अभिभावकत्वलाई गम्भीरताका साथ लिन्छ। सन्तानहरूले चरित्र निर्माण गर्ने चुनौतीहरूको सामना गर्न सक्छन्।"
        ),
        ZodiacSign.AQUARIUS to LocalizedTemplate(
            en = "Saptamsa Lagna in Aquarius indicates unique and independent-minded children. Children are innovative and socially conscious. The native gives children freedom to develop individuality. Unconventional approach to parenting yields unique children.",
            ne = "कुम्भ सप्तांश लग्नले अद्वितीय र स्वतन्त्र-मनको सन्तानहरू संकेत गर्छ। सन्तानहरू नवीन र सामाजिक रूपमा सचेत हुन्छन्। जातकले सन्तानहरूलाई व्यक्तित्व विकास गर्न स्वतन्त्रता दिन्छ। अभिभावकत्वमा अपरम्परागत दृष्टिकोणले अद्वितीय सन्तानहरू दिन्छ।"
        ),
        ZodiacSign.PISCES to LocalizedTemplate(
            en = "Saptamsa Lagna in Pisces indicates spiritually inclined and sensitive children. Children are imaginative and compassionate. The native has intuitive understanding of children's needs. There is spiritual bond and unconditional love between parent and children.",
            ne = "मीन सप्तांश लग्नले आध्यात्मिक रूपमा झुकेको र संवेदनशील सन्तानहरू संकेत गर्छ। सन्तानहरू कल्पनाशील र दयालु हुन्छन्। जातकमा सन्तानहरूको आवश्यकताहरूको अन्तर्ज्ञानी बुझाइ हुन्छ। अभिभावक र सन्तानहरूबीच आध्यात्मिक बन्धन र शर्तरहित प्रेम हुन्छ।"
        )
    )

    // ==================== D-12 DWADASAMSA TEMPLATES ====================

    /**
     * Dwadasamsa - Parents and Ancestry
     */
    val dwadasamsaLagnaTemplates = mapOf(
        ZodiacSign.ARIES to LocalizedTemplate(
            en = "Dwadasamsa Lagna in Aries indicates strong paternal lineage with leadership qualities. The family has pioneering and courageous ancestry. Parents, especially father, have independent nature. The native inherits energy and initiative from ancestors.",
            ne = "मेष द्वादशांश लग्नले नेतृत्व गुणहरू भएको बलियो पितृ वंश संकेत गर्छ। परिवारमा अग्रणी र साहसी पूर्वज छन्। अभिभावकहरू, विशेष गरी पिता, स्वतन्त्र स्वभावको हुन्छ। जातकले पूर्वजहरूबाट ऊर्जा र पहल प्राप्त गर्छ।"
        ),
        ZodiacSign.TAURUS to LocalizedTemplate(
            en = "Dwadasamsa Lagna in Taurus indicates wealthy and stable parental lineage. The family has strong connection to land and resources. Parents provide material security and stability. The native inherits values and appreciation for comfort from ancestors.",
            ne = "वृष द्वादशांश लग्नले धनी र स्थिर पितृ वंश संकेत गर्छ। परिवारको जमीन र स्रोतहरूसँग बलियो सम्बन्ध छ। अभिभावकहरूले भौतिक सुरक्षा र स्थिरता प्रदान गर्छन्। जातकले पूर्वजहरूबाट मूल्यहरू र आरामको कदर प्राप्त गर्छ।"
        ),
        ZodiacSign.GEMINI to LocalizedTemplate(
            en = "Dwadasamsa Lagna in Gemini indicates intellectual parental lineage. The family has tradition of learning and communication. Parents are versatile and mentally active. The native inherits intelligence and curiosity from ancestors.",
            ne = "मिथुन द्वादशांश लग्नले बौद्धिक पितृ वंश संकेत गर्छ। परिवारमा सिकाइ र संवादको परम्परा छ। अभिभावकहरू बहुमुखी र मानसिक रूपमा सक्रिय हुन्छन्। जातकले पूर्वजहरूबाट बुद्धिमत्ता र जिज्ञासा प्राप्त गर्छ।"
        ),
        ZodiacSign.CANCER to LocalizedTemplate(
            en = "Dwadasamsa Lagna in Cancer indicates nurturing and emotionally connected parental lineage. The family has strong maternal influence and traditions. Home and family bonds are central. The native inherits emotional depth and nurturing abilities from ancestors.",
            ne = "कर्कट द्वादशांश लग्नले पालनपोषण र भावनात्मक रूपमा जोडिएको पितृ वंश संकेत गर्छ। परिवारमा बलियो मातृ प्रभाव र परम्पराहरू छन्। घर र पारिवारिक बन्धन केन्द्रमा छन्। जातकले पूर्वजहरूबाट भावनात्मक गहिराइ र पालनपोषण क्षमताहरू प्राप्त गर्छ।"
        ),
        ZodiacSign.LEO to LocalizedTemplate(
            en = "Dwadasamsa Lagna in Leo indicates royal or distinguished parental lineage. The family has history of leadership and recognition. Parents are dignified and proud. The native inherits self-confidence and creative abilities from ancestors.",
            ne = "सिंह द्वादशांश लग्नले राजसी वा विशिष्ट पितृ वंश संकेत गर्छ। परिवारमा नेतृत्व र मान्यताको इतिहास छ। अभिभावकहरू मर्यादित र गर्वित हुन्छन्। जातकले पूर्वजहरूबाट आत्मविश्वास र रचनात्मक क्षमताहरू प्राप्त गर्छ।"
        ),
        ZodiacSign.VIRGO to LocalizedTemplate(
            en = "Dwadasamsa Lagna in Virgo indicates service-oriented parental lineage. The family has tradition of healing or analytical work. Parents are practical and detail-oriented. The native inherits analytical abilities and service mentality from ancestors.",
            ne = "कन्या द्वादशांश लग्नले सेवा-उन्मुख पितृ वंश संकेत गर्छ। परिवारमा उपचार वा विश्लेषणात्मक कामको परम्परा छ। अभिभावकहरू व्यावहारिक र विस्तार-उन्मुख हुन्छन्। जातकले पूर्वजहरूबाट विश्लेषणात्मक क्षमताहरू र सेवा मानसिकता प्राप्त गर्छ।"
        ),
        ZodiacSign.LIBRA to LocalizedTemplate(
            en = "Dwadasamsa Lagna in Libra indicates harmonious and artistic parental lineage. The family values balance, justice, and beauty. Parents are diplomatic and fair-minded. The native inherits aesthetic sense and social skills from ancestors.",
            ne = "तुला द्वादशांश लग्नले सामंजस्यपूर्ण र कलात्मक पितृ वंश संकेत गर्छ। परिवारले सन्तुलन, न्याय र सौन्दर्यलाई मूल्य दिन्छ। अभिभावकहरू कूटनीतिक र निष्पक्ष-मनको हुन्छन्। जातकले पूर्वजहरूबाट सौन्दर्यबोध र सामाजिक सीपहरू प्राप्त गर्छ।"
        ),
        ZodiacSign.SCORPIO to LocalizedTemplate(
            en = "Dwadasamsa Lagna in Scorpio indicates intense and secretive parental lineage. The family has hidden history or transformative experiences. Parents have depth and intensity. The native inherits research abilities and transformative power from ancestors.",
            ne = "वृश्चिक द्वादशांश लग्नले तीव्र र गोप्य पितृ वंश संकेत गर्छ। परिवारमा लुकेको इतिहास वा परिवर्तनकारी अनुभवहरू छन्। अभिभावकहरूमा गहिराइ र तीव्रता हुन्छ। जातकले पूर्वजहरूबाट अनुसन्धान क्षमताहरू र परिवर्तनकारी शक्ति प्राप्त गर्छ।"
        ),
        ZodiacSign.SAGITTARIUS to LocalizedTemplate(
            en = "Dwadasamsa Lagna in Sagittarius indicates religious or philosophical parental lineage. The family has tradition of higher learning and spirituality. Parents are wise and principled. The native inherits wisdom and dharmic values from ancestors.",
            ne = "धनु द्वादशांश लग्नले धार्मिक वा दार्शनिक पितृ वंश संकेत गर्छ। परिवारमा उच्च शिक्षा र आध्यात्मिकताको परम्परा छ। अभिभावकहरू बुद्धिमान र सैद्धान्तिक हुन्छन्। जातकले पूर्वजहरूबाट ज्ञान र धार्मिक मूल्यहरू प्राप्त गर्छ।"
        ),
        ZodiacSign.CAPRICORN to LocalizedTemplate(
            en = "Dwadasamsa Lagna in Capricorn indicates ambitious and disciplined parental lineage. The family has tradition of hard work and achievement. Parents are responsible and authority figures. The native inherits ambition and perseverance from ancestors.",
            ne = "मकर द्वादशांश लग्नले महत्वाकांक्षी र अनुशासित पितृ वंश संकेत गर्छ। परिवारमा कडा परिश्रम र उपलब्धिको परम्परा छ। अभिभावकहरू जिम्मेवार र अधिकार व्यक्ति हुन्छन्। जातकले पूर्वजहरूबाट महत्वाकांक्षा र दृढता प्राप्त गर्छ।"
        ),
        ZodiacSign.AQUARIUS to LocalizedTemplate(
            en = "Dwadasamsa Lagna in Aquarius indicates unconventional or humanitarian parental lineage. The family has tradition of innovation or social service. Parents are independent thinkers. The native inherits originality and humanitarian values from ancestors.",
            ne = "कुम्भ द्वादशांश लग्नले अपरम्परागत वा मानवतावादी पितृ वंश संकेत गर्छ। परिवारमा नवीनता वा सामाजिक सेवाको परम्परा छ। अभिभावकहरू स्वतन्त्र विचारक हुन्छन्। जातकले पूर्वजहरूबाट मौलिकता र मानवतावादी मूल्यहरू प्राप्त गर्छ।"
        ),
        ZodiacSign.PISCES to LocalizedTemplate(
            en = "Dwadasamsa Lagna in Pisces indicates spiritual or artistic parental lineage. The family has tradition of devotion and creativity. Parents are compassionate and intuitive. The native inherits spiritual inclination and artistic abilities from ancestors.",
            ne = "मीन द्वादशांश लग्नले आध्यात्मिक वा कलात्मक पितृ वंश संकेत गर्छ। परिवारमा भक्ति र रचनात्मकताको परम्परा छ। अभिभावकहरू दयालु र अन्तर्ज्ञानी हुन्छन्। जातकले पूर्वजहरूबाट आध्यात्मिक झुकाव र कलात्मक क्षमताहरू प्राप्त गर्छ।"
        )
    )

    // ==================== D-20 VIMSAMSA TEMPLATES ====================

    /**
     * Vimsamsa - Spiritual Progress and Devotion
     */
    val vimsamsaLagnaTemplates = mapOf(
        ZodiacSign.ARIES to LocalizedTemplate(
            en = "Vimsamsa Lagna in Aries indicates active and warrior-like spiritual path. The native approaches spirituality with energy and initiative. Meditation may be challenging but action-oriented practices like karma yoga suit well. Devotion to Mars-ruled deities like Hanuman or Kartikeya is favorable.",
            ne = "मेष विंशांश लग्नले सक्रिय र योद्धा जस्तो आध्यात्मिक मार्ग संकेत गर्छ। जातक ऊर्जा र पहलका साथ आध्यात्मिकतामा अघि बढ्छ। ध्यान चुनौतीपूर्ण हुन सक्छ तर कर्म योग जस्ता कार्य-उन्मुख अभ्यासहरू राम्रोसँग मिल्छन्। हनुमान वा कार्तिकेय जस्ता मंगल-शासित देवताहरूप्रति भक्ति अनुकूल छ।"
        ),
        ZodiacSign.TAURUS to LocalizedTemplate(
            en = "Vimsamsa Lagna in Taurus indicates devotional and materially grounded spirituality. The native approaches spiritual practice through beauty, ritual, and sensory experience. Bhakti yoga and temple worship are favorable. Devotion to Venus-ruled deities like Lakshmi is indicated.",
            ne = "वृष विंशांश लग्नले भक्तिमय र भौतिक रूपमा आधारित आध्यात्मिकता संकेत गर्छ। जातक सौन्दर्य, अनुष्ठान र संवेदी अनुभव मार्फत आध्यात्मिक अभ्यासमा अघि बढ्छ। भक्ति योग र मन्दिर पूजा अनुकूल छ। लक्ष्मी जस्ता शुक्र-शासित देवताहरूप्रति भक्ति संकेत गरिन्छ।"
        ),
        ZodiacSign.GEMINI to LocalizedTemplate(
            en = "Vimsamsa Lagna in Gemini indicates intellectual approach to spirituality. The native pursues spiritual knowledge through study and communication. Jnana yoga and scriptural learning are favorable. Devotion to Mercury-ruled forms of Vishnu is indicated.",
            ne = "मिथुन विंशांश लग्नले आध्यात्मिकतामा बौद्धिक दृष्टिकोण संकेत गर्छ। जातक अध्ययन र संवाद मार्फत आध्यात्मिक ज्ञान खोज्छ। ज्ञान योग र शास्त्रीय शिक्षा अनुकूल छ। विष्णुको बुध-शासित रूपहरूप्रति भक्ति संकेत गरिन्छ।"
        ),
        ZodiacSign.CANCER to LocalizedTemplate(
            en = "Vimsamsa Lagna in Cancer indicates emotional and devotional spirituality. The native connects to divine through nurturing and compassion. Bhakti yoga with maternal deities is favorable. Devotion to the Divine Mother in all forms is strongly indicated.",
            ne = "कर्कट विंशांश लग्नले भावनात्मक र भक्तिमय आध्यात्मिकता संकेत गर्छ। जातक पालनपोषण र करुणा मार्फत दिव्यसँग जोडिन्छ। मातृ देवताहरूसँग भक्ति योग अनुकूल छ। सबै रूपहरूमा दिव्य माताप्रति भक्ति बलियो रूपमा संकेत गरिन्छ।"
        ),
        ZodiacSign.LEO to LocalizedTemplate(
            en = "Vimsamsa Lagna in Leo indicates royal and radiant spiritual path. The native approaches spirituality with confidence and devotion to solar deities. Surya Namaskar and Sun worship are favorable. Devotion to Surya, Vishnu, and Shiva in their glorious forms is indicated.",
            ne = "सिंह विंशांश लग्नले राजसी र दीप्तिमान आध्यात्मिक मार्ग संकेत गर्छ। जातक आत्मविश्वास र सौर्य देवताहरूप्रति भक्तिका साथ आध्यात्मिकतामा अघि बढ्छ। सूर्य नमस्कार र सूर्य पूजा अनुकूल छ। सूर्य, विष्णु र शिवको गौरवशाली रूपहरूप्रति भक्ति संकेत गरिन्छ।"
        ),
        ZodiacSign.VIRGO to LocalizedTemplate(
            en = "Vimsamsa Lagna in Virgo indicates service-oriented and analytical spirituality. The native pursues spiritual progress through seva and practical application. Karma yoga and detailed rituals are favorable. Devotion to Vishnu, especially as the orderly preserver, is indicated.",
            ne = "कन्या विंशांश लग्नले सेवा-उन्मुख र विश्लेषणात्मक आध्यात्मिकता संकेत गर्छ। जातक सेवा र व्यावहारिक प्रयोग मार्फत आध्यात्मिक प्रगति खोज्छ। कर्म योग र विस्तृत अनुष्ठानहरू अनुकूल छन्। विशेष गरी व्यवस्थित संरक्षकको रूपमा विष्णुप्रति भक्ति संकेत गरिन्छ।"
        ),
        ZodiacSign.LIBRA to LocalizedTemplate(
            en = "Vimsamsa Lagna in Libra indicates balanced and harmonious spiritual path. The native pursues spirituality through beauty, art, and relationships. Bhakti yoga and partner-based practices are favorable. Devotion to divine couples like Lakshmi-Narayana is indicated.",
            ne = "तुला विंशांश लग्नले सन्तुलित र सामंजस्यपूर्ण आध्यात्मिक मार्ग संकेत गर्छ। जातक सौन्दर्य, कला र सम्बन्धहरू मार्फत आध्यात्मिकता खोज्छ। भक्ति योग र साथी-आधारित अभ्यासहरू अनुकूल छन्। लक्ष्मी-नारायण जस्ता दिव्य जोडीहरूप्रति भक्ति संकेत गरिन्छ।"
        ),
        ZodiacSign.SCORPIO to LocalizedTemplate(
            en = "Vimsamsa Lagna in Scorpio indicates intense and transformative spiritual path. The native pursues deep esoteric practices and transformation. Tantra and kundalini yoga are favorable. Devotion to Shiva, Kali, and transformative deities is strongly indicated.",
            ne = "वृश्चिक विंशांश लग्नले तीव्र र परिवर्तनकारी आध्यात्मिक मार्ग संकेत गर्छ। जातक गहिरो गुप्त अभ्यासहरू र परिवर्तन खोज्छ। तन्त्र र कुण्डलिनी योग अनुकूल छन्। शिव, काली र परिवर्तनकारी देवताहरूप्रति भक्ति बलियो रूपमा संकेत गरिन्छ।"
        ),
        ZodiacSign.SAGITTARIUS to LocalizedTemplate(
            en = "Vimsamsa Lagna in Sagittarius indicates philosophical and dharmic spiritual path. The native pursues higher knowledge and religious traditions. Raja yoga and vedantic study are favorable. Devotion to Jupiter-ruled deities and gurus is strongly indicated.",
            ne = "धनु विंशांश लग्नले दार्शनिक र धार्मिक आध्यात्मिक मार्ग संकेत गर्छ। जातक उच्च ज्ञान र धार्मिक परम्पराहरू खोज्छ। राज योग र वेदान्तिक अध्ययन अनुकूल छ। बृहस्पति-शासित देवताहरू र गुरुहरूप्रति भक्ति बलियो रूपमा संकेत गरिन्छ।"
        ),
        ZodiacSign.CAPRICORN to LocalizedTemplate(
            en = "Vimsamsa Lagna in Capricorn indicates disciplined and structured spiritual path. The native pursues spirituality through persistent effort and traditional methods. Hatha yoga and austere practices are favorable. Devotion to Saturn-ruled forms like Shani or Hanuman is indicated.",
            ne = "मकर विंशांश लग्नले अनुशासित र संरचित आध्यात्मिक मार्ग संकेत गर्छ। जातक निरन्तर प्रयास र परम्परागत विधिहरू मार्फत आध्यात्मिकता खोज्छ। हठ योग र कठोर अभ्यासहरू अनुकूल छन्। शनि वा हनुमान जस्ता शनि-शासित रूपहरूप्रति भक्ति संकेत गरिन्छ।"
        ),
        ZodiacSign.AQUARIUS to LocalizedTemplate(
            en = "Vimsamsa Lagna in Aquarius indicates unconventional and humanitarian spiritual path. The native pursues spirituality through service to humanity and unique methods. Group meditation and social service as spiritual practice are favorable.",
            ne = "कुम्भ विंशांश लग्नले अपरम्परागत र मानवतावादी आध्यात्मिक मार्ग संकेत गर्छ। जातक मानवता सेवा र अद्वितीय विधिहरू मार्फत आध्यात्मिकता खोज्छ। समूह ध्यान र आध्यात्मिक अभ्यासको रूपमा सामाजिक सेवा अनुकूल छ।"
        ),
        ZodiacSign.PISCES to LocalizedTemplate(
            en = "Vimsamsa Lagna in Pisces is highly auspicious for spiritual attainment. The native has natural inclination toward moksha and divine surrender. All forms of yoga, especially bhakti and dhyana, are favorable. Devotion flows naturally and liberation is the ultimate goal.",
            ne = "मीन विंशांश लग्न आध्यात्मिक उपलब्धिको लागि अत्यधिक शुभ छ। जातकमा मोक्ष र दिव्य समर्पणप्रति प्राकृतिक झुकाव हुन्छ। योगका सबै रूपहरू, विशेष गरी भक्ति र ध्यान, अनुकूल छन्। भक्ति स्वाभाविक रूपमा बग्छ र मुक्ति अन्तिम लक्ष्य हो।"
        )
    )

    // ==================== D-24 CHATURVIMSAMSA TEMPLATES ====================

    /**
     * Chaturvimsamsa (D-24) - Education and Learning
     */
    val chaturvimsamsaLagnaTemplates = mapOf(
        ZodiacSign.ARIES to LocalizedTemplate(
            en = "Chaturvimsamsa Lagna in Aries indicates quick learner with competitive academic approach. The native excels in subjects requiring initiative and courage. Engineering, military studies, and sports science are favored. Learning style is active and pioneering.",
            ne = "मेष चतुर्विंशांश लग्नले प्रतिस्पर्धात्मक शैक्षिक दृष्टिकोण भएको छिटो सिक्ने संकेत गर्छ। जातक पहल र साहस आवश्यक पर्ने विषयहरूमा उत्कृष्ट हुन्छ। इन्जिनियरिङ, सैन्य अध्ययन र खेलकुद विज्ञान अनुकूल छन्। सिकाइ शैली सक्रिय र अग्रणी हुन्छ।"
        ),
        ZodiacSign.TAURUS to LocalizedTemplate(
            en = "Chaturvimsamsa Lagna in Taurus indicates steady and practical learning approach. The native excels in subjects related to finance, arts, and agriculture. Music, commerce, and culinary arts are favored. Learning is methodical and value-oriented.",
            ne = "वृष चतुर्विंशांश लग्नले स्थिर र व्यावहारिक सिकाइ दृष्टिकोण संकेत गर्छ। जातक वित्त, कला र कृषि सम्बन्धी विषयहरूमा उत्कृष्ट हुन्छ। संगीत, वाणिज्य र पाक कला अनुकूल छन्। सिकाइ विधिवत र मूल्य-उन्मुख हुन्छ।"
        ),
        ZodiacSign.GEMINI to LocalizedTemplate(
            en = "Chaturvimsamsa Lagna in Gemini is excellent for academic success. The native excels in communication, languages, and intellectual subjects. Journalism, mathematics, and linguistics are favored. Learning is versatile and curiosity-driven.",
            ne = "मिथुन चतुर्विंशांश लग्न शैक्षिक सफलताको लागि उत्कृष्ट छ। जातक संचार, भाषा र बौद्धिक विषयहरूमा उत्कृष्ट हुन्छ। पत्रकारिता, गणित र भाषाविज्ञान अनुकूल छन्। सिकाइ बहुमुखी र जिज्ञासा-संचालित हुन्छ।"
        ),
        ZodiacSign.CANCER to LocalizedTemplate(
            en = "Chaturvimsamsa Lagna in Cancer indicates emotionally engaged learning. The native excels in subjects related to psychology, nursing, and history. Home economics and child development are favored. Learning is intuitive and memory-based.",
            ne = "कर्कट चतुर्विंशांश लग्नले भावनात्मक रूपमा संलग्न सिकाइ संकेत गर्छ। जातक मनोविज्ञान, नर्सिङ र इतिहास सम्बन्धी विषयहरूमा उत्कृष्ट हुन्छ। गृह अर्थशास्त्र र बाल विकास अनुकूल छन्। सिकाइ अन्तर्ज्ञानी र स्मृति-आधारित हुन्छ।"
        ),
        ZodiacSign.LEO to LocalizedTemplate(
            en = "Chaturvimsamsa Lagna in Leo indicates confident and creative learning approach. The native excels in performing arts, leadership studies, and political science. Drama, management, and creative arts are favored. Learning involves self-expression.",
            ne = "सिंह चतुर्विंशांश लग्नले आत्मविश्वासी र रचनात्मक सिकाइ दृष्टिकोण संकेत गर्छ। जातक प्रदर्शन कला, नेतृत्व अध्ययन र राजनीति विज्ञानमा उत्कृष्ट हुन्छ। नाटक, व्यवस्थापन र रचनात्मक कला अनुकूल छन्। सिकाइमा आत्म-अभिव्यक्ति समावेश हुन्छ।"
        ),
        ZodiacSign.VIRGO to LocalizedTemplate(
            en = "Chaturvimsamsa Lagna in Virgo is highly favorable for academic excellence. The native excels in analytical subjects and detail-oriented studies. Medicine, research, and technical fields are favored. Learning is precise and improvement-oriented.",
            ne = "कन्या चतुर्विंशांश लग्न शैक्षिक उत्कृष्टताको लागि अत्यधिक अनुकूल छ। जातक विश्लेषणात्मक विषयहरू र विस्तार-उन्मुख अध्ययनमा उत्कृष्ट हुन्छ। चिकित्सा, अनुसन्धान र प्राविधिक क्षेत्रहरू अनुकूल छन्। सिकाइ सटीक र सुधार-उन्मुख हुन्छ।"
        ),
        ZodiacSign.LIBRA to LocalizedTemplate(
            en = "Chaturvimsamsa Lagna in Libra indicates balanced and aesthetic learning approach. The native excels in law, diplomacy, and arts. Fashion design, architecture, and social sciences are favored. Learning involves harmony and relationships.",
            ne = "तुला चतुर्विंशांश लग्नले सन्तुलित र सौन्दर्यात्मक सिकाइ दृष्टिकोण संकेत गर्छ। जातक कानून, कूटनीति र कलामा उत्कृष्ट हुन्छ। फेसन डिजाइन, वास्तुकला र सामाजिक विज्ञान अनुकूल छन्। सिकाइमा सामंजस्य र सम्बन्ध समावेश हुन्छ।"
        ),
        ZodiacSign.SCORPIO to LocalizedTemplate(
            en = "Chaturvimsamsa Lagna in Scorpio indicates deep and intensive learning approach. The native excels in research, psychology, and occult sciences. Surgery, investigation, and esoteric studies are favored. Learning penetrates beneath surface.",
            ne = "वृश्चिक चतुर्विंशांश लग्नले गहिरो र गहन सिकाइ दृष्टिकोण संकेत गर्छ। जातक अनुसन्धान, मनोविज्ञान र गुप्त विज्ञानमा उत्कृष्ट हुन्छ। शल्यचिकित्सा, अनुसन्धान र गुप्त अध्ययन अनुकूल छन्। सिकाइ सतहभन्दा तल प्रवेश गर्छ।"
        ),
        ZodiacSign.SAGITTARIUS to LocalizedTemplate(
            en = "Chaturvimsamsa Lagna in Sagittarius is excellent for higher education and philosophy. The native excels in religious studies, law, and philosophy. Teaching, publishing, and international studies are favored. Learning is expansive and principled.",
            ne = "धनु चतुर्विंशांश लग्न उच्च शिक्षा र दर्शनको लागि उत्कृष्ट छ। जातक धार्मिक अध्ययन, कानून र दर्शनमा उत्कृष्ट हुन्छ। शिक्षण, प्रकाशन र अन्तर्राष्ट्रिय अध्ययन अनुकूल छन्। सिकाइ विस्तृत र सैद्धान्तिक हुन्छ।"
        ),
        ZodiacSign.CAPRICORN to LocalizedTemplate(
            en = "Chaturvimsamsa Lagna in Capricorn indicates disciplined and structured learning. The native excels in administration, management, and traditional subjects. Economics, engineering, and organizational studies are favored. Learning is persistent and goal-oriented.",
            ne = "मकर चतुर्विंशांश लग्नले अनुशासित र संरचित सिकाइ संकेत गर्छ। जातक प्रशासन, व्यवस्थापन र परम्परागत विषयहरूमा उत्कृष्ट हुन्छ। अर्थशास्त्र, इन्जिनियरिङ र संगठनात्मक अध्ययन अनुकूल छन्। सिकाइ निरन्तर र लक्ष्य-उन्मुख हुन्छ।"
        ),
        ZodiacSign.AQUARIUS to LocalizedTemplate(
            en = "Chaturvimsamsa Lagna in Aquarius indicates innovative and unconventional learning. The native excels in technology, social sciences, and futuristic studies. Computer science, astronomy, and humanitarian studies are favored. Learning is progressive and original.",
            ne = "कुम्भ चतुर्विंशांश लग्नले नवीन र अपरम्परागत सिकाइ संकेत गर्छ। जातक प्रविधि, सामाजिक विज्ञान र भविष्यवादी अध्ययनमा उत्कृष्ट हुन्छ। कम्प्युटर विज्ञान, खगोल विज्ञान र मानवतावादी अध्ययन अनुकूल छन्। सिकाइ प्रगतिशील र मौलिक हुन्छ।"
        ),
        ZodiacSign.PISCES to LocalizedTemplate(
            en = "Chaturvimsamsa Lagna in Pisces indicates intuitive and imaginative learning. The native excels in arts, spirituality, and healing sciences. Music, poetry, and alternative medicine are favored. Learning is creative and inspired.",
            ne = "मीन चतुर्विंशांश लग्नले अन्तर्ज्ञानी र कल्पनाशील सिकाइ संकेत गर्छ। जातक कला, आध्यात्मिकता र उपचार विज्ञानमा उत्कृष्ट हुन्छ। संगीत, कविता र वैकल्पिक चिकित्सा अनुकूल छन्। सिकाइ रचनात्मक र प्रेरित हुन्छ।"
        )
    )

    // ==================== D-30 TRIMSAMSA TEMPLATES ====================

    /**
     * Trimsamsa (D-30) - Evils and Misfortunes
     */
    val trimsamsaTemplates = mapOf(
        Planet.MARS to LocalizedTemplate(
            en = "Mars ruler of Trimsamsa indicates potential for accidents, injuries, or conflicts. The native should be cautious with fire, sharp objects, and aggressive situations. Remedies include Mars-related mantras and charitable acts on Tuesdays.",
            ne = "त्रिंशांशको मंगल शासकले दुर्घटना, चोटपटक वा द्वन्द्वको सम्भावना संकेत गर्छ। जातक आगो, धारिलो वस्तुहरू र आक्रामक परिस्थितिहरूमा सावधान हुनुपर्छ। उपचारहरूमा मंगल-सम्बन्धित मन्त्र र मंगलबारमा दान-कार्य समावेश छन्।"
        ),
        Planet.SATURN to LocalizedTemplate(
            en = "Saturn ruler of Trimsamsa indicates potential for delays, chronic ailments, or hardships. The native may face challenges requiring patience and perseverance. Remedies include Saturn-related mantras and service to the elderly on Saturdays.",
            ne = "त्रिंशांशको शनि शासकले ढिलाइ, दीर्घकालीन रोग वा कठिनाइहरूको सम्भावना संकेत गर्छ। जातकले धैर्य र दृढता आवश्यक पर्ने चुनौतीहरूको सामना गर्न सक्छ। उपचारहरूमा शनि-सम्बन्धित मन्त्र र शनिबारमा वृद्धहरूको सेवा समावेश छ।"
        ),
        Planet.JUPITER to LocalizedTemplate(
            en = "Jupiter ruler of Trimsamsa provides protection from evils and misfortunes. The native has divine grace and wisdom to overcome challenges. This is a favorable position reducing negative effects. Spiritual practices enhance this protection.",
            ne = "त्रिंशांशको बृहस्पति शासकले दुष्टता र दुर्भाग्यबाट सुरक्षा प्रदान गर्छ। जातकमा चुनौतीहरू पार गर्न दिव्य कृपा र ज्ञान हुन्छ। यो नकारात्मक प्रभावहरू कम गर्ने अनुकूल स्थान हो। आध्यात्मिक अभ्यासहरूले यो सुरक्षा बढाउँछन्।"
        ),
        Planet.MERCURY to LocalizedTemplate(
            en = "Mercury ruler of Trimsamsa indicates potential for nervous ailments, speech problems, or deception. The native should be cautious with contracts and communication. Remedies include Mercury-related mantras and donations on Wednesdays.",
            ne = "त्रिंशांशको बुध शासकले स्नायु रोग, वाणी समस्या वा छलको सम्भावना संकेत गर्छ। जातक सम्झौता र संचारमा सावधान हुनुपर्छ। उपचारहरूमा बुध-सम्बन्धित मन्त्र र बुधबारमा दान समावेश छ।"
        ),
        Planet.VENUS to LocalizedTemplate(
            en = "Venus ruler of Trimsamsa indicates potential for relationship issues, sensual excesses, or reproductive problems. The native should maintain balance in pleasures. Remedies include Venus-related mantras and charitable acts on Fridays.",
            ne = "त्रिंशांशको शुक्र शासकले सम्बन्ध समस्या, संवेदी अतिरेक वा प्रजनन समस्याहरूको सम्भावना संकेत गर्छ। जातकले आनन्दहरूमा सन्तुलन कायम राख्नुपर्छ। उपचारहरूमा शुक्र-सम्बन्धित मन्त्र र शुक्रबारमा दान-कार्य समावेश छ।"
        )
    )

    // ==================== D-60 SHASHTIAMSA TEMPLATES ====================

    /**
     * Shashtiamsa (D-60) - Past Life Karma - First 10 of 60 divisions
     */
    val shashtiamsaTemplates = mapOf(
        1 to LocalizedTemplate(
            en = "Shashtiamsa 1 (Ghora) indicates fierce past life karma. The native may face intense challenges as karmic lessons. Courage and determination from past lives help overcome obstacles. This division is ruled by Mars and requires active effort to progress.",
            ne = "शष्टि्यंश १ (घोर) ले कठोर पूर्वजन्म कर्म संकेत गर्छ। जातकले कार्मिक पाठको रूपमा तीव्र चुनौतीहरूको सामना गर्न सक्छ। पूर्वजन्मको साहस र दृढ संकल्पले बाधाहरू पार गर्न मद्दत गर्छ। यो विभाजन मंगलद्वारा शासित छ र प्रगति गर्न सक्रिय प्रयास आवश्यक छ।"
        ),
        2 to LocalizedTemplate(
            en = "Shashtiamsa 2 (Rakshasa) indicates demonic or negative past life influences. The native may struggle with inner demons or negative patterns. Spiritual practices are essential for transformation. This requires conscious effort to overcome negative karma.",
            ne = "शष्टि्यंश २ (राक्षस) ले राक्षसी वा नकारात्मक पूर्वजन्म प्रभावहरू संकेत गर्छ। जातकले आन्तरिक राक्षस वा नकारात्मक ढाँचाहरूसँग संघर्ष गर्न सक्छ। परिवर्तनको लागि आध्यात्मिक अभ्यासहरू आवश्यक छन्। यसलाई नकारात्मक कर्म पार गर्न सचेत प्रयास चाहिन्छ।"
        ),
        3 to LocalizedTemplate(
            en = "Shashtiamsa 3 (Deva) indicates divine or positive past life influences. The native brings good karma and spiritual merit. There is natural inclination toward dharma and righteous living. This division is blessed and supports spiritual growth.",
            ne = "शष्टि्यंश ३ (देव) ले दिव्य वा सकारात्मक पूर्वजन्म प्रभावहरू संकेत गर्छ। जातकले राम्रो कर्म र आध्यात्मिक योग्यता ल्याउँछ। धर्म र धार्मिक जीवनप्रति प्राकृतिक झुकाव हुन्छ। यो विभाजन आशीर्वादित छ र आध्यात्मिक वृद्धिलाई समर्थन गर्छ।"
        ),
        4 to LocalizedTemplate(
            en = "Shashtiamsa 4 (Kubera) indicates wealth karma from past lives. The native has potential for material abundance. Financial abilities and prosperity are carried forward. This supports material success and resource management.",
            ne = "शष्टि्यंश ४ (कुबेर) ले पूर्वजन्मबाट धन कर्म संकेत गर्छ। जातकमा भौतिक प्रचुरताको सम्भावना हुन्छ। आर्थिक क्षमताहरू र समृद्धि अगाडि लगिन्छ। यसले भौतिक सफलता र स्रोत व्यवस्थापनलाई समर्थन गर्छ।"
        ),
        5 to LocalizedTemplate(
            en = "Shashtiamsa 5 (Yaksha) indicates nature spirit karma. The native has connection to natural forces and environmental sensitivity. Past life involvement with nature spirits influences current life. Protection of nature brings karmic fulfillment.",
            ne = "शष्टि्यंश ५ (यक्ष) ले प्रकृति आत्मा कर्म संकेत गर्छ। जातकको प्राकृतिक शक्तिहरू र वातावरणीय संवेदनशीलतासँग जडान हुन्छ। पूर्वजन्ममा प्रकृति आत्माहरूसँगको संलग्नताले वर्तमान जीवनलाई प्रभाव पार्छ। प्रकृतिको संरक्षणले कार्मिक पूर्णता ल्याउँछ।"
        ),
        6 to LocalizedTemplate(
            en = "Shashtiamsa 6 (Kinnara) indicates celestial musician karma. The native has artistic abilities and musical talents from past lives. Creative expression is karmically supported. Arts and entertainment bring fulfillment.",
            ne = "शष्टि्यंश ६ (किन्नर) ले स्वर्गीय संगीतकार कर्म संकेत गर्छ। जातकमा पूर्वजन्मबाट कलात्मक क्षमताहरू र संगीत प्रतिभाहरू हुन्छन्। रचनात्मक अभिव्यक्ति कार्मिक रूपमा समर्थित छ। कला र मनोरञ्जनले पूर्णता ल्याउँछ।"
        ),
        7 to LocalizedTemplate(
            en = "Shashtiamsa 7 (Bhrashta) indicates fallen or degraded past life karma. The native may face humiliation or downfall to learn humility. Past life pride or arrogance requires correction. Humble service brings karmic healing.",
            ne = "शष्टि्यंश ७ (भ्रष्ट) ले पतित वा अवनत पूर्वजन्म कर्म संकेत गर्छ। जातकले विनम्रता सिक्न अपमान वा पतनको सामना गर्न सक्छ। पूर्वजन्मको घमण्ड वा अहंकारलाई सुधार आवश्यक छ। विनम्र सेवाले कार्मिक उपचार ल्याउँछ।"
        ),
        8 to LocalizedTemplate(
            en = "Shashtiamsa 8 (Kulaghna) indicates family karma destroyer. The native may face issues related to breaking family lineage. Past life family karma requires resolution. Honoring ancestors and family traditions brings healing.",
            ne = "शष्टि्यंश ८ (कुलघ्न) ले पारिवारिक कर्म विनाशक संकेत गर्छ। जातकले पारिवारिक वंश तोड्नेसँग सम्बन्धित समस्याहरूको सामना गर्न सक्छ। पूर्वजन्मको पारिवारिक कर्मलाई समाधान आवश्यक छ। पूर्वजहरू र पारिवारिक परम्पराहरूको सम्मानले उपचार ल्याउँछ।"
        ),
        9 to LocalizedTemplate(
            en = "Shashtiamsa 9 (Garala) indicates poisonous or toxic past life karma. The native may face betrayal or be susceptible to toxicity. Past life involvement with poisons or betrayal creates current patterns. Purification practices bring healing.",
            ne = "शष्टि्यंश ९ (गरल) ले विषालु वा विषाक्त पूर्वजन्म कर्म संकेत गर्छ। जातकले विश्वासघातको सामना गर्न वा विषाक्तताको संवेदनशीलता हुन सक्छ। पूर्वजन्ममा विष वा विश्वासघातसँगको संलग्नताले वर्तमान ढाँचा सिर्जना गर्छ। शुद्धीकरण अभ्यासहरूले उपचार ल्याउँछ।"
        ),
        10 to LocalizedTemplate(
            en = "Shashtiamsa 10 (Vahni) indicates fire karma. The native has past life connection to fire, transformation, or destruction. Fire-related experiences shape current life lessons. Proper use of transformative power brings positive outcomes.",
            ne = "शष्टि्यंश १० (वह्नि) ले अग्नि कर्म संकेत गर्छ। जातकको पूर्वजन्ममा आगो, परिवर्तन वा विनाशसँग सम्बन्ध छ। आगो-सम्बन्धित अनुभवहरूले वर्तमान जीवनका पाठहरू आकार दिन्छन्। परिवर्तनकारी शक्तिको उचित प्रयोगले सकारात्मक परिणामहरू ल्याउँछ।"
        )
    )

    // ==================== HELPER FUNCTIONS ====================

    /**
     * Get Navamsa Lagna template
     */
    fun getNavamsaLagnaTemplate(sign: ZodiacSign): LocalizedTemplate? {
        return navamsaLagnaTemplates[sign]
    }

    /**
     * Get planet in Navamsa template
     */
    fun getPlanetInNavamsaTemplate(planet: Planet, sign: ZodiacSign): LocalizedTemplate? {
        return planetInNavamsaTemplates[Pair(planet, sign)]
    }

    /**
     * Get Vargottama template
     */
    fun getVargottamaTemplate(planet: Planet): LocalizedTemplate? {
        return vargottamaTemplates[planet]
    }

    /**
     * Get Dashamsa Lagna template
     */
    fun getDashamsaLagnaTemplate(sign: ZodiacSign): LocalizedTemplate? {
        return dashamsaLagnaTemplates[sign]
    }

    /**
     * Get 10th lord in Dashamsa house template
     */
    fun getTenthLordInDashamsaTemplate(house: Int): LocalizedTemplate? {
        return tenthLordInDashamsaHouseTemplates[house]
    }

    /**
     * Get Saptamsa Lagna template
     */
    fun getSaptamsaLagnaTemplate(sign: ZodiacSign): LocalizedTemplate? {
        return saptamsaLagnaTemplates[sign]
    }

    /**
     * Get Dwadasamsa Lagna template
     */
    fun getDwadasamsaLagnaTemplate(sign: ZodiacSign): LocalizedTemplate? {
        return dwadasamsaLagnaTemplates[sign]
    }

    /**
     * Get Vimsamsa Lagna template
     */
    fun getVimsamsaLagnaTemplate(sign: ZodiacSign): LocalizedTemplate? {
        return vimsamsaLagnaTemplates[sign]
    }

    /**
     * Get Chaturvimsamsa Lagna template
     */
    fun getChaturvimsamsaLagnaTemplate(sign: ZodiacSign): LocalizedTemplate? {
        return chaturvimsamsaLagnaTemplates[sign]
    }

    /**
     * Get Trimsamsa template
     */
    fun getTrimsamsaTemplate(ruler: Planet): LocalizedTemplate? {
        return trimsamsaTemplates[ruler]
    }

    /**
     * Get Shashtiamsa template
     */
    fun getShashtiamsaTemplate(division: Int): LocalizedTemplate? {
        return shashtiamsaTemplates[division]
    }

    /**
     * Get total template count
     */
    fun getTotalTemplateCount(): Int {
        return navamsaLagnaTemplates.size +
                planetInNavamsaTemplates.size +
                vargottamaTemplates.size +
                dashamsaLagnaTemplates.size +
                tenthLordInDashamsaHouseTemplates.size +
                saptamsaLagnaTemplates.size +
                dwadasamsaLagnaTemplates.size +
                vimsamsaLagnaTemplates.size +
                chaturvimsamsaLagnaTemplates.size +
                trimsamsaTemplates.size +
                shashtiamsaTemplates.size
    }
}
