package com.astro.storm.ephemeris.bnn

import com.astro.storm.core.common.Language
import com.astro.storm.core.model.Planet
import com.astro.storm.core.model.ZodiacSign

/**
 * Bhrigu Nandi Nadi (BNN) Models
 *
 * Nadi astrology is a unique branch of Vedic astrology that uses:
 * 1. Planetary positions WITHOUT reference to houses/Lagna
 * 2. Unique aspect system: Planets aspect 1, 5, 9, 2, 12, and 7 positions from themselves
 * 3. "Planetary Links" formed by consecutive sign placements
 * 4. "Handshake" yoga when two planets mutually aspect each other
 *
 * The Bhrigu Nandi Nadi system focuses on:
 * - The RELATIONSHIP between planets (not houses)
 * - Consecutive sign patterns (planetary yogas)
 * - Graph traversal to find complex planetary links
 *
 * Classical References:
 * - Bhrigu Nandi Nadi (original palm leaf manuscripts)
 * - Dhruva Nadi
 * - Satya Jatakam
 *
 * @author AstroStorm - Ultra-Precision Vedic Astrology
 */

/**
 * BNN Aspect types
 * In Nadi astrology, planets aspect specific positions from themselves
 */
enum class BNNAspectType(
    val houseDistance: Int,
    val description: String,
    val strength: Double,
    val nature: AspectNature
) {
    CONJUNCTION(1, "Same Sign (Conjunction)", 1.0, AspectNature.UNION),
    SECOND_TWELFTH(2, "2nd/12th (Adjacent)", 0.75, AspectNature.INFLUENCE),
    FIFTH_NINTH(5, "5th/9th (Trine)", 0.9, AspectNature.HARMONY),
    SEVENTH(7, "7th (Opposition)", 0.85, AspectNature.CONFRONTATION);

    companion object {
        /**
         * Get all BNN aspect distances (1, 2, 5, 7, 9, 12)
         */
        fun getAllAspectDistances(): Set<Int> = setOf(1, 2, 5, 7, 9, 12)
    }
}

/**
 * Aspect nature classification
 */
enum class AspectNature {
    UNION,          // Same sign - complete blending
    HARMONY,        // Trines - supportive relationship
    INFLUENCE,      // Adjacent signs - influencing
    CONFRONTATION   // Opposition - facing/challenging
}

/**
 * A BNN Aspect between two planets
 */
data class BNNAspect(
    val planet1: Planet,
    val planet2: Planet,
    val sign1: ZodiacSign,
    val sign2: ZodiacSign,
    val aspectType: BNNAspectType,
    val signDistance: Int,
    val isMutual: Boolean,  // Both planets aspect each other
    val interpretation: String
)

/**
 * A Planetary Link in BNN
 * Represents a chain of planetary connections
 */
data class PlanetaryLink(
    val chain: List<Planet>,
    val signPath: List<ZodiacSign>,
    val linkType: LinkType,
    val strength: Double,
    val primaryIndication: String,
    val secondaryIndications: List<String>,
    val interpretation: String
) {
    val length: Int get() = chain.size

    /**
     * Get the starting and ending planets
     */
    val startPlanet: Planet get() = chain.first()
    val endPlanet: Planet get() = chain.last()

    /**
     * Check if a specific planet is part of this link
     */
    fun containsPlanet(planet: Planet): Boolean = chain.contains(planet)
}

/**
 * Types of planetary links
 */
enum class LinkType(val displayName: String, val description: String) {
    DIRECT_CONJUNCTION("Direct Conjunction", "Planets in same sign"),
    TRINE_LINK("Trine Link", "Planets connected via 5th/9th houses"),
    OPPOSITION_LINK("Opposition Link", "Planets connected via 7th house"),
    MIXED_LINK("Mixed Link", "Combination of aspect types"),
    CONSECUTIVE_SIGN("Consecutive Sign", "Planets in adjacent signs"),
    DISPOSITOR_CHAIN("Dispositor Chain", "Planets linked via sign lordship")
}

