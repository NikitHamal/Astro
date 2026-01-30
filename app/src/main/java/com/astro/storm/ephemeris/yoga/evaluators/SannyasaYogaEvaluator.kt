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
 * Sannyasa (Renunciation/Spiritual) Yoga Evaluator
 *
 * Evaluates yogas related to spirituality, renunciation, liberation, and the ascetic path.
 * These yogas indicate detachment from worldly life and inclination towards spiritual pursuits.
 *
 * Yogas Evaluated:
 * - Sannyasa Yoga (True Renunciation)
 * - Pravrajya Yoga (Monastic Life)
 * - Moksha Yoga (Liberation)
 * - Vairagi Yoga (Detachment)
 * - Tapasvi Yoga (Austerity)
 * - Diksha Yoga (Initiation)
 * - Brahma Yoga (Supreme Knowledge)
 * - Yogakaraka Combinations
 * - Guru Yoga (Teacher Connection)
 * - Adhyatmika Yoga (Inner Spirituality)
 * - Parivrajaka Yoga (Wandering Ascetic)
 * - Siddha Yoga (Spiritual Powers)
 *
 * Based on:
 * - Brihat Parashara Hora Shastra (BPHS)
 * - Phaladeepika (Chapter on Sannyasa)
 * - Jataka Parijata
 * - Saravali
 * - Brihat Jataka
 *
 * @author AstroStorm
 */
class SannyasaYogaEvaluator : YogaEvaluator {

    override val category: YogaCategory = YogaCategory.SPECIAL_YOGA

    companion object {
        private val KENDRA_HOUSES = setOf(1, 4, 7, 10)
        private val TRIKONA_HOUSES = setOf(1, 5, 9)
        private val DUSTHANA_HOUSES = setOf(6, 8, 12)
        private val MOKSHA_HOUSES = setOf(4, 8, 12) // Moksha Trikona
        private val SPIRITUAL_SIGNS = setOf(
            ZodiacSign.CANCER, ZodiacSign.SCORPIO, ZodiacSign.PISCES, // Water signs
            ZodiacSign.SAGITTARIUS // Jupiter's sign
        )
    }

    override fun evaluate(chart: VedicChart): List<Yoga> {
        val yogas = mutableListOf<Yoga>()
        val ascendantSign = ZodiacSign.fromLongitude(chart.ascendant)
        val houseLords = YogaHelpers.getHouseLords(ascendantSign)

        // Core Sannyasa Yogas
        evaluateSannyasaYoga(chart, houseLords)?.let { yogas.addAll(it) }
        evaluatePravrajyaYoga(chart, houseLords)?.let { yogas.add(it) }
        
        // Moksha (Liberation) Yogas
        evaluateMokshaYoga(chart, houseLords)?.let { yogas.addAll(it) }
        
        // Spiritual Practice Yogas
        evaluateTapasviYoga(chart, houseLords)?.let { yogas.add(it) }
        evaluateDikshaYoga(chart, houseLords)?.let { yogas.add(it) }
        evaluateBrahmaYoga(chart, houseLords)?.let { yogas.add(it) }
        
        // Detachment Yogas
        evaluateVairagiYoga(chart, houseLords)?.let { yogas.add(it) }
        evaluateParivrajakaYoga(chart, houseLords)?.let { yogas.add(it) }
        
        // Spiritual Power Yogas
        evaluateSiddhaYoga(chart, houseLords)?.let { yogas.add(it) }
        evaluateYogakarakaSpiritual(chart, houseLords)?.let { yogas.add(it) }
        
        // Guru (Teacher) Yogas
        evaluateGuruYoga(chart, houseLords)?.let { yogas.addAll(it) }
        
        // 12th House Yogas
        evaluateTwelfthHouseYogas(chart, houseLords)?.let { yogas.addAll(it) }
        
        // Ketu-based Spiritual Yogas
        evaluateKetuSpiritualYogas(chart, houseLords)?.let { yogas.addAll(it) }

        return yogas
    }

