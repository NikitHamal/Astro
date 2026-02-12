package com.astro.storm.ephemeris

import com.astro.storm.core.model.Planet
import com.astro.storm.core.model.PlanetPosition
import com.astro.storm.core.model.VedicChart
import java.time.LocalDateTime
import kotlin.math.PI
import kotlin.math.asin
import kotlin.math.sin

/**
 * KalaBalaCalculator - Comprehensive Temporal Strength Analysis
 *
 * Kala Bala (Temporal Strength) is one of the six components of Shadbala.
 * It measures the strength a planet gains from time-related factors.
 *
 * Components (9 sub-components):
 * 1. Nathonnatha Bala - Day/Night strength (0-60 virupas)
 * 2. Paksha Bala - Lunar fortnight strength (0-60 virupas)
 * 3. Tribhaga Bala - Day/Night third strength (0-60 virupas)
 * 4. Varsha Bala - Year lord strength (0-15 virupas)
 * 5. Masa Bala - Month lord strength (0-30 virupas)
 * 6. Dina Bala - Day lord strength (0-45 virupas)
 * 7. Hora Bala - Hour lord strength (0-60 virupas)
 * 8. Ayana Bala - Declination/Solstice strength (0-60 virupas)
 * 9. Yuddha Bala - Planetary war strength (-30 to +30 virupas)
 *
 * Vedic References:
 * - BPHS (Brihat Parashara Hora Shastra) Chapter 28
 * - Surya Siddhanta
 */
object KalaBalaCalculator {

    private const val VIRUPAS_PER_RUPA = 60.0
    private const val DEGREES_PER_CIRCLE = 360.0

    /**
     * Planets used in Kala Bala calculation (7 classical planets)
     */
    private val KALA_BALA_PLANETS = setOf(
        Planet.SUN, Planet.MOON, Planet.MARS,
        Planet.MERCURY, Planet.JUPITER, Planet.VENUS, Planet.SATURN
    )

    /**
     * Diurnal/Nocturnal classification
     */
    enum class TemporalNature(val displayName: String) {
        DIURNAL("Diurnal"),
        NOCTURNAL("Nocturnal"),
        NEUTRAL("Neutral")
    }

    /**
     * Paksha (lunar fortnight) type
     */
    enum class PakshaType(val displayName: String) {
        SHUKLA("Shukla Paksha (Bright)"),
        KRISHNA("Krishna Paksha (Dark)")
    }

    /**
     * Tribhaga (day/night third) type
     */
    enum class TribhagaPeriod(val displayName: String, val ruler: Planet?) {
        DAY_FIRST("First Day Tribhaga", Planet.MERCURY),
        DAY_SECOND("Second Day Tribhaga", Planet.SUN),
        DAY_THIRD("Third Day Tribhaga", Planet.SATURN),
        NIGHT_FIRST("First Night Tribhaga", Planet.MOON),
        NIGHT_SECOND("Second Night Tribhaga", Planet.VENUS),
        NIGHT_THIRD("Third Night Tribhaga", Planet.MARS)
    }

    /**
     * Nathonnatha Bala analysis
     */
    data class NathonnathaBalaAnalysis(
        val planet: Planet,
        val isDay: Boolean,
        val temporalNature: TemporalNature,
        val virupas: Double,
        val interpretation: String
    )

    /**
     * Paksha Bala analysis
     */
    data class PakshaBalaAnalysis(
        val planet: Planet,
        val pakshaType: PakshaType,
        val lunarElongation: Double,
        val tithiNumber: Int,
        val isBenefic: Boolean,
        val virupas: Double,
        val interpretation: String
    )

    /**
     * Tribhaga Bala analysis
     */
    data class TribhagaBalaAnalysis(
        val planet: Planet,
        val tribhagaPeriod: TribhagaPeriod,
        val isRuler: Boolean,
        val virupas: Double,
        val interpretation: String
    )

