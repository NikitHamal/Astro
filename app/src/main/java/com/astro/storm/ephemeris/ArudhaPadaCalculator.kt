package com.astro.storm.ephemeris

import com.astro.storm.data.model.Planet
import com.astro.storm.data.model.PlanetPosition
import com.astro.storm.data.model.VedicChart
import com.astro.storm.data.model.ZodiacSign
import java.time.LocalDate
import kotlin.math.abs

/**
 * Arudha Pada Calculator (Jaimini System)
 *
 * Arudha Padas are the "image" or "manifestation" points that show how
 * the matters of each house manifest in the material world. They are
 * essential for understanding worldly perception and material outcomes.
 *
 * ## Calculation Method (per Jaimini Sutras)
 *
 * For a house H with lord L in position P:
 * 1. Count the distance from H to P (inclusive)
 * 2. Count the same distance forward from P
 * 3. The resulting sign is the Arudha Pada of H
 *
 * ## Exception Rules (Classical)
 * - If Arudha falls in the same house or 7th from it, move 10 signs forward
 * - Some scholars use: if in 1st/7th, then take 4th/10th respectively
 *
 * ## Important Arudhas
 * - A1 (Arudha Lagna/AL): Public image, status, maya
 * - A7 (Darapada): Business partnerships, trade
 * - A10 (Rajyapada): Career manifestation, authority
 * - A11 (Labha Pada): Gains, fulfillment of desires
 * - A12 (Upapada/UL): Spouse, marital matters
 *
 * ## References
 * - Jaimini Sutras (Chapter 1, Pada 1)
 * - BPHS Chapter 29-30 on Arudha
 * - Commentary by Raghunatha Bhatta
 * - Sanjay Rath's "Crux of Vedic Astrology"
 *
 * @author AstroStorm
 */
object ArudhaPadaCalculator {

    // ============================================
    // DATA CLASSES
    // ============================================

    /**
     * Complete Arudha Pada analysis result
     */
    data class ArudhaPadaAnalysis(
        val ascendantSign: ZodiacSign,
        val arudhaPadas: List<ArudhaPada>,
        val specialArudhas: SpecialArudhas,
        val arudhaYogas: List<ArudhaYoga>,
        val arudhaRelationships: List<ArudhaRelationship>,
        val transitEffects: List<ArudhaTransitEffect>,
        val dashaActivation: List<ArudhaDashaActivation>,
        val overallAssessment: ArudhaOverallAssessment,
        val interpretation: ArudhaInterpretation
    )

    /**
     * Individual Arudha Pada information
     */
    data class ArudhaPada(
        val house: Int,                    // Original house (1-12)
        val name: String,                  // e.g., "A1", "A7", "AL", "UL"
        val fullName: String,              // e.g., "Arudha Lagna", "Upapada"
        val sign: ZodiacSign,              // Sign where Arudha falls
        val signDegree: Double,            // Degree in the sign (0-30)
        val houseLord: Planet,             // Lord of the original house
        val houseLordSign: ZodiacSign,     // Sign where house lord is placed
        val houseLordHouse: Int,           // House where house lord is placed
        val planetsInArudha: List<PlanetPosition>,  // Planets in the Arudha sign
        val strength: ArudhaStrength,      // Strength assessment
        val significations: List<String>,  // What this Arudha signifies
        val interpretation: String         // Detailed interpretation
    )

    /**
     * Special Arudhas with detailed analysis
     */
    data class SpecialArudhas(
        val arudhaLagna: ArudhaPadaDetail,     // AL - Public image
        val upapada: ArudhaPadaDetail,          // UL - Spouse
        val darapada: ArudhaPadaDetail,         // A7 - Business/Partners
        val labhaPada: ArudhaPadaDetail,        // A11 - Gains
        val rajyaPada: ArudhaPadaDetail,        // A10 - Career/Authority
        val mantriPada: ArudhaPadaDetail,       // A5 - Intelligence/Counsel
        val shatruPada: ArudhaPadaDetail        // A6 - Enemies/Diseases
    )

    /**
     * Detailed Arudha information for special Arudhas
     */
    data class ArudhaPadaDetail(
        val arudha: ArudhaPada,
        val arudhaLord: Planet,
        val arudhaLordSign: ZodiacSign,
        val arudhaLordHouse: Int,
        val dignityOfLord: PlanetaryDignity,
        val beneficsInArudha: List<Planet>,
        val maleficsInArudha: List<Planet>,
        val aspectsOnArudha: List<AspectOnArudha>,
        val detailedInterpretation: DetailedArudhaInterpretation
    )

    /**
     * Yoga formed by Arudha positions
     */
    data class ArudhaYoga(
        val name: String,
        val type: ArudhaYogaType,
        val involvedArudhas: List<String>,
        val involvedSigns: List<ZodiacSign>,
        val strength: YogaStrength,
        val effects: String,
        val timing: String,
        val recommendations: List<String>
    )

    /**
     * Relationship between two Arudhas
     */
    data class ArudhaRelationship(
        val arudha1: String,
        val arudha2: String,
        val distanceInSigns: Int,
        val relationship: RelationshipType,
        val effect: String,
        val isPositive: Boolean
    )

    /**
     * Transit effects on Arudha positions
     */
    data class ArudhaTransitEffect(
        val arudha: String,
        val arudhaSign: ZodiacSign,
        val transitingPlanet: Planet,
        val transitSign: ZodiacSign,
        val aspectType: String,
        val effect: String,
        val intensity: EffectIntensity,
        val approximateTiming: String
    )

    /**
     * Dasha activation of Arudha matters
     */
    data class ArudhaDashaActivation(
        val arudha: String,
        val activatingPeriod: String,
        val activatingPlanet: Planet,
        val activationReason: String,
        val expectedEffects: List<String>,
        val timing: String
    )

    /**
     * Overall assessment of Arudha placements
     */
    data class ArudhaOverallAssessment(
        val publicImageStrength: Int,      // 1-100
        val materialSuccessIndicator: Int,  // 1-100
        val relationshipIndicator: Int,     // 1-100
        val careerManifestationStrength: Int, // 1-100
        val gainsAndFulfillment: Int,      // 1-100
        val overallMayaStrength: Int,      // 1-100 - how strongly material world manifests
        val keyThemes: List<String>,
        val strengthAreas: List<String>,
        val challengeAreas: List<String>
    )

    /**
     * Complete interpretation
     */
    data class ArudhaInterpretation(
        val summary: String,
        val publicPerception: String,
        val materialLife: String,
        val relationshipManifestation: String,
        val careerAndAuthority: String,
        val recommendations: List<String>
    )

    // Supporting types
    data class AspectOnArudha(
        val planet: Planet,
        val aspectType: String,
        val nature: String,  // Benefic/Malefic
        val effect: String
    )

    data class DetailedArudhaInterpretation(
        val primaryMeaning: String,
        val secondaryEffects: List<String>,
        val timingOfResults: String,
        val remedialMeasures: List<String>
    )

    enum class ArudhaStrength {
        VERY_STRONG,  // Benefics in Arudha, lord well-placed
        STRONG,       // Good placement, some support
        MODERATE,     // Mixed influences
        WEAK,         // Malefic influences
        VERY_WEAK     // Severely afflicted
    }

    enum class ArudhaYogaType {
        RAJA_YOGA,        // Royal combinations from Arudhas
        DHANA_YOGA,       // Wealth combinations
        PARIVARTANA,      // Exchange between Arudhas
        ARGALA_YOGA,      // Intervention on Arudha
        BHAVA_YOGA,       // House-based combinations
        GRAHA_YOGA        // Planet-based combinations
    }

    enum class YogaStrength {
        EXCEPTIONAL, STRONG, MODERATE, MILD, WEAK
    }

    enum class RelationshipType {
        CONJUNCTION,      // Same sign
        TRINE,           // 1st, 5th, 9th
        KENDRA,          // 1st, 4th, 7th, 10th
        OPPOSITION,      // 7th
        DUSTHANA,        // 6th, 8th, 12th
        UPACHAYA,        // 3rd, 6th, 10th, 11th
        NEUTRAL          // Other
    }

    enum class EffectIntensity {
        VERY_HIGH, HIGH, MODERATE, LOW, NEGLIGIBLE
    }

