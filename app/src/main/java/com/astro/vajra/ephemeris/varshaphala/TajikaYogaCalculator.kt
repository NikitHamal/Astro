package com.astro.vajra.ephemeris.varshaphala

import com.astro.vajra.core.model.Planet
import com.astro.vajra.core.model.PlanetPosition
import com.astro.vajra.core.model.VedicChart
import com.astro.vajra.core.model.ZodiacSign
import com.astro.vajra.ephemeris.VedicAstrologyUtils
import kotlin.math.abs

/**
 * TajikaYogaCalculator - Comprehensive Tajik (Varshaphala) Yoga Analysis
 *
 * Tajik astrology is an Indo-Persian system primarily used in Varshaphala (annual horoscopy).
 * It employs unique yogas (combinations) that are different from Parashari yogas.
 *
 * The 16 Tajik Yogas (Shodashayogas):
 * 1. Ikbala - Success and progress
 * 2. Induwara - Royal favor and authority
 * 3. Ithasala - Application (separating/applying)
 * 4. Isarpha - Separation after application
 * 5. Nakta - Transfer of light
 * 6. Yamaya - Prohibition and obstruction
 * 7. Manaoo - Frustration before completion
 * 8. Kamboola - Mutual reception and exchange
 * 9. Gairikamboola - Imperfect exchange
 * 10. Khallaasara - Loss after gain
 * 11. Radda - Return and reversal
 * 12. Dhuphali Kuttha - Double transfer
 * 13. Dutthota - Distant aspect connection
 * 14. Tambira - Copper-like (weak) yoga
 * 15. Kuttha - Transfer through intermediary
 * 16. Durupha - Two-fold support
 *
 * Key Tajik Concepts:
 * - Orb: Degrees within which planets are considered in aspect
 * - Hadda: Term rulership (essential dignity)
 * - Muthassil: Applying planet
 * - Mansur: Separating planet
 *
 * Vedic References:
 * - Tajik Neelakanthi
 * - Varshaphala Paddhati
 * - Tajik Tantrasara
 *
 * @author AstroVajra
 * @since 2026.02
 */
object TajikaYogaCalculator {

    /**
     * Standard orb values for Tajik aspects (in degrees)
     */
    private val PLANET_ORBS = mapOf(
        Planet.SUN to 15.0,
        Planet.MOON to 12.0,
        Planet.MARS to 8.0,
        Planet.MERCURY to 7.0,
        Planet.JUPITER to 9.0,
        Planet.VENUS to 7.0,
        Planet.SATURN to 9.0,
        Planet.RAHU to 6.0,
        Planet.KETU to 6.0
    )

    /**
     * Tajik Aspect types (Drishtis)
     */
    enum class TajikaAspect(
        val degreeSeparation: Int,
        val strength: Double,
        val nature: AspectNature,
        val displayName: String
    ) {
        CONJUNCTION(0, 1.0, AspectNature.STRONG, "Conjunction (0°)"),
        SEXTILE(60, 0.5, AspectNature.HARMONIOUS, "Sextile (60°)"),
        SQUARE(90, 0.75, AspectNature.TENSE, "Square (90°)"),
        TRINE(120, 0.75, AspectNature.HARMONIOUS, "Trine (120°)"),
        OPPOSITION(180, 1.0, AspectNature.TENSE, "Opposition (180°)")
    }

    enum class AspectNature {
        STRONG, HARMONIOUS, TENSE
    }

    /**
     * Application state in aspect
     */
    enum class ApplicationState(val displayName: String) {
        APPLYING("Applying - Effect will manifest"),
        SEPARATING("Separating - Effect is diminishing"),
        EXACT("Exact - Peak effect"),
        NO_ASPECT("No aspect within orb")
    }

    /**
     * Tajik Yoga Type enumeration with all 16 yogas
     */
    enum class TajikaYogaType(
        val displayName: String,
        val sanskritName: String,
        val arabicName: String,
        val nature: YogaNature,
        val strength: Int, // 1-10
        val description: String
    ) {
        IKBALA(
            "Prosperity Yoga",
            "Ikbala",
            "Iqbal",
            YogaNature.BENEFICIAL,
            9,
            "Planets in applying aspect with reception - Success and progress"
        ),
        INDUWARA(
            "Authority Yoga",
            "Induwara",
            "Intihar",
            YogaNature.BENEFICIAL,
            8,
            "Moon applying to a planet in Hadda - Royal favor and recognition"
        ),
        ITHASALA(
            "Application Yoga",
            "Ithasala",
            "Ittisal",
            YogaNature.BENEFICIAL,
            8,
            "Faster planet applying to slower planet - Matters will progress"
        ),
        ISARPHA(
            "Separation Yoga",
            "Isarpha",
            "Insaraf",
            YogaNature.NEUTRAL,
            5,
            "Planets separating after aspect - Matters already peaked"
        ),
        NAKTA(
            "Transfer Yoga",
            "Nakta",
            "Naql",
            YogaNature.BENEFICIAL,
            7,
            "Light transferred through faster planet - Help from intermediary"
        ),
        YAMAYA(
            "Prohibition Yoga",
            "Yamaya",
            "Yamana",
            YogaNature.MALEFIC,
            4,
            "Third planet intervenes to block - Obstruction to matters"
        ),
        MANAOO(
            "Frustration Yoga",
            "Manaoo",
            "Mani",
            YogaNature.MALEFIC,
            3,
            "Planet becomes retrograde before perfection - Incomplete results"
        ),
        KAMBOOLA(
            "Exchange Yoga",
            "Kamboola",
            "Kabul",
            YogaNature.HIGHLY_BENEFICIAL,
            10,
            "Mutual reception with aspect - Excellent results, strong support"
        ),
        GAIRIKAMBOOLA(
            "Partial Exchange",
            "Gairikamboola",
            "Ghayr Kabul",
            YogaNature.BENEFICIAL,
            6,
            "Partial reception without full aspect - Moderate support"
        ),
        KHALLAASARA(
            "Loss Yoga",
            "Khallaasara",
            "Khalas",
            YogaNature.MALEFIC,
            2,
            "Gain followed by loss - Initial success turns sour"
        ),
        RADDA(
            "Return Yoga",
            "Radda",
            "Radd",
            YogaNature.NEUTRAL,
            5,
            "Retrograde planet reverses - Matters return or repeat"
        ),
        DHUPHALI_KUTTHA(
            "Double Transfer",
            "Dhuphali Kuttha",
            "Dufa'li",
            YogaNature.BENEFICIAL,
            7,
            "Transfer through two planets - Multiple intermediaries"
        ),
        DUTTHOTA(
            "Distant Aspect",
            "Dutthota",
            "Budood",
            YogaNature.NEUTRAL,
            4,
            "Wide orb aspect - Weak connection, delayed results"
        ),
        TAMBIRA(
            "Weak Yoga",
            "Tambira",
            "Tambir",
            YogaNature.WEAK,
            2,
            "Copper-like yoga - Very weak results"
        ),
        KUTTHA(
            "Single Transfer",
            "Kuttha",
            "Qat",
            YogaNature.BENEFICIAL,
            6,
            "Transfer through single planet - Help from one person"
        ),
        DURUPHA(
            "Support Yoga",
            "Durupha",
            "Daraf",
            YogaNature.BENEFICIAL,
            7,
            "Two-fold support - Assistance from two sources"
        )
    }

