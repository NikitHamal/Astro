package com.astro.vajra.core.model

import com.astro.vajra.core.common.Language
import com.astro.vajra.core.common.StringKey
import com.astro.vajra.core.common.StringKeyAnalysis
import com.astro.vajra.core.common.StringKeyExport
import com.astro.vajra.core.common.StringKeyUICommon
import com.astro.vajra.core.common.StringKeyUIExtra
import com.astro.vajra.core.common.StringResources
import com.astro.vajra.core.common.BikramSambatConverter

/**
 * Complete Vedic astrology chart
 */
data class VedicChart(
    val id: Long = 0,
    val birthData: BirthData,
    val julianDay: Double,
    val ayanamsa: Double,
    val ayanamsaName: String,
    val ascendant: Double,
    val midheaven: Double,
    val planetPositions: List<PlanetPosition>,
    val houseCusps: List<Double>,
    val houseSystem: HouseSystem,
    val calculationTime: Long = System.currentTimeMillis()
) {
    val planetsByHouse: Map<Int, List<PlanetPosition>> by lazy {
        planetPositions.groupBy { it.house }
    }

    fun toPlainText(language: Language = Language.ENGLISH): String {
        val colon = StringResources.get(StringKeyUIExtra.COLON_SPACE, language)
        val pipe = StringResources.get(StringKeyUIExtra.PIPE, language)

        return buildString {
            appendLine("═══════════════════════════════════════════════════════════")
            appendLine("                  ${StringResources.get(StringKeyExport.EXPORT_REPORT_TITLE, language)}")
            appendLine("═══════════════════════════════════════════════════════════")
            appendLine()

            appendLine(StringResources.get(StringKeyAnalysis.EXPORT_BIRTH_INFO, language))
            appendLine("─────────────────────────────────────────────────────────")
            appendLine("${StringResources.get(StringKeyAnalysis.EXPORT_NAME, language).padEnd(14)}$colon${birthData.name}")
            appendLine("${StringResources.get(StringKeyAnalysis.EXPORT_DATE_TIME, language).padEnd(14)}$colon${birthData.dateTime}")
            appendLine("${StringResources.get(StringKeyAnalysis.EXPORT_LOCATION, language).padEnd(14)}$colon${birthData.location}")
            appendLine("${StringResources.get(StringKeyAnalysis.EXPORT_COORDINATES, language).padEnd(14)}$colon${formatCoordinate(birthData.latitude, true, language)}, ${formatCoordinate(birthData.longitude, false, language)}")
            appendLine("${StringResources.get(StringKeyExport.EXPORT_TIMEZONE, language).padEnd(14)}$colon${birthData.timezone}")
            appendLine()

            appendLine(StringResources.get(StringKeyExport.EXPORT_ASTRO_DETAILS, language))
            appendLine("─────────────────────────────────────────────────────────")
            appendLine("${StringResources.get(StringKeyAnalysis.CHART_JULIAN_DAY, language).padEnd(14)}$colon${String.format("%.6f", julianDay)}")
            appendLine("${StringResources.get(StringKeyAnalysis.CHART_AYANAMSA, language).padEnd(14)}$colon$ayanamsaName")
            appendLine("${StringResources.get(StringKeyAnalysis.CHART_AYANAMSA, language).padEnd(14)} ${StringResources.get(StringKeyUICommon.VALUE, language)}$colon ${formatDegree(ayanamsa, language)}")
            appendLine("${StringResources.get(StringKeyAnalysis.CHART_ASCENDANT_LAGNA, language).padEnd(14)}$colon${formatDegree(ascendant, language)} (${ZodiacSign.fromLongitude(ascendant).getLocalizedName(language)})")
            appendLine("${StringResources.get(StringKeyAnalysis.CHART_MIDHEAVEN, language).padEnd(14)}$colon${formatDegree(midheaven, language)} (${ZodiacSign.fromLongitude(midheaven).getLocalizedName(language)})")
            appendLine("${StringResources.get(StringKeyAnalysis.CHART_HOUSE_SYSTEM, language).padEnd(14)}$colon${houseSystem.displayName}")
            appendLine()

            appendLine(StringResources.get(StringKeyAnalysis.EXPORT_PLANETARY_POSITIONS, language))
            appendLine("─────────────────────────────────────────────────────────")
            planetPositions.forEach { position ->
                appendLine(position.toLLMString(language))
            }
            appendLine()

            appendLine(StringResources.get(StringKeyAnalysis.EXPORT_HOUSE_CUSPS, language))
            appendLine("─────────────────────────────────────────────────────────")
            houseCusps.forEachIndexed { index, cusp ->
                val sign = ZodiacSign.fromLongitude(cusp)
                val houseText = StringResources.get(StringKey.CHART_HOUSE, language)
                appendLine("$houseText ${(index + 1).toString().padStart(2)}$colon${formatDegree(cusp, language)} (${sign.getLocalizedName(language)})")
            }
            appendLine()

            appendLine(StringResources.get(StringKeyExport.EXPORT_NAKSHATRA_DETAILS, language))
            appendLine("─────────────────────────────────────────────────────────")
            planetPositions.forEach { position ->
                val padaText = StringResources.get(StringKey.NAKSHATRA_PADA, language)
                val rulerText = StringResources.get(StringKeyExport.EXPORT_RULER, language)
                appendLine("${position.planet.getLocalizedName(language).padEnd(10)}$colon${position.nakshatra.getLocalizedName(language).padEnd(20)} $padaText ${position.nakshatraPada} $pipe $rulerText${colon}${position.nakshatra.ruler.getLocalizedName(language)}")
            }
            appendLine()

            appendLine("═══════════════════════════════════════════════════════════")
            appendLine(StringResources.get(StringKeyExport.EXPORT_GENERATED_BY, language))
            appendLine("${StringResources.get(StringKeyAnalysis.EXPORT_CALC_ENGINE, language)}")
            appendLine("═══════════════════════════════════════════════════════════")
        }
    }

    private fun formatCoordinate(value: Double, isLatitude: Boolean, language: Language): String {
        val abs = kotlin.math.abs(value)
        val degrees = abs.toInt()
        val minutes = ((abs - degrees) * 60).toInt()
        val seconds = ((((abs - degrees) * 60) - minutes) * 60).toInt()

        val direction = if (isLatitude) {
            if (value >= 0) StringResources.get(StringKeyUIExtra.DIR_N, language) else StringResources.get(StringKeyUIExtra.DIR_S, language)
        } else {
            if (value >= 0) StringResources.get(StringKeyUIExtra.DIR_E, language) else StringResources.get(StringKeyUIExtra.DIR_W, language)
        }

        val degSign = StringResources.get(StringKeyUIExtra.DEGREE, language)
        val minSign = StringResources.get(StringKeyUIExtra.ARC_MINUTE, language)
        val secSign = StringResources.get(StringKeyUIExtra.ARC_SECOND, language)

        val result = "$degrees$degSign $minutes$minSign $seconds$secSign $direction"
        return if (language == Language.NEPALI) BikramSambatConverter.toNepaliNumerals(result) else result
    }

    private fun formatDegree(degree: Double, language: Language): String {
        val normalizedDegree = (degree % 360.0 + 360.0) % 360.0
        val deg = normalizedDegree.toInt()
        val min = ((normalizedDegree - deg) * 60).toInt()
        val sec = ((((normalizedDegree - deg) * 60) - min) * 60).toInt()

        val degSign = StringResources.get(StringKeyUIExtra.DEGREE, language)
        val minSign = StringResources.get(StringKeyUIExtra.ARC_MINUTE, language)
        val secSign = StringResources.get(StringKeyUIExtra.ARC_SECOND, language)

        val result = "$deg$degSign $min$minSign $sec$secSign"
        return if (language == Language.NEPALI) BikramSambatConverter.toNepaliNumerals(result) else result
    }
}
