package com.astro.storm.ephemeris

import com.astro.storm.data.model.Planet
import com.astro.storm.data.model.PlanetPosition
import com.astro.storm.data.model.VedicChart
import com.astro.storm.data.model.ZodiacSign
import com.astro.storm.data.localization.Language
import com.astro.storm.data.localization.StringResources
import com.astro.storm.data.localization.StringKeyAnalysis
import com.astro.storm.data.localization.StringKeyMatch

/**
 * Argala (Intervention) Calculator
 *
 * Argala is a concept from Jaimini Astrology that describes how planets in certain
 * houses "intervene" or influence the results of other houses and planets.
 *
 * The word "Argala" means "bolt" or "bar" - like a door bolt that can open or close
 * the flow of energy from one house to another.
 *
 * ## Types of Argala
 *
 * ### Primary Argala (Shubha/Ashubha depending on planets)
 * - 2nd house from any sign/planet - Secondary Argala (Dhan Argala - Wealth intervention)
 * - 4th house from any sign/planet - Primary Argala (Sukha Argala - Happiness intervention)
 * - 11th house from any sign/planet - Primary Argala (Labha Argala - Gains intervention)
 * - 5th house from any sign/planet - Special Argala (Putra Argala - Progeny intervention)
 *
 * ### Virodha Argala (Obstruction)
 * - 12th house obstructs 2nd house Argala
 * - 10th house obstructs 4th house Argala
 * - 3rd house obstructs 11th house Argala
 * - 9th house obstructs 5th house Argala (conditional)
 *
 * ## Key Principles
 * 1. Argala is applied from both signs and planets
 * 2. Benefic planets create Shubha (auspicious) Argala
 * 3. Malefic planets create Ashubha (inauspicious) Argala
 * 4. More planets = stronger Argala
 * 5. Virodha Argala can nullify or reduce primary Argala
 *
 * ## References
 * - Jaimini Sutras (Chapter 1, Pada 1, Sutras 5-8)
 * - Commentary by Raghunatha Bhatta
 * - Commentary by Somanatha
 * - "Jaimini Sutramritam" by Iranganti Rangacharya
 *
 * @author AstroStorm
 */
object ArgalaCalculator {

    // ============================================
    // DATA CLASSES
    // ============================================

    /**
     * Complete Argala analysis for a chart
     */
    data class ArgalaAnalysis(
        val houseArgalas: Map<Int, HouseArgalaResult>,
        val planetArgalas: Map<Planet, PlanetArgalaResult>,
        val significantArgalas: List<SignificantArgala>,
        val overallAssessment: OverallArgalaAssessment
    )

    /**
     * Argala analysis for a specific house
     */
    data class HouseArgalaResult(
        val house: Int,
        val primaryArgalas: List<ArgalaInfluence>,
        val virodhaArgalas: List<VirodhaArgala>,
        val netArgalaStrength: Double,
        val effectiveArgala: EffectiveArgala,
        val interpretation: String
    )

    /**
     * Argala analysis from a specific planet
     */
    data class PlanetArgalaResult(
        val planet: Planet,
        val argalasReceived: List<ArgalaInfluence>,
        val virodhasReceived: List<VirodhaArgala>,
        val netStrength: Double,
        val interpretation: String
    )

    /**
     * Individual Argala influence
     */
    data class ArgalaInfluence(
        val sourceType: SourceType,
        val sourcePlanet: Planet?,
        val sourceHouse: Int,
        val argalaHouse: Int,
        val argalaType: ArgalaType,
        val nature: ArgalaNature,
        val strength: Double,
        val planets: List<Planet>,
        val description: String
    )

    /**
     * Virodha (Obstruction) Argala
     */
    data class VirodhaArgala(
        val obstructingHouse: Int,
        val obstructedArgalaHouse: Int,
        val obstructingPlanets: List<Planet>,
        val obstructionStrength: Double,
        val isEffective: Boolean,
        val description: String
    )

    /**
     * Effective Argala after considering Virodha
     */
    data class EffectiveArgala(
        val netBeneficStrength: Double,
        val netMaleficStrength: Double,
        val dominantNature: ArgalaNature,
        val isSignificant: Boolean,
        val summary: String
    )

    /**
     * Significant Argala formations in the chart
     */
    data class SignificantArgala(
        val targetHouse: Int,
        val targetDescription: String,
        val argalaType: ArgalaType,
        val nature: ArgalaNature,
        val strength: ArgalaStrength,
        val involvedPlanets: List<Planet>,
        val lifeAreaEffect: String,
        val recommendation: String
    )

