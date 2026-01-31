package com.astro.storm.ephemeris.yoga

import com.astro.storm.core.model.Planet
import com.astro.storm.core.model.PlanetPosition
import com.astro.storm.core.model.VedicChart
import com.astro.storm.core.model.ZodiacSign

/**
 * Arishta Yoga Evaluator - Comprehensive Negative and Challenging Combinations
 *
 * This evaluator handles the full spectrum of Arishta (challenging) yogas from classical texts.
 * These yogas indicate obstacles, difficulties, and areas requiring attention and remedies.
 *
 * Categories covered:
 * 1. Daridra Yogas - Poverty and financial difficulties
 * 2. Balarishta Yogas - Early life dangers
 * 3. Madhyayu/Alpayu Yogas - Longevity concerns
 * 4. Rogaishta Yogas - Health-related challenges
 * 5. Bandhana Yogas - Imprisonment/Confinement
 * 6. Duryogas - General misfortune patterns
 * 7. Planetary Affliction Yogas
 * 8. House-based Arishta Yogas
 *
 * Based on classical texts:
 * - Brihat Parasara Hora Shastra (BPHS), Chapters 44-47
 * - Phaladeepika, Chapters 13-14
 * - Jataka Parijata, Chapters 8-9
 * - Saravali, Chapters 44-46
 *
 * Note: These yogas are presented for educational purposes. Many have cancellation
 * conditions and should be evaluated in the context of the full chart.
 *
 * @author AstroStorm
 */
class ArishtaYogaEvaluator : YogaEvaluator {

    override val category: YogaCategory = YogaCategory.NEGATIVE_YOGA

    override fun evaluate(chart: VedicChart): List<Yoga> {
        val yogas = mutableListOf<Yoga>()
        val ascendantSign = ZodiacSign.fromLongitude(chart.ascendant)
        val houseLords = YogaHelpers.getHouseLords(ascendantSign)

        // 1. Daridra Yogas - Poverty combinations
        yogas.addAll(evaluateDaridraYogas(chart, houseLords))

        // 2. Balarishta Yogas - Early life dangers
        yogas.addAll(evaluateBalaarishtaYogas(chart, houseLords))

        // 3. Health-related Yogas
        yogas.addAll(evaluateRogaYogas(chart, houseLords))

        // 4. Bandhana Yogas - Confinement
        yogas.addAll(evaluateBandhanaYogas(chart, houseLords))

        // 5. General Duryogas
        yogas.addAll(evaluateDuryogas(chart, houseLords))

        // 6. Planetary Combustion Yogas
        yogas.addAll(evaluateCombustionYogas(chart))

        // 7. House-based Arishtas
        yogas.addAll(evaluateHouseArishtaYogas(chart, houseLords))

        // 8. Relationship Arishtas
        yogas.addAll(evaluateRelationshipArishtas(chart, houseLords))

        return yogas
    }

    // ==================== DARIDRA (POVERTY) YOGAS ====================

