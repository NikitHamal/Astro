package com.astro.vajra.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.astro.vajra.core.model.VedicChart
import com.astro.vajra.ephemeris.NadiAmshaCalculator
import com.astro.vajra.ephemeris.NadiAmshaCalculator.NadiAmshaResult
import com.astro.vajra.ephemeris.NadiAmshaCalculator.RectificationCandidate
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

/**
 * ViewModel for Nadi Amsha (D-150) Analysis Screen
 */
@HiltViewModel
class NadiAmshaViewModel @Inject constructor() : ViewModel() {

    private val _uiState = MutableStateFlow<NadiAmshaUiState>(NadiAmshaUiState.Loading)
    val uiState: StateFlow<NadiAmshaUiState> = _uiState.asStateFlow()

    private val _selectedTab = MutableStateFlow(0)
    val selectedTab: StateFlow<Int> = _selectedTab.asStateFlow()

    fun setSelectedTab(index: Int) {
        _selectedTab.value = index
    }

    fun calculateNadiAmsha(chart: VedicChart) {
        viewModelScope.launch {
            _uiState.value = NadiAmshaUiState.Loading
            try {
                val result = withContext(Dispatchers.Default) {
                    NadiAmshaCalculator.calculateNadiAmsha(chart)
                }
                _uiState.value = NadiAmshaUiState.Success(result)
            } catch (e: Exception) {
                _uiState.value = NadiAmshaUiState.Error(e.message ?: "Unknown error")
            }
        }
    }

    /**
     * Simulate birth time rectification
     * In a real app, this might trigger a recalculation of the entire chart.
     * For this feature, we might display the candidate result directly.
     */
    fun selectRectificationCandidate(candidate: RectificationCandidate) {
        // Logic to apply rectification would go here
        // For now, we can perhaps just log it or show a 'Simulated' state
    }
}

sealed class NadiAmshaUiState {
    data object Loading : NadiAmshaUiState()
    data class Success(val result: NadiAmshaResult) : NadiAmshaUiState()
    data class Error(val message: String) : NadiAmshaUiState()
}
