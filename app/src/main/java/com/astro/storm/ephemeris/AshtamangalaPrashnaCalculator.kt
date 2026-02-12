package com.astro.storm.ephemeris

import com.astro.storm.core.model.Planet
import com.astro.storm.core.model.PlanetPosition
import com.astro.storm.core.model.VedicChart
import com.astro.storm.core.model.ZodiacSign
import java.time.DateTimeException
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneId
import kotlin.math.abs
import kotlin.random.Random

/**
 * AshtamangalaPrashnaCalculator - Kerala Tradition 8-Fold Horary Divination
 *
 * Implements the authentic Kerala Ashtamangala Prashna system based on:
 * - Prashna Marga (Chapter on Ashtamangala)
 * - Kerala Jyotisha traditions
 * - Classical 8-position cowrie shell divination
 *
 * Features:
 * - Digital cowrie shell simulation with authentic randomization
 * - Eight directional deity positions (Agni, Indra, Yama, Nirriti, Varuna, Vayu, Kubera, Ishana)
 * - Question category routing with specialized interpretations
 * - Integration with Prashna chart for validation
 * - Timing predictions based on shell patterns
 * - Yes/No probability calculations
 * - Remedial measures based on dominant positions
 *
 * References: Prashna Marga, Kerala Jyotisha Paddhati
 */
object AshtamangalaPrashnaCalculator {

    // ============================================
    // DATA CLASSES
    // ============================================

    /**
     * Complete Ashtamangala reading result
     */
    data class AshtamangalaReading(
        val queryTime: LocalDateTime,
        val category: QueryCategory,
        val cowrieThrow: CowrieThrowResult,
        val positionAnalysis: List<PositionResult>,
        val dominantPosition: AshtamangalaPosition,
        val primaryIndication: IndicationStrength,
        val secondaryInfluences: List<String>,
        val yesNoProbability: YesNoProbability,
        val timingPrediction: TimingPrediction,
        val chartValidation: ChartValidation?,
        val remedies: List<Remedy>,
        val interpretation: ReadingInterpretation,
        val specialReadings: SpecialReadings?
    )

    /**
     * Cowrie shell throw result
     */
    data class CowrieThrowResult(
        val openShells: Int,           // Face up (mouth visible)
        val closedShells: Int,         // Face down
        val shellPattern: List<Boolean>, // Individual shell states (true = open)
        val numericalStrength: NumericalStrength,
        val interpretation: String
    )

    /**
     * Individual position result in the 8-fold system
     */
    data class PositionResult(
        val position: AshtamangalaPosition,
        val isActive: Boolean,
        val strength: Int,  // 0-100
        val deity: String,
        val direction: String,
        val element: ElementType,
        val signification: List<String>,
        val effectOnQuery: String
    )

    /**
     * Yes/No probability result
     */
    data class YesNoProbability(
        val yesPercentage: Int,
        val noPercentage: Int,
        val confidence: ConfidenceLevel,
        val reasoning: String
    )

    /**
     * Timing prediction
     */
    data class TimingPrediction(
        val category: TimingCategory,
        val description: String,
        val bestDay: DayOfWeek?,
        val avoidDay: DayOfWeek?,
        val suggestedTithi: String?
    )

    /**
     * Chart validation against Prashna chart
     */
    data class ChartValidation(
        val isSupporting: Boolean,
        val lagnaEffect: String,
        val moonEffect: String,
        val conflictAreas: List<String>,
        val strengthModifier: Int  // -20 to +20
    )

    /**
     * Remedial measures
     */
    data class Remedy(
        val type: RemedyType,
        val description: String,
        val deity: String?,
        val mantra: String?,
        val direction: String?,
        val color: String?,
        val day: DayOfWeek?
    )

    /**
     * Overall interpretation
     */
    data class ReadingInterpretation(
        val summary: String,
        val detailedAnalysis: String,
        val categorySpecific: String,
        val cautions: List<String>,
        val positiveFactors: List<String>,
        val negativeFactors: List<String>
    )

    /**
     * Special readings for specific query types
     */
    data class SpecialReadings(
        val lostObjectDirection: String?,
        val recoveryPossibility: Int?,
        val travelSafety: SafetyLevel?,
        val litigationSuccess: Int?,
        val marriageTiming: String?,
        val healthRecovery: String?
    )

    // ============================================
    // ENUMS
    // ============================================