    private fun evaluateDaridraYogas(chart: VedicChart, houseLords: Map<Int, Planet>): List<Yoga> {
        val yogas = mutableListOf<Yoga>()

        // 1. Daridra Yoga Type 1 - 11th lord in 6th, 8th, or 12th
        val lord11 = houseLords[11]
        val lord11Pos = if (lord11 != null) chart.planetPositions.find { it.planet == lord11 } else null

        if (lord11Pos != null && lord11Pos.house in listOf(6, 8, 12)) {
            val cancellations = mutableListOf<String>()

            // Check for mitigating factors
            if (YogaHelpers.isExalted(lord11Pos) || YogaHelpers.isInOwnSign(lord11Pos)) {
                cancellations.add("${lord11!!.displayName} in own/exalted sign reduces severity")
            }

            val jupiterPos = chart.planetPositions.find { it.planet == Planet.JUPITER }
            if (jupiterPos != null && YogaHelpers.isAspecting(jupiterPos, lord11Pos)) {
                cancellations.add("Jupiter aspect provides protection")
            }

            val isMitigated = cancellations.isNotEmpty()
            val strength = if (isMitigated) 40.0 else 60.0

            yogas.add(Yoga(
                name = "Daridra Yoga (11th Lord)",
                sanskritName = "दारिद्र्य योग",
                category = YogaCategory.NEGATIVE_YOGA,
                planets = listOf(lord11!!),
                houses = listOf(lord11Pos.house),
                description = "11th lord ${lord11.displayName} in dusthana house ${lord11Pos.house}",
                effects = if (isMitigated)
                    "Financial challenges that can be overcome, irregular income initially but improves with effort, gains require persistence"
                else
                    "Difficulties in accumulating wealth, obstacles in financial growth, income from questionable sources possible, friends may not be supportive",
                strength = YogaHelpers.strengthFromPercentage(strength),
                strengthPercentage = strength,
                isAuspicious = false,
                activationPeriod = "${lord11.displayName} Dasha periods",
                cancellationFactors = cancellations.ifEmpty { listOf("No significant mitigating factors") }
            ))
        }

        // 2. Daridra Yoga Type 2 - 5th lord in 6th or 12th
        val lord5 = houseLords[5]
        val lord5Pos = if (lord5 != null) chart.planetPositions.find { it.planet == lord5 } else null

        if (lord5Pos != null && lord5Pos.house in listOf(6, 12)) {
            val strength = 50.0
            yogas.add(Yoga(
                name = "Daridra Yoga (5th Lord)",
                sanskritName = "दारिद्र्य योग",
                category = YogaCategory.NEGATIVE_YOGA,
                planets = listOf(lord5!!),
                houses = listOf(lord5Pos.house),
                description = "5th lord ${lord5.displayName} in house ${lord5Pos.house}",
                effects = "Losses through speculation, children may cause financial drain, poor judgment in investments, creativity not financially rewarding",
                strength = YogaHelpers.strengthFromPercentage(strength),
                strengthPercentage = strength,
                isAuspicious = false,
                activationPeriod = "${lord5.displayName} Dasha periods",
                cancellationFactors = listOf("Jupiter influence on 5th house or lord helps", "Avoid speculation during 5th lord periods")
            ))
        }

        // 3. Daridra Yoga Type 3 - Malefics in 2nd without benefic aspect
        val maleficsIn2nd = chart.planetPositions.filter {
            it.house == 2 && it.planet in listOf(Planet.SATURN, Planet.MARS, Planet.RAHU, Planet.KETU, Planet.SUN)
        }
        val beneficAspectOn2nd = chart.planetPositions.any { pos ->
            pos.planet in listOf(Planet.JUPITER, Planet.VENUS, Planet.MERCURY) &&
            YogaHelpers.isAspecting(pos, chart.planetPositions.firstOrNull { it.house == 2 } ?: return@any false)
        }

        if (maleficsIn2nd.isNotEmpty() && !beneficAspectOn2nd) {
            val strength = 45.0 + (maleficsIn2nd.size - 1) * 10.0
            yogas.add(Yoga(
                name = "Dhana Nashaka Yoga",
                sanskritName = "धन नाशक योग",
                category = YogaCategory.NEGATIVE_YOGA,
                planets = maleficsIn2nd.map { it.planet },
                houses = listOf(2),
                description = "Malefics in 2nd house without benefic aspect",
                effects = "Difficulty saving money, harsh speech affecting income, family wealth dissipation, need for careful financial planning",
                strength = YogaHelpers.strengthFromPercentage(strength.coerceIn(10.0, 100.0)),
                strengthPercentage = strength.coerceIn(10.0, 100.0),
                isAuspicious = false,
                activationPeriod = "Periods of malefics in 2nd house",
                cancellationFactors = listOf("Develop saving habits", "Practice mindful speech", "Mantra for 2nd lord helps")
            ))
        }

        // 4. Kshaya Yoga - Wasting of wealth
        val lord2 = houseLords[2]
        val lord12 = houseLords[12]
        val lord2Pos = if (lord2 != null) chart.planetPositions.find { it.planet == lord2 } else null
        val lord12Pos = if (lord12 != null) chart.planetPositions.find { it.planet == lord12 } else null

        if (lord2Pos != null && lord12Pos != null && YogaHelpers.areConjunct(lord2Pos, lord12Pos)) {
            val strength = 55.0
            yogas.add(Yoga(
                name = "Kshaya Yoga",
                sanskritName = "क्षय योग",
                category = YogaCategory.NEGATIVE_YOGA,
                planets = listOf(lord2!!, lord12!!),
                houses = listOf(lord2Pos.house),
                description = "2nd lord conjunct 12th lord",
                effects = "Wealth tends to dissipate, expenses exceed income, difficulty accumulating savings, may spend on hospitals or foreign travel",
                strength = YogaHelpers.strengthFromPercentage(strength),
                strengthPercentage = strength,
                isAuspicious = false,
                activationPeriod = "Combined periods of 2nd and 12th lords",
                cancellationFactors = listOf("Strong Jupiter can help", "Budgeting and financial discipline essential")
            ))
        }

        return yogas
    }

    // ==================== BALARISHTA (EARLY LIFE) YOGAS ====================

