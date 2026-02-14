package com.astro.storm.ui.screen

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import com.astro.storm.ui.components.common.NeoVedicPageHeader
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.astro.storm.core.common.Language
import com.astro.storm.core.common.StringKey
import com.astro.storm.core.common.StringKeyAnalysis
import com.astro.storm.core.common.StringKeyDosha
import com.astro.storm.core.common.StringKeyMatch
import com.astro.storm.core.common.StringKeyRemedy
import com.astro.storm.core.common.StringKeyUICommon
import com.astro.storm.core.common.StringResources
import com.astro.storm.core.common.getLocalizedName
import com.astro.storm.core.model.Planet
import com.astro.storm.core.model.VedicChart
import com.astro.storm.data.localization.currentLanguage
import com.astro.storm.data.localization.stringResource
import com.astro.storm.data.localization.LocalLanguage
import com.astro.storm.ephemeris.GrahaYuddhaCalculator
import com.astro.storm.ephemeris.GrahaYuddhaCalculator.GrahaYuddhaAnalysis
import com.astro.storm.ephemeris.GrahaYuddhaCalculator.GrahaYuddhaResult
import com.astro.storm.ephemeris.GrahaYuddhaCalculator.DashaWarEffect
import com.astro.storm.ephemeris.GrahaYuddhaCalculator.WarRemedy
import com.astro.storm.ephemeris.GrahaYuddhaCalculator.WarIntensity
import com.astro.storm.ephemeris.GrahaYuddhaCalculator.WarImpactLevel
import com.astro.storm.ephemeris.GrahaYuddhaCalculator.WarStatus
import com.astro.storm.ephemeris.GrahaYuddhaCalculator.RemedyType
import com.astro.storm.ui.components.common.ModernPillTabRow
import com.astro.storm.ui.components.common.TabItem
import com.astro.storm.ui.theme.AppTheme
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext

