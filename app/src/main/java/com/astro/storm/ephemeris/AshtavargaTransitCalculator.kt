package com.astro.storm.ephemeris

import com.astro.storm.core.common.Language
import com.astro.storm.core.model.Planet
import com.astro.storm.core.model.PlanetPosition
import com.astro.storm.core.model.VedicChart
import com.astro.storm.core.model.ZodiacSign
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.temporal.ChronoUnit

/**
 * Ashtavarga Transit Predictions Calculator
 *
 * This calculator provides comprehensive transit predictions based on Ashtakavarga
 * bindu scores. It extends the basic AshtakavargaCalculator to provide:
 *
 * 1. **Transit Intensity Analysis**
 *    - Predicts event intensity when planets transit through bindu-rich or bindu-poor signs
 *    - High SAV scores (30+) indicate favorable periods
 *    - Low SAV scores (<25) suggest caution needed
 *
 * 2. **Current Transit Overview**
 *    - Shows current transit positions with their SAV and BAV scores
 *    - Color-coded strength indicators
 *
 * 3. **Upcoming Transit Predictions**
 *    - Forecasts important transits for next 12 months
 *    - Highlights high-intensity periods (both favorable and challenging)
 *
 * 4. **Sign-wise Transit Analysis**
 *    - Identifies best signs for each planet's transit
 *    - Shows unfavorable transit signs to watch
 *
 * 5. **Reduction Rules (Shodhana)**
 *    - Applies Trikona and Ekadhipatya reductions
 *    - Shows refined Pinda values for advanced predictions
 *
 * Based on:
 * - Brihat Parasara Hora Shastra (BPHS) Chapters 66-72
 * - Jataka Parijata
 * - Traditional Ashtakavarga Transit methodology
 *
 * @author AstroStorm - Advanced Vedic Astrology
 */
object AshtavargaTransitCalculator {

    /**
     * Planets used in transit predictions (7 planets, excluding Rahu/Ketu)
     */
    private val TRANSIT_PLANETS = listOf(
        Planet.SUN, Planet.MOON, Planet.MARS, Planet.MERCURY,
        Planet.JUPITER, Planet.VENUS, Planet.SATURN
    )

    /**
     * Average transit duration per sign for each planet (in days)
     */
    private val TRANSIT_DURATION_DAYS = mapOf(
        Planet.SUN to 30,           // ~1 month per sign
        Planet.MOON to 2,           // ~2.5 days per sign
        Planet.MARS to 45,          // ~1.5 months per sign (avg, varies with retrogression)
        Planet.MERCURY to 30,       // ~1 month per sign (avg, varies)
        Planet.JUPITER to 365,      // ~1 year per sign
        Planet.VENUS to 28,         // ~1 month per sign (avg)
        Planet.SATURN to 912        // ~2.5 years per sign
    )

    /**
     * Complete Ashtavarga Transit Analysis Result
     */
    data class AshtavargaTransitResult(
        val chart: VedicChart,
        val analysisDate: LocalDateTime,
        val currentTransits: List<CurrentTransitInfo>,
        val upcomingTransits: List<UpcomingTransit>,
        val favorableSigns: Map<Planet, List<FavorableSign>>,
        val unfavorableSigns: Map<Planet, List<UnfavorableSign>>,
        val shodhanaResult: AshtakavargaCalculator.ShodhanaResult,
        val overallTransitScore: Double,
        val overallTransitQuality: TransitQuality,
        val interpretation: String,
        val interpretationNe: String,
        val recommendations: List<String>,
        val recommendationsNe: List<String>
    )

    /**
     * Current transit information for a planet
     */
    data class CurrentTransitInfo(
        val planet: Planet,
        val currentSign: ZodiacSign,
        val longitude: Double,
        val bavScore: Int,
        val savScore: Int,
        val quality: TransitQuality,
        val strength: TransitStrength,
        val entryDate: LocalDate?,
        val exitDate: LocalDate?,
        val progressPercent: Double,
        val interpretation: String,
        val interpretationNe: String,
        val houseFromMoon: Int,
        val houseFromAsc: Int
    )

    /**
     * Upcoming transit prediction
     */
    data class UpcomingTransit(
        val planet: Planet,
        val fromSign: ZodiacSign,
        val toSign: ZodiacSign,
        val transitDate: LocalDate,
        val bavScore: Int,
        val savScore: Int,
        val quality: TransitQuality,
        val strength: TransitStrength,
        val durationDays: Int,
        val interpretation: String,
        val interpretationNe: String,
        val isSignificant: Boolean,
        val eventProbability: EventProbability,
        val lifeAreas: List<LifeArea>
    )

    /**
     * Favorable sign for a planet's transit
     */
    data class FavorableSign(
        val sign: ZodiacSign,
        val bavScore: Int,
        val savScore: Int,
        val reason: String,
        val reasonNe: String,
        val rank: Int
    )

