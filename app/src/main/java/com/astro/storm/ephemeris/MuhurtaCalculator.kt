package com.astro.storm.ephemeris

import android.content.Context
import com.astro.storm.data.model.Planet
import com.astro.storm.data.model.Nakshatra
import com.astro.storm.data.model.ZodiacSign
import swisseph.DblObj
import swisseph.SweConst
import swisseph.SweDate
import swisseph.SwissEph
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit
import kotlin.math.abs
import kotlin.math.floor

/**
 * Production-Grade Muhurta Calculator for Vedic Astrology
 *
 * Implements comprehensive electional astrology (Muhurta Shastra) for finding
 * auspicious timings for various activities based on:
 *
 * 1. Panchanga Elements (Tithi, Nakshatra, Yoga, Karana, Vara)
 * 2. Choghadiya (Gujarati/North Indian system)
 * 3. Hora (Planetary hours)
 * 4. Rahukala, Yamaghanta, Gulika Kala (Inauspicious periods)
 * 5. Activity-specific Muhurta rules
 *
 * Based on:
 * - Muhurta Chintamani
 * - Brihat Samhita
 * - Kalaprakashika
 * - Dharmasindhu
 *
 * @author AstroStorm - Ultra-Precision Vedic Astrology
 */
class MuhurtaCalculator(context: Context) {

    private val swissEph = SwissEph()
    private val ephemerisPath: String

    companion object {
        private const val AYANAMSA_LAHIRI = SweConst.SE_SIDM_LAHIRI
        private const val SEFLG_SIDEREAL = SweConst.SEFLG_SIDEREAL
        private const val SEFLG_SPEED = SweConst.SEFLG_SPEED
    }

    init {
        ephemerisPath = context.filesDir.absolutePath + "/ephe"
        swissEph.swe_set_ephe_path(ephemerisPath)
        swissEph.swe_set_sid_mode(AYANAMSA_LAHIRI, 0.0, 0.0)
    }

