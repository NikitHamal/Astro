package com.astro.storm.ephemeris.jaimini

import com.astro.storm.core.common.Language
import com.astro.storm.core.common.StringKeyInterface
import com.astro.storm.core.model.Planet
import com.astro.storm.core.model.PlanetPosition
import com.astro.storm.core.model.VedicChart
import com.astro.storm.core.model.ZodiacSign
import com.astro.storm.ephemeris.DivisionalChartCalculator
import com.astro.storm.ephemeris.DivisionalChartType
import com.astro.storm.ephemeris.VedicAstrologyUtils

/**
 * JaiminiKarakaCalculator - Comprehensive Chara Karaka Analysis System
 *
 * This calculator implements the complete Jaimini Chara Karaka system as described
 * in the Jaimini Sutras. Chara Karakas (variable significators) are determined by
 * the degrees traversed by planets in their respective signs.
 *
 * The 7 Chara Karakas (excluding Rahu/Ketu in 7-karaka system):
 * 1. Atmakaraka (AK) - Soul significator, highest degree planet
 * 2. Amatyakaraka (AmK) - Minister/Career significator
 * 3. Bhratrikaraka (BK) - Sibling significator
 * 4. Matrikaraka (MK) - Mother significator
 * 5. Pitrikaraka (PK) - Father significator (some use Putrakaraka)
 * 6. Gnatikaraka (GK) - Relatives/Cousins significator
 * 7. Darakaraka (DK) - Spouse significator, lowest degree planet
 *
 * The 8-karaka system includes Rahu (counted backwards from 30°).
 *
 * Key Concepts:
 * - Karakamsha: The Navamsa sign occupied by Atmakaraka
 * - Swamsha: The Lagna in Navamsa chart
 * - Karakamsha Lagna: A special chart with Karakamsha as ascendant
 *
 * Vedic References:
 * - Jaimini Sutras (primary source)
 * - Jaimini Upadesa Sutras with commentaries
 * - BPHS (Brihat Parashara Hora Shastra) - Karaka chapter
 *
 * @author AstroStorm
 * @since 2026.02
 */
object JaiminiKarakaCalculator {

    /**
     * Planets eligible for Chara Karaka assignment (7-karaka system)
     */
    private val KARAKA_PLANETS_7 = listOf(
        Planet.SUN, Planet.MOON, Planet.MARS, Planet.MERCURY,
        Planet.JUPITER, Planet.VENUS, Planet.SATURN
    )

    /**
     * Planets eligible for Chara Karaka assignment (8-karaka system including Rahu)
     */
    private val KARAKA_PLANETS_8 = KARAKA_PLANETS_7 + Planet.RAHU

    /**
     * Karaka type enumeration with significations per Jaimini Sutras
     */
    enum class KarakaType(
        val displayName: String,
        val sanskritName: String,
        val abbreviation: String,
        val rank: Int,
        val primarySignification: String,
        val houseSignification: Int,
        val secondarySignifications: List<String>
    ) {
        ATMAKARAKA(
            "Soul Significator",
            "Atmakaraka",
            "AK",
            1,
            "Self, Soul, Overall Life Direction",
            1,
            listOf("Ego", "Body", "Personality", "Life purpose", "Spiritual evolution")
        ),
        AMATYAKARAKA(
            "Minister Significator",
            "Amatyakaraka",
            "AmK",
            2,
            "Career, Profession, Advisors",
            2,
            listOf("Mind", "Wealth through profession", "Right-hand person", "Government connections")
        ),
        BHRATRIKARAKA(
            "Sibling Significator",
            "Bhratrikaraka",
            "BK",
            3,
            "Siblings, Courage, Efforts",
            3,
            listOf("Younger siblings", "Initiative", "Skills", "Short journeys")
        ),
        MATRIKARAKA(
            "Mother Significator",
            "Matrikaraka",
            "MK",
            4,
            "Mother, Happiness, Property",
            4,
            listOf("Emotions", "Vehicles", "Education", "Domestic peace")
        ),
        PUTRAKARAKA(
            "Children Significator",
            "Putrakaraka",
            "PK",
            5,
            "Children, Intelligence, Past Merit",
            5,
            listOf("Creativity", "Romance", "Speculation", "Mantra siddhi")
        ),
        GNATIKARAKA(
            "Relatives Significator",
            "Gnatikaraka",
            "GK",
            6,
            "Cousins, Enemies, Diseases",
            6,
            listOf("Maternal relatives", "Obstacles", "Debts", "Service")
        ),
        DARAKARAKA(
            "Spouse Significator",
            "Darakaraka",
            "DK",
            7,
            "Spouse, Marriage, Partnership",
            7,
            listOf("Business partners", "Foreign travel", "Public dealings")
        ),
        STHIRA_KARAKA(
            "Fixed Significator",
            "Sthira Karaka",
            "StK",
            8,
            "Used in 8-karaka system for Rahu",
            8,
            listOf("Transformation", "Hidden matters", "Research")
        );

        companion object {
            fun fromRank(rank: Int): KarakaType? = entries.find { it.rank == rank }
        }
    }

    /**
     * Data class representing a single Karaka assignment
     */
    data class KarakaAssignment(
        val karakaType: KarakaType,
        val planet: Planet,
        val degreeInSign: Double,
        val longitude: Double,
        val sign: ZodiacSign,
        val house: Int,
        val isRetrograde: Boolean,
        val dignity: VedicAstrologyUtils.PlanetaryDignity,
        val interpretation: String
    ) {
        val isWellPlaced: Boolean
            get() = dignity in listOf(
                VedicAstrologyUtils.PlanetaryDignity.EXALTED,
                VedicAstrologyUtils.PlanetaryDignity.MOOLATRIKONA,
                VedicAstrologyUtils.PlanetaryDignity.OWN_SIGN,
                VedicAstrologyUtils.PlanetaryDignity.FRIEND_SIGN
            )

        val isAfflicted: Boolean
            get() = dignity in listOf(
                VedicAstrologyUtils.PlanetaryDignity.DEBILITATED,
                VedicAstrologyUtils.PlanetaryDignity.ENEMY_SIGN
            )
    }

