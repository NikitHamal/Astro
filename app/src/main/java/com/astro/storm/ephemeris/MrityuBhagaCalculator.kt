package com.astro.storm.ephemeris

import com.astro.storm.data.model.Planet
import com.astro.storm.data.model.PlanetPosition
import com.astro.storm.data.model.VedicChart
import com.astro.storm.data.model.ZodiacSign
import kotlin.math.abs

/**
 * Mrityu Bhaga (Sensitive Degrees) Calculator
 * Implements analysis of critical degrees where planetary placement indicates
 * health vulnerabilities, accidents, or transformative life events.
 *
 * Reference: Phaladeepika, BPHS, Saravali
 */
object MrityuBhagaCalculator {

    /**
     * Traditional Mrityu Bhaga degrees for each planet in each sign
     * These are degrees where the planet is considered to be at its most vulnerable
     * Reference: Phaladeepika Chapter 24, Various traditional texts
     */
    private val MRITYU_BHAGA_DEGREES = mapOf(
        Planet.SUN to mapOf(
            ZodiacSign.ARIES to 20.0, ZodiacSign.TAURUS to 9.0, ZodiacSign.GEMINI to 12.0,
            ZodiacSign.CANCER to 6.0, ZodiacSign.LEO to 8.0, ZodiacSign.VIRGO to 24.0,
            ZodiacSign.LIBRA to 16.0, ZodiacSign.SCORPIO to 17.0, ZodiacSign.SAGITTARIUS to 22.0,
            ZodiacSign.CAPRICORN to 2.0, ZodiacSign.AQUARIUS to 3.0, ZodiacSign.PISCES to 23.0
        ),
        Planet.MOON to mapOf(
            ZodiacSign.ARIES to 26.0, ZodiacSign.TAURUS to 12.0, ZodiacSign.GEMINI to 13.0,
            ZodiacSign.CANCER to 25.0, ZodiacSign.LEO to 24.0, ZodiacSign.VIRGO to 11.0,
            ZodiacSign.LIBRA to 26.0, ZodiacSign.SCORPIO to 14.0, ZodiacSign.SAGITTARIUS to 13.0,
            ZodiacSign.CAPRICORN to 25.0, ZodiacSign.AQUARIUS to 5.0, ZodiacSign.PISCES to 12.0
        ),
        Planet.MARS to mapOf(
            ZodiacSign.ARIES to 19.0, ZodiacSign.TAURUS to 28.0, ZodiacSign.GEMINI to 25.0,
            ZodiacSign.CANCER to 23.0, ZodiacSign.LEO to 29.0, ZodiacSign.VIRGO to 28.0,
            ZodiacSign.LIBRA to 19.0, ZodiacSign.SCORPIO to 14.0, ZodiacSign.SAGITTARIUS to 13.0,
            ZodiacSign.CAPRICORN to 21.0, ZodiacSign.AQUARIUS to 2.0, ZodiacSign.PISCES to 8.0
        ),
        Planet.MERCURY to mapOf(
            ZodiacSign.ARIES to 15.0, ZodiacSign.TAURUS to 14.0, ZodiacSign.GEMINI to 13.0,
            ZodiacSign.CANCER to 12.0, ZodiacSign.LEO to 8.0, ZodiacSign.VIRGO to 18.0,
            ZodiacSign.LIBRA to 20.0, ZodiacSign.SCORPIO to 10.0, ZodiacSign.SAGITTARIUS to 21.0,
            ZodiacSign.CAPRICORN to 22.0, ZodiacSign.AQUARIUS to 7.0, ZodiacSign.PISCES to 15.0
        ),
        Planet.JUPITER to mapOf(
            ZodiacSign.ARIES to 19.0, ZodiacSign.TAURUS to 29.0, ZodiacSign.GEMINI to 12.0,
            ZodiacSign.CANCER to 27.0, ZodiacSign.LEO to 6.0, ZodiacSign.VIRGO to 4.0,
            ZodiacSign.LIBRA to 13.0, ZodiacSign.SCORPIO to 10.0, ZodiacSign.SAGITTARIUS to 17.0,
            ZodiacSign.CAPRICORN to 27.0, ZodiacSign.AQUARIUS to 14.0, ZodiacSign.PISCES to 13.0
        ),
        Planet.VENUS to mapOf(
            ZodiacSign.ARIES to 28.0, ZodiacSign.TAURUS to 15.0, ZodiacSign.GEMINI to 11.0,
            ZodiacSign.CANCER to 17.0, ZodiacSign.LEO to 10.0, ZodiacSign.VIRGO to 13.0,
            ZodiacSign.LIBRA to 4.0, ZodiacSign.SCORPIO to 7.0, ZodiacSign.SAGITTARIUS to 11.0,
            ZodiacSign.CAPRICORN to 6.0, ZodiacSign.AQUARIUS to 15.0, ZodiacSign.PISCES to 17.0
        ),
        Planet.SATURN to mapOf(
            ZodiacSign.ARIES to 10.0, ZodiacSign.TAURUS to 4.0, ZodiacSign.GEMINI to 7.0,
            ZodiacSign.CANCER to 9.0, ZodiacSign.LEO to 12.0, ZodiacSign.VIRGO to 23.0,
            ZodiacSign.LIBRA to 7.0, ZodiacSign.SCORPIO to 22.0, ZodiacSign.SAGITTARIUS to 3.0,
            ZodiacSign.CAPRICORN to 18.0, ZodiacSign.AQUARIUS to 26.0, ZodiacSign.PISCES to 14.0
        ),
        Planet.RAHU to mapOf(
            ZodiacSign.ARIES to 14.0, ZodiacSign.TAURUS to 13.0, ZodiacSign.GEMINI to 12.0,
            ZodiacSign.CANCER to 11.0, ZodiacSign.LEO to 24.0, ZodiacSign.VIRGO to 23.0,
            ZodiacSign.LIBRA to 22.0, ZodiacSign.SCORPIO to 21.0, ZodiacSign.SAGITTARIUS to 20.0,
            ZodiacSign.CAPRICORN to 19.0, ZodiacSign.AQUARIUS to 18.0, ZodiacSign.PISCES to 17.0
        ),
        Planet.KETU to mapOf(
            ZodiacSign.ARIES to 14.0, ZodiacSign.TAURUS to 13.0, ZodiacSign.GEMINI to 12.0,
            ZodiacSign.CANCER to 11.0, ZodiacSign.LEO to 24.0, ZodiacSign.VIRGO to 23.0,
            ZodiacSign.LIBRA to 22.0, ZodiacSign.SCORPIO to 21.0, ZodiacSign.SAGITTARIUS to 20.0,
            ZodiacSign.CAPRICORN to 19.0, ZodiacSign.AQUARIUS to 18.0, ZodiacSign.PISCES to 17.0
        )
    )

