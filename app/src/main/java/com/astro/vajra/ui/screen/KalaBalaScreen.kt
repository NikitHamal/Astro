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
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.astro.vajra.core.common.Language
import com.astro.vajra.core.common.StringKey
import com.astro.vajra.core.common.StringKeyAnalysis
import com.astro.vajra.data.localization.LocalLanguage
import com.astro.vajra.core.common.StringKeyShadbala
import com.astro.vajra.core.common.StringKeyUICommon
import com.astro.vajra.core.common.StringKeyUIExtra
import com.astro.vajra.data.localization.stringResource
import com.astro.vajra.core.model.VedicChart
import com.astro.vajra.ephemeris.KalaBalaCalculator
import com.astro.vajra.ui.theme.AppTheme
import com.astro.vajra.ui.theme.NeoVedicTokens
import com.astro.vajra.ui.theme.SpaceGroteskFamily
import com.astro.vajra.ui.theme.CinzelDecorativeFamily
import com.astro.vajra.ui.theme.PoppinsFontFamily
import com.astro.vajra.ui.components.common.ModernPillTabRow
import com.astro.vajra.ui.components.common.TabItem
import com.astro.vajra.ui.components.common.NeoVedicEmptyState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.foundation.BorderStroke
import androidx.compose.material3.Surface

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
            NeoVedicPageHeader(
                title = stringResource(StringKeyShadbala.KALA_TITLE),
                subtitle = stringResource(StringKeyShadbala.KALA_SUBTITLE),
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

/**
 * Neo-Vedic styled tab selector using ModernPillTabRow
 */
@Composable
private fun KalaBalaTabSelector(
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

/**
 * Overview section with Neo-Vedic styling
 */
@Composable
private fun KalaBalaOverviewSection(analysis: KalaBalaCalculator.KalaBalaAnalysis) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = NeoVedicTokens.ScreenPadding),
        verticalArrangement = Arrangement.spacedBy(NeoVedicTokens.SectionSpacing)
    ) {
        BirthContextCard(analysis.birthContext)
        KalaBalaScoreCard(analysis)
        KalaBalaStrongestWeakestRow(analysis)
        KalaBalaInsightsCard(analysis)
        KalaBalaRecommendationsCard(analysis)
    }
}

/**
 * Birth context card with Neo-Vedic styling
 */
@Composable
private fun BirthContextCard(context: KalaBalaCalculator.BirthContext) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = AppTheme.AccentPrimary.copy(alpha = 0.06f),
        shape = RoundedCornerShape(NeoVedicTokens.CardCornerRadius),
        border = BorderStroke(NeoVedicTokens.ThinBorderWidth, AppTheme.AccentPrimary.copy(alpha = 0.2f))
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
                    fontFamily = CinzelDecorativeFamily,
                    fontSize = com.astro.vajra.ui.theme.NeoVedicFontSizes.S18,
                    fontWeight = FontWeight.Bold,
                    color = AppTheme.TextPrimary
                )
                Text(
                    text = "${context.pakshaType.displayName} " + stringResource(StringKeyUICommon.BULLET) + " " + stringResource(StringKeyShadbala.KALA_TITHI_LABEL) + " ${context.tithiNumber}",
                    fontFamily = PoppinsFontFamily,
                    fontSize = com.astro.vajra.ui.theme.NeoVedicFontSizes.S12,
                    color = AppTheme.TextSecondary
                )
                Text(
                    text = stringResource(StringKeyShadbala.KALA_HORA_LABEL) + stringResource(StringKeyUICommon.COLON) + " ${context.horaLord.displayName} " + stringResource(StringKeyUICommon.BULLET) + " " + stringResource(StringKeyShadbala.KALA_DAY_LABEL) + stringResource(StringKeyUICommon.COLON) + " ${context.dayLord.displayName}",
                    fontFamily = SpaceGroteskFamily,
                    fontSize = com.astro.vajra.ui.theme.NeoVedicFontSizes.S10,
                    color = AppTheme.TextMuted
                )
            }
        }
    }
}

/**
 * Score card with Neo-Vedic border styling
 */
