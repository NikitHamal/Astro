package com.astro.storm.ephemeris.deepanalysis

import com.astro.storm.core.common.Language
import com.astro.storm.core.common.StringKeyInterface
import com.astro.storm.core.model.Planet
import com.astro.storm.core.model.ZodiacSign
import com.astro.storm.core.model.Nakshatra
import com.astro.storm.ephemeris.yoga.Yoga
import java.time.LocalDate

/**
 * Deep Analysis Models - Comprehensive data structures for detailed native analysis
 * 
 * These models represent the output of the Deep Analysis Engine, providing
 * multi-dimensional personality profiles, life area analyses, and predictions.
 * 
 * All text content uses StringKeyInterface for full localization support.
 * 
 * @author AstroStorm Deep Analysis Engine
 */

// ═══════════════════════════════════════════════════════════════════════════════
// CORE ANALYSIS RESULT
// ═══════════════════════════════════════════════════════════════════════════════

/**
 * Complete deep native analysis result containing all life area analyses
 */
data class DeepNativeAnalysis(
    val characterAnalysis: CharacterDeepResult,
    val careerAnalysis: CareerDeepResult,
    val relationshipAnalysis: RelationshipDeepResult,
    val healthAnalysis: HealthDeepResult,
    val wealthAnalysis: WealthDeepResult,
    val educationAnalysis: EducationDeepResult,
    val spiritualAnalysis: SpiritualDeepResult,
    val synthesisReport: SynthesisReport,
    val overallScore: Double,
    val analysisTimestamp: Long = System.currentTimeMillis()
)

/**
 * Synthesis report combining insights from all life areas
 */
data class SynthesisReport(
    val lifePurposeStatement: LocalizedParagraph,
    val coreStrengths: List<LocalizedTrait>,
    val coreChallenges: List<LocalizedTrait>,
    val keyLifeThemes: List<LocalizedParagraph>,
    val karmaIndicators: List<LocalizedParagraph>,
    val lifePathSummary: LocalizedParagraph
)

// ═══════════════════════════════════════════════════════════════════════════════
// CHARACTER & PERSONALITY MODELS
// ═══════════════════════════════════════════════════════════════════════════════

/**
 * Deep character analysis with multiple personality dimensions
 */
data class CharacterDeepResult(
    // Core Identity
    val ascendantAnalysis: AscendantDeepAnalysis,
    val moonAnalysis: MoonDeepAnalysis,
    val sunAnalysis: SunDeepAnalysis,
    val atmakarakaAnalysis: AtmakarakaAnalysis,
    
    // Personality Dimensions
    val temperament: TemperamentProfile,
    val emotionalPattern: EmotionalPattern,
    val intellectualProfile: IntellectualProfile,
    val socialBehavior: SocialBehaviorProfile,
    
    // Planetary Personality Impacts
    val strongPlanetInfluences: List<PlanetaryPersonalityInfluence>,
    val weakPlanetChallenges: List<PlanetaryPersonalityInfluence>,
    val retrogradeEffects: List<RetrogradeEffect>,
    
    // Yogas Affecting Personality
    val personalityYogas: List<YogaPersonalityEffect>,
    
    // Summary
    val personalitySummary: LocalizedParagraph,
    val personalityStrengthScore: Double
)

/**
 * Deep ascendant (Lagna) analysis
 */
data class AscendantDeepAnalysis(
    val sign: ZodiacSign,
    val degree: Double,
    val nakshatra: Nakshatra,
    val nakshatraPada: Int,
    val risingDegreeAnalysis: LocalizedParagraph,
    val ascendantLordPosition: AscendantLordAnalysis,
    val navamshaAscendantCorrelation: LocalizedParagraph,
    val physicalAppearance: LocalizedParagraph,
    val firstImpressionGiven: LocalizedParagraph,
    val lifeApproach: LocalizedParagraph,
    val overallAscendantInterpretation: LocalizedParagraph
)

/**
 * Ascendant lord position analysis (144 combinations)
 */
data class AscendantLordAnalysis(
    val planet: Planet,
    val housePosition: Int,
    val signPosition: ZodiacSign,
    val dignity: PlanetaryDignityLevel,
    val lordPositionInterpretation: LocalizedParagraph,
    val lifeDirectionIndicated: LocalizedParagraph
)

/**
 * Deep Moon analysis - emotional and psychological profile
 */
data class MoonDeepAnalysis(
    val sign: ZodiacSign,
    val housePosition: Int,
    val nakshatra: Nakshatra,
    val nakshatraPada: Int,
    val nakshatraPadaAnalysis: LocalizedParagraph,
    val moonStrengthLevel: StrengthLevel,
    val emotionalNature: LocalizedParagraph,
    val mindsetAnalysis: LocalizedParagraph,
    val innerNeeds: LocalizedParagraph,
    val stressResponse: LocalizedParagraph,
    val motherRelationship: LocalizedParagraph,
    val comfortSeeking: LocalizedParagraph,
    val kemadrumaYogaPresent: Boolean,
    val overallMoonInterpretation: LocalizedParagraph
)

