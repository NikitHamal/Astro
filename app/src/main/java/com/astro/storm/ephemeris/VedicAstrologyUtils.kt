package com.astro.storm.ephemeris

import com.astro.storm.data.localization.Language
import com.astro.storm.data.localization.StringKey
import com.astro.storm.data.localization.StringKeyMatch
import com.astro.storm.data.localization.StringResources
import com.astro.storm.data.model.Nakshatra
import com.astro.storm.data.model.Planet
import com.astro.storm.data.model.PlanetPosition
import com.astro.storm.data.model.VedicChart
import com.astro.storm.data.model.ZodiacSign
import kotlin.math.abs

/**
 * Utility object for common Vedic astrology calculations.
 *
 * This module provides reusable functions for:
 * - Planetary dignity (exaltation, debilitation, own sign, Moolatrikona)
 * - Planetary relationships and friendships
 * - House calculations (Kendra, Trikona, Dusthana, etc.)
 * - Nakshatra attributes (Gana, Yoni, Nadi, Varna, etc.)
 * - Combustion and affliction calculations
 * - Aspect calculations
 *
 * NOTE: This utility class delegates to AstrologicalConstants for all fundamental
 * constant values. Any new constants should be added to AstrologicalConstants.kt,
 * not here. This class focuses on utility functions that operate on those constants.
 *
 * All calculations follow traditional Vedic (Parashari) astrology principles.
 *
 * @see AstrologicalConstants for all fundamental constants
 * @see MatchmakingCalculator for Kundali matching calculations
 * @see YogaCalculator for yoga detection
 */
object VedicAstrologyUtils {

    // ============================================================================
    // PLANETARY DIGNITY - USING CENTRALIZED CONSTANTS
    // ============================================================================

    // Type alias for readability
    typealias MoolatrikonaRange = AstrologicalConstants.MoolatrikonaRange

    /**
     * Extended exaltation signs including Rahu/Ketu variations
     * Uses AstrologicalConstants as base but extends with Rahu/Ketu using
     * the Gemini/Sagittarius tradition (per some Parashari texts)
     */
    private val extendedExaltationSigns: Map<Planet, ZodiacSign> by lazy {
        AstrologicalConstants.EXALTATION_SIGNS.toMutableMap().apply {
            // Override Rahu/Ketu with Gemini/Sagittarius tradition if not already set
            putIfAbsent(Planet.RAHU, ZodiacSign.GEMINI)
            putIfAbsent(Planet.KETU, ZodiacSign.SAGITTARIUS)
        }
    }

    /**
     * Extended debilitation signs including Rahu/Ketu
     */
    private val extendedDebilitationSigns: Map<Planet, ZodiacSign> by lazy {
        AstrologicalConstants.DEBILITATION_SIGNS.toMutableMap().apply {
            putIfAbsent(Planet.RAHU, ZodiacSign.SAGITTARIUS)
            putIfAbsent(Planet.KETU, ZodiacSign.GEMINI)
        }
    }

    /**
     * Extended own signs including Rahu/Ketu co-rulerships
     * Note: Rahu is considered co-ruler of Aquarius, Ketu of Scorpio
     */
    private val extendedOwnSigns: Map<Planet, List<ZodiacSign>> by lazy {
        val base = AstrologicalConstants.OWN_SIGNS.mapValues { it.value.toList() }.toMutableMap()
        base[Planet.RAHU] = listOf(ZodiacSign.AQUARIUS)
        base[Planet.KETU] = listOf(ZodiacSign.SCORPIO)
        base
    }

    /**
     * Check if a planet is exalted in its current sign.
     * Uses extended map that includes Rahu/Ketu.
     */
    fun isExalted(planet: Planet, sign: ZodiacSign): Boolean {
        return extendedExaltationSigns[planet] == sign
    }

    /**
     * Check if a planet position is exalted.
     */
    fun isExalted(pos: PlanetPosition): Boolean {
        return isExalted(pos.planet, pos.sign)
    }

    /**
     * Check if a planet is debilitated in its current sign.
     * Uses extended map that includes Rahu/Ketu.
     */
    fun isDebilitated(planet: Planet, sign: ZodiacSign): Boolean {
        return extendedDebilitationSigns[planet] == sign
    }

    /**
     * Check if a planet position is debilitated.
     */
    fun isDebilitated(pos: PlanetPosition): Boolean {
        return isDebilitated(pos.planet, pos.sign)
    }

    /**
     * Check if a planet is in its own sign (Swakshetra).
     * Uses extended map that includes Rahu/Ketu co-rulerships.
     */
    fun isInOwnSign(planet: Planet, sign: ZodiacSign): Boolean {
        return extendedOwnSigns[planet]?.contains(sign) == true || sign.ruler == planet
    }

    /**
     * Check if a planet position is in its own sign.
     */
    fun isInOwnSign(pos: PlanetPosition): Boolean {
        return isInOwnSign(pos.planet, pos.sign)
    }

    /**
     * Check if a planet is in its Moolatrikona sign and degree range.
     * Delegates to AstrologicalConstants.
     */
    fun isInMoolatrikona(planet: Planet, sign: ZodiacSign, degreeInSign: Double): Boolean {
        return AstrologicalConstants.isInMoolatrikona(planet, sign, degreeInSign)
    }

    /**
     * Check if a planet position is in Moolatrikona.
     */
    fun isInMoolatrikona(pos: PlanetPosition): Boolean {
        val degreeInSign = pos.longitude % 30.0
        return isInMoolatrikona(pos.planet, pos.sign, degreeInSign)
    }

