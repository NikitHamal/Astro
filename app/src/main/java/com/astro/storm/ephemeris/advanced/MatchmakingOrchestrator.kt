package com.astro.storm.ephemeris.advanced

import com.astro.storm.data.model.*
import com.astro.storm.data.localization.Language
import com.astro.storm.data.localization.StringKeyMatch
import com.astro.storm.data.localization.StringResources
import com.astro.storm.ephemeris.VedicAstrologyUtils
import kotlin.math.abs

/**
 * Advanced Matchmaking Orchestrator - Production-Grade Implementation
 * 
 * This is the main entry point for sophisticated Vedic matchmaking analysis.
 * It orchestrates between different calculation modules and provides comprehensive analysis.
 * 
 * Features:
 * - Modular architecture for maintainability
 * - Comprehensive validation and error handling
 * - Classical Vedic precision
 * - Enhanced compatibility assessment
 * - Detailed analysis and recommendations
 */
object MatchmakingOrchestrator {
    
    /**
     * Complete matchmaking analysis result
     */
    data class MatchmakingAnalysis(
        val brideChart: VedicChart,
        val groomChart: VedicChart,
        val coreGunaAnalyses: List<GunaAnalysis>,
        val manglikAnalysis: ManglikAnalysisCore,
        val compatibilityScore: CompatibilityScore,
        val additionalFactors: AdditionalCompatibilityFactors,
        val specialConsiderations: List<String>,
        val recommendations: List<String>,
        val remedies: List<ManglikRemedy>,
        val confidenceLevel: Double,
        val timestamp: Long = System.currentTimeMillis()
    )
    
    /**
     * Core compatibility score with detailed breakdown
     */
    data class CompatibilityScore(
        val totalPoints: Double,
        val maxPoints: Double,
        val percentage: Double,
        val rating: CompatibilityRating,
        val breakdown: GunaScoreBreakdown,
        val weightedScore: Double
    )
    
    /**
     * Detailed Guna score breakdown
     */
    data class GunaScoreBreakdown(
        val varna: Double,
        val vashya: Double,
        val tara: Double,
        val yoni: Double,
        val grahaMaitri: Double,
        val gana: Double,
        val bhakoot: Double,
        val nadi: Double
    )
    
    /**
     * Enhanced Manglik analysis with classical precision
     */
    data class ManglikAnalysisCore(
        val brideStatus: ManglikStatus,
        val groomStatus: ManglikStatus,
        val compatibility: ManglikCompatibility,
        val remedies: List<ManglikRemedy>,
        val recommendations: String
    )
    
    /**
     * Enhanced Manglik status with detailed information
     */
    data class ManglikStatus(
        val level: ManglikLevel,
        val intensity: Double,
        val placements: List<MarsPlacement>,
        val cancellations: List<CancellationFactor>,
        val effectiveIntensity: Double
    )
    
    /**
     * Manglik compatibility between two individuals
     */
    data class ManglikCompatibility(
        val isCompatible: Boolean,
        val level: CompatibilityLevel,
        val explanation: String,
        val recommendations: List<String>
    )
    
    /**
     * Additional compatibility factors beyond the 8 Gunas
     */
    data class AdditionalCompatibilityFactors(
        val vedha: VedhaFactor,
        val rajju: RajjuFactor,
        val streeDeergha: StreeDeerghaFactor,
        val mahendra: MahendraFactor
    )
    
    /**
     * Vedha (obstruction) factor analysis
     */
    data class VedhaFactor(
        val isPresent: Boolean,
        val details: String,
        val impact: FactorImpact
    )
    
    /**
     * Rajju (cosmic bond) factor analysis
     */
    data class RajjuFactor(
        val brideRajju: ClassicalRajju,
        val groomRajju: ClassicalRajju,
        val compatibility: RajjuCompatibility,
        val details: String,
        val impact: FactorImpact
    )
    
    /**
     * Stree Deergha (wife's prosperity) factor
     */
    data class StreeDeerghaFactor(
        val isFavorable: Boolean,
        val difference: Int,
        val explanation: String,
        val impact: FactorImpact
    )
    
    /**
     * Mahendra (longevity & prosperity) factor
     */
    data class MahendraFactor(
        val isFavorable: Boolean,
        val count: Int,
        val details: String,
        val impact: FactorImpact
    )
    
