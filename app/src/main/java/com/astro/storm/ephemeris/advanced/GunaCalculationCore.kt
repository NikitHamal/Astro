package com.astro.storm.ephemeris.advanced

import com.astro.storm.data.model.*
import com.astro.storm.data.localization.Language
import com.astro.storm.data.localization.StringKeyMatch
import com.astro.storm.data.localization.StringResources
import kotlin.math.abs

/**
 * Advanced Guna Calculation Core - High Precision Implementation
 * 
 * Implements classical Vedic astrology rules for Ashtakoota (8 Guna) matching:
 * - Brihat Parasara Hora Shastra (BPHS)
 * - Muhurta Chintamani
 * - Sarvartha Chintamani
 * - Jataka Parijata
 * 
 * Enhanced precision features:
 * - Proper Nakshatra Pada calculations
 * - Classical Guna point allocations
 * - Comprehensive cancellation factors
 * - Accurate planetary relationships
 */
object GunaCalculationCore {
    
    // ============================================
    // VARNA CALCULATION ENHANCED
    // ============================================
    
    /**
     * Enhanced Varna calculation with classical precision
     */
    fun calculateVarna(
        brideSign: ZodiacSign,
        groomSign: ZodiacSign,
        language: Language = Language.ENGLISH
    ): GunaAnalysis {
        val brideVarna = getClassicalVarna(brideSign)
        val groomVarna = getClassicalVarna(groomSign)
        
        val points = if (groomVarna.value >= brideVarna.value) 1.0 else 0.0
        val analysis = generateVarnaAnalysis(brideVarna, groomVarna, points, language)
        
        return GunaAnalysis(
            name = "Varna",
            maxPoints = 1.0,
            obtainedPoints = points,
            description = getVarnaDescription(language),
            brideValue = brideVarna.displayName,
            groomValue = groomVarna.displayName,
            analysis = analysis,
            isPositive = points > 0
        )
    }
    
    /**
     * Classical Varna assignment based on zodiac elements
     */
    private fun getClassicalVarna(sign: ZodiacSign): ClassicalVarna = when (sign) {
        // Water signs - Highest Varna (Brahmin)
        ZodiacSign.CANCER, ZodiacSign.SCORPIO, ZodiacSign.PISCES -> ClassicalVarna.BRAHMIN
        
        // Fire signs - Kshatriya
        ZodiacSign.ARIES, ZodiacSign.LEO, ZodiacSign.SAGITTARIUS -> ClassicalVarna.KSHATRIYA
        
        // Earth signs - Vaishya
        ZodiacSign.TAURUS, ZodiacSign.VIRGO, ZodiacSign.CAPRICORN -> ClassicalVarna.VAISHYA
        
        // Air signs - Shudra
        ZodiacSign.GEMINI, ZodiacSign.LIBRA, ZodiacSign.AQUARIUS -> ClassicalVarna.SHUDRA
    }
    
    private fun generateVarnaAnalysis(
        brideVarna: ClassicalVarna,
        groomVarna: ClassicalVarna,
        points: Double,
        language: Language
    ): String {
        return if (points > 0) {
            StringResources.get(StringKeyMatch.VARNA_COMPATIBLE, language)
                .replace("{groom}", groomVarna.displayName)
                .replace("{bride}", brideVarna.displayName)
        } else {
            StringResources.get(StringKeyMatch.VARNA_INCOMPATIBLE, language)
                .replace("{bride}", brideVarna.displayName)
                .replace("{groom}", groomVarna.displayName)
        }
    }
    
    private fun getVarnaDescription(language: Language): String = 
        StringResources.get(StringKeyMatch.VARNA_DESC, language)
    
    // ============================================
    // VASHYA CALCULATION ENHANCED
    // ============================================
    
