package com.astro.vajra.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.astro.vajra.core.model.VedicChart
import com.astro.vajra.ephemeris.nativeanalysis.NativeAnalysisCalculator
import com.astro.vajra.ephemeris.nativeanalysis.NativeAnalysisResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

/**
 * ViewModel for Native Analysis - Comprehensive personality and life profile
 *
 * Manages the state for detailed analysis including:
 * - Character & Personality
 * - Career & Professional life
 * - Marriage & Relationships
 * - Health & Longevity
 * - Wealth & Finance
 * - Education & Knowledge
 * - Spiritual Path
 */
@HiltViewModel
class NativeAnalysisViewModel @Inject constructor() : ViewModel() {

    private val _uiState = MutableStateFlow<NativeAnalysisUiState>(NativeAnalysisUiState.Idle)
    val uiState: StateFlow<NativeAnalysisUiState> = _uiState.asStateFlow()

    private val _selectedTab = MutableStateFlow(0)
    val selectedTab: StateFlow<Int> = _selectedTab.asStateFlow()

    private val _selectedSection = MutableStateFlow(NativeSection.OVERVIEW)
    val selectedSection: StateFlow<NativeSection> = _selectedSection.asStateFlow()

    private var cachedChart: VedicChart? = null
    private var cachedResult: NativeAnalysisResult? = null

    fun setSelectedTab(index: Int) {
        _selectedTab.value = index
    }

    fun setSelectedSection(section: NativeSection) {
        _selectedSection.value = section
    }

    /**
     * Calculate native analysis for the given chart
     * Uses caching to avoid recalculation if chart hasn't changed
     */
    fun calculateNativeAnalysis(chart: VedicChart) {
        // Check cache first
        if (cachedChart == chart && cachedResult != null) {
            _uiState.value = NativeAnalysisUiState.Success(cachedResult!!)
            return
        }

        viewModelScope.launch {
            _uiState.value = NativeAnalysisUiState.Loading
            try {
                val result = withContext(Dispatchers.Default) {
                    NativeAnalysisCalculator.analyzeNative(chart)
                }
                cachedChart = chart
                cachedResult = result
                _uiState.value = NativeAnalysisUiState.Success(result)
            } catch (e: Exception) {
                _uiState.value = NativeAnalysisUiState.Error(e.message ?: "Analysis failed")
            }
        }
    }

    /**
     * Force recalculation (bypasses cache)
     */
    fun refreshAnalysis(chart: VedicChart) {
        cachedChart = null
        cachedResult = null
        calculateNativeAnalysis(chart)
    }

    /**
     * Clear the cached results
     */
    fun clearCache() {
        cachedChart = null
        cachedResult = null
        _uiState.value = NativeAnalysisUiState.Idle
    }
}

/**
 * UI State for Native Analysis Screen
 */
sealed class NativeAnalysisUiState {
    data object Idle : NativeAnalysisUiState()
    data object Loading : NativeAnalysisUiState()
    data class Success(val result: NativeAnalysisResult) : NativeAnalysisUiState()
    data class Error(val message: String) : NativeAnalysisUiState()
}

/**
 * Sections available in Native Analysis
 */
enum class NativeSection(val displayName: String, val displayNameNe: String) {
    OVERVIEW("Overview", "सारांश"),
    CHARACTER("Character", "चरित्र"),
    CAREER("Career", "पेशा"),
    MARRIAGE("Marriage", "विवाह"),
    HEALTH("Health", "स्वास्थ्य"),
    WEALTH("Wealth", "धन"),
    EDUCATION("Education", "शिक्षा"),
    SPIRITUAL("Spiritual", "आध्यात्मिक")
}
