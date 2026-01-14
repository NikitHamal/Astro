package com.astro.storm.ephemeris.yoga

import com.astro.storm.data.model.Planet
import com.astro.storm.data.model.PlanetPosition
import com.astro.storm.data.model.VedicChart

/**
 * Chandra (Moon) Yoga Evaluator - Lunar Combinations
 *
 * Chandra Yogas are formed based on the Moon's position and its relationship
 * with other planets. The Moon represents mind, emotions, and public life.
 *
 * Types evaluated:
 * 1. Sunafa Yoga - Planet (not Sun) in 2nd from Moon
 * 2. Anafa Yoga - Planet (not Sun) in 12th from Moon
 * 3. Durudhara Yoga - Planets in both 2nd and 12th from Moon
 * 4. Kemadruma Yoga - No planets in 2nd and 12th from Moon (negative)
 * 5. Gaja-Kesari Yoga - Jupiter in Kendra from Moon
 * 6. Adhi Yoga - Benefics in 6, 7, 8 from Moon
 *
 * Based on:
 * - Brihat Parasara Hora Shastra (BPHS), Chapter 36
 * - Phaladeepika, Chapter 4
 *
 * @author AstroStorm
 */
class ChandraYogaEvaluator : YogaEvaluator {

    override val category: YogaCategory = YogaCategory.CHANDRA_YOGA

    override fun evaluate(chart: VedicChart): List<Yoga> {
        val yogas = mutableListOf<Yoga>()

        val moonPos = chart.planetPositions.find { it.planet == Planet.MOON } ?: return yogas

        // Calculate houses from Moon
        val houseFromMoon = { pos: PlanetPosition ->
            YogaHelpers.getHouseFrom(pos.sign, moonPos.sign)
        }

        // Get planets in 2nd and 12th from Moon (excluding Sun, Rahu, Ketu)
        val eligiblePlanets = listOf(Planet.MARS, Planet.MERCURY, Planet.JUPITER, Planet.VENUS, Planet.SATURN)
        val positions = chart.planetPositions.filter { it.planet in eligiblePlanets }

        val planetsIn2nd = positions.filter { houseFromMoon(it) == 2 }
        val planetsIn12th = positions.filter { houseFromMoon(it) == 12 }

        // 1. Sunafa Yoga - Planet in 2nd from Moon
        if (planetsIn2nd.isNotEmpty() && planetsIn12th.isEmpty()) {
            yogas.add(createSunafaYoga(planetsIn2nd, moonPos, chart))
        }

        // 2. Anafa Yoga - Planet in 12th from Moon
        if (planetsIn12th.isNotEmpty() && planetsIn2nd.isEmpty()) {
            yogas.add(createAnafaYoga(planetsIn12th, moonPos, chart))
        }

        // 3. Durudhara Yoga - Planets in both 2nd and 12th
        if (planetsIn2nd.isNotEmpty() && planetsIn12th.isNotEmpty()) {
            yogas.add(createDurudharaYoga(planetsIn2nd, planetsIn12th, moonPos, chart))
        }

        // 4. Kemadruma Yoga - No planets in 2nd and 12th (negative)
        if (planetsIn2nd.isEmpty() && planetsIn12th.isEmpty()) {
            // Check for cancellation conditions
            val hasCancellation = checkKemadrumaCancel(chart, moonPos)
            if (!hasCancellation) {
                yogas.add(createKemadrumaYoga(moonPos, chart))
            }
        }

        // 5. Gaja-Kesari Yoga
        evaluateGajaKesariYoga(chart, moonPos)?.let { yogas.add(it) }

        // 6. Adhi Yoga
        evaluateAdhiYoga(chart, moonPos)?.let { yogas.add(it) }

        return yogas
    }

