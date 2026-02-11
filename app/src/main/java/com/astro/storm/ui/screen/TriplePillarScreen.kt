package com.astro.storm.ui.screen

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.TrendingDown
import androidx.compose.material.icons.filled.TrendingFlat
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material.icons.outlined.Analytics
import androidx.compose.material.icons.outlined.CalendarMonth
import androidx.compose.material.icons.outlined.Dashboard
import androidx.compose.material.icons.outlined.Timeline
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.astro.storm.core.common.Language
import com.astro.storm.core.common.StringKey
import com.astro.storm.core.common.StringKeyTriplePillar
import com.astro.storm.core.common.StringResources
import com.astro.storm.data.localization.LocalLanguage
import com.astro.storm.data.localization.stringResource
import com.astro.storm.core.model.VedicChart
import com.astro.storm.ephemeris.TriplePillarEngine.AshtakavargaAnalysisDetail
import com.astro.storm.ephemeris.TriplePillarEngine.BinduStrength
import com.astro.storm.ephemeris.TriplePillarEngine.DashaAnalysisDetail
import com.astro.storm.ephemeris.TriplePillarEngine.GocharaAnalysisDetail
import com.astro.storm.ephemeris.TriplePillarEngine.GocharaEffectiveness
import com.astro.storm.ephemeris.TriplePillarEngine.LifeAreaImpact
import com.astro.storm.ephemeris.TriplePillarEngine.MonthlyForecast
import com.astro.storm.ephemeris.TriplePillarEngine.OpportunityWindow
import com.astro.storm.ephemeris.TriplePillarEngine.PillarSynthesis
import com.astro.storm.ephemeris.TriplePillarEngine.QualityLevel
import com.astro.storm.ephemeris.TriplePillarEngine.TriplePillarResult
import com.astro.storm.ui.theme.AppTheme
import com.astro.storm.ui.viewmodel.TriplePillarViewModel
import com.astro.storm.ui.viewmodel.TriplePillarViewModel.TriplePillarTab
import com.astro.storm.ui.viewmodel.TriplePillarViewModel.TriplePillarUiState
import java.time.format.DateTimeFormatter

