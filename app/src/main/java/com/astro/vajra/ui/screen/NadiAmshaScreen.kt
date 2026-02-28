package com.astro.vajra.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import com.astro.vajra.ui.components.common.NeoVedicPageHeader
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
import com.astro.vajra.data.localization.LocalLanguage
import com.astro.vajra.core.common.StringKey
import com.astro.vajra.core.common.StringKeyAdvanced
import com.astro.vajra.core.common.getLocalizedName
import com.astro.vajra.data.localization.stringResource
import com.astro.vajra.core.model.VedicChart
import com.astro.vajra.ephemeris.NadiAmshaCalculator
import com.astro.vajra.ephemeris.NadiAmshaCalculator.NadiAmshaResult
import com.astro.vajra.ui.components.common.ModernPillTabRow
import com.astro.vajra.ui.components.common.TabItem
import com.astro.vajra.ui.components.common.NeoVedicEmptyState
import com.astro.vajra.ui.theme.AppTheme
import com.astro.vajra.ui.theme.NeoVedicTokens
import com.astro.vajra.ui.theme.SpaceGroteskFamily
import com.astro.vajra.ui.theme.CinzelDecorativeFamily
import com.astro.vajra.ui.theme.PoppinsFontFamily
import com.astro.vajra.ui.viewmodel.NadiAmshaUiState
import com.astro.vajra.ui.viewmodel.NadiAmshaViewModel
import kotlinx.coroutines.delay
import java.time.format.DateTimeFormatter
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.foundation.BorderStroke

private val nadiTimeFormatter: DateTimeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss")

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
    val language = LocalLanguage.current
    var showInfoDialog by remember { mutableStateOf(false) }

    LaunchedEffect(chart) {
        viewModel.calculateNadiAmsha(chart)
    }

    Scaffold(
        topBar = {
            NeoVedicPageHeader(
                title = stringResource(StringKeyAdvanced.NADI_TITLE),
                subtitle = stringResource(StringKeyAdvanced.NADI_SUBTITLE),
                onBack = onBack,
                actions = {
                    IconButton(onClick = { showInfoDialog = true }) {
                        Icon(
                            Icons.Outlined.Info,
                            contentDescription = stringResource(StringKey.BTN_DETAILS),
                            tint = AppTheme.TextSecondary
                        )
                    }
                }
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
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        NeoVedicEmptyState(
                            title = stringResource(StringKeyAdvanced.NADI_TITLE),
                            subtitle = state.message,
                            icon = Icons.Filled.ErrorOutline
                        )
                    }
                }
                is NadiAmshaUiState.Success -> {
                    NadiAmshaContent(
                        result = state.result,
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
            title = {
                Text(
                    stringResource(StringKeyAdvanced.NADI_INFO_TITLE),
                    fontFamily = CinzelDecorativeFamily,
                    fontWeight = FontWeight.Bold,
                    color = AppTheme.TextPrimary,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            },
            text = { 
                Text(
                    stringResource(StringKeyAdvanced.NADI_INFO_DESC),
                    fontFamily = PoppinsFontFamily,
                    color = AppTheme.TextSecondary,
                    lineHeight = 22.sp
                ) 
            },
            confirmButton = {
                TextButton(onClick = { showInfoDialog = false }) {
                    Text(
                        stringResource(StringKey.BTN_CLOSE),
                        fontFamily = SpaceGroteskFamily,
                        color = AppTheme.AccentPrimary
                    )
                }
            },
            containerColor = AppTheme.CardBackground,
            shape = RoundedCornerShape(NeoVedicTokens.CardCornerRadius)
        )
    }
}

@Composable
private fun NadiAmshaContent(
    result: NadiAmshaResult,
    selectedTab: Int,
    onTabSelected: (Int) -> Unit
) {
    val tabs = listOf(
        TabItem(stringResource(StringKeyAdvanced.NADI_TAB_OVERVIEW), AppTheme.AccentGold),
        TabItem(stringResource(StringKeyAdvanced.NADI_TAB_POSITIONS), AppTheme.AccentTeal),
        TabItem(stringResource(StringKeyAdvanced.NADI_TAB_RECTIFICATION), AppTheme.AccentPrimary)
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(AppTheme.ScreenBackground)
    ) {
        ModernPillTabRow(
            tabs = tabs,
            selectedIndex = selectedTab,
            onTabSelected = onTabSelected,
            modifier = Modifier.padding(horizontal = NeoVedicTokens.ScreenPadding, vertical = NeoVedicTokens.SpaceXS)
        )

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            when (selectedTab) {
                0 -> {
                    item { NadiLagnaCard(result.ascendantNadi) }
                    item { NadiSummaryCard(result) }
                }
                1 -> {
                    items(result.planetNadis) { position ->
                        NadiPlanetItem(position)
                    }
                }
                2 -> {
                    item { RectificationHeader(result.chart) }
                    items(result.rectificationCandidates) { candidate ->
                        RectificationCandidateCard(candidate)
                    }
                }
            }
        }
    }
}

