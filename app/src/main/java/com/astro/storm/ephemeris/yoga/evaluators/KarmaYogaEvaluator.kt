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
 * Karma (Career/Profession) Yoga Evaluator
 *
 * Evaluates all career, profession, and status-related yogas based on classical Vedic texts.
 * The 10th house (Karma Bhava) is the primary house for career, along with 2nd (wealth from work),
 * 6th (service/employment), and 11th (gains/income) houses.
 *
 * Yogas Evaluated:
 * - Rajya Yoga (Government/Authority)
 * - Amatya Yoga (Minister/Advisor Position)
 * - Bhagya-Karma Yoga (Fortune-Career Connection)
 * - Vyapara Yoga (Business/Trade Success)
 * - Vidya Yoga (Education/Knowledge Career)
 * - Karmajiva Yoga (Professional Excellence)
 * - Chatussagara Yoga (Global Recognition)
 * - Parvata Yoga (Mountain of Success)
 * - Kahala Yoga (Career Courage)
 * - Shasha-Malavya Yoga (Authority through Service)
 * - Ubhayachari Yoga (Career with Sun)
 * - Nipuna Yoga (Skill/Expertise)
 * - Saraswati Yoga (Intellectual Profession)
 * - Budhaditya Yoga (Intelligence in Career)
 *
 * Based on:
 * - Brihat Parashara Hora Shastra (BPHS)
 * - Phaladeepika
 * - Jataka Parijata
 * - Saravali
 *
 * @author AstroStorm
 */
class KarmaYogaEvaluator : YogaEvaluator {

    override val category: YogaCategory = YogaCategory.SPECIAL_YOGA

    companion object {
        private val KENDRA_HOUSES = setOf(1, 4, 7, 10)
        private val TRIKONA_HOUSES = setOf(1, 5, 9)
        private val UPACHAYA_HOUSES = setOf(3, 6, 10, 11)
        private val DUSTHANA_HOUSES = setOf(6, 8, 12)
        
        // Profession indicators by planet
        private val PROFESSION_INDICATORS = mapOf(
            Planet.SUN to "government, authority, administration, medicine, politics",
            Planet.MOON to "public service, hospitality, nursing, liquids, travel, psychology",
            Planet.MARS to "military, police, engineering, surgery, sports, real estate",
            Planet.MERCURY to "commerce, writing, communication, accounting, IT, journalism",
            Planet.JUPITER to "teaching, law, religion, counseling, finance, philosophy",
            Planet.VENUS to "arts, entertainment, luxury goods, fashion, beauty, hospitality",
            Planet.SATURN to "labor, mining, agriculture, construction, manufacturing, elderly care",
            Planet.RAHU to "foreign affairs, technology, aviation, unconventional fields, research",
            Planet.KETU to "spirituality, alternative healing, research, occult sciences"
        )
    }

    override fun evaluate(chart: VedicChart): List<Yoga> {
        val yogas = mutableListOf<Yoga>()
        val ascendantSign = ZodiacSign.fromLongitude(chart.ascendant)
        val houseLords = YogaHelpers.getHouseLords(ascendantSign)

        // Core Career Yogas
        evaluateRajyaYoga(chart, houseLords)?.let { yogas.addAll(it) }
        evaluateAmatyaYoga(chart, houseLords)?.let { yogas.add(it) }
        evaluateBhagyaKarmaYoga(chart, houseLords)?.let { yogas.add(it) }
        evaluateVyaparaYoga(chart, houseLords)?.let { yogas.addAll(it) }
        
        // Intellectual/Skill Yogas
        evaluateSaraswatiYoga(chart, houseLords)?.let { yogas.add(it) }
        evaluateBudhAdityaKarmaYoga(chart, houseLords)?.let { yogas.add(it) }
        evaluateNipunaYoga(chart, houseLords)?.let { yogas.add(it) }
        
        // Authority/Recognition Yogas
        evaluateChatussagaraYoga(chart, houseLords)?.let { yogas.add(it) }
        evaluateParvataYoga(chart, houseLords)?.let { yogas.add(it) }
        evaluateKahalaKarmaYoga(chart, houseLords)?.let { yogas.add(it) }
        
        // Sun-based Career Yogas
        evaluateUbhayachariYoga(chart, houseLords)?.let { yogas.add(it) }
        evaluateVesiVasiYoga(chart, houseLords)?.let { yogas.addAll(it) }
        
        // 10th Lord Position Yogas
        evaluateTenthLordYogas(chart, houseLords)?.let { yogas.addAll(it) }
        
        // Karmajiva (Professional Excellence) Yoga
        evaluateKarmajivaYoga(chart, houseLords)?.let { yogas.add(it) }
        
        // Special Career Yogas
        evaluateKhadgaYoga(chart, houseLords)?.let { yogas.add(it) }
        evaluateLakshmiYoga(chart, houseLords)?.let { yogas.add(it) }
        evaluateGajaKesariKarmaYoga(chart, houseLords)?.let { yogas.add(it) }

        return yogas
    }

