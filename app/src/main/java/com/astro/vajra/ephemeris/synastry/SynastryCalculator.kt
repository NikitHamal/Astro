package com.astro.vajra.ephemeris.synastry

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChatBubble
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.ui.graphics.vector.ImageVector
import com.astro.vajra.core.common.Language
import com.astro.vajra.core.common.StringKeyDosha
import com.astro.vajra.core.common.StringResources
import com.astro.vajra.core.model.Planet
import com.astro.vajra.core.model.PlanetPosition
import com.astro.vajra.core.model.VedicChart
import com.astro.vajra.core.model.ZodiacSign
import com.astro.vajra.ephemeris.DivisionalChartCalculator
import com.astro.vajra.ephemeris.VedicAstrologyUtils
import com.astro.vajra.ephemeris.VedicAstrologyUtils.PlanetaryRelationship
import kotlin.math.abs

data class SynastryAspect(
    val planet1: Planet,
    val planet1Chart: Int,
    val planet2: Planet,
    val planet2Chart: Int,
    val aspectType: SynastryAspectType,
    val orb: Double,
    val isApplying: Boolean,
    val strength: Double,
    val interpretation: String
)

enum class SynastryAspectType(
    val displayName: String,
    val nature: AspectNature,
    val symbol: String,
    val maxOrb: Double
) {
    YUTI("Yuti", AspectNature.MAJOR, "Y", 10.0),
    BENEFIC_DRISHTI("Benefic Drishti", AspectNature.HARMONIOUS, "B", 12.0),
    MALEFIC_DRISHTI("Malefic Drishti", AspectNature.CHALLENGING, "M", 12.0),
    RASHI_DRISHTI("Rashi Drishti", AspectNature.MAJOR, "R", 30.0);

    fun getLocalizedName(language: Language): String = when (this) {
        YUTI -> StringResources.get(StringKeyDosha.SYNASTRY_CONJUNCTION, language)
        BENEFIC_DRISHTI -> displayName
        MALEFIC_DRISHTI -> displayName
        RASHI_DRISHTI -> displayName
    }
}

enum class AspectNature {
    MAJOR, HARMONIOUS, CHALLENGING, MINOR
}

data class HouseOverlay(
    val planet: Planet,
    val sourceChart: Int,
    val houseNumber: Int,
    val interpretation: String,
    val lifeArea: String
)

data class CompatibilityCategory(
    val name: String,
    val score: Double,
    val maxScore: Double,
    val description: String,
    val icon: ImageVector
)

data class PracticalRelationshipInputs(
    val communication: Double? = null,
    val financialAlignment: Double? = null,
    val familyValues: Double? = null,
    val conflictStyle: Double? = null
)

data class PracticalCompatibilityResult(
    val communicationScore: Double,
    val financialAlignmentScore: Double,
    val familyValuesScore: Double,
    val conflictStyleScore: Double,
    val overallScore: Double
)

data class SynastryAnalysisResult(
    val aspects: List<SynastryAspect>,
    val harmoniousAspects: List<SynastryAspect>,
    val challengingAspects: List<SynastryAspect>,
    val houseOverlays1In2: List<HouseOverlay>,
    val houseOverlays2In1: List<HouseOverlay>,
    val compatibilityCategories: List<CompatibilityCategory>,
    val overallCompatibility: Double,
    val keyFindings: List<String>,
    val sunMoonAspects: List<SynastryAspect>,
    val venusMarsAspects: List<SynastryAspect>,
    val ascendantConnections: List<SynastryAspect>,
    val practicalCompatibility: PracticalCompatibilityResult,
    val astroCompatibility: Double,
    val structuralVedicScore: Double,
    val relationshipReadiness: Double
)

object SynastryCalculator {

