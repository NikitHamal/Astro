package com.astro.storm.ui.screen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.CalendarToday
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.astro.storm.core.common.StringKey
import com.astro.storm.data.localization.stringResource
import com.astro.storm.core.model.VedicChart
import com.astro.storm.ui.components.common.NeoVedicPageHeader
import com.astro.storm.ui.screen.chartdetail.tabs.DashasTabContent
import com.astro.storm.ui.theme.AppTheme
import com.astro.storm.ui.viewmodel.DashaUiState
import com.astro.storm.ui.viewmodel.DashaViewModel

/**
 * Vimsottari Dasha screen using NeoVedic design.
 */
@Composable
fun VimsottariDashaScreen(
    chart: VedicChart?,
    onBack: () -> Unit,
    viewModel: DashaViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(chart) {
        viewModel.loadDashaTimeline(chart)
    }

    Scaffold(
        containerColor = AppTheme.ScreenBackground,
        topBar = {
            NeoVedicPageHeader(
                title = stringResource(StringKey.FEATURE_DASHAS),
                subtitle = chart?.birthData?.name ?: stringResource(StringKey.NO_PROFILE_MESSAGE),
                onBack = onBack,
                actionIcon = Icons.Outlined.CalendarToday,
                onAction = { viewModel.requestScrollToToday() }
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            when (val state = uiState) {
                is DashaUiState.Loading -> VimsottariLoadingContent()
                is DashaUiState.Success -> DashasTabContent(
                    timeline = state.timeline,
                    scrollToTodayEvent = viewModel.scrollToTodayEvent
                )
                is DashaUiState.Error -> VimsottariErrorContent(
                    message = state.message,
                    onRetry = { viewModel.loadDashaTimeline(chart) }
                )
                is DashaUiState.Idle -> EmptyChartScreen(
                    title = stringResource(StringKey.FEATURE_DASHAS),
                    message = stringResource(StringKey.NO_PROFILE_MESSAGE),
                    onBack = onBack
                )
            }
        }
    }
}

@Composable
private fun VimsottariLoadingContent() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator(color = AppTheme.AccentPrimary)
    }
}

@Composable
private fun VimsottariErrorContent(
    message: String,
    onRetry: () -> Unit
) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = message,
                style = MaterialTheme.typography.bodyMedium,
                color = AppTheme.TextSecondary
            )
            Button(
                onClick = onRetry,
                modifier = Modifier.padding(top = 16.dp)
            ) {
                Text(text = stringResource(StringKey.BTN_RETRY))
            }
        }
    }
}

