package com.astro.vajra.ui.screen

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.material3.IconButton
import com.astro.vajra.ui.components.common.ModernPillTabRow
import com.astro.vajra.ui.components.common.NeoVedicPageHeader
import com.astro.vajra.ui.components.common.TabItem
import com.astro.vajra.ui.components.common.NeoVedicEmptyState
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.astro.vajra.core.common.Language
import com.astro.vajra.core.common.StringKey
import com.astro.vajra.core.common.StringKeyAnalysis
import com.astro.vajra.core.common.StringKeyDosha
import com.astro.vajra.core.common.StringResources
import com.astro.vajra.data.localization.currentLanguage
import com.astro.vajra.core.common.getLocalizedName
import com.astro.vajra.data.localization.stringResource
import com.astro.vajra.core.model.Planet
import com.astro.vajra.core.model.PlanetPosition
import com.astro.vajra.core.model.VedicChart
import com.astro.vajra.ephemeris.UpachayaTransitTracker
import com.astro.vajra.ephemeris.UpachayaTransitAnalysis
import com.astro.vajra.ephemeris.UpachayaTransit
import com.astro.vajra.ephemeris.UpachayaLevel
import com.astro.vajra.ephemeris.HouseStrength
import com.astro.vajra.ephemeris.TransitQuality
import com.astro.vajra.ephemeris.AlertType
import com.astro.vajra.ephemeris.AlertPriority
import com.astro.vajra.ephemeris.TransitReference
import com.astro.vajra.ephemeris.HouseTransitAnalysis
import com.astro.vajra.ephemeris.UpachayaAlert
import com.astro.vajra.ephemeris.UpcomingUpachayaTransit
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
 * Upachaya Transit Screen
 *
 * Tracks beneficial transits through Upachaya houses (3, 6, 10, 11) where
 * natural malefics give especially good results. Provides:
 * - Current transit positions relative to Moon and Lagna
 * - House-wise analysis of growth opportunities
 * - Alerts for significant planetary transits
 * - Recommendations for maximizing transit benefits
 *
 * Based on: Phaladeepika, BPHS transit rules
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UpachayaTransitScreen(
    chart: VedicChart?,
    transitPositions: List<PlanetPosition>,
    onBack: () -> Unit
) {
    if (chart == null) {
        EmptyChartScreen(
            title = stringResource(StringKeyDosha.UPACHAYA_SCREEN_TITLE),
            message = stringResource(StringKey.NO_PROFILE_MESSAGE),
            onBack = onBack
        )
        return
    }

    val language = currentLanguage()
    var showInfoDialog by remember { mutableStateOf(false) }
    var selectedTab by remember { mutableIntStateOf(0) }
    var isCalculating by remember { mutableStateOf(true) }
    var analysisResult by remember { mutableStateOf<UpachayaTransitAnalysis?>(null) }
    var upcomingTransits by remember { mutableStateOf<List<UpcomingUpachayaTransit>>(emptyList()) }

    val tabs = listOf(
        stringResource(StringKeyDosha.SCREEN_OVERVIEW),
        stringResource(StringKeyDosha.UPACHAYA_HOUSE_ANALYSIS),
        stringResource(StringKeyDosha.UPACHAYA_TRANSIT_DETAILS),
        stringResource(StringKeyDosha.UPACHAYA_UPCOMING_TRANSITS)
    )

    // Calculate analysis
    LaunchedEffect(chart, transitPositions, language) {
        isCalculating = true
        delay(300)
        withContext(Dispatchers.Default) {
            analysisResult = UpachayaTransitTracker.analyzeUpachayaTransits(chart, transitPositions, language)
            upcomingTransits = UpachayaTransitTracker.getUpcomingTransits(chart, language)
        }
        isCalculating = false
    }

    Scaffold(
        topBar = {
            NeoVedicPageHeader(
                title = stringResource(StringKeyDosha.UPACHAYA_SCREEN_TITLE),
                subtitle = stringResource(StringKeyDosha.UPACHAYA_SCREEN_SUBTITLE),
                onBack = onBack,
                actions = {
                    IconButton(onClick = { showInfoDialog = true }) {
                        Icon(
                            Icons.Outlined.Info,
                            contentDescription = stringResource(StringKeyDosha.UPACHAYA_SCREEN_ABOUT),
                            tint = AppTheme.TextSecondary
                        )
                    }
                }
            )
        },
        containerColor = AppTheme.ScreenBackground
    ) { paddingValues ->
        when {
            isCalculating -> LoadingContent(paddingValues)
            analysisResult == null -> ErrorContent(
                paddingValues = paddingValues,
                message = stringResource(StringKeyDosha.SCREEN_ERROR_CALCULATION)
            )
            else -> {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                        .background(AppTheme.ScreenBackground),
                    contentPadding = PaddingValues(bottom = 32.dp)
                ) {
                    // Tab Row
                    item {
                        TabSelector(
                            tabs = tabs,
                            selectedIndex = selectedTab,
                            onTabSelected = { selectedTab = it }
                        )
                    }

                    // Content based on selected tab
                    when (selectedTab) {
                        0 -> item { OverviewTab(analysisResult!!, language) }
                        1 -> item { HouseAnalysisTab(analysisResult!!, language) }
                        2 -> item { TransitDetailsTab(analysisResult!!, language) }
                        3 -> item { UpcomingTransitsTab(upcomingTransits, language) }
                    }
                }
            }
        }
    }

    // Info Dialog
    if (showInfoDialog) {
        AlertDialog(
            onDismissRequest = { showInfoDialog = false },
            title = {
                Text(
                    stringResource(StringKeyDosha.UPACHAYA_SCREEN_ABOUT),
                    fontFamily = CinzelDecorativeFamily,
                    fontWeight = FontWeight.Bold,
                    color = AppTheme.TextPrimary,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
            },
            text = {
                Column {
                    Text(
                        stringResource(StringKeyDosha.UPACHAYA_SCREEN_ABOUT_DESC),
                        fontFamily = PoppinsFontFamily,
                        color = AppTheme.TextSecondary,
                        lineHeight = 22.sp
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Surface(
                        color = AppTheme.InfoColor.copy(alpha = 0.1f),
                        shape = RoundedCornerShape(com.astro.vajra.ui.theme.NeoVedicTokens.ElementCornerRadius)
                    ) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Text(
                                stringResource(StringKeyDosha.UPACHAYA_ACTIVE_TRANSITS),
                                fontFamily = SpaceGroteskFamily,
                                fontWeight = FontWeight.SemiBold,
                                fontSize = com.astro.vajra.ui.theme.NeoVedicFontSizes.S13,
                                color = AppTheme.TextPrimary
                            )
                            Text(
                                "${getHouseName(3, language)}, ${getHouseName(6, language)}, ${getHouseName(10, language)}, ${getHouseName(11, language)}",
                                fontFamily = PoppinsFontFamily,
                                fontSize = com.astro.vajra.ui.theme.NeoVedicFontSizes.S12,
                                color = AppTheme.TextSecondary
                            )
                        }
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = { showInfoDialog = false }) {
                    Text(
                        stringResource(StringKey.BTN_CLOSE),
                        color = AppTheme.AccentPrimary,
                        fontFamily = SpaceGroteskFamily
                    )
                }
            },
            containerColor = AppTheme.CardBackground,
            shape = RoundedCornerShape(com.astro.vajra.ui.theme.NeoVedicTokens.ElementCornerRadius)
        )
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
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            CircularProgressIndicator(color = AppTheme.AccentPrimary)
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                stringResource(StringKeyDosha.SCREEN_CALCULATING),
                fontFamily = PoppinsFontFamily,
                color = AppTheme.TextMuted
            )
        }
    }
}

