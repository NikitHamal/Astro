package com.astro.vajra.ephemeris

import android.content.Context
import com.astro.vajra.core.model.BirthData
import com.astro.vajra.core.model.Nakshatra
import com.astro.vajra.core.model.Planet
import com.astro.vajra.core.model.PlanetPosition
import com.astro.vajra.core.model.VedicChart
import com.astro.vajra.core.model.ZodiacSign
import com.astro.vajra.ephemeris.jaimini.JaiminiKarakaCalculator
import com.astro.vajra.ephemeris.muhurta.ActivityType
import com.astro.vajra.ephemeris.muhurta.MuhurtaCalculator
import com.astro.vajra.util.TimezoneSanitizer
import org.json.JSONObject
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.ZoneId
import java.time.ZoneOffset
import java.time.ZonedDateTime
import kotlin.math.abs
import kotlin.math.acos
import kotlin.math.asin
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.floor
import kotlin.math.roundToInt
import kotlin.math.sin
import kotlin.math.tan

object VedicOracleEngine {

    data class OracleAnalysis(
        val generatedAt: LocalDateTime,
        val triplePillar: TriplePillarResult,
        val bhriguNandiNadi: BhriguNandiNadiResult,
        val kpSystem: KpResult,
        val muhurtaOptimization: MuhurtaOptimizationResult,
        val ishtaDevata: IshtaDevataResult,
        val sarvatobhadraVedha: SarvatobhadraVedhaResult,
        val panchapakshi: PanchapakshiResult,
        val vargaDeities: VargaDeityResult,
        val chakraHealth: ChakraHealthResult,
        val customYogas: List<CustomYogaMatch>,
        val karmicNode: KarmicNodeResult,
        val skyView: SkyViewResult
    )

    data class TriplePillarResult(
        val currentMahadasha: Planet?,
        val currentAntardasha: Planet?,
        val timeline: List<ProbabilityPoint>,
        val peakWindows: List<ProbabilityPoint>,
        val summary: String
    )

    data class ProbabilityPoint(
        val date: LocalDate,
        val dashaLord: Planet,
        val antardashaLord: Planet?,
        val transitSupport: Double,
        val ashtakavargaSupport: Double,
        val dashaSupport: Double,
        val successProbability: Int,
        val activatedHouses: List<Int>,
        val summary: String
    )

    data class BhriguNandiNadiResult(
        val links: List<NadiLink>,
        val chains: List<NadiChain>,
        val dominantThemes: List<String>
    )

    data class NadiLink(
        val from: Planet,
        val to: Planet,
        val signDistance: Int,
        val relation: String,
        val weight: Double
    )

    data class NadiChain(
        val planets: List<Planet>,
        val cumulativeWeight: Double,
        val interpretation: String
    )

    data class KpResult(
        val cusps: List<KpCusp>,
        val houseGatekeepers: List<KpHouseGatekeeper>,
        val highlights: List<String>
    )

    data class KpCusp(
        val house: Int,
        val cuspLongitude: Double,
        val sign: ZodiacSign,
        val signLord: Planet,
        val starLord: Planet,
        val subLord: Planet,
        val subSubLord: Planet,
        val supportiveHouses: List<Int>,
        val obstructiveHouses: List<Int>,
        val verdict: String
    )

    data class KpHouseGatekeeper(
        val house: Int,
        val gatekeeper: Planet,
        val promisedBy: List<Int>,
        val blockedBy: List<Int>,
        val decision: String
    )

    data class MuhurtaOptimizationResult(
        val activity: ActivityType,
        val dateRangeStart: LocalDate,
        val dateRangeEnd: LocalDate,
        val bestWindows: List<MuhurtaWindow>
    )

    data class MuhurtaWindow(
        val dateTime: LocalDateTime,
        val score: Int,
        val nakshatra: Nakshatra,
        val tithi: String,
        val notes: String
    )

    data class IshtaDevataResult(
        val atmakaraka: Planet,
        val karakamshaSign: ZodiacSign,
        val ishtaDevata: String,
        val guidingPlanet: Planet,
        val birthAkshara: String,
        val mantraSeed: String,
        val generatedMantra: String,
        val rationale: String
    )

    data class SarvatobhadraVedhaResult(
        val overallScore: Int,
        val strongestVedhas: List<String>,
        val favorableDays: List<String>,
        val recommendations: List<String>
    )

    data class PanchapakshiResult(
        val birthBird: PanchapakshiBird,
        val currentActivity: PanchapakshiActivity,
        val activityStrength: Int,
        val nextTransitions: List<PanchapakshiTransition>,
        val interpretation: String
    )

    enum class PanchapakshiBird { VULTURE, OWL, CROW, COCK, PEACOCK }

    enum class PanchapakshiActivity(val strength: Int) {
        RULING(100),
        EATING(82),
        WALKING(64),
        DREAMING(38),
        DYING(14)
    }

    data class PanchapakshiTransition(
        val startsAt: LocalDateTime,
        val activity: PanchapakshiActivity
    )

    data class VargaDeityResult(
        val highlights: List<VargaDeityPlacement>,
        val summary: String
    )

    data class VargaDeityPlacement(
        val chartType: DivisionalChartType,
        val planet: Planet,
        val sign: ZodiacSign,
        val deity: String,
        val shakti: String,
        val interpretation: String
    )

    data class ChakraHealthResult(
        val chakras: List<ChakraState>,
        val strongest: ChakraState,
        val weakest: ChakraState
    )

    data class ChakraState(
        val name: String,
        val rulers: List<Planet>,
        val score: Int,
        val status: String,
        val interpretation: String
    )

    data class CustomYogaMatch(
        val name: String,
        val sanskritName: String,
        val description: String,
        val effects: String,
        val matchedConditions: List<String>
    )

    data class KarmicNodeResult(
        val bhriguBinduLongitude: Double,
        val karmicWeight: Int,
        val activatedFactors: List<String>,
        val divisionalEchoes: List<String>,
        val interpretation: String
    )

    data class SkyViewResult(
        val localSiderealTimeHours: Double,
        val bodies: List<SkyBody>
    )

    data class SkyBody(
        val planet: Planet,
        val azimuth: Double,
        val altitude: Double,
        val declination: Double,
        val rightAscension: Double,
        val visible: Boolean
    )

