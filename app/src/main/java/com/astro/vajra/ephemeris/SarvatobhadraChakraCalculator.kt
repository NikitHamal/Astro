package com.astro.vajra.ephemeris

import com.astro.vajra.core.model.Nakshatra
import com.astro.vajra.core.model.Planet
import com.astro.vajra.core.model.PlanetPosition
import com.astro.vajra.core.model.VedicChart
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.ZoneId
import java.time.ZoneOffset
import kotlin.math.abs

/**
 * SarvatobhadraChakraCalculator - Traditional 9x9 Vedha Chakra
 *
 * The Sarvatobhadra Chakra is a comprehensive 9x9 grid chart showing relationships
 * between nakshatras, vowels (swaras), weekdays (varas), and tithis. It's primarily
 * used for:
 * 1. Transit analysis - understanding planetary vedha (obstruction) points
 * 2. Muhurta selection - determining auspicious timing
 * 3. Name compatibility - through vowel associations
 *
 * The chakra maps each nakshatra to specific cells, and planetary transits
 * through nakshatras create vedha (hits) on connected cells, affecting the
 * significations of those cells.
 *
 * Vedic References:
 * - Muhurta Chintamani
 * - Sarvatobhadra Chakra traditional commentaries
 * - Muhurta Martanda
 */
object SarvatobhadraChakraCalculator {

    /**
     * Cell types in the Sarvatobhadra Chakra
     */
    enum class CellType(val displayName: String) {
        NAKSHATRA("Nakshatra"),
        VOWEL("Vowel"),
        WEEKDAY("Weekday"),
        TITHI("Tithi"),
        CENTER("Center"),
        EMPTY("Empty")
    }

    /**
     * Vedha effect types
     */
    enum class VedhaType(val displayName: String, val severity: Int) {
        FULL("Full Vedha", 4),
        THREE_QUARTER("3/4 Vedha", 3),
        HALF("Half Vedha", 2),
        QUARTER("Quarter Vedha", 1),
        NONE("No Vedha", 0)
    }

    /**
     * Effect nature for vedha
     */
    enum class VedhaEffect(val displayName: String) {
        BENEFIC("Beneficial"),
        MALEFIC("Malefic"),
        MIXED("Mixed"),
        NEUTRAL("Neutral")
    }

    /**
     * Sanskrit vowels (Swaras) in the chakra
     */
    enum class Swara(val sanskrit: String, val english: String, val associatedPlanets: List<Planet>) {
        A("अ", "A", listOf(Planet.SUN)),
        AA("आ", "Aa", listOf(Planet.SUN)),
        I("इ", "I", listOf(Planet.VENUS)),
        II("ई", "Ii", listOf(Planet.VENUS)),
        U("उ", "U", listOf(Planet.MOON)),
        UU("ऊ", "Uu", listOf(Planet.MOON)),
        RI("ऋ", "Ri", listOf(Planet.MARS)),
        RII("ॠ", "Rii", listOf(Planet.MARS)),
        LRI("ऌ", "Lri", listOf(Planet.MERCURY)),
        E("ए", "E", listOf(Planet.JUPITER)),
        AI("ऐ", "Ai", listOf(Planet.JUPITER)),
        O("ओ", "O", listOf(Planet.SATURN)),
        AU("औ", "Au", listOf(Planet.SATURN)),
        AM("अं", "Am", listOf(Planet.RAHU)),
        AH("अः", "Ah", listOf(Planet.KETU))
    }

    /**
     * Represents a cell in the 9x9 chakra
     */
    data class ChakraCell(
        val row: Int,
        val col: Int,
        val cellType: CellType,
        val nakshatra: Nakshatra? = null,
        val swara: Swara? = null,
        val weekday: DayOfWeek? = null,
        val tithiNumber: Int? = null,
        val displayLabel: String,
        val vedhaPoints: List<Pair<Int, Int>> = emptyList()
    )

    /**
     * Vedha analysis result for a specific nakshatra
     */
    data class NakshatraVedha(
        val nakshatra: Nakshatra,
        val transitingPlanet: Planet,
        val vedhaType: VedhaType,
        val effect: VedhaEffect,
        val affectedCells: List<ChakraCell>,
        val interpretation: String
    )

    /**
     * Daily analysis result
     */
    data class DailyAnalysis(
        val date: LocalDate,
        val weekday: DayOfWeek,
        val moonNakshatra: Nakshatra,
        val moonPada: Int,
        val tithiNumber: Int,
        val overallScore: Int,
        val favorableActivities: List<String>,
        val unfavorableActivities: List<String>,
        val vedhaEffects: List<NakshatraVedha>,
        val interpretation: String
    )

    /**
     * Name analysis result
     */
    data class NameAnalysis(
        val firstLetter: Char,
        val associatedSwara: Swara?,
        val favorableNakshatras: List<Nakshatra>,
        val unfavorableNakshatras: List<Nakshatra>,
        val planetaryInfluence: List<Planet>,
        val interpretation: String
    )