    /**
     * Activity types for Muhurta selection
     */
    enum class ActivityType(
        val displayName: String,
        val description: String,
        val icon: String,
        val favorableNakshatras: List<Nakshatra>,
        val favorableTithis: List<Int>,
        val favorableVaras: List<Vara>,
        val avoidNakshatras: List<Nakshatra>
    ) {
        MARRIAGE(
            "Marriage",
            "Wedding ceremonies and engagements",
            "ring",
            listOf(
                Nakshatra.ROHINI, Nakshatra.MRIGASHIRA, Nakshatra.MAGHA,
                Nakshatra.UTTARA_PHALGUNI, Nakshatra.HASTA, Nakshatra.SWATI,
                Nakshatra.ANURADHA, Nakshatra.MULA, Nakshatra.UTTARA_ASHADHA,
                Nakshatra.UTTARA_BHADRAPADA, Nakshatra.REVATI
            ),
            listOf(2, 3, 5, 7, 10, 11, 12, 13), // Avoid 4, 9, 14, 8, 6
            listOf(Vara.MONDAY, Vara.WEDNESDAY, Vara.THURSDAY, Vara.FRIDAY),
            listOf(Nakshatra.BHARANI, Nakshatra.KRITTIKA, Nakshatra.ARDRA, Nakshatra.ASHLESHA)
        ),
        TRAVEL(
            "Travel",
            "Journey and trips",
            "flight",
            listOf(
                Nakshatra.ASHWINI, Nakshatra.MRIGASHIRA, Nakshatra.PUNARVASU,
                Nakshatra.PUSHYA, Nakshatra.HASTA, Nakshatra.ANURADHA,
                Nakshatra.SHRAVANA, Nakshatra.REVATI
            ),
            listOf(2, 3, 5, 7, 10, 11, 12, 13),
            listOf(Vara.MONDAY, Vara.WEDNESDAY, Vara.THURSDAY, Vara.FRIDAY),
            listOf(Nakshatra.ARDRA, Nakshatra.ASHLESHA, Nakshatra.JYESHTHA)
        ),
        BUSINESS(
            "Business",
            "New ventures, contracts, deals",
            "business",
            listOf(
                Nakshatra.ROHINI, Nakshatra.PUSHYA, Nakshatra.HASTA,
                Nakshatra.CHITRA, Nakshatra.SWATI, Nakshatra.ANURADHA,
                Nakshatra.SHRAVANA, Nakshatra.DHANISHTHA, Nakshatra.REVATI
            ),
            listOf(1, 2, 3, 5, 7, 10, 11, 13),
            listOf(Vara.WEDNESDAY, Vara.THURSDAY, Vara.FRIDAY),
            listOf(Nakshatra.BHARANI, Nakshatra.ASHLESHA, Nakshatra.MULA)
        ),
        PROPERTY(
            "Property",
            "Buying/selling property, house entry",
            "home",
            listOf(
                Nakshatra.ROHINI, Nakshatra.MRIGASHIRA, Nakshatra.UTTARA_PHALGUNI,
                Nakshatra.HASTA, Nakshatra.CHITRA, Nakshatra.SWATI,
                Nakshatra.ANURADHA, Nakshatra.UTTARA_ASHADHA, Nakshatra.SHRAVANA,
                Nakshatra.UTTARA_BHADRAPADA, Nakshatra.REVATI
            ),
            listOf(2, 3, 5, 7, 10, 11, 12, 13),
            listOf(Vara.MONDAY, Vara.WEDNESDAY, Vara.THURSDAY, Vara.FRIDAY),
            listOf(Nakshatra.ARDRA, Nakshatra.ASHLESHA, Nakshatra.JYESHTHA, Nakshatra.MULA)
        ),
        EDUCATION(
            "Education",
            "Starting studies, examinations",
            "school",
            listOf(
                Nakshatra.ASHWINI, Nakshatra.ROHINI, Nakshatra.MRIGASHIRA,
                Nakshatra.PUNARVASU, Nakshatra.PUSHYA, Nakshatra.HASTA,
                Nakshatra.CHITRA, Nakshatra.SWATI, Nakshatra.SHRAVANA, Nakshatra.REVATI
            ),
            listOf(2, 3, 5, 7, 10, 11, 12),
            listOf(Vara.WEDNESDAY, Vara.THURSDAY, Vara.FRIDAY),
            listOf(Nakshatra.KRITTIKA, Nakshatra.ARDRA, Nakshatra.ASHLESHA)
        ),
        MEDICAL(
            "Medical",
            "Surgery, treatments, health procedures",
            "medical",
            listOf(
                Nakshatra.ASHWINI, Nakshatra.ROHINI, Nakshatra.MRIGASHIRA,
                Nakshatra.PUNARVASU, Nakshatra.PUSHYA, Nakshatra.UTTARA_PHALGUNI,
                Nakshatra.HASTA, Nakshatra.SHRAVANA, Nakshatra.REVATI
            ),
            listOf(2, 3, 6, 7, 10, 11),
            listOf(Vara.MONDAY, Vara.WEDNESDAY, Vara.THURSDAY, Vara.FRIDAY),
            listOf(Nakshatra.BHARANI, Nakshatra.KRITTIKA, Nakshatra.ARDRA, Nakshatra.ASHLESHA)
        ),
        VEHICLE(
            "Vehicle",
            "Purchasing or first drive of vehicle",
            "car",
            listOf(
                Nakshatra.ASHWINI, Nakshatra.ROHINI, Nakshatra.PUSHYA,
                Nakshatra.HASTA, Nakshatra.SWATI, Nakshatra.SHRAVANA,
                Nakshatra.DHANISHTHA, Nakshatra.REVATI
            ),
            listOf(2, 3, 5, 7, 10, 11, 12),
            listOf(Vara.WEDNESDAY, Vara.THURSDAY, Vara.FRIDAY),
            listOf(Nakshatra.BHARANI, Nakshatra.MULA, Nakshatra.VISHAKHA)
        ),
        SPIRITUAL(
            "Spiritual",
            "Religious ceremonies, puja, initiation",
            "temple",
            listOf(
                Nakshatra.ASHWINI, Nakshatra.PUNARVASU, Nakshatra.PUSHYA,
                Nakshatra.HASTA, Nakshatra.SWATI, Nakshatra.ANURADHA,
                Nakshatra.SHRAVANA, Nakshatra.UTTARA_BHADRAPADA, Nakshatra.REVATI
            ),
            listOf(2, 3, 5, 7, 10, 11, 12, 15), // Purnima is auspicious
            listOf(Vara.MONDAY, Vara.THURSDAY, Vara.FRIDAY),
            listOf(Nakshatra.KRITTIKA, Nakshatra.ARDRA, Nakshatra.ASHLESHA)
        ),
        GENERAL(
            "General",
            "General auspicious activities",
            "star",
            listOf(
                Nakshatra.ASHWINI, Nakshatra.ROHINI, Nakshatra.MRIGASHIRA,
                Nakshatra.PUNARVASU, Nakshatra.PUSHYA, Nakshatra.UTTARA_PHALGUNI,
                Nakshatra.HASTA, Nakshatra.CHITRA, Nakshatra.SWATI,
                Nakshatra.ANURADHA, Nakshatra.SHRAVANA, Nakshatra.DHANISHTHA,
                Nakshatra.UTTARA_BHADRAPADA, Nakshatra.REVATI
            ),
            listOf(2, 3, 5, 7, 10, 11, 12, 13),
            listOf(Vara.MONDAY, Vara.WEDNESDAY, Vara.THURSDAY, Vara.FRIDAY),
            listOf(Nakshatra.BHARANI, Nakshatra.KRITTIKA, Nakshatra.ARDRA, Nakshatra.ASHLESHA)
        )
    }

    /**
     * Vara (Day of week) with planetary lord
     */
    enum class Vara(val dayNumber: Int, val displayName: String, val lord: Planet) {
        SUNDAY(0, "Sunday", Planet.SUN),
        MONDAY(1, "Monday", Planet.MOON),
        TUESDAY(2, "Tuesday", Planet.MARS),
        WEDNESDAY(3, "Wednesday", Planet.MERCURY),
        THURSDAY(4, "Thursday", Planet.JUPITER),
        FRIDAY(5, "Friday", Planet.VENUS),
        SATURDAY(6, "Saturday", Planet.SATURN)
    }

