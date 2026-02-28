package com.astro.vajra.ui.screen

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import com.astro.vajra.ui.components.common.NeoVedicPageHeader
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.astro.vajra.core.common.Language
import com.astro.vajra.core.common.StringKey
import com.astro.vajra.core.common.StringKeyAnalysis
import com.astro.vajra.core.common.StringKeyDosha
import com.astro.vajra.core.common.StringKeyMatch
import com.astro.vajra.core.common.StringKeyUIExtra
import com.astro.vajra.data.localization.currentLanguage
import com.astro.vajra.data.localization.stringResource
import com.astro.vajra.core.common.getLocalizedName
import com.astro.vajra.data.localization.localizedAbbr
import com.astro.vajra.core.model.Planet
import com.astro.vajra.core.model.VedicChart
import com.astro.vajra.ephemeris.ArgalaCalculator
import com.astro.vajra.ephemeris.ArgalaCalculator.ArgalaAnalysis
import com.astro.vajra.ephemeris.ArgalaCalculator.ArgalaNature
import com.astro.vajra.ephemeris.ArgalaCalculator.ArgalaStrength
import com.astro.vajra.ephemeris.ArgalaCalculator.ArgalaType
import com.astro.vajra.ephemeris.ArgalaCalculator.HouseArgalaResult
import com.astro.vajra.ephemeris.ArgalaCalculator.PlanetArgalaResult
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
 * Argala Analysis Screen
 *
 * Comprehensive Jaimini Argala (intervention) analysis based on classical texts:
 * - Argala: Planetary intervention from specific houses (2nd, 4th, 11th, 5th)
 * - Virodha Argala: Obstruction of Argala (12th, 10th, 3rd, 9th)
 * - Shubha vs Ashubha: Benefic vs Malefic interventions
 * - Net Effect: Final result after considering obstructions
 *
 * References: Jaimini Sutras, Commentary by Raghunatha Bhatta
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ArgalaScreen(
    chart: VedicChart?,
    onBack: () -> Unit
) {
    if (chart == null) {
        EmptyChartScreen(
            title = stringResource(StringKeyDosha.ARGALA_TITLE),
            message = stringResource(StringKey.NO_PROFILE_MESSAGE),
            onBack = onBack
        )
        return
    }

    val language = currentLanguage()
    var showInfoDialog by remember { mutableStateOf(false) }
    var selectedTab by remember { mutableIntStateOf(0) }
    var selectedHouse by remember { mutableIntStateOf(1) }
    var selectedPlanet by remember { mutableStateOf<Planet?>(null) }
    var isCalculating by remember { mutableStateOf(true) }
    var argalaAnalysis by remember { mutableStateOf<ArgalaAnalysis?>(null) }

    val tabs = listOf(
        stringResource(StringKeyDosha.ARGALA_ABOUT),
        stringResource(StringKeyMatch.TAB_HOUSES),
        stringResource(StringKeyMatch.TAB_PLANETS)
    )

    // Calculate Argala
    LaunchedEffect(chart) {
        isCalculating = true
        delay(300)
        try {
            argalaAnalysis = withContext(Dispatchers.Default) {
                ArgalaCalculator.analyzeArgala(chart)
            }
        } catch (e: Exception) {
            // Handle error silently
        }
        isCalculating = false
    }

    if (showInfoDialog) {
        ArgalaInfoDialog(onDismiss = { showInfoDialog = false })
    }

    Scaffold(
        containerColor = AppTheme.ScreenBackground,
        topBar = {
            NeoVedicPageHeader(
                title = stringResource(StringKeyDosha.ARGALA_TITLE),
                subtitle = chart.birthData.name,
                onBack = onBack,
                actions = {
                    IconButton(onClick = { showInfoDialog = true }) {
                        Icon(
                            Icons.Outlined.Info,
                            contentDescription = stringResource(StringKeyDosha.ARGALA_ABOUT),
                            tint = AppTheme.TextPrimary
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        if (isCalculating) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(48.dp),
                        color = AppTheme.AccentPrimary,
                        strokeWidth = 4.dp
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        stringResource(StringKeyAnalysis.ARGALA_CALCULATING),
                        fontFamily = PoppinsFontFamily,
                        fontSize = com.astro.vajra.ui.theme.NeoVedicFontSizes.S14,
                        color = AppTheme.TextMuted
                    )
                }
            }
        } else if (argalaAnalysis == null) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    stringResource(StringKeyAnalysis.ARGALA_FAILED),
                    fontFamily = PoppinsFontFamily,
                    fontSize = com.astro.vajra.ui.theme.NeoVedicFontSizes.S14,
                    color = AppTheme.ErrorColor
                )
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .background(AppTheme.ScreenBackground),
                contentPadding = PaddingValues(bottom = 32.dp)
            ) {
                // Tab selector
                item {
                    ArgalaTabSelector(
                        tabs = tabs,
                        selectedTab = selectedTab,
                        onTabSelected = { selectedTab = it }
                    )
                }

                // Tab content
                when (selectedTab) {
                    0 -> item {
                        ArgalaOverviewTab(
                            analysis = argalaAnalysis!!,
                            language = language
                        )
                    }
                    1 -> item {
                        ArgalaHousesTab(
                            analysis = argalaAnalysis!!,
                            selectedHouse = selectedHouse,
                            onSelectHouse = { selectedHouse = it },
                            language = language
                        )
                    }
                    2 -> item {
                        ArgalaPlanetsTab(
                            analysis = argalaAnalysis!!,
                            selectedPlanet = selectedPlanet,
                            onSelectPlanet = { selectedPlanet = if (selectedPlanet == it) null else it },
                            language = language,
                            chart = chart
                        )
                    }
                }
            }
        }
    }
}

