package com.astro.storm.ephemeris

import com.astro.storm.data.model.Planet
import com.astro.storm.data.model.VedicChart
import com.astro.storm.data.model.ZodiacSign
import com.astro.storm.data.model.Nakshatra
import com.astro.storm.data.model.PlanetPosition

/**
 * Production-Grade Remedies Calculator for Vedic Astrology
 *
 * Provides comprehensive remedy recommendations based on:
 * 1. Planetary strength analysis (Shadbala concepts)
 * 2. House placements and afflictions
 * 3. Nakshatra-based remedies
 * 4. Dasha period requirements
 * 5. Yoga formations (both positive and negative)
 *
 * Remedy types include:
 * - Gemstones (Ratna)
 * - Mantras (Vedic chants)
 * - Yantras (Sacred geometric diagrams)
 * - Charitable acts (Daan)
 * - Fasting (Vrat)
 * - Color therapy
 * - Metal recommendations
 * - Rudraksha beads
 * - Deity worship
 *
 * Based on:
 * - Brihat Parashara Hora Shastra
 * - Phaladeepika
 * - Jataka Parijata
 * - Hora Ratnam
 *
 * @author AstroStorm - Ultra-Precision Vedic Astrology
 */
object RemediesCalculator {

    /**
     * Planetary strength categories
     */
    enum class PlanetaryStrength(val displayName: String, val severity: Int) {
        VERY_STRONG("Very Strong", 0),
        STRONG("Strong", 1),
        MODERATE("Moderate", 2),
        WEAK("Weak", 3),
        VERY_WEAK("Very Weak", 4),
        AFFLICTED("Afflicted", 5)
    }

    /**
     * Remedy category
     */
    enum class RemedyCategory(val displayName: String, val icon: String) {
        GEMSTONE("Gemstone", "gem"),
        MANTRA("Mantra", "om"),
        YANTRA("Yantra", "geometry"),
        CHARITY("Charity", "donate"),
        FASTING("Fasting", "restaurant"),
        COLOR("Color Therapy", "palette"),
        METAL("Metal", "ring"),
        RUDRAKSHA("Rudraksha", "spa"),
        DEITY("Deity Worship", "temple"),
        LIFESTYLE("Lifestyle", "heart")
    }

    /**
     * Priority level for remedies
     */
    enum class RemedyPriority(val displayName: String, val level: Int) {
        ESSENTIAL("Essential", 1),
        HIGHLY_RECOMMENDED("Highly Recommended", 2),
        RECOMMENDED("Recommended", 3),
        OPTIONAL("Optional", 4)
    }

    /**
     * Individual remedy item
     */
    data class Remedy(
        val category: RemedyCategory,
        val title: String,
        val description: String,
        val method: String,
        val timing: String,
        val duration: String,
        val planet: Planet?,
        val priority: RemedyPriority,
        val benefits: List<String>,
        val cautions: List<String>
    )

    /**
     * Planetary analysis for remedies
     */
    data class PlanetaryAnalysis(
        val planet: Planet,
        val strength: PlanetaryStrength,
        val strengthScore: Int,
        val issues: List<String>,
        val positives: List<String>,
        val needsRemedy: Boolean,
        val housePosition: Int,
        val sign: ZodiacSign,
        val nakshatra: Nakshatra,
        val isRetrograde: Boolean,
        val isCombust: Boolean,
        val isDebilitated: Boolean,
        val isExalted: Boolean
    )

    /**
     * Complete remedies result
     */
    data class RemediesResult(
        val chart: VedicChart,
        val planetaryAnalyses: List<PlanetaryAnalysis>,
        val weakestPlanets: List<Planet>,
        val remedies: List<Remedy>,
        val generalRecommendations: List<String>,
        val dashaRemedies: List<Remedy>,
        val lifeAreaFocus: Map<String, List<Remedy>>,
        val prioritizedRemedies: List<Remedy>,
        val summary: String,
        val timestamp: Long = System.currentTimeMillis()
    ) {
        val totalRemediesCount: Int get() = remedies.size
        val essentialRemediesCount: Int get() = remedies.count { it.priority == RemedyPriority.ESSENTIAL }

        fun toPlainText(): String = buildString {
            appendLine("═══════════════════════════════════════════════════════════")
            appendLine("              VEDIC ASTROLOGY REMEDIES REPORT")
            appendLine("═══════════════════════════════════════════════════════════")
            appendLine()
            appendLine("Name: ${chart.birthData.name}")
            appendLine()
            appendLine("─────────────────────────────────────────────────────────")
            appendLine("                PLANETARY STRENGTH ANALYSIS")
            appendLine("─────────────────────────────────────────────────────────")
            appendLine()
            planetaryAnalyses.forEach { analysis ->
                appendLine("${analysis.planet.displayName}: ${analysis.strength.displayName}")
                if (analysis.issues.isNotEmpty()) {
                    analysis.issues.forEach { appendLine("  ⚠ $it") }
                }
            }
            appendLine()
            if (weakestPlanets.isNotEmpty()) {
                appendLine("PLANETS REQUIRING ATTENTION:")
                weakestPlanets.forEach { appendLine("  • ${it.displayName}") }
            }
            appendLine()
            appendLine("─────────────────────────────────────────────────────────")
            appendLine("                    RECOMMENDED REMEDIES")
            appendLine("─────────────────────────────────────────────────────────")
            prioritizedRemedies.take(10).forEachIndexed { index, remedy ->
                appendLine()
                appendLine("${index + 1}. ${remedy.title} [${remedy.priority.displayName}]")
                appendLine("   Category: ${remedy.category.displayName}")
                appendLine("   ${remedy.description}")
                appendLine("   Method: ${remedy.method}")
                appendLine("   Timing: ${remedy.timing}")
            }
            appendLine()
            appendLine("─────────────────────────────────────────────────────────")
            appendLine("                        SUMMARY")
            appendLine("─────────────────────────────────────────────────────────")
            appendLine(summary)
            appendLine()
            appendLine("═══════════════════════════════════════════════════════════")
            appendLine("Generated by AstroStorm - Ultra-Precision Vedic Astrology")
            appendLine("═══════════════════════════════════════════════════════════")
        }
    }

