package com.astro.storm.ephemeris

import com.astro.storm.core.common.Language
import com.astro.storm.core.common.StringKeyAnalysis
import com.astro.storm.core.common.StringKeyDosha
import com.astro.storm.core.common.StringResources
import com.astro.storm.core.model.Planet
import com.astro.storm.core.model.PlanetPosition
import com.astro.storm.core.model.VedicChart
import com.astro.storm.core.model.ZodiacSign
import com.astro.storm.ephemeris.DivisionalChartData
import com.astro.storm.ephemeris.DivisionalChartType
import com.astro.storm.ephemeris.VedicAstrologyUtils.PlanetaryRelationship
import java.text.DecimalFormat
import java.time.LocalDateTime
import kotlin.math.PI
import kotlin.math.abs
import kotlin.math.cos
import kotlin.math.sin


enum class StrengthRating(
    @Deprecated("Use stringResource(rating.stringKey) or getLocalizedName(language)")
    val displayName: String,
    @Deprecated("Use localized interpretation")
    val description: String,
    val minPercentage: Double,
    val stringKey: com.astro.storm.core.common.StringKeyInterface
) {
    EXTREMELY_WEAK(
        "Extremely Weak",
        "Planet is severely debilitated and may cause significant challenges",
        0.0,
        StringKeyUI.STRENGTH_EXTREMELY_WEAK
    ),
    WEAK(
        "Weak",
        "Planet struggles to deliver its significations effectively",
        50.0,
        StringKeyUI.STRENGTH_WEAK
    ),
    BELOW_AVERAGE(
        "Below Average",
        "Planet has limited capacity to provide positive results",
        70.0,
        StringKeyUI.STRENGTH_BELOW_AVERAGE
    ),
    AVERAGE(
        "Average",
        "Planet functions at a baseline level with mixed results",
        85.0,
        StringKeyUI.STRENGTH_AVERAGE
    ),
    ABOVE_AVERAGE(
        "Above Average",
        "Planet is reasonably strong and delivers good results",
        100.0,
        StringKeyUI.STRENGTH_ABOVE_AVERAGE
    ),
    STRONG(
        "Strong",
        "Planet is well-positioned and gives excellent results",
        115.0,
        StringKeyUI.STRENGTH_STRONG
    ),
    VERY_STRONG(
        "Very Strong",
        "Planet is highly potent and provides outstanding outcomes",
        130.0,
        StringKeyUI.STRENGTH_VERY_STRONG
    ),
    EXTREMELY_STRONG(
        "Extremely Strong",
        "Planet is exceptionally powerful and dominates the chart",
        150.0,
        StringKeyUI.STRENGTH_EXTREMELY_STRONG
    );

    fun getLocalizedName(language: Language): String {
        return StringResources.get(stringKey, language)
    }

    companion object {
        fun fromPercentage(percentage: Double): StrengthRating {
            return entries.asReversed().firstOrNull { percentage >= it.minPercentage }
                ?: EXTREMELY_WEAK
        }
    }
}


data class SthanaBala(
    val ucchaBala: Double,
    val saptavargajaBala: Double,
    val ojhayugmarasyamsaBala: Double,
    val kendradiBala: Double,
    val drekkanaBala: Double
) {
    val total: Double = ucchaBala + saptavargajaBala + ojhayugmarasyamsaBala + kendradiBala + drekkanaBala
}

data class KalaBala(
    val nathonnathaBala: Double,
    val pakshaBala: Double,
    val tribhagaBala: Double,
    val horaAdiBala: Double,
    val ayanaBala: Double,
    val yuddhaBala: Double
) {
    val total: Double = nathonnathaBala + pakshaBala + tribhagaBala + horaAdiBala + ayanaBala + yuddhaBala
}

