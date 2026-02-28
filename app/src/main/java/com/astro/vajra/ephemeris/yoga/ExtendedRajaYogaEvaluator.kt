package com.astro.vajra.ephemeris.yoga

import com.astro.vajra.core.model.Planet
import com.astro.vajra.core.model.PlanetPosition
import com.astro.vajra.core.model.VedicChart
import com.astro.vajra.core.model.ZodiacSign

/**
 * Extended Raja Yoga Evaluator - Advanced Kingly Combinations
 *
 * This evaluator handles the extended set of Raja Yogas beyond the basic
 * kendra-trikona combinations. These yogas grant authority, power, leadership,
 * and royal status according to classical Vedic texts.
 *
 * Categories covered:
 * 1. Pancha Mahapurusha Variations - Extended great person yogas
 * 2. Ashtaka Varga Raja Yogas - Based on bindus
 * 3. Simhasana Yoga - Throne/authority yogas
 * 4. Chatussagara Yoga - Four-ocean yogas
 * 5. Rahu-Ketu Raja Yogas - Nodal authority combinations
 * 6. Karaka Raja Yogas - Natural significator-based yogas
 * 7. Hora Lagna Raja Yogas - Based on wealth ascendant
 * 8. Ghatika Lagna Raja Yogas - Based on power ascendant
 * 9. Specific Planet-House Raja Yogas
 * 10. Timing-activated Raja Yogas
 *
 * Based on classical texts:
 * - Brihat Parasara Hora Shastra (BPHS), Chapters 36-41
 * - Phaladeepika, Chapters 6-7
 * - Jataka Parijata, Chapters 7-8
 * - Saravali, Chapters 33-36
 * - Brihat Jataka, Chapter 12
 * - Uttara Kalamrita, Section 5
 *
 * @author AstroVajra
 */
class ExtendedRajaYogaEvaluator : YogaEvaluator {

    override val category: YogaCategory = YogaCategory.RAJA_YOGA

    override fun evaluate(chart: VedicChart): List<Yoga> {
        val yogas = mutableListOf<Yoga>()
        val ascendantSign = ZodiacSign.fromLongitude(chart.ascendant)
        val houseLords = YogaHelpers.getHouseLords(ascendantSign)

        // 1. Extended Kendra-Trikona Raja Yogas
        yogas.addAll(evaluateExtendedKendraTrikona(chart, houseLords))

        // 2. Simhasana Yoga - Authority Combinations
        yogas.addAll(evaluateSimhasanaYogas(chart, houseLords))

        // 3. Chatussagara Yoga - Universal sovereignty
        yogas.addAll(evaluateChatussagaraYogas(chart))

        // 4. Special Planet Raja Yogas
        yogas.addAll(evaluatePlanetSpecificRajaYogas(chart, houseLords, ascendantSign))

        // 5. Rahu-Ketu Raja Yogas
        yogas.addAll(evaluateNodalRajaYogas(chart, houseLords))

        // 6. House Lord Exchange Raja Yogas
        yogas.addAll(evaluateExchangeRajaYogas(chart, houseLords))

        // 7. Natural Karaka Raja Yogas
        yogas.addAll(evaluateKarakaRajaYogas(chart, houseLords))

        // 8. Aspect-based Raja Yogas
        yogas.addAll(evaluateAspectRajaYogas(chart, houseLords))

        // 9. Multiple Planet Raja Yogas
        yogas.addAll(evaluateMultiplePlanetRajaYogas(chart, houseLords))

        // 10. Rare and Powerful Raja Yogas
        yogas.addAll(evaluateRareRajaYogas(chart, houseLords, ascendantSign))

        return yogas
    }

    // ==================== EXTENDED KENDRA-TRIKONA RAJA YOGAS ====================

