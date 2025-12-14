package com.astro.storm.ephemeris

import com.astro.storm.data.model.Planet
import com.astro.storm.data.model.PlanetPosition
import com.astro.storm.data.model.VedicChart
import com.astro.storm.data.model.ZodiacSign

/**
 * Lal Kitab Remedies Calculator
 *
 * Implements Lal Kitab-based remedial system with practical solutions.
 * Note: Lal Kitab is a distinct system from classical Vedic astrology
 * and should be clearly labeled as such.
 *
 * Reference: Lal Kitab original texts, Pandit Roop Chand Joshi's interpretations
 */
object LalKitabRemediesCalculator {

    /**
     * Lal Kitab house associations (different from classical Vedic)
     * In Lal Kitab, houses have specific planetary "ownership" patterns
     */
    private val LAL_KITAB_HOUSE_LORDS = mapOf(
        1 to Planet.MARS,     // Aries natural
        2 to Planet.VENUS,    // Taurus natural
        3 to Planet.MERCURY,  // Gemini natural
        4 to Planet.MOON,     // Cancer natural
        5 to Planet.SUN,      // Leo natural
        6 to Planet.MERCURY,  // Virgo natural
        7 to Planet.VENUS,    // Libra natural
        8 to Planet.MARS,     // Scorpio natural
        9 to Planet.JUPITER,  // Sagittarius natural
        10 to Planet.SATURN,  // Capricorn natural
        11 to Planet.SATURN,  // Aquarius natural
        12 to Planet.JUPITER  // Pisces natural
    )

    /**
     * Analyze Lal Kitab chart and generate remedies
     */
    fun analyzeLalKitab(chart: VedicChart): LalKitabAnalysis {
        val planetaryAfflictions = analyzePlanetaryAfflictions(chart)
        val debts = analyzeKarmicDebts(chart)
        val houseAnalysis = analyzeHousesLalKitab(chart)
        val remedies = generateRemedies(planetaryAfflictions, debts, houseAnalysis)
        val annualCalendar = generateAnnualRemedyCalendar()

        return LalKitabAnalysis(
            systemNote = "Lal Kitab is a distinct astrological system from classical Vedic astrology. " +
                        "These remedies are based on Lal Kitab principles and traditions.",
            planetaryAfflictions = planetaryAfflictions,
            karmicDebts = debts,
            houseAnalysis = houseAnalysis,
            remedies = remedies,
            colorRemedies = generateColorRemedies(planetaryAfflictions),
            directionRemedies = generateDirectionRemedies(planetaryAfflictions),
            annualCalendar = annualCalendar,
            generalRecommendations = generateGeneralRecommendations(chart)
        )
    }

    /**
     * Analyze planetary afflictions per Lal Kitab
     */
    private fun analyzePlanetaryAfflictions(chart: VedicChart): List<PlanetaryAffliction> {
        val afflictions = mutableListOf<PlanetaryAffliction>()

        chart.planetPositions.forEach { position ->
            val house = calculateLalKitabHouse(chart, position)
            val naturalLord = LAL_KITAB_HOUSE_LORDS[house]
            val isAfflicted = checkAffliction(position.planet, house, chart)
            val afflictionType = determineAfflictionType(position.planet, house, chart)

            if (isAfflicted || afflictionType != AfflictionType.NONE) {
                afflictions.add(
                    PlanetaryAffliction(
                        planet = position.planet,
                        house = house,
                        naturalLord = naturalLord,
                        afflictionType = afflictionType,
                        severity = calculateAfflictionSeverity(position.planet, house, chart),
                        effects = getAfflictionEffects(position.planet, house, afflictionType),
                        remedies = getPlanetaryRemedies(position.planet, house)
                    )
                )
            }
        }

        return afflictions.sortedByDescending { it.severity }
    }

    private fun calculateLalKitabHouse(chart: VedicChart, position: PlanetPosition): Int {
        val ascSign = ZodiacSign.fromLongitude(chart.ascendant)
        return ((position.sign.ordinal - ascSign.ordinal + 12) % 12) + 1
    }

