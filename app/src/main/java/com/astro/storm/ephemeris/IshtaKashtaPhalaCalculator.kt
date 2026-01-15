package com.astro.storm.ephemeris

import com.astro.storm.core.common.Language
import com.astro.storm.core.model.Planet
import com.astro.storm.core.model.PlanetPosition
import com.astro.storm.core.model.VedicChart
import com.astro.storm.core.model.ZodiacSign
import java.text.DecimalFormat
import kotlin.math.abs
import kotlin.math.sqrt

/**
 * Ishta Kashta Phala Calculator
 *
 * Calculates the auspicious (Ishta) and inauspicious (Kashta) results each planet
 * will deliver in a Vedic chart based on classical Shadbala principles.
 *
 * Per BPHS (Brihat Parashara Hora Shastra) Chapter 27:
 * - Ishta Phala (Benefic Result): Derived from Chesta Bala and Uccha Bala
 * - Kashta Phala (Malefic Result): Derived from the same factors inversely
 *
 * The formulas are:
 * - Ishta Phala = √(Uccha Bala × Chesta Bala)
 * - Kashta Phala = √((60 - Uccha Bala) × (60 - Chesta Bala))
 *
 * Where maximum value for each Bala is 60 Virupas.
 *
 * A planet with high Ishta Phala and low Kashta Phala gives predominantly good results.
 * A planet with low Ishta Phala and high Kashta Phala gives predominantly adverse results.
 *
 * This calculator also provides:
 * - Subha Phala (Benefic Output): Overall benefic capacity
 * - Ashubha Phala (Malefic Output): Overall malefic capacity
 * - Net Phala: Ishta - Kashta, indicating overall tendency
 * - Life area analysis based on house lordship
 * - Period-based predictions
 */
object IshtaKashtaPhalaCalculator {

    private const val MAX_BALA = 60.0
    private const val VIRUPAS_PER_RUPA = 60.0
    private const val DEGREES_PER_CIRCLE = 360.0
    private const val DEGREES_PER_SIGN = 30.0

    private val SHADBALA_PLANETS = setOf(
        Planet.SUN, Planet.MOON, Planet.MARS,
        Planet.MERCURY, Planet.JUPITER, Planet.VENUS, Planet.SATURN
    )

    private val FORMAT_TWO_DECIMAL = DecimalFormat("0.00")
    private val FORMAT_ONE_DECIMAL = DecimalFormat("0.0")

    /**
     * Result categories based on Net Phala (Ishta - Kashta)
     */
    enum class PhalaCategory(
        val displayName: String,
        val description: String,
        val minNet: Double
    ) {
        HIGHLY_BENEFIC(
            "Highly Benefic",
            "Planet delivers excellent auspicious results with minimal obstacles",
            5.0
        ),
        BENEFIC(
            "Benefic",
            "Planet produces predominantly positive outcomes",
            2.0
        ),
        MODERATELY_BENEFIC(
            "Moderately Benefic",
            "Planet gives more good than adverse results",
            0.5
        ),
        NEUTRAL(
            "Neutral",
            "Planet produces mixed results with balance of good and adverse",
            -0.5
        ),
        MODERATELY_MALEFIC(
            "Moderately Malefic",
            "Planet gives more challenges than benefits",
            -2.0
        ),
        MALEFIC(
            "Malefic",
            "Planet produces predominantly difficult outcomes",
            -5.0
        ),
        HIGHLY_MALEFIC(
            "Highly Malefic",
            "Planet delivers challenging results requiring significant remedial measures",
            Double.NEGATIVE_INFINITY
        );

        companion object {
            fun fromNetPhala(netPhala: Double): PhalaCategory {
                return entries.firstOrNull { netPhala >= it.minNet } ?: HIGHLY_MALEFIC
            }
        }
    }