    enum class YogaNature {
        HIGHLY_BENEFICIAL, BENEFICIAL, NEUTRAL, WEAK, MALEFIC
    }

    /**
     * Individual Tajik Yoga detection result
     */
    data class TajikaYoga(
        val yogaType: TajikaYogaType,
        val primaryPlanet: Planet,
        val secondaryPlanet: Planet,
        val tertiaryPlanet: Planet?, // For Nakta, Kuttha, etc.
        val aspect: TajikaAspect,
        val applicationState: ApplicationState,
        val orb: Double,
        val strength: Double,
        val affectedHouses: List<Int>,
        val interpretation: String,
        val effects: List<String>,
        val timing: String
    )

    /**
     * Hadda (Term) rulership for dignity assessment
     */
    data class HaddaRulership(
        val sign: ZodiacSign,
        val degreeRanges: List<Pair<IntRange, Planet>>
    )

    /**
     * Complete Tajik Yoga analysis result
     */
    data class TajikaYogaAnalysis(
        val chartId: String,
        val yearLord: Planet,
        val muntha: ZodiacSign,
        val munthaLord: Planet,
        val yogasFound: List<TajikaYoga>,
        val beneficYogas: List<TajikaYoga>,
        val maleficYogas: List<TajikaYoga>,
        val strengthRanking: List<Pair<TajikaYogaType, Double>>,
        val overallYearStrength: Double,
        val keyInsights: List<String>,
        val monthlyTrends: List<MonthlyTrend>,
        val recommendations: List<String>
    )

    /**
     * Monthly trend based on Tajik analysis
     */
    data class MonthlyTrend(
        val month: Int,
        val dominantPlanet: Planet,
        val trendNature: TrendNature,
        val activeYogas: List<TajikaYogaType>,
        val advice: String
    )

    enum class TrendNature(val displayName: String) {
        EXCELLENT("Excellent"),
        GOOD("Good"),
        AVERAGE("Average"),
        CHALLENGING("Challenging"),
        DIFFICULT("Difficult")
    }

    /**
     * Calculate all Tajik Yogas for a Varshaphala chart
     *
     * @param varshaphalaChart The solar return (annual) chart
     * @param rasiChart The birth chart for reference
     * @return Complete TajikaYogaAnalysis
     */
    fun calculateTajikaYogas(
        varshaphalaChart: VedicChart,
        rasiChart: VedicChart
    ): TajikaYogaAnalysis {
        val yogasFound = mutableListOf<TajikaYoga>()

        // Calculate Year Lord (Varshesha)
        val yearLord = calculateYearLord(varshaphalaChart)

        // Calculate Muntha
        val muntha = calculateMuntha(varshaphalaChart, rasiChart)
        val munthaLord = muntha.ruler

        // Check each pair of planets for Tajik yogas
        val planets = listOf(
            Planet.SUN, Planet.MOON, Planet.MARS, Planet.MERCURY,
            Planet.JUPITER, Planet.VENUS, Planet.SATURN
        )

        for (i in planets.indices) {
            for (j in (i + 1) until planets.size) {
                val planet1 = planets[i]
                val planet2 = planets[j]

                val pos1 = varshaphalaChart.planetPositions.find { it.planet == planet1 }
                val pos2 = varshaphalaChart.planetPositions.find { it.planet == planet2 }

                if (pos1 != null && pos2 != null) {
                    // Check for each type of Tajik yoga
                    checkIthasalaYoga(pos1, pos2, varshaphalaChart)?.let { yogasFound.add(it) }
                    checkIsarphaYoga(pos1, pos2, varshaphalaChart)?.let { yogasFound.add(it) }
                    checkKamboolaYoga(pos1, pos2, varshaphalaChart)?.let { yogasFound.add(it) }
                    checkIkbalaYoga(pos1, pos2, varshaphalaChart)?.let { yogasFound.add(it) }
                    checkIndumalaYoga(pos1, pos2, varshaphalaChart)?.let { yogasFound.add(it) }
                }
            }
        }

        // Check for three-planet yogas
        for (i in planets.indices) {
            for (j in (i + 1) until planets.size) {
                for (k in (j + 1) until planets.size) {
                    val pos1 = varshaphalaChart.planetPositions.find { it.planet == planets[i] }
                    val pos2 = varshaphalaChart.planetPositions.find { it.planet == planets[j] }
                    val pos3 = varshaphalaChart.planetPositions.find { it.planet == planets[k] }

                    if (pos1 != null && pos2 != null && pos3 != null) {
                        checkNaktaYoga(pos1, pos2, pos3, varshaphalaChart)?.let { yogasFound.add(it) }
                        checkYamayaYoga(pos1, pos2, pos3, varshaphalaChart)?.let { yogasFound.add(it) }
                        checkKutthaYoga(pos1, pos2, pos3, varshaphalaChart)?.let { yogasFound.add(it) }
                    }
                }
            }
        }

        // Check retrograde-based yogas
        checkManaooYoga(varshaphalaChart)?.let { yogasFound.addAll(it) }
        checkRaddaYoga(varshaphalaChart)?.let { yogasFound.addAll(it) }

        // Categorize yogas
        val beneficYogas = yogasFound.filter { it.yogaType.nature in listOf(YogaNature.HIGHLY_BENEFICIAL, YogaNature.BENEFICIAL) }
        val maleficYogas = yogasFound.filter { it.yogaType.nature == YogaNature.MALEFIC }

        // Rank yogas by strength
        val strengthRanking = yogasFound
            .groupBy { it.yogaType }
            .map { (type, yogas) -> type to yogas.maxOf { it.strength } }
            .sortedByDescending { it.second }

        // Calculate overall year strength
        val overallStrength = calculateOverallYearStrength(yogasFound, yearLord, munthaLord, varshaphalaChart)

        // Generate insights
        val insights = generateInsights(yogasFound, yearLord, muntha, munthaLord)

        // Calculate monthly trends
        val monthlyTrends = calculateMonthlyTrends(yogasFound, varshaphalaChart)

        // Generate recommendations
        val recommendations = generateRecommendations(yogasFound, maleficYogas, yearLord)

        return TajikaYogaAnalysis(
            chartId = generateChartId(varshaphalaChart),
            yearLord = yearLord,
            muntha = muntha,
            munthaLord = munthaLord,
            yogasFound = yogasFound,
            beneficYogas = beneficYogas,
            maleficYogas = maleficYogas,
            strengthRanking = strengthRanking,
            overallYearStrength = overallStrength,
            keyInsights = insights,
            monthlyTrends = monthlyTrends,
            recommendations = recommendations
        )
    }