@Composable
private fun ErrorContent(paddingValues: PaddingValues, message: String) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues),
        contentAlignment = Alignment.Center
    ) {
        NeoVedicEmptyState(
            title = stringResource(StringKeyDosha.UPACHAYA_SCREEN_TITLE),
            subtitle = message,
            icon = Icons.Outlined.ErrorOutline
        )
    }
}

@Composable
private fun TabSelector(
    tabs: List<String>,
    selectedIndex: Int,
    onTabSelected: (Int) -> Unit
) {
    ModernPillTabRow(
        tabs = tabs.mapIndexed { index, title ->
            TabItem(
                title = title,
                accentColor = if (selectedIndex == index) AppTheme.AccentPrimary else Color.Unspecified
            )
        },
        selectedIndex = selectedIndex,
        onTabSelected = onTabSelected,
        modifier = Modifier.padding(
            horizontal = com.astro.vajra.ui.theme.NeoVedicTokens.ScreenPadding,
            vertical = com.astro.vajra.ui.theme.NeoVedicTokens.SpaceXS
        )
    )
}

@Composable
private fun SectionHeader(title: String, icon: ImageVector, tint: Color) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(icon, contentDescription = null, tint = tint, modifier = Modifier.size(20.dp))
        Spacer(modifier = Modifier.width(8.dp))
        Text(title, fontWeight = FontWeight.SemiBold, fontSize = com.astro.vajra.ui.theme.NeoVedicFontSizes.S15, color = AppTheme.TextPrimary, fontFamily = CinzelDecorativeFamily)
    }
}

// ============================================
// OVERVIEW TAB
// ============================================
@Composable
private fun OverviewTab(analysis: UpachayaTransitAnalysis, language: Language) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Overall Assessment Card
        OverallAssessmentCard(analysis, language)

        // Reference Points
        ReferencePointsCard(analysis, language)

        // Active Alerts
        if (analysis.alerts.isNotEmpty()) {
            SectionHeader(
                title = stringResource(StringKeyDosha.UPACHAYA_ACTIVE_ALERTS),
                icon = Icons.Filled.Notifications,
                tint = AppTheme.AccentGold
            )
            analysis.alerts.forEach { alert ->
                AlertCard(alert, language)
            }
        }

        // Most Significant Transits
        if (analysis.mostSignificantTransits.isNotEmpty()) {
            SectionHeader(
                title = stringResource(StringKeyDosha.UPACHAYA_SIGNIFICANT_TRANSITS),
                icon = Icons.Filled.Star,
                tint = AppTheme.AccentPrimary
            )
            analysis.mostSignificantTransits.forEach { transit ->
                SignificantTransitCard(transit, language)
            }
        }

        // Recommendations
        if (analysis.recommendations.isNotEmpty()) {
            RecommendationsCard(analysis.recommendations)
        }
    }
}

