package com.astro.vajra.ephemeris.remedy

import com.astro.vajra.core.model.Nakshatra
import com.astro.vajra.core.model.Planet
import com.astro.vajra.core.model.PlanetPosition
import com.astro.vajra.core.model.VedicChart
import com.astro.vajra.core.model.ZodiacSign
import com.astro.vajra.ephemeris.DivisionalChartCalculator
import com.astro.vajra.ephemeris.DivisionalChartType
import com.astro.vajra.ephemeris.VedicAstrologyUtils
import com.astro.vajra.ephemeris.jaimini.JaiminiKarakaCalculator
import com.astro.vajra.ephemeris.jaimini.JaiminiKarakaCalculator.KarakaType

/**
 * IshtaDevataCalculator - Personalized Deity & Beeja Mantra Determination System
 *
 * Calculates the Ishta Devata (chosen deity), Dharma Devata, and Palana Devata
 * based on the Jaimini system (BPHS and Jaimini Sutras). Also generates
 * personalized Beeja Mantras based on the birth chart.
 *
 * Methodology (per BPHS Chapter 33, Jaimini Sutras 1.2.72-80):
 *   1. Identify the Atmakaraka (AK) - highest degree planet
 *   2. Find the Navamsa (D9) position of the Atmakaraka = Karakamsha
 *   3. The 12th house from Karakamsha determines Ishta Devata
 *   4. The 5th house from Karakamsha determines Dharma Devata
 *   5. The 6th house from Karakamsha determines Palana Devata
 *   6. The planet occupying or ruling the relevant house determines the deity
 *
 * Beeja Mantra generation follows:
 *   - Planet-specific Beeja mantras from Mantra Shastra
 *   - Nakshatra pada syllables from Narada Purana / Agni Purana
 *   - Deity-specific Beejas from Tantra Shastra traditions
 *
 * Vedic References:
 *   - Brihat Parashara Hora Shastra (BPHS), Chapter 33
 *   - Jaimini Sutras (1st Adhyaya, 2nd Pada)
 *   - Narada Purana (Nakshatra Aksharas)
 *   - Mantra Mahodadhi (Beeja Mantras)
 */
object IshtaDevataCalculator {

    // ============================================================================
    // PRIMARY API
    // ============================================================================

    /**
     * Perform complete Ishta Devata analysis for a given chart.
     *
     * @param chart The birth chart (VedicChart)
     * @return IshtaDevataAnalysis with deity determinations, mantras, and prescriptions
     */
    fun calculateIshtaDevata(chart: VedicChart): IshtaDevataAnalysis {
        // Step 1: Calculate Chara Karakas to identify Atmakaraka
        val karakaAnalysis = JaiminiKarakaCalculator.calculateKarakas(chart)
        val akAssignment = karakaAnalysis.getAtmakaraka()
            ?: throw IllegalStateException("Unable to determine Atmakaraka from chart")

        val atmakarakaInfo = AtmakarakaInfo(
            planet = akAssignment.planet,
            degreeInSign = akAssignment.degreeInSign,
            rashi = akAssignment.sign,
            house = akAssignment.house,
            isRetrograde = akAssignment.isRetrograde,
            dignity = mapDignity(akAssignment.dignity)
        )

        // Step 2: Find Karakamsha - Navamsa sign of Atmakaraka
        val navamsaChart = DivisionalChartCalculator.calculateDivisionalChart(
            chart, DivisionalChartType.D9_NAVAMSA
        )
        val akNavamsaPos = navamsaChart.planetPositions.find { it.planet == akAssignment.planet }
            ?: throw IllegalStateException("Cannot find Atmakaraka in Navamsa chart")

        val karakamshaSign = akNavamsaPos.sign

        val karakamshaInfo = KarakamshaInfo(
            sign = karakamshaSign,
            lord = karakamshaSign.ruler,
            atmakarakaNavamsaDegree = akNavamsaPos.longitude % 30.0,
            planetsInKarakamsha = navamsaChart.planetPositions
                .filter { it.sign == karakamshaSign }
                .map { it.planet }
        )

        // Step 3: Determine Ishta Devata from 12th from Karakamsha
        val ishtaDevata = determineDeityFromHouse(
            navamsaChart.planetPositions,
            karakamshaSign,
            12,
            DeityContext.ISHTA_DEVATA
        )

        // Step 4: Determine Dharma Devata from 5th from Karakamsha
        val dharmaDevata = determineDeityFromHouse(
            navamsaChart.planetPositions,
            karakamshaSign,
            5,
            DeityContext.DHARMA_DEVATA
        )

        // Step 5: Determine Palana Devata from 6th from Karakamsha
        val palanaDevata = determineDeityFromHouse(
            navamsaChart.planetPositions,
            karakamshaSign,
            6,
            DeityContext.PALANA_DEVATA
        )

        // Step 6: Generate personal mantras
        val moonPos = chart.planetPositions.find { it.planet == Planet.MOON }
        val sunPos = chart.planetPositions.find { it.planet == Planet.SUN }
        val birthNakshatra = moonPos?.let { Nakshatra.fromLongitude(it.longitude) }

        val nakshatraSyllable = birthNakshatra?.let { (nak, pada) ->
            val syllables = getNakshatraPadaSyllables(nak)
            val primarySyllable = syllables.getOrElse(pada - 1) { syllables.firstOrNull() ?: "" }
            NakshatraSyllable(
                nakshatra = nak,
                pada = pada,
                primarySyllable = primarySyllable,
                allPadaSyllables = syllables,
                associatedLetter = primarySyllable.firstOrNull()?.toString() ?: ""
            )
        } ?: NakshatraSyllable(
            nakshatra = Nakshatra.ASHWINI,
            pada = 1,
            primarySyllable = "Chu",
            allPadaSyllables = listOf("Chu", "Che", "Cho", "La"),
            associatedLetter = "C"
        )

        val personalMantras = generatePersonalMantras(
            ishtaDevata = ishtaDevata,
            atmakaraka = akAssignment.planet,
            birthNakshatra = birthNakshatra?.first,
            birthPada = birthNakshatra?.second ?: 1,
            chart = chart
        )

        // Step 7: Generate remedial prescription
        val remedialPrescription = generateRemedialPrescription(
            ishtaDevata = ishtaDevata,
            atmakaraka = akAssignment.planet,
            chart = chart
        )

        return IshtaDevataAnalysis(
            atmakaraka = atmakarakaInfo,
            karakamsha = karakamshaInfo,
            ishtaDevata = ishtaDevata,
            dharmaDevata = dharmaDevata,
            palanaDevata = palanaDevata,
            personalMantras = personalMantras,
            nakshatraSyllable = nakshatraSyllable,
            remedialPrescription = remedialPrescription
        )
    }