    private fun evaluateBalaarishtaYogas(chart: VedicChart, houseLords: Map<Int, Planet>): List<Yoga> {
        val yogas = mutableListOf<Yoga>()
        val moonPos = chart.planetPositions.find { it.planet == Planet.MOON } ?: return yogas

        // 1. Balarishta - Moon afflicted in early degrees
        val moonDegree = moonPos.longitude % 30
        val moonStrength = YogaHelpers.getMoonPhaseStrength(moonPos, chart)

        // Check for severe affliction to weak Moon
        if (moonStrength < 0.25) { // Very weak Moon (new moon phase)
            val maleficAspects = listOf(Planet.SATURN, Planet.MARS, Planet.RAHU, Planet.KETU).count { malefic ->
                val maleficPos = chart.planetPositions.find { it.planet == malefic }
                maleficPos != null && YogaHelpers.isAspecting(maleficPos, moonPos)
            }

            if (maleficAspects >= 2 && moonPos.house in listOf(6, 8, 12)) {
                // Check for cancellation
                val jupiterPos = chart.planetPositions.find { it.planet == Planet.JUPITER }
                val venusPos = chart.planetPositions.find { it.planet == Planet.VENUS }

                val jupiterProtection = jupiterPos != null &&
                    (YogaHelpers.isAspecting(jupiterPos, moonPos) || jupiterPos.house in listOf(1, 4, 7, 10))
                val venusProtection = venusPos != null && YogaHelpers.isAspecting(venusPos, moonPos)

                val cancellations = mutableListOf<String>()
                if (jupiterProtection) cancellations.add("Jupiter provides strong protection")
                if (venusProtection) cancellations.add("Venus aspect mitigates effects")

                val isCancelled = cancellations.size >= 1
                if (!isCancelled) {
                    val strength = 55.0
                    yogas.add(Yoga(
                        name = "Balarishta Yoga",
                        sanskritName = "बालारिष्ट योग",
                        category = YogaCategory.NEGATIVE_YOGA,
                        planets = listOf(Planet.MOON),
                        houses = listOf(moonPos.house),
                        description = "Weak Moon (${String.format("%.0f", moonStrength * 100)}% illuminated) afflicted by malefics in house ${moonPos.house}",
                        effects = "Childhood health concerns, need for extra care in early years, mother's health may need attention, emotional sensitivity requires nurturing",
                        strength = YogaHelpers.strengthFromPercentage(strength),
                        strengthPercentage = strength,
                        isAuspicious = false,
                        activationPeriod = "First 8 years of life and Moon periods",
                        cancellationFactors = listOf(
                            "Modern healthcare greatly reduces classical risks",
                            "Jupiter in Kendra from Moon or Lagna cancels",
                            "Benefic aspect on Moon cancels",
                            "Moon Shanti puja recommended"
                        )
                    ))
                }
            }
        }

        // 2. Lagna lord in 8th afflicted
        val lord1 = houseLords[1]
        val lord1Pos = if (lord1 != null) chart.planetPositions.find { it.planet == lord1 } else null

        if (lord1Pos != null && lord1Pos.house == 8 && YogaHelpers.isDebilitated(lord1Pos)) {
            val strength = 50.0
            yogas.add(Yoga(
                name = "Lagnesh Ashtama Arishta",
                sanskritName = "लग्नेश अष्टम अरिष्ट",
                category = YogaCategory.NEGATIVE_YOGA,
                planets = listOf(lord1!!),
                houses = listOf(8),
                description = "Debilitated Lagna lord in 8th house",
                effects = "Need for attention to health and vitality, transformative life experiences, may face chronic health issues if neglected",
                strength = YogaHelpers.strengthFromPercentage(strength),
                strengthPercentage = strength,
                isAuspicious = false,
                activationPeriod = "Lagna lord Dasha periods",
                cancellationFactors = listOf("Neecha Bhanga if present cancels", "Jupiter aspect helps significantly", "Regular health checkups recommended")
            ))
        }

        return yogas
    }

    // ==================== ROGA (HEALTH) YOGAS ====================