@Composable
private fun OverallAssessmentCard(analysis: UpachayaTransitAnalysis, language: Language) {
    val assessment = analysis.overallAssessment

    val (backgroundColor, iconColor, icon) = when (assessment.level) {
        UpachayaLevel.EXCEPTIONAL -> Triple(
            AppTheme.SuccessColor.copy(alpha = 0.15f),
            AppTheme.SuccessColor,
            Icons.Filled.Star
        )
        UpachayaLevel.HIGH -> Triple(
            AppTheme.SuccessColor.copy(alpha = 0.1f),
            AppTheme.SuccessColor,
            Icons.Filled.TrendingUp
        )
        UpachayaLevel.MODERATE -> Triple(
            AppTheme.AccentGold.copy(alpha = 0.1f),
            AppTheme.AccentGold,
            Icons.Filled.TrendingFlat
        )
        UpachayaLevel.LOW -> Triple(
            AppTheme.TextMuted.copy(alpha = 0.1f),
            AppTheme.TextMuted,
            Icons.Filled.HourglassEmpty
        )
    }

    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = AppTheme.CardBackground,
        shape = RoundedCornerShape(com.astro.vajra.ui.theme.NeoVedicTokens.CardCornerRadius),
        border = BorderStroke(NeoVedicTokens.BorderWidth, iconColor.copy(alpha = 0.3f))
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(56.dp)
                        .clip(CircleShape)
                        .background(iconColor.copy(alpha = 0.2f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        icon,
                        contentDescription = null,
                        tint = iconColor,
                        modifier = Modifier.size(28.dp)
                    )
                }
                Spacer(modifier = Modifier.width(16.dp))
                Column {
                    Text(
                        stringResource(StringKeyDosha.UPACHAYA_TRANSIT_ASSESSMENT),
                        fontWeight = FontWeight.Bold,
                        fontFamily = CinzelDecorativeFamily,
                        fontSize = com.astro.vajra.ui.theme.NeoVedicFontSizes.S16,
                        color = AppTheme.TextPrimary
                    )
                    Text(
                        "${stringResource(StringKeyDosha.QUALITY_LABEL)}: ${assessment.level.getLocalizedName(language)}",
                        fontFamily = PoppinsFontFamily,
                        fontSize = com.astro.vajra.ui.theme.NeoVedicFontSizes.S14,
                        color = iconColor,
                        fontWeight = FontWeight.Medium
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Progress Indicator
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    stringResource(StringKeyDosha.UPACHAYA_SIGNIFICANCE),
                    fontFamily = SpaceGroteskFamily,
                    fontSize = com.astro.vajra.ui.theme.NeoVedicFontSizes.S12,
                    color = AppTheme.TextMuted
                )
                Spacer(modifier = Modifier.weight(1f))
                Text(
                    "${assessment.score.toInt()}%",
                    fontFamily = SpaceGroteskFamily,
                    fontSize = com.astro.vajra.ui.theme.NeoVedicFontSizes.S14,
                    fontWeight = FontWeight.Bold,
                    color = iconColor
                )
            }
            Spacer(modifier = Modifier.height(4.dp))
            LinearProgressIndicator(
                progress = { (assessment.score / 100.0).coerceIn(0.0, 1.0).toFloat() },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(8.dp)
                    .clip(RoundedCornerShape(com.astro.vajra.ui.theme.NeoVedicTokens.ElementCornerRadius)),
                color = iconColor,
                trackColor = AppTheme.ChipBackground
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                assessment.summary,
                fontFamily = PoppinsFontFamily,
                fontSize = com.astro.vajra.ui.theme.NeoVedicFontSizes.S14,
                color = AppTheme.TextSecondary,
                lineHeight = 20.sp
            )

            Spacer(modifier = Modifier.height(8.dp))

            Surface(
                color = AppTheme.ChipBackground,
                shape = RoundedCornerShape(com.astro.vajra.ui.theme.NeoVedicTokens.ElementCornerRadius)
            ) {
                Row(
                    modifier = Modifier.padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        Icons.Outlined.Info,
                        contentDescription = null,
                        tint = AppTheme.InfoColor,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        assessment.keyPeriod,
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
private fun ReferencePointsCard(analysis: UpachayaTransitAnalysis, language: Language) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = AppTheme.CardBackground,
        shape = RoundedCornerShape(com.astro.vajra.ui.theme.NeoVedicTokens.CardCornerRadius),
        border = BorderStroke(NeoVedicTokens.BorderWidth, AppTheme.BorderColor)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                stringResource(StringKeyDosha.UPACHAYA_REFERENCE_POINTS),
                fontWeight = FontWeight.SemiBold,
                fontFamily = CinzelDecorativeFamily,
                color = AppTheme.TextPrimary
            )

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .clip(CircleShape)
                            .background(AppTheme.PlanetMoon.copy(alpha = 0.2f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            Icons.Filled.NightsStay,
                            contentDescription = null,
                            tint = AppTheme.PlanetMoon,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(stringResource(StringKeyDosha.UPACHAYA_MOON_SIGN), fontFamily = SpaceGroteskFamily, fontSize = com.astro.vajra.ui.theme.NeoVedicFontSizes.S11, color = AppTheme.TextMuted)
                    Text(
                        analysis.moonSign.getLocalizedName(language),
                        fontFamily = PoppinsFontFamily,
                        fontSize = com.astro.vajra.ui.theme.NeoVedicFontSizes.S13,
                        fontWeight = FontWeight.Medium,
                        color = AppTheme.TextPrimary
                    )
                }

                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .clip(CircleShape)
                            .background(AppTheme.AccentPrimary.copy(alpha = 0.2f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            Icons.Filled.Home,
                            contentDescription = null,
                            tint = AppTheme.AccentPrimary,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(stringResource(StringKeyDosha.UPACHAYA_LAGNA), fontFamily = SpaceGroteskFamily, fontSize = com.astro.vajra.ui.theme.NeoVedicFontSizes.S11, color = AppTheme.TextMuted)
                    Text(
                        analysis.lagnaSign.getLocalizedName(language),
                        fontFamily = PoppinsFontFamily,
                        fontSize = com.astro.vajra.ui.theme.NeoVedicFontSizes.S13,
                        fontWeight = FontWeight.Medium,
                        color = AppTheme.TextPrimary
                    )
                }

                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .clip(CircleShape)
                            .background(AppTheme.SuccessColor.copy(alpha = 0.2f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            "${analysis.activeUpachayaTransits.size}",
                            fontWeight = FontWeight.Bold,
                            fontFamily = SpaceGroteskFamily,
                            fontSize = com.astro.vajra.ui.theme.NeoVedicFontSizes.S16,
                            color = AppTheme.SuccessColor
                        )
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(stringResource(StringKeyDosha.UPACHAYA_ACTIVE), fontFamily = SpaceGroteskFamily, fontSize = com.astro.vajra.ui.theme.NeoVedicFontSizes.S11, color = AppTheme.TextMuted)
                    Text(
                        stringResource(StringKeyDosha.UPACHAYA_SCREEN_TITLE),
                        fontFamily = PoppinsFontFamily,
                        fontSize = com.astro.vajra.ui.theme.NeoVedicFontSizes.S13,
                        fontWeight = FontWeight.Medium,
                        color = AppTheme.TextPrimary
                    )
                }
            }
        }
    }
}