    /** Orb for Mrityu Bhaga consideration (degrees) */
    private const val MRITYU_BHAGA_ORB = 1.0

    /** Gandanta junction points (water-fire sign boundaries) */
    private val GANDANTA_POINTS = listOf(
        GandantaPoint(ZodiacSign.CANCER, ZodiacSign.LEO, 29.0, 1.0),
        GandantaPoint(ZodiacSign.SCORPIO, ZodiacSign.SAGITTARIUS, 29.0, 1.0),
        GandantaPoint(ZodiacSign.PISCES, ZodiacSign.ARIES, 29.0, 1.0)
    )

    /** Pushkara Navamsa degrees - highly auspicious placements */
    private val PUSHKARA_NAVAMSA = mapOf(
        ZodiacSign.ARIES to listOf(21.0 to 23.333, 24.0 to 26.667),
        ZodiacSign.TAURUS to listOf(7.333 to 10.0, 14.0 to 16.667),
        ZodiacSign.GEMINI to listOf(17.333 to 20.0, 27.333 to 30.0),
        ZodiacSign.CANCER to listOf(0.0 to 3.333, 10.0 to 13.333),
        ZodiacSign.LEO to listOf(21.0 to 23.333, 24.0 to 26.667),
        ZodiacSign.VIRGO to listOf(7.333 to 10.0, 14.0 to 16.667),
        ZodiacSign.LIBRA to listOf(17.333 to 20.0, 27.333 to 30.0),
        ZodiacSign.SCORPIO to listOf(0.0 to 3.333, 10.0 to 13.333),
        ZodiacSign.SAGITTARIUS to listOf(21.0 to 23.333, 24.0 to 26.667),
        ZodiacSign.CAPRICORN to listOf(7.333 to 10.0, 14.0 to 16.667),
        ZodiacSign.AQUARIUS to listOf(17.333 to 20.0, 27.333 to 30.0),
        ZodiacSign.PISCES to listOf(0.0 to 3.333, 10.0 to 13.333)
    )

