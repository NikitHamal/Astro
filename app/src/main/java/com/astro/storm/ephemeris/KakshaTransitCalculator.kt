package com.astro.storm.ephemeris

import com.astro.storm.core.common.Language
import com.astro.storm.core.common.StringKeyAdvanced
import com.astro.storm.core.common.StringResources
import com.astro.storm.core.model.Planet
import com.astro.storm.core.model.PlanetPosition
import com.astro.storm.core.model.VedicChart
import com.astro.storm.core.model.ZodiacSign
import java.time.DateTimeException
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.temporal.ChronoUnit

/**
 * Kakshya (Kaksha) Transit Calculator
 *
 * Kakshya is the 8-fold division of each zodiac sign where each division spans exactly 3°45' (3.75°).
 * This micro-division system is used in Ashtakavarga for precise transit timing predictions.
 *
 * The 8 Kakshya lords in traditional sequence:
 * 1. Saturn (0°00' - 3°45')
 * 2. Jupiter (3°45' - 7°30')
 * 3. Mars (7°30' - 11°15')
 * 4. Sun (11°15' - 15°00')
 * 5. Venus (15°00' - 18°45')
 * 6. Mercury (18°45' - 22°30')
 * 7. Moon (22°30' - 26°15')
 * 8. Ascendant/Lagna (26°15' - 30°00')
 *
 * Transit Effects:
 * - When a planet transits through a Kakshya whose lord has a bindu in that sign, results are favorable
 * - When no bindu exists, results may be challenging or delayed
 * - The Kakshya system provides micro-timing within the broader sign transit
 *
 * @author AstroStorm - Ultra-Precision Vedic Astrology
 */
object KakshaTransitCalculator {

    /** Size of each Kakshya in degrees: 30° / 8 = 3.75° */
    private const val KAKSHYA_SIZE_DEGREES = 3.75

    /** Average planetary speeds in degrees per day (approximate) */
    private val AVERAGE_DAILY_MOTION = mapOf(
        Planet.SUN to 0.9856,
        Planet.MOON to 13.1764,
        Planet.MARS to 0.5240,
        Planet.MERCURY to 1.3833,
        Planet.JUPITER to 0.0831,
        Planet.VENUS to 1.2000,
        Planet.SATURN to 0.0335,
        Planet.RAHU to -0.0530,
        Planet.KETU to -0.0530
    )

    /** Kakshya lords in traditional sequence */
    private val KAKSHYA_LORDS = listOf(
        Planet.SATURN, Planet.JUPITER, Planet.MARS, Planet.SUN,
        Planet.VENUS, Planet.MERCURY, Planet.MOON
    )
    private const val KAKSHYA_LAGNA = "Lagna"

    /**
     * Result of Kakshya transit analysis
     */
    data class KakshaTransitResult(
        val chart: VedicChart,
        val analysisTime: LocalDateTime,
        val currentPositions: List<KakshaPlanetPosition>,
        val upcomingChanges: List<KakshaChange>,
        val favorablePeriods: List<FavorableKakshaPeriod>,
        val criticalTransits: List<CriticalKakshaTransit>,
        val overallKakshaScore: Double,
        val overallQuality: KakshaQuality
    )

    /**
     * Current Kakshya position for a planet
     */
    data class KakshaPlanetPosition(
        val planet: Planet,
        val sign: ZodiacSign,
        val longitude: Double,
        val degreeInSign: Double,
        val kakshaNumber: Int,
        val kakshaLord: String,
        val degreeStart: Double,
        val degreeEnd: Double,
        val hasBinbu: Boolean,
        val bavScore: Int,
        val quality: KakshaQuality,
        val interpretation: String,
        val interpretationNe: String,
        val timeToNextKaksha: Long, // in hours
        val nextKakshaLord: String
    )

    /**
     * Upcoming Kakshya change event
     */
    data class KakshaChange(
        val planet: Planet,
        val currentSign: ZodiacSign,
        val currentKaksha: Int,
        val nextKaksha: Int,
        val nextKakshaLord: String,
        val expectedTime: LocalDateTime,
        val hoursFromNow: Long,
        val willHaveBindu: Boolean,
        val impactDescription: String,
        val impactDescriptionNe: String
    )

