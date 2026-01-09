package com.astro.storm.ephemeris

import com.astro.storm.data.localization.Language
import com.astro.storm.data.localization.StringKeyNative
import com.astro.storm.data.model.Planet
import com.astro.storm.data.model.PlanetPosition
import com.astro.storm.data.model.VedicChart
import com.astro.storm.data.model.ZodiacSign

/**
 * NativeAnalysisCalculator - Comprehensive personality and life analysis
 *
 * Provides detailed analysis of:
 * - Character & Personality (Ascendant, Moon, Nakshatra)
 * - Career & Professional life (10th house, Sun, Saturn)
 * - Marriage & Relationships (7th house, Venus, Jupiter)
 * - Health & Longevity (Ascendant, 6th/8th houses)
 * - Wealth & Finance (2nd/11th houses, Jupiter)
 * - Education & Knowledge (4th/5th houses, Mercury)
 * - Spiritual Path (9th/12th houses, Ketu)
 */
object NativeAnalysisCalculator {

    // ═══════════════════════════════════════════════════════════════════════════
    // DATA CLASSES
    // ═══════════════════════════════════════════════════════════════════════════

    data class NativeAnalysisResult(
        val characterAnalysis: CharacterAnalysis,
        val careerAnalysis: CareerAnalysis,
        val marriageAnalysis: MarriageAnalysis,
        val healthAnalysis: HealthAnalysis,
        val wealthAnalysis: WealthAnalysis,
        val educationAnalysis: EducationAnalysis,
        val spiritualAnalysis: SpiritualAnalysis,
        val keyStrengths: List<TraitInfo>,
        val keyChallenges: List<TraitInfo>,
        val overallScore: Double
    )

    data class CharacterAnalysis(
        val ascendantSign: ZodiacSign,
        val moonSign: ZodiacSign,
        val sunSign: ZodiacSign,
        val ascendantTrait: StringKeyNative,
        val moonTrait: StringKeyNative,
        val nakshatraInfluence: String,
        val nakshatraInfluenceNe: String,
        val personalityStrength: StrengthLevel,
        val dominantElement: Element,
        val dominantModality: Modality,
        val summaryEn: String,
        val summaryNe: String
    )

    data class CareerAnalysis(
        val tenthLord: Planet,
        val tenthLordHouse: Int,
        val tenthLordDignity: PlanetaryDignity,
        val tenthHousePlanets: List<Planet>,
        val careerIndicators: List<StringKeyNative>,
        val favorableFields: List<String>,
        val favorableFieldsNe: List<String>,
        val careerStrength: StrengthLevel,
        val summaryEn: String,
        val summaryNe: String
    )

    data class MarriageAnalysis(
        val seventhLord: Planet,
        val seventhLordHouse: Int,
        val seventhLordDignity: PlanetaryDignity,
        val venusPosition: PlanetPosition?,
        val venusStrength: StrengthLevel,
        val marriageTiming: MarriageTiming,
        val spouseNature: String,
        val spouseNatureNe: String,
        val relationshipStrength: StrengthLevel,
        val summaryEn: String,
        val summaryNe: String
    )

    data class HealthAnalysis(
        val ascendantStrength: StrengthLevel,
        val sixthLord: Planet,
        val eighthLord: Planet,
        val constitution: ConstitutionType,
        val vulnerableAreas: StringKeyNative,
        val longevityIndicator: LongevityIndicator,
        val healthConcerns: List<String>,
        val healthConcernsNe: List<String>,
        val summaryEn: String,
        val summaryNe: String
    )

    data class WealthAnalysis(
        val secondLord: Planet,
        val secondLordStrength: StrengthLevel,
        val eleventhLord: Planet,
        val eleventhLordStrength: StrengthLevel,
        val jupiterStrength: StrengthLevel,
        val dhanaYogaPresent: Boolean,
        val primarySources: List<String>,
        val primarySourcesNe: List<String>,
        val wealthPotential: StrengthLevel,
        val summaryEn: String,
        val summaryNe: String
    )

    data class EducationAnalysis(
        val fourthLord: Planet,
        val fifthLord: Planet,
        val mercuryStrength: StrengthLevel,
        val jupiterAspectOnEducation: Boolean,
        val favorableSubjects: List<String>,
        val favorableSubjectsNe: List<String>,
        val academicPotential: StrengthLevel,
        val summaryEn: String,
        val summaryNe: String
    )

    data class SpiritualAnalysis(
        val ninthLord: Planet,
        val twelfthLord: Planet,
        val ketuPosition: PlanetPosition?,
        val jupiterStrength: StrengthLevel,
        val spiritualInclination: StrengthLevel,
        val recommendedPractices: List<String>,
        val recommendedPracticesNe: List<String>,
        val summaryEn: String,
        val summaryNe: String
    )

    data class TraitInfo(
        val trait: StringKeyNative,
        val strength: StrengthLevel,
        val planet: Planet?
    )

    enum class StrengthLevel(val value: Int, val displayName: String, val displayNameNe: String) {
        EXCELLENT(5, "Excellent", "उत्कृष्ट"),
        STRONG(4, "Strong", "बलियो"),
        MODERATE(3, "Moderate", "मध्यम"),
        WEAK(2, "Weak", "कमजोर"),
        AFFLICTED(1, "Afflicted", "पीडित")
    }

    enum class Element(val displayName: String, val displayNameNe: String) {
        FIRE("Fire", "अग्नि"),
        EARTH("Earth", "पृथ्वी"),
        AIR("Air", "वायु"),
        WATER("Water", "जल")
    }

    enum class Modality(val displayName: String, val displayNameNe: String) {
        CARDINAL("Cardinal (Chara)", "चर"),
        FIXED("Fixed (Sthira)", "स्थिर"),
        MUTABLE("Mutable (Dwiswabhava)", "द्विस्वभाव")
    }

    enum class MarriageTiming(val displayName: String, val displayNameNe: String) {
        EARLY("Early (before 27)", "प्रारम्भिक (२७ अघि)"),
        NORMAL("Normal (27-32)", "सामान्य (२७-३२)"),
        DELAYED("Delayed (after 32)", "ढिलो (३२ पछि)")
    }

    enum class ConstitutionType(val displayName: String, val displayNameNe: String) {
        STRONG("Strong", "बलियो"),
        MODERATE("Moderate", "मध्यम"),
        SENSITIVE("Sensitive", "संवेदनशील")
    }

    enum class LongevityIndicator(val displayName: String, val displayNameNe: String) {
        LONG("Long Life", "दीर्घ आयु"),
        MEDIUM("Medium Life", "मध्यम आयु"),
        REQUIRES_CARE("Requires Care", "हेरचाह आवश्यक")
    }

    enum class PlanetaryDignity(val displayName: String, val displayNameNe: String) {
        EXALTED("Exalted", "उच्च"),
        MOOLATRIKONA("Moolatrikona", "मूलत्रिकोण"),
        OWN_SIGN("Own Sign", "स्वगृह"),
        FRIEND_SIGN("Friend's Sign", "मित्र राशि"),
        NEUTRAL_SIGN("Neutral Sign", "सम राशि"),
        ENEMY_SIGN("Enemy's Sign", "शत्रु राशि"),
        DEBILITATED("Debilitated", "नीच")
    }

    // ═══════════════════════════════════════════════════════════════════════════
    // MAIN ANALYSIS FUNCTION
    // ═══════════════════════════════════════════════════════════════════════════

    fun analyzeNative(chart: VedicChart): NativeAnalysisResult {
        val characterAnalysis = analyzeCharacter(chart)
        val careerAnalysis = analyzeCareer(chart)
        val marriageAnalysis = analyzeMarriage(chart)
        val healthAnalysis = analyzeHealth(chart)
        val wealthAnalysis = analyzeWealth(chart)
        val educationAnalysis = analyzeEducation(chart)
        val spiritualAnalysis = analyzeSpirituality(chart)

        val keyStrengths = identifyKeyStrengths(chart)
        val keyChallenges = identifyKeyChallenges(chart)

        val overallScore = calculateOverallScore(
            characterAnalysis, careerAnalysis, marriageAnalysis,
            healthAnalysis, wealthAnalysis, educationAnalysis
        )

        return NativeAnalysisResult(
            characterAnalysis = characterAnalysis,
            careerAnalysis = careerAnalysis,
            marriageAnalysis = marriageAnalysis,
            healthAnalysis = healthAnalysis,
            wealthAnalysis = wealthAnalysis,
            educationAnalysis = educationAnalysis,
            spiritualAnalysis = spiritualAnalysis,
            keyStrengths = keyStrengths,
            keyChallenges = keyChallenges,
            overallScore = overallScore
        )
    }

    // ═══════════════════════════════════════════════════════════════════════════
    // CHARACTER & PERSONALITY ANALYSIS
    // ═══════════════════════════════════════════════════════════════════════════

