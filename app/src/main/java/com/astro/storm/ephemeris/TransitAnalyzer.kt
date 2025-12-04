package com.astro.storm.ephemeris

import android.content.Context
import com.astro.storm.data.model.Planet
import com.astro.storm.data.model.PlanetPosition
import com.astro.storm.data.model.VedicChart
import com.astro.storm.data.model.ZodiacSign
import com.astro.storm.data.model.BirthData
import com.astro.storm.data.model.HouseSystem
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.temporal.ChronoUnit
import kotlin.math.abs
import kotlin.math.cos
import kotlin.math.PI

class TransitAnalyzer(private val context: Context) {

    private val ephemerisEngine = SwissEphemerisEngine(context)

    companion object {
        private val SUN_VEDHA = mapOf(3 to 9, 6 to 12, 10 to 4, 11 to 5)
        private val MOON_VEDHA = mapOf(1 to 5, 3 to 9, 6 to 12, 7 to 2, 10 to 4, 11 to 8)
        private val MARS_VEDHA = mapOf(3 to 12, 6 to 9, 11 to 5)
        private val MERCURY_VEDHA = mapOf(2 to 5, 4 to 3, 6 to 9, 8 to 1, 10 to 8, 11 to 12)
        private val JUPITER_VEDHA = mapOf(2 to 12, 5 to 4, 7 to 3, 9 to 10, 11 to 8)
        private val VENUS_VEDHA = mapOf(1 to 8, 2 to 7, 3 to 1, 4 to 10, 5 to 9, 8 to 5, 9 to 11, 11 to 6, 12 to 3)
        private val SATURN_VEDHA = mapOf(3 to 12, 6 to 9, 11 to 5)
        private val RAHU_KETU_VEDHA = mapOf(3 to 12, 6 to 9, 10 to 4, 11 to 5)

        private val FAVORABLE_TRANSITS = mapOf(
            Planet.SUN to listOf(3, 6, 10, 11),
            Planet.MOON to listOf(1, 3, 6, 7, 10, 11),
            Planet.MARS to listOf(3, 6, 11),
            Planet.MERCURY to listOf(2, 4, 6, 8, 10, 11),
            Planet.JUPITER to listOf(2, 5, 7, 9, 11),
            Planet.VENUS to listOf(1, 2, 3, 4, 5, 8, 9, 11, 12),
            Planet.SATURN to listOf(3, 6, 11),
            Planet.RAHU to listOf(3, 6, 10, 11),
            Planet.KETU to listOf(3, 6, 10, 11)
        )

        private val DIFFICULT_TRANSITS = mapOf(
            Planet.SUN to listOf(1, 2, 4, 5, 7, 8, 9, 12),
            Planet.MOON to listOf(2, 4, 5, 8, 9, 12),
            Planet.MARS to listOf(1, 2, 4, 5, 7, 8, 9, 10, 12),
            Planet.MERCURY to listOf(1, 3, 5, 7, 9, 12),
            Planet.JUPITER to listOf(1, 3, 4, 6, 8, 10, 12),
            Planet.VENUS to listOf(6, 7, 10),
            Planet.SATURN to listOf(1, 2, 4, 5, 7, 8, 9, 10, 12),
            Planet.RAHU to listOf(1, 2, 4, 5, 7, 8, 9, 12),
            Planet.KETU to listOf(1, 2, 4, 5, 7, 8, 9, 12)
        )

        private val VEDIC_DRISHTI = mapOf(
            Planet.SUN to mapOf(7 to 1.0),
            Planet.MOON to mapOf(7 to 1.0),
            Planet.MARS to mapOf(4 to 0.75, 7 to 1.0, 8 to 0.75),
            Planet.MERCURY to mapOf(7 to 1.0),
            Planet.VENUS to mapOf(7 to 1.0),
            Planet.JUPITER to mapOf(5 to 0.50, 7 to 1.0, 9 to 0.50),
            Planet.SATURN to mapOf(3 to 0.50, 7 to 1.0, 10 to 0.50),
            Planet.RAHU to mapOf(5 to 0.50, 7 to 1.0, 9 to 0.50),
            Planet.KETU to mapOf(5 to 0.50, 7 to 1.0, 9 to 0.50),
            Planet.ASCENDANT to emptyMap()
        )

        private val TRANSIT_ORBS = mapOf(
            Planet.SUN to 10.0,
            Planet.MOON to 12.0,
            Planet.MERCURY to 7.0,
            Planet.VENUS to 7.0,
            Planet.MARS to 8.0,
            Planet.JUPITER to 9.0,
            Planet.SATURN to 10.0,
            Planet.RAHU to 8.0,
            Planet.KETU to 8.0
        )

        private val NATURAL_BENEFICS = setOf(Planet.JUPITER, Planet.VENUS)
        private val NATURAL_MALEFICS = setOf(Planet.SATURN, Planet.MARS, Planet.RAHU, Planet.KETU, Planet.SUN)
        private val SLOW_PLANETS = setOf(Planet.SATURN, Planet.JUPITER, Planet.RAHU, Planet.KETU)

        private const val ASHTAMA_SHANI_INTENSITY = 0.85
        private const val SADE_SATI_PEAK_INTENSITY = 0.90
        private const val KANTAK_SHANI_INTENSITY = 0.75
    }