    /**
     * Karakamsha analysis result
     */
    data class KarakamshaAnalysis(
        val karakamshaSign: ZodiacSign,
        val atmakarakaNavamsaPosition: PlanetPosition,
        val planetsInKarakamsha: List<PlanetPosition>,
        val karakamshaLord: Planet,
        val karakamshaLordPosition: PlanetPosition?,
        val karakamshaAspects: List<PlanetPosition>,
        val interpretation: String,
        val careerIndicators: List<String>,
        val spiritualIndicators: List<String>,
        val relationshipIndicators: List<String>
    )

    /**
     * Swamsha analysis result
     */
    data class SwamshaAnalysis(
        val swamshaSign: ZodiacSign,
        val swamshaLord: Planet,
        val swamshaLordPosition: PlanetPosition?,
        val planetsInSwamsha: List<PlanetPosition>,
        val interpretation: String
    )

    /**
     * Complete Jaimini Karaka analysis result
     */
    data class JaiminiKarakaAnalysis(
        val chartId: String,
        val karakaSystem: KarakaSystem,
        val karakas: Map<KarakaType, KarakaAssignment>,
        val karakaList: List<KarakaAssignment>,
        val karakamsha: KarakamshaAnalysis,
        val swamsha: SwamshaAnalysis,
        val argalaAnalysis: Map<Planet, ArgalaInfo>,
        val karakenshiYogas: List<KarakenshiYoga>,
        val overallScore: Double,
        val keyInsights: List<String>,
        val recommendations: List<String>,
        val timestamp: Long = System.currentTimeMillis()
    ) {
        fun getKaraka(type: KarakaType): KarakaAssignment? = karakas[type]
        fun getAtmakaraka(): KarakaAssignment? = karakas[KarakaType.ATMAKARAKA]
        fun getDarakaraka(): KarakaAssignment? = karakas[KarakaType.DARAKARAKA]
    }

    /**
     * Karaka system type (7 or 8 karaka)
     */
    enum class KarakaSystem(val planetCount: Int, val description: String) {
        SEVEN_KARAKA(7, "Traditional 7-Karaka system (excludes Rahu)"),
        EIGHT_KARAKA(8, "Extended 8-Karaka system (includes Rahu)")
    }

    /**
     * Argala (intervention) information for a planet
     */
    data class ArgalaInfo(
        val planet: Planet,
        val primaryArgala: List<Planet>,
        val secondaryArgala: List<Planet>,
        val virodhaArgala: List<Planet>,
        val netArgalaStrength: Double,
        val interpretation: String
    )

    /**
     * Karakenshi Yoga (yogas formed by karakas)
     */
    data class KarakenshiYoga(
        val name: String,
        val sanskritName: String,
        val involvedKarakas: List<KarakaType>,
        val involvedPlanets: List<Planet>,
        val strength: Double,
        val isAuspicious: Boolean,
        val effects: String,
        val activation: String
    )

    /**
     * Calculate complete Jaimini Karaka analysis
     *
     * @param chart The birth chart
     * @param system The karaka system to use (7 or 8 karaka)
     * @return Complete JaiminiKarakaAnalysis
     */
    fun calculateKarakas(
        chart: VedicChart,
        system: KarakaSystem = KarakaSystem.SEVEN_KARAKA
    ): JaiminiKarakaAnalysis {
        val planets = if (system == KarakaSystem.EIGHT_KARAKA) KARAKA_PLANETS_8 else KARAKA_PLANETS_7
        val planetPositions = chart.planetPositions.filter { it.planet in planets }

        // Calculate degrees in sign for each planet
        val planetDegrees = planetPositions.map { pos ->
            val degreeInSign = if (pos.planet == Planet.RAHU) {
                // Rahu's degree is calculated backwards from 30°
                30.0 - (pos.longitude % 30.0)
            } else {
                pos.longitude % 30.0
            }
            Triple(pos, degreeInSign, pos.planet)
        }.sortedByDescending { it.second }

        // Assign karakas based on degree (highest to lowest)
        val karakaAssignments = mutableMapOf<KarakaType, KarakaAssignment>()
        val karakaTypes = if (system == KarakaSystem.EIGHT_KARAKA) {
            KarakaType.entries.take(8)
        } else {
            KarakaType.entries.take(7)
        }

        planetDegrees.forEachIndexed { index, (pos, degreeInSign, _) ->
            if (index < karakaTypes.size) {
                val karakaType = karakaTypes[index]
                val dignity = VedicAstrologyUtils.getDignity(pos)
                karakaAssignments[karakaType] = KarakaAssignment(
                    karakaType = karakaType,
                    planet = pos.planet,
                    degreeInSign = degreeInSign,
                    longitude = pos.longitude,
                    sign = pos.sign,
                    house = pos.house,
                    isRetrograde = pos.isRetrograde,
                    dignity = dignity,
                    interpretation = buildKarakaInterpretation(karakaType, pos, dignity)
                )
            }
        }

        // Calculate Karakamsha
        val karakamsha = calculateKarakamsha(chart, karakaAssignments[KarakaType.ATMAKARAKA])

        // Calculate Swamsha
        val swamsha = calculateSwamsha(chart)

        // Calculate Argala
        val argalaAnalysis = calculateArgalaForKarakas(chart, karakaAssignments)

        // Identify Karakenshi Yogas
        val yogas = identifyKarakenshiYogas(chart, karakaAssignments)

        // Calculate overall score
        val overallScore = calculateOverallScore(karakaAssignments, karakamsha, yogas)

        // Generate insights and recommendations
        val insights = generateInsights(karakaAssignments, karakamsha, swamsha, yogas)
        val recommendations = generateRecommendations(karakaAssignments, karakamsha)

        return JaiminiKarakaAnalysis(
            chartId = generateChartId(chart),
            karakaSystem = system,
            karakas = karakaAssignments.toMap(),
            karakaList = karakaAssignments.values.sortedBy { it.karakaType.rank },
            karakamsha = karakamsha,
            swamsha = swamsha,
            argalaAnalysis = argalaAnalysis,
            karakenshiYogas = yogas,
            overallScore = overallScore,
            keyInsights = insights,
            recommendations = recommendations
        )
    }

