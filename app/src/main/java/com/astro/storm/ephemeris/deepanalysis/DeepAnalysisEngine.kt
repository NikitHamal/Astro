package com.astro.storm.ephemeris.deepanalysis

import android.content.Context
import com.astro.storm.core.model.VedicChart
import com.astro.storm.ephemeris.deepanalysis.character.CharacterDeepAnalyzer
import com.astro.storm.ephemeris.deepanalysis.career.CareerDeepAnalyzer
import com.astro.storm.ephemeris.deepanalysis.relationship.RelationshipDeepAnalyzer
import com.astro.storm.ephemeris.deepanalysis.health.HealthDeepAnalyzer
import com.astro.storm.ephemeris.deepanalysis.wealth.WealthDeepAnalyzer
import com.astro.storm.ephemeris.deepanalysis.education.EducationDeepAnalyzer
import com.astro.storm.ephemeris.deepanalysis.spiritual.SpiritualDeepAnalyzer
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.withContext
import java.util.Collections
import java.util.WeakHashMap

/**
 * Deep Analysis Engine - Central orchestrator for comprehensive native analysis
 * 
 * This engine coordinates all deep analysis modules to produce a complete,
 * multi-dimensional personality and life profile. It integrates with:
 * - 50+ existing calculators (Shadbala, Yoga, Dasha, Transit, etc.)
 * - All divisional charts (D1-D60)
 * - Complete nakshatra analysis
 * - Ashtakavarga scoring
 * 
 * The engine uses parallel processing for performance and caching for efficiency.
 * 
 * @author AstroStorm Deep Analysis Engine
 */
object DeepAnalysisEngine {
    
    // Thread-safe cache for analysis results
    private val analysisCache = Collections.synchronizedMap(
        WeakHashMap<String, CachedAnalysis>()
    )
    
    // Cache expiry time (30 minutes)
    private const val CACHE_EXPIRY_MS = 30 * 60 * 1000L
    
    /**
     * Perform complete deep native analysis
     * 
     * This is the main entry point for generating comprehensive analysis.
     * Results are cached based on chart ID to avoid redundant calculations.
     * 
     * @param chart The birth chart to analyze
     * @param context Android context for resource access
     * @return Complete deep native analysis result
     */
    suspend fun analyzeNative(
        chart: VedicChart,
        context: Context
    ): DeepNativeAnalysis = withContext(Dispatchers.Default) {
        val chartId = generateChartId(chart)
        
        // Check cache first
        analysisCache[chartId]?.let { cached ->
            if (System.currentTimeMillis() - cached.timestamp < CACHE_EXPIRY_MS) {
                return@withContext cached.analysis
            }
        }
        
        // Perform parallel analysis of all life areas
        val analysis = performParallelAnalysis(chart, context)
        
        // Cache the result
        analysisCache[chartId] = CachedAnalysis(analysis, System.currentTimeMillis())
        
        analysis
    }
    
