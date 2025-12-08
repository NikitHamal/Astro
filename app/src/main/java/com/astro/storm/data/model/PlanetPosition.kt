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
    val degreesInSign: Double,
    val isRetrograde: Boolean,
    val nakshatra: Nakshatra,
    val nakshatraPada: Int,
    val house: Int
)
