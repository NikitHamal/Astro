package com.astro.storm.ephemeris

import com.astro.storm.data.localization.StringKey
import com.astro.storm.data.localization.StringKeyAnalysis
import com.astro.storm.data.localization.StringKeyInterface
import com.astro.storm.data.model.Planet
import com.astro.storm.data.model.PlanetPosition
import com.astro.storm.data.model.VedicChart
import com.astro.storm.data.model.ZodiacSign
import kotlin.math.abs

object PanchMahapurushaYogaCalculator {

    private val KENDRA_HOUSES = setOf(1, 4, 7, 10)

    private val EXALTATION_SIGNS = mapOf(
        Planet.MARS to ZodiacSign.CAPRICORN,
        Planet.MERCURY to ZodiacSign.VIRGO,
        Planet.JUPITER to ZodiacSign.CANCER,
        Planet.VENUS to ZodiacSign.PISCES,
        Planet.SATURN to ZodiacSign.LIBRA
    )

    private val EXALTATION_DEGREES = mapOf(
        Planet.MARS to 28.0,
        Planet.MERCURY to 15.0,
        Planet.JUPITER to 5.0,
        Planet.VENUS to 27.0,
        Planet.SATURN to 20.0
    )

    private val OWN_SIGNS = mapOf(
        Planet.MARS to setOf(ZodiacSign.ARIES, ZodiacSign.SCORPIO),
        Planet.MERCURY to setOf(ZodiacSign.GEMINI, ZodiacSign.VIRGO),
        Planet.JUPITER to setOf(ZodiacSign.SAGITTARIUS, ZodiacSign.PISCES),
        Planet.VENUS to setOf(ZodiacSign.TAURUS, ZodiacSign.LIBRA),
        Planet.SATURN to setOf(ZodiacSign.CAPRICORN, ZodiacSign.AQUARIUS)
    )

    fun analyzePanchMahapurushaYogas(chart: VedicChart): PanchMahapurushaAnalysis {
        val detectedYogas = mutableListOf<MahapurushaYoga>()

        for (planet in listOf(Planet.MARS, Planet.MERCURY, Planet.JUPITER, Planet.VENUS, Planet.SATURN)) {
            val position = chart.planetPositions.find { it.planet == planet } ?: continue
            val yoga = checkMahapurushaYoga(planet, position, chart)
            if (yoga != null) {
                detectedYogas.add(yoga)
            }
        }

        val interpretation = generateOverallInterpretation(detectedYogas, chart)
        val combinedEffects = analyzeCombinedEffects(detectedYogas)
        val activationPeriods = calculateActivationPeriods(detectedYogas)

        return PanchMahapurushaAnalysis(
            yogas = detectedYogas,
            hasAnyYoga = detectedYogas.isNotEmpty(),
            yogaCount = detectedYogas.size,
            strongestYoga = detectedYogas.maxByOrNull { it.strength },
            interpretation = interpretation,
            combinedEffects = combinedEffects,
            activationPeriods = activationPeriods,
            overallYogaStrength = calculateOverallStrength(detectedYogas)
        )
    }

    private fun checkMahapurushaYoga(
        planet: Planet,
        position: PlanetPosition,
        chart: VedicChart
    ): MahapurushaYoga? {
        val house = position.house
        val sign = ZodiacSign.fromLongitude(position.longitude)

        if (house !in KENDRA_HOUSES) return null

        val isExalted = sign == EXALTATION_SIGNS[planet]
        val isOwnSign = sign in (OWN_SIGNS[planet] ?: emptySet())

        if (!isExalted && !isOwnSign) return null

        val yogaType = getYogaType(planet)
        val dignity = if (isExalted) PlanetDignity.EXALTED else PlanetDignity.OWN_SIGN
        val strength = calculateYogaStrength(planet, position, dignity, house, chart)
        val effects = generateYogaEffects(yogaType)
        val remedies = generateRemedies(yogaType)

        return MahapurushaYoga(
            type = yogaType,
            planet = planet,
            house = house,
            sign = sign,
            dignity = dignity,
            strength = strength,
            strengthLevel = getStrengthLevel(strength),
            isExalted = isExalted,
            isOwnSign = isOwnSign,
            degreeFromExact = if (isExalted) calculateDegreeFromExact(position.longitude, planet) else null,
            effects = effects,
            recommendations = remedies,
            planetPosition = position
        )
    }