    enum class PlanetaryDignity {
        EXALTED, OWN_SIGN, MOOLATRIKONA, FRIEND_SIGN, NEUTRAL_SIGN, ENEMY_SIGN, DEBILITATED
    }

    // ============================================
    // MAIN CALCULATION
    // ============================================

    /**
     * Calculate complete Arudha Pada analysis
     */
    fun analyzeArudhaPadas(chart: VedicChart, language: Language = Language.ENGLISH): ArudhaPadaAnalysis {
        val ascendantSign = ZodiacSign.fromLongitude(chart.ascendant)

        // Calculate all 12 Arudha Padas
        val arudhaPadas = (1..12).map { house ->
            calculateArudhaPada(chart, house, ascendantSign, language)
        }

        // Get special Arudhas with detailed analysis
        val specialArudhas = analyzeSpecialArudhas(chart, arudhaPadas, language)

        // Calculate Arudha Yogas
        val arudhaYogas = calculateArudhaYogas(chart, arudhaPadas, specialArudhas, language)

        // Analyze Arudha-to-Arudha relationships
        val arudhaRelationships = analyzeArudhaRelationships(arudhaPadas)

        // Calculate transit effects on Arudhas
        val transitEffects = calculateTransitEffects(chart, arudhaPadas)

        // Calculate Dasha activation
        val dashaActivation = calculateDashaActivation(chart, arudhaPadas)

        // Overall assessment
        val overallAssessment = calculateOverallAssessment(chart, arudhaPadas, specialArudhas, arudhaYogas, language)
 
        // Generate interpretation
        val interpretation = generateInterpretation(chart, arudhaPadas, specialArudhas, arudhaYogas, overallAssessment, language)

        return ArudhaPadaAnalysis(
            ascendantSign = ascendantSign,
            arudhaPadas = arudhaPadas,
            specialArudhas = specialArudhas,
            arudhaYogas = arudhaYogas,
            arudhaRelationships = arudhaRelationships,
            transitEffects = transitEffects,
            dashaActivation = dashaActivation,
            overallAssessment = overallAssessment,
            interpretation = interpretation
        )
    }

    /**
     * Calculate Arudha Pada for a specific house
     */
    private fun calculateArudhaPada(
        chart: VedicChart,
        house: Int,
        ascendantSign: ZodiacSign,
        language: Language
    ): ArudhaPada {
        // Get the sign of the house
        val houseSign = getSignOfHouse(ascendantSign, house)

        // Get the lord of that sign
        val houseLord = houseSign.ruler

        // Find where the house lord is placed
        val houseLordPosition = chart.planetPositions.find { it.planet == houseLord }
        val houseLordSign = houseLordPosition?.sign ?: houseSign
        val houseLordHouse = houseLordPosition?.house ?: house

        // Calculate Arudha position
        // Step 1: Count from house to house lord's position
        val houseSignOrdinal = houseSign.ordinal
        val lordSignOrdinal = houseLordSign.ordinal
        val distanceToLord = ((lordSignOrdinal - houseSignOrdinal + 12) % 12) + 1

        // Step 2: Count same distance from lord's position
        var arudhaOrdinal = (lordSignOrdinal + distanceToLord - 1) % 12
        var arudhaSign = ZodiacSign.entries[arudhaOrdinal]

        // Exception: If Arudha falls in same house or 7th from it
        val arudhaHouseFromOriginal = ((arudhaOrdinal - houseSignOrdinal + 12) % 12) + 1
        if (arudhaHouseFromOriginal == 1 || arudhaHouseFromOriginal == 7) {
            // Move 10 signs forward from original position
            arudhaOrdinal = (arudhaOrdinal + 9) % 12  // 10 signs = 9 indices
            arudhaSign = ZodiacSign.entries[arudhaOrdinal]
        }

        // Find planets in the Arudha sign
        val planetsInArudha = chart.planetPositions.filter { it.sign == arudhaSign }

        // Calculate strength
        val strength = calculateArudhaStrength(chart, arudhaSign, houseLord, houseLordPosition)

        // Get name and full name
        val (name, fullName) = getArudhaName(house, language)

        // Get significations
        val significations = getHouseSignifications(house, language)

        // Generate interpretation
        val interpretation = generateArudhaPadaInterpretation(
            house, arudhaSign, houseLord, houseLordSign, planetsInArudha, strength, language
        )

        return ArudhaPada(
            house = house,
            name = name,
            fullName = fullName,
            sign = arudhaSign,
            signDegree = 15.0, // Middle of sign as default
            houseLord = houseLord,
            houseLordSign = houseLordSign,
            houseLordHouse = houseLordHouse,
            planetsInArudha = planetsInArudha,
            strength = strength,
            significations = significations,
            interpretation = interpretation
        )
    }

    /**
     * Get the sign of a house based on ascendant
     */
    private fun getSignOfHouse(ascendant: ZodiacSign, house: Int): ZodiacSign {
        return ZodiacSign.entries[(ascendant.ordinal + house - 1) % 12]
    }

    /**
     * Calculate strength of Arudha placement
     */
    private fun calculateArudhaStrength(
        chart: VedicChart,
        arudhaSign: ZodiacSign,
        houseLord: Planet,
        houseLordPosition: PlanetPosition?
    ): ArudhaStrength {
        var strengthScore = 50 // Start at moderate

        // Check planets in Arudha sign
        val planetsInArudha = chart.planetPositions.filter { it.sign == arudhaSign }

        val benefics = listOf(Planet.JUPITER, Planet.VENUS, Planet.MERCURY, Planet.MOON)
        val malefics = listOf(Planet.SATURN, Planet.MARS, Planet.RAHU, Planet.KETU, Planet.SUN)

        // Benefics in Arudha increase strength
        val beneficCount = planetsInArudha.count { it.planet in benefics }
        strengthScore += beneficCount * 10

        // Malefics in Arudha decrease strength
        val maleficCount = planetsInArudha.count { it.planet in malefics }
        strengthScore -= maleficCount * 10

        // Check house lord's dignity
        houseLordPosition?.let { pos ->
            when {
                isExalted(pos.planet, pos.sign) -> strengthScore += 15
                isOwnSign(pos.planet, pos.sign) -> strengthScore += 10
                isDebilitated(pos.planet, pos.sign) -> strengthScore -= 15
            }
        }

        // Check if lord aspects Arudha
        houseLordPosition?.let { pos ->
            if (aspectsSign(pos.planet, pos.sign, arudhaSign)) {
                strengthScore += 10
            }
        }

        return when {
            strengthScore >= 80 -> ArudhaStrength.VERY_STRONG
            strengthScore >= 65 -> ArudhaStrength.STRONG
            strengthScore >= 45 -> ArudhaStrength.MODERATE
            strengthScore >= 30 -> ArudhaStrength.WEAK
            else -> ArudhaStrength.VERY_WEAK
        }
    }

    /**
     * Get Arudha name and full name based on house
     */
    private fun getArudhaName(house: Int, language: Language): Pair<String, String> {
        return when (house) {
            1 -> StringResources.get(StringKeyAnalysis.ARUDHA_NAME_AL, language) to StringResources.get(StringKeyAnalysis.ARUDHA_FULL_AL, language)
            7 -> StringResources.get(StringKeyAnalysis.ARUDHA_NAME_A7, language) to StringResources.get(StringKeyAnalysis.ARUDHA_FULL_A7, language)
            10 -> StringResources.get(StringKeyAnalysis.ARUDHA_NAME_A10, language) to StringResources.get(StringKeyAnalysis.ARUDHA_FULL_A10, language)
            11 -> StringResources.get(StringKeyAnalysis.ARUDHA_NAME_A11, language) to StringResources.get(StringKeyAnalysis.ARUDHA_FULL_A11, language)
            12 -> StringResources.get(StringKeyAnalysis.ARUDHA_NAME_UL, language) to StringResources.get(StringKeyAnalysis.ARUDHA_FULL_UL, language)
            else -> "A$house" to StringResources.get(StringKeyAnalysis.ARUDHA_GENERIC_FULL, language, house)
        }
    }