    // ============================================
    // YOGA DETECTION METHODS
    // ============================================

    /**
     * Check for Ithasala Yoga (Application)
     *
     * Ithasala occurs when a faster planet applies to a slower planet
     * within the combined orb and the aspect is not yet perfected.
     */
    private fun checkIthasalaYoga(
        pos1: PlanetPosition,
        pos2: PlanetPosition,
        chart: VedicChart
    ): TajikaYoga? {
        val aspect = getAspectBetweenPlanets(pos1.longitude, pos2.longitude) ?: return null
        val orb = calculateCombinedOrb(pos1.planet, pos2.planet)
        val separation = getAspectSeparation(pos1.longitude, pos2.longitude, aspect)

        if (separation > orb) return null

        // Determine which planet is faster (Moon > Mercury > Venus > Sun > Mars > Jupiter > Saturn)
        val speed1 = getPlanetarySpeed(pos1.planet)
        val speed2 = getPlanetarySpeed(pos2.planet)

        val fasterPlanet = if (speed1 > speed2) pos1 else pos2
        val slowerPlanet = if (speed1 > speed2) pos2 else pos1

        // Check if faster planet is applying (moving toward exact aspect)
        val isApplying = isApplyingAspect(fasterPlanet, slowerPlanet, aspect)

        if (!isApplying) return null

        val affectedHouses = listOf(pos1.house, pos2.house).distinct()

        return TajikaYoga(
            yogaType = TajikaYogaType.ITHASALA,
            primaryPlanet = fasterPlanet.planet,
            secondaryPlanet = slowerPlanet.planet,
            tertiaryPlanet = null,
            aspect = aspect,
            applicationState = ApplicationState.APPLYING,
            orb = separation,
            strength = calculateYogaStrength(TajikaYogaType.ITHASALA, separation, orb, aspect),
            affectedHouses = affectedHouses,
            interpretation = buildIthasalaInterpretation(fasterPlanet.planet, slowerPlanet.planet, aspect, affectedHouses),
            effects = getIthasalaEffects(fasterPlanet.planet, slowerPlanet.planet, affectedHouses),
            timing = "Results manifest as aspect perfects"
        )
    }

    /**
     * Check for Isarpha Yoga (Separation)
     *
     * Isarpha occurs when planets are separating after an exact aspect.
     */
    private fun checkIsarphaYoga(
        pos1: PlanetPosition,
        pos2: PlanetPosition,
        chart: VedicChart
    ): TajikaYoga? {
        val aspect = getAspectBetweenPlanets(pos1.longitude, pos2.longitude) ?: return null
        val orb = calculateCombinedOrb(pos1.planet, pos2.planet)
        val separation = getAspectSeparation(pos1.longitude, pos2.longitude, aspect)

        if (separation > orb) return null

        val speed1 = getPlanetarySpeed(pos1.planet)
        val speed2 = getPlanetarySpeed(pos2.planet)

        val fasterPlanet = if (speed1 > speed2) pos1 else pos2
        val slowerPlanet = if (speed1 > speed2) pos2 else pos1

        // Check if separating
        val isApplying = isApplyingAspect(fasterPlanet, slowerPlanet, aspect)

        if (isApplying) return null // Not Isarpha if applying

        val affectedHouses = listOf(pos1.house, pos2.house).distinct()

        return TajikaYoga(
            yogaType = TajikaYogaType.ISARPHA,
            primaryPlanet = fasterPlanet.planet,
            secondaryPlanet = slowerPlanet.planet,
            tertiaryPlanet = null,
            aspect = aspect,
            applicationState = ApplicationState.SEPARATING,
            orb = separation,
            strength = calculateYogaStrength(TajikaYogaType.ISARPHA, separation, orb, aspect),
            affectedHouses = affectedHouses,
            interpretation = "Isarpha between ${fasterPlanet.planet.displayName} and ${slowerPlanet.planet.displayName}. " +
                    "Matters related to houses ${affectedHouses.joinToString(", ")} have already peaked or passed.",
            effects = listOf("Past opportunities", "Diminishing influence", "Concluding matters"),
            timing = "Results already manifested or passing"
        )
    }

