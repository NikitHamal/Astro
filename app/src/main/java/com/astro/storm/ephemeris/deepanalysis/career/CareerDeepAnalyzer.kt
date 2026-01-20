package com.astro.storm.ephemeris.deepanalysis.career

import com.astro.storm.core.model.Planet
import com.astro.storm.core.model.VedicChart
import com.astro.storm.core.model.ZodiacSign
import com.astro.storm.ephemeris.deepanalysis.*

/**
 * Career Deep Analyzer - Comprehensive profession and career analysis
 * 
 * Integrates 10th house analysis, Dashamsha (D10), career yogas,
 * profession matching, and timing predictions.
 */
object CareerDeepAnalyzer {
    
    fun analyze(chart: VedicChart, context: AnalysisContext): CareerDeepResult {
        val tenthHouseAnalysis = analyzeTenthHouse(context)
        val tenthLordAnalysis = analyzeTenthLord(context)
        val dashamshAnalysis = analyzeDashamsha(context)
        val careerYogas = getCareerYogas(context)
        val suitableProfessions = matchProfessions(context)
        val careerStrengths = getCareerStrengths(context)
        val careerChallenges = getCareerChallenges(context)
        val workStyle = analyzeWorkStyle(context)
        val employmentType = analyzeEmploymentType(context)
        val careerTimeline = generateCareerTimeline(context)
        val currentPhase = analyzeCurrentCareerPhase(context)
        val earningPotential = calculateEarningPotential(context)
        val earningPatterns = getEarningPatterns(context)
        val summary = generateCareerSummary(context, suitableProfessions, earningPotential)
        val score = calculateCareerScore(context)
        
        return CareerDeepResult(
            tenthHouseAnalysis = tenthHouseAnalysis,
            tenthLordAnalysis = tenthLordAnalysis,
            sixthHouseAnalysis = analyzeSixthHouse(context),
            d10Analysis = dashamshAnalysis,
            primaryCareerSignificators = getPrimaryCareerSignificators(context),
            bestCareerPaths = suitableProfessions,
            careerStrengths = careerStrengths,
            careerChallenges = careerChallenges,
            careerYogas = careerYogas,
            careerTimeline = careerTimeline,
            currentCareerPhase = currentPhase,
            careerSummary = summary,
            careerStrengthScore = score,
            workStyle = workStyle
        )
    }
    
    private fun analyzeTenthHouse(context: AnalysisContext): TenthHouseCareerAnalysis {
        val tenthSign = getTenthHouseSign(context)
        val planetsIn10th = context.getPlanetsInHouse(10).map { pos ->
            PlanetInHouseAnalysis(
                planet = pos.planet,
                dignity = context.getDignity(pos.planet),
                effectOnHouse = CareerTextGenerator.getPlanetIn10thEffect(pos.planet)
            )
        }
        
        val strength = context.getHouseStrength(10)
        
        return TenthHouseCareerAnalysis(
            sign = tenthSign,
            planetsInHouse = planetsIn10th,
            houseStrength = strength,
            publicImage = CareerTextGenerator.getPublicImage(tenthSign, strength),
            careerEnvironment = CareerTextGenerator.getCareerEnvironment(tenthSign),
            authorityDynamics = CareerTextGenerator.getAuthorityDynamics(tenthSign, planetsIn10th)
        )
    }

    private fun analyzeSixthHouse(context: AnalysisContext): SixthHouseCareerAnalysis {
        val sixthSign = ZodiacSign.entries[(context.ascendantSign.ordinal + 5) % 12]
        val planetsIn6th = context.getPlanetsInHouse(6).map { pos ->
            PlanetInHouseAnalysis(pos.planet, context.getDignity(pos.planet),
                LocalizedParagraph("${pos.planet.displayName} in 6th affects work and service.",
                    "${pos.planet.displayName} छैठौंमा काम र सेवालाई प्रभाव पार्छ।"))
        }
        
        return SixthHouseCareerAnalysis(
            sign = sixthSign,
            planetsInHouse = planetsIn6th,
            houseStrength = context.getHouseStrength(6),
            serviceQuotient = if (context.getPlanetStrengthLevel(Planet.SATURN) >= StrengthLevel.MODERATE)
                StrengthLevel.STRONG else StrengthLevel.MODERATE,
            workEnvironment = LocalizedParagraph("Daily work environment follows ${sixthSign.displayName}.",
                "दैनिक कार्य वातावरण ${sixthSign.displayName} पछ्याउँछ।"),
            competitionHandling = LocalizedParagraph("How you handle professional competition and obstacles.",
                "तपाईं कसरी व्यावसायिक प्रतिस्पर्धा र अवरोधहरू सामना गर्नुहुन्छ।")
        )
    }
    