    /**
     * Overall assessment of Argala patterns in the chart
     */
    data class OverallArgalaAssessment(
        val strongestBeneficArgala: Int?,
        val strongestMaleficArgala: Int?,
        val mostObstructedHouse: Int?,
        val leastObstructedHouse: Int?,
        val karmaPatterns: List<String>,
        val strengthDistribution: Map<Int, Double>,
        val generalRecommendations: List<String>
    )

    // ============================================
    // ENUMS
    // ============================================

    enum class SourceType {
        HOUSE,      // Argala from a house
        PLANET      // Argala from a planet
    }

    enum class ArgalaType(val offset: Int, val virodhaOffset: Int, val displayName: String) {
        SECONDARY_ARGALA(2, 12, "Secondary Argala (Dhan)"),      // 2nd house, obstructed by 12th
        PRIMARY_ARGALA(4, 10, "Primary Argala (Sukha)"),         // 4th house, obstructed by 10th
        GAINS_ARGALA(11, 3, "Gains Argala (Labha)"),             // 11th house, obstructed by 3rd
        SPECIAL_ARGALA(5, 9, "Special Argala (Putra)")           // 5th house, conditionally obstructed by 9th
    }

    enum class ArgalaNature {
        SHUBHA,         // Auspicious - created by benefics
        ASHUBHA,        // Inauspicious - created by malefics
        MIXED           // Both benefics and malefics involved
    }

    enum class ArgalaStrength {
        VERY_STRONG,
        STRONG,
        MODERATE,
        WEAK,
        OBSTRUCTED
    }

    // ============================================
    // ARGALA CONFIGURATION
    // ============================================

    /**
     * Argala house offsets and their Virodha (obstruction) houses
     * Format: Argala house offset -> Virodha house offset
     */
    private val ARGALA_VIRODHA_MAP = mapOf(
        2 to 12,    // 2nd house Argala obstructed by 12th
        4 to 10,    // 4th house Argala obstructed by 10th
        11 to 3,    // 11th house Argala obstructed by 3rd
        5 to 9      // 5th house Argala conditionally obstructed by 9th
    )

    /**
     * Argala strength weights based on type
     */
    private val ARGALA_WEIGHTS = mapOf(
        ArgalaType.PRIMARY_ARGALA to 1.0,        // 4th house - strongest
        ArgalaType.GAINS_ARGALA to 1.0,          // 11th house - strongest
        ArgalaType.SECONDARY_ARGALA to 0.75,     // 2nd house - secondary strength
        ArgalaType.SPECIAL_ARGALA to 0.5         // 5th house - conditional
    )

    // ============================================
    // MAIN CALCULATION METHODS
    // ============================================

    /**
     * Perform complete Argala analysis for a chart
     *
     * @param chart The VedicChart to analyze
     * @return Complete ArgalaAnalysis with all house and planet Argalas
     */
    fun analyzeArgala(chart: VedicChart, language: Language): ArgalaAnalysis {
        // Group planets by house
        val planetsByHouse = chart.planetPositions.groupBy { it.house }

        // Analyze Argala for each house
        val houseArgalas = (1..12).associateWith { house ->
            analyzeHouseArgala(house, planetsByHouse, chart, language)
        }

        // Analyze Argala received by each planet
        val planetArgalas = Planet.entries
            .filter { it in Planet.MAIN_PLANETS }
            .mapNotNull { planet ->
                chart.planetPositions.find { it.planet == planet }?.let { pos ->
                    planet to analyzePlanetArgala(pos, planetsByHouse, chart, language)
                }
            }.toMap()

        // Identify significant Argalas
        val significantArgalas = identifySignificantArgalas(houseArgalas, chart, language)

        // Generate overall assessment
        val overallAssessment = generateOverallAssessment(houseArgalas, chart, language)

        return ArgalaAnalysis(
            houseArgalas = houseArgalas,
            planetArgalas = planetArgalas,
            significantArgalas = significantArgalas,
            overallAssessment = overallAssessment
        )
    }

