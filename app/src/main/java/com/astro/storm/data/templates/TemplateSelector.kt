package com.astro.storm.data.templates

import com.astro.storm.core.common.Language
import com.astro.storm.core.model.Planet
import com.astro.storm.core.model.ZodiacSign
import com.astro.storm.core.model.PlanetPosition
import com.astro.storm.core.model.VedicChart

/**
 * Rule-based template selection engine for AstroStorm.
 *
 * This engine selects the most appropriate prediction templates based on
 * astrological conditions in the birth chart. Selection is entirely rule-based
 * (no AI required) and works fully offline.
 *
 * Selection Algorithm:
 * 1. Filter by planet
 * 2. Filter by sign
 * 3. Filter by degree range (0-10, 10-20, 20-30 for precision)
 * 4. Filter by house
 * 5. Filter by dignity
 * 6. Filter by retrograde/combust status
 * 7. Score matches by specificity
 * 8. Return highest-priority matches
 *
 * Based on classical Vedic astrology text methodologies from BPHS, Phaladeepika, Saravali.
 */
object TemplateSelector {

    // ============================================
    // DASHA TEMPLATE SELECTION
    // ============================================

    /**
     * Select Dasha effect templates for a planet in a specific chart context.
     *
     * @param dashaLord The Mahadasha lord planet
     * @param antardashaLord Optional Antardasha lord for sub-period specificity
     * @param chart The birth chart for context (house, dignity, degree)
     * @return List of matching templates sorted by priority (highest first)
     */
    fun selectDashaTemplates(
        dashaLord: Planet,
        antardashaLord: Planet? = null,
        chart: VedicChart
    ): List<PredictionTemplate> {
        val position = chart.planetPositions[dashaLord] ?: return emptyList()
        val sign = ZodiacSign.fromLongitude(position.longitude)
        val degreeInSign = position.longitude % 30.0
        val house = getHouseForPlanet(dashaLord, chart)

        return TemplateDatabase.dashaTemplates.filter { template ->
            matchesConditions(template.conditions, dashaLord, sign, degreeInSign, house, position, antardashaLord)
        }.sortedByDescending { it.priority }
    }

    /**
     * Select the best single Dasha template for a planet.
     */
    fun selectBestDashaTemplate(
        dashaLord: Planet,
        antardashaLord: Planet? = null,
        chart: VedicChart
    ): PredictionTemplate? {
        return selectDashaTemplates(dashaLord, antardashaLord, chart).firstOrNull()
    }

    // ============================================
    // TRANSIT TEMPLATE SELECTION
    // ============================================

    /**
     * Select transit effect templates for a transiting planet.
     *
     * @param transitPlanet The transiting planet
     * @param transitHouse House number from natal Moon (1-12)
     * @param natalChart The birth chart
     * @return Matching transit templates sorted by priority
     */
    fun selectTransitTemplates(
        transitPlanet: Planet,
        transitHouse: Int,
        natalChart: VedicChart
    ): List<PredictionTemplate> {
        return TemplateDatabase.transitTemplates.filter { template ->
            val c = template.conditions
            (c.transitPlanet == null || c.transitPlanet == transitPlanet) &&
            (c.transitHouse == null || c.transitHouse == transitHouse) &&
            (c.planet == null || c.planet == transitPlanet)
        }.sortedByDescending { it.priority }
    }

    /**
     * Select the best single transit template.
     */
    fun selectBestTransitTemplate(
        transitPlanet: Planet,
        transitHouse: Int,
        natalChart: VedicChart
    ): PredictionTemplate? {
        return selectTransitTemplates(transitPlanet, transitHouse, natalChart).firstOrNull()
    }

    // ============================================
    // YOGA TEMPLATE SELECTION
    // ============================================

    /**
     * Select yoga effect templates for a specific yoga type.
     *
     * @param yogaType The yoga identifier (e.g., "RAJA_YOGA", "GAJAKESARI")
     * @param strength Optional strength level (0.0-1.0)
     * @return Matching yoga templates
     */
    fun selectYogaTemplates(
        yogaType: String,
        strength: Double? = null
    ): List<PredictionTemplate> {
        return TemplateDatabase.yogaTemplates.filter { template ->
            val c = template.conditions
            (c.yogaType == null || c.yogaType == yogaType) &&
            (strength == null || c.strengthMin == null || strength >= c.strengthMin) &&
            (strength == null || c.strengthMax == null || strength <= c.strengthMax)
        }.sortedByDescending { it.priority }
    }