    data class TransitAnalysis(
        val natalChart: VedicChart,
        val transitDateTime: LocalDateTime,
        val transitPositions: List<PlanetPosition>,
        val gocharaResults: List<GocharaResult>,
        val transitAspects: List<TransitAspect>,
        val vedicDrishtiResults: List<VedicDrishtiResult>,
        val ashtakavargaScores: Map<Planet, AshtakavargaCalculator.TransitScore>,
        val sadeSatiStatus: SadeSatiStatus?,
        val specialSaturnTransit: SpecialSaturnTransit?,
        val overallAssessment: OverallTransitAssessment,
        val significantPeriods: List<SignificantPeriod>
    ) {
        fun toPlainText(): String = buildString {
            appendLine("═══════════════════════════════════════════════════════════")
            appendLine("              VEDIC TRANSIT ANALYSIS REPORT")
            appendLine("═══════════════════════════════════════════════════════════")
            appendLine()
            appendLine("Transit Date/Time: $transitDateTime")
            appendLine()

            appendLine("CURRENT PLANETARY POSITIONS (NIRAYANA)")
            appendLine("─────────────────────────────────────────────────────────")
            transitPositions.forEach { pos ->
                val retro = if (pos.isRetrograde) " ℞" else ""
                val nakshatra = getNakshatraName(pos.longitude)
                appendLine("${pos.planet.displayName.padEnd(10)}: ${pos.sign.displayName.padEnd(12)} ${formatDegree(pos.longitude)}$retro")
                appendLine("             Nakshatra: $nakshatra")
            }
            appendLine()

            sadeSatiStatus?.let { status ->
                appendLine("SADE SATI STATUS")
                appendLine("─────────────────────────────────────────────────────────")
                appendLine("Phase: ${status.phase.displayName}")
                appendLine("Intensity: ${String.format("%.0f", status.intensity * 100)}%")
                appendLine("Description: ${status.description}")
                appendLine()
            }

            specialSaturnTransit?.let { transit ->
                appendLine("SPECIAL SATURN TRANSIT")
                appendLine("─────────────────────────────────────────────────────────")
                appendLine("Type: ${transit.type.displayName}")
                appendLine("Effect: ${transit.interpretation}")
                appendLine()
            }

            appendLine("GOCHARA ANALYSIS (Transit from Moon)")
            appendLine("─────────────────────────────────────────────────────────")
            gocharaResults.forEach { result ->
                val vedhaStr = if (result.isVedhaAffected) " [VEDHA by ${result.vedhaSource?.displayName}]" else ""
                val retroStr = if (result.isRetrograde) " ℞" else ""
                appendLine("${result.planet.displayName.padEnd(10)}: House ${result.houseFromMoon.toString().padStart(2)} - ${result.effect.displayName}$vedhaStr$retroStr")
            }
            appendLine()

            appendLine("VEDIC DRISHTI (Planetary Aspects to Natal)")
            appendLine("─────────────────────────────────────────────────────────")
            if (vedicDrishtiResults.isEmpty()) {
                appendLine("No significant Vedic aspects currently active.")
            } else {
                vedicDrishtiResults.sortedByDescending { it.strength }.take(10).forEach { drishti ->
                    val aspectName = when(drishti.aspectHouse) {
                        7 -> "Full (7th)"
                        4, 8 -> "Mars Special (${drishti.aspectHouse}th)"
                        5, 9 -> "Jupiter Special (${drishti.aspectHouse}th)"
                        3, 10 -> "Saturn Special (${drishti.aspectHouse}th)"
                        else -> "${drishti.aspectHouse}th"
                    }
                    appendLine("Transit ${drishti.transitingPlanet.displayName} aspects Natal ${drishti.natalPlanet.displayName} ($aspectName)")
                    appendLine("  Strength: ${String.format("%.0f", drishti.strength * 100)}% | ${drishti.nature}")
                }
            }
            appendLine()

            if (transitAspects.isNotEmpty()) {
                appendLine("DEGREE-BASED ASPECTS (Conjunction/Opposition)")
                appendLine("─────────────────────────────────────────────────────────")
                transitAspects.sortedByDescending { it.strength }.take(8).forEach { aspect ->
                    val applying = if (aspect.isApplying) "Applying" else "Separating"
                    appendLine("Transit ${aspect.transitingPlanet.displayName} ${aspect.aspectType} Natal ${aspect.natalPlanet.displayName}")
                    appendLine("  Orb: ${String.format("%.2f", aspect.orb)}° ($applying) | Strength: ${String.format("%.0f", aspect.strength * 100)}%")
                }
                appendLine()
            }

            appendLine("ASHTAKAVARGA TRANSIT SCORES")
            appendLine("─────────────────────────────────────────────────────────")
            ashtakavargaScores.entries.sortedByDescending { it.value.binduScore }.forEach { (planet, score) ->
                val quality = when {
                    score.binduScore >= 5 -> "Excellent"
                    score.binduScore >= 4 -> "Good"
                    score.binduScore >= 3 -> "Average"
                    score.binduScore >= 2 -> "Below Average"
                    else -> "Weak"
                }
                appendLine("${planet.displayName.padEnd(10)}: BAV=${score.binduScore}/8, SAV=${score.savScore}/337 - $quality")
            }
            appendLine()

            appendLine("OVERALL ASSESSMENT")
            appendLine("─────────────────────────────────────────────────────────")
            appendLine("Period Quality: ${overallAssessment.quality.displayName}")
            appendLine("Composite Score: ${String.format("%.1f", overallAssessment.score)}/100")
            appendLine()
            appendLine("Summary: ${overallAssessment.summary}")
            appendLine()
            appendLine("Key Areas of Focus:")
            overallAssessment.focusAreas.forEachIndexed { index, area ->
                appendLine("${index + 1}. $area")
            }
            appendLine()

            if (significantPeriods.isNotEmpty()) {
                appendLine("UPCOMING SIGNIFICANT PERIODS (Next 30 Days)")
                appendLine("─────────────────────────────────────────────────────────")
                significantPeriods.forEach { period ->
                    val intensityStars = "★".repeat(period.intensity) + "☆".repeat(5 - period.intensity)
                    appendLine("${period.startDate.toLocalDate()} to ${period.endDate.toLocalDate()}")
                    appendLine("  $intensityStars ${period.description}")
                }
            }
        }

        private fun formatDegree(longitude: Double): String {
            val degInSign = longitude % 30.0
            val deg = degInSign.toInt()
            val minTotal = (degInSign - deg) * 60
            val min = minTotal.toInt()
            val sec = ((minTotal - min) * 60).toInt()
            return "${deg}° ${min}' ${sec}\""
        }

        private fun getNakshatraName(longitude: Double): String {
            val nakshatras = listOf(
                "Ashwini", "Bharani", "Krittika", "Rohini", "Mrigashira", "Ardra",
                "Punarvasu", "Pushya", "Ashlesha", "Magha", "Purva Phalguni", "Uttara Phalguni",
                "Hasta", "Chitra", "Swati", "Vishakha", "Anuradha", "Jyeshtha",
                "Mula", "Purva Ashadha", "Uttara Ashadha", "Shravana", "Dhanishtha", "Shatabhisha",
                "Purva Bhadrapada", "Uttara Bhadrapada", "Revati"
            )
            val nakshatraSpan = 360.0 / 27.0
            val index = (longitude / nakshatraSpan).toInt() % 27
            val pada = ((longitude % nakshatraSpan) / (nakshatraSpan / 4)).toInt() + 1
            return "${nakshatras[index]} Pada $pada"
        }
    }

    data class GocharaResult(
        val planet: Planet,
        val transitSign: ZodiacSign,
        val houseFromMoon: Int,
        val effect: TransitEffect,
        val baseEffect: TransitEffect,
        val isVedhaAffected: Boolean,
        val vedhaSource: Planet?,
        val isRetrograde: Boolean,
        val ashtakavargaBindu: Int,
        val interpretation: String
    )

    data class TransitAspect(
        val transitingPlanet: Planet,
        val natalPlanet: Planet,
        val aspectType: String,
        val exactAngle: Double,
        val orb: Double,
        val isApplying: Boolean,
        val strength: Double,
        val interpretation: String
    )

    data class VedicDrishtiResult(
        val transitingPlanet: Planet,
        val natalPlanet: Planet,
        val aspectHouse: Int,
        val strength: Double,
        val nature: String,
        val interpretation: String
    )

    data class SadeSatiStatus(
        val phase: SadeSatiPhase,
        val intensity: Double,
        val saturnSign: ZodiacSign,
        val moonSign: ZodiacSign,
        val yearsRemaining: Double,
        val description: String
    )