    /** Pushkara Bhaga degrees - single auspicious degrees */
    private val PUSHKARA_BHAGA = mapOf(
        ZodiacSign.ARIES to 21.0,
        ZodiacSign.TAURUS to 14.0,
        ZodiacSign.GEMINI to 18.0,
        ZodiacSign.CANCER to 8.0,
        ZodiacSign.LEO to 19.0,
        ZodiacSign.VIRGO to 9.0,
        ZodiacSign.LIBRA to 24.0,
        ZodiacSign.SCORPIO to 11.0,
        ZodiacSign.SAGITTARIUS to 23.0,
        ZodiacSign.CAPRICORN to 14.0,
        ZodiacSign.AQUARIUS to 19.0,
        ZodiacSign.PISCES to 9.0
    )

    /**
     * Comprehensive sensitive degrees analysis
     */
    fun analyzeSensitiveDegrees(chart: VedicChart): SensitiveDegreesAnalysis {
        val mrityuBhagaResults = analyzeMrityuBhaga(chart)
        val gandantaResults = analyzeGandanta(chart)
        val pushkaraNavamsaResults = analyzePushkaraNavamsa(chart)
        val pushkaraBhagaResults = analyzePushkaraBhaga(chart)

        val criticalPlanets = mrityuBhagaResults.filter { it.isInMrityuBhaga } +
                              gandantaResults.filter { it.isInGandanta }

        val auspiciousPlanets = pushkaraNavamsaResults.filter { it.isInPushkaraNavamsa } +
                                pushkaraBhagaResults.filter { it.isInPushkaraBhaga }

        val overallAssessment = calculateOverallAssessment(
            mrityuBhagaResults, gandantaResults, pushkaraNavamsaResults, pushkaraBhagaResults
        )

        return SensitiveDegreesAnalysis(
            mrityuBhagaAnalysis = mrityuBhagaResults,
            gandantaAnalysis = gandantaResults,
            pushkaraNavamsaAnalysis = pushkaraNavamsaResults,
            pushkaraBhagaAnalysis = pushkaraBhagaResults,
            criticalPlanets = criticalPlanets.map {
                when (it) {
                    is MrityuBhagaResult -> it.planet
                    is GandantaResult -> it.planet
                    else -> Planet.SUN
                }
            }.distinct(),
            auspiciousPlanets = auspiciousPlanets.map {
                when (it) {
                    is PushkaraNavamsaResult -> it.planet
                    is PushkaraBhagaResult -> it.planet
                    else -> Planet.SUN
                }
            }.distinct(),
            overallAssessment = overallAssessment,
            recommendations = generateRecommendations(mrityuBhagaResults, gandantaResults)
        )
    }

    /**
     * Analyze Mrityu Bhaga placements for all planets
     */
    fun analyzeMrityuBhaga(chart: VedicChart): List<MrityuBhagaResult> {
        return chart.planetPositions.mapNotNull { position ->
            val mrityuDegree = MRITYU_BHAGA_DEGREES[position.planet]?.get(position.sign)
                ?: return@mapNotNull null

            val degreeInSign = position.longitude % 30.0
            val distance = abs(degreeInSign - mrityuDegree)
            val isInMrityuBhaga = distance <= MRITYU_BHAGA_ORB

            val severity = when {
                distance <= 0.25 -> MrityuBhagaSeverity.EXACT
                distance <= 0.5 -> MrityuBhagaSeverity.VERY_CLOSE
                distance <= MRITYU_BHAGA_ORB -> MrityuBhagaSeverity.WITHIN_ORB
                distance <= 2.0 -> MrityuBhagaSeverity.APPROACHING
                else -> MrityuBhagaSeverity.SAFE
            }

            val effects = if (isInMrityuBhaga) {
                getMrityuBhagaEffects(position.planet)
            } else emptyList()

            val vulnerabilityAreas = if (isInMrityuBhaga) {
                getVulnerabilityAreas(position.planet)
            } else emptyList()

            MrityuBhagaResult(
                planet = position.planet,
                sign = position.sign,
                actualDegree = degreeInSign,
                mrityuBhagaDegree = mrityuDegree,
                distanceFromMrityuBhaga = distance,
                isInMrityuBhaga = isInMrityuBhaga,
                severity = severity,
                effects = effects,
                vulnerabilityAreas = vulnerabilityAreas,
                remedies = if (isInMrityuBhaga) getMrityuBhagaRemedies(position.planet) else emptyList()
            )
        }
    }