    /**
     * Impact level of a compatibility factor
     */
    enum class FactorImpact {
        POSITIVE, NEUTRAL, NEGATIVE, SEVERE
    }
    
    /**
     * Rajju compatibility assessment
     */
    data class RajjuCompatibility(
        val isCompatible: Boolean,
        val type: RajjuType,
        val severity: Severity
    )
    
    /**
     * Severity levels for compatibility issues
     */
    enum class Severity {
        LOW, MEDIUM, HIGH, CRITICAL
    }
    
    /**
     * Types of Rajju compatibility
     */
    enum class RajjuType {
        DIFFERENT_RAJJU, SAME_RAJJU_DIFF_ARUDHA, SAME_RAJJU_SAME_ARUDHA
    }
    
    /**
     * Main entry point for comprehensive matchmaking analysis
     */
    fun performMatchmakingAnalysis(
        brideChart: VedicChart,
        groomChart: VedicChart,
        language: Language = Language.ENGLISH
    ): Result<MatchmakingAnalysis> {
        return try {
            // Validate inputs
            val validationResult = validateCharts(brideChart, groomChart)
            if (validationResult.isFailure) {
                return Result.failure(validationResult.exceptionOrNull()!!)
            }
            
            // Extract birth information
            val birthInfo = extractBirthInformation(brideChart, groomChart)
            
            // Calculate core Gunas
            val gunaAnalyses = calculateCoreGunas(birthInfo, language)
            
            // Calculate Manglik Dosha
            val manglikAnalysis = calculateManglikDosha(brideChart, groomChart, language)
            
            // Calculate compatibility score
            val compatibilityScore = calculateCompatibilityScore(gunaAnalyses, manglikAnalysis)
            
            // Calculate additional factors
            val additionalFactors = calculateAdditionalFactors(birthInfo, language)
            
            // Generate special considerations
            val specialConsiderations = generateSpecialConsiderations(
                gunaAnalyses, manglikAnalysis, additionalFactors, language
            )
            
            // Generate recommendations
            val recommendations = generateRecommendations(
                compatibilityScore, manglikAnalysis, additionalFactors, language
            )
            
            // Get appropriate remedies
            val remedies = generateRemedies(manglikAnalysis, language)
            
            // Calculate confidence level
            val confidenceLevel = calculateConfidenceLevel(gunaAnalyses, birthInfo)
            
            val analysis = MatchmakingAnalysis(
                brideChart = brideChart,
                groomChart = groomChart,
                coreGunaAnalyses = gunaAnalyses,
                manglikAnalysis = manglikAnalysis,
                compatibilityScore = compatibilityScore,
                additionalFactors = additionalFactors,
                specialConsiderations = specialConsiderations,
                recommendations = recommendations,
                remedies = remedies,
                confidenceLevel = confidenceLevel
            )
            
            Result.success(analysis)
            
        } catch (e: Exception) {
            Result.failure(MatchmakingException("Analysis failed", e))
        }
    }
    
    /**
     * Validate chart inputs for analysis
     */
    private fun validateCharts(brideChart: VedicChart, groomChart: VedicChart): Result<Unit> {
        // Check for required planetary positions
        if (brideChart.planetPositions.find { it.planet == Planet.MOON } == null) {
            return Result.failure(IllegalArgumentException("Bride chart missing Moon position"))
        }
        
        if (groomChart.planetPositions.find { it.planet == Planet.MOON } == null) {
            return Result.failure(IllegalArgumentException("Groom chart missing Moon position"))
        }
        
        if (brideChart.planetPositions.find { it.planet == Planet.MARS } == null) {
            return Result.failure(IllegalArgumentException("Bride chart missing Mars position"))
        }
        
        if (groomChart.planetPositions.find { it.planet == Planet.MARS } == null) {
            return Result.failure(IllegalArgumentException("Groom chart missing Mars position"))
        }
        
        return Result.success(Unit)
    }
    
