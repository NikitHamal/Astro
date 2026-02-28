package com.astro.vajra.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.astro.vajra.core.common.Language
import com.astro.vajra.core.model.Planet
import com.astro.vajra.core.model.VedicChart
import com.astro.vajra.core.model.ZodiacSign
import com.astro.vajra.ephemeris.AshtavargaTransitCalculator
import com.astro.vajra.ephemeris.AshtavargaTransitCalculator.AshtavargaTransitResult
import com.astro.vajra.ephemeris.AshtavargaTransitCalculator.CurrentTransitInfo
import com.astro.vajra.ephemeris.AshtavargaTransitCalculator.FavorableSign
import com.astro.vajra.ephemeris.AshtavargaTransitCalculator.LifeArea
import com.astro.vajra.ephemeris.AshtavargaTransitCalculator.TransitQuality
import com.astro.vajra.ephemeris.AshtavargaTransitCalculator.TransitStrength
import com.astro.vajra.ephemeris.AshtavargaTransitCalculator.UnfavorableSign
import com.astro.vajra.ephemeris.AshtavargaTransitCalculator.UpcomingTransit
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
 * ViewModel for Ashtavarga Transit Predictions Screen
 *
 * This ViewModel manages the state for displaying Ashtavarga-based transit predictions.
 * It provides comprehensive transit analysis based on bindu scores from the natal chart.
 *
 * Features:
 * - Current transit analysis with BAV/SAV scores
 * - 12-month upcoming transit forecasting
 * - Favorable and unfavorable sign rankings per planet
 * - Event probability calculations
 * - Life area impact associations
 * - Bilingual interpretation support (EN/NE)
 *
 * Uses atomic caching to prevent redundant calculations when the chart hasn't changed.
 */
@HiltViewModel
class AshtavargaTransitViewModel @Inject constructor() : ViewModel() {

    /**
     * UI State for Ashtavarga Transit Screen
     */
    sealed class AshtavargaTransitUiState {
        /** Initial state before any calculation */
        data object Initial : AshtavargaTransitUiState()

        /** Loading state during calculation */
        data object Loading : AshtavargaTransitUiState()

        /** Successful calculation result */
        data class Success(
            val result: AshtavargaTransitResult,
            val selectedTab: TransitTab = TransitTab.OVERVIEW,
            val selectedPlanet: Planet? = null,
            val filterQuality: TransitQuality? = null,
            val showOnlySignificant: Boolean = false
        ) : AshtavargaTransitUiState()

        /** Error state with message */
        data class Error(
            val message: String,
            val messageNe: String
        ) : AshtavargaTransitUiState()

        /** No chart selected state */
        data object NoChart : AshtavargaTransitUiState()
    }

    /**
     * Available tabs in the transit screen
     */
    enum class TransitTab {
        OVERVIEW,
        CURRENT,
        UPCOMING,
        PLANETS,
        ANALYSIS
    }

    /**
     * Summary statistics for quick overview
     */
    data class TransitSummary(
        val totalCurrentTransits: Int,
        val favorableCount: Int,
        val challengingCount: Int,
        val averageCount: Int,
        val upcomingSignificantCount: Int,
        val overallScore: Double,
        val overallQuality: TransitQuality,
        val dominantLifeAreas: List<LifeArea>,
        val strongestTransit: CurrentTransitInfo?,
        val weakestTransit: CurrentTransitInfo?,
        val nextSignificantTransit: UpcomingTransit?
    )

    /**
     * Planet-specific transit details for the Planets tab
     */
    data class PlanetTransitDetails(
        val planet: Planet,
        val currentTransit: CurrentTransitInfo?,
        val favorableSigns: List<FavorableSign>,
        val unfavorableSigns: List<UnfavorableSign>,
        val upcomingTransits: List<UpcomingTransit>,
        val averageBavScore: Double,
        val bestSign: ZodiacSign?,
        val worstSign: ZodiacSign?
    )

    // State flows
    private val _uiState = MutableStateFlow<AshtavargaTransitUiState>(AshtavargaTransitUiState.Initial)
    val uiState: StateFlow<AshtavargaTransitUiState> = _uiState.asStateFlow()

    private val _transitSummary = MutableStateFlow<TransitSummary?>(null)
    val transitSummary: StateFlow<TransitSummary?> = _transitSummary.asStateFlow()