    private fun getTenthHouseSign(context: AnalysisContext): ZodiacSign {
        val ascIndex = context.ascendantSign.ordinal
        return ZodiacSign.entries[(ascIndex + 9) % 12]
    }
    
    private fun analyzeTenthLord(context: AnalysisContext): HouseLordDeepAnalysis {
        val lord = context.getHouseLord(10)
        val lordPos = context.getPlanetPosition(lord)
        val housePos = lordPos?.house ?: 10
        
        return HouseLordDeepAnalysis(
            lord = lord,
            housePosition = housePos,
            signPosition = lordPos?.sign ?: context.ascendantSign,
            dignity = context.getDignity(lord),
            strengthLevel = context.getPlanetStrengthLevel(lord),
            interpretation = CareerTextGenerator.get10thLordInHouse(lord, housePos),
            effectOnHouseMatters = CareerTextGenerator.get10thLordEffect(lord, housePos, context.getDignity(lord))
        )
    }
    
    private fun analyzeDashamsha(context: AnalysisContext): DashamshaAnalysis {
        val d10 = context.getDashamsha()
        val d10Asc = d10.ascendantSign
        val tenthLord = context.getHouseLord(10)
        
        return DashamshaAnalysis(
            d10AscendantSign = d10Asc,
            d10AscendantLord = context.getPlanetPosition(tenthLord)?.planet ?: Planet.SUN,
            planetsInD10Houses = emptyList(), // Not fully implemented in core yet
            statusAndAuthority = CareerTextGenerator.getD10CareerRefinement(d10Asc),
            professionalSuccess = CareerTextGenerator.getD10GrowthPattern(d10Asc, ZodiacSign.entries[(d10Asc.ordinal + 9) % 12]),
            karmicDirectionInCareer = LocalizedParagraph("D10 chart indicates your soul's professional path.",
                "D10 कुण्डलीले तपाईंको आत्माको व्यावसायिक मार्ग संकेत गर्छ।"),
            overallD10Strength = context.getHouseStrength(10)
        )
    }
    
    private fun getAspectsToHouse(house: Int, context: AnalysisContext): List<AspectAnalysis> {
        val aspects = mutableListOf<AspectAnalysis>()
        
        context.chart.planetPositions.forEach { pos ->
            val aspectingHouses = getAspectedHouses(pos.planet, pos.house)
            if (house in aspectingHouses) {
                aspects.add(AspectAnalysis(
                    aspectingPlanet = pos.planet,
                    aspectType = if (pos.house == house) AspectType.CONJUNCTION else AspectType.SEVENTH_ASPECT,
                    aspectStrength = context.getPlanetStrengthLevel(pos.planet).value.toDouble() / 5,
                    effect = CareerTextGenerator.getAspectEffect(pos.planet, house)
                ))
            }
        }
        
        return aspects
    }
    
    private fun getAspectedHouses(planet: Planet, fromHouse: Int): List<Int> {
        val houses = mutableListOf<Int>()
        houses.add((fromHouse + 6) % 12 + 1) // 7th aspect
        
        when (planet) {
            Planet.MARS -> {
                houses.add((fromHouse + 3) % 12 + 1) // 4th
                houses.add((fromHouse + 7) % 12 + 1) // 8th
            }
            Planet.JUPITER -> {
                houses.add((fromHouse + 4) % 12 + 1) // 5th
                houses.add((fromHouse + 8) % 12 + 1) // 9th
            }
            Planet.SATURN -> {
                houses.add((fromHouse + 2) % 12 + 1) // 3rd
                houses.add((fromHouse + 9) % 12 + 1) // 10th
            }
            else -> {}
        }
        
        return houses
    }
    
