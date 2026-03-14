package com.astro.vajra.ephemeris

import com.astro.vajra.core.model.Nakshatra
import com.astro.vajra.core.model.Planet
import com.astro.vajra.core.model.PlanetPosition
import com.astro.vajra.core.model.VedicChart
import com.astro.vajra.core.model.ZodiacSign
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZoneOffset
import java.time.temporal.ChronoUnit
import kotlin.math.abs

/**
 * Karmic Node Transformation Analyzer
 *
 * Provides deep karmic analysis based on the Rahu-Ketu axis across divisional charts,
 * combined with Bhrigu Bindu transit tracking for timing karmic events. This analyzer
 * extends the existing [BhriguBinduCalculator] with multi-varga node analysis, karmic
 * weight scoring, theme identification, past-life indicators, and transformation windows.
 *
 * **Core Analysis Domains:**
 *
 * 1. **Rahu-Ketu Axis Analysis across Vargas (D1, D9, D10, D12, D60)**
 *    - Identifies the houses, signs, and nakshatras of the nodes in each divisional chart
 *    - Maps conjunctions and aspects to the nodes
 *    - Determines dispositor strength for karmic expression modulation
 *
 * 2. **Karmic Weight Calculator**
 *    - Scores karmic intensity from 0-100 based on node placement, conjunctions,
 *      axis orientation, and dispositor dignity
 *    - Identifies specific doshas (Grahan Dosha, Kala Sarpa proximity)
 *
 * 3. **Bhrigu Bindu Transit Tracker**
 *    - Tracks when slow-moving transits (Jupiter, Saturn, Rahu) cross or aspect the BB
 *    - Classifies transit events as karmic reward, lesson, or acceleration
 *
 * 4. **Karmic Theme Identification**
 *    - Maps the Rahu-Ketu axis houses to life-area karmic themes
 *    - Provides detailed narrative interpretation
 *
 * 5. **Past Life Indicators (from Ketu)**
 *    - Analyzes Ketu's sign, house, nakshatra, and conjunctions
 *    - Interprets innate abilities and subconscious patterns
 *
 * 6. **Current Life Direction (from Rahu)**
 *    - Analyzes Rahu's sign, house, nakshatra, and conjunctions
 *    - Identifies growth direction and unfulfilled desires
 *
 * Based on:
 * - Bhrigu Samhita traditions
 * - Nadi astrology principles (Rahu-Ketu karmic axis)
 * - K.N. Rao's works on karmic astrology
 * - Prash Trivedi's "The Key of Life" (Rahu-Ketu analysis)
 * - Classical Parashari and Jaimini references
 *
 * @author AstroVajra
 */
object KarmicNodeTransformationAnalyzer {

    // =========================================================================
    // DATA CLASSES
    // =========================================================================

    /**
     * Complete karmic node analysis result.
     *
     * @property rahuKetuAxisByVarga Rahu-Ketu axis info for each analyzed divisional chart
     * @property karmicWeight Computed karmic intensity score and contributing factors
     * @property bhriguBinduTransits Predicted transit events over/aspecting the Bhrigu Bindu
     * @property karmicTheme The primary karmic theme derived from the Rahu-Ketu axis houses
     * @property pastLifeIndicators Profile of past-life tendencies from Ketu
     * @property currentLifeDirection Profile of current-life growth direction from Rahu
     * @property unavoidableKarmaPoints Specific degrees/events marking unavoidable karmic manifestation
     * @property karmicTransformationWindows Time windows when karmic transformation is most active
     */
    data class KarmicNodeAnalysis(
        val rahuKetuAxisByVarga: Map<String, RahuKetuAxisInfo>,
        val karmicWeight: KarmicWeight,
        val bhriguBinduTransits: List<BhriguBinduTransitEvent>,
        val karmicTheme: KarmicTheme,
        val pastLifeIndicators: PastLifeProfile,
        val currentLifeDirection: CurrentLifeProfile,
        val unavoidableKarmaPoints: List<UnavoidableKarmaPoint>,
        val karmicTransformationWindows: List<TransformationWindow>
    )

    /**
     * Rahu-Ketu axis information for a single divisional chart.
     */
    data class RahuKetuAxisInfo(
        val vargaName: String,
        val rahuSign: ZodiacSign,
        val rahuHouse: Int,
        val rahuNakshatra: Nakshatra,
        val rahuNakshatraPada: Int,
        val ketuSign: ZodiacSign,
        val ketuHouse: Int,
        val ketuNakshatra: Nakshatra,
        val ketuNakshatraPada: Int,
        val rahuDispositor: Planet,
        val ketuDispositor: Planet,
        val rahuDispositorDignity: VedicAstrologyUtils.PlanetaryDignity,
        val ketuDispositorDignity: VedicAstrologyUtils.PlanetaryDignity,
        val planetsConjunctRahu: List<Planet>,
        val planetsConjunctKetu: List<Planet>,
        val planetsAspectingRahu: List<Planet>,
        val planetsAspectingKetu: List<Planet>,
        val axisTheme: String
    )

    /**
     * Karmic weight assessment with contributing factors.
     */
    data class KarmicWeight(
        val totalScore: Double,
        val intensity: KarmicIntensity,
        val factors: List<KarmicFactor>,
        val doshas: List<KarmicDosha>,
        val summary: String
    )

    enum class KarmicIntensity {
        EXTREME,
        VERY_HIGH,
        HIGH,
        MODERATE,
        LOW
    }

    data class KarmicFactor(
        val description: String,
        val weight: Double,
        val nature: FactorNature
    )

    enum class FactorNature {
        AMPLIFYING,
        NEUTRAL,
        MITIGATING
    }

    data class KarmicDosha(
        val name: String,
        val severity: DoshaSeverity,
        val description: String,
        val remedialHint: String
    )

    enum class DoshaSeverity {
        SEVERE,
        MODERATE,
        MILD
    }

    /**
     * A predicted transit event affecting the Bhrigu Bindu.
     */
    data class BhriguBinduTransitEvent(
        val transitPlanet: Planet,
        val eventType: BBTransitEventType,
        val estimatedDate: LocalDate,
        val transitLongitude: Double,
        val bhriguBinduLongitude: Double,
        val orbDegrees: Double,
        val significance: BBTransitSignificance,
        val interpretation: String
    )

    enum class BBTransitEventType {
        CONJUNCTION,
        OPPOSITION,
        TRINE,
        SQUARE,
        SEXTILE
    }

    enum class BBTransitSignificance {
        KARMIC_REWARD,
        KARMIC_LESSON,
        KARMIC_ACCELERATION,
        KARMIC_RELEASE,
        MINOR_ACTIVATION
    }

    /**
     * Karmic theme derived from the Rahu-Ketu axis houses.
     */
    data class KarmicTheme(
        val axisHouses: Pair<Int, Int>,
        val themeName: String,
        val themeDescription: String,
        val rahuGrowthArea: String,
        val ketuMasteredArea: String,
        val balancingAdvice: String,
        val lifeLesson: String
    )

    /**
     * Past-life profile based on Ketu analysis.
     */
    data class PastLifeProfile(
        val ketuSign: ZodiacSign,
        val ketuHouse: Int,
        val ketuNakshatra: Nakshatra,
        val nakshatraLord: Planet,
        val pastLifeExpertise: String,
        val innateAbilities: List<String>,
        val subconciousPatterns: List<String>,
        val conjunctPlanets: List<Planet>,
        val conjunctionInterpretation: String,
        val overallNarrative: String
    )

    /**
     * Current-life direction profile based on Rahu analysis.
     */
    data class CurrentLifeProfile(
        val rahuSign: ZodiacSign,
        val rahuHouse: Int,
        val rahuNakshatra: Nakshatra,
        val nakshatraLord: Planet,
        val growthDirection: String,
        val desiresToFulfill: List<String>,
        val challengesOnPath: List<String>,
        val conjunctPlanets: List<Planet>,
        val conjunctionInterpretation: String,
        val overallNarrative: String
    )

    /**
     * A specific degree/event marking unavoidable karmic manifestation.
     */
    data class UnavoidableKarmaPoint(
        val longitude: Double,
        val sign: ZodiacSign,
        val house: Int,
        val description: String,
        val source: String,
        val intensity: Double
    )

    /**
     * A time window when karmic transformation is most active.
     */
    data class TransformationWindow(
        val startDate: LocalDate,
        val endDate: LocalDate,
        val triggerPlanet: Planet,
        val triggerType: String,
        val description: String,
        val intensity: TransformationIntensity
    )

    enum class TransformationIntensity {
        PEAK,
        HIGH,
        MODERATE,
        MILD
    }

    // =========================================================================
    // CORE ANALYSIS METHOD
    // =========================================================================

