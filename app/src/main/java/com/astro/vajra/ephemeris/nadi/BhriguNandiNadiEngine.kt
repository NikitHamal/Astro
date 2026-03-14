package com.astro.vajra.ephemeris.nadi

import com.astro.vajra.core.model.Planet
import com.astro.vajra.core.model.PlanetPosition
import com.astro.vajra.core.model.VedicChart
import com.astro.vajra.core.model.ZodiacSign
import com.astro.vajra.ephemeris.VedicAstrologyUtils
import kotlin.math.abs
import kotlin.math.min

/**
 * Bhrigu Nandi Nadi (BNN) Aspect Engine
 *
 * Bhrigu Nandi Nadi is a unique system within the broader Nadi astrology tradition,
 * attributed to Maharishi Bhrigu. Unlike mainstream Parashari astrology, BNN operates
 * under fundamentally different principles:
 *
 * **Core Principle: Lagna is Ignored.**
 * BNN does not use the Ascendant (Lagna) for interpretation. Instead, it reads
 * results exclusively from the sign positions of planets and the "links"
 * (connections) between them based on their relative sign placements.
 *
 * **BNN Aspect Types:**
 * 1. **Trine Link (1-5-9)**: Planets in signs that are trine to each other
 *    (same sign, 5th from, or 9th from) form the strongest connections. These
 *    are considered primary links in BNN.
 * 2. **Adjacent Link (2-12)**: Planets in adjacent signs (2nd or 12th from each
 *    other) form secondary connections indicating strong mutual influence through
 *    proximity.
 * 3. **Opposition Link (7)**: Planets in the 7th sign from each other form
 *    opposition connections, which in BNN indicate a tension-driven dynamic
 *    that forces results to manifest.
 *
 * **Planetary Chains:**
 * BNN reads results by tracing chains of connected planets. When planet A links
 * to planet B, and planet B links to planet C, the entire chain A-B-C produces
 * a combined result. The interpretation depends on which planets are in the chain
 * and which signs they occupy.
 *
 * **Consecutive Sign Rule:**
 * When two or more planets occupy consecutive signs (e.g., one in Aries, another
 * in Taurus), they exert a particularly strong mutual influence. This is treated
 * as a special intensifier in BNN readings.
 *
 * **BNN Sign-Based Readings:**
 * Each planet's sign position defines a life domain:
 * - Jupiter's sign = Dharma, education, children, husband (for female charts)
 * - Saturn's sign = Karma, profession, longevity, chronic conditions
 * - Mars's sign = Energy, siblings, property, surgery, accidents
 * - Venus's sign = Marriage, spouse (for male charts), luxury, arts, vehicles
 * - Mercury's sign = Intelligence, communication, friends, skin, nervous system
 * - Sun's sign = Father, authority, government relations, soul purpose
 * - Moon's sign = Mother, mind, emotions, public life, mental health
 * - Rahu's sign = Foreign connections, unconventional pursuits, obsessions
 * - Ketu's sign = Spiritual evolution, past-life karma, detachment, losses
 *
 * References:
 * - Bhrigu Nandi Nadi (R.G. Rao)
 * - Nadi Astrology: Its Theory and Practice (R.G. Rao)
 * - Bhrigu Sutras
 * - Devakeralam (Chandra Kala Nadi)
 *
 * @author AstroVajra -- Ultra-Precision Vedic Astrology
 */
object BhriguNandiNadiEngine {

    // ========================================================================
    // DATA CLASSES
    // ========================================================================

    /**
     * Complete BNN analysis containing the planetary graph, discovered chains,
     * per-planet readings, life event predictions, and consecutive sign groups.
     */
    data class BNNAnalysis(
        /** The weighted graph of planetary connections */
        val planetaryGraph: PlanetaryGraph,
        /** All discovered chains of connected planets */
        val planetaryChains: List<PlanetaryChain>,
        /** BNN readings for each planet based on its sign and links */
        val planetReadings: Map<Planet, BNNPlanetReading>,
        /** Specific life event predictions derived from chain analysis */
        val lifeEventPredictions: List<BNNPrediction>,
        /** Groups of planets in consecutive signs (strong mutual influence) */
        val consecutiveSignPlanets: List<ConsecutiveSignGroup>,
        /** Summary of the overall BNN reading */
        val overallSummary: String
    )

    /**
     * Graph representation where nodes are planets and edges represent BNN
     * aspect connections with type and strength.
     */
    data class PlanetaryGraph(
        /** Planet nodes with their sign positions */
        val nodes: Map<Planet, PlanetNode>,
        /** All edges (aspect connections) between planets */
        val edges: List<PlanetaryEdge>,
        /** Adjacency list for efficient traversal */
        val adjacencyList: Map<Planet, List<PlanetaryEdge>>
    )

    /**
     * A node in the planetary graph representing a planet's sign position.
     */
    data class PlanetNode(
        val planet: Planet,
        val sign: ZodiacSign,
        val degree: Double,
        val isRetrograde: Boolean,
        /** Planets sharing the same sign (co-tenants) */
        val coTenants: List<Planet>
    )

    /**
     * An edge in the planetary graph representing a BNN aspect connection.
     */
    data class PlanetaryEdge(
        val fromPlanet: Planet,
        val toPlanet: Planet,
        val aspectType: BNNAspectType,
        /** Connection strength from 0.0 (weakest) to 1.0 (strongest) */
        val strength: Double,
        /** Sign distance from the source planet's sign (1-12) */
        val signDistance: Int,
        /** Description of the connection */
        val description: String
    )

    /**
     * BNN aspect types ordered by strength.
     */
    enum class BNNAspectType(val label: String, val baseStrength: Double) {
        /** Same sign (conjunction) -- strongest link */
        CONJUNCTION("Conjunction (Same Sign)", 1.0),
        /** 5th or 9th from -- trine link, primary BNN connection */
        TRINE("Trine Link (1-5-9)", 0.90),
        /** 2nd or 12th from -- adjacent sign link, secondary */
        ADJACENT("Adjacent Link (2-12)", 0.70),
        /** 7th from -- opposition link, tension-driven */
        OPPOSITION("Opposition Link (7)", 0.65)
    }

    /**
     * A chain of connected planets discovered through graph traversal.
     */
    data class PlanetaryChain(
        /** Ordered list of planets in the chain */
        val planets: List<Planet>,
        /** Signs occupied by the chain's planets */
        val signs: List<ZodiacSign>,
        /** Average connection strength across the chain */
        val averageStrength: Double,
        /** Chain length (number of planets) */
        val length: Int,
        /** Combined BNN interpretation of this chain */
        val interpretation: String,
        /** Life domains activated by this chain */
        val activatedDomains: List<String>
    )

    /**
     * BNN reading for a single planet, combining its sign-based significations
     * with the effects of its linked planets.
     */
    data class BNNPlanetReading(
        val planet: Planet,
        val sign: ZodiacSign,
        val degree: Double,
        /** Core BNN significations based on the planet's sign */
        val coreSignification: String,
        /** Effects of linked planets on this planet's significations */
        val linkedEffects: List<LinkedEffect>,
        /** Overall BNN reading synthesizing all links */
        val overallReading: String,
        /** Strength rating from 1 (weakest) to 5 (strongest) */
        val strengthRating: Int,
        /** Planets directly linked to this planet */
        val linkedPlanets: List<Planet>
    )

    /**
     * Effect of a specific linked planet on the reading.
     */
    data class LinkedEffect(
        val linkedPlanet: Planet,
        val aspectType: BNNAspectType,
        val strength: Double,
        val effect: String
    )

    /**
     * A specific life event prediction derived from BNN chain analysis.
     */
    data class BNNPrediction(
        /** The planetary combination triggering this prediction */
        val involvedPlanets: List<Planet>,
        /** The life domain affected */
        val domain: String,
        /** The predicted outcome */
        val prediction: String,
        /** Confidence level based on chain strength and classical rules */
        val confidence: PredictionConfidence,
        /** Classical BNN reference for this combination */
        val classicalReference: String
    )

