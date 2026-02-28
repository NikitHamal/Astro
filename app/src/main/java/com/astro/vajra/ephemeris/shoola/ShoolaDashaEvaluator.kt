package com.astro.vajra.ephemeris.shoola

import com.astro.vajra.core.model.Planet
import com.astro.vajra.core.model.VedicChart
import com.astro.vajra.core.model.ZodiacSign
import com.astro.vajra.ephemeris.shoola.ShoolaHelpers.isDebilitated
import com.astro.vajra.ephemeris.shoola.ShoolaHelpers.isExalted
import com.astro.vajra.ephemeris.shoola.ShoolaHelpers.isOwnSign
import java.time.LocalDateTime

object ShoolaDashaEvaluator {

    fun assessPeriodNature(chart: VedicChart, sign: ZodiacSign, triMurti: TriMurtiAnalysis): PeriodNature {
        var score = 50; if (triMurti.rudraSign == sign) score -= 30; if (triMurti.brahmaSign == sign) score += 25
        chart.planetPositions.filter { ZodiacSign.fromLongitude(it.longitude) == sign }.forEach { score += when (it.planet) { Planet.JUPITER -> 15; Planet.VENUS -> 12; Planet.MERCURY -> 8; Planet.MOON -> 5; Planet.SATURN -> -15; Planet.MARS -> -12; Planet.RAHU -> -10; Planet.KETU -> -8; else -> 0 } }
        val lp = chart.planetPositions.find { it.planet == sign.ruler }; lp?.let { val s = ZodiacSign.fromLongitude(it.longitude); if (isExalted(sign.ruler, s)) score += 15; if (isDebilitated(sign.ruler, s)) score -= 15; if (isOwnSign(sign.ruler, s)) score += 10 }
        return when { score >= 75 -> PeriodNature.FAVORABLE; score >= 55 -> PeriodNature.SUPPORTIVE; score >= 40 -> PeriodNature.MIXED; score >= 25 -> PeriodNature.CHALLENGING; else -> PeriodNature.VERY_CHALLENGING }
    }

    fun assessHealthSeverity(chart: VedicChart, sign: ZodiacSign, triMurti: TriMurtiAnalysis): HealthSeverity {
        var risk = (if (triMurti.rudraSign == sign) 40 else 0) + (if (triMurti.secondaryRudraSign == sign) 25 else 0) + (if (triMurti.maheshwaraSign == sign) 30 else 0)
        val h = ((sign.number - ZodiacSign.fromLongitude(chart.ascendant).number + 12) % 12) + 1
        if (h in listOf(6, 8, 12)) risk += 20; risk += chart.planetPositions.count { ZodiacSign.fromLongitude(it.longitude) == sign && it.planet in listOf(Planet.SATURN, Planet.MARS, Planet.RAHU, Planet.KETU) } * 10
        return when { risk >= 80 -> HealthSeverity.CRITICAL; risk >= 60 -> HealthSeverity.HIGH; risk >= 40 -> HealthSeverity.MODERATE; risk >= 20 -> HealthSeverity.LOW; risk > 0 -> HealthSeverity.MINIMAL; else -> HealthSeverity.NONE }
    }