    /**
     * Hora Adi Bala analysis (includes Varsha, Masa, Dina, Hora)
     */
    data class HoraAdiBalaAnalysis(
        val planet: Planet,
        val varshaBala: Double,
        val masaBala: Double,
        val dinaBala: Double,
        val horaBala: Double,
        val yearLord: Planet,
        val monthLord: Planet,
        val dayLord: Planet,
        val horaLord: Planet,
        val totalVirupas: Double,
        val interpretation: String
    )

    /**
     * Ayana Bala analysis
     */
    data class AyanaBalaAnalysis(
        val planet: Planet,
        val declination: Double,
        val isNorthernPreference: Boolean,
        val virupas: Double,
        val interpretation: String
    )

    /**
     * Yuddha Bala analysis
     */
    data class YuddhaBalaAnalysis(
        val planet: Planet,
        val inWar: Boolean,
        val warOpponents: List<Planet>,
        val isWinner: Boolean,
        val virupas: Double,
        val interpretation: String
    )

    /**
     * Planet's complete Kala Bala analysis
     */
    data class PlanetKalaBala(
        val planet: Planet,
        val nathonnathaBala: NathonnathaBalaAnalysis,
        val pakshaBala: PakshaBalaAnalysis,
        val tribhagaBala: TribhagaBalaAnalysis,
        val horaAdiBala: HoraAdiBalaAnalysis,
        val ayanaBala: AyanaBalaAnalysis,
        val yuddhaBala: YuddhaBalaAnalysis,
        val totalVirupas: Double,
        val totalRupas: Double,
        val requiredVirupas: Double,
        val percentageOfRequired: Double,
        val strengthRating: String,
        val interpretation: String
    )

    /**
     * Component summary for overview
     */
    data class ComponentSummary(
        val name: String,
        val avgVirupas: Double,
        val maxVirupas: Double,
        val percentage: Double,
        val description: String
    )

    /**
     * Complete Kala Bala analysis result
     */
    data class KalaBalaAnalysis(
        val planetaryKalaBala: Map<Planet, PlanetKalaBala>,
        val componentSummary: List<ComponentSummary>,
        val birthContext: BirthContext,
        val strongestPlanet: Planet,
        val weakestPlanet: Planet,
        val overallScore: Double,
        val keyInsights: List<String>,
        val recommendations: List<String>
    )

    /**
     * Birth context data
     */
    data class BirthContext(
        val isDay: Boolean,
        val sunriseJD: Double,
        val sunsetJD: Double,
        val pakshaType: PakshaType,
        val tribhagaPeriod: TribhagaPeriod,
        val yearLord: Planet,
        val monthLord: Planet,
        val dayLord: Planet,
        val horaLord: Planet,
        val lunarElongation: Double,
        val tithiNumber: Int
    )

    /**
     * Required minimum virupas for each planet
     */
    private val requiredVirupas = mapOf(
        Planet.SUN to 164.0,
        Planet.MOON to 133.0,
        Planet.MARS to 96.0,
        Planet.MERCURY to 165.0,
        Planet.JUPITER to 165.0,
        Planet.VENUS to 133.0,
        Planet.SATURN to 96.0
    )

    /**
     * Planetary war brightness order (brighter wins)
     */
    private val warBrightnessOrder = listOf(
        Planet.VENUS, Planet.JUPITER, Planet.MERCURY, Planet.MARS, Planet.SATURN
    )

    /**
     * Perform complete Kala Bala analysis
     */
    fun analyzeKalaBala(context: android.content.Context, chart: VedicChart): KalaBalaAnalysis {
        val planetMap = chart.planetPositions.associateBy { it.planet }
        val birthContext = calculateBirthContext(context, chart)

        val planetaryKalaBala = mutableMapOf<Planet, PlanetKalaBala>()

        for (position in chart.planetPositions) {
            if (position.planet in KALA_BALA_PLANETS) {
                planetaryKalaBala[position.planet] = calculatePlanetKalaBala(
                    position, birthContext, planetMap
                )
            }
        }

        val componentSummary = calculateComponentSummary(planetaryKalaBala)

        val sortedByStrength = planetaryKalaBala.values.sortedByDescending { it.totalVirupas }
        require(sortedByStrength.isNotEmpty()) { "Kala Bala calculation requires at least one valid planetary position." }
        val strongestPlanet = sortedByStrength.first().planet
        val weakestPlanet = sortedByStrength.last().planet

        val overallScore = calculateOverallScore(planetaryKalaBala)
        val insights = generateInsights(planetaryKalaBala, birthContext)
        val recommendations = generateRecommendations(planetaryKalaBala, birthContext)

        return KalaBalaAnalysis(
            planetaryKalaBala = planetaryKalaBala.toMap(),
            componentSummary = componentSummary,
            birthContext = birthContext,
            strongestPlanet = strongestPlanet,
            weakestPlanet = weakestPlanet,
            overallScore = overallScore,
            keyInsights = insights,
            recommendations = recommendations
        )
    }

