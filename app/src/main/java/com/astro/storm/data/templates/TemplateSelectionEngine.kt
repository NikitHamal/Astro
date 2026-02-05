package com.astro.storm.data.templates

import com.astro.storm.core.common.Language
import com.astro.storm.core.model.Nakshatra
import com.astro.storm.core.model.Planet
import com.astro.storm.core.model.ZodiacSign

/**
 * Template Selection Engine
 *
 * Provides intelligent template selection based on:
 * - Degree-based matching
 * - Planet positions
 * - House lord placements
 * - Nakshatra and pada positions
 * - Dasha periods
 * - Transit configurations
 *
 * This engine integrates all template categories:
 * - Dasha Templates
 * - Transit Templates
 * - Yoga Templates
 * - House Lord Templates
 * - Divisional Chart Templates
 * - Nadi Templates
 * - Life Area Templates
 */
object TemplateSelectionEngine {

    // ==================== DEGREE-BASED CALCULATIONS ====================

    /**
     * Calculate zodiac sign from longitude
     */
    fun getSignFromLongitude(longitude: Double): ZodiacSign {
        val normalizedLong = ((longitude % 360) + 360) % 360
        val signIndex = (normalizedLong / 30).toInt()
        return ZodiacSign.values()[signIndex.coerceIn(0, 11)]
    }

    /**
     * Calculate Nakshatra from longitude
     */
    fun getNakshatraFromLongitude(longitude: Double): Pair<Nakshatra, Int> {
        return Nakshatra.fromLongitude(longitude)
    }

    /**
     * Calculate Navamsa sign from longitude
     */
    fun getNavamsaSignFromLongitude(longitude: Double): ZodiacSign {
        val normalizedLong = ((longitude % 360) + 360) % 360
        // Each navamsa = 3°20' = 3.333... degrees
        val navamsaIndex = ((normalizedLong % 30) / (30.0 / 9)).toInt()
        val rashiIndex = (normalizedLong / 30).toInt()

        // Calculate starting navamsa based on element of rashi
        val startingNavamsa = when (rashiIndex % 4) {
            0 -> 0  // Fire signs start from Aries
            1 -> 9  // Earth signs start from Capricorn
            2 -> 5  // Air signs start from Libra
            3 -> 4  // Water signs start from Cancer
            else -> 0
        }

        val finalIndex = (startingNavamsa + navamsaIndex) % 12
        return ZodiacSign.values()[finalIndex]
    }

    /**
     * Calculate Dashamsa sign from longitude
     */
    fun getDashamsaSignFromLongitude(longitude: Double): ZodiacSign {
        val normalizedLong = ((longitude % 360) + 360) % 360
        // Each dashamsa = 3 degrees
        val dashamsaIndex = ((normalizedLong % 30) / 3).toInt()
        val rashiIndex = (normalizedLong / 30).toInt()

        // Odd signs count from same sign, even signs count from 9th
        val startingSign = if (rashiIndex % 2 == 0) {
            rashiIndex  // Odd signs (0-indexed even)
        } else {
            (rashiIndex + 8) % 12  // Even signs start from 9th
        }

        val finalIndex = (startingSign + dashamsaIndex) % 12
        return ZodiacSign.values()[finalIndex]
    }

    /**
     * Calculate Nadi number from Moon longitude (D-150)
     */
    fun getNadiFromMoonLongitude(moonLongitude: Double): Int {
        val normalizedLong = ((moonLongitude % 360) + 360) % 360
        // Each Nadi = 12 arc minutes = 0.2 degrees
        // 150 Nadis in 30 degrees (one sign)
        val positionInSign = normalizedLong % 30
        val nadiIndex = (positionInSign / 0.2).toInt() + 1
        return nadiIndex.coerceIn(1, 150)
    }

    /**
     * Check if planet is Vargottama (same sign in Rashi and Navamsa)
     */
    fun isVargottama(longitude: Double): Boolean {
        val rashiSign = getSignFromLongitude(longitude)
        val navamsaSign = getNavamsaSignFromLongitude(longitude)
        return rashiSign == navamsaSign
    }

    /**
     * Check degree range match
     */
    fun isInDegreeRange(longitude: Double, range: DegreeRange): Boolean {
        val normalizedLong = ((longitude % 360) + 360) % 360
        return normalizedLong >= range.start && normalizedLong < range.end
    }

    // ==================== DASHA TEMPLATE SELECTION ====================

    /**
     * Get Mahadasha interpretation template
     */
    fun getMahadashaTemplate(planet: Planet, language: Language): String {
        val template = DashaTemplates.getMahadashaTemplate(planet)
        return template?.getText(language) ?: ""
    }

