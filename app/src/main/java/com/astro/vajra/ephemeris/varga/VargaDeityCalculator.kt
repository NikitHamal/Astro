package com.astro.vajra.ephemeris.varga

import com.astro.vajra.core.model.Planet
import com.astro.vajra.core.model.PlanetPosition
import com.astro.vajra.core.model.VedicChart
import com.astro.vajra.core.model.ZodiacSign
import com.astro.vajra.ephemeris.AstrologicalConstants
import com.astro.vajra.ephemeris.DivisionalChartCalculator
import com.astro.vajra.ephemeris.DivisionalChartData
import com.astro.vajra.ephemeris.DivisionalChartType
import com.astro.vajra.ephemeris.VedicAstrologyUtils
import kotlin.math.floor

// =====================================================================================
// VARGA DEITY DATA MODELS
// =====================================================================================

/**
 * The nature/classification of a Varga deity, determining whether the deity's
 * influence is benefic, malefic, or neutral for the planet placed in its division.
 */
enum class DeityNature(val displayName: String) {
    /** Divine/benefic deity - amplifies positive results */
    DEVA("Deva (Divine)"),
    /** Demonic/malefic deity - introduces obstacles and challenges */
    ASURA("Asura (Demonic)"),
    /** Human/mixed deity - neutral influence modulated by planetary state */
    MANUSHYA("Manushya (Human)"),
    /** Mixed influence deity - results depend heavily on associated planet's dignity */
    MIXED("Mixed")
}

/**
 * Information about a deity governing a planet's position in a specific Varga chart.
 *
 * @property planet The planet placed in this Varga division
 * @property vargaType The type of divisional chart (D1, D3, D7, D9, D10, D60, etc.)
 * @property deityName The name of the presiding deity for this division
 * @property deityNature The classification of the deity (Deva/Asura/Manushya/Mixed)
 * @property longitude The planet's longitude in the Varga chart
 * @property sign The zodiac sign in the Varga chart
 * @property interpretation Contextual interpretation of the deity-planet combination
 */
data class VargaDeityInfo(
    val planet: Planet,
    val vargaType: DivisionalChartType,
    val deityName: String,
    val deityNature: DeityNature,
    val longitude: Double,
    val sign: ZodiacSign,
    val interpretation: String
)

/**
 * Analysis of D60 (Shashtyamsha) deities for all planets.
 *
 * @property planetDeities Map of each planet to its D60 deity information
 * @property beneficCount Number of planets in benefic D60 deities
 * @property maleficCount Number of planets in malefic D60 deities
 * @property summary Textual summary of the D60 analysis
 */
data class D60ShashtyamshaAnalysis(
    val planetDeities: Map<Planet, VargaDeityInfo>,
    val beneficCount: Int,
    val maleficCount: Int,
    val summary: String
)

/**
 * Analysis of D10 (Dasamsa) Dikpala assignments for all planets.
 *
 * @property planetDikpalas Map of each planet to its D10 directional guardian info
 * @property dominantDikpala The most frequently occurring Dikpala in the chart
 * @property careerDeityInfluence Interpretation of career implications from Dikpala placement
 */
data class D10DikpalaAnalysis(
    val planetDikpalas: Map<Planet, VargaDeityInfo>,
    val dominantDikpala: String,
    val careerDeityInfluence: String
)

/**
 * Analysis of D9 (Navamsa) deity information including Pushkara Navamsa.
 *
 * @property planetDeities Map of each planet to its D9 deity information
 * @property pushkaraPlanets Planets placed in Pushkara Navamsas (most auspicious)
 * @property pushkaraNavamsaDetails Details of Pushkara placement for each qualifying planet
 */
data class D9NavamsaDeityAnalysis(
    val planetDeities: Map<Planet, VargaDeityInfo>,
    val pushkaraPlanets: List<Planet>,
    val pushkaraNavamsaDetails: List<String>
)

/**
 * Analysis of D7 (Saptamsa) deity assignments for all planets.
 *
 * @property planetDeities Map of each planet to its D7 deity information
 * @property fertilityDeityBalance Balance between nourishing and depleting deities
 */
data class D7SaptamsaAnalysis(
    val planetDeities: Map<Planet, VargaDeityInfo>,
    val fertilityDeityBalance: String
)

/**
 * Analysis of D3 (Drekkana) deity/form assignments for all planets.
 *
 * @property planetDeities Map of each planet to its D3 deity/form information
 * @property drekkanaFormDescription Summary of Drekkana forms and their implications
 */
data class D3DrekkanaAnalysis(
    val planetDeities: Map<Planet, VargaDeityInfo>,
    val drekkanaFormDescription: String
)

/**
 * Overall balance of Deva vs. Asura deity influences across all Varga charts.
 *
 * @property devaCount Total benefic deity placements across all analyzed Vargas
 * @property asuraCount Total malefic deity placements across all analyzed Vargas
 * @property manushyaCount Total neutral/human deity placements
 * @property mixedCount Total mixed-nature deity placements
 * @property overallBalance Descriptive assessment of the cosmic balance
 * @property dominantNature The predominant deity nature in the chart
 */
data class DeityBalance(
    val devaCount: Int,
    val asuraCount: Int,
    val manushyaCount: Int,
    val mixedCount: Int,
    val overallBalance: String,
    val dominantNature: DeityNature
)

/**
 * Complete Varga Deity analysis encompassing D3, D7, D9, D10, and D60 charts.
 *
 * @property planetDeities Map of each planet to all its Varga deity info across charts
 * @property d60Analysis Detailed D60 Shashtyamsha deity analysis
 * @property d10Analysis Detailed D10 Dasamsa Dikpala analysis
 * @property d9Analysis Detailed D9 Navamsa deity analysis
 * @property d7Analysis Detailed D7 Saptamsa deity analysis
 * @property d3Analysis Detailed D3 Drekkana form analysis
 * @property overallDeityBalance Aggregate deity nature balance
 * @property significantFindings Key observations from the deity analysis
 */
data class VargaDeityAnalysis(
    val planetDeities: Map<Planet, List<VargaDeityInfo>>,
    val d60Analysis: D60ShashtyamshaAnalysis,
    val d10Analysis: D10DikpalaAnalysis,
    val d9Analysis: D9NavamsaDeityAnalysis,
    val d7Analysis: D7SaptamsaAnalysis,
    val d3Analysis: D3DrekkanaAnalysis,
    val overallDeityBalance: DeityBalance,
    val significantFindings: List<String>
)

// =====================================================================================
// VARGA DEITY CALCULATOR
// =====================================================================================

/**
 * Varga Deity and Shakti Interpretation Calculator.
 *
 * This calculator maps planets in divisional charts to their presiding deities
 * according to classical Vedic astrological texts, primarily:
 * - **D60 (Shashtyamsha)**: 60 deities per sign from BPHS Chapter 6
 * - **D10 (Dasamsa)**: 10 Dikpalas (Directional Guardians)
 * - **D9 (Navamsa)**: Nava Nayaka deities + Pushkara Navamsa identification
 * - **D7 (Saptamsa)**: 7 liquid/element deities
 * - **D3 (Drekkana)**: 36 Drekkana forms from Brihat Jataka
 *
 * The deity associated with a planet's Varga position reveals the subtle karmic
 * flavor of that planet's expression in the corresponding life domain. Benefic
 * deities amplify positive results while malefic deities introduce challenges
 * requiring remediation.
 *
 * **Classical References:**
 * - Brihat Parashara Hora Shastra (BPHS), Chapter 6 (Shashtyamsha)
 * - Varahamihira's Brihat Jataka (Drekkana forms)
 * - Phaladeepika (Dikpala assignments)
 *
 * @author AstroVajra
 */
object VargaDeityCalculator {