    private fun analyzeCharacter(chart: VedicChart): CharacterAnalysis {
        val ascSign = VedicAstrologyUtils.getAscendantSign(chart)
        val moonPos = chart.planetPositions.find { it.planet == Planet.MOON }
        val sunPos = chart.planetPositions.find { it.planet == Planet.SUN }

        val moonSign = moonPos?.sign ?: ascSign
        val sunSign = sunPos?.sign ?: ascSign

        val ascendantTrait = getAscendantTrait(ascSign)
        val moonTrait = getMoonSignTrait(moonSign)

        val ascLord = ascSign.ruler
        val ascLordPos = chart.planetPositions.find { it.planet == ascLord }
        val ascLordDignity = ascLordPos?.let { getDignity(it) } ?: PlanetaryDignity.NEUTRAL_SIGN

        val personalityStrength = when {
            ascLordDignity == PlanetaryDignity.EXALTED -> StrengthLevel.EXCELLENT
            ascLordDignity == PlanetaryDignity.OWN_SIGN || ascLordDignity == PlanetaryDignity.MOOLATRIKONA -> StrengthLevel.STRONG
            ascLordDignity == PlanetaryDignity.FRIEND_SIGN -> StrengthLevel.MODERATE
            ascLordDignity == PlanetaryDignity.DEBILITATED -> StrengthLevel.AFFLICTED
            else -> StrengthLevel.MODERATE
        }

        val dominantElement = calculateDominantElement(chart)
        val dominantModality = calculateDominantModality(chart)

        val nakshatraName = moonPos?.nakshatra?.displayName ?: "Unknown"
        val nakshatraInfluence = "Your birth nakshatra $nakshatraName shapes your deeper personality traits and karmic patterns."
        val nakshatraInfluenceNe = "तपाईंको जन्म नक्षत्र $nakshatraName ले तपाईंको गहिरो व्यक्तित्व विशेषता र कार्मिक ढाँचाहरू आकार दिन्छ।"

        val summaryEn = buildCharacterSummaryEn(ascSign, moonSign, personalityStrength, dominantElement)
        val summaryNe = buildCharacterSummaryNe(ascSign, moonSign, personalityStrength, dominantElement)

        return CharacterAnalysis(
            ascendantSign = ascSign,
            moonSign = moonSign,
            sunSign = sunSign,
            ascendantTrait = ascendantTrait,
            moonTrait = moonTrait,
            nakshatraInfluence = nakshatraInfluence,
            nakshatraInfluenceNe = nakshatraInfluenceNe,
            personalityStrength = personalityStrength,
            dominantElement = dominantElement,
            dominantModality = dominantModality,
            summaryEn = summaryEn,
            summaryNe = summaryNe
        )
    }

    private fun getAscendantTrait(sign: ZodiacSign): StringKeyNative {
        return when (sign) {
            ZodiacSign.ARIES -> StringKeyNative.CHAR_ARIES_ASC
            ZodiacSign.TAURUS -> StringKeyNative.CHAR_TAURUS_ASC
            ZodiacSign.GEMINI -> StringKeyNative.CHAR_GEMINI_ASC
            ZodiacSign.CANCER -> StringKeyNative.CHAR_CANCER_ASC
            ZodiacSign.LEO -> StringKeyNative.CHAR_LEO_ASC
            ZodiacSign.VIRGO -> StringKeyNative.CHAR_VIRGO_ASC
            ZodiacSign.LIBRA -> StringKeyNative.CHAR_LIBRA_ASC
            ZodiacSign.SCORPIO -> StringKeyNative.CHAR_SCORPIO_ASC
            ZodiacSign.SAGITTARIUS -> StringKeyNative.CHAR_SAGITTARIUS_ASC
            ZodiacSign.CAPRICORN -> StringKeyNative.CHAR_CAPRICORN_ASC
            ZodiacSign.AQUARIUS -> StringKeyNative.CHAR_AQUARIUS_ASC
            ZodiacSign.PISCES -> StringKeyNative.CHAR_PISCES_ASC
        }
    }

    private fun getMoonSignTrait(sign: ZodiacSign): StringKeyNative {
        return when (sign) {
            ZodiacSign.ARIES -> StringKeyNative.MOON_ARIES
            ZodiacSign.TAURUS -> StringKeyNative.MOON_TAURUS
            ZodiacSign.GEMINI -> StringKeyNative.MOON_GEMINI
            ZodiacSign.CANCER -> StringKeyNative.MOON_CANCER
            ZodiacSign.LEO -> StringKeyNative.MOON_LEO
            ZodiacSign.VIRGO -> StringKeyNative.MOON_VIRGO
            ZodiacSign.LIBRA -> StringKeyNative.MOON_LIBRA
            ZodiacSign.SCORPIO -> StringKeyNative.MOON_SCORPIO
            ZodiacSign.SAGITTARIUS -> StringKeyNative.MOON_SAGITTARIUS
            ZodiacSign.CAPRICORN -> StringKeyNative.MOON_CAPRICORN
            ZodiacSign.AQUARIUS -> StringKeyNative.MOON_AQUARIUS
            ZodiacSign.PISCES -> StringKeyNative.MOON_PISCES
        }
    }

    private fun calculateDominantElement(chart: VedicChart): Element {
        val elementCount = mutableMapOf(
            Element.FIRE to 0, Element.EARTH to 0,
            Element.AIR to 0, Element.WATER to 0
        )

        chart.planetPositions.forEach { pos ->
            val element = getSignElement(pos.sign)
            elementCount[element] = elementCount.getOrDefault(element, 0) + 1
        }

        return elementCount.maxByOrNull { it.value }?.key ?: Element.FIRE
    }

    private fun calculateDominantModality(chart: VedicChart): Modality {
        val modalityCount = mutableMapOf(
            Modality.CARDINAL to 0, Modality.FIXED to 0, Modality.MUTABLE to 0
        )

        chart.planetPositions.forEach { pos ->
            val modality = getSignModality(pos.sign)
            modalityCount[modality] = modalityCount.getOrDefault(modality, 0) + 1
        }

        return modalityCount.maxByOrNull { it.value }?.key ?: Modality.CARDINAL
    }

    private fun getSignElement(sign: ZodiacSign): Element {
        return when (sign) {
            ZodiacSign.ARIES, ZodiacSign.LEO, ZodiacSign.SAGITTARIUS -> Element.FIRE
            ZodiacSign.TAURUS, ZodiacSign.VIRGO, ZodiacSign.CAPRICORN -> Element.EARTH
            ZodiacSign.GEMINI, ZodiacSign.LIBRA, ZodiacSign.AQUARIUS -> Element.AIR
            ZodiacSign.CANCER, ZodiacSign.SCORPIO, ZodiacSign.PISCES -> Element.WATER
        }
    }

    private fun getSignModality(sign: ZodiacSign): Modality {
        return when (sign) {
            ZodiacSign.ARIES, ZodiacSign.CANCER, ZodiacSign.LIBRA, ZodiacSign.CAPRICORN -> Modality.CARDINAL
            ZodiacSign.TAURUS, ZodiacSign.LEO, ZodiacSign.SCORPIO, ZodiacSign.AQUARIUS -> Modality.FIXED
            ZodiacSign.GEMINI, ZodiacSign.VIRGO, ZodiacSign.SAGITTARIUS, ZodiacSign.PISCES -> Modality.MUTABLE
        }
    }

    private fun buildCharacterSummaryEn(
        ascSign: ZodiacSign, moonSign: ZodiacSign,
        strength: StrengthLevel, element: Element
    ): String {
        return "With ${ascSign.displayName} Ascendant and Moon in ${moonSign.displayName}, " +
               "you have a ${element.displayName} dominant nature. " +
               "Your personality foundation is ${strength.displayName.lowercase()}, " +
               "giving you ${if (strength.value >= 4) "natural confidence and vitality" else "room for growth and self-development"}."
    }

    private fun buildCharacterSummaryNe(
        ascSign: ZodiacSign, moonSign: ZodiacSign,
        strength: StrengthLevel, element: Element
    ): String {
        return "${ascSign.displayName} लग्न र ${moonSign.displayName} मा चन्द्रमाको साथ, " +
               "तपाईंसँग ${element.displayNameNe} प्रधान स्वभाव छ। " +
               "तपाईंको व्यक्तित्वको आधार ${strength.displayNameNe} छ।"
    }

    // ═══════════════════════════════════════════════════════════════════════════
    // CAREER ANALYSIS
    // ═══════════════════════════════════════════════════════════════════════════

