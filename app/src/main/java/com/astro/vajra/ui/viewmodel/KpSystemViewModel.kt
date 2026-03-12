package com.astro.vajra.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.astro.vajra.core.model.VedicChart
import com.astro.vajra.ephemeris.kp.KpAnalysisResult
import com.astro.vajra.ephemeris.kp.KpSystemCalculator
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.time.LocalDateTime
import javax.inject.Inject

@HiltViewModel
class KpSystemViewModel @Inject constructor(
    private val kpSystemCalculator: KpSystemCalculator
) : ViewModel() {

    private val _uiState = MutableStateFlow<KpSystemUiState>(KpSystemUiState.Idle)
    val uiState: StateFlow<KpSystemUiState> = _uiState.asStateFlow()

    private var cachedKey: String? = null
    private var cachedResult: KpAnalysisResult? = null

    fun loadKpSystem(
        chart: VedicChart?,
        analysisMoment: LocalDateTime
    ) {
        if (chart == null) {
            _uiState.value = KpSystemUiState.Idle
            cachedKey = null
            cachedResult = null
            return
        }

        val requestKey = buildString {
            append(chart.id)
            append('|')
            append(chart.birthData.dateTime)
            append('|')
            append(chart.birthData.latitude)
            append('|')
            append(chart.birthData.longitude)
            append('|')
            append(chart.birthData.timezone)
            append('|')
            append(analysisMoment)
        }

        if (cachedKey == requestKey && cachedResult != null) {
            _uiState.value = KpSystemUiState.Success(cachedResult!!)
            return
        }

        viewModelScope.launch {
            _uiState.value = KpSystemUiState.Loading
            try {
                val result = withContext(Dispatchers.Default) {
                    kpSystemCalculator.calculate(chart, analysisMoment)
                }
                cachedKey = requestKey
                cachedResult = result
                _uiState.value = KpSystemUiState.Success(result)
            } catch (exception: Exception) {
                _uiState.value = KpSystemUiState.Error(
                    exception.message ?: "Unable to calculate KP system"
                )
            }
        }
    }
}

sealed class KpSystemUiState {
    data object Idle : KpSystemUiState()
    data object Loading : KpSystemUiState()
    data class Success(val result: KpAnalysisResult) : KpSystemUiState()
    data class Error(val message: String) : KpSystemUiState()
}
