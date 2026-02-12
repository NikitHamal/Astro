package com.astro.storm.ephemeris

import com.astro.storm.core.model.Nakshatra
import com.astro.storm.core.model.Planet
import com.astro.storm.core.model.PlanetPosition
import com.astro.storm.core.model.VedicChart
import java.time.DateTimeException
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
        if (timezone.isNullOrBlank()) return ZoneOffset.UTC
        return try {
            ZoneId.of(timezone.trim())
        } catch (_: DateTimeException) {
            val normalized = timezone.trim()
                .replace("UTC", "", ignoreCase = true)
                .replace("GMT", "", ignoreCase = true)
                .trim()
            if (normalized.isNotEmpty()) {
                runCatching { ZoneId.of("UTC$normalized") }.getOrElse { ZoneOffset.UTC }
            } else {
                ZoneOffset.UTC
            }
        }
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
}
