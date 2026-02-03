package com.astro.storm.ephemeris.varga

import com.astro.storm.core.model.Planet
import com.astro.storm.core.model.PlanetPosition
import com.astro.storm.core.model.VedicChart
import com.astro.storm.core.model.ZodiacSign
import com.astro.storm.ephemeris.DivisionalChartCalculator
import com.astro.storm.ephemeris.DivisionalChartType
import com.astro.storm.ephemeris.VedicAstrologyUtils

/**
 * SaptamsaAnalyzer - Comprehensive D7 (Saptamsa) Chart Analysis
 *
 * The Saptamsa (D7) is the seventh divisional chart, primarily used for analyzing:
 * - Children and progeny
 * - Fertility and reproductive health
 * - Relationship with children
 * - Creative potential and works
 * - Grandchildren (5th from 5th principle)
 *
 * Calculation Method:
 * Each sign is divided into 7 equal parts (4°17'8.57" each).
 * For Odd signs: Divisions start from the sign itself
 * For Even signs: Divisions start from the 7th sign
 *
 * Key Points for D7 Analysis:
 * 1. D7 Lagna indicates the native's approach to children
 * 2. 5th house in D7 shows children's nature and number
 * 3. Jupiter's position is crucial (natural karaka for children)
 * 4. 9th house shows fortune through children
 * 5. Malefics in 5th house indicate challenges
 *
 * Vedic References:
 * - BPHS Chapter 7-8 (Divisional Charts)
 * - Jataka Parijata
 * - Phala Deepika
 * - Saravali
 *
 * @author AstroStorm
 * @since 2026.02
 */
object SaptamsaAnalyzer {

    /**
     * Planetary significance for progeny
     */
    private val PROGENY_KARAKAS = mapOf(
        Planet.JUPITER to "Putrakaraka (Primary significator of children)",
        Planet.MOON to "Nourishment and emotional connection with children",
        Planet.VENUS to "Fertility and creative expression",
        Planet.MERCURY to "Education and communication with children",
        Planet.SUN to "Father, authority over children",
        Planet.MARS to "Courage and protection of children",
        Planet.SATURN to "Discipline and karmic children",
        Planet.RAHU to "Unconventional or adopted children",
        Planet.KETU to "Spiritual children, past-life connections"
    )

    /**
     * Number of children estimation factors
     */
    data class ChildCountFactors(
        val fifthHousePlanets: Int,
        val fifthLordStrength: Double,
        val jupiterStrength: Double,
        val beneficInfluence: Int,
        val maleficInfluence: Int,
        val estimatedRange: IntRange,
        val modifyingFactors: List<String>
    )

    /**
     * Individual child indication
     */
    data class ChildIndication(
        val orderNumber: Int,
        val indicatingPlanet: Planet,
        val gender: ChildGender,
        val genderConfidence: Double,
        val characteristics: List<String>,
        val relationshipQuality: RelationshipQuality,
        val healthIndications: List<String>,
        val careerIndications: List<String>,
        val timingIndicators: List<String>
    )

    enum class ChildGender(val displayName: String) {
        MALE("Male"),
        FEMALE("Female"),
        UNCERTAIN("Uncertain")
    }

    enum class RelationshipQuality(val displayName: String, val score: Int) {
        EXCELLENT("Excellent", 90),
        GOOD("Good", 75),
        MODERATE("Moderate", 60),
        CHALLENGING("Challenging", 40),
        DIFFICULT("Difficult", 25)
    }

    /**
     * Fertility analysis result
     */
    data class FertilityAnalysis(
        val overallScore: Double, // 0-100
        val fertilityStatus: FertilityStatus,
        val supportingFactors: List<String>,
        val challengingFactors: List<String>,
        val timingForConception: List<ConceptionTiming>,
        val remedies: List<String>
    )

    enum class FertilityStatus(val displayName: String) {
        HIGHLY_FAVORABLE("Highly Favorable"),
        FAVORABLE("Favorable"),
        MODERATE("Moderate"),
        CHALLENGING("Challenging"),
        NEEDS_ATTENTION("Needs Special Attention")
    }

    data class ConceptionTiming(
        val period: String,
        val dashaLord: Planet,
        val favorabilityScore: Double,
        val recommendation: String
    )

    /**
     * D7 Lagna interpretation
     */
    data class D7LagnaAnalysis(
        val lagnaSign: ZodiacSign,
        val lagnaLord: Planet,
        val lagnaLordPosition: PlanetPosition?,
        val lagnaLordDignity: VedicAstrologyUtils.PlanetaryDignity?,
        val planetsInLagna: List<PlanetPosition>,
        val interpretation: String,
        val approachToChildren: String
    )

    /**
     * Fifth house analysis in D7
     */
    data class FifthHouseAnalysis(
        val fifthSign: ZodiacSign,
        val fifthLord: Planet,
        val fifthLordPosition: PlanetPosition?,
        val fifthLordDignity: VedicAstrologyUtils.PlanetaryDignity?,
        val planetsInFifth: List<PlanetPosition>,
        val aspectsOnFifth: List<Planet>,
        val childIndications: List<ChildIndication>,
        val interpretation: String
    )

    /**
     * Complete Saptamsa Analysis Result
     */
    data class SaptamsaAnalysis(
        val chartId: String,
        val d7Chart: VedicChart?,
        val d7LagnaAnalysis: D7LagnaAnalysis,
        val fifthHouseAnalysis: FifthHouseAnalysis,
        val jupiterAnalysis: JupiterAnalysis,
        val childCountEstimate: ChildCountFactors,
        val childIndications: List<ChildIndication>,
        val fertilityAnalysis: FertilityAnalysis,
        val santhanaYogas: List<SanthanaYoga>,
        val challengingYogas: List<String>,
        val overallScore: Double,
        val keyInsights: List<String>,
        val recommendations: List<String>,
        val timestamp: Long = System.currentTimeMillis()
    )

