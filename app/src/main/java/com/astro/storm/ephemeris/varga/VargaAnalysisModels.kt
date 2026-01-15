package com.astro.storm.ephemeris.varga

import com.astro.storm.core.common.Language
import com.astro.storm.core.common.StringKeyDivisional
import com.astro.storm.core.common.StringKeyDosha
import com.astro.storm.core.common.StringResources
import com.astro.storm.core.model.*

// Data classes for analysis results
data class HoraAnalysis(
    val sunHoraPlanets: List<Planet>,
    val moonHoraPlanets: List<Planet>,
    val wealthIndicators: List<WealthIndicator>,
    val overallWealthPotential: WealthPotential,
    val secondLordHoraSign: ZodiacSign?,
    val eleventhLordHoraSign: ZodiacSign?,
    val wealthTimingPeriods: List<WealthTimingPeriod>,
    val recommendations: List<String>
)

data class WealthIndicator(
    val planet: Planet,
    val type: WealthType,
    val strength: Double,
    val sources: List<String>
)

enum class WealthType { SELF_EARNED, INHERITED_LIQUID }
enum class WealthPotential { 
    EXCEPTIONAL, HIGH, MODERATE, AVERAGE, LOW;
    
    fun getLocalizedName(language: Language): String {
        val key = when(this) {
            EXCEPTIONAL -> StringKeyDosha.HORA_POTENTIAL_EXCEPTIONAL
            HIGH -> StringKeyDosha.HORA_POTENTIAL_HIGH
            MODERATE -> StringKeyDosha.HORA_POTENTIAL_MODERATE
            AVERAGE -> StringKeyDosha.HORA_POTENTIAL_AVERAGE
            LOW -> StringKeyDosha.HORA_POTENTIAL_NEEDS_EFFORT
        }
        return StringResources.get(key, language)
    }
}

data class WealthTimingPeriod(
    val planet: Planet,
    val type: WealthType,
    val periodDescription: String,
    val favorableForWealth: Boolean,
    val wealthSources: List<String>
)

data class DrekkanaAnalysis(
    val marsInDrekkana: PlanetPosition?,
    val thirdLordPosition: PlanetPosition?,
    val siblingIndicators: SiblingIndicators,
    val courageAnalysis: CourageAnalysis,
    val communicationSkills: CommunicationAnalysis,
    val thirdHousePlanets: List<PlanetPosition>,
    val eleventhHousePlanets: List<PlanetPosition>,
    val shortJourneyIndicators: List<String>,
    val recommendations: List<String>
)

data class SiblingIndicators(
    val estimatedYoungerSiblings: IntRange,
    val estimatedElderSiblings: IntRange,
    val relationshipQuality: RelationshipQuality,
    val youngerSiblingPlanets: List<Planet>,
    val elderSiblingPlanets: List<Planet>,
    val siblingWelfareIndicators: List<String>
)

enum class RelationshipQuality { 
    EXCELLENT, GOOD, NEUTRAL, CHALLENGING, DIFFICULT;
    
    fun getLocalizedName(language: Language): String {
        val key = when(this) {
            EXCELLENT -> StringKeyDivisional.REL_QUAL_EXCELLENT
            GOOD -> StringKeyDivisional.REL_QUAL_GOOD
            NEUTRAL -> StringKeyDivisional.REL_QUAL_NEUTRAL
            CHALLENGING -> StringKeyDivisional.REL_QUAL_CHALLENGING
            DIFFICULT -> StringKeyDivisional.REL_QUAL_DIFFICULT
        }
        return StringResources.get(key, language)
    }
}

data class CourageAnalysis(
    val overallCourageLevel: CourageLevel,
    val marsStrength: Double,
    val initiativeAbility: String,
    val physicalCourage: String,
    val mentalCourage: String
)

enum class CourageLevel { 
    EXCEPTIONAL, HIGH, MODERATE, LOW, VERY_LOW;
    
