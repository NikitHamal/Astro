package com.astro.storm.ui.screen

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.outlined.CalendarToday
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsStateWithLifecycle
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.astro.storm.core.common.Language
import com.astro.storm.core.common.StringKey
import com.astro.storm.core.common.StringKeyDashaInterpretations
import com.astro.storm.core.common.StringResources
import com.astro.storm.core.common.StringKeyUIExtra
import com.astro.storm.core.common.getLocalizedName
import com.astro.storm.data.localization.LocalLanguage
import com.astro.storm.data.localization.stringResource
import com.astro.storm.core.model.Planet
import com.astro.storm.core.model.VedicChart
import com.astro.storm.ephemeris.DashaCalculator
import com.astro.storm.ui.components.common.ModernPillTabRow
import com.astro.storm.ui.components.common.NeoVedicPageHeader
import com.astro.storm.ui.components.common.TabItem
import com.astro.storm.ui.theme.AppTheme
import com.astro.storm.ui.viewmodel.DashaUiState
import com.astro.storm.ui.viewmodel.DashaViewModel
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Locale

/**
 * Vimsottari Dasha Screen
 *
 * Comprehensive display of the 120-year Vimsottari Dasha system.
 * Shows applicability, timeline with 3 levels (Mahadasha, Antardasha, Pratyantardasha),
 * current periods, and detailed interpretations.
 *
 * Key Features:
 * - Based on Moon's Nakshatra position
 * - 120-year complete cycle
 * - 9 planet lords in specific sequence
 * - Detailed interpretations for Mahadasha and Antardasha
 *
 * Reference: BPHS Chapter 48, Brihat Parashara Hora
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VimsottariDashaScreen(
    chart: VedicChart?,
    onBack: () -> Unit,
    viewModel: DashaViewModel = hiltViewModel()
) {
    val language = LocalLanguage.current
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    var selectedTab by rememberSaveable { mutableIntStateOf(0) }
    var showInfoDialog by remember { mutableStateOf(false) }
    var expandedMahadashaIndex by remember { mutableIntStateOf(-1) }
    var expandedAntardashaIndex by remember { mutableIntStateOf(-1) }

    val tabs = listOf(
        stringResource(StringKeyDosha.ASHTOTTARI_ABOUT),
        stringResource(StringKeyDosha.ASHTOTTARI_TIMELINE),
        stringResource(StringKeyDosha.SCREEN_INTERPRETATION)
    )

    LaunchedEffect(chart) {
        viewModel.loadDashaTimeline(chart)
    }

    if (showInfoDialog) {
        VimsottariInfoDialog(onDismiss = { showInfoDialog = false })
    }

    Scaffold(
        containerColor = AppTheme.ScreenBackground,
        topBar = {
            VimsottariDashaTopBar(
                chartName = chart?.birthData?.name ?: stringResource(StringKey.NO_PROFILE_MESSAGE),
                uiState = uiState,
                onBack = onBack,
                onInfoClick = { showInfoDialog = true },
                onTodayClick = { viewModel.requestScrollToToday() }
            )
        }
    ) { paddingValues ->
        when (val state = uiState) {
            is DashaUiState.Loading -> VimsottariLoadingContent(paddingValues)
            is DashaUiState.Success -> {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                ) {
                    // Tab selector
                    TabSelector(
                        tabs = tabs,
                        selectedIndex = selectedTab,
                        onTabSelected = { selectedTab = it }
                    )

                    // Content based on selected tab
                    when (selectedTab) {
                        0 -> AboutContent(state.timeline, language)
                        1 -> TimelineContent(
                            result = state.timeline,
                            expandedMahadashaIndex = expandedMahadashaIndex,
                            expandedAntardashaIndex = expandedAntardashaIndex,
                            onMahadashaExpandChange = { expandedMahadashaIndex = if (expandedMahadashaIndex == it) -1 else it },
                            onAntardashaExpandChange = { expandedAntardashaIndex = if (expandedAntardashaIndex == it) -1 else it },
                            language = language
                        )
                        2 -> InterpretationContent(state.timeline, language)
                    }
                }
            }
            is DashaUiState.Error -> VimsottariErrorContent(
                message = state.message,
                paddingValues = paddingValues,
                onRetry = { viewModel.loadDashaTimeline(chart) }
            )
            is DashaUiState.Idle -> EmptyChartScreen(
                title = "Vimsottari Dasha",
                message = stringResource(StringKey.NO_PROFILE_MESSAGE),
                onBack = onBack
            )
        }
    }
}

@Composable
private fun VimsottariDashaTopBar(
    chartName: String,
    uiState: DashaUiState,
    onBack: () -> Unit,
    onInfoClick: () -> Unit,
    onTodayClick: () -> Unit
) {
    val language = LocalLanguage.current
    val subtitle = when (val state = uiState) {
        is DashaUiState.Loading -> stringResource(StringKey.DASHA_CALCULATING)
        is DashaUiState.Error -> "${stringResource(StringKey.DASHA_ERROR)} - $chartName"
        is DashaUiState.Success -> buildString {
            state.timeline.currentMahadasha?.let { md ->
                append(md.planet.getLocalizedName(language))
                state.timeline.currentAntardasha?.let { ad ->
                    append(StringResources.get(StringKeyUIExtra.ARROW, language) + ad.planet.getLocalizedName(language))
                    state.timeline.currentPratyantardasha?.let { pd ->
                        append(StringResources.get(StringKeyUIExtra.ARROW, language) + pd.planet.getLocalizedName(language))
                    }
                }
                append(StringResources.get(StringKeyUIExtra.BULLET_SPACE, language))
            }
            append(chartName)
        }
        is DashaUiState.Idle -> chartName
    }
    NeoVedicPageHeader(
        title = "Vimsottari Dasha",
        subtitle = subtitle,
        onBack = onBack,
        actionIcon = Icons.Outlined.Info,
        onAction = onInfoClick
    )
}

@Composable
private fun TabSelector(
    tabs: List<String>,
    selectedIndex: Int,
    onTabSelected: (Int) -> Unit
) {
    val tabItems = tabs.map { TabItem(title = it, accentColor = AppTheme.AccentPrimary) }

    ModernPillTabRow(
        tabs = tabItems,
        selectedIndex = selectedIndex,
        onTabSelected = onTabSelected,
        modifier = Modifier.padding(
            horizontal = com.astro.storm.ui.theme.NeoVedicTokens.ScreenPadding,
            vertical = com.astro.storm.ui.theme.NeoVedicTokens.SpaceXS
        )
    )
}

@Composable
private fun LoadingContent(paddingValues: PaddingValues) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            CircularProgressIndicator(color = AppTheme.AccentPrimary)
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                stringResource(StringKeyDosha.SCREEN_CALCULATING),
                color = AppTheme.TextMuted
            )
        }
    }
}

@Composable
private fun VimsottariLoadingContent(paddingValues: PaddingValues) {
    LoadingContent(paddingValues)
}

@Composable
private fun VimsottariErrorContent(
    message: String,
    paddingValues: PaddingValues,
    onRetry: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Icon(
                Icons.Outlined.Info,
                contentDescription = null,
                tint = AppTheme.ErrorColor,
                modifier = Modifier.size(48.dp)
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(message, color = AppTheme.TextMuted)
            Spacer(modifier = Modifier.height(16.dp))
            androidx.compose.material3.Button(
                onClick = onRetry
            ) {
                Text(stringResource(StringKey.BTN_RETRY))
            }
        }
    }
}

@Composable
private fun AboutContent(
    timeline: DashaCalculator.DashaTimeline,
    language: Language
) {
    val locale = if (language == Language.NEPALI) Locale("ne", "NP") else Locale.ENGLISH
    val dateFormatter = DateTimeFormatter.ofPattern("dd MMM yyyy", locale)

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(com.astro.storm.ui.theme.NeoVedicTokens.ScreenPadding),
        verticalArrangement = Arrangement.spacedBy(com.astro.storm.ui.theme.NeoVedicTokens.SpaceMD)
    ) {
        // System Info Card
        item {
            SystemInfoCard(timeline, language, dateFormatter)
        }

        // Current Period Card
        if (timeline.currentMahadasha != null) {
            item {
                CurrentVimsottariPeriodCard(timeline, language, dateFormatter)
            }
        }

        // Birth Details Card
        item {
            BirthDetailsCard(timeline, language)
        }
    }
}

@Composable
private fun SystemInfoCard(
    timeline: DashaCalculator.DashaTimeline,
    language: Language,
    dateFormatter: DateTimeFormatter
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = AppTheme.CardBackground),
        shape = RoundedCornerShape(com.astro.storm.ui.theme.NeoVedicTokens.ElementCornerRadius)
    ) {
        Column(
            modifier = Modifier.padding(com.astro.storm.ui.theme.NeoVedicTokens.ScreenPadding)
        ) {
            Text(
                "About Vimsottari Dasha",
                fontWeight = FontWeight.SemiBold,
                fontSize = 16.sp,
                color = AppTheme.TextPrimary
            )

            Spacer(modifier = Modifier.height(com.astro.storm.ui.theme.NeoVedicTokens.SpaceMD))

            Text(
                "Vimsottari is the most widely used Nakshatra-based planetary period system in Vedic astrology. It spans 120 years and uses 9 planetary lords in a specific sequence determined by the Moon's birth Nakshatra.",
                fontSize = 14.sp,
                color = AppTheme.TextSecondary,
                lineHeight = 20.sp
            )

            Spacer(modifier = Modifier.height(12.dp))

            HorizontalDivider(color = AppTheme.DividerColor)

            Spacer(modifier = Modifier.height(12.dp))

            // Planet periods
            val periods = listOf(
                "Ketu: 6 years" to "केतु: ६ वर्ष",
                "Venus: 20 years" to "शुक्र: २० वर्ष",
                "Sun: 6 years" to "सूर्य: ६ वर्ष",
                "Moon: 10 years" to "चन्द्र: १० वर्ष",
                "Mars: 7 years" to "मंगल: ७ वर्ष",
                "Rahu: 18 years" to "राहु: १८ वर्ष",
                "Jupiter: 16 years" to "बृहस्पति: १६ वर्ष",
                "Saturn: 19 years" to "शनि: १९ वर्ष",
                "Mercury: 17 years" to "बुध: १७ वर्ष"
            )

            periods.chunked(2).forEach { row ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    row.forEach { (en, ne) ->
                        Text(
                            if (language == Language.NEPALI) ne else en,
                            color = AppTheme.TextSecondary,
                            fontSize = 12.sp,
                            modifier = Modifier.weight(1f)
                        )
                    }
                    if (row.size == 1) {
                        Spacer(modifier = Modifier.weight(1f))
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            HorizontalDivider(color = AppTheme.DividerColor)

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                "Total Cycle: 120 Years",
                color = AppTheme.AccentPrimary,
                fontWeight = FontWeight.Medium,
                fontSize = 14.sp
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                "9 Planets Used",
                color = AppTheme.TextMuted,
                fontSize = 12.sp
            )
        }
    }
}

@Composable
private fun CurrentVimsottariPeriodCard(
    timeline: DashaCalculator.DashaTimeline,
    language: Language,
    dateFormatter: DateTimeFormatter
) {
    val mahadasha = timeline.currentMahadasha ?: return
    val antardasha = timeline.currentAntardasha
    val pratyantardasha = timeline.currentPratyantardasha

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = AppTheme.AccentPrimary.copy(alpha = 0.1f)
        ),
        shape = RoundedCornerShape(com.astro.storm.ui.theme.NeoVedicTokens.ElementCornerRadius)
    ) {
        Column(
            modifier = Modifier.padding(com.astro.storm.ui.theme.NeoVedicTokens.ScreenPadding)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    Icons.Outlined.CalendarToday,
                    contentDescription = null,
                    tint = AppTheme.AccentPrimary,
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    "Current Period",
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    color = AppTheme.TextPrimary
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Current Mahadasha
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        AppTheme.AccentPrimary.copy(alpha = 0.1f),
                        RoundedCornerShape(com.astro.storm.ui.theme.NeoVedicTokens.ElementCornerRadius)
                    )
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        stringResource(StringKey.DASHA_MAHADASHA),
                        color = AppTheme.TextMuted,
                        fontSize = 12.sp
                    )
                    Text(
                        mahadasha.planet.getLocalizedName(language),
                        color = AppTheme.TextPrimary,
                        fontWeight = FontWeight.Bold,
                        fontSize = 17.sp
                    )
                }
                Column(horizontalAlignment = Alignment.End) {
                    Text(
                        mahadasha.startDate.format(dateFormatter),
                        color = AppTheme.TextMuted,
                        fontSize = 11.sp
                    )
                    Text(
                        "- ${mahadasha.endDate.format(dateFormatter)}",
                        color = AppTheme.TextMuted,
                        fontSize = 11.sp
                    )
                }
            }

            // Current Antardasha
            if (antardasha != null) {
                Spacer(modifier = Modifier.height(12.dp))

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            AppTheme.AccentSecondary.copy(alpha = 0.1f),
                            RoundedCornerShape(com.astro.storm.ui.theme.NeoVedicTokens.ElementCornerRadius)
                        )
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            stringResource(StringKey.DASHA_ANTARDASHA),
                            color = AppTheme.TextMuted,
                            fontSize = 12.sp
                        )
                        Text(
                            antardasha.planet.getLocalizedName(language),
                            color = AppTheme.TextPrimary,
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 16.sp
                        )
                    }
                    Column(horizontalAlignment = Alignment.End) {
                        Text(
                            antardasha.startDate.format(dateFormatter),
                            color = AppTheme.TextMuted,
                            fontSize = 11.sp
                        )
                        Text(
                            "- ${antardasha.endDate.format(dateFormatter)}",
                            color = AppTheme.TextMuted,
                            fontSize = 11.sp
                        )
                    }
                }
            }

            // Current Pratyantardasha
            if (pratyantardasha != null) {
                Spacer(modifier = Modifier.height(12.dp))

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            AppTheme.SuccessColor.copy(alpha = 0.1f),
                            RoundedCornerShape(com.astro.storm.ui.theme.NeoVedicTokens.ElementCornerRadius)
                        )
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            stringResource(StringKey.DASHA_PRATYANTARDASHA),
                            color = AppTheme.TextMuted,
                            fontSize = 12.sp
                        )
                        Text(
                            pratyantardasha.planet.getLocalizedName(language),
                            color = AppTheme.TextPrimary,
                            fontWeight = FontWeight.Medium,
                            fontSize = 15.sp
                        )
                    }
                    Column(horizontalAlignment = Alignment.End) {
                        Text(
                            pratyantardasha.startDate.format(dateFormatter),
                            color = AppTheme.TextMuted,
                            fontSize = 11.sp
                        )
                        Text(
                            "- ${pratyantardasha.endDate.format(dateFormatter)}",
                            color = AppTheme.TextMuted,
                            fontSize = 11.sp
                        )
                    }
                }
            }

            // Progress Bar
            Spacer(modifier = Modifier.height(16.dp))

            val progress = mahadasha.getProgressPercent()
            Column {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        stringResource(StringKey.DASHA_PROGRESS),
                        color = AppTheme.TextMuted,
                        fontSize = 12.sp
                    )
                    Text(
                        "${progress.toInt()}%",
                        color = AppTheme.AccentPrimary,
                        fontWeight = FontWeight.Medium,
                        fontSize = 12.sp
                    )
                }
                Spacer(modifier = Modifier.height(4.dp))
                LinearProgressIndicator(
                    progress = { progress.toFloat() / 100 },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(6.dp)
                        .clip(RoundedCornerShape(com.astro.storm.ui.theme.NeoVedicTokens.ElementCornerRadius)),
                    color = AppTheme.AccentPrimary,
                    trackColor = AppTheme.AccentPrimary.copy(alpha = 0.2f)
                )
            }
        }
    }
}

@Composable
private fun BirthDetailsCard(
    timeline: DashaCalculator.DashaTimeline,
    language: Language
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = AppTheme.CardBackground),
        shape = RoundedCornerShape(com.astro.storm.ui.theme.NeoVedicTokens.ElementCornerRadius)
    ) {
        Column(
            modifier = Modifier.padding(com.astro.storm.ui.theme.NeoVedicTokens.ScreenPadding)
        ) {
            Text(
                "Birth Details",
                fontWeight = FontWeight.SemiBold,
                fontSize = 14.sp,
                color = AppTheme.TextPrimary,
                lineHeight = 20.sp
            )

            Spacer(modifier = Modifier.height(com.astro.storm.ui.theme.NeoVedicTokens.SpaceMD))

            // Birth Nakshatra
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    stringResource(StringKeyDosha.BIRTH_NAKSHATRA),
                    color = AppTheme.TextMuted,
                    fontSize = 13.sp
                )
                Text(
                    timeline.birthNakshatra.getLocalizedName(language),
                    color = AppTheme.TextPrimary,
                    fontWeight = FontWeight.Medium,
                    fontSize = 13.sp
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Starting Lord
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    "Starting Lord",
                    color = AppTheme.TextMuted,
                    fontSize = 13.sp
                )
                Text(
                    timeline.birthNakshatraLord.getLocalizedName(language),
                    color = AppTheme.TextPrimary,
                    fontWeight = FontWeight.Medium,
                    fontSize = 13.sp
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Balance at Birth
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    stringResource(StringKeyDosha.ASHTOTTARI_BALANCE),
                    color = AppTheme.TextMuted,
                    fontSize = 13.sp
                )
                Text(
                    String.format("%.2f %s", timeline.balanceOfFirstDasha, stringResource(StringKey.YEARS)),
                    color = AppTheme.TextPrimary,
                    fontWeight = FontWeight.Medium,
                    fontSize = 13.sp
                )
            }
        }
    }
}

@Composable
private fun TimelineContent(
    result: DashaCalculator.DashaTimeline,
    expandedMahadashaIndex: Int,
    expandedAntardashaIndex: Int,
    onMahadashaExpandChange: (Int) -> Unit,
    onAntardashaExpandChange: (Int) -> Unit,
    language: Language
) {
    val locale = if (language == Language.NEPALI) Locale("ne", "NP") else Locale.ENGLISH
    val dateFormatter = DateTimeFormatter.ofPattern("dd MMM yyyy", locale)

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        itemsIndexed(result.mahadashas) { mdIndex, mahadasha ->
            VimsottariMahadashaTimelineCard(
                mahadasha = mahadasha,
                isExpanded = expandedMahadashaIndex == mdIndex,
                onExpandChange = { onMahadashaExpandChange(mdIndex) },
                isCurrent = mahadasha.isActive,
                expandedAntardashaIndex = expandedAntardashaIndex,
                onAntardashaExpandChange = onAntardashaExpandChange,
                language = language,
                dateFormatter = dateFormatter
            )
        }
    }
}

@Composable
private fun VimsottariMahadashaTimelineCard(
    mahadasha: DashaCalculator.Mahadasha,
    isExpanded: Boolean,
    onExpandChange: () -> Unit,
    isCurrent: Boolean,
    expandedAntardashaIndex: Int,
    onAntardashaExpandChange: (Int) -> Unit,
    language: Language,
    dateFormatter: DateTimeFormatter
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onExpandChange() },
        colors = CardDefaults.cardColors(
            containerColor = if (isCurrent)
                AppTheme.AccentPrimary.copy(alpha = 0.15f)
            else
                AppTheme.CardBackground
        ),
        shape = RoundedCornerShape(com.astro.storm.ui.theme.NeoVedicTokens.ElementCornerRadius)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    // Planet indicator
                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .background(
                                getPlanetColor(mahadasha.planet).copy(alpha = 0.2f),
                                CircleShape
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            getPlanetAbbreviation(mahadasha.planet),
                            color = getPlanetColor(mahadasha.planet),
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp
                        )
                    }

                    Spacer(modifier = Modifier.width(12.dp))

                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                mahadasha.planet.getLocalizedName(language),
                                fontWeight = FontWeight.SemiBold,
                                fontSize = 16.sp,
                                color = AppTheme.TextPrimary
                            )
                            if (isCurrent) {
                                Spacer(modifier = Modifier.width(8.dp))
                                Surface(
                                    color = AppTheme.SuccessColor.copy(alpha = 0.2f),
                                    shape = RoundedCornerShape(com.astro.storm.ui.theme.NeoVedicTokens.ElementCornerRadius)
                                ) {
                                    Text(
                                        "Current",
                                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                                        color = AppTheme.SuccessColor,
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Medium
                                    )
                                }
                            }
                        }
                        Text(
                            "${String.format("%.1f", mahadasha.durationYears)} ${stringResource(StringKey.YEARS)}",
                            color = AppTheme.TextMuted,
                            fontSize = 12.sp
                        )
                    }
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Column(horizontalAlignment = Alignment.End) {
                        Text(
                            mahadasha.startDate.format(dateFormatter),
                            color = AppTheme.TextMuted,
                            fontSize = 11.sp
                        )
                        Text(
                            mahadasha.endDate.format(dateFormatter),
                            color = AppTheme.TextMuted,
                            fontSize = 11.sp
                        )
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Icon(
                        if (isExpanded) Icons.Filled.ExpandLess else Icons.Filled.ExpandMore,
                        contentDescription = null,
                        tint = AppTheme.TextMuted
                    )
                }
            }

            // Progress if current
            if (isCurrent) {
                Spacer(modifier = Modifier.height(8.dp))
                val progress = mahadasha.getProgressPercent()
                LinearProgressIndicator(
                    progress = { progress.toFloat() / 100 },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(4.dp)
                        .clip(RoundedCornerShape(com.astro.storm.ui.theme.NeoVedicTokens.ElementCornerRadius)),
                    color = AppTheme.AccentPrimary,
                    trackColor = AppTheme.AccentPrimary.copy(alpha = 0.2f)
                )
            }

            // Expanded content - Antardashas
            AnimatedVisibility(
                visible = isExpanded,
                enter = expandVertically() + fadeIn(),
                exit = shrinkVertically() + fadeOut()
            ) {
                Column(
                    modifier = Modifier.padding(top = 12.dp)
                ) {
                    HorizontalDivider(color = AppTheme.DividerColor)
                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        stringResource(StringKey.DASHA_ANTARDASHA),
                        fontWeight = FontWeight.Medium,
                        fontSize = 13.sp,
                        color = AppTheme.TextSecondary
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    mahadasha.antardashas.forEachIndexed { adIndex, antardasha ->
                        VimsottariAntardashaRow(
                            antardasha = antardasha,
                            isExpanded = expandedAntardashaIndex == adIndex,
                            onExpandChange = { onAntardashaExpandChange(adIndex) },
                            language = language,
                            dateFormatter = dateFormatter
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun VimsottariAntardashaRow(
    antardasha: DashaCalculator.Antardasha,
    isExpanded: Boolean,
    onExpandChange: () -> Unit,
    language: Language,
    dateFormatter: DateTimeFormatter
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
            .background(
                if (antardasha.isActive)
                    AppTheme.AccentPrimary.copy(alpha = 0.1f)
                else
                    Color.Transparent,
                RoundedCornerShape(com.astro.storm.ui.theme.NeoVedicTokens.ElementCornerRadius)
            )
            .padding(horizontal = 8.dp, vertical = 6.dp)
            .clickable { onExpandChange() },
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1f)) {
            Text(
                antardasha.planet.getLocalizedName(language),
                color = if (antardasha.isActive) AppTheme.AccentPrimary else AppTheme.TextSecondary,
                fontWeight = if (antardasha.isActive) FontWeight.Medium else FontWeight.Normal,
                fontSize = 13.sp
            )
            if (antardasha.isActive) {
                Spacer(modifier = Modifier.width(6.dp))
                Box(
                    modifier = Modifier
                        .size(6.dp)
                        .background(AppTheme.SuccessColor, CircleShape)
                )
            }
        }
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                "${antardasha.startDate.format(dateFormatter)} - ${antardasha.endDate.format(dateFormatter)}",
                color = AppTheme.TextMuted,
                fontSize = 11.sp
            )
            if (antardasha.pratyantardashas.isNotEmpty()) {
                Spacer(modifier = Modifier.width(8.dp))
                Icon(
                    if (isExpanded) Icons.Filled.ExpandLess else Icons.Filled.ExpandMore,
                    contentDescription = null,
                    tint = AppTheme.TextMuted,
                    modifier = Modifier.size(16.dp)
                )
            }
        }
    }

    // Expanded Pratyantardashas
    AnimatedVisibility(
        visible = isExpanded && antardasha.pratyantardashas.isNotEmpty(),
        enter = expandVertically() + fadeIn(),
        exit = shrinkVertically() + fadeOut()
    ) {
        Column(modifier = Modifier.padding(start = 16.dp, top = 4.dp, bottom = 4.dp)) {
            antardasha.pratyantardashas.take(10).forEach { pratyantardasha ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 2.dp)
                        .background(
                            if (pratyantardasha.isActive)
                                AppTheme.AccentSecondary.copy(alpha = 0.1f)
                            else
                                Color.Transparent,
                            RoundedCornerShape(com.astro.storm.ui.theme.NeoVedicTokens.ElementCornerRadius)
                        )
                        .padding(horizontal = 6.dp, vertical = 4.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        pratyantardasha.planet.getLocalizedName(language),
                        color = if (pratyantardasha.isActive) AppTheme.AccentSecondary else AppTheme.TextMuted,
                        fontSize = 11.sp,
                        modifier = Modifier.weight(1f)
                    )
                    Text(
                        "${pratyantardasha.startDate.format(dateFormatter)}",
                        color = AppTheme.TextMuted,
                        fontSize = 10.sp
                    )
                }
            }
            if (antardasha.pratyantardashas.size > 10) {
                Text(
                    "... and ${antardasha.pratyantardashas.size - 10} more",
                    color = AppTheme.TextMuted,
                    fontSize = 10.sp,
                    modifier = Modifier.padding(vertical = 4.dp)
                )
            }
        }
    }
}

@Composable
private fun InterpretationContent(
    result: DashaCalculator.DashaTimeline,
    language: Language
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(com.astro.storm.ui.theme.NeoVedicTokens.ScreenPadding),
        verticalArrangement = Arrangement.spacedBy(com.astro.storm.ui.theme.NeoVedicTokens.SpaceMD)
    ) {
        // Mahadasha Effects
        result.currentMahadasha?.let { md ->
            item {
                VimsottariEffectsCard(
                    title = "${md.planet.getLocalizedName(language)} ${stringResource(StringKey.DASHA_MAHADASHA)} Effects",
                    effects = getMahadashaInterpretation(md.planet, language),
                    icon = Icons.Outlined.Info,
                    language = language
                )
            }
        }

        // Antardasha Effects
        result.currentAntardasha?.let { ad ->
            item {
                VimsottariEffectsCard(
                    title = "${ad.planet.getLocalizedName(language)} ${stringResource(StringKey.DASHA_ANTARDASHA)} Effects",
                    effects = getAntardashaInterpretation(ad.planet, language),
                    icon = Icons.Outlined.CalendarToday,
                    language = language
                )
            }
        }

        // Pratyantardasha Effects
        result.currentPratyantardasha?.let { pd ->
            item {
                VimsottariEffectsCard(
                    title = "${pd.planet.getLocalizedName(language)} ${stringResource(StringKey.DASHA_PRATYANTARDASHA)} Effects",
                    effects = getAntardashaInterpretation(pd.planet, language),
                    icon = Icons.Outlined.CalendarToday,
                    language = language
                )
            }
        }

        // General Guidance
        item {
            VimsottariGuidanceCard(result, language)
        }
    }
}

@Composable
private fun VimsottariEffectsCard(
    title: String,
    effects: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    language: Language
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = AppTheme.CardBackground),
        shape = RoundedCornerShape(com.astro.storm.ui.theme.NeoVedicTokens.ElementCornerRadius)
    ) {
        Column(modifier = Modifier.padding(com.astro.storm.ui.theme.NeoVedicTokens.ScreenPadding)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    icon,
                    contentDescription = null,
                    tint = AppTheme.AccentPrimary,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    title,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 16.sp,
                    color = AppTheme.TextPrimary
                )
            }
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                effects,
                color = AppTheme.TextSecondary,
                fontSize = 14.sp,
                lineHeight = 22.sp
            )
        }
    }
}

@Composable
private fun VimsottariGuidanceCard(
    result: DashaCalculator.DashaTimeline,
    language: Language
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = AppTheme.SuccessColor.copy(alpha = 0.1f)
        ),
        shape = RoundedCornerShape(com.astro.storm.ui.theme.NeoVedicTokens.ElementCornerRadius)
    ) {
        Column(modifier = Modifier.padding(com.astro.storm.ui.theme.NeoVedicTokens.ScreenPadding)) {
            Text(
                "Recommendations",
                fontWeight = FontWeight.SemiBold,
                fontSize = 16.sp,
                color = AppTheme.TextPrimary
            )
            Spacer(modifier = Modifier.height(12.dp))
            
            val recommendations = listOf(
                "Focus on strengthening the ruling planet through appropriate mantras and gemstones.",
                "Practice meditation and spiritual activities to enhance the positive effects.",
                "Be patient during challenging periods and maintain discipline.",
                "Perform remedies for malefic planets to reduce their negative effects.",
                "Use this period for personal growth and karmic purification."
            )
            
            recommendations.forEachIndexed { index, rec ->
                Row(
                    modifier = Modifier.padding(vertical = 4.dp)
                ) {
                    Text(
                        "${index + 1}.",
                        color = AppTheme.SuccessColor,
                        fontWeight = FontWeight.Medium,
                        modifier = Modifier.padding(end = 8.dp)
                    )
                    Text(
                        rec,
                        color = AppTheme.TextSecondary,
                        fontSize = 14.sp,
                        lineHeight = 20.sp
                    )
                }
            }
        }
    }
}

@Composable
private fun VimsottariInfoDialog(onDismiss: () -> Unit) {
    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = AppTheme.CardBackground,
        title = {
            Text(
                "About Vimsottari Dasha",
                fontWeight = FontWeight.Bold,
                color = AppTheme.TextPrimary,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
        },
        text = {
            Text(
                "Vimsottari Dasha is the most important and widely used Nakshatra-based planetary period system in Vedic astrology. It spans 120 years and uses 9 planetary lords. The starting point is determined by the Moon's birth Nakshatra position. Each Mahadasha is divided into Antardashas and Pratyantardashas for precise timing of events.",
                color = AppTheme.TextSecondary,
                lineHeight = 22.sp
            )
        },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text(stringResource(StringKey.BTN_CLOSE), color = AppTheme.AccentPrimary)
            }
        }
    )
}

private fun getMahadashaInterpretation(planet: Planet, language: Language): String {
    val key = when (planet) {
        Planet.SUN -> StringKeyDashaInterpretations.MAHADASHA_SUN_INTERP
        Planet.MOON -> StringKeyDashaInterpretations.MAHADASHA_MOON_INTERP
        Planet.MARS -> StringKeyDashaInterpretations.MAHADASHA_MARS_INTERP
        Planet.MERCURY -> StringKeyDashaInterpretations.MAHADASHA_MERCURY_INTERP
        Planet.JUPITER -> StringKeyDashaInterpretations.MAHADASHA_JUPITER_INTERP
        Planet.VENUS -> StringKeyDashaInterpretations.MAHADASHA_VENUS_INTERP
        Planet.SATURN -> StringKeyDashaInterpretations.MAHADASHA_SATURN_INTERP
        Planet.RAHU -> StringKeyDashaInterpretations.MAHADASHA_RAHU_INTERP
        Planet.KETU -> StringKeyDashaInterpretations.MAHADASHA_KETU_INTERP
        else -> StringKeyDashaInterpretations.MAHADASHA_DEFAULT_INTERP
    }
    return StringResources.get(key, language)
}

private fun getAntardashaInterpretation(planet: Planet, language: Language): String {
    val key = when (planet) {
        Planet.SUN -> StringKeyDashaInterpretations.ANTARDASHA_SUN_INTERP
        Planet.MOON -> StringKeyDashaInterpretations.ANTARDASHA_MOON_INTERP
        Planet.MARS -> StringKeyDashaInterpretations.ANTARDASHA_MARS_INTERP
        Planet.MERCURY -> StringKeyDashaInterpretations.ANTARDASHA_MERCURY_INTERP
        Planet.JUPITER -> StringKeyDashaInterpretations.ANTARDASHA_JUPITER_INTERP
        Planet.VENUS -> StringKeyDashaInterpretations.ANTARDASHA_VENUS_INTERP
        Planet.SATURN -> StringKeyDashaInterpretations.ANTARDASHA_SATURN_INTERP
        Planet.RAHU -> StringKeyDashaInterpretations.ANTARDASHA_RAHU_INTERP
        Planet.KETU -> StringKeyDashaInterpretations.ANTARDASHA_KETU_INTERP
        else -> StringKeyDashaInterpretations.ANTARDASHA_DEFAULT_INTERP
    }
    return StringResources.get(key, language)
}

private fun getPlanetColor(planet: Planet): Color {
    return when (planet) {
        Planet.SUN -> com.astro.storm.ui.theme.PlanetSun
        Planet.MOON -> com.astro.storm.ui.theme.PlanetMercury
        Planet.MARS -> com.astro.storm.ui.theme.MarsRed
        Planet.MERCURY -> com.astro.storm.ui.theme.PlanetMercury
        Planet.JUPITER -> com.astro.storm.ui.theme.VedicGold
        Planet.VENUS -> com.astro.storm.ui.theme.PlanetVenus
        Planet.SATURN -> com.astro.storm.ui.theme.SlateDark
        Planet.RAHU -> com.astro.storm.ui.theme.PlanetRahu
        Planet.KETU -> com.astro.storm.ui.theme.PlanetVenus
        else -> AppTheme.AccentPrimary
    }
}

private fun getPlanetAbbreviation(planet: Planet): String {
    return when (planet) {
        Planet.SUN -> "Su"
        Planet.MOON -> "Mo"
        Planet.MARS -> "Ma"
        Planet.MERCURY -> "Me"
        Planet.JUPITER -> "Ju"
        Planet.VENUS -> "Ve"
        Planet.SATURN -> "Sa"
        Planet.RAHU -> "Ra"
        Planet.KETU -> "Ke"
        else -> "?"
    }
}
