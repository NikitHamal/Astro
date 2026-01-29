package com.astro.storm.ephemeris.remedy

import com.astro.storm.core.model.Nakshatra
import com.astro.storm.core.model.Planet
import com.astro.storm.core.model.PlanetPosition
import com.astro.storm.core.model.VedicChart
import com.astro.storm.core.model.ZodiacSign
import com.astro.storm.ephemeris.DivisionalChartCalculator
import com.astro.storm.ephemeris.DivisionalChartType
import com.astro.storm.ephemeris.VedicAstrologyUtils

/**
 * Calculates Ishta Devata using Atmakaraka in Navamsa (Karakamsa) and the 12th from Karakamsa.
 */
object IshtaDevataCalculator {

    data class IshtaDevataResult(
        val atmakaraka: Planet,
        val karakamsaSign: ZodiacSign,
        val twelfthFromKarakamsa: ZodiacSign,
        val ishtaDevataPlanet: Planet,
        val ishtaDevataName: String,
        val reasoning: String
    )

    private val ISHTA_DEVATA_MAP = mapOf(
        Planet.SUN to "Rama",
        Planet.MOON to "Krishna",
        Planet.MARS to "Narasimha",
        Planet.MERCURY to "Vishnu",
        Planet.JUPITER to "Vamana",
        Planet.VENUS to "Parashurama",
        Planet.SATURN to "Kurma",
        Planet.RAHU to "Varaha",
        Planet.KETU to "Matsya"
    )

    fun calculate(chart: VedicChart): IshtaDevataResult {
        val atmakaraka = calculateAtmakaraka(chart)
        val navamsa = DivisionalChartCalculator.calculateDivisionalChart(chart, DivisionalChartType.D9_NAVAMSA)
        val akPosition = navamsa.planetPositions.firstOrNull { it.planet == atmakaraka }
        val karakamsaSign = akPosition?.sign ?: ZodiacSign.fromLongitude(navamsa.ascendantLongitude)
        val twelfthFromKarakamsa = getTwelfthSign(karakamsaSign)

        val candidates = navamsa.planetPositions.filter { it.sign == twelfthFromKarakamsa }
        val ishtaDevataPlanet = when {
            candidates.isNotEmpty() -> {
                candidates.maxByOrNull { dignityScore(it) }?.planet ?: twelfthFromKarakamsa.ruler
            }
            else -> twelfthFromKarakamsa.ruler
        }

        val deityName = ISHTA_DEVATA_MAP[ishtaDevataPlanet] ?: "Vishnu"
        val reasoning = buildString {
            append("Atmakaraka: ${atmakaraka.name}. ")
            append("Karakamsa sign in D9: ${karakamsaSign.displayName}. ")
            append("12th from Karakamsa: ${twelfthFromKarakamsa.displayName}. ")
            append("Ishta Devata planet: ${ishtaDevataPlanet.name} ($deityName).")
        }

        return IshtaDevataResult(
            atmakaraka = atmakaraka,
            karakamsaSign = karakamsaSign,
            twelfthFromKarakamsa = twelfthFromKarakamsa,
            ishtaDevataPlanet = ishtaDevataPlanet,
            ishtaDevataName = deityName,
            reasoning = reasoning
        )
    }

    private fun calculateAtmakaraka(chart: VedicChart): Planet {
        val eligible = chart.planetPositions.filter { it.planet != Planet.RAHU && it.planet != Planet.KETU }
        return eligible.maxByOrNull { it.longitude % 30 }?.planet ?: Planet.SUN
    }

    private fun getTwelfthSign(sign: ZodiacSign): ZodiacSign {
        val targetNumber = if (sign.number == 1) 12 else sign.number - 1
        return ZodiacSign.entries.first { it.number == targetNumber }
    }

    private fun dignityScore(position: PlanetPosition): Int {
        return when {
            VedicAstrologyUtils.isExalted(position) -> 5
            VedicAstrologyUtils.isInOwnSign(position.planet, position.sign) -> 4
            VedicAstrologyUtils.isInFriendSign(position) -> 3
            VedicAstrologyUtils.isInEnemySign(position) -> 1
            else -> 2
        }
    }
}

object BeejaMantraGenerator {

    data class BeejaMantraResult(
        val nakshatra: Nakshatra,
        val pada: Int,
        val akshara: String,
        val planet: Planet,
        val dignityTone: String,
        val mantra: String
    )