    /**
     * Analyze Gandanta placements
     */
    fun analyzeGandanta(chart: VedicChart): List<GandantaResult> {
        return chart.planetPositions.mapNotNull { position ->
            val degreeInSign = position.longitude % 30.0

            val gandanta = GANDANTA_POINTS.find { gp ->
                (position.sign == gp.waterSign && degreeInSign >= 30.0 - gp.orbBefore) ||
                (position.sign == gp.fireSign && degreeInSign <= gp.orbAfter)
            }

            gandanta?.let { gp ->
                val isWaterSide = position.sign == gp.waterSign
                val distanceFromJunction = if (isWaterSide) {
                    30.0 - degreeInSign
                } else {
                    degreeInSign
                }

                val severity = when {
                    distanceFromJunction <= 0.25 -> GandantaSeverity.EXACT_JUNCTION
                    distanceFromJunction <= 0.5 -> GandantaSeverity.CRITICAL
                    distanceFromJunction <= 1.0 -> GandantaSeverity.SEVERE
                    distanceFromJunction <= 2.0 -> GandantaSeverity.MODERATE
                    else -> GandantaSeverity.MILD
                }

                val type = getGandantaType(gp.waterSign)

                GandantaResult(
                    planet = position.planet,
                    sign = position.sign,
                    degree = degreeInSign,
                    isInGandanta = true,
                    distanceFromJunction = distanceFromJunction,
                    severity = severity,
                    gandantaType = type,
                    waterSign = gp.waterSign,
                    fireSign = gp.fireSign,
                    effects = getGandantaEffects(position.planet, type),
                    remedies = getGandantaRemedies(position.planet, type)
                )
            }
        }
    }

    /**
     * Analyze Pushkara Navamsa placements
     */
    fun analyzePushkaraNavamsa(chart: VedicChart): List<PushkaraNavamsaResult> {
        return chart.planetPositions.map { position ->
            val degreeInSign = position.longitude % 30.0
            val ranges = PUSHKARA_NAVAMSA[position.sign] ?: emptyList()

            val matchingRange = ranges.find { (start, end) ->
                degreeInSign >= start && degreeInSign <= end
            }

            val isInPushkara = matchingRange != null

            PushkaraNavamsaResult(
                planet = position.planet,
                sign = position.sign,
                degree = degreeInSign,
                isInPushkaraNavamsa = isInPushkara,
                pushkaraRange = matchingRange,
                benefits = if (isInPushkara) getPushkaraNavamsaBenefits(position.planet) else emptyList()
            )
        }
    }

    /**
     * Analyze Pushkara Bhaga placements
     */
    fun analyzePushkaraBhaga(chart: VedicChart): List<PushkaraBhagaResult> {
        return chart.planetPositions.map { position ->
            val degreeInSign = position.longitude % 30.0
            val pushkaraDegree = PUSHKARA_BHAGA[position.sign] ?: 0.0
            val distance = abs(degreeInSign - pushkaraDegree)
            val isInPushkara = distance <= 1.0

            PushkaraBhagaResult(
                planet = position.planet,
                sign = position.sign,
                actualDegree = degreeInSign,
                pushkaraBhagaDegree = pushkaraDegree,
                distance = distance,
                isInPushkaraBhaga = isInPushkara,
                benefits = if (isInPushkara) getPushkaraBhagaBenefits(position.planet) else emptyList()
            )
        }
    }