    private fun checkAffliction(planet: Planet, house: Int, chart: VedicChart): Boolean {
        // Lal Kitab considers certain house placements problematic
        return when (planet) {
            Planet.RAHU -> house in listOf(1, 5, 7, 8, 12)
            Planet.KETU -> house in listOf(2, 4, 5, 7)
            Planet.SATURN -> house in listOf(1, 4, 5, 7)
            Planet.MARS -> house in listOf(4, 7, 8, 12)
            Planet.SUN -> house in listOf(6, 7, 8, 12)
            Planet.MOON -> house in listOf(8, 12)
            else -> false
        }
    }

    private fun determineAfflictionType(planet: Planet, house: Int, chart: VedicChart): AfflictionType {
        // Check for Lal Kitab specific afflictions
        val hasSaturnAspect = chart.planetPositions.any { pos ->
            pos.planet == Planet.SATURN &&
            calculateLalKitabHouse(chart, pos) in listOf(
                (house + 2) % 12 + 1,
                (house + 6) % 12 + 1,
                (house + 9) % 12 + 1
            )
        }

        val hasRahuKetu = chart.planetPositions.any { pos ->
            pos.planet in listOf(Planet.RAHU, Planet.KETU) &&
            calculateLalKitabHouse(chart, pos) == house
        }

        return when {
            planet == Planet.MOON && house == 8 -> AfflictionType.PITRU_DOSH
            planet == Planet.VENUS && house == 6 -> AfflictionType.STRI_RIN
            planet == Planet.JUPITER && house == 8 -> AfflictionType.PITRU_DOSH
            planet == Planet.MERCURY && house == 12 -> AfflictionType.KANYA_RIN
            hasRahuKetu && planet != Planet.RAHU && planet != Planet.KETU -> AfflictionType.GRAHAN_DOSH
            hasSaturnAspect && planet in listOf(Planet.SUN, Planet.MOON) -> AfflictionType.SHANI_PEEDA
            else -> AfflictionType.NONE
        }
    }

    private fun calculateAfflictionSeverity(planet: Planet, house: Int, chart: VedicChart): AfflictionSeverity {
        var score = 0

        // Base severity from planet-house combination
        when {
            planet == Planet.RAHU && house in listOf(1, 7) -> score += 3
            planet == Planet.SATURN && house in listOf(1, 4, 7) -> score += 3
            planet == Planet.MARS && house in listOf(4, 7, 8) -> score += 2
            planet == Planet.KETU && house in listOf(2, 5) -> score += 2
            else -> score += 1
        }

        // Check for additional afflictions
        val conjunctions = chart.planetPositions.count { pos ->
            calculateLalKitabHouse(chart, pos) == house
        }
        if (conjunctions > 2) score += 1

        return when {
            score >= 4 -> AfflictionSeverity.SEVERE
            score >= 3 -> AfflictionSeverity.MODERATE
            score >= 2 -> AfflictionSeverity.MILD
            else -> AfflictionSeverity.MINIMAL
        }
    }

    private fun getAfflictionEffects(planet: Planet, house: Int, type: AfflictionType): List<String> {
        val effects = mutableListOf<String>()

        // Type-specific effects
        when (type) {
            AfflictionType.PITRU_DOSH -> effects.addAll(listOf(
                "Ancestral karma affecting current life",
                "Obstacles in progeny matters",
                "Health issues related to father's lineage"
            ))
            AfflictionType.MATRU_RIN -> effects.addAll(listOf(
                "Debt towards mother figure",
                "Property and comfort matters affected",
                "Mental peace disturbances"
            ))
            AfflictionType.STRI_RIN -> effects.addAll(listOf(
                "Debt towards women/wife",
                "Marriage and relationship challenges",
                "Financial fluctuations"
            ))
            AfflictionType.KANYA_RIN -> effects.addAll(listOf(
                "Debt towards unmarried girls/daughters",
                "Female child related matters",
                "Education disruptions"
            ))
            AfflictionType.GRAHAN_DOSH -> effects.addAll(listOf(
                "Eclipse-like effect on planet's significations",
                "Sudden obstacles and confusions",
                "Hidden matters surfacing"
            ))
            AfflictionType.SHANI_PEEDA -> effects.addAll(listOf(
                "Saturn's restrictive influence",
                "Delays and obstacles",
                "Karmic lessons intensified"
            ))
            AfflictionType.NONE -> {}
        }

        // Planet-house specific effects
        effects.addAll(getPlanetHouseEffects(planet, house))

        return effects.take(5)
    }