    /**
     * Sannyasa Yoga - True Renunciation
     * Four or more planets in one house (without aspect of other planets)
     * Or strong 10th lord with Saturn aspecting
     */
    private fun evaluateSannyasaYoga(
        chart: VedicChart,
        houseLords: Map<Int, Planet>
    ): List<Yoga>? {
        val yogas = mutableListOf<Yoga>()

        // Type 1: Four or more planets in one house (classic Sannyasa Yoga)
        for (house in 1..12) {
            val planetsInHouse = chart.planetPositions.filter { 
                it.house == house && it.planet !in listOf(Planet.RAHU, Planet.KETU)
            }
            
            if (planetsInHouse.size >= 4) {
                // Strongest planet determines the type of sannyasi
                val strongestPlanet = planetsInHouse.maxByOrNull { pos ->
                    var score = 0.0
                    if (YogaHelpers.isExalted(pos)) score += 30
                    if (YogaHelpers.isInOwnSign(pos)) score += 25
                    if (YogaHelpers.isInFriendSign(pos)) score += 15
                    score
                }?.planet ?: Planet.SATURN

                val sannyasiType = when (strongestPlanet) {
                    Planet.SUN -> "Raja Yogi (royal renunciant with authority)"
                    Planet.MOON -> "Shakti Upasaka (devotee of Divine Mother)"
                    Planet.MARS -> "Kshatriya Sannyasi (warrior-monk, protector of dharma)"
                    Planet.MERCURY -> "Jnana Yogi (path of knowledge and intellect)"
                    Planet.JUPITER -> "Deva Guru Sannyasi (teacher of divine wisdom)"
                    Planet.VENUS -> "Bhakti Yogi (path of devotion and beauty)"
                    Planet.SATURN -> "Vairagi (complete detachment, austere path)"
                    else -> "General renunciation tendencies"
                }

                val strengthPct = YogaHelpers.calculateYogaStrength(chart, planetsInHouse)

                yogas.add(Yoga(
                    name = "Sannyasa Yoga",
                    sanskritName = "संन्यास योग",
                    category = YogaCategory.SPECIAL_YOGA,
                    planets = planetsInHouse.map { it.planet },
                    houses = listOf(house),
                    description = "${planetsInHouse.size} planets conjunct in ${ordinal(house)} house, " +
                            "led by ${strongestPlanet.displayName}",
                    effects = "Strong renunciation tendencies, detachment from worldly life, " +
                            "spiritual calling, may leave material pursuits for higher path. " +
                            "Type: $sannyasiType",
                    strength = YogaHelpers.strengthFromPercentage(strengthPct),
                    strengthPercentage = strengthPct,
                    isAuspicious = true,
                    activationPeriod = "Dasha of strongest planet in combination, especially after Saturn maturity",
                    cancellationFactors = listOf(
                        "Strong Venus may delay renunciation",
                        "Family responsibilities may modify expression"
                    )
                ))
            }
        }

        // Type 2: Moon in drekkana of Saturn, aspected by Saturn
        val moonPos = chart.planetPositions.find { it.planet == Planet.MOON }
        val saturnPos = chart.planetPositions.find { it.planet == Planet.SATURN }
        
        if (moonPos != null && saturnPos != null) {
            // Check if Saturn aspects Moon
            if (YogaHelpers.isAspecting(saturnPos, moonPos)) {
                // Moon in Saturn's drekkana (Capricorn/Aquarius decanate)
                val moonDegree = moonPos.longitude % 30
                val decanate = when {
                    moonDegree < 10 -> 1
                    moonDegree < 20 -> 2
                    else -> 3
                }
                
                // Saturn rules certain decanates
                val saturnDecanate = (moonPos.sign.ordinal * 3 + decanate - 1) % 12
                val isSaturnDecanate = saturnDecanate in listOf(9, 10) // Capricorn, Aquarius lords
                
                if (isSaturnDecanate || moonPos.sign in listOf(ZodiacSign.CAPRICORN, ZodiacSign.AQUARIUS)) {
                    val positions = listOf(moonPos, saturnPos)
                    val strengthPct = YogaHelpers.calculateYogaStrength(chart, positions)
                    
                    yogas.add(Yoga(
                        name = "Shani-Chandra Sannyasa Yoga",
                        sanskritName = "शनि-चंद्र संन्यास योग",
                        category = YogaCategory.SPECIAL_YOGA,
                        planets = listOf(Planet.MOON, Planet.SATURN),
                        houses = listOf(moonPos.house, saturnPos.house),
                        description = "Moon in Saturn's influence with Saturn's aspect",
                        effects = "Mind inclined to detachment, austere lifestyle, " +
                                "philosophical outlook, may embrace solitude, " +
                                "old age brings spiritual focus",
                        strength = YogaHelpers.strengthFromPercentage(strengthPct * 0.9),
                        strengthPercentage = (strengthPct * 0.9).coerceIn(10.0, 100.0),
                        isAuspicious = true,
                        activationPeriod = "Saturn Dasha, especially Saturn-Moon period",
                        cancellationFactors = listOf("Jupiter's aspect may balance with worldly duties")
                    ))
                }
            }
        }

        return yogas.ifEmpty { null }
    }

    /**
     * Pravrajya Yoga - Monastic Life
     * Lord of 10th in 9th, aspected by Saturn
     */
    private fun evaluatePravrajyaYoga(
        chart: VedicChart,
        houseLords: Map<Int, Planet>
    ): Yoga? {
        val lord10 = houseLords[10] ?: return null
        val pos10 = chart.planetPositions.find { it.planet == lord10 } ?: return null
        val saturnPos = chart.planetPositions.find { it.planet == Planet.SATURN } ?: return null

        // 10th lord in 9th house
        if (pos10.house != 9) return null

        // Aspected by Saturn
        if (!YogaHelpers.isAspecting(saturnPos, pos10)) return null

        val positions = listOf(pos10, saturnPos)
        val strengthPct = YogaHelpers.calculateYogaStrength(chart, positions)

        return Yoga(
            name = "Pravrajya Yoga",
            sanskritName = "प्रव्रज्या योग",
            category = YogaCategory.SPECIAL_YOGA,
            planets = listOf(lord10, Planet.SATURN),
            houses = listOf(9, 10, saturnPos.house),
            description = "10th lord ${lord10.displayName} in 9th house, aspected by Saturn",
            effects = "Career leads to spiritual path, profession becomes dharma, " +
                    "may join monastery or ashram, teaching spiritual subjects, " +
                    "giving up worldly profession for higher calling",
            strength = YogaHelpers.strengthFromPercentage(strengthPct),
            strengthPercentage = strengthPct,
            isAuspicious = true,
            activationPeriod = "10th lord Dasha with Saturn aspect activating",
            cancellationFactors = listOf(
                "Strong Venus delays renunciation",
                "Family karmas may need completion first"
            )
        )
    }