    /**
     * Calculate transit vulnerability periods
     */
    fun calculateTransitVulnerability(
        chart: VedicChart,
        transitingPlanet: Planet,
        transitSign: ZodiacSign,
        transitDegree: Double
    ): TransitVulnerabilityResult {
        val mrityuDegree = MRITYU_BHAGA_DEGREES[transitingPlanet]?.get(transitSign)
        val distance = mrityuDegree?.let { abs(transitDegree - it) } ?: Double.MAX_VALUE

        val isVulnerable = distance <= 2.0

        val natalPlanetsAffected = chart.planetPositions.filter { position ->
            val aspect = calculateAspect(transitSign.ordinal, position.sign.ordinal)
            aspect != null && distance <= 1.0
        }

        return TransitVulnerabilityResult(
            transitingPlanet = transitingPlanet,
            transitSign = transitSign,
            transitDegree = transitDegree,
            mrityuBhagaDegree = mrityuDegree ?: 0.0,
            distanceFromMrityuBhaga = distance,
            isVulnerablePeriod = isVulnerable,
            natalPlanetsAffected = natalPlanetsAffected.map { it.planet },
            cautionLevel = when {
                distance <= 0.5 -> CautionLevel.HIGH
                distance <= 1.0 -> CautionLevel.MODERATE
                distance <= 2.0 -> CautionLevel.LOW
                else -> CautionLevel.NONE
            },
            recommendations = if (isVulnerable) {
                getTransitRecommendations(transitingPlanet)
            } else emptyList()
        )
    }

    private fun calculateAspect(fromSignIndex: Int, toSignIndex: Int): Int? {
        val diff = (toSignIndex - fromSignIndex + 12) % 12
        return if (diff in listOf(0, 3, 6, 9)) diff else null
    }

    private fun getMrityuBhagaEffects(planet: Planet): List<String> {
        return when (planet) {
            Planet.SUN -> listOf(
                "Challenges with authority figures or father",
                "Potential health issues related to heart or vitality",
                "Career obstacles during certain periods",
                "Need for ego transformation"
            )
            Planet.MOON -> listOf(
                "Emotional sensitivity and mental stress",
                "Challenges with mother or maternal relationships",
                "Fluctuating public image or reputation",
                "Need for emotional healing and stability"
            )
            Planet.MARS -> listOf(
                "Accident-prone tendencies during Mars periods",
                "Blood-related or surgical issues possible",
                "Conflicts with siblings or competitors",
                "Need to channel energy constructively"
            )
            Planet.MERCURY -> listOf(
                "Communication difficulties or misunderstandings",
                "Nervous system sensitivity",
                "Educational or business challenges",
                "Need for mental clarity practices"
            )
            Planet.JUPITER -> listOf(
                "Challenges in education or spiritual growth",
                "Issues with teachers or mentors",
                "Financial ups and downs",
                "Need for wisdom and philosophical grounding"
            )
            Planet.VENUS -> listOf(
                "Relationship challenges or disappointments",
                "Reproductive health considerations",
                "Artistic blocks or creative challenges",
                "Need for self-love and relationship healing"
            )
            Planet.SATURN -> listOf(
                "Career delays or professional setbacks",
                "Joint or bone-related health issues",
                "Karmic lessons intensified",
                "Need for patience and perseverance"
            )
            Planet.RAHU -> listOf(
                "Sudden unexpected events",
                "Foreign-related complications",
                "Obsessive tendencies intensified",
                "Need for grounding and reality checks"
            )
            Planet.KETU -> listOf(
                "Spiritual crises or confusion",
                "Detachment-related challenges",
                "Past-life karma surfacing",
                "Need for spiritual practices"
            )
            else -> emptyList()
        }
    }

    private fun getVulnerabilityAreas(planet: Planet): List<String> {
        return when (planet) {
            Planet.SUN -> listOf("Heart", "Eyes", "Spine", "Vitality")
            Planet.MOON -> listOf("Mind", "Emotions", "Stomach", "Breasts", "Fluids")
            Planet.MARS -> listOf("Blood", "Muscles", "Head", "Accidents", "Burns")
            Planet.MERCURY -> listOf("Nervous system", "Skin", "Lungs", "Speech")
            Planet.JUPITER -> listOf("Liver", "Fat tissues", "Ears", "Thighs")
            Planet.VENUS -> listOf("Reproductive system", "Kidneys", "Throat", "Face")
            Planet.SATURN -> listOf("Bones", "Joints", "Teeth", "Chronic conditions")
            Planet.RAHU -> listOf("Poisons", "Sudden events", "Unknown diseases")
            Planet.KETU -> listOf("Wounds", "Infections", "Accidents", "Spiritual crises")
            else -> emptyList()
        }
    }

