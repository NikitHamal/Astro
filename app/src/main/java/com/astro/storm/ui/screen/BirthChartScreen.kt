package com.astro.storm.ui.screen

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.material.icons.outlined.Share
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.astro.storm.core.model.VedicChart
import com.astro.storm.core.model.PlanetPosition
import com.astro.storm.core.model.Nakshatra
import com.astro.storm.ui.chart.ChartRenderer
import com.astro.storm.ui.components.HouseDetailDialog
import com.astro.storm.ui.components.NakshatraDetailDialog
import com.astro.storm.ui.components.dialogs.PlanetDetailDialog
import com.astro.storm.ui.screen.chartdetail.tabs.ChartTabContent
import com.astro.storm.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BirthChartScreen(
    chart: VedicChart?,
    chartRenderer: ChartRenderer,
    onBack: () -> Unit
) {
    if (chart == null) {
        EmptyChartScreen(onBack = onBack)
        return
    }

    val hapticFeedback = LocalHapticFeedback.current

    var selectedPlanetPosition by remember { mutableStateOf<PlanetPosition?>(null) }
    var selectedNakshatra by remember { mutableStateOf<Pair<Nakshatra, Int>?>(null) }
    var selectedHouse by remember { mutableStateOf<Int?>(null) }

    // Dialogs
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
        containerColor = Vellum,
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            "Natal Artifact",
                            style = MaterialTheme.typography.titleLarge,
                            color = CosmicIndigo,
                            fontFamily = CinzelDecorativeFontFamily,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            chart.birthData.name.uppercase(),
                            style = MaterialTheme.typography.labelSmall,
                            color = SlateMuted,
                            fontFamily = SpaceGroteskFontFamily,
                            letterSpacing = 1.sp
                        )
                    }
                },
                navigationIcon = {
                    IconButton(onClick = {
                        hapticFeedback.performHapticFeedback(HapticFeedbackType.LongPress)
                        onBack()
                    }) {
                        Icon(Icons.Outlined.ArrowBack, "Back", tint = CosmicIndigo)
                    }
                },
                actions = {
                    IconButton(onClick = { /* Share */ }) {
                        Icon(Icons.Outlined.Share, "Share", tint = CosmicIndigo)
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = Vellum.copy(alpha = 0.95f)
                )
            )
        }
    ) { paddingValues ->
        Box(modifier = Modifier.fillMaxSize().padding(paddingValues)) {
            com.astro.storm.ui.screen.main.GrainTextureOverlay()

            ChartTabContent(
                chart = chart,
                chartRenderer = chartRenderer,
                onChartClick = { _, _ -> },
                onPlanetClick = { selectedPlanetPosition = it },
                onHouseClick = { selectedHouse = it }
            )
        }
    }
}

@Composable
fun EmptyChartScreen(onBack: () -> Unit) {
    Box(modifier = Modifier.fillMaxSize().background(Vellum), contentAlignment = Alignment.Center) {
        Button(onClick = onBack) { Text("Back") }
    }
}