    /**
     * Calculate birth context
     */
    private fun calculateBirthContext(context: android.content.Context, chart: VedicChart): BirthContext {
        val panchangaCalculator = PanchangaCalculator(context)
        val panchanga = panchangaCalculator.calculatePanchanga(
            chart.birthData.dateTime,
            chart.birthData.latitude,
            chart.birthData.longitude,
            chart.birthData.timezone
        )
        panchangaCalculator.close()

        val sunriseJD = panchanga.sunriseJD
        val sunsetJD = panchanga.sunsetJD
        val birthJD = chart.julianDay

        val isDay = birthJD in sunriseJD..sunsetJD

        val sunPos = chart.planetPositions.find { it.planet == Planet.SUN }
        val moonPos = chart.planetPositions.find { it.planet == Planet.MOON }

        val lunarElongation = if (sunPos != null && moonPos != null) {
            VedicAstrologyUtils.normalizeDegree(moonPos.longitude - sunPos.longitude)
        } else 0.0

        val pakshaType = if (lunarElongation < 180.0) PakshaType.SHUKLA else PakshaType.KRISHNA
        val tithiNumber = ((lunarElongation / 12.0).toInt() % 15) + 1

        val dateTime = chart.birthData.dateTime
        val dayLord = getDayLord(dateTime.dayOfWeek.value)
        val horaLord = calculateHoraLord(dateTime, dayLord)
        val yearLord = calculateYearLord(dateTime.year)
        val monthLord = calculateMonthLord(dateTime.monthValue)

        val tribhagaPeriod = if (isDay) {
            val dayDuration = sunsetJD - sunriseJD
            val tribhagaSize = dayDuration / 3.0
            when {
                birthJD < sunriseJD + tribhagaSize -> TribhagaPeriod.DAY_FIRST
                birthJD < sunriseJD + 2.0 * tribhagaSize -> TribhagaPeriod.DAY_SECOND
                else -> TribhagaPeriod.DAY_THIRD
            }
        } else {
            // Night can wrap around midnight JD.
            // If birthJD < sunriseJD, it's before sunrise but after previous sunset.
            // If birthJD > sunsetJD, it's after sunset but before next sunrise.
            // For simplicity in Shadbala, we usually use the night span between current day's sunset and NEXT day's sunrise,
            // or PREVIOUS day's sunset and current day's sunrise.
            
            // Calculate next sunrise to get proper night tribhaga
            val nextDayJD = chart.julianDay + 1.0
            val pNext = PanchangaCalculator(context).use { 
                it.calculatePanchanga(
                    chart.birthData.dateTime.plusDays(1),
                    chart.birthData.latitude,
                    chart.birthData.longitude,
                    chart.birthData.timezone
                )
            }
            val nextSunriseJD = pNext.sunriseJD
            
            val prevDayJD = chart.julianDay - 1.0
            val pPrev = PanchangaCalculator(context).use {
                it.calculatePanchanga(
                    chart.birthData.dateTime.minusDays(1),
                    chart.birthData.latitude,
                    chart.birthData.longitude,
                    chart.birthData.timezone
                )
            }
            val prevSunsetJD = pPrev.sunsetJD

            val (nightStart, nightEnd) = if (birthJD < sunriseJD) {
                Pair(prevSunsetJD, sunriseJD)
            } else {
                Pair(sunsetJD, nextSunriseJD)
            }

            val nightDuration = nightEnd - nightStart
            val tribhagaSize = nightDuration / 3.0
            when {
                birthJD < nightStart + tribhagaSize -> TribhagaPeriod.NIGHT_FIRST
                birthJD < nightStart + 2.0 * tribhagaSize -> TribhagaPeriod.NIGHT_SECOND
                else -> TribhagaPeriod.NIGHT_THIRD
            }
        }

        return BirthContext(
            isDay = isDay,
            sunriseJD = sunriseJD,
            sunsetJD = sunsetJD,
            pakshaType = pakshaType,
            tribhagaPeriod = tribhagaPeriod,
            yearLord = yearLord,
            monthLord = monthLord,
            dayLord = dayLord,
            horaLord = horaLord,
            lunarElongation = lunarElongation,
            tithiNumber = tithiNumber
        )
    }