@Composable
private fun AlertCard(alert: UpachayaAlert, language: Language) {
    val (backgroundColor, iconColor, icon) = when (alert.type) {
        AlertType.OPPORTUNITY -> Triple(
            AppTheme.SuccessColor.copy(alpha = 0.1f),
            AppTheme.SuccessColor,
            Icons.Filled.TrendingUp
        )
        AlertType.MAJOR_TRANSIT -> Triple(
            AppTheme.AccentPrimary.copy(alpha = 0.1f),
            AppTheme.AccentPrimary,
            Icons.Filled.Star
        )
        AlertType.FORTUNE -> Triple(
            AppTheme.AccentGold.copy(alpha = 0.1f),
            AppTheme.AccentGold,
            Icons.Filled.AutoAwesome
        )
        AlertType.CAUTION -> Triple(
            AppTheme.WarningColor.copy(alpha = 0.1f),
            AppTheme.WarningColor,
            Icons.Outlined.Warning
        )
    }

    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = backgroundColor,
        shape = RoundedCornerShape(com.astro.vajra.ui.theme.NeoVedicTokens.ElementCornerRadius)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                icon,
                contentDescription = null,
                tint = iconColor,
                modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    alert.message,
                    fontFamily = PoppinsFontFamily,
                    fontSize = com.astro.vajra.ui.theme.NeoVedicFontSizes.S13,
                    color = AppTheme.TextPrimary,
                    lineHeight = 18.sp
                )
            }
            if (alert.priority == AlertPriority.HIGH) {
                Surface(
                    color = iconColor.copy(alpha = 0.2f),
                    shape = RoundedCornerShape(com.astro.vajra.ui.theme.NeoVedicTokens.ElementCornerRadius)
                ) {
                    Text(
                        stringResource(com.astro.vajra.core.common.StringKeySaham.ACTIVATED),
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                        fontFamily = SpaceGroteskFamily,
                        fontSize = com.astro.vajra.ui.theme.NeoVedicFontSizes.S10,
                        fontWeight = FontWeight.Bold,
                        color = iconColor
                    )
                }
            }
        }
    }
}

