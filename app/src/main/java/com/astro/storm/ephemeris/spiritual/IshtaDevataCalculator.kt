package com.astro.storm.ephemeris.spiritual

import com.astro.storm.core.common.Language
import com.astro.storm.core.model.Nakshatra
import com.astro.storm.core.model.Planet
import com.astro.storm.core.model.VedicChart
import com.astro.storm.core.model.ZodiacSign

/**
 * Ishta Devata and Beeja Mantra Calculator
 *
 * Calculates the personal deity (Ishta Devata) based on Jaimini principles
 * and provides Beeja (seed) mantras based on Nakshatra positions.
 *
 * ISHTA DEVATA CALCULATION (Jaimini System):
 * 1. Find Atmakaraka (AK) - planet with highest degree (excluding Rahu in some systems)
 * 2. Note the sign where AK is placed in Navamsa (D9) chart
 * 3. This sign is called Karakamsha
 * 4. The 12th sign from Karakamsha indicates the Ishta Devata
 * 5. The lord and planets in 12th from Karakamsha determine the specific deity
 *
 * BEEJA MANTRA:
 * - Based on Moon's Nakshatra and Pada
 * - Each Nakshatra has 4 syllables (one per pada) for naming
 * - Each Nakshatra has a seed mantra connected to its presiding deity
 *
 * Classical References:
 * - Jaimini Sutras
 * - Brihat Parashara Hora Shastra (BPHS) Chapter on Karakamsha
 * - Phaladeepika
 *
 * @author AstroStorm - Ultra-Precision Vedic Astrology
 */
object IshtaDevataCalculator {

    /**
     * Planets considered for Atmakaraka (excluding Rahu per Parashari)
     */
    private val AK_CANDIDATES = listOf(
        Planet.SUN, Planet.MOON, Planet.MARS, Planet.MERCURY,
        Planet.JUPITER, Planet.VENUS, Planet.SATURN
    )

    /**
     * Perform complete spiritual analysis
     */
    fun analyze(
        chart: VedicChart,
        navamsaChart: VedicChart,
        language: Language = Language.ENGLISH
    ): SpiritualAnalysisResult {
        // Calculate Karakamsha and Ishta Devata
        val karakamshaResult = calculateIshtaDevata(chart, navamsaChart)

        // Calculate Beeja Mantra
        val beejaResult = calculateBeejaMantra(chart)

        // Generate recommended practices
        val practices = generateSpiritualPractices(karakamshaResult, beejaResult)

        // Calculate auspicious days
        val auspiciousDays = calculateAuspiciousDays(karakamshaResult.determinedDeity)

        // Gemstone recommendation
        val gemstone = generateGemstoneRecommendation(karakamshaResult)

        // Generate full report
        val fullReport = generateFullReport(karakamshaResult, beejaResult, practices, language)

        return SpiritualAnalysisResult(
            karakamshaAnalysis = karakamshaResult,
            beejaMantraAnalysis = beejaResult,
            recommendedPractices = practices,
            auspiciousDays = auspiciousDays,
            gemstoneRecommendation = gemstone,
            fullReport = fullReport
        )
    }

