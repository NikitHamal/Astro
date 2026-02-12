package com.astro.storm.ui.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.astro.storm.core.common.StringKey
import com.astro.storm.core.model.VedicChart
import com.astro.storm.ephemeris.DashaCalculator
import com.astro.storm.ephemeris.HoroscopeCalculator
import com.astro.storm.util.ChartUtils
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.ensureActive
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.supervisorScope
import kotlinx.coroutines.withContext
import java.time.DateTimeException
import java.time.LocalDate
import java.time.ZoneId
import java.time.ZoneOffset
import java.util.concurrent.ConcurrentLinkedQueue
import javax.inject.Inject
import kotlin.math.roundToInt

data class InsightsData(
    val chart: VedicChart,
    val dashaTimeline: DashaCalculator.DashaTimeline?,
    val planetaryInfluences: List<HoroscopeCalculator.PlanetaryInfluence>,
    val todayHoroscope: HoroscopeCalculator.DailyHoroscope?,
    val tomorrowHoroscope: HoroscopeCalculator.DailyHoroscope?,
    val weeklyHoroscope: HoroscopeCalculator.WeeklyHoroscope?,
    val errors: List<InsightError> = emptyList()
)

data class InsightError(
    val type: InsightErrorType,
    val messageKey: StringKey = StringKey.ERROR_SOMETHING_WRONG
)

enum class InsightErrorType {
    TODAY_HOROSCOPE,
    TOMORROW_HOROSCOPE,
    WEEKLY_HOROSCOPE,
    DASHA,
    GENERAL
}

sealed class InsightsUiState {
    data object Loading : InsightsUiState()
    data class Success(val data: InsightsData) : InsightsUiState()
    data class Error(val messageKey: StringKey) : InsightsUiState()
    data object Idle : InsightsUiState()
}