    fun calculateLongevityAssessment(chart: VedicChart, triMurti: TriMurtiAnalysis, ascSign: ZodiacSign): LongevityAssessment {
        val appliedRules = mutableListOf<ShoolaRuleApplication>()
        val supportingFactorsEn = mutableListOf<String>()
        val supportingFactorsNe = mutableListOf<String>()
        val challengingFactorsEn = mutableListOf<String>()
        val challengingFactorsNe = mutableListOf<String>()

        // Apply classical Parashari rule chains

        // Rule 1: Lagna Lord Analysis (BPHS Chapter on Ayurdaya)
        val lagnaLordResult = analyzeLagnaLordClassical(chart, ascSign, appliedRules)
        supportingFactorsEn.addAll(lagnaLordResult.supportingEn)
        supportingFactorsNe.addAll(lagnaLordResult.supportingNe)
        challengingFactorsEn.addAll(lagnaLordResult.challengingEn)
        challengingFactorsNe.addAll(lagnaLordResult.challengingNe)

        // Rule 2: 8th House Analysis (House of Longevity)
        val eighthHouseResult = analyzeEighthHouseClassical(chart, ascSign, appliedRules)
        supportingFactorsEn.addAll(eighthHouseResult.supportingEn)
        supportingFactorsNe.addAll(eighthHouseResult.supportingNe)
        challengingFactorsEn.addAll(eighthHouseResult.challengingEn)
        challengingFactorsNe.addAll(eighthHouseResult.challengingNe)

        // Rule 3: Saturn Analysis (Ayushkaraka)
        val saturnResult = analyzeSaturnClassical(chart, appliedRules)
        supportingFactorsEn.addAll(saturnResult.supportingEn)
        supportingFactorsNe.addAll(saturnResult.supportingNe)
        challengingFactorsEn.addAll(saturnResult.challengingEn)
        challengingFactorsNe.addAll(saturnResult.challengingNe)

        // Rule 4: TriMurti Analysis (Brahma-Rudra-Maheshwara)
        val triMurtiResult = analyzeTriMurtiClassical(triMurti, appliedRules)
        supportingFactorsEn.addAll(triMurtiResult.supportingEn)
        supportingFactorsNe.addAll(triMurtiResult.supportingNe)
        challengingFactorsEn.addAll(triMurtiResult.challengingEn)
        challengingFactorsNe.addAll(triMurtiResult.challengingNe)

        // Rule 5: Benefic-Malefic Balance on Longevity Houses
        val beneficResult = analyzeBeneficMaleficBalanceClassical(chart, appliedRules)
        supportingFactorsEn.addAll(beneficResult.supportingEn)
        supportingFactorsNe.addAll(beneficResult.supportingNe)
        challengingFactorsEn.addAll(beneficResult.challengingEn)
        challengingFactorsNe.addAll(beneficResult.challengingNe)

        // Determine final category based on rule applications
        val category = determineLongevityCategoryFromRules(appliedRules)

        return LongevityAssessment(
            category = category,
            estimatedRange = category.yearsRange,
            estimatedRangeNe = category.yearsRange,
            supportingFactors = supportingFactorsEn,
            supportingFactorsNe = supportingFactorsNe,
            challengingFactors = challengingFactorsEn,
            challengingFactorsNe = challengingFactorsNe,
            interpretation = "Longevity: ${category.displayName} (${category.yearsRange} years)",
            interpretationNe = "आयु: ${category.displayNameNe} (${category.yearsRange} वर्ष)",
            appliedRules = appliedRules
        )
    }

    private data class FactorResult(
        val supportingEn: List<String>,
        val supportingNe: List<String>,
        val challengingEn: List<String>,
        val challengingNe: List<String>
    )