    /** Confidence levels for BNN predictions */
    enum class PredictionConfidence(val label: String) {
        HIGH("High - Strong multi-planet chain with classical support"),
        MEDIUM("Medium - Moderate chain with partial classical support"),
        LOW("Low - Weak or indirect chain connection")
    }

    /**
     * A group of planets occupying consecutive zodiac signs, forming a
     * BNN consecutive-sign cluster with amplified mutual influence.
     */
    data class ConsecutiveSignGroup(
        /** Planets in consecutive signs, ordered by sign */
        val planets: List<Planet>,
        /** The consecutive signs occupied */
        val signs: List<ZodiacSign>,
        /** Interpretation of this consecutive group */
        val interpretation: String,
        /** Strength multiplier applied to these planets' mutual effects */
        val strengthMultiplier: Double
    )

    // ========================================================================
    // BNN SIGN SIGNIFICATIONS (Classical Nadi Texts)
    // ========================================================================

    /**
     * Core BNN signification for each planet based on its sign.
     * In BNN, the *sign* a planet occupies tells us about specific life areas;
     * the *planet* itself represents the karaka (significator) of those areas.
     */
    private val BNN_PLANET_SIGNIFICATIONS: Map<Planet, PlanetSignification> = mapOf(
        Planet.JUPITER to PlanetSignification(
            domains = listOf("Dharma", "Education", "Children", "Husband (for female)", "Higher learning", "Wisdom", "Spiritual teacher"),
            signEffects = mapOf(
                ZodiacSign.ARIES to "Initiative in education and dharma; pioneering spiritual approach; energetic teaching",
                ZodiacSign.TAURUS to "Stable wealth through dharma; artistic education; materially comfortable spiritual path",
                ZodiacSign.GEMINI to "Intellectual dharma path; dual educational interests; communicative teaching style",
                ZodiacSign.CANCER to "Nurturing approach to dharma; emotional wisdom; mother-like spiritual guidance",
                ZodiacSign.LEO to "Authoritative dharma; leadership in education; royal/governmental spiritual connections",
                ZodiacSign.VIRGO to "Analytical approach to dharma; detail-oriented education; service-oriented wisdom",
                ZodiacSign.LIBRA to "Balanced dharma; education in arts and justice; diplomatic spiritual approach",
                ZodiacSign.SCORPIO to "Transformative dharma; deep occult education; intense spiritual practices",
                ZodiacSign.SAGITTARIUS to "Natural dharma lord; excellent education; traditional spiritual teaching",
                ZodiacSign.CAPRICORN to "Structured dharma; practical education; disciplined spiritual path",
                ZodiacSign.AQUARIUS to "Humanitarian dharma; unconventional education; group-oriented spirituality",
                ZodiacSign.PISCES to "Transcendent dharma; intuitive education; natural spiritual inclination"
            )
        ),
        Planet.SATURN to PlanetSignification(
            domains = listOf("Karma", "Profession", "Longevity", "Chronic conditions", "Discipline", "Service", "Delays"),
            signEffects = mapOf(
                ZodiacSign.ARIES to "Frustrated professional ambition; delayed career start; courage through hardship",
                ZodiacSign.TAURUS to "Steady professional growth; financial discipline; long-term material security",
                ZodiacSign.GEMINI to "Communication-related profession; mental discipline; systematic learning",
                ZodiacSign.CANCER to "Emotional burdens in career; service to family; property-related profession",
                ZodiacSign.LEO to "Authority conflicts; governmental service; delayed recognition",
                ZodiacSign.VIRGO to "Detail-oriented profession; health-related career; analytical service",
                ZodiacSign.LIBRA to "Exalted Saturn -- excellent career; justice-related profession; balanced karma",
                ZodiacSign.SCORPIO to "Intense professional life; research or investigation career; transformative karma",
                ZodiacSign.SAGITTARIUS to "Dharma-aligned profession; teaching career; philosophical discipline",
                ZodiacSign.CAPRICORN to "Own-sign strength; natural authority; steady career climb; administrative roles",
                ZodiacSign.AQUARIUS to "Innovative profession; technology or social service; group leadership",
                ZodiacSign.PISCES to "Spiritual profession; service in institutions; behind-the-scenes work"
            )
        ),
        Planet.MARS to PlanetSignification(
            domains = listOf("Energy", "Siblings", "Property", "Surgery", "Accidents", "Courage", "Technical skills"),
            signEffects = mapOf(
                ZodiacSign.ARIES to "Own-sign strength; excellent energy; leadership; property gains; courageous siblings",
                ZodiacSign.TAURUS to "Property accumulation; materialistic energy; stubborn courage; fixed assets",
                ZodiacSign.GEMINI to "Technical communication; skilled hands; multiple property interests; intellectual courage",
                ZodiacSign.CANCER to "Debilitated energy; emotional volatility; property near water; protective of family",
                ZodiacSign.LEO to "Authoritative energy; government property; commanding siblings; surgical skill",
                ZodiacSign.VIRGO to "Analytical energy; health-related property; technical precision; service-oriented courage",
                ZodiacSign.LIBRA to "Diplomatic energy; shared property; partnership in ventures; balanced aggression",
                ZodiacSign.SCORPIO to "Own-sign strength; intense energy; hidden property; surgical precision; deep courage",
                ZodiacSign.SAGITTARIUS to "Dharmic energy; foreign property; educational ventures; philosophical courage",
                ZodiacSign.CAPRICORN to "Exalted Mars; peak energy; government property; structured courage; career in engineering",
                ZodiacSign.AQUARIUS to "Innovative energy; group property; humanitarian ventures; unconventional courage",
                ZodiacSign.PISCES to "Spiritual energy; property near water; hospital or institution-related; subtle courage"
            )
        ),
        Planet.VENUS to PlanetSignification(
            domains = listOf("Marriage", "Spouse (male chart)", "Luxury", "Arts", "Vehicles", "Beauty", "Comfort"),
            signEffects = mapOf(
                ZodiacSign.ARIES to "Passionate marriage; independent spouse; quick romantic attraction; love of sports vehicles",
                ZodiacSign.TAURUS to "Own-sign; stable marriage; beautiful spouse; luxury accumulation; artistic talent",
                ZodiacSign.GEMINI to "Communicative partner; intellectual romance; multiple vehicles; writing or media arts",
                ZodiacSign.CANCER to "Nurturing spouse; emotional marriage; comfortable home; domestic arts",
                ZodiacSign.LEO to "Royal marriage; proud spouse; luxury vehicles; performing arts",
                ZodiacSign.VIRGO to "Debilitated; critical spouse; health-conscious partner; practical arts; modest luxury",
                ZodiacSign.LIBRA to "Own-sign; ideal marriage; harmonious spouse; refined luxury; balanced artistic expression",
                ZodiacSign.SCORPIO to "Intense marriage; transformative relationships; hidden luxury; occult arts",
                ZodiacSign.SAGITTARIUS to "Dharmic marriage; foreign spouse possible; philosophical arts; spiritual luxury",
                ZodiacSign.CAPRICORN to "Practical marriage; responsible spouse; career-oriented luxury; structured arts",
                ZodiacSign.AQUARIUS to "Unconventional marriage; humanitarian spouse; unique vehicles; innovative arts",
                ZodiacSign.PISCES to "Exalted; divine marriage; spiritual spouse; transcendent beauty; artistic mastery"
            )
        ),
        Planet.MERCURY to PlanetSignification(
            domains = listOf("Intelligence", "Communication", "Friends", "Skin", "Nervous system", "Commerce", "Education"),
            signEffects = mapOf(
                ZodiacSign.ARIES to "Quick intelligence; impulsive communication; competitive friends; entrepreneurial commerce",
                ZodiacSign.TAURUS to "Stable intelligence; melodious voice; wealthy friends; financial commerce",
                ZodiacSign.GEMINI to "Own-sign; brilliant intelligence; excellent communication; diverse friends; natural merchant",
                ZodiacSign.CANCER to "Emotional intelligence; intuitive communication; nurturing friends; food commerce",
                ZodiacSign.LEO to "Authoritative communication; government connections; proud intellectual pursuits",
                ZodiacSign.VIRGO to "Exalted; analytical intelligence; precise communication; discriminating friends; health commerce",
                ZodiacSign.LIBRA to "Diplomatic intelligence; balanced communication; artistic friends; luxury commerce",
                ZodiacSign.SCORPIO to "Deep intelligence; secretive communication; intense friendships; research-oriented",
                ZodiacSign.SAGITTARIUS to "Philosophical intelligence; teaching communication; dharmic friends; educational commerce",
                ZodiacSign.CAPRICORN to "Practical intelligence; structured communication; professional friends; systematic commerce",
                ZodiacSign.AQUARIUS to "Innovative intelligence; humanitarian communication; group friendships; technology commerce",
                ZodiacSign.PISCES to "Debilitated; intuitive but scattered intelligence; imaginative communication; spiritual friends"
            )
        ),
        Planet.SUN to PlanetSignification(
            domains = listOf("Father", "Authority", "Government", "Soul purpose", "Vitality", "Self-expression"),
            signEffects = mapOf(
                ZodiacSign.ARIES to "Exalted; powerful father; strong authority; government connection; vibrant vitality",
                ZodiacSign.TAURUS to "Wealthy father; stable authority; financial government role; steady vitality",
                ZodiacSign.GEMINI to "Communicative father; intellectual authority; media or communication government role",
                ZodiacSign.CANCER to "Nurturing father; emotional authority; social welfare government role",
                ZodiacSign.LEO to "Own-sign; strong father figure; natural authority; governmental leadership; peak vitality",
                ZodiacSign.VIRGO to "Analytical father; service-oriented authority; health government role; moderate vitality",
                ZodiacSign.LIBRA to "Debilitated; compromised father figure; diplomatic authority; judicial government role",
                ZodiacSign.SCORPIO to "Intense father; secretive authority; intelligence or research government role",
                ZodiacSign.SAGITTARIUS to "Dharmic father; philosophical authority; educational government role; strong vitality",
                ZodiacSign.CAPRICORN to "Disciplined father; structured authority; administrative government role",
                ZodiacSign.AQUARIUS to "Humanitarian father; progressive authority; social reform government role",
                ZodiacSign.PISCES to "Spiritual father; charitable authority; institutional government role; gentle vitality"
            )
        ),
        Planet.MOON to PlanetSignification(
            domains = listOf("Mother", "Mind", "Emotions", "Public life", "Mental health", "Nourishment"),
            signEffects = mapOf(
                ZodiacSign.ARIES to "Active mind; impulsive emotions; independent mother; public leadership",
                ZodiacSign.TAURUS to "Exalted; stable mind; grounded emotions; nurturing mother; public prosperity",
                ZodiacSign.GEMINI to "Versatile mind; curious emotions; communicative mother; media public image",
                ZodiacSign.CANCER to "Own-sign; deep emotions; strong maternal bond; public nurturing role",
                ZodiacSign.LEO to "Proud mind; dramatic emotions; authoritative mother; royal public image",
                ZodiacSign.VIRGO to "Analytical mind; health-conscious emotions; service-oriented mother",
                ZodiacSign.LIBRA to "Balanced mind; harmonious emotions; artistic mother; diplomatic public image",
                ZodiacSign.SCORPIO to "Debilitated; intense mind; turbulent emotions; transformative mother figure",
                ZodiacSign.SAGITTARIUS to "Philosophical mind; optimistic emotions; dharmic mother; teaching public role",
                ZodiacSign.CAPRICORN to "Disciplined mind; restrained emotions; hardworking mother; professional public image",
                ZodiacSign.AQUARIUS to "Innovative mind; detached emotions; humanitarian mother; group public image",
                ZodiacSign.PISCES to "Intuitive mind; compassionate emotions; spiritual mother; charitable public image"
            )
        ),
        Planet.RAHU to PlanetSignification(
            domains = listOf("Foreign connections", "Unconventional pursuits", "Obsession", "Innovation", "Amplification"),
            signEffects = mapOf(
                ZodiacSign.ARIES to "Obsessive ambition; foreign military or sports; unconventional leadership",
                ZodiacSign.TAURUS to "Exalted (per some); material obsession; foreign wealth; unconventional luxury",
                ZodiacSign.GEMINI to "Obsessive communication; foreign media; unconventional intelligence; technology",
                ZodiacSign.CANCER to "Emotional obsession; foreign homeland; unconventional family; public manipulation",
                ZodiacSign.LEO to "Power obsession; foreign authority; unconventional leadership; political ambition",
                ZodiacSign.VIRGO to "Health obsession; foreign medical; unconventional service; perfectionist drive",
                ZodiacSign.LIBRA to "Relationship obsession; foreign partnerships; unconventional marriage",
                ZodiacSign.SCORPIO to "Debilitated (per some); occult obsession; foreign secrets; underground connections",
                ZodiacSign.SAGITTARIUS to "Dharma obsession; foreign religion or education; unconventional teaching",
                ZodiacSign.CAPRICORN to "Career obsession; foreign profession; unconventional authority; political ambition",
                ZodiacSign.AQUARIUS to "Innovation obsession; foreign technology; unconventional science; humanitarian drive",
                ZodiacSign.PISCES to "Spiritual obsession; foreign spirituality; unconventional charity; psychic ability"
            )
        ),
        Planet.KETU to PlanetSignification(
            domains = listOf("Spiritual evolution", "Past-life karma", "Detachment", "Losses", "Liberation", "Intuition"),
            signEffects = mapOf(
                ZodiacSign.ARIES to "Past-life warrior; karmic leadership; spiritual initiative; detachment from ego",
                ZodiacSign.TAURUS to "Past-life wealth; karmic material lessons; spiritual simplicity; detachment from luxury",
                ZodiacSign.GEMINI to "Past-life scholar; karmic communication; spiritual intellect; detachment from information",
                ZodiacSign.CANCER to "Past-life family karma; emotional detachment; spiritual nurturing; loss of home comfort",
                ZodiacSign.LEO to "Past-life authority; karmic power; spiritual leadership; detachment from recognition",
                ZodiacSign.VIRGO to "Past-life service; karmic health; spiritual healing; detachment from perfection",
                ZodiacSign.LIBRA to "Past-life relationship karma; spiritual balance; detachment from partnerships",
                ZodiacSign.SCORPIO to "Exalted (per some); deep past-life karma; spiritual transformation; mystical power",
                ZodiacSign.SAGITTARIUS to "Past-life dharma; karmic teaching; spiritual evolution through philosophy",
                ZodiacSign.CAPRICORN to "Past-life career karma; spiritual discipline; detachment from worldly status",
                ZodiacSign.AQUARIUS to "Past-life humanitarian karma; spiritual innovation; detachment from group identity",
                ZodiacSign.PISCES to "Ultimate liberation potential; past-life spiritual mastery; complete detachment; moksha"
            )
        )
    )