    /**
     * Gemstone information for each planet
     */
    private val planetaryGemstones = mapOf(
        Planet.SUN to GemstoneInfo("Ruby", "Manikya", "Pingala, Lohit", "Gold", 3.0, 5.0),
        Planet.MOON to GemstoneInfo("Pearl", "Moti", "White, Cream", "Silver", 4.0, 6.0),
        Planet.MARS to GemstoneInfo("Red Coral", "Moonga", "Red, Orange-Red", "Gold/Copper", 5.0, 9.0),
        Planet.MERCURY to GemstoneInfo("Emerald", "Panna", "Green", "Gold", 3.0, 6.0),
        Planet.JUPITER to GemstoneInfo("Yellow Sapphire", "Pukhraj", "Yellow", "Gold", 3.0, 5.0),
        Planet.VENUS to GemstoneInfo("Diamond", "Heera", "White, Colorless", "Platinum/Silver", 0.5, 1.0),
        Planet.SATURN to GemstoneInfo("Blue Sapphire", "Neelam", "Blue", "Gold/Iron", 3.0, 5.0),
        Planet.RAHU to GemstoneInfo("Hessonite", "Gomed", "Honey-colored", "Silver", 4.0, 8.0),
        Planet.KETU to GemstoneInfo("Cat's Eye", "Lahsuniya", "Greenish, Honey", "Silver", 3.0, 5.0)
    )

    private data class GemstoneInfo(
        val name: String,
        val hindiName: String,
        val colors: String,
        val metal: String,
        val minCarat: Double,
        val maxCarat: Double
    )

    /**
     * Mantras for each planet
     */
    private val planetaryMantras = mapOf(
        Planet.SUN to MantraInfo(
            "Om Hraam Hreem Hraum Sah Suryaya Namaha",
            "ॐ ह्रां ह्रीं ह्रौं सः सूर्याय नमः",
            7000,
            "Sunday morning at sunrise"
        ),
        Planet.MOON to MantraInfo(
            "Om Shraam Shreem Shraum Sah Chandraya Namaha",
            "ॐ श्रां श्रीं श्रौं सः चन्द्राय नमः",
            11000,
            "Monday evening"
        ),
        Planet.MARS to MantraInfo(
            "Om Kraam Kreem Kraum Sah Bhaumaya Namaha",
            "ॐ क्रां क्रीं क्रौं सः भौमाय नमः",
            10000,
            "Tuesday morning"
        ),
        Planet.MERCURY to MantraInfo(
            "Om Braam Breem Braum Sah Budhaya Namaha",
            "ॐ ब्रां ब्रीं ब्रौं सः बुधाय नमः",
            9000,
            "Wednesday morning"
        ),
        Planet.JUPITER to MantraInfo(
            "Om Graam Greem Graum Sah Gurave Namaha",
            "ॐ ग्रां ग्रीं ग्रौं सः गुरवे नमः",
            19000,
            "Thursday morning"
        ),
        Planet.VENUS to MantraInfo(
            "Om Draam Dreem Draum Sah Shukraya Namaha",
            "ॐ द्रां द्रीं द्रौं सः शुक्राय नमः",
            16000,
            "Friday morning"
        ),
        Planet.SATURN to MantraInfo(
            "Om Praam Preem Praum Sah Shanaischaraya Namaha",
            "ॐ प्रां प्रीं प्रौं सः शनैश्चराय नमः",
            23000,
            "Saturday evening"
        ),
        Planet.RAHU to MantraInfo(
            "Om Bhraam Bhreem Bhraum Sah Rahave Namaha",
            "ॐ भ्रां भ्रीं भ्रौं सः राहवे नमः",
            18000,
            "Wednesday or Saturday night"
        ),
        Planet.KETU to MantraInfo(
            "Om Sraam Sreem Sraum Sah Ketave Namaha",
            "ॐ स्रां स्रीं स्रौं सः केतवे नमः",
            17000,
            "Tuesday or Saturday"
        )
    )