    /**
     * Get significations for each house
     */
    private fun getHouseSignifications(house: Int, language: Language): List<String> {
        return when (house) {
            1 -> listOf(
                StringResources.get(StringKeyAnalysis.SIG_PUBLIC_IMAGE, language),
                StringResources.get(StringKeyAnalysis.SIG_PHYSICAL_APP, language),
                StringResources.get(StringKeyAnalysis.SIG_PERCEPTION, language),
                StringResources.get(StringKeyAnalysis.SIG_MAYA_SELF, language)
            )
            2 -> listOf(
                StringResources.get(StringKeyAnalysis.SIG_WEALTH_PERC, language),
                StringResources.get(StringKeyAnalysis.SIG_FAMILY_STATUS, language),
                StringResources.get(StringKeyAnalysis.SIG_SPEECH_COMM, language),
                StringResources.get(StringKeyAnalysis.SIG_FOOD_HABITS, language)
            )
            3 -> listOf(
                StringResources.get(StringKeyAnalysis.SIG_COURAGE_DISP, language),
                StringResources.get(StringKeyAnalysis.SIG_SIBLINGS_STATUS, language),
                StringResources.get(StringKeyAnalysis.SIG_COMM_SKILLS, language),
                StringResources.get(StringKeyAnalysis.SIG_SHORT_TRAVELS, language)
            )
            4 -> listOf(
                StringResources.get(StringKeyAnalysis.SIG_PROPERTY_MATTERS, language),
                StringResources.get(StringKeyAnalysis.SIG_MOTHER_IMAGE, language),
                StringResources.get(StringKeyAnalysis.SIG_VEHICLES, language),
                StringResources.get(StringKeyAnalysis.SIG_COMFORT_LUXURY, language)
            )
            5 -> listOf(
                StringResources.get(StringKeyAnalysis.SIG_INTEL_DISP, language),
                StringResources.get(StringKeyAnalysis.SIG_CHILDREN_IMAGE, language),
                StringResources.get(StringKeyAnalysis.SIG_SPEC_GAINS, language),
                StringResources.get(StringKeyAnalysis.SIG_COUNSEL_GIVEN, language)
            )
            6 -> listOf(
                StringResources.get(StringKeyAnalysis.SIG_ENEMIES_STR, language),
                StringResources.get(StringKeyAnalysis.SIG_DISEASE_MANIF, language),
                StringResources.get(StringKeyAnalysis.SIG_DEBTS, language),
                StringResources.get(StringKeyAnalysis.SIG_SERVICE_COND, language)
            )
            7 -> listOf(
                StringResources.get(StringKeyAnalysis.SIG_BUS_PARTNERS, language),
                StringResources.get(StringKeyAnalysis.SIG_TRADE, language),
                StringResources.get(StringKeyAnalysis.SIG_PUBLIC_DEALINGS, language),
                StringResources.get(StringKeyAnalysis.SIG_MARRIAGE_MANIF, language)
            )
            8 -> listOf(
                StringResources.get(StringKeyAnalysis.SIG_HIDDEN_MATTERS, language),
                StringResources.get(StringKeyAnalysis.SIG_INSURANCE_LEGACY, language),
                StringResources.get(StringKeyAnalysis.SIG_TRANSFORMATION, language),
                StringResources.get(StringKeyAnalysis.SIG_RESEARCH, language)
            )
            9 -> listOf(
                StringResources.get(StringKeyAnalysis.SIG_GURU_IMAGE, language),
                StringResources.get(StringKeyAnalysis.SIG_FORTUNE_MANIF, language),
                StringResources.get(StringKeyAnalysis.SIG_RELIGIOUS_DISP, language),
                StringResources.get(StringKeyAnalysis.SIG_HIGHER_LEARNING, language)
            )
            10 -> listOf(
                StringResources.get(StringKeyAnalysis.SIG_CAREER_MANIF, language),
                StringResources.get(StringKeyAnalysis.SIG_REPUTATION, language),
                StringResources.get(StringKeyAnalysis.SIG_GOVT_DEALINGS, language),
                StringResources.get(StringKeyAnalysis.SIG_PUBLIC_DEALINGS, language) // Reused
            )
            11 -> listOf(
                StringResources.get(StringKeyAnalysis.SIG_GAINS_PROFITS, language),
                StringResources.get(StringKeyAnalysis.SIG_ELDER_SIBLINGS, language),
                StringResources.get(StringKeyAnalysis.SIG_GAINS_PROFITS, language), // Reference to Gains
                StringResources.get(StringKeyAnalysis.SIG_NETWORKS, language)
            )
            12 -> listOf(
                StringResources.get(StringKeyAnalysis.SIG_SPOUSE_CHAR, language),
                StringResources.get(StringKeyAnalysis.SIG_MARRIAGE_OUTCOME, language),
                StringResources.get(StringKeyAnalysis.SIG_BEDROOM_MATTERS, language),
                StringResources.get(StringKeyAnalysis.SIG_FOREIGN_LANDS, language)
            )
            else -> listOf()
        }
    }

    /**
     * Generate interpretation for individual Arudha Pada
     */
    private fun generateArudhaPadaInterpretation(
        house: Int,
        arudhaSign: ZodiacSign,
        houseLord: Planet,
        houseLordSign: ZodiacSign,
        planetsInArudha: List<PlanetPosition>,
        strength: ArudhaStrength,
        language: Language
    ): String {
        val houseArea = when (house) {
            1 -> StringResources.get(StringKeyAnalysis.ARUDHA_AREA_1, language)
            2 -> StringResources.get(StringKeyAnalysis.ARUDHA_AREA_2, language)
            3 -> StringResources.get(StringKeyAnalysis.ARUDHA_AREA_3, language)
            4 -> StringResources.get(StringKeyAnalysis.ARUDHA_AREA_4, language)
            5 -> StringResources.get(StringKeyAnalysis.ARUDHA_AREA_5, language)
            6 -> StringResources.get(StringKeyAnalysis.ARUDHA_AREA_6, language)
            7 -> StringResources.get(StringKeyAnalysis.ARUDHA_AREA_7, language)
            8 -> StringResources.get(StringKeyAnalysis.ARUDHA_AREA_8, language)
            9 -> StringResources.get(StringKeyAnalysis.ARUDHA_AREA_9, language)
            10 -> StringResources.get(StringKeyAnalysis.ARUDHA_AREA_10, language)
            11 -> StringResources.get(StringKeyAnalysis.ARUDHA_AREA_11, language)
            12 -> StringResources.get(StringKeyAnalysis.ARUDHA_AREA_12, language)
            else -> StringResources.get(StringKeyMatch.HOUSE_LABEL, language, house)
        }

        val signNature = when (arudhaSign.element) {
            "Fire" -> StringResources.get(StringKeyAnalysis.NATURE_FIRE_VISIBLE, language)
            "Earth" -> StringResources.get(StringKeyAnalysis.NATURE_EARTH_PRACTICAL, language)
            "Air" -> StringResources.get(StringKeyAnalysis.NATURE_AIR_SOCIAL, language)
            "Water" -> StringResources.get(StringKeyAnalysis.NATURE_WATER_INTUITIVE, language)
            else -> StringResources.get(StringKeyAnalysis.ARGALA_MIXED_NATURE, language)
        }

        val strengthDesc = when (strength) {
            ArudhaStrength.VERY_STRONG -> StringResources.get(StringKeyAnalysis.ARUDHA_STR_VERY_STRONG, language)
            ArudhaStrength.STRONG -> StringResources.get(StringKeyAnalysis.ARUDHA_STR_STRONG, language)
            ArudhaStrength.MODERATE -> StringResources.get(StringKeyAnalysis.ARUDHA_STR_MODERATE, language)
            ArudhaStrength.WEAK -> StringResources.get(StringKeyAnalysis.ARUDHA_STR_WEAK, language)
            ArudhaStrength.VERY_WEAK -> StringResources.get(StringKeyAnalysis.ARUDHA_STR_VERY_WEAK, language)
        }

        val planetEffects = if (planetsInArudha.isNotEmpty()) {
            val planetNames = planetsInArudha.joinToString(", ") { it.planet.getLocalizedName(language) }
            StringResources.get(StringKeyAnalysis.ARGALA_PLANETS_INFLUENCE, language, planetNames, houseArea)
        } else {
            StringResources.get(StringKeyAnalysis.ARGALA_NO_PLANETS, language)
        }

        val signName = arudhaSign.getLocalizedName(language)
        val lordName = houseLord.getLocalizedName(language)
        val lordSignName = houseLordSign.getLocalizedName(language)

        return StringResources.get(StringKeyAnalysis.ARGALA_HOUSE_INTERP_DESC, language, houseArea, signName, signNature) + " " +
                StringResources.get(StringKeyAnalysis.ARGALA_LORD_PLACEMENT, language, lordName, lordSignName) + ". " +
                "$strengthDesc. $planetEffects"
    }