    /**
     * Life areas affected by planetary results
     */
    enum class LifeArea(
        val displayName: String,
        val houses: List<Int>
    ) {
        SELF_HEALTH("Self & Health", listOf(1)),
        WEALTH_FAMILY("Wealth & Family", listOf(2)),
        COURAGE_SIBLINGS("Courage & Siblings", listOf(3)),
        HOME_MOTHER("Home & Mother", listOf(4)),
        INTELLIGENCE_CHILDREN("Intelligence & Children", listOf(5)),
        ENEMIES_HEALTH("Obstacles & Health Issues", listOf(6)),
        PARTNERSHIP_SPOUSE("Partnership & Spouse", listOf(7)),
        LONGEVITY_TRANSFORMATION("Longevity & Transformation", listOf(8)),
        FORTUNE_DHARMA("Fortune & Dharma", listOf(9)),
        CAREER_STATUS("Career & Status", listOf(10)),
        GAINS_INCOME("Gains & Income", listOf(11)),
        EXPENSES_LIBERATION("Expenses & Liberation", listOf(12))
    }

    /**
     * Planetary Ishta Kashta Phala data for a single planet
     */
    data class PlanetaryPhala(
        val planet: Planet,
        val sign: ZodiacSign,
        val house: Int,
        val ucchaBala: Double,
        val chestaBala: Double,
        val ishtaPhala: Double,
        val kashtaPhala: Double,
        val netPhala: Double,
        val ishtaPercentage: Double,
        val kashtaPercentage: Double,
        val category: PhalaCategory,
        val housesLorded: List<Int>,
        val affectedAreas: List<LifeArea>,
        val isExalted: Boolean,
        val isDebilitated: Boolean,
        val isRetrograde: Boolean,
        val isOwnSign: Boolean,
        val interpretation: String
    ) {
        val isPredomiantlyBenefic: Boolean = netPhala > 0

        fun getFormattedIshta(): String = FORMAT_TWO_DECIMAL.format(ishtaPhala)
        fun getFormattedKashta(): String = FORMAT_TWO_DECIMAL.format(kashtaPhala)
        fun getFormattedNet(): String = FORMAT_TWO_DECIMAL.format(netPhala)
    }

    /**
     * Period-based Phala predictions
     */
    data class PhalaPeriod(
        val planet: Planet,
        val periodName: String,
        val ishtaStrength: Double,
        val kashtaStrength: Double,
        val expectedBenefits: List<String>,
        val expectedChallenges: List<String>,
        val overallTendency: String
    )

    /**
     * Life area impact analysis
     */
    data class LifeAreaImpact(
        val area: LifeArea,
        val beneficScore: Double,
        val maleficScore: Double,
        val netScore: Double,
        val dominantPlanets: List<Planet>,
        val prediction: String
    )

    /**
     * Complete Ishta Kashta Phala analysis result
     */
    data class IshtaKashtaAnalysis(
        val chartId: String,
        val planetaryPhalas: Map<Planet, PlanetaryPhala>,
        val lifeAreaImpacts: List<LifeAreaImpact>,
        val periodPredictions: List<PhalaPeriod>,
        val mostBeneficPlanet: Planet,
        val mostMaleficPlanet: Planet,
        val overallIshtaScore: Double,
        val overallKashtaScore: Double,
        val overallNetScore: Double,
        val overallCategory: PhalaCategory,
        val summary: String,
        val beneficPlanets: List<Planet>,
        val maleficPlanets: List<Planet>,
        val neutralPlanets: List<Planet>,
        val recommendations: List<String>,
        val timestamp: Long = System.currentTimeMillis()
    ) {
        fun getPlanetsByBenefit(): List<PlanetaryPhala> =
            planetaryPhalas.values.sortedByDescending { it.netPhala }

        fun getPlanetsByIshta(): List<PlanetaryPhala> =
            planetaryPhalas.values.sortedByDescending { it.ishtaPhala }

        fun getPlanetsByKashta(): List<PlanetaryPhala> =
            planetaryPhalas.values.sortedByDescending { it.kashtaPhala }
    }

    /**
     * Exaltation degrees for planets (same as in ShadbalaCalculator)
     */
    private object ExaltationData {
        val degrees = mapOf(
            Planet.SUN to 10.0,      // Aries 10°
            Planet.MOON to 33.0,     // Taurus 3°
            Planet.MARS to 298.0,    // Capricorn 28°
            Planet.MERCURY to 165.0, // Virgo 15°
            Planet.JUPITER to 95.0,  // Cancer 5°
            Planet.VENUS to 357.0,   // Pisces 27°
            Planet.SATURN to 200.0   // Libra 20°
        )