    private fun getMrityuBhagaRemedies(planet: Planet): List<String> {
        return when (planet) {
            Planet.SUN -> listOf(
                "Offer water to Sun at sunrise",
                "Recite Aditya Hridayam regularly",
                "Wear Ruby with proper muhurta (if suitable)",
                "Donate wheat and jaggery on Sundays"
            )
            Planet.MOON -> listOf(
                "Worship Divine Mother on Mondays",
                "Wear Pearl or Moonstone (if suitable)",
                "Offer milk to Shiva Lingam",
                "Practice meditation for mental peace"
            )
            Planet.MARS -> listOf(
                "Recite Hanuman Chalisa daily",
                "Wear Red Coral (if suitable)",
                "Donate red lentils on Tuesdays",
                "Practice physical exercise regularly"
            )
            Planet.MERCURY -> listOf(
                "Worship Lord Vishnu on Wednesdays",
                "Wear Emerald (if suitable)",
                "Donate green vegetables",
                "Chant Om Budhaya Namaha"
            )
            Planet.JUPITER -> listOf(
                "Worship Lord Brihaspati on Thursdays",
                "Wear Yellow Sapphire (if suitable)",
                "Donate yellow items and turmeric",
                "Respect teachers and elders"
            )
            Planet.VENUS -> listOf(
                "Worship Goddess Lakshmi on Fridays",
                "Wear Diamond or White Sapphire (if suitable)",
                "Donate white items and rice",
                "Practice self-care and appreciation"
            )
            Planet.SATURN -> listOf(
                "Worship Lord Shani on Saturdays",
                "Wear Blue Sapphire only after thorough analysis",
                "Donate black sesame and iron items",
                "Serve the elderly and underprivileged"
            )
            Planet.RAHU -> listOf(
                "Worship Goddess Durga",
                "Wear Hessonite (Gomed) if suitable",
                "Donate blue clothes and blankets",
                "Avoid intoxicants and maintain ethics"
            )
            Planet.KETU -> listOf(
                "Worship Lord Ganesha",
                "Wear Cat's Eye (if suitable)",
                "Donate multi-colored blankets",
                "Practice meditation and spiritual disciplines"
            )
            else -> emptyList()
        }
    }

    private fun getGandantaType(waterSign: ZodiacSign): GandantaType {
        return when (waterSign) {
            ZodiacSign.CANCER -> GandantaType.BRAHMA_GANDANTA
            ZodiacSign.SCORPIO -> GandantaType.VISHNU_GANDANTA
            ZodiacSign.PISCES -> GandantaType.SHIVA_GANDANTA
            else -> GandantaType.BRAHMA_GANDANTA
        }
    }

    private fun getGandantaEffects(planet: Planet, type: GandantaType): List<String> {
        val baseEffects = when (type) {
            GandantaType.BRAHMA_GANDANTA -> listOf(
                "Initial life challenges (first 3 years critical)",
                "Mother's health during pregnancy/delivery",
                "Creative blocks requiring resolution"
            )
            GandantaType.VISHNU_GANDANTA -> listOf(
                "Transformation through crisis",
                "Hidden enemies or obstacles",
                "Research and occult abilities"
            )
            GandantaType.SHIVA_GANDANTA -> listOf(
                "Spiritual awakening through dissolution",
                "Endings leading to new beginnings",
                "Liberation from material attachments"
            )
        }

        val planetSpecific = when (planet) {
            Planet.MOON -> listOf("Emotional turbulence at junction points", "Mental transformation needed")
            Planet.SUN -> listOf("Identity crisis leading to self-realization", "Authority challenges")
            Planet.MARS -> listOf("Physical challenges or accidents possible", "Courage tested")
            else -> emptyList()
        }

        return baseEffects + planetSpecific
    }

    private fun getGandantaRemedies(planet: Planet, type: GandantaType): List<String> {
        val typeRemedies = when (type) {
            GandantaType.BRAHMA_GANDANTA -> listOf(
                "Perform Gandanta Shanti puja",
                "Donate grains and gold",
                "Worship Lord Brahma"
            )
            GandantaType.VISHNU_GANDANTA -> listOf(
                "Perform Vishnu Sahasranama recitation",
                "Donate black sesame and oil",
                "Worship Lord Vishnu regularly"
            )
            GandantaType.SHIVA_GANDANTA -> listOf(
                "Perform Rudra Abhishekam",
                "Donate silver and white items",
                "Worship Lord Shiva with devotion"
            )
        }

        val planetRemedies = getMrityuBhagaRemedies(planet)

        return typeRemedies + planetRemedies.take(2)
    }

