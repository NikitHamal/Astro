package com.astro.storm.ephemeris.triplepillar

import com.astro.storm.core.model.Planet
import com.astro.storm.core.model.PlanetPosition
import com.astro.storm.core.model.VedicChart
import com.astro.storm.core.model.ZodiacSign
import com.astro.storm.ephemeris.AshtakavargaCalculator
import com.astro.storm.ephemeris.AshtakavargaCalculator.AshtakavargaAnalysis
import com.astro.storm.ephemeris.AshtakavargaCalculator.Bhinnashtakavarga
import com.astro.storm.ephemeris.AshtakavargaCalculator.Sarvashtakavarga

/**
 * Ashtakavarga Pillar - Analyzes bindu-based transit strength for the Triple-Pillar Engine
 *
 * Ashtakavarga is a sophisticated system from BPHS that quantifies planetary
 * strength through a point (bindu) system. This pillar uses Ashtakavarga to:
 *
 * 1. **Bhinnashtakavarga (BAV)**: Individual planet contribution tables
 *    - Each planet contributes bindus (0-8) to each sign
 *    - Higher BAV score = better results when planet transits that sign
 *    - 4+ bindus considered favorable
 *
 * 2. **Sarvashtakavarga (SAV)**: Combined strength of all signs
 *    - Total bindus for each sign from all contributing planets
 *    - Range: 0-56 (theoretical), typically 18-38
 *    - 28+ bindus considered favorable for any planet's transit
 *
 * 3. **Kaksha (Sub-division)**: Each sign divided into 8 parts of 3°45'
 *    - Each Kaksha ruled by a planet or Ascendant
 *    - Provides fine-tuning of transit effects within a sign
 *
 * 4. **Transit Scoring**: Combines BAV and SAV for precise predictions
 *    - High BAV + High SAV = Excellent transit
 *    - High BAV + Low SAV = Good but limited
 *    - Low BAV + High SAV = Moderate, supported by environment
 *    - Low BAV + Low SAV = Challenging transit
 *
 * Based on Brihat Parasara Hora Shastra (BPHS) Chapters 66-72
 */
object AshtakavargaPillar {

    /**
     * Planets that contribute to Ashtakavarga (Rahu/Ketu excluded)
     */
    private val ASHTAKAVARGA_PLANETS = listOf(
        Planet.SUN, Planet.MOON, Planet.MARS, Planet.MERCURY,
        Planet.JUPITER, Planet.VENUS, Planet.SATURN
    )

    /**
     * Favorable BAV threshold (4+ out of 8 is considered good)
     */
    private const val FAVORABLE_BAV_THRESHOLD = 4

    /**
     * Favorable SAV threshold (28+ out of 56 is considered good)
     */
    private const val FAVORABLE_SAV_THRESHOLD = 28

    /**
     * SAV ranges for classification
     */
    private val SAV_RANGES = listOf(
        33..56 to "Excellent",
        28..32 to "Good",
        23..27 to "Average",
        18..22 to "Below Average",
        0..17 to "Weak"
    )

    /**
     * Evaluate the Ashtakavarga pillar score for given transit positions
     *
     * @param natalChart The natal chart
     * @param transitPositions Current planetary positions
     * @param preCalculatedAnalysis Optional pre-calculated Ashtakavarga analysis
     * @param config Configuration options
     * @return AshtakavargaPillarResult containing score and detailed analysis
     */
    fun evaluateAshtakavargaPillar(
        natalChart: VedicChart,
        transitPositions: List<PlanetPosition>,
        preCalculatedAnalysis: AshtakavargaAnalysis? = null,
        config: TriplePillarConfig = TriplePillarConfig.DEFAULT
    ): AshtakavargaPillarResult {
        // Use pre-calculated analysis if available, otherwise calculate
        val ashtakavargaAnalysis = preCalculatedAnalysis
            ?: AshtakavargaCalculator.calculateAshtakavarga(natalChart)

        val planetScores = mutableListOf<AshtakavargaTransitScore>()
        var totalWeightedScore = 0.0
        var totalWeight = 0.0

        for (transitPos in transitPositions) {
            val planet = transitPos.planet

            // Skip planets not in traditional Ashtakavarga
            if (planet !in ASHTAKAVARGA_PLANETS) continue

            val transitSign = transitPos.sign
            val bavScore = getBavScore(ashtakavargaAnalysis, planet, transitSign)
            val savScore = ashtakavargaAnalysis.sarvashtakavarga.getBindusForSign(transitSign)

            // Calculate Kaksha position and its beneficence
            val kakshaPosition = AshtakavargaCalculator.calculateKakshaPosition(
                transitPos.longitude, natalChart, ashtakavargaAnalysis
            )

            // Calculate composite score for this planet's transit
            val transitScore = calculateTransitScore(
                bavScore, savScore, kakshaPosition.isBeneficial, planet
            )

            val quality = determineTransitQuality(bavScore, savScore)
            val interpretation = generateInterpretation(planet, transitSign, bavScore, savScore, quality)

            planetScores.add(
                AshtakavargaTransitScore(
                    planet = planet,
                    transitSign = transitSign,
                    bavBindus = bavScore,
                    savBindus = savScore,
                    kakshaNumber = kakshaPosition.kakshaNumber,
                    kakshaLord = kakshaPosition.kakshaLord,
                    isKakshaBeneficial = kakshaPosition.isBeneficial,
                    compositeScore = transitScore,
                    quality = quality,
                    interpretation = interpretation
                )
            )

            // Weight based on planet's transit importance
            val weight = getTransitWeight(planet)
            totalWeightedScore += transitScore * weight
            totalWeight += weight
        }

        val compositeScore = if (totalWeight > 0) {
            (totalWeightedScore / totalWeight).coerceIn(0.0, 100.0)
        } else 50.0

        // Analyze SAV distribution
        val savDistribution = analyzeSavDistribution(ashtakavargaAnalysis.sarvashtakavarga)

        // Calculate Shodhana (reduction) for advanced analysis
        val shodhanaAnalysis = analyzeShodhana(ashtakavargaAnalysis)

        val summary = buildAshtakavargaSummary(planetScores, compositeScore, savDistribution)

        return AshtakavargaPillarResult(
            score = compositeScore,
            planetScores = planetScores,
            savDistribution = savDistribution,
            shodhanaAnalysis = shodhanaAnalysis,
            summary = summary
        )
    }