    /**
     * Calculate Karakamsha - the Navamsa sign of Atmakaraka
     */
    private fun calculateKarakamsha(
        chart: VedicChart,
        atmakaraka: KarakaAssignment?
    ): KarakamshaAnalysis {
        if (atmakaraka == null) {
            return createEmptyKarakamshaAnalysis()
        }

        val navamsaCharts = DivisionalChartCalculator.calculateAllDivisionalCharts(chart)
        val navamsaChart = navamsaCharts.find { it.chartType == DivisionalChartType.D9_NAVAMSA }
            ?: return createEmptyKarakamshaAnalysis()

        val akNavamsaPos = navamsaChart.planetPositions.find { it.planet == atmakaraka.planet }
            ?: return createEmptyKarakamshaAnalysis()

        val karakamshaSign = akNavamsaPos.sign
        val karakamshaLord = karakamshaSign.ruler

        // Find planets in Karakamsha in Navamsa
        val planetsInKarakamsha = navamsaChart.planetPositions.filter { it.sign == karakamshaSign }

        // Find Karakamsha lord position in Navamsa
        val karakamshaLordPos = navamsaChart.planetPositions.find { it.planet == karakamshaLord }

        // Find planets aspecting Karakamsha
        val aspectingPlanets = navamsaChart.planetPositions.filter { pos ->
            pos.sign != karakamshaSign &&
                    VedicAstrologyUtils.aspectsHouse(pos.planet, pos.house, getHouseNumber(karakamshaSign, navamsaChart.ascendantSign))
        }

        // Build interpretation
        val careerIndicators = analyzeCareerFromKarakamsha(karakamshaSign, planetsInKarakamsha, karakamshaLordPos)
        val spiritualIndicators = analyzeSpiritualFromKarakamsha(karakamshaSign, planetsInKarakamsha)
        val relationshipIndicators = analyzeRelationshipsFromKarakamsha(karakamshaSign, planetsInKarakamsha)

        val interpretation = buildKarakamshaInterpretation(
            karakamshaSign, atmakaraka.planet, planetsInKarakamsha,
            careerIndicators, spiritualIndicators
        )

        return KarakamshaAnalysis(
            karakamshaSign = karakamshaSign,
            atmakarakaNavamsaPosition = akNavamsaPos,
            planetsInKarakamsha = planetsInKarakamsha,
            karakamshaLord = karakamshaLord,
            karakamshaLordPosition = karakamshaLordPos,
            karakamshaAspects = aspectingPlanets,
            interpretation = interpretation,
            careerIndicators = careerIndicators,
            spiritualIndicators = spiritualIndicators,
            relationshipIndicators = relationshipIndicators
        )
    }

    /**
     * Calculate Swamsha - the Navamsa Lagna
     */
    private fun calculateSwamsha(chart: VedicChart): SwamshaAnalysis {
        val navamsaCharts = DivisionalChartCalculator.calculateAllDivisionalCharts(chart)
        val navamsaChart = navamsaCharts.find { it.chartType == DivisionalChartType.D9_NAVAMSA }
            ?: return createEmptySwamshaAnalysis()

        val swamshaSign = navamsaChart.ascendantSign
        val swamshaLord = swamshaSign.ruler

        val swamshaLordPos = navamsaChart.planetPositions.find { it.planet == swamshaLord }
        val planetsInSwamsha = navamsaChart.planetPositions.filter { it.sign == swamshaSign }

        val interpretation = buildSwamshaInterpretation(swamshaSign, planetsInSwamsha)

        return SwamshaAnalysis(
            swamshaSign = swamshaSign,
            swamshaLord = swamshaLord,
            swamshaLordPosition = swamshaLordPos,
            planetsInSwamsha = planetsInSwamsha,
            interpretation = interpretation
        )
    }