    private fun getYogaType(planet: Planet): MahapurushaYogaType {
        return when (planet) {
            Planet.MARS -> MahapurushaYogaType.RUCHAKA
            Planet.MERCURY -> MahapurushaYogaType.BHADRA
            Planet.JUPITER -> MahapurushaYogaType.HAMSA
            Planet.VENUS -> MahapurushaYogaType.MALAVYA
            Planet.SATURN -> MahapurushaYogaType.SASHA
            else -> throw IllegalArgumentException("Invalid planet for Mahapurusha Yoga")
        }
    }

    private fun calculateYogaStrength(
        planet: Planet,
        position: PlanetPosition,
        dignity: PlanetDignity,
        house: Int,
        chart: VedicChart
    ): Int {
        var strength = 0
        strength += when (dignity) {
            PlanetDignity.EXALTED -> 40
            PlanetDignity.OWN_SIGN -> 30
            else -> 0
        }
        strength += when (house) {
            1 -> 20
            10 -> 18
            7 -> 15
            4 -> 12
            else -> 0
        }
        if (dignity == PlanetDignity.EXALTED) {
            val degreeFromExact = calculateDegreeFromExact(position.longitude, planet)
            if (degreeFromExact != null) {
                val degreeFactor = (15 - degreeFromExact).coerceAtLeast(0.0)
                strength += degreeFactor.toInt()
            }
        }
        val aspectBonus = calculateAspectBonus(position, chart)
        strength += aspectBonus
        if (!position.isRetrograde) strength += 5
        if (!isCombust(position, chart)) strength += 5
        return strength.coerceIn(0, 100)
    }

    private fun calculateDegreeFromExact(longitude: Double, planet: Planet): Double? {
        val exaltationDegree = EXALTATION_DEGREES[planet] ?: return null
        val exaltationSign = EXALTATION_SIGNS[planet] ?: return null
        val signStart = exaltationSign.ordinal * 30.0
        val exactLongitude = signStart + exaltationDegree
        return abs(longitude - exactLongitude)
    }

    private fun calculateAspectBonus(position: PlanetPosition, chart: VedicChart): Int {
        var bonus = 0
        val benefics = listOf(Planet.JUPITER, Planet.VENUS, Planet.MERCURY)
        val malefics = listOf(Planet.SATURN, Planet.MARS, Planet.RAHU, Planet.KETU)
        for (otherPosition in chart.planetPositions) {
            if (otherPosition.planet == position.planet) continue
            val houseDiff = calculateHouseDifference(otherPosition.house, position.house)
            val isAspecting = houseDiff in listOf(0, 4, 6, 8) ||
                    (otherPosition.planet == Planet.JUPITER && houseDiff in listOf(4, 6, 8)) ||
                    (otherPosition.planet == Planet.MARS && houseDiff in listOf(3, 6, 7)) ||
                    (otherPosition.planet == Planet.SATURN && houseDiff in listOf(2, 6, 9))
            if (isAspecting) {
                if (otherPosition.planet in benefics) bonus += 5
                else if (otherPosition.planet in malefics) bonus -= 3
            }
        }
        return bonus.coerceIn(0, 15)
    }

    private fun calculateHouseDifference(house1: Int, house2: Int): Int {
        val diff = house1 - house2
        return if (diff < 0) diff + 12 else diff
    }

    private fun isCombust(position: PlanetPosition, chart: VedicChart): Boolean {
        val sunPosition = chart.planetPositions.find { it.planet == Planet.SUN } ?: return false
        val separation = abs(position.longitude - sunPosition.longitude)
        val normalizedSeparation = if (separation > 180) 360 - separation else separation
        val combustionOrb = when (position.planet) {
            Planet.MARS -> 17.0
            Planet.MERCURY -> 14.0
            Planet.JUPITER -> 11.0
            Planet.VENUS -> 10.0
            Planet.SATURN -> 15.0
            else -> 10.0
        }
        return normalizedSeparation <= combustionOrb
    }

