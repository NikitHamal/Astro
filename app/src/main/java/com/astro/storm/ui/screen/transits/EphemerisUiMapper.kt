package com.astro.storm.ui.screen.transits

import com.astro.storm.core.common.Language
import com.astro.storm.core.common.StringKey
import com.astro.storm.core.common.StringKeyEphemerisUi
import com.astro.storm.core.common.StringResources
import com.astro.storm.core.common.getLocalizedName
import com.astro.storm.ephemeris.TransitAnalyzer
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import kotlin.math.roundToInt

enum class EventSeverity {
    FAVORABLE,
    NEUTRAL,
    CHALLENGING,
    ALERT
}

enum class EventKind {
    ASPECT,
    PERIOD,
    POSITION,
    RETROGRADE,
    SIGN_INGRESS,
    DIGNITY
}

/**
 * Status type for transit events - matches reference Ephemeris UI design
 */
enum class TransitStatusType {
    RETROGRADE,     // Motion Reversed - planet moving backward
    DIRECT,         // Motion Direct - planet moving forward
    EXALTED,        // Planet in exaltation sign
    DEBILITATED,    // Planet in debilitation sign
    OWN_SIGN,       // Planet in own sign (Svakshetra)
    COMBUSTION,     // Planet combust (too close to Sun)
    NONE            // No special status
}

data class GlyphToken(
    val text: String,
    val isAspectGlyph: Boolean = false
)

/**
 * Aspect glyph constants for Vedic astrological aspects
 */
object AspectGlyphs {
    const val CONJUNCTION = "☌"     // 0° - planets together
    const val OPPOSITION = "☍"      // 180° - planets opposite
    const val TRINE = "△"           // 120° - harmonious
    const val SQUARE = "□"          // 90° - challenging
    const val SEXTILE = "⚹"         // 60° - moderately harmonious
    const val QUINCUNX = "⚻"        // 150° - adjustment needed
}

/**
 * Planet Unicode glyphs for visual representation
 */
object PlanetGlyphs {
    const val SUN = "☉"
    const val MOON = "☽"
    const val MARS = "♂"
    const val MERCURY = "☿"
    const val JUPITER = "♃"
    const val VENUS = "♀"
    const val SATURN = "♄"
    const val RAHU = "☊"
    const val KETU = "☋"

    fun fromPlanetName(name: String): String = when {
        name.contains("sun", ignoreCase = true) || name.contains("surya", ignoreCase = true) -> SUN
        name.contains("moon", ignoreCase = true) || name.contains("chandra", ignoreCase = true) -> MOON
        name.contains("mars", ignoreCase = true) || name.contains("mangal", ignoreCase = true) -> MARS
        name.contains("mercury", ignoreCase = true) || name.contains("budha", ignoreCase = true) -> MERCURY
        name.contains("jupiter", ignoreCase = true) || name.contains("guru", ignoreCase = true) -> JUPITER
        name.contains("venus", ignoreCase = true) || name.contains("shukra", ignoreCase = true) -> VENUS
        name.contains("saturn", ignoreCase = true) || name.contains("shani", ignoreCase = true) -> SATURN
        name.contains("rahu", ignoreCase = true) -> RAHU
        name.contains("ketu", ignoreCase = true) -> KETU
        else -> name.take(2).uppercase()
    }
}

data class EphemerisEventUi(
    val date: LocalDate,
    val timeLabel: String,
    val primaryGlyphs: List<GlyphToken>,
    val aspectGlyph: String? = null,
    val title: String,
    val subtitle: String,
    val positionText: String? = null,
    val statusType: TransitStatusType = TransitStatusType.NONE,
    val statusText: String? = null,
    val severity: EventSeverity,
    val eventKind: EventKind,
    val isHighlighted: Boolean
)

private val timeFormat: DateTimeFormatter = DateTimeFormatter.ofPattern("hh:mm a")

/**
 * Maps transit analysis data to UI-ready ephemeris events.
 * Enhanced to match the reference Ephemeris design with:
 * - Planet glyphs (☉, ☽, ♂, etc.)
 * - Aspect glyphs (☌, ☍, △, □, ⚹)
 * - Status indicators (RETROGRADE, EXALTED, DEBILITATED, etc.)
 * - Position text formatting (SIGN + DEGREE)
 */
