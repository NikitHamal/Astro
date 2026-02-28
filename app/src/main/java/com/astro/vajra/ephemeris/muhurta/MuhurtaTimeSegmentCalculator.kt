package com.astro.vajra.ephemeris.muhurta

import com.astro.vajra.core.model.Planet
import com.astro.vajra.ephemeris.muhurta.MuhurtaConstants.CHALDEAN_ORDER
import com.astro.vajra.ephemeris.muhurta.MuhurtaConstants.DAY_CHOGHADIYAS
import com.astro.vajra.ephemeris.muhurta.MuhurtaConstants.DAY_HORAS
import com.astro.vajra.ephemeris.muhurta.MuhurtaConstants.DAY_MUHURTAS
import com.astro.vajra.ephemeris.muhurta.MuhurtaConstants.NIGHT_CHOGHADIYAS
import com.astro.vajra.ephemeris.muhurta.MuhurtaConstants.NIGHT_HORAS
import java.time.LocalTime
import java.time.temporal.ChronoUnit

object MuhurtaTimeSegmentCalculator {
    private const val MINUTES_PER_DAY = 24 * 60

    private fun minutesBetween(start: LocalTime, end: LocalTime): Long {
        val raw = ChronoUnit.MINUTES.between(start, end)
        return if (raw >= 0) raw else raw + MINUTES_PER_DAY
    }

    private fun containsTime(start: LocalTime, end: LocalTime, time: LocalTime): Boolean {
        return if (!end.isBefore(start)) {
            time >= start && time < end
        } else {
            time >= start || time < end
        }
    }

    private fun splitDurations(totalMinutes: Long, parts: Int): List<Long> {
        require(parts > 0) { "parts must be > 0" }
        val base = totalMinutes / parts
        val remainder = totalMinutes % parts
        return (0 until parts).map { i -> base + if (i < remainder) 1 else 0 }
    }

    fun calculateChoghadiya(time: LocalTime, vara: Vara, sunrise: LocalTime, sunset: LocalTime): ChoghadiyaInfo {
        val dayMinutes = minutesBetween(sunrise, sunset)
        require(dayMinutes > 0) { "Invalid sunrise/sunset: sunrise=$sunrise sunset=$sunset" }
        val nightMinutes = MINUTES_PER_DAY - dayMinutes
        val nextSunrise = sunset.plusMinutes(nightMinutes)
        val isDay = containsTime(sunrise, sunset, time)
        return if (isDay) {
            val dayChoghadiyas = calculateAllDayChoghadiya(vara, sunrise, sunset)
            dayChoghadiyas.firstOrNull { containsTime(it.startTime, it.endTime, time) }
                ?: dayChoghadiyas.first()
        } else {
            val nightChoghadiyas = calculateAllNightChoghadiya(vara, sunset, nextSunrise)
            nightChoghadiyas.firstOrNull { containsTime(it.startTime, it.endTime, time) }
                ?: nightChoghadiyas.first()
        }
    }