    private val NAKSHATRA_AKSHARA = mapOf(
        Nakshatra.ASHWINI to listOf("Chu", "Che", "Cho", "La"),
        Nakshatra.BHARANI to listOf("Li", "Lu", "Le", "Lo"),
        Nakshatra.KRITTIKA to listOf("A", "I", "U", "E"),
        Nakshatra.ROHINI to listOf("O", "Va", "Vi", "Vu"),
        Nakshatra.MRIGASHIRA to listOf("Ve", "Vo", "Ka", "Ki"),
        Nakshatra.ARDRA to listOf("Ku", "Gha", "Ng", "Chha"),
        Nakshatra.PUNARVASU to listOf("Ke", "Ko", "Ha", "Hi"),
        Nakshatra.PUSHYA to listOf("Hu", "He", "Ho", "Da"),
        Nakshatra.ASHLESHA to listOf("Di", "Du", "De", "Do"),
        Nakshatra.MAGHA to listOf("Ma", "Mi", "Mu", "Me"),
        Nakshatra.PURVA_PHALGUNI to listOf("Mo", "Ta", "Ti", "Tu"),
        Nakshatra.UTTARA_PHALGUNI to listOf("Te", "To", "Pa", "Pi"),
        Nakshatra.HASTA to listOf("Pu", "Sha", "Na", "Tha"),
        Nakshatra.CHITRA to listOf("Pe", "Po", "Ra", "Ri"),
        Nakshatra.SWATI to listOf("Ru", "Re", "Ro", "Taa"),
        Nakshatra.VISHAKHA to listOf("Ti", "Tu", "Te", "To"),
        Nakshatra.ANURADHA to listOf("Na", "Ni", "Nu", "Ne"),
        Nakshatra.JYESHTHA to listOf("No", "Ya", "Yi", "Yu"),
        Nakshatra.MULA to listOf("Ye", "Yo", "Ba", "Bi"),
        Nakshatra.PURVA_ASHADHA to listOf("Bu", "Dha", "Pha", "Dha"),
        Nakshatra.UTTARA_ASHADHA to listOf("Be", "Bo", "Ja", "Ji"),
        Nakshatra.SHRAVANA to listOf("Ju", "Je", "Jo", "Gha"),
        Nakshatra.DHANISHTHA to listOf("Ga", "Gi", "Gu", "Ge"),
        Nakshatra.SHATABHISHA to listOf("Go", "Sa", "Si", "Su"),
        Nakshatra.PURVA_BHADRAPADA to listOf("Se", "So", "Da", "Di"),
        Nakshatra.UTTARA_BHADRAPADA to listOf("Du", "Jham", "Jna", "Tha"),
        Nakshatra.REVATI to listOf("De", "Do", "Cha", "Chi")
    )

    fun generate(chart: VedicChart, planet: Planet): BeejaMantraResult {
        val moonPos = chart.planetPositions.firstOrNull { it.planet == Planet.MOON }
            ?: throw IllegalStateException("Moon position not found")
        val (nakshatra, pada) = Nakshatra.fromLongitude(moonPos.longitude)
        val aksharaList = NAKSHATRA_AKSHARA[nakshatra] ?: listOf("Om")
        val akshara = aksharaList.getOrNull(pada - 1) ?: aksharaList.first()

        val targetPos = chart.planetPositions.firstOrNull { it.planet == planet }
        val tone = getDignityTone(targetPos)
        val mantra = "Om $tone $akshara Namah"

        return BeejaMantraResult(
            nakshatra = nakshatra,
            pada = pada,
            akshara = akshara,
            planet = planet,
            dignityTone = tone,
            mantra = mantra
        )
    }

    private fun getDignityTone(position: PlanetPosition?): String {
        if (position == null) return "Hreem"
        return when {
            VedicAstrologyUtils.isExalted(position) -> "Shreem"
            VedicAstrologyUtils.isInOwnSign(position.planet, position.sign) -> "Shreem"
            VedicAstrologyUtils.isInFriendSign(position) -> "Hreem"
            VedicAstrologyUtils.isInEnemySign(position) -> "Kreem"
            VedicAstrologyUtils.isDebilitated(position) -> "Kreem"
            else -> "Aim"
        }
    }
}
