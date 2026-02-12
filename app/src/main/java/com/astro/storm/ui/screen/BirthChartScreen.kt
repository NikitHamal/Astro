package com.astro.storm.ui.screen

import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.material.icons.outlined.Share
import androidx.compose.material.icons.rounded.AutoGraph
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.astro.storm.core.common.StringKey
import com.astro.storm.core.common.StringKeyMatch
import com.astro.storm.data.localization.stringResource
import com.astro.storm.core.model.VedicChart
import com.astro.storm.core.model.PlanetPosition
import com.astro.storm.core.model.Nakshatra
import com.astro.storm.ui.chart.ChartRenderer
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
        EmptyChartScreen(
            title = "Birth Chart",
            message = "No profile selected",
            onBack = onBack
        )
        return
    }

    val hapticFeedback = LocalHapticFeedback.current

    var selectedPlanetPosition by remember { mutableStateOf<PlanetPosition?>(null) }
    var selectedNakshatra by remember { mutableStateOf<Pair<Nakshatra, Int>?>(null) }
    var selectedHouse by remember { mutableStateOf<Int?>(null) }

    // Dialogs
    selectedPlanetPosition?.let { position ->
        com.astro.storm.ui.components.dialogs.PlanetDetailDialog(
            planetPosition = position,
            chart = chart,
            onDismiss = { selectedPlanetPosition = null }
        )
    }

    selectedNakshatra?.let { (nakshatra, pada) ->
        com.astro.storm.ui.components.NakshatraDetailDialog(
            nakshatra = nakshatra,
            pada = pada,
            onDismiss = { selectedNakshatra = null }
        )
    }

    selectedHouse?.let { houseNum ->
        val houseCusp = if (houseNum in 1..chart.houseCusps.size) chart.houseCusps[houseNum - 1] else 0.0
        val planetsInHouse = chart.planetPositions.filter { it.house == houseNum }
        com.astro.storm.ui.components.HouseDetailDialog(
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
                    IconButton(onClick = { /* Share action */ }) {
                        Icon(Icons.Outlined.Share, "Share", tint = CosmicIndigo)
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = Vellum.copy(alpha = 0.95f),
                    titleContentColor = CosmicIndigo
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EmptyChartScreen(
    title: String,
    message: String,
    onBack: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Vellum),
        contentAlignment = Alignment.Center
    ) {
        com.astro.storm.ui.screen.main.GrainTextureOverlay()
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.padding(horizontal = 48.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(80.dp)
                    .clip(RoundedCornerShape(2.dp))
                    .background(Paper),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Rounded.AutoGraph,
                    contentDescription = null,
                    tint = SlateMuted,
                    modifier = Modifier.size(40.dp)
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = CosmicIndigo,
                fontFamily = CinzelDecorativeFontFamily,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = message,
                style = MaterialTheme.typography.bodyMedium,
                color = SlateMuted,
                fontFamily = CormorantGaramondFontFamily,
                textAlign = TextAlign.Center,
                lineHeight = 20.sp
            )

            Spacer(modifier = Modifier.height(32.dp))

            Button(
                onClick = onBack,
                colors = ButtonDefaults.buttonColors(containerColor = CosmicIndigo, contentColor = Vellum),
                shape = RoundedCornerShape(2.dp)
            ) {
                Text("BACK", fontFamily = SpaceGroteskFontFamily)
            }
        }
    }
}