    private fun getPushkaraNavamsaBenefits(planet: Planet): List<String> {
        val basebenefits = listOf(
            "Highly auspicious placement bringing good fortune",
            "Planet's significations strengthened",
            "Protection during difficult periods"
        )

        val planetBenefits = when (planet) {
            Planet.MOON -> listOf("Emotional stability and contentment", "Good mental health")
            Planet.JUPITER -> listOf("Wisdom and spiritual growth enhanced", "Educational success")
            Planet.VENUS -> listOf("Relationship harmony", "Artistic success and beauty")
            Planet.MERCURY -> listOf("Intellectual brilliance", "Communication excellence")
            else -> listOf("General protection and auspiciousness")
        }

        return basebenefits + planetBenefits
    }

    private fun getPushkaraBhagaBenefits(planet: Planet): List<String> {
        return listOf(
            "Planet at its most nourishing degree",
            "Significations of ${planet.displayName} receive special support",
            "Auspicious for ${planet.displayName}-related activities",
            "Natural protection during ${planet.displayName} periods"
        )
    }

    private fun getTransitRecommendations(planet: Planet): List<String> {
        return listOf(
            "Be extra cautious during this transit period",
            "Avoid major decisions related to ${planet.displayName}'s significations",
            "Practice ${planet.displayName}'s remedies more intensely",
            "Maintain awareness but avoid excessive worry"
        )
    }

    private fun calculateOverallAssessment(
        mrityuBhaga: List<MrityuBhagaResult>,
        gandanta: List<GandantaResult>,
        pushkaraNavamsa: List<PushkaraNavamsaResult>,
        pushkaraBhaga: List<PushkaraBhagaResult>
    ): OverallSensitiveDegreesAssessment {
        val criticalCount = mrityuBhaga.count { it.isInMrityuBhaga } +
                           gandanta.count { it.isInGandanta }
        val auspiciousCount = pushkaraNavamsa.count { it.isInPushkaraNavamsa } +
                              pushkaraBhaga.count { it.isInPushkaraBhaga }

        val level = when {
            criticalCount >= 3 && auspiciousCount < 2 -> AssessmentLevel.NEEDS_ATTENTION
            criticalCount >= 2 -> AssessmentLevel.MODERATE_CONCERN
            criticalCount == 1 && auspiciousCount >= 2 -> AssessmentLevel.BALANCED
            auspiciousCount >= 3 -> AssessmentLevel.HIGHLY_AUSPICIOUS
            else -> AssessmentLevel.GENERALLY_POSITIVE
        }

        val summary = when (level) {
            AssessmentLevel.NEEDS_ATTENTION ->
                "Multiple sensitive placements require attention and regular remedies"
            AssessmentLevel.MODERATE_CONCERN ->
                "Some sensitive placements present; recommended remedies will help"
            AssessmentLevel.BALANCED ->
                "Balance of challenging and supportive placements"
            AssessmentLevel.HIGHLY_AUSPICIOUS ->
                "Multiple auspicious Pushkara placements provide natural protection"
            AssessmentLevel.GENERALLY_POSITIVE ->
                "Generally favorable placement pattern"
        }

        return OverallSensitiveDegreesAssessment(
            level = level,
            criticalPlacementCount = criticalCount,
            auspiciousPlacementCount = auspiciousCount,
            summary = summary
        )
    }

