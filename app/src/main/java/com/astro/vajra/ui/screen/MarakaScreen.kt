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
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material.icons.outlined.AccessTime
import androidx.compose.material.icons.outlined.Favorite
import androidx.compose.material.icons.outlined.HealthAndSafety
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.Psychology
import androidx.compose.material.icons.outlined.Shield
import androidx.compose.material.icons.outlined.Spa
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
import com.astro.vajra.core.common.StringKeyAnalysis
import com.astro.vajra.core.common.StringKeyRemedy
import com.astro.vajra.core.common.StringKeyDosha
import com.astro.vajra.core.common.getLocalizedName
import com.astro.vajra.data.localization.stringResource
import com.astro.vajra.core.model.VedicChart
import com.astro.vajra.ephemeris.MarakaCalculator
import com.astro.vajra.ui.theme.AppTheme
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import androidx.compose.ui.text.style.TextOverflow

import com.astro.vajra.ui.theme.CinzelDecorativeFamily
import com.astro.vajra.ui.theme.PoppinsFontFamily
import com.astro.vajra.ui.theme.SpaceGroteskFamily
import com.astro.vajra.ui.theme.NeoVedicTokens
import com.astro.vajra.ui.components.common.vedicCornerMarkers

/**
 * Maraka Analysis Screen
 *
 * Displays comprehensive analysis of Maraka (death-inflicting) planets including:
 * - Longevity assessment based on classical Vedic methods
 * - Primary and secondary Maraka planets identification
 * - Critical Dasha periods for health concerns
 * - Protective factors and remedial measures
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MarakaScreen(
    chart: VedicChart?,
    onBack: () -> Unit
) {
    val language = LocalLanguage.current
    var selectedTab by rememberSaveable { mutableIntStateOf(0) }
    var isCalculating by remember { mutableStateOf(true) }
    var analysis by remember { mutableStateOf<MarakaCalculator.MarakaAnalysis?>(null) }
    var showInfoDialog by remember { mutableStateOf(false) }

    val tabs = listOf(
        stringResource(StringKeyDosha.UI_OVERVIEW),
        stringResource(StringKeyDosha.MARAKA_TAB_PLANETS),
        stringResource(StringKeyDosha.MARAKA_TAB_LONGEVITY),
        stringResource(StringKeyDosha.UI_TIMING),
        stringResource(StringKeyDosha.UI_REMEDIES)
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
                MarakaCalculator.analyzeMaraka(chart)
            }
        } catch (_: Exception) { }
        isCalculating = false
    }

    Scaffold(
        containerColor = AppTheme.ScreenBackground,
        topBar = {
            NeoVedicPageHeader(
                title = stringResource(StringKeyDosha.MARAKA_SCREEN_TITLE),
                subtitle = stringResource(StringKeyDosha.MARAKA_SUBTITLE),
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
            isCalculating -> LoadingContent(modifier = Modifier.padding(paddingValues))
            chart == null || analysis == null -> EmptyContent(modifier = Modifier.padding(paddingValues))
            else -> {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                        .background(AppTheme.ScreenBackground),
                    contentPadding = PaddingValues(bottom = 32.dp)
                ) {
                    item {
                        MarakaTabSelector(
                            tabs = tabs,
                            selectedTab = selectedTab,
                            onTabSelected = { selectedTab = it }
                        )
                    }
                    when (selectedTab) {
                        0 -> item { MarakaOverviewSection(analysis!!) }
                        1 -> item { MarakaPlanetsSection(analysis!!) }
                        2 -> item { LongevitySection(analysis!!) }
                        3 -> item { MarakaDashaSection(analysis!!) }
                        4 -> item { MarakaRemediesSection(analysis!!) }
                    }
                }
            }
        }
    }

    if (showInfoDialog) {
        MarakaInfoDialog(onDismiss = { showInfoDialog = false })
    }
}

@Composable
private fun MarakaTabSelector(
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
                        fontSize = 13.sp,
                        fontFamily = PoppinsFontFamily,
                        fontWeight = if (selectedTab == index) FontWeight.SemiBold else FontWeight.Normal
                    )
                },
                colors = FilterChipDefaults.filterChipColors(
                    selectedContainerColor = AppTheme.WarningColor.copy(alpha = 0.15f),
                    selectedLabelColor = AppTheme.WarningColor,
                    containerColor = AppTheme.CardBackground,
                    labelColor = AppTheme.TextSecondary
                ),
                border = FilterChipDefaults.filterChipBorder(
                    borderColor = AppTheme.BorderColor,
                    selectedBorderColor = AppTheme.WarningColor.copy(alpha = 0.3f),
                    enabled = true,
                    selected = selectedTab == index
                )
            )
        }
    }
}

@Composable
private fun MarakaOverviewSection(analysis: MarakaCalculator.MarakaAnalysis) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        LongevityStatusCard(analysis.longevityAnalysis)
        MarakaQuickStats(analysis)
        MarakaInterpretationCard(analysis)
    }
}

@Composable
private fun LongevityStatusCard(longevity: MarakaCalculator.LongevityAnalysis) {
    val statusColor = when (longevity.category) {
        MarakaCalculator.LongevityCategory.PURNAYU,
        MarakaCalculator.LongevityCategory.AMITAYU -> AppTheme.SuccessColor
        MarakaCalculator.LongevityCategory.MADHYAYU -> AppTheme.AccentGold
        MarakaCalculator.LongevityCategory.ALPAYU -> AppTheme.WarningColor
        MarakaCalculator.LongevityCategory.BALARISHTA -> AppTheme.ErrorColor
    }

    Surface(
        modifier = Modifier.fillMaxWidth().vedicCornerMarkers(color = statusColor, strokeWidth = 1.dp),
        color = statusColor.copy(alpha = 0.1f),
        border = androidx.compose.foundation.BorderStroke(1.dp, statusColor.copy(alpha = 0.2f)),
        shape = RoundedCornerShape(NeoVedicTokens.CardCornerRadius)
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
                    .background(statusColor.copy(alpha = 0.15f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Outlined.Favorite,
                    contentDescription = null,
                    tint = statusColor,
                    modifier = Modifier.size(40.dp)
                )
            }
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = longevity.category.displayName,
                fontSize = 24.sp,
                fontFamily = CinzelDecorativeFamily,
                fontWeight = FontWeight.Bold,
                color = statusColor
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = longevity.category.yearsRange,
                fontSize = 16.sp,
                fontFamily = PoppinsFontFamily,
                color = AppTheme.TextSecondary
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = longevity.interpretation,
                fontSize = 14.sp,
                fontFamily = PoppinsFontFamily,
                color = AppTheme.TextSecondary,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(12.dp))
            LinearProgressIndicator(
                progress = { longevity.overallScore / 100f },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(8.dp)
                    .clip(RoundedCornerShape(NeoVedicTokens.ElementCornerRadius)),
                color = statusColor,
                trackColor = statusColor.copy(alpha = 0.2f),
                strokeCap = StrokeCap.Round
            )
            Text(
                text = "${stringResource(StringKeyDosha.MARAKA_LONGEVITY_SCORE)}: ${longevity.overallScore}%",
                fontSize = 12.sp,
                fontFamily = SpaceGroteskFamily,
                color = AppTheme.TextMuted,
                modifier = Modifier.padding(top = 4.dp)
            )
        }
    }
}

@Composable
private fun MarakaQuickStats(analysis: MarakaCalculator.MarakaAnalysis) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        MarakaStatCard(
            title = stringResource(StringKeyDosha.MARAKA_PRIMARY),
            value = "${analysis.primaryMarakas.size}",
            color = AppTheme.ErrorColor,
            modifier = Modifier.weight(1f)
        )
        MarakaStatCard(
            title = stringResource(StringKeyDosha.MARAKA_SECONDARY),
            value = "${analysis.secondaryMarakas.size}",
            color = AppTheme.WarningColor,
            modifier = Modifier.weight(1f)
        )
        MarakaStatCard(
            title = stringResource(StringKeyDosha.MARAKA_PERIODS),
            value = "${analysis.dashaPeriods.size}",
            color = AppTheme.AccentPrimary,
            modifier = Modifier.weight(1f)
        )
    }
}

@Composable
private fun MarakaStatCard(
    title: String,
    value: String,
    color: Color,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier,
        color = AppTheme.CardBackground,
        border = androidx.compose.foundation.BorderStroke(1.dp, AppTheme.BorderColor),
        shape = RoundedCornerShape(NeoVedicTokens.ElementCornerRadius)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = value,
                fontSize = 24.sp,
                fontFamily = CinzelDecorativeFamily,
                fontWeight = FontWeight.Bold,
                color = color
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = title,
                fontSize = 10.sp,
                fontFamily = SpaceGroteskFamily,
                color = AppTheme.TextMuted,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
private fun MarakaInterpretationCard(analysis: MarakaCalculator.MarakaAnalysis) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = AppTheme.CardBackground,
        border = androidx.compose.foundation.BorderStroke(1.dp, AppTheme.BorderColor),
        shape = RoundedCornerShape(NeoVedicTokens.ElementCornerRadius)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Icon(
                    imageVector = Icons.Outlined.Psychology,
                    contentDescription = null,
                    tint = AppTheme.AccentGold,
                    modifier = Modifier.size(20.dp)
                )
                Text(
                    text = stringResource(StringKeyDosha.UI_INTERPRETATION),
                    fontSize = 16.sp,
                    fontFamily = CinzelDecorativeFamily,
                    fontWeight = FontWeight.SemiBold,
                    color = AppTheme.TextPrimary
                )
            }
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = analysis.summary,
                fontSize = 14.sp,
                fontFamily = PoppinsFontFamily,
                color = AppTheme.TextSecondary,
                lineHeight = 24.sp
            )
            if (analysis.protectiveFactors.isNotEmpty()) {
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = stringResource(StringKeyDosha.MARAKA_PROTECTIVE),
                    fontSize = 12.sp,
                    fontFamily = SpaceGroteskFamily,
                    fontWeight = FontWeight.SemiBold,
                    color = AppTheme.SuccessColor
                )
                Spacer(modifier = Modifier.height(4.dp))
                analysis.protectiveFactors.take(3).forEach { factor ->
                    Row(
                        modifier = Modifier.padding(vertical = 2.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text("\u2713", color = AppTheme.SuccessColor, fontWeight = FontWeight.Bold)
                        Text(
                            text = factor,
                            fontSize = 14.sp,
                            fontFamily = PoppinsFontFamily,
                            color = AppTheme.TextSecondary
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun MarakaPlanetsSection(analysis: MarakaCalculator.MarakaAnalysis) {
    val language = LocalLanguage.current
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        if (analysis.primaryMarakas.isEmpty() && analysis.secondaryMarakas.isEmpty()) {
            Surface(
                modifier = Modifier.fillMaxWidth(),
                color = AppTheme.SuccessColor.copy(alpha = 0.1f),
                border = androidx.compose.foundation.BorderStroke(1.dp, AppTheme.SuccessColor.copy(alpha = 0.2f)),
                shape = RoundedCornerShape(NeoVedicTokens.ElementCornerRadius)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Shield,
                        contentDescription = null,
                        tint = AppTheme.SuccessColor,
                        modifier = Modifier.size(48.dp)
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = stringResource(StringKeyDosha.MARAKA_NO_SIGNIFICANT),
                        fontSize = 16.sp,
                        fontFamily = CinzelDecorativeFamily,
                        fontWeight = FontWeight.SemiBold,
                        color = AppTheme.TextPrimary,
                        textAlign = TextAlign.Center
                    )
                }
            }
        } else {
            if (analysis.primaryMarakas.isNotEmpty()) {
                Text(
                    text = stringResource(StringKeyDosha.MARAKA_PRIMARY_TITLE),
                    fontSize = 14.sp,
                    fontFamily = SpaceGroteskFamily,
                    fontWeight = FontWeight.SemiBold,
                    color = AppTheme.ErrorColor
                )
                analysis.primaryMarakas.forEach { maraka ->
                    MarakaPlanetCard(maraka = maraka, isPrimary = true)
                }
            }
            if (analysis.secondaryMarakas.isNotEmpty()) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = stringResource(StringKeyDosha.MARAKA_SECONDARY_TITLE),
                    fontSize = 14.sp,
                    fontFamily = SpaceGroteskFamily,
                    fontWeight = FontWeight.SemiBold,
                    color = AppTheme.WarningColor
                )
                analysis.secondaryMarakas.forEach { maraka ->
                    MarakaPlanetCard(maraka = maraka, isPrimary = false)
                }
            }
        }
    }
}

@Composable
private fun MarakaPlanetCard(
    maraka: MarakaCalculator.MarakaPlanet,
    isPrimary: Boolean
) {
    val language = LocalLanguage.current
    var expanded by remember { mutableStateOf(false) }
    val severityColor = when (maraka.severity) {
        MarakaCalculator.MarakaSeverity.VERY_HIGH -> AppTheme.ErrorColor
        MarakaCalculator.MarakaSeverity.HIGH -> AppTheme.WarningColor
        MarakaCalculator.MarakaSeverity.MODERATE -> AppTheme.AccentGold
        MarakaCalculator.MarakaSeverity.LOW -> AppTheme.AccentTeal
        MarakaCalculator.MarakaSeverity.MINIMAL -> AppTheme.TextMuted
        MarakaCalculator.MarakaSeverity.NONE -> AppTheme.TextMuted
    }

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .animateContentSize(),
        color = AppTheme.CardBackground,
        border = androidx.compose.foundation.BorderStroke(1.dp, AppTheme.BorderColor),
        shape = RoundedCornerShape(NeoVedicTokens.ElementCornerRadius)
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
                            .size(44.dp)
                            .clip(CircleShape)
                            .background(severityColor.copy(alpha = 0.15f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = maraka.planet.symbol,
                            fontSize = 22.sp,
                            color = severityColor
                        )
                    }
                    Column {
                        Text(
                            text = maraka.planet.displayName,
                            fontSize = 16.sp,
                            fontFamily = CinzelDecorativeFamily,
                            fontWeight = FontWeight.SemiBold,
                            color = AppTheme.TextPrimary
                        )
                        Text(
                            text = "${maraka.position.sign.getLocalizedName(language)} \u2022 H${maraka.position.house}",
                            fontSize = 12.sp,
                            fontFamily = PoppinsFontFamily,
                            color = AppTheme.TextMuted
                        )
                    }
                }
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Surface(
                        shape = RoundedCornerShape(NeoVedicTokens.ElementCornerRadius),
                        color = severityColor.copy(alpha = 0.15f)
                    ) {
                        Text(
                            text = maraka.severity.displayName,
                            fontSize = 10.sp,
                            fontFamily = SpaceGroteskFamily,
                            fontWeight = FontWeight.SemiBold,
                            color = severityColor,
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

            AnimatedVisibility(
                visible = expanded,
                enter = expandVertically() + fadeIn(),
                exit = shrinkVertically() + fadeOut()
            ) {
                Column(modifier = Modifier.padding(top = 12.dp)) {
                    HorizontalDivider(color = AppTheme.DividerColor)
                    Spacer(modifier = Modifier.height(12.dp))

                    // maraka.marakaTypes is just maraka.marakaType (single enum)
                    Surface(
                        shape = RoundedCornerShape(NeoVedicTokens.ElementCornerRadius),
                        color = AppTheme.ChipBackground,
                        modifier = Modifier.padding(vertical = 2.dp)
                    ) {
                        Text(
                            text = maraka.marakaType.displayName,
                            fontSize = 10.sp,
                            fontFamily = SpaceGroteskFamily,
                            color = AppTheme.TextSecondary,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = maraka.interpretation,
                        fontSize = 14.sp,
                        fontFamily = PoppinsFontFamily,
                        color = AppTheme.TextSecondary,
                        lineHeight = 20.sp
                    )
                }
            }
        }
    }
}

@Composable
private fun LongevitySection(analysis: MarakaCalculator.MarakaAnalysis) {
    val longevity = analysis.longevityAnalysis
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // Longevity Methods Card
        Surface(
            modifier = Modifier.fillMaxWidth(),
            color = AppTheme.CardBackground,
            border = androidx.compose.foundation.BorderStroke(1.dp, AppTheme.BorderColor),
            shape = RoundedCornerShape(NeoVedicTokens.ElementCornerRadius)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(
                        imageVector = Icons.Outlined.HealthAndSafety,
                        contentDescription = null,
                        tint = AppTheme.AccentPrimary,
                        modifier = Modifier.size(20.dp)
                    )
                    Text(
                        text = stringResource(StringKeyDosha.MARAKA_METHODS),
                        fontSize = 16.sp,
                        fontFamily = CinzelDecorativeFamily,
                        fontWeight = FontWeight.SemiBold,
                        color = AppTheme.TextPrimary
                    )
                }
                Spacer(modifier = Modifier.height(12.dp))

                LongevityMethodRow(
                    method = stringResource(StringKeyDosha.MARAKA_AMSAYURDAYA), // Generalized title
                    result = longevity.category.displayName,
                    score = longevity.overallScore
                )
            }
        }

        if (longevity.supportingFactors.isNotEmpty() || longevity.challengingFactors.isNotEmpty()) {
            Surface(
                modifier = Modifier.fillMaxWidth(),
                color = AppTheme.CardBackground,
                border = androidx.compose.foundation.BorderStroke(1.dp, AppTheme.BorderColor),
                shape = RoundedCornerShape(NeoVedicTokens.ElementCornerRadius)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = stringResource(StringKeyDosha.MARAKA_FACTORS),
                        fontSize = 16.sp,
                        fontFamily = CinzelDecorativeFamily,
                        fontWeight = FontWeight.SemiBold,
                        color = AppTheme.TextPrimary
                    )
                    Spacer(modifier = Modifier.height(12.dp))

                    if (longevity.supportingFactors.isNotEmpty()) {
                        Text(
                            text = stringResource(StringKeyDosha.MARAKA_POSITIVE),
                            fontSize = 12.sp,
                            fontFamily = SpaceGroteskFamily,
                            color = AppTheme.SuccessColor
                        )
                        longevity.supportingFactors.take(4).forEach { factor ->
                            Row(
                                modifier = Modifier.padding(vertical = 2.dp),
                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                Text("\u2713", color = AppTheme.SuccessColor, fontSize = 12.sp)
                                Text(
                                    text = factor,
                                    fontSize = 14.sp,
                                    fontFamily = PoppinsFontFamily,
                                    color = AppTheme.TextSecondary
                                )
                            }
                        }
                    }

                    if (longevity.challengingFactors.isNotEmpty()) {
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = stringResource(StringKeyDosha.MARAKA_NEGATIVE),
                            fontSize = 12.sp,
                            fontFamily = SpaceGroteskFamily,
                            color = AppTheme.WarningColor
                        )
                        longevity.challengingFactors.take(4).forEach { factor ->
                            Row(
                                modifier = Modifier.padding(vertical = 2.dp),
                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                Text("\u26A0", color = AppTheme.WarningColor, fontSize = 12.sp)
                                Text(
                                    text = factor,
                                    fontSize = 14.sp,
                                    fontFamily = PoppinsFontFamily,
                                    color = AppTheme.TextSecondary
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
private fun LongevityMethodRow(method: String, result: String, score: Int) {
    val scoreColor = when {
        score >= 70 -> AppTheme.SuccessColor
        score >= 50 -> AppTheme.AccentGold
        score >= 30 -> AppTheme.WarningColor
        else -> AppTheme.ErrorColor
    }

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        shape = RoundedCornerShape(NeoVedicTokens.ElementCornerRadius),
        color = AppTheme.CardBackgroundElevated
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = method,
                    fontSize = 14.sp,
                    fontFamily = PoppinsFontFamily,
                    fontWeight = FontWeight.Medium,
                    color = AppTheme.TextPrimary
                )
                Text(
                    text = result,
                    fontSize = 12.sp,
                    fontFamily = SpaceGroteskFamily,
                    color = AppTheme.TextMuted
                )
            }
            Surface(
                shape = RoundedCornerShape(NeoVedicTokens.ElementCornerRadius),
                color = scoreColor.copy(alpha = 0.15f)
            ) {
                Text(
                    text = "$score%",
                    fontSize = 12.sp,
                    fontFamily = SpaceGroteskFamily,
                    fontWeight = FontWeight.SemiBold,
                    color = scoreColor,
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                )
            }
        }
    }
}

@Composable
private fun MarakaDashaSection(analysis: MarakaCalculator.MarakaAnalysis) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        if (analysis.dashaPeriods.isEmpty()) {
            Surface(
                modifier = Modifier.fillMaxWidth(),
                color = AppTheme.SuccessColor.copy(alpha = 0.1f),
                border = androidx.compose.foundation.BorderStroke(1.dp, AppTheme.SuccessColor.copy(alpha = 0.2f)),
                shape = RoundedCornerShape(NeoVedicTokens.ElementCornerRadius)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        imageVector = Icons.Outlined.AccessTime,
                        contentDescription = null,
                        tint = AppTheme.SuccessColor,
                        modifier = Modifier.size(48.dp)
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = stringResource(StringKeyDosha.MARAKA_NO_CRITICAL),
                        fontSize = 16.sp,
                        fontFamily = CinzelDecorativeFamily,
                        fontWeight = FontWeight.SemiBold,
                        color = AppTheme.TextPrimary,
                        textAlign = TextAlign.Center
                    )
                }
            }
        } else {
            Surface(
                modifier = Modifier.fillMaxWidth(),
                color = AppTheme.CardBackground,
                border = androidx.compose.foundation.BorderStroke(1.dp, AppTheme.BorderColor),
                shape = RoundedCornerShape(NeoVedicTokens.ElementCornerRadius)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.AccessTime,
                            contentDescription = null,
                            tint = AppTheme.WarningColor,
                            modifier = Modifier.size(20.dp)
                        )
                        Text(
                            text = stringResource(StringKeyDosha.MARAKA_CRITICAL_PERIODS),
                            fontSize = 16.sp,
                            fontFamily = CinzelDecorativeFamily,
                            fontWeight = FontWeight.SemiBold,
                            color = AppTheme.TextPrimary
                        )
                    }
                    Spacer(modifier = Modifier.height(12.dp))

                    analysis.dashaPeriods.take(8).forEach { period ->
                        MarakaDashaPeriodRow(period)
                    }
                }
            }
        }
    }
}

@Composable
private fun MarakaDashaPeriodRow(period: MarakaCalculator.MarakaDashaPeriod) {
    val language = LocalLanguage.current
    val riskColor = when {
        period.riskLevel.level >= 4 -> AppTheme.ErrorColor
        period.riskLevel.level >= 3 -> AppTheme.WarningColor
        period.riskLevel.level >= 2 -> AppTheme.AccentGold
        else -> AppTheme.TextMuted
    }

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        shape = RoundedCornerShape(NeoVedicTokens.ElementCornerRadius),
        color = AppTheme.CardBackgroundElevated
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = stringResource(StringKeyDosha.MARAKA_PERIOD_FMT, period.planet.getLocalizedName(language)),
                    fontSize = 14.sp,
                    fontFamily = PoppinsFontFamily,
                    fontWeight = FontWeight.Medium,
                    color = AppTheme.TextPrimary
                )
            }
            Surface(
                shape = RoundedCornerShape(NeoVedicTokens.ElementCornerRadius),
                color = riskColor.copy(alpha = 0.15f)
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    if (period.riskLevel.level >= 3) {
                        Icon(
                            imageVector = Icons.Default.Warning,
                            contentDescription = null,
                            tint = riskColor,
                            modifier = Modifier.size(12.dp)
                        )
                    }
                    Text(
                        text = "${period.riskLevel.level * 20}%",
                        fontSize = 12.sp,
                        fontFamily = SpaceGroteskFamily,
                        fontWeight = FontWeight.SemiBold,
                        color = riskColor
                    )
                }
            }
        }
    }
}

@Composable
private fun MarakaRemediesSection(analysis: MarakaCalculator.MarakaAnalysis) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        if (analysis.remedies.isEmpty()) {
            Surface(
                modifier = Modifier.fillMaxWidth(),
                color = AppTheme.CardBackground,
                border = androidx.compose.foundation.BorderStroke(1.dp, AppTheme.BorderColor),
                shape = RoundedCornerShape(NeoVedicTokens.ElementCornerRadius)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Spa,
                        contentDescription = null,
                        tint = AppTheme.TextMuted,
                        modifier = Modifier.size(48.dp)
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = stringResource(StringKeyDosha.MARAKA_NO_REMEDIES),
                        fontSize = 14.sp,
                        fontFamily = PoppinsFontFamily,
                        color = AppTheme.TextMuted,
                        textAlign = TextAlign.Center
                    )
                }
            }
        } else {
            analysis.remedies.forEach { remedy ->
                MarakaRemedyCard(remedy)
            }
        }
    }
}

@Composable
private fun MarakaRemedyCard(remedy: MarakaCalculator.MarakaRemedy) {
    val language = LocalLanguage.current
    val categoryColor = when (remedy.remedyType) {
        "Mantra" -> AppTheme.AccentGold
        "Gemstone" -> AppTheme.AccentPrimary
        "Charity" -> AppTheme.SuccessColor
        "Fasting" -> AppTheme.AccentTeal
        "Worship" -> AppTheme.LifeAreaSpiritual
        "Yantra" -> AppTheme.WarningColor
        "Lifestyle" -> AppTheme.LifeAreaHealth
        else -> AppTheme.TextSecondary
    }

    val categoryName = when (remedy.remedyType) {
        "Mantra" -> stringResource(StringKeyRemedy.CAT_MANTRA)
        "Gemstone" -> stringResource(StringKeyRemedy.CAT_GEMSTONE)
        "Charity" -> stringResource(StringKeyRemedy.CAT_CHARITY)
        "Fasting" -> stringResource(StringKeyRemedy.CAT_FASTING)
        "Worship" -> stringResource(StringKeyRemedy.CAT_DEITY)
        "Yantra" -> stringResource(StringKeyRemedy.CAT_YANTRA)
        "Lifestyle" -> stringResource(StringKeyRemedy.CAT_LIFESTYLE)
        else -> remedy.remedyType
    }

    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = AppTheme.CardBackground,
        border = androidx.compose.foundation.BorderStroke(1.dp, AppTheme.BorderColor),
        shape = RoundedCornerShape(NeoVedicTokens.ElementCornerRadius)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Surface(
                    shape = RoundedCornerShape(NeoVedicTokens.ElementCornerRadius),
                    color = categoryColor.copy(alpha = 0.15f)
                ) {
                    Text(
                        text = categoryName,
                        fontSize = 10.sp,
                        fontFamily = SpaceGroteskFamily,
                        color = categoryColor,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }
                Text(
                    text = remedy.planet?.getLocalizedName(language) ?: stringResource(StringKeyAnalysis.TRANSIT_QUALITY_UNKNOWN),
                    fontSize = 12.sp,
                    fontFamily = SpaceGroteskFamily,
                    color = AppTheme.TextMuted
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = remedy.description,
                fontSize = 14.sp,
                fontFamily = PoppinsFontFamily,
                fontWeight = FontWeight.Medium,
                color = AppTheme.TextPrimary
            )
            Text(
                text = remedy.mantra ?: remedy.charity ?: "",
                fontSize = 14.sp,
                fontFamily = PoppinsFontFamily,
                color = AppTheme.TextSecondary
            )
            if (!remedy.fasting.isNullOrEmpty()) {
                Text(
                    text = "${stringResource(StringKeyDosha.UI_TIMING)}: ${remedy.fasting}",
                    fontSize = 10.sp,
                    fontFamily = SpaceGroteskFamily,
                    color = AppTheme.TextMuted,
                    modifier = Modifier.padding(top = 4.dp)
                )
            }
        }
    }
}

@Composable
private fun LoadingContent(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            CircularProgressIndicator(color = AppTheme.WarningColor)
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = stringResource(StringKeyDosha.MARAKA_ANALYZING),
                fontSize = 14.sp,
                fontFamily = PoppinsFontFamily,
                color = AppTheme.TextMuted
            )
        }
    }
}

@Composable
private fun EmptyContent(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(32.dp)
        ) {
            Icon(
                imageVector = Icons.Outlined.Favorite,
                contentDescription = null,
                tint = AppTheme.TextMuted,
                modifier = Modifier.size(64.dp)
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = stringResource(StringKeyDosha.UI_NO_CHART_DATA),
                fontSize = 18.sp,
                fontFamily = CinzelDecorativeFamily,
                fontWeight = FontWeight.SemiBold,
                color = AppTheme.TextPrimary
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = stringResource(StringKeyDosha.MARAKA_NO_CHART_DESC),
                fontSize = 14.sp,
                fontFamily = PoppinsFontFamily,
                color = AppTheme.TextMuted,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
private fun MarakaInfoDialog(onDismiss: () -> Unit) {
    androidx.compose.material3.AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = stringResource(StringKeyDosha.MARAKA_ABOUT_TITLE),
                fontSize = 18.sp,
                fontFamily = CinzelDecorativeFamily,
                fontWeight = FontWeight.SemiBold,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
        },
        text = {
            Text(
                text = stringResource(StringKeyDosha.MARAKA_ABOUT_DESC),
                fontSize = 14.sp,
                fontFamily = PoppinsFontFamily,
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







