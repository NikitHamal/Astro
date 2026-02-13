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
import androidx.compose.runtime.remember
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.astro.storm.core.common.Language
import com.astro.storm.core.common.StringKey
import com.astro.storm.core.common.StringKeyUIExtra
import com.astro.storm.core.common.StringResources
import com.astro.storm.core.common.getLocalizedName
import com.astro.storm.data.localization.LocalLanguage
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
    val language = LocalLanguage.current
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(chart) {
        viewModel.loadDashaTimeline(chart)
    }

    val currentPeriodInfo = remember(uiState, language) {
        extractCurrentVimsottariPeriodInfo(uiState, language)
    }

    Scaffold(
        containerColor = AppTheme.ScreenBackground,
        topBar = {
            VimsottariDashaTopBar(
                chartName = chart?.birthData?.name ?: stringResource(StringKey.NO_PROFILE_MESSAGE),
                currentPeriodInfo = currentPeriodInfo,
                onBack = onBack,
                onTodayClick = { viewModel.requestScrollToToday() }
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

private data class CurrentVimsottariPeriodInfo(
    val mahadasha: String?,
    val antardasha: String?,
    val isLoading: Boolean,
    val hasError: Boolean
)

private fun extractCurrentVimsottariPeriodInfo(
    uiState: DashaUiState,
    language: Language
): CurrentVimsottariPeriodInfo {
    return when (uiState) {
        is DashaUiState.Success -> {
            val md = uiState.timeline.currentMahadasha
            val ad = uiState.timeline.currentAntardasha
            CurrentVimsottariPeriodInfo(
                mahadasha = md?.planet?.getLocalizedName(language),
                antardasha = ad?.planet?.getLocalizedName(language),
                isLoading = false,
                hasError = false
            )
        }
        is DashaUiState.Loading -> CurrentVimsottariPeriodInfo(null, null, true, false)
        is DashaUiState.Error -> CurrentVimsottariPeriodInfo(null, null, false, true)
        is DashaUiState.Idle -> CurrentVimsottariPeriodInfo(null, null, false, false)
    }
}

@Composable
private fun VimsottariDashaTopBar(
    chartName: String,
    currentPeriodInfo: CurrentVimsottariPeriodInfo,
    onBack: () -> Unit,
    onTodayClick: () -> Unit
) {
    val language = LocalLanguage.current
    val subtitle = when {
        currentPeriodInfo.isLoading -> stringResource(StringKey.DASHA_CALCULATING)
        currentPeriodInfo.hasError -> "${stringResource(StringKey.DASHA_ERROR)} - $chartName"
        currentPeriodInfo.mahadasha != null -> buildString {
            append(currentPeriodInfo.mahadasha)
            currentPeriodInfo.antardasha?.let { append(StringResources.get(StringKeyUIExtra.ARROW, language) + it) }
            append(StringResources.get(StringKeyUIExtra.BULLET_SPACE, language) + chartName)
        }
        else -> chartName
    }
    NeoVedicPageHeader(
        title = stringResource(StringKey.FEATURE_DASHAS),
        subtitle = subtitle,
        onBack = onBack,
        actionIcon = Icons.Outlined.CalendarToday,
        onAction = onTodayClick
    )
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