    // ============================================================================
    // DEITY DETERMINATION
    // ============================================================================

    /**
     * Determines the deity for a given house from the Karakamsha sign.
     *
     * Logic:
     *   1. Calculate the sign that is N houses from Karakamsha
     *   2. If any planet occupies that sign in the Navamsa, use that planet's deity
     *   3. If multiple planets, use the strongest (by dignity)
     *   4. If no planet, use the sign lord's deity
     */
    private fun determineDeityFromHouse(
        navamsaPlanets: List<PlanetPosition>,
        karakamshaSign: ZodiacSign,
        houseFromKarakamsha: Int,
        context: DeityContext
    ): DeityInfo {
        val targetSign = getSignFromKarakamsha(karakamshaSign, houseFromKarakamsha)
        val planetsInSign = navamsaPlanets.filter { it.sign == targetSign }

        val determiningPlanet: Planet
        val determinationMethod: String

        if (planetsInSign.isNotEmpty()) {
            // Multiple planets: pick strongest by dignity hierarchy
            determiningPlanet = planetsInSign
                .sortedByDescending { dignityRank(it) }
                .first().planet
            determinationMethod = "Planet occupying ${ordinal(houseFromKarakamsha)} from Karakamsha"
        } else {
            // No planet: use sign lord
            determiningPlanet = targetSign.ruler
            determinationMethod = "Lord of ${ordinal(houseFromKarakamsha)} from Karakamsha (no occupant)"
        }

        val deityNames = getDeityForPlanet(determiningPlanet, context)
        val beeja = getDeityBeeja(determiningPlanet, context)

        return DeityInfo(
            primaryDeity = deityNames.first,
            alternateDeities = deityNames.second,
            determiningPlanet = determiningPlanet,
            houseFromKarakamsha = houseFromKarakamsha,
            sign = targetSign,
            determinationMethod = determinationMethod,
            deityBeeja = beeja,
            deityMantra = buildDeityMantra(deityNames.first, beeja),
            description = buildDeityDescription(deityNames.first, determiningPlanet, context)
        )
    }

    /**
     * Get the sign that is N houses from a reference sign (whole-sign house system).
     */
    private fun getSignFromKarakamsha(karakamshaSign: ZodiacSign, housesForward: Int): ZodiacSign {
        val targetNumber = ((karakamshaSign.number - 1 + housesForward - 1) % 12) + 1
        return ZodiacSign.entries.first { it.number == targetNumber }
    }

    /**
     * Maps a planet to its deity assignment per BPHS and Jaimini traditions.
     * Returns Pair<primaryDeity, listOfAlternates>.
     */
    private fun getDeityForPlanet(
        planet: Planet,
        context: DeityContext
    ): Pair<String, List<String>> {
        return when (planet) {
            Planet.SUN -> when (context) {
                DeityContext.ISHTA_DEVATA -> "Shiva" to listOf("Vishnu (Rama)", "Surya Narayana", "Aditya")
                DeityContext.DHARMA_DEVATA -> "Surya" to listOf("Rama", "Agni")
                DeityContext.PALANA_DEVATA -> "Rama" to listOf("Surya", "Shiva")
            }
            Planet.MOON -> when (context) {
                DeityContext.ISHTA_DEVATA -> "Gauri (Parvati)" to listOf("Krishna", "Ambika", "Lalita")
                DeityContext.DHARMA_DEVATA -> "Parvati" to listOf("Devi", "Chandra")
                DeityContext.PALANA_DEVATA -> "Krishna" to listOf("Durga", "Parvati")
            }
            Planet.MARS -> when (context) {
                DeityContext.ISHTA_DEVATA -> "Hanuman" to listOf("Skanda (Kartikeya)", "Narasimha", "Rudra")
                DeityContext.DHARMA_DEVATA -> "Skanda" to listOf("Hanuman", "Mangala")
                DeityContext.PALANA_DEVATA -> "Narasimha" to listOf("Hanuman", "Bhairava")
            }
            Planet.MERCURY -> when (context) {
                DeityContext.ISHTA_DEVATA -> "Vishnu" to listOf("Buddha (Siddhartha)", "Dattatreya", "Narayana")
                DeityContext.DHARMA_DEVATA -> "Narayana" to listOf("Vishnu", "Saraswati")
                DeityContext.PALANA_DEVATA -> "Dattatreya" to listOf("Vishnu", "Budha Graha")
            }
            Planet.JUPITER -> when (context) {
                DeityContext.ISHTA_DEVATA -> "Vishnu (Vamana)" to listOf("Saddashiva", "Dattatreya", "Brihaspati", "Dakshinamurthy")
                DeityContext.DHARMA_DEVATA -> "Saddashiva" to listOf("Brihaspati", "Vishnu")
                DeityContext.PALANA_DEVATA -> "Dakshinamurthy" to listOf("Brihaspati", "Vishnu")
            }
            Planet.VENUS -> when (context) {
                DeityContext.ISHTA_DEVATA -> "Mahalakshmi" to listOf("Lakshmi", "Parashurama", "Annapurna")
                DeityContext.DHARMA_DEVATA -> "Lakshmi" to listOf("Shukra", "Annapurna")
                DeityContext.PALANA_DEVATA -> "Annapurna" to listOf("Lakshmi", "Tulasi Devi")
            }
            Planet.SATURN -> when (context) {
                DeityContext.ISHTA_DEVATA -> "Shani Dev" to listOf("Brahma", "Vishnu (Kurma)", "Kala Bhairava")
                DeityContext.DHARMA_DEVATA -> "Brahma" to listOf("Kala Bhairava", "Shani")
                DeityContext.PALANA_DEVATA -> "Kala Bhairava" to listOf("Shani Dev", "Yama")
            }
            Planet.RAHU -> when (context) {
                DeityContext.ISHTA_DEVATA -> "Durga" to listOf("Varaha", "Tamasi forms of Divine Mother", "Kali")
                DeityContext.DHARMA_DEVATA -> "Kali" to listOf("Durga", "Chinnamasta")
                DeityContext.PALANA_DEVATA -> "Varaha" to listOf("Durga", "Ugra Tara")
            }
            Planet.KETU -> when (context) {
                DeityContext.ISHTA_DEVATA -> "Ganesha" to listOf("Matsya", "Chitragupta", "Skanda")
                DeityContext.DHARMA_DEVATA -> "Ganesha" to listOf("Matsya", "Chitragupta")
                DeityContext.PALANA_DEVATA -> "Chitragupta" to listOf("Ganesha", "Matsya")
            }
            else -> "Vishnu" to listOf("Shiva", "Devi")
        }
    }

