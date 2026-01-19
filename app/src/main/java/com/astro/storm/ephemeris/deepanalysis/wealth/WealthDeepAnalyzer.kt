package com.astro.storm.ephemeris.deepanalysis.wealth

import com.astro.storm.core.model.Planet
import com.astro.storm.core.model.VedicChart
import com.astro.storm.core.model.ZodiacSign
import com.astro.storm.ephemeris.deepanalysis.*

/**
 * Wealth Deep Analyzer - Finance and prosperity analysis
 */
object WealthDeepAnalyzer {
    
    fun analyze(chart: VedicChart, context: AnalysisContext): WealthDeepResult {
        return WealthDeepResult(
            secondHouseAnalysis = analyzeSecondHouse(context),
            eleventhHouseAnalysis = analyzeEleventhHouse(context),
            dhanaYogaAnalysis = analyzeDhanaYogas(context),
            wealthSourceAnalysis = analyzeWealthSources(context),
            financialStrengths = getFinancialStrengths(context),
            financialChallenges = getFinancialChallenges(context),
            induLagnaAnalysis = analyzeInduLagna(context),
            wealthTimeline = generateWealthTimeline(context),
            currentWealthPhase = analyzeCurrentWealthPhase(context),
            investmentIndicators = getInvestmentIndicators(context),
            wealthSummary = generateSummary(context),
            wealthStrengthScore = calculateScore(context)
        )
    }
    
    private fun analyzeSecondHouse(context: AnalysisContext): SecondHouseWealthAnalysis {
        val secondSign = ZodiacSign.entries[(context.ascendantSign.ordinal + 1) % 12]
        val planetsIn2nd = context.getPlanetsInHouse(2).map { pos ->
            PlanetInHouseAnalysis(pos.planet, context.getDignity(pos.planet),
                LocalizedParagraph("${pos.planet.displayName} in 2nd influences accumulated wealth.",
                    "${pos.planet.displayName} दोस्रोमा संचित धनलाई प्रभाव पार्छ।"))
        }
        
        return SecondHouseWealthAnalysis(
            sign = secondSign,
            planetsInHouse = planetsIn2nd,
            houseStrength = context.getHouseStrength(2),
            accumulationPattern = LocalizedParagraph("Wealth accumulation follows ${secondSign.displayName} pattern.",
                "धन संचय ${secondSign.displayName} ढाँचा पछ्याउँछ।"),
            speechAndFamily = LocalizedParagraph("Family wealth and speech connected to 2nd house.",
                "पारिवारिक धन र वाणी दोस्रो भावसँग जोडिएको छ।"),
            savingsAbility = context.getHouseStrength(2)
        )
    }
    
    private fun analyzeEleventhHouse(context: AnalysisContext): EleventhHouseWealthAnalysis {
        val eleventhSign = ZodiacSign.entries[(context.ascendantSign.ordinal + 10) % 12]
        val planetsIn11th = context.getPlanetsInHouse(11).map { pos ->
            PlanetInHouseAnalysis(pos.planet, context.getDignity(pos.planet),
                LocalizedParagraph("${pos.planet.displayName} in 11th enhances gains.",
                    "${pos.planet.displayName} एघारौंमा लाभ बढाउँछ।"))
        }
        
        return EleventhHouseWealthAnalysis(
            sign = eleventhSign,
            planetsInHouse = planetsIn11th,
            houseStrength = context.getHouseStrength(11),
            gainsPattern = LocalizedParagraph("Financial gains follow ${eleventhSign.displayName} pattern.",
                "आर्थिक लाभ ${eleventhSign.displayName} ढाँचा पछ्याउँछ।"),
            incomeStreams = LocalizedParagraph("Multiple income streams indicated by 11th house.",
                "एघारौं भावले बहु आय स्रोतहरू संकेत गर्छ।"),
            networkWealth = LocalizedParagraph("Wealth through networks and connections.",
                "नेटवर्कहरू र सम्बन्धहरू मार्फत धन।")
        )
    }
    
    private fun analyzeDhanaYogas(context: AnalysisContext): DhanaYogaAnalysis {
        val yogas = context.dhanaYogas.map { yoga ->
            DhanaYogaDetail(
                name = yoga.name,
                strength = yoga.strength.toStrengthLevel(),
                wealthEffect = LocalizedParagraph("${yoga.name} contributes to wealth accumulation.",
                    "${yoga.name}ले धन संचयमा योगदान गर्छ।"),
                activationPeriods = LocalizedParagraph("Activates during favorable dasha periods.",
                    "अनुकूल दशा अवधिहरूमा सक्रिय हुन्छ।"),
                involvedPlanets = yoga.involvedPlanets
            )
        }
        
        return DhanaYogaAnalysis(
            presentYogas = yogas,
            overallDhanaStrength = if (yogas.isNotEmpty()) StrengthLevel.STRONG else StrengthLevel.MODERATE,
            wealthPotentialSummary = LocalizedParagraph(
                "${yogas.size} Dhana yoga(s) indicate ${if (yogas.size > 2) "strong" else "moderate"} wealth potential.",
                "${yogas.size} धन योग(हरू)ले ${if (yogas.size > 2) "बलियो" else "मध्यम"} धन क्षमता संकेत गर्छ।")
        )
    }
    
