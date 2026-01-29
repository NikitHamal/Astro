package com.astro.storm.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.astro.storm.core.model.VedicChart
import com.astro.storm.ephemeris.kp.KpSystemEngine
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class KpSystemViewModel @Inject constructor() : ViewModel() {

    private val _uiState = MutableStateFlow<KpSystemUiState>(KpSystemUiState.Loading)
    val uiState: StateFlow<KpSystemUiState> = _uiState.asStateFlow()

    fun calculate(chart: VedicChart) {
        viewModelScope.launch(Dispatchers.Default) {
            _uiState.value = KpSystemUiState.Loading
            try {
                val cusps = KpSystemEngine.calculateCuspSubLords(chart)
                val significators = KpSystemEngine.calculateSignificators(chart)
                val selectedHouse = cusps.firstOrNull()?.house ?: 1
                val selectedEvent = KpEventType.MARRIAGE
                val verification = cusps.firstOrNull { it.house == selectedHouse }?.let { cusp ->
                    KpSystemEngine.verifyEvent(
                        cusp = cusp,
                        significators = significators,
                        favorableHouses = selectedEvent.favorableHouses,
                        unfavorableHouses = selectedEvent.unfavorableHouses
                    )
                }
                _uiState.value = KpSystemUiState.Success(
                    cusps = cusps,
                    significators = significators,
                    selectedHouse = selectedHouse,
                    selectedEvent = selectedEvent,
                    verification = verification
                )
            } catch (e: Exception) {
                _uiState.value = KpSystemUiState.Error(e.message ?: "KP analysis failed")
            }
        }
    }

    fun selectHouse(house: Int) {
        val current = _uiState.value
        if (current !is KpSystemUiState.Success) return
        val cusp = current.cusps.firstOrNull { it.house == house }
        val verification = cusp?.let {
            KpSystemEngine.verifyEvent(
                cusp = it,
                significators = current.significators,
                favorableHouses = current.selectedEvent.favorableHouses,
                unfavorableHouses = current.selectedEvent.unfavorableHouses
            )
        }
        _uiState.value = current.copy(selectedHouse = house, verification = verification)
    }

    fun selectEvent(eventType: KpEventType) {
        val current = _uiState.value
        if (current !is KpSystemUiState.Success) return
        val cusp = current.cusps.firstOrNull { it.house == current.selectedHouse }
        val verification = cusp?.let {
            KpSystemEngine.verifyEvent(
                cusp = it,
                significators = current.significators,
                favorableHouses = eventType.favorableHouses,
                unfavorableHouses = eventType.unfavorableHouses
            )
        }
        _uiState.value = current.copy(selectedEvent = eventType, verification = verification)
    }
}

enum class KpEventType(
    val label: String,
    val favorableHouses: Set<Int>,
    val unfavorableHouses: Set<Int>
) {
    MARRIAGE("Marriage", setOf(2, 7, 11), setOf(6, 8, 12)),
    CAREER("Career", setOf(2, 6, 10, 11), setOf(5, 8, 12)),
    PROPERTY("Property", setOf(2, 4, 11), setOf(6, 8, 12)),
    EDUCATION("Education", setOf(2, 4, 5, 9, 11), setOf(6, 8, 12)),
    HEALTH("Health", setOf(1, 6, 11), setOf(8, 12))
}

sealed class KpSystemUiState {
    data object Loading : KpSystemUiState()
    data class Success(
        val cusps: List<KpSystemEngine.KpCuspSubLord>,
        val significators: Map<com.astro.storm.core.model.Planet, KpSystemEngine.KpSignificator>,
        val selectedHouse: Int,
        val selectedEvent: KpEventType,
        val verification: KpSystemEngine.KpEventVerification?
    ) : KpSystemUiState()
    data class Error(val message: String) : KpSystemUiState()
}
