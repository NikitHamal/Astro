package com.astro.vajra.ui.screen

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
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
import com.astro.vajra.ui.components.common.NeoVedicPageHeader
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.astro.vajra.core.common.Language
import com.astro.vajra.core.common.StringKey
import com.astro.vajra.core.common.StringKeyAnalysis
import com.astro.vajra.core.common.StringKeyDosha
import com.astro.vajra.core.common.StringKeyMatch
import com.astro.vajra.core.common.StringKeyRemedy
import com.astro.vajra.data.localization.currentLanguage
import com.astro.vajra.core.common.getLocalizedName
import com.astro.vajra.data.localization.stringResource
import com.astro.vajra.core.model.Planet
import com.astro.vajra.core.model.VedicChart
import com.astro.vajra.ephemeris.BhriguBinduCalculator
import com.astro.vajra.ephemeris.BhriguBinduCalculator.AspectType
import com.astro.vajra.ephemeris.BhriguBinduCalculator.AreaInfluence
import com.astro.vajra.ephemeris.BhriguBinduCalculator.FactorInfluence
import com.astro.vajra.ephemeris.BhriguBinduCalculator.LifeArea
import com.astro.vajra.ephemeris.BhriguBinduCalculator.OverallStrength
import com.astro.vajra.ephemeris.BhriguBinduCalculator.RemedyCategory
import com.astro.vajra.ephemeris.BhriguBinduCalculator.RemedyPriority
import com.astro.vajra.ui.theme.AppTheme
import com.astro.vajra.ui.theme.NeoVedicTokens
import com.astro.vajra.ui.theme.SpaceGroteskFamily
import com.astro.vajra.ui.theme.CinzelDecorativeFamily
import com.astro.vajra.ui.theme.PoppinsFontFamily
import com.astro.vajra.ui.components.common.ModernPillTabRow
import com.astro.vajra.ui.components.common.TabItem
import com.astro.vajra.ui.components.common.NeoVedicEmptyState
import java.time.DateTimeException
import java.time.LocalDate
import java.time.ZoneId
import java.time.ZoneOffset
import kotlin.math.roundToInt
import androidx.compose.foundation.BorderStroke

