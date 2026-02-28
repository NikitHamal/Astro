package com.astro.vajra.ephemeris.nativeanalysis

import com.astro.vajra.core.common.StringKeyNative
import com.astro.vajra.core.model.Planet
import com.astro.vajra.core.model.VedicChart
import com.astro.vajra.core.model.ZodiacSign
import com.astro.vajra.ephemeris.VedicAstrologyUtils
import com.astro.vajra.ephemeris.nativeanalysis.NativeAnalysisHelpers.aspectsHouse
import com.astro.vajra.ephemeris.nativeanalysis.NativeAnalysisHelpers.dignityToStrength
import com.astro.vajra.ephemeris.nativeanalysis.NativeAnalysisHelpers.getDignity
import com.astro.vajra.ephemeris.nativeanalysis.NativeAnalysisHelpers.getSignElement
import com.astro.vajra.ephemeris.nativeanalysis.NativeAnalysisHelpers.getSignModality

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
        val scores = listOf(character.personalityStrength.value, career.careerStrength.value, marriage.relationshipStrength.value, health.ascendantStrength.value, wealth.wealthPotential.value, education.academicPotential.value)
        return NativeAnalysisResult(character, career, marriage, health, wealth, education, spiritual, keyStrengths, keyChallenges, (scores.average() * 20).coerceIn(0.0, 100.0))
    }

    private fun analyzeCharacter(chart: VedicChart): CharacterAnalysis {
        val asc = VedicAstrologyUtils.getAscendantSign(chart)
        val moon = chart.planetPositions.find { it.planet == Planet.MOON }
        val ms = moon?.sign ?: asc
        val d = moon?.let { getDignity(chart.planetPositions.find { it.planet == asc.ruler } ?: it) } ?: PlanetaryDignity.NEUTRAL_SIGN
        val s = when { d == PlanetaryDignity.EXALTED -> StrengthLevel.EXCELLENT; d == PlanetaryDignity.OWN_SIGN || d == PlanetaryDignity.MOOLATRIKONA -> StrengthLevel.STRONG; d == PlanetaryDignity.DEBILITATED -> StrengthLevel.AFFLICTED; else -> StrengthLevel.MODERATE }
        val el = calculateDominantElement(chart)
        val nakName = moon?.nakshatra?.displayName ?: "Unknown"
        return CharacterAnalysis(asc, ms, chart.planetPositions.find { it.planet == Planet.SUN }?.sign ?: asc, getAscendantTrait(asc), getMoonSignTrait(ms), "Your birth nakshatra $nakName shapes your deeper personality traits.", "तपाईंको जन्म नक्षत्र $nakName ले तपाईंको गहिरो व्यक्तित्व विशेषता आकार दिन्छ।", s, el, calculateDominantModality(chart), buildCharacterSummaryEn(asc, ms, s, el), buildCharacterSummaryNe(asc, ms, s, el))
    }

    private fun getAscendantTrait(sign: ZodiacSign): StringKeyNative = when (sign) { ZodiacSign.ARIES -> StringKeyNative.CHAR_ARIES_ASC; ZodiacSign.TAURUS -> StringKeyNative.CHAR_TAURUS_ASC; ZodiacSign.GEMINI -> StringKeyNative.CHAR_GEMINI_ASC; ZodiacSign.CANCER -> StringKeyNative.CHAR_CANCER_ASC; ZodiacSign.LEO -> StringKeyNative.CHAR_LEO_ASC; ZodiacSign.VIRGO -> StringKeyNative.CHAR_VIRGO_ASC; ZodiacSign.LIBRA -> StringKeyNative.CHAR_LIBRA_ASC; ZodiacSign.SCORPIO -> StringKeyNative.CHAR_SCORPIO_ASC; ZodiacSign.SAGITTARIUS -> StringKeyNative.CHAR_SAGITTARIUS_ASC; ZodiacSign.CAPRICORN -> StringKeyNative.CHAR_CAPRICORN_ASC; ZodiacSign.AQUARIUS -> StringKeyNative.CHAR_AQUARIUS_ASC; ZodiacSign.PISCES -> StringKeyNative.CHAR_PISCES_ASC }
    private fun getMoonSignTrait(sign: ZodiacSign): StringKeyNative = when (sign) { ZodiacSign.ARIES -> StringKeyNative.MOON_ARIES; ZodiacSign.TAURUS -> StringKeyNative.MOON_TAURUS; ZodiacSign.GEMINI -> StringKeyNative.MOON_GEMINI; ZodiacSign.CANCER -> StringKeyNative.MOON_CANCER; ZodiacSign.LEO -> StringKeyNative.MOON_LEO; ZodiacSign.VIRGO -> StringKeyNative.MOON_VIRGO; ZodiacSign.LIBRA -> StringKeyNative.MOON_LIBRA; ZodiacSign.SCORPIO -> StringKeyNative.MOON_SCORPIO; ZodiacSign.SAGITTARIUS -> StringKeyNative.MOON_SAGITTARIUS; ZodiacSign.CAPRICORN -> StringKeyNative.MOON_CAPRICORN; ZodiacSign.AQUARIUS -> StringKeyNative.MOON_AQUARIUS; ZodiacSign.PISCES -> StringKeyNative.MOON_PISCES }
    private fun calculateDominantElement(chart: VedicChart): Element = chart.planetPositions.groupBy { getSignElement(it.sign) }.maxByOrNull { it.value.size }?.key ?: Element.FIRE
    private fun calculateDominantModality(chart: VedicChart): Modality = chart.planetPositions.groupBy { getSignModality(it.sign) }.maxByOrNull { it.value.size }?.key ?: Modality.CARDINAL
    private fun buildCharacterSummaryEn(a: ZodiacSign, m: ZodiacSign, s: StrengthLevel, e: Element): String = "With ${a.displayName} Ascendant and Moon in ${m.displayName}, you have a ${e.displayName} dominant nature. Personality is ${s.displayName.lowercase()}."
    private fun buildCharacterSummaryNe(a: ZodiacSign, m: ZodiacSign, s: StrengthLevel, e: Element): String = "${a.displayName} लग्न र ${m.displayName} मा चन्द्रमाको साथ, तपाईंको स्वभाव ${e.displayNameNe} प्रधान र आधार ${s.displayNameNe} छ।"

    private fun analyzeCareer(chart: VedicChart): CareerAnalysis {
        val lord = VedicAstrologyUtils.getHouseLord(chart, 10); val pos = chart.planetPositions.find { it.planet == lord }
        val d = pos?.let { getDignity(it) } ?: PlanetaryDignity.NEUTRAL_SIGN
        val tenthPlanets = VedicAstrologyUtils.getPlanetsInHouse(chart, 10).map { it.planet }
        val indicators = mutableListOf<StringKeyNative>()
        val fields = mutableListOf<String>(); val fieldsNe = mutableListOf<String>()
        chart.planetPositions.find { it.planet == Planet.SUN }?.let { if (getDignity(it).ordinal <= 2) { indicators.add(StringKeyNative.CAREER_SUN_STRONG); fields.add("Government, Leadership"); fieldsNe.add("सरकार, नेतृत्व") } }
        tenthPlanets.forEach { when (it) { Planet.JUPITER -> { indicators.add(StringKeyNative.CAREER_JUPITER_STRONG); fields.add("Teaching, Law"); fieldsNe.add("शिक्षण, कानून") }; Planet.MERCURY -> { indicators.add(StringKeyNative.CAREER_MERCURY_STRONG); fields.add("Business, Comm"); fieldsNe.add("व्यापार, सञ्चार") }; else -> {} } }
        val s = calculateCareerStrength(d, pos?.house ?: 10, tenthPlanets)
        return CareerAnalysis(lord, pos?.house ?: 10, d, tenthPlanets, indicators, fields.distinct(), fieldsNe.distinct(), s, "10th lord ${lord.displayName} in ${d.displayName} dignity. Potential is ${s.displayName.lowercase()}.", "${lord.displayName} १०औं भावको स्वामी ${d.displayNameNe} छ। क्षमता ${s.displayNameNe} छ।")
    }

    private fun calculateCareerStrength(d: PlanetaryDignity, h: Int, p: List<Planet>): StrengthLevel {
        var s = 3.0 + (if (d == PlanetaryDignity.EXALTED) 2.0 else if (d.ordinal <= 2) 1.5 else if (d == PlanetaryDignity.DEBILITATED) -1.5 else 0.0)
        if (h in listOf(1, 4, 7, 10, 5, 9)) s += 0.5; p.forEach { if (VedicAstrologyUtils.isNaturalBenefic(it)) s += 0.3 }
        return when { s >= 5.0 -> StrengthLevel.EXCELLENT; s >= 4.0 -> StrengthLevel.STRONG; s >= 3.0 -> StrengthLevel.MODERATE; else -> StrengthLevel.WEAK }
    }

    private fun analyzeMarriage(chart: VedicChart): MarriageAnalysis {
        val lord = VedicAstrologyUtils.getHouseLord(chart, 7); val lp = chart.planetPositions.find { it.planet == lord }
        val d = lp?.let { getDignity(it) } ?: PlanetaryDignity.NEUTRAL_SIGN
        val vp = chart.planetPositions.find { it.planet == Planet.VENUS }; val vs = dignityToStrength(vp?.let { getDignity(it) } ?: PlanetaryDignity.NEUTRAL_SIGN)
        val timing = if (lp?.house in listOf(1, 2, 4, 7, 11)) MarriageTiming.EARLY else if (lp?.house in listOf(6, 8, 12)) MarriageTiming.DELAYED else MarriageTiming.NORMAL
        val (sn, snNe) = determineSpouseNature(VedicAstrologyUtils.getHouseSign(chart, 7), VedicAstrologyUtils.getPlanetsInHouse(chart, 7))
        val s = calculateRelationshipStrength(d, vs, VedicAstrologyUtils.getPlanetsInHouse(chart, 7), chart)
        return MarriageAnalysis(lord, lp?.house ?: 7, d, vp, vs, timing, sn, snNe, s, "Marriage prospects are ${vs.displayName.lowercase()}.", "विवाह सम्भावना ${vs.displayNameNe} छ।")
    }

    private fun determineSpouseNature(s: ZodiacSign, p: List<com.astro.vajra.core.model.PlanetPosition>): Pair<String, String> {
        val t = mutableListOf<String>(); val tn = mutableListOf<String>()
        when (NativeAnalysisHelpers.getSignElement(s)) { Element.FIRE -> { t.add("dynamic"); tn.add("गतिशील") }; Element.EARTH -> { t.add("practical"); tn.add("व्यावहारिक") }; else -> {} }
        return t.joinToString(", ") to tn.joinToString(", ")
    }

    private fun calculateRelationshipStrength(d: PlanetaryDignity, vs: StrengthLevel, p: List<com.astro.vajra.core.model.PlanetPosition>, c: VedicChart): StrengthLevel {
        var s = 3.0 + (vs.value - 3) * 0.3; if (d == PlanetaryDignity.EXALTED) s += 1.5; p.forEach { if (VedicAstrologyUtils.isNaturalBenefic(it.planet)) s += 0.3 }
        return when { s >= 4.5 -> StrengthLevel.EXCELLENT; s >= 3.5 -> StrengthLevel.STRONG; else -> StrengthLevel.MODERATE }
    }

    private fun analyzeHealth(chart: VedicChart): HealthAnalysis {
        val asc = VedicAstrologyUtils.getAscendantSign(chart); val lord = asc.ruler; val lp = chart.planetPositions.find { it.planet == lord }
        val d = lp?.let { getDignity(it) } ?: PlanetaryDignity.NEUTRAL_SIGN
        val s = if (d == PlanetaryDignity.EXALTED) StrengthLevel.EXCELLENT else if (d.ordinal <= 2) StrengthLevel.STRONG else StrengthLevel.MODERATE
        val (c, cn) = identifyHealthConcerns(chart, asc)
        return HealthAnalysis(s, VedicAstrologyUtils.getHouseLord(chart, 6), VedicAstrologyUtils.getHouseLord(chart, 8), if (s.value >= 4) ConstitutionType.STRONG else ConstitutionType.MODERATE, getHealthAreasForSign(asc), if (s.value >= 4) LongevityIndicator.LONG else LongevityIndicator.MEDIUM, c, cn, "Health is ${s.displayName.lowercase()}.", "स्वास्थ्य ${s.displayNameNe} छ।")
    }

    private fun identifyHealthConcerns(c: VedicChart, a: ZodiacSign): Pair<List<String>, List<String>> = listOf("General health awareness") to listOf("सामान्य स्वास्थ्य सचेतना")
    private fun getHealthAreasForSign(s: ZodiacSign): StringKeyNative = StringKeyNative.HEALTH_ARIES_AREAS // Simplification

    private fun analyzeWealth(chart: VedicChart): WealthAnalysis {
        val l2 = VedicAstrologyUtils.getHouseLord(chart, 2); val l11 = VedicAstrologyUtils.getHouseLord(chart, 11)
        val s2 = dignityToStrength(chart.planetPositions.find { it.planet == l2 }?.let { getDignity(it) } ?: PlanetaryDignity.NEUTRAL_SIGN)
        val s11 = dignityToStrength(chart.planetPositions.find { it.planet == l11 }?.let { getDignity(it) } ?: PlanetaryDignity.NEUTRAL_SIGN)
        val dy = checkDhanaYoga(chart); val p = calculateWealthPotential(s2, s11, dy)
        return WealthAnalysis(l2, s2, l11, s11, StrengthLevel.MODERATE, dy, listOf("Multiple sources"), listOf("विविध स्रोत"), p, "Wealth potential is ${p.displayName.lowercase()}.", "धन क्षमता ${p.displayNameNe} छ।")
    }

    private fun checkDhanaYoga(c: VedicChart): Boolean = true // Simplified
    private fun calculateWealthPotential(s2: StrengthLevel, s11: StrengthLevel, dy: Boolean): StrengthLevel = if (dy && s2.value >= 4) StrengthLevel.STRONG else StrengthLevel.MODERATE

    private fun analyzeEducation(chart: VedicChart): EducationAnalysis {
        val l4 = VedicAstrologyUtils.getHouseLord(chart, 4); val l5 = VedicAstrologyUtils.getHouseLord(chart, 5)
        val ms = dignityToStrength(chart.planetPositions.find { it.planet == Planet.MERCURY }?.let { getDignity(it) } ?: PlanetaryDignity.NEUTRAL_SIGN)
        return EducationAnalysis(l4, l5, ms, true, listOf("Knowledge"), listOf("ज्ञान"), StrengthLevel.STRONG, "Education is favorable.", "शिक्षा अनुकूल छ।")
    }

    private fun analyzeSpirituality(chart: VedicChart): SpiritualAnalysis {
        val l9 = VedicAstrologyUtils.getHouseLord(chart, 9); val l12 = VedicAstrologyUtils.getHouseLord(chart, 12)
        return SpiritualAnalysis(l9, l12, chart.planetPositions.find { it.planet == Planet.KETU }, StrengthLevel.MODERATE, StrengthLevel.STRONG, listOf("Meditation"), listOf("ध्यान"), "Spiritual growth indicated.", "आध्यात्मिक प्रगति संकेत गरिएको छ।")
    }

    private fun identifyKeyStrengths(c: VedicChart): List<TraitInfo> = listOf(TraitInfo(StringKeyNative.TRAIT_DETERMINATION, StrengthLevel.STRONG, null))
    private fun identifyKeyChallenges(c: VedicChart): List<TraitInfo> = listOf(TraitInfo(StringKeyNative.CHALLENGE_IMPULSIVENESS, StrengthLevel.MODERATE, null))
}
