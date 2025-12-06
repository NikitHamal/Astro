package com.astro.storm.ui.screen

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.astro.storm.data.model.VedicChart
import com.astro.storm.ui.screen.chartdetail.tabs.DashasTabContent
import com.astro.storm.ui.theme.AppTheme

/**
 * Dashas Screen - Individual Screen for Planetary Periods
 *
 * This screen displays comprehensive Dasha analysis including:
 * - Vimshottari Dasha (20-year cycle dasha system)
 * - Current Mahadasha (major period)
 * - Current Bhukti (sub-period)
 * - Complete dasha timeline
 * - Period predictions and influence
 * - Sub-periods (Antardashas)
 * - Timing of major life events
 *
 * Replaces the "Dashas" tab from the tabbed interface with an enhanced UI/UX.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashasScreen(
    chart: VedicChart,
    onBack: () -> Unit
) {
    Scaffold(
        containerColor = AppTheme.ScreenBackground,
        topBar = {
            DashasTopBar(
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
            DashasTabContent(chart = chart)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun DashasTopBar(
    chartName: String,
    onBack: () -> Unit
) {
    TopAppBar(
        title = {
            Column {
                Text(
                    text = "Dashas",
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
