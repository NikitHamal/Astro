package com.astro.storm.ephemeris.advanced

import com.astro.storm.data.model.*
import com.astro.storm.ephemeris.VedicAstrologyUtils
import kotlin.math.abs

/**
 * Core Manglik Dosha Calculator - High Precision Implementation
 * 
 * Implements classical Vedic astrology rules from:
 * - Brihat Parasara Hora Shastra (BPHS)
 * - Sarvartha Chintamani
 * - Phaladeepika
 * 
 * Enhanced precision through:
 * - Proper house calculations from Lagna, Moon, and Venus
 * - Comprehensive cancellation factors
 * - Intensity calculations with classical weights
 * - Mars placement validation
 */
object ManglikDoshaCore {
    
    // Classical Manglik houses from any reference point
    private val MANGLIK_HOUSES = setOf(1, 2, 4, 7, 8, 12)
    
    // Intensity multipliers based on house severity (Classical weights)
    private val HOUSE_INTENSITY_MULTIPLIERS = mapOf(
        1 to 0.8,   // 1st house - Moderate
        2 to 0.4,   // 2nd house - Mild
        4 to 0.7,   // 4th house - Moderate-High
        7 to 1.0,   // 7th house - Maximum
        8 to 1.0,   // 8th house - Maximum
        12 to 0.8   // 12th house - Moderate
    )
    
    /**
     * Calculate Mars position from different reference points
     */
    data class MarsPlacement(
        val house: Int,
        val sign: ZodiacSign,
        val isManglik: Boolean,
        val intensity: Double,
        val degree: Double
    )
    
    /**
     * Comprehensive cancellation factors with classical validation
     */
    enum class CancellationFactor(
        val weight: Double,
        val description: String
    ) {
        EXALTATION(1.0, "Mars in exaltation (Capricorn)"),
        OWN_SIGN(0.8, "Mars in own sign (Aries/Scorpio)"),
        JUPITER_CONJUNCT(1.0, "Jupiter conjunct Mars"),
        JUPITER_ASPECT(0.7, "Jupiter aspects Mars (5th, 7th, 9th)"),
        VENUS_CONJUNCT(0.8, "Venus conjunct Mars"),
        SECOND_HOUSE_MERCURY(0.8, "Mars in 2nd house with Gemini/Virgo"),
        FOURTH_HOUSE_OWN(0.9, "Mars in 4th house with Aries/Scorpio"),
        SEVENTH_HOUSE_SPECIAL(0.7, "Mars in 7th house with Cancer/Capricorn"),
        EIGHTH_HOUSE_JUPITER(0.7, "Mars in 8th house with Sagittarius/Pisces"),
        TWELFTH_HOUSE_VENUS(0.8, "Mars in 12th house with Taurus/Libra"),
        BENEFIC_ASCENDANT(0.3, "Mars benefic for specific ascendants")
    }
    
    /**
     * Calculate Mars placement from a specific reference point
     */
    fun calculateMarsPlacement(
        chart: VedicChart,
        referenceSign: ZodiacSign,
        referenceType: String
    ): MarsPlacement {
        val marsPosition = VedicAstrologyUtils.getPlanetPosition(chart, Planet.MARS)
            ?: return MarsPlacement(0, ZodiacSign.ARIES, false, 0.0, 0.0)
        
        val marsHouse = VedicAstrologyUtils.getHouseFromSigns(marsPosition.sign, referenceSign)
        val isManglik = marsHouse in MANGLIK_HOUSES
        val intensity = if (isManglik) {
            HOUSE_INTENSITY_MULTIPLIERS[marsHouse] ?: 0.5
        } else 0.0
        
        val degree = marsPosition.longitude % 30
        
        return MarsPlacement(
            house = marsHouse,
            sign = marsPosition.sign,
            isManglik = isManglik,
            intensity = intensity,
            degree = degree
        )
    }
    
