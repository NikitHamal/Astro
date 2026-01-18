package com.astro.storm.ephemeris.varshaphala

import com.astro.storm.core.common.Language
import com.astro.storm.core.common.StringKeyAnalysis
import com.astro.storm.core.common.StringResources
import com.astro.storm.core.model.Planet
import com.astro.storm.core.model.ZodiacSign
import com.astro.storm.ephemeris.varshaphala.VarshaphalaConstants.CONJUNCTION_ORB
import com.astro.storm.ephemeris.varshaphala.VarshaphalaConstants.OPPOSITION_ORB
import com.astro.storm.ephemeris.varshaphala.VarshaphalaConstants.SEXTILE_ORB
import com.astro.storm.ephemeris.varshaphala.VarshaphalaConstants.SQUARE_ORB
import com.astro.storm.ephemeris.varshaphala.VarshaphalaConstants.TRINE_ORB
import com.astro.storm.ephemeris.varshaphala.VarshaphalaHelpers.normalizeAngle
import kotlin.math.abs
import kotlin.math.min

object TajikaAspectAnalyzer {

    fun calculateTajikaAspects(chart: SolarReturnChart, language: Language): List<TajikaAspectResult> {
        val aspects = mutableListOf<TajikaAspectResult>()
        val planets = listOf(Planet.SUN, Planet.MOON, Planet.MARS, Planet.MERCURY, Planet.JUPITER, Planet.VENUS, Planet.SATURN)
        val angles = listOf(0, 60, 90, 120, 180)

        for (i in planets.indices) {
            for (j in (i + 1) until planets.size) {
                val p1 = planets[i]; val p2 = planets[j]
                val pos1 = chart.planetPositions[p1] ?: continue; val pos2 = chart.planetPositions[p2] ?: continue
                val diff = abs(normalizeAngle(pos1.longitude - pos2.longitude))
                for (angle in angles) {
                    val maxOrb = when (angle) { 0 -> CONJUNCTION_ORB; 60 -> SEXTILE_ORB; 90 -> SQUARE_ORB; 120 -> TRINE_ORB; 180 -> OPPOSITION_ORB; else -> 5.0 }
                    val effectiveOrb = min(abs(diff - angle), abs(diff - (360 - angle)))
                    if (effectiveOrb <= maxOrb) {
                        val isApplying = determineTajikaApplication(pos1.longitude, pos2.longitude, pos1.speed, pos2.speed)
                        val type = determineTajikaAspectType(p1, p2, pos1, pos2, isApplying, effectiveOrb, angle)
                        val strength = calculateAspectStrength(effectiveOrb, maxOrb, angle, isApplying)
                        val relatedHouses = listOf(pos1.house, pos2.house).distinct()
                        aspects.add(TajikaAspectResult(
                            type = type, planet1 = p1, planet2 = p2, planet1Longitude = pos1.longitude, planet2Longitude = pos2.longitude,
                            aspectAngle = angle, orb = effectiveOrb, isApplying = isApplying, strength = strength, relatedHouses = relatedHouses,
                            effectDescription = getAspectEffectDescription(type, p1, p2, language),
                            prediction = generateAspectPrediction(type, p1, p2, relatedHouses, language)
                        ))
                    }
                }
            }
        }
        return aspects.sortedByDescending { it.strength.weight }
    }

    private fun determineTajikaApplication(long1: Double, long2: Double, speed1: Double, speed2: Double): Boolean {
        val diff = normalizeAngle(long2 - long1)
        return if (diff < 180) speed1 > speed2 else speed2 > speed1
    }