    fun getLocalizedName(language: Language): String {
        val key = when(this) {
            EXCEPTIONAL -> StringKeyDosha.COURAGE_EXCEPTIONAL
            HIGH -> StringKeyDosha.COURAGE_HIGH
            MODERATE -> StringKeyDosha.COURAGE_MODERATE
            LOW -> StringKeyDosha.COURAGE_DEVELOPING
            VERY_LOW -> StringKeyDosha.COURAGE_NEEDS_WORK
        }
        return StringResources.get(key, language)
    }
}

data class CommunicationAnalysis(
    val overallSkillLevel: String,
    val writingAbility: String,
    val speakingAbility: String,
    val artisticTalents: List<String>,
    val mercuryStrength: Double
)

data class NavamsaMarriageAnalysis(
    val venusInNavamsa: PlanetPosition?,
    val jupiterInNavamsa: PlanetPosition?,
    val seventhLordNavamsa: PlanetPosition?,
    val navamsaLagnaLordPosition: PlanetPosition?,
    val upapadaSign: ZodiacSign,
    val upapadaLordNavamsa: PlanetPosition?,
    val darakaraka: Planet,
    val darakarakaNavamsa: PlanetPosition?,
    val marriageTimingFactors: MarriageTimingFactors,
    val spouseCharacteristics: SpouseCharacteristics,
    val spouseDirection: String,
    val multipleMarriageIndicators: MultipleMarriageIndicators,
    val marriageMuhurtaCompatibility: String,
    val recommendations: List<String>
)

data class MarriageTimingFactors(
    val favorableDashaPlanets: List<Planet>,
    val venusNavamsaStrength: Double,
    val seventhLordStrength: Double,
    val darakarakaStrength: Double,
    val transitConsiderations: String
)

data class SpouseCharacteristics(
    val generalNature: String,
    val physicalTraits: String,
    val probableProfessions: List<String>,
    val familyBackground: String
)

data class MultipleMarriageIndicators(
    val hasStrongIndicators: Boolean,
    val riskFactors: List<String>,
    val mitigatingFactors: List<String>
)

data class DashamsaAnalysis(
    val tenthLordInDashamsa: PlanetPosition?,
    val sunInDashamsa: PlanetPosition?,
    val saturnInDashamsa: PlanetPosition?,
    val mercuryInDashamsa: PlanetPosition?,
    val dashamsaLagna: ZodiacSign,
    val careerTypes: List<CareerType>,
    val industryMappings: Map<Planet, List<String>>,
    val governmentServicePotential: GovernmentServiceAnalysis,
    val businessVsServiceAptitude: BusinessVsServiceAnalysis,
    val careerPeakPeriods: List<CareerPeakPeriod>,
    val multipleCareerIndicators: List<String>,
    val professionalStrengths: List<String>,
    val recommendations: List<String>
)

data class CareerType(
    val name: String,
    val industries: List<String>,
    val suitability: String
)

data class GovernmentServiceAnalysis(
    val potential: String,
    val favorableFactors: List<String>,
    val recommendedDepartments: List<String>
)

data class BusinessVsServiceAnalysis(
    val businessAptitude: Int,
    val serviceAptitude: Int,
    val recommendation: String,
    val businessSectors: List<String>
)

data class CareerPeakPeriod(
    val planet: Planet,
    val description: String,
    val significance: String
)

data class DwadasamsaAnalysis(
    val sunInDwadasamsa: PlanetPosition?,
    val moonInDwadasamsa: PlanetPosition?,
    val ninthLordPosition: PlanetPosition?,
    val fourthLordPosition: PlanetPosition?,
    val fatherAnalysis: ParentAnalysis,
    val motherAnalysis: ParentAnalysis,
    val inheritanceIndicators: InheritanceAnalysis,
    val ancestralPropertyIndicators: List<String>,
    val familyLineageInsights: List<String>,
    val parentalLongevityIndicators: ParentalLongevityIndicators,
    val recommendations: List<String>
)

data class ParentAnalysis(
    val parent: String,
    val significatorStrength: Double,
    val houseLordStrength: Double,
    val overallWellbeing: String,
    val characteristics: String,
    val relationship: String
)

data class InheritanceAnalysis(
    val potential: String,
    val sources: List<String>,
    val timing: String
)

data class ParentalLongevityIndicators(
    val fatherLongevity: String,
    val motherLongevity: String,
    val healthConcerns: List<String>
)
