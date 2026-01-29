package com.astro.storm.ephemeris.triplepillar

import com.astro.storm.core.common.Language
import com.astro.storm.core.common.StringResources
import com.astro.storm.core.model.Planet
import com.astro.storm.core.model.ZodiacSign
import com.astro.storm.ephemeris.DashaCalculator
import java.time.LocalDate
import java.time.LocalDateTime

/**
 * Triple-Pillar Predictive Engine Models
 *
 * The Three Pillars of Vedic Timing:
 * 1. VIMSHOTTARI DASHA - The soul's karmic timeline
 * 2. GOCHARA (TRANSITS) - Current planetary weather
 * 3. ASHTAKAVARGA - Strength of transiting planets in each sign
 *
 * Real-world predictions fail when Dashas are read in isolation.
 * This synthesis engine automatically cross-references all three pillars
 * to generate accurate "Success Probability" timelines.
 *
 * Classical References:
 * - Brihat Parashara Hora Shastra (Ch. 65-72)
 * - Phaladeepika (Ch. 26-28)
 * - Jataka Parijata (Ch. 18-20)
 *
 * @author AstroStorm - Ultra-Precision Vedic Astrology
 */

/**
 * Life area categories for prediction
 */
enum class LifeAreaCategory(
    val displayName: String,
    val houses: List<Int>,
    val significators: List<Planet>,
    val keywords: List<String>
) {
    CAREER(
        "Career & Profession",
        listOf(1, 2, 6, 10, 11),
        listOf(Planet.SUN, Planet.SATURN, Planet.MERCURY, Planet.JUPITER),
        listOf("job", "promotion", "business", "authority", "status")
    ),
    WEALTH(
        "Wealth & Finance",
        listOf(2, 5, 9, 11),
        listOf(Planet.JUPITER, Planet.VENUS, Planet.MERCURY),
        listOf("money", "income", "gains", "investments", "prosperity")
    ),
    RELATIONSHIP(
        "Relationships & Marriage",
        listOf(5, 7, 11),
        listOf(Planet.VENUS, Planet.JUPITER, Planet.MOON),
        listOf("marriage", "partnership", "love", "spouse", "harmony")
    ),
    HEALTH(
        "Health & Vitality",
        listOf(1, 6, 8),
        listOf(Planet.SUN, Planet.MARS, Planet.MOON),
        listOf("health", "energy", "recovery", "vitality", "wellness")
    ),
    EDUCATION(
        "Education & Learning",
        listOf(2, 4, 5, 9),
        listOf(Planet.JUPITER, Planet.MERCURY, Planet.MOON),
        listOf("study", "exams", "knowledge", "degree", "learning")
    ),
    SPIRITUALITY(
        "Spirituality & Growth",
        listOf(5, 9, 12),
        listOf(Planet.JUPITER, Planet.KETU, Planet.MOON),
        listOf("meditation", "spirituality", "moksha", "enlightenment", "devotion")
    ),
    PROPERTY(
        "Property & Assets",
        listOf(4, 11),
        listOf(Planet.MARS, Planet.VENUS, Planet.SATURN),
        listOf("house", "land", "vehicle", "property", "assets")
    ),
    TRAVEL(
        "Travel & Journeys",
        listOf(3, 9, 12),
        listOf(Planet.MOON, Planet.MERCURY, Planet.RAHU),
        listOf("travel", "journey", "foreign", "relocation", "pilgrimage")
    ),
    CHILDREN(
        "Children & Progeny",
        listOf(5, 9, 11),
        listOf(Planet.JUPITER, Planet.MOON, Planet.VENUS),
        listOf("children", "offspring", "conception", "pregnancy", "parenthood")
    ),
    LITIGATION(
        "Legal Matters",
        listOf(6, 7, 12),
        listOf(Planet.MARS, Planet.SATURN, Planet.RAHU),
        listOf("court", "legal", "disputes", "enemies", "litigation")
    );

    companion object {
        fun fromKeyword(keyword: String): LifeAreaCategory? {
            val lowerKeyword = keyword.lowercase()
            return entries.find { category ->
                category.keywords.any { it.contains(lowerKeyword) || lowerKeyword.contains(it) }
            }
        }
    }
}