    /**
     * Unfavorable sign for a planet's transit
     */
    data class UnfavorableSign(
        val sign: ZodiacSign,
        val bavScore: Int,
        val savScore: Int,
        val caution: String,
        val cautionNe: String,
        val rank: Int
    )

    /**
     * Transit strength classification
     */
    enum class TransitStrength(
        val displayName: String,
        val displayNameNe: String,
        val score: Int
    ) {
        VERY_STRONG("Very Strong", "अत्यन्त बलियो", 5),
        STRONG("Strong", "बलियो", 4),
        AVERAGE("Average", "औसत", 3),
        WEAK("Weak", "कमजोर", 2),
        VERY_WEAK("Very Weak", "अत्यन्त कमजोर", 1)
    }

    /**
     * Transit quality (more granular than strength)
     */
    enum class TransitQuality(
        val displayName: String,
        val displayNameNe: String,
        val score: Int
    ) {
        EXCELLENT("Excellent", "उत्कृष्ट", 6),
        VERY_GOOD("Very Good", "धेरै राम्रो", 5),
        GOOD("Good", "राम्रो", 4),
        AVERAGE("Average", "औसत", 3),
        BELOW_AVERAGE("Below Average", "औसतभन्दा कम", 2),
        CHALLENGING("Challenging", "चुनौतीपूर्ण", 1),
        DIFFICULT("Difficult", "कठिन", 0)
    }

    /**
     * Event probability classification
     */
    enum class EventProbability(
        val displayName: String,
        val displayNameNe: String,
        val percent: Int
    ) {
        VERY_HIGH("Very High", "अत्यधिक उच्च", 90),
        HIGH("High", "उच्च", 75),
        MODERATE("Moderate", "मध्यम", 50),
        LOW("Low", "न्यून", 25),
        VERY_LOW("Very Low", "अत्यन्त न्यून", 10)
    }

    /**
     * Life areas affected by transits
     */
    enum class LifeArea(
        val displayName: String,
        val displayNameNe: String
    ) {
        CAREER("Career", "क्यारियर"),
        FINANCE("Finance", "वित्त"),
        HEALTH("Health", "स्वास्थ्य"),
        RELATIONSHIPS("Relationships", "सम्बन्ध"),
        FAMILY("Family", "परिवार"),
        SPIRITUALITY("Spirituality", "आध्यात्मिकता"),
        EDUCATION("Education", "शिक्षा"),
        TRAVEL("Travel", "यात्रा"),
        PROPERTY("Property", "सम्पत्ति"),
        COMMUNICATION("Communication", "संचार")
    }

    /**
     * Calculate comprehensive Ashtavarga transit analysis
     *
     * @param chart The natal VedicChart
     * @param transitPositions Current planetary positions (for transit analysis)
     * @param analysisDate The date for analysis (defaults to now)
     * @param language Language for interpretations
     * @return Complete transit analysis result
     */
    fun calculateAshtavargaTransits(
        chart: VedicChart,
        transitPositions: List<PlanetPosition> = chart.planetPositions,
        analysisDate: LocalDateTime = LocalDateTime.now(),
        language: Language = Language.ENGLISH
    ): AshtavargaTransitResult? {
        return try {
            // Calculate base Ashtakavarga
            val ashtakavarga = AshtakavargaCalculator.calculateAshtakavarga(chart)

            // Calculate Shodhana (reductions)
            val shodhana = AshtakavargaCalculator.calculateShodhana(ashtakavarga)

            // Get Moon and Ascendant positions for house calculations
            val moonSign = chart.planetPositions.find { it.planet == Planet.MOON }?.sign
                ?: ZodiacSign.fromLongitude(chart.ascendant)
            val ascSign = ZodiacSign.fromLongitude(chart.ascendant)

            // Calculate current transits
            val currentTransits = calculateCurrentTransits(
                transitPositions = transitPositions,
                ashtakavarga = ashtakavarga,
                moonSign = moonSign,
                ascSign = ascSign,
                analysisDate = analysisDate.toLocalDate()
            )

            // Calculate upcoming transits (next 12 months)
            val upcomingTransits = calculateUpcomingTransits(
                transitPositions = transitPositions,
                ashtakavarga = ashtakavarga,
                analysisDate = analysisDate.toLocalDate()
            )

            // Calculate favorable and unfavorable signs for each planet
            val favorableSigns = calculateFavorableSigns(ashtakavarga)
            val unfavorableSigns = calculateUnfavorableSigns(ashtakavarga)

            // Calculate overall transit score
            val overallScore = calculateOverallTransitScore(currentTransits)
            val overallQuality = getQualityFromScore(overallScore)

            // Generate interpretations
            val (interpretation, interpretationNe) = generateOverallInterpretation(
                currentTransits = currentTransits,
                overallQuality = overallQuality
            )

            // Generate recommendations
            val (recommendations, recommendationsNe) = generateRecommendations(
                currentTransits = currentTransits,
                upcomingTransits = upcomingTransits
            )

            AshtavargaTransitResult(
                chart = chart,
                analysisDate = analysisDate,
                currentTransits = currentTransits,
                upcomingTransits = upcomingTransits,
                favorableSigns = favorableSigns,
                unfavorableSigns = unfavorableSigns,
                shodhanaResult = shodhana,
                overallTransitScore = overallScore,
                overallTransitQuality = overallQuality,
                interpretation = interpretation,
                interpretationNe = interpretationNe,
                recommendations = recommendations,
                recommendationsNe = recommendationsNe
            )
        } catch (e: Exception) {
            null
        }
    }

