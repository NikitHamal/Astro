package com.astro.storm.ephemeris.prediction

import com.astro.storm.core.model.LifeArea
import com.astro.storm.core.model.Planet
import com.astro.storm.core.model.VedicChart
import java.time.LocalDate

data class PredictionData(
    val chart: VedicChart,
    val lifeOverview: LifeOverview,
    val currentPeriod: CurrentPeriodAnalysis,
    val lifeAreas: List<LifeAreaPrediction>,
    val activeYogas: List<ActiveYoga>,
    val challengesOpportunities: ChallengesOpportunities,
    val timing: TimingAnalysis,
    val remedies: List<RemedySuggestion>
)

data class LifeOverview(
    val overallPath: String,
    val keyStrengths: List<String>,
    val lifeTheme: String,
    val spiritualPath: String,
    val coreNarrative: String
)

data class CurrentPeriodAnalysis(
    val dashaInfo: String,
    val dashaEffect: String,
    val transitHighlights: List<TransitHighlight>,
    val overallEnergy: Int,
    val period: String,
    val keyFocusAreas: List<String>
)

data class TransitHighlight(
    val planet: Planet,
    val description: String,
    val impact: Int,
    val isPositive: Boolean
)

data class LifeAreaPrediction(
    val area: LifeArea,
    val rating: Int,
    val shortTerm: String,
    val mediumTerm: String,
    val longTerm: String,
    val keyFactors: List<String>,
    val advice: String,
    val supportingPlanets: List<Planet>
)

data class ActiveYoga(
    val name: String,
    val description: String,
    val strength: Int,
    val effects: String,
    val planets: List<Planet>
)

data class ChallengesOpportunities(
    val currentChallenges: List<Challenge>,
    val opportunities: List<Opportunity>
)

data class Challenge(
    val area: String,
    val description: String,
    val severity: Int,
    val mitigation: String
)

data class Opportunity(
    val area: String,
    val description: String,
    val timing: String,
    val howToLeverage: String
)

data class TimingAnalysis(
    val favorablePeriods: List<FavorablePeriod>,
    val unfavorablePeriods: List<UnfavorablePeriod>,
    val keyDates: List<KeyDate>
)

data class FavorablePeriod(
    val startDate: LocalDate,
    val endDate: LocalDate,
    val reason: String,
    val bestFor: List<String>
)

data class UnfavorablePeriod(
    val startDate: LocalDate,
    val endDate: LocalDate,
    val reason: String,
    val avoid: List<String>
)

data class KeyDate(
    val date: LocalDate,
    val event: String,
    val significance: String,
    val isPositive: Boolean
)

data class RemedySuggestion(
    val title: String,
    val description: String,
    val method: String,
    val timing: String,
    val duration: String
)