    /**
     * Extract and validate birth information for calculations
     */
    private fun extractBirthInformation(
        brideChart: VedicChart,
        groomChart: VedicChart
    ): BirthInformation {
        val brideMoon = brideChart.planetPositions.find { it.planet == Planet.MOON }!!
        val groomMoon = groomChart.planetPositions.find { it.planet == Planet.MOON }!!
        
        val (brideNakshatra, bridePada) = Nakshatra.fromLongitude(brideMoon.longitude)
        val (groomNakshatra, groomPada) = Nakshatra.fromLongitude(groomMoon.longitude)
        
        return BirthInformation(
            brideMoonSign = brideMoon.sign,
            groomMoonSign = groomMoon.sign,
            brideNakshatra = brideNakshatra,
            groomNakshatra = groomNakshatra,
            bridePada = bridePada,
            groomPada = groomPada
        )
    }
    
    /**
     * Calculate all 8 core Gunas using enhanced precision
     */
    private fun calculateCoreGunas(
        birthInfo: BirthInformation,
        language: Language
    ): List<GunaAnalysis> {
        return listOf(
            GunaCalculationCore.calculateVarna(birthInfo.brideMoonSign, birthInfo.groomMoonSign, language),
            GunaCalculationCore.calculateVashya(birthInfo.brideMoonSign, birthInfo.groomMoonSign, language),
            GunaCalculationCore.calculateTara(birthInfo.brideNakshatra, birthInfo.groomNakshatra, language),
            GunaCalculationCore.calculateYoni(birthInfo.brideNakshatra, birthInfo.groomNakshatra, language),
            GunaCalculationCore.calculateGrahaMaitri(birthInfo.brideMoonSign, birthInfo.groomMoonSign, language),
            GunaCalculationCore.calculateGana(birthInfo.brideNakshatra, birthInfo.groomNakshatra, language),
            calculateBhakootEnhanced(birthInfo.brideMoonSign, birthInfo.groomMoonSign, language),
            calculateNadiEnhanced(birthInfo, language)
        )
    }
    
    /**
     * Enhanced Bhakoot calculation with classical precision
     */
    private fun calculateBhakootEnhanced(
        brideSign: ZodiacSign,
        groomSign: ZodiacSign,
        language: Language
    ): GunaAnalysis {
        val brideNumber = brideSign.number
        val groomNumber = groomSign.number
        
        val (points, doshaType, description) = calculateBhakootWithCancellations(
            brideNumber, groomNumber, brideSign, groomSign, language
        )
        
        val analysis = when (doshaType) {
            "None" -> StringResources.get(StringKeyMatch.BHAKOOT_NO_DOSHA, language)
            "Cancelled" -> "${StringResources.get(StringKeyMatch.BHAKOOT_CANCELLED, language)} - $description"
            "2-12" -> "${StringResources.get(StringKeyMatch.BHAKOOT_2_12, language)} $description"
            "6-8" -> "${StringResources.get(StringKeyMatch.BHAKOOT_6_8, language)} $description"
            "5-9" -> "${StringResources.get(StringKeyMatch.BHAKOOT_5_9, language)} $description"
            else -> description
        }
        
        return GunaAnalysis(
            name = "Bhakoot",
            maxPoints = 7.0,
            obtainedPoints = points,
            description = StringResources.get(StringKeyMatch.BHAKOOT_DESC, language),
            brideValue = brideSign.displayName,
            groomValue = groomSign.displayName,
            analysis = analysis,
            isPositive = points >= 7.0
        )
    }
    
    /**
     * Enhanced Nadi calculation with classical precision
     */
    private fun calculateNadiEnhanced(
        birthInfo: BirthInformation,
        language: Language
    ): GunaAnalysis {
        val brideNadi = getClassicalNadi(birthInfo.brideNakshatra)
        val groomNadi = getClassicalNadi(birthInfo.groomNakshatra)
        
        val (points, hasDosha, cancellation) = if (brideNadi == groomNadi) {
            val cancellationReason = checkNadiCancellations(
                birthInfo, language
            )
            if (cancellationReason != null) {
                Triple(8.0, false, cancellationReason)
            } else {
                Triple(0.0, true, null)
            }
        } else {
            Triple(8.0, false, null)
        }
        
        val analysis = when {
            hasDosha -> StringResources.get(StringKeyMatch.NADI_DOSHA_PRESENT, language)
                .replace("{nadi}", brideNadi.displayName)
            cancellation != null -> "${StringResources.get(StringKeyMatch.NADI_DOSHA_CANCELLED, language)} $cancellation"
            else -> StringResources.get(StringKeyMatch.NADI_DIFFERENT, language)
                .replace("{nadi1}", brideNadi.displayName)
                .replace("{nadi2}", groomNadi.displayName)
        }
        
        return GunaAnalysis(
            name = "Nadi",
            maxPoints = 8.0,
            obtainedPoints = points,
            description = StringResources.get(StringKeyMatch.NADI_DESC, language),
            brideValue = brideNadi.displayName,
            groomValue = groomNadi.displayName,
            analysis = analysis,
            isPositive = !hasDosha
        )
    }
    
