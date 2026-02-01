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
    ITHASALA(StringKeyVarshaphala.TAJIKA_ITHASALA, StringKeyVarshaphala.TAJIKA_ITHASALA_DESC, true),
    EASARAPHA(StringKeyVarshaphala.TAJIKA_EASARAPHA, StringKeyVarshaphala.TAJIKA_EASARAPHA_DESC, false),
    NAKTA(StringKeyVarshaphala.TAJIKA_NAKTA, StringKeyVarshaphala.TAJIKA_NAKTA_DESC, true),
    YAMAYA(StringKeyVarshaphala.TAJIKA_YAMAYA, StringKeyVarshaphala.TAJIKA_YAMAYA_DESC, true),
    MANAU(StringKeyVarshaphala.TAJIKA_MANAU, StringKeyVarshaphala.TAJIKA_MANAU_DESC, false),
    KAMBOOLA(StringKeyVarshaphala.TAJIKA_KAMBOOLA, StringKeyVarshaphala.TAJIKA_KAMBOOLA_DESC, true),
    GAIRI_KAMBOOLA(StringKeyVarshaphala.TAJIKA_GAIRI_KAMBOOLA, StringKeyVarshaphala.TAJIKA_GAIRI_KAMBOOLA_DESC, true),
    KHALASARA(StringKeyVarshaphala.TAJIKA_KHALASARA, StringKeyVarshaphala.TAJIKA_KHALASARA_DESC, false),
    RADDA(StringKeyVarshaphala.TAJIKA_RADDA, StringKeyVarshaphala.TAJIKA_RADDA_DESC, false),
    DUHPHALI_KUTTHA(StringKeyVarshaphala.TAJIKA_DUHPHALI_KUTTHA, StringKeyVarshaphala.TAJIKA_DUHPHALI_KUTTHA_DESC, false),
    TAMBIRA(StringKeyVarshaphala.TAJIKA_TAMBIRA, StringKeyVarshaphala.TAJIKA_TAMBIRA_DESC, true),
    KUTTHA(StringKeyVarshaphala.TAJIKA_KUTTHA, StringKeyVarshaphala.TAJIKA_KUTTHA_DESC, false),
    DURAPHA(StringKeyVarshaphala.TAJIKA_DURAPHA, StringKeyVarshaphala.TAJIKA_DURAPHA_DESC, false),
    MUTHASHILA(StringKeyVarshaphala.TAJIKA_MUTHASHILA, StringKeyVarshaphala.TAJIKA_MUTHASHILA_DESC, true),
    IKKABALA(StringKeyVarshaphala.TAJIKA_IKKABALA, StringKeyVarshaphala.TAJIKA_IKKABALA_DESC, true);

    fun getDisplayName(language: Language): String = StringResources.get(displayNameKey, language)
    fun getDescription(language: Language): String = StringResources.get(descriptionKey, language)
}

enum class AspectStrength(val displayNameKey: StringKeyInterface, val weight: Double) {
    VERY_STRONG(StringKeyGeneralPart2.ASPECT_VERY_STRONG, 1.0),
    STRONG(StringKeyGeneralPart2.ASPECT_STRONG, 0.8),
    MODERATE(StringKeyGeneralPart2.ASPECT_MODERATE, 0.6),
    WEAK(StringKeyGeneralPart2.ASPECT_WEAK, 0.4),
    VERY_WEAK(StringKeyGeneralPart2.ASPECT_VERY_WEAK, 0.2);

    fun getDisplayName(language: Language): String = StringResources.get(displayNameKey, language)
}