    /**
     * Favorable Kakshya period
     */
    data class FavorableKakshaPeriod(
        val planet: Planet,
        val sign: ZodiacSign,
        val kakshaNumber: Int,
        val kakshaLord: String,
        val startTime: LocalDateTime,
        val endTime: LocalDateTime,
        val duration: Long, // in hours
        val bavScore: Int,
        val description: String,
        val descriptionNe: String
    )

    /**
     * Critical/Challenging Kakshya transit
     */
    data class CriticalKakshaTransit(
        val planet: Planet,
        val sign: ZodiacSign,
        val kakshaNumber: Int,
        val kakshaLord: String,
        val expectedTime: LocalDateTime,
        val reason: String,
        val reasonNe: String,
        val severity: CriticalSeverity
    )

    /**
     * Kakshya transit quality levels
     */
    enum class KakshaQuality(val score: Int) {
        EXCELLENT(4),
        GOOD(3),
        MODERATE(2),
        POOR(1)
    }

    /**
     * Critical transit severity
     */
    enum class CriticalSeverity {
        HIGH, MEDIUM, LOW
    }

    /**
     * Calculate comprehensive Kakshya transit analysis
     */
    fun calculateKakshaTransits(
        chart: VedicChart,
        transitPositions: List<PlanetPosition> = chart.planetPositions,
        analysisTime: LocalDateTime? = null,
        language: Language = Language.ENGLISH
    ): KakshaTransitResult? {
        return try {
            val effectiveAnalysisTime = analysisTime ?: LocalDateTime.now(resolveZoneId(chart.birthData.timezone))
            // Get pre-calculated Ashtakavarga for efficiency
            val ashtakavargaAnalysis = AshtakavargaCalculator.calculateAshtakavarga(chart)

            // Calculate current Kakshya positions for all planets
            val currentPositions = calculateCurrentKakshaPositions(
                chart, transitPositions, ashtakavargaAnalysis, effectiveAnalysisTime, language
            )

            // Calculate upcoming Kakshya changes (next 7 days)
            val upcomingChanges = calculateUpcomingChanges(
                chart, transitPositions, ashtakavargaAnalysis, effectiveAnalysisTime, language
            )

            // Identify favorable periods (next 30 days)
            val favorablePeriods = identifyFavorablePeriods(
                chart, transitPositions, ashtakavargaAnalysis, effectiveAnalysisTime, language
            )

            // Identify critical transits
            val criticalTransits = identifyCriticalTransits(
                chart, transitPositions, ashtakavargaAnalysis, effectiveAnalysisTime, language
            )

            // Calculate overall score
            val overallScore = calculateOverallScore(currentPositions)
            val overallQuality = when {
                overallScore >= 75 -> KakshaQuality.EXCELLENT
                overallScore >= 50 -> KakshaQuality.GOOD
                overallScore >= 25 -> KakshaQuality.MODERATE
                else -> KakshaQuality.POOR
            }

            KakshaTransitResult(
                chart = chart,
                analysisTime = effectiveAnalysisTime,
                currentPositions = currentPositions,
                upcomingChanges = upcomingChanges,
                favorablePeriods = favorablePeriods,
                criticalTransits = criticalTransits,
                overallKakshaScore = overallScore,
                overallQuality = overallQuality
            )
        } catch (e: Exception) {
            null
        }
    }