    /**
     * Get the dignity status of a planet.
     */
    enum class PlanetaryDignity {
        EXALTED,           // Highest dignity
        MOOLATRIKONA,      // Second highest
        OWN_SIGN,          // Strong
        FRIEND_SIGN,       // Good
        NEUTRAL_SIGN,      // Neutral
        ENEMY_SIGN,        // Weak
        DEBILITATED        // Weakest
    }

    /**
     * Determine the complete dignity status of a planet position.
     */
    fun getDignity(pos: PlanetPosition): PlanetaryDignity {
        return when {
            isExalted(pos) -> PlanetaryDignity.EXALTED
            isDebilitated(pos) -> PlanetaryDignity.DEBILITATED
            isInMoolatrikona(pos) -> PlanetaryDignity.MOOLATRIKONA
            isInOwnSign(pos) -> PlanetaryDignity.OWN_SIGN
            isInFriendSign(pos) -> PlanetaryDignity.FRIEND_SIGN
            isInEnemySign(pos) -> PlanetaryDignity.ENEMY_SIGN
            else -> PlanetaryDignity.NEUTRAL_SIGN
        }
    }

    // ============================================================================
    // PLANETARY RELATIONSHIPS - FRIENDSHIPS
    // Using AstrologicalConstants with extended support for Rahu/Ketu
    // ============================================================================

    /**
     * Extended natural friendships including Rahu/Ketu
     * Based on Parashari system with traditional Rahu/Ketu relationships
     */
    private val extendedNaturalFriends: Map<Planet, Set<Planet>> by lazy {
        AstrologicalConstants.NATURAL_FRIENDS.toMutableMap().apply {
            // Add Rahu/Ketu relationships per traditional texts
            this[Planet.RAHU] = setOf(Planet.MERCURY, Planet.VENUS, Planet.SATURN)
            this[Planet.KETU] = setOf(Planet.MARS, Planet.VENUS, Planet.SATURN)
        }
    }

    /**
     * Extended natural enemies including Rahu/Ketu
     */
    private val extendedNaturalEnemies: Map<Planet, Set<Planet>> by lazy {
        AstrologicalConstants.NATURAL_ENEMIES.toMutableMap().apply {
            this[Planet.RAHU] = setOf(Planet.SUN, Planet.MOON, Planet.MARS)
            this[Planet.KETU] = setOf(Planet.SUN, Planet.MOON)
        }
    }

    /**
     * Relationship types between planets.
     */
    enum class PlanetaryRelationship {
        BEST_FRIEND,      // Adhimitra - Natural friend + Temporary friend
        FRIEND,           // Mitra - Natural friend
        NEUTRAL,          // Sama - Neutral
        ENEMY,            // Shatru - Natural enemy
        BITTER_ENEMY      // Adhishatru - Natural enemy + Temporary enemy
    }

    /**
     * Check if two planets are natural friends.
     * Uses extended map with Rahu/Ketu support.
     */
    fun areNaturalFriends(planet1: Planet, planet2: Planet): Boolean {
        return extendedNaturalFriends[planet1]?.contains(planet2) == true
    }

    /**
     * Check if two planets are natural enemies.
     * Uses extended map with Rahu/Ketu support.
     */
    fun areNaturalEnemies(planet1: Planet, planet2: Planet): Boolean {
        return extendedNaturalEnemies[planet1]?.contains(planet2) == true
    }

    /**
     * Get the natural relationship between two planets.
     */
    fun getNaturalRelationship(planet1: Planet, planet2: Planet): PlanetaryRelationship {
        return when {
            areNaturalFriends(planet1, planet2) -> PlanetaryRelationship.FRIEND
            areNaturalEnemies(planet1, planet2) -> PlanetaryRelationship.ENEMY
            else -> PlanetaryRelationship.NEUTRAL
        }
    }

    /**
     * Check if a planet is in a friendly sign.
     */
    fun isInFriendSign(planet: Planet, sign: ZodiacSign): Boolean {
        val signLord = sign.ruler
        return areNaturalFriends(planet, signLord)
    }

    /**
     * Check if a planet position is in a friendly sign.
     */
    fun isInFriendSign(pos: PlanetPosition): Boolean {
        return isInFriendSign(pos.planet, pos.sign)
    }

    /**
     * Check if a planet is in an enemy sign.
     */
    fun isInEnemySign(planet: Planet, sign: ZodiacSign): Boolean {
        val signLord = sign.ruler
        return areNaturalEnemies(planet, signLord)
    }

    /**
     * Check if a planet position is in an enemy sign.
     */
    fun isInEnemySign(pos: PlanetPosition): Boolean {
        return isInEnemySign(pos.planet, pos.sign)
    }

    /**
     * Calculate Panchada (5-fold) relationship considering temporary friendship.
     * Temporary friendship is based on house positions in a chart.
     */
    fun getComprehensiveRelationship(
        planet1: Planet,
        planet2: Planet,
        chart: VedicChart
    ): PlanetaryRelationship {
        val pos1 = chart.planetPositions.find { it.planet == planet1 }
        val pos2 = chart.planetPositions.find { it.planet == planet2 }

        if (pos1 == null || pos2 == null) {
            return getNaturalRelationship(planet1, planet2)
        }

        val houseDiff = abs(pos1.house - pos2.house)
        val isTemporaryFriend = houseDiff in listOf(0, 1, 2, 3, 4, 10, 11, 12) // 2,3,4,10,11,12 from each other
        val isTemporaryEnemy = houseDiff in listOf(5, 6, 7, 8, 9)

        val naturalRel = getNaturalRelationship(planet1, planet2)

        return when {
            naturalRel == PlanetaryRelationship.FRIEND && isTemporaryFriend ->
                PlanetaryRelationship.BEST_FRIEND
            naturalRel == PlanetaryRelationship.ENEMY && isTemporaryEnemy ->
                PlanetaryRelationship.BITTER_ENEMY
            naturalRel == PlanetaryRelationship.FRIEND || isTemporaryFriend ->
                PlanetaryRelationship.FRIEND
            naturalRel == PlanetaryRelationship.ENEMY || isTemporaryEnemy ->
                PlanetaryRelationship.ENEMY
            else -> PlanetaryRelationship.NEUTRAL
        }
    }