    private fun getStrengthLevel(strength: Int): YogaStrengthLevel {
        return when {
            strength >= 80 -> YogaStrengthLevel.EXCEPTIONAL
            strength >= 60 -> YogaStrengthLevel.STRONG
            strength >= 40 -> YogaStrengthLevel.MODERATE
            strength >= 20 -> YogaStrengthLevel.WEAK
            else -> YogaStrengthLevel.VERY_WEAK
        }
    }

    private fun generateYogaEffects(type: MahapurushaYogaType): YogaEffects {
        return YogaEffects(
            physicalTraits = type.physicalTraitsKey,
            mentalTraits = type.mentalTraitsKey,
            careerIndications = type.careerIndicationsKey,
            spiritualGrowth = type.spiritualGrowthKey,
            relationshipImpact = type.relationshipImpactKey
        )
    }

    private fun generateRemedies(type: MahapurushaYogaType): List<YogaRecommendation> {
        return listOf(
            YogaRecommendation(
                category = RecommendationType.STRENGTHENING,
                actionKey = StringKeyAnalysis.REC_ACTION_WORSHIP,
                timingKey = StringKeyAnalysis.REC_TIMING_WEEKDAY_MORNING,
                benefitKey = StringKeyAnalysis.REC_BENEFIT_ACTIVATE
            ),
            YogaRecommendation(
                category = RecommendationType.CAREER,
                actionKey = StringKeyAnalysis.REC_ACTION_CAREER,
                timingKey = StringKeyAnalysis.REC_TIMING_DASHA,
                benefitKey = StringKeyAnalysis.REC_BENEFIT_CAREER
            ),
            YogaRecommendation(
                category = RecommendationType.GEMSTONE,
                actionKey = StringKeyAnalysis.REC_ACTION_GEMSTONE,
                timingKey = StringKeyAnalysis.REC_TIMING_MUHURTA,
                benefitKey = StringKeyAnalysis.REC_BENEFIT_GEMSTONE
            ),
            YogaRecommendation(
                category = RecommendationType.MANTRA,
                actionKey = StringKeyAnalysis.REC_ACTION_MANTRA,
                timingKey = StringKeyAnalysis.REC_TIMING_HORA,
                benefitKey = StringKeyAnalysis.REC_BENEFIT_MANTRA
            ),
            YogaRecommendation(
                category = RecommendationType.CHARITY,
                actionKey = StringKeyAnalysis.REC_ACTION_CHARITY,
                timingKey = StringKeyAnalysis.REC_TIMING_REGULAR,
                benefitKey = StringKeyAnalysis.REC_BENEFIT_CHARITY
            )
        )
    }