    // ============================================
    // HOUSE LORD TEMPLATE SELECTION
    // ============================================

    /**
     * Select house lord templates for a lord placed in a specific house.
     *
     * @param lord The house lord planet
     * @param fromHouse The house owned (1-12)
     * @param inHouse The house the lord is placed in (1-12)
     * @param chart The birth chart
     * @return Matching house lord templates
     */
    fun selectHouseLordTemplates(
        lord: Planet,
        fromHouse: Int,
        inHouse: Int,
        chart: VedicChart
    ): List<PredictionTemplate> {
        val position = chart.planetPositions[lord]
        val sign = if (position != null) ZodiacSign.fromLongitude(position.longitude) else null
        val dignity = if (sign != null) getDignity(lord, sign) else null

        return TemplateDatabase.houseLordTemplates.filter { template ->
            val c = template.conditions
            (c.planet == null || c.planet == lord) &&
            (c.house == null || c.house == fromHouse) &&
            (c.transitHouse == null || c.transitHouse == inHouse) &&
            (c.dignity == null || c.dignity == dignity)
        }.sortedByDescending { it.priority }
    }

    // ============================================
    // NADI TEMPLATE SELECTION
    // ============================================

    /**
     * Select Nadi prediction templates for a specific Nadi number and ascendant.
     *
     * @param nadiNumber The Nadi division number (1-150)
     * @param ascendant The ascendant sign
     * @return Matching Nadi prediction template
     */
    fun selectNadiTemplate(
        nadiNumber: Int,
        ascendant: ZodiacSign
    ): PredictionTemplate? {
        return TemplateDatabase.nadiTemplates.find { template ->
            val c = template.conditions
            c.nadiNumber == nadiNumber && c.ascendantSign == ascendant
        }
    }

    /**
     * Select all Nadi templates for a specific Nadi number (all ascendants).
     */
    fun selectNadiTemplatesByNumber(nadiNumber: Int): List<PredictionTemplate> {
        return TemplateDatabase.nadiTemplates.filter { it.conditions.nadiNumber == nadiNumber }
    }

    // ============================================
    // DIVISIONAL CHART TEMPLATE SELECTION
    // ============================================

    /**
     * Select divisional chart templates for a specific varga and planet.
     *
     * @param vargaNumber The divisional chart number (1-60)
     * @param planet Optional planet for planet-specific analysis
     * @param sign Optional sign position in the varga
     * @return Matching templates
     */
    fun selectDivisionalTemplates(
        vargaNumber: Int,
        planet: Planet? = null,
        sign: ZodiacSign? = null
    ): List<PredictionTemplate> {
        return TemplateDatabase.divisionalTemplates.filter { template ->
            val c = template.conditions
            (c.vargaNumber == null || c.vargaNumber == vargaNumber) &&
            (planet == null || c.planet == null || c.planet == planet) &&
            (sign == null || c.sign == null || c.sign == sign)
        }.sortedByDescending { it.priority }
    }

    // ============================================
    // LIFE AREA TEMPLATE SELECTION
    // ============================================

    /**
     * Select life area prediction templates.
     *
     * @param lifeArea The life area (career, health, relationship, etc.)
     * @param planet Optional planet influencing the area
     * @param house Optional house number
     * @param chart The birth chart
     * @return Matching templates
     */
    fun selectLifeAreaTemplates(
        lifeArea: String,
        planet: Planet? = null,
        house: Int? = null,
        chart: VedicChart? = null
    ): List<PredictionTemplate> {
        return TemplateDatabase.lifeAreaTemplates.filter { template ->
            val c = template.conditions
            (c.lifeArea == null || c.lifeArea == lifeArea) &&
            (planet == null || c.planet == null || c.planet == planet) &&
            (house == null || c.house == null || c.house == house)
        }.sortedByDescending { it.priority }
    }

    // ============================================
    // COMPREHENSIVE PREDICTION
    // ============================================

