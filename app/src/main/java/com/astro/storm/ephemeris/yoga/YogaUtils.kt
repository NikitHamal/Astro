package com.astro.storm.ephemeris.yoga

import com.astro.storm.data.model.Planet
import com.astro.storm.data.model.PlanetPosition
import com.astro.storm.data.model.VedicChart
import com.astro.storm.data.model.ZodiacSign
import com.astro.storm.ephemeris.VedicAstrologyUtils
import kotlin.math.abs
import kotlin.math.min

/**
 * Yoga Calculation Utilities
 *
 * This object contains all helper functions used throughout the Yoga calculation system.
 * Includes planetary relationship checks, strength calculations, aspect detection,
 * and cancellation factor analysis.
 *
 * Based on classical Vedic astrology texts:
 * - Brihat Parasara Hora Shastra (BPHS)
 * - Phaladeepika
 * - Saravali
 *
 * @author AstroStorm - Ultra-Precision Vedic Astrology
 */
object YogaUtils {

    // ==================== HOUSE LORD CALCULATIONS ====================

    /**
     * Get house lords for all 12 houses based on ascendant sign
     */
    fun getHouseLords(ascendantSign: ZodiacSign): Map<Int, Planet> {
        val lords = mutableMapOf<Int, Planet>()
        for (house in 1..12) {
            val signIndex = (ascendantSign.ordinal + house - 1) % 12
            val sign = ZodiacSign.entries[signIndex]
            lords[house] = sign.ruler
        }
        return lords
    }

    // ==================== CONJUNCTION & ASPECT DETECTION ====================

    /**
     * Check if two planets are conjunct (within orb)
     * Default orb is 8° as per Vedic standards
     */
    fun areConjunct(pos1: PlanetPosition, pos2: PlanetPosition, customOrb: Double? = null): Boolean {
        val distance = abs(pos1.longitude - pos2.longitude)
        val normalizedDistance = if (distance > 180) 360 - distance else distance
        val orb = customOrb ?: 8.0
        return normalizedDistance <= orb
    }

    /**
     * Check for mutual aspect (opposition - 180°)
     */
    fun areMutuallyAspecting(pos1: PlanetPosition, pos2: PlanetPosition): Boolean {
        val angle = abs(pos1.longitude - pos2.longitude)
        val normalizedAngle = if (angle > 180) 360 - angle else angle
        return normalizedAngle in 170.0..190.0
    }

    /**
     * Check for exchange (Parivartana) - planets in each other's signs
     */
    fun areInExchange(pos1: PlanetPosition, pos2: PlanetPosition): Boolean {
        return pos1.sign.ruler == pos2.planet && pos2.sign.ruler == pos1.planet
    }

    /**
     * Check if planet is in Kendra from reference planet
     */
    fun isInKendraFrom(pos: PlanetPosition, reference: PlanetPosition): Boolean {
        val house = getHouseFrom(pos.sign, reference.sign)
        return house in listOf(1, 4, 7, 10)
    }

    /**
     * Calculate house position from reference sign
     */
    fun getHouseFrom(targetSign: ZodiacSign, referenceSign: ZodiacSign): Int {
        val diff = targetSign.number - referenceSign.number
        return if (diff >= 0) diff + 1 else diff + 13
    }

    // ==================== PLANETARY DIGNITY ====================

    /**
     * Check if planet is in own sign (Swakshetra)
     */
    fun isInOwnSign(pos: PlanetPosition): Boolean {
        return VedicAstrologyUtils.isInOwnSign(pos)
    }

    /**
     * Check if planet is exalted (Uchcha)
     */
    fun isExalted(pos: PlanetPosition): Boolean {
        return VedicAstrologyUtils.isExalted(pos)
    }

    /**
     * Check if planet is debilitated (Neecha)
     */
    fun isDebilitated(pos: PlanetPosition): Boolean {
        return VedicAstrologyUtils.isDebilitated(pos)
    }

