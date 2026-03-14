package com.astro.vajra.ephemeris.prediction

import com.astro.vajra.core.model.LifeArea
import com.astro.vajra.core.model.Planet
import com.astro.vajra.core.model.PlanetPosition
import com.astro.vajra.core.model.VedicChart
import com.astro.vajra.core.model.ZodiacSign
import com.astro.vajra.ephemeris.AshtakavargaCalculator
import com.astro.vajra.ephemeris.DashaCalculator
import com.astro.vajra.ephemeris.VedicAstrologyUtils
import java.time.LocalDateTime
import java.time.ZoneOffset
import kotlin.math.max
import kotlin.math.pow

/**
 * Triple-Pillar Predictive Engine
 *
 * A comprehensive prediction system that synthesizes three core pillars of
 * Vedic astrological timing to produce probability-weighted forecasts for
 * specific life areas:
 *
 * **Pillar 1 -- Vimshottari Dasha (Planetary Periods)**
 * The dasha lord ruling the current Mahadasha, Antardasha, and Pratyantardasha
 * sets the overarching karmic theme. The natal strength and house-rulership of
 * each dasha lord determines its capacity to deliver results.
 *
 * **Pillar 2 -- Gochara (Transits)**
 * Real-time planetary transits measured from the natal Moon sign reveal the
 * immediate environmental influences. Classical Gochara rules (Phaladeepika,
 * Brihat Samhita) define favorable and unfavorable transit houses.
 *
 * **Pillar 3 -- Ashtakavarga (Bindu Scores)**
 * The Sarvashtakavarga and Bhinnashtakavarga scores quantify the intrinsic
 * receptivity of each sign to planetary transits. Signs with high SAV scores
 * (28+) amplify positive transits; low-scoring signs dampen them.
 *
 * The composite Success Probability for a life area peaks **only** when all
 * three pillars align favorably. The engine applies multiplicative weighting
 * so that a weakness in any single pillar suppresses the overall score, per
 * the classical principle that a planet must have both natal promise (dasha)
 * and transit activation (gochara + ashtakavarga) to manifest results.
 *
 * Additionally, the engine detects **Sandhi (junction) periods** between
 * dasha transitions where planetary influences are mixed and predictions
 * carry inherent uncertainty, following BPHS guidance on dasha transition
 * effects.
 *
 * References:
 * - Brihat Parashara Hora Shastra (BPHS), Chapters 46-50 on Dasha results
 * - Phaladeepika, Chapter 26 on Gochara
 * - BPHS Chapters 66-72 on Ashtakavarga
 * - Saravali on composite timing analysis
 *
 * @author AstroVajra -- Ultra-Precision Vedic Astrology
 */
object TriplePillarPredictiveEngine {

    // ========================================================================
    // DATA CLASSES
    // ========================================================================

    /**
     * Complete Triple-Pillar analysis result containing all three pillar states,
     * life area probability scores, a forecast timeline, and Sandhi period warnings.
     */
    data class TriplePillarAnalysis(
        /** Current Vimshottari Dasha state at the analysis moment */
        val currentDashaState: DashaState,
        /** Current transit positions and their Gochara classifications */
        val transitState: TransitState,
        /** Ashtakavarga SAV and BAV scores for all 12 signs */
        val ashtakavargaState: AshtakavargaState,
        /** Probability scores (0-100) for each life area */
        val lifeAreaProbabilities: Map<LifeArea, ProbabilityScore>,
        /** Forecast timeline with periodic data points */
        val timeline: List<TimelinePoint>,
        /** Detected Sandhi (dasha junction) periods */
        val sandhiPeriods: List<SandhiPeriod>,
        /** Synthesized overall forecast summary */
        val overallForecast: OverallForecast,
        /** Timestamp of analysis */
        val analysisTimestamp: LocalDateTime = LocalDateTime.now(ZoneOffset.UTC)
    )

    /**
     * Current state of the Vimshottari Dasha system, including the active
     * Mahadasha, Antardasha, and Pratyantardasha lords and their natal strengths.
     */
    data class DashaState(
        val mahadashaLord: Planet,
        val antardashaLord: Planet,
        val pratyantardashaLord: Planet?,
        val mahadashaStrength: PlanetaryStrength,
        val antardashaStrength: PlanetaryStrength,
        val pratyantardashaStrength: PlanetaryStrength?,
        val mahadashaStartDate: LocalDateTime,
        val mahadashaEndDate: LocalDateTime,
        val antardashaStartDate: LocalDateTime,
        val antardashaEndDate: LocalDateTime,
        val mahadashaProgress: Double,
        val antardashaProgress: Double
    )

    /**
     * Natal strength assessment of a planet based on dignity, house placement,
     * aspects received, retrograde status, combustion, and functional beneficence.
     */
    data class PlanetaryStrength(
        val planet: Planet,
        val dignity: VedicAstrologyUtils.PlanetaryDignity,
        val house: Int,
        val isRetrograde: Boolean,
        val isCombust: Boolean,
        val isInKendra: Boolean,
        val isInTrikona: Boolean,
        val isInDusthana: Boolean,
        val isFunctionalBenefic: Boolean,
        val isFunctionalMalefic: Boolean,
        val hasDigBala: Boolean,
        val receivesAspectFromBenefics: Boolean,
        val receivesAspectFromMalefics: Boolean,
        /** Houses this planet rules (1-12) */
        val ruledHouses: List<Int>,
        /** Composite strength score from 0.0 (weakest) to 1.0 (strongest) */
        val compositeScore: Double
    )

    /**
     * Current transit (Gochara) state showing each planet's transit position
     * relative to the natal Moon sign.
     */
    data class TransitState(
        val moonSign: ZodiacSign,
        val transitPositions: Map<Planet, TransitInfo>,
        /** Overall transit favorability from 0.0 to 1.0 */
        val overallTransitScore: Double
    )

    /**
     * Transit information for a single planet, including its house from Moon
     * and whether it is in a classically favorable, unfavorable, or neutral
     * transit position.
     */
    data class TransitInfo(
        val planet: Planet,
        val transitSign: ZodiacSign,
        val houseFromMoon: Int,
        val transitClassification: TransitClassification,
        val isRetrograde: Boolean,
        val speed: Double
    )

    /** Classical Gochara transit classification */
    enum class TransitClassification {
        FAVORABLE,
        NEUTRAL,
        UNFAVORABLE
    }