    /**
     * Internal data class for planet-sign signification definitions.
     */
    private data class PlanetSignification(
        val domains: List<String>,
        val signEffects: Map<ZodiacSign, String>
    )

    // ========================================================================
    // MAIN ANALYSIS ENTRY POINT
    // ========================================================================

    /**
     * Performs a complete Bhrigu Nandi Nadi analysis on the given chart.
     *
     * The analysis:
     * 1. Builds a weighted graph of planetary connections
     * 2. Discovers all planetary chains via recursive traversal
     * 3. Generates BNN readings for each planet
     * 4. Produces life event predictions from significant chains
     * 5. Identifies consecutive sign groups
     *
     * @param chart The natal chart (Lagna is deliberately ignored per BNN rules)
     * @return Complete [BNNAnalysis] with all BNN interpretations
     */
    fun analyze(chart: VedicChart): BNNAnalysis {
        val mainPositions = chart.planetPositions.filter { it.planet in Planet.MAIN_PLANETS }

        // Step 1: Build the planetary graph
        val graph = buildPlanetaryGraph(mainPositions)

        // Step 2: Discover planetary chains
        val chains = discoverPlanetaryChains(graph, mainPositions)

        // Step 3: Generate per-planet BNN readings
        val planetReadings = generatePlanetReadings(mainPositions, graph)

        // Step 4: Produce life event predictions from chains
        val predictions = generateLifeEventPredictions(chains, mainPositions)

        // Step 5: Identify consecutive sign groups
        val consecutiveGroups = findConsecutiveSignGroups(mainPositions)

        // Step 6: Generate overall summary
        val summary = generateOverallSummary(planetReadings, chains, consecutiveGroups, predictions)

        return BNNAnalysis(
            planetaryGraph = graph,
            planetaryChains = chains,
            planetReadings = planetReadings,
            lifeEventPredictions = predictions,
            consecutiveSignPlanets = consecutiveGroups,
            overallSummary = summary
        )
    }