    /**
     * Choghadiya types
     */
    enum class Choghadiya(
        val displayName: String,
        val nature: ChoghadiyaNature,
        val lord: Planet
    ) {
        UDVEG("Udveg", ChoghadiyaNature.INAUSPICIOUS, Planet.SUN),
        CHAR("Char", ChoghadiyaNature.GOOD, Planet.VENUS),
        LABH("Labh", ChoghadiyaNature.VERY_GOOD, Planet.MERCURY),
        AMRIT("Amrit", ChoghadiyaNature.EXCELLENT, Planet.MOON),
        KAAL("Kaal", ChoghadiyaNature.INAUSPICIOUS, Planet.SATURN),
        SHUBH("Shubh", ChoghadiyaNature.VERY_GOOD, Planet.JUPITER),
        ROG("Rog", ChoghadiyaNature.INAUSPICIOUS, Planet.MARS)
    }

    enum class ChoghadiyaNature(val displayName: String, val score: Int) {
        EXCELLENT("Excellent", 4),
        VERY_GOOD("Very Good", 3),
        GOOD("Good", 2),
        NEUTRAL("Neutral", 1),
        INAUSPICIOUS("Inauspicious", 0)
    }

    /**
     * Hora (Planetary Hour) result
     */
    data class Hora(
        val lord: Planet,
        val startTime: LocalTime,
        val endTime: LocalTime,
        val isDay: Boolean,
        val nature: HoraNature
    )

    enum class HoraNature(val displayName: String) {
        BENEFIC("Benefic"),
        MALEFIC("Malefic"),
        NEUTRAL("Neutral")
    }

    /**
     * Rahukala and other inauspicious periods
     */
    data class InauspiciousPeriods(
        val rahukala: Pair<LocalTime, LocalTime>,
        val yamaghanta: Pair<LocalTime, LocalTime>,
        val gulikaKala: Pair<LocalTime, LocalTime>
    )

    /**
     * Complete Muhurta for a specific time
     */
    data class MuhurtaDetails(
        val dateTime: LocalDateTime,
        val vara: Vara,
        val tithi: TithiInfo,
        val nakshatra: NakshatraInfo,
        val yoga: YogaInfo,
        val karana: KaranaInfo,
        val choghadiya: ChoghadiyaInfo,
        val hora: Hora,
        val inauspiciousPeriods: InauspiciousPeriods,
        val sunrise: LocalTime,
        val sunset: LocalTime,
        val overallScore: Int,
        val suitableActivities: List<ActivityType>,
        val avoidActivities: List<ActivityType>,
        val recommendations: List<String>
    ) {
        val isAuspicious: Boolean get() = overallScore >= 60
    }

    data class TithiInfo(
        val number: Int,
        val name: String,
        val paksha: String,
        val lord: Planet,
        val isAuspicious: Boolean
    )

    data class NakshatraInfo(
        val nakshatra: Nakshatra,
        val pada: Int,
        val lord: Planet,
        val nature: String
    )

    data class YogaInfo(
        val number: Int,
        val name: String,
        val nature: String
    )

    data class KaranaInfo(
        val number: Int,
        val name: String,
        val nature: String
    )

    data class ChoghadiyaInfo(
        val choghadiya: Choghadiya,
        val startTime: LocalTime,
        val endTime: LocalTime
    )

    /**
     * Search result for auspicious muhurta
     */
    data class MuhurtaSearchResult(
        val dateTime: LocalDateTime,
        val score: Int,
        val vara: Vara,
        val nakshatra: Nakshatra,
        val tithi: String,
        val choghadiya: Choghadiya,
        val reasons: List<String>,
        val warnings: List<String>
    )