    fun calculateAllDayChoghadiya(vara: Vara, sunrise: LocalTime, sunset: LocalTime): List<ChoghadiyaInfo> {
        val dayMinutes = minutesBetween(sunrise, sunset)
        require(dayMinutes > 0) { "Invalid day length for choghadiya: sunrise=$sunrise sunset=$sunset" }
        val seq = mapOf(
            Vara.SUNDAY to listOf(Choghadiya.UDVEG, Choghadiya.CHAR, Choghadiya.LABH, Choghadiya.AMRIT, Choghadiya.KAAL, Choghadiya.SHUBH, Choghadiya.ROG, Choghadiya.UDVEG),
            Vara.MONDAY to listOf(Choghadiya.AMRIT, Choghadiya.KAAL, Choghadiya.SHUBH, Choghadiya.ROG, Choghadiya.UDVEG, Choghadiya.CHAR, Choghadiya.LABH, Choghadiya.AMRIT),
            Vara.TUESDAY to listOf(Choghadiya.ROG, Choghadiya.UDVEG, Choghadiya.CHAR, Choghadiya.LABH, Choghadiya.AMRIT, Choghadiya.KAAL, Choghadiya.SHUBH, Choghadiya.ROG),
            Vara.WEDNESDAY to listOf(Choghadiya.LABH, Choghadiya.AMRIT, Choghadiya.KAAL, Choghadiya.SHUBH, Choghadiya.ROG, Choghadiya.UDVEG, Choghadiya.CHAR, Choghadiya.LABH),
            Vara.THURSDAY to listOf(Choghadiya.SHUBH, Choghadiya.ROG, Choghadiya.UDVEG, Choghadiya.CHAR, Choghadiya.LABH, Choghadiya.AMRIT, Choghadiya.KAAL, Choghadiya.SHUBH),
            Vara.FRIDAY to listOf(Choghadiya.CHAR, Choghadiya.LABH, Choghadiya.AMRIT, Choghadiya.KAAL, Choghadiya.SHUBH, Choghadiya.ROG, Choghadiya.UDVEG, Choghadiya.CHAR),
            Vara.SATURDAY to listOf(Choghadiya.KAAL, Choghadiya.SHUBH, Choghadiya.ROG, Choghadiya.UDVEG, Choghadiya.CHAR, Choghadiya.LABH, Choghadiya.AMRIT, Choghadiya.KAAL)
        )[vara] ?: error("Unsupported vara: $vara")
        val durations = splitDurations(dayMinutes, DAY_CHOGHADIYAS)
        var cursor = sunrise
        return seq.mapIndexed { i, c ->
            val start = cursor
            val end = start.plusMinutes(durations[i])
            cursor = end
            ChoghadiyaInfo(c, start, end, true)
        }
    }

    fun calculateAllNightChoghadiya(vara: Vara, sunset: LocalTime, nextSunrise: LocalTime): List<ChoghadiyaInfo> {
        val nightMinutes = minutesBetween(sunset, nextSunrise)
        require(nightMinutes > 0) { "Invalid night length for choghadiya: sunset=$sunset nextSunrise=$nextSunrise" }
        val seq = mapOf(
            Vara.SUNDAY to listOf(Choghadiya.SHUBH, Choghadiya.AMRIT, Choghadiya.CHAR, Choghadiya.ROG, Choghadiya.KAAL, Choghadiya.LABH, Choghadiya.UDVEG, Choghadiya.SHUBH),
            Vara.MONDAY to listOf(Choghadiya.CHAR, Choghadiya.ROG, Choghadiya.KAAL, Choghadiya.LABH, Choghadiya.UDVEG, Choghadiya.SHUBH, Choghadiya.AMRIT, Choghadiya.CHAR),
            Vara.TUESDAY to listOf(Choghadiya.KAAL, Choghadiya.LABH, Choghadiya.UDVEG, Choghadiya.SHUBH, Choghadiya.AMRIT, Choghadiya.CHAR, Choghadiya.ROG, Choghadiya.KAAL),
            Vara.WEDNESDAY to listOf(Choghadiya.UDVEG, Choghadiya.SHUBH, Choghadiya.AMRIT, Choghadiya.CHAR, Choghadiya.ROG, Choghadiya.KAAL, Choghadiya.LABH, Choghadiya.UDVEG),
            Vara.THURSDAY to listOf(Choghadiya.AMRIT, Choghadiya.CHAR, Choghadiya.ROG, Choghadiya.KAAL, Choghadiya.LABH, Choghadiya.UDVEG, Choghadiya.SHUBH, Choghadiya.AMRIT),
            Vara.FRIDAY to listOf(Choghadiya.ROG, Choghadiya.KAAL, Choghadiya.LABH, Choghadiya.UDVEG, Choghadiya.SHUBH, Choghadiya.AMRIT, Choghadiya.CHAR, Choghadiya.ROG),
            Vara.SATURDAY to listOf(Choghadiya.LABH, Choghadiya.UDVEG, Choghadiya.SHUBH, Choghadiya.AMRIT, Choghadiya.CHAR, Choghadiya.ROG, Choghadiya.KAAL, Choghadiya.LABH)
        )[vara] ?: error("Unsupported vara: $vara")
        val durations = splitDurations(nightMinutes, NIGHT_CHOGHADIYAS)
        var cursor = sunset
        return seq.mapIndexed { i, c ->
            val start = cursor
            val end = start.plusMinutes(durations[i])
            cursor = end
            ChoghadiyaInfo(c, start, end, false)
        }
    }