@Composable
private fun SignificantTransitCard(transit: UpachayaTransit, language: Language) {
    val qualityColor = when (transit.transitQuality) {
        TransitQuality.EXCELLENT -> AppTheme.SuccessColor
        TransitQuality.GOOD -> AppTheme.SuccessColor.copy(alpha = 0.8f)
        TransitQuality.FAVORABLE -> AppTheme.AccentGold
        TransitQuality.NEUTRAL -> AppTheme.TextMuted
    }

    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = AppTheme.CardBackground,
        shape = RoundedCornerShape(com.astro.vajra.ui.theme.NeoVedicTokens.ElementCornerRadius),
        border = BorderStroke(NeoVedicTokens.BorderWidth, AppTheme.BorderColor)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(44.dp)
                    .clip(CircleShape)
                    .background(AppTheme.getPlanetColor(transit.planet).copy(alpha = 0.2f)),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    transit.planet.displayName.take(2),
                    fontWeight = FontWeight.Bold,
                    fontFamily = CinzelDecorativeFamily,
                    fontSize = com.astro.vajra.ui.theme.NeoVedicFontSizes.S14,
                    color = AppTheme.getPlanetColor(transit.planet)
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        transit.planet.getLocalizedName(language),
                        fontWeight = FontWeight.SemiBold,
                        fontFamily = CinzelDecorativeFamily,
                        color = AppTheme.TextPrimary
                    )
                    Surface(
                        color = qualityColor.copy(alpha = 0.2f),
                        shape = RoundedCornerShape(com.astro.vajra.ui.theme.NeoVedicTokens.ElementCornerRadius)
                    ) {
                        Text(
                            transit.transitQuality.getLocalizedName(language),
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                            fontFamily = SpaceGroteskFamily,
                            fontSize = com.astro.vajra.ui.theme.NeoVedicFontSizes.S11,
                            fontWeight = FontWeight.Medium,
                            color = qualityColor
                        )
                    }
                }
                Text(
                    "${transit.transitSign.getLocalizedName(language)} \u2022 ${getHouseName(transit.houseFromReference, language)} ${stringResource(StringKeyDosha.FROM_LABEL)} ${transit.reference.getLocalizedName(language)}",
                    fontFamily = PoppinsFontFamily,
                    fontSize = com.astro.vajra.ui.theme.NeoVedicFontSizes.S12,
                    color = AppTheme.TextMuted
                )
                Text(
                    "${stringResource(StringKeyDosha.UPACHAYA_SIGNIFICANCE)}: ${transit.significance.toInt()}%",
                    fontFamily = SpaceGroteskFamily,
                    fontSize = com.astro.vajra.ui.theme.NeoVedicFontSizes.S12,
                    color = qualityColor,
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }
}

@Composable
private fun RecommendationsCard(recommendations: List<String>) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = AppTheme.CardBackground,
        shape = RoundedCornerShape(com.astro.vajra.ui.theme.NeoVedicTokens.ElementCornerRadius),
        border = BorderStroke(NeoVedicTokens.BorderWidth, AppTheme.BorderColor)
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    Icons.Filled.Lightbulb,
                    contentDescription = null,
                    tint = AppTheme.AccentGold,
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(12.dp))
                Text(
                    stringResource(StringKeyDosha.SCREEN_RECOMMENDATIONS),
                    fontWeight = FontWeight.Bold,
                    fontFamily = CinzelDecorativeFamily,
                    fontSize = com.astro.vajra.ui.theme.NeoVedicFontSizes.S16,
                    color = AppTheme.TextPrimary
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            recommendations.forEach { rec ->
                Row(modifier = Modifier.padding(vertical = 4.dp), verticalAlignment = Alignment.Top) {
                    Icon(
                        Icons.Filled.Check,
                        contentDescription = null,
                        tint = AppTheme.SuccessColor,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(rec, fontFamily = PoppinsFontFamily, fontSize = com.astro.vajra.ui.theme.NeoVedicFontSizes.S13, color = AppTheme.TextSecondary, lineHeight = 18.sp)
                }
            }
        }
    }
}

// ============================================
// HOUSE ANALYSIS TAB
// ============================================
@Composable
private fun HouseAnalysisTab(analysis: UpachayaTransitAnalysis, language: Language) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Introduction Card
        item {
            Surface(
                modifier = Modifier.fillMaxWidth(),
                color = AppTheme.AccentPrimary.copy(alpha = 0.08f),
                shape = RoundedCornerShape(com.astro.vajra.ui.theme.NeoVedicTokens.ElementCornerRadius)
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Text(
                        stringResource(StringKeyDosha.UPACHAYA_ABOUT),
                        fontWeight = FontWeight.Bold,
                        fontFamily = CinzelDecorativeFamily,
                        fontSize = com.astro.vajra.ui.theme.NeoVedicFontSizes.S16,
                        color = AppTheme.TextPrimary
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        stringResource(StringKeyDosha.UPACHAYA_TRANSIT_ABOUT_DESC),
                        fontFamily = PoppinsFontFamily,
                        fontSize = com.astro.vajra.ui.theme.NeoVedicFontSizes.S13,
                        color = AppTheme.TextSecondary,
                        lineHeight = 20.sp
                    )
                }
            }
        }

        // House-wise Analysis Cards
        items(analysis.houseWiseAnalysis.entries.toList().sortedBy { it.key }) { (house, houseAnalysis) ->
            HouseAnalysisCard(houseAnalysis, language)
        }
    }
}