    private fun getPlanetHouseEffects(planet: Planet, house: Int): List<String> {
        return when (planet) {
            Planet.SUN -> when (house) {
                6, 8, 12 -> listOf("Authority challenges", "Government-related obstacles", "Father's health concerns")
                else -> listOf("Leadership matters affected by house placement")
            }
            Planet.MOON -> when (house) {
                8, 12 -> listOf("Mental peace disturbances", "Mother's health concerns", "Emotional fluctuations")
                else -> listOf("Emotional matters influenced by house placement")
            }
            Planet.MARS -> when (house) {
                4, 7, 8 -> listOf("Property disputes possible", "Relationship conflicts", "Accident-prone periods")
                else -> listOf("Energy and courage matters affected")
            }
            Planet.MERCURY -> when (house) {
                8, 12 -> listOf("Communication difficulties", "Business challenges", "Education obstacles")
                else -> listOf("Intellectual matters influenced")
            }
            Planet.JUPITER -> when (house) {
                8 -> listOf("Wisdom blocked", "Children-related challenges", "Spiritual obstacles")
                else -> listOf("Fortune matters affected")
            }
            Planet.VENUS -> when (house) {
                6 -> listOf("Marriage delays or conflicts", "Relationship troubles", "Luxury denied")
                else -> listOf("Relationship and comfort matters affected")
            }
            Planet.SATURN -> when (house) {
                1, 4, 7 -> listOf("Health and longevity concerns", "Home comfort issues", "Partnership delays")
                else -> listOf("Karmic matters activated")
            }
            Planet.RAHU -> when (house) {
                1, 7 -> listOf("Identity confusion", "Sudden relationship issues", "Foreign-related complications")
                else -> listOf("Unconventional matters affected")
            }
            Planet.KETU -> when (house) {
                2, 5 -> listOf("Family wealth affected", "Children-related concerns", "Past karma surfacing")
                else -> listOf("Detachment themes activated")
            }
            else -> emptyList()
        }
    }