    /**
     * Get Mahadasha by sign template
     */
    fun getMahadashaBySignTemplate(planet: Planet, sign: ZodiacSign, language: Language): String {
        val template = DashaTemplates.getMahadashaBySignTemplate(planet, sign)
        return template?.getText(language) ?: ""
    }

    /**
     * Get Antardasha template
     */
    fun getAntardashaTemplate(mahadasha: Planet, antardasha: Planet, language: Language): String {
        val template = DashaTemplates.getAntardashaTemplate(mahadasha, antardasha)
        return template?.getText(language) ?: ""
    }

    // ==================== TRANSIT TEMPLATE SELECTION ====================

    /**
     * Get transit from Moon template
     */
    fun getTransitFromMoonTemplate(planet: Planet, houseFromMoon: Int, language: Language): String {
        val template = TransitTemplates.getTransitFromMoonTemplate(planet, houseFromMoon)
        return template?.getText(language) ?: ""
    }

    /**
     * Get Sade Sati template
     */
    fun getSadeSatiTemplate(phase: Int, language: Language): String {
        val template = TransitTemplates.getSadeSatiTemplate(phase)
        return template?.getText(language) ?: ""
    }

    /**
     * Get transit aspect template
     */
    fun getTransitAspectTemplate(
        transitPlanet: Planet,
        natalPlanet: Planet,
        aspectType: String,
        language: Language
    ): String {
        val template = TransitTemplates.getTransitAspectTemplate(transitPlanet, natalPlanet, aspectType)
        return template?.getText(language) ?: ""
    }

    // ==================== YOGA TEMPLATE SELECTION ====================

    /**
     * Get Pancha Mahapurusha Yoga template
     */
    fun getPanchaMahapurushaTemplate(yogaName: String, language: Language): String {
        val template = YogaTemplates.getPanchaMahapurushaTemplate(yogaName)
        return template?.getText(language) ?: ""
    }

    /**
     * Get Raja Yoga template
     */
    fun getRajaYogaTemplate(yogaName: String, language: Language): String {
        val template = YogaTemplates.getRajaYogaTemplate(yogaName)
        return template?.getText(language) ?: ""
    }

    /**
     * Get Dhana Yoga template
     */
    fun getDhanaYogaTemplate(yogaName: String, language: Language): String {
        val template = YogaTemplates.getDhanaYogaTemplate(yogaName)
        return template?.getText(language) ?: ""
    }

    // ==================== HOUSE LORD TEMPLATE SELECTION ====================

    /**
     * Get house lord template
     */
    fun getHouseLordTemplate(houseLord: Int, placedIn: Int, language: Language): String {
        val template = HouseLordTemplates.getHouseLordTemplate(houseLord, placedIn)
        return template?.getText(language) ?: ""
    }

    // ==================== DIVISIONAL CHART TEMPLATE SELECTION ====================

    /**
     * Get Navamsa Lagna template
     */
    fun getNavamsaLagnaTemplate(sign: ZodiacSign, language: Language): String {
        val template = DivisionalChartTemplates.getNavamsaLagnaTemplate(sign)
        return template?.getText(language) ?: ""
    }

    /**
     * Get planet in Navamsa template
     */
    fun getPlanetInNavamsaTemplate(planet: Planet, sign: ZodiacSign, language: Language): String {
        val template = DivisionalChartTemplates.getPlanetInNavamsaTemplate(planet, sign)
        return template?.getText(language) ?: ""
    }

    /**
     * Get Vargottama template
     */
    fun getVargottamaTemplate(planet: Planet, language: Language): String {
        val template = DivisionalChartTemplates.getVargottamaTemplate(planet)
        return template?.getText(language) ?: ""
    }

    /**
     * Get Dashamsa Lagna template
     */
    fun getDashamsaLagnaTemplate(sign: ZodiacSign, language: Language): String {
        val template = DivisionalChartTemplates.getDashamsaLagnaTemplate(sign)
        return template?.getText(language) ?: ""
    }

    // ==================== NADI TEMPLATE SELECTION ====================

    /**
     * Get Nadi prediction template
     */
    fun getNadiPredictionTemplate(
        moonLongitude: Double,
        ascendantSign: ZodiacSign,
        language: Language
    ): String {
        val nadiNumber = getNadiFromMoonLongitude(moonLongitude)
        val template = NadiTemplates.getNadiPredictionByAscendant(nadiNumber, ascendantSign)
        return template?.getText(language) ?: ""
    }

    /**
     * Get Nadi info
     */
    fun getNadiInfo(moonLongitude: Double): NadiTemplates.NadiEntry? {
        val nadiNumber = getNadiFromMoonLongitude(moonLongitude)
        return NadiTemplates.getNadiInfo(nadiNumber)
    }

    // ==================== LIFE AREA TEMPLATE SELECTION ====================

