package com.astro.storm.ephemeris.yoga.evaluators

import com.astro.storm.core.model.Planet
import com.astro.storm.core.model.PlanetPosition
import com.astro.storm.core.model.VedicChart
import com.astro.storm.core.model.ZodiacSign
import com.astro.storm.ephemeris.yoga.Yoga
import com.astro.storm.ephemeris.yoga.YogaCategory
import com.astro.storm.ephemeris.yoga.YogaEvaluator
import com.astro.storm.ephemeris.yoga.YogaHelpers
import com.astro.storm.ephemeris.yoga.YogaStrength

/**
 * Rahu-Ketu (Nodal) Yoga Evaluator
 *
 * Evaluates all Rahu-Ketu based yogas including the 12 types of Kala Sarpa Yoga,
 * nodal house placements, and nodal conjunctions.
 *
 * The lunar nodes (Rahu and Ketu) are shadow planets that create powerful karmic
 * patterns when all planets are hemmed between them.
 *
 * Yogas Evaluated:
 * - 12 Types of Kala Sarpa Yoga (Ananta, Kulik, Vasuki, etc.)
 * - Kala Amrita Yoga (reverse Kala Sarpa)
 * - Partial Kala Sarpa Yoga
 * - Graha Malika involving Rahu-Ketu
 * - Rahu/Ketu house placement yogas
 * - Rahu-Ketu conjunction yogas with planets
 * - Guru Chandala Yoga (Jupiter-Rahu)
 * - Shukra Chandala Yoga (Venus-Rahu)
 * - Chandra Grahan Yoga (Moon-Rahu/Ketu)
 * - Pitra Dosha (Sun-Rahu/Ketu)
 * - Angarak Yoga (Mars-Rahu)
 *
 * Based on:
 * - Brihat Parashara Hora Shastra (BPHS)
 * - Phaladeepika
 * - Jataka Parijata
 * - Lal Kitab
 * - Contemporary Jyotish traditions
 *
 * @author AstroStorm
 */
class RahuKetuYogaEvaluator : YogaEvaluator {

    override val category: YogaCategory = YogaCategory.NEGATIVE_YOGA

    companion object {
        private val KENDRA_HOUSES = setOf(1, 4, 7, 10)
        private val TRIKONA_HOUSES = setOf(1, 5, 9)
        private val DUSTHANA_HOUSES = setOf(6, 8, 12)
        private val UPACHAYA_HOUSES = setOf(3, 6, 10, 11)

        /**
         * The 12 types of Kala Sarpa Yoga based on Rahu's house position
         * Each type has specific characteristics and effects
         */
        private val KALA_SARPA_TYPES = mapOf(
            1 to KalaSarpaType(
                name = "Ananta Kala Sarpa Yoga",
                sanskritName = "अनंत काल सर्प योग",
                deity = "Ananta (Shesha Naga)",
                effects = "Struggles in early life, self-made success later, strong willpower, " +
                        "health challenges, eventual rise through personal effort, leadership qualities emerge"
            ),
            2 to KalaSarpaType(
                name = "Kulik Kala Sarpa Yoga",
                sanskritName = "कुलिक काल सर्प योग",
                deity = "Kulik",
                effects = "Financial fluctuations, speech-related issues, family tensions, " +
                        "eventual wealth accumulation, truth-speaking becomes powerful, eye problems possible"
            ),
            3 to KalaSarpaType(
                name = "Vasuki Kala Sarpa Yoga",
                sanskritName = "वासुकि काल सर्प योग",
                deity = "Vasuki",
                effects = "Sibling relationships complex, courage tested, communication challenges, " +
                        "eventual success through bold initiatives, artistic talents, media success possible"
            ),
            4 to KalaSarpaType(
                name = "Shankhapala Kala Sarpa Yoga",
                sanskritName = "शंखपाल काल सर्प योग",
                deity = "Shankhapala",
                effects = "Property and vehicle issues, mother's health concerns, domestic unrest, " +
                        "eventual peace through spiritual practice, education challenges early"
            ),
            5 to KalaSarpaType(
                name = "Padma Kala Sarpa Yoga",
                sanskritName = "पद्म काल सर्प योग",
                deity = "Padma",
                effects = "Children-related concerns, speculation losses, creative blocks, " +
                        "romantic disappointments, eventual success in education and creative fields"
            ),
            6 to KalaSarpaType(
                name = "Maha Padma Kala Sarpa Yoga",
                sanskritName = "महापद्म काल सर्प योग",
                deity = "Maha Padma",
                effects = "Health issues, enemies and legal troubles, service difficulties, " +
                        "eventual victory over adversaries, healing abilities develop"
            ),
            7 to KalaSarpaType(
                name = "Takshak Kala Sarpa Yoga",
                sanskritName = "तक्षक काल सर्प योग",
                deity = "Takshak",
                effects = "Marriage delays and troubles, partnership conflicts, " +
                        "spouse's health concerns, eventual mature relationship, business challenges"
            ),
            8 to KalaSarpaType(
                name = "Karkotak Kala Sarpa Yoga",
                sanskritName = "कर्कोटक काल सर्प योग",
                deity = "Karkotak",
                effects = "Sudden life changes, inheritance issues, chronic ailments, " +
                        "research aptitude, eventual transformation and spiritual growth, occult interests"
            ),
            9 to KalaSarpaType(
                name = "Shankhachur Kala Sarpa Yoga",
                sanskritName = "शंखचूड़ काल सर्प योग",
                deity = "Shankhachur",
                effects = "Father's troubles, fortune delayed, religious confusion, " +
                        "higher education obstacles, eventual wisdom and philosophical depth"
            ),
            10 to KalaSarpaType(
                name = "Ghatak Kala Sarpa Yoga",
                sanskritName = "घातक काल सर्प योग",
                deity = "Ghatak",
                effects = "Career obstacles, authority conflicts, professional setbacks, " +
                        "eventual rise to prominence, political acumen develops"
            ),
            11 to KalaSarpaType(
                name = "Vishdhar Kala Sarpa Yoga",
                sanskritName = "विषधर काल सर्प योग",
                deity = "Vishdhar",
                effects = "Gains through unconventional means, elder sibling issues, " +
                        "fulfilled desires after struggle, network eventually expands"
            ),
            12 to KalaSarpaType(
                name = "Sheshnag Kala Sarpa Yoga",
                sanskritName = "शेषनाग काल सर्प योग",
                deity = "Sheshnag",
                effects = "Expenditure exceeds income, foreign residence, " +
                        "sleep disorders, eventual spiritual liberation, moksha yoga"
            )
        )
    }