    // ============================================================================
    // HOUSE CLASSIFICATIONS - Delegates to AstrologicalConstants
    // ============================================================================

    /** Kendra houses (Angular/Quadrant) - 1, 4, 7, 10 */
    val KENDRA_HOUSES: Set<Int> get() = AstrologicalConstants.KENDRA_HOUSES

    /** Trikona houses (Trine) - 1, 5, 9 */
    val TRIKONA_HOUSES: Set<Int> get() = AstrologicalConstants.TRIKONA_HOUSES

    /** Dusthana houses (Malefic) - 6, 8, 12 */
    val DUSTHANA_HOUSES: Set<Int> get() = AstrologicalConstants.DUSTHANA_HOUSES

    /** Upachaya houses (Growth) - 3, 6, 10, 11 */
    val UPACHAYA_HOUSES: Set<Int> get() = AstrologicalConstants.UPACHAYA_HOUSES

    /** Maraka houses (Death-inflicting) - 2, 7 */
    val MARAKA_HOUSES: Set<Int> get() = AstrologicalConstants.MARAKA_HOUSES

    /** Dharma houses - 1, 5, 9 */
    val DHARMA_HOUSES: Set<Int> get() = AstrologicalConstants.DHARMA_HOUSES

    /** Artha houses - 2, 6, 10 */
    val ARTHA_HOUSES: Set<Int> get() = AstrologicalConstants.ARTHA_HOUSES

    /** Kama houses - 3, 7, 11 */
    val KAMA_HOUSES: Set<Int> get() = AstrologicalConstants.KAMA_HOUSES

    /** Moksha houses - 4, 8, 12 */
    val MOKSHA_HOUSES: Set<Int> get() = AstrologicalConstants.MOKSHA_HOUSES

    /**
     * Check if a house is a Kendra (angular) house.
     * Delegates to AstrologicalConstants.
     */
    fun isKendra(house: Int): Boolean = AstrologicalConstants.isKendra(house)

    /**
     * Check if a house is a Trikona (trine) house.
     * Delegates to AstrologicalConstants.
     */
    fun isTrikona(house: Int): Boolean = AstrologicalConstants.isTrikona(house)

    /**
     * Check if a house is a Dusthana (malefic) house.
     * Delegates to AstrologicalConstants.
     */
    fun isDusthana(house: Int): Boolean = AstrologicalConstants.isDusthana(house)

    /**
     * Check if a house is an Upachaya (growth) house.
     */
    fun isUpachaya(house: Int): Boolean = house in UPACHAYA_HOUSES

    /**
     * Calculate house number from reference (1-indexed).
     * @param targetSign The sign to calculate house for
     * @param referenceSign The sign of the ascendant or reference point
     * @return House number (1-12)
     */
    fun getHouseFromSigns(targetSign: ZodiacSign, referenceSign: ZodiacSign): Int {
        val diff = targetSign.number - referenceSign.number
        return if (diff >= 0) diff + 1 else diff + 13
    }

    // ============================================================================
    // NAKSHATRA ATTRIBUTES
    // ============================================================================

    /**
     * Gana (Temperament) for each Nakshatra.
     * - Deva: Divine, gentle, spiritual nature
     * - Manushya: Human, balanced, worldly nature
     * - Rakshasa: Demonic, aggressive, dominant nature
     */
    enum class Gana { DEVA, MANUSHYA, RAKSHASA }

    private val nakshatraGanaMap = mapOf(
        Nakshatra.ASHWINI to Gana.DEVA,
        Nakshatra.BHARANI to Gana.MANUSHYA,
        Nakshatra.KRITTIKA to Gana.RAKSHASA,
        Nakshatra.ROHINI to Gana.MANUSHYA,
        Nakshatra.MRIGASHIRA to Gana.DEVA,
        Nakshatra.ARDRA to Gana.MANUSHYA,
        Nakshatra.PUNARVASU to Gana.DEVA,
        Nakshatra.PUSHYA to Gana.DEVA,
        Nakshatra.ASHLESHA to Gana.RAKSHASA,
        Nakshatra.MAGHA to Gana.RAKSHASA,
        Nakshatra.PURVA_PHALGUNI to Gana.MANUSHYA,
        Nakshatra.UTTARA_PHALGUNI to Gana.MANUSHYA,
        Nakshatra.HASTA to Gana.DEVA,
        Nakshatra.CHITRA to Gana.RAKSHASA,
        Nakshatra.SWATI to Gana.DEVA,
        Nakshatra.VISHAKHA to Gana.RAKSHASA,
        Nakshatra.ANURADHA to Gana.DEVA,
        Nakshatra.JYESHTHA to Gana.RAKSHASA,
        Nakshatra.MULA to Gana.RAKSHASA,
        Nakshatra.PURVA_ASHADHA to Gana.MANUSHYA,
        Nakshatra.UTTARA_ASHADHA to Gana.MANUSHYA,
        Nakshatra.SHRAVANA to Gana.DEVA,
        Nakshatra.DHANISHTHA to Gana.RAKSHASA,
        Nakshatra.SHATABHISHA to Gana.RAKSHASA,
        Nakshatra.PURVA_BHADRAPADA to Gana.MANUSHYA,
        Nakshatra.UTTARA_BHADRAPADA to Gana.MANUSHYA,
        Nakshatra.REVATI to Gana.DEVA
    )