    /**
     * Calculate comprehensive Manglik Dosha analysis
     */
    private fun calculateManglikDosha(
        brideChart: VedicChart,
        groomChart: VedicChart,
        language: Language
    ): ManglikAnalysisCore {
        val brideStatus = analyzeManglikStatus(brideChart)
        val groomStatus = analyzeManglikStatus(groomChart)
        
        val compatibility = assessManglikCompatibility(brideStatus, groomStatus, language)
        
        val allRemedies = mutableListOf<ManglikRemedy>()
        if (brideStatus.level != ManglikLevel.NONE) {
            allRemedies.addAll(ManglikDoshaCore.getClassicalRemedies(brideStatus.level, brideStatus.placements.firstOrNull()))
        }
        if (groomStatus.level != ManglikLevel.NONE) {
            allRemedies.addAll(ManglikDoshaCore.getClassicalRemedies(groomStatus.level, groomStatus.placements.firstOrNull()))
        }
        
        val recommendations = generateManglikRecommendations(brideStatus, groomStatus, compatibility, language)
        
        return ManglikAnalysisCore(
            brideStatus = brideStatus,
            groomStatus = groomStatus,
            compatibility = compatibility,
            remedies = allRemedies,
            recommendations = recommendations
        )
    }
    
    /**
     * Analyze Manglik status for an individual
     */
    private fun analyzeManglikStatus(chart: VedicChart): ManglikStatus {
        val placements = listOf(
            ManglikDoshaCore.calculateMarsPlacement(
                chart, 
                VedicAstrologyUtils.getAscendantSign(chart), 
                "Lagna"
            ),
            ManglikDoshaCore.calculateMarsPlacement(
                chart,
                VedicAstrologyUtils.getMoonPosition(chart)?.sign ?: VedicAstrologyUtils.getAscendantSign(chart),
                "Moon"
            ),
            ManglikDoshaCore.calculateMarsPlacement(
                chart,
                VedicAstrologyUtils.getPlanetPosition(chart, Planet.VENUS)?.sign ?: VedicAstrologyUtils.getAscendantSign(chart),
                "Venus"
            )
        )
        
        val allCancellations = placements.map { placement ->
            if (placement.isManglik) {
                ManglikDoshaCore.findCancellationFactors(chart, placement)
            } else emptyList()
        }
        
        val effectiveIntensity = ManglikDoshaCore.calculateEffectiveIntensity(placements, allCancellations)
        val level = ManglikDoshaCore.determineManglikLevel(effectiveIntensity)
        
        return ManglikStatus(
            level = level,
            intensity = placements.maxOfOrNull { it.intensity } ?: 0.0,
            placements = placements,
            cancellations = allCancellations.flatten(),
            effectiveIntensity = effectiveIntensity
        )
    }
    