    override fun evaluate(chart: VedicChart): List<Yoga> {
        val yogas = mutableListOf<Yoga>()
        val ascendantSign = ZodiacSign.fromLongitude(chart.ascendant)
        val houseLords = YogaHelpers.getHouseLords(ascendantSign)

        val rahuPos = chart.planetPositions.find { it.planet == Planet.RAHU } ?: return emptyList()
        val ketuPos = chart.planetPositions.find { it.planet == Planet.KETU } ?: return emptyList()

        // Kala Sarpa / Kala Amrita Yoga
        evaluateKalaSarpaYoga(chart, rahuPos, ketuPos)?.let { yogas.add(it) }
        evaluatePartialKalaSarpa(chart, rahuPos, ketuPos)?.let { yogas.add(it) }

        // Rahu-planet conjunction yogas
        evaluateGuruChandalaYoga(chart, rahuPos)?.let { yogas.add(it) }
        evaluateAngaarakYoga(chart, rahuPos)?.let { yogas.add(it) }
        evaluateShukraChandalaYoga(chart, rahuPos)?.let { yogas.add(it) }
        evaluateChandraGrahanYoga(chart, rahuPos, ketuPos)?.let { yogas.addAll(it) }
        evaluatePitraDosha(chart, rahuPos, ketuPos)?.let { yogas.add(it) }
        evaluateBudhiNashYoga(chart, rahuPos)?.let { yogas.add(it) }

        // Rahu-Ketu axis yogas
        evaluateRahuKetuAxisYogas(chart, rahuPos, ketuPos, houseLords)?.let { yogas.addAll(it) }

        // House-specific nodal yogas
        evaluateRahuHouseYogas(chart, rahuPos, houseLords)?.let { yogas.addAll(it) }
        evaluateKetuHouseYogas(chart, ketuPos, houseLords)?.let { yogas.addAll(it) }

        // Benefic nodal yogas
        evaluateBeneficNodalYogas(chart, rahuPos, ketuPos, houseLords)?.let { yogas.addAll(it) }

        return yogas
    }

    /**
     * Evaluate Kala Sarpa Yoga - All planets between Rahu and Ketu
     * Also evaluates Kala Amrita Yoga (reverse direction)
     */
    private fun evaluateKalaSarpaYoga(
        chart: VedicChart,
        rahuPos: PlanetPosition,
        ketuPos: PlanetPosition
    ): Yoga? {
        // Get all planets except Rahu and Ketu
        val planets = chart.planetPositions.filter { 
            it.planet !in listOf(Planet.RAHU, Planet.KETU) 
        }

        val rahuLong = rahuPos.longitude
        val ketuLong = ketuPos.longitude

        // Check if all planets are between Rahu and Ketu (going forward)
        // Or between Ketu and Rahu (going backward - Kala Amrita)
        
        var allBetweenRahuToKetu = true
        var allBetweenKetuToRahu = true

        planets.forEach { planet ->
            val pLong = planet.longitude

            // Check Rahu to Ketu direction
            val betweenRahuToKetu = if (rahuLong < ketuLong) {
                pLong > rahuLong && pLong < ketuLong
            } else {
                pLong > rahuLong || pLong < ketuLong
            }

            // Check Ketu to Rahu direction
            val betweenKetuToRahu = if (ketuLong < rahuLong) {
                pLong > ketuLong && pLong < rahuLong
            } else {
                pLong > ketuLong || pLong < rahuLong
            }

            if (!betweenRahuToKetu) allBetweenRahuToKetu = false
            if (!betweenKetuToRahu) allBetweenKetuToRahu = false
        }

        // If neither direction has all planets, no Kala Sarpa
        if (!allBetweenRahuToKetu && !allBetweenKetuToRahu) return null

        val isKalaAmrita = allBetweenKetuToRahu && !allBetweenRahuToKetu

        // Determine the type based on Rahu's house position
        val kalaSarpaType = KALA_SARPA_TYPES[rahuPos.house]

        // Calculate strength based on conditions
        var strengthPct = 70.0

        // Check for cancellation factors
        val cancellations = mutableListOf<String>()

        // Cancellation 1: Any planet conjunct Rahu or Ketu closely
        val planetWithRahu = planets.any { YogaHelpers.areConjunct(it, rahuPos, 5.0) }
        val planetWithKetu = planets.any { YogaHelpers.areConjunct(it, ketuPos, 5.0) }
        if (planetWithRahu || planetWithKetu) {
            strengthPct *= 0.8
            cancellations.add("Planet closely conjunct node - partial cancellation")
        }

        // Cancellation 2: Jupiter aspects Rahu or Ketu
        val jupiterPos = chart.planetPositions.find { it.planet == Planet.JUPITER }
        if (jupiterPos != null) {
            if (YogaHelpers.isAspecting(jupiterPos, rahuPos) || YogaHelpers.isAspecting(jupiterPos, ketuPos)) {
                strengthPct *= 0.85
                cancellations.add("Jupiter's aspect provides relief")
            }
        }

        // Cancellation 3: Rahu or Ketu in own sign or exaltation
        val rahuInGoodSign = rahuPos.sign in listOf(ZodiacSign.GEMINI, ZodiacSign.VIRGO, ZodiacSign.TAURUS)
        val ketuInGoodSign = ketuPos.sign in listOf(ZodiacSign.SAGITTARIUS, ZodiacSign.PISCES, ZodiacSign.SCORPIO)
        if (rahuInGoodSign || ketuInGoodSign) {
            strengthPct *= 0.9
            cancellations.add("Node in favorable sign - effects reduced")
        }

        val name = if (isKalaAmrita) {
            "Kala Amrita Yoga"
        } else {
            kalaSarpaType?.name ?: "Kala Sarpa Yoga"
        }

        val sanskritName = if (isKalaAmrita) {
            "काल अमृत योग"
        } else {
            kalaSarpaType?.sanskritName ?: "काल सर्प योग"
        }

        val baseEffects = if (isKalaAmrita) {
            "Reverse Kala Sarpa - karmic rewards after struggle, past-life merit activates, " +
                    "spiritual progress, eventual success in worldly matters"
        } else {
            kalaSarpaType?.effects ?: "Karmic challenges, delayed success, spiritual lessons, eventual transformation"
        }

        val description = buildString {
            append("All planets between ")
            if (isKalaAmrita) {
                append("Ketu (${ketuPos.sign.displayName}) and Rahu (${rahuPos.sign.displayName})")
            } else {
                append("Rahu (${rahuPos.sign.displayName}) and Ketu (${ketuPos.sign.displayName})")
            }
            append(". Type: ${kalaSarpaType?.deity ?: "General"}")
        }

        val activationPeriod = "Rahu and Ketu Dasha periods intensify effects. " +
                "After age 42 (Rahu maturity) and 48 (Ketu maturity), effects reduce"

        return Yoga(
            name = name,
            sanskritName = sanskritName,
            category = if (isKalaAmrita) YogaCategory.SPECIAL_YOGA else YogaCategory.NEGATIVE_YOGA,
            planets = listOf(Planet.RAHU, Planet.KETU),
            houses = listOf(rahuPos.house, ketuPos.house),
            description = description,
            effects = baseEffects,
            strength = YogaHelpers.strengthFromPercentage(strengthPct),
            strengthPercentage = strengthPct.coerceIn(10.0, 100.0),
            isAuspicious = isKalaAmrita,
            activationPeriod = activationPeriod,
            cancellationFactors = cancellations.ifEmpty { 
                listOf(
                    "Regular Rahu-Ketu remedies help",
                    "Sarpa Dosha Nivaran Puja",
                    "Naag Panchami observance"
                )
            }
        )
    }