    /**
     * Calculate Kala Bala for a single planet
     */
    private fun calculatePlanetKalaBala(
        position: PlanetPosition,
        context: BirthContext,
        planetMap: Map<Planet, PlanetPosition>
    ): PlanetKalaBala {
        val planet = position.planet

        val nathonnathaBala = calculateNathonnathaBala(planet, context)
        val pakshaBala = calculatePakshaBala(planet, context)
        val tribhagaBala = calculateTribhagaBala(planet, context)
        val horaAdiBala = calculateHoraAdiBala(planet, context)
        val ayanaBala = calculateAyanaBala(position)
        val yuddhaBala = calculateYuddhaBala(position, planetMap)

        val totalVirupas = nathonnathaBala.virupas + pakshaBala.virupas +
                tribhagaBala.virupas + horaAdiBala.totalVirupas +
                ayanaBala.virupas + yuddhaBala.virupas

        val totalRupas = totalVirupas / VIRUPAS_PER_RUPA
        val required = requiredVirupas[planet] ?: 133.0
        val percentage = (totalVirupas / required) * 100.0

        val rating = when {
            percentage >= 130 -> "Excellent"
            percentage >= 100 -> "Strong"
            percentage >= 80 -> "Average"
            percentage >= 60 -> "Weak"
            else -> "Very Weak"
        }

        return PlanetKalaBala(
            planet = planet,
            nathonnathaBala = nathonnathaBala,
            pakshaBala = pakshaBala,
            tribhagaBala = tribhagaBala,
            horaAdiBala = horaAdiBala,
            ayanaBala = ayanaBala,
            yuddhaBala = yuddhaBala,
            totalVirupas = totalVirupas,
            totalRupas = totalRupas,
            requiredVirupas = required,
            percentageOfRequired = percentage,
            strengthRating = rating,
            interpretation = buildPlanetInterpretation(planet, totalVirupas, percentage, context)
        )
    }

    /**
     * Calculate Nathonnatha Bala (Day/Night strength)
     */
    private fun calculateNathonnathaBala(planet: Planet, context: BirthContext): NathonnathaBalaAnalysis {
        val temporalNature = when (planet) {
            Planet.MERCURY -> TemporalNature.NEUTRAL
            Planet.SUN, Planet.JUPITER, Planet.VENUS -> TemporalNature.DIURNAL
            Planet.MOON, Planet.MARS, Planet.SATURN -> TemporalNature.NOCTURNAL
            else -> TemporalNature.NEUTRAL
        }

        val virupas = when (planet) {
            Planet.MERCURY -> 60.0
            Planet.SUN, Planet.JUPITER, Planet.VENUS -> if (context.isDay) 60.0 else 0.0
            Planet.MOON, Planet.MARS, Planet.SATURN -> if (!context.isDay) 60.0 else 0.0
            else -> 30.0
        }

        return NathonnathaBalaAnalysis(
            planet = planet,
            isDay = context.isDay,
            temporalNature = temporalNature,
            virupas = virupas,
            interpretation = buildNathonnathaInterpretation(planet, context.isDay, temporalNature, virupas)
        )
    }

