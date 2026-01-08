package com.astro.storm.ephemeris

import com.astro.storm.data.model.Planet
import com.astro.storm.data.model.PlanetPosition
import com.astro.storm.data.model.VedicChart
import com.astro.storm.data.model.ZodiacSign
import com.astro.storm.data.model.Nakshatra

/**
 * SahamCalculator - Comprehensive Arabic Parts (Lots) Analysis
 *
 * Saham (Arabic Parts/Lots) are sensitive points calculated from three chart factors:
 * typically Ascendant + Planet1 - Planet2. Each Saham represents a specific life area.
 *
 * Key Features:
 * - 20 Major Sahams covering all life areas
 * - Day/Night birth formula reversal (per Tajika tradition)
 * - House placement and lord analysis
 * - Nakshatra position for fine-tuned timing
 * - Strength assessment based on lord dignity
 *
 * Vedic References:
 * - Tajika Neelakanthi
 * - Prashna Marga (Horary applications)
 * - Varshaphala traditions
 */
object SahamCalculator {

    private const val DEGREES_PER_CIRCLE = 360.0
    private const val DEGREES_PER_SIGN = 30.0

    /**
     * Saham Type enumeration with formulas
     */
    enum class SahamType(
        val displayName: String,
        val nepaliName: String,
        val description: String,
        val category: SahamCategory
    ) {
        PUNYA("Punya Saham", "पुण्य सहम", "Fortune/Merit point", SahamCategory.MAJOR),
        VIDYA("Vidya Saham", "विद्या सहम", "Education/Knowledge", SahamCategory.MAJOR),
        YASHAS("Yashas Saham", "यश सहम", "Fame/Reputation", SahamCategory.MAJOR),
        MITRA("Mitra Saham", "मित्र सहम", "Friends/Allies", SahamCategory.MAJOR),
        DHANA("Dhana Saham", "धन सहम", "Wealth", SahamCategory.MAJOR),
        KARMA("Karma Saham", "कर्म सहम", "Career/Action", SahamCategory.MAJOR),
        VIVAHA("Vivaha Saham", "विवाह सहम", "Marriage", SahamCategory.MAJOR),
        PUTRA("Putra Saham", "पुत्र सहम", "Children", SahamCategory.MAJOR),
        PITRI("Pitri Saham", "पितृ सहम", "Father", SahamCategory.MINOR),
        MATRI("Matri Saham", "मातृ सहम", "Mother", SahamCategory.MINOR),
        BHRATRI("Bhratri Saham", "भ्रातृ सहम", "Siblings", SahamCategory.MINOR),
        SAMARTHA("Samartha Saham", "समर्थ सहम", "Capability/Skill", SahamCategory.MINOR),
        ASHA("Asha Saham", "आशा सहम", "Hopes/Wishes", SahamCategory.MINOR),
        MAHATMYA("Mahatmya Saham", "महात्म्य सहम", "Greatness/Spirituality", SahamCategory.MAJOR),
        ROGA("Roga Saham", "रोग सहम", "Disease", SahamCategory.MINOR),
        MRITYU("Mrityu Saham", "मृत्यु सहम", "Death/Longevity", SahamCategory.MINOR),
        RAJA("Raja Saham", "राज सहम", "Authority/Power", SahamCategory.MAJOR),
        PARADESA("Paradesa Saham", "परदेश सहम", "Foreign/Travel", SahamCategory.MINOR),
        BANDHU("Bandhu Saham", "बन्धु सहम", "Relatives", SahamCategory.MINOR),
        GAURAVA("Gaurava Saham", "गौरव सहम", "Honor/Respect", SahamCategory.MAJOR)
    }

    enum class SahamCategory(val displayName: String) {
        MAJOR("Major Sahams"),
        MINOR("Minor Sahams"),
        PRASHNA("Prashna Sahams")
    }

    enum class SahamStrength(val displayName: String, val nepaliName: String) {
        EXCELLENT("Excellent", "उत्कृष्ट"),
        STRONG("Strong", "बलवान"),
        MODERATE("Moderate", "मध्यम"),
        WEAK("Weak", "कमजोर"),
        AFFLICTED("Afflicted", "पीडित")
    }

    /**
     * Individual Saham result
     */
    data class SahamResult(
        val type: SahamType,
        val longitude: Double,
        val sign: ZodiacSign,
        val degreeInSign: Double,
        val house: Int,
        val nakshatra: Nakshatra,
        val nakshatraPada: Int,
        val lord: Planet,
        val lordPosition: PlanetPosition?,
        val formula: String,
        val strength: SahamStrength,
        val isActivated: Boolean,
        val interpretation: String
    )