    /**
     * Calculate Argala (intervention) for each Karaka planet
     */
    private fun calculateArgalaForKarakas(
        chart: VedicChart,
        karakas: Map<KarakaType, KarakaAssignment>
    ): Map<Planet, ArgalaInfo> {
        val argalaMap = mutableMapOf<Planet, ArgalaInfo>()

        for ((_, karaka) in karakas) {
            val karakaHouse = karaka.house

            // Primary Argala: Planets in 2nd, 4th, 11th from Karaka
            val primaryArgalaHouses = listOf(
                ((karakaHouse + 1 - 1) % 12) + 1,  // 2nd house
                ((karakaHouse + 3 - 1) % 12) + 1,  // 4th house
                ((karakaHouse + 10 - 1) % 12) + 1  // 11th house
            )

            val primaryArgala = chart.planetPositions
                .filter { it.house in primaryArgalaHouses && it.planet != karaka.planet }
                .map { it.planet }

            // Secondary Argala: Planets in 5th from Karaka
            val secondaryArgalaHouse = ((karakaHouse + 4 - 1) % 12) + 1
            val secondaryArgala = chart.planetPositions
                .filter { it.house == secondaryArgalaHouse && it.planet != karaka.planet }
                .map { it.planet }

            // Virodha (obstruction) Argala: Planets in 12th, 10th, 3rd from Karaka
            val virodhaHouses = listOf(
                ((karakaHouse + 11 - 1) % 12) + 1, // 12th house
                ((karakaHouse + 9 - 1) % 12) + 1,  // 10th house
                ((karakaHouse + 2 - 1) % 12) + 1   // 3rd house
            )

            val virodhaArgala = chart.planetPositions
                .filter { it.house in virodhaHouses && it.planet != karaka.planet }
                .map { it.planet }

            // Calculate net strength
            val primaryStrength = primaryArgala.sumOf { getPlanetArgalaStrength(it) }
            val secondaryStrength = secondaryArgala.sumOf { getPlanetArgalaStrength(it) } * 0.5
            val virodhaStrength = virodhaArgala.sumOf { getPlanetArgalaStrength(it) }

            val netStrength = (primaryStrength + secondaryStrength - virodhaStrength).coerceIn(-10.0, 10.0)

            val interpretation = buildArgalaInterpretation(karaka.karakaType, netStrength, primaryArgala, virodhaArgala)

            argalaMap[karaka.planet] = ArgalaInfo(
                planet = karaka.planet,
                primaryArgala = primaryArgala,
                secondaryArgala = secondaryArgala,
                virodhaArgala = virodhaArgala,
                netArgalaStrength = netStrength,
                interpretation = interpretation
            )
        }

        return argalaMap
    }

    /**
     * Identify Karakenshi Yogas (yogas based on Karaka relationships)
     */
    private fun identifyKarakenshiYogas(
        chart: VedicChart,
        karakas: Map<KarakaType, KarakaAssignment>
    ): List<KarakenshiYoga> {
        val yogas = mutableListOf<KarakenshiYoga>()

        val ak = karakas[KarakaType.ATMAKARAKA]
        val amk = karakas[KarakaType.AMATYAKARAKA]
        val bk = karakas[KarakaType.BHRATRIKARAKA]
        val mk = karakas[KarakaType.MATRIKARAKA]
        val pk = karakas[KarakaType.PUTRAKARAKA]
        val gk = karakas[KarakaType.GNATIKARAKA]
        val dk = karakas[KarakaType.DARAKARAKA]

        // Yoga 1: AK-AmK conjunction or mutual aspect - Raja Yoga
        if (ak != null && amk != null) {
            if (ak.house == amk.house || areMutuallyAspecting(ak, amk)) {
                yogas.add(
                    KarakenshiYoga(
                        name = "Atma-Amatya Yoga",
                        sanskritName = "Atmakaraka-Amatyakaraka Sambandha",
                        involvedKarakas = listOf(KarakaType.ATMAKARAKA, KarakaType.AMATYAKARAKA),
                        involvedPlanets = listOf(ak.planet, amk.planet),
                        strength = 85.0,
                        isAuspicious = true,
                        effects = "Person rises to positions of authority. Career aligned with soul purpose. Natural leadership abilities manifest.",
                        activation = "Activates during the dasha/antardasha of ${ak.planet.displayName} or ${amk.planet.displayName}"
                    )
                )
            }
        }

        // Yoga 2: AK-DK conjunction - Marriage significant for soul evolution
        if (ak != null && dk != null) {
            if (ak.house == dk.house) {
                yogas.add(
                    KarakenshiYoga(
                        name = "Atma-Dara Yoga",
                        sanskritName = "Atmakaraka-Darakaraka Yoga",
                        involvedKarakas = listOf(KarakaType.ATMAKARAKA, KarakaType.DARAKARAKA),
                        involvedPlanets = listOf(ak.planet, dk.planet),
                        strength = 80.0,
                        isAuspicious = true,
                        effects = "Spouse plays significant role in soul evolution. Marriage brings spiritual growth and material prosperity.",
                        activation = "Activates during the dasha of ${ak.planet.displayName} or ${dk.planet.displayName}"
                    )
                )
            }
        }

        // Yoga 3: AK in Kendra from Lagna - Strong will and life purpose
        if (ak != null && ak.house in VedicAstrologyUtils.KENDRA_HOUSES) {
            yogas.add(
                KarakenshiYoga(
                    name = "Atmakaraka Kendra Yoga",
                    sanskritName = "Atmakaraka Kendrasthana Yoga",
                    involvedKarakas = listOf(KarakaType.ATMAKARAKA),
                    involvedPlanets = listOf(ak.planet),
                    strength = 75.0,
                    isAuspicious = true,
                    effects = "Strong sense of self and purpose. Native achieves their life goals through determined effort.",
                    activation = "Active throughout life, especially strong during ${ak.planet.displayName} dasha"
                )
            )
        }

        // Yoga 4: AK exalted - Spiritually evolved soul
        if (ak != null && ak.dignity == VedicAstrologyUtils.PlanetaryDignity.EXALTED) {
            yogas.add(
                KarakenshiYoga(
                    name = "Uccha Atmakaraka Yoga",
                    sanskritName = "Uccha Atmakaraka Yoga",
                    involvedKarakas = listOf(KarakaType.ATMAKARAKA),
                    involvedPlanets = listOf(ak.planet),
                    strength = 90.0,
                    isAuspicious = true,
                    effects = "Soul has accumulated great merit from past lives. Native destined for significant achievements and spiritual progress.",
                    activation = "Active throughout life"
                )
            )
        }

        // Yoga 5: AK debilitated - Karmic lessons to learn
        if (ak != null && ak.dignity == VedicAstrologyUtils.PlanetaryDignity.DEBILITATED) {
            yogas.add(
                KarakenshiYoga(
                    name = "Neecha Atmakaraka Yoga",
                    sanskritName = "Neecha Atmakaraka Yoga",
                    involvedKarakas = listOf(KarakaType.ATMAKARAKA),
                    involvedPlanets = listOf(ak.planet),
                    strength = 40.0,
                    isAuspicious = false,
                    effects = "Soul has important karmic lessons to learn. Initial struggles lead to spiritual growth through humility.",
                    activation = "Most challenging during ${ak.planet.displayName} dasha"
                )
            )
        }

        // Yoga 6: PK-MK conjunction - Strong family bonds
        if (pk != null && mk != null && pk.house == mk.house) {
            yogas.add(
                KarakenshiYoga(
                    name = "Putra-Matri Yoga",
                    sanskritName = "Putrakaraka-Matrikaraka Sambandha",
                    involvedKarakas = listOf(KarakaType.PUTRAKARAKA, KarakaType.MATRIKARAKA),
                    involvedPlanets = listOf(pk.planet, mk.planet),
                    strength = 70.0,
                    isAuspicious = true,
                    effects = "Strong bond between mother and children. Happiness through family. Inherited property from maternal side.",
                    activation = "Activates during dasha of ${pk.planet.displayName} or ${mk.planet.displayName}"
                )
            )
        }

        // Yoga 7: GK-AK conjunction in dusthana - Challenges from relatives
        if (gk != null && ak != null && gk.house == ak.house && ak.house in VedicAstrologyUtils.DUSTHANA_HOUSES) {
            yogas.add(
                KarakenshiYoga(
                    name = "Gnati-Atma Dusthana Yoga",
                    sanskritName = "Gnatikaraka-Atmakaraka Dusthana Yoga",
                    involvedKarakas = listOf(KarakaType.GNATIKARAKA, KarakaType.ATMAKARAKA),
                    involvedPlanets = listOf(gk.planet, ak.planet),
                    strength = 35.0,
                    isAuspicious = false,
                    effects = "Challenges from relatives or cousins. Legal disputes possible. Health issues may arise from stress.",
                    activation = "Most challenging during ${gk.planet.displayName} dasha"
                )
            )
        }

        // Yoga 8: Multiple Karakas in same house - Concentration of karmic themes
        val houseOccupancy = karakas.values.groupBy { it.house }
        houseOccupancy.filter { it.value.size >= 3 }.forEach { (house, karakasInHouse) ->
            yogas.add(
                KarakenshiYoga(
                    name = "Karaka Sangama Yoga",
                    sanskritName = "Karaka Sangama",
                    involvedKarakas = karakasInHouse.map { it.karakaType },
                    involvedPlanets = karakasInHouse.map { it.planet },
                    strength = 65.0,
                    isAuspicious = house !in VedicAstrologyUtils.DUSTHANA_HOUSES,
                    effects = "Multiple life themes concentrated in ${getHouseName(house)}. Events related to this house are significant.",
                    activation = "Activates during dasha of any planet in house $house"
                )
            )
        }

        return yogas
    }