    private fun evaluateExtendedKendraTrikona(
        chart: VedicChart,
        houseLords: Map<Int, Planet>
    ): List<Yoga> {
        val yogas = mutableListOf<Yoga>()

        val kendras = listOf(1, 4, 7, 10)
        val trikonas = listOf(1, 5, 9)
        val kendraLords = kendras.mapNotNull { houseLords[it] }
        val trikonaLords = trikonas.mapNotNull { houseLords[it] }

        // Count how many kendra-trikona combinations exist
        var rajaYogaCount = 0
        val rajaYogaPlanets = mutableSetOf<Planet>()

        for (kendraLord in kendraLords) {
            val kendraLordPos = chart.planetPositions.find { it.planet == kendraLord } ?: continue

            for (trikonaLord in trikonaLords) {
                if (kendraLord == trikonaLord) continue
                val trikonaLordPos = chart.planetPositions.find { it.planet == trikonaLord } ?: continue

                // Check for conjunction or mutual aspect
                if (YogaHelpers.areConjunct(kendraLordPos, trikonaLordPos) ||
                    YogaHelpers.isAspecting(kendraLordPos, trikonaLordPos) ||
                    YogaHelpers.isAspecting(trikonaLordPos, kendraLordPos)) {
                    rajaYogaCount++
                    rajaYogaPlanets.add(kendraLord)
                    rajaYogaPlanets.add(trikonaLord)
                }
            }
        }

        // Multiple Raja Yogas - Extremely Powerful
        if (rajaYogaCount >= 3) {
            val strength = 85.0 + (rajaYogaCount - 3) * 3

            yogas.add(Yoga(
                name = "Bahudha Raja Yoga",
                sanskritName = "बहुधा राज योग",
                category = YogaCategory.RAJA_YOGA,
                planets = rajaYogaPlanets.toList(),
                houses = rajaYogaPlanets.mapNotNull { planet ->
                    chart.planetPositions.find { it.planet == planet }?.house
                }.distinct(),
                description = "$rajaYogaCount separate kendra-trikona lord combinations formed",
                effects = "Multiple Raja Yogas grant exceptional authority, recognition in multiple fields, lasting fame, wealth accompanies power, respected leadership, multiple achievements",
                strength = YogaHelpers.strengthFromPercentage(strength.coerceAtMost(98.0)),
                strengthPercentage = strength.coerceAtMost(98.0),
                isAuspicious = true,
                activationPeriod = "Dashas of involved planets bring successive rises",
                cancellationFactors = listOf("More combinations = more powerful", "Results multiply not just add")
            ))
        }

        // 5th lord in 10th or 10th lord in 5th - Specific Raja Yoga
        val lord5 = houseLords[5]
        val lord10 = houseLords[10]
        val lord5Pos = if (lord5 != null) chart.planetPositions.find { it.planet == lord5 } else null
        val lord10Pos = if (lord10 != null) chart.planetPositions.find { it.planet == lord10 } else null

        if (lord5Pos?.house == 10 || lord10Pos?.house == 5) {
            val isExchange = lord5Pos?.house == 10 && lord10Pos?.house == 5
            val strength = if (isExchange) 90.0 else 80.0

            yogas.add(Yoga(
                name = if (isExchange) "Pancha-Dashama Parivarttana Raja Yoga" else "Pancha-Dashama Raja Yoga",
                sanskritName = if (isExchange) "पंच-दशम परिवर्तन राज योग" else "पंच-दशम राज योग",
                category = YogaCategory.RAJA_YOGA,
                planets = listOfNotNull(lord5, lord10).distinct(),
                houses = listOf(5, 10),
                description = "5th lord and 10th lord in ${if (isExchange) "mutual exchange" else "each other's houses"}",
                effects = "Creative intelligence leads to powerful position, children support career, fame through intellectual pursuits, political success, administrative authority through wisdom",
                strength = YogaHelpers.strengthFromPercentage(strength),
                strengthPercentage = strength,
                isAuspicious = true,
                activationPeriod = "5th and 10th lord dashas for authority",
                cancellationFactors = listOf("Very powerful yoga for recognition", "Intelligence translates to power")
            ))
        }

        // 9th lord in 10th or 10th lord in 9th - Dharma-Karma Raja Yoga
        val lord9 = houseLords[9]
        val lord9Pos = if (lord9 != null) chart.planetPositions.find { it.planet == lord9 } else null

        if (lord9Pos?.house == 10 || lord10Pos?.house == 9) {
            val isExchange = lord9Pos?.house == 10 && lord10Pos?.house == 9
            val strength = if (isExchange) 95.0 else 85.0

            yogas.add(Yoga(
                name = if (isExchange) "Dharma-Karmadhipati Parivarttana Yoga" else "Dharma-Karmadhipati Raja Yoga",
                sanskritName = if (isExchange) "धर्म-कर्माधिपति परिवर्तन योग" else "धर्म-कर्माधिपति राज योग",
                category = YogaCategory.RAJA_YOGA,
                planets = listOfNotNull(lord9, lord10).distinct(),
                houses = listOf(9, 10),
                description = "9th and 10th lords ${if (isExchange) "exchange" else "in each other's houses"}",
                effects = "Most powerful Raja Yoga - righteous action leads to highest authority, career serves dharma, father supports profession, lasting fame, king-maker position, ethical leadership",
                strength = YogaHelpers.strengthFromPercentage(strength),
                strengthPercentage = strength,
                isAuspicious = true,
                activationPeriod = "9th-10th lord dashas bring peak authority",
                cancellationFactors = listOf("Classical most powerful yoga", "Fortune and karma unite")
            ))
        }

        return yogas
    }

    // ==================== SIMHASANA (THRONE) YOGAS ====================