    private val _planetDetails = MutableStateFlow<Map<Planet, PlanetTransitDetails>>(emptyMap())
    val planetDetails: StateFlow<Map<Planet, PlanetTransitDetails>> = _planetDetails.asStateFlow()

    // Cache management
    private data class CacheKey(
        val chartId: Long,
        val analysisDate: LocalDate
    )

    private val cachedResult = AtomicReference<Pair<CacheKey, AshtavargaTransitResult>?>(null)

    /**
     * Calculate transit predictions for the given chart
     *
     * @param chart The natal chart to analyze
     * @param language Language for interpretations
     * @param forceRefresh Force recalculation even if cached
     */
    fun calculateTransits(
        chart: VedicChart?,
        language: Language = Language.ENGLISH,
        forceRefresh: Boolean = false
    ) {
        if (chart == null) {
            _uiState.value = AshtavargaTransitUiState.NoChart
            _transitSummary.value = null
            _planetDetails.value = emptyMap()
            return
        }

        val chartZoneId = resolveZoneId(chart.birthData.timezone)
        val currentDate = LocalDate.now(chartZoneId)
        val cacheKey = CacheKey(chart.hashCode().toLong(), currentDate)

        // Check cache first
        if (!forceRefresh) {
            cachedResult.get()?.let { (key, result) ->
                if (key == cacheKey) {
                    updateStateFromResult(result)
                    return
                }
            }
        }

        // Start calculation
        _uiState.value = AshtavargaTransitUiState.Loading

        viewModelScope.launch(Dispatchers.Default) {
            try {
                val result = AshtavargaTransitCalculator.calculateAshtavargaTransits(
                    chart = chart,
                    transitPositions = chart.planetPositions, // Use natal positions as current transits for demo
                    analysisDate = null,
                    language = language
                )

                if (result != null) {
                    // Cache the result
                    cachedResult.set(cacheKey to result)

                    // Update state on main thread
                    updateStateFromResult(result)
                } else {
                    _uiState.value = AshtavargaTransitUiState.Error(
                        message = "Failed to calculate transit predictions. Please try again.",
                        messageNe = "गोचर भविष्यवाणी गणना गर्न असफल। कृपया पुन: प्रयास गर्नुहोस्।"
                    )
                }
            } catch (e: Exception) {
                _uiState.value = AshtavargaTransitUiState.Error(
                    message = "Error: ${e.message ?: "Unknown error occurred"}",
                    messageNe = "त्रुटि: ${e.message ?: "अज्ञात त्रुटि भयो"}"
                )
            }
        }
    }

    /**
     * Update UI state from calculation result
     */
    private fun updateStateFromResult(result: AshtavargaTransitResult) {
        // Calculate summary
        val summary = calculateSummary(result)
        _transitSummary.value = summary

        // Calculate planet details
        val details = calculatePlanetDetails(result)
        _planetDetails.value = details

        // Update main UI state
        _uiState.value = AshtavargaTransitUiState.Success(result = result)
    }

    /**
     * Calculate transit summary statistics
     */
    private fun calculateSummary(result: AshtavargaTransitResult): TransitSummary {
        val currentTransits = result.currentTransits

        val favorableQualities = setOf(
            TransitQuality.EXCELLENT,
            TransitQuality.VERY_GOOD,
            TransitQuality.GOOD
        )
        val challengingQualities = setOf(
            TransitQuality.CHALLENGING,
            TransitQuality.DIFFICULT
        )

        val favorableCount = currentTransits.count { it.quality in favorableQualities }
        val challengingCount = currentTransits.count { it.quality in challengingQualities }
        val averageCount = currentTransits.size - favorableCount - challengingCount

        // Find dominant life areas from upcoming significant transits
        val lifeAreaCounts = mutableMapOf<LifeArea, Int>()
        result.upcomingTransits
            .filter { it.isSignificant }
            .flatMap { it.lifeAreas }
            .forEach { area ->
                lifeAreaCounts[area] = (lifeAreaCounts[area] ?: 0) + 1
            }
        val dominantAreas = lifeAreaCounts.entries
            .sortedByDescending { it.value }
            .take(3)
            .map { it.key }

        // Find strongest and weakest transits
        val sortedByQuality = currentTransits.sortedByDescending { it.quality.score }
        val strongestTransit = sortedByQuality.firstOrNull()
        val weakestTransit = sortedByQuality.lastOrNull()

        // Find next significant upcoming transit
        val nextSignificant = result.upcomingTransits
            .filter { it.isSignificant }
            .minByOrNull { it.transitDate }

        return TransitSummary(
            totalCurrentTransits = currentTransits.size,
            favorableCount = favorableCount,
            challengingCount = challengingCount,
            averageCount = averageCount,
            upcomingSignificantCount = result.upcomingTransits.count { it.isSignificant },
            overallScore = result.overallTransitScore,
            overallQuality = result.overallTransitQuality,
            dominantLifeAreas = dominantAreas,
            strongestTransit = strongestTransit,
            weakestTransit = weakestTransit,
            nextSignificantTransit = nextSignificant
        )
    }