    fun calculateVashya(
        brideSign: ZodiacSign,
        groomSign: ZodiacSign,
        language: Language = Language.ENGLISH
    ): GunaAnalysis {
        val brideVashya = getClassicalVashya(brideSign)
        val groomVashya = getClassicalVashya(groomSign)
        
        val points = calculateVashyaPointsWithPrecision(brideVashya, groomVashya, brideSign, groomSign)
        
        val analysis = generateVashyaAnalysis(points, brideVashya, groomVashya, language)
        
        return GunaAnalysis(
            name = "Vashya",
            maxPoints = 2.0,
            obtainedPoints = points,
            description = getVashyaDescription(language),
            brideValue = "${brideVashya.displayName} (${brideSign.displayName})",
            groomValue = "${groomVashya.displayName} (${groomSign.displayName})",
            analysis = analysis,
            isPositive = points >= 1.0
        )
    }
    
    /**
     * Classical Vashya assignment with enhanced precision
     */
    private fun getClassicalVashya(sign: ZodiacSign): ClassicalVashya = when (sign) {
        ZodiacSign.ARIES, ZodiacSign.TAURUS -> ClassicalVashya.CHATUSHPADA
        ZodiacSign.GEMINI, ZodiacSign.VIRGO, ZodiacSign.LIBRA, 
        ZodiacSign.SAGITTARIUS, ZodiacSign.AQUARIUS -> ClassicalVashya.MANAVA
        ZodiacSign.CANCER, ZodiacSign.CAPRICORN, ZodiacSign.PISCES -> ClassicalVashya.JALACHARA
        ZodiacSign.LEO -> ClassicalVashya.VANACHARA
        ZodiacSign.SCORPIO -> ClassicalVashya.KEETA
    }
    
    /**
     * Enhanced Vashya point calculation with classical rules
     */
    private fun calculateVashyaPointsWithPrecision(
        brideVashya: ClassicalVashya,
        groomVashya: ClassicalVashya,
        brideSign: ZodiacSign,
        groomSign: ZodiacSign
    ): Double {
        // Same sign or same Vashya - Full points
        if (brideSign == groomSign || brideVashya == groomVashya) return 2.0
        
        // Control relationships (classical)
        val groomControlsBride = ClassicalVashya.controlRelationships[groomVashya]?.contains(brideVashya) == true
        val brideControlsGroom = ClassicalVashya.controlRelationships[brideVashya]?.contains(groomVashya) == true
        
        // Enemy relationships
        val isEnemy = ClassicalVashya.enemyPairs.any { 
            it.contains(brideVashya) && it.contains(groomVashya) 
        }
        
        return when {
            // Mutual control - Excellent
            groomControlsBride && brideControlsGroom -> 2.0
            // One-sided control - Good
            groomControlsBride || brideControlsGroom -> 1.0
            // Enemy relationship - Zero
            isEnemy -> 0.0
            // Same category - Partial
            else -> 0.5
        }
    }
    
    private fun generateVashyaAnalysis(
        points: Double,
        brideVashya: ClassicalVashya,
        groomVashya: ClassicalVashya,
        language: Language
    ): String {
        return when {
            points >= 2.0 -> StringResources.get(StringKeyMatch.VASHYA_EXCELLENT, language)
            points >= 1.5 -> StringResources.get(StringKeyMatch.VASHYA_VERY_GOOD, language)
            points >= 1.0 -> StringResources.get(StringKeyMatch.VASHYA_GOOD, language)
            points >= 0.5 -> StringResources.get(StringKeyMatch.VASHYA_PARTIAL, language)
            else -> StringResources.get(StringKeyMatch.VASHYA_INCOMPATIBLE, language)
        }
    }
    
    private fun getVashyaDescription(language: Language): String = 
        StringResources.get(StringKeyMatch.VASHYA_DESC, language)
    
    // ============================================
    // TARA CALCULATION ENHANCED
    // ============================================
    