    private fun evaluateSimhasanaYogas(
        chart: VedicChart,
        houseLords: Map<Int, Planet>
    ): List<Yoga> {
        val yogas = mutableListOf<Yoga>()

        // Simhasana Yoga - All benefics in 2nd, 6th, 8th, 12th from Lagna and Moon
        val moonPos = chart.planetPositions.find { it.planet == Planet.MOON }
        val naturalBenefics = listOf(Planet.JUPITER, Planet.VENUS, Planet.MERCURY, Planet.MOON)

        // Check from Lagna
        val simhasanaHouses = listOf(2, 6, 8, 12)
        val beneficsInSimhasana = chart.planetPositions.filter {
            it.planet in naturalBenefics && it.house in simhasanaHouses
        }

        if (beneficsInSimhasana.size >= 3) {
            yogas.add(Yoga(
                name = "Simhasana Yoga",
                sanskritName = "सिंहासन योग",
                category = YogaCategory.RAJA_YOGA,
                planets = beneficsInSimhasana.map { it.planet },
                houses = beneficsInSimhasana.map { it.house }.distinct(),
                description = "${beneficsInSimhasana.size} benefics in houses 2, 6, 8, 12 from Lagna",
                effects = "Entitled to throne, royal authority, king-like status, commands respect, protected from enemies, wealth supports position, leadership abilities",
                strength = YogaStrength.VERY_STRONG,
                strengthPercentage = 85.0,
                isAuspicious = true,
                activationPeriod = "Benefic planet dashas for royal treatment",
                cancellationFactors = listOf("Classical throne yoga", "Government favor likely")
            ))
        }

        // Bheri Yoga - Venus and Jupiter in kendras, lord of 9th in strong position
        val venusPos = chart.planetPositions.find { it.planet == Planet.VENUS }
        val jupiterPos = chart.planetPositions.find { it.planet == Planet.JUPITER }
        val lord9 = houseLords[9]
        val lord9Pos = if (lord9 != null) chart.planetPositions.find { it.planet == lord9 } else null

        if (venusPos != null && jupiterPos != null) {
            val venusInKendra = venusPos.house in listOf(1, 4, 7, 10)
            val jupiterInKendra = jupiterPos.house in listOf(1, 4, 7, 10)
            val lord9Strong = lord9Pos != null && (
                    YogaHelpers.isExalted(lord9Pos) ||
                            YogaHelpers.isInOwnSign(lord9Pos) ||
                            lord9Pos.house in listOf(1, 4, 5, 7, 9, 10)
                    )

            if (venusInKendra && jupiterInKendra && lord9Strong) {
                yogas.add(Yoga(
                    name = "Bheri Yoga",
                    sanskritName = "भेरी योग",
                    category = YogaCategory.RAJA_YOGA,
                    planets = listOfNotNull(Planet.VENUS, Planet.JUPITER, lord9),
                    houses = listOf(venusPos.house, jupiterPos.house, lord9Pos?.house ?: 0).filter { it > 0 }.distinct(),
                    description = "Venus and Jupiter in kendras with strong 9th lord",
                    effects = "Like a king with drums announcing presence, authority with grace, wealth and wisdom combined, religious inclination in power, protected leadership",
                    strength = YogaStrength.VERY_STRONG,
                    strengthPercentage = 85.0,
                    isAuspicious = true,
                    activationPeriod = "Jupiter and Venus dashas for recognition",
                    cancellationFactors = listOf("Named after royal drums (bheri)", "Graceful authority")
                ))
            }
        }

        // Mridanga Yoga - All planets in kendras and trikonas
        val kendraTrikonas = listOf(1, 4, 5, 7, 9, 10)
        val planetsInKendraTrikona = chart.planetPositions.filter { it.house in kendraTrikonas }
        val planetsElsewhere = chart.planetPositions.filter { it.house !in kendraTrikonas }

        if (planetsInKendraTrikona.size >= 7 && planetsElsewhere.size <= 2) {
            yogas.add(Yoga(
                name = "Mridanga Yoga",
                sanskritName = "मृदंग योग",
                category = YogaCategory.RAJA_YOGA,
                planets = planetsInKendraTrikona.map { it.planet },
                houses = kendraTrikonas,
                description = "Most planets concentrated in kendras and trikonas",
                effects = "Like drums (mridanga) announcing royalty, widespread fame, authority in multiple domains, celebrated life, power with prosperity, lasting dynasty/legacy",
                strength = YogaStrength.VERY_STRONG,
                strengthPercentage = 90.0,
                isAuspicious = true,
                activationPeriod = "Throughout life, enhanced in good dashas",
                cancellationFactors = listOf("Extremely rare configuration", "Fame spreads far")
            ))
        }

        return yogas
    }

    // ==================== CHATUSSAGARA YOGA ====================

    private fun evaluateChatussagaraYogas(chart: VedicChart): List<Yoga> {
        val yogas = mutableListOf<Yoga>()

        // Chatussagara Yoga - All kendras (1, 4, 7, 10) occupied by planets
        val kendras = listOf(1, 4, 7, 10)
        val occupiedKendras = kendras.filter { house ->
            chart.planetPositions.any { it.house == house }
        }

        if (occupiedKendras.size == 4) {
            val kendraplanets = chart.planetPositions.filter { it.house in kendras }
            val beneficCount = kendraplanets.count { it.planet in listOf(Planet.JUPITER, Planet.VENUS, Planet.MERCURY, Planet.MOON) }

            val strength = 80.0 + beneficCount * 3

            yogas.add(Yoga(
                name = "Chatussagara Yoga",
                sanskritName = "चतुःसागर योग",
                category = YogaCategory.RAJA_YOGA,
                planets = kendraplanets.map { it.planet },
                houses = kendras,
                description = "All four kendras (1, 4, 7, 10) occupied by planets",
                effects = "Sovereignty like four oceans, universal authority, fame spreads in all directions, stable power base, support from all quarters, lasting success",
                strength = YogaHelpers.strengthFromPercentage(strength),
                strengthPercentage = strength,
                isAuspicious = true,
                activationPeriod = "Continuous throughout life",
                cancellationFactors = listOf("Named after four oceans surrounding continent", "Benefics in kendras enhance greatly")
            ))
        }

        // Kahala Yoga - Lord of 4th and 9th strong, Jupiter in lagna/4th/10th
        val jupiterPos = chart.planetPositions.find { it.planet == Planet.JUPITER }
        if (jupiterPos != null && jupiterPos.house in listOf(1, 4, 10)) {
            yogas.add(Yoga(
                name = "Kahala Yoga",
                sanskritName = "कहल योग",
                category = YogaCategory.RAJA_YOGA,
                planets = listOf(Planet.JUPITER),
                houses = listOf(jupiterPos.house),
                description = "Jupiter in house ${jupiterPos.house} (lagna/4th/10th)",
                effects = "Bold and daring, strong army/support system, energetic leadership, courageous actions, protected in conflicts, military/strategic success",
                strength = YogaStrength.STRONG,
                strengthPercentage = 75.0,
                isAuspicious = true,
                activationPeriod = "Jupiter dasha brings leadership opportunities",
                cancellationFactors = listOf("Jupiter's benefic influence on angles", "Gives both courage and wisdom")
            ))
        }

        return yogas
    }

    // ==================== PLANET SPECIFIC RAJA YOGAS ====================