    private fun analyzeCareer(chart: VedicChart): CareerAnalysis {
        val tenthLord = VedicAstrologyUtils.getHouseLord(chart, 10)
        val tenthLordPos = chart.planetPositions.find { it.planet == tenthLord }
        val tenthLordHouse = tenthLordPos?.house ?: 10
        val tenthLordDignity = tenthLordPos?.let { getDignity(it) } ?: PlanetaryDignity.NEUTRAL_SIGN

        val tenthHousePlanets = VedicAstrologyUtils.getPlanetsInHouse(chart, 10)
            .map { it.planet }

        val sunPos = chart.planetPositions.find { it.planet == Planet.SUN }
        val saturnPos = chart.planetPositions.find { it.planet == Planet.SATURN }

        val careerIndicators = mutableListOf<StringKeyNative>()
        val favorableFields = mutableListOf<String>()
        val favorableFieldsNe = mutableListOf<String>()

        // Analyze Sun strength for leadership/government
        sunPos?.let { pos ->
            val sunDignity = getDignity(pos)
            if (sunDignity.ordinal <= 2) {
                careerIndicators.add(StringKeyNative.CAREER_SUN_STRONG)
                favorableFields.add("Government, Administration, Leadership")
                favorableFieldsNe.add("सरकार, प्रशासन, नेतृत्व")
            }
        }

        // Analyze Saturn for discipline/hard work
        saturnPos?.let { pos ->
            val saturnDignity = getDignity(pos)
            if (saturnDignity.ordinal <= 3) {
                careerIndicators.add(StringKeyNative.CAREER_SATURN_STRONG)
                favorableFields.add("Research, Construction, Agriculture")
                favorableFieldsNe.add("अनुसन्धान, निर्माण, कृषि")
            }
        }

        // Analyze 10th house planets
        tenthHousePlanets.forEach { planet ->
            when (planet) {
                Planet.JUPITER -> {
                    careerIndicators.add(StringKeyNative.CAREER_JUPITER_STRONG)
                    favorableFields.add("Teaching, Law, Finance, Advisory")
                    favorableFieldsNe.add("शिक्षण, कानून, वित्त, सल्लाहकार")
                }
                Planet.MERCURY -> {
                    careerIndicators.add(StringKeyNative.CAREER_MERCURY_STRONG)
                    favorableFields.add("Communication, Commerce, Technology")
                    favorableFieldsNe.add("सञ्चार, वाणिज्य, प्रविधि")
                }
                Planet.VENUS -> {
                    careerIndicators.add(StringKeyNative.CAREER_VENUS_STRONG)
                    favorableFields.add("Arts, Entertainment, Beauty Industry")
                    favorableFieldsNe.add("कला, मनोरञ्जन, सौन्दर्य उद्योग")
                }
                Planet.MARS -> {
                    careerIndicators.add(StringKeyNative.CAREER_MARS_STRONG)
                    favorableFields.add("Military, Sports, Engineering, Surgery")
                    favorableFieldsNe.add("सेना, खेलकुद, इन्जिनियरिङ, शल्यचिकित्सा")
                }
                Planet.MOON -> {
                    careerIndicators.add(StringKeyNative.CAREER_MOON_STRONG)
                    favorableFields.add("Hospitality, Nursing, Public Relations")
                    favorableFieldsNe.add("आतिथ्य, नर्सिङ, जनसम्पर्क")
                }
                Planet.RAHU -> {
                    careerIndicators.add(StringKeyNative.CAREER_RAHU_INFLUENCE)
                    favorableFields.add("Technology, Media, Foreign Connections")
                    favorableFieldsNe.add("प्रविधि, मिडिया, विदेशी सम्बन्ध")
                }
                Planet.KETU -> {
                    careerIndicators.add(StringKeyNative.CAREER_KETU_INFLUENCE)
                    favorableFields.add("Research, Spirituality, Investigation")
                    favorableFieldsNe.add("अनुसन्धान, आध्यात्मिकता, अनुसन्धान")
                }
                else -> {}
            }
        }

        if (careerIndicators.isEmpty()) {
            // Default based on 10th lord
            when (tenthLord) {
                Planet.SUN -> favorableFields.add("Leadership roles, Government")
                Planet.MOON -> favorableFields.add("Public service, Hospitality")
                Planet.MARS -> favorableFields.add("Technical fields, Sports")
                Planet.MERCURY -> favorableFields.add("Business, Communication")
                Planet.JUPITER -> favorableFields.add("Education, Advisory")
                Planet.VENUS -> favorableFields.add("Arts, Luxury goods")
                Planet.SATURN -> favorableFields.add("Administration, Mining")
                else -> favorableFields.add("Diverse opportunities")
            }
            favorableFieldsNe.add("विविध अवसरहरू")
        }

        val careerStrength = calculateCareerStrength(tenthLordDignity, tenthLordHouse, tenthHousePlanets, chart)

        val summaryEn = buildCareerSummaryEn(tenthLord, tenthLordHouse, tenthLordDignity, careerStrength)
        val summaryNe = buildCareerSummaryNe(tenthLord, tenthLordHouse, tenthLordDignity, careerStrength)

        return CareerAnalysis(
            tenthLord = tenthLord,
            tenthLordHouse = tenthLordHouse,
            tenthLordDignity = tenthLordDignity,
            tenthHousePlanets = tenthHousePlanets,
            careerIndicators = careerIndicators,
            favorableFields = favorableFields.distinct(),
            favorableFieldsNe = favorableFieldsNe.distinct(),
            careerStrength = careerStrength,
            summaryEn = summaryEn,
            summaryNe = summaryNe
        )
    }

    private fun calculateCareerStrength(
        dignity: PlanetaryDignity,
        house: Int,
        planetsIn10th: List<Planet>,
        chart: VedicChart
    ): StrengthLevel {
        var score = 3.0

        // Dignity of 10th lord
        score += when (dignity) {
            PlanetaryDignity.EXALTED -> 2.0
            PlanetaryDignity.MOOLATRIKONA, PlanetaryDignity.OWN_SIGN -> 1.5
            PlanetaryDignity.FRIEND_SIGN -> 0.5
            PlanetaryDignity.ENEMY_SIGN -> -0.5
            PlanetaryDignity.DEBILITATED -> -1.5
            else -> 0.0
        }

        // House placement of 10th lord
        if (house in VedicAstrologyUtils.KENDRA_HOUSES) score += 0.5
        if (house in VedicAstrologyUtils.TRIKONA_HOUSES) score += 0.5
        if (house in VedicAstrologyUtils.DUSTHANA_HOUSES) score -= 0.5

        // Benefics in 10th house
        planetsIn10th.forEach { planet ->
            if (VedicAstrologyUtils.isNaturalBenefic(planet)) score += 0.3
            if (planet == Planet.JUPITER) score += 0.3
        }

        return when {
            score >= 5.0 -> StrengthLevel.EXCELLENT
            score >= 4.0 -> StrengthLevel.STRONG
            score >= 3.0 -> StrengthLevel.MODERATE
            score >= 2.0 -> StrengthLevel.WEAK
            else -> StrengthLevel.AFFLICTED
        }
    }

    private fun buildCareerSummaryEn(
        lord: Planet, house: Int, dignity: PlanetaryDignity, strength: StrengthLevel
    ): String {
        val houseDesc = when (house) {
            1 -> "1st house (self-employment, independent work)"
            2 -> "2nd house (finance, family business)"
            4 -> "4th house (real estate, education)"
            5 -> "5th house (creative fields, speculation)"
            7 -> "7th house (partnerships, consulting)"
            9 -> "9th house (teaching, law, international)"
            10 -> "10th house (strong career focus)"
            11 -> "11th house (networking, large organizations)"
            else -> "${house}th house"
        }
        return "Your 10th lord ${lord.displayName} is placed in the $houseDesc in ${dignity.displayName} dignity. " +
               "Career potential is ${strength.displayName.lowercase()}."
    }

    private fun buildCareerSummaryNe(
        lord: Planet, house: Int, dignity: PlanetaryDignity, strength: StrengthLevel
    ): String {
        return "तपाईंको १०औं भावको स्वामी ${lord.displayName} ${house}औं भावमा ${dignity.displayNameNe} अवस्थामा छ। " +
               "पेशागत क्षमता ${strength.displayNameNe} छ।"
    }

    // ═══════════════════════════════════════════════════════════════════════════
    // MARRIAGE ANALYSIS
    // ═══════════════════════════════════════════════════════════════════════════

    private fun analyzeMarriage(chart: VedicChart): MarriageAnalysis {
        val seventhLord = VedicAstrologyUtils.getHouseLord(chart, 7)
        val seventhLordPos = chart.planetPositions.find { it.planet == seventhLord }
        val seventhLordHouse = seventhLordPos?.house ?: 7
        val seventhLordDignity = seventhLordPos?.let { getDignity(it) } ?: PlanetaryDignity.NEUTRAL_SIGN

        val venusPos = chart.planetPositions.find { it.planet == Planet.VENUS }
        val venusDignity = venusPos?.let { getDignity(it) } ?: PlanetaryDignity.NEUTRAL_SIGN

        val venusStrength = when {
            venusDignity == PlanetaryDignity.EXALTED -> StrengthLevel.EXCELLENT
            venusDignity == PlanetaryDignity.OWN_SIGN -> StrengthLevel.STRONG
            venusDignity == PlanetaryDignity.DEBILITATED -> StrengthLevel.AFFLICTED
            else -> StrengthLevel.MODERATE
        }

        // Marriage timing based on 7th lord and Venus
        val marriageTiming = determineMarriageTiming(chart, seventhLordHouse, venusPos)

        // Spouse nature based on 7th house sign and planets
        val seventhSign = VedicAstrologyUtils.getHouseSign(chart, 7)
        val planetsIn7th = VedicAstrologyUtils.getPlanetsInHouse(chart, 7)

        val (spouseNature, spouseNatureNe) = determineSpouseNature(seventhSign, planetsIn7th)

        val relationshipStrength = calculateRelationshipStrength(
            seventhLordDignity, venusStrength, planetsIn7th, chart
        )

        val summaryEn = buildMarriageSummaryEn(seventhLord, seventhLordHouse, venusStrength, marriageTiming)
        val summaryNe = buildMarriageSummaryNe(seventhLord, seventhLordHouse, venusStrength, marriageTiming)

        return MarriageAnalysis(
            seventhLord = seventhLord,
            seventhLordHouse = seventhLordHouse,
            seventhLordDignity = seventhLordDignity,
            venusPosition = venusPos,
            venusStrength = venusStrength,
            marriageTiming = marriageTiming,
            spouseNature = spouseNature,
            spouseNatureNe = spouseNatureNe,
            relationshipStrength = relationshipStrength,
            summaryEn = summaryEn,
            summaryNe = summaryNe
        )
    }

