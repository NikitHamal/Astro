package com.astro.storm.data.repository

import com.astro.storm.data.local.ChartDao
import com.astro.storm.data.local.ChartEntity
import com.astro.storm.util.TimezoneSanitizer
import com.astro.storm.core.model.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Repository for chart data operations
 */
@Singleton
class ChartRepository @Inject constructor(private val chartDao: ChartDao) {

    fun getAllCharts(): Flow<List<SavedChart>> {
        return chartDao.getAllCharts().map { entities ->
            entities.map { it.toSavedChart() }
        }
    }

    suspend fun getChartById(id: Long): VedicChart? {
        return chartDao.getChartById(id)?.toVedicChart()
    }

    suspend fun saveChart(chart: VedicChart): Long {
        return chartDao.insertChart(chart.toEntity())
    }

    suspend fun deleteChart(id: Long) {
        chartDao.deleteChartById(id)
    }

    /**
     * Update an existing chart with new data
     */
    suspend fun updateChart(id: Long, chart: VedicChart) {
        val entity = chart.toEntity().copy(id = id)
        chartDao.updateChart(entity)
    }

    fun searchCharts(query: String): Flow<List<SavedChart>> {
        return chartDao.searchCharts(query).map { entities ->
            entities.map { it.toSavedChart() }
        }
    }

    private fun VedicChart.toEntity(): ChartEntity {
        return ChartEntity(
            id = id,
            name = birthData.name,
            dateTime = birthData.dateTime.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME),
            latitude = birthData.latitude,
            longitude = birthData.longitude,
            timezone = TimezoneSanitizer.normalizeTimezoneId(birthData.timezone),
            location = birthData.location,
            julianDay = julianDay,
            ayanamsa = ayanamsa,
            ayanamsaName = ayanamsaName,
            ascendant = ascendant,
            midheaven = midheaven,
            planetPositions = planetPositions,
            houseCusps = houseCusps,
            houseSystem = houseSystem.name,
            gender = birthData.gender.name
        )
    }

    private fun ChartEntity.toVedicChart(): VedicChart {
        val normalizedTimezone = TimezoneSanitizer.normalizeTimezoneId(timezone)
        return VedicChart(
            birthData = BirthData(
                name = name,
                dateTime = LocalDateTime.parse(dateTime, DateTimeFormatter.ISO_LOCAL_DATE_TIME),
                latitude = latitude,
                longitude = longitude,
                timezone = normalizedTimezone,
                location = location,
                gender = Gender.fromString(gender)
            ),
            julianDay = julianDay,
            ayanamsa = ayanamsa,
            ayanamsaName = ayanamsaName,
            ascendant = ascendant,
            midheaven = midheaven,
            planetPositions = planetPositions,
            houseCusps = houseCusps,
            houseSystem = HouseSystem.valueOf(houseSystem),
            calculationTime = createdAt
        )
    }

    private fun ChartEntity.toSavedChart(): SavedChart {
        val normalizedTimezone = TimezoneSanitizer.normalizeTimezoneId(timezone)
        return SavedChart(
            id = id,
            name = name,
            dateTime = LocalDateTime.parse(dateTime, DateTimeFormatter.ISO_LOCAL_DATE_TIME),
            location = location,
            createdAt = createdAt,
            latitude = latitude,
            longitude = longitude,
            timezone = normalizedTimezone,
            gender = Gender.fromString(gender)
        )
    }
}

/**
 * Simplified chart data for list display and editing
 */
data class SavedChart(
    val id: Long,
    val name: String,
    val dateTime: LocalDateTime,
    val location: String,
    val createdAt: Long,
    // Fields needed for editing
    val latitude: Double = 0.0,
    val longitude: Double = 0.0,
    val timezone: String = "UTC",
    val gender: Gender = Gender.OTHER
)