    /**
     * Check if planet has Dig Bala (directional strength)
     */
    fun hasDigBala(pos: PlanetPosition): Boolean {
        return VedicAstrologyUtils.hasDigBala(pos)
    }

    /**
     * Check if planet is in friend's sign
     */
    fun isInFriendSign(pos: PlanetPosition): Boolean {
        return VedicAstrologyUtils.isInFriendSign(pos)
    }

    /**
     * Check if planet is in enemy's sign
     */
    fun isInEnemySign(pos: PlanetPosition): Boolean {
        val enemies = getEnemies(pos.planet)
        return pos.sign.ruler in enemies
    }

    /**
     * Get natural enemies of a planet (BPHS)
     */
    fun getEnemies(planet: Planet): List<Planet> {
        return when (planet) {
            Planet.SUN -> listOf(Planet.SATURN, Planet.VENUS)
            Planet.MOON -> emptyList()
            Planet.MARS -> listOf(Planet.MERCURY)
            Planet.MERCURY -> listOf(Planet.MOON)
            Planet.JUPITER -> listOf(Planet.MERCURY, Planet.VENUS)
            Planet.VENUS -> listOf(Planet.SUN, Planet.MOON)
            Planet.SATURN -> listOf(Planet.SUN, Planet.MOON, Planet.MARS)
            Planet.RAHU -> listOf(Planet.SUN, Planet.MOON)
            Planet.KETU -> listOf(Planet.SUN, Planet.MOON)
            else -> emptyList()
        }
    }

    // ==================== NEECHA BHANGA (DEBILITATION CANCELLATION) ====================

    /**
     * Check if debilitated planet has Neecha Bhanga (cancellation)
     *
     * Conditions per BPHS:
     * 1. Lord of debilitation sign aspects the debilitated planet
     * 2. Lord of exaltation sign aspects the debilitated planet
     * 3. Debilitated planet is in Kendra from Lagna or Moon
     * 4. Lord of the sign where planet is debilitated is in Kendra from Lagna or Moon
     */
    fun hasNeechaBhanga(pos: PlanetPosition, chart: VedicChart): Boolean {
        // Condition 3: Planet in Kendra from Lagna
        if (pos.house in listOf(1, 4, 7, 10)) return true

        // Condition 4: Lord of debilitation sign in Kendra
        val debilitatedSignLord = pos.sign.ruler
        val lordPos = chart.planetPositions.find { it.planet == debilitatedSignLord }
        if (lordPos != null && lordPos.house in listOf(1, 4, 7, 10)) return true

        return false
    }

    // ==================== COMBUSTION DETECTION ====================

    /**
     * Combustion orbs based on BPHS and Saravali
     */
    fun getCombustionOrb(planet: Planet, isRetrograde: Boolean): Double {
        return when (planet) {
            Planet.MOON -> 12.0
            Planet.MARS -> 17.0
            Planet.MERCURY -> if (isRetrograde) 12.0 else 14.0
            Planet.JUPITER -> 11.0
            Planet.VENUS -> if (isRetrograde) 8.0 else 10.0
            Planet.SATURN -> 15.0
            else -> 0.0
        }
    }

    /**
     * Calculate combustion factor (0.0 = fully combust, 1.0 = not combust)
     */
    fun getCombustionFactor(pos: PlanetPosition, chart: VedicChart): Double {
        if (pos.planet == Planet.SUN || pos.planet in listOf(Planet.RAHU, Planet.KETU)) {
            return 1.0
        }

        val sunPos = chart.planetPositions.find { it.planet == Planet.SUN } ?: return 1.0

        val distance = abs(pos.longitude - sunPos.longitude)
        val normalizedDistance = if (distance > 180) 360 - distance else distance
        val combustionOrb = getCombustionOrb(pos.planet, pos.isRetrograde)

        if (normalizedDistance >= combustionOrb) {
            return 1.0
        }

        // Deep combustion (within 3°)
        if (normalizedDistance <= 3.0) {
            return 0.2
        }

        val combustionDepth = 1.0 - (normalizedDistance / combustionOrb)
        return 1.0 - (combustionDepth * 0.6)
    }