    /**
     * Get deity-specific Beeja mantra syllable.
     */
    private fun getDeityBeeja(planet: Planet, context: DeityContext): String {
        return when (planet) {
            Planet.SUN -> when (context) {
                DeityContext.ISHTA_DEVATA -> "Om Namah Shivaya"
                DeityContext.DHARMA_DEVATA -> "Om Suryaya Namah"
                DeityContext.PALANA_DEVATA -> "Om Shri Ramaya Namah"
            }
            Planet.MOON -> when (context) {
                DeityContext.ISHTA_DEVATA -> "Om Aim Hreem Shreem"
                DeityContext.DHARMA_DEVATA -> "Om Shri Parvatyai Namah"
                DeityContext.PALANA_DEVATA -> "Om Namo Bhagavate Vasudevaya"
            }
            Planet.MARS -> when (context) {
                DeityContext.ISHTA_DEVATA -> "Om Shri Hanumate Namah"
                DeityContext.DHARMA_DEVATA -> "Om Sharavanabhavaya Namah"
                DeityContext.PALANA_DEVATA -> "Om Ugram Veeram Mahavishnum"
            }
            Planet.MERCURY -> when (context) {
                DeityContext.ISHTA_DEVATA -> "Om Namo Narayanaya"
                DeityContext.DHARMA_DEVATA -> "Om Namo Narayanaya"
                DeityContext.PALANA_DEVATA -> "Om Shri Dattatreyaya Namah"
            }
            Planet.JUPITER -> when (context) {
                DeityContext.ISHTA_DEVATA -> "Om Namo Bhagavate Vasudevaya"
                DeityContext.DHARMA_DEVATA -> "Om Namah Shivaya"
                DeityContext.PALANA_DEVATA -> "Om Shri Dakshinamurthaye Namah"
            }
            Planet.VENUS -> when (context) {
                DeityContext.ISHTA_DEVATA -> "Om Shreem Mahalakshmyai Namah"
                DeityContext.DHARMA_DEVATA -> "Om Shri Lakshmyai Namah"
                DeityContext.PALANA_DEVATA -> "Om Annapurnayai Namah"
            }
            Planet.SATURN -> when (context) {
                DeityContext.ISHTA_DEVATA -> "Om Sham Shanaischaraya Namah"
                DeityContext.DHARMA_DEVATA -> "Om Brahmane Namah"
                DeityContext.PALANA_DEVATA -> "Om Kala Bhairavaya Namah"
            }
            Planet.RAHU -> when (context) {
                DeityContext.ISHTA_DEVATA -> "Om Dum Durgayai Namah"
                DeityContext.DHARMA_DEVATA -> "Om Kreem Kalikayai Namah"
                DeityContext.PALANA_DEVATA -> "Om Shri Varahaya Namah"
            }
            Planet.KETU -> when (context) {
                DeityContext.ISHTA_DEVATA -> "Om Gam Ganapataye Namah"
                DeityContext.DHARMA_DEVATA -> "Om Gam Ganapataye Namah"
                DeityContext.PALANA_DEVATA -> "Om Shri Chitraguptaya Namah"
            }
            else -> "Om Namo Narayanaya"
        }
    }

    // ============================================================================
    // BEEJA MANTRA GENERATION
    // ============================================================================

