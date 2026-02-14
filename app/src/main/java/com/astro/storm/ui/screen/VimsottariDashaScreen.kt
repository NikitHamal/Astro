package com.astro.storm.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.CalendarToday
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.Lightbulb
import androidx.compose.material.icons.outlined.Timeline
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.astro.storm.core.common.Language
import com.astro.storm.core.common.StringKey
import com.astro.storm.core.common.StringKeyDosha
import com.astro.storm.core.common.StringKeyMatch
import com.astro.storm.core.common.getLocalizedName
import com.astro.storm.data.localization.DateFormat
import com.astro.storm.data.localization.LocalDateSystem
import com.astro.storm.data.localization.LocalLanguage
import com.astro.storm.data.localization.formatDate
import com.astro.storm.data.localization.formatDurationYearsMonths
import com.astro.storm.data.localization.stringResource
import com.astro.storm.core.model.Planet
import com.astro.storm.core.model.VedicChart
import com.astro.storm.ephemeris.DashaCalculator
import com.astro.storm.ui.components.common.ModernPillTabRow
import com.astro.storm.ui.components.common.NeoVedicPageHeader
import com.astro.storm.ui.components.common.TabItem
import com.astro.storm.ui.screen.chartdetail.tabs.DashasTabContent
import com.astro.storm.ui.theme.AppTheme
import com.astro.storm.ui.theme.NeoVedicTokens
import com.astro.storm.ui.viewmodel.DashaUiState
import com.astro.storm.ui.viewmodel.DashaViewModel
import java.time.LocalDate
import java.time.temporal.ChronoUnit

private enum class VimsottariTab { ABOUT, TIMELINE, INTERPRETATION }

private data class CurrentVimsottariPeriodInfo(
    val mahadasha: String?,
    val antardasha: String?,
    val isLoading: Boolean,
    val hasError: Boolean
)

@Composable
fun VimsottariDashaScreen(
    chart: VedicChart?,
    onBack: () -> Unit,
    viewModel: DashaViewModel = hiltViewModel()
) {
    val language = LocalLanguage.current
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    var selectedTab by rememberSaveable { mutableIntStateOf(0) }

    LaunchedEffect(chart) { viewModel.loadDashaTimeline(chart) }

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
        when (val state = uiState) {
            is DashaUiState.Loading -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = AppTheme.AccentPrimary)
                }
            }

            is DashaUiState.Error -> {
                VimsottariErrorContent(
                    paddingValues = paddingValues,
                    message = state.message,
                    onRetry = { viewModel.loadDashaTimeline(chart) }
                )
            }

            is DashaUiState.Idle -> {
                EmptyChartScreen(
                    title = stringResource(StringKeyDosha.DASHA_TITLE_VIMSOTTARI),
                    message = stringResource(StringKey.NO_PROFILE_MESSAGE),
                    onBack = onBack
                )
            }

            is DashaUiState.Success -> {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                ) {
                    VimsottariTabRow(
                        selectedTab = selectedTab,
                        onTabSelected = { selectedTab = it }
                    )

                    when (VimsottariTab.entries[selectedTab]) {
                        VimsottariTab.ABOUT -> VimsottariAboutTab()
                        VimsottariTab.TIMELINE -> DashasTabContent(
                            timeline = state.timeline,
                            scrollToTodayEvent = viewModel.scrollToTodayEvent,
                            includeMicroLevels = false,
                            showInfoFooter = false
                        )
                        VimsottariTab.INTERPRETATION -> VimsottariInterpretationTab(timeline = state.timeline)
                    }
                }
            }
        }
    }
}

@Composable
private fun VimsottariDashaTopBar(
    chartName: String,
    currentPeriodInfo: CurrentVimsottariPeriodInfo,
    onBack: () -> Unit,
    onTodayClick: () -> Unit
) {
    val subtitle = when {
        currentPeriodInfo.isLoading -> stringResource(StringKey.DASHA_CALCULATING)
        currentPeriodInfo.hasError -> "${stringResource(StringKey.DASHA_ERROR)} - $chartName"
        currentPeriodInfo.mahadasha != null -> buildString {
            append(currentPeriodInfo.mahadasha)
            currentPeriodInfo.antardasha?.let {
                append(" -> ")
                append(it)
            }
            append(" | ")
            append(chartName)
        }
        else -> chartName
    }

    NeoVedicPageHeader(
        title = stringResource(StringKeyDosha.DASHA_TITLE_VIMSOTTARI),
        subtitle = subtitle,
        onBack = onBack,
        actionIcon = Icons.Outlined.CalendarToday,
        onAction = onTodayClick
    )
}

