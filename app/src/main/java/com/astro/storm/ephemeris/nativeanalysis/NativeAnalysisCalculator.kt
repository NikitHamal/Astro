package com.astro.storm.ephemeris.nativeanalysis

import com.astro.storm.core.common.Language
import com.astro.storm.core.common.StringKeyNative
import com.astro.storm.core.common.StringKeyNativeNarrative
import com.astro.storm.core.common.StringResources
import com.astro.storm.core.model.Planet
import com.astro.storm.core.model.PlanetPosition
import com.astro.storm.core.model.VedicChart
import com.astro.storm.core.model.ZodiacSign
import com.astro.storm.ephemeris.DivisionalChartCalculator
import com.astro.storm.ephemeris.VedicAstrologyUtils
import com.astro.storm.ephemeris.YogaCalculator
import com.astro.storm.ephemeris.nativeanalysis.NativeAnalysisHelpers.aspectsHouse
import com.astro.storm.ephemeris.nativeanalysis.NativeAnalysisHelpers.dignityToStrength
import com.astro.storm.ephemeris.nativeanalysis.NativeAnalysisHelpers.getDignity
import com.astro.storm.ephemeris.nativeanalysis.NativeAnalysisHelpers.getSignElement
import com.astro.storm.ephemeris.nativeanalysis.NativeAnalysisHelpers.getSignModality
import kotlin.math.roundToInt

object NativeAnalysisCalculator {

    fun analyzeNative(chart: VedicChart): NativeAnalysisResult {
        val character = analyzeCharacter(chart)
        val career = analyzeCareer(chart)
        val marriage = analyzeMarriage(chart)
        val health = analyzeHealth(chart)
        val wealth = analyzeWealth(chart)
        val education = analyzeEducation(chart)
        val spiritual = analyzeSpirituality(chart)
        val keyStrengths = identifyKeyStrengths(chart)
        val keyChallenges = identifyKeyChallenges(chart)
        val scores = listOf(
            character.personalityStrength.value,
            career.careerStrength.value,
            marriage.relationshipStrength.value,
            health.ascendantStrength.value,
            wealth.wealthPotential.value,
            education.academicPotential.value
        )
        return NativeAnalysisResult(
            character,
            career,
            marriage,
            health,
            wealth,
            education,
            spiritual,
            keyStrengths,
            keyChallenges,
            (scores.average() * 20).coerceIn(0.0, 100.0)
        )
    }

    private fun analyzeCharacter(chart: VedicChart): CharacterAnalysis {
        val asc = VedicAstrologyUtils.getAscendantSign(chart)
        val moon = chart.planetPositions.find { it.planet == Planet.MOON }
        val sun = chart.planetPositions.find { it.planet == Planet.SUN }
        val moonSign = moon?.sign ?: asc
        val sunSign = sun?.sign ?: asc
        val ascLord = chart.planetPositions.find { it.planet == asc.ruler }
        val nakEn = moon?.nakshatra?.getLocalizedName(Language.ENGLISH)
            ?: StringResources.get(StringKeyNative.LABEL_UNKNOWN, Language.ENGLISH)
        val nakNe = moon?.nakshatra?.getLocalizedName(Language.NEPALI)
            ?: StringResources.get(StringKeyNative.LABEL_UNKNOWN, Language.NEPALI)
        val personalityStrength = scoreToStrength(
            listOfNotNull(ascLord, moon, sun).map { evaluatePlanetScore(it) }.average()
        )
        val dominantElement = calculateDominantElement(chart)
        val dominantModality = calculateDominantModality(chart)

        return CharacterAnalysis(
            ascendantSign = asc,
            moonSign = moonSign,
            sunSign = sunSign,
            ascendantTrait = getAscendantTrait(asc),
            moonTrait = getMoonSignTrait(moonSign),
            nakshatraInfluence = StringResources.get(
                StringKeyNativeNarrative.CHARACTER_NAKSHATRA_NOTE,
                Language.ENGLISH,
                nakEn
            ),
            nakshatraInfluenceNe = StringResources.get(
                StringKeyNativeNarrative.CHARACTER_NAKSHATRA_NOTE,
                Language.NEPALI,
                nakNe
            ),
            personalityStrength = personalityStrength,
            dominantElement = dominantElement,
            dominantModality = dominantModality,
            summaryEn = buildCharacterSummary(asc, moonSign, sunSign, dominantElement, dominantModality, personalityStrength, Language.ENGLISH),
            summaryNe = buildCharacterSummary(asc, moonSign, sunSign, dominantElement, dominantModality, personalityStrength, Language.NEPALI)
        )
    }