    /**
     * Get BAV score for a planet in a sign
     */
    private fun getBavScore(
        analysis: AshtakavargaAnalysis,
        planet: Planet,
        sign: ZodiacSign
    ): Int {
        return analysis.bhinnashtakavarga[planet]?.getBindusForSign(sign) ?: 0
    }

    /**
     * Calculate composite transit score from BAV and SAV
     */
    private fun calculateTransitScore(
        bavScore: Int,
        savScore: Int,
        isKakshaBeneficial: Boolean,
        planet: Planet
    ): Double {
        // BAV contributes 60% (score out of 8)
        val bavContribution = (bavScore / 8.0) * 60.0

        // SAV contributes 30% (score out of max ~40)
        val savContribution = (savScore / 40.0) * 30.0

        // Kaksha contributes 10%
        val kakshaContribution = if (isKakshaBeneficial) 10.0 else 0.0

        var score = bavContribution + savContribution + kakshaContribution

        // Bonus for natural benefics in high-bindu signs
        if (planet in listOf(Planet.JUPITER, Planet.VENUS) && bavScore >= 5 && savScore >= 30) {
            score *= 1.1
        }

        // Reduction for natural malefics in low-bindu signs
        if (planet in listOf(Planet.SATURN, Planet.MARS) && bavScore <= 2 && savScore <= 22) {
            score *= 0.9
        }

        return score.coerceIn(0.0, 100.0)
    }

    /**
     * Determine transit quality based on BAV and SAV
     */
    private fun determineTransitQuality(bavScore: Int, savScore: Int): AshtakavargaTransitQuality {
        return when {
            bavScore >= 5 && savScore >= 30 -> AshtakavargaTransitQuality.EXCELLENT
            bavScore >= 4 && savScore >= 28 -> AshtakavargaTransitQuality.GOOD
            bavScore >= 3 && savScore >= 25 -> AshtakavargaTransitQuality.AVERAGE
            bavScore >= 2 && savScore >= 22 -> AshtakavargaTransitQuality.BELOW_AVERAGE
            bavScore >= 2 || savScore >= 25 -> AshtakavargaTransitQuality.CHALLENGING
            else -> AshtakavargaTransitQuality.DIFFICULT
        }
    }