    /**
     * Calculate Paksha Bala (Lunar fortnight strength)
     */
    private fun calculatePakshaBala(planet: Planet, context: BirthContext): PakshaBalaAnalysis {
        val isBenefic = planet in setOf(Planet.JUPITER, Planet.VENUS, Planet.MOON, Planet.MERCURY)

        val phaseStrength = if (context.lunarElongation < 180.0) {
            context.lunarElongation / 180.0 * 60.0
        } else {
            (DEGREES_PER_CIRCLE - context.lunarElongation) / 180.0 * 60.0
        }

        val virupas = if ((isBenefic && context.pakshaType == PakshaType.SHUKLA) ||
            (!isBenefic && context.pakshaType == PakshaType.KRISHNA)) {
            phaseStrength
        } else {
            60.0 - phaseStrength
        }

        return PakshaBalaAnalysis(
            planet = planet,
            pakshaType = context.pakshaType,
            lunarElongation = context.lunarElongation,
            tithiNumber = context.tithiNumber,
            isBenefic = isBenefic,
            virupas = virupas,
            interpretation = buildPakshaInterpretation(planet, context.pakshaType, isBenefic, virupas)
        )
    }

    /**
     * Calculate Tribhaga Bala (Day/Night third strength)
     */
    private fun calculateTribhagaBala(planet: Planet, context: BirthContext): TribhagaBalaAnalysis {
        val isRuler = context.tribhagaPeriod.ruler == planet

        val virupas = when {
            planet == Planet.JUPITER -> 30.0
            isRuler -> 60.0
            else -> 0.0
        }

        return TribhagaBalaAnalysis(
            planet = planet,
            tribhagaPeriod = context.tribhagaPeriod,
            isRuler = isRuler,
            virupas = virupas,
            interpretation = buildTribhagaInterpretation(planet, context.tribhagaPeriod, isRuler)
        )
    }

    /**
     * Calculate Hora Adi Bala (Varsha, Masa, Dina, Hora combined)
     */
    private fun calculateHoraAdiBala(planet: Planet, context: BirthContext): HoraAdiBalaAnalysis {
        val varshaBala = if (planet == context.yearLord) 15.0 else 0.0
        val masaBala = if (planet == context.monthLord) 30.0 else 0.0
        val dinaBala = if (planet == context.dayLord) 45.0 else 0.0
        val horaBala = if (planet == context.horaLord) 60.0 else 0.0

        val totalVirupas = varshaBala + masaBala + dinaBala + horaBala

        return HoraAdiBalaAnalysis(
            planet = planet,
            varshaBala = varshaBala,
            masaBala = masaBala,
            dinaBala = dinaBala,
            horaBala = horaBala,
            yearLord = context.yearLord,
            monthLord = context.monthLord,
            dayLord = context.dayLord,
            horaLord = context.horaLord,
            totalVirupas = totalVirupas,
            interpretation = buildHoraAdiInterpretation(planet, varshaBala, masaBala, dinaBala, horaBala)
        )
    }

    /**
     * Calculate Ayana Bala (Declination/Solstice strength)
     */
    private fun calculateAyanaBala(position: PlanetPosition): AyanaBalaAnalysis {
        val obliquity = 23.45
        val longitudeRadians = position.longitude * PI / 180.0
        val obliquityRadians = obliquity * PI / 180.0

        val declination = Math.toDegrees(asin(sin(longitudeRadians) * sin(obliquityRadians)))
        val normalizedDeclination = (declination / obliquity) * 30.0

        val isNorthernPreference = position.planet in setOf(Planet.SUN, Planet.MARS, Planet.JUPITER)

        val virupas = when (position.planet) {
            Planet.SUN, Planet.MARS, Planet.JUPITER ->
                (30.0 + normalizedDeclination).coerceIn(0.0, 60.0)
            Planet.MOON, Planet.VENUS, Planet.SATURN ->
                (30.0 - normalizedDeclination).coerceIn(0.0, 60.0)
            Planet.MERCURY -> 30.0
            else -> 30.0
        }

        return AyanaBalaAnalysis(
            planet = position.planet,
            declination = declination,
            isNorthernPreference = isNorthernPreference,
            virupas = virupas,
            interpretation = buildAyanaInterpretation(position.planet, declination, virupas)
        )
    }