    /**
     * Check for Kamboola Yoga (Mutual Reception with Aspect)
     *
     * Kamboola is the most powerful Tajik yoga - occurs when two planets
     * are in mutual reception (each in the other's sign) AND in aspect.
     */
    private fun checkKamboolaYoga(
        pos1: PlanetPosition,
        pos2: PlanetPosition,
        chart: VedicChart
    ): TajikaYoga? {
        // Check for mutual reception
        val planet1InPlanet2Sign = pos1.sign.ruler == pos2.planet
        val planet2InPlanet1Sign = pos2.sign.ruler == pos1.planet

        if (!planet1InPlanet2Sign || !planet2InPlanet1Sign) return null

        // Check for aspect between them
        val aspect = getAspectBetweenPlanets(pos1.longitude, pos2.longitude)
        val orb = calculateCombinedOrb(pos1.planet, pos2.planet)

        if (aspect == null) {
            // No aspect but mutual reception - Gairikamboola
            val affectedHouses = listOf(pos1.house, pos2.house).distinct()
            return TajikaYoga(
                yogaType = TajikaYogaType.GAIRIKAMBOOLA,
                primaryPlanet = pos1.planet,
                secondaryPlanet = pos2.planet,
                tertiaryPlanet = null,
                aspect = TajikaAspect.CONJUNCTION, // Placeholder
                applicationState = ApplicationState.NO_ASPECT,
                orb = 0.0,
                strength = 60.0,
                affectedHouses = affectedHouses,
                interpretation = "Gairikamboola (partial exchange) between ${pos1.planet.displayName} and ${pos2.planet.displayName}. " +
                        "Mutual reception without direct aspect provides moderate support.",
                effects = listOf("Moderate support", "Indirect assistance", "Partial success"),
                timing = "Throughout the year with moderate intensity"
            )
        }

        val separation = getAspectSeparation(pos1.longitude, pos2.longitude, aspect)
        if (separation > orb) return null

        val affectedHouses = listOf(pos1.house, pos2.house).distinct()

        return TajikaYoga(
            yogaType = TajikaYogaType.KAMBOOLA,
            primaryPlanet = pos1.planet,
            secondaryPlanet = pos2.planet,
            tertiaryPlanet = null,
            aspect = aspect,
            applicationState = ApplicationState.EXACT,
            orb = separation,
            strength = 95.0, // Kamboola is extremely strong
            affectedHouses = affectedHouses,
            interpretation = "Kamboola (mutual reception with aspect) between ${pos1.planet.displayName} and ${pos2.planet.displayName}. " +
                    "This is the most powerful Tajik yoga, promising excellent results for houses ${affectedHouses.joinToString(", ")}.",
            effects = listOf(
                "Excellent success",
                "Strong mutual support",
                "Fulfillment of desires",
                "Help from influential persons"
            ),
            timing = "Results throughout the year, peak when aspect is exact by transit"
        )
    }

    /**
     * Check for Ikbala Yoga (Prosperity through Reception)
     *
     * Ikbala occurs when applying aspect has reception (one planet in the sign of the other).
     */
    private fun checkIkbalaYoga(
        pos1: PlanetPosition,
        pos2: PlanetPosition,
        chart: VedicChart
    ): TajikaYoga? {
        val aspect = getAspectBetweenPlanets(pos1.longitude, pos2.longitude) ?: return null
        val orb = calculateCombinedOrb(pos1.planet, pos2.planet)
        val separation = getAspectSeparation(pos1.longitude, pos2.longitude, aspect)

        if (separation > orb) return null

        // Check for single reception
        val hasReception = pos1.sign.ruler == pos2.planet || pos2.sign.ruler == pos1.planet
        if (!hasReception) return null

        // Must be applying
        val speed1 = getPlanetarySpeed(pos1.planet)
        val speed2 = getPlanetarySpeed(pos2.planet)
        val fasterPlanet = if (speed1 > speed2) pos1 else pos2
        val slowerPlanet = if (speed1 > speed2) pos2 else pos1

        if (!isApplyingAspect(fasterPlanet, slowerPlanet, aspect)) return null

        val affectedHouses = listOf(pos1.house, pos2.house).distinct()

        return TajikaYoga(
            yogaType = TajikaYogaType.IKBALA,
            primaryPlanet = fasterPlanet.planet,
            secondaryPlanet = slowerPlanet.planet,
            tertiaryPlanet = null,
            aspect = aspect,
            applicationState = ApplicationState.APPLYING,
            orb = separation,
            strength = 85.0,
            affectedHouses = affectedHouses,
            interpretation = "Ikbala Yoga between ${fasterPlanet.planet.displayName} and ${slowerPlanet.planet.displayName}. " +
                    "Application with reception promises prosperity and progress in matters of houses ${affectedHouses.joinToString(", ")}.",
            effects = listOf("Progress and prosperity", "Success with assistance", "Favorable outcomes"),
            timing = "Results as aspect perfects"
        )
    }

    /**
     * Check for Indumala Yoga (Moon applying to planet in Hadda)
     */
    private fun checkIndumalaYoga(
        pos1: PlanetPosition,
        pos2: PlanetPosition,
        chart: VedicChart
    ): TajikaYoga? {
        val moonPos = if (pos1.planet == Planet.MOON) pos1 else if (pos2.planet == Planet.MOON) pos2 else return null
        val otherPos = if (pos1.planet == Planet.MOON) pos2 else pos1

        val aspect = getAspectBetweenPlanets(moonPos.longitude, otherPos.longitude) ?: return null
        val orb = calculateCombinedOrb(moonPos.planet, otherPos.planet)
        val separation = getAspectSeparation(moonPos.longitude, otherPos.longitude, aspect)

        if (separation > orb) return null

        // Check if Moon is applying
        if (!isApplyingAspect(moonPos, otherPos, aspect)) return null

        // Check if other planet is in its own Hadda
        val isInHadda = isInOwnHadda(otherPos)
        if (!isInHadda) return null

        val affectedHouses = listOf(moonPos.house, otherPos.house).distinct()

        return TajikaYoga(
            yogaType = TajikaYogaType.INDUWARA,
            primaryPlanet = Planet.MOON,
            secondaryPlanet = otherPos.planet,
            tertiaryPlanet = null,
            aspect = aspect,
            applicationState = ApplicationState.APPLYING,
            orb = separation,
            strength = 80.0,
            affectedHouses = affectedHouses,
            interpretation = "Induwara Yoga: Moon applying to ${otherPos.planet.displayName} in its Hadda. " +
                    "Promises royal favor, authority, and recognition.",
            effects = listOf("Government favor", "Authority increase", "Public recognition"),
            timing = "When Moon perfects the aspect"
        )
    }