    private fun determineMarriageTiming(
        chart: VedicChart,
        seventhLordHouse: Int,
        venusPos: PlanetPosition?
    ): MarriageTiming {
        val venusHouse = venusPos?.house ?: 7

        // Early marriage indicators
        if (seventhLordHouse in listOf(1, 2, 4, 7, 11) && venusHouse in listOf(1, 2, 4, 7)) {
            return MarriageTiming.EARLY
        }

        // Delayed marriage indicators
        if (seventhLordHouse in VedicAstrologyUtils.DUSTHANA_HOUSES || venusHouse in listOf(6, 8, 12)) {
            return MarriageTiming.DELAYED
        }

        // Check for Saturn influence on 7th house
        val saturnPos = chart.planetPositions.find { it.planet == Planet.SATURN }
        if (saturnPos?.house == 7 || saturnPos?.house == seventhLordHouse) {
            return MarriageTiming.DELAYED
        }

        return MarriageTiming.NORMAL
    }

    private fun determineSpouseNature(
        seventhSign: ZodiacSign,
        planetsIn7th: List<PlanetPosition>
    ): Pair<String, String> {
        val traits = mutableListOf<String>()
        val traitsNe = mutableListOf<String>()

        // Base nature from sign
        when (getSignElement(seventhSign)) {
            Element.FIRE -> {
                traits.add("dynamic and energetic")
                traitsNe.add("गतिशील र ऊर्जावान")
            }
            Element.EARTH -> {
                traits.add("practical and stable")
                traitsNe.add("व्यावहारिक र स्थिर")
            }
            Element.AIR -> {
                traits.add("intellectual and communicative")
                traitsNe.add("बौद्धिक र संवादशील")
            }
            Element.WATER -> {
                traits.add("emotional and intuitive")
                traitsNe.add("भावनात्मक र अन्तर्ज्ञानी")
            }
        }

        // Modify based on planets
        planetsIn7th.forEach { pos ->
            when (pos.planet) {
                Planet.VENUS -> {
                    traits.add("attractive and romantic")
                    traitsNe.add("आकर्षक र रोमान्टिक")
                }
                Planet.JUPITER -> {
                    traits.add("wise and generous")
                    traitsNe.add("बुद्धिमान र उदार")
                }
                Planet.MARS -> {
                    traits.add("passionate and assertive")
                    traitsNe.add("जोशपूर्ण र दृढ")
                }
                Planet.MERCURY -> {
                    traits.add("intelligent and versatile")
                    traitsNe.add("बुद्धिमान र बहुमुखी")
                }
                else -> {}
            }
        }

        return Pair(
            traits.joinToString(", "),
            traitsNe.joinToString(", ")
        )
    }

    private fun calculateRelationshipStrength(
        seventhLordDignity: PlanetaryDignity,
        venusStrength: StrengthLevel,
        planetsIn7th: List<PlanetPosition>,
        chart: VedicChart
    ): StrengthLevel {
        var score = 3.0

        // 7th lord dignity
        score += when (seventhLordDignity) {
            PlanetaryDignity.EXALTED -> 1.5
            PlanetaryDignity.OWN_SIGN, PlanetaryDignity.MOOLATRIKONA -> 1.0
            PlanetaryDignity.DEBILITATED -> -1.5
            else -> 0.0
        }

        // Venus strength
        score += (venusStrength.value - 3) * 0.3

        // Benefics in 7th house
        planetsIn7th.forEach { pos ->
            if (VedicAstrologyUtils.isNaturalBenefic(pos.planet)) score += 0.3
            if (VedicAstrologyUtils.isNaturalMalefic(pos.planet) && pos.planet !in listOf(Planet.SUN)) {
                score -= 0.3
            }
        }

        // Jupiter aspect on 7th
        val jupiterPos = chart.planetPositions.find { it.planet == Planet.JUPITER }
        if (jupiterPos != null && aspectsHouse(jupiterPos, 7, chart)) {
            score += 0.5
        }

        return when {
            score >= 4.5 -> StrengthLevel.EXCELLENT
            score >= 3.5 -> StrengthLevel.STRONG
            score >= 2.5 -> StrengthLevel.MODERATE
            score >= 1.5 -> StrengthLevel.WEAK
            else -> StrengthLevel.AFFLICTED
        }
    }

    private fun buildMarriageSummaryEn(
        lord: Planet, house: Int, venusStrength: StrengthLevel, timing: MarriageTiming
    ): String {
        return "Your 7th lord ${lord.displayName} is in the ${house}th house. " +
               "Venus strength is ${venusStrength.displayName.lowercase()}, indicating ${timing.displayName.lowercase()} marriage prospects. " +
               "Focus on ${if (venusStrength.value >= 4) "maintaining harmony" else "developing patience and understanding"} in relationships."
    }

    private fun buildMarriageSummaryNe(
        lord: Planet, house: Int, venusStrength: StrengthLevel, timing: MarriageTiming
    ): String {
        return "तपाईंको ७औं भावको स्वामी ${lord.displayName} ${house}औं भावमा छ। " +
               "शुक्रको शक्ति ${venusStrength.displayNameNe} छ, ${timing.displayNameNe} विवाह सम्भावना संकेत गर्दै।"
    }

    // ═══════════════════════════════════════════════════════════════════════════
    // HEALTH ANALYSIS
    // ═══════════════════════════════════════════════════════════════════════════

    private fun analyzeHealth(chart: VedicChart): HealthAnalysis {
        val ascSign = VedicAstrologyUtils.getAscendantSign(chart)
        val ascLord = ascSign.ruler
        val ascLordPos = chart.planetPositions.find { it.planet == ascLord }
        val ascLordDignity = ascLordPos?.let { getDignity(it) } ?: PlanetaryDignity.NEUTRAL_SIGN

        val ascendantStrength = when {
            ascLordDignity == PlanetaryDignity.EXALTED -> StrengthLevel.EXCELLENT
            ascLordDignity == PlanetaryDignity.OWN_SIGN -> StrengthLevel.STRONG
            ascLordDignity == PlanetaryDignity.DEBILITATED -> StrengthLevel.WEAK
            else -> StrengthLevel.MODERATE
        }

        val sixthLord = VedicAstrologyUtils.getHouseLord(chart, 6)
        val eighthLord = VedicAstrologyUtils.getHouseLord(chart, 8)

        val constitution = when (ascendantStrength) {
            StrengthLevel.EXCELLENT, StrengthLevel.STRONG -> ConstitutionType.STRONG
            StrengthLevel.MODERATE -> ConstitutionType.MODERATE
            else -> ConstitutionType.SENSITIVE
        }

        val vulnerableAreas = getHealthAreasForSign(ascSign)

        val longevityIndicator = calculateLongevity(chart, ascendantStrength, sixthLord, eighthLord)

        val (healthConcerns, healthConcernsNe) = identifyHealthConcerns(chart, ascSign)

        val summaryEn = buildHealthSummaryEn(constitution, longevityIndicator, ascSign)
        val summaryNe = buildHealthSummaryNe(constitution, longevityIndicator, ascSign)

        return HealthAnalysis(
            ascendantStrength = ascendantStrength,
            sixthLord = sixthLord,
            eighthLord = eighthLord,
            constitution = constitution,
            vulnerableAreas = vulnerableAreas,
            longevityIndicator = longevityIndicator,
            healthConcerns = healthConcerns,
            healthConcernsNe = healthConcernsNe,
            summaryEn = summaryEn,
            summaryNe = summaryNe
        )
    }

    private fun getHealthAreasForSign(sign: ZodiacSign): StringKeyNative {
        return when (sign) {
            ZodiacSign.ARIES -> StringKeyNative.HEALTH_ARIES_AREAS
            ZodiacSign.TAURUS -> StringKeyNative.HEALTH_TAURUS_AREAS
            ZodiacSign.GEMINI -> StringKeyNative.HEALTH_GEMINI_AREAS
            ZodiacSign.CANCER -> StringKeyNative.HEALTH_CANCER_AREAS
            ZodiacSign.LEO -> StringKeyNative.HEALTH_LEO_AREAS
            ZodiacSign.VIRGO -> StringKeyNative.HEALTH_VIRGO_AREAS
            ZodiacSign.LIBRA -> StringKeyNative.HEALTH_LIBRA_AREAS
            ZodiacSign.SCORPIO -> StringKeyNative.HEALTH_SCORPIO_AREAS
            ZodiacSign.SAGITTARIUS -> StringKeyNative.HEALTH_SAGITTARIUS_AREAS
            ZodiacSign.CAPRICORN -> StringKeyNative.HEALTH_CAPRICORN_AREAS
            ZodiacSign.AQUARIUS -> StringKeyNative.HEALTH_AQUARIUS_AREAS
            ZodiacSign.PISCES -> StringKeyNative.HEALTH_PISCES_AREAS
        }
    }

