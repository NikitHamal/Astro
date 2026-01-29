package com.astro.storm.ephemeris.bnn

import com.astro.storm.core.model.Planet
import com.astro.storm.core.model.VedicChart
import com.astro.storm.core.model.ZodiacSign
import com.astro.storm.ephemeris.VedicAstrologyUtils

/**
 * BNN Aspect Calculator
 *
 * Calculates aspects according to the Bhrigu Nandi Nadi system.
 * In BNN, the aspect system is different from standard Parashari aspects:
 *
 * ALL planets aspect positions 1, 2, 5, 7, 9, 12 from themselves
 * (unlike Parashari where different planets have different aspects)
 *
 * The significance is:
 * - 1st (Same Sign): Complete union/blending of energies
 * - 2nd/12th: Direct influence/exchange
 * - 5th/9th: Harmonious trine connection
 * - 7th: Opposition/confrontation
 *
 * @author AstroStorm - Ultra-Precision Vedic Astrology
 */
object BNNAspectCalculator {

    /**
     * BNN aspect distances (positions from a planet that it aspects)
     */
    private val BNN_ASPECT_DISTANCES = setOf(1, 2, 5, 7, 9, 12)

    /**
     * Calculate all BNN aspects in a chart
     */
    fun calculateAllAspects(chart: VedicChart): List<BNNAspect> {
        val aspects = mutableListOf<BNNAspect>()
        val positions = chart.planetPositions.associateBy { it.planet }

        // For each planet, check aspects to all other planets
        for (planet1 in Planet.MAIN_PLANETS) {
            val pos1 = positions[planet1] ?: continue
            val sign1 = pos1.sign

            for (planet2 in Planet.MAIN_PLANETS) {
                if (planet1 == planet2) continue

                val pos2 = positions[planet2] ?: continue
                val sign2 = pos2.sign

                // Calculate sign distance (1-12)
                val signDistance = calculateSignDistance(sign1, sign2)

                // Check if this is a valid BNN aspect
                if (signDistance in BNN_ASPECT_DISTANCES) {
                    val aspectType = getAspectType(signDistance)
                    val isMutual = isAspectMutual(sign1, sign2)

                    val interpretation = generateAspectInterpretation(
                        planet1, planet2, aspectType, isMutual
                    )

                    aspects.add(BNNAspect(
                        planet1 = planet1,
                        planet2 = planet2,
                        sign1 = sign1,
                        sign2 = sign2,
                        aspectType = aspectType,
                        signDistance = signDistance,
                        isMutual = isMutual,
                        interpretation = interpretation
                    ))
                }
            }
        }

        return aspects
    }

    /**
     * Calculate sign distance (1-12)
     */
    private fun calculateSignDistance(from: ZodiacSign, to: ZodiacSign): Int {
        val distance = ((to.number - from.number + 12) % 12)
        return if (distance == 0) 1 else distance + 1
    }

    /**
     * Get BNN aspect type from sign distance
     */
    private fun getAspectType(signDistance: Int): BNNAspectType {
        return when (signDistance) {
            1 -> BNNAspectType.CONJUNCTION
            2, 12 -> BNNAspectType.SECOND_TWELFTH
            5, 9 -> BNNAspectType.FIFTH_NINTH
            7 -> BNNAspectType.SEVENTH
            else -> BNNAspectType.CONJUNCTION // Fallback
        }
    }

    /**
     * Check if aspect is mutual (both planets aspect each other via same aspect type)
     */
    private fun isAspectMutual(sign1: ZodiacSign, sign2: ZodiacSign): Boolean {
        val dist1 = calculateSignDistance(sign1, sign2)
        val dist2 = calculateSignDistance(sign2, sign1)

        // Conjunction is always mutual
        if (dist1 == 1) return true

        // 2nd/12th are mutual to each other
        if ((dist1 == 2 && dist2 == 12) || (dist1 == 12 && dist2 == 2)) return true

        // 5th/9th are mutual to each other
        if ((dist1 == 5 && dist2 == 9) || (dist1 == 9 && dist2 == 5)) return true

        // 7th is always mutual
        if (dist1 == 7) return true

        return false
    }

