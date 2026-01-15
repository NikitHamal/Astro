package com.astro.storm.ephemeris.varga

import com.astro.storm.core.model.Planet
import com.astro.storm.core.model.PlanetPosition
import com.astro.storm.core.model.VedicChart
import com.astro.storm.core.model.ZodiacSign
import com.astro.storm.ephemeris.AstrologicalConstants

object VargaHelpers {

    fun getHouseLord(chart: VedicChart, house: Int): Planet {
        val ascendantSign = ZodiacSign.fromLongitude(chart.ascendant)
        val houseSign = ZodiacSign.entries[(ascendantSign.ordinal + house - 1) % 12]
        return houseSign.ruler
    }

    fun calculatePlanetStrengthInVarga(position: PlanetPosition): Double {
        var strength = 50.0
        if (AstrologicalConstants.isExalted(position.planet, position.sign)) strength += 30.0
        if (AstrologicalConstants.isInOwnSign(position.planet, position.sign)) strength += 20.0
        if (AstrologicalConstants.isDebilitated(position.planet, position.sign)) strength -= 25.0
        return strength.coerceIn(0.0, 100.0)
    }
}