/**
 * Triple-Pillar Predictive Engine Screen
 *
 * Displays the synthesized Dasha + Gochara + Ashtakavarga prediction analysis
 * with a success probability timeline, pillar breakdowns, and life area impacts.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TriplePillarScreen(
    chart: VedicChart?,
    onNavigateBack: () -> Unit,
    viewModel: TriplePillarViewModel = hiltViewModel()
) {
    val language = LocalLanguage.current
    val colors = AppTheme.current
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(chart, language) {
        viewModel.calculateSynthesis(chart, language)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(
                            text = stringResource(StringKeyTriplePillar.TITLE),
                            fontWeight = FontWeight.SemiBold,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            fontSize = 18.sp
                        )
                        Text(
                            text = stringResource(StringKeyTriplePillar.SUBTITLE),
                            style = MaterialTheme.typography.bodySmall,
                            color = colors.TextSecondary,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(StringKey.BTN_BACK)
                        )
                    }
                },
                actions = {
                    IconButton(onClick = { viewModel.refresh(chart, language) }) {
                        Icon(
                            imageVector = Icons.Default.Refresh,
                            contentDescription = stringResource(StringKey.BTN_RETRY)
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = colors.ScreenBackground,
                    titleContentColor = colors.TextPrimary,
                    navigationIconContentColor = colors.TextPrimary,
                    actionIconContentColor = colors.AccentPrimary
                )
            )
        },
        containerColor = colors.ScreenBackground
    ) { padding ->
        when (val state = uiState) {
            is TriplePillarUiState.Initial,
            is TriplePillarUiState.Loading -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        CircularProgressIndicator(color = colors.AccentPrimary)
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = stringResource(StringKeyTriplePillar.TITLE),
                            color = colors.TextSecondary
                        )
                    }
                }
            }

            is TriplePillarUiState.Error -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.padding(24.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Warning,
                            contentDescription = null,
                            tint = colors.ErrorColor,
                            modifier = Modifier.size(48.dp)
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = if (language == Language.NEPALI) state.messageNe else state.message,
                            color = colors.ErrorColor,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }

            is TriplePillarUiState.NoChart -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = stringResource(StringKeyTriplePillar.ERROR_NO_CHART),
                        color = colors.TextSecondary,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(24.dp)
                    )
                }
            }

            is TriplePillarUiState.Success -> {
                TriplePillarContent(
                    result = state.result,
                    selectedTab = state.selectedTab,
                    onTabSelected = viewModel::selectTab,
                    language = language,
                    modifier = Modifier.padding(padding)
                )
            }
        }
    }
}

// ============================================================================
// MAIN CONTENT WITH TABS
// ============================================================================

@Composable
private fun TriplePillarContent(
    result: TriplePillarResult,
    selectedTab: TriplePillarTab,
    onTabSelected: (TriplePillarTab) -> Unit,
    language: Language,
    modifier: Modifier = Modifier
) {
    val colors = AppTheme.current
    val tabs = TriplePillarTab.entries

    Column(modifier = modifier.fillMaxSize()) {
        TabRow(
            selectedTabIndex = tabs.indexOf(selectedTab),
            containerColor = colors.CardBackground,
            contentColor = colors.AccentPrimary,
            indicator = { tabPositions ->
                if (selectedTab.ordinal < tabPositions.size) {
                    TabRowDefaults.SecondaryIndicator(
                        modifier = Modifier.tabIndicatorOffset(tabPositions[tabs.indexOf(selectedTab)]),
                        color = colors.AccentPrimary,
                        height = 3.dp
                    )
                }
            }
        ) {
            tabs.forEach { tab ->
                val (title, icon) = getTabInfo(tab, language)
                Tab(
                    selected = selectedTab == tab,
                    onClick = { onTabSelected(tab) },
                    text = {
                        Text(
                            text = title,
                            fontSize = 11.sp,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    },
                    icon = {
                        Icon(
                            imageVector = icon,
                            contentDescription = title,
                            modifier = Modifier.size(18.dp)
                        )
                    },
                    selectedContentColor = colors.AccentPrimary,
                    unselectedContentColor = colors.TextSecondary
                )
            }
        }

        when (selectedTab) {
            TriplePillarTab.OVERVIEW -> OverviewTab(result, language)
            TriplePillarTab.TIMELINE -> TimelineTab(result, language)
            TriplePillarTab.PILLARS -> PillarsTab(result, language)
            TriplePillarTab.FORECAST -> ForecastTab(result, language)
        }
    }
}

private fun getTabInfo(tab: TriplePillarTab, language: Language): Pair<String, ImageVector> {
    return when (tab) {
        TriplePillarTab.OVERVIEW -> Pair(
            StringResources.get(StringKeyTriplePillar.TAB_OVERVIEW, language),
            Icons.Outlined.Dashboard
        )
        TriplePillarTab.TIMELINE -> Pair(
            StringResources.get(StringKeyTriplePillar.TAB_TIMELINE, language),
            Icons.Outlined.Timeline
        )
        TriplePillarTab.PILLARS -> Pair(
            StringResources.get(StringKeyTriplePillar.TAB_PILLARS, language),
            Icons.Outlined.Analytics
        )
        TriplePillarTab.FORECAST -> Pair(
            StringResources.get(StringKeyTriplePillar.TAB_FORECAST, language),
            Icons.Outlined.CalendarMonth
        )
    }
}

// ============================================================================
// TAB 1: OVERVIEW
// ============================================================================

@Composable
private fun OverviewTab(result: TriplePillarResult, language: Language) {
    val colors = AppTheme.current
    val synthesis = result.currentSynthesis

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = androidx.compose.foundation.layout.PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Success Probability Hero Card
        item {
            SuccessProbabilityCard(synthesis, language)
        }

        // Three Pillar Scores
        item {
            ThreePillarScoresCard(synthesis, language)
        }

        // Interpretation
        item {
            InterpretationCard(synthesis, language)
        }

        // Life Area Impacts
        item {
            SectionHeader(stringResource(StringKeyTriplePillar.HEADER_LIFE_AREA_IMPACT))
        }

        items(result.lifeAreaImpacts) { impact ->
            LifeAreaImpactRow(impact, language)
        }

        // Peak Windows
        if (result.peakWindows.isNotEmpty()) {
            item {
                SectionHeader(stringResource(StringKeyTriplePillar.HEADER_PEAK_WINDOWS))
            }
            items(result.peakWindows) { window ->
                OpportunityWindowCard(window, language, isPeak = true)
            }
        }

        // Caution Periods
        if (result.cautionPeriods.isNotEmpty()) {
            item {
                SectionHeader(stringResource(StringKeyTriplePillar.HEADER_RISK_WINDOWS))
            }
            items(result.cautionPeriods) { window ->
                OpportunityWindowCard(window, language, isPeak = false)
            }
        }

        // Classical Reference
        item {
            ClassicalReferenceCard(result, language)
        }

        item { Spacer(modifier = Modifier.height(32.dp)) }
    }
}

@Composable
private fun SuccessProbabilityCard(synthesis: PillarSynthesis, language: Language) {
    val colors = AppTheme.current
    val qualityColor = getQualityColor(synthesis.qualityLevel)
    val animatedProgress by animateFloatAsState(
        targetValue = synthesis.successProbability / 100f,
        animationSpec = tween(durationMillis = 1200),
        label = "progress"
    )

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = colors.CardBackground),
        shape = RoundedCornerShape(20.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = stringResource(StringKeyTriplePillar.SUCCESS_PROBABILITY),
                style = MaterialTheme.typography.titleMedium,
                color = colors.TextSecondary,
                fontWeight = FontWeight.Medium
            )
            Spacer(modifier = Modifier.height(16.dp))

            // Large percentage display
            Box(contentAlignment = Alignment.Center) {
                CircularProgressIndicator(
                    progress = { animatedProgress },
                    modifier = Modifier.size(140.dp),
                    color = qualityColor,
                    trackColor = colors.CardBackground.copy(alpha = 0.3f),
                    strokeWidth = 10.dp
                )
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "${synthesis.successProbability}%",
                        fontSize = 36.sp,
                        fontWeight = FontWeight.Bold,
                        color = qualityColor
                    )
                    Text(
                        text = if (language == Language.NEPALI) synthesis.qualityLevel.ne else synthesis.qualityLevel.en,
                        style = MaterialTheme.typography.bodySmall,
                        color = qualityColor,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Recommendation
            Text(
                text = if (language == Language.NEPALI) synthesis.recommendationNe else synthesis.recommendation,
                style = MaterialTheme.typography.bodyMedium,
                color = colors.TextSecondary,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(horizontal = 8.dp)
            )
        }
    }
}

@Composable
private fun ThreePillarScoresCard(synthesis: PillarSynthesis, language: Language) {
    val colors = AppTheme.current

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = colors.CardBackground),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = stringResource(StringKeyTriplePillar.HEADER_PILLAR_BREAKDOWN),
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.SemiBold,
                color = colors.TextPrimary
            )
            Spacer(modifier = Modifier.height(12.dp))

            PillarScoreBar(
                label = StringResources.get(StringKeyTriplePillar.PILLAR_DASHA, language),
                score = synthesis.dashaScore,
                weight = "40%",
                color = Color(0xFF6366F1), // Indigo
                language = language
            )
            Spacer(modifier = Modifier.height(10.dp))

            PillarScoreBar(
                label = StringResources.get(StringKeyTriplePillar.PILLAR_GOCHARA, language),
                score = synthesis.gocharaScore,
                weight = "35%",
                color = Color(0xFF22C55E), // Green
                language = language
            )
            Spacer(modifier = Modifier.height(10.dp))

            PillarScoreBar(
                label = StringResources.get(StringKeyTriplePillar.PILLAR_ASHTAKAVARGA, language),
                score = synthesis.ashtakavargaScore,
                weight = "25%",
                color = Color(0xFFF59E0B), // Amber
                language = language
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Composite score
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = stringResource(StringKeyTriplePillar.COMPOSITE_STRENGTH),
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = colors.TextPrimary
                )
                Text(
                    text = "${(synthesis.compositeScore * 100).toInt()}%",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = getQualityColor(synthesis.qualityLevel)
                )
            }
        }
    }
}

@Composable
private fun PillarScoreBar(
    label: String,
    score: Double,
    weight: String,
    color: Color,
    language: Language
) {
    val colors = AppTheme.current
    val animatedProgress by animateFloatAsState(
        targetValue = score.toFloat().coerceIn(0f, 1f),
        animationSpec = tween(durationMillis = 800),
        label = "pillar_progress"
    )

    Column {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(8.dp)
                        .clip(CircleShape)
                        .background(color)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = label,
                    style = MaterialTheme.typography.bodySmall,
                    color = colors.TextPrimary,
                    fontWeight = FontWeight.Medium
                )
            }
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = "${(score * 100).toInt()}%",
                    style = MaterialTheme.typography.bodySmall,
                    color = color,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = "(${StringResources.get(StringKeyTriplePillar.WEIGHT_LABEL, language)}: $weight)",
                    style = MaterialTheme.typography.labelSmall,
                    color = colors.TextSecondary
                )
            }
        }
        Spacer(modifier = Modifier.height(4.dp))
        LinearProgressIndicator(
            progress = { animatedProgress },
            modifier = Modifier
                .fillMaxWidth()
                .height(6.dp)
                .clip(RoundedCornerShape(3.dp)),
            color = color,
            trackColor = color.copy(alpha = 0.15f)
        )
    }
}

@Composable
private fun InterpretationCard(synthesis: PillarSynthesis, language: Language) {
    val colors = AppTheme.current

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = colors.CardBackground),
        shape = RoundedCornerShape(16.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.Top
        ) {
            Icon(
                imageVector = Icons.Default.Info,
                contentDescription = null,
                tint = colors.AccentPrimary,
                modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.width(12.dp))
            Column {
                Text(
                    text = stringResource(StringKeyTriplePillar.OVERALL_ASSESSMENT),
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.SemiBold,
                    color = colors.TextPrimary
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = if (language == Language.NEPALI) synthesis.interpretationNe else synthesis.interpretation,
                    style = MaterialTheme.typography.bodyMedium,
                    color = colors.TextSecondary,
                    lineHeight = 22.sp
                )
            }
        }
    }
}

// ============================================================================
// TAB 2: TIMELINE
// ============================================================================

@Composable
private fun TimelineTab(result: TriplePillarResult, language: Language) {
    val colors = AppTheme.current

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = androidx.compose.foundation.layout.PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            SectionHeader(stringResource(StringKeyTriplePillar.HEADER_SUCCESS_TIMELINE))
        }

        // Mini bar chart timeline
        item {
            TimelineBarChart(result.monthlyForecasts, language)
        }

        item {
            SectionHeader(stringResource(StringKeyTriplePillar.HEADER_MONTHLY_FORECAST))
        }

        items(result.monthlyForecasts) { forecast ->
            MonthlyForecastCard(forecast, language)
        }

        item { Spacer(modifier = Modifier.height(32.dp)) }
    }
}

@Composable
private fun TimelineBarChart(forecasts: List<MonthlyForecast>, language: Language) {
    val colors = AppTheme.current

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = colors.CardBackground),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(6.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                items(forecasts) { forecast ->
                    val barColor = getQualityColor(forecast.qualityLevel)
                    val animatedHeight by animateFloatAsState(
                        targetValue = (forecast.compositeScore * 120).toFloat(),
                        animationSpec = tween(durationMillis = 600),
                        label = "bar_height"
                    )

                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.width(42.dp)
                    ) {
                        Text(
                            text = "${forecast.successProbability}",
                            style = MaterialTheme.typography.labelSmall,
                            color = barColor,
                            fontWeight = FontWeight.Bold,
                            fontSize = 9.sp
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Box(
                            modifier = Modifier
                                .width(28.dp)
                                .height(animatedHeight.dp.coerceAtLeast(4.dp))
                                .clip(RoundedCornerShape(topStart = 4.dp, topEnd = 4.dp))
                                .background(barColor)
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = if (language == Language.NEPALI)
                                forecast.monthNameNe.take(3)
                            else
                                forecast.monthName.take(3),
                            style = MaterialTheme.typography.labelSmall,
                            color = colors.TextSecondary,
                            fontSize = 9.sp,
                            maxLines = 1
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun MonthlyForecastCard(forecast: MonthlyForecast, language: Language) {
    val colors = AppTheme.current
    val qualityColor = getQualityColor(forecast.qualityLevel)
    var expanded by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { expanded = !expanded },
        colors = CardDefaults.cardColors(containerColor = colors.CardBackground),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(qualityColor.copy(alpha = 0.15f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "${forecast.successProbability}",
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp,
                            color = qualityColor
                        )
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(
                            text = if (language == Language.NEPALI) forecast.monthNameNe else forecast.monthName,
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.SemiBold,
                            color = colors.TextPrimary
                        )
                        Text(
                            text = forecast.month.format(DateTimeFormatter.ofPattern("yyyy")),
                            style = MaterialTheme.typography.bodySmall,
                            color = colors.TextSecondary
                        )
                    }
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = when {
                            forecast.compositeScore >= 0.6 -> Icons.Default.TrendingUp
                            forecast.compositeScore >= 0.4 -> Icons.Default.TrendingFlat
                            else -> Icons.Default.TrendingDown
                        },
                        contentDescription = null,
                        tint = qualityColor,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = if (language == Language.NEPALI) forecast.qualityLevel.ne else forecast.qualityLevel.en,
                        style = MaterialTheme.typography.bodySmall,
                        color = qualityColor,
                        fontWeight = FontWeight.Medium
                    )
                }
            }

            AnimatedVisibility(
                visible = expanded,
                enter = expandVertically(),
                exit = shrinkVertically()
            ) {
                Column(modifier = Modifier.padding(top = 12.dp)) {
                    // Score breakdown
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        MiniPillarScore(
                            label = "D",
                            score = forecast.dashaScore,
                            color = Color(0xFF6366F1)
                        )
                        MiniPillarScore(
                            label = "G",
                            score = forecast.gocharaScore,
                            color = Color(0xFF22C55E)
                        )
                        MiniPillarScore(
                            label = "A",
                            score = forecast.ashtakavargaScore,
                            color = Color(0xFFF59E0B)
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    // Interpretation
                    Text(
                        text = if (language == Language.NEPALI) forecast.briefInterpretationNe else forecast.briefInterpretation,
                        style = MaterialTheme.typography.bodySmall,
                        color = colors.TextSecondary,
                        lineHeight = 18.sp
                    )

                    // Life areas
                    if (forecast.dominantLifeAreas.isNotEmpty()) {
                        Spacer(modifier = Modifier.height(8.dp))
                        Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                            forecast.dominantLifeAreas.take(3).forEach { area ->
                                Text(
                                    text = if (language == Language.NEPALI) area.ne else area.en,
                                    style = MaterialTheme.typography.labelSmall,
                                    color = colors.AccentPrimary,
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(4.dp))
                                        .background(colors.AccentPrimary.copy(alpha = 0.1f))
                                        .padding(horizontal = 6.dp, vertical = 2.dp)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun MiniPillarScore(label: String, score: Double, color: Color) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall,
            color = color,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = "${(score * 100).toInt()}",
            style = MaterialTheme.typography.bodySmall,
            fontWeight = FontWeight.SemiBold,
            color = color
        )
    }
}

// ============================================================================
// TAB 3: PILLARS (Detailed Breakdown)
// ============================================================================

@Composable
private fun PillarsTab(result: TriplePillarResult, language: Language) {
    val synthesis = result.currentSynthesis

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = androidx.compose.foundation.layout.PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Dasha Pillar
        item {
            DashaPillarCard(synthesis.dashaAnalysis, synthesis.dashaScore, language)
        }

        // Gochara Pillar
        item {
            GocharaPillarCard(synthesis.gocharaAnalysis, synthesis.gocharaScore, language)
        }

        // Ashtakavarga Pillar
        item {
            AshtakavargaPillarCard(synthesis.ashtakavargaAnalysis, synthesis.ashtakavargaScore, language)
        }

        // Classical Reference
        item {
            ClassicalReferenceCard(result, language)
        }

        item { Spacer(modifier = Modifier.height(32.dp)) }
    }
}

@Composable
private fun DashaPillarCard(dasha: DashaAnalysisDetail, score: Double, language: Language) {
    val colors = AppTheme.current

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = colors.CardBackground),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(8.dp)
                        .clip(CircleShape)
                        .background(Color(0xFF6366F1))
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = stringResource(StringKeyTriplePillar.PILLAR_DASHA),
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = colors.TextPrimary
                )
                Spacer(modifier = Modifier.weight(1f))
                Text(
                    text = "${(score * 100).toInt()}%",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF6366F1)
                )
            }

            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = stringResource(StringKeyTriplePillar.PILLAR_DASHA_DESC),
                style = MaterialTheme.typography.bodySmall,
                color = colors.TextSecondary
            )
            Spacer(modifier = Modifier.height(12.dp))

            DetailRow(
                label = StringResources.get(StringKeyTriplePillar.MAHADASHA_LORD, language),
                value = dasha.mahadashaLord.getLocalizedName(language),
                colors = colors
            )
            dasha.antardashaLord?.let {
                DetailRow(
                    label = StringResources.get(StringKeyTriplePillar.ANTARDASHA_LORD, language),
                    value = it.getLocalizedName(language),
                    colors = colors
                )
            }
            dasha.pratyantardashaLord?.let {
                DetailRow(
                    label = StringResources.get(StringKeyTriplePillar.PRATYANTARDASHA_LORD, language),
                    value = it.getLocalizedName(language),
                    colors = colors
                )
            }
            DetailRow(
                label = StringResources.get(StringKeyTriplePillar.DASHA_DIGNITY, language),
                value = if (language == Language.NEPALI) dasha.mahadashaDignity.ne else dasha.mahadashaDignity.en,
                colors = colors
            )
            DetailRow(
                label = StringResources.get(StringKeyTriplePillar.DASHA_FUNCTIONAL_NATURE, language),
                value = if (language == Language.NEPALI) dasha.functionalNature.ne else dasha.functionalNature.en,
                colors = colors
            )
            DetailRow(
                label = StringResources.get(StringKeyTriplePillar.DASHA_HOUSE_LORDSHIP, language),
                value = dasha.dashaLordHouses.joinToString(", ") { "H$it" },
                colors = colors
            )

            if (dasha.isSandhiPeriod) {
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(8.dp))
                        .background(Color(0xFFF59E0B).copy(alpha = 0.15f))
                        .padding(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Warning,
                        contentDescription = null,
                        tint = Color(0xFFF59E0B),
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = StringResources.get(StringKeyTriplePillar.INTERP_SANDHI_PERIOD, language),
                        style = MaterialTheme.typography.bodySmall,
                        color = Color(0xFFF59E0B)
                    )
                }
            }
        }
    }
}

@Composable
private fun GocharaPillarCard(gochara: GocharaAnalysisDetail, score: Double, language: Language) {
    val colors = AppTheme.current

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = colors.CardBackground),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(8.dp)
                        .clip(CircleShape)
                        .background(Color(0xFF22C55E))
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = stringResource(StringKeyTriplePillar.PILLAR_GOCHARA),
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = colors.TextPrimary
                )
                Spacer(modifier = Modifier.weight(1f))
                Text(
                    text = "${(score * 100).toInt()}%",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF22C55E)
                )
            }

            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = stringResource(StringKeyTriplePillar.PILLAR_GOCHARA_DESC),
                style = MaterialTheme.typography.bodySmall,
                color = colors.TextSecondary
            )
            Spacer(modifier = Modifier.height(12.dp))

            // Summary row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                GocharaSummaryChip(
                    label = StringResources.get(StringKeyTriplePillar.FAVORABLE, language),
                    count = gochara.favorableCount,
                    color = Color(0xFF22C55E)
                )
                GocharaSummaryChip(
                    label = StringResources.get(StringKeyTriplePillar.UNFAVORABLE, language),
                    count = gochara.unfavorableCount,
                    color = Color(0xFFEF4444)
                )
                GocharaSummaryChip(
                    label = StringResources.get(StringKeyTriplePillar.TRANSIT_VEDHA, language),
                    count = gochara.vedhaActiveCount,
                    color = Color(0xFFF59E0B)
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Individual planet transits
            gochara.planetTransits.forEach { transit ->
                GocharaPlanetRow(transit, language)
            }
        }
    }
}

@Composable
private fun GocharaSummaryChip(label: String, count: Int, color: Color) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .clip(RoundedCornerShape(8.dp))
            .background(color.copy(alpha = 0.1f))
            .padding(horizontal = 12.dp, vertical = 6.dp)
    ) {
        Text(
            text = "$count",
            fontWeight = FontWeight.Bold,
            color = color,
            fontSize = 18.sp
        )
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall,
            color = color,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}

@Composable
private fun GocharaPlanetRow(
    transit: com.astro.storm.ephemeris.TriplePillarEngine.PlanetGocharaResult,
    language: Language
) {
    val colors = AppTheme.current
    val resultColor = when (transit.effectiveResult) {
        GocharaEffectiveness.FAVORABLE_CLEAR -> Color(0xFF22C55E)
        GocharaEffectiveness.FAVORABLE_VEDHA -> Color(0xFFF59E0B)
        GocharaEffectiveness.UNFAVORABLE -> Color(0xFFEF4444)
        GocharaEffectiveness.NEUTRAL -> colors.TextSecondary
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 3.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = transit.planet.getLocalizedName(language),
                style = MaterialTheme.typography.bodySmall,
                color = colors.TextPrimary,
                fontWeight = FontWeight.Medium,
                modifier = Modifier.width(70.dp)
            )
            Text(
                text = "H${transit.transitHouseFromMoon}",
                style = MaterialTheme.typography.bodySmall,
                color = colors.TextSecondary,
                modifier = Modifier.width(30.dp)
            )
            Text(
                text = transit.transitSign.getLocalizedName(language),
                style = MaterialTheme.typography.bodySmall,
                color = colors.TextSecondary,
                modifier = Modifier.weight(1f)
            )
        }

        Row(verticalAlignment = Alignment.CenterVertically) {
            if (transit.hasVedha && transit.vedhaByPlanet != null) {
                Text(
                    text = "V:${transit.vedhaByPlanet.getLocalizedAbbr(language)}",
                    style = MaterialTheme.typography.labelSmall,
                    color = Color(0xFFF59E0B),
                    modifier = Modifier.padding(end = 4.dp)
                )
            }
            Box(
                modifier = Modifier
                    .size(8.dp)
                    .clip(CircleShape)
                    .background(resultColor)
            )
        }
    }
}

@Composable
private fun AshtakavargaPillarCard(
    ashtakavarga: AshtakavargaAnalysisDetail,
    score: Double,
    language: Language
) {
    val colors = AppTheme.current

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = colors.CardBackground),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(8.dp)
                        .clip(CircleShape)
                        .background(Color(0xFFF59E0B))
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = stringResource(StringKeyTriplePillar.PILLAR_ASHTAKAVARGA),
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = colors.TextPrimary
                )
                Spacer(modifier = Modifier.weight(1f))
                Text(
                    text = "${(score * 100).toInt()}%",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFFF59E0B)
                )
            }

            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = stringResource(StringKeyTriplePillar.PILLAR_ASHTAKAVARGA_DESC),
                style = MaterialTheme.typography.bodySmall,
                color = colors.TextSecondary
            )
            Spacer(modifier = Modifier.height(12.dp))

            DetailRow(
                label = StringResources.get(StringKeyTriplePillar.BAV_SCORE, language),
                value = "${ashtakavarga.dashaLordBav}/8",
                colors = colors
            )
            DetailRow(
                label = StringResources.get(StringKeyTriplePillar.SAV_SCORE, language),
                value = "${ashtakavarga.savInTransitSign}/56",
                colors = colors
            )

            val binduLabel = when (ashtakavarga.bavStrength) {
                BinduStrength.HIGH -> StringResources.get(StringKeyTriplePillar.HIGH_BINDU, language)
                BinduStrength.MEDIUM -> StringResources.get(StringKeyTriplePillar.MEDIUM_BINDU, language)
                BinduStrength.LOW -> StringResources.get(StringKeyTriplePillar.LOW_BINDU, language)
            }
            DetailRow(
                label = StringResources.get(StringKeyTriplePillar.BINDU_STRENGTH, language),
                value = binduLabel,
                colors = colors
            )

            val savThresholdLabel = if (ashtakavarga.savAboveThreshold)
                StringResources.get(StringKeyTriplePillar.SAV_ABOVE_THRESHOLD, language)
            else
                StringResources.get(StringKeyTriplePillar.SAV_BELOW_THRESHOLD, language)
            DetailRow(
                label = StringResources.get(StringKeyTriplePillar.SAV_THRESHOLD, language),
                value = savThresholdLabel,
                colors = colors
            )
        }
    }
}

// ============================================================================
// TAB 4: FORECAST
// ============================================================================

@Composable
private fun ForecastTab(result: TriplePillarResult, language: Language) {
    val colors = AppTheme.current

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = androidx.compose.foundation.layout.PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Life Area Impacts
        item {
            SectionHeader(stringResource(StringKeyTriplePillar.HEADER_LIFE_AREA_IMPACT))
        }
        items(result.lifeAreaImpacts) { impact ->
            LifeAreaImpactRow(impact, language)
        }

        // Peak Windows
        if (result.peakWindows.isNotEmpty()) {
            item {
                SectionHeader(stringResource(StringKeyTriplePillar.HEADER_PEAK_WINDOWS))
            }
            items(result.peakWindows) { window ->
                OpportunityWindowCard(window, language, isPeak = true)
            }
        }

        // Caution Periods
        if (result.cautionPeriods.isNotEmpty()) {
            item {
                SectionHeader(stringResource(StringKeyTriplePillar.HEADER_RISK_WINDOWS))
            }
            items(result.cautionPeriods) { window ->
                OpportunityWindowCard(window, language, isPeak = false)
            }
        }

        // Classical reference
        item {
            ClassicalReferenceCard(result, language)
        }

        item { Spacer(modifier = Modifier.height(32.dp)) }
    }
}

// ============================================================================
// SHARED COMPONENTS
// ============================================================================

@Composable
private fun SectionHeader(text: String) {
    val colors = AppTheme.current
    Text(
        text = text,
        style = MaterialTheme.typography.titleSmall,
        fontWeight = FontWeight.SemiBold,
        color = colors.TextPrimary,
        modifier = Modifier.padding(vertical = 4.dp)
    )
}

@Composable
private fun DetailRow(label: String, value: String, colors: com.astro.storm.ui.theme.AppThemeColors) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 3.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodySmall,
            color = colors.TextSecondary
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodySmall,
            color = colors.TextPrimary,
            fontWeight = FontWeight.Medium
        )
    }
}

@Composable
private fun LifeAreaImpactRow(impact: LifeAreaImpact, language: Language) {
    val colors = AppTheme.current
    val qualityColor = getQualityColor(impact.qualityLevel)

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(10.dp))
            .background(colors.CardBackground)
            .padding(12.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = if (language == Language.NEPALI) impact.area.ne else impact.area.en,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Medium,
                color = colors.TextPrimary
            )
        }

        Row(verticalAlignment = Alignment.CenterVertically) {
            LinearProgressIndicator(
                progress = { impact.impactScore.toFloat().coerceIn(0f, 1f) },
                modifier = Modifier
                    .width(60.dp)
                    .height(4.dp)
                    .clip(RoundedCornerShape(2.dp)),
                color = qualityColor,
                trackColor = qualityColor.copy(alpha = 0.15f)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "${(impact.impactScore * 100).toInt()}%",
                style = MaterialTheme.typography.bodySmall,
                fontWeight = FontWeight.Bold,
                color = qualityColor
            )
        }
    }
}

@Composable
private fun OpportunityWindowCard(
    window: OpportunityWindow,
    language: Language,
    isPeak: Boolean
) {
    val colors = AppTheme.current
    val accentColor = if (isPeak) Color(0xFF22C55E) else Color(0xFFF59E0B)
    val dateFormatter = DateTimeFormatter.ofPattern("MMM yyyy")

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = colors.CardBackground),
        shape = RoundedCornerShape(12.dp)
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.Top
        ) {
            Icon(
                imageVector = if (isPeak) Icons.Default.Star else Icons.Default.Warning,
                contentDescription = null,
                tint = accentColor,
                modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.width(12.dp))
            Column {
                Text(
                    text = "${window.startDate.format(dateFormatter)} — ${window.endDate.format(dateFormatter)}",
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = colors.TextPrimary
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = if (language == Language.NEPALI) window.descriptionNe else window.description,
                    style = MaterialTheme.typography.bodySmall,
                    color = colors.TextSecondary
                )

                if (window.lifeAreas.isNotEmpty()) {
                    Spacer(modifier = Modifier.height(6.dp))
                    Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                        window.lifeAreas.take(3).forEach { area ->
                            Text(
                                text = if (language == Language.NEPALI) area.ne else area.en,
                                style = MaterialTheme.typography.labelSmall,
                                color = accentColor,
                                modifier = Modifier
                                    .clip(RoundedCornerShape(4.dp))
                                    .background(accentColor.copy(alpha = 0.1f))
                                    .padding(horizontal = 6.dp, vertical = 2.dp),
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun ClassicalReferenceCard(result: TriplePillarResult, language: Language) {
    val colors = AppTheme.current

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = colors.AccentPrimary.copy(alpha = 0.08f)
        ),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = stringResource(StringKeyTriplePillar.HEADER_CLASSICAL_REFERENCE),
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.SemiBold,
                color = colors.AccentPrimary
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = if (language == Language.NEPALI) result.classicalReferenceNe else result.classicalReference,
                style = MaterialTheme.typography.bodySmall,
                color = colors.TextSecondary,
                lineHeight = 18.sp
            )
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = StringResources.get(StringKeyTriplePillar.REF_SYNTHESIS_METHOD, language),
                style = MaterialTheme.typography.labelSmall,
                color = colors.TextSecondary.copy(alpha = 0.7f),
                lineHeight = 16.sp
            )
        }
    }
}

// ============================================================================
// UTILITY
// ============================================================================

private fun getQualityColor(quality: QualityLevel): Color {
    return when (quality) {
        QualityLevel.EXCEPTIONAL -> Color(0xFF10B981) // Emerald
        QualityLevel.EXCELLENT -> Color(0xFF22C55E)   // Green
        QualityLevel.VERY_GOOD -> Color(0xFF84CC16)   // Lime
        QualityLevel.GOOD -> Color(0xFFA3E635)        // Yellow-green
        QualityLevel.ABOVE_AVERAGE -> Color(0xFFEAB308) // Yellow
        QualityLevel.AVERAGE -> Color(0xFFF59E0B)     // Amber
        QualityLevel.BELOW_AVERAGE -> Color(0xFFF97316) // Orange
        QualityLevel.CHALLENGING -> Color(0xFFEF4444)  // Red
        QualityLevel.DIFFICULT -> Color(0xFFDC2626)    // Dark Red
    }
}
