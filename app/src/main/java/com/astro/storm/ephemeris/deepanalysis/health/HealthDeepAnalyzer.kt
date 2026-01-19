package com.astro.storm.ephemeris.deepanalysis.health

import com.astro.storm.core.model.Planet
import com.astro.storm.core.model.VedicChart
import com.astro.storm.core.model.ZodiacSign
import com.astro.storm.ephemeris.deepanalysis.*

/**
 * Health Deep Analyzer - Constitutional and health analysis
 */
object HealthDeepAnalyzer {
    
    fun analyze(chart: VedicChart, context: AnalysisContext): HealthDeepResult {
        return HealthDeepResult(
            constitutionAnalysis = analyzeConstitution(context),
            sixthHouseAnalysis = analyzeSixthHouse(context),
            eighthHouseAnalysis = analyzeEighthHouse(context),
            ascendantHealthProfile = analyzeAscendantHealth(context),
            bodyPartMapping = generateBodyPartMapping(context),
            potentialVulnerabilities = getVulnerabilities(context),
            healthStrengths = getHealthStrengths(context),
            preventiveFocus = generatePreventiveFocus(context),
            bodySystemMapping = emptyList(),
            vulnerableAreas = emptyList(),
            planetaryHealthInfluences = analyzePlanetaryHealthInfluences(context),
            longevityIndicators = analyzeLongevity(context),
            healthTimeline = generateHealthTimeline(context),
            currentHealthPhase = analyzeCurrentHealthPhase(context),
            lifestyleRecommendations = emptyList(),
            dietaryRecommendations = emptyList(),
            preventiveMeasures = emptyList(),
            healthSummary = generateSummary(context),
            healthStrengthScore = calculateScore(context)
        )
    }
    
    private fun analyzeConstitution(context: AnalysisContext): ConstitutionAnalysis {
        val dominantElement = context.getDominantElement()
        val doshaType = when (dominantElement) {
            Element.FIRE -> DoshaType.PITTA
            Element.EARTH -> DoshaType.KAPHA
            Element.AIR -> DoshaType.VATA
            Element.WATER -> DoshaType.KAPHA
        }
        
        return ConstitutionAnalysis(
            primaryDosha = doshaType,
            secondaryDosha = when (doshaType) {
                DoshaType.VATA -> DoshaType.PITTA
                DoshaType.PITTA -> DoshaType.KAPHA
                DoshaType.KAPHA -> DoshaType.VATA
            },
            doshaBalance = mapOf(DoshaType.VATA to 33.0, DoshaType.PITTA to 33.0, DoshaType.KAPHA to 34.0),
            constitutionDescription = LocalizedParagraph(
                "Your ${doshaType.name.lowercase().replaceFirstChar { it.uppercase() }}-dominant constitution shapes your health patterns.",
                "तपाईंको ${doshaType.name}-प्रधान संविधानले तपाईंको स्वास्थ्य ढाँचाहरू आकार दिन्छ।"),
            naturalStrengths = emptyList(),
            naturalWeaknesses = emptyList(),
            balancingAdvice = LocalizedParagraph("Diet and lifestyle should balance ${doshaType.name} tendencies.",
                "आहार र जीवनशैलीले ${doshaType.name} प्रवृत्तिहरूलाई सन्तुलित गर्नुपर्छ।")
        )
    }
    
    private fun analyzeSixthHouse(context: AnalysisContext): SixthHouseHealthAnalysis {
        val sixthSign = ZodiacSign.entries[(context.ascendantSign.ordinal + 5) % 12]
        val planetsIn6th = context.getPlanetsInHouse(6).map { pos ->
            PlanetInHouseAnalysis(pos.planet, context.getDignity(pos.planet),
                LocalizedParagraph("${pos.planet.displayName} in 6th influences health patterns.",
                    "${pos.planet.displayName} छैटौंमा स्वास्थ्य ढाँचाहरूलाई प्रभाव पार्छ।"))
        }
        
        return SixthHouseHealthAnalysis(
            sign = sixthSign,
            planetsInHouse = planetsIn6th,
            houseStrength = context.getHouseStrength(6),
            diseaseResistance = context.getHouseStrength(6),
            immuneSystemProfile = LocalizedParagraph("Immune system reflects 6th house indicators.",
                "प्रतिरक्षा प्रणालीले छैटौं भाव सूचकहरू प्रतिबिम्बित गर्छ।"),
            potentialAilments = getAilmentsForSign(sixthSign),
            healingCapacity = LocalizedParagraph("Healing capacity follows 6th house strength.",
                "उपचार क्षमता छैटौं भाव बलता पछ्याउँछ।")
        )
    }
    