    /**
     * Rajya Yoga - Government/Authority Position
     * Multiple variants based on different combinations
     */
    private fun evaluateRajyaYoga(
        chart: VedicChart,
        houseLords: Map<Int, Planet>
    ): List<Yoga>? {
        val yogas = mutableListOf<Yoga>()

        val lord9 = houseLords[9] ?: return null
        val lord10 = houseLords[10] ?: return null
        val lord1 = houseLords[1] ?: return null
        val lord4 = houseLords[4] ?: return null
        val lord5 = houseLords[5] ?: return null

        val pos9 = chart.planetPositions.find { it.planet == lord9 }
        val pos10 = chart.planetPositions.find { it.planet == lord10 }
        val pos1 = chart.planetPositions.find { it.planet == lord1 }
        val pos4 = chart.planetPositions.find { it.planet == lord4 }
        val pos5 = chart.planetPositions.find { it.planet == lord5 }

        // Type 1: 9th and 10th lords in conjunction or mutual aspect
        if (pos9 != null && pos10 != null) {
            val isConjunct = YogaHelpers.areConjunct(pos9, pos10)
            val isMutualAspect = YogaHelpers.isAspecting(pos9, pos10) && YogaHelpers.isAspecting(pos10, pos9)
            val isExchange = YogaHelpers.areInExchange(pos9, pos10)

            if (isConjunct || isMutualAspect || isExchange) {
                val positions = listOf(pos9, pos10)
                val (strengthPct, cancellations) = YogaHelpers.calculateYogaStrengthWithReasons(chart, positions)
                
                // Boost for specific conditions
                var adjustedStrength = strengthPct
                if (isExchange) adjustedStrength *= 1.2
                if (pos9.house in KENDRA_HOUSES || pos10.house in KENDRA_HOUSES) adjustedStrength *= 1.15
                if (YogaHelpers.isExalted(pos9) || YogaHelpers.isExalted(pos10)) adjustedStrength *= 1.1

                val connectionType = when {
                    isExchange -> "mutual exchange"
                    isConjunct -> "conjunction"
                    else -> "mutual aspect"
                }

                yogas.add(Yoga(
                    name = "Dharma-Karmadhipati Rajya Yoga",
                    sanskritName = "धर्म-कर्माधिपति राज्य योग",
                    category = YogaCategory.SPECIAL_YOGA,
                    planets = listOf(lord9, lord10),
                    houses = listOf(9, 10),
                    description = "9th lord ${lord9.displayName} and 10th lord ${lord10.displayName} in $connectionType",
                    effects = "Government position, high authority, success in administration, political career, " +
                            "recognition from authorities, father's support in career, righteous professional conduct",
                    strength = YogaHelpers.strengthFromPercentage(adjustedStrength.coerceIn(10.0, 100.0)),
                    strengthPercentage = adjustedStrength.coerceIn(10.0, 100.0),
                    isAuspicious = true,
                    activationPeriod = "Dasha of ${lord9.displayName} or ${lord10.displayName}",
                    cancellationFactors = cancellations.ifEmpty { listOf("None - yoga is unafflicted") }
                ))
            }
        }

        // Type 2: Lagna lord and 10th lord connection
        if (pos1 != null && pos10 != null && lord1 != lord10) {
            val isConjunct = YogaHelpers.areConjunct(pos1, pos10)
            val isMutualAspect = YogaHelpers.isAspecting(pos1, pos10) && YogaHelpers.isAspecting(pos10, pos1)

            if (isConjunct || isMutualAspect) {
                val positions = listOf(pos1, pos10)
                val strengthPct = YogaHelpers.calculateYogaStrength(chart, positions)

                yogas.add(Yoga(
                    name = "Atma-Karma Rajya Yoga",
                    sanskritName = "आत्म-कर्म राज्य योग",
                    category = YogaCategory.SPECIAL_YOGA,
                    planets = listOf(lord1, lord10),
                    houses = listOf(1, 10),
                    description = "Lagna lord ${lord1.displayName} connected with 10th lord ${lord10.displayName}",
                    effects = "Self-made career success, personal efforts leading to authority, " +
                            "strong professional identity, leadership in chosen field",
                    strength = YogaHelpers.strengthFromPercentage(strengthPct),
                    strengthPercentage = strengthPct,
                    isAuspicious = true,
                    activationPeriod = "Dasha of ${lord1.displayName} or ${lord10.displayName}",
                    cancellationFactors = emptyList()
                ))
            }
        }

        // Type 3: 4th and 10th lords connection (Sukha-Karma)
        if (pos4 != null && pos10 != null && lord4 != lord10) {
            val isConjunct = YogaHelpers.areConjunct(pos4, pos10)
            
            if (isConjunct) {
                val positions = listOf(pos4, pos10)
                val strengthPct = YogaHelpers.calculateYogaStrength(chart, positions)

                yogas.add(Yoga(
                    name = "Sukha-Karma Yoga",
                    sanskritName = "सुख-कर्म योग",
                    category = YogaCategory.SPECIAL_YOGA,
                    planets = listOf(lord4, lord10),
                    houses = listOf(4, 10),
                    description = "4th lord ${lord4.displayName} conjunct 10th lord ${lord10.displayName}",
                    effects = "Career brings happiness, work from home success, real estate profession, " +
                            "property through career, vehicles from work, emotional satisfaction in profession",
                    strength = YogaHelpers.strengthFromPercentage(strengthPct),
                    strengthPercentage = strengthPct,
                    isAuspicious = true,
                    activationPeriod = "Dasha of ${lord4.displayName} or ${lord10.displayName}",
                    cancellationFactors = emptyList()
                ))
            }
        }

        // Type 4: 5th and 10th lords connection (Purva Punya Karma)
        if (pos5 != null && pos10 != null && lord5 != lord10) {
            val isConjunct = YogaHelpers.areConjunct(pos5, pos10)
            val isExchange = YogaHelpers.areInExchange(pos5, pos10)

            if (isConjunct || isExchange) {
                val positions = listOf(pos5, pos10)
                val strengthPct = YogaHelpers.calculateYogaStrength(chart, positions)

                yogas.add(Yoga(
                    name = "Purva-Punya Karma Yoga",
                    sanskritName = "पूर्व-पुण्य कर्म योग",
                    category = YogaCategory.SPECIAL_YOGA,
                    planets = listOf(lord5, lord10),
                    houses = listOf(5, 10),
                    description = "5th lord ${lord5.displayName} connected with 10th lord ${lord10.displayName}",
                    effects = "Past life merit supports career, creative profession, children support career, " +
                            "speculative success in profession, mantra siddhi aids career, intelligence in work",
                    strength = YogaHelpers.strengthFromPercentage(strengthPct),
                    strengthPercentage = strengthPct,
                    isAuspicious = true,
                    activationPeriod = "Dasha of ${lord5.displayName} or ${lord10.displayName}",
                    cancellationFactors = emptyList()
                ))
            }
        }

        return yogas.ifEmpty { null }
    }

    /**
     * Amatya Yoga - Minister/Advisor Position
     * Based on the strongest planet after Atmakaraka
     */
    private fun evaluateAmatyaYoga(
        chart: VedicChart,
        houseLords: Map<Int, Planet>
    ): Yoga? {
        // Find Amatyakaraka (planet with second highest degree)
        val planetDegrees = chart.planetPositions
            .filter { it.planet !in listOf(Planet.RAHU, Planet.KETU) }
            .map { Pair(it.planet, it.longitude % 30) }
            .sortedByDescending { it.second }

        if (planetDegrees.size < 2) return null

        val amatyakaraka = planetDegrees[1].first
        val amatyaPos = chart.planetPositions.find { it.planet == amatyakaraka } ?: return null

        // Amatya Yoga is strong when Amatyakaraka is well-placed
        val isStrongPlacement = amatyaPos.house in KENDRA_HOUSES || amatyaPos.house in TRIKONA_HOUSES
        val isDignified = YogaHelpers.isExalted(amatyaPos) || YogaHelpers.isInOwnSign(amatyaPos)

        if (!isStrongPlacement && !isDignified) return null

        val strengthPct = YogaHelpers.calculateYogaStrength(chart, listOf(amatyaPos))
        var adjustedStrength = strengthPct

        // Boost if Amatyakaraka is in 10th house
        if (amatyaPos.house == 10) adjustedStrength *= 1.25
        // Boost if aspecting 10th house
        val tenthHouseSign = ZodiacSign.entries[(ZodiacSign.fromLongitude(chart.ascendant).ordinal + 9) % 12]
        val tenthHouseLongitude = tenthHouseSign.ordinal * 30.0 + 15.0
        val dummyPos = amatyaPos.copy(longitude = tenthHouseLongitude)
        if (YogaHelpers.isAspecting(amatyaPos, dummyPos)) adjustedStrength *= 1.1

        val profession = PROFESSION_INDICATORS[amatyakaraka] ?: "advisory role"

        return Yoga(
            name = "Amatya Yoga",
            sanskritName = "अमात्य योग",
            category = YogaCategory.SPECIAL_YOGA,
            planets = listOf(amatyakaraka),
            houses = listOf(amatyaPos.house),
            description = "Amatyakaraka ${amatyakaraka.displayName} strongly placed in ${ordinal(amatyaPos.house)} house",
            effects = "Advisor to powerful people, ministerial position, consultant, " +
                    "influential role in organizations, trusted counselor. Career in: $profession",
            strength = YogaHelpers.strengthFromPercentage(adjustedStrength.coerceIn(10.0, 100.0)),
            strengthPercentage = adjustedStrength.coerceIn(10.0, 100.0),
            isAuspicious = true,
            activationPeriod = "Dasha of ${amatyakaraka.displayName}",
            cancellationFactors = emptyList()
        )
    }