    private fun analyzeCareer(chart: VedicChart): CareerAnalysis {
        val tenthLord = VedicAstrologyUtils.getHouseLord(chart, 10)
        val tenthLordPos = chart.planetPositions.find { it.planet == tenthLord }
        val tenthDignity = tenthLordPos?.let { getDignity(it) } ?: PlanetaryDignity.NEUTRAL_SIGN
        val tenthPlanets = VedicAstrologyUtils.getPlanetsInHouse(chart, 10).map { it.planet }
        val careerIndicators = buildCareerIndicators(tenthPlanets)
        val fieldsEn = buildCareerFields(tenthPlanets, Language.ENGLISH)
        val fieldsNe = buildCareerFields(tenthPlanets, Language.NEPALI)
        val careerStrength = scoreToStrength(evaluateHouseScore(chart, 10))

        return CareerAnalysis(
            tenthLord = tenthLord,
            tenthLordHouse = tenthLordPos?.house ?: 10,
            tenthLordDignity = tenthDignity,
            tenthHousePlanets = tenthPlanets,
            careerIndicators = careerIndicators,
            favorableFields = fieldsEn,
            favorableFieldsNe = fieldsNe,
            careerStrength = careerStrength,
            summaryEn = buildCareerSummary(chart, tenthLord, tenthLordPos, tenthDignity, tenthPlanets, careerStrength, Language.ENGLISH),
            summaryNe = buildCareerSummary(chart, tenthLord, tenthLordPos, tenthDignity, tenthPlanets, careerStrength, Language.NEPALI)
        )
    }

    private fun analyzeMarriage(chart: VedicChart): MarriageAnalysis {
        val seventhLord = VedicAstrologyUtils.getHouseLord(chart, 7)
        val seventhLordPos = chart.planetPositions.find { it.planet == seventhLord }
        val seventhDignity = seventhLordPos?.let { getDignity(it) } ?: PlanetaryDignity.NEUTRAL_SIGN
        val venusPos = chart.planetPositions.find { it.planet == Planet.VENUS }
        val venusStrength = dignityToStrength(venusPos?.let { getDignity(it) } ?: PlanetaryDignity.NEUTRAL_SIGN)
        val timing = determineMarriageTiming(seventhLordPos, venusPos)
        val spouseNature = buildSpouseNature(chart, Language.ENGLISH)
        val spouseNatureNe = buildSpouseNature(chart, Language.NEPALI)
        val relationshipStrength = scoreToStrength(evaluateHouseScore(chart, 7))

        return MarriageAnalysis(
            seventhLord = seventhLord,
            seventhLordHouse = seventhLordPos?.house ?: 7,
            seventhLordDignity = seventhDignity,
            venusPosition = venusPos,
            venusStrength = venusStrength,
            marriageTiming = timing,
            spouseNature = spouseNature,
            spouseNatureNe = spouseNatureNe,
            relationshipStrength = relationshipStrength,
            summaryEn = buildMarriageSummary(seventhLord, seventhLordPos, seventhDignity, venusStrength, timing, Language.ENGLISH),
            summaryNe = buildMarriageSummary(seventhLord, seventhLordPos, seventhDignity, venusStrength, timing, Language.NEPALI)
        )
    }

    private fun analyzeHealth(chart: VedicChart): HealthAnalysis {
        val asc = VedicAstrologyUtils.getAscendantSign(chart)
        val ascLordPos = chart.planetPositions.find { it.planet == asc.ruler }
        val ascStrength = scoreToStrength(evaluatePlanetScore(ascLordPos))
        val sixthLord = VedicAstrologyUtils.getHouseLord(chart, 6)
        val eighthLord = VedicAstrologyUtils.getHouseLord(chart, 8)
        val constitution = when (ascStrength) {
            StrengthLevel.EXCELLENT, StrengthLevel.STRONG -> ConstitutionType.STRONG
            StrengthLevel.MODERATE -> ConstitutionType.MODERATE
            else -> ConstitutionType.SENSITIVE
        }
        val longevity = when (ascStrength) {
            StrengthLevel.EXCELLENT, StrengthLevel.STRONG -> LongevityIndicator.LONG
            StrengthLevel.MODERATE -> LongevityIndicator.MEDIUM
            else -> LongevityIndicator.REQUIRES_CARE
        }
        val (concernsEn, concernsNe) = identifyHealthConcerns(asc)
        return HealthAnalysis(
            ascendantStrength = ascStrength,
            sixthLord = sixthLord,
            eighthLord = eighthLord,
            constitution = constitution,
            vulnerableAreas = getHealthAreasForSign(asc),
            longevityIndicator = longevity,
            healthConcerns = concernsEn,
            healthConcernsNe = concernsNe,
            summaryEn = buildHealthSummary(ascStrength, sixthLord, eighthLord, constitution, Language.ENGLISH),
            summaryNe = buildHealthSummary(ascStrength, sixthLord, eighthLord, constitution, Language.NEPALI)
        )
    }