    /**
     * Generate complete personalized mantras including:
     *   - Deity-specific mantra
     *   - Planet-specific Beeja mantras (for AK and afflicted planets)
     *   - Nakshatra-syllable based mantra
     */
    private fun generatePersonalMantras(
        ishtaDevata: DeityInfo,
        atmakaraka: Planet,
        birthNakshatra: Nakshatra?,
        birthPada: Int,
        chart: VedicChart
    ): PersonalMantras {
        // Planet-specific Beeja mantras
        val planetBeejas = generatePlanetBeejas()

        // Atmakaraka mantra
        val akMantra = planetBeejas[atmakaraka]
            ?: "Om Hraam Hreem Hraum Sah Suryaya Namah"

        // Identify afflicted planets and their mantras
        val afflictedMantras = identifyAfflictedPlanetMantras(chart, planetBeejas)

        // Nakshatra-syllable composite mantra
        val nakshatraMantra = birthNakshatra?.let { nak ->
            val syllables = getNakshatraPadaSyllables(nak)
            val primarySyllable = syllables.getOrElse(birthPada - 1) { syllables.first() }
            "Om $primarySyllable Namah"
        } ?: "Om Namah"

        // Complete composite Beeja mantra
        val compositeBeeja = buildCompositeBeeja(
            ishtaDevata.deityBeeja,
            akMantra,
            birthNakshatra,
            birthPada
        )

        // Recommended count and timing
        val mantraCount = getRecommendedMantraCount(atmakaraka)
        val bestHora = getHoraForPlanet(ishtaDevata.determiningPlanet)
        val direction = getDirectionForPlanet(ishtaDevata.determiningPlanet)

        return PersonalMantras(
            ishtaDevataMantra = ishtaDevata.deityMantra,
            atmakarakaMantra = akMantra,
            nakshatraMantra = nakshatraMantra,
            compositeBeeja = compositeBeeja,
            afflictedPlanetMantras = afflictedMantras,
            allPlanetBeejas = planetBeejas,
            recommendedCount = mantraCount,
            recommendedTiming = "During ${bestHora.displayName} Hora",
            recommendedDirection = direction,
            specialInstructions = buildSpecialInstructions(atmakaraka, ishtaDevata)
        )
    }

    /**
     * Complete planet-specific Beeja mantras per Mantra Shastra.
     */
    private fun generatePlanetBeejas(): Map<Planet, String> {
        return mapOf(
            Planet.SUN to "Om Hraam Hreem Hraum Sah Suryaya Namah",
            Planet.MOON to "Om Shraam Shreem Shraum Sah Chandraya Namah",
            Planet.MARS to "Om Kraam Kreem Kraum Sah Bhaumaya Namah",
            Planet.MERCURY to "Om Braam Breem Braum Sah Budhaya Namah",
            Planet.JUPITER to "Om Graam Greem Graum Sah Gurave Namah",
            Planet.VENUS to "Om Draam Dreem Draum Sah Shukraya Namah",
            Planet.SATURN to "Om Praam Preem Praum Sah Shanaischaraya Namah",
            Planet.RAHU to "Om Bhraam Bhreem Bhraum Sah Rahuve Namah",
            Planet.KETU to "Om Straam Streem Straum Sah Ketave Namah"
        )
    }

    /**
     * Identifies afflicted planets and returns their remedial mantras.
     * A planet is considered afflicted if:
     *   - Debilitated
     *   - In enemy sign
     *   - Combust
     *   - In dusthana (6, 8, 12)
     *   - Retrograde in debilitation
     */
    private fun identifyAfflictedPlanetMantras(
        chart: VedicChart,
        planetBeejas: Map<Planet, String>
    ): Map<Planet, AfflictedPlanetMantra> {
        val result = mutableMapOf<Planet, AfflictedPlanetMantra>()
        val sunPos = chart.planetPositions.find { it.planet == Planet.SUN }

        for (pos in chart.planetPositions) {
            if (pos.planet !in Planet.MAIN_PLANETS) continue

            val afflictions = mutableListOf<String>()

            if (VedicAstrologyUtils.isDebilitated(pos)) {
                afflictions.add("Debilitated in ${pos.sign.displayName}")
            }
            if (VedicAstrologyUtils.isInEnemySign(pos)) {
                afflictions.add("In enemy sign ${pos.sign.displayName}")
            }
            if (sunPos != null && VedicAstrologyUtils.isCombust(pos, sunPos)) {
                afflictions.add("Combust (too close to Sun)")
            }
            if (VedicAstrologyUtils.isDusthana(pos.house)) {
                afflictions.add("In ${ordinal(pos.house)} house (Dusthana)")
            }
            if (pos.isRetrograde && VedicAstrologyUtils.isDebilitated(pos)) {
                afflictions.add("Retrograde while debilitated")
            }

            if (afflictions.isNotEmpty()) {
                val beeja = planetBeejas[pos.planet] ?: continue
                val count = when {
                    afflictions.size >= 3 -> 21600 // 200 malas
                    afflictions.size == 2 -> 11880 // 110 malas
                    else -> 7560 // 70 malas
                }
                result[pos.planet] = AfflictedPlanetMantra(
                    planet = pos.planet,
                    mantra = beeja,
                    afflictions = afflictions,
                    recommendedCount = count,
                    bestDay = getWeekdayForPlanet(pos.planet),
                    specialNotes = getAfflictionNotes(pos.planet, afflictions)
                )
            }
        }

        return result
    }

    /**
     * Build a composite Beeja mantra combining deity, planet, and nakshatra elements.
     */
    private fun buildCompositeBeeja(
        deityBeeja: String,
        akMantra: String,
        birthNakshatra: Nakshatra?,
        birthPada: Int
    ): String {
        val syllable = birthNakshatra?.let { nak ->
            val syllables = getNakshatraPadaSyllables(nak)
            syllables.getOrElse(birthPada - 1) { syllables.first() }
        } ?: "Om"

        return "Om $syllable | $deityBeeja | $akMantra"
    }

    // ============================================================================
    // NAKSHATRA PADA SYLLABLES (ALL 108 PADAS)
    // ============================================================================