    /**
     * Eight positions of Ashtamangala with their attributes
     */
    enum class AshtamangalaPosition(
        val number: Int,
        val deity: String,
        val deityNepali: String,
        val direction: String,
        val directionNepali: String,
        val element: ElementType,
        val rulingPlanet: Planet,
        val significations: List<String>
    ) {
        AGNI(
            1, "Agni", "अग्नि", "Southeast", "आग्नेय",
            ElementType.FIRE, Planet.MARS,
            listOf("Initiative", "Energy", "Passion", "Transformation", "New beginnings")
        ),
        INDRA(
            2, "Indra", "इन्द्र", "East", "पूर्व",
            ElementType.AIR, Planet.JUPITER,
            listOf("Success", "Authority", "Power", "Achievement", "Victory")
        ),
        YAMA(
            3, "Yama", "यम", "South", "दक्षिण",
            ElementType.EARTH, Planet.SATURN,
            listOf("Endings", "Karma", "Justice", "Life lessons", "Dharma")
        ),
        NIRRITI(
            4, "Nirriti", "निऋति", "Southwest", "नैऋत्य",
            ElementType.EARTH, Planet.RAHU,
            listOf("Obstacles", "Hidden enemies", "Dissolution", "Decay", "Challenges")
        ),
        VARUNA(
            5, "Varuna", "वरुण", "West", "पश्चिम",
            ElementType.WATER, Planet.SATURN,
            listOf("Emotions", "Intuition", "Healing", "Purification", "Justice")
        ),
        VAYU(
            6, "Vayu", "वायु", "Northwest", "वायव्य",
            ElementType.AIR, Planet.MOON,
            listOf("Communication", "Travel", "Change", "Movement", "Messages")
        ),
        KUBERA(
            7, "Kubera", "कुबेर", "North", "उत्तर",
            ElementType.EARTH, Planet.MERCURY,
            listOf("Wealth", "Prosperity", "Resources", "Stability", "Material gains")
        ),
        ISHANA(
            8, "Ishana", "ईशान", "Northeast", "ईशान",
            ElementType.ETHER, Planet.JUPITER,
            listOf("Spirituality", "Wisdom", "Divine grace", "Knowledge", "Blessings")
        );

        companion object {
            fun fromNumber(num: Int): AshtamangalaPosition = entries.first { it.number == num }
        }
    }

    /**
     * Query categories for Ashtamangala Prashna
     */
    enum class QueryCategory(
        val displayName: String,
        val nepaliName: String,
        val relevantPositions: List<AshtamangalaPosition>,
        val primaryHouse: Int
    ) {
        HEALTH("Health & Wellbeing", "स्वास्थ्य र कल्याण",
            listOf(AshtamangalaPosition.AGNI, AshtamangalaPosition.INDRA, AshtamangalaPosition.ISHANA), 1),
        WEALTH("Wealth & Finance", "धन र वित्त",
            listOf(AshtamangalaPosition.KUBERA, AshtamangalaPosition.INDRA, AshtamangalaPosition.AGNI), 2),
        RELATIONSHIP("Relationships", "सम्बन्धहरू",
            listOf(AshtamangalaPosition.VARUNA, AshtamangalaPosition.VAYU, AshtamangalaPosition.ISHANA), 7),
        CAREER("Career & Work", "क्यारियर र काम",
            listOf(AshtamangalaPosition.INDRA, AshtamangalaPosition.KUBERA, AshtamangalaPosition.AGNI), 10),
        TRAVEL("Travel & Movement", "यात्रा र गति",
            listOf(AshtamangalaPosition.VAYU, AshtamangalaPosition.VARUNA, AshtamangalaPosition.INDRA), 3),
        LOST_OBJECT("Lost Objects", "हराएका वस्तुहरू",
            listOf(AshtamangalaPosition.KUBERA, AshtamangalaPosition.NIRRITI, AshtamangalaPosition.VARUNA), 4),
        LITIGATION("Litigation & Disputes", "मुद्दा र विवाद",
            listOf(AshtamangalaPosition.YAMA, AshtamangalaPosition.INDRA, AshtamangalaPosition.NIRRITI), 6),
        SPIRITUAL("Spiritual Progress", "आध्यात्मिक प्रगति",
            listOf(AshtamangalaPosition.ISHANA, AshtamangalaPosition.VARUNA, AshtamangalaPosition.YAMA), 9)
    }

    enum class ElementType { FIRE, WATER, EARTH, AIR, ETHER }

    enum class IndicationStrength {
        VERY_FAVORABLE, FAVORABLE, MIXED, CHALLENGING, VERY_CHALLENGING, NEUTRAL
    }

    enum class NumericalStrength(val openShells: Int, val description: String) {
        DENIAL(0, "Total denial, delay, not now"),
        WEAK_AFFIRM(1, "Weak affirmation, much effort needed"),
        PARTIAL(2, "Partial success, obstacles remain"),
        CONDITIONAL(3, "Favorable with conditions"),
        BALANCED(4, "Balanced outcome, transformation"),
        GOOD(5, "Good success likely"),
        STRONG(6, "Strong positive indication"),
        VERY_FAVORABLE(7, "Very favorable outcome"),
        COMPLETE(8, "Complete success, divine blessing");

        companion object {
            fun fromOpenCount(count: Int): NumericalStrength = entries.first { it.openShells == count }
        }
    }

    enum class ConfidenceLevel { HIGH, MEDIUM, LOW }

    enum class TimingCategory {
        IMMEDIATE, SHORT_TERM, MEDIUM_TERM, LONG_TERM, DELAYED, UNCERTAIN
    }

    enum class SafetyLevel { VERY_SAFE, SAFE, MODERATE, RISKY, VERY_RISKY }

    enum class RemedyType { DEITY_WORSHIP, MANTRA, DIRECTION, COLOR, DAY, DONATION, ACTION }

    // ============================================
    // MAIN CALCULATION FUNCTIONS
    // ============================================