    /**
     * Bhagya-Karma Yoga - Fortune-Career Connection
     * 9th lord in 10th or 10th lord in 9th
     */
    private fun evaluateBhagyaKarmaYoga(
        chart: VedicChart,
        houseLords: Map<Int, Planet>
    ): Yoga? {
        val lord9 = houseLords[9] ?: return null
        val lord10 = houseLords[10] ?: return null

        val pos9 = chart.planetPositions.find { it.planet == lord9 } ?: return null
        val pos10 = chart.planetPositions.find { it.planet == lord10 } ?: return null

        val isFormed = when {
            pos9.house == 10 -> true // 9th lord in 10th
            pos10.house == 9 -> true // 10th lord in 9th
            else -> false
        }

        if (!isFormed) return null

        val (relevantPos, description) = when {
            pos9.house == 10 -> Pair(pos9, "9th lord ${lord9.displayName} in 10th house")
            else -> Pair(pos10, "10th lord ${lord10.displayName} in 9th house")
        }

        val strengthPct = YogaHelpers.calculateYogaStrength(chart, listOf(relevantPos))
        var adjustedStrength = strengthPct

        // Boost for dignified position
        if (YogaHelpers.isExalted(relevantPos) || YogaHelpers.isInOwnSign(relevantPos)) {
            adjustedStrength *= 1.2
        }

        return Yoga(
            name = "Bhagya-Karma Yoga",
            sanskritName = "भाग्य-कर्म योग",
            category = YogaCategory.SPECIAL_YOGA,
            planets = listOf(lord9, lord10),
            houses = listOf(9, 10),
            description = description,
            effects = "Fortune supports career, luck in profession, father helps career, " +
                    "religious/philosophical profession, higher education benefits career, " +
                    "career brings dharmic satisfaction, foreign opportunities in profession",
            strength = YogaHelpers.strengthFromPercentage(adjustedStrength.coerceIn(10.0, 100.0)),
            strengthPercentage = adjustedStrength.coerceIn(10.0, 100.0),
            isAuspicious = true,
            activationPeriod = "Dasha of ${lord9.displayName} or ${lord10.displayName}",
            cancellationFactors = emptyList()
        )
    }

    /**
     * Vyapara Yoga - Business/Trade Success
     * Multiple combinations involving 2nd, 7th, 10th, 11th houses
     */
    private fun evaluateVyaparaYoga(
        chart: VedicChart,
        houseLords: Map<Int, Planet>
    ): List<Yoga>? {
        val yogas = mutableListOf<Yoga>()

        val lord2 = houseLords[2] ?: return null
        val lord7 = houseLords[7] ?: return null
        val lord10 = houseLords[10] ?: return null
        val lord11 = houseLords[11] ?: return null

        val pos2 = chart.planetPositions.find { it.planet == lord2 }
        val pos7 = chart.planetPositions.find { it.planet == lord7 }
        val pos10 = chart.planetPositions.find { it.planet == lord10 }
        val pos11 = chart.planetPositions.find { it.planet == lord11 }

        val mercuryPos = chart.planetPositions.find { it.planet == Planet.MERCURY }

        // Type 1: Mercury strong in 2nd, 7th, 10th, or 11th
        if (mercuryPos != null && mercuryPos.house in listOf(2, 7, 10, 11)) {
            val isDignified = YogaHelpers.isExalted(mercuryPos) || YogaHelpers.isInOwnSign(mercuryPos)
            val isNotCombust = YogaHelpers.getCombustionFactor(mercuryPos, chart) > 0.7

            if (isDignified || (isNotCombust && mercuryPos.house == 10)) {
                val strengthPct = YogaHelpers.calculateYogaStrength(chart, listOf(mercuryPos))

                yogas.add(Yoga(
                    name = "Vanik Yoga",
                    sanskritName = "वाणिक योग",
                    category = YogaCategory.SPECIAL_YOGA,
                    planets = listOf(Planet.MERCURY),
                    houses = listOf(mercuryPos.house),
                    description = "Mercury well-placed in ${ordinal(mercuryPos.house)} house",
                    effects = "Natural trader, business acumen, success in commerce, " +
                            "communication-based business, writing/publishing success, " +
                            "accounting/finance profession, merchant success",
                    strength = YogaHelpers.strengthFromPercentage(strengthPct),
                    strengthPercentage = strengthPct,
                    isAuspicious = true,
                    activationPeriod = "Mercury Dasha/Antardasha",
                    cancellationFactors = emptyList()
                ))
            }
        }

        // Type 2: 2nd and 11th lords exchange or conjunct
        if (pos2 != null && pos11 != null && lord2 != lord11) {
            val isConjunct = YogaHelpers.areConjunct(pos2, pos11)
            val isExchange = YogaHelpers.areInExchange(pos2, pos11)

            if (isConjunct || isExchange) {
                val positions = listOf(pos2, pos11)
                val strengthPct = YogaHelpers.calculateYogaStrength(chart, positions)

                yogas.add(Yoga(
                    name = "Dhana-Labha Vyapara Yoga",
                    sanskritName = "धन-लाभ व्यापार योग",
                    category = YogaCategory.SPECIAL_YOGA,
                    planets = listOf(lord2, lord11),
                    houses = listOf(2, 11),
                    description = "2nd lord ${lord2.displayName} and 11th lord ${lord11.displayName} connected",
                    effects = "Business brings wealth, income from multiple sources, " +
                            "family business success, investments yield gains, " +
                            "networking brings profit",
                    strength = YogaHelpers.strengthFromPercentage(strengthPct),
                    strengthPercentage = strengthPct,
                    isAuspicious = true,
                    activationPeriod = "Dasha of ${lord2.displayName} or ${lord11.displayName}",
                    cancellationFactors = emptyList()
                ))
            }
        }

        // Type 3: 7th and 10th lords connection (Partnership business)
        if (pos7 != null && pos10 != null && lord7 != lord10) {
            val isConjunct = YogaHelpers.areConjunct(pos7, pos10)

            if (isConjunct) {
                val positions = listOf(pos7, pos10)
                val strengthPct = YogaHelpers.calculateYogaStrength(chart, positions)

                yogas.add(Yoga(
                    name = "Sahakari Vyapara Yoga",
                    sanskritName = "सहकारी व्यापार योग",
                    category = YogaCategory.SPECIAL_YOGA,
                    planets = listOf(lord7, lord10),
                    houses = listOf(7, 10),
                    description = "7th lord ${lord7.displayName} conjunct 10th lord ${lord10.displayName}",
                    effects = "Partnership business success, spouse aids career, " +
                            "business through contracts, success in joint ventures, " +
                            "public-facing profession, diplomatic career",
                    strength = YogaHelpers.strengthFromPercentage(strengthPct),
                    strengthPercentage = strengthPct,
                    isAuspicious = true,
                    activationPeriod = "Dasha of ${lord7.displayName} or ${lord10.displayName}",
                    cancellationFactors = emptyList()
                ))
            }
        }

        return yogas.ifEmpty { null }
    }