/**
 * Deep Sun analysis - core identity and ego
 */
data class SunDeepAnalysis(
    val sign: ZodiacSign,
    val housePosition: Int,
    val nakshatra: Nakshatra,
    val sunStrengthLevel: StrengthLevel,
    val coreIdentity: LocalizedParagraph,
    val egoExpression: LocalizedParagraph,
    val authorityRelationship: LocalizedParagraph,
    val fatherRelationship: LocalizedParagraph,
    val leadershipStyle: LocalizedParagraph,
    val vitalityLevel: LocalizedParagraph,
    val overallSunInterpretation: LocalizedParagraph
)

/**
 * Atmakaraka (soul significator) analysis
 */
data class AtmakarakaAnalysis(
    val planet: Planet,
    val signPosition: ZodiacSign,
    val soulDesire: LocalizedParagraph,
    val karmicLesson: LocalizedParagraph,
    val spiritualPath: LocalizedParagraph,
    val overallAtmakarakaInterpretation: LocalizedParagraph
)

/**
 * Temperament profile based on element/modality dominance
 */
data class TemperamentProfile(
    val dominantElement: Element,
    val secondaryElement: Element,
    val dominantModality: Modality,
    val elementBalance: Map<Element, Double>,
    val modalityBalance: Map<Modality, Double>,
    val temperamentDescription: LocalizedParagraph,
    val naturalTendencies: List<LocalizedTrait>
)

/**
 * Emotional pattern analysis
 */
data class EmotionalPattern(
    val emotionalStability: StrengthLevel,
    val emotionalExpression: LocalizedParagraph,
    val emotionalTriggers: List<LocalizedTrait>,
    val emotionalStrengths: List<LocalizedTrait>,
    val emotionalChallenges: List<LocalizedTrait>,
    val emotionalGrowthPath: LocalizedParagraph
)

/**
 * Intellectual profile
 */
data class IntellectualProfile(
    val mercuryStrength: StrengthLevel,
    val learningStyle: LocalizedParagraph,
    val communicationStyle: LocalizedParagraph,
    val analyticalAbility: StrengthLevel,
    val creativeThinking: StrengthLevel,
    val intellectualStrengths: List<LocalizedTrait>,
    val intellectualChallenges: List<LocalizedTrait>
)

/**
 * Social behavior profile
 */
data class SocialBehaviorProfile(
    val socialOrientation: LocalizedParagraph,
    val relationshipApproach: LocalizedParagraph,
    val communicationInGroups: LocalizedParagraph,
    val leadershipOrFollower: LocalizedParagraph,
    val socialStrengths: List<LocalizedTrait>,
    val socialChallenges: List<LocalizedTrait>
)

/**
 * Planetary influence on personality
 */
data class PlanetaryPersonalityInfluence(
    val planet: Planet,
    val strengthLevel: StrengthLevel,
    val dignityLevel: PlanetaryDignityLevel,
    val personalityContribution: LocalizedParagraph,
    val traits: List<LocalizedTrait>
)

/**
 * Retrograde planet effect on personality
 */
data class RetrogradeEffect(
    val planet: Planet,
    val internalProcessing: LocalizedParagraph,
    val pastLifeKarma: LocalizedParagraph,
    val unusualApproach: LocalizedParagraph
)

/**
 * Yoga effect on personality
 */
data class YogaPersonalityEffect(
    val yogaName: String,
    val yogaStrength: StrengthLevel,
    val personalityEffect: LocalizedParagraph,
    val manifestationStyle: LocalizedParagraph
)

// ═══════════════════════════════════════════════════════════════════════════════
// CAREER & PROFESSION MODELS
// ═══════════════════════════════════════════════════════════════════════════════

/**
 * Deep career analysis result
 */
data class CareerDeepResult(
    // 10th House Analysis
    val tenthHouseAnalysis: TenthHouseDeepAnalysis,
    val tenthLordAnalysis: HouseLordDeepAnalysis,
    
    // Divisional Chart Integration
    val dashamshAnalysis: DashamshAnalysis,
    
    // Career Indicators
    val primaryCareerIndicators: List<CareerIndicator>,
    val secondaryCareerIndicators: List<CareerIndicator>,
    val careerYogas: List<CareerYoga>,
    
    // Profession Matching
    val suitableProfessions: List<ProfessionMatch>,
    val careerStrengths: List<LocalizedTrait>,
    val careerChallenges: List<LocalizedTrait>,
    
    // Work Style
    val workStyle: WorkStyleProfile,
    val employmentType: EmploymentTypeAnalysis,
    
    // Timing
    val careerTimeline: List<CareerTimingPeriod>,
    val currentCareerPhase: CareerPhaseAnalysis,
    
    // Financial Aspect
    val earningPotential: StrengthLevel,
    val earningPatterns: LocalizedParagraph,
    
    // Summary
    val careerSummary: LocalizedParagraph,
    val careerStrengthScore: Double
)