    /**
     * Generate complete Ashtamangala reading
     */
    fun generateReading(
        category: QueryCategory,
        chart: VedicChart? = null,
        seedValue: Long? = null
    ): AshtamangalaReading {
        val queryZone = resolveZoneId(chart?.birthData?.timezone)
        val queryTime = LocalDateTime.now(queryZone)

        // Generate cowrie throw
        val cowrieThrow = simulateCowrieThrow(seedValue)

        // Analyze all 8 positions
        val positionAnalysis = analyzePositions(cowrieThrow, category)

        // Find dominant position
        val dominantPosition = findDominantPosition(positionAnalysis, category)

        // Calculate primary indication
        val primaryIndication = calculatePrimaryIndication(cowrieThrow, positionAnalysis, category)

        // Get secondary influences
        val secondaryInfluences = getSecondaryInfluences(positionAnalysis, category)

        // Calculate Yes/No probability
        val yesNoProbability = calculateYesNoProbability(cowrieThrow, positionAnalysis, category)

        // Predict timing
        val timingPrediction = predictTiming(cowrieThrow, dominantPosition, category)

        // Validate against chart if provided
        val chartValidation = chart?.let { validateWithChart(it, cowrieThrow, category) }

        // Generate remedies
        val remedies = generateRemedies(dominantPosition, primaryIndication, category)

        // Create interpretation
        val interpretation = createInterpretation(
            cowrieThrow, positionAnalysis, dominantPosition,
            primaryIndication, category, chartValidation
        )

        // Generate special readings for specific categories
        val specialReadings = generateSpecialReadings(category, cowrieThrow, positionAnalysis)

        return AshtamangalaReading(
            queryTime = queryTime,
            category = category,
            cowrieThrow = cowrieThrow,
            positionAnalysis = positionAnalysis,
            dominantPosition = dominantPosition,
            primaryIndication = primaryIndication,
            secondaryInfluences = secondaryInfluences,
            yesNoProbability = yesNoProbability,
            timingPrediction = timingPrediction,
            chartValidation = chartValidation,
            remedies = remedies,
            interpretation = interpretation,
            specialReadings = specialReadings
        )
    }

    private fun resolveZoneId(timezone: String?): ZoneId {
        if (timezone.isNullOrBlank()) return ZoneId.systemDefault()
        return try {
            ZoneId.of(timezone.trim())
        } catch (_: DateTimeException) {
            val normalized = timezone.trim()
                .replace("UTC", "", ignoreCase = true)
                .replace("GMT", "", ignoreCase = true)
                .trim()
            if (normalized.isNotEmpty()) {
                runCatching { ZoneId.of("UTC$normalized") }.getOrElse { ZoneId.systemDefault() }
            } else {
                ZoneId.systemDefault()
            }
        }
    }

    /**
     * Simulate cowrie shell throw with authentic randomization
     */
    fun simulateCowrieThrow(seedValue: Long? = null): CowrieThrowResult {
        val random = seedValue?.let { Random(it) } ?: Random(System.nanoTime())

        // Generate 8 shell states (true = open/face up)
        val shellPattern = List(8) { random.nextDouble() > 0.5 }
        val openShells = shellPattern.count { it }
        val closedShells = 8 - openShells

        val numericalStrength = NumericalStrength.fromOpenCount(openShells)
        val interpretation = getShellInterpretation(openShells)

        return CowrieThrowResult(
            openShells = openShells,
            closedShells = closedShells,
            shellPattern = shellPattern,
            numericalStrength = numericalStrength,
            interpretation = interpretation
        )
    }

    /**
     * Analyze all 8 positions based on cowrie throw
     */
    private fun analyzePositions(
        cowrieThrow: CowrieThrowResult,
        category: QueryCategory
    ): List<PositionResult> {
        return AshtamangalaPosition.entries.mapIndexed { index, position ->
            val isActive = cowrieThrow.shellPattern.getOrElse(index) { false }
            val isRelevant = category.relevantPositions.contains(position)

            // Calculate strength (0-100)
            val baseStrength = if (isActive) 60 else 20
            val relevanceBonus = if (isRelevant) 25 else 0
            val positionBonus = when {
                position == AshtamangalaPosition.INDRA && cowrieThrow.openShells >= 5 -> 15
                position == AshtamangalaPosition.NIRRITI && cowrieThrow.openShells <= 2 -> 15
                position == AshtamangalaPosition.KUBERA && category == QueryCategory.WEALTH -> 10
                position == AshtamangalaPosition.ISHANA && category == QueryCategory.SPIRITUAL -> 10
                else -> 0
            }
            val strength = (baseStrength + relevanceBonus + positionBonus).coerceIn(0, 100)

            val effectOnQuery = getPositionEffect(position, isActive, category)

            PositionResult(
                position = position,
                isActive = isActive,
                strength = strength,
                deity = position.deity,
                direction = position.direction,
                element = position.element,
                signification = position.significations,
                effectOnQuery = effectOnQuery
            )
        }
    }

    /**
     * Find the dominant position for this reading
     */
    private fun findDominantPosition(
        positionAnalysis: List<PositionResult>,
        category: QueryCategory
    ): AshtamangalaPosition {
        // Filter active positions, prioritize by relevance to category
        val activePositions = positionAnalysis.filter { it.isActive }

        if (activePositions.isEmpty()) {
            // If no active positions, return the most relevant for category
            return category.relevantPositions.first()
        }

        // Find highest strength among category-relevant positions first
        val relevantActive = activePositions.filter {
            category.relevantPositions.contains(it.position)
        }

        return if (relevantActive.isNotEmpty()) {
            relevantActive.maxByOrNull { it.strength }?.position ?: activePositions.first().position
        } else {
            activePositions.maxByOrNull { it.strength }?.position ?: activePositions.first().position
        }
    }