    /**
     * Calculate Ishta Devata from Karakamsha
     */
    fun calculateIshtaDevata(
        rasiChart: VedicChart,
        navamsaChart: VedicChart
    ): KarakamshaResult {
        // Step 1: Find Atmakaraka
        val (atmakaraka, akDegree) = findAtmakaraka(rasiChart)

        // Get AK position in Rasi
        val akRasiPosition = rasiChart.planetPositions.find { it.planet == atmakaraka }
        val akRasiSign = akRasiPosition?.sign ?: ZodiacSign.ARIES

        // Step 2: Get AK's Navamsa position
        val akNavamsaPosition = navamsaChart.planetPositions.find { it.planet == atmakaraka }
        val karakamshaSign = akNavamsaPosition?.sign ?: calculateNavamsaSign(akDegree)

        // Step 3: Calculate 12th from Karakamsha (Ishta Devata house)
        val ishtaDevataSign = getSignFromKarakamsha(karakamshaSign, 12)
        val ishtaDevataLord = ishtaDevataSign.ruler

        // Step 4: Find planets in Ishta Devata sign (in Navamsa)
        val occupantsInIshtaSign = navamsaChart.planetPositions
            .filter { it.sign == ishtaDevataSign }
            .map { it.planet }

        // Step 5: Determine the deity
        val (determinedDeity, alternativeDeities) = determineDeity(
            ishtaDevataSign = ishtaDevataSign,
            ishtaDevataLord = ishtaDevataLord,
            occupants = occupantsInIshtaSign,
            atmakaraka = atmakaraka
        )

        val explanation = generateIshtaDevataExplanation(
            atmakaraka = atmakaraka,
            karakamshaSign = karakamshaSign,
            ishtaDevataSign = ishtaDevataSign,
            determinedDeity = determinedDeity,
            occupants = occupantsInIshtaSign
        )

        return KarakamshaResult(
            atmakaraka = atmakaraka,
            atmakarakaDegree = akDegree,
            atmakarakaSign = akRasiSign,
            navamsaSign = karakamshaSign,
            karakamshaSign = karakamshaSign,
            ishtaDevataSign = ishtaDevataSign,
            ishtaDevataLord = ishtaDevataLord,
            occupantsInIshtaDevataSign = occupantsInIshtaSign,
            determinedDeity = determinedDeity,
            alternativeDeities = alternativeDeities,
            explanation = explanation
        )
    }

    /**
     * Find Atmakaraka - planet with highest degree
     */
    private fun findAtmakaraka(chart: VedicChart): Pair<Planet, Double> {
        var maxDegree = -1.0
        var atmakaraka = Planet.SUN

        for (planet in AK_CANDIDATES) {
            val position = chart.planetPositions.find { it.planet == planet } ?: continue
            val degreeInSign = position.longitude % 30.0

            if (degreeInSign > maxDegree) {
                maxDegree = degreeInSign
                atmakaraka = planet
            }
        }

        return atmakaraka to maxDegree
    }

    /**
     * Calculate Navamsa sign from longitude
     */
    private fun calculateNavamsaSign(longitude: Double): ZodiacSign {
        val navamsaPada = ((longitude % 30) / 3.333333).toInt()
        val rasiSign = ZodiacSign.fromLongitude(longitude)

        // Navamsa starts from sign based on element
        val startSign = when (rasiSign.element.lowercase()) {
            "fire" -> ZodiacSign.ARIES
            "earth" -> ZodiacSign.CAPRICORN
            "air" -> ZodiacSign.LIBRA
            "water" -> ZodiacSign.CANCER
            else -> ZodiacSign.ARIES
        }

        val navamsaIndex = (startSign.number - 1 + navamsaPada) % 12
        return ZodiacSign.entries[navamsaIndex]
    }

    /**
     * Get sign at a specific house from reference
     */
    private fun getSignFromKarakamsha(karakamsha: ZodiacSign, house: Int): ZodiacSign {
        val index = (karakamsha.number - 1 + house - 1) % 12
        return ZodiacSign.entries[index]
    }

    /**
     * Determine deity based on Ishta Devata sign analysis
     */
    private fun determineDeity(
        ishtaDevataSign: ZodiacSign,
        ishtaDevataLord: Planet,
        occupants: List<Planet>,
        atmakaraka: Planet
    ): Pair<Deity, List<Deity>> {
        val alternativeDeities = mutableListOf<Deity>()

        // Priority 1: Planets occupying the Ishta Devata sign
        if (occupants.isNotEmpty()) {
            for (planet in occupants) {
                val deity = getDeityFromPlanet(planet)
                if (deity != null) {
                    // Find alternatives
                    Deity.entries
                        .filter { it != deity && (ishtaDevataLord in it.associatedPlanets || ishtaDevataSign in it.associatedSigns) }
                        .forEach { alternativeDeities.add(it) }
                    return deity to alternativeDeities.distinct()
                }
            }
        }

        // Priority 2: Lord of Ishta Devata sign
        val deityFromLord = getDeityFromPlanet(ishtaDevataLord)
        if (deityFromLord != null) {
            Deity.entries
                .filter { it != deityFromLord && ishtaDevataSign in it.associatedSigns }
                .forEach { alternativeDeities.add(it) }
            return deityFromLord to alternativeDeities.distinct()
        }

        // Priority 3: Sign-based determination
        val deityFromSign = getDeityFromSign(ishtaDevataSign)
        Deity.entries
            .filter { it != deityFromSign && ishtaDevataLord in it.associatedPlanets }
            .forEach { alternativeDeities.add(it) }

        return deityFromSign to alternativeDeities.distinct()
    }