    /**
     * Generate comprehensive predictions for a chart by selecting from all categories.
     *
     * @param chart The birth chart
     * @param language The display language
     * @param maxPerCategory Maximum templates per category
     * @return Map of category to selected templates
     */
    fun generateComprehensivePredictions(
        chart: VedicChart,
        language: Language = Language.ENGLISH,
        maxPerCategory: Int = 20
    ): Map<TemplateCategory, List<PredictionTemplate>> {
        val result = mutableMapOf<TemplateCategory, List<PredictionTemplate>>()

        // Dasha predictions for all main planets
        val dashaResults = Planet.MAIN_PLANETS.flatMap { planet ->
            selectDashaTemplates(planet, null, chart).take(3)
        }.take(maxPerCategory)
        result[TemplateCategory.DASHA] = dashaResults

        // Transit predictions for houses 1-12
        val transitResults = Planet.MAIN_PLANETS.flatMap { planet ->
            (1..12).mapNotNull { house ->
                selectBestTransitTemplate(planet, house, chart)
            }
        }.take(maxPerCategory)
        result[TemplateCategory.TRANSIT] = transitResults

        // Yoga templates
        result[TemplateCategory.YOGA] = TemplateDatabase.yogaTemplates.take(maxPerCategory)

        // House lord templates
        val houseLordResults = (1..12).flatMap { house ->
            val lord = getHouseLord(house, chart)
            if (lord != null) {
                val lordHouse = getHouseForPlanet(lord, chart)
                selectHouseLordTemplates(lord, house, lordHouse, chart).take(2)
            } else emptyList()
        }.take(maxPerCategory)
        result[TemplateCategory.HOUSE_LORD] = houseLordResults

        // Nadi templates based on ascendant
        val ascSign = ZodiacSign.fromLongitude(chart.ascendant)
        result[TemplateCategory.NADI] = TemplateDatabase.nadiTemplates
            .filter { it.conditions.ascendantSign == ascSign }
            .take(maxPerCategory)

        // Divisional chart templates
        result[TemplateCategory.DIVISIONAL] = TemplateDatabase.divisionalTemplates.take(maxPerCategory)

        // Life area templates
        result[TemplateCategory.LIFE_AREA] = TemplateDatabase.lifeAreaTemplates.take(maxPerCategory)

        return result
    }

    // ============================================
    // INTERNAL HELPERS
    // ============================================

    private fun matchesConditions(
        conditions: TemplateConditions,
        planet: Planet,
        sign: ZodiacSign,
        degreeInSign: Double,
        house: Int,
        position: PlanetPosition,
        antardashaLord: Planet?
    ): Boolean {
        return (conditions.planet == null || conditions.planet == planet) &&
               (conditions.sign == null || conditions.sign == sign) &&
               (conditions.degreeMin == null || degreeInSign >= conditions.degreeMin) &&
               (conditions.degreeMax == null || degreeInSign <= conditions.degreeMax) &&
               (conditions.house == null || conditions.house == house) &&
               (conditions.isRetrograde == null || conditions.isRetrograde == position.isRetrograde) &&
               (conditions.dignity == null || conditions.dignity == getDignity(planet, sign)) &&
               (conditions.antardashaLord == null || conditions.antardashaLord == antardashaLord) &&
               (conditions.dashaLord == null || conditions.dashaLord == planet)
    }

    /** Get the house number a planet occupies (1-12) */
    private fun getHouseForPlanet(planet: Planet, chart: VedicChart): Int {
        val position = chart.planetPositions[planet] ?: return 1
        val ascLong = chart.ascendant
        val planetLong = position.longitude
        val diff = ((planetLong - ascLong) % 360.0 + 360.0) % 360.0
        return ((diff / 30.0).toInt() + 1).coerceIn(1, 12)
    }

    /** Get house lord for a given house (Whole Sign system) */
    private fun getHouseLord(house: Int, chart: VedicChart): Planet? {
        val ascSign = ZodiacSign.fromLongitude(chart.ascendant)
        val houseSignNumber = ((ascSign.number - 1 + house - 1) % 12) + 1
        val houseSign = ZodiacSign.entries.find { it.number == houseSignNumber } ?: return null
        return houseSign.ruler
    }

