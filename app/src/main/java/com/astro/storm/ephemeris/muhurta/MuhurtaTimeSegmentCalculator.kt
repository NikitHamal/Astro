package com.astro.storm.ephemeris.muhurta

import com.astro.storm.core.model.Planet
import com.astro.storm.ephemeris.muhurta.MuhurtaConstants.CHALDEAN_ORDER
import com.astro.storm.ephemeris.muhurta.MuhurtaConstants.DAY_CHOGHADIYAS
import com.astro.storm.ephemeris.muhurta.MuhurtaConstants.DAY_HORAS
import com.astro.storm.ephemeris.muhurta.MuhurtaConstants.DAY_MUHURTAS
import com.astro.storm.ephemeris.muhurta.MuhurtaConstants.NIGHT_CHOGHADIYAS
import com.astro.storm.ephemeris.muhurta.MuhurtaConstants.NIGHT_HORAS
import java.time.LocalTime
import java.time.temporal.ChronoUnit

object MuhurtaTimeSegmentCalculator {

    fun calculateChoghadiya(time: LocalTime, vara: Vara, sunrise: LocalTime, sunset: LocalTime): ChoghadiyaInfo {
        val isDay = !time.isBefore(sunrise) && time.isBefore(sunset)
        return if (isDay) {
            val dayChoghadiyas = calculateAllDayChoghadiya(vara, sunrise, sunset)
            dayChoghadiyas.find { time >= it.startTime && time < it.endTime } ?: dayChoghadiyas.first()
        } else {
            val nightChoghadiyas = calculateAllNightChoghadiya(vara, sunset, sunrise.plusHours(24 - ChronoUnit.HOURS.between(sunrise, sunset)))
            nightChoghadiyas.find { if (time >= sunset) time >= it.startTime && time < it.endTime else it.startTime > sunset || (time >= it.startTime && time < it.endTime) } ?: nightChoghadiyas.first()
        }
    }

    fun calculateAllDayChoghadiya(vara: Vara, sunrise: LocalTime, sunset: LocalTime): List<ChoghadiyaInfo> {
        val dur = ChronoUnit.MINUTES.between(sunrise, sunset) / DAY_CHOGHADIYAS
        val seq = mapOf(
            Vara.SUNDAY to listOf(Choghadiya.UDVEG, Choghadiya.CHAR, Choghadiya.LABH, Choghadiya.AMRIT, Choghadiya.KAAL, Choghadiya.SHUBH, Choghadiya.ROG, Choghadiya.UDVEG),
            Vara.MONDAY to listOf(Choghadiya.AMRIT, Choghadiya.KAAL, Choghadiya.SHUBH, Choghadiya.ROG, Choghadiya.UDVEG, Choghadiya.CHAR, Choghadiya.LABH, Choghadiya.AMRIT),
            Vara.TUESDAY to listOf(Choghadiya.ROG, Choghadiya.UDVEG, Choghadiya.CHAR, Choghadiya.LABH, Choghadiya.AMRIT, Choghadiya.KAAL, Choghadiya.SHUBH, Choghadiya.ROG),
            Vara.WEDNESDAY to listOf(Choghadiya.LABH, Choghadiya.AMRIT, Choghadiya.KAAL, Choghadiya.SHUBH, Choghadiya.ROG, Choghadiya.UDVEG, Choghadiya.CHAR, Choghadiya.LABH),
            Vara.THURSDAY to listOf(Choghadiya.SHUBH, Choghadiya.ROG, Choghadiya.UDVEG, Choghadiya.CHAR, Choghadiya.LABH, Choghadiya.AMRIT, Choghadiya.KAAL, Choghadiya.SHUBH),
            Vara.FRIDAY to listOf(Choghadiya.CHAR, Choghadiya.LABH, Choghadiya.AMRIT, Choghadiya.KAAL, Choghadiya.SHUBH, Choghadiya.ROG, Choghadiya.UDVEG, Choghadiya.CHAR),
            Vara.SATURDAY to listOf(Choghadiya.KAAL, Choghadiya.SHUBH, Choghadiya.ROG, Choghadiya.UDVEG, Choghadiya.CHAR, Choghadiya.LABH, Choghadiya.AMRIT, Choghadiya.KAAL)
        )[vara] ?: emptyList()
        return seq.mapIndexed { i, c -> ChoghadiyaInfo(c, sunrise.plusMinutes(i * dur), sunrise.plusMinutes((i + 1) * dur), true) }
    }

