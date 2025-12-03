package com.astro.storm.ephemeris

import com.astro.storm.data.model.Nakshatra
import com.astro.storm.data.model.Planet
import com.astro.storm.data.model.VedicChart
import com.astro.storm.data.model.ZodiacSign
import kotlin.math.abs

/**
 * Production-Grade Matchmaking Calculator for Vedic Astrology
 *
 * Implements the complete Ashtakoota (8 Guna) matching system used in
 * traditional Vedic marriage compatibility analysis.
 *
 * The 8 Gunas (Kootas) and their maximum points:
 * 1. Varna (Caste/Spiritual Level) - 1 point
 * 2. Vashya (Dominance/Control) - 2 points
 * 3. Tara (Birth Star Compatibility) - 3 points
 * 4. Yoni (Sexual/Physical Compatibility) - 4 points
 * 5. Graha Maitri (Planetary Friendship) - 5 points
 * 6. Gana (Temperament) - 6 points
 * 7. Bhakoot (Love/Health/Finance) - 7 points
 * 8. Nadi (Health/Progeny) - 8 points
 *
 * Total Maximum Points: 36
 *
 * Additionally implements:
 * - Manglik Dosha detection and analysis
 * - Advanced compatibility factors
 * - Detailed remedial suggestions
 *
 * Based on:
 * - Brihat Parashara Hora Shastra
 * - Muhurta Chintamani
 * - Jataka Parijata
 *
 * @author AstroStorm - Ultra-Precision Vedic Astrology
 */
object MatchmakingCalculator {

    // Maximum points for each Koota
    private const val MAX_VARNA = 1.0
    private const val MAX_VASHYA = 2.0
    private const val MAX_TARA = 3.0
    private const val MAX_YONI = 4.0
    private const val MAX_GRAHA_MAITRI = 5.0
    private const val MAX_GANA = 6.0
    private const val MAX_BHAKOOT = 7.0
    private const val MAX_NADI = 8.0
    private const val MAX_TOTAL = 36.0

    // Minimum recommended score thresholds
    private const val EXCELLENT_THRESHOLD = 28.0
    private const val GOOD_THRESHOLD = 21.0
    private const val AVERAGE_THRESHOLD = 18.0
    private const val POOR_THRESHOLD = 14.0

    /**
     * Varna (Spiritual/Caste) classification for each sign
     * Brahmin > Kshatriya > Vaishya > Shudra
     */
    enum class Varna(val value: Int, val displayName: String) {
        BRAHMIN(4, "Brahmin"),
        KSHATRIYA(3, "Kshatriya"),
        VAISHYA(2, "Vaishya"),
        SHUDRA(1, "Shudra")
    }

    /**
     * Vashya (Dominance) classification
     */
    enum class Vashya(val displayName: String) {
        CHATUSHPADA("Quadruped"),
        MANAVA("Human"),
        JALACHARA("Water Creature"),
        VANACHARA("Wild Animal"),
        KEETA("Insect")
    }

    /**
     * Gana (Temperament) classification
     */
    enum class Gana(val displayName: String, val description: String) {
        DEVA("Deva", "Divine - Sattvik, gentle, spiritual"),
        MANUSHYA("Manushya", "Human - Rajasik, balanced, worldly"),
        RAKSHASA("Rakshasa", "Demon - Tamasik, aggressive, dominant")
    }

    /**
     * Yoni (Sexual/Animal nature) classification - 14 types
     */
    enum class Yoni(val animal: String, val category: YoniCategory) {
        ASHWA("Horse", YoniCategory.MALE),
        GAJA("Elephant", YoniCategory.MALE),
        MESHA("Ram", YoniCategory.MALE),
        SARPA("Serpent", YoniCategory.MALE),
        SHWAN("Dog", YoniCategory.MALE),
        MARJAR("Cat", YoniCategory.MALE),
        MUSHAK("Rat", YoniCategory.MALE),
        GAU("Cow", YoniCategory.FEMALE),
        MAHISH("Buffalo", YoniCategory.FEMALE),
        VYAGHRA("Tiger", YoniCategory.FEMALE),
        MRIGA("Deer", YoniCategory.FEMALE),
        VANAR("Monkey", YoniCategory.FEMALE),
        NAKUL("Mongoose", YoniCategory.FEMALE),
        SIMHA("Lion", YoniCategory.FEMALE)
    }

    enum class YoniCategory {
        MALE, FEMALE
    }

    /**
     * Nadi (Health/Energy channel) classification
     */
    enum class Nadi(val displayName: String, val description: String) {
        ADI("Adi (Vata)", "Beginning - Wind element"),
        MADHYA("Madhya (Pitta)", "Middle - Fire element"),
        ANTYA("Antya (Kapha)", "End - Water element")
    }

    /**
     * Manglik Dosha levels
     */
    enum class ManglikDosha(val displayName: String, val severity: Int) {
        NONE("No Manglik Dosha", 0),
        PARTIAL("Partial Manglik", 1),
        FULL("Full Manglik", 2),
        DOUBLE("Double Manglik (Severe)", 3)
    }

    /**
     * Overall compatibility rating
     */
    enum class CompatibilityRating(val displayName: String, val description: String, val color: String) {
        EXCELLENT("Excellent Match", "Highly recommended for marriage. Strong compatibility across all factors.", "#4CAF50"),
        GOOD("Good Match", "Recommended. Good overall compatibility with minor differences.", "#8BC34A"),
        AVERAGE("Average Match", "Acceptable with some remedies. Moderate compatibility.", "#FFC107"),
        BELOW_AVERAGE("Below Average", "Caution advised. Several compatibility issues to address.", "#FF9800"),
        POOR("Poor Match", "Not recommended. Significant compatibility challenges.", "#F44336")
    }

    /**
     * Complete Guna (Koota) analysis result
     */
    data class GunaAnalysis(
        val name: String,
        val maxPoints: Double,
        val obtainedPoints: Double,
        val description: String,
        val brideValue: String,
        val groomValue: String,
        val analysis: String,
        val isPositive: Boolean
    ) {
        val percentage: Double get() = (obtainedPoints / maxPoints) * 100.0
    }

    /**
     * Manglik analysis result
     */
    data class ManglikAnalysis(
        val person: String,
        val dosha: ManglikDosha,
        val marsHouse: Int,
        val factors: List<String>,
        val cancellations: List<String>,
        val effectiveDosha: ManglikDosha
    )

