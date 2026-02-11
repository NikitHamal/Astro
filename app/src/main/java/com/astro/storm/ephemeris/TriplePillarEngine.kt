package com.astro.storm.ephemeris

import com.astro.storm.core.common.Language
import com.astro.storm.core.common.StringKeyTriplePillar
import com.astro.storm.core.common.StringResources
import com.astro.storm.core.model.Planet
import com.astro.storm.core.model.PlanetPosition
import com.astro.storm.core.model.VedicChart
import com.astro.storm.core.model.ZodiacSign
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.temporal.ChronoUnit
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min
import kotlin.math.roundToInt

/**
 * Triple-Pillar Predictive Engine
 *
 * Implements the classical "Three Pillars of Vedic Timing" (Tri-Skandha Jyotish):
 *
 * 1. **Vimshottari Dasha** — The planetary period system based on Moon's Nakshatra.
 *    The Dasha Lord's inherent benefic/malefic nature, dignity (exaltation, own sign,
 *    debilitation), house lordship, and functional nature for the Lagna all contribute
 *    a Dasha Score (0.0–1.0).
 *
 * 2. **Gochara (Transit from Moon)** — Classical transit evaluation from Chandra Lagna.
 *    Each planet has favorable/unfavorable house transits per Phaladeepika.
 *    Vedha (obstruction) points can nullify favorable transits.
 *    Produces a Gochara Score (0.0–1.0).
 *
 * 3. **Ashtakavarga** — The BAV (Bhinnashtakavarga) bindu score of the Dasha Lord
 *    in its current transit sign, plus the SAV (Sarvashtakavarga) total for that sign.
 *    High bindu (5+) amplifies good transits; Low bindu (0-2) weakens them.
 *    Produces an Ashtakavarga Score (0.0–1.0).
 *
 * The three scores are combined using a weighted harmonic mean:
 *   - Dasha: 40% weight (most important — sets the overall "flavor" of the period)
 *   - Gochara: 35% weight (external trigger — timing of events)
 *   - Ashtakavarga: 25% weight (quantitative refinement — intensity modifier)
 *
 * Classical References:
 * - Brihat Parasara Hora Shastra (BPHS) — Dasha Phala, Ashtakavarga chapters (66-72)
 * - Phaladeepika by Mantreswara — Gochara Phala, Vedha system
 * - Uttara Kalamrita — Synthesis of Dasha-Gochara-Ashtakavarga
 *
 * @author AstroStorm - Ultra-Precision Vedic Astrology
 */
object TriplePillarEngine {

    // ============================================================================
    // WEIGHTS for synthesis (based on classical prioritization)
    // ============================================================================
    private const val DASHA_WEIGHT = 0.40
    private const val GOCHARA_WEIGHT = 0.35
    private const val ASHTAKAVARGA_WEIGHT = 0.25

    // ============================================================================
    // GOCHARA FAVORABLE HOUSES (Transit from Moon)
    // Based on Phaladeepika Chapter 26
    // ============================================================================
    private val GOCHARA_FAVORABLE_HOUSES = mapOf(
        Planet.SUN to setOf(3, 6, 10, 11),
        Planet.MOON to setOf(1, 3, 6, 7, 10, 11),
        Planet.MARS to setOf(3, 6, 11),
        Planet.MERCURY to setOf(2, 4, 6, 8, 10, 11),
        Planet.JUPITER to setOf(2, 5, 7, 9, 11),
        Planet.VENUS to setOf(1, 2, 3, 4, 5, 8, 9, 11, 12),
        Planet.SATURN to setOf(3, 6, 11),
        Planet.RAHU to setOf(3, 6, 10, 11),
        Planet.KETU to setOf(3, 6, 11)
    )

    // ============================================================================
    // VEDHA (Obstruction) POINTS per Phaladeepika
    // Format: Map<Planet, Map<FavorableHouse, VedhaHouse>>
    // ============================================================================
    private val VEDHA_POINTS = mapOf(
        Planet.SUN to mapOf(3 to 9, 6 to 12, 10 to 4, 11 to 5),
        Planet.MOON to mapOf(1 to 5, 3 to 9, 6 to 12, 7 to 2, 10 to 4, 11 to 8),
        Planet.MARS to mapOf(3 to 12, 6 to 9, 11 to 5),
        Planet.MERCURY to mapOf(2 to 5, 4 to 3, 6 to 9, 8 to 1, 10 to 8, 11 to 12),
        Planet.JUPITER to mapOf(2 to 12, 5 to 4, 7 to 3, 9 to 10, 11 to 8),
        Planet.VENUS to mapOf(1 to 8, 2 to 7, 3 to 1, 4 to 10, 5 to 9, 8 to 5, 9 to 11, 11 to 6, 12 to 3),
        Planet.SATURN to mapOf(3 to 12, 6 to 9, 11 to 5)
    )

    // ============================================================================
    // PLANETARY NATURE (Natural Benefic/Malefic classification)
    // ============================================================================
    private val NATURAL_BENEFICS = setOf(Planet.JUPITER, Planet.VENUS, Planet.MERCURY, Planet.MOON)
    private val NATURAL_MALEFICS = setOf(Planet.SUN, Planet.MARS, Planet.SATURN, Planet.RAHU, Planet.KETU)

    // ============================================================================
    // EXALTATION SIGNS per BPHS
    // ============================================================================
    private val EXALTATION_SIGNS = mapOf(
        Planet.SUN to ZodiacSign.ARIES,
        Planet.MOON to ZodiacSign.TAURUS,
        Planet.MARS to ZodiacSign.CAPRICORN,
        Planet.MERCURY to ZodiacSign.VIRGO,
        Planet.JUPITER to ZodiacSign.CANCER,
        Planet.VENUS to ZodiacSign.PISCES,
        Planet.SATURN to ZodiacSign.LIBRA,
        Planet.RAHU to ZodiacSign.TAURUS,   // Per most traditions
        Planet.KETU to ZodiacSign.SCORPIO
    )

    // ============================================================================
    // DEBILITATION SIGNS per BPHS (180° opposite of exaltation)
    // ============================================================================
    private val DEBILITATION_SIGNS = mapOf(
        Planet.SUN to ZodiacSign.LIBRA,
        Planet.MOON to ZodiacSign.SCORPIO,
        Planet.MARS to ZodiacSign.CANCER,
        Planet.MERCURY to ZodiacSign.PISCES,
        Planet.JUPITER to ZodiacSign.CAPRICORN,
        Planet.VENUS to ZodiacSign.VIRGO,
        Planet.SATURN to ZodiacSign.ARIES,
        Planet.RAHU to ZodiacSign.SCORPIO,
        Planet.KETU to ZodiacSign.TAURUS
    )

    // ============================================================================
    // OWN SIGNS per BPHS
    // ============================================================================
    private val OWN_SIGNS = mapOf(
        Planet.SUN to setOf(ZodiacSign.LEO),
        Planet.MOON to setOf(ZodiacSign.CANCER),
        Planet.MARS to setOf(ZodiacSign.ARIES, ZodiacSign.SCORPIO),
        Planet.MERCURY to setOf(ZodiacSign.GEMINI, ZodiacSign.VIRGO),
        Planet.JUPITER to setOf(ZodiacSign.SAGITTARIUS, ZodiacSign.PISCES),
        Planet.VENUS to setOf(ZodiacSign.TAURUS, ZodiacSign.LIBRA),
        Planet.SATURN to setOf(ZodiacSign.CAPRICORN, ZodiacSign.AQUARIUS),
        Planet.RAHU to setOf(ZodiacSign.AQUARIUS),
        Planet.KETU to setOf(ZodiacSign.SCORPIO)
    )