@Composable
private fun VimsottariTabRow(
    selectedTab: Int,
    onTabSelected: (Int) -> Unit
) {
    val tabs = listOf(
        TabItem(title = stringResource(StringKeyDosha.VIMSOTTARI_ABOUT), accentColor = AppTheme.AccentPrimary),
        TabItem(title = stringResource(StringKeyDosha.SCREEN_TIMELINE), accentColor = AppTheme.AccentTeal),
        TabItem(title = stringResource(StringKeyDosha.SCREEN_INTERPRETATION), accentColor = AppTheme.AccentGold)
    )

    ModernPillTabRow(
        tabs = tabs,
        selectedIndex = selectedTab,
        onTabSelected = onTabSelected,
        modifier = Modifier.padding(
            horizontal = NeoVedicTokens.ScreenPadding,
            vertical = NeoVedicTokens.SpaceXS
        )
    )
}

@Composable
private fun VimsottariAboutTab() {
    val language = LocalLanguage.current
    val sequence = remember {
        listOf(
            Planet.KETU to 7,
            Planet.VENUS to 20,
            Planet.SUN to 6,
            Planet.MOON to 10,
            Planet.MARS to 7,
            Planet.RAHU to 18,
            Planet.JUPITER to 16,
            Planet.SATURN to 19,
            Planet.MERCURY to 17
        )
    }

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(NeoVedicTokens.ScreenPadding),
        verticalArrangement = Arrangement.spacedBy(NeoVedicTokens.SpaceMD)
    ) {
        item {
            VimsottariInfoCard(
                icon = Icons.Outlined.Info,
                title = stringResource(StringKeyDosha.VIMSOTTARI_ABOUT),
                subtitle = stringResource(StringKeyDosha.DASHA_VIMSOTTARI_DESC)
            )
        }

        item {
            Surface(
                shape = MaterialTheme.shapes.large,
                color = AppTheme.CardBackground
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = stringResource(StringKeyMatch.DASHA_PERIODS_SEQUENCE),
                        style = MaterialTheme.typography.titleMedium,
                        color = AppTheme.TextPrimary
                    )
                    Spacer(modifier = Modifier.height(12.dp))

                    sequence.forEach { (planet, years) ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = planet.getLocalizedName(language),
                                modifier = Modifier.weight(1f),
                                style = MaterialTheme.typography.bodyMedium,
                                color = AppTheme.TextPrimary
                            )
                            Text(
                                text = formatDurationYearsMonths(years.toDouble()),
                                style = MaterialTheme.typography.bodySmall,
                                color = AppTheme.TextSecondary
                            )
                        }
                    }
                }
            }
        }

        item {
            Surface(
                shape = MaterialTheme.shapes.large,
                color = AppTheme.CardBackground
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = stringResource(StringKeyMatch.DASHA_HIERARCHY),
                        style = MaterialTheme.typography.titleMedium,
                        color = AppTheme.TextPrimary
                    )
                    Spacer(modifier = Modifier.height(12.dp))

                    HierarchyRow(
                        level = "1",
                        name = stringResource(StringKeyMatch.DASHA_LEVEL_MAHADASHA),
                        desc = stringResource(StringKeyMatch.DASHA_MAJOR_PERIOD_YEARS)
                    )
                    HierarchyRow(
                        level = "2",
                        name = stringResource(StringKeyMatch.DASHA_LEVEL_ANTARDASHA),
                        desc = stringResource(StringKeyMatch.DASHA_SUB_PERIOD_MONTHS)
                    )
                    HierarchyRow(
                        level = "3",
                        name = stringResource(StringKeyMatch.DASHA_LEVEL_PRATYANTARDASHA),
                        desc = stringResource(StringKeyMatch.DASHA_SUB_SUB_PERIOD_WEEKS)
                    )
                }
            }
        }

        item {
            VimsottariInfoCard(
                icon = Icons.Outlined.Lightbulb,
                title = stringResource(StringKeyMatch.DASHA_PERIOD_INSIGHTS),
                subtitle = "Interpret Mahadasha as the life-theme, Antardasha as the active chapter, and Pratyantardasha as short-term trigger."
            )
        }
    }
}

@Composable
private fun HierarchyRow(level: String, name: String, desc: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(24.dp)
                .background(AppTheme.AccentPrimary.copy(alpha = 0.15f), shape = MaterialTheme.shapes.small),
            contentAlignment = Alignment.Center
        ) {
            Text(text = level, style = MaterialTheme.typography.labelSmall, color = AppTheme.AccentPrimary)
        }
        Spacer(modifier = Modifier.width(10.dp))
        Column {
            Text(text = name, style = MaterialTheme.typography.bodyMedium, color = AppTheme.TextPrimary)
            Text(text = desc, style = MaterialTheme.typography.bodySmall, color = AppTheme.TextSecondary)
        }
    }
}