    // ==================== PAPAKARTARI & AFFLICTION DETECTION ====================

    /**
     * Check if planet is hemmed between malefics (Papakartari)
     */
    fun isPapakartari(pos: PlanetPosition, chart: VedicChart): Boolean {
        val malefics = listOf(Planet.SATURN, Planet.MARS, Planet.RAHU, Planet.KETU, Planet.SUN)

        val house = pos.house
        val prevHouse = if (house == 1) 12 else house - 1
        val nextHouse = if (house == 12) 1 else house + 1

        val hasMaleficBefore = chart.planetPositions.any {
            it.planet in malefics && it.house == prevHouse
        }
        val hasMaleficAfter = chart.planetPositions.any {
            it.planet in malefics && it.house == nextHouse
        }

        return hasMaleficBefore && hasMaleficAfter
    }

    /**
     * Calculate malefic affliction factor (0.0 = severely afflicted, 1.0 = not afflicted)
     */
    fun getMaleficAfflictionFactor(pos: PlanetPosition, chart: VedicChart): Double {
        val malefics = mapOf(
            Planet.SATURN to 0.25,
            Planet.MARS to 0.20,
            Planet.RAHU to 0.18,
            Planet.KETU to 0.12,
            Planet.SUN to 0.08
        )

        var totalAffliction = 0.0

        malefics.forEach { (malefic, strength) ->
            if (malefic == pos.planet) return@forEach

            val maleficPos = chart.planetPositions.find { it.planet == malefic } ?: return@forEach

            if (isAspecting(maleficPos, pos)) {
                totalAffliction += strength
            }
        }

        return (1.0 - min(totalAffliction, 0.6))
    }

    // ==================== VEDIC ASPECT CALCULATIONS ====================

    /**
     * Check if aspectingPlanet aspects targetPlanet using Vedic aspect rules
     *
     * Vedic Aspect Rules:
     * - All planets aspect 7th house (opposition)
     * - Mars: additional aspects on 4th and 8th houses
     * - Jupiter: additional aspects on 5th and 9th houses
     * - Saturn: additional aspects on 3rd and 10th houses
     * - Rahu/Ketu: aspect like Saturn (some traditions)
     */
    fun isAspecting(aspectingPlanet: PlanetPosition, targetPlanet: PlanetPosition): Boolean {
        val aspectOrb = 5.0
        val houseDistance = getHouseFrom(targetPlanet.sign, aspectingPlanet.sign)

        // All planets have 7th house aspect
        if (houseDistance == 7) {
            return isWithinAspectOrb(aspectingPlanet.longitude, targetPlanet.longitude, 180.0, aspectOrb)
        }

        // Mars special aspects: 4th and 8th houses
        if (aspectingPlanet.planet == Planet.MARS) {
            if (houseDistance == 4) {
                return isWithinAspectOrb(aspectingPlanet.longitude, targetPlanet.longitude, 90.0, aspectOrb)
            }
            if (houseDistance == 8) {
                return isWithinAspectOrb(aspectingPlanet.longitude, targetPlanet.longitude, 210.0, aspectOrb)
            }
        }

        // Jupiter special aspects: 5th and 9th houses
        if (aspectingPlanet.planet == Planet.JUPITER) {
            if (houseDistance == 5) {
                return isWithinAspectOrb(aspectingPlanet.longitude, targetPlanet.longitude, 120.0, aspectOrb)
            }
            if (houseDistance == 9) {
                return isWithinAspectOrb(aspectingPlanet.longitude, targetPlanet.longitude, 240.0, aspectOrb)
            }
        }

        // Saturn special aspects: 3rd and 10th houses
        if (aspectingPlanet.planet == Planet.SATURN) {
            if (houseDistance == 3) {
                return isWithinAspectOrb(aspectingPlanet.longitude, targetPlanet.longitude, 60.0, aspectOrb)
            }
            if (houseDistance == 10) {
                return isWithinAspectOrb(aspectingPlanet.longitude, targetPlanet.longitude, 270.0, aspectOrb)
            }
        }

        // Rahu/Ketu aspects like Saturn
        if (aspectingPlanet.planet in listOf(Planet.RAHU, Planet.KETU)) {
            if (houseDistance == 3 || houseDistance == 10) {
                return true
            }
        }

        return false
    }