    enum class SadeSatiPhase(val displayName: String) {
        RISING("Rising Phase (12th from Moon)"),
        PEAK("Peak Phase (Over Moon)"),
        SETTING("Setting Phase (2nd from Moon)"),
        NOT_ACTIVE("Not Active")
    }

    data class SpecialSaturnTransit(
        val type: SpecialSaturnType,
        val houseFromMoon: Int,
        val houseFromLagna: Int,
        val intensity: Double,
        val interpretation: String
    )

    enum class SpecialSaturnType(val displayName: String) {
        ASHTAMA_SHANI("Ashtama Shani (8th from Moon)"),
        KANTAK_SHANI("Kantak Shani (4th from Moon)"),
        ARDHA_ASHTAMA("Ardha Ashtama (4th from Moon/Lagna)"),
        SATURN_RETURN("Saturn Return"),
        NONE("No Special Transit")
    }

    enum class TransitEffect(val displayName: String, val score: Double) {
        EXCELLENT("Excellent", 5.0),
        GOOD("Good", 4.0),
        MODERATE("Moderate", 3.5),
        NEUTRAL("Neutral", 3.0),
        CHALLENGING("Challenging", 2.0),
        DIFFICULT("Difficult", 1.0)
    }

    data class OverallTransitAssessment(
        val quality: TransitQuality,
        val score: Double,
        val summary: String,
        val focusAreas: List<String>,
        val recommendations: List<String>
    )

    enum class TransitQuality(val displayName: String) {
        EXCELLENT("Excellent Period"),
        GOOD("Good Period"),
        MODERATE("Moderate Period"),
        MIXED("Mixed Period"),
        CHALLENGING("Challenging Period"),
        DIFFICULT("Difficult Period")
    }

    data class SignificantPeriod(
        val startDate: LocalDateTime,
        val endDate: LocalDateTime,
        val description: String,
        val planets: List<Planet>,
        val intensity: Int,
        val eventType: TransitEventType
    )

    enum class TransitEventType {
        SADE_SATI_PHASE_CHANGE,
        SATURN_SPECIAL_TRANSIT,
        JUPITER_SIGN_CHANGE,
        RAHU_KETU_AXIS_SHIFT,
        RETROGRADE_STATION,
        DIRECT_STATION,
        PLANETARY_WAR,
        ECLIPSE_PERIOD,
        GENERAL_TRANSIT
    }

    fun getCurrentTransitPositions(timezone: String = "UTC"): List<PlanetPosition> {
        val now = LocalDateTime.now(ZoneId.of(timezone))
        return getTransitPositionsForDateTime(now, timezone)
    }

    fun getTransitPositionsForDateTime(
        dateTime: LocalDateTime,
        timezone: String = "UTC"
    ): List<PlanetPosition> {
        val transitBirthData = BirthData(
            name = "Transit",
            dateTime = dateTime,
            latitude = 0.0,
            longitude = 0.0,
            timezone = timezone,
            location = "Transit Chart"
        )
        return ephemerisEngine.calculateVedicChart(transitBirthData, HouseSystem.DEFAULT).planetPositions
    }

    fun analyzeTransits(
        natalChart: VedicChart,
        transitDateTime: LocalDateTime = LocalDateTime.now()
    ): TransitAnalysis {
        val transitPositions = getTransitPositionsForDateTime(
            transitDateTime,
            natalChart.birthData.timezone
        )

        val natalMoon = natalChart.planetPositions.find { it.planet == Planet.MOON }
            ?: throw IllegalStateException("Natal Moon position not found")

        val natalLagna = natalChart.planetPositions.find { it.planet == Planet.ASCENDANT }

        val ashtakavargaAnalysis = AshtakavargaCalculator.calculateAshtakavarga(natalChart)
        val ashtakavargaScores = mutableMapOf<Planet, AshtakavargaCalculator.TransitScore>()

        transitPositions.filter { it.planet in Planet.MAIN_PLANETS && it.planet != Planet.RAHU && it.planet != Planet.KETU }
            .forEach { transitPos ->
                ashtakavargaScores[transitPos.planet] = ashtakavargaAnalysis.getTransitScore(
                    transitPos.planet,
                    transitPos.sign
                )
            }

        val gocharaResults = calculateGochara(natalMoon, transitPositions, ashtakavargaScores)
        val transitAspects = calculateTransitAspects(natalChart, transitPositions)
        val vedicDrishtiResults = calculateVedicDrishti(natalChart, transitPositions)
        val sadeSatiStatus = calculateSadeSatiStatus(natalMoon, transitPositions)
        val specialSaturnTransit = calculateSpecialSaturnTransit(natalMoon, natalLagna, transitPositions)
        val overallAssessment = calculateOverallAssessment(
            gocharaResults, transitAspects, vedicDrishtiResults,
            ashtakavargaScores, sadeSatiStatus, specialSaturnTransit
        )
        val significantPeriods = findSignificantPeriods(natalChart, transitDateTime)

        return TransitAnalysis(
            natalChart = natalChart,
            transitDateTime = transitDateTime,
            transitPositions = transitPositions,
            gocharaResults = gocharaResults,
            transitAspects = transitAspects,
            vedicDrishtiResults = vedicDrishtiResults,
            ashtakavargaScores = ashtakavargaScores,
            sadeSatiStatus = sadeSatiStatus,
            specialSaturnTransit = specialSaturnTransit,
            overallAssessment = overallAssessment,
            significantPeriods = significantPeriods
        )
    }

    private fun calculateGochara(
        natalMoon: PlanetPosition,
        transitPositions: List<PlanetPosition>,
        ashtakavargaScores: Map<Planet, AshtakavargaCalculator.TransitScore>
    ): List<GocharaResult> {
        val results = mutableListOf<GocharaResult>()
        val natalMoonSign = natalMoon.sign

        transitPositions.forEach { transitPos ->
            val planet = transitPos.planet
            if (planet !in Planet.MAIN_PLANETS) return@forEach

            val houseFromMoon = calculateHouseFromSign(transitPos.sign, natalMoonSign)
            val favorableHouses = FAVORABLE_TRANSITS[planet] ?: emptyList()
            val difficultHouses = DIFFICULT_TRANSITS[planet] ?: emptyList()

            val baseEffect = when {
                houseFromMoon in favorableHouses -> TransitEffect.GOOD
                houseFromMoon in difficultHouses -> TransitEffect.CHALLENGING
                else -> TransitEffect.NEUTRAL
            }

            val (isVedhaAffected, vedhaSource) = checkVedha(
                planet, houseFromMoon, transitPositions, natalMoonSign
            )

            val ashtakavargaBindu = ashtakavargaScores[planet]?.binduScore ?: 4

            var finalEffect = baseEffect

            if (isVedhaAffected && baseEffect == TransitEffect.GOOD) {
                finalEffect = TransitEffect.NEUTRAL
            }

            if (transitPos.isRetrograde) {
                finalEffect = when (finalEffect) {
                    TransitEffect.GOOD -> TransitEffect.MODERATE
                    TransitEffect.CHALLENGING -> TransitEffect.DIFFICULT
                    else -> finalEffect
                }
            }

            finalEffect = adjustEffectByAshtakavarga(finalEffect, ashtakavargaBindu)

            val interpretation = generateGocharaInterpretation(
                planet, houseFromMoon, finalEffect, isVedhaAffected,
                transitPos.isRetrograde, ashtakavargaBindu
            )

            results.add(
                GocharaResult(
                    planet = planet,
                    transitSign = transitPos.sign,
                    houseFromMoon = houseFromMoon,
                    effect = finalEffect,
                    baseEffect = baseEffect,
                    isVedhaAffected = isVedhaAffected,
                    vedhaSource = vedhaSource,
                    isRetrograde = transitPos.isRetrograde,
                    ashtakavargaBindu = ashtakavargaBindu,
                    interpretation = interpretation
                )
            )
        }

        return results
    }