    private fun calculateLongevity(
        chart: VedicChart,
        ascStrength: StrengthLevel,
        sixthLord: Planet,
        eighthLord: Planet
    ): LongevityIndicator {
        var score = ascStrength.value.toDouble()

        // Check 8th lord placement
        val eighthLordPos = chart.planetPositions.find { it.planet == eighthLord }
        if (eighthLordPos != null) {
            val dignity = getDignity(eighthLordPos)
            if (dignity == PlanetaryDignity.EXALTED || dignity == PlanetaryDignity.OWN_SIGN) {
                score += 1.0
            }
        }

        // Check malefics in 6th, 8th, 12th
        val maleficsInDusthana = chart.planetPositions.filter {
            it.house in listOf(6, 8, 12) && VedicAstrologyUtils.isNaturalMalefic(it.planet)
        }.size
        score -= maleficsInDusthana * 0.3

        // Check Jupiter strength (natural protector)
        val jupiterPos = chart.planetPositions.find { it.planet == Planet.JUPITER }
        if (jupiterPos != null && getDignity(jupiterPos).ordinal <= 2) {
            score += 0.5
        }

        return when {
            score >= 4.0 -> LongevityIndicator.LONG
            score >= 2.5 -> LongevityIndicator.MEDIUM
            else -> LongevityIndicator.REQUIRES_CARE
        }
    }

    private fun identifyHealthConcerns(
        chart: VedicChart,
        ascSign: ZodiacSign
    ): Pair<List<String>, List<String>> {
        val concerns = mutableListOf<String>()
        val concernsNe = mutableListOf<String>()

        // Based on ascendant sign
        when (ascSign) {
            ZodiacSign.ARIES -> {
                concerns.add("Head injuries, migraines")
                concernsNe.add("टाउको चोट, माइग्रेन")
            }
            ZodiacSign.TAURUS -> {
                concerns.add("Throat issues, thyroid")
                concernsNe.add("घाँटी समस्या, थाइरोइड")
            }
            ZodiacSign.GEMINI -> {
                concerns.add("Respiratory issues, nervous tension")
                concernsNe.add("श्वासप्रश्वास समस्या, स्नायु तनाव")
            }
            ZodiacSign.CANCER -> {
                concerns.add("Digestive issues, emotional health")
                concernsNe.add("पाचन समस्या, भावनात्मक स्वास्थ्य")
            }
            ZodiacSign.LEO -> {
                concerns.add("Heart issues, blood pressure")
                concernsNe.add("हृदय समस्या, रक्तचाप")
            }
            ZodiacSign.VIRGO -> {
                concerns.add("Intestinal issues, anxiety")
                concernsNe.add("आन्द्रा समस्या, चिन्ता")
            }
            ZodiacSign.LIBRA -> {
                concerns.add("Kidney issues, lower back")
                concernsNe.add("मिर्गौला समस्या, तल्लो ढाड")
            }
            ZodiacSign.SCORPIO -> {
                concerns.add("Reproductive health, bladder")
                concernsNe.add("प्रजनन स्वास्थ्य, मूत्राशय")
            }
            ZodiacSign.SAGITTARIUS -> {
                concerns.add("Hip/thigh issues, liver")
                concernsNe.add("कमर/जाँघ समस्या, कलेजो")
            }
            ZodiacSign.CAPRICORN -> {
                concerns.add("Joint/bone issues, skin")
                concernsNe.add("जोर्नी/हड्डी समस्या, छाला")
            }
            ZodiacSign.AQUARIUS -> {
                concerns.add("Circulation, ankle issues")
                concernsNe.add("रक्त संचार, गोली समस्या")
            }
            ZodiacSign.PISCES -> {
                concerns.add("Feet issues, immune system")
                concernsNe.add("खुट्टा समस्या, प्रतिरक्षा प्रणाली")
            }
        }

        // Check afflicted planets
        chart.planetPositions.filter {
            getDignity(it) == PlanetaryDignity.DEBILITATED
        }.forEach { pos ->
            when (pos.planet) {
                Planet.SUN -> {
                    concerns.add("Vitality and heart health need attention")
                    concernsNe.add("जीवनी शक्ति र हृदय स्वास्थ्यमा ध्यान दिनुहोस्")
                }
                Planet.MOON -> {
                    concerns.add("Mental health and emotional balance")
                    concernsNe.add("मानसिक स्वास्थ्य र भावनात्मक सन्तुलन")
                }
                Planet.MARS -> {
                    concerns.add("Blood-related and inflammatory issues")
                    concernsNe.add("रक्त-सम्बन्धित र सूजन समस्या")
                }
                else -> {}
            }
        }

        return Pair(concerns, concernsNe)
    }

    private fun buildHealthSummaryEn(
        constitution: ConstitutionType,
        longevity: LongevityIndicator,
        ascSign: ZodiacSign
    ): String {
        return "Your constitution is ${constitution.displayName.lowercase()} with ${longevity.displayName.lowercase()} longevity indicators. " +
               "As a ${ascSign.displayName} ascendant, pay attention to the vulnerable areas associated with your sign."
    }

    private fun buildHealthSummaryNe(
        constitution: ConstitutionType,
        longevity: LongevityIndicator,
        ascSign: ZodiacSign
    ): String {
        return "तपाईंको संरचना ${constitution.displayNameNe} छ ${longevity.displayNameNe} संकेतकहरूको साथ। " +
               "${ascSign.displayName} लग्नको रूपमा, तपाईंको राशिसँग सम्बन्धित संवेदनशील क्षेत्रहरूमा ध्यान दिनुहोस्।"
    }

    // ═══════════════════════════════════════════════════════════════════════════
    // WEALTH ANALYSIS
    // ═══════════════════════════════════════════════════════════════════════════

    private fun analyzeWealth(chart: VedicChart): WealthAnalysis {
        val secondLord = VedicAstrologyUtils.getHouseLord(chart, 2)
        val secondLordPos = chart.planetPositions.find { it.planet == secondLord }
        val secondLordDignity = secondLordPos?.let { getDignity(it) } ?: PlanetaryDignity.NEUTRAL_SIGN

        val eleventhLord = VedicAstrologyUtils.getHouseLord(chart, 11)
        val eleventhLordPos = chart.planetPositions.find { it.planet == eleventhLord }
        val eleventhLordDignity = eleventhLordPos?.let { getDignity(it) } ?: PlanetaryDignity.NEUTRAL_SIGN

        val jupiterPos = chart.planetPositions.find { it.planet == Planet.JUPITER }
        val jupiterDignity = jupiterPos?.let { getDignity(it) } ?: PlanetaryDignity.NEUTRAL_SIGN

        val secondLordStrength = dignityToStrength(secondLordDignity)
        val eleventhLordStrength = dignityToStrength(eleventhLordDignity)
        val jupiterStrength = dignityToStrength(jupiterDignity)

        // Check for Dhana Yoga
        val dhanaYogaPresent = checkDhanaYoga(chart)

        val (primarySources, primarySourcesNe) = identifyWealthSources(chart, secondLord, eleventhLord)

        val wealthPotential = calculateWealthPotential(
            secondLordStrength, eleventhLordStrength, jupiterStrength, dhanaYogaPresent
        )

        val summaryEn = buildWealthSummaryEn(secondLord, eleventhLord, wealthPotential, dhanaYogaPresent)
        val summaryNe = buildWealthSummaryNe(secondLord, eleventhLord, wealthPotential, dhanaYogaPresent)

        return WealthAnalysis(
            secondLord = secondLord,
            secondLordStrength = secondLordStrength,
            eleventhLord = eleventhLord,
            eleventhLordStrength = eleventhLordStrength,
            jupiterStrength = jupiterStrength,
            dhanaYogaPresent = dhanaYogaPresent,
            primarySources = primarySources,
            primarySourcesNe = primarySourcesNe,
            wealthPotential = wealthPotential,
            summaryEn = summaryEn,
            summaryNe = summaryNe
        )
    }

    private fun checkDhanaYoga(chart: VedicChart): Boolean {
        // Basic Dhana Yoga: Lords of 2nd, 5th, 9th, 11th in mutual relationship
        val wealthLords = listOf(
            VedicAstrologyUtils.getHouseLord(chart, 2),
            VedicAstrologyUtils.getHouseLord(chart, 5),
            VedicAstrologyUtils.getHouseLord(chart, 9),
            VedicAstrologyUtils.getHouseLord(chart, 11)
        )

        // Check if any wealth lord is in Kendra or Trikona
        val wealthLordPositions = chart.planetPositions.filter { it.planet in wealthLords }
        val inGoodHouses = wealthLordPositions.count {
            it.house in VedicAstrologyUtils.KENDRA_HOUSES || it.house in VedicAstrologyUtils.TRIKONA_HOUSES
        }

        // Check if Jupiter aspects 2nd or 11th house
        val jupiterPos = chart.planetPositions.find { it.planet == Planet.JUPITER }
        val jupiterAspects = jupiterPos != null &&
            (aspectsHouse(jupiterPos, 2, chart) || aspectsHouse(jupiterPos, 11, chart))

        return inGoodHouses >= 2 || jupiterAspects
    }

