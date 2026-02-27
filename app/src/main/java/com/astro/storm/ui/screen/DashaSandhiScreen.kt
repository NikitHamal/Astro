package com.astro.storm.ui.screen

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
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
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.outlined.CalendarMonth
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.Schedule
import androidx.compose.material.icons.outlined.TipsAndUpdates
import androidx.compose.material.icons.outlined.Warning
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.astro.storm.data.localization.LocalLanguage
import com.astro.storm.core.common.Language
import com.astro.storm.core.common.StringKey
import com.astro.storm.core.common.StringKeyDosha
import com.astro.storm.core.common.StringKeyMatch
import com.astro.storm.core.common.StringKeyUICommon
import com.astro.storm.core.common.StringKeyUIExtra
import com.astro.storm.core.common.getLocalizedName
import com.astro.storm.data.localization.stringResource
import com.astro.storm.core.model.VedicChart
import com.astro.storm.ephemeris.DashaSandhiAnalyzer
import com.astro.storm.ephemeris.DashaCalculator
import com.astro.storm.ui.components.common.ModernPillTabRow
import com.astro.storm.ui.components.common.NeoVedicEmptyState
import com.astro.storm.ui.components.common.NeoVedicPageHeader
import com.astro.storm.ui.components.common.TabItem
import com.astro.storm.ui.components.common.vedicCornerMarkers
import com.astro.storm.ui.theme.AppTheme
import com.astro.storm.ui.theme.CinzelDecorativeFamily
import com.astro.storm.ui.theme.NeoVedicFontSizes
import com.astro.storm.ui.theme.NeoVedicTokens
import com.astro.storm.ui.theme.PoppinsFontFamily
import com.astro.storm.ui.theme.SpaceGroteskFamily
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import java.time.format.DateTimeFormatter
import java.util.Locale