/**
 * Dasha lord's functional nature for a specific chart
 */
enum class DashaLordNature(val score: Double, val description: String) {
    HIGHLY_BENEFIC(1.0, "Highly benefic - Yogakaraka or strong trine lord"),
    BENEFIC(0.75, "Benefic - Trine lord or benefic by nature"),
    NEUTRAL(0.5, "Neutral - Mixed results expected"),
    MALEFIC(0.25, "Malefic - Difficult results possible"),
    HIGHLY_MALEFIC(0.0, "Highly malefic - Maraka or afflicted dusthana lord")
}

/**
 * Transit quality for a planet
 */
enum class TransitQuality(val score: Double, val description: String) {
    EXCELLENT(1.0, "Excellent - Favorable house with high Ashtakavarga"),
    GOOD(0.75, "Good - Favorable conditions with moderate support"),
    AVERAGE(0.5, "Average - Mixed transit conditions"),
    CHALLENGING(0.25, "Challenging - Unfavorable but manageable"),
    DIFFICULT(0.0, "Difficult - Multiple adverse factors")
}

/**
 * Pillar contribution to overall prediction
 */
data class PillarContribution(
    val pillarName: String,
    val score: Double,           // 0.0 to 1.0
    val weight: Double,          // Relative importance
    val factors: List<String>,   // Contributing factors
    val interpretation: String
) {
    val weightedScore: Double get() = score * weight
}

/**
 * Dasha pillar analysis
 */
data class DashaPillarAnalysis(
    val mahadashaPlanet: Planet,
    val antardashaPlanet: Planet?,
    val pratyantardashaPlanet: Planet?,
    val mahadashaLordNature: DashaLordNature,
    val antardashaNature: DashaLordNature?,
    val mahadashaPlanetHouse: Int,
    val mahadashaPlanetSign: ZodiacSign,
    val dashaLordStrength: Double,           // 0-100 based on Shadbala
    val isDashaSandhi: Boolean,
    val sandhiSeverity: Double?,             // If in sandhi, how severe (0-1)
    val relativePositions: Map<Planet, Int>, // Houses from dasha lord
    val yogaFormations: List<String>,        // Relevant yogas involving dasha lords
    val interpretation: String
) {
    fun toPillarContribution(): PillarContribution {
        val factors = mutableListOf<String>()
        factors.add("Mahadasha: ${mahadashaPlanet.displayName} (${mahadashaLordNature.description})")
        antardashaPlanet?.let {
            factors.add("Antardasha: ${it.displayName} (${antardashaNature?.description ?: "Unknown"})")
        }
        if (isDashaSandhi) {
            factors.add("Warning: Dasha Sandhi period (transition)")
        }
        factors.addAll(yogaFormations.take(3))

        val score = calculateDashaPillarScore()
        val weight = 0.40 // Dasha is 40% of prediction

        val interpretation = buildString {
            append("The ${mahadashaPlanet.displayName} Mahadasha ")
            append(when (mahadashaLordNature) {
                DashaLordNature.HIGHLY_BENEFIC -> "is exceptionally favorable, promising excellent results."
                DashaLordNature.BENEFIC -> "is generally favorable with good potential."
                DashaLordNature.NEUTRAL -> "brings mixed results requiring balance."
                DashaLordNature.MALEFIC -> "requires caution and remedial measures."
                DashaLordNature.HIGHLY_MALEFIC -> "is challenging and needs careful navigation."
            })
            if (isDashaSandhi) {
                append(" Note: You are in a Dasha transition period which can bring uncertainty.")
            }
        }

        return PillarContribution(
            pillarName = "Vimshottari Dasha",
            score = score,
            weight = weight,
            factors = factors,
            interpretation = interpretation
        )
    }

    private fun calculateDashaPillarScore(): Double {
        var score = mahadashaLordNature.score

        // Adjust for antardasha
        antardashaNature?.let {
            score = (score * 0.6) + (it.score * 0.4)
        }

        // Adjust for dasha sandhi
        if (isDashaSandhi) {
            score *= (1.0 - (sandhiSeverity ?: 0.3))
        }

        // Adjust for strength
        score *= (0.5 + (dashaLordStrength / 200.0)) // Scale strength contribution

        return score.coerceIn(0.0, 1.0)
    }
}