    /**
     * Calculate current transit information for all planets
     */
    private fun calculateCurrentTransits(
        transitPositions: List<PlanetPosition>,
        ashtakavarga: AshtakavargaCalculator.AshtakavargaAnalysis,
        moonSign: ZodiacSign,
        ascSign: ZodiacSign,
        analysisDate: LocalDate
    ): List<CurrentTransitInfo> {
        return TRANSIT_PLANETS.mapNotNull { planet ->
            val position = transitPositions.find { it.planet == planet } ?: return@mapNotNull null
            val sign = position.sign
            val longitude = position.longitude

            // Get BAV and SAV scores
            val bavScore = ashtakavarga.bhinnashtakavarga[planet]?.getBindusForSign(sign) ?: 0
            val savScore = ashtakavarga.sarvashtakavarga.getBindusForSign(sign)

            // Calculate quality and strength
            val quality = calculateTransitQuality(bavScore, savScore)
            val strength = calculateTransitStrength(bavScore, savScore)

            // Calculate house from Moon and Ascendant
            val houseFromMoon = calculateHouse(sign, moonSign)
            val houseFromAsc = calculateHouse(sign, ascSign)

            // Estimate entry and exit dates
            val durationDays = TRANSIT_DURATION_DAYS[planet] ?: 30
            val degreeInSign = longitude % 30.0
            val progressPercent = (degreeInSign / 30.0) * 100
            val daysElapsed = (durationDays * progressPercent / 100).toInt()
            val daysRemaining = durationDays - daysElapsed

            val entryDate = analysisDate.minusDays(daysElapsed.toLong())
            val exitDate = analysisDate.plusDays(daysRemaining.toLong())

            // Generate interpretation
            val (interpretation, interpretationNe) = generateTransitInterpretation(
                planet = planet,
                sign = sign,
                bavScore = bavScore,
                savScore = savScore,
                quality = quality,
                houseFromMoon = houseFromMoon
            )

            CurrentTransitInfo(
                planet = planet,
                currentSign = sign,
                longitude = longitude,
                bavScore = bavScore,
                savScore = savScore,
                quality = quality,
                strength = strength,
                entryDate = entryDate,
                exitDate = exitDate,
                progressPercent = progressPercent,
                interpretation = interpretation,
                interpretationNe = interpretationNe,
                houseFromMoon = houseFromMoon,
                houseFromAsc = houseFromAsc
            )
        }
    }

    /**
     * Calculate upcoming transits for next 12 months
     */
    private fun calculateUpcomingTransits(
        transitPositions: List<PlanetPosition>,
        ashtakavarga: AshtakavargaCalculator.AshtakavargaAnalysis,
        analysisDate: LocalDate
    ): List<UpcomingTransit> {
        val upcomingTransits = mutableListOf<UpcomingTransit>()
        val endDate = analysisDate.plusMonths(12)

        TRANSIT_PLANETS.forEach { planet ->
            val currentPosition = transitPositions.find { it.planet == planet } ?: return@forEach
            val currentSign = currentPosition.sign
            val longitude = currentPosition.longitude
            val durationDays = TRANSIT_DURATION_DAYS[planet] ?: 30

            // Calculate when current transit ends
            val degreeInSign = longitude % 30.0
            val progressPercent = degreeInSign / 30.0
            val daysRemaining = (durationDays * (1 - progressPercent)).toInt()
            var nextTransitDate = analysisDate.plusDays(daysRemaining.toLong())
            var nextSign = getNextSign(currentSign)

            // Generate upcoming transits for next 12 months
            while (nextTransitDate.isBefore(endDate)) {
                val bavScore = ashtakavarga.bhinnashtakavarga[planet]?.getBindusForSign(nextSign) ?: 0
                val savScore = ashtakavarga.sarvashtakavarga.getBindusForSign(nextSign)
                val quality = calculateTransitQuality(bavScore, savScore)
                val strength = calculateTransitStrength(bavScore, savScore)
                val eventProbability = calculateEventProbability(bavScore, savScore)
                val lifeAreas = getAffectedLifeAreas(planet, nextSign)
                val isSignificant = isTransitSignificant(planet, bavScore, savScore)

                val (interpretation, interpretationNe) = generateUpcomingTransitInterpretation(
                    planet = planet,
                    sign = nextSign,
                    bavScore = bavScore,
                    savScore = savScore,
                    quality = quality
                )

                upcomingTransits.add(
                    UpcomingTransit(
                        planet = planet,
                        fromSign = getPreviousSign(nextSign),
                        toSign = nextSign,
                        transitDate = nextTransitDate,
                        bavScore = bavScore,
                        savScore = savScore,
                        quality = quality,
                        strength = strength,
                        durationDays = durationDays,
                        interpretation = interpretation,
                        interpretationNe = interpretationNe,
                        isSignificant = isSignificant,
                        eventProbability = eventProbability,
                        lifeAreas = lifeAreas
                    )
                )

                // Move to next sign
                nextTransitDate = nextTransitDate.plusDays(durationDays.toLong())
                nextSign = getNextSign(nextSign)
            }
        }

        // Sort by date and return significant transits first
        return upcomingTransits.sortedWith(
            compareByDescending<UpcomingTransit> { it.isSignificant }
                .thenBy { it.transitDate }
        )
    }