    // ============================================
    // SPECIAL ARUDHAS ANALYSIS
    // ============================================

    private fun analyzeSpecialArudhas(
        chart: VedicChart,
        arudhaPadas: List<ArudhaPada>,
        language: Language
    ): SpecialArudhas {
        return SpecialArudhas(
            arudhaLagna = analyzeSpecialArudha(chart, arudhaPadas[0], language),     // AL - House 1
            upapada = analyzeSpecialArudha(chart, arudhaPadas[11], language),         // UL - House 12
            darapada = analyzeSpecialArudha(chart, arudhaPadas[6], language),         // A7 - House 7
            labhaPada = analyzeSpecialArudha(chart, arudhaPadas[10], language),       // A11 - House 11
            rajyaPada = analyzeSpecialArudha(chart, arudhaPadas[9], language),        // A10 - House 10
            mantriPada = analyzeSpecialArudha(chart, arudhaPadas[4], language),       // A5 - House 5
            shatruPada = analyzeSpecialArudha(chart, arudhaPadas[5], language)        // A6 - House 6
        )
    }

    private fun analyzeSpecialArudha(
        chart: VedicChart,
        arudha: ArudhaPada,
        language: Language
    ): ArudhaPadaDetail {
        val arudhaLord = arudha.sign.ruler
        val arudhaLordPosition = chart.planetPositions.find { it.planet == arudhaLord }
        val arudhaLordSign = arudhaLordPosition?.sign ?: arudha.sign
        val arudhaLordHouse = arudhaLordPosition?.house ?: 1

        val dignity = calculatePlanetDignity(arudhaLord, arudhaLordSign)

        val benefics = listOf(Planet.JUPITER, Planet.VENUS, Planet.MERCURY, Planet.MOON)
        val malefics = listOf(Planet.SATURN, Planet.MARS, Planet.RAHU, Planet.KETU, Planet.SUN)

        val beneficsInArudha = arudha.planetsInArudha
            .filter { it.planet in benefics }
            .map { it.planet }

        val maleficsInArudha = arudha.planetsInArudha
            .filter { it.planet in malefics }
            .map { it.planet }

        val aspectsOnArudha = calculateAspectsOnSign(chart, arudha.sign, language)

        val detailedInterpretation = generateDetailedArudhaInterpretation(
            arudha, arudhaLord, dignity, beneficsInArudha, maleficsInArudha, aspectsOnArudha, language
        )

        return ArudhaPadaDetail(
            arudha = arudha,
            arudhaLord = arudhaLord,
            arudhaLordSign = arudhaLordSign,
            arudhaLordHouse = arudhaLordHouse,
            dignityOfLord = dignity,
            beneficsInArudha = beneficsInArudha,
            maleficsInArudha = maleficsInArudha,
            aspectsOnArudha = aspectsOnArudha,
            detailedInterpretation = detailedInterpretation
        )
    }

    private fun calculatePlanetDignity(planet: Planet, sign: ZodiacSign): PlanetaryDignity {
        return when {
            isExalted(planet, sign) -> PlanetaryDignity.EXALTED
            isDebilitated(planet, sign) -> PlanetaryDignity.DEBILITATED
            isOwnSign(planet, sign) -> PlanetaryDignity.OWN_SIGN
            isMoolatrikona(planet, sign) -> PlanetaryDignity.MOOLATRIKONA
            isFriendSign(planet, sign) -> PlanetaryDignity.FRIEND_SIGN
            isEnemySign(planet, sign) -> PlanetaryDignity.ENEMY_SIGN
            else -> PlanetaryDignity.NEUTRAL_SIGN
        }
    }

    private fun calculateAspectsOnSign(chart: VedicChart, sign: ZodiacSign, language: Language): List<AspectOnArudha> {
        val aspects = mutableListOf<AspectOnArudha>()

        chart.planetPositions.forEach { pos ->
            if (aspectsSign(pos.planet, pos.sign, sign)) {
                val benefics = listOf(Planet.JUPITER, Planet.VENUS, Planet.MERCURY)
                val nature = if (pos.planet in benefics) StringResources.get(StringKeyMatch.ASPECT_NATURE_HARMONIOUS, language) else StringResources.get(StringKeyMatch.ASPECT_NATURE_CHALLENGING, language)
                val effect = when (pos.planet) {
                    Planet.JUPITER -> StringResources.get(StringKeyAnalysis.ARGALA_JUPITER_EXPANSION, language)
                    Planet.VENUS -> StringResources.get(StringKeyAnalysis.ARGALA_VENUS_BEAUTY, language)
                    Planet.MARS -> StringResources.get(StringKeyAnalysis.ARGALA_MARS_CONFLICT, language)
                    Planet.SATURN -> StringResources.get(StringKeyAnalysis.ARGALA_SATURN_DELAY, language)
                    Planet.SUN -> StringResources.get(StringKeyAnalysis.ARGALA_SUN_AUTHORITY, language)
                    Planet.MOON -> StringResources.get(StringKeyAnalysis.ARGALA_MOON_EMOTION, language)
                    Planet.MERCURY -> StringResources.get(StringKeyAnalysis.ARGALA_MERCURY_COMM, language)
                    Planet.RAHU -> StringResources.get(StringKeyAnalysis.ARGALA_RAHU_ILLUSION, language)
                    Planet.KETU -> StringResources.get(StringKeyAnalysis.ARGALA_KETU_DETACHMENT, language)
                    else -> StringResources.get(StringKeyAnalysis.ARGALA_GENERAL_EFFECT, language)
                }
                aspects.add(AspectOnArudha(pos.planet, "Graha Drishti", nature, effect))
            }
        }

        return aspects
    }

    private fun generateDetailedArudhaInterpretation(
        arudha: ArudhaPada,
        lord: Planet,
        dignity: PlanetaryDignity,
        benefics: List<Planet>,
        malefics: List<Planet>,
        aspects: List<AspectOnArudha>,
        language: Language
    ): DetailedArudhaInterpretation {
        val primaryMeaning = when (arudha.house) {
            1 -> StringResources.get(StringKeyAnalysis.ARUDHA_AREA_1, language) + " (" + arudha.sign.getLocalizedName(language) + "): " + getSignImageDescription(arudha.sign, language)
            7 -> StringResources.get(StringKeyAnalysis.ARUDHA_AREA_7, language) + " (" + arudha.sign.getLocalizedName(language) + "): " + getA7BusinessDescription(arudha.sign, language)
            10 -> StringResources.get(StringKeyAnalysis.ARUDHA_AREA_10, language) + " (" + arudha.sign.getLocalizedName(language) + "): " + getA10CareerDescription(arudha.sign, language)
            11 -> StringResources.get(StringKeyAnalysis.ARUDHA_AREA_11, language) + " (" + arudha.sign.getLocalizedName(language) + "): " + getA11GainsDescription(arudha.sign, language)
            12 -> StringResources.get(StringKeyAnalysis.ARUDHA_AREA_12, language) + " (" + arudha.sign.getLocalizedName(language) + "): " + getULSpouseDescription(arudha.sign, language)
            else -> StringResources.get(StringKeyMatch.HOUSE_LABEL, language, arudha.house) + " (" + arudha.sign.getLocalizedName(language) + ")"
        }

        val secondaryEffects = mutableListOf<String>()

        if (benefics.isNotEmpty()) {
            secondaryEffects.add(StringResources.get(StringKeyAnalysis.ARGALA_PLANETS_INFLUENCE, language, benefics.joinToString { it.getLocalizedName(language) }, ""))
        }
        if (malefics.isNotEmpty()) {
            secondaryEffects.add(StringResources.get(StringKeyAnalysis.ARGALA_PLANETS_INFLUENCE, language, malefics.joinToString { it.getLocalizedName(language) }, ""))
        }

        val dignityEffect = dignity.name
        secondaryEffects.add(dignityEffect)

        val timingOfResults = "Results manifest strongly during ${lord.displayName} dasha/antardasha, " +
                "and when transits activate ${arudha.sign.displayName}"

        val remedialMeasures = generateRemediesForArudha(arudha, dignity, malefics)

        return DetailedArudhaInterpretation(
            primaryMeaning = primaryMeaning,
            secondaryEffects = secondaryEffects,
            timingOfResults = timingOfResults,
            remedialMeasures = remedialMeasures
        )
    }