@Composable
private fun NadiLagnaCard(nadi: NadiAmshaCalculator.NadiPosition) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = AppTheme.AccentPrimary.copy(alpha = 0.1f),
        shape = RoundedCornerShape(NeoVedicTokens.CardCornerRadius),
        border = BorderStroke(NeoVedicTokens.ThinBorderWidth, AppTheme.AccentPrimary.copy(alpha = 0.2f))
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
                        stringResource(StringKeyAdvanced.NADI_LORD),
                        fontFamily = SpaceGroteskFamily,
                        fontSize = com.astro.vajra.ui.theme.NeoVedicFontSizes.S12,
                        color = AppTheme.TextMuted
                    )
                    Text(
                        nadi.nadiLord.getLocalizedName(LocalLanguage.current),
                        fontFamily = CinzelDecorativeFamily,
                        fontSize = com.astro.vajra.ui.theme.NeoVedicFontSizes.S20,
                        fontWeight = FontWeight.Bold,
                        color = AppTheme.TextPrimary
                    )
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                NadiInfoItem(stringResource(StringKeyAdvanced.NADI_NUMBER), "#${nadi.nadiNumber}")
                NadiInfoItem(stringResource(StringKeyAdvanced.KAKSHYA_SIGN), nadi.nadiSign.getLocalizedName(LocalLanguage.current))
            }
        }
    }
}

@Composable
private fun NadiInfoItem(label: String, value: String) {
    Column {
        Text(
            label,
            fontFamily = PoppinsFontFamily,
            fontSize = com.astro.vajra.ui.theme.NeoVedicFontSizes.S11,
            color = AppTheme.TextMuted
        )
        Text(
            value,
            fontFamily = SpaceGroteskFamily,
            fontSize = com.astro.vajra.ui.theme.NeoVedicFontSizes.S14,
            fontWeight = FontWeight.SemiBold,
            color = AppTheme.TextPrimary
        )
    }
}

@Composable
private fun NadiSummaryCard(result: NadiAmshaResult) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = AppTheme.CardBackground,
        shape = RoundedCornerShape(NeoVedicTokens.CardCornerRadius),
        border = BorderStroke(NeoVedicTokens.BorderWidth, AppTheme.BorderColor)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                stringResource(StringKeyAdvanced.NADI_TAB_OVERVIEW),
                fontFamily = CinzelDecorativeFamily,
                fontWeight = FontWeight.SemiBold,
                color = AppTheme.TextPrimary,
                fontSize = com.astro.vajra.ui.theme.NeoVedicFontSizes.S16
            )
            Spacer(modifier = Modifier.height(8.dp))
            // Example summary logic
            Text(
                stringResource(
                    StringKeyAdvanced.NADI_SUMMARY_FMT,
                    result.ascendantNadi.nadiLord.getLocalizedName(LocalLanguage.current),
                    result.ascendantNadi.energyType.displayName
                ),
                fontFamily = PoppinsFontFamily,
                color = AppTheme.TextSecondary,
                fontSize = com.astro.vajra.ui.theme.NeoVedicFontSizes.S14,
                lineHeight = 20.sp
            )
        }
    }
}