    /**
     * Saraswati Yoga - Intellectual/Educational Profession
     * Jupiter, Venus, Mercury in Kendra/Trikona with Jupiter strong
     */
    private fun evaluateSaraswatiYoga(
        chart: VedicChart,
        houseLords: Map<Int, Planet>
    ): Yoga? {
        val jupiterPos = chart.planetPositions.find { it.planet == Planet.JUPITER } ?: return null
        val venusPos = chart.planetPositions.find { it.planet == Planet.VENUS } ?: return null
        val mercuryPos = chart.planetPositions.find { it.planet == Planet.MERCURY } ?: return null

        val goodHouses = KENDRA_HOUSES + TRIKONA_HOUSES

        // All three must be in Kendra or Trikona
        val allInGoodHouses = jupiterPos.house in goodHouses &&
                venusPos.house in goodHouses &&
                mercuryPos.house in goodHouses

        if (!allInGoodHouses) return null

        // Jupiter should be strong (own sign, exalted, or friend's sign)
        val jupiterStrong = YogaHelpers.isExalted(jupiterPos) || 
                YogaHelpers.isInOwnSign(jupiterPos) || 
                YogaHelpers.isInFriendSign(jupiterPos)

        if (!jupiterStrong) return null

        val positions = listOf(jupiterPos, venusPos, mercuryPos)
        val strengthPct = YogaHelpers.calculateYogaStrength(chart, positions)

        return Yoga(
            name = "Saraswati Yoga",
            sanskritName = "सरस्वती योग",
            category = YogaCategory.SPECIAL_YOGA,
            planets = listOf(Planet.JUPITER, Planet.VENUS, Planet.MERCURY),
            houses = listOf(jupiterPos.house, venusPos.house, mercuryPos.house),
            description = "Jupiter, Venus, Mercury in Kendra/Trikona with strong Jupiter",
            effects = "Exceptional learning ability, academic career, teaching profession, " +
                    "writing and publishing success, scholarly recognition, " +
                    "expertise in arts and sciences, wisdom in profession, " +
                    "success in education sector, intellectual authority",
            strength = YogaHelpers.strengthFromPercentage(strengthPct),
            strengthPercentage = strengthPct,
            isAuspicious = true,
            activationPeriod = "Jupiter, Venus, or Mercury Dasha",
            cancellationFactors = emptyList()
        )
    }

    /**
     * Budha-Aditya Yoga in career context
     * Sun-Mercury conjunction affecting 10th house
     */
    private fun evaluateBudhAdityaKarmaYoga(
        chart: VedicChart,
        houseLords: Map<Int, Planet>
    ): Yoga? {
        val sunPos = chart.planetPositions.find { it.planet == Planet.SUN } ?: return null
        val mercuryPos = chart.planetPositions.find { it.planet == Planet.MERCURY } ?: return null

        // Must be conjunct
        if (!YogaHelpers.areConjunct(sunPos, mercuryPos)) return null

        // Should be in or aspecting 10th house, or one of them is 10th lord
        val lord10 = houseLords[10]
        val relevantToCareer = sunPos.house == 10 || 
                mercuryPos.house == 10 || 
                lord10 in listOf(Planet.SUN, Planet.MERCURY)

        if (!relevantToCareer) return null

        // Mercury should not be too combust (within 3 degrees)
        val combustionFactor = YogaHelpers.getCombustionFactor(mercuryPos, chart)
        if (combustionFactor < 0.4) return null

        val positions = listOf(sunPos, mercuryPos)
        val (strengthPct, cancellations) = YogaHelpers.calculateYogaStrengthWithReasons(chart, positions)

        return Yoga(
            name = "Budha-Aditya Karma Yoga",
            sanskritName = "बुध-आदित्य कर्म योग",
            category = YogaCategory.SPECIAL_YOGA,
            planets = listOf(Planet.SUN, Planet.MERCURY),
            houses = listOf(sunPos.house),
            description = "Sun-Mercury conjunction influencing career (${ordinal(sunPos.house)} house)",
            effects = "Intelligence in career, government job with communication role, " +
                    "administrative ability, writing for authority, " +
                    "success through analytical skills, royal messenger role, " +
                    "career in education under government",
            strength = YogaHelpers.strengthFromPercentage(strengthPct),
            strengthPercentage = strengthPct,
            isAuspicious = true,
            activationPeriod = "Sun or Mercury Dasha",
            cancellationFactors = cancellations.ifEmpty { listOf("None - yoga is unafflicted") }
        )
    }