    /**
     * Calculate primary indication strength
     */
    private fun calculatePrimaryIndication(
        cowrieThrow: CowrieThrowResult,
        positionAnalysis: List<PositionResult>,
        category: QueryCategory
    ): IndicationStrength {
        val openCount = cowrieThrow.openShells
        val relevantActiveCount = positionAnalysis.count {
            it.isActive && category.relevantPositions.contains(it.position)
        }
        val nirritiActive = positionAnalysis.find {
            it.position == AshtamangalaPosition.NIRRITI
        }?.isActive == true

        return when {
            openCount >= 7 && !nirritiActive -> IndicationStrength.VERY_FAVORABLE
            openCount >= 5 && relevantActiveCount >= 2 -> IndicationStrength.FAVORABLE
            openCount == 4 -> IndicationStrength.MIXED
            openCount <= 2 && nirritiActive -> IndicationStrength.VERY_CHALLENGING
            openCount <= 3 -> IndicationStrength.CHALLENGING
            else -> IndicationStrength.NEUTRAL
        }
    }

    /**
     * Get secondary influences from position analysis
     */
    private fun getSecondaryInfluences(
        positionAnalysis: List<PositionResult>,
        category: QueryCategory
    ): List<String> {
        val influences = mutableListOf<String>()

        val activePositions = positionAnalysis.filter { it.isActive }

        // Element influences
        val activeElements = activePositions.map { it.element }.distinct()
        if (ElementType.FIRE in activeElements) {
            influences.add("Fire element active - Energy and transformation")
        }
        if (ElementType.WATER in activeElements) {
            influences.add("Water element active - Emotions and intuition")
        }
        if (ElementType.EARTH in activeElements) {
            influences.add("Earth element active - Stability and material matters")
        }
        if (ElementType.AIR in activeElements) {
            influences.add("Air element active - Communication and change")
        }
        if (ElementType.ETHER in activeElements) {
            influences.add("Ether element active - Divine grace and wisdom")
        }

        // Special combinations
        if (AshtamangalaPosition.INDRA in activePositions.map { it.position } &&
            AshtamangalaPosition.KUBERA in activePositions.map { it.position }) {
            influences.add("Indra-Kubera combination: Success with wealth")
        }

        if (AshtamangalaPosition.ISHANA in activePositions.map { it.position } &&
            AshtamangalaPosition.AGNI in activePositions.map { it.position }) {
            influences.add("Ishana-Agni combination: Divine blessings with energy")
        }

        return influences.take(5)
    }

    /**
     * Calculate Yes/No probability
     */
    private fun calculateYesNoProbability(
        cowrieThrow: CowrieThrowResult,
        positionAnalysis: List<PositionResult>,
        category: QueryCategory
    ): YesNoProbability {
        val baseYes = when (cowrieThrow.openShells) {
            0 -> 5
            1 -> 15
            2 -> 25
            3 -> 40
            4 -> 50
            5 -> 65
            6 -> 75
            7 -> 85
            8 -> 95
            else -> 50
        }

        // Adjust based on category-relevant positions
        val relevantActive = positionAnalysis.count {
            it.isActive && category.relevantPositions.contains(it.position)
        }
        val relevantBonus = relevantActive * 5

        // Adjust for Nirriti (obstacles)
        val nirritiPenalty = if (positionAnalysis.find {
            it.position == AshtamangalaPosition.NIRRITI
        }?.isActive == true) -10 else 0

        val yesPercent = (baseYes + relevantBonus + nirritiPenalty).coerceIn(5, 95)
        val noPercent = 100 - yesPercent

        val confidence = when {
            cowrieThrow.openShells <= 1 || cowrieThrow.openShells >= 7 -> ConfidenceLevel.HIGH
            cowrieThrow.openShells in 3..5 -> ConfidenceLevel.MEDIUM
            else -> ConfidenceLevel.LOW
        }

        val reasoning = buildString {
            append("Based on ${cowrieThrow.openShells} open shells")
            if (relevantActive > 0) append(", $relevantActive relevant positions active")
            if (nirritiPenalty != 0) append(", obstacle influence present")
        }

        return YesNoProbability(
            yesPercentage = yesPercent,
            noPercentage = noPercent,
            confidence = confidence,
            reasoning = reasoning
        )
    }