    /**
     * Partial Kala Sarpa Yoga - Most planets between nodes
     */
    private fun evaluatePartialKalaSarpa(
        chart: VedicChart,
        rahuPos: PlanetPosition,
        ketuPos: PlanetPosition
    ): Yoga? {
        val planets = chart.planetPositions.filter { 
            it.planet !in listOf(Planet.RAHU, Planet.KETU) 
        }

        val rahuLong = rahuPos.longitude
        val ketuLong = ketuPos.longitude

        var countBetweenRahuToKetu = 0
        var countBetweenKetuToRahu = 0

        planets.forEach { planet ->
            val pLong = planet.longitude

            val betweenRahuToKetu = if (rahuLong < ketuLong) {
                pLong > rahuLong && pLong < ketuLong
            } else {
                pLong > rahuLong || pLong < ketuLong
            }

            val betweenKetuToRahu = if (ketuLong < rahuLong) {
                pLong > ketuLong && pLong < rahuLong
            } else {
                pLong > ketuLong || pLong < rahuLong
            }

            if (betweenRahuToKetu) countBetweenRahuToKetu++
            if (betweenKetuToRahu) countBetweenKetuToRahu++
        }

        val totalPlanets = planets.size
        val maxInOneDirection = maxOf(countBetweenRahuToKetu, countBetweenKetuToRahu)

        // Partial Kala Sarpa if 5-6 planets (out of 7) are on one side
        // Full Kala Sarpa would have all 7
        if (maxInOneDirection < 5 || maxInOneDirection == totalPlanets) return null

        val outsideCount = totalPlanets - maxInOneDirection
        val strengthPct = 40.0 + (maxInOneDirection - 5) * 15

        return Yoga(
            name = "Partial Kala Sarpa Yoga",
            sanskritName = "अंशिक काल सर्प योग",
            category = YogaCategory.NEGATIVE_YOGA,
            planets = listOf(Planet.RAHU, Planet.KETU),
            houses = listOf(rahuPos.house, ketuPos.house),
            description = "$maxInOneDirection of $totalPlanets planets between nodes, $outsideCount outside",
            effects = "Milder form of Kala Sarpa, intermittent challenges, " +
                    "some life areas affected by karmic patterns, " +
                    "easier to overcome than full Kala Sarpa",
            strength = YogaHelpers.strengthFromPercentage(strengthPct),
            strengthPercentage = strengthPct,
            isAuspicious = false,
            activationPeriod = "Rahu-Ketu Dasha periods, effects less intense than full yoga",
            cancellationFactors = listOf(
                "Planet(s) outside the axis break the full yoga",
                "Easier remediation possible"
            )
        )
    }

    /**
     * Guru Chandala Yoga - Jupiter conjunct Rahu
     * Disruption of wisdom and dharma
     */
    private fun evaluateGuruChandalaYoga(
        chart: VedicChart,
        rahuPos: PlanetPosition
    ): Yoga? {
        val jupiterPos = chart.planetPositions.find { it.planet == Planet.JUPITER } ?: return null

        // Check if Jupiter and Rahu are conjunct
        if (!YogaHelpers.areConjunct(jupiterPos, rahuPos)) return null

        val positions = listOf(jupiterPos, rahuPos)
        val strengthPct = YogaHelpers.calculateYogaStrength(chart, positions)

        // Check for mitigations
        val cancellations = mutableListOf<String>()

        // Jupiter in own sign or exalted reduces negativity
        if (YogaHelpers.isExalted(jupiterPos) || YogaHelpers.isInOwnSign(jupiterPos)) {
            cancellations.add("Jupiter dignified - effects significantly reduced")
        }

        // In Kendra/Trikona, can give worldly success
        if (jupiterPos.house in KENDRA_HOUSES || jupiterPos.house in TRIKONA_HOUSES) {
            cancellations.add("Placement in auspicious house - can give material gains")
        }

        val effects = when (jupiterPos.house) {
            1 -> "Unconventional beliefs, questioning of tradition, foreign influence on personality"
            2 -> "Wealth through unconventional means, speech issues, family beliefs disrupted"
            4 -> "Disrupted education, foreign residence, mother's beliefs different"
            5 -> "Unconventional children, speculative tendencies, unique creative expression"
            7 -> "Foreign spouse, unconventional marriage, partnership with foreigners"
            9 -> "Questions father's beliefs, foreign teacher, unconventional spirituality"
            10 -> "Unconventional career, foreign profession, breaks from tradition at work"
            11 -> "Gains through foreign sources, unconventional friends, fulfilled desires abroad"
            else -> "Unconventional approach to Jupiter's significations, foreign connections"
        }

        return Yoga(
            name = "Guru Chandala Yoga",
            sanskritName = "गुरु चांडाल योग",
            category = YogaCategory.NEGATIVE_YOGA,
            planets = listOf(Planet.JUPITER, Planet.RAHU),
            houses = listOf(jupiterPos.house),
            description = "Jupiter conjunct Rahu in ${ordinal(jupiterPos.house)} house (${jupiterPos.sign.displayName})",
            effects = effects,
            strength = YogaHelpers.strengthFromPercentage(strengthPct),
            strengthPercentage = strengthPct,
            isAuspicious = false,
            activationPeriod = "Jupiter Dasha - Rahu Antardasha or Rahu Dasha - Jupiter Antardasha",
            cancellationFactors = cancellations.ifEmpty { 
                listOf(
                    "Worship of Lord Vishnu",
                    "Jupiter mantra recitation",
                    "Respect for teachers and elders"
                )
            }
        )
    }