    /**
     * Calculate complete Muhurta details for a specific date and time
     */
    fun calculateMuhurta(
        dateTime: LocalDateTime,
        latitude: Double,
        longitude: Double,
        timezone: String
    ): MuhurtaDetails {
        // Convert to UTC
        val zonedDateTime = ZonedDateTime.of(dateTime, ZoneId.of(timezone))
        val utcDateTime = zonedDateTime.withZoneSameInstant(ZoneId.of("UTC"))
        val julianDay = calculateJulianDay(utcDateTime.toLocalDateTime())

        // Get planetary positions
        val sunLong = getPlanetLongitude(SweConst.SE_SUN, julianDay)
        val moonLong = getPlanetLongitude(SweConst.SE_MOON, julianDay)

        // Calculate sunrise and sunset
        val (sunrise, sunset) = calculateSunriseSunset(julianDay, latitude, longitude)

        // Calculate Vara
        val vara = calculateVara(dateTime.toLocalDate())

        // Calculate Tithi
        val tithi = calculateTithi(sunLong, moonLong)

        // Calculate Nakshatra
        val nakshatra = calculateNakshatra(moonLong)

        // Calculate Yoga
        val yoga = calculateYoga(sunLong, moonLong)

        // Calculate Karana
        val karana = calculateKarana(sunLong, moonLong)

        // Calculate Choghadiya
        val choghadiya = calculateChoghadiya(dateTime.toLocalTime(), vara, sunrise, sunset)

        // Calculate Hora
        val hora = calculateHora(dateTime.toLocalTime(), vara, sunrise, sunset)

        // Calculate inauspicious periods
        val inauspiciousPeriods = calculateInauspiciousPeriods(vara, sunrise, sunset)

        // Calculate overall score and suitability
        val (score, suitable, avoid, recommendations) = evaluateMuhurta(
            vara, tithi, nakshatra, yoga, karana, choghadiya, hora, dateTime.toLocalTime(), inauspiciousPeriods
        )

        return MuhurtaDetails(
            dateTime = dateTime,
            vara = vara,
            tithi = tithi,
            nakshatra = nakshatra,
            yoga = yoga,
            karana = karana,
            choghadiya = choghadiya,
            hora = hora,
            inauspiciousPeriods = inauspiciousPeriods,
            sunrise = sunrise,
            sunset = sunset,
            overallScore = score,
            suitableActivities = suitable,
            avoidActivities = avoid,
            recommendations = recommendations
        )
    }

    /**
     * Find auspicious muhurtas for a specific activity within a date range
     */
    fun findAuspiciousMuhurtas(
        activity: ActivityType,
        startDate: LocalDate,
        endDate: LocalDate,
        latitude: Double,
        longitude: Double,
        timezone: String,
        minScore: Int = 60
    ): List<MuhurtaSearchResult> {
        val results = mutableListOf<MuhurtaSearchResult>()
        var currentDate = startDate

        while (!currentDate.isAfter(endDate)) {
            // Check multiple time slots throughout the day
            val timeSlots = listOf(
                LocalTime.of(6, 0),
                LocalTime.of(8, 0),
                LocalTime.of(10, 0),
                LocalTime.of(12, 0),
                LocalTime.of(14, 0),
                LocalTime.of(16, 0),
                LocalTime.of(18, 0)
            )

            for (time in timeSlots) {
                val dateTime = LocalDateTime.of(currentDate, time)
                val muhurta = calculateMuhurta(dateTime, latitude, longitude, timezone)

                // Evaluate for specific activity
                val (score, reasons, warnings) = evaluateForActivity(muhurta, activity)

                if (score >= minScore) {
                    results.add(
                        MuhurtaSearchResult(
                            dateTime = dateTime,
                            score = score,
                            vara = muhurta.vara,
                            nakshatra = muhurta.nakshatra.nakshatra,
                            tithi = muhurta.tithi.name,
                            choghadiya = muhurta.choghadiya.choghadiya,
                            reasons = reasons,
                            warnings = warnings
                        )
                    )
                }
            }

            currentDate = currentDate.plusDays(1)
        }

        return results.sortedByDescending { it.score }.take(20)
    }

    /**
     * Get today's complete Choghadiya table
     */
    fun getDailyChoghadiya(
        date: LocalDate,
        latitude: Double,
        longitude: Double,
        timezone: String
    ): List<ChoghadiyaInfo> {
        val dateTime = LocalDateTime.of(date, LocalTime.NOON)
        val zonedDateTime = ZonedDateTime.of(dateTime, ZoneId.of(timezone))
        val utcDateTime = zonedDateTime.withZoneSameInstant(ZoneId.of("UTC"))
        val julianDay = calculateJulianDay(utcDateTime.toLocalDateTime())

        val (sunrise, sunset) = calculateSunriseSunset(julianDay, latitude, longitude)
        val vara = calculateVara(date)

        return calculateDayChoghadiya(vara, sunrise, sunset)
    }

    // ==================== CALCULATION METHODS ====================

    private fun calculateVara(date: LocalDate): Vara {
        val dayOfWeek = date.dayOfWeek.value % 7 // Convert to 0-6 (Sunday=0)
        return Vara.entries.find { it.dayNumber == dayOfWeek } ?: Vara.SUNDAY
    }

    private fun calculateTithi(sunLong: Double, moonLong: Double): TithiInfo {
        var diff = moonLong - sunLong
        if (diff < 0) diff += 360.0

        val tithiNumber = (diff / 12.0).toInt() + 1
        val paksha = if (tithiNumber <= 15) "Shukla" else "Krishna"
        val displayNumber = if (tithiNumber <= 15) tithiNumber else tithiNumber - 15

        val tithiNames = listOf(
            "Pratipada", "Dwitiya", "Tritiya", "Chaturthi", "Panchami",
            "Shashthi", "Saptami", "Ashtami", "Navami", "Dashami",
            "Ekadashi", "Dwadashi", "Trayodashi", "Chaturdashi",
            if (paksha == "Shukla") "Purnima" else "Amavasya"
        )

        val name = tithiNames[(displayNumber - 1).coerceIn(0, 14)]

        // Tithi lords (cyclic pattern)
        val lords = listOf(
            Planet.SUN, Planet.MOON, Planet.MARS, Planet.MERCURY,
            Planet.JUPITER, Planet.VENUS, Planet.SATURN, Planet.RAHU
        )
        val lord = lords[(tithiNumber - 1) % 8]

        // Auspicious tithis: 2, 3, 5, 7, 10, 11, 12, 13
        val auspiciousTithis = listOf(2, 3, 5, 7, 10, 11, 12, 13)
        val isAuspicious = displayNumber in auspiciousTithis

        return TithiInfo(
            number = tithiNumber,
            name = "$paksha $name",
            paksha = paksha,
            lord = lord,
            isAuspicious = isAuspicious
        )
    }