    // ============================================
    // HELPER METHODS
    // ============================================

    private fun getPlanetArgalaStrength(planet: Planet): Double {
        return when (planet) {
            Planet.JUPITER -> 1.5
            Planet.VENUS -> 1.3
            Planet.MERCURY -> 1.0
            Planet.MOON -> 1.0
            Planet.SUN -> 0.8
            Planet.MARS -> 0.7
            Planet.SATURN -> 0.5
            Planet.RAHU, Planet.KETU -> 0.6
            else -> 0.5
        }
    }

    private fun areMutuallyAspecting(k1: KarakaAssignment, k2: KarakaAssignment): Boolean {
        val dist1 = ((k2.house - k1.house + 12) % 12) + 1
        val dist2 = ((k1.house - k2.house + 12) % 12) + 1

        val k1AspectsK2 = dist1 == 7 ||
                (k1.planet == Planet.MARS && dist1 in listOf(4, 8)) ||
                (k1.planet == Planet.JUPITER && dist1 in listOf(5, 9)) ||
                (k1.planet == Planet.SATURN && dist1 in listOf(3, 10))

        val k2AspectsK1 = dist2 == 7 ||
                (k2.planet == Planet.MARS && dist2 in listOf(4, 8)) ||
                (k2.planet == Planet.JUPITER && dist2 in listOf(5, 9)) ||
                (k2.planet == Planet.SATURN && dist2 in listOf(3, 10))

        return k1AspectsK2 || k2AspectsK1
    }

    private fun getHouseNumber(sign: ZodiacSign, ascSign: ZodiacSign): Int {
        return ((sign.number - ascSign.number + 12) % 12) + 1
    }

    private fun getHouseName(house: Int): String {
        return when (house) {
            1 -> "1st house (Self/Lagna)"
            2 -> "2nd house (Wealth/Family)"
            3 -> "3rd house (Siblings/Courage)"
            4 -> "4th house (Mother/Happiness)"
            5 -> "5th house (Children/Intelligence)"
            6 -> "6th house (Enemies/Diseases)"
            7 -> "7th house (Spouse/Partnerships)"
            8 -> "8th house (Longevity/Transformation)"
            9 -> "9th house (Fortune/Dharma)"
            10 -> "10th house (Career/Karma)"
            11 -> "11th house (Gains/Desires)"
            12 -> "12th house (Losses/Liberation)"
            else -> "House $house"
        }
    }

    // ============================================
    // INTERPRETATION BUILDERS
    // ============================================