    /**
     * Angaarak Yoga - Mars conjunct Rahu
     * Explosive energy and aggression
     */
    private fun evaluateAngaarakYoga(
        chart: VedicChart,
        rahuPos: PlanetPosition
    ): Yoga? {
        val marsPos = chart.planetPositions.find { it.planet == Planet.MARS } ?: return null

        if (!YogaHelpers.areConjunct(marsPos, rahuPos)) return null

        val positions = listOf(marsPos, rahuPos)
        val strengthPct = YogaHelpers.calculateYogaStrength(chart, positions)

        val cancellations = mutableListOf<String>()

        // Mars in own sign or exalted handles Rahu better
        if (YogaHelpers.isExalted(marsPos) || YogaHelpers.isInOwnSign(marsPos)) {
            cancellations.add("Mars dignified - aggression channeled constructively")
        }

        // Jupiter's aspect provides control
        val jupiterPos = chart.planetPositions.find { it.planet == Planet.JUPITER }
        if (jupiterPos != null && YogaHelpers.isAspecting(jupiterPos, marsPos)) {
            cancellations.add("Jupiter's aspect provides wisdom and restraint")
        }

        val effects = when (marsPos.house) {
            1 -> "Explosive personality, accident-prone, extreme courage, sudden actions"
            2 -> "Aggressive speech, family conflicts, sudden financial changes"
            3 -> "Extreme courage, sibling rivalry, dangerous pursuits"
            4 -> "Property disputes, domestic violence potential, mother's conflicts"
            5 -> "Risky speculation, children concerns, romantic aggression"
            6 -> "Victory through aggression, health inflammations, enemy creation"
            7 -> "Marital conflicts, aggressive spouse, partnership disputes"
            8 -> "Accident potential, sudden transformations, research into occult"
            9 -> "Father conflicts, religious extremism, foreign dangerous travel"
            10 -> "Career through force, authority conflicts, sudden career changes"
            11 -> "Aggressive networking, gains through force, conflicted friendships"
            12 -> "Hidden enemies, foreign conflicts, expenses through aggression"
            else -> "Intensified Mars energy through Rahu's amplification"
        }

        return Yoga(
            name = "Angarak Yoga",
            sanskritName = "अंगारक योग",
            category = YogaCategory.NEGATIVE_YOGA,
            planets = listOf(Planet.MARS, Planet.RAHU),
            houses = listOf(marsPos.house),
            description = "Mars conjunct Rahu in ${ordinal(marsPos.house)} house (${marsPos.sign.displayName})",
            effects = effects + ". Requires conscious effort to control aggression and impulsive actions.",
            strength = YogaHelpers.strengthFromPercentage(strengthPct),
            strengthPercentage = strengthPct,
            isAuspicious = false,
            activationPeriod = "Mars Dasha - Rahu Antardasha, Tuesdays sensitive",
            cancellationFactors = cancellations.ifEmpty {
                listOf(
                    "Hanuman worship recommended",
                    "Mars mantra and Angarak Yoga remedies",
                    "Avoid red on sensitive dates"
                )
            }
        )
    }

    /**
     * Shukra Chandala Yoga - Venus conjunct Rahu
     * Unconventional pleasures and relationships
     */
    private fun evaluateShukraChandalaYoga(
        chart: VedicChart,
        rahuPos: PlanetPosition
    ): Yoga? {
        val venusPos = chart.planetPositions.find { it.planet == Planet.VENUS } ?: return null

        if (!YogaHelpers.areConjunct(venusPos, rahuPos)) return null

        val positions = listOf(venusPos, rahuPos)
        val strengthPct = YogaHelpers.calculateYogaStrength(chart, positions)

        val cancellations = mutableListOf<String>()

        // Venus dignified handles Rahu better
        if (YogaHelpers.isExalted(venusPos) || YogaHelpers.isInOwnSign(venusPos)) {
            cancellations.add("Venus dignified - refinement maintained")
        }

        // Can give fame in arts/entertainment
        if (venusPos.house in listOf(1, 5, 10, 11)) {
            cancellations.add("Can give fame in arts, entertainment, fashion")
        }

        val effects = "Unconventional relationships, attraction to foreign partners, " +
                "unusual artistic tastes, luxury through unconventional means, " +
                "possible scandals in love life, film/media industry connections, " +
                "technology-enhanced beauty/arts, international fashion sense"

        return Yoga(
            name = "Shukra Chandala Yoga",
            sanskritName = "शुक्र चांडाल योग",
            category = YogaCategory.NEGATIVE_YOGA,
            planets = listOf(Planet.VENUS, Planet.RAHU),
            houses = listOf(venusPos.house),
            description = "Venus conjunct Rahu in ${ordinal(venusPos.house)} house (${venusPos.sign.displayName})",
            effects = effects,
            strength = YogaHelpers.strengthFromPercentage(strengthPct),
            strengthPercentage = strengthPct,
            isAuspicious = false,
            activationPeriod = "Venus Dasha - Rahu Antardasha, Friday sensitive",
            cancellationFactors = cancellations.ifEmpty {
                listOf(
                    "Lakshmi worship recommended",
                    "Venus mantra recitation",
                    "Maintain ethical relationships"
                )
            }
        )
    }