    private fun calculateNakshatra(moonLong: Double): NakshatraInfo {
        val nakshatraSpan = 360.0 / 27.0
        val (nakshatra, pada) = Nakshatra.fromLongitude(moonLong)

        val nature = when (nakshatra) {
            Nakshatra.ASHWINI, Nakshatra.PUSHYA, Nakshatra.HASTA -> "Fixed"
            Nakshatra.MRIGASHIRA, Nakshatra.CHITRA, Nakshatra.ANURADHA -> "Soft"
            Nakshatra.KRITTIKA, Nakshatra.ARDRA, Nakshatra.ASHLESHA -> "Sharp"
            else -> "Mixed"
        }

        return NakshatraInfo(
            nakshatra = nakshatra,
            pada = pada,
            lord = nakshatra.ruler,
            nature = nature
        )
    }

    private fun calculateYoga(sunLong: Double, moonLong: Double): YogaInfo {
        var sum = sunLong + moonLong
        if (sum >= 360.0) sum -= 360.0

        val yogaNumber = (sum / (360.0 / 27.0)).toInt() + 1

        val yogaNames = listOf(
            "Vishkumbha", "Priti", "Ayushman", "Saubhagya", "Shobhana",
            "Atiganda", "Sukarma", "Dhriti", "Shoola", "Ganda",
            "Vriddhi", "Dhruva", "Vyaghata", "Harshana", "Vajra",
            "Siddhi", "Vyatipata", "Variyan", "Parigha", "Shiva",
            "Siddha", "Sadhya", "Shubha", "Shukla", "Brahma",
            "Indra", "Vaidhriti"
        )

        val inauspiciousYogas = listOf(1, 6, 9, 10, 13, 15, 17, 19, 27)
        val nature = if (yogaNumber in inauspiciousYogas) "Inauspicious" else "Auspicious"

        return YogaInfo(
            number = yogaNumber,
            name = yogaNames.getOrElse(yogaNumber - 1) { "Unknown" },
            nature = nature
        )
    }

    private fun calculateKarana(sunLong: Double, moonLong: Double): KaranaInfo {
        var diff = moonLong - sunLong
        if (diff < 0) diff += 360.0

        val karanaNumber = (diff / 6.0).toInt() + 1

        val karanaNames = listOf(
            "Kimstughna", "Bava", "Balava", "Kaulava", "Taitila",
            "Garija", "Vanija", "Vishti", "Shakuni", "Chatushpada", "Nagava"
        )

        // Vishti (Bhadra) is inauspicious
        val name = when (karanaNumber) {
            1 -> "Kimstughna"
            in 2..8 -> karanaNames[(karanaNumber - 2) % 7 + 1]
            58 -> "Shakuni"
            59 -> "Chatushpada"
            60 -> "Nagava"
            else -> karanaNames[(karanaNumber - 2) % 7 + 1]
        }

        val nature = if (name == "Vishti") "Inauspicious" else "Auspicious"

        return KaranaInfo(
            number = karanaNumber,
            name = name,
            nature = nature
        )
    }

    private fun calculateChoghadiya(
        time: LocalTime,
        vara: Vara,
        sunrise: LocalTime,
        sunset: LocalTime
    ): ChoghadiyaInfo {
        val dayChoghadiyas = calculateDayChoghadiya(vara, sunrise, sunset)

        // Find current choghadiya
        for (chog in dayChoghadiyas) {
            if (time >= chog.startTime && time < chog.endTime) {
                return chog
            }
        }

        // Default to first choghadiya if not found
        return dayChoghadiyas.first()
    }

