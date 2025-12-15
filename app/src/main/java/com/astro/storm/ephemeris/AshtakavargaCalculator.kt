package com.astro.storm.ephemeris

import com.astro.storm.data.localization.LocalizableString
import com.astro.storm.data.model.Planet
import com.astro.storm.data.model.VedicChart
import com.astro.storm.data.model.ZodiacSign

object AshtakavargaCalculator {

    private val ASHTAKAVARGA_PLANETS = listOf(
        Planet.SUN, Planet.MOON, Planet.MARS, Planet.MERCURY,
        Planet.JUPITER, Planet.VENUS, Planet.SATURN
    )
    private val KAKSHA_LORDS = listOf(
        Planet.SATURN, Planet.JUPITER, Planet.MARS, Planet.SUN,
        Planet.VENUS, Planet.MERCURY, Planet.MOON
    )
    private val KAKSHA_ASCENDANT = LocalizableString.Static("Ascendant")
    private const val KAKSHA_SIZE_DEGREES = 3.75

    private val SUN_BINDU_RULES = mapOf(
        Planet.SUN to listOf(1, 2, 4, 7, 8, 9, 10, 11),
        Planet.MOON to listOf(3, 6, 10, 11),
        Planet.MARS to listOf(1, 2, 4, 7, 8, 9, 10, 11),
        Planet.MERCURY to listOf(3, 5, 6, 9, 10, 11, 12),
        Planet.JUPITER to listOf(5, 6, 9, 11),
        Planet.VENUS to listOf(6, 7, 12),
        Planet.SATURN to listOf(1, 2, 4, 7, 8, 9, 10, 11)
    )
    private val SUN_ASCENDANT_BINDUS = listOf(3, 4, 6, 10, 11, 12)

    private val MOON_BINDU_RULES = mapOf(
        Planet.SUN to listOf(3, 6, 7, 8, 10, 11),
        Planet.MOON to listOf(1, 3, 6, 7, 10, 11),
        Planet.MARS to listOf(2, 3, 5, 6, 9, 10, 11),
        Planet.MERCURY to listOf(1, 3, 4, 5, 7, 8, 10, 11),
        Planet.JUPITER to listOf(1, 4, 7, 8, 10, 11, 12),
        Planet.VENUS to listOf(3, 4, 5, 7, 9, 10, 11),
        Planet.SATURN to listOf(3, 5, 6, 11)
    )
    private val MOON_ASCENDANT_BINDUS = listOf(3, 6, 10, 11)

    private val MARS_BINDU_RULES = mapOf(
        Planet.SUN to listOf(3, 5, 6, 10, 11),
        Planet.MOON to listOf(3, 6, 11),
        Planet.MARS to listOf(1, 2, 4, 7, 8, 10, 11),
        Planet.MERCURY to listOf(3, 5, 6, 11),
        Planet.JUPITER to listOf(6, 10, 11, 12),
        Planet.VENUS to listOf(6, 8, 11, 12),
        Planet.SATURN to listOf(1, 4, 7, 8, 9, 10, 11)
    )
    private val MARS_ASCENDANT_BINDUS = listOf(1, 3, 6, 10, 11)

    private val MERCURY_BINDU_RULES = mapOf(
        Planet.SUN to listOf(5, 6, 9, 11, 12),
        Planet.MOON to listOf(2, 4, 6, 8, 10, 11),
        Planet.MARS to listOf(1, 2, 4, 7, 8, 9, 10, 11),
        Planet.MERCURY to listOf(1, 3, 5, 6, 9, 10, 11, 12),
        Planet.JUPITER to listOf(6, 8, 11, 12),
        Planet.VENUS to listOf(1, 2, 3, 4, 5, 8, 9, 11),
        Planet.SATURN to listOf(1, 2, 4, 7, 8, 9, 10, 11)
    )
    private val MERCURY_ASCENDANT_BINDUS = listOf(1, 2, 4, 6, 8, 10, 11)

    private val JUPITER_BINDU_RULES = mapOf(
        Planet.SUN to listOf(1, 2, 3, 4, 7, 8, 9, 10, 11),
        Planet.MOON to listOf(2, 5, 7, 9, 11),
        Planet.MARS to listOf(1, 2, 4, 7, 8, 10, 11),
        Planet.MERCURY to listOf(1, 2, 4, 5, 6, 9, 10, 11),
        Planet.JUPITER to listOf(1, 2, 3, 4, 7, 8, 10, 11),
        Planet.VENUS to listOf(2, 5, 6, 9, 10, 11),
        Planet.SATURN to listOf(3, 5, 6, 12)
    )
    private val JUPITER_ASCENDANT_BINDUS = listOf(1, 2, 4, 5, 6, 7, 9, 10, 11)