    /**
     * Get career template by ascendant
     */
    fun getCareerTemplateByAscendant(ascendant: ZodiacSign, language: Language): String {
        val template = LifeAreaTemplates.getCareerByAscendant(ascendant)
        return template?.getText(language) ?: ""
    }

    /**
     * Get career template by 10th lord placement
     */
    fun getCareerTemplateByTenthLord(house: Int, language: Language): String {
        val template = LifeAreaTemplates.getCareerByTenthLord(house)
        return template?.getText(language) ?: ""
    }

    /**
     * Get relationship template by 7th lord placement
     */
    fun getRelationshipTemplateBySeventhLord(house: Int, language: Language): String {
        val template = LifeAreaTemplates.getRelationshipBySeventhLord(house)
        return template?.getText(language) ?: ""
    }

    /**
     * Get Venus relationship template
     */
    fun getVenusRelationshipTemplate(venusSign: ZodiacSign, language: Language): String {
        val template = LifeAreaTemplates.getVenusRelationshipTemplate(venusSign)
        return template?.getText(language) ?: ""
    }

    /**
     * Get health template by ascendant
     */
    fun getHealthTemplateByAscendant(ascendant: ZodiacSign, language: Language): String {
        val template = LifeAreaTemplates.getHealthByAscendant(ascendant)
        return template?.getText(language) ?: ""
    }

    /**
     * Get wealth template by 2nd lord placement
     */
    fun getWealthTemplateBySecondLord(house: Int, language: Language): String {
        val template = LifeAreaTemplates.getWealthBySecondLord(house)
        return template?.getText(language) ?: ""
    }

    /**
     * Get spiritual template by 9th lord placement
     */
    fun getSpiritualTemplateByNinthLord(house: Int, language: Language): String {
        val template = LifeAreaTemplates.getSpiritualByNinthLord(house)
        return template?.getText(language) ?: ""
    }

    // ==================== COMPOSITE PREDICTION GENERATION ====================

    /**
     * Generate comprehensive Dasha prediction
     */
    fun generateDashaPrediction(
        mahadashaPlanet: Planet,
        antardashaPlanet: Planet,
        mahadashaPlanetSign: ZodiacSign,
        language: Language
    ): String {
        val mahadashaGeneral = getMahadashaTemplate(mahadashaPlanet, language)
        val mahadashaBySign = getMahadashaBySignTemplate(mahadashaPlanet, mahadashaPlanetSign, language)
        val antardasha = getAntardashaTemplate(mahadashaPlanet, antardashaPlanet, language)

        return buildString {
            if (mahadashaGeneral.isNotEmpty()) {
                appendLine(mahadashaGeneral)
                appendLine()
            }
            if (mahadashaBySign.isNotEmpty()) {
                appendLine(mahadashaBySign)
                appendLine()
            }
            if (antardasha.isNotEmpty()) {
                appendLine(antardasha)
            }
        }.trim()
    }

    /**
     * Generate comprehensive transit prediction
     */
    fun generateTransitPrediction(
        transitPlanet: Planet,
        moonSign: ZodiacSign,
        transitSign: ZodiacSign,
        language: Language
    ): String {
        val houseFromMoon = calculateHouseFromMoon(moonSign, transitSign)
        val transitTemplate = getTransitFromMoonTemplate(transitPlanet, houseFromMoon, language)

        val sadeSatiTemplate = if (transitPlanet == Planet.SATURN) {
            val phase = calculateSadeSatiPhase(moonSign, transitSign)
            if (phase in 1..3) getSadeSatiTemplate(phase, language) else ""
        } else ""

        return buildString {
            if (transitTemplate.isNotEmpty()) {
                appendLine(transitTemplate)
            }
            if (sadeSatiTemplate.isNotEmpty()) {
                appendLine()
                appendLine(sadeSatiTemplate)
            }
        }.trim()
    }

    /**
     * Generate comprehensive Navamsa prediction
     */
    fun generateNavamsaPrediction(
        ascendantLongitude: Double,
        planetPositions: Map<Planet, Double>,
        language: Language
    ): String {
        val navamsaLagna = getNavamsaSignFromLongitude(ascendantLongitude)
        val navamsaLagnaTemplate = getNavamsaLagnaTemplate(navamsaLagna, language)

        val planetTemplates = mutableListOf<String>()

        for ((planet, longitude) in planetPositions) {
            val navamsaSign = getNavamsaSignFromLongitude(longitude)
            val template = getPlanetInNavamsaTemplate(planet, navamsaSign, language)
            if (template.isNotEmpty()) {
                planetTemplates.add(template)
            }

            // Check for Vargottama
            if (isVargottama(longitude)) {
                val vargottamaTemplate = getVargottamaTemplate(planet, language)
                if (vargottamaTemplate.isNotEmpty()) {
                    planetTemplates.add(vargottamaTemplate)
                }
            }
        }

        return buildString {
            if (navamsaLagnaTemplate.isNotEmpty()) {
                appendLine("=== ${if (language == Language.NEPALI) "नवांश लग्न" else "Navamsa Lagna"} ===")
                appendLine(navamsaLagnaTemplate)
                appendLine()
            }
            planetTemplates.forEach { template ->
                appendLine(template)
                appendLine()
            }
        }.trim()
    }