    private fun getSignImageDescription(sign: ZodiacSign, language: Language): String {
        return when (sign) {
            ZodiacSign.ARIES -> StringResources.get(StringKeyAnalysis.SIG_DESC_ARIES, language)
            ZodiacSign.TAURUS -> StringResources.get(StringKeyAnalysis.SIG_DESC_TAURUS, language)
            ZodiacSign.GEMINI -> StringResources.get(StringKeyAnalysis.SIG_DESC_GEMINI, language)
            ZodiacSign.CANCER -> StringResources.get(StringKeyAnalysis.SIG_DESC_CANCER, language)
            ZodiacSign.LEO -> StringResources.get(StringKeyAnalysis.SIG_DESC_LEO, language)
            ZodiacSign.VIRGO -> StringResources.get(StringKeyAnalysis.SIG_DESC_VIRGO, language)
            ZodiacSign.LIBRA -> StringResources.get(StringKeyAnalysis.SIG_DESC_LIBRA, language)
            ZodiacSign.SCORPIO -> StringResources.get(StringKeyAnalysis.SIG_DESC_SCORPIO, language)
            ZodiacSign.SAGITTARIUS -> StringResources.get(StringKeyAnalysis.SIG_DESC_SAGITTARIUS, language)
            ZodiacSign.CAPRICORN -> StringResources.get(StringKeyAnalysis.SIG_DESC_CAPRICORN, language)
            ZodiacSign.AQUARIUS -> StringResources.get(StringKeyAnalysis.SIG_DESC_AQUARIUS, language)
            ZodiacSign.PISCES -> StringResources.get(StringKeyAnalysis.SIG_DESC_PISCES, language)
        }
    }

    private fun getA7BusinessDescription(sign: ZodiacSign, language: Language): String {
        return when (sign.element) {
            "Fire" -> StringResources.get(StringKeyAnalysis.A7_BUS_FIRE, language)
            "Earth" -> StringResources.get(StringKeyAnalysis.A7_BUS_EARTH, language)
            "Air" -> StringResources.get(StringKeyAnalysis.A7_BUS_AIR, language)
            "Water" -> StringResources.get(StringKeyAnalysis.A7_BUS_WATER, language)
            else -> StringResources.get(StringKeyAnalysis.ARGALA_GENERAL_EFFECT, language)
        }
    }

    private fun getA10CareerDescription(sign: ZodiacSign, language: Language): String {
        return when (sign.ruler) {
            Planet.SUN -> StringResources.get(StringKeyAnalysis.A10_CAR_SUN, language)
            Planet.MOON -> StringResources.get(StringKeyAnalysis.A10_CAR_MOON, language)
            Planet.MARS -> StringResources.get(StringKeyAnalysis.A10_CAR_MARS, language)
            Planet.MERCURY -> StringResources.get(StringKeyAnalysis.A10_CAR_MERCURY, language)
            Planet.JUPITER -> StringResources.get(StringKeyAnalysis.A10_CAR_JUPITER, language)
            Planet.VENUS -> StringResources.get(StringKeyAnalysis.A10_CAR_VENUS, language)
            Planet.SATURN -> StringResources.get(StringKeyAnalysis.A10_CAR_SATURN, language)
            else -> StringResources.get(StringKeyAnalysis.ARGALA_GENERAL_EFFECT, language)
        }
    }

    private fun getA11GainsDescription(sign: ZodiacSign, language: Language): String {
        return StringResources.get(StringKeyAnalysis.ARGALA_GAINS_ARGALA, language) + " (" + sign.getLocalizedName(language) + ")"
    }

    private fun getULSpouseDescription(sign: ZodiacSign, language: Language): String {
        return when (sign) {
            ZodiacSign.ARIES -> StringResources.get(StringKeyAnalysis.UL_SPOUSE_ARIES, language)
            ZodiacSign.TAURUS -> StringResources.get(StringKeyAnalysis.UL_SPOUSE_TAURUS, language)
            ZodiacSign.GEMINI -> StringResources.get(StringKeyAnalysis.UL_SPOUSE_GEMINI, language)
            ZodiacSign.CANCER -> StringResources.get(StringKeyAnalysis.UL_SPOUSE_CANCER, language)
            ZodiacSign.LEO -> StringResources.get(StringKeyAnalysis.UL_SPOUSE_LEO, language)
            ZodiacSign.VIRGO -> StringResources.get(StringKeyAnalysis.UL_SPOUSE_VIRGO, language)
            ZodiacSign.LIBRA -> StringResources.get(StringKeyAnalysis.UL_SPOUSE_LIBRA, language)
            ZodiacSign.SCORPIO -> StringResources.get(StringKeyAnalysis.UL_SPOUSE_SCORPIO, language)
            ZodiacSign.SAGITTARIUS -> StringResources.get(StringKeyAnalysis.UL_SPOUSE_SAGITTARIUS, language)
            ZodiacSign.CAPRICORN -> StringResources.get(StringKeyAnalysis.UL_SPOUSE_CAPRICORN, language)
            ZodiacSign.AQUARIUS -> StringResources.get(StringKeyAnalysis.UL_SPOUSE_AQUARIUS, language)
            ZodiacSign.PISCES -> StringResources.get(StringKeyAnalysis.UL_SPOUSE_PISCES, language)
        }
    }

    private fun generateRemediesForArudha(
        arudha: ArudhaPada,
        dignity: PlanetaryDignity,
        malefics: List<Planet>,
        language: Language
    ): List<String> {
        val remedies = mutableListOf<String>()

        // Remedies based on dignity
        if (dignity == PlanetaryDignity.DEBILITATED || dignity == PlanetaryDignity.ENEMY_SIGN) {
            remedies.add(StringResources.get(StringKeyMatch.REMEDIES_GEMSTONES, language) + ": " + arudha.sign.ruler.getLocalizedName(language))
            remedies.add(StringResources.get(StringKeyMatch.REMEDIES_MANTRAS, language) + ": " + getPlanetMantra(arudha.sign.ruler))
        }

        // Remedies for malefics in Arudha
        malefics.forEach { malefic ->
            when (malefic) {
                Planet.SATURN -> remedies.add("Feed crows on Saturdays to pacify Saturn's influence on ${arudha.name}")
                Planet.RAHU -> remedies.add("Donate to disadvantaged on Saturdays to reduce Rahu's illusions")
                Planet.KETU -> remedies.add("Spiritual practices and meditation help channel Ketu's energy positively")
                Planet.MARS -> remedies.add("Donate red items on Tuesdays to channel Mars's energy constructively")
                else -> {}
            }
        }

        // General remedies
        remedies.add("Worship the deity associated with ${arudha.sign.displayName}")
        remedies.add("Act with integrity in ${arudha.fullName} matters for better manifestation")

        return remedies.distinct()
    }

    private fun getPlanetMantra(planet: Planet): String {
        return when (planet) {
            Planet.SUN -> "Om Suryaya Namaha"
            Planet.MOON -> "Om Chandraya Namaha"
            Planet.MARS -> "Om Mangalaya Namaha"
            Planet.MERCURY -> "Om Budhaya Namaha"
            Planet.JUPITER -> "Om Gurave Namaha"
            Planet.VENUS -> "Om Shukraya Namaha"
            Planet.SATURN -> "Om Shanaischaraya Namaha"
            Planet.RAHU -> "Om Rahave Namaha"
            Planet.KETU -> "Om Ketave Namaha"
            else -> "Om Navagraha Namaha"
        }
    }

    // ============================================
    // ARUDHA YOGAS
    // ============================================