    /**
     * Calculate favorable signs for each planet
     */
    private fun calculateFavorableSigns(
        ashtakavarga: AshtakavargaCalculator.AshtakavargaAnalysis
    ): Map<Planet, List<FavorableSign>> {
        return TRANSIT_PLANETS.associateWith { planet ->
            ZodiacSign.entries.map { sign ->
                val bavScore = ashtakavarga.bhinnashtakavarga[planet]?.getBindusForSign(sign) ?: 0
                val savScore = ashtakavarga.sarvashtakavarga.getBindusForSign(sign)

                val (reason, reasonNe) = getFavorableReason(planet, sign, bavScore, savScore)

                Triple(FavorableSign(
                    sign = sign,
                    bavScore = bavScore,
                    savScore = savScore,
                    reason = reason,
                    reasonNe = reasonNe,
                    rank = 0
                ), bavScore, savScore)
            }
                .filter { (_, bav, sav) -> bav >= 4 && sav >= 28 }
                .sortedWith(compareByDescending<Triple<FavorableSign, Int, Int>> { it.second }
                    .thenByDescending { it.third })
                .take(5)
                .mapIndexed { index, (sign, _, _) -> sign.copy(rank = index + 1) }
        }
    }

    /**
     * Calculate unfavorable signs for each planet
     */
    private fun calculateUnfavorableSigns(
        ashtakavarga: AshtakavargaCalculator.AshtakavargaAnalysis
    ): Map<Planet, List<UnfavorableSign>> {
        return TRANSIT_PLANETS.associateWith { planet ->
            ZodiacSign.entries.map { sign ->
                val bavScore = ashtakavarga.bhinnashtakavarga[planet]?.getBindusForSign(sign) ?: 0
                val savScore = ashtakavarga.sarvashtakavarga.getBindusForSign(sign)

                val (caution, cautionNe) = getUnfavorableCaution(planet, sign, bavScore, savScore)

                Triple(UnfavorableSign(
                    sign = sign,
                    bavScore = bavScore,
                    savScore = savScore,
                    caution = caution,
                    cautionNe = cautionNe,
                    rank = 0
                ), bavScore, savScore)
            }
                .filter { (_, bav, sav) -> bav <= 2 || sav < 25 }
                .sortedWith(compareBy<Triple<UnfavorableSign, Int, Int>> { it.second }
                    .thenBy { it.third })
                .take(5)
                .mapIndexed { index, (sign, _, _) -> sign.copy(rank = index + 1) }
        }
    }

    /**
     * Calculate transit quality from BAV and SAV scores
     */
    private fun calculateTransitQuality(bavScore: Int, savScore: Int): TransitQuality {
        return when {
            bavScore >= 6 && savScore >= 32 -> TransitQuality.EXCELLENT
            bavScore >= 5 && savScore >= 30 -> TransitQuality.VERY_GOOD
            bavScore >= 4 && savScore >= 28 -> TransitQuality.GOOD
            bavScore >= 3 && savScore >= 25 -> TransitQuality.AVERAGE
            bavScore >= 2 && savScore >= 22 -> TransitQuality.BELOW_AVERAGE
            bavScore >= 1 && savScore >= 20 -> TransitQuality.CHALLENGING
            else -> TransitQuality.DIFFICULT
        }
    }

    /**
     * Calculate transit strength from BAV and SAV scores
     */
    private fun calculateTransitStrength(bavScore: Int, savScore: Int): TransitStrength {
        val combinedScore = (bavScore / 8.0) * 0.5 + (savScore / 56.0) * 0.5
        return when {
            combinedScore >= 0.7 -> TransitStrength.VERY_STRONG
            combinedScore >= 0.55 -> TransitStrength.STRONG
            combinedScore >= 0.4 -> TransitStrength.AVERAGE
            combinedScore >= 0.25 -> TransitStrength.WEAK
            else -> TransitStrength.VERY_WEAK
        }
    }

    /**
     * Calculate event probability based on bindu scores
     */
    private fun calculateEventProbability(bavScore: Int, savScore: Int): EventProbability {
        val combinedScore = bavScore * 5 + savScore
        return when {
            combinedScore >= 60 -> EventProbability.VERY_HIGH
            combinedScore >= 50 -> EventProbability.HIGH
            combinedScore >= 40 -> EventProbability.MODERATE
            combinedScore >= 30 -> EventProbability.LOW
            else -> EventProbability.VERY_LOW
        }
    }