@Composable
private fun HouseAnalysisCard(analysis: HouseTransitAnalysis, language: Language) {
    val strengthColor = when (analysis.strength) {
        HouseStrength.VERY_STRONG -> AppTheme.SuccessColor
        HouseStrength.STRONG -> AppTheme.SuccessColor.copy(alpha = 0.8f)
        HouseStrength.MODERATE -> AppTheme.AccentGold
        HouseStrength.MILD -> AppTheme.TextMuted
        HouseStrength.INACTIVE -> AppTheme.TextMuted.copy(alpha = 0.5f)
    }

    val houseIcon = when (analysis.house) {
        3 -> Icons.Filled.Shield
        6 -> Icons.Filled.Security
        10 -> Icons.Filled.Work
        11 -> Icons.Filled.Paid
        else -> Icons.Filled.Home
    }

    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = if (analysis.strength != HouseStrength.INACTIVE)
                strengthColor.copy(alpha = 0.05f)
            else AppTheme.CardBackground,
        shape = RoundedCornerShape(com.astro.vajra.ui.theme.NeoVedicTokens.ElementCornerRadius),
        border = BorderStroke(NeoVedicTokens.BorderWidth, AppTheme.BorderColor)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(44.dp)
                            .clip(CircleShape)
                            .background(strengthColor.copy(alpha = 0.2f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            houseIcon,
                            contentDescription = null,
                            tint = strengthColor,
                            modifier = Modifier.size(22.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(
                            "${getHouseName(analysis.house, language)} ${stringResource(StringKeyDosha.HOUSE_LABEL)}",
                            fontWeight = FontWeight.SemiBold,
                            fontFamily = CinzelDecorativeFamily,
                            color = AppTheme.TextPrimary
                        )
                        Text(
                            analysis.houseTheme,
                            fontFamily = PoppinsFontFamily,
                            fontSize = com.astro.vajra.ui.theme.NeoVedicFontSizes.S12,
                            color = AppTheme.TextMuted
                        )
                    }
                }

                Surface(
                    color = strengthColor.copy(alpha = 0.2f),
                    shape = RoundedCornerShape(com.astro.vajra.ui.theme.NeoVedicTokens.ElementCornerRadius)
                ) {
                    Text(
                        analysis.strength.getLocalizedName(language),
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                        fontFamily = SpaceGroteskFamily,
                        fontSize = com.astro.vajra.ui.theme.NeoVedicFontSizes.S11,
                        fontWeight = FontWeight.Medium,
                        color = strengthColor
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Transiting Planets
            if (analysis.transitingPlanets.isNotEmpty()) {
                Text(
                    stringResource(StringKeyDosha.PLANETS_TITLE),
                    fontFamily = SpaceGroteskFamily,
                    fontSize = com.astro.vajra.ui.theme.NeoVedicFontSizes.S12,
                    color = AppTheme.TextMuted
                )
                Spacer(modifier = Modifier.height(4.dp))
                LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    items(analysis.transitingPlanets) { planet ->
                        Surface(
                            color = AppTheme.getPlanetColor(planet).copy(alpha = 0.2f),
                            shape = RoundedCornerShape(com.astro.vajra.ui.theme.NeoVedicTokens.ElementCornerRadius)
                        ) {
                            Text(
                                planet.getLocalizedName(language),
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                                fontFamily = PoppinsFontFamily,
                                fontSize = com.astro.vajra.ui.theme.NeoVedicFontSizes.S12,
                                fontWeight = FontWeight.Medium,
                                color = AppTheme.getPlanetColor(planet)
                            )
                        }
                    }
                }
            }

            // Effects
            if (analysis.effects.isNotEmpty()) {
                Spacer(modifier = Modifier.height(12.dp))
                analysis.effects.forEach { effect ->
                    Row(
                        modifier = Modifier.padding(vertical = 2.dp),
                        verticalAlignment = Alignment.Top
                    ) {
                        Text("\u2022", color = strengthColor, fontSize = com.astro.vajra.ui.theme.NeoVedicFontSizes.S12)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            effect,
                            fontFamily = PoppinsFontFamily,
                            fontSize = com.astro.vajra.ui.theme.NeoVedicFontSizes.S12,
                            color = AppTheme.TextSecondary,
                            lineHeight = 18.sp
                        )
                    }
                }
            }

            // Timing
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                analysis.timing,
                fontFamily = PoppinsFontFamily,
                fontSize = com.astro.vajra.ui.theme.NeoVedicFontSizes.S11,
                color = AppTheme.TextMuted,
                fontStyle = androidx.compose.ui.text.font.FontStyle.Italic
            )
        }
    }
}

