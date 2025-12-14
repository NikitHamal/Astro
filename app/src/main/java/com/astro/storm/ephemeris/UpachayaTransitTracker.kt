package com.astro.storm.ephemeris

import com.astro.storm.data.model.Planet
import com.astro.storm.data.model.PlanetPosition
import com.astro.storm.data.model.VedicChart
import com.astro.storm.data.model.ZodiacSign
import java.time.LocalDateTime

/**
 * Upachaya House Transit Tracker
 *
 * Tracks transits through Upachaya houses (3, 6, 10, 11) where natural malefics
 * give good results. These houses are "growth houses" where planets gain strength
 * over time and produce increasingly positive results.
 *
 * Reference: Phaladeepika, BPHS transit rules
 */
object UpachayaTransitTracker {

    /** Upachaya houses: 3 (courage), 6 (enemies), 10 (career), 11 (gains) */
    val UPACHAYA_HOUSES = setOf(3, 6, 10, 11)

    /** Malefic planets that give especially good results in Upachaya houses */
    val BENEFICIAL_MALEFICS_IN_UPACHAYA = setOf(
        Planet.SATURN, Planet.MARS, Planet.RAHU, Planet.KETU, Planet.SUN
    )

    /**
     * Analyze current transit positions relative to natal chart
     */
    fun analyzeUpachayaTransits(
        natalChart: VedicChart,
        transitPositions: List<PlanetPosition>
    ): UpachayaTransitAnalysis {
        val moonSign = natalChart.planetPositions.find { it.planet == Planet.MOON }?.sign
            ?: throw IllegalArgumentException("Moon position required")

        val lagnaSign = ZodiacSign.fromLongitude(natalChart.ascendant)

        val upachayaTransitsFromMoon = analyzeTransitsFromReference(
            transitPositions, moonSign, TransitReference.MOON
        )
        val upachayaTransitsFromLagna = analyzeTransitsFromReference(
            transitPositions, lagnaSign, TransitReference.LAGNA
        )

        val activeUpachayaTransits = (upachayaTransitsFromMoon + upachayaTransitsFromLagna)
            .filter { it.isInUpachaya }

        val mostSignificantTransits = activeUpachayaTransits
            .sortedByDescending { it.significance }
            .take(5)

        val overallAssessment = calculateOverallAssessment(activeUpachayaTransits)
        val recommendations = generateRecommendations(activeUpachayaTransits)
        val alerts = generateAlerts(activeUpachayaTransits)

        return UpachayaTransitAnalysis(
            analysisDateTime = LocalDateTime.now(),
            moonSign = moonSign,
            lagnaSign = lagnaSign,
            transitsFromMoon = upachayaTransitsFromMoon,
            transitsFromLagna = upachayaTransitsFromLagna,
            activeUpachayaTransits = activeUpachayaTransits,
            mostSignificantTransits = mostSignificantTransits,
            overallAssessment = overallAssessment,
            houseWiseAnalysis = analyzeByHouse(activeUpachayaTransits),
            recommendations = recommendations,
            alerts = alerts
        )
    }

    /**
     * Analyze transits from a specific reference point
     */
    private fun analyzeTransitsFromReference(
        transitPositions: List<PlanetPosition>,
        referenceSign: ZodiacSign,
        reference: TransitReference
    ): List<UpachayaTransit> {
        return transitPositions.map { transit ->
            val houseFromReference = ((transit.sign.ordinal - referenceSign.ordinal + 12) % 12) + 1
            val isInUpachaya = houseFromReference in UPACHAYA_HOUSES
            val isBeneficialMalefic = transit.planet in BENEFICIAL_MALEFICS_IN_UPACHAYA

            val transitQuality = when {
                isInUpachaya && isBeneficialMalefic -> TransitQuality.EXCELLENT
                isInUpachaya && transit.planet in AstrologicalConstants.NATURAL_BENEFICS ->
                    TransitQuality.GOOD
                isInUpachaya -> TransitQuality.FAVORABLE
                else -> TransitQuality.NEUTRAL
            }

            val significance = calculateTransitSignificance(
                transit.planet, houseFromReference, isInUpachaya, isBeneficialMalefic
            )

            val effects = if (isInUpachaya) {
                getUpachayaTransitEffects(transit.planet, houseFromReference, reference)
            } else emptyList()

            val duration = getTransitDuration(transit.planet)

            UpachayaTransit(
                planet = transit.planet,
                transitSign = transit.sign,
                transitDegree = transit.longitude % 30.0,
                reference = reference,
                referenceSign = referenceSign,
                houseFromReference = houseFromReference,
                isInUpachaya = isInUpachaya,
                isBeneficialMaleficInUpachaya = isInUpachaya && isBeneficialMalefic,
                transitQuality = transitQuality,
                significance = significance,
                effects = effects,
                approximateDuration = duration,
                recommendations = if (isInUpachaya) {
                    getTransitRecommendations(transit.planet, houseFromReference)
                } else emptyList()
            )
        }
    }