    // ========================================================================
    // GRAPH CONSTRUCTION
    // ========================================================================

    /**
     * Builds a weighted undirected graph where:
     * - Nodes are planets with their sign positions
     * - Edges are BNN aspect connections (trine, adjacent, opposition, conjunction)
     * - Edge weights are derived from aspect type and exact degree proximity
     */
    private fun buildPlanetaryGraph(positions: List<PlanetPosition>): PlanetaryGraph {
        val nodes = mutableMapOf<Planet, PlanetNode>()
        val edges = mutableListOf<PlanetaryEdge>()
        val adjacencyList = mutableMapOf<Planet, MutableList<PlanetaryEdge>>()

        // Build nodes
        for (pos in positions) {
            val coTenants = positions
                .filter { it.planet != pos.planet && it.sign == pos.sign }
                .map { it.planet }

            nodes[pos.planet] = PlanetNode(
                planet = pos.planet,
                sign = pos.sign,
                degree = pos.degree,
                isRetrograde = pos.isRetrograde,
                coTenants = coTenants
            )
            adjacencyList[pos.planet] = mutableListOf()
        }

        // Build edges between all planet pairs
        for (i in positions.indices) {
            for (j in i + 1 until positions.size) {
                val pos1 = positions[i]
                val pos2 = positions[j]

                val signDistance = getSignDistance(pos1.sign, pos2.sign)
                val aspectType = classifyBNNAspect(signDistance)

                if (aspectType != null) {
                    val strength = computeEdgeStrength(aspectType, pos1, pos2, signDistance)

                    val desc = "${pos1.planet.displayName} in ${pos1.sign.displayName} " +
                            "${aspectType.label} ${pos2.planet.displayName} in ${pos2.sign.displayName}"

                    val edge = PlanetaryEdge(
                        fromPlanet = pos1.planet,
                        toPlanet = pos2.planet,
                        aspectType = aspectType,
                        strength = strength,
                        signDistance = signDistance,
                        description = desc
                    )

                    val reverseEdge = edge.copy(
                        fromPlanet = pos2.planet,
                        toPlanet = pos1.planet
                    )

                    edges.add(edge)
                    edges.add(reverseEdge)
                    adjacencyList[pos1.planet]?.add(edge)
                    adjacencyList[pos2.planet]?.add(reverseEdge)
                }
            }
        }

        return PlanetaryGraph(
            nodes = nodes,
            edges = edges,
            adjacencyList = adjacencyList
        )
    }

    /**
     * Calculates the sign distance (1-12) from sign1 to sign2.
     * A distance of 1 means the same sign.
     */
    private fun getSignDistance(sign1: ZodiacSign, sign2: ZodiacSign): Int {
        val forward = ((sign2.number - sign1.number + 12) % 12) + 1
        return forward
    }

    /**
     * Classifies a sign distance into a BNN aspect type, or null if no
     * BNN connection exists at that distance.
     *
     * BNN aspects:
     * - Distance 1: Conjunction (same sign)
     * - Distance 5 or 9: Trine link
     * - Distance 2 or 12: Adjacent link
     * - Distance 7: Opposition link
     */
    private fun classifyBNNAspect(signDistance: Int): BNNAspectType? {
        return when (signDistance) {
            1 -> BNNAspectType.CONJUNCTION
            5, 9 -> BNNAspectType.TRINE
            2, 12 -> BNNAspectType.ADJACENT
            7 -> BNNAspectType.OPPOSITION
            else -> null
        }
    }

    /**
     * Computes the edge strength based on:
     * 1. Base strength of the aspect type
     * 2. Degree proximity (closer degrees within aspect = stronger)
     * 3. Retrograde modifier (retrograde planets slightly strengthen the link
     *    in BNN as they are considered to give more focused results)
     */
    private fun computeEdgeStrength(
        aspectType: BNNAspectType,
        pos1: PlanetPosition,
        pos2: PlanetPosition,
        signDistance: Int
    ): Double {
        var strength = aspectType.baseStrength

        // Degree-based refinement: closer degrees within the aspect increase strength
        val degreeDiff = abs(pos1.degree - pos2.degree)
        val degreeModifier = when {
            degreeDiff <= 1.0 -> 0.10   // Near-exact aspect
            degreeDiff <= 3.0 -> 0.07
            degreeDiff <= 5.0 -> 0.04
            degreeDiff <= 10.0 -> 0.0
            degreeDiff <= 20.0 -> -0.03
            else -> -0.05
        }
        strength += degreeModifier

        // Retrograde planets in BNN are considered more focused
        if (pos1.isRetrograde && pos1.planet != Planet.RAHU && pos1.planet != Planet.KETU) {
            strength += 0.03
        }
        if (pos2.isRetrograde && pos2.planet != Planet.RAHU && pos2.planet != Planet.KETU) {
            strength += 0.03
        }

        return strength.coerceIn(0.10, 1.0)
    }

    // ========================================================================
    // PLANETARY CHAIN DISCOVERY
    // ========================================================================

    /**
     * Discovers all meaningful planetary chains through recursive depth-first
     * traversal of the planetary graph.
     *
     * A chain is a sequence of connected planets where each adjacent pair in the
     * sequence shares a BNN aspect link. Chains of length 2+ are retained.
     * Longer chains indicate stronger combined effects.
     *
     * The algorithm prunes chains that:
     * - Revisit the same planet (acyclic paths only)
     * - Have average strength below a minimum threshold
     *
     * Results are sorted by average strength descending.
     */
    private fun discoverPlanetaryChains(
        graph: PlanetaryGraph,
        positions: List<PlanetPosition>
    ): List<PlanetaryChain> {
        val allChains = mutableSetOf<List<Planet>>()

        // Start DFS from each planet
        for (planet in graph.nodes.keys) {
            val visited = mutableSetOf<Planet>()
            val currentPath = mutableListOf<Planet>()
            dfsCollectChains(graph, planet, visited, currentPath, allChains)
        }

        // Convert raw chains to interpreted PlanetaryChain objects
        val posMap = positions.associateBy { it.planet }
        val interpretedChains = allChains
            .filter { it.size >= 2 }
            .distinctBy { it.sorted() } // Deduplicate regardless of direction
            .mapNotNull { chain ->
                val signs = chain.mapNotNull { posMap[it]?.sign }
                if (signs.size != chain.size) return@mapNotNull null

                // Compute average strength across the chain
                var totalStrength = 0.0
                var edgeCount = 0
                for (i in 0 until chain.size - 1) {
                    val edge = graph.adjacencyList[chain[i]]
                        ?.find { it.toPlanet == chain[i + 1] }
                    if (edge != null) {
                        totalStrength += edge.strength
                        edgeCount++
                    }
                }
                val avgStrength = if (edgeCount > 0) totalStrength / edgeCount else 0.0

                if (avgStrength < 0.30) return@mapNotNull null

                val activatedDomains = chain.flatMap { planet ->
                    BNN_PLANET_SIGNIFICATIONS[planet]?.domains ?: emptyList()
                }.distinct()

                val interpretation = generateChainInterpretation(chain, signs, avgStrength)

                PlanetaryChain(
                    planets = chain,
                    signs = signs,
                    averageStrength = avgStrength,
                    length = chain.size,
                    interpretation = interpretation,
                    activatedDomains = activatedDomains
                )
            }
            .sortedByDescending { it.averageStrength }

        return interpretedChains
    }