/**
 * Deep 10th house analysis
 */
data class TenthHouseDeepAnalysis(
    val sign: ZodiacSign,
    val planetsInHouse: List<PlanetInHouseAnalysis>,
    val aspectsReceived: List<AspectAnalysis>,
    val houseStrength: StrengthLevel,
    val publicImage: LocalizedParagraph,
    val careerEnvironment: LocalizedParagraph,
    val authorityDynamics: LocalizedParagraph
)

/**
 * House lord deep analysis (for any house)
 */
data class HouseLordDeepAnalysis(
    val lord: Planet,
    val housePosition: Int,
    val signPosition: ZodiacSign,
    val dignity: PlanetaryDignityLevel,
    val strengthLevel: StrengthLevel,
    val interpretation: LocalizedParagraph,
    val effectOnHouseMatters: LocalizedParagraph
)

/**
 * Planet in house analysis
 */
data class PlanetInHouseAnalysis(
    val planet: Planet,
    val dignity: PlanetaryDignityLevel,
    val effectOnHouse: LocalizedParagraph
)

/**
 * Aspect analysis
 */
data class AspectAnalysis(
    val aspectingPlanet: Planet,
    val aspectType: AspectType,
    val aspectStrength: Double,
    val effect: LocalizedParagraph
)

/**
 * Dashamsha (D10) chart analysis
 */
data class DashamshAnalysis(
    val d10Ascendant: ZodiacSign,
    val d10TenthSign: ZodiacSign,
    val d10SunPosition: D10PlanetPosition,
    val d10MoonPosition: D10PlanetPosition,
    val careerRefinement: LocalizedParagraph,
    val professionalGrowthPattern: LocalizedParagraph
)

/**
 * D10 planet position
 */
data class D10PlanetPosition(
    val planet: Planet,
    val sign: ZodiacSign,
    val house: Int,
    val interpretation: LocalizedParagraph
)

/**
 * Career indicator from chart
 */
data class CareerIndicator(
    val indicatorName: String,
    val sourcePlanet: Planet?,
    val sourceHouse: Int?,
    val strength: StrengthLevel,
    val description: LocalizedParagraph
)

/**
 * Career-specific yoga
 */
data class CareerYoga(
    val name: String,
    val strength: StrengthLevel,
    val careerEffect: LocalizedParagraph,
    val involvedPlanets: List<Planet>
)

/**
 * Profession match with suitability score
 */
data class ProfessionMatch(
    val professionCategory: ProfessionCategory,
    val specificRoles: List<String>,
    val suitabilityScore: Double,
    val reasonForMatch: LocalizedParagraph
)

/**
 * Profession categories
 */
enum class ProfessionCategory {
    GOVERNMENT_ADMINISTRATION,
    MILITARY_POLICE,
    MEDICINE_HEALTHCARE,
    LAW_JUDICIARY,
    EDUCATION_TEACHING,
    FINANCE_BANKING,
    TECHNOLOGY_IT,
    ARTS_ENTERTAINMENT,
    MEDIA_JOURNALISM,
    BUSINESS_COMMERCE,
    ENGINEERING_TECHNICAL,
    SCIENCE_RESEARCH,
    HOSPITALITY_SERVICE,
    REAL_ESTATE_CONSTRUCTION,
    AGRICULTURE_FARMING,
    SPIRITUALITY_RELIGIOUS,
    SPORTS_FITNESS,
    TRANSPORTATION_LOGISTICS,
    FOREIGN_SERVICES,
    FREELANCE_CONSULTING
}

/**
 * Work style profile
 */
data class WorkStyleProfile(
    val preferredEnvironment: LocalizedParagraph,
    val leadershipStyle: LocalizedParagraph,
    val teamworkApproach: LocalizedParagraph,
    val problemSolvingStyle: LocalizedParagraph,
    val stressHandling: LocalizedParagraph
)

/**
 * Employment type analysis
 */
data class EmploymentTypeAnalysis(
    val serviceVsBusiness: LocalizedParagraph,
    val employeeVsEmployer: LocalizedParagraph,
    val partnershipPotential: LocalizedParagraph,
    val foreignEmploymentPotential: LocalizedParagraph
)