    /**
     * Rule 1: Lagna Lord Analysis (BPHS - Brihat Parashara Hora Shastra)
     * The Lagna lord's strength and placement is primary indicator of longevity
     */
    private fun analyzeLagnaLordClassical(
        chart: VedicChart,
        ascSign: ZodiacSign,
        rules: MutableList<ShoolaRuleApplication>
    ): FactorResult {
        val supportingEn = mutableListOf<String>()
        val supportingNe = mutableListOf<String>()
        val challengingEn = mutableListOf<String>()
        val challengingNe = mutableListOf<String>()

        val lagnaLord = ascSign.ruler
        val lagnaLordPos = chart.planetPositions.find { it.planet == lagnaLord }

        if (lagnaLordPos != null) {
            val lagnaLordSign = ZodiacSign.fromLongitude(lagnaLordPos.longitude)
            val house = lagnaLordPos.house

            when {
                isExalted(lagnaLord, lagnaLordSign) -> {
                    supportingEn.add("Exalted Lagna lord (${lagnaLord.displayName}) - BPHS Rule")
                    supportingNe.add("उच्च लग्नेश (${lagnaLord.displayName}) - बृहत्पाराशर होरा शास्त्र")
                    rules.add(ShoolaRuleApplication(
                        ruleName = "Lagna Lord Exaltation",
                        sutraReference = "BPHS Ayurdaya Chapter",
                        condition = "Lagna lord in exaltation sign",
                        observation = "${lagnaLord.displayName} exalted in ${lagnaLordSign.displayName}",
                        result = "Purnayu indicator (+strong)",
                        weight = 20.0
                    ))
                }
                isOwnSign(lagnaLord, lagnaLordSign) -> {
                    supportingEn.add("Lagna lord in own sign - BPHS Rule")
                    supportingNe.add("लग्नेश स्वराशिमा - बृहत्पाराशर होरा शास्त्र")
                    rules.add(ShoolaRuleApplication(
                        ruleName = "Lagna Lord Own Sign",
                        sutraReference = "BPHS Ayurdaya Chapter",
                        condition = "Lagna lord in own sign",
                        observation = "${lagnaLord.displayName} in own sign ${lagnaLordSign.displayName}",
                        result = "Purnayu indicator (+moderate)",
                        weight = 15.0
                    ))
                }
                isDebilitated(lagnaLord, lagnaLordSign) -> {
                    challengingEn.add("Debilitated Lagna lord - BPHS Rule")
                    challengingNe.add("नीच लग्नेश - बृहत्पाराशर होरा शास्त्र")
                    rules.add(ShoolaRuleApplication(
                        ruleName = "Lagna Lord Debilitation",
                        sutraReference = "BPHS Ayurdaya Chapter",
                        condition = "Lagna lord in debilitation sign",
                        observation = "${lagnaLord.displayName} debilitated in ${lagnaLordSign.displayName}",
                        result = "Alpayu indicator (-strong)",
                        weight = -18.0
                    ))
                }
            }

            when {
                house in listOf(1, 4, 7, 10) -> {
                    supportingEn.add("Lagna lord in Kendra (house $house)")
                    supportingNe.add("लग्नेश केन्द्रमा (भाव $house)")
                    rules.add(ShoolaRuleApplication(
                        ruleName = "Lagna Lord in Kendra",
                        sutraReference = "BPHS Chapter 45",
                        condition = "Lagna lord in 1st, 4th, 7th, or 10th house",
                        observation = "Lagna lord in house $house",
                        result = "Purnayu indicator",
                        weight = 12.0
                    ))
                }
                house in listOf(5, 9) -> {
                    supportingEn.add("Lagna lord in Trikona (house $house)")
                    supportingNe.add("लग्नेश त्रिकोणमा (भाव $house)")
                    rules.add(ShoolaRuleApplication(
                        ruleName = "Lagna Lord in Trikona",
                        sutraReference = "BPHS Chapter 45",
                        condition = "Lagna lord in 5th or 9th house",
                        observation = "Lagna lord in house $house",
                        result = "Purnayu indicator",
                        weight = 10.0
                    ))
                }
                house in listOf(6, 8, 12) -> {
                    challengingEn.add("Lagna lord in dusthana (house $house) - reduced vitality")
                    challengingNe.add("लग्नेश दुःस्थानमा (भाव $house) - कमजोर जीवनशक्ति")
                    rules.add(ShoolaRuleApplication(
                        ruleName = "Lagna Lord in Dusthana",
                        sutraReference = "BPHS Chapter 45",
                        condition = "Lagna lord in 6th, 8th, or 12th house",
                        observation = "Lagna lord in house $house (dusthana)",
                        result = "Alpayu indicator",
                        weight = -15.0
                    ))
                }
            }
        }

        return FactorResult(supportingEn, supportingNe, challengingEn, challengingNe)
    }

