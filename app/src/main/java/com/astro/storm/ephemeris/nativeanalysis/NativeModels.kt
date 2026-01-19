package com.astro.storm.ephemeris.nativeanalysis

import com.astro.storm.core.common.InsightSection
import com.astro.storm.core.common.LocalizedText
import com.astro.storm.core.common.StringKeyInterface
import com.astro.storm.core.common.StringKeyNative
import com.astro.storm.core.common.StringKeyNativeTraits
import com.astro.storm.core.model.Planet
import com.astro.storm.core.model.PlanetPosition
import com.astro.storm.core.model.ZodiacSign

data class NativeAnalysisResult(
    val characterAnalysis: CharacterAnalysis,
    val careerAnalysis: CareerAnalysis,
    val marriageAnalysis: MarriageAnalysis,
    val healthAnalysis: HealthAnalysis,
    val wealthAnalysis: WealthAnalysis,
    val educationAnalysis: EducationAnalysis,
    val spiritualAnalysis: SpiritualAnalysis,
    val keyStrengths: List<TraitInfo>,
    val keyChallenges: List<TraitInfo>,
    val overallScore: Double
)

data class CharacterAnalysis(
    val ascendantSign: ZodiacSign,
    val moonSign: ZodiacSign,
    val sunSign: ZodiacSign,
    val ascendantTrait: StringKeyInterface,
    val moonTrait: StringKeyInterface,
    val nakshatraInfluence: LocalizedText,
    val personalityStrength: StrengthLevel,
    val dominantElement: Element,
    val dominantModality: Modality,
    val summary: LocalizedText,
    val insights: List<InsightSection>
)

data class CareerAnalysis(
    val tenthLord: Planet,
    val tenthLordHouse: Int,
    val tenthLordDignity: PlanetaryDignity,
    val tenthHousePlanets: List<Planet>,
    val careerIndicators: List<StringKeyInterface>,
    val favorableFields: List<StringKeyInterface>,
    val careerStrength: StrengthLevel,
    val summary: LocalizedText,
    val insights: List<InsightSection>
)

data class MarriageAnalysis(
    val seventhLord: Planet,
    val seventhLordHouse: Int,
    val seventhLordDignity: PlanetaryDignity,
    val venusPosition: PlanetPosition?,
    val venusStrength: StrengthLevel,
    val marriageTiming: MarriageTiming,
    val spouseNatureTraits: List<StringKeyInterface>,
    val relationshipStrength: StrengthLevel,
    val summary: LocalizedText,
    val insights: List<InsightSection>
)

data class HealthAnalysis(
    val ascendantStrength: StrengthLevel,
    val sixthLord: Planet,
    val eighthLord: Planet,
    val constitution: ConstitutionType,
    val vulnerableAreas: StringKeyInterface,
    val longevityIndicator: LongevityIndicator,
    val healthConcerns: List<LocalizedText>,
    val summary: LocalizedText,
    val insights: List<InsightSection>
)

data class WealthAnalysis(
    val secondLord: Planet,
    val secondLordStrength: StrengthLevel,
    val eleventhLord: Planet,
    val eleventhLordStrength: StrengthLevel,
    val jupiterStrength: StrengthLevel,
    val dhanaYogaPresent: Boolean,
    val primarySources: List<StringKeyInterface>,
    val wealthPotential: StrengthLevel,
    val summary: LocalizedText,
    val insights: List<InsightSection>
)

data class EducationAnalysis(
    val fourthLord: Planet,
    val fifthLord: Planet,
    val mercuryStrength: StrengthLevel,
    val jupiterAspectOnEducation: Boolean,
    val favorableSubjects: List<StringKeyInterface>,
    val academicPotential: StrengthLevel,
    val summary: LocalizedText,
    val insights: List<InsightSection>
)

data class SpiritualAnalysis(
    val ninthLord: Planet,
    val twelfthLord: Planet,
    val ketuPosition: PlanetPosition?,
    val jupiterStrength: StrengthLevel,
    val spiritualInclination: StrengthLevel,
    val recommendedPractices: List<StringKeyInterface>,
    val summary: LocalizedText,
    val insights: List<InsightSection>
)

data class TraitInfo(
    val trait: StringKeyInterface,
    val strength: StrengthLevel,
    val planet: Planet?
)

enum class StrengthLevel(val value: Int, val labelKey: StringKeyInterface) {
    EXCELLENT(5, StringKeyNative.STRENGTH_EXCELLENT),
    STRONG(4, StringKeyNative.STRENGTH_STRONG),
    MODERATE(3, StringKeyNative.STRENGTH_MODERATE),
    WEAK(2, StringKeyNative.STRENGTH_WEAK),
    AFFLICTED(1, StringKeyNative.STRENGTH_AFFLICTED)
}

enum class Element(val labelKey: StringKeyInterface) {
    FIRE(StringKeyNativeTraits.ELEMENT_FIRE),
    EARTH(StringKeyNativeTraits.ELEMENT_EARTH),
    AIR(StringKeyNativeTraits.ELEMENT_AIR),
    WATER(StringKeyNativeTraits.ELEMENT_WATER)
}

enum class Modality(val labelKey: StringKeyInterface) {
    CARDINAL(StringKeyNativeTraits.MODALITY_CARDINAL),
    FIXED(StringKeyNativeTraits.MODALITY_FIXED),
    MUTABLE(StringKeyNativeTraits.MODALITY_MUTABLE)
}

enum class MarriageTiming(val labelKey: StringKeyInterface) {
    EARLY(StringKeyNative.MARRIAGE_TIMING_EARLY),
    NORMAL(StringKeyNative.MARRIAGE_TIMING_NORMAL),
    DELAYED(StringKeyNative.MARRIAGE_TIMING_DELAYED)
}

enum class ConstitutionType(val labelKey: StringKeyInterface) {
    STRONG(StringKeyNativeTraits.CONSTITUTION_STRONG),
    MODERATE(StringKeyNativeTraits.CONSTITUTION_MODERATE),
    SENSITIVE(StringKeyNativeTraits.CONSTITUTION_SENSITIVE)
}

enum class LongevityIndicator(val labelKey: StringKeyInterface) {
    LONG(StringKeyNativeTraits.LONGEVITY_LONG),
    MEDIUM(StringKeyNativeTraits.LONGEVITY_MEDIUM),
    REQUIRES_CARE(StringKeyNativeTraits.LONGEVITY_REQUIRES_CARE)
}

enum class PlanetaryDignity(val labelKey: StringKeyInterface) {
    EXALTED(StringKeyNativeTraits.DIGNITY_EXALTED),
    MOOLATRIKONA(StringKeyNativeTraits.DIGNITY_MOOLATRIKONA),
    OWN_SIGN(StringKeyNativeTraits.DIGNITY_OWN_SIGN),
    FRIEND_SIGN(StringKeyNativeTraits.DIGNITY_FRIEND_SIGN),
    NEUTRAL_SIGN(StringKeyNativeTraits.DIGNITY_NEUTRAL_SIGN),
    ENEMY_SIGN(StringKeyNativeTraits.DIGNITY_ENEMY_SIGN),
    DEBILITATED(StringKeyNativeTraits.DIGNITY_DEBILITATED)
}
