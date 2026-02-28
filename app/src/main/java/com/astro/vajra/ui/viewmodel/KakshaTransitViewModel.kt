package com.astro.vajra.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.astro.vajra.core.common.Language
import com.astro.vajra.core.model.Planet
import com.astro.vajra.core.model.VedicChart
import com.astro.vajra.ephemeris.KakshaTransitCalculator
import com.astro.vajra.ephemeris.KakshaTransitCalculator.KakshaQuality
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.time.DateTimeException
import java.time.LocalDate
import java.time.ZoneId
import java.time.ZoneOffset
import java.util.concurrent.atomic.AtomicReference
import javax.inject.Inject
import kotlin.math.roundToInt

/**
 * ViewModel for Kakshya Transit Analysis Screen
 *
 * Manages the UI state for displaying Kakshya (8-fold division) transit analysis,
 * including current positions, upcoming changes, favorable periods, and critical transits.
 */
@HiltViewModel
class KakshaTransitViewModel @Inject constructor() : ViewModel() {

    // UI State
    private val _uiState = MutableStateFlow<KakshaTransitUiState>(KakshaTransitUiState.Loading)
    val uiState: StateFlow<KakshaTransitUiState> = _uiState.asStateFlow()

    // Selected tab
    private val _selectedTab = MutableStateFlow(KakshaTab.CURRENT)
    val selectedTab: StateFlow<KakshaTab> = _selectedTab.asStateFlow()

    // Selected planet for detailed view
    private val _selectedPlanet = MutableStateFlow<Planet?>(null)
    val selectedPlanet: StateFlow<Planet?> = _selectedPlanet.asStateFlow()

    // Quality filter
    private val _qualityFilter = MutableStateFlow<KakshaQuality?>(null)
    val qualityFilter: StateFlow<KakshaQuality?> = _qualityFilter.asStateFlow()

    // Cache for analysis results
    private val cachedResult = AtomicReference<CachedKakshaResult?>(null)

    /**
     * Calculate Kakshya transit analysis for a chart
     */
    fun calculateKakshaTransits(
        chart: VedicChart,
        language: Language = Language.ENGLISH,
        forceRecalculate: Boolean = false
    ) {
        val chartZoneId = resolveZoneId(chart.birthData.timezone)
        val chartDate = LocalDate.now(chartZoneId)

        // Check cache first
        val cached = cachedResult.get()
        if (!forceRecalculate && cached != null && cached.chartId == chart.id && cached.analysisDate == chartDate) {
            _uiState.value = KakshaTransitUiState.Success(cached.result)
            return
        }

        _uiState.value = KakshaTransitUiState.Loading

        viewModelScope.launch(Dispatchers.Default) {
            try {
                val result = KakshaTransitCalculator.calculateKakshaTransits(
                    chart = chart,
                    analysisTime = null,
                    language = language
                )

                if (result != null) {
                    // Cache the result
                    cachedResult.set(CachedKakshaResult(chart.id, chartDate, result))
                    _uiState.value = KakshaTransitUiState.Success(result)
                } else {
                    _uiState.value = KakshaTransitUiState.Error("Failed to calculate Kakshya transits")
                }
            } catch (e: Exception) {
                _uiState.value = KakshaTransitUiState.Error(
                    e.message ?: "Unknown error calculating Kakshya transits"
                )
            }
        }
    }

    /**
     * Select a tab
     */
    fun selectTab(tab: KakshaTab) {
        _selectedTab.value = tab
        // Clear planet selection when changing tabs (except for PLANETS tab)
        if (tab != KakshaTab.PLANETS) {
            _selectedPlanet.value = null
        }
    }

    /**
     * Select a planet for detailed view
     */
    fun selectPlanet(planet: Planet?) {
        _selectedPlanet.value = planet
    }

    /**
     * Filter by quality
     */
    fun filterByQuality(quality: KakshaQuality?) {
        _qualityFilter.value = quality
    }

    /**
     * Get filtered current positions based on quality filter
     */
    fun getFilteredPositions(): List<KakshaTransitCalculator.KakshaPlanetPosition> {
        val state = _uiState.value
        if (state !is KakshaTransitUiState.Success) return emptyList()

        val filter = _qualityFilter.value
        return if (filter == null) {
            state.result.currentPositions
        } else {
            state.result.currentPositions.filter { it.quality == filter }
        }
    }