/**
 * Career timing period
 */
data class CareerTimingPeriod(
    val startDate: LocalDate,
    val endDate: LocalDate,
    val dasha: String,
    val careerFocus: LocalizedParagraph,
    val opportunities: List<LocalizedTrait>,
    val challenges: List<LocalizedTrait>,
    val favorability: StrengthLevel
)

/**
 * Current career phase analysis
 */
data class CareerPhaseAnalysis(
    val currentDasha: String,
    val phaseDescription: LocalizedParagraph,
    val currentOpportunities: List<LocalizedTrait>,
    val currentChallenges: List<LocalizedTrait>,
    val advice: LocalizedParagraph
)

// ═══════════════════════════════════════════════════════════════════════════════
// RELATIONSHIP & MARRIAGE MODELS
// ═══════════════════════════════════════════════════════════════════════════════

/**
 * Deep relationship analysis result
 */
data class RelationshipDeepResult(
    // 7th House Analysis
    val seventhHouseAnalysis: SeventhHouseDeepAnalysis,
    val seventhLordAnalysis: HouseLordDeepAnalysis,
    
    // Key Planet Analysis
    val venusAnalysis: VenusDeepAnalysis,
    val jupiterAnalysis: JupiterMarriageAnalysis,
    
    // Partner Profile
    val partnerProfile: PartnerProfile,
    
    // Relationship Dynamics
    val relationshipStyle: RelationshipStyleProfile,
    val marriageYogas: List<MarriageYoga>,
    val relationshipChallenges: List<RelationshipChallenge>,
    
    // Navamsha Integration
    val navamshaAnalysis: NavamshaMarriageAnalysis,
    
    // Timing
    val marriageTiming: MarriageTimingAnalysis,
    val relationshipTimeline: List<RelationshipTimingPeriod>,
    
    // Quality Analysis
    val marriageQualityIndicators: MarriageQualityProfile,
    
    // Summary
    val relationshipSummary: LocalizedParagraph,
    val relationshipStrengthScore: Double
)

/**
 * Deep 7th house analysis
 */
data class SeventhHouseDeepAnalysis(
    val sign: ZodiacSign,
    val planetsInHouse: List<PlanetInHouseAnalysis>,
    val aspectsReceived: List<AspectAnalysis>,
    val houseStrength: StrengthLevel,
    val partnershipEnvironment: LocalizedParagraph,
    val publicDealings: LocalizedParagraph
)

/**
 * Deep Venus analysis for relationships
 */
data class VenusDeepAnalysis(
    val sign: ZodiacSign,
    val house: Int,
    val nakshatra: Nakshatra,
    val dignity: PlanetaryDignityLevel,
    val strengthLevel: StrengthLevel,
    val romanticNature: LocalizedParagraph,
    val attractionStyle: LocalizedParagraph,
    val pleasureSeeking: LocalizedParagraph,
    val loveExpression: LocalizedParagraph
)

/**
 * Jupiter analysis for marriage (especially for females)
 */
data class JupiterMarriageAnalysis(
    val sign: ZodiacSign,
    val house: Int,
    val dignity: PlanetaryDignityLevel,
    val husbandIndicator: LocalizedParagraph,
    val blessingsInMarriage: LocalizedParagraph
)

/**
 * Detailed partner profile predicted from chart
 */
data class PartnerProfile(
    val physicalDescription: LocalizedParagraph,
    val personalityTraits: List<LocalizedTrait>,
    val professionIndicators: LocalizedParagraph,
    val backgroundIndicators: LocalizedParagraph,
    val meetingCircumstances: LocalizedParagraph,
    val directionOfMeeting: String,
    val ageRelation: LocalizedParagraph
)

/**
 * Relationship style profile
 */
data class RelationshipStyleProfile(
    val attachmentStyle: LocalizedParagraph,
    val communicationInRelationship: LocalizedParagraph,
    val conflictResolution: LocalizedParagraph,
    val intimacyApproach: LocalizedParagraph,
    val loyaltyPattern: LocalizedParagraph
)

/**
 * Marriage-specific yoga
 */
data class MarriageYoga(
    val name: String,
    val strength: StrengthLevel,
    val effect: LocalizedParagraph,
    val isPositive: Boolean
)

/**
 * Relationship challenge from chart
 */
data class RelationshipChallenge(
    val challengeName: String,
    val severity: StrengthLevel,
    val description: LocalizedParagraph,
    val remedialAdvice: LocalizedParagraph
)

/**
 * Navamsha chart analysis for marriage
 */
data class NavamshaMarriageAnalysis(
    val d9Ascendant: ZodiacSign,
    val d9VenusPosition: D9PlanetPosition,
    val d9SeventhLordPosition: D9PlanetPosition,
    val marriageQualityFromD9: LocalizedParagraph,
    val spouseNatureFromD9: LocalizedParagraph
)