    /**
     * Enhanced cancellation factor detection
     */
    fun findCancellationFactors(
        chart: VedicChart,
        marsPlacement: MarsPlacement
    ): List<CancellationFactor> {
        val factors = mutableListOf<CancellationFactor>()
        val marsPosition = VedicAstrologyUtils.getPlanetPosition(chart, Planet.MARS)
            ?: return factors
        
        // 1. Exaltation check
        if (marsPosition.sign == ZodiacSign.CAPRICORN) {
            factors.add(CancellationFactor.EXALTATION)
        }
        
        // 2. Own sign check
        if (marsPosition.sign in listOf(ZodiacSign.ARIES, ZodiacSign.SCORPIO)) {
            factors.add(CancellationFactor.OWN_SIGN)
        }
        
        // 3. Benefic conjunctions
        val jupiter = VedicAstrologyUtils.getPlanetPosition(chart, Planet.JUPITER)
        val venus = VedicAstrologyUtils.getPlanetPosition(chart, Planet.VENUS)
        
        if (jupiter != null && jupiter.house == marsPosition.house) {
            factors.add(CancellationFactor.JUPITER_CONJUNCT)
        }
        
        if (venus != null && venus.house == marsPosition.house) {
            factors.add(CancellationFactor.VENUS_CONJUNCT)
        }
        
        // 4. Jupiter aspects (5th, 7th, 9th)
        jupiter?.let { jupiterPos ->
            val aspectedHouses = listOf(
                (jupiterPos.house + 4) % 12, // 5th aspect
                (jupiterPos.house + 6) % 12, // 7th aspect
                (jupiterPos.house + 8) % 12  // 9th aspect
            ).map { if (it == 0) 12 else it }
            
            if (marsPosition.house in aspectedHouses) {
                factors.add(CancellationFactor.JUPITER_ASPECT)
            }
        }
        
        // 5. Special house-based cancellations
        when (marsPlacement.house) {
            2 -> if (marsPosition.sign in listOf(ZodiacSign.GEMINI, ZodiacSign.VIRGO)) {
                factors.add(CancellationFactor.SECOND_HOUSE_MERCURY)
            }
            4 -> if (marsPosition.sign in listOf(ZodiacSign.ARIES, ZodiacSign.SCORPIO)) {
                factors.add(CancellationFactor.FOURTH_HOUSE_OWN)
            }
            7 -> if (marsPosition.sign in listOf(ZodiacSign.CANCER, ZodiacSign.CAPRICORN)) {
                factors.add(CancellationFactor.SEVENTH_HOUSE_SPECIAL)
            }
            8 -> if (marsPosition.sign in listOf(ZodiacSign.SAGITTARIUS, ZodiacSign.PISCES)) {
                factors.add(CancellationFactor.EIGHTH_HOUSE_JUPITER)
            }
            12 -> if (marsPosition.sign in listOf(ZodiacSign.TAURUS, ZodiacSign.LIBRA)) {
                factors.add(CancellationFactor.TWELFTH_HOUSE_VENUS)
            }
        }
        
        return factors
    }
    
    /**
     * Calculate effective Manglik intensity after cancellations
     */
    fun calculateEffectiveIntensity(
        placements: List<MarsPlacement>,
        cancellationFactors: List<List<CancellationFactor>>
    ): Double {
        if (placements.isEmpty()) return 0.0
        
        // Calculate weighted average intensity
        var totalIntensity = 0.0
        var totalWeight = 0.0
        
        placements.forEachIndexed { index, placement ->
            val weight = when (index) {
                0 -> 0.4  // Lagna (40%)
                1 -> 0.35 // Moon (35%)
                2 -> 0.25 // Venus (25%)
                else -> 0.2
            }
            totalIntensity += placement.intensity * weight
            totalWeight += weight
        }
        
        if (totalWeight > 0) totalIntensity /= totalWeight
        
        // Apply cancellation factors
        val cancellationMultiplier = calculateCancellationMultiplier(
            cancellationFactors.getOrNull(0) ?: emptyList()
        )
        
        return (totalIntensity * (1.0 - cancellationMultiplier)).coerceIn(0.0, 1.0)
    }
    
    /**
     * Calculate cancellation multiplier based on classical rules
     */
    private fun calculateCancellationMultiplier(factors: List<CancellationFactor>): Double {
        if (factors.isEmpty()) return 0.0
        
        // Full cancellation check
        if (factors.any { it == CancellationFactor.EXALTATION || 
                         it == CancellationFactor.JUPITER_CONJUNCT }) {
            return 1.0
        }
        
        // Partial cancellations
        var totalReduction = 0.0
        factors.forEach { factor ->
            totalReduction += when (factor) {
                CancellationFactor.OWN_SIGN -> 0.6
                CancellationFactor.VENUS_CONJUNCT -> 0.5
                CancellationFactor.JUPITER_ASPECT -> 0.4
                CancellationFactor.SECOND_HOUSE_MERCURY -> 0.7
                CancellationFactor.FOURTH_HOUSE_OWN -> 0.8
                CancellationFactor.SEVENTH_HOUSE_SPECIAL -> 0.5
                CancellationFactor.EIGHTH_HOUSE_JUPITER -> 0.5
                CancellationFactor.TWELFTH_HOUSE_VENUS -> 0.7
                CancellationFactor.BENEFIC_ASCENDANT -> 0.2
                else -> 0.3
            }
        }
        
        return totalReduction.coerceAtMost(0.95)
    }
    
