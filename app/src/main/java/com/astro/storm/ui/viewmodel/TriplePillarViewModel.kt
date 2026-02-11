package com.astro.storm.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.astro.storm.core.common.Language
import com.astro.storm.core.common.StringKeyTriplePillar
import com.astro.storm.core.common.StringResources
import com.astro.storm.core.model.VedicChart
import com.astro.storm.ephemeris.TriplePillarEngine
import com.astro.storm.ephemeris.TriplePillarEngine.LifeAreaImpact
import com.astro.storm.ephemeris.TriplePillarEngine.MonthlyForecast
import com.astro.storm.ephemeris.TriplePillarEngine.OpportunityWindow
import com.astro.storm.ephemeris.TriplePillarEngine.PillarSynthesis
import com.astro.storm.ephemeris.TriplePillarEngine.QualityLevel
import com.astro.storm.ephemeris.TriplePillarEngine.TriplePillarResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.util.concurrent.atomic.AtomicReference
import javax.inject.Inject

/**
 * ViewModel for Triple-Pillar Predictive Engine Screen
 *
 * Manages the UI state for displaying the synthesized Dasha + Gochara + Ashtakavarga
 * prediction analysis. Handles caching, tab selection, and data transformations.
 *
 * Features:
 * - Current synthesis overview with three-pillar breakdown
 * - 12-month success probability timeline
 * - Peak opportunity and caution period detection
 * - Life area impact assessment
 * - Bilingual support (EN/NE)
 */
@HiltViewModel
class TriplePillarViewModel @Inject constructor() : ViewModel() {

    // ============================================================================
    // UI STATE
    // ============================================================================

    sealed class TriplePillarUiState {
        data object Initial : TriplePillarUiState()
        data object Loading : TriplePillarUiState()
        data class Success(
            val result: TriplePillarResult,
            val selectedTab: TriplePillarTab = TriplePillarTab.OVERVIEW
        ) : TriplePillarUiState()
        data class Error(
            val message: String,
            val messageNe: String
        ) : TriplePillarUiState()
        data object NoChart : TriplePillarUiState()
    }

    enum class TriplePillarTab {
        OVERVIEW,
        TIMELINE,
        PILLARS,
        FORECAST
    }

    // ============================================================================
    // STATE FLOWS
    // ============================================================================

    private val _uiState = MutableStateFlow<TriplePillarUiState>(TriplePillarUiState.Initial)
    val uiState: StateFlow<TriplePillarUiState> = _uiState.asStateFlow()

    // ============================================================================
    // CACHE
    // ============================================================================

    private data class CacheKey(val chartHashCode: Int, val analysisDate: LocalDate)
    private val cachedResult = AtomicReference<Pair<CacheKey, TriplePillarResult>?>(null)

    // ============================================================================
    // ACTIONS
    // ============================================================================

    /**
     * Calculate the Triple-Pillar synthesis for the given chart
     */
    fun calculateSynthesis(
        chart: VedicChart?,
        language: Language = Language.ENGLISH,
        forceRefresh: Boolean = false
    ) {
        if (chart == null) {
            _uiState.value = TriplePillarUiState.NoChart
            return
        }

        val currentDate = LocalDate.now()
        val cacheKey = CacheKey(chart.hashCode(), currentDate)

        // Check cache
        if (!forceRefresh) {
            cachedResult.get()?.let { (key, result) ->
                if (key == cacheKey) {
                    _uiState.value = TriplePillarUiState.Success(result = result)
                    return
                }
            }
        }

        _uiState.value = TriplePillarUiState.Loading

        viewModelScope.launch(Dispatchers.Default) {
            try {
                val result = TriplePillarEngine.calculateSynthesis(
                    chart = chart,
                    language = language,
                    forecastMonths = 12
                )

                if (result != null) {
                    cachedResult.set(cacheKey to result)
                    _uiState.value = TriplePillarUiState.Success(result = result)
                } else {
                    _uiState.value = TriplePillarUiState.Error(
                        message = StringResources.get(StringKeyTriplePillar.ERROR_CALCULATION_FAILED, Language.ENGLISH),
                        messageNe = StringResources.get(StringKeyTriplePillar.ERROR_CALCULATION_FAILED, Language.NEPALI)
                    )
                }
            } catch (e: Exception) {
                _uiState.value = TriplePillarUiState.Error(
                    message = "Error: ${e.message ?: "Unknown error"}",
                    messageNe = "त्रुटि: ${e.message ?: "अज्ञात त्रुटि"}"
                )
            }
        }
    }

    /**
     * Select a tab in the screen
     */
    fun selectTab(tab: TriplePillarTab) {
        val currentState = _uiState.value
        if (currentState is TriplePillarUiState.Success) {
            _uiState.value = currentState.copy(selectedTab = tab)
        }
    }

    /**
     * Refresh with force recalculation
     */
    fun refresh(chart: VedicChart?, language: Language = Language.ENGLISH) {
        calculateSynthesis(chart, language, forceRefresh = true)
    }

    /**
     * Reset state and cache
     */
    fun reset() {
        cachedResult.set(null)
        _uiState.value = TriplePillarUiState.Initial
    }

    // ============================================================================
    // DATA ACCESSORS for UI
    // ============================================================================

    /**
     * Get the current synthesis from the success state
     */
    fun getCurrentSynthesis(): PillarSynthesis? {
        val state = _uiState.value
        return if (state is TriplePillarUiState.Success) state.result.currentSynthesis else null
    }

    /**
     * Get monthly forecasts
     */
    fun getMonthlyForecasts(): List<MonthlyForecast> {
        val state = _uiState.value
        return if (state is TriplePillarUiState.Success) state.result.monthlyForecasts else emptyList()
    }

    /**
     * Get peak opportunity windows
     */
    fun getPeakWindows(): List<OpportunityWindow> {
        val state = _uiState.value
        return if (state is TriplePillarUiState.Success) state.result.peakWindows else emptyList()
    }

    /**
     * Get caution periods
     */
    fun getCautionPeriods(): List<OpportunityWindow> {
        val state = _uiState.value
        return if (state is TriplePillarUiState.Success) state.result.cautionPeriods else emptyList()
    }

    /**
     * Get life area impacts
     */
    fun getLifeAreaImpacts(): List<LifeAreaImpact> {
        val state = _uiState.value
        return if (state is TriplePillarUiState.Success) state.result.lifeAreaImpacts else emptyList()
    }

    /**
     * Get the highest scoring month in the forecast range
     */
    fun getBestMonth(): MonthlyForecast? {
        return getMonthlyForecasts().maxByOrNull { it.compositeScore }
    }

    /**
     * Get the lowest scoring month in the forecast range
     */
    fun getWorstMonth(): MonthlyForecast? {
        return getMonthlyForecasts().minByOrNull { it.compositeScore }
    }
}