    private data class MantraInfo(
        val mantra: String,
        val sanskrit: String,
        val minimumCount: Int,
        val timing: String
    )

    /**
     * Charity recommendations for each planet
     */
    private val planetaryCharity = mapOf(
        Planet.SUN to CharityInfo("Wheat, jaggery, copper, gold, red cloth", "Sunday", "Temple, father figures"),
        Planet.MOON to CharityInfo("Rice, white cloth, silver, milk, pearl", "Monday", "Mothers, elderly women"),
        Planet.MARS to CharityInfo("Red lentils, red cloth, coral, copper", "Tuesday", "Soldiers, brothers"),
        Planet.MERCURY to CharityInfo("Green gram, green cloth, emerald", "Wednesday", "Students, scholars"),
        Planet.JUPITER to CharityInfo("Chana dal, yellow cloth, gold, turmeric", "Thursday", "Teachers, priests"),
        Planet.VENUS to CharityInfo("Rice, white items, diamond, silk", "Friday", "Women, artists"),
        Planet.SATURN to CharityInfo("Black gram, iron, blue/black cloth, sesame", "Saturday", "Poor, servants, elderly"),
        Planet.RAHU to CharityInfo("Coconut, blue cloth, hessonite", "Saturday", "Outcasts, lepers"),
        Planet.KETU to CharityInfo("Mixed grains, blanket, cat's eye", "Tuesday/Saturday", "Spiritual seekers, dogs")
    )

    private data class CharityInfo(
        val items: String,
        val day: String,
        val recipients: String
    )

    /**
     * Calculate complete remedies for a chart
     */
    fun calculateRemedies(chart: VedicChart): RemediesResult {
        // Analyze each planet
        val planetaryAnalyses = Planet.MAIN_PLANETS.map { planet ->
            analyzePlanet(planet, chart)
        }

        // Identify weak planets
        val weakestPlanets = planetaryAnalyses
            .filter { it.needsRemedy }
            .sortedByDescending { it.strength.severity }
            .map { it.planet }

        // Generate remedies
        val allRemedies = mutableListOf<Remedy>()

        // Gemstone remedies
        weakestPlanets.forEach { planet ->
            getGemstoneRemedy(planet)?.let { allRemedies.add(it) }
        }

        // Mantra remedies
        weakestPlanets.forEach { planet ->
            getMantraRemedy(planet)?.let { allRemedies.add(it) }
        }

        // Charity remedies
        weakestPlanets.forEach { planet ->
            getCharityRemedy(planet)?.let { allRemedies.add(it) }
        }

        // Fasting remedies
        weakestPlanets.take(3).forEach { planet ->
            getFastingRemedy(planet)?.let { allRemedies.add(it) }
        }

        // Color and lifestyle remedies
        weakestPlanets.take(2).forEach { planet ->
            getColorRemedy(planet)?.let { allRemedies.add(it) }
            getLifestyleRemedy(planet)?.let { allRemedies.add(it) }
        }

        // Rudraksha remedies
        weakestPlanets.take(2).forEach { planet ->
            getRudrakshaRemedy(planet)?.let { allRemedies.add(it) }
        }

        // Yantra remedies
        weakestPlanets.take(2).forEach { planet ->
            getYantraRemedy(planet)?.let { allRemedies.add(it) }
        }

        // Deity worship
        weakestPlanets.forEach { planet ->
            getDeityRemedy(planet)?.let { allRemedies.add(it) }
        }

        // General recommendations based on chart
        val generalRecommendations = generateGeneralRecommendations(chart, planetaryAnalyses)

        // Dasha-specific remedies
        val dashaRemedies = generateDashaRemedies(chart)

        // Life area focus
        val lifeAreaFocus = categorizeByLifeArea(allRemedies, planetaryAnalyses)

        // Prioritize remedies
        val prioritizedRemedies = allRemedies.sortedBy { it.priority.level }

        // Generate summary
        val summary = generateSummary(planetaryAnalyses, weakestPlanets)

        return RemediesResult(
            chart = chart,
            planetaryAnalyses = planetaryAnalyses,
            weakestPlanets = weakestPlanets,
            remedies = allRemedies,
            generalRecommendations = generalRecommendations,
            dashaRemedies = dashaRemedies,
            lifeAreaFocus = lifeAreaFocus,
            prioritizedRemedies = prioritizedRemedies,
            summary = summary
        )
    }

