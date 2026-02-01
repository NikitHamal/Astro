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
            nameKey = StringKeyPrashnaPart2.PRASHNA_HOUSE_1_NAME,
            primaryTopics = listOf(StringKeyPrashnaPart1.PRASHNA_TOPIC_QUERENT, StringKeyPrashnaPart1.PRASHNA_TOPIC_HEALTH, StringKeyPrashnaPart1.PRASHNA_TOPIC_SELF, StringKeyPrashnaPart1.PRASHNA_TOPIC_BEGINNING),
            secondaryTopics = listOf(StringKeyPrashnaPart1.PRASHNA_TOPIC_PHYSICAL, StringKeyPrashnaPart1.PRASHNA_TOPIC_SUCCESS, StringKeyPrashnaPart1.PRASHNA_TOPIC_LIFE_FORCE),
            karaka = Planet.SUN,
            bodyPartKey = StringKeyPrashnaPart2.PRASHNA_BODY_HEAD,
            directionKey = StringKeyPrashnaPart2.PRASHNA_DIR_EAST,
            colorKey = StringKeyPrashnaPart1.PRASHNA_COLOR_RED,
            element = Tattva.FIRE
        ),
        2 to PrashnaHouseSignification(
            house = 2,
            nameKey = StringKeyPrashnaPart2.PRASHNA_HOUSE_2_NAME,
            primaryTopics = listOf(StringKeyPrashnaPart1.PRASHNA_TOPIC_WEALTH, StringKeyPrashnaPart1.PRASHNA_TOPIC_FAMILY, StringKeyPrashnaPart1.PRASHNA_TOPIC_SPEECH, StringKeyPrashnaPart1.PRASHNA_TOPIC_RESOURCES),
            secondaryTopics = listOf(StringKeyPrashnaPart1.PRASHNA_TOPIC_FOOD, StringKeyPrashnaPart1.PRASHNA_TOPIC_RIGHT_EYE, StringKeyPrashnaPart1.PRASHNA_TOPIC_FACE, StringKeyPrashnaPart1.PRASHNA_TOPIC_POSSESSIONS),
            karaka = Planet.JUPITER,
            bodyPartKey = StringKeyPrashnaPart2.PRASHNA_BODY_FACE_MOUTH,
            directionKey = StringKeyPrashnaPart2.PRASHNA_DIR_SOUTH_EAST,
            colorKey = StringKeyPrashnaPart1.PRASHNA_COLOR_ORANGE,
            element = Tattva.EARTH
        ),
        3 to PrashnaHouseSignification(
            house = 3,
            nameKey = StringKeyPrashnaPart2.PRASHNA_HOUSE_3_NAME,
            primaryTopics = listOf(StringKeyPrashnaPart1.PRASHNA_TOPIC_COURAGE, StringKeyPrashnaPart1.PRASHNA_TOPIC_SIBLINGS, StringKeyPrashnaPart1.PRASHNA_TOPIC_SHORT_TRAVEL, StringKeyPrashnaPart1.PRASHNA_TOPIC_COMMUNICATION),
            secondaryTopics = listOf(StringKeyPrashnaPart1.PRASHNA_TOPIC_NEIGHBORS, StringKeyPrashnaPart1.PRASHNA_TOPIC_SKILLS, StringKeyPrashnaPart1.PRASHNA_TOPIC_EFFORTS, StringKeyPrashnaPart1.PRASHNA_TOPIC_RIGHT_EAR),
            karaka = Planet.MARS,
            bodyPartKey = StringKeyPrashnaPart2.PRASHNA_BODY_ARMS_SHOULDERS,
            directionKey = StringKeyPrashnaPart2.PRASHNA_DIR_WEST,
            colorKey = StringKeyPrashnaPart1.PRASHNA_COLOR_GREEN,
            element = Tattva.AIR
        ),
        4 to PrashnaHouseSignification(
            house = 4,
            nameKey = StringKeyPrashnaPart2.PRASHNA_HOUSE_4_NAME,
            primaryTopics = listOf(StringKeyPrashnaPart1.PRASHNA_TOPIC_HOME, StringKeyPrashnaPart1.PRASHNA_TOPIC_MOTHER, StringKeyPrashnaPart1.PRASHNA_TOPIC_PROPERTY, StringKeyPrashnaPart1.PRASHNA_TOPIC_VEHICLES),
            secondaryTopics = listOf(StringKeyPrashnaPart1.PRASHNA_TOPIC_EDUCATION, StringKeyPrashnaPart1.PRASHNA_TOPIC_HAPPINESS, StringKeyPrashnaPart1.PRASHNA_TOPIC_TREASURE, StringKeyPrashnaPart1.PRASHNA_TOPIC_STOMACH),
            karaka = Planet.MOON,
            bodyPartKey = StringKeyPrashnaPart2.PRASHNA_BODY_CHEST,
            directionKey = StringKeyPrashnaPart2.PRASHNA_DIR_NORTH,
            colorKey = StringKeyPrashnaPart1.PRASHNA_COLOR_WHITE,
            element = Tattva.WATER
        ),
        5 to PrashnaHouseSignification(
            house = 5,
            nameKey = StringKeyPrashnaPart2.PRASHNA_HOUSE_5_NAME,
            primaryTopics = listOf(StringKeyPrashnaPart1.PRASHNA_TOPIC_CHILDREN, StringKeyPrashnaPart1.PRASHNA_TOPIC_INTELLIGENCE, StringKeyPrashnaPart1.PRASHNA_TOPIC_ROMANCE, StringKeyPrashnaPart1.PRASHNA_TOPIC_SPECULATION),
            secondaryTopics = listOf(StringKeyPrashnaPart1.PRASHNA_TOPIC_EDUCATION, StringKeyPrashnaPart1.PRASHNA_TOPIC_MANTRAS, StringKeyPrashnaPart1.PRASHNA_TOPIC_PAST_MERIT, StringKeyPrashnaPart1.PRASHNA_TOPIC_STOMACH),
            karaka = Planet.JUPITER,
            bodyPartKey = StringKeyPrashnaPart2.PRASHNA_BODY_UPPER_ABDOMEN,
            directionKey = StringKeyPrashnaPart2.PRASHNA_DIR_EAST_NORTH,
            colorKey = StringKeyPrashnaPart1.PRASHNA_COLOR_YELLOW,
            element = Tattva.FIRE
        ),
        6 to PrashnaHouseSignification(
            house = 6,
            nameKey = StringKeyPrashnaPart2.PRASHNA_HOUSE_6_NAME,
            primaryTopics = listOf(StringKeyPrashnaPart1.PRASHNA_TOPIC_ENEMIES, StringKeyPrashnaPart1.PRASHNA_TOPIC_DISEASE, StringKeyPrashnaPart1.PRASHNA_TOPIC_OBSTACLES, StringKeyPrashnaPart1.PRASHNA_TOPIC_SERVICE),
            secondaryTopics = listOf(StringKeyPrashnaPart1.PRASHNA_TOPIC_DEBTS, StringKeyPrashnaPart1.PRASHNA_TOPIC_COMPETITORS, StringKeyPrashnaPart1.PRASHNA_TOPIC_UNCLE, StringKeyPrashnaPart1.PRASHNA_TOPIC_INTESTINES),
            karaka = Planet.MARS,
            bodyPartKey = StringKeyPrashnaPart2.PRASHNA_BODY_LOWER_ABDOMEN,
            directionKey = StringKeyPrashnaPart2.PRASHNA_DIR_SOUTH,
            colorKey = StringKeyPrashnaPart1.PRASHNA_COLOR_GREY_BLUE,
            element = Tattva.EARTH
        ),
        7 to PrashnaHouseSignification(
            house = 7,
            nameKey = StringKeyPrashnaPart2.PRASHNA_HOUSE_7_NAME,
            primaryTopics = listOf(StringKeyPrashnaPart1.PRASHNA_TOPIC_MARRIAGE, StringKeyPrashnaPart1.PRASHNA_TOPIC_PARTNERSHIP, StringKeyPrashnaPart1.PRASHNA_TOPIC_DEALS, StringKeyPrashnaPart1.PRASHNA_TOPIC_OPPONENT),
            secondaryTopics = listOf(StringKeyPrashnaPart1.PRASHNA_TOPIC_FOREIGN_TRAVEL, StringKeyPrashnaPart1.PRASHNA_TOPIC_DEATH, StringKeyPrashnaPart1.PRASHNA_TOPIC_SEXUAL, StringKeyPrashnaPart1.PRASHNA_TOPIC_KIDNEYS),
            karaka = Planet.VENUS,
            bodyPartKey = StringKeyPrashnaPart2.PRASHNA_BODY_BELOW_NAVEL,
            directionKey = StringKeyPrashnaPart2.PRASHNA_DIR_WEST,
            colorKey = StringKeyPrashnaPart1.PRASHNA_COLOR_MULTI,
            element = Tattva.AIR
        ),
        8 to PrashnaHouseSignification(
            house = 8,
            nameKey = StringKeyPrashnaPart2.PRASHNA_HOUSE_8_NAME,
            primaryTopics = listOf(StringKeyPrashnaPart1.PRASHNA_TOPIC_DEATH, StringKeyPrashnaPart1.PRASHNA_TOPIC_HIDDEN, StringKeyPrashnaPart1.PRASHNA_TOPIC_INHERITANCE, StringKeyPrashnaPart1.PRASHNA_TOPIC_TRANSFORMATION),
            secondaryTopics = listOf(StringKeyPrashnaPart1.PRASHNA_TOPIC_CHRONIC, StringKeyPrashnaPart1.PRASHNA_TOPIC_OCCULT, StringKeyPrashnaPart1.PRASHNA_TOPIC_MYSTERY, StringKeyPrashnaPart1.PRASHNA_TOPIC_REPRODUCTIVE),
            karaka = Planet.SATURN,
            bodyPartKey = StringKeyPrashnaPart2.PRASHNA_BODY_PRIVATE_PARTS,
            directionKey = StringKeyPrashnaPart2.PRASHNA_DIR_SOUTH_WEST,
            colorKey = StringKeyPrashnaPart1.PRASHNA_COLOR_BLACK,
            element = Tattva.WATER
        ),
        9 to PrashnaHouseSignification(
            house = 9,
            nameKey = StringKeyPrashnaPart2.PRASHNA_HOUSE_9_NAME,
            primaryTopics = listOf(StringKeyPrashnaPart1.PRASHNA_TOPIC_LUCK, StringKeyPrashnaPart1.PRASHNA_TOPIC_FATHER, StringKeyPrashnaPart1.PRASHNA_TOPIC_LONG_TRAVEL, StringKeyPrashnaPart1.PRASHNA_TOPIC_HIGHER_LEARNING),
            secondaryTopics = listOf(StringKeyPrashnaPart1.PRASHNA_TOPIC_RELIGION, StringKeyPrashnaPart1.PRASHNA_TOPIC_GURU, StringKeyPrashnaPart1.PRASHNA_TOPIC_FORTUNE, StringKeyPrashnaPart1.PRASHNA_TOPIC_THIGHS),
            karaka = Planet.JUPITER,
            bodyPartKey = StringKeyPrashnaPart2.PRASHNA_BODY_THIGHS,
            directionKey = StringKeyPrashnaPart2.PRASHNA_DIR_EAST,
            colorKey = StringKeyPrashnaPart1.PRASHNA_COLOR_GOLDEN,
            element = Tattva.FIRE
        ),
        10 to PrashnaHouseSignification(
            house = 10,
            nameKey = StringKeyPrashnaPart2.PRASHNA_HOUSE_10_NAME,
            primaryTopics = listOf(StringKeyPrashnaPart1.PRASHNA_TOPIC_CAREER, StringKeyPrashnaPart1.PRASHNA_TOPIC_STATUS, StringKeyPrashnaPart1.PRASHNA_TOPIC_AUTHORITY, StringKeyPrashnaPart1.PRASHNA_TOPIC_GOVERNMENT),
            secondaryTopics = listOf(StringKeyPrashnaPart1.PRASHNA_TOPIC_FATHER, StringKeyPrashnaPart1.PRASHNA_TOPIC_PUBLIC_LIFE, StringKeyPrashnaPart1.PRASHNA_TOPIC_ACHIEVEMENT, StringKeyPrashnaPart1.PRASHNA_TOPIC_KNEES),
            karaka = Planet.SUN,
            bodyPartKey = StringKeyPrashnaPart2.PRASHNA_BODY_KNEES,
            directionKey = StringKeyPrashnaPart2.PRASHNA_DIR_SOUTH,
            colorKey = StringKeyPrashnaPart1.PRASHNA_COLOR_WHITE,
            element = Tattva.EARTH
        ),
        11 to PrashnaHouseSignification(
            house = 11,
            nameKey = StringKeyPrashnaPart2.PRASHNA_HOUSE_11_NAME,
            primaryTopics = listOf(StringKeyPrashnaPart1.PRASHNA_TOPIC_GAINS, StringKeyPrashnaPart1.PRASHNA_TOPIC_FULFILLMENT, StringKeyPrashnaPart1.PRASHNA_TOPIC_ELDER_SIBLINGS, StringKeyPrashnaPart1.PRASHNA_TOPIC_FRIENDS),
            secondaryTopics = listOf(StringKeyPrashnaPart1.PRASHNA_TOPIC_INCOME, StringKeyPrashnaPart1.PRASHNA_TOPIC_WISHES, StringKeyPrashnaPart1.PRASHNA_TOPIC_RECOVERY, StringKeyPrashnaPart1.PRASHNA_TOPIC_ANKLES),
            karaka = Planet.JUPITER,
            bodyPartKey = StringKeyPrashnaPart2.PRASHNA_BODY_CALVES_ANKLES,
            directionKey = StringKeyPrashnaPart2.PRASHNA_DIR_NORTH_WEST,
            colorKey = StringKeyPrashnaPart1.PRASHNA_COLOR_GOLDEN_YELLOW,
            element = Tattva.AIR
        ),
        12 to PrashnaHouseSignification(
            house = 12,
            nameKey = StringKeyPrashnaPart2.PRASHNA_HOUSE_12_NAME,
            primaryTopics = listOf(StringKeyPrashnaPart1.PRASHNA_TOPIC_LOSSES, StringKeyPrashnaPart1.PRASHNA_TOPIC_EXPENSES, StringKeyPrashnaPart1.PRASHNA_TOPIC_LIBERATION, StringKeyPrashnaPart1.PRASHNA_TOPIC_FOREIGN_LANDS),
            secondaryTopics = listOf(StringKeyPrashnaPart1.PRASHNA_TOPIC_BED_PLEASURES, StringKeyPrashnaPart1.PRASHNA_TOPIC_SLEEP, StringKeyPrashnaPart1.PRASHNA_TOPIC_HOSPITAL, StringKeyPrashnaPart1.PRASHNA_TOPIC_FEET),
            karaka = Planet.SATURN,
            bodyPartKey = StringKeyPrashnaPart2.PRASHNA_BODY_FEET,
            directionKey = StringKeyPrashnaPart2.PRASHNA_DIR_NORTH_EAST,
            colorKey = StringKeyPrashnaPart1.PRASHNA_COLOR_BROWN,
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