    fun analyze(
        context: Context,
        chart: VedicChart,
        evaluationDateTime: LocalDateTime = nowForChart(chart),
        customYogaDsl: String? = null
    ): OracleAnalysis {
        val zoneId = resolveZoneId(chart.birthData.timezone)
        val transitPositions = calculateTransitPositions(context, chart, evaluationDateTime)
        val dashaTimeline = DashaCalculator.calculateDashaTimeline(chart)
        val ashtakavarga = AshtakavargaCalculator.calculateAshtakavarga(chart)

        val triplePillar = buildTriplePillar(context, chart, dashaTimeline, ashtakavarga, zoneId, evaluationDateTime.toLocalDate())
        val bnn = buildBhriguNandiNadi(chart)
        val kp = buildKpSystem(chart)
        val muhurta = buildMuhurtaOptimization(context, chart, evaluationDateTime.toLocalDate())
        val ishta = buildIshtaDevata(chart)
        val sarvatobhadra = buildSarvatobhadra(chart, transitPositions, evaluationDateTime.toLocalDate())
        val panchapakshi = buildPanchapakshi(chart, evaluationDateTime, zoneId)
        val vargaDeities = buildVargaDeities(chart)
        val chakraHealth = buildChakraHealth(context, chart)
        val customYogas = evaluateCustomYogas(chart, customYogaDsl)
        val karmicNode = buildKarmicNode(chart, transitPositions, evaluationDateTime.toLocalDate())
        val skyView = buildSkyView(chart, evaluationDateTime, zoneId)

        return OracleAnalysis(
            generatedAt = evaluationDateTime,
            triplePillar = triplePillar,
            bhriguNandiNadi = bnn,
            kpSystem = kp,
            muhurtaOptimization = muhurta,
            ishtaDevata = ishta,
            sarvatobhadraVedha = sarvatobhadra,
            panchapakshi = panchapakshi,
            vargaDeities = vargaDeities,
            chakraHealth = chakraHealth,
            customYogas = customYogas,
            karmicNode = karmicNode,
            skyView = skyView
        )
    }

    private fun buildTriplePillar(
        context: Context,
        chart: VedicChart,
        dashaTimeline: DashaCalculator.DashaTimeline,
        ashtakavarga: AshtakavargaCalculator.AshtakavargaAnalysis,
        zoneId: ZoneId,
        startDate: LocalDate
    ): TriplePillarResult {
        val ascSign = ZodiacSign.fromLongitude(chart.ascendant)
        val timeline = (0..59).map { offset ->
            val date = startDate.plusDays(offset.toLong())
            val sampleTime = LocalDateTime.of(date, LocalTime.NOON)
            val md = dashaTimeline.mahadashas.find { it.isActiveOn(sampleTime) } ?: dashaTimeline.currentMahadasha
            val ad = md?.getAntardashaOn(sampleTime) ?: dashaTimeline.currentAntardasha
            val transitPositions = calculateTransitPositions(context, chart, sampleTime)
            val dashaLord = md?.planet ?: dashaTimeline.birthNakshatraLord
            val antarLord = ad?.planet
            val dashaLordNatal = chart.planetPositions.firstOrNull { it.planet == dashaLord }
            val antarNatal = antarLord?.let { lord -> chart.planetPositions.firstOrNull { it.planet == lord } }
            val dashaTransit = transitPositions.firstOrNull { it.planet == dashaLord }
            val antarTransit = antarLord?.let { lord -> transitPositions.firstOrNull { it.planet == lord } }

            val dashaSupport = listOfNotNull(dashaLordNatal, antarNatal).map { natal ->
                val dignityScore = when (VedicAstrologyUtils.getDignity(natal)) {
                    VedicAstrologyUtils.PlanetaryDignity.EXALTED -> 1.0
                    VedicAstrologyUtils.PlanetaryDignity.MOOLATRIKONA -> 0.92
                    VedicAstrologyUtils.PlanetaryDignity.OWN_SIGN -> 0.86
                    VedicAstrologyUtils.PlanetaryDignity.FRIEND_SIGN -> 0.72
                    VedicAstrologyUtils.PlanetaryDignity.NEUTRAL_SIGN -> 0.55
                    VedicAstrologyUtils.PlanetaryDignity.ENEMY_SIGN -> 0.35
                    VedicAstrologyUtils.PlanetaryDignity.DEBILITATED -> 0.18
                }
                val houseBonus = when (natal.house) {
                    1, 4, 5, 7, 9, 10 -> 0.18
                    2, 3, 11 -> 0.1
                    else -> -0.08
                }
                (dignityScore + houseBonus).coerceIn(0.0, 1.0)
            }.average().ifNaN { 0.5 }

            val transitSupport = listOfNotNull(dashaTransit, antarTransit).map { transit ->
                val houseFromLagna = houseFromSign(transit.sign, ascSign)
                val houseWeight = when (houseFromLagna) {
                    1, 2, 3, 5, 7, 9, 10, 11 -> 1.0
                    4, 6 -> 0.68
                    else -> 0.34
                }
                val speedBonus = if (transit.isRetrograde) 0.08 else 0.0
                (houseWeight + speedBonus).coerceIn(0.0, 1.0)
            }.average().ifNaN { 0.45 }

            val ashtakavargaSupport = listOfNotNull(dashaTransit, antarTransit).map { transit ->
                val score = ashtakavarga.getTransitScore(transit.planet, transit.sign)
                (((score.binduScore / 8.0) * 0.55) + ((score.savScore / 40.0) * 0.45)).coerceIn(0.0, 1.0)
            }.average().ifNaN { 0.45 }

            val successProbability = ((dashaSupport * 0.34 + transitSupport * 0.28 + ashtakavargaSupport * 0.38) * 100).roundToInt()
            val activatedHouses = listOfNotNull(dashaTransit, antarTransit).map { houseFromSign(it.sign, ascSign) }.distinct().sorted()

            ProbabilityPoint(
                date = date,
                dashaLord = dashaLord,
                antardashaLord = antarLord,
                transitSupport = transitSupport,
                ashtakavargaSupport = ashtakavargaSupport,
                dashaSupport = dashaSupport,
                successProbability = successProbability,
                activatedHouses = activatedHouses,
                summary = buildString {
                    append(dashaLord.displayName)
                    antarLord?.let { append("-").append(it.displayName) }
                    append(" day with high-Bindu activation in houses ")
                    append(activatedHouses.joinToString(", "))
                }
            )
        }

        val peaks = timeline.sortedByDescending { it.successProbability }.take(7)
        val summary = buildString {
            append("The strongest synthesis windows occur when ")
            append(peaks.firstOrNull()?.dashaLord?.displayName ?: dashaTimeline.birthNakshatraLord.displayName)
            append(" receives supportive transit placement and its Bhinnashtakavarga bindus stay elevated.")
        }

        return TriplePillarResult(
            currentMahadasha = dashaTimeline.currentMahadasha?.planet,
            currentAntardasha = dashaTimeline.currentAntardasha?.planet,
            timeline = timeline,
            peakWindows = peaks,
            summary = summary
        )
    }