    private fun adjustEffectByAshtakavarga(effect: TransitEffect, bindu: Int): TransitEffect {
        return when {
            bindu >= 5 && effect == TransitEffect.CHALLENGING -> TransitEffect.NEUTRAL
            bindu >= 6 && effect == TransitEffect.NEUTRAL -> TransitEffect.MODERATE
            bindu >= 6 && effect == TransitEffect.GOOD -> TransitEffect.EXCELLENT
            bindu <= 2 && effect == TransitEffect.GOOD -> TransitEffect.MODERATE
            bindu <= 2 && effect == TransitEffect.NEUTRAL -> TransitEffect.CHALLENGING
            bindu <= 1 && effect == TransitEffect.CHALLENGING -> TransitEffect.DIFFICULT
            else -> effect
        }
    }

    private fun checkVedha(
        planet: Planet,
        houseFromMoon: Int,
        transitPositions: List<PlanetPosition>,
        natalMoonSign: ZodiacSign
    ): Pair<Boolean, Planet?> {
        val favorableHouses = FAVORABLE_TRANSITS[planet] ?: emptyList()
        if (houseFromMoon !in favorableHouses) {
            return Pair(false, null)
        }

        val vedhaMap = when (planet) {
            Planet.SUN -> SUN_VEDHA
            Planet.MOON -> MOON_VEDHA
            Planet.MARS -> MARS_VEDHA
            Planet.MERCURY -> MERCURY_VEDHA
            Planet.JUPITER -> JUPITER_VEDHA
            Planet.VENUS -> VENUS_VEDHA
            Planet.SATURN -> SATURN_VEDHA
            Planet.RAHU, Planet.KETU -> RAHU_KETU_VEDHA
            else -> emptyMap()
        }

        val vedhaHouse = vedhaMap[houseFromMoon] ?: return Pair(false, null)

        transitPositions.forEach { otherTransit ->
            if (otherTransit.planet != planet && 
                otherTransit.planet in Planet.MAIN_PLANETS &&
                otherTransit.planet != Planet.MOON) {
                val otherHouseFromMoon = calculateHouseFromSign(otherTransit.sign, natalMoonSign)
                if (otherHouseFromMoon == vedhaHouse) {
                    return Pair(true, otherTransit.planet)
                }
            }
        }

        return Pair(false, null)
    }

    private fun calculateVedicDrishti(
        natalChart: VedicChart,
        transitPositions: List<PlanetPosition>
    ): List<VedicDrishtiResult> {
        val results = mutableListOf<VedicDrishtiResult>()

        transitPositions.forEach { transitPos ->
            val transitPlanet = transitPos.planet
            if (transitPlanet !in Planet.MAIN_PLANETS) return@forEach

            val planetAspects = VEDIC_DRISHTI[transitPlanet] ?: mapOf(7 to 1.0)

            natalChart.planetPositions.forEach { natalPos ->
                if (natalPos.planet !in Planet.MAIN_PLANETS) return@forEach

                val houseDistance = calculateHouseFromSign(natalPos.sign, transitPos.sign)

                planetAspects[houseDistance]?.let { aspectStrength ->
                    val orbFactor = calculateOrbFactor(transitPos.longitude, natalPos.longitude, houseDistance)
                    val finalStrength = aspectStrength * orbFactor

                    if (finalStrength >= 0.25) {
                        val nature = determineAspectNature(transitPlanet, natalPos.planet, houseDistance)
                        val interpretation = generateDrishtiInterpretation(
                            transitPlanet, natalPos.planet, houseDistance, nature
                        )

                        results.add(
                            VedicDrishtiResult(
                                transitingPlanet = transitPlanet,
                                natalPlanet = natalPos.planet,
                                aspectHouse = houseDistance,
                                strength = finalStrength,
                                nature = nature,
                                interpretation = interpretation
                            )
                        )
                    }
                }
            }
        }

        return results.sortedByDescending { it.strength }
    }

    private fun calculateOrbFactor(transitLong: Double, natalLong: Double, expectedHouse: Int): Double {
        val expectedAngle = ((expectedHouse - 1) * 30.0)
        val actualDiff = normalizeAngle(natalLong - transitLong)
        val orb = abs(actualDiff - expectedAngle)
        val adjustedOrb = minOf(orb, 30.0 - orb, orb)

        return when {
            adjustedOrb <= 5.0 -> 1.0
            adjustedOrb <= 10.0 -> 0.85
            adjustedOrb <= 15.0 -> 0.65
            adjustedOrb <= 20.0 -> 0.45
            adjustedOrb <= 25.0 -> 0.25
            else -> 0.0
        }
    }

    private fun determineAspectNature(transitPlanet: Planet, natalPlanet: Planet, aspectHouse: Int): String {
        val transitBenefic = transitPlanet in NATURAL_BENEFICS
        val natalBenefic = natalPlanet in NATURAL_BENEFICS

        return when {
            transitBenefic && natalBenefic -> "Highly Benefic"
            transitBenefic && !natalBenefic -> "Moderating"
            !transitBenefic && natalBenefic -> "Challenging to Benefic"
            transitPlanet == Planet.SATURN && natalPlanet == Planet.MOON -> "Emotionally Challenging"
            transitPlanet == Planet.MARS && natalPlanet == Planet.MARS -> "Intensifying Energy"
            transitPlanet == Planet.SATURN && natalPlanet == Planet.SUN -> "Authority Challenges"
            transitPlanet == Planet.JUPITER && aspectHouse in listOf(5, 9) -> "Expansive Blessing"
            else -> "Mixed Influence"
        }
    }