    /**
     * Calculate Yuddha Bala (Planetary war strength)
     */
    private fun calculateYuddhaBala(
        position: PlanetPosition,
        planetMap: Map<Planet, PlanetPosition>
    ): YuddhaBalaAnalysis {
        val warCapablePlanets = setOf(Planet.MARS, Planet.MERCURY, Planet.JUPITER, Planet.VENUS, Planet.SATURN)

        if (position.planet !in warCapablePlanets) {
            return YuddhaBalaAnalysis(
                planet = position.planet,
                inWar = false,
                warOpponents = emptyList(),
                isWinner = false,
                virupas = 0.0,
                interpretation = "${position.planet.displayName} does not participate in planetary war."
            )
        }

        val opponents = mutableListOf<Planet>()
        var totalVirupas = 0.0
        var isWinner = false

        for ((planet, otherPos) in planetMap) {
            if (planet == position.planet || planet !in warCapablePlanets) continue

            val distance = VedicAstrologyUtils.angularDistance(position.longitude, otherPos.longitude)

            if (distance <= 1.0) {
                opponents.add(planet)
                val winner = getWarWinner(position.planet, planet)
                if (winner == position.planet) {
                    totalVirupas += 30.0
                    isWinner = true
                } else {
                    totalVirupas -= 30.0
                }
            } else if (distance <= 5.0) {
                opponents.add(planet)
                val warStrength = ((5.0 - distance) / 4.0) * 15.0
                val winner = getWarWinner(position.planet, planet)
                if (winner == position.planet) {
                    totalVirupas += warStrength
                    isWinner = true
                } else {
                    totalVirupas -= warStrength
                }
            }
        }

        return YuddhaBalaAnalysis(
            planet = position.planet,
            inWar = opponents.isNotEmpty(),
            warOpponents = opponents,
            isWinner = isWinner,
            virupas = totalVirupas.coerceIn(-30.0, 30.0),
            interpretation = buildYuddhaInterpretation(position.planet, opponents, isWinner, totalVirupas)
        )
    }

    // ============================================
    // HELPER METHODS
    // ============================================

    private fun getDayLord(dayOfWeek: Int): Planet {
        return when (dayOfWeek) {
            1 -> Planet.MOON
            2 -> Planet.MARS
            3 -> Planet.MERCURY
            4 -> Planet.JUPITER
            5 -> Planet.VENUS
            6 -> Planet.SATURN
            7 -> Planet.SUN
            else -> Planet.SUN
        }
    }

    private fun calculateHoraLord(dateTime: LocalDateTime, dayLord: Planet): Planet {
        val hourSequence = listOf(
            Planet.SUN, Planet.VENUS, Planet.MERCURY, Planet.MOON,
            Planet.SATURN, Planet.JUPITER, Planet.MARS
        )
        val startIndex = hourSequence.indexOf(dayLord)
        val hour = dateTime.hour
        val horasSinceSunrise = if (hour >= 6) hour - 6 else hour + 18
        return hourSequence[(startIndex + horasSinceSunrise) % 7]
    }

    private fun calculateYearLord(year: Int): Planet {
        val baseYear = 2000
        val planets = listOf(Planet.SUN, Planet.MOON, Planet.MARS, Planet.MERCURY,
            Planet.JUPITER, Planet.VENUS, Planet.SATURN)
        val index = (year - baseYear) % 7
        return planets[if (index < 0) index + 7 else index]
    }

    private fun calculateMonthLord(month: Int): Planet {
        val planets = listOf(Planet.MARS, Planet.VENUS, Planet.MERCURY, Planet.MOON,
            Planet.SUN, Planet.MERCURY, Planet.VENUS, Planet.MARS,
            Planet.JUPITER, Planet.SATURN, Planet.SATURN, Planet.JUPITER)
        return planets[(month - 1) % 12]
    }