    /**
     * Calculate current Kakshya positions for all transit planets
     */
    private fun calculateCurrentKakshaPositions(
        chart: VedicChart,
        transitPositions: List<PlanetPosition>,
        analysis: AshtakavargaCalculator.AshtakavargaAnalysis,
        analysisTime: LocalDateTime,
        language: Language
    ): List<KakshaPlanetPosition> {
        val ashtakavargaPlanets = listOf(
            Planet.SUN, Planet.MOON, Planet.MARS, Planet.MERCURY,
            Planet.JUPITER, Planet.VENUS, Planet.SATURN
        )

        return transitPositions
            .filter { it.planet in ashtakavargaPlanets }
            .map { position ->
                val kakshaDetails = calculateKakshaDetails(position.longitude, chart, analysis)
                val bavScore = getBavScoreForKaksha(position.planet, kakshaDetails.sign, kakshaDetails.kakshaNumber, analysis)
                val hasBinbu = bavScore >= 4
                val quality = determineKakshaQuality(bavScore, hasBinbu)

                // Calculate time to next Kakshya
                val dailyMotion = AVERAGE_DAILY_MOTION[position.planet] ?: 1.0
                val degreesToNext = kakshaDetails.degreeEnd - kakshaDetails.degreeInSign
                val hoursToNext = (degreesToNext / dailyMotion * 24).toLong().coerceAtLeast(1)

                // Next Kakshya lord
                val nextKakshaNum = if (kakshaDetails.kakshaNumber >= 8) 1 else kakshaDetails.kakshaNumber + 1
                val nextKakshaLord = if (nextKakshaNum <= 7) {
                    KAKSHYA_LORDS[nextKakshaNum - 1].displayName
                } else {
                    KAKSHYA_LAGNA
                }

                val (interpretation, interpretationNe) = getKakshaInterpretation(
                    position.planet, kakshaDetails.kakshaLord, hasBinbu, quality, language
                )

                KakshaPlanetPosition(
                    planet = position.planet,
                    sign = kakshaDetails.sign,
                    longitude = position.longitude,
                    degreeInSign = kakshaDetails.degreeInSign,
                    kakshaNumber = kakshaDetails.kakshaNumber,
                    kakshaLord = kakshaDetails.kakshaLord,
                    degreeStart = kakshaDetails.degreeStart,
                    degreeEnd = kakshaDetails.degreeEnd,
                    hasBinbu = hasBinbu,
                    bavScore = bavScore,
                    quality = quality,
                    interpretation = interpretation,
                    interpretationNe = interpretationNe,
                    timeToNextKaksha = hoursToNext,
                    nextKakshaLord = nextKakshaLord
                )
            }
            .sortedBy { it.planet.ordinal }
    }

    /**
     * Calculate Kakshya details for a given longitude
     */
    private data class KakshaDetails(
        val sign: ZodiacSign,
        val degreeInSign: Double,
        val kakshaNumber: Int,
        val kakshaLord: String,
        val degreeStart: Double,
        val degreeEnd: Double
    )

    private fun calculateKakshaDetails(longitude: Double, chart: VedicChart, analysis: AshtakavargaCalculator.AshtakavargaAnalysis): KakshaDetails {
        val normalizedLong = VedicAstrologyUtils.normalizeLongitude(longitude)
        val sign = ZodiacSign.fromLongitude(normalizedLong)
        val degreeInSign = normalizedLong % 30.0

        val kakshaNumber = ((degreeInSign / KAKSHYA_SIZE_DEGREES).toInt() + 1).coerceIn(1, 8)
        val degreeStart = (kakshaNumber - 1) * KAKSHYA_SIZE_DEGREES
        val degreeEnd = kakshaNumber * KAKSHYA_SIZE_DEGREES

        val kakshaLord = if (kakshaNumber <= 7) {
            KAKSHYA_LORDS[kakshaNumber - 1].displayName
        } else {
            KAKSHYA_LAGNA
        }

        return KakshaDetails(
            sign = sign,
            degreeInSign = degreeInSign,
            kakshaNumber = kakshaNumber,
            kakshaLord = kakshaLord,
            degreeStart = degreeStart,
            degreeEnd = degreeEnd
        )
    }

    /**
     * Get BAV score for a planet in a specific Kakshya
     */
    private fun getBavScoreForKaksha(
        planet: Planet,
        sign: ZodiacSign,
        kakshaNumber: Int,
        analysis: AshtakavargaCalculator.AshtakavargaAnalysis
    ): Int {
        val bav = analysis.bhinnashtakavarga[planet] ?: return 0
        return bav.getBindusForSign(sign)
    }

    /**
     * Determine Kakshya quality based on BAV score and bindu presence
     */
    private fun determineKakshaQuality(bavScore: Int, hasBinbu: Boolean): KakshaQuality {
        return when {
            bavScore >= 6 && hasBinbu -> KakshaQuality.EXCELLENT
            bavScore >= 4 && hasBinbu -> KakshaQuality.GOOD
            bavScore >= 3 -> KakshaQuality.MODERATE
            else -> KakshaQuality.POOR
        }
    }