    /**
     * Complete matchmaking result
     */
    data class MatchmakingResult(
        val brideChart: VedicChart,
        val groomChart: VedicChart,
        val gunaAnalyses: List<GunaAnalysis>,
        val totalPoints: Double,
        val maxPoints: Double,
        val percentage: Double,
        val rating: CompatibilityRating,
        val brideManglik: ManglikAnalysis,
        val groomManglik: ManglikAnalysis,
        val manglikCompatibility: String,
        val specialConsiderations: List<String>,
        val remedies: List<String>,
        val summary: String,
        val detailedAnalysis: String,
        val timestamp: Long = System.currentTimeMillis()
    ) {
        val varnaScore: Double get() = gunaAnalyses.find { it.name == "Varna" }?.obtainedPoints ?: 0.0
        val vashyaScore: Double get() = gunaAnalyses.find { it.name == "Vashya" }?.obtainedPoints ?: 0.0
        val taraScore: Double get() = gunaAnalyses.find { it.name == "Tara" }?.obtainedPoints ?: 0.0
        val yoniScore: Double get() = gunaAnalyses.find { it.name == "Yoni" }?.obtainedPoints ?: 0.0
        val grahaMaitriScore: Double get() = gunaAnalyses.find { it.name == "Graha Maitri" }?.obtainedPoints ?: 0.0
        val ganaScore: Double get() = gunaAnalyses.find { it.name == "Gana" }?.obtainedPoints ?: 0.0
        val bhakootScore: Double get() = gunaAnalyses.find { it.name == "Bhakoot" }?.obtainedPoints ?: 0.0
        val nadiScore: Double get() = gunaAnalyses.find { it.name == "Nadi" }?.obtainedPoints ?: 0.0

        fun toPlainText(): String = buildString {
            appendLine("═══════════════════════════════════════════════════════════")
            appendLine("              KUNDLI MILAN (MATCHMAKING) REPORT")
            appendLine("═══════════════════════════════════════════════════════════")
            appendLine()
            appendLine("BRIDE: ${brideChart.birthData.name}")
            appendLine("GROOM: ${groomChart.birthData.name}")
            appendLine()
            appendLine("─────────────────────────────────────────────────────────")
            appendLine("                    ASHTAKOOTA ANALYSIS")
            appendLine("─────────────────────────────────────────────────────────")
            appendLine()
            appendLine("GUNA        MAX    OBTAINED    STATUS")
            appendLine("─────────────────────────────────────────────────────────")
            gunaAnalyses.forEach { guna ->
                val status = if (guna.isPositive) "✓" else "✗"
                appendLine("${guna.name.padEnd(12)} ${guna.maxPoints.toInt().toString().padStart(3)}    ${String.format("%.1f", guna.obtainedPoints).padStart(8)}      $status")
            }
            appendLine("─────────────────────────────────────────────────────────")
            appendLine("TOTAL        ${maxPoints.toInt()}    ${String.format("%.1f", totalPoints).padStart(8)}      ${String.format("%.1f", percentage)}%")
            appendLine()
            appendLine("OVERALL RATING: ${rating.displayName}")
            appendLine()
            appendLine("─────────────────────────────────────────────────────────")
            appendLine("                    MANGLIK ANALYSIS")
            appendLine("─────────────────────────────────────────────────────────")
            appendLine()
            appendLine("Bride: ${brideManglik.effectiveDosha.displayName}")
            appendLine("Groom: ${groomManglik.effectiveDosha.displayName}")
            appendLine("Compatibility: $manglikCompatibility")
            appendLine()
            if (specialConsiderations.isNotEmpty()) {
                appendLine("─────────────────────────────────────────────────────────")
                appendLine("                  SPECIAL CONSIDERATIONS")
                appendLine("─────────────────────────────────────────────────────────")
                specialConsiderations.forEach { appendLine("• $it") }
                appendLine()
            }
            if (remedies.isNotEmpty()) {
                appendLine("─────────────────────────────────────────────────────────")
                appendLine("                    SUGGESTED REMEDIES")
                appendLine("─────────────────────────────────────────────────────────")
                remedies.forEach { appendLine("• $it") }
                appendLine()
            }
            appendLine("─────────────────────────────────────────────────────────")
            appendLine("                        SUMMARY")
            appendLine("─────────────────────────────────────────────────────────")
            appendLine(summary)
            appendLine()
            appendLine("═══════════════════════════════════════════════════════════")
            appendLine("Generated by AstroStorm - Ultra-Precision Vedic Astrology")
            appendLine("═══════════════════════════════════════════════════════════")
        }
    }

    /**
     * Calculate complete matchmaking analysis between two charts
     *
     * @param brideChart The bride's birth chart
     * @param groomChart The groom's birth chart
     * @return Complete MatchmakingResult with all analyses
     */
    fun calculateMatchmaking(brideChart: VedicChart, groomChart: VedicChart): MatchmakingResult {
        // Get Moon positions for both charts (primary reference for matchmaking)
        val brideMoon = brideChart.planetPositions.find { it.planet == Planet.MOON }
            ?: throw IllegalArgumentException("Bride chart missing Moon position")
        val groomMoon = groomChart.planetPositions.find { it.planet == Planet.MOON }
            ?: throw IllegalArgumentException("Groom chart missing Moon position")

        val brideMoonSign = brideMoon.sign
        val groomMoonSign = groomMoon.sign
        val (brideNakshatra, bridePada) = Nakshatra.fromLongitude(brideMoon.longitude)
        val (groomNakshatra, groomPada) = Nakshatra.fromLongitude(groomMoon.longitude)

        // Calculate all 8 Gunas
        val gunaAnalyses = listOf(
            calculateVarna(brideMoonSign, groomMoonSign),
            calculateVashya(brideMoonSign, groomMoonSign),
            calculateTara(brideNakshatra, groomNakshatra),
            calculateYoni(brideNakshatra, groomNakshatra),
            calculateGrahaMaitri(brideMoonSign, groomMoonSign),
            calculateGana(brideNakshatra, groomNakshatra),
            calculateBhakoot(brideMoonSign, groomMoonSign),
            calculateNadi(brideNakshatra, groomNakshatra)
        )

        val totalPoints = gunaAnalyses.sumOf { it.obtainedPoints }
        val percentage = (totalPoints / MAX_TOTAL) * 100.0

        // Determine rating
        val rating = when {
            totalPoints >= EXCELLENT_THRESHOLD -> CompatibilityRating.EXCELLENT
            totalPoints >= GOOD_THRESHOLD -> CompatibilityRating.GOOD
            totalPoints >= AVERAGE_THRESHOLD -> CompatibilityRating.AVERAGE
            totalPoints >= POOR_THRESHOLD -> CompatibilityRating.BELOW_AVERAGE
            else -> CompatibilityRating.POOR
        }

        // Manglik analysis
        val brideManglik = calculateManglikDosha(brideChart, "Bride")
        val groomManglik = calculateManglikDosha(groomChart, "Groom")
        val manglikCompatibility = assessManglikCompatibility(brideManglik, groomManglik)

        // Special considerations
        val specialConsiderations = calculateSpecialConsiderations(
            brideChart, groomChart, gunaAnalyses, brideManglik, groomManglik
        )

        // Remedies
        val remedies = calculateRemedies(
            gunaAnalyses, brideManglik, groomManglik, totalPoints
        )

        // Summary
        val summary = generateSummary(
            totalPoints, rating, gunaAnalyses, brideManglik, groomManglik
        )

        // Detailed analysis
        val detailedAnalysis = generateDetailedAnalysis(
            brideChart, groomChart, gunaAnalyses
        )

        return MatchmakingResult(
            brideChart = brideChart,
            groomChart = groomChart,
            gunaAnalyses = gunaAnalyses,
            totalPoints = totalPoints,
            maxPoints = MAX_TOTAL,
            percentage = percentage,
            rating = rating,
            brideManglik = brideManglik,
            groomManglik = groomManglik,
            manglikCompatibility = manglikCompatibility,
            specialConsiderations = specialConsiderations,
            remedies = remedies,
            summary = summary,
            detailedAnalysis = detailedAnalysis
        )
    }