    /**
     * Ashtakavarga state containing SAV and BAV scores for all houses.
     */
    data class AshtakavargaState(
        val savScores: Map<Int, Int>,
        val bavScores: Map<Planet, Map<Int, Int>>,
        val strongHouses: List<Int>,
        val weakHouses: List<Int>,
        val totalSAV: Int
    )

    /**
     * Probability score for a specific life area, with component breakdowns.
     */
    data class ProbabilityScore(
        val lifeArea: LifeArea,
        /** Composite probability from 0 to 100 */
        val probability: Int,
        /** Dasha factor contribution (0.0 to 1.0) */
        val dashaFactor: Double,
        /** Transit factor contribution (0.0 to 1.0) */
        val transitFactor: Double,
        /** Ashtakavarga factor contribution (0.0 to 1.0) */
        val ashtakavargaFactor: Double,
        /** Qualitative assessment */
        val assessment: ProbabilityAssessment,
        /** Descriptive interpretation text */
        val interpretation: String,
        /** Primary houses evaluated for this life area */
        val relevantHouses: List<Int>
    )

    /** Qualitative tiers for probability assessments */
    enum class ProbabilityAssessment(val label: String) {
        EXCELLENT("Excellent"),
        GOOD("Good"),
        MODERATE("Moderate"),
        CHALLENGING("Challenging"),
        DIFFICULT("Difficult")
    }

    /**
     * A single point on the forecast timeline showing probability scores
     * at a specific date.
     */
    data class TimelinePoint(
        val date: LocalDateTime,
        val lifeAreaScores: Map<LifeArea, Int>,
        val dominantDashaLord: Planet,
        val isSandhiPeriod: Boolean,
        val overallScore: Int
    )

    /**
     * A Sandhi (junction) period between two dasha phases where planetary
     * influences overlap and predictions carry inherent uncertainty.
     */
    data class SandhiPeriod(
        val startDate: LocalDateTime,
        val endDate: LocalDateTime,
        val outgoingLord: Planet,
        val incomingLord: Planet,
        val level: SandhiLevel,
        val intensity: Double,
        val description: String
    )

    /** Sandhi classification by dasha level */
    enum class SandhiLevel(val label: String, val weight: Double) {
        MAHADASHA("Mahadasha Sandhi", 1.0),
        ANTARDASHA("Antardasha Sandhi", 0.6),
        PRATYANTARDASHA("Pratyantardasha Sandhi", 0.3)
    }

    /**
     * Synthesized overall forecast with a single summary score and guidance.
     */
    data class OverallForecast(
        /** Overall score from 0 to 100 */
        val overallScore: Int,
        val assessment: ProbabilityAssessment,
        val strongestArea: LifeArea,
        val weakestArea: LifeArea,
        val summary: String,
        val recommendations: List<String>
    )

    // ========================================================================
    // CONFIGURATION
    // ========================================================================

    /** Pillar weights for composite scoring (must sum to 1.0) */
    private const val DASHA_WEIGHT = 0.40
    private const val TRANSIT_WEIGHT = 0.35
    private const val ASHTAKAVARGA_WEIGHT = 0.25

    /** Sandhi period duration constants (in days) */
    private const val MAHADASHA_SANDHI_DAYS = 90L
    private const val ANTARDASHA_SANDHI_DAYS = 15L
    private const val PRATYANTARDASHA_SANDHI_DAYS = 3L

    /** Maximum SAV normalization constant (theoretical max of 56 per sign / 12 houses) */
    private const val SAV_MAX_NORMALIZATION = 4.67

    /**
     * Life area to house mapping. The first house in the list is the primary
     * significator; subsequent houses are secondary significators.
     */
    private val LIFE_AREA_HOUSES: Map<LifeArea, List<Int>> = mapOf(
        LifeArea.CAREER to listOf(10, 6, 2),
        LifeArea.FINANCE to listOf(2, 11, 5),
        LifeArea.HEALTH to listOf(1, 6, 8),
        LifeArea.RELATIONSHIPS to listOf(7, 5, 11),
        LifeArea.EDUCATION to listOf(5, 4, 9),
        LifeArea.SPIRITUAL to listOf(9, 12, 5)
    )

    /**
     * House weights within a life area (primary house weighted more heavily)
     */
    private val HOUSE_WEIGHTS = listOf(0.55, 0.30, 0.15)

    /**
     * Classical favorable transit houses from Moon (per Phaladeepika and BPHS)
     */
    private val FAVORABLE_TRANSIT_HOUSES: Map<Planet, Set<Int>> = mapOf(
        Planet.SUN to setOf(3, 6, 10, 11),
        Planet.MOON to setOf(1, 3, 6, 7, 10, 11),
        Planet.MARS to setOf(3, 6, 11),
        Planet.MERCURY to setOf(2, 4, 6, 8, 10, 11),
        Planet.JUPITER to setOf(2, 5, 7, 9, 11),
        Planet.VENUS to setOf(1, 2, 3, 4, 5, 8, 9, 11, 12),
        Planet.SATURN to setOf(3, 6, 11),
        Planet.RAHU to setOf(3, 6, 10, 11),
        Planet.KETU to setOf(3, 6, 10, 11)
    )

    /**
     * Classical unfavorable transit houses from Moon
     */
    private val UNFAVORABLE_TRANSIT_HOUSES: Map<Planet, Set<Int>> = mapOf(
        Planet.SUN to setOf(4, 7, 8, 9, 12),
        Planet.MOON to setOf(4, 8, 9, 12),
        Planet.MARS to setOf(2, 4, 5, 7, 8, 9, 12),
        Planet.MERCURY to setOf(7, 9, 12),
        Planet.JUPITER to setOf(3, 12),
        Planet.VENUS to setOf(6, 7, 10),
        Planet.SATURN to setOf(4, 5, 7, 8, 9, 12),
        Planet.RAHU to setOf(4, 7, 8, 9, 12),
        Planet.KETU to setOf(4, 7, 8, 9, 12)
    )

    // ========================================================================
    // MAIN ANALYSIS ENTRY POINT
    // ========================================================================