    /**
     * Generate aspect interpretation
     */
    private fun generateAspectInterpretation(
        planet1: Planet,
        planet2: Planet,
        aspectType: BNNAspectType,
        isMutual: Boolean
    ): String {
        val mutualStr = if (isMutual) "mutual" else "one-way"
        val p1Name = planet1.displayName
        val p2Name = planet2.displayName

        val baseInterp = when (aspectType) {
            BNNAspectType.CONJUNCTION -> {
                "$p1Name and $p2Name are conjunct in the same sign, creating a powerful " +
                        "fusion of their energies. The significations of both planets blend together."
            }
            BNNAspectType.SECOND_TWELFTH -> {
                "$p1Name influences $p2Name through adjacent sign relationship ($mutualStr). " +
                        "This creates an exchange of resources and values between their significations."
            }
            BNNAspectType.FIFTH_NINTH -> {
                "$p1Name and $p2Name share a harmonious trine relationship ($mutualStr). " +
                        "Their energies support each other, bringing fortune and ease in combined significations."
            }
            BNNAspectType.SEVENTH -> {
                "$p1Name and $p2Name face each other in opposition ($mutualStr). " +
                        "This creates awareness, confrontation, and potential for balance between their significations."
            }
        }

        return "$baseInterp ${getPlanetCombinationMeaning(planet1, planet2, aspectType)}"
    }