    /**
     * Get deity associated with a planet
     */
    private fun getDeityFromPlanet(planet: Planet): Deity? {
        return when (planet) {
            Planet.SUN -> Deity.SURYA
            Planet.MOON -> Deity.KRISHNA
            Planet.MARS -> Deity.KARTIKEYA
            Planet.MERCURY -> Deity.VISHNU
            Planet.JUPITER -> Deity.DATTATREYA
            Planet.VENUS -> Deity.DEVI_LAKSHMI
            Planet.SATURN -> Deity.SHIVA
            Planet.RAHU -> Deity.DEVI_DURGA
            Planet.KETU -> Deity.GANESHA
            else -> null
        }
    }

    /**
     * Get deity associated with a sign
     */
    private fun getDeityFromSign(sign: ZodiacSign): Deity {
        return when (sign) {
            ZodiacSign.ARIES -> Deity.HANUMAN
            ZodiacSign.TAURUS -> Deity.DEVI_LAKSHMI
            ZodiacSign.GEMINI -> Deity.VISHNU
            ZodiacSign.CANCER -> Deity.DEVI_PARVATI
            ZodiacSign.LEO -> Deity.SURYA
            ZodiacSign.VIRGO -> Deity.DEVI_SARASWATI
            ZodiacSign.LIBRA -> Deity.DEVI_LAKSHMI
            ZodiacSign.SCORPIO -> Deity.SHIVA
            ZodiacSign.SAGITTARIUS -> Deity.DATTATREYA
            ZodiacSign.CAPRICORN -> Deity.SHIVA
            ZodiacSign.AQUARIUS -> Deity.SHIVA
            ZodiacSign.PISCES -> Deity.VISHNU
        }
    }

    /**
     * Calculate Beeja Mantra from Moon's Nakshatra
     */
    fun calculateBeejaMantra(chart: VedicChart): BeejaMantraResult {
        val moonPosition = chart.planetPositions.find { it.planet == Planet.MOON }
            ?: return createDefaultBeejaResult()

        val moonNakshatra = moonPosition.nakshatra
        val moonPada = moonPosition.nakshatraPada

        val nakshatraBeejaMantra = NakshatraBeejaMantra.fromNakshatra(moonNakshatra)
            ?: return createDefaultBeejaResult()

        // Get the syllable for the specific pada
        val nameSyllable = when (moonPada) {
            1 -> nakshatraBeejaMantra.syllable1
            2 -> nakshatraBeejaMantra.syllable2
            3 -> nakshatraBeejaMantra.syllable3
            4 -> nakshatraBeejaMantra.syllable4
            else -> nakshatraBeejaMantra.syllable1
        }

        // Additional mantras based on nakshatra ruler
        val additionalMantras = getAdditionalMantras(moonNakshatra)

        val explanation = buildString {
            append("Moon is placed in ${moonNakshatra.displayName} Nakshatra, Pada $moonPada. ")
            append("The ruling syllable for naming is '$nameSyllable'. ")
            append("The primary Beeja Mantra for this Nakshatra is '${nakshatraBeejaMantra.primaryBeeja}'. ")
            append("The presiding deity is ${nakshatraBeejaMantra.deity.displayName}. ")
            append("Regular chanting of this mantra strengthens the Moon and brings ")
            append("mental peace, emotional stability, and spiritual progress.")
        }

        return BeejaMantraResult(
            moonNakshatra = moonNakshatra,
            moonPada = moonPada,
            nameSyllable = nameSyllable,
            primaryBeejaMantra = nakshatraBeejaMantra.primaryBeeja,
            relatedDeity = nakshatraBeejaMantra.deity,
            additionalMantras = additionalMantras,
            explanation = explanation
        )
    }

