package com.astro.vajra.ephemeris.shoola

import com.astro.vajra.core.model.Planet
import com.astro.vajra.core.model.ZodiacSign
import java.time.LocalDateTime

enum class RudraType(val description: String, val descriptionNe: String) {
    PRIMARY("Primary Rudra - Most malefic influence", "प्राथमिक रुद्र - सबैभन्दा अशुभ प्रभाव"),
    SECONDARY("Secondary Rudra - Supporting malefic", "द्वितीय रुद्र - सहायक अशुभ")
}

enum class HealthSeverity(val displayName: String, val displayNameNe: String, val level: Int) {
    CRITICAL("Critical", "गम्भीर", 5), HIGH("High", "उच्च", 4), MODERATE("Moderate", "मध्यम", 3), LOW("Low", "न्यून", 2), MINIMAL("Minimal", "न्यूनतम", 1), NONE("None", "छैन", 0)
}

enum class PeriodNature(val displayName: String, val displayNameNe: String) {
    VERY_CHALLENGING("Very Challenging", "अत्यन्त चुनौतीपूर्ण"), CHALLENGING("Challenging", "चुनौतीपूर्ण"), MIXED("Mixed", "मिश्रित"), SUPPORTIVE("Supportive", "सहायक"), FAVORABLE("Favorable", "अनुकूल")
}

data class TriMurtiAnalysis(
    val brahma: Planet?, val brahmaSign: ZodiacSign?, val brahmaStrength: Double, val brahmaInterpretation: String,
    val rudra: Planet, val rudraSign: ZodiacSign?, val rudraStrength: Double, val rudraType: RudraType, val rudraInterpretation: String,
    val maheshwara: Planet?, val maheshwaraSign: ZodiacSign?, val maheshwaraInterpretation: String,
    val secondaryRudra: Planet?, val secondaryRudraSign: ZodiacSign?
)

data class ShoolaDashaPeriod(
    val sign: ZodiacSign, val signLord: Planet, val startDate: LocalDateTime, val endDate: LocalDateTime, val durationYears: Double,
    val nature: PeriodNature, val healthSeverity: HealthSeverity, val isCurrent: Boolean, val progress: Double, val significantPlanets: List<Planet>,
    val healthConcerns: List<String>, val healthConcernsNe: List<String>, val precautions: List<String>, val precautionsNe: List<String>,
    val interpretation: String, val interpretationNe: String
)

data class ShoolaAntardasha(
    val sign: ZodiacSign, val signLord: Planet, val startDate: LocalDateTime, val endDate: LocalDateTime, val durationMonths: Double,
    val nature: PeriodNature, val healthSeverity: HealthSeverity, val isCurrent: Boolean, val interpretation: String, val interpretationNe: String
)

data class HealthVulnerabilityPeriod(
    val startDate: LocalDateTime, val endDate: LocalDateTime, val severity: HealthSeverity, val dashaSign: ZodiacSign, val antardashSign: ZodiacSign?,
    val concerns: List<String>, val concernsNe: List<String>, val bodyParts: List<String>, val bodyPartsNe: List<String>,
    val recommendations: List<String>, val recommendationsNe: List<String>
)

data class ShoolaDashaResult(
    val triMurti: TriMurtiAnalysis, val startingSign: ZodiacSign, val dashaDirection: DashaDirection, val mahadashas: List<ShoolaDashaPeriod>,
    val currentMahadasha: ShoolaDashaPeriod?, val currentAntardasha: ShoolaAntardasha?, val antardashas: List<ShoolaAntardasha>,
    val healthVulnerabilities: List<HealthVulnerabilityPeriod>, val upcomingCriticalPeriods: List<HealthVulnerabilityPeriod>,
    val longevityAssessment: LongevityAssessment, val remedies: List<ShoolaRemedy>, val overallAssessment: String,
    val overallAssessmentNe: String, val systemApplicability: Double
)

enum class DashaDirection(val displayName: String, val displayNameNe: String) {
    DIRECT("Direct (Zodiacal)", "प्रत्यक्ष (राशिक्रम)"), REVERSE("Reverse (Anti-zodiacal)", "विपरीत (राशिविरुद्ध)")
}

data class LongevityAssessment(
    val category: LongevityCategory,
    val estimatedRange: String,
    val estimatedRangeNe: String,
    val supportingFactors: List<String>,
    val supportingFactorsNe: List<String>,
    val challengingFactors: List<String>,
    val challengingFactorsNe: List<String>,
    val interpretation: String,
    val interpretationNe: String,
    val appliedRules: List<ShoolaRuleApplication> = emptyList()
) {
    /**
     * Generate detailed explanation of longevity calculation
     */
    fun generateExplanation(): String = buildString {
        appendLine("═══════════════════════════════════════════")
        appendLine("SHOOLA DASHA LONGEVITY ANALYSIS")
        appendLine("═══════════════════════════════════════════")
        appendLine()
        appendLine("Final Longevity Category: ${category.displayName} (${category.displayNameNe})")
        appendLine("Estimated Range: ${category.yearsRange} years")
        appendLine()
        appendLine("Classical Rules Applied:")
        appendLine()

        appliedRules.forEachIndexed { index, rule ->
            appendLine("${index + 1}. ${rule.ruleName}")
            if (rule.sutraReference.isNotBlank()) {
                appendLine("   Reference: ${rule.sutraReference}")
            }
            appendLine("   Observation: ${rule.observation}")
            appendLine("   Result: ${rule.result}")
            appendLine()
        }

        if (supportingFactors.isNotEmpty()) {
            appendLine("Supporting Factors:")
            supportingFactors.forEach { appendLine("• $it") }
            appendLine()
        }

        if (challengingFactors.isNotEmpty()) {
            appendLine("Challenging Factors:")
            challengingFactors.forEach { appendLine("• $it") }
        }
    }
}

/**
 * Rule application record for Shoola Dasha explainability
 */
data class ShoolaRuleApplication(
    val ruleName: String,
    val sutraReference: String,
    val condition: String,
    val observation: String,
    val result: String,
    val weight: Double
)

enum class LongevityCategory(val displayName: String, val displayNameNe: String, val yearsRange: String) {
    BALARISHTA("Balarishta", "बालारिष्ट", "0-8"),
    ALPAYU("Alpayu (Short)", "अल्पायु", "8-32"),
    MADHYAYU("Madhyayu (Medium)", "मध्यायु", "32-70"),
    POORNAYU("Poornayu (Full)", "पूर्णायु", "70-100"),
    AMITAYU("Amitayu (Extended)", "अमितायु", "100+")
}

data class ShoolaRemedy(
    val targetPlanet: Planet?, val targetSign: ZodiacSign?, val remedyType: RemedyType, val description: String, val descriptionNe: String,
    val mantra: String?, val deity: String?, val deityNe: String?, val bestDay: String?, val bestDayNe: String?, val effectiveness: Int
)

enum class RemedyType(val displayName: String, val displayNameNe: String) {
    MANTRA("Mantra Japa", "मन्त्र जप"), PUJA("Puja/Worship", "पूजा"), DONATION("Charity", "दान"), FASTING("Fasting", "व्रत"), GEMSTONE("Gemstone", "रत्न"), YANTRA("Yantra", "यन्त्र"), LIFESTYLE("Lifestyle Change", "जीवनशैली परिवर्तन")
}