    /**
     * Complete Sarvatobhadra Chakra analysis
     */
    data class SarvatobhadraAnalysis(
        val chakraGrid: Array<Array<ChakraCell>>,
        val birthNakshatra: Nakshatra,
        val birthPada: Int,
        val currentTransitVedhas: List<NakshatraVedha>,
        val dailyAnalysis: DailyAnalysis?,
        val favorableDays: List<DayOfWeek>,
        val unfavorableDays: List<DayOfWeek>,
        val overallTransitScore: Int,
        val keyInsights: List<String>,
        val recommendations: List<String>
    ) {
        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (other !is SarvatobhadraAnalysis) return false
            return birthNakshatra == other.birthNakshatra &&
                    birthPada == other.birthPada &&
                    overallTransitScore == other.overallTransitScore
        }
        override fun hashCode(): Int {
            return birthNakshatra.hashCode() * 31 + birthPada + overallTransitScore
        }
    }

    // ============================================
    // NAKSHATRA TO CHAKRA POSITION MAPPING
    // ============================================

    /**
     * Maps nakshatras to their positions in the 9x9 grid.
     * The chakra has nakshatras arranged around the perimeter and diagonals.
     */
    private val nakshatraPositions: Map<Nakshatra, Pair<Int, Int>> = mapOf(
        Nakshatra.ASHWINI to (0 to 4),
        Nakshatra.BHARANI to (0 to 5),
        Nakshatra.KRITTIKA to (0 to 6),
        Nakshatra.ROHINI to (1 to 7),
        Nakshatra.MRIGASHIRA to (2 to 8),
        Nakshatra.ARDRA to (3 to 8),
        Nakshatra.PUNARVASU to (4 to 8),
        Nakshatra.PUSHYA to (5 to 8),
        Nakshatra.ASHLESHA to (6 to 8),
        Nakshatra.MAGHA to (7 to 7),
        Nakshatra.PURVA_PHALGUNI to (8 to 6),
        Nakshatra.UTTARA_PHALGUNI to (8 to 5),
        Nakshatra.HASTA to (8 to 4),
        Nakshatra.CHITRA to (8 to 3),
        Nakshatra.SWATI to (8 to 2),
        Nakshatra.VISHAKHA to (7 to 1),
        Nakshatra.ANURADHA to (6 to 0),
        Nakshatra.JYESHTHA to (5 to 0),
        Nakshatra.MULA to (4 to 0),
        Nakshatra.PURVA_ASHADHA to (3 to 0),
        Nakshatra.UTTARA_ASHADHA to (2 to 0),
        Nakshatra.SHRAVANA to (1 to 1),
        Nakshatra.DHANISHTHA to (0 to 2),
        Nakshatra.SHATABHISHA to (0 to 3),
        Nakshatra.PURVA_BHADRAPADA to (1 to 3),
        Nakshatra.UTTARA_BHADRAPADA to (2 to 4),
        Nakshatra.REVATI to (0 to 1)
    )

    /**
     * Weekday positions in the chakra (middle row and column)
     */
    private val weekdayPositions: Map<DayOfWeek, Pair<Int, Int>> = mapOf(
        DayOfWeek.SUNDAY to (4 to 1),
        DayOfWeek.MONDAY to (4 to 2),
        DayOfWeek.TUESDAY to (4 to 3),
        DayOfWeek.WEDNESDAY to (4 to 5),
        DayOfWeek.THURSDAY to (4 to 6),
        DayOfWeek.FRIDAY to (4 to 7),
        DayOfWeek.SATURDAY to (1 to 4)
    )

    /**
     * Swara (vowel) positions around the chakra
     */
    private val swaraPositions: Map<Swara, Pair<Int, Int>> = mapOf(
        Swara.A to (0 to 0),
        Swara.AA to (0 to 7),
        Swara.I to (1 to 0),
        Swara.II to (1 to 8),
        Swara.U to (2 to 1),
        Swara.UU to (2 to 7),
        Swara.RI to (3 to 1),
        Swara.E to (5 to 1),
        Swara.AI to (6 to 1),
        Swara.O to (7 to 2),
        Swara.AU to (7 to 6),
        Swara.AM to (3 to 7),
        Swara.AH to (5 to 7)
    )

    // ============================================
    // VEDHA POINT MAPPINGS
    // ============================================

    /**
     * Each nakshatra has vedha points - cells it affects when transited
     */
    private fun getVedhaPoints(nakshatra: Nakshatra): List<Pair<Int, Int>> {
        val position = nakshatraPositions[nakshatra] ?: return emptyList()
        val (row, col) = position
        val vedhaPoints = mutableListOf<Pair<Int, Int>>()

        // Horizontal vedha (same row)
        for (c in 0..8) {
            if (c != col) vedhaPoints.add(row to c)
        }
        // Vertical vedha (same column)
        for (r in 0..8) {
            if (r != row) vedhaPoints.add(r to col)
        }
        // Diagonal vedha
        for (i in -8..8) {
            val r1 = row + i
            val c1 = col + i
            if (r1 in 0..8 && c1 in 0..8 && (r1 != row || c1 != col)) {
                vedhaPoints.add(r1 to c1)
            }
            val c2 = col - i
            if (r1 in 0..8 && c2 in 0..8 && (r1 != row || c2 != col)) {
                vedhaPoints.add(r1 to c2)
            }
        }
        return vedhaPoints.distinct()
    }

    // ============================================
    // CHAKRA GENERATION
    // ============================================

    /**
     * Generate the complete 9x9 Sarvatobhadra Chakra grid
     */
    fun generateChakraGrid(): Array<Array<ChakraCell>> {
        val grid = Array(9) { row ->
            Array(9) { col ->
                ChakraCell(
                    row = row,
                    col = col,
                    cellType = CellType.EMPTY,
                    displayLabel = ""
                )
            }
        }

        // Place nakshatras
        nakshatraPositions.forEach { (nakshatra, pos) ->
            val (row, col) = pos
            grid[row][col] = ChakraCell(
                row = row,
                col = col,
                cellType = CellType.NAKSHATRA,
                nakshatra = nakshatra,
                displayLabel = nakshatra.displayName.take(4),
                vedhaPoints = getVedhaPoints(nakshatra)
            )
        }

        // Place weekdays
        weekdayPositions.forEach { (day, pos) ->
            val (row, col) = pos
            grid[row][col] = ChakraCell(
                row = row,
                col = col,
                cellType = CellType.WEEKDAY,
                weekday = day,
                displayLabel = day.name.take(3)
            )
        }

        // Place swaras
        swaraPositions.forEach { (swara, pos) ->
            val (row, col) = pos
            grid[row][col] = ChakraCell(
                row = row,
                col = col,
                cellType = CellType.VOWEL,
                swara = swara,
                displayLabel = swara.sanskrit
            )
        }

        // Center cell
        grid[4][4] = ChakraCell(
            row = 4,
            col = 4,
            cellType = CellType.CENTER,
            displayLabel = "ॐ"
        )

        return grid
    }

    // ============================================
    // MAIN ANALYSIS FUNCTIONS
    // ============================================

    /**
     * Perform complete Sarvatobhadra Chakra analysis
     */
    fun analyzeSarvatobhadra(
        chart: VedicChart,
        transitPositions: List<PlanetPosition>? = null,
        currentDate: LocalDate = LocalDate.now(resolveZoneId(chart.birthData.timezone))
    ): SarvatobhadraAnalysis? {
        val moonPos = chart.planetPositions.find { it.planet == Planet.MOON } ?: return null
        val (birthNakshatra, birthPada) = Nakshatra.fromLongitude(moonPos.longitude)

        val chakraGrid = generateChakraGrid()
        val transits = transitPositions ?: chart.planetPositions

        // Analyze current transit vedhas
        val transitVedhas = analyzeTransitVedhas(transits, birthNakshatra)

        // Daily analysis
        val dailyAnalysis = analyzeDailyInfluences(
            currentDate,
            birthNakshatra,
            transits
        )

        // Determine favorable/unfavorable days
        val (favorableDays, unfavorableDays) = analyzeFavorableDays(birthNakshatra)

        // Calculate overall transit score
        val overallScore = calculateOverallScore(transitVedhas, birthNakshatra)

        // Generate insights and recommendations
        val insights = generateInsights(transitVedhas, birthNakshatra, dailyAnalysis)
        val recommendations = generateRecommendations(transitVedhas, favorableDays, unfavorableDays)

        return SarvatobhadraAnalysis(
            chakraGrid = chakraGrid,
            birthNakshatra = birthNakshatra,
            birthPada = birthPada,
            currentTransitVedhas = transitVedhas,
            dailyAnalysis = dailyAnalysis,
            favorableDays = favorableDays,
            unfavorableDays = unfavorableDays,
            overallTransitScore = overallScore,
            keyInsights = insights,
            recommendations = recommendations
        )
    }

    private fun resolveZoneId(timezone: String?): ZoneId {
        return com.astro.vajra.util.TimezoneSanitizer.resolveZoneIdOrNull(timezone)
            ?: ZoneOffset.UTC
    }

    /**
     * Analyze vedha effects from planetary transits
     */
    private fun analyzeTransitVedhas(
        transits: List<PlanetPosition>,
        birthNakshatra: Nakshatra
    ): List<NakshatraVedha> {
        val vedhas = mutableListOf<NakshatraVedha>()
        val chakraGrid = generateChakraGrid()

        // Major transit planets
        val majorPlanets = listOf(
            Planet.SATURN, Planet.JUPITER, Planet.RAHU, Planet.KETU,
            Planet.MARS, Planet.SUN, Planet.MOON, Planet.VENUS, Planet.MERCURY
        )

        transits.filter { it.planet in majorPlanets }.forEach { transit ->
            val (transitNakshatra, _) = Nakshatra.fromLongitude(transit.longitude)
            val vedhaPoints = getVedhaPoints(transitNakshatra)
            val birthPosition = nakshatraPositions[birthNakshatra]

            if (birthPosition != null && birthPosition in vedhaPoints) {
                val vedhaType = calculateVedhaType(transit.planet, transitNakshatra, birthNakshatra)
                val effect = determineVedhaEffect(transit.planet, transitNakshatra)
                val affectedCells = vedhaPoints.mapNotNull { (r, c) ->
                    if (r in 0..8 && c in 0..8) chakraGrid[r][c] else null
                }

                vedhas.add(
                    NakshatraVedha(
                        nakshatra = transitNakshatra,
                        transitingPlanet = transit.planet,
                        vedhaType = vedhaType,
                        effect = effect,
                        affectedCells = affectedCells,
                        interpretation = buildVedhaInterpretation(transit.planet, transitNakshatra, birthNakshatra, effect)
                    )
                )
            }
        }

        return vedhas.sortedByDescending { it.vedhaType.severity }
    }

    /**
     * Calculate vedha type based on planet and nakshatra relationship
     */
    private fun calculateVedhaType(
        planet: Planet,
        transitNakshatra: Nakshatra,
        birthNakshatra: Nakshatra
    ): VedhaType {
        val transitPos = nakshatraPositions[transitNakshatra]
        val birthPos = nakshatraPositions[birthNakshatra]

        if (transitPos == null || birthPos == null) return VedhaType.NONE

        val rowDiff = abs(transitPos.first - birthPos.first)
        val colDiff = abs(transitPos.second - birthPos.second)

        // Same row or column = full vedha
        if (rowDiff == 0 || colDiff == 0) {
            return if (planet in listOf(Planet.SATURN, Planet.RAHU, Planet.KETU))
                VedhaType.FULL else VedhaType.THREE_QUARTER
        }

        // Diagonal = 3/4 or half vedha
        if (rowDiff == colDiff) {
            return if (planet in listOf(Planet.SATURN, Planet.RAHU))
                VedhaType.THREE_QUARTER else VedhaType.HALF
        }

        // Other positions = quarter or none
        return if (rowDiff <= 2 && colDiff <= 2) VedhaType.QUARTER else VedhaType.NONE
    }

    /**
     * Determine the effect of a vedha
     */
    private fun determineVedhaEffect(planet: Planet, nakshatra: Nakshatra): VedhaEffect {
        return when (planet) {
            Planet.JUPITER, Planet.VENUS -> VedhaEffect.BENEFIC
            Planet.SATURN, Planet.RAHU, Planet.KETU -> VedhaEffect.MALEFIC
            Planet.MARS -> if (nakshatra.ruler == Planet.MARS) VedhaEffect.MIXED else VedhaEffect.MALEFIC
            Planet.SUN -> if (nakshatra.ruler == Planet.SUN) VedhaEffect.BENEFIC else VedhaEffect.MIXED
            Planet.MOON -> VedhaEffect.MIXED
            Planet.MERCURY -> VedhaEffect.NEUTRAL
            else -> VedhaEffect.NEUTRAL
        }
    }

    // ============================================
    // DAILY ANALYSIS
    // ============================================

    /**
     * Analyze daily influences based on Sarvatobhadra
     */
    private fun analyzeDailyInfluences(
        date: LocalDate,
        birthNakshatra: Nakshatra,
        transits: List<PlanetPosition>
    ): DailyAnalysis {
        val weekday = date.dayOfWeek
        val moonTransit = transits.find { it.planet == Planet.MOON }
        val (moonNakshatra, moonPada) = if (moonTransit != null) {
            Nakshatra.fromLongitude(moonTransit.longitude)
        } else {
            birthNakshatra to 1
        }

        // Calculate tithi (simplified)
        val sunLong = transits.find { it.planet == Planet.SUN }?.longitude ?: 0.0
        val moonLong = moonTransit?.longitude ?: 0.0
        val tithiNumber = ((moonLong - sunLong + 360) % 360 / 12).toInt() + 1

        val vedhaEffects = analyzeTransitVedhas(transits, birthNakshatra)
        val overallScore = calculateDailyScore(weekday, moonNakshatra, birthNakshatra, vedhaEffects)

        val favorableActivities = determineFavorableActivities(weekday, moonNakshatra, overallScore)
        val unfavorableActivities = determineUnfavorableActivities(vedhaEffects, overallScore)

        return DailyAnalysis(
            date = date,
            weekday = weekday,
            moonNakshatra = moonNakshatra,
            moonPada = moonPada,
            tithiNumber = tithiNumber,
            overallScore = overallScore,
            favorableActivities = favorableActivities,
            unfavorableActivities = unfavorableActivities,
            vedhaEffects = vedhaEffects,
            interpretation = buildDailyInterpretation(weekday, moonNakshatra, birthNakshatra, overallScore)
        )
    }

    /**
     * Calculate daily score (0-100)
     */
    private fun calculateDailyScore(
        weekday: DayOfWeek,
        moonNakshatra: Nakshatra,
        birthNakshatra: Nakshatra,
        vedhas: List<NakshatraVedha>
    ): Int {
        var score = 50 // Base score

        // Weekday influence
        val weekdayPlanet = getWeekdayRuler(weekday)
        if (weekdayPlanet == birthNakshatra.ruler) score += 15
        if (VedicAstrologyUtils.areNaturalFriends(weekdayPlanet, birthNakshatra.ruler)) score += 10

        // Moon nakshatra relationship
        val nakshatraDiff = abs(moonNakshatra.number - birthNakshatra.number)
        when (nakshatraDiff % 9) {
            0, 2, 4, 6, 8 -> score += 10 // Favorable tarabala positions
            1, 3, 5, 7 -> score -= 5     // Challenging positions
        }

        // Vedha effects
        vedhas.forEach { vedha ->
            when (vedha.effect) {
                VedhaEffect.BENEFIC -> score += vedha.vedhaType.severity * 3
                VedhaEffect.MALEFIC -> score -= vedha.vedhaType.severity * 5
                VedhaEffect.MIXED -> score -= vedha.vedhaType.severity * 2
                VedhaEffect.NEUTRAL -> { }
            }
        }

        return score.coerceIn(0, 100)
    }

    /**
     * Get the ruling planet for a weekday
     */
    private fun getWeekdayRuler(day: DayOfWeek): Planet {
        return when (day) {
            DayOfWeek.SUNDAY -> Planet.SUN
            DayOfWeek.MONDAY -> Planet.MOON
            DayOfWeek.TUESDAY -> Planet.MARS
            DayOfWeek.WEDNESDAY -> Planet.MERCURY
            DayOfWeek.THURSDAY -> Planet.JUPITER
            DayOfWeek.FRIDAY -> Planet.VENUS
            DayOfWeek.SATURDAY -> Planet.SATURN
        }
    }

    // ============================================
    // FAVORABLE DAYS ANALYSIS
    // ============================================

    /**
     * Analyze favorable and unfavorable days based on birth nakshatra
     */
    private fun analyzeFavorableDays(birthNakshatra: Nakshatra): Pair<List<DayOfWeek>, List<DayOfWeek>> {
        val nakshatraRuler = birthNakshatra.ruler
        val favorable = mutableListOf<DayOfWeek>()
        val unfavorable = mutableListOf<DayOfWeek>()

        DayOfWeek.entries.forEach { day ->
            val dayRuler = getWeekdayRuler(day)
            when {
                dayRuler == nakshatraRuler -> favorable.add(day)
                VedicAstrologyUtils.areNaturalFriends(dayRuler, nakshatraRuler) -> favorable.add(day)
                VedicAstrologyUtils.areNaturalEnemies(dayRuler, nakshatraRuler) -> unfavorable.add(day)
            }
        }

        // Add Thursday (Jupiter's day) as generally favorable
        if (DayOfWeek.THURSDAY !in favorable && DayOfWeek.THURSDAY !in unfavorable) {
            favorable.add(DayOfWeek.THURSDAY)
        }

        return favorable to unfavorable
    }

    // ============================================
    // NAME ANALYSIS
    // ============================================

    /**
     * Analyze name compatibility with Sarvatobhadra Chakra
     */
    fun analyzeNameCompatibility(firstLetter: Char, birthNakshatra: Nakshatra): NameAnalysis {
        val letterUpper = firstLetter.uppercaseChar()
        val associatedSwara = findSwaraForLetter(letterUpper)

        val favorableNakshatras = mutableListOf<Nakshatra>()
        val unfavorableNakshatras = mutableListOf<Nakshatra>()

        if (associatedSwara != null) {
            val swaraPos = swaraPositions[associatedSwara]
            if (swaraPos != null) {
                // Find nakshatras not in vedha with this swara position
                Nakshatra.entries.forEach { nakshatra ->
                    val nakshatraVedha = getVedhaPoints(nakshatra)
                    if (swaraPos !in nakshatraVedha) {
                        favorableNakshatras.add(nakshatra)
                    } else {
                        unfavorableNakshatras.add(nakshatra)
                    }
                }
            }
        }

        val planetaryInfluence = associatedSwara?.associatedPlanets ?: emptyList()

        return NameAnalysis(
            firstLetter = letterUpper,
            associatedSwara = associatedSwara,
            favorableNakshatras = favorableNakshatras,
            unfavorableNakshatras = unfavorableNakshatras,
            planetaryInfluence = planetaryInfluence,
            interpretation = buildNameInterpretation(letterUpper, associatedSwara, birthNakshatra)
        )
    }

    /**
     * Find the swara (vowel) associated with a letter
     */
    private fun findSwaraForLetter(letter: Char): Swara? {
        return when (letter) {
            'A' -> Swara.A
            'I', 'E' -> Swara.I
            'U', 'O' -> Swara.U
            else -> null
        }
    }

    // ============================================
    // SCORE AND INTERPRETATION BUILDERS
    // ============================================

    /**
     * Calculate overall transit score
     */
    private fun calculateOverallScore(vedhas: List<NakshatraVedha>, birthNakshatra: Nakshatra): Int {
        var score = 70 // Base score

        vedhas.forEach { vedha ->
            val multiplier = when (vedha.transitingPlanet) {
                Planet.SATURN -> 3
                Planet.RAHU, Planet.KETU -> 2
                Planet.JUPITER -> -2 // Positive effect
                else -> 1
            }
            when (vedha.effect) {
                VedhaEffect.BENEFIC -> score += vedha.vedhaType.severity * 3
                VedhaEffect.MALEFIC -> score -= vedha.vedhaType.severity * multiplier
                VedhaEffect.MIXED -> score -= vedha.vedhaType.severity
                VedhaEffect.NEUTRAL -> { }
            }
        }

        return score.coerceIn(0, 100)
    }

    /**
     * Build vedha interpretation text
     */
    private fun buildVedhaInterpretation(
        planet: Planet,
        transitNakshatra: Nakshatra,
        birthNakshatra: Nakshatra,
        effect: VedhaEffect
    ): String {
        val effectText = when (effect) {
            VedhaEffect.BENEFIC -> "brings supportive influences"
            VedhaEffect.MALEFIC -> "may create obstacles"
            VedhaEffect.MIXED -> "creates mixed influences"
            VedhaEffect.NEUTRAL -> "has neutral influence"
        }
        return "${planet.displayName} transiting ${transitNakshatra.displayName} $effectText on ${birthNakshatra.displayName} matters."
    }

    /**
     * Build daily interpretation
     */
    private fun buildDailyInterpretation(
        weekday: DayOfWeek,
        moonNakshatra: Nakshatra,
        birthNakshatra: Nakshatra,
        score: Int
    ): String {
        val quality = when {
            score >= 75 -> "highly favorable"
            score >= 55 -> "moderately favorable"
            score >= 40 -> "neutral"
            score >= 25 -> "somewhat challenging"
            else -> "challenging"
        }
        return "${weekday.name.lowercase().replaceFirstChar { it.uppercase() }} with Moon in ${moonNakshatra.displayName} " +
                "is $quality for ${birthNakshatra.displayName} natives. Overall score: $score/100."
    }

    /**
     * Build name interpretation
     */
    private fun buildNameInterpretation(letter: Char, swara: Swara?, birthNakshatra: Nakshatra): String {
        if (swara == null) {
            return "The letter '$letter' does not have a direct vowel association in the Sarvatobhadra Chakra."
        }
        val planets = swara.associatedPlanets.joinToString { it.displayName }
        return "The letter '$letter' (${swara.sanskrit}) is associated with $planets. " +
                "For ${birthNakshatra.displayName} natives, names starting with this letter have specific " +
                "karmic connections based on the chakra alignments."
    }

    // ============================================
    // FAVORABLE/UNFAVORABLE ACTIVITIES
    // ============================================

    private fun determineFavorableActivities(weekday: DayOfWeek, moonNakshatra: Nakshatra, score: Int): List<String> {
        val activities = mutableListOf<String>()
        val dayRuler = getWeekdayRuler(weekday)

        when (dayRuler) {
            Planet.SUN -> activities.addAll(listOf("Government work", "Authority matters", "Father-related"))
            Planet.MOON -> activities.addAll(listOf("Travel", "Public dealings", "Mother-related"))
            Planet.MARS -> activities.addAll(listOf("Physical activities", "Competition", "Property"))
            Planet.MERCURY -> activities.addAll(listOf("Communication", "Learning", "Business"))
            Planet.JUPITER -> activities.addAll(listOf("Religious activities", "Teaching", "Expansion"))
            Planet.VENUS -> activities.addAll(listOf("Arts", "Relationships", "Luxury purchases"))
            Planet.SATURN -> activities.addAll(listOf("Discipline work", "Long-term planning", "Service"))
            else -> {}
        }

        if (score >= 60) {
            activities.add("New beginnings")
            activities.add("Important decisions")
        }

        return activities.take(5)
    }

    private fun determineUnfavorableActivities(vedhas: List<NakshatraVedha>, score: Int): List<String> {
        val activities = mutableListOf<String>()

        vedhas.filter { it.effect == VedhaEffect.MALEFIC }.forEach { vedha ->
            when (vedha.transitingPlanet) {
                Planet.SATURN -> activities.add("Rushing important matters")
                Planet.MARS -> activities.add("Risky ventures")
                Planet.RAHU -> activities.add("Major financial decisions")
                Planet.KETU -> activities.add("Starting new relationships")
                else -> {}
            }
        }

        if (score < 40) {
            activities.add("Major purchases")
            activities.add("Travel to new places")
        }

        return activities.distinct().take(4)
    }

    // ============================================
    // INSIGHTS AND RECOMMENDATIONS
    // ============================================

    private fun generateInsights(
        vedhas: List<NakshatraVedha>,
        birthNakshatra: Nakshatra,
        dailyAnalysis: DailyAnalysis?
    ): List<String> {
        val insights = mutableListOf<String>()

        // Major vedha insights
        vedhas.filter { it.vedhaType.severity >= 3 }.forEach { vedha ->
            insights.add("${vedha.transitingPlanet.displayName} creates ${vedha.vedhaType.displayName} on your nakshatra")
        }

        // Daily score insight
        dailyAnalysis?.let { daily ->
            when {
                daily.overallScore >= 70 -> insights.add("Today is auspicious for important activities")
                daily.overallScore <= 30 -> insights.add("Today requires caution in new undertakings")
                else -> {}
            }
        }

        // Nakshatra ruler insight
        insights.add("Your birth nakshatra ${birthNakshatra.displayName} is ruled by ${birthNakshatra.ruler.displayName}")

        return insights.take(5)
    }

    private fun generateRecommendations(
        vedhas: List<NakshatraVedha>,
        favorableDays: List<DayOfWeek>,
        unfavorableDays: List<DayOfWeek>
    ): List<String> {
        val recommendations = mutableListOf<String>()

        // Day recommendations
        if (favorableDays.isNotEmpty()) {
            recommendations.add("Favorable days: ${favorableDays.joinToString { it.name.take(3) }}")
        }
        if (unfavorableDays.isNotEmpty()) {
            recommendations.add("Exercise caution on: ${unfavorableDays.joinToString { it.name.take(3) }}")
        }

        // Vedha-based recommendations
        val maleficVedhas = vedhas.filter { it.effect == VedhaEffect.MALEFIC }
        if (maleficVedhas.isNotEmpty()) {
            recommendations.add("Remedies recommended for ${maleficVedhas.first().transitingPlanet.displayName} transit")
        }

        val beneficVedhas = vedhas.filter { it.effect == VedhaEffect.BENEFIC }
        if (beneficVedhas.isNotEmpty()) {
            recommendations.add("Utilize ${beneficVedhas.first().transitingPlanet.displayName}'s supportive transit")
        }

        return recommendations.take(5)
    }

    // ============================================================================
    // ENHANCED TRANSIT VEDHA ANALYSIS
    // ============================================================================

    /**
     * Vedha direction per traditional SBC texts.
     * - SAMMUKHA (Front): Direct line of sight - strongest vedha
     * - VAMA (Left): Vedha from left flank - moderate strength
     * - DAKSHINA (Right): Vedha from right flank - moderate strength
     */
    enum class VedhaDirection(val displayName: String, val strengthMultiplier: Double) {
        SAMMUKHA("Sammukha (Front)", 1.0),
        VAMA("Vama (Left)", 0.75),
        DAKSHINA("Dakshina (Right)", 0.75)
    }

    /**
     * Cell state for the interactive grid representation.
     */
    data class SBCCellState(
        val cell: ChakraCell,
        val isNatalPoint: Boolean = false,
        val activeVedhas: List<CellVedhaInfo> = emptyList(),
        val overallIntensity: Double = 0.0,
        val isHighlighted: Boolean = false
    )

    /**
     * Vedha information for a single cell.
     */
    data class CellVedhaInfo(
        val fromPlanet: Planet,
        val direction: VedhaDirection,
        val intensity: Double,
        val isBenefic: Boolean
    )

    /**
     * Tithi positions in the SBC grid.
     * Tithis 1-15 (Shukla) and 1-15 (Krishna) mapped to grid cells.
     */
    private val tithiPositions: Map<Int, Pair<Int, Int>> = mapOf(
        // Shukla Paksha tithis (1-15)
        1 to (3 to 2), 2 to (3 to 3), 3 to (3 to 4),
        4 to (3 to 5), 5 to (3 to 6), 6 to (2 to 2),
        7 to (2 to 3), 8 to (1 to 2), 9 to (1 to 5),
        10 to (1 to 6), 11 to (2 to 5), 12 to (2 to 6),
        13 to (5 to 2), 14 to (5 to 3), 15 to (5 to 4),
        // Krishna Paksha tithis (16-30 mapped as 16=1K, etc.)
        16 to (5 to 5), 17 to (5 to 6), 18 to (6 to 2),
        19 to (6 to 3), 20 to (6 to 4), 21 to (6 to 5),
        22 to (6 to 6), 23 to (7 to 3), 24 to (7 to 4),
        25 to (7 to 5), 26 to (7 to 6), 27 to (6 to 7),
        28 to (7 to 0), 29 to (8 to 7), 30 to (8 to 8)
    )

    /**
     * Letter to SBC cell mapping for name-based vedha analysis.
     * Maps common first letters to their SBC vowel positions.
     */
    private val letterToSwaraMap: Map<Char, Swara> = mapOf(
        'A' to Swara.A, 'B' to Swara.U, 'C' to Swara.I,
        'D' to Swara.RI, 'E' to Swara.E, 'F' to Swara.U,
        'G' to Swara.U, 'H' to Swara.A, 'I' to Swara.I,
        'J' to Swara.I, 'K' to Swara.A, 'L' to Swara.LRI,
        'M' to Swara.U, 'N' to Swara.RI, 'O' to Swara.O,
        'P' to Swara.U, 'Q' to Swara.A, 'R' to Swara.RI,
        'S' to Swara.A, 'T' to Swara.RI, 'U' to Swara.U,
        'V' to Swara.U, 'W' to Swara.U, 'X' to Swara.A,
        'Y' to Swara.I, 'Z' to Swara.A
    )

    /**
     * Perform comprehensive transit vedha analysis on the SBC grid.
     *
     * Maps the native's birth Nakshatra, Tithi, Vara, and name letter to SBC cells,
     * then checks each transiting planet's nakshatra for vedha connections to these
     * birth points, calculating direction, intensity, and timeline.
     *
     * @param natalChart The native's birth chart
     * @param transitPositions Current planetary transit positions
     * @param birthNakshatra The native's Janma Nakshatra
     * @param birthTithi The birth tithi number (1-30)
     * @param birthVara The birth day of the week
     * @param nameFirstLetter Optional first letter of the native's name
     * @return SBCTransitAnalysis with vedha details, affected points, timeline, and grid state
     */
    fun analyzeTransitVedha(
        natalChart: VedicChart,
        transitPositions: List<PlanetPosition>,
        birthNakshatra: Nakshatra,
        birthTithi: Int,
        birthVara: DayOfWeek,
        nameFirstLetter: String? = null
    ): SBCTransitAnalysis {
        val grid = generateChakraGrid()

        // Step 1: Identify all birth points on the grid
        val birthPoints = mapBirthPoints(birthNakshatra, birthTithi, birthVara, nameFirstLetter)

        // Step 2: Analyze each transiting planet for vedha on birth points
        val activeVedhas = mutableListOf<ActiveVedha>()

        val transitPlanets = transitPositions.filter { it.planet in Planet.MAIN_PLANETS }

        for (transit in transitPlanets) {
            val (transitNakshatra, _) = Nakshatra.fromLongitude(transit.longitude)
            val transitPos = nakshatraPositions[transitNakshatra] ?: continue
            val transitDegreeInNakshatra = transit.longitude % (360.0 / 27.0)

            for (birthPoint in birthPoints) {
                val vedhaDirection = calculateVedhaDirection(transitPos, birthPoint.gridPosition)
                if (vedhaDirection != null) {
                    val vedhaType = calculateEnhancedVedhaType(
                        transit.planet, transitNakshatra, birthPoint, transitPos
                    )
                    if (vedhaType != VedhaType.NONE) {
                        val vedhaEffect = determineEnhancedVedhaEffect(
                            transit.planet, transitNakshatra, birthPoint, transit.isRetrograde
                        )
                        val intensity = calculateVedhaIntensity(
                            planet = transit.planet,
                            vedhaType = vedhaType,
                            vedhaDirection = vedhaDirection,
                            isRetrograde = transit.isRetrograde,
                            degreeInNakshatra = transitDegreeInNakshatra,
                            birthPointType = birthPoint.pointType
                        )

                        activeVedhas.add(
                            ActiveVedha(
                                transitPlanet = transit.planet,
                                transitNakshatra = transitNakshatra,
                                targetCell = grid[birthPoint.gridPosition.first][birthPoint.gridPosition.second],
                                vedhaDirection = vedhaDirection,
                                vedhaType = vedhaType,
                                vedhaEffect = vedhaEffect,
                                intensity = intensity,
                                isRetrograde = transit.isRetrograde
                            )
                        )
                    }
                }
            }
        }

        // Step 3: Determine affected birth points
        val affectedBirthPoints = birthPoints.map { bp ->
            val vedhasOnPoint = activeVedhas.filter { av ->
                av.targetCell.row == bp.gridPosition.first &&
                av.targetCell.col == bp.gridPosition.second
            }
            AffectedBirthPoint(
                pointType = bp.pointType,
                gridPosition = bp.gridPosition,
                label = bp.label,
                vedhas = vedhasOnPoint,
                netEffect = calculateNetEffect(vedhasOnPoint),
                interpretation = buildBirthPointInterpretation(bp, vedhasOnPoint)
            )
        }

        // Step 4: Build vedha timeline (when vedha activates/deactivates)
        val vedhaTimeline = buildVedhaTimeline(transitPositions, birthPoints)

        // Step 5: Calculate overall impact
        val overallImpact = calculateOverallImpact(activeVedhas, affectedBirthPoints)

        // Step 6: Build grid state with vedha highlights
        val gridState = buildGridState(grid, birthPoints, activeVedhas)

        return SBCTransitAnalysis(
            activeVedhas = activeVedhas.sortedByDescending { it.intensity },
            affectedBirthPoints = affectedBirthPoints,
            vedhaTimeline = vedhaTimeline,
            overallImpact = overallImpact,
            gridState = gridState
        )
    }

    // ============================================================================
    // BIRTH POINT MAPPING
    // ============================================================================

    /**
     * Maps the native's birth parameters (Nakshatra, Tithi, Vara, Name) to
     * their corresponding cells in the 9x9 SBC grid.
     */
    private fun mapBirthPoints(
        birthNakshatra: Nakshatra,
        birthTithi: Int,
        birthVara: DayOfWeek,
        nameFirstLetter: String?
    ): List<BirthPoint> {
        val points = mutableListOf<BirthPoint>()

        // 1. Birth Nakshatra position
        val nakshatraPos = nakshatraPositions[birthNakshatra]
        if (nakshatraPos != null) {
            points.add(
                BirthPoint(
                    pointType = BirthPointType.NAKSHATRA,
                    gridPosition = nakshatraPos,
                    label = birthNakshatra.displayName,
                    weight = 1.0 // Nakshatra vedha is most significant
                )
            )
        }

        // 2. Birth Tithi position
        val tithiPos = tithiPositions[birthTithi.coerceIn(1, 30)]
        if (tithiPos != null) {
            points.add(
                BirthPoint(
                    pointType = BirthPointType.TITHI,
                    gridPosition = tithiPos,
                    label = "Tithi $birthTithi",
                    weight = 0.7
                )
            )
        }

        // 3. Birth Vara position
        val varaPos = weekdayPositions[birthVara]
        if (varaPos != null) {
            points.add(
                BirthPoint(
                    pointType = BirthPointType.VARA,
                    gridPosition = varaPos,
                    label = birthVara.name.take(3),
                    weight = 0.6
                )
            )
        }

        // 4. Name first letter position
        if (!nameFirstLetter.isNullOrBlank()) {
            val letter = nameFirstLetter.first().uppercaseChar()
            val swara = letterToSwaraMap[letter]
            if (swara != null) {
                val swaraPos = swaraPositions[swara]
                if (swaraPos != null) {
                    points.add(
                        BirthPoint(
                            pointType = BirthPointType.NAME_LETTER,
                            gridPosition = swaraPos,
                            label = "$letter (${swara.sanskrit})",
                            weight = 0.5
                        )
                    )
                }
            }
        }

        return points
    }

    // ============================================================================
    // VEDHA DIRECTION CALCULATION
    // ============================================================================

    /**
     * Calculates the vedha direction between a transit position and a birth point.
     *
     * Per traditional SBC:
     * - SAMMUKHA (Front): Same row or same column (direct line)
     * - VAMA (Left): Left diagonal (row+col constant or transit is to the left)
     * - DAKSHINA (Right): Right diagonal (row-col constant or transit is to the right)
     *
     * Returns null if no vedha connection exists.
     */
    private fun calculateVedhaDirection(
        transitPos: Pair<Int, Int>,
        birthPos: Pair<Int, Int>
    ): VedhaDirection? {
        val (tRow, tCol) = transitPos
        val (bRow, bCol) = birthPos

        if (tRow == bRow && tCol == bCol) return null // Same cell, no vedha

        // Same row or same column = Sammukha (front) vedha
        if (tRow == bRow || tCol == bCol) {
            return VedhaDirection.SAMMUKHA
        }

        // Diagonal: row difference equals column difference
        val rowDiff = tRow - bRow
        val colDiff = tCol - bCol

        // Main diagonal (top-left to bottom-right): rowDiff == colDiff
        if (rowDiff == colDiff) {
            return if (colDiff > 0) VedhaDirection.DAKSHINA else VedhaDirection.VAMA
        }

        // Anti-diagonal (top-right to bottom-left): rowDiff == -colDiff
        if (rowDiff == -colDiff) {
            return if (colDiff > 0) VedhaDirection.VAMA else VedhaDirection.DAKSHINA
        }

        // No vedha connection
        return null
    }

    // ============================================================================
    // ENHANCED VEDHA TYPE CALCULATION
    // ============================================================================

    /**
     * Enhanced vedha type calculation considering:
     *   - Planet's natural strength (Saturn/Rahu = stronger vedha)
     *   - Grid distance (closer = stronger)
     *   - Birth point type (Nakshatra vedha > Tithi vedha > Vara vedha)
     */
    private fun calculateEnhancedVedhaType(
        planet: Planet,
        transitNakshatra: Nakshatra,
        birthPoint: BirthPoint,
        transitPos: Pair<Int, Int>
    ): VedhaType {
        val (bRow, bCol) = birthPoint.gridPosition
        val (tRow, tCol) = transitPos

        val rowDiff = abs(tRow - bRow)
        val colDiff = abs(tCol - bCol)

        // Direct (same row/col) = Full or 3/4 based on planet
        val isSameRowOrCol = (rowDiff == 0 || colDiff == 0)
        val isDiagonal = (rowDiff == colDiff) || (rowDiff + colDiff == 0)

        val isSlowMoving = planet in listOf(Planet.SATURN, Planet.RAHU, Planet.KETU, Planet.JUPITER)

        return when {
            isSameRowOrCol && isSlowMoving -> VedhaType.FULL
            isSameRowOrCol && !isSlowMoving -> VedhaType.THREE_QUARTER
            isDiagonal && isSlowMoving -> VedhaType.THREE_QUARTER
            isDiagonal && !isSlowMoving -> VedhaType.HALF
            else -> VedhaType.NONE
        }
    }

    // ============================================================================
    // ENHANCED VEDHA EFFECT DETERMINATION
    // ============================================================================

    /**
     * Determines the vedha effect considering:
     *   - Planet's natural benefic/malefic nature
     *   - Whether the planet is retrograde (amplifies effect)
     *   - Nakshatra compatibility between transit and birth
     *   - Birth point type sensitivity
     */
    private fun determineEnhancedVedhaEffect(
        planet: Planet,
        transitNakshatra: Nakshatra,
        birthPoint: BirthPoint,
        isRetrograde: Boolean
    ): VedhaEffect {
        val baseEffect = when (planet) {
            Planet.JUPITER -> VedhaEffect.BENEFIC
            Planet.VENUS -> VedhaEffect.BENEFIC
            Planet.MERCURY -> if (transitNakshatra.ruler in VedicAstrologyUtils.NATURAL_BENEFICS)
                VedhaEffect.BENEFIC else VedhaEffect.NEUTRAL
            Planet.MOON -> if (birthPoint.pointType == BirthPointType.NAKSHATRA)
                VedhaEffect.MIXED else VedhaEffect.NEUTRAL
            Planet.SUN -> if (transitNakshatra.ruler == Planet.SUN)
                VedhaEffect.BENEFIC else VedhaEffect.MIXED
            Planet.MARS -> VedhaEffect.MALEFIC
            Planet.SATURN -> VedhaEffect.MALEFIC
            Planet.RAHU -> VedhaEffect.MALEFIC
            Planet.KETU -> VedhaEffect.MALEFIC
            else -> VedhaEffect.NEUTRAL
        }

        // Retrograde intensifies the effect (both good and bad)
        // but for malefics, retrograde is considered more obstructive
        if (isRetrograde && baseEffect == VedhaEffect.MALEFIC) {
            return VedhaEffect.MALEFIC // Stays malefic but intensity is higher (handled in intensity calc)
        }
        if (isRetrograde && baseEffect == VedhaEffect.BENEFIC) {
            return VedhaEffect.MIXED // Retrograde benefic is less reliable
        }

        return baseEffect
    }

    // ============================================================================
    // VEDHA INTENSITY CALCULATION
    // ============================================================================

    /**
     * Calculates vedha intensity (0.0 - 1.0) based on multiple factors:
     *   - Planet's inherent strength as a vedha-giver
     *   - Vedha type (Full > 3/4 > Half > Quarter)
     *   - Vedha direction (Sammukha strongest)
     *   - Retrograde status (amplifies by 25%)
     *   - Degree proximity within nakshatra (center = strongest)
     *   - Birth point type weight (Nakshatra > Tithi > Vara > Name)
     */
    private fun calculateVedhaIntensity(
        planet: Planet,
        vedhaType: VedhaType,
        vedhaDirection: VedhaDirection,
        isRetrograde: Boolean,
        degreeInNakshatra: Double,
        birthPointType: BirthPointType
    ): Double {
        // Base intensity from planet
        val planetFactor = when (planet) {
            Planet.SATURN -> 0.95
            Planet.RAHU -> 0.90
            Planet.KETU -> 0.85
            Planet.JUPITER -> 0.80
            Planet.MARS -> 0.75
            Planet.SUN -> 0.60
            Planet.VENUS -> 0.55
            Planet.MERCURY -> 0.50
            Planet.MOON -> 0.45
            else -> 0.40
        }

        // Vedha type factor
        val typeFactor = when (vedhaType) {
            VedhaType.FULL -> 1.0
            VedhaType.THREE_QUARTER -> 0.75
            VedhaType.HALF -> 0.50
            VedhaType.QUARTER -> 0.25
            VedhaType.NONE -> 0.0
        }

        // Direction factor
        val directionFactor = vedhaDirection.strengthMultiplier

        // Retrograde amplification
        val retroFactor = if (isRetrograde) 1.25 else 1.0

        // Degree proximity: center of nakshatra (6.67 degrees) is strongest
        val nakshatraSpan = 360.0 / 27.0
        val centerDistance = abs(degreeInNakshatra - (nakshatraSpan / 2.0))
        val proximityFactor = 1.0 - (centerDistance / (nakshatraSpan / 2.0)) * 0.3

        // Birth point type weight
        val birthPointWeight = when (birthPointType) {
            BirthPointType.NAKSHATRA -> 1.0
            BirthPointType.TITHI -> 0.7
            BirthPointType.VARA -> 0.6
            BirthPointType.NAME_LETTER -> 0.5
        }

        val rawIntensity = planetFactor * typeFactor * directionFactor * retroFactor *
            proximityFactor * birthPointWeight

        return rawIntensity.coerceIn(0.0, 1.0)
    }

    // ============================================================================
    // NET EFFECT & OVERALL IMPACT CALCULATION
    // ============================================================================

    /**
     * Calculates the net effect on a birth point from all vedhas hitting it.
     */
    private fun calculateNetEffect(vedhas: List<ActiveVedha>): VedhaEffect {
        if (vedhas.isEmpty()) return VedhaEffect.NEUTRAL

        var beneficScore = 0.0
        var maleficScore = 0.0

        for (vedha in vedhas) {
            when (vedha.vedhaEffect) {
                VedhaEffect.BENEFIC -> beneficScore += vedha.intensity
                VedhaEffect.MALEFIC -> maleficScore += vedha.intensity
                VedhaEffect.MIXED -> {
                    beneficScore += vedha.intensity * 0.3
                    maleficScore += vedha.intensity * 0.5
                }
                VedhaEffect.NEUTRAL -> {
                    beneficScore += vedha.intensity * 0.2
                }
            }
        }

        return when {
            beneficScore > maleficScore * 1.5 -> VedhaEffect.BENEFIC
            maleficScore > beneficScore * 1.5 -> VedhaEffect.MALEFIC
            beneficScore > 0 || maleficScore > 0 -> VedhaEffect.MIXED
            else -> VedhaEffect.NEUTRAL
        }
    }

    /**
     * Calculates the overall SBC transit impact across all birth points.
     */
    private fun calculateOverallImpact(
        vedhas: List<ActiveVedha>,
        affectedPoints: List<AffectedBirthPoint>
    ): SBCImpact {
        if (vedhas.isEmpty()) {
            return SBCImpact(
                score = 70,
                level = ImpactLevel.NEUTRAL,
                summary = "No significant vedha activity on birth chart points.",
                beneficInfluences = emptyList(),
                maleficInfluences = emptyList(),
                keyRecommendations = listOf("Current transit period is relatively quiet in SBC terms.")
            )
        }

        val beneficVedhas = vedhas.filter { it.vedhaEffect == VedhaEffect.BENEFIC }
        val maleficVedhas = vedhas.filter { it.vedhaEffect == VedhaEffect.MALEFIC }

        val beneficIntensity = beneficVedhas.sumOf { it.intensity }
        val maleficIntensity = maleficVedhas.sumOf { it.intensity }

        val score = (70 + (beneficIntensity * 15) - (maleficIntensity * 20)).toInt().coerceIn(0, 100)

        val level = when {
            score >= 80 -> ImpactLevel.HIGHLY_FAVORABLE
            score >= 60 -> ImpactLevel.FAVORABLE
            score >= 45 -> ImpactLevel.NEUTRAL
            score >= 30 -> ImpactLevel.CHALLENGING
            else -> ImpactLevel.HIGHLY_CHALLENGING
        }

        val beneficInfluences = beneficVedhas.map { vedha ->
            "${vedha.transitPlanet.displayName} in ${vedha.transitNakshatra.displayName} " +
            "provides ${vedha.vedhaDirection.displayName} vedha support"
        }

        val maleficInfluences = maleficVedhas.map { vedha ->
            "${vedha.transitPlanet.displayName} in ${vedha.transitNakshatra.displayName} " +
            "creates ${vedha.vedhaDirection.displayName} vedha obstruction" +
            if (vedha.isRetrograde) " (retrograde - intensified)" else ""
        }

        val recommendations = mutableListOf<String>()
        val nakshatraPoints = affectedPoints.filter { it.pointType == BirthPointType.NAKSHATRA }
        if (nakshatraPoints.any { it.netEffect == VedhaEffect.MALEFIC }) {
            recommendations.add("Birth Nakshatra is under malefic vedha - exercise caution in important decisions")
        }
        if (nakshatraPoints.any { it.netEffect == VedhaEffect.BENEFIC }) {
            recommendations.add("Birth Nakshatra receives benefic vedha - favorable for new initiatives")
        }
        val varaPoints = affectedPoints.filter { it.pointType == BirthPointType.VARA }
        if (varaPoints.any { it.netEffect == VedhaEffect.MALEFIC }) {
            recommendations.add("Birth day vedha indicates potential health/energy fluctuations")
        }
        if (maleficVedhas.any { it.transitPlanet == Planet.SATURN }) {
            recommendations.add("Saturn vedha active - patience and discipline are key remedies")
        }
        if (maleficVedhas.any { it.transitPlanet == Planet.RAHU }) {
            recommendations.add("Rahu vedha active - avoid impulsive decisions and speculative ventures")
        }
        if (beneficVedhas.any { it.transitPlanet == Planet.JUPITER }) {
            recommendations.add("Jupiter vedha support - excellent period for spiritual and educational pursuits")
        }

        val summary = buildString {
            append("Overall SBC transit score: $score/100 ($level). ")
            append("${beneficVedhas.size} benefic and ${maleficVedhas.size} malefic vedhas active. ")
            val nakshatraVedhaCount = vedhas.count { v ->
                affectedPoints.any { bp ->
                    bp.pointType == BirthPointType.NAKSHATRA &&
                    bp.gridPosition == (v.targetCell.row to v.targetCell.col)
                }
            }
            if (nakshatraVedhaCount > 0) {
                append("$nakshatraVedhaCount vedha(s) directly affecting birth Nakshatra.")
            }
        }

        return SBCImpact(
            score = score,
            level = level,
            summary = summary,
            beneficInfluences = beneficInfluences,
            maleficInfluences = maleficInfluences,
            keyRecommendations = recommendations.take(5)
        )
    }

    // ============================================================================
    // VEDHA TIMELINE
    // ============================================================================

    /**
     * Builds a vedha timeline showing when each vedha activates and deactivates.
     * Based on the transiting planet's current position relative to nakshatra boundaries.
     *
     * For slow-moving planets (Saturn, Jupiter, Rahu/Ketu), the vedha can last
     * for extended periods. For fast-moving planets (Moon, Sun, Mercury, Venus, Mars),
     * the vedha is more transient.
     */
    private fun buildVedhaTimeline(
        transitPositions: List<PlanetPosition>,
        birthPoints: List<BirthPoint>
    ): List<VedhaTimelineEntry> {
        val timeline = mutableListOf<VedhaTimelineEntry>()
        val nakshatraSpan = 360.0 / 27.0

        for (transit in transitPositions.filter { it.planet in Planet.MAIN_PLANETS }) {
            val (transitNakshatra, _) = Nakshatra.fromLongitude(transit.longitude)
            val transitPos = nakshatraPositions[transitNakshatra] ?: continue

            // Check if this planet creates any vedha
            val hasVedha = birthPoints.any { bp ->
                calculateVedhaDirection(transitPos, bp.gridPosition) != null
            }

            if (!hasVedha) continue

            // Calculate position within nakshatra (0.0 to 1.0)
            val posInNakshatra = (transit.longitude % nakshatraSpan) / nakshatraSpan

            // Estimate days remaining in current nakshatra based on planet speed
            val dailyMotion = abs(transit.speed).coerceAtLeast(0.001)
            val degreesRemaining = nakshatraSpan * (1.0 - posInNakshatra)
            val daysRemaining = (degreesRemaining / dailyMotion).coerceIn(0.0, 365.0)

            // Estimate days since entry
            val degreesTraversed = nakshatraSpan * posInNakshatra
            val daysSinceEntry = (degreesTraversed / dailyMotion).coerceIn(0.0, 365.0)

            val phase = when {
                posInNakshatra < 0.2 -> VedhaPhase.ACTIVATING
                posInNakshatra > 0.8 -> VedhaPhase.DEACTIVATING
                else -> VedhaPhase.PEAK
            }

            val strength = when (phase) {
                VedhaPhase.ACTIVATING -> 0.3 + (posInNakshatra * 3.5) // Ramp up
                VedhaPhase.PEAK -> 1.0
                VedhaPhase.DEACTIVATING -> 1.0 - ((posInNakshatra - 0.8) * 5.0) // Ramp down
            }.coerceIn(0.0, 1.0)

            timeline.add(
                VedhaTimelineEntry(
                    planet = transit.planet,
                    nakshatra = transitNakshatra,
                    phase = phase,
                    currentStrength = strength,
                    estimatedDaysRemaining = daysRemaining,
                    estimatedDaysSinceActivation = daysSinceEntry,
                    isRetrograde = transit.isRetrograde,
                    description = buildTimelineDescription(
                        transit.planet, transitNakshatra, phase,
                        daysRemaining, transit.isRetrograde
                    )
                )
            )
        }

        return timeline.sortedByDescending { it.currentStrength }
    }

    /**
     * Build a human-readable description for a timeline entry.
     */
    private fun buildTimelineDescription(
        planet: Planet,
        nakshatra: Nakshatra,
        phase: VedhaPhase,
        daysRemaining: Double,
        isRetrograde: Boolean
    ): String {
        val phaseDesc = when (phase) {
            VedhaPhase.ACTIVATING -> "entering and strengthening"
            VedhaPhase.PEAK -> "at peak strength"
            VedhaPhase.DEACTIVATING -> "weakening and exiting"
        }
        val retroNote = if (isRetrograde) " (retrograde - may re-enter)" else ""
        val daysStr = when {
            daysRemaining < 1 -> "less than a day"
            daysRemaining < 2 -> "about 1 day"
            daysRemaining < 30 -> "about ${daysRemaining.toInt()} days"
            daysRemaining < 365 -> "about ${(daysRemaining / 30).toInt()} months"
            else -> "over a year"
        }
        return "${planet.displayName} is $phaseDesc in ${nakshatra.displayName}. " +
            "Vedha continues for $daysStr$retroNote."
    }

    // ============================================================================
    // GRID STATE BUILDER
    // ============================================================================

    /**
     * Builds the complete 9x9 grid state with vedha information for each cell.
     */
    private fun buildGridState(
        grid: Array<Array<ChakraCell>>,
        birthPoints: List<BirthPoint>,
        activeVedhas: List<ActiveVedha>
    ): Array<Array<SBCCellState>> {
        return Array(9) { row ->
            Array(9) { col ->
                val cell = grid[row][col]
                val isNatal = birthPoints.any { it.gridPosition == (row to col) }

                val cellVedhas = activeVedhas
                    .filter { it.targetCell.row == row && it.targetCell.col == col }
                    .map { vedha ->
                        CellVedhaInfo(
                            fromPlanet = vedha.transitPlanet,
                            direction = vedha.vedhaDirection,
                            intensity = vedha.intensity,
                            isBenefic = vedha.vedhaEffect == VedhaEffect.BENEFIC
                        )
                    }

                val overallIntensity = cellVedhas.sumOf { it.intensity }.coerceIn(0.0, 1.0)

                SBCCellState(
                    cell = cell,
                    isNatalPoint = isNatal,
                    activeVedhas = cellVedhas,
                    overallIntensity = overallIntensity,
                    isHighlighted = isNatal || cellVedhas.isNotEmpty()
                )
            }
        }
    }

    // ============================================================================
    // INTERPRETATION BUILDERS
    // ============================================================================

    /**
     * Build interpretation for a specific birth point under vedha.
     */
    private fun buildBirthPointInterpretation(
        birthPoint: BirthPoint,
        vedhas: List<ActiveVedha>
    ): String {
        if (vedhas.isEmpty()) {
            return "${birthPoint.label} (${birthPoint.pointType.displayName}) is currently free from significant vedha influence."
        }

        val beneficCount = vedhas.count { it.vedhaEffect == VedhaEffect.BENEFIC }
        val maleficCount = vedhas.count { it.vedhaEffect == VedhaEffect.MALEFIC }

        val pointContext = when (birthPoint.pointType) {
            BirthPointType.NAKSHATRA -> "matters related to your core identity, health, and life direction"
            BirthPointType.TITHI -> "emotional well-being, finances, and mental peace"
            BirthPointType.VARA -> "daily energy levels, routine activities, and general vitality"
            BirthPointType.NAME_LETTER -> "personal reputation, social interactions, and name-related karma"
        }

        return buildString {
            append("${birthPoint.label} (${birthPoint.pointType.displayName}): ")
            append("$beneficCount benefic and $maleficCount malefic vedha(s) active, affecting $pointContext. ")

            val strongestVedha = vedhas.maxByOrNull { it.intensity }
            if (strongestVedha != null) {
                append("The strongest influence comes from ${strongestVedha.transitPlanet.displayName} ")
                append("(${strongestVedha.vedhaDirection.displayName}) ")
                append("with ${(strongestVedha.intensity * 100).toInt()}% intensity.")
            }
        }
    }

    // ============================================================================
    // INTERNAL DATA CLASSES
    // ============================================================================

    /**
     * Represents a birth chart point mapped to the SBC grid.
     */
    private data class BirthPoint(
        val pointType: BirthPointType,
        val gridPosition: Pair<Int, Int>,
        val label: String,
        val weight: Double
    )
}