    private fun createSunafaYoga(
        planetsIn2nd: List<PlanetPosition>,
        moonPos: PlanetPosition,
        chart: VedicChart
    ): Yoga {
        val planets = planetsIn2nd.map { it.planet }
        val strength = YogaHelpers.calculateYogaStrength(chart, planetsIn2nd + moonPos)

        val effectDetail = when {
            Planet.JUPITER in planets -> "wisdom and wealth"
            Planet.VENUS in planets -> "luxury and relationships"
            Planet.MERCURY in planets -> "business acumen"
            Planet.MARS in planets -> "courage and enterprise"
            Planet.SATURN in planets -> "perseverance and discipline"
            else -> "general prosperity"
        }

        return Yoga(
            name = "Sunafa Yoga",
            sanskritName = "Sunafa Yoga",
            category = YogaCategory.CHANDRA_YOGA,
            planets = planets,
            houses = planetsIn2nd.map { it.house },
            description = "${planets.joinToString { it.displayName }} in 2nd from Moon",
            effects = "Self-made wealth and status through $effectDetail, independent success",
            strength = YogaHelpers.strengthFromPercentage(strength),
            strengthPercentage = strength,
            isAuspicious = true,
            activationPeriod = "Moon and ${planets.first().displayName} periods",
            cancellationFactors = emptyList()
        )
    }

    private fun createAnafaYoga(
        planetsIn12th: List<PlanetPosition>,
        moonPos: PlanetPosition,
        chart: VedicChart
    ): Yoga {
        val planets = planetsIn12th.map { it.planet }
        val strength = YogaHelpers.calculateYogaStrength(chart, planetsIn12th + moonPos)

        val effectDetail = when {
            Planet.JUPITER in planets -> "spiritual wisdom"
            Planet.VENUS in planets -> "artistic refinement"
            Planet.MERCURY in planets -> "intellectual depth"
            Planet.MARS in planets -> "physical strength"
            Planet.SATURN in planets -> "organizational ability"
            else -> "general prosperity"
        }

        return Yoga(
            name = "Anafa Yoga",
            sanskritName = "Anafa Yoga",
            category = YogaCategory.CHANDRA_YOGA,
            planets = planets,
            houses = planetsIn12th.map { it.house },
            description = "${planets.joinToString { it.displayName }} in 12th from Moon",
            effects = "Good reputation through $effectDetail, respected in society",
            strength = YogaHelpers.strengthFromPercentage(strength),
            strengthPercentage = strength,
            isAuspicious = true,
            activationPeriod = "Moon and ${planets.first().displayName} periods",
            cancellationFactors = emptyList()
        )
    }

    private fun createDurudharaYoga(
        planetsIn2nd: List<PlanetPosition>,
        planetsIn12th: List<PlanetPosition>,
        moonPos: PlanetPosition,
        chart: VedicChart
    ): Yoga {
        val allPlanets = (planetsIn2nd + planetsIn12th).map { it.planet }
        val strength = YogaHelpers.calculateYogaStrength(chart, planetsIn2nd + planetsIn12th + moonPos) * 1.2

        return Yoga(
            name = "Durudhara Yoga",
            sanskritName = "Durudhara Yoga",
            category = YogaCategory.CHANDRA_YOGA,
            planets = allPlanets,
            houses = (planetsIn2nd + planetsIn12th).map { it.house }.distinct(),
            description = "Moon flanked by planets in 2nd and 12th houses",
            effects = "Wealth, fame, and comforts from multiple sources, well-protected life, royalty-like status",
            strength = YogaHelpers.strengthFromPercentage(strength.coerceAtMost(100.0)),
            strengthPercentage = strength.coerceAtMost(100.0),
            isAuspicious = true,
            activationPeriod = "Moon periods especially",
            cancellationFactors = emptyList()
        )
    }

    private fun checkKemadrumaCancel(chart: VedicChart, moonPos: PlanetPosition): Boolean {
        // Cancellation 1: Moon in Kendra from Lagna
        if (moonPos.house in listOf(1, 4, 7, 10)) return true

        // Cancellation 2: Moon conjunct or aspected by Jupiter
        val jupiterPos = chart.planetPositions.find { it.planet == Planet.JUPITER }
        if (jupiterPos != null) {
            if (YogaHelpers.areConjunct(moonPos, jupiterPos)) return true
            if (YogaHelpers.isAspecting(jupiterPos, moonPos)) return true
        }

        // Cancellation 3: Moon in own sign (Cancer) or exalted (Taurus)
        if (YogaHelpers.isInOwnSign(moonPos) || YogaHelpers.isExalted(moonPos)) return true

        // Cancellation 4: Full moon (bright)
        val moonStrength = YogaHelpers.getMoonPhaseStrength(moonPos, chart)
        if (moonStrength >= 0.8) return true

        return false
    }

