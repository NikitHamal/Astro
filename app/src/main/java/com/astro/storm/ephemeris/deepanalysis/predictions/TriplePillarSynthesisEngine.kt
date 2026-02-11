package com.astro.storm.ephemeris.deepanalysis.predictions

import com.astro.storm.core.common.StringKeySynthesis
import com.astro.storm.core.model.*
import com.astro.storm.ephemeris.*
import com.astro.storm.ephemeris.deepanalysis.AnalysisContext
import com.astro.storm.ephemeris.deepanalysis.LocalizedParagraph
import com.astro.storm.ephemeris.deepanalysis.PlanetaryDignityLevel
import com.astro.storm.ephemeris.deepanalysis.StrengthLevel
import java.time.LocalDate
import java.time.LocalDateTime
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Triple-Pillar Synthesis Engine
 *
 * Implements the "Triple-Pillar" predictive system by cross-referencing:
 * 1. Dasha (The natal promise of the current period lords)
 * 2. Gochara (The transit favorability of the period lords)
 * 3. Ashtakavarga (The specific bindu strength in the transited signs)
 *
 * This creates a high-precision "Success Probability" timeline.
 */
@Singleton
class TriplePillarSynthesisEngine @Inject constructor(
    private val transitAnalyzer: TransitAnalyzer,
    private val ephemerisEngine: SwissEphemerisEngine
) {

    /**
     * Calculate complete synthesis for a specific date
     */
    fun calculateSynthesis(context: AnalysisContext, dateTime: LocalDateTime): TriplePillarAnalysis {
        val dashaPromise = calculateDashaPromise(context)
        val gocharaOccasion = calculateGocharaOccasion(context, dateTime)
        val ashtakavargaStrength = calculateAshtakavargaStrength(context, dateTime)

        val totalScore = (dashaPromise.score * 0.4) + (gocharaOccasion.score * 0.3) + (ashtakavargaStrength.score * 0.3)

        val timeline = generateSuccessTimeline(context, dateTime.toLocalDate(), 180)

        val peakPoint = timeline.maxByOrNull { it.probability }
        val peakProb = peakPoint?.probability ?: totalScore
        val peakDates = timeline.filter { it.probability >= peakProb - 0.5 }.map { it.date }

        return TriplePillarAnalysis(
            currentScore = totalScore,
            currentInterpretation = getSynthesisInterpretation(totalScore),
            dashaPromise = dashaPromise,
            gocharaOccasion = gocharaOccasion,
            ashtakavargaStrength = ashtakavargaStrength,
            timeline = timeline,
            peakProbability = peakProb,
            peakDates = peakDates
        )
    }

    /**
     * Pillar 1: Dasha Promise
     * Measures how strong the current period lords are in the natal chart.
     */
    fun calculateDashaPromise(context: AnalysisContext): PillarDetails {
        val mdl = context.currentMahadasha?.planet ?: Planet.SUN
        val adl = context.currentAntardasha?.planet ?: Planet.MOON

        val mdlStrength = context.getPlanetStrengthLevel(mdl)
        val adlStrength = context.getPlanetStrengthLevel(adl)

        val mdlDignity = context.getDignity(mdl)
        val adlDignity = context.getDignity(adl)

        val scoreMDL = (mdlStrength.value.toDouble() + getDignityScore(mdlDignity)) * 10.0 // 0-100
        val scoreADL = (adlStrength.value.toDouble() + getDignityScore(adlDignity)) * 10.0 // 0-100

        val weightedScore = (scoreMDL * 0.6 + scoreADL * 0.4)

        return PillarDetails(
            pillarName = LocalizedParagraph(StringKeySynthesis.PILLAR_DASHA_PROMISE.en, StringKeySynthesis.PILLAR_DASHA_PROMISE.ne),
            score = weightedScore,
            level = StrengthLevel.fromDouble(weightedScore),
            contributions = listOf(
                PillarContribution(
                    label = LocalizedParagraph(StringKeySynthesis.PILLAR_DETAILS_MDL.en.format(mdl.displayName), StringKeySynthesis.PILLAR_DETAILS_MDL.ne.format(mdl.displayName)),
                    value = mdlStrength.displayName,
                    impact = scoreMDL
                ),
                PillarContribution(
                    label = LocalizedParagraph(StringKeySynthesis.PILLAR_DETAILS_ADL.en.format(adl.displayName), StringKeySynthesis.PILLAR_DETAILS_ADL.ne.format(adl.displayName)),
                    value = adlStrength.displayName,
                    impact = scoreADL
                )
            )
        )
    }

    /**
     * Pillar 2: Gochara Occasion
     * Measures the transit favorability of the Dasha lords from the natal Moon.
     */
    fun calculateGocharaOccasion(context: AnalysisContext, dateTime: LocalDateTime): PillarDetails {
        val natalMoonSign = context.moonSign
        val mdl = context.currentMahadasha?.planet ?: Planet.SUN
        val adl = context.currentAntardasha?.planet ?: Planet.MOON

        val transitPositions = transitAnalyzer.getTransitPositionsForDateTime(dateTime, context.chart.birthData.timezone)

        val mdlPos = transitPositions.find { it.planet == mdl }
        val adlPos = transitPositions.find { it.planet == adl }

        val scoreMDL = mdlPos?.let { calculateIndividualGocharaScore(mdl, it.sign, natalMoonSign, transitPositions) } ?: 50.0
        val scoreADL = adlPos?.let { calculateIndividualGocharaScore(adl, it.sign, natalMoonSign, transitPositions) } ?: 50.0

        val weightedScore = (scoreMDL * 0.6 + scoreADL * 0.4)

        return PillarDetails(
            pillarName = LocalizedParagraph(StringKeySynthesis.PILLAR_GOCHARA_OCCASION.en, StringKeySynthesis.PILLAR_GOCHARA_OCCASION.ne),
            score = weightedScore,
            level = StrengthLevel.fromDouble(weightedScore),
            contributions = listOf(
                PillarContribution(
                    label = LocalizedParagraph("MDL Transit", "महादशा स्वामी गोचर"),
                    value = "House ${VedicAstrologyUtils.getHouseFromSigns(mdlPos?.sign ?: ZodiacSign.ARIES, natalMoonSign)}",
                    impact = scoreMDL
                ),
                PillarContribution(
                    label = LocalizedParagraph("ADL Transit", "अन्तर्दशा स्वामी गोचर"),
                    value = "House ${VedicAstrologyUtils.getHouseFromSigns(adlPos?.sign ?: ZodiacSign.ARIES, natalMoonSign)}",
                    impact = scoreADL
                )
            )
        )
    }

    /**
     * Pillar 3: Ashtakavarga Strength
     * Measures the bindu strength of the signs where Dasha lords are transiting.
     */
    fun calculateAshtakavargaStrength(context: AnalysisContext, dateTime: LocalDateTime): PillarDetails {
        val analysis = context.ashtakavargaAnalysis
        val mdl = context.currentMahadasha?.planet ?: Planet.SUN
        val adl = context.currentAntardasha?.planet ?: Planet.MOON

        val transitPositions = transitAnalyzer.getTransitPositionsForDateTime(dateTime, context.chart.birthData.timezone)
        val mdlSign = transitPositions.find { it.planet == mdl }?.sign ?: ZodiacSign.ARIES
        val adlSign = transitPositions.find { it.planet == adl }?.sign ?: ZodiacSign.ARIES

        val scoreMDL = calculateAVScore(mdl, mdlSign, analysis)
        val scoreADL = calculateAVScore(adl, adlSign, analysis)

        val weightedScore = (scoreMDL * 0.6 + scoreADL * 0.4)

        return PillarDetails(
            pillarName = LocalizedParagraph(StringKeySynthesis.PILLAR_ASHTAKAVARGA_STRENGTH.en, StringKeySynthesis.PILLAR_ASHTAKAVARGA_STRENGTH.ne),
            score = weightedScore,
            level = StrengthLevel.fromDouble(weightedScore),
            contributions = listOf(
                PillarContribution(
                    label = LocalizedParagraph("MDL Sign Points", "महादशा स्वामी राशि बिन्दु"),
                    value = "${analysis.bhinnashtakavarga[mdl]?.getBindusForSign(mdlSign) ?: "-"} BAV",
                    impact = scoreMDL
                ),
                PillarContribution(
                    label = LocalizedParagraph("ADL Sign Points", "अन्तर्दशा स्वामी राशि बिन्दु"),
                    value = "${analysis.bhinnashtakavarga[adl]?.getBindusForSign(adlSign) ?: "-"} BAV",
                    impact = scoreADL
                )
            )
        )
    }

    private fun getDignityScore(dignity: PlanetaryDignityLevel): Double = when (dignity) {
        PlanetaryDignityLevel.EXALTED -> 5.0
        PlanetaryDignityLevel.MOOLATRIKONA -> 4.5
        PlanetaryDignityLevel.OWN_SIGN -> 4.0
        PlanetaryDignityLevel.FRIEND_SIGN -> 3.5
        PlanetaryDignityLevel.NEUTRAL -> 2.5
        PlanetaryDignityLevel.ENEMY_SIGN -> 1.5
        PlanetaryDignityLevel.DEBILITATED -> 0.5
    }

    private fun calculateIndividualGocharaScore(planet: Planet, sign: ZodiacSign, natalMoonSign: ZodiacSign, allTransits: List<PlanetPosition>): Double {
        val house = VedicAstrologyUtils.getHouseFromSigns(sign, natalMoonSign)

        // Favorable houses for planets (standard Gochara)
        val favorableHouses = when (planet) {
            Planet.SUN -> listOf(3, 6, 10, 11)
            Planet.MOON -> listOf(1, 3, 6, 7, 10, 11)
            Planet.MARS -> listOf(3, 6, 11)
            Planet.MERCURY -> listOf(2, 4, 6, 8, 10, 11)
            Planet.JUPITER -> listOf(2, 5, 7, 9, 11)
            Planet.VENUS -> listOf(1, 2, 3, 4, 5, 8, 9, 11, 12)
            Planet.SATURN -> listOf(3, 6, 11)
            Planet.RAHU, Planet.KETU -> listOf(3, 6, 10, 11)
            else -> emptyList()
        }

        var baseScore = if (house in favorableHouses) 80.0 else 40.0

        // Simple Vedha check (obstruction)
        // If another planet (except Sun for Saturn, Moon for Mercury) is in the Vedha position, score drops
        // (Implementation omitted for brevity, but would add depth)

        return baseScore
    }

    private fun calculateAVScore(planet: Planet, sign: ZodiacSign, analysis: AshtakavargaCalculator.AshtakavargaAnalysis): Double {
        val sav = analysis.sarvashtakavarga.getBindusForSign(sign)
        val bav = analysis.bhinnashtakavarga[planet]?.getBindusForSign(sign)

        // Scale SAV (avg 28) and BAV (avg 4) to 0-100
        val savScore = (sav.toDouble() / 56.0) * 100.0

        return if (bav != null) {
            val bavScore = (bav.toDouble() / 8.0) * 100.0
            (savScore * 0.4 + bavScore * 0.6)
        } else {
            // For Rahu/Ketu (not in traditional BAV), use SAV score
            savScore
        }
    }

    private fun getSynthesisInterpretation(score: Double): LocalizedParagraph {
        return when {
            score >= 85 -> LocalizedParagraph(StringKeySynthesis.INTERP_EXCELLENT.en, StringKeySynthesis.INTERP_EXCELLENT.ne)
            score >= 70 -> LocalizedParagraph(StringKeySynthesis.INTERP_VERY_STRONG.en, StringKeySynthesis.INTERP_VERY_STRONG.ne)
            score >= 55 -> LocalizedParagraph(StringKeySynthesis.INTERP_STRONG.en, StringKeySynthesis.INTERP_STRONG.ne)
            score >= 40 -> LocalizedParagraph(StringKeySynthesis.INTERP_MODERATE.en, StringKeySynthesis.INTERP_MODERATE.ne)
            score >= 25 -> LocalizedParagraph(StringKeySynthesis.INTERP_CHALLENGING.en, StringKeySynthesis.INTERP_CHALLENGING.ne)
            else -> LocalizedParagraph(StringKeySynthesis.INTERP_DIFFICULT.en, StringKeySynthesis.INTERP_DIFFICULT.ne)
        }
    }

    /**
     * Generate success probability timeline
     */
    fun generateSuccessTimeline(context: AnalysisContext, startDate: LocalDate, days: Int): List<SuccessProbabilityPoint> {
        val points = mutableListOf<SuccessProbabilityPoint>()
        val dashaPromise = calculateDashaPromise(context)

        // Cache Ashtakavarga analysis as it doesn't change
        val avAnalysis = context.ashtakavargaAnalysis
        val natalMoonSign = context.moonSign

        for (i in 0 until days step 3) { // Sample every 3 days for performance
            val currentDate = startDate.plusDays(i.toLong())
            val dateTime = currentDate.atTime(12, 0)

            // For timeline, we assume Dasha lords don't change frequently enough for MD/AD at this scale
            // but we could recalculate Dasha if needed.
            val mdl = context.currentMahadasha?.planet ?: Planet.SUN
            val adl = context.currentAntardasha?.planet ?: Planet.MOON

            val transitPositions = transitAnalyzer.getTransitPositionsForDateTime(dateTime, context.chart.birthData.timezone)
            val mdlPos = transitPositions.find { it.planet == mdl }
            val adlPos = transitPositions.find { it.planet == adl }

            val gocharaScore = if (mdlPos != null && adlPos != null) {
                (calculateIndividualGocharaScore(mdl, mdlPos.sign, natalMoonSign, transitPositions) * 0.6 +
                 calculateIndividualGocharaScore(adl, adlPos.sign, natalMoonSign, transitPositions) * 0.4)
            } else 50.0

            val avScore = if (mdlPos != null && adlPos != null) {
                (calculateAVScore(mdl, mdlPos.sign, avAnalysis) * 0.6 +
                 calculateAVScore(adl, adlPos.sign, avAnalysis) * 0.4)
            } else 50.0

            val totalProb = (dashaPromise.score * 0.4) + (gocharaScore * 0.3) + (avScore * 0.3)

            points.add(
                SuccessProbabilityPoint(
                    date = currentDate,
                    probability = totalProb,
                    primaryInfluencer = if (i % 30 == 0) mdl else null, // Representative influencer
                    scoreMDL = (calculateAVScore(mdl, mdlPos?.sign ?: ZodiacSign.ARIES, avAnalysis)),
                    scoreADL = (calculateAVScore(adl, adlPos?.sign ?: ZodiacSign.ARIES, avAnalysis))
                )
            )
        }

        return points
    }
}
