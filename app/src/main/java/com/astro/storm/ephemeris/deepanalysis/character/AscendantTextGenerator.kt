package com.astro.storm.ephemeris.deepanalysis.character

import com.astro.storm.core.model.Planet
import com.astro.storm.core.model.PlanetPosition
import com.astro.storm.core.model.ZodiacSign
import com.astro.storm.ephemeris.deepanalysis.*

/**
 * Ascendant Text Generator - 144+ unique ascendant interpretations
 */
object AscendantTextGenerator {
    
    fun getRisingDegreeAnalysis(sign: ZodiacSign, degree: Double): LocalizedParagraph {
        val degreeRange = when {
            degree % 30 < 10 -> "early"
            degree % 30 < 20 -> "middle"
            else -> "late"
        }
        
        return when (degreeRange) {
            "early" -> LocalizedParagraph(
                en = "Rising in the early degrees of ${sign.displayName}, you express this sign's qualities " +
                    "with youthful energy and pure manifestation. The beginner's enthusiasm marks your approach to life.",
                ne = "${sign.displayName}को प्रारम्भिक अंशहरूमा उदय हुँदा, तपाईंले यो राशिको गुणहरू " +
                    "युवा ऊर्जा र शुद्ध अभिव्यक्तिको साथ व्यक्त गर्नुहुन्छ।"
            )
            "middle" -> LocalizedParagraph(
                en = "Rising in the middle degrees of ${sign.displayName}, you express this sign's qualities " +
                    "in their fullest, most stable form. Maturity and balance characterize your personality.",
                ne = "${sign.displayName}को मध्य अंशहरूमा उदय हुँदा, तपाईंले यो राशिको गुणहरू " +
                    "तिनीहरूको पूर्ण, सबैभन्दा स्थिर रूपमा व्यक्त गर्नुहुन्छ।"
            )
            else -> LocalizedParagraph(
                en = "Rising in the late degrees of ${sign.displayName}, you blend this sign's qualities " +
                    "with hints of the next sign. Wisdom and completion energy mark your approach.",
                ne = "${sign.displayName}को अन्तिम अंशहरूमा उदय हुँदा, तपाईंले यो राशिको गुणहरू " +
                    "अर्को राशिको संकेतहरूसँग मिलाउनुहुन्छ।"
            )
        }
    }
    