        val debilitationDegrees = degrees.mapValues { (_, deg) ->
            (deg + 180.0) % DEGREES_PER_CIRCLE
        }
    }

    /**
     * Calculate complete Ishta Kashta Phala analysis
     */
    fun analyzeIshtaKashtaPhala(chart: VedicChart): IshtaKashtaAnalysis {
        val planetMap = chart.planetPositions.associateBy { it.planet }
        val planetaryPhalas = mutableMapOf<Planet, PlanetaryPhala>()

        // Calculate Ishta Kashta for each planet
        for (position in chart.planetPositions) {
            if (position.planet in SHADBALA_PLANETS) {
                planetaryPhalas[position.planet] = calculatePlanetaryPhala(position, chart)
            }
        }

        require(planetaryPhalas.isNotEmpty()) { "No valid planets found for Ishta Kashta calculation" }

        // Calculate life area impacts
        val lifeAreaImpacts = calculateLifeAreaImpacts(planetaryPhalas, chart)

        // Generate period predictions
        val periodPredictions = generatePeriodPredictions(planetaryPhalas)

        // Find extremes
        val sortedByNet = planetaryPhalas.values.sortedByDescending { it.netPhala }
        val mostBenefic = sortedByNet.first().planet
        val mostMalefic = sortedByNet.last().planet

        // Calculate overall scores
        val overallIshta = planetaryPhalas.values.map { it.ishtaPhala }.average()
        val overallKashta = planetaryPhalas.values.map { it.kashtaPhala }.average()
        val overallNet = overallIshta - overallKashta
        val overallCategory = PhalaCategory.fromNetPhala(overallNet)

        // Categorize planets
        val beneficPlanets = planetaryPhalas.values.filter { it.netPhala > 0.5 }.map { it.planet }
        val maleficPlanets = planetaryPhalas.values.filter { it.netPhala < -0.5 }.map { it.planet }
        val neutralPlanets = planetaryPhalas.values.filter { it.netPhala in -0.5..0.5 }.map { it.planet }

        // Generate summary and recommendations
        val summary = generateSummary(planetaryPhalas, overallCategory, mostBenefic, mostMalefic)
        val recommendations = generateRecommendations(planetaryPhalas)

        return IshtaKashtaAnalysis(
            chartId = generateChartId(chart),
            planetaryPhalas = planetaryPhalas.toMap(),
            lifeAreaImpacts = lifeAreaImpacts,
            periodPredictions = periodPredictions,
            mostBeneficPlanet = mostBenefic,
            mostMaleficPlanet = mostMalefic,
            overallIshtaScore = overallIshta,
            overallKashtaScore = overallKashta,
            overallNetScore = overallNet,
            overallCategory = overallCategory,
            summary = summary,
            beneficPlanets = beneficPlanets,
            maleficPlanets = maleficPlanets,
            neutralPlanets = neutralPlanets,
            recommendations = recommendations
        )
    }