    private fun calculateTransitAspects(
        natalChart: VedicChart,
        transitPositions: List<PlanetPosition>
    ): List<TransitAspect> {
        val aspects = mutableListOf<TransitAspect>()

        transitPositions.forEach { transitPos ->
            if (transitPos.planet !in Planet.MAIN_PLANETS) return@forEach

            natalChart.planetPositions.forEach { natalPos ->
                if (natalPos.planet !in Planet.MAIN_PLANETS) return@forEach

                val angularSeparation = calculateAngularSeparation(
                    transitPos.longitude,
                    natalPos.longitude
                )

                val maxOrb = TRANSIT_ORBS[transitPos.planet] ?: 8.0
                val conjunctionOrb = if (angularSeparation <= maxOrb) angularSeparation else null
                val oppositionOrb = if (abs(angularSeparation - 180.0) <= maxOrb) abs(angularSeparation - 180.0) else null

                conjunctionOrb?.let { orb ->
                    val strength = calculateAspectStrength(orb, maxOrb, transitPos.planet)
                    val isApplying = isAspectApplying(transitPos, natalPos, 0.0)

                    aspects.add(
                        TransitAspect(
                            transitingPlanet = transitPos.planet,
                            natalPlanet = natalPos.planet,
                            aspectType = "Conjunction",
                            exactAngle = angularSeparation,
                            orb = orb,
                            isApplying = isApplying,
                            strength = strength,
                            interpretation = generateAspectInterpretation(
                                transitPos.planet, natalPos.planet, "Conjunction", isApplying
                            )
                        )
                    )
                }

                oppositionOrb?.let { orb ->
                    val strength = calculateAspectStrength(orb, maxOrb, transitPos.planet)
                    val isApplying = isAspectApplying(transitPos, natalPos, 180.0)

                    aspects.add(
                        TransitAspect(
                            transitingPlanet = transitPos.planet,
                            natalPlanet = natalPos.planet,
                            aspectType = "Opposition",
                            exactAngle = angularSeparation,
                            orb = orb,
                            isApplying = isApplying,
                            strength = strength,
                            interpretation = generateAspectInterpretation(
                                transitPos.planet, natalPos.planet, "Opposition", isApplying
                            )
                        )
                    )
                }
            }
        }

        return aspects.sortedByDescending { it.strength }
    }

    private fun calculateAspectStrength(orb: Double, maxOrb: Double, planet: Planet): Double {
        val baseStrength = 1.0 - (orb / maxOrb)
        val planetWeight = when (planet) {
            in SLOW_PLANETS -> 1.2
            else -> 1.0
        }
        return (baseStrength * planetWeight).coerceIn(0.0, 1.0)
    }

    private fun calculateSadeSatiStatus(
        natalMoon: PlanetPosition,
        transitPositions: List<PlanetPosition>
    ): SadeSatiStatus? {
        val saturnTransit = transitPositions.find { it.planet == Planet.SATURN } ?: return null
        val moonSign = natalMoon.sign
        val saturnSign = saturnTransit.sign

        val houseFromMoon = calculateHouseFromSign(saturnSign, moonSign)

        val phase = when (houseFromMoon) {
            12 -> SadeSatiPhase.RISING
            1 -> SadeSatiPhase.PEAK
            2 -> SadeSatiPhase.SETTING
            else -> SadeSatiPhase.NOT_ACTIVE
        }

        if (phase == SadeSatiPhase.NOT_ACTIVE) return null

        val saturnDegreeInSign = saturnTransit.longitude % 30.0
        val moonDegreeInSign = natalMoon.longitude % 30.0

        val intensity = when (phase) {
            SadeSatiPhase.RISING -> {
                val progress = saturnDegreeInSign / 30.0
                0.5 + (progress * 0.3)
            }
            SadeSatiPhase.PEAK -> {
                val distanceFromMoon = abs(saturnDegreeInSign - moonDegreeInSign)
                if (distanceFromMoon <= 5.0) SADE_SATI_PEAK_INTENSITY
                else 0.75 + (0.15 * (1 - distanceFromMoon / 30.0))
            }
            SadeSatiPhase.SETTING -> {
                val progress = saturnDegreeInSign / 30.0
                0.7 - (progress * 0.3)
            }
            else -> 0.0
        }

        val yearsRemaining = estimateSadeSatiYearsRemaining(phase, saturnDegreeInSign, saturnTransit.isRetrograde)

        val description = generateSadeSatiDescription(phase, intensity, yearsRemaining)

        return SadeSatiStatus(
            phase = phase,
            intensity = intensity,
            saturnSign = saturnSign,
            moonSign = moonSign,
            yearsRemaining = yearsRemaining,
            description = description
        )
    }

    private fun estimateSadeSatiYearsRemaining(phase: SadeSatiPhase, degreeInSign: Double, isRetrograde: Boolean): Double {
        val degreesRemaining = when (phase) {
            SadeSatiPhase.RISING -> (30.0 - degreeInSign) + 60.0
            SadeSatiPhase.PEAK -> (30.0 - degreeInSign) + 30.0
            SadeSatiPhase.SETTING -> 30.0 - degreeInSign
            else -> 0.0
        }
        val saturnSpeedPerYear = if (isRetrograde) 8.0 else 12.0
        return degreesRemaining / saturnSpeedPerYear
    }

    private fun generateSadeSatiDescription(phase: SadeSatiPhase, intensity: Double, yearsRemaining: Double): String {
        val intensityDesc = when {
            intensity >= 0.85 -> "very intense"
            intensity >= 0.70 -> "significant"
            intensity >= 0.55 -> "moderate"
            else -> "mild"
        }

        return when (phase) {
            SadeSatiPhase.RISING -> "Sade Sati has begun. Saturn is in 12th from Moon, bringing a $intensityDesc period of introspection and potential challenges. Approximately ${String.format("%.1f", yearsRemaining)} years remaining."
            SadeSatiPhase.PEAK -> "Sade Sati peak phase. Saturn is over natal Moon, creating $intensityDesc pressure on mind, emotions, and health. About ${String.format("%.1f", yearsRemaining)} years until completion."
            SadeSatiPhase.SETTING -> "Sade Sati final phase. Saturn in 2nd from Moon affects finances and family. The $intensityDesc period will conclude in approximately ${String.format("%.1f", yearsRemaining)} years."
            else -> ""
        }
    }

