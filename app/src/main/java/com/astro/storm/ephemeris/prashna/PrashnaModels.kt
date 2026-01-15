package com.astro.storm.ephemeris.prashna

import com.astro.storm.core.common.Language
import com.astro.storm.core.common.StringKeyAnalysis
import com.astro.storm.core.common.StringResources
import com.astro.storm.core.model.Nakshatra
import com.astro.storm.core.model.Planet
import com.astro.storm.core.model.PlanetPosition
import com.astro.storm.core.model.VedicChart
import com.astro.storm.core.model.ZodiacSign
import java.time.LocalDateTime

/**
 * Main Prashna categories for question classification
 */
enum class PrashnaCategory(val displayName: String, val description: String) {
    YES_NO("Yes/No", "Simple yes or no questions"),
    CAREER("Career", "Job, profession, and career-related questions"),
    MARRIAGE("Marriage", "Marriage and spouse-related questions"),
    CHILDREN("Children", "Questions about children and progeny"),
    HEALTH("Health", "Health and illness-related questions"),
    WEALTH("Wealth", "Financial and wealth-related questions"),
    PROPERTY("Property", "Real estate and property questions"),
    TRAVEL("Travel", "Journey and travel-related questions"),
    EDUCATION("Education", "Studies and educational questions"),
    LEGAL("Legal", "Court cases and legal matters"),
    LOST_OBJECT("Lost Object", "Finding lost or stolen items"),
    RELATIONSHIP("Relationship", "Love and relationship questions"),
    BUSINESS("Business", "Business partnership and deals"),
    SPIRITUAL("Spiritual", "Spiritual and religious questions"),
    GENERAL("General", "General questions and queries");

    fun getLocalizedName(language: Language): String {
        val key = when (this) {
            YES_NO -> StringKeyAnalysis.PRASHNA_CAT_YES_NO
            CAREER -> StringKeyAnalysis.PRASHNA_CAT_CAREER
            MARRIAGE -> StringKeyAnalysis.PRASHNA_CAT_MARRIAGE
            CHILDREN -> StringKeyAnalysis.PRASHNA_CAT_CHILDREN
            HEALTH -> StringKeyAnalysis.PRASHNA_CAT_HEALTH
            WEALTH -> StringKeyAnalysis.PRASHNA_CAT_WEALTH
            PROPERTY -> StringKeyAnalysis.PRASHNA_CAT_PROPERTY
            TRAVEL -> StringKeyAnalysis.PRASHNA_CAT_TRAVEL
            EDUCATION -> StringKeyAnalysis.PRASHNA_CAT_EDUCATION
            LEGAL -> StringKeyAnalysis.PRASHNA_CAT_LEGAL
            LOST_OBJECT -> StringKeyAnalysis.PRASHNA_CAT_LOST_OBJECT
            RELATIONSHIP -> StringKeyAnalysis.PRASHNA_CAT_RELATIONSHIP
            BUSINESS -> StringKeyAnalysis.PRASHNA_CAT_BUSINESS
            SPIRITUAL -> StringKeyAnalysis.PRASHNA_CAT_SPIRITUAL
            GENERAL -> StringKeyAnalysis.PRASHNA_CAT_GENERAL
        }
        return StringResources.get(key, language)
    }

