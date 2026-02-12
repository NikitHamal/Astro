package com.astro.storm.ephemeris

import com.astro.storm.core.model.Planet
import com.astro.storm.core.model.PlanetPosition
import com.astro.storm.core.model.VedicChart
import com.astro.storm.core.model.ZodiacSign
import com.astro.storm.ephemeris.VedicAstrologyUtils.PlanetaryRelationship
import kotlin.math.abs

/**
 * SthanaBalaCalculator - Comprehensive Positional Strength Analysis
 *
 * Sthana Bala (Positional Strength) is the first and largest component of Shadbala.
 * It measures how strong a planet is based on its placement in signs, houses, and
 * divisional charts.
 *
 * Components:
 * 1. Uccha Bala - Exaltation strength (0-60 virupas)
 * 2. Saptavargaja Bala - Strength from 7 divisional charts
 * 3. Ojhayugma Rasyamsa Bala - Odd/Even sign placement
 * 4. Kendradi Bala - Angular house strength
 * 5. Drekkana Bala - Decanate position strength
 *
 * Vedic References:
 * - BPHS (Brihat Parashara Hora Shastra) Chapter 27
 * - Graha Sutras
 */
object SthanaBalaCalculator {

    private const val VIRUPAS_PER_RUPA = 60.0
    private const val DEGREES_PER_CIRCLE = 360.0
    private const val DEGREES_PER_SIGN = 30.0

    /**
     * Planets used in Sthana Bala calculation (7 classical planets)
     */
    private val STHANA_BALA_PLANETS = setOf(
        Planet.SUN, Planet.MOON, Planet.MARS,
        Planet.MERCURY, Planet.JUPITER, Planet.VENUS, Planet.SATURN
    )

    /**
     * Dignity types for classification
     */
    enum class DignityType(val displayName: String, val virupas: Double) {
        EXALTED("Exalted", 20.0),
        MOOLATRIKONA("Moolatrikona", 22.5),
        OWN_SIGN("Own Sign", 30.0),
        FRIEND("Friend's Sign", 15.0),
        NEUTRAL("Neutral Sign", 10.0),
        ENEMY("Enemy's Sign", 7.5),
        DEBILITATED("Debilitated", 0.0)
    }

    /**
     * House type classification
     */
    enum class HouseType(val displayName: String, val virupas: Double) {
        KENDRA("Kendra (Angular)", 60.0),
        PANAPARA("Panapara (Succedent)", 30.0),
        APOKLIMA("Apoklima (Cadent)", 15.0)
    }

    /**
     * Component analysis data
     */
    data class ComponentAnalysis(
        val name: String,
        val virupas: Double,
        val maxVirupas: Double,
        val percentage: Double,
        val interpretation: String
    )

    /**
     * Uccha Bala analysis
     */
    data class UcchaBalaAnalysis(
        val planet: Planet,
        val currentLongitude: Double,
        val exaltationDegree: Double,
        val debilitationDegree: Double,
        val distanceFromDebilitation: Double,
        val virupas: Double,
        val dignityType: DignityType,
        val interpretation: String
    )

    /**
     * Saptavargaja Bala breakdown
     */
    data class SaptavargajaBalaAnalysis(
        val planet: Planet,
        val d1Strength: Double,
        val d2Strength: Double,
        val d3Strength: Double,
        val d7Strength: Double,
        val d9Strength: Double,
        val d12Strength: Double,
        val d30Strength: Double,
        val totalVirupas: Double,
        val interpretation: String
    )

    /**
     * Ojhayugma Bala analysis
     */
    data class OjhayugmaBalaAnalysis(
        val planet: Planet,
        val isOddSign: Boolean,
        val signName: String,
        val preferredOdd: Boolean,
        val virupas: Double,
        val interpretation: String
    )

    /**
     * Kendradi Bala analysis
     */
    data class KendradiBalaAnalysis(
        val planet: Planet,
        val houseNumber: Int,
        val houseType: HouseType,
        val virupas: Double,
        val interpretation: String
    )

    /**
     * Drekkana Bala analysis
     */
    data class DrekkanaBalaAnalysis(
        val planet: Planet,
        val decanate: Int,
        val degreeInSign: Double,
        val preferredDecanate: Int,
        val virupas: Double,
        val interpretation: String
    )

