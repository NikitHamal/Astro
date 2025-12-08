package com.astro.storm.util.formatter

import android.content.Context
import com.astro.storm.R
import com.astro.storm.data.model.VedicChart
import com.astro.storm.data.model.ZodiacSign

object ChartFormatter {
    fun formatChartName(chart: VedicChart, context: Context): String {
        return chart.birthData.name
    }

    fun formatChartDetails(chart: VedicChart, context: Context): String {
        return "${chart.birthData.dateTime} - ${chart.birthData.location}"
    }

    fun toPlainText(chart: VedicChart, context: Context): String {
        return buildString {
            appendLine("═══════════════════════════════════════════════════════════")
            appendLine("                  ${context.getString(R.string.birth_chart)}")
            appendLine("═══════════════════════════════════════════════════════════")
            appendLine()

            appendLine(context.getString(R.string.birth_information))
            appendLine("─────────────────────────────────────────────────────────")
            appendLine("${context.getString(R.string.name)}          : ${chart.birthData.name}")
            appendLine("${context.getString(R.string.date_amp_time)}   : ${chart.birthData.dateTime}")
            appendLine("${context.getString(R.string.location)}      : ${chart.birthData.location}")
            appendLine("${context.getString(R.string.coordinates)}   : ${formatCoordinate(chart.birthData.latitude, true)}, ${formatCoordinate(chart.birthData.longitude, false)}")
            appendLine("${context.getString(R.string.timezone)}      : ${chart.birthData.timezone}")
            appendLine()

            appendLine(context.getString(R.string.astronomical_data))
            appendLine("─────────────────────────────────────────────────────────")
            appendLine("${context.getString(R.string.julian_day)}    : ${String.format("%.6f", chart.julianDay)}")
            appendLine("${context.getString(R.string.ayanamsa)}      : ${chart.ayanamsaName}")
            appendLine("${context.getString(R.string.ayanamsa_value)}: ${formatDegree(chart.ayanamsa)}")
            appendLine("${context.getString(R.string.ascendant)}     : ${formatDegree(chart.ascendant)} (${getZodiacSignDisplayName(context, chart.ascendant)})")
            appendLine("${context.getString(R.string.midheaven)}     : ${formatDegree(chart.midheaven)} (${getZodiacSignDisplayName(context, chart.midheaven)})")
            appendLine("${context.getString(R.string.house_system)}  : ${context.getString(chart.houseSystem.stringRes)}")
            appendLine()

            appendLine(context.getString(R.string.planetary_positions_sidereal))
            appendLine("─────────────────────────────────────────────────────────")
            chart.planetPositions.forEach { position ->
                appendLine(PlanetFormatter.formatPlanetPosition(position, context))
            }
            appendLine()

            appendLine(context.getString(R.string.house_cusps))
            appendLine("─────────────────────────────────────────────────────────")
            chart.houseCusps.forEachIndexed { index, cusp ->
                appendLine("${context.getString(R.string.house)} ${(index + 1).toString().padStart(2)}: ${formatDegree(cusp)} (${getZodiacSignDisplayName(context, cusp)})")
            }
            appendLine()

            appendLine(context.getString(R.string.nakshatra_details))
            appendLine("─────────────────────────────────────────────────────────")
            chart.planetPositions.forEach { position ->
                appendLine("${context.getString(position.planet.stringRes).padEnd(10)}: ${context.getString(position.nakshatra.stringRes).padEnd(20)} Pada ${position.nakshatraPada} | ${context.getString(R.string.ruler)}: ${context.getString(position.nakshatra.ruler.stringRes)}")
            }
            appendLine()

            appendLine("═══════════════════════════════════════════════════════════")
            appendLine(context.getString(R.string.generated_by_astrostorm))
            appendLine(context.getString(R.string.calculation_engine_swiss_ephemeris))
            appendLine("═══════════════════════════════════════════════════════════")
        }
    }

    private fun formatCoordinate(value: Double, isLatitude: Boolean): String {
        val abs = kotlin.math.abs(value)
        val degrees = abs.toInt()
        val minutes = ((abs - degrees) * 60).toInt()
        val seconds = ((((abs - degrees) * 60) - minutes) * 60).toInt()
        val direction = if (isLatitude) {
            if (value >= 0) "N" else "S"
        } else {
            if (value >= 0) "E" else "W"
        }
        return "$degrees° $minutes' $seconds\" $direction"
    }

    private fun formatDegree(degree: Double): String {
        val normalizedDegree = (degree % 360.0 + 360.0) % 360.0
        val deg = normalizedDegree.toInt()
        val min = ((normalizedDegree - deg) * 60).toInt()
        val sec = ((((normalizedDegree - deg) * 60) - min) * 60).toInt()
        return "$deg° $min' $sec\""
    }

    private fun getZodiacSignDisplayName(context: Context, longitude: Double): String {
        val sign = ZodiacSign.fromLongitude(longitude)
        return context.getString(sign.stringRes)
    }
}