    /**
     * Perform complete karmic node transformation analysis on a Vedic chart.
     *
     * @param chart The natal VedicChart to analyze
     * @param currentDate The reference date for transit calculations (defaults to today)
     * @param transitPositions Optional current planetary transit positions; if null, transits
     *        are estimated using mean daily motion approximations
     * @return Complete [KarmicNodeAnalysis]
     * @throws IllegalArgumentException if Rahu or Ketu positions are missing from the chart
     */
    fun analyze(
        chart: VedicChart,
        currentDate: LocalDate = LocalDate.now(resolveZoneId(chart.birthData.timezone)),
        transitPositions: List<PlanetPosition>? = null
    ): KarmicNodeAnalysis {
        val rahuPos = chart.planetPositions.find { it.planet == Planet.RAHU }
            ?: throw IllegalArgumentException("Rahu position not found in chart")
        val ketuPos = chart.planetPositions.find { it.planet == Planet.KETU }
            ?: throw IllegalArgumentException("Ketu position not found in chart")

        // 1. Rahu-Ketu axis across divisional charts
        val axisMap = analyzeAxisAcrossVargas(chart)

        // 2. Karmic weight
        val karmicWeight = calculateKarmicWeight(chart, rahuPos, ketuPos)

        // 3. Bhrigu Bindu transit tracking
        val bbLongitude = BhriguBinduCalculator.calculateBhriguBindu(chart)
        val bbTransits = trackBhriguBinduTransits(
            chart, bbLongitude, currentDate, transitPositions
        )

        // 4. Karmic theme
        val karmicTheme = identifyKarmicTheme(rahuPos.house, ketuPos.house)

        // 5. Past life indicators
        val pastLife = analyzePastLifeIndicators(chart, ketuPos)

        // 6. Current life direction
        val currentLife = analyzeCurrentLifeDirection(chart, rahuPos)

        // 7. Unavoidable karma points
        val karmaPoints = calculateUnavoidableKarmaPoints(chart, rahuPos, ketuPos, bbLongitude)

        // 8. Transformation windows
        val windows = calculateTransformationWindows(chart, bbLongitude, currentDate, transitPositions)

        return KarmicNodeAnalysis(
            rahuKetuAxisByVarga = axisMap,
            karmicWeight = karmicWeight,
            bhriguBinduTransits = bbTransits,
            karmicTheme = karmicTheme,
            pastLifeIndicators = pastLife,
            currentLifeDirection = currentLife,
            unavoidableKarmaPoints = karmaPoints,
            karmicTransformationWindows = windows
        )
    }

    // =========================================================================
    // 1. RAHU-KETU AXIS ACROSS DIVISIONAL CHARTS
    // =========================================================================

    /**
     * Analyze the Rahu-Ketu axis in key divisional charts.
     *
     * Examines D1 (Rashi), D9 (Navamsa), D10 (Dasamsa), D12 (Dwadasamsa), D60 (Shashtiamsa)
     * to build a multi-layered picture of the karmic axis across different life domains.
     */
    private fun analyzeAxisAcrossVargas(chart: VedicChart): Map<String, RahuKetuAxisInfo> {
        val vargaTypes = listOf(
            DivisionalChartType.D1_RASHI,
            DivisionalChartType.D9_NAVAMSA,
            DivisionalChartType.D10_DASAMSA,
            DivisionalChartType.D12_DWADASAMSA,
            DivisionalChartType.D60_SHASHTIAMSA
        )

        val result = mutableMapOf<String, RahuKetuAxisInfo>()

        for (vargaType in vargaTypes) {
            try {
                val vargaChart = DivisionalChartCalculator.calculateDivisionalChart(chart, vargaType)
                val rahuVarga = vargaChart.planetPositions.find { it.planet == Planet.RAHU }
                val ketuVarga = vargaChart.planetPositions.find { it.planet == Planet.KETU }

                if (rahuVarga != null && ketuVarga != null) {
                    val rahuDispositor = rahuVarga.sign.ruler
                    val ketuDispositor = ketuVarga.sign.ruler

                    // Find dispositor positions for dignity assessment
                    val rahuDispPos = vargaChart.planetPositions.find { it.planet == rahuDispositor }
                    val ketuDispPos = vargaChart.planetPositions.find { it.planet == ketuDispositor }

                    val rahuDispDignity = if (rahuDispPos != null) VedicAstrologyUtils.getDignity(rahuDispPos)
                    else VedicAstrologyUtils.PlanetaryDignity.NEUTRAL_SIGN
                    val ketuDispDignity = if (ketuDispPos != null) VedicAstrologyUtils.getDignity(ketuDispPos)
                    else VedicAstrologyUtils.PlanetaryDignity.NEUTRAL_SIGN

                    // Find planets conjunct nodes (same sign)
                    val conjunctRahu = vargaChart.planetPositions
                        .filter { it.planet != Planet.RAHU && it.planet != Planet.KETU && it.sign == rahuVarga.sign }
                        .map { it.planet }
                    val conjunctKetu = vargaChart.planetPositions
                        .filter { it.planet != Planet.RAHU && it.planet != Planet.KETU && it.sign == ketuVarga.sign }
                        .map { it.planet }

                    // Find planets aspecting nodes
                    val aspectingRahu = findPlanetsAspecting(vargaChart.planetPositions, rahuVarga.house)
                    val aspectingKetu = findPlanetsAspecting(vargaChart.planetPositions, ketuVarga.house)

                    val axisTheme = deriveAxisTheme(vargaType, rahuVarga.house, ketuVarga.house)

                    result[vargaType.shortName] = RahuKetuAxisInfo(
                        vargaName = vargaType.displayName,
                        rahuSign = rahuVarga.sign,
                        rahuHouse = rahuVarga.house,
                        rahuNakshatra = rahuVarga.nakshatra,
                        rahuNakshatraPada = rahuVarga.nakshatraPada,
                        ketuSign = ketuVarga.sign,
                        ketuHouse = ketuVarga.house,
                        ketuNakshatra = ketuVarga.nakshatra,
                        ketuNakshatraPada = ketuVarga.nakshatraPada,
                        rahuDispositor = rahuDispositor,
                        ketuDispositor = ketuDispositor,
                        rahuDispositorDignity = rahuDispDignity,
                        ketuDispositorDignity = ketuDispDignity,
                        planetsConjunctRahu = conjunctRahu,
                        planetsConjunctKetu = conjunctKetu,
                        planetsAspectingRahu = aspectingRahu,
                        planetsAspectingKetu = aspectingKetu,
                        axisTheme = axisTheme
                    )
                }
            } catch (e: Exception) {
                // If a divisional chart calculation fails, skip it gracefully
                android.util.Log.e("KarmicNodeAnalyzer", "Error calculating ${vargaType.shortName}: ${e.message}")
            }
        }

        return result
    }

    /**
     * Find all planets that aspect a given house via Vedic (Parashari) aspects.
     */
    private fun findPlanetsAspecting(
        positions: List<PlanetPosition>,
        targetHouse: Int
    ): List<Planet> {
        return positions
            .filter { it.planet != Planet.RAHU && it.planet != Planet.KETU }
            .filter { pos ->
                val aspectedHouses = VedicAstrologyUtils.getAspectedHouses(pos.planet, pos.house)
                targetHouse in aspectedHouses
            }
            .map { it.planet }
    }

    /**
     * Derive a contextual axis theme label for a specific varga chart.
     */
    private fun deriveAxisTheme(
        vargaType: DivisionalChartType,
        rahuHouse: Int,
        ketuHouse: Int
    ): String {
        val baseTheme = getAxisThemeByHouses(rahuHouse, ketuHouse)
        return when (vargaType) {
            DivisionalChartType.D1_RASHI -> "Overall Life: $baseTheme"
            DivisionalChartType.D9_NAVAMSA -> "Dharma/Marriage: $baseTheme"
            DivisionalChartType.D10_DASAMSA -> "Career/Public Life: $baseTheme"
            DivisionalChartType.D12_DWADASAMSA -> "Ancestry/Parents: $baseTheme"
            DivisionalChartType.D60_SHASHTIAMSA -> "Past Life Karma: $baseTheme"
            else -> baseTheme
        }
    }

    /**
     * Map house axis to karmic theme label.
     */
    private fun getAxisThemeByHouses(rahuHouse: Int, ketuHouse: Int): String {
        val pair = setOf(rahuHouse, ketuHouse)
        return when {
            pair == setOf(1, 7) -> "Self vs Partnership"
            pair == setOf(2, 8) -> "Resources vs Transformation"
            pair == setOf(3, 9) -> "Communication vs Higher Wisdom"
            pair == setOf(4, 10) -> "Home/Security vs Career/Status"
            pair == setOf(5, 11) -> "Creativity vs Community/Gains"
            pair == setOf(6, 12) -> "Service/Health vs Surrender/Spirituality"
            // Non-opposite axis placements
            rahuHouse in listOf(1, 4, 7, 10) -> "Kendra-axis karmic emphasis"
            rahuHouse in listOf(5, 9) -> "Dharma-trikona karmic growth"
            rahuHouse in listOf(6, 8, 12) -> "Dusthana karmic purification"
            else -> "Houses $rahuHouse-$ketuHouse karmic activation"
        }
    }

    // =========================================================================
    // 2. KARMIC WEIGHT CALCULATOR
    // =========================================================================