    private fun calculateDayChoghadiya(
        vara: Vara,
        sunrise: LocalTime,
        sunset: LocalTime
    ): List<ChoghadiyaInfo> {
        val dayDuration = ChronoUnit.MINUTES.between(sunrise, sunset)
        val choghadiyaDuration = dayDuration / 8

        // Choghadiya sequence for each day
        val daySequences = mapOf(
            Vara.SUNDAY to listOf(Choghadiya.UDVEG, Choghadiya.CHAR, Choghadiya.LABH, Choghadiya.AMRIT, Choghadiya.KAAL, Choghadiya.SHUBH, Choghadiya.ROG, Choghadiya.UDVEG),
            Vara.MONDAY to listOf(Choghadiya.AMRIT, Choghadiya.KAAL, Choghadiya.SHUBH, Choghadiya.ROG, Choghadiya.UDVEG, Choghadiya.CHAR, Choghadiya.LABH, Choghadiya.AMRIT),
            Vara.TUESDAY to listOf(Choghadiya.ROG, Choghadiya.UDVEG, Choghadiya.CHAR, Choghadiya.LABH, Choghadiya.AMRIT, Choghadiya.KAAL, Choghadiya.SHUBH, Choghadiya.ROG),
            Vara.WEDNESDAY to listOf(Choghadiya.LABH, Choghadiya.AMRIT, Choghadiya.KAAL, Choghadiya.SHUBH, Choghadiya.ROG, Choghadiya.UDVEG, Choghadiya.CHAR, Choghadiya.LABH),
            Vara.THURSDAY to listOf(Choghadiya.SHUBH, Choghadiya.ROG, Choghadiya.UDVEG, Choghadiya.CHAR, Choghadiya.LABH, Choghadiya.AMRIT, Choghadiya.KAAL, Choghadiya.SHUBH),
            Vara.FRIDAY to listOf(Choghadiya.CHAR, Choghadiya.LABH, Choghadiya.AMRIT, Choghadiya.KAAL, Choghadiya.SHUBH, Choghadiya.ROG, Choghadiya.UDVEG, Choghadiya.CHAR),
            Vara.SATURDAY to listOf(Choghadiya.KAAL, Choghadiya.SHUBH, Choghadiya.ROG, Choghadiya.UDVEG, Choghadiya.CHAR, Choghadiya.LABH, Choghadiya.AMRIT, Choghadiya.KAAL)
        )

        val sequence = daySequences[vara] ?: daySequences[Vara.SUNDAY]!!

        return sequence.mapIndexed { index, choghadiya ->
            ChoghadiyaInfo(
                choghadiya = choghadiya,
                startTime = sunrise.plusMinutes(index * choghadiyaDuration),
                endTime = sunrise.plusMinutes((index + 1) * choghadiyaDuration)
            )
        }
    }

    private fun calculateHora(
        time: LocalTime,
        vara: Vara,
        sunrise: LocalTime,
        sunset: LocalTime
    ): Hora {
        // Hora sequence starting from day lord
        val horaOrder = listOf(
            Planet.SATURN, Planet.JUPITER, Planet.MARS, Planet.SUN,
            Planet.VENUS, Planet.MERCURY, Planet.MOON
        )

        val dayDuration = ChronoUnit.MINUTES.between(sunrise, sunset)
        val nightDuration = 24 * 60 - dayDuration
        val dayHoraDuration = dayDuration / 12
        val nightHoraDuration = nightDuration / 12

        val isDay = time >= sunrise && time < sunset

        // Calculate which hora we're in
        val minutesSinceSunrise = if (isDay) {
            ChronoUnit.MINUTES.between(sunrise, time)
        } else if (time >= sunset) {
            ChronoUnit.MINUTES.between(sunset, time) + dayDuration
        } else {
            // Before sunrise (night)
            ChronoUnit.MINUTES.between(LocalTime.MIDNIGHT, time) + (24 * 60 - ChronoUnit.MINUTES.between(LocalTime.MIDNIGHT, sunset))
        }

        val horaIndex = if (isDay) {
            (minutesSinceSunrise / dayHoraDuration).toInt().coerceIn(0, 11)
        } else {
            ((minutesSinceSunrise - dayDuration) / nightHoraDuration).toInt().coerceIn(0, 11)
        }

        // Get lord based on day and hora number
        val dayLordIndex = horaOrder.indexOf(vara.lord)
        val actualHoraIndex = (dayLordIndex + horaIndex) % 7
        val horaLord = horaOrder[actualHoraIndex]

        val nature = when (horaLord) {
            Planet.JUPITER, Planet.VENUS, Planet.MERCURY, Planet.MOON -> HoraNature.BENEFIC
            Planet.SATURN, Planet.MARS -> HoraNature.MALEFIC
            else -> HoraNature.NEUTRAL
        }

        val startTime = if (isDay) {
            sunrise.plusMinutes(horaIndex * dayHoraDuration)
        } else {
            sunset.plusMinutes(horaIndex * nightHoraDuration)
        }

        val endTime = if (isDay) {
            sunrise.plusMinutes((horaIndex + 1) * dayHoraDuration)
        } else {
            sunset.plusMinutes((horaIndex + 1) * nightHoraDuration)
        }

        return Hora(
            lord = horaLord,
            startTime = startTime,
            endTime = endTime,
            isDay = isDay,
            nature = nature
        )
    }

