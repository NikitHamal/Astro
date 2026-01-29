package com.astro.storm.ephemeris.kp

import com.astro.storm.core.model.Planet
import com.astro.storm.core.model.VedicChart
import com.astro.storm.ephemeris.kp.getCuspLongitude

/**
 * KP 4-Step Theory Engine
 *
 * Implements Prof. K.S. Krishnamurti's 4-Step Theory for event verification.
 * This is the core technique in KP for determining whether an event will happen.
 *
 * THE 4 STEPS:
 *
 * Step 1: Cusp Sub-Lord Analysis
 * - Check the sub-lord of the relevant house cusp
 * - The sub-lord should signify favorable houses for the event
 * - If sub-lord signifies 6, 8, 12 (for positive events), event is denied
 *
 * Step 2: Sub-Lord's Star-Lord Analysis
 * - Check the star-lord of the cusp sub-lord
 * - This gives the "source" of the result
 * - Star-lord should also support the event
 *
 * Step 3: Dasha Analysis
 * - Current Dasha-Bhukti-Antara lords should signify the event houses
 * - At least 2 of 3 period lords should be favorable significators
 *
 * Step 4: Transit Analysis
 * - Transiting planets should pass through favorable sub-lords
 * - Transit triggers the event promised in the natal chart
 *
 * @author AstroStorm - Ultra-Precision Vedic Astrology
 */
object KPFourStepEngine {

    /**
     * Perform complete 4-Step analysis for an event
     */
    fun analyze(
        chart: VedicChart,
        houseGroup: HouseGroup,
        currentDashaLord: Planet,
        currentBhuktiLord: Planet,
        currentAntarLord: Planet? = null,
        transitPositions: Map<Planet, Double>? = null
    ): FourStepTheoryResult {
        // Calculate all significators
        val significators = KPSignificatorCalculator.calculateAllSignificators(chart)

        // Get the primary cusp for this house group
        val primaryCusp = houseGroup.favorableHouses.firstOrNull() ?: 1
        val cuspLongitude = chart.getCuspLongitude(primaryCusp)
        val cuspPosition = KPSubCalculator.getKPPosition(cuspLongitude)

        // Step 1: Cusp Sub-Lord Analysis
        val step1 = analyzeStep1(
            cuspPosition = cuspPosition,
            primaryCusp = primaryCusp,
            houseGroup = houseGroup,
            significators = significators
        )

        // Step 2: Sub-Lord's Star-Lord Analysis
        val step2 = analyzeStep2(
            cuspPosition = cuspPosition,
            houseGroup = houseGroup,
            significators = significators
        )

        // Step 3: Dasha Analysis
        val step3 = analyzeStep3(
            dashaLord = currentDashaLord,
            bhuktiLord = currentBhuktiLord,
            antarLord = currentAntarLord,
            houseGroup = houseGroup,
            significators = significators
        )

        // Step 4: Transit Analysis
        val step4 = analyzeStep4(
            chart = chart,
            transitPositions = transitPositions,
            houseGroup = houseGroup,
            significators = significators
        )

        // Calculate overall verdict
        val (verdict, confidence) = calculateVerdict(step1, step2, step3, step4)

        // Generate explanation
        val explanation = generateExplanation(
            houseGroup = houseGroup,
            step1 = step1,
            step2 = step2,
            step3 = step3,
            step4 = step4,
            verdict = verdict
        )

        return FourStepTheoryResult(
            query = "Will ${houseGroup.displayName} matters be successful?",
            houseGroup = houseGroup,
            relevantHouses = houseGroup.favorableHouses,
            step1Result = step1,
            step2Result = step2,
            step3Result = step3,
            step4Result = step4,
            overallVerdict = verdict,
            confidence = confidence,
            explanation = explanation
        )
    }