    fun calculate(
        chart1: VedicChart,
        chart2: VedicChart,
        language: Language,
        practicalInputs: PracticalRelationshipInputs? = null
    ): SynastryAnalysisResult {
        val aspects = calculateInterChartAspects(chart1, chart2, language)
        val sortedAspects = aspects.sortedByDescending { it.strength }

        val harmoniousAspects = sortedAspects.filter { it.aspectType.nature == AspectNature.HARMONIOUS }
        val challengingAspects = sortedAspects.filter { it.aspectType.nature == AspectNature.CHALLENGING }

        val houseOverlays1In2 = calculateHouseOverlays(chart1, chart2, 1, language)
        val houseOverlays2In1 = calculateHouseOverlays(chart2, chart1, 2, language)

        val sunMoonAspects = sortedAspects.filter { aspect ->
            (aspect.planet1 == Planet.SUN && aspect.planet2 == Planet.MOON) ||
                (aspect.planet1 == Planet.MOON && aspect.planet2 == Planet.SUN)
        }

        val venusMarsAspects = sortedAspects.filter { aspect ->
            (aspect.planet1 == Planet.VENUS && aspect.planet2 == Planet.MARS) ||
                (aspect.planet1 == Planet.MARS && aspect.planet2 == Planet.VENUS)
        }

        val ascendantConnections = calculateAscendantConnections(chart1, chart2, language)

        val totalHarmonious = harmoniousAspects.sumOf { it.strength }
        val totalChallenging = challengingAspects.sumOf { it.strength }
        val aspectCompatibility = ((totalHarmonious / (totalHarmonious + totalChallenging + 0.01)) * 100.0)
            .coerceIn(0.0, 100.0)
        val structuralVedicScore = calculateStructuralVedicScore(chart1, chart2)
        val astroCompatibility = (
            aspectCompatibility * 0.62 +
                structuralVedicScore * 0.38
            ).coerceIn(0.0, 100.0)

        val practicalCompatibility = calculatePracticalCompatibility(
            sortedAspects,
            houseOverlays1In2,
            houseOverlays2In1,
            practicalInputs
        )

        val relationshipReadiness = (
            astroCompatibility * 0.74 +
                practicalCompatibility.overallScore * 0.26
            ).coerceIn(0.0, 100.0)

        val compatibilityCategories = calculateCompatibilityCategories(
            harmoniousAspects = harmoniousAspects,
            challengingAspects = challengingAspects,
            sunMoonAspects = sunMoonAspects,
            venusMarsAspects = venusMarsAspects,
            practicalCompatibility = practicalCompatibility,
            language = language
        )

        val keyFindings = generateKeyFindings(
            aspects = sortedAspects,
            overlays1In2 = houseOverlays1In2,
            overlays2In1 = houseOverlays2In1,
            practicalCompatibility = practicalCompatibility,
            language = language
        )

        return SynastryAnalysisResult(
            aspects = sortedAspects,
            harmoniousAspects = harmoniousAspects,
            challengingAspects = challengingAspects,
            houseOverlays1In2 = houseOverlays1In2,
            houseOverlays2In1 = houseOverlays2In1,
            compatibilityCategories = compatibilityCategories,
            overallCompatibility = relationshipReadiness,
            keyFindings = keyFindings,
            sunMoonAspects = sunMoonAspects,
            venusMarsAspects = venusMarsAspects,
            ascendantConnections = ascendantConnections,
            practicalCompatibility = practicalCompatibility,
            astroCompatibility = astroCompatibility,
            structuralVedicScore = structuralVedicScore,
            relationshipReadiness = relationshipReadiness
        )
    }