    /**
     * Perform a complete Triple-Pillar analysis for the given natal chart using
     * the provided transit positions. Generates probability scores for six core
     * life areas and an optional forecast timeline.
     *
     * @param natalChart The birth chart with planet positions and house cusps
     * @param transitPositions Current real-time planetary positions for Gochara
     * @param timelineStartDate Start date for forecast timeline (default: now)
     * @param timelineEndDate End date for forecast timeline (default: 1 year ahead)
     * @param timelineStepDays Step interval in days for timeline points (default: 30)
     * @return Complete [TriplePillarAnalysis] result
     */
    fun analyze(
        natalChart: VedicChart,
        transitPositions: List<PlanetPosition>,
        timelineStartDate: LocalDateTime = LocalDateTime.now(ZoneOffset.UTC),
        timelineEndDate: LocalDateTime = LocalDateTime.now(ZoneOffset.UTC).plusYears(1),
        timelineStepDays: Long = 30
    ): TriplePillarAnalysis {
        // ---- Pillar 1: Dasha State ----
        val dashaTimeline = DashaCalculator.calculateDashaTimeline(natalChart)
        val dashaState = buildDashaState(natalChart, dashaTimeline, timelineStartDate)

        // ---- Pillar 2: Transit State ----
        val transitState = buildTransitState(natalChart, transitPositions)

        // ---- Pillar 3: Ashtakavarga State ----
        val ashtakavargaAnalysis = AshtakavargaCalculator.calculateAshtakavarga(natalChart)
        val ashtakavargaState = buildAshtakavargaState(natalChart, ashtakavargaAnalysis)

        // ---- Composite Life Area Probabilities ----
        val lifeAreaProbabilities = computeLifeAreaProbabilities(
            natalChart, dashaState, transitState, ashtakavargaState
        )

        // ---- Sandhi Detection ----
        val sandhiPeriods = detectSandhiPeriods(dashaTimeline, timelineStartDate, timelineEndDate)

        // ---- Timeline Generation ----
        val timeline = generateTimeline(
            natalChart, dashaTimeline, ashtakavargaAnalysis,
            timelineStartDate, timelineEndDate, timelineStepDays, sandhiPeriods
        )

        // ---- Overall Forecast ----
        val overallForecast = synthesizeOverallForecast(lifeAreaProbabilities, sandhiPeriods)

        return TriplePillarAnalysis(
            currentDashaState = dashaState,
            transitState = transitState,
            ashtakavargaState = ashtakavargaState,
            lifeAreaProbabilities = lifeAreaProbabilities,
            timeline = timeline,
            sandhiPeriods = sandhiPeriods,
            overallForecast = overallForecast
        )
    }

    // ========================================================================
    // PILLAR 1: DASHA STATE BUILDER
    // ========================================================================

    /**
     * Constructs the [DashaState] from a pre-calculated Vimshottari dasha timeline.
     */
    private fun buildDashaState(
        chart: VedicChart,
        dashaTimeline: DashaCalculator.DashaTimeline,
        asOf: LocalDateTime
    ): DashaState {
        val currentMD = dashaTimeline.currentMahadasha
            ?: dashaTimeline.mahadashas.firstOrNull()
            ?: throw IllegalStateException("No Mahadasha data available")

        val currentAD = currentMD.getAntardashaOn(asOf)
            ?: currentMD.antardashas.firstOrNull()
            ?: throw IllegalStateException("No Antardasha data available")

        val pratyantardashas = DashaCalculator.calculatePratyantardashasForAntardasha(currentAD)
        val currentPAD = pratyantardashas.find { it.isActiveOn(asOf) }

        return DashaState(
            mahadashaLord = currentMD.planet,
            antardashaLord = currentAD.planet,
            pratyantardashaLord = currentPAD?.planet,
            mahadashaStrength = assessPlanetaryStrength(chart, currentMD.planet),
            antardashaStrength = assessPlanetaryStrength(chart, currentAD.planet),
            pratyantardashaStrength = currentPAD?.planet?.let { assessPlanetaryStrength(chart, it) },
            mahadashaStartDate = currentMD.startDate,
            mahadashaEndDate = currentMD.endDate,
            antardashaStartDate = currentAD.startDate,
            antardashaEndDate = currentAD.endDate,
            mahadashaProgress = currentMD.getProgressPercent(asOf),
            antardashaProgress = currentAD.getProgressPercent(asOf)
        )
    }

    /**
     * Performs a comprehensive natal strength assessment for a planet, combining
     * dignity, house placement, aspects, combustion, retrograde status, functional
     * beneficence, and directional strength into a single composite score.
     *
     * The composite score formula:
     *   base = dignity score (0.1 to 1.0)
     *   + house bonus (kendra/trikona) or penalty (dusthana)
     *   + aspect modifiers (benefic aspects boost, malefic aspects reduce)
     *   + combustion penalty
     *   + retrograde modifier (context-dependent)
     *   + dig bala bonus
     *   + functional status modifier
     *   all clamped to [0.0, 1.0]
     */
    private fun assessPlanetaryStrength(chart: VedicChart, planet: Planet): PlanetaryStrength {
        val pos = VedicAstrologyUtils.getPlanetPosition(chart, planet)
            ?: return createDefaultStrength(planet)

        val sunPos = VedicAstrologyUtils.getSunPosition(chart)
        val dignity = VedicAstrologyUtils.getDignity(pos)
        val isInKendra = VedicAstrologyUtils.isKendra(pos.house)
        val isInTrikona = VedicAstrologyUtils.isTrikona(pos.house)
        val isInDusthana = VedicAstrologyUtils.isDusthana(pos.house)
        val isCombust = sunPos?.let { VedicAstrologyUtils.isCombust(pos, it) } ?: false
        val hasDigBala = VedicAstrologyUtils.hasDigBala(pos)
        val isFuncBenefic = VedicAstrologyUtils.isFunctionalBenefic(planet, chart)
        val isFuncMalefic = VedicAstrologyUtils.isFunctionalMalefic(planet, chart)

        // Check aspects from other planets
        var beneficAspects = false
        var maleficAspects = false
        for (otherPos in chart.planetPositions) {
            if (otherPos.planet == planet) continue
            if (otherPos.planet !in Planet.MAIN_PLANETS) continue
            val aspectedHouses = VedicAstrologyUtils.getAspectedHouses(otherPos.planet, otherPos.house)
            if (pos.house in aspectedHouses) {
                if (VedicAstrologyUtils.isNaturalBenefic(otherPos.planet)) {
                    beneficAspects = true
                } else if (VedicAstrologyUtils.isNaturalMalefic(otherPos.planet)) {
                    maleficAspects = true
                }
            }
        }

        // Determine ruled houses
        val ruledHouses = (1..12).filter {
            VedicAstrologyUtils.getHouseSign(chart, it).ruler == planet
        }

        // Compute composite score
        val dignityScore = when (dignity) {
            VedicAstrologyUtils.PlanetaryDignity.EXALTED -> 1.0
            VedicAstrologyUtils.PlanetaryDignity.MOOLATRIKONA -> 0.90
            VedicAstrologyUtils.PlanetaryDignity.OWN_SIGN -> 0.80
            VedicAstrologyUtils.PlanetaryDignity.FRIEND_SIGN -> 0.65
            VedicAstrologyUtils.PlanetaryDignity.NEUTRAL_SIGN -> 0.50
            VedicAstrologyUtils.PlanetaryDignity.ENEMY_SIGN -> 0.30
            VedicAstrologyUtils.PlanetaryDignity.DEBILITATED -> 0.15
        }

        var score = dignityScore

        // House placement modifiers
        if (isInKendra) score += 0.10
        if (isInTrikona) score += 0.10
        if (isInDusthana) score -= 0.15

        // Aspect modifiers
        if (beneficAspects) score += 0.08
        if (maleficAspects) score -= 0.08

        // Combustion penalty (severe reduction)
        if (isCombust) score -= 0.20

        // Retrograde: in classical astrology, retrograde planets gain cheshta bala
        // but results can be delayed or reversed. Slight positive for strength.
        if (pos.isRetrograde && planet != Planet.RAHU && planet != Planet.KETU) {
            score += 0.03
        }

        // Directional strength bonus
        if (hasDigBala) score += 0.08

        // Functional status modifiers
        if (isFuncBenefic) score += 0.08
        if (isFuncMalefic) score -= 0.08

        score = score.coerceIn(0.05, 1.0)

        return PlanetaryStrength(
            planet = planet,
            dignity = dignity,
            house = pos.house,
            isRetrograde = pos.isRetrograde,
            isCombust = isCombust,
            isInKendra = isInKendra,
            isInTrikona = isInTrikona,
            isInDusthana = isInDusthana,
            isFunctionalBenefic = isFuncBenefic,
            isFunctionalMalefic = isFuncMalefic,
            hasDigBala = hasDigBala,
            receivesAspectFromBenefics = beneficAspects,
            receivesAspectFromMalefics = maleficAspects,
            ruledHouses = ruledHouses,
            compositeScore = score
        )
    }