    /**
     * Generate interpretation text for a transit
     */
    private fun generateInterpretation(
        planet: Planet,
        sign: ZodiacSign,
        bavScore: Int,
        savScore: Int,
        quality: AshtakavargaTransitQuality
    ): String {
        val planetName = planet.displayName
        val signName = sign.displayName

        val bavDescription = when {
            bavScore >= 5 -> "excellent BAV strength ($bavScore/8 bindus)"
            bavScore >= 4 -> "good BAV strength ($bavScore/8 bindus)"
            bavScore >= 3 -> "moderate BAV strength ($bavScore/8 bindus)"
            bavScore >= 2 -> "weak BAV strength ($bavScore/8 bindus)"
            else -> "very weak BAV strength ($bavScore/8 bindus)"
        }

        val savDescription = when {
            savScore >= 30 -> "strong sign ($savScore SAV bindus)"
            savScore >= 25 -> "moderately strong sign ($savScore SAV)"
            savScore >= 20 -> "average sign ($savScore SAV)"
            else -> "weak sign ($savScore SAV)"
        }

        val qualityAdvice = when (quality) {
            AshtakavargaTransitQuality.EXCELLENT ->
                "Highly favorable period for $planetName-related matters."
            AshtakavargaTransitQuality.GOOD ->
                "Good period for initiatives related to $planetName."
            AshtakavargaTransitQuality.AVERAGE ->
                "Mixed results expected; proceed with measured expectations."
            AshtakavargaTransitQuality.BELOW_AVERAGE ->
                "Some challenges likely; patience recommended."
            AshtakavargaTransitQuality.CHALLENGING ->
                "Challenging period; extra care needed in $planetName matters."
            AshtakavargaTransitQuality.DIFFICULT ->
                "Difficult transit; avoid major decisions related to $planetName."
        }

        return "$planetName transiting $signName with $bavDescription in a $savDescription. $qualityAdvice"
    }

    /**
     * Get weight for planet's transit based on its importance and speed
     */
    private fun getTransitWeight(planet: Planet): Double {
        return when (planet) {
            Planet.SATURN -> 1.5    // Slow, significant impact
            Planet.JUPITER -> 1.4   // Slow, major benefic
            Planet.MARS -> 1.0      // Medium speed
            Planet.SUN -> 0.9       // Fast but important
            Planet.VENUS -> 0.8     // Fast
            Planet.MERCURY -> 0.7   // Very fast
            Planet.MOON -> 0.5      // Fastest, short-term
            else -> 0.5
        }
    }

    /**
     * Analyze the distribution of SAV across signs
     */
    private fun analyzeSavDistribution(sav: Sarvashtakavarga): SavDistributionAnalysis {
        val signScores = ZodiacSign.entries.map { sign ->
            sign to sav.getBindusForSign(sign)
        }

        val average = signScores.map { it.second }.average()
        val strongSigns = signScores.filter { it.second >= FAVORABLE_SAV_THRESHOLD }
            .map { it.first }
        val weakSigns = signScores.filter { it.second < 22 }
            .map { it.first }

        val quadrantAnalysis = analyzeQuadrants(signScores)

        return SavDistributionAnalysis(
            totalBindus = sav.totalBindus,
            averagePerSign = average,
            strongestSign = sav.strongestSign,
            weakestSign = sav.weakestSign,
            strongSigns = strongSigns,
            weakSigns = weakSigns,
            quadrantScores = quadrantAnalysis
        )
    }

    /**
     * Analyze SAV by quadrants (groups of 3 signs)
     */
    private fun analyzeQuadrants(signScores: List<Pair<ZodiacSign, Int>>): Map<String, Int> {
        return mapOf(
            "Dharma (1,5,9)" to signScores
                .filter { it.first.number in listOf(1, 5, 9) }
                .sumOf { it.second },
            "Artha (2,6,10)" to signScores
                .filter { it.first.number in listOf(2, 6, 10) }
                .sumOf { it.second },
            "Kama (3,7,11)" to signScores
                .filter { it.first.number in listOf(3, 7, 11) }
                .sumOf { it.second },
            "Moksha (4,8,12)" to signScores
                .filter { it.first.number in listOf(4, 8, 12) }
                .sumOf { it.second }
        )
    }

    /**
     * Analyze Shodhana (Ashtakavarga reductions)
     */
    private fun analyzeShodhana(analysis: AshtakavargaAnalysis): ShodhanaAnalysis {
        val shodhanaResult = AshtakavargaCalculator.calculateShodhana(analysis)

        val reductionSummary = mutableMapOf<ZodiacSign, ShodhanaSignSummary>()

        for ((sign, step) in shodhanaResult.reductions) {
            reductionSummary[sign] = ShodhanaSignSummary(
                originalBindus = step.originalBindus,
                afterTrikona = step.afterTrikona,
                afterEkadhipatya = step.afterEkadhipatya,
                finalPinda = step.finalPinda,
                reductionPercentage = if (step.originalBindus > 0) {
                    ((step.originalBindus - step.finalPinda).toDouble() / step.originalBindus * 100)
                } else 0.0
            )
        }

        val strongestAfterShodhana = reductionSummary.maxByOrNull { it.value.finalPinda }?.key
        val weakestAfterShodhana = reductionSummary.minByOrNull { it.value.finalPinda }?.key

        return ShodhanaAnalysis(
            totalPinda = shodhanaResult.totalPinda,
            reductionBySign = reductionSummary,
            strongestAfterShodhana = strongestAfterShodhana,
            weakestAfterShodhana = weakestAfterShodhana
        )
    }