    /**
     * Planet's complete Sthana Bala analysis
     */
    data class PlanetSthanaBala(
        val planet: Planet,
        val ucchaBala: UcchaBalaAnalysis,
        val saptavargajaBala: SaptavargajaBalaAnalysis,
        val ojhayugmaBala: OjhayugmaBalaAnalysis,
        val kendradiBala: KendradiBalaAnalysis,
        val drekkanaBala: DrekkanaBalaAnalysis,
        val totalVirupas: Double,
        val totalRupas: Double,
        val requiredVirupas: Double,
        val percentageOfRequired: Double,
        val strengthRating: String,
        val interpretation: String
    )

    /**
     * Complete Sthana Bala analysis result
     */
    data class SthanaBalaAnalysis(
        val planetarySthanaBala: Map<Planet, PlanetSthanaBala>,
        val componentSummary: List<ComponentAnalysis>,
        val strongestPlanet: Planet,
        val weakestPlanet: Planet,
        val overallScore: Double,
        val keyInsights: List<String>,
        val recommendations: List<String>
    )

    /**
     * Exaltation degrees per BPHS
     */
    private val exaltationDegrees = mapOf(
        Planet.SUN to 10.0,
        Planet.MOON to 33.0,
        Planet.MARS to 298.0,
        Planet.MERCURY to 165.0,
        Planet.JUPITER to 95.0,
        Planet.VENUS to 357.0,
        Planet.SATURN to 200.0
    )

    /**
     * Moolatrikona data per BPHS Chapter 3
     */
    private data class MoolatrikonaRange(val sign: ZodiacSign, val startDegree: Double, val endDegree: Double)

    private val moolatrikonaRanges = mapOf(
        Planet.SUN to MoolatrikonaRange(ZodiacSign.LEO, 0.0, 20.0),
        Planet.MOON to MoolatrikonaRange(ZodiacSign.TAURUS, 3.0, 27.0),
        Planet.MARS to MoolatrikonaRange(ZodiacSign.ARIES, 0.0, 12.0),
        Planet.MERCURY to MoolatrikonaRange(ZodiacSign.VIRGO, 15.0, 20.0),
        Planet.JUPITER to MoolatrikonaRange(ZodiacSign.SAGITTARIUS, 0.0, 10.0),
        Planet.VENUS to MoolatrikonaRange(ZodiacSign.LIBRA, 0.0, 15.0),
        Planet.SATURN to MoolatrikonaRange(ZodiacSign.AQUARIUS, 0.0, 20.0)
    )

    /**
     * Required minimum virupas for each planet (converted from rupas)
     */
    private val requiredVirupas = mapOf(
        Planet.SUN to 165.0,
        Planet.MOON to 133.0,
        Planet.MARS to 96.0,
        Planet.MERCURY to 165.0,
        Planet.JUPITER to 165.0,
        Planet.VENUS to 133.0,
        Planet.SATURN to 96.0
    )

    /**
     * Saptavarga weights
     */
    private object VargaWeights {
        const val D1 = 5.0
        const val D2 = 2.5
        const val D3 = 3.0
        const val D7 = 2.5
        const val D9 = 4.5
        const val D12 = 2.0
        const val D30 = 1.0
    }

    /**
     * Perform complete Sthana Bala analysis
     */
    fun analyzeSthanaBala(chart: VedicChart): SthanaBalaAnalysis {
        val planetMap = chart.planetPositions.associateBy { it.planet }
        val divisionalCharts = DivisionalChartCalculator.calculateAllDivisionalCharts(chart)
        val divisionalMap = divisionalCharts.associateBy { it.chartType }

        val planetarySthanaBala = mutableMapOf<Planet, PlanetSthanaBala>()

        for (position in chart.planetPositions) {
            if (position.planet in STHANA_BALA_PLANETS) {
                planetarySthanaBala[position.planet] = calculatePlanetSthanaBala(
                    position, divisionalMap
                )
            }
        }

        // Calculate component summary
        val componentSummary = calculateComponentSummary(planetarySthanaBala)

        // Find strongest and weakest
        val sortedByStrength = planetarySthanaBala.values.sortedByDescending { it.totalVirupas }
        require(sortedByStrength.isNotEmpty()) { "Sthana Bala calculation requires at least one valid planetary position." }
        val strongestPlanet = sortedByStrength.first().planet
        val weakestPlanet = sortedByStrength.last().planet

        // Calculate overall score
        val overallScore = calculateOverallScore(planetarySthanaBala)

        // Generate insights and recommendations
        val insights = generateInsights(planetarySthanaBala)
        val recommendations = generateRecommendations(planetarySthanaBala)

        return SthanaBalaAnalysis(
            planetarySthanaBala = planetarySthanaBala.toMap(),
            componentSummary = componentSummary,
            strongestPlanet = strongestPlanet,
            weakestPlanet = weakestPlanet,
            overallScore = overallScore,
            keyInsights = insights,
            recommendations = recommendations
        )
    }