    private fun analyzeCombinedEffects(yogas: List<MahapurushaYoga>): CombinedYogaEffects? {
        if (yogas.size < 2) return null
        val yogaTypes = yogas.map { it.type }
        val totalStrength = yogas.sumOf { it.strength }
        val averageStrength = totalStrength / yogas.size
        val synergies = mutableListOf<StringKeyInterface>()
        val combinedEffects = mutableListOf<StringKeyInterface>()

        if (MahapurushaYogaType.HAMSA in yogaTypes && MahapurushaYogaType.MALAVYA in yogaTypes) {
            synergies.add(StringKeyAnalysis.COMBINED_SYNERGY_HAMSA_MALAVYA)
            combinedEffects.add(StringKeyAnalysis.COMBINED_EFFECT_HAMSA_MALAVYA)
        }
        if (MahapurushaYogaType.RUCHAKA in yogaTypes && MahapurushaYogaType.SASHA in yogaTypes) {
            synergies.add(StringKeyAnalysis.COMBINED_SYNERGY_RUCHAKA_SASHA)
            combinedEffects.add(StringKeyAnalysis.COMBINED_EFFECT_RUCHAKA_SASHA)
        }
        if (MahapurushaYogaType.BHADRA in yogaTypes && MahapurushaYogaType.HAMSA in yogaTypes) {
            synergies.add(StringKeyAnalysis.COMBINED_SYNERGY_BHADRA_HAMSA)
            combinedEffects.add(StringKeyAnalysis.COMBINED_EFFECT_BHADRA_HAMSA)
        }
        if (yogas.size >= 3) {
            combinedEffects.add(StringKeyAnalysis.COMBINED_RARE_COMBO)
        }

        return CombinedYogaEffects(
            yogaCount = yogas.size,
            combinedStrength = averageStrength,
            synergies = synergies,
            combinedBenefits = combinedEffects,
            rarityKey = when (yogas.size) {
                2 -> StringKeyAnalysis.COMBINED_RARITY_2
                3 -> StringKeyAnalysis.COMBINED_RARITY_3
                4 -> StringKeyAnalysis.COMBINED_RARITY_4
                5 -> StringKeyAnalysis.COMBINED_RARITY_5
                else -> StringKeyAnalysis.COMBINED_RARITY_SINGLE
            }
        )
    }

    private fun calculateActivationPeriods(yogas: List<MahapurushaYoga>): List<ActivationPeriod> {
        val periods = mutableListOf<ActivationPeriod>()
        for (yoga in yogas) {
            periods.add(ActivationPeriod(
                yoga = yoga.type,
                periodType = "Mahadasha",
                planet = yoga.planet,
                descriptionKey = StringKeyAnalysis.ACTIVATION_MAHADASHA,
                importance = ActivationImportance.HIGH
            ))
            periods.add(ActivationPeriod(
                yoga = yoga.type,
                periodType = "Antardasha",
                planet = yoga.planet,
                descriptionKey = StringKeyAnalysis.ACTIVATION_ANTARDASHA,
                importance = ActivationImportance.MEDIUM
            ))
            periods.add(ActivationPeriod(
                yoga = yoga.type,
                periodType = "Transit",
                planet = yoga.planet,
                descriptionKey = StringKeyAnalysis.ACTIVATION_TRANSIT,
                importance = ActivationImportance.MEDIUM
            ))
            periods.add(ActivationPeriod(
                yoga = yoga.type,
                periodType = "Jupiter Transit",
                planet = Planet.JUPITER,
                descriptionKey = StringKeyAnalysis.ACTIVATION_JUPITER_TRANSIT,
                importance = ActivationImportance.HIGH
            ))
        }
        return periods.sortedByDescending { it.importance }
    }

    private fun generateOverallInterpretation(
        yogas: List<MahapurushaYoga>,
        chart: VedicChart
    ): OverallInterpretation {
        val summaryKey = if (yogas.isEmpty()) StringKeyAnalysis.OVERALL_NO_YOGA_SUMMARY
        else StringKeyAnalysis.OVERALL_YOGA_SUMMARY

        val recommendations = if (yogas.isEmpty()) {
            listOf(
                OverallRecommendation(StringKeyAnalysis.OVERALL_REC_NO_YOGA_1),
                OverallRecommendation(StringKeyAnalysis.OVERALL_REC_NO_YOGA_2),
                OverallRecommendation(StringKeyAnalysis.OVERALL_REC_NO_YOGA_3)
            )
        } else {
            yogas.map {
                OverallRecommendation(StringKeyAnalysis.OVERALL_REC_YOGA, mapOf("yogaName" to it.type.displayNameKey, "planetName" to it.planet.displayNameKey))
            }
        }

        return OverallInterpretation(
            summaryKey = summaryKey,
            keyInsights = yogas.map {
                KeyInsight(
                    yogaNameKey = it.type.displayNameKey,
                    shortDescKey = it.type.shortDescriptionKey,
                    strength = it.strength
                )
            },
            recommendations = recommendations
        )
    }

