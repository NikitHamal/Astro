package com.astro.storm.ephemeris

import android.content.Context
import com.astro.storm.data.localization.Language
import com.astro.storm.data.localization.StringKeyAnalysis
import com.astro.storm.data.localization.StringResources
import com.astro.storm.data.model.BirthData
import com.astro.storm.data.model.HouseSystem
import com.astro.storm.data.model.Nakshatra
import com.astro.storm.data.model.Planet
import com.astro.storm.data.model.PlanetPosition
import com.astro.storm.data.model.VedicChart
import com.astro.storm.data.model.ZodiacSign
import swisseph.SweConst
import swisseph.SweDate
import swisseph.SwissEph
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZonedDateTime
import kotlin.math.abs
import kotlin.math.floor

/**
 * PrashnaCalculator - Production-grade Vedic Horary Astrology Engine
 *
 * Implements comprehensive Prashna (Horary) astrology according to classical
 * Vedic texts including:
 * - Prashna Marga
 * - Prashna Tantra
 * - Daivagna Vallabha
 * - Tajika Neelakanthi
 *
 * Features:
 * - Instant chart generation for current moment
 * - Prashna-specific house significations
 * - Moon as primary significator analysis
 * - Yes/No indication algorithms based on multiple factors
 * - Timing predictions using Vedic methods
 * - Mook (Dumb) Prashna support
 * - Arudha Lagna calculations
 * - Tattva analysis for element-based predictions
 * - Special Prashna Yogas detection
 */
class PrashnaCalculator(context: Context) {

    private val swissEph = SwissEph()
    private val ephemerisPath: String

    companion object {
        private const val AYANAMSA_LAHIRI = SweConst.SE_SIDM_LAHIRI
        private const val SEFLG_SIDEREAL = SweConst.SEFLG_SIDEREAL
        private const val SEFLG_SPEED = SweConst.SEFLG_SPEED

        // Orb values for aspects (in degrees)
        private const val CONJUNCTION_ORB = 10.0
        private const val TRINE_ORB = 8.0
        private const val SEXTILE_ORB = 6.0
        private const val SQUARE_ORB = 8.0
        private const val OPPOSITION_ORB = 10.0

        // Prashna-specific constants
        private const val DEGREES_PER_SIGN = 30.0
        private const val DEGREES_PER_NAKSHATRA = 360.0 / 27.0

        // Moon movement per day (approximately 13.2 degrees)
        private const val MOON_DAILY_MOTION = 13.2

        // Natural benefics and malefics
        private val NATURAL_BENEFICS = setOf(Planet.JUPITER, Planet.VENUS, Planet.MERCURY, Planet.MOON)
        private val NATURAL_MALEFICS = setOf(Planet.SATURN, Planet.MARS, Planet.RAHU, Planet.KETU, Planet.SUN)

        // Prashna-specific house significations
        private val PRASHNA_HOUSE_SIGNIFICATIONS = mapOf(
            1 to PrashnaHouseSignification(
                house = 1,
                name = "Lagna/Querent",
                primaryTopics = listOf("The querent", "Health", "Self", "Beginning of matter"),
                secondaryTopics = listOf("Physical appearance", "General success", "Life force"),
                karaka = Planet.SUN,
                bodyPart = "Head",
                direction = "East",
                color = "Red",
                element = Tattva.FIRE
            ),
            2 to PrashnaHouseSignification(
                house = 2,
                name = "Dhana",
                primaryTopics = listOf("Wealth", "Family", "Speech", "Resources"),
                secondaryTopics = listOf("Food", "Right eye", "Face", "Possessions"),
                karaka = Planet.JUPITER,
                bodyPart = "Face/Mouth",
                direction = "South-East",
                color = "Orange",
                element = Tattva.EARTH
            ),
            3 to PrashnaHouseSignification(
                house = 3,
                name = "Sahaja",
                primaryTopics = listOf("Courage", "Siblings", "Short journeys", "Communication"),
                secondaryTopics = listOf("Neighbors", "Skills", "Efforts", "Right ear"),
                karaka = Planet.MARS,
                bodyPart = "Arms/Shoulders",
                direction = "West",
                color = "Green",
                element = Tattva.AIR
            ),
            4 to PrashnaHouseSignification(
                house = 4,
                name = "Sukha",
                primaryTopics = listOf("Home", "Mother", "Property", "Vehicles"),
                secondaryTopics = listOf("Education", "Happiness", "Buried treasure", "Heart"),
                karaka = Planet.MOON,
                bodyPart = "Chest",
                direction = "North",
                color = "White",
                element = Tattva.WATER
            ),
            5 to PrashnaHouseSignification(
                house = 5,
                name = "Putra",
                primaryTopics = listOf("Children", "Intelligence", "Romance", "Speculation"),
                secondaryTopics = listOf("Education", "Mantras", "Past merit", "Stomach"),
                karaka = Planet.JUPITER,
                bodyPart = "Upper Abdomen",
                direction = "East-North",
                color = "Yellow",
                element = Tattva.FIRE
            ),
            6 to PrashnaHouseSignification(
                house = 6,
                name = "Ripu",
                primaryTopics = listOf("Enemies", "Disease", "Obstacles", "Service"),
                secondaryTopics = listOf("Debts", "Competitors", "Maternal uncle", "Intestines"),
                karaka = Planet.MARS,
                bodyPart = "Lower Abdomen",
                direction = "South",
                color = "Greyish Blue",
                element = Tattva.EARTH
            ),
            7 to PrashnaHouseSignification(
                house = 7,
                name = "Kalatra",
                primaryTopics = listOf("Marriage", "Partnership", "Business deals", "Opponent"),
                secondaryTopics = listOf("Foreign travel", "Death", "Sexual matters", "Kidneys"),
                karaka = Planet.VENUS,
                bodyPart = "Below Navel",
                direction = "West",
                color = "Multi-colored",
                element = Tattva.AIR
            ),
            8 to PrashnaHouseSignification(
                house = 8,
                name = "Ayu",
                primaryTopics = listOf("Death", "Hidden things", "Inheritance", "Transformation"),
                secondaryTopics = listOf("Chronic illness", "Occult", "Mystery", "Reproductive organs"),
                karaka = Planet.SATURN,
                bodyPart = "Private Parts",
                direction = "South-West",
                color = "Black",
                element = Tattva.WATER
            ),
            9 to PrashnaHouseSignification(
                house = 9,
                name = "Dharma",
                primaryTopics = listOf("Luck", "Father", "Long journeys", "Higher learning"),
                secondaryTopics = listOf("Religion", "Guru", "Fortune", "Thighs"),
                karaka = Planet.JUPITER,
                bodyPart = "Thighs",
                direction = "East",
                color = "Golden",
                element = Tattva.FIRE
            ),
            10 to PrashnaHouseSignification(
                house = 10,
                name = "Karma",
                primaryTopics = listOf("Career", "Status", "Authority", "Government"),
                secondaryTopics = listOf("Father", "Public life", "Achievement", "Knees"),
                karaka = Planet.SUN,
                bodyPart = "Knees",
                direction = "South",
                color = "White",
                element = Tattva.EARTH
            ),
            11 to PrashnaHouseSignification(
                house = 11,
                name = "Labha",
                primaryTopics = listOf("Gains", "Fulfillment", "Elder siblings", "Friends"),
                secondaryTopics = listOf("Income", "Wishes", "Recovery from illness", "Ankles"),
                karaka = Planet.JUPITER,
                bodyPart = "Calves/Ankles",
                direction = "North-West",
                color = "Golden Yellow",
                element = Tattva.AIR
            ),
            12 to PrashnaHouseSignification(
                house = 12,
                name = "Vyaya",
                primaryTopics = listOf("Losses", "Expenses", "Liberation", "Foreign lands"),
                secondaryTopics = listOf("Bed pleasures", "Sleep", "Hospital", "Feet"),
                karaka = Planet.SATURN,
                bodyPart = "Feet",
                direction = "North-East",
                color = "Brown",
                element = Tattva.WATER
            )
        )

        // Question categories with relevant houses
        private val QUESTION_CATEGORIES = mapOf(
            PrashnaCategory.YES_NO to listOf(1, 7),
            PrashnaCategory.CAREER to listOf(10, 6, 2),
            PrashnaCategory.MARRIAGE to listOf(7, 2, 11),
            PrashnaCategory.CHILDREN to listOf(5, 9, 11),
            PrashnaCategory.HEALTH to listOf(1, 6, 8),
            PrashnaCategory.WEALTH to listOf(2, 11, 5),
            PrashnaCategory.PROPERTY to listOf(4, 11, 2),
            PrashnaCategory.TRAVEL to listOf(3, 9, 12),
            PrashnaCategory.EDUCATION to listOf(4, 5, 9),
            PrashnaCategory.LEGAL to listOf(6, 7, 9),
            PrashnaCategory.LOST_OBJECT to listOf(2, 4, 7, 12),
            PrashnaCategory.RELATIONSHIP to listOf(7, 5, 11),
            PrashnaCategory.BUSINESS to listOf(7, 10, 11),
            PrashnaCategory.SPIRITUAL to listOf(9, 12, 5),
            PrashnaCategory.GENERAL to listOf(1, 7, 10)
        )

        // Ashtakavarga points threshold for favorable results
        private const val FAVORABLE_ASHTAKAVARGA_THRESHOLD = 4

        // Timing units based on planetary strength
        private val TIMING_UNITS = mapOf(
            Planet.SUN to TimingUnit.MONTHS,
            Planet.MOON to TimingUnit.DAYS,
            Planet.MARS to TimingUnit.WEEKS,
            Planet.MERCURY to TimingUnit.DAYS,
            Planet.JUPITER to TimingUnit.MONTHS,
            Planet.VENUS to TimingUnit.WEEKS,
            Planet.SATURN to TimingUnit.MONTHS,
            Planet.RAHU to TimingUnit.MONTHS,
            Planet.KETU to TimingUnit.MONTHS
        )
    }

    init {
        ephemerisPath = context.filesDir.absolutePath + "/ephe"
        swissEph.swe_set_ephe_path(ephemerisPath)
        swissEph.swe_set_sid_mode(AYANAMSA_LAHIRI, 0.0, 0.0)
    }

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
    enum class Tattva(val displayName: String, val signIndicator: String) {
        FIRE("Agni/Fire", "Aries, Leo, Sagittarius"),
        EARTH("Prithvi/Earth", "Taurus, Virgo, Capricorn"),
        AIR("Vayu/Air", "Gemini, Libra, Aquarius"),
        WATER("Jala/Water", "Cancer, Scorpio, Pisces"),
        ETHER("Akasha/Ether", "None - represents void/space");

        fun getLocalizedName(language: Language): String {
            val key = when (this) {
                FIRE -> StringKeyAnalysis.PRASHNA_TATTVA_FIRE
                EARTH -> StringKeyAnalysis.PRASHNA_TATTVA_EARTH
                AIR -> StringKeyAnalysis.PRASHNA_TATTVA_AIR
                WATER -> StringKeyAnalysis.PRASHNA_TATTVA_WATER
                ETHER -> StringKeyAnalysis.PRASHNA_TATTVA_ETHER
            }
            return StringResources.get(key, language)
        }