    /**
     * Returns a default weak strength for planets not found in the chart.
     */
    private fun createDefaultStrength(planet: Planet): PlanetaryStrength {
        return PlanetaryStrength(
            planet = planet,
            dignity = VedicAstrologyUtils.PlanetaryDignity.NEUTRAL_SIGN,
            house = 1,
            isRetrograde = false,
            isCombust = false,
            isInKendra = false,
            isInTrikona = false,
            isInDusthana = false,
            isFunctionalBenefic = false,
            isFunctionalMalefic = false,
            hasDigBala = false,
            receivesAspectFromBenefics = false,
            receivesAspectFromMalefics = false,
            ruledHouses = emptyList(),
            compositeScore = 0.30
        )
    }

    // ========================================================================
    // PILLAR 2: TRANSIT STATE BUILDER
    // ========================================================================

    /**
     * Builds the [TransitState] by classifying each transit planet's house
     * position from the natal Moon sign according to classical Gochara rules.
     */
    private fun buildTransitState(
        natalChart: VedicChart,
        transitPositions: List<PlanetPosition>
    ): TransitState {
        val moonPos = VedicAstrologyUtils.getMoonPosition(natalChart)
            ?: throw IllegalStateException("Moon position not found in natal chart")
        val moonSign = moonPos.sign

        val transitMap = mutableMapOf<Planet, TransitInfo>()
        var favorableCount = 0
        var totalCount = 0

        for (tPos in transitPositions) {
            if (tPos.planet !in Planet.MAIN_PLANETS) continue

            val houseFromMoon = VedicAstrologyUtils.getHouseFromSigns(tPos.sign, moonSign)

            val classification = when {
                FAVORABLE_TRANSIT_HOUSES[tPos.planet]?.contains(houseFromMoon) == true ->
                    TransitClassification.FAVORABLE
                UNFAVORABLE_TRANSIT_HOUSES[tPos.planet]?.contains(houseFromMoon) == true ->
                    TransitClassification.UNFAVORABLE
                else -> TransitClassification.NEUTRAL
            }

            transitMap[tPos.planet] = TransitInfo(
                planet = tPos.planet,
                transitSign = tPos.sign,
                houseFromMoon = houseFromMoon,
                transitClassification = classification,
                isRetrograde = tPos.isRetrograde,
                speed = tPos.speed
            )

            if (classification == TransitClassification.FAVORABLE) favorableCount++
            totalCount++
        }

        val overallScore = if (totalCount > 0) favorableCount.toDouble() / totalCount else 0.5

        return TransitState(
            moonSign = moonSign,
            transitPositions = transitMap,
            overallTransitScore = overallScore
        )
    }

    // ========================================================================
    // PILLAR 3: ASHTAKAVARGA STATE BUILDER
    // ========================================================================

    /**
     * Builds the [AshtakavargaState] from pre-calculated Ashtakavarga analysis,
     * mapping SAV and BAV scores to house numbers (1-12) based on the chart's
     * ascendant sign.
     */
    private fun buildAshtakavargaState(
        chart: VedicChart,
        analysis: AshtakavargaCalculator.AshtakavargaAnalysis
    ): AshtakavargaState {
        val ascSign = VedicAstrologyUtils.getAscendantSign(chart)
        val savScores = mutableMapOf<Int, Int>()
        val bavScores = mutableMapOf<Planet, MutableMap<Int, Int>>()

        // Map sign-based SAV scores to house numbers
        for (house in 1..12) {
            val sign = getSignForHouse(ascSign, house)
            savScores[house] = analysis.sarvashtakavarga.getBindusForSign(sign)
        }

        // Map BAV scores for each planet to house numbers
        for ((planet, bav) in analysis.bhinnashtakavarga) {
            val planetBavMap = mutableMapOf<Int, Int>()
            for (house in 1..12) {
                val sign = getSignForHouse(ascSign, house)
                planetBavMap[house] = bav.getBindusForSign(sign)
            }
            bavScores[planet] = planetBavMap
        }

        val strongHouses = savScores.filter { it.value >= 28 }.keys.sorted()
        val weakHouses = savScores.filter { it.value < 22 }.keys.sorted()

        return AshtakavargaState(
            savScores = savScores,
            bavScores = bavScores,
            strongHouses = strongHouses,
            weakHouses = weakHouses,
            totalSAV = analysis.sarvashtakavarga.totalBindus
        )
    }

