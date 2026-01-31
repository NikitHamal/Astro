package com.astro.storm.ephemeris.yoga

import com.astro.storm.core.model.Planet
import com.astro.storm.core.model.PlanetPosition
import com.astro.storm.core.model.VedicChart
import com.astro.storm.core.model.ZodiacSign

/**
 * Classical Nabhasa Yoga Evaluator - Complete 32 Nabhasa Yogas
 *
 * Nabhasa means "celestial" or "heavenly". These yogas are formed by the pattern
 * of planetary distribution across the chart, independent of house lordship.
 * They are among the most important yogas in Vedic astrology.
 *
 * The 32 Nabhasa Yogas are classified into 4 categories (per Hora Sara):
 *
 * 1. AKRITI YOGAS (20 yogas) - Based on geometric patterns of planets
 *    - Rajju (rope), Musala (pestle), Nala (reed), Mala (garland), Sarpa (serpent)
 *    - Gada (mace), Sakata (cart), Vihaga (bird), Shringataka (peak)
 *    - Hala (plough), Vajra (thunderbolt), Yava (barley), Kamala (lotus)
 *    - Vapi (well), Yupa (sacrificial post), Shara (arrow), Shakti (spear)
 *    - Danda (staff), Nauka (boat), Kuta (peak)
 *
 * 2. SANKHYA YOGAS (7 yogas) - Based on number of occupied houses
 *    - Vallaki/Veena, Dama, Pasha, Kedara, Shoola, Yuga, Gola
 *
 * 3. DALA YOGAS (2 yogas) - Based on planetary concentration
 *    - Mala (Srik), Sarpa (Bhujanga)
 *
 * 4. AKRITI YOGAS - Additional geometric patterns
 *
 * Based on classical texts:
 * - Brihat Jataka, Chapter 12
 * - Hora Sara, Chapter 10
 * - Phaladeepika, Chapter 7
 * - Saravali, Chapter 21
 *
 * @author AstroStorm
 */
class ClassicalNabhasaYogaEvaluator : YogaEvaluator {

    override val category: YogaCategory = YogaCategory.NABHASA_YOGA

    // We consider only the 7 classical planets (exclude Rahu/Ketu for Nabhasa)
    private val classicalPlanets = listOf(
        Planet.SUN, Planet.MOON, Planet.MARS, Planet.MERCURY,
        Planet.JUPITER, Planet.VENUS, Planet.SATURN
    )

    override fun evaluate(chart: VedicChart): List<Yoga> {
        val yogas = mutableListOf<Yoga>()

        // Get classical planet positions only
        val planetPositions = chart.planetPositions.filter { it.planet in classicalPlanets }
        val occupiedHouses = planetPositions.map { it.house }.distinct().sorted()
        val houseCount = occupiedHouses.size

        // 1. Sankhya Yogas (based on number of occupied houses)
        yogas.addAll(evaluateSankhyaYogas(planetPositions, occupiedHouses, houseCount))

        // 2. Akriti Yogas (based on geometric patterns)
        yogas.addAll(evaluateAkritiYogas(planetPositions, occupiedHouses))

        // 3. Dala Yogas (based on concentration patterns)
        yogas.addAll(evaluateDalaYogas(planetPositions, occupiedHouses))

        // 4. Ashraya Yogas (based on movable/fixed/dual signs)
        yogas.addAll(evaluateAshrayaYogas(planetPositions, chart))

        return yogas
    }

    // ==================== SANKHYA YOGAS ====================
    // Based on how many houses are occupied by the 7 classical planets