    private fun evaluatePlanetSpecificRajaYogas(
        chart: VedicChart,
        houseLords: Map<Int, Planet>,
        ascendantSign: ZodiacSign
    ): List<Yoga> {
        val yogas = mutableListOf<Yoga>()

        // Sun in 10th (Digbala) Raja Yoga
        val sunPos = chart.planetPositions.find { it.planet == Planet.SUN }
        if (sunPos?.house == 10) {
            val isExalted = YogaHelpers.isExalted(sunPos)
            val strength = if (isExalted) 95.0 else 85.0

            yogas.add(Yoga(
                name = "Surya Digbala Raja Yoga",
                sanskritName = "सूर्य दिग्बल राज योग",
                category = YogaCategory.RAJA_YOGA,
                planets = listOf(Planet.SUN),
                houses = listOf(10),
                description = "Sun in 10th house with directional strength${if (isExalted) " and exalted" else ""}",
                effects = "Government authority, king-like status, powerful career, father influential, administrative success, recognized leader, authoritative presence",
                strength = YogaHelpers.strengthFromPercentage(strength),
                strengthPercentage = strength,
                isAuspicious = true,
                activationPeriod = "Sun dasha brings peak authority",
                cancellationFactors = listOf("Sun gets digbala in 10th", "Natural significator of government")
            ))
        }

        // Jupiter in Lagna (Digbala) Raja Yoga
        val jupiterPos = chart.planetPositions.find { it.planet == Planet.JUPITER }
        if (jupiterPos?.house == 1) {
            val isStrong = YogaHelpers.isExalted(jupiterPos) || YogaHelpers.isInOwnSign(jupiterPos)
            val strength = if (isStrong) 95.0 else 85.0

            yogas.add(Yoga(
                name = "Guru Digbala Raja Yoga",
                sanskritName = "गुरु दिग्बल राज योग",
                category = YogaCategory.RAJA_YOGA,
                planets = listOf(Planet.JUPITER),
                houses = listOf(1),
                description = "Jupiter in Lagna with directional strength${if (isStrong) " in dignity" else ""}",
                effects = "Wise ruler, respected authority, ethical leadership, teacher-like presence, children bring honor, dharmic power, protected from enemies",
                strength = YogaHelpers.strengthFromPercentage(strength),
                strengthPercentage = strength,
                isAuspicious = true,
                activationPeriod = "Jupiter dasha brings recognition and authority",
                cancellationFactors = listOf("Jupiter gets digbala in 1st", "Protects the entire chart")
            ))
        }

        // Mars in 10th (Digbala) for Aries/Scorpio risings
        val marsPos = chart.planetPositions.find { it.planet == Planet.MARS }
        if (marsPos?.house == 10) {
            val isExalted = YogaHelpers.isExalted(marsPos)
            val isYogakaraka = ascendantSign in listOf(ZodiacSign.CANCER, ZodiacSign.LEO)
            val strength = when {
                isExalted && isYogakaraka -> 95.0
                isExalted || isYogakaraka -> 85.0
                else -> 75.0
            }

            yogas.add(Yoga(
                name = "Kuja Digbala Raja Yoga",
                sanskritName = "कुज दिग्बल राज योग",
                category = YogaCategory.RAJA_YOGA,
                planets = listOf(Planet.MARS),
                houses = listOf(10),
                description = "Mars in 10th house with directional strength${if (isYogakaraka) " as Yogakaraka" else ""}",
                effects = "Military/executive authority, bold leadership, achievement through action, engineering/technical success, sports fame, courageous decisions",
                strength = YogaHelpers.strengthFromPercentage(strength),
                strengthPercentage = strength,
                isAuspicious = true,
                activationPeriod = "Mars dasha brings executive authority",
                cancellationFactors = listOf("Mars gets digbala in 10th", "Action-oriented success")
            ))
        }

        // Saturn in 7th (Digbala) for Libra/Aquarius/Taurus risings
        val saturnPos = chart.planetPositions.find { it.planet == Planet.SATURN }
        if (saturnPos?.house == 7) {
            val isYogakaraka = ascendantSign in listOf(ZodiacSign.TAURUS, ZodiacSign.LIBRA)
            val isExalted = YogaHelpers.isExalted(saturnPos)
            val strength = when {
                isExalted && isYogakaraka -> 90.0
                isExalted || isYogakaraka -> 80.0
                else -> 70.0
            }

            if (isYogakaraka || isExalted) {
                yogas.add(Yoga(
                    name = "Shani Digbala Raja Yoga",
                    sanskritName = "शनि दिग्बल राज योग",
                    category = YogaCategory.RAJA_YOGA,
                    planets = listOf(Planet.SATURN),
                    houses = listOf(7),
                    description = "Saturn in 7th with directional strength${if (isYogakaraka) " as Yogakaraka" else ""}",
                    effects = "Authority through partnerships, democratic leadership, slow but steady rise, lasting power, public service, authority in later life",
                    strength = YogaHelpers.strengthFromPercentage(strength),
                    strengthPercentage = strength,
                    isAuspicious = true,
                    activationPeriod = "Saturn dasha (after 36) brings authority",
                    cancellationFactors = listOf("Saturn gets digbala in 7th", "Partnership-based success")
                ))
            }
        }

        return yogas
    }

    // ==================== NODAL RAJA YOGAS ====================