    /**
     * Calculate the overall karmic weight/intensity score.
     *
     * The score ranges from 0 (minimal karmic activation) to 100 (extreme karmic intensity).
     * It considers:
     * - Node placement in Kendra, Trikona, or Dusthana houses
     * - Conjunctions with luminaries (Grahan Dosha)
     * - Dispositor strength
     * - Presence of specific doshas
     * - Axis orientation themes
     */
    private fun calculateKarmicWeight(
        chart: VedicChart,
        rahuPos: PlanetPosition,
        ketuPos: PlanetPosition
    ): KarmicWeight {
        val factors = mutableListOf<KarmicFactor>()
        val doshas = mutableListOf<KarmicDosha>()
        var score = 0.0

        // --- Factor: Rahu in Kendra ---
        if (VedicAstrologyUtils.isKendra(rahuPos.house)) {
            score += 15.0
            factors.add(KarmicFactor(
                "Rahu in Kendra house ${rahuPos.house}: prominent karmic agenda in this life",
                15.0, FactorNature.AMPLIFYING
            ))
        }

        // --- Factor: Ketu in Kendra ---
        if (VedicAstrologyUtils.isKendra(ketuPos.house)) {
            score += 12.0
            factors.add(KarmicFactor(
                "Ketu in Kendra house ${ketuPos.house}: past-life mastery actively influencing present",
                12.0, FactorNature.AMPLIFYING
            ))
        }

        // --- Factor: Rahu in Trikona ---
        if (VedicAstrologyUtils.isTrikona(rahuPos.house)) {
            score += 10.0
            factors.add(KarmicFactor(
                "Rahu in Trikona house ${rahuPos.house}: karmic growth aligned with dharma",
                10.0, FactorNature.AMPLIFYING
            ))
        }

        // --- Factor: Nodes in Dusthana ---
        if (VedicAstrologyUtils.isDusthana(rahuPos.house)) {
            score += 8.0
            factors.add(KarmicFactor(
                "Rahu in Dusthana house ${rahuPos.house}: karmic purification through challenges",
                8.0, FactorNature.AMPLIFYING
            ))
        }
        if (VedicAstrologyUtils.isDusthana(ketuPos.house)) {
            score += 6.0
            factors.add(KarmicFactor(
                "Ketu in Dusthana house ${ketuPos.house}: past-life debts being resolved",
                6.0, FactorNature.AMPLIFYING
            ))
        }

        // --- Factor: Grahan Dosha (Rahu/Ketu conjunct Sun or Moon) ---
        val sunPos = chart.planetPositions.find { it.planet == Planet.SUN }
        val moonPos = chart.planetPositions.find { it.planet == Planet.MOON }

        if (sunPos != null && sunPos.sign == rahuPos.sign) {
            score += 20.0
            factors.add(KarmicFactor(
                "Rahu conjunct Sun (Surya Grahan Dosha): intense ego-transformation karma",
                20.0, FactorNature.AMPLIFYING
            ))
            doshas.add(KarmicDosha(
                "Surya Grahan Dosha",
                DoshaSeverity.SEVERE,
                "Rahu eclipses the Sun, creating identity crises and father-related karmic patterns. " +
                        "The soul must learn to shine authentically without ego inflation or deflation.",
                "Surya Namaskar at sunrise, Aditya Hridayam Stotra, ruby or spinel gemstone after consulting an astrologer"
            ))
        }
        if (sunPos != null && sunPos.sign == ketuPos.sign) {
            score += 15.0
            factors.add(KarmicFactor(
                "Ketu conjunct Sun: past-life authority/ego patterns dissolving",
                15.0, FactorNature.AMPLIFYING
            ))
        }
        if (moonPos != null && moonPos.sign == rahuPos.sign) {
            score += 20.0
            factors.add(KarmicFactor(
                "Rahu conjunct Moon (Chandra Grahan Dosha): intense emotional/mental karma",
                20.0, FactorNature.AMPLIFYING
            ))
            doshas.add(KarmicDosha(
                "Chandra Grahan Dosha",
                DoshaSeverity.SEVERE,
                "Rahu eclipses the Moon, creating emotional turbulence, anxiety, and mother-related karmic patterns. " +
                        "Deep psychic sensitivity may manifest as intuition or obsessive thinking.",
                "Chandra Namaskar, meditation on Monday evenings, pearl gemstone after consulting an astrologer, Durga Saptashati recitation"
            ))
        }
        if (moonPos != null && moonPos.sign == ketuPos.sign) {
            score += 15.0
            factors.add(KarmicFactor(
                "Ketu conjunct Moon: past-life emotional detachment and spiritual sensitivity",
                15.0, FactorNature.AMPLIFYING
            ))
        }

        // --- Factor: Rahu-Ketu axis across 1-7 or 4-10 (high-impact axes) ---
        val rahuKetuPair = setOf(rahuPos.house, ketuPos.house)
        if (rahuKetuPair == setOf(1, 7) || rahuKetuPair == setOf(4, 10)) {
            score += 10.0
            factors.add(KarmicFactor(
                "Rahu-Ketu on high-impact axis (${rahuPos.house}-${ketuPos.house}): core life-direction karma",
                10.0, FactorNature.AMPLIFYING
            ))
        }

        // --- Factor: Dispositor strength ---
        val rahuDispositor = rahuPos.sign.ruler
        val ketuDispositor = ketuPos.sign.ruler
        val rahuDispPos = chart.planetPositions.find { it.planet == rahuDispositor }
        val ketuDispPos = chart.planetPositions.find { it.planet == ketuDispositor }

        if (rahuDispPos != null) {
            val dignity = VedicAstrologyUtils.getDignity(rahuDispPos)
            when (dignity) {
                VedicAstrologyUtils.PlanetaryDignity.EXALTED -> {
                    score -= 5.0
                    factors.add(KarmicFactor(
                        "Rahu's dispositor ${rahuDispositor.displayName} is exalted: karmic expression is supported and constructive",
                        -5.0, FactorNature.MITIGATING
                    ))
                }
                VedicAstrologyUtils.PlanetaryDignity.OWN_SIGN, VedicAstrologyUtils.PlanetaryDignity.MOOLATRIKONA -> {
                    score -= 3.0
                    factors.add(KarmicFactor(
                        "Rahu's dispositor ${rahuDispositor.displayName} is strong: karmic agenda has good support",
                        -3.0, FactorNature.MITIGATING
                    ))
                }
                VedicAstrologyUtils.PlanetaryDignity.DEBILITATED -> {
                    score += 8.0
                    factors.add(KarmicFactor(
                        "Rahu's dispositor ${rahuDispositor.displayName} is debilitated: karmic expression faces obstacles",
                        8.0, FactorNature.AMPLIFYING
                    ))
                }
                else -> { /* neutral - no adjustment */ }
            }
        }
        if (ketuDispPos != null) {
            val dignity = VedicAstrologyUtils.getDignity(ketuDispPos)
            when (dignity) {
                VedicAstrologyUtils.PlanetaryDignity.EXALTED -> {
                    score -= 3.0
                    factors.add(KarmicFactor(
                        "Ketu's dispositor ${ketuDispositor.displayName} is exalted: past-life skills are easily accessible",
                        -3.0, FactorNature.MITIGATING
                    ))
                }
                VedicAstrologyUtils.PlanetaryDignity.DEBILITATED -> {
                    score += 5.0
                    factors.add(KarmicFactor(
                        "Ketu's dispositor ${ketuDispositor.displayName} is debilitated: past-life patterns may manifest as confusion",
                        5.0, FactorNature.AMPLIFYING
                    ))
                }
                else -> { /* neutral */ }
            }
        }

        // --- Check for other planets conjunct nodes ---
        val planetsWithRahu = chart.planetPositions.filter {
            it.planet !in listOf(Planet.RAHU, Planet.KETU, Planet.SUN, Planet.MOON) && it.sign == rahuPos.sign
        }
        val planetsWithKetu = chart.planetPositions.filter {
            it.planet !in listOf(Planet.RAHU, Planet.KETU, Planet.SUN, Planet.MOON) && it.sign == ketuPos.sign
        }

        for (p in planetsWithRahu) {
            score += 5.0
            factors.add(KarmicFactor(
                "${p.planet.displayName} conjunct Rahu: ${p.planet.displayName}'s significations drawn into karmic vortex",
                5.0, FactorNature.AMPLIFYING
            ))
        }
        for (p in planetsWithKetu) {
            score += 4.0
            factors.add(KarmicFactor(
                "${p.planet.displayName} conjunct Ketu: ${p.planet.displayName}'s significations touched by past-life energy",
                4.0, FactorNature.AMPLIFYING
            ))
        }

        // --- Normalize score ---
        score = score.coerceIn(0.0, 100.0)

        val intensity = when {
            score >= 80 -> KarmicIntensity.EXTREME
            score >= 60 -> KarmicIntensity.VERY_HIGH
            score >= 40 -> KarmicIntensity.HIGH
            score >= 20 -> KarmicIntensity.MODERATE
            else -> KarmicIntensity.LOW
        }

        val summary = buildString {
            append("Karmic intensity: ${intensity.name} (${String.format("%.1f", score)}/100). ")
            if (doshas.isNotEmpty()) {
                append("Doshas detected: ${doshas.joinToString { it.name }}. ")
            }
            append("Rahu in house ${rahuPos.house} (${rahuPos.sign.displayName}), ")
            append("Ketu in house ${ketuPos.house} (${ketuPos.sign.displayName}). ")
            append("${factors.size} karmic factors analyzed.")
        }

        return KarmicWeight(
            totalScore = score,
            intensity = intensity,
            factors = factors,
            doshas = doshas,
            summary = summary
        )
    }

    // =========================================================================
    // 3. BHRIGU BINDU TRANSIT TRACKER
    // =========================================================================