    /**
     * Nipuna Yoga - Skill/Expertise
     * Mercury in own/exalted sign in Kendra from Lagna or Moon
     */
    private fun evaluateNipunaYoga(
        chart: VedicChart,
        houseLords: Map<Int, Planet>
    ): Yoga? {
        val mercuryPos = chart.planetPositions.find { it.planet == Planet.MERCURY } ?: return null
        val moonPos = chart.planetPositions.find { it.planet == Planet.MOON } ?: return null

        // Mercury must be in own sign (Gemini, Virgo) or exalted (Virgo)
        val mercuryDignified = YogaHelpers.isExalted(mercuryPos) || YogaHelpers.isInOwnSign(mercuryPos)
        if (!mercuryDignified) return null

        // Must be in Kendra from Lagna
        val inKendraFromLagna = mercuryPos.house in KENDRA_HOUSES

        // Or in Kendra from Moon
        val houseFromMoon = YogaHelpers.getHouseFrom(mercuryPos.sign, moonPos.sign)
        val inKendraFromMoon = houseFromMoon in listOf(1, 4, 7, 10)

        if (!inKendraFromLagna && !inKendraFromMoon) return null

        val strengthPct = YogaHelpers.calculateYogaStrength(chart, listOf(mercuryPos))

        return Yoga(
            name = "Nipuna Yoga",
            sanskritName = "निपुण योग",
            category = YogaCategory.SPECIAL_YOGA,
            planets = listOf(Planet.MERCURY),
            houses = listOf(mercuryPos.house),
            description = "Mercury dignified in Kendra (${ordinal(mercuryPos.house)} house)",
            effects = "Expert skills, technical proficiency, versatility in profession, " +
                    "quick learning ability, success through expertise, " +
                    "consulting success, craftsmanship, analytical career, " +
                    "programming/IT success, accounting excellence",
            strength = YogaHelpers.strengthFromPercentage(strengthPct),
            strengthPercentage = strengthPct,
            isAuspicious = true,
            activationPeriod = "Mercury Dasha/Antardasha",
            cancellationFactors = emptyList()
        )
    }

    /**
     * Chatussagara Yoga - Global Recognition
     * All Kendras occupied by planets
     */
    private fun evaluateChatussagaraYoga(
        chart: VedicChart,
        houseLords: Map<Int, Planet>
    ): Yoga? {
        val kendraHouses = listOf(1, 4, 7, 10)
        
        // Check if all Kendras have at least one planet
        val allKendrasOccupied = kendraHouses.all { house ->
            chart.planetPositions.any { it.house == house && it.planet !in listOf(Planet.RAHU, Planet.KETU) }
        }

        if (!allKendrasOccupied) return null

        // Get all planets in Kendras
        val planetsInKendras = chart.planetPositions.filter { 
            it.house in kendraHouses && it.planet !in listOf(Planet.RAHU, Planet.KETU)
        }

        val strengthPct = YogaHelpers.calculateYogaStrength(chart, planetsInKendras)

        // Boost if benefics in Kendras
        val beneficsInKendras = planetsInKendras.count { 
            it.planet in listOf(Planet.JUPITER, Planet.VENUS, Planet.MERCURY, Planet.MOON) 
        }
        val adjustedStrength = strengthPct * (1 + beneficsInKendras * 0.05)

        return Yoga(
            name = "Chatussagara Yoga",
            sanskritName = "चतुस्सागर योग",
            category = YogaCategory.SPECIAL_YOGA,
            planets = planetsInKendras.map { it.planet },
            houses = kendraHouses,
            description = "All four Kendras (1, 4, 7, 10) occupied by planets",
            effects = "Fame spreading in all directions, global recognition, " +
                    "international career, worldwide reputation, " +
                    "influential position, respect from all quarters, " +
                    "success in multiple domains, comprehensive achievement",
            strength = YogaHelpers.strengthFromPercentage(adjustedStrength.coerceIn(10.0, 100.0)),
            strengthPercentage = adjustedStrength.coerceIn(10.0, 100.0),
            isAuspicious = true,
            activationPeriod = "Dashas of planets in Kendras",
            cancellationFactors = emptyList()
        )
    }

    /**
     * Parvata Yoga - Mountain of Success
     * Benefics in Kendras, 6th and 8th houses empty
     */
    private fun evaluateParvataYoga(
        chart: VedicChart,
        houseLords: Map<Int, Planet>
    ): Yoga? {
        val benefics = listOf(Planet.JUPITER, Planet.VENUS, Planet.MERCURY, Planet.MOON)
        
        // Check for benefics in Kendras
        val beneficsInKendras = chart.planetPositions.filter {
            it.planet in benefics && it.house in KENDRA_HOUSES
        }

        if (beneficsInKendras.isEmpty()) return null

        // Check 6th and 8th houses are empty (no major planets)
        val majorPlanets = listOf(Planet.SUN, Planet.MOON, Planet.MARS, Planet.MERCURY, 
                Planet.JUPITER, Planet.VENUS, Planet.SATURN)
        
        val sixthEmpty = chart.planetPositions.none { it.planet in majorPlanets && it.house == 6 }
        val eighthEmpty = chart.planetPositions.none { it.planet in majorPlanets && it.house == 8 }

        if (!sixthEmpty || !eighthEmpty) return null

        val strengthPct = YogaHelpers.calculateYogaStrength(chart, beneficsInKendras)

        return Yoga(
            name = "Parvata Yoga",
            sanskritName = "पर्वत योग",
            category = YogaCategory.SPECIAL_YOGA,
            planets = beneficsInKendras.map { it.planet },
            houses = beneficsInKendras.map { it.house },
            description = "Benefics in Kendras with empty 6th and 8th houses",
            effects = "Steady rise in career like a mountain, unobstructed success, " +
                    "few enemies, stable health, progressive achievement, " +
                    "lasting fame, firm foundation in career, " +
                    "reliable professional growth",
            strength = YogaHelpers.strengthFromPercentage(strengthPct),
            strengthPercentage = strengthPct,
            isAuspicious = true,
            activationPeriod = "Dashas of benefics in Kendras",
            cancellationFactors = emptyList()
        )
    }

    /**
     * Kahala Karma Yoga - Career Courage
     * 4th and 9th lords in mutual Kendra
     */
    private fun evaluateKahalaKarmaYoga(
        chart: VedicChart,
        houseLords: Map<Int, Planet>
    ): Yoga? {
        val lord4 = houseLords[4] ?: return null
        val lord9 = houseLords[9] ?: return null

        if (lord4 == lord9) return null

        val pos4 = chart.planetPositions.find { it.planet == lord4 } ?: return null
        val pos9 = chart.planetPositions.find { it.planet == lord9 } ?: return null

        // Both should be in Kendras
        val bothInKendras = pos4.house in KENDRA_HOUSES && pos9.house in KENDRA_HOUSES

        if (!bothInKendras) return null

        // Should be in mutual Kendra (1-7 or 4-10 from each other)
        val houseFrom4to9 = YogaHelpers.getHouseFrom(pos9.sign, pos4.sign)
        val isMutualKendra = houseFrom4to9 in listOf(1, 4, 7, 10)

        if (!isMutualKendra) return null

        val positions = listOf(pos4, pos9)
        val strengthPct = YogaHelpers.calculateYogaStrength(chart, positions)

        return Yoga(
            name = "Kahala Yoga",
            sanskritName = "कहल योग",
            category = YogaCategory.SPECIAL_YOGA,
            planets = listOf(lord4, lord9),
            houses = listOf(pos4.house, pos9.house),
            description = "4th lord and 9th lord in mutual Kendras",
            effects = "Bold and daring in career, courageous professional decisions, " +
                    "success through initiative, property and fortune combined, " +
                    "adventurous career path, risk-taking brings rewards, " +
                    "father's property supports career",
            strength = YogaHelpers.strengthFromPercentage(strengthPct),
            strengthPercentage = strengthPct,
            isAuspicious = true,
            activationPeriod = "Dasha of ${lord4.displayName} or ${lord9.displayName}",
            cancellationFactors = emptyList()
        )
    }

