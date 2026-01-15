package com.astro.storm.ephemeris.prashna

import com.astro.storm.core.common.Language
import com.astro.storm.core.common.StringKeyAnalysis
import com.astro.storm.core.common.StringResources
import com.astro.storm.core.model.Planet
import com.astro.storm.core.model.PlanetPosition
import com.astro.storm.core.model.VedicChart
import com.astro.storm.core.model.ZodiacSign
import com.astro.storm.ephemeris.prashna.PrashnaConstants.NATURAL_BENEFICS
import com.astro.storm.ephemeris.prashna.PrashnaHelpers.angularDistance
import com.astro.storm.ephemeris.prashna.PrashnaHelpers.calculateArudhaLagna
import com.astro.storm.ephemeris.prashna.PrashnaHelpers.calculateNavamshaSign
import com.astro.storm.ephemeris.prashna.PrashnaHelpers.getCombustionOrb
import com.astro.storm.ephemeris.prashna.PrashnaHelpers.isAspecting
import com.astro.storm.ephemeris.prashna.PrashnaHelpers.isAspectingHouse
import com.astro.storm.ephemeris.prashna.PrashnaHelpers.localized

object PrashnaLagnaAnalyzer {

    fun analyzeLagna(chart: VedicChart, language: Language): LagnaAnalysis {
        val lagnaSign = ZodiacSign.fromLongitude(chart.ascendant)
        val lagnaLord = lagnaSign.ruler
        val lagnaLordPosition = chart.planetPositions.first { it.planet == lagnaLord }
        val lagnaLordStrength = calculatePlanetStrength(lagnaLordPosition, chart)
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
        val planetsInLagna = chart.planetPositions.filter { it.house == 1 }
        val lagnaCondition = when {
            lagnaLordStrength.isCombust -> LagnaCondition.COMBUST
            lagnaLordStrength.isRetrograde -> LagnaCondition.RETROGRADE_LORD
            lagnaLordStrength.overallStrength.value >= 4 -> LagnaCondition.STRONG
            lagnaLordStrength.overallStrength.value >= 2 -> LagnaCondition.MODERATE
            else -> LagnaCondition.WEAK
        }
        val arudhaLagna = calculateArudhaLagna(lagnaLord, lagnaLordPosition, chart)
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

    fun calculatePlanetStrength(
        position: PlanetPosition,
        chart: VedicChart
    ): PlanetStrength {
        val planet = position.planet
        val sign = position.sign
        val exaltationSigns = mapOf(
            Planet.SUN to ZodiacSign.ARIES,
            Planet.MOON to ZodiacSign.TAURUS,
            Planet.MARS to ZodiacSign.CAPRICORN,
            Planet.MERCURY to ZodiacSign.VIRGO,
            Planet.JUPITER to ZodiacSign.CANCER,
            Planet.VENUS to ZodiacSign.PISCES,
            Planet.SATURN to ZodiacSign.LIBRA
        )
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
        val sunPosition = chart.planetPositions.first { it.planet == Planet.SUN }
        val distanceFromSun = angularDistance(position.longitude, sunPosition.longitude)
        val combustionOrb = getCombustionOrb(planet)
        val isCombust = planet != Planet.SUN && distanceFromSun < combustionOrb
        val navamshaSign = calculateNavamshaSign(position.longitude)
        val isVargottama = sign == navamshaSign
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
}
