package com.astro.storm.data.model

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

    fun toFormattedString(context: android.content.Context): String {
        val degreeInSign = longitude % 30.0
        val deg = degreeInSign.toInt()
        val min = ((degreeInSign - deg) * 60).toInt()
        val sec = ((((degreeInSign - deg) * 60) - min) * 60).toInt()
        val retrograde = if (isRetrograde) " (R)" else ""
        return "${planet.displayName.asString(context)}: ${sign.abbreviation} ${deg}° ${min}' ${sec}\"$retrograde"
    }
}