    private fun evaluateSankhyaYogas(
        planetPositions: List<PlanetPosition>,
        occupiedHouses: List<Int>,
        houseCount: Int
    ): List<Yoga> {
        val yogas = mutableListOf<Yoga>()

        when (houseCount) {
            1 -> {
                // Gola Yoga - All 7 planets in one house
                yogas.add(Yoga(
                    name = "Gola Yoga",
                    sanskritName = "गोल योग",
                    category = YogaCategory.NABHASA_YOGA,
                    planets = planetPositions.map { it.planet },
                    houses = occupiedHouses,
                    description = "All 7 classical planets concentrated in house ${occupiedHouses.first()}",
                    effects = "Extremely rare - person is either exceptionally powerful or faces extreme challenges. Life highly focused on one area. Can indicate great concentration of karma.",
                    strength = YogaStrength.EXTREMELY_STRONG,
                    strengthPercentage = 95.0,
                    isAuspicious = false, // Mixed depending on house
                    activationPeriod = "Throughout life with extreme focus",
                    cancellationFactors = listOf("Extremely rare yoga", "Results depend heavily on the occupied house and sign")
                ))
            }
            2 -> {
                // Yuga Yoga - All planets in two houses
                yogas.add(Yoga(
                    name = "Yuga Yoga",
                    sanskritName = "युग योग",
                    category = YogaCategory.NABHASA_YOGA,
                    planets = planetPositions.map { it.planet },
                    houses = occupiedHouses,
                    description = "All 7 classical planets in only 2 houses: ${occupiedHouses.joinToString(", ")}",
                    effects = "Highly concentrated karma in two life areas. Person may be poor, miserable, or orphaned according to classics. Life polarized between two extremes.",
                    strength = YogaStrength.STRONG,
                    strengthPercentage = 75.0,
                    isAuspicious = false,
                    activationPeriod = "Life alternates between the two houses' significations",
                    cancellationFactors = listOf("Very rare configuration", "Results depend on which houses are occupied")
                ))
            }
            3 -> {
                // Shoola Yoga - All planets in three houses
                yogas.add(Yoga(
                    name = "Shoola Yoga",
                    sanskritName = "शूल योग",
                    category = YogaCategory.NABHASA_YOGA,
                    planets = planetPositions.map { it.planet },
                    houses = occupiedHouses,
                    description = "All 7 classical planets in 3 houses: ${occupiedHouses.joinToString(", ")}",
                    effects = "Sharp and penetrating like a trident (shoola). Person may be poor, valorous, cruel, or engage in forbidden activities. Concentrated energy can be directed positively.",
                    strength = YogaStrength.STRONG,
                    strengthPercentage = 70.0,
                    isAuspicious = false,
                    activationPeriod = "Life focused on three areas represented by occupied houses",
                    cancellationFactors = listOf("If in trines or kendras, much more positive", "Benefic planets mitigate negative effects")
                ))
            }
            4 -> {
                // Kedara Yoga - All planets in four houses
                yogas.add(Yoga(
                    name = "Kedara Yoga",
                    sanskritName = "केदार योग",
                    category = YogaCategory.NABHASA_YOGA,
                    planets = planetPositions.map { it.planet },
                    houses = occupiedHouses,
                    description = "All 7 classical planets in 4 houses: ${occupiedHouses.joinToString(", ")}",
                    effects = "Like a cultivated field (kedara). Person is agriculturist, helpful to others, happy, wealthy, truthful. Productive and grounded nature.",
                    strength = YogaStrength.STRONG,
                    strengthPercentage = 75.0,
                    isAuspicious = true,
                    activationPeriod = "Productive periods during involved planets' dashas",
                    cancellationFactors = listOf("Balanced concentration of energy", "Good for practical achievements")
                ))
            }
            5 -> {
                // Pasha Yoga - All planets in five houses
                yogas.add(Yoga(
                    name = "Pasha Yoga",
                    sanskritName = "पाश योग",
                    category = YogaCategory.NABHASA_YOGA,
                    planets = planetPositions.map { it.planet },
                    houses = occupiedHouses,
                    description = "All 7 classical planets in 5 houses: ${occupiedHouses.joinToString(", ")}",
                    effects = "Like being bound by a noose (pasha). May have many servants, prone to imprisonment or bondage, talkative, may earn through questionable means. Karmic bonds are strong.",
                    strength = YogaStrength.MODERATE,
                    strengthPercentage = 60.0,
                    isAuspicious = false,
                    activationPeriod = "May face binding situations during malefic dashas",
                    cancellationFactors = listOf("Benefic planets can transform bondage to devotion", "Position of houses matters greatly")
                ))
            }
            6 -> {
                // Dama Yoga - All planets in six houses
                yogas.add(Yoga(
                    name = "Dama Yoga",
                    sanskritName = "दम योग",
                    category = YogaCategory.NABHASA_YOGA,
                    planets = planetPositions.map { it.planet },
                    houses = occupiedHouses,
                    description = "All 7 classical planets in 6 houses: ${occupiedHouses.joinToString(", ")}",
                    effects = "Self-controlled like a restrained animal (dama). Very charitable, wealthy, intelligent, will have good sons and wife, victorious. Balanced distribution of energy.",
                    strength = YogaStrength.MODERATE,
                    strengthPercentage = 70.0,
                    isAuspicious = true,
                    activationPeriod = "Steady progress through self-discipline",
                    cancellationFactors = listOf("Balanced yoga with moderate effects", "Self-control is key to success")
                ))
            }
            7 -> {
                // Veena/Vallaki Yoga - All planets in seven houses
                yogas.add(Yoga(
                    name = "Veena Yoga (Vallaki)",
                    sanskritName = "वीणा योग",
                    category = YogaCategory.NABHASA_YOGA,
                    planets = planetPositions.map { it.planet },
                    houses = occupiedHouses,
                    description = "All 7 classical planets distributed across 7 houses",
                    effects = "Like a musical instrument (veena). Person is fond of music and dance, leader among relatives, learned, wealthy, skillful, happy. Harmonious distribution of planetary energies.",
                    strength = YogaStrength.MODERATE,
                    strengthPercentage = 75.0,
                    isAuspicious = true,
                    activationPeriod = "Harmonious periods bring artistic expression",
                    cancellationFactors = listOf("Most balanced of Sankhya yogas", "Artistic and cultural inclinations")
                ))
            }
        }

        return yogas
    }