    private fun calculateArudhaYogas(
        chart: VedicChart,
        arudhaPadas: List<ArudhaPada>,
        specialArudhas: SpecialArudhas,
        language: Language
    ): List<ArudhaYoga> {
        val yogas = mutableListOf<ArudhaYoga>()

        val al = arudhaPadas[0]  // Arudha Lagna
        val a10 = arudhaPadas[9] // Rajya Pada
        val a11 = arudhaPadas[10] // Labha Pada
        val ul = arudhaPadas[11] // Upapada
        val a7 = arudhaPadas[6]  // Darapada

        // 1. Raja Yoga from AL-A10 connection
        val alToA10Distance = getSignDistance(al.sign, a10.sign)
        if (alToA10Distance in listOf(1, 5, 9)) { // Trine relationship
            yogas.add(ArudhaYoga(
                name = "Arudha Raja Yoga",
                type = ArudhaYogaType.RAJA_YOGA,
                involvedArudhas = listOf("AL", "A10"),
                involvedSigns = listOf(al.sign, a10.sign),
                strength = YogaStrength.STRONG,
                effects = "Public image and career are harmoniously connected. Recognition and authority manifest together.",
                timing = "Activates during dasha of AL or A10 lords",
                recommendations = listOf(
                    "Leverage public image for career advancement",
                    "Leadership roles bring recognition"
                )
            ))
        }

        // 2. Dhana Yoga from AL-A11 connection
        val alToA11Distance = getSignDistance(al.sign, a11.sign)
        if (alToA11Distance in listOf(1, 5, 9, 4, 7, 10)) { // Trine or Kendra
            yogas.add(ArudhaYoga(
                name = "Arudha Dhana Yoga",
                type = ArudhaYogaType.DHANA_YOGA,
                involvedArudhas = listOf("AL", "A11"),
                involvedSigns = listOf(al.sign, a11.sign),
                strength = if (alToA11Distance in listOf(1, 5, 9)) YogaStrength.STRONG else YogaStrength.MODERATE,
                effects = "Public image directly supports gains. Reputation brings financial success.",
                timing = "Strong during Jupiter and Venus transits to these Arudhas",
                recommendations = listOf(
                    "Build reputation to enhance income",
                    "Networking leads to financial opportunities"
                )
            ))
        }

        // 3. Marriage Yoga from AL-UL connection
        val alToULDistance = getSignDistance(al.sign, ul.sign)
        if (alToULDistance in listOf(1, 5, 9, 7)) {
            val strength = when (alToULDistance) {
                1 -> YogaStrength.EXCEPTIONAL
                5, 9 -> YogaStrength.STRONG
                7 -> YogaStrength.MODERATE
                else -> YogaStrength.MILD
            }
            yogas.add(ArudhaYoga(
                name = "Arudha Vivaha Yoga",
                type = ArudhaYogaType.BHAVA_YOGA,
                involvedArudhas = listOf("AL", "UL"),
                involvedSigns = listOf(al.sign, ul.sign),
                strength = strength,
                effects = "Public image and marriage are connected. Spouse enhances social standing.",
                timing = "Marriage timing indicated by UL lord dasha",
                recommendations = listOf(
                    "Spouse supports public image",
                    "Marriage enhances social status"
                )
            ))
        }

        // 4. Business Success from A7-A10-A11 connection
        val a7ToA10 = getSignDistance(a7.sign, a10.sign)
        val a7ToA11 = getSignDistance(a7.sign, a11.sign)
        if (a7ToA10 in listOf(1, 5, 9) || a7ToA11 in listOf(1, 5, 9)) {
            yogas.add(ArudhaYoga(
                name = "Vyapara Yoga",
                type = ArudhaYogaType.DHANA_YOGA,
                involvedArudhas = listOf("A7", "A10", "A11"),
                involvedSigns = listOf(a7.sign, a10.sign, a11.sign),
                strength = YogaStrength.STRONG,
                effects = "Business partnerships bring career growth and gains.",
                timing = "Mercury and Jupiter periods activate this yoga",
                recommendations = listOf(
                    "Business ventures are favored",
                    "Partnerships lead to gains"
                )
            ))
        }

        // 5. Check for planets creating Argala on AL
        chart.planetPositions.forEach { pos ->
            val distFromAL = getSignDistance(al.sign, pos.sign)
            if (distFromAL in listOf(2, 4, 11)) { // Argala positions
                val isJupiter = pos.planet == Planet.JUPITER
                val isVenus = pos.planet == Planet.VENUS
                if (isJupiter || isVenus) {
                    yogas.add(ArudhaYoga(
                        name = "Shubha Argala on Arudha Lagna",
                        type = ArudhaYogaType.ARGALA_YOGA,
                        involvedArudhas = listOf("AL"),
                        involvedSigns = listOf(al.sign, pos.sign),
                        strength = if (isJupiter) YogaStrength.STRONG else YogaStrength.MODERATE,
                        effects = StringResources.get(StringKeyAnalysis.ARGALA_PLANETS_INFLUENCE, language, pos.planet.getLocalizedName(language), "Arudha Lagna"),
                        timing = StringResources.get(StringKeyAnalysis.TRANSIT_INTERP_AVERAGE, language) + " " + pos.planet.getLocalizedName(language),
                        recommendations = listOf(
                            StringResources.get(StringKeyAnalysis.ARGALA_REMEDY_1, language, al.sign.getLocalizedName(language))
                        )
                    ))
                }
            }
        }

        return yogas
    }

    // ============================================
    // ARUDHA RELATIONSHIPS
    // ============================================

    private fun analyzeArudhaRelationships(arudhaPadas: List<ArudhaPada>): List<ArudhaRelationship> {
        val relationships = mutableListOf<ArudhaRelationship>()

        // Key relationships to analyze
        val keyPairs = listOf(
            0 to 9,   // AL - A10 (Image - Career)
            0 to 10,  // AL - A11 (Image - Gains)
            0 to 11,  // AL - UL (Image - Spouse)
            6 to 9,   // A7 - A10 (Business - Career)
            6 to 10,  // A7 - A11 (Business - Gains)
            9 to 10,  // A10 - A11 (Career - Gains)
            4 to 9,   // A5 - A10 (Intelligence - Career)
            11 to 6   // UL - A7 (Spouse - Business)
        )

        keyPairs.forEach { (idx1, idx2) ->
            val a1 = arudhaPadas[idx1]
            val a2 = arudhaPadas[idx2]
            val distance = getSignDistance(a1.sign, a2.sign)
            val relType = getRelationshipType(distance)

            val (effect, isPositive) = getRelationshipEffect(a1.name, a2.name, relType, distance)

            relationships.add(ArudhaRelationship(
                arudha1 = a1.name,
                arudha2 = a2.name,
                distanceInSigns = distance,
                relationship = relType,
                effect = effect,
                isPositive = isPositive
            ))
        }

        return relationships
    }

    private fun getRelationshipType(distance: Int): RelationshipType {
        return when (distance) {
            1 -> RelationshipType.CONJUNCTION
            5, 9 -> RelationshipType.TRINE
            4, 7, 10 -> RelationshipType.KENDRA
            7 -> RelationshipType.OPPOSITION
            6, 8, 12 -> RelationshipType.DUSTHANA
            3, 6, 10, 11 -> RelationshipType.UPACHAYA
            else -> RelationshipType.NEUTRAL
        }
    }

    private fun getRelationshipEffect(
        a1: String,
        a2: String,
        relType: RelationshipType,
        distance: Int
    ): Pair<String, Boolean> {
        val isPositive = relType in listOf(
            RelationshipType.CONJUNCTION,
            RelationshipType.TRINE,
            RelationshipType.KENDRA,
            RelationshipType.UPACHAYA
        )

        val effect = when (relType) {
            RelationshipType.CONJUNCTION -> "$a1 and $a2 are in same sign - their matters are closely intertwined"
            RelationshipType.TRINE -> "$a1 and $a2 support each other harmoniously (trine relationship)"
            RelationshipType.KENDRA -> "$a1 and $a2 have strong mutual influence (kendra relationship)"
            RelationshipType.OPPOSITION -> "$a1 and $a2 face each other - balance needed between these areas"
            RelationshipType.DUSTHANA -> "$a1 and $a2 have challenging relationship - one may affect other negatively"
            RelationshipType.UPACHAYA -> "$a1 and $a2 relationship improves with time"
            RelationshipType.NEUTRAL -> "$a1 and $a2 have neutral relationship"
        }

        return effect to isPositive
    }

    // ============================================
    // TRANSIT EFFECTS
    // ============================================