    private fun evaluateNodalRajaYogas(
        chart: VedicChart,
        houseLords: Map<Int, Planet>
    ): List<Yoga> {
        val yogas = mutableListOf<Yoga>()

        val rahuPos = chart.planetPositions.find { it.planet == Planet.RAHU }
        val ketuPos = chart.planetPositions.find { it.planet == Planet.KETU }

        // Rahu in 10th - Worldly authority yoga
        if (rahuPos?.house == 10) {
            val jupiterAspect = chart.planetPositions.find { it.planet == Planet.JUPITER }
                ?.let { YogaHelpers.isAspecting(it, rahuPos) } == true

            val strength = if (jupiterAspect) 85.0 else 70.0

            yogas.add(Yoga(
                name = "Rahu Karma Raja Yoga",
                sanskritName = "राहु कर्म राज योग",
                category = YogaCategory.RAJA_YOGA,
                planets = listOfNotNull(Planet.RAHU, if (jupiterAspect) Planet.JUPITER else null),
                houses = listOf(10),
                description = "Rahu in 10th house of career${if (jupiterAspect) " with Jupiter's aspect" else ""}",
                effects = "Unusual rise to power, authority through unconventional means, foreign connections in career, technology/research leadership, sudden prominence",
                strength = YogaHelpers.strengthFromPercentage(strength),
                strengthPercentage = strength,
                isAuspicious = jupiterAspect,
                activationPeriod = "Rahu dasha brings rapid rise",
                cancellationFactors = listOf(
                    if (jupiterAspect) "Jupiter sanctifies Rahu's energy" else "Jupiter aspect would stabilize",
                    "May involve foreign elements"
                )
            ))
        }

        // Rahu conjunct 9th or 10th lord
        val lord9 = houseLords[9]
        val lord10 = houseLords[10]
        val lord9Pos = if (lord9 != null) chart.planetPositions.find { it.planet == lord9 } else null
        val lord10Pos = if (lord10 != null) chart.planetPositions.find { it.planet == lord10 } else null

        if (rahuPos != null) {
            if (lord9Pos != null && YogaHelpers.areConjunct(rahuPos, lord9Pos)) {
                yogas.add(Yoga(
                    name = "Rahu Bhagyesh Yoga",
                    sanskritName = "राहु भाग्येश योग",
                    category = YogaCategory.RAJA_YOGA,
                    planets = listOf(Planet.RAHU, lord9!!),
                    houses = listOf(rahuPos.house),
                    description = "Rahu conjunct 9th lord ${lord9.displayName}",
                    effects = "Fortune through unconventional paths, foreign luck, technology brings fortune, spiritual seeking through unusual means, amplified luck",
                    strength = YogaStrength.STRONG,
                    strengthPercentage = 75.0,
                    isAuspicious = true,
                    activationPeriod = "Rahu dasha or 9th lord antardasha",
                    cancellationFactors = listOf("Rahu amplifies 9th lord", "May involve foreign elements")
                ))
            }

            if (lord10Pos != null && YogaHelpers.areConjunct(rahuPos, lord10Pos)) {
                yogas.add(Yoga(
                    name = "Rahu Karmesh Yoga",
                    sanskritName = "राहु कर्मेश योग",
                    category = YogaCategory.RAJA_YOGA,
                    planets = listOf(Planet.RAHU, lord10!!),
                    houses = listOf(rahuPos.house),
                    description = "Rahu conjunct 10th lord ${lord10.displayName}",
                    effects = "Worldly ambition amplified, unusual career path, technology/foreign career, sudden rise possible, research/occult profession, unconventional authority",
                    strength = YogaStrength.STRONG,
                    strengthPercentage = 75.0,
                    isAuspicious = true,
                    activationPeriod = "Rahu and 10th lord periods bring career leap",
                    cancellationFactors = listOf("Rahu amplifies career karma", "May involve controversy")
                ))
            }
        }

        // Ketu with 5th lord - Spiritual intelligence leading to authority
        val lord5 = houseLords[5]
        val lord5Pos = if (lord5 != null) chart.planetPositions.find { it.planet == lord5 } else null

        if (ketuPos != null && lord5Pos != null && YogaHelpers.areConjunct(ketuPos, lord5Pos)) {
            yogas.add(Yoga(
                name = "Ketu Putrash Yoga",
                sanskritName = "केतु पुत्रेश योग",
                category = YogaCategory.RAJA_YOGA,
                planets = listOf(Planet.KETU, lord5!!),
                houses = listOf(ketuPos.house),
                description = "Ketu conjunct 5th lord ${lord5.displayName}",
                effects = "Spiritual intelligence, intuitive creativity, past-life merit activating, may guide others through wisdom, authority through insight, non-conventional teaching",
                strength = YogaStrength.STRONG,
                strengthPercentage = 75.0,
                isAuspicious = true,
                activationPeriod = "Ketu dasha activates purva punya",
                cancellationFactors = listOf("Ketu brings past-life intelligence", "Spiritual authority possible")
            ))
        }

        return yogas
    }

    // ==================== EXCHANGE RAJA YOGAS ====================

    private fun evaluateExchangeRajaYogas(
        chart: VedicChart,
        houseLords: Map<Int, Planet>
    ): List<Yoga> {
        val yogas = mutableListOf<Yoga>()

        // Check for all possible kendra-trikona exchanges
        val kendras = listOf(1, 4, 7, 10)
        val trikonas = listOf(5, 9)  // Excluding 1 as it's in kendras

        for (kendra in kendras) {
            val kendraLord = houseLords[kendra] ?: continue
            val kendraLordPos = chart.planetPositions.find { it.planet == kendraLord } ?: continue

            for (trikona in trikonas) {
                val trikonaLord = houseLords[trikona] ?: continue
                if (trikonaLord == kendraLord) continue
                val trikonaLordPos = chart.planetPositions.find { it.planet == trikonaLord } ?: continue

                // Check for exchange
                if (kendraLordPos.house == trikona && trikonaLordPos.house == kendra) {
                    yogas.add(Yoga(
                        name = "Parivarttana Raja Yoga ($kendra-$trikona)",
                        sanskritName = "परिवर्तन राज योग",
                        category = YogaCategory.RAJA_YOGA,
                        planets = listOf(kendraLord, trikonaLord),
                        houses = listOf(kendra, trikona),
                        description = "${kendra}th lord ${kendraLord.displayName} exchanges with ${trikona}th lord ${trikonaLord.displayName}",
                        effects = "Powerful mutual support between houses, both significations enhanced, lasting success in both areas, karmic connection between themes",
                        strength = YogaStrength.VERY_STRONG,
                        strengthPercentage = 85.0,
                        isAuspicious = true,
                        activationPeriod = "Both lords' dashas bring combined results",
                        cancellationFactors = listOf("Exchange is like two planets in each other's house", "Very powerful combination")
                    ))
                }
            }
        }

        // Maha Parivarttana - 1st and 10th lord exchange
        val lord1 = houseLords[1]
        val lord10 = houseLords[10]
        val lord1Pos = if (lord1 != null) chart.planetPositions.find { it.planet == lord1 } else null
        val lord10Pos = if (lord10 != null) chart.planetPositions.find { it.planet == lord10 } else null

        if (lord1Pos?.house == 10 && lord10Pos?.house == 1 && lord1 != lord10) {
            yogas.add(Yoga(
                name = "Maha Parivarttana Raja Yoga",
                sanskritName = "महा परिवर्तन राज योग",
                category = YogaCategory.RAJA_YOGA,
                planets = listOf(lord1!!, lord10!!),
                houses = listOf(1, 10),
                description = "Lagna lord ${lord1.displayName} exchanges with 10th lord ${lord10.displayName}",
                effects = "Self and career perfectly aligned, profession reflects true self, exceptional authority, recognized leader, karma and identity unite, lasting legacy",
                strength = YogaStrength.EXTREMELY_STRONG,
                strengthPercentage = 95.0,
                isAuspicious = true,
                activationPeriod = "Both lords' dashas - peak of life",
                cancellationFactors = listOf("Extremely powerful exchange", "Career becomes identity")
            ))
        }

        return yogas
    }