    /**
     * Returns the 4 pada syllables for a given Nakshatra.
     * Based on traditional Narada Purana / Agni Purana aksharas.
     */
    private fun getNakshatraPadaSyllables(nakshatra: Nakshatra): List<String> {
        return when (nakshatra) {
            Nakshatra.ASHWINI -> listOf("Chu", "Che", "Cho", "La")
            Nakshatra.BHARANI -> listOf("Li", "Lu", "Le", "Lo")
            Nakshatra.KRITTIKA -> listOf("A", "I", "U", "E")
            Nakshatra.ROHINI -> listOf("O", "Va", "Vi", "Vu")
            Nakshatra.MRIGASHIRA -> listOf("Ve", "Vo", "Ka", "Ki")
            Nakshatra.ARDRA -> listOf("Ku", "Gha", "Ng", "Chha")
            Nakshatra.PUNARVASU -> listOf("Ke", "Ko", "Ha", "Hi")
            Nakshatra.PUSHYA -> listOf("Hu", "He", "Ho", "Da")
            Nakshatra.ASHLESHA -> listOf("Di", "Du", "De", "Do")
            Nakshatra.MAGHA -> listOf("Ma", "Mi", "Mu", "Me")
            Nakshatra.PURVA_PHALGUNI -> listOf("Mo", "Ta", "Ti", "Tu")
            Nakshatra.UTTARA_PHALGUNI -> listOf("Te", "To", "Pa", "Pi")
            Nakshatra.HASTA -> listOf("Pu", "Sha", "Na", "Tha")
            Nakshatra.CHITRA -> listOf("Pe", "Po", "Ra", "Ri")
            Nakshatra.SWATI -> listOf("Ru", "Re", "Ro", "Ta")
            Nakshatra.VISHAKHA -> listOf("Ti", "Tu", "Te", "To")
            Nakshatra.ANURADHA -> listOf("Na", "Ni", "Nu", "Ne")
            Nakshatra.JYESHTHA -> listOf("No", "Ya", "Yi", "Yu")
            Nakshatra.MULA -> listOf("Ye", "Yo", "Ba", "Bi")
            Nakshatra.PURVA_ASHADHA -> listOf("Bu", "Dha", "Bha", "Dha")
            Nakshatra.UTTARA_ASHADHA -> listOf("Be", "Bo", "Ja", "Ji")
            Nakshatra.SHRAVANA -> listOf("Ju", "Je", "Jo", "Gha")
            Nakshatra.DHANISHTHA -> listOf("Ga", "Gi", "Gu", "Ge")
            Nakshatra.SHATABHISHA -> listOf("Go", "Sa", "Si", "Su")
            Nakshatra.PURVA_BHADRAPADA -> listOf("Se", "So", "Da", "Di")
            Nakshatra.UTTARA_BHADRAPADA -> listOf("Du", "Tha", "Jha", "Da")
            Nakshatra.REVATI -> listOf("De", "Do", "Cha", "Chi")
        }
    }

    // ============================================================================
    // REMEDIAL PRESCRIPTION
    // ============================================================================

    /**
     * Generate a complete remedial prescription based on the Ishta Devata analysis.
     */
    private fun generateRemedialPrescription(
        ishtaDevata: DeityInfo,
        atmakaraka: Planet,
        chart: VedicChart
    ): RemedialPrescription {
        val primaryRemedy = buildPrimaryRemedy(ishtaDevata, atmakaraka)
        val gemstone = getGemstoneForPlanet(atmakaraka)
        val charity = getCharityForPlanet(ishtaDevata.determiningPlanet)
        val fasting = getFastingDay(ishtaDevata.determiningPlanet)
        val color = getColorForPlanet(ishtaDevata.determiningPlanet)
        val metal = getMetalForPlanet(atmakaraka)

        // Identify weak planets for additional remedies
        val weakPlanets = chart.planetPositions.filter { pos ->
            pos.planet in Planet.MAIN_PLANETS &&
            (VedicAstrologyUtils.isDebilitated(pos) ||
             VedicAstrologyUtils.isDusthana(pos.house))
        }.map { it.planet }

        val additionalRemedies = weakPlanets.take(3).map { planet ->
            AdditionalRemedy(
                planet = planet,
                gemstone = getGemstoneForPlanet(planet),
                mantra = generatePlanetBeejas()[planet] ?: "",
                charity = getCharityForPlanet(planet),
                color = getColorForPlanet(planet)
            )
        }

        return RemedialPrescription(
            primaryRemedy = primaryRemedy,
            gemstone = gemstone,
            charity = charity,
            fastingDay = fasting,
            auspiciousColor = color,
            metal = metal,
            additionalRemedies = additionalRemedies,
            lifetimeMantraGoal = getLifetimeMantraGoal(atmakaraka),
            dailyPractice = buildDailyPractice(ishtaDevata, atmakaraka)
        )
    }

    /**
     * Build the primary remedy description.
     */
    private fun buildPrimaryRemedy(ishtaDevata: DeityInfo, atmakaraka: Planet): String {
        return buildString {
            append("Worship of ${ishtaDevata.primaryDeity} as your Ishta Devata. ")
            append("The Atmakaraka ${atmakaraka.displayName} indicates that your soul's ")
            append("journey is guided by ${ishtaDevata.primaryDeity}. ")
            append("Regular recitation of the mantra \"${ishtaDevata.deityBeeja}\" ")
            append("is the most effective remedy for overall spiritual progress and ")
            append("mitigation of karmic challenges.")
        }
    }

    /**
     * Build daily practice instructions.
     */
    private fun buildDailyPractice(ishtaDevata: DeityInfo, atmakaraka: Planet): List<String> {
        val hora = getHoraForPlanet(ishtaDevata.determiningPlanet)
        val direction = getDirectionForPlanet(ishtaDevata.determiningPlanet)
        val practices = mutableListOf<String>()

        practices.add("Begin daily practice during ${hora.displayName} Hora, facing $direction")
        practices.add("Recite \"${ishtaDevata.deityBeeja}\" 108 times on a ${getJapaMalaType(ishtaDevata.determiningPlanet)} mala")
        practices.add("Light a ${getLampType(ishtaDevata.determiningPlanet)} lamp before the deity")
        practices.add("Offer ${getOfferingForPlanet(ishtaDevata.determiningPlanet)} to the deity")
        practices.add("Meditate on the form of ${ishtaDevata.primaryDeity} after japa")
        practices.add("On ${getWeekdayForPlanet(ishtaDevata.determiningPlanet)}, perform extended worship with ${getSpecialOffering(ishtaDevata.determiningPlanet)}")

        return practices
    }