    private fun calculateInterChartAspects(
        chart1: VedicChart,
        chart2: VedicChart,
        language: Language
    ): List<SynastryAspect> {
        val planetsToAnalyze = listOf(
            Planet.SUN,
            Planet.MOON,
            Planet.MARS,
            Planet.MERCURY,
            Planet.JUPITER,
            Planet.VENUS,
            Planet.SATURN,
            Planet.RAHU,
            Planet.KETU
        )

        val aspectMap = mutableMapOf<String, SynastryAspect>()

        for (planet1 in planetsToAnalyze) {
            val pos1 = chart1.planetPositions.find { it.planet == planet1 } ?: continue

            for (planet2 in planetsToAnalyze) {
                val pos2 = chart2.planetPositions.find { it.planet == planet2 } ?: continue

                // Yuti (conjunction by sign+orb) stays primary for relationship chemistry.
                val conjunctionOrb = abs((pos1.longitude % 30.0) - (pos2.longitude % 30.0))
                if (pos1.sign == pos2.sign && conjunctionOrb <= SynastryAspectType.YUTI.maxOrb) {
                    upsertAspect(
                        aspectMap = aspectMap,
                        key = "${planet1.name}-${planet2.name}-${SynastryAspectType.YUTI.name}",
                        aspect = SynastryAspect(
                            planet1 = planet1,
                            planet1Chart = 1,
                            planet2 = planet2,
                            planet2Chart = 2,
                            aspectType = SynastryAspectType.YUTI,
                            orb = conjunctionOrb,
                            isApplying = isAspectApplying(pos1, pos2),
                            strength = calculateAspectStrength(conjunctionOrb, SynastryAspectType.YUTI.maxOrb),
                            interpretation = generateAspectInterpretation(planet1, planet2, SynastryAspectType.YUTI, language)
                        )
                    )
                }

                val houseDistance = VedicAstrologyUtils.getHouseFromSigns(pos2.sign, pos1.sign)
                val aspectOffsets = getGrahaAspectOffsets(planet1)

                if (houseDistance in aspectOffsets) {
                    val drishtiType = if (VedicAstrologyUtils.isNaturalBenefic(planet1)) {
                        SynastryAspectType.BENEFIC_DRISHTI
                    } else {
                        SynastryAspectType.MALEFIC_DRISHTI
                    }

                    val exactAngle = (houseDistance - 1) * 30.0
                    val orb = angularOrb(pos1.longitude, pos2.longitude, exactAngle)

                    if (orb <= drishtiType.maxOrb) {
                        upsertAspect(
                            aspectMap = aspectMap,
                            key = "${planet1.name}-${planet2.name}-${drishtiType.name}-$houseDistance",
                            aspect = SynastryAspect(
                                planet1 = planet1,
                                planet1Chart = 1,
                                planet2 = planet2,
                                planet2Chart = 2,
                                aspectType = drishtiType,
                                orb = orb,
                                isApplying = isAspectApplying(pos1, pos2),
                                strength = calculateAspectStrength(orb, drishtiType.maxOrb),
                                interpretation = generateAspectInterpretation(planet1, planet2, drishtiType, language)
                            )
                        )
                    }
                }

                if (hasRashiDrishti(pos1.sign, pos2.sign)) {
                    val orb = abs((pos1.longitude % 30.0) - (pos2.longitude % 30.0))
                    upsertAspect(
                        aspectMap = aspectMap,
                        key = "${planet1.name}-${planet2.name}-${SynastryAspectType.RASHI_DRISHTI.name}",
                        aspect = SynastryAspect(
                            planet1 = planet1,
                            planet1Chart = 1,
                            planet2 = planet2,
                            planet2Chart = 2,
                            aspectType = SynastryAspectType.RASHI_DRISHTI,
                            orb = orb,
                            isApplying = isAspectApplying(pos1, pos2),
                            strength = calculateAspectStrength(orb, SynastryAspectType.RASHI_DRISHTI.maxOrb),
                            interpretation = generateAspectInterpretation(planet1, planet2, SynastryAspectType.RASHI_DRISHTI, language)
                        )
                    )
                }
            }
        }

        return aspectMap.values.toList()
    }

    private fun calculateAscendantConnections(
        chart1: VedicChart,
        chart2: VedicChart,
        language: Language
    ): List<SynastryAspect> {
        val asc1 = chart1.ascendant
        val result = mutableListOf<SynastryAspect>()

        for (pos in chart2.planetPositions) {
            val orb = angularOrb(asc1, pos.longitude, 0.0)
            if (orb <= 10.0) {
                result.add(
                    SynastryAspect(
                        planet1 = Planet.SUN,
                        planet1Chart = 1,
                        planet2 = pos.planet,
                        planet2Chart = 2,
                        aspectType = SynastryAspectType.YUTI,
                        orb = orb,
                        isApplying = false,
                        strength = calculateAspectStrength(orb, 10.0),
                        interpretation = generateAscendantInterpretation(pos.planet, 1, language)
                    )
                )
            }
        }

        return result.sortedByDescending { it.strength }
    }

    private fun calculateHouseOverlays(
        sourceChart: VedicChart,
        targetChart: VedicChart,
        sourceChartNum: Int,
        language: Language
    ): List<HouseOverlay> {
        val overlays = mutableListOf<HouseOverlay>()

        for (pos in sourceChart.planetPositions) {
            val houseNumber = getHouseForLongitude(pos.longitude, targetChart.houseCusps)
            val lifeArea = getLifeAreaForHouse(houseNumber, language)
            val interpretation = generateHouseOverlayInterpretation(pos.planet, houseNumber, sourceChartNum, language)

            overlays.add(
                HouseOverlay(
                    planet = pos.planet,
                    sourceChart = sourceChartNum,
                    houseNumber = houseNumber,
                    interpretation = interpretation,
                    lifeArea = lifeArea
                )
            )
        }

        return overlays
    }

