package com.astro.storm.ephemeris

import com.astro.storm.data.model.Nakshatra
import com.astro.storm.data.model.Planet
import com.astro.storm.data.model.ZodiacSign
import java.math.BigDecimal

/**
 * Centralized Astrological Constants for AstroStorm
 *
 * This object is the SINGLE SOURCE OF TRUTH for all astrological constants used throughout
 * the application. All calculators and utilities should reference these constants rather than
 * defining their own copies.
 *
 * Organization:
 * 1. Fundamental Zodiac Constants (degrees, signs, nakshatras)
 * 2. Shadbala Constants (planetary strength calculations)
 * 3. Aspect Constants (planetary aspects and orbs)
 * 4. House Classifications (Kendra, Trikona, Dusthana, etc.)
 * 5. Planet Classifications (benefics, malefics, shadow planets)
 * 6. Planetary Dignities (exaltation, debilitation, own sign, moolatrikona)
 * 7. Planetary Relationships (natural friends and enemies)
 * 8. Dasha System Constants (Vimsottari, Yogini, Ashtottari)
 * 9. Hora and Muhurta Constants
 * 10. Combustion Thresholds
 * 11. Nakshatra Lordships
 * 12. Dig Bala (Directional Strength)
 * 13. Utility Functions
 *
 * Classical References:
 * - Brihat Parashara Hora Shastra (BPHS) - Primary reference
 * - Phaladeepika by Mantreswara
 * - Jataka Parijata by Vaidyanatha Dikshita
 * - Saravali by Kalyana Varma
 * - Brihat Jataka by Varahamihira
 *
 * @author AstroStorm - Ultra-Precision Vedic Astrology
 */
object AstrologicalConstants {

    // ============================================
    // FUNDAMENTAL ZODIAC CONSTANTS
    // ============================================

    /** Degrees in one zodiac sign */
    const val DEGREES_PER_SIGN = 30.0

    /** Total degrees in the zodiac circle */
    const val DEGREES_PER_CIRCLE = 360.0

    /** Total number of zodiac signs */
    const val TOTAL_SIGNS = 12

    /** Total number of nakshatras */
    const val TOTAL_NAKSHATRAS = 27

    /** Degrees per nakshatra (360/27 = 13.333...) */
    const val DEGREES_PER_NAKSHATRA = 13.333333333333334

    /** Degrees per nakshatra pada (13.333.../4 = 3.333...) */
    const val DEGREES_PER_PADA = 3.333333333333333

    /** Number of padas per nakshatra */
    const val PADAS_PER_NAKSHATRA = 4

    /** Total padas in zodiac */
    const val TOTAL_PADAS = 108

    // ============================================
    // SHADBALA CONSTANTS
    // ============================================

    /** Virupas per Rupa (60 Virupas = 1 Rupa) */
    const val VIRUPAS_PER_RUPA = 60.0

    /** Maximum Shadbala virupas (considered very strong) */
    const val MAX_SHADBALA_VIRUPAS = 600.0

    /** Minimum required Shadbala for planets (standard) */
    val MINIMUM_REQUIRED_SHADBALA = mapOf(
        Planet.SUN to 390.0,
        Planet.MOON to 360.0,
        Planet.MARS to 300.0,
        Planet.MERCURY to 420.0,
        Planet.JUPITER to 390.0,
        Planet.VENUS to 330.0,
        Planet.SATURN to 300.0
    )

    // ============================================
    // ASPECT CONSTANTS
    // ============================================

    /** Standard aspect (7th house) strength */
    const val FULL_ASPECT_STRENGTH = 1.0

    /** Three-quarter aspect strength */
    const val THREE_QUARTER_ASPECT = 0.75

    /** Half aspect strength */
    const val HALF_ASPECT = 0.5

    /** Quarter aspect strength */
    const val QUARTER_ASPECT = 0.25

    /** Orb for conjunctions (degrees) */
    const val CONJUNCTION_ORB = 10.0

    /** Orb for standard aspects (degrees) */
    const val STANDARD_ASPECT_ORB = 12.0