data class PlanetaryShadbala(
    val planet: Planet,
    val sthanaBala: SthanaBala,
    val digBala: Double,
    val kalaBala: KalaBala,
    val chestaBala: Double,
    val naisargikaBala: Double,
    val drikBala: Double,
    val totalVirupas: Double,
    val totalRupas: Double,
    val requiredRupas: Double,
    val percentageOfRequired: Double,
    val strengthRating: StrengthRating
) {
    val isStrong: Boolean = totalRupas >= requiredRupas

    fun getInterpretation(language: Language = Language.ENGLISH): String = buildString {
        appendLine(StringResources.get(StringKeyShadbala.SHADBALA_PLANET_ANALYSIS, language, planet.getLocalizedName(language)))
        appendLine("═══════════════════════════════════════")
        appendLine()
        appendLine("${StringResources.get(StringKeyShadbala.SHADBALA_TOTAL_STRENGTH, language)}: ${FORMAT_TWO_DECIMAL.format(totalRupas)} ${StringResources.get(StringKeyShadbala.SHADBALA_RUPAS, language)}")
        appendLine("${StringResources.get(StringKeyShadbala.SHADBALA_REQUIRED, language)}: ${FORMAT_TWO_DECIMAL.format(requiredRupas)} ${StringResources.get(StringKeyShadbala.SHADBALA_RUPAS, language)}")
        appendLine("${StringResources.get(StringKeyShadbala.SHADBALA_PERCENTAGE, language)}: ${FORMAT_ONE_DECIMAL.format(percentageOfRequired)}%")
        appendLine("${StringResources.get(StringKeyShadbala.SHADBALA_RATING, language)}: ${strengthRating.getLocalizedName(language)}")
        appendLine()
        appendLine("${StringResources.get(StringKeyShadbala.SHADBALA_BREAKDOWN, language)} (${StringResources.get(StringKeyShadbala.SHADBALA_VIRUPAS, language)}):")
        appendLine("───────────────────────────────────────")
        appendLine("1. ${StringResources.get(StringKeyShadbala.SHADBALA_STHANA_BALA, language)}: ${FORMAT_TWO_DECIMAL.format(sthanaBala.total)}")
        appendLine("   • ${StringResources.get(StringKeyShadbala.SHADBALA_UCCHA_BALA, language)}: ${FORMAT_TWO_DECIMAL.format(sthanaBala.ucchaBala)}")
        appendLine("   • ${StringResources.get(StringKeyShadbala.SHADBALA_SAPTAVARGAJA_BALA, language)}: ${FORMAT_TWO_DECIMAL.format(sthanaBala.saptavargajaBala)}")
        appendLine("   • ${StringResources.get(StringKeyShadbala.SHADBALA_OJHAYUGMA_BALA, language)}: ${FORMAT_TWO_DECIMAL.format(sthanaBala.ojhayugmarasyamsaBala)}")
        appendLine("   • ${StringResources.get(StringKeyShadbala.SHADBALA_KENDRADI_BALA, language)}: ${FORMAT_TWO_DECIMAL.format(sthanaBala.kendradiBala)}")
        appendLine("   • ${StringResources.get(StringKeyShadbala.SHADBALA_DREKKANA_BALA, language)}: ${FORMAT_TWO_DECIMAL.format(sthanaBala.drekkanaBala)}")
        appendLine()
        appendLine("2. ${StringResources.get(StringKeyShadbala.SHADBALA_DIG_BALA, language)}: ${FORMAT_TWO_DECIMAL.format(digBala)}")
        appendLine()
        appendLine("3. ${StringResources.get(StringKeyShadbala.SHADBALA_KALA_BALA, language)}: ${FORMAT_TWO_DECIMAL.format(kalaBala.total)}")
        appendLine("   • ${StringResources.get(StringKeyShadbala.SHADBALA_NATHONNATHA_BALA, language)}: ${FORMAT_TWO_DECIMAL.format(kalaBala.nathonnathaBala)}")
        appendLine("   • ${StringResources.get(StringKeyShadbala.SHADBALA_PAKSHA_BALA, language)}: ${FORMAT_TWO_DECIMAL.format(kalaBala.pakshaBala)}")
        appendLine("   • ${StringResources.get(StringKeyShadbala.SHADBALA_TRIBHAGA_BALA, language)}: ${FORMAT_TWO_DECIMAL.format(kalaBala.tribhagaBala)}")
        appendLine("   • ${StringResources.get(StringKeyShadbala.SHADBALA_HORA_BALA, language)}: ${FORMAT_TWO_DECIMAL.format(kalaBala.horaAdiBala)}")
        appendLine("   • ${StringResources.get(StringKeyShadbala.SHADBALA_AYANA_BALA, language)}: ${FORMAT_TWO_DECIMAL.format(kalaBala.ayanaBala)}")
        appendLine("   • ${StringResources.get(StringKeyShadbala.SHADBALA_YUDDHA_BALA, language)}: ${FORMAT_TWO_DECIMAL.format(kalaBala.yuddhaBala)}")
        appendLine()
        appendLine("4. ${StringResources.get(StringKeyShadbala.SHADBALA_CHESTA_BALA, language)}: ${FORMAT_TWO_DECIMAL.format(chestaBala)}")
        appendLine()
        appendLine("5. ${StringResources.get(StringKeyShadbala.SHADBALA_NAISARGIKA_BALA, language)}: ${FORMAT_TWO_DECIMAL.format(naisargikaBala)}")
        appendLine()
        appendLine("6. ${StringResources.get(StringKeyShadbala.SHADBALA_DRIK_BALA, language)}: ${FORMAT_TWO_DECIMAL.format(drikBala)}")
    }

    companion object {
        private val FORMAT_TWO_DECIMAL = DecimalFormat("0.00")
        private val FORMAT_ONE_DECIMAL = DecimalFormat("0.0")
    }
}

