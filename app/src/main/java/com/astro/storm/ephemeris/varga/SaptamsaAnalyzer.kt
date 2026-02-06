package com.astro.storm.ephemeris.varga

import Language
import StringKeyInterface
import com.astro.storm.core.common.StringKeySaptamsa
import com.astro.storm.core.common.StringResources
import com.astro.storm.core.model.Planet
import com.astro.storm.core.model.PlanetPosition
import com.astro.storm.core.model.VedicChart
import com.astro.storm.core.model.ZodiacSign
import com.astro.storm.ephemeris.DivisionalChartCalculator
import com.astro.storm.ephemeris.DivisionalChartType
import com.astro.storm.ephemeris.VedicAstrologyUtils
import com.astro.storm.ephemeris.DivisionalChartData

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
        Planet.JUPITER to StringKeySaptamsa.KARAKA_JUPITER,
        Planet.MOON to StringKeySaptamsa.KARAKA_MOON,
        Planet.VENUS to StringKeySaptamsa.KARAKA_VENUS,
        Planet.MERCURY to StringKeySaptamsa.KARAKA_MERCURY,
        Planet.SUN to StringKeySaptamsa.KARAKA_SUN,
        Planet.MARS to StringKeySaptamsa.KARAKA_MARS,
        Planet.SATURN to StringKeySaptamsa.KARAKA_SATURN,
        Planet.RAHU to StringKeySaptamsa.KARAKA_RAHU,
        Planet.KETU to StringKeySaptamsa.KARAKA_KETU
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
        val modifyingFactors: List<StringKeyInterface>
    )

    /**
     * Individual child indication
     */
    data class ChildIndication(
        val orderNumber: Int,
        val indicatingPlanet: Planet,
        val gender: ChildGender,
        val genderConfidence: Double,
        val characteristics: List<StringKeyInterface>,
        val relationshipQuality: RelationshipQuality,
        val healthIndications: List<StringKeyInterface>,
        val careerIndications: List<StringKeyInterface>,
        val timingIndicators: List<String>
    )

    enum class ChildGender(val key: com.astro.storm.core.common.StringKeySaptamsa) {
        MALE(com.astro.storm.core.common.StringKeySaptamsa.GENDER_MALE),
        FEMALE(com.astro.storm.core.common.StringKeySaptamsa.GENDER_FEMALE),
        UNCERTAIN(com.astro.storm.core.common.StringKeySaptamsa.GENDER_UNCERTAIN);

        val displayName: String get() = key.en
    }

    enum class RelationshipQuality(val key: com.astro.storm.core.common.StringKeySaptamsa, val score: Int) {
        EXCELLENT(com.astro.storm.core.common.StringKeySaptamsa.QUAL_EXCELLENT, 90),
        GOOD(com.astro.storm.core.common.StringKeySaptamsa.QUAL_GOOD, 75),
        MODERATE(com.astro.storm.core.common.StringKeySaptamsa.QUAL_MODERATE, 60),
        CHALLENGING(com.astro.storm.core.common.StringKeySaptamsa.QUAL_CHALLENGING, 40),
        DIFFICULT(com.astro.storm.core.common.StringKeySaptamsa.QUAL_DIFFICULT, 25);

        val displayName: String get() = key.en
    }

    /**
     * Fertility analysis result
     */
    data class FertilityAnalysis(
        val overallScore: Double, // 0-100
        val fifthHouseScore: Double,
        val jupiterScore: Double,
        val moonScore: Double,
        val fertilityStatus: FertilityStatus,
        val supportingFactors: List<StringKeyInterface>,
        val challengingFactors: List<StringKeyInterface>,
        val timingForConception: List<ConceptionTiming>,
        val remedies: List<StringKeyInterface>
    )

    enum class FertilityStatus(val key: com.astro.storm.core.common.StringKeySaptamsa) {
        HIGHLY_FAVORABLE(com.astro.storm.core.common.StringKeySaptamsa.STATUS_HIGHLY_FAVORABLE),
        FAVORABLE(com.astro.storm.core.common.StringKeySaptamsa.STATUS_FAVORABLE),
        MODERATE(com.astro.storm.core.common.StringKeySaptamsa.STATUS_MODERATE),
        CHALLENGING(com.astro.storm.core.common.StringKeySaptamsa.STATUS_CHALLENGING),
        NEEDS_ATTENTION(com.astro.storm.core.common.StringKeySaptamsa.STATUS_NEEDS_ATTENTION);

        val displayName: String get() = key.en
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
        val approachToChildren: StringKeyInterface
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
        val d7Chart: DivisionalChartData?,
        val d7LagnaAnalysis: D7LagnaAnalysis,
        val fifthHouseAnalysis: FifthHouseAnalysis,
        val jupiterAnalysis: JupiterAnalysis,
        val childCountEstimate: ChildCountFactors,
        val childIndications: List<ChildIndication>,
        val fertilityAnalysis: FertilityAnalysis,
        val santhanaYogas: List<SanthanaYoga>,
        val challengingYogas: List<StringKeyInterface>,
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
        val strength: Double,
        val nameKey: com.astro.storm.core.common.StringKeySaptamsa,
        val sanskritKey: com.astro.storm.core.common.StringKeySaptamsa,
        val effectKey: com.astro.storm.core.common.StringKeySaptamsa
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
    fun analyzeSaptamsa(chart: VedicChart, language: Language = Language.ENGLISH): SaptamsaAnalysis {
        // Get D7 chart
        val divisionalCharts = DivisionalChartCalculator.calculateAllDivisionalCharts(chart)
        val d7Chart = divisionalCharts.find { it.chartType == DivisionalChartType.D7_SAPTAMSA }

        val positions = d7Chart?.planetPositions ?: chart.planetPositions
        val ascendant = d7Chart?.ascendantLongitude ?: chart.ascendant
        val ascSign = ZodiacSign.fromLongitude(ascendant)

        // D7 Lagna analysis
        val d7LagnaAnalysis = analyzeD7Lagna(d7Chart, chart, language)

        // Fifth house analysis
        val fifthHouseAnalysis = analyzeFifthHouse(d7Chart, chart, language)

        // Jupiter analysis
        val jupiterAnalysis = analyzeJupiter(d7Chart, chart, language)

        // Child count estimation
        val childCountEstimate = estimateChildCount(d7Chart, chart, fifthHouseAnalysis, jupiterAnalysis)

        // Individual child indications
        val childIndications = getChildIndications(positions, ascSign, fifthHouseAnalysis)

        // Fertility analysis
        val fertilityAnalysis = analyzeFertility(positions, jupiterAnalysis)

        // Identify Santhana Yogas
        val santhanaYogas = identifySanthanaYogas(positions, ascSign)
        val challengingYogas = identifyChallengingYogas(positions, ascSign)

        // Calculate overall score
        val overallScore = calculateOverallScore(
            d7LagnaAnalysis, fifthHouseAnalysis, jupiterAnalysis,
            santhanaYogas, challengingYogas
        )

        // Generate insights and recommendations
        val keyInsights = generateInsights(
            d7LagnaAnalysis, fifthHouseAnalysis, jupiterAnalysis,
            childCountEstimate, santhanaYogas, language
        )
        val recommendations = generateRecommendations(
            fertilityAnalysis, challengingYogas, jupiterAnalysis, language
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
    private fun analyzeD7Lagna(d7Chart: DivisionalChartData?, rasiChart: VedicChart, language: Language): D7LagnaAnalysis {
        if (d7Chart == null) {
            return createDefaultD7LagnaAnalysis(rasiChart, language)
        }

        val lagnaSign = ZodiacSign.fromLongitude(d7Chart.ascendantLongitude)
        val lagnaLord = lagnaSign.ruler
        val lagnaLordPos = d7Chart.planetPositions.find { it.planet == lagnaLord }
        val lagnaLordDignity = lagnaLordPos?.let { VedicAstrologyUtils.getDignity(it) }
        val planetsInLagna = d7Chart.planetPositions.filter { it.house == 1 }

        val interpretation = buildD7LagnaInterpretation(lagnaSign, lagnaLordPos, planetsInLagna, language)
        val approach = getApproachToChildrenKey(lagnaSign)

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
    private fun analyzeFifthHouse(d7Chart: DivisionalChartData?, rasiChart: VedicChart, language: Language): FifthHouseAnalysis {
        val positions = d7Chart?.planetPositions ?: rasiChart.planetPositions
        val ascendant = d7Chart?.ascendantLongitude ?: rasiChart.ascendant
        val lagnaSign = ZodiacSign.fromLongitude(ascendant)
        
        val fifthSign = ZodiacSign.entries[(lagnaSign.ordinal + 4) % 12]
        val fifthLord = fifthSign.ruler

        val fifthLordPos = positions.find { it.planet == fifthLord }
        val fifthLordDignity = fifthLordPos?.let { VedicAstrologyUtils.getDignity(it) }
        val planetsInFifth = positions.filter { it.house == 5 }

        val aspectsOnFifth = positions
            .filter { it.house != 5 }
            .filter { VedicAstrologyUtils.aspectsHouse(it.planet, it.house, 5) }
            .map { it.planet }

        val childIndications = determineChildIndications(planetsInFifth, fifthLordPos, positions, lagnaSign)

        val interpretation = buildFifthHouseInterpretation(
            fifthSign, fifthLordPos, planetsInFifth, aspectsOnFifth, language
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
    private fun analyzeJupiter(d7Chart: DivisionalChartData?, rasiChart: VedicChart, language: Language): JupiterAnalysis {
        val positions = d7Chart?.planetPositions ?: rasiChart.planetPositions
        val jupiterPos = positions.find { it.planet == Planet.JUPITER }
        val sunPos = positions.find { it.planet == Planet.SUN }

        if (jupiterPos == null) {
            return createDefaultJupiterAnalysis(language)
        }

        val dignity = VedicAstrologyUtils.getDignity(jupiterPos)
        val isRetrograde = jupiterPos.isRetrograde
        val isCombust = sunPos?.let {
            VedicAstrologyUtils.isCombust(Planet.JUPITER, jupiterPos.longitude, it.longitude, false)
        } ?: false

        val aspectingPlanets = positions
            .filter { it.planet != Planet.JUPITER }
            .filter { VedicAstrologyUtils.aspectsHouse(it.planet, it.house, jupiterPos.house) }
            .map { it.planet }

        val strength = calculateJupiterStrength(jupiterPos, dignity, isRetrograde, isCombust, aspectingPlanets)

        val interpretation = buildJupiterInterpretation(jupiterPos, dignity, isRetrograde, isCombust, strength, language)

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
        d7Chart: DivisionalChartData?,
        rasiChart: VedicChart,
        fifthHouseAnalysis: FifthHouseAnalysis,
        jupiterAnalysis: JupiterAnalysis
    ): ChildCountFactors {
        val positions = d7Chart?.planetPositions ?: rasiChart.planetPositions
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

        val modifyingFactors = mutableListOf<StringKeyInterface>()
        if (jupiterStrength > 70) modifyingFactors.add(StringKeySaptamsa.FACTOR_STRONG_JUPITER)
        if (benefics >= 2) modifyingFactors.add(StringKeySaptamsa.FACTOR_BENEFIC_5TH)
        if (malefics >= 2) modifyingFactors.add(StringKeySaptamsa.FACTOR_MALEFIC_5TH)

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
        positions: List<PlanetPosition>,
        ascSign: ZodiacSign,
        fifthHouseAnalysis: FifthHouseAnalysis
    ): List<ChildIndication> {
        val indications = mutableListOf<ChildIndication>()

        // First child - 5th house
        val firstChildPlanet = fifthHouseAnalysis.planetsInFifth.firstOrNull()?.planet
            ?: fifthHouseAnalysis.fifthLord
        indications.add(createChildIndication(1, firstChildPlanet, positions))

        // Second child - 7th from 5th (11th house)
        val eleventhPlanets = positions.filter { it.house == 11 }
        val secondChildPlanet = eleventhPlanets.firstOrNull()?.planet
            ?: ZodiacSign.entries[(ascSign.ordinal + 10) % 12].ruler
        indications.add(createChildIndication(2, secondChildPlanet, positions))

        // Third child - 3rd from 5th (7th house)
        val seventhPlanets = positions.filter { it.house == 7 }
        val thirdChildPlanet = seventhPlanets.firstOrNull()?.planet
            ?: ZodiacSign.entries[(ascSign.ordinal + 6) % 12].ruler
        indications.add(createChildIndication(3, thirdChildPlanet, positions))

        return indications
    }

    private fun createChildIndication(orderNumber: Int, planet: Planet, positions: List<PlanetPosition>): ChildIndication {
        val planetPos = positions.find { it.planet == planet }

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
        val relationship = getRelationshipQuality(planet, planetPos)
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
        positions: List<PlanetPosition>,
        jupiterAnalysis: JupiterAnalysis
    ): FertilityAnalysis {
        val supportingFactors = mutableListOf<StringKeyInterface>()
        val challengingFactors = mutableListOf<StringKeyInterface>()
        
        // Initialize component scores
        var fifthHouseScore = 50.0
        var jupiterScore = jupiterAnalysis.strength
        var moonScore = 50.0
        var score = 50.0

        // Jupiter strength logic
        if (jupiterAnalysis.strength > 70) {
            score += 15
            supportingFactors.add(StringKeySaptamsa.FERTILITY_JUP_STRONG)
        } else if (jupiterAnalysis.strength < 40) {
            score -= 15
            challengingFactors.add(StringKeySaptamsa.FERTILITY_JUP_WEAK)
        }

        // 5th house condition
        val fifthPlanets = positions.filter { it.house == 5 }
        val beneficsInFifth = fifthPlanets.count { VedicAstrologyUtils.isNaturalBenefic(it.planet) }
        val maleficsInFifth = fifthPlanets.count { VedicAstrologyUtils.isNaturalMalefic(it.planet) }

        if (beneficsInFifth >= 1) {
            score += 10
            fifthHouseScore += 20 // Boost specific score
            supportingFactors.add(StringKeySaptamsa.FERTILITY_BENEFIC_5TH)
        }
        if (maleficsInFifth >= 2) {
            score -= 15
            fifthHouseScore -= 20
            challengingFactors.add(StringKeySaptamsa.FERTILITY_MALEFIC_5TH)
        }

        // Moon's condition (important for conception)
        val moonPos = positions.find { it.planet == Planet.MOON }
        if (moonPos != null) {
            val moonDignity = VedicAstrologyUtils.getDignity(moonPos)
            if (moonDignity in listOf(
                    VedicAstrologyUtils.PlanetaryDignity.EXALTED,
                    VedicAstrologyUtils.PlanetaryDignity.OWN_SIGN
                )
            ) {
                score += 10
                moonScore += 25
                supportingFactors.add(StringKeySaptamsa.FERTILITY_MOON_STRONG)
            }
            if (moonDignity == VedicAstrologyUtils.PlanetaryDignity.DEBILITATED) {
                score -= 10
                moonScore -= 25
                challengingFactors.add(StringKeySaptamsa.FERTILITY_MOON_WEAK)
            }
        }

        // Venus condition (general fertility)
        val venusPos = positions.find { it.planet == Planet.VENUS }
        if (venusPos != null) {
            if (venusPos.house in listOf(5, 7, 1, 9)) {
                score += 8
                supportingFactors.add(StringKeySaptamsa.FERTILITY_VENUS_FAV)
            }
            if (venusPos.isRetrograde) {
                score -= 5
                challengingFactors.add(StringKeySaptamsa.FERTILITY_VENUS_RETRO)
            }
        }

        val status = when {
            score >= 75 -> FertilityStatus.HIGHLY_FAVORABLE
            score >= 60 -> FertilityStatus.FAVORABLE
            score >= 45 -> FertilityStatus.MODERATE
            score >= 30 -> FertilityStatus.CHALLENGING
            else -> FertilityStatus.NEEDS_ATTENTION
        }

        return FertilityAnalysis(
            overallScore = score.coerceIn(0.0, 100.0) / 100.0,
            fifthHouseScore = fifthHouseScore.coerceIn(0.0, 100.0) / 100.0,
            jupiterScore = jupiterScore.coerceIn(0.0, 100.0) / 100.0,
            moonScore = moonScore.coerceIn(0.0, 100.0) / 100.0,
            fertilityStatus = status,
            supportingFactors = supportingFactors,
            challengingFactors = challengingFactors,
            timingForConception = emptyList(), // TODO: Implement conception timing logic
            remedies = emptyList() // TODO: Implement specific remedies
        )
    }

    /**
     * Identify Santhana Yogas (favorable progeny yogas)
     */
    private fun identifySanthanaYogas(positions: List<PlanetPosition>, ascSign: ZodiacSign): List<SanthanaYoga> {
        val yogas = mutableListOf<SanthanaYoga>()

        val jupiterPos = positions.find { it.planet == Planet.JUPITER }
        val moonPos = positions.find { it.planet == Planet.MOON }
        val venusPos = positions.find { it.planet == Planet.VENUS }
        val fifthLord = ZodiacSign.entries[(ascSign.ordinal + 4) % 12].ruler
        val fifthLordPos = positions.find { it.planet == fifthLord }

        // 1. Jupiter in Kendra or Trikona
        if (jupiterPos != null && jupiterPos.house in (VedicAstrologyUtils.KENDRA_HOUSES + VedicAstrologyUtils.TRIKONA_HOUSES)) {
            yogas.add(
                SanthanaYoga(
                    name = "Guru Yoga for Progeny",
                    sanskritName = "Guru Santhana Yoga",
                    involvedPlanets = listOf(Planet.JUPITER),
                    type = YogaType.FAVORABLE,
                    effect = "Jupiter in Kendra/Trikona blesses with children",
                    strength = 80.0,
                    nameKey = StringKeySaptamsa.YOGA_GURU_NAME,
                    sanskritKey = StringKeySaptamsa.YOGA_GURU_SANSKRIT,
                    effectKey = StringKeySaptamsa.YOGA_GURU_EFFECT
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
                        strength = 75.0,
                        nameKey = StringKeySaptamsa.YOGA_5TH_LORD_NAME,
                        sanskritKey = StringKeySaptamsa.YOGA_5TH_LORD_SANSKRIT,
                        effectKey = StringKeySaptamsa.YOGA_5TH_LORD_EFFECT
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
                        strength = 85.0,
                        nameKey = StringKeySaptamsa.YOGA_GAJA_KESARI_NAME,
                        sanskritKey = StringKeySaptamsa.YOGA_GAJA_KESARI_SANSKRIT,
                        effectKey = StringKeySaptamsa.YOGA_GAJA_KESARI_EFFECT
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
                        strength = 70.0,
                        nameKey = StringKeySaptamsa.YOGA_VEN_JUP_NAME,
                        sanskritKey = StringKeySaptamsa.YOGA_VEN_JUP_SANSKRIT,
                        effectKey = StringKeySaptamsa.YOGA_VEN_JUP_EFFECT
                    )
                )
            }
        }

        return yogas
    }

    /**
     * Identify challenging yogas for progeny
     */
    private fun identifyChallengingYogas(positions: List<PlanetPosition>, ascSign: ZodiacSign): List<StringKeyInterface> {
        val challenges = mutableListOf<StringKeyInterface>()

        val jupiterPos = positions.find { it.planet == Planet.JUPITER }
        val fifthPlanets = positions.filter { it.house == 5 }
        val sunPos = positions.find { it.planet == Planet.SUN }

        // Saturn in 5th without benefic aspect
        if (fifthPlanets.any { it.planet == Planet.SATURN }) {
            val saturnHasBeneficAspect = positions.any { pos ->
                VedicAstrologyUtils.isNaturalBenefic(pos.planet) &&
                        VedicAstrologyUtils.aspectsHouse(pos.planet, pos.house, 5)
            }
            if (!saturnHasBeneficAspect) {
                challenges.add(StringKeySaptamsa.CHALLENGE_SATURN_5TH)
            }
        }

        // Rahu in 5th
        if (fifthPlanets.any { it.planet == Planet.RAHU }) {
            challenges.add(StringKeySaptamsa.CHALLENGE_RAHU_5TH)
        }

        // Ketu in 5th
        if (fifthPlanets.any { it.planet == Planet.KETU }) {
            challenges.add(StringKeySaptamsa.CHALLENGE_KETU_5TH)
        }

        // Jupiter combust
        if (jupiterPos != null && sunPos != null) {
            val isCombust = VedicAstrologyUtils.isCombust(
                Planet.JUPITER, jupiterPos.longitude, sunPos.longitude, jupiterPos.isRetrograde
            )
            if (isCombust) {
                challenges.add(StringKeySaptamsa.CHALLENGE_JUP_COMBUST)
            }
        }

        // Fifth lord in dusthana
        val fifthLord = ZodiacSign.entries[(ascSign.ordinal + 4) % 12].ruler
        val fifthLordPos = positions.find { it.planet == fifthLord }
        if (fifthLordPos != null && fifthLordPos.house in VedicAstrologyUtils.DUSTHANA_HOUSES) {
            challenges.add(StringKeySaptamsa.CHALLENGE_5TH_LORD_DUSTHANA)
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

    private fun getChildCharacteristicKeys(planet: Planet): List<StringKeyInterface> {
        val characteristics = mutableListOf<StringKeyInterface>()

        when (planet) {
            Planet.SUN -> {
                characteristics.add(StringKeySaptamsa.TRAIT_CONFIDENT)
                characteristics.add(StringKeySaptamsa.TRAIT_LEADERSHIP)
                characteristics.add(StringKeySaptamsa.TRAIT_WILLPOWER)
            }
            Planet.MOON -> {
                characteristics.add(StringKeySaptamsa.TRAIT_SENSITIVE)
                characteristics.add(StringKeySaptamsa.TRAIT_NURTURING)
                characteristics.add(StringKeySaptamsa.TRAIT_INTUITIVE)
            }
            Planet.MARS -> {
                characteristics.add(StringKeySaptamsa.TRAIT_ENERGETIC)
                characteristics.add(StringKeySaptamsa.TRAIT_COMPETITIVE)
                characteristics.add(StringKeySaptamsa.TRAIT_STRONG)
            }
            Planet.MERCURY -> {
                characteristics.add(StringKeySaptamsa.TRAIT_INTELLIGENT)
                characteristics.add(StringKeySaptamsa.TRAIT_STUDIES)
                characteristics.add(StringKeySaptamsa.TRAIT_LEARNER)
            }
            Planet.JUPITER -> {
                characteristics.add(StringKeySaptamsa.TRAIT_WISE)
                characteristics.add(StringKeySaptamsa.TRAIT_RIGHTEOUS)
                characteristics.add(StringKeySaptamsa.TRAIT_OPTIMISTIC)
            }
            Planet.VENUS -> {
                characteristics.add(StringKeySaptamsa.TRAIT_ARTISTIC)
                characteristics.add(StringKeySaptamsa.TRAIT_CHARMING)
                characteristics.add(StringKeySaptamsa.TRAIT_BEAUTY)
            }
            Planet.SATURN -> {
                characteristics.add(StringKeySaptamsa.TRAIT_DISCIPLINED)
                characteristics.add(StringKeySaptamsa.TRAIT_MATURE)
                characteristics.add(StringKeySaptamsa.TRAIT_RESPONSIBLE)
            }
            Planet.RAHU -> {
                characteristics.add(StringKeySaptamsa.TRAIT_UNCONVENTIONAL)
                characteristics.add(StringKeySaptamsa.TRAIT_AMBITIOUS)
                characteristics.add(StringKeySaptamsa.TRAIT_INNOVATIVE)
            }
            Planet.KETU -> {
                characteristics.add(StringKeySaptamsa.TRAIT_SPIRITUAL)
                characteristics.add(StringKeySaptamsa.TRAIT_DETACHED)
                characteristics.add(StringKeySaptamsa.TRAIT_INTUITIVE_ABILITIES)
            }
            else -> {}
        }

        return characteristics
    }

    private fun getRelationshipQuality(planet: Planet, pos: PlanetPosition?): RelationshipQuality {
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

    private fun getHealthIndicatorKeysForChild(planet: Planet): List<StringKeyInterface> {
        val indicators = mutableListOf<StringKeyInterface>()

        when (planet) {
            Planet.SUN -> indicators.add(StringKeySaptamsa.HEALTH_HEART_BONE)
            Planet.MOON -> indicators.add(StringKeySaptamsa.HEALTH_EMOTIONAL)
            Planet.MARS -> indicators.add(StringKeySaptamsa.HEALTH_INJURIES)
            Planet.MERCURY -> indicators.add(StringKeySaptamsa.HEALTH_NERVOUS)
            Planet.JUPITER -> indicators.add(StringKeySaptamsa.HEALTH_GENERAL)
            Planet.VENUS -> indicators.add(StringKeySaptamsa.HEALTH_REPRODUCTIVE)
            Planet.SATURN -> indicators.add(StringKeySaptamsa.HEALTH_CHRONIC)
            Planet.RAHU -> indicators.add(StringKeySaptamsa.HEALTH_UNUSUAL)
            Planet.KETU -> indicators.add(StringKeySaptamsa.HEALTH_MYSTERIOUS)
            else -> {}
        }

        return indicators
    }

    private fun getCareerIndicatorKeysForChild(planet: Planet): List<StringKeyInterface> {
        val indicators = mutableListOf<StringKeyInterface>()

        when (planet) {
            Planet.SUN -> indicators.add(StringKeySaptamsa.CAREER_GOVT)
            Planet.MOON -> indicators.add(StringKeySaptamsa.CAREER_HOSPITALITY)
            Planet.MARS -> indicators.add(StringKeySaptamsa.CAREER_MILITARY)
            Planet.MERCURY -> indicators.add(StringKeySaptamsa.CAREER_BUSINESS)
            Planet.JUPITER -> indicators.add(StringKeySaptamsa.CAREER_EDUCATION)
            Planet.VENUS -> indicators.add(StringKeySaptamsa.CAREER_ARTS)
            Planet.SATURN -> indicators.add(StringKeySaptamsa.CAREER_MINING)
            Planet.RAHU -> indicators.add(StringKeySaptamsa.CAREER_TECH)
            Planet.KETU -> indicators.add(StringKeySaptamsa.CAREER_RESEARCH)
            else -> {}
        }

        return indicators
    }

    private fun getTimingIndicatorStrings(planet: Planet, orderNumber: Int): List<String> {
        return listOf(
            "Birth likely during ${planet.displayName} Dasha or Antardasha",
            "Age ${if (orderNumber == 1) "25-30" else if (orderNumber == 2) "28-35" else "30-40"} typically favorable",
            "Check transit of Jupiter over 5th house"
        )
    }

    private fun determineChildIndications(
        planetsInFifth: List<PlanetPosition>,
        fifthLordPos: PlanetPosition?,
        positions: List<PlanetPosition>,
        ascSign: ZodiacSign
    ): List<ChildIndication> {
        val fifthLord = fifthLordPos?.planet ?: ZodiacSign.entries[(ascSign.ordinal + 4) % 12].ruler
        val dummyAnalysis = FifthHouseAnalysis(
            fifthSign = ZodiacSign.ARIES,
            fifthLord = fifthLord,
            fifthLordPosition = fifthLordPos,
            fifthLordDignity = null,
            planetsInFifth = planetsInFifth,
            aspectsOnFifth = emptyList(),
            childIndications = emptyList(),
            interpretation = ""
        )
        return getChildIndications(positions, ascSign, dummyAnalysis)
    }

    // ============================================
    // INTERPRETATION BUILDERS
    // ============================================

    private fun buildD7LagnaInterpretation(
        lagnaSign: ZodiacSign,
        lagnaLordPos: PlanetPosition?,
        planetsInLagna: List<PlanetPosition>,
        language: Language
    ): String {
        return buildString {
            append(StringResources.get(StringKeySaptamsa.SUMMARY_LAGNA, language, lagnaSign.getLocalizedName(language)))
            append(" ")
            append(
                when (lagnaSign) {
                    ZodiacSign.ARIES -> StringResources.get(StringKeySaptamsa.APPROACH_ENTHUSIASTIC, language)
                    ZodiacSign.TAURUS -> StringResources.get(StringKeySaptamsa.APPROACH_STABLE, language)
                    ZodiacSign.GEMINI -> StringResources.get(StringKeySaptamsa.APPROACH_COMMUNICATIVE, language)
                    ZodiacSign.CANCER -> StringResources.get(StringKeySaptamsa.APPROACH_NURTURING, language)
                    ZodiacSign.LEO -> StringResources.get(StringKeySaptamsa.APPROACH_PROUD, language)
                    ZodiacSign.VIRGO -> StringResources.get(StringKeySaptamsa.APPROACH_PRACTICAL, language)
                    ZodiacSign.LIBRA -> StringResources.get(StringKeySaptamsa.APPROACH_BALANCED, language)
                    ZodiacSign.SCORPIO -> StringResources.get(StringKeySaptamsa.APPROACH_INTENSE, language)
                    ZodiacSign.SAGITTARIUS -> StringResources.get(StringKeySaptamsa.APPROACH_PHILOSOPHICAL, language)
                    ZodiacSign.CAPRICORN -> StringResources.get(StringKeySaptamsa.APPROACH_DISCIPLINED, language)
                    ZodiacSign.AQUARIUS -> StringResources.get(StringKeySaptamsa.APPROACH_UNCONVENTIONAL, language)
                    ZodiacSign.PISCES -> StringResources.get(StringKeySaptamsa.APPROACH_COMPASSIONATE, language)
                }
            )

            if (planetsInLagna.isNotEmpty()) {
                append(" ")
                append(planetsInLagna.joinToString { it.planet.getLocalizedName(language) })
                append(" in D7 Lagna colors the parenting style.")
            }
        }
    }

    private fun buildFifthHouseInterpretation(
        fifthSign: ZodiacSign,
        fifthLordPos: PlanetPosition?,
        planetsInFifth: List<PlanetPosition>,
        aspectsOnFifth: List<Planet>,
        language: Language
    ): String {
        return buildString {
            append(StringResources.get(StringKeySaptamsa.INTERP_FIFTH_HOUSE, language, fifthSign.getLocalizedName(language)))

            if (planetsInFifth.isNotEmpty()) {
                append(StringResources.get(StringKeySaptamsa.INTERP_OCCUPIED_BY, language, planetsInFifth.joinToString { it.planet.getLocalizedName(language) }))
            }

            if (aspectsOnFifth.isNotEmpty()) {
                append(StringResources.get(StringKeySaptamsa.INTERP_ASPECTED_BY, language, aspectsOnFifth.joinToString { it.getLocalizedName(language) }))
            }

            fifthLordPos?.let { pos ->
                append(StringResources.get(StringKeySaptamsa.INTERP_LORD_PLACED, language, pos.planet.getLocalizedName(language), pos.house))
            }
        }
    }

    private fun buildJupiterInterpretation(
        pos: PlanetPosition,
        dignity: VedicAstrologyUtils.PlanetaryDignity,
        isRetrograde: Boolean,
        isCombust: Boolean,
        strength: Double,
        language: Language
    ): String {
        return buildString {
            append(StringResources.get(StringKeySaptamsa.INTERP_JUPITER_POS, language, pos.sign.getLocalizedName(language), pos.house))
            append(StringResources.get(StringKeySaptamsa.INTERP_DIGNITY, language, dignity.name.lowercase().replace("_", " ")))

            if (isRetrograde) append(StringResources.get(StringKeySaptamsa.INTERP_RETROGRADE, language))
            if (isCombust) append(StringResources.get(StringKeySaptamsa.INTERP_COMBUST, language))

            append(StringResources.get(StringKeySaptamsa.INTERP_STRENGTH, language, String.format("%.1f", strength)))

            append(
                when {
                    strength >= 75 -> StringResources.get(StringKeySaptamsa.INTERP_PROGENY_EXCELLENT, language)
                    strength >= 50 -> StringResources.get(StringKeySaptamsa.INTERP_PROGENY_MODERATE, language)
                    else -> StringResources.get(StringKeySaptamsa.INTERP_PROGENY_ATTENTION, language)
                }
            )
        }
    }

    private fun getApproachToChildrenKey(lagnaSign: ZodiacSign): StringKeySaptamsa {
        return when (lagnaSign) {
            ZodiacSign.ARIES -> StringKeySaptamsa.PARENT_STYLE_ENTHUSIASTIC
            ZodiacSign.TAURUS -> StringKeySaptamsa.PARENT_STYLE_PATIENT
            ZodiacSign.GEMINI -> StringKeySaptamsa.PARENT_STYLE_INTELLECTUAL
            ZodiacSign.CANCER -> StringKeySaptamsa.PARENT_STYLE_PROTECTIVE
            ZodiacSign.LEO -> StringKeySaptamsa.PARENT_STYLE_CHARISMATIC
            ZodiacSign.VIRGO -> StringKeySaptamsa.PARENT_STYLE_HEALTH_CONSCIOUS
            ZodiacSign.LIBRA -> StringKeySaptamsa.PARENT_STYLE_BALANCED
            ZodiacSign.SCORPIO -> StringKeySaptamsa.PARENT_STYLE_BONDED
            ZodiacSign.SAGITTARIUS -> StringKeySaptamsa.PARENT_STYLE_FREE
            ZodiacSign.CAPRICORN -> StringKeySaptamsa.PARENT_STYLE_RESPONSIBLE
            ZodiacSign.AQUARIUS -> StringKeySaptamsa.PARENT_STYLE_SOCIAL
            ZodiacSign.PISCES -> StringKeySaptamsa.PARENT_STYLE_SPIRITUAL
        }
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
        yogas: List<SanthanaYoga>,
        language: Language
    ): List<String> {
        val insights = mutableListOf<String>()

        insights.add(StringResources.get(StringKeySaptamsa.INSIGHT_LAGNA, language, d7Lagna.lagnaSign.getLocalizedName(language), StringResources.get(d7Lagna.approachToChildren, language)))

        if (jupiter.strength >= 70) {
            insights.add(StringResources.get(StringKeySaptamsa.INSIGHT_STRONG_JUPITER, language))
        }

        if (childCount.estimatedRange.first > 0) {
            insights.add(StringResources.get(StringKeySaptamsa.INSIGHT_CHILD_ESTIMATE, language, childCount.estimatedRange.first, childCount.estimatedRange.last))
        }

        if (yogas.isNotEmpty()) {
            insights.add(StringResources.get(StringKeySaptamsa.INSIGHT_YOGA_PRESENT, language, StringResources.get(yogas.first().nameKey, language), StringResources.get(yogas.first().effectKey, language)))
        }

        return insights.take(5)
    }

    private fun generateRecommendations(
        fertility: FertilityAnalysis,
        challenges: List<StringKeyInterface>,
        jupiter: JupiterAnalysis,
        language: Language
    ): List<String> {
        val recommendations = mutableListOf<String>()

        if (fertility.fertilityStatus == FertilityStatus.CHALLENGING ||
            fertility.fertilityStatus == FertilityStatus.NEEDS_ATTENTION
        ) {
            recommendations.addAll(fertility.remedies.take(3).map { StringResources.get(it, language) })
        }

        if (jupiter.isCombust) {
            recommendations.add(StringResources.get(StringKeySaptamsa.REC_STRENGTHEN_JUPITER, language))
        }

        if (challenges.isNotEmpty()) {
            recommendations.add(StringResources.get(StringKeySaptamsa.REC_ADDRESS_CHALLENGES, language))
        }

        return recommendations.take(5)
    }

    // ============================================
    // DEFAULT/EMPTY CREATORS
    // ============================================

    private fun createDefaultD7LagnaAnalysis(chart: VedicChart, language: Language): D7LagnaAnalysis {
        val lagnaSign = VedicAstrologyUtils.getAscendantSign(chart)
        return D7LagnaAnalysis(
            lagnaSign = lagnaSign,
            lagnaLord = lagnaSign.ruler,
            lagnaLordPosition = null,
            lagnaLordDignity = null,
            planetsInLagna = emptyList(),
            interpretation = StringResources.get(StringKeySaptamsa.ERROR_DATA_UNAVAILABLE, language),
            approachToChildren = getApproachToChildrenKey(lagnaSign)
        )
    }

    private fun createDefaultJupiterAnalysis(language: Language): JupiterAnalysis {
        return JupiterAnalysis(
            position = null,
            dignity = null,
            house = 0,
            isRetrograde = false,
            isCombust = false,
            aspects = emptyList(),
            strength = 50.0,
            interpretation = StringResources.get(StringKeySaptamsa.ERROR_JUPITER_UNAVAILABLE, language)
        )
    }

    private fun generateChartId(chart: VedicChart): String {
        val birthData = chart.birthData
        return "D7-${birthData.name}-${birthData.dateTime}".replace(Regex("[^a-zA-Z0-9-]"), "_")
    }

    /**
     * Get summary text for display
     */
    fun getSummary(analysis: SaptamsaAnalysis, language: Language = Language.ENGLISH): String {
        return buildString {
            appendLine("═══════════════════════════════════════")
            appendLine(StringResources.get(StringKeySaptamsa.SUMMARY_HEADER, language))
            appendLine("═══════════════════════════════════════")
            appendLine()
            appendLine(StringResources.get(StringKeySaptamsa.SUMMARY_LAGNA, language, analysis.d7LagnaAnalysis.lagnaSign.getLocalizedName(language)))
            appendLine(StringResources.get(StringKeySaptamsa.SUMMARY_FIFTH_HOUSE, language, analysis.fifthHouseAnalysis.fifthSign.getLocalizedName(language)))
            appendLine()
            appendLine(StringResources.get(StringKeySaptamsa.SUMMARY_JUPITER_STRENGTH, language, String.format("%.1f", analysis.jupiterAnalysis.strength)))
            appendLine(StringResources.get(StringKeySaptamsa.SUMMARY_FERTILITY_STATUS, language, StringResources.get(analysis.fertilityAnalysis.fertilityStatus.key, language)))
            appendLine()
            appendLine(StringResources.get(StringKeySaptamsa.SUMMARY_ESTIMATED_CHILDREN, language, analysis.childCountEstimate.estimatedRange.toString()))
            appendLine()
            appendLine(StringResources.get(StringKeySaptamsa.SUMMARY_OVERALL_SCORE, language, String.format("%.1f", analysis.overallScore)))
            appendLine()

            if (analysis.santhanaYogas.isNotEmpty()) {
                appendLine(StringResources.get(StringKeySaptamsa.SUMMARY_FAVORABLE_YOGAS, language))
                analysis.santhanaYogas.take(2).forEach { yoga ->
                    appendLine("  ✓ ${StringResources.get(yoga.nameKey, language)}")
                }
            }

            if (analysis.challengingYogas.isNotEmpty()) {
                appendLine()
                appendLine(StringResources.get(StringKeySaptamsa.SUMMARY_CHALLENGES, language))
                analysis.challengingYogas.take(2).forEach { challenge ->
                    appendLine("  ⚠ ${StringResources.get(challenge, language)}")
                }
            }
        }
    }

    // ============================================
    // HELPER FUNCTIONS FOR CHILD ANALYSIS
    // ============================================

    private fun getChildCharacteristics(planet: Planet, position: PlanetPosition?): List<StringKeyInterface> {
        val characteristics = mutableListOf<StringKeyInterface>()
        
        when (planet) {
            Planet.SUN -> {
                characteristics.add(StringKeySaptamsa.CHILD_CHAR_LEADER)
                characteristics.add(StringKeySaptamsa.CHILD_CHAR_CONFIDENT)
                characteristics.add(StringKeySaptamsa.CHILD_CHAR_AUTHORITATIVE)
            }
            Planet.MOON -> {
                characteristics.add(StringKeySaptamsa.CHILD_CHAR_CARING)
                characteristics.add(StringKeySaptamsa.CHILD_CHAR_INTUITIVE)
                characteristics.add(StringKeySaptamsa.CHILD_CHAR_EMOTIONAL)
            }
            Planet.MARS -> {
                characteristics.add(StringKeySaptamsa.CHILD_CHAR_ENERGETIC)
                characteristics.add(StringKeySaptamsa.CHILD_CHAR_DETERMINED)
                characteristics.add(StringKeySaptamsa.CHILD_CHAR_COURAGEOUS)
            }
            Planet.MERCURY -> {
                characteristics.add(StringKeySaptamsa.CHILD_CHAR_INTELLIGENT)
                characteristics.add(StringKeySaptamsa.CHILD_CHAR_COMMUNICATIVE)
                characteristics.add(StringKeySaptamsa.CHILD_CHAR_ADAPTABLE)
            }
            Planet.JUPITER -> {
                characteristics.add(StringKeySaptamsa.CHILD_CHAR_WISE)
                characteristics.add(StringKeySaptamsa.CHILD_CHAR_GENEROUS)
                characteristics.add(StringKeySaptamsa.CHILD_CHAR_SPIRITUAL)
            }
            Planet.VENUS -> {
                characteristics.add(StringKeySaptamsa.CHILD_CHAR_ARTISTIC)
                characteristics.add(StringKeySaptamsa.CHILD_CHAR_CHARMING)
                characteristics.add(StringKeySaptamsa.CHILD_CHAR_HARMONIOUS)
            }
            Planet.SATURN -> {
                characteristics.add(StringKeySaptamsa.CHILD_CHAR_DISCIPLINED)
                characteristics.add(StringKeySaptamsa.CHILD_CHAR_RESPONSIBLE)
                characteristics.add(StringKeySaptamsa.CHILD_CHAR_PRACTICAL)
            }
            Planet.RAHU -> {
                characteristics.add(StringKeySaptamsa.CHILD_CHAR_UNCONVENTIONAL)
                characteristics.add(StringKeySaptamsa.CHILD_CHAR_AMBITIOUS)
                characteristics.add(StringKeySaptamsa.CHILD_CHAR_MYSTERIOUS)
            }
            Planet.KETU -> {
                characteristics.add(StringKeySaptamsa.CHILD_CHAR_SPIRITUAL)
                characteristics.add(StringKeySaptamsa.CHILD_CHAR_DETACHED)
                characteristics.add(StringKeySaptamsa.CHILD_CHAR_INTUITIVE)
            }
        }
        
        // Add position-based characteristics
        position?.let { pos ->
            when (pos.house) {
                1 -> characteristics.add(StringKeySaptamsa.CHILD_POS_INDEPENDENT)
                4 -> characteristics.add(StringKeySaptamsa.CHILD_POS_FAMILY_ORIENTED)
                5 -> characteristics.add(StringKeySaptamsa.CHILD_POS_CREATIVE)
                7 -> characteristics.add(StringKeySaptamsa.CHILD_POS_PARTNERSHIP_MINDED)
                9 -> characteristics.add(StringKeySaptamsa.CHILD_POS_FORTUNATE)
                10 -> characteristics.add(StringKeySaptamsa.CHILD_POS_AMBITIOUS)
            }
        }
        
        return characteristics
    }

    private fun getHealthIndicatorsForChild(planet: Planet, position: PlanetPosition?): List<StringKeyInterface> {
        val indicators = mutableListOf<StringKeyInterface>()
        
        // Basic health indicators based on planet
        when (planet) {
            Planet.SUN -> indicators.add(StringKeySaptamsa.HEALTH_STRONG_VITALITY)
            Planet.MOON -> indicators.add(StringKeySaptamsa.HEALTH_EMOTIONAL_WELLNESS)
            Planet.MARS -> indicators.add(StringKeySaptamsa.HEALTH_PHYSICAL_STRENGTH)
            Planet.MERCURY -> indicators.add(StringKeySaptamsa.HEALTH_NERVOUS_SYSTEM)
            Planet.JUPITER -> indicators.add(StringKeySaptamsa.HEALTH_OVERALL_GOOD)
            Planet.VENUS -> indicators.add(StringKeySaptamsa.HEALTH_REPRODUCTIVE_HEALTH)
            Planet.SATURN -> indicators.add(StringKeySaptamsa.HEALTH_RESILIENT)
            Planet.RAHU -> indicators.add(StringKeySaptamsa.HEALTH_UNUSUAL_CONDITIONS)
            Planet.KETU -> indicators.add(StringKeySaptamsa.HEALTH_CHRONIC_TENDENCIES)
        }
        
        return indicators
    }

    private fun getCareerIndicatorsForChild(planet: Planet, position: PlanetPosition?): List<StringKeyInterface> {
        val indicators = mutableListOf<StringKeyInterface>()
        
        when (planet) {
            Planet.SUN -> {
                indicators.add(StringKeySaptamsa.CAREER_LEADERSHIP)
                indicators.add(StringKeySaptamsa.CAREER_GOVERNMENT)
            }
            Planet.MOON -> {
                indicators.add(StringKeySaptamsa.CAREER_CARING_PROFESSIONS)
                indicators.add(StringKeySaptamsa.CAREER_PUBLIC_SERVICE)
            }
            Planet.MARS -> {
                indicators.add(StringKeySaptamsa.CAREER_MILITARY)
                indicators.add(StringKeySaptamsa.CAREER_ENGINEERING)
                indicators.add(StringKeySaptamsa.CAREER_SPORTS)
            }
            Planet.MERCURY -> {
                indicators.add(StringKeySaptamsa.CAREER_COMMUNICATION)
                indicators.add(StringKeySaptamsa.CAREER_BUSINESS)
                indicators.add(StringKeySaptamsa.CAREER_TECHNOLOGY)
            }
            Planet.JUPITER -> {
                indicators.add(StringKeySaptamsa.CAREER_TEACHING)
                indicators.add(StringKeySaptamsa.CAREER_LAW)
                indicators.add(StringKeySaptamsa.CAREER_SPIRITUAL)
            }
            Planet.VENUS -> {
                indicators.add(StringKeySaptamsa.CAREER_ARTS)
                indicators.add(StringKeySaptamsa.CAREER_ENTERTAINMENT)
                indicators.add(StringKeySaptamsa.CAREER_DESIGN)
            }
            Planet.SATURN -> {
                indicators.add(StringKeySaptamsa.CAREER_ADMINISTRATION)
                indicators.add(StringKeySaptamsa.CAREER_INFRASTRUCTURE)
                indicators.add(StringKeySaptamsa.CAREER_RESEARCH)
            }
            Planet.RAHU -> {
                indicators.add(StringKeySaptamsa.CAREER_INTERNATIONAL)
                indicators.add(StringKeySaptamsa.CAREER_RESEARCH)
                indicators.add(StringKeySaptamsa.CAREER_TECHNOLOGY)
            }
            Planet.KETU -> {
                indicators.add(StringKeySaptamsa.CAREER_RESEARCH)
                indicators.add(StringKeySaptamsa.CAREER_SPIRITUAL)
                indicators.add(StringKeySaptamsa.CAREER_HEALING)
            }
        }
        
        return indicators
    }

    private fun getTimingIndicators(planet: Planet, childNumber: Int): List<String> {
        val indicators = mutableListOf<String>()
        
        // Calculate approximate timing based on child number
        val baseAge = 25 + (childNumber - 1) * 3
        
        indicators.add("Possible conception around age $baseAge-${baseAge + 3}")
        
        when (planet) {
            Planet.JUPITER -> indicators.add("Favorable during Jupiter dasha/bhukti")
            Planet.VENUS -> indicators.add("Favorable during Venus dasha/bhukti")
            Planet.MERCURY -> indicators.add("Favorable during Mercury dasha/bhukti")
            Planet.SUN -> indicators.add("Favorable during Sun dasha/bhukti")
            Planet.MOON -> indicators.add("Favorable during Moon dasha/bhukti")
            Planet.MARS -> indicators.add("Favorable during Mars dasha/bhukti")
            Planet.SATURN -> indicators.add("May have delays - check Saturn periods")
            Planet.RAHU -> indicators.add("Unexpected timing possible")
            Planet.KETU -> indicators.add("Spiritual significance in timing")
        }
        
        return indicators
    }
}