    /**
     * Moksha Yoga - Liberation
     * Various combinations for spiritual liberation
     */
    private fun evaluateMokshaYoga(
        chart: VedicChart,
        houseLords: Map<Int, Planet>
    ): List<Yoga>? {
        val yogas = mutableListOf<Yoga>()
        val lord12 = houseLords[12] ?: return null
        val lord4 = houseLords[4] ?: return null
        val lord8 = houseLords[8] ?: return null

        val pos12 = chart.planetPositions.find { it.planet == lord12 }
        val pos4 = chart.planetPositions.find { it.planet == lord4 }
        val pos8 = chart.planetPositions.find { it.planet == lord8 }

        val jupiterPos = chart.planetPositions.find { it.planet == Planet.JUPITER }
        val ketuPos = chart.planetPositions.find { it.planet == Planet.KETU }

        // Type 1: 12th lord in 12th (Vimala Yoga variant for moksha)
        if (pos12 != null && pos12.house == 12) {
            val strengthPct = YogaHelpers.calculateYogaStrength(chart, listOf(pos12))
            
            yogas.add(Yoga(
                name = "Moksha Karta Yoga",
                sanskritName = "मोक्ष कर्ता योग",
                category = YogaCategory.SPECIAL_YOGA,
                planets = listOf(lord12),
                houses = listOf(12),
                description = "12th lord ${lord12.displayName} in 12th house",
                effects = "Strong liberation potential, spiritual expenses fruitful, " +
                        "meditation practice effective, final liberation attainable, " +
                        "foreign spiritual journeys beneficial",
                strength = YogaHelpers.strengthFromPercentage(strengthPct),
                strengthPercentage = strengthPct,
                isAuspicious = true,
                activationPeriod = "12th lord Dasha, especially in spiritual sub-periods",
                cancellationFactors = emptyList()
            ))
        }

        // Type 2: Moksha Trikona lords (4, 8, 12) connected
        if (pos4 != null && pos8 != null && pos12 != null) {
            val anyConnection = YogaHelpers.areConjunct(pos4, pos8) ||
                    YogaHelpers.areConjunct(pos8, pos12) ||
                    YogaHelpers.areConjunct(pos4, pos12)

            if (anyConnection) {
                val connectedLords = mutableListOf<Planet>()
                val connectedHouses = mutableListOf<Int>()
                
                if (YogaHelpers.areConjunct(pos4, pos8)) {
                    connectedLords.addAll(listOf(lord4, lord8))
                    connectedHouses.addAll(listOf(4, 8))
                }
                if (YogaHelpers.areConjunct(pos8, pos12)) {
                    if (lord8 !in connectedLords) connectedLords.add(lord8)
                    if (lord12 !in connectedLords) connectedLords.add(lord12)
                    if (8 !in connectedHouses) connectedHouses.add(8)
                    if (12 !in connectedHouses) connectedHouses.add(12)
                }
                if (YogaHelpers.areConjunct(pos4, pos12)) {
                    if (lord4 !in connectedLords) connectedLords.add(lord4)
                    if (lord12 !in connectedLords) connectedLords.add(lord12)
                    if (4 !in connectedHouses) connectedHouses.add(4)
                    if (12 !in connectedHouses) connectedHouses.add(12)
                }

                val positions = listOfNotNull(pos4, pos8, pos12).filter { 
                    it.planet in connectedLords 
                }
                val strengthPct = YogaHelpers.calculateYogaStrength(chart, positions)

                yogas.add(Yoga(
                    name = "Moksha Trikona Yoga",
                    sanskritName = "मोक्ष त्रिकोण योग",
                    category = YogaCategory.SPECIAL_YOGA,
                    planets = connectedLords,
                    houses = connectedHouses,
                    description = "Moksha trikona lords (4th, 8th, 12th) connected",
                    effects = "Deep spiritual transformation, liberation through inner work, " +
                            "kundalini awakening potential, past-life spiritual practices activate, " +
                            "meditation brings profound experiences",
                    strength = YogaHelpers.strengthFromPercentage(strengthPct),
                    strengthPercentage = strengthPct,
                    isAuspicious = true,
                    activationPeriod = "Dashas of connected moksha lords",
                    cancellationFactors = emptyList()
                ))
            }
        }

        // Type 3: Jupiter in 12th with Ketu aspect
        if (jupiterPos != null && ketuPos != null && jupiterPos.house == 12) {
            if (YogaHelpers.isAspecting(ketuPos, jupiterPos) || 
                YogaHelpers.areConjunct(jupiterPos, ketuPos)) {
                
                val positions = listOf(jupiterPos, ketuPos)
                val strengthPct = YogaHelpers.calculateYogaStrength(chart, positions)

                yogas.add(Yoga(
                    name = "Guru-Ketu Moksha Yoga",
                    sanskritName = "गुरु-केतु मोक्ष योग",
                    category = YogaCategory.SPECIAL_YOGA,
                    planets = listOf(Planet.JUPITER, Planet.KETU),
                    houses = listOf(12, ketuPos.house),
                    description = "Jupiter in 12th with Ketu influence",
                    effects = "Wisdom leads to liberation, past-life guru connection, " +
                            "natural meditation ability, spiritual knowledge complete, " +
                            "final life cycle possible, ashram residence likely",
                    strength = YogaHelpers.strengthFromPercentage(strengthPct * 1.1),
                    strengthPercentage = (strengthPct * 1.1).coerceIn(10.0, 100.0),
                    isAuspicious = true,
                    activationPeriod = "Jupiter or Ketu Dasha",
                    cancellationFactors = emptyList()
                ))
            }
        }

        return yogas.ifEmpty { null }
    }