data class ShadbalaAnalysis(
    val chartId: String,
    val planetaryStrengths: Map<Planet, PlanetaryShadbala>,
    val strongestPlanet: Planet,
    val weakestPlanet: Planet,
    val overallStrengthScore: Double,
    val timestamp: Long = System.currentTimeMillis()
) {
    fun getPlanetsByStrength(): List<PlanetaryShadbala> =
        planetaryStrengths.values.sortedByDescending { it.totalRupas }

    fun getWeakPlanets(): List<PlanetaryShadbala> =
        planetaryStrengths.values.filter { !it.isStrong }

    fun getStrongPlanets(): List<PlanetaryShadbala> =
        planetaryStrengths.values.filter { it.isStrong }

    fun getSummaryInterpretation(language: Language = Language.ENGLISH): String {
        val strong = planetaryStrengths.values.count { it.isStrong }
        val weak = planetaryStrengths.values.size - strong

        return buildString {
            appendLine(StringResources.get(StringKeyShadbala.SHADBALA_TITLE, language) + " " + StringResources.get(StringKeyShadbala.SHADBALA_OVERVIEW, language))
            appendLine("═══════════════════════════════════════")
            appendLine()
            appendLine("${StringResources.get(StringKeyShadbala.SHADBALA_OVERALL_STRENGTH, language)}: ${String.format("%.1f", overallStrengthScore)}%")
            appendLine("${StringResources.get(StringKeyShadbala.SHADBALA_STRONG_COUNT, language, strong)}")
            appendLine("${StringResources.get(StringKeyShadbala.SHADBALA_WEAK_COUNT, language, weak)}")
            appendLine()
            appendLine("${StringResources.get(StringKeyShadbala.SHADBALA_STRONGEST_PLANET, language)}: ${strongestPlanet.getLocalizedName(language)}")
            appendLine("${StringResources.get(StringKeyShadbala.SHADBALA_WEAKEST_PLANET, language)}: ${weakestPlanet.getLocalizedName(language)}")
            appendLine()
            appendLine("${StringResources.get(StringKeyShadbala.SHADBALA_BREAKDOWN, language)} (${StringResources.get(StringKeyShadbala.SHADBALA_RUPAS, language)}):")
            appendLine("───────────────────────────────────────")
            getPlanetsByStrength().forEach { shadbala ->
                val status = if (shadbala.isStrong) "✓" else "✗"
                val name = shadbala.planet.getLocalizedName(language).padEnd(10)
                val total = String.format("%.2f", shadbala.totalRupas)
                val required = String.format("%.2f", shadbala.requiredRupas)
                appendLine("$status $name: $total / $required")
            }
        }
    }
}

object ShadbalaCalculator {

    private const val VIRUPAS_PER_RUPA = 60.0
    private const val DEGREES_PER_CIRCLE = 360.0
    private const val DEGREES_PER_SIGN = 30.0

    private val SHADBALA_PLANETS = setOf(
        Planet.SUN, Planet.MOON, Planet.MARS,
        Planet.MERCURY, Planet.JUPITER, Planet.VENUS, Planet.SATURN
    )

    private object ExaltationData {
        val degrees = mapOf(
            Planet.SUN to 10.0,
            Planet.MOON to 33.0,
            Planet.MARS to 298.0,
            Planet.MERCURY to 165.0,
            Planet.JUPITER to 95.0,
            Planet.VENUS to 357.0,
            Planet.SATURN to 200.0
        )

        val debilitationDegrees = degrees.mapValues { (_, deg) ->
            (deg + 180.0) % DEGREES_PER_CIRCLE
        }
    }

    /**
     * Moolatrikona data per BPHS (Brihat Parashara Hora Shastra) Chapter 3.
     *
     * Moolatrikona (root trine) is a special dignity where planets function optimally.
     * These ranges are carefully verified against classical texts.
     */
    private object MoolatrikonaData {
        data class Range(val sign: ZodiacSign, val startDegree: Double, val endDegree: Double)

        /**
         * Moolatrikona ranges per BPHS Chapter 3:
         * - Sun: Leo 0°-20° (rest is own sign)
         * - Moon: Taurus 3°-27° (0°-3° is exaltation zone, 27°-30° is own sign portion)
         * - Mars: Aries 0°-12° (12°-30° is own sign)
         * - Mercury: Virgo 15°-20° (0°-15° is exaltation zone, 20°-30° is own sign)
         * - Jupiter: Sagittarius 0°-10° (10°-30° is own sign)
         * - Venus: Libra 0°-15° (15°-30° is own sign)
         * - Saturn: Aquarius 0°-20° (20°-30° is own sign)
         *
         * Note: Mercury's Moolatrikona is 15°-20° Virgo per BPHS. The 0°-15° is where
         * Mercury is both exalted (at 15°) and approaching exaltation. Some texts
         * consolidate exaltation point (15°) with Moolatrikona, but strict BPHS
         * interpretation separates them.
         */
        val ranges = mapOf(
            Planet.SUN to Range(ZodiacSign.LEO, 0.0, 20.0),
            Planet.MOON to Range(ZodiacSign.TAURUS, 3.0, 27.0),  // Corrected per BPHS
            Planet.MARS to Range(ZodiacSign.ARIES, 0.0, 12.0),
            Planet.MERCURY to Range(ZodiacSign.VIRGO, 15.0, 20.0),  // Corrected: 15-20° per BPHS
            Planet.JUPITER to Range(ZodiacSign.SAGITTARIUS, 0.0, 10.0),
            Planet.VENUS to Range(ZodiacSign.LIBRA, 0.0, 15.0),
            Planet.SATURN to Range(ZodiacSign.AQUARIUS, 0.0, 20.0)
        )

        fun isInMoolatrikona(planet: Planet, sign: ZodiacSign, degreeInSign: Double): Boolean {
            val range = ranges[planet] ?: return false
            return sign == range.sign && degreeInSign >= range.startDegree && degreeInSign <= range.endDegree
        }
    }

    private object NaturalStrength {
        val virupas = mapOf(
            Planet.SUN to 60.0,
            Planet.MOON to 51.43,
            Planet.VENUS to 42.86,
            Planet.JUPITER to 34.29,
            Planet.MERCURY to 25.71,
            Planet.MARS to 17.14,
            Planet.SATURN to 8.57
        )
    }

