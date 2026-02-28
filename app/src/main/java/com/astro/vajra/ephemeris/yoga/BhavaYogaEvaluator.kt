package com.astro.vajra.ephemeris.yoga

import com.astro.vajra.core.common.StringKeyYogaExpanded
import com.astro.vajra.core.model.VedicChart
import com.astro.vajra.core.model.ZodiacSign
import com.astro.vajra.core.model.Planet

/**
 * Bhava Yoga Evaluator - House Lord Placements
 *
 * Evaluates the 144 Bhava Yogas (12 House Lords x 12 Houses).
 * These yogas describe the results of a house lord being placed in a specific house.
 *
 * Logic covers:
 * - Lord of House X in House Y
 * - Dignity of the lord (Exalted, Own, Friend, Enemy, Debilitated)
 * - Nature of the connection (Kendra, Trikona, Dusthana)
 *
 * @author AstroVajra
 */
class BhavaYogaEvaluator : YogaEvaluator {

    override val category: YogaCategory = YogaCategory.BHAVA_YOGA

    override fun evaluate(chart: VedicChart): List<Yoga> {
        val yogas = mutableListOf<Yoga>()
        val ascendantSign = ZodiacSign.fromLongitude(chart.ascendant)
        val houseLords = YogaHelpers.getHouseLords(ascendantSign)

        // Iterate through all 12 lords
        for (lordHouse in 1..12) {
            val lordPlanet = houseLords[lordHouse] ?: continue
            val lordPos = chart.planetPositions.find { it.planet == lordPlanet } ?: continue
            
            val placementHouse = lordPos.house
            
            // Create the yoga
            val yoga = createBhavaYoga(chart, lordHouse, placementHouse, lordPos, lordPlanet)
            yogas.add(yoga)
        }

        return yogas
    }

    private fun createBhavaYoga(
        chart: VedicChart,
        lordHouse: Int,
        placementHouse: Int,
        lordPos: com.astro.vajra.core.model.PlanetPosition,
        lordPlanet: Planet
    ): Yoga {
        // Determine Auspiciousness
        val isDusthanaPlacement = placementHouse in listOf(6, 8, 12)
        val isDusthanaLord = lordHouse in listOf(6, 8, 12)
        
        // Viparita Raja Yoga logic is handled elsewhere, but for general Bhava results:
        // Dusthana lord in Dusthana is generally good (Viparita)
        // Kendra/Trikona lord in Dusthana is weak
        // Any lord in Kendra/Trikona is generally strong
        
        var isAuspicious = true
        var strengthVal = 50.0 // Base
        
        // Modify strength based on dignity
        if (YogaHelpers.isExalted(lordPos)) strengthVal += 30.0
        else if (YogaHelpers.isInOwnSign(lordPos)) strengthVal += 20.0
        else if (YogaHelpers.isInFriendSign(lordPos)) strengthVal += 10.0
        else if (YogaHelpers.isInEnemySign(lordPos)) {
            strengthVal -= 10.0
            isAuspicious = false // Tend towards negative
        }
        else if (YogaHelpers.isDebilitated(lordPos)) {
            strengthVal -= 20.0
            isAuspicious = false
        }
        
        // Modify based on house placement
        if (placementHouse in listOf(1, 4, 7, 10, 5, 9)) {
            strengthVal += 10.0
        } else if (placementHouse in listOf(6, 8, 12)) {
            if (!isDusthanaLord) {
                strengthVal -= 15.0
                isAuspicious = false
            } else {
                // Dusthana lord in Dusthana -> Viparita -> Positive/Strong
                strengthVal += 10.0 
                isAuspicious = true 
            }
        }
        
        // Determine String Keys
        // Title: "Lord of X in Y"
        // We use the generic key and formatting in the UI/Text generation phase, 
        // but here we just assign the yoga name.
        // Since we need localized Name, we can't easily put "Lord of 1 in 1" as pure string.
        // The Yoga data class takes a string name. 
        // We will store the internal code and let localization handle it, 
        // OR construct a localized-ready string if the system supports it.
        // The system seems to use `Yoga.name` as a key or display text.
        // Looking at `YogaAnalysis.toPlainText`, it tries `YogaLocalization.getLocalizedYogaName`.
        // So we should give a standard English name like "Lord of 1 in 1".
        
        val yogaName = "Lord of $lordHouse in House $placementHouse"
        
        // Specific effects for Lord 1 are defined
        val descriptionKey = if (lordHouse == 1) {
            when (placementHouse) {
                1 -> StringKeyYogaExpanded.EFFECT_LORD_1_IN_1
                2 -> StringKeyYogaExpanded.EFFECT_LORD_1_IN_2
                3 -> StringKeyYogaExpanded.EFFECT_LORD_1_IN_3
                4 -> StringKeyYogaExpanded.EFFECT_LORD_1_IN_4
                5 -> StringKeyYogaExpanded.EFFECT_LORD_1_IN_5
                6 -> StringKeyYogaExpanded.EFFECT_LORD_1_IN_6
                7 -> StringKeyYogaExpanded.EFFECT_LORD_1_IN_7
                8 -> StringKeyYogaExpanded.EFFECT_LORD_1_IN_8
                9 -> StringKeyYogaExpanded.EFFECT_LORD_1_IN_9
                10 -> StringKeyYogaExpanded.EFFECT_LORD_1_IN_10
                11 -> StringKeyYogaExpanded.EFFECT_LORD_1_IN_11
                12 -> StringKeyYogaExpanded.EFFECT_LORD_1_IN_12
                else -> StringKeyYogaExpanded.EFFECT_GENERIC_MIXED
            }
        } else {
            // For others, we use generic good/bad
            if (isAuspicious) StringKeyYogaExpanded.EFFECT_GENERIC_GOOD
            else StringKeyYogaExpanded.EFFECT_GENERIC_BAD
        }
        
        val defaultEffects = descriptionKey.en
            .replace("{0}", "House $lordHouse")
            .replace("{1}", "House $placementHouse")
            
        return Yoga(
            name = yogaName,
            sanskritName = "Bhava Yoga $lordHouse-$placementHouse",
            category = YogaCategory.BHAVA_YOGA,
            planets = listOf(lordPlanet),
            houses = listOf(placementHouse),
            description = "Placement of the ${lordHouse}th House Lord in the ${placementHouse}th House",
            effects = defaultEffects,
            strength = YogaHelpers.strengthFromPercentage(strengthVal.coerceIn(0.0, 100.0)),
            strengthPercentage = strengthVal.coerceIn(0.0, 100.0),
            isAuspicious = isAuspicious,
            activationPeriod = "${lordPlanet.displayName} Dasha",
            cancellationFactors = emptyList(),
            
            // Add high-precision keys
            nameKey = StringKeyYogaExpanded.YOGA_BHAVA_LORD_PLACEMENT,
            effectsKey = descriptionKey
        )
    }
}