    private fun analyzeWealth(chart: VedicChart): WealthAnalysis {
        val secondLord = VedicAstrologyUtils.getHouseLord(chart, 2)
        val eleventhLord = VedicAstrologyUtils.getHouseLord(chart, 11)
        val secondStrength = scoreToStrength(evaluatePlanetScore(chart.planetPositions.find { it.planet == secondLord }))
        val eleventhStrength = scoreToStrength(evaluatePlanetScore(chart.planetPositions.find { it.planet == eleventhLord }))
        val jupiterStrength = scoreToStrength(evaluatePlanetScore(chart.planetPositions.find { it.planet == Planet.JUPITER }))
        val dhanaYogaPresent = checkDhanaYoga(chart)
        val wealthPotential = scoreToStrength((evaluateHouseScore(chart, 2) + evaluateHouseScore(chart, 11)) / 2.0)
        val sourcesEn = buildWealthSources(chart, Language.ENGLISH)
        val sourcesNe = buildWealthSources(chart, Language.NEPALI)

        return WealthAnalysis(
            secondLord = secondLord,
            secondLordStrength = secondStrength,
            eleventhLord = eleventhLord,
            eleventhLordStrength = eleventhStrength,
            jupiterStrength = jupiterStrength,
            dhanaYogaPresent = dhanaYogaPresent,
            primarySources = sourcesEn,
            primarySourcesNe = sourcesNe,
            wealthPotential = wealthPotential,
            summaryEn = buildWealthSummary(secondLord, eleventhLord, dhanaYogaPresent, wealthPotential, Language.ENGLISH),
            summaryNe = buildWealthSummary(secondLord, eleventhLord, dhanaYogaPresent, wealthPotential, Language.NEPALI)
        )
    }

    private fun analyzeEducation(chart: VedicChart): EducationAnalysis {
        val fourthLord = VedicAstrologyUtils.getHouseLord(chart, 4)
        val fifthLord = VedicAstrologyUtils.getHouseLord(chart, 5)
        val mercuryStrength = scoreToStrength(evaluatePlanetScore(chart.planetPositions.find { it.planet == Planet.MERCURY }))
        val jupiterAspectOnEducation = chart.planetPositions.any { it.planet == Planet.JUPITER && aspectsHouse(it, 5, chart) }
        val academicPotential = scoreToStrength((evaluateHouseScore(chart, 4) + evaluateHouseScore(chart, 5)) / 2.0)
        val subjectsEn = buildEducationSubjects(chart, Language.ENGLISH)
        val subjectsNe = buildEducationSubjects(chart, Language.NEPALI)

        return EducationAnalysis(
            fourthLord = fourthLord,
            fifthLord = fifthLord,
            mercuryStrength = mercuryStrength,
            jupiterAspectOnEducation = jupiterAspectOnEducation,
            favorableSubjects = subjectsEn,
            favorableSubjectsNe = subjectsNe,
            academicPotential = academicPotential,
            summaryEn = buildEducationSummary(fourthLord, fifthLord, mercuryStrength, academicPotential, jupiterAspectOnEducation, Language.ENGLISH),
            summaryNe = buildEducationSummary(fourthLord, fifthLord, mercuryStrength, academicPotential, jupiterAspectOnEducation, Language.NEPALI)
        )
    }

    private fun analyzeSpirituality(chart: VedicChart): SpiritualAnalysis {
        val ninthLord = VedicAstrologyUtils.getHouseLord(chart, 9)
        val twelfthLord = VedicAstrologyUtils.getHouseLord(chart, 12)
        val ketuPos = chart.planetPositions.find { it.planet == Planet.KETU }
        val jupiterStrength = scoreToStrength(evaluatePlanetScore(chart.planetPositions.find { it.planet == Planet.JUPITER }))
        val spiritualInclination = scoreToStrength(evaluateHouseScore(chart, 12))
        val practicesEn = buildSpiritualPractices(chart, Language.ENGLISH)
        val practicesNe = buildSpiritualPractices(chart, Language.NEPALI)
        val hasTwelfthActivity = chart.planetPositions.any { it.house == 12 }

        return SpiritualAnalysis(
            ninthLord = ninthLord,
            twelfthLord = twelfthLord,
            ketuPosition = ketuPos,
            jupiterStrength = jupiterStrength,
            spiritualInclination = spiritualInclination,
            recommendedPractices = practicesEn,
            recommendedPracticesNe = practicesNe,
            summaryEn = buildSpiritualSummary(ninthLord, twelfthLord, spiritualInclination, hasTwelfthActivity, ketuPos != null, Language.ENGLISH),
            summaryNe = buildSpiritualSummary(ninthLord, twelfthLord, spiritualInclination, hasTwelfthActivity, ketuPos != null, Language.NEPALI)
        )
    }

