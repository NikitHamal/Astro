package com.astro.vajra.ephemeris.chakra

import com.astro.vajra.core.model.Planet
import com.astro.vajra.core.model.PlanetPosition
import com.astro.vajra.core.model.VedicChart
import com.astro.vajra.core.model.ZodiacSign
import com.astro.vajra.ephemeris.AstrologicalConstants
import com.astro.vajra.ephemeris.PlanetaryShadbala
import com.astro.vajra.ephemeris.ShadbalaAnalysis
import com.astro.vajra.ephemeris.VedicAstrologyUtils
import kotlin.math.roundToInt

// =====================================================================================
// CHAKRA MAPPING DATA MODELS
// =====================================================================================

/**
 * Represents the seven primary chakras (energy centers) of the subtle body
 * as mapped in Vedic/Tantric tradition.
 *
 * Each chakra corresponds to specific planets, elements, anatomical locations,
 * colors, and beeja (seed) mantras. The planetary associations derive from
 * classical Tantric texts linking planetary energies to pranic centers.
 *
 * @property sanskrit Transliterated Sanskrit name with diacritical marks
 * @property english Common English name
 * @property location Physical location in the body
 * @property element The Pancha Mahabhuta (five great elements) correspondence
 * @property color The associated aura/energy color
 * @property beejaMantra The seed syllable for activation
 * @property number Ordinal position from base to crown (1-7)
 */
enum class Chakra(
    val sanskrit: String,
    val english: String,
    val location: String,
    val element: String,
    val color: String,
    val beejaMantra: String,
    val number: Int
) {
    MULADHARA(
        "Muladhara", "Root",
        "Base of spine", "Earth", "Red", "LAM", 1
    ),
    SWADHISTHANA(
        "Svadhisthana", "Sacral",
        "Below navel", "Water", "Orange", "VAM", 2
    ),
    MANIPURA(
        "Manipura", "Solar Plexus",
        "Above navel", "Fire", "Yellow", "RAM", 3
    ),
    ANAHATA(
        "Anahata", "Heart",
        "Center of chest", "Air", "Green", "YAM", 4
    ),
    VISHUDDHA(
        "Vishuddha", "Throat",
        "Throat", "Ether", "Blue", "HAM", 5
    ),
    AJNA(
        "Ajna", "Third Eye",
        "Between eyebrows", "Mind", "Indigo", "OM", 6
    ),
    SAHASRARA(
        "Sahasrara", "Crown",
        "Top of head", "Consciousness", "Violet", "Silence", 7
    )
}

/**
 * The health/activation status of a chakra based on its composite score.
 */
enum class ChakraStatus(val displayName: String, val description: String) {
    SEVERELY_BLOCKED(
        "Severely Blocked",
        "Critical energy deficiency. This chakra's functions are severely impaired."
    ),
    UNDER_ACTIVE(
        "Under-active",
        "Below optimal function. This chakra needs strengthening through practice and remedies."
    ),
    BALANCED(
        "Balanced",
        "Optimal function. This chakra is operating within a healthy range."
    ),
    ACTIVE(
        "Active",
        "Strong function. This chakra is well-energized and producing good results."
    ),
    OVER_ACTIVE(
        "Over-active",
        "Excess energy. This chakra may cause imbalance if not grounded properly."
    )
}

/**
 * Describes a planet's contribution to a specific chakra's health.
 *
 * @property planet The contributing planet
 * @property shadbalaScore The planet's Shadbala score in Rupas
 * @property normalizedScore Normalized contribution percentage (0-100)
 * @property isExalted Whether the planet is exalted in the birth chart
 * @property isDebilitated Whether the planet is debilitated in the birth chart
 * @property isCombust Whether the planet is combust
 * @property isRetrograde Whether the planet is retrograde
 * @property isInDusthana Whether the planet is in a dusthana house (6, 8, 12)
 * @property contribution Net contribution to chakra health (positive or negative adjustment)
 */
data class PlanetChakraContribution(
    val planet: Planet,
    val shadbalaScore: Double,
    val normalizedScore: Double,
    val isExalted: Boolean,
    val isDebilitated: Boolean,
    val isCombust: Boolean,
    val isRetrograde: Boolean,
    val isInDusthana: Boolean,
    val contribution: Double
)

/**
 * Describes a planet's association with one or more chakras.
 *
 * @property planet The planet
 * @property primaryChakra The primary chakra this planet influences
 * @property secondaryChakra Optional secondary chakra influenced
 * @property influenceType Whether the influence is harmonizing, disrupting, or neutral
 */
data class ChakraAssociation(
    val planet: Planet,
    val primaryChakra: Chakra,
    val secondaryChakra: Chakra?,
    val influenceType: ChakraInfluenceType
)

/**
 * The type of influence a planet has on a chakra.
 */
enum class ChakraInfluenceType(val displayName: String) {
    HARMONIZING("Harmonizing"),
    DISRUPTING("Disrupting"),
    NEUTRAL("Neutral"),
    ACTIVATING("Activating"),
    BLOCKING("Blocking")
}

/**
 * The complete state of a single chakra including health score, contributing planets,
 * and interpretive details.
 *
 * @property chakra The chakra being analyzed
 * @property healthScore Composite health score (0-100)
 * @property status Categorical status derived from health score
 * @property rulingPlanets Contributions from all planets associated with this chakra
 * @property elementStrength Strength contribution from elemental balance (0-100)
 * @property blockages Identified energy blockages
 * @property strengths Identified energy strengths
 */
data class ChakraState(
    val chakra: Chakra,
    val healthScore: Int,
    val status: ChakraStatus,
    val rulingPlanets: List<PlanetChakraContribution>,
    val elementStrength: Double,
    val blockages: List<String>,
    val strengths: List<String>
)

/**
 * Elemental balance across the four Vedic elements based on planetary distribution.
 *
 * @property fire Fire element strength (Aries, Leo, Sagittarius)
 * @property earth Earth element strength (Taurus, Virgo, Capricorn)
 * @property air Air element strength (Gemini, Libra, Aquarius)
 * @property water Water element strength (Cancer, Scorpio, Pisces)
 * @property dominantElement The strongest element
 * @property deficientElement The weakest element
 */
data class ElementBalance(
    val fire: Double,
    val earth: Double,
    val air: Double,
    val water: Double,
    val dominantElement: String,
    val deficientElement: String
)

/**
 * Ayurvedic Dosha constitution derived from the birth chart.
 *
 * @property vata Vata dosha percentage (Air/Space)
 * @property pitta Pitta dosha percentage (Fire/Water)
 * @property kapha Kapha dosha percentage (Water/Earth)
 * @property primaryDosha The dominant dosha
 * @property secondaryDosha The second-strongest dosha
 * @property constitution Descriptive constitutional type (e.g., "Vata-Pitta")
 * @property constitutionDescription Detailed description of the constitutional type
 */
data class DoshaConstitution(
    val vata: Double,
    val pitta: Double,
    val kapha: Double,
    val primaryDosha: String,
    val secondaryDosha: String,
    val constitution: String,
    val constitutionDescription: String
)

/**
 * Overall energy profile derived from chakra states.
 *
 * @property overallScore Average of all chakra health scores (0-100)
 * @property balancedChakraCount Number of chakras in balanced or active state
 * @property blockedChakraCount Number of blocked or under-active chakras
 * @property overactiveChakraCount Number of over-active chakras
 * @property energyFlow Description of the overall pranic flow pattern
 * @property kundaliniReadiness Assessment of readiness for kundalini activation
 */
data class EnergyProfile(
    val overallScore: Int,
    val balancedChakraCount: Int,
    val blockedChakraCount: Int,
    val overactiveChakraCount: Int,
    val energyFlow: String,
    val kundaliniReadiness: String
)

