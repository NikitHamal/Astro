package com.astro.storm.ephemeris.deepanalysis.career

import com.astro.storm.core.model.Planet
import com.astro.storm.core.model.ZodiacSign
import com.astro.storm.ephemeris.deepanalysis.*

/**
 * Career Text Generator - Text interpretations for career analysis
 */
object CareerTextGenerator {
    
    fun getPlanetIn10thEffect(planet: Planet): LocalizedParagraph = when (planet) {
        Planet.SUN -> LocalizedParagraph(
            en = "Sun in 10th house brings fame, authority, and government connections. You naturally rise to leadership.",
            ne = "दशौं भावमा सूर्यले प्रसिद्धि, अधिकार र सरकारी सम्बन्धहरू ल्याउँछ।"
        )
        Planet.MOON -> LocalizedParagraph(
            en = "Moon in 10th house brings public recognition and fluctuating career. Good for public-facing roles.",
            ne = "दशौं भावमा चन्द्रमाले सार्वजनिक मान्यता र उतार-चढाव भएको क्यारियर ल्याउँछ।"
        )
        Planet.MARS -> LocalizedParagraph(
            en = "Mars in 10th house gives technical skills, courage in business, and competitive advantage.",
            ne = "दशौं भावमा मंगलले प्राविधिक कौशल, व्यापारमा साहस र प्रतिस्पर्धात्मक फाइदा दिन्छ।"
        )
        Planet.MERCURY -> LocalizedParagraph(
            en = "Mercury in 10th house favors communication-based careers, sales, writing, and commerce.",
            ne = "दशौं भावमा बुधले सञ्चार-आधारित क्यारियर, बिक्री, लेखन र वाणिज्यलाई अनुकूल पार्छ।"
        )
        Planet.JUPITER -> LocalizedParagraph(
            en = "Jupiter in 10th house brings wisdom in career, ethical success, and teacher/advisor roles.",
            ne = "दशौं भावमा बृहस्पतिले क्यारियरमा ज्ञान, नैतिक सफलता र शिक्षक/सल्लाहकार भूमिकाहरू ल्याउँछ।"
        )
        Planet.VENUS -> LocalizedParagraph(
            en = "Venus in 10th house indicates success in arts, entertainment, luxury goods, and diplomacy.",
            ne = "दशौं भावमा शुक्रले कला, मनोरञ्जन, विलासी सामान र कूटनीतिमा सफलता संकेत गर्छ।"
        )
        Planet.SATURN -> LocalizedParagraph(
            en = "Saturn in 10th house brings steady rise through hard work, careers in structure and management.",
            ne = "दशौं भावमा शनिले कठिन परिश्रम मार्फत स्थिर वृद्धि, संरचना र व्यवस्थापनमा क्यारियर ल्याउँछ।"
        )
        Planet.RAHU -> LocalizedParagraph(
            en = "Rahu in 10th house gives unconventional career path, foreign connections, and sudden rises.",
            ne = "दशौं भावमा राहूले अपरम्परागत क्यारियर मार्ग, विदेशी सम्बन्धहरू र अचानक वृद्धि दिन्छ।"
        )
        Planet.KETU -> LocalizedParagraph(
            en = "Ketu in 10th house indicates spiritual career, detachment from fame, or research-based work.",
            ne = "दशौं भावमा केतुले आध्यात्मिक क्यारियर, प्रसिद्धिबाट विरक्ति, वा अनुसन्धान-आधारित कार्य संकेत गर्छ।"
        )
        else -> LocalizedParagraph(
            en = "${planet.displayName} in 10th house shapes your public role and career expression in a unique way.",
            ne = "दशौं भावमा ${planet.displayName}ले तपाईंको सार्वजनिक भूमिका र क्यारियर अभिव्यक्तिलाई अद्वितीय तरिकाले आकार दिन्छ।"
        )
    }
    