    // ============================================================================
    // D60 SHASHTYAMSHA DEITIES (60 per sign, from BPHS Chapter 6)
    // ============================================================================

    /**
     * The 60 Shashtyamsha deity names in order (for ODD signs).
     * For EVEN signs, the order is reversed (60 to 1).
     *
     * Each deity spans 0 degrees 30 arc-minutes (30 / 60 = 0.5 degrees) within a sign.
     */
    private val D60_DEITIES = listOf(
        "Ghora", "Rakshasa", "Deva", "Kubera", "Yaksha",
        "Kinnara", "Bhrashta", "Kulaghna", "Garala", "Agni",
        "Maya", "Purishaka", "Apampathi", "Marut", "Kaala",
        "Sarpa", "Amrita", "Indu", "Mridu", "Komala",
        "Heramba", "Brahma", "Vishnu", "Maheshwara", "Deva",
        "Ardra", "Kalinasa", "Kshiteesa", "Kamalakara", "Gulika",
        "Mrityu", "Kaala", "Davagni", "Ghora", "Yama",
        "Kantaka", "Sudha", "Amrita", "Purnachandra", "Vishagdha",
        "Kulanasa", "Vamshakshaya", "Utpata", "Kaala", "Saumya",
        "Komala", "Sheetala", "Karala Damshtra", "Chandramukhi", "Praveena",
        "Kalagni", "Dandayudha", "Nirmala", "Saumya", "Kroora",
        "Atisheetala", "Amrita", "Payodhi", "Bhramana", "Chandrarekha"
    )

    /**
     * Nature classification for each of the 60 Shashtyamsha deities.
     * Based on BPHS interpretation of deity characteristics.
     */
    private val D60_DEITY_NATURES = listOf(
        DeityNature.ASURA,     // 1. Ghora (Terrible)
        DeityNature.ASURA,     // 2. Rakshasa (Demon)
        DeityNature.DEVA,      // 3. Deva (God)
        DeityNature.DEVA,      // 4. Kubera (Wealth Lord)
        DeityNature.DEVA,      // 5. Yaksha (Nature Spirit)
        DeityNature.DEVA,      // 6. Kinnara (Celestial Musician)
        DeityNature.ASURA,     // 7. Bhrashta (Fallen)
        DeityNature.ASURA,     // 8. Kulaghna (Family Destroyer)
        DeityNature.ASURA,     // 9. Garala (Poison)
        DeityNature.MIXED,     // 10. Agni (Fire) - transformative
        DeityNature.MIXED,     // 11. Maya (Illusion)
        DeityNature.ASURA,     // 12. Purishaka (Filth)
        DeityNature.DEVA,      // 13. Apampathi (Water Lord)
        DeityNature.MIXED,     // 14. Marut (Wind)
        DeityNature.ASURA,     // 15. Kaala (Time/Death)
        DeityNature.ASURA,     // 16. Sarpa (Serpent)
        DeityNature.DEVA,      // 17. Amrita (Nectar)
        DeityNature.DEVA,      // 18. Indu (Moon)
        DeityNature.DEVA,      // 19. Mridu (Gentle)
        DeityNature.DEVA,      // 20. Komala (Tender)
        DeityNature.DEVA,      // 21. Heramba (Ganapati)
        DeityNature.DEVA,      // 22. Brahma (Creator)
        DeityNature.DEVA,      // 23. Vishnu (Preserver)
        DeityNature.DEVA,      // 24. Maheshwara (Shiva)
        DeityNature.DEVA,      // 25. Deva (God)
        DeityNature.MIXED,     // 26. Ardra (Moist)
        DeityNature.DEVA,      // 27. Kalinasa (Kali Destroyer)
        DeityNature.DEVA,      // 28. Kshiteesa (Earth Lord)
        DeityNature.DEVA,      // 29. Kamalakara (Lotus Maker)
        DeityNature.ASURA,     // 30. Gulika (Poison)
        DeityNature.ASURA,     // 31. Mrityu (Death)
        DeityNature.ASURA,     // 32. Kaala (Time)
        DeityNature.ASURA,     // 33. Davagni (Forest Fire)
        DeityNature.ASURA,     // 34. Ghora (Terrible)
        DeityNature.ASURA,     // 35. Yama (Death God)
        DeityNature.ASURA,     // 36. Kantaka (Thorn)
        DeityNature.DEVA,      // 37. Sudha (Nectar)
        DeityNature.DEVA,      // 38. Amrita (Immortal)
        DeityNature.DEVA,      // 39. Purnachandra (Full Moon)
        DeityNature.ASURA,     // 40. Vishagdha (Poisoned)
        DeityNature.ASURA,     // 41. Kulanasa (Family Destroyer)
        DeityNature.ASURA,     // 42. Vamshakshaya (Dynasty End)
        DeityNature.ASURA,     // 43. Utpata (Calamity)
        DeityNature.ASURA,     // 44. Kaala (Time)
        DeityNature.DEVA,      // 45. Saumya (Gentle)
        DeityNature.DEVA,      // 46. Komala (Soft)
        DeityNature.DEVA,      // 47. Sheetala (Cool)
        DeityNature.ASURA,     // 48. Karala Damshtra (Fierce Fangs)
        DeityNature.DEVA,      // 49. Chandramukhi (Moon Face)
        DeityNature.MANUSHYA,  // 50. Praveena (Expert)
        DeityNature.ASURA,     // 51. Kalagni (Fire of Time)
        DeityNature.MIXED,     // 52. Dandayudha (Staff Armed)
        DeityNature.DEVA,      // 53. Nirmala (Pure)
        DeityNature.DEVA,      // 54. Saumya (Gentle)
        DeityNature.ASURA,     // 55. Kroora (Cruel)
        DeityNature.DEVA,      // 56. Atisheetala (Very Cool)
        DeityNature.DEVA,      // 57. Amrita (Nectar)
        DeityNature.DEVA,      // 58. Payodhi (Ocean)
        DeityNature.MANUSHYA,  // 59. Bhramana (Wandering)
        DeityNature.DEVA       // 60. Chandrarekha (Moon Line)
    )

    // ============================================================================
    // D10 DIKPALA (DIRECTIONAL GUARDIANS)
    // ============================================================================

    /**
     * The 10 Dikpalas in order for ODD signs.
     * For EVEN signs, the order is reversed.
     * Each Dikpala governs a 3-degree division within a sign.
     */
    private val D10_DIKPALAS = listOf(
        "Indra", "Agni", "Yama", "Nirriti", "Varuna",
        "Vayu", "Kubera", "Ishana", "Brahma", "Ananta"
    )

    /**
     * Nature classification for each Dikpala.
     */
    private val D10_DIKPALA_NATURES = listOf(
        DeityNature.DEVA,      // Indra - King of Gods, authority
        DeityNature.MIXED,     // Agni - Fire, transformative
        DeityNature.ASURA,     // Yama - Death, restrictions
        DeityNature.ASURA,     // Nirriti - Destruction, decay
        DeityNature.DEVA,      // Varuna - Cosmic order, dharma
        DeityNature.MIXED,     // Vayu - Wind, change
        DeityNature.DEVA,      // Kubera - Wealth, prosperity
        DeityNature.DEVA,      // Ishana - Shiva, transcendence
        DeityNature.DEVA,      // Brahma - Creation, knowledge
        DeityNature.DEVA       // Ananta - Infinity, sustenance
    )