    /**
     * Get the Gana (temperament) for a Nakshatra.
     */
    fun getGana(nakshatra: Nakshatra): Gana {
        return nakshatraGanaMap[nakshatra] ?: Gana.MANUSHYA
    }

    /**
     * Get localized Gana name.
     */
    fun getGanaName(gana: Gana, language: Language): String {
        return when (gana) {
            Gana.DEVA -> StringResources.get(StringKeyMatch.GANA_DEVA, language)
            Gana.MANUSHYA -> StringResources.get(StringKeyMatch.GANA_MANUSHYA, language)
            Gana.RAKSHASA -> StringResources.get(StringKeyMatch.GANA_RAKSHASA, language)
        }
    }

    /**
     * Yoni (Sexual compatibility) data for each Nakshatra.
     */
    data class YoniInfo(
        val animal: String,
        val animalKey: com.astro.storm.data.localization.StringKeyInterface,
        val gender: Gender,
        val groupId: Int
    )

    enum class Gender { MALE, FEMALE }

    private val nakshatraYoniMap = mapOf(
        Nakshatra.ASHWINI to YoniInfo("Horse", StringKeyMatch.YONI_HORSE, Gender.MALE, 1),
        Nakshatra.BHARANI to YoniInfo("Elephant", StringKeyMatch.YONI_ELEPHANT, Gender.MALE, 2),
        Nakshatra.KRITTIKA to YoniInfo("Sheep", StringKeyMatch.YONI_SHEEP, Gender.FEMALE, 3),
        Nakshatra.ROHINI to YoniInfo("Serpent", StringKeyMatch.YONI_SERPENT, Gender.MALE, 4),
        Nakshatra.MRIGASHIRA to YoniInfo("Serpent", StringKeyMatch.YONI_SERPENT, Gender.FEMALE, 4),
        Nakshatra.ARDRA to YoniInfo("Dog", StringKeyMatch.YONI_DOG, Gender.FEMALE, 5),
        Nakshatra.PUNARVASU to YoniInfo("Cat", StringKeyMatch.YONI_CAT, Gender.FEMALE, 6),
        Nakshatra.PUSHYA to YoniInfo("Sheep", StringKeyMatch.YONI_SHEEP, Gender.MALE, 3),
        Nakshatra.ASHLESHA to YoniInfo("Cat", StringKeyMatch.YONI_CAT, Gender.MALE, 6),
        Nakshatra.MAGHA to YoniInfo("Rat", StringKeyMatch.YONI_RAT, Gender.MALE, 7),
        Nakshatra.PURVA_PHALGUNI to YoniInfo("Rat", StringKeyMatch.YONI_RAT, Gender.FEMALE, 7),
        Nakshatra.UTTARA_PHALGUNI to YoniInfo("Cow", StringKeyMatch.YONI_COW, Gender.MALE, 8),
        Nakshatra.HASTA to YoniInfo("Buffalo", StringKeyMatch.YONI_BUFFALO, Gender.FEMALE, 9),
        Nakshatra.CHITRA to YoniInfo("Tiger", StringKeyMatch.YONI_TIGER, Gender.FEMALE, 10),
        Nakshatra.SWATI to YoniInfo("Buffalo", StringKeyMatch.YONI_BUFFALO, Gender.MALE, 9),
        Nakshatra.VISHAKHA to YoniInfo("Tiger", StringKeyMatch.YONI_TIGER, Gender.MALE, 10),
        Nakshatra.ANURADHA to YoniInfo("Deer", StringKeyMatch.YONI_DEER, Gender.FEMALE, 11),
        Nakshatra.JYESHTHA to YoniInfo("Deer", StringKeyMatch.YONI_DEER, Gender.MALE, 11),
        Nakshatra.MULA to YoniInfo("Dog", StringKeyMatch.YONI_DOG, Gender.MALE, 5),
        Nakshatra.PURVA_ASHADHA to YoniInfo("Monkey", StringKeyMatch.YONI_MONKEY, Gender.MALE, 12),
        Nakshatra.UTTARA_ASHADHA to YoniInfo("Mongoose", StringKeyMatch.YONI_MONGOOSE, Gender.MALE, 13),
        Nakshatra.SHRAVANA to YoniInfo("Monkey", StringKeyMatch.YONI_MONKEY, Gender.FEMALE, 12),
        Nakshatra.DHANISHTHA to YoniInfo("Lion", StringKeyMatch.YONI_LION, Gender.FEMALE, 14),
        Nakshatra.SHATABHISHA to YoniInfo("Horse", StringKeyMatch.YONI_HORSE, Gender.FEMALE, 1),
        Nakshatra.PURVA_BHADRAPADA to YoniInfo("Lion", StringKeyMatch.YONI_LION, Gender.MALE, 14),
        Nakshatra.UTTARA_BHADRAPADA to YoniInfo("Cow", StringKeyMatch.YONI_COW, Gender.FEMALE, 8),
        Nakshatra.REVATI to YoniInfo("Elephant", StringKeyMatch.YONI_ELEPHANT, Gender.FEMALE, 2)
    )

    /**
     * Get the Yoni info for a Nakshatra.
     */
    fun getYoni(nakshatra: Nakshatra): YoniInfo? {
        return nakshatraYoniMap[nakshatra]
    }

    /**
     * Get Yoni display string (English format).
     */
    fun getYoniDisplayName(nakshatra: Nakshatra): String {
        val yoni = nakshatraYoniMap[nakshatra] ?: return "Unknown"
        val genderStr = if (yoni.gender == Gender.MALE) "Male" else "Female"
        return "${yoni.animal} ($genderStr)"
    }