    fun getPublicImage(sign: ZodiacSign, strength: StrengthLevel): LocalizedParagraph {
        val signQuality = when (sign) {
            ZodiacSign.ARIES -> "bold, pioneering leader"
            ZodiacSign.TAURUS -> "reliable, steady professional"
            ZodiacSign.GEMINI -> "versatile, communicative expert"
            ZodiacSign.CANCER -> "nurturing, caring professional"
            ZodiacSign.LEO -> "confident, charismatic leader"
            ZodiacSign.VIRGO -> "detail-oriented, skilled specialist"
            ZodiacSign.LIBRA -> "diplomatic, balanced professional"
            ZodiacSign.SCORPIO -> "intense, powerful figure"
            ZodiacSign.SAGITTARIUS -> "wise, expansive leader"
            ZodiacSign.CAPRICORN -> "authoritative, accomplished professional"
            ZodiacSign.AQUARIUS -> "innovative, progressive thinker"
            ZodiacSign.PISCES -> "creative, compassionate professional"
        }
        
        return LocalizedParagraph(
            en = "The public perceives you as a $signQuality. Your professional image is ${strength.displayName.lowercase()}, " +
                "creating ${if (strength >= StrengthLevel.STRONG) "strong opportunities" else "room for growth"}.",
            ne = "सार्वजनिकले तपाईंलाई एक $signQuality को रूपमा बुझ्छ।"
        )
    }
    
    fun getCareerEnvironment(sign: ZodiacSign): LocalizedParagraph = when (sign) {
        ZodiacSign.ARIES -> LocalizedParagraph(
            en = "You thrive in competitive, fast-paced environments that reward initiative and quick action.",
            ne = "तपाईं प्रतिस्पर्धात्मक, तीव्र गतिको वातावरणमा फल्नुहुन्छ जसले पहल र द्रुत कार्यलाई पुरस्कृत गर्छ।"
        )
        ZodiacSign.TAURUS -> LocalizedParagraph(
            en = "You prefer stable, comfortable work environments with clear structure and tangible rewards.",
            ne = "तपाईं स्थिर, आरामदायी कार्य वातावरण स्पष्ट संरचना र ठोस पुरस्कारहरूको साथ रुचाउनुहुन्छ।"
        )
        ZodiacSign.CAPRICORN -> LocalizedParagraph(
            en = "You excel in structured, hierarchical environments where discipline and achievement are valued.",
            ne = "तपाईं संरचित, पदानुक्रमित वातावरणमा उत्कृष्ट प्रदर्शन गर्नुहुन्छ जहाँ अनुशासन र उपलब्धिलाई मूल्य दिइन्छ।"
        )
        else -> LocalizedParagraph(
            en = "Your ideal work environment reflects ${sign.displayName}'s qualities, supporting your natural professional style.",
            ne = "तपाईंको आदर्श कार्य वातावरणले ${sign.displayName}का गुणहरू प्रतिबिम्बित गर्छ।"
        )
    }
    
    fun getAuthorityDynamics(sign: ZodiacSign, planetsIn10th: List<PlanetInHouseAnalysis>): LocalizedParagraph {
        val hasSun = planetsIn10th.any { it.planet == Planet.SUN }
        val hasSaturn = planetsIn10th.any { it.planet == Planet.SATURN }
        
        return when {
            hasSun -> LocalizedParagraph(
                en = "You naturally assume authority and command respect from superiors and subordinates alike.",
                ne = "तपाईं स्वाभाविक रूपमा अधिकार ग्रहण गर्नुहुन्छ र वरिष्ठ र अधीनस्थहरूबाट सम्मान आज्ञा गर्नुहुन्छ।"
            )
            hasSaturn -> LocalizedParagraph(
                en = "You earn authority through demonstrated competence and steady, patient effort over time.",
                ne = "तपाईं प्रदर्शित क्षमता र समयमा स्थिर, धैर्यवान प्रयास मार्फत अधिकार अर्जन गर्नुहुन्छ।"
            )
            else -> LocalizedParagraph(
                en = "Your relationship with authority develops through ${sign.displayName}'s characteristic approach.",
                ne = "अधिकारसँगको तपाईंको सम्बन्ध ${sign.displayName}को विशेषता दृष्टिकोण मार्फत विकास हुन्छ।"
            )
        }
    }
    