    /**
     * Perform analysis of all life areas in parallel for performance
     */
    private suspend fun performParallelAnalysis(
        chart: VedicChart,
        context: Context
    ): DeepNativeAnalysis = coroutineScope {
        
        // Create shared analysis context for calculator integration
        val analysisContext = AnalysisContext.create(chart, context)
        
        // Launch parallel analysis for each life area
        val characterDeferred = async { CharacterDeepAnalyzer.analyze(chart, analysisContext) }
        val careerDeferred = async { CareerDeepAnalyzer.analyze(chart, analysisContext) }
        val relationshipDeferred = async { RelationshipDeepAnalyzer.analyze(chart, analysisContext) }
        val healthDeferred = async { HealthDeepAnalyzer.analyze(chart, analysisContext) }
        val wealthDeferred = async { WealthDeepAnalyzer.analyze(chart, analysisContext) }
        val educationDeferred = async { EducationDeepAnalyzer.analyze(chart, analysisContext) }
        val spiritualDeferred = async { SpiritualDeepAnalyzer.analyze(chart, analysisContext) }
        
        // Await all results
        val characterAnalysis = characterDeferred.await()
        val careerAnalysis = careerDeferred.await()
        val relationshipAnalysis = relationshipDeferred.await()
        val healthAnalysis = healthDeferred.await()
        val wealthAnalysis = wealthDeferred.await()
        val educationAnalysis = educationDeferred.await()
        val spiritualAnalysis = spiritualDeferred.await()
        
        // Generate synthesis report combining all insights
        val synthesisReport = SynthesisGenerator.generate(
            characterAnalysis,
            careerAnalysis,
            relationshipAnalysis,
            healthAnalysis,
            wealthAnalysis,
            educationAnalysis,
            spiritualAnalysis,
            analysisContext
        )
        
        // Calculate overall score
        val overallScore = calculateOverallScore(
            characterAnalysis.personalityStrengthScore,
            careerAnalysis.careerStrengthScore,
            relationshipAnalysis.relationshipStrengthScore,
            healthAnalysis.healthStrengthScore,
            wealthAnalysis.wealthStrengthScore,
            educationAnalysis.educationStrengthScore,
            spiritualAnalysis.spiritualStrengthScore
        )
        
        DeepNativeAnalysis(
            character = characterAnalysis,
            career = careerAnalysis,
            relationship = relationshipAnalysis,
            health = healthAnalysis,
            wealth = wealthAnalysis,
            education = educationAnalysis,
            spiritual = spiritualAnalysis,
            synthesisReport = synthesisReport,
            overallScore = overallScore
        )
    }
    
    /**
     * Calculate weighted overall score from individual area scores
     */
    private fun calculateOverallScore(vararg areaScores: Double): Double {
        if (areaScores.isEmpty()) return 0.0
        return areaScores.average().coerceIn(0.0, 100.0)
    }
    
    /**
     * Generate unique chart ID for caching
     */
    private fun generateChartId(chart: VedicChart): String {
        val birthData = chart.birthData
        return "${birthData.dateTime}_${birthData.latitude}_${birthData.longitude}"
    }
    
    /**
     * Clear analysis cache
     */
    fun clearCache() {
        analysisCache.clear()
    }
    
    /**
     * Get cache size for debugging
     */
    fun getCacheSize(): Int = analysisCache.size
    
    /**
     * Internal cache entry
     */
    private data class CachedAnalysis(
        val analysis: DeepNativeAnalysis,
        val timestamp: Long
    )
}

/**
 * Synthesis Generator - Combines insights from all life areas into unified report
 */
internal object SynthesisGenerator {
    
    fun generate(
        character: CharacterDeepResult,
        career: CareerDeepResult,
        relationship: RelationshipDeepResult,
        health: HealthDeepResult,
        wealth: WealthDeepResult,
        education: EducationDeepResult,
        spiritual: SpiritualDeepResult,
        context: AnalysisContext
    ): SynthesisReport {
        
        // Aggregate all strengths
        val coreStrengths = mutableListOf<LocalizedTrait>()
        character.temperament.naturalTendencies
            .filter { it.strengthLevel == StrengthLevel.STRONG || it.strengthLevel == StrengthLevel.EXCELLENT }
            .take(3)
            .let { coreStrengths.addAll(it) }
        career.careerStrengths.take(2).let { coreStrengths.addAll(it) }
        
        // Aggregate all challenges
        val coreChallenges = mutableListOf<LocalizedTrait>()
        character.emotionalPattern.emotionalChallenges.take(2).let { coreChallenges.addAll(it) }
        career.careerChallenges.take(2).let { coreChallenges.addAll(it) }
        
        // Generate life purpose statement
        val lifePurposeStatement = generateLifePurposeStatement(character, career, spiritual, context)
        
        // Identify key life themes
        val keyLifeThemes = identifyKeyLifeThemes(character, career, relationship, context)
        
        // Identify karma indicators
        val karmaIndicators = identifyKarmaIndicators(character, spiritual, context)
        
        // Generate life path summary
        val lifePathSummary = generateLifePathSummary(
            character, career, relationship, health, wealth, education, spiritual, context
        )
        
        return SynthesisReport(
            lifePurposeStatement = lifePurposeStatement,
            coreStrengths = coreStrengths.distinctBy { it.name }.take(5),
            coreChallenges = coreChallenges.distinctBy { it.name }.take(5),
            keyLifeThemes = keyLifeThemes,
            karmaIndicators = karmaIndicators,
            lifePathSummary = lifePathSummary
        )
    }
    