    private fun analyzeWealthSources(context: AnalysisContext): WealthSourceAnalysis {
        val tenthLord = context.getHouseLord(10)
        val secondLord = context.getHouseLord(2)
        
        val sources = mutableListOf<WealthSource>()
        sources.add(WealthSource(
            source = "Career Earnings",
            sourceNe = "क्यारियर आय",
            strength = context.getPlanetStrengthLevel(tenthLord),
            description = LocalizedParagraph("Primary income through career and profession.",
                "क्यारियर र पेशा मार्फत प्राथमिक आय।")
        ))
        
        if (context.getHouseStrength(7) >= StrengthLevel.MODERATE) {
            sources.add(WealthSource(
                source = "Partnership Income",
                sourceNe = "साझेदारी आय",
                strength = context.getHouseStrength(7),
                description = LocalizedParagraph("Income through business partnerships or spouse.",
                    "व्यापार साझेदारी वा जीवनसाथी मार्फत आय।")
            ))
        }
        
        if (context.getHouseStrength(8) >= StrengthLevel.MODERATE) {
            sources.add(WealthSource(
                source = "Inheritance/Unexpected",
                sourceNe = "विरासत/अप्रत्याशित",
                strength = context.getHouseStrength(8),
                description = LocalizedParagraph("Wealth through inheritance or unexpected sources.",
                    "विरासत वा अप्रत्याशित स्रोतहरू मार्फत धन।")
            ))
        }
        
        return WealthSourceAnalysis(
            primarySources = sources,
            secondarySources = emptyList(),
            passiveIncomeIndicators = LocalizedParagraph("Passive income potential from chart indicators.",
                "कुण्डली सूचकहरूबाट निष्क्रिय आय क्षमता।")
        )
    }
    
    private fun getFinancialStrengths(context: AnalysisContext): List<LocalizedTrait> {
        val strengths = mutableListOf<LocalizedTrait>()
        val secondStrength = context.getHouseStrength(2)
        if (secondStrength >= StrengthLevel.STRONG) {
            strengths.add(LocalizedTrait("Savings ability", "बचत क्षमता", secondStrength))
        }
        if (context.dhanaYogas.isNotEmpty()) {
            strengths.add(LocalizedTrait("Wealth yogas present", "धन योगहरू उपस्थित", StrengthLevel.STRONG))
        }
        val jupiterStrength = context.getPlanetStrengthLevel(Planet.JUPITER)
        if (jupiterStrength >= StrengthLevel.STRONG) {
            strengths.add(LocalizedTrait("Jupiter's blessing", "बृहस्पतिको आशीर्वाद", jupiterStrength))
        }
        return strengths.take(5)
    }
    
    private fun getFinancialChallenges(context: AnalysisContext): List<LocalizedTrait> {
        val challenges = mutableListOf<LocalizedTrait>()
        val twelfthStrength = context.getHouseStrength(12)
        if (twelfthStrength >= StrengthLevel.STRONG) {
            challenges.add(LocalizedTrait("Expense tendencies", "खर्च प्रवृत्तिहरू", StrengthLevel.MODERATE))
        }
        if (context.dhanaYogas.isEmpty()) {
            challenges.add(LocalizedTrait("Wealth yoga absent", "धन योग अनुपस्थित", StrengthLevel.MODERATE))
        }
        return challenges.take(3)
    }
    
    private fun analyzeInduLagna(context: AnalysisContext): InduLagnaAnalysis {
        // Simplified Indu Lagna calculation
        val ninthLord = context.getHouseLord(9)
        val moonSign = context.moonSign
        
        return InduLagnaAnalysis(
            induLagnaSign = moonSign,
            induLagnaStrength = context.getHouseStrength(9),
            wealthFromInduLagna = LocalizedParagraph("Indu Lagna indicates wealth accumulation capacity.",
                "इन्दु लग्नले धन संचय क्षमता संकेत गर्छ।"),
            significantPlanets = listOf(ninthLord)
        )
    }
    
