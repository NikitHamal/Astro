package com.astro.storm.ephemeris.deepanalysis

import android.content.Context
import com.astro.storm.core.model.Planet
import com.astro.storm.core.model.PlanetPosition
import com.astro.storm.core.model.VedicChart
import com.astro.storm.core.model.ZodiacSign
import com.astro.storm.ephemeris.*
import com.astro.storm.ephemeris.yoga.*

/**
 * Analysis Context - Centralized calculator integration for deep analysis
 * 
 * This class provides cached access to all astrological calculations
 * from existing calculators. It serves as the integration point between
 * the deep analysis engine and the existing calculation infrastructure.
 * 
 * All calculations are performed once and cached for reuse across
 * different analysis modules.
 * 
 * @author AstroStorm Deep Analysis Engine
 */
class AnalysisContext private constructor(
    val chart: VedicChart,
    private val androidContext: Context
) {
    
    // ═══════════════════════════════════════════════════════════════════════════════
    // CACHED CALCULATIONS
    // ═══════════════════════════════════════════════════════════════════════════════
    
    // House Lords Cache
    private val houseLordsCache = mutableMapOf<Int, Planet>()
    
    // Planet in House Cache
    private val planetsInHouseCache = mutableMapOf<Int, List<PlanetPosition>>()
    
    // Shadbala Cache
    val shadbalaAnalysis: ShadbalaAnalysis by lazy {
        ShadbalaCalculator.calculateShadbala(androidContext, chart)
    }
    
    // Yoga Analysis Cache
    val yogaAnalysis: YogaAnalysis by lazy {
        YogaCalculator.analyzeAllYogas(chart)
    }
    
    // Raja Yoga Analysis
    val rajaYogas: List<Yoga> by lazy {
        RajaYogaEvaluator.evaluate(chart)
    }
    
    // Dhana Yoga Analysis
    val dhanaYogas: List<Yoga> by lazy {
        DhanaYogaEvaluator.evaluate(chart)
    }
    
    // Dasha Timeline
    val dashaTimeline: DashaCalculator.DashaTimeline by lazy {
        DashaCalculator.calculateDashaTimeline(chart)
    }
    
    // Current Dasha
    val currentMahadasha: DashaCalculator.Mahadasha? by lazy {
        dashaTimeline.mahadashas.find { it.isActiveOn(java.time.LocalDateTime.now()) }
    }
    
    val currentAntardasha: DashaCalculator.Antardasha? by lazy {
        currentMahadasha?.getActiveAntardasha()
    }
    
    // Ashtakavarga Analysis
    val ashtakavargaAnalysis: AshtakavargaCalculator.AshtakavargaAnalysis by lazy {
        AshtakavargaCalculator.calculateAshtakavarga(chart)
    }
    
    // Divisional Charts Cache
    private val divisionalChartsCache = mutableMapOf<Int, VedicChart>()
    
    // Avastha Analysis
    val avasthaAnalysis: Map<Planet, AvasthaCalculator.PlanetaryAvastha> by lazy {
        chart.planetPositions
            .filter { !it.planet.isShadowPlanet }
            .associate { it.planet to AvasthaCalculator.calculateAvastha(it, chart) }
    }
    
    // Ascendant Information
    val ascendantSign: ZodiacSign by lazy {
        VedicAstrologyUtils.getAscendantSign(chart)
    }
    
    val ascendantLord: Planet by lazy {
        ascendantSign.ruler
    }
    
    val ascendantLordPosition: PlanetPosition? by lazy {
        chart.planetPositions.find { it.planet == ascendantLord }
    }
    
    // Moon Information
    val moonPosition: PlanetPosition? by lazy {
        chart.planetPositions.find { it.planet == Planet.MOON }
    }
    
    val moonSign: ZodiacSign by lazy {
        moonPosition?.sign ?: ascendantSign
    }
    
    // Sun Information
    val sunPosition: PlanetPosition? by lazy {
        chart.planetPositions.find { it.planet == Planet.SUN }
    }
    
    // Atmakaraka (Soul Significator)
    val atmakaraka: Planet by lazy {
        calculateAtmakaraka()
    }
    
    // Ketu Position
    val ketuPosition: PlanetPosition? by lazy {
        chart.planetPositions.find { it.planet == Planet.KETU }
    }
    
    // Rahu Position
    val rahuPosition: PlanetPosition? by lazy {
        chart.planetPositions.find { it.planet == Planet.RAHU }
    }
    
    // ═══════════════════════════════════════════════════════════════════════════════
    // HELPER METHODS
    // ═══════════════════════════════════════════════════════════════════════════════
    
    /**
     * Get the lord of a specific house
     */
    fun getHouseLord(house: Int): Planet {
        return houseLordsCache.getOrPut(house) {
            VedicAstrologyUtils.getHouseLord(chart, house)
        }
    }
    
    /**
     * Get all planets in a specific house
     */
    fun getPlanetsInHouse(house: Int): List<PlanetPosition> {
        return planetsInHouseCache.getOrPut(house) {
            VedicAstrologyUtils.getPlanetsInHouse(chart, house)
        }
    }
    
    /**
     * Get house number for a planet
     */
    fun getPlanetHouse(planet: Planet): Int {
        return chart.planetPositions.find { it.planet == planet }?.house ?: 1
    }
    
    /**
     * Get planet position
     */
    fun getPlanetPosition(planet: Planet): PlanetPosition? {
        return chart.planetPositions.find { it.planet == planet }
    }
    
    /**
     * Check if planet is exalted
     */
    fun isExalted(planet: Planet): Boolean {
        val position = getPlanetPosition(planet) ?: return false
        return VedicAstrologyUtils.isExalted(position)
    }
    
    /**
     * Check if planet is debilitated
     */
    fun isDebilitated(planet: Planet): Boolean {
        val position = getPlanetPosition(planet) ?: return false
        return VedicAstrologyUtils.isDebilitated(position)
    }
    
    /**
     * Check if planet is in own sign
     */
    fun isInOwnSign(planet: Planet): Boolean {
        val position = getPlanetPosition(planet) ?: return false
        return VedicAstrologyUtils.isInOwnSign(position)
    }
    
    /**
     * Check if planet is retrograde
     */
    fun isRetrograde(planet: Planet): Boolean {
        return getPlanetPosition(planet)?.isRetrograde == true
    }
    
    /**
     * Check if planet is combust
     */
    fun isCombust(planet: Planet): Boolean {
        return getPlanetPosition(planet)?.isCombust == true
    }
    
    /**
     * Get planetary dignity level
     */
    fun getDignity(planet: Planet): PlanetaryDignityLevel {
        val position = getPlanetPosition(planet) ?: return PlanetaryDignityLevel.NEUTRAL
        return when {
            VedicAstrologyUtils.isExalted(position) -> PlanetaryDignityLevel.EXALTED
            VedicAstrologyUtils.isInMoolatrikona(position) -> PlanetaryDignityLevel.MOOLATRIKONA
            VedicAstrologyUtils.isInOwnSign(position) -> PlanetaryDignityLevel.OWN_SIGN
            VedicAstrologyUtils.isInFriendSign(position) -> PlanetaryDignityLevel.FRIEND_SIGN
            VedicAstrologyUtils.isDebilitated(position) -> PlanetaryDignityLevel.DEBILITATED
            VedicAstrologyUtils.isInEnemySign(position) -> PlanetaryDignityLevel.ENEMY_SIGN
            else -> PlanetaryDignityLevel.NEUTRAL
        }
    }
    
    /**
     * Get Shadbala strength level for a planet
     */
    fun getPlanetStrengthLevel(planet: Planet): StrengthLevel {
        val shadbala = shadbalaAnalysis.planetaryBala[planet]
            ?: return StrengthLevel.MODERATE
        
        return when {
            shadbala.percentageStrength >= 120 -> StrengthLevel.EXCELLENT
            shadbala.percentageStrength >= 100 -> StrengthLevel.STRONG
            shadbala.percentageStrength >= 80 -> StrengthLevel.MODERATE
            shadbala.percentageStrength >= 60 -> StrengthLevel.WEAK
            else -> StrengthLevel.AFFLICTED
        }
    }
    
    /**
     * Get house strength based on planets and lords
     */
    fun getHouseStrength(house: Int): StrengthLevel {
        val lord = getHouseLord(house)
        val lordStrength = getPlanetStrengthLevel(lord)
        val planetsInHouse = getPlanetsInHouse(house)
        
        // Calculate house strength based on lord and occupants
        var strengthScore = lordStrength.value.toDouble()
        
        planetsInHouse.forEach { position ->
            val beneficBonus = if (VedicAstrologyUtils.isNaturalBenefic(position.planet)) 0.5 else -0.3
            strengthScore += beneficBonus
        }
        
        return when {
            strengthScore >= 4.5 -> StrengthLevel.EXCELLENT
            strengthScore >= 3.5 -> StrengthLevel.STRONG
            strengthScore >= 2.5 -> StrengthLevel.MODERATE
            strengthScore >= 1.5 -> StrengthLevel.WEAK
            else -> StrengthLevel.AFFLICTED
        }
    }
    
    /**
     * Get Navamsha (D9) chart
     */
    fun getNavamshaChart(): VedicChart {
        return divisionalChartsCache.getOrPut(9) {
            DivisionalChartCalculator.calculateDivisionalChart(chart, 9)
        }
    }
    
    /**
     * Get Dashamsha (D10) chart
     */
    fun getDashamsha(): VedicChart {
        return divisionalChartsCache.getOrPut(10) {
            DivisionalChartCalculator.calculateDivisionalChart(chart, 10)
        }
    }
    
    /**
     * Get any divisional chart
     */
    fun getDivisionalChart(division: Int): VedicChart {
        return divisionalChartsCache.getOrPut(division) {
            DivisionalChartCalculator.calculateDivisionalChart(chart, division)
        }
    }
    
    /**
     * Calculate Atmakaraka (planet with highest degree)
     */
    private fun calculateAtmakaraka(): Planet {
        // Exclude Rahu/Ketu from Atmakaraka calculation
        val eligiblePlanets = chart.planetPositions.filter { 
            it.planet != Planet.RAHU && it.planet != Planet.KETU 
        }
        
        return eligiblePlanets.maxByOrNull { it.longitude % 30 }?.planet ?: Planet.SUN
    }
    
    /**
     * Get dominant element in chart
     */
    fun getDominantElement(): Element {
        val elementCounts = mutableMapOf<Element, Int>()
        
        chart.planetPositions.forEach { position ->
            val element = getSignElement(position.sign)
            elementCounts[element] = (elementCounts[element] ?: 0) + 1
        }
        
        return elementCounts.maxByOrNull { it.value }?.key ?: Element.FIRE
    }
    
    /**
     * Get dominant modality in chart
     */
    fun getDominantModality(): Modality {
        val modalityCounts = mutableMapOf<Modality, Int>()
        
        chart.planetPositions.forEach { position ->
            val modality = getSignModality(position.sign)
            modalityCounts[modality] = (modalityCounts[modality] ?: 0) + 1
        }
        
        return modalityCounts.maxByOrNull { it.value }?.key ?: Modality.CARDINAL
    }
    
    /**
     * Check if a yoga type is present
     */
    fun hasYogaType(yogaName: String): Boolean {
        return yogaAnalysis.allYogas.any { 
            it.name.contains(yogaName, ignoreCase = true) 
        }
    }
    
    /**
     * Get yoga by name
     */
    fun getYoga(yogaName: String): Yoga? {
        return yogaAnalysis.allYogas.find { 
            it.name.equals(yogaName, ignoreCase = true) 
        }
    }
    
    /**
     * Get all yogas affecting a specific life area
     */
    fun getYogasForCategory(category: YogaCategory): List<Yoga> {
        return yogaAnalysis.allYogas.filter { it.category == category }
    }
    
    /**
     * Check for Kemadruma Yoga (Moon without benefic support)
     */
    fun hasKemadrumaYoga(): Boolean {
        return hasYogaType("Kemadruma")
    }
    
    /**
     * Get element for a zodiac sign
     */
    private fun getSignElement(sign: ZodiacSign): Element {
        return when (sign) {
            ZodiacSign.ARIES, ZodiacSign.LEO, ZodiacSign.SAGITTARIUS -> Element.FIRE
            ZodiacSign.TAURUS, ZodiacSign.VIRGO, ZodiacSign.CAPRICORN -> Element.EARTH
            ZodiacSign.GEMINI, ZodiacSign.LIBRA, ZodiacSign.AQUARIUS -> Element.AIR
            else -> Element.WATER
        }
    }
    
    /**
     * Get modality for a zodiac sign
     */
    private fun getSignModality(sign: ZodiacSign): Modality {
        return when (sign) {
            ZodiacSign.ARIES, ZodiacSign.CANCER, ZodiacSign.LIBRA, ZodiacSign.CAPRICORN -> 
                Modality.CARDINAL
            ZodiacSign.TAURUS, ZodiacSign.LEO, ZodiacSign.SCORPIO, ZodiacSign.AQUARIUS -> 
                Modality.FIXED
            else -> Modality.MUTABLE
        }
    }
    
    /**
     * Get element balance across chart
     */
    fun getElementBalance(): Map<Element, Double> {
        val counts = mutableMapOf<Element, Int>()
        chart.planetPositions.forEach { position ->
            val element = getSignElement(position.sign)
            counts[element] = (counts[element] ?: 0) + 1
        }
        val total = counts.values.sum().toDouble().coerceAtLeast(1.0)
        return counts.mapValues { (it.value / total) * 100 }
    }
    
    /**
     * Get modality balance across chart
     */
    fun getModalityBalance(): Map<Modality, Double> {
        val counts = mutableMapOf<Modality, Int>()
        chart.planetPositions.forEach { position ->
            val modality = getSignModality(position.sign)
            counts[modality] = (counts[modality] ?: 0) + 1
        }
        val total = counts.values.sum().toDouble().coerceAtLeast(1.0)
        return counts.mapValues { (it.value / total) * 100 }
    }
    
    companion object {
        /**
         * Create a new AnalysisContext for a chart
         */
        fun create(chart: VedicChart, context: Context): AnalysisContext {
            return AnalysisContext(chart, context)
        }
    }
}