    /**
     * Calculate Sthana Bala for a single planet
     */
    private fun calculatePlanetSthanaBala(
        position: PlanetPosition,
        divisionalMap: Map<DivisionalChartType, DivisionalChartData>
    ): PlanetSthanaBala {
        val planet = position.planet

        val ucchaBala = calculateUcchaBala(position)
        val saptavargajaBala = calculateSaptavargajaBala(position, divisionalMap)
        val ojhayugmaBala = calculateOjhayugmaBala(position)
        val kendradiBala = calculateKendradiBala(position)
        val drekkanaBala = calculateDrekkanaBala(position)

        val totalVirupas = ucchaBala.virupas + saptavargajaBala.totalVirupas +
                ojhayugmaBala.virupas + kendradiBala.virupas + drekkanaBala.virupas
        val totalRupas = totalVirupas / VIRUPAS_PER_RUPA
        val required = requiredVirupas[planet] ?: 133.0
        val percentage = (totalVirupas / required) * 100.0

        val rating = when {
            percentage >= 130 -> "Excellent"
            percentage >= 100 -> "Strong"
            percentage >= 80 -> "Average"
            percentage >= 60 -> "Weak"
            else -> "Very Weak"
        }

        return PlanetSthanaBala(
            planet = planet,
            ucchaBala = ucchaBala,
            saptavargajaBala = saptavargajaBala,
            ojhayugmaBala = ojhayugmaBala,
            kendradiBala = kendradiBala,
            drekkanaBala = drekkanaBala,
            totalVirupas = totalVirupas,
            totalRupas = totalRupas,
            requiredVirupas = required,
            percentageOfRequired = percentage,
            strengthRating = rating,
            interpretation = buildPlanetInterpretation(planet, totalVirupas, percentage, ucchaBala.dignityType)
        )
    }

    /**
     * Calculate Uccha Bala (Exaltation strength)
     */
    private fun calculateUcchaBala(position: PlanetPosition): UcchaBalaAnalysis {
        val planet = position.planet
        val exaltDeg = exaltationDegrees[planet] ?: 0.0
        val debilDeg = (exaltDeg + 180.0) % DEGREES_PER_CIRCLE

        var distance = normalizeDegree(position.longitude - debilDeg)
        if (distance > 180.0) distance = DEGREES_PER_CIRCLE - distance

        val virupas = (distance / 180.0) * 60.0
        val dignityType = getDignityType(position)

        return UcchaBalaAnalysis(
            planet = planet,
            currentLongitude = position.longitude,
            exaltationDegree = exaltDeg,
            debilitationDegree = debilDeg,
            distanceFromDebilitation = distance,
            virupas = virupas,
            dignityType = dignityType,
            interpretation = buildUcchaBalaInterpretation(planet, virupas, dignityType)
        )
    }