/**
 * A remedial suggestion for a specific chakra imbalance.
 *
 * @property chakra The target chakra
 * @property issue The identified issue (blocked or overactive)
 * @property remedyCategory Category of remedy (Mantra, Yoga, Diet, Gem, Lifestyle)
 * @property remedy Specific remedial suggestion
 * @property planetaryRemedy Associated planetary remedy if applicable
 */
data class ChakraRemedy(
    val chakra: Chakra,
    val issue: String,
    val remedyCategory: String,
    val remedy: String,
    val planetaryRemedy: String
)

/**
 * Complete Chakra analysis result combining all seven chakra states,
 * elemental balance, dosha constitution, energy profile, and remedies.
 *
 * @property chakras States of all seven chakras
 * @property elementBalance Elemental distribution analysis
 * @property doshaConstitution Ayurvedic dosha constitution
 * @property overallEnergyProfile Aggregate energy assessment
 * @property remedialSuggestions Targeted remedies for imbalanced chakras
 * @property planetChakraMap Map of each planet to its chakra associations
 */
data class ChakraAnalysis(
    val chakras: List<ChakraState>,
    val elementBalance: ElementBalance,
    val doshaConstitution: DoshaConstitution,
    val overallEnergyProfile: EnergyProfile,
    val remedialSuggestions: List<ChakraRemedy>,
    val planetChakraMap: Map<Planet, List<ChakraAssociation>>
)

// =====================================================================================
// CHAKRA MAPPING CALCULATOR
// =====================================================================================

/**
 * Vedic Bio-Hacking Chakra Mapping Calculator.
 *
 * This calculator maps planetary strengths from Shadbala analysis to the seven
 * chakras (energy centers) of the subtle body, integrating Vedic astrology with
 * Tantric/Ayurvedic principles.
 *
 * **Planet-Chakra Correspondence (from Vedic/Tantric texts):**
 * 1. Muladhara (Root) - Mars, Saturn (earth element, survival, stability)
 * 2. Swadhisthana (Sacral) - Moon, Venus (water element, emotions, pleasure)
 * 3. Manipura (Solar Plexus) - Sun, Mars (fire element, willpower, transformation)
 * 4. Anahata (Heart) - Venus, Moon (air element, love, compassion)
 * 5. Vishuddha (Throat) - Mercury, Jupiter (ether element, communication, truth)
 * 6. Ajna (Third Eye) - Jupiter, Moon, Ketu (intuition, wisdom)
 * 7. Sahasrara (Crown) - Ketu, Sun, Jupiter (transcendence, consciousness)
 *
 * **Shadow Planet Effects:**
 * - Rahu: Disrupts Ajna (illusion) and Manipura (obsessive desire)
 * - Ketu: Activates Sahasrara (liberation) and detaches Muladhara (worldly)
 *
 * **Scoring Methodology:**
 * Each chakra receives a health score (0-100) computed from:
 * 1. Shadbala scores of ruling planets (normalized to percentage)
 * 2. Dignity adjustments (exalted = boost, debilitated = deplete)
 * 3. Affliction penalties (combust, dusthana, malefic aspects)
 * 4. Benefic aspect bonuses
 * 5. Elemental balance contribution from sign distribution
 *
 * **Ayurvedic Integration:**
 * - Vata (Air): Saturn, Mercury, Rahu dominance
 * - Pitta (Fire): Sun, Mars, Ketu dominance
 * - Kapha (Water/Earth): Moon, Venus, Jupiter dominance
 *
 * **Classical References:**
 * - Sat-Chakra-Nirupana (Six Chakras Description)
 * - Padaka-Panchaka (Five Footprints)
 * - Sushruta Samhita (Ayurvedic constitutional analysis)
 *
 * @author AstroVajra
 */
object ChakraMappingCalculator {

    // ============================================================================
    // PLANET-CHAKRA MAPPING
    // ============================================================================

    /**
     * Primary planet-chakra associations from Vedic/Tantric texts.
     * Each chakra has primary rulers and optional secondary influencers.
     *
     * The weighting indicates how much each planet contributes to the chakra's health:
     * - Primary rulers: weight 1.0
     * - Secondary influencers: weight 0.5
     */
    private data class ChakraPlanetWeight(val planet: Planet, val weight: Double)

    private val CHAKRA_PLANET_MAP: Map<Chakra, List<ChakraPlanetWeight>> = mapOf(
        Chakra.MULADHARA to listOf(
            ChakraPlanetWeight(Planet.MARS, 1.0),
            ChakraPlanetWeight(Planet.SATURN, 1.0)
        ),
        Chakra.SWADHISTHANA to listOf(
            ChakraPlanetWeight(Planet.MOON, 1.0),
            ChakraPlanetWeight(Planet.VENUS, 1.0)
        ),
        Chakra.MANIPURA to listOf(
            ChakraPlanetWeight(Planet.SUN, 1.0),
            ChakraPlanetWeight(Planet.MARS, 0.7)
        ),
        Chakra.ANAHATA to listOf(
            ChakraPlanetWeight(Planet.VENUS, 1.0),
            ChakraPlanetWeight(Planet.MOON, 0.7)
        ),
        Chakra.VISHUDDHA to listOf(
            ChakraPlanetWeight(Planet.MERCURY, 1.0),
            ChakraPlanetWeight(Planet.JUPITER, 0.7)
        ),
        Chakra.AJNA to listOf(
            ChakraPlanetWeight(Planet.JUPITER, 1.0),
            ChakraPlanetWeight(Planet.MOON, 0.5),
            ChakraPlanetWeight(Planet.KETU, 0.6)
        ),
        Chakra.SAHASRARA to listOf(
            ChakraPlanetWeight(Planet.KETU, 1.0),
            ChakraPlanetWeight(Planet.SUN, 0.5),
            ChakraPlanetWeight(Planet.JUPITER, 0.6)
        )
    )

    /**
     * Shadow planet disruption map: Rahu and Ketu's disruptive/activating influences.
     */
    private data class ShadowInfluence(
        val chakra: Chakra,
        val influence: ChakraInfluenceType,
        val weight: Double
    )

    private val RAHU_INFLUENCES = listOf(
        ShadowInfluence(Chakra.AJNA, ChakraInfluenceType.DISRUPTING, 0.7),
        ShadowInfluence(Chakra.MANIPURA, ChakraInfluenceType.DISRUPTING, 0.5)
    )

    private val KETU_INFLUENCES = listOf(
        ShadowInfluence(Chakra.SAHASRARA, ChakraInfluenceType.ACTIVATING, 0.8),
        ShadowInfluence(Chakra.MULADHARA, ChakraInfluenceType.BLOCKING, 0.4)
    )

    /**
     * Element-to-chakra mapping for elemental balance contribution.
     */
    private val ELEMENT_CHAKRA_MAP: Map<String, List<Chakra>> = mapOf(
        "Fire" to listOf(Chakra.MANIPURA, Chakra.AJNA),
        "Earth" to listOf(Chakra.MULADHARA),
        "Air" to listOf(Chakra.ANAHATA, Chakra.VISHUDDHA),
        "Water" to listOf(Chakra.SWADHISTHANA, Chakra.SAHASRARA)
    )

    /**
     * Dosha planet associations for Ayurvedic constitution determination.
     */
    private val VATA_PLANETS = setOf(Planet.SATURN, Planet.MERCURY, Planet.RAHU)
    private val PITTA_PLANETS = setOf(Planet.SUN, Planet.MARS, Planet.KETU)
    private val KAPHA_PLANETS = setOf(Planet.MOON, Planet.VENUS, Planet.JUPITER)

    /**
     * Required minimum Shadbala in Rupas for each planet (from BPHS).
     * Used to normalize Shadbala scores to percentages.
     */
    private val REQUIRED_SHADBALA_RUPAS = mapOf(
        Planet.SUN to 6.5,
        Planet.MOON to 6.0,
        Planet.MARS to 5.0,
        Planet.MERCURY to 7.0,
        Planet.JUPITER to 6.5,
        Planet.VENUS to 5.5,
        Planet.SATURN to 5.0
    )