    /**
     * Tapasvi Yoga - Austerity
     * Saturn strong in 9th or 12th
     */
    private fun evaluateTapasviYoga(
        chart: VedicChart,
        houseLords: Map<Int, Planet>
    ): Yoga? {
        val saturnPos = chart.planetPositions.find { it.planet == Planet.SATURN } ?: return null

        // Saturn in 9th or 12th house
        if (saturnPos.house !in listOf(9, 12)) return null

        // Saturn should be reasonably strong
        val saturnStrong = YogaHelpers.isExalted(saturnPos) || 
                YogaHelpers.isInOwnSign(saturnPos) || 
                YogaHelpers.isInFriendSign(saturnPos) ||
                !YogaHelpers.isDebilitated(saturnPos)

        if (!saturnStrong) return null

        val strengthPct = YogaHelpers.calculateYogaStrength(chart, listOf(saturnPos))

        val effects = if (saturnPos.house == 9) {
            "Austere approach to spirituality, disciplined dharma practice, " +
                    "teacher may be strict, pilgrimage through hardship, " +
                    "father may be ascetic or distant, late spiritual awakening"
        } else {
            "Austere meditation practice, solitary spiritual path, " +
                    "foreign ashram residence, expenses on spiritual practices, " +
                    "liberation through service and discipline"
        }

        return Yoga(
            name = "Tapasvi Yoga",
            sanskritName = "तपस्वी योग",
            category = YogaCategory.SPECIAL_YOGA,
            planets = listOf(Planet.SATURN),
            houses = listOf(saturnPos.house),
            description = "Saturn well-placed in ${ordinal(saturnPos.house)} house",
            effects = effects,
            strength = YogaHelpers.strengthFromPercentage(strengthPct),
            strengthPercentage = strengthPct,
            isAuspicious = true,
            activationPeriod = "Saturn Dasha, especially after age 36",
            cancellationFactors = listOf("May manifest as regular discipline rather than formal renunciation")
        )
    }

    /**
     * Diksha Yoga - Spiritual Initiation
     * 9th lord connected with Jupiter and Ketu
     */
    private fun evaluateDikshaYoga(
        chart: VedicChart,
        houseLords: Map<Int, Planet>
    ): Yoga? {
        val lord9 = houseLords[9] ?: return null
        val pos9 = chart.planetPositions.find { it.planet == lord9 } ?: return null
        val jupiterPos = chart.planetPositions.find { it.planet == Planet.JUPITER } ?: return null
        val ketuPos = chart.planetPositions.find { it.planet == Planet.KETU } ?: return null

        // 9th lord connected with Jupiter
        val with9Lord = YogaHelpers.areConjunct(pos9, jupiterPos) || 
                YogaHelpers.isAspecting(jupiterPos, pos9)

        // Any connection with Ketu
        val ketuConnection = YogaHelpers.areConjunct(pos9, ketuPos) || 
                YogaHelpers.areConjunct(jupiterPos, ketuPos) ||
                YogaHelpers.isAspecting(ketuPos, pos9) ||
                YogaHelpers.isAspecting(ketuPos, jupiterPos)

        if (!with9Lord || !ketuConnection) return null

        val positions = listOf(pos9, jupiterPos, ketuPos)
        val strengthPct = YogaHelpers.calculateYogaStrength(chart, positions)

        return Yoga(
            name = "Diksha Yoga",
            sanskritName = "दीक्षा योग",
            category = YogaCategory.SPECIAL_YOGA,
            planets = listOf(lord9, Planet.JUPITER, Planet.KETU),
            houses = listOf(9, pos9.house, jupiterPos.house),
            description = "9th lord connected with Jupiter and Ketu",
            effects = "Spiritual initiation destined, guru will appear, " +
                    "mantra diksha powerful, tantric or yogic initiation, " +
                    "past-life spiritual vows continue, discipleship important",
            strength = YogaHelpers.strengthFromPercentage(strengthPct),
            strengthPercentage = strengthPct,
            isAuspicious = true,
            activationPeriod = "9th lord, Jupiter, or Ketu Dasha",
            cancellationFactors = emptyList()
        )
    }