    private fun calculateInauspiciousPeriods(
        vara: Vara,
        sunrise: LocalTime,
        sunset: LocalTime
    ): InauspiciousPeriods {
        val dayDuration = ChronoUnit.MINUTES.between(sunrise, sunset) / 8

        // Rahukala positions for each day (1-8 representing 8 muhurtas)
        val rahukalaPositions = mapOf(
            Vara.SUNDAY to 8,
            Vara.MONDAY to 2,
            Vara.TUESDAY to 7,
            Vara.WEDNESDAY to 5,
            Vara.THURSDAY to 6,
            Vara.FRIDAY to 4,
            Vara.SATURDAY to 3
        )

        // Yamaghanta positions
        val yamaghantaPositions = mapOf(
            Vara.SUNDAY to 5,
            Vara.MONDAY to 4,
            Vara.TUESDAY to 3,
            Vara.WEDNESDAY to 2,
            Vara.THURSDAY to 1,
            Vara.FRIDAY to 7,
            Vara.SATURDAY to 6
        )

        // Gulika positions
        val gulikaPositions = mapOf(
            Vara.SUNDAY to 7,
            Vara.MONDAY to 6,
            Vara.TUESDAY to 5,
            Vara.WEDNESDAY to 4,
            Vara.THURSDAY to 3,
            Vara.FRIDAY to 2,
            Vara.SATURDAY to 1
        )

        val rahukalaPos = rahukalaPositions[vara] ?: 1
        val yamaghantaPos = yamaghantaPositions[vara] ?: 1
        val gulikaPos = gulikaPositions[vara] ?: 1

        return InauspiciousPeriods(
            rahukala = Pair(
                sunrise.plusMinutes((rahukalaPos - 1) * dayDuration),
                sunrise.plusMinutes(rahukalaPos * dayDuration)
            ),
            yamaghanta = Pair(
                sunrise.plusMinutes((yamaghantaPos - 1) * dayDuration),
                sunrise.plusMinutes(yamaghantaPos * dayDuration)
            ),
            gulikaKala = Pair(
                sunrise.plusMinutes((gulikaPos - 1) * dayDuration),
                sunrise.plusMinutes(gulikaPos * dayDuration)
            )
        )
    }

    private fun evaluateMuhurta(
        vara: Vara,
        tithi: TithiInfo,
        nakshatra: NakshatraInfo,
        yoga: YogaInfo,
        karana: KaranaInfo,
        choghadiya: ChoghadiyaInfo,
        hora: Hora,
        time: LocalTime,
        inauspiciousPeriods: InauspiciousPeriods
    ): Quadruple<Int, List<ActivityType>, List<ActivityType>, List<String>> {
        var score = 50 // Base score

        val recommendations = mutableListOf<String>()
        val suitableActivities = mutableListOf<ActivityType>()
        val avoidActivities = mutableListOf<ActivityType>()

        // Vara evaluation (+/- 10)
        if (vara in listOf(Vara.MONDAY, Vara.WEDNESDAY, Vara.THURSDAY, Vara.FRIDAY)) {
            score += 10
        } else if (vara == Vara.TUESDAY || vara == Vara.SATURDAY) {
            score -= 5
        }

        // Tithi evaluation (+/- 15)
        if (tithi.isAuspicious) {
            score += 15
        } else {
            score -= 10
            recommendations.add("Tithi is not ideal for new beginnings")
        }

        // Nakshatra evaluation (+/- 20)
        val generalActivity = ActivityType.GENERAL
        if (nakshatra.nakshatra in generalActivity.favorableNakshatras) {
            score += 20
        } else if (nakshatra.nakshatra in generalActivity.avoidNakshatras) {
            score -= 15
            recommendations.add("Nakshatra (${nakshatra.nakshatra.displayName}) is challenging")
        }

        // Yoga evaluation (+/- 10)
        if (yoga.nature == "Auspicious") {
            score += 10
        } else {
            score -= 10
            recommendations.add("Yoga (${yoga.name}) is inauspicious")
        }

        // Karana evaluation (+/- 5)
        if (karana.nature == "Auspicious") {
            score += 5
        } else {
            score -= 5
        }

        // Choghadiya evaluation (+/- 15)
        score += when (choghadiya.choghadiya.nature) {
            ChoghadiyaNature.EXCELLENT -> 15
            ChoghadiyaNature.VERY_GOOD -> 10
            ChoghadiyaNature.GOOD -> 5
            ChoghadiyaNature.NEUTRAL -> 0
            ChoghadiyaNature.INAUSPICIOUS -> -10
        }

        // Hora evaluation (+/- 10)
        score += when (hora.nature) {
            HoraNature.BENEFIC -> 10
            HoraNature.MALEFIC -> -10
            HoraNature.NEUTRAL -> 0
        }

        // Check if in Rahukala
        if (time >= inauspiciousPeriods.rahukala.first && time < inauspiciousPeriods.rahukala.second) {
            score -= 20
            recommendations.add("Currently in Rahukala - avoid important activities")
        }

        // Check if in Yamaghanta
        if (time >= inauspiciousPeriods.yamaghanta.first && time < inauspiciousPeriods.yamaghanta.second) {
            score -= 10
            recommendations.add("Currently in Yamaghanta")
        }

        // Determine suitable activities
        ActivityType.entries.forEach { activity ->
            if (nakshatra.nakshatra in activity.favorableNakshatras &&
                vara in activity.favorableVaras &&
                tithi.isAuspicious) {
                suitableActivities.add(activity)
            }
            if (nakshatra.nakshatra in activity.avoidNakshatras) {
                avoidActivities.add(activity)
            }
        }

        return Quadruple(score.coerceIn(0, 100), suitableActivities, avoidActivities, recommendations)
    }