    /**
     * Check if two longitudes are within orb of expected aspect angle
     */
    private fun isWithinAspectOrb(
        long1: Double,
        long2: Double,
        expectedAngle: Double,
        orb: Double
    ): Boolean {
        val actualAngle = abs(long1 - long2)
        val normalizedAngle = if (actualAngle > 180) 360 - actualAngle else actualAngle
        return abs(normalizedAngle - expectedAngle) <= orb
    }

    // ==================== BENEFIC ASPECT BOOST ====================

    /**
     * Calculate benefic aspect boost factor (1.0 = no boost, up to 1.3 = strong boost)
     */
    fun getBeneficAspectBoost(pos: PlanetPosition, chart: VedicChart): Double {
        val benefics = mapOf(
            Planet.JUPITER to 0.15,
            Planet.VENUS to 0.10,
            Planet.MERCURY to 0.08,
            Planet.MOON to 0.05
        )

        var totalBoost = 0.0

        benefics.forEach { (benefic, strength) ->
            if (benefic == pos.planet) return@forEach

            val beneficPos = chart.planetPositions.find { it.planet == benefic } ?: return@forEach

            // Skip weak Moon
            if (benefic == Planet.MOON) {
                val moonStrength = getMoonPhaseStrength(beneficPos, chart)
                if (moonStrength < 0.5) return@forEach
            }

            // Skip combust Mercury
            if (benefic == Planet.MERCURY) {
                val combustionFactor = getCombustionFactor(beneficPos, chart)
                if (combustionFactor < 0.6) return@forEach
            }

            if (isAspecting(beneficPos, pos)) {
                totalBoost += strength
            }
        }

        return 1.0 + min(totalBoost, 0.3)
    }

    /**
     * Calculate Moon phase strength (Paksha Bala)
     * 0.0 = new moon, 1.0 = full moon
     */
    fun getMoonPhaseStrength(moonPos: PlanetPosition, chart: VedicChart): Double {
        val sunPos = chart.planetPositions.find { it.planet == Planet.SUN } ?: return 0.5

        val distance = (moonPos.longitude - sunPos.longitude + 360) % 360

        return if (distance <= 180) {
            distance / 180.0
        } else {
            (360 - distance) / 180.0
        }
    }

    // ==================== COMPREHENSIVE STRENGTH CALCULATIONS ====================

    /**
     * Calculate yoga strength with comprehensive factors
     *
     * Base factors:
     * - Exaltation: +15%
     * - Own sign: +12%
     * - Friend's sign: +6%
     * - Kendra/Trikona: +8%
     * - Wealth houses (2,11): +4%
     * - Debilitation: -15%
     * - Dusthana (6,8,12): -10%
     * - Retrograde benefics: +5%
     * - Dig Bala: +7%
     */
    fun calculateYogaStrength(chart: VedicChart, positions: List<PlanetPosition>): Double {
        var baseStrength = 50.0

        positions.forEach { pos ->
            if (isExalted(pos)) baseStrength += 15.0
            if (isInOwnSign(pos)) baseStrength += 12.0
            if (isInFriendSign(pos)) baseStrength += 6.0
            if (pos.house in listOf(1, 4, 5, 7, 9, 10)) baseStrength += 8.0
            if (pos.house in listOf(2, 11)) baseStrength += 4.0
            if (isDebilitated(pos)) baseStrength -= 15.0
            if (pos.house in listOf(6, 8, 12)) baseStrength -= 10.0

            if (pos.isRetrograde) {
                when (pos.planet) {
                    Planet.JUPITER, Planet.VENUS, Planet.MERCURY -> baseStrength += 5.0
                    Planet.SATURN -> baseStrength += 3.0
                    Planet.MARS -> baseStrength -= 2.0
                    else -> {}
                }
            }

            if (hasDigBala(pos)) baseStrength += 7.0
        }

        // Apply cancellation factors
        val (cancellationFactor, _) = calculateCancellationFactor(positions, chart)
        val adjustedStrength = baseStrength * cancellationFactor

        return adjustedStrength.coerceIn(10.0, 100.0)
    }