    /**
     * Jupiter (Putrakaraka) specific analysis
     */
    data class JupiterAnalysis(
        val position: PlanetPosition?,
        val dignity: VedicAstrologyUtils.PlanetaryDignity?,
        val house: Int,
        val isRetrograde: Boolean,
        val isCombust: Boolean,
        val aspects: List<Planet>,
        val strength: Double,
        val interpretation: String
    )

    /**
     * Santhana Yoga (Progeny Yoga)
     */
    data class SanthanaYoga(
        val name: String,
        val sanskritName: String,
        val involvedPlanets: List<Planet>,
        val type: YogaType,
        val effect: String,
        val strength: Double
    )

    enum class YogaType {
        FAVORABLE, CHALLENGING, TIMING
    }

    /**
     * Perform complete Saptamsa analysis
     *
     * @param chart The birth chart (Rasi/D1)
     * @return Complete SaptamsaAnalysis
     */
    fun analyzeSaptamsa(chart: VedicChart): SaptamsaAnalysis {
        // Get D7 chart
        val divisionalCharts = DivisionalChartCalculator.calculateAllDivisionalCharts(chart)
        val d7Chart = divisionalCharts.find { it.chartType == DivisionalChartType.D7_SAPTAMSA }

        // D7 Lagna analysis
        val d7LagnaAnalysis = analyzeD7Lagna(d7Chart, chart)

        // Fifth house analysis
        val fifthHouseAnalysis = analyzeFifthHouse(d7Chart, chart)

        // Jupiter analysis
        val jupiterAnalysis = analyzeJupiter(d7Chart, chart)

        // Child count estimation
        val childCountEstimate = estimateChildCount(d7Chart, chart, fifthHouseAnalysis, jupiterAnalysis)

        // Individual child indications
        val childIndications = getChildIndications(d7Chart, chart, fifthHouseAnalysis)

        // Fertility analysis
        val fertilityAnalysis = analyzeFertility(d7Chart, chart, jupiterAnalysis)

        // Identify Santhana Yogas
        val santhanaYogas = identifySanthanaYogas(d7Chart, chart)
        val challengingYogas = identifyChallengingYogas(d7Chart, chart)

        // Calculate overall score
        val overallScore = calculateOverallScore(
            d7LagnaAnalysis, fifthHouseAnalysis, jupiterAnalysis,
            santhanaYogas, challengingYogas
        )

        // Generate insights and recommendations
        val keyInsights = generateInsights(
            d7LagnaAnalysis, fifthHouseAnalysis, jupiterAnalysis,
            childCountEstimate, santhanaYogas
        )
        val recommendations = generateRecommendations(
            fertilityAnalysis, challengingYogas, jupiterAnalysis
        )

        return SaptamsaAnalysis(
            chartId = generateChartId(chart),
            d7Chart = d7Chart,
            d7LagnaAnalysis = d7LagnaAnalysis,
            fifthHouseAnalysis = fifthHouseAnalysis,
            jupiterAnalysis = jupiterAnalysis,
            childCountEstimate = childCountEstimate,
            childIndications = childIndications,
            fertilityAnalysis = fertilityAnalysis,
            santhanaYogas = santhanaYogas,
            challengingYogas = challengingYogas,
            overallScore = overallScore,
            keyInsights = keyInsights,
            recommendations = recommendations
        )
    }

    /**
     * Analyze D7 Lagna
     */
    private fun analyzeD7Lagna(d7Chart: VedicChart?, rasiChart: VedicChart): D7LagnaAnalysis {
        if (d7Chart == null) {
            return createDefaultD7LagnaAnalysis(rasiChart)
        }

        val lagnaSign = d7Chart.ascendantSign
        val lagnaLord = lagnaSign.ruler
        val lagnaLordPos = d7Chart.planetPositions.find { it.planet == lagnaLord }
        val lagnaLordDignity = lagnaLordPos?.let { VedicAstrologyUtils.getDignity(it) }
        val planetsInLagna = d7Chart.planetPositions.filter { it.house == 1 }

        val interpretation = buildD7LagnaInterpretation(lagnaSign, lagnaLordPos, planetsInLagna)
        val approach = getApproachToChildren(lagnaSign, planetsInLagna)

        return D7LagnaAnalysis(
            lagnaSign = lagnaSign,
            lagnaLord = lagnaLord,
            lagnaLordPosition = lagnaLordPos,
            lagnaLordDignity = lagnaLordDignity,
            planetsInLagna = planetsInLagna,
            interpretation = interpretation,
            approachToChildren = approach
        )
    }

    /**
     * Analyze Fifth House in D7
     */
    private fun analyzeFifthHouse(d7Chart: VedicChart?, rasiChart: VedicChart): FifthHouseAnalysis {
        val chart = d7Chart ?: rasiChart
        val lagnaSign = chart.ascendantSign
        val fifthSign = ZodiacSign.entries[(lagnaSign.ordinal + 4) % 12]
        val fifthLord = fifthSign.ruler

        val fifthLordPos = chart.planetPositions.find { it.planet == fifthLord }
        val fifthLordDignity = fifthLordPos?.let { VedicAstrologyUtils.getDignity(it) }
        val planetsInFifth = chart.planetPositions.filter { it.house == 5 }

        val aspectsOnFifth = chart.planetPositions
            .filter { it.house != 5 }
            .filter { VedicAstrologyUtils.aspectsHouse(it.planet, it.house, 5) }
            .map { it.planet }

        val childIndications = determineChildIndications(planetsInFifth, fifthLordPos, chart)

        val interpretation = buildFifthHouseInterpretation(
            fifthSign, fifthLordPos, planetsInFifth, aspectsOnFifth
        )

        return FifthHouseAnalysis(
            fifthSign = fifthSign,
            fifthLord = fifthLord,
            fifthLordPosition = fifthLordPos,
            fifthLordDignity = fifthLordDignity,
            planetsInFifth = planetsInFifth,
            aspectsOnFifth = aspectsOnFifth,
            childIndications = childIndications,
            interpretation = interpretation
        )
    }