    fun calculateAllNightChoghadiya(vara: Vara, sunset: LocalTime, nextSunrise: LocalTime): List<ChoghadiyaInfo> {
        val min = if (nextSunrise.isAfter(sunset)) ChronoUnit.MINUTES.between(sunset, nextSunrise) else ChronoUnit.MINUTES.between(sunset, LocalTime.MAX) + ChronoUnit.MINUTES.between(LocalTime.MIN, nextSunrise) + 1
        val dur = min / NIGHT_CHOGHADIYAS
        val seq = mapOf(
            Vara.SUNDAY to listOf(Choghadiya.SHUBH, Choghadiya.AMRIT, Choghadiya.CHAR, Choghadiya.ROG, Choghadiya.KAAL, Choghadiya.LABH, Choghadiya.UDVEG, Choghadiya.SHUBH),
            Vara.MONDAY to listOf(Choghadiya.CHAR, Choghadiya.ROG, Choghadiya.KAAL, Choghadiya.LABH, Choghadiya.UDVEG, Choghadiya.SHUBH, Choghadiya.AMRIT, Choghadiya.CHAR),
            Vara.TUESDAY to listOf(Choghadiya.KAAL, Choghadiya.LABH, Choghadiya.UDVEG, Choghadiya.SHUBH, Choghadiya.AMRIT, Choghadiya.CHAR, Choghadiya.ROG, Choghadiya.KAAL),
            Vara.WEDNESDAY to listOf(Choghadiya.UDVEG, Choghadiya.SHUBH, Choghadiya.AMRIT, Choghadiya.CHAR, Choghadiya.ROG, Choghadiya.KAAL, Choghadiya.LABH, Choghadiya.UDVEG),
            Vara.THURSDAY to listOf(Choghadiya.AMRIT, Choghadiya.CHAR, Choghadiya.ROG, Choghadiya.KAAL, Choghadiya.LABH, Choghadiya.UDVEG, Choghadiya.SHUBH, Choghadiya.AMRIT),
            Vara.FRIDAY to listOf(Choghadiya.ROG, Choghadiya.KAAL, Choghadiya.LABH, Choghadiya.UDVEG, Choghadiya.SHUBH, Choghadiya.AMRIT, Choghadiya.CHAR, Choghadiya.ROG),
            Vara.SATURDAY to listOf(Choghadiya.LABH, Choghadiya.UDVEG, Choghadiya.SHUBH, Choghadiya.AMRIT, Choghadiya.CHAR, Choghadiya.ROG, Choghadiya.KAAL, Choghadiya.LABH)
        )[vara] ?: emptyList()
        return seq.mapIndexed { i, c ->
            var start = sunset.plusMinutes(i * dur); var end = sunset.plusMinutes((i + 1) * dur)
            if (start.isBefore(sunset) && i > 0) start = start.plusHours(24)
            if (end.isBefore(start) || end.isBefore(sunset)) end = end.plusHours(24)
            ChoghadiyaInfo(c, start, end, false)
        }
    }

    fun calculateHora(time: LocalTime, vara: Vara, sunrise: LocalTime, sunset: LocalTime): Hora {
        val isDay = !time.isBefore(sunrise) && time.isBefore(sunset)
        val dayMin = ChronoUnit.MINUTES.between(sunrise, sunset); val nightMin = 24 * 60 - dayMin
        val dDur = dayMin / DAY_HORAS; val nDur = nightMin / NIGHT_HORAS
        val num: Int; val start: LocalTime; val end: LocalTime
        if (isDay) { val m = ChronoUnit.MINUTES.between(sunrise, time); num = (m / dDur).toInt().coerceIn(0, DAY_HORAS - 1); start = sunrise.plusMinutes(num * dDur); end = sunrise.plusMinutes((num + 1) * dDur) }
        else { val m = if (time >= sunset) ChronoUnit.MINUTES.between(sunset, time) else ChronoUnit.MINUTES.between(sunset, LocalTime.MAX) + ChronoUnit.MINUTES.between(LocalTime.MIN, time) + 1; num = DAY_HORAS + (m / nDur).toInt().coerceIn(0, NIGHT_HORAS - 1); start = sunset.plusMinutes((num - DAY_HORAS) * nDur); end = sunset.plusMinutes((num - DAY_HORAS + 1) * nDur) }
        val lord = CHALDEAN_ORDER[(CHALDEAN_ORDER.indexOf(vara.lord) + num) % 7]
        return Hora(lord, num + 1, start, end, isDay, when (lord) { Planet.JUPITER, Planet.VENUS, Planet.MOON -> HoraNature.BENEFIC; Planet.MERCURY -> HoraNature.NEUTRAL; else -> HoraNature.MALEFIC })
    }