    private object RequiredStrength {
        val rupas = mapOf(
            Planet.SUN to 6.5,
            Planet.MOON to 6.0,
            Planet.MARS to 5.0,
            Planet.MERCURY to 7.0,
            Planet.JUPITER to 6.5,
            Planet.VENUS to 5.5,
            Planet.SATURN to 5.0
        )
    }

    private object DigBalaPositions {
        val strongestHouse = mapOf(
            Planet.SUN to 10,
            Planet.MOON to 4,
            Planet.MARS to 10,
            Planet.MERCURY to 1,
            Planet.JUPITER to 1,
            Planet.VENUS to 4,
            Planet.SATURN to 7
        )
    }

    private object VedicAspects {
        data class AspectInfo(val house: Int, val strength: Double)

        val specialAspects = mapOf(
            Planet.MARS to listOf(AspectInfo(4, 0.75), AspectInfo(8, 0.75)),
            Planet.JUPITER to listOf(AspectInfo(5, 1.0), AspectInfo(9, 1.0)),
            Planet.SATURN to listOf(AspectInfo(3, 0.75), AspectInfo(10, 0.75))
        )

        fun getAspectStrength(aspectingPlanet: Planet, houseDifference: Int): Double {
            if (houseDifference == 7) return 1.0

            val specialAspectList = specialAspects[aspectingPlanet] ?: return 0.0
            return specialAspectList.find { it.house == houseDifference }?.strength ?: 0.0
        }
    }

    private object PlanetaryWarBrightness {
        val order = listOf(Planet.VENUS, Planet.JUPITER, Planet.MERCURY, Planet.MARS, Planet.SATURN)

        fun getWinner(planet1: Planet, planet2: Planet): Planet {
            val index1 = order.indexOf(planet1)
            val index2 = order.indexOf(planet2)
            return if (index1 != -1 && index2 != -1 && index1 < index2) planet1 else planet2
        }
    }

    private class ChartContext(val context: android.content.Context, val chart: VedicChart) {
        val planetMap: Map<Planet, PlanetPosition> by lazy {
            chart.planetPositions.associateBy { it.planet }
        }

        val sunPosition: PlanetPosition? by lazy { planetMap[Planet.SUN] }
        val moonPosition: PlanetPosition? by lazy { planetMap[Planet.MOON] }

        val divisionalCharts: List<DivisionalChartData> by lazy {
            DivisionalChartCalculator.calculateAllDivisionalCharts(chart)
        }

        val divisionalChartMap: Map<DivisionalChartType, DivisionalChartData> by lazy {
            divisionalCharts.associateBy { it.chartType }
        }

        val lunarElongation: Double by lazy {
            val moonLong = moonPosition?.longitude
                ?: throw IllegalStateException("Moon position required for Shadbala calculation")
            val sunLong = sunPosition?.longitude
                ?: throw IllegalStateException("Sun position required for Shadbala calculation")
            normalizeDegree(moonLong - sunLong)
        }

        val isShuklaPacksha: Boolean by lazy { lunarElongation < 180.0 }

        val panchangaData by lazy {
            PanchangaCalculator(context).use {
                it.calculatePanchanga(
                    chart.birthData.dateTime,
                    chart.birthData.latitude,
                    chart.birthData.longitude,
                    chart.birthData.timezone
                )
            }
        }

        val sunriseJD: Double by lazy { panchangaData.sunriseJD }
        val sunsetJD: Double by lazy { panchangaData.sunsetJD }
        val birthJD: Double = chart.julianDay

        val isDay: Boolean by lazy { birthJD in sunriseJD..sunsetJD }

        val birthHour: Int = chart.birthData.dateTime.hour

        val dayLord: Planet by lazy {
            getDayLordForWeekday(chart.birthData.dateTime.dayOfWeek.value)
        }

        val horaLord: Planet by lazy {
            calculateHoraLord(chart.birthData.dateTime, dayLord)
        }
    }

    fun calculateShadbala(androidContext: android.content.Context, chart: VedicChart): ShadbalaAnalysis {
        val context = ChartContext(androidContext, chart)
        val strengths = mutableMapOf<Planet, PlanetaryShadbala>()

        for (position in chart.planetPositions) {
            if (position.planet in SHADBALA_PLANETS) {
                strengths[position.planet] = calculatePlanetShadbalaInternal(position, context)
            }
        }

        require(strengths.isNotEmpty()) { "No valid planets found for Shadbala calculation" }

        val sortedStrengths = strengths.values.sortedByDescending { it.totalRupas }
        val overallScore = strengths.values.map { it.percentageOfRequired }.average()

        return ShadbalaAnalysis(
            chartId = generateStableChartId(chart),
            planetaryStrengths = strengths.toMap(),
            strongestPlanet = sortedStrengths.first().planet,
            weakestPlanet = sortedStrengths.last().planet,
            overallStrengthScore = overallScore
        )
    }

    fun calculatePlanetShadbala(
        androidContext: android.content.Context,
        position: PlanetPosition,
        chart: VedicChart
    ): PlanetaryShadbala = calculatePlanetShadbalaInternal(position, ChartContext(androidContext, chart))