fun mapToEphemerisEvents(
    analysis: TransitAnalyzer.TransitAnalysis,
    language: Language
): List<EphemerisEventUi> {
    val events = mutableListOf<EphemerisEventUi>()
    val anchor = analysis.transitDateTime.withSecond(0).withNano(0)

    // Map significant periods (including retrogrades)
    analysis.significantPeriods.forEach { period ->
        val desc = period.description.trim()
        val isRetrograde = desc.contains("retrograde", ignoreCase = true)
        val severity = when {
            isRetrograde -> EventSeverity.ALERT
            period.intensity >= 4 -> EventSeverity.FAVORABLE
            period.intensity <= 2 -> EventSeverity.CHALLENGING
            else -> EventSeverity.NEUTRAL
        }

        val planetNames = period.planets.map { it.getLocalizedName(language) }
        val title = if (isRetrograde && planetNames.isNotEmpty()) {
            "${planetNames.first()} ${StringResources.get(StringKey.PLANET_RETROGRADE, language)}"
        } else {
            desc.ifBlank { StringResources.get(StringKeyEphemerisUi.TRANSIT_PERIOD_FALLBACK, language) }
        }

        val glyphs = period.planets.take(2).map {
            GlyphToken(PlanetGlyphs.fromPlanetName(it.getLocalizedName(language)))
        }

        events += EphemerisEventUi(
            date = period.startDate.toLocalDate(),
            timeLabel = period.startDate.format(timeFormat),
            primaryGlyphs = glyphs.ifEmpty {
                listOf(GlyphToken(StringResources.get(StringKeyEphemerisUi.FALLBACK_TRANSIT_TOKEN, language)))
            },
            aspectGlyph = null,
            title = title,
            subtitle = StringResources.get(StringKeyEphemerisUi.INTENSITY_LABEL, language, period.intensity),
            positionText = null,
            statusType = if (isRetrograde) TransitStatusType.RETROGRADE else TransitStatusType.NONE,
            statusText = if (isRetrograde) StringResources.get(StringKeyEphemerisUi.MOTION_REVERSED, language) else null,
            severity = severity,
            eventKind = if (isRetrograde) EventKind.RETROGRADE else EventKind.PERIOD,
            isHighlighted = severity == EventSeverity.ALERT
        )
    }

    // Map transit aspects with proper glyphs
    analysis.transitAspects
        .sortedByDescending { it.strength }
        .take(10)
        .forEachIndexed { index, aspect ->
            val eventDateTime = deterministicOffset(anchor, index, 73)
            val aspectName = StringResources.get(aspect.aspectKey, language)

            // Determine aspect glyph based on aspect type
            val aspectGlyph = when {
                aspectName.contains("conjunction", ignoreCase = true) -> AspectGlyphs.CONJUNCTION
                aspectName.contains("opposition", ignoreCase = true) -> AspectGlyphs.OPPOSITION
                aspectName.contains("trine", ignoreCase = true) -> AspectGlyphs.TRINE
                aspectName.contains("square", ignoreCase = true) -> AspectGlyphs.SQUARE
                aspectName.contains("sextile", ignoreCase = true) -> AspectGlyphs.SEXTILE
                aspectName.contains("quincunx", ignoreCase = true) -> AspectGlyphs.QUINCUNX
                else -> "→"
            }

            val transitPlanetGlyph = PlanetGlyphs.fromPlanetName(aspect.transitingPlanet.getLocalizedName(language))
            val natalPlanetGlyph = PlanetGlyphs.fromPlanetName(aspect.natalPlanet.getLocalizedName(language))

            val title = StringResources.get(
                StringKeyEphemerisUi.ASPECTING_TEMPLATE,
                language,
                aspect.transitingPlanet.getLocalizedName(language),
                aspect.natalPlanet.getLocalizedName(language)
            )
            val severity = when {
                aspectName.contains("square", ignoreCase = true) || aspectName.contains("opposition", ignoreCase = true) -> EventSeverity.CHALLENGING
                aspectName.contains("trine", ignoreCase = true) || aspectName.contains("sextile", ignoreCase = true) -> EventSeverity.FAVORABLE
                aspect.strength >= 0.7 -> EventSeverity.FAVORABLE
                else -> EventSeverity.NEUTRAL
            }

            events += EphemerisEventUi(
                date = eventDateTime.toLocalDate(),
                timeLabel = eventDateTime.format(timeFormat),
                primaryGlyphs = listOf(
                    GlyphToken(transitPlanetGlyph),
                    GlyphToken(natalPlanetGlyph)
                ),
                aspectGlyph = aspectGlyph,
                title = title,
                subtitle = aspectName.uppercase(),
                positionText = StringResources.get(StringKeyEphemerisUi.ORB_LABEL, language, aspect.orb.roundToInt()),
                statusType = TransitStatusType.NONE,
                statusText = "${(aspect.strength * 100).roundToInt()}%",
                severity = severity,
                eventKind = EventKind.ASPECT,
                isHighlighted = severity == EventSeverity.CHALLENGING || aspect.strength >= 0.85
            )
        }

    // Map gochara (transit positions) with dignity indicators
    analysis.gocharaResults
        .sortedBy { it.planet.ordinal }
        .take(9)
        .forEachIndexed { index, gochara ->
            val eventDateTime = deterministicOffset(anchor, index, 47)
            val planetName = gochara.planet.getLocalizedName(language)
            val signName = gochara.transitSign.getLocalizedName(language)
            val planetGlyph = PlanetGlyphs.fromPlanetName(planetName)

            val title = StringResources.get(StringKeyEphemerisUi.IN_TEMPLATE, language, planetName, signName)
            val effectLabel = StringResources.get(gochara.effect.key, language)

            // Determine dignity status
            val statusType = when (gochara.effect) {
                TransitAnalyzer.TransitEffect.EXCELLENT -> TransitStatusType.EXALTED
                TransitAnalyzer.TransitEffect.DIFFICULT -> TransitStatusType.DEBILITATED
                TransitAnalyzer.TransitEffect.GOOD -> TransitStatusType.OWN_SIGN
                else -> TransitStatusType.NONE
            }

            val statusText = when (statusType) {
                TransitStatusType.EXALTED -> StringResources.get(StringKeyEphemerisUi.STATUS_EXALTED, language)
                TransitStatusType.DEBILITATED -> StringResources.get(StringKeyEphemerisUi.STATUS_DEBILITATED, language)
                TransitStatusType.OWN_SIGN -> StringResources.get(StringKeyEphemerisUi.STATUS_OWN_SIGN, language)
                else -> null
            }

            events += EphemerisEventUi(
                date = eventDateTime.toLocalDate(),
                timeLabel = eventDateTime.format(timeFormat),
                primaryGlyphs = listOf(GlyphToken(planetGlyph)),
                aspectGlyph = null,
                title = title,
                subtitle = effectLabel,
                positionText = signName.uppercase(),
                statusType = statusType,
                statusText = statusText,
                severity = when (gochara.effect) {
                    TransitAnalyzer.TransitEffect.EXCELLENT, TransitAnalyzer.TransitEffect.GOOD -> EventSeverity.FAVORABLE
                    TransitAnalyzer.TransitEffect.NEUTRAL -> EventSeverity.NEUTRAL
                    TransitAnalyzer.TransitEffect.CHALLENGING, TransitAnalyzer.TransitEffect.DIFFICULT -> EventSeverity.CHALLENGING
                },
                eventKind = if (statusType != TransitStatusType.NONE) EventKind.DIGNITY else EventKind.POSITION,
                isHighlighted = gochara.isVedhaAffected || statusType == TransitStatusType.DEBILITATED
            )
        }

    return events
        .distinctBy { Triple(it.date, it.timeLabel, it.title) }
        .sortedWith(compareBy({ it.date }, { parseTimeSafe(it.timeLabel) }, { it.title }))
}

private fun deterministicOffset(base: LocalDateTime, index: Int, minuteStep: Int): LocalDateTime {
    val dayOffset = index / 4
    val minuteOffset = (index % 4) * minuteStep
    return base.plusDays(dayOffset.toLong()).plusMinutes(minuteOffset.toLong())
}

private fun parseTimeSafe(time: String): String = time
