package com.astro.storm.ui.screen

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.astro.storm.data.model.Nakshatra
import com.astro.storm.data.model.PlanetPosition
import com.astro.storm.data.model.VedicChart
import com.astro.storm.ui.components.NakshatraDetailDialog
import com.astro.storm.ui.components.PlanetDetailDialog
import com.astro.storm.ui.components.ShadbalaDialog
import com.astro.storm.ui.screen.chartdetail.tabs.PlanetsTabContent
import com.astro.storm.ui.theme.AppTheme

/**
 * Planets Screen - Individual Screen for Planetary Analysis
 *
 * This screen displays comprehensive planetary analysis including:
 * - Detailed planetary positions for all 12 planets
 * - Nakshatra (lunar mansion) placements
 * - Retrograde and combustion status
 * - Planetary strengths (Shadbala)
 * - House placements
 * - Sign placements with degrees
 *
 * Replaces the "Planets" tab from the tabbed interface with an enhanced UI/UX.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlanetsScreen(
    chart: VedicChart,
    onBack: () -> Unit
) {
    // Dialog states
    var selectedPlanetPosition by remember { mutableStateOf<PlanetPosition?>(null) }
    var selectedNakshatra by remember { mutableStateOf<Pair<Nakshatra, Int>?>(null) }
    var showShadbalaDialog by remember { mutableStateOf(false) }

    // Render dialogs
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

    if (showShadbalaDialog) {
        ShadbalaDialog(
            chart = chart,
            onDismiss = { showShadbalaDialog = false }
        )
    }

    Scaffold(
        containerColor = AppTheme.ScreenBackground,
        topBar = {
            PlanetsTopBar(
                chartName = chart.birthData.name,
                onBack = onBack
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(AppTheme.ScreenBackground)
        ) {
            PlanetsTabContent(
                chart = chart,
                onPlanetClick = { selectedPlanetPosition = it },
                onNakshatraClick = { nakshatra, pada -> selectedNakshatra = nakshatra to pada },
                onShadbalaClick = { showShadbalaDialog = true }
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun PlanetsTopBar(
    chartName: String,
    onBack: () -> Unit
) {
    TopAppBar(
        title = {
            Column {
                Text(
                    text = "Planets",
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