    /**
     * Predict timing based on reading
     */
    private fun predictTiming(
        cowrieThrow: CowrieThrowResult,
        dominantPosition: AshtamangalaPosition,
        category: QueryCategory
    ): TimingPrediction {
        val timingCategory = when (cowrieThrow.openShells) {
            7, 8 -> TimingCategory.IMMEDIATE
            5, 6 -> TimingCategory.SHORT_TERM
            3, 4 -> TimingCategory.MEDIUM_TERM
            1, 2 -> TimingCategory.LONG_TERM
            0 -> TimingCategory.DELAYED
            else -> TimingCategory.UNCERTAIN
        }

        val description = when (timingCategory) {
            TimingCategory.IMMEDIATE -> "Results expected within 1-7 days"
            TimingCategory.SHORT_TERM -> "Results expected within 1-4 weeks"
            TimingCategory.MEDIUM_TERM -> "Results expected within 1-3 months"
            TimingCategory.LONG_TERM -> "Results expected within 3-12 months"
            TimingCategory.DELAYED -> "Results delayed beyond 1 year or significant obstacles"
            TimingCategory.UNCERTAIN -> "Timing unclear, monitor developments"
        }

        // Best day based on dominant position's ruling planet
        val bestDay = when (dominantPosition.rulingPlanet) {
            Planet.SUN -> DayOfWeek.SUNDAY
            Planet.MOON -> DayOfWeek.MONDAY
            Planet.MARS -> DayOfWeek.TUESDAY
            Planet.MERCURY -> DayOfWeek.WEDNESDAY
            Planet.JUPITER -> DayOfWeek.THURSDAY
            Planet.VENUS -> DayOfWeek.FRIDAY
            Planet.SATURN -> DayOfWeek.SATURDAY
            else -> null
        }

        // Avoid day based on opposite energy
        val avoidDay = when (dominantPosition) {
            AshtamangalaPosition.NIRRITI -> DayOfWeek.TUESDAY
            AshtamangalaPosition.YAMA -> DayOfWeek.SATURDAY
            else -> null
        }

        return TimingPrediction(
            category = timingCategory,
            description = description,
            bestDay = bestDay,
            avoidDay = avoidDay,
            suggestedTithi = getSuggestedTithi(dominantPosition)
        )
    }

    /**
     * Validate reading with Prashna chart
     */
    private fun validateWithChart(
        chart: VedicChart,
        cowrieThrow: CowrieThrowResult,
        category: QueryCategory
    ): ChartValidation {
        val ascendantSign = ZodiacSign.fromLongitude(chart.ascendant)
        val moonPosition = chart.planetPositions.find { it.planet == Planet.MOON }

        // Check if chart supports the shell reading
        val lagnaStrength = when {
            ascendantSign.ruler in listOf(Planet.JUPITER, Planet.VENUS) -> 10
            ascendantSign.ruler in listOf(Planet.SATURN, Planet.MARS) && cowrieThrow.openShells < 4 -> -5
            else -> 0
        }

        val moonStrength = moonPosition?.let {
            when {
                it.house in listOf(1, 4, 7, 10) && cowrieThrow.openShells >= 5 -> 10
                it.house in listOf(6, 8, 12) && cowrieThrow.openShells < 4 -> -10
                else -> 0
            }
        } ?: 0

        val strengthModifier = (lagnaStrength + moonStrength).coerceIn(-20, 20)
        val isSupporting = strengthModifier >= 0

        val conflictAreas = mutableListOf<String>()
        if (lagnaStrength < 0) conflictAreas.add("Lagna lord weakens positive indications")
        if (moonStrength < 0) conflictAreas.add("Moon placement suggests obstacles")

        val lagnaEffect = "Lagna in ${ascendantSign.displayName} (${ascendantSign.ruler.displayName}): " +
            if (lagnaStrength >= 0) "Supports reading" else "Requires caution"

        val moonEffect = moonPosition?.let {
            "Moon in House ${it.house} (${it.sign.displayName}): " +
                if (moonStrength >= 0) "Favorable placement" else "Challenging placement"
        } ?: "Moon position unavailable"

        return ChartValidation(
            isSupporting = isSupporting,
            lagnaEffect = lagnaEffect,
            moonEffect = moonEffect,
            conflictAreas = conflictAreas,
            strengthModifier = strengthModifier
        )
    }

    /**
     * Generate remedial measures
     */
    private fun generateRemedies(
        dominantPosition: AshtamangalaPosition,
        indication: IndicationStrength,
        category: QueryCategory
    ): List<Remedy> {
        val remedies = mutableListOf<Remedy>()

        // Deity worship based on dominant position
        remedies.add(Remedy(
            type = RemedyType.DEITY_WORSHIP,
            description = "Worship ${dominantPosition.deity} for blessings",
            deity = dominantPosition.deity,
            mantra = getDeityMantra(dominantPosition),
            direction = dominantPosition.direction,
            color = null,
            day = getDayForPosition(dominantPosition)
        ))

        // Direction-based remedy
        remedies.add(Remedy(
            type = RemedyType.DIRECTION,
            description = "Face ${dominantPosition.direction} during prayers",
            deity = null,
            mantra = null,
            direction = dominantPosition.direction,
            color = null,
            day = null
        ))

        // Color remedy based on element
        val colorRemedy = getElementColor(dominantPosition.element)
        remedies.add(Remedy(
            type = RemedyType.COLOR,
            description = "Wear or use $colorRemedy for auspiciousness",
            deity = null,
            mantra = null,
            direction = null,
            color = colorRemedy,
            day = null
        ))

        // Additional remedy for challenging indications
        if (indication in listOf(IndicationStrength.CHALLENGING, IndicationStrength.VERY_CHALLENGING)) {
            remedies.add(Remedy(
                type = RemedyType.DONATION,
                description = getDonationRemedy(category),
                deity = null,
                mantra = null,
                direction = null,
                color = null,
                day = DayOfWeek.SATURDAY
            ))
        }

        return remedies
    }