    /**
     * Special aspects for Mars, Jupiter, and Saturn as per BPHS
     * - Mars: Casts 3/4 aspect on 4th and 8th houses
     * - Jupiter: Casts full aspect on 5th and 9th houses
     * - Saturn: Casts 3/4 aspect on 3rd and 10th houses
     * All planets cast full aspect on 7th house
     */
    val SPECIAL_ASPECTS = mapOf(
        Planet.MARS to mapOf(4 to THREE_QUARTER_ASPECT, 8 to THREE_QUARTER_ASPECT),
        Planet.JUPITER to mapOf(5 to FULL_ASPECT_STRENGTH, 9 to FULL_ASPECT_STRENGTH),
        Planet.SATURN to mapOf(3 to THREE_QUARTER_ASPECT, 10 to THREE_QUARTER_ASPECT)
    )

    // ============================================
    // HOUSE CLASSIFICATIONS
    // ============================================

    /** Kendra houses (Angular/Quadrant) - 1, 4, 7, 10 */
    val KENDRA_HOUSES = setOf(1, 4, 7, 10)

    /** Trikona houses (Trine) - 1, 5, 9 */
    val TRIKONA_HOUSES = setOf(1, 5, 9)

    /** Dusthana houses (Malefic/Difficult) - 6, 8, 12 */
    val DUSTHANA_HOUSES = setOf(6, 8, 12)

    /** Upachaya houses (Growth) - 3, 6, 10, 11 */
    val UPACHAYA_HOUSES = setOf(3, 6, 10, 11)

    /** Panapara houses (Succedent) - 2, 5, 8, 11 */
    val PANAPARA_HOUSES = setOf(2, 5, 8, 11)

    /** Apoklima houses (Cadent) - 3, 6, 9, 12 */
    val APOKLIMA_HOUSES = setOf(3, 6, 9, 12)

    /** Maraka houses (Death-inflicting) - 2, 7 */
    val MARAKA_HOUSES = setOf(2, 7)

    // ============================================
    // PLANET CLASSIFICATIONS
    // ============================================

    /** Natural benefic planets */
    val NATURAL_BENEFICS = setOf(Planet.JUPITER, Planet.VENUS, Planet.MOON, Planet.MERCURY)

    /** Natural malefic planets */
    val NATURAL_MALEFICS = setOf(Planet.SUN, Planet.MARS, Planet.SATURN, Planet.RAHU, Planet.KETU)

    /** Graha (planets) that rule signs */
    val SIGN_RULING_PLANETS = setOf(
        Planet.SUN, Planet.MOON, Planet.MARS, Planet.MERCURY,
        Planet.JUPITER, Planet.VENUS, Planet.SATURN
    )

    /** Shadow planets (Chaya Grahas) */
    val SHADOW_PLANETS = setOf(Planet.RAHU, Planet.KETU)

    /** Outer planets (modern, not classical Vedic) */
    val OUTER_PLANETS = setOf(Planet.URANUS, Planet.NEPTUNE, Planet.PLUTO)

    // ============================================
    // PLANETARY DIGNITIES
    // ============================================

    /**
     * Exaltation signs for each planet
     * Reference: BPHS Chapter 3
     */
    val EXALTATION_SIGNS = mapOf(
        Planet.SUN to ZodiacSign.ARIES,
        Planet.MOON to ZodiacSign.TAURUS,
        Planet.MARS to ZodiacSign.CAPRICORN,
        Planet.MERCURY to ZodiacSign.VIRGO,
        Planet.JUPITER to ZodiacSign.CANCER,
        Planet.VENUS to ZodiacSign.PISCES,
        Planet.SATURN to ZodiacSign.LIBRA,
        Planet.RAHU to ZodiacSign.TAURUS,   // Some traditions
        Planet.KETU to ZodiacSign.SCORPIO   // Some traditions
    )

