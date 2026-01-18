package com.astro.storm.ephemeris.muhurta

import com.astro.storm.core.common.Language
import com.astro.storm.data.localization.stringResources
import com.astro.storm.core.model.Nakshatra
import java.time.LocalTime

object MuhurtaEvaluator {

    fun calculateSpecialYogas(vara: Vara, tithi: TithiInfo, nakshatra: NakshatraInfo): List<SpecialYoga> {
        val res = mutableListOf<SpecialYoga>()
        if (isAmritaSiddhiYoga(vara, tithi, nakshatra.nakshatra)) res.add(SpecialYoga("Amrita Siddhi Yoga", "Extremely auspicious combination for all activities", true))
        if (isSarvarthaSiddhiYoga(vara, tithi, nakshatra.nakshatra)) res.add(SpecialYoga("Sarvartha Siddhi Yoga", "Success in all undertakings", true))
        if (vara == Vara.SUNDAY && nakshatra.nakshatra == Nakshatra.PUSHYA) res.add(SpecialYoga("Ravi Pushya Yoga", "Highly auspicious for purchases and new ventures", true))
        if (vara == Vara.THURSDAY && nakshatra.nakshatra == Nakshatra.PUSHYA) res.add(SpecialYoga("Guru Pushya Yoga", "Excellent for education, spirituality, and investments", true))
        if (mapOf(Vara.SUNDAY to 12, Vara.MONDAY to 11, Vara.TUESDAY to 5, Vara.WEDNESDAY to 3, Vara.THURSDAY to 6, Vara.FRIDAY to 8, Vara.SATURDAY to 9)[vara] == tithi.displayNumber) res.add(SpecialYoga("Dagdha Tithi", "Burnt tithi - avoid auspicious activities", false))
        return res
    }

    private fun isAmritaSiddhiYoga(v: Vara, t: TithiInfo, n: Nakshatra): Boolean {
        val c = mapOf(Vara.SUNDAY to Pair(listOf(Nakshatra.HASTA, Nakshatra.MULA, Nakshatra.UTTARA_ASHADHA), listOf(2, 7, 12)), Vara.MONDAY to Pair(listOf(Nakshatra.MRIGASHIRA, Nakshatra.SHRAVANA, Nakshatra.ROHINI), listOf(2, 7, 12)), Vara.TUESDAY to Pair(listOf(Nakshatra.ASHWINI, Nakshatra.UTTARA_PHALGUNI), listOf(3, 8, 13)), Vara.WEDNESDAY to Pair(listOf(Nakshatra.ANURADHA, Nakshatra.REVATI), listOf(2, 7, 12)), Vara.THURSDAY to Pair(listOf(Nakshatra.PUNARVASU, Nakshatra.PUSHYA, Nakshatra.ASHWINI), listOf(5, 10, 15)), Vara.FRIDAY to Pair(listOf(Nakshatra.REVATI, Nakshatra.ANURADHA, Nakshatra.SWATI), listOf(1, 6, 11)), Vara.SATURDAY to Pair(listOf(Nakshatra.ROHINI, Nakshatra.SWATI), listOf(3, 8, 13)))[v] ?: return false
        return n in c.first && t.displayNumber in c.second
    }

    private fun isSarvarthaSiddhiYoga(v: Vara, t: TithiInfo, n: Nakshatra): Boolean {
        val c = mapOf(Vara.SUNDAY to listOf(Nakshatra.PUSHYA, Nakshatra.HASTA, Nakshatra.UTTARA_BHADRAPADA, Nakshatra.UTTARA_ASHADHA, Nakshatra.UTTARA_PHALGUNI, Nakshatra.MULA, Nakshatra.ASHWINI), Vara.MONDAY to listOf(Nakshatra.ROHINI, Nakshatra.MRIGASHIRA, Nakshatra.PUSHYA, Nakshatra.ANURADHA, Nakshatra.SHRAVANA), Vara.TUESDAY to listOf(Nakshatra.ASHWINI, Nakshatra.UTTARA_PHALGUNI, Nakshatra.KRITTIKA, Nakshatra.CHITRA), Vara.WEDNESDAY to listOf(Nakshatra.ROHINI, Nakshatra.ANURADHA, Nakshatra.HASTA, Nakshatra.KRITTIKA), Vara.THURSDAY to listOf(Nakshatra.ASHWINI, Nakshatra.PUNARVASU, Nakshatra.PUSHYA, Nakshatra.SWATI, Nakshatra.REVATI), Vara.FRIDAY to listOf(Nakshatra.ASHWINI, Nakshatra.PUNARVASU, Nakshatra.ANURADHA, Nakshatra.REVATI, Nakshatra.SHRAVANA), Vara.SATURDAY to listOf(Nakshatra.ROHINI, Nakshatra.SWATI, Nakshatra.SHRAVANA))[v] ?: return false
        return n in c && t.isAuspicious
    }

