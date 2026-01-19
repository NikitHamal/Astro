package com.astro.storm.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.astro.storm.core.model.VedicChart
import com.astro.storm.core.common.StringKeyInterface
import com.astro.storm.core.common.StringKeyNative
import com.astro.storm.ephemeris.nativeanalysis.NativeAnalysisCalculator
import com.astro.storm.ephemeris.nativeanalysis.NativeAnalysisResult
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
                _uiState.value = NativeAnalysisUiState.Error(StringKeyNative.LABEL_ANALYSIS_ERROR)
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
    data class Error(val messageKey: StringKeyInterface) : NativeAnalysisUiState()
}

/**
 * Sections available in Native Analysis
 */
enum class NativeSection(val titleKey: StringKeyInterface) {
    OVERVIEW(StringKeyNative.LABEL_OVERVIEW),
    CHARACTER(StringKeyNative.SECTION_CHARACTER),
    CAREER(StringKeyNative.SECTION_CAREER),
    MARRIAGE(StringKeyNative.SECTION_MARRIAGE),
    HEALTH(StringKeyNative.SECTION_HEALTH),
    WEALTH(StringKeyNative.SECTION_WEALTH),
    EDUCATION(StringKeyNative.SECTION_EDUCATION),
    SPIRITUAL(StringKeyNative.SECTION_SPIRITUAL)
}