    /**
     * Step 1: Analyze the cusp sub-lord
     */
    private fun analyzeStep1(
        cuspPosition: KPPosition,
        primaryCusp: Int,
        houseGroup: HouseGroup,
        significators: Map<Planet, KPSignificators>
    ): Step1Result {
        val subLord = cuspPosition.subLord
        val subLordSignifications = significators[subLord] ?: return Step1Result(
            primaryCusp = primaryCusp,
            subLord = subLord,
            subLordSignifications = createEmptySignificators(subLord),
            supportsEvent = false,
            explanation = "Unable to calculate sub-lord significators."
        )

        // Check if sub-lord signifies favorable houses
        val favorableCount = houseGroup.favorableHouses.count { 
            subLordSignifications.signifiesHouse(it) 
        }
        val unfavorableCount = houseGroup.unfavorableHouses.count { 
            subLordSignifications.signifiesHouse(it) 
        }

        val supportsEvent = favorableCount > unfavorableCount

        val explanation = buildString {
            append("House $primaryCusp Sub-Lord is ${subLord.displayName}. ")
            val signifiedFavorable = houseGroup.favorableHouses.filter { 
                subLordSignifications.signifiesHouse(it) 
            }
            val signifiedUnfavorable = houseGroup.unfavorableHouses.filter { 
                subLordSignifications.signifiesHouse(it) 
            }

            if (signifiedFavorable.isNotEmpty()) {
                append("Signifies favorable houses: ${signifiedFavorable.joinToString(", ")}. ")
            }
            if (signifiedUnfavorable.isNotEmpty()) {
                append("Also signifies unfavorable houses: ${signifiedUnfavorable.joinToString(", ")}. ")
            }

            if (supportsEvent) {
                append("Step 1 PASSED - Sub-lord supports the event.")
            } else {
                append("Step 1 FAILED - Sub-lord does not support the event.")
            }
        }

        return Step1Result(
            primaryCusp = primaryCusp,
            subLord = subLord,
            subLordSignifications = subLordSignifications,
            supportsEvent = supportsEvent,
            explanation = explanation
        )
    }

    /**
     * Step 2: Analyze the sub-lord's star-lord
     */
    private fun analyzeStep2(
        cuspPosition: KPPosition,
        houseGroup: HouseGroup,
        significators: Map<Planet, KPSignificators>
    ): Step2Result {
        // Get the star-lord of the sub-lord's position
        // This requires knowing where the sub-lord is placed in the chart
        val subLord = cuspPosition.subLord
        val starLord = cuspPosition.starLord

        val starLordSignifications = significators[starLord] ?: return Step2Result(
            starLord = starLord,
            starLordSignifications = createEmptySignificators(starLord),
            supportsEvent = false,
            explanation = "Unable to calculate star-lord significators."
        )

        val favorableCount = houseGroup.favorableHouses.count { 
            starLordSignifications.signifiesHouse(it) 
        }
        val unfavorableCount = houseGroup.unfavorableHouses.count { 
            starLordSignifications.signifiesHouse(it) 
        }

        val supportsEvent = favorableCount >= unfavorableCount

        val explanation = buildString {
            append("Star-Lord of cusp is ${starLord.displayName}. ")
            val signifiedFavorable = houseGroup.favorableHouses.filter { 
                starLordSignifications.signifiesHouse(it) 
            }
            val signifiedUnfavorable = houseGroup.unfavorableHouses.filter { 
                starLordSignifications.signifiesHouse(it) 
            }

            if (signifiedFavorable.isNotEmpty()) {
                append("Signifies favorable houses: ${signifiedFavorable.joinToString(", ")}. ")
            }
            if (signifiedUnfavorable.isNotEmpty()) {
                append("Also signifies unfavorable houses: ${signifiedUnfavorable.joinToString(", ")}. ")
            }

            if (supportsEvent) {
                append("Step 2 PASSED - Star-lord supports the event.")
            } else {
                append("Step 2 FAILED - Star-lord does not support the event.")
            }
        }

        return Step2Result(
            starLord = starLord,
            starLordSignifications = starLordSignifications,
            supportsEvent = supportsEvent,
            explanation = explanation
        )
    }