    /**
     * Get localized Yoni display string.
     */
    fun getYoniDisplayName(nakshatra: Nakshatra, language: Language): String {
        val yoni = nakshatraYoniMap[nakshatra] ?: return "Unknown"
        val animalName = StringResources.get(yoni.animalKey, language)
        val genderStr = if (yoni.gender == Gender.MALE)
            StringResources.get(StringKey.GENDER_MALE, language)
        else
            StringResources.get(StringKey.GENDER_FEMALE, language)
        return "$animalName ($genderStr)"
    }

    /**
     * Get Gana display name (English).
     */
    fun getGanaDisplayName(nakshatra: Nakshatra): String {
        return when (getGana(nakshatra)) {
            Gana.DEVA -> "Deva"
            Gana.MANUSHYA -> "Manushya"
            Gana.RAKSHASA -> "Rakshasa"
        }
    }

    /**
     * Get localized Gana display name.
     */
    fun getGanaDisplayName(nakshatra: Nakshatra, language: Language): String {
        return getGanaName(getGana(nakshatra), language)
    }

    /**
     * Nadi (Health compatibility) for each Nakshatra.
     * - Adi (Vata): Wind element, beginning
     * - Madhya (Pitta): Fire element, middle
     * - Antya (Kapha): Water element, end
     */
    enum class Nadi { ADI, MADHYA, ANTYA }

    private val nakshatraNadiMap = mapOf(
        Nakshatra.ASHWINI to Nadi.ADI,
        Nakshatra.BHARANI to Nadi.MADHYA,
        Nakshatra.KRITTIKA to Nadi.ANTYA,
        Nakshatra.ROHINI to Nadi.ANTYA,
        Nakshatra.MRIGASHIRA to Nadi.MADHYA,
        Nakshatra.ARDRA to Nadi.ADI,
        Nakshatra.PUNARVASU to Nadi.ADI,
        Nakshatra.PUSHYA to Nadi.MADHYA,
        Nakshatra.ASHLESHA to Nadi.ANTYA,
        Nakshatra.MAGHA to Nadi.ANTYA,
        Nakshatra.PURVA_PHALGUNI to Nadi.MADHYA,
        Nakshatra.UTTARA_PHALGUNI to Nadi.ADI,
        Nakshatra.HASTA to Nadi.ADI,
        Nakshatra.CHITRA to Nadi.MADHYA,
        Nakshatra.SWATI to Nadi.ANTYA,
        Nakshatra.VISHAKHA to Nadi.ANTYA,
        Nakshatra.ANURADHA to Nadi.MADHYA,
        Nakshatra.JYESHTHA to Nadi.ADI,
        Nakshatra.MULA to Nadi.ADI,
        Nakshatra.PURVA_ASHADHA to Nadi.MADHYA,
        Nakshatra.UTTARA_ASHADHA to Nadi.ANTYA,
        Nakshatra.SHRAVANA to Nadi.ANTYA,
        Nakshatra.DHANISHTHA to Nadi.MADHYA,
        Nakshatra.SHATABHISHA to Nadi.ADI,
        Nakshatra.PURVA_BHADRAPADA to Nadi.ADI,
        Nakshatra.UTTARA_BHADRAPADA to Nadi.MADHYA,
        Nakshatra.REVATI to Nadi.ANTYA
    )

    /**
     * Get the Nadi for a Nakshatra.
     */
    fun getNadi(nakshatra: Nakshatra): Nadi {
        return nakshatraNadiMap[nakshatra] ?: Nadi.MADHYA
    }

    /**
     * Varna (Social class) for each Nakshatra.
     * Used in Varna Koot matching.
     */
    enum class Varna(val value: Int) { BRAHMIN(4), KSHATRIYA(3), VAISHYA(2), SHUDRA(1) }

    private val nakshatraVarnaMap = mapOf(
        Nakshatra.ASHWINI to Varna.VAISHYA,
        Nakshatra.BHARANI to Varna.SHUDRA,
        Nakshatra.KRITTIKA to Varna.BRAHMIN,
        Nakshatra.ROHINI to Varna.SHUDRA,
        Nakshatra.MRIGASHIRA to Varna.SHUDRA,
        Nakshatra.ARDRA to Varna.SHUDRA,
        Nakshatra.PUNARVASU to Varna.VAISHYA,
        Nakshatra.PUSHYA to Varna.KSHATRIYA,
        Nakshatra.ASHLESHA to Varna.SHUDRA,
        Nakshatra.MAGHA to Varna.SHUDRA,
        Nakshatra.PURVA_PHALGUNI to Varna.BRAHMIN,
        Nakshatra.UTTARA_PHALGUNI to Varna.KSHATRIYA,
        Nakshatra.HASTA to Varna.VAISHYA,
        Nakshatra.CHITRA to Varna.SHUDRA,
        Nakshatra.SWATI to Varna.SHUDRA,
        Nakshatra.VISHAKHA to Varna.BRAHMIN,
        Nakshatra.ANURADHA to Varna.SHUDRA,
        Nakshatra.JYESHTHA to Varna.SHUDRA,
        Nakshatra.MULA to Varna.SHUDRA,
        Nakshatra.PURVA_ASHADHA to Varna.BRAHMIN,
        Nakshatra.UTTARA_ASHADHA to Varna.KSHATRIYA,
        Nakshatra.SHRAVANA to Varna.SHUDRA,
        Nakshatra.DHANISHTHA to Varna.SHUDRA,
        Nakshatra.SHATABHISHA to Varna.SHUDRA,
        Nakshatra.PURVA_BHADRAPADA to Varna.BRAHMIN,
        Nakshatra.UTTARA_BHADRAPADA to Varna.KSHATRIYA,
        Nakshatra.REVATI to Varna.SHUDRA
    )