    // ============================================================================
    // PUBLIC API
    // ============================================================================

    /**
     * Performs a complete Chakra mapping analysis based on the birth chart
     * and Shadbala analysis.
     *
     * This is the primary entry point. It computes health scores for all seven
     * chakras, determines elemental balance and Ayurvedic constitution, and
     * generates targeted remedial suggestions.
     *
     * @param chart The native's Vedic birth chart
     * @param shadbalaAnalysis Pre-computed Shadbala analysis for planetary strengths.
     *                         If null, a simplified estimation is used for Rahu/Ketu
     *                         and non-Shadbala planets.
     * @return Complete [ChakraAnalysis] with all seven chakra states and remedies
     */
    fun analyze(
        chart: VedicChart,
        shadbalaAnalysis: ShadbalaAnalysis? = null
    ): ChakraAnalysis {
        val planetPositionMap = chart.planetPositions.associateBy { it.planet }
        val sunPosition = planetPositionMap[Planet.SUN]

        // 1. Calculate elemental balance
        val elementBalance = calculateElementBalance(chart)

        // 2. Calculate each chakra's state
        val chakraStates = Chakra.entries.map { chakra ->
            calculateChakraState(chakra, chart, shadbalaAnalysis, elementBalance, planetPositionMap, sunPosition)
        }

        // 3. Calculate Dosha constitution
        val doshaConstitution = calculateDoshaConstitution(chart, shadbalaAnalysis)

        // 4. Calculate overall energy profile
        val energyProfile = calculateEnergyProfile(chakraStates)

        // 5. Generate remedial suggestions
        val remedies = generateRemedialSuggestions(chakraStates)

        // 6. Build planet-chakra association map
        val planetChakraMap = buildPlanetChakraMap(chart)

        return ChakraAnalysis(
            chakras = chakraStates,
            elementBalance = elementBalance,
            doshaConstitution = doshaConstitution,
            overallEnergyProfile = energyProfile,
            remedialSuggestions = remedies,
            planetChakraMap = planetChakraMap
        )
    }

    // ============================================================================
    // CHAKRA STATE CALCULATION
    // ============================================================================

    /**
     * Calculates the complete state of a single chakra.
     *
     * The scoring algorithm:
     * 1. Base score: weighted average of ruling planets' normalized Shadbala scores
     * 2. Dignity adjustment: +15 exalted, +10 own sign, +5 moola, -15 debilitated
     * 3. Affliction adjustment: -10 combust, -5 retrograde (for some), -8 dusthana
     * 4. Benefic aspect bonus: +5 per benefic aspecting a ruling planet
     * 5. Malefic aspect penalty: -5 per malefic aspecting a ruling planet
     * 6. Shadow planet effect: Rahu/Ketu disruption or activation
     * 7. Elemental balance contribution: 0-15 based on element presence
     *
     * @param chakra The chakra to analyze
     * @param chart The birth chart
     * @param shadbala Shadbala analysis (nullable)
     * @param elementBalance Pre-computed elemental balance
     * @param positionMap Pre-computed planet position map
     * @param sunPosition Sun's position for combustion checks
     * @return Complete [ChakraState] for this chakra
     */
    private fun calculateChakraState(
        chakra: Chakra,
        chart: VedicChart,
        shadbala: ShadbalaAnalysis?,
        elementBalance: ElementBalance,
        positionMap: Map<Planet, PlanetPosition>,
        sunPosition: PlanetPosition?
    ): ChakraState {
        val chakraPlanets = CHAKRA_PLANET_MAP[chakra] ?: emptyList()
        val contributions = mutableListOf<PlanetChakraContribution>()
        val blockages = mutableListOf<String>()
        val strengths = mutableListOf<String>()

        var weightedScoreSum = 0.0
        var totalWeight = 0.0

        for (cpw in chakraPlanets) {
            val planet = cpw.planet
            val weight = cpw.weight
            val position = positionMap[planet]

            // Get Shadbala score
            val shadbalaScore = shadbala?.planetaryStrengths?.get(planet)?.totalRupas
                ?: estimatePlanetStrength(planet, position, chart)

            // Normalize to 0-100 percentage
            val requiredRupas = REQUIRED_SHADBALA_RUPAS[planet] ?: 5.5
            val normalizedScore = ((shadbalaScore / requiredRupas) * 50.0).coerceIn(0.0, 100.0)

            // Dignity checks
            val isExalted = position?.let { VedicAstrologyUtils.isExalted(it) } ?: false
            val isDebilitated = position?.let { VedicAstrologyUtils.isDebilitated(it) } ?: false
            val isOwnSign = position?.let { VedicAstrologyUtils.isInOwnSign(it) } ?: false
            val isMoolatrikona = position?.let { VedicAstrologyUtils.isInMoolatrikona(it) } ?: false

            // Affliction checks
            val isCombust = position?.let { pos ->
                sunPosition?.let { sun ->
                    VedicAstrologyUtils.isCombust(pos, sun)
                }
            } ?: false

            val isRetrograde = position?.isRetrograde ?: false
            val isInDusthana = position?.let { it.house in VedicAstrologyUtils.DUSTHANA_HOUSES } ?: false

            // Calculate contribution score
            var contribution = normalizedScore

            // Dignity adjustments
            if (isExalted) {
                contribution += 15.0
                strengths.add("${planet.displayName} is exalted, strongly energizing ${chakra.english} chakra")
            }
            if (isOwnSign) {
                contribution += 10.0
                strengths.add("${planet.displayName} in own sign, supporting ${chakra.english} chakra stability")
            }
            if (isMoolatrikona) {
                contribution += 8.0
                strengths.add("${planet.displayName} in Moolatrikona, optimizing ${chakra.english} chakra function")
            }
            if (isDebilitated) {
                contribution -= 15.0
                blockages.add("${planet.displayName} is debilitated, depleting ${chakra.english} chakra energy")
            }

            // Affliction adjustments
            if (isCombust) {
                contribution -= 10.0
                blockages.add("${planet.displayName} is combust (too close to Sun), weakening ${chakra.english} chakra")
            }
            if (isInDusthana) {
                contribution -= 8.0
                blockages.add("${planet.displayName} in dusthana house (${position?.house}), creating ${chakra.english} chakra resistance")
            }

            // Retrograde: mixed effect depending on planet
            if (isRetrograde) {
                when (planet) {
                    Planet.SATURN, Planet.JUPITER -> {
                        // Retrograde Saturn/Jupiter intensify but can go either way
                        contribution += 3.0
                        strengths.add("${planet.displayName} retrograde, intensifying ${chakra.english} chakra from within")
                    }
                    Planet.MERCURY, Planet.VENUS -> {
                        contribution -= 5.0
                        blockages.add("${planet.displayName} retrograde, internalizing ${chakra.english} chakra expression")
                    }
                    Planet.MARS -> {
                        contribution -= 3.0
                        blockages.add("${planet.displayName} retrograde, frustrating ${chakra.english} chakra energy flow")
                    }
                    else -> { /* No adjustment for other planets */ }
                }
            }

            // Benefic/malefic aspects on ruling planet
            if (position != null) {
                for (otherPos in chart.planetPositions) {
                    if (otherPos.planet == planet) continue
                    val aspectedHouses = VedicAstrologyUtils.getAspectedHouses(otherPos.planet, otherPos.house)
                    if (position.house in aspectedHouses) {
                        if (VedicAstrologyUtils.isNaturalBenefic(otherPos.planet)) {
                            contribution += 5.0
                            strengths.add("${otherPos.planet.displayName} aspects ${planet.displayName}, " +
                                    "harmonizing ${chakra.english} chakra")
                        } else if (VedicAstrologyUtils.isNaturalMalefic(otherPos.planet)) {
                            contribution -= 5.0
                            blockages.add("${otherPos.planet.displayName} aspects ${planet.displayName}, " +
                                    "pressuring ${chakra.english} chakra")
                        }
                    }
                }
            }

            contribution = contribution.coerceIn(0.0, 100.0)

            contributions.add(
                PlanetChakraContribution(
                    planet = planet,
                    shadbalaScore = shadbalaScore,
                    normalizedScore = normalizedScore,
                    isExalted = isExalted,
                    isDebilitated = isDebilitated,
                    isCombust = isCombust,
                    isRetrograde = isRetrograde,
                    isInDusthana = isInDusthana,
                    contribution = contribution
                )
            )

            weightedScoreSum += contribution * weight
            totalWeight += weight
        }

        // Apply shadow planet effects
        var shadowAdjustment = 0.0
        val rahuPos = positionMap[Planet.RAHU]
        val ketuPos = positionMap[Planet.KETU]

        for (influence in RAHU_INFLUENCES) {
            if (influence.chakra == chakra && rahuPos != null) {
                val rahuStrength = estimateShadowPlanetStrength(rahuPos, chart)
                val adjustment = -rahuStrength * influence.weight * 0.15
                shadowAdjustment += adjustment
                if (adjustment < -3.0) {
                    blockages.add("Rahu's illusion-creating influence disrupts ${chakra.english} chakra clarity")
                }
            }
        }

        for (influence in KETU_INFLUENCES) {
            if (influence.chakra == chakra && ketuPos != null) {
                val ketuStrength = estimateShadowPlanetStrength(ketuPos, chart)
                when (influence.influence) {
                    ChakraInfluenceType.ACTIVATING -> {
                        val adjustment = ketuStrength * influence.weight * 0.12
                        shadowAdjustment += adjustment
                        if (adjustment > 3.0) {
                            strengths.add("Ketu's spiritual detachment activates ${chakra.english} chakra transcendence")
                        }
                    }
                    ChakraInfluenceType.BLOCKING -> {
                        val adjustment = -ketuStrength * influence.weight * 0.10
                        shadowAdjustment += adjustment
                        if (adjustment < -2.0) {
                            blockages.add("Ketu's detachment weakens ${chakra.english} chakra grounding")
                        }
                    }
                    else -> { /* No adjustment */ }
                }
            }
        }

        // Calculate elemental contribution
        val elementStrength = calculateElementalContribution(chakra, elementBalance)
        val elementBonus = elementStrength * 0.15 // Element contributes up to 15 points

        // Compute final health score
        val baseScore = if (totalWeight > 0) weightedScoreSum / totalWeight else 50.0
        val finalScore = (baseScore + shadowAdjustment + elementBonus).roundToInt().coerceIn(0, 100)

        // Determine status
        val status = when {
            finalScore <= 20 -> ChakraStatus.SEVERELY_BLOCKED
            finalScore <= 40 -> ChakraStatus.UNDER_ACTIVE
            finalScore <= 60 -> ChakraStatus.BALANCED
            finalScore <= 80 -> ChakraStatus.ACTIVE
            else -> ChakraStatus.OVER_ACTIVE
        }

        return ChakraState(
            chakra = chakra,
            healthScore = finalScore,
            status = status,
            rulingPlanets = contributions,
            elementStrength = elementStrength,
            blockages = blockages.distinct(),
            strengths = strengths.distinct()
        )
    }