    /**
     * Analyze individual planet strength and afflictions
     */
    private fun analyzePlanet(planet: Planet, chart: VedicChart): PlanetaryAnalysis {
        val position = chart.planetPositions.find { it.planet == planet }
            ?: return createDefaultAnalysis(planet)

        val issues = mutableListOf<String>()
        val positives = mutableListOf<String>()
        var strengthScore = 50 // Base score

        // Check debilitation
        val isDebilitated = isDebilitated(planet, position.sign)
        if (isDebilitated) {
            issues.add("Debilitated in ${position.sign.displayName}")
            strengthScore -= 25
        }

        // Check exaltation
        val isExalted = isExalted(planet, position.sign)
        if (isExalted) {
            positives.add("Exalted in ${position.sign.displayName}")
            strengthScore += 25
        }

        // Check own sign
        if (position.sign.ruler == planet) {
            positives.add("In own sign ${position.sign.displayName}")
            strengthScore += 15
        }

        // Check enemy sign
        if (isInEnemySign(planet, position.sign)) {
            issues.add("In enemy sign ${position.sign.displayName}")
            strengthScore -= 10
        }

        // Check house placement
        val house = position.house
        if (house in listOf(6, 8, 12)) {
            issues.add("Placed in dusthana house $house")
            strengthScore -= 15
        } else if (house in listOf(1, 4, 7, 10)) {
            positives.add("Placed in kendra house $house")
            strengthScore += 10
        } else if (house in listOf(5, 9)) {
            positives.add("Placed in trikona house $house")
            strengthScore += 10
        }

        // Check retrograde
        val isRetrograde = position.isRetrograde
        if (isRetrograde) {
            issues.add("Retrograde")
            // Retrograde can be both good and bad depending on planet
            if (planet in listOf(Planet.JUPITER, Planet.SATURN, Planet.MERCURY)) {
                strengthScore += 5 // Can strengthen these planets
            } else {
                strengthScore -= 10
            }
        }

        // Check combustion
        val isCombust = checkCombustion(planet, chart)
        if (isCombust) {
            issues.add("Combust by Sun")
            strengthScore -= 20
        }

        // Check conjunction with malefics
        val conjunctMalefics = checkMaleficConjunction(planet, chart)
        if (conjunctMalefics.isNotEmpty()) {
            issues.add("Conjunct malefics: ${conjunctMalefics.joinToString { it.displayName }}")
            strengthScore -= conjunctMalefics.size * 5
        }

        // Check conjunction with benefics
        val conjunctBenefics = checkBeneficConjunction(planet, chart)
        if (conjunctBenefics.isNotEmpty()) {
            positives.add("Conjunct benefics: ${conjunctBenefics.joinToString { it.displayName }}")
            strengthScore += conjunctBenefics.size * 5
        }

        // Determine strength category
        val strength = when {
            strengthScore >= 80 -> PlanetaryStrength.VERY_STRONG
            strengthScore >= 60 -> PlanetaryStrength.STRONG
            strengthScore >= 40 -> PlanetaryStrength.MODERATE
            strengthScore >= 20 -> PlanetaryStrength.WEAK
            strengthScore >= 0 -> PlanetaryStrength.VERY_WEAK
            else -> PlanetaryStrength.AFFLICTED
        }

        val needsRemedy = strength.severity >= 3 || issues.size >= 2

        val (nakshatra, pada) = Nakshatra.fromLongitude(position.longitude)

        return PlanetaryAnalysis(
            planet = planet,
            strength = strength,
            strengthScore = strengthScore.coerceIn(0, 100),
            issues = issues,
            positives = positives,
            needsRemedy = needsRemedy,
            housePosition = house,
            sign = position.sign,
            nakshatra = nakshatra,
            isRetrograde = isRetrograde,
            isCombust = isCombust,
            isDebilitated = isDebilitated,
            isExalted = isExalted
        )
    }

    private fun createDefaultAnalysis(planet: Planet): PlanetaryAnalysis {
        return PlanetaryAnalysis(
            planet = planet,
            strength = PlanetaryStrength.MODERATE,
            strengthScore = 50,
            issues = emptyList(),
            positives = emptyList(),
            needsRemedy = false,
            housePosition = 1,
            sign = ZodiacSign.ARIES,
            nakshatra = Nakshatra.ASHWINI,
            isRetrograde = false,
            isCombust = false,
            isDebilitated = false,
            isExalted = false
        )
    }

    private fun isDebilitated(planet: Planet, sign: ZodiacSign): Boolean {
        return when (planet) {
            Planet.SUN -> sign == ZodiacSign.LIBRA
            Planet.MOON -> sign == ZodiacSign.SCORPIO
            Planet.MARS -> sign == ZodiacSign.CANCER
            Planet.MERCURY -> sign == ZodiacSign.PISCES
            Planet.JUPITER -> sign == ZodiacSign.CAPRICORN
            Planet.VENUS -> sign == ZodiacSign.VIRGO
            Planet.SATURN -> sign == ZodiacSign.ARIES
            else -> false
        }
    }