/**
 * Transit pillar analysis (Gochara)
 */
data class TransitPillarAnalysis(
    val transitDateTime: LocalDateTime,
    val transitPositions: Map<Planet, TransitPosition>,
    val gocharaFromMoon: Map<Planet, GocharaEffect>,
    val significantTransits: List<SignificantTransit>,
    val vedhaAffectedPlanets: List<Planet>,
    val overallGocharaScore: Double
) {
    data class TransitPosition(
        val planet: Planet,
        val sign: ZodiacSign,
        val houseFromLagna: Int,
        val houseFromMoon: Int,
        val isRetrograde: Boolean,
        val speed: Double
    )

    data class GocharaEffect(
        val planet: Planet,
        val houseFromMoon: Int,
        val effect: TransitQuality,
        val isVedhaAffected: Boolean,
        val vedhaSource: Planet?
    )

    data class SignificantTransit(
        val planet: Planet,
        val description: String,
        val impact: Double, // -1.0 to 1.0
        val duration: String
    )

    fun toPillarContribution(): PillarContribution {
        val factors = mutableListOf<String>()

        // Add significant transits
        significantTransits.take(5).forEach { transit ->
            val impactStr = if (transit.impact >= 0) "+" else ""
            factors.add("${transit.planet.displayName}: ${transit.description} ($impactStr${(transit.impact * 100).toInt()}%)")
        }

        // Add vedha warnings
        if (vedhaAffectedPlanets.isNotEmpty()) {
            factors.add("Vedha obstructions: ${vedhaAffectedPlanets.joinToString(", ") { it.displayName }}")
        }

        val weight = 0.35 // Transit is 35% of prediction

        val interpretation = buildString {
            val favorableCount = gocharaFromMoon.count { it.value.effect.score >= 0.5 }
            val unfavorableCount = gocharaFromMoon.size - favorableCount

            when {
                favorableCount > 6 -> append("Current transits are highly supportive with strong planetary alignment.")
                favorableCount > 4 -> append("Transit conditions are generally favorable with moderate support.")
                favorableCount > 2 -> append("Mixed transit influences requiring selective action.")
                else -> append("Challenging transit period requiring patience and caution.")
            }

            if (vedhaAffectedPlanets.isNotEmpty()) {
                append(" Some favorable transits are obstructed by Vedha.")
            }
        }

        return PillarContribution(
            pillarName = "Gochara (Transits)",
            score = overallGocharaScore,
            weight = weight,
            factors = factors,
            interpretation = interpretation
        )
    }
}

/**
 * Ashtakavarga pillar analysis
 */