/**
 * D9 planet position
 */
data class D9PlanetPosition(
    val planet: Planet,
    val sign: ZodiacSign,
    val house: Int,
    val interpretation: LocalizedParagraph
)

/**
 * Marriage timing analysis
 */
data class MarriageTimingAnalysis(
    val timingCategory: MarriageTimingCategory,
    val estimatedAgeRange: String,
    val favorablePeriods: List<TimingPeriod>,
    val challengingPeriods: List<TimingPeriod>,
    val currentPeriodAnalysis: LocalizedParagraph,
    val timingAdvice: LocalizedParagraph
)

enum class MarriageTimingCategory {
    EARLY, NORMAL, DELAYED, LATE, UNCERTAIN
}

/**
 * Relationship timing period
 */
data class RelationshipTimingPeriod(
    val startDate: LocalDate,
    val endDate: LocalDate,
    val dasha: String,
    val relationshipFocus: LocalizedParagraph,
    val favorability: StrengthLevel
)

/**
 * Marriage quality profile
 */
data class MarriageQualityProfile(
    val overallQuality: StrengthLevel,
    val harmonyIndicators: List<LocalizedTrait>,
    val challengeIndicators: List<LocalizedTrait>,
    val growthPotential: LocalizedParagraph,
    val longevityIndicator: LocalizedParagraph
)

// ═══════════════════════════════════════════════════════════════════════════════
// HEALTH & LONGEVITY MODELS
// ═══════════════════════════════════════════════════════════════════════════════

/**
 * Deep health analysis result
 */
data class HealthDeepResult(
    // Constitution
    val constitutionAnalysis: ConstitutionAnalysis,
    
    // House Analysis
    val sixthHouseAnalysis: SixthHouseDeepAnalysis,
    val eighthHouseAnalysis: EighthHouseDeepAnalysis,
    
    // Body Mapping
    val bodySystemMapping: List<BodySystemAnalysis>,
    val vulnerableAreas: List<VulnerableArea>,
    
    // Planetary Health Impacts
    val planetaryHealthInfluences: List<PlanetaryHealthInfluence>,
    
    // Longevity
    val longevityAnalysis: LongevityAnalysis,
    
    // Health Timing
    val healthTimeline: List<HealthTimingPeriod>,
    val currentHealthPhase: LocalizedParagraph,
    
    // Recommendations
    val lifestyleRecommendations: List<LocalizedTrait>,
    val dietaryRecommendations: List<LocalizedTrait>,
    val preventiveMeasures: List<LocalizedTrait>,
    
    // Summary
    val healthSummary: LocalizedParagraph,
    val healthStrengthScore: Double
)

/**
 * Ayurvedic constitution analysis based on chart
 */
data class ConstitutionAnalysis(
    val primaryDosha: AyurvedicDosha,
    val secondaryDosha: AyurvedicDosha,
    val doshaBalance: Map<AyurvedicDosha, Double>,
    val constitutionDescription: LocalizedParagraph,
    val naturalStrengths: List<LocalizedTrait>,
    val naturalWeaknesses: List<LocalizedTrait>,
    val balancingAdvice: LocalizedParagraph
)

enum class AyurvedicDosha {
    VATA, PITTA, KAPHA
}

/**
 * Deep 6th house analysis
 */
data class SixthHouseDeepAnalysis(
    val sign: ZodiacSign,
    val sixthLord: HouseLordDeepAnalysis,
    val planetsInHouse: List<PlanetInHouseAnalysis>,
    val diseaseTerritory: LocalizedParagraph,
    val immunityStrength: StrengthLevel,
    val enemiesAndObstacles: LocalizedParagraph
)

/**
 * Deep 8th house analysis
 */
data class EighthHouseDeepAnalysis(
    val sign: ZodiacSign,
    val eighthLord: HouseLordDeepAnalysis,
    val planetsInHouse: List<PlanetInHouseAnalysis>,
    val chronicConditionRisk: StrengthLevel,
    val transformativeHealth: LocalizedParagraph,
    val hiddenHealthIssues: LocalizedParagraph
)

/**
 * Body system analysis based on Kalapurusha
 */
data class BodySystemAnalysis(
    val bodySystem: BodySystem,
    val associatedSign: ZodiacSign,
    val strengthLevel: StrengthLevel,
    val description: LocalizedParagraph
)

enum class BodySystem {
    HEAD_BRAIN,
    FACE_THROAT,
    ARMS_LUNGS,
    CHEST_STOMACH,
    HEART_SPINE,
    INTESTINES_NERVOUS,
    KIDNEYS_LOWER_BACK,
    REPRODUCTIVE_ORGANS,
    HIPS_THIGHS,
    KNEES_BONES,
    ANKLES_CIRCULATION,
    FEET_LYMPHATIC
}