    /**
     * Calculate upcoming Kakshya changes
     */
    private fun calculateUpcomingChanges(
        chart: VedicChart,
        transitPositions: List<PlanetPosition>,
        analysis: AshtakavargaCalculator.AshtakavargaAnalysis,
        analysisTime: LocalDateTime,
        language: Language
    ): List<KakshaChange> {
        val ashtakavargaPlanets = listOf(
            Planet.SUN, Planet.MOON, Planet.MARS, Planet.MERCURY,
            Planet.JUPITER, Planet.VENUS, Planet.SATURN
        )

        val changes = mutableListOf<KakshaChange>()

        transitPositions
            .filter { it.planet in ashtakavargaPlanets }
            .forEach { position ->
                val kakshaDetails = calculateKakshaDetails(position.longitude, chart, analysis)
                val dailyMotion = AVERAGE_DAILY_MOTION[position.planet] ?: 1.0

                // Calculate next few Kakshya changes (up to 3 per planet)
                var currentDegree = kakshaDetails.degreeInSign
                var currentKaksha = kakshaDetails.kakshaNumber
                var accumulatedHours = 0L

                repeat(3) {
                    val nextKaksha = if (currentKaksha >= 8) 1 else currentKaksha + 1
                    val degreesToNext = if (currentKaksha >= 8) {
                        // Moving to next sign
                        30.0 - currentDegree + (nextKaksha - 1) * KAKSHYA_SIZE_DEGREES
                    } else {
                        nextKaksha * KAKSHYA_SIZE_DEGREES - currentDegree
                    }

                    val hoursToNext = (degreesToNext / dailyMotion * 24).toLong()
                    accumulatedHours += hoursToNext

                    // Only include changes within 7 days
                    if (accumulatedHours <= 7 * 24) {
                        val nextKakshaLord = if (nextKaksha <= 7) {
                            KAKSHYA_LORDS[nextKaksha - 1].displayName
                        } else {
                            KAKSHYA_LAGNA
                        }

                        // Check if next Kakshya will have bindu
                        val nextSign = if (currentKaksha >= 8) {
                            ZodiacSign.entries[(kakshaDetails.sign.ordinal + 1) % 12]
                        } else {
                            kakshaDetails.sign
                        }
                        val nextBav = getBavScoreForKaksha(position.planet, nextSign, nextKaksha, analysis)
                        val willHaveBindu = nextBav >= 4

                        val (impact, impactNe) = getKakshaChangeImpact(
                            position.planet, nextKakshaLord, willHaveBindu, language
                        )

                        changes.add(KakshaChange(
                            planet = position.planet,
                            currentSign = kakshaDetails.sign,
                            currentKaksha = currentKaksha,
                            nextKaksha = nextKaksha,
                            nextKakshaLord = nextKakshaLord,
                            expectedTime = analysisTime.plusHours(accumulatedHours),
                            hoursFromNow = accumulatedHours,
                            willHaveBindu = willHaveBindu,
                            impactDescription = impact,
                            impactDescriptionNe = impactNe
                        ))
                    }

                    currentDegree = nextKaksha * KAKSHYA_SIZE_DEGREES
                    currentKaksha = nextKaksha
                }
            }

        return changes.sortedBy { it.hoursFromNow }
    }

    /**
     * Identify favorable Kakshya periods
     */
    private fun identifyFavorablePeriods(
        chart: VedicChart,
        transitPositions: List<PlanetPosition>,
        analysis: AshtakavargaCalculator.AshtakavargaAnalysis,
        analysisTime: LocalDateTime,
        language: Language
    ): List<FavorableKakshaPeriod> {
        val periods = mutableListOf<FavorableKakshaPeriod>()

        // For each planet, find Kakshas with high BAV scores
        listOf(Planet.SUN, Planet.MOON, Planet.MARS, Planet.MERCURY,
            Planet.JUPITER, Planet.VENUS, Planet.SATURN).forEach { planet ->

            val position = transitPositions.find { it.planet == planet } ?: return@forEach
            val kakshaDetails = calculateKakshaDetails(position.longitude, chart, analysis)
            val dailyMotion = AVERAGE_DAILY_MOTION[planet] ?: 1.0

            // Check current and next few Kakshas for favorable ones
            var scanDegree = kakshaDetails.degreeInSign
            var accumulatedHours = 0L

            for (kaksha in 1..8) {
                val kakshaStart = (kaksha - 1) * KAKSHYA_SIZE_DEGREES
                val kakshaEnd = kaksha * KAKSHYA_SIZE_DEGREES
                val bavScore = getBavScoreForKaksha(planet, kakshaDetails.sign, kaksha, analysis)

                if (bavScore >= 5) { // Good BAV score
                    val kakshaLord = if (kaksha <= 7) {
                        KAKSHYA_LORDS[kaksha - 1].displayName
                    } else {
                        KAKSHYA_LAGNA
                    }

                    // Calculate timing
                    val hoursToStart = if (scanDegree < kakshaStart) {
                        ((kakshaStart - scanDegree) / dailyMotion * 24).toLong()
                    } else if (scanDegree >= kakshaStart && scanDegree < kakshaEnd) {
                        0L // Already in this Kakshya
                    } else {
                        continue // Past this Kakshya
                    }

                    val duration = (KAKSHYA_SIZE_DEGREES / dailyMotion * 24).toLong()

                    if (accumulatedHours + hoursToStart <= 30 * 24) { // Within 30 days
                        val (desc, descNe) = getFavorablePeriodDescription(planet, kakshaLord, bavScore, language)

                        periods.add(FavorableKakshaPeriod(
                            planet = planet,
                            sign = kakshaDetails.sign,
                            kakshaNumber = kaksha,
                            kakshaLord = kakshaLord,
                            startTime = analysisTime.plusHours(accumulatedHours + hoursToStart),
                            endTime = analysisTime.plusHours(accumulatedHours + hoursToStart + duration),
                            duration = duration,
                            bavScore = bavScore,
                            description = desc,
                            descriptionNe = descNe
                        ))
                    }
                }
            }
        }

        return periods.sortedBy { it.startTime }
    }