    fun calculateHora(time: LocalTime, vara: Vara, sunrise: LocalTime, sunset: LocalTime): Hora {
        val dayMinutes = minutesBetween(sunrise, sunset)
        require(dayMinutes > 0) { "Invalid sunrise/sunset for hora: sunrise=$sunrise sunset=$sunset" }
        val nightMinutes = MINUTES_PER_DAY - dayMinutes
        val dayDurations = splitDurations(dayMinutes, DAY_HORAS)
        val nightDurations = splitDurations(nightMinutes, NIGHT_HORAS)
        val isDay = containsTime(sunrise, sunset, time)
        val startIdx = CHALDEAN_ORDER.indexOf(vara.lord)

        var cursor = if (isDay) sunrise else sunset
        val durations = if (isDay) dayDurations else nightDurations
        val baseNumber = if (isDay) 0 else DAY_HORAS
        for (i in durations.indices) {
            val start = cursor
            val end = start.plusMinutes(durations[i])
            if (containsTime(start, end, time)) {
                val hourIndex = baseNumber + i
                val lord = CHALDEAN_ORDER[(startIdx + hourIndex) % 7]
                return Hora(
                    lord = lord,
                    horaNumber = hourIndex + 1,
                    startTime = start,
                    endTime = end,
                    isDay = isDay,
                    nature = when (lord) {
                        Planet.JUPITER, Planet.VENUS, Planet.MOON -> HoraNature.BENEFIC
                        Planet.MERCURY -> HoraNature.NEUTRAL
                        else -> HoraNature.MALEFIC
                    }
                )
            }
            cursor = end
        }

        // Inclusive end-edge case exactly at sunset/next sunrise.
        val lastIndex = if (isDay) DAY_HORAS - 1 else NIGHT_HORAS - 1
        val hourIndex = baseNumber + lastIndex
        val lord = CHALDEAN_ORDER[(startIdx + hourIndex) % 7]
        val end = cursor
        val start = end.minusMinutes(durations[lastIndex])
        return Hora(
            lord = lord,
            horaNumber = hourIndex + 1,
            startTime = start,
            endTime = end,
            isDay = isDay,
            nature = when (lord) {
                Planet.JUPITER, Planet.VENUS, Planet.MOON -> HoraNature.BENEFIC
                Planet.MERCURY -> HoraNature.NEUTRAL
                else -> HoraNature.MALEFIC
            }
        )
    }

    fun calculateAllHoras(vara: Vara, sunrise: LocalTime, sunset: LocalTime, nextSunrise: LocalTime): List<Hora> {
        val res = mutableListOf<Hora>()
        val dayMinutes = minutesBetween(sunrise, sunset)
        val nightMinutes = minutesBetween(sunset, nextSunrise)
        require(dayMinutes > 0) { "Invalid day length for horas: sunrise=$sunrise sunset=$sunset" }
        require(nightMinutes > 0) { "Invalid night length for horas: sunset=$sunset nextSunrise=$nextSunrise" }
        val dayDurations = splitDurations(dayMinutes, DAY_HORAS)
        val nightDurations = splitDurations(nightMinutes, NIGHT_HORAS)
        val startIdx = CHALDEAN_ORDER.indexOf(vara.lord)
        var cursor = sunrise
        for (i in 0 until DAY_HORAS) {
            val l = CHALDEAN_ORDER[(startIdx + i) % 7]
            val start = cursor
            val end = start.plusMinutes(dayDurations[i])
            cursor = end
            res.add(Hora(l, i + 1, start, end, true, when (l) {
                Planet.JUPITER, Planet.VENUS, Planet.MOON -> HoraNature.BENEFIC
                Planet.MERCURY -> HoraNature.NEUTRAL
                else -> HoraNature.MALEFIC
            }))
        }
        cursor = sunset
        for (i in 0 until NIGHT_HORAS) {
            val l = CHALDEAN_ORDER[(startIdx + DAY_HORAS + i) % 7]
            val start = cursor
            val end = start.plusMinutes(nightDurations[i])
            cursor = end
            res.add(Hora(l, DAY_HORAS + i + 1, start, end, false, when (l) {
                Planet.JUPITER, Planet.VENUS, Planet.MOON -> HoraNature.BENEFIC
                Planet.MERCURY -> HoraNature.NEUTRAL
                else -> HoraNature.MALEFIC
            }))
        }
        return res
    }

