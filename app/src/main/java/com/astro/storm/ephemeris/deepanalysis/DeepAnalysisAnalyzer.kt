package com.astro.storm.ephemeris.deepanalysis

import android.content.Context
import com.astro.storm.core.model.*
import com.astro.storm.data.templates.TemplateSelector
import com.astro.storm.ephemeris.deepanalysis.character.CharacterDeepAnalyzer
import com.astro.storm.ephemeris.deepanalysis.career.CareerDeepAnalyzer
import com.astro.storm.ephemeris.deepanalysis.relationship.RelationshipDeepAnalyzer
import com.astro.storm.ephemeris.deepanalysis.health.HealthDeepAnalyzer
import com.astro.storm.ephemeris.deepanalysis.wealth.WealthDeepAnalyzer
import com.astro.storm.ephemeris.deepanalysis.education.EducationDeepAnalyzer
import com.astro.storm.ephemeris.deepanalysis.spiritual.SpiritualDeepAnalyzer
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Deep Analysis Analyzer - Modern replacement for DeepAnalysisEngine
 * 
 * Integrates the rule-based template system into the comprehensive native analysis.
 * This class uses Hilt injection and coordinates all localized interpretations.
 */
@Singleton
class DeepAnalysisAnalyzer @Inject constructor(
    private val templateSelector: TemplateSelector,
    @dagger.hilt.android.qualifiers.ApplicationContext private val context: Context
) {
    /**
     * Perform complete deep native analysis using templates
     */
    suspend fun analyzeNative(chart: VedicChart): DeepNativeAnalysis {
        // Use existing engine for base calculations
        val baseAnalysis = DeepAnalysisEngine.analyzeNative(chart, context)
        val analysisContext = AnalysisContext.create(chart, context)
        
        // Enhance with templates
        return baseAnalysis.copy(
            character = enhanceCharacter(baseAnalysis.character, analysisContext),
            career = enhanceCareer(baseAnalysis.career, analysisContext),
            health = enhanceHealth(baseAnalysis.health, analysisContext),
            wealth = enhanceWealth(baseAnalysis.wealth, analysisContext),
            spiritual = enhanceSpiritual(baseAnalysis.spiritual, analysisContext),
            analysisTimestamp = System.currentTimeMillis()
        )
    }

    private fun enhanceCharacter(
        base: CharacterDeepResult,
        context: AnalysisContext
    ): CharacterDeepResult {
        val ascSign = context.ascendantSign
        val moonPos = context.moonPosition
        
        // 1. Enhance Ascendant Interpretation
        val ascTemplate = templateSelector.findBestTemplate(
            category = "varga",
            varga = "D1",
            ascendant = ascSign,
            degree = context.chart.ascendant % 30.0
        )
        
        val enhancedAsc = base.ascendantAnalysis.copy(
            overallAscendantInterpretation = LocalizedParagraph(
                ascTemplate?.en ?: base.ascendantAnalysis.overallAscendantInterpretation.en,
                ascTemplate?.ne ?: base.ascendantAnalysis.overallAscendantInterpretation.ne
            )
        )
        
        // 2. Enhance Moon Interpretation
        val moonTemplate = moonPos?.let {
            templateSelector.findBestTemplate(
                category = "varga",
                varga = "D1",
                planet = Planet.MOON,
                sign = it.sign,
                house = it.house
            )
        }
        
        val enhancedMoon = base.moonAnalysis.copy(
            overallMoonInterpretation = LocalizedParagraph(
                moonTemplate?.en ?: base.moonAnalysis.overallMoonInterpretation.en,
                moonTemplate?.ne ?: base.moonAnalysis.overallMoonInterpretation.ne
            )
        )
        
        return base.copy(
            ascendantAnalysis = enhancedAsc,
            moonAnalysis = enhancedMoon
        )
    }

    private fun enhanceCareer(
        base: CareerDeepResult,
        context: AnalysisContext
    ): CareerDeepResult {
        val tenthLord = context.getHouseLord(10)
        val tenthLordPos = context.getPlanetPosition(tenthLord)
        
        val lordTemplate = tenthLordPos?.let {
            templateSelector.findBestTemplate(
                category = "house_lord",
                planet = tenthLord,
                house = it.house,
                sign = it.sign
            )
        }
        
        val enhancedLord = base.tenthLordAnalysis.copy(
            interpretation = LocalizedParagraph(
                lordTemplate?.en ?: base.tenthLordAnalysis.interpretation.en,
                lordTemplate?.ne ?: base.tenthLordAnalysis.interpretation.ne
            )
        )
        
        return base.copy(
            tenthLordAnalysis = enhancedLord
        )
    }

    private fun enhanceHealth(
        base: HealthDeepResult,
        context: AnalysisContext
    ): HealthDeepResult {
        val sixthLord = context.getHouseLord(6)
        val template = templateSelector.findBestTemplate(
            category = "life_area",
            lifeArea = LifeArea.HEALTH,
            planet = sixthLord,
            house = context.getPlanetHouse(sixthLord)
        )
        
        return if (template != null) {
            base.copy(
                healthSummary = LocalizedParagraph(template.en, template.ne)
            )
        } else base
    }

    private fun enhanceWealth(
        base: WealthDeepResult,
        context: AnalysisContext
    ): WealthDeepResult {
        val secondLord = context.getHouseLord(2)
        val template = templateSelector.findBestTemplate(
            category = "house_lord",
            planet = secondLord,
            house = context.getPlanetHouse(secondLord)
        )
        
        return if (template != null) {
            base.copy(
                wealthSummary = LocalizedParagraph(template.en, template.ne)
            )
        } else base
    }

    private fun enhanceSpiritual(
        base: SpiritualDeepResult,
        context: AnalysisContext
    ): SpiritualDeepResult {
        val ninthLord = context.getHouseLord(9)
        val template = templateSelector.findBestTemplate(
            category = "life_area",
            lifeArea = LifeArea.SPIRITUAL,
            planet = ninthLord,
            house = context.getPlanetHouse(ninthLord)
        )
        
        return if (template != null) {
            base.copy(
                spiritualSummary = LocalizedParagraph(template.en, template.ne)
            )
        } else base
    }
}