    // ============================================================================
    // PLANET-SPECIFIC MAPPING HELPERS
    // ============================================================================

    private fun getRecommendedMantraCount(planet: Planet): Int {
        return when (planet) {
            Planet.SUN -> 7000    // 7000 (Surya number)
            Planet.MOON -> 11000  // 11000 (Chandra number)
            Planet.MARS -> 10000  // 10000
            Planet.MERCURY -> 9000
            Planet.JUPITER -> 19000
            Planet.VENUS -> 16000
            Planet.SATURN -> 23000
            Planet.RAHU -> 18000
            Planet.KETU -> 17000
            else -> 10800 // 100 malas
        }
    }

    private fun getHoraForPlanet(planet: Planet): Planet {
        return when (planet) {
            Planet.RAHU -> Planet.SATURN    // Rahu hora = Saturn hora
            Planet.KETU -> Planet.MARS      // Ketu hora = Mars hora
            else -> planet
        }
    }

    private fun getDirectionForPlanet(planet: Planet): String {
        return when (planet) {
            Planet.SUN -> "East"
            Planet.MOON -> "Northwest"
            Planet.MARS -> "South"
            Planet.MERCURY -> "North"
            Planet.JUPITER -> "Northeast"
            Planet.VENUS -> "Southeast"
            Planet.SATURN -> "West"
            Planet.RAHU -> "Southwest"
            Planet.KETU -> "Northwest"
            else -> "East"
        }
    }

