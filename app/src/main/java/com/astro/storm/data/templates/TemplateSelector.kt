package com.astro.storm.data.templates

import com.astro.storm.core.common.Language
import com.astro.storm.core.model.Planet
import com.astro.storm.core.model.ZodiacSign
import com.astro.storm.ephemeris.DivisionalChartType
import com.astro.storm.ephemeris.deepanalysis.LifeArea
import com.astro.storm.ephemeris.deepanalysis.LocalizedParagraph
import com.astro.storm.ephemeris.deepanalysis.PlanetaryDignityLevel
import com.astro.storm.ephemeris.deepanalysis.StrengthLevel

object TemplateSelector {

    private val templatesByCategory = TemplateDatabase.templatesByCategory

    fun selectDashaTheme(
        planet: Planet,
        sign: ZodiacSign,
        degree: Double,
        strength: StrengthLevel
    ): LocalizedParagraph {
        val degreeRange = TemplateDatabase.degreeRanges.firstOrNull { it.contains(degree) }
            ?: TemplateDatabase.degreeRanges.last()
        val strengthBand = StrengthBand.fromStrength(strength)

        return selectBest(
            TemplateCategory.DASHA,
            TemplateConditions(
                planet = planet,
                sign = sign,
                degreeRange = degreeRange,
                strengthBand = strengthBand
            )
        ) ?: fallbackParagraph(
            "${planet.getLocalizedName(Language.ENGLISH)} period emphasizes ${sign.getLocalizedName(Language.ENGLISH)} themes.",
            "${planet.getLocalizedName(Language.NEPALI)} अवधिले ${sign.getLocalizedName(Language.NEPALI)} विषयहरूमा जोड दिन्छ।"
        )
    }

    fun selectLifeAreaEffect(
        planet: Planet,
        area: LifeArea,
        sign: ZodiacSign,
        degree: Double,
        strength: StrengthLevel,
        horizon: TimeHorizon
    ): LocalizedParagraph {
        val degreeRange = TemplateDatabase.degreeRanges.firstOrNull { it.contains(degree) }
            ?: TemplateDatabase.degreeRanges.last()
        val strengthBand = StrengthBand.fromStrength(strength)
        val element = TemplateDatabase.elementForSign(sign)

        return selectBest(
            TemplateCategory.LIFE_AREA,
            TemplateConditions(
                lifeArea = area,
                planet = planet,
                element = element,
                degreeRange = degreeRange,
                strengthBand = strengthBand,
                timeHorizon = horizon
            )
        ) ?: fallbackParagraph(
            "${planet.getLocalizedName(Language.ENGLISH)} influences ${area.name.lowercase()} matters.",
            "${planet.getLocalizedName(Language.NEPALI)} ले ${area.name} मामिलाहरूमा प्रभाव पार्छ।"
        )
    }

    fun selectTransitEffect(
        transitingPlanet: Planet,
        natalPlanet: Planet,
        aspect: TransitAspectType,
        phase: TransitPhase
    ): LocalizedParagraph {
        return selectBest(
            TemplateCategory.TRANSIT,
            TemplateConditions(
                transitingPlanet = transitingPlanet,
                natalPlanet = natalPlanet,
                transitAspect = aspect,
                transitPhase = phase
            )
        ) ?: fallbackParagraph(
            "${transitingPlanet.getLocalizedName(Language.ENGLISH)} transit influences ${natalPlanet.getLocalizedName(Language.ENGLISH)} matters.",
            "${transitingPlanet.getLocalizedName(Language.NEPALI)} गोचरले ${natalPlanet.getLocalizedName(Language.NEPALI)} विषयहरूमा प्रभाव पार्छ।"
        )
    }

    fun selectHouseLordEffect(
        planet: Planet,
        house: Int,
        dignity: PlanetaryDignityLevel,
        strength: StrengthLevel
    ): LocalizedParagraph {
        return selectBest(
            TemplateCategory.HOUSE_LORD,
            TemplateConditions(
                planet = planet,
                house = house,
                dignityBand = DignityBand.fromDignity(dignity),
                strengthBand = StrengthBand.fromStrength(strength)
            )
        ) ?: fallbackParagraph(
            "${planet.getLocalizedName(Language.ENGLISH)} shapes house $house matters.",
            "${planet.getLocalizedName(Language.NEPALI)} ले $house भावका विषयहरू निर्धारण गर्छ।"
        )
    }