    private fun determineTajikaAspectType(p1: Planet, p2: Planet, pos1: SolarReturnPlanetPosition, pos2: SolarReturnPlanetPosition, isApplying: Boolean, orb: Double, angle: Int): TajikaAspectType {
        val isAngular1 = pos1.house in listOf(1, 4, 7, 10); val isAngular2 = pos2.house in listOf(1, 4, 7, 10)
        val hasReception = pos1.sign.ruler == p2 && pos2.sign.ruler == p1
        return when {
            isApplying && angle == 0 && orb < 3 -> if (isAngular1 || isAngular2) TajikaAspectType.KAMBOOLA else TajikaAspectType.ITHASALA
            isApplying && orb < 5 -> if (hasReception) TajikaAspectType.NAKTA else TajikaAspectType.ITHASALA
            !isApplying && orb < 5 -> TajikaAspectType.EASARAPHA
            pos1.isRetrograde || pos2.isRetrograde -> TajikaAspectType.RADDA
            pos1.speed < pos2.speed && isApplying -> TajikaAspectType.MANAU
            isApplying && hasReception -> TajikaAspectType.MUTHASHILA
            angle == 90 || angle == 180 -> TajikaAspectType.DURAPHA
            isAngular1 && isAngular2 && !isApplying -> TajikaAspectType.GAIRI_KAMBOOLA
            else -> if (isApplying) TajikaAspectType.ITHASALA else TajikaAspectType.EASARAPHA
        }
    }

    private fun calculateAspectStrength(orb: Double, maxOrb: Double, angle: Int, isApplying: Boolean): AspectStrength {
        val orbRatio = orb / maxOrb
        val bonus = (if (angle == 0 || angle == 120) 0.2 else if (angle == 60) 0.1 else if (angle == 90 || angle == 180) -0.1 else 0.0) + (if (isApplying) 0.1 else 0.0)
        val s = 1.0 - orbRatio + bonus
        return when { s >= 0.9 -> AspectStrength.VERY_STRONG; s >= 0.7 -> AspectStrength.STRONG; s >= 0.5 -> AspectStrength.MODERATE; s >= 0.3 -> AspectStrength.WEAK; else -> AspectStrength.VERY_WEAK }
    }

    private fun getAspectEffectDescription(type: TajikaAspectType, p1: Planet, p2: Planet, language: Language): String {
        return when (type) {
            TajikaAspectType.ITHASALA -> StringResources.get(StringKeyAnalysis.TAJIKA_ITHASALA_EFFECT, language, p1.getLocalizedName(language), p2.getLocalizedName(language))
            TajikaAspectType.EASARAPHA -> StringResources.get(StringKeyAnalysis.TAJIKA_EASARAPHA_EFFECT, language)
            TajikaAspectType.KAMBOOLA -> StringResources.get(StringKeyAnalysis.TAJIKA_KAMBOOLA_EFFECT, language)
            TajikaAspectType.RADDA -> StringResources.get(StringKeyAnalysis.TAJIKA_RADDA_EFFECT, language)
            TajikaAspectType.DURAPHA -> StringResources.get(StringKeyAnalysis.TAJIKA_DURAPHA_EFFECT, language)
            else -> StringResources.get(StringKeyAnalysis.TAJIKA_INFLUENCE_ENERGY, language, type.getDisplayName(language), if (type.isPositive) StringResources.get(StringKeyAnalysis.VARSHA_TONE_SUPPORTIVE, language) else StringResources.get(StringKeyAnalysis.VARSHA_TONE_CHALLENGING, language))
        }
    }

    private fun generateAspectPrediction(type: TajikaAspectType, p1: Planet, p2: Planet, houses: List<Int>, language: Language): String {
        val houseStr = houses.joinToString(if (language == Language.NEPALI) " र " else " and ") { if (language == Language.NEPALI) "${it}औं भाव" else "House $it" }
        return StringResources.get(StringKeyAnalysis.TAJIKA_PREDICTION_X_FOR_Y, language, type.getDisplayName(language), p1.getLocalizedName(language), p2.getLocalizedName(language), if (type.isPositive) StringResources.get(StringKeyAnalysis.VARSHA_TONE_FAVORABLE, language) else StringResources.get(StringKeyAnalysis.VARSHA_TONE_CHALLENGING, language), houseStr)
    }
}