    private fun buildBhriguNandiNadi(chart: VedicChart): BhriguNandiNadiResult {
        val positions = chart.planetPositions.filter { it.planet in Planet.MAIN_PLANETS }
        val links = mutableListOf<NadiLink>()
        positions.forEach { source ->
            positions.filter { it.planet != source.planet }.forEach { target ->
                val distance = houseFromSign(target.sign, source.sign)
                if (distance in BNN_ASPECT_DISTANCES) {
                    val mutuality = if (abs((source.longitude - target.longitude + 540.0) % 360.0 - 180.0) <= 8.0) 0.12 else 0.0
                    val weight = (1.0 - (abs(distance - 7) / 12.0) + mutuality).coerceIn(0.15, 1.0)
                    links += NadiLink(
                        from = source.planet,
                        to = target.planet,
                        signDistance = distance,
                        relation = when (distance) {
                            1 -> "sign handshake"
                            2 -> "12/2 nadi linkage"
                            5 -> "trinal nadi projection"
                            7 -> "direct handshake"
                            9 -> "dharma linkage"
                            12 -> "subtle carry-forward"
                            else -> "nadi link"
                        },
                        weight = weight
                    )
                }
            }
        }

        val adjacency = links.groupBy { it.from }
        val chains = mutableListOf<NadiChain>()
        positions.map { it.planet }.distinct().forEach { start ->
            traverseNadi(start, adjacency, mutableListOf(start), 0.0, chains)
        }

        val topChains = chains.distinctBy { it.planets }.sortedByDescending { it.cumulativeWeight }.take(6)
        val themes = topChains.take(4).map { chain ->
            chain.planets.joinToString(" → ") { it.displayName }
        }

        return BhriguNandiNadiResult(
            links = links.sortedByDescending { it.weight }.take(18),
            chains = topChains,
            dominantThemes = themes
        )
    }

    private fun traverseNadi(
        current: Planet,
        adjacency: Map<Planet, List<NadiLink>>,
        path: MutableList<Planet>,
        weight: Double,
        output: MutableList<NadiChain>
    ) {
        if (path.size >= 3) {
            output += NadiChain(
                planets = path.toList(),
                cumulativeWeight = weight,
                interpretation = "Nadi chain through ${path.joinToString(" → ") { it.displayName }} concentrates results across linked signs."
            )
        }
        if (path.size == 4) return

        adjacency[current].orEmpty().forEach { link ->
            if (link.to in path) return@forEach
            path += link.to
            traverseNadi(link.to, adjacency, path, weight + link.weight, output)
            path.removeAt(path.lastIndex)
        }
    }

    private fun buildKpSystem(chart: VedicChart): KpResult {
        val cusps = chart.houseCusps.take(12).mapIndexed { index, cusp ->
            val sign = ZodiacSign.fromLongitude(cusp)
            val (nakshatra, _) = Nakshatra.fromLongitude(cusp)
            val subLord = resolveKpSubdivision(cusp, nakshatra.ruler)
            val subSubLord = resolveKpSubdivision(cusp, subLord)
            val signLord = sign.ruler
            val supportive = significatorHouses(chart, subLord).filter { it in KP_SUPPORTIVE_HOUSES[index + 1].orEmpty() }
            val obstructive = significatorHouses(chart, subLord).filter { it in KP_OBSTRUCTIVE_HOUSES[index + 1].orEmpty() }
            KpCusp(
                house = index + 1,
                cuspLongitude = cusp,
                sign = sign,
                signLord = signLord,
                starLord = nakshatra.ruler,
                subLord = subLord,
                subSubLord = subSubLord,
                supportiveHouses = supportive.distinct().sorted(),
                obstructiveHouses = obstructive.distinct().sorted(),
                verdict = if (supportive.size >= obstructive.size) "Promise supported" else "Promise delayed or contradicted"
            )
        }

        val gatekeepers = cusps.map { cusp ->
            KpHouseGatekeeper(
                house = cusp.house,
                gatekeeper = cusp.subLord,
                promisedBy = cusp.supportiveHouses,
                blockedBy = cusp.obstructiveHouses,
                decision = if (cusp.supportiveHouses.size >= cusp.obstructiveHouses.size) {
                    "Sub-lord ${cusp.subLord.displayName} opens the gate for house ${cusp.house} matters"
                } else {
                    "Sub-lord ${cusp.subLord.displayName} withholds the result until obstructions clear"
                }
            )
        }

        return KpResult(
            cusps = cusps,
            houseGatekeepers = gatekeepers,
            highlights = gatekeepers.sortedByDescending { it.promisedBy.size - it.blockedBy.size }.take(5).map { it.decision }
        )
    }

    private fun buildMuhurtaOptimization(
        context: Context,
        chart: VedicChart,
        startDate: LocalDate
    ): MuhurtaOptimizationResult {
        val endDate = startDate.plusDays(14)
        val calculator = MuhurtaCalculator(context)
        return try {
            val results = calculator.findAuspiciousMuhurtas(
                activity = ActivityType.VEHICLE,
                startDate = startDate,
                endDate = endDate,
                latitude = chart.birthData.latitude,
                longitude = chart.birthData.longitude,
                timezone = chart.birthData.timezone,
                minScore = 78
            )
            MuhurtaOptimizationResult(
                activity = ActivityType.VEHICLE,
                dateRangeStart = startDate,
                dateRangeEnd = endDate,
                bestWindows = results.take(8).map {
                    MuhurtaWindow(
                        dateTime = it.dateTime,
                        score = it.score,
                        nakshatra = it.nakshatra,
                        tithi = it.tithi,
                        notes = (it.reasons + it.warnings).joinToString(" • ")
                    )
                }
            )
        } finally {
            calculator.close()
        }
    }