    /**
     * Identify critical Kakshya transits
     */
    private fun identifyCriticalTransits(
        chart: VedicChart,
        transitPositions: List<PlanetPosition>,
        analysis: AshtakavargaCalculator.AshtakavargaAnalysis,
        analysisTime: LocalDateTime,
        language: Language
    ): List<CriticalKakshaTransit> {
        val criticals = mutableListOf<CriticalKakshaTransit>()

        // Check Saturn and Mars transits especially
        listOf(Planet.SATURN, Planet.MARS, Planet.SUN).forEach { planet ->
            val position = transitPositions.find { it.planet == planet } ?: return@forEach
            val kakshaDetails = calculateKakshaDetails(position.longitude, chart, analysis)
            val bavScore = getBavScoreForKaksha(planet, kakshaDetails.sign, kakshaDetails.kakshaNumber, analysis)

            // Low BAV score in malefic planet's Kakshya is critical
            if (bavScore <= 2) {
                val kakshaLord = if (kakshaDetails.kakshaNumber <= 7) {
                    KAKSHYA_LORDS[kakshaDetails.kakshaNumber - 1].displayName
                } else {
                    KAKSHYA_LAGNA
                }

                val severity = when {
                    bavScore == 0 && (planet == Planet.SATURN || planet == Planet.MARS) -> CriticalSeverity.HIGH
                    bavScore <= 1 -> CriticalSeverity.MEDIUM
                    else -> CriticalSeverity.LOW
                }

                val (reason, reasonNe) = getCriticalTransitReason(planet, kakshaLord, bavScore, language)

                criticals.add(CriticalKakshaTransit(
                    planet = planet,
                    sign = kakshaDetails.sign,
                    kakshaNumber = kakshaDetails.kakshaNumber,
                    kakshaLord = kakshaLord,
                    expectedTime = analysisTime,
                    reason = reason,
                    reasonNe = reasonNe,
                    severity = severity
                ))
            }
        }

        return criticals.sortedByDescending { it.severity.ordinal }
    }

    /**
     * Calculate overall Kakshya score
     */
    private fun calculateOverallScore(positions: List<KakshaPlanetPosition>): Double {
        if (positions.isEmpty()) return 0.0

        val totalScore = positions.sumOf { pos ->
            val qualityScore = pos.quality.score * 10.0
            val bavBonus = pos.bavScore * 2.0
            val binduBonus = if (pos.hasBinbu) 10.0 else 0.0
            qualityScore + bavBonus + binduBonus
        }

        return (totalScore / positions.size).coerceIn(0.0, 100.0)
    }

    // Interpretation helpers