    /**
     * Calculate Ishta Kashta Phala for a single planet
     */
    private fun calculatePlanetaryPhala(
        position: PlanetPosition,
        chart: VedicChart
    ): PlanetaryPhala {
        val planet = position.planet

        // Calculate Uccha Bala (Exaltation Strength)
        val ucchaBala = calculateUcchaBala(position)

        // Calculate Chesta Bala (Motional Strength)
        val chestaBala = calculateChestaBala(position)

        // Calculate Ishta Phala: √(Uccha Bala × Chesta Bala)
        val ishtaPhala = sqrt(ucchaBala * chestaBala)

        // Calculate Kashta Phala: √((60 - Uccha Bala) × (60 - Chesta Bala))
        val kashtaPhala = sqrt((MAX_BALA - ucchaBala) * (MAX_BALA - chestaBala))

        // Net Phala = Ishta - Kashta
        val netPhala = ishtaPhala - kashtaPhala

        // Calculate percentages
        val totalPhala = ishtaPhala + kashtaPhala
        val ishtaPercentage = if (totalPhala > 0) (ishtaPhala / totalPhala) * 100 else 50.0
        val kashtaPercentage = if (totalPhala > 0) (kashtaPhala / totalPhala) * 100 else 50.0

        // Determine category
        val category = PhalaCategory.fromNetPhala(netPhala)

        // Get houses lorded by this planet
        val housesLorded = getHousesLordedBy(planet, chart)

        // Determine affected life areas
        val affectedAreas = housesLorded.mapNotNull { house ->
            LifeArea.entries.find { house in it.houses }
        }

        // Check dignities
        val isExalted = isExalted(planet, position.sign)
        val isDebilitated = isDebilitated(planet, position.sign)
        val isOwnSign = position.sign.ruler == planet

        // Generate interpretation
        val interpretation = generatePlanetInterpretation(
            planet, ucchaBala, chestaBala, ishtaPhala, kashtaPhala,
            category, housesLorded, isExalted, isDebilitated, position.isRetrograde
        )

        return PlanetaryPhala(
            planet = planet,
            sign = position.sign,
            house = position.house,
            ucchaBala = ucchaBala,
            chestaBala = chestaBala,
            ishtaPhala = ishtaPhala,
            kashtaPhala = kashtaPhala,
            netPhala = netPhala,
            ishtaPercentage = ishtaPercentage,
            kashtaPercentage = kashtaPercentage,
            category = category,
            housesLorded = housesLorded,
            affectedAreas = affectedAreas,
            isExalted = isExalted,
            isDebilitated = isDebilitated,
            isRetrograde = position.isRetrograde,
            isOwnSign = isOwnSign,
            interpretation = interpretation
        )
    }

    /**
     * Calculate Uccha Bala (Exaltation Strength)
     * Maximum when planet is at exact exaltation degree, minimum at debilitation
     */
    private fun calculateUcchaBala(position: PlanetPosition): Double {
        val exaltDeg = ExaltationData.degrees[position.planet] ?: return 30.0
        val debilDeg = ExaltationData.debilitationDegrees[position.planet] ?: return 30.0

        var distance = normalizeDegree(position.longitude - debilDeg)
        if (distance > 180.0) distance = DEGREES_PER_CIRCLE - distance

        // Scale to 0-60 Virupas
        return (distance / 180.0) * MAX_BALA
    }

    /**
     * Calculate Chesta Bala (Motional Strength)
     * Based on planetary motion - retrograde planets have higher Chesta Bala
     */
    private fun calculateChestaBala(position: PlanetPosition): Double {
        // Sun and Moon don't have Chesta Bala in the traditional sense
        // For them, we use alternative measures
        if (position.planet == Planet.SUN) {
            // Sun's Chesta Bala based on its position in the zodiac
            // Maximum near Uttarayana (northern solstice), minimum near Dakshinayana
            val longitude = position.longitude
            val distanceFromCapricorn = abs(normalizeDegree(longitude - 270.0))
            return if (distanceFromCapricorn <= 180.0) {
                (180.0 - distanceFromCapricorn) / 180.0 * MAX_BALA
            } else {
                (distanceFromCapricorn - 180.0) / 180.0 * MAX_BALA
            }
        }

        if (position.planet == Planet.MOON) {
            // Moon's Chesta Bala based on speed (Paksha Bala concept)
            // Faster Moon has higher Chesta Bala
            val speed = abs(position.speed)
            val avgMoonSpeed = 13.2 // Average Moon speed in degrees per day
            return ((speed / avgMoonSpeed) * 30.0).coerceIn(0.0, MAX_BALA)
        }

        // For other planets, retrograde = high Chesta Bala
        return when {
            position.isRetrograde -> MAX_BALA // 60 Virupas
            position.speed < 0.01 -> 50.0 // Nearly stationary
            position.speed < 0.5 -> 40.0 // Slow direct
            position.speed < 1.0 -> 30.0 // Moderate direct
            else -> 20.0 // Fast direct
        }
    }