    /**
     * Analyze Jupiter (Putrakaraka)
     */
    private fun analyzeJupiter(d7Chart: VedicChart?, rasiChart: VedicChart): JupiterAnalysis {
        val chart = d7Chart ?: rasiChart
        val jupiterPos = chart.planetPositions.find { it.planet == Planet.JUPITER }
        val sunPos = chart.planetPositions.find { it.planet == Planet.SUN }

        if (jupiterPos == null) {
            return createDefaultJupiterAnalysis()
        }

        val dignity = VedicAstrologyUtils.getDignity(jupiterPos)
        val isRetrograde = jupiterPos.isRetrograde
        val isCombust = sunPos?.let {
            VedicAstrologyUtils.isCombust(Planet.JUPITER, jupiterPos.longitude, it.longitude, false)
        } ?: false

        val aspectingPlanets = chart.planetPositions
            .filter { it.planet != Planet.JUPITER }
            .filter { VedicAstrologyUtils.aspectsHouse(it.planet, it.house, jupiterPos.house) }
            .map { it.planet }

        val strength = calculateJupiterStrength(jupiterPos, dignity, isRetrograde, isCombust, aspectingPlanets)

        val interpretation = buildJupiterInterpretation(jupiterPos, dignity, isRetrograde, isCombust, strength)

        return JupiterAnalysis(
            position = jupiterPos,
            dignity = dignity,
            house = jupiterPos.house,
            isRetrograde = isRetrograde,
            isCombust = isCombust,
            aspects = aspectingPlanets,
            strength = strength,
            interpretation = interpretation
        )
    }

    /**
     * Estimate number of children
     */
    private fun estimateChildCount(
        d7Chart: VedicChart?,
        rasiChart: VedicChart,
        fifthHouseAnalysis: FifthHouseAnalysis,
        jupiterAnalysis: JupiterAnalysis
    ): ChildCountFactors {
        val chart = d7Chart ?: rasiChart
        val planetsInFifth = fifthHouseAnalysis.planetsInFifth.size
        val fifthLordStrength = fifthHouseAnalysis.fifthLordDignity?.let { dignityToStrength(it) } ?: 50.0
        val jupiterStrength = jupiterAnalysis.strength

        // Count benefic and malefic influences on 5th
        val benefics = fifthHouseAnalysis.planetsInFifth.count {
            VedicAstrologyUtils.isNaturalBenefic(it.planet)
        } + fifthHouseAnalysis.aspectsOnFifth.count {
            VedicAstrologyUtils.isNaturalBenefic(it)
        }

        val malefics = fifthHouseAnalysis.planetsInFifth.count {
            VedicAstrologyUtils.isNaturalMalefic(it.planet)
        } + fifthHouseAnalysis.aspectsOnFifth.count {
            VedicAstrologyUtils.isNaturalMalefic(it)
        }

        // Estimate range based on traditional rules
        var minChildren = 0
        var maxChildren = 3

        // Jupiter's influence
        if (jupiterStrength > 70) {
            maxChildren += 1
        } else if (jupiterStrength < 40) {
            maxChildren -= 1
        }

        // Fifth lord's strength
        if (fifthLordStrength > 70) {
            maxChildren += 1
        }

        // Benefic influence
        if (benefics >= 2) {
            minChildren = 1
            maxChildren += 1
        }

        // Malefic influence
        if (malefics >= 2) {
            maxChildren -= 1
        }

        // Fifth house sign (watery signs are fertile)
        if (fifthHouseAnalysis.fifthSign in listOf(ZodiacSign.CANCER, ZodiacSign.SCORPIO, ZodiacSign.PISCES)) {
            maxChildren += 1
        }

        minChildren = minChildren.coerceAtLeast(0)
        maxChildren = maxChildren.coerceIn(minChildren, 5)

        val modifyingFactors = mutableListOf<String>()
        if (jupiterStrength > 70) modifyingFactors.add("Strong Jupiter increases progeny potential")
        if (benefics >= 2) modifyingFactors.add("Benefic influence on 5th house is favorable")
        if (malefics >= 2) modifyingFactors.add("Malefic influence may reduce or delay children")

        return ChildCountFactors(
            fifthHousePlanets = planetsInFifth,
            fifthLordStrength = fifthLordStrength,
            jupiterStrength = jupiterStrength,
            beneficInfluence = benefics,
            maleficInfluence = malefics,
            estimatedRange = minChildren..maxChildren,
            modifyingFactors = modifyingFactors
        )
    }

    /**
     * Get individual child indications
     */
    private fun getChildIndications(
        d7Chart: VedicChart?,
        rasiChart: VedicChart,
        fifthHouseAnalysis: FifthHouseAnalysis
    ): List<ChildIndication> {
        val chart = d7Chart ?: rasiChart
        val indications = mutableListOf<ChildIndication>()

        // First child - 5th house
        val firstChildPlanet = fifthHouseAnalysis.planetsInFifth.firstOrNull()?.planet
            ?: fifthHouseAnalysis.fifthLord
        indications.add(createChildIndication(1, firstChildPlanet, chart))

        // Second child - 7th from 5th (11th house)
        val eleventhPlanets = chart.planetPositions.filter { it.house == 11 }
        val secondChildPlanet = eleventhPlanets.firstOrNull()?.planet
            ?: ZodiacSign.entries[(chart.ascendantSign.ordinal + 10) % 12].ruler
        indications.add(createChildIndication(2, secondChildPlanet, chart))

        // Third child - 3rd from 5th (7th house)
        val seventhPlanets = chart.planetPositions.filter { it.house == 7 }
        val thirdChildPlanet = seventhPlanets.firstOrNull()?.planet
            ?: ZodiacSign.entries[(chart.ascendantSign.ordinal + 6) % 12].ruler
        indications.add(createChildIndication(3, thirdChildPlanet, chart))

        return indications
    }