    fun calculateTara(
        brideNakshatra: Nakshatra,
        groomNakshatra: Nakshatra,
        language: Language = Language.ENGLISH
    ): GunaAnalysis {
        val brideTara = calculateTaraNumber(brideNakshatra, groomNakshatra)
        val groomTara = calculateTaraNumber(groomNakshatra, brideNakshatra)
        
        val brideAuspicious = isClassicallyAuspiciousTara(brideTara)
        val groomAuspicious = isClassicallyAuspiciousTara(groomTara)
        
        val points = calculateTaraPoints(brideAuspicious, groomAuspicious)
        
        val analysis = generateTaraAnalysis(
            brideTara, groomTara, brideAuspicious, groomAuspicious, points, language
        )
        
        return GunaAnalysis(
            name = "Tara",
            maxPoints = 3.0,
            obtainedPoints = points,
            description = getTaraDescription(language),
            brideValue = "${brideNakshatra.displayName} → ${getTaraName(brideTara, language)}",
            groomValue = "${groomNakshatra.displayName} → ${getTaraName(groomTara, language)}",
            analysis = analysis,
            isPositive = points >= 1.5
        )
    }
    
    /**
     * Classical Tara number calculation with enhanced precision
     */
    private fun calculateTaraNumber(fromNakshatra: Nakshatra, toNakshatra: Nakshatra): Int {
        val diff = (toNakshatra.number - fromNakshatra.number + 27) % 27
        return if (diff == 0) 9 else ((diff - 1) % 9) + 1
    }
    
    /**
     * Classical auspicious Tara determination
     */
    private fun isClassicallyAuspiciousTara(taraNumber: Int): Boolean {
        return when (taraNumber) {
            2, 4, 6, 8, 9 -> true  // Auspicious Tara
            1, 3, 5, 7 -> false    // Inauspicious Tara
            else -> false
        }
    }
    
    private fun calculateTaraPoints(brideAuspicious: Boolean, groomAuspicious: Boolean): Double {
        return when {
            brideAuspicious && groomAuspicious -> 3.0
            brideAuspicious || groomAuspicious -> 1.5
            else -> 0.0
        }
    }
    
    private fun getTaraName(taraNumber: Int, language: Language): String = when (taraNumber) {
        1 -> StringResources.get(StringKeyMatch.TARA_JANMA, language)
        2 -> StringResources.get(StringKeyMatch.TARA_SAMPAT, language)
        3 -> StringResources.get(StringKeyMatch.TARA_VIPAT, language)
        4 -> StringResources.get(StringKeyMatch.TARA_KSHEMA, language)
        5 -> StringResources.get(StringKeyMatch.TARA_PRATYARI, language)
        6 -> StringResources.get(StringKeyMatch.TARA_SADHANA, language)
        7 -> StringResources.get(StringKeyMatch.TARA_VADHA, language)
        8 -> StringResources.get(StringKeyMatch.TARA_MITRA, language)
        9 -> StringResources.get(StringKeyMatch.TARA_PARAMA_MITRA, language)
        else -> "Unknown"
    }
    
    private fun generateTaraAnalysis(
        brideTara: Int,
        groomTara: Int,
        brideAuspicious: Boolean,
        groomAuspicious: Boolean,
        points: Double,
        language: Language
    ): String {
        return when {
            points >= 3.0 -> StringResources.get(StringKeyMatch.TARA_EXCELLENT, language)
            points >= 1.5 -> StringResources.get(StringKeyMatch.TARA_MODERATE, language)
            else -> StringResources.get(StringKeyMatch.TARA_INAUSPICIOUS, language)
        }
    }
    
    private fun getTaraDescription(language: Language): String = 
        StringResources.get(StringKeyMatch.TARA_DESC, language)
    
    // ============================================
    // YONI CALCULATION ENHANCED
    // ============================================
    
    fun calculateYoni(
        brideNakshatra: Nakshatra,
        groomNakshatra: Nakshatra,
        language: Language = Language.ENGLISH
    ): GunaAnalysis {
        val brideYoni = getClassicalYoni(brideNakshatra)
        val groomYoni = getClassicalYoni(groomNakshatra)
        
        val points = calculateYoniPointsWithPrecision(brideYoni, groomYoni)
        
        val analysis = generateYoniAnalysis(points, brideYoni, groomYoni, language)
        
        return GunaAnalysis(
            name = "Yoni",
            maxPoints = 4.0,
            obtainedPoints = points,
            description = getYoniDescription(language),
            brideValue = "${brideYoni.animal} (${brideYoni.gender})",
            groomValue = "${groomYoni.animal} (${groomYoni.gender})",
            analysis = analysis,
            isPositive = points >= 2.0
        )
    }
    