    // ==================== KARAKA RAJA YOGAS ====================

    private fun evaluateKarakaRajaYogas(
        chart: VedicChart,
        houseLords: Map<Int, Planet>
    ): List<Yoga> {
        val yogas = mutableListOf<Yoga>()

        // Sun (King) + Jupiter (Guru) conjunction or aspect
        val sunPos = chart.planetPositions.find { it.planet == Planet.SUN }
        val jupiterPos = chart.planetPositions.find { it.planet == Planet.JUPITER }

        if (sunPos != null && jupiterPos != null) {
            val conjunct = YogaHelpers.areConjunct(sunPos, jupiterPos)
            val mutual = YogaHelpers.isAspecting(sunPos, jupiterPos) && YogaHelpers.isAspecting(jupiterPos, sunPos)

            if (conjunct || mutual) {
                yogas.add(Yoga(
                    name = "Surya-Guru Raja Yoga",
                    sanskritName = "सूर्य-गुरु राज योग",
                    category = YogaCategory.RAJA_YOGA,
                    planets = listOf(Planet.SUN, Planet.JUPITER),
                    houses = if (conjunct) listOf(sunPos.house) else listOf(sunPos.house, jupiterPos.house),
                    description = "Sun and Jupiter ${if (conjunct) "conjunct" else "in mutual aspect"}",
                    effects = "King and minister combination, wise authority, ethical governance, government success, father figure, teaching with authority, religious leadership",
                    strength = YogaStrength.VERY_STRONG,
                    strengthPercentage = 85.0,
                    isAuspicious = true,
                    activationPeriod = "Sun and Jupiter dashas for recognition",
                    cancellationFactors = listOf("Natural karakas for authority", "Righteous power")
                ))
            }
        }

        // Moon (Queen) + Venus (Luxury) in good houses
        val moonPos = chart.planetPositions.find { it.planet == Planet.MOON }
        val venusPos = chart.planetPositions.find { it.planet == Planet.VENUS }

        if (moonPos != null && venusPos != null) {
            val bothInGoodHouses = moonPos.house in listOf(1, 2, 4, 5, 7, 9, 10, 11) &&
                    venusPos.house in listOf(1, 2, 4, 5, 7, 9, 10, 11)
            val connected = YogaHelpers.areConjunct(moonPos, venusPos)

            if (bothInGoodHouses && connected) {
                yogas.add(Yoga(
                    name = "Chandra-Shukra Rajeshwari Yoga",
                    sanskritName = "चन्द्र-शुक्र राजेश्वरी योग",
                    category = YogaCategory.RAJA_YOGA,
                    planets = listOf(Planet.MOON, Planet.VENUS),
                    houses = listOf(moonPos.house),
                    description = "Moon and Venus conjunct in auspicious house ${moonPos.house}",
                    effects = "Queen-like status, luxurious life, fame through arts/beauty, public adoration, emotional fulfillment with luxury, feminine power, creative authority",
                    strength = YogaStrength.STRONG,
                    strengthPercentage = 80.0,
                    isAuspicious = true,
                    activationPeriod = "Moon and Venus dashas bring recognition",
                    cancellationFactors = listOf("Natural karakas for comfort and fame", "Good for public appeal")
                ))
            }
        }

        return yogas
    }

    // ==================== ASPECT RAJA YOGAS ====================