    /**
     * Track significant transits over the Bhrigu Bindu.
     *
     * For slow-moving planets (Jupiter ~12 years, Saturn ~29.5 years, Rahu ~18.6 years),
     * calculates when they will form exact aspects (conjunction, opposition, trine, square)
     * to the natal Bhrigu Bindu within the next 5 years.
     */
    private fun trackBhriguBinduTransits(
        chart: VedicChart,
        bbLongitude: Double,
        currentDate: LocalDate,
        transitPositions: List<PlanetPosition>?
    ): List<BhriguBinduTransitEvent> {
        val events = mutableListOf<BhriguBinduTransitEvent>()
        val bbNorm = VedicAstrologyUtils.normalizeLongitude(bbLongitude)

        // Aspect points relative to BB
        val aspectTargets = mapOf(
            BBTransitEventType.CONJUNCTION to 0.0,
            BBTransitEventType.OPPOSITION to 180.0,
            BBTransitEventType.TRINE to 120.0,
            BBTransitEventType.SQUARE to 90.0,
            BBTransitEventType.SEXTILE to 60.0
        )

        // Slow-moving planets with their approximate daily motion (degrees/day)
        val slowPlanets = mapOf(
            Planet.JUPITER to 0.0833,    // ~30 degrees/year, ~0.083 deg/day
            Planet.SATURN to 0.0335,     // ~12 degrees/year, ~0.034 deg/day
            Planet.RAHU to -0.0530       // ~-19.3 degrees/year (retrograde mean), ~-0.053 deg/day
        )

        for ((planet, dailyMotion) in slowPlanets) {
            // Get starting transit longitude
            val startLong = if (transitPositions != null) {
                transitPositions.find { it.planet == planet }?.longitude
                    ?: chart.planetPositions.find { it.planet == planet }?.longitude
                    ?: continue
            } else {
                // Estimate current position from natal + elapsed time
                val natalPos = chart.planetPositions.find { it.planet == planet } ?: continue
                val daysSinceBirth = ChronoUnit.DAYS.between(
                    chart.birthData.dateTime.toLocalDate(),
                    currentDate
                )
                VedicAstrologyUtils.normalizeLongitude(natalPos.longitude + dailyMotion * daysSinceBirth)
            }

            // Scan forward ~5 years (1826 days) in steps of 1 day
            val scanDays = 1826
            val orb = 1.0 // Degree orb for exact aspect

            for ((aspectType, aspectOffset) in aspectTargets) {
                // Two aspect points: BB + offset, and BB - offset (for trine, etc.)
                val targetPoints = mutableListOf(
                    VedicAstrologyUtils.normalizeLongitude(bbNorm + aspectOffset)
                )
                if (aspectOffset > 0.0 && aspectOffset < 180.0) {
                    targetPoints.add(VedicAstrologyUtils.normalizeLongitude(bbNorm - aspectOffset))
                }

                for (targetPoint in targetPoints) {
                    // Find first crossing within scan window
                    var prevDist = angularDistanceSigned(startLong, targetPoint)
                    for (day in 1..scanDays) {
                        val transitLong = VedicAstrologyUtils.normalizeLongitude(startLong + dailyMotion * day)
                        val currentDist = angularDistanceSigned(transitLong, targetPoint)

                        // Check for sign change in distance (crossing the target)
                        if ((prevDist > 0 && currentDist <= 0) || (prevDist < 0 && currentDist >= 0)) {
                            val actualDist = abs(currentDist)
                            if (actualDist <= orb) {
                                val eventDate = currentDate.plusDays(day.toLong())
                                val significance = classifyBBTransitSignificance(planet, aspectType)
                                val interpretation = interpretBBTransit(planet, aspectType, bbNorm, chart)

                                events.add(BhriguBinduTransitEvent(
                                    transitPlanet = planet,
                                    eventType = aspectType,
                                    estimatedDate = eventDate,
                                    transitLongitude = transitLong,
                                    bhriguBinduLongitude = bbNorm,
                                    orbDegrees = actualDist,
                                    significance = significance,
                                    interpretation = interpretation
                                ))
                                break // Only first occurrence per aspect type per planet
                            }
                        }
                        prevDist = currentDist
                    }
                }
            }
        }

        return events.sortedBy { it.estimatedDate }
    }

    /**
     * Signed angular distance: how far target is from current in the forward direction.
     */
    private fun angularDistanceSigned(current: Double, target: Double): Double {
        val diff = VedicAstrologyUtils.normalizeLongitude(target - current)
        return if (diff > 180.0) diff - 360.0 else diff
    }

    /**
     * Classify the spiritual/karmic significance of a BB transit.
     */
    private fun classifyBBTransitSignificance(
        planet: Planet,
        aspectType: BBTransitEventType
    ): BBTransitSignificance {
        return when (planet) {
            Planet.JUPITER -> when (aspectType) {
                BBTransitEventType.CONJUNCTION -> BBTransitSignificance.KARMIC_REWARD
                BBTransitEventType.TRINE -> BBTransitSignificance.KARMIC_REWARD
                BBTransitEventType.SEXTILE -> BBTransitSignificance.MINOR_ACTIVATION
                BBTransitEventType.OPPOSITION -> BBTransitSignificance.KARMIC_RELEASE
                BBTransitEventType.SQUARE -> BBTransitSignificance.MINOR_ACTIVATION
            }
            Planet.SATURN -> when (aspectType) {
                BBTransitEventType.CONJUNCTION -> BBTransitSignificance.KARMIC_LESSON
                BBTransitEventType.OPPOSITION -> BBTransitSignificance.KARMIC_LESSON
                BBTransitEventType.SQUARE -> BBTransitSignificance.KARMIC_LESSON
                BBTransitEventType.TRINE -> BBTransitSignificance.KARMIC_RELEASE
                BBTransitEventType.SEXTILE -> BBTransitSignificance.MINOR_ACTIVATION
            }
            Planet.RAHU -> when (aspectType) {
                BBTransitEventType.CONJUNCTION -> BBTransitSignificance.KARMIC_ACCELERATION
                BBTransitEventType.OPPOSITION -> BBTransitSignificance.KARMIC_ACCELERATION
                BBTransitEventType.TRINE -> BBTransitSignificance.MINOR_ACTIVATION
                BBTransitEventType.SQUARE -> BBTransitSignificance.KARMIC_ACCELERATION
                BBTransitEventType.SEXTILE -> BBTransitSignificance.MINOR_ACTIVATION
            }
            else -> BBTransitSignificance.MINOR_ACTIVATION
        }
    }

    /**
     * Generate interpretive text for a BB transit event.
     */
    private fun interpretBBTransit(
        planet: Planet,
        aspectType: BBTransitEventType,
        bbLongitude: Double,
        chart: VedicChart
    ): String {
        val bbSign = ZodiacSign.fromLongitude(bbLongitude)
        val bbHouse = VedicAstrologyUtils.calculateWholeSignHouse(bbLongitude, chart.ascendant)
        val aspectName = aspectType.name.lowercase().replace('_', ' ')

        return when (planet) {
            Planet.JUPITER -> when (aspectType) {
                BBTransitEventType.CONJUNCTION -> "Jupiter's transit over the Bhrigu Bindu in ${bbSign.displayName} (house $bbHouse) activates karmic rewards. " +
                        "Expect expansion in areas governed by house $bbHouse. Wisdom, generosity, and fortunate opportunities manifest. " +
                        "Past positive karma ripens during this period."
                BBTransitEventType.TRINE -> "Jupiter's trine to the Bhrigu Bindu brings harmonious karmic fruition. " +
                        "Spiritual growth, educational advancement, and prosperity flow naturally."
                BBTransitEventType.OPPOSITION -> "Jupiter opposes the Bhrigu Bindu, bringing karmic balance through perspective. " +
                        "Others may serve as mirrors for karmic lessons. Relationships and partnerships carry karmic significance."
                BBTransitEventType.SQUARE -> "Jupiter squares the Bhrigu Bindu, creating growth through constructive tension. " +
                        "Overconfidence must be checked; karmic lessons arrive through excess."
                BBTransitEventType.SEXTILE -> "Jupiter's sextile to the Bhrigu Bindu offers gentle karmic support and opportunities for growth."
            }
            Planet.SATURN -> when (aspectType) {
                BBTransitEventType.CONJUNCTION -> "Saturn's transit over the Bhrigu Bindu in ${bbSign.displayName} (house $bbHouse) brings a major karmic test. " +
                        "Responsibilities increase, delays may occur, but perseverance yields lasting results. " +
                        "This is a period of karmic maturation and accountability."
                BBTransitEventType.OPPOSITION -> "Saturn opposes the Bhrigu Bindu, demanding balance between duty and desire. " +
                        "External pressures force confrontation with unresolved karma."
                BBTransitEventType.SQUARE -> "Saturn squares the Bhrigu Bindu, creating friction that demands karmic resolution. " +
                        "Structural changes in life are necessary; resistance increases suffering."
                BBTransitEventType.TRINE -> "Saturn's trine to the Bhrigu Bindu brings disciplined karmic progress. " +
                        "Patient effort in house $bbHouse matters yields solid, lasting results."
                BBTransitEventType.SEXTILE -> "Saturn's sextile offers steady karmic support through methodical effort and discipline."
            }
            Planet.RAHU -> when (aspectType) {
                BBTransitEventType.CONJUNCTION -> "Rahu's transit over the Bhrigu Bindu in ${bbSign.displayName} (house $bbHouse) creates intense karmic acceleration. " +
                        "Obsessive desires surface, unconventional paths open, and destiny takes unexpected turns. " +
                        "This is a watershed karmic moment demanding conscious awareness."
                BBTransitEventType.OPPOSITION -> "Rahu opposes the Bhrigu Bindu, creating a karmic crossroads. " +
                        "Illusions vs. reality in relationships and partnerships. Ketu simultaneously activates the BB by conjunction."
                BBTransitEventType.SQUARE -> "Rahu squares the Bhrigu Bindu, creating restless karmic urgency. " +
                        "Impulsive actions may create new karma; mindfulness is essential."
                BBTransitEventType.TRINE -> "Rahu's trine to the Bhrigu Bindu opens unconventional karmic opportunities. " +
                        "Foreign connections, technology, and innovation feature prominently."
                BBTransitEventType.SEXTILE -> "Rahu's sextile offers subtle karmic openings through unconventional or foreign channels."
            }
            else -> "$planet forms $aspectName to the Bhrigu Bindu at ${String.format("%.2f", bbLongitude)} degrees."
        }
    }

    // =========================================================================
    // 4. KARMIC THEME IDENTIFICATION
    // =========================================================================