    private fun calculateCompatibilityCategories(
        harmoniousAspects: List<SynastryAspect>,
        challengingAspects: List<SynastryAspect>,
        sunMoonAspects: List<SynastryAspect>,
        venusMarsAspects: List<SynastryAspect>,
        practicalCompatibility: PracticalCompatibilityResult,
        language: Language
    ): List<CompatibilityCategory> {
        val emotionalScore = sunMoonAspects.sumOf { it.strength * 10 }.coerceAtMost(10.0)
        val romanceScore = venusMarsAspects.sumOf { it.strength * 10 }.coerceAtMost(10.0)

        val mercuryAspects = harmoniousAspects.filter { it.planet1 == Planet.MERCURY || it.planet2 == Planet.MERCURY }
        val communicationPenalty = challengingAspects.count { it.planet1 == Planet.MERCURY || it.planet2 == Planet.MERCURY } * 0.7
        val communicationScore = (mercuryAspects.sumOf { it.strength * 5 } - communicationPenalty).coerceIn(0.0, 10.0)

        val saturnAspects = harmoniousAspects.filter { it.planet1 == Planet.SATURN || it.planet2 == Planet.SATURN }
        val saturnChallenges = challengingAspects.count { it.planet1 == Planet.SATURN || it.planet2 == Planet.SATURN } * 0.6
        val stabilityScore = (saturnAspects.sumOf { it.strength * 5 } - saturnChallenges).coerceIn(0.0, 10.0)

        val practicalAlignmentScore = (practicalCompatibility.overallScore / 10.0).coerceIn(0.0, 10.0)

        return listOf(
            CompatibilityCategory(
                name = StringResources.get(StringKeyDosha.SYNASTRY_EMOTIONAL_BOND, language),
                score = emotionalScore,
                maxScore = 10.0,
                description = StringResources.get(StringKeyDosha.SYNASTRY_DESC_EMOTIONAL, language),
                icon = Icons.Filled.Favorite
            ),
            CompatibilityCategory(
                name = StringResources.get(StringKeyDosha.SYNASTRY_ROMANCE, language),
                score = romanceScore,
                maxScore = 10.0,
                description = StringResources.get(StringKeyDosha.SYNASTRY_DESC_ROMANCE, language),
                icon = Icons.Filled.FavoriteBorder
            ),
            CompatibilityCategory(
                name = StringResources.get(StringKeyDosha.SYNASTRY_COMMUNICATION, language),
                score = communicationScore,
                maxScore = 10.0,
                description = StringResources.get(StringKeyDosha.SYNASTRY_DESC_COMMUNICATION, language),
                icon = Icons.Filled.ChatBubble
            ),
            CompatibilityCategory(
                name = StringResources.get(StringKeyDosha.SYNASTRY_STABILITY, language),
                score = stabilityScore,
                maxScore = 10.0,
                description = StringResources.get(StringKeyDosha.SYNASTRY_DESC_STABILITY, language),
                icon = Icons.Filled.Shield
            ),
            CompatibilityCategory(
                name = "Practical Alignment",
                score = practicalAlignmentScore,
                maxScore = 10.0,
                description = "Daily-life fit across communication, finance, family, and conflict style.",
                icon = Icons.Filled.TrendingUp
            )
        )
    }