    /**
     * Analyze Karmic Debts (Rin) per Lal Kitab
     */
    private fun analyzeKarmicDebts(chart: VedicChart): List<KarmicDebt> {
        val debts = mutableListOf<KarmicDebt>()

        // Pitru Rin (Father's debt) - Sun/Jupiter in 8th or afflicted
        val sunHouse = chart.planetPositions.find { it.planet == Planet.SUN }?.let {
            calculateLalKitabHouse(chart, it)
        }
        val jupiterHouse = chart.planetPositions.find { it.planet == Planet.JUPITER }?.let {
            calculateLalKitabHouse(chart, it)
        }

        if (sunHouse in listOf(6, 8, 12) || jupiterHouse == 8) {
            debts.add(
                KarmicDebt(
                    type = DebtType.PITRU_RIN,
                    description = "Debt towards father and ancestors",
                    indicators = listOf(
                        "Sun in ${sunHouse?.let { getOrdinal(it) }} house",
                        if (jupiterHouse == 8) "Jupiter in 8th house" else null
                    ).filterNotNull(),
                    effects = listOf(
                        "Obstacles in career and authority",
                        "Father's health or relationship issues",
                        "Children face delays or challenges"
                    ),
                    remedies = listOf(
                        "Offer water to Sun at sunrise daily",
                        "Feed crows with sweet bread on Saturdays",
                        "Perform Shraddha rituals for ancestors",
                        "Help elderly people, especially fathers"
                    )
                )
            )
        }

        // Matru Rin (Mother's debt) - Moon in 8th/12th or afflicted
        val moonHouse = chart.planetPositions.find { it.planet == Planet.MOON }?.let {
            calculateLalKitabHouse(chart, it)
        }

        if (moonHouse in listOf(8, 12)) {
            debts.add(
                KarmicDebt(
                    type = DebtType.MATRU_RIN,
                    description = "Debt towards mother and maternal lineage",
                    indicators = listOf("Moon in ${moonHouse?.let { getOrdinal(it) }} house"),
                    effects = listOf(
                        "Mental peace disturbed",
                        "Mother's health concerns",
                        "Property and comfort issues"
                    ),
                    remedies = listOf(
                        "Serve milk or rice to mother daily",
                        "Donate white items on Mondays",
                        "Keep silver coin given by mother",
                        "Respect and serve elderly women"
                    )
                )
            )
        }

        // Stri Rin (Wife/Women's debt) - Venus afflicted
        val venusHouse = chart.planetPositions.find { it.planet == Planet.VENUS }?.let {
            calculateLalKitabHouse(chart, it)
        }

        if (venusHouse in listOf(6, 8, 12)) {
            debts.add(
                KarmicDebt(
                    type = DebtType.STRI_RIN,
                    description = "Debt towards wife and women",
                    indicators = listOf("Venus in ${venusHouse?.let { getOrdinal(it) }} house"),
                    effects = listOf(
                        "Marriage delays or conflicts",
                        "Relationship troubles",
                        "Financial instability"
                    ),
                    remedies = listOf(
                        "Donate white clothes to women on Fridays",
                        "Respect wife and all women",
                        "Offer rice and camphor at Goddess temple",
                        "Avoid exploiting women in any way"
                    )
                )
            )
        }

        // Kanya Rin (Unmarried girl's debt) - Mercury afflicted in certain houses
        val mercuryHouse = chart.planetPositions.find { it.planet == Planet.MERCURY }?.let {
            calculateLalKitabHouse(chart, it)
        }

        if (mercuryHouse in listOf(8, 12)) {
            debts.add(
                KarmicDebt(
                    type = DebtType.KANYA_RIN,
                    description = "Debt towards unmarried girls/daughters",
                    indicators = listOf("Mercury in ${mercuryHouse?.let { getOrdinal(it) }} house"),
                    effects = listOf(
                        "Daughter's welfare concerns",
                        "Education obstacles",
                        "Business communication issues"
                    ),
                    remedies = listOf(
                        "Donate green items to unmarried girls",
                        "Support girls' education",
                        "Bury green glass bottle filled with honey",
                        "Feed green vegetables to goats on Wednesdays"
                    )
                )
            )
        }

        return debts
    }

    /**
     * Analyze houses per Lal Kitab methodology
     */
    private fun analyzeHousesLalKitab(chart: VedicChart): List<LalKitabHouseAnalysis> {
        return (1..12).map { house ->
            val planetsInHouse = chart.planetPositions.filter { pos ->
                calculateLalKitabHouse(chart, pos) == house
            }
            val naturalLord = LAL_KITAB_HOUSE_LORDS[house]
            val hasAffliction = planetsInHouse.any { pos ->
                checkAffliction(pos.planet, house, chart)
            }

            LalKitabHouseAnalysis(
                house = house,
                naturalLord = naturalLord,
                occupyingPlanets = planetsInHouse.map { it.planet },
                lalKitabSignificance = getHouseLalKitabSignificance(house),
                status = when {
                    planetsInHouse.isEmpty() -> HouseStatus.EMPTY
                    hasAffliction -> HouseStatus.AFFLICTED
                    planetsInHouse.any { it.planet in AstrologicalConstants.NATURAL_BENEFICS } ->
                        HouseStatus.BENEFIC
                    else -> HouseStatus.OCCUPIED
                },
                recommendations = getHouseLalKitabRecommendations(house, planetsInHouse)
            )
        }
    }

    private fun getHouseLalKitabSignificance(house: Int): String {
        return when (house) {
            1 -> "Self, personality, health - the native's foundation"
            2 -> "Wealth, family, speech - material foundation"
            3 -> "Siblings, courage, communication - personal effort"
            4 -> "Mother, property, happiness - emotional foundation"
            5 -> "Children, intelligence, past merit - creative expression"
            6 -> "Enemies, disease, service - challenges to overcome"
            7 -> "Marriage, partnership, public - relationships"
            8 -> "Longevity, inheritance, occult - transformation"
            9 -> "Fortune, father, dharma - blessings and guidance"
            10 -> "Career, status, karma - worldly achievement"
            11 -> "Gains, income, aspirations - fulfillment"
            12 -> "Losses, moksha, foreign - spiritual liberation"
            else -> "General house matters"
        }
    }