    /**
     * Calculate Saptavargaja Bala
     */
    private fun calculateSaptavargajaBala(
        position: PlanetPosition,
        divisionalMap: Map<DivisionalChartType, DivisionalChartData>
    ): SaptavargajaBalaAnalysis {
        val planet = position.planet
        val degreeInSign = position.longitude % DEGREES_PER_SIGN

        val d1Strength = getVargaStrength(planet, position.sign, degreeInSign) * VargaWeights.D1
        val d2Strength = getDivisionalStrength(planet, divisionalMap, DivisionalChartType.D2_HORA) * VargaWeights.D2
        val d3Strength = getDivisionalStrength(planet, divisionalMap, DivisionalChartType.D3_DREKKANA) * VargaWeights.D3
        val d7Strength = getDivisionalStrength(planet, divisionalMap, DivisionalChartType.D7_SAPTAMSA) * VargaWeights.D7
        val d9Strength = getDivisionalStrength(planet, divisionalMap, DivisionalChartType.D9_NAVAMSA) * VargaWeights.D9
        val d12Strength = getDivisionalStrength(planet, divisionalMap, DivisionalChartType.D12_DWADASAMSA) * VargaWeights.D12
        val d30Strength = getDivisionalStrength(planet, divisionalMap, DivisionalChartType.D30_TRIMSAMSA) * VargaWeights.D30

        val totalVirupas = d1Strength + d2Strength + d3Strength + d7Strength +
                d9Strength + d12Strength + d30Strength

        return SaptavargajaBalaAnalysis(
            planet = planet,
            d1Strength = d1Strength,
            d2Strength = d2Strength,
            d3Strength = d3Strength,
            d7Strength = d7Strength,
            d9Strength = d9Strength,
            d12Strength = d12Strength,
            d30Strength = d30Strength,
            totalVirupas = totalVirupas,
            interpretation = buildSaptavargajaInterpretation(planet, totalVirupas)
        )
    }

    /**
     * Calculate Ojhayugma Rasyamsa Bala
     */
    private fun calculateOjhayugmaBala(position: PlanetPosition): OjhayugmaBalaAnalysis {
        val planet = position.planet
        val isOddSign = position.sign.number % 2 == 1
        val preferredOdd = planet !in listOf(Planet.MOON, Planet.VENUS)

        val virupas = when (planet) {
            Planet.MOON, Planet.VENUS -> if (!isOddSign) 15.0 else 0.0
            else -> if (isOddSign) 15.0 else 0.0
        }

        return OjhayugmaBalaAnalysis(
            planet = planet,
            isOddSign = isOddSign,
            signName = position.sign.displayName,
            preferredOdd = preferredOdd,
            virupas = virupas,
            interpretation = buildOjhayugmaInterpretation(planet, isOddSign, preferredOdd)
        )
    }

    /**
     * Calculate Kendradi Bala
     */
    private fun calculateKendradiBala(position: PlanetPosition): KendradiBalaAnalysis {
        val planet = position.planet
        val house = position.house

        val (houseType, virupas) = when (house) {
            1, 4, 7, 10 -> HouseType.KENDRA to 60.0
            2, 5, 8, 11 -> HouseType.PANAPARA to 30.0
            3, 6, 9, 12 -> HouseType.APOKLIMA to 15.0
            else -> HouseType.APOKLIMA to 15.0
        }

        return KendradiBalaAnalysis(
            planet = planet,
            houseNumber = house,
            houseType = houseType,
            virupas = virupas,
            interpretation = buildKendradiInterpretation(planet, house, houseType)
        )
    }

    /**
     * Calculate Drekkana Bala
     */
    private fun calculateDrekkanaBala(position: PlanetPosition): DrekkanaBalaAnalysis {
        val planet = position.planet
        val degreeInSign = position.longitude % DEGREES_PER_SIGN
        val decanate = when {
            degreeInSign < 10.0 -> 1
            degreeInSign < 20.0 -> 2
            else -> 3
        }

        val preferredDecanate = when (planet) {
            Planet.SUN, Planet.MARS, Planet.JUPITER -> 1
            Planet.MOON, Planet.VENUS -> 3
            Planet.MERCURY, Planet.SATURN -> 2
            else -> 1
        }

        val virupas = if (decanate == preferredDecanate) 15.0 else 0.0

        return DrekkanaBalaAnalysis(
            planet = planet,
            decanate = decanate,
            degreeInSign = degreeInSign,
            preferredDecanate = preferredDecanate,
            virupas = virupas,
            interpretation = buildDrekkanaInterpretation(planet, decanate, preferredDecanate)
        )
    }

    /**
     * Get dignity type for a planet
     */
    private fun getDignityType(position: PlanetPosition): DignityType {
        val planet = position.planet
        val sign = position.sign
        val degreeInSign = position.longitude % DEGREES_PER_SIGN

        // Check debilitation first
        if (isDebilitated(planet, sign)) return DignityType.DEBILITATED

        // Check exaltation
        if (isExalted(planet, sign)) return DignityType.EXALTED

        // Check moolatrikona
        moolatrikonaRanges[planet]?.let { range ->
            if (sign == range.sign && degreeInSign >= range.startDegree && degreeInSign <= range.endDegree) {
                return DignityType.MOOLATRIKONA
            }
        }

        // Check own sign
        if (sign.ruler == planet) return DignityType.OWN_SIGN

        // Check relationship
        return when (VedicAstrologyUtils.getNaturalRelationship(planet, sign.ruler)) {
            PlanetaryRelationship.BEST_FRIEND, PlanetaryRelationship.FRIEND -> DignityType.FRIEND
            PlanetaryRelationship.NEUTRAL -> DignityType.NEUTRAL
            PlanetaryRelationship.ENEMY, PlanetaryRelationship.BITTER_ENEMY -> DignityType.ENEMY
        }
    }

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