    private fun createChildIndication(orderNumber: Int, planet: Planet, chart: VedicChart): ChildIndication {
        val planetPos = chart.planetPositions.find { it.planet == planet }

        // Determine gender based on planet nature
        val (gender, confidence) = when (planet) {
            Planet.SUN, Planet.MARS, Planet.JUPITER -> Pair(ChildGender.MALE, 0.7)
            Planet.MOON, Planet.VENUS -> Pair(ChildGender.FEMALE, 0.7)
            Planet.MERCURY -> Pair(ChildGender.UNCERTAIN, 0.5)
            Planet.SATURN -> Pair(ChildGender.MALE, 0.6)
            Planet.RAHU -> Pair(ChildGender.UNCERTAIN, 0.4)
            Planet.KETU -> Pair(ChildGender.UNCERTAIN, 0.4)
            else -> Pair(ChildGender.UNCERTAIN, 0.3)
        }

        val characteristics = getChildCharacteristics(planet, planetPos)
        val relationship = getRelationshipQuality(planet, planetPos, chart)
        val healthIndicators = getHealthIndicatorsForChild(planet, planetPos)
        val careerIndicators = getCareerIndicatorsForChild(planet, planetPos)
        val timingIndicators = getTimingIndicators(planet, orderNumber)

        return ChildIndication(
            orderNumber = orderNumber,
            indicatingPlanet = planet,
            gender = gender,
            genderConfidence = confidence,
            characteristics = characteristics,
            relationshipQuality = relationship,
            healthIndications = healthIndicators,
            careerIndications = careerIndicators,
            timingIndicators = timingIndicators
        )
    }

    /**
     * Analyze fertility factors
     */
    private fun analyzeFertility(
        d7Chart: VedicChart?,
        rasiChart: VedicChart,
        jupiterAnalysis: JupiterAnalysis
    ): FertilityAnalysis {
        val chart = d7Chart ?: rasiChart
        val supportingFactors = mutableListOf<String>()
        val challengingFactors = mutableListOf<String>()
        var score = 50.0

        // Jupiter strength
        if (jupiterAnalysis.strength > 70) {
            score += 15
            supportingFactors.add("Strong Jupiter supports fertility")
        } else if (jupiterAnalysis.strength < 40) {
            score -= 15
            challengingFactors.add("Weak Jupiter may affect fertility")
        }

        // 5th house condition
        val fifthPlanets = chart.planetPositions.filter { it.house == 5 }
        val beneficsInFifth = fifthPlanets.count { VedicAstrologyUtils.isNaturalBenefic(it.planet) }
        val maleficsInFifth = fifthPlanets.count { VedicAstrologyUtils.isNaturalMalefic(it.planet) }

        if (beneficsInFifth >= 1) {
            score += 10
            supportingFactors.add("Benefic planets in 5th house support conception")
        }
        if (maleficsInFifth >= 2) {
            score -= 15
            challengingFactors.add("Multiple malefics in 5th may delay children")
        }

        // Moon's condition (important for conception)
        val moonPos = chart.planetPositions.find { it.planet == Planet.MOON }
        if (moonPos != null) {
            val moonDignity = VedicAstrologyUtils.getDignity(moonPos)
            if (moonDignity in listOf(
                    VedicAstrologyUtils.PlanetaryDignity.EXALTED,
                    VedicAstrologyUtils.PlanetaryDignity.OWN_SIGN
                )
            ) {
                score += 10
                supportingFactors.add("Strong Moon supports reproductive health")
            }
            if (moonDignity == VedicAstrologyUtils.PlanetaryDignity.DEBILITATED) {
                score -= 10
                challengingFactors.add("Debilitated Moon may need attention")
            }
        }

        // Venus condition
        val venusPos = chart.planetPositions.find { it.planet == Planet.VENUS }
        if (venusPos != null) {
            if (venusPos.house in listOf(5, 7, 1, 9)) {
                score += 8
                supportingFactors.add("Venus in favorable house supports fertility")
            }
            if (venusPos.isRetrograde) {
                score -= 5
                challengingFactors.add("Retrograde Venus may indicate timing issues")
            }
        }

        val status = when {
            score >= 75 -> FertilityStatus.HIGHLY_FAVORABLE
            score >= 60 -> FertilityStatus.FAVORABLE
            score >= 45 -> FertilityStatus.MODERATE
            score >= 30 -> FertilityStatus.CHALLENGING
            else -> FertilityStatus.NEEDS_ATTENTION
        }

        // Conception timing based on Jupiter dasha/antardasha
        val conceptionTimings = listOf(
            ConceptionTiming("Jupiter Dasha/Antardasha", Planet.JUPITER, 85.0, "Most favorable for conception"),
            ConceptionTiming("Venus Dasha/Antardasha", Planet.VENUS, 75.0, "Good for conception"),
            ConceptionTiming("Moon Dasha/Antardasha", Planet.MOON, 70.0, "Emotionally favorable period")
        )

        val remedies = if (status == FertilityStatus.CHALLENGING || status == FertilityStatus.NEEDS_ATTENTION) {
            listOf(
                "Worship Santan Gopal (form of Krishna)",
                "Chant Santan Gopala Mantra",
                "Observe Putra Kameshti Vrat",
                "Donate to orphanages",
                "Strengthen Jupiter through Guru Beej Mantra"
            )
        } else {
            listOf("Maintain spiritual practices", "Regular health check-ups")
        }

        return FertilityAnalysis(
            overallScore = score.coerceIn(0.0, 100.0),
            fertilityStatus = status,
            supportingFactors = supportingFactors,
            challengingFactors = challengingFactors,
            timingForConception = conceptionTimings,
            remedies = remedies
        )
    }