    private fun isExalted(planet: Planet, sign: ZodiacSign): Boolean {
        return when (planet) {
            Planet.SUN -> sign == ZodiacSign.ARIES
            Planet.MOON -> sign == ZodiacSign.TAURUS
            Planet.MARS -> sign == ZodiacSign.CAPRICORN
            Planet.MERCURY -> sign == ZodiacSign.VIRGO
            Planet.JUPITER -> sign == ZodiacSign.CANCER
            Planet.VENUS -> sign == ZodiacSign.PISCES
            Planet.SATURN -> sign == ZodiacSign.LIBRA
            Planet.RAHU -> sign == ZodiacSign.TAURUS
            Planet.KETU -> sign == ZodiacSign.SCORPIO
            else -> false
        }
    }

    private fun isInEnemySign(planet: Planet, sign: ZodiacSign): Boolean {
        val signLord = sign.ruler
        val enemies = when (planet) {
            Planet.SUN -> listOf(Planet.VENUS, Planet.SATURN)
            Planet.MOON -> emptyList()
            Planet.MARS -> listOf(Planet.MERCURY)
            Planet.MERCURY -> listOf(Planet.MOON)
            Planet.JUPITER -> listOf(Planet.MERCURY, Planet.VENUS)
            Planet.VENUS -> listOf(Planet.SUN, Planet.MOON)
            Planet.SATURN -> listOf(Planet.SUN, Planet.MOON, Planet.MARS)
            else -> emptyList()
        }
        return signLord in enemies
    }

    private fun checkCombustion(planet: Planet, chart: VedicChart): Boolean {
        if (planet == Planet.SUN) return false

        val sunPos = chart.planetPositions.find { it.planet == Planet.SUN } ?: return false
        val planetPos = chart.planetPositions.find { it.planet == planet } ?: return false

        val combustionDegrees = when (planet) {
            Planet.MOON -> 12.0
            Planet.MARS -> 17.0
            Planet.MERCURY -> 14.0 // 12 if retrograde
            Planet.JUPITER -> 11.0
            Planet.VENUS -> 10.0 // 8 if retrograde
            Planet.SATURN -> 15.0
            else -> 0.0
        }

        val diff = kotlin.math.abs(sunPos.longitude - planetPos.longitude)
        val normalizedDiff = if (diff > 180) 360 - diff else diff

        return normalizedDiff <= combustionDegrees
    }

    private fun checkMaleficConjunction(planet: Planet, chart: VedicChart): List<Planet> {
        val malefics = listOf(Planet.SATURN, Planet.MARS, Planet.RAHU, Planet.KETU)
        val planetPos = chart.planetPositions.find { it.planet == planet } ?: return emptyList()

        return chart.planetPositions
            .filter { it.planet in malefics && it.planet != planet }
            .filter { it.house == planetPos.house }
            .map { it.planet }
    }

    private fun checkBeneficConjunction(planet: Planet, chart: VedicChart): List<Planet> {
        val benefics = listOf(Planet.JUPITER, Planet.VENUS, Planet.MERCURY, Planet.MOON)
        val planetPos = chart.planetPositions.find { it.planet == planet } ?: return emptyList()

        return chart.planetPositions
            .filter { it.planet in benefics && it.planet != planet }
            .filter { it.house == planetPos.house }
            .map { it.planet }
    }

    // ==================== REMEDY GENERATORS ====================

    private fun getGemstoneRemedy(planet: Planet): Remedy? {
        val gemInfo = planetaryGemstones[planet] ?: return null

        return Remedy(
            category = RemedyCategory.GEMSTONE,
            title = "${gemInfo.name} (${gemInfo.hindiName})",
            description = "Primary gemstone for strengthening ${planet.displayName}",
            method = "Wear ${gemInfo.minCarat}-${gemInfo.maxCarat} carat ${gemInfo.name} set in ${gemInfo.metal} on the ${getGemstoneWearingFinger(planet)} finger",
            timing = "On ${getGemstoneWearingDay(planet)} during ${planet.displayName} Hora",
            duration = "Continuous wear recommended",
            planet = planet,
            priority = RemedyPriority.HIGHLY_RECOMMENDED,
            benefits = listOf(
                "Strengthens ${planet.displayName}'s positive influences",
                "Reduces negative effects of weak ${planet.displayName}",
                "Enhances ${getPlanetLifeArea(planet)}"
            ),
            cautions = listOf(
                "Consult an astrologer before wearing",
                "Ensure the gemstone is natural and untreated",
                "Trial period of 3-7 days recommended"
            )
        )
    }

    private fun getMantraRemedy(planet: Planet): Remedy? {
        val mantraInfo = planetaryMantras[planet] ?: return null

        return Remedy(
            category = RemedyCategory.MANTRA,
            title = "${planet.displayName} Beej Mantra",
            description = "Sacred sound vibration to appease ${planet.displayName}",
            method = "Chant the mantra ${mantraInfo.minimumCount} times:\n\"${mantraInfo.mantra}\"\n(${mantraInfo.sanskrit})",
            timing = mantraInfo.timing,
            duration = "40 days minimum for full effect",
            planet = planet,
            priority = RemedyPriority.ESSENTIAL,
            benefits = listOf(
                "Directly invokes ${planet.displayName}'s energy",
                "Reduces karmic obstacles",
                "Creates positive vibrations"
            ),
            cautions = listOf(
                "Maintain purity during chanting",
                "Face East or the direction of the planet",
                "Use a mala (rosary) of 108 beads"
            )
        )
    }