    private fun getKakshaInterpretation(
        planet: Planet,
        kakshaLord: String,
        hasBinbu: Boolean,
        quality: KakshaQuality,
        language: Language
    ): Pair<String, String> {
        val planetName = planet.displayName
        val qualityDesc = when (quality) {
            KakshaQuality.EXCELLENT -> "excellent" to "उत्कृष्ट"
            KakshaQuality.GOOD -> "good" to "राम्रो"
            KakshaQuality.MODERATE -> "moderate" to "मध्यम"
            KakshaQuality.POOR -> "challenging" to "चुनौतीपूर्ण"
        }

        val binduStatus = if (hasBinbu) {
            "has bindu support" to "बिन्दु समर्थन छ"
        } else {
            "lacks bindu support" to "बिन्दु समर्थन छैन"
        }

        val en = "$planetName in $kakshaLord Kakshya shows ${qualityDesc.first} results. Transit ${binduStatus.first}."
        val ne = "$planetName $kakshaLord कक्ष्यामा ${qualityDesc.second} परिणाम देखाउँछ। गोचरमा ${binduStatus.second}।"

        return en to ne
    }

    private fun getKakshaChangeImpact(
        planet: Planet,
        nextKakshaLord: String,
        willHaveBindu: Boolean,
        language: Language
    ): Pair<String, String> {
        val planetName = planet.displayName
        val outcome = if (willHaveBindu) {
            "favorable results expected" to "अनुकूल परिणाम अपेक्षित"
        } else {
            "exercise caution during this period" to "यस अवधिमा सावधानी अपनाउनुहोस्"
        }

        val en = "$planetName entering $nextKakshaLord Kakshya - ${outcome.first}."
        val ne = "$planetName $nextKakshaLord कक्ष्यामा प्रवेश - ${outcome.second}।"

        return en to ne
    }

    private fun getFavorablePeriodDescription(
        planet: Planet,
        kakshaLord: String,
        bavScore: Int,
        language: Language
    ): Pair<String, String> {
        val planetName = planet.displayName
        val en = "$planetName in $kakshaLord Kakshya with strong BAV score ($bavScore/8). Excellent period for ${getPlanetActivities(planet).first}."
        val ne = "$planetName $kakshaLord कक्ष्यामा बलियो BAV स्कोर ($bavScore/८)। ${getPlanetActivities(planet).second}को लागि उत्कृष्ट अवधि।"

        return en to ne
    }

    private fun getCriticalTransitReason(
        planet: Planet,
        kakshaLord: String,
        bavScore: Int,
        language: Language
    ): Pair<String, String> {
        val planetName = planet.displayName
        val en = "$planetName transiting weak Kakshya ($kakshaLord) with low BAV score ($bavScore/8). Delays or obstacles possible."
        val ne = "$planetName कमजोर कक्ष्या ($kakshaLord) मा कम BAV स्कोर ($bavScore/८) सहित। ढिलाइ वा बाधाहरू सम्भव।"

        return en to ne
    }

    private fun getPlanetActivities(planet: Planet): Pair<String, String> {
        return when (planet) {
            Planet.SUN -> "career advancement and authority matters" to "क्यारियर उन्नति र अधिकार सम्बन्धी कुरा"
            Planet.MOON -> "emotional well-being and family matters" to "भावनात्मक कल्याण र पारिवारिक कुरा"
            Planet.MARS -> "courageous actions and property matters" to "साहसिक कार्य र सम्पत्ति सम्बन्धी कुरा"
            Planet.MERCURY -> "communication and business dealings" to "सञ्चार र व्यापारिक कारोबार"
            Planet.JUPITER -> "education, spirituality and expansion" to "शिक्षा, आध्यात्मिकता र विस्तार"
            Planet.VENUS -> "relationships and financial gains" to "सम्बन्ध र आर्थिक लाभ"
            Planet.SATURN -> "disciplined work and long-term planning" to "अनुशासित काम र दीर्घकालीन योजना"
            else -> "general activities" to "सामान्य गतिविधिहरू"
        }
    }
    private fun resolveZoneId(timezone: String?): ZoneId {
        if (timezone.isNullOrBlank()) return ZoneId.systemDefault()
        return try {
            ZoneId.of(timezone.trim())
        } catch (_: DateTimeException) {
            val normalized = timezone.trim()
                .replace("UTC", "", ignoreCase = true)
                .replace("GMT", "", ignoreCase = true)
                .trim()
            if (normalized.isNotEmpty()) {
                runCatching { ZoneId.of("UTC$normalized") }.getOrElse { ZoneId.systemDefault() }
            } else {
                ZoneId.systemDefault()
            }
        }
    }
}