    /**
     * Create comprehensive interpretation
     */
    private fun createInterpretation(
        cowrieThrow: CowrieThrowResult,
        positionAnalysis: List<PositionResult>,
        dominantPosition: AshtamangalaPosition,
        indication: IndicationStrength,
        category: QueryCategory,
        chartValidation: ChartValidation?
    ): ReadingInterpretation {
        val activePositions = positionAnalysis.filter { it.isActive }

        val summary = buildString {
            append("The Ashtamangala reading for ${category.displayName} shows ")
            append(when (indication) {
                IndicationStrength.VERY_FAVORABLE -> "highly auspicious indications"
                IndicationStrength.FAVORABLE -> "favorable prospects"
                IndicationStrength.MIXED -> "mixed results requiring careful action"
                IndicationStrength.CHALLENGING -> "challenges to be overcome"
                IndicationStrength.VERY_CHALLENGING -> "significant obstacles ahead"
                IndicationStrength.NEUTRAL -> "neutral circumstances"
            })
            append(". ")
            append("${dominantPosition.deity} (${dominantPosition.direction}) dominates this reading.")
        }

        val detailedAnalysis = buildString {
            append("With ${cowrieThrow.openShells} shells open, ")
            append(cowrieThrow.interpretation)
            append(" ")
            append("Active positions: ${activePositions.joinToString { it.deity }}. ")
            chartValidation?.let {
                append(if (it.isSupporting)
                    "The Prashna chart supports this reading. "
                    else "The Prashna chart suggests some modifications. "
                )
            }
        }

        val categorySpecific = getCategorySpecificInterpretation(category, indication, dominantPosition)

        val positiveFactors = mutableListOf<String>()
        val negativeFactors = mutableListOf<String>()

        if (cowrieThrow.openShells >= 5) positiveFactors.add("Strong shell count (${cowrieThrow.openShells}/8)")
        if (AshtamangalaPosition.INDRA in activePositions.map { it.position })
            positiveFactors.add("Indra active - success energy present")
        if (AshtamangalaPosition.KUBERA in activePositions.map { it.position })
            positiveFactors.add("Kubera active - prosperity influence")
        if (AshtamangalaPosition.ISHANA in activePositions.map { it.position })
            positiveFactors.add("Ishana active - divine blessings")

        if (cowrieThrow.openShells <= 2) negativeFactors.add("Low shell count indicates delays")
        if (AshtamangalaPosition.NIRRITI in activePositions.map { it.position })
            negativeFactors.add("Nirriti active - watch for hidden obstacles")
        if (AshtamangalaPosition.YAMA in activePositions.map { it.position } && category != QueryCategory.SPIRITUAL)
            negativeFactors.add("Yama active - endings or karmic lessons possible")

        val cautions = mutableListOf<String>()
        if (indication in listOf(IndicationStrength.CHALLENGING, IndicationStrength.VERY_CHALLENGING)) {
            cautions.add("Proceed with extra care and preparation")
            cautions.add("Consider timing carefully before major actions")
        }
        if (chartValidation?.isSupporting == false) {
            cautions.addAll(chartValidation.conflictAreas)
        }

        return ReadingInterpretation(
            summary = summary,
            detailedAnalysis = detailedAnalysis,
            categorySpecific = categorySpecific,
            cautions = cautions,
            positiveFactors = positiveFactors,
            negativeFactors = negativeFactors
        )
    }

    /**
     * Generate special readings for specific categories
     */
    private fun generateSpecialReadings(
        category: QueryCategory,
        cowrieThrow: CowrieThrowResult,
        positionAnalysis: List<PositionResult>
    ): SpecialReadings? {
        return when (category) {
            QueryCategory.LOST_OBJECT -> {
                val dominantActive = positionAnalysis.filter { it.isActive }.maxByOrNull { it.strength }
                SpecialReadings(
                    lostObjectDirection = dominantActive?.direction ?: "Unknown",
                    recoveryPossibility = when {
                        cowrieThrow.openShells >= 6 -> 85
                        cowrieThrow.openShells >= 4 -> 60
                        cowrieThrow.openShells >= 2 -> 35
                        else -> 15
                    },
                    travelSafety = null,
                    litigationSuccess = null,
                    marriageTiming = null,
                    healthRecovery = null
                )
            }
            QueryCategory.TRAVEL -> {
                SpecialReadings(
                    lostObjectDirection = null,
                    recoveryPossibility = null,
                    travelSafety = when {
                        cowrieThrow.openShells >= 6 -> SafetyLevel.VERY_SAFE
                        cowrieThrow.openShells >= 4 -> SafetyLevel.SAFE
                        cowrieThrow.openShells == 3 -> SafetyLevel.MODERATE
                        cowrieThrow.openShells >= 1 -> SafetyLevel.RISKY
                        else -> SafetyLevel.VERY_RISKY
                    },
                    litigationSuccess = null,
                    marriageTiming = null,
                    healthRecovery = null
                )
            }
            QueryCategory.LITIGATION -> {
                val yamaActive = positionAnalysis.find {
                    it.position == AshtamangalaPosition.YAMA
                }?.isActive == true
                SpecialReadings(
                    lostObjectDirection = null,
                    recoveryPossibility = null,
                    travelSafety = null,
                    litigationSuccess = when {
                        cowrieThrow.openShells >= 6 && !yamaActive -> 80
                        cowrieThrow.openShells >= 5 -> 65
                        cowrieThrow.openShells >= 3 -> 45
                        else -> 25
                    },
                    marriageTiming = null,
                    healthRecovery = null
                )
            }
            QueryCategory.RELATIONSHIP -> {
                SpecialReadings(
                    lostObjectDirection = null,
                    recoveryPossibility = null,
                    travelSafety = null,
                    litigationSuccess = null,
                    marriageTiming = when {
                        cowrieThrow.openShells >= 7 -> "Within 3 months"
                        cowrieThrow.openShells >= 5 -> "Within 6 months"
                        cowrieThrow.openShells >= 3 -> "Within 1 year"
                        else -> "Delayed, remedies recommended"
                    },
                    healthRecovery = null
                )
            }
            QueryCategory.HEALTH -> {
                SpecialReadings(
                    lostObjectDirection = null,
                    recoveryPossibility = null,
                    travelSafety = null,
                    litigationSuccess = null,
                    marriageTiming = null,
                    healthRecovery = when {
                        cowrieThrow.openShells >= 7 -> "Quick recovery expected"
                        cowrieThrow.openShells >= 5 -> "Gradual improvement"
                        cowrieThrow.openShells >= 3 -> "Requires treatment and patience"
                        else -> "Chronic condition, seek multiple opinions"
                    }
                )
            }
            else -> null
        }
    }