    fun calculateInauspiciousPeriods(vara: Vara, sunrise: LocalTime, sunset: LocalTime): InauspiciousPeriods {
        val dayMinutes = minutesBetween(sunrise, sunset)
        require(dayMinutes > 0) { "Invalid sunrise/sunset for inauspicious periods: sunrise=$sunrise sunset=$sunset" }
        val durations = splitDurations(dayMinutes, 8)
        val boundaries = mutableListOf(sunrise)
        durations.forEach { boundaries.add(boundaries.last().plusMinutes(it)) }
        val rPos = mapOf(Vara.SUNDAY to 8, Vara.MONDAY to 2, Vara.TUESDAY to 7, Vara.WEDNESDAY to 5, Vara.THURSDAY to 6, Vara.FRIDAY to 4, Vara.SATURDAY to 3)[vara] ?: 1
        val yPos = mapOf(Vara.SUNDAY to 5, Vara.MONDAY to 4, Vara.TUESDAY to 3, Vara.WEDNESDAY to 2, Vara.THURSDAY to 1, Vara.FRIDAY to 7, Vara.SATURDAY to 6)[vara] ?: 1
        val gPos = mapOf(Vara.SUNDAY to 7, Vara.MONDAY to 6, Vara.TUESDAY to 5, Vara.WEDNESDAY to 4, Vara.THURSDAY to 3, Vara.FRIDAY to 2, Vara.SATURDAY to 1)[vara] ?: 1
        return InauspiciousPeriods(
            TimePeriod(boundaries[rPos - 1], boundaries[rPos], "Rahukala"),
            TimePeriod(boundaries[yPos - 1], boundaries[yPos], "Yamaghanta"),
            TimePeriod(boundaries[gPos - 1], boundaries[gPos], "Gulika Kala"),
            calculateDurmuhurtas(vara, sunrise, sunset)
        )
    }

    private fun calculateDurmuhurtas(vara: Vara, sunrise: LocalTime, sunset: LocalTime): List<TimePeriod> {
        val dayMinutes = minutesBetween(sunrise, sunset)
        require(dayMinutes > 0) { "Invalid sunrise/sunset for durmuhurta: sunrise=$sunrise sunset=$sunset" }
        val durations = splitDurations(dayMinutes, DAY_MUHURTAS)
        val boundaries = mutableListOf(sunrise)
        durations.forEach { boundaries.add(boundaries.last().plusMinutes(it)) }
        val pos = when (vara) { Vara.SUNDAY -> listOf(14); Vara.MONDAY -> listOf(10, 14); Vara.TUESDAY -> listOf(4, 11); Vara.WEDNESDAY -> listOf(8, 13); Vara.THURSDAY -> listOf(7, 12); Vara.FRIDAY -> listOf(6, 11); Vara.SATURDAY -> listOf(1, 2) }
        return pos.map { p -> TimePeriod(boundaries[p - 1], boundaries[p], "Durmuhurta") }
    }

    fun calculateAbhijitMuhurta(sunrise: LocalTime, sunset: LocalTime, currentTime: LocalTime): AbhijitMuhurta {
        val dayMinutes = minutesBetween(sunrise, sunset)
        require(dayMinutes > 0) { "Invalid sunrise/sunset for Abhijit Muhurta: sunrise=$sunrise sunset=$sunset" }
        val durations = splitDurations(dayMinutes, DAY_MUHURTAS)
        val boundaries = mutableListOf(sunrise)
        durations.forEach { boundaries.add(boundaries.last().plusMinutes(it)) }
        val start = boundaries[7]
        val end = boundaries[8]
        return AbhijitMuhurta(start, end, containsTime(start, end, currentTime))
    }
}