    /**
     * Recursive DFS to collect all acyclic paths from a starting planet.
     * Limits chain length to 5 to prevent combinatorial explosion while
     * still capturing the most meaningful multi-planet chains.
     */
    private fun dfsCollectChains(
        graph: PlanetaryGraph,
        current: Planet,
        visited: MutableSet<Planet>,
        currentPath: MutableList<Planet>,
        allChains: MutableSet<List<Planet>>
    ) {
        visited.add(current)
        currentPath.add(current)

        // Record current path as a chain if length >= 2
        if (currentPath.size >= 2) {
            allChains.add(currentPath.toList())
        }

        // Limit maximum chain depth to 5 planets
        if (currentPath.size < 5) {
            val neighbors = graph.adjacencyList[current] ?: emptyList()
            for (edge in neighbors) {
                if (edge.toPlanet !in visited && edge.strength >= 0.30) {
                    dfsCollectChains(graph, edge.toPlanet, visited, currentPath, allChains)
                }
            }
        }

        visited.remove(current)
        currentPath.removeAt(currentPath.lastIndex)
    }

    /**
     * Generates a BNN interpretation for a planetary chain by combining the
     * significations of all planets in the chain and their sign positions.
     */
    private fun generateChainInterpretation(
        planets: List<Planet>,
        signs: List<ZodiacSign>,
        strength: Double
    ): String {
        val planetNames = planets.map { it.displayName }
        val signNames = signs.map { it.displayName }

        val domains = planets.flatMap {
            BNN_PLANET_SIGNIFICATIONS[it]?.domains?.take(2) ?: emptyList()
        }.distinct().take(5)

        val strengthLabel = when {
            strength >= 0.85 -> "very strong"
            strength >= 0.70 -> "strong"
            strength >= 0.50 -> "moderate"
            else -> "mild"
        }

        return buildString {
            append("BNN Chain: ${planetNames.joinToString(" - ")} ")
            append("(Signs: ${signNames.joinToString(" - ")}). ")
            append("This $strengthLabel chain (strength ${String.format("%.2f", strength)}) ")
            append("activates the domains of ${domains.joinToString(", ")}. ")
            append(generateSpecificChainReading(planets, signs))
        }
    }

    /**
     * Generates specific BNN readings for known planetary combinations based
     * on classical Nadi text principles.
     */
    private fun generateSpecificChainReading(planets: List<Planet>, signs: List<ZodiacSign>): String {
        val planetSet = planets.toSet()

        return when {
            // Jupiter-Saturn chain: Dharma-Karma connection (most important in BNN)
            Planet.JUPITER in planetSet && Planet.SATURN in planetSet ->
                "Jupiter-Saturn connection creates a powerful Dharma-Karma axis. " +
                        "The native's profession aligns with their spiritual or educational pursuits. " +
                        "Career success comes through righteous conduct and wisdom."

            // Jupiter-Venus chain: Marriage and children/prosperity
            Planet.JUPITER in planetSet && Planet.VENUS in planetSet ->
                "Jupiter-Venus connection strongly activates marriage and prosperity. " +
                        "Indicates a spiritually inclined spouse, financial comfort through wisdom, " +
                        "and children who bring honor."

            // Saturn-Mars chain: Karma and energy/property
            Planet.SATURN in planetSet && Planet.MARS in planetSet ->
                "Saturn-Mars connection links profession with property and technical skills. " +
                        "Indicates career in engineering, construction, real estate, or defense. " +
                        "Hard work leads to property accumulation."

            // Sun-Jupiter chain: Father and dharma
            Planet.SUN in planetSet && Planet.JUPITER in planetSet ->
                "Sun-Jupiter connection elevates the father's status and strengthens the native's " +
                        "dharmic inclination. Indicates government connection through education or spiritual merit."

            // Moon-Mercury chain: Mind and intelligence
            Planet.MOON in planetSet && Planet.MERCURY in planetSet ->
                "Moon-Mercury connection creates a sharp, articulate mind. " +
                        "Excellent for communication, writing, media, and commerce. " +
                        "Emotional intelligence supports intellectual pursuits."

            // Venus-Mercury chain: Arts and commerce
            Planet.VENUS in planetSet && Planet.MERCURY in planetSet ->
                "Venus-Mercury connection favors artistic communication, luxury commerce, " +
                        "and refined social connections. Indicates success in media, fashion, or creative business."

            // Rahu-Jupiter chain: Foreign dharma or unconventional education
            Planet.RAHU in planetSet && Planet.JUPITER in planetSet ->
                "Rahu-Jupiter connection indicates dharma through unconventional means. " +
                        "Foreign education, non-traditional spiritual path, or technology-enhanced learning."

            // Ketu-Moon chain: Spiritual mind, past-life emotional patterns
            Planet.KETU in planetSet && Planet.MOON in planetSet ->
                "Ketu-Moon connection creates deep intuition and psychic sensitivity. " +
                        "Past-life emotional patterns strongly influence the current mindset. " +
                        "Indicates a mind inclined toward meditation and renunciation."

            // Rahu-Venus chain: Unconventional marriage or foreign spouse
            Planet.RAHU in planetSet && Planet.VENUS in planetSet ->
                "Rahu-Venus connection suggests an unconventional marriage or foreign spouse. " +
                        "Obsessive pursuit of luxury, arts, or beauty. Relationships may involve " +
                        "cross-cultural or non-traditional dynamics."

            // Sun-Saturn chain: Authority-karma tension
            Planet.SUN in planetSet && Planet.SATURN in planetSet ->
                "Sun-Saturn connection creates tension between authority and discipline. " +
                        "Father may face career challenges. Professional life involves government " +
                        "service with heavy responsibilities and possible delays in recognition."

            // Mars-Ketu chain: Past-life warrior, spiritual courage
            Planet.MARS in planetSet && Planet.KETU in planetSet ->
                "Mars-Ketu connection indicates past-life martial karma. " +
                        "Strong courage and decisive action with spiritual undertones. " +
                        "May indicate surgery, accidents resolved through spiritual merit, or martial arts."

            // Three or more planet chains
            planetSet.size >= 3 ->
                "This multi-planet chain creates a complex web of interconnected life domains. " +
                        "The combined significations of ${planets.joinToString(", ") { it.displayName }} " +
                        "suggest that events in one domain trigger cascading effects across all connected areas."

            else ->
                "The planets in this chain mutually influence each other's significations, " +
                        "creating a blended outcome across their respective life domains."
        }
    }

    // ========================================================================
    // PER-PLANET BNN READINGS
    // ========================================================================

    /**
     * Generates BNN readings for each planet by combining its sign-based
     * significations with the effects of all linked planets.
     */
    private fun generatePlanetReadings(
        positions: List<PlanetPosition>,
        graph: PlanetaryGraph
    ): Map<Planet, BNNPlanetReading> {
        val readings = mutableMapOf<Planet, BNNPlanetReading>()

        for (pos in positions) {
            if (pos.planet !in Planet.MAIN_PLANETS) continue

            val signification = BNN_PLANET_SIGNIFICATIONS[pos.planet] ?: continue
            val coreReading = signification.signEffects[pos.sign]
                ?: "General effects of ${pos.planet.displayName} in ${pos.sign.displayName}"

            val edges = graph.adjacencyList[pos.planet] ?: emptyList()
            val linkedEffects = edges.map { edge ->
                val linkedPlanetSig = BNN_PLANET_SIGNIFICATIONS[edge.toPlanet]
                val linkedNode = graph.nodes[edge.toPlanet]
                val effectText = generateLinkedEffect(pos.planet, edge.toPlanet, edge.aspectType, linkedNode?.sign)

                LinkedEffect(
                    linkedPlanet = edge.toPlanet,
                    aspectType = edge.aspectType,
                    strength = edge.strength,
                    effect = effectText
                )
            }

            val linkedPlanets = edges.map { it.toPlanet }
            val strengthRating = computeReadingStrength(pos, linkedEffects)

            val overallReading = buildString {
                append("${pos.planet.displayName} in ${pos.sign.displayName}: $coreReading. ")
                if (linkedEffects.isNotEmpty()) {
                    append("Connected to ${linkedEffects.size} planet(s): ")
                    val significantLinks = linkedEffects
                        .sortedByDescending { it.strength }
                        .take(3)
                    for (le in significantLinks) {
                        append("${le.linkedPlanet.displayName} (${le.aspectType.label}, " +
                                "strength ${String.format("%.2f", le.strength)}): ${le.effect} ")
                    }
                } else {
                    append("No direct BNN links found; this planet operates independently in its sign domain.")
                }
            }

            readings[pos.planet] = BNNPlanetReading(
                planet = pos.planet,
                sign = pos.sign,
                degree = pos.degree,
                coreSignification = coreReading,
                linkedEffects = linkedEffects,
                overallReading = overallReading,
                strengthRating = strengthRating,
                linkedPlanets = linkedPlanets
            )
        }

        return readings
    }

