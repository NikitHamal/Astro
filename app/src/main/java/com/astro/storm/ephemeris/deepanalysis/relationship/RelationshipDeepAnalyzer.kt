package com.astro.storm.ephemeris.deepanalysis.relationship

import com.astro.storm.core.model.Planet
import com.astro.storm.core.model.VedicChart
import com.astro.storm.core.model.ZodiacSign
import com.astro.storm.ephemeris.deepanalysis.*
import java.time.LocalDate

/**
 * Relationship Deep Analyzer - Marriage and partnership analysis
 */
object RelationshipDeepAnalyzer {
    
    fun analyze(chart: VedicChart, context: AnalysisContext): RelationshipDeepResult {
        val seventhSign = getSeventhHouseSign(context)
        val seventhLord = context.getHouseLord(7)
        
        return RelationshipDeepResult(
            seventhHouseAnalysis = analyzeSeventhHouse(context, seventhSign),
            seventhLordAnalysis = analyzeSeventhLord(context, seventhLord),
            venusAnalysis = analyzeVenus(context),
            jupiterAnalysis = analyzeJupiterForMarriage(context),
            partnerProfile = generatePartnerProfile(context),
            relationshipStyle = analyzeRelationshipStyle(context),
            marriageYogas = getMarriageYogas(context),
            relationshipChallenges = getRelationshipChallenges(context),
            navamshaAnalysis = analyzeNavamsha(context),
            marriageTiming = analyzeMarriageTiming(context),
            relationshipTimeline = generateTimeline(context),
            marriageQualityIndicators = analyzeMarriageQuality(context),
            relationshipSummary = generateSummary(context),
            relationshipStrengthScore = calculateScore(context)
        )
    }
    
    private fun getSeventhHouseSign(context: AnalysisContext): ZodiacSign {
        return ZodiacSign.entries[(context.ascendantSign.ordinal + 6) % 12]
    }
    
    private fun analyzeSeventhHouse(context: AnalysisContext, sign: ZodiacSign): SeventhHouseDeepAnalysis {
        val planetsIn7th = context.getPlanetsInHouse(7).map { pos ->
            PlanetInHouseAnalysis(pos.planet, context.getDignity(pos.planet),
                LocalizedParagraph("${pos.planet.displayName} in 7th house influences partnerships.",
                    "${pos.planet.displayName} सातौं भावमा साझेदारीलाई प्रभाव पार्छ।"))
        }
        
        return SeventhHouseDeepAnalysis(
            sign = sign,
            planetsInHouse = planetsIn7th,
            aspectsReceived = emptyList(),
            houseStrength = context.getHouseStrength(7),
            partnershipEnvironment = LocalizedParagraph(
                "${sign.displayName} in 7th house shapes your partnership dynamics.",
                "${sign.displayName} सातौं भावमा तपाईंको साझेदारी गतिशीलता आकार दिन्छ।"),
            publicDealings = LocalizedParagraph(
                "Your public dealings reflect ${sign.displayName}'s qualities.",
                "तपाईंको सार्वजनिक व्यवहारले ${sign.displayName}का गुणहरू प्रतिबिम्बित गर्छ।")
        )
    }
    
    private fun analyzeSeventhLord(context: AnalysisContext, lord: Planet): HouseLordDeepAnalysis {
        val pos = context.getPlanetPosition(lord)
        return HouseLordDeepAnalysis(
            lord = lord,
            housePosition = pos?.house ?: 7,
            signPosition = pos?.sign ?: context.ascendantSign,
            dignity = context.getDignity(lord),
            strengthLevel = context.getPlanetStrengthLevel(lord),
            interpretation = LocalizedParagraph(
                "7th lord ${lord.displayName} shapes your partnership karma.",
                "सातौं स्वामी ${lord.displayName}ले तपाईंको साझेदारी कर्म आकार दिन्छ।"),
            effectOnHouseMatters = LocalizedParagraph(
                "Marriage matters are influenced by ${lord.displayName}'s placement.",
                "विवाह मामिलाहरू ${lord.displayName}को स्थानले प्रभावित छन्।")
        )
    }
    