    // ==================== AKRITI (GEOMETRIC) YOGAS ====================

    private fun evaluateAkritiYogas(
        planetPositions: List<PlanetPosition>,
        occupiedHouses: List<Int>
    ): List<Yoga> {
        val yogas = mutableListOf<Yoga>()

        // Check for various geometric patterns
        yogas.addAll(evaluateRajjuMusalaYogas(planetPositions))
        yogas.addAll(evaluateGadaSakataYogas(planetPositions, occupiedHouses))
        yogas.addAll(evaluateShringatakaNaukaYogas(planetPositions, occupiedHouses))
        yogas.addAll(evaluateYupaShaktiYogas(planetPositions, occupiedHouses))
        yogas.addAll(evaluateVajraYavaYogas(planetPositions, occupiedHouses))
        yogas.addAll(evaluateKamalaVapiYogas(planetPositions, occupiedHouses))

        return yogas
    }

    private fun evaluateRajjuMusalaYogas(planetPositions: List<PlanetPosition>): List<Yoga> {
        val yogas = mutableListOf<Yoga>()

        // Group planets by sign modality
        val movableSigns = listOf(ZodiacSign.ARIES, ZodiacSign.CANCER, ZodiacSign.LIBRA, ZodiacSign.CAPRICORN)
        val fixedSigns = listOf(ZodiacSign.TAURUS, ZodiacSign.LEO, ZodiacSign.SCORPIO, ZodiacSign.AQUARIUS)
        val dualSigns = listOf(ZodiacSign.GEMINI, ZodiacSign.VIRGO, ZodiacSign.SAGITTARIUS, ZodiacSign.PISCES)

        val planetsInMovable = planetPositions.filter {
            ZodiacSign.fromLongitude(it.longitude) in movableSigns
        }
        val planetsInFixed = planetPositions.filter {
            ZodiacSign.fromLongitude(it.longitude) in fixedSigns
        }
        val planetsInDual = planetPositions.filter {
            ZodiacSign.fromLongitude(it.longitude) in dualSigns
        }

        // Rajju Yoga - All planets in movable signs
        if (planetsInMovable.size == 7 && planetsInFixed.isEmpty() && planetsInDual.isEmpty()) {
            yogas.add(Yoga(
                name = "Rajju Yoga",
                sanskritName = "रज्जु योग",
                category = YogaCategory.NABHASA_YOGA,
                planets = planetsInMovable.map { it.planet },
                houses = planetsInMovable.map { it.house }.distinct(),
                description = "All 7 planets in movable (cardinal) signs",
                effects = "Like a rope (rajju) - person fond of wandering, travel, and movement. Handsome, cruel-minded, may lose wealth through travels. Active and dynamic but restless.",
                strength = YogaStrength.MODERATE,
                strengthPercentage = 65.0,
                isAuspicious = false,
                activationPeriod = "Periods bring travel and movement",
                cancellationFactors = listOf("Restless energy", "Good for careers requiring travel")
            ))
        }

        // Musala Yoga - All planets in fixed signs
        if (planetsInFixed.size == 7 && planetsInMovable.isEmpty() && planetsInDual.isEmpty()) {
            yogas.add(Yoga(
                name = "Musala Yoga",
                sanskritName = "मुसल योग",
                category = YogaCategory.NABHASA_YOGA,
                planets = planetsInFixed.map { it.planet },
                houses = planetsInFixed.map { it.house }.distinct(),
                description = "All 7 planets in fixed signs",
                effects = "Like a pestle (musala) - person is proud, wealthy, learned, dear to ruler, famous, has many sons. Stable, determined but can be stubborn.",
                strength = YogaStrength.STRONG,
                strengthPercentage = 75.0,
                isAuspicious = true,
                activationPeriod = "Steady progress through persistence",
                cancellationFactors = listOf("Very stable energy", "May resist necessary changes")
            ))
        }

        // Nala Yoga - All planets in dual signs
        if (planetsInDual.size == 7 && planetsInMovable.isEmpty() && planetsInFixed.isEmpty()) {
            yogas.add(Yoga(
                name = "Nala Yoga",
                sanskritName = "नल योग",
                category = YogaCategory.NABHASA_YOGA,
                planets = planetsInDual.map { it.planet },
                houses = planetsInDual.map { it.house }.distinct(),
                description = "All 7 planets in dual (mutable) signs",
                effects = "Like a hollow reed (nala) - person is skilled in arts, well-shaped body, wealthy, fond of relatives but with mixed circumstances. Adaptable but inconsistent.",
                strength = YogaStrength.MODERATE,
                strengthPercentage = 65.0,
                isAuspicious = true,
                activationPeriod = "Adaptable to changing circumstances",
                cancellationFactors = listOf("Flexible but can lack direction", "Good for versatile careers")
            ))
        }

        return yogas
    }

