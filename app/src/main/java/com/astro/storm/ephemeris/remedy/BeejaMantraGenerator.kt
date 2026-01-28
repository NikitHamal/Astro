package com.astro.storm.ephemeris.remedy

import com.astro.storm.core.common.Language
import com.astro.storm.core.common.StringKeyRemedy
import com.astro.storm.core.common.StringResources
import com.astro.storm.core.model.Nakshatra
import com.astro.storm.core.model.Planet
import com.astro.storm.core.model.VedicChart
import com.astro.storm.ephemeris.AstrologicalConstants
import com.astro.storm.ephemeris.DivisionalChartCalculator
import kotlin.math.roundToInt

object BeejaMantraGenerator {

    data class BeejaMantraProfile(
        val nakshatra: Nakshatra,
        val pada: Int,
        val akshara: String,
        val baseBeeja: String,
        val mantra: String,
        val mantraSanskrit: String,
        val rationale: String
    )

    fun generate(chart: VedicChart, ishtaPlanet: Planet, language: Language): BeejaMantraProfile {
        val moon = chart.planetPositions.firstOrNull { it.planet == Planet.MOON }
            ?: throw IllegalStateException("Moon position required for Beeja Mantra")

        val (nakshatra, pada) = Nakshatra.fromLongitude(moon.longitude)
        val akshara = NAKSHATRA_AKSHARAS[nakshatra]?.getOrNull(pada - 1) ?: ""
        val baseBeeja = getBeejaForAkshara(akshara)

        val planetMantra = RemedyConstants.planetaryMantras[ishtaPlanet]
        val seed = planetMantra?.beejMantra ?: baseBeeja

        val dignityPrefix = getDignityPrefix(ishtaPlanet, chart)
        val degreeSuffix = getDegreeSuffix(ishtaPlanet, chart)

        val mantra = "Om $dignityPrefix $akshara $seed $degreeSuffix"
            .replace("  ", " ")
            .trim()

        val mantraSanskrit = "ॐ $dignityPrefix $akshara $seed $degreeSuffix"
            .replace("  ", " ")
            .trim()

        val rationale = StringResources.get(
            StringKeyRemedy.BEEJA_RATIONALE,
            language,
            nakshatra.displayName,
            pada,
            akshara,
            ishtaPlanet.displayName
        )

        return BeejaMantraProfile(
            nakshatra = nakshatra,
            pada = pada,
            akshara = akshara,
            baseBeeja = baseBeeja,
            mantra = mantra,
            mantraSanskrit = mantraSanskrit,
            rationale = rationale
        )
    }

    private fun getBeejaForAkshara(akshara: String): String {
        val key = akshara.lowercase()
        return when {
            key.startsWith("a") || key.startsWith("e") || key.startsWith("i") || key.startsWith("o") -> "Aim"
            key.startsWith("k") || key.startsWith("g") || key.startsWith("h") -> "Kleem"
            key.startsWith("c") || key.startsWith("j") -> "Hreem"
            key.startsWith("t") || key.startsWith("d") -> "Shreem"
            key.startsWith("p") || key.startsWith("b") || key.startsWith("m") -> "Ram"
            key.startsWith("y") || key.startsWith("r") || key.startsWith("l") || key.startsWith("v") -> "Yam"
            else -> "Lam"
        }
    }

    private fun getDignityPrefix(planet: Planet, chart: VedicChart): String {
        val d9 = DivisionalChartCalculator.calculateNavamsa(chart)
        val position = d9.planetPositions.firstOrNull { it.planet == planet } ?: return "Aim"
        return when {
            AstrologicalConstants.isExalted(planet, position.sign) -> "Shreem"
            AstrologicalConstants.isInOwnSign(planet, position.sign) -> "Hreem"
            AstrologicalConstants.isDebilitated(planet, position.sign) -> "Hum"
            AstrologicalConstants.isInMooltrikona(planet, position.sign, position.longitude % 30.0) -> "Kleem"
            else -> "Aim"
        }
    }

    private fun getDegreeSuffix(planet: Planet, chart: VedicChart): String {
        val d9 = DivisionalChartCalculator.calculateNavamsa(chart)
        val position = d9.planetPositions.firstOrNull { it.planet == planet } ?: return "Namah"
        val degreeInSign = (position.longitude % 30.0).coerceIn(0.0, 30.0)
        val segment = (degreeInSign / 10.0).roundToInt().coerceIn(0, 2)
        return when (segment) {
            0 -> "Namah"
            1 -> "Svaha"
            else -> "Hum"
        }
    }

    private val NAKSHATRA_AKSHARAS: Map<Nakshatra, List<String>> = mapOf(
        Nakshatra.ASHWINI to listOf("Chu", "Che", "Cho", "La"),
        Nakshatra.BHARANI to listOf("Li", "Lu", "Le", "Lo"),
        Nakshatra.KRITTIKA to listOf("A", "E", "U", "A"),
        Nakshatra.ROHINI to listOf("O", "Va", "Vi", "Vu"),
        Nakshatra.MRIGASHIRA to listOf("Ve", "Vo", "Ka", "Ki"),
        Nakshatra.ARDRA to listOf("Ku", "Gha", "Na", "Cha"),
        Nakshatra.PUNARVASU to listOf("Ke", "Ko", "Ha", "Hi"),
        Nakshatra.PUSHYA to listOf("Hu", "He", "Ho", "Da"),
        Nakshatra.ASHLESHA to listOf("Di", "Du", "De", "Do"),
        Nakshatra.MAGHA to listOf("Ma", "Mi", "Mu", "Me"),
        Nakshatra.PURVA_PHALGUNI to listOf("Mo", "Ta", "Ti", "Tu"),
        Nakshatra.UTTARA_PHALGUNI to listOf("Te", "To", "Pa", "Pi"),
        Nakshatra.HASTA to listOf("Pu", "Sha", "Na", "Tha"),
        Nakshatra.CHITRA to listOf("Pe", "Po", "Ra", "Ri"),
        Nakshatra.SWATI to listOf("Ru", "Re", "Ro", "Ta"),
        Nakshatra.VISHAKHA to listOf("Ti", "Tu", "Te", "To"),
        Nakshatra.ANURADHA to listOf("Na", "Ni", "Nu", "Ne"),
        Nakshatra.JYESHTHA to listOf("No", "Ya", "Yi", "Yu"),
        Nakshatra.MULA to listOf("Ye", "Yo", "Bha", "Bhi"),
        Nakshatra.PURVA_ASHADHA to listOf("Bhu", "Dha", "Pha", "Dha"),
        Nakshatra.UTTARA_ASHADHA to listOf("Bhe", "Bho", "Ja", "Ji"),
        Nakshatra.SHRAVANA to listOf("Ju", "Je", "Jo", "Gha"),
        Nakshatra.DHANISHTHA to listOf("Ga", "Gi", "Gu", "Ge"),
        Nakshatra.SHATABHISHA to listOf("Go", "Sa", "Si", "Su"),
        Nakshatra.PURVA_BHADRAPADA to listOf("Se", "So", "Da", "Di"),
        Nakshatra.UTTARA_BHADRAPADA to listOf("Du", "Tha", "Jha", "Na"),
        Nakshatra.REVATI to listOf("De", "Do", "Cha", "Chi")
    )
}