    private fun analyzeVenus(context: AnalysisContext): VenusDeepAnalysis {
        val venus = context.getPlanetPosition(Planet.VENUS)
        return VenusDeepAnalysis(
            sign = venus?.sign ?: context.ascendantSign,
            house = venus?.house ?: 1,
            nakshatra = venus?.nakshatra ?: context.moonPosition?.nakshatra!!,
            dignity = context.getDignity(Planet.VENUS),
            strengthLevel = context.getPlanetStrengthLevel(Planet.VENUS),
            romanticNature = LocalizedParagraph("Your romantic nature is shaped by Venus in ${venus?.sign?.displayName ?: "your chart"}.",
                "तपाईंको रोमान्टिक स्वभाव ${venus?.sign?.displayName ?: "तपाईंको कुण्डली"}मा शुक्रले आकार दिएको छ।"),
            attractionStyle = LocalizedParagraph("You attract partners through ${venus?.sign?.displayName ?: "Venus"}'s qualities.",
                "तपाईं ${venus?.sign?.displayName ?: "शुक्र"}का गुणहरू मार्फत साथीहरूलाई आकर्षित गर्नुहुन्छ।"),
            pleasureSeeking = LocalizedParagraph("Pleasure and beauty are experienced through Venus's placement.",
                "आनन्द र सौन्दर्य शुक्रको स्थान मार्फत अनुभव गरिन्छ।"),
            loveExpression = LocalizedParagraph("You express love in ${venus?.sign?.displayName ?: "Venus"}-characteristic ways.",
                "तपाईं ${venus?.sign?.displayName ?: "शुक्र"}-विशेषता तरिकाले प्रेम व्यक्त गर्नुहुन्छ।")
        )
    }
    
    private fun analyzeJupiterForMarriage(context: AnalysisContext): JupiterMarriageAnalysis {
        val jupiter = context.getPlanetPosition(Planet.JUPITER)
        return JupiterMarriageAnalysis(
            sign = jupiter?.sign ?: context.ascendantSign,
            house = jupiter?.house ?: 1,
            dignity = context.getDignity(Planet.JUPITER),
            husbandIndicator = LocalizedParagraph("Jupiter indicates spouse qualities for female natives.",
                "बृहस्पतिले महिला जातकहरूको लागि जीवनसाथीको गुणहरू संकेत गर्छ।"),
            blessingsInMarriage = LocalizedParagraph("Jupiter's blessings support marriage harmony.",
                "बृहस्पतिको आशीर्वादले विवाह सामंजस्यलाई समर्थन गर्छ।")
        )
    }
    
    private fun generatePartnerProfile(context: AnalysisContext): PartnerProfile {
        val seventhSign = getSeventhHouseSign(context)
        return PartnerProfile(
            physicalDescription = LocalizedParagraph(
                "Your partner may have ${seventhSign.displayName}-type physical characteristics.",
                "तपाईंको साथीसँग ${seventhSign.displayName}-प्रकारको शारीरिक विशेषताहरू हुन सक्छ।"),
            personalityTraits = listOf(LocalizedTrait("${seventhSign.displayName} traits", "${seventhSign.displayName} गुणहरू", StrengthLevel.MODERATE)),
            professionIndicators = LocalizedParagraph("Partner's profession aligns with 7th house indications.",
                "साथीको पेशा सातौं भाव संकेतहरूसँग मिल्दछ।"),
            backgroundIndicators = LocalizedParagraph("Partner's background reflects chart indicators.",
                "साथीको पृष्ठभूमि कुण्डली सूचकहरू प्रतिबिम्बित गर्छ।"),
            meetingCircumstances = LocalizedParagraph("Meeting may occur through 7th house related activities.",
                "भेट सातौं भाव सम्बन्धित गतिविधिहरू मार्फत हुन सक्छ।"),
            directionOfMeeting = "Based on 7th lord placement",
            ageRelation = LocalizedParagraph("Partner age relation follows traditional indications.",
                "साथीको उमेर सम्बन्ध परम्परागत संकेतहरू पछ्याउँछ।")
        )
    }
    