    /**
     * Analyze Argala influences on a specific house
     */
    private fun analyzeHouseArgala(
        targetHouse: Int,
        planetsByHouse: Map<Int, List<PlanetPosition>>,
        chart: VedicChart,
        language: Language
    ): HouseArgalaResult {
        val primaryArgalas = mutableListOf<ArgalaInfluence>()
        val virodhaArgalas = mutableListOf<VirodhaArgala>()

        // Calculate each type of Argala
        for (argalaType in ArgalaType.entries) {
            val argalaHouse = getHouseAtOffset(targetHouse, argalaType.offset)
            val virodhaHouse = getHouseAtOffset(targetHouse, argalaType.virodhaOffset)

            val argalaPlanets = planetsByHouse[argalaHouse] ?: emptyList()
            val virodhaPlanets = planetsByHouse[virodhaHouse] ?: emptyList()

            if (argalaPlanets.isNotEmpty()) {
                val argalaInfluence = calculateArgalaInfluence(
                    targetHouse, argalaHouse, argalaType, argalaPlanets, chart, language
                )
                primaryArgalas.add(argalaInfluence)

                // Check for Virodha (obstruction)
                if (virodhaPlanets.isNotEmpty()) {
                    val virodha = calculateVirodhaArgala(
                        virodhaHouse, argalaHouse, virodhaPlanets, argalaInfluence, argalaType, language
                    )
                    virodhaArgalas.add(virodha)
                }
            }
        }

        // Calculate effective Argala
        val effectiveArgala = calculateEffectiveArgala(primaryArgalas, virodhaArgalas, language)

        // Calculate net strength
        val netStrength = effectiveArgala.netBeneficStrength - effectiveArgala.netMaleficStrength

        // Generate interpretation
        val interpretation = generateHouseArgalaInterpretation(targetHouse, effectiveArgala, chart, language)

        return HouseArgalaResult(
            house = targetHouse,
            primaryArgalas = primaryArgalas,
            virodhaArgalas = virodhaArgalas,
            netArgalaStrength = netStrength,
            effectiveArgala = effectiveArgala,
            interpretation = interpretation
        )
    }

    /**
     * Analyze Argala received by a specific planet
     */
    private fun analyzePlanetArgala(
        planetPosition: PlanetPosition,
        planetsByHouse: Map<Int, List<PlanetPosition>>,
        chart: VedicChart,
        language: Language
    ): PlanetArgalaResult {
        val fromHouse = planetPosition.house
        val argalasReceived = mutableListOf<ArgalaInfluence>()
        val virodhasReceived = mutableListOf<VirodhaArgala>()

        // Calculate Argala from each type
        for (argalaType in ArgalaType.entries) {
            val argalaHouse = getHouseAtOffset(fromHouse, argalaType.offset)
            val virodhaHouse = getHouseAtOffset(fromHouse, argalaType.virodhaOffset)

            val argalaPlanets = planetsByHouse[argalaHouse]?.filter { it.planet != planetPosition.planet } ?: emptyList()
            val virodhaPlanets = planetsByHouse[virodhaHouse]?.filter { it.planet != planetPosition.planet } ?: emptyList()

            if (argalaPlanets.isNotEmpty()) {
                val argalaInfluence = calculateArgalaInfluence(
                    fromHouse, argalaHouse, argalaType, argalaPlanets, chart, language
                )
                argalasReceived.add(argalaInfluence)

                if (virodhaPlanets.isNotEmpty()) {
                    val virodha = calculateVirodhaArgala(
                        virodhaHouse, argalaHouse, virodhaPlanets, argalaInfluence, argalaType, language
                    )
                    virodhasReceived.add(virodha)
                }
            }
        }

        // Calculate net strength
        var netStrength = argalasReceived.sumOf { argala ->
            when (argala.nature) {
                ArgalaNature.SHUBHA -> argala.strength
                ArgalaNature.ASHUBHA -> -argala.strength
                ArgalaNature.MIXED -> argala.strength * 0.3
            }
        }

        // Reduce by effective Virodhas
        virodhasReceived.filter { it.isEffective }.forEach { virodha ->
            netStrength *= (1.0 - virodha.obstructionStrength * 0.5)
        }

        val interpretation = generatePlanetArgalaInterpretation(planetPosition.planet, argalasReceived, netStrength, language)

        return PlanetArgalaResult(
            planet = planetPosition.planet,
            argalasReceived = argalasReceived,
            virodhasReceived = virodhasReceived,
            netStrength = netStrength,
            interpretation = interpretation
        )
    }