    /**
     * Step 3: Analyze current Dasha periods
     */
    private fun analyzeStep3(
        dashaLord: Planet,
        bhuktiLord: Planet,
        antarLord: Planet?,
        houseGroup: HouseGroup,
        significators: Map<Planet, KPSignificators>
    ): Step3Result {
        val dashaSignifications = mutableMapOf<Planet, KPSignificators>()

        dashaSignifications[dashaLord] = significators[dashaLord] ?: createEmptySignificators(dashaLord)
        dashaSignifications[bhuktiLord] = significators[bhuktiLord] ?: createEmptySignificators(bhuktiLord)
        antarLord?.let {
            dashaSignifications[it] = significators[it] ?: createEmptySignificators(it)
        }

        // Count how many period lords signify favorable houses
        var supportingPeriods = 0
        var totalPeriods = if (antarLord != null) 3 else 2

        val periodAnalysis = mutableListOf<String>()

        // Analyze Dasha lord
        val dashaFavorable = houseGroup.favorableHouses.filter { 
            dashaSignifications[dashaLord]?.signifiesHouse(it) == true 
        }
        if (dashaFavorable.isNotEmpty()) {
            supportingPeriods++
            periodAnalysis.add("Dasha ${dashaLord.displayName} signifies ${dashaFavorable.joinToString(",")}")
        }

        // Analyze Bhukti lord
        val bhuktiFavorable = houseGroup.favorableHouses.filter { 
            dashaSignifications[bhuktiLord]?.signifiesHouse(it) == true 
        }
        if (bhuktiFavorable.isNotEmpty()) {
            supportingPeriods++
            periodAnalysis.add("Bhukti ${bhuktiLord.displayName} signifies ${bhuktiFavorable.joinToString(",")}")
        }

        // Analyze Antar lord if present
        antarLord?.let {
            val antarFavorable = houseGroup.favorableHouses.filter { house ->
                dashaSignifications[it]?.signifiesHouse(house) == true
            }
            if (antarFavorable.isNotEmpty()) {
                supportingPeriods++
                periodAnalysis.add("Antara ${it.displayName} signifies ${antarFavorable.joinToString(",")}")
            }
        }

        val supportsEvent = supportingPeriods >= (totalPeriods / 2 + 1)

        val explanation = buildString {
            append("Current Period: ${dashaLord.displayName}-${bhuktiLord.displayName}")
            antarLord?.let { append("-${it.displayName}") }
            append(". ")
            periodAnalysis.forEach { append("$it. ") }
            append("$supportingPeriods of $totalPeriods periods support the event. ")
            if (supportsEvent) {
                append("Step 3 PASSED - Dasha supports the event.")
            } else {
                append("Step 3 FAILED - Dasha does not support the event.")
            }
        }

        return Step3Result(
            currentDashaLord = dashaLord,
            currentBhuktiLord = bhuktiLord,
            currentAntarLord = antarLord,
            dashaSignifications = dashaSignifications,
            supportsEvent = supportsEvent,
            explanation = explanation
        )
    }

    /**
     * Step 4: Analyze transits
     */
    private fun analyzeStep4(
        chart: VedicChart,
        transitPositions: Map<Planet, Double>?,
        houseGroup: HouseGroup,
        significators: Map<Planet, KPSignificators>
    ): Step4Result {
        if (transitPositions == null || transitPositions.isEmpty()) {
            return Step4Result(
                significantTransits = emptyList(),
                supportsEvent = true, // Neutral if no transit data
                explanation = "Transit data not provided. Step 4 considered neutral."
            )
        }

        val significantTransits = mutableListOf<KPTransit>()
        var supportingTransits = 0
        var opposingTransits = 0

        for ((planet, longitude) in transitPositions) {
            val kpPosition = KPSubCalculator.getKPPosition(longitude)
            val planetSignificators = significators[planet] ?: continue

            // Check if transiting planet's sub-lord signifies relevant houses
            val subLordSignificators = significators[kpPosition.subLord]

            val signifiesFavorable = houseGroup.favorableHouses.any { 
                subLordSignificators?.signifiesHouse(it) == true || planetSignificators.signifiesHouse(it)
            }
            val signifiesUnfavorable = houseGroup.unfavorableHouses.any { 
                subLordSignificators?.signifiesHouse(it) == true
            }

            val isRelevant = signifiesFavorable || signifiesUnfavorable

            if (isRelevant) {
                significantTransits.add(KPTransit(
                    planet = planet,
                    position = kpPosition,
                    significations = planetSignificators,
                    isRelevant = true
                ))

                if (signifiesFavorable && !signifiesUnfavorable) {
                    supportingTransits++
                } else if (signifiesUnfavorable && !signifiesFavorable) {
                    opposingTransits++
                }
            }
        }

        val supportsEvent = supportingTransits >= opposingTransits

        val explanation = buildString {
            append("Transit Analysis: ")
            if (significantTransits.isEmpty()) {
                append("No significant transits found. ")
            } else {
                append("$supportingTransits supporting, $opposingTransits opposing transits. ")
                significantTransits.take(3).forEach { transit ->
                    append("${transit.planet.displayName} in ${transit.position.subLord.displayName} sub. ")
                }
            }
            if (supportsEvent) {
                append("Step 4 PASSED - Transits support the event.")
            } else {
                append("Step 4 FAILED - Transits do not support the event.")
            }
        }

        return Step4Result(
            significantTransits = significantTransits,
            supportsEvent = supportsEvent,
            explanation = explanation
        )
    }