    // ============================================================================
    // ELEMENT BALANCE
    // ============================================================================

    /**
     * Calculates the elemental balance based on the distribution of planets
     * across the four Vedic elements (Fire, Earth, Air, Water).
     *
     * Each planet in a sign of a given element contributes to that element's
     * strength. The contribution is weighted by the planet's natural significance.
     */
    fun calculateElementBalance(chart: VedicChart): ElementBalance {
        var fire = 0.0
        var earth = 0.0
        var air = 0.0
        var water = 0.0

        for (position in chart.planetPositions) {
            if (position.planet !in Planet.MAIN_PLANETS) continue

            val weight = when (position.planet) {
                Planet.SUN, Planet.MOON -> 1.5  // Luminaries have stronger elemental influence
                Planet.JUPITER -> 1.3           // Jupiter amplifies element
                Planet.RAHU, Planet.KETU -> 0.7 // Shadow planets have reduced elemental weight
                else -> 1.0
            }

            when (position.sign.element) {
                "Fire" -> fire += weight
                "Earth" -> earth += weight
                "Air" -> air += weight
                "Water" -> water += weight
            }
        }

        // Normalize to percentages
        val total = fire + earth + air + water
        if (total > 0) {
            fire = (fire / total) * 100.0
            earth = (earth / total) * 100.0
            air = (air / total) * 100.0
            water = (water / total) * 100.0
        }

        val elements = mapOf("Fire" to fire, "Earth" to earth, "Air" to air, "Water" to water)
        val dominant = elements.maxByOrNull { it.value }?.key ?: "Fire"
        val deficient = elements.minByOrNull { it.value }?.key ?: "Water"

        return ElementBalance(
            fire = fire,
            earth = earth,
            air = air,
            water = water,
            dominantElement = dominant,
            deficientElement = deficient
        )
    }

    /**
     * Calculates the elemental contribution to a specific chakra based on
     * how strongly the chakra's associated element is represented in the chart.
     */
    private fun calculateElementalContribution(chakra: Chakra, elementBalance: ElementBalance): Double {
        val associatedChakras = ELEMENT_CHAKRA_MAP.entries
            .filter { (_, chakras) -> chakra in chakras }
            .map { it.key }

        if (associatedChakras.isEmpty()) return 50.0

        var totalStrength = 0.0
        for (element in associatedChakras) {
            totalStrength += when (element) {
                "Fire" -> elementBalance.fire
                "Earth" -> elementBalance.earth
                "Air" -> elementBalance.air
                "Water" -> elementBalance.water
                else -> 25.0
            }
        }

        return (totalStrength / associatedChakras.size).coerceIn(0.0, 100.0)
    }

    // ============================================================================
    // DOSHA CONSTITUTION
    // ============================================================================

    /**
     * Calculates the Ayurvedic Dosha (constitutional) type from planetary strengths.
     *
     * - Vata (Air/Space): Influenced by Saturn, Mercury, Rahu
     * - Pitta (Fire/Water): Influenced by Sun, Mars, Ketu
     * - Kapha (Water/Earth): Influenced by Moon, Venus, Jupiter
     *
     * The calculation uses both placement strength (sign, house) and Shadbala scores
     * to produce accurate constitutional percentages.
     */
    private fun calculateDoshaConstitution(
        chart: VedicChart,
        shadbala: ShadbalaAnalysis?
    ): DoshaConstitution {
        var vataScore = 0.0
        var pittaScore = 0.0
        var kaphaScore = 0.0

        for (position in chart.planetPositions) {
            if (position.planet !in Planet.MAIN_PLANETS) continue

            val strength = shadbala?.planetaryStrengths?.get(position.planet)?.totalRupas
                ?: estimatePlanetStrength(position.planet, position, chart)

            // Normalize strength to a 1-10 scale for dosha contribution
            val requiredRupas = REQUIRED_SHADBALA_RUPAS[position.planet] ?: 5.5
            val normalizedStrength = (strength / requiredRupas * 5.0).coerceIn(1.0, 10.0)

            when {
                position.planet in VATA_PLANETS -> vataScore += normalizedStrength
                position.planet in PITTA_PLANETS -> pittaScore += normalizedStrength
                position.planet in KAPHA_PLANETS -> kaphaScore += normalizedStrength
            }

            // Element also contributes to dosha
            when (position.sign.element) {
                "Fire" -> pittaScore += 1.0
                "Earth" -> kaphaScore += 0.7
                "Air" -> vataScore += 1.0
                "Water" -> kaphaScore += 0.7
            }
        }

        // Ascendant sign element contribution
        val ascSign = ZodiacSign.fromLongitude(chart.ascendant)
        when (ascSign.element) {
            "Fire" -> pittaScore += 3.0
            "Earth" -> kaphaScore += 2.5
            "Air" -> vataScore += 3.0
            "Water" -> kaphaScore += 2.5
        }

        // Normalize to percentages
        val total = vataScore + pittaScore + kaphaScore
        val vataPercent = if (total > 0) (vataScore / total) * 100.0 else 33.33
        val pittaPercent = if (total > 0) (pittaScore / total) * 100.0 else 33.33
        val kaphaPercent = if (total > 0) (kaphaScore / total) * 100.0 else 33.34

        val doshas = mapOf("Vata" to vataPercent, "Pitta" to pittaPercent, "Kapha" to kaphaPercent)
        val sorted = doshas.entries.sortedByDescending { it.value }
        val primary = sorted[0].key
        val secondary = sorted[1].key
        val constitution = "$primary-$secondary"

        val description = getConstitutionDescription(primary, secondary)

        return DoshaConstitution(
            vata = vataPercent,
            pitta = pittaPercent,
            kapha = kaphaPercent,
            primaryDosha = primary,
            secondaryDosha = secondary,
            constitution = constitution,
            constitutionDescription = description
        )
    }