    fun get10thLordInHouse(lord: Planet, house: Int): LocalizedParagraph = when (house) {
        1 -> LocalizedParagraph(
            en = "10th lord ${lord.displayName} in 1st house creates self-made success. Your career is tied to your personality.",
            ne = "दशौं स्वामी ${lord.displayName} पहिलो भावमा स्व-निर्मित सफलता सिर्जना गर्छ।"
        )
        2 -> LocalizedParagraph(
            en = "10th lord in 2nd house connects career to family business, finance, or speech-related work.",
            ne = "दोस्रो भावमा दशौं स्वामीले क्यारियरलाई पारिवारिक व्यवसाय, वित्त वा भाषण-सम्बन्धित कार्यसँग जोड्छ।"
        )
        7 -> LocalizedParagraph(
            en = "10th lord in 7th house indicates career through partnerships, public dealings, or spouse's support.",
            ne = "सातौं भावमा दशौं स्वामीले साझेदारी, सार्वजनिक व्यवहार वा जीवनसाथीको समर्थन मार्फत क्यारियर संकेत गर्छ।"
        )
        9 -> LocalizedParagraph(
            en = "10th lord in 9th house indicates luck in career, foreign work, or teaching/advisory professions.",
            ne = "नवौं भावमा दशौं स्वामीले क्यारियरमा भाग्य, विदेशी कार्य वा शिक्षण/सल्लाहकार पेशाहरू संकेत गर्छ।"
        )
        11 -> LocalizedParagraph(
            en = "10th lord in 11th house promises career gains, fulfilled ambitions, and network-based success.",
            ne = "एघारौं भावमा दशौं स्वामीले क्यारियर लाभ, पूरा भएका महत्वाकांक्षाहरू र नेटवर्क-आधारित सफलताको वाचा गर्छ।"
        )
        12 -> LocalizedParagraph(
            en = "10th lord in 12th house indicates foreign career, behind-scenes work, or spiritual professions.",
            ne = "बाह्रौं भावमा दशौं स्वामीले विदेशी क्यारियर, पर्दा-पछाडिको कार्य वा आध्यात्मिक पेशाहरू संकेत गर्छ।"
        )
        else -> LocalizedParagraph(
            en = "10th lord ${lord.displayName} in ${house}th house shapes your career through that house's significations.",
            ne = "दशौं स्वामी ${lord.displayName} ${house}औं भावमा त्यो भावको संकेतहरू मार्फत तपाईंको क्यारियर आकार दिन्छ।"
        )
    }
    
    fun get10thLordEffect(lord: Planet, house: Int, dignity: PlanetaryDignityLevel): LocalizedParagraph {
        val dignityDesc = when (dignity) {
            PlanetaryDignityLevel.EXALTED -> "exceptionally favorable"
            PlanetaryDignityLevel.OWN_SIGN, PlanetaryDignityLevel.MOOLATRIKONA -> "well-supported"
            PlanetaryDignityLevel.FRIEND_SIGN -> "positively influenced"
            PlanetaryDignityLevel.NEUTRAL -> "balanced"
            PlanetaryDignityLevel.ENEMY_SIGN -> "requiring extra effort"
            PlanetaryDignityLevel.DEBILITATED -> "providing growth challenges"
        }
        
        return LocalizedParagraph(
            en = "Your career lord's position is $dignityDesc, ${if (dignity.ordinal <= 2) "enhancing" else "shaping"} " +
                "your professional journey through ${lord.displayName}'s influence from the ${house}th house.",
            ne = "तपाईंको क्यारियर स्वामीको स्थिति ${dignityDesc} छ।"
        )
    }
    
    fun getTenthLordIndicator(lord: Planet, strength: StrengthLevel): LocalizedParagraph {
        return LocalizedParagraph(
            en = "${lord.displayName} as your career significator with ${strength.displayName.lowercase()} strength " +
                "indicates ${if (strength >= StrengthLevel.STRONG) "strong professional potential" else "developing career path"}.",
            ne = "${lord.displayName} तपाईंको क्यारियर महत्वपूर्णको रूपमा ${strength.displayNameNe} बलको साथ।"
        )
    }
    