    private fun buildIshtaDevata(chart: VedicChart): IshtaDevataResult {
        val jaimini = JaiminiKarakaCalculator.calculateKarakas(chart)
        val atmakaraka = jaimini.getAtmakaraka()?.planet ?: error("Atmakaraka unavailable")
        val navamsaPosition = jaimini.karakamsha.atmakarakaNavamsaPosition
        val guidingPlanet = navamsaPosition.sign.ruler
        val birthMoon = chart.planetPositions.first { it.planet == Planet.MOON }
        val akshara = NAKSHATRA_AKSHARAS[birthMoon.nakshatra]?.getOrNull((birthMoon.nakshatraPada - 1).coerceIn(0, 3)) ?: "Om"
        val beeja = PLANETARY_BEEJAS[guidingPlanet] ?: "Hreem"
        val deity = PLANETARY_DEVATAS[guidingPlanet] ?: navamsaPosition.nakshatra.deity
        val dignityTone = when (VedicAstrologyUtils.getDignity(navamsaPosition)) {
            VedicAstrologyUtils.PlanetaryDignity.EXALTED -> "Maha"
            VedicAstrologyUtils.PlanetaryDignity.MOOLATRIKONA,
            VedicAstrologyUtils.PlanetaryDignity.OWN_SIGN,
            VedicAstrologyUtils.PlanetaryDignity.FRIEND_SIGN -> "Shri"
            VedicAstrologyUtils.PlanetaryDignity.NEUTRAL_SIGN -> "Om"
            VedicAstrologyUtils.PlanetaryDignity.ENEMY_SIGN,
            VedicAstrologyUtils.PlanetaryDignity.DEBILITATED -> "Shanti"
        }
        val mantra = "$dignityTone $akshara $beeja ${deity.replace(" ", " ")} Namah"
            .replace("  ", " ")

        return IshtaDevataResult(
            atmakaraka = atmakaraka,
            karakamshaSign = jaimini.karakamsha.karakamshaSign,
            ishtaDevata = deity,
            guidingPlanet = guidingPlanet,
            birthAkshara = akshara,
            mantraSeed = beeja,
            generatedMantra = mantra,
            rationale = "Atmakaraka ${atmakaraka.displayName} in Navamsa ${navamsaPosition.sign.displayName} routes devotion through ${guidingPlanet.displayName} and the ${navamsaPosition.nakshatra.displayName} current."
        )
    }

    private fun buildSarvatobhadra(
        chart: VedicChart,
        transitPositions: List<PlanetPosition>,
        currentDate: LocalDate
    ): SarvatobhadraVedhaResult {
        val analysis = SarvatobhadraChakraCalculator.analyzeSarvatobhadra(chart, transitPositions, currentDate)
        return if (analysis == null) {
            SarvatobhadraVedhaResult(0, emptyList(), emptyList(), emptyList())
        } else {
            SarvatobhadraVedhaResult(
                overallScore = analysis.overallTransitScore,
                strongestVedhas = analysis.currentTransitVedhas.sortedByDescending { it.vedhaType.severity }.take(5).map {
                    "${it.transitingPlanet.displayName} on ${it.nakshatra.displayName}: ${it.interpretation}"
                },
                favorableDays = analysis.favorableDays.map { day -> day.name.lowercase().replaceFirstChar { it.uppercase() } },
                recommendations = analysis.recommendations.take(5)
            )
        }
    }

    private fun buildPanchapakshi(
        chart: VedicChart,
        evaluationDateTime: LocalDateTime,
        zoneId: ZoneId
    ): PanchapakshiResult {
        val moon = chart.planetPositions.first { it.planet == Planet.MOON }
        val pakshaOffset = if (lunarPaksha(chart) == "Shukla") 0 else 2
        val bird = PANCHAPAKSHI_BIRDS[(moon.nakshatra.number - 1 + pakshaOffset) % PANCHAPAKSHI_BIRDS.size]
        val localDateTime = evaluationDateTime.atZone(zoneId).toLocalDateTime()
        val dayIndex = localDateTime.dayOfWeek.ordinal
        val minuteOfDay = localDateTime.hour * 60 + localDateTime.minute
        val segment = (minuteOfDay / 144).coerceIn(0, 9)
        val activity = PANCHAPAKSHI_ACTIVITIES[(segment + dayIndex + bird.ordinal) % PANCHAPAKSHI_ACTIVITIES.size]
        val transitions = (1..5).map { step ->
            val nextMinute = (((segment + step) * 144) % 1440)
            PanchapakshiTransition(
                startsAt = LocalDateTime.of(localDateTime.toLocalDate(), LocalTime.of(nextMinute / 60, nextMinute % 60)),
                activity = PANCHAPAKSHI_ACTIVITIES[(segment + step + dayIndex + bird.ordinal) % PANCHAPAKSHI_ACTIVITIES.size]
            )
        }

        return PanchapakshiResult(
            birthBird = bird,
            currentActivity = activity,
            activityStrength = activity.strength,
            nextTransitions = transitions,
            interpretation = "${bird.name.lowercase().replaceFirstChar { it.uppercase() }} is currently in ${activity.name.lowercase()} mode, so decisive actions should align with the stronger transitions rather than the weaker dying or dreaming intervals."
        )
    }

    private fun buildVargaDeities(chart: VedicChart): VargaDeityResult {
        val chartTypes = listOf(
            DivisionalChartType.D1_RASHI,
            DivisionalChartType.D9_NAVAMSA,
            DivisionalChartType.D10_DASAMSA,
            DivisionalChartType.D24_CHATURVIMSAMSA,
            DivisionalChartType.D60_SHASHTIAMSA
        )
        val highlights = mutableListOf<VargaDeityPlacement>()

        chartTypes.forEach { type ->
            val divisional = DivisionalChartCalculator.calculateDivisionalChart(chart, type)
            divisional.planetPositions.filter { it.planet in Planet.MAIN_PLANETS }.take(5).forEach { pos ->
                val deity = when (type) {
                    DivisionalChartType.D10_DASAMSA -> D10_DIKPALAS[(pos.house - 1).coerceIn(0, D10_DIKPALAS.lastIndex)]
                    DivisionalChartType.D60_SHASHTIAMSA -> D60_DEITIES[amshaNumber(pos.longitude, 60) - 1]
                    else -> pos.nakshatra.deity
                }
                val shakti = when (type) {
                    DivisionalChartType.D1_RASHI -> "Embodied karma"
                    DivisionalChartType.D9_NAVAMSA -> "Dharma refinement"
                    DivisionalChartType.D10_DASAMSA -> "Directional authority"
                    DivisionalChartType.D24_CHATURVIMSAMSA -> "Jnana shakti"
                    DivisionalChartType.D60_SHASHTIAMSA -> "Karmic seed"
                    else -> "Subtle shakti"
                }
                highlights += VargaDeityPlacement(
                    chartType = type,
                    planet = pos.planet,
                    sign = pos.sign,
                    deity = deity,
                    shakti = shakti,
                    interpretation = "${pos.planet.displayName} in ${type.shortName} channels ${deity} through ${pos.sign.displayName}, emphasizing $shakti."
                )
            }
        }

        return VargaDeityResult(
            highlights = highlights.take(16),
            summary = "D10 directional deities show worldly execution, while D60 deity currents expose the karmic texture beneath the visible chart."
        )
    }