    /**
     * Identify Santhana Yogas (favorable progeny yogas)
     */
    private fun identifySanthanaYogas(d7Chart: VedicChart?, rasiChart: VedicChart): List<SanthanaYoga> {
        val yogas = mutableListOf<SanthanaYoga>()
        val chart = d7Chart ?: rasiChart

        val jupiterPos = chart.planetPositions.find { it.planet == Planet.JUPITER }
        val moonPos = chart.planetPositions.find { it.planet == Planet.MOON }
        val venusPos = chart.planetPositions.find { it.planet == Planet.VENUS }
        val fifthLord = ZodiacSign.entries[(chart.ascendantSign.ordinal + 4) % 12].ruler
        val fifthLordPos = chart.planetPositions.find { it.planet == fifthLord }

        // 1. Jupiter in Kendra or Trikona
        if (jupiterPos != null && jupiterPos.house in (VedicAstrologyUtils.KENDRA_HOUSES + VedicAstrologyUtils.TRIKONA_HOUSES)) {
            yogas.add(
                SanthanaYoga(
                    name = "Guru Yoga for Progeny",
                    sanskritName = "Guru Santhana Yoga",
                    involvedPlanets = listOf(Planet.JUPITER),
                    type = YogaType.FAVORABLE,
                    effect = "Jupiter in Kendra/Trikona blesses with children",
                    strength = 80.0
                )
            )
        }

        // 2. Fifth lord in own sign or exalted
        if (fifthLordPos != null) {
            val dignity = VedicAstrologyUtils.getDignity(fifthLordPos)
            if (dignity in listOf(
                    VedicAstrologyUtils.PlanetaryDignity.EXALTED,
                    VedicAstrologyUtils.PlanetaryDignity.OWN_SIGN
                )
            ) {
                yogas.add(
                    SanthanaYoga(
                        name = "Strong Fifth Lord Yoga",
                        sanskritName = "Putrabhava Adhipati Yoga",
                        involvedPlanets = listOf(fifthLord),
                        type = YogaType.FAVORABLE,
                        effect = "Fifth lord in strength ensures progeny happiness",
                        strength = 75.0
                    )
                )
            }
        }

        // 3. Moon and Jupiter conjunction or mutual aspect
        if (jupiterPos != null && moonPos != null) {
            val distance = Math.abs(jupiterPos.house - moonPos.house)
            if (distance == 0 || distance == 6) { // Conjunction or opposition
                yogas.add(
                    SanthanaYoga(
                        name = "Gaja Kesari for Children",
                        sanskritName = "Putra Gaja Kesari",
                        involvedPlanets = listOf(Planet.JUPITER, Planet.MOON),
                        type = YogaType.FAVORABLE,
                        effect = "Jupiter-Moon relationship blesses with intelligent children",
                        strength = 85.0
                    )
                )
            }
        }

        // 4. Venus in 5th or 7th from Jupiter
        if (jupiterPos != null && venusPos != null) {
            val venusFromJupiter = ((venusPos.house - jupiterPos.house + 12) % 12) + 1
            if (venusFromJupiter in listOf(5, 7)) {
                yogas.add(
                    SanthanaYoga(
                        name = "Venus-Jupiter Santhana Yoga",
                        sanskritName = "Shukra-Guru Santhana Yoga",
                        involvedPlanets = listOf(Planet.JUPITER, Planet.VENUS),
                        type = YogaType.FAVORABLE,
                        effect = "Artistic and prosperous children",
                        strength = 70.0
                    )
                )
            }
        }

        return yogas
    }

    /**
     * Identify challenging yogas for progeny
     */
    private fun identifyChallengingYogas(d7Chart: VedicChart?, rasiChart: VedicChart): List<String> {
        val challenges = mutableListOf<String>()
        val chart = d7Chart ?: rasiChart

        val jupiterPos = chart.planetPositions.find { it.planet == Planet.JUPITER }
        val fifthPlanets = chart.planetPositions.filter { it.house == 5 }
        val sunPos = chart.planetPositions.find { it.planet == Planet.SUN }

        // Saturn in 5th without benefic aspect
        if (fifthPlanets.any { it.planet == Planet.SATURN }) {
            val saturnHasBeneficAspect = chart.planetPositions.any { pos ->
                VedicAstrologyUtils.isNaturalBenefic(pos.planet) &&
                        VedicAstrologyUtils.aspectsHouse(pos.planet, pos.house, 5)
            }
            if (!saturnHasBeneficAspect) {
                challenges.add("Saturn in 5th without benefic aspect may delay children")
            }
        }

        // Rahu in 5th
        if (fifthPlanets.any { it.planet == Planet.RAHU }) {
            challenges.add("Rahu in 5th may indicate unconventional paths to parenthood")
        }

        // Ketu in 5th
        if (fifthPlanets.any { it.planet == Planet.KETU }) {
            challenges.add("Ketu in 5th may indicate past-life karma related to children")
        }

        // Jupiter combust
        if (jupiterPos != null && sunPos != null) {
            val isCombust = VedicAstrologyUtils.isCombust(
                Planet.JUPITER, jupiterPos.longitude, sunPos.longitude, jupiterPos.isRetrograde
            )
            if (isCombust) {
                challenges.add("Combust Jupiter weakens progeny significations")
            }
        }

        // Fifth lord in dusthana
        val fifthLord = ZodiacSign.entries[(chart.ascendantSign.ordinal + 4) % 12].ruler
        val fifthLordPos = chart.planetPositions.find { it.planet == fifthLord }
        if (fifthLordPos != null && fifthLordPos.house in VedicAstrologyUtils.DUSTHANA_HOUSES) {
            challenges.add("Fifth lord in dusthana (6th/8th/12th) may create obstacles")
        }

        return challenges
    }