    private fun getPrimaryCareerSignificators(context: AnalysisContext): List<PlanetaryCareerInfluence> {
        val influencers = mutableListOf<PlanetaryCareerInfluence>()
        
        val tenthLord = context.getHouseLord(10)
        influencers.add(PlanetaryCareerInfluence(
            planet = tenthLord,
            strengthLevel = context.getPlanetStrengthLevel(tenthLord),
            contribution = CareerTextGenerator.getTenthLordIndicator(tenthLord, context.getPlanetStrengthLevel(tenthLord)).en.let { LocalizedParagraph(it, "") },
            favorableRoles = listOf("Professional leadership in related fields")
        ))
        
        val sun = Planet.SUN
        influencers.add(PlanetaryCareerInfluence(
            planet = sun,
            strengthLevel = context.getPlanetStrengthLevel(sun),
            contribution = LocalizedParagraph("Sun provides authority and administrative capacity.", "सूर्यले अधिकार र प्रशासनिक क्षमता प्रदान गर्दछ।"),
            favorableRoles = listOf("Government", "Administration", "Management")
        ))
        
        return influencers
    }
    
    private fun getCareerYogas(context: AnalysisContext): List<CareerYoga> {
        val yogas = mutableListOf<CareerYoga>()
        
        context.rajaYogas.forEach { yoga ->
            yogas.add(CareerYoga(
                name = yoga.name,
                strength = yoga.strength.toStrengthLevel(),
                careerEffect = LocalizedParagraph(
                    en = "${yoga.name} enhances your professional success and brings recognition.",
                    ne = "${yoga.name}ले तपाईंको व्यावसायिक सफलता बढाउँछ र मान्यता ल्याउँछ।"
                ),
                involvedPlanets = yoga.planets
            ))
        }
        
        return yogas.take(5)
    }
    