    private fun getHouseLalKitabRecommendations(house: Int, planets: List<PlanetPosition>): List<String> {
        val recs = mutableListOf<String>()

        if (planets.isEmpty()) {
            recs.add("Strengthen ${LAL_KITAB_HOUSE_LORDS[house]?.displayName} for ${getOrdinal(house)} house matters")
        } else {
            planets.forEach { pos ->
                recs.addAll(getPlanetaryRemedies(pos.planet, house).take(2))
            }
        }

        return recs.distinct().take(3)
    }

    /**
     * Generate comprehensive remedies
     */
    private fun generateRemedies(
        afflictions: List<PlanetaryAffliction>,
        debts: List<KarmicDebt>,
        houseAnalysis: List<LalKitabHouseAnalysis>
    ): List<LalKitabRemedy> {
        val remedies = mutableListOf<LalKitabRemedy>()

        // Add remedies from afflictions
        afflictions.filter { it.severity in listOf(AfflictionSeverity.SEVERE, AfflictionSeverity.MODERATE) }
            .forEach { affliction ->
                affliction.remedies.forEach { remedy ->
                    remedies.add(
                        LalKitabRemedy(
                            category = RemedyCategory.PLANETARY,
                            forPlanet = affliction.planet,
                            forHouse = affliction.house,
                            remedy = remedy,
                            method = getRemedyMethod(remedy),
                            frequency = getRemedyFrequency(affliction.planet),
                            duration = "Continue for at least 43 days",
                            effectiveness = if (affliction.severity == AfflictionSeverity.SEVERE)
                                "High impact remedy" else "Moderate impact remedy"
                        )
                    )
                }
            }

        // Add remedies from debts
        debts.forEach { debt ->
            debt.remedies.forEach { remedy ->
                remedies.add(
                    LalKitabRemedy(
                        category = RemedyCategory.KARMIC_DEBT,
                        forPlanet = null,
                        forHouse = null,
                        remedy = remedy,
                        method = getRemedyMethod(remedy),
                        frequency = "Weekly or as specified",
                        duration = "Long-term practice recommended",
                        effectiveness = "Addresses root karmic cause"
                    )
                )
            }
        }

        return remedies.distinctBy { it.remedy }
    }

    private fun getPlanetaryRemedies(planet: Planet, house: Int): List<String> {
        val baseRemedies = when (planet) {
            Planet.SUN -> listOf(
                "Offer water to Sun at sunrise with copper vessel",
                "Feed wheat and jaggery to cows on Sundays",
                "Keep a solid silver square piece",
                "Wear Ruby ring on ring finger (if suitable)"
            )
            Planet.MOON -> listOf(
                "Offer milk to Shiva Lingam on Mondays",
                "Keep silver items and wear pearl",
                "Serve your mother and elderly women",
                "Keep drinking water in silver glass"
            )
            Planet.MARS -> listOf(
                "Recite Hanuman Chalisa daily",
                "Feed jaggery/wheat to monkeys on Tuesdays",
                "Keep deer skin at home",
                "Donate red items on Tuesdays"
            )
            Planet.MERCURY -> listOf(
                "Feed green grass to cows",
                "Bury green bottle with honey in deserted place",
                "Donate green vegetables on Wednesdays",
                "Keep a parrot or help parrots"
            )
            Planet.JUPITER -> listOf(
                "Apply saffron tilak on forehead",
                "Offer water at Peepal tree roots",
                "Donate yellow items on Thursdays",
                "Feed Brahmins and seek their blessings"
            )
            Planet.VENUS -> listOf(
                "Donate white items on Fridays",
                "Feed white cows with wheat and sugar",
                "Keep silver in your pocket",
                "Respect and help women"
            )
            Planet.SATURN -> listOf(
                "Donate black items on Saturdays",
                "Feed crows with sweet bread",
                "Serve elderly and disabled persons",
                "Offer mustard oil to Shani idol"
            )
            Planet.RAHU -> listOf(
                "Donate radish and blue/black items",
                "Keep barley under your pillow and feed fish with it next morning",
                "Float coconut in running water",
                "Feed dogs regularly"
            )
            Planet.KETU -> listOf(
                "Donate blanket to poor on Tuesdays",
                "Keep a dog as pet or feed stray dogs",
                "Wear two-toned (black and white) clothes",
                "Offer sesame seeds at temple"
            )
            else -> emptyList()
        }

        // Add house-specific remedies
        val houseRemedies = when (house) {
            6, 8, 12 -> listOf(
                "Perform charity for ${getOrdinal(house)} house remedy",
                "Avoid conflicts and maintain peace"
            )
            else -> emptyList()
        }

        return (baseRemedies + houseRemedies).distinct()
    }

