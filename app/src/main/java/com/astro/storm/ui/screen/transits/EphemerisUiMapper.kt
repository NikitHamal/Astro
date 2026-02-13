package com.astro.storm.ui.screen.transits

import com.astro.storm.core.common.Language
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
    POSITION
}

data class GlyphToken(
    val text: String
)

data class EphemerisEventUi(
    val date: LocalDate,
    val timeLabel: String,
    val primaryGlyphs: List<GlyphToken>,
    val title: String,
    val subtitle: String,
    val severity: EventSeverity,
    val eventKind: EventKind,
    val isHighlighted: Boolean
)

private val timeFormat: DateTimeFormatter = DateTimeFormatter.ofPattern("hh:mm a")

fun mapToEphemerisEvents(
    analysis: TransitAnalyzer.TransitAnalysis,
    language: Language
): List<EphemerisEventUi> {
    val events = mutableListOf<EphemerisEventUi>()
    val anchor = analysis.transitDateTime.withSecond(0).withNano(0)

    analysis.significantPeriods.forEach { period ->
        val desc = period.description.trim()
        val severity = when {
            desc.contains("retrograde", ignoreCase = true) -> EventSeverity.ALERT
            period.intensity >= 4 -> EventSeverity.FAVORABLE
            period.intensity <= 2 -> EventSeverity.CHALLENGING
            else -> EventSeverity.NEUTRAL
        }
        val title = desc.ifBlank { "Transit period" }
        val subtitle = "Intensity ${period.intensity}/5"
        val glyphs = period.planets.take(2).map { GlyphToken(it.getLocalizedName(language).take(2).uppercase()) }
        events += EphemerisEventUi(
            date = period.startDate.toLocalDate(),
            timeLabel = period.startDate.format(timeFormat),
            primaryGlyphs = glyphs.ifEmpty { listOf(GlyphToken("TR")) },
            title = title,
            subtitle = subtitle,
            severity = severity,
            eventKind = EventKind.PERIOD,
            isHighlighted = severity == EventSeverity.ALERT
        )
    }

    analysis.transitAspects
        .sortedByDescending { it.strength }
        .take(8)
        .forEachIndexed { index, aspect ->
            val eventDateTime = deterministicOffset(anchor, index, 73)
            val aspectName = StringResources.get(aspect.aspectKey, language)
            val title = "${aspect.transitingPlanet.getLocalizedName(language)} $aspectName ${aspect.natalPlanet.getLocalizedName(language)}"
            val severity = when {
                aspectName.contains("square", ignoreCase = true) || aspectName.contains("opposition", ignoreCase = true) -> EventSeverity.CHALLENGING
                aspect.strength >= 0.7 -> EventSeverity.FAVORABLE
                else -> EventSeverity.NEUTRAL
            }
            events += EphemerisEventUi(
                date = eventDateTime.toLocalDate(),
                timeLabel = eventDateTime.format(timeFormat),
                primaryGlyphs = listOf(
                    GlyphToken(aspect.transitingPlanet.getLocalizedName(language).take(2).uppercase()),
                    GlyphToken(aspect.natalPlanet.getLocalizedName(language).take(2).uppercase())
                ),
                title = title,
                subtitle = "Orb ${aspect.orb.roundToInt()}°, ${(aspect.strength * 100).roundToInt()}%",
                severity = severity,
                eventKind = EventKind.ASPECT,
                isHighlighted = severity == EventSeverity.CHALLENGING
            )
        }

    analysis.gocharaResults
        .sortedBy { it.planet.ordinal }
        .take(9)
        .forEachIndexed { index, gochara ->
            val eventDateTime = deterministicOffset(anchor, index, 47)
            val title = "${gochara.planet.getLocalizedName(language)} in ${gochara.transitSign.getLocalizedName(language)}"
            val effectLabel = StringResources.get(gochara.effect.key, language)
            events += EphemerisEventUi(
                date = eventDateTime.toLocalDate(),
                timeLabel = eventDateTime.format(timeFormat),
                primaryGlyphs = listOf(GlyphToken(gochara.planet.getLocalizedName(language).take(2).uppercase())),
                title = title,
                subtitle = effectLabel,
                severity = when (gochara.effect) {
                    TransitAnalyzer.TransitEffect.EXCELLENT, TransitAnalyzer.TransitEffect.GOOD -> EventSeverity.FAVORABLE
                    TransitAnalyzer.TransitEffect.NEUTRAL -> EventSeverity.NEUTRAL
                    TransitAnalyzer.TransitEffect.CHALLENGING, TransitAnalyzer.TransitEffect.DIFFICULT -> EventSeverity.CHALLENGING
                },
                eventKind = EventKind.POSITION,
                isHighlighted = gochara.isVedhaAffected
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