data class AshtakavargaPillarAnalysis(
    val currentTransitScores: Map<Planet, AshtakavargaTransitScore>,
    val sarvashtakavargaDistribution: Map<ZodiacSign, Int>,
    val strongHouses: List<Int>,     // Houses with SAV >= 28
    val weakHouses: List<Int>,       // Houses with SAV < 22
    val dashaLordBav: Map<Planet, Int>, // Dasha lord's BAV in current transit sign
    val overallAshtakavargaScore: Double
) {
    data class AshtakavargaTransitScore(
        val planet: Planet,
        val transitSign: ZodiacSign,
        val bavScore: Int,           // 0-8
        val savScore: Int,           // 0-56
        val quality: TransitQuality,
        val interpretation: String
    )

    fun toPillarContribution(): PillarContribution {
        val factors = mutableListOf<String>()

        // Add transit scores
        currentTransitScores.filter { it.key in listOf(Planet.JUPITER, Planet.SATURN, Planet.SUN, Planet.MOON) }
            .forEach { (planet, score) ->
                factors.add("${planet.displayName} in ${score.transitSign.displayName}: BAV ${score.bavScore}/8, SAV ${score.savScore}")
            }

        // Add strong/weak house info
        if (strongHouses.isNotEmpty()) {
            factors.add("Strong houses (SAV 28+): ${strongHouses.joinToString(", ")}")
        }
        if (weakHouses.isNotEmpty()) {
            factors.add("Weak houses (SAV <22): ${weakHouses.joinToString(", ")}")
        }

        val weight = 0.25 // Ashtakavarga is 25% of prediction

        val interpretation = buildString {
            val avgSav = sarvashtakavargaDistribution.values.average()
            when {
                overallAshtakavargaScore >= 0.75 -> append("Ashtakavarga strongly supports current planetary positions.")
                overallAshtakavargaScore >= 0.5 -> append("Moderate Ashtakavarga support for current transits.")
                overallAshtakavargaScore >= 0.25 -> append("Limited Ashtakavarga strength requires selective timing.")
                else -> append("Weak Ashtakavarga positions suggest careful planning.")
            }
        }

        return PillarContribution(
            pillarName = "Ashtakavarga",
            score = overallAshtakavargaScore,
            weight = weight,
            factors = factors,
            interpretation = interpretation
        )
    }
}

/**
 * Time window for prediction
 */
data class PredictionTimeWindow(
    val startDate: LocalDateTime,
    val endDate: LocalDateTime,
    val successProbability: Double,  // 0.0 to 1.0
    val peakDate: LocalDateTime?,
    val lifeAreaScores: Map<LifeAreaCategory, Double>,
    val dominantInfluences: List<String>
)

/**
 * Success probability data point for timeline
 */
data class SuccessProbabilityPoint(
    val date: LocalDate,
    val probability: Double,         // 0.0 to 1.0
    val dashaContribution: Double,
    val transitContribution: Double,
    val ashtakavargaContribution: Double,
    val dominantFactor: String,
    val recommendation: String
)

/**
 * Complete Triple-Pillar synthesis result
 */