    private fun calculateSpecialSaturnTransit(
        natalMoon: PlanetPosition,
        natalLagna: PlanetPosition?,
        transitPositions: List<PlanetPosition>
    ): SpecialSaturnTransit? {
        val saturnTransit = transitPositions.find { it.planet == Planet.SATURN } ?: return null
        val natalSaturn = transitPositions.find { it.planet == Planet.SATURN }

        val houseFromMoon = calculateHouseFromSign(saturnTransit.sign, natalMoon.sign)
        val houseFromLagna = natalLagna?.let { calculateHouseFromSign(saturnTransit.sign, it.sign) } ?: 0

        val type = when {
            houseFromMoon == 8 -> SpecialSaturnType.ASHTAMA_SHANI
            houseFromMoon == 4 || houseFromLagna == 4 -> SpecialSaturnType.KANTAK_SHANI
            houseFromMoon == 4 && houseFromLagna != 4 -> SpecialSaturnType.ARDHA_ASHTAMA
            natalSaturn != null && saturnTransit.sign == natalSaturn.sign -> SpecialSaturnType.SATURN_RETURN
            else -> SpecialSaturnType.NONE
        }

        if (type == SpecialSaturnType.NONE) return null

        val intensity = when (type) {
            SpecialSaturnType.ASHTAMA_SHANI -> ASHTAMA_SHANI_INTENSITY
            SpecialSaturnType.KANTAK_SHANI -> KANTAK_SHANI_INTENSITY
            SpecialSaturnType.ARDHA_ASHTAMA -> 0.65
            SpecialSaturnType.SATURN_RETURN -> 0.70
            else -> 0.0
        }

        val interpretation = when (type) {
            SpecialSaturnType.ASHTAMA_SHANI -> "Ashtama Shani is active. Saturn in 8th from Moon brings challenges related to health, longevity, and sudden obstacles. Focus on health, avoid risky ventures, and maintain patience."
            SpecialSaturnType.KANTAK_SHANI -> "Kantak Shani (thorn Saturn) is active. Saturn in 4th brings challenges to domestic happiness, mother's health, property matters, and mental peace. Practice meditation and patience."
            SpecialSaturnType.ARDHA_ASHTAMA -> "Ardha Ashtama (half-eighth) Saturn transit brings moderate challenges. Exercise caution in decisions and maintain steady progress."
            SpecialSaturnType.SATURN_RETURN -> "Saturn Return is occurring. This 29-year cycle brings major life restructuring, karmic lessons, and the need to take greater responsibility."
            else -> ""
        }

        return SpecialSaturnTransit(
            type = type,
            houseFromMoon = houseFromMoon,
            houseFromLagna = houseFromLagna,
            intensity = intensity,
            interpretation = interpretation
        )
    }

    private fun calculateOverallAssessment(
        gocharaResults: List<GocharaResult>,
        transitAspects: List<TransitAspect>,
        vedicDrishtiResults: List<VedicDrishtiResult>,
        ashtakavargaScores: Map<Planet, AshtakavargaCalculator.TransitScore>,
        sadeSatiStatus: SadeSatiStatus?,
        specialSaturnTransit: SpecialSaturnTransit?
    ): OverallTransitAssessment {
        var totalScore = 0.0
        var weightSum = 0.0

        val slowPlanetWeight = 2.0
        val fastPlanetWeight = 1.0

        gocharaResults.forEach { result ->
            val weight = if (result.planet in SLOW_PLANETS) slowPlanetWeight else fastPlanetWeight
            totalScore += result.effect.score * weight
            weightSum += weight
        }

        val gocharaScore = if (weightSum > 0) (totalScore / weightSum) * 20.0 else 50.0

        val beneficDrishti = vedicDrishtiResults.count { it.nature.contains("Benefic") }
        val maleficDrishti = vedicDrishtiResults.count { it.nature.contains("Challenging") || it.nature.contains("Intensifying") }
        val drishtiScore = ((beneficDrishti * 8 - maleficDrishti * 5) + 50).coerceIn(0, 100).toDouble()

        val ashtakavargaScore = if (ashtakavargaScores.isNotEmpty()) {
            val avgBindu = ashtakavargaScores.values.map { it.binduScore }.average()
            (avgBindu / 8.0) * 100.0
        } else 50.0

        var combinedScore = (gocharaScore * 0.35 + drishtiScore * 0.25 + ashtakavargaScore * 0.40)

        sadeSatiStatus?.let {
            val penalty = when (it.phase) {
                SadeSatiPhase.PEAK -> 15.0 * it.intensity
                SadeSatiPhase.RISING, SadeSatiPhase.SETTING -> 8.0 * it.intensity
                else -> 0.0
            }
            combinedScore -= penalty
        }

        specialSaturnTransit?.let {
            val penalty = when (it.type) {
                SpecialSaturnType.ASHTAMA_SHANI -> 12.0 * it.intensity
                SpecialSaturnType.KANTAK_SHANI -> 8.0 * it.intensity
                else -> 0.0
            }
            combinedScore -= penalty
        }

        combinedScore = combinedScore.coerceIn(0.0, 100.0)

        val quality = when {
            combinedScore >= 80 -> TransitQuality.EXCELLENT
            combinedScore >= 65 -> TransitQuality.GOOD
            combinedScore >= 55 -> TransitQuality.MODERATE
            combinedScore >= 45 -> TransitQuality.MIXED
            combinedScore >= 30 -> TransitQuality.CHALLENGING
            else -> TransitQuality.DIFFICULT
        }

        val summary = generateOverallSummary(quality, gocharaResults, sadeSatiStatus, specialSaturnTransit)
        val focusAreas = generateFocusAreas(gocharaResults, vedicDrishtiResults, sadeSatiStatus, specialSaturnTransit)
        val recommendations = generateRecommendations(quality, gocharaResults, sadeSatiStatus)

        return OverallTransitAssessment(
            quality = quality,
            score = combinedScore,
            summary = summary,
            focusAreas = focusAreas,
            recommendations = recommendations
        )
    }

    private fun findSignificantPeriods(
        natalChart: VedicChart,
        startDate: LocalDateTime
    ): List<SignificantPeriod> {
        val periods = mutableListOf<SignificantPeriod>()
        val natalMoon = natalChart.planetPositions.find { it.planet == Planet.MOON } ?: return periods

        var previousPositions: Map<Planet, ZodiacSign>? = null

        for (dayOffset in 0..30 step 1) {
            val checkDate = startDate.plusDays(dayOffset.toLong())
            val transitPositions = getTransitPositionsForDateTime(checkDate, natalChart.birthData.timezone)

            val currentPositions = transitPositions
                .filter { it.planet in SLOW_PLANETS }
                .associate { it.planet to it.sign }

            if (previousPositions != null) {
                SLOW_PLANETS.forEach { planet ->
                    val currentSign = currentPositions[planet]
                    val previousSign = previousPositions!![planet]

                    if (currentSign != null && previousSign != null && currentSign != previousSign) {
                        val houseFromMoon = calculateHouseFromSign(currentSign, natalMoon.sign)
                        val description = "${planet.displayName} enters ${currentSign.displayName} (${houseFromMoon}th from Moon)"
                        val intensity = when (planet) {
                            Planet.SATURN -> if (houseFromMoon in listOf(1, 4, 8, 12)) 5 else 4
                            Planet.JUPITER -> if (houseFromMoon in listOf(1, 5, 9)) 4 else 3
                            Planet.RAHU, Planet.KETU -> 4
                            else -> 3
                        }

                        periods.add(
                            SignificantPeriod(
                                startDate = checkDate,
                                endDate = checkDate.plusDays(30),
                                description = description,
                                planets = listOf(planet),
                                intensity = intensity,
                                eventType = when (planet) {
                                    Planet.SATURN -> TransitEventType.SATURN_SPECIAL_TRANSIT
                                    Planet.JUPITER -> TransitEventType.JUPITER_SIGN_CHANGE
                                    Planet.RAHU, Planet.KETU -> TransitEventType.RAHU_KETU_AXIS_SHIFT
                                    else -> TransitEventType.GENERAL_TRANSIT
                                }
                            )
                        )
                    }
                }

                transitPositions.forEach { pos ->
                    if (pos.planet in SLOW_PLANETS) {
                        val previousPos = getTransitPositionsForDateTime(
                            checkDate.minusDays(1), natalChart.birthData.timezone
                        ).find { it.planet == pos.planet }

                        if (previousPos != null) {
                            val wasRetro = previousPos.isRetrograde
                            val isRetro = pos.isRetrograde

                            if (wasRetro != isRetro) {
                                val eventType = if (isRetro) TransitEventType.RETROGRADE_STATION else TransitEventType.DIRECT_STATION
                                val stationType = if (isRetro) "Retrograde" else "Direct"

                                periods.add(
                                    SignificantPeriod(
                                        startDate = checkDate,
                                        endDate = checkDate.plusDays(7),
                                        description = "${pos.planet.displayName} stations $stationType in ${pos.sign.displayName}",
                                        planets = listOf(pos.planet),
                                        intensity = 4,
                                        eventType = eventType
                                    )
                                )
                            }
                        }
                    }
                }
            }

            previousPositions = currentPositions
        }

        return periods.distinctBy { "${it.planets.firstOrNull()?.name}_${it.eventType}_${it.startDate.toLocalDate()}" }
    }