    private fun identifyWealthSources(
        chart: VedicChart,
        secondLord: Planet,
        eleventhLord: Planet
    ): Pair<List<String>, List<String>> {
        val sources = mutableListOf<String>()
        val sourcesNe = mutableListOf<String>()

        // Based on 2nd lord
        when (secondLord) {
            Planet.SUN -> {
                sources.add("Government, Authority positions")
                sourcesNe.add("सरकार, अधिकार पद")
            }
            Planet.MOON -> {
                sources.add("Public dealing, Liquids, Travel")
                sourcesNe.add("जन व्यवहार, तरल पदार्थ, यात्रा")
            }
            Planet.MARS -> {
                sources.add("Real estate, Engineering, Sports")
                sourcesNe.add("जग्गा, इन्जिनियरिङ, खेलकुद")
            }
            Planet.MERCURY -> {
                sources.add("Business, Communication, Writing")
                sourcesNe.add("व्यापार, सञ्चार, लेखन")
            }
            Planet.JUPITER -> {
                sources.add("Teaching, Advisory, Finance")
                sourcesNe.add("शिक्षण, सल्लाहकार, वित्त")
            }
            Planet.VENUS -> {
                sources.add("Arts, Entertainment, Beauty")
                sourcesNe.add("कला, मनोरञ्जन, सौन्दर्य")
            }
            Planet.SATURN -> {
                sources.add("Labor, Mining, Agriculture")
                sourcesNe.add("श्रम, खनन, कृषि")
            }
            else -> {
                sources.add("Diverse sources")
                sourcesNe.add("विविध स्रोत")
            }
        }

        // Based on 11th lord (gains)
        when (eleventhLord) {
            Planet.JUPITER -> {
                sources.add("Investments, Teaching")
                sourcesNe.add("लगानी, शिक्षण")
            }
            Planet.MERCURY -> {
                sources.add("Trading, Networking")
                sourcesNe.add("व्यापार, नेटवर्किङ")
            }
            Planet.VENUS -> {
                sources.add("Partnerships, Luxury items")
                sourcesNe.add("साझेदारी, विलासी वस्तुहरू")
            }
            else -> {}
        }

        return Pair(sources.distinct(), sourcesNe.distinct())
    }

    private fun calculateWealthPotential(
        secondStrength: StrengthLevel,
        eleventhStrength: StrengthLevel,
        jupiterStrength: StrengthLevel,
        dhanaYoga: Boolean
    ): StrengthLevel {
        var score = (secondStrength.value + eleventhStrength.value + jupiterStrength.value) / 3.0
        if (dhanaYoga) score += 1.0

        return when {
            score >= 4.5 -> StrengthLevel.EXCELLENT
            score >= 3.5 -> StrengthLevel.STRONG
            score >= 2.5 -> StrengthLevel.MODERATE
            score >= 1.5 -> StrengthLevel.WEAK
            else -> StrengthLevel.AFFLICTED
        }
    }

    private fun buildWealthSummaryEn(
        secondLord: Planet, eleventhLord: Planet, potential: StrengthLevel, dhanaYoga: Boolean
    ): String {
        val yogaText = if (dhanaYoga) "Dhana Yoga present enhances wealth potential. " else ""
        return "${yogaText}Your 2nd lord ${secondLord.displayName} and 11th lord ${eleventhLord.displayName} " +
               "indicate ${potential.displayName.lowercase()} wealth accumulation potential through steady effort."
    }

    private fun buildWealthSummaryNe(
        secondLord: Planet, eleventhLord: Planet, potential: StrengthLevel, dhanaYoga: Boolean
    ): String {
        val yogaText = if (dhanaYoga) "धन योगले धन क्षमता बढाउँछ। " else ""
        return "${yogaText}तपाईंको २औं स्वामी ${secondLord.displayName} र ११औं स्वामी ${eleventhLord.displayName} " +
               "ले ${potential.displayNameNe} धन संचय क्षमता संकेत गर्छ।"
    }

    // ═══════════════════════════════════════════════════════════════════════════
    // EDUCATION ANALYSIS
    // ═══════════════════════════════════════════════════════════════════════════

    private fun analyzeEducation(chart: VedicChart): EducationAnalysis {
        val fourthLord = VedicAstrologyUtils.getHouseLord(chart, 4)
        val fifthLord = VedicAstrologyUtils.getHouseLord(chart, 5)

        val mercuryPos = chart.planetPositions.find { it.planet == Planet.MERCURY }
        val mercuryDignity = mercuryPos?.let { getDignity(it) } ?: PlanetaryDignity.NEUTRAL_SIGN
        val mercuryStrength = dignityToStrength(mercuryDignity)

        val jupiterPos = chart.planetPositions.find { it.planet == Planet.JUPITER }
        val jupiterAspectOnEducation = jupiterPos != null &&
            (aspectsHouse(jupiterPos, 4, chart) || aspectsHouse(jupiterPos, 5, chart))

        val (favorableSubjects, favorableSubjectsNe) = identifyFavorableSubjects(chart)

        val academicPotential = calculateAcademicPotential(
            chart, fourthLord, fifthLord, mercuryStrength, jupiterAspectOnEducation
        )

        val summaryEn = buildEducationSummaryEn(mercuryStrength, academicPotential, jupiterAspectOnEducation)
        val summaryNe = buildEducationSummaryNe(mercuryStrength, academicPotential, jupiterAspectOnEducation)

        return EducationAnalysis(
            fourthLord = fourthLord,
            fifthLord = fifthLord,
            mercuryStrength = mercuryStrength,
            jupiterAspectOnEducation = jupiterAspectOnEducation,
            favorableSubjects = favorableSubjects,
            favorableSubjectsNe = favorableSubjectsNe,
            academicPotential = academicPotential,
            summaryEn = summaryEn,
            summaryNe = summaryNe
        )
    }

    private fun identifyFavorableSubjects(chart: VedicChart): Pair<List<String>, List<String>> {
        val subjects = mutableListOf<String>()
        val subjectsNe = mutableListOf<String>()

        val fifthHousePlanets = VedicAstrologyUtils.getPlanetsInHouse(chart, 5)
        val fourthHousePlanets = VedicAstrologyUtils.getPlanetsInHouse(chart, 4)
        val relevantPlanets = (fifthHousePlanets + fourthHousePlanets).map { it.planet }.distinct()

        if (relevantPlanets.isEmpty()) {
            // Use Mercury and Jupiter positions
            val mercuryPos = chart.planetPositions.find { it.planet == Planet.MERCURY }
            val mercurySign = mercuryPos?.sign
            when (getSignElement(mercurySign ?: ZodiacSign.GEMINI)) {
                Element.FIRE -> {
                    subjects.add("Leadership, Sports, Philosophy")
                    subjectsNe.add("नेतृत्व, खेलकुद, दर्शन")
                }
                Element.EARTH -> {
                    subjects.add("Commerce, Finance, Agriculture")
                    subjectsNe.add("वाणिज्य, वित्त, कृषि")
                }
                Element.AIR -> {
                    subjects.add("Communication, Languages, Technology")
                    subjectsNe.add("सञ्चार, भाषा, प्रविधि")
                }
                Element.WATER -> {
                    subjects.add("Psychology, Arts, Medicine")
                    subjectsNe.add("मनोविज्ञान, कला, चिकित्सा")
                }
            }
        } else {
            relevantPlanets.forEach { planet ->
                when (planet) {
                    Planet.SUN -> {
                        subjects.add("Political Science, Administration")
                        subjectsNe.add("राजनीति विज्ञान, प्रशासन")
                    }
                    Planet.MOON -> {
                        subjects.add("Psychology, Nursing, History")
                        subjectsNe.add("मनोविज्ञान, नर्सिङ, इतिहास")
                    }
                    Planet.MARS -> {
                        subjects.add("Engineering, Medicine, Sports")
                        subjectsNe.add("इन्जिनियरिङ, चिकित्सा, खेलकुद")
                    }
                    Planet.MERCURY -> {
                        subjects.add("Commerce, Mathematics, Languages")
                        subjectsNe.add("वाणिज्य, गणित, भाषा")
                    }
                    Planet.JUPITER -> {
                        subjects.add("Philosophy, Law, Teaching")
                        subjectsNe.add("दर्शन, कानून, शिक्षण")
                    }
                    Planet.VENUS -> {
                        subjects.add("Arts, Music, Design")
                        subjectsNe.add("कला, संगीत, डिजाइन")
                    }
                    Planet.SATURN -> {
                        subjects.add("Science, Research, Mining")
                        subjectsNe.add("विज्ञान, अनुसन्धान, खनन")
                    }
                    Planet.RAHU -> {
                        subjects.add("Technology, Foreign languages")
                        subjectsNe.add("प्रविधि, विदेशी भाषा")
                    }
                    Planet.KETU -> {
                        subjects.add("Spirituality, Occult, Research")
                        subjectsNe.add("आध्यात्मिकता, गुप्त विद्या, अनुसन्धान")
                    }
                    else -> {} // Handle outer planets if present
                }
            }
        }

        return Pair(subjects.distinct(), subjectsNe.distinct())
    }