    /**
     * Identify the primary karmic theme from the Rahu-Ketu house axis.
     *
     * The Rahu-Ketu axis always spans two opposite houses, and the theme reflects
     * the fundamental polarity the soul is working to integrate in this lifetime.
     */
    private fun identifyKarmicTheme(rahuHouse: Int, ketuHouse: Int): KarmicTheme {
        val pair = setOf(rahuHouse, ketuHouse)

        return when {
            pair == setOf(1, 7) -> KarmicTheme(
                axisHouses = rahuHouse to ketuHouse,
                themeName = "Self vs Partnership",
                themeDescription = "The karmic axis spans the houses of identity (1st) and relationships (7th). " +
                        "This lifetime's central lesson revolves around balancing individual identity with the needs of partnership.",
                rahuGrowthArea = if (rahuHouse == 1) "Developing authentic selfhood, independence, and personal initiative"
                else "Learning to cooperate, compromise, and see through others' eyes",
                ketuMasteredArea = if (ketuHouse == 7) "Natural relationship skills and diplomacy from past lives"
                else "Strong past-life individuality and self-reliance",
                balancingAdvice = "Neither complete self-absorption nor total self-sacrifice serves the soul's evolution. " +
                        "The path lies in maintaining identity while genuinely connecting with others.",
                lifeLesson = "True strength includes vulnerability in relationships; true partnership includes individual wholeness."
            )
            pair == setOf(2, 8) -> KarmicTheme(
                axisHouses = rahuHouse to ketuHouse,
                themeName = "Resources vs Transformation",
                themeDescription = "The karmic axis spans the houses of personal values/possessions (2nd) and shared resources/transformation (8th). " +
                        "This lifetime involves deep lessons about material security versus profound transformation.",
                rahuGrowthArea = if (rahuHouse == 2) "Building self-worth, developing personal resources, and establishing values"
                else "Embracing deep transformation, managing shared resources, and exploring mysteries",
                ketuMasteredArea = if (ketuHouse == 8) "Past-life mastery of occult knowledge, inheritance, and crisis management"
                else "Past-life expertise in accumulating wealth and material security",
                balancingAdvice = "Material security must be balanced with willingness to transform. " +
                        "Neither hoarding nor reckless surrender serves growth.",
                lifeLesson = "True wealth includes the capacity for transformation; true transformation preserves essential values."
            )
            pair == setOf(3, 9) -> KarmicTheme(
                axisHouses = rahuHouse to ketuHouse,
                themeName = "Communication vs Higher Wisdom",
                themeDescription = "The karmic axis spans the houses of everyday communication/skills (3rd) and higher learning/philosophy (9th). " +
                        "The soul balances practical knowledge with philosophical/spiritual understanding.",
                rahuGrowthArea = if (rahuHouse == 3) "Developing courage, communication skills, and practical learning"
                else "Pursuing higher education, philosophy, long journeys, and teaching",
                ketuMasteredArea = if (ketuHouse == 9) "Past-life wisdom, spiritual knowledge, and philosophical depth"
                else "Past-life skill in writing, marketing, siblings, and practical affairs",
                balancingAdvice = "Neither intellectual arrogance nor fear of communication serves the soul. " +
                        "Wisdom must be communicated; communication must carry depth.",
                lifeLesson = "True wisdom is accessible and practical; true skill carries philosophical depth."
            )
            pair == setOf(4, 10) -> KarmicTheme(
                axisHouses = rahuHouse to ketuHouse,
                themeName = "Home/Security vs Career/Status",
                themeDescription = "The karmic axis spans the houses of home/inner security (4th) and career/public status (10th). " +
                        "This is one of the most powerful karmic axes, demanding balance between private and public life.",
                rahuGrowthArea = if (rahuHouse == 4) "Creating emotional security, nurturing family, and building roots"
                else "Achieving professional excellence, public recognition, and leadership",
                ketuMasteredArea = if (ketuHouse == 10) "Past-life career mastery and public achievement"
                else "Past-life domestic mastery and emotional intelligence",
                balancingAdvice = "Success at the expense of family creates karmic debt; retreat from duty creates stagnation. " +
                        "The soul must honor both realms.",
                lifeLesson = "True security comes from within; true status comes from authentic service."
            )
            pair == setOf(5, 11) -> KarmicTheme(
                axisHouses = rahuHouse to ketuHouse,
                themeName = "Creativity vs Community/Gains",
                themeDescription = "The karmic axis spans the houses of creative self-expression (5th) and community/group networks (11th). " +
                        "The soul balances individual creativity with group consciousness.",
                rahuGrowthArea = if (rahuHouse == 5) "Developing creative self-expression, romance, and connection with children"
                else "Building community networks, achieving large-scale goals, and humanitarian service",
                ketuMasteredArea = if (ketuHouse == 11) "Past-life mastery of social networks and group leadership"
                else "Past-life creative and romantic expertise",
                balancingAdvice = "Individual brilliance must serve the group; group conformity must not extinguish creative spark.",
                lifeLesson = "True creativity inspires communities; true community celebrates individual gifts."
            )
            pair == setOf(6, 12) -> KarmicTheme(
                axisHouses = rahuHouse to ketuHouse,
                themeName = "Service/Health vs Surrender/Spirituality",
                themeDescription = "The karmic axis spans the houses of daily service/health (6th) and spiritual surrender/liberation (12th). " +
                        "The soul works to balance mundane duty with transcendent awareness.",
                rahuGrowthArea = if (rahuHouse == 6) "Developing discipline, health routines, service orientation, and conflict resolution"
                else "Surrendering ego, developing spiritual practices, embracing solitude, and foreign lands",
                ketuMasteredArea = if (ketuHouse == 12) "Past-life spiritual development, meditation, and transcendence"
                else "Past-life mastery of practical problem-solving and health/service",
                balancingAdvice = "Spiritual bypass avoids necessary worldly work; workaholism avoids necessary spiritual growth.",
                lifeLesson = "True service is spiritual; true spirituality manifests in compassionate service."
            )
            else -> {
                // Non-standard axis (houses not directly opposite in whole-sign)
                KarmicTheme(
                    axisHouses = rahuHouse to ketuHouse,
                    themeName = "Houses $rahuHouse-$ketuHouse Karmic Axis",
                    themeDescription = "Rahu in house $rahuHouse and Ketu in house $ketuHouse create a karmic axis " +
                            "connecting the themes of these houses. This non-standard axis may indicate " +
                            "intercepted signs or a house system where nodes do not fall in exactly opposite houses.",
                    rahuGrowthArea = "Growth through the life areas governed by house $rahuHouse",
                    ketuMasteredArea = "Past-life expertise in areas governed by house $ketuHouse",
                    balancingAdvice = "Integrate the lessons of both houses without overemphasizing either direction.",
                    lifeLesson = "The soul's growth lies in consciously bridging the themes of houses $rahuHouse and $ketuHouse."
                )
            }
        }
    }

    // =========================================================================
    // 5. PAST LIFE INDICATORS (FROM KETU)
    // =========================================================================

    /**
     * Analyze past-life indicators from Ketu's placement.
     *
     * Ketu represents the soul's past-life baggage: expertise, mastery, comfort zones,
     * and also unresolved trauma or patterns that need releasing. Its sign, house,
     * nakshatra, and conjunctions paint a picture of "where the soul has been."
     */
    private fun analyzePastLifeIndicators(
        chart: VedicChart,
        ketuPos: PlanetPosition
    ): PastLifeProfile {
        val nakshatraLord = ketuPos.nakshatra.ruler
        val conjunctPlanets = chart.planetPositions
            .filter { it.planet != Planet.KETU && it.planet != Planet.RAHU && it.sign == ketuPos.sign }
            .map { it.planet }

        val pastLifeExpertise = getKetuSignExpertise(ketuPos.sign)
        val innateAbilities = getKetuHouseAbilities(ketuPos.house)
        val subconsciousPatterns = getKetuNakshatraPatterns(ketuPos.nakshatra)
        val conjunctionInterpretation = interpretKetuConjunctions(conjunctPlanets)

        val overallNarrative = buildString {
            append("In past lives, the soul developed deep expertise in ${pastLifeExpertise.lowercase()}. ")
            append("This manifests as innate abilities in ${innateAbilities.firstOrNull()?.lowercase() ?: "various areas"}. ")
            if (conjunctPlanets.isNotEmpty()) {
                append("The conjunction of ${conjunctPlanets.joinToString { it.displayName }} with Ketu indicates ")
                append("${conjunctionInterpretation.lowercase()}. ")
            }
            append("The nakshatra lord ${nakshatraLord.displayName} (${ketuPos.nakshatra.displayName}) indicates ")
            append("the ruling influence of past-life experiences was ${getNakshatraLordInfluence(nakshatraLord)}. ")
            append("While these past-life skills come naturally, over-reliance on them creates stagnation. ")
            append("The soul must honor its past mastery while moving toward Rahu's growth direction.")
        }

        return PastLifeProfile(
            ketuSign = ketuPos.sign,
            ketuHouse = ketuPos.house,
            ketuNakshatra = ketuPos.nakshatra,
            nakshatraLord = nakshatraLord,
            pastLifeExpertise = pastLifeExpertise,
            innateAbilities = innateAbilities,
            subconciousPatterns = subconsciousPatterns,
            conjunctPlanets = conjunctPlanets,
            conjunctionInterpretation = conjunctionInterpretation,
            overallNarrative = overallNarrative
        )
    }