enum class SahamType(
    val displayNameKey: StringKeyInterface,
    val sanskritNameKey: StringKeyInterface,
    val descriptionKey: StringKeyInterface
) {
    PUNYA(StringKeyVarshaphala.SAHAM_PUNYA, StringKeyVarshaphala.SAHAM_PUNYA_SANSKRIT, StringKeyVarshaphala.SAHAM_PUNYA_DESC),
    VIDYA(StringKeyVarshaphala.SAHAM_VIDYA, StringKeyVarshaphala.SAHAM_VIDYA_SANSKRIT, StringKeyVarshaphala.SAHAM_VIDYA_DESC),
    YASHAS(StringKeyVarshaphala.SAHAM_YASHAS, StringKeyVarshaphala.SAHAM_YASHAS_SANSKRIT, StringKeyVarshaphala.SAHAM_YASHAS_DESC),
    MITRA(StringKeyVarshaphala.SAHAM_MITRA, StringKeyVarshaphala.SAHAM_MITRA_SANSKRIT, StringKeyVarshaphala.SAHAM_MITRA_DESC),
    MAHATMYA(StringKeyVarshaphala.SAHAM_MAHATMYA, StringKeyVarshaphala.SAHAM_MAHATMYA_SANSKRIT, StringKeyVarshaphala.SAHAM_MAHATMYA_DESC),
    ASHA(StringKeyVarshaphala.SAHAM_ASHA, StringKeyVarshaphala.SAHAM_ASHA_SANSKRIT, StringKeyVarshaphala.SAHAM_ASHA_DESC),
    SAMARTHA(StringKeyVarshaphala.SAHAM_SAMARTHA, StringKeyVarshaphala.SAHAM_SAMARTHA_SANSKRIT, StringKeyVarshaphala.SAHAM_SAMARTHA_DESC),
    BHRATRI(StringKeyVarshaphala.SAHAM_BHRATRI, StringKeyVarshaphala.SAHAM_BHRATRI_SANSKRIT, StringKeyVarshaphala.SAHAM_BHRATRI_DESC),
    PITRI(StringKeyVarshaphala.SAHAM_PITRI, StringKeyVarshaphala.SAHAM_PITRI_SANSKRIT, StringKeyVarshaphala.SAHAM_PITRI_DESC),
    MATRI(StringKeyVarshaphala.SAHAM_MATRI, StringKeyVarshaphala.SAHAM_MATRI_SANSKRIT, StringKeyVarshaphala.SAHAM_MATRI_DESC),
    PUTRA(StringKeyVarshaphala.SAHAM_PUTRA, StringKeyVarshaphala.SAHAM_PUTRA_SANSKRIT, StringKeyVarshaphala.SAHAM_PUTRA_DESC),
    VIVAHA(StringKeyVarshaphala.SAHAM_VIVAHA, StringKeyVarshaphala.SAHAM_VIVAHA_SANSKRIT, StringKeyVarshaphala.SAHAM_VIVAHA_DESC),
    KARMA(StringKeyVarshaphala.SAHAM_KARMA, StringKeyVarshaphala.SAHAM_KARMA_SANSKRIT, StringKeyVarshaphala.SAHAM_KARMA_DESC),
    ROGA(StringKeyVarshaphala.SAHAM_ROGA, StringKeyVarshaphala.SAHAM_ROGA_SANSKRIT, StringKeyVarshaphala.SAHAM_ROGA_DESC),
    MRITYU(StringKeyVarshaphala.SAHAM_MRITYU, StringKeyVarshaphala.SAHAM_MRITYU_SANSKRIT, StringKeyVarshaphala.SAHAM_MRITYU_DESC),
    PARADESA(StringKeyVarshaphala.SAHAM_PARADESA, StringKeyVarshaphala.SAHAM_PARADESA_SANSKRIT, StringKeyVarshaphala.SAHAM_PARADESA_DESC),
    DHANA(StringKeyVarshaphala.SAHAM_DHANA, StringKeyVarshaphala.SAHAM_DHANA_SANSKRIT, StringKeyVarshaphala.SAHAM_DHANA_DESC),
    RAJA(StringKeyVarshaphala.SAHAM_RAJA, StringKeyVarshaphala.SAHAM_RAJA_SANSKRIT, StringKeyVarshaphala.SAHAM_RAJA_DESC),
    BANDHANA(StringKeyVarshaphala.SAHAM_BANDHANA, StringKeyVarshaphala.SAHAM_BANDHANA_SANSKRIT, StringKeyVarshaphala.SAHAM_BANDHANA_DESC),
    KARYASIDDHI(StringKeyVarshaphala.SAHAM_KARYASIDDHI_TYPE, StringKeyVarshaphala.SAHAM_KARYASIDDHI_TYPE_SANSKRIT, StringKeyVarshaphala.SAHAM_KARYASIDDHI_TYPE_DESC);

    fun getDisplayName(language: Language): String = StringResources.get(displayNameKey, language)
    fun getSanskritName(language: Language): String = StringResources.get(sanskritNameKey, language)
    fun getDescription(language: Language): String = StringResources.get(descriptionKey, language)
}