    /**
     * Get additional mantras based on nakshatra
     */
    private fun getAdditionalMantras(nakshatra: Nakshatra): List<String> {
        val mantras = mutableListOf<String>()

        // Add nakshatra lord's mantra
        val lordMantra = when (nakshatra.ruler) {
            Planet.KETU -> "Om Kem Ketave Namaha"
            Planet.VENUS -> "Om Shukraya Namaha"
            Planet.SUN -> "Om Suryaya Namaha"
            Planet.MOON -> "Om Chandraya Namaha"
            Planet.MARS -> "Om Mangalaya Namaha"
            Planet.RAHU -> "Om Rahave Namaha"
            Planet.JUPITER -> "Om Gurave Namaha"
            Planet.SATURN -> "Om Shanaischaraya Namaha"
            Planet.MERCURY -> "Om Budhaya Namaha"
            else -> null
        }
        lordMantra?.let { mantras.add(it) }

        // Add Maha Mrityunjaya for all water nakshatras (emotional protection)
        if (nakshatra in listOf(
                Nakshatra.ASHLESHA, Nakshatra.JYESHTHA,
                Nakshatra.PURVA_BHADRAPADA, Nakshatra.UTTARA_BHADRAPADA
            )
        ) {
            mantras.add("Om Tryambakam Yajamahe (Maha Mrityunjaya)")
        }

        return mantras
    }

    /**
     * Create default beeja result when Moon position is unavailable
     */
    private fun createDefaultBeejaResult(): BeejaMantraResult {
        return BeejaMantraResult(
            moonNakshatra = Nakshatra.ASHWINI,
            moonPada = 1,
            nameSyllable = "Chu",
            primaryBeejaMantra = "Om",
            relatedDeity = Deity.GANESHA,
            additionalMantras = listOf("Om Gam Ganapataye Namaha"),
            explanation = "Moon position not available. Universal Beeja 'Om' recommended."
        )
    }

    /**
     * Generate spiritual practices based on analysis
     */
    private fun generateSpiritualPractices(
        karakamsha: KarakamshaResult,
        beeja: BeejaMantraResult
    ): List<SpiritualPractice> {
        val practices = mutableListOf<SpiritualPractice>()

        // Primary deity worship
        practices.add(SpiritualPractice(
            practiceName = "${karakamsha.determinedDeity.displayName} Worship",
            description = "Regular worship of your Ishta Devata for spiritual progress and life fulfillment",
            frequency = "Daily",
            bestTime = getBestTimeForDeity(karakamsha.determinedDeity),
            mantra = karakamsha.determinedDeity.mantra
        ))

        // Beeja mantra practice
        practices.add(SpiritualPractice(
            practiceName = "Beeja Mantra Japa",
            description = "Chanting of seed mantra for Moon strengthening and mental peace",
            frequency = "108 times daily",
            bestTime = "Early morning or Monday evenings",
            mantra = beeja.primaryBeejaMantra
        ))

        // Atmakaraka-based practice
        val akPractice = getAtmakarakaPractice(karakamsha.atmakaraka)
        practices.add(akPractice)

        return practices
    }

    /**
     * Get best worship time for deity
     */
    private fun getBestTimeForDeity(deity: Deity): String {
        return when (deity) {
            Deity.SURYA, Deity.RAMA -> "Sunrise (Brahma Muhurta)"
            Deity.SHIVA, Deity.KALI -> "Early morning (4-6 AM) or evening"
            Deity.VISHNU, Deity.KRISHNA -> "Morning and evening"
            Deity.DEVI_LAKSHMI -> "Friday evenings, Diwali"
            Deity.GANESHA -> "Wednesday mornings"
            Deity.HANUMAN -> "Tuesday and Saturday mornings"
            Deity.KARTIKEYA, Deity.SKANDA -> "Tuesday mornings"
            Deity.DEVI_SARASWATI -> "Wednesday mornings, Vasant Panchami"
            Deity.DEVI_DURGA -> "During Navaratri, Tuesday evenings"
            else -> "Morning hours (Brahma Muhurta)"
        }
    }

