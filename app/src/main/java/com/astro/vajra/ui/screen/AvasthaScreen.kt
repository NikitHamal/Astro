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
import androidx.compose.material.icons.outlined.AutoAwesome
import androidx.compose.material.icons.outlined.Brightness7
import androidx.compose.material.icons.outlined.EmojiEmotions
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.Psychology
import androidx.compose.material.icons.outlined.SelfImprovement
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material.icons.outlined.TrendingDown
import androidx.compose.material.icons.outlined.TrendingUp
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.astro.vajra.data.localization.LocalLanguage
import com.astro.vajra.core.common.StringKeyDosha
import com.astro.vajra.core.common.StringKey
import com.astro.vajra.core.common.StringKeyAnalysis
import com.astro.vajra.core.common.getLocalizedName
import com.astro.vajra.data.localization.currentLanguage
import com.astro.vajra.data.localization.localizedAbbr
import com.astro.vajra.data.localization.stringResource
import com.astro.vajra.core.model.VedicChart
import com.astro.vajra.ephemeris.AvasthaCalculator
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

/**
 * Avastha Screen
 *
 * Displays the various planetary states (Avasthas) that indicate how
 * effectively each planet can deliver its results. Shows Baladi, Jagradadi,
 * Deeptadi, and Lajjitadi Avasthas for each planet.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AvasthaScreen(
    chart: VedicChart?,
    onBack: () -> Unit
) {
    val language = currentLanguage()
    var selectedTab by rememberSaveable { mutableIntStateOf(0) }
    var isCalculating by remember { mutableStateOf(true) }
    var analysis by remember { mutableStateOf<AvasthaCalculator.AvasthaAnalysis?>(null) }
    var showInfoDialog by remember { mutableStateOf(false) }

    val tabs = listOf(
        stringResource(StringKeyAnalysis.AVASTHA_TAB_OVERVIEW),
        stringResource(StringKeyAnalysis.AVASTHA_TAB_PLANETS),
        stringResource(StringKeyAnalysis.AVASTHA_TAB_BALADI),
        stringResource(StringKeyAnalysis.AVASTHA_TAB_JAGRADADI),
        stringResource(StringKeyAnalysis.AVASTHA_TAB_DEEPTADI),
        stringResource(StringKeyAnalysis.AVASTHA_TAB_LAJJITADI)
    )

    // Calculate analysis
    LaunchedEffect(chart, language) {
        if (chart == null) {
            isCalculating = false
            return@LaunchedEffect
        }
        isCalculating = true
        delay(300)
        try {
            analysis = withContext(Dispatchers.Default) {
                AvasthaCalculator.analyzeAvasthas(chart, language)
            }
        } catch (e: Exception) {
            // Handle error
        }
        isCalculating = false
    }

    Scaffold(
        containerColor = AppTheme.ScreenBackground,
        topBar = {
            NeoVedicPageHeader(
                title = stringResource(StringKeyDosha.AVASTHA_SCREEN_TITLE),
                subtitle = stringResource(StringKeyDosha.AVASTHA_SUBTITLE),
                onBack = onBack,
                actions = {
                    IconButton(onClick = { showInfoDialog = true }) {
                        Icon(
                            imageVector = Icons.Outlined.Info,
                            contentDescription = stringResource(StringKey.MISC_INFO),
                            tint = AppTheme.TextSecondary
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        when {
            isCalculating -> {
                AvasthaLoadingContent(modifier = Modifier.padding(paddingValues))
            }
            chart == null || analysis == null -> {
                AvasthaEmptyContent(modifier = Modifier.padding(paddingValues))
            }
            else -> {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                        .background(AppTheme.ScreenBackground),
                    contentPadding = PaddingValues(bottom = 32.dp)
                ) {
                    // Tab selector
                    item {
                        AvasthaTabSelector(
                            tabs = tabs,
                            selectedTab = selectedTab,
                            onTabSelected = { selectedTab = it }
                        )
                    }

                    // Content based on tab
                    when (selectedTab) {
                        0 -> item { AvasthaOverviewSection(analysis!!) }
                        1 -> item { AvasthaPlanetsSection(analysis!!) }
                        2 -> item { BaladiSection(analysis!!) }
                        3 -> item { JagradadiSection(analysis!!) }
                        4 -> item { DeeptadiSection(analysis!!) }
                        5 -> item { LajjitadiSection(analysis!!) }
                    }
                }
            }
        }
    }

    if (showInfoDialog) {
        AvasthaInfoDialog(onDismiss = { showInfoDialog = false })
    }
}

@Composable
private fun AvasthaTabSelector(
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

@Composable
private fun AvasthaOverviewSection(analysis: AvasthaCalculator.AvasthaAnalysis) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Overall Strength Card
        OverallStrengthCard(analysis = analysis)

        // Strongest and Weakest Planets
        StrengthExtremeCards(analysis)

        // Quick Stats
        AvasthaQuickStatsRow(analysis)

        // Interpretation
        AvasthaInterpretationCard(analysis)

        // Recommendations
        if (analysis.recommendations.isNotEmpty()) {
            AvasthaRecommendationsCard(analysis.recommendations)
        }
    }
}

@Composable
private fun OverallStrengthCard(analysis: AvasthaCalculator.AvasthaAnalysis) {
    val strengthColor = getStrengthColor(analysis.overallStrength)

    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = AppTheme.CardBackground,
        shape = RoundedCornerShape(com.astro.vajra.ui.theme.NeoVedicTokens.CardCornerRadius),
        border = BorderStroke(com.astro.vajra.ui.theme.NeoVedicTokens.BorderWidth, AppTheme.BorderColor)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Strength icon
            Box(
                modifier = Modifier
                    .size(72.dp)
                    .clip(CircleShape)
                    .background(strengthColor.copy(alpha = 0.15f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Outlined.Psychology,
                    contentDescription = null,
                    tint = strengthColor,
                    modifier = Modifier.size(40.dp)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = stringResource(StringKeyDosha.AVASTHA_OVERALL_STRENGTH),
                fontFamily = CinzelDecorativeFamily,
                fontSize = com.astro.vajra.ui.theme.NeoVedicFontSizes.S18,
                fontWeight = FontWeight.Bold,
                color = AppTheme.TextPrimary
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "${analysis.overallStrength}%",
                fontFamily = SpaceGroteskFamily,
                fontSize = com.astro.vajra.ui.theme.NeoVedicFontSizes.S32,
                fontWeight = FontWeight.Bold,
                color = strengthColor
            )

            Spacer(modifier = Modifier.height(12.dp))

            LinearProgressIndicator(
                progress = { analysis.overallStrength / 100f },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(8.dp)
                    .clip(RoundedCornerShape(com.astro.vajra.ui.theme.NeoVedicTokens.ElementCornerRadius)),
                color = strengthColor,
                trackColor = strengthColor.copy(alpha = 0.2f),
                strokeCap = StrokeCap.Round
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = when {
                    analysis.overallStrength >= 70 -> stringResource(StringKeyAnalysis.AVASTHA_STRONG_CONFIG)
                    analysis.overallStrength >= 50 -> stringResource(StringKeyAnalysis.AVASTHA_MODERATE_STRENGTH)
                    else -> stringResource(StringKeyAnalysis.AVASTHA_NEEDS_MEASURES)
                },
                fontFamily = PoppinsFontFamily,
                fontSize = com.astro.vajra.ui.theme.NeoVedicFontSizes.S12,
                color = AppTheme.TextMuted,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
private fun StrengthExtremeCards(analysis: AvasthaCalculator.AvasthaAnalysis) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // Strongest Planet
        analysis.strongestPlanet?.let { strongest ->
            Surface(
                modifier = Modifier.weight(1f),
                color = AppTheme.SuccessColor.copy(alpha = 0.08f),
                shape = RoundedCornerShape(com.astro.vajra.ui.theme.NeoVedicTokens.ElementCornerRadius),
                border = BorderStroke(NeoVedicTokens.ThinBorderWidth, AppTheme.SuccessColor.copy(alpha = 0.2f))
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        imageVector = Icons.Outlined.TrendingUp,
                        contentDescription = null,
                        tint = AppTheme.SuccessColor,
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = strongest.planet.localizedAbbr(),
                        fontFamily = CinzelDecorativeFamily,
                        fontSize = com.astro.vajra.ui.theme.NeoVedicFontSizes.S24,
                        color = getPlanetColor(strongest.planet)
                    )
                    Text(
                        text = strongest.planet.displayName,
                        fontFamily = PoppinsFontFamily,
                        fontSize = com.astro.vajra.ui.theme.NeoVedicFontSizes.S12,
                        fontWeight = FontWeight.SemiBold,
                        color = AppTheme.TextPrimary
                    )
                    Text(
                        text = "${strongest.effectiveStrength}%",
                        fontFamily = SpaceGroteskFamily,
                        fontSize = com.astro.vajra.ui.theme.NeoVedicFontSizes.S16,
                        fontWeight = FontWeight.Bold,
                        color = AppTheme.SuccessColor
                    )
                    Text(
                        text = stringResource(StringKeyDosha.AVASTHA_STRONGEST).uppercase(),
                        fontFamily = SpaceGroteskFamily,
                        fontSize = com.astro.vajra.ui.theme.NeoVedicFontSizes.S10,
                        color = AppTheme.TextMuted
                    )
                }
            }
        }

        // Weakest Planet
        analysis.weakestPlanet?.let { weakest ->
            Surface(
                modifier = Modifier.weight(1f),
                color = AppTheme.WarningColor.copy(alpha = 0.08f),
                shape = RoundedCornerShape(com.astro.vajra.ui.theme.NeoVedicTokens.ElementCornerRadius),
                border = BorderStroke(NeoVedicTokens.ThinBorderWidth, AppTheme.WarningColor.copy(alpha = 0.2f))
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        imageVector = Icons.Outlined.TrendingDown,
                        contentDescription = null,
                        tint = AppTheme.WarningColor,
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = weakest.planet.localizedAbbr(),
                        fontFamily = CinzelDecorativeFamily,
                        fontSize = com.astro.vajra.ui.theme.NeoVedicFontSizes.S24,
                        color = getPlanetColor(weakest.planet)
                    )
                    Text(
                        text = weakest.planet.displayName,
                        fontFamily = PoppinsFontFamily,
                        fontSize = com.astro.vajra.ui.theme.NeoVedicFontSizes.S12,
                        fontWeight = FontWeight.SemiBold,
                        color = AppTheme.TextPrimary
                    )
                    Text(
                        text = "${weakest.effectiveStrength}%",
                        fontFamily = SpaceGroteskFamily,
                        fontSize = com.astro.vajra.ui.theme.NeoVedicFontSizes.S16,
                        fontWeight = FontWeight.Bold,
                        color = AppTheme.WarningColor
                    )
                    Text(
                        text = stringResource(StringKeyDosha.AVASTHA_NEEDS_ATTENTION).uppercase(),
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
private fun AvasthaQuickStatsRow(analysis: AvasthaCalculator.AvasthaAnalysis) {
    val strongCount = analysis.planetaryAvasthas.count { it.effectiveStrength >= 60 }
    val moderateCount = analysis.planetaryAvasthas.count { it.effectiveStrength in 40..59 }
    val weakCount = analysis.planetaryAvasthas.count { it.effectiveStrength < 40 }

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        AvasthaStatCard(
            title = stringResource(StringKeyAnalysis.STRENGTH_STRONG),
            value = "$strongCount",
            color = AppTheme.SuccessColor,
            modifier = Modifier.weight(1f)
        )
        AvasthaStatCard(
            title = stringResource(StringKeyAnalysis.STRENGTH_AVERAGE),
            value = "$moderateCount",
            color = AppTheme.AccentGold,
            modifier = Modifier.weight(1f)
        )
        AvasthaStatCard(
            title = stringResource(StringKeyAnalysis.STRENGTH_WEAK),
            value = "$weakCount",
            color = AppTheme.WarningColor,
            modifier = Modifier.weight(1f)
        )
    }
}

@Composable
private fun AvasthaStatCard(
    title: String,
    value: String,
    color: Color,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier,
        color = AppTheme.CardBackground,
        shape = RoundedCornerShape(com.astro.vajra.ui.theme.NeoVedicTokens.ElementCornerRadius),
        border = BorderStroke(com.astro.vajra.ui.theme.NeoVedicTokens.BorderWidth, AppTheme.BorderColor)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = value,
                fontFamily = SpaceGroteskFamily,
                fontSize = com.astro.vajra.ui.theme.NeoVedicFontSizes.S20,
                fontWeight = FontWeight.Bold,
                color = color
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = title,
                fontFamily = PoppinsFontFamily,
                fontSize = com.astro.vajra.ui.theme.NeoVedicFontSizes.S12,
                color = AppTheme.TextMuted
            )
        }
    }
}

@Composable
private fun AvasthaInterpretationCard(analysis: AvasthaCalculator.AvasthaAnalysis) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = AppTheme.CardBackground,
        shape = RoundedCornerShape(com.astro.vajra.ui.theme.NeoVedicTokens.ElementCornerRadius),
        border = BorderStroke(com.astro.vajra.ui.theme.NeoVedicTokens.BorderWidth, AppTheme.BorderColor)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Icon(
                    imageVector = Icons.Outlined.AutoAwesome,
                    contentDescription = null,
                    tint = AppTheme.AccentGold,
                    modifier = Modifier.size(20.dp)
                )
                Text(
                    text = stringResource(StringKeyDosha.UI_INTERPRETATION),
                    fontFamily = CinzelDecorativeFamily,
                    fontSize = com.astro.vajra.ui.theme.NeoVedicFontSizes.S16,
                    fontWeight = FontWeight.SemiBold,
                    color = AppTheme.TextPrimary
                )
            }
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = analysis.interpretation,
                fontFamily = PoppinsFontFamily,
                fontSize = com.astro.vajra.ui.theme.NeoVedicFontSizes.S14,
                color = AppTheme.TextSecondary,
                lineHeight = 24.sp
            )
        }
    }
}

@Composable
private fun AvasthaRecommendationsCard(recommendations: List<AvasthaCalculator.AvasthaRecommendation>) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = AppTheme.CardBackground,
        shape = RoundedCornerShape(com.astro.vajra.ui.theme.NeoVedicTokens.ElementCornerRadius),
        border = BorderStroke(com.astro.vajra.ui.theme.NeoVedicTokens.BorderWidth, AppTheme.BorderColor)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = stringResource(StringKeyDosha.UI_RECOMMENDATIONS),
                fontFamily = CinzelDecorativeFamily,
                fontSize = com.astro.vajra.ui.theme.NeoVedicFontSizes.S16,
                fontWeight = FontWeight.SemiBold,
                color = AppTheme.TextPrimary
            )
            Spacer(modifier = Modifier.height(12.dp))

            recommendations.forEach { rec ->
                val language = LocalLanguage.current
                val priorityColor = when (rec.priority) {
                    AvasthaCalculator.RemedyPriority.HIGH -> AppTheme.ErrorColor
                    AvasthaCalculator.RemedyPriority.MEDIUM -> AppTheme.WarningColor
                    AvasthaCalculator.RemedyPriority.LOW -> AppTheme.AccentTeal
                }

                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp),
                    shape = RoundedCornerShape(com.astro.vajra.ui.theme.NeoVedicTokens.ElementCornerRadius),
                    color = AppTheme.CardBackgroundElevated,
                    border = BorderStroke(NeoVedicTokens.ThinBorderWidth, AppTheme.BorderColor)
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = rec.planet.localizedAbbr(),
                                fontSize = com.astro.vajra.ui.theme.NeoVedicFontSizes.S20,
                                color = getPlanetColor(rec.planet)
                            )
                            Text(
                                text = rec.planet.displayName,
                                fontFamily = PoppinsFontFamily,
                                fontSize = com.astro.vajra.ui.theme.NeoVedicFontSizes.S14,
                                fontWeight = FontWeight.SemiBold,
                                color = AppTheme.TextPrimary
                            )
                            Surface(
                               shape = RoundedCornerShape(com.astro.vajra.ui.theme.NeoVedicTokens.ElementCornerRadius),
                               color = priorityColor.copy(alpha = 0.15f)
                            ) {
                               Text(
                                   text = rec.priority.getLocalizedName(language),
                                   fontFamily = SpaceGroteskFamily,
                                   fontSize = com.astro.vajra.ui.theme.NeoVedicFontSizes.S10,
                                   color = priorityColor,
                                   modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                               )
                            }
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = rec.issue,
                            fontFamily = PoppinsFontFamily,
                            fontSize = com.astro.vajra.ui.theme.NeoVedicFontSizes.S12,
                            color = AppTheme.TextMuted
                        )
                        Text(
                            text = rec.remedy,
                            fontFamily = PoppinsFontFamily,
                            fontSize = com.astro.vajra.ui.theme.NeoVedicFontSizes.S12,
                            color = AppTheme.TextSecondary
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun AvasthaPlanetsSection(analysis: AvasthaCalculator.AvasthaAnalysis) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        analysis.planetaryAvasthas.forEach { avastha ->
            PlanetAvasthaCard(avastha = avastha)
        }
    }
}

@Composable
private fun PlanetAvasthaCard(avastha: AvasthaCalculator.PlanetaryAvastha) {
    var expanded by remember { mutableStateOf(false) }
    val strengthColor = getStrengthColor(avastha.effectiveStrength)

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .animateContentSize(),
        color = AppTheme.CardBackground,
        shape = RoundedCornerShape(com.astro.vajra.ui.theme.NeoVedicTokens.ElementCornerRadius),
        border = BorderStroke(NeoVedicTokens.BorderWidth, AppTheme.BorderColor)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { expanded = !expanded }
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
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
                            .background(getPlanetColor(avastha.planet).copy(alpha = 0.15f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = avastha.planet.localizedAbbr(),
                            fontFamily = CinzelDecorativeFamily,
                            fontSize = com.astro.vajra.ui.theme.NeoVedicFontSizes.S24,
                            color = getPlanetColor(avastha.planet)
                        )
                    }
                    Column {
                        Text(
                            text = avastha.planet.displayName,
                            fontFamily = CinzelDecorativeFamily,
                            fontSize = com.astro.vajra.ui.theme.NeoVedicFontSizes.S16,
                            fontWeight = FontWeight.Bold,
                            color = AppTheme.TextPrimary
                        )
                        Text(
                            text = "${avastha.sign.displayName} at ${String.format("%.1f", avastha.degree)}\u00B0",
                            fontFamily = PoppinsFontFamily,
                            fontSize = com.astro.vajra.ui.theme.NeoVedicFontSizes.S12,
                            color = AppTheme.TextMuted
                        )
                    }
                }
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Column(horizontalAlignment = Alignment.End) {
                        Text(
                            text = "${avastha.effectiveStrength}%",
                            fontFamily = SpaceGroteskFamily,
                            fontSize = com.astro.vajra.ui.theme.NeoVedicFontSizes.S16,
                            fontWeight = FontWeight.Bold,
                            color = strengthColor
                        )
                    }
                    Icon(
                        imageVector = if (expanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                        contentDescription = null,
                        tint = AppTheme.TextMuted
                    )
                }
            }

            // Avastha badges
            Spacer(modifier = Modifier.height(12.dp))
            val language = LocalLanguage.current
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                AvasthaBadge(
                    label = avastha.baladiAvastha.getLocalizedName(language).take(10),
                    color = getBaladiColor(avastha.baladiAvastha)
                )
                AvasthaBadge(
                    label = avastha.jagradadiAvastha.getLocalizedName(language).take(10),
                    color = getJagradadiColor(avastha.jagradadiAvastha)
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

                    // All four avasthas with descriptions
                    AvasthaDetailRow(
                        title = stringResource(StringKeyAnalysis.AVASTHA_AGE_STATE),
                        value = avastha.baladiAvastha.getLocalizedName(language),
                        description = avastha.baladiAvastha.getLocalizedDescription(language),
                        color = getBaladiColor(avastha.baladiAvastha)
                    )
                    AvasthaDetailRow(
                        title = stringResource(StringKeyAnalysis.AVASTHA_ALERTNESS),
                        value = avastha.jagradadiAvastha.getLocalizedName(language),
                        description = avastha.jagradadiAvastha.getLocalizedDescription(language),
                        color = getJagradadiColor(avastha.jagradadiAvastha)
                    )
                    AvasthaDetailRow(
                        title = stringResource(StringKeyAnalysis.AVASTHA_DIGNITY),
                        value = avastha.deeptadiAvastha.getLocalizedName(language),
                        description = avastha.deeptadiAvastha.getLocalizedDescription(language),
                        color = getDeeptadiColor(avastha.deeptadiAvastha)
                    )
                    AvasthaDetailRow(
                        title = stringResource(StringKeyAnalysis.AVASTHA_EMOTIONAL),
                        value = avastha.lajjitadiAvastha.getLocalizedName(language),
                        description = avastha.lajjitadiAvastha.getLocalizedDescription(language),
                        color = getLajjitadiColor(avastha.lajjitadiAvastha)
                    )
                }
            }
        }
    }
}

@Composable
private fun AvasthaBadge(label: String, color: Color) {
    Surface(
        shape = RoundedCornerShape(com.astro.vajra.ui.theme.NeoVedicTokens.ElementCornerRadius),
        color = color.copy(alpha = 0.15f)
    ) {
        Text(
            text = label,
            fontFamily = SpaceGroteskFamily,
            fontSize = com.astro.vajra.ui.theme.NeoVedicFontSizes.S10,
            color = color,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
        )
    }
}

@Composable
private fun AvasthaDetailRow(
    title: String,
    value: String,
    description: String,
    color: Color
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = title,
                fontFamily = PoppinsFontFamily,
                fontSize = com.astro.vajra.ui.theme.NeoVedicFontSizes.S12,
                fontWeight = FontWeight.SemiBold,
                color = AppTheme.TextMuted
            )
            Text(
                text = value,
                fontFamily = PoppinsFontFamily,
                fontSize = com.astro.vajra.ui.theme.NeoVedicFontSizes.S12,
                fontWeight = FontWeight.Bold,
                color = color
            )
        }
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = description,
            fontFamily = PoppinsFontFamily,
            fontSize = com.astro.vajra.ui.theme.NeoVedicFontSizes.S12,
            color = AppTheme.TextSecondary,
            lineHeight = 18.sp
        )
    }
}

@Composable
private fun BaladiSection(analysis: AvasthaCalculator.AvasthaAnalysis) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // Info card
        Surface(
            modifier = Modifier.fillMaxWidth(),
            color = AppTheme.AccentPrimary.copy(alpha = 0.08f),
            shape = RoundedCornerShape(com.astro.vajra.ui.theme.NeoVedicTokens.ElementCornerRadius),
            border = BorderStroke(NeoVedicTokens.ThinBorderWidth, AppTheme.AccentPrimary.copy(alpha = 0.2f))
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(
                        imageVector = Icons.Outlined.SelfImprovement,
                        contentDescription = null,
                        tint = AppTheme.AccentPrimary,
                        modifier = Modifier.size(20.dp)
                    )
                    Text(
                        text = stringResource(StringKeyDosha.AVASTHA_AGE_TITLE),
                        fontFamily = CinzelDecorativeFamily,
                        fontSize = com.astro.vajra.ui.theme.NeoVedicFontSizes.S16,
                        fontWeight = FontWeight.SemiBold,
                        color = AppTheme.TextPrimary
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = stringResource(StringKeyDosha.AVASTHA_AGE_DESC),
                    fontFamily = PoppinsFontFamily,
                    fontSize = com.astro.vajra.ui.theme.NeoVedicFontSizes.S12,
                    color = AppTheme.TextSecondary
                )
            }
        }

        // Planet list
        analysis.planetaryAvasthas.forEach { avastha ->
            BaladiPlanetCard(avastha = avastha)
        }
    }
}

@Composable
private fun BaladiPlanetCard(avastha: AvasthaCalculator.PlanetaryAvastha) {
    val color = getBaladiColor(avastha.baladiAvastha)

    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = AppTheme.CardBackground,
        shape = RoundedCornerShape(com.astro.vajra.ui.theme.NeoVedicTokens.ElementCornerRadius),
        border = BorderStroke(NeoVedicTokens.BorderWidth, AppTheme.BorderColor)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(
                    text = avastha.planet.localizedAbbr(),
                    fontFamily = CinzelDecorativeFamily,
                    fontSize = com.astro.vajra.ui.theme.NeoVedicFontSizes.S24,
                    color = getPlanetColor(avastha.planet)
                )
                Column {
                    Text(
                        text = avastha.planet.displayName,
                        fontFamily = PoppinsFontFamily,
                        fontSize = com.astro.vajra.ui.theme.NeoVedicFontSizes.S14,
                        fontWeight = FontWeight.SemiBold,
                        color = AppTheme.TextPrimary
                    )
                    Text(
                        text = stringResource(StringKeyDosha.AVASTHA_RESULT_CAPACITY, avastha.baladiAvastha.resultPercentage),
                        fontFamily = SpaceGroteskFamily,
                        fontSize = com.astro.vajra.ui.theme.NeoVedicFontSizes.S10,
                        color = AppTheme.TextMuted
                    )
                }
            }
            Surface(
                shape = RoundedCornerShape(com.astro.vajra.ui.theme.NeoVedicTokens.ElementCornerRadius),
                color = color.copy(alpha = 0.15f)
            ) {
                val language = LocalLanguage.current
                Text(
                    text = avastha.baladiAvastha.getLocalizedName(language),
                    fontFamily = SpaceGroteskFamily,
                    fontSize = com.astro.vajra.ui.theme.NeoVedicFontSizes.S12,
                    fontWeight = FontWeight.SemiBold,
                    color = color,
                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                )
            }
        }
    }
}

@Composable
private fun JagradadiSection(analysis: AvasthaCalculator.AvasthaAnalysis) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // Info card
        Surface(
            modifier = Modifier.fillMaxWidth(),
            color = AppTheme.AccentGold.copy(alpha = 0.08f),
            shape = RoundedCornerShape(com.astro.vajra.ui.theme.NeoVedicTokens.ElementCornerRadius),
            border = BorderStroke(NeoVedicTokens.ThinBorderWidth, AppTheme.AccentGold.copy(alpha = 0.2f))
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Brightness7,
                        contentDescription = null,
                        tint = AppTheme.AccentGold,
                        modifier = Modifier.size(20.dp)
                    )
                    Text(
                        text = stringResource(StringKeyDosha.AVASTHA_ALERTNESS_TITLE),
                        fontFamily = CinzelDecorativeFamily,
                        fontSize = com.astro.vajra.ui.theme.NeoVedicFontSizes.S16,
                        fontWeight = FontWeight.SemiBold,
                        color = AppTheme.TextPrimary
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = stringResource(StringKeyDosha.AVASTHA_ALERTNESS_DESC),
                    fontFamily = PoppinsFontFamily,
                    fontSize = com.astro.vajra.ui.theme.NeoVedicFontSizes.S12,
                    color = AppTheme.TextSecondary
                )
            }
        }

        // Planet list
        analysis.planetaryAvasthas.forEach { avastha ->
            JagradadiPlanetCard(avastha = avastha)
        }
    }
}

@Composable
private fun JagradadiPlanetCard(avastha: AvasthaCalculator.PlanetaryAvastha) {
    val color = getJagradadiColor(avastha.jagradadiAvastha)

    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = AppTheme.CardBackground,
        shape = RoundedCornerShape(com.astro.vajra.ui.theme.NeoVedicTokens.ElementCornerRadius),
        border = BorderStroke(NeoVedicTokens.BorderWidth, AppTheme.BorderColor)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(
                    text = avastha.planet.localizedAbbr(),
                    fontFamily = CinzelDecorativeFamily,
                    fontSize = com.astro.vajra.ui.theme.NeoVedicFontSizes.S24,
                    color = getPlanetColor(avastha.planet)
                )
                Column {
                    Text(
                        text = avastha.planet.displayName,
                        fontFamily = PoppinsFontFamily,
                        fontSize = com.astro.vajra.ui.theme.NeoVedicFontSizes.S14,
                        fontWeight = FontWeight.SemiBold,
                        color = AppTheme.TextPrimary
                    )
                    Text(
                        text = stringResource(StringKeyDosha.AVASTHA_RESULT_CAPACITY, avastha.jagradadiAvastha.resultPercentage),
                        fontFamily = SpaceGroteskFamily,
                        fontSize = com.astro.vajra.ui.theme.NeoVedicFontSizes.S10,
                        color = AppTheme.TextMuted
                    )
                }
            }
            Surface(
                shape = RoundedCornerShape(com.astro.vajra.ui.theme.NeoVedicTokens.ElementCornerRadius),
                color = color.copy(alpha = 0.15f)
            ) {
                val language = LocalLanguage.current
                Text(
                    text = avastha.jagradadiAvastha.getLocalizedName(language),
                    fontFamily = SpaceGroteskFamily,
                    fontSize = com.astro.vajra.ui.theme.NeoVedicFontSizes.S12,
                    fontWeight = FontWeight.SemiBold,
                    color = color,
                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                )
            }
        }
    }
}

@Composable
private fun DeeptadiSection(analysis: AvasthaCalculator.AvasthaAnalysis) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // Info card
        Surface(
            modifier = Modifier.fillMaxWidth(),
            color = AppTheme.AccentTeal.copy(alpha = 0.08f),
            shape = RoundedCornerShape(com.astro.vajra.ui.theme.NeoVedicTokens.ElementCornerRadius),
            border = BorderStroke(NeoVedicTokens.ThinBorderWidth, AppTheme.AccentTeal.copy(alpha = 0.2f))
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Star,
                        contentDescription = null,
                        tint = AppTheme.AccentTeal,
                        modifier = Modifier.size(20.dp)
                    )
                    Text(
                        text = stringResource(StringKeyDosha.AVASTHA_DIGNITY_TITLE),
                        fontFamily = CinzelDecorativeFamily,
                        fontSize = com.astro.vajra.ui.theme.NeoVedicFontSizes.S16,
                        fontWeight = FontWeight.SemiBold,
                        color = AppTheme.TextPrimary
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = stringResource(StringKeyDosha.AVASTHA_DIGNITY_DESC),
                    fontFamily = PoppinsFontFamily,
                    fontSize = com.astro.vajra.ui.theme.NeoVedicFontSizes.S12,
                    color = AppTheme.TextSecondary
                )
            }
        }

        // Planet list
        analysis.planetaryAvasthas.forEach { avastha ->
            DeeptadiPlanetCard(avastha = avastha)
        }
    }
}

@Composable
private fun DeeptadiPlanetCard(avastha: AvasthaCalculator.PlanetaryAvastha) {
    val language = LocalLanguage.current
    val color = getDeeptadiColor(avastha.deeptadiAvastha)

    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = AppTheme.CardBackground,
        shape = RoundedCornerShape(com.astro.vajra.ui.theme.NeoVedicTokens.ElementCornerRadius),
        border = BorderStroke(NeoVedicTokens.BorderWidth, AppTheme.BorderColor)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text(
                        text = avastha.planet.localizedAbbr(),
                        fontFamily = CinzelDecorativeFamily,
                        fontSize = com.astro.vajra.ui.theme.NeoVedicFontSizes.S24,
                        color = getPlanetColor(avastha.planet)
                    )
                    Text(
                        text = avastha.planet.displayName,
                        fontFamily = PoppinsFontFamily,
                        fontSize = com.astro.vajra.ui.theme.NeoVedicFontSizes.S14,
                        fontWeight = FontWeight.SemiBold,
                        color = AppTheme.TextPrimary
                    )
                }
                Surface(
                    shape = RoundedCornerShape(com.astro.vajra.ui.theme.NeoVedicTokens.ElementCornerRadius),
                    color = color.copy(alpha = 0.15f)
                ) {
                    val language = LocalLanguage.current
                    Text(
                        text = avastha.deeptadiAvastha.getLocalizedName(language),
                        fontFamily = SpaceGroteskFamily,
                        fontSize = com.astro.vajra.ui.theme.NeoVedicFontSizes.S12,
                        fontWeight = FontWeight.SemiBold,
                        color = color,
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                    )
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = avastha.deeptadiAvastha.getLocalizedDescription(language),
                fontFamily = PoppinsFontFamily,
                fontSize = com.astro.vajra.ui.theme.NeoVedicFontSizes.S12,
                color = AppTheme.TextMuted,
                lineHeight = 18.sp
            )
        }
    }
}

@Composable
private fun LajjitadiSection(analysis: AvasthaCalculator.AvasthaAnalysis) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // Info card
        Surface(
            modifier = Modifier.fillMaxWidth(),
            color = AppTheme.PlanetVenus.copy(alpha = 0.08f),
            shape = RoundedCornerShape(com.astro.vajra.ui.theme.NeoVedicTokens.ElementCornerRadius),
            border = BorderStroke(NeoVedicTokens.ThinBorderWidth, AppTheme.PlanetVenus.copy(alpha = 0.2f))
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(
                        imageVector = Icons.Outlined.EmojiEmotions,
                        contentDescription = null,
                        tint = AppTheme.PlanetVenus,
                        modifier = Modifier.size(20.dp)
                    )
                    Text(
                        text = stringResource(StringKeyDosha.AVASTHA_EMOTIONAL_TITLE),
                        fontFamily = CinzelDecorativeFamily,
                        fontSize = com.astro.vajra.ui.theme.NeoVedicFontSizes.S16,
                        fontWeight = FontWeight.SemiBold,
                        color = AppTheme.TextPrimary
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = stringResource(StringKeyDosha.AVASTHA_EMOTIONAL_DESC),
                    fontFamily = PoppinsFontFamily,
                    fontSize = com.astro.vajra.ui.theme.NeoVedicFontSizes.S12,
                    color = AppTheme.TextSecondary
                )
            }
        }

        // Planet list
        analysis.planetaryAvasthas.forEach { avastha ->
            LajjitadiPlanetCard(avastha = avastha)
        }
    }
}

@Composable
private fun LajjitadiPlanetCard(avastha: AvasthaCalculator.PlanetaryAvastha) {
    val language = LocalLanguage.current
    val color = getLajjitadiColor(avastha.lajjitadiAvastha)

    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = AppTheme.CardBackground,
        shape = RoundedCornerShape(com.astro.vajra.ui.theme.NeoVedicTokens.ElementCornerRadius),
        border = BorderStroke(NeoVedicTokens.BorderWidth, AppTheme.BorderColor)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text(
                        text = avastha.planet.localizedAbbr(),
                        fontFamily = CinzelDecorativeFamily,
                        fontSize = com.astro.vajra.ui.theme.NeoVedicFontSizes.S24,
                        color = getPlanetColor(avastha.planet)
                    )
                    Text(
                        text = avastha.planet.displayName,
                        fontFamily = PoppinsFontFamily,
                        fontSize = com.astro.vajra.ui.theme.NeoVedicFontSizes.S14,
                        fontWeight = FontWeight.SemiBold,
                        color = AppTheme.TextPrimary
                    )
                }
                Surface(
                    shape = RoundedCornerShape(com.astro.vajra.ui.theme.NeoVedicTokens.ElementCornerRadius),
                    color = color.copy(alpha = 0.15f)
                ) {
                    val language = LocalLanguage.current
                    Text(
                        text = avastha.lajjitadiAvastha.getLocalizedName(language),
                        fontFamily = SpaceGroteskFamily,
                        fontSize = com.astro.vajra.ui.theme.NeoVedicFontSizes.S12,
                        fontWeight = FontWeight.SemiBold,
                        color = color,
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                    )
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = avastha.lajjitadiAvastha.getLocalizedDescription(language),
                fontFamily = PoppinsFontFamily,
                fontSize = com.astro.vajra.ui.theme.NeoVedicFontSizes.S12,
                color = AppTheme.TextMuted,
                lineHeight = 18.sp
            )
        }
    }
}

@Composable
private fun AvasthaLoadingContent(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            CircularProgressIndicator(color = AppTheme.AccentGold)
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = stringResource(StringKeyDosha.AVASTHA_ANALYZING),
                fontFamily = PoppinsFontFamily,
                fontSize = com.astro.vajra.ui.theme.NeoVedicFontSizes.S14,
                color = AppTheme.TextMuted
            )
        }
    }
}

@Composable
private fun AvasthaEmptyContent(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(32.dp)
        ) {
            Icon(
                imageVector = Icons.Outlined.Psychology,
                contentDescription = null,
                tint = AppTheme.TextMuted,
                modifier = Modifier.size(64.dp)
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = stringResource(StringKeyDosha.UI_NO_CHART_DATA),
                fontFamily = CinzelDecorativeFamily,
                fontSize = com.astro.vajra.ui.theme.NeoVedicFontSizes.S16,
                fontWeight = FontWeight.SemiBold,
                color = AppTheme.TextPrimary
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = stringResource(StringKeyDosha.AVASTHA_NO_CHART_DESC),
                fontFamily = PoppinsFontFamily,
                fontSize = com.astro.vajra.ui.theme.NeoVedicFontSizes.S14,
                color = AppTheme.TextMuted,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
private fun AvasthaInfoDialog(onDismiss: () -> Unit) {
    androidx.compose.material3.AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = stringResource(StringKeyDosha.AVASTHA_ABOUT_TITLE),
                fontFamily = CinzelDecorativeFamily,
                fontSize = com.astro.vajra.ui.theme.NeoVedicFontSizes.S18,
                fontWeight = FontWeight.SemiBold,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
        },
        text = {
            Text(
                text = stringResource(StringKeyDosha.AVASTHA_ABOUT_DESC),
                fontFamily = PoppinsFontFamily,
                fontSize = com.astro.vajra.ui.theme.NeoVedicFontSizes.S14,
                color = AppTheme.TextSecondary,
                lineHeight = 22.sp
            )
        },
        confirmButton = {
            androidx.compose.material3.TextButton(onClick = onDismiss) {
                Text(stringResource(StringKeyDosha.BTN_GOT_IT), color = AppTheme.AccentGold, fontFamily = SpaceGroteskFamily)
            }
        },
        containerColor = AppTheme.CardBackground
    )
}

// Helper functions for colors
@Composable
private fun getStrengthColor(strength: Int): Color {
    return when {
        strength >= 70 -> AppTheme.SuccessColor
        strength >= 50 -> AppTheme.AccentGold
        strength >= 30 -> AppTheme.WarningColor
        else -> AppTheme.ErrorColor
    }
}

@Composable
private fun getPlanetColor(planet: com.astro.vajra.core.model.Planet): Color {
    return when (planet) {
        com.astro.vajra.core.model.Planet.SUN -> AppTheme.PlanetSun
        com.astro.vajra.core.model.Planet.MOON -> AppTheme.PlanetMoon
        com.astro.vajra.core.model.Planet.MARS -> AppTheme.PlanetMars
        com.astro.vajra.core.model.Planet.MERCURY -> AppTheme.PlanetMercury
        com.astro.vajra.core.model.Planet.JUPITER -> AppTheme.PlanetJupiter
        com.astro.vajra.core.model.Planet.VENUS -> AppTheme.PlanetVenus
        com.astro.vajra.core.model.Planet.SATURN -> AppTheme.PlanetSaturn
        com.astro.vajra.core.model.Planet.RAHU -> AppTheme.PlanetRahu
        com.astro.vajra.core.model.Planet.KETU -> AppTheme.PlanetKetu
        com.astro.vajra.core.model.Planet.TRUE_NODE -> AppTheme.PlanetRahu
        com.astro.vajra.core.model.Planet.URANUS -> AppTheme.PlanetSaturn
        com.astro.vajra.core.model.Planet.NEPTUNE -> AppTheme.PlanetMoon
        com.astro.vajra.core.model.Planet.PLUTO -> AppTheme.PlanetRahu
    }
}

@Composable
private fun getBaladiColor(avastha: AvasthaCalculator.BaladiAvastha): Color {
    return when (avastha) {
        AvasthaCalculator.BaladiAvastha.YUVA -> AppTheme.SuccessColor
        AvasthaCalculator.BaladiAvastha.KUMARA -> AppTheme.AccentTeal
        AvasthaCalculator.BaladiAvastha.BALA -> AppTheme.AccentGold
        AvasthaCalculator.BaladiAvastha.VRIDDHA -> AppTheme.WarningColor
        AvasthaCalculator.BaladiAvastha.MRITA -> AppTheme.ErrorColor
    }
}

@Composable
private fun getJagradadiColor(avastha: AvasthaCalculator.JagradadiAvastha): Color {
    return when (avastha) {
        AvasthaCalculator.JagradadiAvastha.JAGRAT -> AppTheme.SuccessColor
        AvasthaCalculator.JagradadiAvastha.SWAPNA -> AppTheme.AccentGold
        AvasthaCalculator.JagradadiAvastha.SUSHUPTI -> AppTheme.ErrorColor
    }
}

@Composable
private fun getDeeptadiColor(avastha: AvasthaCalculator.DeeptadiAvastha): Color {
    return when (avastha) {
        AvasthaCalculator.DeeptadiAvastha.DEEPTA -> AppTheme.SuccessColor
        AvasthaCalculator.DeeptadiAvastha.SWASTHA -> AppTheme.AccentTeal
        AvasthaCalculator.DeeptadiAvastha.MUDITA -> AppTheme.AccentGold
        AvasthaCalculator.DeeptadiAvastha.SHANTA -> AppTheme.AccentPrimary
        AvasthaCalculator.DeeptadiAvastha.DINA -> AppTheme.TextMuted
        AvasthaCalculator.DeeptadiAvastha.VIKALA -> AppTheme.WarningColor
        AvasthaCalculator.DeeptadiAvastha.KHALA -> AppTheme.ErrorColor
        AvasthaCalculator.DeeptadiAvastha.KOPA -> AppTheme.PlanetMars
        AvasthaCalculator.DeeptadiAvastha.BHITA -> AppTheme.ErrorColor
    }
}

@Composable
private fun getLajjitadiColor(avastha: AvasthaCalculator.LajjitadiAvastha): Color {
    return when (avastha) {
        AvasthaCalculator.LajjitadiAvastha.GARVITA -> AppTheme.SuccessColor
        AvasthaCalculator.LajjitadiAvastha.MUDITA -> AppTheme.AccentTeal
        AvasthaCalculator.LajjitadiAvastha.LAJJITA -> AppTheme.WarningColor
        AvasthaCalculator.LajjitadiAvastha.KSHUDITA -> AppTheme.PlanetMars
        AvasthaCalculator.LajjitadiAvastha.TRUSHITA -> AppTheme.AccentPrimary
        AvasthaCalculator.LajjitadiAvastha.KSHOBHITA -> AppTheme.ErrorColor
    }
}