    /**
     * Check for Nakta Yoga (Transfer of Light)
     *
     * Nakta occurs when a faster planet separates from one and applies to another,
     * transferring the light between two planets not in aspect.
     */
    private fun checkNaktaYoga(
        pos1: PlanetPosition,
        pos2: PlanetPosition,
        pos3: PlanetPosition,
        chart: VedicChart
    ): TajikaYoga? {
        // Find the fastest planet to act as transferor
        val speeds = listOf(pos1, pos2, pos3).sortedByDescending { getPlanetarySpeed(it.planet) }
        val fastest = speeds[0]
        val middle = speeds[1]
        val slowest = speeds[2]

        // Check if fastest is separating from one and applying to another
        val aspectToMiddle = getAspectBetweenPlanets(fastest.longitude, middle.longitude)
        val aspectToSlowest = getAspectBetweenPlanets(fastest.longitude, slowest.longitude)

        if (aspectToMiddle == null || aspectToSlowest == null) return null

        val sepMiddle = getAspectSeparation(fastest.longitude, middle.longitude, aspectToMiddle)
        val sepSlowest = getAspectSeparation(fastest.longitude, slowest.longitude, aspectToSlowest)

        val orbMiddle = calculateCombinedOrb(fastest.planet, middle.planet)
        val orbSlowest = calculateCombinedOrb(fastest.planet, slowest.planet)

        if (sepMiddle > orbMiddle || sepSlowest > orbSlowest) return null

        // Fastest should be separating from middle and applying to slowest
        val isSeparatingFromMiddle = !isApplyingAspect(fastest, middle, aspectToMiddle)
        val isApplyingToSlowest = isApplyingAspect(fastest, slowest, aspectToSlowest)

        if (!isSeparatingFromMiddle || !isApplyingToSlowest) return null

        // Check that middle and slowest are not in direct aspect
        val directAspect = getAspectBetweenPlanets(middle.longitude, slowest.longitude)
        if (directAspect != null) {
            val directSep = getAspectSeparation(middle.longitude, slowest.longitude, directAspect)
            if (directSep <= calculateCombinedOrb(middle.planet, slowest.planet)) return null
        }

        val affectedHouses = listOf(pos1.house, pos2.house, pos3.house).distinct()

        return TajikaYoga(
            yogaType = TajikaYogaType.NAKTA,
            primaryPlanet = fastest.planet,
            secondaryPlanet = middle.planet,
            tertiaryPlanet = slowest.planet,
            aspect = aspectToSlowest,
            applicationState = ApplicationState.APPLYING,
            orb = sepSlowest,
            strength = 75.0,
            affectedHouses = affectedHouses,
            interpretation = "Nakta (Transfer of Light): ${fastest.planet.displayName} transfers light from ${middle.planet.displayName} to ${slowest.planet.displayName}. " +
                    "Matters will be accomplished through an intermediary.",
            effects = listOf("Success through mediator", "Third party assistance", "Indirect achievement"),
            timing = "Results when faster planet perfects aspect to slower"
        )
    }

    /**
     * Check for Yamaya Yoga (Prohibition)
     *
     * Yamaya occurs when a third planet interferes and blocks an application.
     */
    private fun checkYamayaYoga(
        pos1: PlanetPosition,
        pos2: PlanetPosition,
        pos3: PlanetPosition,
        chart: VedicChart
    ): TajikaYoga? {
        // Check all combinations
        val combinations = listOf(
            Triple(pos1, pos2, pos3),
            Triple(pos1, pos3, pos2),
            Triple(pos2, pos3, pos1)
        )

        for ((applying, receiving, blocking) in combinations) {
            val aspect = getAspectBetweenPlanets(applying.longitude, receiving.longitude) ?: continue
            val blockingAspect = getAspectBetweenPlanets(blocking.longitude, receiving.longitude) ?: continue

            // Blocking planet should be between the applying and receiving
            val speed1 = getPlanetarySpeed(applying.planet)
            val speedBlock = getPlanetarySpeed(blocking.planet)

            // Blocking planet must be faster than applying and also applying
            if (speedBlock <= speed1) continue

            val sepToReceiving = getAspectSeparation(applying.longitude, receiving.longitude, aspect)
            val blockSepToReceiving = getAspectSeparation(blocking.longitude, receiving.longitude, blockingAspect)

            // Blocking should perfect before applying
            if (blockSepToReceiving >= sepToReceiving) continue

            val affectedHouses = listOf(pos1.house, pos2.house, pos3.house).distinct()

            return TajikaYoga(
                yogaType = TajikaYogaType.YAMAYA,
                primaryPlanet = applying.planet,
                secondaryPlanet = receiving.planet,
                tertiaryPlanet = blocking.planet,
                aspect = aspect,
                applicationState = ApplicationState.APPLYING,
                orb = sepToReceiving,
                strength = 40.0,
                affectedHouses = affectedHouses,
                interpretation = "Yamaya (Prohibition): ${blocking.planet.displayName} blocks the connection between ${applying.planet.displayName} and ${receiving.planet.displayName}. " +
                        "Matters face obstruction or interference.",
                effects = listOf("Obstruction to plans", "Third party interference", "Blocked outcomes"),
                timing = "Obstruction occurs as blocking planet perfects aspect first"
            )
        }

        return null
    }