    /**
     * Rule 2: 8th House Analysis (BPHS)
     * 8th house is the house of longevity (Ayur Bhava)
     */
    private fun analyzeEighthHouseClassical(
        chart: VedicChart,
        ascSign: ZodiacSign,
        rules: MutableList<ShoolaRuleApplication>
    ): FactorResult {
        val supportingEn = mutableListOf<String>()
        val supportingNe = mutableListOf<String>()
        val challengingEn = mutableListOf<String>()
        val challengingNe = mutableListOf<String>()

        val eighthSign = ZodiacSign.entries[(ascSign.number - 1 + 7) % 12]
        val eighthLord = eighthSign.ruler
        val eighthLordPos = chart.planetPositions.find { it.planet == eighthLord }

        if (eighthLordPos != null) {
            val house = eighthLordPos.house
            val eighthLordSign = ZodiacSign.fromLongitude(eighthLordPos.longitude)

            when {
                isExalted(eighthLord, eighthLordSign) -> {
                    supportingEn.add("8th lord exalted - strong longevity")
                    supportingNe.add("अष्टमेश उच्च - बलियो आयु")
                }
                isDebilitated(eighthLord, eighthLordSign) -> {
                    challengingEn.add("8th lord debilitated - weak longevity")
                    challengingNe.add("अष्टमेश नीच - कमजोर आयु")
                }
            }

            when {
                house in listOf(1, 4, 7, 10) -> {
                    supportingEn.add("8th lord in Kendra (house $house) - excellent for longevity")
                    supportingNe.add("अष्टमेश केन्द्रमा (भाव $house) - उत्तम आयु")
                    rules.add(ShoolaRuleApplication(
                        ruleName = "8th Lord in Kendra",
                        sutraReference = "BPHS Chapter 46",
                        condition = "8th lord in 1st, 4th, 7th, or 10th house",
                        observation = "8th lord ${eighthLord.displayName} in house $house",
                        result = "Purnayu - 8th lord strong in angle",
                        weight = 15.0
                    ))
                }
                house in listOf(5, 9) -> {
                    supportingEn.add("8th lord in Trikona (house $house)")
                    supportingNe.add("अष्टमेश त्रिकोणमा (भाव $house)")
                    rules.add(ShoolaRuleApplication(
                        ruleName = "8th Lord in Trikona",
                        sutraReference = "BPHS Chapter 46",
                        condition = "8th lord in 5th or 9th house",
                        observation = "8th lord in house $house",
                        result = "Madhyayu to Purnayu",
                        weight = 10.0
                    ))
                }
                house in listOf(6, 8, 12) -> {
                    challengingEn.add("8th lord in dusthana (house $house) - vulnerable period")
                    challengingNe.add("अष्टमेश दुःस्थानमा (भाव $house) - जोखिमपूर्ण")
                    rules.add(ShoolaRuleApplication(
                        ruleName = "8th Lord in Dusthana",
                        sutraReference = "BPHS Chapter 46",
                        condition = "8th lord in 6th, 8th, or 12th house",
                        observation = "8th lord in house $house (dusthana)",
                        result = "Alpayu - 8th lord afflicted",
                        weight = -15.0
                    ))
                }
            }
        }

        return FactorResult(supportingEn, supportingNe, challengingEn, challengingNe)
    }