    /**
     * Gets the zodiac sign that falls in the given house number from the
     * ascendant sign.
     */
    private fun getSignForHouse(ascSign: ZodiacSign, house: Int): ZodiacSign {
        val signIndex = ((ascSign.number - 1 + house - 1) % 12)
        return ZodiacSign.entries[signIndex]
    }

    // ========================================================================
    // COMPOSITE PROBABILITY COMPUTATION
    // ========================================================================

    /**
     * Computes the composite probability score for each life area by evaluating
     * all three pillars against the relevant houses for that area.
     *
     * The scoring formula for each life area:
     *
     * For each relevant house (with house-level weight w_h):
     *   dashaFactor_h = evaluate dasha lords' relationship to this house
     *   transitFactor_h = evaluate transiting planets' impact on this house from Moon
     *   ashtakavargaFactor_h = SAV bindus for this house / SAV_MAX_NORMALIZATION
     *
     * compositeFactor_h = (dashaFactor_h * DASHA_WEIGHT) +
     *                     (transitFactor_h * TRANSIT_WEIGHT) +
     *                     (ashtakavargaFactor_h * ASHTAKAVARGA_WEIGHT)
     *
     * lifeProbability = SUM(w_h * compositeFactor_h) * 100, clamped to [0, 100]
     */
    private fun computeLifeAreaProbabilities(
        chart: VedicChart,
        dashaState: DashaState,
        transitState: TransitState,
        ashtakavargaState: AshtakavargaState
    ): Map<LifeArea, ProbabilityScore> {
        val results = mutableMapOf<LifeArea, ProbabilityScore>()

        for ((lifeArea, houses) in LIFE_AREA_HOUSES) {
            var weightedDasha = 0.0
            var weightedTransit = 0.0
            var weightedAshtakavarga = 0.0

            for ((idx, house) in houses.withIndex()) {
                val houseWeight = HOUSE_WEIGHTS.getOrElse(idx) { 0.10 }

                // --- Dasha Factor for this house ---
                val dashaFactor = computeDashaFactorForHouse(chart, dashaState, house)

                // --- Transit Factor for this house ---
                val transitFactor = computeTransitFactorForHouse(chart, transitState, house)

                // --- Ashtakavarga Factor for this house ---
                val savBindus = ashtakavargaState.savScores[house] ?: 25
                val ashtakavargaFactor = (savBindus.toDouble() / (SAV_MAX_NORMALIZATION * 12.0))
                    .coerceIn(0.0, 1.0)

                weightedDasha += houseWeight * dashaFactor
                weightedTransit += houseWeight * transitFactor
                weightedAshtakavarga += houseWeight * ashtakavargaFactor
            }

            // Apply multiplicative-additive hybrid:
            // Base = weighted sum, then apply a multiplicative dampener so that
            // a zero in any pillar significantly drops the final score.
            val compositeRaw = (weightedDasha * DASHA_WEIGHT) +
                    (weightedTransit * TRANSIT_WEIGHT) +
                    (weightedAshtakavarga * ASHTAKAVARGA_WEIGHT)

            // Multiplicative dampener: geometric mean of the three pillar factors
            // ensures that weakness in any pillar suppresses the overall score
            val geoMean = (max(weightedDasha, 0.01) *
                    max(weightedTransit, 0.01) *
                    max(weightedAshtakavarga, 0.01)).pow(1.0 / 3.0)

            // Final blend: 60% additive composite + 40% geometric mean
            val blendedScore = (compositeRaw * 0.60) + (geoMean * 0.40)
            val probability = (blendedScore * 100).toInt().coerceIn(0, 100)

            val assessment = when {
                probability >= 75 -> ProbabilityAssessment.EXCELLENT
                probability >= 60 -> ProbabilityAssessment.GOOD
                probability >= 40 -> ProbabilityAssessment.MODERATE
                probability >= 25 -> ProbabilityAssessment.CHALLENGING
                else -> ProbabilityAssessment.DIFFICULT
            }

            val interpretation = generateInterpretation(
                lifeArea, probability, assessment, dashaState,
                weightedDasha, weightedTransit, weightedAshtakavarga, houses
            )

            results[lifeArea] = ProbabilityScore(
                lifeArea = lifeArea,
                probability = probability,
                dashaFactor = weightedDasha,
                transitFactor = weightedTransit,
                ashtakavargaFactor = weightedAshtakavarga,
                assessment = assessment,
                interpretation = interpretation,
                relevantHouses = houses
            )
        }

        return results
    }

    /**
     * Evaluates the Dasha pillar's contribution to a specific house by checking:
     * - Whether the dasha lords rule the target house
     * - Whether the dasha lords occupy the target house
     * - Whether the dasha lords aspect the target house
     * - The natal strength of the dasha lords
     * - Natural friendship between dasha lords and the house lord
     */
    private fun computeDashaFactorForHouse(
        chart: VedicChart,
        dashaState: DashaState,
        house: Int
    ): Double {
        val houseLord = VedicAstrologyUtils.getHouseLord(chart, house)
        val dashaLords = listOfNotNull(
            dashaState.mahadashaLord to dashaState.mahadashaStrength,
            dashaState.antardashaLord to dashaState.antardashaStrength,
            dashaState.pratyantardashaLord?.let {
                it to (dashaState.pratyantardashaStrength ?: createDefaultStrength(it))
            }
        )

        // Weights for MD, AD, PAD in dasha factor contribution
        val dashaLevelWeights = listOf(0.50, 0.35, 0.15)
        var totalFactor = 0.0

        for ((idx, pair) in dashaLords.withIndex()) {
            val (lord, strength) = pair
            val weight = dashaLevelWeights.getOrElse(idx) { 0.10 }
            var lordFactor = strength.compositeScore

            // Bonus: dasha lord rules the target house
            if (house in strength.ruledHouses) {
                lordFactor += 0.20
            }

            // Bonus: dasha lord occupies the target house
            val lordPos = VedicAstrologyUtils.getPlanetPosition(chart, lord)
            if (lordPos?.house == house) {
                lordFactor += 0.15
            }

            // Bonus: dasha lord aspects the target house
            if (lordPos != null) {
                val aspectedHouses = VedicAstrologyUtils.getAspectedHouses(lord, lordPos.house)
                if (house in aspectedHouses) {
                    lordFactor += 0.10
                }
            }

            // Relationship between dasha lord and house lord
            val relationship = VedicAstrologyUtils.getNaturalRelationship(lord, houseLord)
            lordFactor += when (relationship) {
                VedicAstrologyUtils.PlanetaryRelationship.BEST_FRIEND -> 0.12
                VedicAstrologyUtils.PlanetaryRelationship.FRIEND -> 0.08
                VedicAstrologyUtils.PlanetaryRelationship.NEUTRAL -> 0.0
                VedicAstrologyUtils.PlanetaryRelationship.ENEMY -> -0.08
                VedicAstrologyUtils.PlanetaryRelationship.BITTER_ENEMY -> -0.15
            }

            totalFactor += weight * lordFactor.coerceIn(0.0, 1.0)
        }

        return totalFactor.coerceIn(0.0, 1.0)
    }