        fun getLocalizedSignIndicator(language: Language): String {
            val key = when (this) {
                FIRE -> StringKeyAnalysis.PRASHNA_TATTVA_FIRE_SIGNS
                EARTH -> StringKeyAnalysis.PRASHNA_TATTVA_EARTH_SIGNS
                AIR -> StringKeyAnalysis.PRASHNA_TATTVA_AIR_SIGNS
                WATER -> StringKeyAnalysis.PRASHNA_TATTVA_WATER_SIGNS
                ETHER -> StringKeyAnalysis.PRASHNA_TATTVA_ETHER_SIGNS
            }
            return StringResources.get(key, language)
        }
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
        val name: String,
        val primaryTopics: List<String>,
        val secondaryTopics: List<String>,
        val karaka: Planet,
        val bodyPart: String,
        val direction: String,
        val color: String,
        val element: Tattva
    )

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
                PUSHKARA -> StringKeyAnalysis.PRASHNA_OMEN_PUSHKARA
            }
            return StringResources.get(key, language)
        }
    }

    /**
     * Generate instant Prashna chart for current moment
     */
    fun generatePrashnaChart(
        question: String,
        category: PrashnaCategory,
        latitude: Double,
        longitude: Double,
        timezone: String,
        language: Language = Language.ENGLISH
    ): PrashnaResult {
        val questionTime = LocalDateTime.now()
        return analyzePrashna(question, category, questionTime, latitude, longitude, timezone, language)
    }

    /**
     * Analyze Prashna for a specific time
     */
    fun analyzePrashna(
        question: String,
        category: PrashnaCategory,
        questionTime: LocalDateTime,
        latitude: Double,
        longitude: Double,
        timezone: String,
        language: Language = Language.ENGLISH
    ): PrashnaResult {
        // Create BirthData for the question moment
        val prashnaData = BirthData(
            name = "Prashna Chart",
            dateTime = questionTime,
            latitude = latitude,
            longitude = longitude,
            timezone = timezone,
            location = "Question Location"
        )

        // Calculate the chart using Swiss Ephemeris directly
        val chart = calculatePrashnaChart(prashnaData)

        // Perform comprehensive analysis
        val moonAnalysis = analyzeMoon(chart, questionTime, latitude, longitude, timezone)
        val lagnaAnalysis = analyzeLagna(chart)
        val houseAnalysis = analyzeHouses(chart, category)
        val specialYogas = detectPrashnaYogas(chart, moonAnalysis, lagnaAnalysis)
        val omens = detectOmens(chart, questionTime, moonAnalysis)

        // Calculate main judgment
        val judgment = calculateJudgment(
            chart, category, moonAnalysis, lagnaAnalysis, houseAnalysis, specialYogas, omens, language
        )

        // Calculate timing
        val timingPrediction = calculateTiming(
            chart, category, moonAnalysis, lagnaAnalysis, houseAnalysis, language
        )

        // Generate recommendations
        val recommendations = generateRecommendations(
            judgment, moonAnalysis, lagnaAnalysis, houseAnalysis, specialYogas, language
        )

        // Generate detailed interpretation
        val interpretation = generateDetailedInterpretation(
            question, category, judgment, moonAnalysis, lagnaAnalysis,
            houseAnalysis, timingPrediction, specialYogas, language
        )

        // Calculate confidence score
        val confidence = calculateConfidence(
            judgment, moonAnalysis, lagnaAnalysis, specialYogas
        )

        return PrashnaResult(
            questionTime = questionTime,
            question = question,
            category = category,
            chart = chart,
            judgment = judgment,
            moonAnalysis = moonAnalysis,
            lagnaAnalysis = lagnaAnalysis,
            houseAnalysis = houseAnalysis,
            timingPrediction = timingPrediction,
            specialYogas = specialYogas,
            omens = omens,
            recommendations = recommendations,
            detailedInterpretation = interpretation,
            confidence = confidence
        )
    }

    /**
     * Calculate Prashna chart using Swiss Ephemeris
     */
    private fun calculatePrashnaChart(birthData: BirthData): VedicChart {
        val zoneId = ZoneId.of(birthData.timezone)
        val zonedDateTime = ZonedDateTime.of(birthData.dateTime, zoneId)
        val utcDateTime = zonedDateTime.withZoneSameInstant(ZoneId.of("UTC")).toLocalDateTime()
        val julianDay = calculateJulianDay(utcDateTime)

        val ayanamsa = swissEph.swe_get_ayanamsa_ut(julianDay)

        // Calculate houses
        val houseCusps = DoubleArray(13)
        val ascMc = DoubleArray(10)

        swissEph.swe_houses(
            julianDay,
            SEFLG_SIDEREAL,
            birthData.latitude,
            birthData.longitude,
            'P'.code, // Placidus
            houseCusps,
            ascMc
        )

        val ascendant = ascMc[0]
        val midheaven = ascMc[1]
        val houseCuspsList = (1..12).map { houseCusps[it] }

        // Calculate planet positions
        val planetPositions = Planet.ALL_PLANETS.map { planet ->
            calculatePlanetPosition(planet, julianDay, houseCuspsList)
        }

        return VedicChart(
            birthData = birthData,
            julianDay = julianDay,
            ayanamsa = ayanamsa,
            ayanamsaName = "Lahiri",
            ascendant = ascendant,
            midheaven = midheaven,
            planetPositions = planetPositions,
            houseCusps = houseCuspsList,
            houseSystem = HouseSystem.PLACIDUS
        )
    }

    /**
     * Calculate position for a single planet
     */
    private fun calculatePlanetPosition(
        planet: Planet,
        julianDay: Double,
        houseCusps: List<Double>
    ): PlanetPosition {
        val xx = DoubleArray(6)
        val serr = StringBuffer()

        val sweId = if (planet == Planet.KETU) Planet.RAHU.swissEphId else planet.swissEphId

        swissEph.swe_calc_ut(
            julianDay,
            sweId,
            SEFLG_SIDEREAL or SEFLG_SPEED,
            xx,
            serr
        )

        var longitude = xx[0]
        var speed = xx[3]

        if (planet == Planet.KETU) {
            longitude = normalizeDegrees(longitude + 180.0)
            speed = -speed
        }

        longitude = normalizeDegrees(longitude)

        val sign = ZodiacSign.fromLongitude(longitude)
        val degreeInSign = longitude % DEGREES_PER_SIGN
        val wholeDegrees = degreeInSign.toInt()
        val fractionalDegrees = degreeInSign - wholeDegrees
        val totalMinutes = fractionalDegrees * 60
        val wholeMinutes = totalMinutes.toInt()
        val seconds = (totalMinutes - wholeMinutes) * 60

        val (nakshatra, pada) = Nakshatra.fromLongitude(longitude)
        val house = determineHouse(longitude, houseCusps)

        return PlanetPosition(
            planet = planet,
            longitude = longitude,
            latitude = xx[1],
            distance = xx[2],
            speed = speed,
            sign = sign,
            degree = wholeDegrees.toDouble(),
            minutes = wholeMinutes.toDouble(),
            seconds = seconds,
            isRetrograde = speed < 0,
            nakshatra = nakshatra,
            nakshatraPada = pada,
            house = house
        )
    }

    /**
     * Comprehensive Moon analysis - the primary significator in Prashna
     */
    private fun analyzeMoon(
        chart: VedicChart,
        questionTime: LocalDateTime,
        latitude: Double,
        longitude: Double,
        timezone: String
    ): MoonAnalysis {
        val moonPosition = chart.planetPositions.first { it.planet == Planet.MOON }
        val sunPosition = chart.planetPositions.first { it.planet == Planet.SUN }

        // Calculate tithi
        val moonSunDiff = normalizeDegrees(moonPosition.longitude - sunPosition.longitude)
        val tithiNumber = (floor(moonSunDiff / 12.0).toInt() % 30) + 1
        val tithiName = getTithiName(tithiNumber)

        // Determine if moon is waxing or waning
        val isWaxing = moonSunDiff < 180.0

        // Calculate moon age (days since new moon)
        val moonAge = moonSunDiff / MOON_DAILY_MOTION

        // Determine moon strength
        val moonStrength = calculateMoonStrength(moonPosition, sunPosition, chart)

        // Check if void of course
        val (isVoid, lastAspect, nextAspect) = checkVoidOfCourse(moonPosition, chart)

        // Generate interpretation
        val interpretation = generateMoonInterpretation(
            moonPosition, moonStrength, isWaxing, isVoid, tithiNumber, tithiName
        )

        return MoonAnalysis(
            position = moonPosition,
            nakshatra = moonPosition.nakshatra,
            nakshatraPada = moonPosition.nakshatraPada,
            nakshatraLord = moonPosition.nakshatra.ruler,
            moonSign = moonPosition.sign,
            moonHouse = moonPosition.house,
            isWaxing = isWaxing,
            tithiNumber = tithiNumber,
            tithiName = tithiName,
            moonStrength = moonStrength,
            isVoidOfCourse = isVoid,
            lastAspect = lastAspect,
            nextAspect = nextAspect,
            moonAge = moonAge,
            moonSpeed = moonPosition.speed,
            interpretation = interpretation
        )
    }

    /**
     * Calculate Moon strength for Prashna purposes
     */
    private fun calculateMoonStrength(
        moonPosition: PlanetPosition,
        sunPosition: PlanetPosition,
        chart: VedicChart
    ): MoonStrength {
        var score = 3 // Start with average

        // Paksha Bala (waxing/waning)
        val moonSunDiff = normalizeDegrees(moonPosition.longitude - sunPosition.longitude)
        if (moonSunDiff > 90 && moonSunDiff < 270) score += 1 // Bright half bonus

        // Shukla Paksha (waxing) is stronger
        if (moonSunDiff < 180) score += 1

        // Check sign placement
        when (moonPosition.sign) {
            ZodiacSign.TAURUS -> score += 2 // Exalted
            ZodiacSign.CANCER -> score += 1 // Own sign
            ZodiacSign.SCORPIO -> score -= 2 // Debilitated
            else -> {}
        }

        // Check for malefic aspects
        val maleficAspects = chart.planetPositions
            .filter { it.planet in NATURAL_MALEFICS }
            .count { isAspecting(it, moonPosition) }
        score -= maleficAspects

        // Check for benefic aspects
        val beneficAspects = chart.planetPositions
            .filter { it.planet in NATURAL_BENEFICS && it.planet != Planet.MOON }
            .count { isAspecting(it, moonPosition) }
        score += beneficAspects / 2

        // Check combustion (within 12 degrees of Sun)
        val sunMoonDistance = angularDistance(moonPosition.longitude, sunPosition.longitude)
        if (sunMoonDistance < 12) score -= 2

        return when {
            score >= 5 -> MoonStrength.EXCELLENT
            score >= 4 -> MoonStrength.GOOD
            score >= 3 -> MoonStrength.AVERAGE
            score >= 2 -> MoonStrength.WEAK
            score >= 1 -> MoonStrength.VERY_WEAK
            else -> MoonStrength.AFFLICTED
        }
    }

    /**
     * Check if Moon is void of course
     */
    private fun checkVoidOfCourse(
        moonPosition: PlanetPosition,
        chart: VedicChart
    ): Triple<Boolean, PlanetaryAspect?, PlanetaryAspect?> {
        val currentMoonDegree = moonPosition.longitude
        val moonSignEnd = ((moonPosition.sign.number) * 30.0)

        // Check aspects Moon will make before leaving current sign
        var lastAspect: PlanetaryAspect? = null
        var nextAspect: PlanetaryAspect? = null
        var willMakeAspect = false

        for (planet in chart.planetPositions) {
            if (planet.planet == Planet.MOON) continue

            val aspectDegrees = listOf(0.0, 60.0, 90.0, 120.0, 180.0)
            for (aspectAngle in aspectDegrees) {
                val targetDegree = normalizeDegrees(planet.longitude + aspectAngle)
                val reverseTarget = normalizeDegrees(planet.longitude - aspectAngle)

                // Check if Moon will reach this aspect before leaving sign
                if (targetDegree > currentMoonDegree && targetDegree < moonSignEnd) {
                    willMakeAspect = true
                    if (nextAspect == null || targetDegree < normalizeDegrees(nextAspect.aspectedPlanet?.let {
                            chart.planetPositions.first { p -> p.planet == it }.longitude
                        } ?: 360.0)) {
                        nextAspect = PlanetaryAspect(
                            aspectingPlanet = Planet.MOON,
                            aspectedPlanet = planet.planet,
                            aspectedHouse = planet.house,
                            aspectType = getAspectType(aspectAngle),
                            orb = abs(targetDegree - currentMoonDegree),
                            isBenefic = planet.planet in NATURAL_BENEFICS
                        )
                    }
                }

                // Find last aspect made
                if (targetDegree < currentMoonDegree) {
                    lastAspect = PlanetaryAspect(
                        aspectingPlanet = Planet.MOON,
                        aspectedPlanet = planet.planet,
                        aspectedHouse = planet.house,
                        aspectType = getAspectType(aspectAngle),
                        orb = abs(currentMoonDegree - targetDegree),
                        isBenefic = planet.planet in NATURAL_BENEFICS
                    )
                }
            }
        }

        return Triple(!willMakeAspect, lastAspect, nextAspect)
    }

    /**
     * Analyze Lagna (Ascendant)
     */
    private fun analyzeLagna(chart: VedicChart): LagnaAnalysis {
        val lagnaSign = ZodiacSign.fromLongitude(chart.ascendant)
        val lagnaLord = lagnaSign.ruler
        val lagnaLordPosition = chart.planetPositions.first { it.planet == lagnaLord }

        // Calculate Lagna Lord strength
        val lagnaLordStrength = calculatePlanetStrength(lagnaLordPosition, chart)

        // Find aspects to Lagna
        val lagnaAspects = chart.planetPositions.mapNotNull { planet ->
            if (isAspectingHouse(planet, 1, chart)) {
                PlanetaryAspect(
                    aspectingPlanet = planet.planet,
                    aspectedPlanet = null,
                    aspectedHouse = 1,
                    aspectType = AspectType.CONJUNCTION,
                    orb = 0.0,
                    isBenefic = planet.planet in NATURAL_BENEFICS
                )
            } else null
        }

        // Find planets in Lagna
        val planetsInLagna = chart.planetPositions.filter { it.house == 1 }

        // Determine Lagna condition
        val lagnaCondition = when {
            lagnaLordStrength.isCombust -> LagnaCondition.COMBUST
            lagnaLordStrength.isRetrograde -> LagnaCondition.RETROGRADE_LORD
            lagnaLordStrength.overallStrength.value >= 4 -> LagnaCondition.STRONG
            lagnaLordStrength.overallStrength.value >= 2 -> LagnaCondition.MODERATE
            else -> LagnaCondition.WEAK
        }

        // Calculate Arudha Lagna
        val arudhaLagna = calculateArudhaLagna(lagnaLord, lagnaLordPosition, chart)

        // Generate interpretation
        val interpretation = generateLagnaInterpretation(
            lagnaSign, lagnaLordPosition, lagnaCondition, planetsInLagna
        )

        return LagnaAnalysis(
            lagnaSign = lagnaSign,
            lagnaDegree = chart.ascendant,
            lagnaLord = lagnaLord,
            lagnaLordPosition = lagnaLordPosition,
            lagnaLordStrength = lagnaLordStrength,
            lagnaAspects = lagnaAspects,
            planetsInLagna = planetsInLagna,
            lagnaCondition = lagnaCondition,
            arudhaLagna = arudhaLagna,
            interpretation = interpretation
        )
    }

    /**
     * Calculate planetary strength
     */
    private fun calculatePlanetStrength(
        position: PlanetPosition,
        chart: VedicChart
    ): PlanetStrength {
        val planet = position.planet
        val sign = position.sign

        // Exaltation signs
        val exaltationSigns = mapOf(
            Planet.SUN to ZodiacSign.ARIES,
            Planet.MOON to ZodiacSign.TAURUS,
            Planet.MARS to ZodiacSign.CAPRICORN,
            Planet.MERCURY to ZodiacSign.VIRGO,
            Planet.JUPITER to ZodiacSign.CANCER,
            Planet.VENUS to ZodiacSign.PISCES,
            Planet.SATURN to ZodiacSign.LIBRA
        )

        // Debilitation signs
        val debilitationSigns = mapOf(
            Planet.SUN to ZodiacSign.LIBRA,
            Planet.MOON to ZodiacSign.SCORPIO,
            Planet.MARS to ZodiacSign.CANCER,
            Planet.MERCURY to ZodiacSign.PISCES,
            Planet.JUPITER to ZodiacSign.CAPRICORN,
            Planet.VENUS to ZodiacSign.VIRGO,
            Planet.SATURN to ZodiacSign.ARIES
        )

        val isExalted = exaltationSigns[planet] == sign
        val isDebilitated = debilitationSigns[planet] == sign
        val isInOwnSign = sign.ruler == planet
        val isRetrograde = position.isRetrograde

        // Check combustion
        val sunPosition = chart.planetPositions.first { it.planet == Planet.SUN }
        val distanceFromSun = angularDistance(position.longitude, sunPosition.longitude)
        val combustionOrb = when (planet) {
            Planet.MOON -> 12.0
            Planet.MARS -> 17.0
            Planet.MERCURY -> 14.0
            Planet.JUPITER -> 11.0
            Planet.VENUS -> 10.0
            Planet.SATURN -> 15.0
            else -> 0.0
        }
        val isCombust = planet != Planet.SUN && distanceFromSun < combustionOrb

        // Check Vargottama (same sign in D1 and D9)
        val navamshaSign = calculateNavamshaSign(position.longitude)
        val isVargottama = sign == navamshaSign

        // Calculate aspects received
        val aspectsReceived = chart.planetPositions
            .filter { it.planet != planet }
            .mapNotNull { aspectingPlanet ->
                if (isAspecting(aspectingPlanet, position)) {
                    PlanetaryAspect(
                        aspectingPlanet = aspectingPlanet.planet,
                        aspectedPlanet = planet,
                        aspectedHouse = position.house,
                        aspectType = AspectType.CONJUNCTION,
                        orb = angularDistance(aspectingPlanet.longitude, position.longitude),
                        isBenefic = aspectingPlanet.planet in NATURAL_BENEFICS
                    )
                } else null
            }

        // Calculate overall strength
        var strengthScore = 3
        if (isExalted) strengthScore += 2
        if (isDebilitated) strengthScore -= 2
        if (isInOwnSign) strengthScore += 1
        if (isVargottama) strengthScore += 1
        if (isCombust) strengthScore -= 1
        if (isRetrograde && planet !in listOf(Planet.SUN, Planet.MOON)) strengthScore -= 1

        val overallStrength = when {
            strengthScore >= 5 -> StrengthLevel.VERY_STRONG
            strengthScore >= 4 -> StrengthLevel.STRONG
            strengthScore >= 3 -> StrengthLevel.MODERATE
            strengthScore >= 2 -> StrengthLevel.WEAK
            strengthScore >= 1 -> StrengthLevel.VERY_WEAK
            else -> StrengthLevel.DEBILITATED
        }

        return PlanetStrength(
            planet = planet,
            isExalted = isExalted,
            isDebilitated = isDebilitated,
            isInOwnSign = isInOwnSign,
            isRetrograde = isRetrograde,
            isCombust = isCombust,
            isVargottama = isVargottama,
            aspectsReceived = aspectsReceived,
            overallStrength = overallStrength
        )
    }

    /**
     * Analyze houses relevant to the question category
     */
    private fun analyzeHouses(
        chart: VedicChart,
        category: PrashnaCategory
    ): HouseAnalysis {
        val relevantHouses = QUESTION_CATEGORIES[category] ?: listOf(1, 7)

        val houseConditions = mutableMapOf<Int, HouseCondition>()
        val houseLords = mutableMapOf<Int, PlanetPosition>()
        val planetsInHouses = mutableMapOf<Int, List<PlanetPosition>>()

        for (house in 1..12) {
            val houseSign = ZodiacSign.fromLongitude(chart.houseCusps[house - 1])
            val houseLord = houseSign.ruler
            val lordPosition = chart.planetPositions.first { it.planet == houseLord }

            houseLords[house] = lordPosition
            planetsInHouses[house] = chart.planetPositions.filter { it.house == house }

            val lordStrength = calculatePlanetStrength(lordPosition, chart).overallStrength

            // Calculate aspects to house
            val aspectsToHouse = chart.planetPositions.mapNotNull { planet ->
                if (isAspectingHouse(planet, house, chart)) {
                    PlanetaryAspect(
                        aspectingPlanet = planet.planet,
                        aspectedPlanet = null,
                        aspectedHouse = house,
                        aspectType = AspectType.CONJUNCTION,
                        orb = 0.0,
                        isBenefic = planet.planet in NATURAL_BENEFICS
                    )
                } else null
            }

            // Determine house condition
            val beneficAspects = aspectsToHouse.count { it.isBenefic }
            val maleficAspects = aspectsToHouse.count { !it.isBenefic }
            val planetsPresent = planetsInHouses[house]?.map { it.planet } ?: emptyList()

            val condition = when {
                lordStrength.value >= 4 && beneficAspects > maleficAspects -> HouseStrength.EXCELLENT
                lordStrength.value >= 3 && beneficAspects >= maleficAspects -> HouseStrength.GOOD
                lordStrength.value >= 2 -> HouseStrength.MODERATE
                maleficAspects > beneficAspects -> HouseStrength.AFFLICTED
                else -> HouseStrength.POOR
            }

            houseConditions[house] = HouseCondition(
                house = house,
                lord = houseLord,
                lordPosition = lordPosition.house,
                lordStrength = lordStrength,
                planetsPresent = planetsPresent,
                aspectsToHouse = aspectsToHouse,
                condition = condition
            )
        }

        val interpretation = generateHouseInterpretation(relevantHouses, houseConditions, category)

        return HouseAnalysis(
            relevantHouses = relevantHouses,
            houseConditions = houseConditions,
            houseLords = houseLords,
            planetsInHouses = planetsInHouses,
            interpretation = interpretation
        )
    }

    /**
     * Detect special Prashna Yogas
     */
    private fun detectPrashnaYogas(
        chart: VedicChart,
        moonAnalysis: MoonAnalysis,
        lagnaAnalysis: LagnaAnalysis,
        language: Language = Language.ENGLISH
    ): List<PrashnaYoga> {
        val yogas = mutableListOf<PrashnaYoga>()

        // 1. Ithasala Yoga - applying aspect between significators
        if (isIthasalaPresent(chart, moonAnalysis)) {
            yogas.add(
                PrashnaYoga(
                    name = StringResources.get(StringKeyAnalysis.PRASHNA_YOGA_ITHASALA_NAME, language),
                    description = StringResources.get(StringKeyAnalysis.PRASHNA_YOGA_ITHASALA_DESC, language),
                    isPositive = true,
                    strength = 4,
                    interpretation = StringResources.get(StringKeyAnalysis.PRASHNA_YOGA_ITHASALA_INTERP, language)
                )
            )
        }

        // 2. Musaripha Yoga - separating aspect (negative)
        if (isMusariphaPresent(chart, moonAnalysis)) {
            yogas.add(
                PrashnaYoga(
                    name = StringResources.get(StringKeyAnalysis.PRASHNA_YOGA_MUSARIPHA_NAME, language),
                    description = StringResources.get(StringKeyAnalysis.PRASHNA_YOGA_MUSARIPHA_DESC, language),
                    isPositive = false,
                    strength = 3,
                    interpretation = StringResources.get(StringKeyAnalysis.PRASHNA_YOGA_MUSARIPHA_INTERP, language)
                )
            )
        }

        // 3. Nakta Yoga - transfer of light
        // 3. Nakta Yoga - transfer of light
        if (isNaktaPresent(chart)) {
            yogas.add(
                PrashnaYoga(
                    name = StringResources.get(StringKeyAnalysis.PRASHNA_YOGA_NAKTA_NAME, language),
                    description = StringResources.get(StringKeyAnalysis.PRASHNA_YOGA_NAKTA_DESC, language),
                    isPositive = true,
                    strength = 3,
                    interpretation = StringResources.get(StringKeyAnalysis.PRASHNA_YOGA_NAKTA_INTERP, language)
                )
            )
        }

        // 4. Manaou Yoga - prohibition
        if (isManaouPresent(chart)) {
            yogas.add(
                PrashnaYoga(
                    name = StringResources.get(StringKeyAnalysis.PRASHNA_YOGA_MANAOU_NAME, language),
                    description = StringResources.get(StringKeyAnalysis.PRASHNA_YOGA_MANAOU_DESC, language),
                    isPositive = false,
                    strength = 4,
                    interpretation = StringResources.get(StringKeyAnalysis.PRASHNA_YOGA_MANAOU_INTERP, language)
                )
            )
        }

        // 5. Kamboola Yoga - Moon in angular house with lord
        if (isKamboolaPresent(chart, moonAnalysis)) {
            yogas.add(
                PrashnaYoga(
                    name = StringResources.get(StringKeyAnalysis.PRASHNA_YOGA_KAMBOOLA_NAME, language),
                    description = StringResources.get(StringKeyAnalysis.PRASHNA_YOGA_KAMBOOLA_DESC, language),
                    isPositive = true,
                    strength = 4,
                    interpretation = StringResources.get(StringKeyAnalysis.PRASHNA_YOGA_KAMBOOLA_INTERP, language)
                )
            )
        }

        // 6. Gairi Kamboola - Moon weak in angular house
        if (isGairiKamboolaPresent(chart, moonAnalysis)) {
            yogas.add(
                PrashnaYoga(
                    name = StringResources.get(StringKeyAnalysis.PRASHNA_YOGA_GAIRI_KAMBOOLA_NAME, language),
                    description = StringResources.get(StringKeyAnalysis.PRASHNA_YOGA_GAIRI_KAMBOOLA_DESC, language),
                    isPositive = false,
                    strength = 2,
                    interpretation = StringResources.get(StringKeyAnalysis.PRASHNA_YOGA_GAIRI_KAMBOOLA_INTERP, language)
                )
            )
        }

        // 7. Dhurufa Yoga - Moon cadent and weak
        if (isDhurufaPresent(chart, moonAnalysis)) {
            yogas.add(
                PrashnaYoga(
                    name = StringResources.get(StringKeyAnalysis.PRASHNA_YOGA_DHURUFA_NAME, language),
                    description = StringResources.get(StringKeyAnalysis.PRASHNA_YOGA_DHURUFA_DESC, language),
                    isPositive = false,
                    strength = 4,
                    interpretation = StringResources.get(StringKeyAnalysis.PRASHNA_YOGA_DHURUFA_INTERP, language)
                )
            )
        }

        // 8. Check Pushkara Navamsha
        if (isPushkaraNavamsha(moonAnalysis.position.longitude)) {
            yogas.add(
                PrashnaYoga(
                    name = StringResources.get(StringKeyAnalysis.PRASHNA_YOGA_PUSHKARA_NAME, language),
                    description = StringResources.get(StringKeyAnalysis.PRASHNA_YOGA_PUSHKARA_DESC, language),
                    isPositive = true,
                    strength = 5,
                    interpretation = StringResources.get(StringKeyAnalysis.PRASHNA_YOGA_PUSHKARA_INTERP, language)
                )
            )
        }

        // 9. Check Gandanta
        if (isGandanta(moonAnalysis.position.longitude)) {
            yogas.add(
                PrashnaYoga(
                    name = StringResources.get(StringKeyAnalysis.PRASHNA_YOGA_GANDANTA_NAME, language),
                    description = StringResources.get(StringKeyAnalysis.PRASHNA_YOGA_GANDANTA_DESC, language),
                    isPositive = false,
                    strength = 5,
                    interpretation = StringResources.get(StringKeyAnalysis.PRASHNA_YOGA_GANDANTA_INTERP, language)
                )
            )
        }

        // 10. Lagna Lord and Moon conjunction
        if (moonAnalysis.moonHouse == lagnaAnalysis.lagnaLordPosition.house ||
            angularDistance(moonAnalysis.position.longitude, lagnaAnalysis.lagnaLordPosition.longitude) < 10) {
            yogas.add(
                PrashnaYoga(
                    name = StringResources.get(StringKeyAnalysis.PRASHNA_YOGA_UNION_NAME, language),
                    description = StringResources.get(StringKeyAnalysis.PRASHNA_YOGA_UNION_DESC, language),
                    isPositive = true,
                    strength = 4,
                    interpretation = StringResources.get(StringKeyAnalysis.PRASHNA_YOGA_UNION_INTERP, language)
                )
            )
        }

        return yogas
    }

    /**
     * Detect omens and external signs
     */
    private fun detectOmens(
        chart: VedicChart,
        questionTime: LocalDateTime,
        moonAnalysis: MoonAnalysis
    ): List<PrashnaOmen> {
        val omens = mutableListOf<PrashnaOmen>()

        // 1. Prashna Lagna sign omen
        val lagnaSign = ZodiacSign.fromLongitude(chart.ascendant)
        omens.add(
            PrashnaOmen(
                type = OmenType.PRASHNA_LAGNA,
                description = "Question asked when ${lagnaSign.displayName} is rising",
                indication = getLagnaSignIndication(lagnaSign),
                isPositive = lagnaSign.element in listOf("Fire", "Air")
            )
        )

        // 2. Moon placement omen
        val moonHouseSignification = PRASHNA_HOUSE_SIGNIFICATIONS[moonAnalysis.moonHouse]
        omens.add(
            PrashnaOmen(
                type = OmenType.MOON_PLACEMENT,
                description = "Moon in ${moonAnalysis.moonHouse} house (${moonHouseSignification?.name})",
                indication = getMoonHouseIndication(moonAnalysis.moonHouse),
                isPositive = moonAnalysis.moonHouse in listOf(1, 4, 5, 7, 9, 10, 11)
            )
        )

        // 3. Hora Lord omen
        val horaLord = calculateHoraLord(questionTime)
        omens.add(
            PrashnaOmen(
                type = OmenType.HORA_LORD,
                description = "Question in ${horaLord.displayName} Hora",
                indication = getHoraLordIndication(horaLord),
                isPositive = horaLord in NATURAL_BENEFICS
            )
        )

        // 4. Day Lord omen
        val dayLord = getDayLord(questionTime)
        omens.add(
            PrashnaOmen(
                type = OmenType.DAY_LORD,
                description = "Question on ${dayLord.displayName}'s day",
                indication = getDayLordIndication(dayLord),
                isPositive = dayLord in listOf(Planet.JUPITER, Planet.VENUS, Planet.MERCURY)
            )
        )

        // 5. Nakshatra omen
        omens.add(
            PrashnaOmen(
                type = OmenType.NAKSHATRA,
                description = "Moon in ${moonAnalysis.nakshatra.displayName} Nakshatra",
                indication = getNakshatraIndication(moonAnalysis.nakshatra),
                isPositive = isAuspiciousNakshatra(moonAnalysis.nakshatra)
            )
        )

        // 6. Check for planetary war
        val planetaryWars = detectPlanetaryWars(chart)
        for (war in planetaryWars) {
            omens.add(
                PrashnaOmen(
                    type = OmenType.PLANETARY_WAR,
                    description = "${war.first.displayName} in war with ${war.second.displayName}",
                    indication = "Conflict and competition indicated in the matter",
                    isPositive = false
                )
            )
        }

        // 7. Check for combustion
        val combustPlanets = chart.planetPositions.filter { position ->
            val sunPosition = chart.planetPositions.first { it.planet == Planet.SUN }
            position.planet != Planet.SUN &&
                    angularDistance(position.longitude, sunPosition.longitude) < getCombustionOrb(position.planet)
        }
        for (planet in combustPlanets) {
            omens.add(
                PrashnaOmen(
                    type = OmenType.COMBUSTION,
                    description = "${planet.planet.displayName} is combust",
                    indication = "${planet.planet.displayName}'s significations are weakened",
                    isPositive = false
                )
            )
        }

        // 8. Check for retrograde planets
        val retrogradePlanets = chart.planetPositions.filter {
            it.isRetrograde && it.planet !in listOf(Planet.SUN, Planet.MOON, Planet.RAHU, Planet.KETU)
        }
        for (planet in retrogradePlanets) {
            omens.add(
                PrashnaOmen(
                    type = OmenType.RETROGRADE,
                    description = "${planet.planet.displayName} is retrograde",
                    indication = "Delays, reversals, or need to revisit matters related to ${planet.planet.displayName}",
                    isPositive = false
                )
            )
        }

        return omens
    }

    /**
     * Calculate main judgment based on all factors
     */
    private fun calculateJudgment(
        chart: VedicChart,
        category: PrashnaCategory,
        moonAnalysis: MoonAnalysis,
        lagnaAnalysis: LagnaAnalysis,
        houseAnalysis: HouseAnalysis,
        specialYogas: List<PrashnaYoga>,
        omens: List<PrashnaOmen>,
        language: Language = Language.ENGLISH
    ): PrashnaJudgment {
        var score = 0
        val supportingFactors = mutableListOf<String>()
        val opposingFactors = mutableListOf<String>()

        // 1. Moon strength (most important in Prashna) - weight: 25
        when (moonAnalysis.moonStrength) {
            MoonStrength.EXCELLENT -> {
                score += 25
                supportingFactors.add(StringResources.get(StringKeyAnalysis.PRASHNA_FACTOR_MOON_EXCELLENT, language))
            }
            MoonStrength.GOOD -> {
                score += 18
                supportingFactors.add(StringResources.get(StringKeyAnalysis.PRASHNA_FACTOR_MOON_GOOD, language))
            }
            MoonStrength.AVERAGE -> {
                score += 8
                supportingFactors.add(StringResources.get(StringKeyAnalysis.PRASHNA_FACTOR_MOON_AVERAGE, language))
            }
            MoonStrength.WEAK -> {
                score -= 10
                opposingFactors.add(StringResources.get(StringKeyAnalysis.PRASHNA_FACTOR_MOON_WEAK, language))
            }
            MoonStrength.VERY_WEAK -> {
                score -= 18
                opposingFactors.add(StringResources.get(StringKeyAnalysis.PRASHNA_FACTOR_MOON_VERY_WEAK, language))
            }
            MoonStrength.AFFLICTED -> {
                score -= 25
                opposingFactors.add(StringResources.get(StringKeyAnalysis.PRASHNA_FACTOR_MOON_AFFLICTED, language))
            }
        }

        // 2. Moon waxing/waning - weight: 10
        if (moonAnalysis.isWaxing) {
            score += 10
            supportingFactors.add(StringResources.get(StringKeyAnalysis.PRASHNA_FACTOR_MOON_WAXING, language))
        } else {
            score -= 5
            opposingFactors.add(StringResources.get(StringKeyAnalysis.PRASHNA_FACTOR_MOON_WANING, language))
        }

        // 3. Void of Course Moon - weight: 15
        if (moonAnalysis.isVoidOfCourse) {
            score -= 15
            opposingFactors.add(StringResources.get(StringKeyAnalysis.PRASHNA_FACTOR_MOON_VOID, language))
        }

        // 4. Lagna strength - weight: 20
        when (lagnaAnalysis.lagnaCondition) {
            LagnaCondition.STRONG -> {
                score += 20
                supportingFactors.add(StringResources.get(StringKeyAnalysis.PRASHNA_FACTOR_LAGNA_STRONG, language))
            }
            LagnaCondition.MODERATE -> {
                score += 10
                supportingFactors.add(StringResources.get(StringKeyAnalysis.PRASHNA_FACTOR_LAGNA_MODERATE, language))
            }
            LagnaCondition.WEAK -> {
                score -= 10
                opposingFactors.add(StringResources.get(StringKeyAnalysis.PRASHNA_FACTOR_LAGNA_WEAK, language))
            }
            LagnaCondition.COMBUST -> {
                score -= 15
                opposingFactors.add(StringResources.get(StringKeyAnalysis.PRASHNA_FACTOR_LAGNA_COMBUST, language))
            }
            LagnaCondition.RETROGRADE_LORD -> {
                score -= 5
                opposingFactors.add(StringResources.get(StringKeyAnalysis.PRASHNA_FACTOR_LAGNA_RETRO, language))
            }
        }

        // 5. Relevant house conditions - weight: 15
        val relevantHouseScore = houseAnalysis.relevantHouses.sumOf { house ->
            when (houseAnalysis.houseConditions[house]?.condition) {
                HouseStrength.EXCELLENT -> 5
                HouseStrength.GOOD -> 3
                HouseStrength.MODERATE -> 1
                HouseStrength.POOR -> -2
                HouseStrength.AFFLICTED -> -4
                null -> 0
            } as Int
        }
        score += (relevantHouseScore * 15) / (houseAnalysis.relevantHouses.size * 5)
        if (relevantHouseScore > 0) {
            supportingFactors.add(StringResources.get(StringKeyAnalysis.PRASHNA_FACTOR_HOUSES_FAVORABLE, language))
        } else if (relevantHouseScore < 0) {
            opposingFactors.add(StringResources.get(StringKeyAnalysis.PRASHNA_FACTOR_HOUSES_AFFLICTED, language))
        }

        // 6. Special Yogas - weight: varies
        for (yoga in specialYogas) {
            if (yoga.isPositive) {
                score += yoga.strength * 4
                supportingFactors.add("${yoga.name}: ${yoga.interpretation}")
            } else {
                score -= yoga.strength * 4
                opposingFactors.add("${yoga.name}: ${yoga.interpretation}")
            }
        }

        // 7. Omens - weight: 5 each
        val positiveOmens = omens.count { it.isPositive }
        val negativeOmens = omens.count { !it.isPositive }
        score += (positiveOmens - negativeOmens) * 3

        // Normalize score to -100 to +100
        score = score.coerceIn(-100, 100)

        // Determine verdict
        val verdict = when {
            score >= 70 -> PrashnaVerdict.STRONGLY_YES
            score >= 45 -> PrashnaVerdict.YES
            score >= 20 -> PrashnaVerdict.LIKELY_YES
            score >= -20 -> PrashnaVerdict.UNCERTAIN
            score >= -45 -> PrashnaVerdict.LIKELY_NO
            score >= -70 -> PrashnaVerdict.NO
            else -> PrashnaVerdict.STRONGLY_NO
        }

        // Check for timing-dependent verdict
        val finalVerdict = if (moonAnalysis.isVoidOfCourse && score > -20) {
            PrashnaVerdict.TIMING_DEPENDENT
        } else {
            verdict
        }

        // Determine primary reason
        val primaryReason = when {
            score >= 50 -> "Strong Moon and favorable planetary configurations indicate success"
            score >= 20 -> "Generally favorable indications with some conditions"
            score >= -20 -> "Mixed indications - outcome depends on additional factors and timing"
            score >= -50 -> "Challenges indicated - careful consideration advised"
            else -> "Multiple unfavorable factors suggest difficulty in achieving desired outcome"
        }

        // Calculate certainty
        val certaintyValue = abs(score)
        val certaintyLevel = when {
            certaintyValue >= 70 -> CertaintyLevel.VERY_HIGH
            certaintyValue >= 50 -> CertaintyLevel.HIGH
            certaintyValue >= 30 -> CertaintyLevel.MODERATE
            certaintyValue >= 15 -> CertaintyLevel.LOW
            else -> CertaintyLevel.VERY_LOW
        }

        return PrashnaJudgment(
            verdict = finalVerdict,
            primaryReason = primaryReason,
            supportingFactors = supportingFactors,
            opposingFactors = opposingFactors,
            overallScore = score,
            certaintyLevel = certaintyLevel
        )
    }

    /**
     * Calculate timing prediction
     */
    private fun calculateTiming(
        chart: VedicChart,
        category: PrashnaCategory,
        moonAnalysis: MoonAnalysis,
        lagnaAnalysis: LagnaAnalysis,
        houseAnalysis: HouseAnalysis,
        language: Language = Language.ENGLISH
    ): TimingPrediction {
        // Use multiple timing methods and combine

        // Method 1: Moon transit through relevant houses
        val relevantHouse = houseAnalysis.relevantHouses.firstOrNull() ?: 7
        val moonToRelevantHouse = calculateMoonTransitTime(moonAnalysis, relevantHouse, chart)

        // Method 2: Lagna degrees method
        val lagnaDegreesTiming = calculateLagnaDegreesTiming(lagnaAnalysis)

        // Method 3: Significator's remaining degrees in sign
        val significatorTiming = calculateSignificatorTiming(chart, category)

        // Combine methods with weights
        val primaryMethod: TimingMethod
        val timingValue: Double
        val timingUnit: TimingUnit

        when {
            moonAnalysis.moonStrength.score >= 3 -> {
                // Use Moon method primarily
                primaryMethod = TimingMethod.MOON_TRANSIT
                timingValue = moonToRelevantHouse.first
                timingUnit = moonToRelevantHouse.second
            }
            lagnaAnalysis.lagnaCondition == LagnaCondition.STRONG -> {
                // Use Lagna degrees method
                primaryMethod = TimingMethod.LAGNA_DEGREES
                timingValue = lagnaDegreesTiming.first
                timingUnit = lagnaDegreesTiming.second
            }
            else -> {
                // Use mixed method
                primaryMethod = TimingMethod.MIXED
                timingValue = significatorTiming.first
                timingUnit = significatorTiming.second
            }
        }

        val willOccur = moonAnalysis.moonStrength.score >= 2 &&
                       lagnaAnalysis.lagnaCondition != LagnaCondition.WEAK

        val estimatedTime = formatTimingEstimate(timingValue, timingUnit, language)
        val confidence = calculateTimingConfidence(moonAnalysis, lagnaAnalysis)

        val explanation = buildTimingExplanation(primaryMethod, timingValue, timingUnit, moonAnalysis, language)

        return TimingPrediction(
            willEventOccur = willOccur,
            estimatedTime = estimatedTime,
            timingMethod = primaryMethod,
            unit = timingUnit,
            value = timingValue,
            confidence = confidence,
            explanation = explanation
        )
    }

    private fun generateRecommendations(
        judgment: PrashnaJudgment,
        moonAnalysis: MoonAnalysis,
        lagnaAnalysis: LagnaAnalysis,
        houseAnalysis: HouseAnalysis,
        specialYogas: List<PrashnaYoga>,
        language: Language = Language.ENGLISH
    ): List<String> {
        val recommendations = mutableListOf<String>()

        // Based on verdict
        when (judgment.verdict) {
            PrashnaVerdict.STRONGLY_YES, PrashnaVerdict.YES -> {
                recommendations.add(StringResources.get(StringKeyAnalysis.PRASHNA_REC_STRONGLY_SUPPORT, language))
            }
            PrashnaVerdict.LIKELY_YES -> {
                recommendations.add(StringResources.get(StringKeyAnalysis.PRASHNA_REC_AWARENESS, language))
            }
            PrashnaVerdict.UNCERTAIN -> {
                recommendations.add(StringResources.get(StringKeyAnalysis.PRASHNA_REC_PATIENCE, language))
                recommendations.add(StringResources.get(StringKeyAnalysis.PRASHNA_REC_GUIDANCE, language))
            }
            PrashnaVerdict.TIMING_DEPENDENT -> {
                recommendations.add(StringResources.get(StringKeyAnalysis.PRASHNA_REC_VOID_MOON, language))
                recommendations.add(StringResources.get(StringKeyAnalysis.PRASHNA_REC_VOID_MOON_REASK, language))
            }
            PrashnaVerdict.LIKELY_NO, PrashnaVerdict.NO -> {
                recommendations.add(StringResources.get(StringKeyAnalysis.PRASHNA_REC_RECONSIDER, language))
                recommendations.add(StringResources.get(StringKeyAnalysis.PRASHNA_REC_EXPLORE, language))
            }
            PrashnaVerdict.STRONGLY_NO -> {
                recommendations.add(StringResources.get(StringKeyAnalysis.PRASHNA_REC_ABANDON, language))
                recommendations.add(StringResources.get(StringKeyAnalysis.PRASHNA_REC_FOCUS_FAVORABLE, language))
            }
        }

        // Based on Moon condition
        if (!moonAnalysis.isWaxing) {
            recommendations.add(StringResources.get(StringKeyAnalysis.PRASHNA_REC_WANING_MOON, language))
        }
        if (moonAnalysis.moonStrength in listOf(MoonStrength.WEAK, MoonStrength.VERY_WEAK, MoonStrength.AFFLICTED)) {
            recommendations.add(StringResources.get(StringKeyAnalysis.PRASHNA_REC_STRENGTHEN_MOON, language))
        }

        // Based on Lagna
        if (lagnaAnalysis.lagnaCondition == LagnaCondition.COMBUST) {
            recommendations.add(StringResources.get(StringKeyAnalysis.PRASHNA_REC_AVOID_CONFRONTATION, language))
        }
        if (lagnaAnalysis.lagnaCondition == LagnaCondition.RETROGRADE_LORD) {
            recommendations.add(StringResources.get(StringKeyAnalysis.PRASHNA_REC_REVIEW_PAST, language))
        }

        // Based on special yogas
        val positiveYogas = specialYogas.filter { it.isPositive }
        if (positiveYogas.any { it.name == "Ithasala Yoga" }) {
            recommendations.add(StringResources.get(StringKeyAnalysis.PRASHNA_REC_ACT_PROMPTLY, language))
        }
        if (specialYogas.any { it.name == "Nakta Yoga" }) {
            recommendations.add(StringResources.get(StringKeyAnalysis.PRASHNA_REC_SEEK_ASSISTANCE, language))
        }

        // Remedial measures based on weak houses
        val weakHouses = houseAnalysis.houseConditions.filter {
            it.value.condition in listOf(HouseStrength.POOR, HouseStrength.AFFLICTED)
        }
        for ((house, condition) in weakHouses) {
            val karaka = PRASHNA_HOUSE_SIGNIFICATIONS[house]?.karaka
            if (karaka != null) {
                recommendations.add(StringResources.get(StringKeyAnalysis.PRASHNA_REC_PROPITIATE, language, karaka.getLocalizedName(language), house))
            }
        }

        return recommendations.take(7) // Limit to 7 most relevant recommendations
    }

    private fun generateDetailedInterpretation(
        question: String,
        category: PrashnaCategory,
        judgment: PrashnaJudgment,
        moonAnalysis: MoonAnalysis,
        lagnaAnalysis: LagnaAnalysis,
        houseAnalysis: HouseAnalysis,
        timingPrediction: TimingPrediction,
        specialYogas: List<PrashnaYoga>,
        language: Language = Language.ENGLISH
    ): String {
        return buildString {
            appendLine(StringResources.get(StringKeyAnalysis.PRASHNA_REPORT_TITLE, language))
            appendLine("=" .repeat(50))
            appendLine()

            appendLine("${StringResources.get(StringKeyAnalysis.PRASHNA_REPORT_QUESTION, language)}: $question")
            appendLine("${StringResources.get(StringKeyAnalysis.PRASHNA_REPORT_CATEGORY, language)}: ${category.getLocalizedName(language)}")
            appendLine()

            appendLine("${StringResources.get(StringKeyAnalysis.PRASHNA_REPORT_VERDICT, language)}: ${judgment.verdict.getLocalizedName(language)}")
            appendLine("${StringResources.get(StringKeyAnalysis.PRASHNA_REPORT_CERTAINTY, language)}: ${judgment.certaintyLevel.getLocalizedName(language)}")
            appendLine()

            appendLine("${StringResources.get(StringKeyAnalysis.PRASHNA_REPORT_PRIMARY_INDICATION, language)}:")
            appendLine(judgment.primaryReason)
            appendLine()

            appendLine("${StringResources.get(StringKeyAnalysis.PRASHNA_REPORT_MOON_ANALYSIS, language)}:")
            appendLine("- ${StringResources.get(StringKeyAnalysis.PRASHNA_REPORT_POSITION, language)}: ${moonAnalysis.moonSign.getLocalizedName(language)} in ${StringResources.get(StringKeyAnalysis.PRASHNA_REPORT_HOUSE, language)} ${moonAnalysis.moonHouse}")
            appendLine("- ${StringResources.get(StringKeyAnalysis.PRASHNA_REPORT_NAKSHATRA, language)}: ${moonAnalysis.nakshatra.getLocalizedName(language)} (${StringResources.get(StringKeyAnalysis.PRASHNA_REPORT_PADA, language)} ${moonAnalysis.nakshatraPada})")
            appendLine("- ${StringResources.get(StringKeyAnalysis.PRASHNA_REPORT_PHASE, language)}: ${if (moonAnalysis.isWaxing) StringResources.get(StringKeyAnalysis.PRASHNA_REPORT_WAXING, language) else StringResources.get(StringKeyAnalysis.PRASHNA_REPORT_WANING, language)} - ${moonAnalysis.tithiName}")
            appendLine("- ${StringResources.get(StringKeyAnalysis.PRASHNA_REPORT_STRENGTH, language)}: ${moonAnalysis.moonStrength.getLocalizedName(language)}")
            if (moonAnalysis.isVoidOfCourse) {
                appendLine("- ${StringResources.get(StringKeyAnalysis.PRASHNA_REPORT_WARNING_VOID, language)}")
            }
            appendLine()

            appendLine("${StringResources.get(StringKeyAnalysis.PRASHNA_REPORT_LAGNA_ANALYSIS, language)}:")
            appendLine("- ${StringResources.get(StringKeyAnalysis.PRASHNA_REPORT_RISING_SIGN, language)}: ${lagnaAnalysis.lagnaSign.getLocalizedName(language)}")
            appendLine("- ${StringResources.get(StringKeyAnalysis.PRASHNA_REPORT_LAGNA_LORD, language)}: ${lagnaAnalysis.lagnaLord.getLocalizedName(language)} in ${StringResources.get(StringKeyAnalysis.PRASHNA_REPORT_HOUSE, language)} ${lagnaAnalysis.lagnaLordPosition.house}")
            appendLine("- ${StringResources.get(StringKeyAnalysis.PRASHNA_REPORT_CONDITION, language)}: ${lagnaAnalysis.lagnaCondition.getLocalizedName(language)}")
            appendLine()

            appendLine("${StringResources.get(StringKeyAnalysis.PRASHNA_REPORT_RELEVANT_HOUSES, language)} (${houseAnalysis.relevantHouses.joinToString()}):")
            for (house in houseAnalysis.relevantHouses) {
                val condition = houseAnalysis.houseConditions[house]
                appendLine("- ${StringResources.get(StringKeyAnalysis.PRASHNA_REPORT_HOUSE, language)} $house: ${condition?.condition?.getLocalizedName(language)} (${StringResources.get(StringKeyAnalysis.PRASHNA_REPORT_LAGNA_LORD, language)} in ${StringResources.get(StringKeyAnalysis.PRASHNA_REPORT_HOUSE, language)} ${condition?.lordPosition})")
            }
            appendLine()

            if (specialYogas.isNotEmpty()) {
                appendLine("SPECIAL YOGAS PRESENT:")
                for (yoga in specialYogas) {
                    val symbol = if (yoga.isPositive) "+" else "-"
                    appendLine("$symbol ${yoga.name}: ${yoga.description}")
                }
                appendLine()
            }

            if (timingPrediction.willEventOccur) {
                appendLine("TIMING PREDICTION:")
                appendLine("Estimated: ${timingPrediction.estimatedTime}")
                appendLine("Method: ${timingPrediction.timingMethod.displayName}")
                appendLine("Confidence: ${timingPrediction.confidence}%")
                appendLine()
            }

            appendLine("SUPPORTING FACTORS:")
            for (factor in judgment.supportingFactors.take(5)) {
                appendLine("+ $factor")
            }
            appendLine()

            if (judgment.opposingFactors.isNotEmpty()) {
                appendLine("CHALLENGES:")
                for (factor in judgment.opposingFactors.take(5)) {
                    appendLine("- $factor")
                }
            }
        }
    }

    /**
     * Calculate confidence score
     */
    private fun calculateConfidence(
        judgment: PrashnaJudgment,
        moonAnalysis: MoonAnalysis,
        lagnaAnalysis: LagnaAnalysis,
        specialYogas: List<PrashnaYoga>
    ): Int {
        var confidence = 50

        // Score clarity affects confidence
        confidence += abs(judgment.overallScore) / 3

        // Strong Moon increases confidence
        confidence += moonAnalysis.moonStrength.score * 4

        // Void of course decreases confidence
        if (moonAnalysis.isVoidOfCourse) confidence -= 15

        // Strong Lagna increases confidence
        if (lagnaAnalysis.lagnaCondition == LagnaCondition.STRONG) confidence += 10

        // Clear yogas increase confidence
        val strongYogas = specialYogas.count { it.strength >= 4 }
        confidence += strongYogas * 5

        // Conflicting yogas decrease confidence
        val positiveYogas = specialYogas.count { it.isPositive }
        val negativeYogas = specialYogas.count { !it.isPositive }
        if (positiveYogas > 0 && negativeYogas > 0) {
            confidence -= 10
        }

        return confidence.coerceIn(10, 95)
    }

    // ============ HELPER METHODS ============

    private fun calculateJulianDay(dateTime: LocalDateTime): Double {
        val decimalHours = dateTime.hour +
                (dateTime.minute / 60.0) +
                (dateTime.second / 3600.0) +
                (dateTime.nano / 3600000000000.0)
        val sweDate = SweDate(
            dateTime.year,
            dateTime.monthValue,
            dateTime.dayOfMonth,
            decimalHours,
            SweDate.SE_GREG_CAL
        )
        return sweDate.julDay
    }

    /**
     * Normalize degrees using centralized utility.
     */
    private fun normalizeDegrees(degrees: Double): Double = VedicAstrologyUtils.normalizeDegree(degrees)

    /**
     * Calculate angular distance using centralized utility.
     */
    private fun angularDistance(deg1: Double, deg2: Double): Double =
        VedicAstrologyUtils.angularDistance(deg1, deg2)

    private fun determineHouse(longitude: Double, houseCusps: List<Double>): Int {
        for (houseNum in 1..12) {
            val cuspStart = houseCusps[houseNum - 1]
            val cuspEnd = if (houseNum == 12) houseCusps[0] else houseCusps[houseNum]

            val normalizedLongitude = normalizeDegrees(longitude - cuspStart)
            val houseWidth = normalizeDegrees(cuspEnd - cuspStart)

            val effectiveWidth = if (houseWidth < 0.001) DEGREES_PER_SIGN else houseWidth

            if (normalizedLongitude < effectiveWidth) {
                return houseNum
            }
        }
        return 1
    }

    private fun getTithiName(tithiNumber: Int): String {
        val tithiNames = listOf(
            "Pratipada", "Dwitiya", "Tritiya", "Chaturthi", "Panchami",
            "Shashthi", "Saptami", "Ashtami", "Navami", "Dashami",
            "Ekadashi", "Dwadashi", "Trayodashi", "Chaturdashi"
        )

        return when {
            tithiNumber == 15 -> "Purnima"
            tithiNumber == 30 -> "Amavasya"
            tithiNumber <= 15 -> "Shukla ${tithiNames.getOrElse(tithiNumber - 1) { "" }}"
            else -> "Krishna ${tithiNames.getOrElse(tithiNumber - 16) { "" }}"
        }
    }

    private fun isAspecting(fromPlanet: PlanetPosition, toPlanet: PlanetPosition): Boolean {
        val distance = angularDistance(fromPlanet.longitude, toPlanet.longitude)

        // Standard aspects
        if (distance < CONJUNCTION_ORB) return true
        if (abs(distance - 180) < OPPOSITION_ORB) return true
        if (abs(distance - 120) < TRINE_ORB) return true
        if (abs(distance - 90) < SQUARE_ORB) return true
        if (abs(distance - 60) < SEXTILE_ORB) return true

        // Special Vedic aspects
        when (fromPlanet.planet) {
            Planet.MARS -> {
                // Mars aspects 4th and 8th from itself
                val houseDiff = ((toPlanet.house - fromPlanet.house + 12) % 12)
                if (houseDiff == 3 || houseDiff == 7) return true
            }
            Planet.JUPITER -> {
                // Jupiter aspects 5th and 9th from itself
                val houseDiff = ((toPlanet.house - fromPlanet.house + 12) % 12)
                if (houseDiff == 4 || houseDiff == 8) return true
            }
            Planet.SATURN -> {
                // Saturn aspects 3rd and 10th from itself
                val houseDiff = ((toPlanet.house - fromPlanet.house + 12) % 12)
                if (houseDiff == 2 || houseDiff == 9) return true
            }
            else -> {}
        }

        return false
    }

    private fun isAspectingHouse(planet: PlanetPosition, targetHouse: Int, chart: VedicChart): Boolean {
        val houseDiff = ((targetHouse - planet.house + 12) % 12)

        // 7th aspect (all planets)
        if (houseDiff == 6) return true

        // Special aspects
        when (planet.planet) {
            Planet.MARS -> if (houseDiff == 3 || houseDiff == 7) return true
            Planet.JUPITER -> if (houseDiff == 4 || houseDiff == 8) return true
            Planet.SATURN -> if (houseDiff == 2 || houseDiff == 9) return true
            else -> {}
        }

        // Conjunction (same house)
        if (planet.house == targetHouse) return true

        return false
    }

    private fun getAspectType(angle: Double): AspectType {
        return when {
            abs(angle) < 5 -> AspectType.CONJUNCTION
            abs(angle - 60) < 5 -> AspectType.SEXTILE
            abs(angle - 90) < 5 -> AspectType.SQUARE
            abs(angle - 120) < 5 -> AspectType.TRINE
            abs(angle - 180) < 5 -> AspectType.OPPOSITION
            else -> AspectType.CONJUNCTION
        }
    }

    private fun calculateNavamshaSign(longitude: Double): ZodiacSign {
        val normalizedLong = normalizeDegrees(longitude)
        val navamshaIndex = ((normalizedLong / (30.0 / 9.0)).toInt()) % 12
        return ZodiacSign.entries[navamshaIndex]
    }

    private fun isPushkaraNavamsha(longitude: Double): Boolean {
        val navamshaInSign = ((longitude % 30) / (30.0 / 9.0)).toInt() + 1
        val sign = ZodiacSign.fromLongitude(longitude)

        // Pushkara Navamshas vary by sign
        val pushkaraNavamshas = when (sign) {
            ZodiacSign.ARIES, ZodiacSign.LEO, ZodiacSign.SAGITTARIUS -> listOf(7, 9)
            ZodiacSign.TAURUS, ZodiacSign.VIRGO, ZodiacSign.CAPRICORN -> listOf(3, 5)
            ZodiacSign.GEMINI, ZodiacSign.LIBRA, ZodiacSign.AQUARIUS -> listOf(6, 8)
            ZodiacSign.CANCER, ZodiacSign.SCORPIO, ZodiacSign.PISCES -> listOf(1, 3)
        }

        return navamshaInSign in pushkaraNavamshas
    }

    private fun isGandanta(longitude: Double): Boolean {
        val normalizedLong = normalizeDegrees(longitude)
        val degreeInSign = normalizedLong % 30

        // Gandanta points: last 3°20' of water signs, first 3°20' of fire signs
        val waterSignEnds = listOf(120.0, 240.0, 360.0) // Cancer, Scorpio, Pisces ends
        val fireSignStarts = listOf(0.0, 120.0, 240.0) // Aries, Leo, Sagittarius starts

        val gandantaOrb = 3.333 // 3°20'

        for (waterEnd in waterSignEnds) {
            if (abs(normalizedLong - waterEnd) < gandantaOrb ||
                abs(normalizedLong - (waterEnd - 360)) < gandantaOrb) {
                return true
            }
        }

        for (fireStart in fireSignStarts) {
            if (normalizedLong >= fireStart && normalizedLong < fireStart + gandantaOrb) {
                return true
            }
        }

        return false
    }

    private fun calculateArudhaLagna(
        lagnaLord: Planet,
        lordPosition: PlanetPosition,
        chart: VedicChart
    ): Int {
        val lagnaHouse = 1
        val lordHouse = lordPosition.house

        // Arudha = Lord's house counted same distance from Lord's house
        val distance = lordHouse - lagnaHouse
        var arudha = lordHouse + distance

        // Normalize to 1-12
        arudha = ((arudha - 1) % 12) + 1
        if (arudha <= 0) arudha += 12

        // Arudha cannot be in 1st or 7th from Lagna
        if (arudha == 1) arudha = 10
        if (arudha == 7) arudha = 4

        return arudha
    }

    private fun calculateHoraLord(dateTime: LocalDateTime): Planet {
        val dayOfWeek = dateTime.dayOfWeek.value % 7
        val hour = dateTime.hour

        val dayLords = listOf(
            Planet.SUN, Planet.MOON, Planet.MARS, Planet.MERCURY,
            Planet.JUPITER, Planet.VENUS, Planet.SATURN
        )

        val chaldeanOrder = listOf(
            Planet.SATURN, Planet.JUPITER, Planet.MARS, Planet.SUN,
            Planet.VENUS, Planet.MERCURY, Planet.MOON
        )

        val dayLordIndex = dayOfWeek
        val startingLordIndex = chaldeanOrder.indexOf(dayLords[dayLordIndex])
        val horaLordIndex = (startingLordIndex + hour) % 7

        return chaldeanOrder[horaLordIndex]
    }

    private fun getDayLord(dateTime: LocalDateTime): Planet {
        val dayOfWeek = dateTime.dayOfWeek.value % 7
        return listOf(
            Planet.MOON, Planet.MARS, Planet.MERCURY, Planet.JUPITER,
            Planet.VENUS, Planet.SATURN, Planet.SUN
        )[dayOfWeek]
    }

    private fun detectPlanetaryWars(chart: VedicChart): List<Pair<Planet, Planet>> {
        val wars = mutableListOf<Pair<Planet, Planet>>()
        val warOrb = 1.0 // Within 1 degree

        val warringPlanets = listOf(
            Planet.MARS, Planet.MERCURY, Planet.JUPITER, Planet.VENUS, Planet.SATURN
        )

        for (i in warringPlanets.indices) {
            for (j in i + 1 until warringPlanets.size) {
                val planet1 = chart.planetPositions.first { it.planet == warringPlanets[i] }
                val planet2 = chart.planetPositions.first { it.planet == warringPlanets[j] }

                if (angularDistance(planet1.longitude, planet2.longitude) < warOrb) {
                    wars.add(Pair(warringPlanets[i], warringPlanets[j]))
                }
            }
        }

        return wars
    }

    private fun getCombustionOrb(planet: Planet): Double {
        return when (planet) {
            Planet.MOON -> 12.0
            Planet.MARS -> 17.0
            Planet.MERCURY -> 14.0
            Planet.JUPITER -> 11.0
            Planet.VENUS -> 10.0
            Planet.SATURN -> 15.0
            else -> 0.0
        }
    }

    private fun isAuspiciousNakshatra(nakshatra: Nakshatra): Boolean {
        val auspiciousNakshatras = listOf(
            Nakshatra.ASHWINI, Nakshatra.ROHINI, Nakshatra.MRIGASHIRA,
            Nakshatra.PUNARVASU, Nakshatra.PUSHYA, Nakshatra.UTTARA_PHALGUNI,
            Nakshatra.HASTA, Nakshatra.CHITRA, Nakshatra.SWATI,
            Nakshatra.ANURADHA, Nakshatra.SHRAVANA, Nakshatra.DHANISHTHA,
            Nakshatra.UTTARA_BHADRAPADA, Nakshatra.REVATI
        )
        return nakshatra in auspiciousNakshatras
    }

    // Yoga detection methods
    private fun isIthasalaPresent(chart: VedicChart, moonAnalysis: MoonAnalysis): Boolean {
        // Ithasala: Moon applying to aspect with significator
        val moonSpeed = moonAnalysis.position.speed
        if (moonSpeed <= 0) return false // Moon must be direct and applying

        // Check for applying aspects
        return moonAnalysis.nextAspect != null && moonAnalysis.nextAspect.isBenefic
    }

    private fun isMusariphaPresent(chart: VedicChart, moonAnalysis: MoonAnalysis): Boolean {
        // Musaripha: Moon separating from aspect
        return moonAnalysis.lastAspect != null && !moonAnalysis.isVoidOfCourse
    }

    private fun isNaktaPresent(chart: VedicChart): Boolean {
        // Nakta: Transfer of light - simplified check
        val moon = chart.planetPositions.first { it.planet == Planet.MOON }
        val benefics = chart.planetPositions.filter { it.planet in NATURAL_BENEFICS && it.planet != Planet.MOON }

        // Check if Moon is separating from one benefic and applying to another
        var separating = false
        var applying = false

        for (benefic in benefics) {
            val distance = normalizeDegrees(benefic.longitude - moon.longitude)
            if (distance < 10 && distance > 0) applying = true
            if (distance > 350 || (distance < 0 && distance > -10)) separating = true
        }

        return separating && applying
    }

    private fun isManaouPresent(chart: VedicChart): Boolean {
        // Manaou: Prohibition by malefic
        val moon = chart.planetPositions.first { it.planet == Planet.MOON }
        val lagnaLord = ZodiacSign.fromLongitude(chart.ascendant).ruler
        val lagnaLordPos = chart.planetPositions.first { it.planet == lagnaLord }

        // Check if malefic is between Moon and Lagna Lord
        val moonLong = moon.longitude
        val lordLong = lagnaLordPos.longitude

        for (malefic in chart.planetPositions.filter { it.planet in NATURAL_MALEFICS }) {
            val maleficLong = malefic.longitude
            if ((maleficLong > moonLong && maleficLong < lordLong) ||
                (maleficLong < moonLong && maleficLong > lordLong)) {
                return true
            }
        }

        return false
    }

    private fun isKamboolaPresent(chart: VedicChart, moonAnalysis: MoonAnalysis): Boolean {
        // Kamboola: Moon in angle with strong lord
        val angularHouses = listOf(1, 4, 7, 10)
        return moonAnalysis.moonHouse in angularHouses &&
               moonAnalysis.moonStrength.score >= 3
    }

    private fun isGairiKamboolaPresent(chart: VedicChart, moonAnalysis: MoonAnalysis): Boolean {
        // Gairi Kamboola: Moon in angle but weak
        val angularHouses = listOf(1, 4, 7, 10)
        return moonAnalysis.moonHouse in angularHouses &&
               moonAnalysis.moonStrength.score < 3
    }

    private fun isDhurufaPresent(chart: VedicChart, moonAnalysis: MoonAnalysis): Boolean {
        // Dhurufa: Moon in cadent house and weak
        val cadentHouses = listOf(3, 6, 9, 12)
        return moonAnalysis.moonHouse in cadentHouses &&
               moonAnalysis.moonStrength.score <= 2
    }

    // Interpretation generation methods
    private fun generateMoonInterpretation(
        position: PlanetPosition,
        strength: MoonStrength,
        isWaxing: Boolean,
        isVoid: Boolean,
        tithiNumber: Int,
        tithiName: String
    ): String {
        val phaseDesc = if (isWaxing) "waxing (growing)" else "waning (diminishing)"
        val voidDesc = if (isVoid) " Moon is void of course, indicating that nothing will come of this matter as currently proposed." else ""

        return "The Moon, primary significator in Prashna, is in ${position.sign.displayName} " +
               "in the ${position.house}${getOrdinalSuffix(position.house)} house. " +
               "Moon is $phaseDesc on $tithiName. " +
               "The Moon's strength is ${strength.displayName.lowercase()}.$voidDesc"
    }

    private fun generateLagnaInterpretation(
        lagnaSign: ZodiacSign,
        lordPosition: PlanetPosition,
        condition: LagnaCondition,
        planetsInLagna: List<PlanetPosition>
    ): String {
        val planetsDesc = if (planetsInLagna.isEmpty()) {
            "No planets occupy the Lagna."
        } else {
            "Planets in Lagna: ${planetsInLagna.joinToString { it.planet.displayName }}."
        }

        return "${lagnaSign.displayName} rises at the time of question. " +
               "The Lagna Lord ${lagnaSign.ruler.displayName} is in the ${lordPosition.house}${getOrdinalSuffix(lordPosition.house)} house. " +
               "Lagna condition: ${condition.displayName}. $planetsDesc"
    }

    private fun generateHouseInterpretation(
        relevantHouses: List<Int>,
        conditions: Map<Int, HouseCondition>,
        category: PrashnaCategory
    ): String {
        val houseDescs = relevantHouses.map { house ->
            val condition = conditions[house]
            val signification = PRASHNA_HOUSE_SIGNIFICATIONS[house]
            "House $house (${signification?.name}): ${condition?.condition?.displayName ?: "Unknown"}"
        }

        return "For ${category.displayName} questions, we examine houses ${relevantHouses.joinToString()}. " +
               houseDescs.joinToString(". ") + "."
    }

    private fun getLagnaSignIndication(sign: ZodiacSign): String {
        return when (sign.element) {
            "Fire" -> "Dynamic energy, quick results, direct action needed"
            "Earth" -> "Practical matters, material concerns, patience required"
            "Air" -> "Communication important, mental approach, flexibility needed"
            "Water" -> "Emotional undertones, intuition important, hidden factors"
            else -> "Mixed indications"
        }
    }

    private fun getMoonHouseIndication(house: Int): String {
        return PRASHNA_HOUSE_SIGNIFICATIONS[house]?.let {
            "Focus on ${it.primaryTopics.take(2).joinToString()}"
        } ?: "General indications"
    }

    private fun getHoraLordIndication(lord: Planet): String {
        return when (lord) {
            Planet.SUN -> "Authority, government matters favorable"
            Planet.MOON -> "Public, women, emotional matters favored"
            Planet.MARS -> "Action, conflict, competitive matters"
            Planet.MERCURY -> "Communication, business, learning favored"
            Planet.JUPITER -> "Excellent for wisdom, growth, spirituality"
            Planet.VENUS -> "Favorable for relationships, pleasures, arts"
            Planet.SATURN -> "Delays possible, persistence needed"
            else -> "Mixed indications"
        }
    }

    private fun getDayLordIndication(lord: Planet): String {
        return when (lord) {
            Planet.SUN -> "Sunday favors authority, vitality matters"
            Planet.MOON -> "Monday favors public, emotional matters"
            Planet.MARS -> "Tuesday favors action, competitive matters"
            Planet.MERCURY -> "Wednesday favors communication, business"
            Planet.JUPITER -> "Thursday highly favorable for most matters"
            Planet.VENUS -> "Friday favors relationships, pleasures"
            Planet.SATURN -> "Saturday requires patience, delays likely"
            else -> "Mixed day influences"
        }
    }

    private fun getNakshatraIndication(nakshatra: Nakshatra): String {
        return "${nakshatra.displayName} ruled by ${nakshatra.ruler.displayName}. " +
               "Deity: ${nakshatra.deity}."
    }

    // Timing calculation helpers
    private fun calculateMoonTransitTime(
        moonAnalysis: MoonAnalysis,
        targetHouse: Int,
        chart: VedicChart
    ): Pair<Double, TimingUnit> {
        val currentHouse = moonAnalysis.moonHouse
        val housesToTravel = if (targetHouse >= currentHouse) {
            targetHouse - currentHouse
        } else {
            12 - currentHouse + targetHouse
        }

        // Moon travels ~1 house per 2.5 days
        val days = housesToTravel * 2.5

        return if (days <= 14) {
            Pair(days, TimingUnit.DAYS)
        } else if (days <= 60) {
            Pair(days / 7, TimingUnit.WEEKS)
        } else {
            Pair(days / 30, TimingUnit.MONTHS)
        }
    }

    private fun calculateLagnaDegreesTiming(lagnaAnalysis: LagnaAnalysis): Pair<Double, TimingUnit> {
        // Remaining degrees in Lagna sign
        val degreesInSign = lagnaAnalysis.lagnaDegree % 30
        val remainingDegrees = 30 - degreesInSign

        // Each degree can represent a day, week, or month based on sign quality
        return when (lagnaAnalysis.lagnaSign.quality) {
            com.astro.storm.data.model.Quality.CARDINAL -> Pair(remainingDegrees, TimingUnit.DAYS)
            com.astro.storm.data.model.Quality.FIXED -> Pair(remainingDegrees, TimingUnit.MONTHS)
            com.astro.storm.data.model.Quality.MUTABLE -> Pair(remainingDegrees, TimingUnit.WEEKS)
        }
    }

    private fun calculateSignificatorTiming(
        chart: VedicChart,
        category: PrashnaCategory
    ): Pair<Double, TimingUnit> {
        val relevantHouse = QUESTION_CATEGORIES[category]?.firstOrNull() ?: 7
        val houseSign = ZodiacSign.fromLongitude(chart.houseCusps[relevantHouse - 1])
        val houseLord = houseSign.ruler
        val lordPosition = chart.planetPositions.first { it.planet == houseLord }

        // Remaining degrees of lord in current sign
        val degreesInSign = lordPosition.longitude % 30
        val remainingDegrees = 30 - degreesInSign

        val unit = TIMING_UNITS[houseLord] ?: TimingUnit.WEEKS
        return Pair(remainingDegrees, unit)
    }

    private fun formatTimingEstimate(value: Double, unit: TimingUnit, language: Language): String {
        val roundedValue = kotlin.math.round(value * 10) / 10
        val unitName = unit.getLocalizedName(language)
        
        return when {
            roundedValue < 1 -> StringResources.get(StringKeyAnalysis.PRASHNA_TIMING_WITHIN, language).format("1 $unitName")
            roundedValue == 1.0 -> StringResources.get(StringKeyAnalysis.PRASHNA_TIMING_ABOUT, language).format("1 $unitName")
            else -> StringResources.get(StringKeyAnalysis.PRASHNA_TIMING_ABOUT, language).format("${roundedValue.toInt()} $unitName")
        }
    }

    private fun calculateTimingConfidence(
        moonAnalysis: MoonAnalysis,
        lagnaAnalysis: LagnaAnalysis
    ): Int {
        var confidence = 50

        if (moonAnalysis.moonStrength.score >= 3) confidence += 15
        if (!moonAnalysis.isVoidOfCourse) confidence += 10
        if (lagnaAnalysis.lagnaCondition == LagnaCondition.STRONG) confidence += 10
        if (moonAnalysis.isWaxing) confidence += 5

        return confidence.coerceIn(20, 80)
    }

    private fun buildTimingExplanation(
        method: TimingMethod,
        value: Double,
        unit: TimingUnit,
        moonAnalysis: MoonAnalysis,
        language: Language
    ): String {
        val methodLabel = method.getLocalizedName(language)
        val timeframe = formatTimingEstimate(value, unit, language)
        val moonSpeed = moonAnalysis.moonSpeed.format(2)
        
        return StringResources.get(StringKeyAnalysis.PRASHNA_TIMING_EXPLANATION, language)
            .format(methodLabel, timeframe, moonSpeed)
    }

    private fun getOrdinalSuffix(number: Int): String {
        return when {
            number in 11..13 -> "th"
            number % 10 == 1 -> "st"
            number % 10 == 2 -> "nd"
            number % 10 == 3 -> "rd"
            else -> "th"
        }
    }

    private fun Double.format(decimals: Int): String {
        return "%.${decimals}f".format(this)
    }

    /**
     * Get house signification for Prashna
     */
    fun getHouseSignification(house: Int): PrashnaHouseSignification? {
        return PRASHNA_HOUSE_SIGNIFICATIONS[house]
    }

    /**
     * Get all question categories
     */
    fun getQuestionCategories(): List<PrashnaCategory> {
        return PrashnaCategory.entries
    }

    /**
     * Close and clean up resources
     */
    fun close() {
        swissEph.swe_close()
    }
}