    /**
     * Determine Manglik level based on intensity
     */
    fun determineManglikLevel(effectiveIntensity: Double): ManglikLevel {
        return when {
            effectiveIntensity >= 0.8 -> ManglikLevel.SEVERE
            effectiveIntensity >= 0.6 -> ManglikLevel.FULL
            effectiveIntensity >= 0.35 -> ManglikLevel.PARTIAL
            effectiveIntensity >= 0.15 -> ManglikLevel.MILD
            else -> ManglikLevel.NONE
        }
    }
    
    /**
     * Validate Mars placement for accuracy
     */
    fun validateMarsPlacement(placement: MarsPlacement): Boolean {
        return when {
            placement.house !in 1..12 -> false
            placement.intensity !in 0.0..1.0 -> false
            placement.degree < 0.0 || placement.degree > 30.0 -> false
            placement.isManglik != (placement.house in MANGLIK_HOUSES) -> false
            else -> true
        }
    }
    
    /**
     * Get classical remedy based on Manglik level and placement
     */
    fun getClassicalRemedies(level: ManglikLevel, placement: MarsPlacement?): List<ManglikRemedy> {
        return when (level) {
            ManglikLevel.NONE -> emptyList()
            ManglikLevel.MILD -> listOf(
                ManglikRemedy.RECITE_MARS_MANTRA,
                ManglikRemedy.TUESDAY_CHARITY,
                ManglikRemedy.OBSERVE_FAST
            )
            ManglikLevel.PARTIAL -> listOf(
                ManglikRemedy.MANGAL_SHANTI_PUJA,
                ManglikRemedy.CORAL_PUJA,
                ManglikRemedy.MARS_MANTRA,
                ManglikRemedy.TUESDAY_VRAT
            )
            ManglikLevel.FULL -> listOf(
                ManglikRemedy.KUMBH_VIVAH,
                ManglikRemedy.CORAL_GEMSTONE,
                ManglikRemedy.MANGAL_SHANTI,
                ManglikRemedy.HANUMAN_PUJA,
                ManglikRemedy.TUESDAY_FAST
            )
            ManglikLevel.SEVERE -> listOf(
                ManglikRemedy.KUMBH_VIVAH_ESSENTIAL,
                ManglikRemedy.STRONG_CORAL,
                ManglikRemedy.MULTIPLE_PUJAS,
                ManglikRemedy.EXTENDED_FASTING,
                ManglikRemedy.POWERFUL_MANTRA
            )
        }
    }
}

/**
 * Manglik levels with enhanced precision
 */
enum class ManglikLevel(val intensity: Double, val description: String) {
    NONE(0.0, "No Manglik Dosha"),
    MILD(0.15, "Mild Manglik Dosha"),
    PARTIAL(0.35, "Partial Manglik Dosha"),
    FULL(0.6, "Full Manglik Dosha"),
    SEVERE(0.8, "Severe Manglik Dosha");
    
    companion object {
        fun fromIntensity(intensity: Double): ManglikLevel {
            return values().firstOrNull { intensity <= it.intensity } ?: NONE
        }
    }
}

/**
 * Classical Manglik remedies with detailed descriptions
 */
enum class ManglikRemedy(val description: String, val effectiveness: String) {
    KUMBH_VIVAH_ESSENTIAL("Kumbh Vivah (Essential)", "Complete cancellation"),
    KUMBH_VIVAH("Kumbh Vivah", "Strong cancellation"),
    MANGAL_SHANTI_PUJA("Mangal Shanti Puja", "Very effective"),
    MANGAL_SHANTI("Mangal Shanti", "Very effective"),
    CORAL_GEMSTONE("Natural Coral Gemstone", "Highly effective"),
    STRONG_CORAL("High-quality Coral", "Highly effective"),
    CORAL_PUJA("Coral Puja", "Effective"),
    HANUMAN_PUJA("Hanuman Puja", "Very effective"),
    MULTIPLE_PUJAS("Multiple Pujas", "Comprehensive relief"),
    MARS_MANTRA("Om Mangalaya Namaha (108 times)", "Daily recitation"),
    RECITE_MARS_MANTRA("Mars Mantra recitation", "Daily"),
    POWERFUL_MANTRA("Strong Mars Mantras", "Intensive practice"),
    TUESDAY_FAST("Tuesday Fasting", "Weekly"),
    TUESDAY_VRAT("Tuesday Vrat", "Weekly"),
    TUESDAY_CHARITY("Tuesday Charity", "Weekly"),
    OBSERVE_FAST("Occasional Fasting", "Mild relief"),
    EXTENDED_FASTING("Extended Fasting", "Strong relief");
    
    fun getLocalizedDescription(): String = description
    fun getEffectivenessInfo(): String = effectiveness
}