    private fun getCharityRemedy(planet: Planet): Remedy? {
        val charityInfo = planetaryCharity[planet] ?: return null

        return Remedy(
            category = RemedyCategory.CHARITY,
            title = "${planet.displayName} Daan (Charity)",
            description = "Charitable acts to reduce ${planet.displayName}'s malefic effects",
            method = "Donate: ${charityInfo.items}\nRecipients: ${charityInfo.recipients}",
            timing = "Every ${charityInfo.day}",
            duration = "Regular practice for lasting benefits",
            planet = planet,
            priority = RemedyPriority.HIGHLY_RECOMMENDED,
            benefits = listOf(
                "Reduces negative karma",
                "Pleases ${planet.displayName}",
                "Generates positive merit"
            ),
            cautions = listOf(
                "Donate with pure intentions",
                "Give without expecting returns",
                "Choose genuine recipients"
            )
        )
    }

    private fun getFastingRemedy(planet: Planet): Remedy? {
        val fastingDay = when (planet) {
            Planet.SUN -> "Sunday"
            Planet.MOON -> "Monday"
            Planet.MARS -> "Tuesday"
            Planet.MERCURY -> "Wednesday"
            Planet.JUPITER -> "Thursday"
            Planet.VENUS -> "Friday"
            Planet.SATURN -> "Saturday"
            Planet.RAHU -> "Saturday"
            Planet.KETU -> "Tuesday"
            else -> return null
        }

        return Remedy(
            category = RemedyCategory.FASTING,
            title = "$fastingDay Fast (Vrat)",
            description = "Fasting to strengthen ${planet.displayName}",
            method = "Observe fast from sunrise to sunset on $fastingDay. Consume only fruits, milk, or one meal after sunset.",
            timing = "Every $fastingDay or for 21 continuous ${fastingDay}s",
            duration = "Minimum 21 days, ideally 40 days",
            planet = planet,
            priority = RemedyPriority.RECOMMENDED,
            benefits = listOf(
                "Purifies body and mind",
                "Increases willpower",
                "Pleases ${planet.displayName}"
            ),
            cautions = listOf(
                "Consult a doctor if you have health conditions",
                "Break fast gently with light food",
                "Stay hydrated"
            )
        )
    }

    private fun getColorRemedy(planet: Planet): Remedy? {
        val colors = when (planet) {
            Planet.SUN -> "Orange, Red, Gold"
            Planet.MOON -> "White, Silver, Light Blue"
            Planet.MARS -> "Red, Maroon, Coral"
            Planet.MERCURY -> "Green, Light Green"
            Planet.JUPITER -> "Yellow, Gold, Orange"
            Planet.VENUS -> "White, Pink, Light Colors"
            Planet.SATURN -> "Blue, Black, Dark Colors"
            Planet.RAHU -> "Blue, Smoky, Mixed"
            Planet.KETU -> "Gray, Smoky"
            else -> return null
        }

        return Remedy(
            category = RemedyCategory.COLOR,
            title = "${planet.displayName} Color Therapy",
            description = "Use colors associated with ${planet.displayName}",
            method = "Wear clothes of these colors: $colors\nUse these colors in your environment and workspace.",
            timing = "Especially on ${getGemstoneWearingDay(planet)}",
            duration = "Daily practice",
            planet = planet,
            priority = RemedyPriority.OPTIONAL,
            benefits = listOf(
                "Subtle energy enhancement",
                "Creates favorable vibrations",
                "Easy to implement"
            ),
            cautions = listOf(
                "Balance with other colors",
                "Avoid if contraindicated by other planets"
            )
        )
    }

    private fun getLifestyleRemedy(planet: Planet): Remedy? {
        val lifestyle = when (planet) {
            Planet.SUN -> "Wake early, practice Surya Namaskar, spend time in sunlight, respect father and authority figures"
            Planet.MOON -> "Drink more water/milk, meditate near water, respect mother, practice emotional balance"
            Planet.MARS -> "Exercise regularly, practice martial arts/yoga, channel energy positively, avoid anger"
            Planet.MERCURY -> "Read and study, practice communication skills, learn new things, write daily"
            Planet.JUPITER -> "Study scriptures, respect teachers, practice generosity, maintain ethical conduct"
            Planet.VENUS -> "Appreciate arts and beauty, maintain harmony in relationships, practice gratitude"
            Planet.SATURN -> "Practice discipline, serve the elderly and poor, be patient, work hard consistently"
            Planet.RAHU -> "Avoid shortcuts, practice honesty, reduce desires, avoid intoxicants"
            Planet.KETU -> "Meditate daily, practice detachment, pursue spiritual knowledge"
            else -> return null
        }

        return Remedy(
            category = RemedyCategory.LIFESTYLE,
            title = "${planet.displayName} Lifestyle Practices",
            description = "Daily habits to strengthen ${planet.displayName}",
            method = lifestyle,
            timing = "Daily",
            duration = "Ongoing practice",
            planet = planet,
            priority = RemedyPriority.RECOMMENDED,
            benefits = listOf(
                "Holistic improvement",
                "Sustainable results",
                "Positive karma generation"
            ),
            cautions = emptyList()
        )
    }