    fun getLocalizedDescription(language: Language): String {
        val key = when (this) {
            YES_NO -> StringKeyAnalysis.PRASHNA_CAT_YES_NO_DESC
            CAREER -> StringKeyAnalysis.PRASHNA_CAT_CAREER_DESC
            MARRIAGE -> StringKeyAnalysis.PRASHNA_CAT_MARRIAGE_DESC
            CHILDREN -> StringKeyAnalysis.PRASHNA_CAT_CHILDREN_DESC
            HEALTH -> StringKeyAnalysis.PRASHNA_CAT_HEALTH_DESC
            WEALTH -> StringKeyAnalysis.PRASHNA_CAT_WEALTH_DESC
            PROPERTY -> StringKeyAnalysis.PRASHNA_CAT_PROPERTY_DESC
            TRAVEL -> StringKeyAnalysis.PRASHNA_CAT_TRAVEL_DESC
            EDUCATION -> StringKeyAnalysis.PRASHNA_CAT_EDUCATION_DESC
            LEGAL -> StringKeyAnalysis.PRASHNA_CAT_LEGAL_DESC
            LOST_OBJECT -> StringKeyAnalysis.PRASHNA_CAT_LOST_OBJECT_DESC
            RELATIONSHIP -> StringKeyAnalysis.PRASHNA_CAT_RELATIONSHIP_DESC
            BUSINESS -> StringKeyAnalysis.PRASHNA_CAT_BUSINESS_DESC
            SPIRITUAL -> StringKeyAnalysis.PRASHNA_CAT_SPIRITUAL_DESC
            GENERAL -> StringKeyAnalysis.PRASHNA_CAT_GENERAL_DESC
        }
        return StringResources.get(key, language)
    }
}

/**
 * Five elements/Tattvas in Prashna
 */
enum class Tattva(val nameKey: StringKeyAnalysis, val signsKey: StringKeyAnalysis) {
    FIRE(StringKeyAnalysis.PRASHNA_TATTVA_FIRE, StringKeyAnalysis.PRASHNA_TATTVA_FIRE_SIGNS),
    EARTH(StringKeyAnalysis.PRASHNA_TATTVA_EARTH, StringKeyAnalysis.PRASHNA_TATTVA_EARTH_SIGNS),
    AIR(StringKeyAnalysis.PRASHNA_TATTVA_AIR, StringKeyAnalysis.PRASHNA_TATTVA_AIR_SIGNS),
    WATER(StringKeyAnalysis.PRASHNA_TATTVA_WATER, StringKeyAnalysis.PRASHNA_TATTVA_WATER_SIGNS),
    ETHER(StringKeyAnalysis.PRASHNA_TATTVA_ETHER, StringKeyAnalysis.PRASHNA_TATTVA_ETHER_SIGNS);

    fun getLocalizedName(language: Language): String = StringResources.get(nameKey, language)
    fun getLocalizedSignIndicator(language: Language): String = StringResources.get(signsKey, language)
}

/**
 * Timing units for predictions
 */
enum class TimingUnit(val displayName: String) {
    HOURS("Hours"),
    DAYS("Days"),
    WEEKS("Weeks"),
    MONTHS("Months"),
    YEARS("Years");

    fun getLocalizedName(language: Language): String {
        val key = when (this) {
            HOURS -> StringKeyAnalysis.PRASHNA_TIMING_HOURS
            DAYS -> StringKeyAnalysis.PRASHNA_TIMING_DAYS
            WEEKS -> StringKeyAnalysis.PRASHNA_TIMING_WEEKS
            MONTHS -> StringKeyAnalysis.PRASHNA_TIMING_MONTHS
            YEARS -> StringKeyAnalysis.PRASHNA_TIMING_YEARS
        }
        return StringResources.get(key, language)
    }
}

/**
 * House signification data class for Prashna
 */
data class PrashnaHouseSignification(
    val house: Int,
    val nameKey: StringKeyAnalysis,
    val primaryTopics: List<StringKeyAnalysis>,
    val secondaryTopics: List<StringKeyAnalysis>,
    val karaka: Planet,
    val bodyPartKey: StringKeyAnalysis,
    val directionKey: StringKeyAnalysis,
    val colorKey: StringKeyAnalysis,
    val element: Tattva
) {
    fun getLocalizedName(language: Language): String = StringResources.get(nameKey, language)
    fun getLocalizedBodyPart(language: Language): String = StringResources.get(bodyPartKey, language)
    fun getLocalizedDirection(language: Language): String = StringResources.get(directionKey, language)
    fun getLocalizedColor(language: Language): String = StringResources.get(colorKey, language)
    fun getLocalizedPrimaryTopics(language: Language): List<String> = primaryTopics.map { StringResources.get(it, language) }
    fun getLocalizedSecondaryTopics(language: Language): List<String> = secondaryTopics.map { StringResources.get(it, language) }
}