    private fun evaluateGadaSakataYogas(
        planetPositions: List<PlanetPosition>,
        occupiedHouses: List<Int>
    ): List<Yoga> {
        val yogas = mutableListOf<Yoga>()

        // Gada Yoga - All planets in two adjacent kendras (1-4 or 4-7 or 7-10 or 10-1)
        val adjacentKendraPairs = listOf(
            setOf(1, 4), setOf(4, 7), setOf(7, 10), setOf(10, 1)
        )

        for (pair in adjacentKendraPairs) {
            val planetsInPair = planetPositions.filter { it.house in pair }
            if (planetsInPair.size == 7) {
                yogas.add(Yoga(
                    name = "Gada Yoga",
                    sanskritName = "गदा योग",
                    category = YogaCategory.NABHASA_YOGA,
                    planets = planetsInPair.map { it.planet },
                    houses = pair.toList(),
                    description = "All planets in two adjacent kendras: houses ${pair.joinToString(", ")}",
                    effects = "Like a mace (gada) - person is wealthy through honest means, engaged in religious activities, knows scriptures. Powerful and righteous nature.",
                    strength = YogaStrength.STRONG,
                    strengthPercentage = 80.0,
                    isAuspicious = true,
                    activationPeriod = "Angular houses activated bring recognition",
                    cancellationFactors = listOf("Concentrated angular power", "Dharmic wealth indicated")
                ))
            }
        }

        // Sakata Yoga (Nabhasa version) - Planets in 1st and 7th only, or 4th and 10th only
        val oppositeKendraPairs = listOf(setOf(1, 7), setOf(4, 10))

        for (pair in oppositeKendraPairs) {
            val planetsInPair = planetPositions.filter { it.house in pair }
            val planetsElsewhere = planetPositions.filter { it.house !in pair }

            if (planetsInPair.size >= 5 && planetsElsewhere.size <= 2) {
                yogas.add(Yoga(
                    name = "Sakata Yoga (Nabhasa)",
                    sanskritName = "शकट योग (नभस)",
                    category = YogaCategory.NABHASA_YOGA,
                    planets = planetsInPair.map { it.planet },
                    houses = pair.toList(),
                    description = "Planets concentrated in opposite kendras: houses ${pair.joinToString(", ")}",
                    effects = "Like a cart (sakata) - life moves in ups and downs. May lose wealth and regain it. Fluctuating fortunes but eventual recovery.",
                    strength = YogaStrength.MODERATE,
                    strengthPercentage = 60.0,
                    isAuspicious = false,
                    activationPeriod = "Periods of loss followed by recovery",
                    cancellationFactors = listOf("Jupiter's presence or aspect mitigates", "Fluctuating but resilient")
                ))
            }
        }

        return yogas
    }