    private fun calculateTransitSignificance(
        planet: Planet,
        house: Int,
        isUpachaya: Boolean,
        isBeneficialMalefic: Boolean
    ): Double {
        if (!isUpachaya) return 30.0

        var score = 50.0

        // Slow-moving planets have more significant transits
        score += when (planet) {
            Planet.SATURN -> 30.0
            Planet.JUPITER -> 25.0
            Planet.RAHU, Planet.KETU -> 20.0
            Planet.MARS -> 15.0
            Planet.SUN -> 10.0
            Planet.VENUS -> 8.0
            Planet.MERCURY -> 5.0
            Planet.MOON -> 3.0
            else -> 0.0
        }

        // Malefics in Upachaya get bonus
        if (isBeneficialMalefic) score += 15.0

        // 10th and 11th house transits are most significant
        when (house) {
            10 -> score += 10.0
            11 -> score += 12.0
            6 -> score += 5.0
            3 -> score += 3.0
        }

        return score.coerceIn(0.0, 100.0)
    }

    private fun getUpachayaTransitEffects(
        planet: Planet,
        house: Int,
        reference: TransitReference
    ): List<String> {
        val houseEffects = when (house) {
            3 -> listOf(
                "Courage and initiative enhanced",
                "Short travels and communications favored",
                "Sibling relationships in focus",
                "Skills and hobbies development"
            )
            6 -> listOf(
                "Victory over enemies and competitors",
                "Health improvements through discipline",
                "Service opportunities arise",
                "Debt resolution possibilities"
            )
            10 -> listOf(
                "Career advancement opportunities",
                "Professional recognition possible",
                "Authority and status improvements",
                "Government-related matters favored"
            )
            11 -> listOf(
                "Gains and income increase likely",
                "Desires and aspirations fulfilled",
                "Friendships and social network expansion",
                "Elder siblings related benefits"
            )
            else -> emptyList()
        }

        val planetEffects = when (planet) {
            Planet.SATURN -> listOf(
                "Slow but lasting results through discipline",
                "Karmic rewards for past efforts",
                "Structure and organization bring success"
            )
            Planet.MARS -> listOf(
                "Energy and drive for action",
                "Competitive success possible",
                "Physical activities favored"
            )
            Planet.RAHU -> listOf(
                "Unconventional opportunities arise",
                "Foreign connections beneficial",
                "Technology and innovation favored"
            )
            Planet.KETU -> listOf(
                "Spiritual insights through practical matters",
                "Detachment brings clarity",
                "Past karma resolution"
            )
            Planet.JUPITER -> listOf(
                "Wisdom and expansion in ${getHouseName(house)} matters",
                "Teachers and guides appear",
                "Fortune through righteous action"
            )
            Planet.SUN -> listOf(
                "Authority and confidence boosted",
                "Government favor possible",
                "Leadership opportunities"
            )
            else -> listOf("${planet.displayName} influences ${getHouseName(house)} house themes")
        }

        return houseEffects.take(2) + planetEffects.take(2)
    }

    private fun getTransitDuration(planet: Planet): String {
        return when (planet) {
            Planet.MOON -> "~2.5 days per sign"
            Planet.SUN -> "~1 month per sign"
            Planet.MERCURY -> "~3-4 weeks per sign"
            Planet.VENUS -> "~3-4 weeks per sign"
            Planet.MARS -> "~6-8 weeks per sign"
            Planet.JUPITER -> "~1 year per sign"
            Planet.SATURN -> "~2.5 years per sign"
            Planet.RAHU, Planet.KETU -> "~1.5 years per sign"
            else -> "Variable"
        }
    }