    private fun evaluateRogaYogas(chart: VedicChart, houseLords: Map<Int, Planet>): List<Yoga> {
        val yogas = mutableListOf<Yoga>()

        // 1. Rogaishta Yoga - 6th lord in Lagna
        val lord6 = houseLords[6]
        val lord6Pos = if (lord6 != null) chart.planetPositions.find { it.planet == lord6 } else null

        if (lord6Pos != null && lord6Pos.house == 1) {
            val strength = 55.0
            yogas.add(Yoga(
                name = "Rogaishta Yoga",
                sanskritName = "रोगैष्ट योग",
                category = YogaCategory.NEGATIVE_YOGA,
                planets = listOf(lord6!!),
                houses = listOf(1),
                description = "6th lord ${lord6.displayName} in Lagna",
                effects = "Health consciousness needed, may attract minor ailments, enemies may cause problems, benefits from healthcare profession",
                strength = YogaHelpers.strengthFromPercentage(strength),
                strengthPercentage = strength,
                isAuspicious = false,
                activationPeriod = "6th lord Dasha periods",
                cancellationFactors = listOf("Good for medical/healing professions", "Regular health routine essential", "If benefic, may overcome enemies")
            ))
        }

        // 2. Saturn + Mars in 6th - Chronic health issues
        val saturnPos = chart.planetPositions.find { it.planet == Planet.SATURN }
        val marsPos = chart.planetPositions.find { it.planet == Planet.MARS }

        if (saturnPos != null && marsPos != null && saturnPos.house == 6 && marsPos.house == 6) {
            val strength = 60.0
            yogas.add(Yoga(
                name = "Shatru Roga Yoga",
                sanskritName = "शत्रु रोग योग",
                category = YogaCategory.NEGATIVE_YOGA,
                planets = listOf(Planet.SATURN, Planet.MARS),
                houses = listOf(6),
                description = "Saturn and Mars both in 6th house",
                effects = "Potential for accidents, surgeries, or chronic conditions, need to manage anger and stress, victory over enemies after struggle",
                strength = YogaHelpers.strengthFromPercentage(strength),
                strengthPercentage = strength,
                isAuspicious = false,
                activationPeriod = "Saturn-Mars and Mars-Saturn periods",
                cancellationFactors = listOf("Good for surgeons, military, police", "Exercise and physical discipline help", "Avoid reckless behavior")
            ))
        }

        // 3. Ketu in 6th afflicted - Mysterious ailments
        val ketuPos = chart.planetPositions.find { it.planet == Planet.KETU }
        if (ketuPos != null && ketuPos.house == 6) {
            // Check if afflicted by other malefics
            val marsAspect = marsPos != null && YogaHelpers.isAspecting(marsPos, ketuPos)
            val saturnAspect = saturnPos != null && YogaHelpers.isAspecting(saturnPos, ketuPos)

            if (marsAspect || saturnAspect) {
                val strength = 50.0
                yogas.add(Yoga(
                    name = "Guhya Roga Yoga",
                    sanskritName = "गुह्य रोग योग",
                    category = YogaCategory.NEGATIVE_YOGA,
                    planets = listOf(Planet.KETU),
                    houses = listOf(6),
                    description = "Afflicted Ketu in 6th house",
                    effects = "Difficult-to-diagnose ailments, psychosomatic issues, need for holistic healing approach, may benefit from alternative medicine",
                    strength = YogaHelpers.strengthFromPercentage(strength),
                    strengthPercentage = strength,
                    isAuspicious = false,
                    activationPeriod = "Ketu Dasha periods",
                    cancellationFactors = listOf("Alternative and Ayurvedic medicine may help", "Spiritual practices reduce severity", "Jupiter aspect protects")
                ))
            }
        }

        // 4. Sun afflicted - Eye/Heart concerns
        val sunPos = chart.planetPositions.find { it.planet == Planet.SUN }
        if (sunPos != null && sunPos.house == 6) {
            val rahuPos = chart.planetPositions.find { it.planet == Planet.RAHU }
            if (rahuPos != null && YogaHelpers.areConjunct(sunPos, rahuPos)) {
                val strength = 55.0
                yogas.add(Yoga(
                    name = "Netra/Hridaya Roga Yoga",
                    sanskritName = "नेत्र/हृदय रोग योग",
                    category = YogaCategory.NEGATIVE_YOGA,
                    planets = listOf(Planet.SUN, Planet.RAHU),
                    houses = listOf(6),
                    description = "Sun-Rahu conjunction in 6th house",
                    effects = "Eye care important, heart health requires attention, father's health may be concern, government/authority problems possible",
                    strength = YogaHelpers.strengthFromPercentage(strength),
                    strengthPercentage = strength,
                    isAuspicious = false,
                    activationPeriod = "Sun-Rahu periods",
                    cancellationFactors = listOf("Regular eye checkups recommended", "Heart-healthy lifestyle important", "Surya Namaskar helps")
                ))
            }
        }

        return yogas
    }

    // ==================== BANDHANA (CONFINEMENT) YOGAS ====================

    private fun evaluateBandhanaYogas(chart: VedicChart, houseLords: Map<Int, Planet>): List<Yoga> {
        val yogas = mutableListOf<Yoga>()

        // 1. Classic Bandhana Yoga - Rahu in Lagna, Mars in 7th, Saturn in 4th
        val rahuPos = chart.planetPositions.find { it.planet == Planet.RAHU }
        val marsPos = chart.planetPositions.find { it.planet == Planet.MARS }
        val saturnPos = chart.planetPositions.find { it.planet == Planet.SATURN }

        if (rahuPos?.house == 1 && marsPos?.house == 7 && saturnPos?.house == 4) {
            val strength = 55.0
            yogas.add(Yoga(
                name = "Bandhana Yoga (Classic)",
                sanskritName = "बंधन योग",
                category = YogaCategory.NEGATIVE_YOGA,
                planets = listOf(Planet.RAHU, Planet.MARS, Planet.SATURN),
                houses = listOf(1, 4, 7),
                description = "Rahu in Lagna, Mars in 7th, Saturn in 4th",
                effects = "Restriction of freedom possible, may indicate hospitalization, legal troubles, or feeling trapped in situations; requires ethical conduct",
                strength = YogaHelpers.strengthFromPercentage(strength),
                strengthPercentage = strength,
                isAuspicious = false,
                activationPeriod = "Rahu-Mars-Saturn combined periods",
                cancellationFactors = listOf("Jupiter aspect on Lagna cancels", "Ethical living prevents manifestation", "May manifest as feeling restricted rather than actual imprisonment")
            ))
        }

        // 2. 12th lord in Lagna with malefics
        val lord12 = houseLords[12]
        val lord12Pos = if (lord12 != null) chart.planetPositions.find { it.planet == lord12 } else null

        if (lord12Pos != null && lord12Pos.house == 1) {
            val maleficsWithLord12 = chart.planetPositions.filter {
                it.house == 1 && it.planet in listOf(Planet.SATURN, Planet.MARS, Planet.RAHU, Planet.KETU)
            }
            if (maleficsWithLord12.isNotEmpty()) {
                val strength = 50.0
                yogas.add(Yoga(
                    name = "Bandhana Yoga (12th Lord)",
                    sanskritName = "बंधन योग",
                    category = YogaCategory.NEGATIVE_YOGA,
                    planets = listOf(lord12!!) + maleficsWithLord12.map { it.planet },
                    houses = listOf(1),
                    description = "12th lord in Lagna with malefics",
                    effects = "Possible confinement, hospitalization, or foreign residence, expenses through self, may feel isolated or restricted",
                    strength = YogaHelpers.strengthFromPercentage(strength),
                    strengthPercentage = strength,
                    isAuspicious = false,
                    activationPeriod = "12th lord periods",
                    cancellationFactors = listOf("May manifest as spiritual retreat", "Hospital work/service is positive outlet", "Jupiter aspect protects")
                ))
            }
        }

        return yogas
    }