    private fun buildKarakaInterpretation(
        karakaType: KarakaType,
        pos: PlanetPosition,
        dignity: VedicAstrologyUtils.PlanetaryDignity
    ): String {
        val planetName = pos.planet.displayName
        val signName = pos.sign.displayName
        val houseName = getHouseName(pos.house)

        val dignityText = when (dignity) {
            VedicAstrologyUtils.PlanetaryDignity.EXALTED -> "exalted, giving excellent results"
            VedicAstrologyUtils.PlanetaryDignity.MOOLATRIKONA -> "in Moolatrikona, functioning optimally"
            VedicAstrologyUtils.PlanetaryDignity.OWN_SIGN -> "in own sign, comfortable and strong"
            VedicAstrologyUtils.PlanetaryDignity.FRIEND_SIGN -> "in friendly sign, supported"
            VedicAstrologyUtils.PlanetaryDignity.NEUTRAL_SIGN -> "in neutral sign, average results"
            VedicAstrologyUtils.PlanetaryDignity.ENEMY_SIGN -> "in enemy sign, facing challenges"
            VedicAstrologyUtils.PlanetaryDignity.DEBILITATED -> "debilitated, requiring remedial measures"
        }

        return "$planetName as ${karakaType.sanskritName} (${karakaType.displayName}) is in $signName in $houseName, $dignityText. " +
                "This indicates ${karakaType.primarySignification.lowercase()} matters are influenced by ${planetName}'s nature and placement."
    }

    private fun buildKarakamshaInterpretation(
        sign: ZodiacSign,
        akPlanet: Planet,
        planetsInSign: List<PlanetPosition>,
        careerIndicators: List<String>,
        spiritualIndicators: List<String>
    ): String {
        val signName = sign.displayName
        val planetNames = planetsInSign.joinToString(", ") { it.planet.displayName }

        return buildString {
            append("Karakamsha is in $signName. ")
            append("The Atmakaraka ${akPlanet.displayName} in Navamsa $signName indicates the soul's deepest desires and evolutionary path. ")

            if (planetsInSign.isNotEmpty()) {
                append("Planets in Karakamsha ($planetNames) influence career direction and spiritual growth. ")
            }

            if (careerIndicators.isNotEmpty()) {
                append("Career indications: ${careerIndicators.take(2).joinToString("; ")}. ")
            }

            if (spiritualIndicators.isNotEmpty()) {
                append("Spiritual path: ${spiritualIndicators.take(2).joinToString("; ")}.")
            }
        }
    }

    private fun buildSwamshaInterpretation(sign: ZodiacSign, planetsInSign: List<PlanetPosition>): String {
        val signName = sign.displayName
        val planetNames = planetsInSign.joinToString(", ") { it.planet.displayName }

        return buildString {
            append("Swamsha (Navamsa Lagna) is in $signName. ")
            append("This indicates the native's inner self and spiritual constitution aligned with ${signName}'s qualities. ")

            if (planetsInSign.isNotEmpty()) {
                append("Planets in Swamsha ($planetNames) color the expression of the inner self and dharmic path.")
            }
        }
    }

    private fun buildArgalaInterpretation(
        karakaType: KarakaType,
        netStrength: Double,
        primaryArgala: List<Planet>,
        virodhaArgala: List<Planet>
    ): String {
        val strengthDesc = when {
            netStrength >= 5.0 -> "strongly supported"
            netStrength >= 2.0 -> "moderately supported"
            netStrength >= -2.0 -> "neutral intervention"
            netStrength >= -5.0 -> "moderately obstructed"
            else -> "significantly challenged"
        }

        return buildString {
            append("${karakaType.sanskritName} is $strengthDesc by Argala. ")
            if (primaryArgala.isNotEmpty()) {
                append("Support from ${primaryArgala.joinToString(", ") { it.displayName }}. ")
            }
            if (virodhaArgala.isNotEmpty()) {
                append("Obstruction from ${virodhaArgala.joinToString(", ") { it.displayName }}.")
            }
        }
    }

    // ============================================
    // ANALYSIS HELPERS
    // ============================================

    private fun analyzeCareerFromKarakamsha(
        sign: ZodiacSign,
        planetsInSign: List<PlanetPosition>,
        lordPos: PlanetPosition?
    ): List<String> {
        val indicators = mutableListOf<String>()

        // Sign-based career indicators
        when (sign) {
            ZodiacSign.ARIES, ZodiacSign.SCORPIO -> indicators.add("Leadership roles, military, surgery, engineering")
            ZodiacSign.TAURUS, ZodiacSign.LIBRA -> indicators.add("Arts, beauty, luxury goods, finance, music")
            ZodiacSign.GEMINI, ZodiacSign.VIRGO -> indicators.add("Communication, writing, analysis, accounting")
            ZodiacSign.CANCER -> indicators.add("Nurturing professions, real estate, hospitality")
            ZodiacSign.LEO -> indicators.add("Government, politics, entertainment, management")
            ZodiacSign.SAGITTARIUS, ZodiacSign.PISCES -> indicators.add("Teaching, spirituality, law, philosophy, healing")
            ZodiacSign.CAPRICORN, ZodiacSign.AQUARIUS -> indicators.add("Technology, research, organization, social work")
        }

        // Planet-based indicators
        planetsInSign.forEach { pos ->
            when (pos.planet) {
                Planet.SUN -> indicators.add("Government service, administration, medicine")
                Planet.MOON -> indicators.add("Public dealing, hospitality, nursing, psychology")
                Planet.MARS -> indicators.add("Engineering, military, sports, surgery")
                Planet.MERCURY -> indicators.add("Business, writing, teaching, IT")
                Planet.JUPITER -> indicators.add("Education, law, finance, advisory")
                Planet.VENUS -> indicators.add("Arts, entertainment, luxury, fashion")
                Planet.SATURN -> indicators.add("Mining, agriculture, labor, organization")
                Planet.RAHU -> indicators.add("Foreign connections, technology, unconventional paths")
                Planet.KETU -> indicators.add("Spiritual work, research, occult sciences")
                else -> {}
            }
        }

        return indicators.distinct()
    }

