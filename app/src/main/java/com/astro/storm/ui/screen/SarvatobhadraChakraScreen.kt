package com.astro.storm.ui.screen

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.outlined.CalendarMonth
import androidx.compose.material.icons.outlined.GridOn
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.Lightbulb
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material.icons.outlined.Today
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
import com.astro.storm.ui.components.ScreenTopBar
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
import com.astro.storm.core.common.StringKey
import com.astro.storm.core.common.StringKeyAnalysis
import com.astro.storm.core.common.StringKeyShadbala
import com.astro.storm.core.common.StringKeyUICommon
import com.astro.storm.data.localization.stringResource
import com.astro.storm.core.model.VedicChart
import com.astro.storm.ephemeris.SarvatobhadraChakraCalculator
import com.astro.storm.ui.theme.AppTheme
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext

/**
 * Sarvatobhadra Chakra Screen
 *
 * Displays the 9x9 Vedha chakra for:
 * - Transit analysis with vedha effects
 * - Daily muhurta assessment
 * - Name compatibility analysis
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SarvatobhadraChakraScreen(
    chart: VedicChart?,
    onBack: () -> Unit
) {
    val language = LocalLanguage.current
    var selectedTab by rememberSaveable { mutableIntStateOf(0) }
    var isCalculating by remember { mutableStateOf(true) }
    var analysis by remember { mutableStateOf<SarvatobhadraChakraCalculator.SarvatobhadraAnalysis?>(null) }
    var showInfoDialog by remember { mutableStateOf(false) }

    val tabs = listOf(
        stringResource(StringKeyShadbala.SARVATOBHADRA_TAB_OVERVIEW),
        stringResource(StringKeyShadbala.SARVATOBHADRA_TAB_CHAKRA),
        stringResource(StringKeyShadbala.SARVATOBHADRA_TAB_DAILY),
        stringResource(StringKeyShadbala.SARVATOBHADRA_TAB_VEDHA),
        stringResource(StringKeyShadbala.SARVATOBHADRA_TAB_NAME)
    )

    LaunchedEffect(chart) {
        if (chart == null) {
            isCalculating = false
            return@LaunchedEffect
        }
        isCalculating = true
        delay(300)
        try {
            analysis = withContext(Dispatchers.Default) {
                SarvatobhadraChakraCalculator.analyzeSarvatobhadra(chart)
            }
        } catch (_: Exception) { }
        isCalculating = false
    }

    Scaffold(
        containerColor = AppTheme.ScreenBackground,
        topBar = {
            ScreenTopBar(
                title = stringResource(StringKeyShadbala.SARVATOBHADRA_TITLE),
                subtitle = stringResource(StringKeyShadbala.SARVATOBHADRA_SUBTITLE),
                onBack = onBack,
                actions = {
                    IconButton(onClick = { showInfoDialog = true }) {
                        Icon(
                            imageVector = Icons.Outlined.Info,
                            contentDescription = stringResource(StringKeyUICommon.INFO),
                            tint = AppTheme.TextSecondary
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        when {
            isCalculating -> SarvatobhadraLoadingContent(modifier = Modifier.padding(paddingValues))
            chart == null || analysis == null -> SarvatobhadraEmptyContent(modifier = Modifier.padding(paddingValues))
            else -> {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                        .background(AppTheme.ScreenBackground),
                    contentPadding = PaddingValues(bottom = 32.dp)
                ) {
                    item {
                        SarvatobhadraTabSelector(
                            tabs = tabs,
                            selectedTab = selectedTab,
                            onTabSelected = { selectedTab = it }
                        )
                    }
                    when (selectedTab) {
                        0 -> item { SarvatobhadraOverviewSection(analysis!!) }
                        1 -> item { SarvatobhadraChakraGridSection(analysis!!) }
                        2 -> item { SarvatobhadraDailySection(analysis!!) }
                        3 -> item { SarvatobhadraVedhaSection(analysis!!) }
                        4 -> item { SarvatobhadraNameSection(analysis!!) }
                    }
                }
            }
        }
    }

    if (showInfoDialog) {
        SarvatobhadraInfoDialog(onDismiss = { showInfoDialog = false })
    }
}

@Composable
private fun SarvatobhadraTabSelector(
    tabs: List<String>,
    selectedTab: Int,
    onTabSelected: (Int) -> Unit
) {
    LazyRow(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 12.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(tabs.size) { index ->
            FilterChip(
                selected = selectedTab == index,
                onClick = { onTabSelected(index) },
                label = {
                    Text(
                        text = tabs[index],
                        fontSize = 13.sp,
                        fontWeight = if (selectedTab == index) FontWeight.SemiBold else FontWeight.Normal
                    )
                },
                colors = FilterChipDefaults.filterChipColors(
                    selectedContainerColor = AppTheme.AccentPrimary.copy(alpha = 0.15f),
                    selectedLabelColor = AppTheme.AccentPrimary,
                    containerColor = AppTheme.CardBackground,
                    labelColor = AppTheme.TextSecondary
                ),
                border = FilterChipDefaults.filterChipBorder(
                    borderColor = AppTheme.BorderColor,
                    selectedBorderColor = AppTheme.AccentPrimary.copy(alpha = 0.3f),
                    enabled = true,
                    selected = selectedTab == index
                )
            )
        }
    }
}

@Composable
private fun SarvatobhadraOverviewSection(analysis: SarvatobhadraChakraCalculator.SarvatobhadraAnalysis) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Birth Nakshatra Card
        BirthNakshatraCard(analysis)
        // Transit Score Card
        TransitScoreCard(analysis)
        // Quick Stats
        QuickStatsRow(analysis)
        // Key Insights
        KeyInsightsCard(analysis)
    }
}

@Composable
private fun BirthNakshatraCard(analysis: SarvatobhadraChakraCalculator.SarvatobhadraAnalysis) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = AppTheme.AccentPrimary.copy(alpha = 0.1f)),
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
                    .background(AppTheme.AccentPrimary.copy(alpha = 0.15f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Outlined.Star,
                    contentDescription = null,
                    tint = AppTheme.AccentPrimary,
                    modifier = Modifier.size(28.dp)
                )
            }
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = stringResource(StringKeyShadbala.SARVATOBHADRA_BIRTH_NAKSHATRA),
                    style = MaterialTheme.typography.labelSmall,
                    color = AppTheme.TextMuted
                )
                Text(
                    text = analysis.birthNakshatra.displayName,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = AppTheme.TextPrimary
                )
                Text(
                    text = "Pada ${analysis.birthPada} • ${analysis.birthNakshatra.ruler.displayName}",
                    style = MaterialTheme.typography.bodySmall,
                    color = AppTheme.TextSecondary
                )
            }
        }
    }
}

@Composable
private fun TransitScoreCard(analysis: SarvatobhadraChakraCalculator.SarvatobhadraAnalysis) {
    val scoreColor = when {
        analysis.overallTransitScore >= 70 -> AppTheme.SuccessColor
        analysis.overallTransitScore >= 50 -> AppTheme.AccentGold
        analysis.overallTransitScore >= 30 -> AppTheme.WarningColor
        else -> AppTheme.ErrorColor
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = AppTheme.CardBackground),
        shape = RoundedCornerShape(com.astro.storm.ui.theme.NeoVedicTokens.ElementCornerRadius)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = stringResource(StringKeyShadbala.SARVATOBHADRA_TRANSIT_SCORE),
                style = MaterialTheme.typography.labelMedium,
                color = AppTheme.TextMuted
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "${analysis.overallTransitScore}",
                style = MaterialTheme.typography.displaySmall,
                fontWeight = FontWeight.Bold,
                color = scoreColor
            )
            Text(
                text = "/100",
                style = MaterialTheme.typography.bodySmall,
                color = AppTheme.TextMuted
            )
            Spacer(modifier = Modifier.height(12.dp))
            LinearProgressIndicator(
                progress = { analysis.overallTransitScore / 100f },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(8.dp)
                    .clip(RoundedCornerShape(com.astro.storm.ui.theme.NeoVedicTokens.ElementCornerRadius)),
                color = scoreColor,
                trackColor = scoreColor.copy(alpha = 0.2f),
                strokeCap = StrokeCap.Round
            )
        }
    }
}

@Composable
private fun QuickStatsRow(analysis: SarvatobhadraChakraCalculator.SarvatobhadraAnalysis) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        QuickStatCard(
            title = stringResource(StringKeyShadbala.SARVATOBHADRA_FAVORABLE_DAYS),
            value = "${analysis.favorableDays.size}",
            color = AppTheme.SuccessColor,
            modifier = Modifier.weight(1f)
        )
        QuickStatCard(
            title = stringResource(StringKeyShadbala.SARVATOBHADRA_TAB_VEDHA),
            value = "${analysis.currentTransitVedhas.size}",
            color = AppTheme.AccentPrimary,
            modifier = Modifier.weight(1f)
        )
        QuickStatCard(
            title = stringResource(StringKeyShadbala.SARVATOBHADRA_UNFAVORABLE_DAYS),
            value = "${analysis.unfavorableDays.size}",
            color = AppTheme.ErrorColor,
            modifier = Modifier.weight(1f)
        )
    }
}

@Composable
private fun QuickStatCard(
    title: String,
    value: String,
    color: Color,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(containerColor = AppTheme.CardBackground),
        shape = RoundedCornerShape(com.astro.storm.ui.theme.NeoVedicTokens.ElementCornerRadius)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = value,
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                color = color
            )
            Text(
                text = title,
                style = MaterialTheme.typography.labelSmall,
                color = AppTheme.TextMuted,
                textAlign = TextAlign.Center,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

@Composable
private fun KeyInsightsCard(analysis: SarvatobhadraChakraCalculator.SarvatobhadraAnalysis) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = AppTheme.CardBackground),
        shape = RoundedCornerShape(com.astro.storm.ui.theme.NeoVedicTokens.ElementCornerRadius)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
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
                    text = stringResource(StringKeyAnalysis.UI_KEY_INSIGHTS),
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.SemiBold,
                    color = AppTheme.TextPrimary
                )
            }
            Spacer(modifier = Modifier.height(12.dp))
            analysis.keyInsights.forEach { insight ->
                Row(
                    modifier = Modifier.padding(vertical = 4.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = "•",
                        color = AppTheme.AccentGold,
                        fontWeight = FontWeight.Bold
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
private fun SarvatobhadraChakraGridSection(analysis: SarvatobhadraChakraCalculator.SarvatobhadraAnalysis) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = AppTheme.CardBackground),
            shape = RoundedCornerShape(com.astro.storm.ui.theme.NeoVedicTokens.ElementCornerRadius)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(
                        imageVector = Icons.Outlined.GridOn,
                        contentDescription = null,
                        tint = AppTheme.AccentPrimary,
                        modifier = Modifier.size(20.dp)
                    )
                    Text(
                        text = "9×9 Chakra Grid",
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.SemiBold,
                        color = AppTheme.TextPrimary
                    )
                }
                Spacer(modifier = Modifier.height(16.dp))

                // Scrollable grid
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState())
                ) {
                    Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                        for (row in 0..8) {
                            Row(horizontalArrangement = Arrangement.spacedBy(2.dp)) {
                                for (col in 0..8) {
                                    val cell = analysis.chakraGrid[row][col]
                                    ChakraGridCell(cell, analysis.birthNakshatra)
                                }
                            }
                        }
                    }
                }
            }
        }

        // Legend
        ChakraLegendCard()
    }
}

@Composable
private fun ChakraGridCell(
    cell: SarvatobhadraChakraCalculator.ChakraCell,
    birthNakshatra: com.astro.storm.core.model.Nakshatra
) {
    val (bgColor, textColor) = when (cell.cellType) {
        SarvatobhadraChakraCalculator.CellType.NAKSHATRA -> {
            if (cell.nakshatra == birthNakshatra) {
                AppTheme.AccentPrimary.copy(alpha = 0.3f) to AppTheme.AccentPrimary
            } else {
                AppTheme.CardBackgroundElevated to AppTheme.TextSecondary
            }
        }
        SarvatobhadraChakraCalculator.CellType.VOWEL -> AppTheme.AccentGold.copy(alpha = 0.15f) to AppTheme.AccentGold
        SarvatobhadraChakraCalculator.CellType.WEEKDAY -> AppTheme.AccentTeal.copy(alpha = 0.15f) to AppTheme.AccentTeal
        SarvatobhadraChakraCalculator.CellType.TITHI -> AppTheme.SuccessColor.copy(alpha = 0.15f) to AppTheme.SuccessColor
        SarvatobhadraChakraCalculator.CellType.CENTER -> AppTheme.AccentPrimary.copy(alpha = 0.2f) to AppTheme.AccentPrimary
        SarvatobhadraChakraCalculator.CellType.EMPTY -> AppTheme.CardBackground to AppTheme.TextMuted
    }

    Box(
        modifier = Modifier
            .size(36.dp)
            .clip(RoundedCornerShape(com.astro.storm.ui.theme.NeoVedicTokens.ElementCornerRadius))
            .background(bgColor)
            .border(0.5.dp, AppTheme.BorderColor, RoundedCornerShape(com.astro.storm.ui.theme.NeoVedicTokens.ElementCornerRadius)),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = cell.displayLabel,
            fontSize = 8.sp,
            fontWeight = FontWeight.Medium,
            color = textColor,
            textAlign = TextAlign.Center,
            maxLines = 1
        )
    }
}

@Composable
private fun ChakraLegendCard() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = AppTheme.CardBackground),
        shape = RoundedCornerShape(com.astro.storm.ui.theme.NeoVedicTokens.ElementCornerRadius)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            LegendItem(stringResource(StringKeyShadbala.SARVATOBHADRA_NAKSHATRA), AppTheme.CardBackgroundElevated)
            LegendItem(stringResource(StringKeyShadbala.SARVATOBHADRA_VOWEL), AppTheme.AccentGold.copy(alpha = 0.15f))
            LegendItem(stringResource(StringKeyShadbala.SARVATOBHADRA_WEEKDAY), AppTheme.AccentTeal.copy(alpha = 0.15f))
        }
    }
}

@Composable
private fun LegendItem(label: String, color: Color) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Box(
            modifier = Modifier
                .size(12.dp)
                .clip(RoundedCornerShape(com.astro.storm.ui.theme.NeoVedicTokens.ElementCornerRadius))
                .background(color)
                .border(0.5.dp, AppTheme.BorderColor, RoundedCornerShape(com.astro.storm.ui.theme.NeoVedicTokens.ElementCornerRadius))
        )
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall,
            color = AppTheme.TextMuted
        )
    }
}

@Composable
private fun SarvatobhadraDailySection(analysis: SarvatobhadraChakraCalculator.SarvatobhadraAnalysis) {
    val daily = analysis.dailyAnalysis ?: return

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Today's Overview
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = AppTheme.CardBackground),
            shape = RoundedCornerShape(com.astro.storm.ui.theme.NeoVedicTokens.ElementCornerRadius)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Today,
                        contentDescription = null,
                        tint = AppTheme.AccentPrimary,
                        modifier = Modifier.size(20.dp)
                    )
                    Text(
                        text = stringResource(StringKeyShadbala.SARVATOBHADRA_TODAY),
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.SemiBold,
                        color = AppTheme.TextPrimary
                    )
                }
                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    DailyInfoItem(daily.weekday.name.take(3), "Day")
                    DailyInfoItem(daily.moonNakshatra.displayName.take(6), "Moon")
                    DailyInfoItem("${daily.tithiNumber}", "Tithi")
                    DailyInfoItem("${daily.overallScore}/100", "Score")
                }

                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = daily.interpretation,
                    style = MaterialTheme.typography.bodySmall,
                    color = AppTheme.TextSecondary
                )
            }
        }

        // Favorable Activities
        if (daily.favorableActivities.isNotEmpty()) {
            ActivitiesCard(
                title = stringResource(StringKeyShadbala.SARVATOBHADRA_FAVORABLE_ACTIVITIES),
                activities = daily.favorableActivities,
                color = AppTheme.SuccessColor
            )
        }

        // Unfavorable Activities
        if (daily.unfavorableActivities.isNotEmpty()) {
            ActivitiesCard(
                title = stringResource(StringKeyShadbala.SARVATOBHADRA_UNFAVORABLE_ACTIVITIES),
                activities = daily.unfavorableActivities,
                color = AppTheme.ErrorColor
            )
        }
    }
}

@Composable
private fun DailyInfoItem(value: String, label: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = value,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = AppTheme.TextPrimary
        )
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall,
            color = AppTheme.TextMuted
        )
    }
}

@Composable
private fun ActivitiesCard(title: String, activities: List<String>, color: Color) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = color.copy(alpha = 0.08f)),
        shape = RoundedCornerShape(com.astro.storm.ui.theme.NeoVedicTokens.ElementCornerRadius)
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Text(
                text = title,
                style = MaterialTheme.typography.labelMedium,
                fontWeight = FontWeight.SemiBold,
                color = color
            )
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                activities.take(3).forEach { activity ->
                    Surface(
                        shape = RoundedCornerShape(com.astro.storm.ui.theme.NeoVedicTokens.ElementCornerRadius),
                        color = color.copy(alpha = 0.1f)
                    ) {
                        Text(
                            text = activity,
                            style = MaterialTheme.typography.labelSmall,
                            color = color,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun SarvatobhadraVedhaSection(analysis: SarvatobhadraChakraCalculator.SarvatobhadraAnalysis) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        if (analysis.currentTransitVedhas.isEmpty()) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = AppTheme.SuccessColor.copy(alpha = 0.1f)),
                shape = RoundedCornerShape(com.astro.storm.ui.theme.NeoVedicTokens.ElementCornerRadius)
            ) {
                Text(
                    text = stringResource(StringKeyShadbala.SARVATOBHADRA_NO_VEDHA),
                    style = MaterialTheme.typography.bodyMedium,
                    color = AppTheme.SuccessColor,
                    modifier = Modifier.padding(16.dp),
                    textAlign = TextAlign.Center
                )
            }
        } else {
            analysis.currentTransitVedhas.forEach { vedha ->
                VedhaCard(vedha)
            }
        }
    }
}

@Composable
private fun VedhaCard(vedha: SarvatobhadraChakraCalculator.NakshatraVedha) {
    var expanded by remember { mutableStateOf(false) }
    val effectColor = when (vedha.effect) {
        SarvatobhadraChakraCalculator.VedhaEffect.BENEFIC -> AppTheme.SuccessColor
        SarvatobhadraChakraCalculator.VedhaEffect.MALEFIC -> AppTheme.ErrorColor
        SarvatobhadraChakraCalculator.VedhaEffect.MIXED -> AppTheme.WarningColor
        SarvatobhadraChakraCalculator.VedhaEffect.NEUTRAL -> AppTheme.TextMuted
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .animateContentSize(),
        colors = CardDefaults.cardColors(containerColor = AppTheme.CardBackground),
        shape = RoundedCornerShape(com.astro.storm.ui.theme.NeoVedicTokens.ElementCornerRadius)
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
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
                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .clip(CircleShape)
                            .background(effectColor.copy(alpha = 0.15f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = vedha.transitingPlanet.symbol,
                            fontSize = 18.sp,
                            color = effectColor
                        )
                    }
                    Column {
                        Text(
                            text = "${vedha.transitingPlanet.displayName} in ${vedha.nakshatra.displayName}",
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.Medium,
                            color = AppTheme.TextPrimary
                        )
                        Text(
                            text = "${vedha.vedhaType.displayName} • ${vedha.effect.displayName}",
                            style = MaterialTheme.typography.labelSmall,
                            color = effectColor
                        )
                    }
                }
                Icon(
                    imageVector = if (expanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                    contentDescription = null,
                    tint = AppTheme.TextMuted
                )
            }

            AnimatedVisibility(
                visible = expanded,
                enter = expandVertically() + fadeIn(),
                exit = shrinkVertically() + fadeOut()
            ) {
                Column(modifier = Modifier.padding(top = 12.dp)) {
                    HorizontalDivider(color = AppTheme.DividerColor)
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = vedha.interpretation,
                        style = MaterialTheme.typography.bodySmall,
                        color = AppTheme.TextSecondary
                    )
                }
            }
        }
    }
}

@Composable
private fun SarvatobhadraNameSection(analysis: SarvatobhadraChakraCalculator.SarvatobhadraAnalysis) {
    var inputLetter by remember { mutableStateOf("") }
    var nameAnalysis by remember { mutableStateOf<SarvatobhadraChakraCalculator.NameAnalysis?>(null) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = AppTheme.CardBackground),
            shape = RoundedCornerShape(com.astro.storm.ui.theme.NeoVedicTokens.ElementCornerRadius)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Person,
                        contentDescription = null,
                        tint = AppTheme.AccentPrimary,
                        modifier = Modifier.size(20.dp)
                    )
                    Text(
                        text = stringResource(StringKeyShadbala.SARVATOBHADRA_NAME_ANALYSIS),
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.SemiBold,
                        color = AppTheme.TextPrimary
                    )
                }
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = stringResource(StringKeyShadbala.SARVATOBHADRA_FIRST_LETTER),
                    style = MaterialTheme.typography.labelSmall,
                    color = AppTheme.TextMuted
                )
                Spacer(modifier = Modifier.height(8.dp))

                // Letter selection chips
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    val letters = listOf('A', 'E', 'I', 'O', 'U', 'K', 'R', 'S', 'M', 'N')
                    items(letters.size) { index ->
                        val letter = letters[index]
                        FilterChip(
                            selected = inputLetter == letter.toString(),
                            onClick = {
                                inputLetter = letter.toString()
                                nameAnalysis = SarvatobhadraChakraCalculator.analyzeNameCompatibility(
                                    letter, analysis.birthNakshatra
                                )
                            },
                            label = { Text(letter.toString()) },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = AppTheme.AccentPrimary,
                                selectedLabelColor = Color.White,
                                containerColor = AppTheme.CardBackgroundElevated,
                                labelColor = AppTheme.TextSecondary
                            )
                        )
                    }
                }
            }
        }

        // Name Analysis Result
        nameAnalysis?.let { na ->
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = AppTheme.CardBackground),
                shape = RoundedCornerShape(com.astro.storm.ui.theme.NeoVedicTokens.ElementCornerRadius)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    if (na.associatedSwara != null) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column {
                                Text(
                                    text = stringResource(StringKeyShadbala.SARVATOBHADRA_ASSOCIATED_VOWEL),
                                    style = MaterialTheme.typography.labelSmall,
                                    color = AppTheme.TextMuted
                                )
                                Text(
                                    text = "${na.associatedSwara.sanskrit} (${na.associatedSwara.english})",
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = AppTheme.AccentGold
                                )
                            }
                            if (na.planetaryInfluence.isNotEmpty()) {
                                Column(horizontalAlignment = Alignment.End) {
                                    Text(
                                        text = stringResource(StringKeyShadbala.SARVATOBHADRA_PLANETARY_INFLUENCE),
                                        style = MaterialTheme.typography.labelSmall,
                                        color = AppTheme.TextMuted
                                    )
                                    Row {
                                        na.planetaryInfluence.forEach { planet ->
                                            Text(
                                                text = planet.symbol,
                                                fontSize = 18.sp,
                                                color = AppTheme.AccentPrimary
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = na.interpretation,
                        style = MaterialTheme.typography.bodySmall,
                        color = AppTheme.TextSecondary
                    )
                }
            }
        }
    }
}

@Composable
private fun SarvatobhadraLoadingContent(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            CircularProgressIndicator(color = AppTheme.AccentPrimary)
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = stringResource(StringKeyShadbala.SARVATOBHADRA_ANALYZING),
                style = MaterialTheme.typography.bodyMedium,
                color = AppTheme.TextMuted
            )
        }
    }
}

@Composable
private fun SarvatobhadraEmptyContent(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(32.dp)
        ) {
            Icon(
                imageVector = Icons.Outlined.GridOn,
                contentDescription = null,
                tint = AppTheme.TextMuted,
                modifier = Modifier.size(64.dp)
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = stringResource(StringKeyShadbala.SARVATOBHADRA_UNABLE),
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
                color = AppTheme.TextPrimary
            )
        }
    }
}

@Composable
private fun SarvatobhadraInfoDialog(onDismiss: () -> Unit) {
    androidx.compose.material3.AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = stringResource(StringKeyShadbala.SARVATOBHADRA_INFO_TITLE),
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
        },
        text = {
            Column {
                Text(
                    text = stringResource(StringKeyShadbala.SARVATOBHADRA_INFO_DESC),
                    style = MaterialTheme.typography.bodyMedium,
                    color = AppTheme.TextSecondary,
                    lineHeight = 22.sp
                )
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = stringResource(StringKeyShadbala.SARVATOBHADRA_VEDIC_REF),
                    style = MaterialTheme.typography.labelSmall,
                    color = AppTheme.TextMuted,
                    fontStyle = androidx.compose.ui.text.font.FontStyle.Italic
                )
            }
        },
        confirmButton = {
            androidx.compose.material3.TextButton(onClick = onDismiss) {
                Text(stringResource(StringKeyShadbala.COMMON_GOT_IT), color = AppTheme.AccentPrimary)
            }
        },
        containerColor = AppTheme.CardBackground
    )
}