    // ============================================================================
    // MOOLTRIKONA SIGNS per BPHS
    // ============================================================================
    private val MOOLTRIKONA_SIGNS = mapOf(
        Planet.SUN to ZodiacSign.LEO,        // 0-20° Leo
        Planet.MOON to ZodiacSign.TAURUS,    // 4-20° Taurus
        Planet.MARS to ZodiacSign.ARIES,     // 0-12° Aries
        Planet.MERCURY to ZodiacSign.VIRGO,  // 16-20° Virgo
        Planet.JUPITER to ZodiacSign.SAGITTARIUS, // 0-10° Sagittarius
        Planet.VENUS to ZodiacSign.LIBRA,    // 0-15° Libra
        Planet.SATURN to ZodiacSign.AQUARIUS // 0-20° Aquarius
    )

    // ============================================================================
    // NATURAL FRIENDSHIP between planets per BPHS
    // ============================================================================
    private val NATURAL_FRIENDS = mapOf(
        Planet.SUN to setOf(Planet.MOON, Planet.MARS, Planet.JUPITER),
        Planet.MOON to setOf(Planet.SUN, Planet.MERCURY),
        Planet.MARS to setOf(Planet.SUN, Planet.MOON, Planet.JUPITER),
        Planet.MERCURY to setOf(Planet.SUN, Planet.VENUS),
        Planet.JUPITER to setOf(Planet.SUN, Planet.MOON, Planet.MARS),
        Planet.VENUS to setOf(Planet.MERCURY, Planet.SATURN),
        Planet.SATURN to setOf(Planet.MERCURY, Planet.VENUS),
        Planet.RAHU to setOf(Planet.MERCURY, Planet.VENUS, Planet.SATURN),
        Planet.KETU to setOf(Planet.MARS, Planet.VENUS, Planet.SATURN)
    )

    private val NATURAL_ENEMIES = mapOf(
        Planet.SUN to setOf(Planet.VENUS, Planet.SATURN),
        Planet.MOON to setOf<Planet>(),
        Planet.MARS to setOf(Planet.MERCURY),
        Planet.MERCURY to setOf(Planet.MOON),
        Planet.JUPITER to setOf(Planet.MERCURY, Planet.VENUS),
        Planet.VENUS to setOf(Planet.SUN, Planet.MOON),
        Planet.SATURN to setOf(Planet.SUN, Planet.MOON, Planet.MARS),
        Planet.RAHU to setOf(Planet.SUN, Planet.MOON, Planet.MARS),
        Planet.KETU to setOf(Planet.SUN, Planet.MOON)
    )

    // ============================================================================
    // FUNCTIONAL BENEFIC/MALEFIC LORDS per Ascendant (BPHS)
    // Returns the house lords that are benefic for each Lagna
    // ============================================================================
    private val FUNCTIONAL_BENEFIC_LORDS_BY_LAGNA = mapOf(
        ZodiacSign.ARIES to setOf(1, 4, 5, 9),      // Mars, Moon, Sun, Jupiter
        ZodiacSign.TAURUS to setOf(1, 2, 5, 9, 10),  // Venus, Mercury, Saturn
        ZodiacSign.GEMINI to setOf(1, 4, 5, 11),     // Mercury, Venus
        ZodiacSign.CANCER to setOf(1, 4, 5, 9, 10),  // Moon, Mars, Jupiter
        ZodiacSign.LEO to setOf(1, 4, 5, 9),         // Sun, Mars, Jupiter
        ZodiacSign.VIRGO to setOf(1, 2, 5, 9, 10),   // Mercury, Venus, Saturn
        ZodiacSign.LIBRA to setOf(1, 4, 5, 9),       // Venus, Saturn, Mercury
        ZodiacSign.SCORPIO to setOf(1, 4, 5, 9, 10), // Mars, Jupiter, Moon, Sun
        ZodiacSign.SAGITTARIUS to setOf(1, 4, 5, 9), // Jupiter, Mars, Sun
        ZodiacSign.CAPRICORN to setOf(1, 4, 5, 9, 10), // Saturn, Venus, Mercury
        ZodiacSign.AQUARIUS to setOf(1, 4, 5, 9),    // Saturn, Venus, Mercury
        ZodiacSign.PISCES to setOf(1, 4, 5, 9, 10)   // Jupiter, Moon, Mars
    )

    // ============================================================================
    // DATA CLASSES
    // ============================================================================

    /**
     * Complete synthesis result for the Triple-Pillar analysis
     */
    data class TriplePillarResult(
        val chart: VedicChart,
        val analysisDate: LocalDate,
        val currentSynthesis: PillarSynthesis,
        val monthlyForecasts: List<MonthlyForecast>,
        val peakWindows: List<OpportunityWindow>,
        val cautionPeriods: List<OpportunityWindow>,
        val lifeAreaImpacts: List<LifeAreaImpact>,
        val classicalReference: String,
        val classicalReferenceNe: String
    )

    /**
     * The core synthesis of all three pillars at a specific point in time
     */
    data class PillarSynthesis(
        val date: LocalDate,
        val dashaScore: Double,
        val gocharaScore: Double,
        val ashtakavargaScore: Double,
        val compositeScore: Double,
        val successProbability: Int,
        val qualityLevel: QualityLevel,
        val dashaAnalysis: DashaAnalysisDetail,
        val gocharaAnalysis: GocharaAnalysisDetail,
        val ashtakavargaAnalysis: AshtakavargaAnalysisDetail,
        val interpretation: String,
        val interpretationNe: String,
        val recommendation: String,
        val recommendationNe: String
    )

    /**
     * Detailed Dasha pillar analysis
     */
    data class DashaAnalysisDetail(
        val mahadashaLord: Planet,
        val antardashaLord: Planet?,
        val pratyantardashaLord: Planet?,
        val mahadashaDignity: PlanetaryDignity,
        val antardashaDignity: PlanetaryDignity?,
        val functionalNature: FunctionalNature,
        val dashaLordHouses: List<Int>,
        val isSandhiPeriod: Boolean,
        val rawScore: Double
    )

    /**
     * Detailed Gochara pillar analysis
     */
    data class GocharaAnalysisDetail(
        val planetTransits: List<PlanetGocharaResult>,
        val favorableCount: Int,
        val unfavorableCount: Int,
        val vedhaActiveCount: Int,
        val rawScore: Double
    )

    /**
     * Individual planet transit result for Gochara
     */
    data class PlanetGocharaResult(
        val planet: Planet,
        val transitHouseFromMoon: Int,
        val transitSign: ZodiacSign,
        val isFavorable: Boolean,
        val hasVedha: Boolean,
        val vedhaByPlanet: Planet?,
        val effectiveResult: GocharaEffectiveness
    )

