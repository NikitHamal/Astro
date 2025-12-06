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
import com.astro.storm.ui.screen.chartdetail.tabs.YogasTabContent
import com.astro.storm.ui.theme.AppTheme

/**
 * Yogas Screen - Individual Screen for Yoga Classification
 *
 * This screen displays comprehensive yoga analysis including:
 * - Raja Yogas (yoga of kings - responsible for power and success)
 * - Dhana Yogas (wealth yogas)
 * - Mahapurusha Yogas (great person yogas for exceptional qualities)
 * - Neech Bhanga Yogas (cancellation of debility)
 * - Parivartana Yogas (exchange of houses)
 * - Other yogas (auspicious and inauspicious combinations)
 * - Yoga strength and activation periods
 *
 * Replaces the "Yogas" tab from the tabbed interface with an enhanced UI/UX.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun YogasScreen(
    chart: VedicChart,
    onBack: () -> Unit
) {
    Scaffold(
        containerColor = AppTheme.ScreenBackground,
        topBar = {
            YogasTopBar(
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
            YogasTabContent(chart = chart)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun YogasTopBar(
    chartName: String,
    onBack: () -> Unit
) {
    TopAppBar(
        title = {
            Column {
                Text(
                    text = "Yogas",
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