data class TriplePillarSynthesis(
    val analysisDateTime: LocalDateTime,
    val natalChartId: String,
    val dashaPillar: DashaPillarAnalysis,
    val transitPillar: TransitPillarAnalysis,
    val ashtakavargaPillar: AshtakavargaPillarAnalysis,
    val overallSuccessProbability: Double,
    val lifeAreaPredictions: Map<LifeAreaCategory, LifeAreaPrediction>,
    val timeline: List<SuccessProbabilityPoint>,
    val peakPeriods: List<PredictionTimeWindow>,
    val cautionPeriods: List<PredictionTimeWindow>,
    val recommendations: List<String>,
    val language: Language = Language.ENGLISH
) {
    val dashaPillarContribution: PillarContribution get() = dashaPillar.toPillarContribution()
    val transitPillarContribution: PillarContribution get() = transitPillar.toPillarContribution()
    val ashtakavargaPillarContribution: PillarContribution get() = ashtakavargaPillar.toPillarContribution()

    fun toPlainText(): String = buildString {
        val line = "═".repeat(60)
        appendLine(line)
        appendLine("          TRIPLE-PILLAR PREDICTIVE SYNTHESIS")
        appendLine("          Three Pillars of Vedic Timing")
        appendLine(line)
        appendLine()
        appendLine("Analysis Date: $analysisDateTime")
        appendLine("Overall Success Probability: ${String.format("%.1f", overallSuccessProbability * 100)}%")
        appendLine()

        // Pillar breakdown
        appendLine("PILLAR CONTRIBUTIONS")
        appendLine("─".repeat(60))
        val dashaContrib = dashaPillarContribution
        appendLine("1. ${dashaContrib.pillarName}")
        appendLine("   Score: ${String.format("%.1f", dashaContrib.score * 100)}% (Weight: ${(dashaContrib.weight * 100).toInt()}%)")
        appendLine("   ${dashaContrib.interpretation}")
        dashaContrib.factors.forEach { appendLine("   • $it") }
        appendLine()

        val transitContrib = transitPillarContribution
        appendLine("2. ${transitContrib.pillarName}")
        appendLine("   Score: ${String.format("%.1f", transitContrib.score * 100)}% (Weight: ${(transitContrib.weight * 100).toInt()}%)")
        appendLine("   ${transitContrib.interpretation}")
        transitContrib.factors.forEach { appendLine("   • $it") }
        appendLine()

        val ashtaContrib = ashtakavargaPillarContribution
        appendLine("3. ${ashtaContrib.pillarName}")
        appendLine("   Score: ${String.format("%.1f", ashtaContrib.score * 100)}% (Weight: ${(ashtaContrib.weight * 100).toInt()}%)")
        appendLine("   ${ashtaContrib.interpretation}")
        ashtaContrib.factors.forEach { appendLine("   • $it") }
        appendLine()

        // Life Area Predictions
        appendLine("LIFE AREA PREDICTIONS")
        appendLine("─".repeat(60))
        lifeAreaPredictions.entries.sortedByDescending { it.value.successProbability }.forEach { (area, pred) ->
            val bar = "█".repeat((pred.successProbability * 20).toInt())
            appendLine("${area.displayName.padEnd(25)} ${String.format("%5.1f", pred.successProbability * 100)}% $bar")
            appendLine("   ${pred.briefInterpretation}")
        }
        appendLine()

        // Peak periods
        if (peakPeriods.isNotEmpty()) {
            appendLine("PEAK PERIODS (Success Probability > 70%)")
            appendLine("─".repeat(60))
            peakPeriods.take(5).forEach { period ->
                appendLine("${period.startDate.toLocalDate()} to ${period.endDate.toLocalDate()}: ${String.format("%.1f", period.successProbability * 100)}%")
                period.dominantInfluences.take(2).forEach { appendLine("   • $it") }
            }
            appendLine()
        }

        // Caution periods
        if (cautionPeriods.isNotEmpty()) {
            appendLine("CAUTION PERIODS (Success Probability < 40%)")
            appendLine("─".repeat(60))
            cautionPeriods.take(5).forEach { period ->
                appendLine("${period.startDate.toLocalDate()} to ${period.endDate.toLocalDate()}: ${String.format("%.1f", period.successProbability * 100)}%")
                period.dominantInfluences.take(2).forEach { appendLine("   • $it") }
            }
            appendLine()
        }

        // Recommendations
        appendLine("RECOMMENDATIONS")
        appendLine("─".repeat(60))
        recommendations.forEachIndexed { index, rec ->
            appendLine("${index + 1}. $rec")
        }
    }
}

/**
 * Prediction for a specific life area
 */
data class LifeAreaPrediction(
    val area: LifeAreaCategory,
    val successProbability: Double,
    val dashaSupport: Double,
    val transitSupport: Double,
    val ashtakavargaSupport: Double,
    val relevantDashaLords: List<Planet>,
    val keyTransits: List<String>,
    val favorableTimings: List<LocalDate>,
    val challenges: List<String>,
    val opportunities: List<String>,
    val briefInterpretation: String,
    val detailedInterpretation: String
)

/**
 * Query for prediction
 */
data class PredictionQuery(
    val lifeArea: LifeAreaCategory?,
    val specificQuestion: String?,
    val timeframeMonths: Int = 12,
    val includeTimeline: Boolean = true,
    val granularity: TimelineGranularity = TimelineGranularity.WEEKLY
)

/**
 * Timeline granularity
 */
enum class TimelineGranularity(val days: Int) {
    DAILY(1),
    WEEKLY(7),
    BIWEEKLY(14),
    MONTHLY(30)
}