    fun selectDivisionalEffect(
        varga: DivisionalChartType,
        planet: Planet,
        strength: StrengthLevel
    ): LocalizedParagraph {
        return selectBest(
            TemplateCategory.DIVISIONAL,
            TemplateConditions(
                varga = varga,
                planet = planet,
                strengthBand = StrengthBand.fromStrength(strength)
            )
        ) ?: fallbackParagraph(
            "${varga.shortName} shows ${planet.getLocalizedName(Language.ENGLISH)} influences.",
            "${varga.shortName} ले ${planet.getLocalizedName(Language.NEPALI)} को प्रभाव देखाउँछ।"
        )
    }

    fun selectNadiPrediction(
        startSign: ZodiacSign,
        nadiNumber: Int
    ): LocalizedParagraph {
        return selectBest(
            TemplateCategory.NADI,
            TemplateConditions(
                startSign = startSign,
                nadiNumber = nadiNumber
            )
        ) ?: fallbackParagraph(
            "Nadi $nadiNumber indicates precise timing potential.",
            "नाडी $nadiNumber ले सूक्ष्म समय संकेत गर्छ।"
        )
    }

    fun selectYogaEffect(
        category: YogaCategory,
        sign: ZodiacSign,
        strength: StrengthLevel
    ): LocalizedParagraph {
        return selectBest(
            TemplateCategory.YOGA,
            TemplateConditions(
                yogaCategory = category,
                sign = sign,
                strengthBand = StrengthBand.fromStrength(strength)
            )
        ) ?: fallbackParagraph(
            "Yoga activation influences ${sign.getLocalizedName(Language.ENGLISH)} themes.",
            "योग सक्रियताले ${sign.getLocalizedName(Language.NEPALI)} विषयहरूमा प्रभाव पार्छ।"
        )
    }

    private fun selectBest(category: TemplateCategory, criteria: TemplateConditions): LocalizedParagraph? {
        val candidates = templatesByCategory[category].orEmpty().filter { template ->
            matches(template.conditions, criteria)
        }

        return candidates
            .maxWithOrNull(compareBy<PredictionTemplate> { it.priority }.thenBy { it.conditions.specificity() })
            ?.text
    }

    private fun matches(conditions: TemplateConditions, criteria: TemplateConditions): Boolean {
        if (conditions.planet != null && conditions.planet != criteria.planet) return false
        if (conditions.sign != null && conditions.sign != criteria.sign) return false
        if (conditions.degreeRange != null && criteria.degreeRange != null && conditions.degreeRange != criteria.degreeRange) return false
        if (conditions.strengthBand != null && criteria.strengthBand != null && conditions.strengthBand != criteria.strengthBand) return false
        if (conditions.dignityBand != null && criteria.dignityBand != null && conditions.dignityBand != criteria.dignityBand) return false
        if (conditions.house != null && conditions.house != criteria.house) return false
        if (conditions.lifeArea != null && conditions.lifeArea != criteria.lifeArea) return false
        if (conditions.timeHorizon != null && conditions.timeHorizon != criteria.timeHorizon) return false
        if (conditions.element != null && conditions.element != criteria.element) return false
        if (conditions.transitingPlanet != null && conditions.transitingPlanet != criteria.transitingPlanet) return false
        if (conditions.natalPlanet != null && conditions.natalPlanet != criteria.natalPlanet) return false
        if (conditions.transitAspect != null && conditions.transitAspect != criteria.transitAspect) return false
        if (conditions.transitPhase != null && conditions.transitPhase != criteria.transitPhase) return false
        if (conditions.varga != null && conditions.varga != criteria.varga) return false
        if (conditions.nadiNumber != null && conditions.nadiNumber != criteria.nadiNumber) return false
        if (conditions.startSign != null && conditions.startSign != criteria.startSign) return false
        if (conditions.yogaCategory != null && conditions.yogaCategory != criteria.yogaCategory) return false

        return true
    }

    private fun fallbackParagraph(en: String, ne: String): LocalizedParagraph = LocalizedParagraph(en, ne)
}