    private fun generateLifePurposeStatement(
        character: CharacterDeepResult,
        career: CareerDeepResult,
        spiritual: SpiritualDeepResult,
        context: AnalysisContext
    ): LocalizedParagraph {
        val ascSign = character.ascendantAnalysis.sign
        val atmakaraka = character.atmakarakaAnalysis.planet
        
        return LocalizedParagraph(
            en = "Your life purpose centers around ${atmakaraka.getLocalizedName(com.astro.storm.core.common.Language.ENGLISH)}'s energy, " +
                "working through ${ascSign.getLocalizedName(com.astro.storm.core.common.Language.ENGLISH)} rising nature. " +
                "Your soul seeks ${character.atmakarakaAnalysis.soulDesire.en}, " +
                "with dharmic expression through ${spiritual.ninthHouseDharma.dharmaPath.en.lowercase()}. " +
                "Career fulfillment comes through ${career.tenthLordAnalysis.effectOnHouseMatters.en.lowercase()}.",
            ne = "तपाईंको जीवनको उद्देश्य ${atmakaraka.getLocalizedName(com.astro.storm.core.common.Language.NEPALI)}को ऊर्जामा केन्द्रित छ, " +
                "${ascSign.getLocalizedName(com.astro.storm.core.common.Language.NEPALI)} उदय स्वभाव मार्फत काम गर्दै। " +
                "तपाईंको आत्माले ${character.atmakarakaAnalysis.soulDesire.ne} खोज्छ, " +
                "${spiritual.ninthHouseDharma.dharmaPath.ne} मार्फत धार्मिक अभिव्यक्तिको साथ। " +
                "${career.tenthLordAnalysis.effectOnHouseMatters.ne} मार्फत क्यारियर पूर्ति आउँछ।"
        )
    }
    
    private fun identifyKeyLifeThemes(
        character: CharacterDeepResult,
        career: CareerDeepResult,
        relationship: RelationshipDeepResult,
        context: AnalysisContext
    ): List<LocalizedParagraph> {
        val themes = mutableListOf<LocalizedParagraph>()
        
        // Add personality theme
        themes.add(LocalizedParagraph(
            en = "Personal Growth: ${character.personalitySummary.en}",
            ne = "व्यक्तिगत विकास: ${character.personalitySummary.ne}"
        ))
        
        // Add career theme  
        themes.add(LocalizedParagraph(
            en = "Professional Path: ${career.careerSummary.en}",
            ne = "व्यावसायिक मार्ग: ${career.careerSummary.ne}"
        ))
        
        // Add relationship theme
        themes.add(LocalizedParagraph(
            en = "Relationships: ${relationship.relationshipSummary.en}",
            ne = "सम्बन्ध: ${relationship.relationshipSummary.ne}"
        ))
        
        return themes
    }
    
    private fun identifyKarmaIndicators(
        character: CharacterDeepResult,
        spiritual: SpiritualDeepResult,
        context: AnalysisContext
    ): List<LocalizedParagraph> {
        val indicators = mutableListOf<LocalizedParagraph>()
        
        // Add atmakaraka karma
        indicators.add(LocalizedParagraph(
            en = "Soul Karma: ${character.atmakarakaAnalysis.karmicLesson.en}",
            ne = "आत्मा कर्म: ${character.atmakarakaAnalysis.karmicLesson.ne}"
        ))
        
        // Add Ketu's past life karma
        indicators.add(LocalizedParagraph(
            en = "Past Life Pattern: ${spiritual.ketuAnalysis.pastLifeKarma.en}",
            ne = "पूर्व जीवन ढाँचा: ${spiritual.ketuAnalysis.pastLifeKarma.ne}"
        ))
        
        // Add other karmic patterns
        spiritual.karmicPatterns.take(2).forEach { pattern ->
            val patternNameEn = pattern.patternName
            // patternNameNe would be same for now as it's from the analyzer
            indicators.add(LocalizedParagraph(
                en = "$patternNameEn: ${pattern.lessonToLearn.en}",
                ne = "$patternNameEn: ${pattern.lessonToLearn.ne}"
            ))
        }
        
        return indicators
    }
    