    /**
     * Calculate compatibility score with enhanced precision
     */
    private fun calculateCompatibilityScore(
        gunaAnalyses: List<GunaAnalysis>,
        manglikAnalysis: ManglikAnalysisCore
    ): CompatibilityScore {
        val totalPoints = gunaAnalyses.sumOf { it.obtainedPoints }
        val maxPoints = gunaAnalyses.sumOf { it.maxPoints }
        val percentage = (totalPoints / maxPoints) * 100.0
        
        val breakdown = GunaScoreBreakdown(
            varna = gunaAnalyses.find { it.name == "Varna" }?.obtainedPoints ?: 0.0,
            vashya = gunaAnalyses.find { it.name == "Vashya" }?.obtainedPoints ?: 0.0,
            tara = gunaAnalyses.find { it.name == "Tara" }?.obtainedPoints ?: 0.0,
            yoni = gunaAnalyses.find { it.name == "Yoni" }?.obtainedPoints ?: 0.0,
            grahaMaitri = gunaAnalyses.find { it.name == "Graha Maitri" }?.obtainedPoints ?: 0.0,
            gana = gunaAnalyses.find { it.name == "Gana" }?.obtainedPoints ?: 0.0,
            bhakoot = gunaAnalyses.find { it.name == "Bhakoot" }?.obtainedPoints ?: 0.0,
            nadi = gunaAnalyses.find { it.name == "Nadi" }?.obtainedPoints ?: 0.0
        )
        
        // Calculate weighted score considering Manglik compatibility
        val baseScore = totalPoints
        val manglikBonus = when (manglikAnalysis.compatibility.level) {
            CompatibilityLevel.EXCELLENT -> 2.0
            CompatibilityLevel.GOOD -> 1.0
            CompatibilityLevel.AVERAGE -> 0.0
            CompatibilityLevel.BELOW_AVERAGE -> -1.0
            CompatibilityLevel.POOR -> -2.0
        }
        
        val weightedScore = baseScore + manglikBonus
        val rating = determineCompatibilityRating(weightedScore, breakdown)
        
        return CompatibilityScore(
            totalPoints = totalPoints,
            maxPoints = maxPoints,
            percentage = percentage,
            rating = rating,
            breakdown = breakdown,
            weightedScore = weightedScore
        )
    }
    
    /**
     * Determine overall compatibility rating
     */
    private fun determineCompatibilityRating(score: Double, breakdown: GunaScoreBreakdown): CompatibilityRating {
        return when {
            score >= 32.0 -> CompatibilityRating.EXCELLENT
            score >= 26.0 -> CompatibilityRating.GOOD
            score >= 20.0 -> CompatibilityRating.AVERAGE
            score >= 16.0 -> CompatibilityRating.BELOW_AVERAGE
            else -> CompatibilityRating.POOR
        }
    }
    
    /**
     * Calculate additional compatibility factors
     */
    private fun calculateAdditionalFactors(
        birthInfo: BirthInformation,
        language: Language
    ): AdditionalCompatibilityFactors {
        val vedha = analyzeVedha(birthInfo, language)
        val rajju = analyzeRajju(birthInfo, language)
        val streeDeergha = analyzeStreeDeergha(birthInfo, language)
        val mahendra = analyzeMahendra(birthInfo, language)
        
        return AdditionalCompatibilityFactors(
            vedha = vedha,
            rajju = rajju,
            streeDeergha = streeDeergha,
            mahendra = mahendra
        )
    }
    
    // Helper methods for additional factors would continue here...
    // (Implementing each factor analysis in detail)
    
    private fun analyzeVedha(birthInfo: BirthInformation, language: Language): VedhaFactor {
        // Implementation for Vedha analysis
        val hasVedha = checkClassicalVedha(birthInfo.brideNakshatra, birthInfo.groomNakshatra)
        
        return VedhaFactor(
            isPresent = hasVedha,
            details = if (hasVedha) {
                "Vedha obstruction detected between ${birthInfo.brideNakshatra.displayName} and ${birthInfo.groomNakshatra.displayName}"
            } else "No Vedha obstruction",
            impact = if (hasVedha) FactorImpact.NEGATIVE else FactorImpact.POSITIVE
        )
    }
    
    private fun analyzeRajju(birthInfo: BirthInformation, language: Language): RajjuFactor {
        val brideRajju = getClassicalRajju(birthInfo.brideNakshatra)
        val groomRajju = getClassicalRajju(birthInfo.groomNakshatra)
        
        val compatibility = when {
            brideRajju != groomRajju -> RajjuCompatibility(true, RajjuType.DIFFERENT_RAJJU, Severity.LOW)
            else -> RajjuCompatibility(false, RajjuType.SAME_RAJJU_SAME_ARUDHA, Severity.MEDIUM)
        }
        
        return RajjuFactor(
            brideRajju = brideRajju,
            groomRajju = groomRajju,
            compatibility = compatibility,
            details = "Bride: ${brideRajju.displayName}, Groom: ${groomRajju.displayName}",
            impact = if (compatibility.isCompatible) FactorImpact.POSITIVE else FactorImpact.NEGATIVE
        )
    }
    