    /**
     * Rule 3: Saturn Analysis (Ayushkaraka)
     * Saturn is the natural significator of longevity
     */
    private fun analyzeSaturnClassical(
        chart: VedicChart,
        rules: MutableList<ShoolaRuleApplication>
    ): FactorResult {
        val supportingEn = mutableListOf<String>()
        val supportingNe = mutableListOf<String>()
        val challengingEn = mutableListOf<String>()
        val challengingNe = mutableListOf<String>()

        chart.planetPositions.find { it.planet == Planet.SATURN }?.let { saturnPos ->
            val house = saturnPos.house
            val saturnSign = ZodiacSign.fromLongitude(saturnPos.longitude)

            when {
                isExalted(Planet.SATURN, saturnSign) -> {
                    supportingEn.add("Saturn exalted (Libra) - Ayushkaraka strong")
                    supportingNe.add("शनि उच्च (तुला) - आयुकारक बलियो")
                    rules.add(ShoolaRuleApplication(
                        ruleName = "Saturn Exalted (Ayushkaraka)",
                        sutraReference = "BPHS Chapter 47",
                        condition = "Saturn exalted in Libra",
                        observation = "Saturn exalted in ${saturnSign.displayName}",
                        result = "Purnayu - Ayushkaraka exalted",
                        weight = 18.0
                    ))
                }
                isOwnSign(Planet.SATURN, saturnSign) -> {
                    supportingEn.add("Saturn in own sign (Capricorn/Aquarius)")
                    supportingNe.add("शनि स्वराशिमा (मकर/कुम्भ)")
                    rules.add(ShoolaRuleApplication(
                        ruleName = "Saturn in Own Sign",
                        sutraReference = "BPHS Chapter 47",
                        condition = "Saturn in Capricorn or Aquarius",
                        observation = "Saturn in ${saturnSign.displayName}",
                        result = "Purnayu - Ayushkaraka strong",
                        weight = 15.0
                    ))
                }
                isDebilitated(Planet.SATURN, saturnSign) -> {
                    challengingEn.add("Saturn debilitated (Aries) - weak Ayushkaraka")
                    challengingNe.add("शनि नीच (मेष) - कमजोर आयुकारक")
                    rules.add(ShoolaRuleApplication(
                        ruleName = "Saturn Debilitated",
                        sutraReference = "BPHS Chapter 47",
                        condition = "Saturn debilitated in Aries",
                        observation = "Saturn debilitated",
                        result = "Alpayu - Ayushkaraka weak",
                        weight = -18.0
                    ))
                }
            }

            when {
                house in listOf(1, 4, 7, 10) -> {
                    supportingEn.add("Saturn in Kendra (house $house)")
                    supportingNe.add("शनि केन्द्रमा (भाव $house)")
                }
                house in listOf(5, 9) -> {
                    supportingEn.add("Saturn in Trikona (house $house)")
                    supportingNe.add("शनि त्रिकोणमा (भाव $house)")
                }
                house in listOf(6, 8, 12) -> {
                    challengingEn.add("Saturn in dusthana (house $house)")
                    challengingNe.add("शनि दुःस्थानमा (भाव $house)")
                }
                else -> {}
            }
        }

        return FactorResult(supportingEn, supportingNe, challengingEn, challengingNe)
    }

    /**
     * Rule 4: TriMurti Analysis (Jaimini Sutras)
     */
    private fun analyzeTriMurtiClassical(
        triMurti: TriMurtiAnalysis,
        rules: MutableList<ShoolaRuleApplication>
    ): FactorResult {
        val supportingEn = mutableListOf<String>()
        val supportingNe = mutableListOf<String>()
        val challengingEn = mutableListOf<String>()
        val challengingNe = mutableListOf<String>()

        if (triMurti.brahma != null && triMurti.brahmaStrength > 0.6) {
            supportingEn.add("Strong Brahma (${triMurti.brahma.displayName}) - protective")
            supportingNe.add("बलियो ब्रह्मा (${triMurti.brahma.displayName}) - सुरक्षात्मक")
            rules.add(ShoolaRuleApplication(
                ruleName = "Strong Brahma (Jaimini)",
                sutraReference = "Jaimini Sutras 2.4",
                condition = "Brahma strength > 60%",
                observation = "Brahma is ${triMurti.brahma.displayName} with ${(triMurti.brahmaStrength * 100).toInt()}% strength",
                result = "Supports Purnayu",
                weight = 12.0
            ))
        }

        when {
            triMurti.rudraStrength > 0.7 -> {
                challengingEn.add("Very powerful Rudra (${triMurti.rudra.displayName}) - danger period")
                challengingNe.add("अत्यन्त शक्तिशाली रुद्र (${triMurti.rudra.displayName}) - खतराको अवधि")
                rules.add(ShoolaRuleApplication(
                    ruleName = "Powerful Rudra (Jaimini)",
                    sutraReference = "Jaimini Sutras 2.4",
                    condition = "Rudra strength > 70%",
                    observation = "Rudra is ${triMurti.rudra.displayName} with ${(triMurti.rudraStrength * 100).toInt()}% strength",
                    result = "Alpayu indicator - strong maraka",
                    weight = -20.0
                ))
            }
            triMurti.rudraStrength > 0.5 -> {
                challengingEn.add("Strong Rudra (${triMurti.rudra.displayName}) - caution needed")
                challengingNe.add("बलियो रुद्र (${triMurti.rudra.displayName}) - सावधानी आवश्यक")
                rules.add(ShoolaRuleApplication(
                    ruleName = "Moderate Rudra",
                    sutraReference = "Jaimini Sutras 2.4",
                    condition = "Rudra strength 50-70%",
                    observation = "Rudra strength ${(triMurti.rudraStrength * 100).toInt()}%",
                    result = "Madhyayu to Alpayu indicator",
                    weight = -12.0
                ))
            }
        }

        return FactorResult(supportingEn, supportingNe, challengingEn, challengingNe)
    }