    /** Determine planetary dignity based on sign placement */
    private fun getDignity(planet: Planet, sign: ZodiacSign): DignityCondition {
        // Exaltation signs per BPHS
        val exaltationMap = mapOf(
            Planet.SUN to ZodiacSign.ARIES,
            Planet.MOON to ZodiacSign.TAURUS,
            Planet.MARS to ZodiacSign.CAPRICORN,
            Planet.MERCURY to ZodiacSign.VIRGO,
            Planet.JUPITER to ZodiacSign.CANCER,
            Planet.VENUS to ZodiacSign.PISCES,
            Planet.SATURN to ZodiacSign.LIBRA,
            Planet.RAHU to ZodiacSign.TAURUS,
            Planet.KETU to ZodiacSign.SCORPIO
        )

        // Debilitation signs (opposite of exaltation)
        val debilitationMap = mapOf(
            Planet.SUN to ZodiacSign.LIBRA,
            Planet.MOON to ZodiacSign.SCORPIO,
            Planet.MARS to ZodiacSign.CANCER,
            Planet.MERCURY to ZodiacSign.PISCES,
            Planet.JUPITER to ZodiacSign.CAPRICORN,
            Planet.VENUS to ZodiacSign.VIRGO,
            Planet.SATURN to ZodiacSign.ARIES,
            Planet.RAHU to ZodiacSign.SCORPIO,
            Planet.KETU to ZodiacSign.TAURUS
        )

        // Own signs per BPHS
        val ownSignMap = mapOf(
            Planet.SUN to listOf(ZodiacSign.LEO),
            Planet.MOON to listOf(ZodiacSign.CANCER),
            Planet.MARS to listOf(ZodiacSign.ARIES, ZodiacSign.SCORPIO),
            Planet.MERCURY to listOf(ZodiacSign.GEMINI, ZodiacSign.VIRGO),
            Planet.JUPITER to listOf(ZodiacSign.SAGITTARIUS, ZodiacSign.PISCES),
            Planet.VENUS to listOf(ZodiacSign.TAURUS, ZodiacSign.LIBRA),
            Planet.SATURN to listOf(ZodiacSign.CAPRICORN, ZodiacSign.AQUARIUS),
            Planet.RAHU to listOf(ZodiacSign.AQUARIUS),
            Planet.KETU to listOf(ZodiacSign.SCORPIO)
        )

        // Moolatrikona signs per BPHS
        val moolatrikonaMap = mapOf(
            Planet.SUN to ZodiacSign.LEO,
            Planet.MOON to ZodiacSign.TAURUS,
            Planet.MARS to ZodiacSign.ARIES,
            Planet.MERCURY to ZodiacSign.VIRGO,
            Planet.JUPITER to ZodiacSign.SAGITTARIUS,
            Planet.VENUS to ZodiacSign.LIBRA,
            Planet.SATURN to ZodiacSign.AQUARIUS
        )

        // Friendly signs
        val friendlySignMap = mapOf(
            Planet.SUN to listOf(ZodiacSign.ARIES, ZodiacSign.SCORPIO, ZodiacSign.SAGITTARIUS, ZodiacSign.PISCES, ZodiacSign.CANCER),
            Planet.MOON to listOf(ZodiacSign.GEMINI, ZodiacSign.VIRGO, ZodiacSign.LEO),
            Planet.MARS to listOf(ZodiacSign.LEO, ZodiacSign.CANCER, ZodiacSign.SAGITTARIUS, ZodiacSign.PISCES),
            Planet.MERCURY to listOf(ZodiacSign.TAURUS, ZodiacSign.LIBRA, ZodiacSign.LEO),
            Planet.JUPITER to listOf(ZodiacSign.ARIES, ZodiacSign.SCORPIO, ZodiacSign.LEO),
            Planet.VENUS to listOf(ZodiacSign.GEMINI, ZodiacSign.VIRGO, ZodiacSign.CAPRICORN, ZodiacSign.AQUARIUS),
            Planet.SATURN to listOf(ZodiacSign.TAURUS, ZodiacSign.LIBRA, ZodiacSign.GEMINI, ZodiacSign.VIRGO)
        )

        return when {
            exaltationMap[planet] == sign -> DignityCondition.EXALTED
            debilitationMap[planet] == sign -> DignityCondition.DEBILITATED
            moolatrikonaMap[planet] == sign -> DignityCondition.MOOLATRIKONA
            ownSignMap[planet]?.contains(sign) == true -> DignityCondition.OWN_SIGN
            friendlySignMap[planet]?.contains(sign) == true -> DignityCondition.FRIENDLY
            else -> DignityCondition.NEUTRAL
        }
    }
}