    /**
     * Brahma Yoga - Supreme Knowledge
     * Jupiter and Venus in Kendras, Mercury in Kendra/Trikona
     */
    private fun evaluateBrahmaYoga(
        chart: VedicChart,
        houseLords: Map<Int, Planet>
    ): Yoga? {
        val jupiterPos = chart.planetPositions.find { it.planet == Planet.JUPITER } ?: return null
        val venusPos = chart.planetPositions.find { it.planet == Planet.VENUS } ?: return null
        val mercuryPos = chart.planetPositions.find { it.planet == Planet.MERCURY } ?: return null

        // Jupiter in Kendra
        val jupiterInKendra = jupiterPos.house in KENDRA_HOUSES
        // Venus in Kendra
        val venusInKendra = venusPos.house in KENDRA_HOUSES
        // Mercury in Kendra or Trikona
        val mercuryInGoodHouse = mercuryPos.house in (KENDRA_HOUSES + TRIKONA_HOUSES)

        if (!jupiterInKendra || !venusInKendra || !mercuryInGoodHouse) return null

        val positions = listOf(jupiterPos, venusPos, mercuryPos)
        val strengthPct = YogaHelpers.calculateYogaStrength(chart, positions)

        return Yoga(
            name = "Brahma Yoga",
            sanskritName = "ब्रह्म योग",
            category = YogaCategory.SPECIAL_YOGA,
            planets = listOf(Planet.JUPITER, Planet.VENUS, Planet.MERCURY),
            houses = listOf(jupiterPos.house, venusPos.house, mercuryPos.house),
            description = "Jupiter and Venus in Kendras, Mercury in Kendra/Trikona",
            effects = "Supreme knowledge attainment, Brahma Vidya accessible, " +
                    "understanding of ultimate reality, respected scholar, " +
                    "teaching capacity divine, Vedantic wisdom, " +
                    "integration of knowledge and devotion",
            strength = YogaHelpers.strengthFromPercentage(strengthPct * 1.1),
            strengthPercentage = (strengthPct * 1.1).coerceIn(10.0, 100.0),
            isAuspicious = true,
            activationPeriod = "Jupiter, Venus, or Mercury Dasha",
            cancellationFactors = emptyList()
        )
    }

    /**
     * Vairagi Yoga - Detachment
     * Moon with Saturn, aspected by Ketu
     */
    private fun evaluateVairagiYoga(
        chart: VedicChart,
        houseLords: Map<Int, Planet>
    ): Yoga? {
        val moonPos = chart.planetPositions.find { it.planet == Planet.MOON } ?: return null
        val saturnPos = chart.planetPositions.find { it.planet == Planet.SATURN } ?: return null
        val ketuPos = chart.planetPositions.find { it.planet == Planet.KETU } ?: return null

        // Moon with Saturn (conjunction or aspect)
        val moonSaturnConnection = YogaHelpers.areConjunct(moonPos, saturnPos) || 
                YogaHelpers.isAspecting(saturnPos, moonPos)

        // Ketu aspect on Moon or Saturn
        val ketuInfluence = YogaHelpers.isAspecting(ketuPos, moonPos) || 
                YogaHelpers.isAspecting(ketuPos, saturnPos) ||
                YogaHelpers.areConjunct(ketuPos, moonPos) ||
                YogaHelpers.areConjunct(ketuPos, saturnPos)

        if (!moonSaturnConnection || !ketuInfluence) return null

        val positions = listOf(moonPos, saturnPos, ketuPos)
        val strengthPct = YogaHelpers.calculateYogaStrength(chart, positions)

        return Yoga(
            name = "Vairagi Yoga",
            sanskritName = "वैरागी योग",
            category = YogaCategory.SPECIAL_YOGA,
            planets = listOf(Planet.MOON, Planet.SATURN, Planet.KETU),
            houses = listOf(moonPos.house, saturnPos.house, ketuPos.house),
            description = "Moon-Saturn connection with Ketu influence",
            effects = "Natural detachment from worldly pleasures, " +
                    "emotional renunciation, past-life vairagya continues, " +
                    "may live simply despite means, spiritual maturity early, " +
                    "understands impermanence deeply",
            strength = YogaHelpers.strengthFromPercentage(strengthPct),
            strengthPercentage = strengthPct,
            isAuspicious = true,
            activationPeriod = "Saturn, Moon, or Ketu Dasha",
            cancellationFactors = listOf("May manifest as inner detachment while fulfilling duties")
        )
    }

    /**
     * Parivrajaka Yoga - Wandering Ascetic
     * Four or more planets in one sign without any planet's aspect from outside
     */
    private fun evaluateParivrajakaYoga(
        chart: VedicChart,
        houseLords: Map<Int, Planet>
    ): Yoga? {
        // Check each sign for 4+ planets
        for (sign in ZodiacSign.entries) {
            val planetsInSign = chart.planetPositions.filter { 
                it.sign == sign && it.planet !in listOf(Planet.RAHU, Planet.KETU)
            }

            if (planetsInSign.size >= 4) {
                // Check if any planet outside aspects this combination
                val planetsOutside = chart.planetPositions.filter { 
                    it.sign != sign && it.planet !in listOf(Planet.RAHU, Planet.KETU)
                }

                val anyAspect = planetsOutside.any { outside ->
                    planetsInSign.any { inside -> YogaHelpers.isAspecting(outside, inside) }
                }

                if (!anyAspect) {
                    val strengthPct = YogaHelpers.calculateYogaStrength(chart, planetsInSign)
                    val house = planetsInSign.first().house

                    return Yoga(
                        name = "Parivrajaka Yoga",
                        sanskritName = "परिव्राजक योग",
                        category = YogaCategory.SPECIAL_YOGA,
                        planets = planetsInSign.map { it.planet },
                        houses = listOf(house),
                        description = "${planetsInSign.size} planets in ${sign.displayName} without external aspects",
                        effects = "Wandering ascetic life, no fixed abode spiritually, " +
                                "pilgrimage lifestyle, teaching while traveling, " +
                                "complete independence from worldly ties, " +
                                "may literally become wandering monk",
                        strength = YogaHelpers.strengthFromPercentage(strengthPct),
                        strengthPercentage = strengthPct,
                        isAuspicious = true,
                        activationPeriod = "Dashas of planets in combination",
                        cancellationFactors = listOf("Modern context may manifest as spiritual nomad or retreat leader")
                    )
                }
            }
        }

        return null
    }