    /**
     * Calculate house position from transit sign to reference sign
     */
    private fun calculateHouse(transitSign: ZodiacSign, referenceSign: ZodiacSign): Int {
        val diff = transitSign.ordinal - referenceSign.ordinal
        return if (diff >= 0) diff + 1 else diff + 13
    }

    /**
     * Get next sign in zodiac order
     */
    private fun getNextSign(sign: ZodiacSign): ZodiacSign {
        val nextIndex = (sign.ordinal + 1) % 12
        return ZodiacSign.entries[nextIndex]
    }

    /**
     * Get previous sign in zodiac order
     */
    private fun getPreviousSign(sign: ZodiacSign): ZodiacSign {
        val prevIndex = if (sign.ordinal == 0) 11 else sign.ordinal - 1
        return ZodiacSign.entries[prevIndex]
    }

    /**
     * Determine if a transit is significant
     */
    private fun isTransitSignificant(planet: Planet, bavScore: Int, savScore: Int): Boolean {
        // Slow planets are always significant
        if (planet in listOf(Planet.SATURN, Planet.JUPITER)) return true

        // Very high or very low scores are significant
        if (bavScore >= 6 || bavScore <= 1) return true
        if (savScore >= 32 || savScore <= 20) return true

        return false
    }

    /**
     * Get life areas affected by planet/sign combination
     */
    private fun getAffectedLifeAreas(planet: Planet, sign: ZodiacSign): List<LifeArea> {
        val areas = mutableListOf<LifeArea>()

        // Planet-based areas
        when (planet) {
            Planet.SUN -> areas.addAll(listOf(LifeArea.CAREER, LifeArea.HEALTH))
            Planet.MOON -> areas.addAll(listOf(LifeArea.FAMILY, LifeArea.HEALTH))
            Planet.MARS -> areas.addAll(listOf(LifeArea.PROPERTY, LifeArea.CAREER))
            Planet.MERCURY -> areas.addAll(listOf(LifeArea.COMMUNICATION, LifeArea.EDUCATION))
            Planet.JUPITER -> areas.addAll(listOf(LifeArea.SPIRITUALITY, LifeArea.EDUCATION, LifeArea.FINANCE))
            Planet.VENUS -> areas.addAll(listOf(LifeArea.RELATIONSHIPS, LifeArea.FINANCE))
            Planet.SATURN -> areas.addAll(listOf(LifeArea.CAREER, LifeArea.HEALTH))
            else -> {}
        }

        // Sign-based areas
        when (sign) {
            ZodiacSign.ARIES, ZodiacSign.LEO -> areas.add(LifeArea.CAREER)
            ZodiacSign.TAURUS, ZodiacSign.LIBRA -> areas.add(LifeArea.FINANCE)
            ZodiacSign.GEMINI, ZodiacSign.VIRGO -> areas.add(LifeArea.COMMUNICATION)
            ZodiacSign.CANCER -> areas.add(LifeArea.FAMILY)
            ZodiacSign.SCORPIO -> areas.add(LifeArea.SPIRITUALITY)
            ZodiacSign.SAGITTARIUS -> areas.add(LifeArea.TRAVEL)
            ZodiacSign.CAPRICORN -> areas.add(LifeArea.CAREER)
            ZodiacSign.AQUARIUS -> areas.add(LifeArea.EDUCATION)
            ZodiacSign.PISCES -> areas.add(LifeArea.SPIRITUALITY)
        }

        return areas.distinct().take(3)
    }

    /**
     * Calculate overall transit score from current transits
     */
    private fun calculateOverallTransitScore(currentTransits: List<CurrentTransitInfo>): Double {
        if (currentTransits.isEmpty()) return 0.0

        // Weight planets by importance
        val weights = mapOf(
            Planet.SATURN to 1.5,
            Planet.JUPITER to 1.4,
            Planet.MARS to 1.2,
            Planet.SUN to 1.1,
            Planet.VENUS to 1.0,
            Planet.MERCURY to 1.0,
            Planet.MOON to 0.9
        )

        var totalWeight = 0.0
        var weightedScore = 0.0

        currentTransits.forEach { transit ->
            val weight = weights[transit.planet] ?: 1.0
            val score = transit.quality.score / 6.0 * 100
            weightedScore += score * weight
            totalWeight += weight
        }

        return if (totalWeight > 0) weightedScore / totalWeight else 0.0
    }

    /**
     * Get quality from overall score
     */
    private fun getQualityFromScore(score: Double): TransitQuality {
        return when {
            score >= 85 -> TransitQuality.EXCELLENT
            score >= 70 -> TransitQuality.VERY_GOOD
            score >= 55 -> TransitQuality.GOOD
            score >= 45 -> TransitQuality.AVERAGE
            score >= 35 -> TransitQuality.BELOW_AVERAGE
            score >= 25 -> TransitQuality.CHALLENGING
            else -> TransitQuality.DIFFICULT
        }
    }