    /**
     * Exaltation degrees for each planet (exact point of maximum exaltation)
     * Reference: BPHS Chapter 3, Verse 49
     */
    val EXALTATION_DEGREES = mapOf(
        Planet.SUN to 10.0,      // 10° Aries
        Planet.MOON to 3.0,     // 3° Taurus
        Planet.MARS to 28.0,    // 28° Capricorn
        Planet.MERCURY to 15.0, // 15° Virgo
        Planet.JUPITER to 5.0,  // 5° Cancer
        Planet.VENUS to 27.0,   // 27° Pisces
        Planet.SATURN to 20.0   // 20° Libra
    )

    /**
     * Debilitation signs for each planet (opposite to exaltation)
     * Reference: BPHS Chapter 3
     */
    val DEBILITATION_SIGNS = mapOf(
        Planet.SUN to ZodiacSign.LIBRA,
        Planet.MOON to ZodiacSign.SCORPIO,
        Planet.MARS to ZodiacSign.CANCER,
        Planet.MERCURY to ZodiacSign.PISCES,
        Planet.JUPITER to ZodiacSign.CAPRICORN,
        Planet.VENUS to ZodiacSign.VIRGO,
        Planet.SATURN to ZodiacSign.ARIES,
        Planet.RAHU to ZodiacSign.SCORPIO,  // Some traditions
        Planet.KETU to ZodiacSign.TAURUS    // Some traditions
    )

    /**
     * Debilitation degrees (opposite to exaltation degree)
     */
    val DEBILITATION_DEGREES = mapOf(
        Planet.SUN to 10.0,     // 10° Libra
        Planet.MOON to 3.0,    // 3° Scorpio
        Planet.MARS to 28.0,   // 28° Cancer
        Planet.MERCURY to 15.0,// 15° Pisces
        Planet.JUPITER to 5.0, // 5° Capricorn
        Planet.VENUS to 27.0,  // 27° Virgo
        Planet.SATURN to 20.0  // 20° Aries
    )

    /**
     * Own signs for each planet
     * Reference: BPHS Chapter 3
     */
    val OWN_SIGNS = mapOf(
        Planet.SUN to setOf(ZodiacSign.LEO),
        Planet.MOON to setOf(ZodiacSign.CANCER),
        Planet.MARS to setOf(ZodiacSign.ARIES, ZodiacSign.SCORPIO),
        Planet.MERCURY to setOf(ZodiacSign.GEMINI, ZodiacSign.VIRGO),
        Planet.JUPITER to setOf(ZodiacSign.SAGITTARIUS, ZodiacSign.PISCES),
        Planet.VENUS to setOf(ZodiacSign.TAURUS, ZodiacSign.LIBRA),
        Planet.SATURN to setOf(ZodiacSign.CAPRICORN, ZodiacSign.AQUARIUS)
    )

    /**
     * Moolatrikona signs and degree ranges
     * Moolatrikona is stronger than own sign but weaker than exaltation
     * Reference: BPHS Chapter 3
     */
    data class MoolatrikonaRange(
        val sign: ZodiacSign,
        val startDegree: Double,
        val endDegree: Double
    )

    val MOOLATRIKONA_RANGES = mapOf(
        Planet.SUN to MoolatrikonaRange(ZodiacSign.LEO, 0.0, 20.0),
        Planet.MOON to MoolatrikonaRange(ZodiacSign.TAURUS, 4.0, 30.0),
        Planet.MARS to MoolatrikonaRange(ZodiacSign.ARIES, 0.0, 12.0),
        Planet.MERCURY to MoolatrikonaRange(ZodiacSign.VIRGO, 16.0, 20.0),
        Planet.JUPITER to MoolatrikonaRange(ZodiacSign.SAGITTARIUS, 0.0, 10.0),
        Planet.VENUS to MoolatrikonaRange(ZodiacSign.LIBRA, 0.0, 15.0),
        Planet.SATURN to MoolatrikonaRange(ZodiacSign.AQUARIUS, 0.0, 20.0)
    )

    // ============================================
    // PLANETARY RELATIONSHIPS (PERMANENT)
    // ============================================