    private fun calculatePracticalCompatibility(
        aspects: List<SynastryAspect>,
        overlays1In2: List<HouseOverlay>,
        overlays2In1: List<HouseOverlay>,
        inputs: PracticalRelationshipInputs?
    ): PracticalCompatibilityResult {
        val baseCommunication = inputs?.communication ?: 5.5
        val baseFinancial = inputs?.financialAlignment ?: 5.5
        val baseFamily = inputs?.familyValues ?: 5.5
        val baseConflict = inputs?.conflictStyle ?: 5.5

        val mercurySupport = aspects.count {
            (it.planet1 == Planet.MERCURY || it.planet2 == Planet.MERCURY) &&
                it.aspectType.nature == AspectNature.HARMONIOUS
        }
        val mercuryChallenges = aspects.count {
            (it.planet1 == Planet.MERCURY || it.planet2 == Planet.MERCURY) &&
                it.aspectType.nature == AspectNature.CHALLENGING
        }

        val jupiterVenusSupport = aspects.count {
            (it.planet1 in setOf(Planet.JUPITER, Planet.VENUS) || it.planet2 in setOf(Planet.JUPITER, Planet.VENUS)) &&
                it.aspectType.nature == AspectNature.HARMONIOUS
        }

        val marsSaturnChallenges = aspects.count {
            (it.planet1 in setOf(Planet.MARS, Planet.SATURN) || it.planet2 in setOf(Planet.MARS, Planet.SATURN)) &&
                it.aspectType.nature == AspectNature.CHALLENGING
        }

        val familyHouseHits = (overlays1In2 + overlays2In1).count { it.houseNumber in setOf(4, 5, 7) }
        val financeHouseHits = (overlays1In2 + overlays2In1).count { it.houseNumber in setOf(2, 6, 10, 11) }

        val communicationScore = (baseCommunication + (mercurySupport * 0.6) - (mercuryChallenges * 0.7))
            .coerceIn(0.0, 10.0)

        val financialAlignmentScore = (baseFinancial + (jupiterVenusSupport * 0.4) + (financeHouseHits * 0.08) - (marsSaturnChallenges * 0.35))
            .coerceIn(0.0, 10.0)

        val familyValuesScore = (baseFamily + (familyHouseHits * 0.08) + (jupiterVenusSupport * 0.25))
            .coerceIn(0.0, 10.0)

        val conflictStyleScore = (baseConflict - (marsSaturnChallenges * 0.5) + (mercurySupport * 0.25))
            .coerceIn(0.0, 10.0)

        val overall = ((communicationScore + financialAlignmentScore + familyValuesScore + conflictStyleScore) / 4.0) * 10.0

        return PracticalCompatibilityResult(
            communicationScore = communicationScore,
            financialAlignmentScore = financialAlignmentScore,
            familyValuesScore = familyValuesScore,
            conflictStyleScore = conflictStyleScore,
            overallScore = overall.coerceIn(0.0, 100.0)
        )
    }

    private fun generateKeyFindings(
        aspects: List<SynastryAspect>,
        overlays1In2: List<HouseOverlay>,
        overlays2In1: List<HouseOverlay>,
        practicalCompatibility: PracticalCompatibilityResult,
        language: Language
    ): List<String> {
        val findings = mutableListOf<String>()

        aspects.take(3).forEach { aspect ->
            findings.add(
                StringResources.get(
                    StringKeyDosha.SYNASTRY_FINDING_ASPECT,
                    language,
                    aspect.aspectType.getLocalizedName(language),
                    aspect.planet1.getLocalizedName(language),
                    aspect.planet2.getLocalizedName(language)
                )
            )
        }

        overlays1In2.filter { it.houseNumber in listOf(1, 5, 7, 10) }.take(1).forEach { overlay ->
            findings.add(
                StringResources.get(
                    StringKeyDosha.SYNASTRY_FINDING_HOUSE,
                    language,
                    overlay.planet.getLocalizedName(language),
                    overlay.houseNumber,
                    getLifeAreaForHouse(overlay.houseNumber, language).lowercase()
                )
            )
        }

        overlays2In1.filter { it.houseNumber in listOf(1, 5, 7, 10) }.take(1).forEach { overlay ->
            findings.add(
                StringResources.get(
                    StringKeyDosha.SYNASTRY_FINDING_HOUSE,
                    language,
                    overlay.planet.getLocalizedName(language),
                    overlay.houseNumber,
                    getLifeAreaForHouse(overlay.houseNumber, language).lowercase()
                )
            )
        }

        if (practicalCompatibility.overallScore < 45.0) {
            findings.add("Practical-life compatibility is weak. Prioritize communication and conflict agreements.")
        } else if (practicalCompatibility.overallScore >= 70.0) {
            findings.add("Practical-life compatibility is strong across communication and daily routines.")
        }

        return findings.take(6)
    }

    private fun calculateStructuralVedicScore(chart1: VedicChart, chart2: VedicChart): Double {
        val moonHarmony = moonHarmonyScore(chart1, chart2)
        val seventhLordHarmonyD1 = seventhLordHarmonyScore(chart1, chart2)
        val seventhLordHarmonyD9 = seventhLordHarmonyScoreD9(chart1, chart2)
        val venusMarsCross = venusMarsCrossScore(chart1, chart2)
        val saturnMaturity = saturnMaturityScore(chart1, chart2)

        return (
            moonHarmony * 0.28 +
                seventhLordHarmonyD1 * 0.24 +
                seventhLordHarmonyD9 * 0.22 +
                venusMarsCross * 0.18 +
                saturnMaturity * 0.08
            ).coerceIn(0.0, 100.0)
    }