    /**
     * Career significance for each Dikpala placement.
     */
    private val D10_CAREER_SIGNIFICANCE = mapOf(
        "Indra" to "Leadership roles, government positions, authority and command. " +
                "Indicates success in politics, administration, and executive positions.",
        "Agni" to "Transformative careers, technology, engineering, energy sectors. " +
                "Indicates purifying or transformational role in profession.",
        "Yama" to "Justice-related fields, law enforcement, judiciary, auditing. " +
                "Career may involve dealing with endings, transitions, or discipline.",
        "Nirriti" to "Challenging career path requiring transformation. May involve " +
                "demolition, restructuring, pathology, or crisis management.",
        "Varuna" to "Maritime, water-related industries, diplomacy, international relations. " +
                "Career connected to cosmic order, ethics, and vast enterprises.",
        "Vayu" to "Transportation, aviation, communication, logistics, media. " +
                "Career involves movement, speed, and connectivity.",
        "Kubera" to "Finance, banking, treasury, wealth management, luxury goods. " +
                "Strong indicator of financial success and material prosperity in career.",
        "Ishana" to "Spiritual leadership, philosophy, higher education, research. " +
                "Career connected to transcendence, teaching, and wisdom traditions.",
        "Brahma" to "Creative fields, knowledge industries, education, writing. " +
                "Career involves creation, origination, and intellectual pursuits.",
        "Ananta" to "Sustaining roles, long-term projects, infrastructure, foundations. " +
                "Career characterized by endurance, stability, and infinite potential."
    )

    // ============================================================================
    // D9 NAVAMSA DEITIES & PUSHKARA NAVAMSAS
    // ============================================================================

    /**
     * Nava Nayaka (Nine Planetary Deities) mapped to Navamsa signs.
     * The deity corresponds to the sign's ruler in its divine form.
     */
    private val NAVAMSA_SIGN_DEITIES = mapOf(
        ZodiacSign.ARIES to "Mangala Deva (Mars Divine)",
        ZodiacSign.TAURUS to "Shukra Deva (Venus Divine)",
        ZodiacSign.GEMINI to "Budha Deva (Mercury Divine)",
        ZodiacSign.CANCER to "Chandra Deva (Moon Divine)",
        ZodiacSign.LEO to "Surya Deva (Sun Divine)",
        ZodiacSign.VIRGO to "Budha Deva (Mercury Divine)",
        ZodiacSign.LIBRA to "Shukra Deva (Venus Divine)",
        ZodiacSign.SCORPIO to "Mangala Deva (Mars Divine)",
        ZodiacSign.SAGITTARIUS to "Guru Deva (Jupiter Divine)",
        ZodiacSign.CAPRICORN to "Shani Deva (Saturn Divine)",
        ZodiacSign.AQUARIUS to "Shani Deva (Saturn Divine)",
        ZodiacSign.PISCES to "Guru Deva (Jupiter Divine)"
    )

    /**
     * Pushkara Navamsas - the most auspicious Navamsa degrees.
     *
     * Pushkara Navamsas are specific degree ranges within signs that are considered
     * supremely benefic. A planet placed in a Pushkara Navamsa is nourished and
     * protected, giving excellent results regardless of other afflictions.
     *
     * Traditional Pushkara Navamsas (degree ranges within each sign):
     * The Pushkara degrees are at specific navamsa boundaries that fall in
     * the signs of benefic planets (Jupiter, Venus).
     *
     * Per classical texts, the Pushkara Navamsas are:
     * - Aries: 21d20'-24d40' (Sagittarius Navamsa) and 24d40'-28d00' (Capricorn Navamsa)
     * -- Corrected: standard Pushkara navamsas for each sign.
     *
     * Simplified identification: Navamsas falling in Cancer, Taurus, Sagittarius,
     * and Pisces are traditionally Pushkara when they occur at specific positions.
     */
    private val PUSHKARA_NAVAMSA_RANGES: Map<ZodiacSign, List<Pair<Double, Double>>> = mapOf(
        ZodiacSign.ARIES to listOf(21.333 to 24.667),       // 7th navamsa (Libra)
        ZodiacSign.TAURUS to listOf(7.333 to 10.667),       // 3rd navamsa (Cancer)
        ZodiacSign.GEMINI to listOf(24.0 to 27.333),        // 8th navamsa (Capricorn)
        ZodiacSign.CANCER to listOf(10.667 to 14.0),        // 4th navamsa (Libra)
        ZodiacSign.LEO to listOf(27.333 to 30.0),           // 9th navamsa (Pisces)
        ZodiacSign.VIRGO to listOf(14.0 to 17.333),         // 5th navamsa (Capricorn)
        ZodiacSign.LIBRA to listOf(0.0 to 3.333),           // 1st navamsa (Libra)
        ZodiacSign.SCORPIO to listOf(17.333 to 20.667),     // 6th navamsa (Aries)
        ZodiacSign.SAGITTARIUS to listOf(3.333 to 6.667),   // 2nd navamsa (Capricorn)
        ZodiacSign.CAPRICORN to listOf(20.667 to 24.0),     // 7th navamsa (Cancer)
        ZodiacSign.AQUARIUS to listOf(6.667 to 10.0),       // 3rd navamsa (Pisces)
        ZodiacSign.PISCES to listOf(23.333 to 26.667)       // 8th navamsa (Libra)
    )

    // ============================================================================
    // D7 SAPTAMSA DEITIES
    // ============================================================================

    /**
     * D7 Saptamsa deities for ODD signs.
     * Each deity governs approximately 4.2857 degrees (30/7) within a sign.
     * For EVEN signs, the order reverses.
     */
    private val D7_DEITIES_ODD = listOf(
        "Kshara", "Ksheera", "Dadhi", "Aajya", "Ikshu-rasa", "Madya", "Shuddha-jala"
    )

    /**
     * Nature classification for D7 deities.
     * Nourishing substances are Deva, intoxicating are Mixed, depleting are Asura.
     */
    private val D7_DEITY_NATURES = listOf(
        DeityNature.MIXED,     // Kshara (Alkaline/Caustic water)
        DeityNature.DEVA,      // Ksheera (Milk) - nourishing
        DeityNature.DEVA,      // Dadhi (Curd/Yogurt) - nourishing
        DeityNature.DEVA,      // Aajya (Ghee) - sacred, nourishing
        DeityNature.DEVA,      // Ikshu-rasa (Sugarcane juice) - sweet
        DeityNature.ASURA,     // Madya (Wine/Alcohol) - intoxicating
        DeityNature.DEVA       // Shuddha-jala (Pure water) - purifying
    )

    // ============================================================================
    // D3 DREKKANA FORMS (36 Drekkana Deities from Brihat Jataka)
    // ============================================================================