    private fun getWeekdayForPlanet(planet: Planet): String {
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
            else -> "Thursday"
        }
    }

    private fun getGemstoneForPlanet(planet: Planet): String {
        return when (planet) {
            Planet.SUN -> "Ruby (Manikya)"
            Planet.MOON -> "Pearl (Moti)"
            Planet.MARS -> "Red Coral (Moonga)"
            Planet.MERCURY -> "Emerald (Panna)"
            Planet.JUPITER -> "Yellow Sapphire (Pukhraj)"
            Planet.VENUS -> "Diamond (Heera)"
            Planet.SATURN -> "Blue Sapphire (Neelam)"
            Planet.RAHU -> "Hessonite Garnet (Gomed)"
            Planet.KETU -> "Cat's Eye (Lehsunia)"
            else -> "Yellow Sapphire (Pukhraj)"
        }
    }

    private fun getCharityForPlanet(planet: Planet): String {
        return when (planet) {
            Planet.SUN -> "Donate wheat, jaggery, or copper items on Sunday"
            Planet.MOON -> "Donate rice, white cloth, or silver items on Monday"
            Planet.MARS -> "Donate red lentils (masoor), red cloth, or copper on Tuesday"
            Planet.MERCURY -> "Donate green moong dal, green cloth, or books on Wednesday"
            Planet.JUPITER -> "Donate yellow clothes, turmeric, or gold on Thursday"
            Planet.VENUS -> "Donate white rice, sugar, white clothes, or perfume on Friday"
            Planet.SATURN -> "Donate black sesame, mustard oil, iron items on Saturday"
            Planet.RAHU -> "Donate coconut, blankets, or blue/black items on Saturday"
            Planet.KETU -> "Donate seven grains, blanket, or sesame on Tuesday"
            else -> "Donate to charity on Thursday"
        }
    }

    private fun getFastingDay(planet: Planet): String {
        return when (planet) {
            Planet.SUN -> "Sunday (Surya Vrata)"
            Planet.MOON -> "Monday (Soma Vrata)"
            Planet.MARS -> "Tuesday (Mangala Vrata)"
            Planet.MERCURY -> "Wednesday (Budha Vrata)"
            Planet.JUPITER -> "Thursday (Guru Vrata / Brihaspati Vrata)"
            Planet.VENUS -> "Friday (Shukra Vrata / Santoshi Maa Vrata)"
            Planet.SATURN -> "Saturday (Shani Vrata)"
            Planet.RAHU -> "Saturday (with Durga worship)"
            Planet.KETU -> "Tuesday (with Ganesha worship)"
            else -> "Thursday (Guru Vrata)"
        }
    }

    private fun getColorForPlanet(planet: Planet): String {
        return when (planet) {
            Planet.SUN -> "Red, Orange, Copper"
            Planet.MOON -> "White, Silver, Cream"
            Planet.MARS -> "Red, Maroon, Coral"
            Planet.MERCURY -> "Green, Emerald Green"
            Planet.JUPITER -> "Yellow, Gold, Saffron"
            Planet.VENUS -> "White, Light Blue, Pastel"
            Planet.SATURN -> "Black, Dark Blue, Navy"
            Planet.RAHU -> "Dark Blue, Smoky Grey"
            Planet.KETU -> "Multi-colored, Grey, Brown"
            else -> "Yellow, Gold"
        }
    }

    private fun getMetalForPlanet(planet: Planet): String {
        return when (planet) {
            Planet.SUN -> "Gold (Suvarna)"
            Planet.MOON -> "Silver (Rajata)"
            Planet.MARS -> "Copper (Tamra)"
            Planet.MERCURY -> "Bronze (Kamsya) or Mixed Metals"
            Planet.JUPITER -> "Gold (Suvarna)"
            Planet.VENUS -> "Silver (Rajata) or Platinum"
            Planet.SATURN -> "Iron (Loha)"
            Planet.RAHU -> "Lead (Seesa) or Mixed Alloy"
            Planet.KETU -> "Iron (Loha) or Brass"
            else -> "Gold (Suvarna)"
        }
    }

    private fun getLifetimeMantraGoal(planet: Planet): Long {
        return when (planet) {
            Planet.SUN -> 70000L
            Planet.MOON -> 110000L
            Planet.MARS -> 100000L
            Planet.MERCURY -> 90000L
            Planet.JUPITER -> 190000L
            Planet.VENUS -> 160000L
            Planet.SATURN -> 230000L
            Planet.RAHU -> 180000L
            Planet.KETU -> 170000L
            else -> 125000L
        }
    }

    private fun getJapaMalaType(planet: Planet): String {
        return when (planet) {
            Planet.SUN -> "Rudraksha"
            Planet.MOON -> "Crystal (Sphatik)"
            Planet.MARS -> "Red Coral or Rudraksha"
            Planet.MERCURY -> "Emerald or Tulasi"
            Planet.JUPITER -> "Tulasi or Rudraksha"
            Planet.VENUS -> "Crystal (Sphatik) or Lotus seed"
            Planet.SATURN -> "Iron or Rudraksha (dark)"
            Planet.RAHU -> "Rudraksha (8-faced)"
            Planet.KETU -> "Rudraksha (9-faced)"
            else -> "Rudraksha"
        }
    }

    private fun getLampType(planet: Planet): String {
        return when (planet) {
            Planet.SUN -> "Ghee (clarified butter)"
            Planet.MOON -> "Ghee"
            Planet.MARS -> "Sesame oil"
            Planet.MERCURY -> "Ghee"
            Planet.JUPITER -> "Ghee"
            Planet.VENUS -> "Ghee or Camphor"
            Planet.SATURN -> "Sesame oil or Mustard oil"
            Planet.RAHU -> "Mustard oil"
            Planet.KETU -> "Sesame oil"
            else -> "Ghee"
        }
    }

    private fun getOfferingForPlanet(planet: Planet): String {
        return when (planet) {
            Planet.SUN -> "red flowers, wheat, and jaggery"
            Planet.MOON -> "white flowers, rice, and milk"
            Planet.MARS -> "red flowers, red lentils, and jaggery"
            Planet.MERCURY -> "green flowers, moong dal, and green vegetables"
            Planet.JUPITER -> "yellow flowers, banana, and chana dal"
            Planet.VENUS -> "white flowers, rice kheer, and fruits"
            Planet.SATURN -> "dark flowers, black sesame, and mustard oil"
            Planet.RAHU -> "coconut, blue flowers, and durva grass"
            Planet.KETU -> "seven grains, kusha grass, and bananas"
            else -> "flowers and fruits"
        }
    }

    private fun getSpecialOffering(planet: Planet): String {
        return when (planet) {
            Planet.SUN -> "Arghya (water offering) to the rising Sun"
            Planet.MOON -> "Abhisheka with milk and water"
            Planet.MARS -> "Sindoor (vermillion) tilak and red cloth offering"
            Planet.MERCURY -> "Tulasi leaves and green cloth"
            Planet.JUPITER -> "Turmeric, banana, and yellow cloth"
            Planet.VENUS -> "White sandalwood paste, camphor, and white flowers"
            Planet.SATURN -> "Sesame oil lamp and iron donation"
            Planet.RAHU -> "Coconut with money wrapped in blue cloth"
            Planet.KETU -> "Seven-colored flag or blanket donation"
            else -> "special abhisheka"
        }
    }

    private fun getAfflictionNotes(planet: Planet, afflictions: List<String>): String {
        val severity = when {
            afflictions.size >= 3 -> "severely"
            afflictions.size == 2 -> "moderately"
            else -> "mildly"
        }
        return "${planet.displayName} is $severity afflicted. " +
            "Regular recitation of its Beeja mantra combined with " +
            "${getCharityForPlanet(planet).lowercase()} will help mitigate the adverse effects. " +
            "Wearing ${getGemstoneForPlanet(planet)} after proper consultation is also recommended."
    }

    // ============================================================================
    // INTERPRETATION & DESCRIPTION BUILDERS
    // ============================================================================

    private fun buildDeityMantra(deity: String, beeja: String): String {
        return beeja
    }

    private fun buildDeityDescription(deity: String, planet: Planet, context: DeityContext): String {
        val contextLabel = when (context) {
            DeityContext.ISHTA_DEVATA -> "Ishta Devata (Chosen Personal Deity)"
            DeityContext.DHARMA_DEVATA -> "Dharma Devata (Deity of Righteous Duty)"
            DeityContext.PALANA_DEVATA -> "Palana Devata (Deity of Sustenance and Protection)"
        }
        return "$deity as $contextLabel, indicated by ${planet.displayName}. " +
            when (context) {
                DeityContext.ISHTA_DEVATA ->
                    "This is the deity whose worship leads to moksha (liberation) for the native. " +
                    "The Ishta Devata is determined from the 12th house of the Karakamsha (Navamsa of Atmakaraka), " +
                    "representing the final destination of the soul."
                DeityContext.DHARMA_DEVATA ->
                    "This is the deity who guides the native on the path of dharma (righteous living). " +
                    "Determined from the 5th house of the Karakamsha, representing purva punya (past merit) " +
                    "and the dharmic direction of life."
                DeityContext.PALANA_DEVATA ->
                    "This is the deity who provides sustenance, protection, and removes obstacles. " +
                    "Determined from the 6th house of the Karakamsha, representing the force that " +
                    "overcomes enemies and diseases."
            }
    }

    private fun buildSpecialInstructions(atmakaraka: Planet, ishtaDevata: DeityInfo): List<String> {
        val instructions = mutableListOf<String>()

        instructions.add(
            "The Atmakaraka is ${atmakaraka.displayName}. " +
            "This planet represents your soul's deepest desires and karmic lessons in this life."
        )

        instructions.add(
            "Your Ishta Devata is ${ishtaDevata.primaryDeity}. " +
            "Surrendering to this deity accelerates spiritual progress."
        )

        instructions.add(
            "Begin your sadhana on a ${getWeekdayForPlanet(ishtaDevata.determiningPlanet)} " +
            "during the ${getHoraForPlanet(ishtaDevata.determiningPlanet).displayName} Hora."
        )

        instructions.add(
            "Use a ${getJapaMalaType(ishtaDevata.determiningPlanet)} mala for japa. " +
            "Face ${getDirectionForPlanet(ishtaDevata.determiningPlanet)} while chanting."
        )

        if (ishtaDevata.alternateDeities.isNotEmpty()) {
            instructions.add(
                "Alternate forms of your Ishta Devata include: " +
                ishtaDevata.alternateDeities.joinToString(", ") +
                ". You may feel drawn to any of these forms."
            )
        }

        return instructions
    }

    // ============================================================================
    // UTILITY HELPERS
    // ============================================================================

    /**
     * Rank dignity for comparison (higher = stronger).
     */
    private fun dignityRank(pos: PlanetPosition): Int {
        return when {
            VedicAstrologyUtils.isExalted(pos) -> 7
            VedicAstrologyUtils.isInMoolatrikona(pos) -> 6
            VedicAstrologyUtils.isInOwnSign(pos) -> 5
            VedicAstrologyUtils.isInFriendSign(pos) -> 4
            VedicAstrologyUtils.isInEnemySign(pos) -> 2
            VedicAstrologyUtils.isDebilitated(pos) -> 1
            else -> 3 // neutral
        }
    }

    /**
     * Map JaiminiKarakaCalculator's dignity to our local dignity string.
     */
    private fun mapDignity(dignity: VedicAstrologyUtils.PlanetaryDignity): String {
        return when (dignity) {
            VedicAstrologyUtils.PlanetaryDignity.EXALTED -> "Exalted"
            VedicAstrologyUtils.PlanetaryDignity.MOOLATRIKONA -> "Moolatrikona"
            VedicAstrologyUtils.PlanetaryDignity.OWN_SIGN -> "Own Sign"
            VedicAstrologyUtils.PlanetaryDignity.FRIEND_SIGN -> "Friend's Sign"
            VedicAstrologyUtils.PlanetaryDignity.NEUTRAL_SIGN -> "Neutral Sign"
            VedicAstrologyUtils.PlanetaryDignity.ENEMY_SIGN -> "Enemy's Sign"
            VedicAstrologyUtils.PlanetaryDignity.DEBILITATED -> "Debilitated"
        }
    }

    private fun ordinal(n: Int): String {
        val suffix = VedicAstrologyUtils.getOrdinalSuffix(n)
        return "$n$suffix"
    }

    /**
     * Context for deity determination: which house from Karakamsha.
     */
    private enum class DeityContext {
        ISHTA_DEVATA,
        DHARMA_DEVATA,
        PALANA_DEVATA
    }
}