    private fun getRemedyMethod(remedy: String): String {
        return when {
            remedy.contains("feed", ignoreCase = true) -> "Feeding ritual"
            remedy.contains("donate", ignoreCase = true) -> "Charity/Donation"
            remedy.contains("offer", ignoreCase = true) -> "Offering ritual"
            remedy.contains("keep", ignoreCase = true) -> "Protective item"
            remedy.contains("recite", ignoreCase = true) -> "Mantra/Prayer"
            remedy.contains("float", ignoreCase = true) -> "Water ritual"
            remedy.contains("bury", ignoreCase = true) -> "Earth ritual"
            else -> "General remedy"
        }
    }

    private fun getRemedyFrequency(planet: Planet): String {
        return when (planet) {
            Planet.SUN -> "Daily at sunrise"
            Planet.MOON -> "Mondays"
            Planet.MARS -> "Tuesdays"
            Planet.MERCURY -> "Wednesdays"
            Planet.JUPITER -> "Thursdays"
            Planet.VENUS -> "Fridays"
            Planet.SATURN -> "Saturdays"
            Planet.RAHU -> "Saturdays or specific days"
            Planet.KETU -> "Tuesdays or Saturdays"
            else -> "As recommended"
        }
    }

    /**
     * Generate color remedies
     */
    private fun generateColorRemedies(afflictions: List<PlanetaryAffliction>): List<ColorRemedy> {
        val colorRemedies = mutableListOf<ColorRemedy>()

        val affectedPlanets = afflictions.map { it.planet }.distinct()

        affectedPlanets.forEach { planet ->
            val colorData = getPlanetColor(planet)
            colorRemedies.add(
                ColorRemedy(
                    planet = planet,
                    favorableColors = colorData.favorable,
                    avoidColors = colorData.avoid,
                    application = colorData.application
                )
            )
        }

        return colorRemedies
    }

    private fun getPlanetColor(planet: Planet): ColorData {
        return when (planet) {
            Planet.SUN -> ColorData(
                favorable = listOf("Ruby Red", "Orange", "Bright Yellow"),
                avoid = listOf("Black", "Dark Blue"),
                application = "Wear these colors on Sundays, especially in upper garments"
            )
            Planet.MOON -> ColorData(
                favorable = listOf("White", "Silver", "Light Blue", "Cream"),
                avoid = listOf("Red", "Black"),
                application = "Prefer white clothes on Mondays, keep white items at home"
            )
            Planet.MARS -> ColorData(
                favorable = listOf("Red", "Orange", "Coral"),
                avoid = listOf("Blue", "Black"),
                application = "Wear red on Tuesdays, keep red items in south direction"
            )
            Planet.MERCURY -> ColorData(
                favorable = listOf("Green", "Light Green", "Parrot Green"),
                avoid = listOf("Red"),
                application = "Wear green on Wednesdays, keep green plants at home"
            )
            Planet.JUPITER -> ColorData(
                favorable = listOf("Yellow", "Saffron", "Gold"),
                avoid = listOf("Blue"),
                application = "Apply saffron tilak, wear yellow on Thursdays"
            )
            Planet.VENUS -> ColorData(
                favorable = listOf("White", "Pink", "Light Blue", "Cream"),
                avoid = listOf("Red", "Dark colors"),
                application = "Wear white or pink on Fridays, keep white items in bedroom"
            )
            Planet.SATURN -> ColorData(
                favorable = listOf("Black", "Navy Blue", "Dark Brown"),
                avoid = listOf("Red", "Bright Yellow"),
                application = "Wear blue/black on Saturdays, donate black items"
            )
            Planet.RAHU -> ColorData(
                favorable = listOf("Blue", "Grey", "Smoke colored"),
                avoid = listOf("Bright colors"),
                application = "Prefer subdued colors, avoid flashy dressing"
            )
            Planet.KETU -> ColorData(
                favorable = listOf("Grey", "Brown", "Mixed colors"),
                avoid = listOf("Bright single colors"),
                application = "Two-toned clothes work well, avoid bright single colors"
            )
            else -> ColorData(emptyList(), emptyList(), "")
        }
    }