    private fun calculatePlanetShadbalaInternal(
        position: PlanetPosition,
        context: ChartContext
    ): PlanetaryShadbala {
        val planet = position.planet

        val sthanaBala = calculateSthanaBala(position, context)
        val digBala = calculateDigBala(position)
        val kalaBala = calculateKalaBala(position, context)
        val chestaBala = calculateChestaBala(position)
        val naisargikaBala = NaturalStrength.virupas[planet] ?: 0.0
        val drikBala = calculateDrikBala(position, context)

        val totalVirupas = sthanaBala.total + digBala + kalaBala.total +
                chestaBala + naisargikaBala + drikBala
        val totalRupas = totalVirupas / VIRUPAS_PER_RUPA
        val requiredRupas = RequiredStrength.rupas[planet] ?: 5.0
        val percentage = (totalRupas / requiredRupas) * 100.0
        val rating = StrengthRating.fromPercentage(percentage)

        return PlanetaryShadbala(
            planet = planet,
            sthanaBala = sthanaBala,
            digBala = digBala,
            kalaBala = kalaBala,
            chestaBala = chestaBala,
            naisargikaBala = naisargikaBala,
            drikBala = drikBala,
            totalVirupas = totalVirupas,
            totalRupas = totalRupas,
            requiredRupas = requiredRupas,
            percentageOfRequired = percentage,
            strengthRating = rating
        )
    }

    private fun calculateSthanaBala(position: PlanetPosition, context: ChartContext): SthanaBala {
        return SthanaBala(
            ucchaBala = calculateUcchaBala(position),
            saptavargajaBala = calculateSaptavargajaBala(position, context),
            ojhayugmarasyamsaBala = calculateOjhayugmarasyamsaBala(position),
            kendradiBala = calculateKendradiBala(position),
            drekkanaBala = calculateDrekkanaBala(position)
        )
    }

    private fun calculateUcchaBala(position: PlanetPosition): Double {
        val exaltDeg = ExaltationData.degrees[position.planet] ?: return 0.0
        val debilDeg = ExaltationData.debilitationDegrees[position.planet] ?: return 0.0

        var distance = normalizeDegree(position.longitude - debilDeg)
        if (distance > 180.0) distance = DEGREES_PER_CIRCLE - distance

        return (distance / 180.0) * 60.0
    }

    private fun calculateSaptavargajaBala(position: PlanetPosition, context: ChartContext): Double {
        val planet = position.planet
        var totalBala = 0.0

        // D1 Rashi
        totalBala += getVargaStrength(planet, position.sign, position.longitude % DEGREES_PER_SIGN, context.chart.planetPositions)

        // Tradition B Seven Vargas (Saptavarga): D1, D2, D3, D9, D12, D30, D60
        // D1 is already added above. We add the remaining 6.
        val vargas = listOf(
            DivisionalChartType.D2_HORA,
            DivisionalChartType.D3_DREKKANA,
            DivisionalChartType.D9_NAVAMSA,
            DivisionalChartType.D12_DWADASAMSA,
            DivisionalChartType.D30_TRIMSAMSA,
            DivisionalChartType.D60_SHASHTIAMSA
        )

        for (vargaType in vargas) {
            context.divisionalChartMap[vargaType]?.let { chart ->
                chart.planetPositions.find { it.planet == planet }?.let { pos ->
                    totalBala += getVargaStrength(planet, pos.sign, pos.longitude % DEGREES_PER_SIGN, chart.planetPositions)
                }
            }
        }

        // BPHS standard: Saptavargaja Bala is the sum of virupas
        return totalBala
    }

    private fun getVargaStrength(
        planet: Planet,
        sign: ZodiacSign,
        degreeInSign: Double,
        allPositions: List<PlanetPosition>
    ): Double {
        if (MoolatrikonaData.isInMoolatrikona(planet, sign, degreeInSign)) return 45.0
        if (isOwnSign(planet, sign)) return 30.0

        val signLord = sign.ruler
        val relationship = getCompoundRelationship(planet, signLord, allPositions)

        // BPHS Virupa values for Saptavargaja relationships:
        // Adhimitra (Great Friend): 22.5
        // Mitra (Friend): 15.0
        // Sama (Neutral): 7.5
        // Shatru (Enemy): 3.75
        // Adhishatru (Bitter Enemy): 1.875
        return when (relationship) {
            PlanetaryRelationship.BEST_FRIEND -> 22.5
            PlanetaryRelationship.FRIEND -> 15.0
            PlanetaryRelationship.NEUTRAL -> 7.5
            PlanetaryRelationship.ENEMY -> 3.75
            PlanetaryRelationship.BITTER_ENEMY -> 1.875
        }
    }

    /**
     * Calculate compound (Panchada) relationship for a specific varga chart.
     */
    private fun getCompoundRelationship(
        p1: Planet,
        p2: Planet,
        positions: List<PlanetPosition>
    ): PlanetaryRelationship {
        if (p1 == p2) return PlanetaryRelationship.BEST_FRIEND

        val pos1 = positions.find { it.planet == p1 }
        val pos2 = positions.find { it.planet == p2 }

        val naturalRel = VedicAstrologyUtils.getNaturalRelationship(p1, p2)
        if (pos1 == null || pos2 == null) return naturalRel

        val houseDistance = ((pos2.house - pos1.house + 12) % 12) + 1
        val isTempFriend = houseDistance in listOf(1, 2, 3, 4, 10, 11, 12)
        val isTempEnemy = houseDistance in listOf(5, 6, 7, 8, 9)

        return when (naturalRel) {
            PlanetaryRelationship.FRIEND -> if (isTempFriend) PlanetaryRelationship.BEST_FRIEND else PlanetaryRelationship.NEUTRAL
            PlanetaryRelationship.ENEMY -> if (isTempFriend) PlanetaryRelationship.NEUTRAL else PlanetaryRelationship.BITTER_ENEMY
            PlanetaryRelationship.NEUTRAL -> if (isTempFriend) PlanetaryRelationship.FRIEND else PlanetaryRelationship.ENEMY
            else -> naturalRel
        }
    }