    private fun getKetuSignExpertise(sign: ZodiacSign): String = when (sign) {
        ZodiacSign.ARIES -> "Warrior energy, leadership in battle, pioneering solo enterprises, physical courage and martial skill"
        ZodiacSign.TAURUS -> "Accumulation of material wealth, sensory refinement, agricultural mastery, preservation of tradition"
        ZodiacSign.GEMINI -> "Communication, writing, commerce, intellectual pursuits, networking, and information management"
        ZodiacSign.CANCER -> "Nurturing, emotional intelligence, domestic management, intuitive healing, and mothering"
        ZodiacSign.LEO -> "Royal authority, performance, creative self-expression, governance, and commanding presence"
        ZodiacSign.VIRGO -> "Analytical precision, health and healing arts, craftsmanship, service, and discrimination"
        ZodiacSign.LIBRA -> "Diplomacy, partnership, aesthetic refinement, law, justice, and social grace"
        ZodiacSign.SCORPIO -> "Occult knowledge, transformation, crisis management, research, and psychological depth"
        ZodiacSign.SAGITTARIUS -> "Teaching, philosophy, religious/spiritual leadership, long-distance travel, and higher learning"
        ZodiacSign.CAPRICORN -> "Administrative authority, structural organization, discipline, mountain-climbing persistence"
        ZodiacSign.AQUARIUS -> "Humanitarian vision, scientific innovation, community building, reform movements"
        ZodiacSign.PISCES -> "Spiritual transcendence, meditative depth, artistic/musical talent, compassionate service, mysticism"
    }

    private fun getKetuHouseAbilities(house: Int): List<String> = when (house) {
        1 -> listOf("Strong self-identity", "Independent nature", "Physical endurance", "Natural leadership presence")
        2 -> listOf("Wealth management", "Eloquent speech", "Family traditions", "Innate knowledge of values")
        3 -> listOf("Communication skills", "Courage in daily life", "Artistic talents", "Sibling harmony")
        4 -> listOf("Emotional intelligence", "Property management", "Academic aptitude", "Inner peace cultivation")
        5 -> listOf("Creative genius", "Speculative intelligence", "Romantic charisma", "Connection with children")
        6 -> listOf("Problem-solving", "Health/healing knowledge", "Competitive skill", "Service orientation")
        7 -> listOf("Partnership skills", "Diplomatic finesse", "Business acumen", "Relationship wisdom")
        8 -> listOf("Occult/mystical knowledge", "Transformation facilitation", "Inheritance management", "Research depth")
        9 -> listOf("Philosophical wisdom", "Teaching ability", "Spiritual practice", "Cross-cultural understanding")
        10 -> listOf("Professional mastery", "Leadership capability", "Public reputation", "Administrative skill")
        11 -> listOf("Network building", "Goal achievement", "Community leadership", "Humanitarian instinct")
        12 -> listOf("Meditation mastery", "Spiritual surrender", "Healing through isolation", "Foreign land connections")
        else -> listOf("Various innate abilities")
    }

    private fun getKetuNakshatraPatterns(nakshatra: Nakshatra): List<String> = when (nakshatra) {
        Nakshatra.ASHWINI -> listOf("Healing instinct", "Speed and urgency patterns", "Pioneering impulse")
        Nakshatra.BHARANI -> listOf("Transformative intensity", "Bearing heavy burdens", "Life-death themes")
        Nakshatra.KRITTIKA -> listOf("Purifying fire", "Critical discernment", "Cutting through illusion")
        Nakshatra.ROHINI -> listOf("Artistic sensibility", "Material appreciation", "Creative fertility")
        Nakshatra.MRIGASHIRA -> listOf("Restless seeking", "Intellectual curiosity", "Spiritual quest")
        Nakshatra.ARDRA -> listOf("Emotional storms", "Intellectual penetration", "Transformative grief")
        Nakshatra.PUNARVASU -> listOf("Renewal capacity", "Optimistic return", "Wisdom through repetition")
        Nakshatra.PUSHYA -> listOf("Nourishing instinct", "Protective nature", "Spiritual foundation")
        Nakshatra.ASHLESHA -> listOf("Serpentine wisdom", "Hidden knowledge", "Psychological depth")
        Nakshatra.MAGHA -> listOf("Ancestral authority", "Royal dignity", "Lineage consciousness")
        Nakshatra.PURVA_PHALGUNI -> listOf("Creative enjoyment", "Social charm", "Artistic pleasure")
        Nakshatra.UTTARA_PHALGUNI -> listOf("Generous patronage", "Social responsibility", "Righteous conduct")
        Nakshatra.HASTA -> listOf("Craftsmanship", "Manual dexterity", "Healing hands")
        Nakshatra.CHITRA -> listOf("Artistic brilliance", "Architectural vision", "Aesthetic perfection")
        Nakshatra.SWATI -> listOf("Independent spirit", "Flexibility and balance", "Trade and commerce")
        Nakshatra.VISHAKHA -> listOf("Determined focus", "Goal-oriented intensity", "Branching success")
        Nakshatra.ANURADHA -> listOf("Devotional depth", "Friendship mastery", "Organizational skill")
        Nakshatra.JYESHTHA -> listOf("Protective authority", "Elder wisdom", "Strategic leadership")
        Nakshatra.MULA -> listOf("Root exploration", "Destructive-creative power", "Philosophical inquiry")
        Nakshatra.PURVA_ASHADHA -> listOf("Invincible conviction", "Water-based purification", "Victory consciousness")
        Nakshatra.UTTARA_ASHADHA -> listOf("Universal victory", "Ethical leadership", "Unstoppable determination")
        Nakshatra.SHRAVANA -> listOf("Listening wisdom", "Learning through hearing", "Connected consciousness")
        Nakshatra.DHANISHTHA -> listOf("Musical/rhythmic talent", "Wealth through skill", "Group coordination")
        Nakshatra.SHATABHISHA -> listOf("Healing through secrecy", "Scientific aptitude", "Independent research")
        Nakshatra.PURVA_BHADRAPADA -> listOf("Fiery transformation", "Ascetic power", "Lightning insight")
        Nakshatra.UTTARA_BHADRAPADA -> listOf("Deep meditation", "Serpent wisdom", "Watery depth and stability")
        Nakshatra.REVATI -> listOf("Nurturing guidance", "Wealth distribution", "Journey completion")
    }

    private fun interpretKetuConjunctions(planets: List<Planet>): String {
        if (planets.isEmpty()) return "No planets conjunct Ketu; past-life patterns are expressed purely through Ketu's sign and house"

        val interpretations = planets.map { planet ->
            when (planet) {
                Planet.SUN -> "deep past-life connection with authority, father figures, and ego identity"
                Planet.MOON -> "profound past-life emotional patterns, mothering instincts, and psychic sensitivity"
                Planet.MARS -> "warrior-like past-life skills, courage, and possibly unresolved aggression"
                Planet.MERCURY -> "past-life intellectual mastery, communication skills, and analytical ability"
                Planet.JUPITER -> "past-life wisdom, teaching experience, and philosophical/religious authority"
                Planet.VENUS -> "past-life artistic talent, relationship expertise, and material refinement"
                Planet.SATURN -> "past-life discipline, karmic debts through suffering, and structural mastery"
                else -> "${planet.displayName} bringing its significations from past lives"
            }
        }
        return interpretations.joinToString("; ")
    }

    // =========================================================================
    // 6. CURRENT LIFE DIRECTION (FROM RAHU)
    // =========================================================================

    /**
     * Analyze current-life direction from Rahu's placement.
     *
     * Rahu represents the soul's evolutionary direction: unfulfilled desires, growth
     * edges, and the areas where effort yields maximum spiritual advancement. Its sign,
     * house, nakshatra, and conjunctions paint a picture of "where the soul is headed."
     */
    private fun analyzeCurrentLifeDirection(
        chart: VedicChart,
        rahuPos: PlanetPosition
    ): CurrentLifeProfile {
        val nakshatraLord = rahuPos.nakshatra.ruler
        val conjunctPlanets = chart.planetPositions
            .filter { it.planet != Planet.RAHU && it.planet != Planet.KETU && it.sign == rahuPos.sign }
            .map { it.planet }

        val growthDirection = getRahuSignGrowth(rahuPos.sign)
        val desires = getRahuHouseDesires(rahuPos.house)
        val challenges = getRahuChallenges(rahuPos.sign, rahuPos.house)
        val conjunctionInterpretation = interpretRahuConjunctions(conjunctPlanets)

        val overallNarrative = buildString {
            append("The soul's current-life growth direction points toward ${growthDirection.lowercase()}. ")
            append("In house ${rahuPos.house}, Rahu drives the desire to ${desires.firstOrNull()?.lowercase() ?: "grow"}. ")
            if (conjunctPlanets.isNotEmpty()) {
                append("The conjunction with ${conjunctPlanets.joinToString { it.displayName }} amplifies ")
                append("${conjunctionInterpretation.lowercase()}. ")
            }
            append("The nakshatra lord ${nakshatraLord.displayName} (${rahuPos.nakshatra.displayName}) guides ")
            append("the current-life trajectory through ${getNakshatraLordInfluence(nakshatraLord)}. ")
            append("While pursuing Rahu's direction feels uncomfortable at first (unlike Ketu's familiar comfort), ")
            append("persisting on this path yields the greatest soul evolution.")
        }

        return CurrentLifeProfile(
            rahuSign = rahuPos.sign,
            rahuHouse = rahuPos.house,
            rahuNakshatra = rahuPos.nakshatra,
            nakshatraLord = nakshatraLord,
            growthDirection = growthDirection,
            desiresToFulfill = desires,
            challengesOnPath = challenges,
            conjunctPlanets = conjunctPlanets,
            conjunctionInterpretation = conjunctionInterpretation,
            overallNarrative = overallNarrative
        )
    }