// ============================================================================
// PUBLIC DATA CLASSES
// ============================================================================

/**
 * Complete Ishta Devata analysis result.
 */
data class IshtaDevataAnalysis(
    val atmakaraka: AtmakarakaInfo,
    val karakamsha: KarakamshaInfo,
    val ishtaDevata: DeityInfo,
    val dharmaDevata: DeityInfo,
    val palanaDevata: DeityInfo,
    val personalMantras: PersonalMantras,
    val nakshatraSyllable: NakshatraSyllable,
    val remedialPrescription: RemedialPrescription
)

/**
 * Atmakaraka (Soul Significator) information.
 */
data class AtmakarakaInfo(
    val planet: Planet,
    val degreeInSign: Double,
    val rashi: ZodiacSign,
    val house: Int,
    val isRetrograde: Boolean,
    val dignity: String
)

/**
 * Karakamsha (Navamsa sign of Atmakaraka) information.
 */
data class KarakamshaInfo(
    val sign: ZodiacSign,
    val lord: Planet,
    val atmakarakaNavamsaDegree: Double,
    val planetsInKarakamsha: List<Planet>
)

/**
 * Deity determination result.
 */
data class DeityInfo(
    val primaryDeity: String,
    val alternateDeities: List<String>,
    val determiningPlanet: Planet,
    val houseFromKarakamsha: Int,
    val sign: ZodiacSign,
    val determinationMethod: String,
    val deityBeeja: String,
    val deityMantra: String,
    val description: String
)

/**
 * Nakshatra pada syllable information.
 */
data class NakshatraSyllable(
    val nakshatra: Nakshatra,
    val pada: Int,
    val primarySyllable: String,
    val allPadaSyllables: List<String>,
    val associatedLetter: String
)

/**
 * Personalized mantra collection.
 */
data class PersonalMantras(
    val ishtaDevataMantra: String,
    val atmakarakaMantra: String,
    val nakshatraMantra: String,
    val compositeBeeja: String,
    val afflictedPlanetMantras: Map<Planet, AfflictedPlanetMantra>,
    val allPlanetBeejas: Map<Planet, String>,
    val recommendedCount: Int,
    val recommendedTiming: String,
    val recommendedDirection: String,
    val specialInstructions: List<String>
)

/**
 * Mantra prescription for an afflicted planet.
 */
data class AfflictedPlanetMantra(
    val planet: Planet,
    val mantra: String,
    val afflictions: List<String>,
    val recommendedCount: Int,
    val bestDay: String,
    val specialNotes: String
)

/**
 * Complete remedial prescription.
 */
data class RemedialPrescription(
    val primaryRemedy: String,
    val gemstone: String,
    val charity: String,
    val fastingDay: String,
    val auspiciousColor: String,
    val metal: String,
    val additionalRemedies: List<AdditionalRemedy>,
    val lifetimeMantraGoal: Long,
    val dailyPractice: List<String>
)

/**
 * Additional remedy for a weak planet.
 */
data class AdditionalRemedy(
    val planet: Planet,
    val gemstone: String,
    val mantra: String,
    val charity: String,
    val color: String
)