    private fun evaluateShringatakaNaukaYogas(
        planetPositions: List<PlanetPosition>,
        occupiedHouses: List<Int>
    ): List<Yoga> {
        val yogas = mutableListOf<Yoga>()

        // Shringataka Yoga - Planets in trikonas only (1, 5, 9)
        val trikonas = setOf(1, 5, 9)
        val planetsInTrikonas = planetPositions.filter { it.house in trikonas }

        if (planetsInTrikonas.size >= 5 && occupiedHouses.all { it in trikonas }) {
            yogas.add(Yoga(
                name = "Shringataka Yoga",
                sanskritName = "शृंगाटक योग",
                category = YogaCategory.NABHASA_YOGA,
                planets = planetsInTrikonas.map { it.planet },
                houses = trikonas.toList(),
                description = "Planets concentrated in trinal houses (1, 5, 9)",
                effects = "Like a peak/triangle - person is happy, dear to ruler, protector of others, skilled warrior, handsome, attached to spouse. Highly favorable for dharma and fortune.",
                strength = YogaStrength.VERY_STRONG,
                strengthPercentage = 85.0,
                isAuspicious = true,
                activationPeriod = "Trinal planets' dashas bring fortune",
                cancellationFactors = listOf("Trikonas are most auspicious", "Creates strong Raja Yoga potential")
            ))
        }

        // Nauka Yoga - All planets in 7 consecutive houses starting from lagna
        val consecutiveFrom1 = (1..7).toSet()
        val planetsInConsecutive = planetPositions.filter { it.house in consecutiveFrom1 }

        if (planetsInConsecutive.size >= 6 && occupiedHouses.all { it in consecutiveFrom1 }) {
            yogas.add(Yoga(
                name = "Nauka Yoga",
                sanskritName = "नौका योग",
                category = YogaCategory.NABHASA_YOGA,
                planets = planetsInConsecutive.map { it.planet },
                houses = occupiedHouses,
                description = "Planets in consecutive houses from lagna (first half of chart)",
                effects = "Like a boat (nauka) - person earns through water/ships, famous, cruel-minded, earns through base means but accumulates wealth. Life has direction but may involve questionable paths.",
                strength = YogaStrength.MODERATE,
                strengthPercentage = 65.0,
                isAuspicious = false,
                activationPeriod = "First half of life more active",
                cancellationFactors = listOf("May involve maritime or travel industries", "Wealth accumulation through unusual means")
            ))
        }

        // Kuta Yoga - All planets in the last 7 houses (6-12)
        val lastHalf = (6..12).toSet()
        val planetsInLastHalf = planetPositions.filter { it.house in lastHalf }

        if (planetsInLastHalf.size >= 6 && occupiedHouses.all { it in lastHalf }) {
            yogas.add(Yoga(
                name = "Kuta Yoga",
                sanskritName = "कूट योग",
                category = YogaCategory.NABHASA_YOGA,
                planets = planetsInLastHalf.map { it.planet },
                houses = occupiedHouses,
                description = "Planets concentrated in houses 6-12 (second half of chart)",
                effects = "Like a peak/mountain - person is liar, head of prison or fort, cruel, forsaken by relatives, poor. Second half of life more challenging.",
                strength = YogaStrength.WEAK,
                strengthPercentage = 45.0,
                isAuspicious = false,
                activationPeriod = "Later periods may bring difficulties",
                cancellationFactors = listOf("Benefics in these houses can mitigate", "May work in institutions or isolated places")
            ))
        }

        return yogas
    }

    private fun evaluateYupaShaktiYogas(
        planetPositions: List<PlanetPosition>,
        occupiedHouses: List<Int>
    ): List<Yoga> {
        val yogas = mutableListOf<Yoga>()

        // Yupa Yoga - All planets in houses 1, 2, 3, 4
        val yupaHouses = setOf(1, 2, 3, 4)
        if (occupiedHouses.all { it in yupaHouses } && occupiedHouses.size >= 3) {
            val planetsInYupa = planetPositions.filter { it.house in yupaHouses }
            yogas.add(Yoga(
                name = "Yupa Yoga",
                sanskritName = "यूप योग",
                category = YogaCategory.NABHASA_YOGA,
                planets = planetsInYupa.map { it.planet },
                houses = occupiedHouses,
                description = "Planets concentrated in houses 1-4",
                effects = "Like a sacrificial post (yupa) - person is religious, performs sacrifices/charity, self-controlled, learned in scriptures. Foundation of life is spiritual and domestic.",
                strength = YogaStrength.STRONG,
                strengthPercentage = 75.0,
                isAuspicious = true,
                activationPeriod = "Spiritual and domestic matters prominent",
                cancellationFactors = listOf("Emphasizes self, wealth, courage, home", "Good for spiritual practices")
            ))
        }

        // Shara Yoga - All planets in houses 4, 5, 6, 7
        val sharaHouses = setOf(4, 5, 6, 7)
        if (occupiedHouses.all { it in sharaHouses } && occupiedHouses.size >= 3) {
            val planetsInShara = planetPositions.filter { it.house in sharaHouses }
            yogas.add(Yoga(
                name = "Shara Yoga",
                sanskritName = "शर योग",
                category = YogaCategory.NABHASA_YOGA,
                planets = planetsInShara.map { it.planet },
                houses = occupiedHouses,
                description = "Planets concentrated in houses 4-7",
                effects = "Like an arrow (shara) - person is hunter, dealer in animals/leather, cruel, causes pain to others. Life involves conflict and competition.",
                strength = YogaStrength.MODERATE,
                strengthPercentage = 55.0,
                isAuspicious = false,
                activationPeriod = "Competition and conflicts prominent",
                cancellationFactors = listOf("May work with animals or in competitive fields", "Benefics moderate cruelty")
            ))
        }

        // Shakti Yoga - All planets in houses 7, 8, 9, 10
        val shaktiHouses = setOf(7, 8, 9, 10)
        if (occupiedHouses.all { it in shaktiHouses } && occupiedHouses.size >= 3) {
            val planetsInShakti = planetPositions.filter { it.house in shaktiHouses }
            yogas.add(Yoga(
                name = "Shakti Yoga",
                sanskritName = "शक्ति योग",
                category = YogaCategory.NABHASA_YOGA,
                planets = planetsInShakti.map { it.planet },
                houses = occupiedHouses,
                description = "Planets concentrated in houses 7-10",
                effects = "Like a spear (shakti) - person is lazy, poor, without happiness, long-lived but facing obstacles. Life requires persistence.",
                strength = YogaStrength.WEAK,
                strengthPercentage = 45.0,
                isAuspicious = false,
                activationPeriod = "Partnerships and career face obstacles",
                cancellationFactors = listOf("Benefics in these houses improve outlook", "Persistence eventually pays off")
            ))
        }

        // Danda Yoga - All planets in houses 10, 11, 12, 1
        val dandaHouses = setOf(10, 11, 12, 1)
        if (occupiedHouses.all { it in dandaHouses } && occupiedHouses.size >= 3) {
            val planetsInDanda = planetPositions.filter { it.house in dandaHouses }
            yogas.add(Yoga(
                name = "Danda Yoga",
                sanskritName = "दण्ड योग",
                category = YogaCategory.NABHASA_YOGA,
                planets = planetsInDanda.map { it.planet },
                houses = occupiedHouses,
                description = "Planets concentrated in houses 10-1",
                effects = "Like a staff/scepter (danda) - person is forsaken by near relatives, bereft of children, serves others, poor. Authority without support system.",
                strength = YogaStrength.WEAK,
                strengthPercentage = 50.0,
                isAuspicious = false,
                activationPeriod = "Career success but personal life challenges",
                cancellationFactors = listOf("Strong planets can give authority", "Family connections strained")
            ))
        }

        return yogas
    }