    /**
     * Category summary
     */
    data class CategorySummary(
        val category: SahamCategory,
        val count: Int,
        val strongCount: Int,
        val weakCount: Int,
        val avgStrength: Double
    )

    /**
     * Complete Saham analysis
     */
    data class SahamAnalysis(
        val sahams: List<SahamResult>,
        val majorSahams: List<SahamResult>,
        val minorSahams: List<SahamResult>,
        val categorySummary: List<CategorySummary>,
        val strongestSaham: SahamResult?,
        val weakestSaham: SahamResult?,
        val overallScore: Double,
        val isDayBirth: Boolean,
        val keyInsights: List<String>,
        val recommendations: List<String>
    )

    /**
     * Perform complete Saham analysis for a natal chart
     */
    fun analyzeSahams(chart: VedicChart): SahamAnalysis {
        val isDayBirth = isDayBirth(chart)
        val planetMap = chart.planetPositions.associateBy { it.planet }
        val ascendantLong = chart.ascendant

        val sahams = mutableListOf<SahamResult>()

        // Calculate all Sahams
        SahamType.entries.forEach { type ->
            try {
                val result = calculateSaham(type, isDayBirth, planetMap, ascendantLong, chart)
                result?.let { sahams.add(it) }
            } catch (_: Exception) { }
        }

        val majorSahams = sahams.filter { it.type.category == SahamCategory.MAJOR }
        val minorSahams = sahams.filter { it.type.category == SahamCategory.MINOR }

        val categorySummary = calculateCategorySummary(sahams)
        val strongestSaham = sahams.maxByOrNull { strengthToValue(it.strength) }
        val weakestSaham = sahams.minByOrNull { strengthToValue(it.strength) }
        val overallScore = calculateOverallScore(sahams)
        val insights = generateInsights(sahams, isDayBirth)
        val recommendations = generateRecommendations(sahams)

        return SahamAnalysis(
            sahams = sahams,
            majorSahams = majorSahams,
            minorSahams = minorSahams,
            categorySummary = categorySummary,
            strongestSaham = strongestSaham,
            weakestSaham = weakestSaham,
            overallScore = overallScore,
            isDayBirth = isDayBirth,
            keyInsights = insights,
            recommendations = recommendations
        )
    }