    private fun getRudrakshaRemedy(planet: Planet): Remedy? {
        val mukhi = when (planet) {
            Planet.SUN -> "12 Mukhi (12 faces)"
            Planet.MOON -> "2 Mukhi"
            Planet.MARS -> "3 Mukhi"
            Planet.MERCURY -> "4 Mukhi"
            Planet.JUPITER -> "5 Mukhi"
            Planet.VENUS -> "6 Mukhi"
            Planet.SATURN -> "7 Mukhi"
            Planet.RAHU -> "8 Mukhi"
            Planet.KETU -> "9 Mukhi"
            else -> return null
        }

        return Remedy(
            category = RemedyCategory.RUDRAKSHA,
            title = "$mukhi Rudraksha",
            description = "Sacred bead for ${planet.displayName}",
            method = "Wear $mukhi Rudraksha around neck or wrist. Energize with mantra before wearing.",
            timing = "Wear on ${getGemstoneWearingDay(planet)} after purification",
            duration = "Continuous wear",
            planet = planet,
            priority = RemedyPriority.RECOMMENDED,
            benefits = listOf(
                "Natural remedy with no side effects",
                "Balances planetary energy",
                "Spiritual protection"
            ),
            cautions = listOf(
                "Ensure authenticity",
                "Remove during impure activities",
                "Clean regularly with water"
            )
        )
    }

    private fun getYantraRemedy(planet: Planet): Remedy? {
        val yantra = when (planet) {
            Planet.SUN -> "Surya Yantra"
            Planet.MOON -> "Chandra Yantra"
            Planet.MARS -> "Mangal Yantra"
            Planet.MERCURY -> "Budh Yantra"
            Planet.JUPITER -> "Brihaspati Yantra"
            Planet.VENUS -> "Shukra Yantra"
            Planet.SATURN -> "Shani Yantra"
            Planet.RAHU -> "Rahu Yantra"
            Planet.KETU -> "Ketu Yantra"
            else -> return null
        }

        return Remedy(
            category = RemedyCategory.YANTRA,
            title = yantra,
            description = "Sacred geometric diagram for ${planet.displayName}",
            method = "Install $yantra in your puja room or workplace. Energize with mantra and worship daily.",
            timing = "Install on ${getGemstoneWearingDay(planet)} during auspicious muhurta",
            duration = "Permanent installation",
            planet = planet,
            priority = RemedyPriority.OPTIONAL,
            benefits = listOf(
                "Creates positive energy field",
                "Continuous remedy effect",
                "Enhances meditation"
            ),
            cautions = listOf(
                "Install with proper ritual",
                "Keep clean and respected",
                "Perform daily worship"
            )
        )
    }

    private fun getDeityRemedy(planet: Planet): Remedy? {
        val deity = when (planet) {
            Planet.SUN -> "Lord Surya, Lord Vishnu, Lord Ram"
            Planet.MOON -> "Lord Shiva, Goddess Parvati"
            Planet.MARS -> "Lord Hanuman, Lord Kartikeya"
            Planet.MERCURY -> "Lord Vishnu, Lord Krishna"
            Planet.JUPITER -> "Lord Brihaspati, Lord Vishnu, Lord Dakshinamurthy"
            Planet.VENUS -> "Goddess Lakshmi, Goddess Saraswati"
            Planet.SATURN -> "Lord Shani, Lord Hanuman, Lord Bhairav"
            Planet.RAHU -> "Goddess Durga, Lord Bhairav"
            Planet.KETU -> "Lord Ganesha, Lord Chitragupta"
            else -> return null
        }

        return Remedy(
            category = RemedyCategory.DEITY,
            title = "${planet.displayName} Deity Worship",
            description = "Worship deities associated with ${planet.displayName}",
            method = "Regular worship of: $deity\nVisit their temple on ${getGemstoneWearingDay(planet)}",
            timing = "Daily, especially on ${getGemstoneWearingDay(planet)}",
            duration = "Ongoing devotion",
            planet = planet,
            priority = RemedyPriority.HIGHLY_RECOMMENDED,
            benefits = listOf(
                "Divine grace and blessings",
                "Karmic relief",
                "Spiritual growth"
            ),
            cautions = listOf(
                "Worship with devotion",
                "Follow proper procedures"
            )
        )
    }