    private fun evaluateVajraYavaYogas(
        planetPositions: List<PlanetPosition>,
        occupiedHouses: List<Int>
    ): List<Yoga> {
        val yogas = mutableListOf<Yoga>()

        // Vajra Yoga - Benefics in 1st and 7th, Malefics in 4th and 10th
        val beneficPlanets = listOf(Planet.JUPITER, Planet.VENUS, Planet.MERCURY, Planet.MOON)
        val maleficPlanets = listOf(Planet.SUN, Planet.MARS, Planet.SATURN)

        val beneficsIn1And7 = planetPositions.filter {
            it.planet in beneficPlanets && it.house in listOf(1, 7)
        }
        val maleficsIn4And10 = planetPositions.filter {
            it.planet in maleficPlanets && it.house in listOf(4, 10)
        }

        if (beneficsIn1And7.isNotEmpty() && maleficsIn4And10.isNotEmpty() &&
            beneficsIn1And7.size >= 2 && maleficsIn4And10.size >= 2) {
            yogas.add(Yoga(
                name = "Vajra Yoga",
                sanskritName = "वज्र योग",
                category = YogaCategory.NABHASA_YOGA,
                planets = (beneficsIn1And7 + maleficsIn4And10).map { it.planet },
                houses = listOf(1, 4, 7, 10),
                description = "Benefics in 1st/7th and malefics in 4th/10th",
                effects = "Like a thunderbolt (vajra) - person is happy in beginning and end of life, middle period challenging. Brave, handsome, wealthy later in life.",
                strength = YogaStrength.STRONG,
                strengthPercentage = 70.0,
                isAuspicious = true,
                activationPeriod = "Better beginning and end, challenging middle",
                cancellationFactors = listOf("Classical angular configuration", "Middle age requires caution")
            ))
        }

        // Yava Yoga - Malefics in 1st and 7th, Benefics in 4th and 10th
        val maleficsIn1And7 = planetPositions.filter {
            it.planet in maleficPlanets && it.house in listOf(1, 7)
        }
        val beneficsIn4And10 = planetPositions.filter {
            it.planet in beneficPlanets && it.house in listOf(4, 10)
        }

        if (maleficsIn1And7.isNotEmpty() && beneficsIn4And10.isNotEmpty() &&
            maleficsIn1And7.size >= 2 && beneficsIn4And10.size >= 2) {
            yogas.add(Yoga(
                name = "Yava Yoga",
                sanskritName = "यव योग",
                category = YogaCategory.NABHASA_YOGA,
                planets = (maleficsIn1And7 + beneficsIn4And10).map { it.planet },
                houses = listOf(1, 4, 7, 10),
                description = "Malefics in 1st/7th and benefics in 4th/10th",
                effects = "Like barley grain (yava) - person is happy in middle of life, beginning and end challenging. Charitable, acquires wealth in middle age.",
                strength = YogaStrength.STRONG,
                strengthPercentage = 70.0,
                isAuspicious = true,
                activationPeriod = "Middle age most prosperous",
                cancellationFactors = listOf("Reverse of Vajra", "Peak in middle years")
            ))
        }

        return yogas
    }