    /**
     * Calculate strength with reasons for UI display
     */
    fun calculateYogaStrengthWithReasons(
        chart: VedicChart,
        positions: List<PlanetPosition>
    ): Pair<Double, List<String>> {
        var baseStrength = 50.0

        positions.forEach { pos ->
            if (isExalted(pos)) baseStrength += 15.0
            if (isInOwnSign(pos)) baseStrength += 12.0
            if (isInFriendSign(pos)) baseStrength += 6.0
            if (pos.house in listOf(1, 4, 5, 7, 9, 10)) baseStrength += 8.0
            if (pos.house in listOf(2, 11)) baseStrength += 4.0
            if (isDebilitated(pos)) baseStrength -= 15.0
            if (pos.house in listOf(6, 8, 12)) baseStrength -= 10.0

            if (pos.isRetrograde) {
                when (pos.planet) {
                    Planet.JUPITER, Planet.VENUS, Planet.MERCURY -> baseStrength += 5.0
                    Planet.SATURN -> baseStrength += 3.0
                    Planet.MARS -> baseStrength -= 2.0
                    else -> {}
                }
            }

            if (hasDigBala(pos)) baseStrength += 7.0
        }

        val (cancellationFactor, cancellationReasons) = calculateCancellationFactor(positions, chart)
        val adjustedStrength = (baseStrength * cancellationFactor).coerceIn(10.0, 100.0)

        return Pair(adjustedStrength, cancellationReasons)
    }

    /**
     * Calculate net strength modification factor considering all cancellation factors
     *
     * Factors:
     * 1. Combustion (Asta)
     * 2. Papakartari
     * 3. Malefic aspects
     * 4. Debilitation without cancellation
     * 5. Enemy sign placement
     * 6. Benefic aspects (positive boost)
     */
    fun calculateCancellationFactor(
        positions: List<PlanetPosition>,
        chart: VedicChart
    ): Pair<Double, List<String>> {
        val cancellationFactors = mutableListOf<String>()
        var netFactor = 1.0

        positions.forEach { pos ->
            // Combustion check
            val combustionFactor = getCombustionFactor(pos, chart)
            if (combustionFactor < 0.9) {
                netFactor *= combustionFactor
                if (combustionFactor < 0.5) {
                    cancellationFactors.add("${pos.planet.displayName} is deeply combust")
                } else if (combustionFactor < 0.8) {
                    cancellationFactors.add("${pos.planet.displayName} is combust")
                }
            }

            // Papakartari check
            if (isPapakartari(pos, chart)) {
                netFactor *= 0.7
                cancellationFactors.add("${pos.planet.displayName} hemmed between malefics")
            }

            // Malefic aspects check
            val afflictionFactor = getMaleficAfflictionFactor(pos, chart)
            if (afflictionFactor < 0.9) {
                netFactor *= afflictionFactor
                if (afflictionFactor < 0.7) {
                    cancellationFactors.add("${pos.planet.displayName} severely afflicted by malefics")
                }
            }

            // Debilitation check
            if (isDebilitated(pos)) {
                if (!hasNeechaBhanga(pos, chart)) {
                    netFactor *= 0.5
                    cancellationFactors.add("${pos.planet.displayName} debilitated without cancellation")
                }
            }

            // Enemy sign check
            if (isInEnemySign(pos)) {
                netFactor *= 0.85
                cancellationFactors.add("${pos.planet.displayName} in enemy sign")
            }

            // Benefic aspect boost
            val beneficBoost = getBeneficAspectBoost(pos, chart)
            if (beneficBoost > 1.0) {
                netFactor *= beneficBoost
            }
        }

        return Pair(netFactor.coerceIn(0.1, 1.5), cancellationFactors)
    }