    private fun matchProfessions(context: AnalysisContext): List<ProfessionMatch> {
        val matches = mutableListOf<ProfessionMatch>()
        val tenthSign = getTenthHouseSign(context)
        val tenthLord = context.getHouseLord(10)
        val sunStrength = context.getPlanetStrengthLevel(Planet.SUN)
        
        // Government/Administration
        if (sunStrength >= StrengthLevel.STRONG || tenthLord == Planet.SUN) {
            matches.add(ProfessionMatch(
                professionCategory = ProfessionCategory.GOVERNMENT_ADMINISTRATION,
                specificRoles = listOf("Civil Services", "Administration", "Politics", "Diplomacy"),
                suitabilityScore = (sunStrength.value * 18.0).coerceAtMost(90.0),
                reasonForMatch = LocalizedParagraph(
                    en = "Strong Sun indicates natural authority and success in government roles.",
                    ne = "बलियो सूर्यले सरकारी भूमिकाहरूमा प्राकृतिक अधिकार र सफलता संकेत गर्छ।"
                )
            ))
        }
        
        // Technology
        val mercuryStrength = context.getPlanetStrengthLevel(Planet.MERCURY)
        if (mercuryStrength >= StrengthLevel.MODERATE || tenthSign in listOf(ZodiacSign.GEMINI, ZodiacSign.VIRGO, ZodiacSign.AQUARIUS)) {
            matches.add(ProfessionMatch(
                professionCategory = ProfessionCategory.TECHNOLOGY_IT,
                specificRoles = listOf("Software Development", "Data Analysis", "IT Management", "Technical Writing"),
                suitabilityScore = (mercuryStrength.value * 17.0).coerceAtMost(85.0),
                reasonForMatch = LocalizedParagraph(
                    en = "Mercury's influence supports analytical and technical careers.",
                    ne = "बुधको प्रभावले विश्लेषणात्मक र प्राविधिक क्यारियरहरूलाई समर्थन गर्छ।"
                )
            ))
        }
        
        // Business
        if (context.dhanaYogas.isNotEmpty() || tenthSign in listOf(ZodiacSign.TAURUS, ZodiacSign.LIBRA)) {
            matches.add(ProfessionMatch(
                professionCategory = ProfessionCategory.BUSINESS_COMMERCE,
                specificRoles = listOf("Entrepreneurship", "Trading", "Retail", "E-commerce"),
                suitabilityScore = 75.0,
                reasonForMatch = LocalizedParagraph(
                    en = "Dhana yogas and Venus influence support business success.",
                    ne = "धन योगहरू र शुक्रको प्रभावले व्यापार सफलतालाई समर्थन गर्छ।"
                )
            ))
        }
        
        // Add more based on Mars, Jupiter, Venus strengths
        val marsStrength = context.getPlanetStrengthLevel(Planet.MARS)
        if (marsStrength >= StrengthLevel.STRONG) {
            matches.add(ProfessionMatch(
                professionCategory = ProfessionCategory.ENGINEERING_TECHNICAL,
                specificRoles = listOf("Engineering", "Construction", "Military", "Sports"),
                suitabilityScore = (marsStrength.value * 16.0).coerceAtMost(80.0),
                reasonForMatch = LocalizedParagraph(
                    en = "Strong Mars supports careers requiring physical energy and technical skills.",
                    ne = "बलियो मंगलले शारीरिक ऊर्जा र प्राविधिक कौशल आवश्यक पर्ने क्यारियरहरूलाई समर्थन गर्छ।"
                )
            ))
        }
        
        val jupiterStrength = context.getPlanetStrengthLevel(Planet.JUPITER)
        if (jupiterStrength >= StrengthLevel.MODERATE) {
            matches.add(ProfessionMatch(
                professionCategory = ProfessionCategory.EDUCATION_TEACHING,
                specificRoles = listOf("Teaching", "Consulting", "Counseling", "Training"),
                suitabilityScore = (jupiterStrength.value * 16.0).coerceAtMost(80.0),
                reasonForMatch = LocalizedParagraph(
                    en = "Jupiter's blessing supports educating and guiding others.",
                    ne = "बृहस्पतिको आशीर्वादले अरूलाई शिक्षित र मार्गदर्शन गर्न समर्थन गर्छ।"
                )
            ))
        }
        
        return matches.sortedByDescending { it.suitabilityScore }.take(5)
    }
    
    private fun getCareerStrengths(context: AnalysisContext): List<LocalizedTrait> {
        val strengths = mutableListOf<LocalizedTrait>()
        
        val tenthLordStrength = context.getPlanetStrengthLevel(context.getHouseLord(10))
        if (tenthLordStrength >= StrengthLevel.STRONG) {
            strengths.add(LocalizedTrait("Strong career lord", "बलियो क्यारियर स्वामी", tenthLordStrength))
        }
        
        if (context.rajaYogas.isNotEmpty()) {
            strengths.add(LocalizedTrait("Raja Yoga presence", "राज योग उपस्थिति", StrengthLevel.STRONG))
        }
        
        val sunStrength = context.getPlanetStrengthLevel(Planet.SUN)
        if (sunStrength >= StrengthLevel.STRONG) {
            strengths.add(LocalizedTrait("Leadership ability", "नेतृत्व क्षमता", sunStrength))
        }
        
        val saturnStrength = context.getPlanetStrengthLevel(Planet.SATURN)
        if (saturnStrength >= StrengthLevel.STRONG) {
            strengths.add(LocalizedTrait("Disciplined work ethic", "अनुशासित कार्य नैतिकता", saturnStrength))
        }
        
        return strengths.take(5)
    }
    
    private fun getCareerChallenges(context: AnalysisContext): List<LocalizedTrait> {
        val challenges = mutableListOf<LocalizedTrait>()
        
        val tenthLordStrength = context.getPlanetStrengthLevel(context.getHouseLord(10))
        if (tenthLordStrength <= StrengthLevel.WEAK) {
            challenges.add(LocalizedTrait("Career direction clarity", "क्यारियर दिशा स्पष्टता", StrengthLevel.MODERATE))
        }
        
        val saturnStrength = context.getPlanetStrengthLevel(Planet.SATURN)
        if (saturnStrength <= StrengthLevel.WEAK) {
            challenges.add(LocalizedTrait("Patience in career building", "क्यारियर निर्माणमा धैर्य", StrengthLevel.MODERATE))
        }
        
        return challenges.take(3)
    }
    
