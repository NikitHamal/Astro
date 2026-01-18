package com.astro.storm.ephemeris.varshaphala

import com.astro.storm.core.common.Language
import com.astro.storm.core.common.StringKeyAnalysis
import com.astro.storm.core.common.StringKeyInterface
import com.astro.storm.core.common.StringResources
import com.astro.storm.core.model.*
import java.time.LocalDate
import java.time.LocalDateTime

enum class TajikaAspectType(
    val displayNameKey: StringKeyInterface,
    val descriptionKey: StringKeyInterface,
    val isPositive: Boolean
) {
    ITHASALA(StringKeyAnalysis.TAJIKA_ITHASALA, StringKeyAnalysis.TAJIKA_ITHASALA_DESC, true),
    EASARAPHA(StringKeyAnalysis.TAJIKA_EASARAPHA, StringKeyAnalysis.TAJIKA_EASARAPHA_DESC, false),
    NAKTA(StringKeyAnalysis.TAJIKA_NAKTA, StringKeyAnalysis.TAJIKA_NAKTA_DESC, true),
    YAMAYA(StringKeyAnalysis.TAJIKA_YAMAYA, StringKeyAnalysis.TAJIKA_YAMAYA_DESC, true),
    MANAU(StringKeyAnalysis.TAJIKA_MANAU, StringKeyAnalysis.TAJIKA_MANAU_DESC, false),
    KAMBOOLA(StringKeyAnalysis.TAJIKA_KAMBOOLA, StringKeyAnalysis.TAJIKA_KAMBOOLA_DESC, true),
    GAIRI_KAMBOOLA(StringKeyAnalysis.TAJIKA_GAIRI_KAMBOOLA, StringKeyAnalysis.TAJIKA_GAIRI_KAMBOOLA_DESC, true),
    KHALASARA(StringKeyAnalysis.TAJIKA_KHALASARA, StringKeyAnalysis.TAJIKA_KHALASARA_DESC, false),
    RADDA(StringKeyAnalysis.TAJIKA_RADDA, StringKeyAnalysis.TAJIKA_RADDA_DESC, false),
    DUHPHALI_KUTTHA(StringKeyAnalysis.TAJIKA_DUHPHALI_KUTTHA, StringKeyAnalysis.TAJIKA_DUHPHALI_KUTTHA_DESC, false),
    TAMBIRA(StringKeyAnalysis.TAJIKA_TAMBIRA, StringKeyAnalysis.TAJIKA_TAMBIRA_DESC, true),
    KUTTHA(StringKeyAnalysis.TAJIKA_KUTTHA, StringKeyAnalysis.TAJIKA_KUTTHA_DESC, false),
    DURAPHA(StringKeyAnalysis.TAJIKA_DURAPHA, StringKeyAnalysis.TAJIKA_DURAPHA_DESC, false),
    MUTHASHILA(StringKeyAnalysis.TAJIKA_MUTHASHILA, StringKeyAnalysis.TAJIKA_MUTHASHILA_DESC, true),
    IKKABALA(StringKeyAnalysis.TAJIKA_IKKABALA, StringKeyAnalysis.TAJIKA_IKKABALA_DESC, true);

    fun getDisplayName(language: Language): String = StringResources.get(displayNameKey, language)
    fun getDescription(language: Language): String = StringResources.get(descriptionKey, language)
}

enum class AspectStrength(val displayNameKey: StringKeyInterface, val weight: Double) {
    VERY_STRONG(StringKeyAnalysis.ASPECT_VERY_STRONG, 1.0),
    STRONG(StringKeyAnalysis.ASPECT_STRONG, 0.8),
    MODERATE(StringKeyAnalysis.ASPECT_MODERATE, 0.6),
    WEAK(StringKeyAnalysis.ASPECT_WEAK, 0.4),
    VERY_WEAK(StringKeyAnalysis.ASPECT_VERY_WEAK, 0.2);

    fun getDisplayName(language: Language): String = StringResources.get(displayNameKey, language)
}