    fun getPhysicalAppearance(sign: ZodiacSign): LocalizedParagraph = when (sign) {
        ZodiacSign.ARIES -> LocalizedParagraph(
            en = "You likely have a strong, athletic build with sharp features, prominent forehead, and an energetic bearing. " +
                "Your movements are quick and decisive, reflecting your Mars-ruled nature.",
            ne = "तपाईंसँग सम्भवतः बलियो, एथलेटिक शरीर, तीखा विशेषताहरू, प्रमुख निधार, र ऊर्जावान व्यक्तित्व छ।"
        )
        ZodiacSign.TAURUS -> LocalizedParagraph(
            en = "You tend to have a solid, well-proportioned body with pleasant features and a calm demeanor. " +
                "Your Venus-ruled influence gives you natural attractiveness and graceful movement.",
            ne = "तपाईंसँग ठोस, राम्रो अनुपातको शरीर, सुखद विशेषताहरू र शान्त व्यवहार हुने प्रवृत्ति छ।"
        )
        ZodiacSign.GEMINI -> LocalizedParagraph(
            en = "You typically have a youthful appearance with expressive eyes, quick movements, and animated gestures. " +
                "Your Mercury-ruled nature gives you an alert, curious demeanor.",
            ne = "तपाईंसँग सामान्यतया अभिव्यक्त आँखाहरू, द्रुत चालहरू र एनिमेटेड इशाराहरूको साथ युवा उपस्थिति छ।"
        )
        ZodiacSign.CANCER -> LocalizedParagraph(
            en = "You often have a round face, soft features, and an emotionally expressive countenance. " +
                "Your Moon-ruled nature gives you a nurturing presence that others find comforting.",
            ne = "तपाईंको प्रायः गोलो अनुहार, नरम विशेषताहरू र भावनात्मक रूपमा अभिव्यक्त अनुहार छ।"
        )
        ZodiacSign.LEO -> LocalizedParagraph(
            en = "You typically have a commanding presence with a noble bearing, strong shoulders, and often a distinctive mane of hair. " +
                "Your Sun-ruled nature gives you natural charisma.",
            ne = "तपाईंसँग सामान्यतया उत्कृष्ट व्यवहार, बलियो काँधहरू र प्रायः विशिष्ट कपालको साथ आदेशात्मक उपस्थिति छ।"
        )
        ZodiacSign.VIRGO -> LocalizedParagraph(
            en = "You tend to have neat, well-groomed appearance with refined features and an intelligent expression. " +
                "Your Mercury-ruled nature gives you a practical, discerning demeanor.",
            ne = "तपाईंसँग परिष्कृत विशेषताहरू र बुद्धिमान अभिव्यक्तिको साथ सफा, राम्रोसँग सजाइएको उपस्थिति हुने प्रवृत्ति छ।"
        )
        ZodiacSign.LIBRA -> LocalizedParagraph(
            en = "You often have harmonious, well-balanced features with natural grace and charm. " +
                "Your Venus-ruled nature blesses you with aesthetic appeal and pleasant demeanor.",
            ne = "तपाईंसँग प्रायः प्राकृतिक अनुग्रह र आकर्षणको साथ सामंजस्यपूर्ण, राम्रोसँग सन्तुलित विशेषताहरू छन्।"
        )
        ZodiacSign.SCORPIO -> LocalizedParagraph(
            en = "You typically have intense, penetrating eyes, a magnetic presence, and a mysterious aura. " +
                "Your Mars-Pluto influence gives you powerful physical intensity.",
            ne = "तपाईंसँग सामान्यतया तीव्र, भेदक आँखाहरू, चुम्बकीय उपस्थिति र रहस्यमय आभा छ।"
        )
        ZodiacSign.SAGITTARIUS -> LocalizedParagraph(
            en = "You often have a tall, athletic build with an open, friendly face and optimistic expression. " +
                "Your Jupiter-ruled nature gives you an expansive, jovial presence.",
            ne = "तपाईंसँग प्रायः खुला, मैत्रीपूर्ण अनुहार र आशावादी अभिव्यक्तिको साथ अग्लो, एथलेटिक शरीर छ।"
        )
        ZodiacSign.CAPRICORN -> LocalizedParagraph(
            en = "You tend to have a serious, dignified appearance with strong bone structure and mature features. " +
                "Your Saturn-ruled nature gives you an air of authority and gravitas.",
            ne = "तपाईंसँग बलियो हड्डी संरचना र परिपक्व विशेषताहरूको साथ गम्भीर, मर्यादित उपस्थिति हुने प्रवृत्ति छ।"
        )
        ZodiacSign.AQUARIUS -> LocalizedParagraph(
            en = "You often have distinctive, unconventional features with an intelligent, alert expression. " +
                "Your Saturn-Uranian influence gives you a unique, sometimes eccentric presence.",
            ne = "तपाईंसँग प्रायः बुद्धिमान, सतर्क अभिव्यक्तिको साथ विशिष्ट, अपरम्परागत विशेषताहरू छन्।"
        )
        ZodiacSign.PISCES -> LocalizedParagraph(
            en = "You typically have soft, dreamy features with expressive, soulful eyes and a gentle presence. " +
                "Your Jupiter-Neptune influence gives you an ethereal, artistic quality.",
            ne = "तपाईंसँग सामान्यतया अभिव्यक्त, आत्मिक आँखाहरू र कोमल उपस्थितिको साथ नरम, सपनिलो विशेषताहरू छन्।"
        )
    }
    