    private fun getTransitRecommendations(planet: Planet, house: Int): List<String> {
        val houseRecs = when (house) {
            3 -> listOf(
                "Take initiative on pending matters",
                "Communicate your ideas boldly",
                "Learn a new skill or hobby"
            )
            6 -> listOf(
                "Address health matters proactively",
                "Resolve conflicts decisively",
                "Organize daily routines"
            )
            10 -> listOf(
                "Focus on career goals",
                "Seek recognition for your work",
                "Take on leadership responsibilities"
            )
            11 -> listOf(
                "Network and expand connections",
                "Pursue financial opportunities",
                "Support friends and community"
            )
            else -> emptyList()
        }

        val planetRecs = when (planet) {
            Planet.SATURN -> listOf(
                "Work with discipline and patience",
                "Don't expect quick results",
                "Honor commitments and responsibilities"
            )
            Planet.MARS -> listOf(
                "Channel energy constructively",
                "Avoid impulsive actions",
                "Physical exercise recommended"
            )
            Planet.RAHU -> listOf(
                "Stay grounded amid opportunities",
                "Avoid shortcuts",
                "Embrace innovation carefully"
            )
            Planet.JUPITER -> listOf(
                "Expand through wisdom",
                "Share knowledge generously",
                "Maintain ethical standards"
            )
            else -> emptyList()
        }

        return (houseRecs + planetRecs).take(4)
    }

    private fun analyzeByHouse(transits: List<UpachayaTransit>): Map<Int, HouseTransitAnalysis> {
        return UPACHAYA_HOUSES.associateWith { house ->
            val houseTransits = transits.filter { it.houseFromReference == house && it.isInUpachaya }
            val planets = houseTransits.map { it.planet }.distinct()

            HouseTransitAnalysis(
                house = house,
                houseName = getHouseName(house),
                houseTheme = getHouseTheme(house),
                transitingPlanets = planets,
                strength = calculateHouseTransitStrength(houseTransits),
                effects = getHouseTransitSummary(house, planets),
                timing = if (houseTransits.isNotEmpty()) {
                    "Active: ${houseTransits.joinToString { it.planet.displayName }}"
                } else "No current transits"
            )
        }
    }

    private fun getHouseTheme(house: Int): String {
        return when (house) {
            3 -> "Courage, Siblings, Communication"
            6 -> "Enemies, Health, Service"
            10 -> "Career, Status, Authority"
            11 -> "Gains, Income, Aspirations"
            else -> "General"
        }
    }

    private fun calculateHouseTransitStrength(transits: List<UpachayaTransit>): HouseStrength {
        if (transits.isEmpty()) return HouseStrength.INACTIVE

        val avgSignificance = transits.map { it.significance }.average()
        val hasMalefic = transits.any { it.isBeneficialMaleficInUpachaya }

        return when {
            avgSignificance >= 70 && hasMalefic -> HouseStrength.VERY_STRONG
            avgSignificance >= 60 -> HouseStrength.STRONG
            avgSignificance >= 45 -> HouseStrength.MODERATE
            else -> HouseStrength.MILD
        }
    }

    private fun getHouseTransitSummary(house: Int, planets: List<Planet>): List<String> {
        if (planets.isEmpty()) return listOf("No active Upachaya transits in ${getHouseName(house)} house")

        val summary = mutableListOf<String>()
        summary.add("${planets.size} planet(s) transiting ${getHouseName(house)} house")

        if (planets.any { it == Planet.SATURN }) {
            summary.add("Saturn brings slow, steady progress in ${getHouseTheme(house).lowercase()}")
        }
        if (planets.any { it == Planet.MARS }) {
            summary.add("Mars energizes action in ${getHouseTheme(house).lowercase()}")
        }
        if (planets.any { it == Planet.JUPITER }) {
            summary.add("Jupiter expands opportunities in ${getHouseTheme(house).lowercase()}")
        }

        return summary
    }

    private fun getHouseName(house: Int): String {
        return when (house) {
            1 -> "1st"
            2 -> "2nd"
            3 -> "3rd"
            else -> "${house}th"
        }
    }