    /**
     * Calculate Argala influence from planets in a house
     */
    private fun calculateArgalaInfluence(
        targetHouse: Int,
        argalaHouse: Int,
        argalaType: ArgalaType,
        planets: List<PlanetPosition>,
        chart: VedicChart,
        language: Language
    ): ArgalaInfluence {
        val beneficPlanets = planets.filter { VedicAstrologyUtils.isNaturalBenefic(it.planet) }
        val maleficPlanets = planets.filter { VedicAstrologyUtils.isNaturalMalefic(it.planet) }

        val nature = when {
            beneficPlanets.isNotEmpty() && maleficPlanets.isEmpty() -> ArgalaNature.SHUBHA
            maleficPlanets.isNotEmpty() && beneficPlanets.isEmpty() -> ArgalaNature.ASHUBHA
            else -> ArgalaNature.MIXED
        }

        // Calculate strength based on number and dignity of planets
        var strength = planets.size * (ARGALA_WEIGHTS[argalaType] ?: 0.5)

        // Boost for exalted planets
        planets.forEach { pos ->
            if (VedicAstrologyUtils.isExalted(pos)) strength += 0.5
            if (VedicAstrologyUtils.isInOwnSign(pos)) strength += 0.3
            if (VedicAstrologyUtils.isDebilitated(pos)) strength -= 0.3
        }

        val planetsDesc = planets.joinToString(", ") { it.planet.getLocalizedName(language) }
        val natureDesc = getLocalizedNature(nature, language)
        val description = StringResources.get(
            StringKeyAnalysis.ARGALA_INFLUENCE_DESC,
            language,
            getArgalaTypeName(argalaType, language),
            argalaHouse,
            planetsDesc,
            natureDesc
        )

        return ArgalaInfluence(
            sourceType = SourceType.HOUSE,
            sourcePlanet = null,
            sourceHouse = argalaHouse,
            argalaHouse = argalaHouse,
            argalaType = argalaType,
            nature = nature,
            strength = strength.coerceIn(0.0, 3.0),
            planets = planets.map { it.planet },
            description = description
        )
    }

    /**
     * Calculate Virodha (obstruction) Argala
     */
    private fun calculateVirodhaArgala(
        virodhaHouse: Int,
        obstructedArgalaHouse: Int,
        virodhaPlanets: List<PlanetPosition>,
        argalaInfluence: ArgalaInfluence,
        argalaType: ArgalaType,
        language: Language
    ): VirodhaArgala {
        // Virodha is effective when planets in obstruction house >= planets in Argala house
        val virodhaCount = virodhaPlanets.size
        val argalaCount = argalaInfluence.planets.size

        // Special rule for 5th house Argala: 9th house obstructs only if it has more planets
        val isEffective = if (argalaType == ArgalaType.SPECIAL_ARGALA) {
            virodhaCount > argalaCount
        } else {
            virodhaCount >= argalaCount
        }

        var obstructionStrength = if (isEffective) {
            (virodhaCount.toDouble() / argalaCount).coerceAtMost(1.0)
        } else {
            0.0
        }

        // Strong planets in Virodha position increase obstruction
        virodhaPlanets.forEach { pos ->
            if (VedicAstrologyUtils.isExalted(pos)) obstructionStrength += 0.2
            if (VedicAstrologyUtils.isInOwnSign(pos)) obstructionStrength += 0.1
        }

        val planetsDesc = virodhaPlanets.joinToString(", ") { it.planet.getLocalizedName(language) }
        val key = if (isEffective) StringKeyAnalysis.ARGALA_EFFECTIVE_OBSTRUCTION else StringKeyAnalysis.ARGALA_PARTIAL_OBSTRUCTION
        val description = StringResources.get(
            key,
            language,
            virodhaHouse,
            obstructedArgalaHouse,
            planetsDesc
        )

        return VirodhaArgala(
            obstructingHouse = virodhaHouse,
            obstructedArgalaHouse = obstructedArgalaHouse,
            obstructingPlanets = virodhaPlanets.map { it.planet },
            obstructionStrength = obstructionStrength.coerceIn(0.0, 1.0),
            isEffective = isEffective,
            description = description
        )
    }