    private fun calculateHouseFromSign(targetSign: ZodiacSign, referenceSign: ZodiacSign): Int {
        val diff = targetSign.number - referenceSign.number
        return if (diff >= 0) diff + 1 else diff + 13
    }

    private fun calculateAngularSeparation(long1: Double, long2: Double): Double {
        val diff = abs(normalizeAngle(long1) - normalizeAngle(long2))
        return if (diff > 180.0) 360.0 - diff else diff
    }

    private fun normalizeAngle(angle: Double): Double {
        var normalized = angle % 360.0
        if (normalized < 0) normalized += 360.0
        return normalized
    }

    private fun isAspectApplying(
        transitPos: PlanetPosition,
        natalPos: PlanetPosition,
        aspectAngle: Double
    ): Boolean {
        if (transitPos.speed == 0.0) return false

        val currentSeparation = calculateAngularSeparation(transitPos.longitude, natalPos.longitude)
        val currentOrb = abs(currentSeparation - aspectAngle)

        val futureLong = normalizeAngle(transitPos.longitude + transitPos.speed)
        val futureSeparation = calculateAngularSeparation(futureLong, natalPos.longitude)
        val futureOrb = abs(futureSeparation - aspectAngle)

        return futureOrb < currentOrb
    }

    private fun generateGocharaInterpretation(
        planet: Planet,
        houseFromMoon: Int,
        effect: TransitEffect,
        isVedhaAffected: Boolean,
        isRetrograde: Boolean,
        ashtakavargaBindu: Int
    ): String {
        val houseMatters = when (houseFromMoon) {
            1 -> "self, health, vitality, personality"
            2 -> "wealth, family, speech, food habits"
            3 -> "courage, siblings, communication, short travels"
            4 -> "home, mother, mental peace, vehicles, property"
            5 -> "children, creativity, romance, intelligence, past merit"
            6 -> "enemies, competition, health issues, debts, service"
            7 -> "marriage, partnerships, business, public dealings"
            8 -> "obstacles, longevity, occult, sudden events, transformation"
            9 -> "fortune, father, dharma, higher learning, long journeys"
            10 -> "career, status, karma, government, authority"
            11 -> "gains, income, friends, elder siblings, fulfillment of desires"
            12 -> "expenses, losses, spirituality, foreign lands, liberation"
            else -> "general matters"
        }

        val baseInterpretation = when (effect) {
            TransitEffect.EXCELLENT -> "${planet.displayName} transit in ${houseFromMoon}th house brings excellent results for $houseMatters."
            TransitEffect.GOOD -> "${planet.displayName} transit in ${houseFromMoon}th house supports progress in $houseMatters."
            TransitEffect.MODERATE -> "${planet.displayName} transit in ${houseFromMoon}th house brings moderate support for $houseMatters."
            TransitEffect.NEUTRAL -> "${planet.displayName} transit in ${houseFromMoon}th house has mixed effects on $houseMatters."
            TransitEffect.CHALLENGING -> "${planet.displayName} transit in ${houseFromMoon}th house may create challenges in $houseMatters."
            TransitEffect.DIFFICULT -> "${planet.displayName} transit in ${houseFromMoon}th house requires caution regarding $houseMatters."
        }

        val modifiers = mutableListOf<String>()

        if (isVedhaAffected) {
            modifiers.add("Vedha reduces favorable effects")
        }

        if (isRetrograde) {
            modifiers.add("Retrograde motion intensifies karmic lessons")
        }

        if (ashtakavargaBindu >= 5) {
            modifiers.add("Strong Ashtakavarga score ($ashtakavargaBindu/8) enhances results")
        } else if (ashtakavargaBindu <= 2) {
            modifiers.add("Weak Ashtakavarga score ($ashtakavargaBindu/8) diminishes results")
        }

        return if (modifiers.isEmpty()) {
            baseInterpretation
        } else {
            "$baseInterpretation ${modifiers.joinToString(". ")}."
        }
    }

    private fun generateDrishtiInterpretation(
        transitingPlanet: Planet,
        natalPlanet: Planet,
        aspectHouse: Int,
        nature: String
    ): String {
        val aspectType = when (aspectHouse) {
            7 -> "full"
            4, 8 -> "special Mars"
            5, 9 -> "special Jupiter"
            3, 10 -> "special Saturn"
            else -> "$aspectHouse" + "th house"
        }

        return "Transit ${transitingPlanet.displayName} casts $aspectType aspect on natal ${natalPlanet.displayName}. Nature: $nature."
    }

    private fun generateAspectInterpretation(
        transitingPlanet: Planet,
        natalPlanet: Planet,
        aspectType: String,
        isApplying: Boolean
    ): String {
        val timing = if (isApplying) "approaching exact" else "separating from exact"
        val beneficTransit = transitingPlanet in NATURAL_BENEFICS

        val effectDescription = when {
            aspectType == "Conjunction" && beneficTransit -> "beneficial influence and support"
            aspectType == "Conjunction" && !beneficTransit -> "intense focus and potential challenges"
            aspectType == "Opposition" && beneficTransit -> "awareness and balancing energies"
            aspectType == "Opposition" && !beneficTransit -> "tension requiring conscious management"
            else -> "dynamic interaction"
        }

        return "Transit ${transitingPlanet.displayName} $aspectType natal ${natalPlanet.displayName} ($timing) - $effectDescription."
    }

