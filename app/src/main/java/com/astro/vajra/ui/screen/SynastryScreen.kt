package com.astro.vajra.ui.screen

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import com.astro.vajra.ui.components.common.NeoVedicPageHeader
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.astro.vajra.core.common.Language
import com.astro.vajra.core.common.StringKey
import com.astro.vajra.core.common.StringKeyDosha
import com.astro.vajra.core.common.StringKeyMatch
import com.astro.vajra.data.localization.currentLanguage
import com.astro.vajra.data.localization.stringResource
import com.astro.vajra.core.model.Planet
import com.astro.vajra.core.model.VedicChart
import com.astro.vajra.core.model.ZodiacSign
import com.astro.vajra.data.repository.SavedChart
import com.astro.vajra.ephemeris.synastry.AspectNature
import com.astro.vajra.ephemeris.synastry.CompatibilityCategory
import com.astro.vajra.ephemeris.synastry.HouseOverlay
import com.astro.vajra.ephemeris.synastry.SynastryAnalysisResult
import com.astro.vajra.ephemeris.synastry.SynastryAspect
import com.astro.vajra.ephemeris.synastry.SynastryCalculator
import com.astro.vajra.ui.theme.AppTheme
import com.astro.vajra.ui.viewmodel.ChartViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext

import com.astro.vajra.ui.theme.CinzelDecorativeFamily
import com.astro.vajra.ui.theme.PoppinsFontFamily
import com.astro.vajra.ui.theme.SpaceGroteskFamily
import com.astro.vajra.ui.theme.NeoVedicTokens
import com.astro.vajra.ui.components.common.vedicCornerMarkers