    private fun generateRecommendations(
        mrityuBhaga: List<MrityuBhagaResult>,
        gandanta: List<GandantaResult>
    ): List<String> {
        val recommendations = mutableListOf<String>()

        val criticalMrityu = mrityuBhaga.filter { it.severity == MrityuBhagaSeverity.EXACT ||
                                                   it.severity == MrityuBhagaSeverity.VERY_CLOSE }
        val criticalGandanta = gandanta.filter { it.severity == GandantaSeverity.EXACT_JUNCTION ||
                                                  it.severity == GandantaSeverity.CRITICAL }

        if (criticalMrityu.isNotEmpty()) {
            recommendations.add("Perform specific planetary remedies for planets in exact Mrityu Bhaga")
            recommendations.add("Be especially careful during Dasha/Antardasha of affected planets")
        }

        if (criticalGandanta.isNotEmpty()) {
            val moonGandanta = criticalGandanta.find { it.planet == Planet.MOON }
            if (moonGandanta != null) {
                recommendations.add("Moon in Gandanta requires special attention - Gandanta Shanti recommended")
                recommendations.add("Practice meditation and emotional grounding regularly")
            }
            recommendations.add("Gandanta placements indicate karmic knots requiring spiritual work")
        }

        if (recommendations.isEmpty()) {
            recommendations.add("No critical sensitive degree placements - general spiritual practices sufficient")
            recommendations.add("Continue regular worship and ethical living")
        }

        return recommendations
    }
}

// Data classes
data class SensitiveDegreesAnalysis(
    val mrityuBhagaAnalysis: List<MrityuBhagaResult>,
    val gandantaAnalysis: List<GandantaResult>,
    val pushkaraNavamsaAnalysis: List<PushkaraNavamsaResult>,
    val pushkaraBhagaAnalysis: List<PushkaraBhagaResult>,
    val criticalPlanets: List<Planet>,
    val auspiciousPlanets: List<Planet>,
    val overallAssessment: OverallSensitiveDegreesAssessment,
    val recommendations: List<String>
)

data class MrityuBhagaResult(
    val planet: Planet,
    val sign: ZodiacSign,
    val actualDegree: Double,
    val mrityuBhagaDegree: Double,
    val distanceFromMrityuBhaga: Double,
    val isInMrityuBhaga: Boolean,
    val severity: MrityuBhagaSeverity,
    val effects: List<String>,
    val vulnerabilityAreas: List<String>,
    val remedies: List<String>
)

enum class MrityuBhagaSeverity {
    EXACT, VERY_CLOSE, WITHIN_ORB, APPROACHING, SAFE
}

data class GandantaResult(
    val planet: Planet,
    val sign: ZodiacSign,
    val degree: Double,
    val isInGandanta: Boolean,
    val distanceFromJunction: Double,
    val severity: GandantaSeverity,
    val gandantaType: GandantaType,
    val waterSign: ZodiacSign,
    val fireSign: ZodiacSign,
    val effects: List<String>,
    val remedies: List<String>
)

enum class GandantaSeverity {
    EXACT_JUNCTION, CRITICAL, SEVERE, MODERATE, MILD
}

enum class GandantaType {
    BRAHMA_GANDANTA,  // Cancer-Leo junction
    VISHNU_GANDANTA,  // Scorpio-Sagittarius junction
    SHIVA_GANDANTA    // Pisces-Aries junction
}

data class GandantaPoint(
    val waterSign: ZodiacSign,
    val fireSign: ZodiacSign,
    val orbBefore: Double,
    val orbAfter: Double
)

data class PushkaraNavamsaResult(
    val planet: Planet,
    val sign: ZodiacSign,
    val degree: Double,
    val isInPushkaraNavamsa: Boolean,
    val pushkaraRange: Pair<Double, Double>?,
    val benefits: List<String>
)

data class PushkaraBhagaResult(
    val planet: Planet,
    val sign: ZodiacSign,
    val actualDegree: Double,
    val pushkaraBhagaDegree: Double,
    val distance: Double,
    val isInPushkaraBhaga: Boolean,
    val benefits: List<String>
)

data class TransitVulnerabilityResult(
    val transitingPlanet: Planet,
    val transitSign: ZodiacSign,
    val transitDegree: Double,
    val mrityuBhagaDegree: Double,
    val distanceFromMrityuBhaga: Double,
    val isVulnerablePeriod: Boolean,
    val natalPlanetsAffected: List<Planet>,
    val cautionLevel: CautionLevel,
    val recommendations: List<String>
)

enum class CautionLevel {
    NONE, LOW, MODERATE, HIGH
}

data class OverallSensitiveDegreesAssessment(
    val level: AssessmentLevel,
    val criticalPlacementCount: Int,
    val auspiciousPlacementCount: Int,
    val summary: String
)

enum class AssessmentLevel {
    NEEDS_ATTENTION, MODERATE_CONCERN, BALANCED, GENERALLY_POSITIVE, HIGHLY_AUSPICIOUS
}