    enum class GocharaEffectiveness {
        FAVORABLE_CLEAR,        // Favorable house, no Vedha
        FAVORABLE_VEDHA,        // Favorable house, but Vedha active (nullified)
        UNFAVORABLE,            // Unfavorable house
        NEUTRAL                 // Neutral (not strongly positive or negative)
    }

    /**
     * Detailed Ashtakavarga pillar analysis
     */
    data class AshtakavargaAnalysisDetail(
        val dashaLordBav: Int,
        val dashaLordTransitSign: ZodiacSign,
        val savInTransitSign: Int,
        val bavStrength: BinduStrength,
        val savAboveThreshold: Boolean,
        val rawScore: Double
    )

    enum class BinduStrength { HIGH, MEDIUM, LOW }

    /**
     * Monthly forecast entry for the timeline
     */
    data class MonthlyForecast(
        val month: LocalDate,
        val monthName: String,
        val monthNameNe: String,
        val compositeScore: Double,
        val successProbability: Int,
        val qualityLevel: QualityLevel,
        val dashaScore: Double,
        val gocharaScore: Double,
        val ashtakavargaScore: Double,
        val dominantLifeAreas: List<LifeAreaType>,
        val briefInterpretation: String,
        val briefInterpretationNe: String
    )

    /**
     * Peak or caution window
     */
    data class OpportunityWindow(
        val startDate: LocalDate,
        val endDate: LocalDate,
        val score: Double,
        val qualityLevel: QualityLevel,
        val description: String,
        val descriptionNe: String,
        val lifeAreas: List<LifeAreaType>
    )

    /**
     * Life area impact assessment
     */
    data class LifeAreaImpact(
        val area: LifeAreaType,
        val impactScore: Double,
        val qualityLevel: QualityLevel,
        val description: String,
        val descriptionNe: String
    )

    /**
     * Life area types
     */
    enum class LifeAreaType(val en: String, val ne: String) {
        CAREER("Career & Profession", "करियर र पेशा"),
        FINANCE("Finance & Wealth", "वित्त र सम्पत्ति"),
        RELATIONSHIPS("Relationships & Marriage", "सम्बन्ध र विवाह"),
        HEALTH("Health & Vitality", "स्वास्थ्य र शक्ति"),
        EDUCATION("Education & Knowledge", "शिक्षा र ज्ञान"),
        SPIRITUALITY("Spirituality & Growth", "आध्यात्मिकता र विकास"),
        FAMILY("Family & Home", "परिवार र गृह"),
        TRAVEL("Travel & Movement", "यात्रा र गतिविधि"),
        LEGAL("Legal & Government", "कानूनी र सरकारी"),
        PROPERTY("Property & Assets", "सम्पत्ति र सम्पदा")
    }

    /**
     * Quality levels for synthesis scores
     */
    enum class QualityLevel(val score: Int, val en: String, val ne: String) {
        EXCEPTIONAL(95, "Exceptional", "असाधारण"),
        EXCELLENT(85, "Excellent", "उत्कृष्ट"),
        VERY_GOOD(75, "Very Good", "धेरै राम्रो"),
        GOOD(65, "Good", "राम्रो"),
        ABOVE_AVERAGE(55, "Above Average", "औसतभन्दा माथि"),
        AVERAGE(45, "Average", "औसत"),
        BELOW_AVERAGE(35, "Below Average", "औसतभन्दा तल"),
        CHALLENGING(20, "Challenging", "चुनौतीपूर्ण"),
        DIFFICULT(0, "Difficult", "कठिन")
    }

    /**
     * Planetary dignity
     */
    enum class PlanetaryDignity(val multiplier: Double, val en: String, val ne: String) {
        EXALTED(1.0, "Exalted", "उच्च"),
        MOOLTRIKONA(0.9, "Mooltrikona", "मूलत्रिकोण"),
        OWN_SIGN(0.85, "Own Sign", "स्वराशि"),
        FRIEND_SIGN(0.7, "Friendly Sign", "मित्र राशि"),
        NEUTRAL_SIGN(0.5, "Neutral Sign", "सम राशि"),
        ENEMY_SIGN(0.3, "Enemy Sign", "शत्रु राशि"),
        DEBILITATED(0.15, "Debilitated", "नीच")
    }

    /**
     * Functional nature of a planet for a given Lagna
     */
    enum class FunctionalNature(val multiplier: Double, val en: String, val ne: String) {
        YOGAKARAKA(1.0, "Yogakaraka", "योगकारक"),
        BENEFIC(0.8, "Functional Benefic", "कार्यात्मक शुभ"),
        NEUTRAL(0.5, "Functional Neutral", "कार्यात्मक तटस्थ"),
        MALEFIC(0.25, "Functional Malefic", "कार्यात्मक अशुभ"),
        MARAKA(0.15, "Maraka", "मारक")
    }

    // ============================================================================
    // MAIN CALCULATION ENTRY POINT
    // ============================================================================

    /**
     * Perform complete Triple-Pillar synthesis analysis
     *
     * @param chart The natal Vedic chart
     * @param language Language for interpretations
     * @param forecastMonths Number of months to forecast ahead (default 12)
     * @return Complete TriplePillarResult or null if calculation fails
     */
    fun calculateSynthesis(
        chart: VedicChart,
        language: Language = Language.ENGLISH,
        forecastMonths: Int = 12
    ): TriplePillarResult? {
        // Validate: need Moon position for Gochara
        val moonPosition = chart.planetPositions.find { it.planet == Planet.MOON } ?: return null

        // Calculate Dasha timeline
        val dashaTimeline = try {
            DashaCalculator.calculateDashaTimeline(chart)
        } catch (e: Exception) {
            return null
        }

        // Calculate Ashtakavarga
        val ashtakavargaAnalysis = try {
            AshtakavargaCalculator.calculateAshtakavarga(chart)
        } catch (e: Exception) {
            null
        }

        val now = LocalDate.now()

        // Current synthesis
        val currentSynthesis = calculatePillarSynthesis(
            chart = chart,
            moonPosition = moonPosition,
            dashaTimeline = dashaTimeline,
            ashtakavargaAnalysis = ashtakavargaAnalysis,
            analysisDate = now,
            language = language
        )

        // Monthly forecasts
        val monthlyForecasts = (0 until forecastMonths).map { monthOffset ->
            val forecastDate = now.plusMonths(monthOffset.toLong())
            val monthSynthesis = calculatePillarSynthesis(
                chart = chart,
                moonPosition = moonPosition,
                dashaTimeline = dashaTimeline,
                ashtakavargaAnalysis = ashtakavargaAnalysis,
                analysisDate = forecastDate,
                language = language
            )
            val monthNames = getMonthNames(forecastDate.monthValue)
            MonthlyForecast(
                month = forecastDate,
                monthName = monthNames.first,
                monthNameNe = monthNames.second,
                compositeScore = monthSynthesis.compositeScore,
                successProbability = monthSynthesis.successProbability,
                qualityLevel = monthSynthesis.qualityLevel,
                dashaScore = monthSynthesis.dashaScore,
                gocharaScore = monthSynthesis.gocharaScore,
                ashtakavargaScore = monthSynthesis.ashtakavargaScore,
                dominantLifeAreas = getDominantLifeAreas(monthSynthesis),
                briefInterpretation = monthSynthesis.interpretation,
                briefInterpretationNe = monthSynthesis.interpretationNe
            )
        }

        // Detect peak windows (consecutive months with score >= 0.7)
        val peakWindows = detectWindows(monthlyForecasts, threshold = 0.65, isPeak = true)
        val cautionPeriods = detectWindows(monthlyForecasts, threshold = 0.4, isPeak = false)

        // Life area impacts
        val lifeAreaImpacts = calculateLifeAreaImpacts(currentSynthesis, chart, language)

        return TriplePillarResult(
            chart = chart,
            analysisDate = now,
            currentSynthesis = currentSynthesis,
            monthlyForecasts = monthlyForecasts,
            peakWindows = peakWindows,
            cautionPeriods = cautionPeriods,
            lifeAreaImpacts = lifeAreaImpacts,
            classicalReference = StringResources.get(StringKeyTriplePillar.REF_THREE_PILLARS, Language.ENGLISH),
            classicalReferenceNe = StringResources.get(StringKeyTriplePillar.REF_THREE_PILLARS, Language.NEPALI)
        )
    }