    private fun generateWealthTimeline(context: AnalysisContext): List<WealthTimingPeriod> {
        return context.dashaTimeline.mahadashas.take(3).map { dasha ->
            WealthTimingPeriod(
                startDate = dasha.startDate.toLocalDate(),
                endDate = dasha.endDate.toLocalDate(),
                dasha = "${dasha.planet.displayName} Mahadasha",
                wealthFocus = LocalizedParagraph("${dasha.planet.displayName} period wealth focus.",
                    "${dasha.planet.displayName} अवधि धन फोकस।"),
                opportunities = emptyList(),
                cautions = emptyList(),
                favorability = context.getPlanetStrengthLevel(dasha.planet)
            )
        }
    }
    
    private fun analyzeCurrentWealthPhase(context: AnalysisContext): CurrentWealthPhase {
        val dasha = context.currentMahadasha
        return CurrentWealthPhase(
            currentDasha = "${dasha?.planet?.displayName ?: "Current"} Mahadasha",
            overallFinancialOutlook = context.getPlanetStrengthLevel(dasha?.planet ?: Planet.JUPITER),
            currentOpportunities = emptyList(),
            currentCautions = emptyList(),
            financialAdvice = LocalizedParagraph("Align financial decisions with current period energy.",
                "वर्तमान अवधि ऊर्जासँग आर्थिक निर्णयहरू मिलाउनुहोस्।")
        )
    }
    
    private fun getInvestmentIndicators(context: AnalysisContext): InvestmentProfile {
        val mercuryStrength = context.getPlanetStrengthLevel(Planet.MERCURY)
        val saturnStrength = context.getPlanetStrengthLevel(Planet.SATURN)
        
        return InvestmentProfile(
            riskTolerance = if (context.getPlanetStrengthLevel(Planet.MARS) >= StrengthLevel.STRONG)
                StrengthLevel.STRONG else StrengthLevel.MODERATE,
            investmentStyle = LocalizedParagraph("Investment style reflects chart indicators.",
                "लगानी शैलीले कुण्डली सूचकहरू प्रतिबिम्बित गर्छ।"),
            favorableInvestments = listOf(LocalizedTrait("Chart-aligned investments", "कुण्डली-मिलान लगानीहरू", StrengthLevel.MODERATE)),
            cautionaryInvestments = emptyList(),
            timingAdvice = LocalizedParagraph("Time investments with favorable dasha/transit.",
                "अनुकूल दशा/गोचरसँग लगानीहरू समय गर्नुहोस्।")
        )
    }
    
    private fun generateSummary(context: AnalysisContext): LocalizedParagraph {
        val dhanaCount = context.dhanaYogas.size
        val secondStrength = context.getHouseStrength(2)
        
        return LocalizedParagraph(
            en = "Your wealth profile shows ${secondStrength.displayName.lowercase()} accumulation capacity with " +
                "$dhanaCount Dhana yoga(s). Financial success comes through ${getWealthPath(context)}.",
            ne = "तपाईंको धन प्रोफाइलले ${secondStrength.displayNameNe} संचय क्षमता $dhanaCount धन योग(हरू)को साथ देखाउँछ।"
        )
    }
    
    private fun getWealthPath(context: AnalysisContext): String {
        val tenthLord = context.getHouseLord(10)
        return when (tenthLord) {
            Planet.SUN -> "authority and leadership"
            Planet.MOON -> "public dealing and service"
            Planet.MARS -> "technical and competitive fields"
            Planet.MERCURY -> "commerce and communication"
            Planet.JUPITER -> "wisdom and advisory work"
            Planet.VENUS -> "arts and luxury industry"
            Planet.SATURN -> "structured work and service"
            else -> "varied sources"
        }
    }
    
    private fun calculateScore(context: AnalysisContext): Double {
        val secondScore = context.getHouseStrength(2).value * 10
        val eleventhScore = context.getHouseStrength(11).value * 10
        val dhanaBonus = context.dhanaYogas.size * 5
        return ((secondScore + eleventhScore + dhanaBonus) / 1.5).coerceIn(0.0, 100.0)
    }
    
    private fun com.astro.storm.ephemeris.yoga.YogaStrength.toStrengthLevel(): StrengthLevel = when (this) {
        com.astro.storm.ephemeris.yoga.YogaStrength.VERY_STRONG -> StrengthLevel.EXCELLENT
        com.astro.storm.ephemeris.yoga.YogaStrength.STRONG -> StrengthLevel.STRONG
        com.astro.storm.ephemeris.yoga.YogaStrength.MODERATE -> StrengthLevel.MODERATE
        com.astro.storm.ephemeris.yoga.YogaStrength.WEAK -> StrengthLevel.WEAK
        com.astro.storm.ephemeris.yoga.YogaStrength.VERY_WEAK -> StrengthLevel.AFFLICTED
    }
}