    // ============================================
    // HELPER METHODS
    // ============================================

    private fun dignityToStrength(dignity: VedicAstrologyUtils.PlanetaryDignity): Double {
        return when (dignity) {
            VedicAstrologyUtils.PlanetaryDignity.EXALTED -> 100.0
            VedicAstrologyUtils.PlanetaryDignity.MOOLATRIKONA -> 85.0
            VedicAstrologyUtils.PlanetaryDignity.OWN_SIGN -> 80.0
            VedicAstrologyUtils.PlanetaryDignity.FRIEND_SIGN -> 65.0
            VedicAstrologyUtils.PlanetaryDignity.NEUTRAL_SIGN -> 50.0
            VedicAstrologyUtils.PlanetaryDignity.ENEMY_SIGN -> 35.0
            VedicAstrologyUtils.PlanetaryDignity.DEBILITATED -> 20.0
        }
    }

    private fun calculateJupiterStrength(
        pos: PlanetPosition,
        dignity: VedicAstrologyUtils.PlanetaryDignity,
        isRetrograde: Boolean,
        isCombust: Boolean,
        aspects: List<Planet>
    ): Double {
        var strength = dignityToStrength(dignity)

        if (isRetrograde) strength -= 10
        if (isCombust) strength -= 20

        // Benefic aspects add strength
        aspects.filter { VedicAstrologyUtils.isNaturalBenefic(it) }.forEach { strength += 5 }
        // Malefic aspects reduce strength
        aspects.filter { VedicAstrologyUtils.isNaturalMalefic(it) }.forEach { strength -= 5 }

        // House position
        if (pos.house in listOf(1, 4, 5, 7, 9, 10)) strength += 10
        if (pos.house in listOf(6, 8, 12)) strength -= 10

        return strength.coerceIn(0.0, 100.0)
    }

    private fun getChildCharacteristics(planet: Planet, pos: PlanetPosition?): List<String> {
        val characteristics = mutableListOf<String>()

        when (planet) {
            Planet.SUN -> {
                characteristics.add("Confident and authoritative")
                characteristics.add("Natural leadership qualities")
                characteristics.add("Strong willpower")
            }
            Planet.MOON -> {
                characteristics.add("Emotionally sensitive")
                characteristics.add("Nurturing nature")
                characteristics.add("Intuitive and caring")
            }
            Planet.MARS -> {
                characteristics.add("Energetic and active")
                characteristics.add("Competitive spirit")
                characteristics.add("Physically strong")
            }
            Planet.MERCURY -> {
                characteristics.add("Intelligent and communicative")
                characteristics.add("Good at studies")
                characteristics.add("Quick learner")
            }
            Planet.JUPITER -> {
                characteristics.add("Wise and learned")
                characteristics.add("Righteous nature")
                characteristics.add("Optimistic outlook")
            }
            Planet.VENUS -> {
                characteristics.add("Artistic and creative")
                characteristics.add("Charming personality")
                characteristics.add("Appreciation for beauty")
            }
            Planet.SATURN -> {
                characteristics.add("Disciplined and hardworking")
                characteristics.add("Mature for their age")
                characteristics.add("Responsible nature")
            }
            Planet.RAHU -> {
                characteristics.add("Unconventional thinking")
                characteristics.add("Ambitious nature")
                characteristics.add("Innovative mind")
            }
            Planet.KETU -> {
                characteristics.add("Spiritually inclined")
                characteristics.add("Detached nature")
                characteristics.add("Intuitive abilities")
            }
            else -> {}
        }

        return characteristics
    }

    private fun getRelationshipQuality(planet: Planet, pos: PlanetPosition?, chart: VedicChart): RelationshipQuality {
        if (pos == null) return RelationshipQuality.MODERATE

        val dignity = VedicAstrologyUtils.getDignity(pos)
        val isInGoodHouse = pos.house in listOf(1, 4, 5, 7, 9, 10, 11)
        val isBenefic = VedicAstrologyUtils.isNaturalBenefic(planet)

        return when {
            dignity == VedicAstrologyUtils.PlanetaryDignity.EXALTED && isInGoodHouse -> RelationshipQuality.EXCELLENT
            (dignity == VedicAstrologyUtils.PlanetaryDignity.OWN_SIGN || isBenefic) && isInGoodHouse -> RelationshipQuality.GOOD
            dignity == VedicAstrologyUtils.PlanetaryDignity.DEBILITATED -> RelationshipQuality.CHALLENGING
            pos.house in listOf(6, 8, 12) -> RelationshipQuality.DIFFICULT
            else -> RelationshipQuality.MODERATE
        }
    }

    private fun getHealthIndicatorsForChild(planet: Planet, pos: PlanetPosition?): List<String> {
        val indicators = mutableListOf<String>()

        when (planet) {
            Planet.SUN -> indicators.add("Watch for heart and bone health")
            Planet.MOON -> indicators.add("Emotional well-being important")
            Planet.MARS -> indicators.add("Prone to injuries and inflammations")
            Planet.MERCURY -> indicators.add("Nervous system health")
            Planet.JUPITER -> indicators.add("Generally good health, watch weight")
            Planet.VENUS -> indicators.add("Reproductive health, diabetes")
            Planet.SATURN -> indicators.add("Chronic conditions, bones")
            Planet.RAHU -> indicators.add("Unusual health conditions")
            Planet.KETU -> indicators.add("Mysterious ailments")
            else -> {}
        }

        return indicators
    }