enum class KeyDateType(val displayNameKey: StringKeyInterface) {
    FAVORABLE(StringKeyGeneralPart6.KEY_DATE_FAVORABLE),
    CHALLENGING(StringKeyGeneralPart6.KEY_DATE_CHALLENGING),
    IMPORTANT(StringKeyGeneralPart6.KEY_DATE_IMPORTANT),
    TRANSIT(StringKeyGeneralPart6.KEY_DATE_TRANSIT);

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
        val reportTitle = StringResources.get(StringKeyGeneralPart12.VARSHA_REPORT_TITLE, language)
        val yearLordSection = StringResources.get(StringKeyGeneralPart12.VARSHA_REPORT_SECTION_YEARLORD, language)
        val munthaSection = StringResources.get(StringKeyGeneralPart12.VARSHA_REPORT_SECTION_MUNTHA, language)
        val themesSection = StringResources.get(StringKeyGeneralPart12.VARSHA_REPORT_SECTION_THEMES, language)
        val muddaDashaSection = StringResources.get(StringKeyGeneralPart12.VARSHA_REPORT_SECTION_MUDDA, language)
        val predictionSection = StringResources.get(StringKeyGeneralPart12.VARSHA_REPORT_SECTION_PREDICTION, language)
        val currentMarker = StringResources.get(StringKeyGeneralPart12.VARSHA_REPORT_CURRENT_MARKER, language)
        val footer = StringResources.get(StringKeyGeneralPart12.VARSHA_REPORT_FOOTER, language)

        appendLine("═══════════════════════════════════════════════════════════")
        appendLine("            $reportTitle")
        appendLine("═══════════════════════════════════════════════════════════")
        appendLine()
        appendLine(StringResources.get(StringKeyGeneralPart12.VARSHA_REPORT_NAME, language, natalChart.birthData.name))
        appendLine(StringResources.get(StringKeyGeneralPart12.VARSHA_REPORT_YEAR, language, year, age))
        appendLine(StringResources.get(StringKeyGeneralPart12.VARSHA_REPORT_SOLAR_RETURN, language, solarReturnChart.solarReturnTime.toString()))
        appendLine(StringResources.get(StringKeyGeneralPart12.VARSHA_REPORT_YEAR_RATING, language, String.format("%.1f", yearRating)))
        appendLine()
        appendLine("─────────────────────────────────────────────────────────")
        appendLine("                      $yearLordSection")
        appendLine("─────────────────────────────────────────────────────────")
        appendLine(StringResources.get(StringKeyGeneralPart12.VARSHA_REPORT_YEARLORD_LINE, language, yearLord.getLocalizedName(language), yearLordStrength))
        appendLine(StringResources.get(StringKeyGeneralPart12.VARSHA_REPORT_POSITION, language, yearLordHouse))
        appendLine(yearLordDignity)
        appendLine()
        appendLine("─────────────────────────────────────────────────────────")
        appendLine("                       $munthaSection")
        appendLine("─────────────────────────────────────────────────────────")
        appendLine(StringResources.get(StringKeyGeneralPart12.VARSHA_REPORT_MUNTHA_POSITION, language, String.format("%.2f", muntha.degree), muntha.sign.getLocalizedName(language)))
        appendLine(StringResources.get(StringKeyGeneralPart12.VARSHA_REPORT_MUNTHA_HOUSE, language, muntha.house))
        appendLine(StringResources.get(StringKeyGeneralPart12.VARSHA_REPORT_MUNTHA_LORD, language, muntha.lord.getLocalizedName(language), muntha.lordHouse))
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
            appendLine(StringResources.get(StringKeyGeneralPart12.VARSHA_REPORT_DASHA_LINE, language,
                period.planet.getLocalizedName(language),
                period.startDate.toString(),
                period.endDate.toString(),
                period.days,
                marker))
        }
        appendLine()
        appendLine("─────────────────────────────────────────────────────────")
        appendLine(StringResources.get(StringKeyGeneralPart12.VARSHA_REPORT_FAVORABLE_MONTHS, language, favorableMonths.joinToString()))
        appendLine(StringResources.get(StringKeyGeneralPart12.VARSHA_REPORT_CHALLENGING_MONTHS, language, challengingMonths.joinToString()))
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


