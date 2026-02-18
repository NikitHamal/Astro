package com.astro.storm.ui.viewmodel

import android.app.Application
import android.content.Context
import androidx.compose.ui.unit.Density
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.astro.storm.core.model.BirthData
import com.astro.storm.core.model.HouseSystem
import com.astro.storm.core.model.VedicChart
import com.astro.storm.data.api.GeocodingService
import com.astro.storm.data.repository.ChartRepository
import com.astro.storm.data.repository.SavedChart
import com.astro.storm.di.IoDispatcher
import com.astro.storm.ephemeris.HoroscopeCalculator
import com.astro.storm.ephemeris.SwissEphemerisEngine
import com.astro.storm.ephemeris.TransitAnalyzer
import com.astro.storm.ui.chart.ChartColorConfig
import com.astro.storm.ui.chart.ChartRenderer
import com.astro.storm.util.TimezoneSanitizer
import com.astro.storm.util.ChartExporter
import com.astro.storm.util.ExportUtils
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import android.util.Log
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.time.DateTimeException
import java.time.ZoneId
import java.time.zone.ZoneRulesException
import java.util.Objects
import javax.inject.Inject

/**
 * ViewModel for chart operations
 */
@HiltViewModel
class ChartViewModel @Inject constructor(
    private val application: Application,
    private val repository: ChartRepository,
    private val ephemerisEngine: SwissEphemerisEngine,
    private val geocodingService: GeocodingService,
    private val chartExporter: ChartExporter,
    private val transitAnalyzer: TransitAnalyzer,
    private val horoscopeCalculator: HoroscopeCalculator,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher
) : AndroidViewModel(application) {
    companion object {
        private const val TAG = "ChartViewModel"
    }

    // Default chart renderer for light theme - for theme-aware rendering, use getChartRenderer(isDark)
    val chartRenderer = ChartRenderer(application, ChartColorConfig.Light)
    private val prefs = application.getSharedPreferences("chart_prefs", Context.MODE_PRIVATE)

    // Theme-aware chart renderer cache
    private var darkChartRenderer: ChartRenderer? = null
    private var lightChartRenderer: ChartRenderer? = null

    /**
     * Get a chart renderer configured for the current theme.
     * @param isDarkTheme Whether the app is in dark theme mode
     * @return ChartRenderer with appropriate color configuration
     */
    fun getChartRenderer(isDarkTheme: Boolean): ChartRenderer {
        return if (isDarkTheme) {
            darkChartRenderer ?: ChartRenderer(application, ChartColorConfig.Dark).also { darkChartRenderer = it }
        } else {
            lightChartRenderer ?: ChartRenderer(application, ChartColorConfig.Light).also { lightChartRenderer = it }
        }
    }

    private val _uiState = MutableStateFlow<ChartUiState>(ChartUiState.Initial)
    val uiState: StateFlow<ChartUiState> = _uiState.asStateFlow()

    private val _savedCharts = MutableStateFlow<List<SavedChart>>(emptyList())
    val savedCharts: StateFlow<List<SavedChart>> = _savedCharts.asStateFlow()

    private val _selectedChartId = MutableStateFlow<Long?>(null)
    val selectedChartId: StateFlow<Long?> = _selectedChartId.asStateFlow()

    // Guard against duplicate saves of the same chart during a single calculation cycle
    private var lastSavedChartHash: Int? = null

    init {
        loadSavedCharts()
    }

    private fun loadSavedCharts() {
        viewModelScope.launch(ioDispatcher) {
            repository.getAllCharts().collect { charts ->
                _savedCharts.value = charts
                // Use compareAndSet pattern to avoid race condition
                val currentSelected = _selectedChartId.value
                if (currentSelected == null) {
                    val lastSelectedId = prefs.getLong("last_selected_chart_id", -1)
                    val targetChartId = when {
                        lastSelectedId != -1L && charts.any { it.id == lastSelectedId } -> lastSelectedId
                        charts.isNotEmpty() -> charts.first().id
                        else -> null
                    }
                    // Only load if still not selected (double-check pattern)
                    if (targetChartId != null && _selectedChartId.value == null) {
                        loadChart(targetChartId)
                    }
                }
            }
        }
    }

    /**
     * Search location using GeocodingService
     */
    suspend fun searchLocation(query: String): Result<List<GeocodingService.GeocodingResult>> {
        return geocodingService.searchLocation(query)
    }

    /**
     * Calculate a new Vedic chart
     */
    fun calculateChart(
        birthData: BirthData,
        houseSystem: HouseSystem? = null
    ) {
        viewModelScope.launch(ioDispatcher) {
            // Reset the save guard for new calculations
            lastSavedChartHash = null
            _uiState.value = ChartUiState.Calculating

            try {
                val chart = withContext(Dispatchers.Default) {
                    calculateChartWithTimezoneFallback(birthData, houseSystem)
                }
                _uiState.value = ChartUiState.Success(chart)
            } catch (e: Exception) {
                Log.e(TAG, "Chart calculation failed", e)
                _uiState.value = ChartUiState.Error(mapCalculationError(e))
            }
        }
    }

    /**
     * Calculate chart for updating an existing profile
     * Re-calculates the chart with new birth data and updates the existing record
     */
    fun calculateChartForUpdate(
        birthData: BirthData,
        existingChartId: Long,
        houseSystem: HouseSystem? = null
    ) {
        viewModelScope.launch(ioDispatcher) {
            lastSavedChartHash = null
            _uiState.value = ChartUiState.Calculating

            try {
                val chart = withContext(Dispatchers.Default) {
                    calculateChartWithTimezoneFallback(birthData, houseSystem)
                }

                // Preserve the ID of the existing chart so repository.updateChart knows which one to update
                val updatedChart = chart.copy(id = existingChartId)

                // Update the existing chart instead of creating a new one
                repository.updateChart(existingChartId, updatedChart)

                // Update selected ID and cache
                _selectedChartId.value = existingChartId
                prefs.edit().putLong("last_selected_chart_id", existingChartId).apply()

                // Update hash to prevent duplicate saves of the same data
                lastSavedChartHash = generateChartHash(updatedChart)

                // Set success state directly with the updated chart
                // This ensures UI and other screens reflect changes immediately
                _uiState.value = ChartUiState.Success(updatedChart)
            } catch (e: Exception) {
                Log.e(TAG, "Chart update calculation failed", e)
                _uiState.value = ChartUiState.Error(mapCalculationError(e))
            }
        }
    }

    /**
     * Runs chart calculation with controlled timezone fallbacks to guard against
     * parser edge cases seen with legacy or device-provided timezone identifiers.
     */
    private fun calculateChartWithTimezoneFallback(
        birthData: BirthData,
        houseSystem: HouseSystem?
    ): VedicChart {
        return try {
            calculateChartWithTimezoneAttempts(birthData, houseSystem)
        } catch (e: Exception) {
            if (houseSystem == HouseSystem.WHOLE_SIGN || !isIndexBoundaryFailure(e)) {
                throw e
            }

            Log.w(
                TAG,
                "Retrying chart calculation with WHOLE_SIGN fallback after boundary failure",
                e
            )
            calculateChartWithTimezoneAttempts(birthData, HouseSystem.WHOLE_SIGN)
        }
    }

    private fun calculateChartWithTimezoneAttempts(
        birthData: BirthData,
        houseSystem: HouseSystem?
    ): VedicChart {
        val timezoneAttempts = buildTimezoneAttempts(birthData.timezone)
        var lastError: Exception? = null

        timezoneAttempts.forEachIndexed { index, timezoneId ->
            val attemptBirthData = birthData.copy(timezone = timezoneId)
            val attemptLabel = when (index) {
                0 -> "selected"
                1 -> "system"
                else -> "utc"
            }

            try {
                if (index > 0) {
                    Log.w(
                        TAG,
                        "Retrying chart calculation with $attemptLabel timezone fallback: $timezoneId"
                    )
                }
                return ephemerisEngine.calculateVedicChart(attemptBirthData, houseSystem)
            } catch (e: Exception) {
                lastError = e
                val canRetry = index < timezoneAttempts.lastIndex && shouldRetryWithTimezoneFallback(e)
                if (canRetry) {
                    Log.w(
                        TAG,
                        "Chart calculation failed with timezone attempt '$timezoneId'; trying next fallback",
                        e
                    )
                } else {
                    throw e
                }
            }
        }

        throw lastError ?: IllegalStateException("Chart calculation failed")
    }

    private fun mapCalculationError(error: Exception): String {
        val msg = error.message
        if (isIndexBoundaryFailure(error)) {
            return "Calculation failed due to an internal calculation boundary issue. Please try again."
        }
        val isTimezoneDateTimeParsingFailure = shouldRetryWithTimezoneFallback(error) ||
                (msg?.contains("index=", ignoreCase = true) == true &&
                        msg.contains("length=", ignoreCase = true))
        return if (isTimezoneDateTimeParsingFailure) {
            "Calculation failed due to invalid date/time-timezone parsing. Please reselect timezone and try again."
        } else {
            msg ?: "Calculation failed"
        }
    }

    private fun buildTimezoneAttempts(rawTimezone: String?): List<String> {
        val attempts = linkedSetOf<String>()
        val selectedNormalized = TimezoneSanitizer.resolveZoneIdOrNull(rawTimezone)?.id
        if (!selectedNormalized.isNullOrBlank()) {
            attempts += selectedNormalized
        }
        attempts += ZoneId.systemDefault().id
        attempts += "UTC"
        return attempts.toList()
    }

    private fun shouldRetryWithTimezoneFallback(error: Exception): Boolean {
        val causes = generateSequence<Throwable>(error) { it.cause }.toList()
        if (causes.any {
                it is StringIndexOutOfBoundsException ||
                        it is ArrayIndexOutOfBoundsException ||
                        it is DateTimeException ||
                        it is ZoneRulesException
            }) {
            return true
        }

        return causes
            .mapNotNull { it.message?.lowercase() }
            .any { message ->
                "timezone" in message ||
                        "time zone" in message ||
                        "zone id" in message ||
                        ("date/time" in message && "timezone" in message) ||
                        ("index=" in message && "length=" in message)
            }
    }

    private fun isIndexBoundaryFailure(error: Exception): Boolean {
        val causes = generateSequence<Throwable>(error) { it.cause }.toList()
        if (causes.any { it is ArrayIndexOutOfBoundsException || it is StringIndexOutOfBoundsException }) {
            return true
        }
        val message = error.message?.lowercase().orEmpty()
        return "index=" in message && "length=" in message
    }

    /**
     * Load a saved chart
     */
    fun loadChart(chartId: Long) {
        viewModelScope.launch(ioDispatcher) {
            _uiState.value = ChartUiState.Loading

            try {
                val chart = repository.getChartById(chartId)
                if (chart != null) {
                    _uiState.value = ChartUiState.Success(chart)
                    _selectedChartId.value = chartId
                    prefs.edit().putLong("last_selected_chart_id", chartId).apply()
                } else {
                    _uiState.value = ChartUiState.Error("Chart not found")
                }
            } catch (e: Exception) {
                _uiState.value = ChartUiState.Error(e.message ?: "Failed to load chart")
            }
        }
    }

    /**
     * Save current chart with duplicate prevention
     * Uses a hash of birth data to prevent saving the same chart multiple times
     * in rapid succession (e.g., due to recomposition or state changes)
     */
    fun saveChart(chart: VedicChart) {
        viewModelScope.launch(ioDispatcher) {
            try {
                // Generate a hash based on birth data to identify unique charts
                val chartHash = generateChartHash(chart)

                // Skip if this exact chart was just saved (prevents duplicates)
                if (lastSavedChartHash == chartHash) {
                    // Already saved this chart, just update state without saving again
                    _uiState.value = ChartUiState.Saved
                    return@launch
                }

                val id = repository.saveChart(chart)
                lastSavedChartHash = chartHash

                // Set the newly saved chart as selected
                _selectedChartId.value = id
                prefs.edit().putLong("last_selected_chart_id", id).apply()

                _uiState.value = ChartUiState.Saved
            } catch (e: Exception) {
                _uiState.value = ChartUiState.Error("Failed to save chart: ${e.message}")
            }
        }
    }

    /**
     * Generate a hash to uniquely identify a chart by its birth data
     * This prevents saving duplicates of the exact same chart
     */
    private fun generateChartHash(chart: VedicChart): Int {
        return Objects.hash(
            chart.birthData.name,
            chart.birthData.dateTime.toString(),
            chart.birthData.latitude,
            chart.birthData.longitude,
            chart.birthData.timezone,
            chart.birthData.location
        )
    }

    /**
     * Delete a saved chart
     */
    fun deleteChart(chartId: Long) {
        viewModelScope.launch(ioDispatcher) {
            try {
                repository.deleteChart(chartId)
                if (_selectedChartId.value == chartId) {
                    prefs.edit().remove("last_selected_chart_id").apply()
                    _selectedChartId.value = null
                    _uiState.value = ChartUiState.Initial
                }
            } catch (e: Exception) {
                _uiState.value = ChartUiState.Error("Failed to delete chart: ${e.message}")
            }
        }
    }

    /**
     * Export chart as image
     */
    fun exportChartImage(chart: VedicChart, fileName: String, density: Density) {
        viewModelScope.launch(ioDispatcher) {
            try {
                val bitmap = withContext(Dispatchers.Default) {
                    // Create a dedicated renderer for export to avoid thread safety issues
                    // with the shared UI renderer which is not thread-safe
                    val exportRenderer = ChartRenderer(application, ChartColorConfig.Light)
                    exportRenderer.createChartBitmap(chart, 2048, 2048, density)
                }

                val result = ExportUtils.saveChartImage(getApplication(), bitmap, fileName)
                result.onSuccess {
                    _uiState.value = ChartUiState.Exported("Image saved successfully")
                }.onFailure {
                    _uiState.value = ChartUiState.Error("Failed to save image: ${it.message}")
                }
            } catch (e: Exception) {
                _uiState.value = ChartUiState.Error("Export failed: ${e.message}")
            }
        }
    }

    /**
     * Export chart to PDF with comprehensive report
     */
    fun exportChartToPdf(
        chart: VedicChart,
        density: Density,
        options: ChartExporter.PdfExportOptions = ChartExporter.PdfExportOptions()
    ) {
        viewModelScope.launch(ioDispatcher) {
            try {
                _uiState.value = ChartUiState.Exporting("Generating PDF report...")
                val result = chartExporter.exportToPdf(chart, options, density)
                when (result) {
                    is ChartExporter.ExportResult.Success -> {
                        _uiState.value = ChartUiState.Exported("PDF saved successfully")
                    }
                    is ChartExporter.ExportResult.Error -> {
                        _uiState.value = ChartUiState.Error(result.message)
                    }
                }
            } catch (e: Exception) {
                _uiState.value = ChartUiState.Error("PDF export failed: ${e.message}")
            }
        }
    }

    /**
     * Export chart to JSON
     */
    fun exportChartToJson(chart: VedicChart) {
        viewModelScope.launch(ioDispatcher) {
            try {
                _uiState.value = ChartUiState.Exporting("Generating JSON...")
                val result = chartExporter.exportToJson(chart)
                when (result) {
                    is ChartExporter.ExportResult.Success -> {
                        _uiState.value = ChartUiState.Exported("JSON saved successfully")
                    }
                    is ChartExporter.ExportResult.Error -> {
                        _uiState.value = ChartUiState.Error(result.message)
                    }
                }
            } catch (e: Exception) {
                _uiState.value = ChartUiState.Error("JSON export failed: ${e.message}")
            }
        }
    }

    /**
     * Export chart to CSV
     */
    fun exportChartToCsv(chart: VedicChart) {
        viewModelScope.launch(ioDispatcher) {
            try {
                _uiState.value = ChartUiState.Exporting("Generating CSV...")
                val result = chartExporter.exportToCsv(chart)
                when (result) {
                    is ChartExporter.ExportResult.Success -> {
                        _uiState.value = ChartUiState.Exported("CSV saved successfully")
                    }
                    is ChartExporter.ExportResult.Error -> {
                        _uiState.value = ChartUiState.Error(result.message)
                    }
                }
            } catch (e: Exception) {
                _uiState.value = ChartUiState.Error("CSV export failed: ${e.message}")
            }
        }
    }

    /**
     * Export chart as high-quality image with options
     */
    fun exportChartToImage(
        chart: VedicChart,
        density: Density,
        options: ChartExporter.ImageExportOptions = ChartExporter.ImageExportOptions()
    ) {
        viewModelScope.launch(ioDispatcher) {
            try {
                _uiState.value = ChartUiState.Exporting("Generating image...")
                val result = chartExporter.exportToImage(chart, options, density)
                when (result) {
                    is ChartExporter.ExportResult.Success -> {
                        _uiState.value = ChartUiState.Exported("Image saved successfully")
                    }
                    is ChartExporter.ExportResult.Error -> {
                        _uiState.value = ChartUiState.Error(result.message)
                    }
                }
            } catch (e: Exception) {
                _uiState.value = ChartUiState.Error("Image export failed: ${e.message}")
            }
        }
    }

    /**
     * Export chart as plain text report
     */
    fun exportChartToText(chart: VedicChart) {
        viewModelScope.launch(ioDispatcher) {
            try {
                _uiState.value = ChartUiState.Exporting("Generating text report...")
                val result = chartExporter.exportToText(chart)
                when (result) {
                    is ChartExporter.ExportResult.Success -> {
                        _uiState.value = ChartUiState.Exported("Text report saved successfully")
                    }
                    is ChartExporter.ExportResult.Error -> {
                        _uiState.value = ChartUiState.Error(result.message)
                    }
                }
            } catch (e: Exception) {
                _uiState.value = ChartUiState.Error("Text export failed: ${e.message}")
            }
        }
    }

    /**
     * Get a chart by ID directly (for matchmaking and other features)
     */
    suspend fun getChartById(chartId: Long): VedicChart? {
        return repository.getChartById(chartId)
    }

    /**
     * Reset UI state to previous chart state if available, otherwise Initial
     * This is used after export operations to restore the normal UI state
     */
    fun resetState() {
        // If we had a chart loaded, restore to Success state
        val currentState = _uiState.value
        when (currentState) {
            is ChartUiState.Exported, is ChartUiState.Error, is ChartUiState.Exporting -> {
                // Try to reload the current chart if one was selected
                _selectedChartId.value?.let { chartId ->
                    loadChart(chartId)
                } ?: run {
                    _uiState.value = ChartUiState.Initial
                }
            }
            else -> {
                // Keep current state for Success, Calculating, Loading, etc.
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        ephemerisEngine.close()
    }
}

/**
 * UI states for chart operations
 */
sealed class ChartUiState {
    object Initial : ChartUiState()
    object Loading : ChartUiState()
    object Calculating : ChartUiState()
    data class Success(val chart: VedicChart) : ChartUiState()
    data class Error(val message: String) : ChartUiState()
    object Saved : ChartUiState()
    data class Exporting(val message: String) : ChartUiState()
    data class Exported(val message: String) : ChartUiState()
}
