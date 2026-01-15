package com.astro.storm.ephemeris.nativeanalysis

import com.astro.storm.core.common.StringKeyNative
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

enum class StrengthLevel(val value: Int, val displayName: String, val displayNameNe: String) {
    EXCELLENT(5, "Excellent", "उत्कृष्ट"),
    STRONG(4, "Strong", "बलियो"),
    MODERATE(3, "Moderate", "मध्यम"),
    WEAK(2, "Weak", "कमजोर"),
    AFFLICTED(1, "Afflicted", "पीडित")
}

enum class Element(val displayName: String, val displayNameNe: String) {
    FIRE("Fire", "अग्नि"),
    EARTH("Earth", "पृथ्वी"),
    AIR("Air", "वायु"),
    WATER("Water", "जल")
}

enum class Modality(val displayName: String, val displayNameNe: String) {
    CARDINAL("Cardinal (Chara)", "चर"),
    FIXED("Fixed (Sthira)", "स्थिर"),
    MUTABLE("Mutable (Dwiswabhava)", "द्विस्वभाव")
}

enum class MarriageTiming(val displayName: String, val displayNameNe: String) {
    EARLY("Early (before 27)", "प्रारम्भिक (२७ अघि)"),
    NORMAL("Normal (27-32)", "सामान्य (२७-३२)"),
    DELAYED("Delayed (after 32)", "ढिलो (३२ पछि)")
}

enum class ConstitutionType(val displayName: String, val displayNameNe: String) {
    STRONG("Strong", "बलियो"),
    MODERATE("Moderate", "मध्यम"),
    SENSITIVE("Sensitive", "संवेदनशील")
}

enum class LongevityIndicator(val displayName: String, val displayNameNe: String) {
    LONG("Long Life", "दीर्घ आयु"),
    MEDIUM("Medium Life", "मध्यम आयु"),
    REQUIRES_CARE("Requires Care", "हेरचाह आवश्यक")
}

enum class PlanetaryDignity(val displayName: String, val displayNameNe: String) {
    EXALTED("Exalted", "उच्च"),
    MOOLATRIKONA("Moolatrikona", "मूलत्रिकोण"),
    OWN_SIGN("Own Sign", "स्वगृह"),
    FRIEND_SIGN("Friend's Sign", "मित्र राशि"),
    NEUTRAL_SIGN("Neutral Sign", "सम राशि"),
    ENEMY_SIGN("Enemy's Sign", "शत्रु राशि"),
    DEBILITATED("Debilitated", "नीच")
}
