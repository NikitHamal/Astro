package com.astro.storm.core.model

import com.astro.storm.core.common.Language
import com.astro.storm.core.common.StringResources

/**
 * Position of a planet in the chart
 */
data class PlanetPosition(
    val planet: Planet,
    val longitude: Double,
    val latitude: Double,
    val distance: Double,
    val speed: Double,
    val sign: ZodiacSign,
    val degree: Double,
    val minutes: Double,
    val seconds: Double,
    val isRetrograde: Boolean,
    val nakshatra: Nakshatra,
    val nakshatraPada: Int,
    val house: Int
) {
    /** Formatted degree string (e.g., "12°34'56\"") */
    val formattedDegree: String
        get() {
            val degreeInSign = longitude % 30.0
            val deg = degreeInSign.toInt()
            val min = ((degreeInSign - deg) * 60).toInt()
            val sec = ((((degreeInSign - deg) * 60) - min) * 60).toInt()
            return "${deg}° ${min}' ${sec}\""
        }

    /** Alias for house property for transit context */
    val houseTransit: Int
        get() = house

    fun toFormattedString(language: Language): String {
        val degreeInSign = longitude % 30.0
        val deg = degreeInSign.toInt()
        val min = ((degreeInSign - deg) * 60).toInt()
        val sec = ((((degreeInSign - deg) * 60) - min) * 60).toInt()
        
        val isNepali = language == Language.NEPALI
        fun formatNum(n: Int) = if (isNepali) com.astro.storm.core.common.BikramSambatConverter.toNepaliNumerals(n) else n.toString()
        
        val retrograde = if (isRetrograde) {
            " " + StringResources.get(com.astro.storm.core.common.StringKey.PLANET_RETROGRADE_SHORT, language)
        } else ""
        
        return "${planet.getLocalizedName(language)}: ${sign.abbreviation} ${formatNum(deg)}° ${formatNum(min)}' ${formatNum(sec)}\"$retrograde"
    }

    fun toLLMString(language: Language = Language.ENGLISH): String {
        val degreeInSign = longitude % 30.0
        val deg = degreeInSign.toInt()
        val min = ((degreeInSign - deg) * 60).toInt()
        val sec = ((((degreeInSign - deg) * 60) - min) * 60).toInt()
        
        val isNepali = language == Language.NEPALI
        fun formatNum(n: Int) = if (isNepali) com.astro.storm.core.common.BikramSambatConverter.toNepaliNumerals(n) else n.toString()
        
        val retrograde = if (isRetrograde) {
            " [" + StringResources.get(com.astro.storm.core.common.StringKey.PLANET_RETROGRADE, language) + "]"
        } else ""
        
        val houseText = StringResources.get(com.astro.storm.core.common.StringKey.CHART_HOUSE, language)
        val padaText = StringResources.get(com.astro.storm.core.common.StringKey.NAKSHATRA_PADA, language)
        
        return "${planet.getLocalizedName(language).padEnd(10)}: ${sign.getLocalizedName(language).padEnd(12)} ${formatNum(deg)}° ${formatNum(min)}' ${formatNum(sec)}\" | $houseText ${formatNum(house)} | ${nakshatra.getLocalizedName(language)} ($padaText ${formatNum(nakshatraPada)})$retrograde"
    }
}