    /**
     * Get houses lorded by a planet based on ascendant sign
     */
    private fun getHousesLordedBy(planet: Planet, chart: VedicChart): List<Int> {
        val ascSign = ZodiacSign.fromLongitude(chart.ascendant)
        val houses = mutableListOf<Int>()

        for (i in 1..12) {
            val houseSign = ZodiacSign.entries[(ascSign.ordinal + i - 1) % 12]
            if (houseSign.ruler == planet) {
                houses.add(i)
            }
        }

        return houses
    }

    /**
     * Calculate impacts on different life areas
     */
    private fun calculateLifeAreaImpacts(
        phalas: Map<Planet, PlanetaryPhala>,
        chart: VedicChart
    ): List<LifeAreaImpact> {
        return LifeArea.entries.map { area ->
            val relevantPlanets = mutableListOf<Planet>()
            var beneficScore = 0.0
            var maleficScore = 0.0

            // Find planets affecting this area
            phalas.values.forEach { phala ->
                if (phala.affectedAreas.contains(area) || phala.house in area.houses) {
                    relevantPlanets.add(phala.planet)
                    beneficScore += phala.ishtaPhala
                    maleficScore += phala.kashtaPhala
                }
            }

            // Also consider planets placed in these houses
            chart.planetPositions.forEach { pos ->
                if (pos.house in area.houses && pos.planet in SHADBALA_PLANETS) {
                    val phala = phalas[pos.planet]
                    if (phala != null && pos.planet !in relevantPlanets) {
                        relevantPlanets.add(pos.planet)
                        beneficScore += phala.ishtaPhala * 0.5
                        maleficScore += phala.kashtaPhala * 0.5
                    }
                }
            }

            val netScore = if (relevantPlanets.isNotEmpty()) {
                (beneficScore - maleficScore) / relevantPlanets.size
            } else 0.0

            val prediction = generateAreaPrediction(area, netScore, relevantPlanets)

            LifeAreaImpact(
                area = area,
                beneficScore = beneficScore,
                maleficScore = maleficScore,
                netScore = netScore,
                dominantPlanets = relevantPlanets,
                prediction = prediction
            )
        }.sortedByDescending { it.netScore }
    }

    /**
     * Generate period-based predictions
     */
    private fun generatePeriodPredictions(phalas: Map<Planet, PlanetaryPhala>): List<PhalaPeriod> {
        return phalas.values.map { phala ->
            val benefits = mutableListOf<String>()
            val challenges = mutableListOf<String>()

            // Generate benefits based on high Ishta
            if (phala.ishtaPhala > 5.0) {
                benefits.add("Strong positive outcomes in ${phala.planet.displayName}'s significations")
                phala.affectedAreas.forEach { area ->
                    benefits.add("Progress in ${area.displayName.lowercase()}")
                }
            }
            if (phala.isExalted) {
                benefits.add("Peak expression of planetary qualities")
            }
            if (phala.isRetrograde && phala.ishtaPhala > 4.0) {
                benefits.add("Internalized wisdom and spiritual growth")
            }

            // Generate challenges based on high Kashta
            if (phala.kashtaPhala > 5.0) {
                challenges.add("Potential obstacles related to ${phala.planet.displayName}'s domains")
                if (phala.isDebilitated) {
                    challenges.add("Need for patience and remedial measures")
                }
            }
            if (phala.netPhala < -2.0) {
                phala.affectedAreas.forEach { area ->
                    challenges.add("Challenges in ${area.displayName.lowercase()}")
                }
            }

            val tendency = when {
                phala.netPhala > 3.0 -> "Highly favorable period with abundant opportunities"
                phala.netPhala > 1.0 -> "Generally positive with steady progress"
                phala.netPhala > -1.0 -> "Mixed results requiring balanced approach"
                phala.netPhala > -3.0 -> "Challenging period requiring patience"
                else -> "Difficult period requiring remedial measures"
            }

            PhalaPeriod(
                planet = phala.planet,
                periodName = "${phala.planet.displayName} Dasha/Antardasha",
                ishtaStrength = phala.ishtaPhala,
                kashtaStrength = phala.kashtaPhala,
                expectedBenefits = benefits.take(3),
                expectedChallenges = challenges.take(3),
                overallTendency = tendency
            )
        }.sortedByDescending { it.ishtaStrength - it.kashtaStrength }
    }

