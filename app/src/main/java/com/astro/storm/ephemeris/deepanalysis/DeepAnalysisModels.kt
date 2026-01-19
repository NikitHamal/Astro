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
 * Localized trait (short string)
 */
data class LocalizedTrait(
    val en: String,
    val ne: String,
    val strengthLevel: StrengthLevel = StrengthLevel.MODERATE
) {
    val name: String get() = en
    val nameNe: String get() = ne
    val strength: StrengthLevel get() = strengthLevel

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
    VERY_STRONG(4, "Very Strong", "धेरै बलियो"),
    STRONG(3, "Strong", "बलियो"),
    MODERATE(2, "Moderate", "मध्यम"),
    WEAK(1, "Weak", "कमजोर"),
    EXTREMELY_STRONG(6, "Extremely Strong", "अत्यधिक बलियो"),
    AFFLICTED(0, "Afflicted", "पीडित");

    companion object {
        fun fromInt(value: Int): StrengthLevel = entries.find { it.value == value } ?: MODERATE
        fun fromDouble(score: Double): StrengthLevel = when {
            score >= 80 -> EXCELLENT
            score >= 60 -> STRONG
            score >= 40 -> MODERATE
            score >= 20 -> WEAK
            else -> AFFLICTED
        }
    }
}

/**
 * Complete deep native analysis result containing all life area analyses
 */
data class DeepNativeAnalysis(
    val character: CharacterDeepResult,
    val career: CareerDeepResult,
    val relationship: RelationshipDeepResult,
    val health: HealthDeepResult,
    val wealth: WealthDeepResult,
    val education: EducationDeepResult,
    val spiritual: SpiritualDeepResult,
    val synthesisReport: SynthesisReport,
    val overallScore: Double,
    val analysisTimestamp: Long = System.currentTimeMillis()
) {
    // Aliases for compatibility with older code
    val characterAnalysis get() = character
    val careerAnalysis get() = career
    val relationshipAnalysis get() = relationship
    val healthAnalysis get() = health
    val wealthAnalysis get() = wealth
    val educationAnalysis get() = education
    val spiritualAnalysis get() = spiritual
}

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
    
    // Traits
    val keyTraits: List<LocalizedTrait>,
    
    val personalitySummary: LocalizedParagraph,
    val personalityStrengthScore: Double
) {
    // Aliases for compatibility
    val planetaryPersonalityImpacts get() = strongPlanetInfluences + weakPlanetChallenges
    val retrogradeAnalysis get() = retrogradeEffects
    val personalityYogaEffects get() = personalityYogas
    val personalityYogasPresent get() = personalityYogas
}

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
    val overallAscendantInterpretation: LocalizedParagraph,
    val overallAscendantStrength: StrengthLevel = StrengthLevel.MODERATE
) {
    val firstImpression: LocalizedParagraph get() = firstImpressionGiven
}

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
    val nakshatraCharacteristics: LocalizedParagraph = LocalizedParagraph("", ""),
    val overallMoonInterpretation: LocalizedParagraph
) {
    val moonSign: ZodiacSign get() = sign
    val overallEmotionalStrength: StrengthLevel get() = moonStrengthLevel
}

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
) {
    val sunSign: ZodiacSign get() = sign
    val sunStrength: StrengthLevel get() = sunStrengthLevel
}

/**
 * Atmakaraka (soul significator) analysis
 */