    private fun generateLifePathSummary(
        character: CharacterDeepResult,
        career: CareerDeepResult,
        relationship: RelationshipDeepResult,
        health: HealthDeepResult,
        wealth: WealthDeepResult,
        education: EducationDeepResult,
        spiritual: SpiritualDeepResult,
        context: AnalysisContext
    ): LocalizedParagraph {
        return LocalizedParagraph(
            en = buildString {
                append("Your life path reveals a journey of ")
                append(describeStrength(character.personalityStrengthScore, "character development").lowercase())
                append(", combined with ")
                append(describeStrength(career.careerStrengthScore, "professional success").lowercase())
                append(". ")
                append("Relationships show ")
                append(describeStrength(relationship.relationshipStrengthScore, "harmony").lowercase())
                append(", while health indicators are ")
                append(describeStrength(health.healthStrengthScore, "robust").lowercase())
                append(". ")
                append("Financial prospects are ")
                append(describeStrength(wealth.wealthStrengthScore, "favorable").lowercase())
                append(", with ")
                append(describeStrength(education.educationStrengthScore, "strong academic potential").lowercase())
                append(". ")
                append("Your spiritual path shows ")
                append(describeStrength(spiritual.spiritualStrengthScore, "deep inclination").lowercase())
                append(" toward higher awareness and inner growth.")
            },
            ne = buildString {
                append("तपाईंको जीवन मार्गले ")
                append(describeStrengthNe(character.personalityStrengthScore, "चरित्र विकास").lowercase())
                append("को यात्रा प्रकट गर्दछ, ")
                append(describeStrengthNe(career.careerStrengthScore, "व्यावसायिक सफलता").lowercase())
                append("को साथ संयुक्त। ")
                append("सम्बन्धहरूले ")
                append(describeStrengthNe(relationship.relationshipStrengthScore, "सामंजस्य").lowercase())
                append(" देखाउँछ, जबकि स्वास्थ्य सूचकहरू ")
                append(describeStrengthNe(health.healthStrengthScore, "बलियो").lowercase())
                append(" छन्। ")
                append("आर्थिक सम्भावनाहरू ")
                append(describeStrengthNe(wealth.wealthStrengthScore, "अनुकूल").lowercase())
                append(" छन्, ")
                append(describeStrengthNe(education.educationStrengthScore, "बलियो शैक्षिक क्षमता").lowercase())
                append("को साथ। ")
                append("तपाईंको आध्यात्मिक मार्गले उच्च चेतना र आन्तरिक विकासतर्फ ")
                append(describeStrengthNe(spiritual.spiritualStrengthScore, "गहिरो झुकाव").lowercase())
                append(" देखाउँछ।")
            }
        )
    }
    
    private fun describeStrength(score: Double, context: String): String {
        return when {
            score >= 80 -> "exceptional $context"
            score >= 60 -> "strong $context"
            score >= 40 -> "moderate $context"
            score >= 20 -> "developing $context"
            else -> "potential for $context"
        }
    }
    
    private fun describeStrengthNe(score: Double, context: String): String {
        return when {
            score >= 80 -> "असाधारण $context"
            score >= 60 -> "बलियो $context"
            score >= 40 -> "मध्यम $context"
            score >= 20 -> "विकासशील $context"
            else -> "$context को सम्भावना"
        }
    }
}