    private fun calculateTransitEffects(
        chart: VedicChart,
        arudhaPadas: List<ArudhaPada>
    ): List<ArudhaTransitEffect> {
        val effects = mutableListOf<ArudhaTransitEffect>()

        // Current transits would need real-time ephemeris data
        // For now, we provide general transit guidance for key Arudhas

        val keyArudhas = listOf(
            arudhaPadas[0],  // AL
            arudhaPadas[6],  // A7
            arudhaPadas[9],  // A10
            arudhaPadas[10], // A11
            arudhaPadas[11]  // UL
        )

        val slowPlanets = listOf(Planet.JUPITER, Planet.SATURN, Planet.RAHU, Planet.KETU)

        keyArudhas.forEach { arudha ->
            slowPlanets.forEach { planet ->
                val effect = getTransitEffectDescription(planet, arudha)
                effects.add(ArudhaTransitEffect(
                    arudha = arudha.name,
                    arudhaSign = arudha.sign,
                    transitingPlanet = planet,
                    transitSign = arudha.sign, // Transit over Arudha sign
                    aspectType = "Conjunction/Transit",
                    effect = effect,
                    intensity = if (planet == Planet.JUPITER || planet == Planet.SATURN)
                        EffectIntensity.HIGH else EffectIntensity.MODERATE,
                    approximateTiming = "${planet.displayName} transit through ${arudha.sign.displayName}"
                ))
            }
        }

        return effects
    }

    private fun getTransitEffectDescription(planet: Planet, arudha: ArudhaPada): String {
        val area = when (arudha.house) {
            1 -> "public image"
            7 -> "business and partnerships"
            10 -> "career and authority"
            11 -> "gains and profits"
            12 -> "spouse matters"
            else -> "house ${arudha.house} matters"
        }

        return when (planet) {
            Planet.JUPITER -> "Jupiter transit brings expansion, opportunities, and blessings to $area. Favorable for growth."
            Planet.SATURN -> "Saturn transit brings structure, delays, but lasting results in $area. Tests and solidifies."
            Planet.RAHU -> "Rahu transit amplifies worldly desires and can bring sudden changes in $area. Watch for illusions."
            Planet.KETU -> "Ketu transit brings spiritual insights but may cause detachment in $area."
            else -> "Transit influences $area"
        }
    }

    // ============================================
    // DASHA ACTIVATION
    // ============================================

    private fun calculateDashaActivation(
        chart: VedicChart,
        arudhaPadas: List<ArudhaPada>
    ): List<ArudhaDashaActivation> {
        val activations = mutableListOf<ArudhaDashaActivation>()

        // Key Arudhas and their activation triggers
        val keyArudhas = listOf(
            arudhaPadas[0],  // AL
            arudhaPadas[6],  // A7
            arudhaPadas[9],  // A10
            arudhaPadas[10], // A11
            arudhaPadas[11]  // UL
        )

        keyArudhas.forEach { arudha ->
            val lord = arudha.sign.ruler

            // Primary activation - Lord's dasha
            activations.add(ArudhaDashaActivation(
                arudha = arudha.name,
                activatingPeriod = "${lord.displayName} Mahadasha/Antardasha",
                activatingPlanet = lord,
                activationReason = "Lord of ${arudha.name} sign activates ${arudha.fullName} matters",
                expectedEffects = getExpectedEffects(arudha),
                timing = "During ${lord.displayName} periods"
            ))

            // Secondary activation - Planets in Arudha
            arudha.planetsInArudha.forEach { planetPos ->
                activations.add(ArudhaDashaActivation(
                    arudha = arudha.name,
                    activatingPeriod = "${planetPos.planet.displayName} Dasha",
                    activatingPlanet = planetPos.planet,
                    activationReason = "${planetPos.planet.displayName} placed in ${arudha.name}",
                    expectedEffects = getExpectedEffectsWithPlanet(arudha, planetPos.planet),
                    timing = "During ${planetPos.planet.displayName} periods"
                ))
            }
        }

        return activations
    }

    private fun getExpectedEffects(arudha: ArudhaPada): List<String> {
        return when (arudha.house) {
            1 -> listOf(
                "Changes in public image and perception",
                "Recognition or reputation shifts",
                "Physical appearance changes"
            )
            7 -> listOf(
                "Business opportunities or challenges",
                "Partnership developments",
                "Public dealings intensify"
            )
            10 -> listOf(
                "Career changes or advancement",
                "Authority and position changes",
                "Professional recognition"
            )
            11 -> listOf(
                "Financial gains or fluctuations",
                "Desires manifest or face obstacles",
                "Network expansion"
            )
            12 -> listOf(
                "Spouse-related events",
                "Marriage developments",
                "Bedroom/private life changes"
            )
            else -> listOf("${arudha.fullName} matters activate")
        }
    }

    private fun getExpectedEffectsWithPlanet(arudha: ArudhaPada, planet: Planet): List<String> {
        val effects = mutableListOf<String>()

        val planetEffect = when (planet) {
            Planet.JUPITER -> "expansion, blessings, and growth"
            Planet.SATURN -> "structure, delays, and hard work"
            Planet.MARS -> "energy, competition, and drive"
            Planet.VENUS -> "harmony, beauty, and comfort"
            Planet.MERCURY -> "communication, commerce, and learning"
            Planet.SUN -> "authority, recognition, and ego"
            Planet.MOON -> "emotions, popularity, and changes"
            Planet.RAHU -> "ambition, worldly gains, and illusions"
            Planet.KETU -> "spirituality, detachment, and sudden changes"
            else -> "influence"
        }

        effects.add("${planet.displayName} brings $planetEffect to ${arudha.fullName} matters")

        return effects
    }

    // ============================================
    // OVERALL ASSESSMENT
    // ============================================

    private fun calculateOverallAssessment(
        chart: VedicChart,
        arudhaPadas: List<ArudhaPada>,
        specialArudhas: SpecialArudhas,
        yogas: List<ArudhaYoga>,
        language: Language
    ): ArudhaOverallAssessment {
        // Calculate scores based on Arudha strengths and yogas

        val al = specialArudhas.arudhaLagna
        val a7 = specialArudhas.darapada
        val a10 = specialArudhas.rajyaPada
        val a11 = specialArudhas.labhaPada
        val ul = specialArudhas.upapada
 
        val publicImageStrength = calculateStrengthScore(al, language)
        val materialSuccess = (calculateStrengthScore(a10, language) + calculateStrengthScore(a11, language)) / 2
        val relationshipIndicator = (calculateStrengthScore(ul, language) + calculateStrengthScore(a7, language)) / 2
        val careerStrength = calculateStrengthScore(a10, language)
        val gainsIndicator = calculateStrengthScore(a11, language)

        // Overall maya strength - how strongly material world manifests
        val overallMaya = (publicImageStrength + materialSuccess + relationshipIndicator +
                careerStrength + gainsIndicator) / 5

        // Add yoga bonuses
        val yogaBonus: Int = yogas.sumOf { yoga: ArudhaYoga ->
            val bonus: Int = when (yoga.strength) {
                YogaStrength.EXCEPTIONAL -> 10
                YogaStrength.STRONG -> 7
                YogaStrength.MODERATE -> 4
                YogaStrength.MILD -> 2
                YogaStrength.WEAK -> 0
            }
            bonus
        }.coerceAtMost(20)

        val keyThemes = mutableListOf<String>()
        val strengthAreas = mutableListOf<String>()
        val challengeAreas = mutableListOf<String>()

        // Determine themes and areas
        if (publicImageStrength >= 60) {
            strengthAreas.add("Strong public image and recognition potential")
        } else if (publicImageStrength <= 40) {
            challengeAreas.add("Public image may need conscious cultivation")
        }

        if (careerStrength >= 60) {
            strengthAreas.add("Career manifestation is strong")
        }

        if (gainsIndicator >= 60) {
            strengthAreas.add("Good potential for financial gains")
        }

        if (relationshipIndicator >= 60) {
            strengthAreas.add("Favorable relationship manifestation")
        }

        // Yogas add to themes
        yogas.forEach { yoga ->
            if (yoga.strength in listOf(YogaStrength.EXCEPTIONAL, YogaStrength.STRONG)) {
                keyThemes.add(yoga.name)
            }
        }

        return ArudhaOverallAssessment(
            publicImageStrength = publicImageStrength,
            materialSuccessIndicator = materialSuccess + yogaBonus / 2,
            relationshipIndicator = relationshipIndicator,
            careerManifestationStrength = careerStrength,
            gainsAndFulfillment = gainsIndicator,
            overallMayaStrength = (overallMaya + yogaBonus).coerceAtMost(100),
            keyThemes = keyThemes.ifEmpty { listOf("Individual growth and development") },
            strengthAreas = strengthAreas.ifEmpty { listOf("Balanced manifestation across areas") },
            challengeAreas = challengeAreas.ifEmpty { listOf("No major challenging areas identified") }
        )
    }