    private fun getVargaStrength(planet: Planet, sign: ZodiacSign, degreeInSign: Double): Double {
        if (isExalted(planet, sign)) return 20.0
        moolatrikonaRanges[planet]?.let { range ->
            if (sign == range.sign && degreeInSign >= range.startDegree && degreeInSign <= range.endDegree) {
                return 22.5
            }
        }
        if (sign.ruler == planet) return 30.0

        return when (VedicAstrologyUtils.getNaturalRelationship(planet, sign.ruler)) {
            PlanetaryRelationship.FRIEND, PlanetaryRelationship.BEST_FRIEND -> 15.0
            PlanetaryRelationship.NEUTRAL -> 10.0
            PlanetaryRelationship.ENEMY, PlanetaryRelationship.BITTER_ENEMY -> 7.5
        }
    }

    private fun getDivisionalStrength(
        planet: Planet,
        divisionalMap: Map<DivisionalChartType, DivisionalChartData>,
        chartType: DivisionalChartType
    ): Double {
        return divisionalMap[chartType]?.planetPositions
            ?.find { it.planet == planet }
            ?.let { pos -> getVargaStrengthBasic(planet, pos.sign) }
            ?: 10.0
    }

    private fun getVargaStrengthBasic(planet: Planet, sign: ZodiacSign): Double {
        if (isExalted(planet, sign)) return 20.0
        if (sign.ruler == planet) return 30.0

        return when (VedicAstrologyUtils.getNaturalRelationship(planet, sign.ruler)) {
            PlanetaryRelationship.FRIEND, PlanetaryRelationship.BEST_FRIEND -> 15.0
            PlanetaryRelationship.NEUTRAL -> 10.0
            PlanetaryRelationship.ENEMY, PlanetaryRelationship.BITTER_ENEMY -> 7.5
        }
    }

    private fun normalizeDegree(degree: Double): Double {
        var normalized = degree % DEGREES_PER_CIRCLE
        if (normalized < 0) normalized += DEGREES_PER_CIRCLE
        return normalized
    }

    // ============================================
    // COMPONENT SUMMARY
    // ============================================

    private fun calculateComponentSummary(balaMap: Map<Planet, PlanetSthanaBala>): List<ComponentAnalysis> {
        val avgUccha = balaMap.values.map { it.ucchaBala.virupas }.average()
        val avgSaptavargaja = balaMap.values.map { it.saptavargajaBala.totalVirupas }.average()
        val avgOjhayugma = balaMap.values.map { it.ojhayugmaBala.virupas }.average()
        val avgKendradi = balaMap.values.map { it.kendradiBala.virupas }.average()
        val avgDrekkana = balaMap.values.map { it.drekkanaBala.virupas }.average()

        return listOf(
            ComponentAnalysis("Uccha Bala", avgUccha, 60.0, (avgUccha / 60.0) * 100, "Exaltation strength"),
            ComponentAnalysis("Saptavargaja Bala", avgSaptavargaja, 337.5, (avgSaptavargaja / 337.5) * 100, "Seven divisional charts"),
            ComponentAnalysis("Ojhayugma Bala", avgOjhayugma, 15.0, (avgOjhayugma / 15.0) * 100, "Odd/Even placement"),
            ComponentAnalysis("Kendradi Bala", avgKendradi, 60.0, (avgKendradi / 60.0) * 100, "Angular house strength"),
            ComponentAnalysis("Drekkana Bala", avgDrekkana, 15.0, (avgDrekkana / 15.0) * 100, "Decanate strength")
        )
    }

    private fun calculateOverallScore(balaMap: Map<Planet, PlanetSthanaBala>): Double {
        if (balaMap.isEmpty()) return 50.0
        return balaMap.values.map { it.percentageOfRequired }.average().coerceIn(0.0, 150.0) / 1.5
    }