    /**
     * Natural friends for each planet
     * Reference: BPHS Chapter 3
     */
    val NATURAL_FRIENDS = mapOf(
        Planet.SUN to setOf(Planet.MOON, Planet.MARS, Planet.JUPITER),
        Planet.MOON to setOf(Planet.SUN, Planet.MERCURY),
        Planet.MARS to setOf(Planet.SUN, Planet.MOON, Planet.JUPITER),
        Planet.MERCURY to setOf(Planet.SUN, Planet.VENUS),
        Planet.JUPITER to setOf(Planet.SUN, Planet.MOON, Planet.MARS),
        Planet.VENUS to setOf(Planet.MERCURY, Planet.SATURN),
        Planet.SATURN to setOf(Planet.MERCURY, Planet.VENUS)
    )

    /**
     * Natural enemies for each planet
     * Reference: BPHS Chapter 3
     */
    val NATURAL_ENEMIES = mapOf(
        Planet.SUN to setOf(Planet.SATURN, Planet.VENUS),
        Planet.MOON to emptySet<Planet>(),  // Moon has no natural enemies
        Planet.MARS to setOf(Planet.MERCURY),
        Planet.MERCURY to setOf(Planet.MOON),
        Planet.JUPITER to setOf(Planet.MERCURY, Planet.VENUS),
        Planet.VENUS to setOf(Planet.SUN, Planet.MOON),
        Planet.SATURN to setOf(Planet.SUN, Planet.MOON, Planet.MARS)
    )

    // ============================================
    // VIMSOTTARI DASHA PERIODS
    // ============================================

    /**
     * Vimsottari Mahadasha periods in years
     * Total cycle: 120 years
     * Reference: BPHS Chapter 46
     */
    val VIMSOTTARI_PERIODS = mapOf(
        Planet.KETU to 7.0,
        Planet.VENUS to 20.0,
        Planet.SUN to 6.0,
        Planet.MOON to 10.0,
        Planet.MARS to 7.0,
        Planet.RAHU to 18.0,
        Planet.JUPITER to 16.0,
        Planet.SATURN to 19.0,
        Planet.MERCURY to 17.0
    )

    /** Total Vimsottari Dasha cycle in years */
    const val VIMSOTTARI_TOTAL_YEARS = 120.0

    /**
     * Vimsottari Dasha sequence
     * Starts from birth nakshatra ruler
     */
    val VIMSOTTARI_SEQUENCE = listOf(
        Planet.KETU, Planet.VENUS, Planet.SUN, Planet.MOON,
        Planet.MARS, Planet.RAHU, Planet.JUPITER, Planet.SATURN, Planet.MERCURY
    )

    // ============================================
    // HORA CONSTANTS
    // ============================================

    /**
     * Hora rulers for each day
     * Reference: Traditional Muhurta texts
     */
    val DAY_HORA_LORDS = mapOf(
        java.time.DayOfWeek.SUNDAY to Planet.SUN,
        java.time.DayOfWeek.MONDAY to Planet.MOON,
        java.time.DayOfWeek.TUESDAY to Planet.MARS,
        java.time.DayOfWeek.WEDNESDAY to Planet.MERCURY,
        java.time.DayOfWeek.THURSDAY to Planet.JUPITER,
        java.time.DayOfWeek.FRIDAY to Planet.VENUS,
        java.time.DayOfWeek.SATURDAY to Planet.SATURN
    )

    /** Hora sequence for day hours (starting from day lord) */
    val HORA_SEQUENCE = listOf(
        Planet.SUN, Planet.VENUS, Planet.MERCURY, Planet.MOON,
        Planet.SATURN, Planet.JUPITER, Planet.MARS
    )

    // ============================================
    // COMBUSTION THRESHOLDS
    // ============================================

    /**
     * Combustion degrees (distance from Sun below which planet is combust)
     * Reference: Phaladeepika, BPHS
     */
    val COMBUSTION_DEGREES = mapOf(
        Planet.MOON to 12.0,
        Planet.MARS to 17.0,
        Planet.MERCURY to 14.0,  // 12° when retrograde
        Planet.JUPITER to 11.0,
        Planet.VENUS to 10.0,    // 8° when retrograde
        Planet.SATURN to 15.0
    )

