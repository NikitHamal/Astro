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
import androidx.compose.material.icons.outlined.Analytics
import androidx.compose.material.icons.outlined.AutoAwesome
import androidx.compose.material.icons.outlined.BarChart
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.Lightbulb
import androidx.compose.material.icons.outlined.Psychology
import androidx.compose.material.icons.outlined.Schedule
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material.icons.outlined.TrendingDown
import androidx.compose.material.icons.outlined.TrendingUp
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
import com.astro.vajra.core.common.getLocalizedName
import com.astro.vajra.data.localization.stringResource
import com.astro.vajra.core.model.VedicChart
import com.astro.vajra.ephemeris.IshtaKashtaPhalaCalculator
import com.astro.vajra.ui.theme.AppTheme
import com.astro.vajra.ui.theme.NeoVedicTokens
import com.astro.vajra.ui.theme.SpaceGroteskFamily
import com.astro.vajra.ui.theme.CinzelDecorativeFamily
import com.astro.vajra.ui.theme.PoppinsFontFamily
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.foundation.BorderStroke

/**
 * Ishta Kashta Phala Screen
 *
 * Displays comprehensive analysis of planetary benefic (Ishta) and malefic (Kashta) results
 * based on classical Shadbala principles from BPHS.
 *
 * Features:
 * - Overall benefic/malefic disposition
 * - Per-planet Ishta and Kashta Phala values
 * - Life area impact analysis
 * - Period-based predictions
 * - Personalized recommendations
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun IshtaKashtaPhalaScreen(
    chart: VedicChart?,
    onBack: () -> Unit
) {
    val language = LocalLanguage.current
    var selectedTab by rememberSaveable { mutableIntStateOf(0) }
    var isCalculating by remember { mutableStateOf(true) }
    var analysis by remember { mutableStateOf<IshtaKashtaPhalaCalculator.IshtaKashtaAnalysis?>(null) }
    var showInfoDialog by remember { mutableStateOf(false) }

    val tabs = listOf(
        stringResource(StringKeyDosha.UI_OVERVIEW),
        stringResource(StringKeyDosha.ISHTA_KASHTA_TAB_PLANETS),
        stringResource(StringKeyDosha.ISHTA_KASHTA_TAB_LIFE_AREAS),
        stringResource(StringKeyDosha.UI_TIMING),
        stringResource(StringKeyDosha.UI_RECOMMENDATIONS)
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
                IshtaKashtaPhalaCalculator.analyzeIshtaKashtaPhala(chart)
            }
        } catch (_: Exception) { }
        isCalculating = false
    }

    Scaffold(
        containerColor = AppTheme.ScreenBackground,
        topBar = {
            NeoVedicPageHeader(
                title = stringResource(StringKeyDosha.ISHTA_KASHTA_SCREEN_TITLE),
                subtitle = stringResource(StringKeyDosha.ISHTA_KASHTA_SUBTITLE),
                onBack = onBack,
                actions = {
                    IconButton(onClick = { showInfoDialog = true }) {
                        Icon(
                            imageVector = Icons.Outlined.Info,
                            contentDescription = "Info",
                            tint = AppTheme.TextSecondary
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        when {
            isCalculating -> IshtaKashtaLoadingContent(modifier = Modifier.padding(paddingValues))
            chart == null || analysis == null -> IshtaKashtaEmptyContent(modifier = Modifier.padding(paddingValues))
            else -> {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                        .background(AppTheme.ScreenBackground),
                    contentPadding = PaddingValues(bottom = 32.dp)
                ) {
                    item {
                        IshtaKashtaTabSelector(
                            tabs = tabs,
                            selectedTab = selectedTab,
                            onTabSelected = { selectedTab = it }
                        )
                    }
                    when (selectedTab) {
                        0 -> item { IshtaKashtaOverviewSection(analysis!!) }
                        1 -> item { PlanetaryPhalaSection(analysis!!) }
                        2 -> item { LifeAreaImpactSection(analysis!!) }
                        3 -> item { PeriodPredictionSection(analysis!!) }
                        4 -> item { RecommendationsSection(analysis!!) }
                    }
                }
            }
        }
    }

    if (showInfoDialog) {
        IshtaKashtaInfoDialog(onDismiss = { showInfoDialog = false })
    }
}

@Composable
private fun IshtaKashtaTabSelector(
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
private fun IshtaKashtaOverviewSection(analysis: IshtaKashtaPhalaCalculator.IshtaKashtaAnalysis) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        OverallDispositionCard(analysis)
        IshtaKashtaQuickStats(analysis)
        IshtaKashtaSummaryCard(analysis)
    }
}

@Composable
private fun OverallDispositionCard(analysis: IshtaKashtaPhalaCalculator.IshtaKashtaAnalysis) {
    val categoryColor = getPhalaColor(analysis.overallCategory)
    val isBenefic = analysis.overallNetScore > 0

    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = categoryColor.copy(alpha = 0.1f),
        shape = RoundedCornerShape(com.astro.vajra.ui.theme.NeoVedicTokens.CardCornerRadius),
        border = BorderStroke(com.astro.vajra.ui.theme.NeoVedicTokens.BorderWidth, AppTheme.BorderColor)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .size(72.dp)
                    .clip(CircleShape)
                    .background(categoryColor.copy(alpha = 0.15f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = if (isBenefic) Icons.Outlined.TrendingUp else Icons.Outlined.TrendingDown,
                    contentDescription = null,
                    tint = categoryColor,
                    modifier = Modifier.size(40.dp)
                )
            }
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = analysis.overallCategory.displayName,
                fontFamily = CinzelDecorativeFamily,
                fontSize = com.astro.vajra.ui.theme.NeoVedicFontSizes.S20,
                fontWeight = FontWeight.Bold,
                color = categoryColor
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = analysis.overallCategory.description,
                fontFamily = PoppinsFontFamily,
                fontSize = com.astro.vajra.ui.theme.NeoVedicFontSizes.S14,
                color = AppTheme.TextSecondary,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(16.dp))

            // Ishta vs Kashta bar
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = stringResource(StringKeyDosha.ISHTA_KASHTA_ISHTA).uppercase(),
                    fontFamily = SpaceGroteskFamily,
                    fontSize = com.astro.vajra.ui.theme.NeoVedicFontSizes.S10,
                    letterSpacing = 1.sp,
                    color = AppTheme.SuccessColor,
                    fontWeight = FontWeight.SemiBold
                )
                Text(
                    text = stringResource(StringKeyDosha.ISHTA_KASHTA_KASHTA).uppercase(),
                    fontFamily = SpaceGroteskFamily,
                    fontSize = com.astro.vajra.ui.theme.NeoVedicFontSizes.S10,
                    letterSpacing = 1.sp,
                    color = AppTheme.ErrorColor,
                    fontWeight = FontWeight.SemiBold
                )
            }
            Spacer(modifier = Modifier.height(4.dp))

            val ishtaRatio = (analysis.overallIshtaScore / (analysis.overallIshtaScore + analysis.overallKashtaScore)).coerceIn(0.0, 1.0).toFloat()
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(12.dp)
                    .clip(RoundedCornerShape(com.astro.vajra.ui.theme.NeoVedicTokens.ElementCornerRadius))
                    .background(AppTheme.ErrorColor.copy(alpha = 0.3f))
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth(ishtaRatio)
                        .height(12.dp)
                        .clip(RoundedCornerShape(com.astro.vajra.ui.theme.NeoVedicTokens.ElementCornerRadius))
                        .background(AppTheme.SuccessColor)
                )
            }
            Spacer(modifier = Modifier.height(4.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = String.format("%.1f", analysis.overallIshtaScore),
                    fontFamily = SpaceGroteskFamily,
                    fontSize = com.astro.vajra.ui.theme.NeoVedicFontSizes.S10,
                    color = AppTheme.TextMuted
                )
                Text(
                    text = String.format("%.1f", analysis.overallKashtaScore),
                    fontFamily = SpaceGroteskFamily,
                    fontSize = com.astro.vajra.ui.theme.NeoVedicFontSizes.S10,
                    color = AppTheme.TextMuted
                )
            }
        }
    }
}

@Composable
private fun IshtaKashtaQuickStats(analysis: IshtaKashtaPhalaCalculator.IshtaKashtaAnalysis) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        IshtaKashtaStatCard(
            title = stringResource(StringKeyDosha.ISHTA_KASHTA_BENEFIC),
            value = "${analysis.beneficPlanets.size}",
            color = AppTheme.SuccessColor,
            modifier = Modifier.weight(1f)
        )
        IshtaKashtaStatCard(
            title = stringResource(StringKeyDosha.ISHTA_KASHTA_NEUTRAL),
            value = "${analysis.neutralPlanets.size}",
            color = AppTheme.AccentGold,
            modifier = Modifier.weight(1f)
        )
        IshtaKashtaStatCard(
            title = stringResource(StringKeyDosha.ISHTA_KASHTA_MALEFIC),
            value = "${analysis.maleficPlanets.size}",
            color = AppTheme.ErrorColor,
            modifier = Modifier.weight(1f)
        )
    }
}

@Composable
private fun IshtaKashtaStatCard(
    title: String,
    value: String,
    color: Color,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier,
        color = AppTheme.CardBackground,
        shape = RoundedCornerShape(com.astro.vajra.ui.theme.NeoVedicTokens.CardCornerRadius),
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
                color = AppTheme.TextMuted,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
private fun IshtaKashtaSummaryCard(analysis: IshtaKashtaPhalaCalculator.IshtaKashtaAnalysis) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = AppTheme.CardBackground,
        shape = RoundedCornerShape(com.astro.vajra.ui.theme.NeoVedicTokens.CardCornerRadius),
        border = BorderStroke(com.astro.vajra.ui.theme.NeoVedicTokens.BorderWidth, AppTheme.BorderColor)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Icon(
                    imageVector = Icons.Outlined.Psychology,
                    contentDescription = null,
                    tint = AppTheme.AccentPrimary,
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
                text = analysis.summary,
                fontFamily = PoppinsFontFamily,
                fontSize = com.astro.vajra.ui.theme.NeoVedicFontSizes.S14,
                color = AppTheme.TextSecondary,
                lineHeight = 24.sp
            )

            Spacer(modifier = Modifier.height(12.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Surface(
                    shape = RoundedCornerShape(com.astro.vajra.ui.theme.NeoVedicTokens.ElementCornerRadius),
                    color = AppTheme.SuccessColor.copy(alpha = 0.1f),
                    border = BorderStroke(NeoVedicTokens.ThinBorderWidth, AppTheme.SuccessColor.copy(alpha = 0.2f)),
                    modifier = Modifier.weight(1f)
                ) {
                    Column(
                        modifier = Modifier.padding(12.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = analysis.mostBeneficPlanet.symbol,
                            fontFamily = CinzelDecorativeFamily,
                            fontSize = com.astro.vajra.ui.theme.NeoVedicFontSizes.S24,
                            color = AppTheme.SuccessColor
                        )
                        Text(
                            text = stringResource(StringKeyDosha.ISHTA_KASHTA_MOST_BENEFIC).uppercase(),
                            fontFamily = SpaceGroteskFamily,
                            fontSize = com.astro.vajra.ui.theme.NeoVedicFontSizes.S10,
                            color = AppTheme.SuccessColor,
                            textAlign = TextAlign.Center
                        )
                    }
                }
                Surface(
                    shape = RoundedCornerShape(com.astro.vajra.ui.theme.NeoVedicTokens.ElementCornerRadius),
                    color = AppTheme.ErrorColor.copy(alpha = 0.1f),
                    border = BorderStroke(NeoVedicTokens.ThinBorderWidth, AppTheme.ErrorColor.copy(alpha = 0.2f)),
                    modifier = Modifier.weight(1f)
                ) {
                    Column(
                        modifier = Modifier.padding(12.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = analysis.mostMaleficPlanet.symbol,
                            fontFamily = CinzelDecorativeFamily,
                            fontSize = com.astro.vajra.ui.theme.NeoVedicFontSizes.S24,
                            color = AppTheme.ErrorColor
                        )
                        Text(
                            text = stringResource(StringKeyDosha.ISHTA_KASHTA_MOST_MALEFIC).uppercase(),
                            fontFamily = SpaceGroteskFamily,
                            fontSize = com.astro.vajra.ui.theme.NeoVedicFontSizes.S10,
                            color = AppTheme.ErrorColor,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun PlanetaryPhalaSection(analysis: IshtaKashtaPhalaCalculator.IshtaKashtaAnalysis) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        analysis.getPlanetsByBenefit().forEach { phala ->
            PlanetPhalaCard(phala)
        }
    }
}

@Composable
private fun PlanetPhalaCard(phala: IshtaKashtaPhalaCalculator.PlanetaryPhala) {
    val language = LocalLanguage.current
    var expanded by remember { mutableStateOf(false) }
    val categoryColor = getPhalaColor(phala.category)

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .animateContentSize(),
        color = AppTheme.CardBackground,
        shape = RoundedCornerShape(com.astro.vajra.ui.theme.NeoVedicTokens.ElementCornerRadius),
        border = BorderStroke(NeoVedicTokens.BorderWidth, AppTheme.BorderColor)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
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
                            .background(categoryColor.copy(alpha = 0.15f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = phala.planet.symbol,
                            fontFamily = CinzelDecorativeFamily,
                            fontSize = com.astro.vajra.ui.theme.NeoVedicFontSizes.S24,
                            color = categoryColor
                        )
                    }
                    Column {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Text(
                                text = phala.planet.displayName,
                                fontFamily = CinzelDecorativeFamily,
                                fontSize = com.astro.vajra.ui.theme.NeoVedicFontSizes.S16,
                                fontWeight = FontWeight.SemiBold,
                                color = AppTheme.TextPrimary
                            )
                            if (phala.isExalted) {
                                Surface(
                                    shape = RoundedCornerShape(com.astro.vajra.ui.theme.NeoVedicTokens.ElementCornerRadius),
                                    color = AppTheme.SuccessColor.copy(alpha = 0.15f)
                                ) {
                                    Text(
                                        text = "\u2191",
                                        fontFamily = SpaceGroteskFamily,
                                        fontSize = com.astro.vajra.ui.theme.NeoVedicFontSizes.S10,
                                        color = AppTheme.SuccessColor,
                                        modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp)
                                    )
                                }
                            }
                            if (phala.isDebilitated) {
                                Surface(
                                    shape = RoundedCornerShape(com.astro.vajra.ui.theme.NeoVedicTokens.ElementCornerRadius),
                                    color = AppTheme.ErrorColor.copy(alpha = 0.15f)
                                ) {
                                    Text(
                                        text = "\u2193",
                                        fontFamily = SpaceGroteskFamily,
                                        fontSize = com.astro.vajra.ui.theme.NeoVedicFontSizes.S10,
                                        color = AppTheme.ErrorColor,
                                        modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp)
                                    )
                                }
                            }
                            if (phala.isRetrograde) {
                                Surface(
                                    shape = RoundedCornerShape(com.astro.vajra.ui.theme.NeoVedicTokens.ElementCornerRadius),
                                    color = AppTheme.AccentPrimary.copy(alpha = 0.15f)
                                ) {
                                    Text(
                                        text = "R",
                                        fontFamily = SpaceGroteskFamily,
                                        fontSize = com.astro.vajra.ui.theme.NeoVedicFontSizes.S10,
                                        color = AppTheme.AccentPrimary,
                                        modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp)
                                    )
                                }
                            }
                        }
                        Text(
                            text = "${phala.sign.getLocalizedName(language)} \u2022 H${phala.house}",
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
                    Surface(
                        shape = RoundedCornerShape(com.astro.vajra.ui.theme.NeoVedicTokens.ElementCornerRadius),
                        color = categoryColor.copy(alpha = 0.15f)
                    ) {
                        Text(
                            text = phala.getFormattedNet(),
                            fontFamily = SpaceGroteskFamily,
                            fontSize = com.astro.vajra.ui.theme.NeoVedicFontSizes.S12,
                            fontWeight = FontWeight.SemiBold,
                            color = categoryColor,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                        )
                    }
                    Icon(
                        imageVector = if (expanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                        contentDescription = null,
                        tint = AppTheme.TextMuted
                    )
                }
            }

            // Mini Ishta/Kashta bar
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "I: ${phala.getFormattedIshta()}",
                    fontFamily = SpaceGroteskFamily,
                    fontSize = com.astro.vajra.ui.theme.NeoVedicFontSizes.S10,
                    color = AppTheme.SuccessColor
                )
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .height(6.dp)
                        .clip(RoundedCornerShape(com.astro.vajra.ui.theme.NeoVedicTokens.ElementCornerRadius))
                        .background(AppTheme.ErrorColor.copy(alpha = 0.2f))
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth((phala.ishtaPercentage / 100.0).toFloat())
                            .height(6.dp)
                            .clip(RoundedCornerShape(com.astro.vajra.ui.theme.NeoVedicTokens.ElementCornerRadius))
                            .background(AppTheme.SuccessColor)
                    )
                }
                Text(
                    text = "K: ${phala.getFormattedKashta()}",
                    fontFamily = SpaceGroteskFamily,
                    fontSize = com.astro.vajra.ui.theme.NeoVedicFontSizes.S10,
                    color = AppTheme.ErrorColor
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

                    // Strength values
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Surface(
                            shape = RoundedCornerShape(com.astro.vajra.ui.theme.NeoVedicTokens.ElementCornerRadius),
                            color = AppTheme.ChipBackground,
                            modifier = Modifier.weight(1f)
                        ) {
                            Column(
                                modifier = Modifier.padding(8.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(
                                    text = stringResource(StringKeyDosha.ISHTA_KASHTA_UCCHA_BALA).uppercase(),
                                    fontFamily = SpaceGroteskFamily,
                                    fontSize = com.astro.vajra.ui.theme.NeoVedicFontSizes.S10,
                                    color = AppTheme.TextMuted
                                )
                                Text(
                                    text = String.format("%.1f", phala.ucchaBala),
                                    fontFamily = SpaceGroteskFamily,
                                    fontSize = com.astro.vajra.ui.theme.NeoVedicFontSizes.S14,
                                    fontWeight = FontWeight.SemiBold,
                                    color = AppTheme.TextPrimary
                                )
                            }
                        }
                        Surface(
                            shape = RoundedCornerShape(com.astro.vajra.ui.theme.NeoVedicTokens.ElementCornerRadius),
                            color = AppTheme.ChipBackground,
                            modifier = Modifier.weight(1f)
                        ) {
                            Column(
                                modifier = Modifier.padding(8.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(
                                    text = stringResource(StringKeyDosha.ISHTA_KASHTA_CHESTA_BALA).uppercase(),
                                    fontFamily = SpaceGroteskFamily,
                                    fontSize = com.astro.vajra.ui.theme.NeoVedicFontSizes.S10,
                                    color = AppTheme.TextMuted
                                )
                                Text(
                                    text = String.format("%.1f", phala.chestaBala),
                                    fontFamily = SpaceGroteskFamily,
                                    fontSize = com.astro.vajra.ui.theme.NeoVedicFontSizes.S14,
                                    fontWeight = FontWeight.SemiBold,
                                    color = AppTheme.TextPrimary
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = phala.interpretation,
                        fontFamily = PoppinsFontFamily,
                        fontSize = com.astro.vajra.ui.theme.NeoVedicFontSizes.S12,
                        color = AppTheme.TextSecondary,
                        lineHeight = 20.sp
                    )

                    if (phala.affectedAreas.isNotEmpty()) {
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = stringResource(StringKeyDosha.ISHTA_KASHTA_AFFECTS).uppercase(),
                            fontFamily = SpaceGroteskFamily,
                            fontSize = com.astro.vajra.ui.theme.NeoVedicFontSizes.S10,
                            fontWeight = FontWeight.SemiBold,
                            color = AppTheme.TextPrimary
                        )
                        Row(
                            modifier = Modifier.padding(top = 4.dp),
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            phala.affectedAreas.take(3).forEach { area ->
                                Surface(
                                    shape = RoundedCornerShape(com.astro.vajra.ui.theme.NeoVedicTokens.ElementCornerRadius),
                                    color = categoryColor.copy(alpha = 0.1f)
                                ) {
                                    Text(
                                        text = area.displayName,
                                        fontFamily = PoppinsFontFamily,
                                        fontSize = com.astro.vajra.ui.theme.NeoVedicFontSizes.S10,
                                        color = categoryColor,
                                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun LifeAreaImpactSection(analysis: IshtaKashtaPhalaCalculator.IshtaKashtaAnalysis) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Surface(
            modifier = Modifier.fillMaxWidth(),
            color = AppTheme.CardBackground,
            shape = RoundedCornerShape(com.astro.vajra.ui.theme.NeoVedicTokens.CardCornerRadius),
            border = BorderStroke(NeoVedicTokens.BorderWidth, AppTheme.BorderColor)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(
                        imageVector = Icons.Outlined.BarChart,
                        contentDescription = null,
                        tint = AppTheme.AccentPrimary,
                        modifier = Modifier.size(20.dp)
                    )
                    Text(
                        text = stringResource(StringKeyDosha.ISHTA_KASHTA_LIFE_AREAS),
                        fontFamily = CinzelDecorativeFamily,
                        fontSize = com.astro.vajra.ui.theme.NeoVedicFontSizes.S16,
                        fontWeight = FontWeight.SemiBold,
                        color = AppTheme.TextPrimary
                    )
                }
                Spacer(modifier = Modifier.height(12.dp))

                analysis.lifeAreaImpacts.forEach { impact ->
                    LifeAreaImpactRow(impact)
                }
            }
        }
    }
}

@Composable
private fun LifeAreaImpactRow(impact: IshtaKashtaPhalaCalculator.LifeAreaImpact) {
    val scoreColor = when {
        impact.netScore > 3.0 -> AppTheme.SuccessColor
        impact.netScore > 1.0 -> AppTheme.AccentTeal
        impact.netScore > -1.0 -> AppTheme.AccentGold
        impact.netScore > -3.0 -> AppTheme.WarningColor
        else -> AppTheme.ErrorColor
    }

    var expanded by remember { mutableStateOf(false) }

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
            .clickable { expanded = !expanded },
        shape = RoundedCornerShape(com.astro.vajra.ui.theme.NeoVedicTokens.ElementCornerRadius),
        color = AppTheme.CardBackgroundElevated,
        border = BorderStroke(NeoVedicTokens.ThinBorderWidth, AppTheme.BorderColor)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp)
                .animateContentSize()
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = impact.area.displayName,
                    fontFamily = PoppinsFontFamily,
                    fontSize = com.astro.vajra.ui.theme.NeoVedicFontSizes.S14,
                    fontWeight = FontWeight.Medium,
                    color = AppTheme.TextPrimary,
                    modifier = Modifier.weight(1f)
                )
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Surface(
                        shape = RoundedCornerShape(com.astro.vajra.ui.theme.NeoVedicTokens.ElementCornerRadius),
                        color = scoreColor.copy(alpha = 0.15f)
                    ) {
                        Text(
                            text = String.format("%+.1f", impact.netScore),
                            fontFamily = SpaceGroteskFamily,
                            fontSize = com.astro.vajra.ui.theme.NeoVedicFontSizes.S12,
                            fontWeight = FontWeight.SemiBold,
                            color = scoreColor,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                        )
                    }
                    Icon(
                        imageVector = if (expanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                        contentDescription = null,
                        tint = AppTheme.TextMuted,
                        modifier = Modifier.size(16.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(4.dp))
            LinearProgressIndicator(
                progress = { ((impact.netScore + 8) / 16.0).coerceIn(0.0, 1.0).toFloat() },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(4.dp)
                    .clip(RoundedCornerShape(com.astro.vajra.ui.theme.NeoVedicTokens.ElementCornerRadius)),
                color = scoreColor,
                trackColor = scoreColor.copy(alpha = 0.2f),
                strokeCap = StrokeCap.Round
            )

            AnimatedVisibility(visible = expanded) {
                Column(modifier = Modifier.padding(top = 8.dp)) {
                    Text(
                        text = impact.prediction,
                        fontFamily = PoppinsFontFamily,
                        fontSize = com.astro.vajra.ui.theme.NeoVedicFontSizes.S12,
                        color = AppTheme.TextSecondary
                    )
                    if (impact.dominantPlanets.isNotEmpty()) {
                        Spacer(modifier = Modifier.height(4.dp))
                        Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                            Text(
                                text = stringResource(StringKeyDosha.ISHTA_KASHTA_INFLUENCED_BY).uppercase(),
                                fontFamily = SpaceGroteskFamily,
                                fontSize = com.astro.vajra.ui.theme.NeoVedicFontSizes.S10,
                                color = AppTheme.TextMuted
                            )
                            impact.dominantPlanets.take(3).forEach { planet ->
                                Text(
                                    text = planet.symbol,
                                    fontFamily = CinzelDecorativeFamily,
                                    fontSize = com.astro.vajra.ui.theme.NeoVedicFontSizes.S14,
                                    color = scoreColor
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun PeriodPredictionSection(analysis: IshtaKashtaPhalaCalculator.IshtaKashtaAnalysis) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Surface(
            modifier = Modifier.fillMaxWidth(),
            color = AppTheme.CardBackground,
            shape = RoundedCornerShape(com.astro.vajra.ui.theme.NeoVedicTokens.CardCornerRadius),
            border = BorderStroke(NeoVedicTokens.BorderWidth, AppTheme.BorderColor)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Schedule,
                        contentDescription = null,
                        tint = AppTheme.AccentGold,
                        modifier = Modifier.size(20.dp)
                    )
                    Text(
                        text = stringResource(StringKeyDosha.ISHTA_KASHTA_PERIOD_PREDICTIONS),
                        fontFamily = CinzelDecorativeFamily,
                        fontSize = com.astro.vajra.ui.theme.NeoVedicFontSizes.S16,
                        fontWeight = FontWeight.SemiBold,
                        color = AppTheme.TextPrimary
                    )
                }
                Spacer(modifier = Modifier.height(12.dp))

                analysis.periodPredictions.forEach { period ->
                    PeriodPredictionCard(period)
                }
            }
        }
    }
}

@Composable
private fun PeriodPredictionCard(period: IshtaKashtaPhalaCalculator.PhalaPeriod) {
    var expanded by remember { mutableStateOf(false) }
    val netStrength = period.ishtaStrength - period.kashtaStrength
    val trendColor = when {
        netStrength > 3.0 -> AppTheme.SuccessColor
        netStrength > 1.0 -> AppTheme.AccentTeal
        netStrength > -1.0 -> AppTheme.AccentGold
        netStrength > -3.0 -> AppTheme.WarningColor
        else -> AppTheme.ErrorColor
    }

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
            .clickable { expanded = !expanded },
        shape = RoundedCornerShape(com.astro.vajra.ui.theme.NeoVedicTokens.ElementCornerRadius),
        color = AppTheme.CardBackgroundElevated,
        border = BorderStroke(NeoVedicTokens.ThinBorderWidth, AppTheme.BorderColor)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp)
                .animateContentSize()
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = period.planet.symbol,
                        fontFamily = CinzelDecorativeFamily,
                        fontSize = com.astro.vajra.ui.theme.NeoVedicFontSizes.S20,
                        color = trendColor
                    )
                    Column {
                        Text(
                            text = period.periodName,
                            fontFamily = PoppinsFontFamily,
                            fontSize = com.astro.vajra.ui.theme.NeoVedicFontSizes.S14,
                            fontWeight = FontWeight.Medium,
                            color = AppTheme.TextPrimary
                        )
                        Text(
                            text = period.overallTendency.uppercase(),
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

            AnimatedVisibility(visible = expanded) {
                Column(modifier = Modifier.padding(top = 8.dp)) {
                    HorizontalDivider(color = AppTheme.DividerColor)
                    Spacer(modifier = Modifier.height(8.dp))

                    if (period.expectedBenefits.isNotEmpty()) {
                        Text(
                            text = stringResource(StringKeyDosha.ISHTA_KASHTA_BENEFITS).uppercase(),
                            fontFamily = SpaceGroteskFamily,
                            fontSize = com.astro.vajra.ui.theme.NeoVedicFontSizes.S10,
                            fontWeight = FontWeight.SemiBold,
                            color = AppTheme.SuccessColor
                        )
                        period.expectedBenefits.forEach { benefit ->
                            Text(
                                text = "\u2022 $benefit",
                                fontFamily = PoppinsFontFamily,
                                fontSize = com.astro.vajra.ui.theme.NeoVedicFontSizes.S12,
                                color = AppTheme.TextSecondary
                            )
                        }
                    }

                    if (period.expectedChallenges.isNotEmpty()) {
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = stringResource(StringKeyDosha.ISHTA_KASHTA_CHALLENGES).uppercase(),
                            fontFamily = SpaceGroteskFamily,
                            fontSize = com.astro.vajra.ui.theme.NeoVedicFontSizes.S10,
                            fontWeight = FontWeight.SemiBold,
                            color = AppTheme.WarningColor
                        )
                        period.expectedChallenges.forEach { challenge ->
                            Text(
                                text = "\u2022 $challenge",
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
}

@Composable
private fun RecommendationsSection(analysis: IshtaKashtaPhalaCalculator.IshtaKashtaAnalysis) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Surface(
            modifier = Modifier.fillMaxWidth(),
            color = AppTheme.CardBackground,
            shape = RoundedCornerShape(com.astro.vajra.ui.theme.NeoVedicTokens.CardCornerRadius),
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
                        text = stringResource(StringKeyDosha.UI_RECOMMENDATIONS),
                        fontFamily = CinzelDecorativeFamily,
                        fontSize = com.astro.vajra.ui.theme.NeoVedicFontSizes.S16,
                        fontWeight = FontWeight.SemiBold,
                        color = AppTheme.TextPrimary
                    )
                }
                Spacer(modifier = Modifier.height(12.dp))

                analysis.recommendations.forEachIndexed { index, recommendation ->
                    Surface(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp),
                        shape = RoundedCornerShape(com.astro.vajra.ui.theme.NeoVedicTokens.ElementCornerRadius),
                        color = AppTheme.AccentGold.copy(alpha = 0.08f),
                        border = BorderStroke(NeoVedicTokens.ThinBorderWidth, AppTheme.AccentGold.copy(alpha = 0.2f))
                    ) {
                        Row(
                            modifier = Modifier.padding(12.dp),
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(24.dp)
                                    .clip(CircleShape)
                                    .background(AppTheme.AccentGold.copy(alpha = 0.2f)),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = "${index + 1}",
                                    fontFamily = SpaceGroteskFamily,
                                    fontSize = com.astro.vajra.ui.theme.NeoVedicFontSizes.S12,
                                    fontWeight = FontWeight.Bold,
                                    color = AppTheme.AccentGold
                                )
                            }
                            Text(
                                text = recommendation,
                                fontFamily = PoppinsFontFamily,
                                fontSize = com.astro.vajra.ui.theme.NeoVedicFontSizes.S12,
                                color = AppTheme.TextSecondary,
                                modifier = Modifier.weight(1f)
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun getPhalaColor(category: IshtaKashtaPhalaCalculator.PhalaCategory): Color {
    return when (category) {
        IshtaKashtaPhalaCalculator.PhalaCategory.HIGHLY_BENEFIC -> AppTheme.SuccessColor
        IshtaKashtaPhalaCalculator.PhalaCategory.BENEFIC -> AppTheme.AccentTeal
        IshtaKashtaPhalaCalculator.PhalaCategory.MODERATELY_BENEFIC -> AppTheme.AccentPrimary
        IshtaKashtaPhalaCalculator.PhalaCategory.NEUTRAL -> AppTheme.AccentGold
        IshtaKashtaPhalaCalculator.PhalaCategory.MODERATELY_MALEFIC -> AppTheme.WarningColor
        IshtaKashtaPhalaCalculator.PhalaCategory.MALEFIC -> AppTheme.ErrorColor
        IshtaKashtaPhalaCalculator.PhalaCategory.HIGHLY_MALEFIC -> AppTheme.ErrorColor
    }
}

@Composable
private fun IshtaKashtaLoadingContent(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            CircularProgressIndicator(color = AppTheme.AccentPrimary)
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = stringResource(StringKeyDosha.ISHTA_KASHTA_ANALYZING),
                fontFamily = PoppinsFontFamily,
                fontSize = com.astro.vajra.ui.theme.NeoVedicFontSizes.S14,
                color = AppTheme.TextMuted
            )
        }
    }
}

@Composable
private fun IshtaKashtaEmptyContent(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(32.dp)
        ) {
            Icon(
                imageVector = Icons.Outlined.Analytics,
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
                text = stringResource(StringKeyDosha.ISHTA_KASHTA_NO_CHART_DESC),
                fontFamily = PoppinsFontFamily,
                fontSize = com.astro.vajra.ui.theme.NeoVedicFontSizes.S14,
                color = AppTheme.TextMuted,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
private fun IshtaKashtaInfoDialog(onDismiss: () -> Unit) {
    androidx.compose.material3.AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = stringResource(StringKeyDosha.ISHTA_KASHTA_ABOUT_TITLE),
                fontFamily = CinzelDecorativeFamily,
                fontSize = com.astro.vajra.ui.theme.NeoVedicFontSizes.S18,
                fontWeight = FontWeight.SemiBold,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
        },
        text = {
            Text(
                text = stringResource(StringKeyDosha.ISHTA_KASHTA_ABOUT_DESC),
                fontFamily = PoppinsFontFamily,
                fontSize = com.astro.vajra.ui.theme.NeoVedicFontSizes.S14,
                color = AppTheme.TextSecondary,
                lineHeight = 22.sp
            )
        },
        confirmButton = {
            androidx.compose.material3.TextButton(onClick = onDismiss) {
                Text(stringResource(StringKeyDosha.BTN_GOT_IT), color = AppTheme.AccentPrimary, fontFamily = SpaceGroteskFamily)
            }
        },
        containerColor = AppTheme.CardBackground
    )
}