    private fun getRahuSignGrowth(sign: ZodiacSign): String = when (sign) {
        ZodiacSign.ARIES -> "Developing independence, courage, initiative, and authentic self-expression"
        ZodiacSign.TAURUS -> "Building material stability, sensory appreciation, and patient persistence"
        ZodiacSign.GEMINI -> "Expanding communication skills, intellectual versatility, and social connections"
        ZodiacSign.CANCER -> "Cultivating emotional depth, nurturing instincts, and domestic security"
        ZodiacSign.LEO -> "Embracing creative self-expression, leadership, and personal radiance"
        ZodiacSign.VIRGO -> "Developing analytical precision, service orientation, and practical skills"
        ZodiacSign.LIBRA -> "Learning partnership, diplomacy, aesthetic refinement, and balanced judgment"
        ZodiacSign.SCORPIO -> "Embracing transformation, emotional depth, shared resources, and hidden truths"
        ZodiacSign.SAGITTARIUS -> "Pursuing higher education, philosophy, travel, teaching, and expansive vision"
        ZodiacSign.CAPRICORN -> "Building structures, assuming authority, developing discipline, and achieving worldly mastery"
        ZodiacSign.AQUARIUS -> "Innovating for humanity, building networks, pursuing scientific/social ideals"
        ZodiacSign.PISCES -> "Surrendering to spiritual practice, developing compassion, artistic/mystical transcendence"
    }

    private fun getRahuHouseDesires(house: Int): List<String> = when (house) {
        1 -> listOf("Establish a powerful personal identity", "Become self-reliant and pioneering", "Physical vitality and presence")
        2 -> listOf("Accumulate wealth and resources", "Develop eloquent speech", "Establish family security and values")
        3 -> listOf("Master communication and media", "Demonstrate courage in daily life", "Build meaningful sibling/community bonds")
        4 -> listOf("Create a secure and beautiful home", "Achieve academic excellence", "Develop emotional roots and inner peace")
        5 -> listOf("Express creative genius", "Experience deep romantic love", "Connect meaningfully with children/progeny")
        6 -> listOf("Overcome enemies and obstacles", "Master health and wellness", "Excel through competitive service")
        7 -> listOf("Build transformative partnerships", "Achieve success through collaboration", "Master the art of negotiation and diplomacy")
        8 -> listOf("Explore occult and hidden knowledge", "Manage transformative crises", "Handle shared resources and inheritance")
        9 -> listOf("Attain higher wisdom and spiritual realization", "Travel to distant lands", "Become a teacher or guide")
        10 -> listOf("Achieve professional eminence", "Serve in public leadership", "Build lasting legacy through career")
        11 -> listOf("Build extensive networks and community", "Achieve large-scale goals and aspirations", "Engage in humanitarian endeavors")
        12 -> listOf("Develop spiritual liberation practices", "Thrive in foreign lands or isolation", "Transcend material attachment")
        else -> listOf("Fulfill growth through life's experiences")
    }

    private fun getRahuChallenges(sign: ZodiacSign, house: Int): List<String> {
        val signChallenges = when (sign) {
            ZodiacSign.ARIES -> "Impulsive overreaction and premature action"
            ZodiacSign.TAURUS -> "Excessive materialism and resistance to change"
            ZodiacSign.GEMINI -> "Scattered attention and superficial connections"
            ZodiacSign.CANCER -> "Emotional dependency and fear of abandonment"
            ZodiacSign.LEO -> "Ego inflation and desperate need for attention"
            ZodiacSign.VIRGO -> "Paralyzing perfectionism and excessive criticism"
            ZodiacSign.LIBRA -> "Codependency and loss of self in relationships"
            ZodiacSign.SCORPIO -> "Obsessive control and destructive intensity"
            ZodiacSign.SAGITTARIUS -> "Dogmatic beliefs and restless dissatisfaction"
            ZodiacSign.CAPRICORN -> "Ruthless ambition and emotional coldness"
            ZodiacSign.AQUARIUS -> "Detached rebellion and refusal to connect emotionally"
            ZodiacSign.PISCES -> "Escapism, addiction, and loss of boundaries"
        }
        val houseChallenges = when (house) {
            1 -> "Overidentification with external persona"
            2 -> "Greed and insecurity around money"
            3 -> "Restless anxiety and communication conflicts"
            4 -> "Emotional volatility at home"
            5 -> "Obsession with romance or speculation"
            6 -> "Chronic health anxiety or litigation"
            7 -> "Attracting complicated partnerships"
            8 -> "Fear of loss and transformation"
            9 -> "Spiritual bypassing and guru dependency"
            10 -> "Workaholism and status obsession"
            11 -> "Using people for personal gain"
            12 -> "Isolation, hidden enemies, and self-undoing"
            else -> "Various life challenges"
        }
        return listOf(signChallenges, houseChallenges)
    }

    private fun interpretRahuConjunctions(planets: List<Planet>): String {
        if (planets.isEmpty()) return "No planets conjunct Rahu; the growth direction is purely driven by Rahu's sign and house"

        val interpretations = planets.map { planet ->
            when (planet) {
                Planet.SUN -> "intense desire for authority and recognition, with risk of ego inflation"
                Planet.MOON -> "powerful emotional compulsions and psychic amplification driving the life direction"
                Planet.MARS -> "aggressive ambition and courage on the growth path, with risk of conflict"
                Planet.MERCURY -> "obsessive intellectual pursuit and communication-driven growth"
                Planet.JUPITER -> "expansive spiritual and material ambition, guru-seeking tendencies"
                Planet.VENUS -> "intense desire for luxury, beauty, and relationship experiences"
                Planet.SATURN -> "disciplined but anxious pursuit of worldly achievement and structure"
                else -> "${planet.displayName} amplifying Rahu's desires in its domain"
            }
        }
        return interpretations.joinToString("; ")
    }

    private fun getNakshatraLordInfluence(planet: Planet): String = when (planet) {
        Planet.SUN -> "authority, self-expression, and soul identity"
        Planet.MOON -> "emotional intelligence, intuition, and nurturing"
        Planet.MARS -> "courage, action, and competitive drive"
        Planet.MERCURY -> "intellect, communication, and analytical skill"
        Planet.JUPITER -> "wisdom, teaching, philosophy, and expansion"
        Planet.VENUS -> "beauty, relationships, art, and material comfort"
        Planet.SATURN -> "discipline, patience, karmic duty, and endurance"
        Planet.RAHU -> "amplified worldly desires, unconventional paths, and foreign connections"
        Planet.KETU -> "spiritual detachment, past-life carry-over, and mystical insight"
        else -> "${planet.displayName}'s domain of influence"
    }

    // =========================================================================
    // 7. UNAVOIDABLE KARMA POINTS
    // =========================================================================

    /**
     * Calculate specific sensitive degrees in the chart where karmic manifestation is unavoidable.
     *
     * These include:
     * - The Bhrigu Bindu itself
     * - The Rahu-Ketu exact degree axis
     * - The midpoint of Rahu and its dispositor
     * - The midpoint of Ketu and its dispositor
     * - The prenatal eclipse point (approximate Sun-Rahu proximity)
     */
    private fun calculateUnavoidableKarmaPoints(
        chart: VedicChart,
        rahuPos: PlanetPosition,
        ketuPos: PlanetPosition,
        bbLongitude: Double
    ): List<UnavoidableKarmaPoint> {
        val points = mutableListOf<UnavoidableKarmaPoint>()

        // 1. Bhrigu Bindu
        val bbSign = ZodiacSign.fromLongitude(bbLongitude)
        val bbHouse = VedicAstrologyUtils.calculateWholeSignHouse(bbLongitude, chart.ascendant)
        points.add(UnavoidableKarmaPoint(
            longitude = bbLongitude,
            sign = bbSign,
            house = bbHouse,
            description = "Bhrigu Bindu: the soul's karmic destiny point. Transits over this degree trigger karmic events.",
            source = "Bhrigu Nandi Nadi",
            intensity = 1.0
        ))

        // 2. Rahu exact degree
        points.add(UnavoidableKarmaPoint(
            longitude = rahuPos.longitude,
            sign = rahuPos.sign,
            house = rahuPos.house,
            description = "Rahu's natal degree: the vortex of current-life desires and karmic acceleration.",
            source = "Rahu-Ketu Axis",
            intensity = 0.9
        ))

        // 3. Ketu exact degree
        points.add(UnavoidableKarmaPoint(
            longitude = ketuPos.longitude,
            sign = ketuPos.sign,
            house = ketuPos.house,
            description = "Ketu's natal degree: the release point of past-life karmic residue.",
            source = "Rahu-Ketu Axis",
            intensity = 0.85
        ))

        // 4. Rahu-Dispositor midpoint
        val rahuDispositorPos = chart.planetPositions.find { it.planet == rahuPos.sign.ruler }
        if (rahuDispositorPos != null) {
            val midpoint = calculateMidpoint(rahuPos.longitude, rahuDispositorPos.longitude)
            val midSign = ZodiacSign.fromLongitude(midpoint)
            val midHouse = VedicAstrologyUtils.calculateWholeSignHouse(midpoint, chart.ascendant)
            points.add(UnavoidableKarmaPoint(
                longitude = midpoint,
                sign = midSign,
                house = midHouse,
                description = "Rahu-Dispositor midpoint: where Rahu's ambition meets its channel of expression (${rahuPos.sign.ruler.displayName}).",
                source = "Midpoint Analysis",
                intensity = 0.7
            ))
        }

        // 5. Ketu-Dispositor midpoint
        val ketuDispositorPos = chart.planetPositions.find { it.planet == ketuPos.sign.ruler }
        if (ketuDispositorPos != null) {
            val midpoint = calculateMidpoint(ketuPos.longitude, ketuDispositorPos.longitude)
            val midSign = ZodiacSign.fromLongitude(midpoint)
            val midHouse = VedicAstrologyUtils.calculateWholeSignHouse(midpoint, chart.ascendant)
            points.add(UnavoidableKarmaPoint(
                longitude = midpoint,
                sign = midSign,
                house = midHouse,
                description = "Ketu-Dispositor midpoint: where past-life energy meets its current expression (${ketuPos.sign.ruler.displayName}).",
                source = "Midpoint Analysis",
                intensity = 0.65
            ))
        }

        // 6. Prenatal eclipse approximation: Sun-Rahu proximity
        val sunPos = chart.planetPositions.find { it.planet == Planet.SUN }
        if (sunPos != null) {
            val sunRahuDist = VedicAstrologyUtils.angularDistance(sunPos.longitude, rahuPos.longitude)
            if (sunRahuDist < 18.0) {
                // Near a prenatal solar eclipse
                val eclipsePoint = VedicAstrologyUtils.normalizeLongitude((sunPos.longitude + rahuPos.longitude) / 2.0)
                val eclSign = ZodiacSign.fromLongitude(eclipsePoint)
                val eclHouse = VedicAstrologyUtils.calculateWholeSignHouse(eclipsePoint, chart.ascendant)
                points.add(UnavoidableKarmaPoint(
                    longitude = eclipsePoint,
                    sign = eclSign,
                    house = eclHouse,
                    description = "Prenatal Solar Eclipse Point: a powerful karmic imprint from the eclipse closest to birth.",
                    source = "Eclipse Cycle Analysis",
                    intensity = 0.8
                ))
            }
            val sunKetuDist = VedicAstrologyUtils.angularDistance(sunPos.longitude, ketuPos.longitude)
            if (sunKetuDist < 18.0) {
                val eclipsePoint = VedicAstrologyUtils.normalizeLongitude((sunPos.longitude + ketuPos.longitude) / 2.0)
                val eclSign = ZodiacSign.fromLongitude(eclipsePoint)
                val eclHouse = VedicAstrologyUtils.calculateWholeSignHouse(eclipsePoint, chart.ascendant)
                points.add(UnavoidableKarmaPoint(
                    longitude = eclipsePoint,
                    sign = eclSign,
                    house = eclHouse,
                    description = "Prenatal Solar Eclipse Point (Ketu): shadow-side karmic imprint from birth-adjacent eclipse.",
                    source = "Eclipse Cycle Analysis",
                    intensity = 0.75
                ))
            }
        }

        return points.sortedByDescending { it.intensity }
    }

