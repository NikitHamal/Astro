package com.astro.storm.ephemeris.prashna

import com.astro.storm.core.common.Language
import com.astro.storm.core.common.StringKeyAnalysis
import com.astro.storm.core.common.StringResources
import com.astro.storm.core.model.Planet
import com.astro.storm.core.model.PlanetPosition
import com.astro.storm.core.model.VedicChart
import com.astro.storm.core.model.ZodiacSign
import com.astro.storm.ephemeris.prashna.PrashnaConstants.MOON_DAILY_MOTION
import com.astro.storm.ephemeris.prashna.PrashnaConstants.NATURAL_BENEFICS
import com.astro.storm.ephemeris.prashna.PrashnaConstants.NATURAL_MALEFICS
import com.astro.storm.ephemeris.prashna.PrashnaHelpers.angularDistance
import com.astro.storm.ephemeris.prashna.PrashnaHelpers.getAspectType
import com.astro.storm.ephemeris.prashna.PrashnaHelpers.getTithiName
import com.astro.storm.ephemeris.prashna.PrashnaHelpers.isAspecting
import com.astro.storm.ephemeris.prashna.PrashnaHelpers.localized
import com.astro.storm.ephemeris.prashna.PrashnaHelpers.normalizeDegrees
import java.time.LocalDateTime
import kotlin.math.abs
import kotlin.math.floor

object PrashnaMoonAnalyzer {

    fun analyzeMoon(
        chart: VedicChart,
        questionTime: LocalDateTime,
        language: Language
    ): MoonAnalysis {
        val moonPosition = chart.planetPositions.first { it.planet == Planet.MOON }
        val sunPosition = chart.planetPositions.first { it.planet == Planet.SUN }

        val moonSunDiff = normalizeDegrees(moonPosition.longitude - sunPosition.longitude)
        val tithiNumber = (floor(moonSunDiff / 12.0).toInt() % 30) + 1
        val isWaxing = moonSunDiff < 180.0
        val moonAge = moonSunDiff / MOON_DAILY_MOTION
        val moonStrength = calculateMoonStrength(moonPosition, sunPosition, chart)
        val (isVoid, lastAspect, nextAspect) = checkVoidOfCourse(moonPosition, chart)
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

    private fun calculateMoonStrength(
        moonPosition: PlanetPosition,
        sunPosition: PlanetPosition,
        chart: VedicChart
    ): MoonStrength {
        var score = 3
        val moonSunDiff = normalizeDegrees(moonPosition.longitude - sunPosition.longitude)
        if (moonSunDiff > 90 && moonSunDiff < 270) score += 1
        if (moonSunDiff < 180) score += 1

        when (moonPosition.sign) {
            ZodiacSign.TAURUS -> score += 2
            ZodiacSign.CANCER -> score += 1
            ZodiacSign.SCORPIO -> score -= 2
            else -> {}
        }

        val maleficAspects = chart.planetPositions
            .filter { it.planet in NATURAL_MALEFICS }
            .count { isAspecting(it, moonPosition) }
        score -= maleficAspects

        val beneficAspects = chart.planetPositions
            .filter { it.planet in NATURAL_BENEFICS && it.planet != Planet.MOON }
            .count { isAspecting(it, moonPosition) }
        score += beneficAspects / 2

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

    private fun checkVoidOfCourse(
        moonPosition: PlanetPosition,
        chart: VedicChart
    ): Triple<Boolean, PlanetaryAspect?, PlanetaryAspect?> {
        val currentMoonDegree = moonPosition.longitude
        val moonSignEnd = ((moonPosition.sign.number) * 30.0)
        var lastAspect: PlanetaryAspect? = null
        var nextAspect: PlanetaryAspect? = null
        var willMakeAspect = false

        for (planet in chart.planetPositions) {
            if (planet.planet == Planet.MOON) continue
            val aspectDegrees = listOf(0.0, 60.0, 90.0, 120.0, 180.0)
            for (aspectAngle in aspectDegrees) {
                val targetDegree = normalizeDegrees(planet.longitude + aspectAngle)
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
}


