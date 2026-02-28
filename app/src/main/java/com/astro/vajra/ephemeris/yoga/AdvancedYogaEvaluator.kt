package com.astro.vajra.ephemeris.yoga

import com.astro.vajra.core.model.Planet
import com.astro.vajra.core.model.PlanetPosition
import com.astro.vajra.core.model.VedicChart
import com.astro.vajra.core.model.ZodiacSign

/**
 * Advanced Yoga Evaluator - Rare and powerful combinations
 */
class AdvancedYogaEvaluator : YogaEvaluator {

    override val category: YogaCategory = YogaCategory.SPECIAL_YOGA

    override fun evaluate(chart: VedicChart): List<Yoga> {
        val yogas = mutableListOf<Yoga>()
        val ascendantSign = ZodiacSign.fromLongitude(chart.ascendant)
        val houseLords = YogaHelpers.getHouseLords(ascendantSign)

        // 1. Lakshmi Yoga
        evaluateLakshmiYoga(chart, houseLords)?.let { yogas.add(it) }

        // 2. Gouri Yoga
        evaluateGouriYoga(chart, houseLords)?.let { yogas.add(it) }

        // 3. Bharathi Yoga
        evaluateBharathiYoga(chart, houseLords)?.let { yogas.add(it) }

        // 4. Chapa Yoga
        evaluateChapaYoga(chart, houseLords)?.let { yogas.add(it) }

        // 5. Shrinatha Yoga (if not already in special)
        evaluateShrinathaYoga(chart, houseLords)?.let { yogas.add(it) }

        return yogas
    }

    private fun evaluateLakshmiYoga(chart: VedicChart, houseLords: Map<Int, Planet>): Yoga? {
        val lagnaLord = houseLords[1] ?: return null
        val lord9 = houseLords[9] ?: return null
        
        val lagnaLordPos = chart.planetPositions.find { it.planet == lagnaLord } ?: return null
        val lord9Pos = chart.planetPositions.find { it.planet == lord9 } ?: return null
        
        // Lagna lord strong and 9th lord in Kendra/Own/Exalted
        val lagnaStrong = YogaHelpers.isExalted(lagnaLordPos) || YogaHelpers.isInOwnSign(lagnaLordPos)
        val lord9Kendra = lord9Pos.house in listOf(1, 4, 7, 10)
        val lord9Strong = YogaHelpers.isExalted(lord9Pos) || YogaHelpers.isInOwnSign(lord9Pos)
        
        if (lagnaStrong && lord9Kendra && lord9Strong) {
            val strength = YogaHelpers.calculateYogaStrength(chart, listOf(lagnaLordPos, lord9Pos))
            return Yoga(
                name = "Lakshmi Yoga",
                sanskritName = "Lakshmi Yoga",
                category = YogaCategory.DHANA_YOGA,
                planets = listOf(lagnaLord, lord9),
                houses = listOf(lagnaLordPos.house, lord9Pos.house),
                description = "Lagna lord strong and 9th lord in Kendra in own/exalted sign",
                effects = "Wealthy, handsome, learned, noble, high status, blessed by Goddess Lakshmi",
                strength = YogaHelpers.strengthFromPercentage(strength),
                strengthPercentage = strength,
                isAuspicious = true,
                activationPeriod = "Lagna or 9th lord periods",
                cancellationFactors = emptyList()
            )
        }
        return null
    }

    private fun evaluateGouriYoga(chart: VedicChart, houseLords: Map<Int, Planet>): Yoga? {
        val lord9 = houseLords[9] ?: return null
        val moonPos = chart.planetPositions.find { it.planet == Planet.MOON } ?: return null
        
        // Lord of 9th house from Lagna in Kendra or Trikona from Moon
        val lord9Pos = chart.planetPositions.find { it.planet == lord9 } ?: return null
        val houseFromMoon = YogaHelpers.getHouseFrom(lord9Pos.sign, moonPos.sign)
        
        if (houseFromMoon in listOf(1, 4, 7, 10, 5, 9) && (YogaHelpers.isExalted(lord9Pos) || YogaHelpers.isInOwnSign(lord9Pos))) {
            val strength = YogaHelpers.calculateYogaStrength(chart, listOf(lord9Pos, moonPos))
            return Yoga(
                name = "Gouri Yoga",
                sanskritName = "Gouri Yoga",
                category = YogaCategory.SPECIAL_YOGA,
                planets = listOf(lord9, Planet.MOON),
                houses = listOf(lord9Pos.house, moonPos.house),
                description = "9th lord in Kendra/Trikona from Moon and strong",
                effects = "Beautiful, religious, wealthy, famous, blessed with a good family",
                strength = YogaHelpers.strengthFromPercentage(strength),
                strengthPercentage = strength,
                isAuspicious = true,
                activationPeriod = "9th lord or Moon periods",
                cancellationFactors = emptyList()
            )
        }
        return null
    }