    // ============================================
    // HELPER FUNCTIONS
    // ============================================

    private fun getShellInterpretation(openCount: Int): String = when (openCount) {
        0 -> "Complete denial. This is not the time for this endeavor. Wait for better circumstances."
        1 -> "Weak affirmation. Success possible only with tremendous effort and patience."
        2 -> "Partial success expected. Significant obstacles remain. Proceed cautiously."
        3 -> "Favorable outcome possible with conditions. Some compromise may be needed."
        4 -> "Balanced situation. Transformation is indicated. Stay adaptable."
        5 -> "Good prospects for success. Favorable conditions developing."
        6 -> "Strong positive indication. Move forward with confidence."
        7 -> "Very favorable outcome expected. Excellent timing for action."
        8 -> "Complete success indicated. Divine blessings present. Proceed with full confidence."
        else -> "Interpret based on overall patterns."
    }

    private fun getPositionEffect(
        position: AshtamangalaPosition,
        isActive: Boolean,
        category: QueryCategory
    ): String {
        val relevance = if (category.relevantPositions.contains(position)) "highly relevant" else "secondary"
        val state = if (isActive) "active" else "inactive"

        return when (position) {
            AshtamangalaPosition.AGNI -> if (isActive)
                "Fire energy ignites your query - transformation and initiative"
                else "Fire energy dormant - build momentum gradually"
            AshtamangalaPosition.INDRA -> if (isActive)
                "Success energy strongly present - victory indicated"
                else "Authority support needed - seek endorsements"
            AshtamangalaPosition.YAMA -> if (isActive)
                "Karmic forces at play - dharmic approach essential"
                else "Karmic debts not blocking - favorable for progress"
            AshtamangalaPosition.NIRRITI -> if (isActive)
                "Hidden obstacles present - be vigilant"
                else "Obstacle energies subdued - clear path ahead"
            AshtamangalaPosition.VARUNA -> if (isActive)
                "Emotional clarity present - trust intuition"
                else "Emotional factors neutral - use logic"
            AshtamangalaPosition.VAYU -> if (isActive)
                "Movement and change favored - communication key"
                else "Stability preferred - avoid hasty changes"
            AshtamangalaPosition.KUBERA -> if (isActive)
                "Wealth energies supportive - material success"
                else "Financial patience needed - growth takes time"
            AshtamangalaPosition.ISHANA -> if (isActive)
                "Divine grace present - spiritual support strong"
                else "Seek divine blessings - worship recommended"
        }
    }

    private fun getDeityMantra(position: AshtamangalaPosition): String = when (position) {
        AshtamangalaPosition.AGNI -> "Om Agnaye Namaha"
        AshtamangalaPosition.INDRA -> "Om Indraya Namaha"
        AshtamangalaPosition.YAMA -> "Om Yamaya Namaha"
        AshtamangalaPosition.NIRRITI -> "Om Nirritaye Namaha"
        AshtamangalaPosition.VARUNA -> "Om Varunaya Namaha"
        AshtamangalaPosition.VAYU -> "Om Vayave Namaha"
        AshtamangalaPosition.KUBERA -> "Om Kuberaya Namaha"
        AshtamangalaPosition.ISHANA -> "Om Ishanaya Namaha"
    }

    private fun getDayForPosition(position: AshtamangalaPosition): DayOfWeek = when (position.rulingPlanet) {
        Planet.SUN -> DayOfWeek.SUNDAY
        Planet.MOON -> DayOfWeek.MONDAY
        Planet.MARS -> DayOfWeek.TUESDAY
        Planet.MERCURY -> DayOfWeek.WEDNESDAY
        Planet.JUPITER -> DayOfWeek.THURSDAY
        Planet.VENUS -> DayOfWeek.FRIDAY
        Planet.SATURN, Planet.RAHU -> DayOfWeek.SATURDAY
        else -> DayOfWeek.THURSDAY
    }

    private fun getElementColor(element: ElementType): String = when (element) {
        ElementType.FIRE -> "Red or Orange"
        ElementType.WATER -> "White or Light Blue"
        ElementType.EARTH -> "Yellow or Brown"
        ElementType.AIR -> "Green or Light colors"
        ElementType.ETHER -> "Violet or Saffron"
    }