    /** Cazimi distance (heart of Sun - planet gains strength) */
    const val CAZIMI_DEGREE = 0.2833  // 17 arc minutes

    // ============================================
    // UTILITY FUNCTIONS
    // ============================================

    /**
     * Normalize an angle to 0-360 range
     */
    fun normalizeDegree(degree: Double): Double {
        var result = degree % DEGREES_PER_CIRCLE
        if (result < 0) result += DEGREES_PER_CIRCLE
        return result
    }

    /**
     * Calculate the degree within a sign (0-30)
     */
    fun getDegreeInSign(longitude: Double): Double {
        return normalizeDegree(longitude) % DEGREES_PER_SIGN
    }

    /**
     * Calculate the angular distance between two points (shortest path)
     */
    fun angularDistance(deg1: Double, deg2: Double): Double {
        val diff = kotlin.math.abs(normalizeDegree(deg1) - normalizeDegree(deg2))
        return kotlin.math.min(diff, DEGREES_PER_CIRCLE - diff)
    }

    /**
     * Check if a planet is in exaltation
     */
    fun isExalted(planet: Planet, sign: ZodiacSign): Boolean {
        return EXALTATION_SIGNS[planet] == sign
    }

    /**
     * Check if a planet is in debilitation
     */
    fun isDebilitated(planet: Planet, sign: ZodiacSign): Boolean {
        return DEBILITATION_SIGNS[planet] == sign
    }

    /**
     * Check if a planet is in its own sign
     */
    fun isInOwnSign(planet: Planet, sign: ZodiacSign): Boolean {
        return OWN_SIGNS[planet]?.contains(sign) == true
    }

    /**
     * Check if a planet is in moolatrikona
     */
    fun isInMoolatrikona(planet: Planet, sign: ZodiacSign, degreeInSign: Double): Boolean {
        val range = MOOLATRIKONA_RANGES[planet] ?: return false
        return sign == range.sign && degreeInSign >= range.startDegree && degreeInSign <= range.endDegree
    }

    /**
     * Check if a planet is a natural benefic
     */
    fun isNaturalBenefic(planet: Planet): Boolean {
        return planet in NATURAL_BENEFICS
    }

    /**
     * Check if a planet is a natural malefic
     */
    fun isNaturalMalefic(planet: Planet): Boolean {
        return planet in NATURAL_MALEFICS
    }

    /**
     * Check if a house is a Kendra
     */
    fun isKendra(house: Int): Boolean {
        return house in KENDRA_HOUSES
    }

    /**
     * Check if a house is a Trikona
     */
    fun isTrikona(house: Int): Boolean {
        return house in TRIKONA_HOUSES
    }

    /**
     * Check if a house is a Dusthana
     */
    fun isDusthana(house: Int): Boolean {
        return house in DUSTHANA_HOUSES
    }

    /**
     * Get special aspect strength for a planet aspecting a house
     * Returns the strength factor (0.25, 0.5, 0.75, or 1.0)
     */
    fun getSpecialAspectStrength(planet: Planet, aspectHouse: Int): Double {
        // All planets have full aspect on 7th house
        if (aspectHouse == 7) return FULL_ASPECT_STRENGTH

        // Check special aspects
        return SPECIAL_ASPECTS[planet]?.get(aspectHouse) ?: 0.0
    }

    /**
     * Get all houses a planet aspects with their strengths
     * Returns map of house number to aspect strength
     */
    fun getAllAspects(planet: Planet): Map<Int, Double> {
        val aspects = mutableMapOf<Int, Double>()

        // Full aspect on 7th for all planets
        aspects[7] = FULL_ASPECT_STRENGTH

        // Add special aspects if any
        SPECIAL_ASPECTS[planet]?.let { specialAspects ->
            aspects.putAll(specialAspects)
        }

        return aspects
    }