    /**
     * Get specific meaning for planet combinations
     */
    private fun getPlanetCombinationMeaning(
        planet1: Planet,
        planet2: Planet,
        aspectType: BNNAspectType
    ): String {
        val pair = setOf(planet1, planet2)

        return when {
            // Sun combinations
            pair == setOf(Planet.SUN, Planet.MOON) -> {
                "Father-mother axis. Emotional connection to authority. Public recognition."
            }
            pair == setOf(Planet.SUN, Planet.MARS) -> {
                "Courage and authority. Leadership in action. Military or government connections."
            }
            pair == setOf(Planet.SUN, Planet.JUPITER) -> {
                "Wisdom and authority. Teaching, dharma, spiritual leadership. Father-son bonding."
            }
            pair == setOf(Planet.SUN, Planet.SATURN) -> {
                "Authority meets discipline. Government service. Father-related karma. Delayed recognition."
            }
            pair == setOf(Planet.SUN, Planet.VENUS) -> {
                "Creativity and recognition. Arts, entertainment. Relationship with authority."
            }
            pair == setOf(Planet.SUN, Planet.MERCURY) -> {
                "Communication and authority. Writing, speaking, administration."
            }
            pair == setOf(Planet.SUN, Planet.RAHU) -> {
                "Ambition for status. Unconventional rise. Foreign connections to authority."
            }
            pair == setOf(Planet.SUN, Planet.KETU) -> {
                "Spiritual authority. Detachment from ego. Past-life connections to power."
            }

            // Moon combinations
            pair == setOf(Planet.MOON, Planet.MARS) -> {
                "Emotional courage. Property matters. Mother-related action."
            }
            pair == setOf(Planet.MOON, Planet.JUPITER) -> {
                "Wisdom and emotions. Prosperity. Good fortune through mother/women."
            }
            pair == setOf(Planet.MOON, Planet.SATURN) -> {
                "Emotional restrictions. Delays in comfort. Mother-related karma."
            }
            pair == setOf(Planet.MOON, Planet.VENUS) -> {
                "Beauty and emotions. Artistic sensitivity. Relationship fulfillment."
            }
            pair == setOf(Planet.MOON, Planet.MERCURY) -> {
                "Mind and emotions. Communication of feelings. Literary abilities."
            }
            pair == setOf(Planet.MOON, Planet.RAHU) -> {
                "Emotional obsessions. Foreign connections. Unusual emotional patterns."
            }
            pair == setOf(Planet.MOON, Planet.KETU) -> {
                "Spiritual emotions. Detachment from comforts. Psychic sensitivity."
            }

            // Mars combinations
            pair == setOf(Planet.MARS, Planet.JUPITER) -> {
                "Action with wisdom. Teaching aggressive arts. Sports coaching."
            }
            pair == setOf(Planet.MARS, Planet.SATURN) -> {
                "Disciplined action. Engineering, construction. Delayed achievements."
            }
            pair == setOf(Planet.MARS, Planet.VENUS) -> {
                "Passion and creativity. Arts with energy. Romantic intensity."
            }
            pair == setOf(Planet.MARS, Planet.MERCURY) -> {
                "Quick action. Technical communication. Debate and argumentation."
            }
            pair == setOf(Planet.MARS, Planet.RAHU) -> {
                "Aggressive ambition. Unconventional action. Risk-taking."
            }
            pair == setOf(Planet.MARS, Planet.KETU) -> {
                "Spiritual warrior. Martial arts. Sudden events."
            }

            // Mercury combinations
            pair == setOf(Planet.MERCURY, Planet.JUPITER) -> {
                "Wisdom and intellect. Teaching, writing. Educational pursuits."
            }
            pair == setOf(Planet.MERCURY, Planet.SATURN) -> {
                "Systematic thinking. Research, accounts. Technical writing."
            }
            pair == setOf(Planet.MERCURY, Planet.VENUS) -> {
                "Artistic communication. Music, poetry. Business in arts."
            }
            pair == setOf(Planet.MERCURY, Planet.RAHU) -> {
                "Unconventional thinking. Technology, innovation. Foreign languages."
            }
            pair == setOf(Planet.MERCURY, Planet.KETU) -> {
                "Intuitive knowledge. Occult studies. Detached intellect."
            }

            // Jupiter combinations
            pair == setOf(Planet.JUPITER, Planet.SATURN) -> {
                "Wisdom and discipline. Traditional knowledge. Long-term growth."
            }
            pair == setOf(Planet.JUPITER, Planet.VENUS) -> {
                "Luxury with wisdom. Creative teaching. Abundant relationships."
            }
            pair == setOf(Planet.JUPITER, Planet.RAHU) -> {
                "Unconventional wisdom. Foreign teachers. Expansion through unusual means."
            }
            pair == setOf(Planet.JUPITER, Planet.KETU) -> {
                "Spiritual wisdom. Detached teaching. Moksha path."
            }

            // Venus combinations
            pair == setOf(Planet.VENUS, Planet.SATURN) -> {
                "Disciplined creativity. Classical arts. Delayed relationships."
            }
            pair == setOf(Planet.VENUS, Planet.RAHU) -> {
                "Unusual attractions. Foreign relationships. Glamour industry."
            }
            pair == setOf(Planet.VENUS, Planet.KETU) -> {
                "Spiritual art. Detached beauty. Past-life relationships."
            }

            // Saturn combinations
            pair == setOf(Planet.SATURN, Planet.RAHU) -> {
                "Hard work for ambition. Unconventional discipline. Foreign karma."
            }
            pair == setOf(Planet.SATURN, Planet.KETU) -> {
                "Spiritual discipline. Renunciation. Deep karma."
            }

            // Node combination
            pair == setOf(Planet.RAHU, Planet.KETU) -> {
                "Karmic axis. Past and future. Destiny points."
            }

            else -> "Combined influence of ${planet1.displayName} and ${planet2.displayName}."
        }
    }

    /**
     * Find all handshake yogas (mutual aspects)
     */
    fun findHandshakeYogas(aspects: List<BNNAspect>): List<HandshakeYoga> {
        val handshakes = mutableListOf<HandshakeYoga>()

        // Group aspects by planet pair
        val mutualAspects = aspects
            .filter { it.isMutual }
            .distinctBy { setOf(it.planet1, it.planet2) }

        for (aspect in mutualAspects) {
            val yogaName = generateHandshakeYogaName(aspect.planet1, aspect.planet2, aspect.aspectType)
            val lifeAreas = getLifeAreasForPlanets(aspect.planet1, aspect.planet2)
            val interpretation = generateHandshakeInterpretation(aspect)

            handshakes.add(HandshakeYoga(
                planet1 = aspect.planet1,
                planet2 = aspect.planet2,
                sign1 = aspect.sign1,
                sign2 = aspect.sign2,
                aspectType = aspect.aspectType,
                strength = aspect.aspectType.strength,
                yogaName = yogaName,
                interpretation = interpretation,
                lifeAreas = lifeAreas
            ))
        }

        return handshakes
    }