    private fun moonHarmonyScore(chart1: VedicChart, chart2: VedicChart): Double {
        val moon1 = VedicAstrologyUtils.getMoonPosition(chart1)
        val moon2 = VedicAstrologyUtils.getMoonPosition(chart2)
        if (moon1 == null || moon2 == null) return 58.0

        val distance = VedicAstrologyUtils.getHouseFromSigns(moon2.sign, moon1.sign)
        return houseHarmonyScore(distance)
    }

    private fun seventhLordHarmonyScore(chart1: VedicChart, chart2: VedicChart): Double {
        val asc1 = ZodiacSign.fromLongitude(chart1.ascendant)
        val asc2 = ZodiacSign.fromLongitude(chart2.ascendant)
        val seventhLord1 = ZodiacSign.entries[(asc1.ordinal + 6) % 12].ruler
        val seventhLord2 = ZodiacSign.entries[(asc2.ordinal + 6) % 12].ruler
        return relationshipScore(seventhLord1, seventhLord2)
    }

    private fun seventhLordHarmonyScoreD9(chart1: VedicChart, chart2: VedicChart): Double {
        val d9_1 = DivisionalChartCalculator.calculateNavamsa(chart1)
        val d9_2 = DivisionalChartCalculator.calculateNavamsa(chart2)
        val seventhLord1 = ZodiacSign.entries[(d9_1.ascendantSign.ordinal + 6) % 12].ruler
        val seventhLord2 = ZodiacSign.entries[(d9_2.ascendantSign.ordinal + 6) % 12].ruler
        return relationshipScore(seventhLord1, seventhLord2)
    }

    private fun venusMarsCrossScore(chart1: VedicChart, chart2: VedicChart): Double {
        val venus1 = VedicAstrologyUtils.getPlanetPosition(chart1, Planet.VENUS)
        val mars1 = VedicAstrologyUtils.getPlanetPosition(chart1, Planet.MARS)
        val venus2 = VedicAstrologyUtils.getPlanetPosition(chart2, Planet.VENUS)
        val mars2 = VedicAstrologyUtils.getPlanetPosition(chart2, Planet.MARS)
        if (venus1 == null || mars1 == null || venus2 == null || mars2 == null) return 55.0

        val cross1 = VedicAstrologyUtils.getHouseFromSigns(mars2.sign, venus1.sign)
        val cross2 = VedicAstrologyUtils.getHouseFromSigns(mars1.sign, venus2.sign)
        val intra1 = VedicAstrologyUtils.getHouseFromSigns(mars1.sign, venus1.sign)
        val intra2 = VedicAstrologyUtils.getHouseFromSigns(mars2.sign, venus2.sign)

        return (
            houseHarmonyScore(cross1) * 0.36 +
                houseHarmonyScore(cross2) * 0.36 +
                houseHarmonyScore(intra1) * 0.14 +
                houseHarmonyScore(intra2) * 0.14
            ).coerceIn(0.0, 100.0)
    }

    private fun saturnMaturityScore(chart1: VedicChart, chart2: VedicChart): Double {
        val saturn1 = VedicAstrologyUtils.getPlanetPosition(chart1, Planet.SATURN)
        val saturn2 = VedicAstrologyUtils.getPlanetPosition(chart2, Planet.SATURN)
        val moon1 = VedicAstrologyUtils.getMoonPosition(chart1)
        val moon2 = VedicAstrologyUtils.getMoonPosition(chart2)
        val venus1 = VedicAstrologyUtils.getPlanetPosition(chart1, Planet.VENUS)
        val venus2 = VedicAstrologyUtils.getPlanetPosition(chart2, Planet.VENUS)

        var pressure = 0
        if (saturn1 != null && moon2 != null && isSaturnAspectingSign(saturn1.sign, moon2.sign)) pressure++
        if (saturn1 != null && venus2 != null && isSaturnAspectingSign(saturn1.sign, venus2.sign)) pressure++
        if (saturn2 != null && moon1 != null && isSaturnAspectingSign(saturn2.sign, moon1.sign)) pressure++
        if (saturn2 != null && venus1 != null && isSaturnAspectingSign(saturn2.sign, venus1.sign)) pressure++

        return when (pressure) {
            0 -> 74.0
            1 -> 62.0
            2 -> 52.0
            3 -> 43.0
            else -> 35.0
        }
    }

