package com.astro.storm.ephemeris.yoga

import com.astro.storm.core.model.Planet
import com.astro.storm.core.model.PlanetPosition
import com.astro.storm.core.model.VedicChart
import com.astro.storm.ephemeris.KemadrumaYogaCalculator

/**
 * Chandra (Moon) Yoga Evaluator - Lunar Combinations
 *
 * Chandra Yogas are formed based on the Moon's position and its relationship
 * with other planets. The Moon represents mind, emotions, and public life.
 *
 * This evaluator integrates with the detailed [KemadrumaYogaCalculator]
 * for exhaustive Moon isolation analysis.
 *
 * Types evaluated:
 * 1. Sunafa Yoga - Planet (not Sun) in 2nd from Moon
 * 2. Anafa Yoga - Planet (not Sun) in 12th from Moon
 * 3. Durudhara Yoga - Planets in both 2nd and 12th from Moon
 * 4. Kemadruma Yoga - No planets in 2nd and 12th from Moon (with exhaustive Bhanga analysis)
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

        // 1-4. Lunar Position Yogas (Sunafa, Anafa, Durudhara, Kemadruma)
        evaluateLunarPositionYogas(chart, moonPos, yogas)

        // 5. Gaja-Kesari Yoga
        evaluateGajaKesariYoga(chart, moonPos)?.let { yogas.add(it) }

        // 6. Adhi Yoga
        evaluateAdhiYoga(chart, moonPos)?.let { yogas.add(it) }

        return yogas
    }

    private fun evaluateLunarPositionYogas(chart: VedicChart, moonPos: PlanetPosition, yogas: MutableList<Yoga>) {
        val kemadrumaAnalysis = KemadrumaYogaCalculator.analyzeKemadruma(chart) ?: return

        // Map Kemadruma from detailed calculator
        if (kemadrumaAnalysis.isKemadrumaFormed) {
            val isFullyCancelled = kemadrumaAnalysis.effectiveStatus == KemadrumaYogaCalculator.KemadrumaStatus.FULLY_CANCELLED ||
                                   kemadrumaAnalysis.effectiveStatus == KemadrumaYogaCalculator.KemadrumaStatus.NOT_PRESENT

            val severity = kemadrumaAnalysis.effectiveStatus.severity * 20.0

            yogas.add(Yoga(
                name = if (isFullyCancelled) "Kemadruma Bhanga" else "Kemadruma Yoga",
                sanskritName = "Kemadruma Yoga",
                category = YogaCategory.CHANDRA_YOGA,
                planets = listOf(Planet.MOON),
                houses = listOf(moonPos.house),
                description = "No planets in 2nd or 12th from Moon. " +
                             if (isFullyCancelled) "Fully cancelled by beneficial factors." else "Status: ${kemadrumaAnalysis.effectiveStatus.displayName}",
                effects = if (isFullyCancelled) "Resilience and overcoming obstacles after struggle."
                         else "Emotional sensitivity, feelings of isolation, financial instability.",
                strength = YogaHelpers.strengthFromPercentage(severity.coerceIn(10.0, 100.0)),
                strengthPercentage = severity.coerceIn(10.0, 100.0),
                isAuspicious = isFullyCancelled,
                activationPeriod = "Moon periods",
                cancellationFactors = kemadrumaAnalysis.cancellations.map { it.description },
                detailedResult = kemadrumaAnalysis,
                
                // Add high-precision keys
                nameKey = if (isFullyCancelled) com.astro.storm.core.common.StringKeyMatch.YOGA_KEMADRUMA_BHANGA else com.astro.storm.core.common.StringKeyMatch.YOGA_KEMADRUMA,
                effectsKey = if (isFullyCancelled) com.astro.storm.core.common.StringKeyMatch.YOGA_EFFECT_KEMADRUMA_BHANGA else com.astro.storm.core.common.StringKeyMatch.YOGA_EFFECT_KEMADRUMA
            ))
        }

        // Calculate houses from Moon for Sunafa, Anafa, Durudhara
        val houseFromMoon = { pos: PlanetPosition ->
            YogaHelpers.getHouseFrom(pos.sign, moonPos.sign)
        }

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
            cancellationFactors = emptyList(),
            nameKey = com.astro.storm.core.common.StringKeyMatch.YOGA_SUNAFA,
            effectsKey = com.astro.storm.core.common.StringKeyMatch.YOGA_EFFECT_SUNAFA
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
            cancellationFactors = emptyList(),
            nameKey = com.astro.storm.core.common.StringKeyMatch.YOGA_ANAFA,
            effectsKey = com.astro.storm.core.common.StringKeyMatch.YOGA_EFFECT_ANAFA
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
            cancellationFactors = emptyList(),
            nameKey = com.astro.storm.core.common.StringKeyMatch.YOGA_DURUDHARA,
            effectsKey = com.astro.storm.core.common.StringKeyMatch.YOGA_EFFECT_DURUDHARA
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
                cancellationFactors = cancellationReasons.ifEmpty { listOf("None - yoga is unafflicted") },
                nameKey = com.astro.storm.core.common.StringKeyMatch.YOGA_GAJA_KESARI,
                effectsKey = com.astro.storm.core.common.StringKeyMatch.YOGA_EFFECT_GAJA_KESARI
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
                cancellationFactors = emptyList(),
                nameKey = com.astro.storm.core.common.StringKeyMatch.YOGA_ADHI,
                effectsKey = com.astro.storm.core.common.StringKeyMatch.YOGA_EFFECT_ADHI
            )
        }

        return null
    }
}