    /**
     * Get Vimsottari Dasha period for a planet
     */
    fun getVimsottariPeriod(planet: Planet): Double {
        return VIMSOTTARI_PERIODS[planet] ?: 0.0
    }

    /**
     * Check if a planet is combust (too close to Sun)
     */
    fun isCombust(planet: Planet, distanceFromSun: Double): Boolean {
        if (planet == Planet.SUN) return false // Sun cannot be combust
        val threshold = COMBUSTION_DEGREES[planet] ?: return false
        return distanceFromSun < threshold
    }

    /**
     * Check if a planet is in Cazimi (heart of Sun - empowered)
     */
    fun isCazimi(distanceFromSun: Double): Boolean {
        return distanceFromSun <= CAZIMI_DEGREE
    }

    // ============================================
    // DASHA SYSTEM CONSTANTS - HIGH PRECISION
    // ============================================

    /**
     * Vimsottari Dasha periods as BigDecimal for precise calculations
     * Using BigDecimal prevents floating-point errors in dasha boundary calculations
     * Reference: BPHS Chapter 46
     */
    val VIMSOTTARI_PERIODS_PRECISE: Map<Planet, BigDecimal> = mapOf(
        Planet.KETU to BigDecimal("7"),
        Planet.VENUS to BigDecimal("20"),
        Planet.SUN to BigDecimal("6"),
        Planet.MOON to BigDecimal("10"),
        Planet.MARS to BigDecimal("7"),
        Planet.RAHU to BigDecimal("18"),
        Planet.JUPITER to BigDecimal("16"),
        Planet.SATURN to BigDecimal("19"),
        Planet.MERCURY to BigDecimal("17")
    )

    /** Total Vimsottari cycle as BigDecimal */
    val VIMSOTTARI_TOTAL_YEARS_PRECISE: BigDecimal = BigDecimal("120")

    /** Days per year for dasha calculations (accounting for leap years) */
    val DAYS_PER_YEAR_PRECISE: BigDecimal = BigDecimal("365.25")

    /** Nakshatra span in degrees (360/27) as BigDecimal */
    val NAKSHATRA_SPAN_PRECISE: BigDecimal = BigDecimal("13.333333333333333333")

    // ============================================
    // NAKSHATRA LORDS (FOR DASHA)
    // ============================================

    /**
     * Nakshatra lords for Vimsottari Dasha calculation
     * Each nakshatra is ruled by one of the 9 Vimsottari planets
     * The sequence repeats every 9 nakshatras
     * Reference: BPHS Chapter 46
     */
    val NAKSHATRA_LORDS: Map<Nakshatra, Planet> = mapOf(
        Nakshatra.ASHWINI to Planet.KETU,
        Nakshatra.BHARANI to Planet.VENUS,
        Nakshatra.KRITTIKA to Planet.SUN,
        Nakshatra.ROHINI to Planet.MOON,
        Nakshatra.MRIGASHIRA to Planet.MARS,
        Nakshatra.ARDRA to Planet.RAHU,
        Nakshatra.PUNARVASU to Planet.JUPITER,
        Nakshatra.PUSHYA to Planet.SATURN,
        Nakshatra.ASHLESHA to Planet.MERCURY,
        Nakshatra.MAGHA to Planet.KETU,
        Nakshatra.PURVA_PHALGUNI to Planet.VENUS,
        Nakshatra.UTTARA_PHALGUNI to Planet.SUN,
        Nakshatra.HASTA to Planet.MOON,
        Nakshatra.CHITRA to Planet.MARS,
        Nakshatra.SWATI to Planet.RAHU,
        Nakshatra.VISHAKHA to Planet.JUPITER,
        Nakshatra.ANURADHA to Planet.SATURN,
        Nakshatra.JYESHTHA to Planet.MERCURY,
        Nakshatra.MULA to Planet.KETU,
        Nakshatra.PURVA_ASHADHA to Planet.VENUS,
        Nakshatra.UTTARA_ASHADHA to Planet.SUN,
        Nakshatra.SHRAVANA to Planet.MOON,
        Nakshatra.DHANISHTHA to Planet.MARS,
        Nakshatra.SHATABHISHA to Planet.RAHU,
        Nakshatra.PURVA_BHADRAPADA to Planet.JUPITER,
        Nakshatra.UTTARA_BHADRAPADA to Planet.SATURN,
        Nakshatra.REVATI to Planet.MERCURY
    )