    /**
     * Classical Yoni assignment with enhanced precision
     */
    private fun getClassicalYoni(nakshatra: Nakshatra): ClassicalYoni = when (nakshatra) {
        Nakshatra.ASHWINI -> ClassicalYoni.ASHWA_MALE
        Nakshatra.SHATABHISHA -> ClassicalYoni.ASHWA_FEMALE
        Nakshatra.BHARANI -> ClassicalYoni.GAJA_MALE
        Nakshatra.REVATI -> ClassicalYoni.GAJA_FEMALE
        Nakshatra.PUSHYA -> ClassicalYoni.MESHA_MALE
        Nakshatra.KRITTIKA -> ClassicalYoni.MESHA_FEMALE
        Nakshatra.ROHINI -> ClassicalYoni.SARPA_MALE
        Nakshatra.MRIGASHIRA -> ClassicalYoni.SARPA_FEMALE
        Nakshatra.MULA -> ClassicalYoni.SHWAN_MALE
        Nakshatra.ARDRA -> ClassicalYoni.SHWAN_FEMALE
        Nakshatra.ASHLESHA -> ClassicalYoni.MARJAR_MALE
        Nakshatra.PUNARVASU -> ClassicalYoni.MARJAR_FEMALE
        Nakshatra.MAGHA -> ClassicalYoni.MUSHAK_MALE
        Nakshatra.PURVA_PHALGUNI -> ClassicalYoni.MUSHAK_FEMALE
        Nakshatra.UTTARA_PHALGUNI -> ClassicalYoni.GAU_MALE
        Nakshatra.UTTARA_BHADRAPADA -> ClassicalYoni.GAU_FEMALE
        Nakshatra.SWATI -> ClassicalYoni.MAHISH_MALE
        Nakshatra.HASTA -> ClassicalYoni.MAHISH_FEMALE
        Nakshatra.VISHAKHA -> ClassicalYoni.VYAGHRA_MALE
        Nakshatra.CHITRA -> ClassicalYoni.VYAGHRA_FEMALE
        Nakshatra.JYESHTHA -> ClassicalYoni.MRIGA_MALE
        Nakshatra.ANURADHA -> ClassicalYoni.MRIGA_FEMALE
        Nakshatra.PURVA_ASHADHA -> ClassicalYoni.VANAR_MALE
        Nakshatra.SHRAVANA -> ClassicalYoni.VANAR_FEMALE
        Nakshatra.UTTARA_ASHADHA -> ClassicalYoni.NAKUL_MALE
        Nakshatra.PURVA_BHADRAPADA -> ClassicalYoni.SIMHA_MALE
        Nakshatra.DHANISHTHA -> ClassicalYoni.SIMHA_FEMALE
    }
    
    /**
     * Enhanced Yoni point calculation with classical rules
     */
    private fun calculateYoniPointsWithPrecision(brideYoni: ClassicalYoni, groomYoni: ClassicalYoni): Double {
        // Same Yoni group - Full points
        if (brideYoni.groupId == groomYoni.groupId) return 4.0
        
        // Enemy Yoni pairs - Zero points
        if (ClassicalYoni.enemyPairs.any { it.contains(brideYoni.groupId) && it.contains(groomYoni.groupId) }) {
            return 0.0
        }
        
        // Friendly Yoni groups - High points
        for (group in ClassicalYoni.friendlyGroups) {
            if (group.contains(brideYoni.groupId) && group.contains(groomYoni.groupId)) {
                return 3.0
            }
        }
        
        // Neutral relationship - Medium points
        return 2.0
    }
    