    /**
     * Get practice based on Atmakaraka planet
     */
    private fun getAtmakarakaPractice(atmakaraka: Planet): SpiritualPractice {
        return when (atmakaraka) {
            Planet.SUN -> SpiritualPractice(
                practiceName = "Surya Namaskar & Gayatri",
                description = "Sun salutation and Gayatri mantra for soul purification",
                frequency = "Daily at sunrise",
                bestTime = "Sunrise",
                mantra = "Om Bhur Bhuva Swaha..."
            )
            Planet.MOON -> SpiritualPractice(
                practiceName = "Chandra Puja",
                description = "Moon worship for emotional healing and intuition",
                frequency = "Mondays and Full Moons",
                bestTime = "Evening",
                mantra = "Om Som Somaya Namaha"
            )
            Planet.MARS -> SpiritualPractice(
                practiceName = "Hanuman Chalisa",
                description = "Recitation for courage, strength, and protection",
                frequency = "Tuesdays and Saturdays",
                bestTime = "Morning",
                mantra = "Jai Hanuman Gyan Gun Sagar..."
            )
            Planet.MERCURY -> SpiritualPractice(
                practiceName = "Vishnu Sahasranama",
                description = "1000 names of Vishnu for wisdom and success",
                frequency = "Wednesdays",
                bestTime = "Morning",
                mantra = "Om Namo Bhagavate Vasudevaya"
            )
            Planet.JUPITER -> SpiritualPractice(
                practiceName = "Guru Stotram",
                description = "Prayers to the Guru principle for wisdom",
                frequency = "Thursdays",
                bestTime = "Morning",
                mantra = "Om Brim Brihaspataye Namaha"
            )
            Planet.VENUS -> SpiritualPractice(
                practiceName = "Lakshmi Puja",
                description = "Worship of Lakshmi for prosperity and beauty",
                frequency = "Fridays",
                bestTime = "Evening",
                mantra = "Om Shreem Mahalakshmiyei Namaha"
            )
            Planet.SATURN -> SpiritualPractice(
                practiceName = "Shani Stotram",
                description = "Saturn prayers for karmic balance and discipline",
                frequency = "Saturdays",
                bestTime = "Evening",
                mantra = "Om Sham Shanaishcharaya Namaha"
            )
            else -> SpiritualPractice(
                practiceName = "Universal Meditation",
                description = "Silent meditation on the Supreme",
                frequency = "Daily",
                bestTime = "Brahma Muhurta",
                mantra = "Om"
            )
        }
    }

