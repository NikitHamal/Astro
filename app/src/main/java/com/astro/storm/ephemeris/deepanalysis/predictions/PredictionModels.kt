package com.astro.storm.ephemeris.deepanalysis.predictions

import com.astro.storm.core.model.Planet
import com.astro.storm.core.model.ZodiacSign
import com.astro.storm.ephemeris.deepanalysis.*
import java.time.LocalDate

/**
 * Data models for Deep Predictions
 */
data class DeepPredictions(
    val dashaAnalysis: DashaDeepAnalysis,
    val transitAnalysis: TransitDeepAnalysis,
    val yearlyPrediction: YearlyPrediction,
    val monthlyPredictions: List<MonthlyPrediction>,
    val lifeAreaPredictions: Map<LifeArea, LifeAreaPrediction>,
    val yogaActivationTimeline: List<YogaActivationEvent>,
    val criticalPeriods: List<CriticalPeriod>,
    val opportunityWindows: List<OpportunityWindow>,
    val remedialMeasures: RemedialProfile,
    val overallPredictionSummary: LocalizedParagraph,
    val predictionScore: Double
)

data class DashaDeepAnalysis(
    val currentMahadasha: MahadashaDeepAnalysis?,
    val currentAntardasha: AntardashaDeepAnalysis?,
    val upcomingDashas: List<UpcomingDashaPeriod>,
    val dashaBalance: LocalizedParagraph
)

data class MahadashaDeepAnalysis(
    val planet: Planet,
    val startDate: LocalDate,
    val endDate: LocalDate,
    val overallTheme: LocalizedParagraph,
    val lifeAreaEffects: Map<LifeArea, LocalizedParagraph>,
    val strengths: List<LocalizedTrait>,
    val challenges: List<LocalizedTrait>,
    val advice: LocalizedParagraph
)

data class AntardashaDeepAnalysis(
    val planet: Planet,
    val startDate: LocalDate,
    val endDate: LocalDate,
    val refinedTheme: LocalizedParagraph,
    val shortTermEffects: List<LocalizedParagraph>,
    val activatedYogas: List<ActivatedYoga>
)

data class UpcomingDashaPeriod(
    val planet: Planet,
    val startDate: LocalDate,
    val endDate: LocalDate,
    val briefPreview: LocalizedParagraph
)

data class ActivatedYoga(
    val yogaName: String,
    val strength: StrengthLevel,
    val activationLevel: StrengthLevel,
    val expectedResults: LocalizedParagraph
)

data class TransitDeepAnalysis(
    val majorTransits: List<MajorTransit>,
    val saturnSadeSati: SadeSatiAnalysis,
    val jupiterTransit: JupiterTransitAnalysis,
    val rahuKetuTransit: NodalTransitAnalysis,
    val transitInteractions: List<TransitInteraction>
)

data class MajorTransit(
    val planet: Planet,
    val currentSign: ZodiacSign,
    val effectOnNative: LocalizedParagraph,
    val duration: LocalizedParagraph,
    val intensity: StrengthLevel
)

data class SadeSatiAnalysis(
    val isActive: Boolean,
    val phase: SadeSatiPhase,
    val startDate: LocalDate?,
    val endDate: LocalDate?,
    val effects: LocalizedParagraph,
    val remedies: LocalizedParagraph
)

enum class SadeSatiPhase {
    RISING, PEAK, SETTING, NOT_ACTIVE
}

data class JupiterTransitAnalysis(
    val currentTransitSign: ZodiacSign,
    val transitHouse: Int,
    val effects: LocalizedParagraph,
    val favorableForAreas: List<LifeArea>
)

data class NodalTransitAnalysis(
    val rahuTransitSign: ZodiacSign,
    val ketuTransitSign: ZodiacSign,
    val rahuTransitHouse: Int,
    val ketuTransitHouse: Int,
    val nodalAxisEffects: LocalizedParagraph,
    val duration: LocalizedParagraph
)

data class TransitInteraction(
    val transitingPlanet: Planet,
    val natalPlanet: Planet,
    val aspectType: String,
    val effect: LocalizedParagraph,
    val timing: LocalDate
)