    // ==================== VARNA (1 Point) ====================

    /**
     * Calculate Varna compatibility
     * Groom's Varna should be equal or higher than Bride's
     */
    private fun calculateVarna(brideSign: ZodiacSign, groomSign: ZodiacSign): GunaAnalysis {
        val brideVarna = getVarna(brideSign)
        val groomVarna = getVarna(groomSign)

        val points = if (groomVarna.value >= brideVarna.value) MAX_VARNA else 0.0

        val analysis = if (points > 0) {
            "Compatible: Groom's Varna (${groomVarna.displayName}) is equal to or higher than Bride's (${brideVarna.displayName})"
        } else {
            "Not compatible: Bride's Varna (${brideVarna.displayName}) is higher than Groom's (${groomVarna.displayName})"
        }

        return GunaAnalysis(
            name = "Varna",
            maxPoints = MAX_VARNA,
            obtainedPoints = points,
            description = "Spiritual compatibility and ego harmony",
            brideValue = brideVarna.displayName,
            groomValue = groomVarna.displayName,
            analysis = analysis,
            isPositive = points > 0
        )
    }

    private fun getVarna(sign: ZodiacSign): Varna {
        return when (sign) {
            ZodiacSign.CANCER, ZodiacSign.SCORPIO, ZodiacSign.PISCES -> Varna.BRAHMIN
            ZodiacSign.ARIES, ZodiacSign.LEO, ZodiacSign.SAGITTARIUS -> Varna.KSHATRIYA
            ZodiacSign.TAURUS, ZodiacSign.VIRGO, ZodiacSign.CAPRICORN -> Varna.VAISHYA
            ZodiacSign.GEMINI, ZodiacSign.LIBRA, ZodiacSign.AQUARIUS -> Varna.SHUDRA
        }
    }

    // ==================== VASHYA (2 Points) ====================

    /**
     * Calculate Vashya compatibility
     * Determines control/dominance in relationship
     */
    private fun calculateVashya(brideSign: ZodiacSign, groomSign: ZodiacSign): GunaAnalysis {
        val brideVashya = getVashya(brideSign)
        val groomVashya = getVashya(groomSign)

        val points = calculateVashyaPoints(brideVashya, groomVashya, brideSign, groomSign)

        val analysis = when {
            points >= 2.0 -> "Excellent mutual attraction and influence"
            points >= 1.0 -> "Good compatibility with moderate influence"
            points >= 0.5 -> "Partial compatibility, one-sided influence"
            else -> "Incompatible Vashya, potential control issues"
        }

        return GunaAnalysis(
            name = "Vashya",
            maxPoints = MAX_VASHYA,
            obtainedPoints = points,
            description = "Mutual attraction and influence",
            brideValue = brideVashya.displayName,
            groomValue = groomVashya.displayName,
            analysis = analysis,
            isPositive = points >= 1.0
        )
    }

    private fun getVashya(sign: ZodiacSign): Vashya {
        return when (sign) {
            ZodiacSign.ARIES, ZodiacSign.TAURUS -> Vashya.CHATUSHPADA
            ZodiacSign.GEMINI, ZodiacSign.VIRGO, ZodiacSign.LIBRA,
            ZodiacSign.SAGITTARIUS, ZodiacSign.AQUARIUS -> Vashya.MANAVA
            ZodiacSign.CANCER -> Vashya.JALACHARA
            ZodiacSign.LEO -> Vashya.VANACHARA
            ZodiacSign.SCORPIO -> Vashya.KEETA
            ZodiacSign.CAPRICORN -> Vashya.JALACHARA // First half water, second half quadruped
            ZodiacSign.PISCES -> Vashya.JALACHARA
        }
    }

    private fun calculateVashyaPoints(
        brideVashya: Vashya,
        groomVashya: Vashya,
        brideSign: ZodiacSign,
        groomSign: ZodiacSign
    ): Double {
        // Same Vashya = 2 points
        if (brideVashya == groomVashya) return 2.0

        // Check for Vashya food chain (one controls other)
        val vashyaMatrix = mapOf(
            Vashya.MANAVA to setOf(Vashya.CHATUSHPADA, Vashya.JALACHARA),
            Vashya.VANACHARA to setOf(Vashya.CHATUSHPADA),
            Vashya.CHATUSHPADA to setOf(Vashya.JALACHARA),
            Vashya.KEETA to setOf() // Keeta doesn't have natural Vashya
        )

        val groomControls = vashyaMatrix[groomVashya]?.contains(brideVashya) == true
        val brideControls = vashyaMatrix[brideVashya]?.contains(groomVashya) == true

        return when {
            groomControls && brideControls -> 1.5
            groomControls || brideControls -> 1.0
            // Check for enemy Vashyas
            isEnemyVashya(brideVashya, groomVashya) -> 0.0
            else -> 0.5
        }
    }

    private fun isEnemyVashya(vashya1: Vashya, vashya2: Vashya): Boolean {
        val enemies = mapOf(
            Vashya.MANAVA to setOf(Vashya.VANACHARA),
            Vashya.VANACHARA to setOf(Vashya.MANAVA),
            Vashya.CHATUSHPADA to setOf(Vashya.VANACHARA),
            Vashya.KEETA to setOf(Vashya.VANACHARA)
        )
        return enemies[vashya1]?.contains(vashya2) == true
    }

    // ==================== TARA (3 Points) ====================