    // ==================== HELPER METHODS ====================

    private fun getGemstoneWearingFinger(planet: Planet): String {
        return when (planet) {
            Planet.SUN -> "ring"
            Planet.MOON -> "little"
            Planet.MARS -> "ring"
            Planet.MERCURY -> "little"
            Planet.JUPITER -> "index"
            Planet.VENUS -> "middle or little"
            Planet.SATURN -> "middle"
            Planet.RAHU -> "middle"
            Planet.KETU -> "middle"
            else -> "ring"
        }
    }

    private fun getGemstoneWearingDay(planet: Planet): String {
        return when (planet) {
            Planet.SUN -> "Sunday"
            Planet.MOON -> "Monday"
            Planet.MARS -> "Tuesday"
            Planet.MERCURY -> "Wednesday"
            Planet.JUPITER -> "Thursday"
            Planet.VENUS -> "Friday"
            Planet.SATURN -> "Saturday"
            Planet.RAHU -> "Saturday"
            Planet.KETU -> "Tuesday"
            else -> "Sunday"
        }
    }

    private fun getPlanetLifeArea(planet: Planet): String {
        return when (planet) {
            Planet.SUN -> "authority, career, father, government"
            Planet.MOON -> "mind, emotions, mother, public"
            Planet.MARS -> "energy, courage, property, siblings"
            Planet.MERCURY -> "communication, business, education"
            Planet.JUPITER -> "wisdom, wealth, children, fortune"
            Planet.VENUS -> "love, beauty, arts, luxury"
            Planet.SATURN -> "discipline, longevity, career, delays"
            Planet.RAHU -> "foreign connections, technology, ambition"
            Planet.KETU -> "spirituality, liberation, intuition"
            else -> "general well-being"
        }
    }

    private fun generateGeneralRecommendations(
        chart: VedicChart,
        analyses: List<PlanetaryAnalysis>
    ): List<String> {
        val recommendations = mutableListOf<String>()

        // Check overall planetary balance
        val weakCount = analyses.count { it.needsRemedy }
        if (weakCount >= 4) {
            recommendations.add("Multiple planets need attention. Focus on the most afflicted 2-3 planets first.")
        }

        // Check specific combinations
        val sunAnalysis = analyses.find { it.planet == Planet.SUN }
        val moonAnalysis = analyses.find { it.planet == Planet.MOON }

        if (sunAnalysis?.needsRemedy == true && moonAnalysis?.needsRemedy == true) {
            recommendations.add("Both luminaries (Sun and Moon) need strengthening. Prioritize mental and physical health practices.")
        }

        // Check for Ketu in 12th - spiritual tendencies
        val ketuPos = chart.planetPositions.find { it.planet == Planet.KETU }
        if (ketuPos?.house == 12) {
            recommendations.add("Strong spiritual potential indicated. Consider meditation and spiritual practices.")
        }

        // General recommendations
        recommendations.add("Practice daily meditation for overall planetary harmony.")
        recommendations.add("Maintain a clean and organized living space.")
        recommendations.add("Respect elders and perform regular charity.")

        return recommendations
    }

    private fun generateDashaRemedies(chart: VedicChart): List<Remedy> {
        // This would integrate with DashaCalculator for period-specific remedies
        // For now, return general Dasha advice
        return emptyList()
    }

    private fun categorizeByLifeArea(
        remedies: List<Remedy>,
        analyses: List<PlanetaryAnalysis>
    ): Map<String, List<Remedy>> {
        return mapOf(
            "Career" to remedies.filter { it.planet in listOf(Planet.SUN, Planet.SATURN, Planet.JUPITER) },
            "Relationships" to remedies.filter { it.planet in listOf(Planet.VENUS, Planet.MOON, Planet.MARS) },
            "Health" to remedies.filter { it.planet in listOf(Planet.MOON, Planet.SUN, Planet.MARS) },
            "Wealth" to remedies.filter { it.planet in listOf(Planet.JUPITER, Planet.VENUS, Planet.MERCURY) },
            "Spiritual" to remedies.filter { it.planet in listOf(Planet.KETU, Planet.JUPITER, Planet.MOON) }
        ).filterValues { it.isNotEmpty() }
    }

    private fun generateSummary(
        analyses: List<PlanetaryAnalysis>,
        weakPlanets: List<Planet>
    ): String {
        return buildString {
            if (weakPlanets.isEmpty()) {
                appendLine("Your planetary positions are generally favorable. Continue with positive practices to maintain balance.")
            } else {
                appendLine("Primary focus should be on strengthening: ${weakPlanets.take(3).joinToString { it.displayName }}")
                appendLine()
                appendLine("Start with mantras and charity as they are universally beneficial and have no side effects.")
                appendLine("Gemstones should be worn only after consulting an experienced astrologer.")
                appendLine()
                appendLine("Remember: Remedies work best with positive attitude, good deeds, and regular practice.")
            }
        }
    }
}