    /**
     * Check for Kuttha Yoga (Transfer through Intermediary)
     */
    private fun checkKutthaYoga(
        pos1: PlanetPosition,
        pos2: PlanetPosition,
        pos3: PlanetPosition,
        chart: VedicChart
    ): TajikaYoga? {
        // Similar to Nakta but with different conditions
        // Simplified implementation - checks for chain of aspects
        val chain = findAspectChain(listOf(pos1, pos2, pos3))

        if (chain.isEmpty()) return null

        val affectedHouses = chain.map { it.house }.distinct()

        return TajikaYoga(
            yogaType = TajikaYogaType.KUTTHA,
            primaryPlanet = chain[0].planet,
            secondaryPlanet = chain[1].planet,
            tertiaryPlanet = if (chain.size > 2) chain[2].planet else null,
            aspect = TajikaAspect.CONJUNCTION,
            applicationState = ApplicationState.APPLYING,
            orb = 0.0,
            strength = 65.0,
            affectedHouses = affectedHouses,
            interpretation = "Kuttha: Transfer of influence through ${chain[1].planet.displayName}.",
            effects = listOf("Help from intermediary", "Indirect assistance"),
            timing = "As chain of aspects perfect"
        )
    }

    /**
     * Check for Manaoo Yoga (Frustration due to Retrograde)
     */
    private fun checkManaooYoga(chart: VedicChart): List<TajikaYoga>? {
        val yogas = mutableListOf<TajikaYoga>()

        val retrogradePlanets = chart.planetPositions.filter { it.isRetrograde }

        for (retroPos in retrogradePlanets) {
            // Check if this retrograde planet was in applying aspect
            for (otherPos in chart.planetPositions) {
                if (otherPos.planet == retroPos.planet) continue

                val aspect = getAspectBetweenPlanets(retroPos.longitude, otherPos.longitude) ?: continue
                val orb = calculateCombinedOrb(retroPos.planet, otherPos.planet)
                val separation = getAspectSeparation(retroPos.longitude, otherPos.longitude, aspect)

                if (separation <= orb) {
                    yogas.add(
                        TajikaYoga(
                            yogaType = TajikaYogaType.MANAOO,
                            primaryPlanet = retroPos.planet,
                            secondaryPlanet = otherPos.planet,
                            tertiaryPlanet = null,
                            aspect = aspect,
                            applicationState = ApplicationState.APPLYING,
                            orb = separation,
                            strength = 30.0,
                            affectedHouses = listOf(retroPos.house, otherPos.house).distinct(),
                            interpretation = "Manaoo: ${retroPos.planet.displayName} retrograde prevents completion of aspect to ${otherPos.planet.displayName}. " +
                                    "Matters may not reach fruition.",
                            effects = listOf("Incomplete results", "Frustration", "Delays"),
                            timing = "Until retrograde planet turns direct"
                        )
                    )
                }
            }
        }

        return if (yogas.isNotEmpty()) yogas else null
    }

    /**
     * Check for Radda Yoga (Return due to Retrograde)
     */
    private fun checkRaddaYoga(chart: VedicChart): List<TajikaYoga>? {
        val yogas = mutableListOf<TajikaYoga>()

        val retrogradePlanets = chart.planetPositions.filter { it.isRetrograde }

        for (retroPos in retrogradePlanets) {
            yogas.add(
                TajikaYoga(
                    yogaType = TajikaYogaType.RADDA,
                    primaryPlanet = retroPos.planet,
                    secondaryPlanet = retroPos.planet, // Same planet
                    tertiaryPlanet = null,
                    aspect = TajikaAspect.CONJUNCTION,
                    applicationState = ApplicationState.SEPARATING,
                    orb = 0.0,
                    strength = 50.0,
                    affectedHouses = listOf(retroPos.house),
                    interpretation = "Radda: ${retroPos.planet.displayName} retrograde indicates return or repetition of matters related to house ${retroPos.house}.",
                    effects = listOf("Return of past matters", "Repetition", "Reconsideration"),
                    timing = "Throughout retrograde period"
                )
            )
        }

        return if (yogas.isNotEmpty()) yogas else null
    }

    // ============================================
    // HELPER METHODS
    // ============================================

    private fun getAspectBetweenPlanets(long1: Double, long2: Double): TajikaAspect? {
        val diff = abs(VedicAstrologyUtils.normalizeDegree(long1 - long2))
        val normalizedDiff = if (diff > 180) 360 - diff else diff

        return TajikaAspect.entries.find { aspect ->
            val orb = 12.0 // General orb for aspect detection
            abs(normalizedDiff - aspect.degreeSeparation) <= orb
        }
    }

    private fun getAspectSeparation(long1: Double, long2: Double, aspect: TajikaAspect): Double {
        val diff = abs(VedicAstrologyUtils.normalizeDegree(long1 - long2))
        val normalizedDiff = if (diff > 180) 360 - diff else diff
        return abs(normalizedDiff - aspect.degreeSeparation)
    }

    private fun calculateCombinedOrb(planet1: Planet, planet2: Planet): Double {
        val orb1 = PLANET_ORBS[planet1] ?: 7.0
        val orb2 = PLANET_ORBS[planet2] ?: 7.0
        return (orb1 + orb2) / 2
    }

    private fun getPlanetarySpeed(planet: Planet): Double {
        return when (planet) {
            Planet.MOON -> 13.0
            Planet.MERCURY -> 4.0
            Planet.VENUS -> 1.6
            Planet.SUN -> 1.0
            Planet.MARS -> 0.5
            Planet.JUPITER -> 0.08
            Planet.SATURN -> 0.03
            else -> 0.0
        }
    }

    private fun isApplyingAspect(faster: PlanetPosition, slower: PlanetPosition, aspect: TajikaAspect): Boolean {
        // Simplified: check if faster planet's longitude is before the exact aspect point
        val exactAspectLong = VedicAstrologyUtils.normalizeDegree(slower.longitude + aspect.degreeSeparation)
        val diff = VedicAstrologyUtils.normalizeDegree(faster.longitude - slower.longitude)

        return when (aspect) {
            TajikaAspect.CONJUNCTION -> diff < 180 && diff > 0
            else -> diff < aspect.degreeSeparation
        }
    }

    private fun isInOwnHadda(pos: PlanetPosition): Boolean {
        // Simplified Hadda implementation
        val degreeInSign = pos.longitude % 30

        // Traditional Hadda (Egyptian terms)
        val haddaRulers = getHaddaRuler(pos.sign, degreeInSign)
        return haddaRulers == pos.planet
    }