    fun getFirstImpression(sign: ZodiacSign): LocalizedParagraph = when (sign) {
        ZodiacSign.ARIES -> LocalizedParagraph(
            en = "Others perceive you as confident, energetic, and ready for action. You make a bold first impression " +
                "that commands attention and respect.",
            ne = "अरूले तपाईंलाई आत्मविश्वासी, ऊर्जावान र कार्यको लागि तयार भनेर बुझ्छन्।"
        )
        ZodiacSign.TAURUS -> LocalizedParagraph(
            en = "Others perceive you as calm, reliable, and grounded. You make a reassuring first impression " +
                "that puts people at ease.",
            ne = "अरूले तपाईंलाई शान्त, भरपर्दो र जमिनमा भनेर बुझ्छन्।"
        )
        ZodiacSign.GEMINI -> LocalizedParagraph(
            en = "Others perceive you as witty, curious, and engaging. You make a lively first impression " +
                "that sparks interesting conversations.",
            ne = "अरूले तपाईंलाई बुद्धिमान, जिज्ञासु र आकर्षक भनेर बुझ्छन्।"
        )
        ZodiacSign.CANCER -> LocalizedParagraph(
            en = "Others perceive you as warm, caring, and emotionally attuned. You make a nurturing first impression " +
                "that creates instant emotional connection.",
            ne = "अरूले तपाईंलाई न्यानो, हेरचाह गर्ने र भावनात्मक रूपमा जोडिएको भनेर बुझ्छन्।"
        )
        ZodiacSign.LEO -> LocalizedParagraph(
            en = "Others perceive you as charismatic, generous, and confident. You make a memorable first impression " +
                "that naturally draws people to you.",
            ne = "अरूले तपाईंलाई करिश्माई, उदार र आत्मविश्वासी भनेर बुझ्छन्।"
        )
        ZodiacSign.VIRGO -> LocalizedParagraph(
            en = "Others perceive you as intelligent, helpful, and detail-oriented. You make a competent first impression " +
                "that inspires trust in your abilities.",
            ne = "अरूले तपाईंलाई बुद्धिमान, सहायक र विस्तार-उन्मुख भनेर बुझ्छन्।"
        )
        ZodiacSign.LIBRA -> LocalizedParagraph(
            en = "Others perceive you as charming, diplomatic, and aesthetically refined. You make a pleasant first impression " +
                "that creates harmony in any situation.",
            ne = "अरूले तपाईंलाई आकर्षक, कूटनीतिक र सौन्दर्य रूपमा परिष्कृत भनेर बुझ्छन्।"
        )
        ZodiacSign.SCORPIO -> LocalizedParagraph(
            en = "Others perceive you as intense, mysterious, and powerful. You make a profound first impression " +
                "that people rarely forget.",
            ne = "अरूले तपाईंलाई तीव्र, रहस्यमय र शक्तिशाली भनेर बुझ्छन्।"
        )
        ZodiacSign.SAGITTARIUS -> LocalizedParagraph(
            en = "Others perceive you as optimistic, adventurous, and philosophical. You make an inspiring first impression " +
                "that uplifts those around you.",
            ne = "अरूले तपाईंलाई आशावादी, साहसिक र दार्शनिक भनेर बुझ्छन्।"
        )
        ZodiacSign.CAPRICORN -> LocalizedParagraph(
            en = "Others perceive you as serious, ambitious, and capable. You make a professional first impression " +
                "that commands respect.",
            ne = "अरूले तपाईंलाई गम्भीर, महत्वाकांक्षी र सक्षम भनेर बुझ्छन्।"
        )
        ZodiacSign.AQUARIUS -> LocalizedParagraph(
            en = "Others perceive you as unique, innovative, and intellectually stimulating. You make an intriguing first impression " +
                "that sparks curiosity.",
            ne = "अरूले तपाईंलाई अद्वितीय, नवीन र बौद्धिक रूपमा उत्तेजक भनेर बुझ्छन्।"
        )
        ZodiacSign.PISCES -> LocalizedParagraph(
            en = "Others perceive you as empathetic, artistic, and spiritually aware. You make a gentle first impression " +
                "that creates deep emotional resonance.",
            ne = "अरूले तपाईंलाई सहानुभूतिशील, कलात्मक र आध्यात्मिक रूपमा जागरूक भनेर बुझ्छन्।"
        )
    }
    
