package com.astro.storm.ui.screen.main

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.FormatQuote
import androidx.compose.material.icons.outlined.Planet
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.astro.storm.core.model.VedicChart
import com.astro.storm.ui.theme.AppTheme
import com.astro.storm.ui.viewmodel.InsightsData
import com.astro.storm.ui.viewmodel.InsightsUiState
import com.astro.storm.ui.viewmodel.InsightsViewModel

@Composable
fun NeoVedicInsightsTab(
    chart: VedicChart?,
    onCreateChart: () -> Unit = {},
    viewModel: InsightsViewModel = hiltViewModel()
) {
    val chartIdentity = remember(chart) { chart?.id to chart?.birthData?.dateTime }

    LaunchedEffect(chartIdentity) {
        viewModel.loadInsights(chart)
    }

    val state by viewModel.uiState.collectAsState()
    when (state) {
        is InsightsUiState.Loading -> InsightsLoadingSkeleton()
        is InsightsUiState.Error -> InsightsErrorState((state as InsightsUiState.Error).messageKey) { viewModel.refreshInsights(chart) }
        is InsightsUiState.Idle -> EmptyInsightsState(onCreateChart = onCreateChart)
        is InsightsUiState.Success -> NeoOracleContent((state as InsightsUiState.Success).data)
    }
}

@Composable
private fun NeoOracleContent(data: InsightsData) {
    val daily = data.todayHoroscope ?: data.tomorrowHoroscope
    val title = (data.dashaTimeline?.currentMahadasha?.planet?.name ?: "Daily Insight").uppercase()
    val subtitle = daily?.themeDescription ?: "The Teacher of Patience"

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(AppTheme.ScreenBackground),
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 20.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, AppTheme.BorderColor)
                    .background(AppTheme.CardBackgroundElevated)
                    .padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(Icons.Outlined.Planet, contentDescription = null, tint = AppTheme.AccentGold)
                Spacer(modifier = Modifier.height(8.dp))
                Text(title, style = MaterialTheme.typography.headlineMedium, color = AppTheme.TextPrimary, textAlign = TextAlign.Center)
                Text(
                    subtitle,
                    style = MaterialTheme.typography.titleMedium,
                    color = AppTheme.AccentGold,
                    fontStyle = FontStyle.Italic,
                    textAlign = TextAlign.Center
                )
                val cycle = data.dashaTimeline?.currentMahadasha?.let { "Cycle: ${it.startDate.year} — ${it.endDate.year}" }
                if (cycle != null) {
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(cycle, style = MaterialTheme.typography.labelSmall, color = AppTheme.TextMuted, letterSpacing = 1.5.sp)
                }
            }
        }

        daily?.let { horoscope ->
            item {
                Text(
                    text = horoscope.lifeAreas.joinToString(" ") { it.prediction },
                    style = MaterialTheme.typography.bodyLarge.copy(lineHeight = 30.sp),
                    color = AppTheme.TextPrimary,
                    textAlign = TextAlign.Justify,
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(1.dp, AppTheme.BorderColor)
                        .background(AppTheme.CardBackground)
                        .padding(18.dp)
                )
            }

            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(1.dp, AppTheme.AccentGold.copy(alpha = 0.6f))
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(Icons.Outlined.FormatQuote, contentDescription = null, tint = AppTheme.AccentGold)
                    Text(
                        text = horoscope.affirmation,
                        style = MaterialTheme.typography.titleMedium,
                        color = AppTheme.TextPrimary,
                        textAlign = TextAlign.Center,
                        fontStyle = FontStyle.Italic
                    )
                }
            }
        }

        item {
            Text(
                text = "READING COMPLETE",
                style = MaterialTheme.typography.labelSmall,
                color = AppTheme.TextMuted,
                letterSpacing = 2.sp,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center
            )
        }
    }
}