    private fun getHaddaRuler(sign: ZodiacSign, degree: Double): Planet {
        // Simplified - using Egyptian terms
        val termBoundaries = when (sign) {
            ZodiacSign.ARIES -> listOf(6 to Planet.JUPITER, 12 to Planet.VENUS, 20 to Planet.MERCURY, 25 to Planet.MARS, 30 to Planet.SATURN)
            ZodiacSign.TAURUS -> listOf(8 to Planet.VENUS, 14 to Planet.MERCURY, 22 to Planet.JUPITER, 27 to Planet.SATURN, 30 to Planet.MARS)
            ZodiacSign.GEMINI -> listOf(6 to Planet.MERCURY, 12 to Planet.JUPITER, 17 to Planet.VENUS, 24 to Planet.MARS, 30 to Planet.SATURN)
            ZodiacSign.CANCER -> listOf(7 to Planet.MARS, 13 to Planet.VENUS, 19 to Planet.MERCURY, 26 to Planet.JUPITER, 30 to Planet.SATURN)
            ZodiacSign.LEO -> listOf(6 to Planet.JUPITER, 11 to Planet.VENUS, 18 to Planet.SATURN, 24 to Planet.MERCURY, 30 to Planet.MARS)
            ZodiacSign.VIRGO -> listOf(7 to Planet.MERCURY, 17 to Planet.VENUS, 21 to Planet.JUPITER, 28 to Planet.MARS, 30 to Planet.SATURN)
            ZodiacSign.LIBRA -> listOf(6 to Planet.SATURN, 14 to Planet.MERCURY, 21 to Planet.JUPITER, 28 to Planet.VENUS, 30 to Planet.MARS)
            ZodiacSign.SCORPIO -> listOf(7 to Planet.MARS, 11 to Planet.VENUS, 19 to Planet.MERCURY, 24 to Planet.JUPITER, 30 to Planet.SATURN)
            ZodiacSign.SAGITTARIUS -> listOf(12 to Planet.JUPITER, 17 to Planet.VENUS, 21 to Planet.MERCURY, 26 to Planet.SATURN, 30 to Planet.MARS)
            ZodiacSign.CAPRICORN -> listOf(7 to Planet.MERCURY, 14 to Planet.JUPITER, 22 to Planet.VENUS, 26 to Planet.SATURN, 30 to Planet.MARS)
            ZodiacSign.AQUARIUS -> listOf(7 to Planet.MERCURY, 13 to Planet.VENUS, 20 to Planet.JUPITER, 25 to Planet.MARS, 30 to Planet.SATURN)
            ZodiacSign.PISCES -> listOf(12 to Planet.VENUS, 16 to Planet.JUPITER, 19 to Planet.MERCURY, 28 to Planet.MARS, 30 to Planet.SATURN)
        }

        val normalizedDegree = degree.coerceIn(0.0, 30.0)
        return termBoundaries.firstOrNull { normalizedDegree <= it.first }?.second
            ?: termBoundaries.last().second
    }

    private fun findAspectChain(positions: List<PlanetPosition>): List<PlanetPosition> {
        // Simplified chain finding
        val sorted = positions.sortedByDescending { getPlanetarySpeed(it.planet) }
        return sorted.take(2)
    }

    private fun calculateYogaStrength(
        yogaType: TajikaYogaType,
        separation: Double,
        orb: Double,
        aspect: TajikaAspect
    ): Double {
        val baseStrength = yogaType.strength * 10.0
        val orbFactor = 1.0 - (separation / orb) * 0.3
        val aspectFactor = aspect.strength

        return (baseStrength * orbFactor * aspectFactor).coerceIn(0.0, 100.0)
    }

    private fun calculateYearLord(chart: VedicChart): Planet {
        // Year lord is determined by the lord of the weekday of the solar return
        val sunPos = chart.planetPositions.find { it.planet == Planet.SUN }
        requireNotNull(sunPos) { "Year lord calculation requires Sun position in the varshaphala chart." }
        return sunPos.sign.ruler
    }

    private fun calculateMuntha(varshaphalaChart: VedicChart, rasiChart: VedicChart): ZodiacSign {
        // Muntha advances one sign per year from birth Lagna
        val rasiLagna = VedicAstrologyUtils.getAscendantSign(rasiChart)
        val varshaphalaLagna = VedicAstrologyUtils.getAscendantSign(varshaphalaChart)

        // Simplified - return the sign where Muntha falls
        return ZodiacSign.entries[(rasiLagna.ordinal + 1) % 12]
    }

    private fun calculateOverallYearStrength(
        yogas: List<TajikaYoga>,
        yearLord: Planet,
        munthaLord: Planet,
        chart: VedicChart
    ): Double {
        var strength = 50.0

        // Benefic yogas add strength
        val beneficStrength = yogas
            .filter { it.yogaType.nature in listOf(YogaNature.HIGHLY_BENEFICIAL, YogaNature.BENEFICIAL) }
            .sumOf { it.strength } / 10

        // Malefic yogas reduce strength
        val maleficStrength = yogas
            .filter { it.yogaType.nature == YogaNature.MALEFIC }
            .sumOf { it.strength } / 15

        strength += beneficStrength - maleficStrength

        // Year lord strength
        val yearLordPos = chart.planetPositions.find { it.planet == yearLord }
        yearLordPos?.let {
            val dignity = VedicAstrologyUtils.getDignity(it)
            strength += when (dignity) {
                VedicAstrologyUtils.PlanetaryDignity.EXALTED -> 15.0
                VedicAstrologyUtils.PlanetaryDignity.OWN_SIGN -> 10.0
                VedicAstrologyUtils.PlanetaryDignity.DEBILITATED -> -10.0
                else -> 0.0
            }
        }

        return strength.coerceIn(0.0, 100.0)
    }