    /**
     * Generate direction remedies
     */
    private fun generateDirectionRemedies(afflictions: List<PlanetaryAffliction>): List<DirectionRemedy> {
        val directionRemedies = mutableListOf<DirectionRemedy>()

        afflictions.forEach { affliction ->
            val dirData = getPlanetDirection(affliction.planet)
            directionRemedies.add(
                DirectionRemedy(
                    planet = affliction.planet,
                    favorableDirection = dirData.favorable,
                    avoidDirection = dirData.avoid,
                    application = dirData.application
                )
            )
        }

        return directionRemedies.distinctBy { it.planet }
    }

    private fun getPlanetDirection(planet: Planet): DirectionData {
        return when (planet) {
            Planet.SUN -> DirectionData(
                favorable = "East",
                avoid = "West",
                application = "Face east during Sun remedies, keep important items in east"
            )
            Planet.MOON -> DirectionData(
                favorable = "Northwest",
                avoid = "South",
                application = "Sleep with head towards northwest for mental peace"
            )
            Planet.MARS -> DirectionData(
                favorable = "South",
                avoid = "North",
                application = "Keep red items in south, face south for Mars mantras"
            )
            Planet.MERCURY -> DirectionData(
                favorable = "North",
                avoid = "South",
                application = "Study/work area in north direction is beneficial"
            )
            Planet.JUPITER -> DirectionData(
                favorable = "Northeast",
                avoid = "Southwest",
                application = "Prayer area in northeast, face northeast for Jupiter remedies"
            )
            Planet.VENUS -> DirectionData(
                favorable = "Southeast",
                avoid = "Northwest",
                application = "Keep bedroom in southeast if possible"
            )
            Planet.SATURN -> DirectionData(
                favorable = "West",
                avoid = "East",
                application = "West direction work benefits, donate facing west on Saturdays"
            )
            Planet.RAHU -> DirectionData(
                favorable = "Southwest",
                avoid = "Northeast",
                application = "Perform Rahu remedies facing southwest"
            )
            Planet.KETU -> DirectionData(
                favorable = "Northwest",
                avoid = "Southeast",
                application = "Northwest direction for spiritual practices"
            )
            else -> DirectionData("", "", "")
        }
    }

    /**
     * Generate annual remedy calendar
     */
    private fun generateAnnualRemedyCalendar(): List<AnnualRemedyEntry> {
        return listOf(
            AnnualRemedyEntry(
                day = "Sunday",
                planet = Planet.SUN,
                remedies = listOf(
                    "Offer water to Sun at sunrise",
                    "Feed wheat and jaggery to cows",
                    "Wear ruby red colored clothes"
                )
            ),
            AnnualRemedyEntry(
                day = "Monday",
                planet = Planet.MOON,
                remedies = listOf(
                    "Offer milk to Shiva Lingam",
                    "Wear white clothes",
                    "Serve mother and elderly women"
                )
            ),
            AnnualRemedyEntry(
                day = "Tuesday",
                planet = Planet.MARS,
                remedies = listOf(
                    "Recite Hanuman Chalisa",
                    "Feed monkeys",
                    "Donate red items"
                )
            ),
            AnnualRemedyEntry(
                day = "Wednesday",
                planet = Planet.MERCURY,
                remedies = listOf(
                    "Feed green grass to cows",
                    "Donate green vegetables",
                    "Wear green clothes"
                )
            ),
            AnnualRemedyEntry(
                day = "Thursday",
                planet = Planet.JUPITER,
                remedies = listOf(
                    "Apply saffron tilak",
                    "Offer water at Peepal tree",
                    "Donate yellow items"
                )
            ),
            AnnualRemedyEntry(
                day = "Friday",
                planet = Planet.VENUS,
                remedies = listOf(
                    "Donate white items",
                    "Feed white cows",
                    "Respect and help women"
                )
            ),
            AnnualRemedyEntry(
                day = "Saturday",
                planet = Planet.SATURN,
                remedies = listOf(
                    "Donate black items",
                    "Feed crows",
                    "Serve elderly and disabled"
                )
            )
        )
    }