    private fun getWarWinner(planet1: Planet, planet2: Planet): Planet {
        val index1 = warBrightnessOrder.indexOf(planet1)
        val index2 = warBrightnessOrder.indexOf(planet2)
        return if (index1 != -1 && index2 != -1 && index1 < index2) planet1 else planet2
    }

    // ============================================
    // SUMMARY AND INTERPRETATION
    // ============================================

    private fun calculateComponentSummary(balaMap: Map<Planet, PlanetKalaBala>): List<ComponentSummary> {
        val avgNathonnatha = balaMap.values.map { it.nathonnathaBala.virupas }.average()
        val avgPaksha = balaMap.values.map { it.pakshaBala.virupas }.average()
        val avgTribhaga = balaMap.values.map { it.tribhagaBala.virupas }.average()
        val avgHoraAdi = balaMap.values.map { it.horaAdiBala.totalVirupas }.average()
        val avgAyana = balaMap.values.map { it.ayanaBala.virupas }.average()
        val avgYuddha = balaMap.values.map { it.yuddhaBala.virupas }.average()

        return listOf(
            ComponentSummary("Nathonnatha", avgNathonnatha, 60.0, (avgNathonnatha / 60.0) * 100, "Day/Night strength"),
            ComponentSummary("Paksha", avgPaksha, 60.0, (avgPaksha / 60.0) * 100, "Lunar fortnight"),
            ComponentSummary("Tribhaga", avgTribhaga, 60.0, (avgTribhaga / 60.0) * 100, "Day/Night third"),
            ComponentSummary("Hora Adi", avgHoraAdi, 150.0, (avgHoraAdi / 150.0) * 100, "Time lords"),
            ComponentSummary("Ayana", avgAyana, 60.0, (avgAyana / 60.0) * 100, "Declination"),
            ComponentSummary("Yuddha", avgYuddha + 30.0, 60.0, ((avgYuddha + 30.0) / 60.0) * 100, "Planetary war")
        )
    }

    private fun calculateOverallScore(balaMap: Map<Planet, PlanetKalaBala>): Double {
        if (balaMap.isEmpty()) return 50.0
        return balaMap.values.map { it.percentageOfRequired }.average().coerceIn(0.0, 150.0) / 1.5
    }

    // ============================================
    // INTERPRETATION BUILDERS
    // ============================================

    private fun buildNathonnathaInterpretation(planet: Planet, isDay: Boolean, nature: TemporalNature, virupas: Double): String {
        val timeStr = if (isDay) "day" else "night"
        return when {
            virupas >= 60 -> "${planet.displayName} gains full strength as a ${nature.displayName.lowercase()} planet born during $timeStr."
            virupas >= 30 -> "${planet.displayName} has neutral temporal strength."
            else -> "${planet.displayName} as a ${nature.displayName.lowercase()} planet is weakened by $timeStr birth."
        }
    }

    private fun buildPakshaInterpretation(planet: Planet, paksha: PakshaType, isBenefic: Boolean, virupas: Double): String {
        val beneficStr = if (isBenefic) "benefic" else "malefic"
        val pakshaStr = if (paksha == PakshaType.SHUKLA) "bright fortnight" else "dark fortnight"
        return when {
            virupas >= 45 -> "${planet.displayName} ($beneficStr) gains strong Paksha Bala in $pakshaStr."
            virupas >= 30 -> "${planet.displayName} has moderate Paksha Bala."
            else -> "${planet.displayName} has weak Paksha Bala in current lunar phase."
        }
    }

    private fun buildTribhagaInterpretation(planet: Planet, period: TribhagaPeriod, isRuler: Boolean): String {
        return if (isRuler) {
            "${planet.displayName} rules the ${period.displayName}, gaining full Tribhaga Bala."
        } else if (planet == Planet.JUPITER) {
            "${planet.displayName} gains partial Tribhaga Bala as natural benefic."
        } else {
            "${planet.displayName} does not rule this time period."
        }
    }