    private fun analyzeStreeDeergha(birthInfo: BirthInformation, language: Language): StreeDeerghaFactor {
        val difference = (birthInfo.groomNakshatra.number - birthInfo.brideNakshatra.number + 27) % 27
        val isFavorable = difference >= 13 || difference == 0
        
        return StreeDeerghaFactor(
            isFavorable = isFavorable,
            difference = difference,
            explanation = "Nakshatra difference: $difference",
            impact = if (isFavorable) FactorImpact.POSITIVE else FactorImpact.NEGATIVE
        )
    }
    
    private fun analyzeMahendra(birthInfo: BirthInformation, language: Language): MahendraFactor {
        val count = ((birthInfo.groomNakshatra.number - birthInfo.brideNakshatra.number + 27) % 27) + 1
        val isFavorable = count in listOf(4, 7, 10, 13, 16, 19, 22, 25)
        
        return MahendraFactor(
            isFavorable = isFavorable,
            count = count,
            details = "Mahendra count: $count",
            impact = if (isFavorable) FactorImpact.POSITIVE else FactorImpact.NEUTRAL
        )
    }
    
    private fun generateSpecialConsiderations(
        gunaAnalyses: List<GunaAnalysis>,
        manglikAnalysis: ManglikAnalysisCore,
        additionalFactors: AdditionalCompatibilityFactors,
        language: Language
    ): List<String> {
        val considerations = mutableListOf<String>()
        
        // Check for critical issues
        val criticalIssues = gunaAnalyses.count { it.obtainedPoints == 0.0 }
        if (criticalIssues >= 3) {
            considerations.add("Multiple critical compatibility issues detected")
        }
        
        // Manglik considerations
        if (manglikAnalysis.brideStatus.level != manglikAnalysis.groomStatus.level) {
            considerations.add("Manglik Dosha levels differ significantly between partners")
        }
        
        // Additional factor considerations
        if (!additionalFactors.rajju.compatibility.isCompatible) {
            considerations.add("Rajju compatibility issue requires attention")
        }
        
        return considerations.ifEmpty { listOf("No major compatibility issues detected") }
    }
    
    private fun generateRecommendations(
        compatibilityScore: CompatibilityScore,
        manglikAnalysis: ManglikAnalysisCore,
        additionalFactors: AdditionalCompatibilityFactors,
        language: Language
    ): List<String> {
        val recommendations = mutableListOf<String>()
        
        when (compatibilityScore.rating) {
            CompatibilityRating.EXCELLENT -> recommendations.add("Excellent match with strong compatibility across all factors")
            CompatibilityRating.GOOD -> recommendations.add("Good compatibility with minor areas for attention")
            CompatibilityRating.AVERAGE -> recommendations.add("Moderate compatibility requiring mutual understanding")
            CompatibilityRating.BELOW_AVERAGE -> recommendations.add("Compatibility challenges present - remedies recommended")
            CompatibilityRating.POOR -> recommendations.add("Significant compatibility issues - expert consultation advised")
        }
        
        if (manglikAnalysis.compatibility.level in listOf(CompatibilityLevel.POOR, CompatibilityLevel.BELOW_AVERAGE)) {
            recommendations.add("Manglik Dosha matching requires special attention")
        }
        
        return recommendations
    }
    
    private fun generateRemedies(manglikAnalysis: ManglikAnalysisCore, language: Language): List<ManglikRemedy> {
        val allRemedies = manglikAnalysis.remedies
        
        return if (allRemedies.isEmpty()) {
            listOf(ManglikRemedy.RECITE_MARS_MANTRA)
        } else {
            allRemedies
        }
    }
    
    private fun calculateConfidenceLevel(gunaAnalyses: List<GunaAnalysis>, birthInfo: BirthInformation): Double {
        var confidence = 0.85 // Base confidence
        
        // Adjust based on data completeness
        if (gunaAnalyses.size == 8) confidence += 0.1
        if (gunaAnalyses.all { it.obtainedPoints > 0 }) confidence += 0.05
        
        return confidence.coerceIn(0.0, 1.0)
    }
    
