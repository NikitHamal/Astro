package com.astro.storm.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.astro.storm.data.localization.LocalLanguage
import com.astro.storm.core.common.StringKey
import com.astro.storm.core.common.StringKeyAdvanced
import com.astro.storm.core.common.getLocalizedName
import com.astro.storm.data.localization.stringResource
import com.astro.storm.core.model.VedicChart
import com.astro.storm.ephemeris.NadiAmshaCalculator
import com.astro.storm.ephemeris.NadiAmshaCalculator.NadiAmshaResult
import com.astro.storm.ui.components.common.ModernPillTabRow
import com.astro.storm.ui.components.common.TabItem
import com.astro.storm.ui.theme.AppTheme
import com.astro.storm.ui.viewmodel.NadiAmshaUiState
import com.astro.storm.ui.viewmodel.NadiAmshaViewModel
import kotlinx.coroutines.delay
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NadiAmshaScreen(
    chart: VedicChart?,
    onBack: () -> Unit,
    viewModel: NadiAmshaViewModel = hiltViewModel()
) {
    if (chart == null) {
        EmptyChartScreen(
            title = stringResource(StringKeyAdvanced.NADI_TITLE),
            message = stringResource(StringKey.NO_PROFILE_MESSAGE),
            onBack = onBack
        )
        return
    }

    val uiState by viewModel.uiState.collectAsState()
    val selectedTab by viewModel.selectedTab.collectAsState()
    val nadiAnalysis by viewModel.nadiAnalysis.collectAsState()
    val language = LocalLanguage.current
    var showInfoDialog by remember { mutableStateOf(false) }

    LaunchedEffect(chart) {
        viewModel.calculateNadiAmsha(chart)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(
                            stringResource(StringKeyAdvanced.NADI_TITLE),
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            stringResource(StringKeyAdvanced.NADI_SUBTITLE),
                            fontSize = 12.sp,
                            color = AppTheme.TextMuted
                        )
                    }
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(StringKey.BTN_BACK),
                            tint = AppTheme.TextPrimary
                        )
                    }
                },
                actions = {
                    IconButton(onClick = { showInfoDialog = true }) {
                        Icon(
                            Icons.Outlined.Info,
                            contentDescription = stringResource(StringKey.BTN_DETAILS),
                            tint = AppTheme.TextSecondary
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = AppTheme.ScreenBackground,
                    titleContentColor = AppTheme.TextPrimary
                )
            )
        },
        containerColor = AppTheme.ScreenBackground
    ) { paddingValues ->
        Box(modifier = Modifier.padding(paddingValues)) {
            when (val state = uiState) {
                is NadiAmshaUiState.Loading -> {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator(color = AppTheme.AccentPrimary)
                    }
                }
                is NadiAmshaUiState.Error -> {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text(state.message, color = AppTheme.ErrorColor)
                    }
                }
                is NadiAmshaUiState.Success -> {
                    NadiAmshaContent(
                        result = state.result,
                        analysis = nadiAnalysis,
                        selectedTab = selectedTab,
                        onTabSelected = { viewModel.setSelectedTab(it) }
                    )
                }
            }
        }
    }

    if (showInfoDialog) {
        AlertDialog(
            onDismissRequest = { showInfoDialog = false },
            title = { Text(stringResource(StringKeyAdvanced.NADI_INFO_TITLE)) },
            text = { Text(stringResource(StringKeyAdvanced.NADI_INFO_DESC)) },
            confirmButton = {
                TextButton(onClick = { showInfoDialog = false }) {
                    Text(stringResource(StringKey.BTN_CLOSE))
                }
            },
            containerColor = AppTheme.CardBackground
        )
    }
}