    /**
     * Generate interpretation for a current transit
     */
    private fun generateTransitInterpretation(
        planet: Planet,
        sign: ZodiacSign,
        bavScore: Int,
        savScore: Int,
        quality: TransitQuality,
        houseFromMoon: Int
    ): Pair<String, String> {
        val planetEffect = getPlanetEffect(planet)
        val houseEffect = getHouseEffect(houseFromMoon)

        val en = when (quality) {
            TransitQuality.EXCELLENT, TransitQuality.VERY_GOOD ->
                "${planet.displayName} in ${sign.displayName} (BAV: $bavScore, SAV: $savScore) brings excellent results for $planetEffect. Transit through ${houseFromMoon}th house enhances $houseEffect."
            TransitQuality.GOOD ->
                "${planet.displayName} transiting ${sign.displayName} (BAV: $bavScore, SAV: $savScore) is favorable for $planetEffect. Good support for $houseEffect in the ${houseFromMoon}th house."
            TransitQuality.AVERAGE ->
                "${planet.displayName} in ${sign.displayName} (BAV: $bavScore, SAV: $savScore) gives mixed results. Balance needed in matters of $planetEffect and $houseEffect."
            TransitQuality.BELOW_AVERAGE, TransitQuality.CHALLENGING ->
                "${planet.displayName} transit through ${sign.displayName} (BAV: $bavScore, SAV: $savScore) requires caution. Challenges possible in $planetEffect. Monitor $houseEffect carefully."
            TransitQuality.DIFFICULT ->
                "${planet.displayName} in ${sign.displayName} (BAV: $bavScore, SAV: $savScore) is challenging. Extra care needed for $planetEffect. Protect matters related to $houseEffect."
        }

        val ne = when (quality) {
            TransitQuality.EXCELLENT, TransitQuality.VERY_GOOD ->
                "${planet.displayName} ${sign.displayName} मा (BAV: $bavScore, SAV: $savScore) उत्कृष्ट फल दिन्छ। ${houseFromMoon} औं भावमा गोचरले राम्रो परिणाम ल्याउँछ।"
            TransitQuality.GOOD ->
                "${planet.displayName} ${sign.displayName} मा (BAV: $bavScore, SAV: $savScore) अनुकूल छ। ${houseFromMoon} औं भावमा राम्रो समर्थन।"
            TransitQuality.AVERAGE ->
                "${planet.displayName} ${sign.displayName} मा (BAV: $bavScore, SAV: $savScore) मिश्रित फल। सन्तुलन आवश्यक।"
            TransitQuality.BELOW_AVERAGE, TransitQuality.CHALLENGING ->
                "${planet.displayName} ${sign.displayName} मा (BAV: $bavScore, SAV: $savScore) सावधानी चाहिन्छ। चुनौतीहरू सम्भव।"
            TransitQuality.DIFFICULT ->
                "${planet.displayName} ${sign.displayName} मा (BAV: $bavScore, SAV: $savScore) कठिन छ। विशेष ध्यान आवश्यक।"
        }

        return Pair(en, ne)
    }

    /**
     * Generate interpretation for an upcoming transit
     */
    private fun generateUpcomingTransitInterpretation(
        planet: Planet,
        sign: ZodiacSign,
        bavScore: Int,
        savScore: Int,
        quality: TransitQuality
    ): Pair<String, String> {
        val durationText = when (planet) {
            Planet.SATURN -> "approximately 2.5 years"
            Planet.JUPITER -> "approximately 1 year"
            Planet.MARS -> "approximately 1.5 months"
            else -> "approximately 1 month"
        }

        val durationTextNe = when (planet) {
            Planet.SATURN -> "लगभग २.५ वर्ष"
            Planet.JUPITER -> "लगभग १ वर्ष"
            Planet.MARS -> "लगभग १.५ महिना"
            else -> "लगभग १ महिना"
        }

        val en = when (quality) {
            TransitQuality.EXCELLENT, TransitQuality.VERY_GOOD ->
                "${planet.displayName} entering ${sign.displayName} for $durationText with excellent scores (BAV: $bavScore, SAV: $savScore). Highly favorable period approaching."
            TransitQuality.GOOD ->
                "${planet.displayName} will transit ${sign.displayName} for $durationText with good scores (BAV: $bavScore, SAV: $savScore). Positive developments expected."
            TransitQuality.AVERAGE ->
                "${planet.displayName} moving to ${sign.displayName} for $durationText (BAV: $bavScore, SAV: $savScore). Mixed results likely, maintain balance."
            TransitQuality.BELOW_AVERAGE, TransitQuality.CHALLENGING ->
                "${planet.displayName} entering ${sign.displayName} for $durationText (BAV: $bavScore, SAV: $savScore). Prepare for some challenges ahead."
            TransitQuality.DIFFICULT ->
                "${planet.displayName} will transit ${sign.displayName} for $durationText with low scores (BAV: $bavScore, SAV: $savScore). Plan remedial measures."
        }

        val ne = when (quality) {
            TransitQuality.EXCELLENT, TransitQuality.VERY_GOOD ->
                "${planet.displayName} ${sign.displayName} मा $durationTextNe को लागि प्रवेश। उत्कृष्ट स्कोर (BAV: $bavScore, SAV: $savScore)। अत्यन्त अनुकूल समय आउँदैछ।"
            TransitQuality.GOOD ->
                "${planet.displayName} ${sign.displayName} मा $durationTextNe को लागि गोचर। राम्रो स्कोर (BAV: $bavScore, SAV: $savScore)। सकारात्मक विकास अपेक्षित।"
            TransitQuality.AVERAGE ->
                "${planet.displayName} ${sign.displayName} मा $durationTextNe को लागि सर्दैछ (BAV: $bavScore, SAV: $savScore)। मिश्रित फल सम्भव।"
            TransitQuality.BELOW_AVERAGE, TransitQuality.CHALLENGING ->
                "${planet.displayName} ${sign.displayName} मा $durationTextNe को लागि प्रवेश (BAV: $bavScore, SAV: $savScore)। केही चुनौतीहरूको लागि तयार रहनुहोस्।"
            TransitQuality.DIFFICULT ->
                "${planet.displayName} ${sign.displayName} मा $durationTextNe को लागि गोचर। कम स्कोर (BAV: $bavScore, SAV: $savScore)। उपचारात्मक उपाय योजना गर्नुहोस्।"
        }

        return Pair(en, ne)
    }