/**
 * Vulnerable body area
 */
data class VulnerableArea(
    val bodyArea: String,
    val vulnerabilityLevel: StrengthLevel,
    val causingFactor: LocalizedParagraph,
    val preventiveAdvice: LocalizedParagraph
)

/**
 * Planetary influence on health
 */
data class PlanetaryHealthInfluence(
    val planet: Planet,
    val influenceType: HealthInfluenceType,
    val affectedAreas: List<String>,
    val description: LocalizedParagraph
)

enum class HealthInfluenceType {
    PROTECTIVE, NEUTRAL, CHALLENGING
}

/**
 * Longevity analysis
 */
data class LongevityAnalysis(
    val longevityCategory: LongevityCategory,
    val ascendantLordStrength: StrengthLevel,
    val eighthLordPosition: LocalizedParagraph,
    val saturnPosition: LocalizedParagraph,
    val longevityYogas: List<LocalizedTrait>,
    val lifeSpanIndicators: LocalizedParagraph
)

enum class LongevityCategory {
    ALPAYU, // Short
    MADHYAYU, // Medium
    POORNAYU // Long
}

/**
 * Health timing period
 */
data class HealthTimingPeriod(
    val startDate: LocalDate,
    val endDate: LocalDate,
    val dasha: String,
    val healthFocus: LocalizedParagraph,
    val vulnerabilities: List<LocalizedTrait>,
    val protectivePeriod: Boolean
)

// ═══════════════════════════════════════════════════════════════════════════════
// WEALTH & FINANCE MODELS
// ═══════════════════════════════════════════════════════════════════════════════

/**
 * Deep wealth analysis result
 */
data class WealthDeepResult(
    // House Analysis
    val secondHouseAnalysis: HouseLordDeepAnalysis,
    val eleventhHouseAnalysis: HouseLordDeepAnalysis,
    
    // Wealth Yogas
    val dhanaYogas: List<DhanaYogaAnalysis>,
    
    // Wealth Sources
    val primaryWealthSources: List<WealthSource>,
    val secondaryWealthSources: List<WealthSource>,
    
    // Financial Patterns
    val earningPattern: LocalizedParagraph,
    val savingPattern: LocalizedParagraph,
    val spendingPattern: LocalizedParagraph,
    val investmentTendency: LocalizedParagraph,
    
    // Special Calculations
    val induLagnaAnalysis: InduLagnaAnalysis,
    val sriLagnaAnalysis: SriLagnaAnalysis,
    
    // Timing
    val wealthTimeline: List<WealthTimingPeriod>,
    val currentWealthPhase: LocalizedParagraph,
    
    // Potential
    val wealthPotentialLevel: StrengthLevel,
    val wealthAccumulationPattern: LocalizedParagraph,
    
    // Summary
    val wealthSummary: LocalizedParagraph,
    val wealthStrengthScore: Double
)

/**
 * Dhana Yoga analysis
 */
data class DhanaYogaAnalysis(
    val yogaName: String,
    val yogaStrength: StrengthLevel,
    val involvedPlanets: List<Planet>,
    val wealthEffect: LocalizedParagraph,
    val manifestationTiming: LocalizedParagraph
)

/**
 * Wealth source from chart
 */
data class WealthSource(
    val sourceType: WealthSourceType,
    val sourceDescription: LocalizedParagraph,
    val potentialLevel: StrengthLevel,
    val relatedHouse: Int,
    val relatedPlanet: Planet?
)

enum class WealthSourceType {
    SELF_EARNED,
    INHERITANCE,
    SPOUSE,
    BUSINESS,
    PROFESSION,
    INVESTMENTS,
    SPECULATION,
    REAL_ESTATE,
    FOREIGN_SOURCES,
    GOVERNMENT,
    UNEXPECTED_GAINS
}

/**
 * Indu Lagna (wealth lagna) analysis
 */
data class InduLagnaAnalysis(
    val induLagnaSign: ZodiacSign,
    val induLagnaLord: Planet,
    val wealthIndicator: LocalizedParagraph
)

/**
 * Sri Lagna analysis
 */
data class SriLagnaAnalysis(
    val sriLagnaSign: ZodiacSign,
    val prosperityIndicator: LocalizedParagraph
)

/**
 * Wealth timing period
 */
data class WealthTimingPeriod(
    val startDate: LocalDate,
    val endDate: LocalDate,
    val dasha: String,
    val financialFocus: LocalizedParagraph,
    val opportunities: List<LocalizedTrait>,
    val cautions: List<LocalizedTrait>,
    val favorability: StrengthLevel
)

