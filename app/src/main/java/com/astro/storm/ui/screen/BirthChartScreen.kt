package com.astro.storm.ui.screen

import android.content.Context
import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.astro.storm.data.model.PlanetPosition
import com.astro.storm.data.model.VedicChart
import com.astro.storm.data.model.ZodiacSign
import com.astro.storm.ephemeris.DivisionalChartCalculator
import com.astro.storm.ephemeris.DivisionalChartData
import com.astro.storm.ephemeris.DivisionalChartType
import com.astro.storm.ui.chart.ChartRenderer
import com.astro.storm.ui.components.FullScreenChartDialog
import com.astro.storm.ui.components.HouseDetailDialog
import com.astro.storm.ui.components.PlanetDetailDialog
import com.astro.storm.ui.screen.chartdetail.ChartDetailColors
import com.astro.storm.ui.screen.chartdetail.ChartDetailUtils
import com.astro.storm.ui.screen.chartdetail.tabs.ChartTabContent
import com.astro.storm.ui.theme.AppTheme
import java.time.format.DateTimeFormatter

/**
 * Birth Chart Screen - Individual Screen for Birth Chart Analysis
 *
 * This screen displays comprehensive birth chart details including:
 * - Lagna (Ascendant) chart
 * - Divisional charts (D1-D60)
 * - Planetary positions
 * - House cusps and planetary house placements
 * - Birth information
 *
 * Replaces the "Chart" tab from the tabbed interface with an enhanced UI/UX.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BirthChartScreen(
    chart: VedicChart,
    onBack: () -> Unit
) {
    val context = LocalContext.current
    val chartRenderer = remember { ChartRenderer() }

    // Dialog states
    var showFullScreenChart by remember { mutableStateOf(false) }
    var fullScreenChartTitle by remember { mutableStateOf("Lagna") }
    var fullScreenDivisionalData by remember { mutableStateOf<DivisionalChartData?>(null) }
    var selectedPlanetPosition by remember { mutableStateOf<PlanetPosition?>(null) }
    var selectedHouse by remember { mutableStateOf<Int?>(null) }

    // Render dialogs
    if (showFullScreenChart) {
        FullScreenChartDialog(
            chart = chart,
            chartRenderer = chartRenderer,
            chartTitle = fullScreenChartTitle,
            divisionalChartData = fullScreenDivisionalData,
            onDismiss = { showFullScreenChart = false }
        )
    }

    selectedPlanetPosition?.let { position ->
        PlanetDetailDialog(
            planetPosition = position,
            chart = chart,
            onDismiss = { selectedPlanetPosition = null }
        )
    }

    selectedHouse?.let { houseNum ->
        val houseCusp = if (houseNum <= chart.houseCusps.size) chart.houseCusps[houseNum - 1] else 0.0
        val planetsInHouse = chart.planetPositions.filter { it.house == houseNum }
        HouseDetailDialog(
            houseNumber = houseNum,
            houseCusp = houseCusp,
            planetsInHouse = planetsInHouse,
            chart = chart,
            onDismiss = { selectedHouse = null }
        )
    }

    Scaffold(
        containerColor = AppTheme.ScreenBackground,
        topBar = {
            BirthChartTopBar(
                chartName = chart.birthData.name,
                onBack = onBack
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            ChartTabContent(
                chart = chart,
                chartRenderer = chartRenderer,
                context = context,
                onChartClick = { title, divisionalData ->
                    fullScreenChartTitle = title
                    fullScreenDivisionalData = divisionalData
                    showFullScreenChart = true
                },
                onPlanetClick = { selectedPlanetPosition = it },
                onHouseClick = { selectedHouse = it }
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun BirthChartTopBar(
    chartName: String,
    onBack: () -> Unit
) {
    TopAppBar(
        title = {
            Column {
                Text(
                    text = "Birth Chart",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = AppTheme.TextPrimary
                )
                Text(
                    text = chartName,
                    style = MaterialTheme.typography.bodySmall,
                    color = AppTheme.TextMuted
                )
            }
        },
        navigationIcon = {
            IconButton(onClick = onBack) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back",
                    tint = AppTheme.TextPrimary
                )
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = AppTheme.ScreenBackground
        )
    )
}