    /**
     * Calculate overall verdict from all 4 steps
     */
    private fun calculateVerdict(
        step1: Step1Result,
        step2: Step2Result,
        step3: Step3Result,
        step4: Step4Result
    ): Pair<KPVerdict, Double> {
        val passedSteps = listOf(
            step1.supportsEvent,
            step2.supportsEvent,
            step3.supportsEvent,
            step4.supportsEvent
        ).count { it }

        val verdict = when (passedSteps) {
            4 -> KPVerdict.STRONGLY_POSITIVE
            3 -> KPVerdict.POSITIVE
            2 -> KPVerdict.NEUTRAL
            1 -> KPVerdict.NEGATIVE
            0 -> KPVerdict.STRONGLY_NEGATIVE
            else -> KPVerdict.NEUTRAL
        }

        // Calculate confidence (weighted by step importance)
        // Step 1 is most important (40%), Step 3 second (30%), Step 2 (20%), Step 4 (10%)
        var confidence = 0.0
        if (step1.supportsEvent) confidence += 0.40
        if (step2.supportsEvent) confidence += 0.20
        if (step3.supportsEvent) confidence += 0.30
        if (step4.supportsEvent) confidence += 0.10

        return verdict to confidence
    }

    /**
     * Generate comprehensive explanation
     */
    private fun generateExplanation(
        houseGroup: HouseGroup,
        step1: Step1Result,
        step2: Step2Result,
        step3: Step3Result,
        step4: Step4Result,
        verdict: KPVerdict
    ): String {
        return buildString {
            appendLine("KP 4-STEP THEORY ANALYSIS FOR ${houseGroup.displayName.uppercase()}")
            appendLine("=" .repeat(50))
            appendLine()
            appendLine("Favorable Houses: ${houseGroup.favorableHouses.joinToString(", ")}")
            appendLine("Unfavorable Houses: ${houseGroup.unfavorableHouses.joinToString(", ")}")
            appendLine()
            appendLine("STEP 1 - CUSP SUB-LORD: ${if (step1.supportsEvent) "PASS" else "FAIL"}")
            appendLine(step1.explanation)
            appendLine()
            appendLine("STEP 2 - STAR-LORD: ${if (step2.supportsEvent) "PASS" else "FAIL"}")
            appendLine(step2.explanation)
            appendLine()
            appendLine("STEP 3 - DASHA: ${if (step3.supportsEvent) "PASS" else "FAIL"}")
            appendLine(step3.explanation)
            appendLine()
            appendLine("STEP 4 - TRANSIT: ${if (step4.supportsEvent) "PASS" else "FAIL"}")
            appendLine(step4.explanation)
            appendLine()
            appendLine("=" .repeat(50))
            appendLine("VERDICT: ${verdict.displayName}")
            appendLine()
            when (verdict) {
                KPVerdict.STRONGLY_POSITIVE -> {
                    appendLine("All 4 steps support the event. The event is strongly indicated ")
                    appendLine("and will likely manifest during favorable dasha periods.")
                }
                KPVerdict.POSITIVE -> {
                    appendLine("3 of 4 steps support the event. The event is likely to happen ")
                    appendLine("with some minor challenges or delays.")
                }
                KPVerdict.NEUTRAL -> {
                    appendLine("Mixed indications. The event may or may not happen depending ")
                    appendLine("on additional factors and efforts.")
                }
                KPVerdict.NEGATIVE -> {
                    appendLine("Only 1 step supports the event. Significant challenges exist. ")
                    appendLine("The event is unlikely without major changes in circumstances.")
                }
                KPVerdict.STRONGLY_NEGATIVE -> {
                    appendLine("No steps support the event. The event is strongly denied ")
                    appendLine("in the current configuration.")
                }
            }
        }
    }

    /**
     * Create empty significators for a planet
     */
    private fun createEmptySignificators(planet: Planet): KPSignificators {
        return KPSignificators(
            planet = planet,
            primarySignifications = emptyList(),
            secondarySignifications = emptyList(),
            tertiarySignifications = emptyList(),
            quaternarySignifications = emptyList(),
            allSignifications = emptySet()
        )
    }

    /**
     * Quick check if an event is promised (Step 1 only)
     */
    fun isEventPromised(chart: VedicChart, houseGroup: HouseGroup): Boolean {
        val primaryCusp = houseGroup.favorableHouses.firstOrNull() ?: return false
        val cuspLongitude = chart.getCuspLongitude(primaryCusp)
        val cuspPosition = KPSubCalculator.getKPPosition(cuspLongitude)

        val significators = KPSignificatorCalculator.calculateAllSignificators(chart)
        val subLordSignifications = significators[cuspPosition.subLord] ?: return false

        val favorableCount = houseGroup.favorableHouses.count { 
            subLordSignifications.signifiesHouse(it) 
        }
        val unfavorableCount = houseGroup.unfavorableHouses.count { 
            subLordSignifications.signifiesHouse(it) 
        }

        return favorableCount > unfavorableCount
    }
}
