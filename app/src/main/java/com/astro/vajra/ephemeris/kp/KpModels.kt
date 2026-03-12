package com.astro.vajra.ephemeris.kp

import com.astro.vajra.core.model.Ayanamsa
import com.astro.vajra.core.model.HouseSystem
import com.astro.vajra.core.model.Nakshatra
import com.astro.vajra.core.model.Planet
import com.astro.vajra.core.model.PlanetPosition
import com.astro.vajra.core.model.VedicChart
import com.astro.vajra.core.model.ZodiacSign
import com.astro.vajra.data.preferences.NodeCalculationMode
import java.time.LocalDateTime

data class KpConfiguration(
    val ayanamsa: Ayanamsa,
    val houseSystem: HouseSystem,
    val nodeMode: NodeCalculationMode
)

data class KpAnalysisResult(
    val sourceChart: VedicChart,
    val kpChart: VedicChart,
    val configuration: KpConfiguration,
    val cuspDetails: List<KpCuspDetail>,
    val planetDetails: List<KpPlanetDetail>,
    val houseSignificators: List<KpHouseSignificators>,
    val birthRulingPlanets: KpRulingPlanets,
    val analysisRulingPlanets: KpRulingPlanets,
    val horaryNumbers: List<KpHoraryNumber>
)

data class KpSubdivision(
    val longitude: Double,
    val sign: ZodiacSign,
    val nakshatra: Nakshatra,
    val pada: Int,
    val starLord: Planet,
    val subLord: Planet,
    val subSubLord: Planet
)

enum class KpLinkSource(val weight: Int) {
    STAR_OCCUPANT(600),
    OCCUPANT(500),
    STAR_OWNER(400),
    OWNER(300),
    SUB_LORD(200),
    NODE_SIGN_LORD(100)
}

data class KpHouseLink(
    val house: Int,
    val sources: List<KpLinkSource>
) {
    val primarySource: KpLinkSource = sources.maxByOrNull { it.weight } ?: KpLinkSource.OWNER
}

data class KpPlanetDetail(
    val position: PlanetPosition,
    val signLord: Planet,
    val subdivision: KpSubdivision,
    val ownedHouses: List<Int>,
    val starLinkedHouses: List<Int>,
    val subLinkedHouses: List<Int>,
    val finalSignifiedHouses: List<KpHouseLink>
)

data class KpCuspDetail(
    val house: Int,
    val longitude: Double,
    val sign: ZodiacSign,
    val signLord: Planet,
    val subdivision: KpSubdivision,
    val occupants: List<Planet>,
    val ownerPlanets: List<Planet>,
    val subLordLinkedHouses: List<Int>
)

data class KpHouseSignificators(
    val house: Int,
    val cuspStarLord: Planet,
    val cuspSubLord: Planet,
    val occupants: List<Planet>,
    val owners: List<Planet>,
    val starOfOccupants: List<Planet>,
    val starOfOwners: List<Planet>,
    val principalSignificators: List<Planet>,
    val cuspSubLordLinkedHouses: List<Int>
)

data class KpRulingPlanets(
    val moment: LocalDateTime,
    val weekdayLord: Planet,
    val moonSignLord: Planet,
    val moonStarLord: Planet,
    val moonSubLord: Planet,
    val lagnaSignLord: Planet,
    val lagnaStarLord: Planet,
    val lagnaSubLord: Planet,
    val ordered: List<Planet>
)

data class KpHoraryNumber(
    val number: Int,
    val startLongitude: Double,
    val endLongitude: Double,
    val subdivision: KpSubdivision
)