    /**
     * Evaluates the Transit pillar's contribution to a specific house.
     * Checks which planets are transiting the target house's sign from Moon
     * and whether those transits are classically favorable.
     *
     * Also considers planets aspecting the target house from their transit positions.
     */
    private fun computeTransitFactorForHouse(
        chart: VedicChart,
        transitState: TransitState,
        house: Int
    ): Double {
        val ascSign = VedicAstrologyUtils.getAscendantSign(chart)
        val houseSign = getSignForHouse(ascSign, house)

        // Compute transit house from Moon for the sign of this house
        val houseFromMoon = VedicAstrologyUtils.getHouseFromSigns(houseSign, transitState.moonSign)

        var factor = 0.50 // Neutral baseline

        // Evaluate each transiting planet
        for ((planet, info) in transitState.transitPositions) {
            // Direct occupation of the house's sign
            if (info.transitSign == houseSign) {
                when (info.transitClassification) {
                    TransitClassification.FAVORABLE -> factor += getPlanetTransitWeight(planet) * 0.15
                    TransitClassification.UNFAVORABLE -> factor -= getPlanetTransitWeight(planet) * 0.12
                    TransitClassification.NEUTRAL -> { /* no significant change */ }
                }
            }

            // Aspect to the house from transit position
            val transitHouseFromAsc = VedicAstrologyUtils.getHouseFromSigns(info.transitSign, ascSign)
            val aspectedHouses = VedicAstrologyUtils.getAspectedHouses(planet, transitHouseFromAsc)
            if (house in aspectedHouses) {
                when {
                    VedicAstrologyUtils.isNaturalBenefic(planet) -> factor += 0.06
                    VedicAstrologyUtils.isNaturalMalefic(planet) -> factor -= 0.05
                }
            }
        }

        // Benefic planet transiting favorable house from Moon for the target house
        val favorableFromMoon = FAVORABLE_TRANSIT_HOUSES.count { (planet, favorableHouses) ->
            val tInfo = transitState.transitPositions[planet] ?: return@count false
            tInfo.houseFromMoon in favorableHouses && tInfo.transitSign == houseSign
        }
        factor += favorableFromMoon * 0.08

        return factor.coerceIn(0.0, 1.0)
    }

    /**
     * Returns a weight reflecting how impactful a planet's transit is.
     * Slow-moving planets (Jupiter, Saturn, Rahu/Ketu) have greater impact;
     * fast-moving planets (Moon, Sun, Mercury, Venus) have lesser impact.
     */
    private fun getPlanetTransitWeight(planet: Planet): Double = when (planet) {
        Planet.SATURN -> 1.0
        Planet.JUPITER -> 0.95
        Planet.RAHU, Planet.KETU -> 0.85
        Planet.MARS -> 0.65
        Planet.SUN -> 0.50
        Planet.VENUS -> 0.50
        Planet.MERCURY -> 0.45
        Planet.MOON -> 0.30
        else -> 0.40
    }

    // ========================================================================
    // SANDHI (JUNCTION PERIOD) DETECTION
    // ========================================================================

    /**
     * Detects all Dasha Sandhi (junction) periods within the given date range
     * at Mahadasha, Antardasha, and Pratyantardasha levels.
     *
     * A Sandhi period spans before and after the exact transition point.
     * Duration scales with the dasha level: Mahadasha sandhis last months,
     * while Pratyantardasha sandhis last only days.
     */
    private fun detectSandhiPeriods(
        dashaTimeline: DashaCalculator.DashaTimeline,
        startDate: LocalDateTime,
        endDate: LocalDateTime
    ): List<SandhiPeriod> {
        val periods = mutableListOf<SandhiPeriod>()

        // Mahadasha Sandhis
        for (i in 0 until dashaTimeline.mahadashas.size - 1) {
            val current = dashaTimeline.mahadashas[i]
            val next = dashaTimeline.mahadashas[i + 1]
            val transitionDate = current.endDate

            if (transitionDate.isAfter(startDate) && transitionDate.isBefore(endDate)) {
                val sandhiStart = transitionDate.minusDays(MAHADASHA_SANDHI_DAYS / 2)
                val sandhiEnd = transitionDate.plusDays(MAHADASHA_SANDHI_DAYS / 2)

                val relationship = VedicAstrologyUtils.getNaturalRelationship(
                    current.planet, next.planet
                )
                val intensity = when (relationship) {
                    VedicAstrologyUtils.PlanetaryRelationship.BITTER_ENEMY -> 1.0
                    VedicAstrologyUtils.PlanetaryRelationship.ENEMY -> 0.85
                    VedicAstrologyUtils.PlanetaryRelationship.NEUTRAL -> 0.60
                    VedicAstrologyUtils.PlanetaryRelationship.FRIEND -> 0.40
                    VedicAstrologyUtils.PlanetaryRelationship.BEST_FRIEND -> 0.25
                }

                periods.add(
                    SandhiPeriod(
                        startDate = sandhiStart,
                        endDate = sandhiEnd,
                        outgoingLord = current.planet,
                        incomingLord = next.planet,
                        level = SandhiLevel.MAHADASHA,
                        intensity = intensity,
                        description = buildSandhiDescription(
                            current.planet, next.planet, SandhiLevel.MAHADASHA, intensity
                        )
                    )
                )
            }
        }

        // Antardasha Sandhis within Mahadashas that overlap the analysis window
        for (md in dashaTimeline.mahadashas) {
            if (md.endDate.isBefore(startDate) || md.startDate.isAfter(endDate)) continue
            for (i in 0 until md.antardashas.size - 1) {
                val currentAD = md.antardashas[i]
                val nextAD = md.antardashas[i + 1]
                val transitionDate = currentAD.endDate

                if (transitionDate.isAfter(startDate) && transitionDate.isBefore(endDate)) {
                    val sandhiStart = transitionDate.minusDays(ANTARDASHA_SANDHI_DAYS / 2)
                    val sandhiEnd = transitionDate.plusDays(ANTARDASHA_SANDHI_DAYS / 2)

                    val relationship = VedicAstrologyUtils.getNaturalRelationship(
                        currentAD.planet, nextAD.planet
                    )
                    val intensity = when (relationship) {
                        VedicAstrologyUtils.PlanetaryRelationship.BITTER_ENEMY -> 0.80
                        VedicAstrologyUtils.PlanetaryRelationship.ENEMY -> 0.65
                        VedicAstrologyUtils.PlanetaryRelationship.NEUTRAL -> 0.45
                        VedicAstrologyUtils.PlanetaryRelationship.FRIEND -> 0.30
                        VedicAstrologyUtils.PlanetaryRelationship.BEST_FRIEND -> 0.15
                    }

                    periods.add(
                        SandhiPeriod(
                            startDate = sandhiStart,
                            endDate = sandhiEnd,
                            outgoingLord = currentAD.planet,
                            incomingLord = nextAD.planet,
                            level = SandhiLevel.ANTARDASHA,
                            intensity = intensity,
                            description = buildSandhiDescription(
                                currentAD.planet, nextAD.planet, SandhiLevel.ANTARDASHA, intensity
                            )
                        )
                    )
                }
            }
        }

        return periods.sortedBy { it.startDate }
    }