    /**
     * Generate overall interpretation
     */
    private fun generateOverallInterpretation(
        currentTransits: List<CurrentTransitInfo>,
        overallQuality: TransitQuality
    ): Pair<String, String> {
        val strongTransits = currentTransits.filter { it.quality in listOf(TransitQuality.EXCELLENT, TransitQuality.VERY_GOOD, TransitQuality.GOOD) }
        val weakTransits = currentTransits.filter { it.quality in listOf(TransitQuality.CHALLENGING, TransitQuality.DIFFICULT) }

        val en = buildString {
            append("Current overall transit quality is ${overallQuality.displayName}. ")
            if (strongTransits.isNotEmpty()) {
                append("Strong support from ${strongTransits.joinToString(", ") { it.planet.displayName }} transits. ")
            }
            if (weakTransits.isNotEmpty()) {
                append("Extra attention needed for ${weakTransits.joinToString(", ") { it.planet.displayName }} transits. ")
            }
            append("Plan important activities when Saturn and Jupiter are in favorable positions.")
        }

        val ne = buildString {
            append("वर्तमान समग्र गोचर गुणस्तर ${overallQuality.displayNameNe} छ। ")
            if (strongTransits.isNotEmpty()) {
                append("${strongTransits.joinToString(", ") { it.planet.displayName }} गोचरबाट बलियो समर्थन। ")
            }
            if (weakTransits.isNotEmpty()) {
                append("${weakTransits.joinToString(", ") { it.planet.displayName }} गोचरमा विशेष ध्यान चाहिन्छ। ")
            }
            append("शनि र गुरु अनुकूल स्थितिमा हुँदा महत्त्वपूर्ण क्रियाकलापहरू योजना गर्नुहोस्।")
        }

        return Pair(en, ne)
    }