    // ==================== MAHAPURUSHA STRENGTH ====================

    /**
     * Calculate Pancha Mahapurusha Yoga strength with proper cancellation logic
     */
    fun calculateMahapurushaStrength(pos: PlanetPosition, chart: VedicChart): Double {
        var strength = 70.0

        when (pos.house) {
            1 -> strength += 15.0
            10 -> strength += 12.0
            7 -> strength += 10.0
            4 -> strength += 8.0
        }

        if (hasDigBala(pos)) strength += 5.0

        val combustionFactor = getCombustionFactor(pos, chart)
        if (combustionFactor < 1.0) {
            strength *= combustionFactor
        }

        strength *= getBeneficAspectBoost(pos, chart)

        val afflictionFactor = getMaleficAfflictionFactor(pos, chart)
        strength *= afflictionFactor

        if (isPapakartari(pos, chart)) {
            strength *= 0.75
        }

        // Check placement from Moon
        val moonPos = chart.planetPositions.find { it.planet == Planet.MOON }
        if (moonPos != null) {
            val houseFromMoon = getHouseFrom(pos.sign, moonPos.sign)
            if (houseFromMoon in listOf(6, 8, 12)) {
                strength *= 0.85
            }
            if (houseFromMoon in listOf(1, 4, 7, 10)) {
                strength *= 1.1
            }
        }

        return strength.coerceIn(30.0, 100.0)
    }

    /**
     * Calculate Mahapurusha strength with cancellation reasons
     */
    fun calculateMahapurushaStrengthWithReasons(
        pos: PlanetPosition,
        chart: VedicChart
    ): Pair<Double, List<String>> {
        val cancellations = mutableListOf<String>()
        var strength = 70.0

        when (pos.house) {
            1 -> strength += 15.0
            10 -> strength += 12.0
            7 -> strength += 10.0
            4 -> strength += 8.0
        }

        if (hasDigBala(pos)) strength += 5.0

        val combustionFactor = getCombustionFactor(pos, chart)
        if (combustionFactor < 1.0) {
            strength *= combustionFactor
            if (combustionFactor < 0.6) {
                cancellations.add("${pos.planet.displayName} is combust - yoga significantly weakened")
            }
        }

        strength *= getBeneficAspectBoost(pos, chart)

        val afflictionFactor = getMaleficAfflictionFactor(pos, chart)
        if (afflictionFactor < 0.85) {
            strength *= afflictionFactor
            cancellations.add("Malefic aspects reduce yoga results")
        }

        if (isPapakartari(pos, chart)) {
            strength *= 0.75
            cancellations.add("Planet hemmed between malefics")
        }

        val moonPos = chart.planetPositions.find { it.planet == Planet.MOON }
        if (moonPos != null) {
            val houseFromMoon = getHouseFrom(pos.sign, moonPos.sign)
            if (houseFromMoon in listOf(6, 8, 12)) {
                strength *= 0.85
                cancellations.add("Weak position from Moon")
            } else if (houseFromMoon in listOf(1, 4, 7, 10)) {
                strength *= 1.1
            }
        }

        return Pair(strength.coerceIn(30.0, 100.0), cancellations)
    }

    // ==================== STRENGTH LEVEL CONVERSION ====================

    /**
     * Convert percentage to YogaStrength enum
     */
    fun strengthFromPercentage(percentage: Double): YogaStrength {
        return YogaStrength.fromPercentage(percentage)
    }
}