data class AtmakarakaAnalysis(
    val planet: Planet,
    val karakaName: String = planet.displayName,
    val signPosition: ZodiacSign,
    val soulDesire: LocalizedParagraph,
    val karmicLesson: LocalizedParagraph,
    val spiritualPath: LocalizedParagraph,
    val atmakarakaLessons: LocalizedParagraph = spiritualPath,
    val karmicTask: LocalizedParagraph = spiritualPath,
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
    val modalBalance: Map<Modality, Double> = modalityBalance,
    val elementBalanceMap: Map<Element, Double> = elementBalance,
    val gunaSummary: LocalizedParagraph = LocalizedParagraph("Guna balance provides psychological depth.", "गुण सन्तुलनले मनोवैज्ञानिक गहिराई प्रदान गर्दछ।"),
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
    // House Analysis
    val tenthHouseAnalysis: TenthHouseCareerAnalysis,
    val tenthLordAnalysis: HouseLordDeepAnalysis,
    val sixthHouseAnalysis: SixthHouseCareerAnalysis,
    
    // Divisional Chart
    val d10Analysis: DashamshaAnalysis,
    
    // Career Indicators
    val primaryCareerSignificators: List<PlanetaryCareerInfluence>,
    val bestCareerPaths: List<ProfessionMatch>,
    val careerStrengths: List<LocalizedTrait>,
    val careerChallenges: List<LocalizedTrait>,
    
    // Yogas
    val careerYogas: List<CareerYoga>,
    
    // Timing
    val careerTimeline: List<CareerTimingPeriod>,
    val currentCareerPhase: CurrentCareerPhase,
    
    // Summary
    val careerSummary: LocalizedParagraph,
    val careerStrengthScore: Double
) {
    // Aliases
    val dashamshaAnalysis get() = d10Analysis
    val careerYogaEffects get() = careerYogas
    val suitableProfessions get() = bestCareerPaths
    val workStyle get() = LocalizedParagraph("Calculated work style", "गणना गरिएको काम गर्ने शैली")
}

/**
 * Deep 10th house analysis
 */
data class TenthHouseCareerAnalysis(
    val sign: ZodiacSign,
    val planetsInHouse: List<PlanetInHouseAnalysis>,
    val houseStrength: StrengthLevel,
    val publicImage: LocalizedParagraph,
    val careerEnvironment: LocalizedParagraph,
    val authorityDynamics: LocalizedParagraph
)

