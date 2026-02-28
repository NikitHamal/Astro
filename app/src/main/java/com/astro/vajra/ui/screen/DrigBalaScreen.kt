package com.astro.vajra.ui.screen

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
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import com.astro.vajra.ui.components.common.NeoVedicPageHeader
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
import com.astro.vajra.data.localization.LocalLanguage
import com.astro.vajra.core.common.StringKeyShadbala
import com.astro.vajra.core.common.StringKey
import com.astro.vajra.core.common.StringKeyAnalysis
import com.astro.vajra.core.common.StringKeyUICommon
import com.astro.vajra.core.common.Language
import com.astro.vajra.core.common.StringKeyUIExtra
import com.astro.vajra.data.localization.localizedAbbr
import com.astro.vajra.data.localization.stringResource
import com.astro.vajra.core.model.Planet
import com.astro.vajra.core.model.VedicChart
import com.astro.vajra.ephemeris.DrigBalaCalculator
import com.astro.vajra.ui.theme.AppTheme
import com.astro.vajra.ui.theme.NeoVedicTokens
import com.astro.vajra.ui.theme.SpaceGroteskFamily
import com.astro.vajra.ui.theme.CinzelDecorativeFamily
import com.astro.vajra.ui.theme.PoppinsFontFamily
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import androidx.compose.foundation.BorderStroke

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
            NeoVedicPageHeader(
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
            com.astro.vajra.ui.components.common.NeoVedicChoicePill(
                selected = selectedTab == index,
                onClick = { onTabSelected(index) },
                label = {
                    Text(
                        text = tabs[index],
                        fontFamily = SpaceGroteskFamily,
                        fontSize = com.astro.vajra.ui.theme.NeoVedicFontSizes.S13,
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

    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = AppTheme.CardBackground,
        shape = RoundedCornerShape(NeoVedicTokens.CardCornerRadius),
        border = BorderStroke(NeoVedicTokens.BorderWidth, AppTheme.BorderColor)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = stringResource(StringKeyShadbala.DRIG_TOTAL_SCORE).uppercase(),
                fontFamily = SpaceGroteskFamily,
                fontSize = com.astro.vajra.ui.theme.NeoVedicFontSizes.S12,
                letterSpacing = 1.sp,
                color = AppTheme.TextMuted
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = String.format("%.0f", analysis.overallScore),
                fontFamily = SpaceGroteskFamily,
                fontSize = 32.sp, // S32 fixed
                fontWeight = FontWeight.Bold,
                color = scoreColor
            )
            Text(
                text = stringResource(StringKeyUIExtra.SLASH) + "100",
                fontFamily = PoppinsFontFamily,
                fontSize = com.astro.vajra.ui.theme.NeoVedicFontSizes.S12,
                color = AppTheme.TextMuted
            )
            Spacer(modifier = Modifier.height(12.dp))
            LinearProgressIndicator(
                progress = { (analysis.overallScore / 100f).toFloat() },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(8.dp)
                    .clip(RoundedCornerShape(com.astro.vajra.ui.theme.NeoVedicTokens.ElementCornerRadius)),
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
        Surface(
            modifier = Modifier.weight(1f),
            color = AppTheme.SuccessColor.copy(alpha = 0.06f),
            shape = RoundedCornerShape(NeoVedicTokens.CardCornerRadius),
            border = BorderStroke(NeoVedicTokens.ThinBorderWidth, AppTheme.SuccessColor.copy(alpha = 0.2f))
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = stringResource(StringKeyShadbala.DRIG_POSITIVE).uppercase(),
                    fontFamily = SpaceGroteskFamily,
                    fontSize = com.astro.vajra.ui.theme.NeoVedicFontSizes.S10,
                    letterSpacing = 1.sp,
                    color = AppTheme.SuccessColor
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = analysis.strongestPlanet.localizedAbbr(),
                    fontFamily = CinzelDecorativeFamily,
                    fontSize = com.astro.vajra.ui.theme.NeoVedicFontSizes.S24,
                    color = AppTheme.SuccessColor
                )
                Text(
                    text = analysis.strongestPlanet.displayName,
                    fontFamily = PoppinsFontFamily,
                    fontSize = com.astro.vajra.ui.theme.NeoVedicFontSizes.S12,
                    fontWeight = FontWeight.Medium,
                    color = AppTheme.TextPrimary
                )
                val strongestBala = analysis.planetaryDrigBala[analysis.strongestPlanet]
                strongestBala?.let {
                    Text(
                        text = "+${String.format("%.1f", it.netDrigBala)} ${stringResource(StringKeyShadbala.DRIG_VIRUPAS)}",
                        fontFamily = SpaceGroteskFamily,
                        fontSize = com.astro.vajra.ui.theme.NeoVedicFontSizes.S10,
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
                    .padding(12.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = stringResource(StringKeyShadbala.DRIG_NEGATIVE).uppercase(),
                    fontFamily = SpaceGroteskFamily,
                    fontSize = com.astro.vajra.ui.theme.NeoVedicFontSizes.S10,
                    letterSpacing = 1.sp,
                    color = AppTheme.ErrorColor
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = analysis.weakestPlanet.localizedAbbr(),
                    fontFamily = CinzelDecorativeFamily,
                    fontSize = com.astro.vajra.ui.theme.NeoVedicFontSizes.S24,
                    color = AppTheme.ErrorColor
                )
                Text(
                    text = analysis.weakestPlanet.displayName,
                    fontFamily = PoppinsFontFamily,
                    fontSize = com.astro.vajra.ui.theme.NeoVedicFontSizes.S12,
                    fontWeight = FontWeight.Medium,
                    color = AppTheme.TextPrimary
                )
                val weakestBala = analysis.planetaryDrigBala[analysis.weakestPlanet]
                weakestBala?.let {
                    Text(
                        text = "${String.format("%.1f", it.netDrigBala)} ${stringResource(StringKeyShadbala.DRIG_VIRUPAS)}",
                        fontFamily = SpaceGroteskFamily,
                        fontSize = com.astro.vajra.ui.theme.NeoVedicFontSizes.S10,
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
                    fontFamily = CinzelDecorativeFamily,
                    fontSize = com.astro.vajra.ui.theme.NeoVedicFontSizes.S16,
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
                        text = stringResource(StringKeyUICommon.BULLET),
                        color = AppTheme.AccentGold,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = insight,
                        fontFamily = PoppinsFontFamily,
                        fontSize = com.astro.vajra.ui.theme.NeoVedicFontSizes.S12,
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
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = stringResource(StringKeyUICommon.REMEDIES),
                fontFamily = CinzelDecorativeFamily,
                fontSize = com.astro.vajra.ui.theme.NeoVedicFontSizes.S16,
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
                        text = stringResource(StringKeyUIExtra.ARROW),
                        color = AppTheme.AccentPrimary,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = rec,
                        fontFamily = PoppinsFontFamily,
                        fontSize = com.astro.vajra.ui.theme.NeoVedicFontSizes.S12,
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
            text = stringResource(StringKeyShadbala.DRIG_TAB_ASPECTS).uppercase(),
            fontFamily = SpaceGroteskFamily,
            fontSize = com.astro.vajra.ui.theme.NeoVedicFontSizes.S12,
            letterSpacing = 2.sp,
            fontWeight = FontWeight.SemiBold,
            color = AppTheme.TextMuted,
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
                            text = planet.localizedAbbr(),
                            fontFamily = CinzelDecorativeFamily,
                            fontSize = com.astro.vajra.ui.theme.NeoVedicFontSizes.S18,
                            color = AppTheme.AccentPrimary
                        )
                    }
                    Column {
                        Text(
                            text = planet.displayName,
                            fontFamily = PoppinsFontFamily,
                            fontSize = com.astro.vajra.ui.theme.NeoVedicFontSizes.S14,
                            fontWeight = FontWeight.Medium,
                            color = AppTheme.TextPrimary
                        )
                        val aspectsCount = if (language == Language.NEPALI) com.astro.vajra.core.common.BikramSambatConverter.toNepaliNumerals(aspects.size) else aspects.size.toString()
                        Text(
                            text = stringResource(StringKeyAnalysis.TRANSIT_ASPECTS_TO_NATAL) + " " + stringResource(StringKeyUIExtra.PAREN_START) + aspectsCount + stringResource(StringKeyUIExtra.PAREN_END),
                            fontFamily = SpaceGroteskFamily,
                            fontSize = com.astro.vajra.ui.theme.NeoVedicFontSizes.S10,
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
                text = aspect.aspectedPlanet.localizedAbbr(),
                fontFamily = CinzelDecorativeFamily,
                fontSize = com.astro.vajra.ui.theme.NeoVedicFontSizes.S16,
                color = AppTheme.TextPrimary
            )
            Column {
                Text(
                    text = aspect.aspectedPlanet.displayName,
                    fontFamily = PoppinsFontFamily,
                    fontSize = com.astro.vajra.ui.theme.NeoVedicFontSizes.S12,
                    color = AppTheme.TextPrimary
                )
                Text(
                    text = aspect.aspectType.displayName,
                    fontFamily = PoppinsFontFamily,
                    fontSize = com.astro.vajra.ui.theme.NeoVedicFontSizes.S10,
                    color = AppTheme.TextMuted
                )
            }
        }
        Column(horizontalAlignment = Alignment.End) {
            Text(
                text = "${if (aspect.aspectEffect == DrigBalaCalculator.AspectEffect.BENEFIC) "+" else "-"}${String.format("%.1f", aspect.virupas)}",
                fontFamily = SpaceGroteskFamily,
                fontSize = com.astro.vajra.ui.theme.NeoVedicFontSizes.S12,
                fontWeight = FontWeight.Medium,
                color = effectColor
            )
            if (aspect.isSpecialAspect) {
                Text(
                    text = stringResource(StringKeyShadbala.DRIG_SPECIAL_ASPECT).uppercase(),
                    fontFamily = SpaceGroteskFamily,
                    fontSize = com.astro.vajra.ui.theme.NeoVedicFontSizes.S10,
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

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .animateContentSize(),
        color = AppTheme.CardBackground,
        shape = RoundedCornerShape(NeoVedicTokens.CardCornerRadius),
        border = BorderStroke(NeoVedicTokens.BorderWidth, AppTheme.BorderColor)
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
                            fontFamily = CinzelDecorativeFamily,
                            fontSize = com.astro.vajra.ui.theme.NeoVedicFontSizes.S22,
                            color = netColor
                        )
                    }
                    Column {
                        Text(
                            text = planetBala.planet.displayName,
                            fontFamily = CinzelDecorativeFamily,
                            fontSize = com.astro.vajra.ui.theme.NeoVedicFontSizes.S16,
                            fontWeight = FontWeight.SemiBold,
                            color = AppTheme.TextPrimary
                        )
                        Text(
                            text = planetBala.strengthRating,
                            fontFamily = PoppinsFontFamily,
                            fontSize = com.astro.vajra.ui.theme.NeoVedicFontSizes.S10,
                            color = netColor
                        )
                    }
                }

                Column(horizontalAlignment = Alignment.End) {
                    Text(
                        text = "${if (planetBala.isPositive) "+" else ""}${String.format("%.1f", planetBala.netDrigBala)}",
                        fontFamily = SpaceGroteskFamily,
                        fontSize = com.astro.vajra.ui.theme.NeoVedicFontSizes.S16,
                        fontWeight = FontWeight.Bold,
                        color = netColor
                    )
                    Text(
                        text = stringResource(StringKeyShadbala.DRIG_VIRUPAS),
                        fontFamily = SpaceGroteskFamily,
                        fontSize = com.astro.vajra.ui.theme.NeoVedicFontSizes.S10,
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
                                text = stringResource(StringKeyShadbala.DRIG_BENEFIC_ASPECTS).uppercase(),
                                fontFamily = SpaceGroteskFamily,
                                fontSize = com.astro.vajra.ui.theme.NeoVedicFontSizes.S10,
                                color = AppTheme.TextMuted
                            )
                            Text(
                                text = "+${String.format("%.1f", planetBala.beneficVirupas)}",
                                fontFamily = SpaceGroteskFamily,
                                fontSize = com.astro.vajra.ui.theme.NeoVedicFontSizes.S14,
                                fontWeight = FontWeight.Medium,
                                color = AppTheme.SuccessColor
                            )
                        }
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                text = stringResource(StringKeyShadbala.DRIG_MALEFIC_ASPECTS).uppercase(),
                                fontFamily = SpaceGroteskFamily,
                                fontSize = com.astro.vajra.ui.theme.NeoVedicFontSizes.S10,
                                color = AppTheme.TextMuted
                            )
                            Text(
                                text = "-${String.format("%.1f", planetBala.maleficVirupas)}",
                                fontFamily = SpaceGroteskFamily,
                                fontSize = com.astro.vajra.ui.theme.NeoVedicFontSizes.S14,
                                fontWeight = FontWeight.Medium,
                                color = AppTheme.ErrorColor
                            )
                        }
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                text = stringResource(StringKeyShadbala.DRIG_NET_STRENGTH).uppercase(),
                                fontFamily = SpaceGroteskFamily,
                                fontSize = com.astro.vajra.ui.theme.NeoVedicFontSizes.S10,
                                color = AppTheme.TextMuted
                            )
                            Text(
                                text = String.format("%.1f", planetBala.netDrigBala),
                                fontFamily = SpaceGroteskFamily,
                                fontSize = com.astro.vajra.ui.theme.NeoVedicFontSizes.S14,
                                fontWeight = FontWeight.Medium,
                                color = netColor
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = planetBala.interpretation,
                        fontFamily = PoppinsFontFamily,
                        fontSize = com.astro.vajra.ui.theme.NeoVedicFontSizes.S12,
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
            text = stringResource(StringKeyShadbala.DRIG_TAB_HOUSES).uppercase(),
            fontFamily = SpaceGroteskFamily,
            fontSize = com.astro.vajra.ui.theme.NeoVedicFontSizes.S12,
            letterSpacing = 2.sp,
            fontWeight = FontWeight.SemiBold,
            color = AppTheme.TextMuted,
            modifier = Modifier.padding(bottom = 4.dp)
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
                .padding(12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                val houseNum = if (language == Language.NEPALI) com.astro.vajra.core.common.BikramSambatConverter.toNepaliNumerals(houseAspect.houseNumber) else houseAspect.houseNumber.toString()
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                        .background(netColor.copy(alpha = 0.15f)),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = houseNum,
                        fontFamily = CinzelDecorativeFamily,
                        fontSize = com.astro.vajra.ui.theme.NeoVedicFontSizes.S16,
                        fontWeight = FontWeight.Bold,
                        color = netColor
                    )
                }
                Column {
                    Text(
                        text = stringResource(StringKeyAnalysis.TRANSIT_HOUSE_LABEL) + " $houseNum",
                        fontFamily = PoppinsFontFamily,
                        fontSize = com.astro.vajra.ui.theme.NeoVedicFontSizes.S14,
                        fontWeight = FontWeight.Medium,
                        color = AppTheme.TextPrimary
                    )
                    Text(
                        text = houseAspect.netEffect.displayName,
                        fontFamily = SpaceGroteskFamily,
                        fontSize = com.astro.vajra.ui.theme.NeoVedicFontSizes.S10,
                        color = netColor
                    )
                }
            }

            Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "${houseAspect.beneficCount}",
                        fontFamily = SpaceGroteskFamily,
                        fontSize = com.astro.vajra.ui.theme.NeoVedicFontSizes.S14,
                        fontWeight = FontWeight.Medium,
                        color = AppTheme.SuccessColor
                    )
                    Text(
                        text = stringResource(StringKeyShadbala.SARVATOBHADRA_BENEFIC).uppercase(),
                        fontFamily = SpaceGroteskFamily,
                        fontSize = com.astro.vajra.ui.theme.NeoVedicFontSizes.S10,
                        color = AppTheme.TextMuted
                    )
                }
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "${houseAspect.maleficCount}",
                        fontFamily = SpaceGroteskFamily,
                        fontSize = com.astro.vajra.ui.theme.NeoVedicFontSizes.S14,
                        fontWeight = FontWeight.Medium,
                        color = AppTheme.ErrorColor
                    )
                    Text(
                        text = stringResource(StringKeyShadbala.SARVATOBHADRA_MALEFIC).uppercase(),
                        fontFamily = SpaceGroteskFamily,
                        fontSize = com.astro.vajra.ui.theme.NeoVedicFontSizes.S10,
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
                fontFamily = PoppinsFontFamily,
                fontSize = com.astro.vajra.ui.theme.NeoVedicFontSizes.S14,
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
                fontFamily = CinzelDecorativeFamily,
                fontSize = com.astro.vajra.ui.theme.NeoVedicFontSizes.S16,
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
                fontFamily = CinzelDecorativeFamily,
                fontSize = com.astro.vajra.ui.theme.NeoVedicFontSizes.S18,
                fontWeight = FontWeight.SemiBold,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
        },
        text = {
            Column {
                Text(
                    text = stringResource(StringKeyShadbala.DRIG_INFO_DESC),
                    fontFamily = PoppinsFontFamily,
                    fontSize = com.astro.vajra.ui.theme.NeoVedicFontSizes.S14,
                    color = AppTheme.TextSecondary,
                    lineHeight = 22.sp
                )
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = stringResource(StringKeyShadbala.DRIG_VEDIC_REF),
                    fontFamily = SpaceGroteskFamily,
                    fontSize = com.astro.vajra.ui.theme.NeoVedicFontSizes.S10,
                    color = AppTheme.TextMuted,
                    fontStyle = androidx.compose.ui.text.font.FontStyle.Italic
                )
            }
        },
        confirmButton = {
            androidx.compose.material3.TextButton(onClick = onDismiss) {
                Text(stringResource(StringKeyShadbala.COMMON_GOT_IT), color = AppTheme.AccentPrimary, fontFamily = SpaceGroteskFamily)
            }
        },
        containerColor = AppTheme.CardBackground
    )
}
