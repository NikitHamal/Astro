package com.astro.storm.ui.screen

import androidx.compose.animation.animateContentSize
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
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.Timeline
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
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
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.compose.foundation.interaction.MutableInteractionSource
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
    val pratyantardasha: String?,
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
    var showInfoDialog by rememberSaveable { mutableStateOf(false) }

    LaunchedEffect(chart) { viewModel.loadDashaTimeline(chart) }

    val currentPeriodInfo = remember(uiState, language) {
        extractCurrentVimsottariPeriodInfo(uiState, language)
    }

    if (showInfoDialog) {
        VimsottariInfoDialog(onDismiss = { showInfoDialog = false })
    }

    Scaffold(
        containerColor = AppTheme.ScreenBackground,
        topBar = {
            VimsottariDashaTopBar(
                chartName = chart?.birthData?.name ?: stringResource(StringKey.NO_PROFILE_MESSAGE),
                currentPeriodInfo = currentPeriodInfo,
                onBack = onBack,
                onInfoClick = { showInfoDialog = true }
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
                        VimsottariTab.ABOUT -> VimsottariAboutTab(timeline = state.timeline)
                        VimsottariTab.TIMELINE -> VimsottariTimelineTab(timeline = state.timeline)
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
    onInfoClick: () -> Unit
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
            currentPeriodInfo.pratyantardasha?.let {
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
        actionIcon = Icons.Outlined.Info,
        onAction = onInfoClick
    )
}

@Composable
private fun VimsottariTabRow(
    selectedTab: Int,
    onTabSelected: (Int) -> Unit
) {
    val tabs = listOf(
        TabItem(title = stringResource(StringKeyDosha.SCREEN_ABOUT), accentColor = AppTheme.AccentPrimary),
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
private fun VimsottariAboutTab(timeline: DashaCalculator.DashaTimeline) {
    val language = LocalLanguage.current
    val dateSystem = LocalDateSystem.current
    val now = remember(timeline) { timeline.nowInTimelineZone() }
    val nextMahadasha = remember(timeline, now) { timeline.getNextMahadasha(now) }
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
            ActiveDashaCard(timeline = timeline, now = now)
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

        nextMahadasha?.let { next ->
            item {
                Surface(
                    shape = MaterialTheme.shapes.large,
                    color = AppTheme.CardBackground
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = stringResource(StringKeyMatch.DASHA_NEXT_PERIOD),
                            style = MaterialTheme.typography.titleSmall,
                            color = AppTheme.TextSecondary
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = next.planet.getLocalizedName(language),
                            style = MaterialTheme.typography.titleMedium,
                            color = AppTheme.TextPrimary,
                            fontWeight = FontWeight.SemiBold
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "${formatDate(next.startDate.toLocalDate(), dateSystem, language, DateFormat.MONTH_YEAR)} - ${formatDate(next.endDate.toLocalDate(), dateSystem, language, DateFormat.MONTH_YEAR)}",
                            style = MaterialTheme.typography.bodySmall,
                            color = AppTheme.TextSecondary
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun ActiveDashaCard(
    timeline: DashaCalculator.DashaTimeline,
    now: java.time.LocalDateTime
) {
    val language = LocalLanguage.current
    val dateSystem = LocalDateSystem.current

    val maha = timeline.currentMahadasha
    val antar = timeline.currentAntardasha
    val praty = timeline.currentPratyantardasha

    Surface(
        shape = MaterialTheme.shapes.large,
        color = AppTheme.CardBackground
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Outlined.Timeline,
                    contentDescription = null,
                    tint = AppTheme.AccentPrimary,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = stringResource(StringKeyMatch.DASHA_CURRENT_DASHA_PERIOD),
                    style = MaterialTheme.typography.titleMedium,
                    color = AppTheme.TextPrimary
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            maha?.let {
                ActiveLevelRow(
                    label = stringResource(StringKeyMatch.DASHA_LEVEL_MAHADASHA),
                    planet = it.planet,
                    dateRange = "${formatDate(it.startDate.toLocalDate(), dateSystem, language, DateFormat.MONTH_YEAR)} - ${formatDate(it.endDate.toLocalDate(), dateSystem, language, DateFormat.MONTH_YEAR)}",
                    progress = it.getProgressPercent(now).toFloat() / 100f
                )
            }

            antar?.let {
                ActiveLevelRow(
                    label = stringResource(StringKeyMatch.DASHA_LEVEL_ANTARDASHA),
                    planet = it.planet,
                    dateRange = "${formatDate(it.startDate.toLocalDate(), dateSystem, language, DateFormat.MONTH_YEAR)} - ${formatDate(it.endDate.toLocalDate(), dateSystem, language, DateFormat.MONTH_YEAR)}",
                    progress = it.getProgressPercent(now).toFloat() / 100f
                )
            }

            praty?.let {
                ActiveLevelRow(
                    label = stringResource(StringKeyMatch.DASHA_LEVEL_PRATYANTARDASHA),
                    planet = it.planet,
                    dateRange = "${formatDate(it.startDate.toLocalDate(), dateSystem, language, DateFormat.DAY_MONTH)} - ${formatDate(it.endDate.toLocalDate(), dateSystem, language, DateFormat.DAY_MONTH)}",
                    progress = calculateProgress(it.startDate.toLocalDate(), it.endDate.toLocalDate(), now.toLocalDate())
                )
            }
        }
    }
}

@Composable
private fun ActiveLevelRow(
    label: String,
    planet: Planet,
    dateRange: String,
    progress: Float
) {
    val language = LocalLanguage.current

    Column(modifier = Modifier.padding(bottom = 10.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = label,
                style = MaterialTheme.typography.labelMedium,
                color = AppTheme.TextSecondary
            )
            Text(
                text = planet.getLocalizedName(language),
                style = MaterialTheme.typography.bodyMedium,
                color = AppTheme.TextPrimary,
                fontWeight = FontWeight.Medium
            )
        }
        Text(
            text = dateRange,
            style = MaterialTheme.typography.bodySmall,
            color = AppTheme.TextSecondary,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
        Spacer(modifier = Modifier.height(6.dp))
        LinearProgressIndicator(
            progress = { progress.coerceIn(0f, 1f) },
            modifier = Modifier.fillMaxWidth(),
            color = AppTheme.AccentPrimary
        )
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
private fun VimsottariTimelineTab(timeline: DashaCalculator.DashaTimeline) {
    val language = LocalLanguage.current
    val dateSystem = LocalDateSystem.current
    val now = remember(timeline) { timeline.nowInTimelineZone() }
    var expandedMahadashaKeys by rememberSaveable { mutableStateOf(setOf<String>()) }

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(NeoVedicTokens.ScreenPadding),
        verticalArrangement = Arrangement.spacedBy(NeoVedicTokens.SpaceMD)
    ) {
        item {
            Surface(
                shape = MaterialTheme.shapes.large,
                color = AppTheme.CardBackground
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = stringResource(StringKeyDosha.DASHA_TITLE_VIMSOTTARI),
                        style = MaterialTheme.typography.titleMedium,
                        color = AppTheme.TextPrimary
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "120-year cycle with Mahadasha -> Antardasha -> Pratyantardasha.",
                        style = MaterialTheme.typography.bodySmall,
                        color = AppTheme.TextSecondary
                    )
                }
            }
        }

        itemsIndexed(
            items = timeline.mahadashas,
            key = { index, mahadasha ->
                "mahadasha_${mahadasha.planet.symbol}_${mahadasha.startDate.toLocalDate().toEpochDay()}_$index"
            }
        ) { _, mahadasha ->
            val cardKey = "${mahadasha.planet.symbol}_${mahadasha.startDate.toLocalDate().toEpochDay()}"
            val isExpanded = cardKey in expandedMahadashaKeys
            val isCurrent = mahadasha == timeline.currentMahadasha
            val currentAntardasha = if (isCurrent) timeline.currentAntardasha else null
            val currentPratyantardasha = if (isCurrent) timeline.currentPratyantardasha else null
            val expandRotation by animateFloatAsState(
                targetValue = if (isExpanded) 180f else 0f,
                animationSpec = tween(220),
                label = "vimsottari_expand_rotation"
            )

            Surface(
                shape = RoundedCornerShape(NeoVedicTokens.ElementCornerRadius),
                color = if (isCurrent) AppTheme.AccentPrimary.copy(alpha = 0.08f) else AppTheme.CardBackground
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = rememberRipple(),
                            onClick = {
                                expandedMahadashaKeys = if (isExpanded) {
                                    expandedMahadashaKeys - cardKey
                                } else {
                                    expandedMahadashaKeys + cardKey
                                }
                            }
                        )
                        .animateContentSize()
                        .padding(16.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = mahadasha.planet.getLocalizedName(language),
                            modifier = Modifier.weight(1f),
                            style = MaterialTheme.typography.titleMedium,
                            color = AppTheme.TextPrimary,
                            fontWeight = FontWeight.SemiBold
                        )
                        if (isCurrent) {
                            Text(
                                text = stringResource(StringKeyMatch.MISC_CURRENT),
                                style = MaterialTheme.typography.labelSmall,
                                color = AppTheme.AccentPrimary,
                                modifier = Modifier.padding(end = 10.dp)
                            )
                        }
                        Icon(
                            imageVector = Icons.Filled.ExpandMore,
                            contentDescription = null,
                            tint = AppTheme.TextSecondary,
                            modifier = Modifier.rotate(expandRotation)
                        )
                    }

                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "${formatDurationYearsMonths(mahadasha.durationYears)} | ${formatDate(mahadasha.startDate.toLocalDate(), dateSystem, language, DateFormat.MONTH_YEAR)} - ${formatDate(mahadasha.endDate.toLocalDate(), dateSystem, language, DateFormat.MONTH_YEAR)}",
                        style = MaterialTheme.typography.bodySmall,
                        color = AppTheme.TextSecondary
                    )

                    if (isCurrent) {
                        Spacer(modifier = Modifier.height(8.dp))
                        LinearProgressIndicator(
                            progress = { mahadasha.getProgressPercent(now).toFloat() / 100f },
                            modifier = Modifier.fillMaxWidth(),
                            color = AppTheme.AccentPrimary
                        )
                    }

                    if (isExpanded) {
                        Spacer(modifier = Modifier.height(12.dp))
                        HorizontalDivider(color = AppTheme.DividerColor)
                        Spacer(modifier = Modifier.height(10.dp))

                        mahadasha.antardashas.forEach { antardasha ->
                            val isCurrentAntardasha = antardasha == currentAntardasha
                            Text(
                                text = antardasha.planet.getLocalizedName(language),
                                style = MaterialTheme.typography.bodyMedium,
                                color = AppTheme.TextPrimary,
                                fontWeight = if (isCurrentAntardasha) FontWeight.SemiBold else FontWeight.Normal
                            )
                            Text(
                                text = "${formatDate(antardasha.startDate.toLocalDate(), dateSystem, language, DateFormat.MONTH_YEAR)} - ${formatDate(antardasha.endDate.toLocalDate(), dateSystem, language, DateFormat.MONTH_YEAR)}",
                                style = MaterialTheme.typography.bodySmall,
                                color = AppTheme.TextSecondary
                            )

                            if (isCurrentAntardasha) {
                                Spacer(modifier = Modifier.height(4.dp))
                                LinearProgressIndicator(
                                    progress = { antardasha.getProgressPercent(now).toFloat() / 100f },
                                    modifier = Modifier.fillMaxWidth(),
                                    color = AppTheme.AccentTeal
                                )

                                if (antardasha.pratyantardashas.isNotEmpty()) {
                                    Spacer(modifier = Modifier.height(6.dp))
                                    antardasha.pratyantardashas.forEach { praty ->
                                        val isCurrentPraty = praty == currentPratyantardasha
                                        Text(
                                            text = "- ${praty.planet.getLocalizedName(language)} | ${formatDate(praty.startDate.toLocalDate(), dateSystem, language, DateFormat.DAY_MONTH)} - ${formatDate(praty.endDate.toLocalDate(), dateSystem, language, DateFormat.DAY_MONTH)}${if (isCurrentPraty) " [current]" else ""}",
                                            style = MaterialTheme.typography.bodySmall,
                                            color = if (isCurrentPraty) AppTheme.AccentGold else AppTheme.TextSecondary,
                                            modifier = Modifier.padding(start = 8.dp, bottom = 2.dp)
                                        )
                                    }
                                }
                            }

                            Spacer(modifier = Modifier.height(8.dp))
                        }
                    }
                }
            }
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
private fun VimsottariInfoDialog(onDismiss: () -> Unit) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = stringResource(StringKeyDosha.DASHA_TITLE_VIMSOTTARI),
                style = MaterialTheme.typography.titleLarge,
                color = AppTheme.TextPrimary
            )
        },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text(
                    text = stringResource(StringKeyDosha.DASHA_VIMSOTTARI_DESC),
                    style = MaterialTheme.typography.bodyMedium,
                    color = AppTheme.TextPrimary
                )
                Text(
                    text = "This screen focuses on Mahadasha, Antardasha, and Pratyantardasha.",
                    style = MaterialTheme.typography.bodySmall,
                    color = AppTheme.TextSecondary
                )
                Text(
                    text = "Use Timeline for full period flow and Interpretation for active-period guidance.",
                    style = MaterialTheme.typography.bodySmall,
                    color = AppTheme.TextSecondary
                )
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text(text = stringResource(StringKey.BTN_CLOSE), color = AppTheme.AccentPrimary)
            }
        },
        containerColor = AppTheme.CardBackground
    )
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
            val pd = uiState.timeline.currentPratyantardasha
            CurrentVimsottariPeriodInfo(
                mahadasha = md?.planet?.getLocalizedName(language),
                antardasha = ad?.planet?.getLocalizedName(language),
                pratyantardasha = pd?.planet?.getLocalizedName(language),
                isLoading = false,
                hasError = false
            )
        }
        is DashaUiState.Loading -> CurrentVimsottariPeriodInfo(null, null, null, true, false)
        is DashaUiState.Error -> CurrentVimsottariPeriodInfo(null, null, null, false, true)
        is DashaUiState.Idle -> CurrentVimsottariPeriodInfo(null, null, null, false, false)
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