    /**
     * Siddha Yoga - Spiritual Powers
     * Jupiter in Kendra from Moon with benefic aspects
     */
    private fun evaluateSiddhaYoga(
        chart: VedicChart,
        houseLords: Map<Int, Planet>
    ): Yoga? {
        val moonPos = chart.planetPositions.find { it.planet == Planet.MOON } ?: return null
        val jupiterPos = chart.planetPositions.find { it.planet == Planet.JUPITER } ?: return null

        // Jupiter in Kendra from Moon
        val houseFromMoon = YogaHelpers.getHouseFrom(jupiterPos.sign, moonPos.sign)
        if (houseFromMoon !in listOf(1, 4, 7, 10)) return null

        // Jupiter should be strong
        val jupiterStrong = YogaHelpers.isExalted(jupiterPos) || 
                YogaHelpers.isInOwnSign(jupiterPos) || 
                YogaHelpers.isInFriendSign(jupiterPos)

        if (!jupiterStrong) return null

        // Check for benefic aspects on Jupiter
        val venusPos = chart.planetPositions.find { it.planet == Planet.VENUS }
        val mercuryPos = chart.planetPositions.find { it.planet == Planet.MERCURY }
        
        val hasBeneficAspect = (venusPos != null && YogaHelpers.isAspecting(venusPos, jupiterPos)) ||
                (mercuryPos != null && YogaHelpers.isAspecting(mercuryPos, jupiterPos))

        val positions = listOf(moonPos, jupiterPos)
        val strengthPct = YogaHelpers.calculateYogaStrength(chart, positions)
        val adjustedStrength = if (hasBeneficAspect) strengthPct * 1.15 else strengthPct

        return Yoga(
            name = "Siddha Yoga",
            sanskritName = "सिद्ध योग",
            category = YogaCategory.SPECIAL_YOGA,
            planets = listOf(Planet.JUPITER, Planet.MOON),
            houses = listOf(jupiterPos.house, moonPos.house),
            description = "Jupiter strong in Kendra from Moon (${ordinal(houseFromMoon)})",
            effects = "Spiritual powers develop naturally, siddhis through practice, " +
                    "healing abilities, intuitive wisdom, mantra siddhi, " +
                    "dreams are prophetic, guru's grace flows easily",
            strength = YogaHelpers.strengthFromPercentage(adjustedStrength.coerceIn(10.0, 100.0)),
            strengthPercentage = adjustedStrength.coerceIn(10.0, 100.0),
            isAuspicious = true,
            activationPeriod = "Jupiter Dasha, especially Jupiter-Moon period",
            cancellationFactors = listOf("Powers should be used for service, not ego")
        )
    }

    /**
     * Yogakaraka Spiritual - 5th and 9th lords together for spirituality
     */
    private fun evaluateYogakarakaSpiritual(
        chart: VedicChart,
        houseLords: Map<Int, Planet>
    ): Yoga? {
        val lord5 = houseLords[5] ?: return null
        val lord9 = houseLords[9] ?: return null

        if (lord5 == lord9) return null

        val pos5 = chart.planetPositions.find { it.planet == lord5 } ?: return null
        val pos9 = chart.planetPositions.find { it.planet == lord9 } ?: return null

        // Should be conjunct or in mutual aspect
        val isConjunct = YogaHelpers.areConjunct(pos5, pos9)
        val isMutualAspect = YogaHelpers.isAspecting(pos5, pos9) && YogaHelpers.isAspecting(pos9, pos5)

        if (!isConjunct && !isMutualAspect) return null

        // Should be in spiritual houses (5, 9, 12) or spiritual signs
        val inSpiritualHouse = pos5.house in listOf(5, 9, 12) || pos9.house in listOf(5, 9, 12)
        val inSpiritualSign = pos5.sign in SPIRITUAL_SIGNS || pos9.sign in SPIRITUAL_SIGNS

        if (!inSpiritualHouse && !inSpiritualSign) return null

        val positions = listOf(pos5, pos9)
        val strengthPct = YogaHelpers.calculateYogaStrength(chart, positions)

        return Yoga(
            name = "Adhyatma Yogakaraka",
            sanskritName = "अध्यात्म योगकारक",
            category = YogaCategory.SPECIAL_YOGA,
            planets = listOf(lord5, lord9),
            houses = listOf(5, 9, pos5.house, pos9.house),
            description = "5th and 9th lords (${lord5.displayName}, ${lord9.displayName}) connected in spiritual context",
            effects = "Past-life merit for spirituality, dharmic intelligence, " +
                    "children may be spiritual, guru blessings strong, " +
                    "mantra and tantra success, pilgrimage brings transformation",
            strength = YogaHelpers.strengthFromPercentage(strengthPct * 1.1),
            strengthPercentage = (strengthPct * 1.1).coerceIn(10.0, 100.0),
            isAuspicious = true,
            activationPeriod = "5th or 9th lord Dasha",
            cancellationFactors = emptyList()
        )
    }