// ============================================================================
// PUBLIC DATA CLASSES FOR TRANSIT VEDHA ANALYSIS
// ============================================================================

/**
 * Birth point type in the SBC grid.
 */
enum class BirthPointType(val displayName: String) {
    NAKSHATRA("Birth Nakshatra"),
    TITHI("Birth Tithi"),
    VARA("Birth Weekday"),
    NAME_LETTER("Name Letter")
}

/**
 * Impact level classification.
 */
enum class ImpactLevel(val displayName: String) {
    HIGHLY_FAVORABLE("Highly Favorable"),
    FAVORABLE("Favorable"),
    NEUTRAL("Neutral"),
    CHALLENGING("Challenging"),
    HIGHLY_CHALLENGING("Highly Challenging")
}

/**
 * Vedha phase in the timeline.
 */
enum class VedhaPhase(val displayName: String) {
    ACTIVATING("Activating"),
    PEAK("Peak"),
    DEACTIVATING("Deactivating")
}

/**
 * Complete SBC transit vedha analysis result.
 */
data class SBCTransitAnalysis(
    val activeVedhas: List<ActiveVedha>,
    val affectedBirthPoints: List<AffectedBirthPoint>,
    val vedhaTimeline: List<VedhaTimelineEntry>,
    val overallImpact: SBCImpact,
    val gridState: Array<Array<SarvatobhadraChakraCalculator.SBCCellState>>
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is SBCTransitAnalysis) return false
        return activeVedhas == other.activeVedhas &&
            affectedBirthPoints == other.affectedBirthPoints &&
            overallImpact == other.overallImpact
    }

    override fun hashCode(): Int {
        return activeVedhas.hashCode() * 31 +
            affectedBirthPoints.hashCode() * 17 +
            overallImpact.hashCode()
    }
}