    /**
     * Calculate the shorter-arc midpoint between two longitudes.
     */
    private fun calculateMidpoint(long1: Double, long2: Double): Double {
        val a = VedicAstrologyUtils.normalizeLongitude(long1)
        val b = VedicAstrologyUtils.normalizeLongitude(long2)
        val diff = abs(a - b)
        return if (diff <= 180.0) {
            VedicAstrologyUtils.normalizeLongitude((a + b) / 2.0)
        } else {
            VedicAstrologyUtils.normalizeLongitude((a + b) / 2.0 + 180.0)
        }
    }

    // =========================================================================
    // 8. TRANSFORMATION WINDOWS
    // =========================================================================

    /**
     * Calculate time windows when karmic transformation is most active.
     *
     * Windows are triggered by:
     * - Saturn transiting over natal Rahu, Ketu, or BB (within 5-degree orb)
     * - Jupiter transiting over natal Rahu, Ketu, or BB
     * - Rahu/Ketu transiting over their own natal positions (nodal return, ~18.6 years)
     * - Transit Rahu/Ketu crossing the natal BB
     */
    private fun calculateTransformationWindows(
        chart: VedicChart,
        bbLongitude: Double,
        currentDate: LocalDate,
        transitPositions: List<PlanetPosition>?
    ): List<TransformationWindow> {
        val windows = mutableListOf<TransformationWindow>()
        val rahuPos = chart.planetPositions.find { it.planet == Planet.RAHU } ?: return windows
        val ketuPos = chart.planetPositions.find { it.planet == Planet.KETU } ?: return windows

        // Sensitive natal points to track
        val sensitivePoints = listOf(
            rahuPos.longitude to "natal Rahu",
            ketuPos.longitude to "natal Ketu",
            bbLongitude to "Bhrigu Bindu"
        )

        // Transit planets and their approximate daily motions
        val transitPlanets = mapOf(
            Planet.SATURN to 0.0335,
            Planet.JUPITER to 0.0833,
            Planet.RAHU to -0.0530
        )

        for ((planet, dailyMotion) in transitPlanets) {
            val startLong = if (transitPositions != null) {
                transitPositions.find { it.planet == planet }?.longitude
                    ?: chart.planetPositions.find { it.planet == planet }?.longitude
                    ?: continue
            } else {
                val natalPos = chart.planetPositions.find { it.planet == planet } ?: continue
                val daysSinceBirth = ChronoUnit.DAYS.between(chart.birthData.dateTime.toLocalDate(), currentDate)
                VedicAstrologyUtils.normalizeLongitude(natalPos.longitude + dailyMotion * daysSinceBirth)
            }

            for ((targetLong, targetName) in sensitivePoints) {
                val targetNorm = VedicAstrologyUtils.normalizeLongitude(targetLong)
                val orb = 5.0 // Degree orb for transformation window
                val scanDays = 1826 // ~5 years

                var windowStart: LocalDate? = null

                for (day in 0..scanDays) {
                    val transitLong = VedicAstrologyUtils.normalizeLongitude(startLong + dailyMotion * day)
                    val dist = VedicAstrologyUtils.angularDistance(transitLong, targetNorm)

                    if (dist <= orb) {
                        if (windowStart == null) {
                            windowStart = currentDate.plusDays(day.toLong())
                        }
                    } else {
                        if (windowStart != null) {
                            val windowEnd = currentDate.plusDays((day - 1).toLong())
                            val intensity = when (planet) {
                                Planet.SATURN -> if (targetName.contains("Rahu") || targetName.contains("Ketu"))
                                    TransformationIntensity.PEAK else TransformationIntensity.HIGH
                                Planet.JUPITER -> TransformationIntensity.MODERATE
                                Planet.RAHU -> TransformationIntensity.HIGH
                                else -> TransformationIntensity.MILD
                            }

                            val description = buildTransformationDescription(planet, targetName)

                            windows.add(TransformationWindow(
                                startDate = windowStart,
                                endDate = windowEnd,
                                triggerPlanet = planet,
                                triggerType = "Conjunction with $targetName",
                                description = description,
                                intensity = intensity
                            ))
                            windowStart = null
                        }
                    }
                }

                // Close any open window at scan boundary
                if (windowStart != null) {
                    windows.add(TransformationWindow(
                        startDate = windowStart,
                        endDate = currentDate.plusDays(scanDays.toLong()),
                        triggerPlanet = planet,
                        triggerType = "Conjunction with $targetName",
                        description = buildTransformationDescription(planet, targetName),
                        intensity = TransformationIntensity.MODERATE
                    ))
                }
            }
        }

        return windows.sortedBy { it.startDate }
    }

    /**
     * Build descriptive text for a transformation window.
     */
    private fun buildTransformationDescription(planet: Planet, targetName: String): String {
        return when (planet) {
            Planet.SATURN -> when {
                targetName.contains("Rahu") -> "Saturn transiting over natal Rahu: a profound period of karmic accountability. " +
                        "Rahu's desires are tested by Saturn's demand for reality. Career changes, " +
                        "delayed ambitions, and structural transformation are common."
                targetName.contains("Ketu") -> "Saturn transiting over natal Ketu: past-life karmic debts demand settlement. " +
                        "Spiritual deepening through hardship, detachment lessons, and ancestral karma activation."
                targetName.contains("Bhrigu") -> "Saturn transiting over Bhrigu Bindu: the most significant karmic maturation point. " +
                        "Major life lessons manifest. Patience, responsibility, and integrity are tested."
                else -> "Saturn activating $targetName: karmic testing and maturation."
            }
            Planet.JUPITER -> when {
                targetName.contains("Rahu") -> "Jupiter transiting over natal Rahu: expansion of Rahu's agenda. " +
                        "Opportunities for growth in Rahu's domain arrive. Teacher/guru figures appear."
                targetName.contains("Ketu") -> "Jupiter transiting over natal Ketu: spiritual wisdom from past-life patterns. " +
                        "Religious/philosophical awakening and past-life skill activation."
                targetName.contains("Bhrigu") -> "Jupiter transiting over Bhrigu Bindu: karmic reward period. " +
                        "Fortune, wisdom, and positive karmic fruition manifest prominently."
                else -> "Jupiter activating $targetName: expansion and grace in karmic matters."
            }
            Planet.RAHU -> when {
                targetName.contains("Rahu") -> "Transit Rahu conjunct natal Rahu (Nodal Return): a major karmic reset every ~18.6 years. " +
                        "Life direction realigns with the soul's evolutionary purpose. Pivotal decisions and destiny-altering events."
                targetName.contains("Ketu") -> "Transit Rahu over natal Ketu (Reverse Nodal Transit): the past is confronted directly. " +
                        "Old patterns demand release as the soul is pulled toward Rahu's growth direction."
                targetName.contains("Bhrigu") -> "Transit Rahu over Bhrigu Bindu: intense karmic acceleration and obsessive drive. " +
                        "Unconventional events, foreign connections, and destiny-altering circumstances."
                else -> "Rahu activating $targetName: amplification and obsessive focus on karmic themes."
            }
            else -> "$planet transiting $targetName: karmic activation."
        }
    }

    // =========================================================================
    // UTILITY
    // =========================================================================

    /**
     * Resolve a timezone string to a ZoneId, falling back to UTC if invalid.
     */
    private fun resolveZoneId(timezone: String?): ZoneId {
        return com.astro.vajra.util.TimezoneSanitizer.resolveZoneIdOrNull(timezone)
            ?: ZoneOffset.UTC
    }
}