    private val VENUS_BINDU_RULES = mapOf(
        Planet.SUN to listOf(8, 11, 12),
        Planet.MOON to listOf(1, 2, 3, 4, 5, 8, 9, 11, 12),
        Planet.MARS to listOf(3, 5, 6, 9, 11, 12),
        Planet.MERCURY to listOf(3, 5, 6, 9, 11),
        Planet.JUPITER to listOf(5, 8, 9, 10, 11),
        Planet.VENUS to listOf(1, 2, 3, 4, 5, 8, 9, 10, 11),
        Planet.SATURN to listOf(3, 4, 5, 8, 9, 10, 11)
    )
    private val VENUS_ASCENDANT_BINDUS = listOf(1, 2, 3, 4, 5, 8, 9, 11)

    private val SATURN_BINDU_RULES = mapOf(
        Planet.SUN to listOf(1, 2, 4, 7, 8, 10, 11),
        Planet.MOON to listOf(3, 6, 11),
        Planet.MARS to listOf(3, 5, 6, 10, 11, 12),
        Planet.MERCURY to listOf(6, 8, 9, 10, 11, 12),
        Planet.JUPITER to listOf(5, 6, 11, 12),
        Planet.VENUS to listOf(6, 11, 12),
        Planet.SATURN to listOf(3, 5, 6, 11)
    )
    private val SATURN_ASCENDANT_BINDUS = listOf(1, 3, 4, 6, 10, 11)

    data class AshtakavargaAnalysis(
        val chart: VedicChart,
        val bhinnashtakavarga: Map<Planet, Bhinnashtakavarga>,
        val sarvashtakavarga: Sarvashtakavarga,
        val prastarashtakavarga: Map<Planet, Prastarashtakavarga>,
        val timestamp: Long = System.currentTimeMillis()
    )

    data class Bhinnashtakavarga(
        val planet: Planet,
        val binduMatrix: Map<ZodiacSign, Int>,
        val contributorMatrix: Map<ZodiacSign, List<LocalizableString>>,
        val totalBindus: Int
    ) {
        fun getBindusForSign(sign: ZodiacSign): Int = binduMatrix[sign] ?: 0
    }

    data class Sarvashtakavarga(
        val binduMatrix: Map<ZodiacSign, Int>,
        val totalBindus: Int,
        val strongestSign: ZodiacSign,
        val weakestSign: ZodiacSign
    ) {
        fun getBindusForSign(sign: ZodiacSign): Int = binduMatrix[sign] ?: 0
    }

    data class Prastarashtakavarga(
        val planet: Planet,
        val contributionMatrix: Map<ZodiacSign, Map<LocalizableString, Boolean>>,
        val bindusByContributor: Map<LocalizableString, Int>
    )

    fun calculateAshtakavarga(chart: VedicChart): AshtakavargaAnalysis {
        val bhinnashtakavarga = mutableMapOf<Planet, Bhinnashtakavarga>()
        val prastarashtakavarga = mutableMapOf<Planet, Prastarashtakavarga>()

        ASHTAKAVARGA_PLANETS.forEach { planet ->
            val (bav, prastara) = calculateBhinnashtakavarga(planet, chart)
            bhinnashtakavarga[planet] = bav
            prastarashtakavarga[planet] = prastara
        }

        val sarvashtakavarga = calculateSarvashtakavarga(bhinnashtakavarga)

        return AshtakavargaAnalysis(
            chart = chart,
            bhinnashtakavarga = bhinnashtakavarga,
            sarvashtakavarga = sarvashtakavarga,
            prastarashtakavarga = prastarashtakavarga
        )
    }