@Composable
private fun VimsottariInterpretationTab(timeline: DashaCalculator.DashaTimeline) {
    val language = LocalLanguage.current
    val dateSystem = LocalDateSystem.current
    val now = remember(timeline) { timeline.nowInTimelineZone() }
    val today = now.toLocalDate()
    val maha = timeline.currentMahadasha
    val antar = timeline.currentAntardasha
    val prat = timeline.currentPratyantardasha

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(NeoVedicTokens.ScreenPadding),
        verticalArrangement = Arrangement.spacedBy(NeoVedicTokens.SpaceMD)
    ) {
        item {
            VimsottariInfoCard(
                icon = Icons.Outlined.Timeline,
                title = stringResource(StringKeyMatch.DASHA_CURRENT_DASHA_PERIOD),
                subtitle = when {
                    maha == null -> stringResource(StringKeyMatch.MISC_NO_DATA)
                    antar != null -> "${maha.planet.getLocalizedName(language)} -> ${antar.planet.getLocalizedName(language)}"
                    else -> maha.planet.getLocalizedName(language)
                }
            )
        }

        maha?.let { md ->
            item {
                PeriodInterpretationCard(
                    title = stringResource(StringKeyMatch.DASHA_LEVEL_MAHADASHA),
                    planetName = md.planet.getLocalizedName(language),
                    dateRange = "${formatDate(md.startDate.toLocalDate(), dateSystem, language, DateFormat.MONTH_YEAR)} - ${formatDate(md.endDate.toLocalDate(), dateSystem, language, DateFormat.MONTH_YEAR)}",
                    progress = md.getProgressPercent(now).toFloat() / 100f,
                    summary = getMahadashaInterpretation(md.planet)
                )
            }
        }

        antar?.let { ad ->
            item {
                PeriodInterpretationCard(
                    title = stringResource(StringKeyMatch.DASHA_LEVEL_ANTARDASHA),
                    planetName = ad.planet.getLocalizedName(language),
                    dateRange = "${formatDate(ad.startDate.toLocalDate(), dateSystem, language, DateFormat.MONTH_YEAR)} - ${formatDate(ad.endDate.toLocalDate(), dateSystem, language, DateFormat.MONTH_YEAR)}",
                    progress = ad.getProgressPercent(now).toFloat() / 100f,
                    summary = getAntardashaInterpretation(ad.planet)
                )
            }
        }

        prat?.let { pd ->
            item {
                PeriodInterpretationCard(
                    title = stringResource(StringKeyMatch.DASHA_LEVEL_PRATYANTARDASHA),
                    planetName = pd.planet.getLocalizedName(language),
                    dateRange = "${formatDate(pd.startDate.toLocalDate(), dateSystem, language, DateFormat.DAY_MONTH)} - ${formatDate(pd.endDate.toLocalDate(), dateSystem, language, DateFormat.DAY_MONTH)}",
                    progress = calculateProgress(pd.startDate.toLocalDate(), pd.endDate.toLocalDate(), today),
                    summary = getPratyantardashaInterpretation(pd.planet)
                )
            }
        }
    }
}

@Composable
private fun PeriodInterpretationCard(
    title: String,
    planetName: String,
    dateRange: String,
    progress: Float,
    summary: String
) {
    Surface(
        shape = MaterialTheme.shapes.large,
        color = AppTheme.CardBackground
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = title, style = MaterialTheme.typography.titleSmall, color = AppTheme.TextSecondary)
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = planetName, style = MaterialTheme.typography.titleMedium, color = AppTheme.TextPrimary, fontWeight = FontWeight.SemiBold)
            Spacer(modifier = Modifier.height(6.dp))
            Text(text = dateRange, style = MaterialTheme.typography.bodySmall, color = AppTheme.TextSecondary)
            Spacer(modifier = Modifier.height(10.dp))
            LinearProgressIndicator(
                progress = { progress.coerceIn(0f, 1f) },
                modifier = Modifier.fillMaxWidth(),
                color = AppTheme.AccentPrimary
            )
            Spacer(modifier = Modifier.height(12.dp))
            Text(text = summary, style = MaterialTheme.typography.bodyMedium, color = AppTheme.TextPrimary)
        }
    }
}