    // ==================== GENERAL DURYOGAS ====================

    private fun evaluateDuryogas(chart: VedicChart, houseLords: Map<Int, Planet>): List<Yoga> {
        val yogas = mutableListOf<Yoga>()

        // 1. Dur Yoga - Waning Moon in dusthana with malefics
        val moonPos = chart.planetPositions.find { it.planet == Planet.MOON }
        if (moonPos != null) {
            val moonStrength = YogaHelpers.getMoonPhaseStrength(moonPos, chart)
            if (moonStrength < 0.3 && moonPos.house in listOf(6, 8, 12)) {
                val maleficsAspecting = listOf(Planet.SATURN, Planet.MARS).count { malefic ->
                    val pos = chart.planetPositions.find { it.planet == malefic }
                    pos != null && YogaHelpers.isAspecting(pos, moonPos)
                }
                if (maleficsAspecting > 0) {
                    val strength = 55.0
                    yogas.add(Yoga(
                        name = "Dur Yoga (Moon)",
                        sanskritName = "दुर् योग",
                        category = YogaCategory.NEGATIVE_YOGA,
                        planets = listOf(Planet.MOON),
                        houses = listOf(moonPos.house),
                        description = "Weak Moon in dusthana (house ${moonPos.house}) aspected by malefics",
                        effects = "Mental stress, emotional challenges, mother's health concerns, need for psychological well-being attention",
                        strength = YogaHelpers.strengthFromPercentage(strength),
                        strengthPercentage = strength,
                        isAuspicious = false,
                        activationPeriod = "Moon Dasha periods",
                        cancellationFactors = listOf("Jupiter aspect greatly helps", "Chandra Shanti recommended", "Meditation and counseling beneficial")
                    ))
                }
            }
        }

        // 2. Pitr Rina Yoga - Father-related karma
        val sunPos = chart.planetPositions.find { it.planet == Planet.SUN }
        val saturnPos = chart.planetPositions.find { it.planet == Planet.SATURN }
        val lord9 = houseLords[9]
        val lord9Pos = if (lord9 != null) chart.planetPositions.find { it.planet == lord9 } else null

        if (sunPos != null && saturnPos != null && YogaHelpers.areConjunct(sunPos, saturnPos, 10.0)) {
            val strength = 50.0
            yogas.add(Yoga(
                name = "Pitr Dosha Yoga",
                sanskritName = "पितृ दोष योग",
                category = YogaCategory.NEGATIVE_YOGA,
                planets = listOf(Planet.SUN, Planet.SATURN),
                houses = listOf(sunPos.house),
                description = "Sun conjunct Saturn within 10°",
                effects = "Father-son tensions, may experience delays or obstacles from authority figures, ancestral karma to be resolved, ego-discipline conflicts",
                strength = YogaHelpers.strengthFromPercentage(strength),
                strengthPercentage = strength,
                isAuspicious = false,
                activationPeriod = "Sun-Saturn periods",
                cancellationFactors = listOf("Pitru Tarpan (ancestor rituals) recommended", "Respect for father/elders helps", "Career delays but eventual success")
            ))
        }

        // 3. Matr Rina Yoga - Mother-related karma
        if (moonPos != null && saturnPos != null && YogaHelpers.areConjunct(moonPos, saturnPos, 10.0)) {
            val strength = 50.0
            yogas.add(Yoga(
                name = "Matr Dosha Yoga",
                sanskritName = "मातृ दोष योग",
                category = YogaCategory.NEGATIVE_YOGA,
                planets = listOf(Planet.MOON, Planet.SATURN),
                houses = listOf(moonPos.house),
                description = "Moon conjunct Saturn within 10°",
                effects = "Emotional restriction, mother may be strict or have health issues, depression tendencies, delayed emotional maturity",
                strength = YogaHelpers.strengthFromPercentage(strength),
                strengthPercentage = strength,
                isAuspicious = false,
                activationPeriod = "Moon-Saturn periods",
                cancellationFactors = listOf("Care for mother and elderly", "Express emotions constructively", "Therapy/counseling beneficial")
            ))
        }

        // 4. Bhratri Karaka Dosha - Sibling difficulties
        val marsPos = chart.planetPositions.find { it.planet == Planet.MARS }
        val lord3 = houseLords[3]
        val lord3Pos = if (lord3 != null) chart.planetPositions.find { it.planet == lord3 } else null

        if (marsPos != null && marsPos.house in listOf(6, 8, 12) && YogaHelpers.isDebilitated(marsPos)) {
            val strength = 45.0
            yogas.add(Yoga(
                name = "Bhratri Dosha Yoga",
                sanskritName = "भ्रातृ दोष योग",
                category = YogaCategory.NEGATIVE_YOGA,
                planets = listOf(Planet.MARS),
                houses = listOf(marsPos.house),
                description = "Debilitated Mars in dusthana house ${marsPos.house}",
                effects = "Difficulties with siblings, lack of courage in difficult times, may lack proper initiative, brothers may face troubles",
                strength = YogaHelpers.strengthFromPercentage(strength),
                strengthPercentage = strength,
                isAuspicious = false,
                activationPeriod = "Mars Dasha periods",
                cancellationFactors = listOf("Hanuman worship helps", "Physical exercise builds courage", "Support siblings proactively")
            ))
        }

        return yogas
    }