    /**
     * Generates the effect text for a linked planet's influence on the source planet.
     */
    private fun generateLinkedEffect(
        sourcePlanet: Planet,
        linkedPlanet: Planet,
        aspectType: BNNAspectType,
        linkedSign: ZodiacSign?
    ): String {
        val sourceDomains = BNN_PLANET_SIGNIFICATIONS[sourcePlanet]?.domains?.take(2)?.joinToString("/")
            ?: "general"
        val linkedDomains = BNN_PLANET_SIGNIFICATIONS[linkedPlanet]?.domains?.take(2)?.joinToString("/")
            ?: "general"

        val connectionQuality = when (aspectType) {
            BNNAspectType.CONJUNCTION -> "directly merges"
            BNNAspectType.TRINE -> "harmoniously supports"
            BNNAspectType.ADJACENT -> "closely influences"
            BNNAspectType.OPPOSITION -> "creates dynamic tension with"
        }

        val signContext = linkedSign?.let { " from ${it.displayName}" } ?: ""

        return "${linkedPlanet.displayName}$signContext $connectionQuality " +
                "${sourcePlanet.displayName}'s ${sourceDomains} matters through its ${linkedDomains} significations."
    }

    /**
     * Computes a 1-5 strength rating for a planet reading based on its
     * dignity, number of links, and average link strength.
     */
    private fun computeReadingStrength(pos: PlanetPosition, effects: List<LinkedEffect>): Int {
        val dignity = VedicAstrologyUtils.getDignity(pos)
        val dignityScore = when (dignity) {
            VedicAstrologyUtils.PlanetaryDignity.EXALTED -> 2.0
            VedicAstrologyUtils.PlanetaryDignity.MOOLATRIKONA -> 1.5
            VedicAstrologyUtils.PlanetaryDignity.OWN_SIGN -> 1.5
            VedicAstrologyUtils.PlanetaryDignity.FRIEND_SIGN -> 1.0
            VedicAstrologyUtils.PlanetaryDignity.NEUTRAL_SIGN -> 0.5
            VedicAstrologyUtils.PlanetaryDignity.ENEMY_SIGN -> 0.0
            VedicAstrologyUtils.PlanetaryDignity.DEBILITATED -> -0.5
        }

        val linkScore = min(effects.size.toDouble(), 4.0) * 0.5
        val avgStrength = if (effects.isNotEmpty()) {
            effects.map { it.strength }.average()
        } else 0.0

        val raw = dignityScore + linkScore + avgStrength
        return raw.toInt().coerceIn(1, 5)
    }

    // ========================================================================
    // LIFE EVENT PREDICTIONS
    // ========================================================================

    /**
     * Generates specific life event predictions from the most significant
     * planetary chains, applying classical BNN rules.
     */
    private fun generateLifeEventPredictions(
        chains: List<PlanetaryChain>,
        positions: List<PlanetPosition>
    ): List<BNNPrediction> {
        val predictions = mutableListOf<BNNPrediction>()
        val posMap = positions.associateBy { it.planet }

        // Evaluate known classical BNN combinations
        predictions.addAll(evaluateJupiterSaturnAxis(posMap))
        predictions.addAll(evaluateMarriageIndicators(posMap))
        predictions.addAll(evaluateCareerIndicators(posMap))
        predictions.addAll(evaluateSpiritualIndicators(posMap))
        predictions.addAll(evaluateForeignIndicators(posMap))
        predictions.addAll(evaluateHealthIndicators(posMap))

        // Generate predictions from strong multi-planet chains
        for (chain in chains.filter { it.length >= 3 && it.averageStrength >= 0.60 }) {
            val domain = chain.activatedDomains.firstOrNull() ?: "General"
            predictions.add(
                BNNPrediction(
                    involvedPlanets = chain.planets,
                    domain = domain,
                    prediction = chain.interpretation,
                    confidence = if (chain.averageStrength >= 0.80)
                        PredictionConfidence.HIGH else PredictionConfidence.MEDIUM,
                    classicalReference = "BNN multi-planet chain analysis"
                )
            )
        }

        return predictions.distinctBy { it.prediction }.sortedByDescending {
            when (it.confidence) {
                PredictionConfidence.HIGH -> 3
                PredictionConfidence.MEDIUM -> 2
                PredictionConfidence.LOW -> 1
            }
        }
    }

    private fun evaluateJupiterSaturnAxis(posMap: Map<Planet, PlanetPosition>): List<BNNPrediction> {
        val jupiter = posMap[Planet.JUPITER] ?: return emptyList()
        val saturn = posMap[Planet.SATURN] ?: return emptyList()
        val results = mutableListOf<BNNPrediction>()
        val dist = getSignDistance(jupiter.sign, saturn.sign)

        if (dist in listOf(1, 5, 9)) {
            results.add(
                BNNPrediction(
                    involvedPlanets = listOf(Planet.JUPITER, Planet.SATURN),
                    domain = "Career & Dharma",
                    prediction = "Jupiter-Saturn trine connection: Profession aligns with dharma. " +
                            "Career success through education, teaching, law, or spiritual pursuits. " +
                            "Saturn in ${saturn.sign.displayName} directs career toward " +
                            "${BNN_PLANET_SIGNIFICATIONS[Planet.SATURN]?.signEffects?.get(saturn.sign) ?: "structured fields"}.",
                    confidence = PredictionConfidence.HIGH,
                    classicalReference = "BNN Dharma-Karma Yoga: Jupiter and Saturn in mutual trines"
                )
            )
        }

        if (dist == 7) {
            results.add(
                BNNPrediction(
                    involvedPlanets = listOf(Planet.JUPITER, Planet.SATURN),
                    domain = "Career & Dharma",
                    prediction = "Jupiter-Saturn opposition: Tension between dharmic aspirations and karmic duties. " +
                            "Career may oscillate between spiritual/educational pursuits and practical obligations. " +
                            "Resolution comes through balancing wisdom with discipline.",
                    confidence = PredictionConfidence.HIGH,
                    classicalReference = "BNN Dharma-Karma opposition: Jupiter 7th from Saturn"
                )
            )
        }

        return results
    }

