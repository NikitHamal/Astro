package com.astro.storm.ephemeris.yoga

import com.astro.storm.core.model.Planet
import com.astro.storm.core.model.PlanetPosition
import com.astro.storm.core.model.VedicChart
import com.astro.storm.core.model.ZodiacSign
import com.astro.storm.ephemeris.VedicAstrologyUtils
import kotlin.math.abs
import kotlin.math.min

/**
 * Yoga Helpers - Shared utility functions for Yoga calculations
 *
 * Contains all helper functions used across yoga evaluators:
 * - Conjunction detection (areConjunct, areMutuallyAspecting, areInExchange)
 * - Strength calculations (calculateYogaStrength, getCombustionFactor)
 * - Dignity checks (isExalted, isDebilitated, isInOwnSign)
 * - House utilities (getHouseLords, getHouseFrom, isInKendraFrom)
 *
 * Based on classical texts:
 * - Brihat Parasara Hora Shastra (BPHS)
 * - Phaladeepika
 * - Saravali
 *
 * @author AstroStorm
 */
object YogaHelpers {

    // ==================== HOUSE LORD UTILITIES ====================

    /**
     * Get house lords based on ascendant sign
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

    /**
     * Calculate the house position of a sign from a reference sign
     * Returns 1-12 where 1 means same sign
     */
    fun getHouseFrom(targetSign: ZodiacSign, referenceSign: ZodiacSign): Int {
        val diff = targetSign.number - referenceSign.number
        return if (diff >= 0) diff + 1 else diff + 13
    }

    /**
     * Check if a planet is in Kendra (1, 4, 7, 10) from a reference position
     */
    fun isInKendraFrom(pos: PlanetPosition, reference: PlanetPosition): Boolean {
        val house = getHouseFrom(pos.sign, reference.sign)
        return house in listOf(1, 4, 7, 10)
    }

    // ==================== CONJUNCTION & ASPECT UTILITIES ====================

    /**
     * Check if two planets are conjunct within an orb
     * Default orb is 8° as per Vedic standards
     */
    fun areConjunct(pos1: PlanetPosition, pos2: PlanetPosition, customOrb: Double? = null): Boolean {
        val distance = abs(pos1.longitude - pos2.longitude)
        val normalizedDistance = if (distance > 180) 360 - distance else distance
        val orb = customOrb ?: 8.0
        return normalizedDistance <= orb
    }

    /**
     * Check if two planets are mutually aspecting (opposition aspect primarily)
     */
    fun areMutuallyAspecting(pos1: PlanetPosition, pos2: PlanetPosition): Boolean {
        val angle = abs(pos1.longitude - pos2.longitude)
        val normalizedAngle = if (angle > 180) 360 - angle else angle
        return normalizedAngle in 170.0..190.0
    }

    /**
     * Check if two planets are in mutual exchange (Parivartana)
     */
    fun areInExchange(pos1: PlanetPosition, pos2: PlanetPosition): Boolean {
        return pos1.sign.ruler == pos2.planet && pos2.sign.ruler == pos1.planet
    }

    /**
     * Check if aspectingPlanet aspects targetPlanet using Vedic aspect rules
     * Uses 5° orb for aspect calculations
     */
    fun isAspecting(aspectingPlanet: PlanetPosition, targetPlanet: PlanetPosition): Boolean {
        val aspectOrb = 5.0
        val houseDistance = getHouseFrom(targetPlanet.sign, aspectingPlanet.sign)

        // All planets have 7th house (opposition) aspect
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

        // Rahu/Ketu aspects like Saturn (some traditions)
        if (aspectingPlanet.planet in listOf(Planet.RAHU, Planet.KETU)) {
            if (houseDistance == 3 || houseDistance == 10) {
                return true
            }
        }

        return false
    }

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

    // ==================== DIGNITY UTILITIES ====================

    /**
     * Check if a planet is in its own sign
     */
    fun isInOwnSign(pos: PlanetPosition): Boolean {
        return VedicAstrologyUtils.isInOwnSign(pos)
    }

    /**
     * Check if a planet is exalted
     */
    fun isExalted(pos: PlanetPosition): Boolean {
        return VedicAstrologyUtils.isExalted(pos)
    }

    /**
     * Check if a planet is debilitated
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
     * Check if planet is in a friend's sign
     */
    fun isInFriendSign(pos: PlanetPosition): Boolean {
        return VedicAstrologyUtils.isInFriendSign(pos)
    }

    /**
     * Check if planet is in enemy sign
     */
    fun isInEnemySign(pos: PlanetPosition): Boolean {
        val enemies = getEnemies(pos.planet)
        return pos.sign.ruler in enemies
    }

