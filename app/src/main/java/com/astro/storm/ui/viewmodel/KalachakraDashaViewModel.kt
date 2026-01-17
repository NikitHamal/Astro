package com.astro.storm.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.astro.storm.core.model.VedicChart
import com.astro.storm.ephemeris.KalachakraDashaCalculator
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.concurrent.atomic.AtomicReference
import javax.inject.Inject

/**
 * UI State for Kalachakra Dasha Screen
 */
sealed class KalachakraDashaUiState {
    data object Loading : KalachakraDashaUiState()
    data class Success(val result: KalachakraDashaCalculator.KalachakraDashaResult) : KalachakraDashaUiState()
    data class Error(val message: String) : KalachakraDashaUiState()
    data object Idle : KalachakraDashaUiState()
}

/**
 * ViewModel for Kalachakra Dasha calculations
 *
 * Manages the calculation of Kalachakra Dasha from birth chart data
 * with caching for performance optimization.
 */
@HiltViewModel
class KalachakraDashaViewModel @Inject constructor() : ViewModel() {

    private val _uiState = MutableStateFlow<KalachakraDashaUiState>(KalachakraDashaUiState.Idle)
    val uiState: StateFlow<KalachakraDashaUiState> = _uiState.asStateFlow()

    private var calculationJob: Job? = null

    private val cache = AtomicReference<CachedKalachakraResult?>(null)

    private data class CachedKalachakraResult(
        val chartKey: String,
        val result: KalachakraDashaCalculator.KalachakraDashaResult
    )

    /**
     * Load Kalachakra Dasha for the given chart
     */
    fun loadKalachakraDasha(chart: VedicChart?, language: com.astro.storm.core.common.Language = com.astro.storm.core.common.Language.ENGLISH) {
        if (chart == null) {
            _uiState.value = KalachakraDashaUiState.Idle
            return
        }

        val chartKey = generateChartKey(chart, language)

        // Check cache first
        cache.get()?.let { cached ->
            if (cached.chartKey == chartKey) {
                _uiState.value = KalachakraDashaUiState.Success(cached.result)
                return
            }
        }

        // Don't start a new calculation if one is already in progress
        val currentState = _uiState.value
        if (currentState is KalachakraDashaUiState.Loading) {
            return
        }

        // Cancel any existing calculation and start a new one
        calculationJob?.cancel()
        _uiState.value = KalachakraDashaUiState.Loading

        calculationJob = viewModelScope.launch {
            try {
                val result = withContext(Dispatchers.Default) {
                    KalachakraDashaCalculator.calculateKalachakraDasha(
                        chart = chart,
                        numberOfCycles = 2, // Calculate 2 cycles = 200 years
                        language = language
                    )
                }

                cache.set(CachedKalachakraResult(chartKey, result))
                _uiState.value = KalachakraDashaUiState.Success(result)

            } catch (e: CancellationException) {
                // Don't update state on cancellation
                throw e
            } catch (e: Exception) {
                val errorMessage = when {
                    e.message?.contains("Moon", ignoreCase = true) == true ->
                        "Unable to determine Moon's Nakshatra position. Please verify birth data."
                    e.message?.contains("birth", ignoreCase = true) == true ->
                        "Invalid birth data provided. Please check date, time, and location."
                    e.message?.contains("nakshatra", ignoreCase = true) == true ->
                        "Unable to calculate Kalachakra Dasha. Nakshatra calculation error."
                    else ->
                        e.message ?: "Failed to calculate Kalachakra Dasha. Please try again."
                }
                _uiState.value = KalachakraDashaUiState.Error(errorMessage)
            }
        }
    }

    /**
     * Clear the cached result
     */
    fun clearCache() {
        cache.set(null)
    }

    /**
     * Generate a unique key for the chart to use for caching
     */
    private fun generateChartKey(chart: VedicChart, language: com.astro.storm.core.common.Language): String {
        val birthData = chart.birthData
        return buildString {
            append(birthData.dateTime.toEpochSecond(java.time.ZoneOffset.UTC))
            append('|')
            append((birthData.latitude * 1_000_000).toLong())
            append('|')
            append((birthData.longitude * 1_000_000).toLong())
            append('|')
            append(chart.ayanamsaName)
            append("|kalachakra|")
            append(language.name)
        }
    }

    override fun onCleared() {
        super.onCleared()
        calculationJob?.cancel()
    }
}