    private fun buildCharacterSummary(
        asc: ZodiacSign,
        moon: ZodiacSign,
        sun: ZodiacSign,
        element: Element,
        modality: Modality,
        strength: StrengthLevel,
        language: Language
    ): String {
        return listOf(
            StringResources.get(StringKeyNativeNarrative.CHARACTER_SUMMARY_CORE, language, asc.getLocalizedName(language), moon.getLocalizedName(language), sun.getLocalizedName(language)),
            StringResources.get(StringKeyNativeNarrative.CHARACTER_ELEMENTAL_NOTE, language, element.getLocalizedName(language)),
            StringResources.get(StringKeyNativeNarrative.CHARACTER_MODALITY_NOTE, language, modality.getLocalizedName(language)),
            StringResources.get(StringKeyNativeNarrative.CHARACTER_STRENGTH_NOTE, language, strength.getLocalizedName(language))
        ).joinToString("\n\n")
    }

    private fun buildCareerSummary(
        chart: VedicChart,
        lord: Planet,
        lordPos: PlanetPosition?,
        dignity: PlanetaryDignity,
        tenthPlanets: List<Planet>,
        strength: StrengthLevel,
        language: Language
    ): String {
        val base = StringResources.get(
            StringKeyNativeNarrative.CAREER_SUMMARY_CORE,
            language,
            lord.getLocalizedName(language),
            lordPos?.house ?: 10,
            dignity.getLocalizedName(language)
        )
        val statusKey = when (strength) {
            StrengthLevel.EXCELLENT, StrengthLevel.STRONG -> StringKeyNative.CAREER_10TH_STATUS_EXCELLENT
            StrengthLevel.MODERATE -> StringKeyNative.CAREER_10TH_STATUS_GOOD
            else -> StringKeyNative.CAREER_10TH_STATUS_CHALLENGING
        }
        val status = StringResources.get(
            StringKeyNative.CAREER_10TH_LORD_STATUS,
            language,
            lord.getLocalizedName(language),
            lordPos?.house ?: 10,
            StringResources.get(statusKey, language)
        )
        val planetNote = if (tenthPlanets.isNotEmpty()) {
            StringResources.get(
                StringKeyNativeNarrative.CAREER_PLANETS_NOTE,
                language,
                tenthPlanets.joinToString { it.getLocalizedName(language) }
            )
        } else {
            StringResources.get(StringKeyNative.CAREER_10TH_LORD_FOCUS, language)
        }
        val strengthNote = StringResources.get(
            StringKeyNativeNarrative.CAREER_STRENGTH_NOTE,
            language,
            strength.getLocalizedName(language)
        )
        return listOf(base, status, planetNote, strengthNote).joinToString("\n\n")
    }

    private fun buildMarriageSummary(
        lord: Planet,
        lordPos: PlanetPosition?,
        dignity: PlanetaryDignity,
        venusStrength: StrengthLevel,
        timing: MarriageTiming,
        language: Language
    ): String {
        val base = StringResources.get(
            StringKeyNativeNarrative.MARRIAGE_SUMMARY_CORE,
            language,
            lord.getLocalizedName(language),
            lordPos?.house ?: 7,
            dignity.getLocalizedName(language)
        )
        val venusNote = StringResources.get(
            StringKeyNativeNarrative.MARRIAGE_VENUS_NOTE,
            language,
            venusStrength.getLocalizedName(language)
        )
        val timingNote = StringResources.get(
            StringKeyNativeNarrative.MARRIAGE_TIMING_NOTE,
            language,
            timing.getLocalizedName(language)
        )
        return listOf(base, venusNote, timingNote).joinToString("\n\n")
    }

    private fun buildHealthSummary(
        strength: StrengthLevel,
        sixthLord: Planet,
        eighthLord: Planet,
        constitution: ConstitutionType,
        language: Language
    ): String {
        val overview = StringResources.get(StringKeyNative.HEALTH_OVERVIEW, language)
        val base = StringResources.get(
            StringKeyNativeNarrative.HEALTH_SUMMARY_CORE,
            language,
            strength.getLocalizedName(language)
        )
        val lordNote = StringResources.get(
            StringKeyNativeNarrative.HEALTH_LORDS_NOTE,
            language,
            sixthLord.getLocalizedName(language),
            eighthLord.getLocalizedName(language)
        )
        val constitutionNote = StringResources.get(
            StringKeyNativeNarrative.HEALTH_CONSTITUTION_NOTE,
            language,
            constitution.getLocalizedName(language)
        )
        val constitutionDetail = when (constitution) {
            ConstitutionType.STRONG -> StringResources.get(StringKeyNative.HEALTH_CONSTITUTION_STRONG, language)
            ConstitutionType.MODERATE -> StringResources.get(StringKeyNative.HEALTH_CONSTITUTION_MODERATE, language)
            ConstitutionType.SENSITIVE -> StringResources.get(StringKeyNative.HEALTH_CONSTITUTION_SENSITIVE, language)
        }
        return listOf(overview, base, lordNote, constitutionNote, constitutionDetail).joinToString("\n\n")
    }