    // ==================== COMBUSTION YOGAS ====================

    private fun evaluateCombustionYogas(chart: VedicChart): List<Yoga> {
        val yogas = mutableListOf<Yoga>()
        val sunPos = chart.planetPositions.find { it.planet == Planet.SUN } ?: return yogas

        // Check each planet for combustion
        val combustiblePlanets = listOf(
            Planet.MOON to 12.0,
            Planet.MARS to 17.0,
            Planet.MERCURY to 14.0,
            Planet.JUPITER to 11.0,
            Planet.VENUS to 10.0,
            Planet.SATURN to 15.0
        )

        combustiblePlanets.forEach { (planet, orb) ->
            val planetPos = chart.planetPositions.find { it.planet == planet } ?: return@forEach

            val distance = kotlin.math.abs(planetPos.longitude - sunPos.longitude)
            val normalizedDistance = if (distance > 180) 360 - distance else distance

            if (normalizedDistance <= orb) {
                val isDeepCombustion = normalizedDistance <= 3.0
                val severity = when {
                    isDeepCombustion -> "Deep combustion (Maudhya)"
                    normalizedDistance <= orb / 2 -> "Strong combustion"
                    else -> "Mild combustion"
                }

                // Get planet-specific effects
                val effects = when (planet) {
                    Planet.MOON -> "Emotional distress, mother's health, mental peace disturbed, lack of public support"
                    Planet.MARS -> "Reduced courage, sibling issues, property problems, rash actions"
                    Planet.MERCURY -> "Communication difficulties, education issues, nervous system, business problems"
                    Planet.JUPITER -> "Reduced wisdom, spiritual obstacles, problems with gurus/teachers, children concerns"
                    Planet.VENUS -> "Relationship troubles, reduced luxuries, vehicle problems, artistic blocks"
                    Planet.SATURN -> "Career obstacles, chronic health issues, servant problems, delays"
                    else -> "Planet's significations weakened"
                }

                val baseStrength = if (isDeepCombustion) 65.0 else 50.0

                // Check for mitigating factors
                val cancellations = mutableListOf<String>()
                if (planetPos.isRetrograde) {
                    cancellations.add("Retrograde motion reduces combustion effect")
                }
                if (YogaHelpers.isExalted(planetPos)) {
                    cancellations.add("Exaltation provides some protection")
                }
                val jupiterPos = chart.planetPositions.find { it.planet == Planet.JUPITER }
                if (jupiterPos != null && YogaHelpers.isAspecting(jupiterPos, planetPos)) {
                    cancellations.add("Jupiter aspect mitigates")
                }

                yogas.add(Yoga(
                    name = "Asta Yoga (${planet.displayName})",
                    sanskritName = "अस्त योग",
                    category = YogaCategory.NEGATIVE_YOGA,
                    planets = listOf(planet, Planet.SUN),
                    houses = listOf(sunPos.house),
                    description = "${planet.displayName} combust at ${String.format("%.1f", normalizedDistance)}° from Sun - $severity",
                    effects = effects,
                    strength = YogaHelpers.strengthFromPercentage(baseStrength),
                    strengthPercentage = baseStrength,
                    isAuspicious = false,
                    activationPeriod = "${planet.displayName}-Sun or Sun-${planet.displayName} periods",
                    cancellationFactors = cancellations.ifEmpty { listOf("Planet's significations weakened during its periods") }
                ))
            }
        }

        return yogas
    }

    // ==================== HOUSE-BASED ARISHTA YOGAS ====================