    /**
     * Ubhayachari Yoga - Planets on both sides of Sun
     */
    private fun evaluateUbhayachariYoga(
        chart: VedicChart,
        houseLords: Map<Int, Planet>
    ): Yoga? {
        val sunPos = chart.planetPositions.find { it.planet == Planet.SUN } ?: return null
        val sunHouse = sunPos.house

        val prevHouse = if (sunHouse == 1) 12 else sunHouse - 1
        val nextHouse = if (sunHouse == 12) 1 else sunHouse + 1

        // Exclude Rahu, Ketu, and Moon from consideration
        val excludedPlanets = listOf(Planet.RAHU, Planet.KETU, Planet.MOON)

        val planetBefore = chart.planetPositions.any {
            it.planet !in excludedPlanets && it.planet != Planet.SUN && it.house == prevHouse
        }
        val planetAfter = chart.planetPositions.any {
            it.planet !in excludedPlanets && it.planet != Planet.SUN && it.house == nextHouse
        }

        if (!planetBefore || !planetAfter) return null

        val planetsInvolved = chart.planetPositions.filter {
            it.planet !in excludedPlanets && 
            it.planet != Planet.SUN && 
            it.house in listOf(prevHouse, nextHouse)
        }

        val strengthPct = YogaHelpers.calculateYogaStrength(chart, planetsInvolved + sunPos)

        // Check if benefics are involved (stronger yoga)
        val beneficsInvolved = planetsInvolved.any { 
            it.planet in listOf(Planet.JUPITER, Planet.VENUS, Planet.MERCURY) 
        }

        val adjustedStrength = if (beneficsInvolved) strengthPct * 1.15 else strengthPct

        return Yoga(
            name = "Ubhayachari Yoga",
            sanskritName = "उभयचरी योग",
            category = YogaCategory.SPECIAL_YOGA,
            planets = listOf(Planet.SUN) + planetsInvolved.map { it.planet },
            houses = listOf(sunHouse, prevHouse, nextHouse),
            description = "Planets on both sides of Sun (${ordinal(prevHouse)} and ${ordinal(nextHouse)} houses)",
            effects = "Supported by subordinates and superiors, influential position, " +
                    "good reputation, wealth from authority, " +
                    "success through proper timing, balanced approach to power, " +
                    "administrative ability, trusted position",
            strength = YogaHelpers.strengthFromPercentage(adjustedStrength.coerceIn(10.0, 100.0)),
            strengthPercentage = adjustedStrength.coerceIn(10.0, 100.0),
            isAuspicious = true,
            activationPeriod = "Sun Dasha or Dashas of planets flanking Sun",
            cancellationFactors = emptyList()
        )
    }

    /**
     * Vesi and Vasi Yoga - Planet before or after Sun
     */
    private fun evaluateVesiVasiYoga(
        chart: VedicChart,
        houseLords: Map<Int, Planet>
    ): List<Yoga>? {
        val yogas = mutableListOf<Yoga>()
        val sunPos = chart.planetPositions.find { it.planet == Planet.SUN } ?: return null
        val sunHouse = sunPos.house

        val prevHouse = if (sunHouse == 1) 12 else sunHouse - 1
        val nextHouse = if (sunHouse == 12) 1 else sunHouse + 1

        val excludedPlanets = listOf(Planet.RAHU, Planet.KETU, Planet.MOON)

        // Vesi Yoga - Planet in 2nd from Sun (next house from Sun)
        val planetsAfterSun = chart.planetPositions.filter {
            it.planet !in excludedPlanets && it.planet != Planet.SUN && it.house == nextHouse
        }

        if (planetsAfterSun.isNotEmpty()) {
            val strengthPct = YogaHelpers.calculateYogaStrength(chart, planetsAfterSun)
            
            yogas.add(Yoga(
                name = "Vesi Yoga",
                sanskritName = "वेशी योग",
                category = YogaCategory.SPECIAL_YOGA,
                planets = planetsAfterSun.map { it.planet },
                houses = listOf(nextHouse),
                description = "${planetsAfterSun.joinToString { it.planet.displayName }} in 2nd from Sun",
                effects = "Eloquent speech, wealth accumulation ability, " +
                        "resources to support career, family supports profession, " +
                        "good values in work, truthful in dealings",
                strength = YogaHelpers.strengthFromPercentage(strengthPct),
                strengthPercentage = strengthPct,
                isAuspicious = true,
                activationPeriod = "Dashas of planets in 2nd from Sun",
                cancellationFactors = emptyList()
            ))
        }

        // Vasi Yoga - Planet in 12th from Sun (previous house from Sun)
        val planetsBeforeSun = chart.planetPositions.filter {
            it.planet !in excludedPlanets && it.planet != Planet.SUN && it.house == prevHouse
        }

        if (planetsBeforeSun.isNotEmpty()) {
            val strengthPct = YogaHelpers.calculateYogaStrength(chart, planetsBeforeSun)
            
            yogas.add(Yoga(
                name = "Vasi Yoga",
                sanskritName = "वासी योग",
                category = YogaCategory.SPECIAL_YOGA,
                planets = planetsBeforeSun.map { it.planet },
                houses = listOf(prevHouse),
                description = "${planetsBeforeSun.joinToString { it.planet.displayName }} in 12th from Sun",
                effects = "Expenditure for good causes, charitable nature in profession, " +
                        "spiritual approach to work, foreign career connections, " +
                        "investments in career, behind-the-scenes influence",
                strength = YogaHelpers.strengthFromPercentage(strengthPct),
                strengthPercentage = strengthPct,
                isAuspicious = true,
                activationPeriod = "Dashas of planets in 12th from Sun",
                cancellationFactors = emptyList()
            ))
        }

        return yogas.ifEmpty { null }
    }