    /**
     * Chandra Grahan Yoga - Moon with Rahu or Ketu
     * Eclipse yoga affecting mind and emotions
     */
    private fun evaluateChandraGrahanYoga(
        chart: VedicChart,
        rahuPos: PlanetPosition,
        ketuPos: PlanetPosition
    ): List<Yoga>? {
        val yogas = mutableListOf<Yoga>()
        val moonPos = chart.planetPositions.find { it.planet == Planet.MOON } ?: return null

        // Moon with Rahu
        if (YogaHelpers.areConjunct(moonPos, rahuPos)) {
            val strengthPct = YogaHelpers.calculateYogaStrength(chart, listOf(moonPos, rahuPos))

            val cancellations = mutableListOf<String>()
            
            // Strong Moon (bright phase) handles Rahu better
            val moonStrength = YogaHelpers.getMoonPhaseStrength(moonPos, chart)
            if (moonStrength > 0.7) {
                cancellations.add("Bright Moon - mental strength present")
            }

            // Jupiter's aspect protects mind
            val jupiterPos = chart.planetPositions.find { it.planet == Planet.JUPITER }
            if (jupiterPos != null && YogaHelpers.isAspecting(jupiterPos, moonPos)) {
                cancellations.add("Jupiter's aspect protects mind")
            }

            yogas.add(Yoga(
                name = "Chandra-Rahu Grahan Yoga",
                sanskritName = "चंद्र-राहु ग्रहण योग",
                category = YogaCategory.NEGATIVE_YOGA,
                planets = listOf(Planet.MOON, Planet.RAHU),
                houses = listOf(moonPos.house),
                description = "Moon conjunct Rahu in ${ordinal(moonPos.house)} house",
                effects = "Mental restlessness, anxiety tendencies, mother's health concerns, " +
                        "unusual imagination, foreign residence possible, " +
                        "powerful intuition when controlled, media/public exposure",
                strength = YogaHelpers.strengthFromPercentage(strengthPct),
                strengthPercentage = strengthPct,
                isAuspicious = false,
                activationPeriod = "Moon Dasha - Rahu Antardasha, Full moon sensitive",
                cancellationFactors = cancellations.ifEmpty {
                    listOf(
                        "Chandra mantra recitation",
                        "Mother's blessings help",
                        "White colors and pearls"
                    )
                }
            ))
        }

        // Moon with Ketu
        if (YogaHelpers.areConjunct(moonPos, ketuPos)) {
            val strengthPct = YogaHelpers.calculateYogaStrength(chart, listOf(moonPos, ketuPos))

            yogas.add(Yoga(
                name = "Chandra-Ketu Grahan Yoga",
                sanskritName = "चंद्र-केतु ग्रहण योग",
                category = YogaCategory.NEGATIVE_YOGA,
                planets = listOf(Planet.MOON, Planet.KETU),
                houses = listOf(moonPos.house),
                description = "Moon conjunct Ketu in ${ordinal(moonPos.house)} house",
                effects = "Emotional detachment, intuitive but disconnected, " +
                        "spiritual inclinations, past-life memories possible, " +
                        "mother relationship complex, isolation tendencies",
                strength = YogaHelpers.strengthFromPercentage(strengthPct),
                strengthPercentage = strengthPct,
                isAuspicious = false,
                activationPeriod = "Moon Dasha - Ketu Antardasha",
                cancellationFactors = listOf(
                    "Excellent for spiritual practice",
                    "Ganesha worship helps",
                    "Past-life karma clearing"
                )
            ))
        }

        return yogas.ifEmpty { null }
    }

    /**
     * Pitra Dosha - Sun with Rahu or Ketu
     * Ancestral afflictions
     */
    private fun evaluatePitraDosha(
        chart: VedicChart,
        rahuPos: PlanetPosition,
        ketuPos: PlanetPosition
    ): Yoga? {
        val sunPos = chart.planetPositions.find { it.planet == Planet.SUN } ?: return null

        val withRahu = YogaHelpers.areConjunct(sunPos, rahuPos)
        val withKetu = YogaHelpers.areConjunct(sunPos, ketuPos)

        if (!withRahu && !withKetu) return null

        val nodeInvolved = if (withRahu) Planet.RAHU else Planet.KETU
        val nodePos = if (withRahu) rahuPos else ketuPos

        val positions = listOf(sunPos, nodePos)
        val strengthPct = YogaHelpers.calculateYogaStrength(chart, positions)

        val cancellations = mutableListOf<String>()

        // Sun in own sign (Leo) or exalted (Aries)
        if (YogaHelpers.isExalted(sunPos) || YogaHelpers.isInOwnSign(sunPos)) {
            cancellations.add("Sun dignified - father's blessings present")
        }

        // In 9th house, stronger Pitra Dosha but also strong remediation potential
        if (sunPos.house == 9) {
            cancellations.add("9th house placement - ancestral rituals very effective")
        }

        val effects = buildString {
            append("Ancestral karma affecting life, father relationship complex, ")
            if (withRahu) {
                append("foreign connections through father, unconventional father figure, ")
                append("ego inflation issues, authority conflicts with foreign elements")
            } else {
                append("spiritual father connection, father may be detached, ")
                append("past-life father karma, leadership through service")
            }
        }

        return Yoga(
            name = "Pitra Dosha",
            sanskritName = "पितृ दोष",
            category = YogaCategory.NEGATIVE_YOGA,
            planets = listOf(Planet.SUN, nodeInvolved),
            houses = listOf(sunPos.house, 9),
            description = "Sun conjunct ${nodeInvolved.displayName} in ${ordinal(sunPos.house)} house",
            effects = effects,
            strength = YogaHelpers.strengthFromPercentage(strengthPct),
            strengthPercentage = strengthPct,
            isAuspicious = false,
            activationPeriod = "Sun Dasha - ${nodeInvolved.displayName} Antardasha, Pitru Paksha sensitive",
            cancellationFactors = cancellations.ifEmpty {
                listOf(
                    "Pitru Tarpan (ancestral offerings)",
                    "Shradh rituals during Pitru Paksha",
                    "Surya Namaskar and Sun worship"
                )
            }
        )
    }