    private fun calculateOverallStrength(yogas: List<MahapurushaYoga>): Int {
        if (yogas.isEmpty()) return 0
        return yogas.sumOf { it.strength } / yogas.size
    }

    data class PanchMahapurushaAnalysis(
        val yogas: List<MahapurushaYoga>,
        val hasAnyYoga: Boolean,
        val yogaCount: Int,
        val strongestYoga: MahapurushaYoga?,
        val interpretation: OverallInterpretation,
        val combinedEffects: CombinedYogaEffects?,
        val activationPeriods: List<ActivationPeriod>,
        val overallYogaStrength: Int
    )

    data class MahapurushaYoga(
        val type: MahapurushaYogaType,
        val planet: Planet,
        val house: Int,
        val sign: ZodiacSign,
        val dignity: PlanetDignity,
        val strength: Int,
        val strengthLevel: YogaStrengthLevel,
        val isExalted: Boolean,
        val isOwnSign: Boolean,
        val degreeFromExact: Double?,
        val effects: YogaEffects,
        val recommendations: List<YogaRecommendation>,
        val planetPosition: PlanetPosition
    )

    data class YogaEffects(
        val physicalTraits: StringKeyInterface,
        val mentalTraits: StringKeyInterface,
        val careerIndications: StringKeyInterface,
        val spiritualGrowth: StringKeyInterface,
        val relationshipImpact: StringKeyInterface,
    )

    data class YogaRecommendation(
        val category: RecommendationType,
        val actionKey: StringKeyInterface,
        val timingKey: StringKeyInterface,
        val benefitKey: StringKeyInterface,
    )

    data class CombinedYogaEffects(
        val yogaCount: Int,
        val combinedStrength: Int,
        val synergies: List<StringKeyInterface>,
        val combinedBenefits: List<StringKeyInterface>,
        val rarityKey: StringKeyInterface,
    )

    data class ActivationPeriod(
        val yoga: MahapurushaYogaType,
        val periodType: String,
        val planet: Planet,
        val descriptionKey: StringKeyInterface,
        val importance: ActivationImportance
    )

    data class OverallInterpretation(
        val summaryKey: StringKeyInterface,
        val keyInsights: List<KeyInsight>,
        val recommendations: List<OverallRecommendation>
    )

    data class KeyInsight(
        val yogaNameKey: StringKeyInterface,
        val shortDescKey: StringKeyInterface,
        val strength: Int
    )

    data class OverallRecommendation(
        val textKey: StringKeyInterface,
        val args: Map<String, StringKeyInterface> = emptyMap()
    )