    /**
     * Generates a descriptive string for a Sandhi period.
     */
    private fun buildSandhiDescription(
        outgoing: Planet,
        incoming: Planet,
        level: SandhiLevel,
        intensity: Double
    ): String {
        val intensityLabel = when {
            intensity >= 0.80 -> "highly volatile"
            intensity >= 0.60 -> "significantly unsettled"
            intensity >= 0.40 -> "moderately transitional"
            else -> "mildly transitional"
        }
        return "${level.label}: Transition from ${outgoing.displayName} to ${incoming.displayName} period. " +
                "This is a $intensityLabel phase where results from the outgoing period wind down " +
                "and the incoming period's themes begin to manifest. Predictions during this " +
                "junction carry inherent uncertainty."
    }

    // ========================================================================
    // TIMELINE GENERATION
    // ========================================================================

    /**
     * Generates a forecast timeline with data points at regular intervals.
     * Each point contains life area scores computed from the dasha lord active
     * at that date, a simplified transit estimate, and the Ashtakavarga baseline.
     *
     * For timeline generation, transit positions are estimated as static
     * (based on the natal chart's Ashtakavarga and dasha data), since real-time
     * ephemeris data for future dates requires the Swiss Ephemeris engine.
     */
    private fun generateTimeline(
        natalChart: VedicChart,
        dashaTimeline: DashaCalculator.DashaTimeline,
        ashtakavargaAnalysis: AshtakavargaCalculator.AshtakavargaAnalysis,
        startDate: LocalDateTime,
        endDate: LocalDateTime,
        stepDays: Long,
        sandhiPeriods: List<SandhiPeriod>
    ): List<TimelinePoint> {
        val points = mutableListOf<TimelinePoint>()
        var currentDate = startDate

        while (!currentDate.isAfter(endDate)) {
            val activeMD = dashaTimeline.mahadashas.find { it.isActiveOn(currentDate) }
            val activeAD = activeMD?.getAntardashaOn(currentDate)
            val dominantLord = activeAD?.planet ?: activeMD?.planet ?: Planet.KETU

            val isSandhi = sandhiPeriods.any { sp ->
                !currentDate.isBefore(sp.startDate) && !currentDate.isAfter(sp.endDate)
            }

            // Compute simplified scores per life area using dasha + ashtakavarga
            val lifeScores = mutableMapOf<LifeArea, Int>()
            val dashaStrength = assessPlanetaryStrength(natalChart, dominantLord)
            val ascSign = VedicAstrologyUtils.getAscendantSign(natalChart)

            for ((area, houses) in LIFE_AREA_HOUSES) {
                var areaScore = 0.0
                for ((idx, house) in houses.withIndex()) {
                    val houseWeight = HOUSE_WEIGHTS.getOrElse(idx) { 0.10 }
                    val sign = getSignForHouse(ascSign, house)
                    val savBindus = ashtakavargaAnalysis.sarvashtakavarga.getBindusForSign(sign)
                    val ashtFactor = (savBindus.toDouble() / (SAV_MAX_NORMALIZATION * 12.0))
                        .coerceIn(0.0, 1.0)

                    // Dasha factor for this house
                    var dashaFactor = dashaStrength.compositeScore
                    if (house in dashaStrength.ruledHouses) dashaFactor += 0.15
                    dashaFactor = dashaFactor.coerceIn(0.0, 1.0)

                    // Blended score for this house (no transit data for future dates)
                    val houseScore = (dashaFactor * 0.55) + (ashtFactor * 0.45)
                    areaScore += houseWeight * houseScore
                }

                var finalScore = (areaScore * 100).toInt()

                // Sandhi dampening: reduce confidence during junction periods
                if (isSandhi) {
                    val maxSandhiIntensity = sandhiPeriods
                        .filter { !currentDate.isBefore(it.startDate) && !currentDate.isAfter(it.endDate) }
                        .maxOfOrNull { it.intensity } ?: 0.0
                    finalScore = (finalScore * (1.0 - maxSandhiIntensity * 0.30)).toInt()
                }

                lifeScores[area] = finalScore.coerceIn(0, 100)
            }

            val overallScore = if (lifeScores.isNotEmpty()) {
                lifeScores.values.average().toInt()
            } else 50

            points.add(
                TimelinePoint(
                    date = currentDate,
                    lifeAreaScores = lifeScores,
                    dominantDashaLord = dominantLord,
                    isSandhiPeriod = isSandhi,
                    overallScore = overallScore
                )
            )

            currentDate = currentDate.plusDays(stepDays)
        }

        return points
    }

    // ========================================================================
    // OVERALL FORECAST SYNTHESIS
    // ========================================================================