    private fun buildWealthSummary(
        secondLord: Planet,
        eleventhLord: Planet,
        dhanaYoga: Boolean,
        potential: StrengthLevel,
        language: Language
    ): String {
        val overview = StringResources.get(StringKeyNative.WEALTH_OVERVIEW, language)
        val base = StringResources.get(
            StringKeyNativeNarrative.WEALTH_SUMMARY_CORE,
            language,
            secondLord.getLocalizedName(language),
            eleventhLord.getLocalizedName(language)
        )
        val dhanaNote = StringResources.get(
            StringKeyNativeNarrative.WEALTH_DHANA_NOTE,
            language,
            if (dhanaYoga) StringResources.get(com.astro.storm.core.common.StringKeyAnalysis.UI_YES, language)
            else StringResources.get(com.astro.storm.core.common.StringKeyAnalysis.UI_NO, language)
        )
        val strengthNote = StringResources.get(
            StringKeyNativeNarrative.WEALTH_STRENGTH_NOTE,
            language,
            potential.getLocalizedName(language)
        )
        val dhanaDetail = if (dhanaYoga) StringResources.get(StringKeyNative.WEALTH_DHANA_YOGA, language) else ""
        return listOf(overview, base, dhanaNote, strengthNote, dhanaDetail).filter { it.isNotBlank() }.joinToString("\n\n")
    }

    private fun buildEducationSummary(
        fourthLord: Planet,
        fifthLord: Planet,
        mercuryStrength: StrengthLevel,
        academicPotential: StrengthLevel,
        jupiterAspectOnEducation: Boolean,
        language: Language
    ): String {
        val overview = StringResources.get(StringKeyNative.EDUCATION_OVERVIEW, language)
        val base = StringResources.get(
            StringKeyNativeNarrative.EDUCATION_SUMMARY_CORE,
            language,
            fourthLord.getLocalizedName(language),
            fifthLord.getLocalizedName(language)
        )
        val mercuryNote = StringResources.get(
            StringKeyNativeNarrative.EDUCATION_MERCURY_NOTE,
            language,
            mercuryStrength.getLocalizedName(language)
        )
        val potentialNote = StringResources.get(
            StringKeyNativeNarrative.EDUCATION_POTENTIAL_NOTE,
            language,
            academicPotential.getLocalizedName(language)
        )
        val jupiterNote = if (jupiterAspectOnEducation) {
            StringResources.get(StringKeyNative.EDUCATION_JUPITER_ASPECT, language)
        } else ""
        val fifthNote = if (academicPotential.value >= StrengthLevel.STRONG.value) {
            StringResources.get(StringKeyNative.EDUCATION_5TH_STRONG, language)
        } else ""
        return listOf(overview, base, mercuryNote, potentialNote, jupiterNote, fifthNote).filter { it.isNotBlank() }.joinToString("\n\n")
    }

    private fun buildSpiritualSummary(
        ninthLord: Planet,
        twelfthLord: Planet,
        inclination: StrengthLevel,
        hasTwelfthActivity: Boolean,
        hasKetu: Boolean,
        language: Language
    ): String {
        val base = StringResources.get(
            StringKeyNativeNarrative.SPIRITUAL_SUMMARY_CORE,
            language,
            ninthLord.getLocalizedName(language),
            twelfthLord.getLocalizedName(language)
        )
        val inclinationNote = StringResources.get(
            StringKeyNativeNarrative.SPIRITUAL_INCLINATION_NOTE,
            language,
            inclination.getLocalizedName(language)
        )
        val ketuNote = if (hasKetu) StringResources.get(StringKeyNative.SPIRITUAL_KETU_INFLUENCE, language) else ""
        val twelfthNote = if (hasTwelfthActivity) StringResources.get(StringKeyNative.SPIRITUAL_12TH_ACTIVE, language) else ""
        return listOf(base, inclinationNote, ketuNote, twelfthNote).filter { it.isNotBlank() }.joinToString("\n\n")
    }