enum class SahamType(
    val displayNameKey: StringKeyInterface,
    val sanskritNameKey: StringKeyInterface,
    val descriptionKey: StringKeyInterface
) {
    PUNYA(StringKeyAnalysis.SAHAM_PUNYA, StringKeyAnalysis.SAHAM_PUNYA_SANSKRIT, StringKeyAnalysis.SAHAM_PUNYA_DESC),
    VIDYA(StringKeyAnalysis.SAHAM_VIDYA, StringKeyAnalysis.SAHAM_VIDYA_SANSKRIT, StringKeyAnalysis.SAHAM_VIDYA_DESC),
    YASHAS(StringKeyAnalysis.SAHAM_YASHAS, StringKeyAnalysis.SAHAM_YASHAS_SANSKRIT, StringKeyAnalysis.SAHAM_YASHAS_DESC),
    MITRA(StringKeyAnalysis.SAHAM_MITRA, StringKeyAnalysis.SAHAM_MITRA_SANSKRIT, StringKeyAnalysis.SAHAM_MITRA_DESC),
    MAHATMYA(StringKeyAnalysis.SAHAM_MAHATMYA, StringKeyAnalysis.SAHAM_MAHATMYA_SANSKRIT, StringKeyAnalysis.SAHAM_MAHATMYA_DESC),
    ASHA(StringKeyAnalysis.SAHAM_ASHA, StringKeyAnalysis.SAHAM_ASHA_SANSKRIT, StringKeyAnalysis.SAHAM_ASHA_DESC),
    SAMARTHA(StringKeyAnalysis.SAHAM_SAMARTHA, StringKeyAnalysis.SAHAM_SAMARTHA_SANSKRIT, StringKeyAnalysis.SAHAM_SAMARTHA_DESC),
    BHRATRI(StringKeyAnalysis.SAHAM_BHRATRI, StringKeyAnalysis.SAHAM_BHRATRI_SANSKRIT, StringKeyAnalysis.SAHAM_BHRATRI_DESC),
    PITRI(StringKeyAnalysis.SAHAM_PITRI, StringKeyAnalysis.SAHAM_PITRI_SANSKRIT, StringKeyAnalysis.SAHAM_PITRI_DESC),
    MATRI(StringKeyAnalysis.SAHAM_MATRI, StringKeyAnalysis.SAHAM_MATRI_SANSKRIT, StringKeyAnalysis.SAHAM_MATRI_DESC),
    PUTRA(StringKeyAnalysis.SAHAM_PUTRA, StringKeyAnalysis.SAHAM_PUTRA_SANSKRIT, StringKeyAnalysis.SAHAM_PUTRA_DESC),
    VIVAHA(StringKeyAnalysis.SAHAM_VIVAHA, StringKeyAnalysis.SAHAM_VIVAHA_SANSKRIT, StringKeyAnalysis.SAHAM_VIVAHA_DESC),
    KARMA(StringKeyAnalysis.SAHAM_KARMA, StringKeyAnalysis.SAHAM_KARMA_SANSKRIT, StringKeyAnalysis.SAHAM_KARMA_DESC),
    ROGA(StringKeyAnalysis.SAHAM_ROGA, StringKeyAnalysis.SAHAM_ROGA_SANSKRIT, StringKeyAnalysis.SAHAM_ROGA_DESC),
    MRITYU(StringKeyAnalysis.SAHAM_MRITYU, StringKeyAnalysis.SAHAM_MRITYU_SANSKRIT, StringKeyAnalysis.SAHAM_MRITYU_DESC),
    PARADESA(StringKeyAnalysis.SAHAM_PARADESA, StringKeyAnalysis.SAHAM_PARADESA_SANSKRIT, StringKeyAnalysis.SAHAM_PARADESA_DESC),
    DHANA(StringKeyAnalysis.SAHAM_DHANA, StringKeyAnalysis.SAHAM_DHANA_SANSKRIT, StringKeyAnalysis.SAHAM_DHANA_DESC),
    RAJA(StringKeyAnalysis.SAHAM_RAJA, StringKeyAnalysis.SAHAM_RAJA_SANSKRIT, StringKeyAnalysis.SAHAM_RAJA_DESC),
    BANDHANA(StringKeyAnalysis.SAHAM_BANDHANA, StringKeyAnalysis.SAHAM_BANDHANA_SANSKRIT, StringKeyAnalysis.SAHAM_BANDHANA_DESC),
    KARYASIDDHI(StringKeyAnalysis.SAHAM_KARYASIDDHI_TYPE, StringKeyAnalysis.SAHAM_KARYASIDDHI_TYPE_SANSKRIT, StringKeyAnalysis.SAHAM_KARYASIDDHI_TYPE_DESC);

    fun getDisplayName(language: Language): String = StringResources.get(displayNameKey, language)
    fun getSanskritName(language: Language): String = StringResources.get(sanskritNameKey, language)
    fun getDescription(language: Language): String = StringResources.get(descriptionKey, language)
}

enum class KeyDateType(val displayNameKey: StringKeyInterface) {
    FAVORABLE(StringKeyAnalysis.KEY_DATE_FAVORABLE),
    CHALLENGING(StringKeyAnalysis.KEY_DATE_CHALLENGING),
    IMPORTANT(StringKeyAnalysis.KEY_DATE_IMPORTANT),
    TRANSIT(StringKeyAnalysis.KEY_DATE_TRANSIT);