    private fun calculateOverallAssessment(transits: List<UpachayaTransit>): OverallUpachayaAssessment {
        if (transits.isEmpty()) {
            return OverallUpachayaAssessment(
                level = UpachayaLevel.LOW,
                score = 30.0,
                summary = "No significant Upachaya transits currently active",
                keyPeriod = "Wait for slow planets to enter Upachaya houses"
            )
        }

        val avgSignificance = transits.map { it.significance }.average()
        val excellentCount = transits.count { it.transitQuality == TransitQuality.EXCELLENT }

        val level = when {
            avgSignificance >= 70 && excellentCount >= 2 -> UpachayaLevel.EXCEPTIONAL
            avgSignificance >= 60 || excellentCount >= 1 -> UpachayaLevel.HIGH
            avgSignificance >= 45 -> UpachayaLevel.MODERATE
            else -> UpachayaLevel.LOW
        }

        val summary = when (level) {
            UpachayaLevel.EXCEPTIONAL ->
                "Exceptional period! Multiple powerful transits favor growth in career, gains, and overcoming obstacles"
            UpachayaLevel.HIGH ->
                "Favorable period with strong Upachaya transits supporting professional and material growth"
            UpachayaLevel.MODERATE ->
                "Moderate Upachaya support - steady progress possible with focused effort"
            UpachayaLevel.LOW ->
                "Limited Upachaya activation - focus on preparation for upcoming favorable transits"
        }

        val keyPeriod = transits
            .filter { it.planet in listOf(Planet.SATURN, Planet.JUPITER, Planet.RAHU) }
            .maxByOrNull { it.significance }
            ?.let { "${it.planet.displayName} in ${getHouseName(it.houseFromReference)} house is the key transit" }
            ?: "Focus on daily actions to maximize current transits"

        return OverallUpachayaAssessment(
            level = level,
            score = avgSignificance,
            summary = summary,
            keyPeriod = keyPeriod
        )
    }

    private fun generateRecommendations(transits: List<UpachayaTransit>): List<String> {
        val recommendations = mutableListOf<String>()

        val activeHouses = transits.filter { it.isInUpachaya }.map { it.houseFromReference }.distinct()

        if (3 in activeHouses) {
            recommendations.add("Take bold initiatives - courage is supported now")
        }
        if (6 in activeHouses) {
            recommendations.add("Address health and resolve conflicts - victory over obstacles favored")
        }
        if (10 in activeHouses) {
            recommendations.add("Focus on career advancement - professional recognition possible")
        }
        if (11 in activeHouses) {
            recommendations.add("Pursue financial goals - gains and fulfillment of desires supported")
        }

        val hasSlowMalefic = transits.any {
            it.isInUpachaya && it.planet in listOf(Planet.SATURN, Planet.RAHU)
        }
        if (hasSlowMalefic) {
            recommendations.add("Major slow-planet transit active - commit to long-term goals")
        }

        if (recommendations.isEmpty()) {
            recommendations.add("Prepare for upcoming Upachaya transits by organizing current affairs")
        }

        return recommendations
    }

    private fun generateAlerts(transits: List<UpachayaTransit>): List<UpachayaAlert> {
        val alerts = mutableListOf<UpachayaAlert>()

        transits.filter { it.transitQuality == TransitQuality.EXCELLENT }.forEach { transit ->
            alerts.add(
                UpachayaAlert(
                    type = AlertType.OPPORTUNITY,
                    planet = transit.planet,
                    house = transit.houseFromReference,
                    message = "${transit.planet.displayName} in ${getHouseName(transit.houseFromReference)} house - Excellent opportunity period!",
                    priority = AlertPriority.HIGH
                )
            )
        }

        val saturnIn10th = transits.find {
            it.planet == Planet.SATURN && it.houseFromReference == 10 && it.isInUpachaya
        }
        if (saturnIn10th != null) {
            alerts.add(
                UpachayaAlert(
                    type = AlertType.MAJOR_TRANSIT,
                    planet = Planet.SATURN,
                    house = 10,
                    message = "Saturn transiting 10th house - Major career period! Work hard for lasting results",
                    priority = AlertPriority.HIGH
                )
            )
        }

        val jupiterIn11th = transits.find {
            it.planet == Planet.JUPITER && it.houseFromReference == 11 && it.isInUpachaya
        }
        if (jupiterIn11th != null) {
            alerts.add(
                UpachayaAlert(
                    type = AlertType.FORTUNE,
                    planet = Planet.JUPITER,
                    house = 11,
                    message = "Jupiter transiting 11th house - Excellent for gains and fulfillment of desires!",
                    priority = AlertPriority.HIGH
                )
            )
        }

        return alerts.sortedByDescending { it.priority }
    }