    /**
     * Guru Yoga - Teacher Connection
     */
    private fun evaluateGuruYoga(
        chart: VedicChart,
        houseLords: Map<Int, Planet>
    ): List<Yoga>? {
        val yogas = mutableListOf<Yoga>()
        val jupiterPos = chart.planetPositions.find { it.planet == Planet.JUPITER } ?: return null
        val lord9 = houseLords[9] ?: return null
        val pos9 = chart.planetPositions.find { it.planet == lord9 }

        // Type 1: Jupiter in 9th house
        if (jupiterPos.house == 9) {
            val strengthPct = YogaHelpers.calculateYogaStrength(chart, listOf(jupiterPos))
            
            yogas.add(Yoga(
                name = "Guru Yoga (Jupiter in 9th)",
                sanskritName = "गुरु योग",
                category = YogaCategory.SPECIAL_YOGA,
                planets = listOf(Planet.JUPITER),
                houses = listOf(9),
                description = "Jupiter in 9th house of dharma and guru",
                effects = "Blessed with excellent teacher, guru's grace abundant, " +
                        "dharmic life, father may be spiritual, " +
                        "higher education successful, pilgrimage beneficial",
                strength = YogaHelpers.strengthFromPercentage(strengthPct * 1.1),
                strengthPercentage = (strengthPct * 1.1).coerceIn(10.0, 100.0),
                isAuspicious = true,
                activationPeriod = "Jupiter Dasha",
                cancellationFactors = emptyList()
            ))
        }

        // Type 2: Jupiter aspects 9th house lord
        if (pos9 != null && lord9 != Planet.JUPITER && YogaHelpers.isAspecting(jupiterPos, pos9)) {
            val positions = listOf(jupiterPos, pos9)
            val strengthPct = YogaHelpers.calculateYogaStrength(chart, positions)
            
            yogas.add(Yoga(
                name = "Guru-Navamesh Yoga",
                sanskritName = "गुरु-नवमेश योग",
                category = YogaCategory.SPECIAL_YOGA,
                planets = listOf(Planet.JUPITER, lord9),
                houses = listOf(9, jupiterPos.house),
                description = "Jupiter aspects 9th lord ${lord9.displayName}",
                effects = "Guru connection through dharma lord, wisdom guides fortune, " +
                        "religious education important, teacher appears when ready, " +
                        "father blessed by Jupiter",
                strength = YogaHelpers.strengthFromPercentage(strengthPct),
                strengthPercentage = strengthPct,
                isAuspicious = true,
                activationPeriod = "Jupiter or 9th lord Dasha",
                cancellationFactors = emptyList()
            ))
        }

        // Type 3: Jupiter in 5th (Guru of intellect)
        if (jupiterPos.house == 5) {
            val strengthPct = YogaHelpers.calculateYogaStrength(chart, listOf(jupiterPos))
            
            yogas.add(Yoga(
                name = "Vidya Guru Yoga",
                sanskritName = "विद्या गुरु योग",
                category = YogaCategory.SPECIAL_YOGA,
                planets = listOf(Planet.JUPITER),
                houses = listOf(5),
                description = "Jupiter in 5th house of learning",
                effects = "Teacher of wisdom and learning, children may be spiritual, " +
                        "mantra initiation powerful, past-life learning continues, " +
                        "may become teacher/guru to others",
                strength = YogaHelpers.strengthFromPercentage(strengthPct),
                strengthPercentage = strengthPct,
                isAuspicious = true,
                activationPeriod = "Jupiter Dasha",
                cancellationFactors = emptyList()
            ))
        }

        return yogas.ifEmpty { null }
    }

    /**
     * 12th House Yogas - Moksha bhava analysis
     */
    private fun evaluateTwelfthHouseYogas(
        chart: VedicChart,
        houseLords: Map<Int, Planet>
    ): List<Yoga>? {
        val yogas = mutableListOf<Yoga>()
        val planetsIn12th = chart.planetPositions.filter { it.house == 12 }

        // Jupiter in 12th
        val jupiterIn12th = planetsIn12th.find { it.planet == Planet.JUPITER }
        if (jupiterIn12th != null) {
            val strengthPct = YogaHelpers.calculateYogaStrength(chart, listOf(jupiterIn12th))
            
            yogas.add(Yoga(
                name = "Guru Vyaya Yoga",
                sanskritName = "गुरु व्यय योग",
                category = YogaCategory.SPECIAL_YOGA,
                planets = listOf(Planet.JUPITER),
                houses = listOf(12),
                description = "Jupiter in 12th house",
                effects = "Spiritual expenditure blessed, meditation comes naturally, " +
                        "foreign ashram connection, divine grace in sleep/dreams, " +
                        "moksha through wisdom, expenses on dharma fruitful",
                strength = YogaHelpers.strengthFromPercentage(strengthPct),
                strengthPercentage = strengthPct,
                isAuspicious = true,
                activationPeriod = "Jupiter Dasha, especially later in life",
                cancellationFactors = emptyList()
            ))
        }

        // Venus in 12th (spiritual love)
        val venusIn12th = planetsIn12th.find { it.planet == Planet.VENUS }
        if (venusIn12th != null) {
            val strengthPct = YogaHelpers.calculateYogaStrength(chart, listOf(venusIn12th))
            
            yogas.add(Yoga(
                name = "Shukra Vyaya Yoga",
                sanskritName = "शुक्र व्यय योग",
                category = YogaCategory.SPECIAL_YOGA,
                planets = listOf(Planet.VENUS),
                houses = listOf(12),
                description = "Venus in 12th house",
                effects = "Devotional spirituality, bhakti yoga success, " +
                        "divine love experienced, pleasures through spiritual means, " +
                        "bedroom comforts, foreign luxuries, " +
                        "may worship divine feminine",
                strength = YogaHelpers.strengthFromPercentage(strengthPct),
                strengthPercentage = strengthPct,
                isAuspicious = true,
                activationPeriod = "Venus Dasha",
                cancellationFactors = listOf("Material desires may need transcending")
            ))
        }

        // Saturn in 12th (karma yoga for liberation)
        val saturnIn12th = planetsIn12th.find { it.planet == Planet.SATURN }
        if (saturnIn12th != null) {
            val strengthPct = YogaHelpers.calculateYogaStrength(chart, listOf(saturnIn12th))
            
            yogas.add(Yoga(
                name = "Shani Vyaya Yoga",
                sanskritName = "शनि व्यय योग",
                category = YogaCategory.SPECIAL_YOGA,
                planets = listOf(Planet.SATURN),
                houses = listOf(12),
                description = "Saturn in 12th house",
                effects = "Liberation through service, karma yoga path, " +
                        "past-life karmas cleared, solitude beneficial, " +
                        "may work in hospitals/prisons/ashrams, " +
                        "long meditation retreats fruitful",
                strength = YogaHelpers.strengthFromPercentage(strengthPct),
                strengthPercentage = strengthPct,
                isAuspicious = true,
                activationPeriod = "Saturn Dasha, especially later life",
                cancellationFactors = listOf("May experience isolation before liberation")
            ))
        }

        return yogas.ifEmpty { null }
    }