    private fun calculateOjhayugmarasyamsaBala(position: PlanetPosition): Double {
        val isOddSign = position.sign.number % 2 == 1

        return when (position.planet) {
            Planet.MOON, Planet.VENUS -> if (!isOddSign) 15.0 else 0.0
            else -> if (isOddSign) 15.0 else 0.0
        }
    }

    private fun calculateKendradiBala(position: PlanetPosition): Double {
        return when (position.house) {
            1, 4, 7, 10 -> 60.0
            2, 5, 8, 11 -> 30.0
            3, 6, 9, 12 -> 15.0
            else -> 0.0
        }
    }

    private fun calculateDrekkanaBala(position: PlanetPosition): Double {
        val degreeInSign = position.longitude % DEGREES_PER_SIGN
        val decanate = when {
            degreeInSign < 10.0 -> 1
            degreeInSign < 20.0 -> 2
            else -> 3
        }

        return when (position.planet) {
            Planet.SUN, Planet.MARS, Planet.JUPITER -> if (decanate == 1) 15.0 else 0.0
            Planet.MOON, Planet.VENUS -> if (decanate == 3) 15.0 else 0.0
            Planet.MERCURY, Planet.SATURN -> if (decanate == 2) 15.0 else 0.0
            else -> 0.0
        }
    }

    private fun calculateDigBala(position: PlanetPosition): Double {
        val strongHouse = DigBalaPositions.strongestHouse[position.planet] ?: return 0.0
        val currentHouse = position.house

        var distance = abs(currentHouse - strongHouse)
        if (distance > 6) distance = 12 - distance

        return (6 - distance) * 10.0
    }

    private fun calculateKalaBala(position: PlanetPosition, context: ChartContext): KalaBala {
        return KalaBala(
            nathonnathaBala = calculateNathonnathaBala(position, context),
            pakshaBala = calculatePakshaBala(position, context),
            tribhagaBala = calculateTribhagaBala(position, context),
            horaAdiBala = calculateHoraAdiBala(position, context),
            ayanaBala = calculateAyanaBala(position, context.chart.ayanamsa),
            yuddhaBala = calculateYuddhaBala(position, context)
        )
    }

    private fun calculateNathonnathaBala(position: PlanetPosition, context: ChartContext): Double {
        val planet = position.planet
        if (planet == Planet.MERCURY) return 60.0

        val sunHouse = context.sunPosition?.house ?: return 30.0
        
        // Midday is House 10, Midnight is House 4
        // Day planets (Sun, Jupiter, Venus) are strongest at Midday (House 10)
        // Night planets (Moon, Mars, Saturn) are strongest at Midnight (House 4)
        
        return when (planet) {
            Planet.SUN, Planet.JUPITER, Planet.VENUS -> {
                // Distance from Midday (House 10)
                var distance = abs(sunHouse - 10)
                if (distance > 6) distance = 12 - distance
                (6 - distance) * 10.0
            }
            Planet.MOON, Planet.MARS, Planet.SATURN -> {
                // Distance from Midnight (House 4)
                var distance = abs(sunHouse - 4)
                if (distance > 6) distance = 12 - distance
                (6 - distance) * 10.0
            }
            else -> 30.0
        }
    }

    private fun calculatePakshaBala(position: PlanetPosition, context: ChartContext): Double {
        val elongation = context.lunarElongation

        val phaseStrength = if (elongation < 180.0) {
            elongation / 180.0 * 60.0
        } else {
            (DEGREES_PER_CIRCLE - elongation) / 180.0 * 60.0
        }

        val isBenefic = position.planet in setOf(
            Planet.JUPITER, Planet.VENUS, Planet.MOON, Planet.MERCURY
        )

        return if ((isBenefic && context.isShuklaPacksha) || (!isBenefic && !context.isShuklaPacksha)) {
            phaseStrength
        } else {
            60.0 - phaseStrength
        }
    }