    private fun evaluateHouseArishtaYogas(chart: VedicChart, houseLords: Map<Int, Planet>): List<Yoga> {
        val yogas = mutableListOf<Yoga>()

        // 1. 8th house heavily afflicted
        val planetsIn8th = chart.planetPositions.filter { it.house == 8 }
        val maleficsIn8th = planetsIn8th.filter {
            it.planet in listOf(Planet.SATURN, Planet.MARS, Planet.RAHU, Planet.KETU)
        }

        if (maleficsIn8th.size >= 2) {
            val strength = 55.0 + (maleficsIn8th.size - 2) * 5.0
            yogas.add(Yoga(
                name = "Ashtama Peeditha Yoga",
                sanskritName = "अष्टम पीडित योग",
                category = YogaCategory.NEGATIVE_YOGA,
                planets = maleficsIn8th.map { it.planet },
                houses = listOf(8),
                description = "${maleficsIn8th.size} malefics in 8th house: ${maleficsIn8th.joinToString { it.planet.displayName }}",
                effects = "Sudden events, transformative experiences, inheritance issues, need for insurance planning, may face surgeries or accidents",
                strength = YogaHelpers.strengthFromPercentage(strength.coerceIn(10.0, 100.0)),
                strengthPercentage = strength.coerceIn(10.0, 100.0),
                isAuspicious = false,
                activationPeriod = "Periods of malefics in 8th house",
                cancellationFactors = listOf("Life insurance recommended", "Transformative spiritual practices help", "May give research/occult abilities")
            ))
        }

        // 2. Lagna lord in 6th, 8th, or 12th
        val lord1 = houseLords[1]
        val lord1Pos = if (lord1 != null) chart.planetPositions.find { it.planet == lord1 } else null

        if (lord1Pos != null && lord1Pos.house in listOf(6, 8, 12)) {
            val effectsByHouse = when (lord1Pos.house) {
                6 -> "Health consciousness needed, may attract enemies, good for service industries"
                8 -> "Transformative life experiences, insurance/occult interests, longevity questions"
                12 -> "Expenses, foreign residence, spiritual inclinations, isolation periods"
                else -> ""
            }

            // Check if cancelled
            val cancellations = mutableListOf<String>()
            if (YogaHelpers.isExalted(lord1Pos) || YogaHelpers.isInOwnSign(lord1Pos)) {
                cancellations.add("Strong dignity reduces negative effects")
            }
            val jupiterPos = chart.planetPositions.find { it.planet == Planet.JUPITER }
            if (jupiterPos != null && YogaHelpers.isAspecting(jupiterPos, lord1Pos)) {
                cancellations.add("Jupiter aspect provides protection")
            }

            val isMitigated = cancellations.isNotEmpty()
            val strength = if (isMitigated) 40.0 else 55.0

            yogas.add(Yoga(
                name = "Lagnesh Dusthana Yoga",
                sanskritName = "लग्नेश दुस्थान योग",
                category = YogaCategory.NEGATIVE_YOGA,
                planets = listOf(lord1!!),
                houses = listOf(lord1Pos.house),
                description = "Lagna lord ${lord1.displayName} in house ${lord1Pos.house}",
                effects = effectsByHouse,
                strength = YogaHelpers.strengthFromPercentage(strength),
                strengthPercentage = strength,
                isAuspicious = false,
                activationPeriod = "Lagna lord Dasha periods",
                cancellationFactors = cancellations.ifEmpty { listOf("Focus on positive outlets for the house significations") }
            ))
        }

        // 3. Kendras empty while dusthanas occupied
        val kendraHouses = listOf(1, 4, 7, 10)
        val dusthanaHouses = listOf(6, 8, 12)

        val planetsInKendras = chart.planetPositions.filter { it.house in kendraHouses }
        val planetsInDusthanas = chart.planetPositions.filter { it.house in dusthanaHouses }

        if (planetsInKendras.isEmpty() && planetsInDusthanas.size >= 3) {
            val strength = 50.0
            yogas.add(Yoga(
                name = "Kendra Shunya Yoga",
                sanskritName = "केंद्र शून्य योग",
                category = YogaCategory.NEGATIVE_YOGA,
                planets = planetsInDusthanas.map { it.planet },
                houses = dusthanaHouses,
                description = "Empty Kendras while ${planetsInDusthanas.size} planets in dusthanas",
                effects = "Lack of strong foundation, life feels challenging, need to work harder for stability, may feel unsupported",
                strength = YogaHelpers.strengthFromPercentage(strength),
                strengthPercentage = strength,
                isAuspicious = false,
                activationPeriod = "Throughout life, especially early years",
                cancellationFactors = listOf("Build strong support systems", "Kendra lords if well-placed still help", "Service and helping others creates good karma")
            ))
        }

        return yogas
    }

    // ==================== RELATIONSHIP ARISHTA YOGAS ====================