data class YearlyPrediction(
    val year: Int,
    val overallTheme: LocalizedParagraph,
    val overallRating: StrengthLevel,
    val careerOutlook: LifeAreaOutlook,
    val relationshipOutlook: LifeAreaOutlook,
    val healthOutlook: LifeAreaOutlook,
    val wealthOutlook: LifeAreaOutlook,
    val keyMonths: List<KeyMonth>,
    val yearlyAdvice: LocalizedParagraph
)

data class LifeAreaOutlook(
    val area: LifeArea,
    val rating: StrengthLevel,
    val summary: LocalizedParagraph,
    val opportunities: List<LocalizedTrait>,
    val challenges: List<LocalizedTrait>,
    val advice: LocalizedParagraph
)

data class KeyMonth(
    val month: Int,
    val significance: LocalizedParagraph,
    val rating: StrengthLevel
)

data class MonthlyPrediction(
    val month: Int,
    val year: Int,
    val overallRating: StrengthLevel,
    val summary: LocalizedParagraph,
    val focusAreas: List<LifeArea>,
    val favorableDates: List<Int>,
    val challengingDates: List<Int>
)

enum class LifeArea {
    GENERAL, CAREER, RELATIONSHIP, HEALTH, WEALTH, EDUCATION, SPIRITUAL
}

data class LifeAreaPrediction(
    val area: LifeArea,
    val shortTermOutlook: LifeAreaOutlook,
    val mediumTermOutlook: LifeAreaOutlook,
    val longTermOutlook: LifeAreaOutlook,
    val timingAnalysis: TimingAnalysis,
    val recommendations: List<LocalizedParagraph>
)

data class TimingAnalysis(
    val favorablePeriods: List<TimingPeriod>,
    val challengingPeriods: List<TimingPeriod>,
    val peakPeriod: LocalizedParagraph
)

data class YogaActivationEvent(
    val yogaName: String,
    val activationPeriod: LocalizedParagraph,
    val expectedResults: LocalizedParagraph,
    val activationStrength: StrengthLevel
)

data class CriticalPeriod(
    val periodName: LocalizedParagraph,
    val startDate: LocalDate,
    val endDate: LocalDate,
    val areaAffected: LifeArea,
    val nature: CriticalPeriodNature,
    val intensity: StrengthLevel,
    val advice: LocalizedParagraph
)

enum class CriticalPeriodNature {
    CHALLENGING, TRANSFORMATIVE, KARMIC
}

data class OpportunityWindow(
    val windowName: LocalizedParagraph,
    val startDate: LocalDate,
    val endDate: LocalDate,
    val affectedAreas: List<LifeArea>,
    val opportunityType: LocalizedParagraph,
    val intensity: StrengthLevel,
    val advice: LocalizedParagraph
)

data class RemedialProfile(
    val gemstoneRemedies: List<GemstoneRemedy>,
    val mantraRemedies: List<MantraRemedy>,
    val charitableRemedies: List<CharitableRemedy>,
    val fastingRemedies: List<FastingRemedy>,
    val yogicRemedies: List<YogicRemedy>,
    val overallRemedialAdvice: LocalizedParagraph
)

data class GemstoneRemedy(
    val planet: Planet,
    val primaryGemstone: LocalizedParagraph,
    val alternativeGemstone: LocalizedParagraph,
    val wearingGuidelines: LocalizedParagraph,
    val cautions: LocalizedParagraph
)

data class MantraRemedy(
    val planet: Planet,
    val beejaMantra: String,
    val fullMantra: String,
    val chantCount: Int,
    val bestTime: LocalizedParagraph
)

data class CharitableRemedy(
    val planet: Planet,
    val donationItems: LocalizedParagraph,
    val bestDay: LocalizedParagraph,
    val guidelines: LocalizedParagraph
)

data class FastingRemedy(
    val planet: Planet,
    val fastingDay: LocalizedParagraph,
    val fastingType: LocalizedParagraph,
    val guidelines: LocalizedParagraph
)

data class YogicRemedy(
    val practiceName: LocalizedParagraph,
    val targetPlanet: Planet,
    val guidelines: LocalizedParagraph,
    val benefits: LocalizedParagraph
)