    /**
     * Calculate Tara (Birth Star) compatibility
     * Based on counting from one nakshatra to another
     */
    private fun calculateTara(brideNakshatra: Nakshatra, groomNakshatra: Nakshatra): GunaAnalysis {
        // Count from Bride's nakshatra to Groom's
        val brideToGroom = calculateTaraNumber(brideNakshatra, groomNakshatra)
        // Count from Groom's nakshatra to Bride's
        val groomToBride = calculateTaraNumber(groomNakshatra, brideNakshatra)

        val brideTara = getTaraName(brideToGroom)
        val groomTara = getTaraName(groomToBride)

        // Points based on whether Taras are auspicious
        val brideAuspicious = isAuspiciousTara(brideToGroom)
        val groomAuspicious = isAuspiciousTara(groomToBride)

        val points = when {
            brideAuspicious && groomAuspicious -> 3.0
            brideAuspicious || groomAuspicious -> 1.5
            else -> 0.0
        }

        val analysis = when {
            points >= 3.0 -> "Both have auspicious Taras ($brideTara & $groomTara) - excellent star compatibility"
            points >= 1.5 -> "One auspicious Tara - moderate star compatibility"
            else -> "Both Taras are inauspicious - star incompatibility present"
        }

        return GunaAnalysis(
            name = "Tara",
            maxPoints = MAX_TARA,
            obtainedPoints = points,
            description = "Destiny and birth star compatibility",
            brideValue = "$brideNakshatra → $brideTara",
            groomValue = "$groomNakshatra → $groomTara",
            analysis = analysis,
            isPositive = points >= 1.5
        )
    }

    private fun calculateTaraNumber(fromNakshatra: Nakshatra, toNakshatra: Nakshatra): Int {
        val diff = (toNakshatra.number - fromNakshatra.number + 27) % 27
        return ((diff % 9) + 1)
    }

    private fun getTaraName(taraNumber: Int): String {
        return when (taraNumber) {
            1 -> "Janma (Birth)"
            2 -> "Sampat (Wealth)"
            3 -> "Vipat (Danger)"
            4 -> "Kshema (Wellbeing)"
            5 -> "Pratyak (Obstacle)"
            6 -> "Sadhana (Achievement)"
            7 -> "Vadha (Death)"
            8 -> "Mitra (Friend)"
            9 -> "Param Mitra (Best Friend)"
            else -> "Unknown"
        }
    }

    private fun isAuspiciousTara(taraNumber: Int): Boolean {
        // Sampat(2), Kshema(4), Sadhana(6), Mitra(8), Param Mitra(9) are auspicious
        return taraNumber in listOf(2, 4, 6, 8, 9)
    }

    // ==================== YONI (4 Points) ====================

    /**
     * Calculate Yoni (Sexual/Physical) compatibility
     * Based on animal nature associated with nakshatras
     */
    private fun calculateYoni(brideNakshatra: Nakshatra, groomNakshatra: Nakshatra): GunaAnalysis {
        val brideYoni = getYoni(brideNakshatra)
        val groomYoni = getYoni(groomNakshatra)

        val points = calculateYoniPoints(brideYoni, groomYoni)

        val analysis = when {
            points >= 4.0 -> "Same Yoni - perfect physical and instinctual compatibility"
            points >= 3.0 -> "Friendly Yonis - very good physical compatibility"
            points >= 2.0 -> "Neutral Yonis - moderate physical compatibility"
            points >= 1.0 -> "Unfriendly Yonis - some physical incompatibility"
            else -> "Enemy Yonis - significant physical incompatibility"
        }

        return GunaAnalysis(
            name = "Yoni",
            maxPoints = MAX_YONI,
            obtainedPoints = points,
            description = "Physical and sexual compatibility",
            brideValue = "${brideYoni.animal} (${brideYoni.category})",
            groomValue = "${groomYoni.animal} (${groomYoni.category})",
            analysis = analysis,
            isPositive = points >= 2.0
        )
    }

    private fun getYoni(nakshatra: Nakshatra): Yoni {
        return when (nakshatra) {
            Nakshatra.ASHWINI, Nakshatra.SHATABHISHA -> Yoni.ASHWA
            Nakshatra.BHARANI, Nakshatra.REVATI -> Yoni.GAJA
            Nakshatra.PUSHYA, Nakshatra.KRITTIKA -> Yoni.MESHA
            Nakshatra.ROHINI, Nakshatra.MRIGASHIRA -> Yoni.SARPA
            Nakshatra.ARDRA, Nakshatra.MULA -> Yoni.SHWAN
            Nakshatra.PUNARVASU, Nakshatra.ASHLESHA -> Yoni.MARJAR
            Nakshatra.MAGHA, Nakshatra.PURVA_PHALGUNI -> Yoni.MUSHAK
            Nakshatra.UTTARA_PHALGUNI, Nakshatra.UTTARA_BHADRAPADA -> Yoni.GAU
            Nakshatra.HASTA, Nakshatra.SWATI -> Yoni.MAHISH
            Nakshatra.CHITRA, Nakshatra.VISHAKHA -> Yoni.VYAGHRA
            Nakshatra.ANURADHA, Nakshatra.JYESHTHA -> Yoni.MRIGA
            Nakshatra.PURVA_ASHADHA, Nakshatra.SHRAVANA -> Yoni.VANAR
            Nakshatra.UTTARA_ASHADHA, Nakshatra.PURVA_BHADRAPADA -> Yoni.NAKUL
            Nakshatra.DHANISHTHA -> Yoni.SIMHA
        }
    }

    private fun calculateYoniPoints(brideYoni: Yoni, groomYoni: Yoni): Double {
        // Same Yoni = 4 points
        if (brideYoni == groomYoni) return 4.0

        // Define enemy pairs
        val enemyPairs = setOf(
            setOf(Yoni.ASHWA, Yoni.MAHISH),
            setOf(Yoni.GAJA, Yoni.SIMHA),
            setOf(Yoni.MESHA, Yoni.VANAR),
            setOf(Yoni.SARPA, Yoni.NAKUL),
            setOf(Yoni.SHWAN, Yoni.MRIGA),
            setOf(Yoni.MARJAR, Yoni.MUSHAK),
            setOf(Yoni.GAU, Yoni.VYAGHRA)
        )

        if (enemyPairs.any { it.contains(brideYoni) && it.contains(groomYoni) }) {
            return 0.0 // Enemy Yonis
        }

        // Friendly Yonis (same category but not enemies)
        if (brideYoni.category == groomYoni.category) return 3.0

        // Neutral - different categories, not enemies
        return 2.0
    }

    // ==================== GRAHA MAITRI (5 Points) ====================