    // ============================================================================
    // PILLAR SYNTHESIS CALCULATION
    // ============================================================================

    private fun calculatePillarSynthesis(
        chart: VedicChart,
        moonPosition: PlanetPosition,
        dashaTimeline: DashaCalculator.DashaTimeline,
        ashtakavargaAnalysis: AshtakavargaCalculator.AshtakavargaAnalysis?,
        analysisDate: LocalDate,
        language: Language
    ): PillarSynthesis {
        val analysisDateTime = analysisDate.atStartOfDay()
        val lagnaSign = ZodiacSign.fromLongitude(chart.ascendant)

        // ===== PILLAR 1: DASHA =====
        val dashaDetail = calculateDashaPillar(chart, dashaTimeline, analysisDateTime, lagnaSign)

        // ===== PILLAR 2: GOCHARA =====
        val gocharaDetail = calculateGocharaPillar(chart, moonPosition)

        // ===== PILLAR 3: ASHTAKAVARGA =====
        val ashtakavargaDetail = calculateAshtakavargaPillar(
            chart, dashaTimeline, ashtakavargaAnalysis, analysisDateTime
        )

        // ===== WEIGHTED SYNTHESIS =====
        val compositeScore = calculateWeightedScore(
            dashaDetail.rawScore,
            gocharaDetail.rawScore,
            ashtakavargaDetail.rawScore
        )

        val successProbability = (compositeScore * 100).roundToInt().coerceIn(0, 100)
        val qualityLevel = scoreToQuality(compositeScore)

        // Generate interpretation
        val (interpEn, interpNe) = generateInterpretation(dashaDetail, gocharaDetail, ashtakavargaDetail, compositeScore)
        val (recEn, recNe) = generateRecommendation(qualityLevel)

        return PillarSynthesis(
            date = analysisDate,
            dashaScore = dashaDetail.rawScore,
            gocharaScore = gocharaDetail.rawScore,
            ashtakavargaScore = ashtakavargaDetail.rawScore,
            compositeScore = compositeScore,
            successProbability = successProbability,
            qualityLevel = qualityLevel,
            dashaAnalysis = dashaDetail,
            gocharaAnalysis = gocharaDetail,
            ashtakavargaAnalysis = ashtakavargaDetail,
            interpretation = interpEn,
            interpretationNe = interpNe,
            recommendation = recEn,
            recommendationNe = recNe
        )
    }

    // ============================================================================
    // PILLAR 1: DASHA SCORE CALCULATION
    // ============================================================================

    private fun calculateDashaPillar(
        chart: VedicChart,
        dashaTimeline: DashaCalculator.DashaTimeline,
        analysisDateTime: LocalDateTime,
        lagnaSign: ZodiacSign
    ): DashaAnalysisDetail {
        val mahadasha = dashaTimeline.mahadashas.find { it.isActiveOn(analysisDateTime) }
        val antardasha = mahadasha?.getAntardashaOn(analysisDateTime)
        val pratyantardasha = antardasha?.getPratyantardashaOn(analysisDateTime)

        val mahadashaLord = mahadasha?.planet ?: Planet.SUN
        val antardashaLord = antardasha?.planet
        val pratyantardashaLord = pratyantardasha?.planet

        // Get planet position in natal chart
        val mahadashaPosition = chart.planetPositions.find { it.planet == mahadashaLord }
        val mahadashaSign = mahadashaPosition?.let { ZodiacSign.fromLongitude(it.longitude) }

        // Calculate dignity of Mahadasha lord
        val dignity = mahadashaSign?.let { getDignity(mahadashaLord, it) } ?: PlanetaryDignity.NEUTRAL_SIGN

        // Calculate Antardasha lord dignity
        val antardashaPosition = antardashaLord?.let { lord ->
            chart.planetPositions.find { it.planet == lord }
        }
        val antardashaDignity = antardashaPosition?.let {
            getDignity(antardashaLord!!, ZodiacSign.fromLongitude(it.longitude))
        }

        // Functional nature for this Lagna
        val functionalNature = getFunctionalNature(mahadashaLord, lagnaSign, chart)

        // House lordship of Dasha lord
        val houses = getHouseLordships(mahadashaLord, lagnaSign)

        // Check if in Sandhi period
        val isSandhi = dashaTimeline.upcomingSandhis.any { it.isWithinSandhi(analysisDateTime) }

        // Compute raw score
        var score = 0.0

        // Natural nature contribution (25%)
        val natureScore = if (mahadashaLord in NATURAL_BENEFICS) 0.75 else 0.35
        score += natureScore * 0.25

        // Dignity contribution (35%)
        score += dignity.multiplier * 0.35

        // Functional nature contribution (25%)
        score += functionalNature.multiplier * 0.25

        // Antardasha lord alignment (15%)
        val antardashaScore = if (antardashaLord != null) {
            val aaDignity = antardashaDignity?.multiplier ?: 0.5
            val aaFunctional = getFunctionalNature(antardashaLord, lagnaSign, chart).multiplier
            // Check if Antardasha lord is friend of Mahadasha lord
            val isFriend = NATURAL_FRIENDS[mahadashaLord]?.contains(antardashaLord) == true
            val friendBonus = if (isFriend) 0.15 else 0.0
            ((aaDignity + aaFunctional) / 2.0 + friendBonus).coerceIn(0.0, 1.0)
        } else {
            0.5
        }
        score += antardashaScore * 0.15

        // Sandhi penalty
        if (isSandhi) score *= 0.7

        score = score.coerceIn(0.0, 1.0)

        return DashaAnalysisDetail(
            mahadashaLord = mahadashaLord,
            antardashaLord = antardashaLord,
            pratyantardashaLord = pratyantardashaLord,
            mahadashaDignity = dignity,
            antardashaDignity = antardashaDignity,
            functionalNature = functionalNature,
            dashaLordHouses = houses,
            isSandhiPeriod = isSandhi,
            rawScore = score
        )
    }

    // ============================================================================
    // PILLAR 2: GOCHARA SCORE CALCULATION
    // ============================================================================