    /**
     * Calculate upcoming Upachaya transit dates (simplified)
     */
    fun getUpcomingTransits(natalChart: VedicChart): List<UpcomingUpachayaTransit> {
        val moonSign = natalChart.planetPositions.find { it.planet == Planet.MOON }?.sign
            ?: return emptyList()

        val upcomingTransits = mutableListOf<UpcomingUpachayaTransit>()

        UPACHAYA_HOUSES.forEach { house ->
            val targetSign = ZodiacSign.entries[(moonSign.ordinal + house - 1) % 12]

            listOf(Planet.SATURN, Planet.JUPITER, Planet.MARS).forEach { planet ->
                upcomingTransits.add(
                    UpcomingUpachayaTransit(
                        planet = planet,
                        targetHouse = house,
                        targetSign = targetSign,
                        significance = "When ${planet.displayName} enters ${targetSign.displayName} - ${getHouseTheme(house)} activated",
                        recommendation = "Prepare for ${getHouseTheme(house).lowercase()} related activities"
                    )
                )
            }
        }

        return upcomingTransits.sortedBy { it.targetHouse }
    }
}

// Data classes
data class UpachayaTransitAnalysis(
    val analysisDateTime: LocalDateTime,
    val moonSign: ZodiacSign,
    val lagnaSign: ZodiacSign,
    val transitsFromMoon: List<UpachayaTransit>,
    val transitsFromLagna: List<UpachayaTransit>,
    val activeUpachayaTransits: List<UpachayaTransit>,
    val mostSignificantTransits: List<UpachayaTransit>,
    val overallAssessment: OverallUpachayaAssessment,
    val houseWiseAnalysis: Map<Int, HouseTransitAnalysis>,
    val recommendations: List<String>,
    val alerts: List<UpachayaAlert>
)

enum class TransitReference {
    MOON, LAGNA
}

data class UpachayaTransit(
    val planet: Planet,
    val transitSign: ZodiacSign,
    val transitDegree: Double,
    val reference: TransitReference,
    val referenceSign: ZodiacSign,
    val houseFromReference: Int,
    val isInUpachaya: Boolean,
    val isBeneficialMaleficInUpachaya: Boolean,
    val transitQuality: TransitQuality,
    val significance: Double,
    val effects: List<String>,
    val approximateDuration: String,
    val recommendations: List<String>
)

enum class TransitQuality {
    EXCELLENT, GOOD, FAVORABLE, NEUTRAL
}

data class HouseTransitAnalysis(
    val house: Int,
    val houseName: String,
    val houseTheme: String,
    val transitingPlanets: List<Planet>,
    val strength: HouseStrength,
    val effects: List<String>,
    val timing: String
)

enum class HouseStrength {
    INACTIVE, MILD, MODERATE, STRONG, VERY_STRONG
}

data class OverallUpachayaAssessment(
    val level: UpachayaLevel,
    val score: Double,
    val summary: String,
    val keyPeriod: String
)

enum class UpachayaLevel {
    LOW, MODERATE, HIGH, EXCEPTIONAL
}

data class UpachayaAlert(
    val type: AlertType,
    val planet: Planet,
    val house: Int,
    val message: String,
    val priority: AlertPriority
)

enum class AlertType {
    OPPORTUNITY, MAJOR_TRANSIT, FORTUNE, CAUTION
}

enum class AlertPriority {
    LOW, MEDIUM, HIGH
}

data class UpcomingUpachayaTransit(
    val planet: Planet,
    val targetHouse: Int,
    val targetSign: ZodiacSign,
    val significance: String,
    val recommendation: String
)