    /**
     * Calculate effective Argala after considering Virodha
     */
    private fun calculateEffectiveArgala(
        primaryArgalas: List<ArgalaInfluence>,
        virodhaArgalas: List<VirodhaArgala>,
        language: Language
    ): EffectiveArgala {
        var beneficStrength = 0.0
        var maleficStrength = 0.0

        primaryArgalas.forEach { argala ->
            // Find matching Virodha
            val virodha = virodhaArgalas.find { it.obstructedArgalaHouse == argala.argalaHouse }
            val reductionFactor = if (virodha?.isEffective == true) {
                1.0 - virodha.obstructionStrength
            } else {
                1.0
            }

            when (argala.nature) {
                ArgalaNature.SHUBHA -> beneficStrength += argala.strength * reductionFactor
                ArgalaNature.ASHUBHA -> maleficStrength += argala.strength * reductionFactor
                ArgalaNature.MIXED -> {
                    beneficStrength += argala.strength * 0.4 * reductionFactor
                    maleficStrength += argala.strength * 0.4 * reductionFactor
                }
            }
        }

        val dominantNature = when {
            beneficStrength > maleficStrength + 0.5 -> ArgalaNature.SHUBHA
            maleficStrength > beneficStrength + 0.5 -> ArgalaNature.ASHUBHA
            else -> ArgalaNature.MIXED
        }

        val isSignificant = (beneficStrength + maleficStrength) >= 1.0

        val summary = buildString {
            when {
                beneficStrength > maleficStrength * 2 -> append(StringResources.get(StringKeyAnalysis.AVASTHA_STRONG_CONFIG, language))
                maleficStrength > beneficStrength * 2 -> append(StringResources.get(StringKeyAnalysis.AVASTHA_NEEDS_MEASURES, language))
                isSignificant -> append(StringResources.get(StringKeyAnalysis.ARGALA_INTERP_MIXED, language))
                else -> append(StringResources.get(StringKeyAnalysis.ARGALA_INTERP_DEPENDS, language))
            }
            append(" Benefic: ${String.format("%.2f", beneficStrength)}, ")
            append("Malefic: ${String.format("%.2f", maleficStrength)}")
        }

        return EffectiveArgala(
            netBeneficStrength = beneficStrength,
            netMaleficStrength = maleficStrength,
            dominantNature = dominantNature,
            isSignificant = isSignificant,
            summary = summary
        )
    }

    /**
     * Identify significant Argala patterns in the chart
     */
    private fun identifySignificantArgalas(
        houseArgalas: Map<Int, HouseArgalaResult>,
        chart: VedicChart,
        language: Language
    ): List<SignificantArgala> {
        val significants = mutableListOf<SignificantArgala>()

        houseArgalas.forEach { (house, result) ->
            if (result.effectiveArgala.isSignificant) {
                result.primaryArgalas.forEach { argala ->
                    if (argala.strength >= 1.0) {
                        val strength = when {
                            argala.strength >= 2.5 -> ArgalaStrength.VERY_STRONG
                            argala.strength >= 2.0 -> ArgalaStrength.STRONG
                            argala.strength >= 1.5 -> ArgalaStrength.MODERATE
                            else -> ArgalaStrength.WEAK
                        }

                        val virodha = result.virodhaArgalas.find { it.obstructedArgalaHouse == argala.argalaHouse }
                        val effectiveStrength = if (virodha?.isEffective == true) ArgalaStrength.OBSTRUCTED else strength

                        significants.add(
                            SignificantArgala(
                                targetHouse = house,
                                targetDescription = getHouseDescription(house, language),
                                argalaType = argala.argalaType,
                                nature = argala.nature,
                                strength = effectiveStrength,
                                involvedPlanets = argala.planets,
                                lifeAreaEffect = getLifeAreaEffect(house, argala.nature, argala.argalaType, language),
                                recommendation = getArgalaRecommendation(house, argala.nature, argala.planets, language)
                            )
                        )
                    }
                }
            }
        }

        return significants.sortedByDescending {
            when (it.strength) {
                ArgalaStrength.VERY_STRONG -> 5
                ArgalaStrength.STRONG -> 4
                ArgalaStrength.MODERATE -> 3
                ArgalaStrength.WEAK -> 2
                ArgalaStrength.OBSTRUCTED -> 1
            }
        }
    }

    /**
     * Generate overall Argala assessment
     */
    private fun generateOverallAssessment(
        houseArgalas: Map<Int, HouseArgalaResult>,
        chart: VedicChart,
        language: Language
    ): OverallArgalaAssessment {
        val strengthDistribution = houseArgalas.mapValues { it.value.netArgalaStrength }

        val strongestBenefic = houseArgalas.entries
            .filter { it.value.effectiveArgala.dominantNature == ArgalaNature.SHUBHA }
            .maxByOrNull { it.value.effectiveArgala.netBeneficStrength }?.key

        val strongestMalefic = houseArgalas.entries
            .filter { it.value.effectiveArgala.dominantNature == ArgalaNature.ASHUBHA }
            .maxByOrNull { it.value.effectiveArgala.netMaleficStrength }?.key

        val mostObstructed = houseArgalas.entries
            .filter { it.value.virodhaArgalas.any { v -> v.isEffective } }
            .maxByOrNull { entry ->
                entry.value.virodhaArgalas.sumOf { it.obstructionStrength }
            }?.key

        val leastObstructed = houseArgalas.entries
            .filter { it.value.primaryArgalas.isNotEmpty() }
            .minByOrNull { entry ->
                entry.value.virodhaArgalas.sumOf { if (it.isEffective) it.obstructionStrength else 0.0 }
            }?.key

        val karmaPatterns = generateKarmaPatterns(houseArgalas, chart, language)
        val recommendations = generateGeneralRecommendations(houseArgalas, chart, language)

        return OverallArgalaAssessment(
            strongestBeneficArgala = strongestBenefic,
            strongestMaleficArgala = strongestMalefic,
            mostObstructedHouse = mostObstructed,
            leastObstructedHouse = leastObstructed,
            karmaPatterns = karmaPatterns,
            strengthDistribution = strengthDistribution,
            generalRecommendations = recommendations
        )
    }