    /**
     * Calculate individual Saham
     */
    private fun calculateSaham(
        type: SahamType,
        isDayBirth: Boolean,
        planetMap: Map<Planet, PlanetPosition>,
        ascLong: Double,
        chart: VedicChart
    ): SahamResult? {
        val sunLong = planetMap[Planet.SUN]?.longitude ?: return null
        val moonLong = planetMap[Planet.MOON]?.longitude ?: return null
        val marsLong = planetMap[Planet.MARS]?.longitude ?: return null
        val mercuryLong = planetMap[Planet.MERCURY]?.longitude ?: return null
        val jupiterLong = planetMap[Planet.JUPITER]?.longitude ?: return null
        val venusLong = planetMap[Planet.VENUS]?.longitude ?: return null
        val saturnLong = planetMap[Planet.SATURN]?.longitude ?: return null

        val (longitude, formula) = when (type) {
            SahamType.PUNYA -> {
                if (isDayBirth) moonLong + ascLong - sunLong to "Moon + Asc - Sun"
                else sunLong + ascLong - moonLong to "Sun + Asc - Moon"
            }
            SahamType.VIDYA -> {
                if (isDayBirth) mercuryLong + ascLong - sunLong to "Mercury + Asc - Sun"
                else sunLong + ascLong - mercuryLong to "Sun + Asc - Mercury"
            }
            SahamType.YASHAS -> {
                if (isDayBirth) jupiterLong + ascLong - sunLong to "Jupiter + Asc - Sun"
                else sunLong + ascLong - jupiterLong to "Sun + Asc - Jupiter"
            }
            SahamType.MITRA -> {
                if (isDayBirth) moonLong + ascLong - mercuryLong to "Moon + Asc - Mercury"
                else mercuryLong + ascLong - moonLong to "Mercury + Asc - Moon"
            }
            SahamType.DHANA -> {
                if (isDayBirth) jupiterLong + ascLong - moonLong to "Jupiter + Asc - Moon"
                else moonLong + ascLong - jupiterLong to "Moon + Asc - Jupiter"
            }
            SahamType.KARMA -> {
                if (isDayBirth) saturnLong + ascLong - sunLong to "Saturn + Asc - Sun"
                else sunLong + ascLong - saturnLong to "Sun + Asc - Saturn"
            }
            SahamType.VIVAHA -> {
                if (isDayBirth) venusLong + ascLong - saturnLong to "Venus + Asc - Saturn"
                else saturnLong + ascLong - venusLong to "Saturn + Asc - Venus"
            }
            SahamType.PUTRA -> {
                if (isDayBirth) jupiterLong + ascLong - moonLong to "Jupiter + Asc - Moon"
                else moonLong + ascLong - jupiterLong to "Moon + Asc - Jupiter"
            }
            SahamType.PITRI -> {
                if (isDayBirth) saturnLong + ascLong - sunLong to "Saturn + Asc - Sun"
                else sunLong + ascLong - saturnLong to "Sun + Asc - Saturn"
            }
            SahamType.MATRI -> {
                if (isDayBirth) moonLong + ascLong - venusLong to "Moon + Asc - Venus"
                else venusLong + ascLong - moonLong to "Venus + Asc - Moon"
            }
            SahamType.BHRATRI -> {
                if (isDayBirth) jupiterLong + ascLong - saturnLong to "Jupiter + Asc - Saturn"
                else saturnLong + ascLong - jupiterLong to "Saturn + Asc - Jupiter"
            }
            SahamType.SAMARTHA -> {
                if (isDayBirth) marsLong + ascLong - saturnLong to "Mars + Asc - Saturn"
                else saturnLong + ascLong - marsLong to "Saturn + Asc - Mars"
            }
            SahamType.ASHA -> {
                if (isDayBirth) saturnLong + ascLong - venusLong to "Saturn + Asc - Venus"
                else venusLong + ascLong - saturnLong to "Venus + Asc - Saturn"
            }
            SahamType.MAHATMYA -> {
                if (isDayBirth) jupiterLong + ascLong - moonLong to "Jupiter + Asc - Moon"
                else moonLong + ascLong - jupiterLong to "Moon + Asc - Jupiter"
            }
            SahamType.ROGA -> {
                if (isDayBirth) saturnLong + ascLong - marsLong to "Saturn + Asc - Mars"
                else marsLong + ascLong - saturnLong to "Mars + Asc - Saturn"
            }
            SahamType.MRITYU -> {
                if (isDayBirth) saturnLong + ascLong - moonLong to "Saturn + Asc - Moon"
                else moonLong + ascLong - saturnLong to "Moon + Asc - Saturn"
            }
            SahamType.RAJA -> {
                if (isDayBirth) sunLong + ascLong - saturnLong to "Sun + Asc - Saturn"
                else saturnLong + ascLong - sunLong to "Saturn + Asc - Sun"
            }
            SahamType.PARADESA -> {
                if (isDayBirth) saturnLong + ascLong - moonLong to "Saturn + Asc - Moon"
                else moonLong + ascLong - saturnLong to "Moon + Asc - Saturn"
            }
            SahamType.BANDHU -> {
                if (isDayBirth) mercuryLong + ascLong - moonLong to "Mercury + Asc - Moon"
                else moonLong + ascLong - mercuryLong to "Moon + Asc - Mercury"
            }
            SahamType.GAURAVA -> {
                if (isDayBirth) jupiterLong + ascLong - sunLong to "Jupiter + Asc - Sun"
                else sunLong + ascLong - jupiterLong to "Sun + Asc - Jupiter"
            }
        }

        val normalizedLong = normalizeDegree(longitude)
        val sign = ZodiacSign.fromLongitude(normalizedLong)
        val degreeInSign = normalizedLong % DEGREES_PER_SIGN
        val house = calculateWholeSignHouse(normalizedLong, ascLong)
        val (nakshatra, _) = Nakshatra.fromLongitude(normalizedLong)
        val nakshatraPada = calculateNakshatraPada(normalizedLong)
        val lord = sign.ruler
        val lordPosition = planetMap[lord]

        val strength = calculateSahamStrength(
            house, lord, lordPosition, chart
        )
        val isActivated = isSahamActivated(lord, lordPosition, house)
        val interpretation = buildInterpretation(type, sign, house, lord, lordPosition, strength)

        return SahamResult(
            type = type,
            longitude = normalizedLong,
            sign = sign,
            degreeInSign = degreeInSign,
            house = house,
            nakshatra = nakshatra,
            nakshatraPada = nakshatraPada,
            lord = lord,
            lordPosition = lordPosition,
            formula = formula,
            strength = strength,
            isActivated = isActivated,
            interpretation = interpretation
        )
    }