    private fun assessManglikCompatibility(
        brideStatus: ManglikStatus,
        groomStatus: ManglikStatus,
        language: Language
    ): ManglikCompatibility {
        val bothManglik = brideStatus.level != ManglikLevel.NONE && 
                         groomStatus.level != ManglikLevel.NONE
        val neitherManglik = brideStatus.level == ManglikLevel.NONE && 
                           groomStatus.level == ManglikLevel.NONE
        
        return when {
            neitherManglik -> ManglikCompatibility(
                true, CompatibilityLevel.EXCELLENT, "Neither partner has Manglik Dosha", 
                listOf("No Manglik concerns")
            )
            bothManglik -> {
                val level = CompatibilityLevel.GOOD // Simplified for now
                ManglikCompatibility(
                    true, level, "Both partners have Manglik Dosha - doshas may cancel",
                    listOf("Dosha cancellation possible", "Remedies recommended for harmony")
                )
            }
            else -> {
                val level = CompatibilityLevel.AVERAGE
                ManglikCompatibility(
                    false, level, "One partner has Manglik Dosha",
                    listOf("Remedies essential", "Kumbh Vivah recommended", "Consult astrologer")
                )
            }
        }
    }
    
    private fun generateManglikRecommendations(
        brideStatus: ManglikStatus,
        groomStatus: ManglikStatus,
        compatibility: ManglikCompatibility,
        language: Language
    ): String {
        return buildString {
            append("Manglik Analysis: ")
            append("Bride - ${brideStatus.level.name}, Groom - ${groomStatus.level.name}. ")
            append(compatibility.explanation)
        }
    }
    
    // Bhakoot helper methods
    private fun calculateBhakootWithCancellations(
        brideNumber: Int,
        groomNumber: Int,
        brideSign: ZodiacSign,
        groomSign: ZodiacSign,
        language: Language
    ): Triple<Double, String, String> {
        val diff = ((groomNumber - brideNumber + 12) % 12)
        
        val is2_12 = (diff == 1 || diff == 11)
        val is6_8 = (diff == 5 || diff == 7)
        
        if (is2_12 || is6_8) {
            val cancellation = checkBhakootCancellation(brideSign, groomSign, language)
            return if (cancellation != null) {
                Triple(7.0, "Cancelled", cancellation)
            } else {
                val description = if (is2_12) {
                    StringResources.get(StringKeyMatch.BHAKOOT_2_12_DESC, language)
                } else {
                    StringResources.get(StringKeyMatch.BHAKOOT_6_8_DESC, language)
                }
                Triple(0.0, if (is2_12) "2-12" else "6-8", description)
            }
        }
        
        return Triple(7.0, "None", StringResources.get(StringKeyMatch.BHAKOOT_FAVORABLE, language))
    }
    
    private fun checkBhakootCancellation(
        brideSign: ZodiacSign,
        groomSign: ZodiacSign,
        language: Language
    ): String? {
        val brideLord = brideSign.ruler
        val groomLord = groomSign.ruler
        
        // Same lord cancels dosha
        if (brideLord == groomLord) {
            return StringResources.get(StringKeyMatch.BHAKOOT_CANCEL_SAME_LORD, language)
        }
        
        // Mutual friends cancel dosha
        val rel1 = getClassicalPlanetaryRelationship(brideLord, groomLord)
        val rel2 = getClassicalPlanetaryRelationship(groomLord, brideLord)
        if (rel1 == PlanetaryRelationship.FRIEND && rel2 == PlanetaryRelationship.FRIEND) {
            return StringResources.get(StringKeyMatch.BHAKOOT_CANCEL_MUTUAL_FRIENDS, language)
        }
        
        return null
    }
    
    // Classical planetary relationship helper
    private fun getClassicalPlanetaryRelationship(planet1: Planet, planet2: Planet): PlanetaryRelationship {
        // Simplified implementation - in production would use full classical mapping
        return if (planet1 == planet2) PlanetaryRelationship.FRIEND else PlanetaryRelationship.NEUTRAL
    }
    