    private fun getAilmentsForSign(sign: ZodiacSign): List<LocalizedTrait> = listOf(
        LocalizedTrait("${sign.displayName}-related", "${sign.displayName}-सम्बन्धित", StrengthLevel.MODERATE)
    )
    
    private fun analyzeEighthHouse(context: AnalysisContext): EighthHouseHealthAnalysis {
        val eighthSign = ZodiacSign.entries[(context.ascendantSign.ordinal + 7) % 12]
        return EighthHouseHealthAnalysis(
            sign = eighthSign,
            planetsInHouse = context.getPlanetsInHouse(8).map { pos ->
                PlanetInHouseAnalysis(pos.planet, context.getDignity(pos.planet),
                    LocalizedParagraph("${pos.planet.displayName} in 8th affects vitality.",
                        "${pos.planet.displayName} आठौंमा जीवनी शक्तिलाई असर गर्छ।"))
            },
            houseStrength = context.getHouseStrength(8),
            chronicHealthPatterns = LocalizedParagraph("8th house shows chronic health patterns.",
                "आठौं भावले दीर्घकालीन स्वास्थ्य ढाँचाहरू देखाउँछ।"),
            regenerativeCapacity = LocalizedParagraph("Regenerative capacity follows 8th house.",
                "पुनर्जनन क्षमता आठौं भाव पछ्याउँछ।"),
            criticalPeriodIndicators = LocalizedParagraph("8th house indicates critical health periods.",
                "आठौं भावले महत्वपूर्ण स्वास्थ्य अवधिहरू संकेत गर्छ।")
        )
    }
    
    private fun analyzeAscendantHealth(context: AnalysisContext): AscendantHealthProfile {
        val asc = context.ascendantSign
        val ascLordStrength = context.getPlanetStrengthLevel(context.ascendantLord)
        
        return AscendantHealthProfile(
            generalVitality = ascLordStrength,
            physicalConstitutionType = LocalizedParagraph("${asc.displayName} rising gives characteristic constitution.",
                "${asc.displayName} उदयले विशेषता संविधान दिन्छ।"),
            innateProwess = LocalizedParagraph("Natural physical prowess from ascendant.",
                "लग्नबाट प्राकृतिक शारीरिक शक्ति।"),
            bodyFrame = getBodyFrame(asc),
            metabolicType = getMetabolicType(asc)
        )
    }
    
    private fun getBodyFrame(sign: ZodiacSign): LocalizedParagraph = LocalizedParagraph(
        "${sign.displayName} rising typically shows ${getFrameType(sign)} body frame.",
        "${sign.displayName} उदयले सामान्यतया ${getFrameType(sign)} शरीर फ्रेम देखाउँछ।"
    )
    
    private fun getFrameType(sign: ZodiacSign): String = when (sign) {
        ZodiacSign.ARIES, ZodiacSign.LEO, ZodiacSign.SAGITTARIUS -> "athletic"
        ZodiacSign.TAURUS, ZodiacSign.VIRGO, ZodiacSign.CAPRICORN -> "sturdy"
        ZodiacSign.GEMINI, ZodiacSign.LIBRA, ZodiacSign.AQUARIUS -> "lean"
        else -> "sensitive"
    }
    
    private fun getMetabolicType(sign: ZodiacSign): LocalizedParagraph = LocalizedParagraph(
        "Metabolism follows ${sign.displayName}'s element influence.",
        "चयापचय ${sign.displayName}को तत्व प्रभाव पछ्याउँछ।"
    )
    
    private fun analyzePlanetaryHealthInfluences(context: AnalysisContext): List<PlanetaryHealthInfluence> {
        return listOf(Planet.SUN, Planet.MOON, Planet.MARS).map { planet ->
            PlanetaryHealthInfluence(
                planet = planet,
                strengthLevel = context.getPlanetStrengthLevel(planet),
                healthArea = getHealthArea(planet),
                influence = LocalizedParagraph("${planet.displayName} influences ${getHealthArea(planet).en}.",
                    "${planet.displayName}ले ${getHealthArea(planet).ne}लाई प्रभाव पार्छ।"),
                remedialMeasures = LocalizedParagraph("${planet.displayName} remedies support related health.",
                    "${planet.displayName} उपायहरूले सम्बन्धित स्वास्थ्यलाई समर्थन गर्छ।")
            )
        }
    }
    