    /**
     * 10th Lord Position Yogas - Based on where 10th lord is placed
     */
    private fun evaluateTenthLordYogas(
        chart: VedicChart,
        houseLords: Map<Int, Planet>
    ): List<Yoga>? {
        val yogas = mutableListOf<Yoga>()
        val lord10 = houseLords[10] ?: return null
        val pos10 = chart.planetPositions.find { it.planet == lord10 } ?: return null

        val strengthPct = YogaHelpers.calculateYogaStrength(chart, listOf(pos10))
        val profession = PROFESSION_INDICATORS[lord10] ?: "various professional fields"

        // 10th lord in different houses
        when (pos10.house) {
            1 -> yogas.add(Yoga(
                name = "Karma-Lagna Yoga",
                sanskritName = "कर्म-लग्न योग",
                category = YogaCategory.SPECIAL_YOGA,
                planets = listOf(lord10),
                houses = listOf(1, 10),
                description = "10th lord ${lord10.displayName} in Lagna (1st house)",
                effects = "Self-employed success, independent career, personal brand, " +
                        "career defines identity, entrepreneurship, " +
                        "professional reputation tied to personality. Suitable: $profession",
                strength = YogaHelpers.strengthFromPercentage(strengthPct * 1.1),
                strengthPercentage = (strengthPct * 1.1).coerceIn(10.0, 100.0),
                isAuspicious = true,
                activationPeriod = "${lord10.displayName} Dasha",
                cancellationFactors = emptyList()
            ))
            
            2 -> yogas.add(Yoga(
                name = "Karma-Dhana Yoga",
                sanskritName = "कर्म-धन योग",
                category = YogaCategory.SPECIAL_YOGA,
                planets = listOf(lord10),
                houses = listOf(2, 10),
                description = "10th lord ${lord10.displayName} in 2nd house",
                effects = "Career brings wealth, family business success, " +
                        "speech/communication career, banking/finance profession, " +
                        "wealth from profession. Suitable: $profession",
                strength = YogaHelpers.strengthFromPercentage(strengthPct),
                strengthPercentage = strengthPct,
                isAuspicious = true,
                activationPeriod = "${lord10.displayName} Dasha",
                cancellationFactors = emptyList()
            ))
            
            5 -> yogas.add(Yoga(
                name = "Karma-Putra Yoga",
                sanskritName = "कर्म-पुत्र योग",
                category = YogaCategory.SPECIAL_YOGA,
                planets = listOf(lord10),
                houses = listOf(5, 10),
                description = "10th lord ${lord10.displayName} in 5th house",
                effects = "Creative career, entertainment industry, speculation success, " +
                        "children continue profession, teaching/counseling career, " +
                        "intelligent work approach. Suitable: $profession",
                strength = YogaHelpers.strengthFromPercentage(strengthPct * 1.05),
                strengthPercentage = (strengthPct * 1.05).coerceIn(10.0, 100.0),
                isAuspicious = true,
                activationPeriod = "${lord10.displayName} Dasha",
                cancellationFactors = emptyList()
            ))
            
            9 -> yogas.add(Yoga(
                name = "Karma-Bhagya Yoga",
                sanskritName = "कर्म-भाग्य योग",
                category = YogaCategory.SPECIAL_YOGA,
                planets = listOf(lord10),
                houses = listOf(9, 10),
                description = "10th lord ${lord10.displayName} in 9th house",
                effects = "Fortunate career, father supports profession, " +
                        "religious/philosophical work, higher education career, " +
                        "foreign career success, dharmic profession. Suitable: $profession",
                strength = YogaHelpers.strengthFromPercentage(strengthPct * 1.15),
                strengthPercentage = (strengthPct * 1.15).coerceIn(10.0, 100.0),
                isAuspicious = true,
                activationPeriod = "${lord10.displayName} Dasha",
                cancellationFactors = emptyList()
            ))
            
            11 -> yogas.add(Yoga(
                name = "Karma-Labha Yoga",
                sanskritName = "कर्म-लाभ योग",
                category = YogaCategory.SPECIAL_YOGA,
                planets = listOf(lord10),
                houses = listOf(10, 11),
                description = "10th lord ${lord10.displayName} in 11th house",
                effects = "Career brings gains, multiple income sources, " +
                        "networking advances career, elder sibling helps profession, " +
                        "fulfilled professional desires. Suitable: $profession",
                strength = YogaHelpers.strengthFromPercentage(strengthPct * 1.1),
                strengthPercentage = (strengthPct * 1.1).coerceIn(10.0, 100.0),
                isAuspicious = true,
                activationPeriod = "${lord10.displayName} Dasha",
                cancellationFactors = emptyList()
            ))
        }

        // 10th lord exalted or in own sign
        if (YogaHelpers.isExalted(pos10) || YogaHelpers.isInOwnSign(pos10)) {
            yogas.add(Yoga(
                name = "Karma-Uccha Yoga",
                sanskritName = "कर्म-उच्च योग",
                category = YogaCategory.SPECIAL_YOGA,
                planets = listOf(lord10),
                houses = listOf(pos10.house, 10),
                description = "10th lord ${lord10.displayName} dignified in ${pos10.sign.displayName}",
                effects = "Excellent career potential, professional excellence, " +
                        "high status in chosen field, recognition and honors, " +
                        "authority in profession, leadership position. Suitable: $profession",
                strength = YogaHelpers.strengthFromPercentage(strengthPct * 1.2),
                strengthPercentage = (strengthPct * 1.2).coerceIn(10.0, 100.0),
                isAuspicious = true,
                activationPeriod = "${lord10.displayName} Dasha",
                cancellationFactors = emptyList()
            ))
        }

        return yogas.ifEmpty { null }
    }

    /**
     * Karmajiva Yoga - Professional Excellence
     * Strong planets in 10th house
     */
    private fun evaluateKarmajivaYoga(
        chart: VedicChart,
        houseLords: Map<Int, Planet>
    ): Yoga? {
        val planetsIn10th = chart.planetPositions.filter { 
            it.house == 10 && it.planet !in listOf(Planet.RAHU, Planet.KETU)
        }

        if (planetsIn10th.isEmpty()) return null

        // At least one planet should be dignified or strong
        val hasStrongPlanet = planetsIn10th.any { pos ->
            YogaHelpers.isExalted(pos) || 
            YogaHelpers.isInOwnSign(pos) || 
            YogaHelpers.isInFriendSign(pos)
        }

        if (!hasStrongPlanet) return null

        val strengthPct = YogaHelpers.calculateYogaStrength(chart, planetsIn10th)

        // Build profession indications based on planets
        val professionIndicators = planetsIn10th.mapNotNull { 
            PROFESSION_INDICATORS[it.planet] 
        }.joinToString("; ")

        return Yoga(
            name = "Karmajiva Yoga",
            sanskritName = "कर्मजीव योग",
            category = YogaCategory.SPECIAL_YOGA,
            planets = planetsIn10th.map { it.planet },
            houses = listOf(10),
            description = "Strong planet(s) in 10th house: ${planetsIn10th.joinToString { it.planet.displayName }}",
            effects = "Professional excellence, career success, recognition in work, " +
                    "strong professional identity, dedication to career. " +
                    "Career indications: $professionIndicators",
            strength = YogaHelpers.strengthFromPercentage(strengthPct),
            strengthPercentage = strengthPct,
            isAuspicious = true,
            activationPeriod = "Dashas of planets in 10th house",
            cancellationFactors = emptyList()
        )
    }