    private fun evaluateRelationshipArishtas(chart: VedicChart, houseLords: Map<Int, Planet>): List<Yoga> {
        val yogas = mutableListOf<Yoga>()

        // 1. Vivaha Bandhana Yoga - Marriage difficulties
        val lord7 = houseLords[7]
        val lord7Pos = if (lord7 != null) chart.planetPositions.find { it.planet == lord7 } else null
        val venusPos = chart.planetPositions.find { it.planet == Planet.VENUS }

        // 7th lord in 6th, 8th, or 12th
        if (lord7Pos != null && lord7Pos.house in listOf(6, 8, 12)) {
            val houseEffects = when (lord7Pos.house) {
                6 -> "conflicts in marriage, spouse health issues"
                8 -> "transformative marriage events, inheritance through spouse"
                12 -> "expenses through spouse, foreign spouse possible"
                else -> ""
            }

            val strength = 50.0
            yogas.add(Yoga(
                name = "Vivaha Kashta Yoga",
                sanskritName = "विवाह कष्ट योग",
                category = YogaCategory.NEGATIVE_YOGA,
                planets = listOf(lord7!!),
                houses = listOf(lord7Pos.house),
                description = "7th lord in house ${lord7Pos.house}",
                effects = "Marriage requires effort, $houseEffects, delay or difficulties in finding suitable partner",
                strength = YogaHelpers.strengthFromPercentage(strength),
                strengthPercentage = strength,
                isAuspicious = false,
                activationPeriod = "7th lord Dasha periods, especially for marriage timing",
                cancellationFactors = listOf("Venus strong helps", "Jupiter aspect on 7th protects", "Marriage after 28 often better")
            ))
        }

        // 2. Venus afflicted - General relationship difficulties
        if (venusPos != null) {
            val saturnPos = chart.planetPositions.find { it.planet == Planet.SATURN }
            val marsPos = chart.planetPositions.find { it.planet == Planet.MARS }

            // Venus with Saturn - delays and restrictions in love
            if (saturnPos != null && YogaHelpers.areConjunct(venusPos, saturnPos)) {
                val strength = 50.0
                yogas.add(Yoga(
                    name = "Shukra-Shani Yoga",
                    sanskritName = "शुक्र-शनि योग",
                    category = YogaCategory.NEGATIVE_YOGA,
                    planets = listOf(Planet.VENUS, Planet.SATURN),
                    houses = listOf(venusPos.house),
                    description = "Venus conjunct Saturn",
                    effects = "Delayed marriage, age gap in relationships, difficulties in expressing love, spouse may be older or serious, loyal but reserved partner",
                    strength = YogaHelpers.strengthFromPercentage(strength),
                    strengthPercentage = strength,
                    isAuspicious = false,
                    activationPeriod = "Venus-Saturn periods",
                    cancellationFactors = listOf("Patient approach to relationships", "Mature partner often better match", "Jupiter aspect helps")
                ))
            }

            // Venus with Mars - passion but conflicts
            if (marsPos != null && YogaHelpers.areConjunct(venusPos, marsPos)) {
                val strength = 45.0
                yogas.add(Yoga(
                    name = "Shukra-Mangala Yoga",
                    sanskritName = "शुक्र-मंगल योग",
                    category = YogaCategory.NEGATIVE_YOGA,
                    planets = listOf(Planet.VENUS, Planet.MARS),
                    houses = listOf(venusPos.house),
                    description = "Venus conjunct Mars",
                    effects = "Passionate but volatile relationships, arguments with spouse, excessive desires, need to balance passion with patience",
                    strength = YogaHelpers.strengthFromPercentage(strength),
                    strengthPercentage = strength,
                    isAuspicious = false,
                    activationPeriod = "Venus-Mars periods",
                    cancellationFactors = listOf("Can give success in arts and entertainment", "Channel passion constructively", "If in good houses, passion is productive")
                ))
            }

            // Venus combust - relationship blindness
            val sunPos = chart.planetPositions.find { it.planet == Planet.SUN }
            if (sunPos != null) {
                val distance = kotlin.math.abs(venusPos.longitude - sunPos.longitude)
                val normalizedDistance = if (distance > 180) 360 - distance else distance

                if (normalizedDistance <= 10.0) {
                    val strength = 50.0
                    yogas.add(Yoga(
                        name = "Shukra Asta Vivaha Yoga",
                        sanskritName = "शुक्र अस्त विवाह योग",
                        category = YogaCategory.NEGATIVE_YOGA,
                        planets = listOf(Planet.VENUS, Planet.SUN),
                        houses = listOf(sunPos.house),
                        description = "Venus combust within ${String.format("%.1f", normalizedDistance)}° of Sun",
                        effects = "Difficulty seeing partners clearly, ego in relationships, attraction to unavailable partners, need for realistic expectations",
                        strength = YogaHelpers.strengthFromPercentage(strength),
                        strengthPercentage = strength,
                        isAuspicious = false,
                        activationPeriod = "Venus-Sun periods for marriage matters",
                        cancellationFactors = listOf("Take time before committing", "Seek counsel from elders", "Venus Shanti helps")
                    ))
                }
            }
        }

        // 3. Putra Dosha - Children difficulties
        val lord5 = houseLords[5]
        val lord5Pos = if (lord5 != null) chart.planetPositions.find { it.planet == lord5 } else null
        val jupiterPos = chart.planetPositions.find { it.planet == Planet.JUPITER }

        if (lord5Pos != null && lord5Pos.house in listOf(6, 8, 12) &&
            (jupiterPos == null || jupiterPos.house in listOf(6, 8, 12))) {
            val strength = 50.0
            yogas.add(Yoga(
                name = "Putra Dosha Yoga",
                sanskritName = "पुत्र दोष योग",
                category = YogaCategory.NEGATIVE_YOGA,
                planets = listOfNotNull(lord5, Planet.JUPITER),
                houses = listOf(lord5Pos.house),
                description = "5th lord in dusthana and Jupiter afflicted",
                effects = "Delays or difficulties regarding children, may need medical assistance, children may live far away, adoption possible",
                strength = YogaHelpers.strengthFromPercentage(strength),
                strengthPercentage = strength,
                isAuspicious = false,
                activationPeriod = "5th lord and Jupiter periods",
                cancellationFactors = listOf("Santana Gopala mantra helps", "Medical advances overcome many issues", "Jupiter aspect on 5th protects")
            ))
        }

        return yogas
    }
}