/**
 * Bhrigu Bindu Analysis Screen
 *
 * Comprehensive Vedic astrology analysis of the Bhrigu Bindu (BB) point featuring:
 * - Bhrigu Bindu calculation formula and position
 * - BB longitude, sign, nakshatra, pada
 * - House placement and its significance
 * - Sign lord and nakshatra lord
 * - Strength assessment (Excellent/Good/Moderate/Challenging/Difficult)
 * - Aspecting planets and their effects
 * - Conjunct planets
 * - Transit analysis (when transits activate BB)
 * - Karmic significance and life events timing
 * - Recommended remedies
 *
 * Based on Bhrigu Nandi Nadi traditions for timing events and understanding karmic patterns.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BhriguBinduScreen(
    chart: VedicChart?,
    onBack: () -> Unit
) {
    if (chart == null) {
        EmptyChartScreen(
            title = stringResource(StringKeyDosha.BHRIGU_BINDU_TITLE),
            message = stringResource(StringKey.NO_PROFILE_MESSAGE),
            onBack = onBack
        )
        return
    }

    val language = currentLanguage()
    var showInfoDialog by remember { mutableStateOf(false) }
    var selectedTab by remember { mutableIntStateOf(0) }
    var expandedFactor by remember { mutableStateOf<Int?>(null) }
    var expandedLifeArea by remember { mutableStateOf<LifeArea?>(null) }
    val analysisDate = remember(chart) { LocalDate.now(resolveZoneId(chart.birthData.timezone)) }

    val tabs = listOf(
        stringResource(StringKeyMatch.TAB_OVERVIEW),
        stringResource(StringKeyMatch.TAB_ANALYSIS),
        stringResource(StringKeyDosha.BHRIGU_BINDU_TRANSITS),
        stringResource(StringKeyDosha.BHRIGU_BINDU_REMEDIES)
    )

    // Calculate Bhrigu Bindu analysis
    val bbAnalysis = remember(chart, analysisDate) {
        try {
            BhriguBinduCalculator.analyzeBhriguBindu(chart, analysisDate)
        } catch (e: Exception) {
            null
        }
    }

    if (bbAnalysis == null) {
        EmptyChartScreen(
            title = stringResource(StringKeyDosha.BHRIGU_BINDU_TITLE),
            message = stringResource(StringKeyDosha.BHRIGU_BINDU_CALC_ERROR),
            onBack = onBack
        )
        return
    }

    if (showInfoDialog) {
        BhriguBinduInfoDialog(onDismiss = { showInfoDialog = false })
    }

    Scaffold(
        containerColor = AppTheme.ScreenBackground,
        topBar = {
            NeoVedicPageHeader(
                title = stringResource(StringKeyDosha.BHRIGU_BINDU_TITLE),
                subtitle = chart.birthData.name,
                onBack = onBack,
                actions = {
                    IconButton(onClick = { showInfoDialog = true }) {
                        Icon(
                            Icons.Outlined.Info,
                            contentDescription = stringResource(StringKeyDosha.BHRIGU_BINDU_ABOUT),
                            tint = AppTheme.TextPrimary
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(AppTheme.ScreenBackground),
            contentPadding = PaddingValues(bottom = 32.dp)
        ) {
            // Tab selector
            item {
                BhriguBinduTabSelector(
                    tabs = tabs,
                    selectedTab = selectedTab,
                    onTabSelected = { selectedTab = it }
                )
            }

            // Tab content
            when (selectedTab) {
                0 -> item {
                    BhriguBinduOverviewTab(
                        analysis = bbAnalysis,
                        chart = chart,
                        language = language
                    )
                }
                1 -> item {
                    BhriguBinduAnalysisTab(
                        analysis = bbAnalysis,
                        chart = chart,
                        language = language,
                        expandedFactor = expandedFactor,
                        onExpandFactor = { expandedFactor = if (expandedFactor == it) null else it },
                        expandedLifeArea = expandedLifeArea,
                        onExpandLifeArea = { expandedLifeArea = if (expandedLifeArea == it) null else it }
                    )
                }
                2 -> item {
                    BhriguBinduTransitsTab(
                        analysis = bbAnalysis,
                        language = language
                    )
                }
                3 -> item {
                    BhriguBinduRemediesTab(
                        analysis = bbAnalysis,
                        language = language
                    )
                }
            }
        }
    }
}

// ============================================
// UI Components
// ============================================

@Composable
private fun BhriguBinduTabSelector(
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
private fun BhriguBinduOverviewTab(
    analysis: BhriguBinduCalculator.BhriguBinduAnalysis,
    chart: VedicChart,
    language: Language
) {
    Column(modifier = Modifier.padding(16.dp)) {
        // Main BB Position Card
    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = AppTheme.CardBackground,
        shape = RoundedCornerShape(com.astro.vajra.ui.theme.NeoVedicTokens.CardCornerRadius),
        border = BorderStroke(NeoVedicTokens.BorderWidth, AppTheme.BorderColor)
    ) {
            Column(
                modifier = Modifier.padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Title
                Text(
                    stringResource(StringKeyDosha.BHRIGU_BINDU_SUBTITLE),
                    style = MaterialTheme.typography.titleSmall,
                    color = AppTheme.TextMuted
                )

                Spacer(modifier = Modifier.height(12.dp))

                // Longitude display
                Box(
                    modifier = Modifier
                        .size(120.dp)
                        .clip(CircleShape)
                        .background(
                            Brush.radialGradient(
                                colors = listOf(
                                    AppTheme.AccentGold.copy(alpha = 0.2f),
                                    AppTheme.AccentGold.copy(alpha = 0.05f)
                                )
                            )
                        )
                        .border(2.dp, AppTheme.AccentGold.copy(alpha = 0.3f), CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            String.format("%.2f\u00B0", analysis.bhriguBindu),
                            fontFamily = SpaceGroteskFamily,
                            fontSize = com.astro.vajra.ui.theme.NeoVedicFontSizes.S32,
                            fontWeight = FontWeight.Bold,
                            color = AppTheme.AccentGold
                        )
                        Text(
                            stringResource(StringKeyDosha.BHRIGU_BINDU_LONGITUDE),
                            style = MaterialTheme.typography.labelSmall,
                            color = AppTheme.TextMuted
                        )
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                // Calculation formula
                Surface(
                    color = AppTheme.AccentTeal.copy(alpha = 0.1f),
                    shape = RoundedCornerShape(com.astro.vajra.ui.theme.NeoVedicTokens.ElementCornerRadius)
                ) {
                    Column(
                        modifier = Modifier.padding(12.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            stringResource(StringKeyDosha.BHRIGU_BINDU_CALCULATION),
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.Medium,
                            color = AppTheme.AccentTeal
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            "(${String.format("%.2f\u00B0", analysis.rahuLongitude)} + ${String.format("%.2f\u00B0", analysis.moonLongitude)}) / 2",
                            style = MaterialTheme.typography.bodySmall,
                            color = AppTheme.TextSecondary
                        )
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                HorizontalDivider(color = AppTheme.BorderColor.copy(alpha = 0.5f))

                Spacer(modifier = Modifier.height(16.dp))

                // Position details
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    BBInfoChip(
                        label = stringResource(StringKeyDosha.BHRIGU_BINDU_SIGN),
                        value = analysis.bhriguBinduSign.getLocalizedName(language),
                        icon = Icons.Filled.Star
                    )
                    BBInfoChip(
                        label = stringResource(StringKeyDosha.BHRIGU_BINDU_HOUSE),
                        value = analysis.bhriguBinduHouse.toString(),
                        icon = Icons.Filled.Home
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Nakshatra details card
    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = AppTheme.CardElevated,
        shape = RoundedCornerShape(com.astro.vajra.ui.theme.NeoVedicTokens.CardCornerRadius),
        border = BorderStroke(NeoVedicTokens.BorderWidth, AppTheme.BorderColor)
    ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    stringResource(StringKeyDosha.BHRIGU_BINDU_NAKSHATRA),
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.SemiBold,
                    color = AppTheme.TextPrimary
                )
                Spacer(modifier = Modifier.height(12.dp))

                BBDetailRow(
                    label = stringResource(StringKeyDosha.BHRIGU_BINDU_NAKSHATRA),
                    value = analysis.bhriguBinduNakshatra.getLocalizedName(language)
                )
                Spacer(modifier = Modifier.height(8.dp))
                BBDetailRow(
                    label = stringResource(StringKeyDosha.BHRIGU_BINDU_PADA),
                    value = analysis.bhriguBinduPada.toString()
                )
                Spacer(modifier = Modifier.height(8.dp))
                BBDetailRow(
                    label = stringResource(StringKeyDosha.BHRIGU_BINDU_NAKSHATRA_LORD),
                    value = analysis.nakshatraLord.getLocalizedName(language)
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Strength assessment card
        Surface(
            modifier = Modifier.fillMaxWidth(),
            color = getStrengthBackgroundColor(analysis.strengthAssessment.overallStrength),
            shape = RoundedCornerShape(com.astro.vajra.ui.theme.NeoVedicTokens.ElementCornerRadius)
        ) {
            Column(modifier = Modifier.padding(20.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            stringResource(StringKeyDosha.BHRIGU_BINDU_STRENGTH),
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.SemiBold,
                            color = AppTheme.TextPrimary
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            getStrengthText(analysis.strengthAssessment.overallStrength),
                            fontFamily = SpaceGroteskFamily,
                            fontSize = com.astro.vajra.ui.theme.NeoVedicFontSizes.S20,
                            fontWeight = FontWeight.Bold,
                            color = getStrengthColor(analysis.strengthAssessment.overallStrength)
                        )
                    }

                    Icon(
                        getStrengthIcon(analysis.strengthAssessment.overallStrength),
                        contentDescription = null,
                        tint = getStrengthColor(analysis.strengthAssessment.overallStrength),
                        modifier = Modifier.size(48.dp)
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Benefic/Malefic influence
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    InfluenceIndicator(
                        label = stringResource(StringKeyAnalysis.BENEFIC),
                        value = analysis.strengthAssessment.beneficInfluence,
                        color = AppTheme.SuccessColor
                    )
                    InfluenceIndicator(
                        label = stringResource(StringKeyAnalysis.MALEFIC),
                        value = analysis.strengthAssessment.maleficInfluence,
                        color = AppTheme.WarningColor
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Lords card
    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = AppTheme.CardBackground,
        shape = RoundedCornerShape(com.astro.vajra.ui.theme.NeoVedicTokens.CardCornerRadius),
        border = BorderStroke(NeoVedicTokens.BorderWidth, AppTheme.BorderColor)
    ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    stringResource(StringKeyAnalysis.LORDS),
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.SemiBold,
                    color = AppTheme.TextPrimary
                )
                Spacer(modifier = Modifier.height(12.dp))

                PlanetLordRow(
                    label = stringResource(StringKeyDosha.BHRIGU_BINDU_LORD),
                    planet = analysis.bhriguBinduLord,
                    language = language
                )
                Spacer(modifier = Modifier.height(8.dp))
                PlanetLordRow(
                    label = stringResource(StringKeyDosha.BHRIGU_BINDU_NAKSHATRA_LORD),
                    planet = analysis.nakshatraLord,
                    language = language
                )
            }
        }

        // Conjunct planets (if any)
        if (analysis.conjunctPlanets.isNotEmpty()) {
            Spacer(modifier = Modifier.height(16.dp))
            Surface(
                modifier = Modifier.fillMaxWidth(),
                color = AppTheme.CardElevated,
                shape = RoundedCornerShape(com.astro.vajra.ui.theme.NeoVedicTokens.ElementCornerRadius)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            Icons.Filled.Adjust,
                            contentDescription = null,
                            tint = AppTheme.AccentPrimary,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            stringResource(StringKeyDosha.BHRIGU_BINDU_CONJUNCTIONS),
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.SemiBold,
                            color = AppTheme.TextPrimary
                        )
                    }
                    Spacer(modifier = Modifier.height(12.dp))

                    analysis.conjunctPlanets.forEach { planet ->
                        ConjunctPlanetChip(planet = planet, language = language)
                        Spacer(modifier = Modifier.height(8.dp))
                    }
                }
            }
        }
    }
}

@Composable
private fun BhriguBinduAnalysisTab(
    analysis: BhriguBinduCalculator.BhriguBinduAnalysis,
    chart: VedicChart,
    language: Language,
    expandedFactor: Int?,
    onExpandFactor: (Int) -> Unit,
    expandedLifeArea: LifeArea?,
    onExpandLifeArea: (LifeArea) -> Unit
) {
    Column(modifier = Modifier.padding(16.dp)) {
        // Karmic Significance
        Surface(
            modifier = Modifier.fillMaxWidth(),
            color = AppTheme.AccentGold.copy(alpha = 0.08f),
            shape = RoundedCornerShape(com.astro.vajra.ui.theme.NeoVedicTokens.ElementCornerRadius)
        ) {
            Column(modifier = Modifier.padding(20.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        Icons.Filled.AutoAwesome,
                        contentDescription = null,
                        tint = AppTheme.AccentGold,
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(
                        stringResource(StringKeyDosha.BHRIGU_BINDU_KARMIC_SIGNIFICANCE),
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = AppTheme.TextPrimary
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    analysis.interpretation.karmicSignificance,
                    style = MaterialTheme.typography.bodyMedium,
                    color = AppTheme.TextSecondary,
                    lineHeight = 22.sp
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // General Meaning
    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = AppTheme.CardBackground,
        shape = RoundedCornerShape(com.astro.vajra.ui.theme.NeoVedicTokens.CardCornerRadius),
        border = BorderStroke(NeoVedicTokens.BorderWidth, AppTheme.BorderColor)
    ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    stringResource(StringKeyDosha.POSITION_INTERPRETATION),
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.SemiBold,
                    color = AppTheme.TextPrimary
                )
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    analysis.interpretation.generalMeaning,
                    style = MaterialTheme.typography.bodyMedium,
                    color = AppTheme.TextSecondary,
                    lineHeight = 20.sp
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Strength Factors
        Text(
            stringResource(StringKeyDosha.STRENGTH_FACTORS),
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = AppTheme.TextPrimary,
            modifier = Modifier.padding(bottom = 12.dp)
        )

        analysis.strengthAssessment.factors.forEachIndexed { index, factor ->
            StrengthFactorCard(
                factor = factor,
                isExpanded = expandedFactor == index,
                onToggle = { onExpandFactor(index) }
            )
            Spacer(modifier = Modifier.height(8.dp))
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Life Area Influences
        Text(
            stringResource(StringKeyDosha.BHRIGU_BINDU_LIFE_AREAS),
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = AppTheme.TextPrimary,
            modifier = Modifier.padding(bottom = 12.dp)
        )

        analysis.interpretation.lifeAreas.forEach { lifeAreaInfluence ->
            LifeAreaCard(
                lifeAreaInfluence = lifeAreaInfluence,
                isExpanded = expandedLifeArea == lifeAreaInfluence.area,
                onToggle = { onExpandLifeArea(lifeAreaInfluence.area) }
            )
            Spacer(modifier = Modifier.height(8.dp))
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Aspecting Planets
        if (analysis.aspectingPlanets.isNotEmpty()) {
            Text(
                stringResource(StringKeyDosha.BHRIGU_BINDU_ASPECTS),
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = AppTheme.TextPrimary,
                modifier = Modifier.padding(bottom = 12.dp)
            )

            analysis.aspectingPlanets.take(5).forEach { aspectingPlanet ->
                AspectingPlanetCard(aspectingPlanet = aspectingPlanet, language = language)
                Spacer(modifier = Modifier.height(8.dp))
            }
        }
    }
}

@Composable
private fun BhriguBinduTransitsTab(
    analysis: BhriguBinduCalculator.BhriguBinduAnalysis,
    language: Language
) {
    Column(modifier = Modifier.padding(16.dp)) {
        if (analysis.transitAnalysis == null) {
            Surface(
                modifier = Modifier.fillMaxWidth(),
                color = AppTheme.CardBackground,
                shape = RoundedCornerShape(com.astro.vajra.ui.theme.NeoVedicTokens.ElementCornerRadius)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(40.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        Icons.Outlined.Info,
                        contentDescription = null,
                        tint = AppTheme.TextMuted,
                        modifier = Modifier.size(48.dp)
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        stringResource(StringKeyDosha.BHRIGU_BINDU_TRANSIT_NOT_AVAILABLE),
                        style = MaterialTheme.typography.bodyMedium,
                        color = AppTheme.TextMuted,
                        textAlign = TextAlign.Center
                    )
                }
            }
            return
        }

        // Current Transits
        Text(
            stringResource(StringKeyDosha.CURRENT_PLANETARY_POSITIONS),
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = AppTheme.TextPrimary,
            modifier = Modifier.padding(bottom = 12.dp)
        )

        analysis.transitAnalysis.currentTransits.take(6).forEach { transit ->
            TransitCard(transit = transit, language = language)
            Spacer(modifier = Modifier.height(8.dp))
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Upcoming Transits
        if (analysis.transitAnalysis.upcomingTransits.isNotEmpty()) {
            Text(
                stringResource(StringKeyDosha.UPCOMING_TRANSITS),
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = AppTheme.TextPrimary,
                modifier = Modifier.padding(bottom = 12.dp)
            )

            analysis.transitAnalysis.upcomingTransits.forEach { upcomingTransit ->
                UpcomingTransitCard(upcomingTransit = upcomingTransit, language = language)
                Spacer(modifier = Modifier.height(8.dp))
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Transit Interpretation
        Surface(
            modifier = Modifier.fillMaxWidth(),
            color = AppTheme.AccentTeal.copy(alpha = 0.08f),
            shape = RoundedCornerShape(com.astro.vajra.ui.theme.NeoVedicTokens.ElementCornerRadius)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        Icons.Filled.Lightbulb,
                        contentDescription = null,
                        tint = AppTheme.AccentTeal,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        stringResource(StringKeyDosha.BHRIGU_BINDU_TRANSIT_TIMING),
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.SemiBold,
                        color = AppTheme.TextPrimary
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                text = stringResource(StringKeyDosha.TRANSIT_TIMING_DESC),
                    style = MaterialTheme.typography.bodySmall,
                    color = AppTheme.TextSecondary,
                    lineHeight = 18.sp
                )
            }
        }
    }
}

@Composable
private fun BhriguBinduRemediesTab(
    analysis: BhriguBinduCalculator.BhriguBinduAnalysis,
    language: Language
) {
    Column(modifier = Modifier.padding(16.dp)) {
        // Recommendations
        Text(
            stringResource(StringKeyDosha.BHRIGU_BINDU_RECOMMENDATIONS),
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = AppTheme.TextPrimary,
            modifier = Modifier.padding(bottom = 12.dp)
        )

        analysis.interpretation.recommendations.forEach { recommendation ->
            RecommendationCard(recommendation = recommendation)
            Spacer(modifier = Modifier.height(8.dp))
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Remedial Measures
        Text(
            stringResource(StringKeyDosha.BHRIGU_BINDU_REMEDIES),
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = AppTheme.TextPrimary,
            modifier = Modifier.padding(bottom = 12.dp)
        )

        analysis.interpretation.remedialMeasures.forEach { remedy ->
            RemedyCard(remedy = remedy)
            Spacer(modifier = Modifier.height(8.dp))
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Auspicious Days
        Surface(
            modifier = Modifier.fillMaxWidth(),
            color = AppTheme.AccentGold.copy(alpha = 0.08f),
            shape = RoundedCornerShape(com.astro.vajra.ui.theme.NeoVedicTokens.ElementCornerRadius)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        Icons.Filled.CalendarToday,
                        contentDescription = null,
                        tint = AppTheme.AccentGold,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        stringResource(StringKeyDosha.BHRIGU_BINDU_AUSPICIOUS_DAYS),
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.SemiBold,
                        color = AppTheme.TextPrimary
                    )
                }
                Spacer(modifier = Modifier.height(12.dp))

                analysis.interpretation.auspiciousDays.forEach { day ->
                    Row(
                        modifier = Modifier.padding(vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            Icons.Filled.CheckCircle,
                            contentDescription = null,
                            tint = AppTheme.SuccessColor,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            day,
                            style = MaterialTheme.typography.bodyMedium,
                            color = AppTheme.TextSecondary
                        )
                    }
                }
            }
        }
    }
}

// ============================================
// Reusable UI Components
// ============================================

@Composable
private fun BBInfoChip(
    label: String,
    value: String,
    icon: ImageVector
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Box(
            modifier = Modifier
                .size(40.dp)
                .clip(CircleShape)
                .background(AppTheme.AccentPrimary.copy(alpha = 0.1f)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                icon,
                contentDescription = null,
                tint = AppTheme.AccentPrimary,
                modifier = Modifier.size(20.dp)
            )
        }
        Spacer(modifier = Modifier.height(6.dp))
        Text(
            label,
            style = MaterialTheme.typography.labelSmall,
            color = AppTheme.TextSubtle
        )
        Text(
            value,
            style = MaterialTheme.typography.bodySmall,
            fontWeight = FontWeight.Medium,
            color = AppTheme.TextPrimary
        )
    }
}

@Composable
private fun BBDetailRow(label: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            label,
            style = MaterialTheme.typography.bodyMedium,
            color = AppTheme.TextMuted
        )
        Text(
            value,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Medium,
            color = AppTheme.TextPrimary
        )
    }
}

@Composable
private fun PlanetLordRow(label: String, planet: Planet, language: Language) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            label,
            style = MaterialTheme.typography.bodyMedium,
            color = AppTheme.TextMuted
        )
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .size(24.dp)
                    .clip(CircleShape)
                    .background(AppTheme.getPlanetColor(planet).copy(alpha = 0.15f)),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    planet.symbol,
                    style = MaterialTheme.typography.bodySmall,
                    color = AppTheme.getPlanetColor(planet)
                )
            }
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                planet.getLocalizedName(language),
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Medium,
                color = AppTheme.TextPrimary
            )
        }
    }
}

@Composable
private fun ConjunctPlanetChip(planet: Planet, language: Language) {
    Surface(
        color = AppTheme.getPlanetColor(planet).copy(alpha = 0.1f),
        shape = RoundedCornerShape(com.astro.vajra.ui.theme.NeoVedicTokens.ElementCornerRadius)
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                planet.symbol,
                style = MaterialTheme.typography.bodyMedium,
                color = AppTheme.getPlanetColor(planet)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                planet.getLocalizedName(language),
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Medium,
                color = AppTheme.TextPrimary
            )
        }
    }
}

@Composable
private fun InfluenceIndicator(label: String, value: Double, color: Color) {
    Column {
        Text(
            label,
            style = MaterialTheme.typography.labelSmall,
            color = AppTheme.TextMuted
        )
        Spacer(modifier = Modifier.height(4.dp))
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                String.format("%.1f", value),
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Bold,
                color = color
            )
            Spacer(modifier = Modifier.width(4.dp))
            Icon(
                Icons.Filled.Circle,
                contentDescription = null,
                tint = color,
                modifier = Modifier.size(8.dp)
            )
        }
    }
}

@Composable
private fun StrengthFactorCard(
    factor: BhriguBinduCalculator.StrengthFactor,
    isExpanded: Boolean,
    onToggle: () -> Unit
) {
    val rotationAngle by animateFloatAsState(
        targetValue = if (isExpanded) 180f else 0f,
        animationSpec = tween(300),
        label = "rotation"
    )

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick = onToggle
            ),
        color = AppTheme.CardBackground,
        shape = RoundedCornerShape(com.astro.vajra.ui.theme.NeoVedicTokens.CardCornerRadius),
        border = BorderStroke(NeoVedicTokens.BorderWidth, AppTheme.BorderColor)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    modifier = Modifier.weight(1f),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(8.dp)
                            .clip(CircleShape)
                            .background(getInfluenceColor(factor.influence))
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(
                        factor.factor,
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Medium,
                        color = AppTheme.TextPrimary
                    )
                }

                Icon(
                    Icons.Filled.ExpandMore,
                    contentDescription = null,
                    tint = AppTheme.TextMuted,
                    modifier = Modifier
                        .size(24.dp)
                        .rotate(rotationAngle)
                )
            }

            AnimatedVisibility(visible = isExpanded) {
                Column(modifier = Modifier.padding(top = 12.dp)) {
                    HorizontalDivider(color = AppTheme.BorderColor.copy(alpha = 0.5f))
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        factor.description,
                        style = MaterialTheme.typography.bodySmall,
                        color = AppTheme.TextSecondary,
                        lineHeight = 18.sp
                    )
                }
            }
        }
    }
}

@Composable
private fun LifeAreaCard(
    lifeAreaInfluence: BhriguBinduCalculator.LifeAreaInfluence,
    isExpanded: Boolean,
    onToggle: () -> Unit
) {
    val rotationAngle by animateFloatAsState(
        targetValue = if (isExpanded) 180f else 0f,
        animationSpec = tween(300),
        label = "rotation"
    )

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick = onToggle
            ),
        color = AppTheme.CardElevated,
        shape = RoundedCornerShape(com.astro.vajra.ui.theme.NeoVedicTokens.ElementCornerRadius)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    modifier = Modifier.weight(1f),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        getLifeAreaIcon(lifeAreaInfluence.area),
                        contentDescription = null,
                        tint = getAreaInfluenceColor(lifeAreaInfluence.influence),
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(
                        getLifeAreaName(lifeAreaInfluence.area),
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.SemiBold,
                        color = AppTheme.TextPrimary
                    )
                }

                Icon(
                    Icons.Filled.ExpandMore,
                    contentDescription = null,
                    tint = AppTheme.TextMuted,
                    modifier = Modifier
                        .size(24.dp)
                        .rotate(rotationAngle)
                )
            }

            AnimatedVisibility(visible = isExpanded) {
                Column(modifier = Modifier.padding(top = 12.dp)) {
                    HorizontalDivider(color = AppTheme.BorderColor.copy(alpha = 0.5f))
                    Spacer(modifier = Modifier.height(12.dp))

                    Surface(
                        color = getAreaInfluenceColor(lifeAreaInfluence.influence).copy(alpha = 0.1f),
                        shape = RoundedCornerShape(com.astro.vajra.ui.theme.NeoVedicTokens.ElementCornerRadius)
                    ) {
                        Text(
                            getAreaInfluenceName(lifeAreaInfluence.influence),
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.Medium,
                            color = getAreaInfluenceColor(lifeAreaInfluence.influence),
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        lifeAreaInfluence.description,
                        style = MaterialTheme.typography.bodySmall,
                        color = AppTheme.TextSecondary,
                        lineHeight = 18.sp
                    )
                }
            }
        }
    }
}

@Composable
private fun AspectingPlanetCard(
    aspectingPlanet: BhriguBinduCalculator.AspectingPlanet,
    language: Language
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = AppTheme.CardBackground,
        shape = RoundedCornerShape(com.astro.vajra.ui.theme.NeoVedicTokens.ElementCornerRadius)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(AppTheme.getPlanetColor(aspectingPlanet.planet).copy(alpha = 0.15f)),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    aspectingPlanet.planet.symbol,
                    style = MaterialTheme.typography.bodyLarge,
                    color = AppTheme.getPlanetColor(aspectingPlanet.planet)
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    aspectingPlanet.planet.getLocalizedName(language),
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = AppTheme.TextPrimary
                )
                Text(
                    getAspectTypeName(aspectingPlanet.aspectType),
                    style = MaterialTheme.typography.bodySmall,
                    color = AppTheme.TextMuted
                )
            }

            Column(horizontalAlignment = Alignment.End) {
                Text(
                    "${(aspectingPlanet.aspectStrength * 100).toInt()}%",
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Bold,
                    color = getAspectTypeColor(aspectingPlanet.aspectType)
                )
                if (aspectingPlanet.isApplying) {
                    Text(
                        stringResource(StringKeyDosha.APPLYING),
                        style = MaterialTheme.typography.labelSmall,
                        color = AppTheme.AccentTeal
                    )
                }
            }
        }
    }
}

@Composable
private fun TransitCard(
    transit: BhriguBinduCalculator.TransitingPlanet,
    language: Language
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = if (transit.isConjunct)
                AppTheme.AccentGold.copy(alpha = 0.08f)
            else
                AppTheme.CardBackground,
        shape = RoundedCornerShape(com.astro.vajra.ui.theme.NeoVedicTokens.ElementCornerRadius)
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
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(AppTheme.getPlanetColor(transit.planet).copy(alpha = 0.15f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            transit.planet.symbol,
                            style = MaterialTheme.typography.bodyMedium,
                            color = AppTheme.getPlanetColor(transit.planet)
                        )
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(
                            transit.planet.getLocalizedName(language),
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.SemiBold,
                            color = AppTheme.TextPrimary
                        )
                        Text(
                            String.format("%.2f\u00B0 from BB", transit.distanceFromBB),
                            style = MaterialTheme.typography.bodySmall,
                            color = AppTheme.TextMuted
                        )
                    }
                }

                if (transit.isConjunct) {
                    Surface(
                        color = AppTheme.AccentGold.copy(alpha = 0.2f),
                        shape = RoundedCornerShape(com.astro.vajra.ui.theme.NeoVedicTokens.ElementCornerRadius)
                    ) {
                        Text(
                            stringResource(StringKeyDosha.CONJUNCT),
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.Bold,
                            color = AppTheme.AccentGold,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                        )
                    }
                }
            }

            if (transit.effectDescription.isNotEmpty()) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    transit.effectDescription,
                    style = MaterialTheme.typography.bodySmall,
                    color = AppTheme.TextSecondary,
                    lineHeight = 18.sp
                )
            }
        }
    }
}

@Composable
private fun UpcomingTransitCard(
    upcomingTransit: BhriguBinduCalculator.UpcomingTransit,
    language: Language
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = AppTheme.CardElevated,
        shape = RoundedCornerShape(com.astro.vajra.ui.theme.NeoVedicTokens.ElementCornerRadius)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        upcomingTransit.planet.getLocalizedName(language),
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.SemiBold,
                        color = AppTheme.TextPrimary
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        stringResource(StringKeyDosha.CONJUNCTION),
                        style = MaterialTheme.typography.labelSmall,
                        color = AppTheme.AccentGold
                    )
                }
                Text(
                    upcomingTransit.transitDate.toString(),
                    style = MaterialTheme.typography.bodySmall,
                    color = AppTheme.TextMuted
                )
            }
            Text(
                upcomingTransit.effect,
                style = MaterialTheme.typography.bodySmall,
                color = AppTheme.TextSecondary,
                modifier = Modifier.weight(1.5f),
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

@Composable
private fun RecommendationCard(recommendation: String) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = AppTheme.CardBackground,
        shape = RoundedCornerShape(com.astro.vajra.ui.theme.NeoVedicTokens.ElementCornerRadius)
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.Top
        ) {
            Icon(
                Icons.Outlined.Lightbulb,
                contentDescription = null,
                tint = AppTheme.AccentGold,
                modifier = Modifier.size(16.dp)
            )
            Spacer(modifier = Modifier.width(12.dp))
            Text(
                recommendation,
                style = MaterialTheme.typography.bodySmall,
                color = AppTheme.TextSecondary
            )
        }
    }
}

@Composable
private fun RemedyCard(remedy: BhriguBinduCalculator.Remedy) {
    val priorityColor = when (remedy.priority) {
        RemedyPriority.HIGH -> AppTheme.ErrorColor
        RemedyPriority.MEDIUM -> AppTheme.WarningColor
        RemedyPriority.LOW -> AppTheme.SuccessColor
    }

    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = AppTheme.CardElevated,
        shape = RoundedCornerShape(com.astro.vajra.ui.theme.NeoVedicTokens.ElementCornerRadius),
        border = BorderStroke(1.dp, priorityColor.copy(alpha = 0.3f))
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    remedy.category.name.replace("_", " "),
                    style = MaterialTheme.typography.labelSmall,
                    color = AppTheme.TextMuted
                )
                Surface(
                    color = priorityColor.copy(alpha = 0.1f),
                    shape = RoundedCornerShape(4.dp)
                ) {
                    Text(
                        remedy.priority.name,
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                        style = MaterialTheme.typography.labelSmall,
                        color = priorityColor
                    )
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                remedy.description,
                style = MaterialTheme.typography.bodyMedium,
                color = AppTheme.TextPrimary
            )
        }
    }
}

// ============================================
// Helper Functions
// ============================================

@Composable
private fun getStrengthColor(strength: OverallStrength): Color {
    return when (strength) {
        OverallStrength.EXCELLENT -> AppTheme.SuccessColor
        OverallStrength.GOOD -> AppTheme.AccentTeal
        OverallStrength.MODERATE -> AppTheme.AccentPrimary
        OverallStrength.CHALLENGING -> AppTheme.WarningColor
        OverallStrength.DIFFICULT -> AppTheme.ErrorColor
    }
}

@Composable
private fun getStrengthBackgroundColor(strength: OverallStrength): Color {
    return getStrengthColor(strength).copy(alpha = 0.08f)
}

@Composable
private fun getStrengthText(strength: OverallStrength): String {
    return when (strength) {
        OverallStrength.EXCELLENT -> stringResource(StringKeyAnalysis.STRENGTH_EXCELLENT)
        OverallStrength.GOOD -> stringResource(StringKeyAnalysis.STRENGTH_GOOD)
        OverallStrength.MODERATE -> stringResource(StringKeyAnalysis.STRENGTH_MODERATE)
        OverallStrength.CHALLENGING -> stringResource(StringKeyAnalysis.STRENGTH_WEAK)
        OverallStrength.DIFFICULT -> stringResource(StringKeyAnalysis.STRENGTH_WEAK)
    }
}

@Composable
private fun getStrengthIcon(strength: OverallStrength): ImageVector {
    return when (strength) {
        OverallStrength.EXCELLENT, OverallStrength.GOOD -> Icons.Filled.TrendingUp
        OverallStrength.MODERATE -> Icons.Filled.Remove
        OverallStrength.CHALLENGING, OverallStrength.DIFFICULT -> Icons.Filled.TrendingDown
    }
}

@Composable
private fun getInfluenceColor(influence: FactorInfluence): Color {
    return when (influence) {
        FactorInfluence.POSITIVE -> AppTheme.SuccessColor
        FactorInfluence.NEUTRAL -> AppTheme.TextMuted
        FactorInfluence.NEGATIVE -> AppTheme.ErrorColor
    }
}

@Composable
private fun getAreaInfluenceColor(influence: AreaInfluence): Color {
    return when (influence) {
        AreaInfluence.FAVORABLE -> AppTheme.SuccessColor
        AreaInfluence.MIXED -> AppTheme.AccentGold
        AreaInfluence.CHALLENGING -> AppTheme.ErrorColor
    }
}

@Composable
private fun getAreaInfluenceName(influence: AreaInfluence): String {
    return when (influence) {
        AreaInfluence.FAVORABLE -> stringResource(StringKeyAnalysis.BENEFIC)
        AreaInfluence.MIXED -> stringResource(StringKeyAnalysis.ARGALA_MIXED)
        AreaInfluence.CHALLENGING -> stringResource(StringKeyAnalysis.MALEFIC)
    }
}

@Composable
private fun getLifeAreaIcon(area: LifeArea): ImageVector {
    return when (area) {
        LifeArea.CAREER -> Icons.Outlined.Work
        LifeArea.RELATIONSHIPS -> Icons.Outlined.Favorite
        LifeArea.HEALTH -> Icons.Outlined.LocalHospital
        LifeArea.FINANCE -> Icons.Outlined.AttachMoney
        LifeArea.SPIRITUALITY -> Icons.Outlined.SelfImprovement
        LifeArea.FAMILY -> Icons.Outlined.FamilyRestroom
    }
}

@Composable
private fun getLifeAreaName(area: LifeArea): String {
    return when (area) {
        LifeArea.CAREER -> stringResource(StringKeyDosha.ARUDHA_CAREER)
        LifeArea.RELATIONSHIPS -> stringResource(StringKeyDosha.ARUDHA_RELATIONSHIPS)
        LifeArea.HEALTH -> stringResource(StringKeyRemedy.REMEDY_CAT_HEALTH)
        LifeArea.FINANCE -> stringResource(StringKeyDosha.ARUDHA_GAINS)
        LifeArea.SPIRITUALITY -> stringResource(StringKey.CATEGORY_REMEDIAL)
        LifeArea.FAMILY -> stringResource(StringKeyDosha.KUJA_DOSHA_DOMESTIC) // Approximate
    }
}

@Composable
private fun getAspectTypeName(type: AspectType): String {
    return when (type) {
        AspectType.CONJUNCTION -> stringResource(StringKeyDosha.CONJUNCTION)
        AspectType.OPPOSITION -> stringResource(StringKeyDosha.OPPOSITION)
        AspectType.TRINE -> stringResource(StringKeyDosha.TRINE)
        AspectType.SQUARE -> stringResource(StringKeyDosha.SQUARE)
        AspectType.SEXTILE -> stringResource(StringKeyDosha.SEXTILE)
    }
}

@Composable
private fun getAspectTypeColor(type: AspectType): Color {
    return when (type) {
        AspectType.CONJUNCTION, AspectType.TRINE, AspectType.SEXTILE -> AppTheme.SuccessColor
        AspectType.OPPOSITION, AspectType.SQUARE -> AppTheme.WarningColor
    }
}

@Composable
private fun BhriguBinduInfoDialog(onDismiss: () -> Unit) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = stringResource(StringKeyDosha.BHRIGU_BINDU_ABOUT),
                fontWeight = FontWeight.SemiBold,
                color = AppTheme.TextPrimary
            )
        },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                Text(
                    text = stringResource(StringKeyDosha.BHRIGU_BINDU_INFO_1),
                    style = MaterialTheme.typography.bodyMedium,
                    color = AppTheme.TextSecondary
                )
                Text(
                    text = stringResource(StringKeyDosha.BHRIGU_BINDU_INFO_2),
                    style = MaterialTheme.typography.bodyMedium,
                    color = AppTheme.TextSecondary
                )
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text(stringResource(StringKeyDosha.BTN_GOT_IT), color = AppTheme.AccentPrimary)
            }
        },
        containerColor = AppTheme.CardBackground
    )
}