    /**
     * Khadga Yoga - Sword of Success
     * 2nd lord in 9th and 9th lord in 2nd
     */
    private fun evaluateKhadgaYoga(
        chart: VedicChart,
        houseLords: Map<Int, Planet>
    ): Yoga? {
        val lord2 = houseLords[2] ?: return null
        val lord9 = houseLords[9] ?: return null

        if (lord2 == lord9) return null

        val pos2 = chart.planetPositions.find { it.planet == lord2 } ?: return null
        val pos9 = chart.planetPositions.find { it.planet == lord9 } ?: return null

        // 2nd lord in 9th and 9th lord in 2nd
        val isFormed = pos2.house == 9 && pos9.house == 2

        if (!isFormed) return null

        val positions = listOf(pos2, pos9)
        val strengthPct = YogaHelpers.calculateYogaStrength(chart, positions)

        return Yoga(
            name = "Khadga Yoga",
            sanskritName = "खड्ग योग",
            category = YogaCategory.SPECIAL_YOGA,
            planets = listOf(lord2, lord9),
            houses = listOf(2, 9),
            description = "2nd lord ${lord2.displayName} in 9th, 9th lord ${lord9.displayName} in 2nd",
            effects = "Sharp intellect brings wealth, decisive career moves, " +
                    "cuts through obstacles, dharmic wealth, " +
                    "fortune through speech and values, " +
                    "inheritance and earned wealth combined",
            strength = YogaHelpers.strengthFromPercentage(strengthPct),
            strengthPercentage = strengthPct,
            isAuspicious = true,
            activationPeriod = "Dasha of ${lord2.displayName} or ${lord9.displayName}",
            cancellationFactors = emptyList()
        )
    }

    /**
     * Lakshmi Yoga - Wealth Goddess Blessing
     * 9th lord in Kendra/Trikona in own/exalted sign with Lagna lord strong
     */
    private fun evaluateLakshmiYoga(
        chart: VedicChart,
        houseLords: Map<Int, Planet>
    ): Yoga? {
        val lord1 = houseLords[1] ?: return null
        val lord9 = houseLords[9] ?: return null

        val pos1 = chart.planetPositions.find { it.planet == lord1 } ?: return null
        val pos9 = chart.planetPositions.find { it.planet == lord9 } ?: return null

        // 9th lord should be in Kendra or Trikona
        val goodHouses = KENDRA_HOUSES + TRIKONA_HOUSES
        if (pos9.house !in goodHouses) return null

        // 9th lord should be in own/exalted sign
        val lord9Dignified = YogaHelpers.isExalted(pos9) || YogaHelpers.isInOwnSign(pos9)
        if (!lord9Dignified) return null

        // Lagna lord should be strong (in Kendra/Trikona or dignified)
        val lord1Strong = pos1.house in goodHouses || 
                YogaHelpers.isExalted(pos1) || 
                YogaHelpers.isInOwnSign(pos1)
        if (!lord1Strong) return null

        val positions = listOf(pos1, pos9)
        val strengthPct = YogaHelpers.calculateYogaStrength(chart, positions)

        return Yoga(
            name = "Lakshmi Yoga",
            sanskritName = "लक्ष्मी योग",
            category = YogaCategory.SPECIAL_YOGA,
            planets = listOf(lord1, lord9),
            houses = listOf(pos1.house, pos9.house),
            description = "9th lord ${lord9.displayName} dignified in ${ordinal(pos9.house)} house with strong Lagna lord",
            effects = "Wealth and prosperity, blessed by fortune, " +
                    "abundant resources, high status, " +
                    "luxurious lifestyle through righteous means, " +
                    "career brings lasting wealth, respected position",
            strength = YogaHelpers.strengthFromPercentage(strengthPct * 1.15),
            strengthPercentage = (strengthPct * 1.15).coerceIn(10.0, 100.0),
            isAuspicious = true,
            activationPeriod = "Dasha of ${lord9.displayName}",
            cancellationFactors = emptyList()
        )
    }

    /**
     * Gaja-Kesari Yoga affecting career (when in 10th)
     */
    private fun evaluateGajaKesariKarmaYoga(
        chart: VedicChart,
        houseLords: Map<Int, Planet>
    ): Yoga? {
        val moonPos = chart.planetPositions.find { it.planet == Planet.MOON } ?: return null
        val jupiterPos = chart.planetPositions.find { it.planet == Planet.JUPITER } ?: return null

        // Check if Moon and Jupiter are in Kendra from each other
        val houseFromMoon = YogaHelpers.getHouseFrom(jupiterPos.sign, moonPos.sign)
        val inMutualKendra = houseFromMoon in listOf(1, 4, 7, 10)

        if (!inMutualKendra) return null

        // At least one should be in or aspecting 10th house
        val relevantToCareer = moonPos.house == 10 || 
                jupiterPos.house == 10 ||
                (jupiterPos.house in listOf(2, 6, 10)) // Jupiter aspects 10th from these houses

        if (!relevantToCareer) return null

        val positions = listOf(moonPos, jupiterPos)
        val strengthPct = YogaHelpers.calculateYogaStrength(chart, positions)

        return Yoga(
            name = "Gaja-Kesari Karma Yoga",
            sanskritName = "गज-केसरी कर्म योग",
            category = YogaCategory.SPECIAL_YOGA,
            planets = listOf(Planet.MOON, Planet.JUPITER),
            houses = listOf(moonPos.house, jupiterPos.house),
            description = "Moon-Jupiter Kendra relationship influencing 10th house",
            effects = "Wisdom in career, respected professional, " +
                    "teaching/counseling success, public popularity, " +
                    "career brings fame, ethical professional conduct, " +
                    "lasting professional reputation",
            strength = YogaHelpers.strengthFromPercentage(strengthPct),
            strengthPercentage = strengthPct,
            isAuspicious = true,
            activationPeriod = "Moon or Jupiter Dasha",
            cancellationFactors = emptyList()
        )
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