    private fun calculateGocharaPillar(
        chart: VedicChart,
        moonPosition: PlanetPosition
    ): GocharaAnalysisDetail {
        val moonSign = ZodiacSign.fromLongitude(moonPosition.longitude)
        val moonSignNumber = moonSign.number

        val planetResults = mutableListOf<PlanetGocharaResult>()

        // Analyze each planet's transit position relative to Moon
        val transitPlanets = listOf(
            Planet.SUN, Planet.MOON, Planet.MARS, Planet.MERCURY,
            Planet.JUPITER, Planet.VENUS, Planet.SATURN, Planet.RAHU, Planet.KETU
        )

        for (planet in transitPlanets) {
            val position = chart.planetPositions.find { it.planet == planet } ?: continue
            val transitSign = ZodiacSign.fromLongitude(position.longitude)
            val transitSignNumber = transitSign.number

            // Calculate house from Moon
            val houseFromMoon = ((transitSignNumber - moonSignNumber + 12) % 12) + 1

            // Check if favorable
            val favorableHouses = GOCHARA_FAVORABLE_HOUSES[planet] ?: emptySet()
            val isFavorable = houseFromMoon in favorableHouses

            // Check Vedha
            var hasVedha = false
            var vedhaByPlanet: Planet? = null

            if (isFavorable) {
                val vedhaPoints = VEDHA_POINTS[planet] ?: emptyMap()
                val vedhaHouse = vedhaPoints[houseFromMoon]
                if (vedhaHouse != null) {
                    // Check if any planet occupies the Vedha house from Moon
                    for (otherPlanet in transitPlanets) {
                        if (otherPlanet == planet) continue
                        // Exception: Sun-Saturn do not cause Vedha to each other (classical rule)
                        if ((planet == Planet.SUN && otherPlanet == Planet.SATURN) ||
                            (planet == Planet.SATURN && otherPlanet == Planet.SUN)) continue
                        // Exception: Moon-Mercury do not cause Vedha to each other
                        if ((planet == Planet.MOON && otherPlanet == Planet.MERCURY) ||
                            (planet == Planet.MERCURY && otherPlanet == Planet.MOON)) continue

                        val otherPos = chart.planetPositions.find { it.planet == otherPlanet } ?: continue
                        val otherSign = ZodiacSign.fromLongitude(otherPos.longitude)
                        val otherHouse = ((otherSign.number - moonSignNumber + 12) % 12) + 1
                        if (otherHouse == vedhaHouse) {
                            hasVedha = true
                            vedhaByPlanet = otherPlanet
                            break
                        }
                    }
                }
            }

            val effectiveness = when {
                isFavorable && !hasVedha -> GocharaEffectiveness.FAVORABLE_CLEAR
                isFavorable && hasVedha -> GocharaEffectiveness.FAVORABLE_VEDHA
                !isFavorable -> GocharaEffectiveness.UNFAVORABLE
                else -> GocharaEffectiveness.NEUTRAL
            }

            planetResults.add(
                PlanetGocharaResult(
                    planet = planet,
                    transitHouseFromMoon = houseFromMoon,
                    transitSign = transitSign,
                    isFavorable = isFavorable,
                    hasVedha = hasVedha,
                    vedhaByPlanet = vedhaByPlanet,
                    effectiveResult = effectiveness
                )
            )
        }

        val favorableCount = planetResults.count { it.effectiveResult == GocharaEffectiveness.FAVORABLE_CLEAR }
        val unfavorableCount = planetResults.count { it.effectiveResult == GocharaEffectiveness.UNFAVORABLE }
        val vedhaCount = planetResults.count { it.hasVedha }

        // Score: ratio of effectively favorable transits vs total
        // Weight by planet importance (Jupiter, Saturn transits are more significant)
        var weightedFavorable = 0.0
        var totalWeight = 0.0

        for (result in planetResults) {
            val planetWeight = getGocharaPlanetWeight(result.planet)
            totalWeight += planetWeight
            when (result.effectiveResult) {
                GocharaEffectiveness.FAVORABLE_CLEAR -> weightedFavorable += planetWeight
                GocharaEffectiveness.FAVORABLE_VEDHA -> weightedFavorable += planetWeight * 0.2 // Vedha reduces to 20%
                GocharaEffectiveness.NEUTRAL -> weightedFavorable += planetWeight * 0.5
                GocharaEffectiveness.UNFAVORABLE -> weightedFavorable += planetWeight * 0.1
            }
        }

        val rawScore = if (totalWeight > 0) (weightedFavorable / totalWeight).coerceIn(0.0, 1.0) else 0.5

        return GocharaAnalysisDetail(
            planetTransits = planetResults,
            favorableCount = favorableCount,
            unfavorableCount = unfavorableCount,
            vedhaActiveCount = vedhaCount,
            rawScore = rawScore
        )
    }

    // ============================================================================
    // PILLAR 3: ASHTAKAVARGA SCORE CALCULATION
    // ============================================================================

    private fun calculateAshtakavargaPillar(
        chart: VedicChart,
        dashaTimeline: DashaCalculator.DashaTimeline,
        ashtakavargaAnalysis: AshtakavargaCalculator.AshtakavargaAnalysis?,
        analysisDateTime: LocalDateTime
    ): AshtakavargaAnalysisDetail {
        val activeMahadasha = dashaTimeline.mahadashas.find { it.isActiveOn(analysisDateTime) }
        val dashaLord = activeMahadasha?.planet ?: Planet.SUN

        // Find the current transit sign of the Dasha Lord (using natal position as baseline)
        val dashaLordPosition = chart.planetPositions.find { it.planet == dashaLord }
        val transitSign = dashaLordPosition?.let { ZodiacSign.fromLongitude(it.longitude) } ?: ZodiacSign.ARIES

        // Get BAV score
        val bavScore = ashtakavargaAnalysis?.let { analysis ->
            getBavScore(analysis, dashaLord, transitSign)
        } ?: 4 // Default average

        // Get SAV score
        val savScore = ashtakavargaAnalysis?.let { analysis ->
            getSavScore(analysis, transitSign)
        } ?: 28 // Default threshold

        val bavStrength = when {
            bavScore >= 5 -> BinduStrength.HIGH
            bavScore >= 3 -> BinduStrength.MEDIUM
            else -> BinduStrength.LOW
        }

        val savAboveThreshold = savScore >= 28

        // Compute raw score
        // BAV contribution (60%): 0-8 scale -> 0.0-1.0
        val bavNormalized = (bavScore.toDouble() / 8.0).coerceIn(0.0, 1.0)

        // SAV contribution (40%): 0-56 scale, with 28 as midpoint
        val savNormalized = (savScore.toDouble() / 56.0).coerceIn(0.0, 1.0)

        val rawScore = (bavNormalized * 0.6 + savNormalized * 0.4).coerceIn(0.0, 1.0)

        return AshtakavargaAnalysisDetail(
            dashaLordBav = bavScore,
            dashaLordTransitSign = transitSign,
            savInTransitSign = savScore,
            bavStrength = bavStrength,
            savAboveThreshold = savAboveThreshold,
            rawScore = rawScore
        )
    }

    // ============================================================================
    // WEIGHTED SCORE CALCULATION
    // ============================================================================