    /**
     * Calculate Graha Maitri (Planetary Friendship) compatibility
     * Based on the Moon sign lords' relationship
     */
    private fun calculateGrahaMaitri(brideSign: ZodiacSign, groomSign: ZodiacSign): GunaAnalysis {
        val brideLord = brideSign.ruler
        val groomLord = groomSign.ruler

        val points = calculateGrahaMaitriPoints(brideLord, groomLord)

        val relationship = getPlanetaryRelationship(brideLord, groomLord)

        val analysis = when {
            points >= 5.0 -> "Same lord or mutual friends - excellent mental compatibility"
            points >= 4.0 -> "One friend, one neutral - very good mental harmony"
            points >= 2.5 -> "Neutral relationship - average mental compatibility"
            points >= 1.0 -> "One enemy - some mental friction"
            else -> "Mutual enemies - significant mental incompatibility"
        }

        return GunaAnalysis(
            name = "Graha Maitri",
            maxPoints = MAX_GRAHA_MAITRI,
            obtainedPoints = points,
            description = "Mental compatibility and friendship",
            brideValue = "${brideSign.displayName} (${brideLord.displayName})",
            groomValue = "${groomSign.displayName} (${groomLord.displayName})",
            analysis = analysis,
            isPositive = points >= 2.5
        )
    }

    private fun calculateGrahaMaitriPoints(lord1: Planet, lord2: Planet): Double {
        if (lord1 == lord2) return 5.0

        val relationship1 = getPlanetaryFriendship(lord1, lord2)
        val relationship2 = getPlanetaryFriendship(lord2, lord1)

        return when {
            relationship1 == "Friend" && relationship2 == "Friend" -> 5.0
            relationship1 == "Friend" && relationship2 == "Neutral" -> 4.0
            relationship2 == "Friend" && relationship1 == "Neutral" -> 4.0
            relationship1 == "Neutral" && relationship2 == "Neutral" -> 2.5
            relationship1 == "Friend" && relationship2 == "Enemy" -> 1.0
            relationship2 == "Friend" && relationship1 == "Enemy" -> 1.0
            relationship1 == "Neutral" && relationship2 == "Enemy" -> 0.5
            relationship2 == "Neutral" && relationship1 == "Enemy" -> 0.5
            else -> 0.0 // Both enemies
        }
    }

    private fun getPlanetaryFriendship(planet1: Planet, planet2: Planet): String {
        val friendships = mapOf(
            Planet.SUN to Triple(
                listOf(Planet.MOON, Planet.MARS, Planet.JUPITER),
                listOf(Planet.MERCURY),
                listOf(Planet.VENUS, Planet.SATURN)
            ),
            Planet.MOON to Triple(
                listOf(Planet.SUN, Planet.MERCURY),
                listOf(Planet.MARS, Planet.JUPITER, Planet.VENUS, Planet.SATURN),
                listOf()
            ),
            Planet.MARS to Triple(
                listOf(Planet.SUN, Planet.MOON, Planet.JUPITER),
                listOf(Planet.VENUS, Planet.SATURN),
                listOf(Planet.MERCURY)
            ),
            Planet.MERCURY to Triple(
                listOf(Planet.SUN, Planet.VENUS),
                listOf(Planet.MARS, Planet.JUPITER, Planet.SATURN),
                listOf(Planet.MOON)
            ),
            Planet.JUPITER to Triple(
                listOf(Planet.SUN, Planet.MOON, Planet.MARS),
                listOf(Planet.SATURN),
                listOf(Planet.MERCURY, Planet.VENUS)
            ),
            Planet.VENUS to Triple(
                listOf(Planet.MERCURY, Planet.SATURN),
                listOf(Planet.MARS, Planet.JUPITER),
                listOf(Planet.SUN, Planet.MOON)
            ),
            Planet.SATURN to Triple(
                listOf(Planet.MERCURY, Planet.VENUS),
                listOf(Planet.JUPITER),
                listOf(Planet.SUN, Planet.MOON, Planet.MARS)
            )
        )

        val (friends, neutrals, enemies) = friendships[planet1] ?: return "Neutral"

        return when (planet2) {
            in friends -> "Friend"
            in enemies -> "Enemy"
            else -> "Neutral"
        }
    }

    private fun getPlanetaryRelationship(planet1: Planet, planet2: Planet): String {
        val rel1 = getPlanetaryFriendship(planet1, planet2)
        val rel2 = getPlanetaryFriendship(planet2, planet1)

        return when {
            rel1 == "Friend" && rel2 == "Friend" -> "Mutual Friends"
            rel1 == "Enemy" && rel2 == "Enemy" -> "Mutual Enemies"
            rel1 == "Friend" || rel2 == "Friend" -> "One Friend"
            rel1 == "Enemy" || rel2 == "Enemy" -> "One Enemy"
            else -> "Neutral"
        }
    }

    // ==================== GANA (6 Points) ====================

    /**
     * Calculate Gana (Temperament) compatibility
     * Deva, Manushya, or Rakshasa
     */
    private fun calculateGana(brideNakshatra: Nakshatra, groomNakshatra: Nakshatra): GunaAnalysis {
        val brideGana = getGana(brideNakshatra)
        val groomGana = getGana(groomNakshatra)

        val points = calculateGanaPoints(brideGana, groomGana)

        val analysis = when {
            points >= 6.0 -> "Same Gana - perfect temperamental harmony"
            points >= 5.0 -> "Compatible Ganas (Deva-Manushya) - good harmony"
            points >= 3.0 -> "Partially compatible - some adjustment needed"
            points >= 1.0 -> "Different temperaments - significant adjustment required"
            else -> "Opposite temperaments (Deva-Rakshasa) - major incompatibility"
        }

        return GunaAnalysis(
            name = "Gana",
            maxPoints = MAX_GANA,
            obtainedPoints = points,
            description = "Temperament and behavior compatibility",
            brideValue = brideGana.displayName,
            groomValue = groomGana.displayName,
            analysis = analysis,
            isPositive = points >= 3.0
        )
    }

    private fun getGana(nakshatra: Nakshatra): Gana {
        return when (nakshatra) {
            Nakshatra.ASHWINI, Nakshatra.MRIGASHIRA, Nakshatra.PUNARVASU,
            Nakshatra.PUSHYA, Nakshatra.HASTA, Nakshatra.SWATI,
            Nakshatra.ANURADHA, Nakshatra.SHRAVANA, Nakshatra.REVATI -> Gana.DEVA

            Nakshatra.BHARANI, Nakshatra.ROHINI, Nakshatra.ARDRA,
            Nakshatra.PURVA_PHALGUNI, Nakshatra.UTTARA_PHALGUNI, Nakshatra.UTTARA_ASHADHA,
            Nakshatra.PURVA_BHADRAPADA, Nakshatra.UTTARA_BHADRAPADA -> Gana.MANUSHYA

            Nakshatra.KRITTIKA, Nakshatra.ASHLESHA, Nakshatra.MAGHA,
            Nakshatra.CHITRA, Nakshatra.VISHAKHA, Nakshatra.JYESHTHA,
            Nakshatra.MULA, Nakshatra.PURVA_ASHADHA, Nakshatra.DHANISHTHA,
            Nakshatra.SHATABHISHA -> Gana.RAKSHASA
        }
    }

