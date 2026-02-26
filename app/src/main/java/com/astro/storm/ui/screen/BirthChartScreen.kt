package com.astro.storm.ui.screen

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.outlined.CalendarMonth
import androidx.compose.material.icons.outlined.ContentCopy
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material.icons.outlined.Schedule
import androidx.compose.material.icons.rounded.AutoGraph
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import com.astro.storm.ui.components.common.NeoVedicPageHeader
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.astro.storm.data.localization.LocalLanguage
import com.astro.storm.core.common.StringKey
import com.astro.storm.core.common.StringKeyMatch
import com.astro.storm.core.common.getLocalizedName
import com.astro.storm.data.localization.stringResource
import com.astro.storm.core.model.Nakshatra
import com.astro.storm.core.model.PlanetPosition
import com.astro.storm.core.model.VedicChart
import com.astro.storm.ephemeris.DivisionalChartData
import com.astro.storm.ui.chart.ChartRenderer
import com.astro.storm.ui.components.FullScreenChartDialog
import com.astro.storm.ui.components.HouseDetailDialog
import com.astro.storm.ui.components.NakshatraDetailDialog
import com.astro.storm.ui.components.dialogs.PlanetDetailDialog
import com.astro.storm.ui.screen.chartdetail.tabs.ChartTabContent
import com.astro.storm.ui.theme.AppTheme
import com.astro.storm.ui.viewmodel.ChartViewModel
import java.time.format.DateTimeFormatter
import java.util.Locale

// Performance Optimization: Pass specific, stable parameters instead of the entire ViewModel.
// This makes the Composable's inputs more stable, allowing Compose to skip
// unnecessary recompositions more effectively.
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BirthChartScreen(
    chart: VedicChart?,
    chartRenderer: ChartRenderer,
    onBack: () -> Unit
) {
    if (chart == null) {
        EmptyChartScreen(
            title = stringResource(StringKey.FEATURE_BIRTH_CHART),
            message = stringResource(StringKey.NO_PROFILE_MESSAGE_LONG),
            onBack = onBack
        )
        return
    }

    val hapticFeedback = LocalHapticFeedback.current
    val ascendantLabel = stringResource(StringKey.CHART_ASCENDANT)

    var showFullScreenChart by remember { mutableStateOf(false) }
    var fullScreenChartTitle by remember { mutableStateOf(ascendantLabel) }
    var fullScreenDivisionalData by remember { mutableStateOf<DivisionalChartData?>(null) }
    var selectedPlanetPosition by remember { mutableStateOf<PlanetPosition?>(null) }
    var selectedNakshatra by remember { mutableStateOf<Pair<Nakshatra, Int>?>(null) }
    var selectedHouse by remember { mutableStateOf<Int?>(null) }

    AnimatedVisibility(
        visible = showFullScreenChart,
        enter = fadeIn(tween(200)) + scaleIn(tween(200), initialScale = 0.95f),
        exit = fadeOut(tween(150)) + scaleOut(tween(150), targetScale = 0.95f)
    ) {
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

    selectedNakshatra?.let { (nakshatra, pada) ->
        NakshatraDetailDialog(
            nakshatra = nakshatra,
            pada = pada,
            onDismiss = { selectedNakshatra = null }
        )
    }

    selectedHouse?.let { houseNum ->
        val houseCusp = if (houseNum in 1..chart.houseCusps.size) chart.houseCusps[houseNum - 1] else 0.0
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
                onBack = {
                    hapticFeedback.performHapticFeedback(HapticFeedbackType.LongPress)
                    onBack()
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(AppTheme.ScreenBackground)
        ) {
            ChartTabContent(
                chart = chart,
                chartRenderer = chartRenderer,
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
    NeoVedicPageHeader(
                title = stringResource(StringKey.FEATURE_BIRTH_CHART),
                subtitle = chartName,
                onBack = onBack
            )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EmptyChartScreen(
    title: String,
    message: String,
    onBack: () -> Unit
) {
    val hapticFeedback = LocalHapticFeedback.current

    Scaffold(
        containerColor = AppTheme.ScreenBackground,
        topBar = {
            NeoVedicPageHeader(
                title = title,
                onBack = {
                            hapticFeedback.performHapticFeedback(HapticFeedbackType.LongPress)
                            onBack()
                        }
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(AppTheme.ScreenBackground),
            contentAlignment = Alignment.Center
        ) {
            com.astro.storm.ui.components.common.NeoVedicEmptyState(
                title = title,
                subtitle = message,
                icon = Icons.Rounded.AutoGraph
            )
        }
    }
}