    private fun evaluateMarriageIndicators(posMap: Map<Planet, PlanetPosition>): List<BNNPrediction> {
        val venus = posMap[Planet.VENUS] ?: return emptyList()
        val results = mutableListOf<BNNPrediction>()

        // Venus-Jupiter link (classical marriage indicator in BNN)
        val jupiter = posMap[Planet.JUPITER]
        if (jupiter != null) {
            val dist = getSignDistance(venus.sign, jupiter.sign)
            if (dist in listOf(1, 5, 9)) {
                results.add(
                    BNNPrediction(
                        involvedPlanets = listOf(Planet.VENUS, Planet.JUPITER),
                        domain = "Marriage",
                        prediction = "Venus-Jupiter trine: Auspicious marriage. Spouse brings prosperity and wisdom. " +
                                "Marriage ceremony likely to be traditional and dharmic. " +
                                "Venus in ${venus.sign.displayName} colors the marital experience with " +
                                "${BNN_PLANET_SIGNIFICATIONS[Planet.VENUS]?.signEffects?.get(venus.sign) ?: "general Venus themes"}.",
                        confidence = PredictionConfidence.HIGH,
                        classicalReference = "BNN Marriage Yoga: Venus-Jupiter trine connection"
                    )
                )
            }
        }

        // Venus-Rahu link (unconventional marriage)
        val rahu = posMap[Planet.RAHU]
        if (rahu != null) {
            val dist = getSignDistance(venus.sign, rahu.sign)
            if (dist in listOf(1, 2, 12)) {
                results.add(
                    BNNPrediction(
                        involvedPlanets = listOf(Planet.VENUS, Planet.RAHU),
                        domain = "Marriage",
                        prediction = "Venus-Rahu conjunction/adjacent: Marriage involves unconventional elements. " +
                                "Possible inter-caste, inter-cultural, or foreign spouse. " +
                                "Obsessive romantic attraction followed by karmic lessons in relationships.",
                        confidence = PredictionConfidence.MEDIUM,
                        classicalReference = "BNN Rahu-Venus conjunction: unconventional marriage"
                    )
                )
            }
        }

        // Venus-Saturn link (delayed or mature marriage)
        val saturn = posMap[Planet.SATURN]
        if (saturn != null) {
            val dist = getSignDistance(venus.sign, saturn.sign)
            if (dist in listOf(1, 5, 9)) {
                results.add(
                    BNNPrediction(
                        involvedPlanets = listOf(Planet.VENUS, Planet.SATURN),
                        domain = "Marriage",
                        prediction = "Venus-Saturn connection: Marriage may be delayed but stable once established. " +
                                "Spouse is responsible, mature, and hardworking. " +
                                "Practical approach to romantic relationships.",
                        confidence = PredictionConfidence.MEDIUM,
                        classicalReference = "BNN Venus-Saturn: delayed but stable marriage"
                    )
                )
            }
        }

        return results
    }

    private fun evaluateCareerIndicators(posMap: Map<Planet, PlanetPosition>): List<BNNPrediction> {
        val saturn = posMap[Planet.SATURN] ?: return emptyList()
        val results = mutableListOf<BNNPrediction>()

        // Saturn-Sun link (government career)
        val sun = posMap[Planet.SUN]
        if (sun != null) {
            val dist = getSignDistance(saturn.sign, sun.sign)
            if (dist in listOf(1, 5, 9, 2, 12)) {
                results.add(
                    BNNPrediction(
                        involvedPlanets = listOf(Planet.SATURN, Planet.SUN),
                        domain = "Career",
                        prediction = "Saturn-Sun connection: Career involves government, authority, or administrative roles. " +
                                "Father may influence career direction. Professional rise through disciplined " +
                                "application of authority and structured leadership.",
                        confidence = if (dist in listOf(1, 5, 9)) PredictionConfidence.HIGH
                        else PredictionConfidence.MEDIUM,
                        classicalReference = "BNN Saturn-Sun: governmental/authority career"
                    )
                )
            }
        }

        // Saturn-Mercury link (intellectual/commercial career)
        val mercury = posMap[Planet.MERCURY]
        if (mercury != null) {
            val dist = getSignDistance(saturn.sign, mercury.sign)
            if (dist in listOf(1, 5, 9)) {
                results.add(
                    BNNPrediction(
                        involvedPlanets = listOf(Planet.SATURN, Planet.MERCURY),
                        domain = "Career",
                        prediction = "Saturn-Mercury trine: Career in communication, commerce, technology, or accounting. " +
                                "Disciplined application of intelligence leads to professional success. " +
                                "Possible career in writing, IT, auditing, or analytical fields.",
                        confidence = PredictionConfidence.HIGH,
                        classicalReference = "BNN Saturn-Mercury trine: intellectual/commercial career"
                    )
                )
            }
        }

        // Saturn-Mars link (technical/engineering career)
        val mars = posMap[Planet.MARS]
        if (mars != null) {
            val dist = getSignDistance(saturn.sign, mars.sign)
            if (dist in listOf(1, 5, 9)) {
                results.add(
                    BNNPrediction(
                        involvedPlanets = listOf(Planet.SATURN, Planet.MARS),
                        domain = "Career",
                        prediction = "Saturn-Mars trine: Career in engineering, construction, defense, surgery, or manufacturing. " +
                                "Combines discipline with energy for demanding technical professions. " +
                                "Property-related career also indicated.",
                        confidence = PredictionConfidence.HIGH,
                        classicalReference = "BNN Saturn-Mars trine: technical/engineering career"
                    )
                )
            }
        }

        return results
    }

    private fun evaluateSpiritualIndicators(posMap: Map<Planet, PlanetPosition>): List<BNNPrediction> {
        val ketu = posMap[Planet.KETU] ?: return emptyList()
        val results = mutableListOf<BNNPrediction>()

        // Ketu-Jupiter link (strong spiritual inclination)
        val jupiter = posMap[Planet.JUPITER]
        if (jupiter != null) {
            val dist = getSignDistance(ketu.sign, jupiter.sign)
            if (dist in listOf(1, 5, 9)) {
                results.add(
                    BNNPrediction(
                        involvedPlanets = listOf(Planet.KETU, Planet.JUPITER),
                        domain = "Spirituality",
                        prediction = "Ketu-Jupiter trine: Powerful moksha combination. Past-life spiritual merit activates " +
                                "in this life through dharmic pursuits. Strong inclination toward meditation, " +
                                "astrology, yoga, or renunciation. Natural guru qualities emerge.",
                        confidence = PredictionConfidence.HIGH,
                        classicalReference = "BNN Ketu-Jupiter: Moksha Yoga in Nadi astrology"
                    )
                )
            }
        }

        // Ketu-Moon link (spiritual mind)
        val moon = posMap[Planet.MOON]
        if (moon != null) {
            val dist = getSignDistance(ketu.sign, moon.sign)
            if (dist in listOf(1, 5, 9)) {
                results.add(
                    BNNPrediction(
                        involvedPlanets = listOf(Planet.KETU, Planet.MOON),
                        domain = "Spirituality & Mental Health",
                        prediction = "Ketu-Moon trine: Deeply intuitive and psychically sensitive mind. " +
                                "Past-life emotional patterns manifest as spiritual seekings. " +
                                "May experience periods of detachment or disillusionment that ultimately " +
                                "lead to spiritual awakening.",
                        confidence = PredictionConfidence.MEDIUM,
                        classicalReference = "BNN Ketu-Moon: spiritual/psychic mind"
                    )
                )
            }
        }

        return results
    }

    private fun evaluateForeignIndicators(posMap: Map<Planet, PlanetPosition>): List<BNNPrediction> {
        val rahu = posMap[Planet.RAHU] ?: return emptyList()
        val results = mutableListOf<BNNPrediction>()

        // Rahu-Saturn link (foreign career)
        val saturn = posMap[Planet.SATURN]
        if (saturn != null) {
            val dist = getSignDistance(rahu.sign, saturn.sign)
            if (dist in listOf(1, 5, 9, 2, 12)) {
                results.add(
                    BNNPrediction(
                        involvedPlanets = listOf(Planet.RAHU, Planet.SATURN),
                        domain = "Foreign & Career",
                        prediction = "Rahu-Saturn connection: Strong indication of career in foreign land. " +
                                "Profession involves international connections, technology, or multinational organizations. " +
                                "Foreign settlement possible if supported by other factors.",
                        confidence = if (dist in listOf(1, 5, 9)) PredictionConfidence.HIGH
                        else PredictionConfidence.MEDIUM,
                        classicalReference = "BNN Rahu-Saturn: foreign career/settlement"
                    )
                )
            }
        }

        // Rahu-Moon link (foreign residence/public image abroad)
        val moon = posMap[Planet.MOON]
        if (moon != null) {
            val dist = getSignDistance(rahu.sign, moon.sign)
            if (dist in listOf(1, 2, 12)) {
                results.add(
                    BNNPrediction(
                        involvedPlanets = listOf(Planet.RAHU, Planet.MOON),
                        domain = "Foreign & Mind",
                        prediction = "Rahu-Moon proximity: Mind drawn toward foreign cultures and unconventional paths. " +
                                "Public image may involve foreign connections. Emotional fulfillment sought " +
                                "through exploration beyond familiar boundaries.",
                        confidence = PredictionConfidence.MEDIUM,
                        classicalReference = "BNN Rahu-Moon: foreign mind and residence"
                    )
                )
            }
        }

        return results
    }