// ═══════════════════════════════════════════════════════════════════════════════
// EDUCATION MODELS
// ═══════════════════════════════════════════════════════════════════════════════

/**
 * Deep education analysis result
 */
data class EducationDeepResult(
    // House Analysis
    val fourthHouseAnalysis: HouseLordDeepAnalysis,
    val fifthHouseAnalysis: HouseLordDeepAnalysis,
    val ninthHouseAnalysis: HouseLordDeepAnalysis,
    
    // Key Planet Analysis
    val mercuryAnalysis: MercuryEducationAnalysis,
    val jupiterEducationAnalysis: JupiterEducationAnalysis,
    
    // Academic Profile
    val learningStyle: LocalizedParagraph,
    val academicStrengths: List<LocalizedTrait>,
    val academicChallenges: List<LocalizedTrait>,
    val concentrationAbility: StrengthLevel,
    val memoryStrength: StrengthLevel,
    
    // Specialization
    val favorableSubjects: List<SubjectMatch>,
    val researchAptitude: StrengthLevel,
    
    // Education Yogas
    val educationYogas: List<EducationYoga>,
    
    // Higher Education
    val higherEducationPotential: StrengthLevel,
    val foreignEducationPotential: StrengthLevel,
    
    // Timing
    val educationTimeline: List<EducationTimingPeriod>,
    
    // Summary
    val educationSummary: LocalizedParagraph,
    val educationStrengthScore: Double
)

/**
 * Mercury analysis for education
 */
data class MercuryEducationAnalysis(
    val sign: ZodiacSign,
    val house: Int,
    val dignity: PlanetaryDignityLevel,
    val intellectualCapacity: LocalizedParagraph,
    val communicationInLearning: LocalizedParagraph
)

/**
 * Jupiter analysis for education
 */
data class JupiterEducationAnalysis(
    val sign: ZodiacSign,
    val house: Int,
    val dignity: PlanetaryDignityLevel,
    val wisdomAcquisition: LocalizedParagraph,
    val teacherBlessings: LocalizedParagraph
)

/**
 * Subject match with aptitude score
 */
data class SubjectMatch(
    val subjectCategory: SubjectCategory,
    val specificSubjects: List<String>,
    val aptitudeScore: Double,
    val reasonForMatch: LocalizedParagraph
)

enum class SubjectCategory {
    SCIENCE_TECHNOLOGY,
    MATHEMATICS,
    COMMERCE_FINANCE,
    ARTS_HUMANITIES,
    MEDICINE_HEALTHCARE,
    LAW_POLITICS,
    ENGINEERING,
    LITERATURE_LANGUAGES,
    PHILOSOPHY_RELIGION,
    MANAGEMENT_BUSINESS,
    CREATIVE_ARTS
}

/**
 * Education yoga
 */
data class EducationYoga(
    val name: String,
    val strength: StrengthLevel,
    val academicEffect: LocalizedParagraph
)

/**
 * Education timing period
 */
data class EducationTimingPeriod(
    val startDate: LocalDate,
    val endDate: LocalDate,
    val dasha: String,
    val educationFocus: LocalizedParagraph,
    val favorability: StrengthLevel
)

// ═══════════════════════════════════════════════════════════════════════════════
// SPIRITUAL PATH MODELS
// ═══════════════════════════════════════════════════════════════════════════════

/**
 * Deep spiritual analysis result
 */
data class SpiritualDeepResult(
    // House Analysis
    val ninthHouseDharma: NinthHouseDeepAnalysis,
    val twelfthHouseMoksha: TwelfthHouseDeepAnalysis,
    
    // Key Planet Analysis
    val jupiterSpiritualAnalysis: JupiterSpiritualAnalysis,
    val ketuAnalysis: KetuSpiritualAnalysis,
    
    // Spiritual Profile
    val spiritualInclination: StrengthLevel,
    val faithOrientation: LocalizedParagraph,
    val philosophicalBent: LocalizedParagraph,
    val meditativeCapacity: StrengthLevel,
    
    // Spiritual Yogas
    val spiritualYogas: List<SpiritualYoga>,
    
    // Path Recommendations
    val suitablePaths: List<SpiritualPathMatch>,
    val recommendedPractices: List<LocalizedTrait>,
    val suitableDeities: List<LocalizedTrait>,
    val favorableMantras: List<LocalizedTrait>,
    
    // Karmic Indicators
    val karmicPatterns: List<KarmicPattern>,
    
    // Summary
    val spiritualSummary: LocalizedParagraph,
    val spiritualStrengthScore: Double
)

/**
 * Deep 9th house analysis
 */