    // ============================================
    // INTERPRETATION METHODS
    // ============================================

    private fun generateHouseArgalaInterpretation(
        house: Int,
        effectiveArgala: EffectiveArgala,
        chart: VedicChart,
        language: Language
    ): String {
        val houseTheme = getHouseDescription(house, language)

        return buildString {
            append("$houseTheme: ")
            when (effectiveArgala.dominantNature) {
                ArgalaNature.SHUBHA -> {
                    append(StringResources.get(StringKeyAnalysis.ARGALA_INTERP_SUPPORT, language))
                    append(" ")
                    append(StringResources.get(StringKeyAnalysis.ARGALA_INTERP_FLOURISH, language))
                }
                ArgalaNature.ASHUBHA -> {
                    append(StringResources.get(StringKeyAnalysis.ARGALA_INTERP_CHALLENGE, language))
                    append(" ")
                    append(StringResources.get(StringKeyAnalysis.ARGALA_INTERP_OVERCOME, language))
                }
                ArgalaNature.MIXED -> {
                    append(StringResources.get(StringKeyAnalysis.ARGALA_INTERP_MIXED, language))
                    append(" ")
                    append(StringResources.get(StringKeyAnalysis.ARGALA_INTERP_DEPENDS, language))
                }
            }
            append(" ")
            append(effectiveArgala.summary)
        }
    }

    private fun generatePlanetArgalaInterpretation(
        planet: Planet,
        argalas: List<ArgalaInfluence>,
        netStrength: Double,
        language: Language
    ): String {
        return buildString {
            append("${planet.getLocalizedName(language)}: ")
            when {
                netStrength > 1.0 -> append(StringResources.get(StringKeyAnalysis.ARGALA_PLANET_SUPPORTED, language))
                netStrength > 0 -> append(StringResources.get(StringKeyAnalysis.ARGALA_PLANET_MODERATE, language))
                netStrength > -1.0 -> append(StringResources.get(StringKeyAnalysis.ARGALA_PLANET_OBSTRUCTED, language))
                else -> append(StringResources.get(StringKeyAnalysis.ARGALA_PLANET_CHALLENGED, language))
            }
            if (argalas.isNotEmpty()) {
                append(" ")
                append(StringResources.get(StringKeyAnalysis.ARGALA_PLANET_INFLUENCES, language, argalas.size))
            }
        }
    }

    private fun generateKarmaPatterns(
        houseArgalas: Map<Int, HouseArgalaResult>,
        chart: VedicChart,
        language: Language
    ): List<String> {
        val patterns = mutableListOf<String>()

        // Check Dharma houses (1, 5, 9)
        val dharmaStrength = listOf(1, 5, 9).sumOf { houseArgalas[it]?.netArgalaStrength ?: 0.0 }
        if (dharmaStrength > 3.0) {
            patterns.add(StringResources.get(StringKeyAnalysis.ARGALA_KARMA_DHARMA_STRONG, language))
        } else if (dharmaStrength < -1.0) {
            patterns.add(StringResources.get(StringKeyAnalysis.ARGALA_KARMA_DHARMA_WEAK, language))
        }

        // Check Artha houses (2, 6, 10)
        val arthaStrength = listOf(2, 6, 10).sumOf { houseArgalas[it]?.netArgalaStrength ?: 0.0 }
        if (arthaStrength > 3.0) {
            patterns.add(StringResources.get(StringKeyAnalysis.ARGALA_KARMA_ARTHA_STRONG, language))
        } else if (arthaStrength < -1.0) {
            patterns.add(StringResources.get(StringKeyAnalysis.ARGALA_KARMA_ARTHA_WEAK, language))
        }

        // Check Kama houses (3, 7, 11)
        val kamaStrength = listOf(3, 7, 11).sumOf { houseArgalas[it]?.netArgalaStrength ?: 0.0 }
        if (kamaStrength > 3.0) {
            patterns.add(StringResources.get(StringKeyAnalysis.ARGALA_KARMA_KAMA_STRONG, language))
        } else if (kamaStrength < -1.0) {
            patterns.add(StringResources.get(StringKeyAnalysis.ARGALA_KARMA_KAMA_WEAK, language))
        }

        // Check Moksha houses (4, 8, 12)
        val mokshaStrength = listOf(4, 8, 12).sumOf { houseArgalas[it]?.netArgalaStrength ?: 0.0 }
        if (mokshaStrength > 3.0) {
            patterns.add(StringResources.get(StringKeyAnalysis.ARGALA_KARMA_MOKSHA_STRONG, language))
        }

        return patterns
    }