    /**
     * Calculate planet-specific transit details
     */
    private fun calculatePlanetDetails(result: AshtavargaTransitResult): Map<Planet, PlanetTransitDetails> {
        val transitPlanets = listOf(
            Planet.SUN, Planet.MOON, Planet.MARS, Planet.MERCURY,
            Planet.JUPITER, Planet.VENUS, Planet.SATURN
        )

        return transitPlanets.associateWith { planet ->
            val currentTransit = result.currentTransits.find { it.planet == planet }
            val favorableSigns = result.favorableSigns[planet] ?: emptyList()
            val unfavorableSigns = result.unfavorableSigns[planet] ?: emptyList()
            val upcomingTransits = result.upcomingTransits.filter { it.planet == planet }

            // Calculate average BAV score from all signs
            val allBavScores = (favorableSigns.map { it.bavScore } +
                    unfavorableSigns.map { it.bavScore }).filter { it > 0 }
            val avgBav = if (allBavScores.isNotEmpty()) {
                allBavScores.average()
            } else {
                currentTransit?.bavScore?.toDouble() ?: 0.0
            }

            PlanetTransitDetails(
                planet = planet,
                currentTransit = currentTransit,
                favorableSigns = favorableSigns,
                unfavorableSigns = unfavorableSigns,
                upcomingTransits = upcomingTransits,
                averageBavScore = avgBav,
                bestSign = favorableSigns.firstOrNull()?.sign,
                worstSign = unfavorableSigns.firstOrNull()?.sign
            )
        }
    }

    /**
     * Select a tab in the transit screen
     */
    fun selectTab(tab: TransitTab) {
        val currentState = _uiState.value
        if (currentState is AshtavargaTransitUiState.Success) {
            _uiState.value = currentState.copy(selectedTab = tab)
        }
    }

    /**
     * Select a planet for detailed view
     */
    fun selectPlanet(planet: Planet?) {
        val currentState = _uiState.value
        if (currentState is AshtavargaTransitUiState.Success) {
            _uiState.value = currentState.copy(selectedPlanet = planet)
        }
    }

    /**
     * Filter transits by quality
     */
    fun filterByQuality(quality: TransitQuality?) {
        val currentState = _uiState.value
        if (currentState is AshtavargaTransitUiState.Success) {
            _uiState.value = currentState.copy(filterQuality = quality)
        }
    }

    /**
     * Toggle showing only significant transits
     */
    fun toggleSignificantOnly() {
        val currentState = _uiState.value
        if (currentState is AshtavargaTransitUiState.Success) {
            _uiState.value = currentState.copy(
                showOnlySignificant = !currentState.showOnlySignificant
            )
        }
    }

    /**
     * Get filtered upcoming transits based on current filters
     */
    fun getFilteredUpcomingTransits(): List<UpcomingTransit> {
        val currentState = _uiState.value
        if (currentState !is AshtavargaTransitUiState.Success) return emptyList()

        var filtered = currentState.result.upcomingTransits

        // Filter by quality if set
        currentState.filterQuality?.let { quality ->
            filtered = filtered.filter { it.quality == quality }
        }

        // Filter by significance if enabled
        if (currentState.showOnlySignificant) {
            filtered = filtered.filter { it.isSignificant }
        }

        // Filter by selected planet if set
        currentState.selectedPlanet?.let { planet ->
            filtered = filtered.filter { it.planet == planet }
        }

        return filtered
    }