@Composable
private fun NadiAmshaContent(
    result: NadiAmshaResult,
    analysis: com.astro.storm.ephemeris.nadi.NadiAmshaAnalyzer.NadiAnalysis?,
    selectedTab: Int,
    onTabSelected: (Int) -> Unit
) {
    val tabs = listOf(
        TabItem(stringResource(StringKeyAdvanced.NADI_TAB_OVERVIEW), Icons.Filled.Dashboard),
        TabItem(stringResource(StringKeyAdvanced.NADI_TAB_POSITIONS), Icons.Filled.List),
        TabItem(stringResource(StringKeyAdvanced.NADI_TAB_RECTIFICATION), Icons.Filled.Build)
    )

    Column(modifier = Modifier.fillMaxSize()) {
        ModernPillTabRow(
            tabs = tabs,
            selectedIndex = selectedTab,
            onTabSelected = onTabSelected,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
        )

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            when (selectedTab) {
                0 -> {
                    item { NadiLagnaCard(result.ascendantNadi, analysis) }
                    item { NadiSummaryCard(result, analysis) }
                }
                1 -> {
                    items(result.planetNadis) { position ->
                        NadiPlanetItem(position)
                    }
                }
                2 -> {
                    item { RectificationHeader(result.chart, analysis) }
                    items(result.rectificationCandidates) { candidate ->
                        RectificationCandidateCard(candidate)
                    }
                }
            }
        }
    }
}

@Composable
private fun NadiLagnaCard(
    nadi: NadiAmshaCalculator.NadiPosition,
    analysis: com.astro.storm.ephemeris.nadi.NadiAmshaAnalyzer.NadiAnalysis?
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = AppTheme.AccentPrimary.copy(alpha = 0.1f)),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    Icons.Filled.AccountBalance,
                    contentDescription = null,
                    tint = AppTheme.AccentPrimary,
                    modifier = Modifier.size(32.dp)
                )
                Spacer(modifier = Modifier.width(16.dp))
                Column {
                    Text(
                        stringResource(StringKeyAdvanced.NADI_NAME),
                        fontSize = 12.sp,
                        color = AppTheme.TextMuted
                    )
                    Text(
                        analysis?.nadiName ?: "Nadi #${nadi.nadiNumber}",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = AppTheme.TextPrimary
                    )
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                NadiInfoItem(stringResource(StringKeyAdvanced.NADI_NUMBER), "#${nadi.nadiNumber}")
                NadiInfoItem(stringResource(StringKeyAdvanced.NADI_LORD), nadi.nadiLord.getLocalizedName(LocalLanguage.current))
            }
        }
    }
}

@Composable
private fun NadiInfoItem(label: String, value: String) {
    Column {
        Text(label, fontSize = 11.sp, color = AppTheme.TextMuted)
        Text(value, fontSize = 14.sp, fontWeight = FontWeight.SemiBold, color = AppTheme.TextPrimary)
    }
}

@Composable
private fun NadiSummaryCard(
    result: NadiAmshaResult,
    analysis: com.astro.storm.ephemeris.nadi.NadiAmshaAnalyzer.NadiAnalysis?
) {
    val language = LocalLanguage.current
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = AppTheme.CardBackground),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                stringResource(StringKeyAdvanced.NADI_TAB_OVERVIEW),
                fontWeight = FontWeight.SemiBold,
                color = AppTheme.TextPrimary
            )
            Spacer(modifier = Modifier.height(8.dp))
            
            val prediction = if (language == com.astro.storm.core.common.Language.NEPALI) {
                analysis?.predictionNe
            } else {
                analysis?.predictionEn
            }
            
            Text(
                prediction ?: "Ascendant Nadi is ruled by ${result.ascendantNadi.nadiLord.getLocalizedName(language)}. " +
                "This indicates ${result.ascendantNadi.energyType.displayName} dominance in the chart's finest division.",
                color = AppTheme.TextSecondary,
                fontSize = 14.sp,
                lineHeight = 20.sp
            )
        }
    }
}