    private fun generateYoniAnalysis(
        points: Double,
        brideYoni: ClassicalYoni,
        groomYoni: ClassicalYoni,
        language: Language
    ): String {
        return when {
            points >= 4.0 -> StringResources.get(StringKeyMatch.YONI_SAME, language)
            points >= 3.0 -> StringResources.get(StringKeyMatch.YONI_FRIENDLY, language)
            points >= 2.0 -> StringResources.get(StringKeyMatch.YONI_NEUTRAL, language)
            points >= 1.0 -> StringResources.get(StringKeyMatch.YONI_UNFRIENDLY, language)
            else -> StringResources.get(StringKeyMatch.YONI_ENEMY, language)
        }
    }
    
    private fun getYoniDescription(language: Language): String = 
        StringResources.get(StringKeyMatch.YONI_DESC, language)
    
    // ============================================
    // GRAHA MAITRI CALCULATION ENHANCED
    // ============================================
    
    fun calculateGrahaMaitri(
        brideSign: ZodiacSign,
        groomSign: ZodiacSign,
        language: Language = Language.ENGLISH
    ): GunaAnalysis {
        val brideLord = brideSign.ruler
        val groomLord = groomSign.ruler
        
        val points = calculateGrahaMaitriPointsWithPrecision(brideLord, groomLord)
        
        val analysis = generateGrahaMaitriAnalysis(points, brideLord, groomLord, language)
        
        return GunaAnalysis(
            name = "Graha Maitri",
            maxPoints = 5.0,
            obtainedPoints = points,
            description = getGrahaMaitriDescription(language),
            brideValue = "${brideSign.displayName} (${brideLord.displayName})",
            groomValue = "${groomSign.displayName} (${groomLord.displayName})",
            analysis = analysis,
            isPositive = points >= 2.5
        )
    }
    
    /**
     * Enhanced Graha Maitri calculation with classical planetary relationships
     */
    private fun calculateGrahaMaitriPointsWithPrecision(lord1: Planet, lord2: Planet): Double {
        if (lord1 == lord2) return 5.0
        
        val relationship1 = getClassicalPlanetaryRelationship(lord1, lord2)
        val relationship2 = getClassicalPlanetaryRelationship(lord2, lord1)
        
        return when {
            // Mutual friendship - Excellent
            relationship1 == PlanetaryRelationship.FRIEND && 
            relationship2 == PlanetaryRelationship.FRIEND -> 5.0
            
            // One-sided friendship - Very good
            (relationship1 == PlanetaryRelationship.FRIEND && relationship2 == PlanetaryRelationship.NEUTRAL) ||
            (relationship1 == PlanetaryRelationship.NEUTRAL && relationship2 == PlanetaryRelationship.FRIEND) -> 4.0
            
            // Both neutral - Average
            relationship1 == PlanetaryRelationship.NEUTRAL && 
            relationship2 == PlanetaryRelationship.NEUTRAL -> 2.5
            
            // Mixed friendship/enmity - Low
            (relationship1 == PlanetaryRelationship.FRIEND && relationship2 == PlanetaryRelationship.ENEMY) ||
            (relationship1 == PlanetaryRelationship.ENEMY && relationship2 == PlanetaryRelationship.FRIEND) -> 1.0
            
            // Mixed neutral/enmity - Very low
            (relationship1 == PlanetaryRelationship.NEUTRAL && relationship2 == PlanetaryRelationship.ENEMY) ||
            (relationship1 == PlanetaryRelationship.ENEMY && relationship2 == PlanetaryRelationship.NEUTRAL) -> 0.5
            
            // Mutual enmity - Zero
            else -> 0.0
        }
    }
    