    private fun analyzeSpiritualFromKarakamsha(
        sign: ZodiacSign,
        planetsInSign: List<PlanetPosition>
    ): List<String> {
        val indicators = mutableListOf<String>()

        // Spiritual signs
        if (sign in listOf(ZodiacSign.SAGITTARIUS, ZodiacSign.PISCES, ZodiacSign.CANCER, ZodiacSign.SCORPIO)) {
            indicators.add("Natural inclination toward spiritual practices")
        }

        // Jupiter or Ketu in Karakamsha
        if (planetsInSign.any { it.planet == Planet.JUPITER }) {
            indicators.add("Wisdom through traditional spiritual paths, guru connection")
        }
        if (planetsInSign.any { it.planet == Planet.KETU }) {
            indicators.add("Past life spiritual merit, moksha orientation")
        }
        if (planetsInSign.any { it.planet == Planet.MOON }) {
            indicators.add("Devotional path (Bhakti), emotional connection to divine")
        }
        if (planetsInSign.any { it.planet == Planet.SUN }) {
            indicators.add("Karma yoga, service-oriented spirituality")
        }
        if (planetsInSign.any { it.planet == Planet.SATURN }) {
            indicators.add("Discipline in spiritual practice, Tantra or Yoga path")
        }

        return indicators.distinct()
    }

    private fun analyzeRelationshipsFromKarakamsha(
        sign: ZodiacSign,
        planetsInSign: List<PlanetPosition>
    ): List<String> {
        val indicators = mutableListOf<String>()

        // Venus in Karakamsha
        if (planetsInSign.any { it.planet == Planet.VENUS }) {
            indicators.add("Strong desire for romantic relationships, artistic spouse")
        }

        // Mars in Karakamsha
        if (planetsInSign.any { it.planet == Planet.MARS }) {
            indicators.add("Passionate relationships, possible conflicts in marriage")
        }

        // Saturn in Karakamsha
        if (planetsInSign.any { it.planet == Planet.SATURN }) {
            indicators.add("Delayed marriage, mature/older spouse, karmic relationships")
        }

        // Rahu in Karakamsha
        if (planetsInSign.any { it.planet == Planet.RAHU }) {
            indicators.add("Unconventional relationships, foreign spouse possible")
        }

        // Sign-based
        when (sign) {
            ZodiacSign.LIBRA -> indicators.add("Marriage-oriented, seeks partnership")
            ZodiacSign.SCORPIO -> indicators.add("Intense relationships, transformation through partnerships")
            ZodiacSign.AQUARIUS -> indicators.add("Independent in relationships, needs space")
            ZodiacSign.PISCES -> indicators.add("Spiritual connection in relationships, sacrificing nature")
            else -> {}
        }

        return indicators.distinct()
    }

    // ============================================
    // SCORING AND INSIGHTS
    // ============================================

    private fun calculateOverallScore(
        karakas: Map<KarakaType, KarakaAssignment>,
        karakamsha: KarakamshaAnalysis,
        yogas: List<KarakenshiYoga>
    ): Double {
        var score = 50.0 // Base score

        // Score based on AK dignity
        karakas[KarakaType.ATMAKARAKA]?.let { ak ->
            score += when (ak.dignity) {
                VedicAstrologyUtils.PlanetaryDignity.EXALTED -> 20.0
                VedicAstrologyUtils.PlanetaryDignity.MOOLATRIKONA -> 15.0
                VedicAstrologyUtils.PlanetaryDignity.OWN_SIGN -> 12.0
                VedicAstrologyUtils.PlanetaryDignity.FRIEND_SIGN -> 8.0
                VedicAstrologyUtils.PlanetaryDignity.NEUTRAL_SIGN -> 0.0
                VedicAstrologyUtils.PlanetaryDignity.ENEMY_SIGN -> -8.0
                VedicAstrologyUtils.PlanetaryDignity.DEBILITATED -> -15.0
            }

            // AK in Kendra bonus
            if (ak.house in VedicAstrologyUtils.KENDRA_HOUSES) score += 5.0
            // AK in Trikona bonus
            if (ak.house in VedicAstrologyUtils.TRIKONA_HOUSES) score += 5.0
        }

        // Score based on yogas
        yogas.forEach { yoga ->
            score += if (yoga.isAuspicious) yoga.strength * 0.1 else -yoga.strength * 0.05
        }

        return score.coerceIn(0.0, 100.0)
    }

    private fun generateInsights(
        karakas: Map<KarakaType, KarakaAssignment>,
        karakamsha: KarakamshaAnalysis,
        swamsha: SwamshaAnalysis,
        yogas: List<KarakenshiYoga>
    ): List<String> {
        val insights = mutableListOf<String>()

        // AK insight
        karakas[KarakaType.ATMAKARAKA]?.let { ak ->
            insights.add("${ak.planet.displayName} as Atmakaraka indicates soul's primary lessons related to ${ak.planet.displayName}'s significations")
        }

        // DK insight
        karakas[KarakaType.DARAKARAKA]?.let { dk ->
            insights.add("${dk.planet.displayName} as Darakaraka indicates spouse with ${dk.planet.displayName}-like qualities")
        }

        // Karakamsha insight
        insights.add("Karakamsha in ${karakamsha.karakamshaSign.displayName} shows career and spiritual direction")

        // Yoga insights
        yogas.filter { it.isAuspicious }.take(2).forEach { yoga ->
            insights.add("${yoga.name} present - ${yoga.effects.take(60)}...")
        }

        return insights.take(5)
    }