    private fun getHealthArea(planet: Planet): LocalizedParagraph = when (planet) {
        Planet.SUN -> LocalizedParagraph("vitality and heart", "जीवनी शक्ति र मुटु")
        Planet.MOON -> LocalizedParagraph("mind and fluids", "मन र तरल पदार्थ")
        Planet.MARS -> LocalizedParagraph("blood and muscles", "रगत र मांसपेशी")
        Planet.MERCURY -> LocalizedParagraph("nervous system", "स्नायु प्रणाली")
        Planet.JUPITER -> LocalizedParagraph("liver and growth", "कलेजो र वृद्धि")
        Planet.VENUS -> LocalizedParagraph("reproductive system", "प्रजनन प्रणाली")
        Planet.SATURN -> LocalizedParagraph("bones and joints", "हड्डी र जोडहरू")
        else -> LocalizedParagraph("subtle energies", "सूक्ष्म ऊर्जाहरू")
    }
    
    private fun generateBodyPartMapping(context: AnalysisContext): BodyPartMapping {
        return BodyPartMapping(
            headAndFace = getBodyPartStrength(1, context),
            throatAndNeck = getBodyPartStrength(2, context),
            armsAndShoulders = getBodyPartStrength(3, context),
            chest = getBodyPartStrength(4, context),
            stomach = getBodyPartStrength(5, context),
            intestines = getBodyPartStrength(6, context),
            lowerAbdomen = getBodyPartStrength(7, context),
            reproductiveOrgans = getBodyPartStrength(8, context),
            thighs = getBodyPartStrength(9, context),
            knees = getBodyPartStrength(10, context),
            ankles = getBodyPartStrength(11, context),
            feet = getBodyPartStrength(12, context)
        )
    }
    
    private fun getBodyPartStrength(house: Int, context: AnalysisContext): BodyPartHealth {
        val strength = context.getHouseStrength(house)
        return BodyPartHealth(
            strengthLevel = strength,
            observations = LocalizedParagraph("House $house body areas show ${strength.displayName.lowercase()} vitality.",
                "भाव $house शरीर क्षेत्रहरूले ${strength.displayNameNe} जीवनी शक्ति देखाउँछ।"),
            recommendations = LocalizedParagraph("Care for house $house related body areas.",
                "भाव $house सम्बन्धित शरीर क्षेत्रहरूको हेरचाह गर्नुहोस्।")
        )
    }
    
    private fun getVulnerabilities(context: AnalysisContext): List<HealthVulnerability> {
        val vulnerabilities = mutableListOf<HealthVulnerability>()
        val sixthLordStrength = context.getPlanetStrengthLevel(context.getHouseLord(6))
        if (sixthLordStrength <= StrengthLevel.WEAK) {
            vulnerabilities.add(HealthVulnerability(
                area = "6th house related",
                severity = sixthLordStrength,
                description = LocalizedParagraph("6th lord weakness indicates health area to monitor.",
                    "छैटौं स्वामी कमजोरीले निगरानी गर्नुपर्ने स्वास्थ्य क्षेत्र संकेत गर्छ।"),
                preventiveMeasures = LocalizedParagraph("Strengthen 6th lord for better immunity.",
                    "राम्रो प्रतिरक्षाको लागि छैटौं स्वामीलाई बलियो बनाउनुहोस्।")
            ))
        }
        return vulnerabilities
    }
    
    private fun getHealthStrengths(context: AnalysisContext): List<LocalizedTrait> {
        val strengths = mutableListOf<LocalizedTrait>()
        val ascLordStrength = context.getPlanetStrengthLevel(context.ascendantLord)
        if (ascLordStrength >= StrengthLevel.STRONG) {
            strengths.add(LocalizedTrait("Strong constitution", "बलियो संविधान", ascLordStrength))
        }
        val sunStrength = context.getPlanetStrengthLevel(Planet.SUN)
        if (sunStrength >= StrengthLevel.STRONG) {
            strengths.add(LocalizedTrait("Good vitality", "राम्रो जीवनी शक्ति", sunStrength))
        }
        return strengths.take(5)
    }
    