    fun getPlanetCareerInfluence(planet: Planet, strength: StrengthLevel): LocalizedParagraph = when (planet) {
        Planet.MERCURY -> LocalizedParagraph(
            en = "Mercury's ${strength.displayName.lowercase()} influence supports communication, analysis, and commerce.",
            ne = "बुधको ${strength.displayNameNe} प्रभावले सञ्चार, विश्लेषण र वाणिज्यलाई समर्थन गर्छ।"
        )
        Planet.VENUS -> LocalizedParagraph(
            en = "Venus's ${strength.displayName.lowercase()} influence supports arts, luxury, and relationship-based work.",
            ne = "शुक्रको ${strength.displayNameNe} प्रभावले कला, विलासिता र सम्बन्ध-आधारित कार्यलाई समर्थन गर्छ।"
        )
        Planet.MARS -> LocalizedParagraph(
            en = "Mars's ${strength.displayName.lowercase()} influence supports technical, competitive, and action-oriented work.",
            ne = "मंगलको ${strength.displayNameNe} प्रभावले प्राविधिक, प्रतिस्पर्धात्मक र कार्य-उन्मुख कार्यलाई समर्थन गर्छ।"
        )
        Planet.JUPITER -> LocalizedParagraph(
            en = "Jupiter's ${strength.displayName.lowercase()} influence supports teaching, counseling, and wisdom-based roles.",
            ne = "बृहस्पतिको ${strength.displayNameNe} प्रभावले शिक्षण, परामर्श र ज्ञान-आधारित भूमिकाहरूलाई समर्थन गर्छ।"
        )
        else -> LocalizedParagraph(
            en = "${planet.displayName}'s influence shapes your career in unique ways.",
            ne = "${planet.displayName}को प्रभावले तपाईंको क्यारियरलाई अद्वितीय तरिकाले आकार दिन्छ।"
        )
    }
    
    fun getAspectEffect(planet: Planet, house: Int): LocalizedParagraph {
        return LocalizedParagraph(
            en = "${planet.displayName}'s aspect on ${house}th house ${if (planet in listOf(Planet.JUPITER, Planet.VENUS, Planet.MOON)) "blesses" else "influences"} your career.",
            ne = "${planet.displayName}को ${house}औं भावमा दृष्टिले तपाईंको क्यारियर ${if (planet in listOf(Planet.JUPITER, Planet.VENUS, Planet.MOON)) "आशीर्वाद दिन्छ" else "प्रभाव पार्छ"}।"
        )
    }
    
    fun getD10SunInterpretation(sign: ZodiacSign?, house: Int): LocalizedParagraph {
        return LocalizedParagraph(
            en = "Sun in ${sign?.displayName ?: "this sign"} in D10's ${house}th house refines your professional authority and public recognition potential.",
            ne = "D10को ${house}औं भावमा ${sign?.displayName ?: "यो राशि"}मा सूर्यले तपाईंको व्यावसायिक अधिकार र सार्वजनिक मान्यता क्षमतालाई परिष्कृत गर्छ।"
        )
    }
    
    fun getD10MoonInterpretation(sign: ZodiacSign?, house: Int): LocalizedParagraph {
        return LocalizedParagraph(
            en = "Moon in ${sign?.displayName ?: "this sign"} in D10's ${house}th house shows your emotional connection to career and public image.",
            ne = "D10को ${house}औं भावमा ${sign?.displayName ?: "यो राशि"}मा चन्द्रमाले क्यारियर र सार्वजनिक छविसँगको तपाईंको भावनात्मक सम्बन्ध देखाउँछ।"
        )
    }
    
