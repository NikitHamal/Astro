package com.astro.storm.ui.screen

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.outlined.DarkMode
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.LightMode
import androidx.compose.material.icons.outlined.Lightbulb
import androidx.compose.material.icons.outlined.Schedule
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
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
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
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.astro.storm.core.common.Language
import com.astro.storm.core.common.StringKey
import com.astro.storm.core.common.StringKeyAnalysis
import com.astro.storm.data.localization.LocalLanguage
import com.astro.storm.core.common.StringKeyShadbala
import com.astro.storm.data.localization.stringResource
import com.astro.storm.core.model.VedicChart
import com.astro.storm.ephemeris.KalaBalaCalculator
import com.astro.storm.ui.theme.AppTheme
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext

/**
 * Kala Bala Screen
 *
 * Displays comprehensive temporal strength analysis showing:
 * - Overview with overall score and birth context
 * - Component breakdown (Nathonnatha, Paksha, Tribhaga, etc.)
 * - Per-planet detailed analysis
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun KalaBalaScreen(
    chart: VedicChart?,
    onBack: () -> Unit
) {
    val language = LocalLanguage.current
    val context = androidx.compose.ui.platform.LocalContext.current
    var selectedTab by rememberSaveable { mutableIntStateOf(0) }
    var isCalculating by remember { mutableStateOf(true) }
    var analysis by remember { mutableStateOf<KalaBalaCalculator.KalaBalaAnalysis?>(null) }
    var showInfoDialog by remember { mutableStateOf(false) }

    val tabs = listOf(
        stringResource(StringKeyShadbala.KALA_TAB_OVERVIEW),
        stringResource(StringKeyShadbala.KALA_TAB_COMPONENTS),
        stringResource(StringKeyShadbala.KALA_TAB_PLANETS)
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
                KalaBalaCalculator.analyzeKalaBala(context, chart)
            }
        } catch (_: Exception) { }
        isCalculating = false
    }

    Scaffold(
        containerColor = AppTheme.ScreenBackground,
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(
                            text = stringResource(StringKeyShadbala.KALA_TITLE),
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = AppTheme.TextPrimary
                        )
                        Text(
                            text = stringResource(StringKeyShadbala.KALA_SUBTITLE),
                            style = MaterialTheme.typography.bodySmall,
                            color = AppTheme.TextMuted
                        )
                    }
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = AppTheme.TextPrimary
                        )
                    }
                },
                actions = {
                    IconButton(onClick = { showInfoDialog = true }) {
                        Icon(
                            imageVector = Icons.Outlined.Info,
                            contentDescription = "Info",
                            tint = AppTheme.TextSecondary
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = AppTheme.ScreenBackground
                )
            )
        }
    ) { paddingValues ->
        when {
            isCalculating -> KalaBalaLoadingContent(modifier = Modifier.padding(paddingValues))
            chart == null || analysis == null -> KalaBalaEmptyContent(modifier = Modifier.padding(paddingValues))
            else -> {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                        .background(AppTheme.ScreenBackground),
                    contentPadding = PaddingValues(bottom = 32.dp)
                ) {
                    item {
                        KalaBalaTabSelector(
                            tabs = tabs,
                            selectedTab = selectedTab,
                            onTabSelected = { selectedTab = it }
                        )
                    }
                    when (selectedTab) {
                        0 -> item { KalaBalaOverviewSection(analysis!!) }
                        1 -> item { KalaBalaComponentsSection(analysis!!) }
                        2 -> item { KalaBalaPlanetsSection(analysis!!) }
                    }
                }
            }
        }
    }

    if (showInfoDialog) {
        KalaBalaInfoDialog(onDismiss = { showInfoDialog = false })
    }
}

@Composable
private fun KalaBalaTabSelector(
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
private fun KalaBalaOverviewSection(analysis: KalaBalaCalculator.KalaBalaAnalysis) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        BirthContextCard(analysis.birthContext)
        KalaBalaScoreCard(analysis)
        KalaBalaStrongestWeakestRow(analysis)
        KalaBalaInsightsCard(analysis)
        KalaBalaRecommendationsCard(analysis)
    }
}

@Composable
private fun BirthContextCard(context: KalaBalaCalculator.BirthContext) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = AppTheme.AccentPrimary.copy(alpha = 0.1f)),
        shape = RoundedCornerShape(16.dp)
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
                    imageVector = if (context.isDay) Icons.Outlined.LightMode else Icons.Outlined.DarkMode,
                    contentDescription = null,
                    tint = AppTheme.AccentPrimary,
                    modifier = Modifier.size(28.dp)
                )
            }
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = if (context.isDay) stringResource(StringKeyShadbala.KALA_DAY_BIRTH) else stringResource(StringKeyShadbala.KALA_NIGHT_BIRTH),
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = AppTheme.TextPrimary
                )
                Text(
                    text = "${context.pakshaType.displayName} • Tithi ${context.tithiNumber}",
                    style = MaterialTheme.typography.bodySmall,
                    color = AppTheme.TextSecondary
                )
                Text(
                    text = "Hora: ${context.horaLord.displayName} • Day: ${context.dayLord.displayName}",
                    style = MaterialTheme.typography.labelSmall,
                    color = AppTheme.TextMuted
                )
            }
        }
    }
}

@Composable
private fun KalaBalaScoreCard(analysis: KalaBalaCalculator.KalaBalaAnalysis) {
    val scoreColor = when {
        analysis.overallScore >= 70 -> AppTheme.SuccessColor
        analysis.overallScore >= 50 -> AppTheme.AccentGold
        analysis.overallScore >= 30 -> AppTheme.WarningColor
        else -> AppTheme.ErrorColor
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = AppTheme.CardBackground),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = stringResource(StringKeyShadbala.KALA_TOTAL),
                style = MaterialTheme.typography.labelMedium,
                color = AppTheme.TextMuted
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = String.format("%.0f", analysis.overallScore),
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
                progress = { (analysis.overallScore / 100f).toFloat() },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(8.dp)
                    .clip(RoundedCornerShape(4.dp)),
                color = scoreColor,
                trackColor = scoreColor.copy(alpha = 0.2f),
                strokeCap = StrokeCap.Round
            )
        }
    }
}

@Composable
private fun KalaBalaStrongestWeakestRow(analysis: KalaBalaCalculator.KalaBalaAnalysis) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Card(
            modifier = Modifier.weight(1f),
            colors = CardDefaults.cardColors(containerColor = AppTheme.SuccessColor.copy(alpha = 0.1f)),
            shape = RoundedCornerShape(12.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = stringResource(StringKeyShadbala.STHANA_STRONG),
                    style = MaterialTheme.typography.labelSmall,
                    color = AppTheme.SuccessColor
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = analysis.strongestPlanet.symbol,
                    fontSize = 24.sp,
                    color = AppTheme.SuccessColor
                )
                Text(
                    text = analysis.strongestPlanet.displayName,
                    style = MaterialTheme.typography.bodySmall,
                    fontWeight = FontWeight.Medium,
                    color = AppTheme.TextPrimary
                )
                val strongestBala = analysis.planetaryKalaBala[analysis.strongestPlanet]
                strongestBala?.let {
                    Text(
                        text = "${String.format("%.0f", it.percentageOfRequired)}%",
                        style = MaterialTheme.typography.labelSmall,
                        color = AppTheme.SuccessColor
                    )
                }
            }
        }

        Card(
            modifier = Modifier.weight(1f),
            colors = CardDefaults.cardColors(containerColor = AppTheme.ErrorColor.copy(alpha = 0.1f)),
            shape = RoundedCornerShape(12.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = stringResource(StringKeyShadbala.STHANA_WEAK),
                    style = MaterialTheme.typography.labelSmall,
                    color = AppTheme.ErrorColor
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = analysis.weakestPlanet.symbol,
                    fontSize = 24.sp,
                    color = AppTheme.ErrorColor
                )
                Text(
                    text = analysis.weakestPlanet.displayName,
                    style = MaterialTheme.typography.bodySmall,
                    fontWeight = FontWeight.Medium,
                    color = AppTheme.TextPrimary
                )
                val weakestBala = analysis.planetaryKalaBala[analysis.weakestPlanet]
                weakestBala?.let {
                    Text(
                        text = "${String.format("%.0f", it.percentageOfRequired)}%",
                        style = MaterialTheme.typography.labelSmall,
                        color = AppTheme.ErrorColor
                    )
                }
            }
        }
    }
}

@Composable
private fun KalaBalaInsightsCard(analysis: KalaBalaCalculator.KalaBalaAnalysis) {
    if (analysis.keyInsights.isEmpty()) return

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = AppTheme.CardBackground),
        shape = RoundedCornerShape(16.dp)
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
                    text = stringResource(StringKeyUIPart2.UI_KEY_INSIGHTS),
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
                    Text(text = "•", color = AppTheme.AccentGold, fontWeight = FontWeight.Bold)
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
private fun KalaBalaRecommendationsCard(analysis: KalaBalaCalculator.KalaBalaAnalysis) {
    if (analysis.recommendations.isEmpty()) return

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = AppTheme.AccentPrimary.copy(alpha = 0.08f)),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "Recommendations",
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.SemiBold,
                color = AppTheme.AccentPrimary
            )
            Spacer(modifier = Modifier.height(12.dp))
            analysis.recommendations.forEach { rec ->
                Row(
                    modifier = Modifier.padding(vertical = 4.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(text = "→", color = AppTheme.AccentPrimary, fontWeight = FontWeight.Bold)
                    Text(
                        text = rec,
                        style = MaterialTheme.typography.bodySmall,
                        color = AppTheme.TextSecondary
                    )
                }
            }
        }
    }
}

@Composable
private fun KalaBalaComponentsSection(analysis: KalaBalaCalculator.KalaBalaAnalysis) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(
            text = stringResource(StringKeyShadbala.KALA_TAB_COMPONENTS),
            style = MaterialTheme.typography.titleSmall,
            fontWeight = FontWeight.SemiBold,
            color = AppTheme.TextPrimary,
            modifier = Modifier.padding(bottom = 4.dp)
        )

        analysis.componentSummary.forEach { component ->
            KalaBalaComponentCard(component)
        }
    }
}

@Composable
private fun KalaBalaComponentCard(component: KalaBalaCalculator.ComponentSummary) {
    val scoreColor = when {
        component.percentage >= 70 -> AppTheme.SuccessColor
        component.percentage >= 50 -> AppTheme.AccentGold
        component.percentage >= 30 -> AppTheme.WarningColor
        else -> AppTheme.ErrorColor
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = AppTheme.CardBackground),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = component.name,
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Medium,
                        color = AppTheme.TextPrimary
                    )
                    Text(
                        text = component.description,
                        style = MaterialTheme.typography.labelSmall,
                        color = AppTheme.TextMuted
                    )
                }
                Column(horizontalAlignment = Alignment.End) {
                    Text(
                        text = String.format("%.1f", component.avgVirupas),
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = scoreColor
                    )
                    Text(
                        text = "/ ${String.format("%.0f", component.maxVirupas)}",
                        style = MaterialTheme.typography.labelSmall,
                        color = AppTheme.TextMuted
                    )
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
            LinearProgressIndicator(
                progress = { (component.percentage / 100f).toFloat().coerceIn(0f, 1f) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(6.dp)
                    .clip(RoundedCornerShape(3.dp)),
                color = scoreColor,
                trackColor = scoreColor.copy(alpha = 0.2f),
                strokeCap = StrokeCap.Round
            )
        }
    }
}

@Composable
private fun KalaBalaPlanetsSection(analysis: KalaBalaCalculator.KalaBalaAnalysis) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        val sortedPlanets = analysis.planetaryKalaBala.values.sortedByDescending { it.totalVirupas }

        sortedPlanets.forEach { planetBala ->
            PlanetKalaBalaCard(planetBala)
        }
    }
}

@Composable
private fun PlanetKalaBalaCard(planetBala: KalaBalaCalculator.PlanetKalaBala) {
    var expanded by remember { mutableStateOf(false) }

    val scoreColor = when {
        planetBala.percentageOfRequired >= 100 -> AppTheme.SuccessColor
        planetBala.percentageOfRequired >= 80 -> AppTheme.AccentGold
        planetBala.percentageOfRequired >= 60 -> AppTheme.WarningColor
        else -> AppTheme.ErrorColor
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .animateContentSize(),
        colors = CardDefaults.cardColors(containerColor = AppTheme.CardBackground),
        shape = RoundedCornerShape(12.dp)
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
                            .size(48.dp)
                            .clip(CircleShape)
                            .background(scoreColor.copy(alpha = 0.15f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = planetBala.planet.symbol,
                            fontSize = 22.sp,
                            color = scoreColor
                        )
                    }
                    Column {
                        Text(
                            text = planetBala.planet.displayName,
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.SemiBold,
                            color = AppTheme.TextPrimary
                        )
                        Text(
                            text = planetBala.strengthRating,
                            style = MaterialTheme.typography.labelSmall,
                            color = scoreColor
                        )
                    }
                }

                Column(horizontalAlignment = Alignment.End) {
                    Text(
                        text = "${String.format("%.0f", planetBala.percentageOfRequired)}%",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = scoreColor
                    )
                    Text(
                        text = "${String.format("%.1f", planetBala.totalRupas)} ${stringResource(StringKeyShadbala.COMMON_RUPAS)}",
                        style = MaterialTheme.typography.labelSmall,
                        color = AppTheme.TextMuted
                    )
                }
            }

            AnimatedVisibility(
                visible = expanded,
                enter = expandVertically() + fadeIn(),
                exit = shrinkVertically() + fadeOut()
            ) {
                Column(modifier = Modifier.padding(top = 12.dp)) {
                    HorizontalDivider(color = AppTheme.DividerColor)
                    Spacer(modifier = Modifier.height(12.dp))

                    KalaBalaBreakdownRow(stringResource(StringKeyShadbala.KALA_NATHONNATHA), planetBala.nathonnathaBala.virupas, 60.0)
                    KalaBalaBreakdownRow(stringResource(StringKeyShadbala.KALA_PAKSHA), planetBala.pakshaBala.virupas, 60.0)
                    KalaBalaBreakdownRow(stringResource(StringKeyShadbala.KALA_TRIBHAGA), planetBala.tribhagaBala.virupas, 60.0)
                    KalaBalaBreakdownRow(stringResource(StringKeyShadbala.KALA_HORA), planetBala.horaAdiBala.totalVirupas, 150.0)
                    KalaBalaBreakdownRow(stringResource(StringKeyShadbala.KALA_AYANA), planetBala.ayanaBala.virupas, 60.0)
                    KalaBalaBreakdownRow(stringResource(StringKeyShadbala.KALA_YUDDHA), planetBala.yuddhaBala.virupas + 30, 60.0)

                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = planetBala.interpretation,
                        style = MaterialTheme.typography.bodySmall,
                        color = AppTheme.TextSecondary
                    )
                }
            }

            if (!expanded) {
                Icon(
                    imageVector = Icons.Default.ExpandMore,
                    contentDescription = null,
                    tint = AppTheme.TextMuted,
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .padding(top = 4.dp)
                        .size(16.dp)
                )
            }
        }
    }
}

@Composable
private fun KalaBalaBreakdownRow(name: String, value: Double, max: Double) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = name,
            style = MaterialTheme.typography.bodySmall,
            color = AppTheme.TextSecondary
        )
        Text(
            text = "${String.format("%.1f", value)} / ${String.format("%.0f", max)}",
            style = MaterialTheme.typography.bodySmall,
            fontWeight = FontWeight.Medium,
            color = AppTheme.TextPrimary
        )
    }
}

@Composable
private fun KalaBalaLoadingContent(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            CircularProgressIndicator(color = AppTheme.AccentPrimary)
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = stringResource(StringKeyShadbala.KALA_ANALYZING),
                style = MaterialTheme.typography.bodyMedium,
                color = AppTheme.TextMuted
            )
        }
    }
}

@Composable
private fun KalaBalaEmptyContent(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(32.dp)
        ) {
            Icon(
                imageVector = Icons.Outlined.Schedule,
                contentDescription = null,
                tint = AppTheme.TextMuted,
                modifier = Modifier.size(64.dp)
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = stringResource(StringKeyShadbala.KALA_UNABLE),
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
                color = AppTheme.TextPrimary
            )
        }
    }
}

@Composable
private fun KalaBalaInfoDialog(onDismiss: () -> Unit) {
    androidx.compose.material3.AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = stringResource(StringKeyShadbala.KALA_INFO_TITLE),
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold
            )
        },
        text = {
            Column {
                Text(
                    text = stringResource(StringKeyShadbala.KALA_INFO_DESC),
                    style = MaterialTheme.typography.bodyMedium,
                    color = AppTheme.TextSecondary,
                    lineHeight = 22.sp
                )
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = stringResource(StringKeyShadbala.KALA_VEDIC_REF),
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