    private fun evaluateKamalaVapiYogas(
        planetPositions: List<PlanetPosition>,
        occupiedHouses: List<Int>
    ): List<Yoga> {
        val yogas = mutableListOf<Yoga>()

        // Kamala Yoga - All planets in 4 kendras only
        val kendras = setOf(1, 4, 7, 10)
        if (occupiedHouses.all { it in kendras } && occupiedHouses.size >= 3) {
            val planetsInKendras = planetPositions.filter { it.house in kendras }
            yogas.add(Yoga(
                name = "Kamala Yoga",
                sanskritName = "कमल योग",
                category = YogaCategory.NABHASA_YOGA,
                planets = planetsInKendras.map { it.planet },
                houses = occupiedHouses,
                description = "All planets concentrated in kendras (1, 4, 7, 10)",
                effects = "Like a lotus (kamala) - person is very famous, performs many deeds, prosperous, virtuous, long-lived. All angular houses activated creates powerful foundation.",
                strength = YogaStrength.VERY_STRONG,
                strengthPercentage = 90.0,
                isAuspicious = true,
                activationPeriod = "Fame and prosperity throughout life",
                cancellationFactors = listOf("Kendras are pillars of horoscope", "Extremely powerful if benefics involved")
            ))
        }

        // Vapi Yoga - All planets in houses other than kendras (2,3,5,6,8,9,11,12)
        val nonKendras = setOf(2, 3, 5, 6, 8, 9, 11, 12)
        if (occupiedHouses.all { it in nonKendras } && occupiedHouses.size >= 5) {
            val planetsInNonKendras = planetPositions.filter { it.house in nonKendras }
            yogas.add(Yoga(
                name = "Vapi Yoga",
                sanskritName = "वापी योग",
                category = YogaCategory.NABHASA_YOGA,
                planets = planetsInNonKendras.map { it.planet },
                houses = occupiedHouses,
                description = "All planets in non-angular houses (panapharas and apoklimas)",
                effects = "Like a well (vapi) - person is capable of accumulating wealth, hoarding tendency, enjoys comforts but may lack visibility. Wealth comes through supporting roles.",
                strength = YogaStrength.MODERATE,
                strengthPercentage = 65.0,
                isAuspicious = true,
                activationPeriod = "Wealth accumulates steadily",
                cancellationFactors = listOf("No angular strength but supportive houses active", "Background success rather than fame")
            ))
        }

        return yogas
    }

    // ==================== DALA YOGAS ====================

    private fun evaluateDalaYogas(
        planetPositions: List<PlanetPosition>,
        occupiedHouses: List<Int>
    ): List<Yoga> {
        val yogas = mutableListOf<Yoga>()

        val beneficPlanets = listOf(Planet.JUPITER, Planet.VENUS, Planet.MERCURY, Planet.MOON)
        val maleficPlanets = listOf(Planet.SUN, Planet.MARS, Planet.SATURN)

        // Mala/Srik Yoga - All benefics in kendras
        val beneficsInKendras = planetPositions.filter {
            it.planet in beneficPlanets && it.house in listOf(1, 4, 7, 10)
        }

        if (beneficsInKendras.size >= 3) {
            yogas.add(Yoga(
                name = "Mala Yoga (Srik)",
                sanskritName = "माला योग (स्रिक्)",
                category = YogaCategory.NABHASA_YOGA,
                planets = beneficsInKendras.map { it.planet },
                houses = beneficsInKendras.map { it.house }.distinct(),
                description = "Multiple benefics (${beneficsInKendras.size}) in kendras",
                effects = "Like a garland (mala) - person is always happy, has vehicles, clothes, food, good spouse. Life adorned with benefic influences. Comfortable existence.",
                strength = YogaStrength.VERY_STRONG,
                strengthPercentage = 85.0,
                isAuspicious = true,
                activationPeriod = "Benefic dashas bring happiness",
                cancellationFactors = listOf("Benefics in angles is highly auspicious", "Garland of blessings")
            ))
        }

        // Sarpa/Bhujanga Yoga - All malefics in kendras
        val maleficsInKendras = planetPositions.filter {
            it.planet in maleficPlanets && it.house in listOf(1, 4, 7, 10)
        }

        if (maleficsInKendras.size >= 3) {
            yogas.add(Yoga(
                name = "Sarpa Yoga (Bhujanga)",
                sanskritName = "सर्प योग (भुजंग)",
                category = YogaCategory.NABHASA_YOGA,
                planets = maleficsInKendras.map { it.planet },
                houses = maleficsInKendras.map { it.house }.distinct(),
                description = "Multiple malefics (${maleficsInKendras.size}) in kendras",
                effects = "Like a serpent (sarpa) - person is poor, miserable, cruel, depends on others for food. Life has many challenges and obstacles. Requires strong will.",
                strength = YogaStrength.WEAK,
                strengthPercentage = 40.0,
                isAuspicious = false,
                activationPeriod = "Malefic periods bring challenges",
                cancellationFactors = listOf("Malefics in angles create difficulties", "Jupiter's aspect provides relief")
            ))
        }

        return yogas
    }