    /**
     * The 36 Drekkana forms per Varahamihira's Brihat Jataka.
     * 3 Drekkanas per sign x 12 signs = 36 total.
     * Each Drekkana spans 10 degrees within a sign.
     *
     * Format: Triple(signNumber, dekkanaNumber, description)
     */
    private val D3_DREKKANA_FORMS: Map<Pair<Int, Int>, Triple<String, DeityNature, String>> = mapOf(
        // Aries Drekkanas
        Pair(1, 1) to Triple("A man with a white cloth, dark complexion, red eyes, holding an axe",
            DeityNature.MIXED, "Mars energy: courage, pioneering spirit, initiative"),
        Pair(1, 2) to Triple("A woman with red cloth, pot-bellied, fond of food and pleasures",
            DeityNature.MANUSHYA, "Venus sub-energy: desire nature, sensual expression"),
        Pair(1, 3) to Triple("A man skilled in arts, cruel, yellowish complexion, restless",
            DeityNature.MIXED, "Mercury-Jupiter mix: intellectual aggression"),

        // Taurus Drekkanas
        Pair(2, 1) to Triple("A woman with torn clothes, hungry, thirsty, disheveled hair",
            DeityNature.ASURA, "Venus afflicted: material struggles, unfulfilled desires"),
        Pair(2, 2) to Triple("A man with a large body, farmlands, flocks, determination",
            DeityNature.DEVA, "Mercury-Saturn: agricultural wealth, perseverance"),
        Pair(2, 3) to Triple("A man with an elephant body, white teeth, forest dweller",
            DeityNature.MANUSHYA, "Saturn energy: patience, endurance, wild nature"),

        // Gemini Drekkanas
        Pair(3, 1) to Triple("A woman skilled in needlework, fond of ornaments, beautiful",
            DeityNature.DEVA, "Mercury creativity: artistic skill, craftsmanship"),
        Pair(3, 2) to Triple("A man in armor, wearing a quiver, residing in a garden",
            DeityNature.MIXED, "Venus-Mars: warrior aesthete, strategic beauty"),
        Pair(3, 3) to Triple("A man armed with bows, adorned with gems, musician",
            DeityNature.DEVA, "Saturn-Aquarius: artistic discipline, musical talent"),

        // Cancer Drekkanas
        Pair(4, 1) to Triple("A man on horseback with serpents on body, hog face, searching for wife",
            DeityNature.MIXED, "Moon energy: emotional searching, instinctual nature"),
        Pair(4, 2) to Triple("A woman with lotus flowers, trees, serpents in garden",
            DeityNature.DEVA, "Mars-Scorpio: feminine power amidst nature"),
        Pair(4, 3) to Triple("A man adorned with gold, serpents on head, crying in distress",
            DeityNature.ASURA, "Jupiter-Pisces: material wealth with emotional suffering"),

        // Leo Drekkanas
        Pair(5, 1) to Triple("A vulture, dog, man in dirty clothes, fierce-looking",
            DeityNature.ASURA, "Sun-Mars: fierce authority, ruthless power"),
        Pair(5, 2) to Triple("A man with a horse head, white colored, wearing white",
            DeityNature.DEVA, "Jupiter energy: noble bearing, dharmic leadership"),
        Pair(5, 3) to Triple("A man with a bear face, monkey, hairy body",
            DeityNature.MANUSHYA, "Mars-Aries: primal strength, warrior archetype"),

        // Virgo Drekkanas
        Pair(6, 1) to Triple("A woman with a cooking pot, going to a temple, beautiful dress",
            DeityNature.DEVA, "Mercury-Virgo: domestic skill, devotional service"),
        Pair(6, 2) to Triple("A man dark-complexioned, writing, tall, armed with pen",
            DeityNature.MANUSHYA, "Saturn-Capricorn: scholarly discipline, literary talent"),
        Pair(6, 3) to Triple("A woman yellowish-white, tall, moving towards temple, covered with cloth",
            DeityNature.DEVA, "Venus-Taurus: spiritual devotion, aesthetic purity"),

        // Libra Drekkanas
        Pair(7, 1) to Triple("A man in marketplace, holding scales, thinking of merchandise",
            DeityNature.MANUSHYA, "Venus-Libra: commerce, fairness, trade acumen"),
        Pair(7, 2) to Triple("A man resembling a vulture, hungry, wandering, thinking of wife and wealth",
            DeityNature.ASURA, "Saturn-Aquarius: detachment struggle, material anxieties"),
        Pair(7, 3) to Triple("A man golden-adorned, monkey-like, armed with fruits and flesh",
            DeityNature.MIXED, "Mercury-Gemini: versatility, resourcefulness"),

        // Scorpio Drekkanas
        Pair(8, 1) to Triple("A beautiful woman, without clothes, bound, crossing ocean on a plank",
            DeityNature.ASURA, "Mars-Scorpio: crisis navigation, vulnerability in transformation"),
        Pair(8, 2) to Triple("A woman with serpents on body, bound, distressed, harassed by foes",
            DeityNature.ASURA, "Jupiter-Pisces: karmic bondage, serpent energy"),
        Pair(8, 3) to Triple("A dog, tortoise, pig faces combined with human body",
            DeityNature.ASURA, "Moon-Cancer: primal instincts, emotional depth"),

        // Sagittarius Drekkanas
        Pair(9, 1) to Triple("A man with horse body (centaur), armed with bow in a stable",
            DeityNature.DEVA, "Jupiter-Sagittarius: dharmic warrior, philosophical archer"),
        Pair(9, 2) to Triple("A beautiful woman picking fruits in a garden",
            DeityNature.DEVA, "Mars-Aries: active beauty, fertile productivity"),
        Pair(9, 3) to Triple("A man golden-colored, wearing silk, seated on a golden throne",
            DeityNature.DEVA, "Sun-Leo: royal authority, golden splendor"),

        // Capricorn Drekkanas
        Pair(10, 1) to Triple("A man with deer face, pig body, holding net and rope, searching for wealth",
            DeityNature.MANUSHYA, "Saturn-Capricorn: material pursuit, strategic gathering"),
        Pair(10, 2) to Triple("A woman expert in arts, beautiful face, seeking husband",
            DeityNature.DEVA, "Venus-Taurus: artistic feminine energy, relational seeking"),
        Pair(10, 3) to Triple("A man armed with quiver and bow, hairy body, searching for food",
            DeityNature.MIXED, "Mercury-Virgo: survival instinct, analytical hunting"),

        // Aquarius Drekkanas
        Pair(11, 1) to Triple("A woman with dirty clothes, head adorned with serpent, metal worker",
            DeityNature.MIXED, "Saturn-Aquarius: unconventional skill, metallurgical knowledge"),
        Pair(11, 2) to Triple("A man with pot body, woman face, hairy, dressed in iron",
            DeityNature.ASURA, "Mercury-Virgo: androgynous energy, industrial nature"),
        Pair(11, 3) to Triple("A dark man, angry, wearing a deerskin, holding weapons",
            DeityNature.ASURA, "Venus-Libra darkened: suppressed beauty, aggressive balance-seeking"),

        // Pisces Drekkanas
        Pair(12, 1) to Triple("A man with ornaments, carrying gems across the sea",
            DeityNature.DEVA, "Jupiter-Pisces: spiritual wealth, ocean crossing"),
        Pair(12, 2) to Triple("A beautiful woman crossing the sea in a boat with treasure",
            DeityNature.DEVA, "Moon-Cancer: emotional navigation, treasure of intuition"),
        Pair(12, 3) to Triple("A man with serpent body, worn out, thinking of wife and wealth",
            DeityNature.ASURA, "Mars-Scorpio: serpentine transformation, material attachment")
    )

    // ============================================================================
    // PUBLIC API
    // ============================================================================

    /**
     * Performs a complete Varga Deity analysis across D3, D7, D9, D10, and D60 charts.
     *
     * This is the primary entry point. It calculates all divisional charts,
     * identifies presiding deities for each planet in each Varga, classifies
     * the deity natures, and generates interpretive insights.
     *
     * @param chart The native's Vedic birth chart
     * @return Complete [VargaDeityAnalysis] with all deity mappings and interpretations
     */
    fun analyze(chart: VedicChart): VargaDeityAnalysis {
        val d3Chart = DivisionalChartCalculator.calculateDivisionalChart(chart, DivisionalChartType.D3_DREKKANA)
        val d7Chart = DivisionalChartCalculator.calculateDivisionalChart(chart, DivisionalChartType.D7_SAPTAMSA)
        val d9Chart = DivisionalChartCalculator.calculateDivisionalChart(chart, DivisionalChartType.D9_NAVAMSA)
        val d10Chart = DivisionalChartCalculator.calculateDivisionalChart(chart, DivisionalChartType.D10_DASAMSA)
        val d60Chart = DivisionalChartCalculator.calculateDivisionalChart(chart, DivisionalChartType.D60_SHASHTIAMSA)

        // Analyze each Varga
        val d60Analysis = analyzeD60(d60Chart, chart)
        val d10Analysis = analyzeD10(d10Chart, chart)
        val d9Analysis = analyzeD9(d9Chart, chart)
        val d7Analysis = analyzeD7(d7Chart, chart)
        val d3Analysis = analyzeD3(d3Chart, chart)

        // Build combined planet-deity map
        val planetDeities = buildPlanetDeityMap(
            d60Analysis, d10Analysis, d9Analysis, d7Analysis, d3Analysis
        )

        // Calculate overall balance
        val allDeities = planetDeities.values.flatten()
        val overallBalance = calculateDeityBalance(allDeities)

        // Generate significant findings
        val significantFindings = generateSignificantFindings(
            d60Analysis, d10Analysis, d9Analysis, d7Analysis, d3Analysis, chart
        )

        return VargaDeityAnalysis(
            planetDeities = planetDeities,
            d60Analysis = d60Analysis,
            d10Analysis = d10Analysis,
            d9Analysis = d9Analysis,
            d7Analysis = d7Analysis,
            d3Analysis = d3Analysis,
            overallDeityBalance = overallBalance,
            significantFindings = significantFindings
        )
    }

