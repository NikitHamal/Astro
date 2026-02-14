package com.astro.storm.ui.screen

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.BorderStroke
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.Lightbulb
import androidx.compose.material.icons.outlined.RemoveRedEye
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import com.astro.storm.ui.components.ScreenTopBar
import com.astro.storm.ui.components.common.ModernPillTabRow
import com.astro.storm.ui.components.common.NeoVedicEmptyState
import com.astro.storm.ui.components.common.TabItem
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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.astro.storm.data.localization.LocalLanguage
import com.astro.storm.core.common.StringKeyShadbala
import com.astro.storm.core.common.StringKeyAnalysis
import com.astro.storm.core.common.StringKeyUICommon
import com.astro.storm.core.common.Language
import com.astro.storm.core.common.StringKeyUIExtra
import com.astro.storm.data.localization.localizedAbbr
import com.astro.storm.data.localization.stringResource
import com.astro.storm.core.model.Planet
import com.astro.storm.core.model.VedicChart
import com.astro.storm.ephemeris.DrigBalaCalculator
import com.astro.storm.ui.theme.AppTheme
import com.astro.storm.ui.theme.NeoVedicTokens
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
            ScreenTopBar(
                title = stringResource(StringKeyShadbala.DRIG_TITLE),
                subtitle = stringResource(StringKeyShadbala.DRIG_SUBTITLE),
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
            isCalculating -> DrigBalaLoadingContent(modifier = Modifier.padding(paddingValues))
            chart == null || analysis == null -> NeoVedicEmptyState(
                title = stringResource(StringKeyShadbala.DRIG_TITLE),
                subtitle = stringResource(StringKeyShadbala.DRIG_UNABLE),
                icon = Icons.Outlined.RemoveRedEye,
                modifier = Modifier.padding(paddingValues)
            )
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
    val tabItems = tabs.mapIndexed { index, title ->
        TabItem(
            title = title,
            accentColor = when (index) {
                0 -> AppTheme.AccentGold
                1 -> AppTheme.AccentTeal
                2 -> AppTheme.AccentPrimary
                else -> AppTheme.AccentPrimary
            }
        )
    }
    ModernPillTabRow(
        tabs = tabItems,
        selectedIndex = selectedTab,
        onTabSelected = onTabSelected,
        modifier = Modifier.padding(horizontal = NeoVedicTokens.ScreenPadding, vertical = NeoVedicTokens.SpaceXS)
    )
}

