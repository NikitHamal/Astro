package com.astro.storm.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.astro.storm.core.model.VedicChart
import com.astro.storm.ephemeris.deepanalysis.*
import com.astro.storm.ephemeris.deepanalysis.predictions.DeepPredictions
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel for Deep Native Analysis
 * 
 * Manages the state for comprehensive deep analysis results
 * with caching and loading states.
 */
@HiltViewModel
class DeepAnalysisViewModel @Inject constructor(
    @dagger.hilt.android.qualifiers.ApplicationContext private val context: android.content.Context
) : ViewModel() {
    
    private val _uiState = MutableStateFlow<DeepAnalysisUiState>(DeepAnalysisUiState.Initial)
    val uiState: StateFlow<DeepAnalysisUiState> = _uiState.asStateFlow()
    
    private val _selectedSection = MutableStateFlow(DeepAnalysisSection.OVERVIEW)
    val selectedSection: StateFlow<DeepAnalysisSection> = _selectedSection.asStateFlow()
    
    private val _selectedTab = MutableStateFlow(0)
    val selectedTab: StateFlow<Int> = _selectedTab.asStateFlow()
    
    private val _expandedCards = MutableStateFlow<Set<String>>(emptySet())
    val expandedCards: StateFlow<Set<String>> = _expandedCards.asStateFlow()
    
    // Cache to prevent recalculation
    private var cachedChart: VedicChart? = null
    private var cachedResult: DeepNativeAnalysis? = null
    
    fun calculateDeepAnalysis(chart: VedicChart) {
        // Check cache first
        if (cachedChart?.hashCode() == chart.hashCode() && cachedResult != null) {
            _uiState.value = DeepAnalysisUiState.Success(cachedResult!!)
            return
        }
        
        viewModelScope.launch(Dispatchers.Default) {
            _uiState.value = DeepAnalysisUiState.Loading
            
            try {
                val result = DeepAnalysisEngine.analyzeNative(chart, context)
                cachedChart = chart
                cachedResult = result
                _uiState.value = DeepAnalysisUiState.Success(result)
            } catch (e: Exception) {
                val errorMsg = e.message ?: "Analysis failed"
                _uiState.value = DeepAnalysisUiState.Error(errorMsg)
            }
        }
    }
    
    fun selectSection(section: DeepAnalysisSection) {
        _selectedSection.value = section
    }
    
    fun selectTab(index: Int) {
        _selectedTab.value = index
    }
    
    fun toggleCardExpansion(cardId: String) {
        val current = _expandedCards.value.toMutableSet()
        if (cardId in current) {
            current.remove(cardId)
        } else {
            current.add(cardId)
        }
        _expandedCards.value = current
    }
    
    fun isCardExpanded(cardId: String): Boolean = cardId in _expandedCards.value
    
    fun clearCache() {
        cachedChart = null
        cachedResult = null
        _uiState.value = DeepAnalysisUiState.Initial
    }
}

/**
 * UI State for Deep Analysis
 */
sealed class DeepAnalysisUiState {
    data object Initial : DeepAnalysisUiState()
    data object Loading : DeepAnalysisUiState()
    data class Success(val analysis: DeepNativeAnalysis) : DeepAnalysisUiState()
    data class Error(val message: String) : DeepAnalysisUiState()
}

/**
 * Sections in Deep Analysis Screen
 */
enum class DeepAnalysisSection {
    OVERVIEW,
    CHARACTER,
    CAREER,
    RELATIONSHIP,
    HEALTH,
    WEALTH,
    EDUCATION,
    SPIRITUAL,
    PREDICTIONS
}

/**
 * ViewModel for Deep Predictions
 */
@HiltViewModel
@HiltViewModel
class DeepPredictionsViewModel @Inject constructor(
    @dagger.hilt.android.qualifiers.ApplicationContext private val context: android.content.Context,
    private val ephemerisEngine: com.astro.storm.ephemeris.SwissEphemerisEngine,
    private val triplePillarSynthesisEngine: com.astro.storm.ephemeris.triplepillar.TriplePillarSynthesisEngine
) : ViewModel() {
    
    // ... items ...

    fun calculatePredictions(chart: VedicChart) {
        // ... items ...
        viewModelScope.launch(Dispatchers.Default) {
            _uiState.value = DeepPredictionsUiState.Loading
            
            try {
                val context = AnalysisContext(chart, context)
                val predictions = com.astro.storm.ephemeris.deepanalysis.predictions.DeepPredictionEngine
                    .generatePredictions(chart, context, ephemerisEngine, triplePillarSynthesisEngine)
                cachedChart = chart
                // ... items ...
    
    fun selectTab(index: Int) {
        _selectedTab.value = index
    }
}

sealed class DeepPredictionsUiState {
    data object Initial : DeepPredictionsUiState()
    data object Loading : DeepPredictionsUiState()
    data class Success(val predictions: DeepPredictions) : DeepPredictionsUiState()
    data class Error(val message: String) : DeepPredictionsUiState()
}