    /**
     * Calculate Tribhaga Bala (Three-Part Strength) per BPHS.
     *
     * The day and night are each divided into three equal parts (Tribhagas):
     *
     * Day (assuming sunrise at ~6 AM and sunset at ~6 PM for simplicity):
     * - First Tribhaga (6 AM - 10 AM): Mercury rules
     * - Second Tribhaga (10 AM - 2 PM): Sun rules
     * - Third Tribhaga (2 PM - 6 PM): Saturn rules
     *
     * Night (assuming sunset at ~6 PM and sunrise at ~6 AM):
     * - First Tribhaga (6 PM - 10 PM): Moon rules
     * - Second Tribhaga (10 PM - 2 AM): Venus rules
     * - Third Tribhaga (2 AM - 6 AM): Mars rules
     *
     * Jupiter gains Tribhaga Bala at all times (some texts give Jupiter
     * strength during all Tribhagas as a natural benefic).
     *
     * Note: For more accurate calculations, actual sunrise/sunset times
     * based on location and date should be used. This implementation uses
     * a standard 6 AM sunrise / 6 PM sunset approximation.
     *
     * Per BPHS: Maximum Tribhaga Bala = 60 Virupas
     *
     * @param position The planet position to calculate Tribhaga Bala for
     * @param context The chart context with birth time information
     * @return Tribhaga Bala in Virupas (0 or 60)
     */
    private fun calculateTribhagaBala(position: PlanetPosition, context: ChartContext): Double {
        val birthJD = context.birthJD

        // Jupiter gets partial Tribhaga Bala at all times per some traditions
        if (position.planet == Planet.JUPITER) return 30.0

        val periodLord = if (context.isDay) {
            val dayDuration = context.sunsetJD - context.sunriseJD
            val tribhagaSize = dayDuration / 3.0
            
            when {
                birthJD < context.sunriseJD + tribhagaSize -> Planet.MERCURY
                birthJD < context.sunriseJD + 2.0 * tribhagaSize -> Planet.SUN
                else -> Planet.SATURN
            }
        } else {
            // Precise night tribhaga
            val sunriseJD = context.sunriseJD
            val sunsetJD = context.sunsetJD
            
            val (nightStart, nightEnd) = if (birthJD < sunriseJD) {
                // Before current sunrise, must be after previous sunset
                val prevSunsetJD = PanchangaCalculator(context.context).use {
                    it.calculatePanchanga(
                        context.chart.birthData.dateTime.minusDays(1),
                        context.chart.birthData.latitude,
                        context.chart.birthData.longitude,
                        context.chart.birthData.timezone
                    ).sunsetJD
                }
                Pair(prevSunsetJD, sunriseJD)
            } else {
                // After current sunset, must be before next sunrise
                val nextSunriseJD = PanchangaCalculator(context.context).use {
                    it.calculatePanchanga(
                        context.chart.birthData.dateTime.plusDays(1),
                        context.chart.birthData.latitude,
                        context.chart.birthData.longitude,
                        context.chart.birthData.timezone
                    ).sunriseJD
                }
                Pair(sunsetJD, nextSunriseJD)
            }

            val nightDuration = nightEnd - nightStart
            val tribhagaSize = nightDuration / 3.0
            
            when {
                birthJD < nightStart + tribhagaSize -> Planet.MOON
                birthJD < nightStart + 2.0 * tribhagaSize -> Planet.VENUS
                else -> Planet.MARS
            }
        }

        return if (position.planet == periodLord) 60.0 else 0.0
    }

    private fun calculateHoraAdiBala(position: PlanetPosition, context: ChartContext): Double {
        var bala = 0.0

        if (position.planet == context.dayLord) bala += 15.0
        if (position.planet == context.horaLord) bala += 15.0

        context.moonPosition?.let { moon ->
            if (position.planet == moon.sign.ruler) bala += 10.0
        }

        if (position.planet == Planet.SUN) bala += 5.0

        return bala
    }

    /**
     * Calculate Ayana Bala (Solstice Strength) per BPHS.
     *
     * Ayana Bala is based on the planet's declination relative to the celestial equator.
     * Planets gain strength based on their hemispheric position:
     * - Northern declination planets (Sun, Mars, Jupiter) are stronger when in northern hemisphere
     * - Southern declination planets (Moon, Venus, Saturn) are stronger when in southern hemisphere
     *
     * IMPORTANT: This must be calculated using TROPICAL (Sayana) longitude.
     * declination = arcsin(sin(tropical_longitude) × sin(obliquity))
     *
     * Per BPHS: Maximum Ayana Bala = 60 Virupas
     *
     * @param position The planet position to calculate Ayana Bala for
     * @param ayanamsa The Ayanamsa value to convert sidereal to tropical longitude
     * @return Ayana Bala in Virupas (0-60)
     */
    private fun calculateAyanaBala(position: PlanetPosition, ayanamsa: Double): Double {
        // Obliquity of the ecliptic (Earth's axial tilt) - approx 23.44° for modern era
        val obliquity = 23.44

        // Convert sidereal longitude to tropical (Sayana)
        val tropicalLongitude = normalizeDegree(position.longitude + ayanamsa)

        // Calculate declination using proper spherical astronomy formula
        // declination = arcsin(sin(longitude) × sin(obliquity))
        val longitudeRadians = Math.toRadians(tropicalLongitude)
        val obliquityRadians = Math.toRadians(obliquity)

        // Declination ranges from -23.44° to +23.44°
        val declination = Math.toDegrees(
            kotlin.math.asin(sin(longitudeRadians) * sin(obliquityRadians))
        )

        // Normalize declination to a 0-60 Virupa scale
        // declination ranges from -23.44 to +23.44, so we scale it
        val normalizedDeclination = (declination / obliquity) * 30.0  // -30 to +30

        return when (position.planet) {
            // Northern planets gain strength with positive (northern) declination
            Planet.SUN, Planet.MARS, Planet.JUPITER ->
                (30.0 + normalizedDeclination).coerceIn(0.0, 60.0)
            // Southern planets gain strength with negative (southern) declination
            Planet.MOON, Planet.VENUS, Planet.SATURN ->
                (30.0 - normalizedDeclination).coerceIn(0.0, 60.0)
            // Mercury is neutral
            Planet.MERCURY -> 30.0
            else -> 30.0
        }
    }