/**
 * Complete Prashna Analysis Result
 */
data class PrashnaResult(
    val questionTime: LocalDateTime,
    val question: String,
    val category: PrashnaCategory,
    val chart: VedicChart,
    val judgment: PrashnaJudgment,
    val moonAnalysis: MoonAnalysis,
    val lagnaAnalysis: LagnaAnalysis,
    val houseAnalysis: HouseAnalysis,
    val timingPrediction: TimingPrediction,
    val specialYogas: List<PrashnaYoga>,
    val omens: List<PrashnaOmen>,
    val recommendations: List<String>,
    val detailedInterpretation: String,
    val confidence: Int // 0-100 confidence score
)

/**
 * Main judgment result with Yes/No indication
 */
data class PrashnaJudgment(
    val verdict: PrashnaVerdict,
    val primaryReason: String,
    val supportingFactors: List<String>,
    val opposingFactors: List<String>,
    val overallScore: Int, // -100 to +100
    val certaintyLevel: CertaintyLevel
)

enum class PrashnaVerdict(val displayName: String) {
    STRONGLY_YES("Strongly Yes - Success Indicated"),
    YES("Yes - Favorable Outcome"),
    LIKELY_YES("Likely Yes - Conditions Apply"),
    UNCERTAIN("Uncertain - Mixed Indications"),
    LIKELY_NO("Likely No - Difficulties Indicated"),
    NO("No - Unfavorable Outcome"),
    STRONGLY_NO("Strongly No - Failure Indicated"),
    TIMING_DEPENDENT("Timing Dependent - Wait Indicated");

    fun getLocalizedName(language: Language): String {
        val key = when (this) {
            STRONGLY_YES -> StringKeyAnalysis.PRASHNA_VERDICT_STRONGLY_YES
            YES -> StringKeyAnalysis.PRASHNA_VERDICT_YES
            LIKELY_YES -> StringKeyAnalysis.PRASHNA_VERDICT_LIKELY_YES
            UNCERTAIN -> StringKeyAnalysis.PRASHNA_VERDICT_UNCERTAIN
            LIKELY_NO -> StringKeyAnalysis.PRASHNA_VERDICT_LIKELY_NO
            NO -> StringKeyAnalysis.PRASHNA_VERDICT_NO
            STRONGLY_NO -> StringKeyAnalysis.PRASHNA_VERDICT_STRONGLY_NO
            TIMING_DEPENDENT -> StringKeyAnalysis.PRASHNA_VERDICT_TIMING_DEPENDENT
        }
        return StringResources.get(key, language)
    }
}

enum class CertaintyLevel(val displayName: String, val percentage: IntRange) {
    VERY_HIGH("Very High Certainty", 85..100),
    HIGH("High Certainty", 70..84),
    MODERATE("Moderate Certainty", 50..69),
    LOW("Low Certainty", 30..49),
    VERY_LOW("Very Low Certainty", 0..29);

    fun getLocalizedName(language: Language): String {
        val key = when (this) {
            VERY_HIGH -> StringKeyAnalysis.PRASHNA_CERTAINTY_VERY_HIGH
            HIGH -> StringKeyAnalysis.PRASHNA_CERTAINTY_HIGH
            MODERATE -> StringKeyAnalysis.PRASHNA_CERTAINTY_MODERATE
            LOW -> StringKeyAnalysis.PRASHNA_CERTAINTY_LOW
            VERY_LOW -> StringKeyAnalysis.PRASHNA_CERTAINTY_VERY_LOW
        }
        return StringResources.get(key, language)
    }
}

/**
 * Moon analysis - primary significator in Prashna
 */