    /**
     * Calculates the weighted harmonic mean of three pillar scores.
     * Harmonic mean is used instead of arithmetic mean because it penalizes
     * very low scores more heavily — a single failed pillar should significantly
     * reduce the overall score (classical principle: all three must align).
     */
    private fun calculateWeightedScore(
        dashaScore: Double,
        gocharaScore: Double,
        ashtakavargaScore: Double
    ): Double {
        // Avoid division by zero - floor at 0.01
        val d = max(dashaScore, 0.01)
        val g = max(gocharaScore, 0.01)
        val a = max(ashtakavargaScore, 0.01)

        // Weighted harmonic mean: (w1+w2+w3) / (w1/d + w2/g + w3/a)
        val totalWeight = DASHA_WEIGHT + GOCHARA_WEIGHT + ASHTAKAVARGA_WEIGHT
        val harmonicDenom = DASHA_WEIGHT / d + GOCHARA_WEIGHT / g + ASHTAKAVARGA_WEIGHT / a

        return (totalWeight / harmonicDenom).coerceIn(0.0, 1.0)
    }

    // ============================================================================
    // HELPER FUNCTIONS
    // ============================================================================

    private fun getDignity(planet: Planet, sign: ZodiacSign): PlanetaryDignity {
        return when {
            EXALTATION_SIGNS[planet] == sign -> PlanetaryDignity.EXALTED
            MOOLTRIKONA_SIGNS[planet] == sign -> PlanetaryDignity.MOOLTRIKONA
            OWN_SIGNS[planet]?.contains(sign) == true -> PlanetaryDignity.OWN_SIGN
            DEBILITATION_SIGNS[planet] == sign -> PlanetaryDignity.DEBILITATED
            else -> {
                // Check friendship with sign lord
                val signLord = sign.ruler
                when {
                    NATURAL_FRIENDS[planet]?.contains(signLord) == true -> PlanetaryDignity.FRIEND_SIGN
                    NATURAL_ENEMIES[planet]?.contains(signLord) == true -> PlanetaryDignity.ENEMY_SIGN
                    else -> PlanetaryDignity.NEUTRAL_SIGN
                }
            }
        }
    }

    private fun getFunctionalNature(planet: Planet, lagnaSign: ZodiacSign, chart: VedicChart): FunctionalNature {
        val beneficHouses = FUNCTIONAL_BENEFIC_LORDS_BY_LAGNA[lagnaSign] ?: setOf(1, 5, 9)
        val housesOwned = getHouseLordships(planet, lagnaSign)

        // Yogakaraka: owns both a Kendra (1,4,7,10) and Trikona (1,5,9)
        val kendraHouses = setOf(1, 4, 7, 10)
        val trikonaHouses = setOf(1, 5, 9)
        val ownsKendra = housesOwned.any { it in kendraHouses }
        val ownsTrikona = housesOwned.any { it in trikonaHouses }

        if (ownsKendra && ownsTrikona) return FunctionalNature.YOGAKARAKA

        // Maraka: lords of 2nd and 7th houses
        val marakaHouses = setOf(2, 7)
        if (housesOwned.any { it in marakaHouses } && housesOwned.none { it in trikonaHouses }) {
            return FunctionalNature.MARAKA
        }

        // Check if houses owned are in benefic list
        val allBenefic = housesOwned.all { it in beneficHouses }
        val anyBenefic = housesOwned.any { it in beneficHouses }

        // Trishadaya lords (3, 6, 11) are generally malefic
        val trishadaya = setOf(3, 6, 11)
        val dusthana = setOf(6, 8, 12)
        val anyDusthana = housesOwned.any { it in dusthana }

        return when {
            allBenefic -> FunctionalNature.BENEFIC
            anyDusthana && !anyBenefic -> FunctionalNature.MALEFIC
            anyBenefic -> FunctionalNature.NEUTRAL
            else -> FunctionalNature.NEUTRAL
        }
    }

    private fun getHouseLordships(planet: Planet, lagnaSign: ZodiacSign): List<Int> {
        val houses = mutableListOf<Int>()
        val ownSigns = OWN_SIGNS[planet] ?: return houses

        for (ownSign in ownSigns) {
            val houseNumber = ((ownSign.number - lagnaSign.number + 12) % 12) + 1
            houses.add(houseNumber)
        }

        return houses
    }

    private fun getGocharaPlanetWeight(planet: Planet): Double {
        return when (planet) {
            Planet.JUPITER -> 1.5   // Guru — most benefic
            Planet.SATURN -> 1.4    // Slowest, longest impact
            Planet.RAHU -> 1.2
            Planet.KETU -> 1.1
            Planet.MARS -> 1.0
            Planet.SUN -> 0.9
            Planet.VENUS -> 0.9
            Planet.MERCURY -> 0.8
            Planet.MOON -> 0.7      // Fastest, shortest impact
            else -> 0.5
        }
    }

    private fun getBavScore(
        analysis: AshtakavargaCalculator.AshtakavargaAnalysis,
        planet: Planet,
        sign: ZodiacSign
    ): Int {
        return analysis.bhinnashtakavarga[planet]?.getBindusForSign(sign) ?: 4
    }

    private fun getSavScore(
        analysis: AshtakavargaCalculator.AshtakavargaAnalysis,
        sign: ZodiacSign
    ): Int {
        return analysis.sarvashtakavarga.getBindusForSign(sign)
    }

    private fun scoreToQuality(score: Double): QualityLevel {
        val percent = (score * 100).toInt()
        return when {
            percent >= 90 -> QualityLevel.EXCEPTIONAL
            percent >= 78 -> QualityLevel.EXCELLENT
            percent >= 68 -> QualityLevel.VERY_GOOD
            percent >= 58 -> QualityLevel.GOOD
            percent >= 48 -> QualityLevel.ABOVE_AVERAGE
            percent >= 38 -> QualityLevel.AVERAGE
            percent >= 28 -> QualityLevel.BELOW_AVERAGE
            percent >= 18 -> QualityLevel.CHALLENGING
            else -> QualityLevel.DIFFICULT
        }
    }

    private fun generateInterpretation(
        dasha: DashaAnalysisDetail,
        gochara: GocharaAnalysisDetail,
        ashtakavarga: AshtakavargaAnalysisDetail,
        compositeScore: Double
    ): Pair<String, String> {
        return when {
            dasha.rawScore >= 0.7 && gochara.rawScore >= 0.6 && ashtakavarga.rawScore >= 0.6 -> {
                Pair(
                    StringResources.get(StringKeyTriplePillar.INTERP_STRONG_ALL_THREE, Language.ENGLISH),
                    StringResources.get(StringKeyTriplePillar.INTERP_STRONG_ALL_THREE, Language.NEPALI)
                )
            }
            dasha.rawScore >= 0.6 && gochara.rawScore < 0.4 -> {
                Pair(
                    StringResources.get(StringKeyTriplePillar.INTERP_DASHA_STRONG_TRANSIT_WEAK, Language.ENGLISH),
                    StringResources.get(StringKeyTriplePillar.INTERP_DASHA_STRONG_TRANSIT_WEAK, Language.NEPALI)
                )
            }
            gochara.rawScore >= 0.6 && dasha.rawScore < 0.4 -> {
                Pair(
                    StringResources.get(StringKeyTriplePillar.INTERP_TRANSIT_STRONG_DASHA_WEAK, Language.ENGLISH),
                    StringResources.get(StringKeyTriplePillar.INTERP_TRANSIT_STRONG_DASHA_WEAK, Language.NEPALI)
                )
            }
            ashtakavarga.rawScore >= 0.7 -> {
                Pair(
                    StringResources.get(StringKeyTriplePillar.INTERP_ASHTAKAVARGA_HIGH, Language.ENGLISH),
                    StringResources.get(StringKeyTriplePillar.INTERP_ASHTAKAVARGA_HIGH, Language.NEPALI)
                )
            }
            ashtakavarga.rawScore < 0.3 -> {
                Pair(
                    StringResources.get(StringKeyTriplePillar.INTERP_ASHTAKAVARGA_LOW, Language.ENGLISH),
                    StringResources.get(StringKeyTriplePillar.INTERP_ASHTAKAVARGA_LOW, Language.NEPALI)
                )
            }
            gochara.vedhaActiveCount >= 3 -> {
                Pair(
                    StringResources.get(StringKeyTriplePillar.INTERP_VEDHA_NULLIFIED, Language.ENGLISH),
                    StringResources.get(StringKeyTriplePillar.INTERP_VEDHA_NULLIFIED, Language.NEPALI)
                )
            }
            dasha.isSandhiPeriod -> {
                Pair(
                    StringResources.get(StringKeyTriplePillar.INTERP_SANDHI_PERIOD, Language.ENGLISH),
                    StringResources.get(StringKeyTriplePillar.INTERP_SANDHI_PERIOD, Language.NEPALI)
                )
            }
            else -> {
                // General interpretation based on overall score
                val quality = scoreToQuality(compositeScore)
                Pair(quality.en + " period overall.", quality.ne + " अवधि।")
            }
        }
    }

