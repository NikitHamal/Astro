package com.astro.storm.ephemeris.nativeanalysis

import com.astro.storm.core.model.*
import com.astro.storm.ephemeris.AspectUtils
import com.astro.storm.ephemeris.VedicAstrologyUtils

object NativeAnalysisHelpers {

    fun getDignity(pos: PlanetPosition): PlanetaryDignity {
        return when {
            VedicAstrologyUtils.isExalted(pos) -> PlanetaryDignity.EXALTED
            VedicAstrologyUtils.isDebilitated(pos) -> PlanetaryDignity.DEBILITATED
            VedicAstrologyUtils.isInMoolatrikona(pos) -> PlanetaryDignity.MOOLATRIKONA
            VedicAstrologyUtils.isInOwnSign(pos) -> PlanetaryDignity.OWN_SIGN
            VedicAstrologyUtils.isInFriendSign(pos) -> PlanetaryDignity.FRIEND_SIGN
            VedicAstrologyUtils.isInEnemySign(pos) -> PlanetaryDignity.ENEMY_SIGN
            else -> PlanetaryDignity.NEUTRAL_SIGN
        }
    }

    fun dignityToStrength(dignity: PlanetaryDignity): StrengthLevel {
        return when (dignity) {
            PlanetaryDignity.EXALTED -> StrengthLevel.EXCELLENT
            PlanetaryDignity.MOOLATRIKONA, PlanetaryDignity.OWN_SIGN -> StrengthLevel.STRONG
            PlanetaryDignity.FRIEND_SIGN, PlanetaryDignity.NEUTRAL_SIGN -> StrengthLevel.MODERATE
            PlanetaryDignity.ENEMY_SIGN -> StrengthLevel.WEAK
            PlanetaryDignity.DEBILITATED -> StrengthLevel.AFFLICTED
        }
    }

    fun aspectsHouse(planetPos: PlanetPosition, targetHouse: Int, chart: VedicChart): Boolean {
        return AspectUtils.aspectsHouse(planetPos, targetHouse)
    }

    fun getSignElement(sign: ZodiacSign): Element {
        return when (sign) {
            ZodiacSign.ARIES, ZodiacSign.LEO, ZodiacSign.SAGITTARIUS -> Element.FIRE
            ZodiacSign.TAURUS, ZodiacSign.VIRGO, ZodiacSign.CAPRICORN -> Element.EARTH
            ZodiacSign.GEMINI, ZodiacSign.LIBRA, ZodiacSign.AQUARIUS -> Element.AIR
            else -> Element.WATER
        }
    }

    fun getSignModality(sign: ZodiacSign): Modality {
        return when (sign) {
            ZodiacSign.ARIES, ZodiacSign.CANCER, ZodiacSign.LIBRA, ZodiacSign.CAPRICORN -> Modality.CARDINAL
            ZodiacSign.TAURUS, ZodiacSign.LEO, ZodiacSign.SCORPIO, ZodiacSign.AQUARIUS -> Modality.FIXED
            else -> Modality.MUTABLE
        }
    }
}