    private fun buildCareerIndicators(planets: List<Planet>): List<StringKeyNative> {
        return planets.mapNotNull { planet ->
            when (planet) {
                Planet.SUN -> StringKeyNative.CAREER_SUN_STRONG
                Planet.MARS -> StringKeyNative.CAREER_MARS_STRONG
                Planet.MERCURY -> StringKeyNative.CAREER_MERCURY_STRONG
                Planet.JUPITER -> StringKeyNative.CAREER_JUPITER_STRONG
                Planet.VENUS -> StringKeyNative.CAREER_VENUS_STRONG
                Planet.SATURN -> StringKeyNative.CAREER_SATURN_STRONG
                else -> null
            }
        }.distinct()
    }

    private fun buildCareerFields(planets: List<Planet>, language: Language): List<String> {
        if (planets.isEmpty()) {
            return listOf(StringResources.get(StringKeyNative.CAREER_FIELD_GENERAL, language))
        }
        val fields = planets.mapNotNull { planet ->
            when (planet) {
                Planet.SUN -> StringKeyNative.CAREER_FIELD_LEADERSHIP
                Planet.MERCURY -> StringKeyNative.CAREER_FIELD_COMMUNICATION
                Planet.JUPITER -> StringKeyNative.CAREER_FIELD_ADVISORY
                Planet.VENUS -> StringKeyNative.CAREER_FIELD_CREATIVE
                Planet.SATURN -> StringKeyNative.CAREER_FIELD_ENGINEERING
                Planet.MARS -> StringKeyNative.CAREER_FIELD_ACTION
                Planet.MOON -> StringKeyNative.CAREER_FIELD_PUBLIC
                else -> null
            }
        }.distinct()
        return fields.map { StringResources.get(it, language) }
    }

    private fun buildWealthSources(chart: VedicChart, language: Language): List<String> {
        val sources = mutableListOf<StringKeyNative>()
        val secondHousePlanets = VedicAstrologyUtils.getPlanetsInHouse(chart, 2)
        val eleventhHousePlanets = VedicAstrologyUtils.getPlanetsInHouse(chart, 11)
        if (secondHousePlanets.any { it.planet == Planet.MERCURY } || eleventhHousePlanets.any { it.planet == Planet.MERCURY }) {
            sources.add(StringKeyNative.WEALTH_SOURCE_BUSINESS)
        }
        if (secondHousePlanets.any { it.planet == Planet.JUPITER } || eleventhHousePlanets.any { it.planet == Planet.JUPITER }) {
            sources.add(StringKeyNative.WEALTH_SOURCE_ADVISORY)
        }
        if (secondHousePlanets.any { it.planet == Planet.VENUS } || eleventhHousePlanets.any { it.planet == Planet.VENUS }) {
            sources.add(StringKeyNative.WEALTH_SOURCE_LUXURY)
        }
        if (sources.isEmpty()) sources.add(StringKeyNative.WEALTH_SOURCE_MULTIPLE)
        return sources.map { StringResources.get(it, language) }
    }

    private fun buildEducationSubjects(chart: VedicChart, language: Language): List<String> {
        val subjects = mutableListOf<StringKeyNative>()
        if (chart.planetPositions.any { it.planet == Planet.MERCURY }) subjects.add(StringKeyNative.EDU_SUBJECT_COMMERCE)
        if (chart.planetPositions.any { it.planet == Planet.JUPITER }) subjects.add(StringKeyNative.EDU_SUBJECT_PHILOSOPHY)
        if (chart.planetPositions.any { it.planet == Planet.MARS }) subjects.add(StringKeyNative.EDU_SUBJECT_TECHNICAL)
        if (chart.planetPositions.any { it.planet == Planet.VENUS }) subjects.add(StringKeyNative.EDU_SUBJECT_ARTS)
        if (subjects.isEmpty()) subjects.add(StringKeyNative.EDU_SUBJECT_GENERAL)
        return subjects.distinct().map { StringResources.get(it, language) }
    }

    private fun buildSpiritualPractices(chart: VedicChart, language: Language): List<String> {
        val practices = mutableListOf<StringKeyNative>()
        val ketuHouse = chart.planetPositions.find { it.planet == Planet.KETU }?.house
        if (ketuHouse == 12 || ketuHouse == 8) practices.add(StringKeyNative.SPIRITUAL_PRACTICE_MEDITATION)
        if (chart.planetPositions.any { it.planet == Planet.JUPITER }) practices.add(StringKeyNative.SPIRITUAL_PRACTICE_STUDY)
        if (chart.planetPositions.any { it.planet == Planet.MOON }) practices.add(StringKeyNative.SPIRITUAL_PRACTICE_DEVOTION)
        if (practices.isEmpty()) practices.add(StringKeyNative.SPIRITUAL_PRACTICE_REFLECTION)
        return practices.map { StringResources.get(it, language) }
    }