    private fun evaluateBharathiYoga(chart: VedicChart, houseLords: Map<Int, Planet>): Yoga? {
        val lord2 = houseLords[2] ?: return null
        val lord5 = houseLords[5] ?: return null
        val lord9 = houseLords[9] ?: return null
        val jupiterPos = chart.planetPositions.find { it.planet == Planet.JUPITER } ?: return null
        
        val lord2Pos = chart.planetPositions.find { it.planet == lord2 } ?: return null
        val lord5Pos = chart.planetPositions.find { it.planet == lord5 } ?: return null
        val lord9Pos = chart.planetPositions.find { it.planet == lord9 } ?: return null
        
        // Lords of 2nd, 5th, 9th in Kendra and Jupiter exalted/own
        val allInKendra = lord2Pos.house in listOf(1, 4, 7, 10) && 
                         lord5Pos.house in listOf(1, 4, 7, 10) && 
                         lord9Pos.house in listOf(1, 4, 7, 10)
        
        val jupiterStrong = YogaHelpers.isExalted(jupiterPos) || YogaHelpers.isInOwnSign(jupiterPos)
        
        if (allInKendra && jupiterStrong) {
            val strength = YogaHelpers.calculateYogaStrength(chart, listOf(lord2Pos, lord5Pos, lord9Pos, jupiterPos))
            return Yoga(
                name = "Bharathi Yoga",
                sanskritName = "Bharathi Yoga",
                category = YogaCategory.SPECIAL_YOGA,
                planets = listOf(lord2, lord5, lord9, Planet.JUPITER),
                houses = listOf(lord2Pos.house, lord5Pos.house, lord9Pos.house),
                description = "Lords of 2, 5, 9 in Kendra and Jupiter strong",
                effects = "Exceptional intelligence, scholarship, fame, wealth, religious nature",
                strength = YogaHelpers.strengthFromPercentage(strength),
                strengthPercentage = strength,
                isAuspicious = true,
                activationPeriod = "Jupiter or house lord periods",
                cancellationFactors = emptyList()
            )
        }
        return null
    }

    private fun evaluateChapaYoga(chart: VedicChart, houseLords: Map<Int, Planet>): Yoga? {
        val lord4 = houseLords[4] ?: return null
        val lord10 = houseLords[10] ?: return null
        val lagnaLord = houseLords[1] ?: return null
        
        val lord4Pos = chart.planetPositions.find { it.planet == lord4 } ?: return null
        val lord10Pos = chart.planetPositions.find { it.planet == lord10 } ?: return null
        val lagnaLordPos = chart.planetPositions.find { it.planet == lagnaLord } ?: return null
        
        // 4th and 10th lords in exchange, and Lagna lord strong
        val exchange = YogaHelpers.areInExchange(lord4Pos, lord10Pos)
        val lagnaStrong = YogaHelpers.isExalted(lagnaLordPos) || YogaHelpers.isInOwnSign(lagnaLordPos)
        
        if (exchange && lagnaStrong) {
            val strength = YogaHelpers.calculateYogaStrength(chart, listOf(lord4Pos, lord10Pos, lagnaLordPos))
            return Yoga(
                name = "Chapa Yoga",
                sanskritName = "Chapa Yoga",
                category = YogaCategory.RAJA_YOGA,
                planets = listOf(lord4, lord10, lagnaLord),
                houses = listOf(lord4Pos.house, lord10Pos.house),
                description = "Exchange of 4th and 10th lords with strong Lagna lord",
                effects = "High status in government or military, authority, wealth, property",
                strength = YogaHelpers.strengthFromPercentage(strength),
                strengthPercentage = strength,
                isAuspicious = true,
                activationPeriod = "4th or 10th lord periods",
                cancellationFactors = emptyList()
            )
        }
        return null
    }

    private fun evaluateShrinathaYoga(chart: VedicChart, houseLords: Map<Int, Planet>): Yoga? {
        val lord7 = houseLords[7] ?: return null
        val lord9 = houseLords[9] ?: return null
        
        // 7th lord in 10th, 10th lord conjunct 9th lord
        val lord7Pos = chart.planetPositions.find { it.planet == lord7 } ?: return null
        val lord9Pos = chart.planetPositions.find { it.planet == lord9 } ?: return null
        val lord10 = houseLords[10] ?: return null
        val lord10Pos = chart.planetPositions.find { it.planet == lord10 } ?: return null
        
        if (lord7Pos.house == 10 && YogaHelpers.areConjunct(lord10Pos, lord9Pos)) {
            val strength = YogaHelpers.calculateYogaStrength(chart, listOf(lord7Pos, lord9Pos, lord10Pos))
            return Yoga(
                name = "Shrinatha Yoga",
                sanskritName = "Shrinatha Yoga",
                category = YogaCategory.SPECIAL_YOGA,
                planets = listOf(lord7, lord9, lord10),
                houses = listOf(10),
                description = "7th lord in 10th house, and 9th & 10th lords conjunct",
                effects = "Splendorous life, wealthy, famous, religious, blessed by Lord Vishnu",
                strength = YogaHelpers.strengthFromPercentage(strength),
                strengthPercentage = strength,
                isAuspicious = true,
                activationPeriod = "Lord 7, 9, or 10 periods",
                cancellationFactors = emptyList()
            )
        }
        return null
    }
}