    private fun isSaturnAspectingSign(fromSign: ZodiacSign, toSign: ZodiacSign): Boolean {
        val distance = VedicAstrologyUtils.getHouseFromSigns(toSign, fromSign)
        return distance in setOf(3, 7, 10)
    }

    private fun relationshipScore(lord1: Planet, lord2: Planet): Double {
        val rel1 = VedicAstrologyUtils.getNaturalRelationship(lord1, lord2)
        val rel2 = VedicAstrologyUtils.getNaturalRelationship(lord2, lord1)
        return when {
            rel1 == PlanetaryRelationship.FRIEND && rel2 == PlanetaryRelationship.FRIEND -> 90.0
            (rel1 == PlanetaryRelationship.FRIEND && rel2 == PlanetaryRelationship.NEUTRAL) ||
                (rel1 == PlanetaryRelationship.NEUTRAL && rel2 == PlanetaryRelationship.FRIEND) -> 76.0
            rel1 == PlanetaryRelationship.NEUTRAL && rel2 == PlanetaryRelationship.NEUTRAL -> 66.0
            (rel1 == PlanetaryRelationship.ENEMY && rel2 == PlanetaryRelationship.NEUTRAL) ||
                (rel1 == PlanetaryRelationship.NEUTRAL && rel2 == PlanetaryRelationship.ENEMY) -> 46.0
            else -> 28.0
        }
    }

    private fun houseHarmonyScore(distance: Int): Double = when (distance) {
        1, 5, 7, 9 -> 86.0
        3, 4, 10, 11 -> 72.0
        2, 12 -> 54.0
        6, 8 -> 40.0
        else -> 58.0
    }

    private fun generateAspectInterpretation(
        planet1: Planet,
        planet2: Planet,
        aspectType: SynastryAspectType,
        language: Language
    ): String {
        val p1Name = planet1.getLocalizedName(language)
        val p2Name = planet2.getLocalizedName(language)

        val key = when (aspectType.nature) {
            AspectNature.HARMONIOUS -> StringKeyDosha.SYNASTRY_INTERPRET_HARMONIOUS
            AspectNature.CHALLENGING -> StringKeyDosha.SYNASTRY_INTERPRET_CHALLENGING
            AspectNature.MAJOR -> StringKeyDosha.SYNASTRY_INTERPRET_MAJOR
            AspectNature.MINOR -> StringKeyDosha.SYNASTRY_INTERPRET_MINOR
        }

        return StringResources.get(key, language, p1Name, p2Name)
    }

    private fun generateAscendantInterpretation(planet: Planet, chartNum: Int, language: Language): String {
        return StringResources.get(
            StringKeyDosha.SYNASTRY_INTERPRET_ASCENDANT,
            language,
            planet.getLocalizedName(language),
            chartNum
        )
    }

    private fun generateHouseOverlayInterpretation(planet: Planet, house: Int, chartNum: Int, language: Language): String {
        return StringResources.get(
            StringKeyDosha.SYNASTRY_INTERPRET_OVERLAY,
            language,
            chartNum,
            planet.getLocalizedName(language),
            house,
            getLifeAreaForHouse(house, language).lowercase()
        )
    }

    private fun getLifeAreaForHouse(house: Int, language: Language): String {
        val key = when (house) {
            1 -> StringKeyDosha.HOUSE_SIG_1
            2 -> StringKeyDosha.HOUSE_SIG_2
            3 -> StringKeyDosha.HOUSE_SIG_3
            4 -> StringKeyDosha.HOUSE_SIG_4
            5 -> StringKeyDosha.HOUSE_SIG_5
            6 -> StringKeyDosha.HOUSE_SIG_6
            7 -> StringKeyDosha.HOUSE_SIG_7
            8 -> StringKeyDosha.HOUSE_SIG_8
            9 -> StringKeyDosha.HOUSE_SIG_9
            10 -> StringKeyDosha.HOUSE_SIG_10
            11 -> StringKeyDosha.HOUSE_SIG_11
            12 -> StringKeyDosha.HOUSE_SIG_12
            else -> StringKeyDosha.SYNASTRY_LIFE_AREA_GENERAL
        }
        return StringResources.get(key, language)
    }