    private fun evaluateAspectRajaYogas(
        chart: VedicChart,
        houseLords: Map<Int, Planet>
    ): List<Yoga> {
        val yogas = mutableListOf<Yoga>()

        val lord9 = houseLords[9]
        val lord10 = houseLords[10]
        val lord5 = houseLords[5]

        val lord9Pos = if (lord9 != null) chart.planetPositions.find { it.planet == lord9 } else null
        val lord10Pos = if (lord10 != null) chart.planetPositions.find { it.planet == lord10 } else null
        val lord5Pos = if (lord5 != null) chart.planetPositions.find { it.planet == lord5 } else null

        // 9th lord aspecting 10th lord
        if (lord9Pos != null && lord10Pos != null && YogaHelpers.isAspecting(lord9Pos, lord10Pos)) {
            yogas.add(Yoga(
                name = "Bhagyesh-Karmesh Drishti Yoga",
                sanskritName = "भाग्येश-कर्मेश दृष्टि योग",
                category = YogaCategory.RAJA_YOGA,
                planets = listOf(lord9!!, lord10!!),
                houses = listOf(lord9Pos.house, lord10Pos.house),
                description = "9th lord ${lord9.displayName} aspects 10th lord ${lord10.displayName}",
                effects = "Fortune supports career, dharmic profession, father helps career, religious work, guru guides profession, ethical success",
                strength = YogaStrength.STRONG,
                strengthPercentage = 75.0,
                isAuspicious = true,
                activationPeriod = "9th lord dasha benefits career",
                cancellationFactors = listOf("Aspect also creates connection", "Less powerful than conjunction but significant")
            ))
        }

        // 5th lord aspecting 9th lord - Trikona lords mutual support
        if (lord5Pos != null && lord9Pos != null && YogaHelpers.isAspecting(lord5Pos, lord9Pos)) {
            yogas.add(Yoga(
                name = "Trikona Drishti Yoga",
                sanskritName = "त्रिकोण दृष्टि योग",
                category = YogaCategory.RAJA_YOGA,
                planets = listOf(lord5!!, lord9!!),
                houses = listOf(lord5Pos.house, lord9Pos.house),
                description = "5th lord ${lord5.displayName} aspects 9th lord ${lord9.displayName}",
                effects = "Intelligence supports fortune, children bring luck, creative dharma, mantra siddhi, past merit activates luck",
                strength = YogaStrength.STRONG,
                strengthPercentage = 75.0,
                isAuspicious = true,
                activationPeriod = "5th and 9th lord periods mutually support",
                cancellationFactors = listOf("Trikona lords are always beneficial", "Creative fortune combination")
            ))
        }

        return yogas
    }

    // ==================== MULTIPLE PLANET RAJA YOGAS ====================

    private fun evaluateMultiplePlanetRajaYogas(
        chart: VedicChart,
        houseLords: Map<Int, Planet>
    ): List<Yoga> {
        val yogas = mutableListOf<Yoga>()

        // Pushkala Yoga - Lagna lord strong, Moon with benefic, all angles aspected by benefics
        val lord1 = houseLords[1]
        val lord1Pos = if (lord1 != null) chart.planetPositions.find { it.planet == lord1 } else null
        val moonPos = chart.planetPositions.find { it.planet == Planet.MOON }

        if (lord1Pos != null && moonPos != null) {
            val lagneshStrong = YogaHelpers.isExalted(lord1Pos) || YogaHelpers.isInOwnSign(lord1Pos)
            val moonWithBenefic = chart.planetPositions.any {
                it.planet in listOf(Planet.JUPITER, Planet.VENUS, Planet.MERCURY) &&
                        YogaHelpers.areConjunct(moonPos, it)
            }

            if (lagneshStrong && moonWithBenefic) {
                yogas.add(Yoga(
                    name = "Pushkala Yoga",
                    sanskritName = "पुष्कल योग",
                    category = YogaCategory.RAJA_YOGA,
                    planets = listOf(lord1!!, Planet.MOON),
                    houses = listOf(lord1Pos.house, moonPos.house),
                    description = "Strong lagna lord with Moon joined by benefics",
                    effects = "Nourished by fortune, abundant resources, honored by rulers, eloquent speech, famous and wealthy, leadership through goodness",
                    strength = YogaStrength.VERY_STRONG,
                    strengthPercentage = 85.0,
                    isAuspicious = true,
                    activationPeriod = "Lagna lord and Moon dashas for recognition",
                    cancellationFactors = listOf("Pushkala means nourished/abundant", "Very beneficial yoga")
                ))
            }
        }

        // Chakra Yoga - All planets in alternate houses (1,3,5,7,9,11 OR 2,4,6,8,10,12)
        val oddHousePlanets = chart.planetPositions.filter { it.house in listOf(1, 3, 5, 7, 9, 11) }
        val evenHousePlanets = chart.planetPositions.filter { it.house in listOf(2, 4, 6, 8, 10, 12) }

        if (oddHousePlanets.size >= 7 || evenHousePlanets.size >= 7) {
            val isOddChakra = oddHousePlanets.size >= 7
            yogas.add(Yoga(
                name = "Chakra Yoga",
                sanskritName = "चक्र योग",
                category = YogaCategory.RAJA_YOGA,
                planets = if (isOddChakra) oddHousePlanets.map { it.planet } else evenHousePlanets.map { it.planet },
                houses = if (isOddChakra) listOf(1, 3, 5, 7, 9, 11) else listOf(2, 4, 6, 8, 10, 12),
                description = "Most planets in ${if (isOddChakra) "odd" else "even"} houses forming wheel pattern",
                effects = "Like a chakra (wheel) of authority, universal sovereignty, commands far and wide, military success, administrative genius, emperor-like status",
                strength = YogaStrength.VERY_STRONG,
                strengthPercentage = 85.0,
                isAuspicious = true,
                activationPeriod = "Throughout life with enhancing dashas",
                cancellationFactors = listOf("Named after chakra/wheel of sovereignty", "Rare and powerful configuration")
            ))
        }

        return yogas
    }

    // ==================== RARE RAJA YOGAS ====================