    /**
     * Build summary text for Ashtakavarga analysis
     */
    private fun buildAshtakavargaSummary(
        planetScores: List<AshtakavargaTransitScore>,
        compositeScore: Double,
        savDistribution: SavDistributionAnalysis
    ): String {
        return buildString {
            val quality = when {
                compositeScore >= 70 -> "Favorable"
                compositeScore >= 55 -> "Moderately favorable"
                compositeScore >= 45 -> "Mixed"
                compositeScore >= 30 -> "Challenging"
                else -> "Difficult"
            }

            append("$quality Ashtakavarga support (Score: ${String.format("%.1f", compositeScore)}). ")

            val excellentTransits = planetScores.filter {
                it.quality == AshtakavargaTransitQuality.EXCELLENT
            }.map { it.planet.displayName }

            val difficultTransits = planetScores.filter {
                it.quality == AshtakavargaTransitQuality.DIFFICULT
            }.map { it.planet.displayName }

            if (excellentTransits.isNotEmpty()) {
                append("Strong support for ${excellentTransits.joinToString(", ")} transits. ")
            }

            if (difficultTransits.isNotEmpty()) {
                append("Weak support for ${difficultTransits.joinToString(", ")} transits. ")
            }

            append("Strongest sign: ${savDistribution.strongestSign.displayName} " +
                "(${savDistribution.totalBindus / 12} avg bindus).")
        }
    }

    /**
     * Get favorable signs for a specific activity based on SAV
     *
     * @param analysis Pre-calculated Ashtakavarga analysis
     * @param activity The activity category
     * @return List of favorable signs sorted by total strength
     */
    fun getFavorableSignsForActivity(
        analysis: AshtakavargaAnalysis,
        activity: ActivityCategory
    ): List<Pair<ZodiacSign, Int>> {
        val keyPlanets = activity.keyPlanets.filter { it in ASHTAKAVARGA_PLANETS }

        return ZodiacSign.entries.map { sign ->
            val totalKeyPlanetBindus = keyPlanets.sumOf { planet ->
                analysis.bhinnashtakavarga[planet]?.getBindusForSign(sign) ?: 0
            }
            val savBindus = analysis.sarvashtakavarga.getBindusForSign(sign)
            sign to (totalKeyPlanetBindus * 3 + savBindus) // Weighted combination
        }.sortedByDescending { it.second }
    }
}

/**
 * Result of Ashtakavarga pillar analysis
 */
data class AshtakavargaPillarResult(
    val score: Double,
    val planetScores: List<AshtakavargaTransitScore>,
    val savDistribution: SavDistributionAnalysis,
    val shodhanaAnalysis: ShodhanaAnalysis,
    val summary: String
)

/**
 * Ashtakavarga score for a single planet's transit
 */
data class AshtakavargaTransitScore(
    val planet: Planet,
    val transitSign: ZodiacSign,
    val bavBindus: Int,              // 0-8
    val savBindus: Int,              // 0-56
    val kakshaNumber: Int,           // 1-8
    val kakshaLord: String,
    val isKakshaBeneficial: Boolean,
    val compositeScore: Double,
    val quality: AshtakavargaTransitQuality,
    val interpretation: String
)

/**
 * Quality classification for Ashtakavarga transit
 */
enum class AshtakavargaTransitQuality(val description: String) {
    EXCELLENT("Excellent - High BAV and SAV support"),
    GOOD("Good - Favorable bindu count"),
    AVERAGE("Average - Moderate support"),
    BELOW_AVERAGE("Below Average - Limited support"),
    CHALLENGING("Challenging - Weak bindu count"),
    DIFFICULT("Difficult - Very weak support")
}

/**
 * Analysis of SAV distribution across signs
 */
data class SavDistributionAnalysis(
    val totalBindus: Int,
    val averagePerSign: Double,
    val strongestSign: ZodiacSign,
    val weakestSign: ZodiacSign,
    val strongSigns: List<ZodiacSign>,       // SAV >= 28
    val weakSigns: List<ZodiacSign>,         // SAV < 22
    val quadrantScores: Map<String, Int>     // Dharma, Artha, Kama, Moksha
)

/**
 * Analysis of Shodhana (Ashtakavarga reductions)
 */
data class ShodhanaAnalysis(
    val totalPinda: Int,
    val reductionBySign: Map<ZodiacSign, ShodhanaSignSummary>,
    val strongestAfterShodhana: ZodiacSign?,
    val weakestAfterShodhana: ZodiacSign?
)

/**
 * Shodhana summary for a single sign
 */
data class ShodhanaSignSummary(
    val originalBindus: Int,
    val afterTrikona: Int,
    val afterEkadhipatya: Int,
    val finalPinda: Int,
    val reductionPercentage: Double
)