    fun getD10CareerRefinement(ascendant: ZodiacSign): LocalizedParagraph {
        return LocalizedParagraph(
            en = "${ascendant.displayName} rising in Dashamsha adds specific professional qualities to your career expression.",
            ne = "दशांशमा ${ascendant.displayName} उदयले तपाईंको क्यारियर अभिव्यक्तिमा विशेष व्यावसायिक गुणहरू थप्छ।"
        )
    }
    
    fun getD10GrowthPattern(asc: ZodiacSign, tenth: ZodiacSign): LocalizedParagraph {
        return LocalizedParagraph(
            en = "Your professional growth pattern moves from ${asc.displayName}'s approach toward ${tenth.displayName}'s achievement style.",
            ne = "तपाईंको व्यावसायिक विकास ढाँचा ${asc.displayName}को दृष्टिकोणबाट ${tenth.displayName}को उपलब्धि शैलीतर्फ सर्छ।"
        )
    }
    
    fun getWorkEnvironment(sign: ZodiacSign): LocalizedParagraph {
        return LocalizedParagraph(
            en = "You perform best in a ${sign.displayName}-compatible environment that honors your natural work style.",
            ne = "तपाईं ${sign.displayName}-अनुकूल वातावरणमा सबैभन्दा राम्रो प्रदर्शन गर्नुहुन्छ।"
        )
    }
    
    fun getWorkLeadershipStyle(sign: ZodiacSign, sunStrength: StrengthLevel): LocalizedParagraph {
        return LocalizedParagraph(
            en = "Your leadership style is ${sign.displayName}-influenced with ${sunStrength.displayName.lowercase()} natural authority.",
            ne = "तपाईंको नेतृत्व शैली ${sign.displayName}-प्रभावित ${sunStrength.displayNameNe} प्राकृतिक अधिकारको साथ छ।"
        )
    }
    
    fun getTeamworkApproach(sign: ZodiacSign): LocalizedParagraph {
        return LocalizedParagraph(
            en = "You approach teamwork with ${sign.displayName}'s characteristic style, ${getTeamStyle(sign)}.",
            ne = "तपाईं ${sign.displayName}को विशेषता शैलीको साथ टोली कार्यमा दृष्टिकोण राख्नुहुन्छ।"
        )
    }
    
    private fun getTeamStyle(sign: ZodiacSign): String = when (sign) {
        ZodiacSign.ARIES, ZodiacSign.LEO -> "naturally leading and inspiring others"
        ZodiacSign.GEMINI, ZodiacSign.LIBRA -> "facilitating communication and harmony"
        ZodiacSign.CANCER, ZodiacSign.PISCES -> "nurturing team cohesion"
        else -> "contributing your unique strengths"
    }
    
    fun getProblemSolvingStyle(mercuryStrength: StrengthLevel): LocalizedParagraph {
        return LocalizedParagraph(
            en = "You solve problems with ${mercuryStrength.displayName.lowercase()} analytical ability, " +
                "${if (mercuryStrength >= StrengthLevel.STRONG) "quickly finding innovative solutions" else "developing systematic approaches"}.",
            ne = "तपाईं ${mercuryStrength.displayNameNe} विश्लेषणात्मक क्षमताको साथ समस्याहरू समाधान गर्नुहुन्छ।"
        )
    }
    
    fun getStressHandling(sign: ZodiacSign): LocalizedParagraph {
        return LocalizedParagraph(
            en = "Under work stress, your ${sign.displayName} rising nature ${getStressStyle(sign)}.",
            ne = "कार्य तनावमा, तपाईंको ${sign.displayName} उदय स्वभाव ${getStressStyleNe(sign)}।"
        )
    }
    
    private fun getStressStyle(sign: ZodiacSign): String = when (sign) {
        ZodiacSign.ARIES -> "pushes through with action"
        ZodiacSign.TAURUS -> "stays calm and grounded"
        ZodiacSign.CANCER -> "seeks emotional support"
        ZodiacSign.CAPRICORN -> "doubles down on discipline"
        else -> "adapts using natural coping mechanisms"
    }
    
    private fun getStressStyleNe(sign: ZodiacSign): String = "तनाव व्यवस्थापन गर्छ"
    
