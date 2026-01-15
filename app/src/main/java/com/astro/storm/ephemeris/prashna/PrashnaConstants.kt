package com.astro.storm.ephemeris.prashna

import com.astro.storm.core.common.StringKeyAnalysis
import com.astro.storm.core.model.Planet
import swisseph.SweConst

object PrashnaConstants {
    const val AYANAMSA_LAHIRI = SweConst.SE_SIDM_LAHIRI
    const val SEFLG_SIDEREAL = SweConst.SEFLG_SIDEREAL
    const val SEFLG_SPEED = SweConst.SEFLG_SPEED

    // Orb values for aspects (in degrees)
    const val CONJUNCTION_ORB = 10.0
    const val TRINE_ORB = 8.0
    const val SEXTILE_ORB = 6.0
    const val SQUARE_ORB = 8.0
    const val OPPOSITION_ORB = 10.0

    // Prashna-specific constants
    const val DEGREES_PER_SIGN = 30.0
    const val DEGREES_PER_NAKSHATRA = 360.0 / 27.0

    // Moon movement per day (approximately 13.2 degrees)
    const val MOON_DAILY_MOTION = 13.2

    // Natural benefics and malefics
    val NATURAL_BENEFICS = setOf(Planet.JUPITER, Planet.VENUS, Planet.MERCURY, Planet.MOON)
    val NATURAL_MALEFICS = setOf(Planet.SATURN, Planet.MARS, Planet.RAHU, Planet.KETU, Planet.SUN)

    // Prashna-specific house significations
    val PRASHNA_HOUSE_SIGNIFICATIONS = mapOf(
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
    val QUESTION_CATEGORIES = mapOf(
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
    const val FAVORABLE_ASHTAKAVARGA_THRESHOLD = 4

    // Timing units based on planetary strength
    val TIMING_UNITS = mapOf(
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
