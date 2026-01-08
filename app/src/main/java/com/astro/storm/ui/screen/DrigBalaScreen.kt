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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.Lightbulb
import androidx.compose.material.icons.outlined.RemoveRedEye
import androidx.compose.material.icons.outlined.Star
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.astro.storm.data.localization.LocalLanguage
import com.astro.storm.data.localization.StringKeyShadbala
import com.astro.storm.data.localization.stringResource
import com.astro.storm.data.model.Planet
import com.astro.storm.data.model.VedicChart
import com.astro.storm.ephemeris.DrigBalaCalculator
import com.astro.storm.ui.theme.AppTheme
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext

/**
 * Drig Bala Screen
 *
 * Displays comprehensive aspectual strength analysis showing:
 * - Overview with strongest/weakest planets and overall score
 * - Detailed aspect matrix
 * - Per-planet aspect analysis
 * - House aspect analysis
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DrigBalaScreen(
    chart: VedicChart?,
    onBack: () -> Unit
) {
    val language = LocalLanguage.current
    var selectedTab by rememberSaveable { mutableIntStateOf(0) }
    var isCalculating by remember { mutableStateOf(true) }
    var analysis by remember { mutableStateOf<DrigBalaCalculator.DrigBalaAnalysis?>(null) }
    var showInfoDialog by remember { mutableStateOf(false) }

    val tabs = listOf(
        stringResource(StringKeyShadbala.DRIG_TAB_OVERVIEW),
        stringResource(StringKeyShadbala.DRIG_TAB_ASPECTS),
        stringResource(StringKeyShadbala.DRIG_TAB_PLANETS),
        stringResource(StringKeyShadbala.DRIG_TAB_HOUSES)
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
                DrigBalaCalculator.analyzeDrigBala(chart)
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
                            text = stringResource(StringKeyShadbala.DRIG_TITLE),
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = AppTheme.TextPrimary
                        )
                        Text(
                            text = stringResource(StringKeyShadbala.DRIG_SUBTITLE),
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
            isCalculating -> DrigBalaLoadingContent(modifier = Modifier.padding(paddingValues))
            chart == null || analysis == null -> DrigBalaEmptyContent(modifier = Modifier.padding(paddingValues))
            else -> {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                        .background(AppTheme.ScreenBackground),
                    contentPadding = PaddingValues(bottom = 32.dp)
                ) {
                    item {
                        DrigBalaTabSelector(
                            tabs = tabs,
                            selectedTab = selectedTab,
                            onTabSelected = { selectedTab = it }
                        )
                    }
                    when (selectedTab) {
                        0 -> item { DrigBalaOverviewSection(analysis!!) }
                        1 -> item { DrigBalaAspectsSection(analysis!!) }
                        2 -> item { DrigBalaPlanetsSection(analysis!!) }
                        3 -> item { DrigBalaHousesSection(analysis!!) }
                    }
                }
            }
        }
    }

    if (showInfoDialog) {
        DrigBalaInfoDialog(onDismiss = { showInfoDialog = false })
    }
}

@Composable
private fun DrigBalaTabSelector(
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
private fun DrigBalaOverviewSection(analysis: DrigBalaCalculator.DrigBalaAnalysis) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Overall Score Card
        OverallScoreCard(analysis)
        // Strongest/Weakest Planets
        StrongestWeakestRow(analysis)
        // Key Insights
        DrigBalaInsightsCard(analysis)
        // Recommendations
        DrigBalaRecommendationsCard(analysis)
    }
}

@Composable
private fun OverallScoreCard(analysis: DrigBalaCalculator.DrigBalaAnalysis) {
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
                text = stringResource(StringKeyShadbala.DRIG_TOTAL_SCORE),
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
private fun StrongestWeakestRow(analysis: DrigBalaCalculator.DrigBalaAnalysis) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // Strongest Planet
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
                    text = stringResource(StringKeyShadbala.DRIG_POSITIVE),
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
                val strongestBala = analysis.planetaryDrigBala[analysis.strongestPlanet]
                strongestBala?.let {
                    Text(
                        text = "+${String.format("%.1f", it.netDrigBala)} ${stringResource(StringKeyShadbala.DRIG_VIRUPAS)}",
                        style = MaterialTheme.typography.labelSmall,
                        color = AppTheme.SuccessColor
                    )
                }
            }
        }

        // Weakest Planet
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
                    text = stringResource(StringKeyShadbala.DRIG_NEGATIVE),
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
                val weakestBala = analysis.planetaryDrigBala[analysis.weakestPlanet]
                weakestBala?.let {
                    Text(
                        text = "${String.format("%.1f", it.netDrigBala)} ${stringResource(StringKeyShadbala.DRIG_VIRUPAS)}",
                        style = MaterialTheme.typography.labelSmall,
                        color = AppTheme.ErrorColor
                    )
                }
            }
        }
    }
}

@Composable
private fun DrigBalaInsightsCard(analysis: DrigBalaCalculator.DrigBalaAnalysis) {
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
                    text = "Key Insights",
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
private fun DrigBalaRecommendationsCard(analysis: DrigBalaCalculator.DrigBalaAnalysis) {
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
                    Text(
                        text = "→",
                        color = AppTheme.AccentPrimary,
                        fontWeight = FontWeight.Bold
                    )
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
private fun DrigBalaAspectsSection(analysis: DrigBalaCalculator.DrigBalaAnalysis) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(
            text = stringResource(StringKeyShadbala.DRIG_TAB_ASPECTS),
            style = MaterialTheme.typography.titleSmall,
            fontWeight = FontWeight.SemiBold,
            color = AppTheme.TextPrimary,
            modifier = Modifier.padding(bottom = 4.dp)
        )

        // Group aspects by aspecting planet
        val groupedAspects = analysis.aspectMatrix.groupBy { it.aspectingPlanet }

        groupedAspects.forEach { (planet, aspects) ->
            AspectGroupCard(planet, aspects)
        }
    }
}

@Composable
private fun AspectGroupCard(planet: Planet, aspects: List<DrigBalaCalculator.AspectInfo>) {
    var expanded by remember { mutableStateOf(false) }

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
                            .size(40.dp)
                            .clip(CircleShape)
                            .background(AppTheme.AccentPrimary.copy(alpha = 0.15f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = planet.symbol,
                            fontSize = 18.sp,
                            color = AppTheme.AccentPrimary
                        )
                    }
                    Column {
                        Text(
                            text = planet.displayName,
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.Medium,
                            color = AppTheme.TextPrimary
                        )
                        Text(
                            text = "${aspects.size} aspects cast",
                            style = MaterialTheme.typography.labelSmall,
                            color = AppTheme.TextMuted
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
                    aspects.forEach { aspect ->
                        AspectDetailRow(aspect)
                    }
                }
            }
        }
    }
}

@Composable
private fun AspectDetailRow(aspect: DrigBalaCalculator.AspectInfo) {
    val effectColor = when (aspect.aspectEffect) {
        DrigBalaCalculator.AspectEffect.BENEFIC -> AppTheme.SuccessColor
        DrigBalaCalculator.AspectEffect.MALEFIC -> AppTheme.ErrorColor
        DrigBalaCalculator.AspectEffect.NEUTRAL -> AppTheme.TextMuted
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = aspect.aspectedPlanet.symbol,
                fontSize = 16.sp,
                color = AppTheme.TextPrimary
            )
            Column {
                Text(
                    text = aspect.aspectedPlanet.displayName,
                    style = MaterialTheme.typography.bodySmall,
                    color = AppTheme.TextPrimary
                )
                Text(
                    text = aspect.aspectType.displayName,
                    style = MaterialTheme.typography.labelSmall,
                    color = AppTheme.TextMuted
                )
            }
        }
        Column(horizontalAlignment = Alignment.End) {
            Text(
                text = "${if (aspect.aspectEffect == DrigBalaCalculator.AspectEffect.BENEFIC) "+" else "-"}${String.format("%.1f", aspect.virupas)}",
                style = MaterialTheme.typography.bodySmall,
                fontWeight = FontWeight.Medium,
                color = effectColor
            )
            if (aspect.isSpecialAspect) {
                Text(
                    text = stringResource(StringKeyShadbala.DRIG_SPECIAL_ASPECT),
                    style = MaterialTheme.typography.labelSmall,
                    color = AppTheme.AccentGold
                )
            }
        }
    }
}

@Composable
private fun DrigBalaPlanetsSection(analysis: DrigBalaCalculator.DrigBalaAnalysis) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        val sortedPlanets = analysis.planetaryDrigBala.values.sortedByDescending { it.netDrigBala }

        sortedPlanets.forEach { planetBala ->
            PlanetDrigBalaCard(planetBala)
        }
    }
}

@Composable
private fun PlanetDrigBalaCard(planetBala: DrigBalaCalculator.PlanetDrigBala) {
    var expanded by remember { mutableStateOf(false) }

    val netColor = when {
        planetBala.netDrigBala >= 15 -> AppTheme.SuccessColor
        planetBala.netDrigBala >= 0 -> AppTheme.AccentGold
        planetBala.netDrigBala >= -15 -> AppTheme.WarningColor
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
                            .background(netColor.copy(alpha = 0.15f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = planetBala.planet.symbol,
                            fontSize = 22.sp,
                            color = netColor
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
                            color = netColor
                        )
                    }
                }

                Column(horizontalAlignment = Alignment.End) {
                    Text(
                        text = "${if (planetBala.isPositive) "+" else ""}${String.format("%.1f", planetBala.netDrigBala)}",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = netColor
                    )
                    Text(
                        text = stringResource(StringKeyShadbala.DRIG_VIRUPAS),
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

                    // Benefic/Malefic breakdown
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                text = stringResource(StringKeyShadbala.DRIG_BENEFIC_ASPECTS),
                                style = MaterialTheme.typography.labelSmall,
                                color = AppTheme.TextMuted
                            )
                            Text(
                                text = "+${String.format("%.1f", planetBala.beneficVirupas)}",
                                style = MaterialTheme.typography.bodyMedium,
                                fontWeight = FontWeight.Medium,
                                color = AppTheme.SuccessColor
                            )
                        }
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                text = stringResource(StringKeyShadbala.DRIG_MALEFIC_ASPECTS),
                                style = MaterialTheme.typography.labelSmall,
                                color = AppTheme.TextMuted
                            )
                            Text(
                                text = "-${String.format("%.1f", planetBala.maleficVirupas)}",
                                style = MaterialTheme.typography.bodyMedium,
                                fontWeight = FontWeight.Medium,
                                color = AppTheme.ErrorColor
                            )
                        }
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                text = stringResource(StringKeyShadbala.DRIG_NET_STRENGTH),
                                style = MaterialTheme.typography.labelSmall,
                                color = AppTheme.TextMuted
                            )
                            Text(
                                text = String.format("%.1f", planetBala.netDrigBala),
                                style = MaterialTheme.typography.bodyMedium,
                                fontWeight = FontWeight.Medium,
                                color = netColor
                            )
                        }
                    }

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
private fun DrigBalaHousesSection(analysis: DrigBalaCalculator.DrigBalaAnalysis) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(
            text = stringResource(StringKeyShadbala.DRIG_TAB_HOUSES),
            style = MaterialTheme.typography.titleSmall,
            fontWeight = FontWeight.SemiBold,
            color = AppTheme.TextPrimary,
            modifier = Modifier.padding(bottom = 4.dp)
        )

        analysis.houseAspects.forEach { houseAspect ->
            HouseAspectCard(houseAspect)
        }
    }
}

@Composable
private fun HouseAspectCard(houseAspect: DrigBalaCalculator.HouseAspects) {
    val netColor = when (houseAspect.netEffect) {
        DrigBalaCalculator.AspectEffect.BENEFIC -> AppTheme.SuccessColor
        DrigBalaCalculator.AspectEffect.MALEFIC -> AppTheme.ErrorColor
        DrigBalaCalculator.AspectEffect.NEUTRAL -> AppTheme.TextMuted
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = AppTheme.CardBackground),
        shape = RoundedCornerShape(12.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
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
                        .background(netColor.copy(alpha = 0.15f)),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "${houseAspect.houseNumber}",
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold,
                        color = netColor
                    )
                }
                Column {
                    Text(
                        text = "House ${houseAspect.houseNumber}",
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Medium,
                        color = AppTheme.TextPrimary
                    )
                    Text(
                        text = houseAspect.netEffect.displayName,
                        style = MaterialTheme.typography.labelSmall,
                        color = netColor
                    )
                }
            }

            Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "${houseAspect.beneficCount}",
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Medium,
                        color = AppTheme.SuccessColor
                    )
                    Text(
                        text = stringResource(StringKeyShadbala.SARVATOBHADRA_BENEFIC),
                        style = MaterialTheme.typography.labelSmall,
                        color = AppTheme.TextMuted
                    )
                }
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "${houseAspect.maleficCount}",
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Medium,
                        color = AppTheme.ErrorColor
                    )
                    Text(
                        text = stringResource(StringKeyShadbala.SARVATOBHADRA_MALEFIC),
                        style = MaterialTheme.typography.labelSmall,
                        color = AppTheme.TextMuted
                    )
                }
            }
        }
    }
}

@Composable
private fun DrigBalaLoadingContent(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            CircularProgressIndicator(color = AppTheme.AccentPrimary)
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = stringResource(StringKeyShadbala.DRIG_ANALYZING),
                style = MaterialTheme.typography.bodyMedium,
                color = AppTheme.TextMuted
            )
        }
    }
}

@Composable
private fun DrigBalaEmptyContent(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(32.dp)
        ) {
            Icon(
                imageVector = Icons.Outlined.RemoveRedEye,
                contentDescription = null,
                tint = AppTheme.TextMuted,
                modifier = Modifier.size(64.dp)
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = stringResource(StringKeyShadbala.DRIG_UNABLE),
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
                color = AppTheme.TextPrimary
            )
        }
    }
}

@Composable
private fun DrigBalaInfoDialog(onDismiss: () -> Unit) {
    androidx.compose.material3.AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = stringResource(StringKeyShadbala.DRIG_INFO_TITLE),
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold
            )
        },
        text = {
            Column {
                Text(
                    text = stringResource(StringKeyShadbala.DRIG_INFO_DESC),
                    style = MaterialTheme.typography.bodyMedium,
                    color = AppTheme.TextSecondary,
                    lineHeight = 22.sp
                )
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = stringResource(StringKeyShadbala.DRIG_VEDIC_REF),
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
