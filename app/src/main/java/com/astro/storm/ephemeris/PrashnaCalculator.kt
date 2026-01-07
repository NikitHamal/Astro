package com.astro.storm.ephemeris

import android.content.Context
import com.astro.storm.data.localization.Language
import com.astro.storm.data.localization.StringKeyAnalysis
import com.astro.storm.data.localization.BikramSambatConverter
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
                nameKey = StringKeyAnalysis.PRASHNA_HOUSE_1_NAME,
                primaryTopics = listOf(StringKeyAnalysis.PRASHNA_TOPIC_QUERENT, StringKeyAnalysis.PRASHNA_TOPIC_HEALTH, StringKeyAnalysis.PRASHNA_TOPIC_SELF, StringKeyAnalysis.PRASHNA_TOPIC_BEGINNING),
                secondaryTopics = listOf(StringKeyAnalysis.PRASHNA_TOPIC_PHYSICAL, StringKeyAnalysis.PRASHNA_TOPIC_SUCCESS, StringKeyAnalysis.PRASHNA_TOPIC_LIFE_FORCE),
                karaka = Planet.SUN,
                bodyPartKey = StringKeyAnalysis.PRASHNA_BODY_HEAD,
                directionKey = StringKeyAnalysis.PRASHNA_DIR_EAST,
                colorKey = StringKeyAnalysis.PRASHNA_COLOR_RED,
                element = Tattva.FIRE
            ),
            2 to PrashnaHouseSignification(
                house = 2,
                nameKey = StringKeyAnalysis.PRASHNA_HOUSE_2_NAME,
                primaryTopics = listOf(StringKeyAnalysis.PRASHNA_TOPIC_WEALTH, StringKeyAnalysis.PRASHNA_TOPIC_FAMILY, StringKeyAnalysis.PRASHNA_TOPIC_SPEECH, StringKeyAnalysis.PRASHNA_TOPIC_RESOURCES),
                secondaryTopics = listOf(StringKeyAnalysis.PRASHNA_TOPIC_FOOD, StringKeyAnalysis.PRASHNA_TOPIC_RIGHT_EYE, StringKeyAnalysis.PRASHNA_TOPIC_FACE, StringKeyAnalysis.PRASHNA_TOPIC_POSSESSIONS),
                karaka = Planet.JUPITER,
                bodyPartKey = StringKeyAnalysis.PRASHNA_BODY_FACE_MOUTH,
                directionKey = StringKeyAnalysis.PRASHNA_DIR_SOUTH_EAST,
                colorKey = StringKeyAnalysis.PRASHNA_COLOR_ORANGE,
                element = Tattva.EARTH
            ),
            3 to PrashnaHouseSignification(
                house = 3,
                nameKey = StringKeyAnalysis.PRASHNA_HOUSE_3_NAME,
                primaryTopics = listOf(StringKeyAnalysis.PRASHNA_TOPIC_COURAGE, StringKeyAnalysis.PRASHNA_TOPIC_SIBLINGS, StringKeyAnalysis.PRASHNA_TOPIC_SHORT_TRAVEL, StringKeyAnalysis.PRASHNA_TOPIC_COMMUNICATION),
                secondaryTopics = listOf(StringKeyAnalysis.PRASHNA_TOPIC_NEIGHBORS, StringKeyAnalysis.PRASHNA_TOPIC_SKILLS, StringKeyAnalysis.PRASHNA_TOPIC_EFFORTS, StringKeyAnalysis.PRASHNA_TOPIC_RIGHT_EAR),
                karaka = Planet.MARS,
                bodyPartKey = StringKeyAnalysis.PRASHNA_BODY_ARMS_SHOULDERS,
                directionKey = StringKeyAnalysis.PRASHNA_DIR_WEST,
                colorKey = StringKeyAnalysis.PRASHNA_COLOR_GREEN,
                element = Tattva.AIR
            ),
            4 to PrashnaHouseSignification(
                house = 4,
                nameKey = StringKeyAnalysis.PRASHNA_HOUSE_4_NAME,
                primaryTopics = listOf(StringKeyAnalysis.PRASHNA_TOPIC_HOME, StringKeyAnalysis.PRASHNA_TOPIC_MOTHER, StringKeyAnalysis.PRASHNA_TOPIC_PROPERTY, StringKeyAnalysis.PRASHNA_TOPIC_VEHICLES),
                secondaryTopics = listOf(StringKeyAnalysis.PRASHNA_TOPIC_EDUCATION, StringKeyAnalysis.PRASHNA_TOPIC_HAPPINESS, StringKeyAnalysis.PRASHNA_TOPIC_TREASURE, StringKeyAnalysis.PRASHNA_TOPIC_STOMACH),
                karaka = Planet.MOON,
                bodyPartKey = StringKeyAnalysis.PRASHNA_BODY_CHEST,
                directionKey = StringKeyAnalysis.PRASHNA_DIR_NORTH,
                colorKey = StringKeyAnalysis.PRASHNA_COLOR_WHITE,
                element = Tattva.WATER
            ),
            5 to PrashnaHouseSignification(
                house = 5,
                nameKey = StringKeyAnalysis.PRASHNA_HOUSE_5_NAME,
                primaryTopics = listOf(StringKeyAnalysis.PRASHNA_TOPIC_CHILDREN, StringKeyAnalysis.PRASHNA_TOPIC_INTELLIGENCE, StringKeyAnalysis.PRASHNA_TOPIC_ROMANCE, StringKeyAnalysis.PRASHNA_TOPIC_SPECULATION),
                secondaryTopics = listOf(StringKeyAnalysis.PRASHNA_TOPIC_EDUCATION, StringKeyAnalysis.PRASHNA_TOPIC_MANTRAS, StringKeyAnalysis.PRASHNA_TOPIC_PAST_MERIT, StringKeyAnalysis.PRASHNA_TOPIC_STOMACH),
                karaka = Planet.JUPITER,
                bodyPartKey = StringKeyAnalysis.PRASHNA_BODY_UPPER_ABDOMEN,
                directionKey = StringKeyAnalysis.PRASHNA_DIR_EAST_NORTH,
                colorKey = StringKeyAnalysis.PRASHNA_COLOR_YELLOW,
                element = Tattva.FIRE
            ),
            6 to PrashnaHouseSignification(
                house = 6,
                nameKey = StringKeyAnalysis.PRASHNA_HOUSE_6_NAME,
                primaryTopics = listOf(StringKeyAnalysis.PRASHNA_TOPIC_ENEMIES, StringKeyAnalysis.PRASHNA_TOPIC_DISEASE, StringKeyAnalysis.PRASHNA_TOPIC_OBSTACLES, StringKeyAnalysis.PRASHNA_TOPIC_SERVICE),
                secondaryTopics = listOf(StringKeyAnalysis.PRASHNA_TOPIC_DEBTS, StringKeyAnalysis.PRASHNA_TOPIC_COMPETITORS, StringKeyAnalysis.PRASHNA_TOPIC_UNCLE, StringKeyAnalysis.PRASHNA_TOPIC_INTESTINES),
                karaka = Planet.MARS,
                bodyPartKey = StringKeyAnalysis.PRASHNA_BODY_LOWER_ABDOMEN,
                directionKey = StringKeyAnalysis.PRASHNA_DIR_SOUTH,
                colorKey = StringKeyAnalysis.PRASHNA_COLOR_GREY_BLUE,
                element = Tattva.EARTH
            ),
            7 to PrashnaHouseSignification(
                house = 7,
                nameKey = StringKeyAnalysis.PRASHNA_HOUSE_7_NAME,
                primaryTopics = listOf(StringKeyAnalysis.PRASHNA_TOPIC_MARRIAGE, StringKeyAnalysis.PRASHNA_TOPIC_PARTNERSHIP, StringKeyAnalysis.PRASHNA_TOPIC_DEALS, StringKeyAnalysis.PRASHNA_TOPIC_OPPONENT),
                secondaryTopics = listOf(StringKeyAnalysis.PRASHNA_TOPIC_FOREIGN_TRAVEL, StringKeyAnalysis.PRASHNA_TOPIC_DEATH, StringKeyAnalysis.PRASHNA_TOPIC_SEXUAL, StringKeyAnalysis.PRASHNA_TOPIC_KIDNEYS),
                karaka = Planet.VENUS,
                bodyPartKey = StringKeyAnalysis.PRASHNA_BODY_BELOW_NAVEL,
                directionKey = StringKeyAnalysis.PRASHNA_DIR_WEST,
                colorKey = StringKeyAnalysis.PRASHNA_COLOR_MULTI,
                element = Tattva.AIR
            ),
            8 to PrashnaHouseSignification(
                house = 8,
                nameKey = StringKeyAnalysis.PRASHNA_HOUSE_8_NAME,
                primaryTopics = listOf(StringKeyAnalysis.PRASHNA_TOPIC_DEATH, StringKeyAnalysis.PRASHNA_TOPIC_HIDDEN, StringKeyAnalysis.PRASHNA_TOPIC_INHERITANCE, StringKeyAnalysis.PRASHNA_TOPIC_TRANSFORMATION),
                secondaryTopics = listOf(StringKeyAnalysis.PRASHNA_TOPIC_CHRONIC, StringKeyAnalysis.PRASHNA_TOPIC_OCCULT, StringKeyAnalysis.PRASHNA_TOPIC_MYSTERY, StringKeyAnalysis.PRASHNA_TOPIC_REPRODUCTIVE),
                karaka = Planet.SATURN,
                bodyPartKey = StringKeyAnalysis.PRASHNA_BODY_PRIVATE_PARTS,
                directionKey = StringKeyAnalysis.PRASHNA_DIR_SOUTH_WEST,
                colorKey = StringKeyAnalysis.PRASHNA_COLOR_BLACK,
                element = Tattva.WATER
            ),
            9 to PrashnaHouseSignification(
                house = 9,
                nameKey = StringKeyAnalysis.PRASHNA_HOUSE_9_NAME,
                primaryTopics = listOf(StringKeyAnalysis.PRASHNA_TOPIC_LUCK, StringKeyAnalysis.PRASHNA_TOPIC_FATHER, StringKeyAnalysis.PRASHNA_TOPIC_LONG_TRAVEL, StringKeyAnalysis.PRASHNA_TOPIC_HIGHER_LEARNING),
                secondaryTopics = listOf(StringKeyAnalysis.PRASHNA_TOPIC_RELIGION, StringKeyAnalysis.PRASHNA_TOPIC_GURU, StringKeyAnalysis.PRASHNA_TOPIC_FORTUNE, StringKeyAnalysis.PRASHNA_TOPIC_THIGHS),
                karaka = Planet.JUPITER,
                bodyPartKey = StringKeyAnalysis.PRASHNA_BODY_THIGHS,
                directionKey = StringKeyAnalysis.PRASHNA_DIR_EAST,
                colorKey = StringKeyAnalysis.PRASHNA_COLOR_GOLDEN,
                element = Tattva.FIRE
            ),
            10 to PrashnaHouseSignification(
                house = 10,
                nameKey = StringKeyAnalysis.PRASHNA_HOUSE_10_NAME,
                primaryTopics = listOf(StringKeyAnalysis.PRASHNA_TOPIC_CAREER, StringKeyAnalysis.PRASHNA_TOPIC_STATUS, StringKeyAnalysis.PRASHNA_TOPIC_AUTHORITY, StringKeyAnalysis.PRASHNA_TOPIC_GOVERNMENT),
                secondaryTopics = listOf(StringKeyAnalysis.PRASHNA_TOPIC_FATHER, StringKeyAnalysis.PRASHNA_TOPIC_PUBLIC_LIFE, StringKeyAnalysis.PRASHNA_TOPIC_ACHIEVEMENT, StringKeyAnalysis.PRASHNA_TOPIC_KNEES),
                karaka = Planet.SUN,
                bodyPartKey = StringKeyAnalysis.PRASHNA_BODY_KNEES,
                directionKey = StringKeyAnalysis.PRASHNA_DIR_SOUTH,
                colorKey = StringKeyAnalysis.PRASHNA_COLOR_WHITE,
                element = Tattva.EARTH
            ),
            11 to PrashnaHouseSignification(
                house = 11,
                nameKey = StringKeyAnalysis.PRASHNA_HOUSE_11_NAME,
                primaryTopics = listOf(StringKeyAnalysis.PRASHNA_TOPIC_GAINS, StringKeyAnalysis.PRASHNA_TOPIC_FULFILLMENT, StringKeyAnalysis.PRASHNA_TOPIC_ELDER_SIBLINGS, StringKeyAnalysis.PRASHNA_TOPIC_FRIENDS),
                secondaryTopics = listOf(StringKeyAnalysis.PRASHNA_TOPIC_INCOME, StringKeyAnalysis.PRASHNA_TOPIC_WISHES, StringKeyAnalysis.PRASHNA_TOPIC_RECOVERY, StringKeyAnalysis.PRASHNA_TOPIC_ANKLES),
                karaka = Planet.JUPITER,
                bodyPartKey = StringKeyAnalysis.PRASHNA_BODY_CALVES_ANKLES,
                directionKey = StringKeyAnalysis.PRASHNA_DIR_NORTH_WEST,
                colorKey = StringKeyAnalysis.PRASHNA_COLOR_GOLDEN_YELLOW,
                element = Tattva.AIR
            ),
            12 to PrashnaHouseSignification(
                house = 12,
                nameKey = StringKeyAnalysis.PRASHNA_HOUSE_12_NAME,
                primaryTopics = listOf(StringKeyAnalysis.PRASHNA_TOPIC_LOSSES, StringKeyAnalysis.PRASHNA_TOPIC_EXPENSES, StringKeyAnalysis.PRASHNA_TOPIC_LIBERATION, StringKeyAnalysis.PRASHNA_TOPIC_FOREIGN_LANDS),
                secondaryTopics = listOf(StringKeyAnalysis.PRASHNA_TOPIC_BED_PLEASURES, StringKeyAnalysis.PRASHNA_TOPIC_SLEEP, StringKeyAnalysis.PRASHNA_TOPIC_HOSPITAL, StringKeyAnalysis.PRASHNA_TOPIC_FEET),
                karaka = Planet.SATURN,
                bodyPartKey = StringKeyAnalysis.PRASHNA_BODY_FEET,
                directionKey = StringKeyAnalysis.PRASHNA_DIR_NORTH_EAST,
                colorKey = StringKeyAnalysis.PRASHNA_COLOR_BROWN,
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
            name = StringResources.get(StringKeyAnalysis.PRASHNA_CHART_LABEL, language),
            dateTime = questionTime,
            latitude = latitude,
            longitude = longitude,
            timezone = timezone,
            location = StringResources.get(StringKeyAnalysis.PRASHNA_QUESTION_LOCATION, language)
        )

        // Calculate the chart using Swiss Ephemeris directly
        val chart = calculatePrashnaChart(prashnaData)

        // Perform comprehensive analysis
        val moonAnalysis = analyzeMoon(chart, questionTime, latitude, longitude, timezone, language)
        val lagnaAnalysis = analyzeLagna(chart, language)
        val houseAnalysis = analyzeHouses(chart, category, language)
        val specialYogas = detectPrashnaYogas(chart, moonAnalysis, lagnaAnalysis, language)
        val omens = detectOmens(chart, questionTime, moonAnalysis, language)

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
        timezone: String,
        language: Language
    ): MoonAnalysis {
        val moonPosition = chart.planetPositions.first { it.planet == Planet.MOON }
        val sunPosition = chart.planetPositions.first { it.planet == Planet.SUN }

        // Calculate tithi
        val moonSunDiff = normalizeDegrees(moonPosition.longitude - sunPosition.longitude)
        val tithiNumber = (floor(moonSunDiff / 12.0).toInt() % 30) + 1

        // Determine if moon is waxing or waning
        val isWaxing = moonSunDiff < 180.0

        // Calculate moon age (days since new moon)
        val moonAge = moonSunDiff / MOON_DAILY_MOTION

        // Determine moon strength
        val moonStrength = calculateMoonStrength(moonPosition, sunPosition, chart)

        // Check if void of course
        val (isVoid, lastAspect, nextAspect) = checkVoidOfCourse(moonPosition, chart)

        // Generate interpretation
        val tithiName = getTithiName(tithiNumber, language)
        val interpretation = generateMoonInterpretation(
            moonPosition, moonStrength, isWaxing, isVoid, tithiNumber, tithiName, language
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
    private fun analyzeLagna(chart: VedicChart, language: Language): LagnaAnalysis {
        val lagnaSign = ZodiacSign.fromLongitude(chart.ascendant)
        val lagnaLord = lagnaSign.ruler
        val lagnaLordPosition = chart.planetPositions.first { it.planet == lagnaLord }

        // Calculate Lagna Lord strength
        val lagnaLordStrength = calculatePlanetStrength(lagnaLordPosition, chart)

        // Find aspects to Lagna
        val lagnaAspects = chart.planetPositions.mapNotNull { planet ->
            if (isAspectingHouse(planet, 1)) {
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
            lagnaSign, lagnaLordPosition, lagnaCondition, planetsInLagna, language
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
        category: PrashnaCategory,
        language: Language
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
                if (isAspectingHouse(planet, house)) {
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

        val interpretation = generateHouseInterpretation(relevantHouses, houseConditions, category, language)

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
        moonAnalysis: MoonAnalysis,
        language: Language
    ): List<PrashnaOmen> {
        val omens = mutableListOf<PrashnaOmen>()

        // 1. Prashna Lagna sign omen
        val lagnaSign = ZodiacSign.fromLongitude(chart.ascendant)
        omens.add(
            PrashnaOmen(
                type = OmenType.PRASHNA_LAGNA,
                description = StringResources.get(StringKeyAnalysis.PRASHNA_OMEN_LAGNA_DESC, language, lagnaSign.getLocalizedName(language)),
                indication = getLagnaSignIndication(lagnaSign, language),
                isPositive = lagnaSign.element in listOf("Fire", "Air")
            )
        )

        // 2. Moon placement omen
        val moonHouseSignification = PRASHNA_HOUSE_SIGNIFICATIONS[moonAnalysis.moonHouse]
        omens.add(
            PrashnaOmen(
                type = OmenType.MOON_PLACEMENT,
                description = StringResources.get(StringKeyAnalysis.PRASHNA_OMEN_MOON_PLACEMENT_DESC, language, moonAnalysis.moonHouse, moonHouseSignification?.getLocalizedName(language) ?: ""),
                indication = getMoonHouseIndication(moonAnalysis.moonHouse, language),
                isPositive = moonAnalysis.moonHouse in listOf(1, 4, 5, 7, 9, 10, 11)
            )
        )

        // 3. Hora Lord omen
        val horaLord = calculateHoraLord(questionTime)
        omens.add(
            PrashnaOmen(
                type = OmenType.HORA_LORD,
                description = StringResources.get(StringKeyAnalysis.PRASHNA_OMEN_HORA_LORD_DESC, language, horaLord.getLocalizedName(language)),
                indication = getHoraLordIndication(horaLord, language),
                isPositive = horaLord in NATURAL_BENEFICS
            )
        )

        // 4. Day Lord omen
        val dayLord = getDayLord(questionTime)
        omens.add(
            PrashnaOmen(
                type = OmenType.DAY_LORD,
                description = StringResources.get(StringKeyAnalysis.PRASHNA_OMEN_DAY_LORD_DESC, language, dayLord.getLocalizedName(language)),
                indication = getDayLordIndication(dayLord, language),
                isPositive = dayLord in listOf(Planet.JUPITER, Planet.VENUS, Planet.MERCURY)
            )
        )

        // 5. Nakshatra omen
        omens.add(
            PrashnaOmen(
                type = OmenType.NAKSHATRA,
                description = StringResources.get(StringKeyAnalysis.PRASHNA_OMEN_NAKSHATRA_DESC, language, moonAnalysis.nakshatra.getLocalizedName(language)),
                indication = getNakshatraIndication(moonAnalysis.nakshatra, language),
                isPositive = isAuspiciousNakshatra(moonAnalysis.nakshatra)
            )
        )

        // 6. Check for planetary war
        val planetaryWars = detectPlanetaryWars(chart)
        for (war in planetaryWars) {
            omens.add(
                PrashnaOmen(
                    type = OmenType.PLANETARY_WAR,
                    description = StringResources.get(StringKeyAnalysis.PRASHNA_OMEN_PLANETARY_WAR_DESC, language, war.first.getLocalizedName(language), war.second.getLocalizedName(language)),
                    indication = StringResources.get(StringKeyAnalysis.PRASHNA_OMEN_WAR_IND, language),
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
                    description = StringResources.get(StringKeyAnalysis.PRASHNA_OMEN_COMBUSTION_DESC, language, planet.planet.getLocalizedName(language)),
                    indication = StringResources.get(StringKeyAnalysis.PRASHNA_OMEN_COMBUST_IND, language, planet.planet.getLocalizedName(language)),
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
                    description = StringResources.get(StringKeyAnalysis.PRASHNA_OMEN_RETROGRADE_DESC, language, planet.planet.getLocalizedName(language)),
                    indication = StringResources.get(StringKeyAnalysis.PRASHNA_OMEN_RETRO_IND, language, planet.planet.getLocalizedName(language)),
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
            val symbolKey = if (yoga.isPositive) StringKeyAnalysis.PRASHNA_YOGA_SYMBOL_POSITIVE else StringKeyAnalysis.PRASHNA_YOGA_SYMBOL_NEGATIVE
            if (yoga.isPositive) {
                score += yoga.strength * 4
                supportingFactors.add(StringResources.get(symbolKey, language).format(yoga.name, yoga.interpretation))
            } else {
                score -= yoga.strength * 4
                opposingFactors.add(StringResources.get(symbolKey, language).format(yoga.name, yoga.interpretation))
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
            score >= 50 -> StringResources.get(StringKeyAnalysis.PRASHNA_REASON_STRONGLY_YES, language)
            score >= 20 -> StringResources.get(StringKeyAnalysis.PRASHNA_REASON_YES, language)
            score >= -20 -> StringResources.get(StringKeyAnalysis.PRASHNA_REASON_UNCERTAIN, language)
            score >= -50 -> StringResources.get(StringKeyAnalysis.PRASHNA_REASON_CHALLENGES, language)
            else -> StringResources.get(StringKeyAnalysis.PRASHNA_REASON_STRONGLY_NO, language)
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
        if (positiveYogas.any { it.name == StringResources.get(StringKeyAnalysis.PRASHNA_YOGA_ITHASALA_NAME, language) }) {
            recommendations.add(StringResources.get(StringKeyAnalysis.PRASHNA_REC_ACT_PROMPTLY, language))
        }
        if (specialYogas.any { it.name == StringResources.get(StringKeyAnalysis.PRASHNA_YOGA_NAKTA_NAME, language) }) {
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
            appendLine("- ${StringResources.get(StringKeyAnalysis.PRASHNA_REPORT_POSITION, language)}: ${moonAnalysis.moonSign.getLocalizedName(language)} in ${StringResources.get(StringKeyAnalysis.PRASHNA_REPORT_HOUSE, language)} ${moonAnalysis.moonHouse.localized(language)}")
            appendLine("- ${StringResources.get(StringKeyAnalysis.PRASHNA_REPORT_NAKSHATRA, language)}: ${moonAnalysis.nakshatra.getLocalizedName(language)} (${StringResources.get(StringKeyAnalysis.PRASHNA_REPORT_PADA, language)} ${moonAnalysis.nakshatraPada.localized(language)})")
            appendLine("- ${StringResources.get(StringKeyAnalysis.PRASHNA_REPORT_PHASE, language)}: ${if (moonAnalysis.isWaxing) StringResources.get(StringKeyAnalysis.PRASHNA_REPORT_WAXING, language) else StringResources.get(StringKeyAnalysis.PRASHNA_REPORT_WANING, language)} - ${moonAnalysis.tithiName}")
            appendLine("- ${StringResources.get(StringKeyAnalysis.PRASHNA_REPORT_STRENGTH, language)}: ${moonAnalysis.moonStrength.getLocalizedName(language)}")
            if (moonAnalysis.isVoidOfCourse) {
                appendLine("- ${StringResources.get(StringKeyAnalysis.PRASHNA_REPORT_WARNING_VOID, language)}")
            }
            appendLine()

            appendLine("${StringResources.get(StringKeyAnalysis.PRASHNA_REPORT_LAGNA_ANALYSIS, language)}:")
            appendLine("- ${StringResources.get(StringKeyAnalysis.PRASHNA_REPORT_RISING_SIGN, language)}: ${lagnaAnalysis.lagnaSign.getLocalizedName(language)}")
            appendLine("- ${StringResources.get(StringKeyAnalysis.PRASHNA_REPORT_LAGNA_LORD, language)}: ${lagnaAnalysis.lagnaLord.getLocalizedName(language)} in ${StringResources.get(StringKeyAnalysis.PRASHNA_REPORT_HOUSE, language)} ${lagnaAnalysis.lagnaLordPosition.house.localized(language)}")
            appendLine("- ${StringResources.get(StringKeyAnalysis.PRASHNA_REPORT_CONDITION, language)}: ${lagnaAnalysis.lagnaCondition.getLocalizedName(language)}")
            appendLine()

            appendLine("${StringResources.get(StringKeyAnalysis.PRASHNA_REPORT_RELEVANT_HOUSES, language)} (${houseAnalysis.relevantHouses.joinToString { it.localized(language) }}):")
            for (house in houseAnalysis.relevantHouses) {
                val condition = houseAnalysis.houseConditions[house]
                appendLine("- ${StringResources.get(StringKeyAnalysis.PRASHNA_REPORT_HOUSE, language)} ${house.localized(language)}: ${condition?.condition?.getLocalizedName(language)} (${StringResources.get(StringKeyAnalysis.PRASHNA_REPORT_LAGNA_LORD, language)} in ${StringResources.get(StringKeyAnalysis.PRASHNA_REPORT_HOUSE, language)} ${condition?.lordPosition?.localized(language)})")
            }
            appendLine()

            if (specialYogas.isNotEmpty()) {
                appendLine(StringResources.get(StringKeyAnalysis.PRASHNA_YOGAS_TITLE, language) + ":")
                for (yoga in specialYogas) {
                    val symbolKey = if (yoga.isPositive) StringKeyAnalysis.PRASHNA_YOGA_SYMBOL_POSITIVE else StringKeyAnalysis.PRASHNA_YOGA_SYMBOL_NEGATIVE
                    appendLine(StringResources.get(symbolKey, language).format(yoga.name, yoga.interpretation))
                }
                appendLine()
            }

            if (timingPrediction.willEventOccur) {
                appendLine(StringResources.get(StringKeyAnalysis.PRASHNA_REPORT_TIMING, language) + ":")
                appendLine("${StringResources.get(StringKeyAnalysis.PRASHNA_REPORT_ESTIMATED, language)}: ${timingPrediction.estimatedTime}")
                appendLine("${StringResources.get(StringKeyAnalysis.PRASHNA_REPORT_METHOD, language)}: ${timingPrediction.timingMethod.getLocalizedName(language)}")
                appendLine("${StringResources.get(StringKeyAnalysis.PRASHNA_REPORT_CONFIDENCE, language)}: ${timingPrediction.confidence}%")
                appendLine()
            }

            appendLine(StringResources.get(StringKeyAnalysis.PRASHNA_SUPPORTING_FACTORS, language) + ":")
            for (factor in judgment.supportingFactors.take(5)) {
                appendLine("+ $factor")
            }
            appendLine()

            if (judgment.opposingFactors.isNotEmpty()) {
                appendLine(StringResources.get(StringKeyAnalysis.PRASHNA_CHALLENGES, language) + ":")
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

    private fun getTithiName(tithiNumber: Int, language: Language): String {
        val tithiKey = when (tithiNumber) {
            1, 16 -> StringKeyAnalysis.PRASHNA_TITHI_PRATIPADA
            2, 17 -> StringKeyAnalysis.PRASHNA_TITHI_DWITIYA
            3, 18 -> StringKeyAnalysis.PRASHNA_TITHI_TRITIYA
            4, 19 -> StringKeyAnalysis.PRASHNA_TITHI_CHATURTHI
            5, 20 -> StringKeyAnalysis.PRASHNA_TITHI_PANCHAMI
            6, 21 -> StringKeyAnalysis.PRASHNA_TITHI_SHASHTHI
            7, 22 -> StringKeyAnalysis.PRASHNA_TITHI_SAPTAMI
            8, 23 -> StringKeyAnalysis.PRASHNA_TITHI_ASHTAMI
            9, 24 -> StringKeyAnalysis.PRASHNA_TITHI_NAVAMI
            10, 25 -> StringKeyAnalysis.PRASHNA_TITHI_DASHAMI
            11, 26 -> StringKeyAnalysis.PRASHNA_TITHI_EKADASHI
            12, 27 -> StringKeyAnalysis.PRASHNA_TITHI_DWADASHI
            13, 28 -> StringKeyAnalysis.PRASHNA_TITHI_TRAYODASHI
            14, 29 -> StringKeyAnalysis.PRASHNA_TITHI_CHATURDASHI
            15 -> StringKeyAnalysis.PRASHNA_TITHI_PURNIMA
            30 -> StringKeyAnalysis.PRASHNA_TITHI_AMAVASYA
            else -> StringKeyAnalysis.PRASHNA_TITHI_PRATIPADA
        }

        val name = StringResources.get(tithiKey, language)
        
        return when {
            tithiNumber == 15 || tithiNumber == 30 -> name
            tithiNumber < 15 -> StringResources.get(StringKeyAnalysis.PRASHNA_TITHI_SHUKLA, language).format(name)
            else -> StringResources.get(StringKeyAnalysis.PRASHNA_TITHI_KRISHNA, language).format(name)
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

    private fun isAspectingHouse(planet: PlanetPosition, targetHouse: Int): Boolean {
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
        tithiName: String,
        language: Language
    ): String {
        val phaseDesc = if (isWaxing) StringResources.get(StringKeyAnalysis.PRASHNA_REPORT_WAXING, language) else StringResources.get(StringKeyAnalysis.PRASHNA_REPORT_WANING, language)
        val voidDesc = if (isVoid) " (" + StringResources.get(StringKeyAnalysis.PRASHNA_REPORT_WARNING_VOID, language) + ")" else ""

        return StringResources.get(StringKeyAnalysis.PRASHNA_MOON_INTERP_TEMPLATE, language).format(
            position.planet.getLocalizedName(language),
            position.sign.getLocalizedName(language),
            position.house.localized(language),
            phaseDesc,
            tithiName,
            strength.getLocalizedName(language),
            voidDesc
        )
    }

    private fun generateLagnaInterpretation(
        lagnaSign: ZodiacSign,
        lordPosition: PlanetPosition,
        condition: LagnaCondition,
        planetsInLagna: List<PlanetPosition>,
        language: Language
    ): String {
        val planetsDesc = if (planetsInLagna.isEmpty()) {
            ""
        } else {
            " " + StringResources.get(StringKeyAnalysis.PRASHNA_PLANETS_IN_LAGNA_TEMPLATE, language)
                .format(planetsInLagna.joinToString { it.planet.getLocalizedName(language) })
        }

        return StringResources.get(StringKeyAnalysis.PRASHNA_LAGNA_INTERP_TEMPLATE, language).format(
            lagnaSign.getLocalizedName(language),
            lagnaSign.ruler.getLocalizedName(language),
            lordPosition.house.localized(language),
            condition.getLocalizedName(language),
            planetsDesc
        )
    }

    private fun generateHouseInterpretation(
        relevantHouses: List<Int>,
        conditions: Map<Int, HouseCondition>,
        category: PrashnaCategory,
        language: Language
    ): String {
        val houseDescs = relevantHouses.map { house ->
            val condition = conditions[house]
            StringResources.get(StringKeyAnalysis.PRASHNA_HOUSE_ITEM_TEMPLATE, language).format(
                house.localized(language),
                condition?.condition?.getLocalizedName(language) ?: ""
            )
        }

        return StringResources.get(StringKeyAnalysis.PRASHNA_HOUSE_INTERP_TEMPLATE, language).format(
            category.getLocalizedName(language),
            houseDescs.joinToString(". ")
        )
    }

    private fun getLagnaSignIndication(sign: ZodiacSign, language: Language): String {
        val key = when (sign.element.lowercase()) {
            "fire" -> StringKeyAnalysis.PRASHNA_TATTVA_FIRE
            "earth" -> StringKeyAnalysis.PRASHNA_TATTVA_EARTH
            "air" -> StringKeyAnalysis.PRASHNA_TATTVA_AIR
            "water" -> StringKeyAnalysis.PRASHNA_TATTVA_WATER
            else -> StringKeyAnalysis.PRASHNA_TATTVA_ETHER
        }
        return StringResources.get(key, language)
    }

    private fun getMoonHouseIndication(house: Int, language: Language): String {
        return StringResources.get(StringKeyAnalysis.PRASHNA_MOON_HOUSE_IND_TEMPLATE, language).format(house.localized(language))
    }

    private fun getHoraLordIndication(lord: Planet, language: Language): String {
        val key = when (lord) {
            Planet.SUN -> StringKeyAnalysis.VARSHA_YEARLORD_SUN
            Planet.MOON -> StringKeyAnalysis.VARSHA_YEARLORD_MOON
            Planet.MARS -> StringKeyAnalysis.VARSHA_YEARLORD_MARS
            Planet.MERCURY -> StringKeyAnalysis.VARSHA_YEARLORD_MERCURY
            Planet.JUPITER -> StringKeyAnalysis.VARSHA_YEARLORD_JUPITER
            Planet.VENUS -> StringKeyAnalysis.VARSHA_YEARLORD_VENUS
            Planet.SATURN -> StringKeyAnalysis.VARSHA_YEARLORD_SATURN
            else -> StringKeyAnalysis.VARSHA_YEARLORD_GENERIC
        }
        return StringResources.get(key, language)
    }

    private fun getDayLordIndication(lord: Planet, language: Language): String {
        val key = when (lord) {
            Planet.SUN -> StringKeyAnalysis.VARA_SUNDAY
            Planet.MOON -> StringKeyAnalysis.VARA_MONDAY
            Planet.MARS -> StringKeyAnalysis.VARA_TUESDAY
            Planet.MERCURY -> StringKeyAnalysis.VARA_WEDNESDAY
            Planet.JUPITER -> StringKeyAnalysis.VARA_THURSDAY
            Planet.VENUS -> StringKeyAnalysis.VARA_FRIDAY
            Planet.SATURN -> StringKeyAnalysis.VARA_SATURDAY
            else -> StringKeyAnalysis.AUSPICIOUSNESS_NEUTRAL
        }
        return StringResources.get(key, language)
    }

    private fun getNakshatraIndication(nakshatra: Nakshatra, language: Language): String {
        return StringResources.get(StringKeyAnalysis.PRASHNA_NAKSHATRA_IND_TEMPLATE, language).format(
            nakshatra.getLocalizedName(language),
            nakshatra.ruler.getLocalizedName(language)
        )
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
            roundedValue < 1 -> StringResources.get(StringKeyAnalysis.PRASHNA_TIMING_WITHIN, language).format("1 $unitName".let { if (language == Language.NEPALI) BikramSambatConverter.toNepaliNumerals(it) else it })
            roundedValue == 1.0 -> StringResources.get(StringKeyAnalysis.PRASHNA_TIMING_ABOUT, language).format("1 $unitName".let { if (language == Language.NEPALI) BikramSambatConverter.toNepaliNumerals(it) else it })
            else -> StringResources.get(StringKeyAnalysis.PRASHNA_TIMING_ABOUT, language).format("${roundedValue.toInt()} $unitName".let { if (language == Language.NEPALI) BikramSambatConverter.toNepaliNumerals(it) else it })
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
        val moonSpeed = moonAnalysis.moonSpeed.localized(language, 2)
        
        return StringResources.get(StringKeyAnalysis.PRASHNA_TIMING_EXPLANATION, language).format(methodLabel, timeframe, moonSpeed)
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

    private fun Int.localized(language: Language): String {
        return if (language == Language.NEPALI) BikramSambatConverter.toNepaliNumerals(this) else this.toString()
    }

    private fun Double.localized(language: Language, precision: Int = 1): String {
        val formatted = "%.${precision}f".format(this)
        return if (language == Language.NEPALI) BikramSambatConverter.toNepaliNumerals(formatted) else formatted
    }

    /**
     * Close and clean up resources
     */
    fun close() {
        swissEph.swe_close()
    }
}