@Composable
private fun KalaBalaScoreCard(analysis: KalaBalaCalculator.KalaBalaAnalysis) {
    val language = LocalLanguage.current
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
                text = stringResource(StringKeyShadbala.KALA_TOTAL).uppercase(),
                fontFamily = SpaceGroteskFamily,
                fontSize = com.astro.vajra.ui.theme.NeoVedicFontSizes.S12,
                letterSpacing = 1.sp,
                color = AppTheme.TextMuted
            )
            Spacer(modifier = Modifier.height(8.dp))
            val scoreText = if (language == Language.NEPALI) com.astro.vajra.core.common.BikramSambatConverter.toNepaliNumerals(analysis.overallScore.toInt()) else String.format("%.0f", analysis.overallScore)
            Text(
                text = scoreText,
                fontFamily = SpaceGroteskFamily,
                fontSize = com.astro.vajra.ui.theme.NeoVedicFontSizes.S32,
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

/**
 * Strongest/weakest planet row with Neo-Vedic styling
 */
@Composable
private fun KalaBalaStrongestWeakestRow(analysis: KalaBalaCalculator.KalaBalaAnalysis) {
    val language = LocalLanguage.current
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(NeoVedicTokens.CardSpacing)
    ) {
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
                    text = stringResource(StringKeyShadbala.STHANA_STRONG).uppercase(),
                    fontFamily = SpaceGroteskFamily,
                    fontSize = com.astro.vajra.ui.theme.NeoVedicFontSizes.S10,
                    letterSpacing = 1.sp,
                    color = AppTheme.SuccessColor
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = analysis.strongestPlanet.symbol,
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
                val strongestBala = analysis.planetaryKalaBala[analysis.strongestPlanet]
                strongestBala?.let {
                    val perc = if (language == Language.NEPALI) com.astro.vajra.core.common.BikramSambatConverter.toNepaliNumerals(it.percentageOfRequired.toInt()) else String.format("%.0f", it.percentageOfRequired)
                    Text(
                        text = "$perc" + stringResource(StringKeyUIExtra.PERCENT),
                        fontFamily = SpaceGroteskFamily,
                        fontSize = com.astro.vajra.ui.theme.NeoVedicFontSizes.S10,
                        color = AppTheme.SuccessColor
                    )
                }
            }
        }

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
                    text = stringResource(StringKeyShadbala.STHANA_WEAK).uppercase(),
                    fontFamily = SpaceGroteskFamily,
                    fontSize = com.astro.vajra.ui.theme.NeoVedicFontSizes.S10,
                    letterSpacing = 1.sp,
                    color = AppTheme.ErrorColor
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = analysis.weakestPlanet.symbol,
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
                val weakestBala = analysis.planetaryKalaBala[analysis.weakestPlanet]
                weakestBala?.let {
                    val perc = if (language == Language.NEPALI) com.astro.vajra.core.common.BikramSambatConverter.toNepaliNumerals(it.percentageOfRequired.toInt()) else String.format("%.0f", it.percentageOfRequired)
                    Text(
                        text = "$perc" + stringResource(StringKeyUIExtra.PERCENT),
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
private fun KalaBalaInsightsCard(analysis: KalaBalaCalculator.KalaBalaAnalysis) {
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
                    Text(text = stringResource(StringKeyUICommon.BULLET), color = AppTheme.AccentGold, fontWeight = FontWeight.Bold)
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
private fun KalaBalaRecommendationsCard(analysis: KalaBalaCalculator.KalaBalaAnalysis) {
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
                    Text(text = stringResource(StringKeyUIExtra.ARROW), color = AppTheme.AccentPrimary, fontWeight = FontWeight.Bold)
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
private fun KalaBalaComponentsSection(analysis: KalaBalaCalculator.KalaBalaAnalysis) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(
            text = stringResource(StringKeyShadbala.KALA_TAB_COMPONENTS).uppercase(),
            fontFamily = SpaceGroteskFamily,
            fontSize = com.astro.vajra.ui.theme.NeoVedicFontSizes.S12,
            letterSpacing = 2.sp,
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
    val language = LocalLanguage.current
    val scoreColor = when {
        component.percentage >= 70 -> AppTheme.SuccessColor
        component.percentage >= 50 -> AppTheme.AccentGold
        component.percentage >= 30 -> AppTheme.WarningColor
        else -> AppTheme.ErrorColor
    }

    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = AppTheme.CardBackground,
        shape = RoundedCornerShape(NeoVedicTokens.CardCornerRadius),
        border = BorderStroke(NeoVedicTokens.BorderWidth, AppTheme.BorderColor)
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
                        fontFamily = PoppinsFontFamily,
                        fontSize = com.astro.vajra.ui.theme.NeoVedicFontSizes.S14,
                        fontWeight = FontWeight.Medium,
                        color = AppTheme.TextPrimary
                    )
                    Text(
                        text = component.description,
                        fontFamily = PoppinsFontFamily,
                        fontSize = com.astro.vajra.ui.theme.NeoVedicFontSizes.S10,
                        color = AppTheme.TextMuted
                    )
                }
                Column(horizontalAlignment = Alignment.End) {
                    Text(
                        text = String.format("%.1f", component.avgVirupas),
                        fontFamily = SpaceGroteskFamily,
                        fontSize = com.astro.vajra.ui.theme.NeoVedicFontSizes.S16,
                        fontWeight = FontWeight.Bold,
                        color = scoreColor
                    )
                    Text(
                        text = stringResource(StringKeyUIExtra.SLASH) + " ${String.format("%.0f", component.maxVirupas)}",
                        fontFamily = SpaceGroteskFamily,
                        fontSize = com.astro.vajra.ui.theme.NeoVedicFontSizes.S10,
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
                    .clip(RoundedCornerShape(com.astro.vajra.ui.theme.NeoVedicTokens.ElementCornerRadius)),
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
                            .background(scoreColor.copy(alpha = 0.15f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = planetBala.planet.symbol,
                            fontFamily = CinzelDecorativeFamily,
                            fontSize = com.astro.vajra.ui.theme.NeoVedicFontSizes.S22,
                            color = scoreColor
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
                            color = scoreColor
                        )
                    }
                }

                val language = LocalLanguage.current
                Column(horizontalAlignment = Alignment.End) {
                    val perc = if (language == Language.NEPALI) com.astro.vajra.core.common.BikramSambatConverter.toNepaliNumerals(planetBala.percentageOfRequired.toInt()) else String.format("%.0f", planetBala.percentageOfRequired)
                    Text(
                        text = "$perc" + stringResource(StringKeyUIExtra.PERCENT),
                        fontFamily = SpaceGroteskFamily,
                        fontSize = com.astro.vajra.ui.theme.NeoVedicFontSizes.S16,
                        fontWeight = FontWeight.Bold,
                        color = scoreColor
                    )
                    val rupas = if (language == Language.NEPALI) com.astro.vajra.core.common.BikramSambatConverter.toNepaliNumerals(String.format("%.1f", planetBala.totalRupas)) else String.format("%.1f", planetBala.totalRupas)
                    Text(
                        text = "$rupas ${stringResource(StringKeyShadbala.COMMON_RUPAS)}",
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

                    KalaBalaBreakdownRow(stringResource(StringKeyShadbala.KALA_NATHONNATHA), planetBala.nathonnathaBala.virupas, 60.0)
                    KalaBalaBreakdownRow(stringResource(StringKeyShadbala.KALA_PAKSHA), planetBala.pakshaBala.virupas, 60.0)
                    KalaBalaBreakdownRow(stringResource(StringKeyShadbala.KALA_TRIBHAGA), planetBala.tribhagaBala.virupas, 60.0)
                    KalaBalaBreakdownRow(stringResource(StringKeyShadbala.KALA_HORA), planetBala.horaAdiBala.totalVirupas, 150.0)
                    KalaBalaBreakdownRow(stringResource(StringKeyShadbala.KALA_AYANA), planetBala.ayanaBala.virupas, 60.0)
                    KalaBalaBreakdownRow(stringResource(StringKeyShadbala.KALA_YUDDHA), planetBala.yuddhaBala.virupas + 30, 60.0)

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
private fun KalaBalaBreakdownRow(name: String, value: Double, max: Double) {
    val language = LocalLanguage.current
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = name,
            fontFamily = PoppinsFontFamily,
            fontSize = com.astro.vajra.ui.theme.NeoVedicFontSizes.S12,
            color = AppTheme.TextSecondary
        )
        Text(
            text = "${String.format("%.1f", value)} " + stringResource(StringKeyUIExtra.SLASH) + " ${String.format("%.0f", max)}",
            fontFamily = SpaceGroteskFamily,
            fontSize = com.astro.vajra.ui.theme.NeoVedicFontSizes.S12,
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
                fontFamily = PoppinsFontFamily,
                fontSize = com.astro.vajra.ui.theme.NeoVedicFontSizes.S14,
                color = AppTheme.TextMuted
            )
        }
    }
}

/**
 * Empty content using NeoVedicEmptyState component
 */
@Composable
private fun KalaBalaEmptyContent(modifier: Modifier = Modifier) {
    NeoVedicEmptyState(
        title = stringResource(StringKeyShadbala.KALA_TITLE),
        subtitle = stringResource(StringKeyShadbala.KALA_UNABLE),
        icon = Icons.Outlined.Schedule,
        modifier = modifier
    )
}

@Composable
private fun KalaBalaInfoDialog(onDismiss: () -> Unit) {
    androidx.compose.material3.AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = stringResource(StringKeyShadbala.KALA_INFO_TITLE),
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
                    text = stringResource(StringKeyShadbala.KALA_INFO_DESC),
                    fontFamily = PoppinsFontFamily,
                    fontSize = com.astro.vajra.ui.theme.NeoVedicFontSizes.S14,
                    color = AppTheme.TextSecondary,
                    lineHeight = 22.sp
                )
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = stringResource(StringKeyShadbala.KALA_VEDIC_REF),
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
