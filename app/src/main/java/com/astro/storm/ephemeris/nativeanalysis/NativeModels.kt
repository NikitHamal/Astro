package com.astro.storm.ephemeris.nativeanalysis

import com.astro.storm.core.common.Language
import com.astro.storm.core.common.StringKeyNative
import com.astro.storm.core.common.StringResources
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
    val ascendantTrait: StringKeyNative,
    val moonTrait: StringKeyNative,
    val nakshatraInfluence: String,
    val nakshatraInfluenceNe: String,
    val personalityStrength: StrengthLevel,
    val dominantElement: Element,
    val dominantModality: Modality,
    val summaryEn: String,
    val summaryNe: String
)

data class CareerAnalysis(
    val tenthLord: Planet,
    val tenthLordHouse: Int,
    val tenthLordDignity: PlanetaryDignity,
    val tenthHousePlanets: List<Planet>,
    val careerIndicators: List<StringKeyNative>,
    val favorableFields: List<String>,
    val favorableFieldsNe: List<String>,
    val careerStrength: StrengthLevel,
    val summaryEn: String,
    val summaryNe: String
)

data class MarriageAnalysis(
    val seventhLord: Planet,
    val seventhLordHouse: Int,
    val seventhLordDignity: PlanetaryDignity,
    val venusPosition: PlanetPosition?,
    val venusStrength: StrengthLevel,
    val marriageTiming: MarriageTiming,
    val spouseNature: String,
    val spouseNatureNe: String,
    val relationshipStrength: StrengthLevel,
    val summaryEn: String,
    val summaryNe: String
)

data class HealthAnalysis(
    val ascendantStrength: StrengthLevel,
    val sixthLord: Planet,
    val eighthLord: Planet,
    val constitution: ConstitutionType,
    val vulnerableAreas: StringKeyNative,
    val longevityIndicator: LongevityIndicator,
    val healthConcerns: List<String>,
    val healthConcernsNe: List<String>,
    val summaryEn: String,
    val summaryNe: String
)

data class WealthAnalysis(
    val secondLord: Planet,
    val secondLordStrength: StrengthLevel,
    val eleventhLord: Planet,
    val eleventhLordStrength: StrengthLevel,
    val jupiterStrength: StrengthLevel,
    val dhanaYogaPresent: Boolean,
    val primarySources: List<String>,
    val primarySourcesNe: List<String>,
    val wealthPotential: StrengthLevel,
    val summaryEn: String,
    val summaryNe: String
)

data class EducationAnalysis(
    val fourthLord: Planet,
    val fifthLord: Planet,
    val mercuryStrength: StrengthLevel,
    val jupiterAspectOnEducation: Boolean,
    val favorableSubjects: List<String>,
    val favorableSubjectsNe: List<String>,
    val academicPotential: StrengthLevel,
    val summaryEn: String,
    val summaryNe: String
)

data class SpiritualAnalysis(
    val ninthLord: Planet,
    val twelfthLord: Planet,
    val ketuPosition: PlanetPosition?,
    val jupiterStrength: StrengthLevel,
    val spiritualInclination: StrengthLevel,
    val recommendedPractices: List<String>,
    val recommendedPracticesNe: List<String>,
    val summaryEn: String,
    val summaryNe: String
)

data class TraitInfo(
    val trait: StringKeyNative,
    val strength: StrengthLevel,
    val planet: Planet?
)

enum class StrengthLevel(val value: Int, val stringKey: StringKeyNative) {
    EXCELLENT(5, StringKeyNative.STRENGTH_EXCELLENT),
    STRONG(4, StringKeyNative.STRENGTH_STRONG),
    MODERATE(3, StringKeyNative.STRENGTH_MODERATE),
    WEAK(2, StringKeyNative.STRENGTH_WEAK),
    AFFLICTED(1, StringKeyNative.STRENGTH_AFFLICTED);

    fun getLocalizedName(language: Language): String = StringResources.get(stringKey, language)
}

enum class Element(val stringKey: StringKeyNative) {
    FIRE(StringKeyNative.ELEMENT_FIRE),
    EARTH(StringKeyNative.ELEMENT_EARTH),
    AIR(StringKeyNative.ELEMENT_AIR),
    WATER(StringKeyNative.ELEMENT_WATER);

    fun getLocalizedName(language: Language): String = StringResources.get(stringKey, language)
}

enum class Modality(val stringKey: StringKeyNative) {
    CARDINAL(StringKeyNative.MODALITY_CARDINAL),
    FIXED(StringKeyNative.MODALITY_FIXED),
    MUTABLE(StringKeyNative.MODALITY_MUTABLE);

    fun getLocalizedName(language: Language): String = StringResources.get(stringKey, language)
}

enum class MarriageTiming(val stringKey: StringKeyNative) {
    EARLY(StringKeyNative.MARRIAGE_TIMING_LABEL_EARLY),
    NORMAL(StringKeyNative.MARRIAGE_TIMING_LABEL_NORMAL),
    DELAYED(StringKeyNative.MARRIAGE_TIMING_LABEL_DELAYED);

    fun getLocalizedName(language: Language): String = StringResources.get(stringKey, language)
}

enum class ConstitutionType(val stringKey: StringKeyNative) {
    STRONG(StringKeyNative.CONSTITUTION_LABEL_STRONG),
    MODERATE(StringKeyNative.CONSTITUTION_LABEL_MODERATE),
    SENSITIVE(StringKeyNative.CONSTITUTION_LABEL_SENSITIVE);

    fun getLocalizedName(language: Language): String = StringResources.get(stringKey, language)
}

enum class LongevityIndicator(val stringKey: StringKeyNative) {
    LONG(StringKeyNative.LONGEVITY_LABEL_LONG),
    MEDIUM(StringKeyNative.LONGEVITY_LABEL_MEDIUM),
    REQUIRES_CARE(StringKeyNative.LONGEVITY_LABEL_REQUIRES_CARE);

    fun getLocalizedName(language: Language): String = StringResources.get(stringKey, language)
}

enum class PlanetaryDignity(val stringKey: StringKeyNative) {
    EXALTED(StringKeyNative.DIGNITY_EXALTED),
    MOOLATRIKONA(StringKeyNative.DIGNITY_MOOLATRIKONA),
    OWN_SIGN(StringKeyNative.DIGNITY_OWN_SIGN),
    FRIEND_SIGN(StringKeyNative.DIGNITY_FRIEND_SIGN),
    NEUTRAL_SIGN(StringKeyNative.DIGNITY_NEUTRAL_SIGN),
    ENEMY_SIGN(StringKeyNative.DIGNITY_ENEMY_SIGN),
    DEBILITATED(StringKeyNative.DIGNITY_DEBILITATED);

    fun getLocalizedName(language: Language): String = StringResources.get(stringKey, language)
}