    private fun analyzeWorkStyle(context: AnalysisContext): WorkStyleProfile {
        val asc = context.ascendantSign
        val tenthSign = getTenthHouseSign(context)
        
        return WorkStyleProfile(
            preferredEnvironment = CareerTextGenerator.getWorkEnvironment(tenthSign),
            leadershipStyle = CareerTextGenerator.getWorkLeadershipStyle(asc, context.getPlanetStrengthLevel(Planet.SUN)),
            teamworkApproach = CareerTextGenerator.getTeamworkApproach(asc),
            problemSolvingStyle = CareerTextGenerator.getProblemSolvingStyle(context.getPlanetStrengthLevel(Planet.MERCURY)),
            stressHandling = CareerTextGenerator.getStressHandling(asc)
        )
    }
    
    private fun analyzeEmploymentType(context: AnalysisContext): EmploymentTypeAnalysis {
        val tenthLordHouse = context.getPlanetHouse(context.getHouseLord(10))
        val saturnHouse = context.getPlanetHouse(Planet.SATURN)
        
        return EmploymentTypeAnalysis(
            serviceVsBusiness = CareerTextGenerator.getServiceVsBusiness(tenthLordHouse),
            employeeVsEmployer = CareerTextGenerator.getEmployeeVsEmployer(context.getPlanetStrengthLevel(Planet.SUN)),
            partnershipPotential = CareerTextGenerator.getPartnershipPotential(context.getHouseStrength(7)),
            foreignEmploymentPotential = CareerTextGenerator.getForeignPotential(context.getPlanetHouse(Planet.RAHU))
        )
    }
    
    private fun generateCareerTimeline(context: AnalysisContext): List<CareerTimingPeriod> {
        val timeline = mutableListOf<CareerTimingPeriod>()
        
        context.dashaTimeline.mahadashas.take(3).forEach { mahadasha ->
            timeline.add(CareerTimingPeriod(
                startDate = mahadasha.startDate.toLocalDate(),
                endDate = mahadasha.endDate.toLocalDate(),
                dasha = "${mahadasha.planet.displayName} Mahadasha",
                careerFocus = CareerTextGenerator.getDashaCareerFocus(mahadasha.planet),
                opportunities = emptyList(),
                challenges = emptyList(),
                favorability = context.getPlanetStrengthLevel(mahadasha.planet)
            ))
        }
        
        return timeline
    }
    
    private fun analyzeCurrentCareerPhase(context: AnalysisContext): CurrentCareerPhase {
        val currentDasha = context.currentMahadasha
        val planet = currentDasha?.planet ?: Planet.SUN
        
        return CurrentCareerPhase(
            currentDasha = "${planet.displayName} Mahadasha",
            careerOutlook = context.getPlanetStrengthLevel(planet),
            currentFocus = CareerTextGenerator.getDashaCareerFocus(planet),
            advice = CareerTextGenerator.getCareerAdvice(planet)
        )
    }
    
    private fun calculateEarningPotential(context: AnalysisContext): StrengthLevel {
        val dhanaYogaCount = context.dhanaYogas.size
        val secondLordStrength = context.getPlanetStrengthLevel(context.getHouseLord(2))
        val eleventhLordStrength = context.getPlanetStrengthLevel(context.getHouseLord(11))
        
        val avgStrength = (secondLordStrength.value + eleventhLordStrength.value) / 2.0 + (dhanaYogaCount * 0.5)
        
        return when {
            avgStrength >= 4.5 -> StrengthLevel.EXCELLENT
            avgStrength >= 3.5 -> StrengthLevel.STRONG
            avgStrength >= 2.5 -> StrengthLevel.MODERATE
            avgStrength >= 1.5 -> StrengthLevel.WEAK
            else -> StrengthLevel.AFFLICTED
        }
    }
    
