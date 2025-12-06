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
import com.astro.storm.ui.screen.chartdetail.tabs.PanchangaTabContent
import com.astro.storm.ui.theme.AppTheme

/**
 * Panchanga Screen - Individual Screen for Lunar Calendar Analysis
 *
 * This screen displays comprehensive Panchanga (five-fold time) analysis including:
 * - Tithi (lunar day - phase of the moon)
 * - Nakshatra (lunar mansion - star position)
 * - Yoga (auspicious lunar configuration)
 * - Karana (half-day period)
 * - Vara (day of the week)
 * - Moon phase details
 * - Lunar month and year information
 * - Muhurta (auspicious time) indicators
 *
 * Replaces the "Panchanga" tab from the tabbed interface with an enhanced UI/UX.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PanchangaScreenWrapper(
    chart: VedicChart,
    onBack: () -> Unit
) {
    Scaffold(
        containerColor = AppTheme.ScreenBackground,
        topBar = {
            PanchangaTopBar(
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
            PanchangaTabContent(chart = chart)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun PanchangaTopBar(
    chartName: String,
    onBack: () -> Unit
) {
    TopAppBar(
        title = {
            Column {
                Text(
                    text = "Panchanga",
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