    /**
     * Budhi Nash Yoga - Mercury with Rahu
     * Intellectual confusion
     */
    private fun evaluateBudhiNashYoga(
        chart: VedicChart,
        rahuPos: PlanetPosition
    ): Yoga? {
        val mercuryPos = chart.planetPositions.find { it.planet == Planet.MERCURY } ?: return null

        if (!YogaHelpers.areConjunct(mercuryPos, rahuPos)) return null

        val positions = listOf(mercuryPos, rahuPos)
        val strengthPct = YogaHelpers.calculateYogaStrength(chart, positions)

        val cancellations = mutableListOf<String>()

        // Mercury dignified
        if (YogaHelpers.isExalted(mercuryPos) || YogaHelpers.isInOwnSign(mercuryPos)) {
            cancellations.add("Mercury dignified - intelligence channels constructively")
        }

        // Can give tech/media success
        if (mercuryPos.house in listOf(3, 5, 10, 11)) {
            cancellations.add("Can give success in technology, media, communication")
        }

        return Yoga(
            name = "Budhi-Rahu Yoga",
            sanskritName = "बुद्धि-राहु योग",
            category = YogaCategory.NEGATIVE_YOGA,
            planets = listOf(Planet.MERCURY, Planet.RAHU),
            houses = listOf(mercuryPos.house),
            description = "Mercury conjunct Rahu in ${ordinal(mercuryPos.house)} house",
            effects = "Unconventional thinking, communication distortions possible, " +
                    "tech-savvy mind, marketing/advertising talent, " +
                    "nervous system sensitivity, speech may be deceptive, " +
                    "excellent for IT, foreign languages, media work",
            strength = YogaHelpers.strengthFromPercentage(strengthPct),
            strengthPercentage = strengthPct,
            isAuspicious = false,
            activationPeriod = "Mercury Dasha - Rahu Antardasha, Wednesday sensitive",
            cancellationFactors = cancellations.ifEmpty {
                listOf(
                    "Vishnu worship recommended",
                    "Speak truth consciously",
                    "Green colors beneficial"
                )
            }
        )
    }

    /**
     * Rahu-Ketu Axis Yogas - Based on axis placement
     */
    private fun evaluateRahuKetuAxisYogas(
        chart: VedicChart,
        rahuPos: PlanetPosition,
        ketuPos: PlanetPosition,
        houseLords: Map<Int, Planet>
    ): List<Yoga>? {
        val yogas = mutableListOf<Yoga>()

        // 1-7 axis: Self vs Partnership
        if ((rahuPos.house == 1 && ketuPos.house == 7) || (rahuPos.house == 7 && ketuPos.house == 1)) {
            val rahuIn1 = rahuPos.house == 1
            yogas.add(Yoga(
                name = if (rahuIn1) "Rahu-Ketu 1-7 Axis" else "Ketu-Rahu 1-7 Axis",
                sanskritName = "राहु-केतु १-७ अक्ष",
                category = YogaCategory.SPECIAL_YOGA,
                planets = listOf(Planet.RAHU, Planet.KETU),
                houses = listOf(1, 7),
                description = "Rahu in ${ordinal(rahuPos.house)}, Ketu in ${ordinal(ketuPos.house)}",
                effects = if (rahuIn1) {
                    "Focus on self-development, partnership challenges, " +
                            "need to balance ego with relationships, foreign personality influences"
                } else {
                    "Past-life self-work complete, now learning partnerships, " +
                            "spiritual approach to relationships, detachment from ego"
                },
                strength = YogaStrength.MODERATE,
                strengthPercentage = 55.0,
                isAuspicious = false,
                activationPeriod = "Rahu-Ketu Dasha periods",
                cancellationFactors = listOf("Growth through balancing both houses")
            ))
        }

        // 4-10 axis: Home vs Career
        if ((rahuPos.house == 4 && ketuPos.house == 10) || (rahuPos.house == 10 && ketuPos.house == 4)) {
            val rahuIn4 = rahuPos.house == 4
            yogas.add(Yoga(
                name = if (rahuIn4) "Rahu-Ketu 4-10 Axis" else "Ketu-Rahu 4-10 Axis",
                sanskritName = "राहु-केतु ४-१० अक्ष",
                category = YogaCategory.SPECIAL_YOGA,
                planets = listOf(Planet.RAHU, Planet.KETU),
                houses = listOf(4, 10),
                description = "Rahu in ${ordinal(rahuPos.house)}, Ketu in ${ordinal(ketuPos.house)}",
                effects = if (rahuIn4) {
                    "Focus on home, property, foreign residence, " +
                            "career less emphasized, mother's foreign connections"
                } else {
                    "Career-focused life, home life detached, " +
                            "professional success abroad, public recognition"
                },
                strength = YogaStrength.MODERATE,
                strengthPercentage = 55.0,
                isAuspicious = false,
                activationPeriod = "Rahu-Ketu Dasha periods",
                cancellationFactors = listOf("Balance home and career for best results")
            ))
        }

        // 5-11 axis: Creativity vs Gains
        if ((rahuPos.house == 5 && ketuPos.house == 11) || (rahuPos.house == 11 && ketuPos.house == 5)) {
            val rahuIn5 = rahuPos.house == 5
            yogas.add(Yoga(
                name = if (rahuIn5) "Rahu-Ketu 5-11 Axis" else "Ketu-Rahu 5-11 Axis",
                sanskritName = "राहु-केतु ५-११ अक्ष",
                category = YogaCategory.SPECIAL_YOGA,
                planets = listOf(Planet.RAHU, Planet.KETU),
                houses = listOf(5, 11),
                description = "Rahu in ${ordinal(rahuPos.house)}, Ketu in ${ordinal(ketuPos.house)}",
                effects = if (rahuIn5) {
                    "Unconventional creativity, foreign children connection, " +
                            "speculation through foreign means, romance with foreigners"
                } else {
                    "Gains from foreign sources, network detachment, " +
                            "spiritual friendships, past-life creative talents"
                },
                strength = YogaStrength.MODERATE,
                strengthPercentage = 55.0,
                isAuspicious = false,
                activationPeriod = "Rahu-Ketu Dasha periods",
                cancellationFactors = listOf("Creative use of both houses' significations")
            ))
        }

        // 6-12 axis: Service/Enemies vs Losses/Spirituality
        if ((rahuPos.house == 6 && ketuPos.house == 12) || (rahuPos.house == 12 && ketuPos.house == 6)) {
            val rahuIn6 = rahuPos.house == 6
            yogas.add(Yoga(
                name = if (rahuIn6) "Rahu-Ketu 6-12 Axis" else "Ketu-Rahu 6-12 Axis",
                sanskritName = "राहु-केतु ६-१२ अक्ष",
                category = YogaCategory.SPECIAL_YOGA,
                planets = listOf(Planet.RAHU, Planet.KETU),
                houses = listOf(6, 12),
                description = "Rahu in ${ordinal(rahuPos.house)}, Ketu in ${ordinal(ketuPos.house)}",
                effects = if (rahuIn6) {
                    "Victory through unconventional means, foreign enemies, " +
                            "health through alternative medicine, service in foreign lands"
                } else {
                    "Spiritual liberation focus, expenses on foreign travel, " +
                            "isolation beneficial, past-life service completion"
                },
                strength = YogaStrength.MODERATE,
                strengthPercentage = 55.0,
                isAuspicious = rahuIn6, // Rahu in 6th is considered good
                activationPeriod = "Rahu-Ketu Dasha periods",
                cancellationFactors = listOf("Axis favorable for spiritual growth")
            ))
        }

        return yogas.ifEmpty { null }
    }