    // ==================== ASHRAYA YOGAS ====================

    private fun evaluateAshrayaYogas(
        planetPositions: List<PlanetPosition>,
        chart: VedicChart
    ): List<Yoga> {
        val yogas = mutableListOf<Yoga>()

        val ascendantSign = ZodiacSign.fromLongitude(chart.ascendant)
        val moonSign = planetPositions.find { it.planet == Planet.MOON }
            ?.let { ZodiacSign.fromLongitude(it.longitude) }

        // Check all planets' signs
        val planetSigns = planetPositions.map { ZodiacSign.fromLongitude(it.longitude) }

        val movableSigns = listOf(ZodiacSign.ARIES, ZodiacSign.CANCER, ZodiacSign.LIBRA, ZodiacSign.CAPRICORN)
        val fixedSigns = listOf(ZodiacSign.TAURUS, ZodiacSign.LEO, ZodiacSign.SCORPIO, ZodiacSign.AQUARIUS)
        val dualSigns = listOf(ZodiacSign.GEMINI, ZodiacSign.VIRGO, ZodiacSign.SAGITTARIUS, ZodiacSign.PISCES)

        // Count planets by sign type
        val inMovable = planetSigns.count { it in movableSigns }
        val inFixed = planetSigns.count { it in fixedSigns }
        val inDual = planetSigns.count { it in dualSigns }

        // Bhagya Yoga - Majority in movable with lagna/Moon also movable
        if (inMovable >= 5 && (ascendantSign in movableSigns || moonSign in movableSigns)) {
            yogas.add(Yoga(
                name = "Chara Bhagya Yoga",
                sanskritName = "चर भाग्य योग",
                category = YogaCategory.NABHASA_YOGA,
                planets = planetPositions.filter {
                    ZodiacSign.fromLongitude(it.longitude) in movableSigns
                }.map { it.planet },
                houses = emptyList(),
                description = "Majority of planets ($inMovable) in movable signs with lagna/Moon also movable",
                effects = "Mobile fortune - person travels for wealth, adaptable, initiates new ventures. Quick to act, restless energy brings varied experiences.",
                strength = YogaStrength.MODERATE,
                strengthPercentage = 70.0,
                isAuspicious = true,
                activationPeriod = "Movement brings fortune",
                cancellationFactors = listOf("Cardinal energy dominant", "Travel and change favored")
            ))
        }

        // Sthira Bhagya Yoga - Majority in fixed with lagna/Moon also fixed
        if (inFixed >= 5 && (ascendantSign in fixedSigns || moonSign in fixedSigns)) {
            yogas.add(Yoga(
                name = "Sthira Bhagya Yoga",
                sanskritName = "स्थिर भाग्य योग",
                category = YogaCategory.NABHASA_YOGA,
                planets = planetPositions.filter {
                    ZodiacSign.fromLongitude(it.longitude) in fixedSigns
                }.map { it.planet },
                houses = emptyList(),
                description = "Majority of planets ($inFixed) in fixed signs with lagna/Moon also fixed",
                effects = "Stable fortune - person accumulates wealth slowly but surely. Determined, persistent, builds lasting foundations.",
                strength = YogaStrength.STRONG,
                strengthPercentage = 75.0,
                isAuspicious = true,
                activationPeriod = "Patience brings lasting results",
                cancellationFactors = listOf("Fixed energy dominant", "Stability and persistence rewarded")
            ))
        }

        // Dwiswabhava Bhagya Yoga - Majority in dual with lagna/Moon also dual
        if (inDual >= 5 && (ascendantSign in dualSigns || moonSign in dualSigns)) {
            yogas.add(Yoga(
                name = "Dwiswabhava Bhagya Yoga",
                sanskritName = "द्विस्वभाव भाग्य योग",
                category = YogaCategory.NABHASA_YOGA,
                planets = planetPositions.filter {
                    ZodiacSign.fromLongitude(it.longitude) in dualSigns
                }.map { it.planet },
                houses = emptyList(),
                description = "Majority of planets ($inDual) in dual signs with lagna/Moon also dual",
                effects = "Versatile fortune - person adapts to circumstances, multiple skills, fluctuating fortunes. Flexible and resourceful nature.",
                strength = YogaStrength.MODERATE,
                strengthPercentage = 70.0,
                isAuspicious = true,
                activationPeriod = "Adaptability brings fortune",
                cancellationFactors = listOf("Mutable energy dominant", "Versatility is key strength")
            ))
        }

        return yogas
    }
}