    private fun calculateMonthlyTrends(
        yogas: List<TajikaYoga>,
        chart: VedicChart
    ): List<MonthlyTrend> {
        val trends = mutableListOf<MonthlyTrend>()

        // Simplified monthly trend calculation based on Moon's nakshatra cycle
        val moonPos = chart.planetPositions.find { it.planet == Planet.MOON }

        val startIndex = moonPos?.sign?.ordinal ?: 0
        for (month in 1..12) {
            val signIndex = (startIndex + month - 1) % ZodiacSign.entries.size
            val dominantPlanet = ZodiacSign.entries[signIndex].ruler
            val activeYogas = yogas
                .filter { it.primaryPlanet == dominantPlanet || it.secondaryPlanet == dominantPlanet }
                .map { it.yogaType }

            val trendNature = when {
                activeYogas.any { it.nature == YogaNature.HIGHLY_BENEFICIAL } -> TrendNature.EXCELLENT
                activeYogas.any { it.nature == YogaNature.BENEFICIAL } -> TrendNature.GOOD
                activeYogas.any { it.nature == YogaNature.MALEFIC } -> TrendNature.CHALLENGING
                else -> TrendNature.AVERAGE
            }

            trends.add(
                MonthlyTrend(
                    month = month,
                    dominantPlanet = dominantPlanet,
                    trendNature = trendNature,
                    activeYogas = activeYogas,
                    advice = getMonthlyAdvice(trendNature, dominantPlanet)
                )
            )
        }

        return trends
    }

    private fun getMonthlyAdvice(trend: TrendNature, planet: Planet): String {
        return when (trend) {
            TrendNature.EXCELLENT -> "Excellent month for major initiatives under ${planet.displayName}'s influence"
            TrendNature.GOOD -> "Good period for progress in ${planet.displayName}-related matters"
            TrendNature.AVERAGE -> "Average month - maintain steady efforts"
            TrendNature.CHALLENGING -> "Exercise caution in ${planet.displayName}-related activities"
            TrendNature.DIFFICULT -> "Difficult period - focus on remedies and patience"
        }
    }

    // ============================================
    // INTERPRETATION BUILDERS
    // ============================================

    private fun buildIthasalaInterpretation(
        faster: Planet,
        slower: Planet,
        aspect: TajikaAspect,
        houses: List<Int>
    ): String {
        return "Ithasala Yoga: ${faster.displayName} applies to ${slower.displayName} by ${aspect.displayName}. " +
                "Matters related to houses ${houses.joinToString(", ")} will progress and reach completion. " +
                "This is a positive indication for the year."
    }

    private fun getIthasalaEffects(faster: Planet, slower: Planet, houses: List<Int>): List<String> {
        val effects = mutableListOf<String>()

        effects.add("Progress in matters of houses ${houses.joinToString(", ")}")
        effects.add("${faster.displayName} brings energy to ${slower.displayName}'s significations")

        if (VedicAstrologyUtils.isNaturalBenefic(faster) && VedicAstrologyUtils.isNaturalBenefic(slower)) {
            effects.add("Double benefic influence promises excellent results")
        }

        return effects
    }

    private fun generateInsights(
        yogas: List<TajikaYoga>,
        yearLord: Planet,
        muntha: ZodiacSign,
        munthaLord: Planet
    ): List<String> {
        val insights = mutableListOf<String>()

        insights.add("Year Lord: ${yearLord.displayName} - governs overall year fortunes")
        insights.add("Muntha in ${muntha.displayName} - focus area for the year")

        val strongestYoga = yogas.maxByOrNull { it.strength }
        strongestYoga?.let {
            insights.add("Strongest yoga: ${it.yogaType.displayName} (${it.yogaType.sanskritName}) at ${String.format("%.1f", it.strength)}%")
        }

        val kamboolaPresent = yogas.any { it.yogaType == TajikaYogaType.KAMBOOLA }
        if (kamboolaPresent) {
            insights.add("Kamboola yoga present - extremely favorable year for success")
        }

        return insights.take(5)
    }

    private fun generateRecommendations(
        yogas: List<TajikaYoga>,
        maleficYogas: List<TajikaYoga>,
        yearLord: Planet
    ): List<String> {
        val recommendations = mutableListOf<String>()

        recommendations.add("Strengthen ${yearLord.displayName} through appropriate remedies")

        if (maleficYogas.isNotEmpty()) {
            recommendations.add("Address ${maleficYogas.first().yogaType.displayName} through patience and caution")
        }

        val beneficYogas = yogas.filter { it.yogaType.nature in listOf(YogaNature.HIGHLY_BENEFICIAL, YogaNature.BENEFICIAL) }
        if (beneficYogas.isNotEmpty()) {
            recommendations.add("Leverage ${beneficYogas.first().yogaType.displayName} for maximum benefit")
        }

        return recommendations.take(5)
    }

    private fun generateChartId(chart: VedicChart): String {
        val birthData = chart.birthData
        return "TAJIK-${birthData.name}-${birthData.dateTime}".replace(Regex("[^a-zA-Z0-9-]"), "_")
    }

    /**
     * Get summary text for display
     */
    fun getSummary(analysis: TajikaYogaAnalysis): String {
        return buildString {
            appendLine("═══════════════════════════════════════")
            appendLine("TAJIK YOGA ANALYSIS - VARSHAPHALA")
            appendLine("═══════════════════════════════════════")
            appendLine()
            appendLine("Year Lord: ${analysis.yearLord.displayName}")
            appendLine("Muntha: ${analysis.muntha.displayName} (Lord: ${analysis.munthaLord.displayName})")
            appendLine()
            appendLine("Overall Year Strength: ${String.format("%.1f", analysis.overallYearStrength)}%")
            appendLine()
            appendLine("Tajik Yogas Found: ${analysis.yogasFound.size}")
            appendLine("  Benefic: ${analysis.beneficYogas.size}")
            appendLine("  Malefic: ${analysis.maleficYogas.size}")
            appendLine()

            if (analysis.strengthRanking.isNotEmpty()) {
                appendLine("Top Yogas:")
                analysis.strengthRanking.take(3).forEach { (type, strength) ->
                    val nature = if (type.nature in listOf(YogaNature.HIGHLY_BENEFICIAL, YogaNature.BENEFICIAL)) "✓" else "✗"
                    appendLine("  $nature ${type.displayName} (${String.format("%.1f", strength)}%)")
                }
            }

            appendLine()
            appendLine("Key Insights:")
            analysis.keyInsights.take(3).forEach { insight ->
                appendLine("  • $insight")
            }
        }
    }
}