/**
 * Handshake Yoga - Mutual aspect in BNN
 */
data class HandshakeYoga(
    val planet1: Planet,
    val planet2: Planet,
    val sign1: ZodiacSign,
    val sign2: ZodiacSign,
    val aspectType: BNNAspectType,
    val strength: Double,
    val yogaName: String,
    val interpretation: String,
    val lifeAreas: List<String>
)

/**
 * Career indication based on planetary links
 */
data class CareerIndication(
    val primaryPlanets: List<Planet>,
    val supportingPlanets: List<Planet>,
    val careerField: String,
    val specificRoles: List<String>,
    val confidence: Double, // 0-1
    val explanation: String
)

/**
 * Known career link patterns in BNN
 */
enum class CareerLinkPattern(
    val planets: Set<Planet>,
    val careerField: String,
    val specificRoles: List<String>
) {
    // Engineering fields
    MECHANICAL_ENGINEER(
        setOf(Planet.JUPITER, Planet.MARS, Planet.SATURN),
        "Mechanical Engineering",
        listOf("Mechanical Engineer", "Manufacturing Engineer", "Industrial Designer", "Heavy Machinery Operator")
    ),
    SOFTWARE_ENGINEER(
        setOf(Planet.MERCURY, Planet.SATURN, Planet.RAHU),
        "Software/IT",
        listOf("Software Developer", "Data Scientist", "IT Consultant", "Systems Architect")
    ),
    CIVIL_ENGINEER(
        setOf(Planet.MARS, Planet.SATURN, Planet.VENUS),
        "Civil Engineering",
        listOf("Civil Engineer", "Architect", "Construction Manager", "Urban Planner")
    ),
    ELECTRICAL_ENGINEER(
        setOf(Planet.MARS, Planet.SUN, Planet.MERCURY),
        "Electrical Engineering",
        listOf("Electrical Engineer", "Electronics Designer", "Power Systems Engineer")
    ),

    // Medical fields
    DOCTOR_SURGEON(
        setOf(Planet.SUN, Planet.MARS, Planet.JUPITER),
        "Medicine - Surgery",
        listOf("Surgeon", "Physician", "Medical Specialist")
    ),
    DOCTOR_MEDICINE(
        setOf(Planet.SUN, Planet.JUPITER, Planet.MOON),
        "Medicine - General",
        listOf("General Physician", "Internist", "Family Doctor")
    ),
    NURSE_CAREGIVER(
        setOf(Planet.MOON, Planet.VENUS, Planet.JUPITER),
        "Healthcare - Nursing",
        listOf("Nurse", "Caregiver", "Health Worker", "Midwife")
    ),
    PHARMACIST(
        setOf(Planet.MERCURY, Planet.MOON, Planet.JUPITER),
        "Pharmacy",
        listOf("Pharmacist", "Drug Research", "Pharmaceutical Sales")
    ),

    // Finance and Commerce
    BANKER_FINANCE(
        setOf(Planet.JUPITER, Planet.MERCURY, Planet.VENUS),
        "Banking & Finance",
        listOf("Banker", "Financial Analyst", "Investment Manager", "CFO")
    ),
    ACCOUNTANT(
        setOf(Planet.MERCURY, Planet.SATURN, Planet.JUPITER),
        "Accounting",
        listOf("Chartered Accountant", "Auditor", "Tax Consultant", "Bookkeeper")
    ),
    STOCK_TRADING(
        setOf(Planet.MERCURY, Planet.RAHU, Planet.VENUS),
        "Stock Market/Trading",
        listOf("Stock Trader", "Investment Banker", "Hedge Fund Manager")
    ),

    // Creative fields
    ARTIST_MUSICIAN(
        setOf(Planet.VENUS, Planet.MOON, Planet.MERCURY),
        "Arts & Music",
        listOf("Artist", "Musician", "Composer", "Painter", "Sculptor")
    ),
    ACTOR_PERFORMER(
        setOf(Planet.VENUS, Planet.SUN, Planet.RAHU),
        "Acting & Performance",
        listOf("Actor", "Performer", "Entertainer", "Public Figure")
    ),
    WRITER_AUTHOR(
        setOf(Planet.MERCURY, Planet.JUPITER, Planet.MOON),
        "Writing & Literature",
        listOf("Author", "Journalist", "Content Writer", "Editor")
    ),

    // Teaching and Education
    TEACHER_PROFESSOR(
        setOf(Planet.JUPITER, Planet.MERCURY, Planet.SUN),
        "Education",
        listOf("Teacher", "Professor", "Academic Researcher", "Education Administrator")
    ),
    SPIRITUAL_TEACHER(
        setOf(Planet.JUPITER, Planet.KETU, Planet.MOON),
        "Spiritual Teaching",
        listOf("Spiritual Guide", "Religious Teacher", "Meditation Instructor", "Astrologer")
    ),

    // Law and Government
    LAWYER_JUDGE(
        setOf(Planet.JUPITER, Planet.SUN, Planet.MERCURY),
        "Law",
        listOf("Lawyer", "Judge", "Legal Consultant", "Advocate")
    ),
    GOVERNMENT_ADMIN(
        setOf(Planet.SUN, Planet.SATURN, Planet.JUPITER),
        "Government Administration",
        listOf("Government Officer", "Administrator", "Bureaucrat", "Diplomat")
    ),
    POLITICIAN(
        setOf(Planet.SUN, Planet.RAHU, Planet.MARS),
        "Politics",
        listOf("Politician", "Political Leader", "Public Servant")
    ),

    // Science and Research
    SCIENTIST_RESEARCHER(
        setOf(Planet.MERCURY, Planet.JUPITER, Planet.SATURN),
        "Science & Research",
        listOf("Scientist", "Researcher", "R&D Specialist", "Lab Director")
    ),
    ASTRONOMER_PHYSICIST(
        setOf(Planet.SATURN, Planet.JUPITER, Planet.SUN),
        "Physics & Astronomy",
        listOf("Physicist", "Astronomer", "Astrophysicist", "Space Scientist")
    ),

    // Business and Entrepreneurship
    BUSINESSMAN_ENTREPRENEUR(
        setOf(Planet.MERCURY, Planet.JUPITER, Planet.MARS),
        "Business/Entrepreneurship",
        listOf("Entrepreneur", "Business Owner", "CEO", "Director")
    ),
    REAL_ESTATE(
        setOf(Planet.MARS, Planet.VENUS, Planet.MERCURY),
        "Real Estate",
        listOf("Real Estate Developer", "Property Dealer", "Land Broker")
    ),

    // Defense and Security
    MILITARY_OFFICER(
        setOf(Planet.MARS, Planet.SUN, Planet.SATURN),
        "Military/Defense",
        listOf("Military Officer", "Army Personnel", "Defense Specialist")
    ),
    POLICE_SECURITY(
        setOf(Planet.MARS, Planet.SATURN, Planet.SUN),
        "Police/Security",
        listOf("Police Officer", "Security Professional", "Detective", "Intelligence Officer")
    ),

    // Agriculture and Nature
    AGRICULTURE(
        setOf(Planet.MOON, Planet.VENUS, Planet.MARS),
        "Agriculture",
        listOf("Farmer", "Agricultural Scientist", "Horticulturist")
    ),
    MARINE_SHIPPING(
        setOf(Planet.MOON, Planet.SATURN, Planet.VENUS),
        "Marine/Shipping",
        listOf("Sailor", "Ship Captain", "Marine Engineer", "Port Manager")
    )
}