    private fun generateOverallSummary(
        quality: TransitQuality,
        gocharaResults: List<GocharaResult>,
        sadeSatiStatus: SadeSatiStatus?,
        specialSaturnTransit: SpecialSaturnTransit?
    ): String {
        val favorablePlanets = gocharaResults
            .filter { it.effect in listOf(TransitEffect.EXCELLENT, TransitEffect.GOOD) }
            .map { it.planet.displayName }

        val challengingPlanets = gocharaResults
            .filter { it.effect in listOf(TransitEffect.CHALLENGING, TransitEffect.DIFFICULT) }
            .map { it.planet.displayName }

        val baseSummary = when (quality) {
            TransitQuality.EXCELLENT -> "This is an excellent transit period. ${favorablePlanets.joinToString(", ")} provide strong support for growth, success, and positive developments."
            TransitQuality.GOOD -> "Favorable transit period overall. ${favorablePlanets.joinToString(", ")} create opportunities. Good time for important initiatives and progress."
            TransitQuality.MODERATE -> "Moderately favorable period. Some planetary support available from ${favorablePlanets.joinToString(", ")}. Proceed with awareness."
            TransitQuality.MIXED -> "Mixed influences present. Balance the support of ${favorablePlanets.joinToString(", ")} against challenges from ${challengingPlanets.joinToString(", ")}."
            TransitQuality.CHALLENGING -> "Challenging period requiring patience and perseverance. ${challengingPlanets.joinToString(", ")} create obstacles. Focus on steady, cautious progress."
            TransitQuality.DIFFICULT -> "Difficult period requiring significant caution. ${challengingPlanets.joinToString(", ")} create substantial challenges. Avoid major decisions and focus on remedial measures."
        }

        val additionalContext = mutableListOf<String>()

        sadeSatiStatus?.let {
            additionalContext.add("Sade Sati ${it.phase.displayName} is active.")
        }

        specialSaturnTransit?.let {
            if (it.type != SpecialSaturnType.NONE) {
                additionalContext.add("${it.type.displayName} adds karmic weight to this period.")
            }
        }

        return if (additionalContext.isEmpty()) {
            baseSummary
        } else {
            "$baseSummary ${additionalContext.joinToString(" ")}"
        }
    }

    private fun generateFocusAreas(
        gocharaResults: List<GocharaResult>,
        vedicDrishtiResults: List<VedicDrishtiResult>,
        sadeSatiStatus: SadeSatiStatus?,
        specialSaturnTransit: SpecialSaturnTransit?
    ): List<String> {
        val areas = mutableListOf<String>()

        sadeSatiStatus?.let {
            when (it.phase) {
                SadeSatiPhase.RISING -> areas.add("Sade Sati beginning - focus on mental peace, health maintenance, and spiritual practices")
                SadeSatiPhase.PEAK -> areas.add("Sade Sati peak - prioritize emotional stability, mother's health, and patience in all matters")
                SadeSatiPhase.SETTING -> areas.add("Sade Sati ending - attention to finances, family harmony, and consolidating lessons learned")
                else -> {}
            }
        }

        specialSaturnTransit?.let {
            when (it.type) {
                SpecialSaturnType.ASHTAMA_SHANI -> areas.add("Ashtama Shani active - focus on health, avoid risky ventures, strengthen immunity")
                SpecialSaturnType.KANTAK_SHANI -> areas.add("Kantak Shani active - attention to home affairs, property matters, and mental peace")
                SpecialSaturnType.SATURN_RETURN -> areas.add("Saturn Return - major life restructuring, taking greater responsibilities, karmic maturation")
                else -> {}
            }
        }

        gocharaResults.find { it.planet == Planet.JUPITER }?.let { jupiterResult ->
            when (jupiterResult.houseFromMoon) {
                1 -> areas.add("Jupiter over Moon - excellent for personal growth, wisdom, and new beginnings")
                5 -> areas.add("Jupiter in 5th - favorable for creativity, children matters, education, and romance")
                9 -> areas.add("Jupiter in 9th - highly auspicious for fortune, dharma, higher learning, and spiritual growth")
                7 -> areas.add("Jupiter in 7th - supportive for relationships, marriage, and partnerships")
                11 -> areas.add("Jupiter in 11th - excellent for gains, income growth, and fulfillment of desires")
            }
        }

        gocharaResults.find { it.planet == Planet.RAHU }?.let { rahuResult ->
            if (rahuResult.houseFromMoon in listOf(1, 7)) {
                areas.add("Rahu on Moon axis - intense period for desires, worldly ambitions, and potential confusion")
            }
        }

        vedicDrishtiResults
            .filter { it.strength >= 0.75 }
            .take(2)
            .forEach { drishti ->
                areas.add("Strong ${drishti.transitingPlanet.displayName} aspect on natal ${drishti.natalPlanet.displayName} - ${drishti.nature.lowercase()}")
            }

        return areas.take(6)
    }

    private fun generateRecommendations(
        quality: TransitQuality,
        gocharaResults: List<GocharaResult>,
        sadeSatiStatus: SadeSatiStatus?
    ): List<String> {
        val recommendations = mutableListOf<String>()

        when (quality) {
            TransitQuality.EXCELLENT, TransitQuality.GOOD -> {
                recommendations.add("Initiate important projects and ventures")
                recommendations.add("Make significant life decisions with confidence")
                recommendations.add("Expand professional and personal networks")
            }
            TransitQuality.MODERATE -> {
                recommendations.add("Proceed with calculated optimism")
                recommendations.add("Focus on consolidation rather than major expansion")
                recommendations.add("Strengthen existing relationships and commitments")
            }
            TransitQuality.MIXED -> {
                recommendations.add("Maintain balance and avoid extremes")
                recommendations.add("Focus on areas with favorable planetary support")
                recommendations.add("Postpone major decisions if possible")
            }
            TransitQuality.CHALLENGING, TransitQuality.DIFFICULT -> {
                recommendations.add("Practice patience and avoid impulsive decisions")
                recommendations.add("Focus on remedial measures and spiritual practices")
                recommendations.add("Maintain health through proper diet and rest")
                recommendations.add("Avoid starting new major ventures")
            }
        }

        sadeSatiStatus?.let {
            recommendations.add("Perform Saturn remedies: charity on Saturdays, service to elderly")
            recommendations.add("Chant Saturn mantras or Hanuman Chalisa regularly")
        }

        gocharaResults.find { it.planet == Planet.SATURN && it.effect in listOf(TransitEffect.CHALLENGING, TransitEffect.DIFFICULT) }?.let {
            recommendations.add("Saturn transit challenges: maintain discipline, accept delays gracefully")
        }

        gocharaResults.find { it.planet == Planet.JUPITER && it.effect in listOf(TransitEffect.GOOD, TransitEffect.EXCELLENT) }?.let {
            recommendations.add("Leverage favorable Jupiter: pursue education, spirituality, and righteous actions")
        }

        return recommendations.distinct().take(5)
    }

    fun close() {
        ephemerisEngine.close()
    }
}