    fun getServiceVsBusiness(tenthLordHouse: Int): LocalizedParagraph {
        val isBusiness = tenthLordHouse in listOf(2, 7, 10, 11)
        return LocalizedParagraph(
            en = "Your chart ${if (isBusiness) "supports business ventures and entrepreneurship" else "favors service-oriented careers and employment"}.",
            ne = "तपाईंको कुण्डलीले ${if (isBusiness) "व्यापार उद्यम र उद्यमशीलता समर्थन गर्छ" else "सेवा-उन्मुख क्यारियर र रोजगारलाई अनुकूल गर्छ"}।"
        )
    }
    
    fun getEmployeeVsEmployer(sunStrength: StrengthLevel): LocalizedParagraph {
        return LocalizedParagraph(
            en = if (sunStrength >= StrengthLevel.STRONG) 
                "Strong Sun suggests natural ability to be an employer or leader rather than subordinate." else
                "Career success comes through collaboration with authority figures.",
            ne = if (sunStrength >= StrengthLevel.STRONG)
                "बलियो सूर्यले अधीनस्थभन्दा नियोक्ता वा नेता हुने प्राकृतिक क्षमता सुझाव गर्छ।" else
                "अधिकार व्यक्तिहरूसँग सहकार्य मार्फत क्यारियर सफलता आउँछ।"
        )
    }
    
    fun getPartnershipPotential(seventhStrength: StrengthLevel): LocalizedParagraph {
        return LocalizedParagraph(
            en = "Business partnerships show ${seventhStrength.displayName.lowercase()} potential for success.",
            ne = "व्यापार साझेदारीले सफलताको लागि ${seventhStrength.displayNameNe} क्षमता देखाउँछ।"
        )
    }
    
    fun getForeignPotential(rahuHouse: Int): LocalizedParagraph {
        val isFavorable = rahuHouse in listOf(3, 9, 10, 11, 12)
        return LocalizedParagraph(
            en = "Foreign career opportunities are ${if (isFavorable) "supported" else "possible with effort"} based on Rahu's placement.",
            ne = "विदेशी क्यारियर अवसरहरू राहुको स्थानको आधारमा ${if (isFavorable) "समर्थित" else "प्रयासको साथ सम्भव"} छन्।"
        )
    }
    
    fun getDashaCareerFocus(planet: Planet): LocalizedParagraph = when (planet) {
        Planet.SUN -> LocalizedParagraph(en = "Focus on leadership, authority, and recognition during this period.", ne = "यस अवधिमा नेतृत्व, अधिकार र मान्यतामा ध्यान दिनुहोस्।")
        Planet.MOON -> LocalizedParagraph(en = "Public-facing roles and emotionally fulfilling work are emphasized.", ne = "सार्वजनिक-मुखी भूमिका र भावनात्मक रूपमा सन्तुष्ट कार्यमा जोड दिइएको छ।")
        Planet.MARS -> LocalizedParagraph(en = "Technical achievements, competitive success, and action-oriented career moves.", ne = "प्राविधिक उपलब्धिहरू, प्रतिस्पर्धात्मक सफलता र कार्य-उन्मुख क्यारियर चालहरू।")
        Planet.MERCURY -> LocalizedParagraph(en = "Communication, commerce, and intellectual career developments.", ne = "सञ्चार, वाणिज्य र बौद्धिक क्यारियर विकास।")
        Planet.JUPITER -> LocalizedParagraph(en = "Expansion, teaching, advisory roles, and wisdom-based success.", ne = "विस्तार, शिक्षण, सल्लाहकार भूमिका र ज्ञान-आधारित सफलता।")
        Planet.VENUS -> LocalizedParagraph(en = "Creative pursuits, luxury industry, and relationship-based career growth.", ne = "रचनात्मक खोज, विलासिता उद्योग र सम्बन्ध-आधारित क्यारियर वृद्धि।")
        Planet.SATURN -> LocalizedParagraph(en = "Slow but steady progress, discipline, and long-term career building.", ne = "ढिलो तर स्थिर प्रगति, अनुशासन र दीर्घकालीन क्यारियर निर्माण।")
        Planet.RAHU -> LocalizedParagraph(en = "Unconventional opportunities, foreign connections, and rapid rises.", ne = "अपरम्परागत अवसरहरू, विदेशी सम्बन्धहरू र द्रुत वृद्धि।")
        Planet.KETU -> LocalizedParagraph(en = "Spiritual career direction, research, or detachment from material success.", ne = "आध्यात्मिक क्यारियर दिशा, अनुसन्धान, वा भौतिक सफलताबाट विरक्ति।")
        else -> LocalizedParagraph(en = "Focus on aligning your career with ${planet.displayName}'s energy during this period.", ne = "यस अवधिमा ${planet.displayName}को ऊर्जासँग आफ्नो क्यारियर मिलाउनुहोस्।")
    }
    