    private fun calculateStrengthScore(detail: ArudhaPadaDetail, language: Language): Int {
        var score = 50
 
        // Dignity of lord
        score += when (detail.dignityOfLord) {
            PlanetaryDignity.EXALTED -> 20
            PlanetaryDignity.OWN_SIGN -> 15
            PlanetaryDignity.MOOLATRIKONA -> 12
            PlanetaryDignity.FRIEND_SIGN -> 8
            PlanetaryDignity.NEUTRAL_SIGN -> 0
            PlanetaryDignity.ENEMY_SIGN -> -10
            PlanetaryDignity.DEBILITATED -> -15
        }
 
        // Benefics in Arudha
        score += detail.beneficsInArudha.size * 8
 
        // Malefics in Arudha
        score -= detail.maleficsInArudha.size * 6
 
        // Benefic aspects
        score += detail.aspectsOnArudha.count { it.nature == StringResources.get(StringKeyMatch.ASPECT_NATURE_HARMONIOUS, language) } * 5
 
        return score.coerceIn(10, 100)
    }

    // ============================================
    // INTERPRETATION GENERATION
    // ============================================

    private fun generateInterpretation(
        chart: VedicChart,
        arudhaPadas: List<ArudhaPada>,
        specialArudhas: SpecialArudhas,
        yogas: List<ArudhaYoga>,
        assessment: ArudhaOverallAssessment,
        language: Language
    ): ArudhaInterpretation {
        val al = specialArudhas.arudhaLagna.arudha
        val ul = specialArudhas.upapada.arudha
        val a10 = specialArudhas.rajyaPada.arudha

        val summary = buildString {
            append(StringResources.get(StringKeyAnalysis.ARUDHA_AREA_1, language) + " (" + al.sign.getLocalizedName(language) + "): " + getSignImageDescription(al.sign, language) + ". ")
            append(StringResources.get(StringKeyAnalysis.ARUDHA_AREA_10, language) + " (" + a10.sign.getLocalizedName(language) + "): " + getA10CareerDescription(a10.sign, language) + ". ")
        }

        val publicPerception = buildString {
            append(StringResources.get(StringKeyAnalysis.ARUDHA_AREA_1, language) + " " + al.sign.getLocalizedName(language) + " ")
            append(StringResources.get(StringKeyAnalysis.ARGALA_LORD_PLACEMENT, language, al.houseLord.getLocalizedName(language), al.houseLordSign.getLocalizedName(language)) + ". ")
            append(getSignImageDescription(al.sign, language) + ". ")
        }

        val materialLife = buildString {
            val a11 = specialArudhas.labhaPada.arudha
            append(StringResources.get(StringKeyAnalysis.ARUDHA_AREA_11, language) + " " + a11.sign.getLocalizedName(language) + ". ")
            append(if (assessment.materialSuccessIndicator >= 60) StringResources.get(StringKeyAnalysis.ARUDHA_STR_STRONG, language) else StringResources.get(StringKeyAnalysis.ARUDHA_STR_MODERATE, language))
        }

        val relationshipManifestation = buildString {
            append(StringResources.get(StringKeyAnalysis.ARUDHA_AREA_12, language) + " " + ul.sign.getLocalizedName(language) + ". ")
            append(getULSpouseDescription(ul.sign, language) + ". ")
            val a7 = specialArudhas.darapada.arudha
            append(StringResources.get(StringKeyAnalysis.ARUDHA_AREA_7, language) + " " + a7.sign.getLocalizedName(language) + ". ")
            append(getA7BusinessDescription(a7.sign, language) + ". ")
        }

        val careerAndAuthority = buildString {
            append(StringResources.get(StringKeyAnalysis.ARUDHA_AREA_10, language) + " " + a10.sign.getLocalizedName(language) + ". ")
            append(getA10CareerDescription(a10.sign, language) + ". ")
        }

        val recommendations = mutableListOf<String>()
        recommendations.add("Focus on ${al.sign.element.lowercase()} sign activities to enhance public image")
        recommendations.add("Career advancement favored during ${a10.houseLord.displayName} periods")
        recommendations.add("Business partnerships benefit from ${specialArudhas.darapada.arudha.sign.element.lowercase()} sign qualities")

        assessment.challengeAreas.forEach {
            recommendations.add("Address: $it")
        }

        yogas.flatMap { it.recommendations }.take(3).forEach {
            recommendations.add(it)
        }

        return ArudhaInterpretation(
            summary = summary,
            publicPerception = publicPerception,
            materialLife = materialLife,
            relationshipManifestation = relationshipManifestation,
            careerAndAuthority = careerAndAuthority,
            recommendations = recommendations.distinct()
        )
    }

    // ============================================
    // HELPER FUNCTIONS
    // ============================================

    private fun getSignDistance(from: ZodiacSign, to: ZodiacSign): Int {
        return ((to.ordinal - from.ordinal + 12) % 12) + 1
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

    private fun isOwnSign(planet: Planet, sign: ZodiacSign): Boolean {
        return sign.ruler == planet
    }

    private fun isMoolatrikona(planet: Planet, sign: ZodiacSign): Boolean {
        return when (planet) {
            Planet.SUN -> sign == ZodiacSign.LEO
            Planet.MOON -> sign == ZodiacSign.TAURUS
            Planet.MARS -> sign == ZodiacSign.ARIES
            Planet.MERCURY -> sign == ZodiacSign.VIRGO
            Planet.JUPITER -> sign == ZodiacSign.SAGITTARIUS
            Planet.VENUS -> sign == ZodiacSign.LIBRA
            Planet.SATURN -> sign == ZodiacSign.AQUARIUS
            else -> false
        }
    }

    private fun isFriendSign(planet: Planet, sign: ZodiacSign): Boolean {
        val friends = when (planet) {
            Planet.SUN -> listOf(Planet.MOON, Planet.MARS, Planet.JUPITER)
            Planet.MOON -> listOf(Planet.SUN, Planet.MERCURY)
            Planet.MARS -> listOf(Planet.SUN, Planet.MOON, Planet.JUPITER)
            Planet.MERCURY -> listOf(Planet.SUN, Planet.VENUS)
            Planet.JUPITER -> listOf(Planet.SUN, Planet.MOON, Planet.MARS)
            Planet.VENUS -> listOf(Planet.MERCURY, Planet.SATURN)
            Planet.SATURN -> listOf(Planet.MERCURY, Planet.VENUS)
            else -> emptyList()
        }
        return sign.ruler in friends
    }

    private fun isEnemySign(planet: Planet, sign: ZodiacSign): Boolean {
        val enemies = when (planet) {
            Planet.SUN -> listOf(Planet.SATURN, Planet.VENUS)
            Planet.MOON -> emptyList()
            Planet.MARS -> listOf(Planet.MERCURY)
            Planet.MERCURY -> listOf(Planet.MOON)
            Planet.JUPITER -> listOf(Planet.MERCURY, Planet.VENUS)
            Planet.VENUS -> listOf(Planet.SUN, Planet.MOON)
            Planet.SATURN -> listOf(Planet.SUN, Planet.MOON, Planet.MARS)
            else -> emptyList()
        }
        return sign.ruler in enemies
    }

    private fun aspectsSign(planet: Planet, fromSign: ZodiacSign, toSign: ZodiacSign): Boolean {
        val distance = getSignDistance(fromSign, toSign)

        // All planets aspect 7th sign
        if (distance == 7) return true

        // Special aspects
        return when (planet) {
            Planet.MARS -> distance in listOf(4, 8) // 4th and 8th
            Planet.JUPITER -> distance in listOf(5, 9) // 5th and 9th
            Planet.SATURN -> distance in listOf(3, 10) // 3rd and 10th
            Planet.RAHU, Planet.KETU -> distance in listOf(5, 9) // Like Jupiter
            else -> false
        }
    }
}