/**
 * Complete BNN Analysis Result
 */
data class BNNAnalysisResult(
    val planetaryPositions: Map<Planet, ZodiacSign>,
    val allAspects: List<BNNAspect>,
    val handshakeYogas: List<HandshakeYoga>,
    val planetaryLinks: List<PlanetaryLink>,
    val careerIndications: List<CareerIndication>,
    val characterTraits: List<CharacterTrait>,
    val relationshipPatterns: List<RelationshipPattern>,
    val healthIndicators: List<HealthIndicator>,
    val keyInterpretations: List<String>,
    val language: Language = Language.ENGLISH
) {
    fun toPlainText(): String = buildString {
        val line = "═".repeat(60)
        appendLine(line)
        appendLine("         BHRIGU NANDI NADI (BNN) ANALYSIS")
        appendLine("         Planetary Links & Handshake Yogas")
        appendLine(line)
        appendLine()

        // Planetary Positions (without houses)
        appendLine("PLANETARY POSITIONS (Sign-Based)")
        appendLine("─".repeat(60))
        planetaryPositions.forEach { (planet, sign) ->
            appendLine("${planet.displayName.padEnd(12)}: ${sign.displayName}")
        }
        appendLine()

        // Handshake Yogas
        if (handshakeYogas.isNotEmpty()) {
            appendLine("HANDSHAKE YOGAS (Mutual Aspects)")
            appendLine("─".repeat(60))
            handshakeYogas.forEach { yoga ->
                appendLine("${yoga.yogaName}")
                appendLine("  ${yoga.planet1.displayName} ↔ ${yoga.planet2.displayName} (${yoga.aspectType.description})")
                appendLine("  Strength: ${String.format("%.0f", yoga.strength * 100)}%")
                appendLine("  ${yoga.interpretation}")
                appendLine()
            }
        }

        // Planetary Links
        if (planetaryLinks.isNotEmpty()) {
            appendLine("PLANETARY LINKS")
            appendLine("─".repeat(60))
            planetaryLinks.sortedByDescending { it.strength }.take(10).forEach { link ->
                val chainStr = link.chain.joinToString(" → ") { it.displayName }
                appendLine("$chainStr")
                appendLine("  Type: ${link.linkType.displayName}")
                appendLine("  Strength: ${String.format("%.0f", link.strength * 100)}%")
                appendLine("  Indication: ${link.primaryIndication}")
                appendLine()
            }
        }

        // Career Indications
        if (careerIndications.isNotEmpty()) {
            appendLine("CAREER INDICATIONS")
            appendLine("─".repeat(60))
            careerIndications.sortedByDescending { it.confidence }.take(5).forEach { career ->
                appendLine("${career.careerField} (${String.format("%.0f", career.confidence * 100)}% match)")
                appendLine("  Roles: ${career.specificRoles.joinToString(", ")}")
                appendLine("  Planets: ${career.primaryPlanets.joinToString(", ") { it.displayName }}")
                appendLine()
            }
        }

        // Key Interpretations
        if (keyInterpretations.isNotEmpty()) {
            appendLine("KEY INTERPRETATIONS")
            appendLine("─".repeat(60))
            keyInterpretations.forEachIndexed { index, interp ->
                appendLine("${index + 1}. $interp")
            }
        }
    }
}

/**
 * Character trait from BNN analysis
 */
data class CharacterTrait(
    val trait: String,
    val sourcePlanets: List<Planet>,
    val strength: Double,
    val description: String
)

/**
 * Relationship pattern from BNN
 */
data class RelationshipPattern(
    val patternName: String,
    val planets: List<Planet>,
    val relationshipType: String,
    val description: String,
    val challenges: List<String>,
    val strengths: List<String>
)

/**
 * Health indicator from BNN
 */
data class HealthIndicator(
    val bodyPart: String,
    val planets: List<Planet>,
    val vulnerability: Double,
    val recommendations: List<String>
)

/**
 * Graph node for BNN analysis
 */
data class BNNGraphNode(
    val planet: Planet,
    val sign: ZodiacSign,
    val connections: MutableList<BNNGraphEdge> = mutableListOf()
)

/**
 * Graph edge for BNN analysis
 */
data class BNNGraphEdge(
    val targetPlanet: Planet,
    val aspectType: BNNAspectType,
    val signDistance: Int,
    val weight: Double
)