    private fun createKemadrumaYoga(moonPos: PlanetPosition, chart: VedicChart): Yoga {
        val moonStrength = YogaHelpers.getMoonPhaseStrength(moonPos, chart)
        val severity = if (moonStrength < 0.3) 70.0 else 50.0

        return Yoga(
            name = "Kemadruma Yoga",
            sanskritName = "Kemadruma Yoga",
            category = YogaCategory.CHANDRA_YOGA,
            planets = listOf(Planet.MOON),
            houses = listOf(moonPos.house),
            description = "No planets in 2nd or 12th from Moon",
            effects = "Periods of loneliness or financial struggle, need to rely on self, emotional challenges",
            strength = YogaHelpers.strengthFromPercentage(severity),
            strengthPercentage = severity,
            isAuspicious = false,
            activationPeriod = "Moon Mahadasha especially",
            cancellationFactors = listOf(
                "Jupiter aspect cancels",
                "Moon in Kendra cancels",
                "Moon in own/exalted sign cancels"
            )
        )
    }

    private fun evaluateGajaKesariYoga(chart: VedicChart, moonPos: PlanetPosition): Yoga? {
        val jupiterPos = chart.planetPositions.find { it.planet == Planet.JUPITER } ?: return null

        if (YogaHelpers.isInKendraFrom(jupiterPos, moonPos)) {
            val positions = listOf(jupiterPos, moonPos)
            val (strength, cancellationReasons) = YogaHelpers.calculateYogaStrengthWithReasons(chart, positions)

            // Boost for Jupiter in good dignity
            var adjustedStrength = strength
            if (YogaHelpers.isExalted(jupiterPos) || YogaHelpers.isInOwnSign(jupiterPos)) {
                adjustedStrength *= 1.2
            }

            // Reduce for weak Moon
            val moonStrength = YogaHelpers.getMoonPhaseStrength(moonPos, chart)
            if (moonStrength < 0.5) {
                adjustedStrength *= 0.8
            }

            return Yoga(
                name = "Gaja-Kesari Yoga",
                sanskritName = "Gaja-Kesari Yoga",
                category = YogaCategory.CHANDRA_YOGA,
                planets = listOf(Planet.JUPITER, Planet.MOON),
                houses = listOf(jupiterPos.house, moonPos.house),
                description = "Jupiter in Kendra (${YogaHelpers.getHouseFrom(jupiterPos.sign, moonPos.sign)}) from Moon",
                effects = "Fame, wealth, and wisdom, respected leader, royal associations, lasting reputation",
                strength = YogaHelpers.strengthFromPercentage(adjustedStrength.coerceIn(10.0, 100.0)),
                strengthPercentage = adjustedStrength.coerceIn(10.0, 100.0),
                isAuspicious = true,
                activationPeriod = "Jupiter and Moon periods",
                cancellationFactors = cancellationReasons.ifEmpty { listOf("None - yoga is unafflicted") }
            )
        }

        return null
    }

    private fun evaluateAdhiYoga(chart: VedicChart, moonPos: PlanetPosition): Yoga? {
        val benefics = listOf(Planet.MERCURY, Planet.JUPITER, Planet.VENUS)
        val adhiHouses = listOf(6, 7, 8)

        val beneficsInAdhiHouses = chart.planetPositions.filter { pos ->
            pos.planet in benefics &&
                    YogaHelpers.getHouseFrom(pos.sign, moonPos.sign) in adhiHouses
        }

        if (beneficsInAdhiHouses.size >= 2) {
            val strength = YogaHelpers.calculateYogaStrength(chart, beneficsInAdhiHouses + moonPos) * 1.1

            val yogaType = when (beneficsInAdhiHouses.size) {
                3 -> "Complete Adhi Yoga"
                else -> "Partial Adhi Yoga"
            }

            return Yoga(
                name = yogaType,
                sanskritName = "Adhi Yoga",
                category = YogaCategory.CHANDRA_YOGA,
                planets = beneficsInAdhiHouses.map { it.planet },
                houses = beneficsInAdhiHouses.map { it.house },
                description = "${beneficsInAdhiHouses.size} benefics in 6th, 7th, 8th from Moon",
                effects = "Commander of armies, minister-like status, authority over others, respected leader",
                strength = YogaHelpers.strengthFromPercentage(strength.coerceAtMost(100.0)),
                strengthPercentage = strength.coerceAtMost(100.0),
                isAuspicious = true,
                activationPeriod = "Moon and benefic periods",
                cancellationFactors = emptyList()
            )
        }

        return null
    }
}