@HiltViewModel
class InsightsViewModel @Inject constructor(
    application: Application,
    private val horoscopeCalculator: HoroscopeCalculator,
    private val localizationManager: com.astro.storm.data.localization.LocalizationManager
) : AndroidViewModel(application) {

    private val _uiState = MutableStateFlow<InsightsUiState>(InsightsUiState.Idle)
    val uiState = _uiState.asStateFlow()

    private var currentLoadJob: Job? = null
    private var cachedData: InsightsData? = null
    private var cachedChartId: String? = null
    private var cachedDate: LocalDate? = null
    private var cachedLanguage: com.astro.storm.core.common.Language? = null

    /**
     * Uses shared ChartUtils for consistent cache key generation across ViewModels.
     */
    private fun getChartId(chart: VedicChart): String = ChartUtils.generateChartKey(chart)

    private fun isCacheValid(chartId: String, today: LocalDate, language: com.astro.storm.core.common.Language): Boolean {
        return cachedData?.let {
            cachedChartId == chartId &&
            cachedDate == today &&
            cachedLanguage == language &&
            it.todayHoroscope != null &&
            it.weeklyHoroscope != null
        } ?: false
    }

    fun loadInsights(chart: VedicChart?) {
        if (chart == null) {
            _uiState.value = InsightsUiState.Idle
            return
        }

        val today = LocalDate.now(resolveZoneId(chart.birthData.timezone))
        val chartId = getChartId(chart)
        val language = localizationManager.currentLanguage

        if (isCacheValid(chartId, today, language)) {
            cachedData?.let {
                _uiState.value = InsightsUiState.Success(it)
                return
            }
        }

        currentLoadJob?.cancel()

        currentLoadJob = viewModelScope.launch {
            _uiState.value = InsightsUiState.Loading

            try {
                val errors = ConcurrentLinkedQueue<InsightError>()

                val loadedData = loadInsightsData(chart, today, language, errors)

                ensureActive()

                if (loadedData.todayHoroscope == null && loadedData.dashaTimeline == null) {
                    _uiState.value = InsightsUiState.Error(StringKey.ERROR_EPHEMERIS_DATA)
                    return@launch
                }

                val finalData = InsightsData(
                    chart = chart,
                    dashaTimeline = loadedData.dashaTimeline,
                    planetaryInfluences = loadedData.todayHoroscope?.planetaryInfluences ?: emptyList(),
                    todayHoroscope = loadedData.todayHoroscope,
                    tomorrowHoroscope = loadedData.tomorrowHoroscope,
                    weeklyHoroscope = loadedData.weeklyHoroscope,
                    errors = errors.toList()
                )

                cachedData = finalData
                cachedChartId = chartId
                cachedDate = today
                cachedLanguage = language

                _uiState.value = InsightsUiState.Success(finalData)

            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                Log.e(TAG, "Unexpected error loading insights", e)
                _uiState.value = InsightsUiState.Error(StringKey.ERROR_SOMETHING_WRONG)
            }
        }
    }

    private data class LoadedInsights(
        val dashaTimeline: DashaCalculator.DashaTimeline?,
        val todayHoroscope: HoroscopeCalculator.DailyHoroscope?,
        val tomorrowHoroscope: HoroscopeCalculator.DailyHoroscope?,
        val weeklyHoroscope: HoroscopeCalculator.WeeklyHoroscope?
    )

    private suspend fun loadInsightsData(
        chart: VedicChart,
        today: LocalDate,
        language: com.astro.storm.core.common.Language,
        errors: ConcurrentLinkedQueue<InsightError>
    ): LoadedInsights {
        return withContext(Dispatchers.Default) {
            coroutineScope {
                val dashaDeferred = async {
                    try {
                        DashaCalculator.calculateDashaTimeline(chart)
                    } catch (e: Exception) {
                        if (e is CancellationException) throw e
                        Log.e(TAG, "Dasha calculation failed", e)
                        errors.add(InsightError(InsightErrorType.DASHA, StringKey.DASHA_ERROR))
                        null
                    }
                }

                val todayDeferred = async {
                    try {
                        horoscopeCalculator.calculateDailyHoroscope(chart, today, language)
                    } catch (e: Exception) {
                        if (e is CancellationException) throw e
                        Log.e(TAG, "Today's horoscope calculation failed", e)
                        errors.add(InsightError(InsightErrorType.TODAY_HOROSCOPE, StringKey.ERROR_ANALYSIS_FAILED))
                        null
                    }
                }

                val tomorrowDeferred = async {
                    try {
                        horoscopeCalculator.calculateDailyHoroscope(chart, today.plusDays(1), language)
                    } catch (e: Exception) {
                        if (e is CancellationException) throw e
                        Log.e(TAG, "Tomorrow's horoscope calculation failed", e)
                        errors.add(InsightError(InsightErrorType.TOMORROW_HOROSCOPE, StringKey.ERROR_ANALYSIS_FAILED))
                        null
                    }
                }

                val weeklyDeferred = async {
                    try {
                        horoscopeCalculator.calculateWeeklyHoroscope(chart, today, language)
                    } catch (e: Exception) {
                        if (e is CancellationException) throw e
                        Log.e(TAG, "Weekly horoscope calculation failed", e)
                        errors.add(InsightError(InsightErrorType.WEEKLY_HOROSCOPE, StringKey.ERROR_ANALYSIS_FAILED))
                        null
                    }
                }

                LoadedInsights(
                    dashaTimeline = dashaDeferred.await(),
                    todayHoroscope = todayDeferred.await(),
                    tomorrowHoroscope = tomorrowDeferred.await(),
                    weeklyHoroscope = weeklyDeferred.await()
                )
            }
        }
    }

    fun refreshInsights(chart: VedicChart?) {
        clearCache()
        loadInsights(chart)
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

    fun clearCache() {
        cachedData = null
        cachedChartId = null
        cachedDate = null
        // horoscopeCalculator is injected singleton, caching is handled internally or should be managed differently if needed per-VM
    }

    override fun onCleared() {
        super.onCleared()
        currentLoadJob?.cancel()
        // Don't close injected singleton
    }

    companion object {
        private const val TAG = "InsightsViewModel"
    }
}