    fun calculateAllHoras(vara: Vara, sunrise: LocalTime, sunset: LocalTime, nextSunrise: LocalTime): List<Hora> {
        val res = mutableListOf<Hora>()
        val dayMin = ChronoUnit.MINUTES.between(sunrise, sunset); val nightMin = if (nextSunrise.isAfter(sunset)) ChronoUnit.MINUTES.between(sunset, nextSunrise) else ChronoUnit.MINUTES.between(sunset, LocalTime.MAX) + ChronoUnit.MINUTES.between(LocalTime.MIN, nextSunrise) + 1
        val dDur = dayMin / DAY_HORAS; val nDur = nightMin / NIGHT_HORAS
        val startIdx = CHALDEAN_ORDER.indexOf(vara.lord)
        for (i in 0 until DAY_HORAS) { val l = CHALDEAN_ORDER[(startIdx + i) % 7]; res.add(Hora(l, i + 1, sunrise.plusMinutes(i * dDur), sunrise.plusMinutes((i + 1) * dDur), true, when (l) { Planet.JUPITER, Planet.VENUS, Planet.MOON -> HoraNature.BENEFIC; Planet.MERCURY -> HoraNature.NEUTRAL; else -> HoraNature.MALEFIC })) }
        for (i in 0 until NIGHT_HORAS) { val l = CHALDEAN_ORDER[(startIdx + DAY_HORAS + i) % 7]; res.add(Hora(l, DAY_HORAS + i + 1, sunset.plusMinutes(i * nDur), sunset.plusMinutes((i + 1) * nDur), false, when (l) { Planet.JUPITER, Planet.VENUS, Planet.MOON -> HoraNature.BENEFIC; Planet.MERCURY -> HoraNature.NEUTRAL; else -> HoraNature.MALEFIC })) }
        return res
    }

    fun calculateInauspiciousPeriods(vara: Vara, sunrise: LocalTime, sunset: LocalTime): InauspiciousPeriods {
        val dur = ChronoUnit.MINUTES.between(sunrise, sunset) / 8
        val rPos = mapOf(Vara.SUNDAY to 8, Vara.MONDAY to 2, Vara.TUESDAY to 7, Vara.WEDNESDAY to 5, Vara.THURSDAY to 6, Vara.FRIDAY to 4, Vara.SATURDAY to 3)[vara] ?: 1
        val yPos = mapOf(Vara.SUNDAY to 5, Vara.MONDAY to 4, Vara.TUESDAY to 3, Vara.WEDNESDAY to 2, Vara.THURSDAY to 1, Vara.FRIDAY to 7, Vara.SATURDAY to 6)[vara] ?: 1
        val gPos = mapOf(Vara.SUNDAY to 7, Vara.MONDAY to 6, Vara.TUESDAY to 5, Vara.WEDNESDAY to 4, Vara.THURSDAY to 3, Vara.FRIDAY to 2, Vara.SATURDAY to 1)[vara] ?: 1
        return InauspiciousPeriods(TimePeriod(sunrise.plusMinutes((rPos - 1) * dur), sunrise.plusMinutes(rPos * dur), "Rahukala"), TimePeriod(sunrise.plusMinutes((yPos - 1) * dur), sunrise.plusMinutes(yPos * dur), "Yamaghanta"), TimePeriod(sunrise.plusMinutes((gPos - 1) * dur), sunrise.plusMinutes(gPos * dur), "Gulika Kala"), calculateDurmuhurtas(vara, sunrise, sunset))
    }

    private fun calculateDurmuhurtas(vara: Vara, sunrise: LocalTime, sunset: LocalTime): List<TimePeriod> {
        val dur = ChronoUnit.MINUTES.between(sunrise, sunset) / DAY_MUHURTAS
        val pos = when (vara) { Vara.SUNDAY -> listOf(14); Vara.MONDAY -> listOf(10, 14); Vara.TUESDAY -> listOf(4, 11); Vara.WEDNESDAY -> listOf(8, 13); Vara.THURSDAY -> listOf(7, 12); Vara.FRIDAY -> listOf(6, 11); Vara.SATURDAY -> listOf(1, 2) }
        return pos.map { p -> TimePeriod(sunrise.plusMinutes((p - 1) * dur), sunrise.plusMinutes(p * dur), "Durmuhurta") }
    }

    fun calculateAbhijitMuhurta(sunrise: LocalTime, sunset: LocalTime, currentTime: LocalTime): AbhijitMuhurta {
        val dur = ChronoUnit.MINUTES.between(sunrise, sunset) / DAY_MUHURTAS
        val start = sunrise.plusMinutes(7 * dur); val end = sunrise.plusMinutes(8 * dur)
        return AbhijitMuhurta(start, end, currentTime >= start && currentTime < end)
    }
}