    /**
     * Get the Varna for a Nakshatra.
     */
    fun getVarna(nakshatra: Nakshatra): Varna {
        return nakshatraVarnaMap[nakshatra] ?: Varna.SHUDRA
    }

    /**
     * Rajju (Body part) classification for Nakshatras.
     * Used in Rajju Koot matching for longevity of spouse.
     */
    enum class Rajju { PADA, KATI, NABHI, KANTHA, SIRO }

    /**
     * Rajju direction for enhanced matching.
     */
    enum class RajjuDirection { ASCENDING, DESCENDING }

    data class RajjuInfo(val rajju: Rajju, val direction: RajjuDirection)

    private val nakshatraRajjuMap = mapOf(
        // Pada Rajju (Feet)
        Nakshatra.ASHWINI to RajjuInfo(Rajju.PADA, RajjuDirection.ASCENDING),
        Nakshatra.ASHLESHA to RajjuInfo(Rajju.PADA, RajjuDirection.DESCENDING),
        Nakshatra.MAGHA to RajjuInfo(Rajju.PADA, RajjuDirection.ASCENDING),
        Nakshatra.JYESHTHA to RajjuInfo(Rajju.PADA, RajjuDirection.DESCENDING),
        Nakshatra.MULA to RajjuInfo(Rajju.PADA, RajjuDirection.ASCENDING),
        Nakshatra.REVATI to RajjuInfo(Rajju.PADA, RajjuDirection.DESCENDING),

        // Kati Rajju (Waist)
        Nakshatra.BHARANI to RajjuInfo(Rajju.KATI, RajjuDirection.ASCENDING),
        Nakshatra.PUSHYA to RajjuInfo(Rajju.KATI, RajjuDirection.DESCENDING),
        Nakshatra.PURVA_PHALGUNI to RajjuInfo(Rajju.KATI, RajjuDirection.ASCENDING),
        Nakshatra.ANURADHA to RajjuInfo(Rajju.KATI, RajjuDirection.DESCENDING),
        Nakshatra.PURVA_ASHADHA to RajjuInfo(Rajju.KATI, RajjuDirection.ASCENDING),
        Nakshatra.UTTARA_BHADRAPADA to RajjuInfo(Rajju.KATI, RajjuDirection.DESCENDING),

        // Nabhi Rajju (Navel)
        Nakshatra.KRITTIKA to RajjuInfo(Rajju.NABHI, RajjuDirection.ASCENDING),
        Nakshatra.PUNARVASU to RajjuInfo(Rajju.NABHI, RajjuDirection.DESCENDING),
        Nakshatra.UTTARA_PHALGUNI to RajjuInfo(Rajju.NABHI, RajjuDirection.ASCENDING),
        Nakshatra.VISHAKHA to RajjuInfo(Rajju.NABHI, RajjuDirection.DESCENDING),
        Nakshatra.UTTARA_ASHADHA to RajjuInfo(Rajju.NABHI, RajjuDirection.ASCENDING),
        Nakshatra.PURVA_BHADRAPADA to RajjuInfo(Rajju.NABHI, RajjuDirection.DESCENDING),

        // Kantha Rajju (Neck)
        Nakshatra.ROHINI to RajjuInfo(Rajju.KANTHA, RajjuDirection.ASCENDING),
        Nakshatra.ARDRA to RajjuInfo(Rajju.KANTHA, RajjuDirection.DESCENDING),
        Nakshatra.HASTA to RajjuInfo(Rajju.KANTHA, RajjuDirection.ASCENDING),
        Nakshatra.SWATI to RajjuInfo(Rajju.KANTHA, RajjuDirection.DESCENDING),
        Nakshatra.SHRAVANA to RajjuInfo(Rajju.KANTHA, RajjuDirection.ASCENDING),
        Nakshatra.SHATABHISHA to RajjuInfo(Rajju.KANTHA, RajjuDirection.DESCENDING),

        // Siro Rajju (Head)
        Nakshatra.MRIGASHIRA to RajjuInfo(Rajju.SIRO, RajjuDirection.ASCENDING),
        Nakshatra.CHITRA to RajjuInfo(Rajju.SIRO, RajjuDirection.ASCENDING),
        Nakshatra.DHANISHTHA to RajjuInfo(Rajju.SIRO, RajjuDirection.ASCENDING)
    )

    /**
     * Get the Rajju info for a Nakshatra.
     */
    fun getRajju(nakshatra: Nakshatra): RajjuInfo {
        return nakshatraRajjuMap[nakshatra] ?: RajjuInfo(Rajju.NABHI, RajjuDirection.ASCENDING)
    }

    // ============================================================================
    // COMBUSTION (ASTA) CALCULATIONS - Using AstrologicalConstants
    // ============================================================================

    /**
     * Check if a planet is combust (too close to Sun).
     * Combust planets lose strength and their significations suffer.
     * Uses AstrologicalConstants.COMBUSTION_DEGREES for thresholds.
     *
     * @param planet The planet to check
     * @param planetLongitude The longitude of the planet
     * @param sunLongitude The longitude of the Sun
     * @param isRetrograde Whether the planet is retrograde
     * @return True if the planet is combust
     */
    fun isCombust(
        planet: Planet,
        planetLongitude: Double,
        sunLongitude: Double,
        isRetrograde: Boolean = false
    ): Boolean {
        // Sun, Rahu, Ketu cannot be combust
        if (planet in listOf(Planet.SUN, Planet.RAHU, Planet.KETU)) return false

        val orb = AstrologicalConstants.COMBUSTION_DEGREES[planet] ?: return false
        val adjustedOrb = when {
            // Mercury and Venus have tighter orbs when retrograde (as per classical texts)
            planet == Planet.MERCURY && isRetrograde -> 12.0
            planet == Planet.VENUS && isRetrograde -> 8.0
            else -> orb
        }

        val diff = abs(normalizeAngle(planetLongitude - sunLongitude))
        return diff <= adjustedOrb || diff >= (360.0 - adjustedOrb)
    }

