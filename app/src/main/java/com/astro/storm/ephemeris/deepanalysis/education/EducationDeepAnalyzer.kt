package com.astro.storm.ephemeris.deepanalysis.education

import com.astro.storm.core.model.Planet
import com.astro.storm.core.model.VedicChart
import com.astro.storm.core.model.ZodiacSign
import com.astro.storm.ephemeris.deepanalysis.*

/**
 * Education Deep Analyzer - Learning and knowledge analysis
 */
object EducationDeepAnalyzer {
    
    fun analyze(chart: VedicChart, context: AnalysisContext): EducationDeepResult {
        return EducationDeepResult(
            fourthHouseAnalysis = analyzeFourthHouse(context),
            fifthHouseAnalysis = analyzeFifthHouse(context),
            ninthHouseAnalysis = analyzeNinthHouse(context),
            mercuryAnalysis = analyzeMercury(context),
            jupiterEducationAnalysis = analyzeJupiter(context),
            learningStyleProfile = analyzeLearningStyle(context),
            academicStrengths = getAcademicStrengths(context),
            academicChallenges = getAcademicChallenges(context),
            concentrationAbility = context.getPlanetStrengthLevel(Planet.MERCURY),
            memoryStrength = context.getPlanetStrengthLevel(Planet.MOON),
            suitableSubjects = getSuitableSubjects(context),
            researchAptitude = context.getHouseStrength(8),
            educationYogas = emptyList(),
            higherEducationAnalysis = analyzeHigherEducation(context),
            educationTimeline = generateTimeline(context),
            currentEducationPhase = analyzeCurrentPhase(context),
            educationSummary = generateSummary(context),
            educationStrengthScore = calculateScore(context)
        )
    }
    
    private fun analyzeFourthHouse(context: AnalysisContext): FourthHouseEducationAnalysis {
        val fourthSign = ZodiacSign.entries[(context.ascendantSign.ordinal + 3) % 12]
        return FourthHouseEducationAnalysis(
            sign = fourthSign,
            planetsInHouse = context.getPlanetsInHouse(4).map { pos ->
                PlanetInHouseAnalysis(pos.planet, context.getDignity(pos.planet),
                    LocalizedParagraph("${pos.planet.displayName} in 4th influences basic education.",
                        "${pos.planet.displayName} चौथोमा आधारभूत शिक्षालाई प्रभाव पार्छ।"))
            },
            houseStrength = context.getHouseStrength(4),
            primaryEducationPattern = LocalizedParagraph("Basic education follows ${fourthSign.displayName} pattern.",
                "आधारभूत शिक्षा ${fourthSign.displayName} ढाँचा पछ्याउँछ।"),
            educationalEnvironment = LocalizedParagraph("Learning environment shaped by 4th house.",
                "सिक्ने वातावरण चौथो भावले आकार दिएको छ।"),
            motherInfluenceOnEducation = LocalizedParagraph("Mother's influence on early education.",
                "प्रारम्भिक शिक्षामा आमाको प्रभाव।")
        )
    }
    
    private fun analyzeFifthHouse(context: AnalysisContext): FifthHouseEducationAnalysis {
        val fifthSign = ZodiacSign.entries[(context.ascendantSign.ordinal + 4) % 12]
        return FifthHouseEducationAnalysis(
            sign = fifthSign,
            planetsInHouse = context.getPlanetsInHouse(5).map { pos ->
                PlanetInHouseAnalysis(pos.planet, context.getDignity(pos.planet),
                    LocalizedParagraph("${pos.planet.displayName} in 5th enhances intelligence.",
                        "${pos.planet.displayName} पाँचौंमा बुद्धिमत्ता बढाउँछ।"))
            },
            houseStrength = context.getHouseStrength(5),
            intellectualAbility = context.getHouseStrength(5),
            creativeIntelligence = LocalizedParagraph("Creative intelligence from 5th house.",
                "पाँचौं भावबाट रचनात्मक बुद्धिमत्ता।"),
            memoryAndGrasp = LocalizedParagraph("Memory and grasp follow 5th house indicators.",
                "स्मरणशक्ति र ग्रहणशक्ति पाँचौं भाव सूचकहरू पछ्याउँछ।"),
            competitiveExamPotential = LocalizedParagraph("Exam performance relates to 5th house.",
                "परीक्षा प्रदर्शन पाँचौं भावसँग सम्बन्धित छ।")
        )
    }
    