    private fun evaluateHealthIndicators(posMap: Map<Planet, PlanetPosition>): List<BNNPrediction> {
        val results = mutableListOf<BNNPrediction>()

        // Mars-Saturn conjunction/opposition (surgery, accidents)
        val mars = posMap[Planet.MARS]
        val saturn = posMap[Planet.SATURN]
        if (mars != null && saturn != null) {
            val dist = getSignDistance(mars.sign, saturn.sign)
            if (dist == 1 || dist == 7) {
                results.add(
                    BNNPrediction(
                        involvedPlanets = listOf(Planet.MARS, Planet.SATURN),
                        domain = "Health",
                        prediction = "Mars-Saturn ${if (dist == 1) "conjunction" else "opposition"}: " +
                                "Indicates vulnerability to surgical procedures, bone/joint issues, or accident-related injuries. " +
                                "Courage and discipline in health management are essential. " +
                                "Chronic conditions related to inflammation or structural issues.",
                        confidence = PredictionConfidence.HIGH,
                        classicalReference = "BNN Mars-Saturn: surgery/health vulnerability"
                    )
                )
            }
        }

        // Sun-Saturn conjunction (vitality issues)
        val sun = posMap[Planet.SUN]
        if (sun != null && saturn != null) {
            val dist = getSignDistance(sun.sign, saturn.sign)
            if (dist == 1) {
                results.add(
                    BNNPrediction(
                        involvedPlanets = listOf(Planet.SUN, Planet.SATURN),
                        domain = "Health & Authority",
                        prediction = "Sun-Saturn conjunction: Vitality may be under pressure. " +
                                "Bone health, cardiac issues, or constitutional weakness requires attention. " +
                                "Father's health may also be a concern.",
                        confidence = PredictionConfidence.MEDIUM,
                        classicalReference = "BNN Sun-Saturn conjunction: vitality pressure"
                    )
                )
            }
        }

        return results
    }

    // ========================================================================
    // CONSECUTIVE SIGN GROUPS
    // ========================================================================

    /**
     * Identifies groups of planets occupying consecutive zodiac signs.
     * In BNN, planets in consecutive signs exert amplified mutual influence
     * regardless of whether they share a formal trine/opposition link.
     */
    private fun findConsecutiveSignGroups(
        positions: List<PlanetPosition>
    ): List<ConsecutiveSignGroup> {
        // Map planets to their sign numbers
        val signToPlanets = positions
            .filter { it.planet in Planet.MAIN_PLANETS }
            .groupBy { it.sign.number }

        val groups = mutableListOf<ConsecutiveSignGroup>()
        val processedSigns = mutableSetOf<Int>()

        for (signNum in 1..12) {
            if (signNum in processedSigns) continue
            if (signToPlanets[signNum].isNullOrEmpty()) continue

            // Try to extend a consecutive group starting from this sign
            val consecutiveSigns = mutableListOf(signNum)
            var nextSign = (signNum % 12) + 1

            while (signToPlanets[nextSign]?.isNotEmpty() == true && nextSign !in processedSigns) {
                consecutiveSigns.add(nextSign)
                processedSigns.add(nextSign)
                nextSign = (nextSign % 12) + 1
            }

            if (consecutiveSigns.size >= 2) {
                processedSigns.addAll(consecutiveSigns)

                val planets = consecutiveSigns.flatMap { sn ->
                    signToPlanets[sn]?.map { it.planet } ?: emptyList()
                }
                val signs = consecutiveSigns.map { sn ->
                    ZodiacSign.entries.find { it.number == sn } ?: ZodiacSign.ARIES
                }

                // Strength multiplier increases with group size
                val multiplier = 1.0 + (consecutiveSigns.size - 1) * 0.20

                val interpretation = buildString {
                    append("Consecutive sign group: ${planets.joinToString(", ") { it.displayName }} ")
                    append("in signs ${signs.joinToString(", ") { it.displayName }}. ")
                    append("This cluster of ${planets.size} planet(s) across ${consecutiveSigns.size} ")
                    append("consecutive signs creates a concentrated zone of planetary energy. ")
                    append("The significations of ")
                    append(planets.flatMap {
                        BNN_PLANET_SIGNIFICATIONS[it]?.domains?.take(1) ?: emptyList()
                    }.distinct().joinToString(", "))
                    append(" are strongly interlinked and mutually amplified (multiplier: ${String.format("%.2f", multiplier)}x).")
                }

                groups.add(
                    ConsecutiveSignGroup(
                        planets = planets,
                        signs = signs,
                        interpretation = interpretation,
                        strengthMultiplier = multiplier
                    )
                )
            }
        }

        // Also check wrap-around (Pisces -> Aries)
        val piscesPlanes = signToPlanets[12]
        val ariesPlanets = signToPlanets[1]
        if (!piscesPlanes.isNullOrEmpty() && !ariesPlanets.isNullOrEmpty() &&
            12 !in processedSigns && 1 !in processedSigns
        ) {
            val planets = piscesPlanes.map { it.planet } + ariesPlanets.map { it.planet }
            val signs = listOf(ZodiacSign.PISCES, ZodiacSign.ARIES)
            val multiplier = 1.20

            groups.add(
                ConsecutiveSignGroup(
                    planets = planets,
                    signs = signs,
                    interpretation = "Wrap-around consecutive group: ${planets.joinToString(", ") { it.displayName }} " +
                            "span the Pisces-Aries cusp, the zodiac's beginning/ending point. " +
                            "This creates a powerful karmic junction where past-life themes (Pisces) " +
                            "merge with new beginnings (Aries).",
                    strengthMultiplier = multiplier
                )
            )
        }

        return groups
    }

    // ========================================================================
    // OVERALL SUMMARY
    // ========================================================================

    /**
     * Generates an overall BNN reading summary synthesizing all findings.
     */
    private fun generateOverallSummary(
        readings: Map<Planet, BNNPlanetReading>,
        chains: List<PlanetaryChain>,
        consecutiveGroups: List<ConsecutiveSignGroup>,
        predictions: List<BNNPrediction>
    ): String {
        val strongReadings = readings.values.filter { it.strengthRating >= 4 }
        val strongChains = chains.filter { it.averageStrength >= 0.70 }
        val highConfPredictions = predictions.filter { it.confidence == PredictionConfidence.HIGH }

        return buildString {
            append("BHRIGU NANDI NADI ANALYSIS SUMMARY\n\n")

            append("Total planetary connections: ${chains.size}\n")
            append("Strong chains (0.70+): ${strongChains.size}\n")
            append("High-confidence predictions: ${highConfPredictions.size}\n")
            append("Consecutive sign groups: ${consecutiveGroups.size}\n\n")

            if (strongReadings.isNotEmpty()) {
                append("STRONGEST PLANETS: ")
                append(strongReadings.joinToString(", ") {
                    "${it.planet.displayName} in ${it.sign.displayName} (rating: ${it.strengthRating}/5)"
                })
                append("\n\n")
            }

            if (highConfPredictions.isNotEmpty()) {
                append("KEY PREDICTIONS:\n")
                for (pred in highConfPredictions.take(5)) {
                    append("- [${pred.domain}] ${pred.prediction.take(120)}...\n")
                }
                append("\n")
            }

            if (consecutiveGroups.isNotEmpty()) {
                append("ENERGY CLUSTERS:\n")
                for (group in consecutiveGroups) {
                    append("- ${group.planets.joinToString(", ") { it.displayName }} " +
                            "in ${group.signs.joinToString("-") { it.displayName }} " +
                            "(multiplier: ${String.format("%.2f", group.strengthMultiplier)}x)\n")
                }
                append("\n")
            }

            append("Note: BNN analysis is independent of the Ascendant (Lagna). " +
                    "It reads planetary sign positions and their mutual connections to reveal " +
                    "karmic patterns and life themes.")
        }
    }
}