    /**
     * Produces a synthesized [OverallForecast] from the computed life area
     * probabilities and detected Sandhi periods.
     */
    private fun synthesizeOverallForecast(
        probabilities: Map<LifeArea, ProbabilityScore>,
        sandhiPeriods: List<SandhiPeriod>
    ): OverallForecast {
        if (probabilities.isEmpty()) {
            return OverallForecast(
                overallScore = 50,
                assessment = ProbabilityAssessment.MODERATE,
                strongestArea = LifeArea.GENERAL,
                weakestArea = LifeArea.GENERAL,
                summary = "Insufficient data for detailed forecast.",
                recommendations = listOf("Consider providing complete chart data for accurate analysis.")
            )
        }

        val avgScore = probabilities.values.map { it.probability }.average().toInt()
        val strongest = probabilities.maxByOrNull { it.value.probability }!!
        val weakest = probabilities.minByOrNull { it.value.probability }!!

        val assessment = when {
            avgScore >= 75 -> ProbabilityAssessment.EXCELLENT
            avgScore >= 60 -> ProbabilityAssessment.GOOD
            avgScore >= 40 -> ProbabilityAssessment.MODERATE
            avgScore >= 25 -> ProbabilityAssessment.CHALLENGING
            else -> ProbabilityAssessment.DIFFICULT
        }

        val hasMajorSandhi = sandhiPeriods.any { it.level == SandhiLevel.MAHADASHA }

        val summary = buildString {
            append("Overall forecast indicates ${assessment.label.lowercase()} conditions ")
            append("with a composite score of $avgScore/100. ")
            append("${strongest.key.name.lowercase().replaceFirstChar { it.uppercase() }} ")
            append("shows the strongest potential at ${strongest.value.probability}%, ")
            append("while ${weakest.key.name.lowercase().replaceFirstChar { it.uppercase() }} ")
            append("requires the most attention at ${weakest.value.probability}%. ")
            if (hasMajorSandhi) {
                append("A Mahadasha transition is approaching, indicating a period of significant ")
                append("life shifts where predictions carry greater uncertainty.")
            }
        }

        val recommendations = buildRecommendations(probabilities, sandhiPeriods)

        return OverallForecast(
            overallScore = avgScore,
            assessment = assessment,
            strongestArea = strongest.key,
            weakestArea = weakest.key,
            summary = summary,
            recommendations = recommendations
        )
    }

    /**
     * Generates actionable recommendations based on the analysis results.
     */
    private fun buildRecommendations(
        probabilities: Map<LifeArea, ProbabilityScore>,
        sandhiPeriods: List<SandhiPeriod>
    ): List<String> {
        val recs = mutableListOf<String>()

        // Recommend leveraging strong areas
        probabilities.filter { it.value.probability >= 65 }.forEach { (area, score) ->
            recs.add(
                "Capitalize on favorable ${area.name.lowercase().replaceFirstChar { it.uppercase() }} " +
                        "conditions (${score.probability}%) for important initiatives in this domain."
            )
        }

        // Warn about weak areas
        probabilities.filter { it.value.probability < 35 }.forEach { (area, score) ->
            recs.add(
                "Exercise caution in ${area.name.lowercase().replaceFirstChar { it.uppercase() }} " +
                        "matters (${score.probability}%). Delay major decisions if possible or " +
                        "seek remedial measures."
            )
        }

        // Sandhi warnings
        val majorSandhi = sandhiPeriods.filter { it.level == SandhiLevel.MAHADASHA }
        if (majorSandhi.isNotEmpty()) {
            recs.add(
                "Mahadasha transition detected. This is a period of fundamental karmic shift. " +
                        "Avoid binding commitments and focus on spiritual practices during the junction."
            )
        }

        if (recs.isEmpty()) {
            recs.add("Current planetary conditions are moderate. Maintain steady progress " +
                    "and review charts periodically for emerging trends.")
        }

        return recs
    }

    // ========================================================================
    // INTERPRETATION GENERATION
    // ========================================================================

    /**
     * Generates a human-readable interpretation for a life area probability score.
     */
    private fun generateInterpretation(
        lifeArea: LifeArea,
        probability: Int,
        assessment: ProbabilityAssessment,
        dashaState: DashaState,
        dashaFactor: Double,
        transitFactor: Double,
        ashtakavargaFactor: Double,
        houses: List<Int>
    ): String {
        val areaName = lifeArea.name.lowercase().replaceFirstChar { it.uppercase() }
        val mdLord = dashaState.mahadashaLord.displayName
        val adLord = dashaState.antardashaLord.displayName
        val primaryHouse = houses.firstOrNull() ?: 1
        val ordinal = VedicAstrologyUtils.getOrdinalSuffix(primaryHouse)

        val pillarStrengths = mutableListOf<String>()
        if (dashaFactor >= 0.60) pillarStrengths.add("strong dasha support from $mdLord-$adLord period")
        else if (dashaFactor <= 0.35) pillarStrengths.add("limited dasha support from $mdLord-$adLord period")

        if (transitFactor >= 0.60) pillarStrengths.add("favorable transit alignments")
        else if (transitFactor <= 0.35) pillarStrengths.add("challenging transit configurations")

        if (ashtakavargaFactor >= 0.60) pillarStrengths.add("high Ashtakavarga strength in the ${primaryHouse}${ordinal} house")
        else if (ashtakavargaFactor <= 0.35) pillarStrengths.add("low Ashtakavarga score in the ${primaryHouse}${ordinal} house")

        return buildString {
            append("$areaName forecast: ${assessment.label} ($probability%). ")
            append("Analysis of the ${primaryHouse}${ordinal} house (primary significator) ")
            if (pillarStrengths.isNotEmpty()) {
                append("reveals ")
                append(pillarStrengths.joinToString(", "))
                append(". ")
            }
            when (assessment) {
                ProbabilityAssessment.EXCELLENT ->
                    append("All three pillars align favorably, indicating a peak period for ${areaName.lowercase()} matters.")
                ProbabilityAssessment.GOOD ->
                    append("Two or more pillars support positive outcomes in ${areaName.lowercase()} matters.")
                ProbabilityAssessment.MODERATE ->
                    append("Mixed signals across the three pillars suggest moderate and varied results.")
                ProbabilityAssessment.CHALLENGING ->
                    append("Multiple pillars indicate headwinds; patience and remedial action are advised.")
                ProbabilityAssessment.DIFFICULT ->
                    append("All three pillars indicate a difficult phase; defer major decisions and focus on remedial measures.")
            }
        }
    }
}