    /**
     * Calculate Saham strength based on placement
     */
    private fun calculateSahamStrength(
        house: Int,
        lord: Planet,
        lordPosition: PlanetPosition?,
        chart: VedicChart
    ): SahamStrength {
        if (lordPosition == null) return SahamStrength.WEAK

        var strengthScore = 0

        // House placement strength
        strengthScore += when (house) {
            1, 4, 7, 10 -> 3  // Kendra houses
            5, 9 -> 3         // Trikona houses
            2, 11 -> 2        // Dhana houses
            3 -> 1            // Neutral
            6, 8, 12 -> -1    // Dusthana houses
            else -> 0
        }

        // Lord's house placement
        strengthScore += when (lordPosition.house) {
            1, 4, 5, 7, 9, 10, 11 -> 2
            2, 3 -> 1
            6, 8, 12 -> -1
            else -> 0
        }

        // Lord's dignity
        val lordSign = lordPosition.sign
        if (isExalted(lord, lordSign)) strengthScore += 2
        else if (isOwnSign(lord, lordSign)) strengthScore += 2
        else if (isDebilitated(lord, lordSign)) strengthScore -= 2

        // Lord retrograde
        if (lordPosition.isRetrograde) strengthScore -= 1

        return when {
            strengthScore >= 6 -> SahamStrength.EXCELLENT
            strengthScore >= 4 -> SahamStrength.STRONG
            strengthScore >= 2 -> SahamStrength.MODERATE
            strengthScore >= 0 -> SahamStrength.WEAK
            else -> SahamStrength.AFFLICTED
        }
    }

    /**
     * Check if Saham is activated
     */
    private fun isSahamActivated(
        lord: Planet,
        lordPosition: PlanetPosition?,
        house: Int
    ): Boolean {
        if (lordPosition == null) return false

        val isLordInGoodHouse = lordPosition.house in listOf(1, 4, 5, 7, 9, 10, 11)
        val isSahamInGoodHouse = house in listOf(1, 2, 4, 5, 7, 9, 10, 11)
        val isNotRetrograde = !lordPosition.isRetrograde

        return (isLordInGoodHouse && isSahamInGoodHouse) || (isLordInGoodHouse && isNotRetrograde)
    }

    /**
     * Determine if day or night birth
     */
    private fun isDayBirth(chart: VedicChart): Boolean {
        val hour = chart.birthData.dateTime.hour
        return hour in 6..17
    }

    // ============================================
    // HELPER METHODS
    // ============================================

    private fun normalizeDegree(degree: Double): Double {
        var normalized = degree % DEGREES_PER_CIRCLE
        if (normalized < 0) normalized += DEGREES_PER_CIRCLE
        return normalized
    }

    private fun calculateWholeSignHouse(longitude: Double, ascendant: Double): Int {
        val ascSign = (ascendant / DEGREES_PER_SIGN).toInt()
        val planetSign = (longitude / DEGREES_PER_SIGN).toInt()
        var house = planetSign - ascSign + 1
        if (house <= 0) house += 12
        if (house > 12) house -= 12
        return house
    }

    private fun calculateNakshatraPada(longitude: Double): Int {
        val nakshatraIndex = (longitude / (DEGREES_PER_CIRCLE / 27.0)).toInt()
        val degreeInNakshatra = longitude % (DEGREES_PER_CIRCLE / 27.0)
        return ((degreeInNakshatra / (DEGREES_PER_CIRCLE / 108.0)).toInt() % 4) + 1
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
            else -> false
        }
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

    private fun isOwnSign(planet: Planet, sign: ZodiacSign): Boolean {
        return sign.ruler == planet
    }

    private fun strengthToValue(strength: SahamStrength): Int {
        return when (strength) {
            SahamStrength.EXCELLENT -> 5
            SahamStrength.STRONG -> 4
            SahamStrength.MODERATE -> 3
            SahamStrength.WEAK -> 2
            SahamStrength.AFFLICTED -> 1
        }
    }

    // ============================================
    // ANALYSIS METHODS
    // ============================================