    private fun generateGeneralRecommendations(
        houseArgalas: Map<Int, HouseArgalaResult>,
        chart: VedicChart,
        language: Language
    ): List<String> {
        val recommendations = mutableListOf<String>()

        // Check for houses needing remedies
        houseArgalas.forEach { (house, result) ->
            if (result.effectiveArgala.dominantNature == ArgalaNature.ASHUBHA &&
                result.effectiveArgala.netMaleficStrength > 1.5) {
                val remedy = when (house) {
                    1 -> StringResources.get(StringKeyAnalysis.ARGALA_REC_REMEDY_SUN, language)
                    7 -> StringResources.get(StringKeyAnalysis.ARGALA_REC_REMEDY_VENUS, language)
                    10 -> StringResources.get(StringKeyAnalysis.ARGALA_REC_REMEDY_SATURN, language)
                    else -> StringResources.get(StringKeyAnalysis.ARGALA_REC_REMEDY_GENERIC, language, house)
                }
                recommendations.add(remedy)
            }
        }

        // Add general advice based on patterns
        val totalBenefic = houseArgalas.values.sumOf { it.effectiveArgala.netBeneficStrength }
        val totalMalefic = houseArgalas.values.sumOf { it.effectiveArgala.netMaleficStrength }

        if (totalBenefic > totalMalefic * 1.5) {
            recommendations.add(StringResources.get(StringKeyAnalysis.ARGALA_REC_FAVORABLE, language))
        } else if (totalMalefic > totalBenefic * 1.5) {
            recommendations.add(StringResources.get(StringKeyAnalysis.ARGALA_REC_CHALLENGING, language))
        }

        return recommendations
    }

    // ============================================
    // HELPER METHODS
    // ============================================

    /**
     * Calculate house at a specific offset from base house
     */
    private fun getHouseAtOffset(baseHouse: Int, offset: Int): Int {
        return ((baseHouse + offset - 2) % 12) + 1
    }

    /**
     * Get description for a house
     */
    private fun getHouseDescription(house: Int, language: Language): String {
        val key = when (house) {
            1 -> StringKeyAnalysis.HOUSE_1_NAME
            2 -> StringKeyAnalysis.HOUSE_2_NAME
            3 -> StringKeyAnalysis.HOUSE_3_NAME
            4 -> StringKeyAnalysis.HOUSE_4_NAME
            5 -> StringKeyAnalysis.HOUSE_5_NAME
            6 -> StringKeyAnalysis.HOUSE_6_NAME
            7 -> StringKeyAnalysis.HOUSE_7_NAME
            8 -> StringKeyAnalysis.HOUSE_8_NAME
            9 -> StringKeyAnalysis.HOUSE_9_NAME
            10 -> StringKeyAnalysis.HOUSE_10_NAME
            11 -> StringKeyAnalysis.HOUSE_11_NAME
            12 -> StringKeyAnalysis.HOUSE_12_NAME
            else -> null
        }
        return if (key != null) StringResources.get(key, language) else "House $house"
    }