    private fun generateRecommendation(quality: QualityLevel): Pair<String, String> {
        return when (quality) {
            QualityLevel.EXCEPTIONAL, QualityLevel.EXCELLENT -> Pair(
                StringResources.get(StringKeyTriplePillar.REC_HIGHLY_FAVORABLE, Language.ENGLISH),
                StringResources.get(StringKeyTriplePillar.REC_HIGHLY_FAVORABLE, Language.NEPALI)
            )
            QualityLevel.VERY_GOOD, QualityLevel.GOOD -> Pair(
                StringResources.get(StringKeyTriplePillar.REC_FAVORABLE, Language.ENGLISH),
                StringResources.get(StringKeyTriplePillar.REC_FAVORABLE, Language.NEPALI)
            )
            QualityLevel.ABOVE_AVERAGE -> Pair(
                StringResources.get(StringKeyTriplePillar.REC_MODERATELY_FAVORABLE, Language.ENGLISH),
                StringResources.get(StringKeyTriplePillar.REC_MODERATELY_FAVORABLE, Language.NEPALI)
            )
            QualityLevel.AVERAGE -> Pair(
                StringResources.get(StringKeyTriplePillar.REC_NEUTRAL, Language.ENGLISH),
                StringResources.get(StringKeyTriplePillar.REC_NEUTRAL, Language.NEPALI)
            )
            QualityLevel.BELOW_AVERAGE -> Pair(
                StringResources.get(StringKeyTriplePillar.REC_CAUTION, Language.ENGLISH),
                StringResources.get(StringKeyTriplePillar.REC_CAUTION, Language.NEPALI)
            )
            QualityLevel.CHALLENGING, QualityLevel.DIFFICULT -> Pair(
                StringResources.get(StringKeyTriplePillar.REC_CHALLENGING, Language.ENGLISH),
                StringResources.get(StringKeyTriplePillar.REC_CHALLENGING, Language.NEPALI)
            )
        }
    }

    private fun getMonthNames(monthNumber: Int): Pair<String, String> {
        return when (monthNumber) {
            1 -> Pair(StringResources.get(StringKeyTriplePillar.MONTH_JAN, Language.ENGLISH),
                StringResources.get(StringKeyTriplePillar.MONTH_JAN, Language.NEPALI))
            2 -> Pair(StringResources.get(StringKeyTriplePillar.MONTH_FEB, Language.ENGLISH),
                StringResources.get(StringKeyTriplePillar.MONTH_FEB, Language.NEPALI))
            3 -> Pair(StringResources.get(StringKeyTriplePillar.MONTH_MAR, Language.ENGLISH),
                StringResources.get(StringKeyTriplePillar.MONTH_MAR, Language.NEPALI))
            4 -> Pair(StringResources.get(StringKeyTriplePillar.MONTH_APR, Language.ENGLISH),
                StringResources.get(StringKeyTriplePillar.MONTH_APR, Language.NEPALI))
            5 -> Pair(StringResources.get(StringKeyTriplePillar.MONTH_MAY, Language.ENGLISH),
                StringResources.get(StringKeyTriplePillar.MONTH_MAY, Language.NEPALI))
            6 -> Pair(StringResources.get(StringKeyTriplePillar.MONTH_JUN, Language.ENGLISH),
                StringResources.get(StringKeyTriplePillar.MONTH_JUN, Language.NEPALI))
            7 -> Pair(StringResources.get(StringKeyTriplePillar.MONTH_JUL, Language.ENGLISH),
                StringResources.get(StringKeyTriplePillar.MONTH_JUL, Language.NEPALI))
            8 -> Pair(StringResources.get(StringKeyTriplePillar.MONTH_AUG, Language.ENGLISH),
                StringResources.get(StringKeyTriplePillar.MONTH_AUG, Language.NEPALI))
            9 -> Pair(StringResources.get(StringKeyTriplePillar.MONTH_SEP, Language.ENGLISH),
                StringResources.get(StringKeyTriplePillar.MONTH_SEP, Language.NEPALI))
            10 -> Pair(StringResources.get(StringKeyTriplePillar.MONTH_OCT, Language.ENGLISH),
                StringResources.get(StringKeyTriplePillar.MONTH_OCT, Language.NEPALI))
            11 -> Pair(StringResources.get(StringKeyTriplePillar.MONTH_NOV, Language.ENGLISH),
                StringResources.get(StringKeyTriplePillar.MONTH_NOV, Language.NEPALI))
            12 -> Pair(StringResources.get(StringKeyTriplePillar.MONTH_DEC, Language.ENGLISH),
                StringResources.get(StringKeyTriplePillar.MONTH_DEC, Language.NEPALI))
            else -> Pair("Unknown", "अज्ञात")
        }
    }

    private fun getDominantLifeAreas(synthesis: PillarSynthesis): List<LifeAreaType> {
        val areas = mutableListOf<LifeAreaType>()

        // Based on Mahadasha lord's natural significations
        val dashaLord = synthesis.dashaAnalysis.mahadashaLord
        areas.addAll(getPlanetLifeAreas(dashaLord))

        // Based on house lordship
        for (house in synthesis.dashaAnalysis.dashaLordHouses) {
            areas.addAll(getHouseLifeAreas(house))
        }

        return areas.distinct().take(3)
    }