    /**
     * Rahu House-specific Yogas
     */
    private fun evaluateRahuHouseYogas(
        chart: VedicChart,
        rahuPos: PlanetPosition,
        houseLords: Map<Int, Planet>
    ): List<Yoga>? {
        val yogas = mutableListOf<Yoga>()

        // Rahu in favorable houses (3, 6, 10, 11) - Upachaya
        if (rahuPos.house in UPACHAYA_HOUSES) {
            val effects = when (rahuPos.house) {
                3 -> "Courageous in unconventional ways, media success, tech communication"
                6 -> "Victory over enemies, health through alternative means, foreign service"
                10 -> "Career success through unconventional means, foreign career, tech profession"
                11 -> "Gains from foreign sources, fulfilled desires, large network"
                else -> "Rahu well-placed for material success"
            }

            yogas.add(Yoga(
                name = "Rahu Upachaya Yoga",
                sanskritName = "राहु उपचय योग",
                category = YogaCategory.SPECIAL_YOGA,
                planets = listOf(Planet.RAHU),
                houses = listOf(rahuPos.house),
                description = "Rahu in Upachaya house (${ordinal(rahuPos.house)})",
                effects = effects + ". Material success through Rahu's significations.",
                strength = YogaStrength.STRONG,
                strengthPercentage = 70.0,
                isAuspicious = true,
                activationPeriod = "Rahu Dasha brings significant gains",
                cancellationFactors = emptyList()
            ))
        }

        // Rahu in Taurus/Gemini/Virgo (good signs)
        if (rahuPos.sign in listOf(ZodiacSign.TAURUS, ZodiacSign.GEMINI, ZodiacSign.VIRGO)) {
            yogas.add(Yoga(
                name = "Rahu Swakshetra Yoga",
                sanskritName = "राहु स्वक्षेत्र योग",
                category = YogaCategory.SPECIAL_YOGA,
                planets = listOf(Planet.RAHU),
                houses = listOf(rahuPos.house),
                description = "Rahu in favorable sign ${rahuPos.sign.displayName}",
                effects = "Rahu's positive significations manifest - material success, " +
                        "foreign connections beneficial, technology aptitude, " +
                        "worldly achievements, strategic thinking",
                strength = YogaStrength.STRONG,
                strengthPercentage = 65.0,
                isAuspicious = true,
                activationPeriod = "Rahu Dasha",
                cancellationFactors = emptyList()
            ))
        }

        return yogas.ifEmpty { null }
    }

    /**
     * Ketu House-specific Yogas
     */
    private fun evaluateKetuHouseYogas(
        chart: VedicChart,
        ketuPos: PlanetPosition,
        houseLords: Map<Int, Planet>
    ): List<Yoga>? {
        val yogas = mutableListOf<Yoga>()

        // Ketu in 12th - Moksha Yoga
        if (ketuPos.house == 12) {
            yogas.add(Yoga(
                name = "Ketu Moksha Yoga",
                sanskritName = "केतु मोक्ष योग",
                category = YogaCategory.SPECIAL_YOGA,
                planets = listOf(Planet.KETU),
                houses = listOf(12),
                description = "Ketu in 12th house of liberation",
                effects = "Strong spiritual inclination, past-life spiritual practices activate, " +
                        "detachment from material world, meditation comes naturally, " +
                        "dreams are significant, foreign spiritual connections",
                strength = YogaStrength.STRONG,
                strengthPercentage = 70.0,
                isAuspicious = true,
                activationPeriod = "Ketu Dasha - spiritual breakthroughs",
                cancellationFactors = emptyList()
            ))
        }

        // Ketu in 9th - Dharma Ketu
        if (ketuPos.house == 9) {
            yogas.add(Yoga(
                name = "Dharma Ketu Yoga",
                sanskritName = "धर्म केतु योग",
                category = YogaCategory.SPECIAL_YOGA,
                planets = listOf(Planet.KETU),
                houses = listOf(9),
                description = "Ketu in 9th house of dharma",
                effects = "Past-life religious practices, natural spiritual wisdom, " +
                        "unconventional beliefs, may reject traditional religion, " +
                        "direct spiritual experience, guru connection mystical",
                strength = YogaStrength.MODERATE,
                strengthPercentage = 60.0,
                isAuspicious = true,
                activationPeriod = "Ketu Dasha - spiritual awakening",
                cancellationFactors = listOf("Father relationship may be distant")
            ))
        }

        // Ketu in Sagittarius/Pisces/Scorpio (good signs)
        if (ketuPos.sign in listOf(ZodiacSign.SAGITTARIUS, ZodiacSign.PISCES, ZodiacSign.SCORPIO)) {
            yogas.add(Yoga(
                name = "Ketu Swakshetra Yoga",
                sanskritName = "केतु स्वक्षेत्र योग",
                category = YogaCategory.SPECIAL_YOGA,
                planets = listOf(Planet.KETU),
                houses = listOf(ketuPos.house),
                description = "Ketu in favorable sign ${ketuPos.sign.displayName}",
                effects = "Ketu's positive significations manifest - spiritual insights, " +
                        "intuition strong, past-life talents accessible, " +
                        "research aptitude, occult abilities, healing potential",
                strength = YogaStrength.STRONG,
                strengthPercentage = 65.0,
                isAuspicious = true,
                activationPeriod = "Ketu Dasha",
                cancellationFactors = emptyList()
            ))
        }

        return yogas.ifEmpty { null }
    }