    private fun calculateGanaPoints(brideGana: Gana, groomGana: Gana): Double {
        return when {
            brideGana == groomGana -> 6.0
            // Deva-Manushya combinations
            (brideGana == Gana.DEVA && groomGana == Gana.MANUSHYA) -> 5.0
            (brideGana == Gana.MANUSHYA && groomGana == Gana.DEVA) -> 6.0 // Groom Deva is better
            // Manushya-Rakshasa combinations
            (brideGana == Gana.MANUSHYA && groomGana == Gana.RAKSHASA) -> 1.0
            (brideGana == Gana.RAKSHASA && groomGana == Gana.MANUSHYA) -> 3.0
            // Deva-Rakshasa - most incompatible
            else -> 0.0
        }
    }

    // ==================== BHAKOOT (7 Points) ====================

    /**
     * Calculate Bhakoot (Love/Health/Finance) compatibility
     * Based on house relationship between Moon signs
     */
    private fun calculateBhakoot(brideSign: ZodiacSign, groomSign: ZodiacSign): GunaAnalysis {
        val brideNumber = brideSign.number
        val groomNumber = groomSign.number

        val (points, doshaType) = calculateBhakootPoints(brideNumber, groomNumber)

        val analysis = when {
            points >= 7.0 -> "No Bhakoot dosha - excellent compatibility for love, health, and finances"
            doshaType == "6-8" -> "6-8 Bhakoot Dosha - health concerns may arise"
            doshaType == "9-5" -> "9-5 Bhakoot Dosha - may affect progeny/children"
            doshaType == "2-12" -> "2-12 Bhakoot Dosha - financial concerns possible"
            else -> "Bhakoot incompatibility present"
        }

        return GunaAnalysis(
            name = "Bhakoot",
            maxPoints = MAX_BHAKOOT,
            obtainedPoints = points,
            description = "Love, health, and financial compatibility",
            brideValue = brideSign.displayName,
            groomValue = groomSign.displayName,
            analysis = analysis,
            isPositive = points >= 7.0
        )
    }

    private fun calculateBhakootPoints(brideNumber: Int, groomNumber: Int): Pair<Double, String> {
        val diff = abs(brideNumber - groomNumber)
        val reverseDiff = 12 - diff

        // Check for Bhakoot Dosha patterns
        // 2-12 pattern
        if ((diff == 1 || diff == 11) || (reverseDiff == 1 || reverseDiff == 11)) {
            // Check for cancellation by same lord
            val brideSign = ZodiacSign.entries[brideNumber - 1]
            val groomSign = ZodiacSign.entries[groomNumber - 1]
            if (brideSign.ruler == groomSign.ruler) {
                return 7.0 to "Cancelled"
            }
            return 0.0 to "2-12"
        }

        // 6-8 pattern (most serious)
        if (diff == 5 || diff == 7 || reverseDiff == 5 || reverseDiff == 7) {
            val brideSign = ZodiacSign.entries[brideNumber - 1]
            val groomSign = ZodiacSign.entries[groomNumber - 1]
            if (brideSign.ruler == groomSign.ruler) {
                return 7.0 to "Cancelled"
            }
            return 0.0 to "6-8"
        }

        // 9-5 pattern
        if (diff == 4 || diff == 8 || reverseDiff == 4 || reverseDiff == 8) {
            val brideSign = ZodiacSign.entries[brideNumber - 1]
            val groomSign = ZodiacSign.entries[groomNumber - 1]
            if (brideSign.ruler == groomSign.ruler) {
                return 7.0 to "Cancelled"
            }
            return 0.0 to "9-5"
        }

        // No dosha
        return 7.0 to "None"
    }

    // ==================== NADI (8 Points) ====================

    /**
     * Calculate Nadi (Health/Progeny) compatibility
     * Most important factor - affects health and children
     */
    private fun calculateNadi(brideNakshatra: Nakshatra, groomNakshatra: Nakshatra): GunaAnalysis {
        val brideNadi = getNadi(brideNakshatra)
        val groomNadi = getNadi(groomNakshatra)

        // Same Nadi = Nadi Dosha (very serious)
        val (points, hasDosha) = if (brideNadi == groomNadi) {
            // Check for cancellation conditions
            val cancelled = checkNadiDoshaCancellation(brideNakshatra, groomNakshatra)
            if (cancelled) 8.0 to false else 0.0 to true
        } else {
            8.0 to false
        }

        val analysis = when {
            hasDosha -> "NADI DOSHA: Same Nadi (${brideNadi.displayName}) - serious concern for health and progeny"
            points >= 8.0 -> "Different Nadis - excellent health and progeny compatibility"
            else -> "Partial Nadi compatibility"
        }

        return GunaAnalysis(
            name = "Nadi",
            maxPoints = MAX_NADI,
            obtainedPoints = points,
            description = "Health and progeny compatibility (most important)",
            brideValue = brideNadi.displayName,
            groomValue = groomNadi.displayName,
            analysis = analysis,
            isPositive = !hasDosha
        )
    }

    private fun getNadi(nakshatra: Nakshatra): Nadi {
        return when (nakshatra) {
            Nakshatra.ASHWINI, Nakshatra.ARDRA, Nakshatra.PUNARVASU,
            Nakshatra.UTTARA_PHALGUNI, Nakshatra.HASTA, Nakshatra.JYESHTHA,
            Nakshatra.MULA, Nakshatra.SHATABHISHA, Nakshatra.PURVA_BHADRAPADA -> Nadi.ADI

            Nakshatra.BHARANI, Nakshatra.MRIGASHIRA, Nakshatra.PUSHYA,
            Nakshatra.PURVA_PHALGUNI, Nakshatra.CHITRA, Nakshatra.ANURADHA,
            Nakshatra.PURVA_ASHADHA, Nakshatra.DHANISHTHA, Nakshatra.UTTARA_BHADRAPADA -> Nadi.MADHYA

            Nakshatra.KRITTIKA, Nakshatra.ROHINI, Nakshatra.ASHLESHA,
            Nakshatra.MAGHA, Nakshatra.SWATI, Nakshatra.VISHAKHA,
            Nakshatra.UTTARA_ASHADHA, Nakshatra.SHRAVANA, Nakshatra.REVATI -> Nadi.ANTYA
        }
    }

    private fun checkNadiDoshaCancellation(brideNakshatra: Nakshatra, groomNakshatra: Nakshatra): Boolean {
        // Cancellation if same nakshatra but different padas (quarter)
        // This is a simplified check - full implementation would check Moon signs too
        return brideNakshatra == groomNakshatra
    }

    // ==================== MANGLIK DOSHA ====================

