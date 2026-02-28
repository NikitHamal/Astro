package com.astro.vajra.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.astro.vajra.core.common.Language
import com.astro.vajra.core.model.VedicChart
import com.astro.vajra.ephemeris.shoola.ShoolaDashaCalculator
import com.astro.vajra.ephemeris.shoola.ShoolaDashaResult
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
 * UI State for Shoola Dasha Screen
 */
sealed class ShoolaDashaUiState {
    data object Loading : ShoolaDashaUiState()
    data class Success(val result: ShoolaDashaResult) : ShoolaDashaUiState()
    data class Error(val message: String) : ShoolaDashaUiState()
    data object Idle : ShoolaDashaUiState()
}

/**
 * ViewModel for Shoola Dasha calculations
 *
 * Manages the calculation of Shoola Dasha (Jaimini health/accident timing system)
 * from birth chart data with caching for performance optimization.
 *
 * Shoola Dasha is a Jaimini sign-based dasha system used specifically for
 * timing health issues, accidents, and critical life events.
 */
@HiltViewModel
class ShoolaDashaViewModel @Inject constructor() : ViewModel() {

    private val _uiState = MutableStateFlow<ShoolaDashaUiState>(ShoolaDashaUiState.Idle)
    val uiState: StateFlow<ShoolaDashaUiState> = _uiState.asStateFlow()

    private var calculationJob: Job? = null

    private val cache = AtomicReference<CachedShoolaResult?>(null)

    private data class CachedShoolaResult(
        val chartKey: String,
        val result: ShoolaDashaResult
    )

    /**
     * Load Shoola Dasha for the given chart
     */
    fun loadShoolaDasha(
        chart: VedicChart?,
        language: Language = Language.ENGLISH
    ) {
        if (chart == null) {
            _uiState.value = ShoolaDashaUiState.Idle
            return
        }

        val chartKey = generateChartKey(chart, language)

        // Check cache first
        cache.get()?.let { cached ->
            if (cached.chartKey == chartKey) {
                _uiState.value = ShoolaDashaUiState.Success(cached.result)
                return
            }
        }

        // Don't start a new calculation if one is already in progress
        val currentState = _uiState.value
        if (currentState is ShoolaDashaUiState.Loading) {
            return
        }

        // Cancel any existing calculation and start a new one
        calculationJob?.cancel()
        _uiState.value = ShoolaDashaUiState.Loading

        calculationJob = viewModelScope.launch {
            try {
                val result = withContext(Dispatchers.Default) {
                    ShoolaDashaCalculator.calculateShoolaDasha(chart)
                }

                if (result != null) {
                    cache.set(CachedShoolaResult(chartKey, result))
                    _uiState.value = ShoolaDashaUiState.Success(result)
                } else {
                    _uiState.value = ShoolaDashaUiState.Error(
                        "Unable to calculate Shoola Dasha. Please verify birth data."
                    )
                }

            } catch (e: CancellationException) {
                // Don't update state on cancellation
                throw e
            } catch (e: Exception) {
                val errorMessage = when {
                    e.message?.contains("Ascendant", ignoreCase = true) == true ->
                        "Unable to determine Ascendant position. Please verify birth data."
                    e.message?.contains("birth", ignoreCase = true) == true ->
                        "Invalid birth data provided. Please check date, time, and location."
                    e.message?.contains("planet", ignoreCase = true) == true ->
                        "Unable to calculate planetary positions. Please verify birth data."
                    else ->
                        e.message ?: "Failed to calculate Shoola Dasha. Please try again."
                }
                _uiState.value = ShoolaDashaUiState.Error(errorMessage)
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
    private fun generateChartKey(chart: VedicChart, language: Language): String {
        val birthData = chart.birthData
        return buildString {
            append(birthData.dateTime.toEpochSecond(java.time.ZoneOffset.UTC))
            append('|')
            append((birthData.latitude * 1_000_000).toLong())
            append('|')
            append((birthData.longitude * 1_000_000).toLong())
            append('|')
            append(chart.ayanamsaName)
            append("|shoola|")
            append(language.name)
        }
    }

    override fun onCleared() {
        super.onCleared()
        calculationJob?.cancel()
    }
}
