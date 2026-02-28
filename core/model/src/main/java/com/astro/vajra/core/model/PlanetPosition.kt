package com.astro.vajra.core.model

import com.astro.vajra.core.common.Language
import com.astro.vajra.core.common.StringResources
import com.astro.vajra.core.common.StringKeyUIExtra

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
    val house: Int,
    val isOnHouseCusp: Boolean = false
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
        fun formatNum(n: Int) = if (isNepali) com.astro.vajra.core.common.BikramSambatConverter.toNepaliNumerals(n) else n.toString()
        
        val retrograde = if (isRetrograde) {
            " " + StringResources.get(com.astro.vajra.core.common.StringKey.PLANET_RETROGRADE_SHORT, language)
        } else ""
        
        val degSign = StringResources.get(StringKeyUIExtra.DEGREE, language)
        val minSign = StringResources.get(StringKeyUIExtra.ARC_MINUTE, language)
        val secSign = StringResources.get(StringKeyUIExtra.ARC_SECOND, language)
        val colon = StringResources.get(StringKeyUIExtra.COLON_SPACE, language)

        return "${planet.getLocalizedName(language)}$colon${sign.abbreviation} ${formatNum(deg)}$degSign ${formatNum(min)}$minSign ${formatNum(sec)}$secSign$retrograde"
    }

    fun toLLMString(language: Language = Language.ENGLISH): String {
        val degreeInSign = longitude % 30.0
        val deg = degreeInSign.toInt()
        val min = ((degreeInSign - deg) * 60).toInt()
        val sec = ((((degreeInSign - deg) * 60) - min) * 60).toInt()
        
        val isNepali = language == Language.NEPALI
        fun formatNum(n: Int) = if (isNepali) com.astro.vajra.core.common.BikramSambatConverter.toNepaliNumerals(n) else n.toString()
        
        val degSign = StringResources.get(StringKeyUIExtra.DEGREE, language)
        val minSign = StringResources.get(StringKeyUIExtra.ARC_MINUTE, language)
        val secSign = StringResources.get(StringKeyUIExtra.ARC_SECOND, language)
        val pipe = StringResources.get(StringKeyUIExtra.PIPE_SEP, language)
        val colon = StringResources.get(StringKeyUIExtra.COLON_SPACE, language)
        val pStart = StringResources.get(StringKeyUIExtra.PAREN_START, language)
        val pEnd = StringResources.get(StringKeyUIExtra.PAREN_END, language)
        val bStart = StringResources.get(StringKeyUIExtra.BRACKET_START, language)
        val bEnd = StringResources.get(StringKeyUIExtra.BRACKET_END, language)

        val retrograde = if (isRetrograde) {
            " $bStart" + StringResources.get(com.astro.vajra.core.common.StringKey.PLANET_RETROGRADE, language) + bEnd
        } else ""
        
        val houseText = StringResources.get(com.astro.vajra.core.common.StringKey.CHART_HOUSE, language)
        val padaText = StringResources.get(com.astro.vajra.core.common.StringKey.NAKSHATRA_PADA, language)
        
        return "${planet.getLocalizedName(language).padEnd(10)}$colon${sign.getLocalizedName(language).padEnd(12)} ${formatNum(deg)}$degSign ${formatNum(min)}$minSign ${formatNum(sec)}$secSign $pipe $houseText ${formatNum(house)} $pipe ${nakshatra.getLocalizedName(language)} $pStart$padaText ${formatNum(nakshatraPada)}$pEnd$retrograde"
    }
}