    /**
     * Get position for a specific planet
     */
    fun getPositionForPlanet(planet: Planet): KakshaTransitCalculator.KakshaPlanetPosition? {
        val state = _uiState.value
        if (state !is KakshaTransitUiState.Success) return null
        return state.result.currentPositions.find { it.planet == planet }
    }

    /**
     * Get upcoming changes for a specific planet
     */
    fun getChangesForPlanet(planet: Planet): List<KakshaTransitCalculator.KakshaChange> {
        val state = _uiState.value
        if (state !is KakshaTransitUiState.Success) return emptyList()
        return state.result.upcomingChanges.filter { it.planet == planet }
    }

    /**
     * Get favorable periods for a specific planet
     */
    fun getFavorablePeriodsForPlanet(planet: Planet): List<KakshaTransitCalculator.FavorableKakshaPeriod> {
        val state = _uiState.value
        if (state !is KakshaTransitUiState.Success) return emptyList()
        return state.result.favorablePeriods.filter { it.planet == planet }
    }

    /**
     * Get critical transits for a specific planet
     */
    fun getCriticalTransitsForPlanet(planet: Planet): List<KakshaTransitCalculator.CriticalKakshaTransit> {
        val state = _uiState.value
        if (state !is KakshaTransitUiState.Success) return emptyList()
        return state.result.criticalTransits.filter { it.planet == planet }
    }

    /**
     * Get summary statistics
     */
    fun getSummary(): KakshaSummary? {
        val state = _uiState.value
        if (state !is KakshaTransitUiState.Success) return null

        val result = state.result
        val positions = result.currentPositions

        return KakshaSummary(
            overallScore = result.overallKakshaScore,
            overallQuality = result.overallQuality,
            totalPlanets = positions.size,
            excellentCount = positions.count { it.quality == KakshaQuality.EXCELLENT },
            goodCount = positions.count { it.quality == KakshaQuality.GOOD },
            moderateCount = positions.count { it.quality == KakshaQuality.MODERATE },
            poorCount = positions.count { it.quality == KakshaQuality.POOR },
            planetsWithBindu = positions.count { it.hasBinbu },
            upcomingChangesCount = result.upcomingChanges.size,
            favorablePeriodsCount = result.favorablePeriods.size,
            criticalTransitsCount = result.criticalTransits.size,
            nextChangeHours = result.upcomingChanges.minOfOrNull { it.hoursFromNow }
        )
    }

    /**
     * Clear cache and reset state
     */
    fun clearCache() {
        cachedResult.set(null)
        _uiState.value = KakshaTransitUiState.Loading
        _selectedTab.value = KakshaTab.CURRENT
        _selectedPlanet.value = null
        _qualityFilter.value = null
    }

    private fun resolveZoneId(timezone: String): ZoneId {
        return try {
            ZoneId.of(timezone)
        } catch (_: DateTimeException) {
            val trimmed = timezone.trim()
            val numericHours = trimmed.toDoubleOrNull()
            if (numericHours != null) {
                val totalSeconds = (numericHours * 3600.0).roundToInt()
                ZoneOffset.ofTotalSeconds(totalSeconds.coerceIn(-18 * 3600, 18 * 3600))
            } else {
                ZoneId.systemDefault()
            }
        }
    }
}

/**
 * UI State for Kakshya Transit Screen
 */
sealed class KakshaTransitUiState {
    object Loading : KakshaTransitUiState()
    data class Success(val result: KakshaTransitCalculator.KakshaTransitResult) : KakshaTransitUiState()
    data class Error(val message: String) : KakshaTransitUiState()
}

/**
 * Tabs for Kakshya Transit Screen
 */
enum class KakshaTab {
    CURRENT,    // Current Kakshya positions
    PLANETS,    // Planet-by-planet view
    TIMELINE,   // Upcoming changes timeline
    FAVORABLE   // Favorable periods
}

/**
 * Summary statistics for Kakshya analysis
 */
data class KakshaSummary(
    val overallScore: Double,
    val overallQuality: KakshaQuality,
    val totalPlanets: Int,
    val excellentCount: Int,
    val goodCount: Int,
    val moderateCount: Int,
    val poorCount: Int,
    val planetsWithBindu: Int,
    val upcomingChangesCount: Int,
    val favorablePeriodsCount: Int,
    val criticalTransitsCount: Int,
    val nextChangeHours: Long?
)

/**
 * Cached result for avoiding recalculation
 */
private data class CachedKakshaResult(
    val chartId: Long,
    val analysisDate: LocalDate,
    val result: KakshaTransitCalculator.KakshaTransitResult
)
