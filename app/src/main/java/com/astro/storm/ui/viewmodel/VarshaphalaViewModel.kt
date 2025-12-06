package com.astro.storm.ui.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.astro.storm.data.model.VedicChart
import com.astro.storm.ephemeris.VarshaphalaCalculator
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * UI states for Varshaphala operations
 */
sealed class VarshaphalaUiState {
    object Initial : VarshaphalaUiState()
    object Loading : VarshaphalaUiState()
    data class Success(val result: VarshaphalaCalculator.VarshaphalaResult) : VarshaphalaUiState()
    data class Error(val message: String) : VarshaphalaUiState()
}

/**
 * ViewModel for Varshaphala (Annual Chart) operations.
 */
class VarshaphalaViewModel(application: Application) : AndroidViewModel(application) {

    private val varshaphalaCalculator = VarshaphalaCalculator(application)

    private val _uiState = MutableStateFlow<VarshaphalaUiState>(VarshaphalaUiState.Initial)
    val uiState: StateFlow<VarshaphalaUiState> = _uiState.asStateFlow()

    /**
     * Calculates the Varshaphala (annual chart) for a given natal chart and year.
     *
     * @param natalChart The base Vedic chart of the native.
     * @param year The target year for the annual chart calculation.
     */
    fun calculateVarshaphala(natalChart: VedicChart, year: Int) {
        viewModelScope.launch {
            _uiState.value = VarshaphalaUiState.Loading
            try {
                val result = withContext(Dispatchers.Default) {
                    varshaphalaCalculator.calculateVarshaphala(natalChart, year)
                }
                Log.d("VarshaphalaViewModel", "Varshaphala calculation successful: $result")
                _uiState.value = VarshaphalaUiState.Success(result)
            } catch (e: Exception) {
                // It's good practice to log the exception
                 Log.e("VarshaphalaViewModel", "Varshaphala calculation failed", e)
                _uiState.value = VarshaphalaUiState.Error(e.message ?: "An unknown error occurred during calculation.")
            }
        }
    }

    /**
     * Resets the UI state to its initial value.
     */
    fun resetState() {
        _uiState.value = VarshaphalaUiState.Initial
    }

    override fun onCleared() {
        super.onCleared()
        // Ensure the Swiss Ephemeris instance is closed to free up resources
        varshaphalaCalculator.close()
    }
}