    private fun buildChakraHealth(context: Context, chart: VedicChart): ChakraHealthResult {
        val shadbala = ShadbalaCalculator.calculateShadbala(context, chart)
        val chakras = CHAKRA_RULERS.map { (name, rulers) ->
            val positions = rulers.mapNotNull { planet -> chart.planetPositions.firstOrNull { it.planet == planet } }
            val shadbalaScores = rulers.mapNotNull { planet -> shadbala.planetaryStrengths[planet]?.percentageOfRequired }
            val dignityBonus = positions.map {
                when (VedicAstrologyUtils.getDignity(it)) {
                    VedicAstrologyUtils.PlanetaryDignity.EXALTED -> 18.0
                    VedicAstrologyUtils.PlanetaryDignity.MOOLATRIKONA -> 14.0
                    VedicAstrologyUtils.PlanetaryDignity.OWN_SIGN -> 10.0
                    VedicAstrologyUtils.PlanetaryDignity.FRIEND_SIGN -> 6.0
                    VedicAstrologyUtils.PlanetaryDignity.NEUTRAL_SIGN -> 0.0
                    VedicAstrologyUtils.PlanetaryDignity.ENEMY_SIGN -> -8.0
                    VedicAstrologyUtils.PlanetaryDignity.DEBILITATED -> -16.0
                }
            }.average().ifNaN { 0.0 }
            val score = (shadbalaScores.average().ifNaN { 50.0 } + dignityBonus).roundToInt().coerceIn(0, 100)
            ChakraState(
                name = name,
                rulers = rulers,
                score = score,
                status = when {
                    score >= 78 -> "Flourishing"
                    score >= 60 -> "Balanced"
                    score >= 42 -> "Sensitive"
                    else -> "Depleted"
                },
                interpretation = "$name responds to ${rulers.joinToString("/") { it.displayName }}; current planetary strength scores it at $score/100."
            )
        }

        val strongest = chakras.maxBy { it.score }
        val weakest = chakras.minBy { it.score }
        return ChakraHealthResult(chakras = chakras, strongest = strongest, weakest = weakest)
    }

    private fun evaluateCustomYogas(chart: VedicChart, customYogaDsl: String?): List<CustomYogaMatch> {
        val source = customYogaDsl?.ifBlank { null } ?: DEFAULT_YOGA_RULES_JSON
        val root = JSONObject(source)
        val rules = root.getJSONArray("rules")
        val aspectMatrix = AspectCalculator.calculateAspectMatrix(chart)
        val matches = mutableListOf<CustomYogaMatch>()

        for (index in 0 until rules.length()) {
            val rule = rules.getJSONObject(index)
            val conditions = rule.getJSONArray("conditions")
            val matchedConditions = mutableListOf<String>()
            var allMatched = true
            for (conditionIndex in 0 until conditions.length()) {
                val condition = conditions.getJSONObject(conditionIndex)
                val description = evaluateRuleCondition(chart, aspectMatrix, condition)
                if (description == null) {
                    allMatched = false
                    break
                }
                matchedConditions += description
            }
            if (allMatched) {
                matches += CustomYogaMatch(
                    name = rule.getString("name"),
                    sanskritName = rule.optString("sanskritName", rule.getString("name")),
                    description = rule.optString("description"),
                    effects = rule.optString("effects"),
                    matchedConditions = matchedConditions
                )
            }
        }

        return matches
    }

    private fun evaluateRuleCondition(
        chart: VedicChart,
        aspectMatrix: AspectCalculator.AspectMatrix,
        condition: JSONObject
    ): String? {
        return when (condition.getString("type")) {
            "planet_in_house" -> {
                val planet = Planet.valueOf(condition.getString("planet"))
                val house = condition.getInt("house")
                chart.planetPositions.firstOrNull { it.planet == planet && it.house == house }?.let {
                    "${planet.displayName} occupies house $house"
                }
            }
            "planet_in_sign" -> {
                val planet = Planet.valueOf(condition.getString("planet"))
                val sign = ZodiacSign.valueOf(condition.getString("sign"))
                chart.planetPositions.firstOrNull { it.planet == planet && it.sign == sign }?.let {
                    "${planet.displayName} occupies ${sign.displayName}"
                }
            }
            "planet_aspects_planet" -> {
                val planet = Planet.valueOf(condition.getString("planet"))
                val target = Planet.valueOf(condition.getString("target"))
                aspectMatrix.getAspectBetween(planet, target)?.let {
                    "${planet.displayName} aspects ${target.displayName}"
                }
            }
            "planets_conjunct" -> {
                val first = Planet.valueOf(condition.getString("planet"))
                val second = Planet.valueOf(condition.getString("target"))
                aspectMatrix.conjunctions.firstOrNull { it.aspectingPlanet == first && it.aspectedPlanet == second }?.let {
                    "${first.displayName} conjoins ${second.displayName}"
                }
            }
            "house_lord_in_house" -> {
                val sourceHouse = condition.getInt("sourceHouse")
                val targetHouse = condition.getInt("targetHouse")
                val ascSign = ZodiacSign.fromLongitude(chart.ascendant)
                val sourceSign = signFromAscendant(ascSign, sourceHouse)
                val lord = sourceSign.ruler
                chart.planetPositions.firstOrNull { it.planet == lord && it.house == targetHouse }?.let {
                    "${sourceHouse}th lord ${lord.displayName} sits in house $targetHouse"
                }
            }
            "planet_has_dignity" -> {
                val planet = Planet.valueOf(condition.getString("planet"))
                val dignity = VedicAstrologyUtils.PlanetaryDignity.valueOf(condition.getString("dignity"))
                chart.planetPositions.firstOrNull { it.planet == planet && VedicAstrologyUtils.getDignity(it) == dignity }?.let {
                    "${planet.displayName} is ${dignity.name.lowercase().replace('_', ' ')}"
                }
            }
            else -> null
        }
    }