    /**
     * Get natural enemies of a planet based on BPHS
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

    /**
     * Check if a debilitated planet has Neecha Bhanga (cancellation of debilitation)
     */
    fun hasNeechaBhanga(pos: PlanetPosition, chart: VedicChart): Boolean {
        // Condition 1: Debilitated planet in Kendra from Lagna
        if (pos.house in listOf(1, 4, 7, 10)) return true

        // Condition 2: Lord of debilitation sign in Kendra from Lagna
        val debilitatedSignLord = pos.sign.ruler
        val lordPos = chart.planetPositions.find { it.planet == debilitatedSignLord }
        if (lordPos != null && lordPos.house in listOf(1, 4, 7, 10)) return true

        return false
    }

    // ==================== COMBUSTION UTILITIES ====================

    /**
     * Get combustion orb based on planet and retrograde status
     * Based on BPHS and Saravali
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

    // ==================== AFFLICTION UTILITIES ====================

    /**
     * Check if a planet is under Papakartari Yoga (hemmed between malefics)
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
     * Get malefic affliction factor (0.0 = severely afflicted, 1.0 = not afflicted)
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

    /**
     * Get benefic aspect boost factor (1.0 = no benefic, up to 1.3 = strong benefics)
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

            // Skip Moon if waning
            if (benefic == Planet.MOON) {
                val moonStrength = getMoonPhaseStrength(beneficPos, chart)
                if (moonStrength < 0.5) return@forEach
            }

            // Skip Mercury if combust
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
     * Returns 0.0-1.0 where 1.0 is full moon and 0.0 is new moon
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

    // ==================== STRENGTH CALCULATION ====================

    /**
     * Calculate comprehensive cancellation factor for yoga strength
     * Returns factor and list of cancellation reasons
     */
    fun calculateCancellationFactor(
        positions: List<PlanetPosition>,
        chart: VedicChart
    ): Pair<Double, List<String>> {
        val cancellationFactors = mutableListOf<String>()
        var netFactor = 1.0

        positions.forEach { pos ->
            // Check combustion
            val combustionFactor = getCombustionFactor(pos, chart)
            if (combustionFactor < 0.9) {
                netFactor *= combustionFactor
                if (combustionFactor < 0.5) {
                    cancellationFactors.add("${pos.planet.displayName} is deeply combust")
                } else if (combustionFactor < 0.8) {
                    cancellationFactors.add("${pos.planet.displayName} is combust")
                }
            }

            // Check Papakartari
            if (isPapakartari(pos, chart)) {
                netFactor *= 0.7
                cancellationFactors.add("${pos.planet.displayName} hemmed between malefics")
            }

            // Check malefic aspects
            val afflictionFactor = getMaleficAfflictionFactor(pos, chart)
            if (afflictionFactor < 0.9) {
                netFactor *= afflictionFactor
                if (afflictionFactor < 0.7) {
                    cancellationFactors.add("${pos.planet.displayName} severely afflicted by malefics")
                }
            }

            // Check debilitation
            if (isDebilitated(pos)) {
                if (!hasNeechaBhanga(pos, chart)) {
                    netFactor *= 0.5
                    cancellationFactors.add("${pos.planet.displayName} debilitated without cancellation")
                }
            }

            // Check enemy sign
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

    /**
     * Calculate yoga strength with comprehensive factors applied
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

        val (cancellationFactor, _) = calculateCancellationFactor(positions, chart)
        val adjustedStrength = baseStrength * cancellationFactor

        return adjustedStrength.coerceIn(10.0, 100.0)
    }

    /**
     * Calculate yoga strength with reasons for cancellation
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
     * Convert percentage strength to YogaStrength enum
     */
    fun strengthFromPercentage(percentage: Double): YogaStrength {
        return YogaStrength.fromPercentage(percentage)
    }

    // ==================== ASTRONOMICAL UTILITIES ====================

    /**
     * Check if birth was during the day
     */
    fun isDayBirth(chart: VedicChart): Boolean {
        val sunPos = chart.planetPositions.find { it.planet == Planet.SUN } ?: return true
        // Sun in houses 7-12 is generally above horizon (day) in some systems, 
        // but better to check if house is 7, 8, 9, 10, 11, 12
        return sunPos.house in 7..12
    }

    /**
     * Check if sign number is odd (1, 3, 5, 7, 9, 11)
     */
    fun isOddSign(signNumber: Int): Boolean {
        return signNumber % 2 != 0
    }

    // ==================== HOUSE SIGNIFICATIONS ====================

    /**
     * Get house significations (English fallback)
     */
    fun getHouseSignifications(house: Int): String {
        return when (house) {
            1 -> "self-effort and personality"
            2 -> "family wealth and speech"
            3 -> "courage and communication"
            4 -> "property and domestic comfort"
            5 -> "speculation and creative ventures"
            6 -> "service and defeating competition"
            7 -> "partnership and business"
            8 -> "inheritance and unexpected gains"
            9 -> "fortune and higher pursuits"
            10 -> "career and public recognition"
            11 -> "gains and social networks"
            12 -> "foreign connections and spiritual pursuits"
            else -> "various activities"
        }
    }
}