    private fun getDonationRemedy(category: QueryCategory): String = when (category) {
        QueryCategory.HEALTH -> "Donate medicines or help the sick"
        QueryCategory.WEALTH -> "Feed the poor or donate to charitable causes"
        QueryCategory.RELATIONSHIP -> "Support marriage of the needy"
        QueryCategory.CAREER -> "Help someone find employment"
        QueryCategory.LITIGATION -> "Support legal aid for the poor"
        QueryCategory.LOST_OBJECT -> "Donate items to those in need"
        QueryCategory.TRAVEL -> "Support pilgrims or travelers"
        QueryCategory.SPIRITUAL -> "Donate to religious institutions"
    }

    private fun getSuggestedTithi(position: AshtamangalaPosition): String = when (position) {
        AshtamangalaPosition.AGNI, AshtamangalaPosition.INDRA -> "Shukla Paksha (Bright fortnight) preferred"
        AshtamangalaPosition.YAMA, AshtamangalaPosition.NIRRITI -> "Avoid Amavasya (New Moon)"
        AshtamangalaPosition.VARUNA -> "Full Moon (Purnima) auspicious"
        AshtamangalaPosition.KUBERA -> "Dhanteras or Akshaya Tritiya ideal"
        AshtamangalaPosition.ISHANA -> "Ekadashi highly favorable"
        AshtamangalaPosition.VAYU -> "Pratipada (First day) good for new beginnings"
    }

    private fun getCategorySpecificInterpretation(
        category: QueryCategory,
        indication: IndicationStrength,
        dominantPosition: AshtamangalaPosition
    ): String = when (category) {
        QueryCategory.HEALTH -> when (indication) {
            IndicationStrength.VERY_FAVORABLE, IndicationStrength.FAVORABLE ->
                "Health prospects are positive. Recovery is indicated. ${dominantPosition.deity}'s blessings support healing."
            IndicationStrength.MIXED ->
                "Health requires attention but is manageable. Follow medical advice and spiritual remedies."
            else ->
                "Health challenges present. Seek multiple opinions and strengthen remedial measures."
        }
        QueryCategory.WEALTH -> when (indication) {
            IndicationStrength.VERY_FAVORABLE, IndicationStrength.FAVORABLE ->
                "Financial prospects excellent. Investments and ventures favored. ${dominantPosition.deity} supports abundance."
            IndicationStrength.MIXED ->
                "Financial gains possible with effort. Avoid speculation. Steady approach recommended."
            else ->
                "Financial caution advised. Avoid major expenses. Build reserves gradually."
        }
        QueryCategory.RELATIONSHIP -> when (indication) {
            IndicationStrength.VERY_FAVORABLE, IndicationStrength.FAVORABLE ->
                "Relationships blessed. Marriage proposals favored. ${dominantPosition.deity} supports harmony."
            IndicationStrength.MIXED ->
                "Relationships need nurturing. Communication is key. Patience required."
            else ->
                "Relationship challenges present. Focus on self-improvement first."
        }
        QueryCategory.CAREER -> when (indication) {
            IndicationStrength.VERY_FAVORABLE, IndicationStrength.FAVORABLE ->
                "Career advancement indicated. New opportunities arising. ${dominantPosition.deity} supports professional growth."
            IndicationStrength.MIXED ->
                "Career requires strategic effort. Build skills and network. Timing matters."
            else ->
                "Career obstacles present. Focus on stability. Avoid confrontations."
        }
        QueryCategory.TRAVEL -> when (indication) {
            IndicationStrength.VERY_FAVORABLE, IndicationStrength.FAVORABLE ->
                "Travel highly auspicious. Journey will be successful. ${dominantPosition.direction} direction favorable."
            IndicationStrength.MIXED ->
                "Travel possible with precautions. Plan thoroughly. Avoid rush."
            else ->
                "Travel not recommended now. Postpone if possible or take extra care."
        }
        QueryCategory.LOST_OBJECT -> when (indication) {
            IndicationStrength.VERY_FAVORABLE, IndicationStrength.FAVORABLE ->
                "Good chances of recovery. Search in ${dominantPosition.direction} direction."
            IndicationStrength.MIXED ->
                "Partial recovery possible. May find through unexpected means."
            else ->
                "Recovery difficult. Accept loss or seek professional help."
        }
        QueryCategory.LITIGATION -> when (indication) {
            IndicationStrength.VERY_FAVORABLE, IndicationStrength.FAVORABLE ->
                "Favorable outcome in legal matters. Justice supports your case."
            IndicationStrength.MIXED ->
                "Legal matters require persistence. Settlement may be preferable."
            else ->
                "Legal challenges significant. Consider compromise. Avoid prolonged battles."
        }
        QueryCategory.SPIRITUAL -> when (indication) {
            IndicationStrength.VERY_FAVORABLE, IndicationStrength.FAVORABLE ->
                "Spiritual progress blessed. Sadhana will bear fruit. Divine grace abundant."
            IndicationStrength.MIXED ->
                "Spiritual growth steady. Increase dedication. Seek guidance."
            else ->
                "Spiritual obstacles present. Purification needed. Serve others."
        }
    }
}