    private fun getHouseForLongitude(longitude: Double, houseCusps: List<Double>): Int {
        val normalizedLong = VedicAstrologyUtils.normalizeAngle(longitude)
        for (i in 0 until 12) {
            val nextIndex = (i + 1) % 12
            val cusp = houseCusps[i]
            val nextCusp = houseCusps[nextIndex]

            if (nextCusp > cusp) {
                if (normalizedLong >= cusp && normalizedLong < nextCusp) {
                    return i + 1
                }
            } else {
                if (normalizedLong >= cusp || normalizedLong < nextCusp) {
                    return i + 1
                }
            }
        }
        return 1
    }

    private fun getGrahaAspectOffsets(planet: Planet): Set<Int> = when (planet) {
        Planet.MARS -> setOf(4, 7, 8)
        Planet.JUPITER -> setOf(5, 7, 9)
        Planet.SATURN -> setOf(3, 7, 10)
        Planet.RAHU, Planet.KETU -> setOf(5, 7, 9)
        else -> setOf(7)
    }

    private fun hasRashiDrishti(fromSign: ZodiacSign, toSign: ZodiacSign): Boolean {
        val movableMap = mapOf(
            ZodiacSign.ARIES to setOf(ZodiacSign.LEO, ZodiacSign.SCORPIO, ZodiacSign.AQUARIUS),
            ZodiacSign.CANCER to setOf(ZodiacSign.SCORPIO, ZodiacSign.AQUARIUS, ZodiacSign.TAURUS),
            ZodiacSign.LIBRA to setOf(ZodiacSign.AQUARIUS, ZodiacSign.TAURUS, ZodiacSign.LEO),
            ZodiacSign.CAPRICORN to setOf(ZodiacSign.TAURUS, ZodiacSign.LEO, ZodiacSign.SCORPIO)
        )

        val fixedMap = mapOf(
            ZodiacSign.TAURUS to setOf(ZodiacSign.CANCER, ZodiacSign.LIBRA, ZodiacSign.CAPRICORN),
            ZodiacSign.LEO to setOf(ZodiacSign.LIBRA, ZodiacSign.CAPRICORN, ZodiacSign.ARIES),
            ZodiacSign.SCORPIO to setOf(ZodiacSign.CAPRICORN, ZodiacSign.ARIES, ZodiacSign.CANCER),
            ZodiacSign.AQUARIUS to setOf(ZodiacSign.ARIES, ZodiacSign.CANCER, ZodiacSign.LIBRA)
        )

        val dualSigns = setOf(ZodiacSign.GEMINI, ZodiacSign.VIRGO, ZodiacSign.SAGITTARIUS, ZodiacSign.PISCES)

        return when {
            fromSign in movableMap -> movableMap[fromSign]?.contains(toSign) == true
            fromSign in fixedMap -> fixedMap[fromSign]?.contains(toSign) == true
            fromSign in dualSigns -> toSign in dualSigns && toSign != fromSign
            else -> false
        }
    }

    private fun angularOrb(longitude1: Double, longitude2: Double, targetAngle: Double): Double {
        val diff = abs(VedicAstrologyUtils.normalizeAngle(longitude1 - longitude2))
        val orb = abs(diff - targetAngle)
        return minOf(orb, 360.0 - orb)
    }

    private fun calculateAspectStrength(orb: Double, maxOrb: Double): Double {
        return ((maxOrb - orb) / maxOrb).coerceIn(0.0, 1.0)
    }

    private fun isAspectApplying(pos1: PlanetPosition, pos2: PlanetPosition): Boolean {
        val currentDiff = VedicAstrologyUtils.normalizeAngle(pos2.longitude - pos1.longitude)
        val futurePos1 = VedicAstrologyUtils.normalizeAngle(pos1.longitude + pos1.speed)
        val futurePos2 = VedicAstrologyUtils.normalizeAngle(pos2.longitude + pos2.speed)
        val futureDiff = VedicAstrologyUtils.normalizeAngle(futurePos2 - futurePos1)
        return abs(futureDiff) < abs(currentDiff)
    }

    private fun upsertAspect(
        aspectMap: MutableMap<String, SynastryAspect>,
        key: String,
        aspect: SynastryAspect
    ) {
        val existing = aspectMap[key]
        if (existing == null || aspect.strength > existing.strength) {
            aspectMap[key] = aspect
        }
    }
}