    /**
     * Generate recommendations based on transit analysis
     */
    private fun generateRecommendations(
        currentTransits: List<CurrentTransitInfo>,
        upcomingTransits: List<UpcomingTransit>
    ): Pair<List<String>, List<String>> {
        val recommendationsEn = mutableListOf<String>()
        val recommendationsNe = mutableListOf<String>()

        // Saturn recommendations
        currentTransits.find { it.planet == Planet.SATURN }?.let { saturn ->
            when (saturn.quality) {
                TransitQuality.EXCELLENT, TransitQuality.VERY_GOOD, TransitQuality.GOOD -> {
                    recommendationsEn.add("Saturn's favorable transit supports career advancement and long-term planning. Good time for property investments.")
                    recommendationsNe.add("शनिको अनुकूल गोचरले क्यारियर प्रगति र दीर्घकालीन योजनालाई समर्थन गर्छ। सम्पत्ति लगानीको राम्रो समय।")
                }
                TransitQuality.CHALLENGING, TransitQuality.DIFFICULT -> {
                    recommendationsEn.add("During Saturn's challenging transit, maintain discipline and avoid major risks. Focus on completing existing projects.")
                    recommendationsNe.add("शनिको चुनौतीपूर्ण गोचरमा, अनुशासन कायम राख्नुहोस् र ठूला जोखिमहरूबाट बच्नुहोस्। अवस्थित परियोजनाहरू पूरा गर्नमा ध्यान दिनुहोस्।")
                }
                else -> {}
            }
        }

        // Jupiter recommendations
        currentTransits.find { it.planet == Planet.JUPITER }?.let { jupiter ->
            when (jupiter.quality) {
                TransitQuality.EXCELLENT, TransitQuality.VERY_GOOD, TransitQuality.GOOD -> {
                    recommendationsEn.add("Jupiter's blessing transit is excellent for education, spiritual growth, and expansion of wealth. Pursue new learning opportunities.")
                    recommendationsNe.add("गुरुको आशीर्वाद गोचर शिक्षा, आध्यात्मिक वृद्धि, र धन विस्तारको लागि उत्कृष्ट छ। नयाँ सिकाइ अवसरहरू पछ्याउनुहोस्।")
                }
                TransitQuality.CHALLENGING, TransitQuality.DIFFICULT -> {
                    recommendationsEn.add("During Jupiter's weak transit, avoid over-expansion and risky investments. Focus on consolidation rather than growth.")
                    recommendationsNe.add("गुरुको कमजोर गोचरमा, अत्यधिक विस्तार र जोखिमपूर्ण लगानीबाट बच्नुहोस्। वृद्धिभन्दा समेकनमा ध्यान दिनुहोस्।")
                }
                else -> {}
            }
        }

        // Upcoming significant transits
        val nextSignificant = upcomingTransits.filter { it.isSignificant }.take(2)
        nextSignificant.forEach { transit ->
            if (transit.quality in listOf(TransitQuality.EXCELLENT, TransitQuality.VERY_GOOD)) {
                recommendationsEn.add("Mark ${transit.transitDate}: ${transit.planet.displayName} enters favorable ${transit.toSign.displayName}. Plan important initiatives around this time.")
                recommendationsNe.add("${transit.transitDate} चिन्ह लगाउनुहोस्: ${transit.planet.displayName} अनुकूल ${transit.toSign.displayName} मा प्रवेश गर्छ। यस समय महत्त्वपूर्ण पहलहरू योजना गर्नुहोस्।")
            } else if (transit.quality in listOf(TransitQuality.CHALLENGING, TransitQuality.DIFFICULT)) {
                recommendationsEn.add("Prepare for ${transit.transitDate}: ${transit.planet.displayName}'s challenging entry into ${transit.toSign.displayName}. Complete important tasks before this date.")
                recommendationsNe.add("${transit.transitDate} को लागि तयारी गर्नुहोस्: ${transit.planet.displayName} को ${transit.toSign.displayName} मा चुनौतीपूर्ण प्रवेश। यो मिति अघि महत्त्वपूर्ण कार्यहरू पूरा गर्नुहोस्।")
            }
        }

        return Pair(recommendationsEn, recommendationsNe)
    }

    /**
     * Get planet effect description
     */
    private fun getPlanetEffect(planet: Planet): String {
        return when (planet) {
            Planet.SUN -> "authority, career, and vitality"
            Planet.MOON -> "emotions, mental peace, and relationships"
            Planet.MARS -> "energy, courage, and property matters"
            Planet.MERCURY -> "communication, business, and intellect"
            Planet.JUPITER -> "wisdom, fortune, and spiritual growth"
            Planet.VENUS -> "love, luxury, and creative pursuits"
            Planet.SATURN -> "discipline, career stability, and longevity"
            else -> "general life matters"
        }
    }

    /**
     * Get house effect description
     */
    private fun getHouseEffect(house: Int): String {
        return when (house) {
            1 -> "self and personality"
            2 -> "wealth and family"
            3 -> "courage and siblings"
            4 -> "home and happiness"
            5 -> "children and intelligence"
            6 -> "health and enemies"
            7 -> "partnerships and marriage"
            8 -> "transformation and occult"
            9 -> "luck and spirituality"
            10 -> "career and status"
            11 -> "gains and aspirations"
            12 -> "liberation and expenses"
            else -> "various life matters"
        }
    }

    /**
     * Get reason for favorable sign
     */
    private fun getFavorableReason(planet: Planet, sign: ZodiacSign, bavScore: Int, savScore: Int): Pair<String, String> {
        val en = "Strong bindu support (BAV: $bavScore, SAV: $savScore) ensures ${planet.displayName}'s beneficial results in ${sign.displayName}."
        val ne = "बलियो बिन्दु समर्थन (BAV: $bavScore, SAV: $savScore) ले ${sign.displayName} मा ${planet.displayName} को लाभदायक फल सुनिश्चित गर्छ।"
        return Pair(en, ne)
    }

    /**
     * Get caution for unfavorable sign
     */
    private fun getUnfavorableCaution(planet: Planet, sign: ZodiacSign, bavScore: Int, savScore: Int): Pair<String, String> {
        val en = "Low bindu scores (BAV: $bavScore, SAV: $savScore) indicate ${planet.displayName} may face obstacles in ${sign.displayName}. Extra precaution advised."
        val ne = "कम बिन्दु स्कोर (BAV: $bavScore, SAV: $savScore) संकेत गर्छ कि ${planet.displayName} ले ${sign.displayName} मा बाधाहरू सामना गर्न सक्छ। विशेष सावधानी सल्लाह दिइएको छ।"
        return Pair(en, ne)
    }
}