/**
 * An active vedha created by a transiting planet on a birth point.
 */
data class ActiveVedha(
    val transitPlanet: Planet,
    val transitNakshatra: Nakshatra,
    val targetCell: SarvatobhadraChakraCalculator.ChakraCell,
    val vedhaDirection: SarvatobhadraChakraCalculator.VedhaDirection,
    val vedhaType: SarvatobhadraChakraCalculator.VedhaType,
    val vedhaEffect: SarvatobhadraChakraCalculator.VedhaEffect,
    val intensity: Double,
    val isRetrograde: Boolean
)

/**
 * A birth point that is affected by one or more transit vedhas.
 */
data class AffectedBirthPoint(
    val pointType: BirthPointType,
    val gridPosition: Pair<Int, Int>,
    val label: String,
    val vedhas: List<ActiveVedha>,
    val netEffect: SarvatobhadraChakraCalculator.VedhaEffect,
    val interpretation: String
)

/**
 * Timeline entry showing vedha activation/deactivation.
 */
data class VedhaTimelineEntry(
    val planet: Planet,
    val nakshatra: Nakshatra,
    val phase: VedhaPhase,
    val currentStrength: Double,
    val estimatedDaysRemaining: Double,
    val estimatedDaysSinceActivation: Double,
    val isRetrograde: Boolean,
    val description: String
)

/**
 * Overall SBC transit impact assessment.
 */
data class SBCImpact(
    val score: Int,
    val level: ImpactLevel,
    val summary: String,
    val beneficInfluences: List<String>,
    val maleficInfluences: List<String>,
    val keyRecommendations: List<String>
)