    // ============================================================================
    // D60 ANALYSIS
    // ============================================================================

    /**
     * Analyzes D60 (Shashtyamsha) deities for all planets.
     *
     * Each sign is divided into 60 parts of 0.5 degrees each.
     * For ODD signs: deities go 1-60 in order.
     * For EVEN signs: deities go 60-1 in reverse order.
     */
    private fun analyzeD60(d60Chart: DivisionalChartData, chart: VedicChart): D60ShashtyamshaAnalysis {
        val planetDeities = mutableMapOf<Planet, VargaDeityInfo>()

        for (position in chart.planetPositions) {
            if (position.planet !in Planet.MAIN_PLANETS) continue

            val degreeInSign = VedicAstrologyUtils.normalizeLongitude(position.longitude) % 30.0
            val d60Index = floor(degreeInSign / 0.5).toInt().coerceIn(0, 59)

            val isOddSign = position.sign.number % 2 == 1
            val deityIndex = if (isOddSign) d60Index else (59 - d60Index)

            val deityName = D60_DEITIES[deityIndex]
            val deityNature = D60_DEITY_NATURES[deityIndex]

            val d60Pos = d60Chart.planetPositions.find { it.planet == position.planet }

            val interpretation = generateD60Interpretation(
                position.planet, deityName, deityNature, position, chart
            )

            planetDeities[position.planet] = VargaDeityInfo(
                planet = position.planet,
                vargaType = DivisionalChartType.D60_SHASHTIAMSA,
                deityName = deityName,
                deityNature = deityNature,
                longitude = d60Pos?.longitude ?: position.longitude,
                sign = d60Pos?.sign ?: position.sign,
                interpretation = interpretation
            )
        }

        val beneficCount = planetDeities.values.count { it.deityNature == DeityNature.DEVA }
        val maleficCount = planetDeities.values.count { it.deityNature == DeityNature.ASURA }

        val summary = buildString {
            append("D60 Shashtyamsha Analysis: $beneficCount planets in Deva deities, ")
            append("$maleficCount in Asura deities. ")
            if (beneficCount > maleficCount) {
                append("Overall positive past-life karmic inheritance. The soul carries predominantly ")
                append("benefic impressions (samskaras) that support this life's endeavors.")
            } else if (maleficCount > beneficCount) {
                append("Karmic challenges from past lives indicated. The soul carries some heavy ")
                append("impressions requiring conscious effort to transmute through dharmic living.")
            } else {
                append("Balanced karmic inheritance. Past-life patterns are mixed, requiring ")
                append("discernment to strengthen positive tendencies and mitigate negative ones.")
            }
        }

        return D60ShashtyamshaAnalysis(
            planetDeities = planetDeities,
            beneficCount = beneficCount,
            maleficCount = maleficCount,
            summary = summary
        )
    }

    /**
     * Generates interpretation for a planet's D60 deity placement.
     */
    private fun generateD60Interpretation(
        planet: Planet,
        deityName: String,
        deityNature: DeityNature,
        position: PlanetPosition,
        chart: VedicChart
    ): String {
        val planetNature = if (VedicAstrologyUtils.isNaturalBenefic(planet)) "benefic" else "malefic"
        val dignity = VedicAstrologyUtils.getDignity(position)

        return buildString {
            append("${planet.displayName} in D60 deity '$deityName' (${deityNature.displayName}). ")

            when (deityNature) {
                DeityNature.DEVA -> {
                    append("This is a highly auspicious placement indicating positive past-life karma ")
                    append("supporting ${planet.displayName}'s significations. ")
                    if (dignity == VedicAstrologyUtils.PlanetaryDignity.EXALTED ||
                        dignity == VedicAstrologyUtils.PlanetaryDignity.OWN_SIGN) {
                        append("Combined with strong dignity, this creates exceptional results ")
                        append("in ${getHouseSignification(position.house)}.")
                    } else {
                        append("Even with moderate dignity, the Deva D60 deity protects and nourishes ")
                        append("this planet's outcomes.")
                    }
                }
                DeityNature.ASURA -> {
                    append("This placement indicates challenging past-life karma affecting ")
                    append("${planet.displayName}'s significations. ")
                    if (dignity == VedicAstrologyUtils.PlanetaryDignity.DEBILITATED) {
                        append("Combined with debilitation, this requires remedial measures for ")
                        append("${getHouseSignification(position.house)}. ")
                    }
                    append("Remedies: worship the deity of ${planet.displayName}, donate on ")
                    append("${planet.displayName}'s day, and practice related mantras.")
                }
                DeityNature.MANUSHYA -> {
                    append("This neutral placement indicates ordinary human karma. Results depend ")
                    append("heavily on ${planet.displayName}'s overall chart strength and dignity. ")
                    append("The planet's $planetNature nature will manifest through normal karmic channels.")
                }
                DeityNature.MIXED -> {
                    append("This mixed-nature placement can go either way depending on ")
                    append("${planet.displayName}'s overall chart strength. ")
                    append("When strong and well-placed, it aids transformation; when weak, it ")
                    append("can amplify instability in ${getHouseSignification(position.house)}.")
                }
            }
        }
    }

    // ============================================================================
    // D10 ANALYSIS
    // ============================================================================

    /**
     * Analyzes D10 (Dasamsa) Dikpala assignments for all planets.
     *
     * Each sign is divided into 10 parts of 3 degrees each.
     * For ODD signs: Indra, Agni, Yama, Nirriti, Varuna, Vayu, Kubera, Ishana, Brahma, Ananta
     * For EVEN signs: Ananta, Brahma, Ishana, Kubera, Vayu, Varuna, Nirriti, Yama, Agni, Indra
     */
    private fun analyzeD10(d10Chart: DivisionalChartData, chart: VedicChart): D10DikpalaAnalysis {
        val planetDikpalas = mutableMapOf<Planet, VargaDeityInfo>()
        val dikpalaCounts = mutableMapOf<String, Int>()

        for (position in chart.planetPositions) {
            if (position.planet !in Planet.MAIN_PLANETS) continue

            val degreeInSign = VedicAstrologyUtils.normalizeLongitude(position.longitude) % 30.0
            val d10Index = floor(degreeInSign / 3.0).toInt().coerceIn(0, 9)

            val isOddSign = position.sign.number % 2 == 1
            val dikpalaIndex = if (isOddSign) d10Index else (9 - d10Index)

            val dikpalaName = D10_DIKPALAS[dikpalaIndex]
            val dikpalaNature = D10_DIKPALA_NATURES[dikpalaIndex]

            dikpalaCounts[dikpalaName] = (dikpalaCounts[dikpalaName] ?: 0) + 1

            val d10Pos = d10Chart.planetPositions.find { it.planet == position.planet }
            val careerSig = D10_CAREER_SIGNIFICANCE[dikpalaName] ?: ""

            val interpretation = buildString {
                append("${position.planet.displayName} under Dikpala '$dikpalaName' ")
                append("(${dikpalaNature.displayName}) in D10. ")
                append(careerSig)
            }

            planetDikpalas[position.planet] = VargaDeityInfo(
                planet = position.planet,
                vargaType = DivisionalChartType.D10_DASAMSA,
                deityName = dikpalaName,
                deityNature = dikpalaNature,
                longitude = d10Pos?.longitude ?: position.longitude,
                sign = d10Pos?.sign ?: position.sign,
                interpretation = interpretation
            )
        }

        val dominantDikpala = dikpalaCounts.maxByOrNull { it.value }?.key ?: "None"
        val careerInfluence = buildString {
            append("The dominant Dikpala in D10 is $dominantDikpala, suggesting ")
            append("the overall career orientation is shaped by ")
            append(D10_CAREER_SIGNIFICANCE[dominantDikpala] ?: "mixed influences")
            append(". ")
            val tenthLord = VedicAstrologyUtils.getHouseLord(chart, 10)
            val tenthLordDikpala = planetDikpalas[tenthLord]
            if (tenthLordDikpala != null) {
                append("The 10th lord (${tenthLord.displayName}) falls under Dikpala '${tenthLordDikpala.deityName}', ")
                append("which strongly colors the professional direction.")
            }
        }

        return D10DikpalaAnalysis(
            planetDikpalas = planetDikpalas,
            dominantDikpala = dominantDikpala,
            careerDeityInfluence = careerInfluence
        )
    }

