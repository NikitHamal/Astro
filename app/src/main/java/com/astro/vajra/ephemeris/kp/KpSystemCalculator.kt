package com.astro.vajra.ephemeris.kp

import com.astro.vajra.core.model.Ayanamsa
import com.astro.vajra.core.model.HouseSystem
import com.astro.vajra.core.model.Nakshatra
import com.astro.vajra.core.model.Planet
import com.astro.vajra.core.model.PlanetPosition
import com.astro.vajra.core.model.VedicChart
import com.astro.vajra.core.model.ZodiacSign
import com.astro.vajra.data.preferences.NodeCalculationMode
import com.astro.vajra.ephemeris.SwissEphemerisEngine
import com.astro.vajra.ephemeris.VedicAstrologyUtils
import java.time.DayOfWeek
import java.time.LocalDateTime
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.math.floor
import kotlin.math.min

@Singleton
class KpSystemCalculator @Inject constructor(
    private val ephemerisEngine: SwissEphemerisEngine
) {

    companion object {
        private const val FULL_CIRCLE = 360.0
        private const val SIGN_SPAN = 30.0
        private const val NAKSHATRA_SPAN = FULL_CIRCLE / 27.0
        private const val EPSILON = 1e-9

        private val KP_SEQUENCE = listOf(
            Planet.KETU,
            Planet.VENUS,
            Planet.SUN,
            Planet.MOON,
            Planet.MARS,
            Planet.RAHU,
            Planet.JUPITER,
            Planet.SATURN,
            Planet.MERCURY
        )

        private val VIMSHOTTARI_YEARS = mapOf(
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

        private val KP_PLANETS = Planet.MAIN_PLANETS
    }

    private data class SegmentMatch(
        val lord: Planet,
        val startOffset: Double,
        val endOffset: Double
    )

    fun calculate(
        chart: VedicChart,
        analysisMoment: LocalDateTime
    ): KpAnalysisResult {
        val configuration = KpConfiguration(
            ayanamsa = Ayanamsa.KRISHNAMURTI,
            houseSystem = HouseSystem.PLACIDUS,
            nodeMode = NodeCalculationMode.MEAN
        )

        val kpChart = ephemerisEngine.calculateVedicChart(
            birthData = chart.birthData,
            houseSystem = configuration.houseSystem,
            ayanamsa = configuration.ayanamsa,
            nodeMode = configuration.nodeMode
        )

        val analysisChart = ephemerisEngine.calculateVedicChart(
            birthData = chart.birthData.copy(dateTime = analysisMoment),
            houseSystem = configuration.houseSystem,
            ayanamsa = configuration.ayanamsa,
            nodeMode = configuration.nodeMode
        )

        val kpPlanetPositions = kpChart.planetPositions
            .filter { it.planet in KP_PLANETS }
            .sortedBy { KP_PLANETS.indexOf(it.planet) }

        val positionsByPlanet = kpPlanetPositions.associateBy { it.planet }
        val subdivisionsByPlanet = kpPlanetPositions.associate { it.planet to subdivisionForLongitude(it.longitude) }
        val houseOwners = (1..12).associateWith { house ->
            ZodiacSign.fromLongitude(kpChart.houseCusps[house - 1]).ruler
        }
        val ownedHousesByPlanet = KP_PLANETS.associateWith { planet ->
            houseOwners.filterValues { it == planet }.keys.sorted()
        }

        val planetDetails = kpPlanetPositions.map { position ->
            buildPlanetDetail(
                position = position,
                subdivision = subdivisionsByPlanet.getValue(position.planet),
                positionsByPlanet = positionsByPlanet,
                ownedHousesByPlanet = ownedHousesByPlanet
            )
        }
        val planetDetailsByPlanet = planetDetails.associateBy { it.position.planet }

        val cuspDetails = kpChart.houseCusps.mapIndexed { index, cuspLongitude ->
            val house = index + 1
            val cuspSubdivision = subdivisionForLongitude(cuspLongitude)
            val sign = ZodiacSign.fromLongitude(cuspLongitude)
            val occupants = kpPlanetPositions
                .filter { it.house == house }
                .map { it.planet }
            KpCuspDetail(
                house = house,
                longitude = cuspLongitude,
                sign = sign,
                signLord = sign.ruler,
                subdivision = cuspSubdivision,
                occupants = occupants,
                ownerPlanets = listOf(sign.ruler),
                subLordLinkedHouses = planetDetailsByPlanet[cuspSubdivision.subLord]
                    ?.finalSignifiedHouses
                    ?.map { it.house }
                    .orEmpty()
            )
        }

        val houseSignificators = (1..12).map { house ->
            val cusp = cuspDetails[house - 1]
            val occupants = kpPlanetPositions
                .filter { it.house == house }
                .map { it.planet }
            val owners = listOf(houseOwners.getValue(house))
            val starOfOccupants = kpPlanetPositions
                .filter { subdivisionsByPlanet.getValue(it.planet).starLord in occupants }
                .map { it.planet }
                .distinct()
            val starOfOwners = kpPlanetPositions
                .filter { subdivisionsByPlanet.getValue(it.planet).starLord in owners }
                .map { it.planet }
                .distinct()
            val principal = when {
                starOfOccupants.isNotEmpty() -> starOfOccupants
                occupants.isNotEmpty() -> occupants
                starOfOwners.isNotEmpty() -> starOfOwners
                else -> owners
            }
            KpHouseSignificators(
                house = house,
                cuspStarLord = cusp.subdivision.starLord,
                cuspSubLord = cusp.subdivision.subLord,
                occupants = occupants,
                owners = owners,
                starOfOccupants = starOfOccupants,
                starOfOwners = starOfOwners,
                principalSignificators = principal,
                cuspSubLordLinkedHouses = cusp.subLordLinkedHouses
            )
        }

        return KpAnalysisResult(
            sourceChart = chart,
            kpChart = kpChart,
            configuration = configuration,
            cuspDetails = cuspDetails,
            planetDetails = planetDetails,
            houseSignificators = houseSignificators,
            birthRulingPlanets = calculateRulingPlanets(kpChart, chart.birthData.dateTime),
            analysisRulingPlanets = calculateRulingPlanets(analysisChart, analysisMoment),
            horaryNumbers = generateHoraryNumbers()
        )
    }

    private fun buildPlanetDetail(
        position: PlanetPosition,
        subdivision: KpSubdivision,
        positionsByPlanet: Map<Planet, PlanetPosition>,
        ownedHousesByPlanet: Map<Planet, List<Int>>
    ): KpPlanetDetail {
        val starLinkedHouses = linkHousesOfPlanet(
            sourcePlanet = subdivision.starLord,
            positionsByPlanet = positionsByPlanet,
            ownedHousesByPlanet = ownedHousesByPlanet
        )
        val subLinkedHouses = linkHousesOfPlanet(
            sourcePlanet = subdivision.subLord,
            positionsByPlanet = positionsByPlanet,
            ownedHousesByPlanet = ownedHousesByPlanet
        )

        val houseSourceMap = linkedHouseSources(
            position = position,
            subdivision = subdivision,
            positionsByPlanet = positionsByPlanet,
            ownedHousesByPlanet = ownedHousesByPlanet
        )

        val finalLinks = houseSourceMap
            .map { (house, sources) ->
                KpHouseLink(
                    house = house,
                    sources = sources.sortedByDescending { it.weight }
                )
            }
            .sortedWith(
                compareByDescending<KpHouseLink> { it.primarySource.weight }
                    .thenBy { it.house }
            )

        return KpPlanetDetail(
            position = position,
            signLord = position.sign.ruler,
            subdivision = subdivision,
            ownedHouses = ownedHousesByPlanet[position.planet].orEmpty(),
            starLinkedHouses = starLinkedHouses,
            subLinkedHouses = subLinkedHouses,
            finalSignifiedHouses = finalLinks
        )
    }

    private fun linkedHouseSources(
        position: PlanetPosition,
        subdivision: KpSubdivision,
        positionsByPlanet: Map<Planet, PlanetPosition>,
        ownedHousesByPlanet: Map<Planet, List<Int>>
    ): Map<Int, Set<KpLinkSource>> {
        val houseToSources = linkedMapOf<Int, MutableSet<KpLinkSource>>()

        fun add(house: Int, source: KpLinkSource) {
            houseToSources.getOrPut(house) { linkedSetOf() }.add(source)
        }

        fun addLinkedPlanet(planet: Planet, occupantSource: KpLinkSource, ownerSource: KpLinkSource) {
            linkedPlanetHouses(
                sourcePlanet = planet,
                positionsByPlanet = positionsByPlanet,
                ownedHousesByPlanet = ownedHousesByPlanet
            ).occupied.forEach { add(it, occupantSource) }
            linkedPlanetHouses(
                sourcePlanet = planet,
                positionsByPlanet = positionsByPlanet,
                ownedHousesByPlanet = ownedHousesByPlanet
            ).owned.forEach { add(it, ownerSource) }
        }

        addLinkedPlanet(subdivision.starLord, KpLinkSource.STAR_OCCUPANT, KpLinkSource.STAR_OWNER)
        add(position.house, KpLinkSource.OCCUPANT)
        ownedHousesByPlanet[position.planet].orEmpty().forEach { add(it, KpLinkSource.OWNER) }
        linkedPlanetHouses(
            sourcePlanet = subdivision.subLord,
            positionsByPlanet = positionsByPlanet,
            ownedHousesByPlanet = ownedHousesByPlanet
        ).all.forEach { add(it, KpLinkSource.SUB_LORD) }

        if (position.planet == Planet.RAHU || position.planet == Planet.KETU) {
            linkedPlanetHouses(
                sourcePlanet = position.sign.ruler,
                positionsByPlanet = positionsByPlanet,
                ownedHousesByPlanet = ownedHousesByPlanet
            ).all.forEach { add(it, KpLinkSource.NODE_SIGN_LORD) }
        }

        return houseToSources
    }

    private data class LinkedPlanetHouses(
        val occupied: Set<Int>,
        val owned: Set<Int>
    ) {
        val all: Set<Int> get() = occupied + owned
    }

    private fun linkedPlanetHouses(
        sourcePlanet: Planet,
        positionsByPlanet: Map<Planet, PlanetPosition>,
        ownedHousesByPlanet: Map<Planet, List<Int>>
    ): LinkedPlanetHouses {
        val position = positionsByPlanet[sourcePlanet]
        val occupied = linkedSetOf<Int>()
        val owned = linkedSetOf<Int>()

        position?.house?.let { occupied += it }
        owned += ownedHousesByPlanet[sourcePlanet].orEmpty()

        if (sourcePlanet == Planet.RAHU || sourcePlanet == Planet.KETU) {
            val signLord = position?.sign?.ruler
            signLord?.let { lord ->
                positionsByPlanet[lord]?.house?.let { occupied += it }
                owned += ownedHousesByPlanet[lord].orEmpty()
            }
        }

        return LinkedPlanetHouses(
            occupied = occupied,
            owned = owned
        )
    }

    private fun linkHousesOfPlanet(
        sourcePlanet: Planet,
        positionsByPlanet: Map<Planet, PlanetPosition>,
        ownedHousesByPlanet: Map<Planet, List<Int>>
    ): List<Int> {
        return linkedPlanetHouses(
            sourcePlanet = sourcePlanet,
            positionsByPlanet = positionsByPlanet,
            ownedHousesByPlanet = ownedHousesByPlanet
        ).all.sorted()
    }

    private fun calculateRulingPlanets(momentChart: VedicChart, moment: LocalDateTime): KpRulingPlanets {
        val moonPosition = momentChart.planetPositions.first { it.planet == Planet.MOON }
        val moonSubdivision = subdivisionForLongitude(moonPosition.longitude)
        val lagnaSubdivision = subdivisionForLongitude(momentChart.ascendant)
        val lagnaSignLord = ZodiacSign.fromLongitude(momentChart.ascendant).ruler
        val moonSignLord = moonPosition.sign.ruler
        val weekdayLord = weekdayLord(moment.dayOfWeek)

        val ordered = listOf(
            weekdayLord,
            moonSignLord,
            moonSubdivision.starLord,
            lagnaSignLord,
            lagnaSubdivision.starLord,
            lagnaSubdivision.subLord,
            moonSubdivision.subLord
        ).distinct()

        return KpRulingPlanets(
            moment = moment,
            weekdayLord = weekdayLord,
            moonSignLord = moonSignLord,
            moonStarLord = moonSubdivision.starLord,
            moonSubLord = moonSubdivision.subLord,
            lagnaSignLord = lagnaSignLord,
            lagnaStarLord = lagnaSubdivision.starLord,
            lagnaSubLord = lagnaSubdivision.subLord,
            ordered = ordered
        )
    }

    private fun weekdayLord(dayOfWeek: DayOfWeek): Planet {
        return when (dayOfWeek) {
            DayOfWeek.MONDAY -> Planet.MOON
            DayOfWeek.TUESDAY -> Planet.MARS
            DayOfWeek.WEDNESDAY -> Planet.MERCURY
            DayOfWeek.THURSDAY -> Planet.JUPITER
            DayOfWeek.FRIDAY -> Planet.VENUS
            DayOfWeek.SATURDAY -> Planet.SATURN
            DayOfWeek.SUNDAY -> Planet.SUN
        }
    }

    fun subdivisionForLongitude(longitude: Double): KpSubdivision {
        val normalizedLongitude = VedicAstrologyUtils.normalizeLongitude(longitude)
        val sign = ZodiacSign.fromLongitude(normalizedLongitude)
        val (nakshatra, pada) = Nakshatra.fromLongitude(normalizedLongitude)
        val offsetInNakshatra = VedicAstrologyUtils.normalizeDegree(normalizedLongitude - nakshatra.startDegree)
        val subMatch = matchSegment(
            startLord = nakshatra.ruler,
            segmentSpan = NAKSHATRA_SPAN,
            offset = offsetInNakshatra
        )
        val subSubMatch = matchSegment(
            startLord = subMatch.lord,
            segmentSpan = subMatch.endOffset - subMatch.startOffset,
            offset = offsetInNakshatra - subMatch.startOffset
        )

        return KpSubdivision(
            longitude = normalizedLongitude,
            sign = sign,
            nakshatra = nakshatra,
            pada = pada,
            starLord = nakshatra.ruler,
            subLord = subMatch.lord,
            subSubLord = subSubMatch.lord
        )
    }

    private fun matchSegment(
        startLord: Planet,
        segmentSpan: Double,
        offset: Double
    ): SegmentMatch {
        val sequence = dashaSequenceFrom(startLord)
        val normalizedOffset = offset.coerceIn(0.0, segmentSpan)
        var cursor = 0.0

        sequence.forEachIndexed { index, lord ->
            val span = segmentSpan * (VIMSHOTTARI_YEARS.getValue(lord) / 120.0)
            val end = if (index == sequence.lastIndex) segmentSpan else cursor + span
            if (normalizedOffset <= end + EPSILON) {
                return SegmentMatch(
                    lord = lord,
                    startOffset = cursor,
                    endOffset = end
                )
            }
            cursor = end
        }

        return SegmentMatch(
            lord = sequence.last(),
            startOffset = cursor,
            endOffset = segmentSpan
        )
    }

    private fun dashaSequenceFrom(startLord: Planet): List<Planet> {
        val startIndex = KP_SEQUENCE.indexOf(startLord)
            .takeIf { it >= 0 }
            ?: error("Unsupported KP lord: $startLord")
        return List(KP_SEQUENCE.size) { index -> KP_SEQUENCE[(startIndex + index) % KP_SEQUENCE.size] }
    }

    private fun generateHoraryNumbers(): List<KpHoraryNumber> {
        val segments = mutableListOf<KpHoraryNumber>()
        var sequenceNumber = 1

        Nakshatra.entries.forEach { nakshatra ->
            var cursor = nakshatra.startDegree
            dashaSequenceFrom(nakshatra.ruler).forEach { lord ->
                val span = NAKSHATRA_SPAN * (VIMSHOTTARI_YEARS.getValue(lord) / 120.0)
                val segmentEnd = cursor + span
                var splitStart = cursor
                while (splitStart < segmentEnd - EPSILON) {
                    val nextBoundary = min(segmentEnd, nextSignBoundary(splitStart))
                    val midpoint = splitStart + ((nextBoundary - splitStart) / 2.0)
                    segments += KpHoraryNumber(
                        number = sequenceNumber++,
                        startLongitude = VedicAstrologyUtils.normalizeLongitude(splitStart),
                        endLongitude = if (nextBoundary >= FULL_CIRCLE - EPSILON) FULL_CIRCLE else nextBoundary,
                        subdivision = subdivisionForLongitude(midpoint)
                    )
                    splitStart = nextBoundary
                }
                cursor = segmentEnd
            }
        }

        return segments
    }

    private fun nextSignBoundary(longitude: Double): Double {
        val normalized = VedicAstrologyUtils.normalizeLongitude(longitude)
        val currentSignIndex = floor(normalized / SIGN_SPAN).toInt()
        val next = (currentSignIndex + 1) * SIGN_SPAN
        return if (next <= longitude + EPSILON) longitude + SIGN_SPAN else next
    }
}