    fun evaluateMuhurta(v: Vara, t: TithiInfo, n: NakshatraInfo, y: YogaInfo, k: KaranaInfo, c: ChoghadiyaInfo, h: Hora, time: LocalTime, ip: InauspiciousPeriods, am: AbhijitMuhurta, sy: List<SpecialYoga>, lang: Language): Quadruple<Int, List<ActivityType>, List<ActivityType>, List<String>> {
        var s = 50; val rec = mutableListOf<String>(); val suit = mutableListOf<ActivityType>(); val avoid = mutableListOf<ActivityType>()
        when (v) { Vara.MONDAY, Vara.WEDNESDAY, Vara.THURSDAY, Vara.FRIDAY -> s += 10; Vara.TUESDAY, Vara.SATURDAY -> { s -= 5; rec.add("${v.getLocalizedName(lang)} requires caution") }; Vara.SUNDAY -> s += 5 }
        if (t.isAuspicious) { s += 15; if (t.nature == TithiNature.PURNA) { s += 5; rec.add("Purna Tithi - excellent") } } else { s -= 10; rec.add("${t.name} is not ideal"); if (t.nature == TithiNature.RIKTA) { s -= 5; rec.add("Rikta Tithi - avoid finance") } }
        val ga = ActivityType.GENERAL
        if (n.nakshatra in ga.favorableNakshatras) { s += 20; rec.add("${n.nakshatra.getLocalizedName(lang)} is auspicious") } else if (n.nakshatra in ga.avoidNakshatras) { s -= 15; rec.add("${n.nakshatra.getLocalizedName(lang)} requires caution") } else s += 5
        when (n.nature) { NakshatraNature.DHRUVA -> { s += 5; rec.add("Fixed - good for permanent") }; NakshatraNature.KSHIPRA -> { s += 3; rec.add("Swift - good for quick") }; NakshatraNature.MRIDU -> { s += 3; rec.add("Soft - good for gentle") }; else -> if (n.nature == NakshatraNature.TIKSHNA || n.nature == NakshatraNature.UGRA) s -= 5 }
        if (y.isAuspicious) s += 10 else { s -= 10; rec.add("${y.name} yoga is inauspicious") }
        if (k.isAuspicious) s += 5 else { s -= 8; if (k.name == "Vishti") rec.add("Vishti (Bhadra) Karana - avoid") }
        s += when (c.choghadiya.nature) { ChoghadiyaNature.EXCELLENT -> { rec.add("${c.choghadiya.getLocalizedName(lang)} Choghadiya - excellent"); 15 }; ChoghadiyaNature.VERY_GOOD -> 10; ChoghadiyaNature.GOOD -> 5; ChoghadiyaNature.NEUTRAL -> 0; else -> { rec.add("${c.choghadiya.getLocalizedName(lang)} Choghadiya - inauspicious"); -10 } }
        s += when (h.nature) { HoraNature.BENEFIC -> { rec.add("${h.lord.getLocalizedName(lang)} Hora - benefic"); 10 }; HoraNature.MALEFIC -> -8; else -> 2 }
        if (am.isActive) { s += 15; rec.add("Abhijit Muhurta active") }
        for (yoga in sy) if (yoga.isAuspicious) { s += 15; rec.add("${yoga.name}: ${yoga.description}") } else { s -= 15; rec.add("Warning: ${yoga.name}") }
        if (ip.rahukala.contains(time)) { s -= 25; rec.add("Rahukala - avoid") }
        if (ip.yamaghanta.contains(time)) { s -= 15; rec.add("Yamaghanta - avoid travel") }
        if (ip.gulikaKala.contains(time)) { s -= 10; rec.add("Gulika Kala - caution") }
        for (d in ip.durmuhurtas) if (d.contains(time)) { s -= 12; rec.add("Durmuhurta - avoid"); break }
        ActivityType.entries.forEach { a ->
            var ac = 0; var av = false
            if (n.nakshatra in a.favorableNakshatras) ac += 3; if (n.nakshatra in a.avoidNakshatras) { ac -= 5; av = true }
            if (v in a.favorableVaras) ac += 2; if (t.displayNumber in a.favorableTithis) ac += 2; if (t.isAuspicious) ac += 1
            if (ac >= 5 && !av && s >= 50) suit.add(a) else if (av || ac <= -2) avoid.add(a)
        }
        return Quadruple(s.coerceIn(0, 100), suit.distinct(), avoid.distinct(), rec.distinct())
    }