    /**
     * Check if a planet position is combust.
     */
    fun isCombust(pos: PlanetPosition, sunPosition: PlanetPosition): Boolean {
        return isCombust(pos.planet, pos.longitude, sunPosition.longitude, pos.isRetrograde)
    }

    // ============================================================================
    // DIG BALA (DIRECTIONAL STRENGTH) - Using AstrologicalConstants
    // ============================================================================

    /**
     * Check if a planet has Dig Bala (directional strength).
     * Delegates to AstrologicalConstants.
     */
    fun hasDigBala(planet: Planet, house: Int): Boolean {
        return AstrologicalConstants.hasDigBala(planet, house)
    }

    /**
     * Check if a planet position has Dig Bala.
     */
    fun hasDigBala(pos: PlanetPosition): Boolean {
        return hasDigBala(pos.planet, pos.house)
    }

    // ============================================================================
    // BENEFIC/MALEFIC CLASSIFICATION - Using AstrologicalConstants
    // ============================================================================

    /** Natural benefics - delegates to AstrologicalConstants */
    val NATURAL_BENEFICS: Set<Planet> get() = AstrologicalConstants.NATURAL_BENEFICS

    /** Natural malefics - delegates to AstrologicalConstants */
    val NATURAL_MALEFICS: Set<Planet> get() = AstrologicalConstants.NATURAL_MALEFICS

    /**
     * Check if a planet is naturally benefic.
     * Note: Mercury becomes malefic when conjunct malefics.
     * Note: Moon becomes malefic when waning (Krishna Paksha).
     * Delegates to AstrologicalConstants.
     */
    fun isNaturalBenefic(planet: Planet): Boolean {
        return AstrologicalConstants.isNaturalBenefic(planet)
    }

    /**
     * Check if a planet is naturally malefic.
     * Delegates to AstrologicalConstants.
     */
    fun isNaturalMalefic(planet: Planet): Boolean {
        return AstrologicalConstants.isNaturalMalefic(planet)
    }

    /**
     * Check functional benefic status based on ascendant.
     * A planet ruling Kendra or Trikona becomes benefic for that ascendant.
     */
    fun isFunctionalBenefic(planet: Planet, ascendantSign: ZodiacSign): Boolean {
        val ruledSigns = extendedOwnSigns[planet] ?: emptyList()

        return ruledSigns.any { sign ->
            val houseFromAsc = getHouseFromSigns(sign, ascendantSign)
            houseFromAsc in KENDRA_HOUSES || houseFromAsc in TRIKONA_HOUSES
        }
    }

    /**
     * Check functional malefic status based on ascendant.
     * A planet ruling Dusthana becomes malefic for that ascendant.
     */
    fun isFunctionalMalefic(planet: Planet, ascendantSign: ZodiacSign): Boolean {
        val ruledSigns = extendedOwnSigns[planet] ?: emptyList()

        return ruledSigns.any { sign ->
            val houseFromAsc = getHouseFromSigns(sign, ascendantSign)
            houseFromAsc in DUSTHANA_HOUSES
        }
    }

    // ============================================================================
    // ASPECT CALCULATIONS - Using AstrologicalConstants
    // ============================================================================

    /**
     * Extended special aspects including Rahu/Ketu (some schools treat them like Jupiter)
     */
    private val extendedSpecialAspects: Map<Planet, List<Int>> by lazy {
        // Start with AstrologicalConstants special aspects (Mars, Jupiter, Saturn)
        val base = AstrologicalConstants.SPECIAL_ASPECTS.mapValues { (_, aspectMap) ->
            aspectMap.keys.toList()
        }.toMutableMap()
        // Add Rahu/Ketu aspects (5th and 9th like Jupiter, per some traditions)
        base[Planet.RAHU] = listOf(5, 9)
        base[Planet.KETU] = listOf(5, 9)
        base
    }

    /**
     * Get all houses a planet aspects from its current house.
     * @param planet The planet
     * @param fromHouse The house the planet is in (1-12)
     * @return List of houses (1-12) the planet aspects
     */
    fun getAspectedHouses(planet: Planet, fromHouse: Int): List<Int> {
        val aspects = mutableListOf<Int>()

        // All planets aspect the 7th house from themselves
        val seventhHouse = ((fromHouse + 6) % 12).let { if (it == 0) 12 else it }
        aspects.add(seventhHouse)

        // Add special aspects from extended map
        extendedSpecialAspects[planet]?.forEach { offset ->
            val aspectedHouse = ((fromHouse + offset - 1) % 12).let { if (it == 0) 12 else it }
            aspects.add(aspectedHouse)
        }

        return aspects.distinct().sorted()
    }

    /**
     * Check if a planet aspects a specific house.
     */
    fun aspectsHouse(planet: Planet, fromHouse: Int, targetHouse: Int): Boolean {
        return targetHouse in getAspectedHouses(planet, fromHouse)
    }

    /**
     * Get aspect strength for a planet's aspect on a house.
     * Delegates to AstrologicalConstants for strength values.
     */
    fun getAspectStrength(planet: Planet, aspectHouse: Int): Double {
        return AstrologicalConstants.getSpecialAspectStrength(planet, aspectHouse)
    }

    // ============================================================================
    // UTILITY FUNCTIONS - Some delegate to AstrologicalConstants
    // ============================================================================

    /**
     * Normalize an angle to 0-360 range.
     * Delegates to AstrologicalConstants for consistency.
     */
    fun normalizeAngle(angle: Double): Double {
        return AstrologicalConstants.normalizeDegree(angle)
    }