    /**
     * Generate interpretation for a planet
     */
    private fun generatePlanetInterpretation(
        planet: Planet,
        ucchaBala: Double,
        chestaBala: Double,
        ishtaPhala: Double,
        kashtaPhala: Double,
        category: PhalaCategory,
        housesLorded: List<Int>,
        isExalted: Boolean,
        isDebilitated: Boolean,
        isRetrograde: Boolean
    ): String {
        val sb = StringBuilder()

        // Opening statement
        sb.append("${planet.displayName} ")
        when {
            isExalted -> sb.append("is exalted, ")
            isDebilitated -> sb.append("is debilitated, ")
            isRetrograde -> sb.append("is retrograde, ")
        }

        // Ishta Kashta analysis
        when (category) {
            PhalaCategory.HIGHLY_BENEFIC -> {
                sb.append("delivering exceptional benefic results. ")
                sb.append("With Ishta Phala of ${FORMAT_ONE_DECIMAL.format(ishtaPhala)} and minimal Kashta, ")
                sb.append("this planet strongly supports success in its significations. ")
            }
            PhalaCategory.BENEFIC -> {
                sb.append("producing predominantly positive outcomes. ")
                sb.append("The favorable Ishta-Kashta ratio indicates good fortune ")
                sb.append("in matters governed by this planet. ")
            }
            PhalaCategory.MODERATELY_BENEFIC -> {
                sb.append("giving more beneficial than adverse results. ")
                sb.append("While some challenges may arise, overall outcomes are positive. ")
            }
            PhalaCategory.NEUTRAL -> {
                sb.append("producing mixed results. ")
                sb.append("Both opportunities and obstacles are indicated, ")
                sb.append("requiring balanced efforts and patience. ")
            }
            PhalaCategory.MODERATELY_MALEFIC -> {
                sb.append("presenting more challenges than benefits. ")
                sb.append("Remedial measures can help mitigate difficulties. ")
            }
            PhalaCategory.MALEFIC -> {
                sb.append("indicating significant obstacles in its domains. ")
                sb.append("Focus on strengthening this planet through remedies. ")
            }
            PhalaCategory.HIGHLY_MALEFIC -> {
                sb.append("creating notable difficulties requiring attention. ")
                sb.append("Consistent remedial practices are strongly recommended. ")
            }
        }

        // House lordship effects
        if (housesLorded.isNotEmpty()) {
            val houseStr = housesLorded.joinToString(" and ") { "${it}${getOrdinalSuffix(it)}" }
            sb.append("As lord of the $houseStr house, ")
            sb.append("these areas of life are especially influenced by this planet's Phala results.")
        }

        return sb.toString()
    }

    /**
     * Generate area prediction
     */
    private fun generateAreaPrediction(
        area: LifeArea,
        netScore: Double,
        planets: List<Planet>
    ): String {
        if (planets.isEmpty()) return "Minimal planetary influence on this area."

        val planetNames = planets.take(2).joinToString(" and ") { it.displayName }

        return when {
            netScore > 3.0 -> "Highly favorable outcomes expected through $planetNames influence."
            netScore > 1.0 -> "Positive developments indicated with support from $planetNames."
            netScore > -1.0 -> "Mixed results with both opportunities and challenges via $planetNames."
            netScore > -3.0 -> "Some difficulties may arise; $planetNames requires strengthening."
            else -> "Challenging area requiring remedial attention to $planetNames."
        }
    }

    /**
     * Generate overall summary
     */
    private fun generateSummary(
        phalas: Map<Planet, PlanetaryPhala>,
        overallCategory: PhalaCategory,
        mostBenefic: Planet,
        mostMalefic: Planet
    ): String {
        val beneficCount = phalas.values.count { it.netPhala > 0.5 }
        val maleficCount = phalas.values.count { it.netPhala < -0.5 }

        return buildString {
            append("The overall Ishta Kashta analysis reveals a ${overallCategory.displayName.lowercase()} chart disposition. ")
            append("Out of ${phalas.size} planets analyzed, $beneficCount show predominantly benefic results ")
            append("while $maleficCount indicate challenging influences. ")
            append("${mostBenefic.displayName} emerges as the most auspicious planet, ")
            append("capable of delivering excellent results during its periods. ")
            if (mostMalefic != mostBenefic) {
                append("${mostMalefic.displayName} requires attention through remedial measures ")
                append("to mitigate its Kashta Phala effects.")
            }
        }
    }