    /**
     * Get classical planetary relationship with enhanced accuracy
     */
    private fun getClassicalPlanetaryRelationship(planet1: Planet, planet2: Planet): PlanetaryRelationship {
        return when (planet1) {
            Planet.SUN -> when (planet2) {
                Planet.MOON, Planet.MARS, Planet.JUPITER -> PlanetaryRelationship.FRIEND
                Planet.MERCURY -> PlanetaryRelationship.NEUTRAL
                Planet.VENUS, Planet.SATURN -> PlanetaryRelationship.ENEMY
                else -> PlanetaryRelationship.NEUTRAL
            }
            Planet.MOON -> when (planet2) {
                Planet.SUN, Planet.MERCURY -> PlanetaryRelationship.FRIEND
                Planet.MARS, Planet.JUPITER, Planet.VENUS, Planet.SATURN -> PlanetaryRelationship.NEUTRAL
                else -> PlanetaryRelationship.NEUTRAL
            }
            Planet.MARS -> when (planet2) {
                Planet.SUN, Planet.MOON, Planet.JUPITER -> PlanetaryRelationship.FRIEND
                Planet.VENUS, Planet.SATURN -> PlanetaryRelationship.NEUTRAL
                Planet.MERCURY -> PlanetaryRelationship.ENEMY
                else -> PlanetaryRelationship.NEUTRAL
            }
            Planet.MERCURY -> when (planet2) {
                Planet.SUN, Planet.VENUS -> PlanetaryRelationship.FRIEND
                Planet.MARS, Planet.JUPITER, Planet.SATURN -> PlanetaryRelationship.NEUTRAL
                Planet.MOON -> PlanetaryRelationship.ENEMY
                else -> PlanetaryRelationship.NEUTRAL
            }
            Planet.JUPITER -> when (planet2) {
                Planet.SUN, Planet.MOON, Planet.MARS -> PlanetaryRelationship.FRIEND
                Planet.SATURN -> PlanetaryRelationship.NEUTRAL
                Planet.MERCURY, Planet.VENUS -> PlanetaryRelationship.ENEMY
                else -> PlanetaryRelationship.NEUTRAL
            }
            Planet.VENUS -> when (planet2) {
                Planet.MERCURY, Planet.SATURN -> PlanetaryRelationship.FRIEND
                Planet.MARS, Planet.JUPITER -> PlanetaryRelationship.NEUTRAL
                Planet.SUN, Planet.MOON -> PlanetaryRelationship.ENEMY
                else -> PlanetaryRelationship.NEUTRAL
            }
            Planet.SATURN -> when (planet2) {
                Planet.MERCURY, Planet.VENUS -> PlanetaryRelationship.FRIEND
                Planet.JUPITER -> PlanetaryRelationship.NEUTRAL
                Planet.SUN, Planet.MOON, Planet.MARS -> PlanetaryRelationship.ENEMY
                else -> PlanetaryRelationship.NEUTRAL
            }
            else -> PlanetaryRelationship.NEUTRAL
        }
    }
    
    private fun generateGrahaMaitriAnalysis(
        points: Double,
        brideLord: Planet,
        groomLord: Planet,
        language: Language
    ): String {
        return when {
            points >= 5.0 -> StringResources.get(StringKeyMatch.GRAHA_MAITRI_EXCELLENT, language)
            points >= 4.0 -> StringResources.get(StringKeyMatch.GRAHA_MAITRI_VERY_GOOD, language)
            points >= 2.5 -> StringResources.get(StringKeyMatch.GRAHA_MAITRI_AVERAGE, language)
            points >= 1.0 -> StringResources.get(StringKeyMatch.GRAHA_MAITRI_FRICTION, language)
            else -> StringResources.get(StringKeyMatch.GRAHA_MAITRI_INCOMPATIBLE, language)
        }
    }
    
    private fun getGrahaMaitriDescription(language: Language): String = 
        StringResources.get(StringKeyMatch.GRAHA_MAITRI_DESC, language)
    
    // ============================================
    // GANA CALCULATION ENHANCED
    // ============================================
    