    /**
     * Returns a detailed description of the Ayurvedic constitutional type.
     */
    private fun getConstitutionDescription(primary: String, secondary: String): String {
        return when (primary) {
            "Vata" -> when (secondary) {
                "Pitta" -> "Vata-Pitta constitution: Quick-minded, creative, and intense. " +
                        "Tendency toward anxiety and inflammation under stress. Benefits from " +
                        "grounding routines, warm foods, regular sleep schedule, and oil massage (Abhyanga). " +
                        "Saturn and Mercury dominate the planetary influences, creating a highly " +
                        "intellectual but potentially restless constitution."
                "Kapha" -> "Vata-Kapha constitution: Creative yet stable, but prone to cold conditions. " +
                        "Benefits from warming spices, regular exercise, and stimulating activities. " +
                        "This combination can create periods of high energy alternating with lethargy. " +
                        "Focus on maintaining consistent daily rhythms."
                else -> "Vata-dominant constitution: Highly creative, quick-thinking, and adaptable. " +
                        "Prone to dryness, anxiety, and irregular digestion. Requires grounding, " +
                        "warmth, and routine to maintain balance."
            }
            "Pitta" -> when (secondary) {
                "Vata" -> "Pitta-Vata constitution: Sharp intellect with high drive and ambition. " +
                        "Tendency toward burnout, anger, and digestive fire imbalance. Benefits from " +
                        "cooling foods, moderate exercise, and stress management. Sun and Mars " +
                        "dominate, creating strong leadership potential but risk of overheating."
                "Kapha" -> "Pitta-Kapha constitution: Strong body and mind with good endurance. " +
                        "Tendency toward weight gain and inflammation. Benefits from regular vigorous " +
                        "exercise, bitter/astringent foods, and competitive outlets. This is one of " +
                        "the most resilient constitutional types."
                else -> "Pitta-dominant constitution: Strong digestive fire, sharp intellect, and " +
                        "natural leadership. Prone to inflammation, anger, and skin issues. Requires " +
                        "cooling practices, moderation, and compassion cultivation."
            }
            "Kapha" -> when (secondary) {
                "Vata" -> "Kapha-Vata constitution: Stable and creative but prone to stagnation " +
                        "and irregular energy. Benefits from stimulating activities, light diet, and " +
                        "varied routines. Moon and Venus dominate, creating emotional depth and " +
                        "artistic sensitivity."
                "Pitta" -> "Kapha-Pitta constitution: Strong, enduring, and determined. Tendency " +
                        "toward excess weight and sluggish metabolism. Benefits from vigorous exercise, " +
                        "spicy foods, and challenging goals. This type has excellent physical resilience " +
                        "and emotional stability."
                else -> "Kapha-dominant constitution: Strong, stable, compassionate, and enduring. " +
                        "Prone to lethargy, weight gain, and attachment. Requires stimulation, " +
                        "lightness, and detachment practices to maintain balance."
            }
            else -> "Balanced Tridoshic constitution: Relatively equal distribution of all three doshas. " +
                    "This rare type maintains good health when balanced but can be destabilized by " +
                    "any of the three doshas. Requires awareness of current imbalance direction."
        }
    }

    // ============================================================================
    // ENERGY PROFILE
    // ============================================================================

    /**
     * Calculates the overall energy profile from all seven chakra states.
     */
    private fun calculateEnergyProfile(chakraStates: List<ChakraState>): EnergyProfile {
        val overallScore = chakraStates.map { it.healthScore }.average().roundToInt()
        val balancedCount = chakraStates.count {
            it.status == ChakraStatus.BALANCED || it.status == ChakraStatus.ACTIVE
        }
        val blockedCount = chakraStates.count {
            it.status == ChakraStatus.SEVERELY_BLOCKED || it.status == ChakraStatus.UNDER_ACTIVE
        }
        val overactiveCount = chakraStates.count { it.status == ChakraStatus.OVER_ACTIVE }

        val energyFlow = buildString {
            when {
                blockedCount == 0 && overactiveCount == 0 -> {
                    append("Excellent pranic flow throughout all chakras. Energy moves freely from ")
                    append("Muladhara to Sahasrara without significant blockages. This indicates ")
                    append("good physical health, emotional stability, and spiritual receptivity.")
                }
                blockedCount <= 1 && overactiveCount <= 1 -> {
                    append("Good pranic flow with minor imbalances. ")
                    val blocked = chakraStates.filter {
                        it.status == ChakraStatus.SEVERELY_BLOCKED || it.status == ChakraStatus.UNDER_ACTIVE
                    }
                    if (blocked.isNotEmpty()) {
                        append("Minor blockage at ${blocked.first().chakra.english} ")
                        append("(${blocked.first().chakra.sanskrit}) may restrict energy flow. ")
                    }
                    val overactive = chakraStates.filter { it.status == ChakraStatus.OVER_ACTIVE }
                    if (overactive.isNotEmpty()) {
                        append("Excess energy at ${overactive.first().chakra.english} ")
                        append("may cause imbalance with neighboring chakras. ")
                    }
                }
                blockedCount >= 3 -> {
                    append("Significant pranic blockages detected. Energy flow is restricted at ")
                    append("multiple points, which may manifest as chronic health issues, emotional ")
                    append("stagnation, or spiritual disconnection. Systematic chakra healing work ")
                    append("is strongly recommended, starting from the lowest blocked chakra.")
                }
                else -> {
                    append("Mixed energy flow pattern. Some chakras are well-functioning while ")
                    append("others need attention. Focus on bringing the under-active chakras into ")
                    append("balance while gently moderating any overactive centers.")
                }
            }
        }

        val kundaliniReadiness = buildString {
            val lowerChakrasHealthy = chakraStates
                .filter { it.chakra.number <= 3 }
                .all { it.healthScore >= 40 }
            val upperChakrasActive = chakraStates
                .filter { it.chakra.number >= 5 }
                .any { it.healthScore >= 60 }
            val heartBalanced = chakraStates
                .find { it.chakra == Chakra.ANAHATA }?.healthScore?.let { it >= 50 } ?: false

            when {
                lowerChakrasHealthy && heartBalanced && upperChakrasActive && overallScore >= 60 -> {
                    append("Good foundation for spiritual practices. Lower chakras provide adequate ")
                    append("grounding, the heart center is open, and upper chakras show receptivity. ")
                    append("Meditation, pranayama, and mantra practice will be effective.")
                }
                lowerChakrasHealthy && heartBalanced -> {
                    append("Moderate readiness. Good grounding and emotional balance, but upper ")
                    append("chakras need development. Focus on concentration practices (Dharana) ")
                    append("and devotional meditation to activate higher centers.")
                }
                !lowerChakrasHealthy -> {
                    append("Foundation building needed. Lower chakras require strengthening before ")
                    append("pursuing intense spiritual practices. Focus on physical grounding, ")
                    append("service (Seva), and basic pranayama (Nadi Shodhana) first.")
                }
                else -> {
                    append("Partial readiness. Some aspects of the energy system support spiritual ")
                    append("growth while others need attention. A balanced approach combining ")
                    append("physical, emotional, and spiritual practices is recommended.")
                }
            }
        }

        return EnergyProfile(
            overallScore = overallScore,
            balancedChakraCount = balancedCount,
            blockedChakraCount = blockedCount,
            overactiveChakraCount = overactiveCount,
            energyFlow = energyFlow,
            kundaliniReadiness = kundaliniReadiness
        )
    }