    private fun calculateAcademicPotential(
        chart: VedicChart,
        fourthLord: Planet,
        fifthLord: Planet,
        mercuryStrength: StrengthLevel,
        jupiterAspect: Boolean
    ): StrengthLevel {
        var score = mercuryStrength.value.toDouble()

        // 5th lord strength
        val fifthLordPos = chart.planetPositions.find { it.planet == fifthLord }
        if (fifthLordPos != null) {
            val dignity = getDignity(fifthLordPos)
            score += when (dignity) {
                PlanetaryDignity.EXALTED -> 1.5
                PlanetaryDignity.OWN_SIGN -> 1.0
                PlanetaryDignity.DEBILITATED -> -1.0
                else -> 0.0
            }
        }

        if (jupiterAspect) score += 1.0

        // Check benefics in 5th house
        val beneficsIn5th = VedicAstrologyUtils.getPlanetsInHouse(chart, 5)
            .count { VedicAstrologyUtils.isNaturalBenefic(it.planet) }
        score += beneficsIn5th * 0.3

        return when {
            score >= 5.0 -> StrengthLevel.EXCELLENT
            score >= 4.0 -> StrengthLevel.STRONG
            score >= 3.0 -> StrengthLevel.MODERATE
            score >= 2.0 -> StrengthLevel.WEAK
            else -> StrengthLevel.AFFLICTED
        }
    }

    private fun buildEducationSummaryEn(
        mercuryStrength: StrengthLevel, potential: StrengthLevel, jupiterAspect: Boolean
    ): String {
        val jupiterText = if (jupiterAspect) "Jupiter's blessing on education houses enhances learning ability. " else ""
        return "${jupiterText}Mercury strength is ${mercuryStrength.displayName.lowercase()}, " +
               "indicating ${potential.displayName.lowercase()} academic potential and intellectual capacity."
    }

    private fun buildEducationSummaryNe(
        mercuryStrength: StrengthLevel, potential: StrengthLevel, jupiterAspect: Boolean
    ): String {
        val jupiterText = if (jupiterAspect) "शिक्षा भावहरूमा बृहस्पतिको आशीर्वादले सिक्ने क्षमता बढाउँछ। " else ""
        return "${jupiterText}बुधको शक्ति ${mercuryStrength.displayNameNe} छ, " +
               "${potential.displayNameNe} शैक्षिक क्षमता संकेत गर्दै।"
    }

    // ═══════════════════════════════════════════════════════════════════════════
    // SPIRITUAL ANALYSIS
    // ═══════════════════════════════════════════════════════════════════════════

    private fun analyzeSpirituality(chart: VedicChart): SpiritualAnalysis {
        val ninthLord = VedicAstrologyUtils.getHouseLord(chart, 9)
        val twelfthLord = VedicAstrologyUtils.getHouseLord(chart, 12)

        val ketuPos = chart.planetPositions.find { it.planet == Planet.KETU }
        val jupiterPos = chart.planetPositions.find { it.planet == Planet.JUPITER }
        val jupiterDignity = jupiterPos?.let { getDignity(it) } ?: PlanetaryDignity.NEUTRAL_SIGN
        val jupiterStrength = dignityToStrength(jupiterDignity)

        val spiritualInclination = calculateSpiritualInclination(chart, ninthLord, twelfthLord, ketuPos)

        val (practices, practicesNe) = recommendSpiritualPractices(chart)

        val summaryEn = buildSpiritualSummaryEn(jupiterStrength, spiritualInclination, ketuPos)
        val summaryNe = buildSpiritualSummaryNe(jupiterStrength, spiritualInclination, ketuPos)

        return SpiritualAnalysis(
            ninthLord = ninthLord,
            twelfthLord = twelfthLord,
            ketuPosition = ketuPos,
            jupiterStrength = jupiterStrength,
            spiritualInclination = spiritualInclination,
            recommendedPractices = practices,
            recommendedPracticesNe = practicesNe,
            summaryEn = summaryEn,
            summaryNe = summaryNe
        )
    }

    private fun calculateSpiritualInclination(
        chart: VedicChart,
        ninthLord: Planet,
        twelfthLord: Planet,
        ketuPos: PlanetPosition?
    ): StrengthLevel {
        var score = 3.0

        // 9th lord strength
        val ninthLordPos = chart.planetPositions.find { it.planet == ninthLord }
        if (ninthLordPos != null) {
            val dignity = getDignity(ninthLordPos)
            if (dignity.ordinal <= 2) score += 1.0
        }

        // Ketu in spiritual houses (9, 12, 4, 5)
        if (ketuPos != null && ketuPos.house in listOf(4, 5, 9, 12)) {
            score += 1.0
        }

        // Jupiter in good dignity
        val jupiterPos = chart.planetPositions.find { it.planet == Planet.JUPITER }
        if (jupiterPos != null && getDignity(jupiterPos).ordinal <= 2) {
            score += 0.5
        }

        // Planets in 12th house
        val planetsIn12th = VedicAstrologyUtils.getPlanetsInHouse(chart, 12)
        if (planetsIn12th.isNotEmpty()) score += 0.3

        return when {
            score >= 5.0 -> StrengthLevel.EXCELLENT
            score >= 4.0 -> StrengthLevel.STRONG
            score >= 3.0 -> StrengthLevel.MODERATE
            else -> StrengthLevel.WEAK
        }
    }

    private fun recommendSpiritualPractices(chart: VedicChart): Pair<List<String>, List<String>> {
        val practices = mutableListOf<String>()
        val practicesNe = mutableListOf<String>()

        val ascSign = VedicAstrologyUtils.getAscendantSign(chart)
        val moonPos = chart.planetPositions.find { it.planet == Planet.MOON }

        // Based on ascendant element
        when (getSignElement(ascSign)) {
            Element.FIRE -> {
                practices.add("Active meditation, Yoga, Pilgrimage")
                practicesNe.add("सक्रिय ध्यान, योग, तीर्थयात्रा")
            }
            Element.EARTH -> {
                practices.add("Seva (service), Mantra japa, Temple worship")
                practicesNe.add("सेवा, मन्त्र जप, मन्दिर पूजा")
            }
            Element.AIR -> {
                practices.add("Pranayama, Study of scriptures, Satsang")
                practicesNe.add("प्राणायाम, शास्त्र अध्ययन, सत्संग")
            }
            Element.WATER -> {
                practices.add("Bhakti yoga, Music, River/ocean meditation")
                practicesNe.add("भक्ति योग, संगीत, नदी/समुद्र ध्यान")
            }
        }

        // Based on Jupiter position
        val jupiterPos = chart.planetPositions.find { it.planet == Planet.JUPITER }
        if (jupiterPos != null && jupiterPos.house in listOf(1, 4, 5, 9, 12)) {
            practices.add("Guru upasana (devotion to teacher)")
            practicesNe.add("गुरु उपासना")
        }

        return Pair(practices.distinct(), practicesNe.distinct())
    }

    private fun buildSpiritualSummaryEn(
        jupiterStrength: StrengthLevel,
        inclination: StrengthLevel,
        ketuPos: PlanetPosition?
    ): String {
        val ketuText = if (ketuPos != null) "Ketu in ${ketuPos.house}th house indicates past life spiritual merit. " else ""
        return "${ketuText}Jupiter strength is ${jupiterStrength.displayName.lowercase()}, " +
               "with ${inclination.displayName.lowercase()} spiritual inclination. " +
               "Regular spiritual practice will bring inner peace and growth."
    }

    private fun buildSpiritualSummaryNe(
        jupiterStrength: StrengthLevel,
        inclination: StrengthLevel,
        ketuPos: PlanetPosition?
    ): String {
        val ketuText = if (ketuPos != null) "केतु ${ketuPos.house}औं भावमा छ जसले विगत जीवनको आध्यात्मिक पुण्य संकेत गर्छ। " else ""
        return "${ketuText}बृहस्पतिको शक्ति ${jupiterStrength.displayNameNe} छ, " +
               "${inclination.displayNameNe} आध्यात्मिक झुकावको साथ।"
    }

    // ═══════════════════════════════════════════════════════════════════════════
    // KEY STRENGTHS & CHALLENGES
    // ═══════════════════════════════════════════════════════════════════════════

