package com.astro.vajra.ephemeris.nadi

import com.astro.vajra.core.model.VedicChart
import com.astro.vajra.core.model.ZodiacSign
import com.astro.vajra.data.templates.TemplateSelector
import com.astro.vajra.ephemeris.NadiAmshaCalculator
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NadiAmshaAnalyzer @Inject constructor(
    private val templateSelector: TemplateSelector
) {
    data class NadiAnalysis(
        val nadiNumber: Int,
        val nadiName: String,
        val predictionEn: String,
        val predictionNe: String,
        val rectificationAdviceEn: String,
        val rectificationAdviceNe: String
    )

    fun analyzeNadi(chart: VedicChart): NadiAnalysis {
        val result = NadiAmshaCalculator.calculateNadiAmsha(chart)
        val ascNadi = result.ascendantNadi
        val ascSign = ZodiacSign.fromLongitude(chart.ascendant)

        val template = templateSelector.findBestTemplate(
            category = "nadi",
            ascendant = ascSign,
            degree = chart.ascendant % 30.0
        )

        return NadiAnalysis(
            nadiNumber = ascNadi.nadiNumber,
            nadiName = getNadiName(ascNadi.nadiNumber),
            predictionEn = template?.en ?: "General results for Nadi #${ascNadi.nadiNumber}",
            predictionNe = template?.ne ?: "नाडी #${ascNadi.nadiNumber} को लागि सामान्य परिणाम",
            rectificationAdviceEn = "If these traits don't match, consider a birth time adjustment of +/- ${result.rectificationCandidates.firstOrNull()?.timeAdjustmentMinutes ?: 4} minutes.",
            rectificationAdviceNe = "यदि यी लक्षणहरू मेल खाँदैनन् भने, +/- ${result.rectificationCandidates.firstOrNull()?.timeAdjustmentMinutes ?: 4} मिनेटको जन्म समय समायोजन विचार गर्नुहोस्।"
        )
    }

    private fun getNadiName(number: Int): String {
        val names = listOf(
            "Vasudha", "Vaishnavi", "Brahmi", "Kalakuta", "Shankari", "Sudha", "Sama", "Saumya", "Suradevi", "Maya",
            "Manohara", "Madhavi", "Manjusvana", "Ghora", "Kumbhini", "Kutila", "Prabha", "Para", "Payasvini", "Mala",
            "Jagati", "Jarjhara", "Varuna", "Shesha", "Visha", "Amrita", "Bhaya", "Jada", "Kala", "Kalika",
            "Kalushya", "Kandala", "Kunda", "Kura", "Krittika", "Kalaratri", "Karali", "Kandala", "Kantha", "Kamala",
            "Kaladhara", "Kala", "Kandara", "Kalila", "Kalpavriksha", "Kamini", "Kamada", "Kanti", "Karuna", "Kala",
            "Kalyani", "Kamadhenu", "Kalika", "Kanchana", "Karala", "Kanjaka", "Kantidevi", "Karunadevi", "Kala", "Kala"
        )
        return names.getOrElse((number - 1) % names.size) { "Nadi #$number" }
    }
}