    // ============================================================================
    // D9 ANALYSIS
    // ============================================================================

    /**
     * Analyzes D9 (Navamsa) deities and identifies Pushkara Navamsas.
     */
    private fun analyzeD9(d9Chart: DivisionalChartData, chart: VedicChart): D9NavamsaDeityAnalysis {
        val planetDeities = mutableMapOf<Planet, VargaDeityInfo>()
        val pushkaraPlanets = mutableListOf<Planet>()
        val pushkaraDetails = mutableListOf<String>()

        for (position in chart.planetPositions) {
            if (position.planet !in Planet.MAIN_PLANETS) continue

            val d9Pos = d9Chart.planetPositions.find { it.planet == position.planet }
            val navamsaSign = d9Pos?.sign ?: position.sign
            val deityName = NAVAMSA_SIGN_DEITIES[navamsaSign] ?: "Unknown"
            val deityNature = getNavamsaDeityNature(navamsaSign)

            // Check for Pushkara Navamsa
            val degreeInSign = VedicAstrologyUtils.normalizeLongitude(position.longitude) % 30.0
            val isPushkara = isPushkaraNavamsa(position.sign, degreeInSign)

            if (isPushkara) {
                pushkaraPlanets.add(position.planet)
                pushkaraDetails.add(
                    "${position.planet.displayName} is in Pushkara Navamsa at ${position.sign.displayName} " +
                            "${String.format("%.2f", degreeInSign)} degrees. This is an extremely auspicious " +
                            "placement that protects and nourishes the planet's significations regardless " +
                            "of other chart afflictions. The dharmic and karmic merit associated with this " +
                            "planet is exceptional."
                )
            }

            val interpretation = buildString {
                append("${position.planet.displayName} in ${navamsaSign.displayName} Navamsa ")
                append("under $deityName (${deityNature.displayName}). ")
                if (isPushkara) {
                    append("PUSHKARA NAVAMSA: Supreme auspiciousness and divine protection. ")
                }
                append(getNavamsaInterpretation(position.planet, navamsaSign, d9Pos))
            }

            planetDeities[position.planet] = VargaDeityInfo(
                planet = position.planet,
                vargaType = DivisionalChartType.D9_NAVAMSA,
                deityName = deityName + if (isPushkara) " [PUSHKARA]" else "",
                deityNature = deityNature,
                longitude = d9Pos?.longitude ?: position.longitude,
                sign = navamsaSign,
                interpretation = interpretation
            )
        }

        return D9NavamsaDeityAnalysis(
            planetDeities = planetDeities,
            pushkaraPlanets = pushkaraPlanets,
            pushkaraNavamsaDetails = pushkaraDetails
        )
    }

    /**
     * Determines whether a planet at the given degree within a sign
     * falls in a Pushkara Navamsa.
     */
    private fun isPushkaraNavamsa(sign: ZodiacSign, degreeInSign: Double): Boolean {
        val ranges = PUSHKARA_NAVAMSA_RANGES[sign] ?: return false
        return ranges.any { (start, end) -> degreeInSign >= start && degreeInSign < end }
    }

    /**
     * Determines the deity nature for a Navamsa sign based on its ruler.
     */
    private fun getNavamsaDeityNature(sign: ZodiacSign): DeityNature {
        return when (sign.ruler) {
            Planet.JUPITER, Planet.VENUS -> DeityNature.DEVA
            Planet.SUN, Planet.MOON, Planet.MERCURY -> DeityNature.MANUSHYA
            Planet.MARS, Planet.SATURN -> DeityNature.MIXED
            else -> DeityNature.MANUSHYA
        }
    }

    /**
     * Generates Navamsa-specific interpretation for a planet.
     */
    private fun getNavamsaInterpretation(
        planet: Planet,
        navamsaSign: ZodiacSign,
        d9Pos: PlanetPosition?
    ): String {
        val isVargottama = d9Pos?.let {
            ZodiacSign.fromLongitude(it.longitude) == navamsaSign
        } ?: false

        return buildString {
            if (isVargottama) {
                append("Vargottama: Same sign in D1 and D9, conferring exceptional strength. ")
            }
            when (planet) {
                Planet.VENUS -> append("Venus in ${navamsaSign.displayName} Navamsa indicates the " +
                        "dharmic quality of marital life and the spouse's inner nature.")
                Planet.JUPITER -> append("Jupiter in ${navamsaSign.displayName} Navamsa reveals " +
                        "the depth of wisdom, dharmic orientation, and spiritual capacity.")
                Planet.MOON -> append("Moon in ${navamsaSign.displayName} Navamsa shows the " +
                        "emotional and psychological constitution at its deepest level.")
                Planet.SUN -> append("Sun in ${navamsaSign.displayName} Navamsa reveals the " +
                        "soul's true purpose and the quality of vitality at the dharmic level.")
                Planet.MARS -> append("Mars in ${navamsaSign.displayName} Navamsa shows the " +
                        "quality of courage, initiative, and passion in its dharmic expression.")
                Planet.MERCURY -> append("Mercury in ${navamsaSign.displayName} Navamsa indicates " +
                        "the dharmic quality of intellect, speech, and discrimination.")
                Planet.SATURN -> append("Saturn in ${navamsaSign.displayName} Navamsa reveals " +
                        "the deeper karmic lessons and the quality of perseverance and duty.")
                Planet.RAHU -> append("Rahu in ${navamsaSign.displayName} Navamsa shows the " +
                        "dharmic direction of material desires and worldly ambitions.")
                Planet.KETU -> append("Ketu in ${navamsaSign.displayName} Navamsa indicates " +
                        "the spiritual liberation pattern and past-life spiritual attainments.")
                else -> append("${planet.displayName} in ${navamsaSign.displayName} Navamsa.")
            }
        }
    }

    // ============================================================================
    // D7 ANALYSIS
    // ============================================================================