    private fun analyzeRelationshipStyle(context: AnalysisContext): RelationshipStyleProfile {
        return RelationshipStyleProfile(
            attachmentStyle = LocalizedParagraph("Your attachment style reflects Moon and Venus placements.",
                "तपाईंको आसक्ति शैलीले चन्द्रमा र शुक्र स्थानहरू प्रतिबिम्बित गर्छ।"),
            communicationInRelationship = LocalizedParagraph("Communication in relationships follows Mercury's influence.",
                "सम्बन्धहरूमा सञ्चार बुधको प्रभाव पछ्याउँछ।"),
            conflictResolution = LocalizedParagraph("Conflict resolution style is shaped by Mars and Saturn.",
                "द्वन्द्व समाधान शैली मंगल र शनिले आकार दिएको छ।"),
            intimacyApproach = LocalizedParagraph("Intimacy approach reflects 8th house and Venus.",
                "घनिष्टता दृष्टिकोण आठौं भाव र शुक्र प्रतिबिम्बित गर्छ।"),
            loyaltyPattern = LocalizedParagraph("Loyalty patterns follow 7th lord and Saturn placements.",
                "वफादारी ढाँचाहरू सातौं स्वामी र शनि स्थानहरू पछ्याउँछ।")
        )
    }
    
    private fun getMarriageYogas(context: AnalysisContext): List<MarriageYoga> {
        val yogas = mutableListOf<MarriageYoga>()
        val venusStrength = context.getPlanetStrengthLevel(Planet.VENUS)
        if (venusStrength >= StrengthLevel.STRONG) {
            yogas.add(MarriageYoga("Strong Venus", venusStrength,
                LocalizedParagraph("Strong Venus blesses harmonious relationships.",
                    "बलियो शुक्रले सामंजस्यपूर्ण सम्बन्धहरूलाई आशीर्वाद दिन्छ।"), true))
        }
        return yogas
    }
    
    private fun getRelationshipChallenges(context: AnalysisContext): List<RelationshipChallenge> {
        val challenges = mutableListOf<RelationshipChallenge>()
        val venusStrength = context.getPlanetStrengthLevel(Planet.VENUS)
        if (venusStrength <= StrengthLevel.WEAK) {
            challenges.add(RelationshipChallenge("Venus Weakness", venusStrength,
                LocalizedParagraph("Venus requires strengthening for relationship harmony.",
                    "सम्बन्ध सामंजस्यको लागि शुक्रलाई बलियो बनाउनु आवश्यक छ।"),
                LocalizedParagraph("Venus remedies can help.", "शुक्र उपायहरूले मद्दत गर्न सक्छ।")))
        }
        return challenges
    }
    
    private fun analyzeNavamsha(context: AnalysisContext): NavamshaMarriageAnalysis {
        val d9 = context.getNavamshaChart()
        return NavamshaMarriageAnalysis(
            d9Ascendant = d9.ascendantSign,
            d9VenusPosition = D9PlanetPosition(Planet.VENUS, d9.planetPositions.find { it.planet == Planet.VENUS }?.sign ?: d9.ascendantSign,
                d9.planetPositions.find { it.planet == Planet.VENUS }?.house ?: 1,
                LocalizedParagraph("D9 Venus refines relationship qualities.", "D9 शुक्रले सम्बन्ध गुणहरू परिष्कृत गर्छ।")),
            d9SeventhLordPosition = D9PlanetPosition(context.getHouseLord(7), d9.ascendantSign, 1,
                LocalizedParagraph("D9 7th lord shows marriage refinement.", "D9 सातौं स्वामीले विवाह परिष्करण देखाउँछ।")),
            marriageQualityFromD9 = LocalizedParagraph("Navamsha confirms marriage quality indicators.",
                "नवांशले विवाह गुणस्तर सूचकहरू पुष्टि गर्छ।"),
            spouseNatureFromD9 = LocalizedParagraph("D9 reveals deeper spouse characteristics.",
                "D9 ले गहिरो जीवनसाथी विशेषताहरू प्रकट गर्छ।")
        )
    }
    
