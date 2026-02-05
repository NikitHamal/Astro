package com.astro.storm.ui.screen

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.*
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
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.astro.storm.core.common.Language
import com.astro.storm.data.localization.LocalLanguage
import com.astro.storm.core.common.StringKey
import com.astro.storm.core.common.StringKeyAnalysis
import com.astro.storm.core.common.StringKeyAdvanced
import com.astro.storm.core.common.StringKeyUIExtra
import com.astro.storm.core.common.getLocalizedName
import com.astro.storm.data.localization.stringResource
import com.astro.storm.core.model.Planet
import com.astro.storm.core.model.VedicChart
import com.astro.storm.ephemeris.KakshaTransitCalculator
import com.astro.storm.ephemeris.KakshaTransitCalculator.KakshaQuality
import com.astro.storm.ui.theme.AppTheme
import com.astro.storm.ui.viewmodel.KakshaTab
import com.astro.storm.ui.viewmodel.KakshaTransitUiState
import com.astro.storm.ui.viewmodel.KakshaTransitViewModel
import java.time.format.DateTimeFormatter

/**
 * Kakshya Transit Analysis Screen
 *
 * Displays micro-transit analysis where each sign is divided into 8 parts (3°45' each).
 * This system provides high-precision timing for planetary results based on Ashtakavarga.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun KakshaTransitScreen(
    chart: VedicChart?,
    onBack: () -> Unit,
    viewModel: KakshaTransitViewModel = hiltViewModel()
) {
    val language = LocalLanguage.current
    val uiState by viewModel.uiState.collectAsState()
    val selectedTab by viewModel.selectedTab.collectAsState()
    val colors = AppTheme.current

    LaunchedEffect(chart) {
        chart?.let {
            viewModel.calculateKakshaTransits(it, language)
        }
    }

    Scaffold(
        containerColor = colors.ScreenBackground,
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(
                            text = stringResource(StringKeyAdvanced.KAKSHYA_TITLE),
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = colors.TextPrimary
                        )
                        Text(
                            text = stringResource(StringKeyAdvanced.KAKSHYA_SUBTITLE),
                            style = MaterialTheme.typography.bodySmall,
                            color = colors.TextMuted
                        )
                    }
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = colors.TextPrimary
                        )
                    }
                },
                actions = {
                    IconButton(onClick = { chart?.let { viewModel.calculateKakshaTransits(it, language, true) } }) {
                        Icon(
                            imageVector = Icons.Default.Refresh,
                            contentDescription = "Refresh",
                            tint = colors.TextPrimary
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = colors.ScreenBackground
                )
            )
        }
    ) { paddingValues ->
        when (val state = uiState) {
            is KakshaTransitUiState.Loading -> KakshaLoadingContent(modifier = Modifier.padding(paddingValues))
            is KakshaTransitUiState.Error -> KakshaErrorContent(
                message = state.message,
                modifier = Modifier.padding(paddingValues),
                onRetry = { chart?.let { viewModel.calculateKakshaTransits(it, language, true) } }
            )
            is KakshaTransitUiState.Success -> {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                ) {
                    KakshaTabSelector(
                        selectedTab = selectedTab,
                        onTabSelected = { viewModel.selectTab(it) },
                        language = language
                    )

                    Box(modifier = Modifier.weight(1f)) {
                        when (selectedTab) {
                            KakshaTab.CURRENT -> CurrentKakshaTab(state.result, viewModel, language)
                            KakshaTab.PLANETS -> PlanetsKakshaTab(state.result, viewModel, language)
                            KakshaTab.TIMELINE -> TimelineKakshaTab(state.result, language)
                            KakshaTab.FAVORABLE -> FavorableKakshaTab(state.result, language)
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun KakshaTabSelector(
    selectedTab: KakshaTab,
    onTabSelected: (KakshaTab) -> Unit,
    language: Language
) {
    val colors = AppTheme.current
    
    ScrollableTabRow(
        selectedTabIndex = selectedTab.ordinal,
        containerColor = colors.ScreenBackground,
        contentColor = colors.AccentPrimary,
        edgePadding = 16.dp,
        divider = {},
        indicator = { tabPositions ->
            TabRowDefaults.SecondaryIndicator(
                Modifier.tabIndicatorOffset(tabPositions[selectedTab.ordinal]),
                color = colors.AccentPrimary,
                height = 3.dp
            )
        }
    ) {
        KakshaTab.entries.forEach { tab ->
            Tab(
                selected = selectedTab == tab,
                onClick = { onTabSelected(tab) },
                text = {
                    Text(
                        text = getTabTitle(tab, language),
                        style = MaterialTheme.typography.labelLarge,
                        fontWeight = if (selectedTab == tab) FontWeight.Bold else FontWeight.Medium
                    )
                },
                selectedContentColor = colors.AccentPrimary,
                unselectedContentColor = colors.TextMuted
            )
        }
    }
}

private fun getTabTitle(tab: KakshaTab, language: Language): String {
    val key = when (tab) {
        KakshaTab.CURRENT -> StringKeyAdvanced.KAKSHYA_TAB_CURRENT
        KakshaTab.PLANETS -> StringKeyAdvanced.KAKSHYA_TAB_PLANETS
        KakshaTab.TIMELINE -> StringKeyAdvanced.KAKSHYA_TAB_TIMELINE
        KakshaTab.FAVORABLE -> StringKeyAdvanced.KAKSHYA_TAB_FAVORABLE
    }
    return com.astro.storm.core.common.StringResources.get(key, language)
}

@Composable
private fun CurrentKakshaTab(
    result: KakshaTransitCalculator.KakshaTransitResult,
    viewModel: KakshaTransitViewModel,
    language: Language
) {
    val summary = viewModel.getSummary()
    val colors = AppTheme.current

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            KakshaSummaryCard(summary, language)
        }

        item {
            Text(
                text = stringResource(StringKeyAdvanced.KAKSHYA_PLANET_POSITIONS),
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = colors.TextPrimary
            )
        }

        items(result.currentPositions) { position ->
            KakshaPlanetCard(position, language)
        }
    }
}

@Composable
private fun KakshaSummaryCard(summary: com.astro.storm.ui.viewmodel.KakshaSummary?, language: Language) {
    if (summary == null) return
    val colors = AppTheme.current
    val qualityColor = getQualityColor(summary.overallQuality)

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = colors.CardBackground)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = stringResource(StringKeyAdvanced.KAKSHYA_OVERALL_QUALITY),
                        style = MaterialTheme.typography.labelMedium,
                        color = colors.TextMuted
                    )
                    Text(
                        text = summary.overallQuality.getLocalizedName(language),
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = qualityColor
                    )
                }
                
                Box(contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(
                        progress = { (summary.overallScore / 100).toFloat() },
                        modifier = Modifier.size(60.dp),
                        color = qualityColor,
                        trackColor = qualityColor.copy(alpha = 0.1f),
                        strokeWidth = 6.dp,
                        strokeCap = StrokeCap.Round
                    )
                    Text(
                        text = "${summary.overallScore.toInt()}",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = colors.TextPrimary
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            HorizontalDivider(color = colors.DividerColor)
            Spacer(modifier = Modifier.height(16.dp))
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                SummaryStat(
                    label = stringResource(StringKeyAdvanced.KAKSHYA_QUALITY_EXCELLENT),
                    value = summary.excellentCount.toString(),
                    color = colors.SuccessColor
                )
                SummaryStat(
                    label = stringResource(StringKeyAdvanced.KAKSHYA_QUALITY_GOOD),
                    value = summary.goodCount.toString(),
                    color = colors.AccentTeal
                )
                SummaryStat(
                    label = stringResource(StringKeyAdvanced.KAKSHYA_QUALITY_MODERATE),
                    value = summary.moderateCount.toString(),
                    color = colors.AccentGold
                )
                SummaryStat(
                    label = stringResource(StringKeyAdvanced.KAKSHYA_QUALITY_POOR),
                    value = summary.poorCount.toString(),
                    color = colors.ErrorColor
                )
            }
        }
    }
}

@Composable
private fun SummaryStat(label: String, value: String, color: Color) {
    val colors = AppTheme.current
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = value,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = color
        )
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall,
            color = colors.TextMuted
        )
    }
}

@Composable
private fun KakshaPlanetCard(position: KakshaTransitCalculator.KakshaPlanetPosition, language: Language) {
    val colors = AppTheme.current
    val qualityColor = getQualityColor(position.quality)

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = colors.CardBackground)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Surface(
                        modifier = Modifier.size(40.dp),
                        shape = CircleShape,
                        color = colors.AccentPrimary.copy(alpha = 0.1f)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Text(
                                text = position.planet.symbol,
                                style = MaterialTheme.typography.titleSmall,
                                fontWeight = FontWeight.Bold,
                                color = colors.AccentPrimary
                            )
                        }
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(
                            text = position.planet.getLocalizedName(language),
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = colors.TextPrimary
                        )
                        Text(
                            text = position.sign.displayName + stringResource(StringKeyUIExtra.PAREN_START) + position.degreeInSign.toInt() + stringResource(StringKeyUIExtra.DEGREE) + ((position.degreeInSign % 1) * 60).toInt() + stringResource(StringKeyUIExtra.ARC_MINUTE) + stringResource(StringKeyUIExtra.PAREN_END),
                            style = MaterialTheme.typography.bodySmall,
                            color = colors.TextMuted
                        )
                    }
                }
                
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = qualityColor.copy(alpha = 0.1f)
                ) {
                    Text(
                        text = position.quality.getLocalizedName(language),
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = FontWeight.Bold,
                        color = qualityColor,
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(12.dp))
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = stringResource(StringKeyAdvanced.KAKSHYA_LORD),
                        style = MaterialTheme.typography.labelSmall,
                        color = colors.TextMuted
                    )
                    Text(
                        text = position.kakshaLord,
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Medium,
                        color = colors.TextPrimary
                    )
                }
                
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "BAV",
                        style = MaterialTheme.typography.labelSmall,
                        color = colors.TextMuted
                    )
                    Text(
                        text = "${position.bavScore}/8",
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Bold,
                        color = if (position.hasBinbu) colors.SuccessColor else colors.TextPrimary
                    )
                }

                Column(horizontalAlignment = Alignment.End) {
                    Text(
                        text = stringResource(StringKeyAdvanced.KAKSHYA_NEXT),
                        style = MaterialTheme.typography.labelSmall,
                        color = colors.TextMuted
                    )
                    Text(
                        text = "${position.timeToNextKaksha}h",
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Medium,
                        color = colors.AccentPrimary
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(12.dp))
            
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(8.dp),
                color = colors.ScreenBackground.copy(alpha = 0.5f)
            ) {
                Text(
                    text = if (language == Language.NEPALI) position.interpretationNe else position.interpretation,
                    style = MaterialTheme.typography.bodySmall,
                    color = colors.TextSecondary,
                    modifier = Modifier.padding(12.dp)
                )
            }
        }
    }
}

@Composable
private fun PlanetsKakshaTab(
    result: KakshaTransitCalculator.KakshaTransitResult,
    viewModel: KakshaTransitViewModel,
    language: Language
) {
    val selectedPlanet by viewModel.selectedPlanet.collectAsState()
    val colors = AppTheme.current

    Column(modifier = Modifier.fillMaxSize()) {
        LazyRow(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(result.currentPositions) { pos ->
                FilterChip(
                    selected = selectedPlanet == pos.planet,
                    onClick = { viewModel.selectPlanet(if (selectedPlanet == pos.planet) null else pos.planet) },
                    label = { Text(pos.planet.getLocalizedName(language)) },
                    leadingIcon = if (selectedPlanet == pos.planet) {
                        { Icon(Icons.Default.Check, null, modifier = Modifier.size(16.dp)) }
                    } else null,
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = colors.AccentPrimary,
                        selectedLabelColor = Color.White
                    )
                )
            }
        }

        if (selectedPlanet != null) {
            val planetPos = viewModel.getPositionForPlanet(selectedPlanet!!)
            val changes = viewModel.getChangesForPlanet(selectedPlanet!!)
            val favorable = viewModel.getFavorablePeriodsForPlanet(selectedPlanet!!)

            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                if (planetPos != null) {
                    item { KakshaPlanetCard(planetPos, language) }
                }

                if (favorable.isNotEmpty()) {
                    item {
                        Text(
                            text = stringResource(StringKeyAdvanced.KAKSHYA_UPCOMING_FAVORABLE),
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.Bold,
                            color = colors.TextPrimary
                        )
                    }
                    items(favorable) { period ->
                        FavorablePeriodItem(period, language)
                    }
                }

                if (changes.isNotEmpty()) {
                    item {
                        Text(
                            text = stringResource(StringKeyAdvanced.KAKSHYA_UPCOMING_CHANGES),
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.Bold,
                            color = colors.TextPrimary
                        )
                    }
                    items(changes) { change ->
                        KakshaChangeItem(change, language)
                    }
                }
            }
        } else {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(
                        imageVector = Icons.Outlined.Public,
                        contentDescription = null,
                        modifier = Modifier.size(64.dp),
                        tint = colors.TextMuted
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = stringResource(StringKeyAdvanced.KAKSHYA_SELECT_PLANET),
                        style = MaterialTheme.typography.bodyMedium,
                        color = colors.TextMuted
                    )
                }
            }
        }
    }
}

@Composable
private fun FavorablePeriodItem(period: KakshaTransitCalculator.FavorableKakshaPeriod, language: Language) {
    val colors = AppTheme.current
    val dateFormatter = DateTimeFormatter.ofPattern("MMM d, HH:mm")

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = colors.SuccessColor.copy(alpha = 0.05f))
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Star,
                        contentDescription = null,
                        tint = colors.AccentGold,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = period.kakshaLord,
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Bold,
                        color = colors.TextPrimary
                    )
                }
                Surface(
                    shape = RoundedCornerShape(4.dp),
                    color = colors.SuccessColor.copy(alpha = 0.1f)
                ) {
                    Text(
                        text = "BAV: ${period.bavScore}",
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.Bold,
                        color = colors.SuccessColor,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(8.dp))
            
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.Schedule,
                    contentDescription = null,
                    tint = colors.TextMuted,
                    modifier = Modifier.size(14.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = "${period.startTime.format(dateFormatter)} - ${period.endTime.format(dateFormatter)}",
                    style = MaterialTheme.typography.bodySmall,
                    color = colors.TextMuted
                )
            }
            
            Spacer(modifier = Modifier.height(8.dp))
            
            Text(
                text = if (language == Language.NEPALI) period.descriptionNe else period.description,
                style = MaterialTheme.typography.bodySmall,
                color = colors.TextSecondary
            )
        }
    }
}

@Composable
private fun KakshaChangeItem(change: KakshaTransitCalculator.KakshaChange, language: Language) {
    val colors = AppTheme.current
    val dateFormatter = DateTimeFormatter.ofPattern("MMM d, HH:mm")

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = colors.CardBackground)
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "Kakshya ${change.currentKaksha}",
                        style = MaterialTheme.typography.bodySmall,
                        color = colors.TextMuted
                    )
                    Icon(
                        imageVector = Icons.Default.ArrowForward,
                        contentDescription = null,
                        modifier = Modifier.size(14.dp).padding(horizontal = 4.dp),
                        tint = colors.TextMuted
                    )
                    Text(
                        text = change.nextKakshaLord,
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Bold,
                        color = colors.AccentPrimary
                    )
                }
                Text(
                    text = change.expectedTime.format(dateFormatter),
                    style = MaterialTheme.typography.bodySmall,
                    color = colors.TextMuted
                )
            }
            
            Column(horizontalAlignment = Alignment.End) {
                Surface(
                    shape = RoundedCornerShape(4.dp),
                    color = if (change.willHaveBindu) colors.SuccessColor.copy(alpha = 0.1f) else colors.ErrorColor.copy(alpha = 0.1f)
                ) {
                    Text(
                        text = if (change.willHaveBindu) "Bindu +" else "Bindu -",
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.Bold,
                        color = if (change.willHaveBindu) colors.SuccessColor else colors.ErrorColor,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                    )
                }
                Text(
                    text = "in ${change.hoursFromNow}h",
                    style = MaterialTheme.typography.labelSmall,
                    color = colors.TextMuted
                )
            }
        }
    }
}

@Composable
private fun TimelineKakshaTab(
    result: KakshaTransitCalculator.KakshaTransitResult,
    language: Language
) {
    val colors = AppTheme.current
    
    if (result.upcomingChanges.isEmpty()) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text(
                text = stringResource(StringKeyAdvanced.KAKSHYA_NO_CHANGES),
                style = MaterialTheme.typography.bodyMedium,
                color = colors.TextMuted
            )
        }
        return
    }

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(result.upcomingChanges) { change ->
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = colors.CardBackground)
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Surface(
                        modifier = Modifier.size(40.dp),
                        shape = CircleShape,
                        color = colors.AccentPrimary.copy(alpha = 0.1f)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Text(
                                text = change.planet.symbol,
                                style = MaterialTheme.typography.titleSmall,
                                fontWeight = FontWeight.Bold,
                                color = colors.AccentPrimary
                            )
                        }
                    }
                    Spacer(modifier = Modifier.width(16.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = if (language == Language.NEPALI) change.impactDescriptionNe else change.impactDescription,
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.Medium,
                            color = colors.TextPrimary
                        )
                        Text(
                            text = change.expectedTime.format(DateTimeFormatter.ofPattern("MMM d, HH:mm")),
                            style = MaterialTheme.typography.bodySmall,
                            color = colors.TextMuted
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun FavorableKakshaTab(
    result: KakshaTransitCalculator.KakshaTransitResult,
    language: Language
) {
    val colors = AppTheme.current

    if (result.favorablePeriods.isEmpty()) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text(
                text = stringResource(StringKeyAdvanced.KAKSHYA_NO_FAVORABLE),
                style = MaterialTheme.typography.bodyMedium,
                color = colors.TextMuted
            )
        }
        return
    }

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        items(result.favorablePeriods) { period ->
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = colors.CardBackground)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Surface(
                            modifier = Modifier.size(32.dp),
                            shape = CircleShape,
                            color = colors.AccentGold.copy(alpha = 0.1f)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Icon(Icons.Default.Star, null, tint = colors.AccentGold, modifier = Modifier.size(16.dp))
                            }
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Text(
                            text = "${period.planet.getLocalizedName(language)} in ${period.kakshaLord} Kakshya",
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.Bold,
                            color = colors.TextPrimary
                        )
                    }
                    
                    Spacer(modifier = Modifier.height(12.dp))
                    
                    Text(
                        text = if (language == Language.NEPALI) period.descriptionNe else period.description,
                        style = MaterialTheme.typography.bodyMedium,
                        color = colors.TextSecondary
                    )
                    
                    Spacer(modifier = Modifier.height(12.dp))
                    
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Schedule, null, tint = colors.TextMuted, modifier = Modifier.size(14.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "${period.startTime.format(DateTimeFormatter.ofPattern("MMM d"))} - ${period.endTime.format(DateTimeFormatter.ofPattern("MMM d"))} (${period.duration/24} days)",
                            style = MaterialTheme.typography.bodySmall,
                            color = colors.TextMuted
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun KakshaLoadingContent(modifier: Modifier = Modifier) {
    Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        CircularProgressIndicator(color = AppTheme.current.AccentPrimary)
    }
}

@Composable
private fun KakshaErrorContent(message: String, modifier: Modifier = Modifier, onRetry: () -> Unit) {
    val colors = AppTheme.current
    Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Icon(Icons.Default.Error, null, tint = colors.ErrorColor, modifier = Modifier.size(48.dp))
            Spacer(modifier = Modifier.height(16.dp))
            Text(text = message, color = colors.TextPrimary, textAlign = TextAlign.Center)
            Spacer(modifier = Modifier.height(16.dp))
            Button(onClick = onRetry) {
                Text(text = stringResource(StringKey.BTN_RETRY))
            }
        }
    }
}

private fun getQualityColor(quality: KakshaQuality): Color {
    return when (quality) {
        KakshaQuality.EXCELLENT -> Color(0xFF4CAF50)
        KakshaQuality.GOOD -> Color(0xFF2196F3)
        KakshaQuality.MODERATE -> Color(0xFFFFA000)
        KakshaQuality.POOR -> Color(0xFFF44336)
    }
}