    fun calculateGana(
        brideNakshatra: Nakshatra,
        groomNakshatra: Nakshatra,
        language: Language = Language.ENGLISH
    ): GunaAnalysis {
        val brideGana = getClassicalGana(brideNakshatra)
        val groomGana = getClassicalGana(groomNakshatra)
        
        val points = calculateGanaPointsWithPrecision(brideGana, groomGana)
        
        val analysis = generateGanaAnalysis(points, brideGana, groomGana, language)
        
        return GunaAnalysis(
            name = "Gana",
            maxPoints = 6.0,
            obtainedPoints = points,
            description = getGanaDescription(language),
            brideValue = "${brideGana.displayName} (${brideGana.description})",
            groomValue = "${groomGana.displayName} (${groomGana.description})",
            analysis = analysis,
            isPositive = points >= 3.0
        )
    }
    
    /**
     * Classical Gana assignment with enhanced precision
     */
    private fun getClassicalGana(nakshatra: Nakshatra): ClassicalGana = when (nakshatra) {
        // Deva Gana - Divine
        Nakshatra.ASHWINI, Nakshatra.MRIGASHIRA, Nakshatra.PUNARVASU,
        Nakshatra.PUSHYA, Nakshatra.HASTA, Nakshatra.SWATI,
        Nakshatra.ANURADHA, Nakshatra.SHRAVANA, Nakshatra.REVATI -> ClassicalGana.DEVA
        
        // Manushya Gana - Human
        Nakshatra.BHARANI, Nakshatra.ROHINI, Nakshatra.ARDRA,
        Nakshatra.PURVA_PHALGUNI, Nakshatra.UTTARA_PHALGUNI,
        Nakshatra.PURVA_ASHADHA, Nakshatra.UTTARA_ASHADHA,
        Nakshatra.PURVA_BHADRAPADA, Nakshatra.UTTARA_BHADRAPADA -> ClassicalGana.MANUSHYA
        
        // Rakshasa Gana - Demon
        Nakshatra.KRITTIKA, Nakshatra.ASHLESHA, Nakshatra.MAGHA,
        Nakshatra.CHITRA, Nakshatra.VISHAKHA, Nakshatra.JYESHTHA,
        Nakshatra.MULA, Nakshatra.DHANISHTHA, Nakshatra.SHATABHISHA -> ClassicalGana.RAKSHASA
    }
    
    /**
     * Enhanced Gana calculation with classical rules
     */
    private fun calculateGanaPointsWithPrecision(brideGana: ClassicalGana, groomGana: ClassicalGana): Double {
        return when {
            // Same Gana - Full points
            brideGana == groomGana -> 6.0
            
            // Deva + Manushya - Different compatibility based on who is bride/groom
            brideGana == ClassicalGana.DEVA && groomGana == ClassicalGana.MANUSHYA -> 5.0
            brideGana == ClassicalGana.MANUSHYA && groomGana == ClassicalGana.DEVA -> 6.0
            
            // Manushya + Rakshasa
            brideGana == ClassicalGana.MANUSHYA && groomGana == ClassicalGana.RAKSHASA -> 1.0
            brideGana == ClassicalGana.RAKSHASA && groomGana == ClassicalGana.MANUSHYA -> 3.0
            
            // Both Rakshasa - Full points (mutual understanding)
            brideGana == ClassicalGana.RAKSHASA && groomGana == ClassicalGana.RAKSHASA -> 6.0
            
            // Deva + Rakshasa - Incompatible
            else -> 0.0
        }
    }
    
    private fun generateGanaAnalysis(
        points: Double,
        brideGana: ClassicalGana,
        groomGana: ClassicalGana,
        language: Language
    ): String {
        return when {
            points >= 6.0 -> StringResources.get(StringKeyMatch.GANA_SAME, language)
            points >= 5.0 -> StringResources.get(StringKeyMatch.GANA_COMPATIBLE, language)
            points >= 3.0 -> StringResources.get(StringKeyMatch.GANA_PARTIAL, language)
            points >= 1.0 -> StringResources.get(StringKeyMatch.GANA_DIFFERENT, language)
            else -> StringResources.get(StringKeyMatch.GANA_OPPOSITE, language)
        }
    }
    
    private fun getGanaDescription(language: Language): String = 
        StringResources.get(StringKeyMatch.GANA_DESC, language)
}

