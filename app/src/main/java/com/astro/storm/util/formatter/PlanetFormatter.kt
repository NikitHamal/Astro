package com.astro.storm.util.formatter

import android.content.Context
import com.astro.storm.R
import com.astro.storm.data.model.PlanetPosition
import com.astro.storm.data.model.ZodiacSign

object PlanetFormatter {
    fun toFormattedString(planetPosition: PlanetPosition, context: Context): String {
        val degreeInSign = planetPosition.longitude % 30.0
        val deg = degreeInSign.toInt()
        val min = ((degreeInSign - deg) * 60).toInt()
        val sec = ((((degreeInSign - deg) * 60) - min) * 60).toInt()
        val retrograde = if (planetPosition.isRetrograde) " (R)" else ""
        return "${context.getString(planetPosition.planet.stringRes)}: ${context.getString(planetPosition.sign.abbreviationRes)} ${deg}° ${min}' ${sec}\"$retrograde"
    }

    fun formatSignName(sign: ZodiacSign, context: Context): String {
        return context.getString(sign.stringRes)
    }

    fun toLLMString(planetPosition: PlanetPosition, context: Context): String {
        val degreeInSign = planetPosition.longitude % 30.0
        val deg = degreeInSign.toInt()
        val min = ((degreeInSign - deg) * 60).toInt()
        val sec = ((((degreeInSign - deg) * 60) - min) * 60).toInt()
        val retrograde = if (planetPosition.isRetrograde) " [Retrograde]" else ""
        return "${context.getString(planetPosition.planet.stringRes).padEnd(10)}: ${context.getString(planetPosition.sign.stringRes).padEnd(12)} ${deg}° ${min}' ${sec}\" | House ${planetPosition.house} | ${context.getString(planetPosition.nakshatra.stringRes)} (Pada ${planetPosition.nakshatraPada})$retrograde"
    }
}