    /**
     * Calculate Yuddha Bala (Planetary War Strength) per BPHS.
     *
     * Planetary War (Graha Yuddha) occurs when two planets are in close conjunction.
     * Per classical texts, war occurs when planets are within 1 degree of each other.
     *
     * The winner is determined by:
     * 1. Northern latitude (greater northern/lesser southern latitude) wins
     * 2. The winner gains strength, loser loses strength
     *
     * Per BPHS: Maximum Yuddha Bala = ±30 Virupas
     *
     * @param position The planet position to calculate Yuddha Bala for
     * @param context The chart context containing all planet positions
     * @return Yuddha Bala in Virupas (-30 to +30, or 0 if no war)
     */
    private fun calculateYuddhaBala(position: PlanetPosition, context: ChartContext): Double {
        if (position.planet !in WAR_CAPABLE_PLANETS) return 0.0

        var totalYuddhaBala = 0.0

        for ((planet, otherPos) in context.planetMap) {
            if (planet == position.planet) continue
            if (planet !in WAR_CAPABLE_PLANETS) continue

            val distance = angularDistance(position.longitude, otherPos.longitude)

            // War within 1 degree
            if (distance <= 1.0) {
                // Determine winner by latitude (higher latitude wins)
                val isWinner = position.latitude > otherPos.latitude
                totalYuddhaBala += if (isWinner) 30.0 else -30.0
            }
        }

        return totalYuddhaBala.coerceIn(-30.0, 30.0)
    }

    /**
     * Calculate Chesta Bala (Mototional Strength) per BPHS.
     * 
     * Chesta Bala is based on the planet's speed and direction.
     * Retrograde planets (Vakra) and slow moving planets gain more strength.
     * 
     * Formula: (Distance from Mean Planet / Total Range) * 60
     * Threshold-based simplification for production-grade robustness.
     */
    private fun calculateChestaBala(position: PlanetPosition): Double {
        if (position.planet in setOf(Planet.SUN, Planet.MOON)) return 0.0

        return when {
            position.isRetrograde -> 60.0
            position.speed < 0.05 -> 50.0 // Very slow
            position.speed < 0.5 -> 40.0  // Slow
            position.speed < 0.8 -> 30.0  // Average
            position.speed < 1.2 -> 20.0  // Fast
            else -> 10.0                  // Very Fast
        }
    }

    private fun calculateDrikBala(position: PlanetPosition, context: ChartContext): Double {
        var bala = 0.0

        for ((planet, aspectingPos) in context.planetMap) {
            if (planet == position.planet) continue

            val houseDiff = calculateHouseDifference(aspectingPos.house, position.house)
            val aspectStrength = VedicAspects.getAspectStrength(planet, houseDiff)

            if (aspectStrength > 0.0) {
                val aspectValue = when (planet) {
                    Planet.JUPITER, Planet.VENUS -> aspectStrength * 15.0
                    Planet.MOON -> if (!aspectingPos.isRetrograde) aspectStrength * 10.0 else aspectStrength * 5.0
                    Planet.MERCURY -> aspectStrength * 8.0
                    Planet.SUN -> -aspectStrength * 5.0
                    Planet.MARS, Planet.SATURN -> -aspectStrength * 10.0
                    else -> 0.0
                }
                bala += aspectValue
            }
        }

        return bala.coerceIn(-30.0, 60.0)
    }

    private fun isExalted(planet: Planet, sign: ZodiacSign): Boolean {
        return when (planet) {
            Planet.SUN -> sign == ZodiacSign.ARIES
            Planet.MOON -> sign == ZodiacSign.TAURUS
            Planet.MARS -> sign == ZodiacSign.CAPRICORN
            Planet.MERCURY -> sign == ZodiacSign.VIRGO
            Planet.JUPITER -> sign == ZodiacSign.CANCER
            Planet.VENUS -> sign == ZodiacSign.PISCES
            Planet.SATURN -> sign == ZodiacSign.LIBRA
            else -> false
        }
    }

    private fun isOwnSign(planet: Planet, sign: ZodiacSign): Boolean = sign.ruler == planet

    private fun getDayLordForWeekday(dayOfWeek: Int): Planet {
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
        val horaIndex = (startIndex + horasSinceSunrise) % 7

        return hourSequence[horaIndex]
    }

    private fun calculateHouseDifference(fromHouse: Int, toHouse: Int): Int {
        var diff = toHouse - fromHouse
        if (diff <= 0) diff += 12
        return diff
    }

    /**
     * Normalize degree using centralized utility.
     */
    private fun normalizeDegree(degree: Double): Double = VedicAstrologyUtils.normalizeDegree(degree)

    /**
     * Calculate angular distance using centralized utility.
     */
    private fun angularDistance(deg1: Double, deg2: Double): Double =
        VedicAstrologyUtils.angularDistance(deg1, deg2)

    private val WAR_CAPABLE_PLANETS = setOf(
        Planet.MARS, Planet.MERCURY, Planet.JUPITER, Planet.VENUS, Planet.SATURN
    )

    private fun generateStableChartId(chart: VedicChart): String {
        val birthData = chart.birthData
        return "${birthData.name}-${birthData.dateTime}-${birthData.latitude}-${birthData.longitude}".replace(
            Regex("[^a-zA-Z0-9-]"),
            "_"
        )
    }
}