    private fun analyzeMarriageTiming(context: AnalysisContext): MarriageTimingAnalysis {
        val venusStrength = context.getPlanetStrengthLevel(Planet.VENUS)
        val category = when {
            venusStrength >= StrengthLevel.STRONG -> MarriageTimingCategory.NORMAL
            venusStrength == StrengthLevel.MODERATE -> MarriageTimingCategory.NORMAL
            else -> MarriageTimingCategory.DELAYED
        }
        
        return MarriageTimingAnalysis(
            timingCategory = category,
            estimatedAgeRange = when (category) {
                MarriageTimingCategory.EARLY -> "21-25"
                MarriageTimingCategory.NORMAL -> "25-30"
                MarriageTimingCategory.DELAYED -> "30-35"
                else -> "Variable"
            },
            favorablePeriods = listOf(TimingPeriod(LocalDate.now(), LocalDate.now().plusYears(2),
                LocalizedParagraph("Current period shows marriage potential.", "वर्तमान अवधिले विवाह सम्भावना देखाउँछ।"),
                StrengthLevel.MODERATE)),
            challengingPeriods = emptyList(),
            currentPeriodAnalysis = LocalizedParagraph("Current dasha influences marriage timing.",
                "वर्तमान दशाले विवाह समयलाई प्रभाव पार्छ।"),
            timingAdvice = LocalizedParagraph("Follow dasha and transit triggers for optimal timing.",
                "इष्टतम समयको लागि दशा र गोचर ट्रिगरहरू पछ्याउनुहोस्।")
        )
    }
    
    private fun generateTimeline(context: AnalysisContext): List<RelationshipTimingPeriod> {
        return context.dashaTimeline.mahadashas.take(2).map { dasha ->
            RelationshipTimingPeriod(
                startDate = dasha.startDate.toLocalDate(),
                endDate = dasha.endDate.toLocalDate(),
                dasha = "${dasha.planet.displayName} Mahadasha",
                relationshipFocus = LocalizedParagraph("${dasha.planet.displayName} period relationship focus.",
                    "${dasha.planet.displayName} अवधि सम्बन्ध फोकस।"),
                favorability = context.getPlanetStrengthLevel(dasha.planet)
            )
        }
    }
    
    private fun analyzeMarriageQuality(context: AnalysisContext): MarriageQualityProfile {
        val seventhStrength = context.getHouseStrength(7)
        return MarriageQualityProfile(
            overallQuality = seventhStrength,
            harmonyIndicators = listOf(LocalizedTrait("7th house harmony", "सातौं भाव सामंजस्य", seventhStrength)),
            challengeIndicators = if (seventhStrength <= StrengthLevel.WEAK) 
                listOf(LocalizedTrait("Relationship work needed", "सम्बन्ध कार्य आवश्यक", StrengthLevel.MODERATE)) else emptyList(),
            growthPotential = LocalizedParagraph("Marriage offers growth through partnership.",
                "विवाहले साझेदारी मार्फत विकास प्रदान गर्छ।"),
            longevityIndicator = LocalizedParagraph("Marriage longevity follows 7th and 8th house indications.",
                "विवाह दीर्घायु सातौं र आठौं भाव संकेतहरू पछ्याउँछ।")
        )
    }
    
    private fun generateSummary(context: AnalysisContext): LocalizedParagraph {
        val venusStrength = context.getPlanetStrengthLevel(Planet.VENUS)
        val seventhStrength = context.getHouseStrength(7)
        return LocalizedParagraph(
            en = "Your relationship potential shows ${seventhStrength.displayName.lowercase()} indications with " +
                "${venusStrength.displayName.lowercase()} Venus influence. Marriage brings ${if (seventhStrength >= StrengthLevel.MODERATE) "harmony and growth" else "learning opportunities"}.",
            ne = "तपाईंको सम्बन्ध क्षमताले ${seventhStrength.displayNameNe} संकेतहरू ${venusStrength.displayNameNe} शुक्र प्रभावको साथ देखाउँछ।"
        )
    }
    
    private fun calculateScore(context: AnalysisContext): Double {
        val venusScore = context.getPlanetStrengthLevel(Planet.VENUS).value * 10
        val seventhScore = context.getHouseStrength(7).value * 10
        val jupiterBonus = if (context.getPlanetStrengthLevel(Planet.JUPITER) >= StrengthLevel.STRONG) 10 else 0
        return ((venusScore + seventhScore + jupiterBonus) / 1.5).coerceIn(0.0, 100.0)
    }
}