data class MoonAnalysis(
    val position: PlanetPosition,
    val nakshatra: Nakshatra,
    val nakshatraPada: Int,
    val nakshatraLord: Planet,
    val moonSign: ZodiacSign,
    val moonHouse: Int,
    val isWaxing: Boolean,
    val tithiNumber: Int,
    val tithiName: String,
    val moonStrength: MoonStrength,
    val isVoidOfCourse: Boolean,
    val lastAspect: PlanetaryAspect?,
    val nextAspect: PlanetaryAspect?,
    val moonAge: Double, // Days since new moon
    val moonSpeed: Double,
    val interpretation: String
)

enum class MoonStrength(val displayName: String, val score: Int) {
    EXCELLENT("Excellent", 5),
    GOOD("Good", 4),
    AVERAGE("Average", 3),
    WEAK("Weak", 2),
    VERY_WEAK("Very Weak", 1),
    AFFLICTED("Afflicted", 0);

    fun getLocalizedName(language: Language): String {
        val key = when (this) {
            EXCELLENT -> StringKeyAnalysis.PRASHNA_MOON_EXCELLENT
            GOOD -> StringKeyAnalysis.PRASHNA_MOON_GOOD
            AVERAGE -> StringKeyAnalysis.PRASHNA_MOON_AVERAGE
            WEAK -> StringKeyAnalysis.PRASHNA_MOON_WEAK
            VERY_WEAK -> StringKeyAnalysis.PRASHNA_MOON_VERY_WEAK
            AFFLICTED -> StringKeyAnalysis.PRASHNA_MOON_AFFLICTED
        }
        return StringResources.get(key, language)
    }
}

/**
 * Lagna (Ascendant) analysis
 */
data class LagnaAnalysis(
    val lagnaSign: ZodiacSign,
    val lagnaDegree: Double,
    val lagnaLord: Planet,
    val lagnaLordPosition: PlanetPosition,
    val lagnaLordStrength: PlanetStrength,
    val lagnaAspects: List<PlanetaryAspect>,
    val planetsInLagna: List<PlanetPosition>,
    val lagnaCondition: LagnaCondition,
    val arudhaLagna: Int,
    val interpretation: String
)

enum class LagnaCondition(val displayName: String) {
    STRONG("Strong - Well placed lord"),
    MODERATE("Moderate - Mixed influences"),
    WEAK("Weak - Afflicted or poorly placed"),
    COMBUST("Combust - Lord too close to Sun"),
    RETROGRADE_LORD("Lord is Retrograde");

    fun getLocalizedName(language: Language): String {
        val key = when (this) {
            STRONG -> StringKeyAnalysis.PRASHNA_LAGNA_STRONG
            MODERATE -> StringKeyAnalysis.PRASHNA_LAGNA_MODERATE
            WEAK -> StringKeyAnalysis.PRASHNA_LAGNA_WEAK
            COMBUST -> StringKeyAnalysis.PRASHNA_LAGNA_COMBUST
            RETROGRADE_LORD -> StringKeyAnalysis.PRASHNA_LAGNA_RETROGRADE_LORD
        }
        return StringResources.get(key, language)
    }
}

data class PlanetStrength(
    val planet: Planet,
    val isExalted: Boolean,
    val isDebilitated: Boolean,
    val isInOwnSign: Boolean,
    val isRetrograde: Boolean,
    val isCombust: Boolean,
    val isVargottama: Boolean,
    val aspectsReceived: List<PlanetaryAspect>,
    val overallStrength: StrengthLevel
)

enum class StrengthLevel(val displayName: String, val value: Int) {
    VERY_STRONG("Very Strong", 5),
    STRONG("Strong", 4),
    MODERATE("Moderate", 3),
    WEAK("Weak", 2),
    VERY_WEAK("Very Weak", 1),
    DEBILITATED("Debilitated", 0);

