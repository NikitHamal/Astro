package com.astro.storm.ephemeris

import com.astro.storm.R
import com.astro.storm.data.model.Planet
import com.astro.storm.data.model.PlanetPosition
import com.astro.storm.data.model.VedicChart
import com.astro.storm.data.model.ZodiacSign
import com.astro.storm.util.LocalizableString

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
        val interpretation: LocalizableString
    )

    /**
     * Argala analysis from a specific planet
     */
    data class PlanetArgalaResult(
        val planet: Planet,
        val argalasReceived: List<ArgalaInfluence>,
        val virodhasReceived: List<VirodhaArgala>,
        val netStrength: Double,
        val interpretation: LocalizableString
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
        val description: LocalizableString
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
        val description: LocalizableString
    )

    /**
     * Effective Argala after considering Virodha
     */
    data class EffectiveArgala(
        val netBeneficStrength: Double,
        val netMaleficStrength: Double,
        val dominantNature: ArgalaNature,
        val isSignificant: Boolean,
        val summary: LocalizableString
    )

    /**
     * Significant Argala formations in the chart
     */
    data class SignificantArgala(
        val targetHouse: Int,
        val targetDescription: LocalizableString,
        val argalaType: ArgalaType,
        val nature: ArgalaNature,
        val strength: ArgalaStrength,
        val involvedPlanets: List<Planet>,
        val lifeAreaEffect: LocalizableString,
        val recommendation: LocalizableString
    )

    /**
     * Overall assessment of Argala patterns in the chart
     */
    data class OverallArgalaAssessment(
        val strongestBeneficArgala: Int?,
        val strongestMaleficArgala: Int?,
        val mostObstructedHouse: Int?,
        val leastObstructedHouse: Int?,
        val karmaPatterns: List<LocalizableString>,
        val strengthDistribution: Map<Int, Double>,
        val generalRecommendations: List<LocalizableString>
    )

    // ============================================
    // ENUMS
    // ============================================

    enum class SourceType {
        HOUSE,      // Argala from a house
        PLANET      // Argala from a planet
    }

    enum class ArgalaType(val offset: Int, val virodhaOffset: Int, val displayName: LocalizableString) {
        SECONDARY_ARGALA(2, 12, LocalizableString.Res(R.string.argala_type_secondary)),
        PRIMARY_ARGALA(4, 10, LocalizableString.Res(R.string.argala_type_primary)),
        GAINS_ARGALA(11, 3, LocalizableString.Res(R.string.argala_type_gains)),
        SPECIAL_ARGALA(5, 9, LocalizableString.Res(R.string.argala_type_special))
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
    fun analyzeArgala(chart: VedicChart): ArgalaAnalysis {
        // Group planets by house
        val planetsByHouse = chart.planetPositions.groupBy { it.house }

        // Analyze Argala for each house
        val houseArgalas = (1..12).associateWith { house ->
            analyzeHouseArgala(house, planetsByHouse, chart)
        }

        // Analyze Argala received by each planet
        val planetArgalas = Planet.entries
            .filter { it in Planet.MAIN_PLANETS }
            .mapNotNull { planet ->
                chart.planetPositions.find { it.planet == planet }?.let { pos ->
                    planet to analyzePlanetArgala(pos, planetsByHouse, chart)
                }
            }.toMap()

        // Identify significant Argalas
        val significantArgalas = identifySignificantArgalas(houseArgalas, chart)

        // Generate overall assessment
        val overallAssessment = generateOverallAssessment(houseArgalas, chart)

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
        chart: VedicChart
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
                    targetHouse, argalaHouse, argalaType, argalaPlanets, chart
                )
                primaryArgalas.add(argalaInfluence)

                // Check for Virodha (obstruction)
                if (virodhaPlanets.isNotEmpty()) {
                    val virodha = calculateVirodhaArgala(
                        virodhaHouse, argalaHouse, virodhaPlanets, argalaInfluence, argalaType
                    )
                    virodhaArgalas.add(virodha)
                }
            }
        }

        // Calculate effective Argala
        val effectiveArgala = calculateEffectiveArgala(primaryArgalas, virodhaArgalas)

        // Calculate net strength
        val netStrength = effectiveArgala.netBeneficStrength - effectiveArgala.netMaleficStrength

        // Generate interpretation
        val interpretation = generateHouseArgalaInterpretation(targetHouse, effectiveArgala, chart)

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
        chart: VedicChart
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
                    fromHouse, argalaHouse, argalaType, argalaPlanets, chart
                )
                argalasReceived.add(argalaInfluence)

                if (virodhaPlanets.isNotEmpty()) {
                    val virodha = calculateVirodhaArgala(
                        virodhaHouse, argalaHouse, virodhaPlanets, argalaInfluence, argalaType
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

        val interpretation = generatePlanetArgalaInterpretation(planetPosition.planet, argalasReceived, netStrength)

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
        chart: VedicChart
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

        val planetNames = LocalizableString.Chain(
            planets.map { it.planet.displayName }.reduce { acc, localizableString ->
                LocalizableString.Chain(listOf(acc, LocalizableString.Raw(", "), localizableString))
            }
        )

        val description = LocalizableString.ResWithArgs(
            R.string.argala_influence_description,
            listOf(argalaType.displayName, argalaHouse, planetNames, nature.name.lowercase())
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
        argalaType: ArgalaType
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

        val planetNames = LocalizableString.Chain(
            virodhaPlanets.map { it.planet.displayName }.reduce { acc, localizableString ->
                LocalizableString.Chain(listOf(acc, LocalizableString.Raw(", "), localizableString))
            }
        )

        val description = LocalizableString.ResWithArgs(
            if (isEffective) R.string.virodha_argala_description_effective else R.string.virodha_argala_description_partial,
            listOf(virodhaHouse, obstructedArgalaHouse, planetNames)
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
        virodhaArgalas: List<VirodhaArgala>
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

        val summary = LocalizableString.Chain(
            listOf(
                when {
                    beneficStrength > maleficStrength * 2 -> LocalizableString.Res(R.string.effective_argala_summary_strong_auspicious)
                    maleficStrength > beneficStrength * 2 -> LocalizableString.Res(R.string.effective_argala_summary_challenging)
                    isSignificant -> LocalizableString.Res(R.string.effective_argala_summary_mixed)
                    else -> LocalizableString.Res(R.string.effective_argala_summary_minimal)
                },
                LocalizableString.Raw(" "),
                LocalizableString.ResWithArgs(R.string.effective_argala_summary_benefic, listOf(beneficStrength)),
                LocalizableString.Raw(", "),
                LocalizableString.ResWithArgs(R.string.effective_argala_summary_malefic, listOf(maleficStrength))
            )
        )

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
        chart: VedicChart
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
                                targetDescription = getHouseDescription(house),
                                argalaType = argala.argalaType,
                                nature = argala.nature,
                                strength = effectiveStrength,
                                involvedPlanets = argala.planets,
                                lifeAreaEffect = getLifeAreaEffect(house, argala.nature, argala.argalaType),
                                recommendation = getArgalaRecommendation(house, argala.nature, argala.planets)
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
        chart: VedicChart
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

        val karmaPatterns = generateKarmaPatterns(houseArgalas, chart)
        val recommendations = generateGeneralRecommendations(houseArgalas, chart)

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
        chart: VedicChart
    ): LocalizableString {
        val houseTheme = getHouseDescription(house)

        return LocalizableString.Chain(
            listOf(
                houseTheme,
                LocalizableString.Raw(": "),
                when (effectiveArgala.dominantNature) {
                    ArgalaNature.SHUBHA -> LocalizableString.Res(R.string.house_argala_interpretation_beneficial)
                    ArgalaNature.ASHUBHA -> LocalizableString.Res(R.string.house_argala_interpretation_challenging)
                    ArgalaNature.MIXED -> LocalizableString.Res(R.string.house_argala_interpretation_mixed)
                },
                LocalizableString.Raw(" "),
                effectiveArgala.summary
            )
        )
    }

    private fun generatePlanetArgalaInterpretation(
        planet: Planet,
        argalas: List<ArgalaInfluence>,
        netStrength: Double
    ): LocalizableString {
        return LocalizableString.Chain(
            listOfNotNull(
                planet.displayName,
                LocalizableString.Raw(": "),
                when {
                    netStrength > 1.0 -> LocalizableString.Res(R.string.planet_argala_interpretation_strongly_supported)
                    netStrength > 0 -> LocalizableString.Res(R.string.planet_argala_interpretation_moderately_supported)
                    netStrength > -1.0 -> LocalizableString.Res(R.string.planet_argala_interpretation_some_obstruction)
                    else -> LocalizableString.Res(R.string.planet_argala_interpretation_significantly_challenged)
                },
                if (argalas.isNotEmpty()) LocalizableString.ResWithArgs(
                    R.string.planet_argala_interpretation_influence_count,
                    listOf(argalas.size)
                ) else null
            )
        )
    }

    private fun generateKarmaPatterns(
        houseArgalas: Map<Int, HouseArgalaResult>,
        chart: VedicChart
    ): List<LocalizableString> {
        val patterns = mutableListOf<LocalizableString>()

        // Check Dharma houses (1, 5, 9)
        val dharmaStrength = listOf(1, 5, 9).sumOf { houseArgalas[it]?.netArgalaStrength ?: 0.0 }
        if (dharmaStrength > 3.0) {
            patterns.add(LocalizableString.Res(R.string.karma_pattern_dharmic_support))
        } else if (dharmaStrength < -1.0) {
            patterns.add(LocalizableString.Res(R.string.karma_pattern_dharmic_strengthening))
        }

        // Check Artha houses (2, 6, 10)
        val arthaStrength = listOf(2, 6, 10).sumOf { houseArgalas[it]?.netArgalaStrength ?: 0.0 }
        if (arthaStrength > 3.0) {
            patterns.add(LocalizableString.Res(R.string.karma_pattern_artha_support))
        } else if (arthaStrength < -1.0) {
            patterns.add(LocalizableString.Res(R.string.karma_pattern_artha_obstacles))
        }

        // Check Kama houses (3, 7, 11)
        val kamaStrength = listOf(3, 7, 11).sumOf { houseArgalas[it]?.netArgalaStrength ?: 0.0 }
        if (kamaStrength > 3.0) {
            patterns.add(LocalizableString.Res(R.string.karma_pattern_kama_support))
        } else if (kamaStrength < -1.0) {
            patterns.add(LocalizableString.Res(R.string.karma_pattern_kama_challenges))
        }

        // Check Moksha houses (4, 8, 12)
        val mokshaStrength = listOf(4, 8, 12).sumOf { houseArgalas[it]?.netArgalaStrength ?: 0.0 }
        if (mokshaStrength > 3.0) {
            patterns.add(LocalizableString.Res(R.string.karma_pattern_moksha_support))
        }

        return patterns
    }

    private fun generateGeneralRecommendations(
        houseArgalas: Map<Int, HouseArgalaResult>,
        chart: VedicChart
    ): List<LocalizableString> {
        val recommendations = mutableListOf<LocalizableString>()

        // Check for houses needing remedies
        houseArgalas.forEach { (house, result) ->
            if (result.effectiveArgala.dominantNature == ArgalaNature.ASHUBHA &&
                result.effectiveArgala.netMaleficStrength > 1.5) {
                val remedy = when (house) {
                    1 -> LocalizableString.Res(R.string.recommendation_remedy_sun)
                    7 -> LocalizableString.Res(R.string.recommendation_remedy_venus)
                    10 -> LocalizableString.Res(R.string.recommendation_remedy_saturn)
                    else -> LocalizableString.ResWithArgs(R.string.recommendation_remedy_house_lord, listOf(house))
                }
                recommendations.add(remedy)
            }
        }

        // Add general advice based on patterns
        val totalBenefic = houseArgalas.values.sumOf { it.effectiveArgala.netBeneficStrength }
        val totalMalefic = houseArgalas.values.sumOf { it.effectiveArgala.netMaleficStrength }

        if (totalBenefic > totalMalefic * 1.5) {
            recommendations.add(LocalizableString.Res(R.string.recommendation_overall_favorable))
        } else if (totalMalefic > totalBenefic * 1.5) {
            recommendations.add(LocalizableString.Res(R.string.recommendation_overall_strengthen))
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
    private fun getHouseDescription(house: Int): LocalizableString {
        return when (house) {
            1 -> LocalizableString.Res(R.string.house_description_1)
            2 -> LocalizableString.Res(R.string.house_description_2)
            3 -> LocalizableString.Res(R.string.house_description_3)
            4 -> LocalizableString.Res(R.string.house_description_4)
            5 -> LocalizableString.Res(R.string.house_description_5)
            6 -> LocalizableString.Res(R.string.house_description_6)
            7 -> LocalizableString.Res(R.string.house_description_7)
            8 -> LocalizableString.Res(R.string.house_description_8)
            9 -> LocalizableString.Res(R.string.house_description_9)
            10 -> LocalizableString.Res(R.string.house_description_10)
            11 -> LocalizableString.Res(R.string.house_description_11)
            12 -> LocalizableString.Res(R.string.house_description_12)
            else -> LocalizableString.ResWithArgs(R.string.house_description_generic, listOf(house))
        }
    }

    /**
     * Get life area effect based on Argala
     */
    private fun getLifeAreaEffect(house: Int, nature: ArgalaNature, argalaType: ArgalaType): LocalizableString {
        val effect = if (nature == ArgalaNature.SHUBHA)
            LocalizableString.Res(R.string.life_area_effect_support)
        else
            LocalizableString.Res(R.string.life_area_effect_challenge)

        val source = when (argalaType) {
            ArgalaType.SECONDARY_ARGALA -> LocalizableString.Res(R.string.life_area_source_wealth)
            ArgalaType.PRIMARY_ARGALA -> LocalizableString.Res(R.string.life_area_source_happiness)
            ArgalaType.GAINS_ARGALA -> LocalizableString.Res(R.string.life_area_source_gains)
            ArgalaType.SPECIAL_ARGALA -> LocalizableString.Res(R.string.life_area_source_intelligence)
        }

        val area = when (house) {
            1 -> LocalizableString.Res(R.string.life_area_personality)
            7 -> LocalizableString.Res(R.string.life_area_relationships)
            10 -> LocalizableString.Res(R.string.life_area_career)
            4 -> LocalizableString.Res(R.string.life_area_home)
            5 -> LocalizableString.Res(R.string.life_area_children)
            else -> LocalizableString.ResWithArgs(R.string.life_area_house_matters, listOf(house))
        }

        return LocalizableString.ResWithArgs(R.string.life_area_effect_through_source, listOf(source, effect, area))
    }

    /**
     * Get recommendation for Argala
     */
    private fun getArgalaRecommendation(house: Int, nature: ArgalaNature, planets: List<Planet>): LocalizableString {
        return if (nature == ArgalaNature.ASHUBHA) {
            val mainPlanet = planets.firstOrNull()
            when (mainPlanet) {
                Planet.SATURN -> LocalizableString.Res(R.string.argala_recommendation_remedy_saturn)
                Planet.MARS -> LocalizableString.Res(R.string.argala_recommendation_remedy_mars)
                Planet.RAHU -> LocalizableString.Res(R.string.argala_recommendation_remedy_rahu)
                Planet.KETU -> LocalizableString.Res(R.string.argala_recommendation_remedy_ketu)
                else -> LocalizableString.Res(R.string.argala_recommendation_remedy_strengthen)
            }
        } else {
            LocalizableString.Res(R.string.argala_recommendation_leverage)
        }
    }

    // ============================================
    // UTILITY METHODS
    // ============================================

    /**
     * Get Argala summary for a specific house
     */
    fun getHouseSummary(chart: VedicChart, house: Int): LocalizableString {
        val analysis = analyzeArgala(chart)
        val houseResult = analysis.houseArgalas[house] ?: return LocalizableString.Res(R.string.argala_house_summary_no_data)
        return houseResult.interpretation
    }

    /**
     * Check if a house has strong Shubha Argala
     */
    fun hasStrongBeneficArgala(chart: VedicChart, house: Int): Boolean {
        val analysis = analyzeArgala(chart)
        val result = analysis.houseArgalas[house] ?: return false
        return result.effectiveArgala.dominantNature == ArgalaNature.SHUBHA &&
               result.effectiveArgala.netBeneficStrength >= 1.5
    }

    /**
     * Get planets causing Argala on a house
     */
    fun getArgalaCausingPlanets(chart: VedicChart, house: Int): List<Planet> {
        val analysis = analyzeArgala(chart)
        val result = analysis.houseArgalas[house] ?: return emptyList()
        return result.primaryArgalas.flatMap { it.planets }.distinct()
    }
}