@Composable
private fun NadiPlanetItem(position: NadiAmshaCalculator.NadiPosition) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = AppTheme.CardBackground),
        shape = RoundedCornerShape(12.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .background(AppTheme.getPlanetColor(position.planet!!).copy(alpha = 0.2f), RoundedCornerShape(8.dp)),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    position.planet.symbol,
                    fontSize = 20.sp,
                    color = AppTheme.getPlanetColor(position.planet)
                )
            }
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    position.planet.getLocalizedName(LocalLanguage.current),
                    fontWeight = FontWeight.Bold,
                    color = AppTheme.TextPrimary
                )
                Text(
                    "Nadi #${position.nadiNumber} (${position.nadiSign.getLocalizedName(LocalLanguage.current)})",
                    fontSize = 12.sp,
                    color = AppTheme.TextSecondary
                )
            }
            Text(
                position.nadiLord.getLocalizedName(LocalLanguage.current),
                fontSize = 13.sp,
                color = AppTheme.TextMuted,
                fontWeight = FontWeight.Medium
            )
        }
    }
}

@Composable
private fun RectificationHeader(
    chart: VedicChart,
    analysis: com.astro.storm.ephemeris.nadi.NadiAmshaAnalyzer.NadiAnalysis?
) {
    val language = LocalLanguage.current
    Column {
        Text(
            stringResource(StringKeyAdvanced.NADI_RECTIFICATION_TITLE),
            fontWeight = FontWeight.Bold,
            fontSize = 16.sp,
            color = AppTheme.TextPrimary
        )
        Text(
            "${stringResource(StringKeyAdvanced.NADI_CURRENT_TIME)}: ${chart.birthData.dateTime.format(DateTimeFormatter.ofPattern("HH:mm:ss"))}",
            fontSize = 12.sp,
            color = AppTheme.TextMuted
        )
        Spacer(modifier = Modifier.height(12.dp))
        
        Surface(
            color = AppTheme.AccentPrimary.copy(alpha = 0.05f),
            shape = RoundedCornerShape(8.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(modifier = Modifier.padding(12.dp), verticalAlignment = Alignment.Top) {
                Icon(
                    Icons.Filled.History,
                    contentDescription = null,
                    tint = AppTheme.AccentPrimary,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(12.dp))
                val advice = if (language == com.astro.storm.core.common.Language.NEPALI) {
                    analysis?.rectificationAdviceNe
                } else {
                    analysis?.rectificationAdviceEn
                }
                Text(
                    advice ?: stringResource(StringKeyAdvanced.NADI_RECTIFICATION_DESC),
                    fontSize = 13.sp,
                    color = AppTheme.TextPrimary,
                    lineHeight = 18.sp
                )
            }
        }
        
        Spacer(modifier = Modifier.height(16.dp))
    }
}

@Composable
private fun RectificationCandidateCard(candidate: NadiAmshaCalculator.RectificationCandidate) {
    Card(
        modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
        colors = CardDefaults.cardColors(containerColor = AppTheme.CardBackground),
        shape = RoundedCornerShape(12.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                Icons.Filled.Schedule,
                contentDescription = null,
                tint = if (candidate.timeAdjustmentMinutes < 0) AppTheme.WarningColor else AppTheme.SuccessColor,
                modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                val sign = if(candidate.timeAdjustmentMinutes > 0) "+" else ""
                Text(
                    "${sign}${candidate.timeAdjustmentMinutes} min  (${candidate.adjustedTime.format(DateTimeFormatter.ofPattern("HH:mm:ss"))})",
                    fontWeight = FontWeight.Bold,
                    color = AppTheme.TextPrimary
                )
                Text(
                    candidate.description,
                    fontSize = 12.sp,
                    color = AppTheme.TextSecondary
                )
            }
            Surface(
                color = AppTheme.AccentPrimary.copy(alpha = 0.1f),
                shape = RoundedCornerShape(4.dp)
            ) {
                Text(
                    "${candidate.confidence}%",
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = AppTheme.AccentPrimary
                )
            }
        }
    }
}