    private fun buildSpouseNature(chart: VedicChart, language: Language): String {
        val seventhSign = VedicAstrologyUtils.getHouseSign(chart, 7)
        val element = getSignElement(seventhSign)
        val modality = getSignModality(seventhSign)
        return StringResources.get(
            StringKeyNative.SPOUSE_NATURE_TEMPLATE,
            language,
            element.getLocalizedName(language),
            modality.getLocalizedName(language)
        )
    }

    private fun determineMarriageTiming(seventhLord: PlanetPosition?, venus: PlanetPosition?): MarriageTiming {
        val house = seventhLord?.house ?: 7
        return when {
            house in listOf(1, 2, 4, 7, 11) && (venus?.house ?: 0) in listOf(1, 5, 7) -> MarriageTiming.EARLY
            house in listOf(6, 8, 12) || (venus?.house ?: 0) in listOf(6, 8, 12) -> MarriageTiming.DELAYED
            else -> MarriageTiming.NORMAL
        }
    }

    private fun identifyHealthConcerns(asc: ZodiacSign): Pair<List<String>, List<String>> {
        val key = when (asc) {
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
        return listOf(StringResources.get(key, Language.ENGLISH)) to listOf(StringResources.get(key, Language.NEPALI))
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

    private fun checkDhanaYoga(chart: VedicChart): Boolean {
        val secondLord = VedicAstrologyUtils.getHouseLord(chart, 2)
        val eleventhLord = VedicAstrologyUtils.getHouseLord(chart, 11)
        val secondLordPos = chart.planetPositions.find { it.planet == secondLord }
        val eleventhLordPos = chart.planetPositions.find { it.planet == eleventhLord }
        val secondHousePlanets = VedicAstrologyUtils.getPlanetsInHouse(chart, 2)
        val eleventhHousePlanets = VedicAstrologyUtils.getPlanetsInHouse(chart, 11)
        val wealthPlanets = setOf(Planet.JUPITER, Planet.VENUS, Planet.MERCURY)

        val lordConnection = listOfNotNull(secondLordPos, eleventhLordPos)
            .any { VedicAstrologyUtils.isKendra(it.house) || VedicAstrologyUtils.isTrikona(it.house) }

        val beneficSupport = (secondHousePlanets + eleventhHousePlanets)
            .any { it.planet in wealthPlanets }

        val yogaFromYogas = YogaCalculator.calculateYogas(chart).dhanaYogas.isNotEmpty()

        return lordConnection && beneficSupport && yogaFromYogas
    }

    private fun evaluateHouseScore(chart: VedicChart, house: Int): Double {
        val lord = VedicAstrologyUtils.getHouseLord(chart, house)
        val lordPos = chart.planetPositions.find { it.planet == lord }
        val lordScore = evaluatePlanetScore(lordPos)
        val beneficPlanets = chart.planetPositions.count { it.house == house && VedicAstrologyUtils.isNaturalBenefic(it.planet) }
        val maleficPlanets = chart.planetPositions.count { it.house == house && !VedicAstrologyUtils.isNaturalBenefic(it.planet) }
        val aspectSupport = chart.planetPositions.count { VedicAstrologyUtils.isNaturalBenefic(it.planet) && aspectsHouse(it, house, chart) }
        val aspectPressure = chart.planetPositions.count { !VedicAstrologyUtils.isNaturalBenefic(it.planet) && aspectsHouse(it, house, chart) }

        return (lordScore + beneficPlanets * 0.4 - maleficPlanets * 0.4 + aspectSupport * 0.3 - aspectPressure * 0.3)
            .coerceIn(1.0, 5.0)
    }

    private fun evaluatePlanetScore(position: PlanetPosition?): Double {
        if (position == null) return 3.0
        val dignity = getDignity(position)
        var score = when (dignity) {
            PlanetaryDignity.EXALTED -> 5.0
            PlanetaryDignity.MOOLATRIKONA -> 4.7
            PlanetaryDignity.OWN_SIGN -> 4.3
            PlanetaryDignity.FRIEND_SIGN -> 3.8
            PlanetaryDignity.NEUTRAL_SIGN -> 3.2
            PlanetaryDignity.ENEMY_SIGN -> 2.6
            PlanetaryDignity.DEBILITATED -> 2.0
        }
        if (VedicAstrologyUtils.isKendra(position.house) || VedicAstrologyUtils.isTrikona(position.house)) score += 0.3
        if (VedicAstrologyUtils.isDusthana(position.house)) score -= 0.3
        if (position.isRetrograde) score += 0.2
        val navamsaSign = DivisionalChartCalculator.getNavamsaSign(position.longitude)
        if (navamsaSign.ruler == position.planet) score += 0.3
        return score.coerceIn(1.0, 5.0)
    }

    private fun scoreToStrength(score: Double): StrengthLevel {
        return when {
            score >= 4.6 -> StrengthLevel.EXCELLENT
            score >= 4.0 -> StrengthLevel.STRONG
            score >= 3.2 -> StrengthLevel.MODERATE
            score >= 2.4 -> StrengthLevel.WEAK
            else -> StrengthLevel.AFFLICTED
        }
    }

    private fun calculateDominantElement(chart: VedicChart): Element {
        return chart.planetPositions
            .groupBy { getSignElement(it.sign) }
            .maxByOrNull { it.value.size }
            ?.key ?: Element.FIRE
    }

    private fun calculateDominantModality(chart: VedicChart): Modality {
        return chart.planetPositions
            .groupBy { getSignModality(it.sign) }
            .maxByOrNull { it.value.size }
            ?.key ?: Modality.CARDINAL
    }

    private fun getAscendantTrait(sign: ZodiacSign): StringKeyNative = when (sign) {
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

    private fun getMoonSignTrait(sign: ZodiacSign): StringKeyNative = when (sign) {
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

    private fun identifyKeyStrengths(chart: VedicChart): List<TraitInfo> {
        val strongestPlanets = chart.planetPositions
            .sortedByDescending { evaluatePlanetScore(it) }
            .take(3)
            .map { it.planet }
        val traits = mutableListOf<TraitInfo>()
        strongestPlanets.forEach { planet ->
            when (planet) {
                Planet.SUN -> traits.add(TraitInfo(StringKeyNative.TRAIT_LEADERSHIP, StrengthLevel.STRONG, planet))
                Planet.MOON -> traits.add(TraitInfo(StringKeyNative.TRAIT_COMPASSION, StrengthLevel.STRONG, planet))
                Planet.MERCURY -> traits.add(TraitInfo(StringKeyNative.TRAIT_COMMUNICATION, StrengthLevel.STRONG, planet))
                Planet.JUPITER -> traits.add(TraitInfo(StringKeyNative.TRAIT_WISDOM, StrengthLevel.STRONG, planet))
                Planet.VENUS -> traits.add(TraitInfo(StringKeyNative.TRAIT_CREATIVITY, StrengthLevel.STRONG, planet))
                Planet.MARS -> traits.add(TraitInfo(StringKeyNative.TRAIT_DETERMINATION, StrengthLevel.STRONG, planet))
                Planet.SATURN -> traits.add(TraitInfo(StringKeyNative.TRAIT_PATIENCE, StrengthLevel.STRONG, planet))
                else -> {}
            }
        }
        if (traits.isEmpty()) traits.add(TraitInfo(StringKeyNative.TRAIT_PRACTICALITY, StrengthLevel.MODERATE, null))
        return traits
    }

    private fun identifyKeyChallenges(chart: VedicChart): List<TraitInfo> {
        val weakestPlanets = chart.planetPositions
            .sortedBy { evaluatePlanetScore(it) }
            .take(2)
            .map { it.planet }
        val challenges = mutableListOf<TraitInfo>()
        weakestPlanets.forEach { planet ->
            when (planet) {
                Planet.MARS -> challenges.add(TraitInfo(StringKeyNative.CHALLENGE_IMPULSIVENESS, StrengthLevel.MODERATE, planet))
                Planet.MOON -> challenges.add(TraitInfo(StringKeyNative.CHALLENGE_OVERSENSITIVITY, StrengthLevel.MODERATE, planet))
                Planet.MERCURY -> challenges.add(TraitInfo(StringKeyNative.CHALLENGE_INDECISION, StrengthLevel.MODERATE, planet))
                Planet.VENUS -> challenges.add(TraitInfo(StringKeyNative.CHALLENGE_ESCAPISM, StrengthLevel.WEAK, planet))
                Planet.SATURN -> challenges.add(TraitInfo(StringKeyNative.CHALLENGE_RIGIDITY, StrengthLevel.WEAK, planet))
                Planet.RAHU -> challenges.add(TraitInfo(StringKeyNative.CHALLENGE_RESTLESSNESS, StrengthLevel.WEAK, planet))
                Planet.KETU -> challenges.add(TraitInfo(StringKeyNative.CHALLENGE_DETACHMENT, StrengthLevel.WEAK, planet))
                else -> {}
            }
        }
        if (challenges.isEmpty()) challenges.add(TraitInfo(StringKeyNative.CHALLENGE_ANXIETY, StrengthLevel.MODERATE, null))
        return challenges
    }
}