    enum class MahapurushaYogaType(
        val displayNameKey: StringKeyInterface,
        val planet: Planet,
        val descriptionKey: StringKeyInterface,
        val shortDescriptionKey: StringKeyInterface,
        val primaryBlessingsKey: StringKeyInterface,
        val deityKey: StringKeyInterface,
        val gemstoneKey: StringKeyInterface,
        val mantraKey: StringKeyInterface,
        val charityItemsKey: StringKeyInterface,
        val suitableCareersKey: StringKeyInterface,
        val physicalTraitsKey: StringKeyInterface,
        val mentalTraitsKey: StringKeyInterface,
        val careerIndicationsKey: StringKeyInterface,
        val spiritualGrowthKey: StringKeyInterface,
        val relationshipImpactKey: StringKeyInterface,
    ) {
        RUCHAKA(
            displayNameKey = StringKeyAnalysis.YOGA_TYPE_RUCHAKA,
            planet = Planet.MARS,
            descriptionKey = StringKeyAnalysis.RUCHAKA_DESC,
            shortDescriptionKey = StringKeyAnalysis.RUCHAKA_SHORT_DESC,
            primaryBlessingsKey = StringKeyAnalysis.RUCHAKA_BLESSINGS,
            deityKey = StringKeyAnalysis.RUCHAKA_DEITY,
            gemstoneKey = StringKeyAnalysis.RUCHAKA_GEMSTONE,
            mantraKey = StringKeyAnalysis.RUCHAKA_MANTRA,
            charityItemsKey = StringKeyAnalysis.RUCHAKA_CHARITY,
            suitableCareersKey = StringKeyAnalysis.RUCHAKA_CAREERS,
            physicalTraitsKey = StringKeyAnalysis.EFFECT_RUCHAKA_PHYSICAL,
            mentalTraitsKey = StringKeyAnalysis.EFFECT_RUCHAKA_MENTAL,
            careerIndicationsKey = StringKeyAnalysis.EFFECT_RUCHAKA_CAREER,
            spiritualGrowthKey = StringKeyAnalysis.EFFECT_RUCHAKA_SPIRITUAL,
            relationshipImpactKey = StringKeyAnalysis.EFFECT_RUCHAKA_RELATIONSHIP,
        ),
        BHADRA(
            displayNameKey = StringKeyAnalysis.YOGA_TYPE_BHADRA,
            planet = Planet.MERCURY,
            descriptionKey = StringKeyAnalysis.BHADRA_DESC,
            shortDescriptionKey = StringKeyAnalysis.BHADRA_SHORT_DESC,
            primaryBlessingsKey = StringKeyAnalysis.BHADRA_BLESSINGS,
            deityKey = StringKeyAnalysis.BHADRA_DEITY,
            gemstoneKey = StringKeyAnalysis.BHADRA_GEMSTONE,
            mantraKey = StringKeyAnalysis.BHADRA_MANTRA,
            charityItemsKey = StringKeyAnalysis.BHADRA_CHARITY,
            suitableCareersKey = StringKeyAnalysis.BHADRA_CAREERS,
            physicalTraitsKey = StringKeyAnalysis.EFFECT_BHADRA_PHYSICAL,
            mentalTraitsKey = StringKeyAnalysis.EFFECT_BHADRA_MENTAL,
            careerIndicationsKey = StringKeyAnalysis.EFFECT_BHADRA_CAREER,
            spiritualGrowthKey = StringKeyAnalysis.EFFECT_BHADRA_SPIRITUAL,
            relationshipImpactKey = StringKeyAnalysis.EFFECT_BHADRA_RELATIONSHIP,
        ),
        HAMSA(
            displayNameKey = StringKeyAnalysis.YOGA_TYPE_HAMSA,
            planet = Planet.JUPITER,
            descriptionKey = StringKeyAnalysis.HAMSA_DESC,
            shortDescriptionKey = StringKeyAnalysis.HAMSA_SHORT_DESC,
            primaryBlessingsKey = StringKeyAnalysis.HAMSA_BLESSINGS,
            deityKey = StringKeyAnalysis.HAMSA_DEITY,
            gemstoneKey = StringKeyAnalysis.HAMSA_GEMSTONE,
            mantraKey = StringKeyAnalysis.HAMSA_MANTRA,
            charityItemsKey = StringKeyAnalysis.HAMSA_CHARITY,
            suitableCareersKey = StringKeyAnalysis.HAMSA_CAREERS,
            physicalTraitsKey = StringKeyAnalysis.EFFECT_HAMSA_PHYSICAL,
            mentalTraitsKey = StringKeyAnalysis.EFFECT_HAMSA_MENTAL,
            careerIndicationsKey = StringKeyAnalysis.EFFECT_HAMSA_CAREER,
            spiritualGrowthKey = StringKeyAnalysis.EFFECT_HAMSA_SPIRITUAL,
            relationshipImpactKey = StringKeyAnalysis.EFFECT_HAMSA_RELATIONSHIP,
        ),
        MALAVYA(
            displayNameKey = StringKeyAnalysis.YOGA_TYPE_MALAVYA,
            planet = Planet.VENUS,
            descriptionKey = StringKeyAnalysis.MALAVYA_DESC,
            shortDescriptionKey = StringKeyAnalysis.MALAVYA_SHORT_DESC,
            primaryBlessingsKey = StringKeyAnalysis.MALAVYA_BLESSINGS,
            deityKey = StringKeyAnalysis.MALAVYA_DEITY,
            gemstoneKey = StringKeyAnalysis.MALAVYA_GEMSTONE,
            mantraKey = StringKeyAnalysis.MALAVYA_MANTRA,
            charityItemsKey = StringKeyAnalysis.MALAVYA_CHARITY,
            suitableCareersKey = StringKeyAnalysis.MALAVYA_CAREERS,
            physicalTraitsKey = StringKeyAnalysis.EFFECT_MALAVYA_PHYSICAL,
            mentalTraitsKey = StringKeyAnalysis.EFFECT_MALAVYA_MENTAL,
            careerIndicationsKey = StringKeyAnalysis.EFFECT_MALAVYA_CAREER,
            spiritualGrowthKey = StringKeyAnalysis.EFFECT_MALAVYA_SPIRITUAL,
            relationshipImpactKey = StringKeyAnalysis.EFFECT_MALAVYA_RELATIONSHIP,
        ),
        SASHA(
            displayNameKey = StringKeyAnalysis.YOGA_TYPE_SASHA,
            planet = Planet.SATURN,
            descriptionKey = StringKeyAnalysis.SASHA_DESC,
            shortDescriptionKey = StringKeyAnalysis.SASHA_SHORT_DESC,
            primaryBlessingsKey = StringKeyAnalysis.SASHA_BLESSINGS,
            deityKey = StringKeyAnalysis.SASHA_DEITY,
            gemstoneKey = StringKeyAnalysis.SASHA_GEMSTONE,
            mantraKey = StringKeyAnalysis.SASHA_MANTRA,
            charityItemsKey = StringKeyAnalysis.SASHA_CHARITY,
            suitableCareersKey = StringKeyAnalysis.SASHA_CAREERS,
            physicalTraitsKey = StringKeyAnalysis.EFFECT_SASHA_PHYSICAL,
            mentalTraitsKey = StringKeyAnalysis.EFFECT_SASHA_MENTAL,
            careerIndicationsKey = StringKeyAnalysis.EFFECT_SASHA_CAREER,
            spiritualGrowthKey = StringKeyAnalysis.EFFECT_SASHA_SPIRITUAL,
            relationshipImpactKey = StringKeyAnalysis.EFFECT_SASHA_RELATIONSHIP,
        )
    }