    /**
     * Alias for normalizeAngle for semantic clarity when working with longitudes.
     */
    fun normalizeLongitude(longitude: Double): Double = normalizeAngle(longitude)

    /**
     * Alias for normalizeAngle for semantic clarity when working with degrees.
     */
    fun normalizeDegree(degree: Double): Double = normalizeAngle(degree)

    /**
     * Get zodiac sign from longitude.
     * @param longitude The sidereal longitude in degrees.
     * @return The corresponding zodiac sign.
     */
    fun getSignFromLongitude(longitude: Double): ZodiacSign {
        return ZodiacSign.fromLongitude(normalizeLongitude(longitude))
    }

    /**
     * Get the degree within the sign (0-30).
     * @param longitude The sidereal longitude in degrees.
     * @return The degree within the sign (0-30).
     */
    fun getDegreeInSign(longitude: Double): Double {
        return normalizeLongitude(longitude) % 30.0
    }

    /**
     * Calculate the angular distance between two longitudes.
     * Returns the shortest distance (0-180).
     * @param long1 First longitude.
     * @param long2 Second longitude.
     * @return The angular distance (0-180).
     */
    fun angularDistance(long1: Double, long2: Double): Double {
        val diff = abs(normalizeLongitude(long1) - normalizeLongitude(long2))
        return if (diff > 180.0) 360.0 - diff else diff
    }

    /**
     * Calculate whole sign house from longitude and ascendant.
     * @param longitude The planet's longitude.
     * @param ascendantLongitude The ascendant longitude.
     * @return The house number (1-12).
     */
    fun calculateWholeSignHouse(longitude: Double, ascendantLongitude: Double): Int {
        val normalizedLong = normalizeLongitude(longitude)
        val normalizedAsc = normalizeLongitude(ascendantLongitude)
        val ascSign = (normalizedAsc / 30.0).toInt().coerceIn(0, 11)
        val planetSign = (normalizedLong / 30.0).toInt().coerceIn(0, 11)
        val house = ((planetSign - ascSign + 12) % 12) + 1
        return house.coerceIn(1, 12)
    }

    /**
     * Get ordinal suffix for a number (1st, 2nd, 3rd, etc.)
     * @param n The number.
     * @return The ordinal suffix.
     */
    fun getOrdinalSuffix(n: Int): String {
        return when {
            n in 11..13 -> "th"
            n % 10 == 1 -> "st"
            n % 10 == 2 -> "nd"
            n % 10 == 3 -> "rd"
            else -> "th"
        }
    }

    /**
     * Get Moon position from a chart.
     */
    fun getMoonPosition(chart: VedicChart): PlanetPosition? {
        return chart.planetPositions.find { it.planet == Planet.MOON }
    }

    /**
     * Get Sun position from a chart.
     */
    fun getSunPosition(chart: VedicChart): PlanetPosition? {
        return chart.planetPositions.find { it.planet == Planet.SUN }
    }

    /**
     * Get a specific planet's position from a chart.
     */
    fun getPlanetPosition(chart: VedicChart, planet: Planet): PlanetPosition? {
        return chart.planetPositions.find { it.planet == planet }
    }

    /**
     * Get the ascendant sign from a chart.
     */
    fun getAscendantSign(chart: VedicChart): ZodiacSign {
        return ZodiacSign.fromLongitude(chart.ascendant)
    }

    /**
     * Get all planets in a specific house.
     */
    fun getPlanetsInHouse(chart: VedicChart, house: Int): List<PlanetPosition> {
        return chart.planetPositions.filter { it.house == house }
    }

    /**
     * Get the lord of a house.
     */
    fun getHouseLord(chart: VedicChart, house: Int): Planet {
        val ascSign = getAscendantSign(chart)
        val houseSignNumber = ((ascSign.number + house - 2) % 12) + 1
        val houseSign = ZodiacSign.entries.find { it.number == houseSignNumber } ?: ZodiacSign.ARIES
        return houseSign.ruler
    }

    /**
     * Check if two planets are in conjunction (same house or within orb).
     */
    fun areInConjunction(pos1: PlanetPosition, pos2: PlanetPosition, orb: Double = 10.0): Boolean {
        if (pos1.house != pos2.house) return false
        val diff = abs(normalizeAngle(pos1.longitude - pos2.longitude))
        return diff <= orb || diff >= (360.0 - orb)
    }

    /**
     * Check if a planet is hemmed between malefics (Papakartari Yoga).
     */
    fun isPapakartari(pos: PlanetPosition, chart: VedicChart): Boolean {
        val prevHouse = if (pos.house == 1) 12 else pos.house - 1
        val nextHouse = if (pos.house == 12) 1 else pos.house + 1

        val hasMaleficBefore = chart.planetPositions.any {
            it.house == prevHouse && isNaturalMalefic(it.planet)
        }
        val hasMaleficAfter = chart.planetPositions.any {
            it.house == nextHouse && isNaturalMalefic(it.planet)
        }

        return hasMaleficBefore && hasMaleficAfter
    }

    /**
     * Check if a planet is hemmed between benefics (Shubhakartari Yoga).
     */
    fun isShubhakartari(pos: PlanetPosition, chart: VedicChart): Boolean {
        val prevHouse = if (pos.house == 1) 12 else pos.house - 1
        val nextHouse = if (pos.house == 12) 1 else pos.house + 1

        val hasBeneficBefore = chart.planetPositions.any {
            it.house == prevHouse && isNaturalBenefic(it.planet)
        }
        val hasBeneficAfter = chart.planetPositions.any {
            it.house == nextHouse && isNaturalBenefic(it.planet)
        }

        return hasBeneficBefore && hasBeneficAfter
    }
}
