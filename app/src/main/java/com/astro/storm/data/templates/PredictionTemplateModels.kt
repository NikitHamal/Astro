package com.astro.storm.data.templates

import com.astro.storm.core.model.Planet
import com.astro.storm.core.model.ZodiacSign
import com.astro.storm.ephemeris.DivisionalChartType
import com.astro.storm.ephemeris.deepanalysis.Element
import com.astro.storm.ephemeris.deepanalysis.LifeArea
import com.astro.storm.ephemeris.deepanalysis.LocalizedParagraph
import com.astro.storm.ephemeris.deepanalysis.PlanetaryDignityLevel
import com.astro.storm.ephemeris.deepanalysis.StrengthLevel

enum class TemplateCategory {
    DASHA,
    TRANSIT,
    HOUSE_LORD,
    DIVISIONAL,
    NADI,
    LIFE_AREA,
    YOGA
}

data class DegreeRange(
    val start: Double,
    val end: Double,
    val labelEn: String,
    val labelNe: String
) {
    fun contains(degree: Double): Boolean = degree >= start && degree < end
}

enum class StrengthBand(
    val labelEn: String,
    val labelNe: String
) {
    STRONG("strong", "बलियो"),
    MODERATE("moderate", "मध्यम"),
    WEAK("weak", "कमजोर");

    fun matches(strength: StrengthLevel): Boolean = when (this) {
        STRONG -> strength.value >= StrengthLevel.STRONG.value
        MODERATE -> strength.value == StrengthLevel.MODERATE.value
        WEAK -> strength.value <= StrengthLevel.WEAK.value
    }

    companion object {
        fun fromStrength(strength: StrengthLevel): StrengthBand = when {
            strength.value >= StrengthLevel.STRONG.value -> STRONG
            strength.value == StrengthLevel.MODERATE.value -> MODERATE
            else -> WEAK
        }
    }
}

enum class DignityBand(
    val labelEn: String,
    val labelNe: String
) {
    FAVORABLE("favorable", "अनुकूल"),
    NEUTRAL("neutral", "तटस्थ"),
    CHALLENGED("challenged", "चुनौतीपूर्ण");

    fun matches(dignity: PlanetaryDignityLevel): Boolean = when (this) {
        FAVORABLE -> dignity in setOf(
            PlanetaryDignityLevel.EXALTED,
            PlanetaryDignityLevel.MOOLATRIKONA,
            PlanetaryDignityLevel.OWN_SIGN,
            PlanetaryDignityLevel.FRIEND_SIGN
        )
        NEUTRAL -> dignity == PlanetaryDignityLevel.NEUTRAL
        CHALLENGED -> dignity in setOf(
            PlanetaryDignityLevel.DEBILITATED,
            PlanetaryDignityLevel.ENEMY_SIGN
        )
    }

    companion object {
        fun fromDignity(dignity: PlanetaryDignityLevel): DignityBand = when (dignity) {
            PlanetaryDignityLevel.EXALTED,
            PlanetaryDignityLevel.MOOLATRIKONA,
            PlanetaryDignityLevel.OWN_SIGN,
            PlanetaryDignityLevel.FRIEND_SIGN -> FAVORABLE
            PlanetaryDignityLevel.DEBILITATED,
            PlanetaryDignityLevel.ENEMY_SIGN -> CHALLENGED
            PlanetaryDignityLevel.NEUTRAL -> NEUTRAL
        }
    }
}

enum class TimeHorizon {
    SHORT,
    MEDIUM,
    LONG
}

enum class TransitAspectType {
    CONJUNCTION,
    OPPOSITION,
    TRINE,
    SQUARE,
    SEXTILE
}

enum class TransitPhase {
    APPLYING,
    SEPARATING
}

enum class YogaCategory {
    RAJA,
    DHANA,
    MAHAPURUSHA,
    NABHASA,
    ARISHTA,
    VIPARITA,
    PARIVARTANA,
    BHAVA,
    GENERAL
}

data class TemplateConditions(
    val planet: Planet? = null,
    val sign: ZodiacSign? = null,
    val degreeRange: DegreeRange? = null,
    val strengthBand: StrengthBand? = null,
    val dignityBand: DignityBand? = null,
    val house: Int? = null,
    val lifeArea: LifeArea? = null,
    val timeHorizon: TimeHorizon? = null,
    val element: Element? = null,
    val transitingPlanet: Planet? = null,
    val natalPlanet: Planet? = null,
    val transitAspect: TransitAspectType? = null,
    val transitPhase: TransitPhase? = null,
    val varga: DivisionalChartType? = null,
    val nadiNumber: Int? = null,
    val startSign: ZodiacSign? = null,
    val yogaCategory: YogaCategory? = null
) {
    fun specificity(): Int = listOf(
        planet,
        sign,
        degreeRange,
        strengthBand,
        dignityBand,
        house,
        lifeArea,
        timeHorizon,
        element,
        transitingPlanet,
        natalPlanet,
        transitAspect,
        transitPhase,
        varga,
        nadiNumber,
        startSign,
        yogaCategory
    ).count { it != null }
}

data class PredictionTemplate(
    val id: String,
    val category: TemplateCategory,
    val conditions: TemplateConditions,
    val text: LocalizedParagraph,
    val priority: Int
)