    /**
     * Generate personalized recommendations
     */
    private fun generateRecommendations(phalas: Map<Planet, PlanetaryPhala>): List<String> {
        val recommendations = mutableListOf<String>()

        // Strengthen most benefic planet
        phalas.values.maxByOrNull { it.netPhala }?.let { best ->
            recommendations.add("Enhance ${best.planet.displayName}'s influence through its gemstone and mantra to maximize positive results")
        }

        // Remedy most malefic planet
        phalas.values.minByOrNull { it.netPhala }?.let { worst ->
            if (worst.netPhala < -1.0) {
                recommendations.add("Perform remedies for ${worst.planet.displayName} such as charity and specific mantras to reduce Kashta effects")
            }
        }

        // Debilitated planets
        phalas.values.filter { it.isDebilitated }.forEach { phala ->
            recommendations.add("${phala.planet.displayName} is debilitated - consider wearing its gemstone after consulting an astrologer")
        }

        // Retrograde benefic planets
        phalas.values.filter { it.isRetrograde && it.netPhala > 1.0 }.forEach { phala ->
            recommendations.add("Leverage ${phala.planet.displayName}'s retrograde strength for internal growth and spiritual practices")
        }

        // General recommendations based on overall disposition
        recommendations.add("Focus on periods of benefic planets for important initiatives and decisions")
        recommendations.add("During challenging planetary periods, maintain patience and practice appropriate remedies")

        return recommendations.distinct().take(6)
    }

    /**
     * Check if planet is exalted
     */
    private fun isExalted(planet: Planet, sign: ZodiacSign): Boolean {
        return when (planet) {
            Planet.SUN -> sign == ZodiacSign.ARIES
            Planet.MOON -> sign == ZodiacSign.TAURUS
            Planet.MARS -> sign == ZodiacSign.CAPRICORN
            Planet.MERCURY -> sign == ZodiacSign.VIRGO
            Planet.JUPITER -> sign == ZodiacSign.CANCER
            Planet.VENUS -> sign == ZodiacSign.PISCES
            Planet.SATURN -> sign == ZodiacSign.LIBRA
            else -> false
        }
    }

    /**
     * Check if planet is debilitated
     */
    private fun isDebilitated(planet: Planet, sign: ZodiacSign): Boolean {
        return when (planet) {
            Planet.SUN -> sign == ZodiacSign.LIBRA
            Planet.MOON -> sign == ZodiacSign.SCORPIO
            Planet.MARS -> sign == ZodiacSign.CANCER
            Planet.MERCURY -> sign == ZodiacSign.PISCES
            Planet.JUPITER -> sign == ZodiacSign.CAPRICORN
            Planet.VENUS -> sign == ZodiacSign.VIRGO
            Planet.SATURN -> sign == ZodiacSign.ARIES
            else -> false
        }
    }

    /**
     * Normalize degree to 0-360 range
     */
    private fun normalizeDegree(degree: Double): Double {
        var result = degree % DEGREES_PER_CIRCLE
        if (result < 0) result += DEGREES_PER_CIRCLE
        return result
    }

    /**
     * Get ordinal suffix for house numbers
     */
    private fun getOrdinalSuffix(number: Int): String {
        return when {
            number in 11..13 -> "th"
            number % 10 == 1 -> "st"
            number % 10 == 2 -> "nd"
            number % 10 == 3 -> "rd"
            else -> "th"
        }
    }

    /**
     * Generate stable chart ID
     */
    private fun generateChartId(chart: VedicChart): String {
        val birthData = chart.birthData
        return "${birthData.name}-${birthData.dateTime}-${birthData.latitude}-${birthData.longitude}"
            .replace(Regex("[^a-zA-Z0-9-]"), "_")
    }
}