    /**
     * Rule 5: Benefic-Malefic Balance on Longevity Houses
     */
    private fun analyzeBeneficMaleficBalanceClassical(
        chart: VedicChart,
        rules: MutableList<ShoolaRuleApplication>
    ): FactorResult {
        val supportingEn = mutableListOf<String>()
        val supportingNe = mutableListOf<String>()
        val challengingEn = mutableListOf<String>()
        val challengingNe = mutableListOf<String>()

        val benefics = listOf(Planet.JUPITER, Planet.VENUS, Planet.MERCURY, Planet.MOON)
        val malefics = listOf(Planet.SATURN, Planet.MARS, Planet.RAHU, Planet.KETU, Planet.SUN)
        val longevityHouses = listOf(1, 8, 10)

        var beneficCount = 0
        var maleficCount = 0
        val observations = mutableListOf<String>()

        for (house in longevityHouses) {
            val planetsInHouse = chart.planetPositions.filter { it.house == house }
            val houseBenefics = planetsInHouse.count { it.planet in benefics }
            val houseMalefics = planetsInHouse.count { it.planet in malefics }

            beneficCount += houseBenefics
            maleficCount += houseMalefics

            if (houseBenefics > 0 || houseMalefics > 0) {
                observations.add("H$house: $houseBenefics benefic(s), $houseMalefics malefic(s)")
            }
        }

        val total = beneficCount + maleficCount
        val beneficRatio = if (total > 0) beneficCount.toDouble() / total else 0.5

        when {
            beneficRatio >= 0.7 -> {
                supportingEn.add("Strong benefic influence (${(beneficRatio * 100).toInt()}%) on longevity houses")
                supportingNe.add("बलियो शुभ प्रभाव (${(beneficRatio * 100).toInt()}%) आयु भावमा")
                rules.add(ShoolaRuleApplication(
                    ruleName = "Benefic Dominance on Longevity Houses",
                    sutraReference = "BPHS General Principles",
                    condition = "Benefic ratio > 70% in houses 1, 8, 10",
                    observation = observations.joinToString("; "),
                    result = "Purnayu indicator",
                    weight = 10.0
                ))
            }
            beneficRatio <= 0.3 -> {
                challengingEn.add("Strong malefic influence (${((1 - beneficRatio) * 100).toInt()}%) on longevity houses")
                challengingNe.add("बलियो अशुभ प्रभाव (${((1 - beneficRatio) * 100).toInt()}%) आयु भावमा")
                rules.add(ShoolaRuleApplication(
                    ruleName = "Malefic Dominance on Longevity Houses",
                    sutraReference = "BPHS General Principles",
                    condition = "Malefic ratio > 70% in houses 1, 8, 10",
                    observation = observations.joinToString("; "),
                    result = "Alpayu indicator",
                    weight = -10.0
                ))
            }
        }

        return FactorResult(supportingEn, supportingNe, challengingEn, challengingNe)
    }

    /**
     * Determine final longevity category based on cumulative rule weights
     */
    private fun determineLongevityCategoryFromRules(rules: List<ShoolaRuleApplication>): LongevityCategory {
        val totalWeight = rules.sumOf { it.weight }

        return when {
            totalWeight >= 50 -> LongevityCategory.POORNAYU
            totalWeight >= 20 -> LongevityCategory.MADHYAYU
            totalWeight >= -10 -> LongevityCategory.ALPAYU
            else -> LongevityCategory.BALARISHTA
        }
    }
}