    private fun identifyKeyStrengths(chart: VedicChart): List<TraitInfo> {
        val strengths = mutableListOf<TraitInfo>()

        // Check exalted planets
        chart.planetPositions.filter { getDignity(it) == PlanetaryDignity.EXALTED }.forEach { pos ->
            val trait = when (pos.planet) {
                Planet.SUN -> StringKeyNative.TRAIT_LEADERSHIP
                Planet.MOON -> StringKeyNative.TRAIT_INTUITION
                Planet.MARS -> StringKeyNative.TRAIT_DETERMINATION
                Planet.MERCURY -> StringKeyNative.TRAIT_COMMUNICATION
                Planet.JUPITER -> StringKeyNative.TRAIT_COMPASSION
                Planet.VENUS -> StringKeyNative.TRAIT_CREATIVITY
                Planet.SATURN -> StringKeyNative.TRAIT_PATIENCE
                else -> null
            }
            trait?.let {
                strengths.add(TraitInfo(it, StrengthLevel.EXCELLENT, pos.planet))
            }
        }

        // Check planets in own sign
        chart.planetPositions.filter { getDignity(it) == PlanetaryDignity.OWN_SIGN }.forEach { pos ->
            val trait = when (pos.planet) {
                Planet.SUN -> StringKeyNative.TRAIT_LEADERSHIP
                Planet.MOON -> StringKeyNative.TRAIT_INTUITION
                Planet.MARS -> StringKeyNative.TRAIT_DETERMINATION
                Planet.MERCURY -> StringKeyNative.TRAIT_ANALYTICAL
                Planet.JUPITER -> StringKeyNative.TRAIT_COMPASSION
                Planet.VENUS -> StringKeyNative.TRAIT_DIPLOMACY
                Planet.SATURN -> StringKeyNative.TRAIT_PRACTICALITY
                else -> null
            }
            trait?.let {
                if (strengths.none { s -> s.trait == it }) {
                    strengths.add(TraitInfo(it, StrengthLevel.STRONG, pos.planet))
                }
            }
        }

        // Based on ascendant
        val ascSign = VedicAstrologyUtils.getAscendantSign(chart)
        when (ascSign) {
            ZodiacSign.ARIES, ZodiacSign.LEO, ZodiacSign.SCORPIO ->
                strengths.add(TraitInfo(StringKeyNative.TRAIT_DETERMINATION, StrengthLevel.STRONG, null))
            ZodiacSign.GEMINI, ZodiacSign.VIRGO ->
                strengths.add(TraitInfo(StringKeyNative.TRAIT_ANALYTICAL, StrengthLevel.STRONG, null))
            ZodiacSign.LIBRA, ZodiacSign.PISCES ->
                strengths.add(TraitInfo(StringKeyNative.TRAIT_COMPASSION, StrengthLevel.STRONG, null))
            ZodiacSign.SAGITTARIUS, ZodiacSign.AQUARIUS ->
                strengths.add(TraitInfo(StringKeyNative.TRAIT_INDEPENDENCE, StrengthLevel.STRONG, null))
            else -> {}
        }

        return strengths.distinctBy { it.trait }.take(5)
    }

    private fun identifyKeyChallenges(chart: VedicChart): List<TraitInfo> {
        val challenges = mutableListOf<TraitInfo>()

        // Check debilitated planets
        chart.planetPositions.filter { getDignity(it) == PlanetaryDignity.DEBILITATED }.forEach { pos ->
            val challenge = when (pos.planet) {
                Planet.SUN -> StringKeyNative.CHALLENGE_PRIDE
                Planet.MOON -> StringKeyNative.CHALLENGE_OVERSENSITIVITY
                Planet.MARS -> StringKeyNative.CHALLENGE_IMPULSIVENESS
                Planet.MERCURY -> StringKeyNative.CHALLENGE_INDECISION
                Planet.JUPITER -> StringKeyNative.CHALLENGE_RIGIDITY
                Planet.VENUS -> StringKeyNative.CHALLENGE_DETACHMENT
                Planet.SATURN -> StringKeyNative.CHALLENGE_ANXIETY
                else -> null
            }
            challenge?.let {
                challenges.add(TraitInfo(it, StrengthLevel.WEAK, pos.planet))
            }
        }

        // Based on ascendant challenges
        val ascSign = VedicAstrologyUtils.getAscendantSign(chart)
        when (ascSign) {
            ZodiacSign.ARIES -> challenges.add(TraitInfo(StringKeyNative.CHALLENGE_IMPULSIVENESS, StrengthLevel.MODERATE, null))
            ZodiacSign.TAURUS -> challenges.add(TraitInfo(StringKeyNative.CHALLENGE_STUBBORNNESS, StrengthLevel.MODERATE, null))
            ZodiacSign.GEMINI -> challenges.add(TraitInfo(StringKeyNative.CHALLENGE_RESTLESSNESS, StrengthLevel.MODERATE, null))
            ZodiacSign.CANCER -> challenges.add(TraitInfo(StringKeyNative.CHALLENGE_OVERSENSITIVITY, StrengthLevel.MODERATE, null))
            ZodiacSign.LEO -> challenges.add(TraitInfo(StringKeyNative.CHALLENGE_PRIDE, StrengthLevel.MODERATE, null))
            ZodiacSign.VIRGO -> challenges.add(TraitInfo(StringKeyNative.CHALLENGE_CRITICISM, StrengthLevel.MODERATE, null))
            ZodiacSign.LIBRA -> challenges.add(TraitInfo(StringKeyNative.CHALLENGE_INDECISION, StrengthLevel.MODERATE, null))
            ZodiacSign.SCORPIO -> challenges.add(TraitInfo(StringKeyNative.CHALLENGE_INTENSITY, StrengthLevel.MODERATE, null))
            ZodiacSign.SAGITTARIUS -> challenges.add(TraitInfo(StringKeyNative.CHALLENGE_RESTLESSNESS, StrengthLevel.MODERATE, null))
            ZodiacSign.CAPRICORN -> challenges.add(TraitInfo(StringKeyNative.CHALLENGE_RIGIDITY, StrengthLevel.MODERATE, null))
            ZodiacSign.AQUARIUS -> challenges.add(TraitInfo(StringKeyNative.CHALLENGE_DETACHMENT, StrengthLevel.MODERATE, null))
            ZodiacSign.PISCES -> challenges.add(TraitInfo(StringKeyNative.CHALLENGE_ESCAPISM, StrengthLevel.MODERATE, null))
        }

        return challenges.distinctBy { it.trait }.take(5)
    }

    // ═══════════════════════════════════════════════════════════════════════════
    // UTILITY FUNCTIONS
    // ═══════════════════════════════════════════════════════════════════════════

    private fun getDignity(pos: PlanetPosition): PlanetaryDignity {
        return when {
            VedicAstrologyUtils.isExalted(pos) -> PlanetaryDignity.EXALTED
            VedicAstrologyUtils.isDebilitated(pos) -> PlanetaryDignity.DEBILITATED
            VedicAstrologyUtils.isInMoolatrikona(pos) -> PlanetaryDignity.MOOLATRIKONA
            VedicAstrologyUtils.isInOwnSign(pos) -> PlanetaryDignity.OWN_SIGN
            VedicAstrologyUtils.isInFriendSign(pos) -> PlanetaryDignity.FRIEND_SIGN
            VedicAstrologyUtils.isInEnemySign(pos) -> PlanetaryDignity.ENEMY_SIGN
            else -> PlanetaryDignity.NEUTRAL_SIGN
        }
    }

    private fun dignityToStrength(dignity: PlanetaryDignity): StrengthLevel {
        return when (dignity) {
            PlanetaryDignity.EXALTED -> StrengthLevel.EXCELLENT
            PlanetaryDignity.MOOLATRIKONA, PlanetaryDignity.OWN_SIGN -> StrengthLevel.STRONG
            PlanetaryDignity.FRIEND_SIGN, PlanetaryDignity.NEUTRAL_SIGN -> StrengthLevel.MODERATE
            PlanetaryDignity.ENEMY_SIGN -> StrengthLevel.WEAK
            PlanetaryDignity.DEBILITATED -> StrengthLevel.AFFLICTED
        }
    }

    private fun aspectsHouse(planetPos: PlanetPosition, targetHouse: Int, chart: VedicChart): Boolean {
        val planetHouse = planetPos.house
        val diff = ((targetHouse - planetHouse + 12) % 12)

        // All planets aspect 7th from their position
        if (diff == 6) return true

        // Special aspects
        return when (planetPos.planet) {
            Planet.MARS -> diff in listOf(3, 7) // 4th and 8th aspects
            Planet.JUPITER -> diff in listOf(4, 8) // 5th and 9th aspects
            Planet.SATURN -> diff in listOf(2, 9) // 3rd and 10th aspects
            Planet.RAHU, Planet.KETU -> diff in listOf(4, 8) // 5th and 9th aspects (like Jupiter)
            else -> false
        }
    }

    private fun calculateOverallScore(
        character: CharacterAnalysis,
        career: CareerAnalysis,
        marriage: MarriageAnalysis,
        health: HealthAnalysis,
        wealth: WealthAnalysis,
        education: EducationAnalysis
    ): Double {
        val scores = listOf(
            character.personalityStrength.value,
            career.careerStrength.value,
            marriage.relationshipStrength.value,
            health.ascendantStrength.value,
            wealth.wealthPotential.value,
            education.academicPotential.value
        )
        return (scores.average() * 20).coerceIn(0.0, 100.0)
    }
}