    /**
     * Generate handshake yoga name
     */
    private fun generateHandshakeYogaName(
        planet1: Planet,
        planet2: Planet,
        aspectType: BNNAspectType
    ): String {
        val aspectName = when (aspectType) {
            BNNAspectType.CONJUNCTION -> "Yuti"
            BNNAspectType.SECOND_TWELFTH -> "Dwirdwadash"
            BNNAspectType.FIFTH_NINTH -> "Trikona"
            BNNAspectType.SEVENTH -> "Saptama"
        }

        val shortName1 = planet1.displayName.take(3)
        val shortName2 = planet2.displayName.take(3)

        return "$shortName1-$shortName2 $aspectName Handshake"
    }

    /**
     * Get life areas affected by planetary combination
     */
    private fun getLifeAreasForPlanets(planet1: Planet, planet2: Planet): List<String> {
        val areas = mutableListOf<String>()

        // Sun significations
        if (Planet.SUN in listOf(planet1, planet2)) {
            areas.addAll(listOf("Authority", "Father", "Health", "Government"))
        }
        // Moon significations
        if (Planet.MOON in listOf(planet1, planet2)) {
            areas.addAll(listOf("Mind", "Mother", "Emotions", "Public"))
        }
        // Mars significations
        if (Planet.MARS in listOf(planet1, planet2)) {
            areas.addAll(listOf("Courage", "Property", "Siblings", "Energy"))
        }
        // Mercury significations
        if (Planet.MERCURY in listOf(planet1, planet2)) {
            areas.addAll(listOf("Communication", "Business", "Education", "Skills"))
        }
        // Jupiter significations
        if (Planet.JUPITER in listOf(planet1, planet2)) {
            areas.addAll(listOf("Wisdom", "Children", "Fortune", "Teaching"))
        }
        // Venus significations
        if (Planet.VENUS in listOf(planet1, planet2)) {
            areas.addAll(listOf("Relationships", "Art", "Luxury", "Creativity"))
        }
        // Saturn significations
        if (Planet.SATURN in listOf(planet1, planet2)) {
            areas.addAll(listOf("Career", "Discipline", "Longevity", "Service"))
        }
        // Rahu significations
        if (Planet.RAHU in listOf(planet1, planet2)) {
            areas.addAll(listOf("Ambition", "Foreign", "Technology", "Unconventional"))
        }
        // Ketu significations
        if (Planet.KETU in listOf(planet1, planet2)) {
            areas.addAll(listOf("Spirituality", "Liberation", "Past Life", "Intuition"))
        }

        return areas.distinct()
    }

    /**
     * Generate handshake interpretation
     */
    private fun generateHandshakeInterpretation(aspect: BNNAspect): String {
        val p1 = aspect.planet1.displayName
        val p2 = aspect.planet2.displayName

        return buildString {
            append("The mutual ${aspect.aspectType.description.lowercase()} between $p1 and $p2 ")
            append("creates a powerful handshake yoga. ")

            when (aspect.aspectType) {
                BNNAspectType.CONJUNCTION -> {
                    append("Both planets are in the same sign, creating intense blending of energies. ")
                    append("The native embodies qualities of both planets simultaneously. ")
                }
                BNNAspectType.FIFTH_NINTH -> {
                    append("This trine connection brings harmony and mutual support. ")
                    append("Activities related to either planet benefit the other. ")
                }
                BNNAspectType.SEVENTH -> {
                    append("This opposition creates awareness and balance. ")
                    append("The native must learn to integrate opposing forces. ")
                }
                BNNAspectType.SECOND_TWELFTH -> {
                    append("Adjacent sign connection creates resource exchange. ")
                    append("One planet feeds into the other's significations. ")
                }
            }

            append(aspect.interpretation)
        }
    }
}