    private fun buildKarmicNode(
        chart: VedicChart,
        transitPositions: List<PlanetPosition>,
        currentDate: LocalDate
    ): KarmicNodeResult {
        val bhrigu = BhriguBinduCalculator.analyzeBhriguBindu(chart, currentDate, transitPositions)
        val d9 = DivisionalChartCalculator.calculateDivisionalChart(chart, DivisionalChartType.D9_NAVAMSA)
        val d60 = DivisionalChartCalculator.calculateDivisionalChart(chart, DivisionalChartType.D60_SHASHTIAMSA)
        val natalRahu = chart.planetPositions.first { it.planet == Planet.RAHU }
        val natalKetu = chart.planetPositions.first { it.planet == Planet.KETU }
        val transitRahu = transitPositions.firstOrNull { it.planet == Planet.RAHU }
        val transitKetu = transitPositions.firstOrNull { it.planet == Planet.KETU }
        val activations = mutableListOf<String>()

        if (transitRahu != null && angularDistance(transitRahu.longitude, bhrigu.bhriguBindu) <= 6.0) {
            activations += "Transit Rahu is activating the natal Bhrigu Bindu"
        }
        if (transitKetu != null && angularDistance(transitKetu.longitude, bhrigu.bhriguBindu) <= 6.0) {
            activations += "Transit Ketu is completing a karmic release over Bhrigu Bindu"
        }
        if (angularDistance(natalRahu.longitude, natalKetu.longitude) == 180.0) {
            activations += "Natal nodal axis is cleanly polarized, amplifying destiny-style swings"
        }

        val divisionalEchoes = listOfNotNull(
            d9.planetPositions.firstOrNull { it.planet == Planet.RAHU }?.let { "D9 Rahu in ${it.sign.displayName}" },
            d9.planetPositions.firstOrNull { it.planet == Planet.KETU }?.let { "D9 Ketu in ${it.sign.displayName}" },
            d60.planetPositions.firstOrNull { it.planet == Planet.RAHU }?.let { "D60 Rahu in ${it.sign.displayName}" },
            d60.planetPositions.firstOrNull { it.planet == Planet.KETU }?.let { "D60 Ketu in ${it.sign.displayName}" }
        )

        val strengthScore = when (bhrigu.strengthAssessment.overallStrength) {
            BhriguBinduCalculator.OverallStrength.EXCELLENT -> 78
            BhriguBinduCalculator.OverallStrength.GOOD -> 66
            BhriguBinduCalculator.OverallStrength.MODERATE -> 52
            BhriguBinduCalculator.OverallStrength.CHALLENGING -> 38
            BhriguBinduCalculator.OverallStrength.DIFFICULT -> 26
        }
        val karmicWeight = (strengthScore + activations.size * 9 + divisionalEchoes.size * 6).coerceIn(0, 100)
        return KarmicNodeResult(
            bhriguBinduLongitude = bhrigu.bhriguBindu,
            karmicWeight = karmicWeight,
            activatedFactors = activations.ifEmpty { listOf("Nodes are present but not tightly triggering the destiny midpoint today") },
            divisionalEchoes = divisionalEchoes,
            interpretation = "Rahu-Ketu pressure is strongest when nodal transits repeatedly echo the Bhrigu Bindu and the same axis reappears in D9/D60."
        )
    }

    private fun buildSkyView(
        chart: VedicChart,
        evaluationDateTime: LocalDateTime,
        zoneId: ZoneId
    ): SkyViewResult {
        val lst = calculateLocalSiderealTime(evaluationDateTime.atZone(zoneId), chart.birthData.longitude)
        val bodies = chart.planetPositions.filter { it.planet in Planet.MAIN_PLANETS && it.planet != Planet.KETU }.map { pos ->
            val tropicalLongitude = normalizeLongitude(pos.longitude + chart.ayanamsa)
            val equatorial = eclipticToEquatorial(tropicalLongitude, pos.latitude)
            val horizontal = equatorialToHorizontal(
                rightAscension = equatorial.first,
                declination = equatorial.second,
                localSiderealTimeHours = lst,
                latitude = chart.birthData.latitude
            )
            SkyBody(
                planet = pos.planet,
                azimuth = horizontal.first,
                altitude = horizontal.second,
                declination = equatorial.second,
                rightAscension = equatorial.first,
                visible = horizontal.second > 0.0
            )
        }.sortedByDescending { it.altitude }

        return SkyViewResult(localSiderealTimeHours = lst, bodies = bodies)
    }

    private fun calculateTransitPositions(context: Context, chart: VedicChart, dateTime: LocalDateTime): List<PlanetPosition> {
        val zoneId = resolveZoneId(chart.birthData.timezone)
        val engine = SwissEphemerisEngine.getInstance(context)
        val transitBirthData = BirthData(
            name = "Transit",
            dateTime = dateTime,
            latitude = chart.birthData.latitude,
            longitude = chart.birthData.longitude,
            timezone = zoneId.id,
            location = chart.birthData.location
        )
        return engine.calculateVedicChart(transitBirthData).planetPositions
    }

    private fun resolveZoneId(timezone: String): ZoneId {
        return TimezoneSanitizer.resolveZoneIdOrNull(timezone) ?: ZoneOffset.UTC
    }

    private fun houseFromSign(target: ZodiacSign, reference: ZodiacSign): Int {
        return ((target.number - reference.number + 12) % 12) + 1
    }

    private fun signFromAscendant(ascendant: ZodiacSign, house: Int): ZodiacSign {
        val index = ((ascendant.ordinal + house - 1) % 12 + 12) % 12
        return ZodiacSign.entries[index]
    }

    private fun significatorHouses(chart: VedicChart, planet: Planet): List<Int> {
        val pos = chart.planetPositions.firstOrNull { it.planet == planet }
        val asc = ZodiacSign.fromLongitude(chart.ascendant)
        val owned = ZodiacSign.entries.filter { it.ruler == planet }.map { houseFromSign(it, asc) }
        return listOfNotNull(pos?.house) + owned
    }