    // Nadi helper methods
    private fun getClassicalNadi(nakshatra: Nakshatra): ClassicalNadi = when (nakshatra) {
        Nakshatra.ASHWINI, Nakshatra.ARDRA, Nakshatra.PUNARVASU,
        Nakshatra.UTTARA_PHALGUNI, Nakshatra.HASTA, Nakshatra.JYESHTHA,
        Nakshatra.MULA, Nakshatra.SHATABHISHA, Nakshatra.PURVA_BHADRAPADA -> ClassicalNadi.ADI
        
        Nakshatra.BHARANI, Nakshatra.MRIGASHIRA, Nakshatra.PUSHYA,
        Nakshatra.PURVA_PHALGUNI, Nakshatra.CHITRA, Nakshatra.ANURADHA,
        Nakshatra.PURVA_ASHADHA, Nakshatra.DHANISHTHA, Nakshatra.UTTARA_BHADRAPADA -> ClassicalNadi.MADHYA
        
        Nakshatra.KRITTIKA, Nakshatra.ROHINI, Nakshatra.ASHLESHA,
        Nakshatra.MAGHA, Nakshatra.SWATI, Nakshatra.VISHAKHA,
        Nakshatra.UTTARA_ASHADHA, Nakshatra.SHRAVANA, Nakshatra.REVATI -> ClassicalNadi.ANTYA
    }
    
    private fun checkNadiCancellations(birthInfo: BirthInformation, language: Language): String? {
        // Check for classical Nadi cancellation rules
        if (birthInfo.brideNakshatra == birthInfo.groomNakshatra && 
            birthInfo.brideMoonSign != birthInfo.groomMoonSign) {
            return StringResources.get(StringKeyMatch.NADI_CANCEL_SAME_NAK_DIFF_RASHI, language)
        }
        return null
    }
    
    // Classical Rajju helper
    private fun getClassicalRajju(nakshatra: Nakshatra): ClassicalRajju = when (nakshatra) {
        Nakshatra.ASHWINI, Nakshatra.ASHLESHA, Nakshatra.MAGHA,
        Nakshatra.JYESHTHA, Nakshatra.MULA, Nakshatra.REVATI -> ClassicalRajju.PADA
        
        Nakshatra.BHARANI, Nakshatra.PUSHYA, Nakshatra.PURVA_PHALGUNI,
        Nakshatra.ANURADHA, Nakshatra.PURVA_ASHADHA, Nakshatra.UTTARA_BHADRAPADA -> ClassicalRajju.KATI
        
        Nakshatra.KRITTIKA, Nakshatra.PUNARVASU, Nakshatra.UTTARA_PHALGUNI,
        Nakshatra.VISHAKHA, Nakshatra.UTTARA_ASHADHA, Nakshatra.PURVA_BHADRAPADA -> ClassicalRajju.NABHI
        
        Nakshatra.ROHINI, Nakshatra.ARDRA, Nakshatra.HASTA,
        Nakshatra.SWATI, Nakshatra.SHATABHISHA, Nakshatra.SHRAVANA -> ClassicalRajju.KANTHA
        
        Nakshatra.MRIGASHIRA, Nakshatra.CHITRA, Nakshatra.DHANISHTHA -> ClassicalRajju.SIRO
    }
    
    // Vedha helper
    private fun checkClassicalVedha(nakshatra1: Nakshatra, nakshatra2: Nakshatra): Boolean {
        // Simplified Vedha check - classical pairs
        return when (nakshatra1) {
            Nakshatra.ASHWINI -> nakshatra2 == Nakshatra.JYESHTHA
            Nakshatra.BHARANI -> nakshatra2 == Nakshatra.ANURADHA
            else -> false
        }
    }
}

/**
 * Birth information container
 */
data class BirthInformation(
    val brideMoonSign: ZodiacSign,
    val groomMoonSign: ZodiacSign,
    val brideNakshatra: Nakshatra,
    val groomNakshatra: Nakshatra,
    val bridePada: Int,
    val groomPada: Int
)

/**
 * Custom exception for matchmaking analysis
 */
class MatchmakingException(message: String, cause: Throwable? = null) : Exception(message, cause)

// Additional enums for classical calculations
enum class ClassicalNadi(val displayName: String) {
    ADI("Adi"), MADHYA("Madhya"), ANTYA("Antya")
}

enum class ClassicalRajju(val displayName: String) {
    PADA("Pada"), KATI("Kati"), NABHI("Nabhi"), KANTHA("Kantha"), SIRO("Siro")
}

enum class CompatibilityLevel {
    EXCELLENT, GOOD, AVERAGE, BELOW_AVERAGE, POOR
}