    private fun analyzeNinthHouse(context: AnalysisContext): NinthHouseEducationAnalysis {
        val ninthSign = ZodiacSign.entries[(context.ascendantSign.ordinal + 8) % 12]
        return NinthHouseEducationAnalysis(
            sign = ninthSign,
            planetsInHouse = context.getPlanetsInHouse(9).map { pos ->
                PlanetInHouseAnalysis(pos.planet, context.getDignity(pos.planet),
                    LocalizedParagraph("${pos.planet.displayName} in 9th supports higher learning.",
                        "${pos.planet.displayName} नवौंमा उच्च शिक्षालाई समर्थन गर्छ।"))
            },
            houseStrength = context.getHouseStrength(9),
            higherEducationPotential = context.getHouseStrength(9),
            philosophicalLearning = LocalizedParagraph("Philosophical bent follows 9th house.",
                "दार्शनिक झुकाव नवौं भाव पछ्याउँछ।"),
            foreignEducationPotential = LocalizedParagraph("Foreign education potential from 9th.",
                "नवौं भावबाट विदेशी शिक्षा क्षमता।"),
            guruBlessings = LocalizedParagraph("Guru blessings for education.",
                "शिक्षाको लागि गुरु आशीर्वाद।")
        )
    }
    
    private fun analyzeMercury(context: AnalysisContext): MercuryEducationAnalysis {
        val mercury = context.getPlanetPosition(Planet.MERCURY)
        return MercuryEducationAnalysis(
            sign = mercury?.sign ?: context.ascendantSign,
            house = mercury?.house ?: 1,
            dignity = context.getDignity(Planet.MERCURY),
            strengthLevel = context.getPlanetStrengthLevel(Planet.MERCURY),
            intellectualCapacity = LocalizedParagraph("Mercury shapes analytical thinking.",
                "बुधले विश्लेषणात्मक सोच आकार दिन्छ।"),
            communicationInLearning = LocalizedParagraph("Communication skills from Mercury.",
                "बुधबाट सञ्चार कौशल।"),
            analyticalAbility = LocalizedParagraph("Analytical reasoning ability.", "विश्लेषणात्मक तर्क क्षमता।"),
            communicationSkill = LocalizedParagraph("Expression in learning.", "सीखने मा अभिव्यक्ति।"),
            mathematicalAptitude = LocalizedParagraph("Mathematical aptitude follows Mercury.",
                "गणितीय क्षमता बुध पछ्याउँछ।"),
            languageAbility = LocalizedParagraph("Language learning from Mercury placement.",
                "बुध स्थानबाट भाषा सिक्ने।")
        )
    }
    
    private fun analyzeJupiter(context: AnalysisContext): JupiterEducationAnalysis {
        val jupiter = context.getPlanetPosition(Planet.JUPITER)
        return JupiterEducationAnalysis(
            sign = jupiter?.sign ?: context.ascendantSign,
            house = jupiter?.house ?: 1,
            dignity = context.getDignity(Planet.JUPITER),
            strengthLevel = context.getPlanetStrengthLevel(Planet.JUPITER),
            wisdomAcquisition = LocalizedParagraph("Jupiter develops wisdom and knowledge.",
                "बृहस्पतिले ज्ञान र ज्ञान विकास गर्छ।"),
            wisdomDevelopment = LocalizedParagraph("Knowledge development path.", "ज्ञान विकास मार्ग।"),
            higherLearningAbility = LocalizedParagraph("Higher learning from Jupiter's blessing.",
                "बृहस्पतिको आशीर्वादबाट उच्च शिक्षा।"),
            spiritualKnowledge = LocalizedParagraph("Spiritual and philosophical knowledge.",
                "आध्यात्मिक र दार्शनिक ज्ञान।"),
            teachingAbility = LocalizedParagraph("Teaching ability from Jupiter.",
                "बृहस्पतिबाट शिक्षण क्षमता।"),
            teacherBlessings = LocalizedParagraph("Blessings from teachers and elders.",
                "गुरु र मान्यवरहरूबाट आशीर्वाद।")
        )
    }
    