    /**
     * Calculate auspicious days for deity worship
     */
    private fun calculateAuspiciousDays(deity: Deity): List<AuspiciousDay> {
        return when (deity) {
            Deity.SHIVA -> listOf(
                AuspiciousDay("Monday", "Shiva's day", "Rudrabhishek, Shiva Puja"),
                AuspiciousDay("Maha Shivaratri", "Most auspicious night for Shiva", "Night-long worship"),
                AuspiciousDay("Pradosh", "13th tithi evenings", "Special Shiva worship")
            )
            Deity.VISHNU, Deity.KRISHNA, Deity.RAMA -> listOf(
                AuspiciousDay("Thursday", "Vishnu's day", "Vishnu Sahasranama"),
                AuspiciousDay("Ekadashi", "11th tithi", "Fasting and Vishnu worship"),
                AuspiciousDay("Janmashtami", "Krishna's birthday", "Night vigil and puja")
            )
            Deity.DEVI_LAKSHMI -> listOf(
                AuspiciousDay("Friday", "Lakshmi's day", "Lakshmi Puja"),
                AuspiciousDay("Diwali", "Festival of lights", "Main Lakshmi worship"),
                AuspiciousDay("Full Moon", "Purnima", "Lakshmi is pleased")
            )
            Deity.GANESHA -> listOf(
                AuspiciousDay("Wednesday", "Mercury's day (Ganesha)", "Ganesha Puja"),
                AuspiciousDay("Chaturthi", "4th tithi", "Ganesha's tithi"),
                AuspiciousDay("Ganesh Chaturthi", "Ganesha festival", "Main worship day")
            )
            Deity.HANUMAN -> listOf(
                AuspiciousDay("Tuesday", "Mars day (Hanuman)", "Hanuman Chalisa"),
                AuspiciousDay("Saturday", "Also auspicious for Hanuman", "Hanuman worship"),
                AuspiciousDay("Hanuman Jayanti", "Hanuman's birthday", "Special worship")
            )
            else -> listOf(
                AuspiciousDay("Your birth nakshatra day", "Personal auspicious day", "Deity worship"),
                AuspiciousDay("Full Moon", "General auspicious day", "Any divine worship")
            )
        }
    }

    /**
     * Generate gemstone recommendation
     */
    private fun generateGemstoneRecommendation(karakamsha: KarakamshaResult): GemstoneRecommendation {
        val lord = karakamsha.ishtaDevataLord

        return when (lord) {
            Planet.SUN -> GemstoneRecommendation(
                primaryGemstone = "Ruby (Manik)",
                alternativeGemstones = listOf("Red Spinel", "Red Garnet"),
                metal = "Gold",
                finger = "Ring finger (Sunday)",
                weight = "3-6 carats",
                energizationMantra = "Om Hraam Hreem Hraum Sah Suryaya Namaha"
            )
            Planet.MOON -> GemstoneRecommendation(
                primaryGemstone = "Pearl (Moti)",
                alternativeGemstones = listOf("Moonstone", "White Coral"),
                metal = "Silver",
                finger = "Little finger (Monday)",
                weight = "4-6 carats",
                energizationMantra = "Om Som Somaya Namaha"
            )
            Planet.MARS -> GemstoneRecommendation(
                primaryGemstone = "Red Coral (Moonga)",
                alternativeGemstones = listOf("Carnelian", "Red Jasper"),
                metal = "Gold or Copper",
                finger = "Ring finger (Tuesday)",
                weight = "5-9 carats",
                energizationMantra = "Om Kraam Kreem Kraum Sah Bhaumaya Namaha"
            )
            Planet.MERCURY -> GemstoneRecommendation(
                primaryGemstone = "Emerald (Panna)",
                alternativeGemstones = listOf("Green Tourmaline", "Peridot"),
                metal = "Gold",
                finger = "Little finger (Wednesday)",
                weight = "3-6 carats",
                energizationMantra = "Om Braam Breem Braum Sah Budhaya Namaha"
            )
            Planet.JUPITER -> GemstoneRecommendation(
                primaryGemstone = "Yellow Sapphire (Pukhraj)",
                alternativeGemstones = listOf("Yellow Topaz", "Citrine"),
                metal = "Gold",
                finger = "Index finger (Thursday)",
                weight = "3-6 carats",
                energizationMantra = "Om Graam Greem Graum Sah Gurave Namaha"
            )
            Planet.VENUS -> GemstoneRecommendation(
                primaryGemstone = "Diamond (Heera)",
                alternativeGemstones = listOf("White Sapphire", "White Topaz", "Zircon"),
                metal = "Platinum or White Gold",
                finger = "Middle finger (Friday)",
                weight = "1-2 carats",
                energizationMantra = "Om Draam Dreem Draum Sah Shukraya Namaha"
            )
            Planet.SATURN -> GemstoneRecommendation(
                primaryGemstone = "Blue Sapphire (Neelam)",
                alternativeGemstones = listOf("Amethyst", "Blue Spinel"),
                metal = "Gold or Silver",
                finger = "Middle finger (Saturday)",
                weight = "4-7 carats",
                energizationMantra = "Om Praam Preem Praum Sah Shanaischaraya Namaha"
            )
            else -> GemstoneRecommendation(
                primaryGemstone = "Clear Quartz",
                alternativeGemstones = listOf("Rock Crystal"),
                metal = "Silver",
                finger = "Any finger",
                weight = "5+ carats",
                energizationMantra = "Om"
            )
        }
    }