    /**
     * Analyzes D7 (Saptamsa) deities for all planets.
     *
     * Each sign is divided into 7 parts of approximately 4.2857 degrees.
     * Odd signs: Kshara, Ksheera, Dadhi, Aajya, Ikshu-rasa, Madya, Shuddha-jala
     * Even signs: Shuddha-jala, Madya, Ikshu-rasa, Aajya, Dadhi, Ksheera, Kshara
     */
    private fun analyzeD7(d7Chart: DivisionalChartData, chart: VedicChart): D7SaptamsaAnalysis {
        val planetDeities = mutableMapOf<Planet, VargaDeityInfo>()

        for (position in chart.planetPositions) {
            if (position.planet !in Planet.MAIN_PLANETS) continue

            val degreeInSign = VedicAstrologyUtils.normalizeLongitude(position.longitude) % 30.0
            val saptamsaPart = floor(degreeInSign / (30.0 / 7.0)).toInt().coerceIn(0, 6)

            val isOddSign = position.sign.number % 2 == 1
            val deityIndex = if (isOddSign) saptamsaPart else (6 - saptamsaPart)

            val deityName = D7_DEITIES_ODD[deityIndex]
            val deityNature = D7_DEITY_NATURES[deityIndex]

            val d7Pos = d7Chart.planetPositions.find { it.planet == position.planet }

            val interpretation = buildString {
                append("${position.planet.displayName} in D7 deity '$deityName' ")
                append("(${deityNature.displayName}). ")
                when (deityName) {
                    "Kshara" -> append("Kshara (alkaline/caustic) indicates transformative but potentially ")
                        .append("harsh conditions for progeny matters. May indicate difficult pregnancies ")
                        .append("or challenging early childhood requiring special care.")
                    "Ksheera" -> append("Ksheera (milk) indicates abundant nourishment for children. ")
                        .append("Highly auspicious for fertility, healthy progeny, and nurturing parenthood.")
                    "Dadhi" -> append("Dadhi (curd) indicates cultured, well-formed progeny. ")
                        .append("Children will be intelligent, refined, and well-mannered.")
                    "Aajya" -> append("Aajya (ghee) is the most auspicious D7 deity, indicating ")
                        .append("sacred, blessed progeny. Children will bring honor and spiritual merit.")
                    "Ikshu-rasa" -> append("Ikshu-rasa (sugarcane juice) indicates sweet, pleasant ")
                        .append("progeny matters. Children will bring joy, pleasure, and happiness.")
                    "Madya" -> append("Madya (wine/intoxicant) indicates some complications or ")
                        .append("intoxicating attachment in progeny matters. May suggest addictive ")
                        .append("or obsessive parental patterns requiring awareness.")
                    "Shuddha-jala" -> append("Shuddha-jala (pure water) indicates purifying influence. ")
                        .append("Children will have cleansing, healing qualities and bring spiritual growth.")
                }
            }

            planetDeities[position.planet] = VargaDeityInfo(
                planet = position.planet,
                vargaType = DivisionalChartType.D7_SAPTAMSA,
                deityName = deityName,
                deityNature = deityNature,
                longitude = d7Pos?.longitude ?: position.longitude,
                sign = d7Pos?.sign ?: position.sign,
                interpretation = interpretation
            )
        }

        val nourishingCount = planetDeities.values.count {
            it.deityNature == DeityNature.DEVA
        }
        val challengingCount = planetDeities.values.count {
            it.deityNature == DeityNature.ASURA || it.deityNature == DeityNature.MIXED
        }

        val balance = buildString {
            append("Fertility deity balance: $nourishingCount nourishing vs $challengingCount challenging placements. ")
            when {
                nourishingCount > challengingCount + 2 -> append("Exceptionally favorable for progeny.")
                nourishingCount > challengingCount -> append("Generally favorable for progeny matters.")
                nourishingCount == challengingCount -> append("Mixed indications for progeny, requiring careful timing.")
                else -> append("Some challenges in progeny matters; targeted remedies recommended.")
            }
        }

        return D7SaptamsaAnalysis(
            planetDeities = planetDeities,
            fertilityDeityBalance = balance
        )
    }

    // ============================================================================
    // D3 ANALYSIS
    // ============================================================================

    /**
     * Analyzes D3 (Drekkana) forms/deities from Varahamihira's Brihat Jataka.
     *
     * Each sign has 3 Drekkanas of 10 degrees each, yielding 36 unique forms
     * across the zodiac.
     */
    private fun analyzeD3(d3Chart: DivisionalChartData, chart: VedicChart): D3DrekkanaAnalysis {
        val planetDeities = mutableMapOf<Planet, VargaDeityInfo>()

        for (position in chart.planetPositions) {
            if (position.planet !in Planet.MAIN_PLANETS) continue

            val degreeInSign = VedicAstrologyUtils.normalizeLongitude(position.longitude) % 30.0
            val drekkanaNumber = when {
                degreeInSign < 10.0 -> 1
                degreeInSign < 20.0 -> 2
                else -> 3
            }

            val signNumber = position.sign.number
            val formData = D3_DREKKANA_FORMS[Pair(signNumber, drekkanaNumber)]

            val formDescription = formData?.first ?: "Classical Drekkana form"
            val formNature = formData?.second ?: DeityNature.MANUSHYA
            val formEnergy = formData?.third ?: "Standard planetary energy"

            val d3Pos = d3Chart.planetPositions.find { it.planet == position.planet }

            val interpretation = buildString {
                append("${position.planet.displayName} in ${position.sign.displayName} ")
                append("Drekkana $drekkanaNumber: \"$formDescription\" ")
                append("(${formNature.displayName}). $formEnergy. ")
                append("This form influences the expression of courage, siblings, ")
                append("and short journeys through ${position.planet.displayName}'s lens.")
            }

            planetDeities[position.planet] = VargaDeityInfo(
                planet = position.planet,
                vargaType = DivisionalChartType.D3_DREKKANA,
                deityName = "Drekkana $drekkanaNumber of ${position.sign.displayName}",
                deityNature = formNature,
                longitude = d3Pos?.longitude ?: position.longitude,
                sign = d3Pos?.sign ?: position.sign,
                interpretation = interpretation
            )
        }

        val formDescription = buildString {
            append("The 36 Drekkana forms from Brihat Jataka reveal the subtle body of ")
            append("courage, initiative, and sibling karma. ")
            val devaCount = planetDeities.values.count { it.deityNature == DeityNature.DEVA }
            val asuraCount = planetDeities.values.count { it.deityNature == DeityNature.ASURA }
            when {
                devaCount > asuraCount -> append("Predominantly divine Drekkana forms indicate ")
                    .append("strong courage, supportive siblings, and favorable short journeys.")
                asuraCount > devaCount -> append("Several challenging Drekkana forms suggest ")
                    .append("tests of courage and complex sibling dynamics requiring patience.")
                else -> append("A balanced mix of Drekkana forms indicates varied experiences ")
                    .append("in matters of courage, siblings, and travel.")
            }
        }

        return D3DrekkanaAnalysis(
            planetDeities = planetDeities,
            drekkanaFormDescription = formDescription
        )
    }

    // ============================================================================
    // UTILITY METHODS
    // ============================================================================

    /**
     * Builds a combined map of all Varga deity information per planet.
     */
    private fun buildPlanetDeityMap(
        d60: D60ShashtyamshaAnalysis,
        d10: D10DikpalaAnalysis,
        d9: D9NavamsaDeityAnalysis,
        d7: D7SaptamsaAnalysis,
        d3: D3DrekkanaAnalysis
    ): Map<Planet, List<VargaDeityInfo>> {
        val result = mutableMapOf<Planet, MutableList<VargaDeityInfo>>()

        for (planet in Planet.MAIN_PLANETS) {
            val deities = mutableListOf<VargaDeityInfo>()
            d3.planetDeities[planet]?.let { deities.add(it) }
            d7.planetDeities[planet]?.let { deities.add(it) }
            d9.planetDeities[planet]?.let { deities.add(it) }
            d10.planetDeities[planet]?.let { deities.add(it) }
            d60.planetDeities[planet]?.let { deities.add(it) }
            if (deities.isNotEmpty()) {
                result[planet] = deities
            }
        }

        return result
    }