    private fun analyzeLearningStyle(context: AnalysisContext): LearningStyleProfile {
        val mercury = context.getPlanetPosition(Planet.MERCURY)
        return LearningStyleProfile(
            dominantStyle = getLearningStyleType(mercury?.sign ?: context.ascendantSign),
            preferredMethods = listOf(LocalizedTrait("Visual learning", "दृश्य सिक्ने", StrengthLevel.MODERATE)),
            studyEnvironment = LocalizedParagraph("Preferred study environment from chart.",
                "कुण्डलीबाट मनपर्ने अध्ययन वातावरण।"),
            concentrationAbility = context.getPlanetStrengthLevel(Planet.MERCURY),
            retentionCapacity = LocalizedParagraph("Memory retention follows Moon and Mercury.",
                "स्मरणशक्ति राख्ने चन्द्रमा र बुध पछ्याउँछ।")
        )
    }
    
    private fun getLearningStyleType(sign: ZodiacSign): LearningStyleType = when (sign) {
        ZodiacSign.GEMINI, ZodiacSign.VIRGO -> LearningStyleType.ANALYTICAL
        ZodiacSign.ARIES, ZodiacSign.LEO, ZodiacSign.SAGITTARIUS -> LearningStyleType.EXPERIENTIAL
        ZodiacSign.CANCER, ZodiacSign.SCORPIO, ZodiacSign.PISCES -> LearningStyleType.INTUITIVE
        else -> LearningStyleType.VISUAL
    }
    
    private fun getAcademicStrengths(context: AnalysisContext): List<LocalizedTrait> {
        val strengths = mutableListOf<LocalizedTrait>()
        val mercuryStrength = context.getPlanetStrengthLevel(Planet.MERCURY)
        if (mercuryStrength >= StrengthLevel.STRONG) {
            strengths.add(LocalizedTrait("Strong intellect", "बलियो बुद्धि", mercuryStrength))
        }
        val jupiterStrength = context.getPlanetStrengthLevel(Planet.JUPITER)
        if (jupiterStrength >= StrengthLevel.STRONG) {
            strengths.add(LocalizedTrait("Wisdom learning", "ज्ञान सिक्ने", jupiterStrength))
        }
        val fifthStrength = context.getHouseStrength(5)
        if (fifthStrength >= StrengthLevel.STRONG) {
            strengths.add(LocalizedTrait("Intelligence blessing", "बुद्धिमत्ता आशीर्वाद", fifthStrength))
        }
        return strengths.take(5)
    }
    
    private fun getAcademicChallenges(context: AnalysisContext): List<LocalizedTrait> {
        val challenges = mutableListOf<LocalizedTrait>()
        val mercuryStrength = context.getPlanetStrengthLevel(Planet.MERCURY)
        if (mercuryStrength <= StrengthLevel.WEAK) {
            challenges.add(LocalizedTrait("Focus development", "फोकस विकास", StrengthLevel.MODERATE))
        }
        return challenges.take(3)
    }
    
    private fun getSuitableSubjects(context: AnalysisContext): List<SubjectAffinity> {
        val subjects = mutableListOf<SubjectAffinity>()
        
        val mercuryStrength = context.getPlanetStrengthLevel(Planet.MERCURY)
        if (mercuryStrength >= StrengthLevel.MODERATE) {
            subjects.add(SubjectMatch(
                subjectCategory = SubjectCategory.COMMERCE_FINANCE,
                specificSubjects = listOf("Mathematics", "Commerce"),
                aptitudeScore = 7.5,
                reasonForMatch = LocalizedParagraph("Mercury supports analytical subjects.", "बुधले विश्लेषणात्मक विषयहरूलाई समर्थन गर्छ।"),
                affinity = mercuryStrength
            ))
        }
        
        val jupiterStrength = context.getPlanetStrengthLevel(Planet.JUPITER)
        if (jupiterStrength >= StrengthLevel.MODERATE) {
            subjects.add(SubjectMatch(
                subjectCategory = SubjectCategory.PHILOSOPHY_RELIGION,
                specificSubjects = listOf("Philosophy", "Law"),
                aptitudeScore = 7.5,
                reasonForMatch = LocalizedParagraph("Jupiter supports wisdom subjects.", "बृहस्पतिले ज्ञान विषयहरूलाई समर्थन गर्छ।"),
                affinity = jupiterStrength
            ))
        }
        
        val marsStrength = context.getPlanetStrengthLevel(Planet.MARS)
        if (marsStrength >= StrengthLevel.MODERATE) {
            subjects.add(SubjectMatch(
                subjectCategory = SubjectCategory.ENGINEERING,
                specificSubjects = listOf("Engineering", "Science"),
                aptitudeScore = 7.5,
                reasonForMatch = LocalizedParagraph("Mars supports technical subjects.", "मंगलले प्राविधिक विषयहरूलाई समर्थन गर्छ।"),
                affinity = marsStrength
            ))
        }
        
        return subjects.sortedByDescending { it.affinity.value }.take(5)
    }
    