@Composable
private fun VimsottariInfoCard(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    title: String,
    subtitle: String
) {
    Surface(
        shape = MaterialTheme.shapes.large,
        color = AppTheme.CardBackground
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .background(AppTheme.AccentPrimary.copy(alpha = 0.12f), shape = MaterialTheme.shapes.medium),
                contentAlignment = Alignment.Center
            ) {
                androidx.compose.material3.Icon(icon, contentDescription = null, tint = AppTheme.AccentPrimary)
            }
            Spacer(modifier = Modifier.width(12.dp))
            Column {
                Text(text = title, style = MaterialTheme.typography.titleMedium, color = AppTheme.TextPrimary)
                Spacer(modifier = Modifier.height(2.dp))
                Text(text = subtitle, style = MaterialTheme.typography.bodySmall, color = AppTheme.TextSecondary)
            }
        }
    }
}

@Composable
private fun VimsottariErrorContent(
    paddingValues: PaddingValues,
    message: String,
    onRetry: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(text = message, style = MaterialTheme.typography.bodyMedium, color = AppTheme.TextSecondary)
            Spacer(modifier = Modifier.height(12.dp))
            Button(onClick = onRetry) {
                Text(text = stringResource(StringKey.BTN_RETRY))
            }
        }
    }
}

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

private fun calculateProgress(startDate: LocalDate, endDate: LocalDate, asOfDate: LocalDate): Float {
    val total = ChronoUnit.DAYS.between(startDate, endDate).coerceAtLeast(1)
    val elapsed = ChronoUnit.DAYS.between(startDate, asOfDate).coerceAtLeast(0).coerceAtMost(total)
    return (elapsed.toFloat() / total.toFloat()).coerceIn(0f, 1f)
}

private fun getPratyantardashaInterpretation(planet: Planet): String = when (planet) {
    Planet.SUN -> "Short-term trigger for decisive action, authority visibility, and leadership execution."
    Planet.MOON -> "Short-term trigger for emotional shifts, public engagement, and family focus."
    Planet.MARS -> "Short-term trigger for rapid action, technical execution, and conflict management."
    Planet.MERCURY -> "Short-term trigger for communication, analysis, transactions, and planning."
    Planet.JUPITER -> "Short-term trigger for counsel, ethics, expansion opportunities, and blessings."
    Planet.VENUS -> "Short-term trigger for relationships, agreements, aesthetics, and comforts."
    Planet.SATURN -> "Short-term trigger for discipline, accountability, and structural corrections."
    Planet.RAHU -> "Short-term trigger for unconventional openings, foreign links, and sudden material shifts."
    Planet.KETU -> "Short-term trigger for detachment, insight, and spiritual recalibration."
    else -> "Short-term karmic trigger phase."
}

private fun getAntardashaInterpretation(planet: Planet): String = when (planet) {
    Planet.SUN -> "Sub-period emphasizes authority, visibility, career accountability, and self-definition."
    Planet.MOON -> "Sub-period emphasizes emotional life, adaptability, nurturing roles, and public connection."
    Planet.MARS -> "Sub-period emphasizes initiative, competition, engineering effort, and decisive momentum."
    Planet.MERCURY -> "Sub-period emphasizes intelligence work, communication networks, learning, and trade."
    Planet.JUPITER -> "Sub-period emphasizes growth through dharma, mentorship, teaching, and wise expansion."
    Planet.VENUS -> "Sub-period emphasizes harmony, relationships, artistry, material comforts, and refinement."
    Planet.SATURN -> "Sub-period emphasizes karma settlement through duty, patience, and sustained discipline."
    Planet.RAHU -> "Sub-period emphasizes ambitious leaps, foreign influences, technology, and risk-reward swings."
    Planet.KETU -> "Sub-period emphasizes introspection, karmic release, and non-material reorientation."
    else -> "Sub-period modifies the Mahadasha through practical events and focused priorities."
}

private fun getMahadashaInterpretation(planet: Planet): String = when (planet) {
    Planet.SUN -> "Core life-theme of identity, authority, leadership, recognition, and responsibility."
    Planet.MOON -> "Core life-theme of mind, emotional nourishment, home, adaptability, and public sensitivity."
    Planet.MARS -> "Core life-theme of action, courage, strategy, competition, and execution."
    Planet.MERCURY -> "Core life-theme of intellect, communication, skills, commerce, and information mastery."
    Planet.JUPITER -> "Core life-theme of dharma, wisdom, expansion, children, teaching, and grace."
    Planet.VENUS -> "Core life-theme of relationships, aesthetics, values, enjoyment, and prosperity."
    Planet.SATURN -> "Core life-theme of karma, endurance, responsibility, maturity, and long-term construction."
    Planet.RAHU -> "Core life-theme of material ambition, boundary-breaking, innovation, and worldly experimentation."
    Planet.KETU -> "Core life-theme of detachment, spiritualization, inner truth, and karmic resolution."
    else -> "Core life-theme of karmic development."
}