    private fun evaluateForActivity(
        muhurta: MuhurtaDetails,
        activity: ActivityType
    ): Triple<Int, List<String>, List<String>> {
        var score = 50
        val reasons = mutableListOf<String>()
        val warnings = mutableListOf<String>()

        // Check Nakshatra
        if (muhurta.nakshatra.nakshatra in activity.favorableNakshatras) {
            score += 20
            reasons.add("Excellent Nakshatra: ${muhurta.nakshatra.nakshatra.displayName}")
        } else if (muhurta.nakshatra.nakshatra in activity.avoidNakshatras) {
            score -= 20
            warnings.add("Unfavorable Nakshatra for this activity")
        }

        // Check Vara
        if (muhurta.vara in activity.favorableVaras) {
            score += 15
            reasons.add("Favorable day: ${muhurta.vara.displayName}")
        }

        // Check Tithi
        val tithiNumber = muhurta.tithi.number % 15
        if (tithiNumber == 0) {
            // Purnima or Amavasya
            if (activity == ActivityType.SPIRITUAL) {
                score += 10
            }
        } else if (tithiNumber in activity.favorableTithis) {
            score += 10
            reasons.add("Favorable Tithi")
        }

        // Check Choghadiya
        if (muhurta.choghadiya.choghadiya.nature == ChoghadiyaNature.EXCELLENT ||
            muhurta.choghadiya.choghadiya.nature == ChoghadiyaNature.VERY_GOOD) {
            score += 10
            reasons.add("Auspicious Choghadiya: ${muhurta.choghadiya.choghadiya.displayName}")
        } else if (muhurta.choghadiya.choghadiya.nature == ChoghadiyaNature.INAUSPICIOUS) {
            score -= 10
            warnings.add("Inauspicious Choghadiya")
        }

        // Check Hora
        if (muhurta.hora.nature == HoraNature.BENEFIC) {
            score += 5
        }

        // Check if in Rahukala
        val time = muhurta.dateTime.toLocalTime()
        if (time >= muhurta.inauspiciousPeriods.rahukala.first &&
            time < muhurta.inauspiciousPeriods.rahukala.second) {
            score -= 25
            warnings.add("Rahukala period - avoid")
        }

        return Triple(score.coerceIn(0, 100), reasons, warnings)
    }

    // ==================== SWISS EPHEMERIS METHODS ====================

    private fun getPlanetLongitude(planetId: Int, julianDay: Double): Double {
        val xx = DoubleArray(6)
        val serr = StringBuffer()

        swissEph.swe_calc_ut(
            julianDay,
            planetId,
            SEFLG_SIDEREAL or SEFLG_SPEED,
            xx,
            serr
        )

        return ((xx[0] % 360.0) + 360.0) % 360.0
    }

    private fun calculateSunriseSunset(
        julianDay: Double,
        latitude: Double,
        longitude: Double
    ): Pair<LocalTime, LocalTime> {
        val geopos = doubleArrayOf(longitude, latitude, 0.0)
        val tret = DblObj()
        val serr = StringBuffer()

        // Calculate sunrise
        swissEph.swe_rise_trans(
            julianDay,
            SweConst.SE_SUN,
            null,
            SweConst.SEFLG_SWIEPH,
            SweConst.SE_CALC_RISE,
            geopos,
            0.0,
            0.0,
            tret,
            serr
        )
        val sunriseJD = tret.`val`

        // Calculate sunset
        swissEph.swe_rise_trans(
            julianDay,
            SweConst.SE_SUN,
            null,
            SweConst.SEFLG_SWIEPH,
            SweConst.SE_CALC_SET,
            geopos,
            0.0,
            0.0,
            tret,
            serr
        )
        val sunsetJD = tret.`val`

        return Pair(
            jdToLocalTime(sunriseJD),
            jdToLocalTime(sunsetJD)
        )
    }

    private fun jdToLocalTime(jd: Double): LocalTime {
        val sweDate = SweDate(jd)
        val hour = sweDate.hour.toInt()
        val minute = ((sweDate.hour - hour) * 60).toInt()
        return LocalTime.of(hour.coerceIn(0, 23), minute.coerceIn(0, 59))
    }

    private fun calculateJulianDay(dateTime: LocalDateTime): Double {
        val decimalHours = dateTime.hour + (dateTime.minute / 60.0) + (dateTime.second / 3600.0)
        val sweDate = SweDate(
            dateTime.year,
            dateTime.monthValue,
            dateTime.dayOfMonth,
            decimalHours,
            SweDate.SE_GREG_CAL
        )
        return sweDate.julDay
    }

    fun close() {
        swissEph.swe_close()
    }

    // Helper data class for returning 4 values
    private data class Quadruple<A, B, C, D>(
        val first: A,
        val second: B,
        val third: C,
        val fourth: D
    )
}