    fun getLifeApproach(sign: ZodiacSign): LocalizedParagraph = when (sign) {
        ZodiacSign.ARIES -> LocalizedParagraph(
            en = "You approach life as a pioneer and warrior. Every challenge is an opportunity to prove yourself, " +
                "and you thrive on competition and new beginnings.",
            ne = "तपाईं जीवनलाई अग्रगामी र योद्धाको रूपमा हेर्नुहुन्छ।"
        )
        ZodiacSign.TAURUS -> LocalizedParagraph(
            en = "You approach life as a builder and preserver. Security and comfort are paramount, " +
                "and you work steadily toward creating a stable, beautiful life.",
            ne = "तपाईं जीवनलाई निर्माता र संरक्षकको रूपमा हेर्नुहुन्छ।"
        )
        ZodiacSign.GEMINI -> LocalizedParagraph(
            en = "You approach life as a student and communicator. Learning and sharing information are your passions, " +
                "and you thrive on variety and mental stimulation.",
            ne = "तपाईं जीवनलाई विद्यार्थी र सञ्चारकर्ताको रूपमा हेर्नुहुन्छ।"
        )
        ZodiacSign.CANCER -> LocalizedParagraph(
            en = "You approach life as a nurturer and protector. Creating emotional security for yourself and loved ones " +
                "is your primary motivation.",
            ne = "तपाईं जीवनलाई पालनपोषणकर्ता र रक्षकको रूपमा हेर्नुहुन्छ।"
        )
        ZodiacSign.LEO -> LocalizedParagraph(
            en = "You approach life as a creator and leader. Self-expression and recognition drive you, " +
                "and you naturally gravitate toward positions of authority.",
            ne = "तपाईं जीवनलाई सिर्जनाकर्ता र नेताको रूपमा हेर्नुहुन्छ।"
        )
        ZodiacSign.VIRGO -> LocalizedParagraph(
            en = "You approach life as a healer and perfectionist. Improving yourself and serving others " +
                "give your life meaning and purpose.",
            ne = "तपाईं जीवनलाई उपचारक र पूर्णतावादीको रूपमा हेर्नुहुन्छ।"
        )
        ZodiacSign.LIBRA -> LocalizedParagraph(
            en = "You approach life as a diplomat and artist. Creating harmony and beauty in all areas " +
                "is your constant pursuit.",
            ne = "तपाईं जीवनलाई कूटनीतिज्ञ र कलाकारको रूपमा हेर्नुहुन्छ।"
        )
        ZodiacSign.SCORPIO -> LocalizedParagraph(
            en = "You approach life as a transformer and investigator. Depth and authenticity matter most, " +
                "and you're not afraid to face life's shadows.",
            ne = "तपाईं जीवनलाई परिवर्तनकर्ता र अनुसन्धानकर्ताको रूपमा हेर्नुहुन्छ।"
        )
        ZodiacSign.SAGITTARIUS -> LocalizedParagraph(
            en = "You approach life as an explorer and philosopher. Seeking truth and expanding horizons " +
                "drive your endless quest for knowledge.",
            ne = "तपाईं जीवनलाई अन्वेषक र दार्शनिकको रूपमा हेर्नुहुन्छ।"
        )
        ZodiacSign.CAPRICORN -> LocalizedParagraph(
            en = "You approach life as an achiever and master builder. Long-term goals and lasting legacy " +
                "motivate your patient, determined efforts.",
            ne = "तपाईं जीवनलाई उपलब्धिकर्ता र मास्टर बिल्डरको रूपमा हेर्नुहुन्छ।"
        )
        ZodiacSign.AQUARIUS -> LocalizedParagraph(
            en = "You approach life as a visionary and humanitarian. Progressive ideas and collective betterment " +
                "fuel your unique contributions to the world.",
            ne = "तपाईं जीवनलाई दूरदर्शी र मानवतावादीको रूपमा हेर्नुहुन्छ।"
        )
        ZodiacSign.PISCES -> LocalizedParagraph(
            en = "You approach life as a mystic and healer. Spiritual connection and compassionate service " +
                "guide your transcendent path through life.",
            ne = "तपाईं जीवनलाई रहस्यवादी र उपचारकको रूपमा हेर्नुहुन्छ।"
        )
    }
    
    fun getOverallInterpretation(sign: ZodiacSign, lord: Planet, context: AnalysisContext): LocalizedParagraph {
        val lordStrength = context.getPlanetStrengthLevel(lord)
        val strengthDesc = when (lordStrength) {
            StrengthLevel.EXCELLENT -> "exceptionally strong"
            StrengthLevel.STRONG -> "well-placed"
            StrengthLevel.MODERATE -> "moderately positioned"
            StrengthLevel.WEAK -> "challenged"
            StrengthLevel.AFFLICTED -> "significantly afflicted"
        }
        
        return LocalizedParagraph(
            en = "Your ${sign.displayName} Ascendant, ruled by ${lord.displayName} which is $strengthDesc, " +
                "shapes your entire life experience. ${getLifeApproach(sign).en} " +
                "This rising sign colors how others perceive you and how you instinctively respond to new situations.",
            ne = "तपाईंको ${sign.displayName} लग्न, ${lord.displayName} द्वारा शासित जुन ${lordStrength.displayNameNe} छ, " +
                "तपाईंको सम्पूर्ण जीवन अनुभव आकार दिन्छ। " +
                "यो उदय राशिले अरूले तपाईंलाई कसरी बुझ्छन् र तपाईं नयाँ परिस्थितिहरूमा कसरी प्रतिक्रिया दिनुहुन्छ भनेर रंग दिन्छ।"
        )
    }
}