    fun getDisplayName(language: Language): String = StringResources.get(displayNameKey, language)
}

data class SolarReturnChart(
    val year: Int,
    val solarReturnTime: LocalDateTime,
    val solarReturnTimeUtc: LocalDateTime,
    val julianDay: Double,
    val planetPositions: Map<Planet, SolarReturnPlanetPosition>,
    val ascendant: ZodiacSign,
    val ascendantDegree: Double,
    val midheaven: Double,
    val houseCusps: List<Double>,
    val ayanamsa: Double,
    val isDayBirth: Boolean,
    val moonSign: ZodiacSign,
    val moonNakshatra: String
)

data class SolarReturnPlanetPosition(
    val longitude: Double,
    val sign: ZodiacSign,
    val house: Int,
    val degree: Double,
    val nakshatra: String,
    val nakshatraPada: Int,
    val isRetrograde: Boolean,
    val speed: Double
)

data class MunthaResult(
    val longitude: Double,
    val sign: ZodiacSign,
    val house: Int,
    val degree: Double,
    val lord: Planet,
    val lordHouse: Int,
    val lordStrength: String,
    val interpretation: String,
    val themes: List<String>
)

data class SahamResult(
    val type: SahamType,
    val name: String,
    val sanskritName: String,
    val formula: String,
    val longitude: Double,
    val sign: ZodiacSign,
    val house: Int,
    val degree: Double,
    val lord: Planet,
    val lordHouse: Int,
    val lordStrength: String,
    val interpretation: String,
    val isActive: Boolean,
    val activationPeriods: List<String>
)

data class TajikaAspectResult(
    val type: TajikaAspectType,
    val planet1: Planet,
    val planet2: Planet,
    val planet1Longitude: Double,
    val planet2Longitude: Double,
    val aspectAngle: Int,
    val orb: Double,
    val isApplying: Boolean,
    val strength: AspectStrength,
    val relatedHouses: List<Int>,
    val effectDescription: String,
    val prediction: String
)

data class MuddaDashaPeriod(
    val planet: Planet,
    val startDate: LocalDate,
    val endDate: LocalDate,
    val days: Int,
    val subPeriods: List<MuddaAntardasha>,
    val planetStrength: String,
    val houseRuled: List<Int>,
    val prediction: String,
    val keywords: List<String>,
    val isCurrent: Boolean,
    val progressPercent: Float
)

data class MuddaAntardasha(
    val planet: Planet,
    val startDate: LocalDate,
    val endDate: LocalDate,
    val days: Int,
    val interpretation: String
)

data class PanchaVargiyaBala(
    val planet: Planet,
    val uchcha: Double,
    val hadda: Double,
    val dreshkana: Double,
    val navamsha: Double,
    val dwadashamsha: Double,
    val total: Double,
    val category: String
)

data class TriPatakiSector(
    val name: String,
    val signs: List<ZodiacSign>,
    val planets: List<Planet>,
    val influence: String
)

data class TriPatakiChakra(
    val risingSign: ZodiacSign,
    val sectors: List<TriPatakiSector>,
    val dominantInfluence: String,
    val interpretation: String
)

data class HousePrediction(
    val house: Int,
    val signOnCusp: ZodiacSign,
    val houseLord: Planet,
    val lordPosition: Int,
    val planetsInHouse: List<Planet>,
    val strength: String,
    val keywords: List<String>,
    val prediction: String,
    val rating: Float,
    val specificEvents: List<String>
)

data class KeyDate(
    val date: LocalDate,
    val event: String,
    val type: KeyDateType,
    val description: String
)