/**
 * Synastry/Chart Comparison Screen
 *
 * Advanced Vedic astrology synastry analysis comparing two birth charts to analyze:
 * - Inter-chart aspects between planets
 * - House overlay influences
 * - Overall relationship compatibility
 * - Key synastry indicators (Sun-Moon, Venus-Mars, Ascendant connections)
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SynastryScreen(
    savedCharts: List<SavedChart>,
    viewModel: ChartViewModel,
    onBack: () -> Unit
) {
    val haptic = LocalHapticFeedback.current
    val language = currentLanguage()

    var selectedChart1Id by remember { mutableStateOf<Long?>(null) }
    var selectedChart2Id by remember { mutableStateOf<Long?>(null) }
    var chart1 by remember { mutableStateOf<VedicChart?>(null) }
    var chart2 by remember { mutableStateOf<VedicChart?>(null) }

    var synastryResult by remember { mutableStateOf<SynastryAnalysisResult?>(null) }
    var isCalculating by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    var showChart1Selector by remember { mutableStateOf(false) }
    var showChart2Selector by remember { mutableStateOf(false) }
    var showInfoDialog by remember { mutableStateOf(false) }

    var selectedTab by remember { mutableIntStateOf(0) }
    val tabs = listOf(
        stringResource(StringKeyDosha.SYNASTRY_OVERVIEW),
        stringResource(StringKeyDosha.SYNASTRY_ASPECTS),
        stringResource(StringKeyDosha.SYNASTRY_HOUSES),
        stringResource(StringKeyDosha.SYNASTRY_COMPATIBILITY)
    )

    val animatedProgress by animateFloatAsState(
        targetValue = (synastryResult?.overallCompatibility?.div(100.0)?.toFloat() ?: 0f),
        animationSpec = tween(durationMillis = 1200, easing = FastOutSlowInEasing),
        label = "progress"
    )

    // Load charts when selected
    LaunchedEffect(selectedChart1Id) {
        selectedChart1Id?.let { id ->
            chart1 = withContext(Dispatchers.IO) { viewModel.getChartById(id) }
        } ?: run { chart1 = null }
    }

    LaunchedEffect(selectedChart2Id) {
        selectedChart2Id?.let { id ->
            chart2 = withContext(Dispatchers.IO) { viewModel.getChartById(id) }
        } ?: run { chart2 = null }
    }

    // Calculate synastry when both charts are selected
    LaunchedEffect(chart1, chart2) {
        if (chart1 != null && chart2 != null) {
            isCalculating = true
            errorMessage = null
            delay(300)
            try {
                synastryResult = withContext(Dispatchers.Default) {
                    SynastryCalculator.calculate(
                        chart1 = chart1!!,
                        chart2 = chart2!!,
                        language = language
                    )
                }
            } catch (e: Exception) {
                errorMessage = e.message ?: com.astro.vajra.core.common.StringResources.get(StringKeyDosha.SYNASTRY_CALC_FAILED, language)
            }
            isCalculating = false
        } else {
            synastryResult = null
        }
    }

    if (showInfoDialog) {
        SynastryInfoDialog(onDismiss = { showInfoDialog = false })
    }

    Scaffold(
        containerColor = AppTheme.ScreenBackground,
        topBar = {
            NeoVedicPageHeader(
                title = stringResource(StringKeyDosha.SYNASTRY_TITLE),
                subtitle = stringResource(StringKeyDosha.SYNASTRY_SUBTITLE),
                onBack = onBack,
                actions = {
                    IconButton(onClick = { showInfoDialog = true }) {
                        Icon(
                            Icons.Outlined.Info,
                            contentDescription = stringResource(StringKeyDosha.SYNASTRY_INFO_TITLE),
                            tint = AppTheme.TextPrimary
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(AppTheme.ScreenBackground),
            contentPadding = PaddingValues(bottom = 32.dp)
        ) {
            // Chart Selection Section
            item {
                ChartSelectionSection(
                    savedCharts = savedCharts,
                    selectedChart1Id = selectedChart1Id,
                    selectedChart2Id = selectedChart2Id,
                    chart1 = chart1,
                    chart2 = chart2,
                    onSelectChart1 = { showChart1Selector = true },
                    onSelectChart2 = { showChart2Selector = true },
                    onSwapCharts = {
                        haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                        val temp = selectedChart1Id
                        selectedChart1Id = selectedChart2Id
                        selectedChart2Id = temp
                    },
                    onClearSelection = {
                        selectedChart1Id = null
                        selectedChart2Id = null
                    }
                )
            }

            // Loading state
            if (isCalculating) {
                item { SynastryCalculatingState() }
            }

            // Error state
            errorMessage?.let { error ->
                item {
                    SynastryErrorCard(
                        error = error,
                        onRetry = {
                            errorMessage = null
                            // Trigger recalculation
                            val temp1 = chart1
                            val temp2 = chart2
                            chart1 = null
                            chart2 = null
                            chart1 = temp1
                            chart2 = temp2
                        }
                    )
                }
            }

            // Results
            synastryResult?.let { result ->
                // Tab selector
                item {
                    SynastryTabSelector(
                        tabs = tabs,
                        selectedTab = selectedTab,
                        onTabSelected = { selectedTab = it }
                    )
                }

                // Tab content
                when (selectedTab) {
                    0 -> item {
                        SynastryOverviewTab(
                            result = result,
                            chart1 = chart1!!,
                            chart2 = chart2!!,
                            animatedProgress = animatedProgress
                        )
                    }
                    1 -> item { SynastryAspectsTab(result = result) }
                    2 -> item { SynastryHouseOverlaysTab(result = result, chart1 = chart1!!, chart2 = chart2!!) }
                    3 -> item { SynastryCompatibilityTab(result = result) }
                }
            }
        }
    }

    // Chart selector bottom sheets
    if (showChart1Selector) {
        ChartSelectorBottomSheet(
            title = stringResource(StringKeyDosha.SYNASTRY_CHART_1),
            icon = Icons.Filled.Person,
            accentColor = AppTheme.AccentPrimary,
            charts = savedCharts,
            selectedId = selectedChart1Id,
            excludeId = selectedChart2Id,
            onSelect = { id ->
                haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                selectedChart1Id = id
                showChart1Selector = false
            },
            onDismiss = { showChart1Selector = false }
        )
    }

    if (showChart2Selector) {
        ChartSelectorBottomSheet(
            title = stringResource(StringKeyDosha.SYNASTRY_CHART_2),
            icon = Icons.Filled.PersonOutline,
            accentColor = AppTheme.AccentTeal,
            charts = savedCharts,
            selectedId = selectedChart2Id,
            excludeId = selectedChart1Id,
            onSelect = { id ->
                haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                selectedChart2Id = id
                showChart2Selector = false
            },
            onDismiss = { showChart2Selector = false }
        )
    }
}

// ============================================
// UI Components
// ============================================

@Composable
private fun ChartSelectionSection(
    savedCharts: List<SavedChart>,
    selectedChart1Id: Long?,
    selectedChart2Id: Long?,
    chart1: VedicChart?,
    chart2: VedicChart?,
    onSelectChart1: () -> Unit,
    onSelectChart2: () -> Unit,
    onSwapCharts: () -> Unit,
    onClearSelection: () -> Unit
) {
    val hasSelection = chart1 != null || chart2 != null

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        color = AppTheme.CardBackground,
        border = androidx.compose.foundation.BorderStroke(1.dp, AppTheme.BorderColor),
        shape = RoundedCornerShape(com.astro.vajra.ui.theme.NeoVedicTokens.ElementCornerRadius)
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    stringResource(StringKeyDosha.SYNASTRY_SELECT_CHARTS),
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = AppTheme.TextPrimary
                )

                AnimatedVisibility(visible = hasSelection) {
                    Row {
                        if (chart1 != null && chart2 != null) {
                            IconButton(
                                onClick = onSwapCharts,
                                modifier = Modifier.size(36.dp)
                            ) {
                                Icon(
                                    Icons.Filled.SwapHoriz,
                                    contentDescription = stringResource(StringKeyDosha.SYNASTRY_SWAP),
                                    tint = AppTheme.AccentPrimary,
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                        }
                        IconButton(
                            onClick = onClearSelection,
                            modifier = Modifier.size(36.dp)
                        ) {
                            Icon(
                                Icons.Filled.Close,
                                contentDescription = stringResource(StringKeyDosha.SYNASTRY_CLEAR),
                                tint = AppTheme.TextMuted,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                SynastryChartCard(
                    label = stringResource(StringKeyDosha.SYNASTRY_CHART_1),
                    chart = chart1,
                    icon = Icons.Filled.Person,
                    color = AppTheme.AccentPrimary,
                    onClick = onSelectChart1,
                    modifier = Modifier.weight(1f)
                )

                Box(
                    modifier = Modifier
                        .padding(horizontal = 8.dp)
                        .size(40.dp)
                        .clip(CircleShape)
                        .background(
                            if (chart1 != null && chart2 != null)
                                AppTheme.SuccessColor.copy(alpha = 0.15f)
                            else AppTheme.ChipBackground
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        if (chart1 != null && chart2 != null) Icons.Filled.CompareArrows else Icons.Outlined.Compare,
                        contentDescription = null,
                        tint = if (chart1 != null && chart2 != null) AppTheme.SuccessColor else AppTheme.TextSubtle,
                        modifier = Modifier.size(22.dp)
                    )
                }

                SynastryChartCard(
                    label = stringResource(StringKeyDosha.SYNASTRY_CHART_2),
                    chart = chart2,
                    icon = Icons.Filled.PersonOutline,
                    color = AppTheme.AccentTeal,
                    onClick = onSelectChart2,
                    modifier = Modifier.weight(1f)
                )
            }

            if (savedCharts.isEmpty()) {
                Spacer(modifier = Modifier.height(16.dp))
                Surface(
                    color = AppTheme.InfoColor.copy(alpha = 0.1f),
                    shape = RoundedCornerShape(com.astro.vajra.ui.theme.NeoVedicTokens.ElementCornerRadius),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            Icons.Outlined.Info,
                            contentDescription = null,
                            tint = AppTheme.InfoColor,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            stringResource(StringKeyMatch.MATCH_CREATE_CHARTS_FIRST),
                            style = MaterialTheme.typography.bodySmall,
                            color = AppTheme.InfoColor
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun SynastryChartCard(
    label: String,
    chart: VedicChart?,
    icon: ImageVector,
    color: Color,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val scale by animateFloatAsState(
        targetValue = if (chart != null) 1f else 0.98f,
        animationSpec = spring(dampingRatio = 0.8f),
        label = "scale"
    )

    Surface(
        modifier = modifier
            .scale(scale)
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick = onClick
            ),
        color = if (chart != null) color.copy(alpha = 0.08f) else AppTheme.ChipBackground,
        shape = RoundedCornerShape(NeoVedicTokens.ElementCornerRadius),
        border = if (chart != null)
            androidx.compose.foundation.BorderStroke(1.5.dp, color.copy(alpha = 0.3f))
        else null
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .size(52.dp)
                    .clip(CircleShape)
                    .background(if (chart != null) color.copy(alpha = 0.15f) else AppTheme.BorderColor),
                contentAlignment = Alignment.Center
            ) {
                if (chart != null) {
                    Text(
                        chart.birthData.name.firstOrNull()?.uppercase() ?: "?",
                        fontSize = 24.sp,
                        fontFamily = CinzelDecorativeFamily,
                        fontWeight = FontWeight.Bold,
                        color = color
                    )
                } else {
                    Icon(
                        Icons.Filled.Add,
                        contentDescription = null,
                        tint = AppTheme.TextSubtle,
                        modifier = Modifier.size(24.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                chart?.birthData?.name ?: label,
                fontSize = 14.sp,
                fontFamily = PoppinsFontFamily,
                fontWeight = if (chart != null) FontWeight.SemiBold else FontWeight.Normal,
                color = if (chart != null) AppTheme.TextPrimary else AppTheme.TextMuted,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                textAlign = TextAlign.Center
            )

            if (chart != null) {
                Text(
                    ZodiacSign.fromLongitude(chart.ascendant).displayName,
                    fontSize = 12.sp,
                    fontFamily = SpaceGroteskFamily,
                    color = AppTheme.TextSubtle
                )
            } else {
                Text(
                    stringResource(StringKeyMatch.MATCH_TAP_TO_SELECT),
                    fontSize = 12.sp,
                    fontFamily = SpaceGroteskFamily,
                    color = AppTheme.TextSubtle
                )
            }
        }
    }
}

@Composable
private fun SynastryTabSelector(
    tabs: List<String>,
    selectedTab: Int,
    onTabSelected: (Int) -> Unit
) {
    LazyRow(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(tabs.size) { index ->
            val isSelected = selectedTab == index
            com.astro.vajra.ui.components.common.NeoVedicChoicePill(
                selected = isSelected,
                onClick = { onTabSelected(index) },
                label = {
                    Text(
                        tabs[index],
                        fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal
                    )
                },
                colors = FilterChipDefaults.filterChipColors(
                    selectedContainerColor = AppTheme.AccentPrimary.copy(alpha = 0.15f),
                    selectedLabelColor = AppTheme.AccentPrimary,
                    containerColor = AppTheme.ChipBackground,
                    labelColor = AppTheme.TextSecondary
                )
            )
        }
    }
}

@Composable
private fun SynastryOverviewTab(
    result: SynastryAnalysisResult,
    chart1: VedicChart,
    chart2: VedicChart,
    animatedProgress: Float
) {
    Column(modifier = Modifier.padding(16.dp)) {
        // Overall Score Card
        Surface(
            modifier = Modifier.fillMaxWidth().vedicCornerMarkers(color = getCompatibilityColor(result.overallCompatibility), strokeWidth = 1.dp),
            color = AppTheme.CardBackground,
            border = androidx.compose.foundation.BorderStroke(1.dp, AppTheme.BorderColor),
            shape = RoundedCornerShape(NeoVedicTokens.ElementCornerRadius)
        ) {
            Column(
                modifier = Modifier.padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    stringResource(StringKeyDosha.SYNASTRY_OVERALL_SCORE),
                    fontSize = 18.sp,
                    fontFamily = CinzelDecorativeFamily,
                    fontWeight = FontWeight.SemiBold,
                    color = AppTheme.TextPrimary
                )

                Spacer(modifier = Modifier.height(20.dp))

                Box(
                    modifier = Modifier.size(140.dp),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(
                        progress = { 1f },
                        modifier = Modifier.fillMaxSize(),
                        strokeWidth = 12.dp,
                        color = AppTheme.ChipBackground,
                        strokeCap = StrokeCap.Round
                    )
                    CircularProgressIndicator(
                        progress = { animatedProgress },
                        modifier = Modifier.fillMaxSize(),
                        strokeWidth = 12.dp,
                        color = getCompatibilityColor(result.overallCompatibility),
                        strokeCap = StrokeCap.Round
                    )

                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = String.format("%.0f", result.overallCompatibility),
                            fontSize = 36.sp,
                            fontFamily = CinzelDecorativeFamily,
                            fontWeight = FontWeight.Bold,
                            color = AppTheme.TextPrimary
                        )
                        Text(
                            text = "%",
                            fontSize = 14.sp,
                            fontFamily = SpaceGroteskFamily,
                            color = AppTheme.TextMuted
                        )
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    ScoreMiniChip(
                        label = "Astro",
                        value = result.astroCompatibility,
                        modifier = Modifier.weight(1f)
                    )
                    ScoreMiniChip(
                        label = "Daily Life",
                        value = result.practicalCompatibility.overallScore,
                        modifier = Modifier.weight(1f)
                    )
                    ScoreMiniChip(
                        label = "Vedic Core",
                        value = result.structuralVedicScore,
                        modifier = Modifier.weight(1f)
                    )
                }

                Spacer(modifier = Modifier.height(20.dp))

                // Profile comparison
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            chart1.birthData.name,
                            fontSize = 14.sp,
                            fontFamily = PoppinsFontFamily,
                            fontWeight = FontWeight.SemiBold,
                            color = AppTheme.AccentPrimary
                        )
                        Text(
                            ZodiacSign.fromLongitude(chart1.ascendant).displayName,
                            fontSize = 12.sp,
                            fontFamily = SpaceGroteskFamily,
                            color = AppTheme.TextMuted
                        )
                    }

                    Icon(
                        Icons.Filled.CompareArrows,
                        contentDescription = null,
                        tint = AppTheme.TextSubtle,
                        modifier = Modifier.size(24.dp)
                    )

                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            chart2.birthData.name,
                            fontSize = 14.sp,
                            fontFamily = PoppinsFontFamily,
                            fontWeight = FontWeight.SemiBold,
                            color = AppTheme.AccentTeal
                        )
                        Text(
                            ZodiacSign.fromLongitude(chart2.ascendant).displayName,
                            fontSize = 12.sp,
                            fontFamily = SpaceGroteskFamily,
                            color = AppTheme.TextMuted
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Compatibility categories
        Surface(
            modifier = Modifier.fillMaxWidth(),
            color = AppTheme.CardBackground,
            border = androidx.compose.foundation.BorderStroke(1.dp, AppTheme.BorderColor),
            shape = RoundedCornerShape(NeoVedicTokens.ElementCornerRadius)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                result.compatibilityCategories.forEach { category ->
                    CompatibilityCategoryRow(category = category)
                    if (category != result.compatibilityCategories.last()) {
                        HorizontalDivider(
                            modifier = Modifier.padding(vertical = 12.dp),
                            color = AppTheme.BorderColor.copy(alpha = 0.5f)
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Aspect summary
        Surface(
            modifier = Modifier.fillMaxWidth(),
            color = AppTheme.CardBackground,
            border = androidx.compose.foundation.BorderStroke(1.dp, AppTheme.BorderColor),
            shape = RoundedCornerShape(NeoVedicTokens.ElementCornerRadius)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    stringResource(StringKeyDosha.SYNASTRY_KEY_ASPECTS),
                    fontSize = 16.sp,
                    fontFamily = CinzelDecorativeFamily,
                    fontWeight = FontWeight.SemiBold,
                    color = AppTheme.TextPrimary
                )

                Spacer(modifier = Modifier.height(12.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    AspectCountChip(
                        label = stringResource(StringKeyDosha.SYNASTRY_HARMONIOUS),
                        count = result.harmoniousAspects.size,
                        color = AppTheme.SuccessColor
                    )
                    AspectCountChip(
                        label = stringResource(StringKeyDosha.SYNASTRY_CHALLENGING),
                        count = result.challengingAspects.size,
                        color = AppTheme.WarningColor
                    )
                }
            }
        }

        // Key findings
        if (result.keyFindings.isNotEmpty()) {
            Spacer(modifier = Modifier.height(16.dp))

            Surface(
                modifier = Modifier.fillMaxWidth(),
                color = AppTheme.CardElevated,
                border = androidx.compose.foundation.BorderStroke(1.dp, AppTheme.BorderColor),
                shape = RoundedCornerShape(NeoVedicTokens.ElementCornerRadius)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    result.keyFindings.forEach { finding ->
                        Row(
                            modifier = Modifier.padding(vertical = 4.dp),
                            verticalAlignment = Alignment.Top
                        ) {
                            Icon(
                                Icons.Filled.Stars,
                                contentDescription = null,
                                tint = AppTheme.AccentGold,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                finding,
                                fontSize = 14.sp,
                                fontFamily = PoppinsFontFamily,
                                color = AppTheme.TextSecondary
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun ScoreMiniChip(
    label: String,
    value: Double,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier,
        color = AppTheme.ChipBackground,
        shape = RoundedCornerShape(NeoVedicTokens.ElementCornerRadius)
    ) {
        Column(
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = label,
                style = MaterialTheme.typography.labelSmall,
                color = AppTheme.TextMuted,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                text = String.format("%.0f", value),
                style = MaterialTheme.typography.titleSmall,
                color = getCompatibilityColor(value),
                fontWeight = FontWeight.Bold,
                fontFamily = SpaceGroteskFamily
            )
        }
    }
}

@Composable
private fun CompatibilityCategoryRow(category: CompatibilityCategory) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(36.dp)
                .clip(CircleShape)
                .background(AppTheme.AccentPrimary.copy(alpha = 0.1f)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                category.icon,
                contentDescription = null,
                tint = AppTheme.AccentPrimary,
                modifier = Modifier.size(18.dp)
            )
        }

        Spacer(modifier = Modifier.width(12.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                category.name,
                fontSize = 14.sp,
                fontFamily = PoppinsFontFamily,
                fontWeight = FontWeight.Medium,
                color = AppTheme.TextPrimary
            )
            LinearProgressIndicator(
                progress = { (category.score / category.maxScore).toFloat() },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(4.dp)
                    .clip(RoundedCornerShape(NeoVedicTokens.ElementCornerRadius)),
                color = getCompatibilityColor(category.score * 10),
                trackColor = AppTheme.ChipBackground
            )
        }

        Spacer(modifier = Modifier.width(12.dp))

        Text(
            String.format("%.1f", category.score),
            fontSize = 14.sp,
            fontFamily = SpaceGroteskFamily,
            fontWeight = FontWeight.SemiBold,
            color = getCompatibilityColor(category.score * 10)
        )
    }
}

@Composable
private fun AspectCountChip(label: String, count: Int, color: Color) {
    Surface(
        color = color.copy(alpha = 0.1f),
        shape = RoundedCornerShape(NeoVedicTokens.ElementCornerRadius)
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                count.toString(),
                fontSize = 18.sp,
                fontFamily = CinzelDecorativeFamily,
                fontWeight = FontWeight.Bold,
                color = color
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                label,
                fontSize = 12.sp,
                fontFamily = PoppinsFontFamily,
                color = color.copy(alpha = 0.8f)
            )
        }
    }
}

@Composable
private fun SynastryAspectsTab(result: SynastryAnalysisResult) {
    val language = currentLanguage()

    Column(modifier = Modifier.padding(16.dp)) {
        if (result.aspects.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    stringResource(StringKeyDosha.SYNASTRY_NO_ASPECTS),
                    style = MaterialTheme.typography.bodyMedium,
                    color = AppTheme.TextMuted
                )
            }
        } else {
            result.aspects.take(20).forEach { aspect ->
                AspectCard(aspect = aspect, language = language)
                Spacer(modifier = Modifier.height(8.dp))
            }
        }
    }
}

@Composable
private fun AspectCard(aspect: SynastryAspect, language: Language) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = when (aspect.aspectType.nature) {
            AspectNature.HARMONIOUS -> AppTheme.SuccessColor.copy(alpha = 0.05f)
            AspectNature.CHALLENGING -> AppTheme.WarningColor.copy(alpha = 0.05f)
            else -> AppTheme.CardBackground
        },
        border = androidx.compose.foundation.BorderStroke(1.dp, AppTheme.BorderColor),
        shape = RoundedCornerShape(NeoVedicTokens.ElementCornerRadius)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Aspect symbol
            Box(
                modifier = Modifier
                    .size(44.dp)
                    .clip(CircleShape)
                    .background(
                        when (aspect.aspectType.nature) {
                            AspectNature.HARMONIOUS -> AppTheme.SuccessColor.copy(alpha = 0.15f)
                            AspectNature.CHALLENGING -> AppTheme.WarningColor.copy(alpha = 0.15f)
                            else -> AppTheme.ChipBackground
                        }
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    aspect.aspectType.symbol,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color = when (aspect.aspectType.nature) {
                        AspectNature.HARMONIOUS -> AppTheme.SuccessColor
                        AspectNature.CHALLENGING -> AppTheme.WarningColor
                        else -> AppTheme.TextPrimary
                    }
                )
            }

            Spacer(modifier = Modifier.width(14.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    "${aspect.planet1.getLocalizedName(language)} ${aspect.aspectType.symbol} ${aspect.planet2.getLocalizedName(language)}",
                    fontSize = 14.sp,
                    fontFamily = PoppinsFontFamily,
                    fontWeight = FontWeight.SemiBold,
                    color = AppTheme.TextPrimary
                )
                Text(
                    aspect.aspectType.getLocalizedName(language),
                    fontSize = 12.sp,
                    fontFamily = SpaceGroteskFamily,
                    color = AppTheme.TextMuted
                )
            }

            Column(horizontalAlignment = Alignment.End) {
                Text(
                    String.format("%.1f\u00B0", aspect.orb),
                    fontSize = 12.sp,
                    fontFamily = SpaceGroteskFamily,
                    fontWeight = FontWeight.Medium,
                    color = AppTheme.TextSecondary
                )
                Text(
                    if (aspect.isApplying) stringResource(StringKeyDosha.SYNASTRY_APPLYING)
                    else stringResource(StringKeyDosha.SYNASTRY_SEPARATING),
                    fontSize = 10.sp,
                    fontFamily = SpaceGroteskFamily,
                    color = AppTheme.TextSubtle
                )
            }
        }
    }
}

@Composable
private fun SynastryHouseOverlaysTab(
    result: SynastryAnalysisResult,
    chart1: VedicChart,
    chart2: VedicChart
) {
    Column(modifier = Modifier.padding(16.dp)) {
        // Chart 1 planets in Chart 2 houses
        Text(
            "${chart1.birthData.name}'s planets in ${chart2.birthData.name}'s houses",
            fontSize = 16.sp,
            fontFamily = CinzelDecorativeFamily,
            fontWeight = FontWeight.SemiBold,
            color = AppTheme.TextPrimary,
            modifier = Modifier.padding(bottom = 12.dp)
        )

        result.houseOverlays1In2.forEach { overlay ->
            HouseOverlayCard(overlay = overlay, chartName = chart2.birthData.name)
            Spacer(modifier = Modifier.height(8.dp))
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Chart 2 planets in Chart 1 houses
        Text(
            "${chart2.birthData.name}'s planets in ${chart1.birthData.name}'s houses",
            fontSize = 16.sp,
            fontFamily = CinzelDecorativeFamily,
            fontWeight = FontWeight.SemiBold,
            color = AppTheme.TextPrimary,
            modifier = Modifier.padding(bottom = 12.dp)
        )

        result.houseOverlays2In1.forEach { overlay ->
            HouseOverlayCard(overlay = overlay, chartName = chart1.birthData.name)
            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}

@Composable
private fun HouseOverlayCard(overlay: HouseOverlay, chartName: String) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = AppTheme.CardBackground,
        border = androidx.compose.foundation.BorderStroke(1.dp, AppTheme.BorderColor),
        shape = RoundedCornerShape(NeoVedicTokens.ElementCornerRadius)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(AppTheme.AccentPrimary.copy(alpha = 0.1f)),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    "H${overlay.houseNumber}",
                    fontSize = 14.sp,
                    fontFamily = SpaceGroteskFamily,
                    fontWeight = FontWeight.Bold,
                    color = AppTheme.AccentPrimary
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    overlay.planet.displayName,
                    fontSize = 14.sp,
                    fontFamily = PoppinsFontFamily,
                    fontWeight = FontWeight.SemiBold,
                    color = AppTheme.TextPrimary
                )
                Text(
                    overlay.lifeArea,
                    fontSize = 12.sp,
                    fontFamily = PoppinsFontFamily,
                    color = AppTheme.TextMuted
                )
            }
        }
    }
}

@Composable
private fun SynastryCompatibilityTab(result: SynastryAnalysisResult) {
    Column(modifier = Modifier.padding(16.dp)) {
        PracticalBreakdownCard(result)
        Spacer(modifier = Modifier.height(12.dp))

        // Detailed breakdown of each compatibility category
        result.compatibilityCategories.forEach { category ->
            CompatibilityDetailCard(category = category)
            Spacer(modifier = Modifier.height(12.dp))
        }
    }
}

@Composable
private fun PracticalBreakdownCard(result: SynastryAnalysisResult) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = AppTheme.CardBackground,
        border = androidx.compose.foundation.BorderStroke(1.dp, AppTheme.BorderColor),
        shape = RoundedCornerShape(NeoVedicTokens.ElementCornerRadius)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "Practical Breakdown",
                fontSize = 16.sp,
                fontFamily = CinzelDecorativeFamily,
                fontWeight = FontWeight.SemiBold,
                color = AppTheme.TextPrimary
            )
            Spacer(modifier = Modifier.height(10.dp))

            PracticalMetricRow("Communication", result.practicalCompatibility.communicationScore)
            PracticalMetricRow("Financial Alignment", result.practicalCompatibility.financialAlignmentScore)
            PracticalMetricRow("Family Values", result.practicalCompatibility.familyValuesScore)
            PracticalMetricRow("Conflict Style", result.practicalCompatibility.conflictStyleScore)
        }
    }
}

@Composable
private fun PracticalMetricRow(label: String, score: Double) {
    Column(modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = label,
                style = MaterialTheme.typography.bodySmall,
                color = AppTheme.TextSecondary
            )
            Text(
                text = String.format("%.1f/10", score),
                style = MaterialTheme.typography.bodySmall,
                color = getCompatibilityColor(score * 10.0),
                fontWeight = FontWeight.SemiBold
            )
        }
        LinearProgressIndicator(
            progress = { (score / 10.0).toFloat() },
            modifier = Modifier
                .fillMaxWidth()
                .height(6.dp)
                .clip(RoundedCornerShape(NeoVedicTokens.ElementCornerRadius)),
            color = getCompatibilityColor(score * 10.0),
            trackColor = AppTheme.ChipBackground
        )
    }
}

@Composable
private fun CompatibilityDetailCard(category: CompatibilityCategory) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = AppTheme.CardBackground,
        border = androidx.compose.foundation.BorderStroke(1.dp, AppTheme.BorderColor),
        shape = RoundedCornerShape(NeoVedicTokens.ElementCornerRadius)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    category.icon,
                    contentDescription = null,
                    tint = AppTheme.AccentPrimary,
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(12.dp))
                Text(
                    category.name,
                    fontSize = 16.sp,
                    fontFamily = CinzelDecorativeFamily,
                    fontWeight = FontWeight.SemiBold,
                    color = AppTheme.TextPrimary
                )
                Spacer(modifier = Modifier.weight(1f))
                Text(
                    String.format("%.1f/%.1f", category.score, category.maxScore),
                    fontSize = 14.sp,
                    fontFamily = SpaceGroteskFamily,
                    fontWeight = FontWeight.Bold,
                    color = getCompatibilityColor(category.score * 10)
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            LinearProgressIndicator(
                progress = { (category.score / category.maxScore).toFloat() },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(8.dp)
                    .clip(RoundedCornerShape(NeoVedicTokens.ElementCornerRadius)),
                color = getCompatibilityColor(category.score * 10),
                trackColor = AppTheme.ChipBackground
            )

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                category.description,
                fontSize = 12.sp,
                fontFamily = PoppinsFontFamily,
                color = AppTheme.TextMuted
            )
        }
    }
}

@Composable
private fun SynastryCalculatingState() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            CircularProgressIndicator(
                modifier = Modifier.size(48.dp),
                color = AppTheme.AccentPrimary,
                strokeWidth = 4.dp
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                stringResource(StringKeyDosha.SYNASTRY_ANALYZING),
                fontSize = 14.sp,
                fontFamily = PoppinsFontFamily,
                color = AppTheme.TextMuted
            )
        }
    }
}

@Composable
private fun SynastryErrorCard(error: String, onRetry: () -> Unit) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        color = AppTheme.ErrorColor.copy(alpha = 0.08f),
        border = androidx.compose.foundation.BorderStroke(1.dp, AppTheme.ErrorColor.copy(alpha = 0.2f)),
        shape = RoundedCornerShape(NeoVedicTokens.ElementCornerRadius)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                Icons.Filled.ErrorOutline,
                contentDescription = null,
                tint = AppTheme.ErrorColor,
                modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.width(12.dp))
            Text(
                error,
                fontSize = 14.sp,
                fontFamily = PoppinsFontFamily,
                color = AppTheme.ErrorColor,
                modifier = Modifier.weight(1f)
            )
            TextButton(onClick = onRetry) {
                Text(
                    stringResource(StringKey.BTN_RETRY),
                    color = AppTheme.ErrorColor,
                    fontFamily = SpaceGroteskFamily
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ChartSelectorBottomSheet(
    title: String,
    icon: ImageVector,
    accentColor: Color,
    charts: List<SavedChart>,
    selectedId: Long?,
    excludeId: Long?,
    onSelect: (Long) -> Unit,
    onDismiss: () -> Unit
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val availableCharts = charts.filter { it.id != excludeId }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = AppTheme.CardBackground,
        shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
                .padding(bottom = 32.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(bottom = 20.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .clip(CircleShape)
                        .background(accentColor.copy(alpha = 0.12f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(icon, contentDescription = null, tint = accentColor)
                }
                Spacer(modifier = Modifier.width(16.dp))
                Column {
                    Text(
                        title,
                        fontSize = 20.sp,
                        fontFamily = CinzelDecorativeFamily,
                        fontWeight = FontWeight.Bold,
                        color = AppTheme.TextPrimary
                    )
                    Text(
                        stringResource(StringKeyDosha.SYNASTRY_AVAILABLE_CHARTS, availableCharts.size),
                        fontSize = 12.sp,
                        fontFamily = PoppinsFontFamily,
                        color = AppTheme.TextMuted
                    )
                }
            }

            if (availableCharts.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(150.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        stringResource(StringKeyMatch.MATCH_NO_CHARTS_AVAILABLE),
                        fontSize = 14.sp,
                        fontFamily = PoppinsFontFamily,
                        color = AppTheme.TextMuted
                    )
                }
            } else {
                LazyColumn(
                    modifier = Modifier.heightIn(max = 400.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(availableCharts, key = { it.id }) { chart ->
                        val isSelected = chart.id == selectedId

                        Surface(
                            onClick = { onSelect(chart.id) },
                            color = if (isSelected) accentColor.copy(alpha = 0.1f) else Color.Transparent,
                            shape = RoundedCornerShape(NeoVedicTokens.ElementCornerRadius),
                            border = if (isSelected)
                                androidx.compose.foundation.BorderStroke(1.5.dp, accentColor)
                            else
                                androidx.compose.foundation.BorderStroke(1.dp, AppTheme.BorderColor.copy(alpha = 0.5f))
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(44.dp)
                                        .clip(CircleShape)
                                        .background(
                                            if (isSelected) accentColor.copy(alpha = 0.15f)
                                            else AppTheme.ChipBackground
                                        ),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        chart.name.firstOrNull()?.uppercase() ?: "?",
                                        fontSize = 18.sp,
                                        fontFamily = CinzelDecorativeFamily,
                                        fontWeight = FontWeight.Bold,
                                        color = if (isSelected) accentColor else AppTheme.TextMuted
                                    )
                                }
                                Spacer(modifier = Modifier.width(14.dp))
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        chart.name,
                                        fontSize = 16.sp,
                                        fontFamily = PoppinsFontFamily,
                                        fontWeight = FontWeight.SemiBold,
                                        color = AppTheme.TextPrimary
                                    )
                                    Text(
                                        chart.location,
                                        fontSize = 12.sp,
                                        fontFamily = SpaceGroteskFamily,
                                        color = AppTheme.TextMuted
                                    )
                                }
                                if (isSelected) {
                                    Icon(
                                        Icons.Filled.CheckCircle,
                                        contentDescription = null,
                                        tint = accentColor
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun SynastryInfoDialog(onDismiss: () -> Unit) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                stringResource(StringKeyDosha.SYNASTRY_INFO_TITLE),
                fontWeight = FontWeight.Bold,
                fontFamily = CinzelDecorativeFamily,
                color = AppTheme.TextPrimary,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
        },
        text = {
            Text(
                stringResource(StringKeyDosha.SYNASTRY_INFO_DESC),
                fontFamily = PoppinsFontFamily,
                style = MaterialTheme.typography.bodyMedium,
                color = AppTheme.TextSecondary
            )
        },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text(stringResource(StringKey.BTN_CLOSE), color = AppTheme.AccentGold, fontFamily = SpaceGroteskFamily)
            }
        },
        containerColor = AppTheme.CardBackground
    )
}

@Composable
private fun getCompatibilityColor(score: Double): Color {
    return when {
        score >= 80 -> AppTheme.SuccessColor
        score >= 60 -> AppTheme.SuccessColor
        score >= 40 -> AppTheme.WarningColor
        else -> AppTheme.ErrorColor
    }
}