    private fun buildHoraAdiInterpretation(planet: Planet, varsha: Double, masa: Double, dina: Double, hora: Double): String {
        val parts = mutableListOf<String>()
        if (varsha > 0) parts.add("Year Lord")
        if (masa > 0) parts.add("Month Lord")
        if (dina > 0) parts.add("Day Lord")
        if (hora > 0) parts.add("Hora Lord")

        return if (parts.isNotEmpty()) {
            "${planet.displayName} is ${parts.joinToString(", ")}, gaining temporal lordship strength."
        } else {
            "${planet.displayName} has no temporal lordship at birth time."
        }
    }

    private fun buildAyanaInterpretation(planet: Planet, declination: Double, virupas: Double): String {
        val hemisphere = if (declination >= 0) "northern" else "southern"
        return when {
            virupas >= 45 -> "${planet.displayName} is well-placed at ${String.format("%.1f", declination)}° $hemisphere declination."
            virupas >= 30 -> "${planet.displayName} has moderate Ayana Bala at ${String.format("%.1f", declination)}° declination."
            else -> "${planet.displayName} has weak Ayana Bala at ${String.format("%.1f", declination)}° declination."
        }
    }

    private fun buildYuddhaInterpretation(planet: Planet, opponents: List<Planet>, isWinner: Boolean, virupas: Double): String {
        if (opponents.isEmpty()) {
            return "${planet.displayName} is not in planetary war."
        }
        val opponentStr = opponents.joinToString(", ") { it.displayName }
        return if (isWinner) {
            "${planet.displayName} wins planetary war against $opponentStr, gaining strength."
        } else {
            "${planet.displayName} loses planetary war to $opponentStr, reducing strength."
        }
    }

    private fun buildPlanetInterpretation(planet: Planet, virupas: Double, percentage: Double, context: BirthContext): String {
        val quality = when {
            percentage >= 120 -> "exceptionally strong temporal strength"
            percentage >= 100 -> "good temporal strength"
            percentage >= 80 -> "moderate temporal strength"
            percentage >= 60 -> "weak temporal strength"
            else -> "very weak temporal strength"
        }
        val timeStr = if (context.isDay) "day" else "night"
        return "${planet.displayName} has $quality (${String.format("%.1f", percentage)}%) for $timeStr birth."
    }

    private fun generateInsights(balaMap: Map<Planet, PlanetKalaBala>, context: BirthContext): List<String> {
        val insights = mutableListOf<String>()

        val timeStr = if (context.isDay) "day" else "night"
        insights.add("Birth during $timeStr time affects planet strengths based on their temporal nature")

        val strongNathonnatha = balaMap.values.filter { it.nathonnathaBala.virupas >= 60 }
        if (strongNathonnatha.isNotEmpty()) {
            insights.add("${strongNathonnatha.joinToString { it.planet.displayName }} gain full day/night strength")
        }

        insights.add("${context.pakshaType.displayName} phase enhances ${if (context.pakshaType == PakshaType.SHUKLA) "benefic" else "malefic"} planets")

        val horaLordPlanet = balaMap.values.find { it.horaAdiBala.horaBala > 0 }
        horaLordPlanet?.let {
            insights.add("${it.planet.displayName} as Hora Lord gains significant temporal strength")
        }

        val inWar = balaMap.values.filter { it.yuddhaBala.inWar }
        if (inWar.isNotEmpty()) {
            insights.add("Planetary war affects ${inWar.joinToString { it.planet.displayName }}")
        }

        return insights.take(5)
    }

    private fun generateRecommendations(balaMap: Map<Planet, PlanetKalaBala>, context: BirthContext): List<String> {
        val recommendations = mutableListOf<String>()

        val weak = balaMap.values.filter { it.percentageOfRequired < 80 }
        weak.forEach {
            recommendations.add("Consider remedies for ${it.planet.displayName}'s weak temporal strength")
        }

        val strong = balaMap.values.filter { it.percentageOfRequired >= 120 }
        strong.forEach {
            recommendations.add("${it.planet.displayName}'s strong temporal support can be leveraged")
        }

        val timeStr = if (context.isDay) "diurnal" else "nocturnal"
        recommendations.add("$timeStr planets are naturally favored at birth time")

        return recommendations.take(5)
    }
}