    /**
     * Calculate Manglik Dosha for a chart
     */
    private fun calculateManglikDosha(chart: VedicChart, person: String): ManglikAnalysis {
        val mars = chart.planetPositions.find { it.planet == Planet.MARS }
            ?: return ManglikAnalysis(person, ManglikDosha.NONE, 0, emptyList(), emptyList(), ManglikDosha.NONE)

        val marsHouse = mars.house
        val factors = mutableListOf<String>()
        val cancellations = mutableListOf<String>()

        // Manglik houses: 1, 2, 4, 7, 8, 12
        val manglikHouses = listOf(1, 2, 4, 7, 8, 12)
        val isManglik = marsHouse in manglikHouses

        if (!isManglik) {
            return ManglikAnalysis(person, ManglikDosha.NONE, marsHouse, factors, cancellations, ManglikDosha.NONE)
        }

        factors.add("Mars in ${marsHouse}th house")

        var doshaLevel = when (marsHouse) {
            7, 8 -> ManglikDosha.FULL  // Most severe
            1, 4, 12 -> ManglikDosha.FULL
            2 -> ManglikDosha.PARTIAL // Less severe
            else -> ManglikDosha.NONE
        }

        // Check for cancellation factors
        val ascendantSign = ZodiacSign.fromLongitude(chart.ascendant)

        // Cancellation 1: Mars in own sign or exalted
        if (mars.sign in listOf(ZodiacSign.ARIES, ZodiacSign.SCORPIO, ZodiacSign.CAPRICORN)) {
            cancellations.add("Mars in own sign/exalted sign")
        }

        // Cancellation 2: Mars aspected by benefics
        val jupiter = chart.planetPositions.find { it.planet == Planet.JUPITER }
        if (jupiter != null) {
            val jupiterAspects = listOf(jupiter.house, (jupiter.house + 4) % 12 + 1, (jupiter.house + 6) % 12 + 1, (jupiter.house + 8) % 12 + 1)
            if (marsHouse in jupiterAspects) {
                cancellations.add("Jupiter aspects Mars")
            }
        }

        // Cancellation 3: Mars in specific signs for specific houses
        if (marsHouse == 1 && mars.sign in listOf(ZodiacSign.ARIES, ZodiacSign.LEO, ZodiacSign.SAGITTARIUS)) {
            cancellations.add("Mars in fiery sign in 1st house")
        }

        if (marsHouse == 2 && mars.sign in listOf(ZodiacSign.GEMINI, ZodiacSign.VIRGO)) {
            cancellations.add("Mars in Mercury's sign in 2nd house")
        }

        if (marsHouse == 4 && mars.sign == ZodiacSign.ARIES) {
            cancellations.add("Mars in Aries in 4th house")
        }

        if (marsHouse == 7 && mars.sign in listOf(ZodiacSign.CANCER, ZodiacSign.CAPRICORN)) {
            cancellations.add("Mars exalted/debilitated in 7th house")
        }

        if (marsHouse == 8 && mars.sign in listOf(ZodiacSign.SAGITTARIUS, ZodiacSign.PISCES)) {
            cancellations.add("Mars in Jupiter's sign in 8th house")
        }

        if (marsHouse == 12 && mars.sign in listOf(ZodiacSign.TAURUS, ZodiacSign.LIBRA)) {
            cancellations.add("Mars in Venus's sign in 12th house")
        }

        // Cancellation 4: Venus in 1st or 7th
        val venus = chart.planetPositions.find { it.planet == Planet.VENUS }
        if (venus != null && venus.house in listOf(1, 7)) {
            cancellations.add("Venus in 1st or 7th house")
        }

        // Cancellation 5: Jupiter in Kendra
        if (jupiter != null && jupiter.house in listOf(1, 4, 7, 10)) {
            cancellations.add("Jupiter in Kendra")
        }

        // Determine effective dosha
        val effectiveDosha = when {
            cancellations.size >= 3 -> ManglikDosha.NONE
            cancellations.size >= 2 -> if (doshaLevel == ManglikDosha.FULL) ManglikDosha.PARTIAL else ManglikDosha.NONE
            cancellations.size >= 1 -> if (doshaLevel == ManglikDosha.FULL) ManglikDosha.PARTIAL else doshaLevel
            else -> doshaLevel
        }

        // Check for double Manglik (Rahu/Ketu or Saturn with Mars in same house)
        val saturn = chart.planetPositions.find { it.planet == Planet.SATURN }
        val rahu = chart.planetPositions.find { it.planet == Planet.RAHU }
        val ketu = chart.planetPositions.find { it.planet == Planet.KETU }

        if ((saturn?.house == marsHouse) || (rahu?.house == marsHouse) || (ketu?.house == marsHouse)) {
            if (effectiveDosha != ManglikDosha.NONE) {
                factors.add("Mars conjunct malefic - Double Manglik potential")
                return ManglikAnalysis(person, ManglikDosha.DOUBLE, marsHouse, factors, cancellations, ManglikDosha.DOUBLE)
            }
        }

        return ManglikAnalysis(person, doshaLevel, marsHouse, factors, cancellations, effectiveDosha)
    }

    /**
     * Assess Manglik compatibility between both partners
     */
    private fun assessManglikCompatibility(bride: ManglikAnalysis, groom: ManglikAnalysis): String {
        val brideLevel = bride.effectiveDosha.severity
        val groomLevel = groom.effectiveDosha.severity

        return when {
            brideLevel == 0 && groomLevel == 0 -> "Both non-Manglik - No concerns"
            brideLevel > 0 && groomLevel > 0 -> "Both Manglik - Doshas cancel each other"
            abs(brideLevel - groomLevel) <= 1 -> "Manageable difference - Minor remedies recommended"
            else -> "Significant Manglik imbalance - Remedies strongly advised"
        }
    }

    // ==================== SPECIAL CONSIDERATIONS ====================

