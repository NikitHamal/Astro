package com.astro.storm.ephemeris.varga

import com.astro.storm.core.common.Language
import com.astro.storm.core.model.VedicChart

/**
 * DivisionalChartAnalyzer - Orchestrator for refactored Divisional Chart analysis
 */
object DivisionalChartAnalyzer {

    fun analyzeHora(chart: VedicChart, language: Language): HoraAnalysis = 
        HoraAnalyzer.analyzeHora(chart, language)

    fun analyzeDrekkana(chart: VedicChart, language: Language): DrekkanaAnalysis = 
        DrekkanaAnalyzer.analyzeDrekkana(chart, language)

    fun analyzeNavamsaForMarriage(chart: VedicChart, language: Language): NavamsaMarriageAnalysis = 
        NavamsaMarriageAnalyzer.analyzeNavamsaForMarriage(chart, language)

    fun analyzeDashamsa(chart: VedicChart, language: Language): DashamsaAnalysis = 
        DashamsaAnalyzer.analyzeDashamsa(chart, language)

    fun analyzeDwadasamsa(chart: VedicChart, language: Language): DwadasamsaAnalysis = 
        DwadasamsaAnalyzer.analyzeDwadasamsa(chart, language)

    fun analyzeGenericVarga(chart: VedicChart, type: DivisionalChartType, language: Language): GenericVargaAnalysis =
        GenericVargaAnalyzer.analyzeVarga(chart, type, language)
}
