package com.astro.storm.ephemeris

import com.astro.storm.core.model.Planet
import com.astro.storm.core.model.PlanetPosition
import com.astro.storm.core.model.VedicChart
import com.astro.storm.core.model.ZodiacSign
import com.astro.storm.core.common.Language
import com.astro.storm.core.common.StringKeyLalKitab
import com.astro.storm.core.common.StringKeyAnalysis
import com.astro.storm.core.common.StringKeyRemedy
import com.astro.storm.core.common.StringResources

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
    fun analyzeLalKitab(chart: VedicChart, language: Language): LalKitabAnalysis {
        val planetaryAfflictions = analyzePlanetaryAfflictions(chart, language)
        val debts = analyzeKarmicDebts(chart, language)
        val houseAnalysis = analyzeHousesLalKitab(chart, language)
        val remedies = generateRemedies(planetaryAfflictions, debts, houseAnalysis, language)
        val annualCalendar = generateAnnualRemedyCalendar(language)

        return LalKitabAnalysis(
            systemNote = StringResources.get(StringKeyGeneralPart11.SYSTEM_NOTE, language),
            planetaryAfflictions = planetaryAfflictions,
            karmicDebts = debts,
            houseAnalysis = houseAnalysis,
            remedies = remedies,
            colorRemedies = generateColorRemedies(planetaryAfflictions, language),
            directionRemedies = generateDirectionRemedies(planetaryAfflictions, language),
            annualCalendar = annualCalendar,
            generalRecommendations = generateGeneralRecommendations(chart, language)
        )
    }

    /**
     * Analyze planetary afflictions per Lal Kitab
     */
    private fun analyzePlanetaryAfflictions(chart: VedicChart, language: Language): List<PlanetaryAffliction> {
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
                        effects = getAfflictionEffects(position.planet, house, afflictionType, language),
                        remedies = getPlanetaryRemedies(position.planet, house, language)
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

    private fun getAfflictionEffects(planet: Planet, house: Int, type: AfflictionType, language: Language): List<String> {
        val effects = mutableListOf<String>()

        // Type-specific effects
        when (type) {
            AfflictionType.PITRU_DOSH -> effects.addAll(listOf(
                StringResources.get(StringKeyGeneralPart1.AFFL_PITRU, language),
                StringResources.get(StringKeyGeneralPart1.AFFL_PITRU_PROGENY, language),
                StringResources.get(StringKeyGeneralPart1.AFFL_PITRU_HEALTH, language)
            ))
            AfflictionType.MATRU_RIN -> effects.addAll(listOf(
                StringResources.get(StringKeyGeneralPart1.AFFL_MATRU, language),
                StringResources.get(StringKeyGeneralPart1.AFFL_MATRU_PROP, language),
                StringResources.get(StringKeyGeneralPart1.AFFL_MATRU_PEACE, language)
            ))
            AfflictionType.STRI_RIN -> effects.addAll(listOf(
                StringResources.get(StringKeyGeneralPart1.AFFL_STRI, language),
                StringResources.get(StringKeyGeneralPart1.AFFL_STRI_MARR, language),
                StringResources.get(StringKeyGeneralPart1.AFFL_STRI_FIN, language)
            ))
            AfflictionType.KANYA_RIN -> effects.addAll(listOf(
                StringResources.get(StringKeyGeneralPart1.AFFL_KANYA, language),
                StringResources.get(StringKeyGeneralPart1.AFFL_KANYA_CHILD, language),
                StringResources.get(StringKeyGeneralPart1.AFFL_KANYA_EDU, language)
            ))
            AfflictionType.GRAHAN_DOSH -> effects.addAll(listOf(
                StringResources.get(StringKeyGeneralPart1.AFFL_GRAHAN, language),
                StringResources.get(StringKeyGeneralPart1.AFFL_GRAHAN_SUDDEN, language),
                StringResources.get(StringKeyGeneralPart1.AFFL_GRAHAN_HIDDEN, language)
            ))
            AfflictionType.SHANI_PEEDA -> effects.addAll(listOf(
                StringResources.get(StringKeyGeneralPart1.AFFL_SHANI, language),
                StringResources.get(StringKeyGeneralPart1.AFFL_SHANI_DELAY, language),
                StringResources.get(StringKeyGeneralPart1.AFFL_SHANI_INTENSE, language)
            ))
            AfflictionType.NONE -> {}
        }

        // Planet-house specific effects
        effects.addAll(getPlanetHouseEffects(planet, house, language))

        return effects.take(5)
    }

    private fun getPlanetHouseEffects(planet: Planet, house: Int, language: Language): List<String> {
        return when (planet) {
            Planet.SUN -> when (house) {
                6, 8, 12 -> listOf(
                    StringResources.get(StringKeyGeneralPart4.EFFECT_SUN_AUTH, language),
                    StringResources.get(StringKeyGeneralPart4.EFFECT_SUN_GOVT, language),
                    StringResources.get(StringKeyGeneralPart4.EFFECT_SUN_FATHER, language)
                )
                else -> listOf(StringResources.get(StringKeyGeneralPart4.EFFECT_SUN_GEN, language))
            }
            Planet.MOON -> when (house) {
                8, 12 -> listOf(
                    StringResources.get(StringKeyGeneralPart4.EFFECT_MOON_PEACE, language),
                    StringResources.get(StringKeyGeneralPart4.EFFECT_MOON_MOTHER, language),
                    StringResources.get(StringKeyGeneralPart4.EFFECT_MOON_EMO, language)
                )
                else -> listOf(StringResources.get(StringKeyGeneralPart4.EFFECT_MOON_GEN, language))
            }
            Planet.MARS -> when (house) {
                4, 7, 8 -> listOf(
                    StringResources.get(StringKeyGeneralPart4.EFFECT_MARS_PROP, language),
                    StringResources.get(StringKeyGeneralPart4.EFFECT_MARS_REL, language),
                    StringResources.get(StringKeyGeneralPart4.EFFECT_MARS_ACC, language)
                )
                else -> listOf(StringResources.get(StringKeyGeneralPart4.EFFECT_MARS_GEN, language))
            }
            Planet.MERCURY -> when (house) {
                8, 12 -> listOf(
                    StringResources.get(StringKeyGeneralPart4.EFFECT_MERC_COMM, language),
                    StringResources.get(StringKeyGeneralPart4.EFFECT_MERC_BIZ, language),
                    StringResources.get(StringKeyGeneralPart4.EFFECT_MERC_EDU, language)
                )
                else -> listOf(StringResources.get(StringKeyGeneralPart4.EFFECT_MERC_GEN, language))
            }
            Planet.JUPITER -> when (house) {
                8 -> listOf(
                    StringResources.get(StringKeyGeneralPart4.EFFECT_JUP_WISDOM, language),
                    StringResources.get(StringKeyGeneralPart4.EFFECT_JUP_CHILD, language),
                    StringResources.get(StringKeyGeneralPart4.EFFECT_JUP_SPIRIT, language)
                )
                else -> listOf(StringResources.get(StringKeyGeneralPart4.EFFECT_JUP_GEN, language))
            }
            Planet.VENUS -> when (house) {
                6 -> listOf(
                    StringResources.get(StringKeyGeneralPart4.EFFECT_VENUS_MARR, language),
                    StringResources.get(StringKeyGeneralPart4.EFFECT_VENUS_REL, language),
                    StringResources.get(StringKeyGeneralPart4.EFFECT_VENUS_LUXURY, language)
                )
                else -> listOf(StringResources.get(StringKeyGeneralPart4.EFFECT_VENUS_GEN, language))
            }
            Planet.SATURN -> when (house) {
                1, 4, 7 -> listOf(
                    StringResources.get(StringKeyGeneralPart4.EFFECT_SAT_HEALTH, language),
                    StringResources.get(StringKeyGeneralPart4.EFFECT_SAT_HOME, language),
                    StringResources.get(StringKeyGeneralPart4.EFFECT_SAT_PARTNER, language)
                )
                else -> listOf(StringResources.get(StringKeyGeneralPart4.EFFECT_SAT_GEN, language))
            }
            Planet.RAHU -> when (house) {
                1, 7 -> listOf(
                    StringResources.get(StringKeyGeneralPart4.EFFECT_RAHU_ID, language),
                    StringResources.get(StringKeyGeneralPart4.EFFECT_RAHU_SUDDEN, language),
                    StringResources.get(StringKeyGeneralPart4.EFFECT_RAHU_FOREIGN, language)
                )
                else -> listOf(StringResources.get(StringKeyGeneralPart4.EFFECT_RAHU_GEN, language))
            }
            Planet.KETU -> when (house) {
                2, 5 -> listOf(
                    StringResources.get(StringKeyGeneralPart4.EFFECT_KETU_WEALTH, language),
                    StringResources.get(StringKeyGeneralPart4.EFFECT_KETU_CHILD, language),
                    StringResources.get(StringKeyGeneralPart4.EFFECT_KETU_KARMA, language)
                )
                else -> listOf(StringResources.get(StringKeyGeneralPart4.EFFECT_KETU_GEN, language))
            }
            else -> emptyList()
        }
    }

    /**
     * Analyze Karmic Debts (Rin) per Lal Kitab
     */
    private fun analyzeKarmicDebts(chart: VedicChart, language: Language): List<KarmicDebt> {
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
                    description = StringResources.get(StringKeyGeneralPart3.DEBT_PITRU_DESC, language),
                    indicators = listOf(
                        StringResources.get(StringKeyGeneralPart5.INDIC_SUN_HOUSE, language, getOrdinal(sunHouse ?: 1, language)),
                        if (jupiterHouse == 8) StringResources.get(StringKeyGeneralPart5.INDIC_JUP_8, language) else null
                    ).filterNotNull(),
                    effects = listOf(
                        StringResources.get(StringKeyGeneralPart3.DEBT_PITRU_EFF_1, language),
                        StringResources.get(StringKeyGeneralPart3.DEBT_PITRU_EFF_2, language),
                        StringResources.get(StringKeyGeneralPart3.DEBT_PITRU_EFF_3, language)
                    ),
                    remedies = listOf(
                        StringResources.get(StringKeyGeneralPart3.DEBT_PITRU_REM_1, language),
                        StringResources.get(StringKeyGeneralPart3.DEBT_PITRU_REM_2, language),
                        StringResources.get(StringKeyGeneralPart3.DEBT_PITRU_REM_3, language),
                        StringResources.get(StringKeyGeneralPart3.DEBT_PITRU_REM_4, language)
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
                    description = StringResources.get(StringKeyGeneralPart3.DEBT_MATRU_DESC, language),
                    indicators = listOf(StringResources.get(StringKeyGeneralPart5.INDIC_MOON_HOUSE, language, getOrdinal(moonHouse ?: 1, language))),
                    effects = listOf(
                        StringResources.get(StringKeyGeneralPart3.DEBT_MATRU_EFF_1, language),
                        StringResources.get(StringKeyGeneralPart3.DEBT_MATRU_EFF_2, language),
                        StringResources.get(StringKeyGeneralPart3.DEBT_MATRU_EFF_3, language)
                    ),
                    remedies = listOf(
                        StringResources.get(StringKeyGeneralPart3.DEBT_MATRU_REM_1, language),
                        StringResources.get(StringKeyGeneralPart3.DEBT_MATRU_REM_2, language),
                        StringResources.get(StringKeyGeneralPart3.DEBT_MATRU_REM_3, language),
                        StringResources.get(StringKeyGeneralPart3.DEBT_MATRU_REM_4, language)
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
                    description = StringResources.get(StringKeyGeneralPart3.DEBT_STRI_DESC, language),
                    indicators = listOf(StringResources.get(StringKeyGeneralPart5.INDIC_VENUS_HOUSE, language, getOrdinal(venusHouse ?: 1, language))),
                    effects = listOf(
                        StringResources.get(StringKeyGeneralPart3.DEBT_STRI_EFF_1, language),
                        StringResources.get(StringKeyGeneralPart3.DEBT_STRI_EFF_2, language),
                        StringResources.get(StringKeyGeneralPart3.DEBT_STRI_EFF_3, language)
                    ),
                    remedies = listOf(
                        StringResources.get(StringKeyGeneralPart3.DEBT_STRI_REM_1, language),
                        StringResources.get(StringKeyGeneralPart3.DEBT_STRI_REM_2, language),
                        StringResources.get(StringKeyGeneralPart3.DEBT_STRI_REM_3, language),
                        StringResources.get(StringKeyGeneralPart3.DEBT_STRI_REM_4, language)
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
                    description = StringResources.get(StringKeyGeneralPart3.DEBT_KANYA_DESC, language),
                    indicators = listOf(StringResources.get(StringKeyGeneralPart5.INDIC_MERC_HOUSE, language, getOrdinal(mercuryHouse ?: 1, language))),
                    effects = listOf(
                        StringResources.get(StringKeyGeneralPart3.DEBT_KANYA_EFF_1, language),
                        StringResources.get(StringKeyGeneralPart3.DEBT_KANYA_EFF_2, language),
                        StringResources.get(StringKeyGeneralPart3.DEBT_KANYA_EFF_3, language)
                    ),
                    remedies = listOf(
                        StringResources.get(StringKeyGeneralPart3.DEBT_KANYA_REM_1, language),
                        StringResources.get(StringKeyGeneralPart3.DEBT_KANYA_REM_2, language),
                        StringResources.get(StringKeyGeneralPart3.DEBT_KANYA_REM_3, language),
                        StringResources.get(StringKeyGeneralPart3.DEBT_KANYA_REM_4, language)
                    )
                )
            )
        }

        return debts
    }

    /**
     * Analyze houses per Lal Kitab methodology
     */
    private fun analyzeHousesLalKitab(chart: VedicChart, language: Language): List<LalKitabHouseAnalysis> {
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
                lalKitabSignificance = getHouseLalKitabSignificance(house, language),
                status = when {
                    planetsInHouse.isEmpty() -> HouseStatus.EMPTY
                    hasAffliction -> HouseStatus.AFFLICTED
                    planetsInHouse.any { it.planet in AstrologicalConstants.NATURAL_BENEFICS } ->
                        HouseStatus.BENEFIC
                    else -> HouseStatus.OCCUPIED
                },
                recommendations = getHouseLalKitabRecommendations(house, planetsInHouse, language)
            )
        }
    }

    private fun getHouseLalKitabSignificance(house: Int, language: Language): String {
        val key = when (house) {
            1 -> StringKeyGeneralPart10.SIG_1
            2 -> StringKeyGeneralPart10.SIG_2
            3 -> StringKeyGeneralPart10.SIG_3
            4 -> StringKeyGeneralPart10.SIG_4
            5 -> StringKeyGeneralPart10.SIG_5
            6 -> StringKeyGeneralPart10.SIG_6
            7 -> StringKeyGeneralPart10.SIG_7
            8 -> StringKeyGeneralPart10.SIG_8
            9 -> StringKeyGeneralPart10.SIG_9
            10 -> StringKeyGeneralPart10.SIG_10
            11 -> StringKeyGeneralPart10.SIG_11
            12 -> StringKeyGeneralPart10.SIG_12
            else -> StringKeyGeneralPart10.SIG_GEN
        }
        return StringResources.get(key, language)
    }

    private fun getHouseLalKitabRecommendations(house: Int, planets: List<PlanetPosition>, language: Language): List<String> {
        val recs = mutableListOf<String>()

        if (planets.isEmpty()) {
            val lordName = LAL_KITAB_HOUSE_LORDS[house]?.getLocalizedName(language) ?: ""
            recs.add(StringResources.get(StringKeyGeneralPart5.HOUSE_REC_STRENGTHEN, language, lordName, getOrdinal(house, language)))
        } else {
            planets.forEach { pos ->
                recs.addAll(getPlanetaryRemedies(pos.planet, house, language).take(2))
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
        houseAnalysis: List<LalKitabHouseAnalysis>,
        language: Language
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
                            method = getRemedyMethod(remedy, language),
                            frequency = getRemedyFrequency(affliction.planet, language),
                            duration = StringResources.get(StringKeyGeneralPart9.REM_DURATION_43, language),
                            effectiveness = if (affliction.severity == AfflictionSeverity.SEVERE)
                                StringResources.get(StringKeyGeneralPart9.REM_EFF_HIGH, language) else StringResources.get(StringKeyGeneralPart9.REM_EFF_MOD, language)
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
                        method = getRemedyMethod(remedy, language),
                        frequency = StringResources.get(StringKeyGeneralPart9.REM_FREQ_WEEKLY, language),
                        duration = StringResources.get(StringKeyGeneralPart9.REM_DURATION_LONG, language),
                        effectiveness = StringResources.get(StringKeyGeneralPart9.REM_EFF_ROOT, language)
                    )
                )
            }
        }

        return remedies.distinctBy { it.remedy }
    }

    private fun getPlanetaryRemedies(planet: Planet, house: Int, language: Language): List<String> {
        val baseRemedies = when (planet) {
            Planet.SUN -> listOf(
                StringResources.get(StringKeyGeneralPart9.REM_SUN_WATER, language),
                StringResources.get(StringKeyGeneralPart9.REM_SUN_FEED, language),
                StringResources.get(StringKeyGeneralPart9.REM_SUN_SILVER, language),
                StringResources.get(StringKeyGeneralPart9.REM_SUN_RUBY, language)
            )
            Planet.MOON -> listOf(
                StringResources.get(StringKeyGeneralPart9.REM_MOON_SHIVA, language),
                StringResources.get(StringKeyGeneralPart9.REM_MOON_SILVER, language),
                StringResources.get(StringKeyGeneralPart9.REM_MOON_MOTHER, language),
                StringResources.get(StringKeyGeneralPart9.REM_MOON_GLASS, language)
            )
            Planet.MARS -> listOf(
                StringResources.get(StringKeyGeneralPart9.REM_MARS_HANUMAN, language),
                StringResources.get(StringKeyGeneralPart9.REM_MARS_FEED, language),
                StringResources.get(StringKeyGeneralPart9.REM_MARS_DEER, language),
                StringResources.get(StringKeyGeneralPart9.REM_MARS_DONATE, language)
            )
            Planet.MERCURY -> listOf(
                StringResources.get(StringKeyGeneralPart9.REM_MERC_FEED, language),
                StringResources.get(StringKeyGeneralPart9.REM_MERC_BURY, language),
                StringResources.get(StringKeyGeneralPart9.REM_MERC_DONATE, language),
                StringResources.get(StringKeyGeneralPart9.REM_MERC_PARROT, language)
            )
            Planet.JUPITER -> listOf(
                StringResources.get(StringKeyGeneralPart9.REM_JUP_TILAK, language),
                StringResources.get(StringKeyGeneralPart9.REM_JUP_PEEPAL, language),
                StringResources.get(StringKeyGeneralPart9.REM_JUP_DONATE, language),
                StringResources.get(StringKeyGeneralPart9.REM_JUP_BRAHMIN, language)
            )
            Planet.VENUS -> listOf(
                StringResources.get(StringKeyGeneralPart9.REM_VENUS_DONATE, language),
                StringResources.get(StringKeyGeneralPart9.REM_VENUS_FEED, language),
                StringResources.get(StringKeyGeneralPart9.REM_VENUS_SILVER, language),
                StringResources.get(StringKeyGeneralPart9.REM_VENUS_WOMEN, language)
            )
            Planet.SATURN -> listOf(
                StringResources.get(StringKeyGeneralPart9.REM_SAT_DONATE, language),
                StringResources.get(StringKeyGeneralPart9.REM_SAT_FEED, language),
                StringResources.get(StringKeyGeneralPart9.REM_SAT_SERVE, language),
                StringResources.get(StringKeyGeneralPart9.REM_SAT_OIL, language)
            )
            Planet.RAHU -> listOf(
                StringResources.get(StringKeyGeneralPart9.REM_RAHU_DONATE, language),
                StringResources.get(StringKeyGeneralPart9.REM_RAHU_BARLEY, language),
                StringResources.get(StringKeyGeneralPart9.REM_RAHU_COCONUT, language),
                StringResources.get(StringKeyGeneralPart9.REM_RAHU_DOGS, language)
            )
            Planet.KETU -> listOf(
                StringResources.get(StringKeyGeneralPart9.REM_KETU_DONATE, language),
                StringResources.get(StringKeyGeneralPart9.REM_KETU_DOGS, language),
                StringResources.get(StringKeyGeneralPart9.REM_KETU_CLOTHES, language),
                StringResources.get(StringKeyGeneralPart9.REM_KETU_SESAME, language)
            )
            else -> emptyList()
        }

        // Add house-specific remedies
        val houseRemedies = when (house) {
            6, 8, 12 -> listOf(
                StringResources.get(StringKeyGeneralPart9.REM_HOUSE_CHARITY, language, getOrdinal(house, language)),
                StringResources.get(StringKeyGeneralPart9.REM_HOUSE_PEACE, language)
            )
            else -> emptyList()
        }

        return (baseRemedies + houseRemedies).distinct()
    }

    private fun getRemedyMethod(remedy: String, language: Language): String {
        return when {
            remedy.contains("feed", ignoreCase = true) || remedy.contains("भोजन", ignoreCase = true) || remedy.contains("खुवाउनु", ignoreCase = true) -> StringResources.get(StringKeyGeneralPart7.METHOD_FEEDING, language)
            remedy.contains("donate", ignoreCase = true) || remedy.contains("दान", ignoreCase = true) -> StringResources.get(StringKeyGeneralPart7.METHOD_CHARITY, language)
            remedy.contains("offer", ignoreCase = true) || remedy.contains("चढाउनु", ignoreCase = true) || remedy.contains("अर्पण", ignoreCase = true) -> StringResources.get(StringKeyGeneralPart7.METHOD_OFFERING, language)
            remedy.contains("keep", ignoreCase = true) || remedy.contains("राख्नु", ignoreCase = true) -> StringResources.get(StringKeyGeneralPart7.METHOD_PROTECTIVE, language)
            remedy.contains("recite", ignoreCase = true) || remedy.contains("पाठ", ignoreCase = true) || remedy.contains("जप", ignoreCase = true) -> StringResources.get(StringKeyGeneralPart7.METHOD_MANTRA, language)
            remedy.contains("float", ignoreCase = true) || remedy.contains("बगाउनु", ignoreCase = true) -> StringResources.get(StringKeyGeneralPart7.METHOD_WATER, language)
            remedy.contains("bury", ignoreCase = true) || remedy.contains("गाड्नु", ignoreCase = true) -> StringResources.get(StringKeyGeneralPart7.METHOD_EARTH, language)
            else -> StringResources.get(StringKeyGeneralPart7.METHOD_GENERAL, language)
        }
    }

    private fun getRemedyFrequency(planet: Planet, language: Language): String {
        return when (planet) {
            Planet.SUN -> StringResources.get(StringKeyGeneralPart4.FREQ_SUN, language)
            Planet.MOON -> StringResources.get(StringKeyGeneralPart4.FREQ_MOON, language)
            Planet.MARS -> StringResources.get(StringKeyGeneralPart4.FREQ_MARS, language)
            Planet.MERCURY -> StringResources.get(StringKeyGeneralPart4.FREQ_MERC, language)
            Planet.JUPITER -> StringResources.get(StringKeyGeneralPart4.FREQ_JUP, language)
            Planet.VENUS -> StringResources.get(StringKeyGeneralPart4.FREQ_VENUS, language)
            Planet.SATURN -> StringResources.get(StringKeyGeneralPart4.FREQ_SAT, language)
            Planet.RAHU -> StringResources.get(StringKeyGeneralPart4.FREQ_RAHU, language)
            Planet.KETU -> StringResources.get(StringKeyGeneralPart4.FREQ_KETU, language)
            else -> StringResources.get(StringKeyGeneralPart4.FREQ_GEN, language)
        }
    }

    /**
     * Generate color remedies
     */
    private fun generateColorRemedies(afflictions: List<PlanetaryAffliction>, language: Language): List<ColorRemedy> {
        val colorRemedies = mutableListOf<ColorRemedy>()

        val affectedPlanets = afflictions.map { it.planet }.distinct()

        affectedPlanets.forEach { planet ->
            val colorData = getPlanetColor(planet, language)
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

    private fun getPlanetColor(planet: Planet, language: Language): ColorData {
        return try {
            val colorsKey = StringKeyRemedy.valueOf("COLOR_${planet.name}_USE")
            val avoidKey = StringKeyRemedy.valueOf("COLOR_${planet.name}_AVOID")
            val appKey = StringKeyRemedy.valueOf("COLOR_APP_${planet.name}")
            
            ColorData(
                favorable = StringResources.get(colorsKey, language).split(",").map { it.trim() },
                avoid = StringResources.get(avoidKey, language).split(",").map { it.trim() },
                application = StringResources.get(appKey, language)
            )
        } catch (e: Exception) {
            ColorData(listOf("General"), listOf("None"), "Follow general color guidelines")
        }
    }

    /**
     * Generate direction remedies
     */
    private fun generateDirectionRemedies(afflictions: List<PlanetaryAffliction>, language: Language): List<DirectionRemedy> {
        val directionRemedies = mutableListOf<DirectionRemedy>()

        afflictions.forEach { affliction ->
            val dirData = getPlanetDirection(affliction.planet, language)
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

    private fun getPlanetDirection(planet: Planet, language: Language): DirectionData {
        return try {
            val (favKeyAnal, avoidKeyAnal) = when(planet) {
                Planet.SUN -> StringKeyPrashnaPart1.PRASHNA_DIR_EAST to StringKeyPrashnaPart1.PRASHNA_DIR_WEST
                Planet.MOON -> StringKeyPrashnaPart1.PRASHNA_DIR_NORTH_WEST to StringKeyPrashnaPart1.PRASHNA_DIR_SOUTH
                Planet.MARS -> StringKeyPrashnaPart1.PRASHNA_DIR_SOUTH to StringKeyPrashnaPart1.PRASHNA_DIR_NORTH
                Planet.MERCURY -> StringKeyPrashnaPart1.PRASHNA_DIR_NORTH to StringKeyPrashnaPart1.PRASHNA_DIR_SOUTH
                Planet.JUPITER -> StringKeyPrashnaPart1.PRASHNA_DIR_NORTH_EAST to StringKeyPrashnaPart1.PRASHNA_DIR_SOUTH_WEST
                Planet.VENUS -> StringKeyPrashnaPart1.PRASHNA_DIR_SOUTH_EAST to StringKeyPrashnaPart1.PRASHNA_DIR_NORTH_WEST
                Planet.SATURN -> StringKeyPrashnaPart1.PRASHNA_DIR_WEST to StringKeyPrashnaPart1.PRASHNA_DIR_EAST
                Planet.RAHU -> StringKeyPrashnaPart1.PRASHNA_DIR_SOUTH_WEST to StringKeyPrashnaPart1.PRASHNA_DIR_NORTH_EAST
                Planet.KETU -> StringKeyPrashnaPart1.PRASHNA_DIR_NORTH_WEST to StringKeyPrashnaPart1.PRASHNA_DIR_SOUTH_EAST
                else -> StringKeyPrashnaPart1.PRASHNA_DIR_EAST to StringKeyPrashnaPart1.PRASHNA_DIR_WEST
            }
            
            val appKey = StringKeyLalKitab.valueOf("DIR_APP_${planet.name}")

            DirectionData(
                favorable = StringResources.get(favKeyAnal, language),
                avoid = StringResources.get(avoidKeyAnal, language),
                application = StringResources.get(appKey, language)
            )
        } catch (e: Exception) {
            DirectionData("General", "None", "Follow general direction guidelines")
        }
    }

    /**
     * Generate annual remedy calendar
     */
    private fun generateAnnualRemedyCalendar(language: Language): List<AnnualRemedyEntry> {
        val weekDays = listOf(
            Planet.SUN to StringResources.get(StringKeyPanchanga.WEEKDAY_SUNDAY, language),
            Planet.MOON to StringResources.get(StringKeyPanchanga.WEEKDAY_MONDAY, language),
            Planet.MARS to StringResources.get(StringKeyPanchanga.WEEKDAY_TUESDAY, language),
            Planet.MERCURY to StringResources.get(StringKeyPanchanga.WEEKDAY_WEDNESDAY, language),
            Planet.JUPITER to StringResources.get(StringKeyPanchanga.WEEKDAY_THURSDAY, language),
            Planet.VENUS to StringResources.get(StringKeyPanchanga.WEEKDAY_FRIDAY, language),
            Planet.SATURN to StringResources.get(StringKeyPanchanga.WEEKDAY_SATURDAY, language)
        )

        return weekDays.map { (planet, day) ->
            AnnualRemedyEntry(
                day = day,
                planet = planet,
                remedies = getPlanetaryRemedies(planet, 1, language).take(3) // house 1 as placeholder for general daily recs
            )
        }
    }

    private fun generateGeneralRecommendations(chart: VedicChart, language: Language): List<String> {
        return listOf(
            StringResources.get(StringKeyGeneralPart4.GEN_REC_1, language),
            StringResources.get(StringKeyGeneralPart4.GEN_REC_2, language),
            StringResources.get(StringKeyGeneralPart4.GEN_REC_3, language),
            StringResources.get(StringKeyGeneralPart4.GEN_REC_4, language),
            StringResources.get(StringKeyGeneralPart4.GEN_REC_5, language),
            StringResources.get(StringKeyGeneralPart4.GEN_REC_6, language),
            StringResources.get(StringKeyGeneralPart4.GEN_REC_7, language)
        )
    }

    private fun getOrdinal(number: Int, language: Language): String {
        return when (number) {
            1 -> "1st" // I'll use simple formatting for ordinals or add keys if needed. 
            // In Nepali, we use "औं" or "म". 
            // I'll skip localizing "st", "nd" for now as it's secondary to the message content.
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