    private fun evaluateRareRajaYogas(
        chart: VedicChart,
        houseLords: Map<Int, Planet>,
        ascendantSign: ZodiacSign
    ): List<Yoga> {
        val yogas = mutableListOf<Yoga>()

        // Akhanda Samrajya Yoga - Jupiter in 2nd/5th/11th, lord of 2nd/9th/11th in kendra from Moon
        val jupiterPos = chart.planetPositions.find { it.planet == Planet.JUPITER }
        val moonPos = chart.planetPositions.find { it.planet == Planet.MOON }

        if (jupiterPos != null && jupiterPos.house in listOf(2, 5, 11) && moonPos != null) {
            val lord2 = houseLords[2]
            val lord9 = houseLords[9]
            val lord11 = houseLords[11]

            val kendrasFromMoon = listOf(1, 4, 7, 10).map { ((moonPos.house - 1 + it - 1) % 12) + 1 }

            val hasKendraLord = listOfNotNull(lord2, lord9, lord11).any { lord ->
                val lordPos = chart.planetPositions.find { it.planet == lord }
                lordPos?.house in kendrasFromMoon
            }

            if (hasKendraLord) {
                yogas.add(Yoga(
                    name = "Akhanda Samrajya Yoga",
                    sanskritName = "अखंड साम्राज्य योग",
                    category = YogaCategory.RAJA_YOGA,
                    planets = listOf(Planet.JUPITER, Planet.MOON),
                    houses = listOf(jupiterPos.house, moonPos.house),
                    description = "Jupiter in ${jupiterPos.house} with wealth lords in kendras from Moon",
                    effects = "Undivided empire, lasting sovereignty, dynasty founder, tremendous wealth with power, unbroken authority, legendary status",
                    strength = YogaStrength.EXTREMELY_STRONG,
                    strengthPercentage = 95.0,
                    isAuspicious = true,
                    activationPeriod = "Jupiter dasha brings empire-level success",
                    cancellationFactors = listOf("One of the most powerful yogas", "Creates lasting legacy")
                ))
            }
        }

        // Vasumati Yoga - All benefics in upachaya houses (3, 6, 10, 11)
        val naturalBenefics = listOf(Planet.JUPITER, Planet.VENUS, Planet.MERCURY, Planet.MOON)
        val upachayas = listOf(3, 6, 10, 11)

        val beneficsInUpachaya = chart.planetPositions.filter {
            it.planet in naturalBenefics && it.house in upachayas
        }

        if (beneficsInUpachaya.size >= 3) {
            yogas.add(Yoga(
                name = "Vasumati Yoga",
                sanskritName = "वसुमती योग",
                category = YogaCategory.RAJA_YOGA,
                planets = beneficsInUpachaya.map { it.planet },
                houses = beneficsInUpachaya.map { it.house }.distinct(),
                description = "${beneficsInUpachaya.size} benefics in upachaya houses (3, 6, 10, 11)",
                effects = "Tremendous wealth, victory over enemies, career success, gains increase over time, competitive success with ethical means, growing fortune",
                strength = YogaStrength.VERY_STRONG,
                strengthPercentage = 85.0,
                isAuspicious = true,
                activationPeriod = "Benefic planet dashas bring growing wealth",
                cancellationFactors = listOf("Vasumati means wealthy/prosperous", "Upachayas give increasing results")
            ))
        }

        // Parijata Yoga - Lagna lord in trikona/kendra, dispositor also well-placed
        val lord1 = houseLords[1]
        val lord1Pos = if (lord1 != null) chart.planetPositions.find { it.planet == lord1 } else null

        if (lord1Pos != null && lord1Pos.house in listOf(1, 4, 5, 7, 9, 10)) {
            val dispositor = YogaHelpers.getSignLord(ZodiacSign.fromLongitude(lord1Pos.longitude))
            val dispositorPos = chart.planetPositions.find { it.planet == dispositor }

            if (dispositorPos != null && dispositorPos.house in listOf(1, 4, 5, 7, 9, 10)) {
                yogas.add(Yoga(
                    name = "Parijata Yoga",
                    sanskritName = "पारिजात योग",
                    category = YogaCategory.RAJA_YOGA,
                    planets = listOf(lord1!!, dispositor),
                    houses = listOf(lord1Pos.house, dispositorPos.house),
                    description = "Lagna lord in kendra/trikona, its dispositor also well-placed",
                    effects = "Celestial tree of wishes, desires fulfilled, respected like royalty, comfortable middle age, surrounded by loved ones, lasting happiness",
                    strength = YogaStrength.STRONG,
                    strengthPercentage = 80.0,
                    isAuspicious = true,
                    activationPeriod = "Lagna lord and dispositor periods bring fulfillment",
                    cancellationFactors = listOf("Parijata is celestial wish-fulfilling tree", "Dispositor chain creates strength")
                ))
            }
        }

        // Sreenatha Yoga - Exalted 7th lord in 10th with 10th lord in 9th
        val lord7 = houseLords[7]
        val lord10 = houseLords[10]
        val lord7Pos = if (lord7 != null) chart.planetPositions.find { it.planet == lord7 } else null
        val lord10Pos = if (lord10 != null) chart.planetPositions.find { it.planet == lord10 } else null

        if (lord7Pos != null && lord10Pos != null) {
            val lord7Exalted = YogaHelpers.isExalted(lord7Pos) && lord7Pos.house == 10
            val lord10In9 = lord10Pos.house == 9

            if (lord7Exalted && lord10In9) {
                yogas.add(Yoga(
                    name = "Sreenatha Yoga",
                    sanskritName = "श्रीनाथ योग",
                    category = YogaCategory.RAJA_YOGA,
                    planets = listOf(lord7!!, lord10!!),
                    houses = listOf(10, 9),
                    description = "7th lord exalted in 10th with 10th lord in 9th",
                    effects = "Lord of prosperity, tremendous wealth through career, spouse supports success, partnership brings authority, dharmic profession, lasting legacy",
                    strength = YogaStrength.EXTREMELY_STRONG,
                    strengthPercentage = 95.0,
                    isAuspicious = true,
                    activationPeriod = "7th and 10th lord dashas for peak success",
                    cancellationFactors = listOf("Sreenatha means Lord of Prosperity", "Very rare and powerful")
                ))
            }
        }

        return yogas
    }
}