// ============================================
// TRANSIT DETAILS TAB
// ============================================
@Composable
private fun TransitDetailsTab(analysis: UpachayaTransitAnalysis, language: Language) {
    var selectedReference by remember { mutableStateOf(TransitReference.MOON) }

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // Reference Toggle
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                com.astro.vajra.ui.components.common.NeoVedicChoicePill(
                    selected = selectedReference == TransitReference.MOON,
                    onClick = { selectedReference = TransitReference.MOON },
                    label = { Text(stringResource(StringKeyDosha.UPACHAYA_FROM_MOON), fontFamily = SpaceGroteskFamily) },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = AppTheme.PlanetMoon.copy(alpha = 0.2f),
                        selectedLabelColor = AppTheme.PlanetMoon
                    )
                )
                Spacer(modifier = Modifier.width(8.dp))
                com.astro.vajra.ui.components.common.NeoVedicChoicePill(
                    selected = selectedReference == TransitReference.LAGNA,
                    onClick = { selectedReference = TransitReference.LAGNA },
                    label = { Text(stringResource(StringKeyDosha.UPACHAYA_FROM_LAGNA), fontFamily = SpaceGroteskFamily) },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = AppTheme.AccentPrimary.copy(alpha = 0.2f),
                        selectedLabelColor = AppTheme.AccentPrimary
                    )
                )
            }
        }

        val transits = if (selectedReference == TransitReference.MOON)
            analysis.transitsFromMoon
        else
            analysis.transitsFromLagna

        items(transits.sortedByDescending { it.significance }) { transit ->
            TransitDetailCard(transit, language)
        }
    }
}