    fun getDashaOpportunities(planet: Planet): List<LocalizedTrait> = when (planet) {
        Planet.SUN -> listOf(LocalizedTrait("Leadership roles", "नेतृत्व भूमिकाहरू", StrengthLevel.STRONG))
        Planet.JUPITER -> listOf(LocalizedTrait("Expansion opportunities", "विस्तार अवसरहरू", StrengthLevel.STRONG))
        Planet.VENUS -> listOf(LocalizedTrait("Creative success", "रचनात्मक सफलता", StrengthLevel.STRONG))
        else -> listOf(LocalizedTrait("Growth potential", "विकास क्षमता", StrengthLevel.MODERATE))
    }
    
    fun getDashaChallenges(planet: Planet): List<LocalizedTrait> = when (planet) {
        Planet.SATURN -> listOf(LocalizedTrait("Patience required", "धैर्य आवश्यक", StrengthLevel.MODERATE))
        Planet.RAHU -> listOf(LocalizedTrait("Uncertainty navigation", "अनिश्चितता नेभिगेसन", StrengthLevel.MODERATE))
        Planet.KETU -> listOf(LocalizedTrait("Material detachment", "भौतिक विरक्ति", StrengthLevel.MODERATE))
        else -> listOf(LocalizedTrait("Minor obstacles", "सानो बाधाहरू", StrengthLevel.WEAK))
    }
    
    fun getCurrentPhaseDescription(planet: Planet, context: AnalysisContext): LocalizedParagraph {
        val strength = context.getPlanetStrengthLevel(planet)
        return LocalizedParagraph(
            en = "You are currently in ${planet.displayName} Mahadasha with ${strength.displayName.lowercase()} influence. " +
                "This period ${if (strength >= StrengthLevel.STRONG) "actively supports" else "gradually develops"} your career.",
            ne = "तपाईं हाल ${planet.displayName} महादशामा ${strength.displayNameNe} प्रभावको साथ हुनुहुन्छ।"
        )
    }
    
    fun getCareerAdvice(planet: Planet): LocalizedParagraph = when (planet) {
        Planet.SUN -> LocalizedParagraph(en = "Focus on building authority and taking leadership initiatives.", ne = "अधिकार निर्माण र नेतृत्व पहलहरू लिनमा ध्यान दिनुहोस्।")
        Planet.SATURN -> LocalizedParagraph(en = "Be patient, work diligently, and build long-term foundations.", ne = "धैर्य राख्नुहोस्, परिश्रमपूर्वक काम गर्नुहोस् र दीर्घकालीन आधारहरू बनाउनुहोस्।")
        Planet.JUPITER -> LocalizedParagraph(en = "Seek wisdom-based roles and mentor others when possible.", ne = "ज्ञान-आधारित भूमिकाहरू खोज्नुहोस् र सम्भव हुँदा अरूलाई मार्गदर्शन गर्नुहोस्।")
        else -> LocalizedParagraph(en = "Align your efforts with ${planet.displayName}'s natural significations.", ne = "${planet.displayName}का प्राकृतिक संकेतहरूसँग तपाईंको प्रयासहरू मिलाउनुहोस्।")
    }
}