    /**
     * Get current transits sorted by a specific criteria
     */
    fun getCurrentTransitsSorted(sortBy: TransitSortCriteria): List<CurrentTransitInfo> {
        val currentState = _uiState.value
        if (currentState !is AshtavargaTransitUiState.Success) return emptyList()

        return when (sortBy) {
            TransitSortCriteria.BY_QUALITY_DESC -> {
                currentState.result.currentTransits.sortedByDescending { it.quality.score }
            }
            TransitSortCriteria.BY_QUALITY_ASC -> {
                currentState.result.currentTransits.sortedBy { it.quality.score }
            }
            TransitSortCriteria.BY_BAV_DESC -> {
                currentState.result.currentTransits.sortedByDescending { it.bavScore }
            }
            TransitSortCriteria.BY_SAV_DESC -> {
                currentState.result.currentTransits.sortedByDescending { it.savScore }
            }
            TransitSortCriteria.BY_PLANET -> {
                currentState.result.currentTransits.sortedBy { it.planet.ordinal }
            }
            TransitSortCriteria.BY_EXIT_DATE -> {
                currentState.result.currentTransits.sortedBy { it.exitDate }
            }
        }
    }

    /**
     * Sorting criteria for current transits
     */
    enum class TransitSortCriteria {
        BY_QUALITY_DESC,
        BY_QUALITY_ASC,
        BY_BAV_DESC,
        BY_SAV_DESC,
        BY_PLANET,
        BY_EXIT_DATE
    }

    /**
     * Get upcoming transits for a specific month
     */
    fun getUpcomingTransitsForMonth(monthOffset: Int): List<UpcomingTransit> {
        val currentState = _uiState.value
        if (currentState !is AshtavargaTransitUiState.Success) return emptyList()

        val targetMonth = currentState.result.analysisDate.toLocalDate().plusMonths(monthOffset.toLong())
        val monthStart = targetMonth.withDayOfMonth(1)
        val monthEnd = targetMonth.withDayOfMonth(targetMonth.lengthOfMonth())

        return currentState.result.upcomingTransits.filter { transit ->
            !transit.transitDate.isBefore(monthStart) && !transit.transitDate.isAfter(monthEnd)
        }
    }

    /**
     * Get life areas most affected by current transits
     */
    fun getMostAffectedLifeAreas(): List<Pair<LifeArea, Int>> {
        val currentState = _uiState.value
        if (currentState !is AshtavargaTransitUiState.Success) return emptyList()

        val areaImpact = mutableMapOf<LifeArea, Int>()

        // Count impact from current transits
        currentState.result.currentTransits.forEach { transit ->
            // Higher quality transits have more impact
            val impactMultiplier = when (transit.quality) {
                TransitQuality.EXCELLENT -> 3
                TransitQuality.VERY_GOOD -> 2
                TransitQuality.GOOD -> 1
                TransitQuality.AVERAGE -> 0
                TransitQuality.BELOW_AVERAGE -> -1
                TransitQuality.CHALLENGING -> -2
                TransitQuality.DIFFICULT -> -3
            }

            // Associate life areas based on planet
            val areas = getLifeAreasForPlanet(transit.planet)
            areas.forEach { area ->
                areaImpact[area] = (areaImpact[area] ?: 0) + impactMultiplier
            }
        }

        return areaImpact.entries
            .sortedByDescending { kotlin.math.abs(it.value) }
            .map { it.key to it.value }
    }

    /**
     * Get life areas associated with a planet
     */
    private fun getLifeAreasForPlanet(planet: Planet): List<LifeArea> {
        return when (planet) {
            Planet.SUN -> listOf(LifeArea.CAREER, LifeArea.HEALTH)
            Planet.MOON -> listOf(LifeArea.FAMILY, LifeArea.HEALTH)
            Planet.MARS -> listOf(LifeArea.PROPERTY, LifeArea.CAREER)
            Planet.MERCURY -> listOf(LifeArea.COMMUNICATION, LifeArea.EDUCATION)
            Planet.JUPITER -> listOf(LifeArea.SPIRITUALITY, LifeArea.EDUCATION, LifeArea.FINANCE)
            Planet.VENUS -> listOf(LifeArea.RELATIONSHIPS, LifeArea.FINANCE)
            Planet.SATURN -> listOf(LifeArea.CAREER, LifeArea.HEALTH)
            else -> emptyList()
        }
    }

    /**
     * Clear cache and reset state
     */
    fun reset() {
        cachedResult.set(null)
        _uiState.value = AshtavargaTransitUiState.Initial
        _transitSummary.value = null
        _planetDetails.value = emptyMap()
    }

    /**
     * Refresh transits with current date
     */
    fun refresh(chart: VedicChart?, language: Language = Language.ENGLISH) {
        calculateTransits(chart, language, forceRefresh = true)
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