/**
 * Graha Yuddha (Planetary War) Analysis Screen
 *
 * Comprehensive analysis of planetary wars showing:
 * - Active wars with winner/loser determination
 * - War intensity and effects
 * - Dasha period implications
 * - Remedial measures
 *
 * References: BPHS, Surya Siddhanta, Phaladeepika
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GrahaYuddhaScreen(
    chart: com.astro.storm.core.model.VedicChart?,
    onBack: () -> Unit
) {
    if (chart == null) {
        EmptyChartScreen(
            title = com.astro.storm.data.localization.stringResource(com.astro.storm.core.common.StringKeyDosha.GRAHA_SCREEN_TITLE),
            message = com.astro.storm.data.localization.stringResource(com.astro.storm.core.common.StringKey.NO_PROFILE_MESSAGE),
            onBack = onBack
        )
        return
    }

    val language = com.astro.storm.data.localization.currentLanguage()
    var showInfoDialog by remember { mutableStateOf(false) }
    var selectedTab by remember { mutableIntStateOf(0) }
    var isCalculating by remember { mutableStateOf(true) }
    var yuddhaAnalysis by remember { mutableStateOf<com.astro.storm.ephemeris.GrahaYuddhaCalculator.GrahaYuddhaAnalysis?>(null) }

    val overviewTabTitle = com.astro.storm.data.localization.stringResource(com.astro.storm.core.common.StringKeyDosha.UI_OVERVIEW)
    val activeWarsTabTitle = com.astro.storm.data.localization.stringResource(com.astro.storm.core.common.StringKeyDosha.GRAHA_ACTIVE_WARS)
    val dashaEffectsTabTitle = com.astro.storm.data.localization.stringResource(com.astro.storm.core.common.StringKeyDosha.GRAHA_DASHA_EFFECTS)
    val remediesTabTitle = com.astro.storm.data.localization.stringResource(com.astro.storm.core.common.StringKeyDosha.UI_REMEDIES)

    val tabs = listOf(
        overviewTabTitle,
        activeWarsTabTitle,
        dashaEffectsTabTitle,
        remediesTabTitle
    )

    // Calculate Graha Yuddha
    LaunchedEffect(chart) {
        isCalculating = true
        delay(300)
        try {
            yuddhaAnalysis = withContext(Dispatchers.Default) {
                GrahaYuddhaCalculator.analyzeGrahaYuddha(chart)
            }
        } catch (e: Exception) {
            // Handle error silently
        }
        isCalculating = false
    }

    if (showInfoDialog) {
        GrahaYuddhaInfoDialog(onDismiss = { showInfoDialog = false })
    }

    Scaffold(
        containerColor = AppTheme.ScreenBackground,
        topBar = {
            NeoVedicPageHeader(
                title = stringResource(StringKeyDosha.GRAHA_SCREEN_TITLE),
                subtitle = chart.birthData.name,
                onBack = onBack,
                actions = {
                    IconButton(onClick = { showInfoDialog = true }) {
                        Icon(
                            Icons.Outlined.Info,
                            contentDescription = stringResource(StringKeyDosha.GRAHA_ABOUT_TITLE),
                            tint = AppTheme.TextPrimary
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        if (isCalculating) {
            LoadingContent(paddingValues)
        } else if (yuddhaAnalysis != null) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            ) {
                // Tab row
                val accentColor = com.astro.storm.ui.theme.AppTheme.AccentPrimary
                val tabItems = remember(tabs, accentColor) {
                    tabs.map { tabTitle -> com.astro.storm.ui.components.common.TabItem(title = tabTitle, accentColor = accentColor) }
                }
                
                com.astro.storm.ui.components.common.ModernPillTabRow(
                    tabs = tabItems,
                    selectedIndex = selectedTab,
                    onTabSelected = { selectedTab = it },
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                )

                // Content based on selected tab
                when (selectedTab) {
                    0 -> OverviewTab(yuddhaAnalysis!!)
                    1 -> ActiveWarsTab(yuddhaAnalysis!!)
                    2 -> DashaEffectsTab(yuddhaAnalysis!!)
                    3 -> RemediesTab(yuddhaAnalysis!!)
                }
            }
        }
    }
}

@Composable
private fun LoadingContent(paddingValues: PaddingValues) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            CircularProgressIndicator(
                color = AppTheme.AccentPrimary,
                modifier = Modifier.size(48.dp)
            )
            Text(
                text = stringResource(StringKeyDosha.GRAHA_ANALYZING),
                style = MaterialTheme.typography.bodyMedium,
                color = AppTheme.TextMuted
            )
        }
    }
}

// ============================================
// OVERVIEW TAB
// ============================================

@Composable
private fun OverviewTab(analysis: GrahaYuddhaAnalysis) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // War Status Card
        item {
            WarStatusCard(analysis)
        }

        // Summary Card
        item {
            SummaryCard(analysis.interpretation.summary)
        }

        // Impact Assessment
        item {
            ImpactAssessmentCard(analysis)
        }

        // Key Insights
        if (analysis.interpretation.keyInsights.isNotEmpty()) {
            item {
                KeyInsightsCard(analysis.interpretation.keyInsights)
            }
        }

        // Affected Life Areas
        if (analysis.interpretation.affectedLifeAreas.isNotEmpty()) {
            item {
                AffectedAreasCard(analysis.interpretation.affectedLifeAreas)
            }
        }

        // Recommendations
        item {
            RecommendationsCard(analysis.interpretation.recommendations)
        }
    }
}

@Composable
private fun WarStatusCard(analysis: GrahaYuddhaAnalysis) {
    val hasWar = analysis.hasActiveWar
    val warCount = analysis.activeWars.size

    val (statusColor, statusIcon, statusText) = if (hasWar) {
        Triple(
            AppTheme.WarningColor,
            Icons.Outlined.Warning,
            stringResource(StringKeyDosha.GRAHA_WARS_DETECTED_FMT, warCount)
        )
    } else {
        Triple(
            AppTheme.SuccessColor,
            Icons.Outlined.CheckCircle,
            stringResource(StringKeyDosha.GRAHA_NO_WARS)
        )
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = AppTheme.CardBackground),
        shape = RoundedCornerShape(com.astro.storm.ui.theme.NeoVedicTokens.ElementCornerRadius)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(56.dp)
                    .clip(CircleShape)
                    .background(statusColor.copy(alpha = 0.15f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = statusIcon,
                    contentDescription = null,
                    tint = statusColor,
                    modifier = Modifier.size(28.dp)
                )
            }

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = statusText,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = AppTheme.TextPrimary
                )
                Text(
                    text = if (hasWar)
                        stringResource(com.astro.storm.core.common.StringKeyDosha.GRAHA_WAR_AFFECTS_DESC)
                    else
                        stringResource(com.astro.storm.core.common.StringKeyDosha.GRAHA_NO_WARS_DESC),
                    style = MaterialTheme.typography.bodySmall,
                    color = AppTheme.TextMuted
                )
            }
        }
    }
}

@Composable
private fun SummaryCard(summary: String) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = AppTheme.CardBackground),
        shape = RoundedCornerShape(com.astro.storm.ui.theme.NeoVedicTokens.ElementCornerRadius)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Icon(
                    imageVector = Icons.Outlined.AutoAwesome,
                    contentDescription = null,
                    tint = AppTheme.AccentGold,
                    modifier = Modifier.size(24.dp)
                )
                Text(
                    text = stringResource(StringKeyDosha.GRAHA_ANALYSIS_SUMMARY),
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = AppTheme.TextPrimary
                )
            }

            Text(
                text = summary,
                style = MaterialTheme.typography.bodyMedium,
                color = AppTheme.TextSecondary,
                lineHeight = 22.sp
            )
        }
    }
}

@Composable
private fun ImpactAssessmentCard(analysis: GrahaYuddhaAnalysis) {
    val impactLevel = analysis.interpretation.overallWarImpact
    val impactColor = when (impactLevel) {
        WarImpactLevel.NONE -> AppTheme.SuccessColor
        WarImpactLevel.MILD -> com.astro.storm.ui.theme.SuccessDark
        WarImpactLevel.MODERATE -> AppTheme.AccentGold
        WarImpactLevel.SIGNIFICANT -> AppTheme.WarningColor
        WarImpactLevel.SEVERE -> AppTheme.ErrorColor
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = AppTheme.CardBackground),
        shape = RoundedCornerShape(com.astro.storm.ui.theme.NeoVedicTokens.ElementCornerRadius)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = stringResource(StringKeyDosha.GRAHA_OVERALL_IMPACT),
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.SemiBold,
                color = AppTheme.TextPrimary
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                LinearProgressIndicator(
                    progress = { when (impactLevel) {
                        WarImpactLevel.NONE -> 0f
                        WarImpactLevel.MILD -> 0.25f
                        WarImpactLevel.MODERATE -> 0.5f
                        WarImpactLevel.SIGNIFICANT -> 0.75f
                        WarImpactLevel.SEVERE -> 1f
                    } },
                    modifier = Modifier
                        .weight(1f)
                        .height(10.dp)
                        .clip(RoundedCornerShape(com.astro.storm.ui.theme.NeoVedicTokens.ElementCornerRadius)),
                    color = impactColor,
                    trackColor = AppTheme.ChipBackground
                )
                Surface(
                    color = impactColor.copy(alpha = 0.1f),
                    shape = RoundedCornerShape(com.astro.storm.ui.theme.NeoVedicTokens.ElementCornerRadius)
                ) {
                    Text(
                        text = impactLevel.displayName,
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = FontWeight.Medium,
                        color = impactColor
                    )
                }
            }

            // Winner/Loser summary
            if (analysis.activeWars.isNotEmpty()) {
                Divider(color = AppTheme.BorderColor, modifier = Modifier.padding(vertical = 4.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    val winners = analysis.activeWars.map { it.winner }.distinct()
                    val losers = analysis.activeWars.map { it.loser }.distinct()

                    // Winners
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = stringResource(StringKeyDosha.GRAHA_VICTORY),
                            style = MaterialTheme.typography.labelSmall,
                            color = AppTheme.TextMuted
                        )
                        Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                            winners.forEach { planet ->
                                PlanetChip(planet = planet, isWinner = true)
                            }
                        }
                    }

                    // Losers
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = stringResource(StringKeyDosha.GRAHA_DEFEATED),
                            style = MaterialTheme.typography.labelSmall,
                            color = AppTheme.TextMuted
                        )
                        Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                            losers.forEach { planet ->
                                PlanetChip(planet = planet, isWinner = false)
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun PlanetChip(planet: com.astro.storm.core.model.Planet, isWinner: Boolean) {
    val language = com.astro.storm.data.localization.currentLanguage()
    val color = if (isWinner) com.astro.storm.ui.theme.AppTheme.SuccessColor else com.astro.storm.ui.theme.AppTheme.ErrorColor

    Surface(
        color = color.copy(alpha = 0.1f),
        shape = RoundedCornerShape(com.astro.storm.ui.theme.NeoVedicTokens.ElementCornerRadius)
    ) {
        Text(
            text = planet.symbol,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
            style = MaterialTheme.typography.labelMedium,
            color = color
        )
    }
}

@Composable
private fun KeyInsightsCard(insights: List<String>) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = AppTheme.CardBackground),
        shape = RoundedCornerShape(com.astro.storm.ui.theme.NeoVedicTokens.ElementCornerRadius)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Icon(
                    imageVector = Icons.Outlined.Lightbulb,
                    contentDescription = null,
                    tint = AppTheme.AccentGold,
                    modifier = Modifier.size(20.dp)
                )
                Text(
                    text = stringResource(StringKeyDosha.UI_KEY_INSIGHTS),
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.SemiBold,
                    color = AppTheme.TextPrimary
                )
            }

            insights.forEach { insight ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.ArrowRight,
                        contentDescription = null,
                        tint = AppTheme.AccentPrimary,
                        modifier = Modifier.size(16.dp)
                    )
                    Text(
                        text = insight,
                        style = MaterialTheme.typography.bodySmall,
                        color = AppTheme.TextSecondary
                    )
                }
            }
        }
    }
}

@Composable
private fun AffectedAreasCard(areas: List<String>) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = AppTheme.CardBackground),
        shape = RoundedCornerShape(com.astro.storm.ui.theme.NeoVedicTokens.ElementCornerRadius)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = stringResource(StringKeyDosha.GRAHA_AFFECTED_AREAS),
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.SemiBold,
                color = AppTheme.TextPrimary
            )

            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(areas) { area ->
                    SuggestionChip(
                        onClick = {},
                        label = { Text(area, style = MaterialTheme.typography.labelSmall) },
                        colors = SuggestionChipDefaults.suggestionChipColors(
                            containerColor = AppTheme.WarningColor.copy(alpha = 0.1f),
                            labelColor = AppTheme.WarningColor
                        ),
                        border = null
                    )
                }
            }
        }
    }
}

@Composable
private fun RecommendationsCard(recommendations: List<String>) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = AppTheme.CardBackground),
        shape = RoundedCornerShape(com.astro.storm.ui.theme.NeoVedicTokens.ElementCornerRadius)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Icon(
                    imageVector = Icons.Outlined.CheckCircle,
                    contentDescription = null,
                    tint = AppTheme.SuccessColor,
                    modifier = Modifier.size(20.dp)
                )
                Text(
                    text = stringResource(StringKeyDosha.UI_RECOMMENDATIONS),
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.SemiBold,
                    color = AppTheme.TextPrimary
                )
            }

            recommendations.forEach { rec ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = "\u2022",
                        color = AppTheme.AccentPrimary
                    )
                    Text(
                        text = rec,
                        style = MaterialTheme.typography.bodySmall,
                        color = AppTheme.TextSecondary,
                        lineHeight = 18.sp
                    )
                }
            }
        }
    }
}

// ============================================
// ACTIVE WARS TAB
// ============================================

@Composable
private fun ActiveWarsTab(analysis: GrahaYuddhaAnalysis) {
    if (analysis.activeWars.isEmpty()) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Icon(
                    imageVector = Icons.Outlined.Shield,
                    contentDescription = null,
                    tint = AppTheme.SuccessColor,
                    modifier = Modifier.size(48.dp)
                )
                Text(
                    text = stringResource(StringKeyDosha.GRAHA_NO_WARS),
                    style = MaterialTheme.typography.bodyMedium,
                    color = AppTheme.TextMuted
                )
                Text(
                    text = stringResource(StringKeyDosha.GRAHA_NO_WARS_DESC),
                    style = MaterialTheme.typography.bodySmall,
                    color = AppTheme.TextSubtle
                )
            }
        }
    } else {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(analysis.activeWars) { war ->
                WarCard(war = war)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun WarCard(war: GrahaYuddhaResult) {
    var isExpanded by remember { mutableStateOf(false) }

    val intensityColor = when (war.intensityLevel) {
        WarIntensity.SEVERE -> AppTheme.ErrorColor
        WarIntensity.INTENSE -> AppTheme.WarningColor
        WarIntensity.MODERATE -> AppTheme.AccentGold
        WarIntensity.MILD -> AppTheme.TextMuted
    }

    Card(
        onClick = { isExpanded = !isExpanded },
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = AppTheme.CardBackground),
        shape = RoundedCornerShape(com.astro.storm.ui.theme.NeoVedicTokens.ElementCornerRadius)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Header - Winner vs Loser
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    // Winner
                    Box(
                        modifier = Modifier
                            .size(48.dp)
                            .clip(CircleShape)
                            .background(AppTheme.SuccessColor.copy(alpha = 0.15f))
                            .border(2.dp, AppTheme.SuccessColor, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = war.winner.symbol,
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = AppTheme.SuccessColor
                        )
                    }

                    // VS indicator
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.CompareArrows,
                            contentDescription = null,
                            tint = intensityColor,
                            modifier = Modifier.size(20.dp)
                        )
                        Text(
                            text = stringResource(StringKeyDosha.GRAHA_VS),
                            style = MaterialTheme.typography.labelSmall,
                            color = AppTheme.TextMuted
                        )
                    }

                    // Loser
                    Box(
                        modifier = Modifier
                            .size(48.dp)
                            .clip(CircleShape)
                            .background(AppTheme.ErrorColor.copy(alpha = 0.15f))
                            .border(2.dp, AppTheme.ErrorColor, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = war.loser.symbol,
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = AppTheme.ErrorColor
                        )
                    }
                }

                // Intensity badge
                Surface(
                    color = intensityColor.copy(alpha = 0.1f),
                    shape = RoundedCornerShape(com.astro.storm.ui.theme.NeoVedicTokens.ElementCornerRadius)
                ) {
                    Text(
                        text = war.intensityLevel.displayName,
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = FontWeight.SemiBold,
                        color = intensityColor
                    )
                }
            }

            // War details
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(
                        text = stringResource(StringKeyDosha.GRAHA_DEFEATS_MSG, war.winner.displayName, war.loser.displayName),
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Medium,
                        color = AppTheme.TextPrimary
                    )
                    Text(
                        text = stringResource(StringKeyDosha.GRAHA_IN_HOUSE_MSG, war.warSign.displayName, war.warHouse),
                        style = MaterialTheme.typography.bodySmall,
                        color = AppTheme.TextMuted
                    )
                }
            }

            // Separation and advantage
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                DetailChip(
                    label = stringResource(StringKeyDosha.GRAHA_SEPARATION),
                    value = "${String.format("%.2f", war.separation)}\u00B0"
                )
                DetailChip(
                    label = stringResource(StringKeyDosha.GRAHA_ADVANTAGE),
                    value = war.winnerAdvantage.displayName
                )
            }

            // Expand button
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                Icon(
                    imageVector = if (isExpanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                    contentDescription = null,
                    tint = AppTheme.TextMuted
                )
            }

            // Expanded content
            AnimatedVisibility(
                visible = isExpanded,
                enter = expandVertically() + fadeIn(),
                exit = shrinkVertically() + fadeOut()
            ) {
                Column(
                    modifier = Modifier.padding(top = 8.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Divider(color = AppTheme.BorderColor)

                    // Interpretation
                    Text(
                        text = war.interpretation,
                        style = MaterialTheme.typography.bodySmall,
                        color = AppTheme.TextSecondary,
                        lineHeight = 20.sp
                    )

                    // Winner effects
                    val language = currentLanguage()
                    EffectsSection(
                        title = stringResource(StringKeyDosha.GRAHA_WINNER_SUFFIX, war.winner.getLocalizedName(language)),
                        color = AppTheme.SuccessColor,
                        content = war.winnerEffects.overallBenefit
                    )

                    // Loser effects
                    EffectsSection(
                        title = stringResource(StringKeyDosha.GRAHA_LOSER_SUFFIX, war.loser.getLocalizedName(language)),
                        color = AppTheme.ErrorColor,
                        content = war.loserEffects.overallDeficit
                    )

                    // Weakness areas
                    Text(
                        text = stringResource(StringKeyDosha.GRAHA_WEAKNESS_AREAS),
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = FontWeight.Medium,
                        color = AppTheme.TextPrimary
                    )
                    LazyRow(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                        items(war.loserEffects.weaknessAreas) { area ->
                            SuggestionChip(
                                onClick = {},
                                label = { Text(area, style = MaterialTheme.typography.labelSmall) },
                                colors = SuggestionChipDefaults.suggestionChipColors(
                                    containerColor = AppTheme.ChipBackground,
                                    labelColor = AppTheme.TextMuted
                                ),
                                border = null
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun DetailChip(label: String, value: String) {
    Surface(
        color = AppTheme.ChipBackground,
        shape = RoundedCornerShape(com.astro.storm.ui.theme.NeoVedicTokens.ElementCornerRadius)
    ) {
        Column(
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = label,
                style = MaterialTheme.typography.labelSmall,
                color = AppTheme.TextMuted
            )
            Text(
                text = value,
                style = MaterialTheme.typography.bodySmall,
                fontWeight = FontWeight.Medium,
                color = AppTheme.TextPrimary
            )
        }
    }
}

@Composable
private fun EffectsSection(title: String, color: Color, content: String) {
    Surface(
        color = color.copy(alpha = 0.05f),
        shape = RoundedCornerShape(com.astro.storm.ui.theme.NeoVedicTokens.ElementCornerRadius)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp)
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.labelMedium,
                fontWeight = FontWeight.SemiBold,
                color = color
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = content,
                style = MaterialTheme.typography.bodySmall,
                color = AppTheme.TextSecondary,
                lineHeight = 18.sp
            )
        }
    }
}

// ============================================
// DASHA EFFECTS TAB
// ============================================

@Composable
private fun DashaEffectsTab(analysis: GrahaYuddhaAnalysis) {
    if (analysis.dashaWarEffects.isEmpty()) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Icon(
                    imageVector = Icons.Outlined.Timeline,
                    contentDescription = null,
                    tint = AppTheme.TextMuted,
                    modifier = Modifier.size(48.dp)
                )
                Text(
                    text = stringResource(StringKeyDosha.GRAHA_NO_DASHA_EFFECTS),
                    style = MaterialTheme.typography.bodyMedium,
                    color = AppTheme.TextMuted
                )
            }
        }
    } else {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(analysis.dashaWarEffects) { effect ->
                DashaEffectCard(effect = effect)
            }
        }
    }
}

@Composable
private fun DashaEffectCard(effect: DashaWarEffect) {
    val color = if (effect.isWinner) AppTheme.SuccessColor else AppTheme.ErrorColor

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = AppTheme.CardBackground),
        shape = RoundedCornerShape(com.astro.storm.ui.theme.NeoVedicTokens.ElementCornerRadius)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Header
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                        .background(color.copy(alpha = 0.15f)),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = effect.planet.symbol,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = color
                    )
                }
                Column {
                    val language = currentLanguage()
                    Text(
                        text = stringResource(StringKeyDosha.GRAHA_PERIODS_LABEL_FMT, effect.planet.getLocalizedName(language)),
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.SemiBold,
                        color = AppTheme.TextPrimary
                    )
                    Text(
                        text = if (effect.isWinner) 
                            stringResource(StringKeyDosha.GRAHA_VICTOR_LABEL) 
                        else 
                            stringResource(StringKeyDosha.GRAHA_DEFEATED_LABEL),
                        style = MaterialTheme.typography.labelSmall,
                        color = color
                    )
                }
            }

            // Mahadasha
            DashaEffectRow(
                icon = Icons.Outlined.Timeline,
                title = stringResource(StringKeyMatch.DASHA_LEVEL_MAHADASHA),
                content = effect.mahadashaEffect
            )

            // Antardasha
            DashaEffectRow(
                icon = Icons.Outlined.Schedule,
                title = stringResource(StringKeyMatch.DASHA_LEVEL_ANTARDASHA),
                content = effect.antardashaEffect
            )

            // Transit
            DashaEffectRow(
                icon = Icons.Outlined.Sync,
                title = stringResource(StringKeyAnalysis.TRANSIT_LABEL),
                content = effect.transitEffect
            )
        }
    }
}

@Composable
private fun DashaEffectRow(
    icon: ImageVector,
    title: String,
    content: String
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = AppTheme.AccentPrimary,
            modifier = Modifier.size(16.dp)
        )
        Column {
            Text(
                text = title,
                style = MaterialTheme.typography.labelMedium,
                fontWeight = FontWeight.Medium,
                color = AppTheme.TextPrimary
            )
            Text(
                text = content,
                style = MaterialTheme.typography.bodySmall,
                color = AppTheme.TextMuted,
                lineHeight = 18.sp
            )
        }
    }
}

// ============================================
// REMEDIES TAB
// ============================================

@Composable
private fun RemediesTab(analysis: GrahaYuddhaAnalysis) {
    if (analysis.remedies.isEmpty()) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Icon(
                    imageVector = Icons.Outlined.Spa,
                    contentDescription = null,
                    tint = AppTheme.SuccessColor,
                    modifier = Modifier.size(48.dp)
                )
                Text(
                    text = stringResource(StringKeyDosha.GRAHA_NO_REMEDIES),
                    style = MaterialTheme.typography.bodyMedium,
                    color = AppTheme.TextMuted
                )
                Text(
                    text = stringResource(StringKeyDosha.GRAHA_NO_REMEDIES_DESC),
                    style = MaterialTheme.typography.bodySmall,
                    color = AppTheme.TextSubtle
                )
            }
        }
    } else {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Group by planet
            val groupedRemedies = analysis.remedies.groupBy { it.forPlanet }

            groupedRemedies.forEach { (planet, remedies) ->
                item {
                    val language = currentLanguage()
                    Text(
                        text = planet?.getLocalizedName(language) ?: stringResource(StringKeyDosha.GRAHA_GENERAL_REMEDIES),
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.SemiBold,
                        color = AppTheme.TextPrimary,
                        modifier = Modifier.padding(vertical = 4.dp)
                    )
                }

                items(remedies) { remedy ->
                    RemedyCard(remedy = remedy)
                }
            }
        }
    }
}

@Composable
private fun RemedyCard(remedy: WarRemedy) {
    val typeIcon = when (remedy.type) {
        RemedyType.MANTRA -> Icons.Outlined.RecordVoiceOver
        RemedyType.CHARITY -> Icons.Outlined.VolunteerActivism
        RemedyType.WORSHIP -> Icons.Outlined.SelfImprovement
        RemedyType.GEMSTONE -> Icons.Outlined.Diamond
        RemedyType.GENERAL -> Icons.Outlined.AutoAwesome
    }

    val typeName = when (remedy.type) {
        RemedyType.MANTRA -> stringResource(StringKeyRemedy.CAT_MANTRA)
        RemedyType.CHARITY -> stringResource(StringKeyRemedy.CAT_CHARITY)
        RemedyType.WORSHIP -> stringResource(StringKeyRemedy.CAT_DEITY)
        RemedyType.GEMSTONE -> stringResource(StringKeyRemedy.CAT_GEMSTONE)
        RemedyType.GENERAL -> stringResource(StringKeyRemedy.CAT_LIFESTYLE)
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = AppTheme.CardBackground),
        shape = RoundedCornerShape(com.astro.storm.ui.theme.NeoVedicTokens.ElementCornerRadius)
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
                    .clip(CircleShape)
                    .background(AppTheme.AccentGold.copy(alpha = 0.15f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = typeIcon,
                    contentDescription = null,
                    tint = AppTheme.AccentGold,
                    modifier = Modifier.size(22.dp)
                )
            }

            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = typeName,
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = AppTheme.AccentGold
                )
                Text(
                    text = remedy.description,
                    style = MaterialTheme.typography.bodyMedium,
                    color = AppTheme.TextPrimary
                )

                remedy.mantra?.let { mantra ->
                    Surface(
                        color = AppTheme.ChipBackground,
                        shape = RoundedCornerShape(com.astro.storm.ui.theme.NeoVedicTokens.ElementCornerRadius)
                    ) {
                        Text(
                            text = mantra,
                            modifier = Modifier.padding(8.dp),
                            style = MaterialTheme.typography.bodySmall,
                            color = AppTheme.AccentPrimary
                        )
                    }
                }

                Row(
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Schedule,
                        contentDescription = null,
                        tint = AppTheme.TextMuted,
                        modifier = Modifier.size(14.dp)
                    )
                    Text(
                        text = remedy.timing,
                        style = MaterialTheme.typography.labelSmall,
                        color = AppTheme.TextMuted
                    )
                }

                Text(
                    text = remedy.expectedBenefit,
                    style = MaterialTheme.typography.bodySmall,
                    color = AppTheme.TextSecondary
                )
            }
        }
    }
}

// ============================================
// INFO DIALOG
// ============================================

@Composable
private fun GrahaYuddhaInfoDialog(onDismiss: () -> Unit) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = stringResource(StringKeyDosha.GRAHA_ABOUT_TITLE),
                fontWeight = FontWeight.SemiBold,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
        },
        text = {
            Column(
                modifier = Modifier.verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(
                    text = stringResource(StringKeyDosha.GRAHA_ABOUT_DESC),
                    style = MaterialTheme.typography.bodyMedium,
                    color = AppTheme.TextSecondary,
                    lineHeight = 22.sp
                )
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text(stringResource(StringKeyDosha.BTN_GOT_IT), color = AppTheme.AccentPrimary)
            }
        },
        containerColor = AppTheme.CardBackground
    )
}