    private fun calculateCategorySummary(sahams: List<SahamResult>): List<CategorySummary> {
        return SahamCategory.entries.mapNotNull { category ->
            val categorySahams = sahams.filter { it.type.category == category }
            if (categorySahams.isEmpty()) return@mapNotNull null

            val strongCount = categorySahams.count {
                it.strength in listOf(SahamStrength.EXCELLENT, SahamStrength.STRONG)
            }
            val weakCount = categorySahams.count {
                it.strength in listOf(SahamStrength.WEAK, SahamStrength.AFFLICTED)
            }
            val avgStrength = categorySahams.map { strengthToValue(it.strength) }.average()

            CategorySummary(
                category = category,
                count = categorySahams.size,
                strongCount = strongCount,
                weakCount = weakCount,
                avgStrength = avgStrength
            )
        }
    }

    private fun calculateOverallScore(sahams: List<SahamResult>): Double {
        if (sahams.isEmpty()) return 50.0
        val avgStrength = sahams.map { strengthToValue(it.strength) }.average()
        return (avgStrength / 5.0 * 100.0).coerceIn(0.0, 100.0)
    }

    // ============================================
    // INTERPRETATION BUILDERS
    // ============================================

    private fun buildInterpretation(
        type: SahamType,
        sign: ZodiacSign,
        house: Int,
        lord: Planet,
        lordPosition: PlanetPosition?,
        strength: SahamStrength
    ): String {
        val houseQuality = when (house) {
            1, 4, 7, 10 -> "angular"
            5, 9 -> "trinal"
            2, 11 -> "wealth"
            3, 6 -> "growth"
            8, 12 -> "hidden"
            else -> "variable"
        }

        val lordQuality = when {
            lordPosition == null -> "unaspected"
            isExalted(lord, lordPosition.sign) -> "exalted"
            isOwnSign(lord, lordPosition.sign) -> "in own sign"
            isDebilitated(lord, lordPosition.sign) -> "debilitated"
            lordPosition.isRetrograde -> "retrograde"
            else -> "placed"
        }

        val strengthDesc = when (strength) {
            SahamStrength.EXCELLENT -> "exceptionally strong"
            SahamStrength.STRONG -> "well-supported"
            SahamStrength.MODERATE -> "moderately placed"
            SahamStrength.WEAK -> "needing strengthening"
            SahamStrength.AFFLICTED -> "requiring attention"
        }

        return "${type.displayName} in ${sign.displayName} (house $house, $houseQuality) is $strengthDesc. " +
                "Lord ${lord.displayName} $lordQuality in house ${lordPosition?.house ?: "unknown"}."
    }

    private fun generateInsights(sahams: List<SahamResult>, isDayBirth: Boolean): List<String> {
        val insights = mutableListOf<String>()

        val birthType = if (isDayBirth) "day" else "night"
        insights.add("$birthType birth: formulas adjusted per Tajika tradition")

        val strongSahams = sahams.filter { it.strength in listOf(SahamStrength.EXCELLENT, SahamStrength.STRONG) }
        if (strongSahams.isNotEmpty()) {
            val names = strongSahams.take(3).joinToString { it.type.displayName.split(" ")[0] }
            insights.add("Strong Sahams: $names indicate favorable life areas")
        }

        val activatedSahams = sahams.filter { it.isActivated }
        if (activatedSahams.isNotEmpty()) {
            insights.add("${activatedSahams.size} Sahams currently activated for manifestation")
        }

        val kendraCount = sahams.count { it.house in listOf(1, 4, 7, 10) }
        if (kendraCount >= 3) {
            insights.add("Multiple Sahams in angular houses strengthen overall chart")
        }

        val dusthanaCount = sahams.count { it.house in listOf(6, 8, 12) }
        if (dusthanaCount >= 4) {
            insights.add("Several Sahams in dusthana houses may indicate challenges")
        }

        return insights.take(5)
    }

    private fun generateRecommendations(sahams: List<SahamResult>): List<String> {
        val recommendations = mutableListOf<String>()

        val weakSahams = sahams.filter {
            it.strength in listOf(SahamStrength.WEAK, SahamStrength.AFFLICTED) &&
            it.type.category == SahamCategory.MAJOR
        }

        weakSahams.take(3).forEach {
            val area = it.type.description
            recommendations.add("Strengthen ${it.type.displayName.split(" ")[0]} through ${it.lord.displayName} remedies for $area")
        }

        val strongSahams = sahams.filter { it.strength == SahamStrength.EXCELLENT }
        strongSahams.take(2).forEach {
            recommendations.add("Leverage strong ${it.type.displayName.split(" ")[0]} during ${it.lord.displayName} periods")
        }

        return recommendations.take(5)
    }
}
