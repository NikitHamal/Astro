package com.astro.storm.ephemeris.deepanalysis.predictions

import com.astro.storm.core.model.Planet
import com.astro.storm.ephemeris.deepanalysis.LocalizedParagraph
import com.astro.storm.ephemeris.deepanalysis.StrengthLevel
import java.time.LocalDate

/**
 * Data models for the Triple-Pillar Synthesis Engine
 *
 * Cross-references Dasha (Promise), Transit (Occasion), and Ashtakavarga (Strength).
 * This represents the "Success Probability" engine.
 */
data class TriplePillarAnalysis(
    val currentScore: Double, // 0-100
    val currentInterpretation: LocalizedParagraph,
    val dashaPromise: PillarDetails,
    val gocharaOccasion: PillarDetails,
    val ashtakavargaStrength: PillarDetails,
    val timeline: List<SuccessProbabilityPoint>,
    val peakProbability: Double,
    val peakDates: List<LocalDate>
)

data class PillarDetails(
    val pillarName: LocalizedParagraph,
    val score: Double, // 0-100
    val level: StrengthLevel,
    val contributions: List<PillarContribution>
)

data class PillarContribution(
    val label: LocalizedParagraph,
    val value: String,
    val impact: Double, // Positive or negative weight
    val isPositive: Boolean = impact >= 0
)

data class SuccessProbabilityPoint(
    val date: LocalDate,
    val probability: Double, // 0-100
    val primaryInfluencer: Planet?,
    val scoreMDL: Double,
    val scoreADL: Double
)