    // ============================================
    // INTERPRETATION BUILDERS
    // ============================================

    private fun buildUcchaBalaInterpretation(planet: Planet, virupas: Double, dignity: DignityType): String {
        val quality = when {
            virupas >= 50 -> "highly elevated"
            virupas >= 35 -> "moderately elevated"
            virupas >= 20 -> "moderately positioned"
            else -> "poorly positioned"
        }
        return "${planet.displayName} is $quality with ${dignity.displayName} dignity."
    }

    private fun buildSaptavargajaInterpretation(planet: Planet, virupas: Double): String {
        val quality = when {
            virupas >= 250 -> "exceptional"
            virupas >= 180 -> "strong"
            virupas >= 120 -> "moderate"
            else -> "weak"
        }
        return "${planet.displayName} has $quality divisional chart strength."
    }

    private fun buildOjhayugmaInterpretation(planet: Planet, isOdd: Boolean, preferredOdd: Boolean): String {
        val matches = (isOdd && preferredOdd) || (!isOdd && !preferredOdd)
        return if (matches) {
            "${planet.displayName} is well-placed in ${if (isOdd) "odd" else "even"} sign."
        } else {
            "${planet.displayName} is not in its preferred sign type."
        }
    }

    private fun buildKendradiInterpretation(planet: Planet, house: Int, houseType: HouseType): String {
        return "${planet.displayName} in house $house (${houseType.displayName}) gains ${houseType.virupas} virupas."
    }

    private fun buildDrekkanaInterpretation(planet: Planet, decanate: Int, preferred: Int): String {
        return if (decanate == preferred) {
            "${planet.displayName} is in its preferred decanate ($decanate)."
        } else {
            "${planet.displayName} is in decanate $decanate but prefers $preferred."
        }
    }

    private fun buildPlanetInterpretation(
        planet: Planet,
        virupas: Double,
        percentage: Double,
        dignity: DignityType
    ): String {
        val quality = when {
            percentage >= 120 -> "exceptionally strong positional strength"
            percentage >= 100 -> "good positional strength"
            percentage >= 80 -> "moderate positional strength"
            percentage >= 60 -> "weak positional strength"
            else -> "very weak positional strength"
        }
        return "${planet.displayName} with ${dignity.displayName} dignity has $quality (${String.format("%.1f", percentage)}% of required)."
    }

    private fun generateInsights(balaMap: Map<Planet, PlanetSthanaBala>): List<String> {
        val insights = mutableListOf<String>()

        // Exalted planets
        val exalted = balaMap.values.filter { it.ucchaBala.dignityType == DignityType.EXALTED }
        if (exalted.isNotEmpty()) {
            insights.add("${exalted.joinToString { it.planet.displayName }} in exaltation provides excellent strength")
        }

        // Debilitated planets
        val debilitated = balaMap.values.filter { it.ucchaBala.dignityType == DignityType.DEBILITATED }
        if (debilitated.isNotEmpty()) {
            insights.add("${debilitated.joinToString { it.planet.displayName }} in debilitation needs remedial measures")
        }

        // Kendra planets
        val kendraCount = balaMap.values.count { it.kendradiBala.houseType == HouseType.KENDRA }
        if (kendraCount >= 3) {
            insights.add("Multiple planets in angular houses indicate strong chart foundation")
        }

        // Strong navamsa
        val strongNavamsa = balaMap.values.filter { it.saptavargajaBala.d9Strength >= 100 }
        if (strongNavamsa.isNotEmpty()) {
            insights.add("${strongNavamsa.joinToString { it.planet.displayName }} has strong Navamsa placement")
        }

        return insights.take(5)
    }

    private fun generateRecommendations(balaMap: Map<Planet, PlanetSthanaBala>): List<String> {
        val recommendations = mutableListOf<String>()

        val weak = balaMap.values.filter { it.percentageOfRequired < 80 }
        weak.forEach {
            recommendations.add("Consider remedies for ${it.planet.displayName}'s weak positional strength")
        }

        val strong = balaMap.values.filter { it.percentageOfRequired >= 120 }
        strong.forEach {
            recommendations.add("${it.planet.displayName}'s strong position can be leveraged for success")
        }

        return recommendations.take(5)
    }
}