    /**
     * Get nakshatra lord for Vimsottari dasha
     */
    fun getNakshatraLord(nakshatra: Nakshatra): Planet {
        return NAKSHATRA_LORDS[nakshatra] ?: Planet.KETU
    }

    // ============================================
    // DIG BALA (DIRECTIONAL STRENGTH)
    // ============================================

    /**
     * Houses where planets have maximum Dig Bala (directional strength)
     * Reference: BPHS Chapter 27
     *
     * Sun & Mars: Strong in 10th house (Midheaven/South)
     * Jupiter & Mercury: Strong in 1st house (Ascendant/East)
     * Moon & Venus: Strong in 4th house (IC/North)
     * Saturn: Strong in 7th house (Descendant/West)
     */
    val DIG_BALA_HOUSES: Map<Planet, Int> = mapOf(
        Planet.SUN to 10,
        Planet.MARS to 10,
        Planet.JUPITER to 1,
        Planet.MERCURY to 1,
        Planet.MOON to 4,
        Planet.VENUS to 4,
        Planet.SATURN to 7
    )

    /**
     * Check if a planet has Dig Bala in the given house
     */
    fun hasDigBala(planet: Planet, house: Int): Boolean {
        return DIG_BALA_HOUSES[planet] == house
    }

    // ============================================
    // ADDITIONAL HOUSE CLASSIFICATIONS
    // ============================================

    /** Dharma houses - Related to righteousness and purpose: 1, 5, 9 */
    val DHARMA_HOUSES = setOf(1, 5, 9)

    /** Artha houses - Related to wealth and resources: 2, 6, 10 */
    val ARTHA_HOUSES = setOf(2, 6, 10)

    /** Kama houses - Related to desires and relationships: 3, 7, 11 */
    val KAMA_HOUSES = setOf(3, 7, 11)

    /** Moksha houses - Related to liberation and spirituality: 4, 8, 12 */
    val MOKSHA_HOUSES = setOf(4, 8, 12)

    // ============================================
    // YOGINI DASHA CONSTANTS
    // ============================================

    /**
     * Yogini Dasha periods in years
     * Total cycle: 36 years
     * Reference: Traditional Yogini Dasha texts
     */
    val YOGINI_DASHA_PERIODS: Map<String, Double> = mapOf(
        "Mangala" to 1.0,
        "Pingala" to 2.0,
        "Dhanya" to 3.0,
        "Bhramari" to 4.0,
        "Bhadrika" to 5.0,
        "Ulka" to 6.0,
        "Siddha" to 7.0,
        "Sankata" to 8.0
    )

    /** Yogini Dasha sequence */
    val YOGINI_SEQUENCE = listOf(
        "Mangala", "Pingala", "Dhanya", "Bhramari",
        "Bhadrika", "Ulka", "Siddha", "Sankata"
    )

    /** Total Yogini Dasha cycle in years */
    const val YOGINI_TOTAL_YEARS = 36.0

    /**
     * Yogini to Planet mapping
     * Each Yogini is associated with a planet
     */
    val YOGINI_PLANETS: Map<String, Planet> = mapOf(
        "Mangala" to Planet.MOON,
        "Pingala" to Planet.SUN,
        "Dhanya" to Planet.JUPITER,
        "Bhramari" to Planet.MARS,
        "Bhadrika" to Planet.MERCURY,
        "Ulka" to Planet.SATURN,
        "Siddha" to Planet.VENUS,
        "Sankata" to Planet.RAHU
    )

    // ============================================
    // ASHTOTTARI DASHA CONSTANTS
    // ============================================