    /**
     * Get life area effect based on Argala
     */
    private fun getLifeAreaEffect(house: Int, nature: ArgalaNature, argalaType: ArgalaType, language: Language): String {
        val effect = if (nature == ArgalaNature.SHUBHA) 
            StringResources.get(StringKeyAnalysis.ARGALA_EFFECT_SUPPORT, language) 
        else 
            StringResources.get(StringKeyAnalysis.ARGALA_EFFECT_CHALLENGE, language)

        val source = when (argalaType) {
            ArgalaType.SECONDARY_ARGALA -> StringResources.get(StringKeyAnalysis.ARGALA_SOURCE_WEALTH, language)
            ArgalaType.PRIMARY_ARGALA -> StringResources.get(StringKeyAnalysis.ARGALA_SOURCE_HAPPINESS, language)
            ArgalaType.GAINS_ARGALA -> StringResources.get(StringKeyAnalysis.ARGALA_SOURCE_GAINS, language)
            ArgalaType.SPECIAL_ARGALA -> StringResources.get(StringKeyAnalysis.ARGALA_SOURCE_INTELLIGENCE, language)
        }

        val area = when (house) {
            1 -> StringResources.get(StringKeyAnalysis.ARGALA_TARGET_PERSONALITY, language)
            7 -> StringResources.get(StringKeyAnalysis.ARGALA_TARGET_RELATIONSHIPS, language)
            10 -> StringResources.get(StringKeyAnalysis.ARGALA_TARGET_CAREER, language)
            4 -> StringResources.get(StringKeyAnalysis.ARGALA_TARGET_HOME, language)
            5 -> StringResources.get(StringKeyAnalysis.ARGALA_TARGET_CHILDREN, language)
            else -> StringResources.get(StringKeyAnalysis.ARGALA_TARGET_GENERIC, language, house)
        }

        return StringResources.get(StringKeyAnalysis.ARGALA_AREA_EFFECT, language, source, effect, area)
    }

    /**
     * Get recommendation for Argala
     */
    private fun getArgalaRecommendation(house: Int, nature: ArgalaNature, planets: List<Planet>, language: Language): String {
        return if (nature == ArgalaNature.ASHUBHA) {
            val mainPlanet = planets.firstOrNull()
            when (mainPlanet) {
                Planet.SATURN -> StringResources.get(StringKeyAnalysis.ARGALA_REC_REMEDY_SATURN, language)
                // Add more specific remedies if desired, currently using generic
                else -> StringResources.get(StringKeyAnalysis.ARGALA_REC_REMEDY_GENERIC, language, house)
            }
        } else {
            StringResources.get(StringKeyAnalysis.ARGALA_REC_FAVORABLE, language)
        }
    }

    private fun getArgalaTypeName(type: ArgalaType, language: Language): String {
        return when (type) {
            ArgalaType.SECONDARY_ARGALA -> StringResources.get(StringKeyAnalysis.ARGALA_SECONDARY_DESC, language)
            ArgalaType.PRIMARY_ARGALA -> StringResources.get(StringKeyAnalysis.ARGALA_PRIMARY_DESC, language)
            ArgalaType.GAINS_ARGALA -> StringResources.get(StringKeyAnalysis.ARGALA_GAINS_ARGALA, language)
            ArgalaType.SPECIAL_ARGALA -> StringResources.get(StringKeyAnalysis.ARGALA_FIFTH_HOUSE_DESC, language)
        }
    }

    private fun getLocalizedNature(nature: ArgalaNature, language: Language): String {
        return when (nature) {
            ArgalaNature.SHUBHA -> StringResources.get(StringKeyAnalysis.BENEFIC, language)
            ArgalaNature.ASHUBHA -> StringResources.get(StringKeyAnalysis.MALEFIC, language)
            ArgalaNature.MIXED -> StringResources.get(StringKeyAnalysis.MIXED, language)
        }
    }

    // ============================================
    // UTILITY METHODS
    // ============================================

    /**
     * Get Argala summary for a specific house
     */
    fun getHouseSummary(chart: VedicChart, house: Int, language: Language): String {
        val analysis = analyzeArgala(chart, language)
        val houseResult = analysis.houseArgalas[house] ?: return "No Argala data available"
        return houseResult.interpretation
    }

    /**
     * Check if a house has strong Shubha Argala
     */
    fun hasStrongBeneficArgala(chart: VedicChart, house: Int, language: Language): Boolean {
        val analysis = analyzeArgala(chart, language)
        val result = analysis.houseArgalas[house] ?: return false
        return result.effectiveArgala.dominantNature == ArgalaNature.SHUBHA &&
               result.effectiveArgala.netBeneficStrength >= 1.5
    }

    /**
     * Get planets causing Argala on a house
     */
    fun getArgalaCausingPlanets(chart: VedicChart, house: Int, language: Language): List<Planet> {
        val analysis = analyzeArgala(chart, language)
        val result = analysis.houseArgalas[house] ?: return emptyList()
        return result.primaryArgalas.flatMap { it.planets }.distinct()
    }
}