    private fun getEarningPatterns(context: AnalysisContext): LocalizedParagraph {
        val secondLord = context.getHouseLord(2)
        val eleventhLord = context.getHouseLord(11)
        
        return LocalizedParagraph(
            en = "Your earning pattern is influenced by ${secondLord.displayName} (2nd lord) and " +
                "${eleventhLord.displayName} (11th lord). Income flows through ${getIncomeSource(context)}.",
            ne = "तपाईंको आय ढाँचा ${secondLord.displayName} (दोस्रो स्वामी) र " +
                "${eleventhLord.displayName} (एघारौं स्वामी) द्वारा प्रभावित छ।"
        )
    }
    
    private fun getIncomeSource(context: AnalysisContext): String {
        val tenthLord = context.getHouseLord(10)
        return when (tenthLord) {
            Planet.SUN -> "authority positions and government"
            Planet.MOON -> "public dealings and nurturing professions"
            Planet.MARS -> "technical work and competitive fields"
            Planet.MERCURY -> "communication and intellectual work"
            Planet.JUPITER -> "teaching, counseling, and advisory roles"
            Planet.VENUS -> "arts, luxury, and relationship-based work"
            Planet.SATURN -> "structured work and service industries"
            else -> "varied sources"
        }
    }
    
    private fun generateCareerSummary(context: AnalysisContext, professions: List<ProfessionMatch>, earning: StrengthLevel): LocalizedParagraph {
        val topProfession = professions.firstOrNull()?.professionCategory?.name?.replace("_", " ")?.lowercase() ?: "varied fields"
        
        return LocalizedParagraph(
            en = "Your career path shows strong potential in $topProfession with ${earning.displayName.lowercase()} " +
                "earning capacity. Professional success comes through ${getSuccessPath(context)}.",
            ne = "तपाईंको क्यारियर मार्गले $topProfession मा बलियो क्षमता देखाउँछ ${earning.displayNameNe} " +
                "आय क्षमताको साथ।"
        )
    }
    
    private fun getSuccessPath(context: AnalysisContext): String {
        val sunStrength = context.getPlanetStrengthLevel(Planet.SUN)
        val saturnStrength = context.getPlanetStrengthLevel(Planet.SATURN)
        
        return when {
            sunStrength >= StrengthLevel.STRONG -> "natural leadership and authority"
            saturnStrength >= StrengthLevel.STRONG -> "disciplined effort and patience"
            else -> "consistent effort and skill development"
        }
    }
    
    private fun calculateCareerScore(context: AnalysisContext): Double {
        val tenthLordScore = context.getPlanetStrengthLevel(context.getHouseLord(10)).value * 10
        val sunScore = context.getPlanetStrengthLevel(Planet.SUN).value * 5
        val saturnScore = context.getPlanetStrengthLevel(Planet.SATURN).value * 5
        val yogaBonus = context.rajaYogas.size * 5
        
        return ((tenthLordScore + sunScore + saturnScore + yogaBonus) / 1.5).coerceIn(0.0, 100.0)
    }
    
    private fun com.astro.storm.ephemeris.yoga.YogaStrength.toStrengthLevel(): StrengthLevel = when (this) {
        com.astro.storm.ephemeris.yoga.YogaStrength.EXTREMELY_STRONG -> StrengthLevel.EXTREMELY_STRONG
        com.astro.storm.ephemeris.yoga.YogaStrength.VERY_STRONG -> StrengthLevel.VERY_STRONG
        com.astro.storm.ephemeris.yoga.YogaStrength.STRONG -> StrengthLevel.STRONG
        com.astro.storm.ephemeris.yoga.YogaStrength.MODERATE -> StrengthLevel.MODERATE
        com.astro.storm.ephemeris.yoga.YogaStrength.WEAK -> StrengthLevel.WEAK
        com.astro.storm.ephemeris.yoga.YogaStrength.VERY_WEAK -> StrengthLevel.AFFLICTED
    }
}