    /**
     * Ashtottari Dasha periods in years
     * Total cycle: 108 years
     * Reference: Traditional Ashtottari texts
     */
    val ASHTOTTARI_DASHA_PERIODS: Map<Planet, Double> = mapOf(
        Planet.SUN to 6.0,
        Planet.MOON to 15.0,
        Planet.MARS to 8.0,
        Planet.MERCURY to 17.0,
        Planet.SATURN to 10.0,
        Planet.JUPITER to 19.0,
        Planet.RAHU to 12.0,
        Planet.VENUS to 21.0
    )

    /** Ashtottari sequence (8 planets, excludes Ketu) */
    val ASHTOTTARI_SEQUENCE = listOf(
        Planet.SUN, Planet.MOON, Planet.MARS, Planet.MERCURY,
        Planet.SATURN, Planet.JUPITER, Planet.RAHU, Planet.VENUS
    )

    /** Total Ashtottari cycle in years */
    const val ASHTOTTARI_TOTAL_YEARS = 108.0

    // ============================================
    // SPECIAL DEGREES
    // ============================================

    /**
     * Pushkara Navamsa degrees - Highly auspicious degrees
     * Planets in these degrees gain special strength
     * Reference: Classical Jyotish texts
     */
    val PUSHKARA_DEGREES: Set<Double> = setOf(
        21.0, 24.0, // Aries, Scorpio
        14.0, 17.0, // Taurus, Libra
        7.0, 10.0,  // Gemini, Virgo
        0.0, 3.0,   // Cancer, Aquarius
        21.0, 24.0, // Leo, Capricorn
        14.0, 17.0, // Sagittarius, Pisces
    )

    /**
     * Gandanta degrees - Junction points between water and fire signs
     * These are spiritually significant but materially challenging
     * Reference: BPHS
     */
    val GANDANTA_RANGES: List<Pair<Double, Double>> = listOf(
        Pair(356.667, 360.0),   // End of Pisces (Revati)
        Pair(0.0, 3.333),       // Start of Aries (Ashwini)
        Pair(116.667, 120.0),   // End of Cancer (Ashlesha)
        Pair(120.0, 123.333),   // Start of Leo (Magha)
        Pair(236.667, 240.0),   // End of Scorpio (Jyeshtha)
        Pair(240.0, 243.333)    // Start of Sagittarius (Mula)
    )

    /**
     * Check if a longitude falls in Gandanta
     */
    fun isInGandanta(longitude: Double): Boolean {
        val normalized = normalizeDegree(longitude)
        return GANDANTA_RANGES.any { (start, end) ->
            normalized >= start && normalized <= end
        }
    }

    // ============================================
    // RAHU/KETU SPECIFIC
    // ============================================

    /**
     * Rahu exaltation - varies by tradition
     * Using Gemini as per some Parashari texts
     * Taurus is used in other traditions
     */
    val RAHU_EXALTATION_TRADITIONS: Map<String, ZodiacSign> = mapOf(
        "Parashari" to ZodiacSign.GEMINI,
        "Jaimini" to ZodiacSign.TAURUS,
        "Modern" to ZodiacSign.GEMINI
    )

    /**
     * Get next planet in Vimsottari sequence
     */
    fun getNextVimsottariPlanet(current: Planet): Planet {
        val index = VIMSOTTARI_SEQUENCE.indexOf(current)
        return if (index >= 0) {
            VIMSOTTARI_SEQUENCE[(index + 1) % VIMSOTTARI_SEQUENCE.size]
        } else {
            VIMSOTTARI_SEQUENCE[0]
        }
    }

    /**
     * Get the index of a planet in Vimsottari sequence
     */
    fun getVimsottariIndex(planet: Planet): Int {
        return VIMSOTTARI_SEQUENCE.indexOf(planet).let { if (it < 0) 0 else it }
    }

    /**
     * Get precise Vimsottari period for a planet
     */
    fun getVimsottariPeriodPrecise(planet: Planet): BigDecimal {
        return VIMSOTTARI_PERIODS_PRECISE[planet] ?: BigDecimal.ZERO
    }
}