@Composable
private fun DrigBalaOverviewSection(analysis: DrigBalaCalculator.DrigBalaAnalysis) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = NeoVedicTokens.ScreenPadding),
        verticalArrangement = Arrangement.spacedBy(NeoVedicTokens.SectionSpacing)
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

    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = AppTheme.CardBackground,
        shape = RoundedCornerShape(NeoVedicTokens.CardCornerRadius),
        border = BorderStroke(NeoVedicTokens.BorderWidth, AppTheme.BorderColor)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(NeoVedicTokens.ScreenPadding),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = stringResource(StringKeyShadbala.DRIG_TOTAL_SCORE),
                style = MaterialTheme.typography.labelMedium,
                color = AppTheme.TextMuted
            )
            Spacer(modifier = Modifier.height(NeoVedicTokens.SpaceXS))
            Text(
                text = String.format("%.0f", analysis.overallScore),
                style = MaterialTheme.typography.displaySmall,
                fontWeight = FontWeight.Bold,
                color = scoreColor
            )
            Text(
                text = stringResource(StringKeyUIExtra.SLASH) + "100",
                style = MaterialTheme.typography.bodySmall,
                color = AppTheme.TextMuted
            )
            Spacer(modifier = Modifier.height(NeoVedicTokens.CardSpacing))
            LinearProgressIndicator(
                progress = { (analysis.overallScore / 100f).toFloat() },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(8.dp)
                    .clip(RoundedCornerShape(NeoVedicTokens.CardCornerRadius)),
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
        horizontalArrangement = Arrangement.spacedBy(NeoVedicTokens.CardSpacing)
    ) {
        // Strongest Planet
        Surface(
            modifier = Modifier.weight(1f),
            color = AppTheme.SuccessColor.copy(alpha = 0.06f),
            shape = RoundedCornerShape(NeoVedicTokens.CardCornerRadius),
            border = BorderStroke(NeoVedicTokens.ThinBorderWidth, AppTheme.SuccessColor.copy(alpha = 0.2f))
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(NeoVedicTokens.CardSpacing),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = stringResource(StringKeyShadbala.DRIG_POSITIVE),
                    style = MaterialTheme.typography.labelSmall,
                    color = AppTheme.SuccessColor
                )
                Spacer(modifier = Modifier.height(NeoVedicTokens.SpaceXXS))
                Text(
                    text = analysis.strongestPlanet.localizedAbbr(),
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
        Surface(
            modifier = Modifier.weight(1f),
            color = AppTheme.ErrorColor.copy(alpha = 0.06f),
            shape = RoundedCornerShape(NeoVedicTokens.CardCornerRadius),
            border = BorderStroke(NeoVedicTokens.ThinBorderWidth, AppTheme.ErrorColor.copy(alpha = 0.2f))
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(NeoVedicTokens.CardSpacing),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = stringResource(StringKeyShadbala.DRIG_NEGATIVE),
                    style = MaterialTheme.typography.labelSmall,
                    color = AppTheme.ErrorColor
                )
                Spacer(modifier = Modifier.height(NeoVedicTokens.SpaceXXS))
                Text(
                    text = analysis.weakestPlanet.localizedAbbr(),
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

    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = AppTheme.CardBackground,
        shape = RoundedCornerShape(NeoVedicTokens.CardCornerRadius),
        border = BorderStroke(NeoVedicTokens.BorderWidth, AppTheme.BorderColor)
    ) {
        Column(modifier = Modifier.padding(NeoVedicTokens.ScreenPadding)) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(NeoVedicTokens.SpaceXS)
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
            Spacer(modifier = Modifier.height(NeoVedicTokens.CardSpacing))
            analysis.keyInsights.forEach { insight ->
                Row(
                    modifier = Modifier.padding(vertical = NeoVedicTokens.SpaceXXS),
                    horizontalArrangement = Arrangement.spacedBy(NeoVedicTokens.SpaceXS)
                ) {
                    Text(
                        text = stringResource(StringKeyUICommon.BULLET),
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

    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = AppTheme.AccentPrimary.copy(alpha = 0.06f),
        shape = RoundedCornerShape(NeoVedicTokens.CardCornerRadius),
        border = BorderStroke(NeoVedicTokens.ThinBorderWidth, AppTheme.AccentPrimary.copy(alpha = 0.2f))
    ) {
        Column(modifier = Modifier.padding(NeoVedicTokens.ScreenPadding)) {
            Text(
                text = stringResource(StringKeyUICommon.REMEDIES),
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.SemiBold,
                color = AppTheme.AccentPrimary
            )
            Spacer(modifier = Modifier.height(NeoVedicTokens.CardSpacing))
            analysis.recommendations.forEach { rec ->
                Row(
                    modifier = Modifier.padding(vertical = NeoVedicTokens.SpaceXXS),
                    horizontalArrangement = Arrangement.spacedBy(NeoVedicTokens.SpaceXS)
                ) {
                    Text(
                        text = stringResource(StringKeyUIExtra.ARROW),
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
            .padding(horizontal = NeoVedicTokens.ScreenPadding),
        verticalArrangement = Arrangement.spacedBy(NeoVedicTokens.CardSpacing)
    ) {
        Text(
            text = stringResource(StringKeyShadbala.DRIG_TAB_ASPECTS),
            style = MaterialTheme.typography.titleSmall,
            fontWeight = FontWeight.SemiBold,
            color = AppTheme.TextPrimary,
            modifier = Modifier.padding(bottom = NeoVedicTokens.SpaceXXS)
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
    val language = LocalLanguage.current
    var expanded by remember { mutableStateOf(false) }

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .animateContentSize(),
        color = AppTheme.CardBackground,
        shape = RoundedCornerShape(NeoVedicTokens.CardCornerRadius),
        border = BorderStroke(NeoVedicTokens.BorderWidth, AppTheme.BorderColor)
    ) {
        Column(modifier = Modifier.padding(NeoVedicTokens.CardSpacing)) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { expanded = !expanded },
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(NeoVedicTokens.CardSpacing)
                ) {
                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .clip(CircleShape)
                            .background(AppTheme.AccentPrimary.copy(alpha = 0.15f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = planet.localizedAbbr(),
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
                        val aspectsCount = if (language == Language.NEPALI) com.astro.storm.core.common.BikramSambatConverter.toNepaliNumerals(aspects.size) else aspects.size.toString()
                        Text(
                            text = stringResource(StringKeyAnalysis.TRANSIT_ASPECTS_TO_NATAL) + " " + stringResource(StringKeyUIExtra.PAREN_START) + aspectsCount + stringResource(StringKeyUIExtra.PAREN_END),
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
                Column(modifier = Modifier.padding(top = NeoVedicTokens.CardSpacing)) {
                    HorizontalDivider(color = AppTheme.DividerColor)
                    Spacer(modifier = Modifier.height(NeoVedicTokens.CardSpacing))
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
            .padding(vertical = NeoVedicTokens.PillVerticalPadding),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(NeoVedicTokens.SpaceXS)
        ) {
            Text(
                text = aspect.aspectedPlanet.localizedAbbr(),
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
            .padding(horizontal = NeoVedicTokens.ScreenPadding),
        verticalArrangement = Arrangement.spacedBy(NeoVedicTokens.CardSpacing)
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

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .animateContentSize(),
        color = AppTheme.CardBackground,
        shape = RoundedCornerShape(NeoVedicTokens.CardCornerRadius),
        border = BorderStroke(NeoVedicTokens.BorderWidth, AppTheme.BorderColor)
    ) {
        Column(modifier = Modifier.padding(NeoVedicTokens.CardSpacing)) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { expanded = !expanded },
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(NeoVedicTokens.CardSpacing)
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
                Column(modifier = Modifier.padding(top = NeoVedicTokens.CardSpacing)) {
                    HorizontalDivider(color = AppTheme.DividerColor)
                    Spacer(modifier = Modifier.height(NeoVedicTokens.CardSpacing))

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

                    Spacer(modifier = Modifier.height(NeoVedicTokens.CardSpacing))
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
                        .padding(top = NeoVedicTokens.SpaceXXS)
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
            .padding(horizontal = NeoVedicTokens.ScreenPadding),
        verticalArrangement = Arrangement.spacedBy(NeoVedicTokens.CardSpacing)
    ) {
        Text(
            text = stringResource(StringKeyShadbala.DRIG_TAB_HOUSES),
            style = MaterialTheme.typography.titleSmall,
            fontWeight = FontWeight.SemiBold,
            color = AppTheme.TextPrimary,
            modifier = Modifier.padding(bottom = NeoVedicTokens.SpaceXXS)
        )

        analysis.houseAspects.forEach { houseAspect ->
            HouseAspectCard(houseAspect)
        }
    }
}

@Composable
private fun HouseAspectCard(houseAspect: DrigBalaCalculator.HouseAspects) {
    val language = LocalLanguage.current
    val netColor = when (houseAspect.netEffect) {
        DrigBalaCalculator.AspectEffect.BENEFIC -> AppTheme.SuccessColor
        DrigBalaCalculator.AspectEffect.MALEFIC -> AppTheme.ErrorColor
        DrigBalaCalculator.AspectEffect.NEUTRAL -> AppTheme.TextMuted
    }

    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = AppTheme.CardBackground,
        shape = RoundedCornerShape(NeoVedicTokens.CardCornerRadius),
        border = BorderStroke(NeoVedicTokens.BorderWidth, AppTheme.BorderColor)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(NeoVedicTokens.CardSpacing),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(NeoVedicTokens.CardSpacing)
            ) {
                val houseNum = if (language == Language.NEPALI) com.astro.storm.core.common.BikramSambatConverter.toNepaliNumerals(houseAspect.houseNumber) else houseAspect.houseNumber.toString()
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                        .background(netColor.copy(alpha = 0.15f)),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = houseNum,
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold,
                        color = netColor
                    )
                }
                Column {
                    Text(
                        text = stringResource(StringKeyAnalysis.TRANSIT_HOUSE_LABEL) + " $houseNum",
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

            Row(horizontalArrangement = Arrangement.spacedBy(NeoVedicTokens.ScreenPadding)) {
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
            Spacer(modifier = Modifier.height(NeoVedicTokens.ScreenPadding))
            Text(
                text = stringResource(StringKeyShadbala.DRIG_ANALYZING),
                style = MaterialTheme.typography.bodyMedium,
                color = AppTheme.TextMuted
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
                fontWeight = FontWeight.SemiBold,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
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
                Spacer(modifier = Modifier.height(NeoVedicTokens.CardSpacing))
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