    private fun resolveKpSubdivision(longitude: Double, startingLord: Planet): Planet {
        val (nakshatra, _) = Nakshatra.fromLongitude(longitude)
        val positionInNakshatra = ((normalizeLongitude(longitude) - nakshatra.startDegree) + 360.0) % (360.0 / 27.0)
        val sequence = rotatedDashaSequence(startingLord)
        var cursor = 0.0
        sequence.forEach { lord ->
            val span = (DASHA_YEARS[lord] ?: 0.0) / 120.0 * NAKSHATRA_SPAN
            cursor += span
            if (positionInNakshatra <= cursor + 1e-8) return lord
        }
        return sequence.last()
    }

    private fun rotatedDashaSequence(start: Planet): List<Planet> {
        val startIndex = DASHA_SEQUENCE.indexOf(start).coerceAtLeast(0)
        return DASHA_SEQUENCE.drop(startIndex) + DASHA_SEQUENCE.take(startIndex)
    }

    private fun amshaNumber(longitude: Double, division: Int): Int {
        val degreeInSign = normalizeLongitude(longitude) % 30.0
        val span = 30.0 / division
        return (floor(degreeInSign / span).toInt() + 1).coerceIn(1, division)
    }

    private fun lunarPaksha(chart: VedicChart): String {
        val sun = chart.planetPositions.firstOrNull { it.planet == Planet.SUN } ?: return "Shukla"
        val moon = chart.planetPositions.firstOrNull { it.planet == Planet.MOON } ?: return "Shukla"
        val phase = normalizeLongitude(moon.longitude - sun.longitude)
        return if (phase < 180.0) "Shukla" else "Krishna"
    }

    private fun calculateLocalSiderealTime(dateTime: ZonedDateTime, longitude: Double): Double {
        val utc = dateTime.withZoneSameInstant(ZoneOffset.UTC)
        val jd = julianDay(utc)
        val t = (jd - 2451545.0) / 36525.0
        val gmst = 280.46061837 + 360.98564736629 * (jd - 2451545.0) + 0.000387933 * t * t - (t * t * t) / 38710000.0
        return normalizeLongitude(gmst + longitude) / 15.0
    }

    private fun julianDay(dateTime: ZonedDateTime): Double {
        val year = dateTime.year
        val month = dateTime.monthValue
        val day = dateTime.dayOfMonth + (dateTime.hour + (dateTime.minute + dateTime.second / 60.0) / 60.0) / 24.0
        var y = year
        var m = month
        if (m <= 2) {
            y -= 1
            m += 12
        }
        val a = floor(y / 100.0)
        val b = 2 - a + floor(a / 4.0)
        return floor(365.25 * (y + 4716)) + floor(30.6001 * (m + 1)) + day + b - 1524.5
    }

    private fun eclipticToEquatorial(longitude: Double, latitude: Double): Pair<Double, Double> {
        val eps = Math.toRadians(23.4367)
        val lon = Math.toRadians(longitude)
        val lat = Math.toRadians(latitude)
        val sinDec = sin(lat) * cos(eps) + cos(lat) * sin(eps) * sin(lon)
        val dec = asin(sinDec)
        val y = sin(lon) * cos(eps) - tan(lat) * sin(eps)
        val x = cos(lon)
        val ra = atan2(y, x)
        return Pair((Math.toDegrees(ra) / 15.0 + 24.0) % 24.0, Math.toDegrees(dec))
    }

    private fun equatorialToHorizontal(
        rightAscension: Double,
        declination: Double,
        localSiderealTimeHours: Double,
        latitude: Double
    ): Pair<Double, Double> {
        val hourAngle = Math.toRadians((localSiderealTimeHours - rightAscension) * 15.0)
        val dec = Math.toRadians(declination)
        val lat = Math.toRadians(latitude)
        val altitude = asin(sin(dec) * sin(lat) + cos(dec) * cos(lat) * cos(hourAngle))
        val azimuth = atan2(
            -sin(hourAngle),
            tan(dec) * cos(lat) - sin(lat) * cos(hourAngle)
        )
        return Pair((Math.toDegrees(azimuth) + 360.0) % 360.0, Math.toDegrees(altitude))
    }

    private fun angularDistance(a: Double, b: Double): Double {
        val diff = abs(normalizeLongitude(a) - normalizeLongitude(b))
        return minOf(diff, 360.0 - diff)
    }

    private fun normalizeLongitude(value: Double): Double {
        return (value % 360.0 + 360.0) % 360.0
    }

    private fun nowForChart(chart: VedicChart): LocalDateTime {
        return LocalDateTime.now(resolveZoneId(chart.birthData.timezone))
    }

    private fun Double.ifNaN(fallback: () -> Double): Double = if (isNaN()) fallback() else this