    private fun calculateBhinnashtakavarga(planet: Planet, chart: VedicChart): Pair<Bhinnashtakavarga, Prastarashtakavarga> {
        val binduMatrix = mutableMapOf<ZodiacSign, Int>()
        val contributorMatrix = mutableMapOf<ZodiacSign, MutableList<LocalizableString>>()
        val prastaraMatrix = mutableMapOf<ZodiacSign, MutableMap<LocalizableString, Boolean>>()

        val binduRules = getBinduRulesForPlanet(planet)
        val ascendantBindus = getAscendantBindusForPlanet(planet)

        ZodiacSign.entries.forEach { sign ->
            binduMatrix[sign] = 0
            contributorMatrix[sign] = mutableListOf()
            prastaraMatrix[sign] = mutableMapOf()
        }

        ASHTAKAVARGA_PLANETS.forEach { contributor ->
            val contributorPosition = chart.planetPositions.find { it.planet == contributor } ?: return@forEach
            val contributorSignNumber = contributorPosition.sign.number
            val bindusFromThisPlanet = binduRules[contributor] ?: emptyList()

            bindusFromThisPlanet.forEach { houseFromContributor ->
                val targetSignIndex = (contributorSignNumber - 1 + houseFromContributor - 1) % 12
                val targetSign = ZodiacSign.entries[targetSignIndex]
                binduMatrix[targetSign] = binduMatrix.getOrDefault(targetSign, 0) + 1
                contributorMatrix[targetSign]?.add(contributor.displayName)
                prastaraMatrix[targetSign]?.set(contributor.displayName, true)
            }

            ZodiacSign.entries.forEach { sign ->
                if (prastaraMatrix[sign]?.get(contributor.displayName) != true) {
                    prastaraMatrix[sign]?.set(contributor.displayName, false)
                }
            }
        }

        val ascendantSign = ZodiacSign.fromLongitude(chart.ascendant)
        val ascendantSignNumber = ascendantSign.number
        ascendantBindus.forEach { houseFromAscendant ->
            val targetSignIndex = (ascendantSignNumber - 1 + houseFromAscendant - 1) % 12
            val targetSign = ZodiacSign.entries[targetSignIndex]
            binduMatrix[targetSign] = binduMatrix.getOrDefault(targetSign, 0) + 1
            contributorMatrix[targetSign]?.add(KAKSHA_ASCENDANT)
            prastaraMatrix[targetSign]?.set(KAKSHA_ASCENDANT, true)
        }

        ZodiacSign.entries.forEach { sign ->
            if (prastaraMatrix[sign]?.get(KAKSHA_ASCENDANT) != true) {
                prastaraMatrix[sign]?.set(KAKSHA_ASCENDANT, false)
            }
        }

        val totalBindus = binduMatrix.values.sum()
        val bav = Bhinnashtakavarga(planet, binduMatrix, contributorMatrix, totalBindus)

        val bindusByContributor = mutableMapOf<LocalizableString, Int>()
        ASHTAKAVARGA_PLANETS.forEach { contributor ->
            bindusByContributor[contributor.displayName] = prastaraMatrix.values.count { it[contributor.displayName] == true }
        }
        bindusByContributor[KAKSHA_ASCENDANT] = prastaraMatrix.values.count { it[KAKSHA_ASCENDANT] == true }

        val prastara = Prastarashtakavarga(planet, prastaraMatrix, bindusByContributor)
        return Pair(bav, prastara)
    }

    private fun calculateSarvashtakavarga(bhinnashtakavarga: Map<Planet, Bhinnashtakavarga>): Sarvashtakavarga {
        val combinedBindus = ZodiacSign.entries.associateWith { sign ->
            bhinnashtakavarga.values.sumOf { it.getBindusForSign(sign) }
        }.toMutableMap()

        val totalBindus = combinedBindus.values.sum()
        val strongestSign = combinedBindus.maxByOrNull { it.value }?.key ?: ZodiacSign.ARIES
        val weakestSign = combinedBindus.minByOrNull { it.value }?.key ?: ZodiacSign.ARIES

        return Sarvashtakavarga(combinedBindus, totalBindus, strongestSign, weakestSign)
    }

    private fun getBinduRulesForPlanet(planet: Planet) = when (planet) {
        Planet.SUN -> SUN_BINDU_RULES
        Planet.MOON -> MOON_BINDU_RULES
        Planet.MARS -> MARS_BINDU_RULES
        Planet.MERCURY -> MERCURY_BINDU_RULES
        Planet.JUPITER -> JUPITER_BINDU_RULES
        Planet.VENUS -> VENUS_BINDU_RULES
        Planet.SATURN -> SATURN_BINDU_RULES
        else -> emptyMap()
    }

    private fun getAscendantBindusForPlanet(planet: Planet) = when (planet) {
        Planet.SUN -> SUN_ASCENDANT_BINDUS
        Planet.MOON -> MOON_ASCENDANT_BINDUS
        Planet.MARS -> MARS_ASCENDANT_BINDUS
        Planet.MERCURY -> MERCURY_ASCENDANT_BINDUS
        Planet.JUPITER -> JUPITER_ASCENDANT_BINDUS
        Planet.VENUS -> VENUS_ASCENDANT_BINDUS
        Planet.SATURN -> SATURN_ASCENDANT_BINDUS
        else -> emptyList()
    }
}