    fun getLocalizedName(language: Language): String {
        val key = when (this) {
            VERY_STRONG -> StringKeyAnalysis.PRASHNA_STRENGTH_VERY_STRONG
            STRONG -> StringKeyAnalysis.PRASHNA_STRENGTH_STRONG
            MODERATE -> StringKeyAnalysis.PRASHNA_STRENGTH_MODERATE
            WEAK -> StringKeyAnalysis.PRASHNA_STRENGTH_WEAK
            VERY_WEAK -> StringKeyAnalysis.PRASHNA_STRENGTH_VERY_WEAK
            DEBILITATED -> StringKeyAnalysis.PRASHNA_STRENGTH_DEBILITATED
        }
        return StringResources.get(key, language)
    }
}

/**
 * Planetary aspect data
 */
data class PlanetaryAspect(
    val aspectingPlanet: Planet,
    val aspectedPlanet: Planet?,
    val aspectedHouse: Int,
    val aspectType: AspectType,
    val orb: Double,
    val isBenefic: Boolean
)

enum class AspectType(val displayName: String, val angle: Int) {
    CONJUNCTION("Conjunction", 0),
    SEXTILE("Sextile", 60),
    SQUARE("Square", 90),
    TRINE("Trine", 120),
    OPPOSITION("Opposition", 180),
    // Vedic special aspects
    MARS_4TH("Mars 4th Aspect", 90),
    MARS_8TH("Mars 8th Aspect", 210),
    JUPITER_5TH("Jupiter 5th Aspect", 120),
    JUPITER_9TH("Jupiter 9th Aspect", 240),
    SATURN_3RD("Saturn 3rd Aspect", 60),
    SATURN_10TH("Saturn 10th Aspect", 270);

    fun getLocalizedName(language: Language): String {
        val key = when (this) {
            CONJUNCTION -> StringKeyAnalysis.PRASHNA_ASPECT_CONJUNCTION
            SEXTILE -> StringKeyAnalysis.PRASHNA_ASPECT_SEXTILE
            SQUARE -> StringKeyAnalysis.PRASHNA_ASPECT_SQUARE
            TRINE -> StringKeyAnalysis.PRASHNA_ASPECT_TRINE
            OPPOSITION -> StringKeyAnalysis.PRASHNA_ASPECT_OPPOSITION
            MARS_4TH -> StringKeyAnalysis.PRASHNA_ASPECT_MARS_4TH
            MARS_8TH -> StringKeyAnalysis.PRASHNA_ASPECT_MARS_8TH
            JUPITER_5TH -> StringKeyAnalysis.PRASHNA_ASPECT_JUPITER_5TH
            JUPITER_9TH -> StringKeyAnalysis.PRASHNA_ASPECT_JUPITER_9TH
            SATURN_3RD -> StringKeyAnalysis.PRASHNA_ASPECT_SATURN_3RD
            SATURN_10TH -> StringKeyAnalysis.PRASHNA_ASPECT_SATURN_10TH
        }
        return StringResources.get(key, language)
    }
}

/**
 * House analysis for the relevant question category
 */
data class HouseAnalysis(
    val relevantHouses: List<Int>,
    val houseConditions: Map<Int, HouseCondition>,
    val houseLords: Map<Int, PlanetPosition>,
    val planetsInHouses: Map<Int, List<PlanetPosition>>,
    val interpretation: String
)

data class HouseCondition(
    val house: Int,
    val lord: Planet,
    val lordPosition: Int, // House where lord is placed
    val lordStrength: StrengthLevel,
    val planetsPresent: List<Planet>,
    val aspectsToHouse: List<PlanetaryAspect>,
    val condition: HouseStrength
)

enum class HouseStrength(val displayName: String) {
    EXCELLENT("Excellent"),
    GOOD("Good"),
    MODERATE("Moderate"),
    POOR("Poor"),
    AFFLICTED("Afflicted");

    fun getLocalizedName(language: Language): String {
        val key = when (this) {
            EXCELLENT -> StringKeyAnalysis.PRASHNA_HOUSE_EXCELLENT
            GOOD -> StringKeyAnalysis.PRASHNA_HOUSE_GOOD
            MODERATE -> StringKeyAnalysis.PRASHNA_HOUSE_MODERATE
            POOR -> StringKeyAnalysis.PRASHNA_HOUSE_POOR
            AFFLICTED -> StringKeyAnalysis.PRASHNA_HOUSE_AFFLICTED
        }
        return StringResources.get(key, language)
    }
}

