package com.astro.vajra.ui.screen

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.TrendingDown
import androidx.compose.material.icons.filled.TrendingFlat
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material.icons.outlined.Public
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.Button
import com.astro.vajra.ui.components.common.ModernPillTabRow
import com.astro.vajra.ui.components.common.NeoVedicPageHeader
import com.astro.vajra.ui.components.common.TabItem
import com.astro.vajra.ui.components.common.NeoVedicEmptyState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import com.astro.vajra.core.common.StringKey
import com.astro.vajra.core.common.StringKeyAnalysis
import com.astro.vajra.core.common.StringKeyAshtavarga
import com.astro.vajra.core.common.StringKeyNative
import com.astro.vajra.core.common.StringResources
import com.astro.vajra.core.common.getLocalizedName
import com.astro.vajra.data.localization.stringResource
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.astro.vajra.core.common.Language
import com.astro.vajra.data.localization.LocalLanguage
import com.astro.vajra.core.model.Planet
import com.astro.vajra.core.model.VedicChart
import com.astro.vajra.core.model.ZodiacSign
import com.astro.vajra.ephemeris.AshtavargaTransitCalculator.AshtavargaTransitResult
import com.astro.vajra.ephemeris.AshtavargaTransitCalculator.CurrentTransitInfo
import com.astro.vajra.ephemeris.AshtavargaTransitCalculator.LifeArea
import com.astro.vajra.ephemeris.AshtavargaTransitCalculator.TransitQuality
import com.astro.vajra.ephemeris.AshtavargaTransitCalculator.UpcomingTransit
import com.astro.vajra.ui.theme.AppTheme
import com.astro.vajra.ui.theme.NeoVedicTokens
import com.astro.vajra.ui.theme.SpaceGroteskFamily
import com.astro.vajra.ui.theme.CinzelDecorativeFamily
import com.astro.vajra.ui.theme.PoppinsFontFamily
import com.astro.vajra.ui.viewmodel.AshtavargaTransitViewModel
import com.astro.vajra.ui.viewmodel.AshtavargaTransitViewModel.AshtavargaTransitUiState
import com.astro.vajra.ui.viewmodel.AshtavargaTransitViewModel.PlanetTransitDetails
import com.astro.vajra.ui.viewmodel.AshtavargaTransitViewModel.TransitSortCriteria
import com.astro.vajra.ui.viewmodel.AshtavargaTransitViewModel.TransitSummary
import com.astro.vajra.ui.viewmodel.AshtavargaTransitViewModel.TransitTab
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit
import java.util.Locale
import androidx.compose.foundation.BorderStroke