data class NinthHouseDeepAnalysis(
    val sign: ZodiacSign,
    val ninthLord: HouseLordDeepAnalysis,
    val planetsInHouse: List<PlanetInHouseAnalysis>,
    val dharmaPath: LocalizedParagraph,
    val guruBlessings: LocalizedParagraph,
    val fortuneIndicators: LocalizedParagraph
)

/**
 * Deep 12th house analysis
 */
data class TwelfthHouseDeepAnalysis(
    val sign: ZodiacSign,
    val twelfthLord: HouseLordDeepAnalysis,
    val planetsInHouse: List<PlanetInHouseAnalysis>,
    val liberationPath: LocalizedParagraph,
    val spiritualExperiences: LocalizedParagraph,
    val lossesForGrowth: LocalizedParagraph
)

/**
 * Jupiter spiritual analysis
 */
data class JupiterSpiritualAnalysis(
    val sign: ZodiacSign,
    val house: Int,
    val dignity: PlanetaryDignityLevel,
    val wisdomPath: LocalizedParagraph,
    val spiritualTeachings: LocalizedParagraph
)

/**
 * Ketu spiritual analysis (past life karma)
 */
data class KetuSpiritualAnalysis(
    val sign: ZodiacSign,
    val house: Int,
    val nakshatra: Nakshatra,
    val pastLifeKarma: LocalizedParagraph,
    val detachmentArea: LocalizedParagraph,
    val spiritualGifts: LocalizedParagraph
)

/**
 * Spiritual yoga
 */
data class SpiritualYoga(
    val name: String,
    val strength: StrengthLevel,
    val spiritualEffect: LocalizedParagraph,
    val manifestationType: String
)

/**
 * Spiritual path match
 */
data class SpiritualPathMatch(
    val pathType: SpiritualPathType,
    val suitabilityScore: Double,
    val description: LocalizedParagraph
)

enum class SpiritualPathType {
    BHAKTI_DEVOTION,
    JNANA_KNOWLEDGE,
    KARMA_ACTION,
    RAJA_MEDITATION,
    TANTRA_RITUALS,
    SEVA_SERVICE
}

/**
 * Karmic pattern from chart
 */
data class KarmicPattern(
    val patternName: String,
    val description: LocalizedParagraph,
    val lessonToLearn: LocalizedParagraph
)

// ═══════════════════════════════════════════════════════════════════════════════
// COMMON UTILITY MODELS
// ═══════════════════════════════════════════════════════════════════════════════

/**
 * Localized paragraph with full EN/NE support
 */
data class LocalizedParagraph(
    val en: String,
    val ne: String
) {
    fun get(language: Language): String = when (language) {
        Language.ENGLISH -> en
        Language.NEPALI -> ne
    }
}

/**
 * Localized trait (short string)
 */
data class LocalizedTrait(
    val en: String,
    val ne: String,
    val strengthLevel: StrengthLevel? = null
) {
    fun get(language: Language): String = when (language) {
        Language.ENGLISH -> en
        Language.NEPALI -> ne
    }
}

/**
 * Timing period for predictions
 */
data class TimingPeriod(
    val startDate: LocalDate,
    val endDate: LocalDate,
    val description: LocalizedParagraph,
    val favorability: StrengthLevel
)

/**
 * Strength level enumeration
 */
enum class StrengthLevel(val value: Int, val displayName: String, val displayNameNe: String) {
    EXCELLENT(5, "Excellent", "उत्कृष्ट"),
    STRONG(4, "Strong", "बलियो"),
    MODERATE(3, "Moderate", "मध्यम"),
    WEAK(2, "Weak", "कमजोर"),
    AFFLICTED(1, "Afflicted", "पीडित")
}

/**
 * Planetary dignity level
 */
enum class PlanetaryDignityLevel {
    EXALTED,
    MOOLATRIKONA,
    OWN_SIGN,
    FRIEND_SIGN,
    NEUTRAL,
    ENEMY_SIGN,
    DEBILITATED
}

/**
 * Element enumeration
 */
enum class Element(val displayName: String, val displayNameNe: String) {
    FIRE("Fire", "अग्नि"),
    EARTH("Earth", "पृथ्वी"),
    AIR("Air", "वायु"),
    WATER("Water", "जल")
}

/**
 * Modality enumeration
 */
enum class Modality(val displayName: String, val displayNameNe: String) {
    CARDINAL("Cardinal", "चर"),
    FIXED("Fixed", "स्थिर"),
    MUTABLE("Mutable", "द्विस्वभाव")
}

/**
 * Aspect type
 */
enum class AspectType {
    CONJUNCTION,
    SEVENTH_ASPECT,
    SPECIAL_ASPECT // Mars 4th/8th, Jupiter 5th/9th, Saturn 3rd/10th
}