/**
 * Dasha Sandhi Screen - Displays planetary period transition analysis
 *
 * Shows detailed analysis of Dasha Sandhi (junction points between planetary periods)
 * including current sandhi, upcoming transitions, and calendar view.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashaSandhiScreen(
    chart: VedicChart?,
    onBack: () -> Unit
) {
    val language = LocalLanguage.current
    var selectedTab by rememberSaveable { mutableIntStateOf(0) }
    var isCalculating by remember { mutableStateOf(true) }
    var sandhiAnalysis by remember { mutableStateOf<DashaSandhiAnalyzer.CompleteSandhiAnalysis?>(null) }
    var showInfoDialog by remember { mutableStateOf(false) }

    val tabs = listOf(
        stringResource(StringKeyDosha.UI_OVERVIEW),
        stringResource(StringKeyDosha.UI_CURRENT),
        stringResource(StringKeyDosha.UI_UPCOMING),
        stringResource(StringKeyDosha.UI_CALENDAR)
    )

    // Calculate Dasha Sandhi analysis
    LaunchedEffect(chart) {
        if (chart == null) {
            isCalculating = false
            return@LaunchedEffect
        }
        isCalculating = true
        delay(300) // Brief delay for smooth transition
        try {
            sandhiAnalysis = withContext(Dispatchers.Default) {
                val dashaTimeline = DashaCalculator.calculateDashaTimeline(chart)
                DashaSandhiAnalyzer.analyzeCompleteSandhis(
                    chart = chart,
                    dashaTimeline = dashaTimeline,
                    analysisDate = null,
                    lookAheadMonths = 24
                )
            }
        } catch (e: Exception) {
            // Handle calculation error
        }
        isCalculating = false
    }

    Scaffold(
        containerColor = AppTheme.ScreenBackground,
        topBar = {
            DashaSandhiTopBar(
                chartName = chart?.birthData?.name ?: stringResource(StringKeyMatch.MISC_UNKNOWN),
                isCalculating = isCalculating,
                onBack = onBack,
                onInfoClick = { showInfoDialog = true }
            )
        }
    ) { paddingValues ->
        when {
            isCalculating -> {
                LoadingContent(modifier = Modifier.padding(paddingValues))
            }
            chart == null || sandhiAnalysis == null -> {
                EmptyContent(modifier = Modifier.padding(paddingValues))
            }
            else -> {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                        .background(AppTheme.ScreenBackground),
                    contentPadding = PaddingValues(bottom = 32.dp)
                ) {
                    // Tab selector
                    item {
                        TabSelector(
                            tabs = tabs,
                            selectedTab = selectedTab,
                            onTabSelected = { selectedTab = it }
                        )
                    }

                    // Content based on selected tab
                    when (selectedTab) {
                        0 -> {
                            item { OverviewSection(sandhiAnalysis!!) }
                        }
                        1 -> {
                            item { CurrentSandhiSection(sandhiAnalysis!!) }
                        }
                        2 -> {
                            item { UpcomingSandhiSection(sandhiAnalysis!!) }
                        }
                        3 -> {
                            item { CalendarSection(sandhiAnalysis!!) }
                        }
                    }
                }
            }
        }
    }

    // Info Dialog
    if (showInfoDialog) {
        InfoDialog(
            title = stringResource(StringKeyUIExtra.SANDHI_ABOUT_TITLE),
            content = buildString {
                append(stringResource(StringKeyUIExtra.SANDHI_ABOUT_DESC))
                append("\n\n")
                append(stringResource(StringKeyUIExtra.SANDHI_DESC_P1))
                append("\n")
                append(stringResource(StringKeyUIExtra.SANDHI_DESC_P1_ITEM1))
                append("\n")
                append(stringResource(StringKeyUIExtra.SANDHI_DESC_P1_ITEM2))
                append("\n")
                append(stringResource(StringKeyUIExtra.SANDHI_DESC_P1_ITEM3))
                append("\n")
                append(stringResource(StringKeyUIExtra.SANDHI_DESC_P1_ITEM4))
                append("\n\n")
                append(stringResource(StringKeyUIExtra.SANDHI_DESC_P2))
                append("\n")
                append(stringResource(StringKeyUIExtra.SANDHI_DESC_P2_ITEM1))
                append("\n")
                append(stringResource(StringKeyUIExtra.SANDHI_DESC_P2_ITEM2))
                append("\n")
                append(stringResource(StringKeyUIExtra.SANDHI_DESC_P2_ITEM3))
                append("\n\n")
                append(stringResource(StringKeyUIExtra.SANDHI_DESC_FOOTER))
            },
            onDismiss = { showInfoDialog = false }
        )
    }
}

@Composable
private fun DashaSandhiTopBar(
    chartName: String,
    isCalculating: Boolean,
    onBack: () -> Unit,
    onInfoClick: () -> Unit
) {
    val subtitle = if (isCalculating) {
        stringResource(StringKey.DASHA_CALCULATING)
    } else {
        chartName
    }
    NeoVedicPageHeader(
        title = stringResource(StringKeyDosha.SANDHI_SCREEN_TITLE),
        subtitle = subtitle,
        onBack = onBack,
        actionIcon = Icons.Outlined.Info,
        onAction = onInfoClick
    )
}

@Composable
private fun TabSelector(
    tabs: List<String>,
    selectedTab: Int,
    onTabSelected: (Int) -> Unit
) {
    ModernPillTabRow(
        tabs = tabs.mapIndexed { index, title ->
            TabItem(
                title = title,
                accentColor = if (selectedTab == index) AppTheme.AccentPrimary else Color.Unspecified
            )
        },
        selectedIndex = selectedTab,
        onTabSelected = onTabSelected,
        modifier = Modifier.padding(
            horizontal = com.astro.storm.ui.theme.NeoVedicTokens.ScreenPadding,
            vertical = com.astro.storm.ui.theme.NeoVedicTokens.SpaceXS
        )
    )
}

@Composable
private fun OverviewSection(analysis: DashaSandhiAnalyzer.CompleteSandhiAnalysis) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = com.astro.storm.ui.theme.NeoVedicTokens.ScreenPadding),
        verticalArrangement = Arrangement.spacedBy(com.astro.storm.ui.theme.NeoVedicTokens.SpaceMD)
    ) {
        // Volatility Score Card
        VolatilityScoreCard(score = analysis.overallVolatilityScore)

        // Quick Stats
        QuickStatsRow(analysis)

        // General Guidance
        GuidanceCard(guidance = analysis.generalGuidance)

        // Current Status Summary
        analysis.currentSandhi?.let { currentSandhi ->
            CurrentStatusCard(sandhi = currentSandhi)
        }
    }
}

@Composable
private fun VolatilityScoreCard(score: Int) {
    val scoreColor = getVolatilityColor(score)
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .vedicCornerMarkers(color = scoreColor),
        color = AppTheme.CardBackground,
        shape = RoundedCornerShape(NeoVedicTokens.CardCornerRadius),
        border = androidx.compose.foundation.BorderStroke(NeoVedicTokens.BorderWidth, AppTheme.BorderColor)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(NeoVedicTokens.ScreenPadding),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = stringResource(StringKeyDosha.SANDHI_VOLATILITY),
                fontSize = NeoVedicFontSizes.S14,
                fontWeight = FontWeight.SemiBold,
                color = AppTheme.TextPrimary,
                fontFamily = SpaceGroteskFamily
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Circular progress indicator
            Box(
                modifier = Modifier.size(120.dp),
                contentAlignment = Alignment.Center
            ) {
                val progress by animateFloatAsState(
                    targetValue = score / 100f,
                    animationSpec = tween(1000),
                    label = "volatilityProgress"
                )
                CircularProgressIndicator(
                    progress = { progress },
                    modifier = Modifier.fillMaxSize(),
                    color = getVolatilityColor(score),
                    trackColor = AppTheme.DividerColor,
                    strokeWidth = 10.dp,
                    strokeCap = StrokeCap.Round
                )
                val language = LocalLanguage.current
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    val scoreText = if (language == Language.NEPALI) com.astro.storm.core.common.BikramSambatConverter.toNepaliNumerals(score) else score.toString()
                    Text(
                        text = scoreText + stringResource(StringKeyUIExtra.PERCENT),
                        fontSize = NeoVedicFontSizes.S28,
                        fontWeight = FontWeight.Bold,
                        color = getVolatilityColor(score),
                        fontFamily = CinzelDecorativeFamily
                    )
                    Text(
                        text = getVolatilityLabel(score),
                        fontSize = NeoVedicFontSizes.S11,
                        color = AppTheme.TextMuted,
                        fontFamily = PoppinsFontFamily
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = stringResource(StringKeyDosha.SANDHI_VOLATILITY_DESC),
                fontSize = NeoVedicFontSizes.S13,
                color = AppTheme.TextMuted,
                textAlign = TextAlign.Center,
                fontFamily = PoppinsFontFamily,
                lineHeight = 20.sp
            )
        }
    }
}

@Composable
private fun QuickStatsRow(analysis: DashaSandhiAnalyzer.CompleteSandhiAnalysis) {
    val language = LocalLanguage.current
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        StatCard(
            title = stringResource(StringKeyDosha.SANDHI_ACTIVE),
            value = if (analysis.currentSandhi != null) stringResource(StringKeyUIExtra.LABEL_YES) else stringResource(StringKeyUIExtra.LABEL_NO),
            color = if (analysis.currentSandhi != null) AppTheme.WarningColor else AppTheme.SuccessColor,
            modifier = Modifier.weight(1f)
        )
        val upcoming = if (language == Language.NEPALI) com.astro.storm.core.common.BikramSambatConverter.toNepaliNumerals(analysis.upcomingSandhis.size) else analysis.upcomingSandhis.size.toString()
        StatCard(
            title = stringResource(StringKeyDosha.SANDHI_UPCOMING),
            value = upcoming,
            color = AppTheme.AccentPrimary,
            modifier = Modifier.weight(1f)
        )
        val recent = if (language == Language.NEPALI) com.astro.storm.core.common.BikramSambatConverter.toNepaliNumerals(analysis.recentPastSandhis.size) else analysis.recentPastSandhis.size.toString()
        StatCard(
            title = stringResource(StringKeyDosha.SANDHI_RECENT),
            value = recent,
            color = AppTheme.TextMuted,
            modifier = Modifier.weight(1f)
        )
    }
}

@Composable
private fun StatCard(
    title: String,
    value: String,
    color: Color,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier,
        color = AppTheme.CardBackground,
        shape = RoundedCornerShape(NeoVedicTokens.CardCornerRadius),
        border = androidx.compose.foundation.BorderStroke(NeoVedicTokens.BorderWidth, AppTheme.BorderColor)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = value,
                fontSize = NeoVedicFontSizes.S20,
                fontWeight = FontWeight.Bold,
                color = color,
                fontFamily = SpaceGroteskFamily
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = title,
                fontSize = NeoVedicFontSizes.S11,
                color = AppTheme.TextMuted,
                fontFamily = PoppinsFontFamily
            )
        }
    }
}

@Composable
private fun GuidanceCard(guidance: String) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = AppTheme.CardBackground,
        shape = RoundedCornerShape(NeoVedicTokens.CardCornerRadius),
        border = androidx.compose.foundation.BorderStroke(NeoVedicTokens.BorderWidth, AppTheme.BorderColor)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .background(AppTheme.AccentGold.copy(alpha = 0.15f), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Outlined.TipsAndUpdates,
                    contentDescription = null,
                    tint = AppTheme.AccentGold,
                    modifier = Modifier.size(20.dp)
                )
            }
            Column {
                Text(
                    text = stringResource(StringKeyDosha.SANDHI_GUIDANCE),
                    fontSize = NeoVedicFontSizes.S14,
                    fontWeight = FontWeight.SemiBold,
                    color = AppTheme.TextPrimary,
                    fontFamily = CinzelDecorativeFamily
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = guidance,
                    fontSize = NeoVedicFontSizes.S13,
                    color = AppTheme.TextSecondary,
                    lineHeight = 20.sp,
                    fontFamily = PoppinsFontFamily
                )
            }
        }
    }
}

@Composable
private fun CurrentStatusCard(sandhi: DashaSandhiAnalyzer.SandhiAnalysis) {
    val language = LocalLanguage.current
    val monthDayYearFormatter = sandhiMonthDayYearFormatter()
    val intensityColor = getSandhiIntensityColor(sandhi.intensity)

    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = intensityColor.copy(alpha = 0.08f),
        shape = RoundedCornerShape(NeoVedicTokens.CardCornerRadius),
        border = androidx.compose.foundation.BorderStroke(NeoVedicTokens.BorderWidth, intensityColor.copy(alpha = 0.2f))
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Icon(
                    imageVector = Icons.Outlined.Warning,
                    contentDescription = null,
                    tint = intensityColor,
                    modifier = Modifier.size(20.dp)
                )
                Text(
                    text = stringResource(StringKeyDosha.SANDHI_CURRENT_ACTIVE),
                    fontSize = NeoVedicFontSizes.S14,
                    fontWeight = FontWeight.SemiBold,
                    color = intensityColor,
                    fontFamily = SpaceGroteskFamily
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = sandhi.sandhi.fromPlanet.getLocalizedName(language) + stringResource(StringKeyUIExtra.ARROW) + sandhi.sandhi.toPlanet.getLocalizedName(language),
                fontSize = NeoVedicFontSizes.S18,
                fontWeight = FontWeight.Medium,
                color = AppTheme.TextPrimary,
                fontFamily = CinzelDecorativeFamily
            )

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "${stringResource(StringKeyDosha.SANDHI_INTENSITY)} ${sandhi.intensity.displayName}",
                    fontSize = NeoVedicFontSizes.S12,
                    color = AppTheme.TextSecondary,
                    fontFamily = PoppinsFontFamily
                )
                Text(
                    text = "${stringResource(StringKeyDosha.SANDHI_ENDS)} ${sandhi.sandhi.sandhiEndDate.format(monthDayYearFormatter)}",
                    fontSize = NeoVedicFontSizes.S12,
                    color = AppTheme.TextSecondary,
                    fontFamily = PoppinsFontFamily
                )
            }
        }
    }
}

@Composable
private fun CurrentSandhiSection(analysis: DashaSandhiAnalyzer.CompleteSandhiAnalysis) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = com.astro.storm.ui.theme.NeoVedicTokens.ScreenPadding),
        verticalArrangement = Arrangement.spacedBy(com.astro.storm.ui.theme.NeoVedicTokens.SpaceMD)
    ) {
        if (analysis.currentSandhi != null) {
            SandhiDetailCard(
                sandhi = analysis.currentSandhi,
                isExpanded = true
            )
        } else {
            // No current sandhi
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = AppTheme.CardBackground),
                shape = RoundedCornerShape(com.astro.storm.ui.theme.NeoVedicTokens.ElementCornerRadius)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(com.astro.storm.ui.theme.NeoVedicTokens.SpaceLG),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Schedule,
                        contentDescription = null,
                        tint = AppTheme.SuccessColor,
                        modifier = Modifier.size(48.dp)
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = stringResource(StringKeyDosha.SANDHI_NO_ACTIVE),
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold,
                        color = AppTheme.TextPrimary
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = stringResource(StringKeyDosha.SANDHI_NO_ACTIVE_DESC),
                        style = MaterialTheme.typography.bodyMedium,
                        color = AppTheme.TextMuted,
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun SandhiDetailCard(
    sandhi: DashaSandhiAnalyzer.SandhiAnalysis,
    isExpanded: Boolean = false
) {
    val language = LocalLanguage.current
    val monthDayYearFormatter = sandhiMonthDayYearFormatter()
    var expanded by remember { mutableStateOf(isExpanded) }
    val intensityColor = getSandhiIntensityColor(sandhi.intensity)

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .animateContentSize(),
        color = AppTheme.CardBackground,
        shape = RoundedCornerShape(NeoVedicTokens.CardCornerRadius),
        border = androidx.compose.foundation.BorderStroke(NeoVedicTokens.BorderWidth, AppTheme.BorderColor)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            // Header
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { expanded = !expanded },
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    // Intensity indicator
                    Box(
                        modifier = Modifier
                            .size(12.dp)
                            .clip(CircleShape)
                            .background(intensityColor)
                    )
                    Column {
                        Text(
                            text = sandhi.sandhi.fromPlanet.getLocalizedName(language) + stringResource(StringKeyUIExtra.ARROW) + sandhi.sandhi.toPlanet.getLocalizedName(language),
                            fontSize = NeoVedicFontSizes.S15,
                            fontWeight = FontWeight.SemiBold,
                            color = AppTheme.TextPrimary,
                            fontFamily = CinzelDecorativeFamily
                        )
                        Text(
                            text = sandhi.transitionType.name.replace("_", " "),
                            fontSize = NeoVedicFontSizes.S12,
                            color = AppTheme.TextMuted,
                            fontFamily = PoppinsFontFamily
                        )
                    }
                }
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Surface(
                        shape = RoundedCornerShape(NeoVedicTokens.ChipCornerRadius),
                        color = intensityColor.copy(alpha = 0.15f)
                    ) {
                        Text(
                            text = sandhi.intensity.displayName,
                            fontSize = NeoVedicFontSizes.S10,
                            fontWeight = FontWeight.Bold,
                            color = intensityColor,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                            fontFamily = SpaceGroteskFamily
                        )
                    }
                    Icon(
                        imageVector = if (expanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                        contentDescription = null,
                        tint = AppTheme.TextMuted
                    )
                }
            }

            // Date range
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text(
                    text = "${stringResource(StringKeyDosha.UI_START)} ${sandhi.sandhi.sandhiStartDate.format(monthDayYearFormatter)}",
                    fontSize = NeoVedicFontSizes.S11,
                    color = AppTheme.TextSecondary,
                    fontFamily = SpaceGroteskFamily
                )
                Text(
                    text = "${stringResource(StringKeyDosha.UI_END)} ${sandhi.sandhi.sandhiEndDate.format(monthDayYearFormatter)}",
                    fontSize = NeoVedicFontSizes.S11,
                    color = AppTheme.TextSecondary,
                    fontFamily = SpaceGroteskFamily
                )
            }

            // Expanded content
            AnimatedVisibility(
                visible = expanded,
                enter = expandVertically() + fadeIn(),
                exit = shrinkVertically() + fadeOut()
            ) {
                Column(modifier = Modifier.padding(top = 16.dp)) {
                    HorizontalDivider(color = AppTheme.DividerColor)
                    Spacer(modifier = Modifier.height(16.dp))

                    // Predictions
                    Text(
                        text = stringResource(StringKeyDosha.SANDHI_PREDICTIONS),
                        fontSize = NeoVedicFontSizes.S14,
                        fontWeight = FontWeight.SemiBold,
                        color = AppTheme.TextPrimary,
                        fontFamily = CinzelDecorativeFamily
                    )
                    Spacer(modifier = Modifier.height(8.dp))

                    // Key dates
                    sandhi.predictions.keyDates.forEach { keyDate ->
                        KeyDateItem(keyDate = keyDate)
                    }

                    // Life area impacts
                    if (sandhi.affectedLifeAreas.isNotEmpty()) {
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = stringResource(StringKeyDosha.SANDHI_IMPACTS),
                            fontSize = NeoVedicFontSizes.S14,
                            fontWeight = FontWeight.SemiBold,
                            color = AppTheme.TextPrimary,
                            fontFamily = CinzelDecorativeFamily
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        FlowRow(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            sandhi.affectedLifeAreas.forEach { impact ->
                                LifeAreaChip(impact = impact)
                            }
                        }
                    }

                    // Remedies
                    if (sandhi.remedies.isNotEmpty()) {
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = stringResource(StringKeyDosha.SANDHI_REMEDIES),
                            fontSize = NeoVedicFontSizes.S14,
                            fontWeight = FontWeight.SemiBold,
                            color = AppTheme.TextPrimary,
                            fontFamily = CinzelDecorativeFamily
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        sandhi.remedies.take(3).forEach { remedy ->
                            RemedyItem(remedy = remedy)
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun KeyDateItem(keyDate: DashaSandhiAnalyzer.KeyDatePrediction) {
    val monthDayFormatter = sandhiMonthDayFormatter()
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalAlignment = Alignment.Top
    ) {
        Text(
            text = keyDate.date.format(monthDayFormatter),
            fontSize = NeoVedicFontSizes.S12,
            fontWeight = FontWeight.Medium,
            color = AppTheme.AccentPrimary,
            modifier = Modifier.width(50.dp),
            fontFamily = SpaceGroteskFamily
        )
        Column {
            Text(
                text = keyDate.event,
                fontSize = NeoVedicFontSizes.S13,
                fontWeight = FontWeight.Medium,
                color = AppTheme.TextPrimary,
                fontFamily = PoppinsFontFamily
            )
            Text(
                text = keyDate.significance,
                fontSize = NeoVedicFontSizes.S11,
                color = AppTheme.TextMuted,
                fontFamily = PoppinsFontFamily
            )
        }
    }
}

@Composable
private fun LifeAreaChip(impact: DashaSandhiAnalyzer.LifeAreaImpact) {
    val impactColor = getImpactColor(impact.impactLevel)
    Surface(
        shape = RoundedCornerShape(NeoVedicTokens.ChipCornerRadius),
        color = impactColor.copy(alpha = 0.15f)
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(8.dp)
                    .clip(CircleShape)
                    .background(impactColor)
            )
            Text(
                text = impact.area,
                fontSize = NeoVedicFontSizes.S11,
                fontWeight = FontWeight.Medium,
                color = AppTheme.TextPrimary,
                fontFamily = PoppinsFontFamily
            )
        }
    }
}

@Composable
private fun RemedyItem(remedy: DashaSandhiAnalyzer.SandhiRemedy) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(NeoVedicTokens.ElementCornerRadius),
        color = AppTheme.CardBackgroundElevated
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = remedy.type.name.replace("_", " "),
                    fontSize = NeoVedicFontSizes.S11,
                    fontWeight = FontWeight.Bold,
                    color = AppTheme.AccentPrimary,
                    fontFamily = SpaceGroteskFamily
                )
            }
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = remedy.description,
                fontSize = NeoVedicFontSizes.S12,
                color = AppTheme.TextSecondary,
                fontFamily = PoppinsFontFamily
            )
        }
    }
    Spacer(modifier = Modifier.height(8.dp))
}

@Composable
private fun UpcomingSandhiSection(analysis: DashaSandhiAnalyzer.CompleteSandhiAnalysis) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = NeoVedicTokens.ScreenPadding),
        verticalArrangement = Arrangement.spacedBy(NeoVedicTokens.SpaceMD)
    ) {
        if (analysis.upcomingSandhis.isEmpty()) {
            Surface(
                modifier = Modifier.fillMaxWidth(),
                color = AppTheme.CardBackground,
                shape = RoundedCornerShape(NeoVedicTokens.CardCornerRadius),
                border = androidx.compose.foundation.BorderStroke(NeoVedicTokens.BorderWidth, AppTheme.BorderColor)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = stringResource(StringKeyDosha.SANDHI_NO_UPCOMING),
                        fontSize = NeoVedicFontSizes.S14,
                        color = AppTheme.TextMuted,
                        textAlign = TextAlign.Center,
                        fontFamily = PoppinsFontFamily
                    )
                }
            }
        } else {
            analysis.upcomingSandhis.forEach { sandhi ->
                SandhiDetailCard(sandhi = sandhi)
            }
        }
    }
}

@Composable
private fun CalendarSection(analysis: DashaSandhiAnalyzer.CompleteSandhiAnalysis) {
    val monthYearFormatter = sandhiMonthYearFormatter()
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = NeoVedicTokens.ScreenPadding),
        verticalArrangement = Arrangement.spacedBy(NeoVedicTokens.SpaceMD)
    ) {
        if (analysis.sandhiCalendar.isEmpty()) {
            Surface(
                modifier = Modifier.fillMaxWidth(),
                color = AppTheme.CardBackground,
                shape = RoundedCornerShape(NeoVedicTokens.CardCornerRadius),
                border = androidx.compose.foundation.BorderStroke(NeoVedicTokens.BorderWidth, AppTheme.BorderColor)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        imageVector = Icons.Outlined.CalendarMonth,
                        contentDescription = null,
                        tint = AppTheme.TextMuted,
                        modifier = Modifier.size(48.dp)
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = stringResource(StringKeyDosha.SANDHI_NO_CALENDAR),
                        fontSize = NeoVedicFontSizes.S14,
                        color = AppTheme.TextMuted,
                        textAlign = TextAlign.Center,
                        fontFamily = PoppinsFontFamily
                    )
                }
            }
        } else {
            // Group by month
            val groupedByMonth = analysis.sandhiCalendar.groupBy { entry ->
                entry.date.format(monthYearFormatter)
            }

            groupedByMonth.forEach { (month, entries) ->
                Text(
                    text = month,
                    fontSize = NeoVedicFontSizes.S14,
                    fontWeight = FontWeight.SemiBold,
                    color = AppTheme.TextPrimary,
                    modifier = Modifier.padding(vertical = 8.dp),
                    fontFamily = SpaceGroteskFamily
                )

                entries.forEach { entry ->
                    CalendarEntryCard(entry = entry)
                }
            }
        }
    }
}

@Composable
private fun CalendarEntryCard(entry: DashaSandhiAnalyzer.SandhiCalendarEntry) {
    val dayFormatter = sandhiDayFormatter()
    val weekdayFormatter = sandhiWeekdayFormatter()
    val intensityColor = getSandhiIntensityColor(entry.intensity)

    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = intensityColor.copy(alpha = 0.08f),
        shape = RoundedCornerShape(NeoVedicTokens.CardCornerRadius),
        border = androidx.compose.foundation.BorderStroke(NeoVedicTokens.BorderWidth, intensityColor.copy(alpha = 0.2f))
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Date badge
            val language = LocalLanguage.current
            Surface(
                shape = RoundedCornerShape(NeoVedicTokens.ElementCornerRadius),
                color = AppTheme.CardBackground
            ) {
                Column(
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    val dayStr = entry.date.format(dayFormatter)
                    Text(
                        text = if (language == Language.NEPALI) com.astro.storm.core.common.BikramSambatConverter.toNepaliNumerals(dayStr.toInt()) else dayStr,
                        fontSize = NeoVedicFontSizes.S16,
                        fontWeight = FontWeight.Bold,
                        color = AppTheme.TextPrimary,
                        fontFamily = SpaceGroteskFamily
                    )
                    Text(
                        text = entry.date.format(weekdayFormatter),
                        fontSize = NeoVedicFontSizes.S11,
                        color = AppTheme.TextMuted,
                        fontFamily = PoppinsFontFamily
                    )
                }
            }

            // Event details
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = entry.sandhiType,
                    fontSize = NeoVedicFontSizes.S14,
                    fontWeight = FontWeight.Medium,
                    color = AppTheme.TextPrimary,
                    fontFamily = PoppinsFontFamily
                )
                Text(
                    text = entry.fromPlanet.displayName + stringResource(StringKeyUIExtra.ARROW) + entry.toPlanet.displayName,
                    fontSize = NeoVedicFontSizes.S12,
                    color = AppTheme.TextSecondary,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    fontFamily = PoppinsFontFamily
                )
            }

            // Intensity badge
            Surface(
                shape = RoundedCornerShape(NeoVedicTokens.ChipCornerRadius),
                color = intensityColor.copy(alpha = 0.2f)
            ) {
                Text(
                    text = entry.intensity.displayName,
                    fontSize = NeoVedicFontSizes.S10,
                    fontWeight = FontWeight.Bold,
                    color = intensityColor,
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                    fontFamily = SpaceGroteskFamily
                )
            }
        }
    }
    Spacer(modifier = Modifier.height(8.dp))
}

@Composable
private fun LoadingContent(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            CircularProgressIndicator(color = AppTheme.AccentPrimary)
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = stringResource(StringKeyUIExtra.SANDHI_ANALYZING),
                style = MaterialTheme.typography.bodyMedium,
                color = AppTheme.TextMuted
            )
        }
    }
}

@Composable
private fun EmptyContent(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        NeoVedicEmptyState(
            title = stringResource(StringKeyDosha.UI_NO_CHART_DATA),
            subtitle = stringResource(StringKeyDosha.SANDHI_NO_CHART_DESC),
            icon = Icons.Outlined.Schedule
        )
    }
}

@Composable
private fun InfoDialog(
    title: String,
    content: String,
    onDismiss: () -> Unit
) {
    androidx.compose.material3.AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
        },
        text = {
            Text(
                text = content,
                style = MaterialTheme.typography.bodyMedium,
                color = AppTheme.TextSecondary,
                lineHeight = 22.sp
            )
        },
        confirmButton = {
            androidx.compose.material3.TextButton(onClick = onDismiss) {
                Text(stringResource(StringKeyDosha.BTN_GOT_IT), color = AppTheme.AccentPrimary)
            }
        },
        containerColor = AppTheme.CardBackground
    )
}

// Helper functions
@Composable
private fun getVolatilityColor(score: Int): Color {
    return when {
        score >= 80 -> AppTheme.ErrorColor
        score >= 60 -> AppTheme.WarningColor
        score >= 40 -> AppTheme.AccentGold
        score >= 20 -> AppTheme.AccentTeal
        else -> AppTheme.SuccessColor
    }
}

@Composable
private fun getVolatilityLabel(score: Int): String {
    return when {
        score >= 80 -> stringResource(StringKeyUIExtra.SANDHI_VOL_VERY_HIGH)
        score >= 60 -> stringResource(StringKeyUIExtra.SANDHI_VOL_HIGH)
        score >= 40 -> stringResource(StringKeyUIExtra.SANDHI_VOL_MODERATE)
        score >= 20 -> stringResource(StringKeyUIExtra.SANDHI_VOL_LOW)
        else -> stringResource(StringKeyUIExtra.SANDHI_VOL_MINIMAL)
    }
}

@Composable
private fun getSandhiIntensityColor(intensity: DashaSandhiAnalyzer.SandhiIntensity): Color {
    return when (intensity) {
        DashaSandhiAnalyzer.SandhiIntensity.CRITICAL -> AppTheme.ErrorColor
        DashaSandhiAnalyzer.SandhiIntensity.HIGH -> com.astro.storm.ui.theme.WarningDark
        DashaSandhiAnalyzer.SandhiIntensity.MODERATE -> AppTheme.WarningColor
        DashaSandhiAnalyzer.SandhiIntensity.MILD -> AppTheme.SuccessColor
        DashaSandhiAnalyzer.SandhiIntensity.MINIMAL -> AppTheme.AccentTeal
    }
}

@Composable
private fun getImpactColor(level: Int): Color {
    return when {
        level >= 4 -> AppTheme.ErrorColor
        level >= 3 -> AppTheme.WarningColor
        level >= 2 -> AppTheme.AccentGold
        else -> AppTheme.SuccessColor
    }
}

@Composable
private fun sandhiMonthDayYearFormatter(): DateTimeFormatter {
    val language = LocalLanguage.current
    return remember(language) {
        DateTimeFormatter.ofPattern(
            "MMM d, yyyy",
            if (language == Language.NEPALI) Locale.forLanguageTag("ne-NP") else Locale.ENGLISH
        )
    }
}

@Composable
private fun sandhiMonthDayFormatter(): DateTimeFormatter {
    val language = LocalLanguage.current
    return remember(language) {
        DateTimeFormatter.ofPattern(
            "MMM d",
            if (language == Language.NEPALI) Locale.forLanguageTag("ne-NP") else Locale.ENGLISH
        )
    }
}

@Composable
private fun sandhiMonthYearFormatter(): DateTimeFormatter {
    val language = LocalLanguage.current
    return remember(language) {
        DateTimeFormatter.ofPattern(
            "MMMM yyyy",
            if (language == Language.NEPALI) Locale.forLanguageTag("ne-NP") else Locale.ENGLISH
        )
    }
}

@Composable
private fun sandhiDayFormatter(): DateTimeFormatter {
    val language = LocalLanguage.current
    return remember(language) {
        DateTimeFormatter.ofPattern(
            "d",
            if (language == Language.NEPALI) Locale.forLanguageTag("ne-NP") else Locale.ENGLISH
        )
    }
}

@Composable
private fun sandhiWeekdayFormatter(): DateTimeFormatter {
    val language = LocalLanguage.current
    return remember(language) {
        DateTimeFormatter.ofPattern(
            "EEE",
            if (language == Language.NEPALI) Locale.forLanguageTag("ne-NP") else Locale.ENGLISH
        )
    }
}



