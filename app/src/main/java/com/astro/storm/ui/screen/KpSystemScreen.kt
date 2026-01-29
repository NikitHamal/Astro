package com.astro.storm.ui.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.HelpOutline
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.astro.storm.core.common.StringKey
import com.astro.storm.core.common.StringKeyAdvanced
import com.astro.storm.core.common.StringKeyAnalysis
import com.astro.storm.data.localization.LocalLanguage
import com.astro.storm.data.localization.localizedName
import com.astro.storm.data.localization.stringResource
import com.astro.storm.core.model.VedicChart
import com.astro.storm.ephemeris.kp.KpSystemEngine
import com.astro.storm.ui.theme.AppTheme
import com.astro.storm.ui.viewmodel.KpEventType
import com.astro.storm.ui.viewmodel.KpSystemUiState
import com.astro.storm.ui.viewmodel.KpSystemViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun KpSystemScreen(
    chart: VedicChart?,
    onBack: () -> Unit,
    viewModel: KpSystemViewModel = hiltViewModel()
) {
    if (chart == null) {
        EmptyChartScreen(
            title = stringResource(StringKeyAdvanced.KP_TITLE),
            message = stringResource(StringKey.NO_PROFILE_MESSAGE),
            onBack = onBack
        )
        return
    }

    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(chart) {
        viewModel.calculate(chart)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = stringResource(StringKeyAdvanced.KP_SCREEN_TITLE),
                        fontWeight = FontWeight.SemiBold,
                        color = AppTheme.TextPrimary
                    )
                },
                navigationIcon = {
                    androidx.compose.material3.IconButton(onClick = onBack) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(StringKey.BTN_BACK),
                            tint = AppTheme.TextPrimary
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = AppTheme.ScreenBackground)
            )
        },
        containerColor = AppTheme.ScreenBackground
    ) { padding ->
        when (val state = uiState) {
            is KpSystemUiState.Loading -> {
                LoadingScreen(modifier = Modifier.padding(padding))
            }
            is KpSystemUiState.Error -> {
                ErrorScreen(message = state.message, modifier = Modifier.padding(padding))
            }
            is KpSystemUiState.Success -> {
                KpSystemContent(
                    state = state,
                    onHouseSelected = viewModel::selectHouse,
                    onEventSelected = viewModel::selectEvent,
                    modifier = Modifier.padding(padding)
                )
            }
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun KpSystemContent(
    state: KpSystemUiState.Success,
    onHouseSelected: (Int) -> Unit,
    onEventSelected: (KpEventType) -> Unit,
    modifier: Modifier = Modifier
) {
    val language = LocalLanguage.current
    val cusp = state.cusps.firstOrNull { it.house == state.selectedHouse }

    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            Text(
                text = stringResource(StringKeyAdvanced.KP_SUB_LORD_NAVIGATOR),
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = AppTheme.TextPrimary
            )
        }

        item {
            FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                (1..12).forEach { house ->
                    FilterChip(
                        selected = state.selectedHouse == house,
                        onClick = { onHouseSelected(house) },
                        label = { Text("$house") },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = AppTheme.AccentPrimary.copy(alpha = 0.2f)
                        )
                    )
                }
            }
        }

        item {
            Text(
                text = stringResource(StringKeyAdvanced.KP_EVENT_VERIFICATION),
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = AppTheme.TextPrimary
            )
        }

        item {
            FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                KpEventType.entries.forEach { event ->
                    FilterChip(
                        selected = state.selectedEvent == event,
                        onClick = { onEventSelected(event) },
                        label = { Text(event.label) },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = AppTheme.AccentPrimary.copy(alpha = 0.2f)
                        )
                    )
                }
            }
        }

        cusp?.let { info ->
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = AppTheme.CardBackground),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = "${stringResource(StringKeyAnalysis.HOUSE)} ${info.house}",
                            style = MaterialTheme.typography.titleSmall,
                            color = AppTheme.TextPrimary,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = "Sign: ${info.sign.localizedName()} • Sign Lord: ${info.signLord.localizedName()}",
                            style = MaterialTheme.typography.bodySmall,
                            color = AppTheme.TextSecondary
                        )
                        Text(
                            text = "Star Lord: ${info.starLord.localizedName()} • Sub-Lord: ${info.subLord.localizedName()}",
                            style = MaterialTheme.typography.bodySmall,
                            color = AppTheme.TextSecondary
                        )
                        Text(
                            text = "Nakshatra: ${info.nakshatra.getLocalizedName(language)}",
                            style = MaterialTheme.typography.bodySmall,
                            color = AppTheme.TextSecondary
                        )
                    }
                }
            }
        }

        state.verification?.let { verification ->
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = AppTheme.CardBackground),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            val (icon, color, label) = when (verification.verdict) {
                                KpSystemEngine.KpVerdict.YES -> Triple(Icons.Default.CheckCircle, AppTheme.SuccessColor, "YES")
                                KpSystemEngine.KpVerdict.NO -> Triple(Icons.Default.Close, AppTheme.ErrorColor, "NO")
                                KpSystemEngine.KpVerdict.MIXED -> Triple(Icons.Default.HelpOutline, AppTheme.WarningColor, "MIXED")
                            }
                            Icon(icon, contentDescription = label, tint = color)
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "${stringResource(StringKeyAdvanced.KP_VERDICT)}: $label",
                                color = color,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(start = 8.dp)
                            )
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        verification.reasoning.forEach { line ->
                            Text(text = "• $line", color = AppTheme.TextSecondary, style = MaterialTheme.typography.bodySmall)
                        }
                    }
                }
            }
        }

        item {
            Text(
                text = stringResource(StringKeyAdvanced.KP_SIGNIFICATIONS),
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = AppTheme.TextPrimary
            )
        }

        state.verification?.significations?.let { houses ->
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = AppTheme.CardBackground),
                    shape = RoundedCornerShape(14.dp)
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Text(
                            text = houses.sorted().joinToString(", ") { "${stringResource(StringKeyAnalysis.HOUSE)} $it" },
                            color = AppTheme.TextSecondary,
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun LoadingScreen(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        androidx.compose.material3.CircularProgressIndicator(color = AppTheme.AccentPrimary)
    }
}

@Composable
private fun ErrorScreen(message: String, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(text = message, color = AppTheme.ErrorColor)
    }
}