    private val BNN_ASPECT_DISTANCES = setOf(1, 2, 5, 7, 9, 12)
    private const val NAKSHATRA_SPAN = 360.0 / 27.0
    private val DASHA_SEQUENCE = listOf(
        Planet.KETU, Planet.VENUS, Planet.SUN, Planet.MOON, Planet.MARS,
        Planet.RAHU, Planet.JUPITER, Planet.SATURN, Planet.MERCURY
    )
    private val DASHA_YEARS = mapOf(
        Planet.KETU to 7.0,
        Planet.VENUS to 20.0,
        Planet.SUN to 6.0,
        Planet.MOON to 10.0,
        Planet.MARS to 7.0,
        Planet.RAHU to 18.0,
        Planet.JUPITER to 16.0,
        Planet.SATURN to 19.0,
        Planet.MERCURY to 17.0
    )
    private val KP_SUPPORTIVE_HOUSES = mapOf(
        1 to listOf(1, 5, 9),
        2 to listOf(2, 6, 10, 11),
        3 to listOf(3, 6, 11),
        4 to listOf(4, 5, 11),
        5 to listOf(1, 5, 9, 11),
        6 to listOf(2, 6, 10, 11),
        7 to listOf(2, 7, 11),
        8 to listOf(8, 12),
        9 to listOf(1, 5, 9),
        10 to listOf(2, 6, 10, 11),
        11 to listOf(2, 6, 10, 11),
        12 to listOf(8, 12)
    )
    private val KP_OBSTRUCTIVE_HOUSES = mapOf(
        1 to listOf(6, 8, 12),
        2 to listOf(5, 8, 12),
        3 to listOf(8, 12),
        4 to listOf(8, 12),
        5 to listOf(6, 8, 12),
        6 to listOf(5, 8, 12),
        7 to listOf(6, 8, 12),
        8 to listOf(1, 5, 9),
        9 to listOf(6, 8, 12),
        10 to listOf(5, 8, 12),
        11 to listOf(5, 12),
        12 to listOf(2, 6, 10, 11)
    )
    private val PLANETARY_DEVATAS = mapOf(
        Planet.SUN to "Surya Narayana",
        Planet.MOON to "Gauri",
        Planet.MARS to "Skanda",
        Planet.MERCURY to "Vishnu",
        Planet.JUPITER to "Dakshinamurti",
        Planet.VENUS to "Lakshmi",
        Planet.SATURN to "Kala Bhairava",
        Planet.RAHU to "Durga",
        Planet.KETU to "Ganesha"
    )
    private val PLANETARY_BEEJAS = mapOf(
        Planet.SUN to "Hraam",
        Planet.MOON to "Shraam",
        Planet.MARS to "Kraam",
        Planet.MERCURY to "Braam",
        Planet.JUPITER to "Graam",
        Planet.VENUS to "Draam",
        Planet.SATURN to "Praam",
        Planet.RAHU to "Bhraam",
        Planet.KETU to "Sraam"
    )
    private val NAKSHATRA_AKSHARAS = mapOf(
        Nakshatra.ASHWINI to listOf("Chu", "Che", "Cho", "La"),
        Nakshatra.BHARANI to listOf("Li", "Lu", "Le", "Lo"),
        Nakshatra.KRITTIKA to listOf("A", "I", "U", "E"),
        Nakshatra.ROHINI to listOf("O", "Va", "Vi", "Vu"),
        Nakshatra.MRIGASHIRA to listOf("Ve", "Vo", "Ka", "Ki"),
        Nakshatra.ARDRA to listOf("Ku", "Gha", "Ing", "Cha"),
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
        Nakshatra.SHRAVANA to listOf("Ju", "Je", "Jo", "Khi"),
        Nakshatra.DHANISHTHA to listOf("Ga", "Gi", "Gu", "Ge"),
        Nakshatra.SHATABHISHA to listOf("Go", "Sa", "Si", "Su"),
        Nakshatra.PURVA_BHADRAPADA to listOf("Se", "So", "Da", "Di"),
        Nakshatra.UTTARA_BHADRAPADA to listOf("Du", "Tha", "Jha", "Da"),
        Nakshatra.REVATI to listOf("De", "Do", "Cha", "Chi")
    )
    private val PANCHAPAKSHI_BIRDS = listOf(
        PanchapakshiBird.VULTURE,
        PanchapakshiBird.OWL,
        PanchapakshiBird.CROW,
        PanchapakshiBird.COCK,
        PanchapakshiBird.PEACOCK
    )
    private val PANCHAPAKSHI_ACTIVITIES = listOf(
        PanchapakshiActivity.RULING,
        PanchapakshiActivity.EATING,
        PanchapakshiActivity.WALKING,
        PanchapakshiActivity.DREAMING,
        PanchapakshiActivity.DYING
    )
    private val D10_DIKPALAS = listOf(
        "Indra", "Agni", "Yama", "Nirrti", "Varuna",
        "Vayu", "Kubera", "Ishana", "Brahma", "Ananta"
    )
    private val D60_DEITIES = listOf(
        "Ghora", "Rakshasa", "Deva", "Kubera", "Yaksha", "Kinnara", "Bhrashta", "Kulaghna", "Garala", "Agni",
        "Maya", "Preta", "Purisha", "Apampati", "Marut", "Kala", "Sarpa", "Amrita", "Indu", "Mridu",
        "Komala", "Heramba", "Brahma", "Vishnu", "Maheshvara", "Deva", "Ardra", "Kalinasha", "Kshiti", "Kamala",
        "Gulika", "Mrityu", "Kala", "Davagni", "Ghora", "Yama", "Kantaka", "Sudha", "Amrita", "Purna",
        "Vishada", "Kulaghna", "Vamsha-kshaya", "Utpata", "Saumya", "Komala", "Sheetala", "Karala", "Dandayudha", "Nirmala",
        "Saubhagya", "Kula-nandana", "Lakshmi", "Vagisha", "Digambara", "Devesha", "Indurekha", "Pravin", "Kala-agni", "Moksha"
    )
    private val CHAKRA_RULERS = linkedMapOf(
        "Muladhara" to listOf(Planet.MARS, Planet.KETU),
        "Swadhisthana" to listOf(Planet.MOON, Planet.VENUS),
        "Manipura" to listOf(Planet.SUN, Planet.MARS),
        "Anahata" to listOf(Planet.VENUS, Planet.JUPITER),
        "Vishuddha" to listOf(Planet.MERCURY, Planet.SATURN),
        "Ajna" to listOf(Planet.JUPITER, Planet.RAHU),
        "Sahasrara" to listOf(Planet.SATURN, Planet.KETU)
    )
    private const val DEFAULT_YOGA_RULES_JSON = """
        {
          "rules": [
            {
              "name": "Guru-Mangala Yoga",
              "sanskritName": "Guru-Mangala Yoga",
              "description": "Jupiter and Mars combine wisdom with initiative.",
              "effects": "Supports enterprise, technical execution, and strategic courage.",
              "conditions": [
                { "type": "planet_aspects_planet", "planet": "MARS", "target": "JUPITER" }
              ]
            },
            {
              "name": "Lakshmi Prosperity Flow",
              "sanskritName": "Lakshmi Pravaha",
              "description": "9th and 10th house wealth dharma pattern.",
              "effects": "Supports fortune, patronage, and status accumulation.",
              "conditions": [
                { "type": "house_lord_in_house", "sourceHouse": 9, "targetHouse": 10 },
                { "type": "planet_has_dignity", "planet": "JUPITER", "dignity": "FRIEND_SIGN" }
              ]
            },
            {
              "name": "Royal Counsel Combination",
              "sanskritName": "Raja Mantri Yoga",
              "description": "Sun and Mercury produce leadership intelligence.",
              "effects": "Strengthens policy skill, advisory influence, and executive clarity.",
              "conditions": [
                { "type": "planets_conjunct", "planet": "SUN", "target": "MERCURY" }
              ]
            }
          ]
        }
    """
}