    fun evaluateForActivity(m: MuhurtaDetails, a: ActivityType, lang: Language): Triple<Int, List<String>, List<String>> {
        var s = 50; val r = mutableListOf<String>(); val w = mutableListOf<String>()
        if (m.nakshatra.nakshatra in a.favorableNakshatras) { s += 20; r.add("Excellent Nakshatra: ${m.nakshatra.nakshatra.getLocalizedName(lang)}") } else if (m.nakshatra.nakshatra in a.avoidNakshatras) { s -= 25; w.add("Unfavorable Nakshatra: ${m.nakshatra.nakshatra.getLocalizedName(lang)}") }
        if (m.vara in a.favorableVaras) { s += 15; r.add("Favorable day: ${m.vara.getLocalizedName(lang)}") } else if (m.vara == Vara.TUESDAY || m.vara == Vara.SATURDAY) if (a != ActivityType.MEDICAL) { s -= 5; w.add("${m.vara.getLocalizedName(lang)} is not ideal") }
        if (m.tithi.displayNumber in a.favorableTithis) { s += 12; r.add("Favorable Tithi: ${m.tithi.name}") }
        if (!m.tithi.isAuspicious) { s -= 10; w.add("Tithi (${m.tithi.name}) may not be ideal") }
        if (m.tithi.number == 15 && a == ActivityType.SPIRITUAL) { s += 10; r.add("Purnima - spiritual") }
        when (m.choghadiya.choghadiya.nature) { ChoghadiyaNature.EXCELLENT, ChoghadiyaNature.VERY_GOOD -> { s += 10; r.add("Auspicious Choghadiya: ${m.choghadiya.choghadiya.getLocalizedName(lang)}") }; ChoghadiyaNature.INAUSPICIOUS -> { s -= 12; w.add("Inauspicious Choghadiya: ${m.choghadiya.choghadiya.getLocalizedName(lang)}") }; else -> {} }
        if (m.hora.nature == HoraNature.BENEFIC) { s += 5; r.add("Benefic Hora: ${m.hora.lord.getLocalizedName(lang)}") } else if (m.hora.nature == HoraNature.MALEFIC) s -= 5
        if (m.abhijitMuhurta.isActive) { s += 15; r.add("Abhijit Muhurta") }
        for (y in m.specialYogas) if (y.isAuspicious) { s += 15; r.add(y.name) } else { s -= 15; w.add(y.name) }
        val time = m.dateTime.toLocalTime()
        if (m.inauspiciousPeriods.rahukala.contains(time)) { s -= 30; w.add("Rahukala") }
        if (m.inauspiciousPeriods.yamaghanta.contains(time)) { s -= 15; w.add("Yamaghanta") }
        if (m.inauspiciousPeriods.gulikaKala.contains(time)) { s -= 10; w.add("Gulika Kala") }
        for (d in m.inauspiciousPeriods.durmuhurtas) if (d.contains(time)) { s -= 12; w.add("Durmuhurta"); break }
        if (!m.yoga.isAuspicious) { s -= 8; w.add("Inauspicious Yoga: ${m.yoga.name}") }
        if (!m.karana.isAuspicious) { s -= 8; w.add("Inauspicious Karana: ${m.karana.name}") }
        return Triple(s.coerceIn(0, 100), r.distinct(), w.distinct())
    }

    data class Quadruple<A, B, C, D>(val first: A, val second: B, val third: C, val fourth: D)
}