    private fun generateRecommendations(
        karakas: Map<KarakaType, KarakaAssignment>,
        karakamsha: KarakamshaAnalysis
    ): List<String> {
        val recommendations = mutableListOf<String>()

        // AK-based recommendations
        karakas[KarakaType.ATMAKARAKA]?.let { ak ->
            when (ak.dignity) {
                VedicAstrologyUtils.PlanetaryDignity.DEBILITATED -> {
                    recommendations.add("Strengthen ${ak.planet.displayName} through appropriate remedies for soul-level growth")
                }
                VedicAstrologyUtils.PlanetaryDignity.EXALTED -> {
                    recommendations.add("Leverage strong ${ak.planet.displayName} for achieving life purpose")
                }
                else -> {}
            }

            // Gemstone recommendation based on AK
            val gemstone = getGemstoneForPlanet(ak.planet)
            recommendations.add("Consider wearing $gemstone for ${ak.planet.displayName} to support soul evolution")
        }

        // Karakamsha-based recommendations
        recommendations.add("Focus career efforts in directions indicated by ${karakamsha.karakamshaSign.displayName} Karakamsha")

        return recommendations.take(5)
    }

    private fun getGemstoneForPlanet(planet: Planet): String {
        return when (planet) {
            Planet.SUN -> "Ruby (Manikya)"
            Planet.MOON -> "Pearl (Moti)"
            Planet.MARS -> "Red Coral (Moonga)"
            Planet.MERCURY -> "Emerald (Panna)"
            Planet.JUPITER -> "Yellow Sapphire (Pukhraj)"
            Planet.VENUS -> "Diamond (Heera)"
            Planet.SATURN -> "Blue Sapphire (Neelam)"
            Planet.RAHU -> "Hessonite (Gomed)"
            Planet.KETU -> "Cat's Eye (Lehsunia)"
            else -> "appropriate gemstone"
        }
    }

    // ============================================
    // EMPTY RESULT CREATORS
    // ============================================

    private fun createEmptyKarakamshaAnalysis(): KarakamshaAnalysis {
        return KarakamshaAnalysis(
            karakamshaSign = ZodiacSign.ARIES,
            atmakarakaNavamsaPosition = PlanetPosition(
                planet = Planet.SUN,
                longitude = 0.0,
                latitude = 0.0,
                speed = 0.0,
                sign = ZodiacSign.ARIES,
                house = 1,
                nakshatra = com.astro.storm.core.model.Nakshatra.ASHWINI,
                nakshatraPada = 1,
                isRetrograde = false
            ),
            planetsInKarakamsha = emptyList(),
            karakamshaLord = Planet.MARS,
            karakamshaLordPosition = null,
            karakamshaAspects = emptyList(),
            interpretation = "Karakamsha analysis unavailable - Navamsa chart data needed",
            careerIndicators = emptyList(),
            spiritualIndicators = emptyList(),
            relationshipIndicators = emptyList()
        )
    }

    private fun createEmptySwamshaAnalysis(): SwamshaAnalysis {
        return SwamshaAnalysis(
            swamshaSign = ZodiacSign.ARIES,
            swamshaLord = Planet.MARS,
            swamshaLordPosition = null,
            planetsInSwamsha = emptyList(),
            interpretation = "Swamsha analysis unavailable - Navamsa chart data needed"
        )
    }

    private fun generateChartId(chart: VedicChart): String {
        val birthData = chart.birthData
        return "${birthData.name}-${birthData.dateTime}-${birthData.latitude}-${birthData.longitude}"
            .replace(Regex("[^a-zA-Z0-9-]"), "_")
    }

    /**
     * Get a summary of karaka assignments for display
     */
    fun getKarakaSummary(analysis: JaiminiKarakaAnalysis, language: Language = Language.ENGLISH): String {
        return buildString {
            appendLine("═══════════════════════════════════════")
            appendLine("JAIMINI CHARA KARAKA ANALYSIS")
            appendLine("═══════════════════════════════════════")
            appendLine()
            appendLine("Karaka Assignments (${analysis.karakaSystem.description}):")
            appendLine("───────────────────────────────────────")

            analysis.karakaList.forEach { karaka ->
                val dignityStar = when (karaka.dignity) {
                    VedicAstrologyUtils.PlanetaryDignity.EXALTED -> "★★★"
                    VedicAstrologyUtils.PlanetaryDignity.MOOLATRIKONA,
                    VedicAstrologyUtils.PlanetaryDignity.OWN_SIGN -> "★★"
                    VedicAstrologyUtils.PlanetaryDignity.FRIEND_SIGN -> "★"
                    VedicAstrologyUtils.PlanetaryDignity.DEBILITATED -> "☆"
                    else -> ""
                }
                val abbr = karaka.karakaType.abbreviation.padEnd(4)
                val planet = karaka.planet.displayName.padEnd(10)
                val degree = String.format("%.2f°", karaka.degreeInSign)
                appendLine("$abbr $planet in ${karaka.sign.displayName.padEnd(12)} ($degree) $dignityStar")
            }

            appendLine()
            appendLine("Karakamsha: ${analysis.karakamsha.karakamshaSign.displayName}")
            appendLine("Swamsha: ${analysis.swamsha.swamshaSign.displayName}")
            appendLine()
            appendLine("Overall Score: ${String.format("%.1f", analysis.overallScore)}%")

            if (analysis.karakenshiYogas.isNotEmpty()) {
                appendLine()
                appendLine("Karakenshi Yogas:")
                appendLine("───────────────────────────────────────")
                analysis.karakenshiYogas.take(3).forEach { yoga ->
                    val status = if (yoga.isAuspicious) "✓" else "✗"
                    appendLine("$status ${yoga.name} (${yoga.sanskritName})")
                }
            }
        }
    }
}