    /**
     * Generate Ishta Devata explanation
     */
    private fun generateIshtaDevataExplanation(
        atmakaraka: Planet,
        karakamshaSign: ZodiacSign,
        ishtaDevataSign: ZodiacSign,
        determinedDeity: Deity,
        occupants: List<Planet>
    ): String {
        return buildString {
            appendLine("ISHTA DEVATA DETERMINATION")
            appendLine("=" .repeat(40))
            appendLine()
            appendLine("Step 1: Atmakaraka (Soul Significator)")
            appendLine("Your Atmakaraka is ${atmakaraka.displayName}, the planet with the highest degree.")
            appendLine("The Atmakaraka represents your soul's journey and ultimate purpose.")
            appendLine()
            appendLine("Step 2: Karakamsha (AK in Navamsa)")
            appendLine("${atmakaraka.displayName} is placed in ${karakamshaSign.displayName} in Navamsa.")
            appendLine("This sign is called Karakamsha - the soul's dwelling.")
            appendLine()
            appendLine("Step 3: Ishta Devata Sign (12th from Karakamsha)")
            appendLine("The 12th house from Karakamsha is ${ishtaDevataSign.displayName}.")
            appendLine("This sign reveals your personal deity for liberation (Moksha).")
            appendLine()
            if (occupants.isNotEmpty()) {
                appendLine("Planets in Ishta Devata sign: ${occupants.joinToString(", ") { it.displayName }}")
            }
            appendLine()
            appendLine("RESULT: Your Ishta Devata is ${determinedDeity.displayName}")
            appendLine("Sanskrit: ${determinedDeity.sanskritName}")
            appendLine()
            appendLine("${determinedDeity.description}")
            appendLine()
            appendLine("Primary Mantra: ${determinedDeity.mantra}")
        }
    }

    /**
     * Generate full spiritual report
     */
    private fun generateFullReport(
        karakamsha: KarakamshaResult,
        beeja: BeejaMantraResult,
        practices: List<SpiritualPractice>,
        language: Language
    ): String {
        return buildString {
            appendLine("═".repeat(60))
            appendLine("          SPIRITUAL ANALYSIS REPORT")
            appendLine("          Ishta Devata & Beeja Mantra")
            appendLine("═".repeat(60))
            appendLine()

            // Ishta Devata Section
            append(karakamsha.explanation)
            appendLine()

            // Beeja Mantra Section
            appendLine("─".repeat(60))
            appendLine("BEEJA MANTRA ANALYSIS")
            appendLine("─".repeat(60))
            appendLine()
            appendLine(beeja.explanation)
            appendLine()
            appendLine("Name Syllable: ${beeja.nameSyllable}")
            appendLine("Primary Beeja: ${beeja.primaryBeejaMantra}")
            if (beeja.additionalMantras.isNotEmpty()) {
                appendLine("Additional Mantras:")
                beeja.additionalMantras.forEach { appendLine("  • $it") }
            }
            appendLine()

            // Practices Section
            appendLine("─".repeat(60))
            appendLine("RECOMMENDED SPIRITUAL PRACTICES")
            appendLine("─".repeat(60))
            practices.forEach { practice ->
                appendLine()
                appendLine("${practice.practiceName}")
                appendLine("  ${practice.description}")
                appendLine("  Frequency: ${practice.frequency}")
                appendLine("  Best Time: ${practice.bestTime}")
                appendLine("  Mantra: ${practice.mantra}")
            }
            appendLine()

            appendLine("═".repeat(60))
            appendLine("Generated by AstroStorm - Ultra-Precision Vedic Astrology")
            appendLine("═".repeat(60))
        }
    }
}