    enum class PlanetDignity(val displayNameKey: StringKeyInterface) {
        EXALTED(StringKeyAnalysis.DIGNITY_EXALTED),
        OWN_SIGN(StringKeyAnalysis.DIGNITY_OWN_SIGN),
        MOOLATRIKONA(StringKeyAnalysis.DIGNITY_MOOLATRIKONA),
        FRIEND(StringKeyAnalysis.DIGNITY_FRIEND),
        NEUTRAL(StringKeyAnalysis.DIGNITY_NEUTRAL),
        ENEMY(StringKeyAnalysis.DIGNITY_ENEMY),
        DEBILITATED(StringKeyAnalysis.DIGNITY_DEBILITATED)
    }

    enum class YogaStrengthLevel(val displayNameKey: StringKeyInterface) {
        EXCEPTIONAL(StringKeyAnalysis.STRENGTH_EXCEPTIONAL),
        STRONG(StringKeyAnalysis.STRENGTH_STRONG),
        MODERATE(StringKeyAnalysis.STRENGTH_MODERATE),
        WEAK(StringKeyAnalysis.STRENGTH_WEAK),
        VERY_WEAK(StringKeyAnalysis.STRENGTH_VERY_WEAK)
    }

    enum class RecommendationType(val displayNameKey: StringKeyInterface) {
        STRENGTHENING(StringKeyAnalysis.REC_TYPE_STRENGTHENING),
        CAREER(StringKeyAnalysis.REC_TYPE_CAREER),
        GEMSTONE(StringKeyAnalysis.REC_TYPE_GEMSTONE),
        MANTRA(StringKeyAnalysis.REC_TYPE_MANTRA),
        CHARITY(StringKeyAnalysis.REC_TYPE_CHARITY)
    }

    enum class ActivationImportance {
        HIGH, MEDIUM, LOW
    }
}