    // ============================================================================
    // REMEDIAL SUGGESTIONS
    // ============================================================================

    /**
     * Generates targeted remedial suggestions for each imbalanced chakra.
     *
     * For blocked/under-active chakras: strengthening remedies.
     * For over-active chakras: grounding and moderating remedies.
     */
    private fun generateRemedialSuggestions(chakraStates: List<ChakraState>): List<ChakraRemedy> {
        val remedies = mutableListOf<ChakraRemedy>()

        for (state in chakraStates) {
            when (state.status) {
                ChakraStatus.SEVERELY_BLOCKED, ChakraStatus.UNDER_ACTIVE -> {
                    remedies.addAll(getBlockedChakraRemedies(state))
                }
                ChakraStatus.OVER_ACTIVE -> {
                    remedies.addAll(getOveractiveChakraRemedies(state))
                }
                else -> { /* Balanced/Active - no remedies needed */ }
            }
        }

        return remedies
    }

    /**
     * Returns remedies for a blocked or under-active chakra.
     */
    private fun getBlockedChakraRemedies(state: ChakraState): List<ChakraRemedy> {
        val remedies = mutableListOf<ChakraRemedy>()
        val c = state.chakra
        val issue = if (state.status == ChakraStatus.SEVERELY_BLOCKED) "Severely Blocked" else "Under-active"

        when (c) {
            Chakra.MULADHARA -> {
                remedies.add(ChakraRemedy(c, issue, "Mantra",
                    "Chant 'LAM' 108 times daily, preferably at sunrise. Also chant Mars beeja mantra " +
                            "'Om Kram Kreem Kraum Sah Bhaumaya Namah' on Tuesdays.",
                    "Wear red coral (Moonga) after consultation, donate red lentils on Tuesdays"))
                remedies.add(ChakraRemedy(c, issue, "Yoga",
                    "Practice grounding asanas: Virabhadrasana I & II (Warrior), Malasana (Garland), " +
                            "Tadasana (Mountain). Walk barefoot on earth daily for 15 minutes.",
                    "Perform Mars-related Havan with red sandalwood"))
                remedies.add(ChakraRemedy(c, issue, "Diet",
                    "Include red foods: beets, red lentils, pomegranate, cherries, red bell peppers. " +
                            "Eat root vegetables: potatoes, carrots, turnips. Use warming spices.",
                    "Offer red flowers to Hanuman on Tuesdays"))
                remedies.add(ChakraRemedy(c, issue, "Lifestyle",
                    "Establish regular daily routines. Practice earthing/grounding meditation. " +
                            "Physical labor, gardening, and connection with nature strengthen Muladhara.",
                    "Saturn remedies: donate black sesame on Saturdays, feed crows"))
            }
            Chakra.SWADHISTHANA -> {
                remedies.add(ChakraRemedy(c, issue, "Mantra",
                    "Chant 'VAM' 108 times daily. Also chant Moon beeja mantra " +
                            "'Om Shram Shreem Shraum Sah Chandraya Namah' on Mondays.",
                    "Wear pearl or moonstone, offer white flowers to Shiva on Mondays"))
                remedies.add(ChakraRemedy(c, issue, "Yoga",
                    "Practice hip-opening asanas: Baddha Konasana (Butterfly), Upavistha Konasana, " +
                            "Pigeon Pose, and gentle pelvic rotations. Supta Baddha Konasana for relaxation.",
                    "Perform Moon-related Havan with white rice offerings"))
                remedies.add(ChakraRemedy(c, issue, "Diet",
                    "Include orange foods: oranges, mangoes, carrots, apricots, sweet potatoes. " +
                            "Ensure adequate hydration. Include healthy fats and sweet tastes.",
                    "Donate white food items (rice, milk, sugar) on Mondays"))
                remedies.add(ChakraRemedy(c, issue, "Lifestyle",
                    "Practice creative expression: painting, music, dance. Spend time near water " +
                            "(rivers, ocean). Allow emotional expression through journaling or art.",
                    "Venus remedies: wear white on Fridays, donate white sweets"))
            }
            Chakra.MANIPURA -> {
                remedies.add(ChakraRemedy(c, issue, "Mantra",
                    "Chant 'RAM' 108 times daily, facing East at sunrise. Sun beeja mantra " +
                            "'Om Hram Hreem Hraum Sah Suryaya Namah' daily for vitality.",
                    "Offer water to Sun at sunrise (Surya Arghya), wear Ruby after evaluation"))
                remedies.add(ChakraRemedy(c, issue, "Yoga",
                    "Practice core-strengthening asanas: Navasana (Boat), Bhujangasana (Cobra), " +
                            "Dhanurasana (Bow). Agni Sara and Kapalabhati pranayama for digestive fire.",
                    "Perform Surya Namaskar (Sun Salutation) 12 rounds daily"))
                remedies.add(ChakraRemedy(c, issue, "Diet",
                    "Include yellow foods: turmeric, bananas, yellow lentils, ginger, corn. " +
                            "Use warming spices: black pepper, ginger, cumin. Avoid cold, heavy foods.",
                    "Donate wheat and jaggery on Sundays"))
                remedies.add(ChakraRemedy(c, issue, "Lifestyle",
                    "Take on leadership challenges. Practice setting healthy boundaries. " +
                            "Engage in competitive activities. Build core physical strength.",
                    "Mars remedies on Tuesdays: donate red items, visit Hanuman temple"))
            }
            Chakra.ANAHATA -> {
                remedies.add(ChakraRemedy(c, issue, "Mantra",
                    "Chant 'YAM' 108 times daily. Venus beeja mantra 'Om Dram Dreem Draum " +
                            "Sah Shukraya Namah' on Fridays for heart opening.",
                    "Wear Diamond or White Sapphire after consultation, donate white items on Fridays"))
                remedies.add(ChakraRemedy(c, issue, "Yoga",
                    "Practice heart-opening asanas: Ustrasana (Camel), Matsyasana (Fish), " +
                            "Setu Bandhasana (Bridge), Anahatasana (Heart Melting Pose).",
                    "Perform Venus Havan with rose petals and ghee"))
                remedies.add(ChakraRemedy(c, issue, "Diet",
                    "Include green foods: leafy greens, broccoli, green tea, kiwi, green grapes. " +
                            "Include rose water, cardamom, and saffron in diet.",
                    "Donate green items, feed cows on Fridays"))
                remedies.add(ChakraRemedy(c, issue, "Lifestyle",
                    "Practice loving-kindness (Metta) meditation. Volunteer for service. " +
                            "Cultivate forgiveness. Spend quality time with loved ones. Practice gratitude.",
                    "Moon remedies on Mondays: wear silver, donate milk"))
            }
            Chakra.VISHUDDHA -> {
                remedies.add(ChakraRemedy(c, issue, "Mantra",
                    "Chant 'HAM' 108 times daily. Mercury beeja mantra 'Om Bram Breem Braum " +
                            "Sah Budhaya Namah' on Wednesdays for communication power.",
                    "Wear Emerald after consultation, donate green moong dal on Wednesdays"))
                remedies.add(ChakraRemedy(c, issue, "Yoga",
                    "Practice throat-opening asanas: Sarvangasana (Shoulder Stand), Halasana (Plough), " +
                            "Simhasana (Lion Pose with tongue extension). Ujjayi pranayama.",
                    "Perform Mercury Havan with green herbs"))
                remedies.add(ChakraRemedy(c, issue, "Diet",
                    "Include blue foods: blueberries, blackberries, plums. Soothe throat with warm " +
                            "honey-ginger water. Include high-quality oils for throat lubrication.",
                    "Donate educational materials, books on Wednesdays"))
                remedies.add(ChakraRemedy(c, issue, "Lifestyle",
                    "Practice singing, chanting, or recitation daily. Express authentic truth. " +
                            "Journaling and public speaking strengthen Vishuddha. Learn a new language.",
                    "Jupiter remedies on Thursdays: wear yellow topaz, donate bananas"))
            }
            Chakra.AJNA -> {
                remedies.add(ChakraRemedy(c, issue, "Mantra",
                    "Chant 'OM' 108 times daily in meditation. Jupiter beeja mantra 'Om Gram Greem " +
                            "Graum Sah Gurave Namah' on Thursdays for wisdom activation.",
                    "Wear Yellow Sapphire after consultation, donate yellow items on Thursdays"))
                remedies.add(ChakraRemedy(c, issue, "Yoga",
                    "Practice Trataka (candle-gazing meditation), Nadi Shodhana (alternate nostril " +
                            "breathing), Shambhavi Mudra (eyebrow center gazing). Child's Pose with " +
                            "forehead touching ground.",
                    "Perform Jupiter Havan with turmeric and yellow flowers"))
                remedies.add(ChakraRemedy(c, issue, "Diet",
                    "Include purple/indigo foods: purple grapes, eggplant, purple cabbage. " +
                            "Include brain-nourishing foods: walnuts, almonds, brahmi, shankhapushpi.",
                    "Donate educational materials and spiritual books on Thursdays"))
                remedies.add(ChakraRemedy(c, issue, "Lifestyle",
                    "Reduce excessive screen time. Practice visualization exercises. " +
                            "Study sacred texts and philosophy. Maintain a dream journal. " +
                            "Practice Yoga Nidra for subconscious clearing.",
                    "Ketu remedies: donate grey blankets, chant Ganesha mantras"))
            }
            Chakra.SAHASRARA -> {
                remedies.add(ChakraRemedy(c, issue, "Mantra",
                    "Practice silent meditation (Mauna) daily. Chant 'Om Namah Shivaya' or " +
                            "Gayatri Mantra 108 times at dawn for consciousness expansion.",
                    "Ketu remedies: chant 'Om Ketave Namah' 108 times, donate multi-colored blankets"))
                remedies.add(ChakraRemedy(c, issue, "Yoga",
                    "Practice Shirshasana (Headstand - only if experienced), Savasana with extended " +
                            "relaxation, and Viparita Karani (Legs Up the Wall). Deep meditation in " +
                            "Padmasana or Siddhasana.",
                    "Perform Ketu Havan with Kusha grass"))
                remedies.add(ChakraRemedy(c, issue, "Diet",
                    "Practice periodic fasting (Ekadashi fasts). Include sattvic foods: fresh fruits, " +
                            "light grains, pure water. Reduce heavy, tamasic foods. Include tulsi tea.",
                    "Donate on Ekadashi days, support spiritual institutions"))
                remedies.add(ChakraRemedy(c, issue, "Lifestyle",
                    "Dedicate time to selfless service (Nishkama Karma). Practice detachment from " +
                            "outcomes. Spend time in nature and silence. Study Advaita Vedanta or " +
                            "similar non-dual philosophy.",
                    "Sun remedies: Surya Namaskar at dawn, donate wheat on Sundays"))
            }
        }

        return remedies
    }