    /**
     * Calculates the overall balance of deity natures across all Varga charts.
     */
    private fun calculateDeityBalance(allDeities: List<VargaDeityInfo>): DeityBalance {
        val devaCount = allDeities.count { it.deityNature == DeityNature.DEVA }
        val asuraCount = allDeities.count { it.deityNature == DeityNature.ASURA }
        val manushyaCount = allDeities.count { it.deityNature == DeityNature.MANUSHYA }
        val mixedCount = allDeities.count { it.deityNature == DeityNature.MIXED }

        val dominantNature = when {
            devaCount >= asuraCount && devaCount >= manushyaCount && devaCount >= mixedCount -> DeityNature.DEVA
            asuraCount >= devaCount && asuraCount >= manushyaCount && asuraCount >= mixedCount -> DeityNature.ASURA
            manushyaCount >= devaCount && manushyaCount >= asuraCount && manushyaCount >= mixedCount -> DeityNature.MANUSHYA
            else -> DeityNature.MIXED
        }

        val balance = buildString {
            append("Across all analyzed Varga charts: ")
            append("$devaCount Deva, $asuraCount Asura, $manushyaCount Manushya, $mixedCount Mixed placements. ")
            when (dominantNature) {
                DeityNature.DEVA -> append("The chart is predominantly under divine influence, indicating ")
                    .append("strong past-life merit and natural protection from negative outcomes. ")
                    .append("The native benefits from invisible support in their life endeavors.")
                DeityNature.ASURA -> append("The chart shows significant demonic/challenging influences, ")
                    .append("indicating past-life karmic debts requiring conscious remediation. ")
                    .append("Regular spiritual practice and targeted remedies are strongly recommended.")
                DeityNature.MANUSHYA -> append("The chart is predominantly under human-level influences, ")
                    .append("indicating that results will largely depend on personal effort and choices. ")
                    .append("Neither strongly protected nor strongly challenged by invisible forces.")
                DeityNature.MIXED -> append("The chart shows a balanced mix of influences, indicating ")
                    .append("that the native has both opportunities and challenges from past-life karma. ")
                    .append("Wisdom in choosing actions is particularly important.")
            }
        }

        return DeityBalance(
            devaCount = devaCount,
            asuraCount = asuraCount,
            manushyaCount = manushyaCount,
            mixedCount = mixedCount,
            overallBalance = balance,
            dominantNature = dominantNature
        )
    }

    /**
     * Generates a list of significant findings from the complete Varga deity analysis.
     */
    private fun generateSignificantFindings(
        d60: D60ShashtyamshaAnalysis,
        d10: D10DikpalaAnalysis,
        d9: D9NavamsaDeityAnalysis,
        d7: D7SaptamsaAnalysis,
        d3: D3DrekkanaAnalysis,
        chart: VedicChart
    ): List<String> {
        val findings = mutableListOf<String>()

        // D60 significant findings
        val lagnaLord = VedicAstrologyUtils.getHouseLord(chart, 1)
        d60.planetDeities[lagnaLord]?.let { info ->
            findings.add(
                "Lagna lord (${lagnaLord.displayName}) is in D60 deity '${info.deityName}' " +
                        "(${info.deityNature.displayName}). This critically shapes the overall " +
                        "karmic trajectory of the native's life."
            )
        }

        // Moon's D60 placement
        d60.planetDeities[Planet.MOON]?.let { info ->
            if (info.deityNature == DeityNature.DEVA) {
                findings.add(
                    "Moon in Deva D60 deity '${info.deityName}': The mind and emotions are " +
                            "karmically supported, indicating emotional resilience and a naturally " +
                            "positive psychological constitution."
                )
            } else if (info.deityNature == DeityNature.ASURA) {
                findings.add(
                    "Moon in Asura D60 deity '${info.deityName}': The mind may face karmic " +
                            "emotional challenges. Moon-related remedies (pearl, chanting Chandra " +
                            "mantras, Monday fasting) are recommended."
                )
            }
        }

        // Pushkara Navamsa findings
        if (d9.pushkaraPlanets.isNotEmpty()) {
            findings.add(
                "Pushkara Navamsa planets: ${d9.pushkaraPlanets.joinToString(", ") { it.displayName }}. " +
                        "These planets are in the most auspicious Navamsa positions, receiving " +
                        "special divine nourishment and protection."
            )
        }

        // D10 Kubera placement
        val kuberaPlanets = d10.planetDikpalas.filter { it.value.deityName == "Kubera" }
        if (kuberaPlanets.isNotEmpty()) {
            findings.add(
                "Planet(s) under Kubera in D10: ${kuberaPlanets.keys.joinToString(", ") { it.displayName }}. " +
                        "This indicates significant wealth potential through career and professional endeavors."
            )
        }

        // D7 Aajya (ghee) placement - most auspicious for progeny
        val aajyaPlanets = d7.planetDeities.filter { it.value.deityName == "Aajya" }
        if (aajyaPlanets.isNotEmpty()) {
            findings.add(
                "Planet(s) in Aajya (Ghee) D7: ${aajyaPlanets.keys.joinToString(", ") { it.displayName }}. " +
                        "Most auspicious D7 deity for progeny - indicates blessed, sacred children."
            )
        }

        // Jupiter's overall deity pattern
        val jupiterDeities = listOfNotNull(
            d60.planetDeities[Planet.JUPITER],
            d10.planetDikpalas[Planet.JUPITER],
            d9.planetDeities[Planet.JUPITER],
            d7.planetDeities[Planet.JUPITER],
            d3.planetDeities[Planet.JUPITER]
        )
        val jupiterDevaCount = jupiterDeities.count { it.deityNature == DeityNature.DEVA }
        if (jupiterDevaCount >= 4) {
            findings.add(
                "Jupiter is in Deva deities across $jupiterDevaCount Varga charts. This is a " +
                        "powerful indicator of divine grace (Guru Kripa) operating throughout the " +
                        "native's life, protecting dharma, progeny, wealth, and wisdom."
            )
        }

        // Saturn's deity pattern
        val saturnAsuraCount = listOfNotNull(
            d60.planetDeities[Planet.SATURN],
            d10.planetDikpalas[Planet.SATURN],
            d9.planetDeities[Planet.SATURN],
            d7.planetDeities[Planet.SATURN],
            d3.planetDeities[Planet.SATURN]
        ).count { it.deityNature == DeityNature.ASURA }
        if (saturnAsuraCount >= 3) {
            findings.add(
                "Saturn is in Asura deities across $saturnAsuraCount Varga charts. This " +
                        "intensifies Saturn's karmic challenges and delays. Strong Saturn " +
                        "remedies (Blue Sapphire evaluation, Shani mantras, Saturday charity) " +
                        "are particularly important for this chart."
            )
        }

        return findings
    }

    /**
     * Returns a brief signification description for a house number.
     */
    private fun getHouseSignification(house: Int): String = when (house) {
        1 -> "self, body, personality, and overall life direction"
        2 -> "wealth, family, speech, and food"
        3 -> "courage, siblings, communication, and short journeys"
        4 -> "home, mother, emotions, vehicles, and property"
        5 -> "children, creativity, intelligence, and romance"
        6 -> "health, enemies, debts, and service"
        7 -> "marriage, partnerships, and public dealings"
        8 -> "longevity, transformation, inheritance, and occult"
        9 -> "fortune, father, dharma, and higher learning"
        10 -> "career, reputation, public standing, and authority"
        11 -> "gains, income, social circles, and fulfillment of desires"
        12 -> "losses, expenses, foreign lands, liberation, and spiritual retreats"
        else -> "general life matters"
    }
}