@Composable
private fun TransitDetailCard(transit: UpachayaTransit, language: Language) {
    var expanded by remember { mutableStateOf(false) }

    val cardColor = if (transit.isInUpachaya)
        AppTheme.SuccessColor.copy(alpha = 0.05f)
    else
        AppTheme.CardBackground

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { expanded = !expanded },
        color = cardColor,
        shape = RoundedCornerShape(com.astro.vajra.ui.theme.NeoVedicTokens.ElementCornerRadius),
        border = BorderStroke(NeoVedicTokens.BorderWidth, AppTheme.BorderColor)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .clip(CircleShape)
                            .background(AppTheme.getPlanetColor(transit.planet).copy(alpha = 0.2f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            transit.planet.displayName.take(2),
                            fontWeight = FontWeight.Bold,
                            fontFamily = CinzelDecorativeFamily,
                            fontSize = com.astro.vajra.ui.theme.NeoVedicFontSizes.S12,
                            color = AppTheme.getPlanetColor(transit.planet)
                        )
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(
                            transit.planet.getLocalizedName(language),
                            fontFamily = CinzelDecorativeFamily,
                            fontWeight = FontWeight.SemiBold,
                            color = AppTheme.TextPrimary
                        )
                        Text(
                            "${transit.transitSign.getLocalizedName(language)} ${String.format("%.1f", transit.transitDegree)}\u00B0",
                            fontFamily = SpaceGroteskFamily,
                            fontSize = com.astro.vajra.ui.theme.NeoVedicFontSizes.S12,
                            color = AppTheme.TextMuted
                        )
                    }
                }

                Column(horizontalAlignment = Alignment.End) {
                    Text(
                        "${getHouseName(transit.houseFromReference, language)} ${stringResource(StringKeyDosha.HOUSE_LABEL)}",
                        fontWeight = FontWeight.Medium,
                        fontFamily = PoppinsFontFamily,
                        fontSize = com.astro.vajra.ui.theme.NeoVedicFontSizes.S13,
                        color = if (transit.isInUpachaya) AppTheme.SuccessColor else AppTheme.TextSecondary
                    )
                    if (transit.isInUpachaya) {
                        Text(
                            stringResource(StringKeyDosha.UPACHAYA_SCREEN_TITLE).uppercase(),
                            fontFamily = SpaceGroteskFamily,
                            fontSize = com.astro.vajra.ui.theme.NeoVedicFontSizes.S10,
                            fontWeight = FontWeight.Bold,
                            color = AppTheme.SuccessColor
                        )
                    }
                }
            }

            AnimatedVisibility(visible = expanded && transit.isInUpachaya) {
                Column(modifier = Modifier.padding(top = 12.dp)) {
                    HorizontalDivider(color = AppTheme.DividerColor)
                    Spacer(modifier = Modifier.height(12.dp))

                    // Duration
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            Icons.Outlined.Schedule,
                            contentDescription = null,
                            tint = AppTheme.TextMuted,
                            modifier = Modifier.size(14.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            transit.approximateDuration,
                            fontFamily = PoppinsFontFamily,
                            fontSize = com.astro.vajra.ui.theme.NeoVedicFontSizes.S12,
                            color = AppTheme.TextMuted
                        )
                    }

                    // Effects
                    if (transit.effects.isNotEmpty()) {
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            stringResource(StringKeyDosha.EFFECTS_LABEL),
                            fontWeight = FontWeight.Medium,
                            fontFamily = CinzelDecorativeFamily,
                            fontSize = com.astro.vajra.ui.theme.NeoVedicFontSizes.S13,
                            color = AppTheme.TextPrimary
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        transit.effects.forEach { effect ->
                            Row(
                                modifier = Modifier.padding(vertical = 2.dp),
                                verticalAlignment = Alignment.Top
                            ) {
                                Text("\u2022", color = AppTheme.TextMuted, fontSize = com.astro.vajra.ui.theme.NeoVedicFontSizes.S12)
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    effect,
                                    fontFamily = PoppinsFontFamily,
                                    fontSize = com.astro.vajra.ui.theme.NeoVedicFontSizes.S12,
                                    color = AppTheme.TextSecondary,
                                    lineHeight = 18.sp
                                )
                            }
                        }
                    }

                    // Recommendations
                    if (transit.recommendations.isNotEmpty()) {
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            stringResource(StringKeyDosha.SCREEN_RECOMMENDATIONS),
                            fontWeight = FontWeight.Medium,
                            fontFamily = CinzelDecorativeFamily,
                            fontSize = com.astro.vajra.ui.theme.NeoVedicFontSizes.S13,
                            color = AppTheme.SuccessColor
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        transit.recommendations.forEach { rec ->
                            Row(
                                modifier = Modifier.padding(vertical = 2.dp),
                                verticalAlignment = Alignment.Top
                            ) {
                                Icon(
                                    Icons.Filled.Check,
                                    contentDescription = null,
                                    tint = AppTheme.SuccessColor,
                                    modifier = Modifier.size(14.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    rec,
                                    fontFamily = PoppinsFontFamily,
                                    fontSize = com.astro.vajra.ui.theme.NeoVedicFontSizes.S12,
                                    color = AppTheme.TextSecondary,
                                    lineHeight = 18.sp
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

// ============================================
// UPCOMING TRANSITS TAB
// ============================================
@Composable
private fun UpcomingTransitsTab(upcomingTransits: List<UpcomingUpachayaTransit>, language: Language) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            Text(
                stringResource(StringKeyDosha.UPACHAYA_UPCOMING_TRANSITS),
                fontWeight = FontWeight.Bold,
                fontFamily = CinzelDecorativeFamily,
                fontSize = com.astro.vajra.ui.theme.NeoVedicFontSizes.S18,
                color = AppTheme.TextPrimary,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            Text(
                stringResource(StringKeyDosha.UPACHAYA_TRANSIT_SUBTITLE),
                fontFamily = PoppinsFontFamily,
                fontSize = com.astro.vajra.ui.theme.NeoVedicFontSizes.S13,
                color = AppTheme.TextMuted,
                modifier = Modifier.padding(bottom = 8.dp)
            )
        }

        // Group by house
        val groupedByHouse = upcomingTransits.groupBy { it.targetHouse }

        groupedByHouse.forEach { (house, transits) ->
            item {
                Text(
                    "${getHouseName(house, language)} ${stringResource(StringKeyDosha.UPACHAYA_TRANSITS_LABEL)}",
                    fontWeight = FontWeight.SemiBold,
                    fontFamily = CinzelDecorativeFamily,
                    fontSize = com.astro.vajra.ui.theme.NeoVedicFontSizes.S15,
                    color = AppTheme.TextPrimary,
                    modifier = Modifier.padding(top = 8.dp, bottom = 4.dp)
                )
            }

            items(transits) { transit ->
                UpcomingTransitCard(transit, language)
            }
        }
    }
}

@Composable
private fun UpcomingTransitCard(transit: UpcomingUpachayaTransit, language: Language) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = AppTheme.CardBackground,
        shape = RoundedCornerShape(com.astro.vajra.ui.theme.NeoVedicTokens.ElementCornerRadius),
        border = BorderStroke(NeoVedicTokens.BorderWidth, AppTheme.BorderColor)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(AppTheme.getPlanetColor(transit.planet).copy(alpha = 0.2f)),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    transit.planet.displayName.take(2),
                    fontWeight = FontWeight.Bold,
                    fontFamily = CinzelDecorativeFamily,
                    fontSize = com.astro.vajra.ui.theme.NeoVedicFontSizes.S12,
                    color = AppTheme.getPlanetColor(transit.planet)
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    "${transit.planet.getLocalizedName(language)} \u2192 ${transit.targetSign.getLocalizedName(language)}",
                    fontWeight = FontWeight.SemiBold,
                    fontFamily = PoppinsFontFamily,
                    color = AppTheme.TextPrimary
                )
                Text(
                    transit.significance,
                    fontFamily = PoppinsFontFamily,
                    fontSize = com.astro.vajra.ui.theme.NeoVedicFontSizes.S12,
                    color = AppTheme.TextSecondary,
                    lineHeight = 16.sp
                )
                Spacer(modifier = Modifier.height(4.dp))
                Surface(
                    color = AppTheme.AccentPrimary.copy(alpha = 0.1f),
                    shape = RoundedCornerShape(4.dp)
                ) {
                    Text(
                        transit.expectedDate,
                        modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp),
                        fontFamily = SpaceGroteskFamily,
                        fontSize = com.astro.vajra.ui.theme.NeoVedicFontSizes.S10,
                        color = AppTheme.AccentPrimary
                    )
                }
            }
        }
    }
}

// Helper to get house name (1st House, 2nd House etc.)
@Composable
private fun getHouseName(house: Int, language: Language): String {
    return stringResource(StringKeyDosha.HOUSE_LABEL_FMT, house)
}