    /**
     * Returns remedies for an over-active chakra.
     */
    private fun getOveractiveChakraRemedies(state: ChakraState): List<ChakraRemedy> {
        val remedies = mutableListOf<ChakraRemedy>()
        val c = state.chakra
        val issue = "Over-active"

        when (c) {
            Chakra.MULADHARA -> {
                remedies.add(ChakraRemedy(c, issue, "Lifestyle",
                    "Over-active Root chakra can manifest as materialism, hoarding, fear-based " +
                            "decision making, and resistance to change. Practice letting go of excess " +
                            "possessions. Engage in charitable giving. Practice trust and surrender.",
                    "Reduce Saturn's material grip: donate iron items on Saturdays"))
                remedies.add(ChakraRemedy(c, issue, "Yoga",
                    "Balance with upper chakra practices: meditation, chanting, and pranayama " +
                            "to draw energy upward. Reduce heavy physical exercise temporarily.",
                    "Practice Ketu-oriented detachment meditations"))
            }
            Chakra.SWADHISTHANA -> {
                remedies.add(ChakraRemedy(c, issue, "Lifestyle",
                    "Over-active Sacral chakra can manifest as emotional volatility, attachment, " +
                            "excessive sensual indulgence, and co-dependency. Practice emotional " +
                            "boundaries. Engage in disciplined creative expression.",
                    "Balance Moon's excess with Saturn's discipline: structured routines"))
                remedies.add(ChakraRemedy(c, issue, "Yoga",
                    "Practice grounding and centering: Tadasana, Padmasana. Engage navel lock " +
                            "(Uddiyana Bandha) to draw energy upward from sacral region.",
                    "Fast on Ekadashi to moderate Venus and Moon excess"))
            }
            Chakra.MANIPURA -> {
                remedies.add(ChakraRemedy(c, issue, "Lifestyle",
                    "Over-active Solar Plexus can manifest as domineering behavior, anger, " +
                            "perfectionism, and digestive inflammation. Practice humility and " +
                            "acceptance. Engage in cooling activities.",
                    "Cool Sun and Mars: drink sandalwood water, apply sandalwood paste"))
                remedies.add(ChakraRemedy(c, issue, "Diet",
                    "Include cooling foods: cucumber, coconut water, mint, coriander, fennel. " +
                            "Reduce spicy, hot, and fermented foods. Practice Sheetali pranayama.",
                    "Donate wheat on Sundays, perform Moon-cooling remedies on Mondays"))
            }
            Chakra.ANAHATA -> {
                remedies.add(ChakraRemedy(c, issue, "Lifestyle",
                    "Over-active Heart chakra can manifest as excessive self-sacrifice, poor " +
                            "boundaries, co-dependency, and jealousy in love. Practice healthy " +
                            "self-love and boundary-setting. Engage in solo contemplation.",
                    "Balance Venus excess with Saturn's boundaries: structured self-care routines"))
                remedies.add(ChakraRemedy(c, issue, "Yoga",
                    "Practice Mula Bandha to ground excess heart energy downward. " +
                            "Forward folds (Uttanasana, Paschimottanasana) help introvert excess energy.",
                    "Chant Saturn mantra on Saturdays for healthy boundaries"))
            }
            Chakra.VISHUDDHA -> {
                remedies.add(ChakraRemedy(c, issue, "Lifestyle",
                    "Over-active Throat chakra can manifest as excessive talking, gossip, " +
                            "inability to listen, and verbal aggression. Practice Noble Silence " +
                            "(Mauna) periodically. Cultivate deep listening.",
                    "Balance Mercury's excess communication with Saturn's discipline"))
                remedies.add(ChakraRemedy(c, issue, "Yoga",
                    "Practice Mauna (silence) for at least 1 hour daily. Jalandhara Bandha " +
                            "(chin lock) in meditation. Listen more than you speak.",
                    "Fast on Wednesdays to moderate Mercury's verbal energy"))
            }
            Chakra.AJNA -> {
                remedies.add(ChakraRemedy(c, issue, "Lifestyle",
                    "Over-active Third Eye can manifest as excessive fantasy, disconnection from " +
                            "reality, headaches, and difficulty with practical tasks. Practice " +
                            "grounding activities: gardening, cooking, physical exercise.",
                    "Ground Jupiter's excess with Saturn's practicality and Mars's physical action"))
                remedies.add(ChakraRemedy(c, issue, "Yoga",
                    "Reduce meditation duration temporarily. Increase physical asana practice. " +
                            "Walk barefoot. Practice Mula Bandha to draw energy downward.",
                    "Strengthen Muladhara with Mars remedies: physical service on Tuesdays"))
            }
            Chakra.SAHASRARA -> {
                remedies.add(ChakraRemedy(c, issue, "Lifestyle",
                    "Over-active Crown chakra can manifest as spiritual bypassing, dissociation, " +
                            "difficulty with worldly responsibilities, and ungroundedness. Practice " +
                            "engagement with practical life. Help others with concrete tasks.",
                    "Ground Ketu's excess detachment with Rahu's worldly engagement"))
                remedies.add(ChakraRemedy(c, issue, "Yoga",
                    "Practice vigorous physical exercise. Engage all five senses consciously. " +
                            "Eat grounding foods (root vegetables). Reduce spiritual practices " +
                            "temporarily and increase worldly engagement.",
                    "Strengthen lower chakras: Mars and Saturn remedies for grounding"))
            }
        }

        return remedies
    }