    private fun getCareerIndicatorsForChild(planet: Planet, pos: PlanetPosition?): List<String> {
        val indicators = mutableListOf<String>()

        when (planet) {
            Planet.SUN -> indicators.addAll(listOf("Government", "Medicine", "Leadership"))
            Planet.MOON -> indicators.addAll(listOf("Hospitality", "Psychology", "Public service"))
            Planet.MARS -> indicators.addAll(listOf("Military", "Engineering", "Sports"))
            Planet.MERCURY -> indicators.addAll(listOf("Business", "Writing", "Teaching"))
            Planet.JUPITER -> indicators.addAll(listOf("Education", "Law", "Finance"))
            Planet.VENUS -> indicators.addAll(listOf("Arts", "Entertainment", "Fashion"))
            Planet.SATURN -> indicators.addAll(listOf("Engineering", "Mining", "Labor"))
            Planet.RAHU -> indicators.addAll(listOf("Technology", "Foreign", "Politics"))
            Planet.KETU -> indicators.addAll(listOf("Research", "Spiritual", "Healing"))
            else -> {}
        }

        return indicators
    }

    private fun getTimingIndicators(planet: Planet, orderNumber: Int): List<String> {
        return listOf(
            "Birth likely during ${planet.displayName} Dasha or Antardasha",
            "Age ${if (orderNumber == 1) "25-30" else if (orderNumber == 2) "28-35" else "30-40"} typically favorable",
            "Check transit of Jupiter over 5th house"
        )
    }

    private fun determineChildIndications(
        planetsInFifth: List<PlanetPosition>,
        fifthLordPos: PlanetPosition?,
        chart: VedicChart
    ): List<ChildIndication> {
        return getChildIndications(null, chart, FifthHouseAnalysis(
            ZodiacSign.ARIES, Planet.MARS, null, null, emptyList(), emptyList(), emptyList(), ""
        ))
    }

    // ============================================
    // INTERPRETATION BUILDERS
    // ============================================

    private fun buildD7LagnaInterpretation(
        lagnaSign: ZodiacSign,
        lagnaLordPos: PlanetPosition?,
        planetsInLagna: List<PlanetPosition>
    ): String {
        return buildString {
            append("D7 Lagna in ${lagnaSign.displayName} indicates ")
            append(
                when (lagnaSign) {
                    ZodiacSign.ARIES -> "active and independent approach to parenting."
                    ZodiacSign.TAURUS -> "stable and nurturing approach to children."
                    ZodiacSign.GEMINI -> "communicative and educationally focused parenting."
                    ZodiacSign.CANCER -> "deeply nurturing and protective nature."
                    ZodiacSign.LEO -> "proud and generous with children."
                    ZodiacSign.VIRGO -> "practical and health-conscious approach."
                    ZodiacSign.LIBRA -> "balanced and fair parenting style."
                    ZodiacSign.SCORPIO -> "intense and transformative relationships."
                    ZodiacSign.SAGITTARIUS -> "philosophical and freedom-giving nature."
                    ZodiacSign.CAPRICORN -> "disciplined and responsible approach."
                    ZodiacSign.AQUARIUS -> "unconventional and progressive parenting."
                    ZodiacSign.PISCES -> "compassionate and spiritual connection."
                }
            )

            if (planetsInLagna.isNotEmpty()) {
                append(" ${planetsInLagna.joinToString { it.planet.displayName }} in D7 Lagna colors the parenting style.")
            }
        }
    }

    private fun buildFifthHouseInterpretation(
        fifthSign: ZodiacSign,
        fifthLordPos: PlanetPosition?,
        planetsInFifth: List<PlanetPosition>,
        aspectsOnFifth: List<Planet>
    ): String {
        return buildString {
            append("Fifth house in ${fifthSign.displayName}. ")

            if (planetsInFifth.isNotEmpty()) {
                append("Occupied by ${planetsInFifth.joinToString { it.planet.displayName }}. ")
            }

            if (aspectsOnFifth.isNotEmpty()) {
                append("Aspected by ${aspectsOnFifth.joinToString { it.displayName }}. ")
            }

            fifthLordPos?.let { pos ->
                append("Fifth lord ${pos.planet.displayName} placed in house ${pos.house}.")
            }
        }
    }

    private fun buildJupiterInterpretation(
        pos: PlanetPosition,
        dignity: VedicAstrologyUtils.PlanetaryDignity,
        isRetrograde: Boolean,
        isCombust: Boolean,
        strength: Double
    ): String {
        return buildString {
            append("Jupiter (Putrakaraka) is in ${pos.sign.displayName} in house ${pos.house}. ")
            append("Dignity: ${dignity.name.lowercase().replace("_", " ")}. ")

            if (isRetrograde) append("Retrograde status may indicate delayed but karmic children. ")
            if (isCombust) append("Combustion weakens Jupiter's significations. ")

            append("Overall strength: ${String.format("%.1f", strength)}%. ")

            append(
                when {
                    strength >= 75 -> "Excellent for progeny matters."
                    strength >= 50 -> "Moderate support for children."
                    else -> "May need attention for progeny happiness."
                }
            )
        }
    }

    private fun getApproachToChildren(lagnaSign: ZodiacSign, planetsInLagna: List<PlanetPosition>): String {
        val baseApproach = when (lagnaSign) {
            ZodiacSign.ARIES -> "Enthusiastic and encouraging"
            ZodiacSign.TAURUS -> "Patient and providing material security"
            ZodiacSign.GEMINI -> "Intellectual and communicative"
            ZodiacSign.CANCER -> "Emotionally nurturing and protective"
            ZodiacSign.LEO -> "Proud and generous with recognition"
            ZodiacSign.VIRGO -> "Practical and health-focused"
            ZodiacSign.LIBRA -> "Balanced and aesthetically oriented"
            ZodiacSign.SCORPIO -> "Deeply bonded and transformative"
            ZodiacSign.SAGITTARIUS -> "Philosophical and freedom-giving"
            ZodiacSign.CAPRICORN -> "Disciplined and responsibility-focused"
            ZodiacSign.AQUARIUS -> "Unconventional and socially conscious"
            ZodiacSign.PISCES -> "Compassionate and spiritually nurturing"
        }

        return baseApproach
    }