    private fun generatePreventiveFocus(context: AnalysisContext): PreventiveFocus {
        return PreventiveFocus(
            priorityAreas = listOf(LocalizedTrait("Constitution balance", "संविधान सन्तुलन", StrengthLevel.MODERATE)),
            seasonalGuidelines = LocalizedParagraph("Follow seasonal health practices aligned with dosha.",
                "दोष संग मिलाएर मौसमी स्वास्थ्य अभ्यासहरू पछ्याउनुहोस्।"),
            yogaPractices = LocalizedParagraph("Yoga practices suited to your constitution.",
                "तपाईंको संविधानको लागि उपयुक्त योग अभ्यासहरू।"),
            ayurvedicRecommendations = LocalizedParagraph("Ayurvedic lifestyle for your dosha type.",
                "तपाईंको दोष प्रकारको लागि आयुर्वेदिक जीवनशैली।")
        )
    }
    
    private fun generateHealthTimeline(context: AnalysisContext): List<HealthTimingPeriod> {
        return context.dashaTimeline.mahadashas.take(2).map { dasha ->
                HealthTimingPeriod(
                    startDate = dasha.startDate.toLocalDate(),
                    endDate = dasha.endDate.toLocalDate(),
                    dasha = "${dasha.planet.displayName} Mahadasha",
                    healthFocus = LocalizedParagraph("${dasha.planet.displayName} period health focus.",
                        "${dasha.planet.displayName} अवधि स्वास्थ्य फोकस।"),
                    vulnerabilities = emptyList(),
                    favorability = context.getPlanetStrengthLevel(dasha.planet)
                )
        }
    }
    
    private fun analyzeCurrentHealthPhase(context: AnalysisContext): CurrentHealthPhase {
        val dasha = context.currentMahadasha
        return CurrentHealthPhase(
            currentDasha = "${dasha?.planet?.displayName ?: "Current"} Mahadasha",
            overallVitality = context.getPlanetStrengthLevel(dasha?.planet ?: Planet.SUN),
            currentFocus = LocalizedParagraph("Current period health recommendations.",
                "वर्तमान अवधि स्वास्थ्य सिफारिसहरू।"),
            watchAreas = emptyList(),
            recommendations = LocalizedParagraph("Follow general health practices.",
                "सामान्य स्वास्थ्य अभ्यासहरू पछ्याउनुहोस्।")
        )
    }
    
    private fun analyzeLongevity(context: AnalysisContext): LongevityProfile {
        val ascLordStrength = context.getPlanetStrengthLevel(context.ascendantLord)
        val eighthStrength = context.getHouseStrength(8)
        
        val category = when {
            ascLordStrength >= StrengthLevel.STRONG && eighthStrength >= StrengthLevel.MODERATE -> LongevityCategory.POORNAYU
            ascLordStrength >= StrengthLevel.MODERATE -> LongevityCategory.MADHYAYU
            else -> LongevityCategory.MADHYAYU
        }
        
        return LongevityProfile(
            category = category,
            primaryIndicators = listOf(LocalizedTrait("Ascendant lord", "लग्न स्वामी", ascLordStrength)),
            supportingFactors = emptyList(),
            challengingFactors = emptyList(),
            longevitySummary = LocalizedParagraph("Longevity indicators show ${category.name.lowercase()} lifespan potential.",
                "दीर्घायु सूचकहरूले ${category.name} आयु क्षमता देखाउँछ।")
        )
    }
    
    private fun generateSummary(context: AnalysisContext): LocalizedParagraph {
        val ascLordStrength = context.getPlanetStrengthLevel(context.ascendantLord)
        return LocalizedParagraph(
            en = "Your health profile shows ${ascLordStrength.displayName.lowercase()} constitutional strength. " +
                "Focus on preventive care aligned with your dosha type for optimal wellbeing.",
            ne = "तपाईंको स्वास्थ्य प्रोफाइलले ${ascLordStrength.displayNameNe} संवैधानिक शक्ति देखाउँछ।"
        )
    }
    
    private fun calculateScore(context: AnalysisContext): Double {
        val ascLordScore = context.getPlanetStrengthLevel(context.ascendantLord).value * 10
        val sunScore = context.getPlanetStrengthLevel(Planet.SUN).value * 5
        val moonScore = context.getPlanetStrengthLevel(Planet.MOON).value * 5
        val sixthScore = context.getHouseStrength(6).value * 5
        return ((ascLordScore + sunScore + moonScore + sixthScore) / 1.5).coerceIn(0.0, 100.0)
    }
}