// ============================================
// UI Components
// ============================================

@Composable
private fun ArgalaTabSelector(
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
private fun ArgalaOverviewTab(
    analysis: ArgalaAnalysis,
    language: Language
) {
    Column(modifier = Modifier.padding(16.dp)) {
        // About Argala Card
        Surface(
            modifier = Modifier.fillMaxWidth(),
            color = AppTheme.CardBackground,
            shape = RoundedCornerShape(com.astro.vajra.ui.theme.NeoVedicTokens.CardCornerRadius),
            border = BorderStroke(NeoVedicTokens.BorderWidth, AppTheme.BorderColor)
        ) {
            Column(modifier = Modifier.padding(20.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(48.dp)
                            .clip(CircleShape)
                            .background(AppTheme.AccentGold.copy(alpha = 0.15f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            Icons.Filled.Bolt,
                            contentDescription = null,
                            tint = AppTheme.AccentGold,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(16.dp))
                    Column {
                        Text(
                            stringResource(StringKeyDosha.ARGALA_TITLE),
                            fontFamily = CinzelDecorativeFamily,
                            fontSize = com.astro.vajra.ui.theme.NeoVedicFontSizes.S18,
                            fontWeight = FontWeight.Bold,
                            color = AppTheme.TextPrimary
                        )
                        Text(
                            stringResource(StringKeyDosha.ARGALA_SUBTITLE),
                            fontFamily = PoppinsFontFamily,
                            fontSize = com.astro.vajra.ui.theme.NeoVedicFontSizes.S12,
                            color = AppTheme.TextMuted
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    stringResource(StringKeyDosha.ARGALA_ABOUT_DESC),
                    fontFamily = PoppinsFontFamily,
                    fontSize = com.astro.vajra.ui.theme.NeoVedicFontSizes.S14,
                    color = AppTheme.TextSecondary,
                    lineHeight = 22.sp
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Argala Types Explanation
        Surface(
            modifier = Modifier.fillMaxWidth(),
            color = AppTheme.CardElevated,
            shape = RoundedCornerShape(com.astro.vajra.ui.theme.NeoVedicTokens.CardCornerRadius),
            border = BorderStroke(NeoVedicTokens.BorderWidth, AppTheme.BorderColor)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    stringResource(StringKeyAnalysis.ARGALA_TYPES_TITLE),
                    fontFamily = CinzelDecorativeFamily,
                    fontSize = com.astro.vajra.ui.theme.NeoVedicFontSizes.S16,
                    fontWeight = FontWeight.SemiBold,
                    color = AppTheme.TextPrimary
                )

                Spacer(modifier = Modifier.height(12.dp))

                ArgalaTypeItem(
                    icon = Icons.Filled.Star,
                    title = stringResource(StringKeyUIExtra.ARGALA_TYPES_TITLE),
                    description = stringResource(StringKeyAnalysis.ARGALA_PRIMARY_DESC),
                    color = AppTheme.AccentGold
                )

                Spacer(modifier = Modifier.height(10.dp))

                ArgalaTypeItem(
                    icon = Icons.Filled.StarHalf,
                    title = stringResource(StringKeyDosha.ARGALA_SECONDARY),
                    description = stringResource(StringKeyAnalysis.ARGALA_SECONDARY_DESC),
                    color = AppTheme.AccentTeal
                )

                Spacer(modifier = Modifier.height(10.dp))

                ArgalaTypeItem(
                    icon = Icons.Filled.AutoAwesome,
                    title = stringResource(StringKeyDosha.ARGALA_FIFTH_HOUSE),
                    description = stringResource(StringKeyAnalysis.ARGALA_FIFTH_HOUSE_DESC),
                    color = AppTheme.InfoColor
                )

                Spacer(modifier = Modifier.height(10.dp))

                ArgalaTypeItem(
                    icon = Icons.Filled.Block,
                    title = stringResource(StringKeyDosha.ARGALA_VIRODHA),
                    description = stringResource(StringKeyAnalysis.ARGALA_VIRODHA_DESC),
                    color = AppTheme.WarningColor
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Overall Assessment
        OverallArgalaCard(analysis = analysis, language = language)

        Spacer(modifier = Modifier.height(16.dp))

        // Karma Pattern Analysis
        if (analysis.overallAssessment.karmaPatterns.isNotEmpty()) {
            KarmaPatternCard(analysis = analysis)
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Significant Argalas
        if (analysis.significantArgalas.isNotEmpty()) {
            SignificantArgalasCard(analysis = analysis, language = language)
        }
    }
}

@Composable
private fun ArgalaTypeItem(
    icon: ImageVector,
    title: String,
    description: String,
    color: Color
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(36.dp)
                .clip(CircleShape)
                .background(color.copy(alpha = 0.15f)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                icon,
                contentDescription = null,
                tint = color,
                modifier = Modifier.size(18.dp)
            )
        }

        Spacer(modifier = Modifier.width(12.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                title,
                fontFamily = PoppinsFontFamily,
                fontSize = com.astro.vajra.ui.theme.NeoVedicFontSizes.S14,
                fontWeight = FontWeight.Medium,
                color = AppTheme.TextPrimary
            )
            Text(
                description,
                fontFamily = PoppinsFontFamily,
                fontSize = com.astro.vajra.ui.theme.NeoVedicFontSizes.S12,
                color = AppTheme.TextMuted
            )
        }
    }
}

@Composable
private fun OverallArgalaCard(
    analysis: ArgalaAnalysis,
    language: Language
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = AppTheme.CardBackground,
        shape = RoundedCornerShape(com.astro.vajra.ui.theme.NeoVedicTokens.CardCornerRadius),
        border = BorderStroke(NeoVedicTokens.BorderWidth, AppTheme.BorderColor)
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Text(
                stringResource(StringKeyAnalysis.ARGALA_CHART_WIDE_PATTERNS),
                fontFamily = CinzelDecorativeFamily,
                fontSize = com.astro.vajra.ui.theme.NeoVedicFontSizes.S16,
                fontWeight = FontWeight.SemiBold,
                color = AppTheme.TextPrimary
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Strongest Benefic Argala
            analysis.overallAssessment.strongestBeneficArgala?.let { house ->
                ArgalaHighlightRow(
                    label = stringResource(StringKeyAnalysis.ARGALA_STRONGEST_SUPPORT),
                    value = stringResource(StringKeyMatch.HOUSE_LABEL, house),
                    icon = Icons.Filled.TrendingUp,
                    color = AppTheme.SuccessColor
                )
                Spacer(modifier = Modifier.height(10.dp))
            }

            // Strongest Malefic Argala
            analysis.overallAssessment.strongestMaleficArgala?.let { house ->
                ArgalaHighlightRow(
                    label = stringResource(StringKeyAnalysis.ARGALA_GREATEST_CHALLENGE),
                    value = stringResource(StringKeyMatch.HOUSE_LABEL, house),
                    icon = Icons.Filled.TrendingDown,
                    color = AppTheme.WarningColor
                )
                Spacer(modifier = Modifier.height(10.dp))
            }

            // Most Obstructed
            analysis.overallAssessment.mostObstructedHouse?.let { house ->
                ArgalaHighlightRow(
                    label = stringResource(StringKeyAnalysis.ARGALA_MOST_OBSTRUCTED),
                    value = stringResource(StringKeyMatch.HOUSE_LABEL, house),
                    icon = Icons.Filled.Block,
                    color = AppTheme.ErrorColor
                )
                Spacer(modifier = Modifier.height(10.dp))
            }

            // Least Obstructed
            analysis.overallAssessment.leastObstructedHouse?.let { house ->
                ArgalaHighlightRow(
                    label = stringResource(StringKeyAnalysis.ARGALA_LEAST_OBSTRUCTED),
                    value = stringResource(StringKeyMatch.HOUSE_LABEL, house),
                    icon = Icons.Filled.CheckCircle,
                    color = AppTheme.InfoColor
                )
            }

            // Recommendations
            if (analysis.overallAssessment.generalRecommendations.isNotEmpty()) {
                Spacer(modifier = Modifier.height(16.dp))
                HorizontalDivider(color = AppTheme.BorderColor.copy(alpha = 0.5f))
                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    stringResource(StringKeyDosha.UI_RECOMMENDATIONS),
                    fontFamily = CinzelDecorativeFamily,
                    fontSize = com.astro.vajra.ui.theme.NeoVedicFontSizes.S14,
                    fontWeight = FontWeight.SemiBold,
                    color = AppTheme.TextPrimary
                )

                Spacer(modifier = Modifier.height(8.dp))

                analysis.overallAssessment.generalRecommendations.take(3).forEach { recommendation ->
                    Row(
                        modifier = Modifier.padding(vertical = 4.dp),
                        verticalAlignment = Alignment.Top
                    ) {
                        Icon(
                            Icons.Filled.LightbulbCircle,
                            contentDescription = null,
                            tint = AppTheme.AccentGold,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            recommendation,
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
private fun ArgalaHighlightRow(
    label: String,
    value: String,
    icon: ImageVector,
    color: Color
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            modifier = Modifier.weight(1f),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                icon,
                contentDescription = null,
                tint = color,
                modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                label,
                fontFamily = PoppinsFontFamily,
                fontSize = com.astro.vajra.ui.theme.NeoVedicFontSizes.S14,
                color = AppTheme.TextSecondary,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
        }
        Text(
            value,
            fontFamily = SpaceGroteskFamily,
            fontSize = com.astro.vajra.ui.theme.NeoVedicFontSizes.S14,
            fontWeight = FontWeight.Bold,
            color = color,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            textAlign = TextAlign.End
        )
    }
}

@Composable
private fun KarmaPatternCard(analysis: ArgalaAnalysis) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = AppTheme.CardElevated,
        shape = RoundedCornerShape(com.astro.vajra.ui.theme.NeoVedicTokens.CardCornerRadius),
        border = BorderStroke(NeoVedicTokens.BorderWidth, AppTheme.BorderColor)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    Icons.Filled.AccountTree,
                    contentDescription = null,
                    tint = AppTheme.LifeAreaSpiritual,
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(12.dp))
                Text(
                    stringResource(StringKeyAnalysis.ARGALA_KARMA_PATTERNS),
                    fontFamily = CinzelDecorativeFamily,
                    fontSize = com.astro.vajra.ui.theme.NeoVedicFontSizes.S16,
                    fontWeight = FontWeight.SemiBold,
                    color = AppTheme.TextPrimary
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            analysis.overallAssessment.karmaPatterns.forEach { pattern ->
                Row(
                    modifier = Modifier.padding(vertical = 6.dp),
                    verticalAlignment = Alignment.Top
                ) {
                    Box(
                        modifier = Modifier
                            .size(6.dp)
                            .clip(CircleShape)
                            .background(AppTheme.LifeAreaSpiritual)
                            .padding(top = 8.dp)
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(
                        pattern,
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
private fun SignificantArgalasCard(
    analysis: ArgalaAnalysis,
    language: Language
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = AppTheme.CardBackground,
        shape = RoundedCornerShape(com.astro.vajra.ui.theme.NeoVedicTokens.CardCornerRadius),
        border = BorderStroke(NeoVedicTokens.BorderWidth, AppTheme.BorderColor)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                stringResource(StringKeyAnalysis.ARGALA_SIGNIFICANT_ARGALAS),
                fontFamily = CinzelDecorativeFamily,
                fontSize = com.astro.vajra.ui.theme.NeoVedicFontSizes.S16,
                fontWeight = FontWeight.SemiBold,
                color = AppTheme.TextPrimary
            )

            Spacer(modifier = Modifier.height(12.dp))

            analysis.significantArgalas.take(5).forEach { argala ->
                SignificantArgalaItem(argala = argala, language = language)
                Spacer(modifier = Modifier.height(10.dp))
            }
        }
    }
}

@Composable
private fun SignificantArgalaItem(
    argala: ArgalaCalculator.SignificantArgala,
    language: Language
) {
    val natureColor = when (argala.nature) {
        ArgalaNature.SHUBHA -> AppTheme.SuccessColor
        ArgalaNature.ASHUBHA -> AppTheme.WarningColor
        ArgalaNature.MIXED -> AppTheme.InfoColor
    }

    Surface(
        color = natureColor.copy(alpha = 0.08f),
        shape = RoundedCornerShape(com.astro.vajra.ui.theme.NeoVedicTokens.ElementCornerRadius)
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    argala.targetDescription,
                    modifier = Modifier.weight(1f),
                    fontFamily = PoppinsFontFamily,
                    fontSize = com.astro.vajra.ui.theme.NeoVedicFontSizes.S14,
                    fontWeight = FontWeight.SemiBold,
                    color = AppTheme.TextPrimary,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
                ArgalaStrengthBadge(strength = argala.strength, color = natureColor)
            }

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                argala.lifeAreaEffect,
                fontFamily = PoppinsFontFamily,
                fontSize = com.astro.vajra.ui.theme.NeoVedicFontSizes.S12,
                color = AppTheme.TextSecondary
            )

            if (argala.involvedPlanets.isNotEmpty()) {
                Spacer(modifier = Modifier.height(8.dp))
                LazyRow(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    items(argala.involvedPlanets) { planet ->
                        PlanetChip(planet = planet, language = language)
                    }
                }
            }
        }
    }
}

@Composable
private fun ArgalaStrengthBadge(strength: ArgalaStrength, color: Color) {
    val (label, bgColor) = when (strength) {
        ArgalaStrength.VERY_STRONG -> stringResource(StringKeyAnalysis.ARGALA_STRENGTH_VERY_STRONG) to color
        ArgalaStrength.STRONG -> stringResource(StringKeyAnalysis.ARGALA_STRENGTH_STRONG) to color
        ArgalaStrength.MODERATE -> stringResource(StringKeyAnalysis.ARGALA_STRENGTH_MODERATE) to color.copy(alpha = 0.7f)
        ArgalaStrength.WEAK -> stringResource(StringKeyAnalysis.ARGALA_STRENGTH_WEAK) to AppTheme.TextMuted
        ArgalaStrength.OBSTRUCTED -> stringResource(StringKeyAnalysis.ARGALA_STRENGTH_OBSTRUCTED) to AppTheme.ErrorColor
    }

    Surface(
        color = bgColor.copy(alpha = 0.15f),
        shape = RoundedCornerShape(com.astro.vajra.ui.theme.NeoVedicTokens.ElementCornerRadius)
    ) {
        Text(
            label,
            fontFamily = SpaceGroteskFamily,
            fontSize = com.astro.vajra.ui.theme.NeoVedicFontSizes.S10,
            fontWeight = FontWeight.SemiBold,
            color = bgColor,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}

@Composable
private fun PlanetChip(planet: Planet, language: Language) {
    Surface(
        color = AppTheme.getPlanetColor(planet).copy(alpha = 0.15f),
        shape = RoundedCornerShape(com.astro.vajra.ui.theme.NeoVedicTokens.ElementCornerRadius)
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                planet.symbol,
                fontFamily = CinzelDecorativeFamily,
                fontSize = com.astro.vajra.ui.theme.NeoVedicFontSizes.S12,
                color = AppTheme.getPlanetColor(planet)
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                planet.getLocalizedName(language),
                fontFamily = PoppinsFontFamily,
                fontSize = com.astro.vajra.ui.theme.NeoVedicFontSizes.S12,
                color = AppTheme.getPlanetColor(planet)
            )
        }
    }
}

@Composable
private fun ArgalaHousesTab(
    analysis: ArgalaAnalysis,
    selectedHouse: Int,
    onSelectHouse: (Int) -> Unit,
    language: Language
) {
    Column(modifier = Modifier.padding(16.dp)) {
        // House selector
        Text(
            stringResource(StringKeyAnalysis.ARGALA_HOUSE_SELECTOR_LABEL),
            fontFamily = CinzelDecorativeFamily,
            fontSize = com.astro.vajra.ui.theme.NeoVedicFontSizes.S14,
            fontWeight = FontWeight.SemiBold,
            color = AppTheme.TextPrimary,
            modifier = Modifier.padding(bottom = 12.dp)
        )

        // House grid selector
        HouseGridSelector(
            selectedHouse = selectedHouse,
            onSelectHouse = onSelectHouse,
            houseArgalas = analysis.houseArgalas
        )

        Spacer(modifier = Modifier.height(16.dp))

        // House detail
        analysis.houseArgalas[selectedHouse]?.let { houseResult ->
            HouseArgalaDetailCard(
                house = selectedHouse,
                result = houseResult,
                language = language
            )
        }
    }
}

@Composable
private fun HouseGridSelector(
    selectedHouse: Int,
    onSelectHouse: (Int) -> Unit,
    houseArgalas: Map<Int, HouseArgalaResult>
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        for (row in 0..2) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                for (col in 0..3) {
                    val house = row * 4 + col + 1
                    val isSelected = house == selectedHouse
                    val result = houseArgalas[house]

                    val backgroundColor = when {
                        isSelected -> AppTheme.AccentPrimary.copy(alpha = 0.2f)
                        result?.effectiveArgala?.dominantNature == ArgalaNature.SHUBHA ->
                            AppTheme.SuccessColor.copy(alpha = 0.1f)
                        result?.effectiveArgala?.dominantNature == ArgalaNature.ASHUBHA ->
                            AppTheme.WarningColor.copy(alpha = 0.1f)
                        else -> AppTheme.ChipBackground
                    }

                    Surface(
                        onClick = { onSelectHouse(house) },
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(com.astro.vajra.ui.theme.NeoVedicTokens.ElementCornerRadius),
                        color = backgroundColor
                    ) {
                        Box(
                            modifier = Modifier
                                .aspectRatio(1f)
                                .padding(8.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                house.toString(),
                                fontFamily = SpaceGroteskFamily,
                                fontSize = com.astro.vajra.ui.theme.NeoVedicFontSizes.S16,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                color = if (isSelected) AppTheme.AccentPrimary else AppTheme.TextPrimary
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun HouseArgalaDetailCard(
    house: Int,
    result: HouseArgalaResult,
    language: Language
) {
    Column {
        // Header card
        Surface(
            modifier = Modifier.fillMaxWidth(),
            color = when (result.effectiveArgala.dominantNature) {
                ArgalaNature.SHUBHA -> AppTheme.SuccessColor.copy(alpha = 0.08f)
                ArgalaNature.ASHUBHA -> AppTheme.WarningColor.copy(alpha = 0.08f)
                ArgalaNature.MIXED -> AppTheme.InfoColor.copy(alpha = 0.08f)
            },
            shape = RoundedCornerShape(com.astro.vajra.ui.theme.NeoVedicTokens.CardCornerRadius),
            border = BorderStroke(NeoVedicTokens.BorderWidth, AppTheme.BorderColor)
        ) {
            Column(modifier = Modifier.padding(20.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            stringResource(StringKeyDosha.ARGALA_TO_HOUSE, house),
                            fontFamily = CinzelDecorativeFamily,
                            fontSize = com.astro.vajra.ui.theme.NeoVedicFontSizes.S18,
                            fontWeight = FontWeight.Bold,
                            color = AppTheme.TextPrimary,
                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis
                        )
                        Text(
                            getHouseName(house, language),
                            fontFamily = PoppinsFontFamily,
                            fontSize = com.astro.vajra.ui.theme.NeoVedicFontSizes.S14,
                            color = AppTheme.TextMuted,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }

                    NetEffectIndicator(
                        netStrength = result.netArgalaStrength,
                        nature = result.effectiveArgala.dominantNature
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    result.interpretation,
                    fontFamily = PoppinsFontFamily,
                    fontSize = com.astro.vajra.ui.theme.NeoVedicFontSizes.S14,
                    color = AppTheme.TextSecondary,
                    lineHeight = 20.sp
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Primary Argalas
        if (result.primaryArgalas.isNotEmpty()) {
            Surface(
                modifier = Modifier.fillMaxWidth(),
                color = AppTheme.CardBackground,
                shape = RoundedCornerShape(com.astro.vajra.ui.theme.NeoVedicTokens.ElementCornerRadius),
                border = BorderStroke(NeoVedicTokens.BorderWidth, AppTheme.BorderColor)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            Icons.Filled.CallReceived,
                            contentDescription = null,
                            tint = AppTheme.AccentGold,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            stringResource(com.astro.vajra.core.common.StringKeyAnalysis.UI_ARGALA_INFLUENCES),
                            fontFamily = CinzelDecorativeFamily,
                            fontSize = com.astro.vajra.ui.theme.NeoVedicFontSizes.S14,
                            fontWeight = FontWeight.SemiBold,
                            color = AppTheme.TextPrimary
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    result.primaryArgalas.forEach { argala ->
                        ArgalaInfluenceItem(argala = argala, language = language)
                        Spacer(modifier = Modifier.height(10.dp))
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
        }

        // Virodha Argalas (Obstructions)
        if (result.virodhaArgalas.isNotEmpty()) {
            Surface(
                modifier = Modifier.fillMaxWidth(),
                color = AppTheme.CardElevated,
                shape = RoundedCornerShape(com.astro.vajra.ui.theme.NeoVedicTokens.ElementCornerRadius),
                border = BorderStroke(NeoVedicTokens.BorderWidth, AppTheme.BorderColor)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            Icons.Filled.Block,
                            contentDescription = null,
                            tint = AppTheme.WarningColor,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            stringResource(StringKeyDosha.ARGALA_VIRODHA),
                            fontFamily = CinzelDecorativeFamily,
                            fontSize = com.astro.vajra.ui.theme.NeoVedicFontSizes.S14,
                            fontWeight = FontWeight.SemiBold,
                            color = AppTheme.TextPrimary
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    result.virodhaArgalas.forEach { virodha ->
                        VirodhaArgalaItem(virodha = virodha, language = language)
                        Spacer(modifier = Modifier.height(10.dp))
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Net Effect Summary
        NetEffectCard(effectiveArgala = result.effectiveArgala)
    }
}

@Composable
private fun NetEffectIndicator(
    netStrength: Double,
    nature: ArgalaNature
) {
    val (color, icon) = when {
        netStrength > 1.0 -> AppTheme.SuccessColor to Icons.Filled.TrendingUp
        netStrength > 0 -> AppTheme.InfoColor to Icons.Filled.TrendingUp
        netStrength > -1.0 -> AppTheme.WarningColor to Icons.Filled.TrendingDown
        else -> AppTheme.ErrorColor to Icons.Filled.TrendingDown
    }

    Surface(
        color = color.copy(alpha = 0.15f),
        shape = RoundedCornerShape(com.astro.vajra.ui.theme.NeoVedicTokens.ElementCornerRadius)
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                icon,
                contentDescription = null,
                tint = color,
                modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text(
                String.format("%.2f", netStrength),
                fontFamily = SpaceGroteskFamily,
                fontSize = com.astro.vajra.ui.theme.NeoVedicFontSizes.S14,
                fontWeight = FontWeight.Bold,
                color = color
            )
        }
    }
}

@Composable
private fun ArgalaInfluenceItem(
    argala: ArgalaCalculator.ArgalaInfluence,
    language: Language
) {
    val natureColor = when (argala.nature) {
        ArgalaNature.SHUBHA -> AppTheme.SuccessColor
        ArgalaNature.ASHUBHA -> AppTheme.WarningColor
        ArgalaNature.MIXED -> AppTheme.InfoColor
    }

    Surface(
        color = natureColor.copy(alpha = 0.08f),
        shape = RoundedCornerShape(com.astro.vajra.ui.theme.NeoVedicTokens.ElementCornerRadius)
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    argala.argalaType.displayName,
                    modifier = Modifier.weight(1f),
                    fontFamily = PoppinsFontFamily,
                    fontSize = com.astro.vajra.ui.theme.NeoVedicFontSizes.S14,
                    fontWeight = FontWeight.SemiBold,
                    color = AppTheme.TextPrimary,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
                Surface(
                    color = natureColor.copy(alpha = 0.2f),
                    shape = RoundedCornerShape(com.astro.vajra.ui.theme.NeoVedicTokens.ElementCornerRadius)
                ) {
                    Text(
                        when (argala.nature) {
                            ArgalaNature.SHUBHA -> stringResource(StringKeyDosha.ARGALA_SHUBHA)
                            ArgalaNature.ASHUBHA -> stringResource(StringKeyDosha.ARGALA_ASHUBHA)
                            ArgalaNature.MIXED -> stringResource(StringKeyDosha.ARGALA_MIXED)
                        },
                        fontFamily = SpaceGroteskFamily,
                        fontSize = com.astro.vajra.ui.theme.NeoVedicFontSizes.S10,
                        fontWeight = FontWeight.Medium,
                        color = natureColor,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                stringResource(StringKeyDosha.ARGALA_FROM_HOUSE, argala.argalaHouse),
                fontFamily = PoppinsFontFamily,
                fontSize = com.astro.vajra.ui.theme.NeoVedicFontSizes.S12,
                color = AppTheme.TextMuted
            )

            if (argala.planets.isNotEmpty()) {
                Spacer(modifier = Modifier.height(8.dp))
                LazyRow(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    items(argala.planets) { planet ->
                        PlanetChip(planet = planet, language = language)
                    }
                }
            }

            Spacer(modifier = Modifier.height(6.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    stringResource(StringKeyUIExtra.ARGALA_STRENGTH_LABEL),
                    modifier = Modifier.weight(1f),
                    fontFamily = PoppinsFontFamily,
                    fontSize = com.astro.vajra.ui.theme.NeoVedicFontSizes.S12,
                    color = AppTheme.TextMuted,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    String.format("%.2f", argala.strength),
                    fontFamily = SpaceGroteskFamily,
                    fontSize = com.astro.vajra.ui.theme.NeoVedicFontSizes.S12,
                    fontWeight = FontWeight.SemiBold,
                    color = natureColor
                )
            }
        }
    }
}

@Composable
private fun VirodhaArgalaItem(
    virodha: ArgalaCalculator.VirodhaArgala,
    language: Language
) {
    Surface(
        color = if (virodha.isEffective)
            AppTheme.WarningColor.copy(alpha = 0.08f)
        else
            AppTheme.ChipBackground,
        shape = RoundedCornerShape(com.astro.vajra.ui.theme.NeoVedicTokens.ElementCornerRadius)
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    stringResource(StringKeyUIExtra.ARGALA_HOUSE_PREFIX, virodha.obstructingHouse),
                    modifier = Modifier.weight(1f),
                    fontFamily = PoppinsFontFamily,
                    fontSize = com.astro.vajra.ui.theme.NeoVedicFontSizes.S14,
                    fontWeight = FontWeight.SemiBold,
                    color = AppTheme.TextPrimary,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                if (virodha.isEffective) {
                    Surface(
                        color = AppTheme.WarningColor.copy(alpha = 0.2f),
                        shape = RoundedCornerShape(com.astro.vajra.ui.theme.NeoVedicTokens.ElementCornerRadius)
                    ) {
                        Text(
                            stringResource(StringKeyUIExtra.ARGALA_EFFECTIVE),
                            fontFamily = SpaceGroteskFamily,
                            fontSize = com.astro.vajra.ui.theme.NeoVedicFontSizes.S10,
                            fontWeight = FontWeight.Medium,
                            color = AppTheme.WarningColor,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                virodha.description,
                fontFamily = PoppinsFontFamily,
                fontSize = com.astro.vajra.ui.theme.NeoVedicFontSizes.S12,
                color = AppTheme.TextSecondary
            )

            if (virodha.obstructingPlanets.isNotEmpty()) {
                Spacer(modifier = Modifier.height(8.dp))
                LazyRow(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    items(virodha.obstructingPlanets) { planet ->
                        PlanetChip(planet = planet, language = language)
                    }
                }
            }

            Spacer(modifier = Modifier.height(6.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    stringResource(StringKeyUIExtra.ARGALA_OBSTRUCTION_LABEL),
                    modifier = Modifier.weight(1f),
                    fontFamily = PoppinsFontFamily,
                    fontSize = com.astro.vajra.ui.theme.NeoVedicFontSizes.S12,
                    color = AppTheme.TextMuted,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    "${(virodha.obstructionStrength * 100).toInt()}%",
                    fontFamily = SpaceGroteskFamily,
                    fontSize = com.astro.vajra.ui.theme.NeoVedicFontSizes.S12,
                    fontWeight = FontWeight.SemiBold,
                    color = if (virodha.isEffective) AppTheme.WarningColor else AppTheme.TextMuted
                )
            }
        }
    }
}

@Composable
private fun NetEffectCard(
    effectiveArgala: ArgalaCalculator.EffectiveArgala
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = AppTheme.CardBackground,
        shape = RoundedCornerShape(com.astro.vajra.ui.theme.NeoVedicTokens.ElementCornerRadius),
        border = BorderStroke(NeoVedicTokens.BorderWidth, AppTheme.BorderColor)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    Icons.Filled.Balance,
                    contentDescription = null,
                    tint = AppTheme.AccentGold,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    stringResource(StringKeyDosha.ARGALA_NET_EFFECT),
                    fontFamily = CinzelDecorativeFamily,
                    fontSize = com.astro.vajra.ui.theme.NeoVedicFontSizes.S14,
                    fontWeight = FontWeight.SemiBold,
                    color = AppTheme.TextPrimary
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Benefic strength
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    modifier = Modifier.weight(1f),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        Icons.Filled.Add,
                        contentDescription = null,
                        tint = AppTheme.SuccessColor,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        stringResource(StringKeyUIExtra.ARGALA_BENEFIC_STRENGTH),
                        fontFamily = PoppinsFontFamily,
                        fontSize = com.astro.vajra.ui.theme.NeoVedicFontSizes.S12,
                        color = AppTheme.TextSecondary,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
                Text(
                    String.format("%.2f", effectiveArgala.netBeneficStrength),
                    fontFamily = SpaceGroteskFamily,
                    fontSize = com.astro.vajra.ui.theme.NeoVedicFontSizes.S14,
                    fontWeight = FontWeight.SemiBold,
                    color = AppTheme.SuccessColor
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Malefic strength
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    modifier = Modifier.weight(1f),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        Icons.Filled.Remove,
                        contentDescription = null,
                        tint = AppTheme.WarningColor,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        stringResource(StringKeyUIExtra.ARGALA_MALEFIC_STRENGTH),
                        fontFamily = PoppinsFontFamily,
                        fontSize = com.astro.vajra.ui.theme.NeoVedicFontSizes.S12,
                        color = AppTheme.TextSecondary,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
                Text(
                    String.format("%.2f", effectiveArgala.netMaleficStrength),
                    fontFamily = SpaceGroteskFamily,
                    fontSize = com.astro.vajra.ui.theme.NeoVedicFontSizes.S14,
                    fontWeight = FontWeight.SemiBold,
                    color = AppTheme.WarningColor
                )
            }

            Spacer(modifier = Modifier.height(12.dp))
            HorizontalDivider(color = AppTheme.BorderColor.copy(alpha = 0.5f))
            Spacer(modifier = Modifier.height(12.dp))

            Text(
                effectiveArgala.summary,
                fontFamily = PoppinsFontFamily,
                fontSize = com.astro.vajra.ui.theme.NeoVedicFontSizes.S14,
                color = AppTheme.TextSecondary,
                lineHeight = 20.sp
            )
        }
    }
}

@Composable
private fun ArgalaPlanetsTab(
    analysis: ArgalaAnalysis,
    selectedPlanet: Planet?,
    onSelectPlanet: (Planet) -> Unit,
    language: Language,
    chart: VedicChart
) {
    Column(modifier = Modifier.padding(16.dp)) {
        // Planet selector
        Text(
            stringResource(StringKeyDosha.ARGALA_PLANET_CAUSES),
            fontFamily = CinzelDecorativeFamily,
            fontSize = com.astro.vajra.ui.theme.NeoVedicFontSizes.S14,
            fontWeight = FontWeight.SemiBold,
            color = AppTheme.TextPrimary,
            modifier = Modifier.padding(bottom = 12.dp)
        )

        LazyRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(analysis.planetArgalas.keys.toList()) { planet ->
                val isSelected = selectedPlanet == planet
                val result = analysis.planetArgalas[planet]

                com.astro.vajra.ui.components.common.NeoVedicChoicePill(
                    selected = isSelected,
                    onClick = { onSelectPlanet(planet) },
                    label = {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(planet.symbol, fontFamily = CinzelDecorativeFamily)
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(planet.getLocalizedName(language), fontFamily = PoppinsFontFamily)
                        }
                    },
                    leadingIcon = if (result?.netStrength ?: 0.0 > 0) {
                        {
                            Icon(
                                Icons.Filled.TrendingUp,
                                contentDescription = null,
                                tint = AppTheme.SuccessColor,
                                modifier = Modifier.size(16.dp)
                            )
                        }
                    } else null,
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = AppTheme.getPlanetColor(planet).copy(alpha = 0.15f),
                        selectedLabelColor = AppTheme.getPlanetColor(planet),
                        containerColor = AppTheme.ChipBackground,
                        labelColor = AppTheme.TextSecondary
                    )
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Planet details
        val planetResult = if (selectedPlanet != null) {
            analysis.planetArgalas[selectedPlanet]
        } else {
            // Default to planet with most strength
            val maxStrengthPlanet = analysis.planetArgalas.maxByOrNull { it.value.netStrength }?.key
            analysis.planetArgalas[maxStrengthPlanet]
        }

        planetResult?.let { result ->
            val planet = analysis.planetArgalas.entries.firstOrNull { it.value == result }?.key ?: Planet.SUN
            PlanetArgalaDetailCard(
                planet = planet,
                result = result,
                language = language,
                chart = chart
            )
        }
    }
}

// Fixed ArgalaScreen.kt
@Composable
private fun PlanetArgalaDetailCard(
    planet: Planet,
    result: PlanetArgalaResult,
    language: Language,
    chart: VedicChart
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = AppTheme.CardBackground,
        shape = RoundedCornerShape(com.astro.vajra.ui.theme.NeoVedicTokens.CardCornerRadius),
        border = BorderStroke(NeoVedicTokens.BorderWidth, AppTheme.BorderColor)
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(56.dp)
                        .clip(CircleShape)
                        .background(AppTheme.getPlanetColor(planet).copy(alpha = 0.15f)),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        planet.localizedAbbr(),
                        fontFamily = CinzelDecorativeFamily,
                        fontSize = com.astro.vajra.ui.theme.NeoVedicFontSizes.S24,
                        color = AppTheme.getPlanetColor(planet)
                    )
                }
                
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        planet.getLocalizedName(language),
                        fontFamily = CinzelDecorativeFamily,
                        fontSize = com.astro.vajra.ui.theme.NeoVedicFontSizes.S20,
                        fontWeight = FontWeight.Bold,
                        color = AppTheme.TextPrimary
                    )
                    Text(
                        "Caused by Planet", // Hardcoded fallback for ARGALA_CAUSED_BY_PLANET
                        fontFamily = PoppinsFontFamily,
                        fontSize = com.astro.vajra.ui.theme.NeoVedicFontSizes.S12,
                        color = AppTheme.TextMuted
                    )
                }

                Surface(
                    color = (if (result.netStrength > 0) AppTheme.SuccessColor else AppTheme.WarningColor).copy(alpha = 0.15f),
                    shape = RoundedCornerShape(com.astro.vajra.ui.theme.NeoVedicTokens.ElementCornerRadius)
                ) {
                    Text(
                        "Net: ${String.format("%.1f", result.netStrength)}", // Hardcoded fallback for ARGALA_NET_STRENGTH
                        fontFamily = SpaceGroteskFamily,
                        fontSize = com.astro.vajra.ui.theme.NeoVedicFontSizes.S12,
                        fontWeight = FontWeight.Bold,
                        color = if (result.netStrength > 0) AppTheme.SuccessColor else AppTheme.WarningColor,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                result.interpretation,
                fontFamily = PoppinsFontFamily,
                fontSize = com.astro.vajra.ui.theme.NeoVedicFontSizes.S14,
                color = AppTheme.TextSecondary,
                lineHeight = 22.sp
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Removed affectedHouses block due to Unresolved Reference
        }
    }
}

@Composable
private fun ArgalaInfoDialog(onDismiss: () -> Unit) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = stringResource(StringKeyDosha.ARGALA_ABOUT),
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
                    text = stringResource(StringKeyDosha.ARGALA_ABOUT_DESC),
                    fontFamily = PoppinsFontFamily,
                    fontSize = com.astro.vajra.ui.theme.NeoVedicFontSizes.S14,
                    color = AppTheme.TextSecondary,
                    lineHeight = 22.sp
                )
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = "Source: Jaimini Sutras", // Hardcoded fallback for ARGALA_REF
                    fontFamily = SpaceGroteskFamily,
                    fontSize = com.astro.vajra.ui.theme.NeoVedicFontSizes.S10,
                    color = AppTheme.TextMuted,
                    fontStyle = androidx.compose.ui.text.font.FontStyle.Italic
                )
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text(stringResource(StringKey.BTN_CLOSE), color = AppTheme.AccentGold, fontFamily = SpaceGroteskFamily)
            }
        },
        containerColor = AppTheme.CardBackground
    )
}

// Helper to get house name (1st House, 2nd House etc.)
@Composable
private fun getHouseName(house: Int, language: Language): String {
    return stringResource(StringKeyMatch.HOUSE_LABEL, house)
}