data class VarshaphalaResult(
    val natalChart: VedicChart,
    val year: Int,
    val age: Int,
    val solarReturnChart: SolarReturnChart,
    val yearLord: Planet,
    val yearLordStrength: String,
    val yearLordHouse: Int,
    val yearLordDignity: String,
    val muntha: MunthaResult,
    val panchaVargiyaBala: List<PanchaVargiyaBala>,
    val triPatakiChakra: TriPatakiChakra,
    val sahams: List<SahamResult>,
    val tajikaAspects: List<TajikaAspectResult>,
    val muddaDasha: List<MuddaDashaPeriod>,
    val housePredictions: List<HousePrediction>,
    val majorThemes: List<String>,
    val favorableMonths: List<Int>,
    val challengingMonths: List<Int>,
    val overallPrediction: String,
    val yearRating: Float,
    val keyDates: List<KeyDate>,
    val timestamp: Long = System.currentTimeMillis()
) {
    fun toPlainText(language: Language = Language.ENGLISH): String = buildString {
        val reportTitle = StringResources.get(StringKeyAnalysis.VARSHA_REPORT_TITLE, language)
        val yearLordSection = StringResources.get(StringKeyAnalysis.VARSHA_REPORT_SECTION_YEARLORD, language)
        val munthaSection = StringResources.get(StringKeyAnalysis.VARSHA_REPORT_SECTION_MUNTHA, language)
        val themesSection = StringResources.get(StringKeyAnalysis.VARSHA_REPORT_SECTION_THEMES, language)
        val muddaDashaSection = StringResources.get(StringKeyAnalysis.VARSHA_REPORT_SECTION_MUDDA, language)
        val predictionSection = StringResources.get(StringKeyAnalysis.VARSHA_REPORT_SECTION_PREDICTION, language)
        val currentMarker = StringResources.get(StringKeyAnalysis.VARSHA_REPORT_CURRENT_MARKER, language)
        val footer = StringResources.get(StringKeyAnalysis.VARSHA_REPORT_FOOTER, language)

        appendLine("═══════════════════════════════════════════════════════════")
        appendLine("            $reportTitle")
        appendLine("═══════════════════════════════════════════════════════════")
        appendLine()
        appendLine(StringResources.get(StringKeyAnalysis.VARSHA_REPORT_NAME, language, natalChart.birthData.name))
        appendLine(StringResources.get(StringKeyAnalysis.VARSHA_REPORT_YEAR, language, year, age))
        appendLine(StringResources.get(StringKeyAnalysis.VARSHA_REPORT_SOLAR_RETURN, language, solarReturnChart.solarReturnTime.toString()))
        appendLine(StringResources.get(StringKeyAnalysis.VARSHA_REPORT_YEAR_RATING, language, String.format("%.1f", yearRating)))
        appendLine()
        appendLine("─────────────────────────────────────────────────────────")
        appendLine("                      $yearLordSection")
        appendLine("─────────────────────────────────────────────────────────")
        appendLine(StringResources.get(StringKeyAnalysis.VARSHA_REPORT_YEARLORD_LINE, language, yearLord.getLocalizedName(language), yearLordStrength))
        appendLine(StringResources.get(StringKeyAnalysis.VARSHA_REPORT_POSITION, language, yearLordHouse))
        appendLine(yearLordDignity)
        appendLine()
        appendLine("─────────────────────────────────────────────────────────")
        appendLine("                       $munthaSection")
        appendLine("─────────────────────────────────────────────────────────")
        appendLine(StringResources.get(StringKeyAnalysis.VARSHA_REPORT_MUNTHA_POSITION, language, String.format("%.2f", muntha.degree), muntha.sign.getLocalizedName(language)))
        appendLine(StringResources.get(StringKeyAnalysis.VARSHA_REPORT_MUNTHA_HOUSE, language, muntha.house))
        appendLine(StringResources.get(StringKeyAnalysis.VARSHA_REPORT_MUNTHA_LORD, language, muntha.lord.getLocalizedName(language), muntha.lordHouse))
        appendLine(muntha.interpretation)
        appendLine()
        appendLine("─────────────────────────────────────────────────────────")
        appendLine("                    $themesSection")
        appendLine("─────────────────────────────────────────────────────────")
        majorThemes.forEach { appendLine("• $it") }
        appendLine()
        appendLine("─────────────────────────────────────────────────────────")
        appendLine("                   $muddaDashaSection")
        appendLine("─────────────────────────────────────────────────────────")
        muddaDasha.forEach { period ->
            val marker = if (period.isCurrent) currentMarker else ""
            appendLine(StringResources.get(StringKeyAnalysis.VARSHA_REPORT_DASHA_LINE, language,
                period.planet.getLocalizedName(language),
                period.startDate.toString(),
                period.endDate.toString(),
                period.days,
                marker))
        }
        appendLine()
        appendLine("─────────────────────────────────────────────────────────")
        appendLine(StringResources.get(StringKeyAnalysis.VARSHA_REPORT_FAVORABLE_MONTHS, language, favorableMonths.joinToString()))
        appendLine(StringResources.get(StringKeyAnalysis.VARSHA_REPORT_CHALLENGING_MONTHS, language, challengingMonths.joinToString()))
        appendLine("─────────────────────────────────────────────────────────")
        appendLine()
        appendLine("                   $predictionSection")
        appendLine("─────────────────────────────────────────────────────────")
        appendLine(overallPrediction)
        appendLine()
        appendLine("═══════════════════════════════════════════════════════════")
        appendLine(footer)
        appendLine("═══════════════════════════════════════════════════════════")
    }
}