    private fun calculateSpecialConsiderations(
        brideChart: VedicChart,
        groomChart: VedicChart,
        gunaAnalyses: List<GunaAnalysis>,
        brideManglik: ManglikAnalysis,
        groomManglik: ManglikAnalysis
    ): List<String> {
        val considerations = mutableListOf<String>()

        // Nadi Dosha check
        val nadiAnalysis = gunaAnalyses.find { it.name == "Nadi" }
        if (nadiAnalysis != null && nadiAnalysis.obtainedPoints == 0.0) {
            considerations.add("CRITICAL: Nadi Dosha present - May affect health and progeny. Consult a learned astrologer for remedies.")
        }

        // Bhakoot Dosha check
        val bhakootAnalysis = gunaAnalyses.find { it.name == "Bhakoot" }
        if (bhakootAnalysis != null && bhakootAnalysis.obtainedPoints == 0.0) {
            considerations.add("Bhakoot Dosha present - ${bhakootAnalysis.analysis}")
        }

        // Manglik imbalance
        if (brideManglik.effectiveDosha.severity > 0 && groomManglik.effectiveDosha.severity == 0) {
            considerations.add("Bride is Manglik while Groom is not - Matching with another Manglik or performing Kumbh Vivah recommended")
        } else if (groomManglik.effectiveDosha.severity > 0 && brideManglik.effectiveDosha.severity == 0) {
            considerations.add("Groom is Manglik while Bride is not - Matching with another Manglik or performing remedies recommended")
        }

        // Gana incompatibility
        val ganaAnalysis = gunaAnalyses.find { it.name == "Gana" }
        if (ganaAnalysis != null && ganaAnalysis.obtainedPoints == 0.0) {
            considerations.add("Gana incompatibility (Deva-Rakshasa) - May cause temperamental clashes")
        }

        // Multiple low scores
        val lowScores = gunaAnalyses.count { !it.isPositive }
        if (lowScores >= 4) {
            considerations.add("Multiple compatibility factors are below threshold - Overall harmony may require extra effort")
        }

        // Check 7th house lords relationship
        val brideAscendant = ZodiacSign.fromLongitude(brideChart.ascendant)
        val groomAscendant = ZodiacSign.fromLongitude(groomChart.ascendant)
        val bride7thLord = ZodiacSign.entries[(brideAscendant.number + 5) % 12].ruler
        val groom7thLord = ZodiacSign.entries[(groomAscendant.number + 5) % 12].ruler

        val lordRelationship = getPlanetaryFriendship(bride7thLord, groom7thLord)
        if (lordRelationship == "Enemy") {
            considerations.add("7th house lords are mutual enemies - Extra care in partnership matters")
        }

        return considerations
    }

    // ==================== REMEDIES ====================

    private fun calculateRemedies(
        gunaAnalyses: List<GunaAnalysis>,
        brideManglik: ManglikAnalysis,
        groomManglik: ManglikAnalysis,
        totalPoints: Double
    ): List<String> {
        val remedies = mutableListOf<String>()

        // Nadi Dosha remedies
        val nadiAnalysis = gunaAnalyses.find { it.name == "Nadi" }
        if (nadiAnalysis != null && nadiAnalysis.obtainedPoints == 0.0) {
            remedies.add("For Nadi Dosha: Donate gold/grains, perform Mahamrityunjaya Japa, conduct Nadi Dosha Nivaran Puja")
        }

        // Bhakoot Dosha remedies
        val bhakootAnalysis = gunaAnalyses.find { it.name == "Bhakoot" }
        if (bhakootAnalysis != null && bhakootAnalysis.obtainedPoints == 0.0) {
            remedies.add("For Bhakoot Dosha: Worship respective Moon sign deities, donate as per sign elements")
        }

        // Manglik remedies
        if (brideManglik.effectiveDosha.severity > 0 || groomManglik.effectiveDosha.severity > 0) {
            remedies.add("For Manglik Dosha: Kumbh Vivah (marriage with pot/tree), Mangal Shanti Puja, wear Red Coral after consultation")
            if (brideManglik.effectiveDosha.severity > 0) {
                remedies.add("Bride should observe Tuesday fasts and recite Hanuman Chalisa")
            }
            if (groomManglik.effectiveDosha.severity > 0) {
                remedies.add("Groom should observe Tuesday fasts and recite Sunderkand")
            }
        }

        // Gana incompatibility
        val ganaAnalysis = gunaAnalyses.find { it.name == "Gana" }
        if (ganaAnalysis != null && ganaAnalysis.obtainedPoints <= 1.0) {
            remedies.add("For Gana incompatibility: Perform Ganapati Homam, worship Lord Ganesha together")
        }

        // Graha Maitri
        val grahaMaitri = gunaAnalyses.find { it.name == "Graha Maitri" }
        if (grahaMaitri != null && grahaMaitri.obtainedPoints <= 1.0) {
            remedies.add("For Graha Maitri: Strengthen both Moon signs' ruling planets through gemstones and mantras")
        }

        // Low overall score
        if (totalPoints < AVERAGE_THRESHOLD) {
            remedies.add("General: Perform Navgraha Shanti, couples should practice meditation together")
            remedies.add("Recommended: Visit a temple together on Mondays (for Moon) and conduct Rudrabhishek")
        }

        if (remedies.isEmpty()) {
            remedies.add("No specific remedies required - compatibility is favorable")
        }

        return remedies
    }

    // ==================== SUMMARY GENERATION ====================

    private fun generateSummary(
        totalPoints: Double,
        rating: CompatibilityRating,
        gunaAnalyses: List<GunaAnalysis>,
        brideManglik: ManglikAnalysis,
        groomManglik: ManglikAnalysis
    ): String {
        val strongPoints = gunaAnalyses.filter { it.isPositive && it.obtainedPoints >= it.maxPoints * 0.7 }
        val weakPoints = gunaAnalyses.filter { !it.isPositive }

        return buildString {
            appendLine("Overall Score: ${String.format("%.1f", totalPoints)}/36 (${String.format("%.1f", (totalPoints / 36.0) * 100)}%)")
            appendLine("Rating: ${rating.displayName}")
            appendLine()

            if (strongPoints.isNotEmpty()) {
                appendLine("Strengths:")
                strongPoints.forEach { append("• ${it.name}: ${it.analysis}\n") }
                appendLine()
            }

            if (weakPoints.isNotEmpty()) {
                appendLine("Areas of Concern:")
                weakPoints.forEach { append("• ${it.name}: ${it.analysis}\n") }
                appendLine()
            }

            // Manglik summary
            val manglikIssue = brideManglik.effectiveDosha.severity > 0 || groomManglik.effectiveDosha.severity > 0
            if (manglikIssue) {
                appendLine("Manglik Status: Attention required")
                appendLine("• Bride: ${brideManglik.effectiveDosha.displayName}")
                appendLine("• Groom: ${groomManglik.effectiveDosha.displayName}")
            } else {
                appendLine("Manglik Status: No Manglik Dosha concerns")
            }

            appendLine()
            appendLine("Final Recommendation: ${rating.description}")
        }
    }

    private fun generateDetailedAnalysis(
        brideChart: VedicChart,
        groomChart: VedicChart,
        gunaAnalyses: List<GunaAnalysis>
    ): String {
        return buildString {
            appendLine("DETAILED KOOTA ANALYSIS")
            appendLine()

            gunaAnalyses.forEach { guna ->
                appendLine("${guna.name} (${guna.obtainedPoints.toInt()}/${guna.maxPoints.toInt()} points)")
                appendLine("Description: ${guna.description}")
                appendLine("Bride: ${guna.brideValue}")
                appendLine("Groom: ${guna.groomValue}")
                appendLine("Analysis: ${guna.analysis}")
                appendLine()
            }
        }
    }
}