    private fun generateGeneralRecommendations(chart: VedicChart): List<String> {
        return listOf(
            "Lal Kitab emphasizes practical, daily remedies over expensive rituals",
            "Consistency in remedies is more important than occasional grand gestures",
            "Charity (daan) is considered highly effective in Lal Kitab",
            "Respecting elders and serving the needy brings general planetary blessings",
            "Keep your ancestral items with respect for Pitru dosh relief",
            "Avoid hoarding money - keep it flowing through charity",
            "Maintain clean kitchen and bathroom for overall planetary harmony"
        )
    }

    private fun getOrdinal(number: Int): String {
        return when (number) {
            1 -> "1st"
            2 -> "2nd"
            3 -> "3rd"
            else -> "${number}th"
        }
    }
}

// Data classes
data class LalKitabAnalysis(
    val systemNote: String,
    val planetaryAfflictions: List<PlanetaryAffliction>,
    val karmicDebts: List<KarmicDebt>,
    val houseAnalysis: List<LalKitabHouseAnalysis>,
    val remedies: List<LalKitabRemedy>,
    val colorRemedies: List<ColorRemedy>,
    val directionRemedies: List<DirectionRemedy>,
    val annualCalendar: List<AnnualRemedyEntry>,
    val generalRecommendations: List<String>
)

data class PlanetaryAffliction(
    val planet: Planet,
    val house: Int,
    val naturalLord: Planet?,
    val afflictionType: AfflictionType,
    val severity: AfflictionSeverity,
    val effects: List<String>,
    val remedies: List<String>
)

enum class AfflictionType {
    NONE, PITRU_DOSH, MATRU_RIN, STRI_RIN, KANYA_RIN, GRAHAN_DOSH, SHANI_PEEDA
}

enum class AfflictionSeverity {
    MINIMAL, MILD, MODERATE, SEVERE
}

data class KarmicDebt(
    val type: DebtType,
    val description: String,
    val indicators: List<String>,
    val effects: List<String>,
    val remedies: List<String>
)

enum class DebtType {
    PITRU_RIN, MATRU_RIN, STRI_RIN, KANYA_RIN
}

data class LalKitabHouseAnalysis(
    val house: Int,
    val naturalLord: Planet?,
    val occupyingPlanets: List<Planet>,
    val lalKitabSignificance: String,
    val status: HouseStatus,
    val recommendations: List<String>
)

enum class HouseStatus {
    EMPTY, OCCUPIED, BENEFIC, AFFLICTED
}

data class LalKitabRemedy(
    val category: RemedyCategory,
    val forPlanet: Planet?,
    val forHouse: Int?,
    val remedy: String,
    val method: String,
    val frequency: String,
    val duration: String,
    val effectiveness: String
)

enum class RemedyCategory {
    PLANETARY, KARMIC_DEBT, HOUSE_BASED, GENERAL
}

data class ColorRemedy(
    val planet: Planet,
    val favorableColors: List<String>,
    val avoidColors: List<String>,
    val application: String
)

data class ColorData(
    val favorable: List<String>,
    val avoid: List<String>,
    val application: String
)

data class DirectionRemedy(
    val planet: Planet,
    val favorableDirection: String,
    val avoidDirection: String,
    val application: String
)

data class DirectionData(
    val favorable: String,
    val avoid: String,
    val application: String
)

data class AnnualRemedyEntry(
    val day: String,
    val planet: Planet,
    val remedies: List<String>
)