data class SixthHouseCareerAnalysis(
    val sign: ZodiacSign,
    val planetsInHouse: List<PlanetInHouseAnalysis>,
    val houseStrength: StrengthLevel,
    val serviceQuotient: StrengthLevel,
    val workEnvironment: LocalizedParagraph,
    val competitionHandling: LocalizedParagraph
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
data class DashamshaAnalysis(
    val d10AscendantSign: ZodiacSign,
    val d10AscendantLord: Planet,
    val planetsInD10Houses: List<PlanetInHouseAnalysis>,
    val statusAndAuthority: LocalizedParagraph,
    val careerSignificance: LocalizedParagraph = statusAndAuthority,
    val professionalSuccess: LocalizedParagraph,
    val karmicDirectionInCareer: LocalizedParagraph,
    val overallD10Strength: StrengthLevel
)

data class PlanetaryCareerInfluence(
    val planet: Planet,
    val strengthLevel: StrengthLevel,
    val contribution: LocalizedParagraph,
    val favorableRoles: List<String>
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
    val involvedPlanets: List<Planet> = emptyList()
)

/**
 * Profession match with suitability score
 */
data class ProfessionMatch(
    val professionCategory: ProfessionCategory,
    val specificRoles: List<String>,
    val suitabilityScore: Double,
    val suitability: StrengthLevel = StrengthLevel.fromDouble(suitabilityScore),
    val professionName: String = professionCategory.displayName,
    val reasonForMatch: LocalizedParagraph
)

/**
 * Profession categories
 */
enum class ProfessionCategory(val displayName: String) {
    GOVERNMENT_ADMINISTRATION("Government & Administration"),
    MILITARY_POLICE("Military & Police"),
    MEDICINE_HEALTHCARE("Medicine & Healthcare"),
    LAW_JUDICIARY("Law & Judiciary"),
    EDUCATION_TEACHING("Education & Teaching"),
    FINANCE_BANKING("Finance & Banking"),
    TECHNOLOGY_IT("Technology & IT"),
    ARTS_ENTERTAINMENT("Arts & Entertainment"),
    MEDIA_JOURNALISM("Media & Journalism"),
    BUSINESS_COMMERCE("Business & Commerce"),
    ENGINEERING_TECHNICAL("Engineering & Technical"),
    SCIENCE_RESEARCH("Science & Research"),
    HOSPITALITY_SERVICE("Hospitality & Service"),
    REAL_ESTATE_CONSTRUCTION("Real Estate & Construction"),
    AGRICULTURE_FARMING("Agriculture & Farming"),
    SPIRITUALITY_RELIGIOUS("Spirituality & Religious"),
    SPORTS_FITNESS("Sports & Fitness"),
    TRANSPORTATION_LOGISTICS("Transportation & Logistics"),
    FOREIGN_SERVICES("Foreign Services"),
    FREELANCE_CONSULTING("Freelance & Consulting")
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
    val opportunities: List<LocalizedTrait> = emptyList(),
    val challenges: List<LocalizedTrait> = emptyList(),
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

data class CurrentHealthPhase(
    val currentDasha: String,
    val overallVitality: StrengthLevel,
    val currentFocus: LocalizedParagraph,
    val watchAreas: List<LocalizedTrait>,
    val recommendations: LocalizedParagraph,
    val vitalityLevel: StrengthLevel = overallVitality,
    val healthFocus: LocalizedParagraph = currentFocus,
    val vulnerabilities: List<LocalizedTrait> = watchAreas,
    val protectivePeriod: Boolean = false
)

data class CurrentCareerPhase(
    val currentDasha: String,
    val careerOutlook: StrengthLevel,
    val currentFocus: LocalizedParagraph,
    val advice: LocalizedParagraph
)


/**
 * Deep health analysis result
 */
data class HealthDeepResult(
    // Constitution
    val constitutionAnalysis: ConstitutionAnalysis,
    
    // House Analysis
    val sixthHouseAnalysis: SixthHouseHealthAnalysis,
    val eighthHouseAnalysis: EighthHouseHealthAnalysis,
    
    // Ascendant Health
    val ascendantHealthProfile: AscendantHealthProfile,
    
    // Body Mapping
    val bodyPartMapping: BodyPartMapping,
    val potentialVulnerabilities: List<HealthVulnerability>,
    val healthStrengths: List<LocalizedTrait>,
    val preventiveFocus: PreventiveFocus,
    
    // Body Mapping
    val bodySystemMapping: List<BodySystemAnalysis>,
    val vulnerableAreas: List<VulnerableArea>,
    
    // Planetary Health Impacts
    val planetaryHealthInfluences: List<PlanetaryHealthInfluence>,
    
    // Longevity
    val longevityIndicators: LongevityProfile,
    
    // Health Timing
    val healthTimeline: List<HealthTimingPeriod>,
    val currentHealthPhase: CurrentHealthPhase,
    
    // Recommendations
    val lifestyleRecommendations: List<LocalizedTrait>,
    val dietaryRecommendations: List<LocalizedTrait>,
    val preventiveMeasures: List<LocalizedTrait>,
    
    // Summary
    val healthSummary: LocalizedParagraph,
    val healthStrengthScore: Double
) {
    val dietaryRecommendationsList get() = dietaryRecommendations
}



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

typealias DoshaType = AyurvedicDosha

data class AscendantHealthProfile(
    val generalVitality: StrengthLevel,
    val physicalConstitutionType: LocalizedParagraph,
    val innateProwess: LocalizedParagraph,
    val bodyFrame: LocalizedParagraph,
    val metabolicType: LocalizedParagraph
)

data class BodyPartHealth(
    val strengthLevel: StrengthLevel,
    val observations: LocalizedParagraph,
    val recommendations: LocalizedParagraph
)

data class BodyPartMapping(
    val headAndFace: BodyPartHealth,
    val throatAndNeck: BodyPartHealth,
    val armsAndShoulders: BodyPartHealth,
    val chest: BodyPartHealth,
    val stomach: BodyPartHealth,
    val intestines: BodyPartHealth,
    val lowerAbdomen: BodyPartHealth,
    val reproductiveOrgans: BodyPartHealth,
    val thighs: BodyPartHealth,
    val knees: BodyPartHealth,
    val ankles: BodyPartHealth,
    val feet: BodyPartHealth
)

data class HealthVulnerability(
    val area: String,
    val severity: StrengthLevel,
    val description: LocalizedParagraph,
    val preventiveMeasures: LocalizedParagraph
)

data class PreventiveFocus(
    val priorityAreas: List<LocalizedTrait>,
    val seasonalGuidelines: LocalizedParagraph,
    val yogaPractices: LocalizedParagraph,
    val ayurvedicRecommendations: LocalizedParagraph
)

data class LongevityProfile(
    val category: LongevityCategory,
    val primaryIndicators: List<LocalizedTrait>,
    val supportingFactors: List<LocalizedTrait>,
    val challengingFactors: List<LocalizedTrait>,
    val longevitySummary: LocalizedParagraph
)

data class SixthHouseHealthAnalysis(
    val sign: ZodiacSign,
    val planetsInHouse: List<PlanetInHouseAnalysis>,
    val houseStrength: StrengthLevel,
    val diseaseResistance: StrengthLevel,
    val immuneSystemProfile: LocalizedParagraph,
    val potentialAilments: List<LocalizedTrait>,
    val healingCapacity: LocalizedParagraph
)

data class EighthHouseHealthAnalysis(
    val sign: ZodiacSign,
    val planetsInHouse: List<PlanetInHouseAnalysis>,
    val houseStrength: StrengthLevel,
    val chronicHealthPatterns: LocalizedParagraph,
    val regenerativeCapacity: LocalizedParagraph,
    val criticalPeriodIndicators: LocalizedParagraph
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
    val description: LocalizedParagraph,
    val healthArea: String = affectedAreas.firstOrNull() ?: "",
    val remedialMeasures: LocalizedParagraph = LocalizedParagraph("", ""),
    val strengthLevel: StrengthLevel = StrengthLevel.MODERATE,
    val influence: LocalizedParagraph = description
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
    val protectivePeriod: Boolean = false,
    val concernAreas: List<LocalizedTrait> = vulnerabilities,
    val favorability: StrengthLevel = StrengthLevel.MODERATE
)

// ═══════════════════════════════════════════════════════════════════════════════
// WEALTH & FINANCE MODELS
// ═══════════════════════════════════════════════════════════════════════════════

/**
 * Deep wealth analysis result
 */
data class WealthDeepResult(
    // House Analysis
    val secondHouseAnalysis: SecondHouseWealthAnalysis,
    val eleventhHouseAnalysis: EleventhHouseWealthAnalysis,
    
    // Wealth Yogas
    val dhanaYogaAnalysis: DhanaYogaAnalysis,
    
    // Wealth Sources
    val wealthSourceAnalysis: WealthSourceAnalysis,
    
    // Financial Strengths
    val financialStrengths: List<LocalizedTrait>,
    val financialChallenges: List<LocalizedTrait>,
    
    // Special Calculations
    val induLagnaAnalysis: InduLagnaAnalysis,
    
    // Timing
    val wealthTimeline: List<WealthTimingPeriod>,
    val currentWealthPhase: CurrentWealthPhase,
    
    // Investment
    val investmentIndicators: InvestmentProfile,
    
    // Summary
    val wealthSummary: LocalizedParagraph,
    val wealthStrengthScore: Double
)

/**
 * Dhana Yoga analysis
 */
data class DhanaYogaDetail(
    val name: String,
    val strength: StrengthLevel,
    val involvedPlanets: List<Planet>,
    val wealthEffect: LocalizedParagraph,
    val activationPeriods: LocalizedParagraph
)

data class DhanaYogaAnalysis(
    val presentYogas: List<DhanaYogaDetail>,
    val overallDhanaStrength: StrengthLevel,
    val wealthPotentialSummary: LocalizedParagraph
)

/**
 * Wealth source from chart
 */
data class WealthSource(
    val source: String,
    val sourceNe: String,
    val strength: StrengthLevel,
    val description: LocalizedParagraph
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
    val induLagnaStrength: StrengthLevel,
    val wealthFromInduLagna: LocalizedParagraph,
    @get:JvmName("getWealthFocus") val wealthFocus: LocalizedParagraph = wealthFromInduLagna,
    val significantPlanets: List<Planet> = emptyList()
)

data class SecondHouseWealthAnalysis(
    val sign: ZodiacSign,
    val planetsInHouse: List<PlanetInHouseAnalysis>,
    val houseStrength: StrengthLevel,
    val accumulationPattern: LocalizedParagraph,
    val speechAndFamily: LocalizedParagraph,
    val savingsAbility: StrengthLevel
)

data class EleventhHouseWealthAnalysis(
    val sign: ZodiacSign,
    val planetsInHouse: List<PlanetInHouseAnalysis>,
    val houseStrength: StrengthLevel,
    val gainsPattern: LocalizedParagraph,
    val incomeStreams: LocalizedParagraph,
    val networkWealth: LocalizedParagraph
)

data class WealthSourceAnalysis(
    val primarySources: List<WealthSource>,
    val secondarySources: List<WealthSource>,
    val passiveIncomeIndicators: LocalizedParagraph
)

data class CurrentWealthPhase(
    val currentDasha: String,
    val overallFinancialOutlook: StrengthLevel,
    val currentOpportunities: List<LocalizedTrait>,
    val currentCautions: List<LocalizedTrait>,
    val financialAdvice: LocalizedParagraph,
    @get:JvmName("getFinancialFocus") val financialFocus: LocalizedParagraph = financialAdvice,
    @get:JvmName("getGuidance") val guidance: LocalizedParagraph = financialAdvice
)

data class InvestmentProfile(
    val riskTolerance: StrengthLevel,
    val investmentStyle: LocalizedParagraph,
    val favorableInvestments: List<LocalizedTrait>,
    val cautionaryInvestments: List<LocalizedTrait>,
    val timingAdvice: LocalizedParagraph
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
    @get:JvmName("getWealthFocus") val wealthFocus: LocalizedParagraph = financialFocus,
    val opportunities: List<LocalizedTrait> = emptyList(),
    val cautions: List<LocalizedTrait> = emptyList(),
    val favorability: StrengthLevel = StrengthLevel.MODERATE
)

// ═══════════════════════════════════════════════════════════════════════════════
// EDUCATION MODELS
// ═══════════════════════════════════════════════════════════════════════════════

/**
 * Deep education analysis result
 */
data class EducationDeepResult(
    // House Analysis
    val fourthHouseAnalysis: FourthHouseEducationAnalysis,
    val fifthHouseAnalysis: FifthHouseEducationAnalysis,
    val ninthHouseAnalysis: NinthHouseEducationAnalysis,
    
    // Key Planet Analysis
    val mercuryAnalysis: MercuryEducationAnalysis,
    val jupiterEducationAnalysis: JupiterEducationAnalysis,
    
    // Academic Profile
    val learningStyleProfile: LearningStyleProfile,
    val academicStrengths: List<LocalizedTrait>,
    val academicChallenges: List<LocalizedTrait>,
    val concentrationAbility: StrengthLevel,
    val memoryStrength: StrengthLevel,
    
    // Specialization
    val suitableSubjects: List<SubjectMatch>,
    val researchAptitude: StrengthLevel,
    
    // Education Yogas
    val educationYogas: List<EducationYoga>,
    
    // Higher Education
    val higherEducationAnalysis: HigherEducationAnalysis,
    val higherEducationPotential: StrengthLevel = higherEducationAnalysis.overallPotential,
    val foreignEducationPotential: StrengthLevel = StrengthLevel.MODERATE,
    
    // Timing
    val educationTimeline: List<EducationTimingPeriod>,
    val currentEducationPhase: CurrentEducationPhase,
    
    // Summary
    val educationSummary: LocalizedParagraph,
    val educationStrengthScore: Double
) {
    val jupiterAnalysis get() = jupiterEducationAnalysis
    val learningStyle get() = learningStyleProfile
    val suitableSubjects get() = favorableSubjects
    val suitableSubjects get() = favorableSubjects
    val higherEducationIndicators get() = higherEducationAnalysis
}

data class LearningStyleProfile(
    val dominantStyle: LearningStyleType,
    val preferredMethods: List<LocalizedTrait>,
    val studyEnvironment: LocalizedParagraph,
    val concentrationAbility: StrengthLevel,
    val retentionCapacity: LocalizedParagraph
)

enum class LearningStyleType {
    ANALYTICAL, EXPERIENTIAL, INTUITIVE, VISUAL, AUDITORY, KINAESTHETIC
}

data class HigherEducationAnalysis(
    val overallPotential: StrengthLevel,
    val academicDegreeSuccess: LocalizedParagraph,
    val researchAbility: LocalizedParagraph,
    val foreignEducationProspects: LocalizedParagraph,
    val professionalCertifications: LocalizedParagraph
)

data class CurrentEducationPhase(
    val currentDasha: String,
    val learningFavorability: StrengthLevel,
    val currentFocus: LocalizedParagraph,
    val studyAdvice: LocalizedParagraph
)

/**
 * Specialized house analysis for education
 */
data class FourthHouseEducationAnalysis(
    val sign: ZodiacSign,
    val planetsInHouse: List<PlanetInHouseAnalysis>,
    val houseStrength: StrengthLevel,
    val primaryEducationPattern: LocalizedParagraph,
    val educationalEnvironment: LocalizedParagraph,
    val motherInfluenceOnEducation: LocalizedParagraph
)

data class FifthHouseEducationAnalysis(
    val sign: ZodiacSign,
    val planetsInHouse: List<PlanetInHouseAnalysis>,
    val houseStrength: StrengthLevel,
    val intellectualAbility: StrengthLevel,
    val creativeIntelligence: LocalizedParagraph,
    val memoryAndGrasp: LocalizedParagraph,
    val competitiveExamPotential: LocalizedParagraph
)

data class NinthHouseEducationAnalysis(
    val sign: ZodiacSign,
    val planetsInHouse: List<PlanetInHouseAnalysis>,
    val houseStrength: StrengthLevel,
    val higherEducationPotential: StrengthLevel,
    val philosophicalLearning: LocalizedParagraph,
    val foreignEducationPotential: LocalizedParagraph,
    val guruBlessings: LocalizedParagraph
)

/**
 * Mercury analysis for education
 */
data class MercuryEducationAnalysis(
    val sign: ZodiacSign,
    val house: Int,
    val dignity: PlanetaryDignityLevel,
    val strengthLevel: StrengthLevel = StrengthLevel.MODERATE,
    val intellectualCapacity: LocalizedParagraph,
    val analyticalAbility: LocalizedParagraph = intellectualCapacity,
    val communicationInLearning: LocalizedParagraph,
    val communicationSkill: LocalizedParagraph = communicationInLearning,
    val mathematicalAptitude: LocalizedParagraph = LocalizedParagraph("", ""),
    val languageAbility: LocalizedParagraph = LocalizedParagraph("", "")
) {
    val intellectualAbility get() = intellectualCapacity
}

/**
 * Jupiter analysis for education
 */
data class JupiterEducationAnalysis(
    val sign: ZodiacSign,
    val house: Int,
    val dignity: PlanetaryDignityLevel,
    val strengthLevel: StrengthLevel = StrengthLevel.MODERATE,
    val wisdomAcquisition: LocalizedParagraph,
    val wisdomDevelopment: LocalizedParagraph = wisdomAcquisition,
    val higherLearningAbility: LocalizedParagraph = LocalizedParagraph("", ""),
    val higherEducationAptitude: LocalizedParagraph = higherLearningAbility, // For analyzer typo
    val spiritualKnowledge: LocalizedParagraph = LocalizedParagraph("", ""),
    val teachingAbility: LocalizedParagraph = LocalizedParagraph("", ""),
    val teacherBlessings: LocalizedParagraph
)

/**
 * Subject match with aptitude score
 */
data class SubjectMatch(
    val subjectCategory: SubjectCategory,
    val specificSubjects: List<String>,
    val aptitudeScore: Double,
    val reasonForMatch: LocalizedParagraph,
    val affinity: StrengthLevel = StrengthLevel.fromDouble(aptitudeScore),
    val subjectName: String = subjectCategory.name.replace("_", " ").lowercase().capitalize()
)

typealias SubjectAffinity = SubjectMatch

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
    val academicEffect: LocalizedParagraph,
    val involvedPlanets: List<Planet> = emptyList()
)

/**
 * Education timing period
 */
data class EducationTimingPeriod(
    val startDate: LocalDate,
    val endDate: LocalDate,
    val dasha: String,
    val educationFocus: LocalizedParagraph,
    val favorability: StrengthLevel,
    val opportunities: List<LocalizedTrait> = emptyList(),
    val challenges: List<LocalizedTrait> = emptyList()
)

// ═══════════════════════════════════════════════════════════════════════════════
// SPIRITUAL PATH MODELS
// ═══════════════════════════════════════════════════════════════════════════════

/**
 * Deep spiritual analysis result
 */
data class SpiritualDeepResult(
    // House Analysis
    val ninthHouseDharma: NinthHouseDharmaAnalysis,
    val twelfthHouseMoksha: TwelfthHouseMokshaAnalysis,
    
    // Key Planet Analysis
    val atmakarakaAnalysis: AtmakarakaAnalysis,
    val jupiterAnalysis: JupiterSpiritualAnalysis,
    val ketuAnalysis: KetuSpiritualAnalysis,
    
    // Spiritual Profile
    val spiritualYogas: List<SpiritualYoga>,
    val karmicPatterns: List<KarmicPattern>,
    val spiritualStrengths: List<LocalizedTrait>,
    val spiritualChallenges: List<LocalizedTrait>,
    
    // Practices
    val meditationPractices: MeditationProfile,
    
    // Timing
    val spiritualTimeline: List<SpiritualTimingPeriod>,
    val currentSpiritualPhase: CurrentSpiritualPhase,
    
    // Summary
    val spiritualSummary: LocalizedParagraph,
    val spiritualStrengthScore: Double
)

/**
 * Deep 9th house analysis
 */
data class NinthHouseDharmaAnalysis(
    val sign: ZodiacSign,
    val planetsInHouse: List<PlanetInHouseAnalysis>,
    val houseStrength: StrengthLevel,
    val dharmaPath: LocalizedParagraph,
    val religiousInclination: LocalizedParagraph,
    val guruConnection: LocalizedParagraph,
    val higherPhilosophy: LocalizedParagraph
)

data class TwelfthHouseMokshaAnalysis(
    val sign: ZodiacSign,
    val planetsInHouse: List<PlanetInHouseAnalysis>,
    val houseStrength: StrengthLevel,
    val liberationPath: LocalizedParagraph,
    val meditationInclination: LocalizedParagraph,
    val asceticTendencies: LocalizedParagraph,
    val dreamLife: LocalizedParagraph
)

/**
 * Jupiter spiritual analysis
 */
data class JupiterSpiritualAnalysis(
    val sign: ZodiacSign,
    val house: Int,
    val dignity: PlanetaryDignityLevel,
    val wisdomDevelopment: LocalizedParagraph,
    val faithAndBelief: LocalizedParagraph,
    val strengthLevel: StrengthLevel = StrengthLevel.MODERATE,
    val spiritualGrowthPattern: LocalizedParagraph = wisdomDevelopment,
    val blessingsReceived: LocalizedParagraph = faithAndBelief,
    val wisdomPath: LocalizedParagraph = wisdomDevelopment,
    val spiritualTeachings: LocalizedParagraph = faithAndBelief
)

/**
 * Ketu spiritual analysis (past life karma)
 */
data class KetuSpiritualAnalysis(
    val sign: ZodiacSign,
    val house: Int,
    val dignity: PlanetaryDignityLevel = PlanetaryDignityLevel.NEUTRAL,
    val nakshatra: Nakshatra,
    val pastLifeKarma: LocalizedParagraph,
    val detachmentArea: LocalizedParagraph,
    val spiritualGifts: LocalizedParagraph,
    val detachmentAreas: LocalizedParagraph = detachmentArea,
    val spiritualTalents: LocalizedParagraph = spiritualGifts,
    val liberationIndicators: LocalizedParagraph = pastLifeKarma
)

/**
 * Spiritual yoga
 */
data class SpiritualYoga(
    val name: String,
    val strength: StrengthLevel,
    val spiritualEffect: LocalizedParagraph,
    val involvedPlanets: List<Planet>
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
    val areaAffected: LocalizedParagraph,
    val karmicLesson: LocalizedParagraph,
    val lessonToLearn: LocalizedParagraph,
    val growthOpportunities: LocalizedParagraph
)

data class MeditationProfile(
    val suitablePractices: List<LocalizedTrait>,
    val bestTimes: LocalizedParagraph,
    val mantras: LocalizedParagraph,
    val deities: LocalizedParagraph
)

data class SpiritualTimingPeriod(
    val startDate: LocalDate,
    val endDate: LocalDate,
    val dasha: String,
    val spiritualFocus: LocalizedParagraph,
    val opportunities: List<LocalizedTrait>,
    val challenges: List<LocalizedTrait>,
    val favorability: StrengthLevel
)

data class CurrentSpiritualPhase(
    val currentDasha: String,
    val spiritualGrowthPotential: StrengthLevel,
    val currentFocus: LocalizedParagraph,
    val practiceAdvice: LocalizedParagraph
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