    // ============================================================================
    // PLANET-CHAKRA MAP BUILDING
    // ============================================================================

    /**
     * Builds the complete map of each planet to its chakra associations.
     */
    private fun buildPlanetChakraMap(chart: VedicChart): Map<Planet, List<ChakraAssociation>> {
        val result = mutableMapOf<Planet, MutableList<ChakraAssociation>>()

        for ((chakra, planetWeights) in CHAKRA_PLANET_MAP) {
            for (cpw in planetWeights) {
                val associations = result.getOrPut(cpw.planet) { mutableListOf() }
                val influenceType = determineInfluenceType(cpw.planet, chakra, chart)
                associations.add(
                    ChakraAssociation(
                        planet = cpw.planet,
                        primaryChakra = chakra,
                        secondaryChakra = null,
                        influenceType = influenceType
                    )
                )
            }
        }

        // Add Rahu's shadow influences
        for (influence in RAHU_INFLUENCES) {
            val associations = result.getOrPut(Planet.RAHU) { mutableListOf() }
            associations.add(
                ChakraAssociation(
                    planet = Planet.RAHU,
                    primaryChakra = influence.chakra,
                    secondaryChakra = null,
                    influenceType = influence.influence
                )
            )
        }

        // Add Ketu's shadow influences
        for (influence in KETU_INFLUENCES) {
            val associations = result.getOrPut(Planet.KETU) { mutableListOf() }
            associations.add(
                ChakraAssociation(
                    planet = Planet.KETU,
                    primaryChakra = influence.chakra,
                    secondaryChakra = null,
                    influenceType = influence.influence
                )
            )
        }

        return result
    }

    /**
     * Determines the type of influence a planet has on a chakra based on
     * its current state in the chart.
     */
    private fun determineInfluenceType(
        planet: Planet,
        chakra: Chakra,
        chart: VedicChart
    ): ChakraInfluenceType {
        val position = chart.planetPositions.find { it.planet == planet }
            ?: return ChakraInfluenceType.NEUTRAL

        val isExalted = VedicAstrologyUtils.isExalted(position)
        val isDebilitated = VedicAstrologyUtils.isDebilitated(position)
        val isInDusthana = position.house in VedicAstrologyUtils.DUSTHANA_HOUSES

        return when {
            isExalted -> ChakraInfluenceType.HARMONIZING
            isDebilitated -> ChakraInfluenceType.BLOCKING
            isInDusthana -> ChakraInfluenceType.DISRUPTING
            VedicAstrologyUtils.isInOwnSign(position) -> ChakraInfluenceType.HARMONIZING
            else -> ChakraInfluenceType.NEUTRAL
        }
    }

    // ============================================================================
    // UTILITY METHODS
    // ============================================================================

    /**
     * Estimates planet strength when Shadbala is not available.
     *
     * Uses a simplified scoring based on dignity and house placement.
     * This provides a reasonable approximation for Rahu/Ketu (which don't
     * have classical Shadbala) and for cases where full Shadbala computation
     * is not available.
     *
     * @return Estimated strength in Rupas-equivalent scale
     */
    private fun estimatePlanetStrength(
        planet: Planet,
        position: PlanetPosition?,
        chart: VedicChart
    ): Double {
        if (position == null) return 3.0 // Minimal default

        var strength = 4.0 // Baseline

        // Dignity adjustments
        if (VedicAstrologyUtils.isExalted(position)) strength += 3.0
        if (VedicAstrologyUtils.isInOwnSign(position)) strength += 2.0
        if (VedicAstrologyUtils.isInMoolatrikona(position)) strength += 1.5
        if (VedicAstrologyUtils.isDebilitated(position)) strength -= 2.5
        if (VedicAstrologyUtils.isInFriendSign(position)) strength += 1.0
        if (VedicAstrologyUtils.isInEnemySign(position)) strength -= 1.0

        // House placement
        when (position.house) {
            1, 4, 7, 10 -> strength += 1.5  // Kendra
            5, 9 -> strength += 1.5          // Trikona
            2, 11 -> strength += 0.5         // Wealth houses
            6, 8, 12 -> strength -= 1.0      // Dusthana
        }

        // Retrograde bonus for certain planets
        if (position.isRetrograde && planet in setOf(Planet.JUPITER, Planet.SATURN)) {
            strength += 0.5
        }

        return strength.coerceIn(1.0, 12.0)
    }

    /**
     * Estimates shadow planet (Rahu/Ketu) strength on a 0-100 scale.
     *
     * Since Rahu and Ketu don't have classical Shadbala, their strength is
     * estimated from sign placement, house position, and nakshatra dignity.
     */
    private fun estimateShadowPlanetStrength(
        position: PlanetPosition,
        chart: VedicChart
    ): Double {
        var strength = 50.0

        // Rahu is strong in Gemini, Virgo, Aquarius; Ketu in Sagittarius, Pisces, Scorpio
        when (position.planet) {
            Planet.RAHU -> {
                if (position.sign in listOf(ZodiacSign.GEMINI, ZodiacSign.VIRGO, ZodiacSign.AQUARIUS)) {
                    strength += 20.0
                }
                if (position.sign == ZodiacSign.TAURUS) strength += 15.0 // Exaltation
                if (position.sign == ZodiacSign.SCORPIO) strength -= 15.0 // Debilitation
            }
            Planet.KETU -> {
                if (position.sign in listOf(ZodiacSign.SAGITTARIUS, ZodiacSign.PISCES, ZodiacSign.SCORPIO)) {
                    strength += 20.0
                }
                if (position.sign == ZodiacSign.SCORPIO) strength += 15.0 // Exaltation
                if (position.sign == ZodiacSign.TAURUS) strength -= 15.0 // Debilitation
            }
            else -> { /* Not a shadow planet */ }
        }

        // House placement
        when (position.house) {
            3, 6, 10, 11 -> strength += 10.0 // Upachaya houses
            1, 4, 7, 10 -> strength += 5.0   // Kendra
            8, 12 -> strength -= 5.0          // Dusthana (excluding 6 which is good for nodes)
        }

        return strength.coerceIn(0.0, 100.0)
    }
}