/**
 * Ashtavarga Transit Predictions Screen
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AshtavargaTransitScreen(
    chart: VedicChart?,
    onNavigateBack: () -> Unit,
    viewModel: AshtavargaTransitViewModel = hiltViewModel()
) {
    val language = LocalLanguage.current
    val colors = AppTheme.current
    val uiState by viewModel.uiState.collectAsState()
    val transitSummary by viewModel.transitSummary.collectAsState()
    val planetDetails by viewModel.planetDetails.collectAsState()

    // Calculate transits when chart changes
    LaunchedEffect(chart, language) {
        viewModel.calculateTransits(chart, language)
    }

    Scaffold(
        topBar = {
            NeoVedicPageHeader(
                title = stringResource(StringKeyAnalysis.ASHTAVARGA_TRANSIT_TITLE),
                subtitle = stringResource(StringKeyAnalysis.ASHTAVARGA_TRANSIT_DESC),
                onBack = onNavigateBack,
                actionIcon = Icons.Default.Refresh,
                onAction = { viewModel.refresh(chart, language) }
            )
        },
        containerColor = colors.ScreenBackground
    ) { padding ->
        when (val state = uiState) {
            is AshtavargaTransitUiState.Initial,
            is AshtavargaTransitUiState.Loading -> {
                LoadingStateSS(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding),
                    language = language
                )
            }

            is AshtavargaTransitUiState.NoChart -> {
                NoChartStateSS(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding),
                    language = language
                )
            }

            is AshtavargaTransitUiState.Error -> {
                ErrorStateSS(
                    message = if (language == Language.NEPALI) state.messageNe else state.message,
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding),
                    onRetry = { viewModel.refresh(chart, language) }
                )
            }

            is AshtavargaTransitUiState.Success -> {
                TransitContent(
                    state = state,
                    summary = transitSummary,
                    planetDetails = planetDetails,
                    viewModel = viewModel,
                    language = language,
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding)
                )
            }
        }
    }
}

@Composable
private fun TransitContent(
    state: AshtavargaTransitUiState.Success,
    summary: TransitSummary?,
    planetDetails: Map<Planet, PlanetTransitDetails>,
    viewModel: AshtavargaTransitViewModel,
    language: Language,
    modifier: Modifier = Modifier
) {
    val colors = AppTheme.current
    var selectedTabIndex by remember { mutableIntStateOf(0) }

    val tabs = listOf(
        TabInfo(title = stringResource(StringKeyNative.LABEL_OVERVIEW)),
        TabInfo(title = stringResource(StringKeyAnalysis.VARSHAPHALA_CURRENT)),
        TabInfo(title = stringResource(StringKeyAnalysis.UI_UPCOMING_TRANSITS)),
        TabInfo(title = stringResource(StringKeyAnalysis.ANALYSIS_TAB_PLANETS)),
        TabInfo(title = stringResource(StringKeyAnalysis.ANALYSIS_CHART_ANALYSIS))
    )

    Column(modifier = modifier) {
        // Tab Row
        ModernPillTabRow(
            tabs = tabs.mapIndexed { index, tab ->
                TabItem(
                    title = tab.title,
                    accentColor = if (selectedTabIndex == index) colors.AccentPrimary else Color.Unspecified
                )
            },
            selectedIndex = selectedTabIndex,
            onTabSelected = { selectedTabIndex = it },
            modifier = Modifier.padding(
                horizontal = NeoVedicTokens.ScreenPadding,
                vertical = NeoVedicTokens.SpaceXS
            )
        )

        // Tab Content
        when (selectedTabIndex) {
            0 -> OverviewTab(state.result, summary, language)
            1 -> CurrentTransitsTab(state.result, viewModel, language)
            2 -> UpcomingTransitsTab(state.result, viewModel, language)
            3 -> PlanetsTab(planetDetails, language)
            4 -> AnalysisTab(state.result, viewModel, language)
        }
    }
}

@Composable
private fun OverviewTab(
    result: AshtavargaTransitResult,
    summary: TransitSummary?,
    language: Language
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Overall Score Card
        item {
            OverallScoreCard(
                score = result.overallTransitScore,
                quality = result.overallTransitQuality,
                language = language
            )
        }

        // Summary Statistics
        summary?.let { sum ->
            item {
                SummaryStatisticsCard(summary = sum, language = language)
            }
        }

        // Interpretation Card
        item {
            InterpretationCardSS(
                interpretation = if (language == Language.NEPALI) result.interpretationNe else result.interpretation,
                language = language
            )
        }

        // Recommendations
        if (result.recommendations.isNotEmpty()) {
            item {
                RecommendationsCardSS(
                    recommendations = if (language == Language.NEPALI) result.recommendationsNe else result.recommendations,
                    language = language
                )
            }
        }

        // Current Transit Highlights
        item {
            SectionHeaderSS(
                title = StringResources.get(StringKeyAnalysis.ASHTAVARGA_TRANSIT_HIGHLIGHTS, language),
                language = language
            )
        }

        items(result.currentTransits.take(4)) { transit ->
            CurrentTransitCard(transit = transit, language = language)
        }
    }
}

@Composable
private fun OverallScoreCard(
    score: Double,
    quality: TransitQuality,
    language: Language
) {
    val colors = AppTheme.current
    val qualityColor = getQualityColorSS(quality, colors)

    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = colors.CardBackground,
        shape = RoundedCornerShape(NeoVedicTokens.CardCornerRadius),
        border = BorderStroke(NeoVedicTokens.BorderWidth, AppTheme.BorderColor)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = StringResources.get(StringKeyAnalysis.ASHTAVARGA_TRANSIT_SCORE, language),
                fontFamily = CinzelDecorativeFamily,
                fontWeight = FontWeight.SemiBold,
                fontSize = com.astro.vajra.ui.theme.NeoVedicFontSizes.S16,
                color = colors.TextSecondary
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Circular Score Display
            Box(
                modifier = Modifier
                    .size(120.dp)
                    .clip(CircleShape)
                    .background(qualityColor.copy(alpha = 0.15f))
                    .border(4.dp, qualityColor, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "${score.toInt()}",
                        fontFamily = SpaceGroteskFamily,
                        fontSize = 32.sp,
                        fontWeight = FontWeight.Bold,
                        color = qualityColor
                    )
                    Text(
                        text = "/100",
                        fontFamily = PoppinsFontFamily,
                        fontSize = com.astro.vajra.ui.theme.NeoVedicFontSizes.S12,
                        color = colors.TextMuted
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Quality Badge
            Surface(
                shape = RoundedCornerShape(NeoVedicTokens.ElementCornerRadius),
                color = qualityColor.copy(alpha = 0.15f)
            ) {
                Text(
                    text = if (language == Language.NEPALI) quality.displayNameNe else quality.displayName,
                    fontFamily = SpaceGroteskFamily,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = com.astro.vajra.ui.theme.NeoVedicFontSizes.S12,
                    color = qualityColor,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp)
                )
            }

            // Quality Icon
            val trendIcon = when {
                score >= 70 -> Icons.Default.TrendingUp
                score >= 40 -> Icons.Default.TrendingFlat
                else -> Icons.Default.TrendingDown
            }

            Spacer(modifier = Modifier.height(8.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = trendIcon,
                    contentDescription = null,
                    tint = qualityColor,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = when {
                        score >= 70 -> stringResource(StringKeyAnalysis.UI_FAVORABLE_PERIOD)
                        score >= 40 -> stringResource(StringKeyAnalysis.UI_MIXED_PERIOD)
                        else -> stringResource(StringKeyAnalysis.UI_CAUTION_PERIOD)
                    },
                    fontFamily = PoppinsFontFamily,
                    fontSize = com.astro.vajra.ui.theme.NeoVedicFontSizes.S12,
                    color = colors.TextMuted
                )
            }
        }
    }
}

@Composable
private fun SummaryStatisticsCard(
    summary: TransitSummary,
    language: Language
) {
    val colors = AppTheme.current

    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = colors.CardBackground,
        shape = RoundedCornerShape(NeoVedicTokens.CardCornerRadius),
        border = BorderStroke(NeoVedicTokens.BorderWidth, AppTheme.BorderColor)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(
                text = StringResources.get(StringKeyAnalysis.ASHTAVARGA_TRANSIT_SUMMARY, language),
                fontFamily = CinzelDecorativeFamily,
                fontWeight = FontWeight.SemiBold,
                fontSize = com.astro.vajra.ui.theme.NeoVedicFontSizes.S16,
                color = colors.TextPrimary
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Statistics Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                StatItemSS(
                    value = summary.favorableCount.toString(),
                    label = StringResources.get(StringKeyAnalysis.TRANSIT_FAVORABLE, language),
                    color = colors.SuccessColor
                )
                StatItemSS(
                    value = summary.averageCount.toString(),
                    label = StringResources.get(com.astro.vajra.core.common.StringKeyMatch.MATCH_AVERAGE, language),
                    color = colors.WarningColor
                )
                StatItemSS(
                    value = summary.challengingCount.toString(),
                    label = StringResources.get(StringKeyAnalysis.TRANSIT_CHALLENGING, language),
                    color = colors.ErrorColor
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Dominant Life Areas
            if (summary.dominantLifeAreas.isNotEmpty()) {
                Text(
                    text = StringResources.get(StringKeyAnalysis.UI_AFFECTED_AREAS, language),
                    fontFamily = PoppinsFontFamily,
                    fontWeight = FontWeight.Medium,
                    fontSize = com.astro.vajra.ui.theme.NeoVedicFontSizes.S14,
                    color = colors.TextSecondary
                )

                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    modifier = Modifier.horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    summary.dominantLifeAreas.forEach { area ->
                        LifeAreaChipSS(area = area, language = language)
                    }
                }
            }

            // Next Significant Transit
            summary.nextSignificantTransit?.let { next ->
                Spacer(modifier = Modifier.height(16.dp))

                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(NeoVedicTokens.ElementCornerRadius),
                    color = colors.ChipBackground
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Star,
                            contentDescription = null,
                            tint = colors.AccentGold,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = StringResources.get(StringKeyAnalysis.ASHTAVARGA_TRANSIT_NEXT, language),
                                fontFamily = SpaceGroteskFamily,
                                fontSize = com.astro.vajra.ui.theme.NeoVedicFontSizes.S10,
                                color = colors.TextMuted
                            )
                            Text(
                                text = "${next.planet.getLocalizedName(language)} \u2192 ${next.toSign.getLocalizedName(language)}",
                                fontFamily = PoppinsFontFamily,
                                fontWeight = FontWeight.Medium,
                                fontSize = com.astro.vajra.ui.theme.NeoVedicFontSizes.S14,
                                color = colors.TextPrimary
                            )
                        }
                        Text(
                            text = next.transitDate.format(formatMonthDay(language)),
                            fontFamily = SpaceGroteskFamily,
                            fontSize = com.astro.vajra.ui.theme.NeoVedicFontSizes.S12,
                            color = colors.AccentPrimary
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun StatItemSS(
    value: String,
    label: String,
    color: Color
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = value,
            fontFamily = CinzelDecorativeFamily,
            fontSize = com.astro.vajra.ui.theme.NeoVedicFontSizes.S20,
            fontWeight = FontWeight.Bold,
            color = color
        )
        Text(
            text = label,
            fontFamily = PoppinsFontFamily,
            fontSize = com.astro.vajra.ui.theme.NeoVedicFontSizes.S10,
            color = AppTheme.current.TextMuted
        )
    }
}

@Composable
private fun LifeAreaChipSS(area: LifeArea, language: Language) {
    val colors = AppTheme.current

    Surface(
        shape = RoundedCornerShape(NeoVedicTokens.ElementCornerRadius),
        color = colors.AccentPrimary.copy(alpha = 0.15f)
    ) {
        Text(
            text = if (language == Language.NEPALI) area.displayNameNe else area.displayName,
            fontFamily = SpaceGroteskFamily,
            fontSize = com.astro.vajra.ui.theme.NeoVedicFontSizes.S12,
            color = colors.AccentPrimary,
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
        )
    }
}

@Composable
private fun InterpretationCardSS(
    interpretation: String,
    language: Language
) {
    val colors = AppTheme.current

    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = colors.CardBackground,
        shape = RoundedCornerShape(NeoVedicTokens.CardCornerRadius),
        border = BorderStroke(NeoVedicTokens.BorderWidth, AppTheme.BorderColor)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.Info,
                    contentDescription = null,
                    tint = colors.AccentPrimary,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = stringResource(StringKeyAnalysis.PANCHANGA_SANSKRIT_LABEL),
                    fontFamily = CinzelDecorativeFamily,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = com.astro.vajra.ui.theme.NeoVedicFontSizes.S16,
                    color = colors.TextPrimary
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = interpretation,
                fontFamily = PoppinsFontFamily,
                fontSize = com.astro.vajra.ui.theme.NeoVedicFontSizes.S14,
                color = colors.TextSecondary,
                lineHeight = 22.sp
            )
        }
    }
}

@Composable
private fun RecommendationsCardSS(
    recommendations: List<String>,
    language: Language
) {
    val colors = AppTheme.current

    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = colors.CardBackground,
        shape = RoundedCornerShape(NeoVedicTokens.CardCornerRadius),
        border = BorderStroke(NeoVedicTokens.BorderWidth, AppTheme.BorderColor)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(
                text = stringResource(StringKeyAnalysis.PANCHANGA_AVOID).substringAfter(" "),
                fontFamily = CinzelDecorativeFamily,
                fontWeight = FontWeight.SemiBold,
                fontSize = com.astro.vajra.ui.theme.NeoVedicFontSizes.S16,
                color = colors.TextPrimary
            )

            Spacer(modifier = Modifier.height(12.dp))

            recommendations.forEach { recommendation ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 6.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Check,
                        contentDescription = null,
                        tint = colors.SuccessColor,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = recommendation,
                        fontFamily = PoppinsFontFamily,
                        fontSize = com.astro.vajra.ui.theme.NeoVedicFontSizes.S14,
                        color = colors.TextSecondary,
                        lineHeight = 20.sp
                    )
                }
            }
        }
    }
}

@Composable
private fun CurrentTransitsTab(
    result: AshtavargaTransitResult,
    viewModel: AshtavargaTransitViewModel,
    language: Language
) {
    var sortCriteria by remember { mutableStateOf(TransitSortCriteria.BY_QUALITY_DESC) }
    val sortedTransits = viewModel.getCurrentTransitsSorted(sortCriteria)

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // Sort Options
        item {
            SortOptionsRowSS(
                currentSort = sortCriteria,
                onSortSelected = { sortCriteria = it },
                language = language
            )
        }

        // Transit Cards
        items(sortedTransits) { transit ->
            CurrentTransitCard(transit = transit, language = language, expanded = true)
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun SortOptionsRowSS(
    currentSort: TransitSortCriteria,
    onSortSelected: (TransitSortCriteria) -> Unit,
    language: Language
) {
    val colors = AppTheme.current

    Column {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.FilterList,
                contentDescription = null,
                tint = colors.TextMuted,
                modifier = Modifier.size(18.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = StringResources.get(StringKeyAnalysis.UI_SORT_BY, language),
                fontFamily = PoppinsFontFamily,
                fontSize = com.astro.vajra.ui.theme.NeoVedicFontSizes.S14,
                color = colors.TextMuted
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        FlowRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            val options = listOf(
                TransitSortCriteria.BY_QUALITY_DESC to StringResources.get(StringKeyAnalysis.TRANSIT_QUALITY_LABEL, language),
                TransitSortCriteria.BY_BAV_DESC to StringResources.get(StringKeyAshtavarga.BAV_LABEL, language),
                TransitSortCriteria.BY_SAV_DESC to StringResources.get(StringKeyAshtavarga.SAV_LABEL, language),
                TransitSortCriteria.BY_EXIT_DATE to StringResources.get(StringKeyAnalysis.UI_EXIT_DATE, language)
            )

            options.forEach { (criteria, label) ->
                com.astro.vajra.ui.components.common.NeoVedicChoicePill(
                    selected = currentSort == criteria,
                    onClick = { onSortSelected(criteria) },
                    label = { Text(label, fontFamily = SpaceGroteskFamily, fontSize = com.astro.vajra.ui.theme.NeoVedicFontSizes.S12) },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = colors.AccentPrimary,
                        selectedLabelColor = colors.ButtonText
                    )
                )
            }
        }
    }
}

@Composable
private fun CurrentTransitCard(
    transit: CurrentTransitInfo,
    language: Language,
    expanded: Boolean = false
) {
    val colors = AppTheme.current
    val qualityColor = getQualityColorSS(transit.quality, colors)
    var isExpanded by remember { mutableStateOf(expanded) }

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { isExpanded = !isExpanded },
        color = colors.CardBackground,
        shape = RoundedCornerShape(NeoVedicTokens.CardCornerRadius),
        border = BorderStroke(NeoVedicTokens.BorderWidth, AppTheme.BorderColor)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            // Header Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Planet Info
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .clip(CircleShape)
                            .background(qualityColor.copy(alpha = 0.15f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = transit.planet.getLocalizedName(language).take(2),
                            fontFamily = CinzelDecorativeFamily,
                            fontWeight = FontWeight.Bold,
                            fontSize = com.astro.vajra.ui.theme.NeoVedicFontSizes.S14,
                            color = qualityColor
                        )
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(
                            text = transit.planet.getLocalizedName(language),
                            fontFamily = CinzelDecorativeFamily,
                            fontWeight = FontWeight.SemiBold,
                            fontSize = com.astro.vajra.ui.theme.NeoVedicFontSizes.S16,
                            color = colors.TextPrimary
                        )
                        Text(
                            text = StringResources.get(StringKeyAshtavarga.TRANSIT_IN_SIGN, language, transit.currentSign.getLocalizedName(language)),
                            fontFamily = PoppinsFontFamily,
                            fontSize = com.astro.vajra.ui.theme.NeoVedicFontSizes.S12,
                            color = colors.TextMuted
                        )
                    }
                }

                // Scores
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    ScoreBadgeSS(label = StringResources.get(StringKeyAshtavarga.BAV_LABEL, language), value = transit.bavScore, maxValue = 8)
                    ScoreBadgeSS(label = StringResources.get(StringKeyAshtavarga.SAV_LABEL, language), value = transit.savScore, maxValue = 56)
                }
            }

            // Progress Bar
            Spacer(modifier = Modifier.height(12.dp))

            Column {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = transit.entryDate?.format(formatMonthDay(language)) ?: "-",
                        fontFamily = SpaceGroteskFamily,
                        fontSize = com.astro.vajra.ui.theme.NeoVedicFontSizes.S10,
                        color = colors.TextMuted
                    )
                    Text(
                        text = "${transit.progressPercent.toInt()}%",
                        fontFamily = SpaceGroteskFamily,
                        fontSize = com.astro.vajra.ui.theme.NeoVedicFontSizes.S10,
                        fontWeight = FontWeight.Medium,
                        color = qualityColor
                    )
                    Text(
                        text = transit.exitDate?.format(formatMonthDay(language)) ?: "-",
                        fontFamily = SpaceGroteskFamily,
                        fontSize = com.astro.vajra.ui.theme.NeoVedicFontSizes.S10,
                        color = colors.TextMuted
                    )
                }

                Spacer(modifier = Modifier.height(4.dp))

                LinearProgressIndicator(
                    progress = { (transit.progressPercent / 100f).toFloat() },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(6.dp)
                        .clip(RoundedCornerShape(com.astro.vajra.ui.theme.NeoVedicTokens.ElementCornerRadius)),
                    color = qualityColor,
                    trackColor = qualityColor.copy(alpha = 0.2f)
                )
            }

            // Expanded Content
            AnimatedVisibility(
                visible = isExpanded,
                enter = expandVertically(),
                exit = shrinkVertically()
            ) {
                Column(modifier = Modifier.padding(top = 16.dp)) {
                    // Quality Badge
                    Surface(
                        shape = RoundedCornerShape(NeoVedicTokens.ElementCornerRadius),
                        color = qualityColor.copy(alpha = 0.15f)
                    ) {
                        Text(
                            text = if (language == Language.NEPALI) transit.quality.displayNameNe else transit.quality.displayName,
                            fontFamily = SpaceGroteskFamily,
                            fontWeight = FontWeight.Medium,
                            fontSize = com.astro.vajra.ui.theme.NeoVedicFontSizes.S11,
                            color = qualityColor,
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    // House Positions
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        InfoItemSS(
                            label = StringResources.get(StringKeyAnalysis.UI_FROM_MOON, language),
                            value = "${transit.houseFromMoon}"
                        )
                        InfoItemSS(
                            label = StringResources.get(StringKeyAnalysis.UI_FROM_LAGNA, language),
                            value = "${transit.houseFromAsc}"
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    // Interpretation
                    Text(
                        text = if (language == Language.NEPALI) transit.interpretationNe else transit.interpretation,
                        fontFamily = PoppinsFontFamily,
                        fontSize = com.astro.vajra.ui.theme.NeoVedicFontSizes.S14,
                        color = colors.TextSecondary,
                        lineHeight = 18.sp
                    )
                }
            }

            // Expand/Collapse Indicator
            val rotation by animateFloatAsState(
                targetValue = if (isExpanded) 180f else 0f,
                label = "rotation"
            )

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.ArrowDropDown,
                    contentDescription = if (isExpanded) StringResources.get(StringKeyAshtavarga.COLLAPSE, language) else StringResources.get(StringKeyAshtavarga.EXPAND, language),
                    tint = colors.TextMuted,
                    modifier = Modifier
                        .size(24.dp)
                        .rotate(rotation)
                )
            }
        }
    }
}

@Composable
private fun ScoreBadgeSS(label: String, value: Int, maxValue: Int) {
    val colors = AppTheme.current
    val ratio = value.toFloat() / maxValue
    val color = when {
        ratio >= 0.6f -> colors.SuccessColor
        ratio >= 0.4f -> colors.WarningColor
        else -> colors.ErrorColor
    }

    Surface(
        shape = RoundedCornerShape(NeoVedicTokens.ElementCornerRadius),
        color = color.copy(alpha = 0.15f)
    ) {
        Column(
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = label,
                fontFamily = SpaceGroteskFamily,
                fontSize = com.astro.vajra.ui.theme.NeoVedicFontSizes.S10,
                color = colors.TextMuted
            )
            Text(
                text = "$value",
                fontFamily = SpaceGroteskFamily,
                fontWeight = FontWeight.Bold,
                fontSize = com.astro.vajra.ui.theme.NeoVedicFontSizes.S14,
                color = color
            )
        }
    }
}

@Composable
private fun InfoItemSS(label: String, value: String) {
    val colors = AppTheme.current

    Column {
        Text(
            text = label,
            fontFamily = SpaceGroteskFamily,
            fontSize = com.astro.vajra.ui.theme.NeoVedicFontSizes.S10,
            color = colors.TextMuted
        )
        Text(
            text = value,
            fontFamily = PoppinsFontFamily,
            fontWeight = FontWeight.Medium,
            fontSize = com.astro.vajra.ui.theme.NeoVedicFontSizes.S14,
            color = colors.TextPrimary
        )
    }
}

@Composable
private fun UpcomingTransitsTab(
    result: AshtavargaTransitResult,
    viewModel: AshtavargaTransitViewModel,
    language: Language
) {
    val colors = AppTheme.current
    var showOnlySignificant by remember { mutableStateOf(false) }

    val filteredTransits = if (showOnlySignificant) {
        result.upcomingTransits.filter { it.isSignificant }
    } else {
        result.upcomingTransits
    }

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // Filter Toggle
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "${filteredTransits.size} ${stringResource(StringKeyAnalysis.UI_UPCOMING_TRANSITS)}",
                    fontFamily = CinzelDecorativeFamily,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = com.astro.vajra.ui.theme.NeoVedicFontSizes.S16,
                    color = colors.TextPrimary
                )

                com.astro.vajra.ui.components.common.NeoVedicChoicePill(
                    selected = showOnlySignificant,
                    onClick = { showOnlySignificant = !showOnlySignificant },
                    label = {
                        Text(
                            text = StringResources.get(StringKeyAshtavarga.SIGNIFICANT_ONLY, language),
                            fontFamily = SpaceGroteskFamily,
                            fontSize = com.astro.vajra.ui.theme.NeoVedicFontSizes.S12
                        )
                    },
                    leadingIcon = if (showOnlySignificant) {
                        { Icon(Icons.Default.Check, null, Modifier.size(16.dp)) }
                    } else null,
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = colors.AccentPrimary,
                        selectedLabelColor = colors.ButtonText
                    )
                )
            }
        }

        // Upcoming Transit Cards
        items(filteredTransits.take(20)) { transit ->
            UpcomingTransitCard(
                transit = transit,
                language = language,
                asOfDate = result.analysisDate.toLocalDate()
            )
        }
    }
}

@Composable
private fun UpcomingTransitCard(
    transit: UpcomingTransit,
    language: Language,
    asOfDate: java.time.LocalDate
) {
    val colors = AppTheme.current
    val qualityColor = getQualityColorSS(transit.quality, colors)
    val daysUntil = ChronoUnit.DAYS.between(asOfDate, transit.transitDate).toInt()

    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = colors.CardBackground,
        shape = RoundedCornerShape(NeoVedicTokens.CardCornerRadius),
        border = BorderStroke(NeoVedicTokens.BorderWidth, AppTheme.BorderColor)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Left: Planet and Sign Info
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.weight(1f)
            ) {
                // Date Badge
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .clip(RoundedCornerShape(NeoVedicTokens.ElementCornerRadius))
                        .background(colors.ChipBackground)
                        .padding(8.dp)
                ) {
                    Text(
                        text = transit.transitDate.format(formatMonth(language)),
                        fontFamily = SpaceGroteskFamily,
                        fontSize = com.astro.vajra.ui.theme.NeoVedicFontSizes.S10,
                        color = colors.TextMuted
                    )
                    Text(
                        text = transit.transitDate.format(formatDay(language)),
                        fontFamily = SpaceGroteskFamily,
                        fontSize = com.astro.vajra.ui.theme.NeoVedicFontSizes.S18,
                        fontWeight = FontWeight.Bold,
                        color = colors.TextPrimary
                    )
                }

                Spacer(modifier = Modifier.width(12.dp))

                Column {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = transit.planet.getLocalizedName(language),
                            fontFamily = CinzelDecorativeFamily,
                            fontWeight = FontWeight.SemiBold,
                            fontSize = com.astro.vajra.ui.theme.NeoVedicFontSizes.S14,
                            color = colors.TextPrimary
                        )
                        if (transit.isSignificant) {
                            Spacer(modifier = Modifier.width(4.dp))
                            Icon(
                                imageVector = Icons.Default.Star,
                                contentDescription = StringResources.get(StringKeyAshtavarga.SIGNIFICANT, language),
                                tint = colors.AccentGold,
                                modifier = Modifier.size(16.dp)
                            )
                        }
                    }
                    Text(
                        text = "${transit.fromSign.getLocalizedName(language)} \u2192 ${transit.toSign.getLocalizedName(language)}",
                        fontFamily = PoppinsFontFamily,
                        fontSize = com.astro.vajra.ui.theme.NeoVedicFontSizes.S12,
                        color = colors.TextMuted
                    )
                    Text(
                        text = if (daysUntil == 0) {
                            StringResources.get(StringKey.PERIOD_TODAY, language)
                        } else {
                            String.format(stringResource(StringKeyAnalysis.UI_IN_DAYS_FMT), daysUntil)
                        },
                        fontFamily = SpaceGroteskFamily,
                        fontSize = com.astro.vajra.ui.theme.NeoVedicFontSizes.S10,
                        color = colors.AccentPrimary
                    )
                }
            }

            // Right: Quality and Scores
            Column(horizontalAlignment = Alignment.End) {
                Surface(
                    shape = RoundedCornerShape(NeoVedicTokens.ElementCornerRadius),
                    color = qualityColor.copy(alpha = 0.15f)
                ) {
                    Text(
                        text = if (language == Language.NEPALI) transit.quality.displayNameNe else transit.quality.displayName,
                        fontFamily = SpaceGroteskFamily,
                        fontSize = com.astro.vajra.ui.theme.NeoVedicFontSizes.S10,
                        color = qualityColor,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "${StringResources.get(StringKeyAshtavarga.BAV_LABEL, language)}: ${transit.bavScore} | ${StringResources.get(StringKeyAshtavarga.SAV_LABEL, language)}: ${transit.savScore}",
                    fontFamily = SpaceGroteskFamily,
                    fontSize = com.astro.vajra.ui.theme.NeoVedicFontSizes.S10,
                    color = colors.TextMuted
                )
            }
        }
    }
}

@Composable
private fun PlanetsTab(
    planetDetails: Map<Planet, PlanetTransitDetails>,
    language: Language
) {
    val colors = AppTheme.current
    var selectedPlanet by remember { mutableStateOf<Planet?>(null) }

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // Planet Selection Row
        item {
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                val sortedPlanets = planetDetails.keys.sortedBy { it.ordinal }
                items(sortedPlanets) { planet ->
                    val isSelected = selectedPlanet == planet
                    val details = planetDetails[planet]

                    com.astro.vajra.ui.components.common.NeoVedicChoicePill(
                        selected = isSelected,
                        onClick = {
                            selectedPlanet = if (isSelected) null else planet
                        },
                        label = { Text(planet.getLocalizedName(language)) },
                        leadingIcon = details?.currentTransit?.let {
                            {
                                Box(
                                    modifier = Modifier
                                        .size(8.dp)
                                        .clip(CircleShape)
                                        .background(getQualityColorSS(it.quality, colors))
                                )
                            }
                        },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = colors.AccentPrimary,
                            selectedLabelColor = colors.ButtonText
                        )
                    )
                }
            }
        }

        // Planet Detail Cards
        val planetsToShow = if (selectedPlanet != null) {
            listOf(selectedPlanet!!)
        } else {
            planetDetails.keys.toList().sortedBy { it.ordinal }
        }

        items(planetsToShow) { planet ->
            planetDetails[planet]?.let { details ->
                PlanetDetailCardSS(planet = planet, details = details, language = language)
            }
        }
    }
}

@Composable
private fun PlanetDetailCardSS(
    planet: Planet,
    details: PlanetTransitDetails,
    language: Language
) {
    val colors = AppTheme.current
    var isExpanded by remember { mutableStateOf(false) }

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { isExpanded = !isExpanded },
        color = colors.CardBackground,
        shape = RoundedCornerShape(NeoVedicTokens.CardCornerRadius),
        border = BorderStroke(NeoVedicTokens.BorderWidth, AppTheme.BorderColor)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            // Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = planet.getLocalizedName(language),
                        fontFamily = CinzelDecorativeFamily,
                        fontWeight = FontWeight.Bold,
                        fontSize = com.astro.vajra.ui.theme.NeoVedicFontSizes.S18,
                        color = colors.TextPrimary
                    )
                    details.currentTransit?.let { transit ->
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = StringResources.get(StringKeyAshtavarga.TRANSIT_IN_SIGN, language, transit.currentSign.getLocalizedName(language)),
                            fontFamily = PoppinsFontFamily,
                            fontSize = com.astro.vajra.ui.theme.NeoVedicFontSizes.S14,
                            color = colors.TextMuted
                        )
                    }
                }

                details.currentTransit?.let { transit ->
                    Surface(
                        shape = RoundedCornerShape(NeoVedicTokens.ElementCornerRadius),
                        color = getQualityColorSS(transit.quality, colors).copy(alpha = 0.15f)
                    ) {
                        Text(
                            text = "${StringResources.get(StringKeyAshtavarga.BAV_LABEL, language)}: ${transit.bavScore}",
                            fontFamily = SpaceGroteskFamily,
                            fontWeight = FontWeight.Medium,
                            fontSize = com.astro.vajra.ui.theme.NeoVedicFontSizes.S12,
                            color = getQualityColorSS(transit.quality, colors),
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Best/Worst Signs
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                details.bestSign?.let { sign ->
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = StringResources.get(StringKeyAnalysis.ASHTAVARGA_TRANSIT_BEST_SIGN, language),
                            fontFamily = SpaceGroteskFamily,
                            fontSize = com.astro.vajra.ui.theme.NeoVedicFontSizes.S10,
                            color = colors.TextMuted
                        )
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(8.dp)
                                    .clip(CircleShape)
                                    .background(colors.SuccessColor)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = sign.getLocalizedName(language),
                                fontFamily = PoppinsFontFamily,
                                fontWeight = FontWeight.Medium,
                                fontSize = com.astro.vajra.ui.theme.NeoVedicFontSizes.S14,
                                color = colors.TextPrimary
                            )
                        }
                    }
                }

                details.worstSign?.let { sign ->
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = StringResources.get(StringKeyAshtavarga.WEAK_SIGN, language),
                            fontFamily = SpaceGroteskFamily,
                            fontSize = com.astro.vajra.ui.theme.NeoVedicFontSizes.S10,
                            color = colors.TextMuted
                        )
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(8.dp)
                                    .clip(CircleShape)
                                    .background(colors.ErrorColor)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = sign.getLocalizedName(language),
                                fontFamily = PoppinsFontFamily,
                                fontWeight = FontWeight.Medium,
                                fontSize = com.astro.vajra.ui.theme.NeoVedicFontSizes.S14,
                                color = colors.TextPrimary
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Stats
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = StringResources.get(StringKeyAshtavarga.AVG_SCORE, language),
                        fontFamily = SpaceGroteskFamily,
                        fontSize = com.astro.vajra.ui.theme.NeoVedicFontSizes.S10,
                        color = colors.TextMuted
                    )
                    Text(
                        text = String.format("%.1f", details.averageScore),
                        fontFamily = PoppinsFontFamily,
                        fontWeight = FontWeight.Medium,
                        fontSize = com.astro.vajra.ui.theme.NeoVedicFontSizes.S14,
                        color = colors.TextPrimary
                    )
                }
            }
        }
    }
}

@Composable
private fun AnalysisTab(
    result: AshtavargaTransitResult,
    viewModel: AshtavargaTransitViewModel,
    language: Language
) {
    // This seems to be a placeholder in the original code or implemented similarly to Overview.
    // Reusing Overview content for now, or could display advanced charts/graphs if available.
    OverviewTab(result, null, language)
}

@Composable
private fun LoadingStateSS(modifier: Modifier = Modifier, language: Language) {
    Box(modifier = modifier, contentAlignment = Alignment.Center) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            CircularProgressIndicator(color = AppTheme.current.AccentPrimary)
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = StringResources.get(StringKeyAnalysis.LOADING_ANALYSIS, language),
                fontFamily = PoppinsFontFamily,
                color = AppTheme.current.TextMuted
            )
        }
    }
}

@Composable
private fun NoChartStateSS(modifier: Modifier = Modifier, language: Language) {
    NeoVedicEmptyState(
        title = StringResources.get(StringKeyAnalysis.ASHTAVARGA_TRANSIT_TITLE, language),
        subtitle = StringResources.get(StringKey.NO_PROFILE_MESSAGE, language),
        icon = Icons.Outlined.Public,
        modifier = modifier
    )
}

@Composable
private fun ErrorStateSS(message: String, modifier: Modifier = Modifier, onRetry: () -> Unit) {
    NeoVedicEmptyState(
        title = "Error",
        subtitle = message,
        icon = Icons.Filled.Info,
        modifier = modifier
    )
    // Add retry button if needed within NeoVedicEmptyState or below it
}

@Composable
private fun SectionHeaderSS(title: String, language: Language) {
    Text(
        text = title,
        fontFamily = CinzelDecorativeFamily,
        fontWeight = FontWeight.Bold,
        fontSize = com.astro.vajra.ui.theme.NeoVedicFontSizes.S16,
        color = AppTheme.current.TextPrimary,
        modifier = Modifier.padding(top = 8.dp)
    )
}

private fun getQualityColorSS(quality: TransitQuality, colors: com.astro.vajra.ui.theme.AppColors): Color {
    return when (quality) {
        TransitQuality.EXCELLENT -> colors.SuccessColor
        TransitQuality.GOOD -> colors.AccentTeal
        TransitQuality.AVERAGE -> colors.AccentGold
        TransitQuality.CHALLENGING -> colors.WarningColor
        TransitQuality.DIFFICULT -> colors.ErrorColor
    }
}

private fun formatMonthDay(language: Language): DateTimeFormatter {
    return DateTimeFormatter.ofPattern("MMM d", if (language == Language.NEPALI) Locale("ne") else Locale.ENGLISH)
}

private fun formatDay(language: Language): DateTimeFormatter {
    return DateTimeFormatter.ofPattern("d", if (language == Language.NEPALI) Locale("ne") else Locale.ENGLISH)
}

private fun formatMonth(language: Language): DateTimeFormatter {
    return DateTimeFormatter.ofPattern("MMM", if (language == Language.NEPALI) Locale("ne") else Locale.ENGLISH)
}

data class TabInfo(val title: String)