@Composable
private fun NadiPlanetItem(position: NadiAmshaCalculator.NadiPosition) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = AppTheme.CardBackground,
        shape = RoundedCornerShape(NeoVedicTokens.CardCornerRadius),
        border = BorderStroke(NeoVedicTokens.BorderWidth, AppTheme.BorderColor)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(RoundedCornerShape(NeoVedicTokens.ElementCornerRadius))
                    .background(AppTheme.getPlanetColor(position.planet!!).copy(alpha = 0.2f)),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    position.planet.symbol,
                    fontFamily = CinzelDecorativeFamily,
                    fontSize = com.astro.vajra.ui.theme.NeoVedicFontSizes.S20,
                    color = AppTheme.getPlanetColor(position.planet)
                )
            }
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    position.planet.getLocalizedName(LocalLanguage.current),
                    fontFamily = PoppinsFontFamily,
                    fontWeight = FontWeight.Bold,
                    color = AppTheme.TextPrimary
                )
                Text(
                    stringResource(
                        StringKeyAdvanced.NADI_PLANET_FMT,
                        position.nadiNumber,
                        position.nadiSign.getLocalizedName(LocalLanguage.current)
                    ),
                    fontFamily = SpaceGroteskFamily,
                    fontSize = com.astro.vajra.ui.theme.NeoVedicFontSizes.S12,
                    color = AppTheme.TextSecondary
                )
            }
            Text(
                position.nadiLord.getLocalizedName(LocalLanguage.current),
                fontFamily = PoppinsFontFamily,
                fontSize = com.astro.vajra.ui.theme.NeoVedicFontSizes.S13,
                color = AppTheme.TextMuted,
                fontWeight = FontWeight.Medium
            )
        }
    }
}

@Composable
private fun RectificationHeader(chart: VedicChart) {
    Column {
        Text(
            stringResource(StringKeyAdvanced.NADI_RECTIFICATION_TITLE),
            fontFamily = CinzelDecorativeFamily,
            fontWeight = FontWeight.Bold,
            fontSize = com.astro.vajra.ui.theme.NeoVedicFontSizes.S16,
            color = AppTheme.TextPrimary
        )
        Text(
            "${stringResource(StringKeyAdvanced.NADI_CURRENT_TIME)}: ${chart.birthData.dateTime.format(nadiTimeFormatter)}",
            fontFamily = SpaceGroteskFamily,
            fontSize = com.astro.vajra.ui.theme.NeoVedicFontSizes.S12,
            color = AppTheme.TextMuted
        )
        Spacer(modifier = Modifier.height(16.dp))
    }
}

@Composable
private fun RectificationCandidateCard(candidate: NadiAmshaCalculator.RectificationCandidate) {
    Surface(
        modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
        color = AppTheme.CardBackground,
        shape = RoundedCornerShape(NeoVedicTokens.CardCornerRadius),
        border = BorderStroke(NeoVedicTokens.BorderWidth, AppTheme.BorderColor)
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
                    "${sign}${candidate.timeAdjustmentMinutes} ${stringResource(StringKeyAdvanced.MIN_LABEL)}  (${candidate.adjustedTime.format(nadiTimeFormatter)})",
                    fontFamily = SpaceGroteskFamily,
                    fontWeight = FontWeight.Bold,
                    color = AppTheme.TextPrimary
                )
                Text(
                    candidate.description,
                    fontFamily = PoppinsFontFamily,
                    fontSize = com.astro.vajra.ui.theme.NeoVedicFontSizes.S12,
                    color = AppTheme.TextSecondary
                )
            }
            Surface(
                color = AppTheme.AccentPrimary.copy(alpha = 0.1f),
                shape = RoundedCornerShape(NeoVedicTokens.ElementCornerRadius)
            ) {
                Text(
                    "${candidate.confidence}%",
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                    fontFamily = SpaceGroteskFamily,
                    fontSize = com.astro.vajra.ui.theme.NeoVedicFontSizes.S11,
                    fontWeight = FontWeight.Bold,
                    color = AppTheme.AccentPrimary
                )
            }
        }
    }
}