    private fun getPlanetLifeAreas(planet: Planet): List<LifeAreaType> {
        return when (planet) {
            Planet.SUN -> listOf(LifeAreaType.CAREER, LifeAreaType.LEGAL, LifeAreaType.HEALTH)
            Planet.MOON -> listOf(LifeAreaType.FAMILY, LifeAreaType.HEALTH, LifeAreaType.TRAVEL)
            Planet.MARS -> listOf(LifeAreaType.PROPERTY, LifeAreaType.CAREER, LifeAreaType.LEGAL)
            Planet.MERCURY -> listOf(LifeAreaType.EDUCATION, LifeAreaType.FINANCE, LifeAreaType.CAREER)
            Planet.JUPITER -> listOf(LifeAreaType.SPIRITUALITY, LifeAreaType.EDUCATION, LifeAreaType.FINANCE)
            Planet.VENUS -> listOf(LifeAreaType.RELATIONSHIPS, LifeAreaType.FINANCE, LifeAreaType.TRAVEL)
            Planet.SATURN -> listOf(LifeAreaType.CAREER, LifeAreaType.HEALTH, LifeAreaType.PROPERTY)
            Planet.RAHU -> listOf(LifeAreaType.CAREER, LifeAreaType.TRAVEL, LifeAreaType.LEGAL)
            Planet.KETU -> listOf(LifeAreaType.SPIRITUALITY, LifeAreaType.HEALTH, LifeAreaType.EDUCATION)
            else -> listOf(LifeAreaType.CAREER)
        }
    }

    private fun getHouseLifeAreas(house: Int): List<LifeAreaType> {
        return when (house) {
            1 -> listOf(LifeAreaType.HEALTH, LifeAreaType.CAREER)
            2 -> listOf(LifeAreaType.FINANCE, LifeAreaType.FAMILY)
            3 -> listOf(LifeAreaType.EDUCATION, LifeAreaType.TRAVEL)
            4 -> listOf(LifeAreaType.FAMILY, LifeAreaType.PROPERTY)
            5 -> listOf(LifeAreaType.EDUCATION, LifeAreaType.SPIRITUALITY)
            6 -> listOf(LifeAreaType.HEALTH, LifeAreaType.LEGAL)
            7 -> listOf(LifeAreaType.RELATIONSHIPS, LifeAreaType.CAREER)
            8 -> listOf(LifeAreaType.HEALTH, LifeAreaType.SPIRITUALITY)
            9 -> listOf(LifeAreaType.SPIRITUALITY, LifeAreaType.TRAVEL)
            10 -> listOf(LifeAreaType.CAREER, LifeAreaType.LEGAL)
            11 -> listOf(LifeAreaType.FINANCE, LifeAreaType.CAREER)
            12 -> listOf(LifeAreaType.SPIRITUALITY, LifeAreaType.TRAVEL)
            else -> listOf(LifeAreaType.CAREER)
        }
    }

    private fun detectWindows(
        forecasts: List<MonthlyForecast>,
        threshold: Double,
        isPeak: Boolean
    ): List<OpportunityWindow> {
        val windows = mutableListOf<OpportunityWindow>()
        var windowStart: LocalDate? = null
        var windowScore = 0.0
        var windowCount = 0
        var windowAreas = mutableSetOf<LifeAreaType>()

        for (forecast in forecasts) {
            val meetsCondition = if (isPeak) {
                forecast.compositeScore >= threshold
            } else {
                forecast.compositeScore < threshold
            }

            if (meetsCondition) {
                if (windowStart == null) {
                    windowStart = forecast.month
                }
                windowScore += forecast.compositeScore
                windowCount++
                windowAreas.addAll(forecast.dominantLifeAreas)
            } else {
                if (windowStart != null && windowCount > 0) {
                    val avgScore = windowScore / windowCount
                    val quality = scoreToQuality(avgScore)
                    val endDate = forecasts[forecasts.indexOf(forecast) - 1].month
                    val (descEn, descNe) = if (isPeak) {
                        Pair(
                            "${quality.en} opportunity window spanning $windowCount month(s)",
                            "${quality.ne} अवसर सञ्झ्याल $windowCount महिना(हरू) फैलिएको"
                        )
                    } else {
                        Pair(
                            "Caution period spanning $windowCount month(s) - patience recommended",
                            "सावधानी अवधि $windowCount महिना(हरू) फैलिएको - धैर्य सिफारिस"
                        )
                    }
                    windows.add(
                        OpportunityWindow(
                            startDate = windowStart,
                            endDate = endDate,
                            score = avgScore,
                            qualityLevel = quality,
                            description = descEn,
                            descriptionNe = descNe,
                            lifeAreas = windowAreas.toList()
                        )
                    )
                    windowStart = null
                    windowScore = 0.0
                    windowCount = 0
                    windowAreas = mutableSetOf()
                }
            }
        }

        // Handle trailing window
        if (windowStart != null && windowCount > 0) {
            val avgScore = windowScore / windowCount
            val quality = scoreToQuality(avgScore)
            val endDate = forecasts.last().month
            val (descEn, descNe) = if (isPeak) {
                Pair(
                    "${quality.en} opportunity window spanning $windowCount month(s)",
                    "${quality.ne} अवसर सञ्झ्याल $windowCount महिना(हरू) फैलिएको"
                )
            } else {
                Pair(
                    "Caution period spanning $windowCount month(s) - patience recommended",
                    "सावधानी अवधि $windowCount महिना(हरू) फैलिएको - धैर्य सिफारिस"
                )
            }
            windows.add(
                OpportunityWindow(
                    startDate = windowStart,
                    endDate = endDate,
                    score = avgScore,
                    qualityLevel = quality,
                    description = descEn,
                    descriptionNe = descNe,
                    lifeAreas = windowAreas.toList()
                )
            )
        }

        return windows
    }

    private fun calculateLifeAreaImpacts(
        synthesis: PillarSynthesis,
        chart: VedicChart,
        language: Language
    ): List<LifeAreaImpact> {
        val areaScores = mutableMapOf<LifeAreaType, MutableList<Double>>()

        // Add Dasha lord's natural signification scores
        val dashaAreas = getPlanetLifeAreas(synthesis.dashaAnalysis.mahadashaLord)
        for (area in dashaAreas) {
            areaScores.getOrPut(area) { mutableListOf() }.add(synthesis.dashaScore)
        }

        // Add house lordship impacts
        for (house in synthesis.dashaAnalysis.dashaLordHouses) {
            val houseAreas = getHouseLifeAreas(house)
            for (area in houseAreas) {
                areaScores.getOrPut(area) { mutableListOf() }.add(synthesis.dashaScore * 0.8)
            }
        }

        // Add Gochara impacts per planet
        for (gocharaResult in synthesis.gocharaAnalysis.planetTransits) {
            val planetAreas = getPlanetLifeAreas(gocharaResult.planet)
            val gocharaScore = when (gocharaResult.effectiveResult) {
                GocharaEffectiveness.FAVORABLE_CLEAR -> 0.85
                GocharaEffectiveness.FAVORABLE_VEDHA -> 0.4
                GocharaEffectiveness.UNFAVORABLE -> 0.2
                GocharaEffectiveness.NEUTRAL -> 0.5
            }
            for (area in planetAreas) {
                areaScores.getOrPut(area) { mutableListOf() }.add(gocharaScore)
            }
        }

        return areaScores.map { (area, scores) ->
            val avgScore = scores.average()
            val quality = scoreToQuality(avgScore)
            LifeAreaImpact(
                area = area,
                impactScore = avgScore,
                qualityLevel = quality,
                description = "${area.en}: ${quality.en}",
                descriptionNe = "${area.ne}: ${quality.ne}"
            )
        }.sortedByDescending { it.impactScore }
    }
}