/**
 * Timing prediction
 */
data class TimingPrediction(
    val willEventOccur: Boolean,
    val estimatedTime: String,
    val timingMethod: TimingMethod,
    val unit: TimingUnit,
    val value: Double,
    val confidence: Int,
    val explanation: String
)

enum class TimingMethod(val displayName: String) {
    MOON_TRANSIT("Moon Transit Method"),
    MOON_NAKSHATRA("Moon Nakshatra Method"),
    HOUSE_LORD_DEGREES("House Lord Degrees"),
    LAGNA_DEGREES("Lagna Degrees Method"),
    PLANETARY_CONJUNCTION("Planetary Conjunction"),
    DASHA_BASED("Dasha-based Timing"),
    MIXED("Combined Methods");

    fun getLocalizedName(language: Language): String {
        val key = when (this) {
            MOON_TRANSIT -> StringKeyAnalysis.PRASHNA_METHOD_MOON_TRANSIT
            MOON_NAKSHATRA -> StringKeyAnalysis.PRASHNA_METHOD_MOON_NAKSHATRA
            HOUSE_LORD_DEGREES -> StringKeyAnalysis.PRASHNA_METHOD_HOUSE_LORD_DEGREES
            LAGNA_DEGREES -> StringKeyAnalysis.PRASHNA_METHOD_LAGNA_DEGREES
            PLANETARY_CONJUNCTION -> StringKeyAnalysis.PRASHNA_METHOD_PLANETARY_CONJUNCTION
            DASHA_BASED -> StringKeyAnalysis.PRASHNA_METHOD_DASHA_BASED
            MIXED -> StringKeyAnalysis.PRASHNA_METHOD_MIXED
        }
        return StringResources.get(key, language)
    }
}

/**
 * Special Prashna Yogas
 */
data class PrashnaYoga(
    val name: String,
    val description: String,
    val isPositive: Boolean,
    val strength: Int, // 1-5
    val interpretation: String
)

/**
 * Omens and external signs
 */
data class PrashnaOmen(
    val type: OmenType,
    val description: String,
    val indication: String,
    val isPositive: Boolean
)

enum class OmenType(val displayName: String) {
    PRASHNA_LAGNA("Prashna Lagna Sign"),
    MOON_PLACEMENT("Moon Placement"),
    HORA_LORD("Hora Lord"),
    DAY_LORD("Day Lord"),
    NAKSHATRA("Question Nakshatra"),
    PLANETARY_WAR("Planetary War"),
    COMBUSTION("Combustion"),
    RETROGRADE("Retrograde Planet"),
    GANDANTA("Gandanta Position"),
    PUSHKARA("Pushkara Navamsha");

    fun getLocalizedName(language: Language): String {
        val key = when (this) {
            PRASHNA_LAGNA -> StringKeyAnalysis.PRASHNA_OMEN_LAGNA
            MOON_PLACEMENT -> StringKeyAnalysis.PRASHNA_OMEN_MOON_PLACEMENT
            HORA_LORD -> StringKeyAnalysis.PRASHNA_OMEN_HORA_LORD
            DAY_LORD -> StringKeyAnalysis.PRASHNA_OMEN_DAY_LORD
            NAKSHATRA -> StringKeyAnalysis.PRASHNA_OMEN_NAKSHATRA
            PLANETARY_WAR -> StringKeyAnalysis.PRASHNA_OMEN_PLANETARY_WAR
            COMBUSTION -> StringKeyAnalysis.PRASHNA_OMEN_COMBUSTION
            RETROGRADE -> StringKeyAnalysis.PRASHNA_OMEN_RETROGRADE
            GANDANTA -> StringKeyAnalysis.PRASHNA_OMEN_GANDANTA
            PUSHKARA -> StringKeyAnalysis.PRASHNA_PUSHKARA
        }
        return StringResources.get(key, language)
    }
}