    private fun analyzeHigherEducation(context: AnalysisContext): HigherEducationAnalysis {
        val ninthStrength = context.getHouseStrength(9)
        val jupiterStrength = context.getPlanetStrengthLevel(Planet.JUPITER)
        
        return HigherEducationAnalysis(
            overallPotential = if (ninthStrength >= StrengthLevel.STRONG && jupiterStrength >= StrengthLevel.MODERATE)
                StrengthLevel.STRONG else StrengthLevel.MODERATE,
            academicDegreeSuccess = LocalizedParagraph("Degree completion follows 9th and 5th house.",
                "डिग्री पूर्णता नवौं र पाँचौं भाव पछ्याउँछ।"),
            researchAbility = LocalizedParagraph("Research ability from 8th and 5th house.",
                "आठौं र पाँचौं भावबाट अनुसन्धान क्षमता।"),
            foreignEducationProspects = LocalizedParagraph("Foreign study prospects from 9th and 12th.",
                "नवौं र बाह्रौंबाट विदेशी अध्ययन सम्भावनाहरू।"),
            professionalCertifications = LocalizedParagraph("Professional certifications align with 10th house.",
                "व्यावसायिक प्रमाणपत्रहरू दशौं भावसँग मिल्छन्।")
        )
    }
    
    private fun generateTimeline(context: AnalysisContext): List<EducationTimingPeriod> {
        return context.dashaTimeline.mahadashas.take(2).map { dasha ->
            EducationTimingPeriod(
                startDate = dasha.startDate.toLocalDate(),
                endDate = dasha.endDate.toLocalDate(),
                dasha = "${dasha.planet.displayName} Mahadasha",
                educationFocus = LocalizedParagraph("${dasha.planet.displayName} period education focus.",
                    "${dasha.planet.displayName} अवधि शिक्षा फोकस।"),
                opportunities = emptyList(),
                challenges = emptyList(),
                favorability = context.getPlanetStrengthLevel(dasha.planet)
            )
        }
    }
    
    private fun analyzeCurrentPhase(context: AnalysisContext): CurrentEducationPhase {
        val dasha = context.currentMahadasha
        return CurrentEducationPhase(
            currentDasha = "${dasha?.planet?.displayName ?: "Current"} Mahadasha",
            learningFavorability = context.getPlanetStrengthLevel(dasha?.planet ?: Planet.MERCURY),
            currentFocus = LocalizedParagraph("Current period educational recommendations.",
                "वर्तमान अवधि शैक्षिक सिफारिसहरू।"),
            studyAdvice = LocalizedParagraph("Study approach for current period.",
                "वर्तमान अवधिको लागि अध्ययन दृष्टिकोण।")
        )
    }
    
    private fun generateSummary(context: AnalysisContext): LocalizedParagraph {
        val mercuryStrength = context.getPlanetStrengthLevel(Planet.MERCURY)
        val fifthStrength = context.getHouseStrength(5)
        
        return LocalizedParagraph(
            en = "Your education profile shows ${fifthStrength.displayName.lowercase()} intelligence with " +
                "${mercuryStrength.displayName.lowercase()} Mercury. Academic success comes through ${getLearningPath(context)}.",
            ne = "तपाईंको शिक्षा प्रोफाइलले ${fifthStrength.displayNameNe} बुद्धिमत्ता ${mercuryStrength.displayNameNe} बुधको साथ देखाउँछ।"
        )
    }
    
    private fun getLearningPath(context: AnalysisContext): String {
        val mercury = context.getPlanetPosition(Planet.MERCURY)
        return when (mercury?.sign) {
            ZodiacSign.GEMINI, ZodiacSign.VIRGO -> "analytical and detailed study"
            ZodiacSign.SAGITTARIUS, ZodiacSign.PISCES -> "conceptual and intuitive learning"
            else -> "consistent effort and application"
        }
    }
    
    private fun calculateScore(context: AnalysisContext): Double {
        val mercuryScore = context.getPlanetStrengthLevel(Planet.MERCURY).value * 10
        val jupiterScore = context.getPlanetStrengthLevel(Planet.JUPITER).value * 5
        val fifthScore = context.getHouseStrength(5).value * 10
        return ((mercuryScore + jupiterScore + fifthScore) / 1.5).coerceIn(0.0, 100.0)
    }
}