    /**
     * Ketu-based Spiritual Yogas
     */
    private fun evaluateKetuSpiritualYogas(
        chart: VedicChart,
        houseLords: Map<Int, Planet>
    ): List<Yoga>? {
        val yogas = mutableListOf<Yoga>()
        val ketuPos = chart.planetPositions.find { it.planet == Planet.KETU } ?: return null

        // Ketu in 5th (past-life spiritual merits)
        if (ketuPos.house == 5) {
            yogas.add(Yoga(
                name = "Ketu Purvapunya Yoga",
                sanskritName = "केतु पूर्वपुण्य योग",
                category = YogaCategory.SPECIAL_YOGA,
                planets = listOf(Planet.KETU),
                houses = listOf(5),
                description = "Ketu in 5th house of past-life merit",
                effects = "Past-life spiritual practices active, intuitive intelligence, " +
                        "may remember past lives, children spiritually inclined, " +
                        "speculation through intuition, natural meditation ability",
                strength = YogaStrength.STRONG,
                strengthPercentage = 65.0,
                isAuspicious = true,
                activationPeriod = "Ketu Dasha",
                cancellationFactors = emptyList()
            ))
        }

        // Ketu in 9th (spiritual seeker from past)
        if (ketuPos.house == 9) {
            yogas.add(Yoga(
                name = "Ketu Dharma Yoga",
                sanskritName = "केतु धर्म योग",
                category = YogaCategory.SPECIAL_YOGA,
                planets = listOf(Planet.KETU),
                houses = listOf(9),
                description = "Ketu in 9th house of dharma",
                effects = "Past-life spiritual seeker, may reject traditional religion, " +
                        "direct experience preferred over belief, unusual guru, " +
                        "pilgrimage to remote places, father may be spiritual or distant",
                strength = YogaStrength.STRONG,
                strengthPercentage = 65.0,
                isAuspicious = true,
                activationPeriod = "Ketu Dasha",
                cancellationFactors = emptyList()
            ))
        }

        // Ketu in 12th (moksha karaka in moksha house)
        if (ketuPos.house == 12) {
            yogas.add(Yoga(
                name = "Ketu Moksha Karaka Yoga",
                sanskritName = "केतु मोक्ष कारक योग",
                category = YogaCategory.SPECIAL_YOGA,
                planets = listOf(Planet.KETU),
                houses = listOf(12),
                description = "Ketu (moksha karaka) in 12th house (moksha bhava)",
                effects = "Strong liberation potential, spiritual past-life, " +
                        "natural detachment, dreams significant, " +
                        "may achieve enlightenment, foreign spiritual connections, " +
                        "final life cycle possible",
                strength = YogaStrength.VERY_STRONG,
                strengthPercentage = 75.0,
                isAuspicious = true,
                activationPeriod = "Ketu Dasha - significant spiritual events",
                cancellationFactors = emptyList()
            ))
        }

        // Ketu with Jupiter (spiritual wisdom)
        val jupiterPos = chart.planetPositions.find { it.planet == Planet.JUPITER }
        if (jupiterPos != null && YogaHelpers.areConjunct(ketuPos, jupiterPos)) {
            val positions = listOf(ketuPos, jupiterPos)
            val strengthPct = YogaHelpers.calculateYogaStrength(chart, positions)
            
            yogas.add(Yoga(
                name = "Ketu-Guru Yoga",
                sanskritName = "केतु-गुरु योग",
                category = YogaCategory.SPECIAL_YOGA,
                planets = listOf(Planet.KETU, Planet.JUPITER),
                houses = listOf(ketuPos.house),
                description = "Ketu conjunct Jupiter in ${ordinal(ketuPos.house)} house",
                effects = "Spiritual wisdom from past lives, unconventional teacher, " +
                        "may become spiritual guide, intuitive knowledge of scriptures, " +
                        "natural philosopher, detached wisdom",
                strength = YogaHelpers.strengthFromPercentage(strengthPct),
                strengthPercentage = strengthPct,
                isAuspicious = true,
                activationPeriod = "Ketu or Jupiter Dasha",
                cancellationFactors = listOf("May challenge traditional Jupiter significations initially")
            ))
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
}