    /**
     * Benefic Nodal Yogas - When nodes give positive results
     */
    private fun evaluateBeneficNodalYogas(
        chart: VedicChart,
        rahuPos: PlanetPosition,
        ketuPos: PlanetPosition,
        houseLords: Map<Int, Planet>
    ): List<Yoga>? {
        val yogas = mutableListOf<Yoga>()

        // Nodes with their dispositors well-placed
        val rahuDispositor = rahuPos.sign.ruler
        val ketuDispositor = ketuPos.sign.ruler

        val rahuDispositorPos = chart.planetPositions.find { it.planet == rahuDispositor }
        val ketuDispositorPos = chart.planetPositions.find { it.planet == ketuDispositor }

        // Rahu's dispositor strong
        if (rahuDispositorPos != null) {
            val dispositorStrong = YogaHelpers.isExalted(rahuDispositorPos) ||
                    YogaHelpers.isInOwnSign(rahuDispositorPos) ||
                    rahuDispositorPos.house in KENDRA_HOUSES

            if (dispositorStrong) {
                yogas.add(Yoga(
                    name = "Rahu Dispositor Yoga",
                    sanskritName = "राहु पदाधिपति योग",
                    category = YogaCategory.SPECIAL_YOGA,
                    planets = listOf(Planet.RAHU, rahuDispositor),
                    houses = listOf(rahuPos.house, rahuDispositorPos.house),
                    description = "Rahu's sign lord ${rahuDispositor.displayName} strongly placed",
                    effects = "Rahu's effects channeled positively through strong dispositor, " +
                            "worldly success with wisdom, foreign connections beneficial, " +
                            "ambitions fulfilled constructively",
                    strength = YogaStrength.STRONG,
                    strengthPercentage = 65.0,
                    isAuspicious = true,
                    activationPeriod = "Rahu Dasha - ${rahuDispositor.displayName} Antardasha",
                    cancellationFactors = emptyList()
                ))
            }
        }

        // Ketu's dispositor strong
        if (ketuDispositorPos != null) {
            val dispositorStrong = YogaHelpers.isExalted(ketuDispositorPos) ||
                    YogaHelpers.isInOwnSign(ketuDispositorPos) ||
                    ketuDispositorPos.house in KENDRA_HOUSES

            if (dispositorStrong) {
                yogas.add(Yoga(
                    name = "Ketu Dispositor Yoga",
                    sanskritName = "केतु पदाधिपति योग",
                    category = YogaCategory.SPECIAL_YOGA,
                    planets = listOf(Planet.KETU, ketuDispositor),
                    houses = listOf(ketuPos.house, ketuDispositorPos.house),
                    description = "Ketu's sign lord ${ketuDispositor.displayName} strongly placed",
                    effects = "Ketu's effects channeled positively through strong dispositor, " +
                            "spiritual growth with material stability, past-life talents manifest, " +
                            "intuition guides correctly",
                    strength = YogaStrength.STRONG,
                    strengthPercentage = 65.0,
                    isAuspicious = true,
                    activationPeriod = "Ketu Dasha - ${ketuDispositor.displayName} Antardasha",
                    cancellationFactors = emptyList()
                ))
            }
        }

        // Nodes aspected by Jupiter
        val jupiterPos = chart.planetPositions.find { it.planet == Planet.JUPITER }
        if (jupiterPos != null) {
            val jupiterAspectsRahu = YogaHelpers.isAspecting(jupiterPos, rahuPos)
            val jupiterAspectsKetu = YogaHelpers.isAspecting(jupiterPos, ketuPos)

            if (jupiterAspectsRahu && jupiterAspectsKetu) {
                yogas.add(Yoga(
                    name = "Guru Graha Yoga on Nodes",
                    sanskritName = "गुरु ग्रह योग",
                    category = YogaCategory.SPECIAL_YOGA,
                    planets = listOf(Planet.JUPITER, Planet.RAHU, Planet.KETU),
                    houses = listOf(jupiterPos.house, rahuPos.house, ketuPos.house),
                    description = "Jupiter aspects both Rahu and Ketu",
                    effects = "Nodal effects significantly moderated by Jupiter's wisdom, " +
                            "karmic lessons come with grace, spiritual protection strong, " +
                            "dharmic approach to material and spiritual matters",
                    strength = YogaStrength.STRONG,
                    strengthPercentage = 70.0,
                    isAuspicious = true,
                    activationPeriod = "Jupiter Dasha - nodal Antardashas blessed",
                    cancellationFactors = emptyList()
                ))
            }
        }

        return yogas.ifEmpty { null }
    }

    /**
     * Convert number to ordinal string
     */
    private fun ordinal(n: Int): String = when (n) {
        1 -> "1st"
        2 -> "2nd"
        3 -> "3rd"
        else -> "${n}th"
    }

    /**
     * Data class for Kala Sarpa types
     */
    private data class KalaSarpaType(
        val name: String,
        val sanskritName: String,
        val deity: String,
        val effects: String
    )
}