    /**
     * Generate comprehensive life area prediction
     */
    fun generateLifeAreaPrediction(
        ascendant: ZodiacSign,
        tenthLordHouse: Int,
        seventhLordHouse: Int,
        secondLordHouse: Int,
        venusSign: ZodiacSign,
        language: Language
    ): String {
        val careerByAscendant = getCareerTemplateByAscendant(ascendant, language)
        val careerByTenthLord = getCareerTemplateByTenthLord(tenthLordHouse, language)
        val relationship = getRelationshipTemplateBySeventhLord(seventhLordHouse, language)
        val venusRelationship = getVenusRelationshipTemplate(venusSign, language)
        val health = getHealthTemplateByAscendant(ascendant, language)
        val wealth = getWealthTemplateBySecondLord(secondLordHouse, language)

        return buildString {
            if (careerByAscendant.isNotEmpty()) {
                appendLine("=== ${if (language == Language.NEPALI) "क्यारियर" else "Career"} ===")
                appendLine(careerByAscendant)
                appendLine()
            }
            if (careerByTenthLord.isNotEmpty()) {
                appendLine(careerByTenthLord)
                appendLine()
            }
            if (relationship.isNotEmpty()) {
                appendLine("=== ${if (language == Language.NEPALI) "सम्बन्ध" else "Relationships"} ===")
                appendLine(relationship)
                appendLine()
            }
            if (venusRelationship.isNotEmpty()) {
                appendLine(venusRelationship)
                appendLine()
            }
            if (health.isNotEmpty()) {
                appendLine("=== ${if (language == Language.NEPALI) "स्वास्थ्य" else "Health"} ===")
                appendLine(health)
                appendLine()
            }
            if (wealth.isNotEmpty()) {
                appendLine("=== ${if (language == Language.NEPALI) "सम्पत्ति" else "Wealth"} ===")
                appendLine(wealth)
            }
        }.trim()
    }

    // ==================== HELPER CALCULATIONS ====================

    /**
     * Calculate house from Moon sign
     */
    private fun calculateHouseFromMoon(moonSign: ZodiacSign, transitSign: ZodiacSign): Int {
        val moonIndex = moonSign.ordinal
        val transitIndex = transitSign.ordinal
        return ((transitIndex - moonIndex + 12) % 12) + 1
    }

    /**
     * Calculate Sade Sati phase
     */
    private fun calculateSadeSatiPhase(moonSign: ZodiacSign, saturnSign: ZodiacSign): Int {
        val moonIndex = moonSign.ordinal
        val saturnIndex = saturnSign.ordinal
        val diff = ((saturnIndex - moonIndex + 12) % 12)

        return when (diff) {
            11 -> 1  // First phase (12th from Moon)
            0 -> 2   // Second phase (on Moon)
            1 -> 3   // Third phase (2nd from Moon)
            else -> 0 // Not in Sade Sati
        }
    }

    // ==================== TEMPLATE STATISTICS ====================

    /**
     * Get total template count across all categories
     */
    fun getTotalTemplateCount(): Int {
        return DashaTemplates.getTotalTemplateCount() +
                TransitTemplates.getTotalTemplateCount() +
                YogaTemplates.getTotalTemplateCount() +
                HouseLordTemplates.getTotalTemplateCount() +
                DivisionalChartTemplates.getTotalTemplateCount() +
                NadiTemplates.getTotalTemplateCount() +
                LifeAreaTemplates.getTotalTemplateCount()
    }

    /**
     * Get template count by category
     */
    fun getTemplateCategoryBreakdown(): Map<String, Int> {
        return mapOf(
            "Dasha Templates" to DashaTemplates.getTotalTemplateCount(),
            "Transit Templates" to TransitTemplates.getTotalTemplateCount(),
            "Yoga Templates" to YogaTemplates.getTotalTemplateCount(),
            "House Lord Templates" to HouseLordTemplates.getTotalTemplateCount(),
            "Divisional Chart Templates" to DivisionalChartTemplates.getTotalTemplateCount(),
            "Nadi Templates" to NadiTemplates.getTotalTemplateCount(),
            "Life Area Templates" to LifeAreaTemplates.getTotalTemplateCount()
        )
    }
}

/**
 * Extension function to get text based on language
 */
fun LocalizedTemplate.getText(language: Language): String {
    return when (language) {
        Language.ENGLISH -> en
        Language.NEPALI -> ne
    }
}