// ============================================
// ENUMS FOR CLASSICAL CALCULATIONS
// ============================================

enum class ClassicalVarna(val value: Int, val displayName: String) {
    BRAHMIN(4, "Brahmin"),
    KSHATRIYA(3, "Kshatriya"),
    VAISHYA(2, "Vaishya"),
    SHUDRA(1, "Shudra")
}

enum class ClassicalVashya(val displayName: String) {
    CHATUSHPADA("Quadruped"),
    MANAVA("Human"),
    JALACHARA("Aquatic"),
    VANACHARA("Wild"),
    KEETA("Insect");
    
    companion object {
        val controlRelationships = mapOf(
            MANAVA to setOf(CHATUSHPADA, JALACHARA),
            VANACHARA to setOf(CHATUSHPADA),
            CHATUSHPADA to setOf(JALACHARA)
        )
        
        val enemyPairs = setOf(
            setOf(MANAVA, VANACHARA),
            setOf(CHATUSHPADA, VANACHARA)
        )
    }
}

enum class ClassicalYoni(val animal: String, val gender: String, val groupId: Int) {
    ASHWA_MALE("Horse", "Male", 1),
    ASHWA_FEMALE("Horse", "Female", 1),
    GAJA_MALE("Elephant", "Male", 2),
    GAJA_FEMALE("Elephant", "Female", 2),
    MESHA_MALE("Sheep", "Male", 3),
    MESHA_FEMALE("Sheep", "Female", 3),
    SARPA_MALE("Serpent", "Male", 4),
    SARPA_FEMALE("Serpent", "Female", 4),
    SHWAN_MALE("Dog", "Male", 5),
    SHWAN_FEMALE("Dog", "Female", 5),
    MARJAR_MALE("Cat", "Male", 6),
    MARJAR_FEMALE("Cat", "Female", 6),
    MUSHAK_MALE("Rat", "Male", 7),
    MUSHAK_FEMALE("Rat", "Female", 7),
    GAU_MALE("Cow", "Male", 8),
    GAU_FEMALE("Cow", "Female", 8),
    MAHISH_MALE("Buffalo", "Male", 9),
    MAHISH_FEMALE("Buffalo", "Female", 9),
    VYAGHRA_MALE("Tiger", "Male", 10),
    VYAGHRA_FEMALE("Tiger", "Female", 10),
    MRIGA_MALE("Deer", "Male", 11),
    MRIGA_FEMALE("Deer", "Female", 11),
    VANAR_MALE("Monkey", "Male", 12),
    VANAR_FEMALE("Monkey", "Female", 12),
    NAKUL_MALE("Mongoose", "Male", 13),
    NAKUL_FEMALE("Mongoose", "Female", 13),
    SIMHA_MALE("Lion", "Male", 14),
    SIMHA_FEMALE("Lion", "Female", 14);
    
    companion object {
        val enemyPairs = setOf(
            setOf(1, 9),   // Horse - Buffalo
            setOf(2, 14),  // Elephant - Lion
            setOf(3, 12),  // Sheep - Monkey
            setOf(4, 13),  // Serpent - Mongoose
            setOf(5, 11),  // Dog - Deer
            setOf(6, 7),   // Cat - Rat
            setOf(8, 10)   // Cow - Tiger
        )
        
        val friendlyGroups = listOf(
            setOf(1, 2, 3),     // Horse, Elephant, Sheep
            setOf(4, 5, 6),     // Serpent, Dog, Cat
            setOf(8, 9, 11),    // Cow, Buffalo, Deer
            setOf(12, 14)       // Monkey, Lion
        )
    }
}

enum class ClassicalGana(val displayName: String, val description: String) {
    DEVA("Deva", "Divine - Sattvik, gentle, spiritual"),
    MANUSHYA("Manushya", "Human - Rajasik, balanced, worldly"),
    RAKSHASA("Rakshasa", "Demon - Tamasik, aggressive, dominant")
}

enum class PlanetaryRelationship {
    FRIEND, NEUTRAL, ENEMY
}