    // ============================================
    // SCORING AND INSIGHTS
    // ============================================

    private fun calculateOverallScore(
        d7Lagna: D7LagnaAnalysis,
        fifthHouse: FifthHouseAnalysis,
        jupiter: JupiterAnalysis,
        yogas: List<SanthanaYoga>,
        challenges: List<String>
    ): Double {
        var score = 50.0

        // D7 Lagna lord strength
        d7Lagna.lagnaLordDignity?.let {
            score += (dignityToStrength(it) - 50) * 0.2
        }

        // Fifth lord strength
        fifthHouse.fifthLordDignity?.let {
            score += (dignityToStrength(it) - 50) * 0.3
        }

        // Jupiter strength (most important)
        score += (jupiter.strength - 50) * 0.4

        // Yogas bonus
        yogas.filter { it.type == YogaType.FAVORABLE }.forEach {
            score += it.strength * 0.1
        }

        // Challenges penalty
        score -= challenges.size * 3

        return score.coerceIn(0.0, 100.0)
    }

    private fun generateInsights(
        d7Lagna: D7LagnaAnalysis,
        fifthHouse: FifthHouseAnalysis,
        jupiter: JupiterAnalysis,
        childCount: ChildCountFactors,
        yogas: List<SanthanaYoga>
    ): List<String> {
        val insights = mutableListOf<String>()

        insights.add("D7 Lagna in ${d7Lagna.lagnaSign.displayName} - ${d7Lagna.approachToChildren}")

        if (jupiter.strength >= 70) {
            insights.add("Strong Jupiter is excellent for progeny matters")
        }

        if (childCount.estimatedRange.first > 0) {
            insights.add("Estimated ${childCount.estimatedRange.first}-${childCount.estimatedRange.last} children based on chart factors")
        }

        if (yogas.isNotEmpty()) {
            insights.add("${yogas.first().name} present - ${yogas.first().effect}")
        }

        return insights.take(5)
    }

    private fun generateRecommendations(
        fertility: FertilityAnalysis,
        challenges: List<String>,
        jupiter: JupiterAnalysis
    ): List<String> {
        val recommendations = mutableListOf<String>()

        if (fertility.fertilityStatus == FertilityStatus.CHALLENGING ||
            fertility.fertilityStatus == FertilityStatus.NEEDS_ATTENTION
        ) {
            recommendations.addAll(fertility.remedies.take(3))
        }

        if (jupiter.isCombust) {
            recommendations.add("Strengthen Jupiter through Guru mantra jaap")
        }

        if (challenges.isNotEmpty()) {
            recommendations.add("Address challenges through appropriate remedies")
        }

        return recommendations.take(5)
    }

    // ============================================
    // DEFAULT/EMPTY CREATORS
    // ============================================

    private fun createDefaultD7LagnaAnalysis(chart: VedicChart): D7LagnaAnalysis {
        val lagnaSign = VedicAstrologyUtils.getAscendantSign(chart)
        return D7LagnaAnalysis(
            lagnaSign = lagnaSign,
            lagnaLord = lagnaSign.ruler,
            lagnaLordPosition = null,
            lagnaLordDignity = null,
            planetsInLagna = emptyList(),
            interpretation = "D7 chart data unavailable, using Rasi chart reference",
            approachToChildren = getApproachToChildren(lagnaSign, emptyList())
        )
    }

    private fun createDefaultJupiterAnalysis(): JupiterAnalysis {
        return JupiterAnalysis(
            position = null,
            dignity = null,
            house = 0,
            isRetrograde = false,
            isCombust = false,
            aspects = emptyList(),
            strength = 50.0,
            interpretation = "Jupiter position unavailable"
        )
    }

    private fun generateChartId(chart: VedicChart): String {
        val birthData = chart.birthData
        return "D7-${birthData.name}-${birthData.dateTime}".replace(Regex("[^a-zA-Z0-9-]"), "_")
    }

    /**
     * Get summary text for display
     */
    fun getSummary(analysis: SaptamsaAnalysis): String {
        return buildString {
            appendLine("═══════════════════════════════════════")
            appendLine("SAPTAMSA (D7) - PROGENY ANALYSIS")
            appendLine("═══════════════════════════════════════")
            appendLine()
            appendLine("D7 Lagna: ${analysis.d7LagnaAnalysis.lagnaSign.displayName}")
            appendLine("Fifth House: ${analysis.fifthHouseAnalysis.fifthSign.displayName}")
            appendLine()
            appendLine("Jupiter Strength: ${String.format("%.1f", analysis.jupiterAnalysis.strength)}%")
            appendLine("Fertility Status: ${analysis.fertilityAnalysis.fertilityStatus.displayName}")
            appendLine()
            appendLine("Estimated Children: ${analysis.childCountEstimate.estimatedRange}")
            appendLine()
            appendLine("Overall Score: ${String.format("%.1f", analysis.overallScore)}%")
            appendLine()

            if (analysis.santhanaYogas.isNotEmpty()) {
                appendLine("Favorable Yogas:")
                analysis.santhanaYogas.take(2).forEach { yoga ->
                    appendLine("  ✓ ${yoga.name}")
                }
            }

            if (analysis.challengingYogas.isNotEmpty()) {
                appendLine()
                appendLine("Challenges:")
                analysis.challengingYogas.take(2).forEach { challenge ->
                    appendLine("  ⚠ $challenge")
                }
            }
        }
    }
}
